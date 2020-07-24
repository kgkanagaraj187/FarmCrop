/*
 * ImageInfo.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.util.entity;

public class ImageInfo {

	private long id;
	private Image photo;
	private Image biometric;

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
	public Image getPhoto() {
		return photo;
	}

	/**
	 * Sets the photo.
	 * 
	 * @param photo
	 *            the new photo
	 */
	public void setPhoto(Image photo) {
		this.photo = photo;
	}

	/**
	 * Gets the biometric.
	 * 
	 * @return the biometric
	 */
	public Image getBiometric() {
		return biometric;
	}

	/**
	 * Sets the biometric.
	 * 
	 * @param biometric
	 *            the new biometric
	 */
	public void setBiometric(Image biometric) {
		this.biometric = biometric;
	}
}
