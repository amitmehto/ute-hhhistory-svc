package com.rogers.ute.hhHistoryService.service;

import com.rogers.ute.asyncservices.soap.RequestHeader;
import com.rogers.ute.hhHistoryService.domain.HHHistoryData;
import com.rogers.ute.logging.ProcessingContext;

import java.util.concurrent.CompletionStage;

public interface HHHistoryService {
    CompletionStage<HHHistoryData> hhHistory(ProcessingContext processingContext, RequestHeader requestHeader,
                                                 String ctn, String ban, long ifModifiedSinceTimestamp);
}
