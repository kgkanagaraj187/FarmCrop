package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;
import java.util.Set;

import com.sourcetrace.esesw.entity.profile.ProcurementProduct;

public class ForecastAdvisory 
{
	
	private Long id;
	private ProcurementProduct procurementProduct;
	private String description;
	private Date createdDate;
	private Date updatedDate;
	private Set<ForecastAdvisoryDetails> forecastAdvisoryDetails;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	public ProcurementProduct getProcurementProduct() {
		return procurementProduct;
	}
	public void setProcurementProduct(ProcurementProduct procurementProduct) {
		this.procurementProduct = procurementProduct;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public Set<ForecastAdvisoryDetails> getForecastAdvisoryDetails() {
		return forecastAdvisoryDetails;
	}
	public void setForecastAdvisoryDetails(Set<ForecastAdvisoryDetails> forecastAdvisoryDetails) {
		this.forecastAdvisoryDetails = forecastAdvisoryDetails;
	}

}
