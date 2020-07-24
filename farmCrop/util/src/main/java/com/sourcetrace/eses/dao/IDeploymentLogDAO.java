/*
 * IDeploymentLogDAO.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.List;

import com.sourcetrace.eses.util.log.DeploymentLog;
import com.sourcetrace.eses.util.log.UptimeLog;

public interface IDeploymentLogDAO extends IESEDAO {

    /**
     * Find deployment logs.
     * @param id the id
     * @return the deployment log
     */
    public DeploymentLog findDeploymentLogs(long id);

    /**
     * Find latest deployment log.
     * @param module the module
     * @return the deployment log
     */
    public DeploymentLog findLatestDeploymentLog(String module);

    /**
     * List deployment logs.
     * @param module the module
     * @return the list
     */
    public List<DeploymentLog> listDeploymentLogs(String module);

    /**
     * List deployment logs.
     * @return the list
     */
    public List<DeploymentLog> listDeploymentLogs();

	public void saveDeploymentLog(DeploymentLog deploymentLog);

	public void saveUptimeLog(UptimeLog uptimeLog);
}
