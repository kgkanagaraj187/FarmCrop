/*
 * FarmerFailedQuestions.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.txn.agrocert;

/**
 * The Class FarmerFailedQuestions.
 */
public class FarmerFailedQuestions {
	private long id;
	private String farmerId;
	private String serialNo;
	private String questionName;
	private long failedCount;
	private String userName;
	private String branchId;


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
	 * Gets the farmer id.
	 * 
	 * @return the farmer id
	 */
	public String getFarmerId() {

		return farmerId;
	}

	/**
	 * Sets the farmer id.
	 * 
	 * @param farmerId
	 *            the new farmer id
	 */
	public void setFarmerId(String farmerId) {

		this.farmerId = farmerId;
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
	 * Gets the question name.
	 * 
	 * @return the question name
	 */
	public String getQuestionName() {

		return questionName;
	}

	/**
	 * Sets the question name.
	 * 
	 * @param questionName
	 *            the new question name
	 */
	public void setQuestionName(String questionName) {

		this.questionName = questionName;
	}

	/**
	 * Gets the failed count.
	 * 
	 * @return the failed count
	 */
	public long getFailedCount() {

		return failedCount;
	}

	/**
	 * Sets the failed count.
	 * 
	 * @param failedCount
	 *            the new failed count
	 */
	public void setFailedCount(long failedCount) {

		this.failedCount = failedCount;
	}

	/**
	 * Gets the user name.
	 * 
	 * @return the user name
	 */
	public String getUserName() {

		return userName;
	}

	/**
	 * Sets the user name.
	 * 
	 * @param userName
	 *            the new user name
	 */
	public void setUserName(String userName) {

		this.userName = userName;
	}

    public String getBranchId() {

        return branchId;
    }

    public void setBranchId(String branchId) {

        this.branchId = branchId;
    }

}
