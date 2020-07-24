/*
 * DeploymentLog.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.util.log;

import java.util.Date;

public class DeploymentLog {
    private Long id;
    private Date date;
    private String version;
    private String module;

    /**
     * Instantiates a new deployment log.
     */
    public DeploymentLog() {

    }

    /**
     * Instantiates a new deployment log.
     * @param module the module
     * @param version the version
     */
    public DeploymentLog(String module, String version) {

        this.date = new Date();
        this.module = module;
        this.version = version;
    }

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
     * Gets the date.
     * @return the date
     */
    public Date getDate() {

        return date;
    }

    /**
     * Sets the date.
     * @param date the new date
     */
    public void setDate(Date date) {

        this.date = date;
    }

    /**
     * Gets the version.
     * @return the version
     */
    public String getVersion() {

        return version;
    }

    /**
     * Sets the version.
     * @param version the new version
     */
    public void setVersion(String version) {

        this.version = version;
    }

    /**
     * Gets the module.
     * @return the module
     */
    public String getModule() {

        return module;
    }

    /**
     * Sets the module.
     * @param module the new module
     */
    public void setModule(String module) {

        this.module = module;
    }
}
