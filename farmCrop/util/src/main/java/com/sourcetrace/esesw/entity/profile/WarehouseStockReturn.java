package com.sourcetrace.esesw.entity.profile;

import java.util.Date;
import java.util.Set;

import com.sourcetrace.eses.entity.Warehouse;


public class WarehouseStockReturn
{
	private long id;
    private Date trxnDate;
    private String receiptNo;
    private Warehouse warehouse;
    private Vendor vendor;
    private String orderNo;
    private Long totalDamagedStock;
    private Double totalAmount;
    private String paymentMode;
    private Double paymentAmount;
    private String remarks;
    private Long revisionNo;
    private String returnType;
    private Set<WarehouseStockReturnDetails> warehouseStockReturnDetails;
    private String seasonCode;

    //Transient Variable
    private Double creditBalance;
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
     * Gets the total damaged stock.
     * @return the total damaged stock
     */
    public Long getTotalDamagedStock() {

        return totalDamagedStock;
    }

    /**
     * Sets the total damaged stock.
     * @param totalDamagedStock the new total damaged stock
     */
    public void setTotalDamagedStock(Long totalDamagedStock) {

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
     * Gets the receipt no.
     * @return the revision no
     */
    public String getReceiptNo() {
        
        return receiptNo;
    }

    /**
     * Sets the receipt no.
     * @param receiptNo the new revision no
     */
    public void setReceiptNo(String receiptNo) {
    
        this.receiptNo = receiptNo;
    }

	public void setWarehouseStockReturnDetails(
			Set<WarehouseStockReturnDetails> warehouseStockReturnDetails) {
		this.warehouseStockReturnDetails = warehouseStockReturnDetails;
	}

	public Set<WarehouseStockReturnDetails> getWarehouseStockReturnDetails() {
		return warehouseStockReturnDetails;
	}

    public String getReturnType() {
    
        return returnType;
    }

    public void setReturnType(String returnType) {
    
        this.returnType = returnType;
    }

	public Double getCreditBalance() {
		return creditBalance;
	}

	public void setCreditBalance(Double creditBalance) {
		this.creditBalance = creditBalance;
	}

    public String getSeasonCode() {
    
        return seasonCode;
    }

    public void setSeasonCode(String seasonCode) {
    
        this.seasonCode = seasonCode;
    }
	
	

}
