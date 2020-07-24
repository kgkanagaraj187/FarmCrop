package com.ese.entity.profile;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.sourcetrace.esesw.entity.profile.Farmer;

public class CropHarvest {
	private long id;
	private Date harvestDate;
	private Set<CropHarvestDetails> cropHarvestDetails;
	private String farmerId;
	private String farmerName;
	private String farmCode;
	private String farmName;
	private String totalQty;
	private String receiptNo;
	private String branchId;
	private String storageIn;
	private String packedIn;
	private String seasonCode;
	private String otherStorageInType;
	private String otherPackedInType;
	private String warehouseId;
	private String icsname;
	private String fpo;
	private String agentId;
	private String latitude;
	private String longitude;
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	// transient variable
	private String village;
	private String searchPage;
	private String fatherName;
	private List<String> branchesList;
	private String cropName;
	private String stateName;
	public long getId() {

		return id;
	}

	public String getVillage() {
		return village;
	}

	public void setVillage(String village) {
		this.village = village;
	}

	public void setId(long id) {

		this.id = id;
	}

	public Date getHarvestDate() {

		return harvestDate;
	}

	public void setHarvestDate(Date harvestDate) {

		this.harvestDate = harvestDate;
	}

	public Set<CropHarvestDetails> getCropHarvestDetails() {

		return cropHarvestDetails;
	}

	public void setCropHarvestDetails(Set<CropHarvestDetails> cropHarvestDetails) {

		this.cropHarvestDetails = cropHarvestDetails;
	}

	public String getFarmerId() {

		return farmerId;
	}

	public void setFarmerId(String farmerId) {

		this.farmerId = farmerId;
	}

	public String getFarmerName() {

		return farmerName;
	}

	public void setFarmerName(String farmerName) {

		this.farmerName = farmerName;
	}

	public String getFarmCode() {

		return farmCode;
	}

	public void setFarmCode(String farmCode) {

		this.farmCode = farmCode;
	}

	public String getFarmName() {

		return farmName;
	}

	public void setFarmName(String farmName) {

		this.farmName = farmName;
	}

	public String getTotalQty() {

		return totalQty;
	}

	public void setTotalQty(String totalQty) {

		this.totalQty = totalQty;
	}

	public String getReceiptNo() {

		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {

		this.receiptNo = receiptNo;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getSearchPage() {
		return searchPage;
	}

	public void setSearchPage(String searchPage) {
		this.searchPage = searchPage;
	}

	public String getStorageIn() {
		return storageIn;
	}

	public void setStorageIn(String storageIn) {
		this.storageIn = storageIn;
	}

	public String getPackedIn() {
		return packedIn;
	}

	public void setPackedIn(String packedIn) {
		this.packedIn = packedIn;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getSeasonCode() {

		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {

		this.seasonCode = seasonCode;
	}

	public String getOtherStorageInType() {
		return otherStorageInType;
	}

	public void setOtherStorageInType(String otherStorageInType) {
		this.otherStorageInType = otherStorageInType;
	}

	public String getOtherPackedInType() {
		return otherPackedInType;
	}

	public void setOtherPackedInType(String otherPackedInType) {
		this.otherPackedInType = otherPackedInType;
	}

	public List<String> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}


    public String getWarehouseId() {
    
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
    
        this.warehouseId = warehouseId;
    }

    public String getIcsname() {
    
        return icsname;
    }

    public void setIcsname(String icsname) {
    
        this.icsname = icsname;
    }

	public String getFpo() {
		return fpo;
	}

	public void setFpo(String fpo) {
		this.fpo = fpo;
	}

	public String getCropName() {
		return cropName;
	}

	public void setCropName(String cropName) {
		this.cropName = cropName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	

}
