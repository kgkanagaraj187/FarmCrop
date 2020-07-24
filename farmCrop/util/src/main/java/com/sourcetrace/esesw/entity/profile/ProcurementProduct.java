/*
 * ProcurementProduct.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import java.util.List;
import java.util.Set;

import com.sourcetrace.eses.entity.FarmCatalogue;

// TODO: Auto-generated Javadoc
public class ProcurementProduct {

    private long id;
    private String name;
    private String code;
    private long revisionNo;
    private int type;
    private Set<ProcurementVariety> procurementVarieties;
    private String branchId;
    //private String noOfDaysToGrow;
    private String cropCategory;
    private String cropType;
    private Double dryLoss;
    private Double gradingLoss;
    private String unit;
    private FarmCatalogue types;
    private Double mspRate;
    private Double mspPercentage;
    /**
	 * Transient variable
	 */
	private List<String> branchesList;
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
     * Gets the name.
     * @return the name
     */
/*    @Pattern(regexp = "[^\\p{Punct}]+$", message = "pattern.name")
    @NotEmpty(message = "empty.name")*/
    public String getName() {

        return name;
    }

    /**
     * Sets the name.
     * @param name the new name
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Gets the code.
     * @return the code
     */
  /*  @Pattern(regexp = "[^\\p{Punct}]+$", message = "pattern.code")
    @NotEmpty(message = "empty.code")*/
    public String getCode() {

        return code;
    }

    /**
     * Sets the code.
     * @param code the new code
     */
    public void setCode(String code) {

        this.code = code;
    }

    /**
     * Sets the revision no.
     * @param revisionNo the new revision no
     */
    public void setRevisionNo(long revisionNo) {

        this.revisionNo = revisionNo;
    }

    /**
     * Gets the revision no.
     * @return the revision no
     */
    public long getRevisionNo() {

        return revisionNo;
    }

    /**
     * Gets the type.
     * @return the type
     */
    public int getType() {

        return type;
    }

    /**
     * Sets the type.
     * @param type the new type
     */
    public void setType(int type) {

        this.type = type;
    }

    /**
     * Gets the procurement varieties.
     * @return the procurement varieties
     */
    public Set<ProcurementVariety> getProcurementVarieties() {

        return procurementVarieties;
    }

    /**
     * Sets the procurement varieties.
     * @param procurementVarieties the new procurement varieties
     */
    public void setProcurementVarieties(Set<ProcurementVariety> procurementVarieties) {

        this.procurementVarieties = procurementVarieties;
    }

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	/*public String getNoOfDaysToGrow() {
		return noOfDaysToGrow;
	}

	public void setNoOfDaysToGrow(String noOfDaysToGrow) {
		this.noOfDaysToGrow = noOfDaysToGrow;
	}*/

	public String getCropCategory() {
		return cropCategory;
	}

	public void setCropCategory(String cropCategory) {
		this.cropCategory = cropCategory;
	}

	public String getCropType() {
		return cropType;
	}

	public void setCropType(String cropType) {
		this.cropType = cropType;
	}

    public double getDryLoss() {
    
        return dryLoss;
    }

    public void setDryLoss(double dryLoss) {
    
        this.dryLoss = dryLoss;
    }

    public double getGradingLoss() {
    
        return gradingLoss;
    }

    public void setGradingLoss(double gradingLoss) {
    
        this.gradingLoss = gradingLoss;
    }

	public List<String> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public FarmCatalogue getTypes() {
		return types;
	}

	public void setTypes(FarmCatalogue types) {
		this.types = types;
	}

	public Double getMspRate() {
		return mspRate;
	}

	public void setMspRate(Double mspRate) {
		this.mspRate = mspRate;
	}

	public Double getMspPercentage() {
		return mspPercentage;
	}

	public void setMspPercentage(Double mspPercentage) {
		this.mspPercentage = mspPercentage;
	}

	

	
	
}
