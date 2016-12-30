package com.rogers.ute.hhHistoryService.actors.frontend;

import akka.actor.*;
import akka.japi.Creator;
import akka.japi.pf.ReceiveBuilder;
import com.rogers.ute.commons.exceptions.HttpAwareUteException;
import com.rogers.ute.hhHistoryService.actors.messages.HHHistoryRequest;
import com.rogers.ute.hhHistoryService.common.CameoStart;
import com.rogers.ute.hhHistoryService.domain.HHHistoryData;
import com.rogers.ute.hhHistoryService.exception.HHHistoryServiceErrorCodes;
import com.rogers.ute.logging.LoggerWrapper;
import scala.PartialFunction;
import scala.concurrent.duration.FiniteDuration;
import scala.runtime.BoxedUnit;

import java.util.concurrent.TimeUnit;

public class HHHistoryCameoActor extends AbstractLoggingActor {
    private static final LoggerWrapper logger = LoggerWrapper.slf4jLogger(HHHistoryCameoActor.class);
    private final HHHistoryRequest request;
    private final ActorRef replyTo;
    private final ActorRef cassandraReaderActor;

    public static void hhHistory(ActorRefFactory factory, HHHistoryRequest request, ActorRef replyTo, ActorRef cassandraReaderActor) {

        Props props = Props.create(new Creator<HHHistoryCameoActor>() {
            private static final long serialVersionUID = 6751118647984022135L;

            @Override
            public HHHistoryCameoActor create() throws Exception {
                return new HHHistoryCameoActor(request, replyTo, cassandraReaderActor);
            }
        });
        ActorRef actor = factory.actorOf(props);
        actor.tell(CameoStart.INSTANCE, replyTo);
    }

    public HHHistoryCameoActor(HHHistoryRequest request, ActorRef replyTo, ActorRef cassandraReaderActor) {
        this.request = request;
        this.replyTo = replyTo;
        this.cassandraReaderActor = cassandraReaderActor;

        context().setReceiveTimeout(FiniteDuration.create(10000, TimeUnit.MILLISECONDS));

        receive(ReceiveBuilder
                .matchEquals(CameoStart.INSTANCE, message -> {
                        context().become(waitingForHHHistoryDeatails());
                        cassandraReaderActor.tell(request, self());
                })
                .match(ReceiveTimeout.class, message -> processTimeout("Timeout while fetching HHEntitlement for CTN: ", request, HHHistoryServiceErrorCodes.REQUEST_TIMED_OUT))
                .build()
        );
    }

    private PartialFunction<Object, BoxedUnit> waitingForHHHistoryDeatails() {
        return ReceiveBuilder
                .match(HHHistoryData.class, message -> {
                        HHHistoryData hhHistoryData = (HHHistoryData) message;
                        replyTo.tell(hhHistoryData, self());
                        context().stop(self());
                })
                .match(ReceiveTimeout.class, message -> {
                    processTimeout("Timeout while fetching HH History Data from backend by ctn: ",
                            request, HHHistoryServiceErrorCodes.BACKEND_TIMEOUT);
                })
                .build();
    }

    private void processTimeout(String errorMessage, HHHistoryRequest request, HHHistoryServiceErrorCodes errorCode) {
        log().error("{} - {} {}", "", errorMessage, request.getCtn());
        if (true) {
            HttpAwareUteException exception = HttpAwareUteException.serviceUnavailable(errorMessage + request.getCtn(),
                    errorCode);
            replyTo.tell(new Status.Failure(exception), self());
        }
        context().stop(self());
    }
}
