/*
 * WarehouseProductDetail.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.entity;

import java.util.Date;

// TODO: Auto-generated Javadoc
public class WarehouseProductDetail {

    private long id;
    private String receiptNo;
    private Date date;
    private String desc;
    private String orderNo;
    private String vendorId;
    private double costPrice;
    private String status;
    private String userName;
    private double prevStock;
    private double txnStock;
    private double finalStock;
    private double damagedQty;
     

	private WarehouseProduct warehouseProduct;

   
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Gets the id.
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Sets the receipt no.
     * @param receiptNo the new receipt no
     */
    public void setReceiptNo(String receiptNo) {

        this.receiptNo = receiptNo;
    }

    /**
     * Gets the receipt no.
     * @return the receipt no
     */
    public String getReceiptNo() {

        return receiptNo;
    }

    /**
     * Sets the date.
     * @param date the new date
     */
    public void setDate(Date date) {

        this.date = date;
    }

    /**
     * Gets the date.
     * @return the date
     */
    public Date getDate() {

        return date;
    }

    /**
     * Sets the desc.
     * @param desc the new desc
     */
    public void setDesc(String desc) {

        this.desc = desc;
    }

    /**
     * Gets the desc.
     * @return the desc
     */
    public String getDesc() {

        return desc;
    }

    /**
     * Sets the prev stock.
     * @param prevStock the new prev stock
     */
    public void setPrevStock(double prevStock) {

        this.prevStock = prevStock;
    }

    /**
     * Gets the prev stock.
     * @return the prev stock
     */
    public double getPrevStock() {

        return prevStock;
    }

    /**
     * Sets the txn stock.
     * @param txnStock the new txn stock
     */
    public void setTxnStock(double txnStock) {

        this.txnStock = txnStock;
    }

    /**
     * Gets the txn stock.
     * @return the txn stock
     */
    public double getTxnStock() {

        return txnStock;
    }

    /**
     * Sets the final stock.
     * @param finalStock the new final stock
     */
    public void setFinalStock(double finalStock) {

        this.finalStock = finalStock;
    }

    /**
     * Gets the final stock.
     * @return the final stock
     */
    public double getFinalStock() {

        return finalStock;
    }

    /**
     * Sets the warehouse product.
     * @param warehouseProduct the new warehouse product
     */
    public void setWarehouseProduct(WarehouseProduct warehouseProduct) {

        this.warehouseProduct = warehouseProduct;
    }

    /**
     * Gets the warehouse product.
     * @return the warehouse product
     */
    public WarehouseProduct getWarehouseProduct() {

        return warehouseProduct;
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
     * Gets the user name.
     * @return the user name
     */
    public String getUserName() {

        return userName;
    }

    /**
     * Sets the user name.
     * @param userName the new user name
     */
    public void setUserName(String userName) {

        this.userName = userName;
    }

    /**
     * Gets the vendor id.
     * @return the vendor id
     */
    public String getVendorId() {

        return vendorId;
    }

    /**
     * Sets the vendor id.
     * @param vendorId the new vendor id
     */
    public void setVendorId(String vendorId) {

        this.vendorId = vendorId;
    }

    /**
     * Gets the cost price.
     * @return the cost price
     */
    public double getCostPrice() {

        return costPrice;
    }

    /**
     * Sets the cost price.
     * @param costPrice the new cost price
     */
    public void setCostPrice(double costPrice) {

        this.costPrice = costPrice;
    }

    /**
     * Gets the status.
     * @return the status
     */
    public String getStatus() {

        return status;
    }

    /**
     * Sets the status.
     * @param status the new status
     */
    public void setStatus(String status) {

        this.status = status;
    }

    /**
     * Gets the damaged qty.
     * @return the damaged qty
     */
    public double getDamagedQty() {

        return damagedQty;
    }

    /**
     * Sets the damaged qty.
     * @param damagedQty the new damaged qty
     */
    public void setDamagedQty(double damagedQty) {

        this.damagedQty = damagedQty;
    }

}
