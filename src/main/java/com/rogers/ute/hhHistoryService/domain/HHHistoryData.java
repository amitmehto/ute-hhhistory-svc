package com.rogers.ute.hhHistoryService.domain;



import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class HHHistoryData {
    private List<HHBill> billingPeriodList;
    private String resultCode;
    private String resultCodeDesc;
    @JsonIgnore
    private Throwable exception;

    public HHHistoryData(){}

    public HHHistoryData(List<HHBill> billingPeriodList, String resultCode) {
        this.billingPeriodList = billingPeriodList;
        this.resultCode = resultCode;
    }

    public List<HHBill> getBillingPeriodList() {
        return billingPeriodList;
    }

    public void setBillingPeriodList(List<HHBill> billingPeriodList) {
        this.billingPeriodList = billingPeriodList;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultCodeDesc() {
        return resultCodeDesc;
    }

    public void setResultCodeDesc(String resultCodeDesc) {
        this.resultCodeDesc = resultCodeDesc;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }
}
