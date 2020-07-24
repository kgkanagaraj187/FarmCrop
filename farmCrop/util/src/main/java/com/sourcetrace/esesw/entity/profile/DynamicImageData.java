package com.sourcetrace.esesw.entity.profile;

import java.io.File;
import java.util.Date;

public class DynamicImageData {
	
	public static enum TYPES {
		digitalSignature,agentSignature
	}
	
	private Long id;
	private byte[] image;
	private Date photoCaptureTime;
	private String latitude;
	private String longitude;
	private String fileExt;
	private String order;
	private FarmerDynamicFieldsValue farmerDynamicFieldsValue;
	private FarmerDynamicData farmerDynamicData;
	private int typez;
	
	


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public Date getPhotoCaptureTime() {
		return photoCaptureTime;
	}

	public void setPhotoCaptureTime(Date photoCaptureTime) {
		this.photoCaptureTime = photoCaptureTime;
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

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public FarmerDynamicFieldsValue getFarmerDynamicFieldsValue() {
		return farmerDynamicFieldsValue;
	}

	public void setFarmerDynamicFieldsValue(FarmerDynamicFieldsValue farmerDynamicFieldsValue) {
		this.farmerDynamicFieldsValue = farmerDynamicFieldsValue;
	}

	public String getFileExt() {
		return fileExt;
	}

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public FarmerDynamicData getFarmerDynamicData() {
		return farmerDynamicData;
	}

	public void setFarmerDynamicData(FarmerDynamicData farmerDynamicData) {
		this.farmerDynamicData = farmerDynamicData;
	}

	public int getTypez() {
		return typez;
	}

	public void setTypez(int typez) {
		this.typez = typez;
	}
	
	

}
