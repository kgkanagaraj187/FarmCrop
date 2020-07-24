/*
 * OrderPending.java
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

public class OrderPending implements Serializable {
	private long id;
	private String shopDealerId;
	private String shopDealerName;
	private Date orderDate;
	private String orderNo;
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

}
