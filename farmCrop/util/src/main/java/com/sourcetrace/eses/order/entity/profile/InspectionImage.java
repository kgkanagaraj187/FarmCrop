/*
 * InspectionImage.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.profile;

import java.io.File;
import java.util.Date;

public class InspectionImage implements Comparable<InspectionImage> {

	private long id;
	private byte[] photo;
	private Date photoCaptureTime;
	private String latitude;
	private String longitude;
	private InspectionImageInfo inspectionImageInfo;

	// Transient variable
	private String imageByteString;
	private File imageFile;

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {

		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(long id) {

		this.id = id;
	}

	/**
	 * Gets the photo.
	 * 
	 * @return the photo
	 */
	public byte[] getPhoto() {

		return photo;
	}

	/**
	 * Sets the photo.
	 * 
	 * @param photo
	 *            the new photo
	 */
	public void setPhoto(byte[] photo) {

		this.photo = photo;
	}

	/**
	 * Gets the photo capture time.
	 * 
	 * @return the photo capture time
	 */
	public Date getPhotoCaptureTime() {

		return photoCaptureTime;
	}

	/**
	 * Sets the photo capture time.
	 * 
	 * @param photoCaptureTime
	 *            the new photo capture time
	 */
	public void setPhotoCaptureTime(Date photoCaptureTime) {

		this.photoCaptureTime = photoCaptureTime;
	}

	/**
	 * Gets the latitude.
	 * 
	 * @return the latitude
	 */
	public String getLatitude() {

		return latitude;
	}

	/**
	 * Sets the latitude.
	 * 
	 * @param latitude
	 *            the new latitude
	 */
	public void setLatitude(String latitude) {

		this.latitude = latitude;
	}

	/**
	 * Gets the longitude.
	 * 
	 * @return the longitude
	 */
	public String getLongitude() {

		return longitude;
	}

	/**
	 * Sets the longitude.
	 * 
	 * @param longitude
	 *            the new longitude
	 */
	public void setLongitude(String longitude) {

		this.longitude = longitude;
	}

	/**
	 * Gets the inspection image info.
	 * 
	 * @return the inspection image info
	 */
	public InspectionImageInfo getInspectionImageInfo() {

		return inspectionImageInfo;
	}

	/**
	 * Sets the inspection image info.
	 * 
	 * @param inspectionImageInfo
	 *            the new inspection image info
	 */
	public void setInspectionImageInfo(InspectionImageInfo inspectionImageInfo) {

		this.inspectionImageInfo = inspectionImageInfo;
	}

	public String getImageByteString() {
		return imageByteString;
	}

	public void setImageByteString(String imageByteString) {
		this.imageByteString = imageByteString;
	}

	public int compareTo(InspectionImage paramT) {

		int returnValue = 1;
		if (this.id != 0 && paramT.id != 0) {
			if (this.id < paramT.id) {
				returnValue = -1;
			} else if (this.id == paramT.id) {
				returnValue = 0;
			}
		}
		return returnValue;
	}

    public File getImageFile() {
    
        return imageFile;
    }

    public void setImageFile(File imageFile) {
    
        this.imageFile = imageFile;
    }
	
	

}
