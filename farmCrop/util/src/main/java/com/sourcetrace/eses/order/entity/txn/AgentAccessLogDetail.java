package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;

public class AgentAccessLogDetail {
    private long id;
    private AgentAccessLog agentAccessLog;
    private String txnType;
    private String txnMode;
    private String messageNumber;
    private Long txnCount;
    private String servicePoint;
    private Date txnDate;

    public long getId() {

        return id;
    }

    public void setId(long id) {

        this.id = id;
    }

    public AgentAccessLog getAgentAccessLog() {

        return agentAccessLog;
    }

    public void setAgentAccessLog(AgentAccessLog agentAccessLog) {

        this.agentAccessLog = agentAccessLog;
    }

    public String getTxnType() {

        return txnType;
    }

    public void setTxnType(String txnType) {

        this.txnType = txnType;
    }

    public String getTxnMode() {

        return txnMode;
    }

    public void setTxnMode(String txnMode) {

        this.txnMode = txnMode;
    }

    public String getMessageNumber() {

        return messageNumber;
    }

    public void setMessageNumber(String messageNumber) {

        this.messageNumber = messageNumber;
    }

    public Long getTxnCount() {

        return txnCount;
    }

    public void setTxnCount(Long txnCount) {

        this.txnCount = txnCount;
    }

    public String getServicePoint() {

        return servicePoint;
    }

    public void setServicePoint(String servicePoint) {

        this.servicePoint = servicePoint;
    }

    public Date getTxnDate() {
    
        return txnDate;
    }

    public void setTxnDate(Date txnDate) {
    
        this.txnDate = txnDate;
    }
    
    
}
