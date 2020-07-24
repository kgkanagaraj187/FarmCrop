/*
 * Locate.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.txn.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The Class Locate.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "txnTime", "serialNo", "longitude", "latitude","tenantId","accuracy","versNo","netStatus","gpsStatus" })
@XmlRootElement(name = "Locate")
public class Locate {

    @XmlElement(required = true)
    private String txnTime;
    @XmlElement(required = true)
    private String serialNo;
    @XmlElement(required = true)
    private String longitude;
    @XmlElement(required = true)
    private String latitude;
    @XmlElement(required = true)
    private String tenantId;
    @XmlElement(required = true)
    private String accuracy;
    @XmlElement(required = true)
    private String versNo;
    @XmlElement(required = true)
    private String netStatus;
    @XmlElement(required = true)
    private String gpsStatus;
    
    /**
     * Gets the txn time.
     * @return the txn time
     */
    public String getTxnTime() {

        return txnTime;
    }

    /**
     * Sets the txn time.
     * @param txnTime the new txn time
     */
    public void setTxnTime(String txnTime) {

        this.txnTime = txnTime;
    }

    /**
     * Gets the serial no.
     * @return the serial no
     */
    public String getSerialNo() {

        return serialNo;
    }

    /**
     * Sets the serial no.
     * @param serialNo the new serial no
     */
    public void setSerialNo(String serialNo) {

        this.serialNo = serialNo;
    }

    /**
     * Gets the longitude.
     * @return the longitude
     */
    public String getLongitude() {

        return longitude;
    }

    /**
     * Sets the longitude.
     * @param longitude the new longitude
     */
    public void setLongitude(String longitude) {

        this.longitude = longitude;
    }

    /**
     * Gets the latitude.
     * @return the latitude
     */
    public String getLatitude() {

        return latitude;
    }

    /**
     * Sets the latitude.
     * @param latitude the new latitude
     */
    public void setLatitude(String latitude) {

        this.latitude = latitude;
    }

    public String getTenantId() {
    
        return tenantId;
    }

    public void setTenantId(String tenantId) {
    
        this.tenantId = tenantId;
    }

	public String getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(String accuracy) {
		this.accuracy = accuracy;
	}

	public String getVersNo() {
		return versNo;
	}

	public void setVersNo(String versNo) {
		this.versNo = versNo;
	}

	public String getNetStatus() {
		return netStatus;
	}

	public void setNetStatus(String netStatus) {
		this.netStatus = netStatus;
	}

	public String getGpsStatus() {
		return gpsStatus;
	}

	public void setGpsStatus(String gpsStatus) {
		this.gpsStatus = gpsStatus;
	}
	
	
    
    

}
