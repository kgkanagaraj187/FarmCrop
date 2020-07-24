/*
 * TransactionLog.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.util.log;

import com.sourcetrace.eses.util.entity.EntityInfo;

public class TransactionLog extends EntityInfo {

	private long id;
	private String txnType;
	private String serialNo;
	private String msgNo;
	private String resentCount;
	private String agentId;
	private String operationType;
	private String mode;
	private String version;
	private String requestLog;
	private int status;
	private String statusCode;
	private String statusMsg;
	private String branchId;

	private StringBuffer requestLogStringBuffer;

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
	 * Gets the serial no.
	 * 
	 * @return the serial no
	 */
	public String getSerialNo() {

		return serialNo;
	}

	/**
	 * Sets the serial no.
	 * 
	 * @param serialNo
	 *            the new serial no
	 */
	public void setSerialNo(String serialNo) {

		this.serialNo = serialNo;
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
	 * Gets the resent count.
	 * 
	 * @return the resent count
	 */
	public String getResentCount() {

		return resentCount;
	}

	/**
	 * Sets the resent count.
	 * 
	 * @param resentCount
	 *            the new resent count
	 */
	public void setResentCount(String resentCount) {

		this.resentCount = resentCount;
	}

	/**
	 * Gets the agent id.
	 * 
	 * @return the agent id
	 */
	public String getAgentId() {

		return agentId;
	}

	/**
	 * Sets the agent id.
	 * 
	 * @param agentId
	 *            the new agent id
	 */
	public void setAgentId(String agentId) {

		this.agentId = agentId;
	}

	/**
	 * Gets the operation type.
	 * 
	 * @return the operation type
	 */
	public String getOperationType() {

		return operationType;
	}

	/**
	 * Sets the operation type.
	 * 
	 * @param operationType
	 *            the new operation type
	 */
	public void setOperationType(String operationType) {

		this.operationType = operationType;
	}

	/**
	 * Gets the mode.
	 * 
	 * @return the mode
	 */
	public String getMode() {

		return mode;
	}

	/**
	 * Sets the mode.
	 * 
	 * @param mode
	 *            the new mode
	 */
	public void setMode(String mode) {

		this.mode = mode;
	}

	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	public String getVersion() {

		return version;
	}

	/**
	 * Sets the version.
	 * 
	 * @param version
	 *            the new version
	 */
	public void setVersion(String version) {

		this.version = version;
	}

	/**
	 * Gets the request log.
	 * 
	 * @return the request log
	 */
	public String getRequestLog() {

		return requestLog;
	}

	/**
	 * Sets the request log.
	 * 
	 * @param requestLog
	 *            the new request log
	 */
	public void setRequestLog(String requestLog) {

		this.requestLog = requestLog;
	}

	/**
	 * Gets the status.
	 * 
	 * @return the status
	 */
	public int getStatus() {

		return status;
	}

	/**
	 * Sets the status.
	 * 
	 * @param status
	 *            the new status
	 */
	public void setStatus(int status) {

		this.status = status;
	}

	/**
	 * Gets the status code.
	 * 
	 * @return the status code
	 */
	public String getStatusCode() {

		return statusCode;
	}

	/**
	 * Sets the status code.
	 * 
	 * @param statusCode
	 *            the new status code
	 */
	public void setStatusCode(String statusCode) {

		this.statusCode = statusCode;
	}

	/**
	 * Gets the status msg.
	 * 
	 * @return the status msg
	 */
	public String getStatusMsg() {

		return statusMsg;
	}

	/**
	 * Sets the status msg.
	 * 
	 * @param statusMsg
	 *            the new status msg
	 */
	public void setStatusMsg(String statusMsg) {

		this.statusMsg = statusMsg;
	}

	/**
	 * Gets the request log string buffer.
	 * 
	 * @return the request log string buffer
	 */
	public StringBuffer getRequestLogStringBuffer() {

		return requestLogStringBuffer;
	}

	/**
	 * Sets the request log string buffer.
	 * 
	 * @param requestLogStringBuffer
	 *            the new request log string buffer
	 */
	public void setRequestLogStringBuffer(StringBuffer requestLogStringBuffer) {

		this.requestLogStringBuffer = requestLogStringBuffer;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

}
