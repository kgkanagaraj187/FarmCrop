/*
 * PeriodicInspectionData.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;


public class PeriodicInspectionData {

    private long id;
    private String catalogueValue;
    private String type;
    private String otherCatalogueValueName;
    private String quantityValue;
    private PeriodicInspection periodicInspection;
    private String cocDone;
    private String uom;
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
     * Gets the type.
     * @return the type
     */
    public String getType() {

        return type;
    }

    /**
     * Sets the type.
     * @param type the new type
     */
    public void setType(String type) {

        this.type = type;
    }

    /**
     * Gets the other catalogue value name.
     * @return the other catalogue value name
     */
    public String getOtherCatalogueValueName() {

        return otherCatalogueValueName;
    }

    /**
     * Sets the other catalogue value name.
     * @param otherCatalogueValueName the new other catalogue value name
     */
    public void setOtherCatalogueValueName(String otherCatalogueValueName) {

        this.otherCatalogueValueName = otherCatalogueValueName;
    }

    /**
     * Gets the quantity value.
     * @return the quantity value
     */
    public String getQuantityValue() {

        return quantityValue;
    }

    /**
     * Sets the quantity value.
     * @param quantityValue the new quantity value
     */
    public void setQuantityValue(String quantityValue) {

        this.quantityValue = quantityValue;
    }

    /**
     * Gets the periodic inspection.
     * @return the periodic inspection
     */
    public PeriodicInspection getPeriodicInspection() {

        return periodicInspection;
    }

    /**
     * Sets the periodic inspection.
     * @param periodicInspection the new periodic inspection
     */
    public void setPeriodicInspection(PeriodicInspection periodicInspection) {

        this.periodicInspection = periodicInspection;
    }

	public String getCatalogueValue() {
		return catalogueValue;
	}

	public void setCatalogueValue(String catalogueValue) {
		this.catalogueValue = catalogueValue;
	}
   
	  public String getCocDone() {
		    
	        return cocDone;
	    }

	    public void setCocDone(String cocDone) {
	    
	        this.cocDone = cocDone;
	    }

		public String getUom() {
			return uom;
		}

		public void setUom(String uom) {
			this.uom = uom;
		}
	    
	    
}
