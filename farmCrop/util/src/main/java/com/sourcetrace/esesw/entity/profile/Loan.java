/*
 * Loan.java
 * Copyright (c) 2008, Source Trace Systems
 * ALL RIGHTS RESERVED
 */
package com.sourcetrace.esesw.entity.profile;

import java.util.Date;
import java.util.Map;


/**
 * The Class Loan.
 * @author $Author: aravind $
 * @version $Rev: 59 $, $Date: 2009-08-04 17:10:51 +0530 (Tue, 04 Aug 2009) $
 */
public class Loan {

    /**
	 * The id.
	 */
    private long id;

    /**
	 * The loan number.
	 */
    private String loanNumber;

    /**
	 * The loan amount.
	 */
    private double loanAmount;

    /**
	 * The payment mode.
	 */
    private String paymentMode;

    /**
	 * The monthly payment.
	 */
    private String monthlyPayment;

    /**
	 * The date.
	 */
    private Date date;

    /**
	 * The type.
	 */
    private int type;

    /**
	 * The status.
	 */
    private int status;

    /**
	 * The client.
	 */
    private Client client;

    /**
	 * The form data.
	 */
    private Map<String, String> formData;


    /**
	 * Gets the client.
	 * @return the client
	 */
    public Client getClient() {
        return client;
    }

    /**
	 * Sets the client.
	 * @param client the new client
	 */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
	 * Gets the id.
	 * @return the id
	 */
    public long getId() {
        return id;
    }

    /**
	 * Sets the id.
	 * @param id the new id
	 */
    public void setId(long id) {
        this.id = id;
    }

    /**
	 * Gets the loan number.
	 * @return the loan number
	 */
    public String getLoanNumber() {
        return loanNumber;
    }

    /**
	 * Sets the loan number.
	 * @param loanNumber the new loan number
	 */
    public void setLoanNumber(String loanNumber) {
        this.loanNumber = loanNumber;
    }

    /**
	 * Gets the loan amount.
	 * @return the loan amount
	 */
    public double getLoanAmount() {
        return loanAmount;
    }

    /**
	 * Sets the loan amount.
	 * @param loanAmount the new loan amount
	 */
    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    /**
	 * Gets the payment mode.
	 * @return the payment mode
	 */
    public String getPaymentMode() {
        return paymentMode;
    }

    /**
	 * Sets the payment mode.
	 * @param paymentMode the new payment mode
	 */
    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    /**
	 * Gets the monthly payment.
	 * @return the monthly payment
	 */
    public String getMonthlyPayment() {
        return monthlyPayment;
    }

    /**
	 * Sets the monthly payment.
	 * @param monthlyPayment the new monthly payment
	 */
    public void setMonthlyPayment(String monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    /**
	 * Gets the date.
	 * @return the date
	 */
    public Date getDate() {
        return date;
    }

    /**
	 * Sets the date.
	 * @param date the new date
	 */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
	 * Gets the type.
	 * @return the type
	 */
    public int getType() {
        return type;
    }

    /**
	 * Sets the type.
	 * @param type the new type
	 */
    public void setType(int type) {
        this.type = type;
    }

    /**
	 * Gets the form data.
	 * @return the form data
	 */
    public Map<String, String> getFormData() {
        return formData;
    }

    /**
	 * Sets the form data.
	 * @param formData the form data
	 */
    public void setFormData(Map<String, String> formData) {
        this.formData = formData;
    }

	/**
	 * Gets the status.
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 * @param status the new status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	public String toString() {
		return loanNumber + " [" + loanAmount + "]";
	}
}
