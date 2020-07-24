package com.sourcetrace.eses.entity;

import java.util.Date;

import com.sourcetrace.esesw.entity.profile.Village;

public class FarmerFeedbackEntity
{
	
	private Long id;
	private String farmerId;
	private String question1;
	private String question2;
	private String question3;
	private String question4;
	private Village village;
	private Warehouse warehouse;
	private String farmerName;
	private Date answeredDate;

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFarmerId() {
		return farmerId;
	}
	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}
	public String getQuestion1() {
		return question1;
	}
	public void setQuestion1(String question1) {
		this.question1 = question1;
	}
	public String getQuestion2() {
		return question2;
	}
	public void setQuestion2(String question2) {
		this.question2 = question2;
	}
	public String getQuestion3() {
		return question3;
	}
	public void setQuestion3(String question3) {
		this.question3 = question3;
	}
	public String getQuestion4() {
		return question4;
	}
	public void setQuestion4(String question4) {
		this.question4 = question4;
	}
	public Date getAnsweredDate() {
		return answeredDate;
	}
	public void setAnsweredDate(Date answeredDate) {
		this.answeredDate = answeredDate;
	}
	public Village getVillage() {
		return village;
	}
	public void setVillage(Village village) {
		this.village = village;
	}
	public Warehouse getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}
	public String getFarmerName() {
		return farmerName;
	}
	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
	}
	
	
	
}
