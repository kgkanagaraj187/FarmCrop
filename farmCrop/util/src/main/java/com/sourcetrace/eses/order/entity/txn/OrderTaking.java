/*
 * OrderTaking.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;
import java.util.Set;

public class OrderTaking {

	public static final String DELIVERED = "1";
	public static final String PENDING = "2";

	private long id;
	private Date orderDate;
	private String agentId;
	private String agentName;
	private String deviceId;
	private String deviceName;
	private String branchId;
	private String branchName;
	private String customerId;
	private String storeName;
	private String firstName;
	private String lastName;
	private String address;
	private String location;
	private String totalOrder;
	private String totalOrderPrice;
	private String orderStatus;
	private Set<OrderDetail> orderDetails;

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	/**
	 * Gets the customer name.
	 * 
	 * @return the customer name
	 */
	public String getCustomerName() {

		StringBuffer name = new StringBuffer();
		if (firstName != null) {
			name.append(firstName);
			name.append(" ");
		}
		if (lastName != null) {
			name.append(lastName);
		}
		return name.toString();

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
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(long id) {
		this.id = id;
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
	 * Gets the branch id.
	 * 
	 * @return the branch id
	 */
	public String getBranchId() {
		return branchId;
	}

	/**
	 * Sets the branch id.
	 * 
	 * @param branchId
	 *            the new branch id
	 */
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	/**
	 * Gets the branch name.
	 * 
	 * @return the branch name
	 */
	public String getBranchName() {
		return branchName;
	}

	/**
	 * Sets the branch name.
	 * 
	 * @param branchName
	 *            the new branch name
	 */
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	/**
	 * Gets the customer id.
	 * 
	 * @return the customer id
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * Sets the customer id.
	 * 
	 * @param customerId
	 *            the new customer id
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * Gets the first name.
	 * 
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name.
	 * 
	 * @param firstName
	 *            the new first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the last name.
	 * 
	 * @return the last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name.
	 * 
	 * @param lastName
	 *            the new last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the address.
	 * 
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the address.
	 * 
	 * @param address
	 *            the new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Gets the location.
	 * 
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Sets the location.
	 * 
	 * @param location
	 *            the new location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Gets the total order.
	 * 
	 * @return the total order
	 */
	public String getTotalOrder() {
		return totalOrder;
	}

	/**
	 * Sets the total order.
	 * 
	 * @param totalOrder
	 *            the new total order
	 */
	public void setTotalOrder(String totalOrder) {
		this.totalOrder = totalOrder;
	}

	/**
	 * Gets the total order price.
	 * 
	 * @return the total order price
	 */
	public String getTotalOrderPrice() {
		return totalOrderPrice;
	}

	/**
	 * Sets the total order price.
	 * 
	 * @param totalOrderPrice
	 *            the new total order price
	 */
	public void setTotalOrderPrice(String totalOrderPrice) {
		this.totalOrderPrice = totalOrderPrice;
	}

	/**
	 * Gets the order status.
	 * 
	 * @return the order status
	 */
	public String getOrderStatus() {
		return orderStatus;
	}

	/**
	 * Sets the order status.
	 * 
	 * @param orderStatus
	 *            the new order status
	 */
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	/**
	 * Sets the order details.
	 * 
	 * @param orderDetails
	 *            the new order details
	 */
	public void setOrderDetails(Set<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	/**
	 * Gets the order details.
	 * 
	 * @return the order details
	 */
	public Set<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

}
