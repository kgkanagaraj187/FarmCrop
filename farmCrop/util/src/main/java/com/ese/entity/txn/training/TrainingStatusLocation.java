/*
 * TrainingStatusLocation.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.entity.txn.training;

public class TrainingStatusLocation {
    private long id;
    private byte[] photo;
    private String latitude;
    private String longitude;
    private String updateTime;
    private TrainingStatus trainingStatus;
    private OfflineTrainingStatus offlineTrainingStatus;

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
     * Gets the update time.
     * @return the update time
     */
    public String getUpdateTime() {

        return updateTime;
    }

    /**
     * Sets the update time.
     * @param updateTime the new update time
     */
    public void setUpdateTime(String updateTime) {

        this.updateTime = updateTime;
    }

    /**
     * Gets the training status.
     * @return the training status
     */
    public TrainingStatus getTrainingStatus() {

        return trainingStatus;
    }

    /**
     * Sets the training status.
     * @param trainingStatus the new training status
     */
    public void setTrainingStatus(TrainingStatus trainingStatus) {

        this.trainingStatus = trainingStatus;
    }

    /**
     * Gets the offline training status.
     * @return the offline training status
     */
    public OfflineTrainingStatus getOfflineTrainingStatus() {

        return offlineTrainingStatus;
    }

    /**
     * Sets the offline training status.
     * @param offlineTrainingStatus the new offline training status
     */
    public void setOfflineTrainingStatus(OfflineTrainingStatus offlineTrainingStatus) {

        this.offlineTrainingStatus = offlineTrainingStatus;
    }

}
