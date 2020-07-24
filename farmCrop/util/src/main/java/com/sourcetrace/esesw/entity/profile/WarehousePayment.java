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

import java.util.Date;
import java.util.Set;

import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;

// TODO: Auto-generated Javadoc
/**
 * @author PANNEER
 */
public class WarehousePayment {

    private long id;
    private Date trxnDate;
    private Warehouse warehouse;
    private Vendor vendor;
    private String orderNo;
    private Double totalGoodStock;
    private Double totalDamagedStock;
    private Double totalAmount;
    private Double tax;
    private Double finalAmount;
    private String paymentMode;
    private Double paymentAmount;
    private String remarks;
    private Long revisionNo;
    private AgroTransaction agroTransaction;
    private Set<WarehousePaymentDetails> warehousePaymentDetails;
    private String seasonCode;

    private String receiptNo;
    private Double totalQty;
    private String branchId;
    
    
    private String transactionDate;
    /**
     * Gets the id.
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Gets the trxn date.
     * @return the trxn date
     */
    public Date getTrxnDate() {

        return trxnDate;
    }

    /**
     * Sets the trxn date.
     * @param trxnDate the new trxn date
     */
    public void setTrxnDate(Date trxnDate) {

        this.trxnDate = trxnDate;
    }

    /**
     * Gets the warehouse.
     * @return the warehouse
     */
    public Warehouse getWarehouse() {

        return warehouse;
    }

    /**
     * Sets the warehouse.
     * @param warehouse the new warehouse
     */
    public void setWarehouse(Warehouse warehouse) {

        this.warehouse = warehouse;
    }

    /**
     * Gets the vendor.
     * @return the vendor
     */
    public Vendor getVendor() {

        return vendor;
    }

    /**
     * Sets the vendor.
     * @param vendor the new vendor
     */
    public void setVendor(Vendor vendor) {

        this.vendor = vendor;
    }

    /**
     * Gets the order no.
     * @return the order no
     */
    public String getOrderNo() {

        return orderNo;
    }

    /**
     * Sets the order no.
     * @param orderNo the new order no
     */
    public void setOrderNo(String orderNo) {

        this.orderNo = orderNo;
    }

    /**
     * Gets the total good stock.
     * @return the total good stock
     */
    public Double getTotalGoodStock() {

        return totalGoodStock;
    }

    /**
     * Sets the total good stock.
     * @param totalGoodStock the new total good stock
     */
    public void setTotalGoodStock(Double totalGoodStock) {

        this.totalGoodStock = totalGoodStock;
    }

    /**
     * Gets the total damaged stock.
     * @return the total damaged stock
     */
    public Double getTotalDamagedStock() {

        return totalDamagedStock;
    }

    /**
     * Sets the total damaged stock.
     * @param totalDamagedStock the new total damaged stock
     */
    public void setTotalDamagedStock(Double totalDamagedStock) {

        this.totalDamagedStock = totalDamagedStock;
    }

    /**
     * Gets the total amount.
     * @return the total amount
     */
    public Double getTotalAmount() {

        return totalAmount;
    }

    /**
     * Sets the total amount.
     * @param totalAmount the new total amount
     */
    public void setTotalAmount(Double totalAmount) {

        this.totalAmount = totalAmount;
    }

    /**
     * Gets the tax.
     * @return the tax
     */
    public Double getTax() {

        return tax;
    }

    /**
     * Sets the tax.
     * @param tax the new tax
     */
    public void setTax(Double tax) {

        this.tax = tax;
    }

    /**
     * Gets the final amount.
     * @return the final amount
     */
    public Double getFinalAmount() {

        return finalAmount;
    }

    /**
     * Sets the final amount.
     * @param finalAmount the new final amount
     */
    public void setFinalAmount(Double finalAmount) {

        this.finalAmount = finalAmount;
    }

    /**
     * Gets the payment mode.
     * @return the payment mode
     */
    public String getPaymentMode() {

        return paymentMode;
    }

    /**
     * Sets the payment mode.
     * @param paymentMode the new payment mode
     */
    public void setPaymentMode(String paymentMode) {

        this.paymentMode = paymentMode;
    }

    /**
     * Gets the payment amount.
     * @return the payment amount
     */
    public Double getPaymentAmount() {

        return paymentAmount;
    }

    /**
     * Sets the payment amount.
     * @param paymentAmount the new payment amount
     */
    public void setPaymentAmount(Double paymentAmount) {

        this.paymentAmount = paymentAmount;
    }

    /**
     * Gets the remarks.
     * @return the remarks
     */
    public String getRemarks() {

        return remarks;
    }

    /**
     * Sets the remarks.
     * @param remarks the new remarks
     */
    public void setRemarks(String remarks) {

        this.remarks = remarks;
    }

    /**
     * Gets the revision no.
     * @return the revision no
     */
    public Long getRevisionNo() {

        return revisionNo;
    }

    /**
     * Sets the revision no.
     * @param revisionNo the new revision no
     */
    public void setRevisionNo(Long revisionNo) {

        this.revisionNo = revisionNo;
    }

    /**
     * Gets the agro transaction.
     * @return the agro transaction
     */
    public AgroTransaction getAgroTransaction() {

        return agroTransaction;
    }

    /**
     * Sets the agro transaction.
     * @param agroTransaction the new agro transaction
     */
    public void setAgroTransaction(AgroTransaction agroTransaction) {

        this.agroTransaction = agroTransaction;
    }

    
    public Set<WarehousePaymentDetails> getWarehousePaymentDetails() {
    
        return warehousePaymentDetails;
    }

    public void setWarehousePaymentDetails(Set<WarehousePaymentDetails> warehousePaymentDetails) {
    
        this.warehousePaymentDetails = warehousePaymentDetails;
    }

    public String getReceiptNo() {
    
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
    
        this.receiptNo = receiptNo;
    }

    public Double getTotalQty() {
    
        return totalQty;
    }

    public void setTotalQty(Double totalQty) {
    
        this.totalQty = totalQty;
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

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}    

	
}
