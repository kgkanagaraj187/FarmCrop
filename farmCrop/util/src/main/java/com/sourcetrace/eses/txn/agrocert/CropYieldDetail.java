/*
 * CropYieldDetail.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.txn.agrocert;

import com.sourcetrace.esesw.entity.profile.ProcurementProduct;

public class CropYieldDetail {

    private long id;
  //  private FarmCropsMaster farmCropsMaster;
    private ProcurementProduct procurementProduct;
    private String yield;
    private CropYield cropYield;
    private int status;
    private int type;

   // transient Variable
    private String moleculeName;
    private String moleculeValue;
    private String moleculeExpectedValue;

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
     * Sets the yield.
     * @param yield the new yield
     */
    public void setYield(String yield) {

        this.yield = yield;
    }

    /**
     * Gets the yield.
     * @return the yield
     */
    public String getYield() {

        return yield;
    }

    /**
     * Gets the crop yield.
     * @return the crop yield
     */
    public CropYield getCropYield() {

        return cropYield;
    }

    /**
     * Sets the crop yield.
     * @param cropYield the new crop yield
     */
    public void setCropYield(CropYield cropYield) {

        this.cropYield = cropYield;
    }

	public ProcurementProduct getProcurementProduct() {
		return procurementProduct;
	}

	public void setProcurementProduct(ProcurementProduct procurementProduct) {
		this.procurementProduct = procurementProduct;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMoleculeName() {
		return moleculeName;
	}

	public void setMoleculeName(String moleculeName) {
		this.moleculeName = moleculeName;
	}

	public String getMoleculeValue() {
		return moleculeValue;
	}

	public void setMoleculeValue(String moleculeValue) {
		this.moleculeValue = moleculeValue;
	}

	public String getMoleculeExpectedValue() {
		return moleculeExpectedValue;
	}

	public void setMoleculeExpectedValue(String moleculeExpectedValue) {
		this.moleculeExpectedValue = moleculeExpectedValue;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}


    
    
}
