/*
 * Inventory.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class Inventory implements Serializable {

	private static final long serialVersionUID = 8609813984809768025L;

	public static enum InventoryType {
		ORDER, DELIVERY, BOTH, CANCELLED
	}

	public static enum DeliveryMode {
		DIRECT, PARTIAL, PENDING
	}

	public static enum PaymentMode {
		CASH, CHEQUE, DEMANDDRAFT, NO_PAYMENT
	}

	private long id;
	private String shopDealerId;
	private String shopDealerName;
	private int type;
	private double creditLimit;
	private double outstandingBalance;
	private Date orderDate;
	private String orderNo;
	private Date deliveryDate;
	private String deliveryNo;
	private double totalQuantity;
	private double grandTotal;
	private int paymentMode;
	private String paymentReference;
	private double paymentAmount;
	private int deliveryMode;
	private String agentId;
	private String agentName;
	private String deviceId;
	private String deviceName;
	private String servicePointId;
	private String servicePointName;
	private String approvalCode;
	private Date cancelDate;
	private int operationType;
	private Set<InventoryDetail> inventoryDetails;

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
	 * @param shopDealerId
	 *            the new shop dealer id
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
	 * @param shopDealerName
	 *            the new shop dealer name
	 */
	public void setShopDealerName(String shopDealerName) {
		this.shopDealerName = shopDealerName;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Gets the credit limit.
	 * 
	 * @return the credit limit
	 */
	public double getCreditLimit() {
		return creditLimit;
	}

	/**
	 * Sets the credit limit.
	 * 
	 * @param creditLimit
	 *            the new credit limit
	 */
	public void setCreditLimit(double creditLimit) {
		this.creditLimit = creditLimit;
	}

	/**
	 * Gets the outstanding balance.
	 * 
	 * @return the outstanding balance
	 */
	public double getOutstandingBalance() {
		return outstandingBalance;
	}

	/**
	 * Sets the outstanding balance.
	 * 
	 * @param outstandingBalance
	 *            the new outstanding balance
	 */
	public void setOutstandingBalance(double outstandingBalance) {
		this.outstandingBalance = outstandingBalance;
	}

	/**
	 * Gets the order date.
	 * 
	 * @return the order date
	 */
	public Date getOrderDate() {
		return orderDate;
	}

	/**
	 * Sets the order date.
	 * 
	 * @param orderDate
	 *            the new order date
	 */
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	/**
	 * Gets the order no.
	 * 
	 * @return the order no
	 */
	public String getOrderNo() {
		return orderNo;
	}

	/**
	 * Sets the order no.
	 * 
	 * @param orderNo
	 *            the new order no
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * Gets the delivery date.
	 * 
	 * @return the delivery date
	 */
	public Date getDeliveryDate() {
		return deliveryDate;
	}

	/**
	 * Sets the delivery date.
	 * 
	 * @param deliveryDate
	 *            the new delivery date
	 */
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	/**
	 * Gets the delivery no.
	 * 
	 * @return the delivery no
	 */
	public String getDeliveryNo() {
		return deliveryNo;
	}

	/**
	 * Sets the delivery no.
	 * 
	 * @param deliveryNo
	 *            the new delivery no
	 */
	public void setDeliveryNo(String deliveryNo) {
		this.deliveryNo = deliveryNo;
	}

	/**
	 * Gets the total quantity.
	 * 
	 * @return the total quantity
	 */
	public double getTotalQuantity() {
		return totalQuantity;
	}

	/**
	 * Sets the total quantity.
	 * 
	 * @param totalQuantity
	 *            the new total quantity
	 */
	public void setTotalQuantity(double totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	/**
	 * Gets the grand total.
	 * 
	 * @return the grand total
	 */
	public double getGrandTotal() {
		return grandTotal;
	}

	/**
	 * Sets the grand total.
	 * 
	 * @param grandTotal
	 *            the new grand total
	 */
	public void setGrandTotal(double grandTotal) {
		this.grandTotal = grandTotal;
	}

	/**
	 * Gets the payment mode.
	 * 
	 * @return the payment mode
	 */
	public int getPaymentMode() {
		return paymentMode;
	}

	/**
	 * Sets the payment mode.
	 * 
	 * @param paymentMode
	 *            the new payment mode
	 */
	public void setPaymentMode(int paymentMode) {
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
	 * @param paymentReference
	 *            the new payment reference
	 */
	public void setPaymentReference(String paymentReference) {
		this.paymentReference = paymentReference;
	}

	/**
	 * Gets the payment amount.
	 * 
	 * @return the payment amount
	 */
	public double getPaymentAmount() {
		return paymentAmount;
	}

	/**
	 * Sets the payment amount.
	 * 
	 * @param paymentAmount
	 *            the new payment amount
	 */
	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	/**
	 * Gets the delivery mode.
	 * 
	 * @return the delivery mode
	 */
	public int getDeliveryMode() {
		return deliveryMode;
	}

	/**
	 * Sets the delivery mode.
	 * 
	 * @param deliveryMode
	 *            the new delivery mode
	 */
	public void setDeliveryMode(int deliveryMode) {
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
	 * @param agentId
	 *            the new agent id
	 */
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	/**
	 * Gets the agent name.
	 * 
	 * @return the agent name
	 */
	public String getAgentName() {
		return agentName;
	}

	/**
	 * Sets the agent name.
	 * 
	 * @param agentName
	 *            the new agent name
	 */
	public void setAgentName(String agentName) {
		this.agentName = agentName;
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
	 * @param deviceId
	 *            the new device id
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * Gets the device name.
	 * 
	 * @return the device name
	 */
	public String getDeviceName() {
		return deviceName;
	}

	/**
	 * Sets the device name.
	 * 
	 * @param deviceName
	 *            the new device name
	 */
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
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
	 * @param servicePointId
	 *            the new service point id
	 */
	public void setServicePointId(String servicePointId) {
		this.servicePointId = servicePointId;
	}

	/**
	 * Gets the service point name.
	 * 
	 * @return the service point name
	 */
	public String getServicePointName() {
		return servicePointName;
	}

	/**
	 * Sets the service point name.
	 * 
	 * @param servicePointName
	 *            the new service point name
	 */
	public void setServicePointName(String servicePointName) {
		this.servicePointName = servicePointName;
	}

	/**
	 * Gets the approval code.
	 * 
	 * @return the approval code
	 */
	public String getApprovalCode() {
		return approvalCode;
	}

	/**
	 * Sets the approval code.
	 * 
	 * @param approvalCode
	 *            the new approval code
	 */
	public void setApprovalCode(String approvalCode) {
		this.approvalCode = approvalCode;
	}

	/**
	 * Gets the inventory details.
	 * 
	 * @return the inventory details
	 */
	public Set<InventoryDetail> getInventoryDetails() {
		return inventoryDetails;
	}

	/**
	 * Sets the inventory details.
	 * 
	 * @param inventoryDetails
	 *            the new inventory details
	 */
	public void setInventoryDetails(Set<InventoryDetail> inventoryDetails) {
		this.inventoryDetails = inventoryDetails;
	}

	/**
	 * Sets the cancel date.
	 * 
	 * @param cancelDate the new cancel date
	 */
	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}

	/**
	 * Gets the cancel date.
	 * 
	 * @return the cancel date
	 */
	public Date getCancelDate() {
		return cancelDate;
	}

	/**
	 * Sets the operation type.
	 * 
	 * @param operationType the new operation type
	 */
	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}

	/**
	 * Gets the operation type.
	 * 
	 * @return the operation type
	 */
	public int getOperationType() {
		return operationType;
	}

}
