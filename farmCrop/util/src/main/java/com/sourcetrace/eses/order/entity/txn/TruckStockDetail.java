/*
 * TruckStockDetail.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;

public class TruckStockDetail {

    private long id;
    private Date createTime;
    private String driverId;
    private String uniqueSeqId;
    private String stock;
    private int type;
    private TruckStock truckStock;

    /**
     * Sets the id.
     * @param id the new id
     */
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
     * Sets the creates the time.
     * @param createTime the new creates the time
     */
    public void setCreateTime(Date createTime) {

        this.createTime = createTime;
    }

    /**
     * Gets the creates the time.
     * @return the creates the time
     */
    public Date getCreateTime() {

        return createTime;
    }

    /**
     * Sets the driver id.
     * @param driverId the new driver id
     */
    public void setDriverId(String driverId) {

        this.driverId = driverId;
    }

    /**
     * Gets the driver id.
     * @return the driver id
     */
    public String getDriverId() {

        return driverId;
    }

    /**
     * Sets the unique seq id.
     * @param uniqueSeqId the new unique seq id
     */
    public void setUniqueSeqId(String uniqueSeqId) {

        this.uniqueSeqId = uniqueSeqId;
    }

    /**
     * Gets the unique seq id.
     * @return the unique seq id
     */
    public String getUniqueSeqId() {

        return uniqueSeqId;
    }

    /**
     * Sets the stock.
     * @param stock the new stock
     */
    public void setStock(String stock) {

        this.stock = stock;
    }

    /**
     * Gets the stock.
     * @return the stock
     */
    public String getStock() {

        return stock;
    }

    public void setType(int type) {

        this.type = type;
    }

    public int getType() {

        return type;
    }

    /**
     * Sets the truck stock.
     * @param truckStock the new truck stock
     */
    public void setTruckStock(TruckStock truckStock) {

        this.truckStock = truckStock;
    }

    /**
     * Gets the truck stock.
     * @return the truck stock
     */
    public TruckStock getTruckStock() {

        return truckStock;
    }
}
