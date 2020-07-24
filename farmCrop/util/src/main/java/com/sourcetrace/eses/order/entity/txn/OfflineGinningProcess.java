package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;

import com.sourcetrace.eses.entity.Warehouse;

public class OfflineGinningProcess {
	private Long id;
	private String processDate;
	private String agentId;
	private String createdUser;
	private String updatedUser;
	private int statusCode;
	private String statusMsg;
	private String branchId;
	private String deviceId;
	private Date createdDate;
	private String heapCode;
	private String productCode;
	private String ics;
	private String processQty;
	private String lintQty;
	private String seedQty;
	private String scrapQty;
	private String remark;
	private String season;
	private Warehouse ginning;
	private String messageNo;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProcessDate() {
		return processDate;
	}
	public void setProcessDate(String processDate) {
		this.processDate = processDate;
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
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
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
	public String getHeapCode() {
		return heapCode;
	}
	public void setHeapCode(String heapCode) {
		this.heapCode = heapCode;
	}
	
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getIcs() {
		return ics;
	}
	public void setIcs(String ics) {
		this.ics = ics;
	}
	public String getProcessQty() {
		return processQty;
	}
	public void setProcessQty(String processQty) {
		this.processQty = processQty;
	}
	public String getLintQty() {
		return lintQty;
	}
	public void setLintQty(String lintQty) {
		this.lintQty = lintQty;
	}
	public String getSeedQty() {
		return seedQty;
	}
	public void setSeedQty(String seedQty) {
		this.seedQty = seedQty;
	}
	public String getScrapQty() {
		return scrapQty;
	}
	public void setScrapQty(String scrapQty) {
		this.scrapQty = scrapQty;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Warehouse getGinning() {
		return ginning;
	}
	public void setGinning(Warehouse ginning) {
		this.ginning = ginning;
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
