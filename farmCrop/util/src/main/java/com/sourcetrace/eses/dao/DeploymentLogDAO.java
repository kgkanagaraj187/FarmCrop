/*
 * DeploymentLogDAO.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sourcetrace.eses.util.log.DeploymentLog;
import com.sourcetrace.eses.util.log.UptimeLog;

@SuppressWarnings("unchecked")
@Repository
@Transactional
public class DeploymentLogDAO extends ESEDAO implements IDeploymentLogDAO {

    /**
     * Instantiates a new deployment log dao.
     * @param sessionFactory the session factory
     */
    @Autowired
    public DeploymentLogDAO(SessionFactory sessionFactory) {

        this.setSessionFactory(sessionFactory);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.dao.IDeploymentLogDAO#findDeploymentLogs(long)
     */
    @Override
    public DeploymentLog findDeploymentLogs(long id) {

        DeploymentLog deploymentLog = (DeploymentLog) find("FROM DeploymentLog dl WHERE dl.id = ?",
                id);
        return deploymentLog;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.dao.IDeploymentLogDAO#findLatestDeploymentLog(java.lang.String)
     */
    @Override
    public DeploymentLog findLatestDeploymentLog(String module) {

        DeploymentLog log = (DeploymentLog) find(
                "FROM DeploymentLog dl WHERE dl.id = (SELECT MAX(id) FROM DeploymentLog) AND dl.module = ?",
                module);
        return log;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.dao.IDeploymentLogDAO#listDeploymentLogs(java.lang.String)
     */
    @Override
    public List<DeploymentLog> listDeploymentLogs(String module) {

        List<DeploymentLog> logs = list("FROM DeploymentLog dl WHERE dl.module = ?", module);
        return logs;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.dao.IDeploymentLogDAO#listDeploymentLogs()
     */
    @Override
    public List<DeploymentLog> listDeploymentLogs() {

        List<DeploymentLog> logs = list("FROM DeploymentLog dl");
        return logs;
    }

	@Override
	public void saveDeploymentLog(DeploymentLog deploymentLog) {
		Session sessions = getSessionFactory().openSession();
		sessions.save(deploymentLog);
        sessions.flush();
        sessions.close();
		
	}

	@Override
	public void saveUptimeLog(UptimeLog uptimeLog) {
		Session sessions = getSessionFactory().openSession();
		sessions.save(uptimeLog);
        sessions.flush();
        sessions.close();
		
	}

}
