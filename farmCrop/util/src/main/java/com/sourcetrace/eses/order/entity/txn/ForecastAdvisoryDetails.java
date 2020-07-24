package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;
import java.util.List;

public class ForecastAdvisoryDetails 
{
	
	private long id;
	private String minimumHumi;
	private String maximumHumi;
	private String minimumWind;
	private String maximumWind;
	private String minimumRain;
	private String maximumRain;
	private String minimumTemp;
	private String maximumTemp;
	private Date createdDate;
	private Date updatedDate;
	
	private ForecastAdvisory forecastAdvisory;
	private List<String> branchesList;
	private String branchId;
	
	public List<String> getBranchesList() {
		return branchesList;
	}
	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	
	public String getMinimumHumi() {
		return minimumHumi;
	}
	public void setMinimumHumi(String minimumHumi) {
		this.minimumHumi = minimumHumi;
	}
	public String getMaximumHumi() {
		return maximumHumi;
	}
	public void setMaximumHumi(String maximumHumi) {
		this.maximumHumi = maximumHumi;
	}
	public String getMinimumWind() {
		return minimumWind;
	}
	public void setMinimumWind(String minimumWind) {
		this.minimumWind = minimumWind;
	}
	public String getMaximumWind() {
		return maximumWind;
	}
	public void setMaximumWind(String maximumWind) {
		this.maximumWind = maximumWind;
	}
	public String getMinimumRain() {
		return minimumRain;
	}
	public void setMinimumRain(String minimumRain) {
		this.minimumRain = minimumRain;
	}
	public String getMaximumRain() {
		return maximumRain;
	}
	public void setMaximumRain(String maximumRain) {
		this.maximumRain = maximumRain;
	}
	public String getMinimumTemp() {
		return minimumTemp;
	}
	public void setMinimumTemp(String minimumTemp) {
		this.minimumTemp = minimumTemp;
	}
	public String getMaximumTemp() {
		return maximumTemp;
	}
	public void setMaximumTemp(String maximumTemp) {
		this.maximumTemp = maximumTemp;
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
	public ForecastAdvisory getForecastAdvisory() {
		return forecastAdvisory;
	}
	public void setForecastAdvisory(ForecastAdvisory forecastAdvisory) {
		this.forecastAdvisory = forecastAdvisory;
	}
	

	
	
	

}
