/*
 * IMailConfigurationService.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import java.util.List;

import com.sourcetrace.esesw.entity.profile.ReportMailConfiguration;

public interface IMailConfigurationService {

    /**
     * List report names.
     * @return the list< string>
     */
    public List<String> listReportNames();

    /**
     * Adds the report mail configuration.
     * @param reportMailConfiguration the report mail configuration
     */
    public void addReportMailConfiguration(ReportMailConfiguration reportMailConfiguration);

    /**
     * Find report mail configuration by id.
     * @param id the id
     * @return the report mail configuration
     */
    public ReportMailConfiguration findReportMailConfigurationById(Long id);

    /**
     * Edits the report mail configuration.
     * @param reportMailConfiguration the report mail configuration
     */
    public void editReportMailConfiguration(ReportMailConfiguration reportMailConfiguration);

    /**
     * Removes the report mail configuration.
     * @param reportMailConfiguration the report mail configuration
     */
    public void removeReportMailConfiguration(ReportMailConfiguration reportMailConfiguration);

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
