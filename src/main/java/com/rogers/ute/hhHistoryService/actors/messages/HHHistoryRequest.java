package com.rogers.ute.hhHistoryService.actors.messages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rogers.ute.asyncservices.soap.RequestHeader;
import com.rogers.ute.logging.ProcessingContext;

import java.util.Date;

public class HHHistoryRequest {

    private final String ban;
    private final String ctn;
    private final RequestHeader requestHeader;
    @JsonIgnore
    private final ProcessingContext processingContext;

    public HHHistoryRequest(String ban, String ctn, RequestHeader requestHeader, ProcessingContext processingContext) {
        this.ban = ban;
        this.ctn = ctn;
        this.requestHeader = requestHeader;
        this.processingContext = processingContext;
    }

    public String getBan() {
        return ban;
    }

    public String getCtn() {
        return ctn;
    }

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public ProcessingContext getProcessingContext() {
        return processingContext;
    }
}
