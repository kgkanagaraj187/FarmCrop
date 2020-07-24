/*
 * Deposit.java
 * Copyright (c) 2008, Source Trace Systems
 * ALL RIGHTS RESERVED
 */
package com.sourcetrace.esesw.entity.profile;

import java.util.Date;
import java.util.Map;


/**
 * The Class Deposit.
 * @author $Author: aravind $
 * @version $Rev: 59 $, $Date: 2009-08-04 17:10:51 +0530 (Tue, 04 Aug 2009) $
 */
public class Deposit {

    private long id;
    private String voucherNumber;
    private double amount;
    private Date date;
    private int type;
    private int status;
    private Map<String, String> formData;
    private Client client;

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
	 * Gets the voucher number.
	 * @return the voucher number
	 */
    public String getVoucherNumber() {
        return voucherNumber;
    }

    /**
	 * Sets the voucher number.
	 * @param voucherNumber the new voucher number
	 */
    public void setVoucherNumber(String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    /**
	 * Gets the amount.
	 * @return the amount
	 */
    public double getAmount() {
        return amount;
    }

    /**
	 * Sets the amount.
	 * @param amount the new amount
	 */
    public void setAmount(double amount) {
        this.amount = amount;
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
		return voucherNumber + " [" + amount + "]";
	}
}
