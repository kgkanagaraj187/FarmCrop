package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;
import java.util.Set;

public class OfflineBaleGeneration {
	private long id;
	private Date createdDate;
	private String agentId;
	private String createdUser;
	private String updatedUser;
	private int statusCode;
	private String statusMsg;
	private String deviceId;
	private String genDate;
	private String baleDate;
	private String heap;
	private String lotNo;
	private String prNo;
	private String baleWeight;
	private String branchId;
	private String season;
	private String messageNo;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getGenDate() {
		return genDate;
	}
	public void setGenDate(String genDate) {
		this.genDate = genDate;
	}
	public String getLotNo() {
		return lotNo;
	}
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	public String getPrNo() {
		return prNo;
	}
	public void setPrNo(String prNo) {
		this.prNo = prNo;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getBaleWeight() {
		return baleWeight;
	}
	public void setBaleWeight(String baleWeight) {
		this.baleWeight = baleWeight;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}
	public String getUpdatedUser() {
		return updatedUser;
	}
	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusMsg() {
		return statusMsg;
	}
	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getHeap() {
		return heap;
	}
	public void setHeap(String heap) {
		this.heap = heap;
	}
	public String getBaleDate() {
		return baleDate;
	}
	public void setBaleDate(String baleDate) {
		this.baleDate = baleDate;
	}
	public String getSeason() {
		return season;
	}
	public void setSeason(String season) {
		this.season = season;
	}
	public String getMessageNo() {
		return messageNo;
	}
	public void setMessageNo(String messageNo) {
		this.messageNo = messageNo;
	}

	
	
	
	}
