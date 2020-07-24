/*
 * PricePatternDetail.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.io.Serializable;

import com.sourcetrace.eses.order.entity.profile.GradeMaster;

public class PricePatternDetail implements Serializable {
    private long id;
    private GradeMaster gradeMaster;
    private PricePattern pricePattern;
    private double price;

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
     * Gets the grade master.
     * @return the grade master
     */
    public GradeMaster getGradeMaster() {

        return gradeMaster;
    }

    /**
     * Sets the grade master.
     * @param gradeMaster the new grade master
     */
    public void setGradeMaster(GradeMaster gradeMaster) {

        this.gradeMaster = gradeMaster;
    }

    /**
     * Gets the price pattern.
     * @return the price pattern
     */
    public PricePattern getPricePattern() {

        return pricePattern;
    }

    /**
     * Sets the price pattern.
     * @param pricePattern the new price pattern
     */
    public void setPricePattern(PricePattern pricePattern) {

        this.pricePattern = pricePattern;
    }

    /**
     * Gets the price.
     * @return the price
     */
    public double getPrice() {

        return price;
    }

    /**
     * Sets the price.
     * @param price the new price
     */
    public void setPrice(double price) {

        this.price = price;
    }

}
