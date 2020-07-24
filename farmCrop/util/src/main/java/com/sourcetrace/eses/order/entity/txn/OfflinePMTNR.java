package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;
import java.util.Set;

public class OfflinePMTNR {
	private Long id;
	private String mtnrDate;
	private String mtntReceiptNo;
	private String receiptNo;
	private Set<OfflinePMTNRDetail> offlinePMTRDetail;
	private Set<OfflinePMTImageDetails> offlinePmtImages;
	// private TransferInfo mtnrTransferInfo;
	private String agentId;
	private String coOperativeCode;
	private String createdUser;
	private String updatedUser;
	private String truckId;
	private String driverName;
	private int statusCode;
	private String statusMsg;
	private String branchId;
	private String seasonCode;
	private String trnType;
	private String deviceId;
	private String transporter;
	private Date createdDated;
	private String messageNo;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getMtnrDate() {
		return mtnrDate;
	}

	public void setMtnrDate(String mtnrDate) {
		this.mtnrDate = mtnrDate;
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

	
	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getTruckId() {
		return truckId;
	}

	public void setTruckId(String truckId) {
		this.truckId = truckId;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getSeasonCode() {
		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
	}

	public Set<OfflinePMTNRDetail> getOfflinePMTRDetail() {
		return offlinePMTRDetail;
	}

	public void setOfflinePMTRDetail(Set<OfflinePMTNRDetail> offlinePMTRDetail) {
		this.offlinePMTRDetail = offlinePMTRDetail;
	}


	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getTrnType() {
		return trnType;
	}

	public void setTrnType(String trnType) {
		this.trnType = trnType;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getCoOperativeCode() {
		return coOperativeCode;
	}

	public void setCoOperativeCode(String coOperativeCode) {
		this.coOperativeCode = coOperativeCode;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getTransporter() {
		return transporter;
	}

	public void setTransporter(String transporter) {
		this.transporter = transporter;
	}

	public Set<OfflinePMTImageDetails> getOfflinePmtImages() {
		return offlinePmtImages;
	}

	public void setOfflinePmtImages(Set<OfflinePMTImageDetails> offlinePmtImages) {
		this.offlinePmtImages = offlinePmtImages;
	}

	public Date getCreatedDated() {
		return createdDated;
	}

	public void setCreatedDated(Date createdDated) {
		this.createdDated = createdDated;
	}

	public String getMtntReceiptNo() {
		return mtntReceiptNo;
	}

	public void setMtntReceiptNo(String mtntReceiptNo) {
		this.mtntReceiptNo = mtntReceiptNo;
	}

	public String getMessageNo() {
		return messageNo;
	}

	public void setMessageNo(String messageNo) {
		this.messageNo = messageNo;
	}



}
