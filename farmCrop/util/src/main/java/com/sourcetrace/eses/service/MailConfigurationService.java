/*
 * MailConfigurationService.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sourcetrace.eses.dao.IMailConfigurationDAO;
import com.sourcetrace.esesw.entity.profile.ReportMailConfiguration;

@Service
@Transactional
public class MailConfigurationService implements IMailConfigurationService {

	@Autowired	
    private IMailConfigurationDAO mailConfigurationDAO;

  

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IMailConfigurationService#listReportNames()
     */
    public List<String> listReportNames() {

        return mailConfigurationDAO.listReportNames();
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ese.service.profile.IMailConfigurationService#addReportMailConfiguration(com.ese.entity
     * .profile.ReportMailConfiguration)
     */
    public void addReportMailConfiguration(ReportMailConfiguration reportMailConfiguration) {

        mailConfigurationDAO.save(reportMailConfiguration);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ese.service.profile.IMailConfigurationService#findReportMailConfigurationById(java.lang
     * .Long)
     */
    public ReportMailConfiguration findReportMailConfigurationById(Long id) {

        return mailConfigurationDAO.findReportMailConfigurationById(id);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ese.service.profile.IMailConfigurationService#editReportMailConfiguration(com.ese.entity
     * .profile.ReportMailConfiguration)
     */
    public void editReportMailConfiguration(ReportMailConfiguration reportMailConfiguration) {

        mailConfigurationDAO.update(reportMailConfiguration);

    }

    /*
     * (non-Javadoc)
     * @see
     * com.ese.service.profile.IMailConfigurationService#removeReportMailConfiguration(com.ese.entity
     * .profile.ReportMailConfiguration)
     */
    public void removeReportMailConfiguration(ReportMailConfiguration reportMailConfiguration) {

        mailConfigurationDAO.delete(reportMailConfiguration);

    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IMailConfigurationService#listDailyReports()
     */
    public List<String> listDailyReports() {

        return mailConfigurationDAO.listDailyReports();
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IMailConfigurationService#listReportMailConfiguration()
     */
    public List<ReportMailConfiguration> listReportMailConfiguration() {

        return mailConfigurationDAO.listReportMailConfiguration();
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IMailConfigurationService#listConsolidatedReports()
     */
    public List<String> listConsolidatedReports() {

        return mailConfigurationDAO.listConsolidatedReports();
    }

}
