package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;

public class PaymentLedger {

    private long id;
    private Date createdDate;
    private String createdUser;
    private String type;
    private String refId;
    private double prevValue;
    private double txnValue;
    private double newValue;

    public long getId() {

        return id;
    }

    public void setId(long id) {

        this.id = id;
    }

    public Date getCreatedDate() {

        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {

        this.createdDate = createdDate;
    }

    public String getCreatedUser() {

        return createdUser;
    }

    public void setCreatedUser(String createdUser) {

        this.createdUser = createdUser;
    }

    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    public String getRefId() {

        return refId;
    }

    public void setRefId(String refId) {

        this.refId = refId;
    }

    public double getPrevValue() {

        return prevValue;
    }

    public void setPrevValue(double prevValue) {

        this.prevValue = prevValue;
    }

    public double getTxnValue() {

        return txnValue;
    }

    public void setTxnValue(double txnValue) {

        this.txnValue = txnValue;
    }

    public double getNewValue() {

        return newValue;
    }

    public void setNewValue(double newValue) {

        this.newValue = newValue;
    }

}
