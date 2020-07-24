/*
 * POS.java
 * Copyright (c) 2008, Source Trace Systems
 * ALL RIGHTS RESERVED
 */
package com.sourcetrace.esesw.entity.profile;

import com.sourcetrace.eses.entity.Device;

// TODO: Auto-generated Javadoc
/**
 * The Class POS.
 * 
 * @author $Author: aravind $
 * @version $Rev: 59 $, $Date: 2009-08-04 17:10:51 +0530 (Tue, 04 Aug 2009) $
 */
public class POS extends Device {

    public static final int ONLINE = 1;
    public static final int OFFLINE = 2;
    public static final int BOTH = 3;

    private int mode;
    private long receiptNumber;
    private long messageNumber;
    private long authCode;

    /**
     * Instantiates a new pOS.
     */
    public POS() {}

    /**
     * Instantiates a new pOS.
     * 
     * @param device the device
     */
    public POS(Device device) {
        this.setAgents(device.getAgents());
        this.setBranch(device.getBranch());
        this.setCreatedTime(device.getCreatedTime());
        this.setDeviceId(device.getDeviceId());
        this.setEnabled(device.isEnabled());
        this.setId(device.getId());
        this.setModifiedTime(device.getModifiedTime());
        this.setName(device.getName());
        this.setSerialNumber(device.getSerialNumber());
    }

    /**
     * Gets the receipt number.
     * 
     * @return the receipt number
     */
    public long getReceiptNumber() {
        return receiptNumber;
    }

    /**
     * Sets the receipt number.
     * 
     * @param receiptNumber the new receipt number
     */
    public void setReceiptNumber(long receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    /**
     * Gets the mode.
     * 
     * @return the mode
     */
    public int getMode() {
        return mode;
    }

    /**
     * Sets the mode.
     * 
     * @param mode the new mode
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    /**
     * Gets the message number.
     * 
     * @return the message number
     */
    public long getMessageNumber() {
        return messageNumber;
    }

    /**
     * Sets the message number.
     * 
     * @param messageNumber the new message number
     */
    public void setMessageNumber(long messageNumber) {
        this.messageNumber = messageNumber;
    }

    /**
     * Gets the auth code.
     * 
     * @return the auth code
     */
    public long getAuthCode() {
		return authCode;
	}

	/**
	 * Sets the auth code.
	 * 
	 * @param authCode the new auth code
	 */
	public void setAuthCode(long authCode) {
		this.authCode = authCode;
	}

	/* (non-Javadoc)
     * @see com.ese.entity.profile.Device#toString()
     */
    public String toString() {
    	 return getName()+"-"+getDeviceId();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object object) {
    	boolean equal = false;

    	if(object instanceof POS) {
    		POS other = (POS) object;
    		equal = other.getDeviceId().equals(getDeviceId());
    	}

    	return equal;
    }

    /**
     * To agent name.
     * 
     * @return the string
     */
    public String toAgentName() {
        return getName();
    }
}

