/*
 * PricePattern.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.Season;

public class PricePattern implements Serializable {
    private long id;
    private String code;
    private String name;
    private Season season;
    private ProcurementProduct procurementProduct;
    private Date createdDate;
    private Date updatedDate;
    private long revisionNo;
    private Set<PricePatternDetail> pricePatternDetails;

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
     * Gets the code.
     * @return the code
     */
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
    @Pattern(regexp = "[^\\p{Punct}]+$", message = "pattern.name")
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
     * Gets the created date.
     * @return the created date
     */
    public Date getCreatedDate() {

        return createdDate;
    }

    /**
     * Sets the created date.
     * @param createdDate the new created date
     */
    public void setCreatedDate(Date createdDate) {

        this.createdDate = createdDate;
    }

    /**
     * Gets the updated date.
     * @return the updated date
     */
    public Date getUpdatedDate() {

        return updatedDate;
    }

    /**
     * Sets the updated date.
     * @param updatedDate the new updated date
     */
    public void setUpdatedDate(Date updatedDate) {

        this.updatedDate = updatedDate;
    }

    /**
     * Gets the revision no.
     * @return the revision no
     */
    public long getRevisionNo() {

        return revisionNo;
    }

    /**
     * Sets the revision no.
     * @param revisionNo the new revision no
     */
    public void setRevisionNo(long revisionNo) {

        this.revisionNo = revisionNo;
    }

    /**
     * Gets the price pattern details.
     * @return the price pattern details
     */
    public Set<PricePatternDetail> getPricePatternDetails() {

        return pricePatternDetails;
    }

    /**
     * Sets the price pattern details.
     * @param pricePatternDetails the new price pattern details
     */
    public void setPricePatternDetails(Set<PricePatternDetail> pricePatternDetails) {

        this.pricePatternDetails = pricePatternDetails;
    }
    
    public String getNameWithCode(){
        StringBuffer sb = new StringBuffer();
        sb.append(name);
        if(!StringUtil.isEmpty(code))
            sb.append(" - ").append(code);
        return sb.toString();
    }

}
