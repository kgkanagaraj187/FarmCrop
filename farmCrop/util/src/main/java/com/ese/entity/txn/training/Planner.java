/*
 * Planner.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.entity.txn.training;

public class Planner {

    private long id;
    private int year;
    private int month;
    private int week;
    private FarmerTraining farmerTraining;

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
     * Gets the year.
     * @return the year
     */
    public int getYear() {

        return year;
    }

    /**
     * Sets the year.
     * @param year the new year
     */
    public void setYear(int year) {

        this.year = year;
    }

    /**
     * Gets the month.
     * @return the month
     */
    public int getMonth() {

        return month;
    }

    /**
     * Sets the month.
     * @param month the new month
     */
    public void setMonth(int month) {

        this.month = month;
    }

    /**
     * Gets the week.
     * @return the week
     */
    public int getWeek() {

        return week;
    }

    /**
     * Sets the week.
     * @param week the new week
     */
    public void setWeek(int week) {

        this.week = week;
    }

    /**
     * Gets the farmer training.
     * @return the farmer training
     */
    public FarmerTraining getFarmerTraining() {

        return farmerTraining;
    }

    /**
     * Sets the farmer training.
     * @param farmerTraining the new farmer training
     */
    public void setFarmerTraining(FarmerTraining farmerTraining) {

        this.farmerTraining = farmerTraining;
    }

}
