package com.sourcetrace.esesw.entity.profile;

public class FarmerPlants
{
	private Long id;
	private String plants;
	private Long noOfPlants;
	private Long noOfLive;
	private Long farmId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPlants() {
		return plants;
	}
	public void setPlants(String plants) {
		this.plants = plants;
	}
	
	public Long getFarmId() {
		return farmId;
	}
	public void setFarmId(Long farmId) {
		this.farmId = farmId;
	}
	public Long getNoOfPlants() {
		return noOfPlants;
	}
	public void setNoOfPlants(Long noOfPlants) {
		this.noOfPlants = noOfPlants;
	}
	public Long getNoOfLive() {
		return noOfLive;
	}
	public void setNoOfLive(Long noOfLive) {
		this.noOfLive = noOfLive;
	}

		
	

}
