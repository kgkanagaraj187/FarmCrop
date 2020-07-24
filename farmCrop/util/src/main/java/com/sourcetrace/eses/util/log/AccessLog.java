/*
 * AccessLog.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.util.log;

import java.util.Date;

public class AccessLog {
    private Long id;
    private Date login;
    private Date logout;
    private String module;
    private String user;
    private String userIPAddress;
    private int status;

    /**
     * Instantiates a new access log.
     */
    public AccessLog() {

    }

    /**
     * Instantiates a new access log.
     * @param module the module
     * @param user the user
     */
    public AccessLog(String module, String user) {

        this.login = new Date();
        this.module = module;
        this.user = user;
    }

    /**
     * Instantiates a new access log.
     * @param module the module
     * @param user the user
     * @param userIPAddress the user ip address
     */
    public AccessLog(String module, String user, String userIPAddress) {

        this.login = new Date();
        this.module = module;
        this.user = user;
        this.userIPAddress = userIPAddress;
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
     * Gets the login.
     * @return the login
     */
    public Date getLogin() {

        return login;
    }

    /**
     * Sets the login.
     * @param login the new login
     */
    public void setLogin(Date login) {

        this.login = login;
    }

    /**
     * Gets the logout.
     * @return the logout
     */
    public Date getLogout() {

        return logout;
    }

    /**
     * Sets the logout.
     * @param logout the new logout
     */
    public void setLogout(Date logout) {

        this.logout = logout;
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

    /**
     * Gets the user.
     * @return the user
     */
    public String getUser() {

        return user;
    }

    /**
     * Sets the user.
     * @param user the new user
     */
    public void setUser(String user) {

        this.user = user;
    }

    /**
     * Gets the user ip address.
     * @return the user ip address
     */
    public String getUserIPAddress() {

        return userIPAddress;
    }

    /**
     * Sets the user ip address.
     * @param userIPAddress the new user ip address
     */
    public void setUserIPAddress(String userIPAddress) {

        this.userIPAddress = userIPAddress;
    }

    /**
     * Gets the status.
     * @return the status
     */
    public int getStatus() {

        return status;
    }

    /**
     * Sets the status.
     * @param status the new status
     */
    public void setStatus(int status) {

        this.status = status;
    }
}
