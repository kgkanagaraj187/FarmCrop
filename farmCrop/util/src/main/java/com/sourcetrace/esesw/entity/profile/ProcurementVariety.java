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

import java.util.Set;

import org.hibernate.validator.constraints.NotEmpty;

// TODO: Auto-generated Javadoc
public class ProcurementVariety {

    private long id;
    private ProcurementProduct procurementProduct;
    private String code;
    private String name;
    private Long revisionNo;
    private Set<ProcurementGrade> procurementGrades;
    private String yield;
    private String noDaysToGrow;
    private String harvestDays;
    private String branchId;

    /**
     * Gets the id.
     * @return the id
     */
    public Long getId() {

        return id;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(Long id) {

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
     * Gets the code.
     * @return the code
     */
   // @Pattern(regex = "[^\\p{Punct}]+$", message = "pattern.code")
    @NotEmpty(message = "empty.code")
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
     * Gets the name.
     * @return the name
     */
    //@Pattern(regex = "[^\\p{Punct}]+$", message = "pattern.name")
    @NotEmpty(message = "empty.name")
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
     * Gets the revision no.
     * @return the revision no
     */
    public Long getRevisionNo() {

        return revisionNo;
    }

    /**
     * Sets the revision no.
     * @param revisionNo the new revision no
     */
    public void setRevisionNo(Long revisionNo) {

        this.revisionNo = revisionNo;
    }

    /**
     * Gets the procurement grades.
     * @return the procurement grades
     */
    public Set<ProcurementGrade> getProcurementGrades() {

        return procurementGrades;
    }

    /**
     * Sets the procurement grades.
     * @param procurementGrades the new procurement grades
     */
    public void setProcurementGrades(Set<ProcurementGrade> procurementGrades) {

        this.procurementGrades = procurementGrades;
    }

	public String getYield() {
		return yield;
	}

	public void setYield(String yield) {
		this.yield = yield;
	}

	public String getNoDaysToGrow() {
		return noDaysToGrow;
	}

	public void setNoDaysToGrow(String noDaysToGrow) {
		this.noDaysToGrow = noDaysToGrow;
	}

	public String getHarvestDays() {
		return harvestDays;
	}

	public void setHarvestDays(String harvestDays) {
		this.harvestDays = harvestDays;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	
	

	
}
