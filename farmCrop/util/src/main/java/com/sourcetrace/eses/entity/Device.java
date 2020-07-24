/*
 * Device.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.entity;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.sourcetrace.eses.util.entity.AuditLog;
import com.sourcetrace.esesw.entity.profile.Branch;
import com.sourcetrace.esesw.entity.profile.LocationDetail;

// TODO: Auto-generated Javadoc
/**
 * The Class Device.
 *
 * @author $Author: antronivan $
 * @version $Rev: 422 $, $Date: 2009-11-23 08:19:13 +0530 (Mon, 23 Nov 2009) $
 */
public class Device {

	public static final int POS = 1;
	public static final int ENROLLMENT_STATION = 2;
	public static final int DEVICE_CODE_LEN_MIN = 1;
	public static final int SERIAL_NUMBER_LEN_MIN = 1;
	public static final int DEVICE_CODE_LEN_MAX = 20;
	public static final int SERIAL_NUMBER_MAX = 35;
	public static final int DEVICE_NAME_MAX = 20;

	public static final String POS_DEVICE = "POS";
	public static final String MOBILE_DEVICE = "Mobile";
	public static final String LAPTOP_DEVICE = "Laptop";

	public static final int DEVICE_NOT_REGISTERED = 0;
	public static final int DEVICE_REGISTERED = 1;

	private long id;
	private String deviceId;
	private String serialNumber;
	private String name;
	private int type;
	private Branch branch;
	private LocationDetail location;
	private boolean enabled;
	private Set<Agent> agents;
	private Date createdTime;
	private Date modifiedTime;
	private String deviceType;
	private String code;
	private Agent agent;
	private String msgNo;
	private String receiptNo;
	private boolean deleted;
	private int isRegistered;
	private String lastUpdatedUsername;
	private String branchId;
	private String createdUsername;
	private Date createdDate;
	
    public String appversion;
    public String logintime;
    public String androidVer;
    public String mobileModel;


	
	/**
	 * Transient variable
	 */
	private List<String> branchesList;
	public String getAppversion() {
		return appversion;
	}

	public void setAppversion(String appversion) {
		this.appversion = appversion;
	}

	public String getLogintime() {
		return logintime;
	}

	public void setLogintime(String logintime) {
		this.logintime = logintime;
	}

	private String filterStatus;
	private String macAddress;
	private String deviceAddress;
	
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
	 * Gets the receipt no.
	 *
	 * @return the receipt no
	 */
	public String getReceiptNo() {
		return receiptNo;
	}

	/**
	 * Sets the receipt no.
	 *
	 * @param receiptNo
	 *            the new receipt no
	 */
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	/*
	 * // @Pattern(regexp = "[[^@#$%&*();:,{}^<>?+|^'!^/=\"\\_-]]+$", message =
	 * "pattern.deviceCode") // @Length(min = DEVICE_CODE_LEN_MIN, max =
	 * DEVICE_CODE_LEN_MAX, message = "length.code") // @NotEmpty(message =
	 * "empty.code") // @NotNull(message = "empty.code")
	 */ public String getCode() {
		return code;
	}

	/**
	 * Sets the code.
	 *
	 * @param code
	 *            the new code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Gets the device type.
	 *
	 * @return the device type
	 */
	// @Length(max = 10, message = "length.deviceType")
	// @NotEmpty(message = "empty.deviceType")
	public String getDeviceType() {
		return deviceType;
	}

	/**
	 * Sets the device type.
	 *
	 * @param deviceType
	 *            the new device type
	 */
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	// transient properties for audit log
	private String siteUser;
	private String action;

	/**
	 * Gets the serial number.
	 *
	 * @return the serial number
	 */
	// @Pattern(regexp = "[[^@#$%&*();:,{}^<>?+|^'!^/=\"\\_-]]+$", message =
	// "pattern.serialNumber")
	// @Length(min = SERIAL_NUMBER_LEN_MIN, max = SERIAL_NUMBER_MAX, message =
	// "length.serialNumber")
	// @NotEmpty(message = "empty.serialNumber")
	// @NotNull(message = "empty.serialNumber")
	public String getSerialNumber() {

		return serialNumber;
	}

	/**
	 * Sets the serial number.
	 *
	 * @param serialNumber
	 *            the new serial number
	 */
	public void setSerialNumber(String serialNumber) {

		this.serialNumber = serialNumber;
	}

	/**
	 * Gets the branch.
	 *
	 * @return the branch
	 */
	// @NotNull(message = "empty.branch")
	public Branch getBranch() {

		return branch;
	}

	//
	// /**
	// * Sets the branch.
	// * @param branch the new branch
	// */
	/**
	 * Sets the branch.
	 *
	 * @param branch
	 *            the new branch
	 */
	public void setBranch(Branch branch) {

		this.branch = branch;
	}

	//
	/**
	 * Gets the created time.
	 *
	 * @return the created time
	 */
	public Date getCreatedTime() {

		return createdTime;
	}

	/**
	 * Sets the created time.
	 *
	 * @param createdTime
	 *            the new created time
	 */
	public void setCreatedTime(Date createdTime) {

		this.createdTime = createdTime;
	}

	/**
	 * Gets the modified time.
	 *
	 * @return the modified time
	 */
	public Date getModifiedTime() {

		return modifiedTime;
	}

	/**
	 * Sets the modified time.
	 *
	 * @param modifiedTime
	 *            the new modified time
	 */
	public void setModifiedTime(Date modifiedTime) {

		this.modifiedTime = modifiedTime;
	}

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

	// /**
	// * Gets the device id.
	// * @return the device id
	// */
	// @Length(max = 20, message = "length.deviceId")
	// @Pattern(regex = "[^\\p{Punct}]+$", message = "pattern.deviceId")
	// @NotEmpty(message = "empty.deviceId")
	/**
	 * Gets the device id.
	 *
	 * @return the device id
	 */
	public String getDeviceId() {

		return deviceId;
	}

	//
	// /**
	// * Sets the device id.
	// * @param deviceId the new device id
	// */
	/**
	 * Sets the device id.
	 *
	 * @param deviceId
	 *            the new device id
	 */
	public void setDeviceId(String deviceId) {

		this.deviceId = deviceId;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	/*
	 * @Length(max = 20, message = "length.name")
	 * 
	 * @Pattern(regex = "[^\\p{Punct}]+$", message = "pattern.name")
	 * 
	 * @NotEmpty(message = "empty.name")
	 */
	// @Pattern(regexp = "[[^@#$%&*();:,{}^<>?+|^'!^/=\"\\_-]]+$", message =
	// "pattern.deviceName")
	// @Length(max = DEVICE_NAME_MAX, message = "length.deviceName")
	// @NotEmpty(message = "empty.deviceName")
	// @NotNull(message = "empty.deviceName")
	public String getName() {

		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {

		this.name = name;
	}

	// /**
	// * Checks if is enabled.
	// * @return true, if is enabled
	// */
	// @NotNull(message = "empty.enabled")
	/**
	 * Checks if is enabled.
	 *
	 * @return true, if is enabled
	 */
	public boolean isEnabled() {

		return enabled;
	}

	//
	// /**
	// * Sets the enabled.
	// * @param enabled the new enabled
	// */
	/**
	 * Sets the enabled.
	 *
	 * @param enabled
	 *            the new enabled
	 */
	public void setEnabled(boolean enabled) {

		if (enabled != this.enabled) {
			action = (enabled) ? AuditLog.ENABLE : AuditLog.DISABLE;
		}
		this.enabled = enabled;
	}

	//
	// /**
	// * Gets the agents.
	// * @return the agents
	// */
	// @NotEmpty(message = "empty.agents")
	// @NotNull(message = "empty.agents")
	/**
	 * Gets the agents.
	 *
	 * @return the agents
	 */
	public Set<Agent> getAgents() {

		return agents;
	}

	//
	// /**
	// * Sets the agents.
	// * @param agents the new agents
	// */
	/**
	 * Sets the agents.
	 *
	 * @param agents
	 *            the new agents
	 */
	public void setAgents(Set<Agent> agents) {

		this.agents = agents;
	}

	//
	// /**
	// * Gets the type.
	// * @return the type
	// */
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public int getType() {

		return type;
	}

	//
	// /**
	// * Sets the type.
	// * @param type the new type
	// */
	/**
	 * Sets the type.
	 *
	 * @param type
	 *            the new type
	 */
	public void setType(int type) {

		this.type = type;
	}

	//
	// /*
	// * (non-Javadoc)
	// * @see java.lang.Object#toString()
	// */
	public String toString() {

		return getName();
	}

	//
	/**
	 * Sets the site user.
	 *
	 * @param userName
	 *            the new site user
	 */
	public void setSiteUser(String userName) {

		this.siteUser = userName;
	}

	/**
	 * Gets the site user.
	 *
	 * @return the site user
	 */
	public String getSiteUser() {

		return siteUser;
	}

	/**
	 * Gets the action.
	 *
	 * @return the action
	 */
	public String getAction() {

		return action;
	}

	/**
	 * Sets the action.
	 *
	 * @param action
	 *            the new action
	 */
	public void setAction(String action) {

		this.action = action;
	}

	//
	// /**
	// * Gets the location.
	// * @return the location
	// */
	// @NotNull(message = "empty.location")
	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public LocationDetail getLocation() {

		return location;
	}

	//
	// /**
	// * Sets the location.
	// * @param location the new location
	// */
	/**
	 * Sets the location.
	 *
	 * @param location
	 *            the new location
	 */
	public void setLocation(LocationDetail location) {

		this.location = location;
	}

	/**
	 * Sets the agent.
	 *
	 * @param agent
	 *            the new agent
	 */
	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	/**
	 * Gets the agent.
	 *
	 * @return the agent
	 */
	public Agent getAgent() {
		return agent;
	}

	/**
	 * Sets the deleted.
	 *
	 * @param deleted
	 *            the new deleted
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * Checks if is deleted.
	 *
	 * @return true, if is deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * Sets the filter status.
	 *
	 * @param filterStatus
	 *            the new filter status
	 */
	public void setFilterStatus(String filterStatus) {
		this.filterStatus = filterStatus;
	}

	/**
	 * Gets the filter status.
	 *
	 * @return the filter status
	 */
	public String getFilterStatus() {
		return filterStatus;
	}

	public int getIsRegistered() {
		return isRegistered;
	}

	public void setIsRegistered(int isRegistered) {
		this.isRegistered = isRegistered;
	}

	public String getLastUpdatedUsername() {
		return lastUpdatedUsername;
	}

	public void setLastUpdatedUsername(String lastUpdatedUsername) {
		this.lastUpdatedUsername = lastUpdatedUsername;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getCreatedUsername() {
		return createdUsername;
	}

	public void setCreatedUsername(String createdUsername) {
		this.createdUsername = createdUsername;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public List<String> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getDeviceAddress() {
		return deviceAddress;
	}

	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}

	public String getAndroidVer() {
		return androidVer;
	}

	public void setAndroidVer(String androidVer) {
		this.androidVer = androidVer;
	}

	public String getMobileModel() {
		return mobileModel;
	}

	public void setMobileModel(String mobileModel) {
		this.mobileModel = mobileModel;
	}
	
	
	
	
}
