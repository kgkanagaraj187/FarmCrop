/*
 * MailConfigurationDAO.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sourcetrace.esesw.entity.profile.ReportMailConfiguration;
@Repository
@Transactional
public class MailConfigurationDAO extends ESEDAO implements IMailConfigurationDAO {

	
	@Autowired
	public MailConfigurationDAO(SessionFactory sessionFactory) {
	    this.setSessionFactory(sessionFactory);
	}

    /*
     * (non-Javadoc)
     * @see com.ese.dao.profile.IMailConfigurationDAO#listReportNames()
     */
    @SuppressWarnings("unchecked")
    public List<String> listReportNames() {

        return list("SELECT m.label FROM Menu m WHERE m.label LIKE 'report.%' AND m.url !='javascript:void(0)' AND m.exportAvailability = TRUE ");
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ese.dao.profile.IMailConfigurationDAO#findReportMailConfigurationById(java.lang.Long)
     */
    public ReportMailConfiguration findReportMailConfigurationById(Long id) {

        return (ReportMailConfiguration) find("FROM ReportMailConfiguration rmc WHERE rmc.id=?", id);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.dao.profile.IMailConfigurationDAO#findUniqueMailIdByName(java.lang.String)
     */
    public ReportMailConfiguration findUniqueMailIdByName(String mailId) {

        return (ReportMailConfiguration) find(
                "FROM ReportMailConfiguration rmc WHERE rmc.mailId=?", mailId);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.dao.profile.IMailConfigurationDAO#listDailyReports()
     */
    @SuppressWarnings("unchecked")
    public List<String> listDailyReports() {

        Session session = getSessionFactory().openSession();
        String queryString = "SELECT drmc.`NAME` FROM report_mail_config AS rmc INNER JOIN daily_report_mail_config AS drmc ON rmc.ID = drmc.REPORT_MAIL_CONFIG_ID WHERE rmc.`STATUS` = 1 GROUP BY drmc.`NAME`";
        Query query = session.createSQLQuery(queryString);
        List<String> list = query.list();
        session.flush();
        session.close();
        return list;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.dao.profile.IMailConfigurationDAO#listReportMailConfiguration()
     */
    @SuppressWarnings("unchecked")
    public List<ReportMailConfiguration> listReportMailConfiguration() {

        return list(
                "FROM ReportMailConfiguration rmc WHERE rmc.status = ? AND (rmc.dailyReport.size > 0 OR rmc.consolidatedReport.size > 0 )",
                ReportMailConfiguration.ACTIVE);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.dao.profile.IMailConfigurationDAO#listConsolidatedReports()
     */
    public List<String> listConsolidatedReports() {

        Session session = getSessionFactory().openSession();
        String queryString = "SELECT crmc.`NAME` FROM report_mail_config AS rmc INNER JOIN consolidate_report_mail_config AS crmc ON rmc.ID = crmc.REPORT_MAIL_CONFIG_ID WHERE rmc.`STATUS` = 1 GROUP BY crmc.`NAME`";
        Query query = session.createSQLQuery(queryString);
        List<String> list = query.list();
        session.flush();
        session.close();
        return list;
    }

}
