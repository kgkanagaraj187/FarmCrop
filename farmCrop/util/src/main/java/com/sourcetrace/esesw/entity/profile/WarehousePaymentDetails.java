/*
 * WarehouseProduct.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import java.util.List;

import com.sourcetrace.eses.util.profile.Product;

// TODO: Auto-generated Javadoc
/**
 * @author PANNEER
 */
public class WarehousePaymentDetails {

	private long id;
	private WarehousePayment warehousePayment;
	private Product product;
	private Double costPrice;
	private Double stock;
	private Double damagedStock;
	private String expDate;
	private String receiptNo;
	private String batchNo;
	private double totalStock;
	private Double totalAmount;
	private Double amount;
	private String branchId;


	private List<String> branchesList;
// Transient 
	  private long paymentId;
	    private double avlStock;
	    private double damagedAvlStock;
	    private long warehouseProductId;
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
	 * Gets the warehouse payment.
	 * 
	 * @return the warehouse payment
	 */
	public WarehousePayment getWarehousePayment() {

		return warehousePayment;
	}

	/**sagiSeed
	 * Sets the warehouse payment.
	 * 
	 * @param warehousePayment
	 *            the new warehouse payment
	 */
	public void setWarehousePayment(WarehousePayment warehousePayment) {

		this.warehousePayment = warehousePayment;
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
	 * Gets the cost price.
	 * 
	 * @return the cost price
	 */
	public Double getCostPrice() {

		return costPrice;
	}

	/**
	 * Sets the cost price.
	 * 
	 * @param costPrice
	 *            the new cost price
	 */
	public void setCostPrice(Double costPrice) {

		this.costPrice = costPrice;
	}

	/**
	 * Gets the stock.
	 * 
	 * @return the stock
	 */
	public Double getStock() {

		return stock;
	}

	/**
	 * Sets the stock.
	 * 
	 * @param stock
	 *            the new stock
	 */
	public void setStock(Double stock) {

		this.stock = stock;
	}

	/**
	 * Gets the damaged stock.
	 * 
	 * @return the damaged stock
	 */
	public Double getDamagedStock() {

		return damagedStock;
	}

	/**
	 * Sets the damaged stock.
	 * 
	 * @param damagedStock
	 *            the new damaged stock
	 */
	public void setDamagedStock(Double damagedStock) {

		this.damagedStock = damagedStock;
	}

	/**
	 * Gets the total stock.
	 * 
	 * @return the total stock
	 */
	public Double getTotalStock() {

		if (this.stock > 0) {
			totalStock = totalStock + this.stock;
		} else if (this.damagedStock > 0) {
			totalStock = totalStock + this.damagedStock;
		}
		return totalStock;
	}

	/**
	 * Gets the total amount.
	 * 
	 * @return the total amount
	 */
	public Double getTotalAmount() {

		if (this.costPrice > 0) {
			if (getTotalStock() > 0) {
				totalAmount = getTotalStock() * this.costPrice;
			}
		}
		return totalAmount;
	}

	/**
	 * Gets the exp date.
	 * 
	 * @return the exp date
	 */
	public String getExpDate() {

		return expDate;
	}

	/**
	 * Sets the exp date.
	 * 
	 * @param expDate
	 *            the new exp date
	 */
	public void setExpDate(String expDate) {

		this.expDate = expDate;
	}

	public void setTotalAmount(Double totalAmount) {

		this.totalAmount = totalAmount;
	}

	public String getReceiptNo() {

		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {

		this.receiptNo = receiptNo;
	}

	public Double getAmount() {

		return amount;
	}

	public void setAmount(Double amount) {

		this.amount = amount;
	}

	public void setTotalStock(Long totalStock) {

		this.totalStock = totalStock;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
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

	public long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(long paymentId) {
		this.paymentId = paymentId;
	}

	public double getAvlStock() {
		return avlStock;
	}

	public void setAvlStock(double avlStock) {
		this.avlStock = avlStock;
	}

	public double getDamagedAvlStock() {
		return damagedAvlStock;
	}

	public void setDamagedAvlStock(double damagedAvlStock) {
		this.damagedAvlStock = damagedAvlStock;
	}

	public long getWarehouseProductId() {
		return warehouseProductId;
	}

	public void setWarehouseProductId(long warehouseProductId) {
		this.warehouseProductId = warehouseProductId;
	}

	public void setTotalStock(double totalStock) {
		this.totalStock = totalStock;
	}

}
