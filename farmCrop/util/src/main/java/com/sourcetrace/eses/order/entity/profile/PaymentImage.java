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

import java.util.Date;

import com.sourcetrace.eses.order.entity.txn.AgroTransaction;

public class PaymentImage implements Comparable<PaymentImage> {

	private long id;
	private byte[] photo;
	private Date photoCaptureTime;
	private String latitude;
	private String longitude;
	private AgroTransaction txn;

	// Transient variable
	private String imageByteString;

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


	public String getImageByteString() {
		return imageByteString;
	}

	public void setImageByteString(String imageByteString) {
		this.imageByteString = imageByteString;
	}

	public int compareTo(PaymentImage paramT) {

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

	public AgroTransaction getTxn() {
		return txn;
	}

	public void setTxn(AgroTransaction txn) {
		this.txn = txn;
	}
}
