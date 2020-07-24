/*
 * CertificateCategory.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.inspect.agrocert;

import java.util.Set;

/**
 * The Class CertificateCategory.
 */
public class CertificateCategory {

    private long id;
    private String code;
    private String name;
    private Set<CertificateStandard> standards;

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
     * Gets the standards.
     * @return the standards
     */
    public Set<CertificateStandard> getStandards() {

        return standards;
    }

    /**
     * Sets the standards.
     * @param standards the new standards
     */
    public void setStandards(Set<CertificateStandard> standards) {

        this.standards = standards;
    }

}
