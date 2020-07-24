/*
 * GradeMaster.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.profile;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.sourcetrace.esesw.entity.profile.ProcurementProduct;

/**
 * The Class GradeMaster.
 */
public class GradeMaster {

    private long id;
    private String code;
    private String name;
    private ProcurementProduct product;
    private long revisionNo;

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
     * Sets the code.
     * @param code the new code
     */
    public void setCode(String code) {

        this.code = code;
    }

    /**
     * Gets the code.
     * @return the code
     */
    @Pattern(regexp = "[^\\p{Punct}]+$", message = "pattern.code")
    @NotEmpty(message = "empty.code")
    public String getCode() {

        return code;
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

}
