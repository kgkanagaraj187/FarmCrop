/*
 * PeriodicInspectionSymptom.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

public class PeriodicInspectionSymptom {

    private long id;
    private String symCode;
    private String type;
    private PeriodicInspection periodicInspection;

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
     * Gets the type.
     * @return the type
     */
    public String getType() {

        return type;
    }

    /**
     * Sets the type.
     * @param type the new type
     */
    public void setType(String type) {

        this.type = type;
    }

    /**
     * Gets the periodic inspection.
     * @return the periodic inspection
     */
    public PeriodicInspection getPeriodicInspection() {

        return periodicInspection;
    }

    /**
     * Sets the periodic inspection.
     * @param periodicInspection the new periodic inspection
     */
    public void setPeriodicInspection(PeriodicInspection periodicInspection) {

        this.periodicInspection = periodicInspection;
    }

    public String getSymCode() {
    
        return symCode;
    }

    public void setSymCode(String symCode) {
    
        this.symCode = symCode;
    }

    
    

}
