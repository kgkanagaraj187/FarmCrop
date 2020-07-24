/*
 * ReportMailConfigurationAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view.general;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sourcetrace.eses.service.IMailConfigurationService;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.ReportMailConfiguration;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

public class ReportMailConfigurationAction extends SwitchValidatorAction {

    private static final long serialVersionUID = 1L;

    private String id;
    private String dailyReportDetail;
    private String consolidatedReportDetail;
    private List<String> selectedDailyReportName = new ArrayList<String>();
    private List<String> selectedConsolidatedReportName = new ArrayList<String>();
    private Map<String, String> availableDailyReports = new HashMap<String, String>();
    private Map<String, String> selectedDailyReports = new HashMap<String, String>();
    private Map<String, String> availableconsolidatedReports = new HashMap<String, String>();
    private Map<String, String> selectedconsolidatedReports = new HashMap<String, String>();
    private Map<Integer, String> reportMailConfigurationStatus = new LinkedHashMap<Integer, String>();

    private ReportMailConfiguration reportMailConfiguration;
    private IMailConfigurationService mailConfigurationService;
    List<String> menuList=new ArrayList<>();

    /**
     * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
     */
    @Override
    public Object getData() {

        reportMailConfigurationStatus.put(ReportMailConfiguration.ACTIVE, getText("status1"));
        reportMailConfigurationStatus.put(ReportMailConfiguration.INACTIVE, getText("status0"));

        if (!ObjectUtil.isEmpty(reportMailConfiguration)) {
            for (String val : selectedDailyReportName)
                selectedDailyReports.put(val, getText(val));
            Set<String> dailyVal = new HashSet<>(selectedDailyReports.values());
            reportMailConfiguration.setDailyReport(dailyVal);
               

            for (String val : selectedConsolidatedReportName)
                selectedconsolidatedReports.put(val, getText(val));
            Set<String> conVal = new HashSet<>(selectedconsolidatedReports.values());
            reportMailConfiguration.setConsolidatedReport(conVal);
        }

        return reportMailConfiguration;
    }

    /**
     * @see com.sourcetrace.esesw.view.SwitchAction#data()
     */
    @SuppressWarnings("unchecked")
    public String data() throws Exception {

        Map<String, String> searchRecord = getJQGridRequestParam();
        ReportMailConfiguration filter = new ReportMailConfiguration();

        if (!StringUtil.isEmpty(searchRecord.get("name")))
            filter.setName(searchRecord.get("name").trim());

        if (!StringUtil.isEmpty(searchRecord.get("mailId")))
            filter.setMailId(searchRecord.get("mailId").trim());

        if (!StringUtil.isEmpty(searchRecord.get("status"))) {
            if (ReportMailConfiguration.ACTIVE == Integer.valueOf(searchRecord.get("status")))
                filter.setStatus(ReportMailConfiguration.ACTIVE);
            else
                filter.setStatus(ReportMailConfiguration.INACTIVE);
        } else
            filter.setStatus(-1);

        Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(),
                getResults(), filter, getPage());

        return sendJQGridJSONResponse(data);
    }

    /**
     * @see com.sourcetrace.esesw.view.SwitchAction#toJSON(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJSON(Object obj) {

        ReportMailConfiguration reportMailConfiguration = (ReportMailConfiguration) obj;
        JSONObject jsonObject = new JSONObject();
        JSONArray rows = new JSONArray();
        rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">"
                + reportMailConfiguration.getName() + "</font>");
        rows.add(reportMailConfiguration.getMailId());
        rows.add(getText("status" + reportMailConfiguration.getStatus()));
        jsonObject.put("id", reportMailConfiguration.getId());
        jsonObject.put("cell", rows);
        return jsonObject;
    }

    /**
     * Creates the.
     * @return the string
     * @throws Exception the exception
     */
    public String create() throws Exception {

        if (reportMailConfiguration == null) {
            command = CREATE;
            request.setAttribute(HEADING, getText(CREATE));
            return INPUT;
        } else {
            if (!StringUtil.isListEmpty(selectedDailyReportName)) {
                Set<String> dailyReport = new HashSet<String>(selectedDailyReportName);
                reportMailConfiguration.setDailyReport(dailyReport);
            }
            if (!StringUtil.isListEmpty(selectedConsolidatedReportName)) {
                Set<String> consolidatedReport = new HashSet<String>(selectedConsolidatedReportName);
                reportMailConfiguration.setConsolidatedReport(consolidatedReport);
            }
            mailConfigurationService.addReportMailConfiguration(reportMailConfiguration);
            return REDIRECT;
        }

    }

    /**
     * Detail.
     * @return the string
     * @throws Exception the exception
     */
    public String detail() throws Exception {

        String view = "";
        if (id != null && !id.equals("")) {
            reportMailConfiguration = mailConfigurationService.findReportMailConfigurationById(Long
                    .valueOf(id));
            if (!ObjectUtil.isEmpty(reportMailConfiguration)) {
                if (!StringUtil.isListEmpty(reportMailConfiguration.getDailyReport())) {
                    dailyReportDetail = "";
                    for (String temp : reportMailConfiguration.getDailyReport()) {
                        dailyReportDetail = dailyReportDetail + getText(temp) + ", ";
                    }
                    dailyReportDetail = dailyReportDetail.substring(0,
                            dailyReportDetail.length() - 2);
                }
                if (!StringUtil.isListEmpty(reportMailConfiguration.getConsolidatedReport())) {
                    consolidatedReportDetail = "";
                    for (String temp : reportMailConfiguration.getConsolidatedReport()) {
                        consolidatedReportDetail = consolidatedReportDetail + getText(temp) + ", ";
                    }
                    consolidatedReportDetail = consolidatedReportDetail.substring(0,
                            consolidatedReportDetail.length() - 2);
                }
            } else {
                addActionError(NO_RECORD);
                return REDIRECT;
            }
            command = UPDATE;
            view = DETAIL;
            request.setAttribute(HEADING, getText(DETAIL));
        } else {
            request.setAttribute(HEADING, getText(LIST));
            return LIST;
        }
        return view;
    }

    /**
     * Update.
     * @return the string
     * @throws Exception the exception
     */
    public String update() throws Exception {

        if (id != null && !id.equals("")) {
            reportMailConfiguration = mailConfigurationService.findReportMailConfigurationById(Long
                    .valueOf(id));
            if (!ObjectUtil.isEmpty(reportMailConfiguration)) {
                if (!StringUtil.isListEmpty(reportMailConfiguration.getDailyReport())) {
                    for (String val : reportMailConfiguration.getDailyReport()) {
                        selectedDailyReports.put(val, getText(val));
                    }
                }
                if (!StringUtil.isListEmpty(reportMailConfiguration.getConsolidatedReport())) {
                    for (String val : reportMailConfiguration.getConsolidatedReport()) {
                        selectedconsolidatedReports.put(val, getText(val));
                    }
                }
            } else {
                addActionError(NO_RECORD);
                return REDIRECT;
            }
            setCurrentPage(getCurrentPage());
            id = null;
            command = UPDATE;
            request.setAttribute(HEADING, getText(UPDATE));
        } else {

            if (!ObjectUtil.isEmpty(reportMailConfiguration)) {
                ReportMailConfiguration temp = mailConfigurationService
                        .findReportMailConfigurationById(reportMailConfiguration.getId());
                Set<String> dailyReport = new HashSet<String>();
                Set<String> consolidatedReport = new HashSet<String>();
                
                if (!ObjectUtil.isEmpty(temp)) {
                    temp.setMailId(reportMailConfiguration.getMailId());
                    if (!StringUtil.isListEmpty(selectedDailyReportName)) 
                        dailyReport = new HashSet<String>(selectedDailyReportName);
                    temp.setDailyReport(dailyReport);
                    if (!StringUtil.isListEmpty(selectedConsolidatedReportName)) 
                        consolidatedReport = new HashSet<String>(selectedConsolidatedReportName);
                    temp.setConsolidatedReport(consolidatedReport);
                    temp.setStatus(reportMailConfiguration.getStatus());
                    mailConfigurationService.editReportMailConfiguration(temp);
                } else {
                    addActionError(NO_RECORD);
                    return REDIRECT;
                }
            }
            request.setAttribute(HEADING, getText(LIST));
            return LIST;
        }
        return super.execute();
    }

    /**
     * Delete.
     * @return the string
     * @throws Exception the exception
     */
    public String delete() throws Exception {

        if (id != null && !id.equals("")) {
            reportMailConfiguration = mailConfigurationService.findReportMailConfigurationById(Long
                    .valueOf(id));
            if (!ObjectUtil.isEmpty(reportMailConfiguration)) {
                mailConfigurationService.removeReportMailConfiguration(reportMailConfiguration);
            } else {
                addActionError(NO_RECORD);
                return REDIRECT;
            }
        }
        request.setAttribute(HEADING, getText(LIST));
        return LIST;
    }

    /**
     * Sets the available daily reports.
     * @param availableDailyReports the available daily reports
     */
    public void setAvailableDailyReports(Map<String, String> availableDailyReports) {

        this.availableDailyReports = availableDailyReports;
    }

    /**
     * Gets the available daily reports.
     * @return the available daily reports
     */
    public Map<String, String> getAvailableDailyReports() {
    	
    	

        menuList = mailConfigurationService.listReportNames();
        if (!StringUtil.isListEmpty(menuList)) {
            for (String menuVal : menuList) {
                availableDailyReports.put(menuVal, getText(menuVal));
            }
            if (!selectedDailyReports.isEmpty()) {
                for (Object key : selectedDailyReports.keySet()) {
                    if (availableDailyReports.containsKey(key)) {
                        availableDailyReports.remove(key);
                    }
                }
            }
        }
        return availableDailyReports;
    }

    /**
     * Sets the availableconsolidated reports.
     * @param availableconsolidatedReports the availableconsolidated reports
     */
    public void setAvailableconsolidatedReports(Map<String, String> availableconsolidatedReports) {

        this.availableconsolidatedReports = availableconsolidatedReports;
    }

    /**
     * Gets the availableconsolidated reports.
     * @return the availableconsolidated reports
     */
    public Map<String, String> getAvailableconsolidatedReports() {

        List<String> menuList = mailConfigurationService.listReportNames();
        if (!StringUtil.isListEmpty(menuList)) {
            for (String menuVal : menuList) {
                availableconsolidatedReports.put(menuVal, getText(menuVal));
            }
            if (!selectedconsolidatedReports.isEmpty()) {
                for (Object key : selectedconsolidatedReports.keySet()) {
                    if (availableconsolidatedReports.containsKey(key)) {
                        availableconsolidatedReports.remove(key);
                    }
                }
            }
        }
        return availableconsolidatedReports;
    }

    /**
     * Gets the report mail configuration.
     * @return the report mail configuration
     */
    public ReportMailConfiguration getReportMailConfiguration() {

        return reportMailConfiguration;
    }

    /**
     * Sets the report mail configuration.
     * @param reportMailConfiguration the new report mail configuration
     */
    public void setReportMailConfiguration(ReportMailConfiguration reportMailConfiguration) {

        this.reportMailConfiguration = reportMailConfiguration;
    }

    /**
     * Gets the report mail configuration status.
     * @return the report mail configuration status
     */
    public Map<Integer, String> getReportMailConfigurationStatus() {

        return reportMailConfigurationStatus;
    }

    /**
     * Sets the report mail configuration status.
     * @param reportMailConfigurationStatus the report mail configuration status
     */
    public void setReportMailConfigurationStatus(Map<Integer, String> reportMailConfigurationStatus) {

        this.reportMailConfigurationStatus = reportMailConfigurationStatus;
    }

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
     * Sets the selected daily report name.
     * @param selectedDailyReportName the new selected daily report name
     */
    public void setSelectedDailyReportName(List<String> selectedDailyReportName) {

        this.selectedDailyReportName = selectedDailyReportName;
    }

    /**
     * Gets the selected daily report name.
     * @return the selected daily report name
     */
    public List<String> getSelectedDailyReportName() {

        return selectedDailyReportName;
    }

    /**
     * Sets the selected consolidated report name.
     * @param selectedConsolidatedReportName the new selected consolidated report name
     */
    public void setSelectedConsolidatedReportName(List<String> selectedConsolidatedReportName) {

        this.selectedConsolidatedReportName = selectedConsolidatedReportName;
    }

    /**
     * Gets the selected consolidated report name.
     * @return the selected consolidated report name
     */
    public List<String> getSelectedConsolidatedReportName() {

        return selectedConsolidatedReportName;
    }

    /**
     * Sets the selected daily reports.
     * @param selectedDailyReports the selected daily reports
     */
    public void setSelectedDailyReports(Map<String, String> selectedDailyReports) {

        this.selectedDailyReports = selectedDailyReports;
    }

    /**
     * Gets the selected daily reports.
     * @return the selected daily reports
     */
    public Map<String, String> getSelectedDailyReports() {

        return selectedDailyReports;
    }

    /**
     * Sets the selectedconsolidated reports.
     * @param selectedconsolidatedReports the selectedconsolidated reports
     */
    public void setSelectedconsolidatedReports(Map<String, String> selectedconsolidatedReports) {

        this.selectedconsolidatedReports = selectedconsolidatedReports;
    }

    /**
     * Gets the selectedconsolidated reports.
     * @return the selectedconsolidated reports
     */
    public Map<String, String> getSelectedconsolidatedReports() {

        return selectedconsolidatedReports;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(String id) {

        this.id = id;
    }

    /**
     * Gets the id.
     * @return the id
     */
    public String getId() {

        return id;
    }

    /**
     * Sets the daily report detail.
     * @param dailyReportDetail the new daily report detail
     */
    public void setDailyReportDetail(String dailyReportDetail) {

        this.dailyReportDetail = dailyReportDetail;
    }

    /**
     * Gets the daily report detail.
     * @return the daily report detail
     */
    public String getDailyReportDetail() {

        return dailyReportDetail;
    }

    /**
     * Gets the consolidated report detail.
     * @return the consolidated report detail
     */
    public String getConsolidatedReportDetail() {

        return consolidatedReportDetail;
    }

    /**
     * Sets the consolidated report detail.
     * @param consolidatedReportDetail the new consolidated report detail
     */
    public void setConsolidatedReportDetail(String consolidatedReportDetail) {

        this.consolidatedReportDetail = consolidatedReportDetail;
    }

}
