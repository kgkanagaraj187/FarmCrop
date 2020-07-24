/*
 * AccessLogDAO.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sourcetrace.eses.util.log.AccessLog;

@SuppressWarnings("unchecked")
@Repository
public class AccessLogDAO extends ESEDAO implements IAccessLogDAO {

    /**
     * Instantiates a new access log dao.
     * @param sessionFactory the session factory
     */
    @Autowired
    public AccessLogDAO(SessionFactory sessionFactory) {

        this.setSessionFactory(sessionFactory);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.dao.IAccessLogDAO#findLatestAccessLog(java.lang.String,
     * java.lang.String)
     */
    @Override
    public AccessLog findLatestAccessLog(String module, String user) {

        Object[] values = new Object[] { module, user };
        AccessLog log = (AccessLog) find(
                "FROM AccessLog al WHERE al.id = (SELECT MAX(id) FROM AccessLog) AND al.module = ? AND al.user = ?",
                values);
        return log;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.dao.IAccessLogDAO#listAccessLogs()
     */
    @Override
    public List<AccessLog> listAccessLogs() {

        List<AccessLog> logs = (List<AccessLog>) list("FROM AccessLog al");
        return logs;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.dao.IAccessLogDAO#listAccessLogsForModule(java.lang.String)
     */
    @Override
    public List<AccessLog> listAccessLogsForModule(String module) {

        List<AccessLog> logs = (List<AccessLog>) list("FROM AccessLog al WHERE al.module = ?",
                module);
        return logs;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.dao.IAccessLogDAO#listAccessLogsForUser(java.lang.String)
     */
    @Override
    public List<AccessLog> listAccessLogsForUser(String userId) {

        List<AccessLog> logs = (List<AccessLog>) list("FROM AccessLog al WHERE al.user = ?", userId);
        return logs;
    }

	@Override
	public void updateAccessLog(AccessLog accessLog) {
		Session sessions = getSessionFactory().openSession();
		sessions.update(accessLog);
        sessions.flush();
        sessions.close();
		
	}

}
