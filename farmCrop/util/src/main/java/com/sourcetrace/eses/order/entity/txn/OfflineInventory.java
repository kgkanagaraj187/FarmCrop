/*
 * OfflineInventory.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

package com.sourcetrace.eses.order.entity.txn;

import java.io.Serializable;
import java.util.Set;

/**
 * The Class OfflineInventory.
 */
public class OfflineInventory implements Serializable {

	private static final long serialVersionUID = 8609813984809768025L;
	
	private long id;
	private String shopDealerId;
	private String shopDealerName;
	private String txnType;
	private String creditLimit;
	private String outstandingBalance;
	private String inventoryDate;
	private String inventoryOrderNo;
	private String inventoryDeliveryNo;
	private String totalQuantity;
	private String grandTotal;
	private String paymentMode;
	private String paymentReference;
	private String paymentAmount;
	private String deliveryMode;
	private String agentId;
	private String deviceId;
	private String servicePointId;
	private int statusCode;
	private String statusMsg;
	private Set<OfflineInventoryDetail> offlineInventoryDetails;

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
	 * @param id the new id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the shop dealer id.
	 * 
	 * @return the shop dealer id
	 */
	public String getShopDealerId() {
		return shopDealerId;
	}

	/**
	 * Sets the shop dealer id.
	 * 
	 * @param shopDealerId the new shop dealer id
	 */
	public void setShopDealerId(String shopDealerId) {
		this.shopDealerId = shopDealerId;
	}

	/**
	 * Gets the shop dealer name.
	 * 
	 * @return the shop dealer name
	 */
	public String getShopDealerName() {
		return shopDealerName;
	}

	/**
	 * Sets the shop dealer name.
	 * 
	 * @param shopDealerName the new shop dealer name
	 */
	public void setShopDealerName(String shopDealerName) {
		this.shopDealerName = shopDealerName;
	}

	/**
	 * Gets the txn type.
	 * 
	 * @return the txn type
	 */
	public String getTxnType() {
		return txnType;
	}

	/**
	 * Sets the txn type.
	 * 
	 * @param txnType the new txn type
	 */
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	/**
	 * Gets the credit limit.
	 * 
	 * @return the credit limit
	 */
	public String getCreditLimit() {
		return creditLimit;
	}

	/**
	 * Sets the credit limit.
	 * 
	 * @param creditLimit the new credit limit
	 */
	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit;
	}

	/**
	 * Gets the outstanding balance.
	 * 
	 * @return the outstanding balance
	 */
	public String getOutstandingBalance() {
		return outstandingBalance;
	}

	/**
	 * Sets the outstanding balance.
	 * 
	 * @param outstandingBalance the new outstanding balance
	 */
	public void setOutstandingBalance(String outstandingBalance) {
		this.outstandingBalance = outstandingBalance;
	}

	/**
	 * Gets the inventory date.
	 * 
	 * @return the inventory date
	 */
	public String getInventoryDate() {
		return inventoryDate;
	}

	/**
	 * Sets the inventory date.
	 * 
	 * @param inventoryDate the new inventory date
	 */
	public void setInventoryDate(String inventoryDate) {
		this.inventoryDate = inventoryDate;
	}

	/**
	 * Gets the inventory order no.
	 * 
	 * @return the inventory order no
	 */
	public String getInventoryOrderNo() {
		return inventoryOrderNo;
	}

	/**
	 * Sets the inventory order no.
	 * 
	 * @param inventoryOrderNo the new inventory order no
	 */
	public void setInventoryOrderNo(String inventoryOrderNo) {
		this.inventoryOrderNo = inventoryOrderNo;
	}

	/**
	 * Gets the inventory delivery no.
	 * 
	 * @return the inventory delivery no
	 */
	public String getInventoryDeliveryNo() {
		return inventoryDeliveryNo;
	}

	/**
	 * Sets the inventory delivery no.
	 * 
	 * @param inventoryDeliveryNo the new inventory delivery no
	 */
	public void setInventoryDeliveryNo(String inventoryDeliveryNo) {
		this.inventoryDeliveryNo = inventoryDeliveryNo;
	}

	/**
	 * Gets the total quantity.
	 * 
	 * @return the total quantity
	 */
	public String getTotalQuantity() {
		return totalQuantity;
	}

	/**
	 * Sets the total quantity.
	 * 
	 * @param totalQuantity the new total quantity
	 */
	public void setTotalQuantity(String totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	/**
	 * Gets the grand total.
	 * 
	 * @return the grand total
	 */
	public String getGrandTotal() {
		return grandTotal;
	}

	/**
	 * Sets the grand total.
	 * 
	 * @param grandTotal the new grand total
	 */
	public void setGrandTotal(String grandTotal) {
		this.grandTotal = grandTotal;
	}

	/**
	 * Gets the payment mode.
	 * 
	 * @return the payment mode
	 */
	public String getPaymentMode() {
		return paymentMode;
	}

	/**
	 * Sets the payment mode.
	 * 
	 * @param paymentMode the new payment mode
	 */
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	/**
	 * Gets the payment reference.
	 * 
	 * @return the payment reference
	 */
	public String getPaymentReference() {
		return paymentReference;
	}

	/**
	 * Sets the payment reference.
	 * 
	 * @param paymentReference the new payment reference
	 */
	public void setPaymentReference(String paymentReference) {
		this.paymentReference = paymentReference;
	}

	/**
	 * Gets the payment amount.
	 * 
	 * @return the payment amount
	 */
	public String getPaymentAmount() {
		return paymentAmount;
	}

	/**
	 * Sets the payment amount.
	 * 
	 * @param paymentAmount the new payment amount
	 */
	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	/**
	 * Gets the delivery mode.
	 * 
	 * @return the delivery mode
	 */
	public String getDeliveryMode() {
		return deliveryMode;
	}

	/**
	 * Sets the delivery mode.
	 * 
	 * @param deliveryMode the new delivery mode
	 */
	public void setDeliveryMode(String deliveryMode) {
		this.deliveryMode = deliveryMode;
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
	 * Sets the agent id.
	 * 
	 * @param agentId the new agent id
	 */
	public void setAgentId(String agentId) {
		this.agentId = agentId;
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
	 * Sets the device id.
	 * 
	 * @param deviceId the new device id
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
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
	 * Sets the service point id.
	 * 
	 * @param servicePointId the new service point id
	 */
	public void setServicePointId(String servicePointId) {
		this.servicePointId = servicePointId;
	}

	/**
	 * Gets the offline inventory details.
	 * 
	 * @return the offline inventory details
	 */
	public Set<OfflineInventoryDetail> getOfflineInventoryDetails() {
		return offlineInventoryDetails;
	}

	/**
	 * Sets the offline inventory details.
	 * 
	 * @param offlineInventoryDetails the new offline inventory details
	 */
	public void setOfflineInventoryDetails(
			Set<OfflineInventoryDetail> offlineInventoryDetails) {
		this.offlineInventoryDetails = offlineInventoryDetails;
	}

	/**
	 * Sets the status code.
	 * 
	 * @param statusCode the new status code
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
	 * @param statusMsg the new status msg
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

}
