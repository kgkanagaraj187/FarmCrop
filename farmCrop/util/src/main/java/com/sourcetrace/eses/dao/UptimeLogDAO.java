/*
 * UptimeLogDAO.java
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

import com.sourcetrace.eses.util.log.UptimeLog;

@SuppressWarnings("unchecked")
@Repository
@Transactional
public class UptimeLogDAO extends ESEDAO implements IUptimeLogDAO {

    /**
     * Instantiates a new uptime log dao.
     * @param sessionFactory the session factory
     */
    @Autowired
    public UptimeLogDAO(SessionFactory sessionFactory) {

        this.setSessionFactory(sessionFactory);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.dao.IUptimeLogDAO#findLatestUptimeLog(java.lang.String)
     */
    @Override
    public UptimeLog findLatestUptimeLog(String module) {

        UptimeLog log = (UptimeLog) find(
                "FROM UptimeLog ul WHERE ul.id = (SELECT MAX(id) FROM UptimeLog) AND ul.module = ?",
                module);
        return log;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.dao.IUptimeLogDAO#listUptimeLogsForModule(java.lang.String)
     */
    @Override
    public List<UptimeLog> listUptimeLogsForModule(String module) {

        List<UptimeLog> logs = (List<UptimeLog>) list("FROM UptimeLog ul WHERE ul.module = ?",
                module);
        return logs;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.dao.IUptimeLogDAO#listUptimeLogs()
     */
    @Override
    public List<UptimeLog> listUptimeLogs() {

        List<UptimeLog> logs = (List<UptimeLog>) list("FROM UptimeLog ul");
        return logs;
    }

	@Override
	public void updateUptimeLog(UptimeLog uptimeLog) {
		Session sessions = getSessionFactory().openSession();
		sessions.update(uptimeLog);
        sessions.flush();
        sessions.close();
		
	}

}
