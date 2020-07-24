package com.sourcetrace.esesw.entity.profile;

public class FarmerSoilTesting 
{
	private Long id;
	private String officersSuggestion;
	private String takenAction;
	private Long farmId;
	public String getOfficersSuggestion() {
		return officersSuggestion;
	}
	public void setOfficersSuggestion(String officersSuggestion) {
		this.officersSuggestion = officersSuggestion;
	}
	public String getTakenAction() {
		return takenAction;
	}
	public void setTakenAction(String takenAction) {
		this.takenAction = takenAction;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
		public Long getFarmId() {
		return farmId;
	}
	public void setFarmId(Long farmId) {
		this.farmId = farmId;
	}

	

}
