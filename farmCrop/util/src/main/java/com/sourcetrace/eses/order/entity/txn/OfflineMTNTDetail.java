/*
 * OfflineMTNTDetail.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.io.Serializable;

public class OfflineMTNTDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    private long id;
    private String productCode;
    private String villageCode;
    private String numberOfBags;
    private String grossWeight;
    private String tareWeight;
    private String netWeight;
    private String mode;
    private String quality;
    private OfflineMTNT offlineMTNT;
    private String pricePerUnit;
    private String subTotal;
    private String uom;
    private String icsCode;
    private String cooperativeId;

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
     * Gets the product code.
     * @return the product code
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * Sets the product code.
     * @param productCode the new product code
     */
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    /**
     * Gets the village code.
     * @return the village code
     */
    public String getVillageCode() {
        return villageCode;
    }

    /**
     * Sets the village code.
     * @param villageCode the new village code
     */
    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    /**
     * Gets the number of bags.
     * @return the number of bags
     */
    public String getNumberOfBags() {
        return numberOfBags;
    }

    /**
     * Sets the number of bags.
     * @param numberOfBags the new number of bags
     */
    public void setNumberOfBags(String numberOfBags) {
        this.numberOfBags = numberOfBags;
    }

    /**
     * Gets the gross weight.
     * @return the gross weight
     */
    public String getGrossWeight() {
        return grossWeight;
    }

    /**
     * Sets the gross weight.
     * @param grossWeight the new gross weight
     */
    public void setGrossWeight(String grossWeight) {
        this.grossWeight = grossWeight;
    }

    /**
     * Gets the tare weight.
     * @return the tare weight
     */
    public String getTareWeight() {
        return tareWeight;
    }

    /**
     * Sets the tare weight.
     * @param tareWeight the new tare weight
     */
    public void setTareWeight(String tareWeight) {
        this.tareWeight = tareWeight;
    }

    /**
     * Gets the net weight.
     * @return the net weight
     */
    public String getNetWeight() {
        return netWeight;
    }

    /**
     * Sets the net weight.
     * @param netWeight the new net weight
     */
    public void setNetWeight(String netWeight) {
        this.netWeight = netWeight;
    }

    /**
     * Gets the mode.
     * @return the mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * Sets the mode.
     * @param mode the new mode
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * Gets the offline mtnt.
     * @return the offline mtnt
     */
    public OfflineMTNT getOfflineMTNT() {
        return offlineMTNT;
    }

    /**
     * Sets the offline mtnt.
     * @param offlineMTNT the new offline mtnt
     */
    public void setOfflineMTNT(OfflineMTNT offlineMTNT) {
        this.offlineMTNT = offlineMTNT;
    }

    /**
     * Sets the quality.
     * @param quality the new quality
     */
    public void setQuality(String quality) {
        this.quality = quality;
    }

    /**
     * Gets the quality.
     * @return the quality
     */
    public String getQuality() {
        return quality;
    }

	public String getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(String pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public String getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public String getIcsCode() {
		return icsCode;
	}

	public void setIcsCode(String icsCode) {
		this.icsCode = icsCode;
	}

	public String getCooperativeId() {
		return cooperativeId;
	}

	public void setCooperativeId(String cooperativeId) {
		this.cooperativeId = cooperativeId;
	}
    
    
}
