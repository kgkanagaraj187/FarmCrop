/*
 * OrderPendingDetail.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

public class OrderPendingDetail {
	private long id;
	private String orderNo;
	private String productName;
	private String productUnit;
	private double orderQty;
	private double deliveryQty;
	private double balanceQty;

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
	 * Gets the product name.
	 * 
	 * @return the product name
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * Sets the product name.
	 * 
	 * @param productName the new product name
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * Gets the order qty.
	 * 
	 * @return the order qty
	 */
	public double getOrderQty() {
		return orderQty;
	}

	/**
	 * Sets the order qty.
	 * 
	 * @param orderQty the new order qty
	 */
	public void setOrderQty(double orderQty) {
		this.orderQty = orderQty;
	}

	/**
	 * Gets the delivery qty.
	 * 
	 * @return the delivery qty
	 */
	public double getDeliveryQty() {
		return deliveryQty;
	}

	/**
	 * Sets the delivery qty.
	 * 
	 * @param deliveryQty the new delivery qty
	 */
	public void setDeliveryQty(double deliveryQty) {
		this.deliveryQty = deliveryQty;
	}

	/**
	 * Gets the balance qty.
	 * 
	 * @return the balance qty
	 */
	public double getBalanceQty() {
		return balanceQty;
	}

	/**
	 * Sets the balance qty.
	 * 
	 * @param balanceQty the new balance qty
	 */
	public void setBalanceQty(double balanceQty) {
		this.balanceQty = balanceQty;
	}

	/**
	 * Gets the product unit.
	 * 
	 * @return the product unit
	 */
	public String getProductUnit() {
		return productUnit;
	}

	/**
	 * Sets the product unit.
	 * 
	 * @param productUnit the new product unit
	 */
	public void setProductUnit(String productUnit) {
		this.productUnit = productUnit;
	}
	
	
}
