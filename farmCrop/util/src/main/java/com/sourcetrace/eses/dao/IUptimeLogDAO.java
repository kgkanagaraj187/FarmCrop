/*
 * IUptimeLogDAO.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.List;

import com.sourcetrace.eses.util.log.UptimeLog;

public interface IUptimeLogDAO extends IESEDAO {

    /**
     * Find latest uptime log.
     * @param module the module
     * @return the uptime log
     */
    public UptimeLog findLatestUptimeLog(String module);

    /**
     * List uptime logs for module.
     * @param module the module
     * @return the list
     */
    public List<UptimeLog> listUptimeLogsForModule(String module);

    /**
     * List uptime logs.
     * @return the list
     */
    public List<UptimeLog> listUptimeLogs();

	public void updateUptimeLog(UptimeLog uptimeLog);
}
