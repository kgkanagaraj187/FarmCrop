/*
 * PMT.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Warehouse;

public class PMT {

	public static enum Status {
		NONE, MTNT, MTNR, COMPLETE
	}

	public static final String TRN_TYPE_STOCK_TRNASFER = "STOCK_TRANSFER";
	public static final String TRN_TYPE_OTEHR = "OTHER";
	public static final String TRN_TYPE_PRODUCT_RECEPTION = "PRODUCT RECEPTION";

	private long id;
	private Date mtntDate;
	private Date mtnrDate;
	private String mtntReceiptNumber;
	private String mtnrReceiptNumber;
	private String truckId;
	private String driverName;
	private int statusCode;
	private String statusMessage;
	private Set<TripSheet> tripSheets;
	private Set<PMTDetail> pmtDetails;
	private Set<PMTFarmerDetail> pmtFarmerDetais;
	private TransferInfo mtntTransferInfo;
	private TransferInfo mtnrTransferInfo;
	private Warehouse coOperative;
	private String trnType;
	private String branchId;
	private Double totalLabourCost;
	private Double transportCost;
	private Double totalAmt;
	private String client;
	private String invoiceNo;
	private PMT transferInfo;
	private Set<PMTImageDetails> pmtImageDetails;

	// transient
	private List<PMTAgentDetail> pmtAgentDetails;
	private String senderCityId;
	private Agent agentRef;
	private String ginningCode;
	// private String branchId;
	private String seasonCode;
	private String pmtDetailCooperative;
	private List<String> branchesList;
	private String transporter;
	private String farmerId;
	private String latitude;
	private String longitude;

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
	 * Gets the mtnt date.
	 * 
	 * @return the mtnt date
	 */
	public Date getMtntDate() {

		return mtntDate;
	}

	/**
	 * Sets the mtnt date.
	 * 
	 * @param mtntDate
	 *            the new mtnt date
	 */
	public void setMtntDate(Date mtntDate) {

		this.mtntDate = mtntDate;
	}

	/**
	 * Gets the mtnr date.
	 * 
	 * @return the mtnr date
	 */
	public Date getMtnrDate() {

		return mtnrDate;
	}

	/**
	 * Sets the mtnr date.
	 * 
	 * @param mtnrDate
	 *            the new mtnr date
	 */
	public void setMtnrDate(Date mtnrDate) {

		this.mtnrDate = mtnrDate;
	}

	/**
	 * Gets the mtnt receipt number.
	 * 
	 * @return the mtnt receipt number
	 */
	public String getMtntReceiptNumber() {

		return mtntReceiptNumber;
	}

	/**
	 * Sets the mtnt receipt number.
	 * 
	 * @param mtntReceiptNumber
	 *            the new mtnt receipt number
	 */
	public void setMtntReceiptNumber(String mtntReceiptNumber) {

		this.mtntReceiptNumber = mtntReceiptNumber;
	}

	/**
	 * Gets the mtnr receipt number.
	 * 
	 * @return the mtnr receipt number
	 */
	public String getMtnrReceiptNumber() {

		return mtnrReceiptNumber;
	}

	/**
	 * Sets the mtnr receipt number.
	 * 
	 * @param mtnrReceiptNumber
	 *            the new mtnr receipt number
	 */
	public void setMtnrReceiptNumber(String mtnrReceiptNumber) {

		this.mtnrReceiptNumber = mtnrReceiptNumber;
	}

	/**
	 * Gets the truck id.
	 * 
	 * @return the truck id
	 */
	public String getTruckId() {

		return truckId;
	}

	/**
	 * Sets the truck id.
	 * 
	 * @param truckId
	 *            the new truck id
	 */
	public void setTruckId(String truckId) {

		this.truckId = truckId;
	}

	/**
	 * Gets the driver name.
	 * 
	 * @return the driver name
	 */
	public String getDriverName() {

		return driverName;
	}

	/**
	 * Sets the driver name.
	 * 
	 * @param driverName
	 *            the new driver name
	 */
	public void setDriverName(String driverName) {

		this.driverName = driverName;
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
	 * Sets the status code.
	 * 
	 * @param statusCode
	 *            the new status code
	 */
	public void setStatusCode(int statusCode) {

		this.statusCode = statusCode;
	}

	/**
	 * Gets the status message.
	 * 
	 * @return the status message
	 */
	public String getStatusMessage() {

		return statusMessage;
	}

	/**
	 * Sets the status message.
	 * 
	 * @param statusMessage
	 *            the new status message
	 */
	public void setStatusMessage(String statusMessage) {

		this.statusMessage = statusMessage;
	}

	/**
	 * Gets the trip sheets.
	 * 
	 * @return the trip sheets
	 */
	public Set<TripSheet> getTripSheets() {

		return tripSheets;
	}

	/**
	 * Sets the trip sheets.
	 * 
	 * @param tripSheets
	 *            the new trip sheets
	 */
	public void setTripSheets(Set<TripSheet> tripSheets) {

		this.tripSheets = tripSheets;
	}

	/**
	 * Gets the pmt details.
	 * 
	 * @return the pmt details
	 */
	public Set<PMTDetail> getPmtDetails() {

		return pmtDetails;
	}

	/**
	 * Sets the pmt details.
	 * 
	 * @param pmtDetails
	 *            the new pmt details
	 */
	public void setPmtDetails(Set<PMTDetail> pmtDetails) {

		this.pmtDetails = pmtDetails;
	}

	/**
	 * Gets the mtnt transfer info.
	 * 
	 * @return the mtnt transfer info
	 */
	public TransferInfo getMtntTransferInfo() {

		return mtntTransferInfo;
	}

	/**
	 * Sets the mtnt transfer info.
	 * 
	 * @param mtntTransferInfo
	 *            the new mtnt transfer info
	 */
	public void setMtntTransferInfo(TransferInfo mtntTransferInfo) {

		this.mtntTransferInfo = mtntTransferInfo;
	}

	/**
	 * Gets the mtnr transfer info.
	 * 
	 * @return the mtnr transfer info
	 */
	public TransferInfo getMtnrTransferInfo() {

		return mtnrTransferInfo;
	}

	/**
	 * Sets the mtnr transfer info.
	 * 
	 * @param mtnrTransferInfo
	 *            the new mtnr transfer info
	 */
	public void setMtnrTransferInfo(TransferInfo mtnrTransferInfo) {

		this.mtnrTransferInfo = mtnrTransferInfo;
	}

	/**
	 * Gets the pmt agent details.
	 * 
	 * @return the pmt agent details
	 */
	public List<PMTAgentDetail> getPmtAgentDetails() {

		return pmtAgentDetails;
	}

	/**
	 * Sets the pmt agent details.
	 * 
	 * @param pmtAgentDetails
	 *            the new pmt agent details
	 */
	public void setPmtAgentDetails(List<PMTAgentDetail> pmtAgentDetails) {

		this.pmtAgentDetails = pmtAgentDetails;
	}

	/**
	 * Sets the sender city id.
	 * 
	 * @param senderCityId
	 *            the new sender city id
	 */
	public void setSenderCityId(String senderCityId) {

		this.senderCityId = senderCityId;
	}

	/**
	 * Gets the sender city id.
	 * 
	 * @return the sender city id
	 */
	public String getSenderCityId() {

		return senderCityId;
	}

	/**
	 * Gets the co operative.
	 * 
	 * @return the co operative
	 */
	public Warehouse getCoOperative() {

		return coOperative;
	}

	/**
	 * Sets the co operative.
	 * 
	 * @param coOperative
	 *            the new co operative
	 */
	public void setCoOperative(Warehouse coOperative) {

		this.coOperative = coOperative;
	}

	/**
	 * Gets the agent ref.
	 * 
	 * @return the agent ref
	 */
	public Agent getAgentRef() {

		return agentRef;
	}

	/**
	 * Sets the agent ref.
	 * 
	 * @param agentRef
	 *            the new agent ref
	 */
	public void setAgentRef(Agent agentRef) {

		this.agentRef = agentRef;
	}

	public String getTrnType() {

		return trnType;
	}

	public void setTrnType(String trnType) {

		this.trnType = trnType;
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

	public String getPmtDetailCooperative() {

		return pmtDetailCooperative;
	}

	public void setPmtDetailCooperative(String pmtDetailCooperative) {

		this.pmtDetailCooperative = pmtDetailCooperative;
	}

	public List<String> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}

	public Double getTotalLabourCost() {
		return totalLabourCost;
	}

	public void setTotalLabourCost(Double totalLabourCost) {
		this.totalLabourCost = totalLabourCost;
	}

	public Double getTransportCost() {
		return transportCost;
	}

	public void setTransportCost(Double transportCost) {
		this.transportCost = transportCost;
	}

	public Double getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(Double totalAmt) {
		this.totalAmt = totalAmt;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Set<PMTFarmerDetail> getPmtFarmerDetais() {
		return pmtFarmerDetais;
	}

	public void setPmtFarmerDetais(Set<PMTFarmerDetail> pmtFarmerDetais) {
		this.pmtFarmerDetais = pmtFarmerDetais;
	}

	public String getGinningCode() {
		return ginningCode;
	}

	public void setGinningCode(String ginningCode) {
		this.ginningCode = ginningCode;
	}

	public PMT getTransferInfo() {
		return transferInfo;
	}

	public void setTransferInfo(PMT transferInfo) {
		this.transferInfo = transferInfo;
	}

	public String getTransporter() {
		return transporter;
	}

	public void setTransporter(String transporter) {
		this.transporter = transporter;
	}

	public Set<PMTImageDetails> getPmtImageDetails() {
		return pmtImageDetails;
	}

	public void setPmtImageDetails(Set<PMTImageDetails> pmtImageDetails) {
		this.pmtImageDetails = pmtImageDetails;
	}

	public String getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
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
	
	

}
