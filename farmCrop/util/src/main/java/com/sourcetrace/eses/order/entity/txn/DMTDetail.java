/*
 * DMTDetail.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import com.sourcetrace.eses.util.profile.Product;

public class DMTDetail {

    private long id;
    private Product product;
    private String transferedUnit;
    private double transferedQty;
    private String receivedUnit;
    private double receivedQty;
    private DMT dmt;

    // transient variables
    private String transitUnit;
    private double transitQty;

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
     * Sets the product.
     * @param product the new product
     */
    public void setProduct(Product product) {

        this.product = product;
    }

    /**
     * Gets the product.
     * @return the product
     */
    public Product getProduct() {

        return product;
    }

    /**
     * Sets the transfered unit.
     * @param transferedUnit the new transfered unit
     */
    public void setTransferedUnit(String transferedUnit) {

        this.transferedUnit = transferedUnit;
    }

    /**
     * Gets the transfered unit.
     * @return the transfered unit
     */
    public String getTransferedUnit() {

        return transferedUnit;
    }

    /**
     * Sets the transfered qty.
     * @param transferedQty the new transfered qty
     */
    public void setTransferedQty(double transferedQty) {

        this.transferedQty = transferedQty;
    }

    /**
     * Gets the transfered qty.
     * @return the transfered qty
     */
    public double getTransferedQty() {

        return transferedQty;
    }

    /**
     * Sets the received unit.
     * @param receivedUnit the new received unit
     */
    public void setReceivedUnit(String receivedUnit) {

        this.receivedUnit = receivedUnit;
    }

    /**
     * Gets the received unit.
     * @return the received unit
     */
    public String getReceivedUnit() {

        return receivedUnit;
    }

    /**
     * Sets the received qty.
     * @param receivedQty the new received qty
     */
    public void setReceivedQty(double receivedQty) {

        this.receivedQty = receivedQty;
    }

    /**
     * Gets the received qty.
     * @return the received qty
     */
    public double getReceivedQty() {

        return receivedQty;
    }

    /**
     * Sets the dmt.
     * @param dmt the new dmt
     */
    public void setDmt(DMT dmt) {

        this.dmt = dmt;
    }

    /**
     * Gets the dmt.
     * @return the dmt
     */
    public DMT getDmt() {

        return dmt;
    }

    /**
     * Gets the transit unit.
     * @return the transit unit
     */
    public String getTransitUnit() {

        return String.valueOf(Double.parseDouble(getTransferedUnit())
                - Double.parseDouble(getReceivedUnit()));
    }

    /**
     * Gets the transit qty.
     * @return the transit qty
     */
    public double getTransitQty() {

        return (getTransferedQty() - getReceivedQty());
    }

}
