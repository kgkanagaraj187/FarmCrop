package com.sourcetrace.esesw.entity.profile;

import java.util.Set;

public class FarmingSeason
{
	private Long id;
	private String seasonName;
	private Long farmId;
	 private Set<FarmerLandDetails> farmerLandDetails ;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSeasonName() {
		return seasonName;
	}
	public void setSeasonName(String seasonName) {
		this.seasonName = seasonName;
	}
	public Set<FarmerLandDetails> getFarmerLandDetails() {
		return farmerLandDetails;
	}
	public void setFarmerLandDetails(Set<FarmerLandDetails> farmerLandDetails) {
		this.farmerLandDetails = farmerLandDetails;
	}
	public Long getFarmId() {
		return farmId;
	}
	public void setFarmId(Long farmId) {
		this.farmId = farmId;
	}

}
