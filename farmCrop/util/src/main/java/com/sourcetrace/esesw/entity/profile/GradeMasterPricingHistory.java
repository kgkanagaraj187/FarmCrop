/*
 * GradePricingHistory.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import java.util.Date;

import com.sourcetrace.eses.order.entity.profile.GradeMaster;

public class GradeMasterPricingHistory {

    private long id;
    private Date date;
    private ProcurementProduct product;
    private GradeMaster gradeMaster;
    private double price;

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
     * Sets the product.
     * @param product the new product
     */
    public void setProduct(ProcurementProduct product) {

        this.product = product;
    }

    /**
     * Gets the product.
     * @return the product
     */
    public ProcurementProduct getProduct() {

        return product;
    }

    /**
     * Sets the grade master.
     * @param gradeMaster the new grade master
     */
    public void setGradeMaster(GradeMaster gradeMaster) {

        this.gradeMaster = gradeMaster;
    }

    /**
     * Gets the grade master.
     * @return the grade master
     */
    public GradeMaster getGradeMaster() {

        return gradeMaster;
    }

    /**
     * Sets the price.
     * @param price the new price
     */
    public void setPrice(double price) {

        this.price = price;
    }

    /**
     * Gets the price.
     * @return the price
     */
    public double getPrice() {

        return price;
    }

}
