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


// TODO: Auto-generated Javadoc
public class ProcurementGradePricingHistory {

    private Long id;
    private ProcurementGrade procurementGrade;
    private Double price;
    private Long revisionNo;

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
     * Gets the procurement grade.
     * @return the procurement grade
     */
    public ProcurementGrade getProcurementGrade() {

        return procurementGrade;
    }

    /**
     * Sets the procurement grade.
     * @param procurementGrade the new procurement grade
     */
    public void setProcurementGrade(ProcurementGrade procurementGrade) {

        this.procurementGrade = procurementGrade;
    }

    /**
     * Gets the price.
     * @return the price
     */
    public Double getPrice() {

        return price;
    }

    /**
     * Sets the price.
     * @param price the new price
     */
    public void setPrice(Double price) {

        this.price = price;
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

}
