/*
 * OfflineInventoryDetail.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

/**
 * The Class OfflineInventoryDetail.
 */
public class OfflineInventoryDetail {

	private long id;
	private String productCode;
	private String quantity;
	private String pricePerUnit;
	private String subTotal;
	private OfflineInventory offlineInventory;

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
	 * @param productCode the new product code
	 */
	public void setProductCode(String productCode) {
		this.productCode = productCode;
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
	 * @param quantity the new quantity
	 */
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	/**
	 * Gets the price per unit.
	 * 
	 * @return the price per unit
	 */
	public String getPricePerUnit() {
		return pricePerUnit;
	}

	/**
	 * Sets the price per unit.
	 * 
	 * @param pricePerUnit the new price per unit
	 */
	public void setPricePerUnit(String pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
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
	 * @param subTotal the new sub total
	 */
	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}

	/**
	 * Gets the offline inventory.
	 * 
	 * @return the offline inventory
	 */
	public OfflineInventory getOfflineInventory() {
		return offlineInventory;
	}

	/**
	 * Sets the offline inventory.
	 * 
	 * @param offlineInventory the new offline inventory
	 */
	public void setOfflineInventory(OfflineInventory offlineInventory) {
		this.offlineInventory = offlineInventory;
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
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {
		return id;
	}

}
