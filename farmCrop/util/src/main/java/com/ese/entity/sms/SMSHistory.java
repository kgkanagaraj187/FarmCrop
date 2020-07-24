/*
 * SMSHistory.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.entity.sms;

import java.util.Date;
import java.util.Set;

import com.sourcetrace.eses.entity.Profile;

// TODO: Auto-generated Javadoc
public class SMSHistory extends Profile {

    public static final int SMS_SINGLE = 1;
    public static final int SMS_BULK = 2;

    public static final int MOBILE_NO_LENGTH = 10;
    public static final String MOBILE_NO_PATTERN = "[0-9]*";
    public static final String BULK_MOBILE_NO_PATTERN = "[0-9,]*";
    public static final int MESSAGE_LENGTH = 160;

    private long id;
    private int smsType;
    private String smsRoute;
    private String senderMobNo;
    private String message;
    private String statusz;
    private String statusMsg;
    private String uuid;
    private Set<SMSHistoryDetail> smsHistoryDetails;
    private Date createDT;
    private String createUser;
    private Date lastUpdateDT;
    private String lastUpdateUser;
    private String branchId;
    private String responce;

    // Transient
    private String receiverMobNo;

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
     * Gets the sms type.
     * @return the sms type
     */
    public int getSmsType() {

        return smsType;
    }

    /**
     * Sets the sms type.
     * @param smsType the new sms type
     */
    public void setSmsType(int smsType) {

        this.smsType = smsType;
    }

    /**
     * Gets the sms route.
     * @return the sms route
     */
    public String getSmsRoute() {

        return smsRoute;
    }

    /**
     * Sets the sms route.
     * @param smsRoute the new sms route
     */
    public void setSmsRoute(String smsRoute) {

        this.smsRoute = smsRoute;
    }

    /**
     * Gets the sender mob no.
     * @return the sender mob no
     */
    public String getSenderMobNo() {

        return senderMobNo;
    }

    /**
     * Sets the sender mob no.
     * @param senderMobNo the new sender mob no
     */
    public void setSenderMobNo(String senderMobNo) {

        this.senderMobNo = senderMobNo;
    }

    /**
     * Gets the message.
     * @return the message
     */
    public String getMessage() {

        return message;
    }

    /**
     * Sets the message.
     * @param message the new message
     */
    public void setMessage(String message) {

        this.message = message;
    }

    public String getStatusz() {

        return statusz;
    }

    public void setStatusz(String statusz) {

        this.statusz = statusz;
    }

    /**
     * Gets the status msg.
     * @return the status msg
     */
    public String getStatusMsg() {

        return statusMsg;
    }

    /**
     * Sets the status msg.
     * @param statusMsg the new status msg
     */
    public void setStatusMsg(String statusMsg) {

        this.statusMsg = statusMsg;
    }

    /**
     * Gets the uuid.
     * @return the uuid
     */
    public String getUuid() {

        return uuid;
    }

    /**
     * Sets the uuid.
     * @param uuid the new uuid
     */
    public void setUuid(String uuid) {

        this.uuid = uuid;
    }

    /**
     * Gets the sms history details.
     * @return the sms history details
     */
    public Set<SMSHistoryDetail> getSmsHistoryDetails() {

        return smsHistoryDetails;
    }

    /**
     * Sets the sms history details.
     * @param smsHistoryDetails the new sms history details
     */
    public void setSmsHistoryDetails(Set<SMSHistoryDetail> smsHistoryDetails) {

        this.smsHistoryDetails = smsHistoryDetails;
    }

    /**
     * Gets the create dt.
     * @return the create dt
     */
    public Date getCreateDT() {

        return createDT;
    }

    /**
     * Sets the create dt.
     * @param createDT the new create dt
     */
    public void setCreateDT(Date createDT) {

        this.createDT = createDT;
    }

    /**
     * Gets the create user.
     * @return the create user
     */
    public String getCreateUser() {

        return createUser;
    }

    /**
     * Sets the create user.
     * @param createUser the new create user
     */
    public void setCreateUser(String createUser) {

        this.createUser = createUser;
    }

    /**
     * Gets the last update dt.
     * @return the last update dt
     */
    public Date getLastUpdateDT() {

        return lastUpdateDT;
    }

    /**
     * Sets the last update dt.
     * @param lastUpdateDT the new last update dt
     */
    public void setLastUpdateDT(Date lastUpdateDT) {

        this.lastUpdateDT = lastUpdateDT;
    }

    /**
     * Gets the last update user.
     * @return the last update user
     */
    public String getLastUpdateUser() {

        return lastUpdateUser;
    }

    /**
     * Sets the last update user.
     * @param lastUpdateUser the new last update user
     */
    public void setLastUpdateUser(String lastUpdateUser) {

        this.lastUpdateUser = lastUpdateUser;
    }

    public String getReceiverMobNo() {

        return receiverMobNo;
    }

    public void setReceiverMobNo(String receiverMobNo) {

        this.receiverMobNo = receiverMobNo;
    }

    public String getBranchId() {

        return branchId;
    }

    public void setBranchId(String branchId) {

        this.branchId = branchId;
    }

	public String getResponce() {
		return responce;
	}

	public void setResponce(String responce) {
		this.responce = responce;
	}

}
