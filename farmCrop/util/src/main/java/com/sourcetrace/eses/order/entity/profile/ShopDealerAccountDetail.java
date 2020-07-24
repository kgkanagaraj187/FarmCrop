/*
 * ShopDealerAccountDetail.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.profile;

import java.io.Serializable;
import java.util.Date;

public class ShopDealerAccountDetail implements Serializable {

	private static final long serialVersionUID = 5502267347967374752L;
	public static final String ORDER = "order";
	public static final String PAYMENT = "payment";
	public static final String ORDER_CANCELLED = "orderCancel";
	public static final String ORDER_VOID = "orderVoid";
	public static final String PAYMENT_VOID = "paymentVoid";
	public static final String ORDER_CANCELLED_VOID = "orderCancelVoid";
	public static final String PAYMENT_CANCELLED_VOID = "paymentCancelVoid";
	private long id;
	private ShopDealerAccount shopDealerAccount;
	private String type;
	private double previousBalance;
	private double txnAmount;
	private double outstandingAmount;
	private Date txnDate;

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the shop dealer account.
	 * 
	 * @return the shop dealer account
	 */
	public ShopDealerAccount getShopDealerAccount() {
		return shopDealerAccount;
	}

	/**
	 * Sets the shop dealer account.
	 * 
	 * @param shopDealerAccount
	 *            the new shop dealer account
	 */
	public void setShopDealerAccount(ShopDealerAccount shopDealerAccount) {
		this.shopDealerAccount = shopDealerAccount;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the previous balance.
	 * 
	 * @return the previous balance
	 */
	public double getPreviousBalance() {
		return previousBalance;
	}

	/**
	 * Sets the previous balance.
	 * 
	 * @param previousBalance
	 *            the new previous balance
	 */
	public void setPreviousBalance(double previousBalance) {
		this.previousBalance = previousBalance;
	}

	/**
	 * Gets the txn amount.
	 * 
	 * @return the txn amount
	 */
	public double getTxnAmount() {
		return txnAmount;
	}

	/**
	 * Sets the txn amount.
	 * 
	 * @param txnAmount
	 *            the new txn amount
	 */
	public void setTxnAmount(double txnAmount) {
		this.txnAmount = txnAmount;
	}

	/**
	 * Gets the outstanding amount.
	 * 
	 * @return the outstanding amount
	 */
	public double getOutstandingAmount() {
		return outstandingAmount;
	}

	/**
	 * Sets the outstanding amount.
	 * 
	 * @param outstandingAmount
	 *            the new outstanding amount
	 */
	public void setOutstandingAmount(double outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}

	/**
	 * Gets the txn date.
	 * 
	 * @return the txn date
	 */
	public Date getTxnDate() {
		return txnDate;
	}

	/**
	 * Sets the txn date.
	 * 
	 * @param txnDate
	 *            the new txn date
	 */
	public void setTxnDate(Date txnDate) {
		this.txnDate = txnDate;
	}

	/**
	 * Gets the prev balance.
	 * 
	 * @return the prev balance
	 */
	public String getPrevBalance() {
		return Double.toString(previousBalance);
	}

	/**
	 * Gets the txn amt.
	 * 
	 * @return the txn amt
	 */
	public String getTxnAmt() {
		return Double.toString(txnAmount);
	}

	/**
	 * Gets the outstanding amt.
	 * 
	 * @return the outstanding amt
	 */
	public String getOutstandingAmt() {
		return Double.toString(outstandingAmount);
	}

}
