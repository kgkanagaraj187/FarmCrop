/*
 * MailConfigurationSchedulerTask.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.ws.task;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.sourcetrace.eses.service.IMailConfigurationService;
import com.sourcetrace.eses.service.IMailService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.esesw.view.IExporter;

public class MailConfigurationSchedulerTask {

    private static final long serialVersionUID = 1L;
    private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private IMailConfigurationService mailConfigurationService;
    @Autowired
    private IMailService mailService;
    @Autowired
    private IPreferencesService preferncesService;
    private Properties errors;
    @Resource
    private Map<String, IExporter> reportConfigMap;
    private String exportType;
    

    /**
     * Execute check.
     * @throws Exception the exception
     */
    public void process() throws Exception {/*

        Map<String, byte[]> commonExportMap = new HashMap<String, byte[]>();
        try {
            IExporter exporter = null;
            List<String> reportList = new ArrayList<String>();
            if(IExporter.EXPORT_DAILY.equalsIgnoreCase(exportType))
                reportList = mailConfigurationService.listDailyReports();
            else
                reportList = mailConfigurationService.listConsolidatedReports();
            for (String report : reportList) {//To Fetch Configured Report
                if (reportConfigMap.containsKey(report)) {
                    exporter = (IExporter) reportConfigMap.get(report);
                    InputStream is = exporter.getExportDataStream(exportType);
                    if(!ObjectUtil.isEmpty(is)) {
                        commonExportMap.put(report, IOUtils.toByteArray(is));
                    }
                        
                }
            }
            
            if(!ObjectUtil.isEmpty(commonExportMap) && commonExportMap.size() > 0){
            for (ReportMailConfiguration reportMail : mailConfigurationService.listReportMailConfiguration()) {//Report Mail Configuration
                Map<String, byte[]> attachment = new HashMap<String, byte[]>();
                Set<String> reports = reportMail.getDailyReport();
                if(IExporter.EXPORT_DAILY.equalsIgnoreCase(exportType))
                    reports = reportMail.getDailyReport();
                else
                    reports =  reportMail.getConsolidatedReport();
                for (String reportNames : reports) {//Attaching Reports
                    if (commonExportMap.containsKey(reportNames)) {
                        attachment.put(getLocaleProperty(reportNames)+ fileNameDateFormat.format(new Date()) + ".xls", commonExportMap.get(reportNames));
                    }
                }
                String[] to = new String[] { reportMail.getMailId() };//Mail Configuration 
                List<ESESystem> prefList = preferncesService.listPrefernces();
                Map<String, String> mailConfigMap = new HashMap<String, String>();
                mailConfigMap.put(ESESystem.USER_NAME, prefList.get(0).getPreferences().get(
                        ESESystem.USER_NAME));
                mailConfigMap.put(ESESystem.PASSWORD, prefList.get(0).getPreferences().get(
                        ESESystem.PASSWORD));
                mailConfigMap.put(ESESystem.MAIL_HOST, prefList.get(0).getPreferences().get(
                        ESESystem.MAIL_HOST));
                mailConfigMap.put(ESESystem.PORT, prefList.get(0).getPreferences().get(
                        ESESystem.PORT));
                StringBuffer content = new StringBuffer();//Contents 
                content.append(getLocaleProperty("hi")+ " "  + reportMail.getName() + ",\n" + getLocaleProperty("content")).append("\n").append("\n");
                int temp = 1;
                for (String report : reports) {
                    content.append(temp + "." + getLocaleProperty(report)).append("\n");
                    temp++;
                }
                content.append("\n\n" + getLocaleProperty("note"));
                //Sending mail
                String subject = IExporter.EXPORT_CONSOLIDATED.equalsIgnoreCase(exportType) ? getLocaleProperty("subjectCon")+ " " +DateUtil.minusWeek(DateUtil.getDateInFormat(DateUtil.DATABASE_DATE_FORMAT), 1, DateUtil.DATABASE_DATE_FORMAT) +" to "+DateUtil.getDateInFormat(DateUtil.DATABASE_DATE_FORMAT): getLocaleProperty("subject")+" " +DateUtil.getDateInFormat(DateUtil.PROFILE_DATE_FORMAT); 
                mailService.sendAttachmentMail(prefList.get(0).getPreferences().get(
                        ESESystem.USER_NAME), to, subject, content.toString(),
                        mailConfigMap, attachment);
            }
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    */}

    /**
     * Sets the mail configuration service.
     * @param mailConfigurationService the new mail configuration service
     */
    public void setMailConfigurationService(IMailConfigurationService mailConfigurationService) {

        this.mailConfigurationService = mailConfigurationService;
    }

    /**
     * Gets the mail configuration service.
     * @return the mail configuration service
     */
    public IMailConfigurationService getMailConfigurationService() {

        return mailConfigurationService;
    }

    /**
     * Gets the mail service.
     * @return the mail service
     */
    public IMailService getMailService() {

        return mailService;
    }

    /**
     * Sets the mail service.
     * @param mailService the new mail service
     */
    public void setMailService(IMailService mailService) {

        this.mailService = mailService;
    }

    /**
     * Gets the prefernces service.
     * @return the prefernces service
     */
    public IPreferencesService getPreferncesService() {

        return preferncesService;
    }

    /**
     * Sets the prefernces service.
     * @param preferncesService the new prefernces service
     */
    public void setPreferncesService(IPreferencesService preferncesService) {

        this.preferncesService = preferncesService;
    }

    /**
     * Gets the property.
     * @param key the key
     * @return the property
     */
    public String getLocaleProperty(String key) {

        if (ObjectUtil.isEmpty(errors)) {//Fetching Properties
            errors = new Properties();
            try {
                errors.load(MailConfigurationSchedulerTask.class.getResourceAsStream("MailConfigurationSchedulerTask.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return errors.getProperty(key);
    }

    /**
     * Gets the errors.
     * @return the errors
     */
    public Properties getErrors() {

        return errors;
    }

    /**
     * Sets the errors.
     * @param errors the new errors
     */
    public void setErrors(Properties errors) {

        this.errors = errors;
    }

    /**
     * Gets the report config map.
     * @return the report config map
     */
    public Map<String, IExporter> getReportConfigMap() {

        return reportConfigMap;
    }

    /**
     * Sets the report config map.
     * @param reportConfigMap the report config map
     */
    public void setReportConfigMap(Map<String, IExporter> reportConfigMap) {

        this.reportConfigMap = reportConfigMap;
    }

    /**
     * @param exportType the exportType to set
     */
    public void setExportType(String exportType) {

        this.exportType = exportType;
    }

    /**
     * @return the exportType
     */
    public String getExportType() {

        return exportType;
    }

}
