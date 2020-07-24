package com.sourcetrace.esesw.entity.profile;

import java.util.Date;
import java.util.Set;

public class ResearchStation {
	
private long id;
private String researchStationId;
private String name;
private String division;
private String pointPerson;
private String designation;
private Set<Cow> cows;
private Date createdDate;
private Date updatedDate;
private String branchId;
private String researchStationAddress;
private long revisionNo;
private Locality city;
private Municipality municipality;
private Integer status;

public static final int STATUS_ACTIVE = 1;
public static final int STATUS_INACTIVE = 0;
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public String getResearchStationId() {
	return researchStationId;
}
public void setResearchStationId(String researchStationId) {
	this.researchStationId = researchStationId;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getDivision() {
	return division;
}
public void setDivision(String division) {
	this.division = division;
}
public String getPointPerson() {
	return pointPerson;
}
public void setPointPerson(String pointPerson) {
	this.pointPerson = pointPerson;
}
public String getDesignation() {
	return designation;
}
public void setDesignation(String designation) {
	this.designation = designation;
}
public Set<Cow> getCows() {
	return cows;
}
public void setCows(Set<Cow> cows) {
	this.cows = cows;
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
public String getBranchId() {

    return branchId;
}
public void setBranchId(String branchId) {

    this.branchId = branchId;
}
public String getResearchStationAddress() {

    return researchStationAddress;
}
public void setResearchStationAddress(String researchStationAddress) {

    this.researchStationAddress = researchStationAddress;
}
public long getRevisionNo() {

    return revisionNo;
}
public void setRevisionNo(long revisionNo) {

    this.revisionNo = revisionNo;
}
public Locality getCity() {

    return city;
}
public void setCity(Locality city) {

    this.city = city;
}
public Municipality getMunicipality() {

    return municipality;
}
public void setMunicipality(Municipality municipality) {

    this.municipality = municipality;
}
public Integer getStatus() {

    return status;
}
public void setStatus(Integer status) {

    this.status = status;
}


}
