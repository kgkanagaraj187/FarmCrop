/*
 * ReportMailConfiguration.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import java.util.Set;

public class ReportMailConfiguration {

    public static final int INACTIVE = 0;
    public static final int ACTIVE = 1;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private long id;
    private String name;
    private String mailId;
    private int status;
    private Set<String> dailyReport;
    private Set<String> consolidatedReport;

    /**
     * Gets the id.
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Gets the name.
     * @return the name
     */
  //  @Pattern(regexp = "[^\\p{Punct}]+$", message = "pattern.name")
 //   @NotEmpty(message = "empty.name")
    public String getName() {

        return name;
    }

    /**
     * Sets the name.
     * @param name the new name
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Gets the mail id.
     * @return the mail id
     */
   // @Pattern(regexp = EMAIL_PATTERN, message = "pattern.mailId")
   // @NotEmpty(message = "empty.email")
  //  @NotNull(message = "empty.email")
    public String getMailId() {

        return mailId;
    }

    /**
     * Sets the mail id.
     * @param mailId the new mail id
     */
    public void setMailId(String mailId) {

        this.mailId = mailId;
    }

    /**
     * Gets the status.
     * @return the status
     */
    public int getStatus() {

        return status;
    }

    /**
     * Sets the status.
     * @param status the new status
     */
    public void setStatus(int status) {

        this.status = status;
    }

    /**
     * Gets the daily report.
     * @return the daily report
     */
    public Set<String> getDailyReport() {

        return dailyReport;
    }

    /**
     * Sets the daily report.
     * @param dailyReport the new daily report
     */
    public void setDailyReport(Set<String> dailyReport) {

        this.dailyReport = dailyReport;
    }

    /**
     * Gets the consolidated report.
     * @return the consolidated report
     */
    public Set<String> getConsolidatedReport() {

        return consolidatedReport;
    }

    /**
     * Sets the consolidated report.
     * @param consolidatedReport the new consolidated report
     */
    public void setConsolidatedReport(Set<String> consolidatedReport) {

        this.consolidatedReport = consolidatedReport;
    }

}
