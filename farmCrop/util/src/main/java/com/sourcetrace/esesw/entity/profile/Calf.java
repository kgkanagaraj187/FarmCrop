package com.sourcetrace.esesw.entity.profile;

import java.util.Date;

public class Calf extends Object
{
	private long id;
	private Date serviceDate;
	private Date lastDateCalving;
	private String bullId;
	private String calfId;
	private String gender;
	private double birthWeight;
	private double calvingIntvalDays;
	private String deliveryProcess;
	
	private String bullIdVal;
	private String deliveryProcessVal;
	private Cow cow;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getServiceDate() {
		return serviceDate;
	}
	public void setServiceDate(Date serviceDate) {
		this.serviceDate = serviceDate;
	}
	public Date getLastDateCalving() {
		return lastDateCalving;
	}
	public void setLastDateCalving(Date lastDateCalving) {
		this.lastDateCalving = lastDateCalving;
	}
	public String getBullId() {
		return bullId;
	}
	public void setBullId(String bullId) {
		this.bullId = bullId;
	}
	public String getCalfId() {
		return calfId;
	}
	public void setCalfId(String calfId) {
		this.calfId = calfId;
	}

	public double getBirthWeight() {
		return birthWeight;
	}
	public void setBirthWeight(double birthWeight) {
		this.birthWeight = birthWeight;
	}
	public double getCalvingIntvalDays() {
		return calvingIntvalDays;
	}
	public void setCalvingIntvalDays(double calvingIntvalDays) {
		this.calvingIntvalDays = calvingIntvalDays;
	}
	public String getDeliveryProcess() {
		return deliveryProcess;
	}
	public void setDeliveryProcess(String deliveryProcess) {
		this.deliveryProcess = deliveryProcess;
	}
	public Cow getCow() {
		return cow;
	}
	public void setCow(Cow cow) {
		this.cow = cow;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBullIdVal() {
		return bullIdVal;
	}
	public void setBullIdVal(String bullIdVal) {
		this.bullIdVal = bullIdVal;
	}
	public String getDeliveryProcessVal() {
		return deliveryProcessVal;
	}
	public void setDeliveryProcessVal(String deliveryProcessVal) {
		this.deliveryProcessVal = deliveryProcessVal;
	}
	

}
