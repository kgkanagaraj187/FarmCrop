/*
 * SMSHistoryDetail.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.entity.sms;

import java.util.Date;

import com.sourcetrace.eses.entity.Profile;

// TODO: Auto-generated Javadoc
public class SMSHistoryDetail extends Profile{

    private long id;
    private SMSHistory smsHistory;
    private String receiverNo;
    private Date sendAt;
    private String receiverId;
    private String receiverType;
    private String groupId;
    private String statusz;
    private Date createDT;
    private String createUser;
    private Date lastUpdateDT;
    private String lastUpdateUser;
    private String message;

    // Transient
    private String receiverName;
    private String receiverGroupName;

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
     * Gets the sms history.
     * @return the sms history
     */
    public SMSHistory getSmsHistory() {

        return smsHistory;
    }

    /**
     * Sets the sms history.
     * @param smsHistory the new sms history
     */
    public void setSmsHistory(SMSHistory smsHistory) {

        this.smsHistory = smsHistory;
    }

    /**
     * Gets the receiver no.
     * @return the receiver no
     */
    public String getReceiverNo() {

        return receiverNo;
    }

    /**
     * Sets the receiver no.
     * @param receiverNo the new receiver no
     */
    public void setReceiverNo(String receiverNo) {

        this.receiverNo = receiverNo;
    }

    /**
     * Gets the send at.
     * @return the send at
     */
    public Date getSendAt() {

        return sendAt;
    }

    /**
     * Sets the send at.
     * @param sendAt the new send at
     */
    public void setSendAt(Date sendAt) {

        this.sendAt = sendAt;
    }

    /**
     * Gets the receiver id.
     * @return the receiver id
     */
    public String getReceiverId() {

        return receiverId;
    }

    /**
     * Sets the receiver id.
     * @param receiverId the new receiver id
     */
    public void setReceiverId(String receiverId) {

        this.receiverId = receiverId;
    }

  

    /**
     * Gets the group id.
     * @return the group id
     */
    public String getGroupId() {

        return groupId;
    }

    /**
     * Sets the group id.
     * @param groupId the new group id
     */
    public void setGroupId(String groupId) {

        this.groupId = groupId;
    }

    
    public String getStatusz() {
    
        return statusz;
    }

    public void setStatusz(String statusz) {
    
        this.statusz = statusz;
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

    /**
     * Gets the receiver name.
     * @return the receiver name
     */
    public String getReceiverName() {

        return receiverName;
    }

    /**
     * Sets the receiver name.
     * @param receiverName the new receiver name
     */
    public void setReceiverName(String receiverName) {

        this.receiverName = receiverName;
    }

    /**
     * Gets the receiver group name.
     * @return the receiver group name
     */
    public String getReceiverGroupName() {

        return receiverGroupName;
    }

    /**
     * Sets the receiver group name.
     * @param receiverGroupName the new receiver group name
     */
    public void setReceiverGroupName(String receiverGroupName) {

        this.receiverGroupName = receiverGroupName;
    }

	public String getReceiverType() {
		return receiverType;
	}

	public void setReceiverType(String receiverType) {
		this.receiverType = receiverType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
