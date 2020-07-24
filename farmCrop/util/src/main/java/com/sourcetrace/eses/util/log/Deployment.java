/*
 * Deployment.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.util.log;

public class Deployment {
    private String module;
    private String version;

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

}
