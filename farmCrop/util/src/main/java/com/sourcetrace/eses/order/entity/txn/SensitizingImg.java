package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;

public class SensitizingImg {
	private Long id;
	private Sensitizing sentizing;
	private Date captureTime;
	private String latitude;
	private String longitude;
	private String imageByteString;
	private byte[] photo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Sensitizing getSentizing() {
		return sentizing;
	}

	public void setSentizing(Sensitizing sentizing) {
		this.sentizing = sentizing;
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

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

}
