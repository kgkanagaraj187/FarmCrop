/*
 * IMailConfigurationDAO.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.List;

import com.sourcetrace.esesw.entity.profile.ReportMailConfiguration;

public interface IMailConfigurationDAO extends IESEDAO {

    /**
     * List report names.
     * @return the list< string>
     */
    public List<String> listReportNames();

    /**
     * Find report mail configuration by id.
     * @param id the id
     * @return the report mail configuration
     */
    public ReportMailConfiguration findReportMailConfigurationById(Long id);

    /**
     * Find unique mail id by name.
     * @param mailId the mail id
     * @return the report mail configuration
     */
    public ReportMailConfiguration findUniqueMailIdByName(String mailId);

    /**
     * List daily reports.
     * @return the list< string>
     */
    public List<String> listDailyReports();

    /**
     * List report mail configuration.
     * @return the list< report mail configuration>
     */
    public List<ReportMailConfiguration> listReportMailConfiguration();

    /**
     * List consolidated reports.
     * @return the list< string>
     */
    public List<String> listConsolidatedReports();

}
