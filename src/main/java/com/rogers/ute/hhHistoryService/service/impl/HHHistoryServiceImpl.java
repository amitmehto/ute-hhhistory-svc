package com.rogers.ute.hhHistoryService.service.impl;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import com.rogers.ute.asyncservices.soap.RequestHeader;
import com.rogers.ute.hhHistoryService.actors.messages.HHHistoryRequest;
import com.rogers.ute.hhHistoryService.domain.HHHistoryData;
import com.rogers.ute.hhHistoryService.service.HHHistoryService;
import com.rogers.ute.logging.LoggerWrapper;
import com.rogers.ute.logging.ProcessingContext;
import scala.compat.java8.FutureConverters;
import scala.concurrent.Future;

import java.util.concurrent.CompletionStage;

public class HHHistoryServiceImpl implements HHHistoryService {
    private static final LoggerWrapper logger = LoggerWrapper.slf4jLogger(HHHistoryServiceImpl.class);

    private final ActorRef hhHistoryServiceActor;

    public HHHistoryServiceImpl(ActorRef hhHistoryServiceActor) {
        this.hhHistoryServiceActor = hhHistoryServiceActor;
    }

    public CompletionStage<HHHistoryData> hhHistory(ProcessingContext processingContext, RequestHeader requestHeader,
                                                    String ctn, String ban, long ifModifiedSinceTimestamp){
        logger.debug(processingContext, "HHHistoryServiceImpl :: hhHistory() : BEGIN");
        Future<Object> future = Patterns.ask(hhHistoryServiceActor, new HHHistoryRequest(ban,ctn,requestHeader,processingContext), 10000);
        return FutureConverters.toJava(future).thenApply(response -> {return (HHHistoryData) response;});
    }
}
