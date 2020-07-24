/*
 * EntityInfo.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.util.entity;

import java.io.Serializable;
import java.util.Date;

import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

// TODO: Auto-generated Javadoc
public class EntityInfo implements Serializable {

	public static char NULL_CHAR = '\u0000';
	public static char ACTIVE = 'Y';
	public static char IN_ACTIVE = 'N';
	public static char DELETE = 'D';
	public static char PENDING = 'P';
	public static char VERIFIED = 'V';
	public static char YES = 'Y';
	public static char NO = 'N';
	public static final int TYPE_USER = 0;
	public static final int TYPE_AGENT = 1;

	// Regex patterns for Validation
	public static final String CODE_NAME_REGEX = "[^\\p{Punct}]+$";

	private Date txnTime;
	private Integer userType;
	private char isActive;
	private Date createdDT;
	private String createdUserName;
	private Date lastUpdatedDT;
	private String lastUpdatedUserName;
	private long revisionNumber;
	private String agentInfo;
	private String deviceInfo;

	// Transcient
	private String txnTimeFormatted;
	private String userTypeString;

	/**
	 * Gets the is active.
	 * 
	 * @return the is active
	 */
	public char getIsActive() {

		return isActive;
	}

	/**
	 * Sets the is active.
	 * 
	 * @param isActive
	 *            the new is active
	 */
	public void setIsActive(char isActive) {

		this.isActive = isActive;
	}

	/**
	 * Sets the is active.
	 * 
	 * @param isActive
	 *            the new is active
	 */
	public void setIsActive(String isActive) {

		if (!StringUtil.isEmpty(isActive)) {
			this.isActive = isActive.charAt(0);
		}
	}

	/**
	 * Gets the created dt.
	 * 
	 * @return the created dt
	 */
	public Date getCreatedDT() {

		return createdDT;
	}

	/**
	 * Sets the created dt.
	 * 
	 * @param createdDT
	 *            the new created dt
	 */
	public void setCreatedDT(Date createdDT) {

		this.createdDT = createdDT;
	}

	/**
	 * Gets the created user name.
	 * 
	 * @return the created user name
	 */
	public String getCreatedUserName() {

		return createdUserName;
	}

	/**
	 * Sets the created user name.
	 * 
	 * @param createdUserName
	 *            the new created user name
	 */
	public void setCreatedUserName(String createdUserName) {

		this.createdUserName = createdUserName;
	}

	/**
	 * Gets the last updated dt.
	 * 
	 * @return the last updated dt
	 */
	public Date getLastUpdatedDT() {

		return lastUpdatedDT;
	}

	/**
	 * Sets the last updated dt.
	 * 
	 * @param lastUpdatedDT
	 *            the new last updated dt
	 */
	public void setLastUpdatedDT(Date lastUpdatedDT) {

		this.lastUpdatedDT = lastUpdatedDT;
	}

	/**
	 * Gets the last updated user name.
	 * 
	 * @return the last updated user name
	 */
	public String getLastUpdatedUserName() {

		return lastUpdatedUserName;
	}

	/**
	 * Sets the last updated user name.
	 * 
	 * @param lastUpdatedUserName
	 *            the new last updated user name
	 */
	public void setLastUpdatedUserName(String lastUpdatedUserName) {

		this.lastUpdatedUserName = lastUpdatedUserName;
	}

	/**
	 * Gets the revision number.
	 * 
	 * @return the revision number
	 */
	public long getRevisionNumber() {

		return revisionNumber;
	}

	/**
	 * Sets the revision number.
	 * 
	 * @param revisionNumber
	 *            the new revision number
	 */
	public void setRevisionNumber(long revisionNumber) {

		this.revisionNumber = revisionNumber;
	}

	/**
	 * Sets the creation info.
	 * 
	 * @param userName
	 *            the new creation info
	 */
	public void setCreationInfo(String userName) {

		this.createdUserName = userName;
		this.lastUpdatedUserName = userName;
		this.createdDT = new Date();
		this.lastUpdatedDT = new Date();
		this.setRevisionNumber(DateUtil.getRevisionNumber());
	}

	/**
	 * Sets the creation info.
	 * 
	 * @param userName
	 *            the user name
	 * @param userType
	 *            the user type
	 */
	public void setCreationInfo(String userName, int userType) {

		this.userType = userType;
		this.createdUserName = userName;
		this.lastUpdatedUserName = userName;
		this.createdDT = new Date();
		this.lastUpdatedDT = new Date();
		this.setRevisionNumber(DateUtil.getRevisionNumber());
	}

	/**
	 * Sets the updation info.
	 * 
	 * @param userName
	 *            the new updation info
	 */
	public void setUpdationInfo(String userName) {

		this.lastUpdatedUserName = userName;
		this.lastUpdatedDT = new Date();
		this.setRevisionNumber(DateUtil.getRevisionNumber());
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
	 * Gets the user type.
	 * 
	 * @return the user type
	 */
	public Integer getUserType() {

		return userType;
	}

	/**
	 * Sets the user type.
	 * 
	 * @param userType
	 *            the new user type
	 */
	public void setUserType(Integer userType) {

		this.userType = userType;
	}

	/**
	 * Gets the txn time formatted.
	 * 
	 * @return the txn time formatted
	 */
	public String getTxnTimeFormatted() {

		if (!ObjectUtil.isEmpty(txnTime)) {
			txnTimeFormatted = DateUtil.convertDateToString(txnTime, DateUtil.TXN_TIME_FORMAT);
		}
		return txnTimeFormatted;
	}

	/**
	 * Sets the txn time formatted.
	 * 
	 * @param txnTimeFormatted
	 *            the new txn time formatted
	 */
	public void setTxnTimeFormatted(String txnTimeFormatted) {

		this.txnTimeFormatted = txnTimeFormatted;
	}

	/**
	 * Gets the user type string.
	 * 
	 * @return the user type string
	 */
	public String getUserTypeString() {

		if (!ObjectUtil.isEmpty(userType)) {
			if (userType == TYPE_USER) {
				userTypeString = "userType.user";
			} else if (userType == TYPE_AGENT) {
				userTypeString = "userType.agent";
			}
		}
		return userTypeString;
	}

	/**
	 * Sets the user type string.
	 * 
	 * @param userTypeString
	 *            the new user type string
	 */
	public void setUserTypeString(String userTypeString) {

		this.userTypeString = userTypeString;
	}

	/**
	 * Gets the is active string.
	 * 
	 * @return the is active string
	 */
	public String getIsActiveString() {

		if (isActive != NULL_CHAR) {
			if (isActive == 'N') {
				return "In active";
			} else if (isActive == 'Y') {
				return "Active";
			}
		}
		return String.valueOf(isActive);
	}

	/**
	 * Gets the created user name string.
	 * 
	 * @return the created user name string
	 */
	public String getCreatedUserNameString() {

		if (!ObjectUtil.isEmpty(userType)) {
			if (userType == TYPE_USER) {
				userTypeString = "(user)";
			} else if (userType == TYPE_AGENT) {
				userTypeString = "(fieldStaff)";
			}
		}
		return createdUserName + userTypeString;
	}

	/**
	 * Gets the agent info.
	 * 
	 * @return the agent info
	 */
	public String getAgentInfo() {

		return agentInfo;
	}

	/**
	 * Sets the agent info.
	 * 
	 * @param agentInfo
	 *            the new agent info
	 */
	public void setAgentInfo(String agentInfo) {

		this.agentInfo = agentInfo;
	}

	/**
	 * Gets the device info.
	 * 
	 * @return the device info
	 */
	public String getDeviceInfo() {

		return deviceInfo;
	}

	/**
	 * Sets the device info.
	 * 
	 * @param deviceInfo
	 *            the new device info
	 */
	public void setDeviceInfo(String deviceInfo) {

		this.deviceInfo = deviceInfo;
	}

}
