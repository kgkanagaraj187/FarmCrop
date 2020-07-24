/*
 * TruckStock.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;
import java.util.Set;

public class TruckStock {

    public static final String COMPLETED = "Completed";
    public static final String THEFT = "Theft";

    private long id;
    private Date createTime;
    private String truckId;
    private String sendStock;
    private String receivedStock;
    private String reason;
    private Set<TruckStockDetail> truckStockDetails;

    // Transient Variable
    private String remainingStock;
    private boolean isTransit;

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
     * Sets the truck id.
     * @param truckId the new truck id
     */
    public void setTruckId(String truckId) {

        this.truckId = truckId;
    }

    /**
     * Gets the truck id.
     * @return the truck id
     */
    public String getTruckId() {

        return truckId;
    }

    /**
     * Sets the send stock.
     * @param sendStock the new send stock
     */
    public void setSendStock(String sendStock) {

        this.sendStock = sendStock;
    }

    /**
     * Gets the send stock.
     * @return the send stock
     */
    public String getSendStock() {

        return sendStock;
    }

    /**
     * Sets the received stock.
     * @param receivedStock the new received stock
     */
    public void setReceivedStock(String receivedStock) {

        this.receivedStock = receivedStock;
    }

    /**
     * Gets the received stock.
     * @return the received stock
     */
    public String getReceivedStock() {

        return receivedStock;
    }

    /**
     * Sets the reason.
     * @param reason the new reason
     */
    public void setReason(String reason) {

        this.reason = reason;
    }

    /**
     * Gets the reason.
     * @return the reason
     */
    public String getReason() {

        return reason;
    }

    /**
     * Sets the truck stock details.
     * @param truckStockDetails the new truck stock details
     */
    public void setTruckStockDetails(Set<TruckStockDetail> truckStockDetails) {

        this.truckStockDetails = truckStockDetails;
    }

    /**
     * Gets the truck stock details.
     * @return the truck stock details
     */
    public Set<TruckStockDetail> getTruckStockDetails() {

        return truckStockDetails;
    }

    /**
     * Sets the remaining stock.
     * @param remainingStock the new remaining stock
     */
    public void setRemainingStock(String remainingStock) {

        this.remainingStock = remainingStock;
    }

    /**
     * Gets the remaining stock.
     * @return the remaining stock
     */
    public String getRemainingStock() {

        return String.valueOf((Double.parseDouble(getSendStock()) - Double
                .parseDouble(getReceivedStock())));

    }

    /**
     * Sets the transit.
     * @param isTransit the new transit
     */
    public void setTransit(boolean isTransit) {

        this.isTransit = isTransit;
    }

    /**
     * Checks if is transit.
     * @return true, if is transit
     */
    public boolean isTransit() {

        return isTransit;
    }

}
