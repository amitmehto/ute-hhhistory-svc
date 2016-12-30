package com.rogers.ute.hhHistoryService.actors.frontend;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.ActorRefFactory;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.rogers.ute.hhHistoryService.actors.messages.HHHistoryRequest;

public class HHHistoryServiceActor extends AbstractLoggingActor {
    private static final String NAME = "hhhistory_service";

    public static ActorRef create(ActorRefFactory actorRefFactory, ActorRef cassandraReaderActor) {
        Props props = Props.create(HHHistoryServiceActor.class, ()->{return new HHHistoryServiceActor(cassandraReaderActor);});
        return actorRefFactory.actorOf(props, NAME);
    }

    private HHHistoryServiceActor(ActorRef cassandraReaderActor){
        receive(ReceiveBuilder
                .match(HHHistoryRequest.class, (message)->{
                    HHHistoryCameoActor.hhHistory(context(), message, sender(), cassandraReaderActor);
                })
                .build());
    }
}
