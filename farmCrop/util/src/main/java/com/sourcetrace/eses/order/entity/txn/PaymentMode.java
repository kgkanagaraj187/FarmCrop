/*
 * PaymentMode.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

package com.sourcetrace.eses.order.entity.txn;

public class PaymentMode {
	
	public static final String DISTRIBUTION_ADVANCE_PAYMENT_MODE_CODE = "5";
    public static final String DISTRIBUTION_ADVANCE_PAYMENT_MODE_NAME = "Distribution Advance";
    public static final String SETTLEMENT_PAYMENT_MODE_NAME = "Settlement";
    public static final String DISTIBUTION_PAYMENT_TXN = "334D";
    public static final String PROCURMENT_PAYMENT_TXN = "334P";
    public static final String DISTRIBUTION_PAYMENT_MODE_NAME = "Distribution Payment";
    public static final String LOAN_REPAYMENT_TXN="702";
    public static final String LOAN_REPAYMENT_MODE_NAME="LOAN REPAYMENT";
    public static final String LOAN_REPAYMENT_AMOUNT="LOAN REPAYMENT";
    public static final String LOAN_REPAYMENT="6";
    private long id;
    private String code;
    private String name;

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Gets the id.
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Sets the code.
     * @param code the new code
     */
    public void setCode(String code) {

        this.code = code;
    }

    /**
     * Gets the code.
     * @return the code
     */
    public String getCode() {

        return code;
    }

    /**
     * Sets the name.
     * @param name the new name
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Gets the name.
     * @return the name
     */
    public String getName() {

        return name;
    }

}
