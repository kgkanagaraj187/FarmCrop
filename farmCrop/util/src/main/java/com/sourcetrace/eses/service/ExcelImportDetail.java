package com.sourcetrace.eses.service;

public class ExcelImportDetail {
    public static enum Status {
        SUCCESS, FAILURE
    }

    private String sNo;
    private String farmerId;
    private String farmerCode;
    private String farmerName;
    private String fatherName;
    private String surName;
   
	private String gender;
    private String dob;
    private String dobString;
    private String age;
    private String education;
    private String maritalStatus;
    private String accBalance;
    private String address;
    private String email;
    private String mobile;
    private String country;
    private String state;
    private String district;
    private String city;
    private String gramPanchayat;
    private String village;
    private String warehouse;
    private String fpo;
    private String phoneNumber;
    private String status;
    private String nameOfICS;
    private String icsCode;
    private String isCertified;
    private String certType;
    private String noOfFamilyMem;
    private String adult;
    private String children;
    private String schoolGoingChild;
    private String migratedMembers;
    private String male;
    private String female;
    private String agriculture;
    private String otherSource;
    private String consumerElectronics;
    private String vehicle;
    private String cellPhone;
    private String housingOwnerShip;
    private String housingOwnerShipOth;
    private String electrifiedHouse;
    private String housingType;
    private String housingTypeOth;
    private String drinkingWaterSource;
    private String drinkingWaterSourceOth;
    private String lifeHealthIns;
    private String cropIns;
    private String loanTaken;
    private String loanTakenFrom;
    private String pincode;
    private String StatusCode;
    private String StatusMsg;
    private String bankAccType;
    private String bankAccNo;
    private String bankName;
    private String branchDetails;
    private String ifscCode;
    private String religion;
    private String socialCategory;
    private String aadharNo;
    private String idProof;
    private String proofNo;
    private String primaryOccupation;
    private String secondaryOccupation;
    /* Farm Variables */
    private String farmfarmerCode;
    private String farmName;
    private String surveyNumber;
    private String totalLandHolding;
    private String proposedPlanting;
    private String platNo;
    private String landOwnership;
    private String landGradient;
    private String isSameAddress;
    private String farmAddress;
    private String approachRoad;
    private String soilType;
    private String soilTexture;
    private String fertilityStatus;
    private String irrigationSource;
    private String irrigationType;
    private String others;
    private String fullTimeWorkersCount;
    private String partTimeWorkersCount;
    private String seasonalWorkesCount;
    private String lastDateOfChemicalApp;
    private String conventionalLands;
    private String pastureLand;
    private String conventionalCrops;
    private String estimatedYield;
    private String ics1LandDetails;
    private String ics1BeginOfConversation;
    private String ics1SurveyNumber;
    private String ics2LandDetails;
    private String ics2BeginOfConversation;
    private String ics2SurveyNumber;
    private String ics3LandDetails;
    private String ics3BeginOfConversation;
    private String ics3SurveyNumber;
    private String organicLandDetails;
    private String organicBeginOfConversation;
    private String organicSurveyNumber;
    private String latitude;
    private String longitude;
    private String landMark;
    private String totalProdArea;
    private String totalOrganicArea;
    private String totalNonOrganicArea;
    private String estYieldToCerti;
    
    /** FARM CROP VARIABLES */
    private String cropFarmCode;
    private String cropCategory;
    private String season;
    private String cultivationType;
    private String cropName;
    private String variety;
    private String sowingDate;
    private String type;
    private String seedSource;
    private String stapleLen;
    private String seedQuantityUsed;
    private String seedQuantityCost;
    private String estimatedYieldCrop;

    public String getsNo() {

        return sNo;
    }

    public void setsNo(String sNo) {

        this.sNo = sNo;
    }

    public String getFarmerCode() {

        return farmerCode;
    }

    public void setFarmerCode(String farmerCode) {

        this.farmerCode = farmerCode;
    }

    public String getFarmerName() {

        return farmerName;
    }

    public void setFarmerName(String farmerName) {

        this.farmerName = farmerName;
    }

    public String getFatherName() {

        return fatherName;
    }

    public void setFatherName(String fatherName) {

        this.fatherName = fatherName;
    }

    public String getGender() {

        return gender;
    }

    public void setGender(String gender) {

        this.gender = gender;
    }

    public String getDob() {

        return dob;
    }

    public void setDob(String dob) {

        this.dob = dob;
    }

    public String getDobString() {

        return dobString;
    }

    public void setDobString(String dobString) {

        this.dobString = dobString;
    }

    public String getAge() {

        return age;
    }

    public void setAge(String age) {

        this.age = age;
    }

    public String getEducation() {

        return education;
    }

    public void setEducation(String education) {

        this.education = education;
    }

    public String getMaritalStatus() {

        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {

        this.maritalStatus = maritalStatus;
    }

    public String getAccBalance() {

        return accBalance;
    }

    public void setAccBalance(String accBalance) {

        this.accBalance = accBalance;
    }

    public String getAddress() {

        return address;
    }

    public void setAddress(String address) {

        this.address = address;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public String getMobile() {

        return mobile;
    }

    public void setMobile(String mobile) {

        this.mobile = mobile;
    }

    public String getCountry() {

        return country;
    }

    public void setCountry(String country) {

        this.country = country;
    }

    public String getState() {

        return state;
    }

    public void setState(String state) {

        this.state = state;
    }

    public String getDistrict() {

        return district;
    }

    public void setDistrict(String district) {

        this.district = district;
    }

    public String getCity() {

        return city;
    }

    public void setCity(String city) {

        this.city = city;
    }

    public String getGramPanchayat() {

        return gramPanchayat;
    }

    public void setGramPanchayat(String gramPanchayat) {

        this.gramPanchayat = gramPanchayat;
    }

    public String getVillage() {

        return village;
    }

    public void setVillage(String village) {

        this.village = village;
    }

    public String getWarehouse() {

        return warehouse;
    }

    public void setWarehouse(String warehouse) {

        this.warehouse = warehouse;
    }

    public String getPhoneNumber() {

        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {

        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public String getNameOfICS() {

        return nameOfICS;
    }

    public void setNameOfICS(String nameOfICS) {

        this.nameOfICS = nameOfICS;
    }

    public String getIcsCode() {

        return icsCode;
    }

    public void setIcsCode(String icsCode) {

        this.icsCode = icsCode;
    }

    public String getCertType() {

        return certType;
    }

    public void setCertType(String certType) {

        this.certType = certType;
    }

    public String getNoOfFamilyMem() {

        return noOfFamilyMem;
    }

    public void setNoOfFamilyMem(String noOfFamilyMem) {

        this.noOfFamilyMem = noOfFamilyMem;
    }

    public String getAdult() {

        return adult;
    }

    public void setAdult(String adult) {

        this.adult = adult;
    }

    public String getChildren() {

        return children;
    }

    public void setChildren(String children) {

        this.children = children;
    }

    public String getSchoolGoingChild() {

        return schoolGoingChild;
    }

    public void setSchoolGoingChild(String schoolGoingChild) {

        this.schoolGoingChild = schoolGoingChild;
    }

    public String getMigratedMembers() {

        return migratedMembers;
    }

    public void setMigratedMembers(String migratedMembers) {

        this.migratedMembers = migratedMembers;
    }

    public String getMale() {

        return male;
    }

    public void setMale(String male) {

        this.male = male;
    }

    public String getFemale() {

        return female;
    }

    public void setFemale(String female) {

        this.female = female;
    }

    public String getAgriculture() {

        return agriculture;
    }

    public void setAgriculture(String agriculture) {

        this.agriculture = agriculture;
    }

    public String getOtherSource() {

        return otherSource;
    }

    public void setOtherSource(String otherSource) {

        this.otherSource = otherSource;
    }

    public String getConsumerElectronics() {

        return consumerElectronics;
    }

    public void setConsumerElectronics(String consumerElectronics) {

        this.consumerElectronics = consumerElectronics;
    }

    public String getVehicle() {

        return vehicle;
    }

    public void setVehicle(String vehicle) {

        this.vehicle = vehicle;
    }

    public String getCellPhone() {

        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {

        this.cellPhone = cellPhone;
    }

    public String getHousingOwnerShip() {

        return housingOwnerShip;
    }

    public void setHousingOwnerShip(String housingOwnerShip) {

        this.housingOwnerShip = housingOwnerShip;
    }

    public String getHousingOwnerShipOth() {

        return housingOwnerShipOth;
    }

    public void setHousingOwnerShipOth(String housingOwnerShipOth) {

        this.housingOwnerShipOth = housingOwnerShipOth;
    }

    public String getElectrifiedHouse() {

        return electrifiedHouse;
    }

    public void setElectrifiedHouse(String electrifiedHouse) {

        this.electrifiedHouse = electrifiedHouse;
    }

    public String getHousingType() {

        return housingType;
    }

    public void setHousingType(String housingType) {

        this.housingType = housingType;
    }

    public String getHousingTypeOth() {

        return housingTypeOth;
    }

    public void setHousingTypeOth(String housingTypeOth) {

        this.housingTypeOth = housingTypeOth;
    }

    public String getDrinkingWaterSource() {

        return drinkingWaterSource;
    }

    public void setDrinkingWaterSource(String drinkingWaterSource) {

        this.drinkingWaterSource = drinkingWaterSource;
    }

    public String getDrinkingWaterSourceOth() {

        return drinkingWaterSourceOth;
    }

    public void setDrinkingWaterSourceOth(String drinkingWaterSourceOth) {

        this.drinkingWaterSourceOth = drinkingWaterSourceOth;
    }

    public String getLifeHealthIns() {

        return lifeHealthIns;
    }

    public void setLifeHealthIns(String lifeHealthIns) {

        this.lifeHealthIns = lifeHealthIns;
    }

    public String getCropIns() {

        return cropIns;
    }

    public void setCropIns(String cropIns) {

        this.cropIns = cropIns;
    }

    public String getLoanTaken() {

        return loanTaken;
    }

    public void setLoanTaken(String loanTaken) {

        this.loanTaken = loanTaken;
    }

    public String getLoanTakenFrom() {

        return loanTakenFrom;
    }

    public void setLoanTakenFrom(String loanTakenFrom) {

        this.loanTakenFrom = loanTakenFrom;
    }

    public String getPincode() {

        return pincode;
    }

    public void setPincode(String pincode) {

        this.pincode = pincode;
    }

    public String getStatusCode() {

        return StatusCode;
    }

    public void setStatusCode(String statusCode) {

        StatusCode = statusCode;
    }

    public String getStatusMsg() {

        return StatusMsg;
    }

    public void setStatusMsg(String statusMsg) {

        StatusMsg = statusMsg;
    }

    public String getBankAccType() {

        return bankAccType;
    }

    public void setBankAccType(String bankAccType) {

        this.bankAccType = bankAccType;
    }

    public String getBankAccNo() {

        return bankAccNo;
    }

    public void setBankAccNo(String bankAccNo) {

        this.bankAccNo = bankAccNo;
    }

    public String getBankName() {

        return bankName;
    }

    public void setBankName(String bankName) {

        this.bankName = bankName;
    }

    public String getBranchDetails() {

        return branchDetails;
    }

    public void setBranchDetails(String branchDetails) {

        this.branchDetails = branchDetails;
    }

    public String getIfscCode() {

        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {

        this.ifscCode = ifscCode;
    }

    public String getFarmfarmerCode() {

        return farmfarmerCode;
    }

    public void setFarmfarmerCode(String farmfarmerCode) {

        this.farmfarmerCode = farmfarmerCode;
    }

    public String getFarmName() {

        return farmName;
    }

    public void setFarmName(String farmName) {

        this.farmName = farmName;
    }

    public String getSurveyNumber() {

        return surveyNumber;
    }

    public void setSurveyNumber(String surveyNumber) {

        this.surveyNumber = surveyNumber;
    }

    public String getTotalLandHolding() {

        return totalLandHolding;
    }

    public void setTotalLandHolding(String totalLandHolding) {

        this.totalLandHolding = totalLandHolding;
    }

    public String getProposedPlanting() {

        return proposedPlanting;
    }

    public void setProposedPlanting(String proposedPlanting) {

        this.proposedPlanting = proposedPlanting;
    }

    public String getLandOwnership() {

        return landOwnership;
    }

    public void setLandOwnership(String landOwnership) {

        this.landOwnership = landOwnership;
    }

    public String getIsSameAddress() {

        return isSameAddress;
    }

    public void setIsSameAddress(String isSameAddress) {

        this.isSameAddress = isSameAddress;
    }

    public String getFarmAddress() {

        return farmAddress;
    }

    public void setFarmAddress(String farmAddress) {

        this.farmAddress = farmAddress;
    }

    public String getApproachRoad() {

        return approachRoad;
    }

    public void setApproachRoad(String approachRoad) {

        this.approachRoad = approachRoad;
    }

    public String getSoilType() {

        return soilType;
    }

    public void setSoilType(String soilType) {

        this.soilType = soilType;
    }

    public String getSoilTexture() {

        return soilTexture;
    }

    public void setSoilTexture(String soilTexture) {

        this.soilTexture = soilTexture;
    }

    public String getFertilityStatus() {

        return fertilityStatus;
    }

    public void setFertilityStatus(String fertilityStatus) {

        this.fertilityStatus = fertilityStatus;
    }

    public String getIrrigationSource() {

        return irrigationSource;
    }

    public void setIrrigationSource(String irrigationSource) {

        this.irrigationSource = irrigationSource;
    }

    public String getIrrigationType() {

        return irrigationType;
    }

    public void setIrrigationType(String irrigationType) {

        this.irrigationType = irrigationType;
    }

    public String getOthers() {

        return others;
    }

    public void setOthers(String others) {

        this.others = others;
    }

    public String getFullTimeWorkersCount() {

        return fullTimeWorkersCount;
    }

    public void setFullTimeWorkersCount(String fullTimeWorkersCount) {

        this.fullTimeWorkersCount = fullTimeWorkersCount;
    }

    public String getPartTimeWorkersCount() {

        return partTimeWorkersCount;
    }

    public void setPartTimeWorkersCount(String partTimeWorkersCount) {

        this.partTimeWorkersCount = partTimeWorkersCount;
    }

    public String getSeasonalWorkesCount() {

        return seasonalWorkesCount;
    }

    public void setSeasonalWorkesCount(String seasonalWorkesCount) {

        this.seasonalWorkesCount = seasonalWorkesCount;
    }

    public String getLastDateOfChemicalApp() {

        return lastDateOfChemicalApp;
    }

    public void setLastDateOfChemicalApp(String lastDateOfChemicalApp) {

        this.lastDateOfChemicalApp = lastDateOfChemicalApp;
    }

    public String getConventionalLands() {

        return conventionalLands;
    }

    public void setConventionalLands(String conventionalLands) {

        this.conventionalLands = conventionalLands;
    }

    public String getPastureLand() {

        return pastureLand;
    }

    public void setPastureLand(String pastureLand) {

        this.pastureLand = pastureLand;
    }

    public String getConventionalCrops() {

        return conventionalCrops;
    }

    public void setConventionalCrops(String conventionalCrops) {

        this.conventionalCrops = conventionalCrops;
    }

    public String getEstimatedYield() {

        return estimatedYield;
    }

    public void setEstimatedYield(String estimatedYield) {

        this.estimatedYield = estimatedYield;
    }

    public String getIcs1LandDetails() {

        return ics1LandDetails;
    }

    public void setIcs1LandDetails(String ics1LandDetails) {

        this.ics1LandDetails = ics1LandDetails;
    }

    public String getIcs1BeginOfConversation() {

        return ics1BeginOfConversation;
    }

    public void setIcs1BeginOfConversation(String ics1BeginOfConversation) {

        this.ics1BeginOfConversation = ics1BeginOfConversation;
    }

    public String getIcs1SurveyNumber() {

        return ics1SurveyNumber;
    }

    public void setIcs1SurveyNumber(String ics1SurveyNumber) {

        this.ics1SurveyNumber = ics1SurveyNumber;
    }

    public String getIcs2LandDetails() {

        return ics2LandDetails;
    }

    public void setIcs2LandDetails(String ics2LandDetails) {

        this.ics2LandDetails = ics2LandDetails;
    }

    public String getIcs2BeginOfConversation() {

        return ics2BeginOfConversation;
    }

    public void setIcs2BeginOfConversation(String ics2BeginOfConversation) {

        this.ics2BeginOfConversation = ics2BeginOfConversation;
    }

    public String getIcs2SurveyNumber() {

        return ics2SurveyNumber;
    }

    public void setIcs2SurveyNumber(String ics2SurveyNumber) {

        this.ics2SurveyNumber = ics2SurveyNumber;
    }

    public String getIcs3LandDetails() {

        return ics3LandDetails;
    }

    public void setIcs3LandDetails(String ics3LandDetails) {

        this.ics3LandDetails = ics3LandDetails;
    }

    public String getIcs3BeginOfConversation() {

        return ics3BeginOfConversation;
    }

    public void setIcs3BeginOfConversation(String ics3BeginOfConversation) {

        this.ics3BeginOfConversation = ics3BeginOfConversation;
    }

    public String getIcs3SurveyNumber() {

        return ics3SurveyNumber;
    }

    public void setIcs3SurveyNumber(String ics3SurveyNumber) {

        this.ics3SurveyNumber = ics3SurveyNumber;
    }

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

    public String getLandMark() {

        return landMark;
    }

    public void setLandMark(String landMark) {

        this.landMark = landMark;
    }

    public String getOrganicLandDetails() {

        return organicLandDetails;
    }

    public void setOrganicLandDetails(String organicLandDetails) {

        this.organicLandDetails = organicLandDetails;
    }

    public String getOrganicBeginOfConversation() {

        return organicBeginOfConversation;
    }

    public void setOrganicBeginOfConversation(String organicBeginOfConversation) {

        this.organicBeginOfConversation = organicBeginOfConversation;
    }

    public String getOrganicSurveyNumber() {

        return organicSurveyNumber;
    }

    public void setOrganicSurveyNumber(String organicSurveyNumber) {

        this.organicSurveyNumber = organicSurveyNumber;
    }

    public String getLandGradient() {

        return landGradient;
    }

    public void setLandGradient(String landGradient) {

        this.landGradient = landGradient;
    }

    public String getCropFarmCode() {

        return cropFarmCode;
    }

    public void setCropFarmCode(String cropFarmCode) {

        this.cropFarmCode = cropFarmCode;
    }

    public String getCropCategory() {

        return cropCategory;
    }

    public void setCropCategory(String cropCategory) {

        this.cropCategory = cropCategory;
    }

    public String getSeason() {

        return season;
    }

    public void setSeason(String season) {

        this.season = season;
    }

    public String getCultivationType() {

        return cultivationType;
    }

    public void setCultivationType(String cultivationType) {

        this.cultivationType = cultivationType;
    }

    public String getCropName() {

        return cropName;
    }

    public void setCropName(String cropName) {

        this.cropName = cropName;
    }

    public String getVariety() {

        return variety;
    }

    public void setVariety(String variety) {

        this.variety = variety;
    }

    public String getSowingDate() {

        return sowingDate;
    }

    public void setSowingDate(String sowingDate) {

        this.sowingDate = sowingDate;
    }

    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    public String getSeedSource() {

        return seedSource;
    }

    public void setSeedSource(String seedSource) {

        this.seedSource = seedSource;
    }

    public String getStapleLen() {

        return stapleLen;
    }

    public void setStapleLen(String stapleLen) {

        this.stapleLen = stapleLen;
    }

    public String getSeedQuantityUsed() {

        return seedQuantityUsed;
    }

    public void setSeedQuantityUsed(String seedQuantityUsed) {

        this.seedQuantityUsed = seedQuantityUsed;
    }

    public String getSeedQuantityCost() {

        return seedQuantityCost;
    }

    public void setSeedQuantityCost(String seedQuantityCost) {

        this.seedQuantityCost = seedQuantityCost;
    }

    public String getEstimatedYieldCrop() {

        return estimatedYieldCrop;
    }

    public void setEstimatedYieldCrop(String estimatedYieldCrop) {

        this.estimatedYieldCrop = estimatedYieldCrop;
    }

    public String getIsCertified() {

        return isCertified;
    }

    public void setIsCertified(String isCertified) {

        this.isCertified = isCertified;
    }

    public String getReligion() {
    
        return religion;
    }

    public void setReligion(String religion) {
    
        this.religion = religion;
    }

    public String getSocialCategory() {
    
        return socialCategory;
    }

    public void setSocialCategory(String socialCategory) {
    
        this.socialCategory = socialCategory;
    }

    public String getAadharNo() {
    
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
    
        this.aadharNo = aadharNo;
    }

    public String getIdProof() {
    
        return idProof;
    }

    public void setIdProof(String idProof) {
    
        this.idProof = idProof;
    }

    public String getProofNo() {
    
        return proofNo;
    }

    public void setProofNo(String proofNo) {
    
        this.proofNo = proofNo;
    }

    public String getPrimaryOccupation() {
    
        return primaryOccupation;
    }

    public void setPrimaryOccupation(String primaryOccupation) {
    
        this.primaryOccupation = primaryOccupation;
    }

    public String getSecondaryOccupation() {
    
        return secondaryOccupation;
    }

    public void setSecondaryOccupation(String secondaryOccupation) {
    
        this.secondaryOccupation = secondaryOccupation;
    }
    
    public String getSurName() {
		return surName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}

	public String getFpo() {
		return fpo;
	}

	public void setFpo(String fpo) {
		this.fpo = fpo;
	}

	public String getPlatNo() {
		return platNo;
	}

	public void setPlatNo(String platNo) {
		this.platNo = platNo;
	}

	public String getTotalProdArea() {
		return totalProdArea;
	}

	public void setTotalProdArea(String totalProdArea) {
		this.totalProdArea = totalProdArea;
	}

	public String getTotalOrganicArea() {
		return totalOrganicArea;
	}

	public void setTotalOrganicArea(String totalOrganicArea) {
		this.totalOrganicArea = totalOrganicArea;
	}

	public String getTotalNonOrganicArea() {
		return totalNonOrganicArea;
	}

	public void setTotalNonOrganicArea(String totalNonOrganicArea) {
		this.totalNonOrganicArea = totalNonOrganicArea;
	}

	public String getEstYieldToCerti() {
		return estYieldToCerti;
	}

	public void setEstYieldToCerti(String estYieldToCerti) {
		this.estYieldToCerti = estYieldToCerti;
	}

	public String getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}
	

}
