/*
 * TripSheet.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.sourcetrace.esesw.entity.profile.Municipality;

public class TripSheet implements Serializable {

    public static enum TRANSIT_STATUS {
        NONE, MTNT, MTNR
    }

    private long id;
    private Municipality city;
    private String chartNo;
    private Date date;
    private String driverName;
    private String vehicleNo;
    private String buyerName;
    private int transitStatus;
    private Set<Procurement> procurements;

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
     * Gets the city.
     * @return the city
     */
    public Municipality getCity() {

        return city;
    }

    /**
     * Sets the city.
     * @param city the new city
     */
    public void setCity(Municipality city) {

        this.city = city;
    }

    /**
     * Gets the chart no.
     * @return the chart no
     */
    public String getChartNo() {

        return chartNo;
    }

    /**
     * Sets the chart no.
     * @param chartNo the new chart no
     */
    public void setChartNo(String chartNo) {

        this.chartNo = chartNo;
    }

    /**
     * Gets the date.
     * @return the date
     */
    public Date getDate() {

        return date;
    }

    /**
     * Sets the date.
     * @param date the new date
     */
    public void setDate(Date date) {

        this.date = date;
    }

    /**
     * Gets the driver name.
     * @return the driver name
     */
    public String getDriverName() {

        return driverName;
    }

    /**
     * Sets the driver name.
     * @param driverName the new driver name
     */
    public void setDriverName(String driverName) {

        this.driverName = driverName;
    }

    /**
     * Gets the vehicle no.
     * @return the vehicle no
     */
    public String getVehicleNo() {

        return vehicleNo;
    }

    /**
     * Sets the vehicle no.
     * @param vehicleNo the new vehicle no
     */
    public void setVehicleNo(String vehicleNo) {

        this.vehicleNo = vehicleNo;
    }

    /**
     * Gets the buyer name.
     * @return the buyer name
     */
    public String getBuyerName() {

        return buyerName;
    }

    /**
     * Sets the buyer name.
     * @param buyerName the new buyer name
     */
    public void setBuyerName(String buyerName) {

        this.buyerName = buyerName;
    }

    /**
     * Gets the transit status.
     * @return the transit status
     */
    public int getTransitStatus() {

        return transitStatus;
    }

    /**
     * Sets the transit status.
     * @param transitStatus the new transit status
     */
    public void setTransitStatus(int transitStatus) {

        this.transitStatus = transitStatus;
    }

    /**
     * Gets the procurements.
     * @return the procurements
     */
    public Set<Procurement> getProcurements() {

        return procurements;
    }

    /**
     * Sets the procurements.
     * @param procurements the new procurements
     */
    public void setProcurements(Set<Procurement> procurements) {

        this.procurements = procurements;
    }

}
