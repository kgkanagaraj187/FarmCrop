/*
 * MTNTDetail.java
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
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.Village;

public class MTNTDetail implements Serializable {

    private static final long serialVersionUID = 3039005395120898544L;

    public static enum Mode {
        FULL, PARTIAL, NON
    }

    private long id;
    private ProcurementProduct procurementProduct;
    private Village village;
    private MTNT mtnt;
    private long numberOfBags;
    private double grossWeight;
    private double tareWeight;
    private double netWeight;
    private int mode;
    private GradeMaster gradeMaster;

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
     * Gets the procurement product.
     * @return the procurement product
     */
    public ProcurementProduct getProcurementProduct() {

        return procurementProduct;
    }

    /**
     * Sets the procurement product.
     * @param procurementProduct the new procurement product
     */
    public void setProcurementProduct(ProcurementProduct procurementProduct) {

        this.procurementProduct = procurementProduct;
    }

    /**
     * Sets the village.
     * @param village the new village
     */
    public void setVillage(Village village) {

        this.village = village;
    }

    /**
     * Gets the village.
     * @return the village
     */
    public Village getVillage() {

        return village;
    }

    /**
     * Sets the mtnt.
     * @param mtnt the new mtnt
     */
    public void setMtnt(MTNT mtnt) {

        this.mtnt = mtnt;
    }

    /**
     * Gets the mtnt.
     * @return the mtnt
     */
    public MTNT getMtnt() {

        return mtnt;
    }

    /**
     * Sets the number of bags.
     * @param numberOfBags the new number of bags
     */
    public void setNumberOfBags(long numberOfBags) {

        this.numberOfBags = numberOfBags;
    }

    /**
     * Gets the number of bags.
     * @return the number of bags
     */
    public long getNumberOfBags() {

        return numberOfBags;
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

    /**
     * Gets the tare weight.
     * @return the tare weight
     */
    public double getTareWeight() {

        return tareWeight;
    }

    /**
     * Sets the tare weight.
     * @param tareWeight the new tare weight
     */
    public void setTareWeight(double tareWeight) {

        this.tareWeight = tareWeight;
    }

    /**
     * Gets the net weight.
     * @return the net weight
     */
    public double getNetWeight() {

        return netWeight;
    }

    /**
     * Sets the net weight.
     * @param netWeight the new net weight
     */
    public void setNetWeight(double netWeight) {

        this.netWeight = netWeight;
    }

    /**
     * Gets the mode.
     * @return the mode
     */
    public int getMode() {

        return mode;
    }

    /**
     * Sets the mode.
     * @param mode the new mode
     */
    public void setMode(int mode) {

        this.mode = mode;
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

}
