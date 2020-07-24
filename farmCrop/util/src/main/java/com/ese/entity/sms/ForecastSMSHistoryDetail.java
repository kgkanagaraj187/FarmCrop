package com.ese.entity.sms;

import java.util.Date;

import com.sourcetrace.eses.entity.Profile;

public class ForecastSMSHistoryDetail extends Profile {
	private long id;
	private ForecastSMSHistory forecastSMSHistory;
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getReceiverNo() {
		return receiverNo;
	}

	public void setReceiverNo(String receiverNo) {
		this.receiverNo = receiverNo;
	}

	public Date getSendAt() {
		return sendAt;
	}

	public void setSendAt(Date sendAt) {
		this.sendAt = sendAt;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public String getReceiverType() {
		return receiverType;
	}

	public void setReceiverType(String receiverType) {
		this.receiverType = receiverType;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getStatusz() {
		return statusz;
	}

	public void setStatusz(String statusz) {
		this.statusz = statusz;
	}

	public Date getCreateDT() {
		return createDT;
	}

	public void setCreateDT(Date createDT) {
		this.createDT = createDT;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getLastUpdateDT() {
		return lastUpdateDT;
	}

	public void setLastUpdateDT(Date lastUpdateDT) {
		this.lastUpdateDT = lastUpdateDT;
	}

	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverGroupName() {
		return receiverGroupName;
	}

	public void setReceiverGroupName(String receiverGroupName) {
		this.receiverGroupName = receiverGroupName;
	}

	public ForecastSMSHistory getForecastSMSHistory() {
		return forecastSMSHistory;
	}

	public void setForecastSMSHistory(ForecastSMSHistory forecastSMSHistory) {
		this.forecastSMSHistory = forecastSMSHistory;
	}

}
