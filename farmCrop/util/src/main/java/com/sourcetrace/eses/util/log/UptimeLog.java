/*
 * UptimeLog.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.util.log;

import java.util.Date;

public class UptimeLog {
    private Long id;
    private Date startup;
    private Date shutdown;
    private String module;

    /**
     * Instantiates a new uptime log.
     */
    public UptimeLog() {

    }

    /**
     * Instantiates a new uptime log.
     * @param module the module
     */
    public UptimeLog(String module) {

        this.module = module;
        this.startup = new Date();
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
     * Gets the startup.
     * @return the startup
     */
    public Date getStartup() {

        return startup;
    }

    /**
     * Sets the startup.
     * @param startup the new startup
     */
    public void setStartup(Date startup) {

        this.startup = startup;
    }

    /**
     * Gets the shutdown.
     * @return the shutdown
     */
    public Date getShutdown() {

        return shutdown;
    }

    /**
     * Sets the shutdown.
     * @param shutdown the new shutdown
     */
    public void setShutdown(Date shutdown) {

        this.shutdown = shutdown;
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
