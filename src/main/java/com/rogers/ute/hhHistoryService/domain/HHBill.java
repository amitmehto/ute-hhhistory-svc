package com.rogers.ute.hhHistoryService.domain;

import java.util.List;

public class HHBill {
    private String billingPeriodNo;
    private String billPeriodStartDate;
    private String billPeriodEndDate;
    private List<HHSession> hhSessionList;

    public HHBill(){}

    public HHBill(String billingPeriodNo, String billPeriodStartDate, String billPeriodEndDate, List<HHSession> hhSessionList) {
        this.billingPeriodNo = billingPeriodNo;
        this.billPeriodStartDate = billPeriodStartDate;
        this.billPeriodEndDate = billPeriodEndDate;
        this.hhSessionList = hhSessionList;
    }

    public String getBillingPeriodNo() {
        return billingPeriodNo;
    }

    public void setBillingPeriodNo(String billingPeriodNo) {
        this.billingPeriodNo = billingPeriodNo;
    }

    public String getBillPeriodStartDate() {
        return billPeriodStartDate;
    }

    public void setBillPeriodStartDate(String billPeriodStartDate) {
        this.billPeriodStartDate = billPeriodStartDate;
    }

    public String getBillPeriodEndDate() {
        return billPeriodEndDate;
    }

    public void setBillPeriodEndDate(String billPeriodEndDate) {
        this.billPeriodEndDate = billPeriodEndDate;
    }

    public List<HHSession> getHhSessionList() {
        return hhSessionList;
    }

    public void setHhSessionList(List<HHSession> hhSessionList) {
        this.hhSessionList = hhSessionList;
    }
}
