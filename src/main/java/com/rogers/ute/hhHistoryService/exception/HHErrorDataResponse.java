package com.rogers.ute.hhHistoryService.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HHErrorDataResponse {
    @JsonProperty("resultCode")
    private String resultcode;
    @JsonProperty("resultDesc")
    private String resultDesc;

    public HHErrorDataResponse() {
    }

    public HHErrorDataResponse(String resultcode, String resultDesc) {
        this.resultcode = resultcode;
        this.resultDesc = resultDesc;
    }

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }
}
