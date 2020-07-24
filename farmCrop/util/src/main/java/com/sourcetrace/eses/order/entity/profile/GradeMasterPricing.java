/*
 * GradeMasterPricing.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.profile;

import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.Season;

/**
 * The Class GradeMasterPricing.
 */
public class GradeMasterPricing {

    private long id;
    private GradeMaster gradeMaster;
    private Season season;
    private Municipality area;
    private ProcurementProduct product;
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
     * Gets the season.
     * @return the season
     */
    public Season getSeason() {

        return season;
    }

    /**
     * Sets the season.
     * @param season the new season
     */
    public void setSeason(Season season) {

        this.season = season;
    }

    /**
     * Gets the area.
     * @return the area
     */
    public Municipality getArea() {

        return area;
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
     * Sets the area.
     * @param area the new area
     */
    public void setArea(Municipality area) {

        this.area = area;
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
