package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;

import com.ese.entity.profile.CropSupply;

public class CropSupplyImages {
	private long id;
	private byte[] photo;
	private CropSupply cropSupply;
	private Date captureTime;
	private String latitude;
	private String longitude;
	private String imageByteString;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public CropSupply getCropSupply() {
		return cropSupply;
	}

	public void setCropSupply(CropSupply cropSupply) {
		this.cropSupply = cropSupply;
	}

	public Date getCaptureTime() {
		return captureTime;
	}

	public void setCaptureTime(Date captureTime) {
		this.captureTime = captureTime;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getImageByteString() {
		return imageByteString;
	}

	public void setImageByteString(String imageByteString) {
		this.imageByteString = imageByteString;
	}

}
