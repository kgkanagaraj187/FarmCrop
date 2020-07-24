/*
 * OrderDelivery.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.profile;

import java.io.Serializable;
import java.util.Date;

public class OrderDelivery implements Serializable {
	
	private static final long serialVersionUID = 8834633708203065568L;
	private Long id;
	private Date orderDate;
	private String orderNo;
	private String shopDealerId;
	private String shopDealerName;
	private Double totalQty;
	private Double grandTotal;
	private Integer deliveryMode;
	private String status;
	private int operationType;

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id the new id
	 */
	public void setId(Long id) {
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
	 * @param orderDate the new order date
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
	 * @param orderNo the new order no
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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
	 * Gets the total qty.
	 * 
	 * @return the total qty
	 */
	public Double getTotalQty() {
		return totalQty;
	}

	/**
	 * Sets the total qty.
	 * 
	 * @param totalQty the new total qty
	 */
	public void setTotalQty(Double totalQty) {
		this.totalQty = totalQty;
	}

	/**
	 * Gets the grand total.
	 * 
	 * @return the grand total
	 */
	public Double getGrandTotal() {
		return grandTotal;
	}

	/**
	 * Sets the grand total.
	 * 
	 * @param grandTotal the new grand total
	 */
	public void setGrandTotal(Double grandTotal) {
		this.grandTotal = grandTotal;
	}

	/**
	 * Gets the delivery mode.
	 * 
	 * @return the delivery mode
	 */
	public Integer getDeliveryMode() {
		return deliveryMode;
	}

	/**
	 * Sets the delivery mode.
	 * 
	 * @param deliveryMode the new delivery mode
	 */
	public void setDeliveryMode(Integer deliveryMode) {
		this.deliveryMode = deliveryMode;
	}

	/**
	 * Gets the status.
	 * 
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 * 
	 * @param status the new status
	 */
	public void setStatus(String status) {
		this.status = status;
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
