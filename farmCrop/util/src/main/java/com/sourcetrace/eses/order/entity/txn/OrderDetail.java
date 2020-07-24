/*
 * OrderDetail.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

package com.sourcetrace.eses.order.entity.txn;

public class OrderDetail {

	private long id;
	private String categoryName;
	private String productName;
	private String quantity;
	private String price;
	private String subTotal;
	private OrderTaking orderTaking;

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
	 * Gets the category name.
	 * 
	 * @return the category name
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * Sets the category name.
	 * 
	 * @param categoryName
	 *            the new category name
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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
	 * Gets the quantity.
	 * 
	 * @return the quantity
	 */
	public String getQuantity() {
		return quantity;
	}

	/**
	 * Sets the quantity.
	 * 
	 * @param quantity
	 *            the new quantity
	 */
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	/**
	 * Gets the price.
	 * 
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}

	/**
	 * Sets the price.
	 * 
	 * @param price
	 *            the new price
	 */
	public void setPrice(String price) {
		this.price = price;
	}

	/**
	 * Gets the sub total.
	 * 
	 * @return the sub total
	 */
	public String getSubTotal() {
		return subTotal;
	}

	/**
	 * Sets the sub total.
	 * 
	 * @param subTotal
	 *            the new sub total
	 */
	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}

	/**
	 * @param orderTaking the orderTaking to set
	 */
	public void setOrderTaking(OrderTaking orderTaking) {
		this.orderTaking = orderTaking;
	}

	/**
	 * @return the orderTaking
	 */
	public OrderTaking getOrderTaking() {
		return orderTaking;
	}

}
