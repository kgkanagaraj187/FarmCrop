/*
 * WarehouseProduct.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.entity;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

import com.sourcetrace.eses.util.profile.Product;

// TODO: Auto-generated Javadoc
public class WarehouseProduct {

	public static enum StockType {
		WAREHOUSE_STOCK, AGENT_STOCK
	}

	private long id;
	private Warehouse warehouse;
	private Product product;
	private String unit;
	private double stock;
	private long revisionNo;
	private Agent agent;
	private double costPrice;
	private String batchNo;
	// private String lotNo;

	private Set<WarehouseProductDetail> warehouseDetails;
	private double damagedStock;
	// private long seed;
	// private double seedling;

	// Transit Varaibles
	private double txnQty;
	private boolean edit;
	private String receiptNumber;
	private String orderNo;
	private String userName;
	private String vendorId;
	private double damagedQty;
	private double totalAmt;
	private String branchId;
	private String seasonCode;
	
	/**
	 * Transient variable
	 */
	private List<String> branchesList;
	private String uomList;

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
	 * Gets the warehouse.
	 * 
	 * @return the warehouse
	 */
	public Warehouse getWarehouse() {

		return warehouse;
	}

	/**
	 * Sets the warehouse.
	 * 
	 * @param warehouse
	 *            the new warehouse
	 */
	public void setWarehouse(Warehouse warehouse) {

		this.warehouse = warehouse;
	}

	/**
	 * Gets the product.
	 * 
	 * @return the product
	 */
	public Product getProduct() {

		return product;
	}

	/**
	 * Sets the product.
	 * 
	 * @param product
	 *            the new product
	 */
	public void setProduct(Product product) {

		this.product = product;
	}

	/**
	 * Gets the stock.
	 * 
	 * @return the stock
	 */
	public double getStock() {

		return stock;
	}

	/**
	 * Sets the stock.
	 * 
	 * @param stock
	 *            the new stock
	 */
	public void setStock(double stock) {

		this.stock = stock;
	}

	/**
	 * Sets the revision no.
	 * 
	 * @param revisionNo
	 *            the new revision no
	 */
	public void setRevisionNo(long revisionNo) {

		this.revisionNo = revisionNo;
	}

	/**
	 * Gets the revision no.
	 * 
	 * @return the revision no
	 */
	public long getRevisionNo() {

		return revisionNo;
	}

	/**
	 * Gets the stock qty.
	 * 
	 * @return the stock qty
	 */
	public String getStockQty() {

		DecimalFormat formatter = new DecimalFormat("#.###");
		return formatter.format(stock);
	}

	/**
	 * Sets the warehouse details.
	 * 
	 * @param warehouseDetails
	 *            the new warehouse details
	 */
	public void setWarehouseDetails(Set<WarehouseProductDetail> warehouseDetails) {

		this.warehouseDetails = warehouseDetails;
	}

	/**
	 * Gets the warehouse details.
	 * 
	 * @return the warehouse details
	 */
	public Set<WarehouseProductDetail> getWarehouseDetails() {

		return warehouseDetails;
	}

	/**
	 * Sets the agent.
	 * 
	 * @param agent
	 *            the new agent
	 */
	public void setAgent(Agent agent) {

		this.agent = agent;
	}

	/**
	 * Gets the agent.
	 * 
	 * @return the agent
	 */
	public Agent getAgent() {

		return agent;
	}

	/**
	 * Sets the txn qty.
	 * 
	 * @param txnQty
	 *            the new txn qty
	 */
	public void setTxnQty(double txnQty) {

		this.txnQty = txnQty;
	}

	/**
	 * Gets the txn qty.
	 * 
	 * @return the txn qty
	 */
	public double getTxnQty() {

		return txnQty;
	}

	/**
	 * Sets the edit.
	 * 
	 * @param edit
	 *            the new edit
	 */
	public void setEdit(boolean edit) {

		this.edit = edit;
	}

	/**
	 * Checks if is edits the.
	 * 
	 * @return true, if is edits the
	 */
	public boolean isEdit() {

		return edit;
	}

	/**
	 * Gets the receipt number.
	 * 
	 * @return the receipt number
	 */
	public String getReceiptNumber() {

		return receiptNumber;
	}

	/**
	 * Sets the receipt number.
	 * 
	 * @param receiptNumber
	 *            the new receipt number
	 */
	public void setReceiptNumber(String receiptNumber) {

		this.receiptNumber = receiptNumber;
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
	 * Gets the user name.
	 * 
	 * @return the user name
	 */
	public String getUserName() {

		return userName;
	}

	/**
	 * Sets the user name.
	 * 
	 * @param userName
	 *            the new user name
	 */
	public void setUserName(String userName) {

		this.userName = userName;
	}

	/**
	 * Gets the cost price.
	 * 
	 * @return the cost price
	 */
	public double getCostPrice() {

		return costPrice;
	}

	/**
	 * Sets the cost price.
	 * 
	 * @param costPrice
	 *            the new cost price
	 */
	public void setCostPrice(double costPrice) {

		this.costPrice = costPrice;
	}

	/**
	 * Gets the vendor id.
	 * 
	 * @return the vendor id
	 */
	public String getVendorId() {

		return vendorId;
	}

	/**
	 * Sets the vendor id.
	 * 
	 * @param vendorId
	 *            the new vendor id
	 */
	public void setVendorId(String vendorId) {

		this.vendorId = vendorId;
	}

	/**
	 * Gets the damaged qty.
	 * 
	 * @return the damaged qty
	 */
	public double getDamagedQty() {

		return damagedQty;
	}

	/**
	 * Sets the damaged qty.
	 * 
	 * @param damagedQty
	 *            the new damaged qty
	 */
	public void setDamagedQty(double damagedQty) {

		this.damagedQty = damagedQty;
	}

	/**
	 * Gets the total amt.
	 * 
	 * @return the total amt
	 */
	public double getTotalAmt() {

		return totalAmt;
	}

	/**
	 * Sets the total amt.
	 * 
	 * @param totalAmt
	 *            the new total amt
	 */
	public void setTotalAmt(double totalAmt) {

		this.totalAmt = totalAmt;
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

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public List<String> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}

	public double getDamagedStock() {
		return damagedStock;
	}

	public void setDamagedStock(double damagedStock) {
		this.damagedStock = damagedStock;
	}

	public String getUomList() {
		return uomList;
	}

	public void setUomList(String uomList) {
		this.uomList = uomList;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

}
