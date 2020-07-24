/*
 * PMTAgentDetail.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import com.sourcetrace.eses.order.entity.profile.GradeMaster;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;

public class PMTAgentDetail {

    private Municipality city;
    private ProcurementProduct product;
    private String agentId;
    private GradeMaster gradeMaster;
    private long noOfBags;
    private double grossWeight;

    /**
     * Gets the city.
     * @return the city
     */
    public Municipality getCity() {

        return city;
    }

    /**
     * Sets the city.
     * @param city the new city
     */
    public void setCity(Municipality city) {

        this.city = city;
    }

    /**
     * Gets the product.
     * @return the product
     */
    public ProcurementProduct getProduct() {

        return product;
    }

    /**
     * Sets the product.
     * @param product the new product
     */
    public void setProduct(ProcurementProduct product) {

        this.product = product;
    }

    /**
     * Gets the agent id.
     * @return the agent id
     */
    public String getAgentId() {

        return agentId;
    }

    /**
     * Sets the agent id.
     * @param agentId the new agent id
     */
    public void setAgentId(String agentId) {

        this.agentId = agentId;
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
     * Gets the no of bags.
     * @return the no of bags
     */
    public long getNoOfBags() {

        return noOfBags;
    }

    /**
     * Sets the no of bags.
     * @param noOfBags the new no of bags
     */
    public void setNoOfBags(long noOfBags) {

        this.noOfBags = noOfBags;
    }

    /**
     * Gets the gross weight.
     * @return the gross weight
     */
    public double getGrossWeight() {

        return grossWeight;
    }

    /**
     * Sets the gross weight.
     * @param grossWeight the new gross weight
     */
    public void setGrossWeight(double grossWeight) {

        this.grossWeight = grossWeight;
    }

}
