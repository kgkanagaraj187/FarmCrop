package com.sourcetrace.eses.order.entity.txn;

import java.io.File;
import java.util.Date;

public class PMTImageDetails {
	
	public static enum Type {
		NA, PMT, SPINNING,GMO,PRODUCT_RETURN
	}
	private Long id;
	private Long offlinePMT;
	private Long pmt;
	private Date captureTime;
	private String latitude;
	private String longitude;
	private byte[] photo;
	private Integer type;
	private String imageByteString;
	private File imageFile;
	private String fileName;
	private String fileType;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPmt() {
		return pmt;
	}

	public void setPmt(Long pmt) {
		this.pmt = pmt;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Long getOfflinePMT() {
		return offlinePMT;
	}

	public void setOfflinePMT(Long offlinePMT) {
		this.offlinePMT = offlinePMT;
	}
	
	

}
