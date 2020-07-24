/*
 * ProductPending.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.io.Serializable;

public class ProductPending implements Serializable {
	private long id;
	private String productCode;
	private String productName;
	private String productUnit;
	private double orderQty;
	private double deliveryQty;
	private double pendingQty;

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
	 * Gets the product code.
	 * 
	 * @return the product code
	 */
	public String getProductCode() {
		return productCode;
	}

	/**
	 * Sets the product code.
	 * 
	 * @param productCode
	 *            the new product code
	 */
	public void setProductCode(String productCode) {
		this.productCode = productCode;
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
	 * @param productName
	 *            the new product name
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
	 * @param orderQty
	 *            the new order qty
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
	 * @param deliveryQty
	 *            the new delivery qty
	 */
	public void setDeliveryQty(double deliveryQty) {
		this.deliveryQty = deliveryQty;
	}

	/**
	 * Gets the pending qty.
	 * 
	 * @return the pending qty
	 */
	public double getPendingQty() {
		return pendingQty;
	}

	/**
	 * Sets the pending qty.
	 * 
	 * @param pendingQty
	 *            the new pending qty
	 */
	public void setPendingQty(double pendingQty) {
		this.pendingQty = pendingQty;
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
	 * @param productUnit
	 *            the new product unit
	 */
	public void setProductUnit(String productUnit) {
		this.productUnit = productUnit;
	}

}
