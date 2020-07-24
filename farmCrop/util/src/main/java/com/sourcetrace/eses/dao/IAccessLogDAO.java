/*
 * IAccessLogDAO.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.List;

import com.sourcetrace.eses.util.log.AccessLog;

public interface IAccessLogDAO extends IESEDAO {

    /**
     * Find latest access log.
     * @param module the module
     * @param user the user
     * @return the access log
     */
    public AccessLog findLatestAccessLog(String module, String user);

    /**
     * List access logs.
     * @return the list
     */
    public List<AccessLog> listAccessLogs();

    /**
     * List access logs for module.
     * @param module the module
     * @return the list
     */
    public List<AccessLog> listAccessLogsForModule(String module);

    /**
     * List access logs for user.
     * @param userId the user id
     * @return the list
     */
    public List<AccessLog> listAccessLogsForUser(String userId);

	public void updateAccessLog(AccessLog accessLog);
}
