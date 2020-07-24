package com.sourcetrace.esesw.entity.profile;

import java.io.File;
import java.util.Date;
import java.util.Set;

public class Cow
{
 public static final String ELITE_RESEARCH = "1";
 public static final String ELITE_FARMER = "0";
	    
private long id;
private String elitType;
private ResearchStation researchStation;
private String farmerId;
private Farm farm;
private String cowId;
private byte[] photo;
private Date photoCaptureTime;
private String latitude;
private String longitude;
private String tagNo;
private Date dob;
private String sireId;
private String damId;
private String genoType;
private double milkFirstHundPerDay;
private String lactationNo;
private Set<Calf> calfs;
private Date createdDate;
private Date updatedDate;
private long revisionNo;
private String branchId;
private String createdUserName;
private String lastUpdatedUserName;
private File cowImage;
private String cowImageExist;
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}

public String getFarmerId() {
	return farmerId;
}
public void setFarmerId(String farmerId) {
	this.farmerId = farmerId;
}
public Farm getFarm() {
	return farm;
}
public void setFarm(Farm farm) {
	this.farm = farm;
}
public String getCowId() {
	return cowId;
}
public void setCowId(String cowId) {
	this.cowId = cowId;
}
public byte[] getPhoto() {
	return photo;
}
public void setPhoto(byte[] photo) {
	this.photo = photo;
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
public String getTagNo() {
	return tagNo;
}
public void setTagNo(String tagNo) {
	this.tagNo = tagNo;
}
public Date getDob() {
	return dob;
}
public void setDob(Date dob) {
	this.dob = dob;
}
public String getSireId() {
	return sireId;
}
public void setSireId(String sireId) {
	this.sireId = sireId;
}
public String getDamId() {
	return damId;
}
public void setDamId(String damId) {
	this.damId = damId;
}
public String getGenoType() {
	return genoType;
}
public void setGenoType(String genoType) {
	this.genoType = genoType;
}
public double getMilkFirstHundPerDay() {
	return milkFirstHundPerDay;
}
public void setMilkFirstHundPerDay(double milkFirstHundPerDay) {
	this.milkFirstHundPerDay = milkFirstHundPerDay;
}
public String getLactationNo() {
	return lactationNo;
}
public void setLactationNo(String lactationNo) {
	this.lactationNo = lactationNo;
}
public Set<Calf> getCalfs() {
	return calfs;
}
public void setCalfs(Set<Calf> calfs) {
	this.calfs = calfs;
}
public ResearchStation getResearchStation() {
	return researchStation;
}
public void setResearchStation(ResearchStation researchStation) {
	this.researchStation = researchStation;
}
public Date getCreatedDate() {
	return createdDate;
}
public void setCreatedDate(Date createdDate) {
	this.createdDate = createdDate;
}
public Date getUpdatedDate() {
	return updatedDate;
}
public void setUpdatedDate(Date updatedDate) {
	this.updatedDate = updatedDate;
}
public long getRevisionNo() {
	return revisionNo;
}
public void setRevisionNo(long revisionNo) {
	this.revisionNo = revisionNo;
}
public String getBranchId() {
	return branchId;
}
public void setBranchId(String branchId) {
	this.branchId = branchId;
}
public File getCowImage() {
	return cowImage;
}
public void setCowImage(File cowImage) {
	this.cowImage = cowImage;
}
public String getCowImageExist() {
	return cowImageExist;
}
public void setCowImageExist(String cowImageExist) {
	this.cowImageExist = cowImageExist;
}
public String getElitType() {
	return elitType;
}
public void setElitType(String elitType) {
	this.elitType = elitType;
}
public String getCreatedUserName() {
	return createdUserName;
}
public void setCreatedUserName(String createdUserName) {
	this.createdUserName = createdUserName;
}
public String getLastUpdatedUserName() {
	return lastUpdatedUserName;
}
public void setLastUpdatedUserName(String lastUpdatedUserName) {
	this.lastUpdatedUserName = lastUpdatedUserName;
}
	

}
