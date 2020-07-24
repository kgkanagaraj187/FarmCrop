/*
 * OfflineFarmEnrollment.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

public class OfflineFarmEnrollment {

    private long id;
    private String farmCode;
    private String farmName;
    private String hectares;
    private String landInProduction;
    private String landNotInProduction;
    private String latitude;
    private String longitude;
    private byte[] photo;
    private String photoCaptureTime;
    private OfflineFarmerEnrollment farmer;

    /**
     * Gets the id.
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Gets the farm code.
     * @return the farm code
     */
    public String getFarmCode() {

        return farmCode;
    }

    /**
     * Gets the farm name.
     * @return the farm name
     */
    public String getFarmName() {

        return farmName;
    }

    /**
     * Gets the hectares.
     * @return the hectares
     */
    public String getHectares() {

        return hectares;
    }

    /**
     * Gets the land in production.
     * @return the land in production
     */
    public String getLandInProduction() {

        return landInProduction;
    }

    /**
     * Gets the land not in production.
     * @return the land not in production
     */
    public String getLandNotInProduction() {

        return landNotInProduction;
    }

    /**
     * Gets the farmer.
     * @return the farmer
     */
    public OfflineFarmerEnrollment getFarmer() {

        return farmer;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Sets the farm code.
     * @param farmCode the new farm code
     */
    public void setFarmCode(String farmCode) {

        this.farmCode = farmCode;
    }

    /**
     * Sets the farm name.
     * @param farmName the new farm name
     */
    public void setFarmName(String farmName) {

        this.farmName = farmName;
    }

    /**
     * Sets the hectares.
     * @param hectares the new hectares
     */
    public void setHectares(String hectares) {

        this.hectares = hectares;
    }

    /**
     * Sets the land in production.
     * @param landInProduction the new land in production
     */
    public void setLandInProduction(String landInProduction) {

        this.landInProduction = landInProduction;
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
     * Gets the photo.
     * @return the photo
     */
    public byte[] getPhoto() {

        return photo;
    }

    /**
     * Sets the photo.
     * @param photo the new photo
     */
    public void setPhoto(byte[] photo) {

        this.photo = photo;
    }

    /**
     * Gets the photo capture time.
     * @return the photo capture time
     */
    public String getPhotoCaptureTime() {

        return photoCaptureTime;
    }

    /**
     * Sets the photo capture time.
     * @param photoCaptureTime the new photo capture time
     */
    public void setPhotoCaptureTime(String photoCaptureTime) {

        this.photoCaptureTime = photoCaptureTime;
    }

    /**
     * Sets the land not in production.
     * @param landNotInProduction the new land not in production
     */
    public void setLandNotInProduction(String landNotInProduction) {

        this.landNotInProduction = landNotInProduction;
    }

    /**
     * Sets the farmer.
     * @param farmer the new farmer
     */
    public void setFarmer(OfflineFarmerEnrollment farmer) {

        this.farmer = farmer;
    }

}
