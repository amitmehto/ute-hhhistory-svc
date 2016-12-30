package com.rogers.ute.hhHistoryService.actors.frontend;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.ActorRefFactory;
import akka.actor.Props;
import akka.japi.Creator;
import akka.japi.pf.ReceiveBuilder;
import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.rogers.ute.billingaccountsvc.domain.account.BillingInfo;
import com.rogers.ute.billingaccountsvc.services.BillingAccountServiceCachable;
import com.rogers.ute.commons.exceptions.HttpAwareUteException;
import com.rogers.ute.hhHistoryService.actors.messages.HHHistoryRequest;
import com.rogers.ute.hhHistoryService.cassandra.CassandraConfig;
import com.rogers.ute.hhHistoryService.cassandra.CassandraSessionProvider;
import com.rogers.ute.hhHistoryService.domain.HHBill;
import com.rogers.ute.hhHistoryService.domain.HHHistoryData;
import com.rogers.ute.hhHistoryService.domain.HHSession;
import com.rogers.ute.hhHistoryService.exception.HHErrorConstants;
import com.rogers.ute.hhHistoryService.exception.HHHistoryServiceErrorCodes;
import play.libs.Json;
import play.mvc.Http;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

public class CassandraReaderActor extends AbstractLoggingActor {

    public static final String NAME = "hh_cassandra_reader";

    //
    private final CassandraConfig cassandraConfig;
    private final Session session;
    private final BillingAccountServiceCachable billingAccountService;

    public static ActorRef create(ActorRefFactory actorRefFactory, CassandraConfig cassandraConfig, Session session, BillingAccountServiceCachable billingAccountService) {
        Props props = Props.create(new Creator<CassandraReaderActor>() {
            private static final long serialVersionUID = 4953414852651744012L;

            @Override
            public CassandraReaderActor create() throws Exception {
                return new CassandraReaderActor(cassandraConfig, session, billingAccountService);
            }
        });
        return actorRefFactory.actorOf(props, NAME);
    }

    public CassandraReaderActor(CassandraConfig cassandraConfig, Session session, BillingAccountServiceCachable billingAccountService) {
        this.cassandraConfig = cassandraConfig;
        this.session = session;
        this.billingAccountService = billingAccountService;

        receive(ReceiveBuilder
                .match(HHHistoryRequest.class, message ->
                        getHHHistoryDetails(message)
                )
                .build());
    }

    private void getHHHistoryDetails(HHHistoryRequest message) {
        HHHistoryData hhHistoryData = new HHHistoryData();
        Statement stmt = hhHistoryStatement("ute",message.getCtn(), message.getBan());
        System.out.println("statement :: "+stmt);
        ActorRef sender = context().sender();
        ActorRef self = self();
        billingAccountService.getBillingDetails(message.getProcessingContext(),message.getRequestHeader(),message.getBan(),10000).whenComplete((billingDetailsResp, t) -> {
            if(t == null) {
                BillingInfo billingInfo = billingDetailsResp.getData();
                String billStartDate = billingInfo.getCurrentBillCycleStartDate();/*"12/14/2016";*/
                String billEndDate = billingInfo.getCurrentBillCycleEndDate();/*"12/16/2016";*/
                String ban = billingInfo.getAccountNumber();/*"ban6";*/
                ResultSetFuture resultSetFuture = session.executeAsync(stmt);
                resultSetFuture.addListener(() -> {
                    try {
                        List<HHSession> hhSessionList = new ArrayList<HHSession>();
                        List<HHBill> hhBillList = new ArrayList<HHBill>();
                        HHBill hhBill = new HHBill();
                        ResultSet resultSet = resultSetFuture.get();
                        resultSet.forEach(row -> {
                            System.out.println("Row :: "+row.toString());
                            Date startTime = row.getTimestamp("starttime");
                            Date endTime = row.getTimestamp("endtime");
                            XMLGregorianCalendar startTimeValue = convertToXMLGCFormat(startTime);
                            XMLGregorianCalendar endTimeValue = convertToXMLGCFormat(endTime);
                            if(ban.equalsIgnoreCase(message.getBan())) {
                                String billStartDateValue = null, billEndDateValue = null;
                                Date startDt = null, endDt = null;
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                if (billStartDate != null && billEndDate != null) {
                                    System.out.println("inside null check :: ");
                                    startDt = convertStringToDate(billStartDate);
                                    endDt = convertStringToDate(billEndDate);
                                    billStartDateValue = sdf.format(startDt);
                                    billEndDateValue = sdf.format(endDt);
                                    System.out.println("startTime :: " + startTime + " :: after start date :: " + startDt + " :: before end date ::" + endDt);
                                    if (startTime.after(startDt) && startTime.before(endDt)) {
                                        System.out.println("inside session list");
                                        HHSession hhSession = new HHSession();
                                        hhSession.setEndTime(endTimeValue.toString());
                                        hhSession.setStartTime(startTimeValue.toString());
                                        hhSession.setSessionUsage(Integer.toString((Integer) row.getInt("sessionusage")));
                                        hhSessionList.add(hhSession);
                                    }
                                    hhBill.setBillPeriodEndDate(billEndDateValue);
                                    hhBill.setBillPeriodStartDate(billStartDateValue);
                                    hhBill.setBillingPeriodNo("0");
                                }
                            }
                        });
                        hhBill.setHhSessionList(hhSessionList);
                        if(hhSessionList != null && hhSessionList.size() > 0) {
                            hhBillList.add(hhBill);
                            hhHistoryData.setBillingPeriodList(hhBillList);
                            hhHistoryData.setResultCode(HHErrorConstants.SUCCESS_CODE);
                            hhHistoryData.setResultCodeDesc(HHErrorConstants.SUCCESS_DESC);
                            sender.tell(hhHistoryData,self);
                        } else {
                            hhHistoryData.setResultCode(HHErrorConstants.NO_HISTORY_DATA_CODE);
                            hhHistoryData.setResultCodeDesc(HHErrorConstants.NO_HISTORY_DATA_DESC);
                            sender.tell(hhHistoryData,self);
                        }
                    }  catch (Throwable e) {
                        log().error("{} - {} failed", message.getProcessingContext(), "Getting HHistory Data");
                        HttpAwareUteException exception = HttpAwareUteException.internalServerError("Getting HHistory Data" + " failed", e,
                                HHHistoryServiceErrorCodes.FETCHING_FROM_CACHE_FAILED);
                        hhHistoryData.setException(exception);
                        sender.tell(hhHistoryData, self);
                    }

                }, context().dispatcher());
            }
            else{
              hhHistoryData.setException(t);
              sender.tell(hhHistoryData, self);
            }
        });

    }

    private  Statement hhHistoryStatement(String keyspace, String ctn, String ban) {
        return QueryBuilder.select()
                .all()
                .from(keyspace, "HHHistory")
                .where(eq("ctn", ctn)).and(eq("ban",ban)).and(eq("status","active"));
    }

    private XMLGregorianCalendar convertToXMLGCFormat(Date date) {
        GregorianCalendar gcStart = new GregorianCalendar();
        XMLGregorianCalendar xmlGregorianCalendar = null;
        gcStart.setTime(date);
        try {
            xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcStart);
            xmlGregorianCalendar.setTimezone(DatatypeConstants.FIELD_UNDEFINED);

        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        return xmlGregorianCalendar;
    }

    private long getIfModifiedSince(Http.Request request) {
        String ifModifiedSince = request.getHeader("IF_MODIFIED_SINCE");
        if (ifModifiedSince == null) {
            return 0L;
        }
        return Long.valueOf(ifModifiedSince);
    }

    private Date convertStringToDate(String date) {
        System.out.println("date :: "+date);
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date expectedDate = null;
        try {
            expectedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            System.out.println("parse exception");
            return null;
        }
        System.out.println("expected date :: "+expectedDate);
        return expectedDate;

    }
}
