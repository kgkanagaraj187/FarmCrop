package com.sourcetrace.eses.order.entity.txn;

import java.io.File;
import java.util.Date;

public class OfflinePMTImageDetails {
	private Long id;
	private Long pmt;
	private Date captureTime;
	private String latitude;
	private String longitude;
	private byte[] photo;
	private String imageByteString;
	private File imageFile;
	private Integer type;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getImageByteString() {
		return imageByteString;
	}

	public void setImageByteString(String imageByteString) {
		this.imageByteString = imageByteString;
	}

	public File getImageFile() {
		return imageFile;
	}

	public void setImageFile(File imageFile) {
		this.imageFile = imageFile;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getPmt() {
		return pmt;
	}

	public void setPmt(Long pmt) {
		this.pmt = pmt;
	}

}
