/*
 * OfflinePayment.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

public class OfflinePayment {

	private long id;
	private String receiptNo;
	private String agentId;
	private String deviceId;
	private String servicePointId;
	private String farmerId;
	private String paymentDate;
	private String paymentAmt;
	private String paymentType;
	private String seasonCode;
	private String pageNo;
	private String remarks;
	private int statusCode;
	private String statusMsg;
	private String paymentMode;
	private String remark;
	private byte[] image;
	private String pcTime;
	private String latitude;
	private String longitude;
	private String branchId;

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
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {

		return id;
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
	 * Gets the receipt no.
	 * 
	 * @return the receipt no
	 */
	public String getReceiptNo() {

		return receiptNo;
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
	 * Gets the agent id.
	 * 
	 * @return the agent id
	 */
	public String getAgentId() {

		return agentId;
	}

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
	 * Gets the device id.
	 * 
	 * @return the device id
	 */
	public String getDeviceId() {

		return deviceId;
	}

	/**
	 * Sets the service point id.
	 * 
	 * @param servicePointId
	 *            the new service point id
	 */
	public void setServicePointId(String servicePointId) {

		this.servicePointId = servicePointId;
	}

	/**
	 * Gets the service point id.
	 * 
	 * @return the service point id
	 */
	public String getServicePointId() {

		return servicePointId;
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
	 * Gets the farmer id.
	 * 
	 * @return the farmer id
	 */
	public String getFarmerId() {

		return farmerId;
	}

	/**
	 * Sets the payment date.
	 * 
	 * @param paymentDate
	 *            the new payment date
	 */
	public void setPaymentDate(String paymentDate) {

		this.paymentDate = paymentDate;
	}

	/**
	 * Gets the payment date.
	 * 
	 * @return the payment date
	 */
	public String getPaymentDate() {

		return paymentDate;
	}

	/**
	 * Sets the payment amt.
	 * 
	 * @param paymentAmt
	 *            the new payment amt
	 */
	public void setPaymentAmt(String paymentAmt) {

		this.paymentAmt = paymentAmt;
	}

	/**
	 * Gets the payment amt.
	 * 
	 * @return the payment amt
	 */
	public String getPaymentAmt() {

		return paymentAmt;
	}

	/**
	 * Sets the payment type.
	 * 
	 * @param paymentType
	 *            the new payment type
	 */
	public void setPaymentType(String paymentType) {

		this.paymentType = paymentType;
	}

	/**
	 * Gets the payment type.
	 * 
	 * @return the payment type
	 */
	public String getPaymentType() {

		return paymentType;
	}

	/**
	 * Sets the season code.
	 * 
	 * @param seasonCode
	 *            the new season code
	 */
	public void setSeasonCode(String seasonCode) {

		this.seasonCode = seasonCode;
	}

	/**
	 * Gets the season code.
	 * 
	 * @return the season code
	 */
	public String getSeasonCode() {

		return seasonCode;
	}

	/**
	 * Sets the page no.
	 * 
	 * @param pageNo
	 *            the new page no
	 */
	public void setPageNo(String pageNo) {

		this.pageNo = pageNo;
	}

	/**
	 * Gets the page no.
	 * 
	 * @return the page no
	 */
	public String getPageNo() {

		return pageNo;
	}

	/**
	 * Gets the remarks.
	 * 
	 * @return the remarks
	 */
	public String getRemarks() {

		return remarks;
	}

	/**
	 * Sets the remarks.
	 * 
	 * @param remarks
	 *            the new remarks
	 */
	public void setRemarks(String remarks) {

		this.remarks = remarks;
	}

	/**
	 * Sets the status code.
	 * 
	 * @param statusCode
	 *            the new status code
	 */
	public void setStatusCode(int statusCode) {

		this.statusCode = statusCode;
	}

	/**
	 * Gets the status code.
	 * 
	 * @return the status code
	 */
	public int getStatusCode() {

		return statusCode;
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
	 * Gets the status msg.
	 * 
	 * @return the status msg
	 */
	public String getStatusMsg() {

		return statusMsg;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPcTime() {
		return pcTime;
	}

	public void setPcTime(String pcTime) {
		this.pcTime = pcTime;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	
}
