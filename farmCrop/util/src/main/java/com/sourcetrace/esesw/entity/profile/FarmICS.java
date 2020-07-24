package com.sourcetrace.esesw.entity.profile;

import java.util.Date;

// TODO: Auto-generated Javadoc
public class FarmICS {

    public enum ICSTypes {
        ICS_1, ICS_2, ICS_3, ORGANIC
    }
    
    public static final int IN_ACTIVE = 0;
    public static final int ACTIVE = 1;
    
    private Long id;
    private Farm farm;
    private Integer icsType;
    private String landIcsDetails;
    private Date beginOfConversion;
    private String surveyNo;
    private Integer status;

    //Transient Variable
    
    private String beginOfConversionString;
    /**
     * Gets the id.
     * @return the id
     */
    public Long getId() {

        return id;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(Long id) {

        this.id = id;
    }

    /**
     * Gets the farm.
     * @return the farm
     */
    public Farm getFarm() {

        return farm;
    }

    /**
     * Sets the farm.
     * @param farm the new farm
     */
    public void setFarm(Farm farm) {

        this.farm = farm;
    }

    /**
     * Gets the ics type.
     * @return the ics type
     */
    public Integer getIcsType() {

        return icsType;
    }

    /**
     * Sets the ics type.
     * @param icsType the new ics type
     */
    public void setIcsType(Integer icsType) {

        this.icsType = icsType;
    }

    /**
     * Gets the land ics details.
     * @return the land ics details
     */
    public String getLandIcsDetails() {

        return landIcsDetails;
    }

    /**
     * Sets the land ics details.
     * @param landIcsDetails the new land ics details
     */
    public void setLandIcsDetails(String landIcsDetails) {

        this.landIcsDetails = landIcsDetails;
    }

    /**
     * Gets the begin of conversion.
     * @return the begin of conversion
     */
    public Date getBeginOfConversion() {

        return beginOfConversion;
    }

    /**
     * Sets the begin of conversion.
     * @param beginOfConversion the new begin of conversion
     */
    public void setBeginOfConversion(Date beginOfConversion) {

        this.beginOfConversion = beginOfConversion;
    }

    /**
     * Gets the survey no.
     * @return the survey no
     */
    public String getSurveyNo() {

        return surveyNo;
    }

    /**
     * Sets the survey no.
     * @param surveyNo the new survey no
     */
    public void setSurveyNo(String surveyNo) {

        this.surveyNo = surveyNo;
    }

    /**
     * Gets the status.
     * @return the status
     */
    public Integer getStatus() {

        return status;
    }

    /**
     * Sets the status.
     * @param status the new status
     */
    public void setStatus(Integer status) {

        this.status = status;
    }

    /**
     * Gets the in active.
     * @return the in active
     */
    public static int getInActive() {

        return IN_ACTIVE;
    }

    public String getBeginOfConversionString() {
    
        return beginOfConversionString;
    }

    public void setBeginOfConversionString(String beginOfConversionString) {
    
        this.beginOfConversionString = beginOfConversionString;
    }
    
    

}
