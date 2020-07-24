package com.ese.entity.profile;

import java.util.List;

public class ViewFarmerActivity {

	private Long fid;
	private String farmerId;
	private String firstName;
	private int distCount;
	private int procCount;
	private int periodicCount;
	private int trainingCount;
	private String branchId;

	//transient variable
	private List<String> branchesList;
	
	

	public Long getFid() {
		return fid;
	}

	public void setFid(Long fid) {
		this.fid = fid;
	}

	public String getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}

	public int getDistCount() {
		return distCount;
	}

	public void setDistCount(int distCount) {
		this.distCount = distCount;
	}

	public int getProcCount() {
		return procCount;
	}

	public void setProcCount(int procCount) {
		this.procCount = procCount;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public List<String> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}

	public int getPeriodicCount() {
		return periodicCount;
	}

	public void setPeriodicCount(int periodicCount) {
		this.periodicCount = periodicCount;
	}

	public int getTrainingCount() {
		return trainingCount;
	}

	public void setTrainingCount(int trainingCount) {
		this.trainingCount = trainingCount;
	}
	


}