package com.sourcetrace.eses.order.entity.txn;

import java.io.Serializable;
import java.util.Date;

import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicData;

public class LoanApplication implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private Farmer farmer;
	private String seedlingQty;
	private String loanStatus;
	private String declineReason;
	private String createdUser;
	private String updatedUser;
	private Date createdDate;
	private Date updatedDate;	
	private String branchId;
	private FarmerDynamicData farmerDynamicData;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Farmer getFarmer() {
		return farmer;
	}
	public void setFarmer(Farmer farmer) {
		this.farmer = farmer;
	}
	
	public String getSeedlingQty() {
		return seedlingQty;
	}
	public void setSeedlingQty(String seedlingQty) {
		this.seedlingQty = seedlingQty;
	}
	public String getLoanStatus() {
		return loanStatus;
	}
	public void setLoanStatus(String loanStatus) {
		this.loanStatus = loanStatus;
	}
	public String getDeclineReason() {
		return declineReason;
	}
	public void setDeclineReason(String declineReason) {
		this.declineReason = declineReason;
	}
	public String getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}
	public String getUpdatedUser() {
		return updatedUser;
	}
	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
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
	public FarmerDynamicData getFarmerDynamicData() {
		return farmerDynamicData;
	}
	public void setFarmerDynamicData(FarmerDynamicData farmerDynamicData) {
		this.farmerDynamicData = farmerDynamicData;
	}
	
	
	
}
