/*
 * Transaction.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.entity;

import java.util.Date;

/**
 * @author Saravanan
 */
public class Transaction {

	public enum Status {
		SUCCESS, FAILED, PENDING
	};

	public static final String OPERATIONTYPE_REGULAR = "01";
	public static final String OPERATIONTYPE_VOID = "02";

	public static final String MODE_ONLINE = "01";
	public static final String MODE_OFFLINE = "02";

	public static final int BOD = 1;
	public static final int EOD = 0;

	public static final String SUCCESS = "00";
	public static final String FAILED = "01";
	public static final String PENDING = "02";

	public static final String SUCCESS_MSG = "SUCCESS";
	public static final String FAILED_MSG = "FAILED";
	public static final String PENDING_MSG = "PENDING";

	private long id;
	private Date txnTime;
	private String txnType;
	private String txnCode;
	private int operType; // (Regular / Void)
	private int mode; // (Online / Offline)
	private long resentCount;
	private String msgNo;
	private long batchNo;
	private String statusCode;
	private String statusMessage;
	private TransactionHeader header;
	private String branchId;
	private String tenantId;

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
	 * Gets the txn time.
	 * 
	 * @return the txn time
	 */
	public Date getTxnTime() {

		return txnTime;
	}

	/**
	 * Sets the txn time.
	 * 
	 * @param txnTime
	 *            the new txn time
	 */
	public void setTxnTime(Date txnTime) {

		this.txnTime = txnTime;
	}

	/**
	 * Gets the txn type.
	 * 
	 * @return the txn type
	 */
	public String getTxnType() {

		return txnType;
	}

	/**
	 * Sets the txn type.
	 * 
	 * @param txnType
	 *            the new txn type
	 */
	public void setTxnType(String txnType) {

		this.txnType = txnType;
	}

	/**
	 * Gets the txn code.
	 * 
	 * @return the txn code
	 */
	public String getTxnCode() {

		return txnCode;
	}

	/**
	 * Sets the txn code.
	 * 
	 * @param txnCode
	 *            the new txn code
	 */
	public void setTxnCode(String txnCode) {

		this.txnCode = txnCode;
	}

	/**
	 * Gets the oper type.
	 * 
	 * @return the oper type
	 */
	public int getOperType() {

		return operType;
	}

	/**
	 * Sets the oper type.
	 * 
	 * @param operType
	 *            the new oper type
	 */
	public void setOperType(int operType) {

		this.operType = operType;
	}

	/**
	 * Gets the mode.
	 * 
	 * @return the mode
	 */
	public int getMode() {

		return mode;
	}

	/**
	 * Sets the mode.
	 * 
	 * @param mode
	 *            the new mode
	 */
	public void setMode(int mode) {

		this.mode = mode;
	}

	/**
	 * Gets the resent count.
	 * 
	 * @return the resent count
	 */
	public long getResentCount() {

		return resentCount;
	}

	/**
	 * Sets the resent count.
	 * 
	 * @param resentCount
	 *            the new resent count
	 */
	public void setResentCount(long resentCount) {

		this.resentCount = resentCount;
	}

	/**
	 * Gets the msg no.
	 * 
	 * @return the msg no
	 */
	public String getMsgNo() {

		return msgNo;
	}

	/**
	 * Sets the msg no.
	 * 
	 * @param msgNo
	 *            the new msg no
	 */
	public void setMsgNo(String msgNo) {

		this.msgNo = msgNo;
	}

	/**
	 * Gets the batch no.
	 * 
	 * @return the batch no
	 */
	public long getBatchNo() {

		return batchNo;
	}

	/**
	 * Sets the batch no.
	 * 
	 * @param batchNo
	 *            the new batch no
	 */
	public void setBatchNo(long batchNo) {

		this.batchNo = batchNo;
	}

	/**
	 * Gets the header.
	 * 
	 * @return the header
	 */
	public TransactionHeader getHeader() {

		return header;
	}

	/**
	 * Sets the header.
	 * 
	 * @param header
	 *            the new header
	 */
	public void setHeader(TransactionHeader header) {

		this.header = header;
	}

	/**
	 * Gets the statusCode.
	 * 
	 * @return the statusCode
	 */
	public String getStatusCode() {

		return statusCode;
	}

	/**
	 * Sets the statusCode.
	 * 
	 * @param code
	 *            the new statusCode
	 */
	public void setStatusCode(String statusCode) {

		this.statusCode = statusCode;
	}

	/**
	 * Gets the statusMessage.
	 * 
	 * @return the statusMessage
	 */
	public String getStatusMessage() {

		return statusMessage;
	}

	/**
	 * Sets the statusMessage.
	 * 
	 * @param statusMessage
	 *            the new statusMessage
	 */
	public void setStatusMessage(String statusMessage) {

		this.statusMessage = statusMessage;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

}
