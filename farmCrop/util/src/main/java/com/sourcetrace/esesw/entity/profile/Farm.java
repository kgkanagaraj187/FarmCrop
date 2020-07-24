/*
 * Farm.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;

import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

// TODO: Auto-generated Javadoc
public class Farm {

    public static final String DECIMAL_PATTERN = "(?:\\d*\\.\\d+|\\d*)";
    public static final String THREE_DECIMAL_PATTERN = "(^\\d{0,8}(\\.\\d{1,3})?$)";
    
    public static final String SOILTESTED_YES = "YES";
    public static final String SOILTESTED_NO = "NO";
    
    
	public static enum Status {
		INACTIVE, ACTIVE, DELETED
	}
	
	
    private long id;
    private String farmCode;
    private String farmName;
    private String farmId;
    private String hectares;
    private String landInProduction;
    private String landNotInProduction;
    private String latitude;
    private String longitude;
    private byte[] photo;
    private Date photoCaptureTime;
    private Farmer farmer;
    private FarmDetailedInfo farmDetailedInfo;
    private byte[] audio;
    private Integer isVerified;
    private Date verifiedDate;
    private String verifiedAgentId;
    private String verifiedAgentName;
    private Set<FarmCrops> farmCrops;
   // private Set<Coordinates> coordinates;
    private Set<FarmICS> farmICS;
    private String landmark;
    private Set<FarmElement> farmElement;
    private Set<FarmElement> farmElementMach;
    private int certYear;
    private Set<Expenditure> expenditures;
    private String landTopology;
    private Set<DocumentUpload> docUpload;
    private String farmOther;
    private Set<FarmIcsConversion> farmICSConversion;
    private String farmRegNumber;
    private String farmPlatNo;
    private long revisionNo;
    //APMAS CHANGES
    private Set<FarmerLandDetails> farmerLandDetails;
    private Set<FarmerScheme> farmerScheme;
    private Set<FarmerPlants> farmerPlants;
    private Set<FarmerSoilTesting> farmerSoilTesting;
    private String ifs;
    private String ifsOther;
    private String vegetableName;
    private String kitchenGarden;
    private String backYardPoultry;
    private String waterConservation;
    private String waterConservationOther;
    private String soilConservation;
    private String soilConservationOther;
    private String serviceCentres;
    private String serviceCentresOther;
    private String soilTesting;
    private String trainingProgram;
    private String trainingProgramOther;
    private int status;
    private int actStatus;
    private String gcParcelID;
    private String cropTimeline;
    private String season;
    //BLRI
    
    private Set<Cow> cows;
    private Set<HousingInfo> housingInfos;
    
    private Date plotCapturingTime;
    
    //AWI
    private Village village;
    private Warehouse samithi;
    private String fpo;
    
    //CANDA
    
    private String ownLand;
    private String leasedLand;
    private String irrigationLand;
    private String waterHarvest;
    private String avgStore;
    private String treeName;
   
    //Wilmar
    private String distanceProcessingUnit;
    private String organicStatus;
    //CofBoard
    private String photoId;
    
    //Olivado
    private String presenceBananaTrees;
    private String parallelProd;
    private String inputOrganicUnit;
    private String presenceHiredLabour;
    private String riskCategory;
    private Set<TreeDetail> treeDetails;
    private Double totalOrganicTrees;
    private Double totalConventionalTrees;
    private Double totalAvocadoTrees;
    private Double hectarOrganicTrees;
    private Double hectarConventionalTrees;
    private Double hectarAvocadoTrees;
   
    // Transient Variable
    private List<FarmICS> farmICSList;
    private File farmImage;
    public String farmImageFileName;
    private String farmImageExist;
    private List<DocumentUpload> docUploadList;
    private FarmIcsConversion farmIcsConv;
    //transient variables for APMAS
     private String  faimingtype;	
	 private String farmtype; 
	 private Double irrigatedLand ;
	 private String jsonString;
	 private String irrigatedFarmingLand ;
	 private String faimingtype1;
	 private String farmtype1; 
	 private Double fedtotaland; 
	 private String fedtotalics; 
	 //Transient	 
	 private String farmerCode;
	 private String tenantId;
	 private String cropCode;
	 private String agriIncome;
	 private String cottonIncome;
	 private String grossAgriIncome;
	 private String interCropIncome;
	 private String otherIncome;
	 private String cultivationId;
	 private String ctnQty;
	 private String ctnSale;
	 
	 private String waterSource;
	 private String localNameOfCrotenTree ;
	 private String NoOfCrotenTrees;
	 private String organicVariety;
	 private String conventionalVariety;
	 private Set<CoordinatesMap> coordinatesMap;
	 private  CoordinatesMap activeCoordinates;
	 private int plottingStatus;
	 private int certificateStandardLevel;
	public List<JSONObject> farmJsonObjectList;
	private String createdUsername;
	private Date createdDate;
	private String lastUpdatedUsername;
	private Date lastUpdatedDate;
	 
	 
	 public String getWaterSource() {
		return waterSource;
	}

	public void setWaterSource(String waterSource) {
		this.waterSource = waterSource;
	}

	public String getLocalNameOfCrotenTree() {
		return localNameOfCrotenTree;
	}

	public void setLocalNameOfCrotenTree(String localNameOfCrotenTree) {
		this.localNameOfCrotenTree = localNameOfCrotenTree;
	}

	public String getNoOfCrotenTrees() {
		return NoOfCrotenTrees;
	}

	public void setNoOfCrotenTrees(String noOfCrotenTrees) {
		NoOfCrotenTrees = noOfCrotenTrees;
	}

	/**
     * Gets the farm image exist.
     * @return the farm image exist
     */
    public String getFarmImageExist() {

        /*
         * String existData = "0"; if(getPhoto().length > 0){ existData = "1"; }
         */
        return farmImageExist;
    }

    /**
     * Sets the farm image exist.
     * @param farmImageExist the new farm image exist
     */
    public void setFarmImageExist(String farmImageExist) {

        this.farmImageExist = farmImageExist;
    }

    /**
     * Gets the id.
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Gets the farm code.
     * @return the farm code
     */
   // @Pattern(regexp = "[^\\p{Punct}]+$", message = "pattern.farmCode")
 //   @NotEmpty(message = "empty.farmCode")
    public String getFarmCode() {

        return farmCode;
    }

    /**
     * Sets the farm code.
     * @param farmCode the new farm code
     */
    public void setFarmCode(String farmCode) {

        this.farmCode = farmCode;
    }

    /**
     * Gets the farm name.
     * @return the farm name
     */
   // @Pattern(regexp = "[^\\p{Punct}]+$", message = "pattern.farmName")
  //  @NotEmpty(message = "empty.farmName")
    public String getFarmName() {

        StringBuilder builder = new StringBuilder();
        if (!StringUtil.isEmpty(this.farmName))
            builder.append(this.farmName);

        return builder.toString();
    }

    /**
     * Sets the farm name.
     * @param farmName the new farm name
     */
    public void setFarmName(String farmName) {

        this.farmName = farmName;
    }

    /**
     * Gets the hectares.
     * @return the hectares
     */
   // @Pattern(regexp = DECIMAL_PATTERN, message = "pattern.hectares")
   // @NotEmpty(message = "empty.hectares")
    public String getHectares() {

        return hectares;
    }

    /**
     * Sets the hectares.
     * @param hectares the new hectares
     */
    public void setHectares(String hectares) {

        this.hectares = hectares;
    }

    /**
     * Gets the land in production.
     * @return the land in production
     */
   // @Pattern(regexp = THREE_DECIMAL_PATTERN, message = "pattern.landInProduction")
   // @NotEmpty(message = "empty.landInProduction")
    public String getLandInProduction() {

        return landInProduction;
    }

    /**
     * Sets the land in production.
     * @param landInProduction the new land in production
     */
    public void setLandInProduction(String landInProduction) {

        this.landInProduction = landInProduction;
    }

    /**
     * Gets the land not in production.
     * @return the land not in production
     */
   // @Pattern(regexp = THREE_DECIMAL_PATTERN, message = "pattern.landNotInProduction")
    public String getLandNotInProduction() {

        return landNotInProduction;
    }

    /**
     * Sets the land not in production.
     * @param landNotInProduction the new land not in production
     */
    public void setLandNotInProduction(String landNotInProduction) {

        this.landNotInProduction = landNotInProduction;
    }

    /**
     * Gets the latitude.
     * @return the latitude
     */
   // @Length(max = Municipality.MAX_LENGTH_LANANDLOG, message = "length.latitude")
   // @Pattern(regexp = Municipality.EXPRESSION_LATITUDE, message = "pattern.latitude")
    /* @NotEmpty(message = "empty.latitude") */
    public String getLatitude() {

        return latitude;
    }

    /**
     * Sets the latitude.
     * @param latitude the new latitude
     */
    public void setLatitude(String latitude) {

        this.latitude = latitude;
    }

    /**
     * Gets the longitude.
     * @return the longitude
     */
   // @Length(max = Municipality.MAX_LENGTH_LANANDLOG, message = "length.longitude")
  //  @Pattern(regexp = Municipality.EXPRESSION_LONGITUDE, message = "pattern.longitude")
    /* @NotEmpty(message = "empty.longitude") */
    public String getLongitude() {

        return longitude;
    }

    /**
     * Sets the longitude.
     * @param longitude the new longitude
     */
    public void setLongitude(String longitude) {

        this.longitude = longitude;
    }

    /**
     * Gets the photo.
     * @return the photo
     */
    public byte[] getPhoto() {

        return photo;
    }

    /**
     * Sets the photo.
     * @param photo the new photo
     */
    public void setPhoto(byte[] photo) {

        this.photo = photo;
    }

    /**
     * Gets the photo capture time.
     * @return the photo capture time
     */
    public Date getPhotoCaptureTime() {

        return photoCaptureTime;
    }

    /**
     * Sets the photo capture time.
     * @param photoCaptureTime the new photo capture time
     */
    public void setPhotoCaptureTime(Date photoCaptureTime) {

        this.photoCaptureTime = photoCaptureTime;
    }

    /**
     * Sets the farmer.
     * @param farmer the new farmer
     */
    public void setFarmer(Farmer farmer) {

        this.farmer = farmer;
    }

    /**
     * Gets the farmer.
     * @return the farmer
     */
    public Farmer getFarmer() {

        return farmer;
    }

    /**
     * Sets the farm crops.
     * @param farmCrops the new farm crops
     */
    public void setFarmCrops(Set<FarmCrops> farmCrops) {

        this.farmCrops = farmCrops;
    }

    /**
     * Gets the farm crops.
     * @return the farm crops
     */
    public Set<FarmCrops> getFarmCrops() {

        return farmCrops;
    }

    /**
     * Sets the farm detailed info.
     * @param farmDetailedInfo the new farm detailed info
     */
    public void setFarmDetailedInfo(FarmDetailedInfo farmDetailedInfo) {

        this.farmDetailedInfo = farmDetailedInfo;
    }

    /**
     * Gets the farm detailed info.
     * @return the farm detailed info
     */
    public FarmDetailedInfo getFarmDetailedInfo() {

        return farmDetailedInfo;
    }

    /**
     * Gets the audio.
     * @return the audio
     */
    public byte[] getAudio() {

        return audio;
    }

    /**
     * Sets the audio.
     * @param audio the new audio
     */
    public void setAudio(byte[] audio) {

        this.audio = audio;
    }

    /**
     * Gets the is verified.
     * @return the is verified
     */
    public Integer getIsVerified() {

        return isVerified;
    }

    /**
     * Sets the is verified.
     * @param isVerified the new is verified
     */
    public void setIsVerified(Integer isVerified) {

        this.isVerified = isVerified;
    }

    /**
     * Gets the verified date.
     * @return the verified date
     */
    public Date getVerifiedDate() {

        return verifiedDate;
    }

    /**
     * Sets the verified date.
     * @param verifiedDate the new verified date
     */
    public void setVerifiedDate(Date verifiedDate) {

        this.verifiedDate = verifiedDate;
    }

    /**
     * Gets the verified agent id.
     * @return the verified agent id
     */
    public String getVerifiedAgentId() {

        return verifiedAgentId;
    }

    /**
     * Sets the verified agent id.
     * @param verifiedAgentId the new verified agent id
     */
    public void setVerifiedAgentId(String verifiedAgentId) {

        this.verifiedAgentId = verifiedAgentId;
    }

    /**
     * Gets the verified agent name.
     * @return the verified agent name
     */
    public String getVerifiedAgentName() {

        return verifiedAgentName;
    }

    /**
     * Sets the verified agent name.
     * @param verifiedAgentName the new verified agent name
     */
    public void setVerifiedAgentName(String verifiedAgentName) {

        this.verifiedAgentName = verifiedAgentName;
    }

    /**
     * Sets the coordinates.
     * @param coordinates the new coordinates
     */
   /* public void setCoordinates(Set<Coordinates> coordinates) {

        this.coordinates = coordinates;
    }
*/
    /**
     * Gets the coordinates.
     * @return the coordinates
     */
    /*public Set<Coordinates> getCoordinates() {

        return coordinates;
    }
*/
    /**
     * Gets the farm ics.
     * @return the farm ics
     */
    public Set<FarmICS> getFarmICS() {

        return farmICS;
    }

    /**
     * Sets the farm ics.
     * @param farmICS the new farm ics
     */
    public void setFarmICS(Set<FarmICS> farmICS) {

        this.farmICS = farmICS;
    }

    /**
     * Gets the farm ics list.
     * @return the farm ics list
     */
    public List<FarmICS> getFarmICSList() {

        return farmICSList;
    }

    /**
     * Sets the farm ics list.
     * @param farmICSList the new farm ics list
     */
    public void setFarmICSList(List<FarmICS> farmICSList) {

        this.farmICSList = farmICSList;
    }

    /**
     * Gets the farm image.
     * @return the farm image
     */
    public File getFarmImage() {

        return farmImage;
    }

    /**
     * Sets the farm image.
     * @param farmImage the new farm image
     */
    public void setFarmImage(File farmImage) {

        this.farmImage = farmImage;
    }

    /**
     * Gets the farm id and name.
     * @return the farm id and name
     */
    public String getFarmIdAndName() {

        StringBuffer sb = new StringBuffer();
        if (!StringUtil.isEmpty(farmCode)) {
            sb.append(farmCode);
            if (!StringUtil.isEmpty(farmName)) {
                sb.append(" - ");
            }
        }

        if (!StringUtil.isEmpty(farmName)) {
            sb.append(farmName);
        }
        return sb.toString();
    }

    /**
     * Gets the farm image file name.
     * @return the farm image file name
     */
    public String getFarmImageFileName() {

        return farmImageFileName;
    }

    /**
     * Sets the farm image file name.
     * @param farmImageFileName the new farm image file name
     */
    public void setFarmImageFileName(String farmImageFileName) {

        this.farmImageFileName = farmImageFileName;
    }

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public Set<FarmElement> getFarmElement() {
		return farmElement;
	}

	public void setFarmElement(Set<FarmElement> farmElement) {
		this.farmElement = farmElement;
	}

	public Set<FarmElement> getFarmElementMach() {
		return farmElementMach;
	}

	public void setFarmElementMach(Set<FarmElement> farmElementMach) {
		this.farmElementMach = farmElementMach;
	}
	
	public Set<FarmerScheme> getFarmerScheme() {
		return farmerScheme;
	}

	public void setFarmerScheme(Set<FarmerScheme> farmerScheme) {
		this.farmerScheme = farmerScheme;
	}

	public Set<FarmerPlants> getFarmerPlants() {
		return farmerPlants;
	}

	public void setFarmerPlants(Set<FarmerPlants> farmerPlants) {
		this.farmerPlants = farmerPlants;
	}

	public Set<FarmerSoilTesting> getFarmerSoilTesting() {
		return farmerSoilTesting;
	}

	public void setFarmerSoilTesting(Set<FarmerSoilTesting> farmerSoilTesting) {
		this.farmerSoilTesting = farmerSoilTesting;
	}
	

	
	
	public String getIfs() {
		return ifs;
	}

	public void setIfs(String ifs) {
		this.ifs = ifs;
	}

	public String getIfsOther() {
		return ifsOther;
	}

	public void setIfsOther(String ifsOther) {
		this.ifsOther = ifsOther;
	}

	public String getVegetableName() {
		return vegetableName;
	}

	public void setVegetableName(String vegetableName) {
		this.vegetableName = vegetableName;
	}

	public String getKitchenGarden() {
		return kitchenGarden;
	}

	public void setKitchenGarden(String kitchenGarden) {
		this.kitchenGarden = kitchenGarden;
	}

	public String getBackYardPoultry() {
		return backYardPoultry;
	}

	public void setBackYardPoultry(String backYardPoultry) {
		this.backYardPoultry = backYardPoultry;
	}

	public String getSoilConservation() {
		return soilConservation;
	}

	public void setSoilConservation(String soilConservation) {
		this.soilConservation = soilConservation;
	}

	public String getSoilConservationOther() {
		return soilConservationOther;
	}

	public void setSoilConservationOther(String soilConservationOther) {
		this.soilConservationOther = soilConservationOther;
	}

	public String getServiceCentres() {
		return serviceCentres;
	}

	public void setServiceCentres(String serviceCentres) {
		this.serviceCentres = serviceCentres;
	}

	public String getServiceCentresOther() {
		return serviceCentresOther;
	}

	public void setServiceCentresOther(String serviceCentresOther) {
		this.serviceCentresOther = serviceCentresOther;
	}

	public String getSoilTesting() {
		return soilTesting;
	}

	public void setSoilTesting(String soilTesting) {
		this.soilTesting = soilTesting;
	}

	public String getTrainingProgram() {
		return trainingProgram;
	}

	public void setTrainingProgram(String trainingProgram) {
		this.trainingProgram = trainingProgram;
	}
	
	public String getWaterConservation() {
		return waterConservation;
	}

	public void setWaterConservation(String waterConservation) {
		this.waterConservation = waterConservation;
	}

	public String getWaterConservationOther() {
		return waterConservationOther;
	}

	public void setWaterConservationOther(String waterConservationOther) {
		this.waterConservationOther = waterConservationOther;
	}

	public Set<FarmerLandDetails> getFarmerLandDetails() {
		return farmerLandDetails;
	}

	public void setFarmerLandDetails(Set<FarmerLandDetails> farmerLandDetails) {
		this.farmerLandDetails = farmerLandDetails;
	}

	public String getFaimingtype() {
		return faimingtype;
	}

	public void setFaimingtype(String faimingtype) {
		this.faimingtype = faimingtype;
	}

	public String getFarmtype() {
		return farmtype;
	}

	public void setFarmtype(String farmtype) {
		this.farmtype = farmtype;
	}

	public Double getIrrigatedLand() {
		return irrigatedLand;
	}

	public void setIrrigatedLand(Double irrigatedLand) {
		this.irrigatedLand = irrigatedLand;
	}

	public String getIrrigatedFarmingLand() {
		return irrigatedFarmingLand;
	}

	public void setIrrigatedFarmingLand(String irrigatedFarmingLand) {
		this.irrigatedFarmingLand = irrigatedFarmingLand;
	}

	public String getFarmtype1() {
		return farmtype1;
	}

	public void setFarmtype1(String farmtype1) {
		this.farmtype1 = farmtype1;
	}

	public Double getFedtotaland() {
		return fedtotaland;
	}

	public void setFedtotaland(Double fedtotaland) {
		this.fedtotaland = fedtotaland;
	}

	public String getFedtotalics() {
		return fedtotalics;
	}

	public void setFedtotalics(String fedtotalics) {
		this.fedtotalics = fedtotalics;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public String getTrainingProgramOther() {
		return trainingProgramOther;
	}

	public void setTrainingProgramOther(String trainingProgramOther) {
		this.trainingProgramOther = trainingProgramOther;
	}

	public int getCertYear() {
		return certYear;
	}

	public void setCertYear(int certYear) {
		this.certYear = certYear;
	}

	public Set<Expenditure> getExpenditures() {
		return expenditures;
	}

	public void setExpenditures(Set<Expenditure> expenditures) {
		this.expenditures = expenditures;
	}

    public String getLandTopology() {
    
        return landTopology;
    }

    public void setLandTopology(String landTopology) {
    
        this.landTopology = landTopology;
    }

    public Set<DocumentUpload> getDocUpload() {
    
        return docUpload;
    }

    public void setDocUpload(Set<DocumentUpload> docUpload) {
    
        this.docUpload = docUpload;
    }

    public List<DocumentUpload> getDocUploadList() {
    
        return docUploadList;
    }

    public void setDocUploadList(List<DocumentUpload> docUploadList) {
    
        this.docUploadList = docUploadList;
    }

	public String getFarmOther() {
		return farmOther;
	}

	public void setFarmOther(String farmOther) {
		this.farmOther = farmOther;
	}

    public Set<FarmIcsConversion> getFarmICSConversion() {
    
        return farmICSConversion;
    }

    public void setFarmICSConversion(Set<FarmIcsConversion> farmICSConversion) {
    
        this.farmICSConversion = farmICSConversion;
    }

    public String getFarmRegNumber() {
    
        return farmRegNumber;
    }

    public void setFarmRegNumber(String farmRegNumber) {
    
        this.farmRegNumber = farmRegNumber;
    }

    public FarmIcsConversion getFarmIcsConv() {
    
        return farmIcsConv;
    }

    public void setFarmIcsConv(FarmIcsConversion farmIcsConv) {
    
        this.farmIcsConv = farmIcsConv;
    }

    public String getFarmId() {
    
        return farmId;
    }

    public void setFarmId(String farmId) {
    
        this.farmId = farmId;
    }

	public Set<Cow> getCows() {
		return cows;
	}

	public void setCows(Set<Cow> cows) {
		this.cows = cows;
	}

	public Set<HousingInfo> getHousingInfos() {
		return housingInfos;
	}

	public void setHousingInfos(Set<HousingInfo> housingInfos) {
		this.housingInfos = housingInfos;
	}

	public String getFarmerCode() {
		return farmerCode;
	}

	public void setFarmerCode(String farmerCode) {
		this.farmerCode = farmerCode;
	}

    public Village getVillage() {
    
        return village;
    }

    public void setVillage(Village village) {
    
        this.village = village;
    }

    public String getTenantId() {
    
        return tenantId;
    }

    public void setTenantId(String tenantId) {
    
        this.tenantId = tenantId;
    }

    public Warehouse getSamithi() {
    
        return samithi;
    }

    public void setSamithi(Warehouse samithi) {
    
        this.samithi = samithi;
    }

    public String getFpo() {
    
        return fpo;
    }

    public void setFpo(String fpo) {
    
        this.fpo = fpo;
    }

	public String getOwnLand() {
		return ownLand;
	}

	public void setOwnLand(String ownLand) {
		this.ownLand = ownLand;
	}

	public String getLeasedLand() {
		return leasedLand;
	}

	public void setLeasedLand(String leasedLand) {
		this.leasedLand = leasedLand;
	}

	public String getIrrigationLand() {
		return irrigationLand;
	}

	public void setIrrigationLand(String irrigationLand) {
		this.irrigationLand = irrigationLand;
	}

	public String getCropCode() {
		return cropCode;
	}

	public void setCropCode(String cropCode) {
		this.cropCode = cropCode;
	}

	public String getFarmPlatNo() {
		return farmPlatNo;
	}

	public void setFarmPlatNo(String farmPlatNo) {
		this.farmPlatNo = farmPlatNo;
	}

	public String getAgriIncome() {
		return agriIncome;
	}

	public void setAgriIncome(String agriIncome) {
		this.agriIncome = agriIncome;
	}

	public String getCottonIncome() {
		return cottonIncome;
	}

	public void setCottonIncome(String cottonIncome) {
		this.cottonIncome = cottonIncome;
	}

	public String getGrossAgriIncome() {
		return grossAgriIncome;
	}

	public void setGrossAgriIncome(String grossAgriIncome) {
		this.grossAgriIncome = grossAgriIncome;
	}

	public String getInterCropIncome() {
		return interCropIncome;
	}

	public void setInterCropIncome(String interCropIncome) {
		this.interCropIncome = interCropIncome;
	}

	public String getOtherIncome() {
		return otherIncome;
	}

	public void setOtherIncome(String otherIncome) {
		this.otherIncome = otherIncome;
	}

	public String getCultivationId() {
		return cultivationId;
	}

	public void setCultivationId(String cultivationId) {
		this.cultivationId = cultivationId;
	}

	public String getCtnQty() {
		return ctnQty;
	}

	public void setCtnQty(String ctnQty) {
		this.ctnQty = ctnQty;
	}

	public String getCtnSale() {
		return ctnSale;
	}

	public void setCtnSale(String ctnSale) {
		this.ctnSale = ctnSale;
	}

	public String getAvgStore() {
		return avgStore;
	}

	public void setAvgStore(String avgStore) {
		this.avgStore = avgStore;
	}

	public String getTreeName() {
		return treeName;
	}

	public void setTreeName(String treeName) {
		this.treeName = treeName;
	}

	public String getWaterHarvest() {
		return waterHarvest;
	}

	public void setWaterHarvest(String waterHarvest) {
		this.waterHarvest = waterHarvest;
	}

	public long getRevisionNo() {
		return revisionNo;
	}

	public void setRevisionNo(long revisionNo) {
		this.revisionNo = revisionNo;
	}

	public String getDistanceProcessingUnit() {
		return distanceProcessingUnit;
	}

	public void setDistanceProcessingUnit(String distanceProcessingUnit) {
		this.distanceProcessingUnit = distanceProcessingUnit;
	}

	public String getOrganicStatus() {
		return organicStatus;
	}

	public void setOrganicStatus(String organicStatus) {
		this.organicStatus = organicStatus;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPresenceBananaTrees() {
		return presenceBananaTrees;
	}

	public void setPresenceBananaTrees(String presenceBananaTrees) {
		this.presenceBananaTrees = presenceBananaTrees;
	}

	public String getParallelProd() {
		return parallelProd;
	}

	public void setParallelProd(String parallelProd) {
		this.parallelProd = parallelProd;
	}

	public String getInputOrganicUnit() {
		return inputOrganicUnit;
	}

	public void setInputOrganicUnit(String inputOrganicUnit) {
		this.inputOrganicUnit = inputOrganicUnit;
	}

	public String getPresenceHiredLabour() {
		return presenceHiredLabour;
	}

	public void setPresenceHiredLabour(String presenceHiredLabour) {
		this.presenceHiredLabour = presenceHiredLabour;
	}

	public String getRiskCategory() {
		return riskCategory;
	}

	public void setRiskCategory(String riskCategory) {
		this.riskCategory = riskCategory;
	}

	public Set<TreeDetail> getTreeDetails() {
		return treeDetails;
	}

	public void setTreeDetails(Set<TreeDetail> treeDetails) {
		this.treeDetails = treeDetails;
	}

	public Double getTotalOrganicTrees() {
		return totalOrganicTrees;
	}

	public void setTotalOrganicTrees(Double totalOrganicTrees) {
		this.totalOrganicTrees = totalOrganicTrees;
	}

	public Double getTotalConventionalTrees() {
		return totalConventionalTrees;
	}

	public void setTotalConventionalTrees(Double totalConventionalTrees) {
		this.totalConventionalTrees = totalConventionalTrees;
	}

	public Double getTotalAvocadoTrees() {
		return totalAvocadoTrees;
	}

	public void setTotalAvocadoTrees(Double totalAvocadoTrees) {
		this.totalAvocadoTrees = totalAvocadoTrees;
	}

	public Double getHectarOrganicTrees() {
		return hectarOrganicTrees;
	}

	public void setHectarOrganicTrees(Double hectarOrganicTrees) {
		this.hectarOrganicTrees = hectarOrganicTrees;
	}

	public Double getHectarConventionalTrees() {
		return hectarConventionalTrees;
	}

	public void setHectarConventionalTrees(Double hectarConventionalTrees) {
		this.hectarConventionalTrees = hectarConventionalTrees;
	}

	public Double getHectarAvocadoTrees() {
		return hectarAvocadoTrees;
	}

	public void setHectarAvocadoTrees(Double hectarAvocadoTrees) {
		this.hectarAvocadoTrees = hectarAvocadoTrees;
	}

	public Date getPlotCapturingTime() {
		return plotCapturingTime;
	}

	public void setPlotCapturingTime(Date plotCapturingTime) {
		this.plotCapturingTime = plotCapturingTime;
	}

	public String getOrganicVariety() {
		return organicVariety;
	}

	public void setOrganicVariety(String organicVariety) {
		this.organicVariety = organicVariety;
	}

	public String getConventionalVariety() {
		return conventionalVariety;
	}

	public void setConventionalVariety(String conventionalVariety) {
		this.conventionalVariety = conventionalVariety;
	}

	public String getPhotoId() {
		return photoId;
	}

	public void setPhotoId(String photoId) {
		this.photoId = photoId;
	}

	public Set<CoordinatesMap> getCoordinatesMap() {
		return coordinatesMap;
	}

	public void setCoordinatesMap(Set<CoordinatesMap> coordinatesMap) {
		this.coordinatesMap = coordinatesMap;
	}

	public CoordinatesMap getActiveCoordinates() {
		CoordinatesMap coMap= this.coordinatesMap!=null && !ObjectUtil.isListEmpty(this.coordinatesMap) && this.coordinatesMap.size()>0?this.coordinatesMap.stream().filter(f-> f.getStatus()==1).findFirst().orElse(null):this.activeCoordinates;
		return coMap;
	}

	public void setActiveCoordinates(CoordinatesMap activeCoordinates) {
		this.activeCoordinates = activeCoordinates;
	}

	public int getPlottingStatus() {
		return plottingStatus;
	}

	public void setPlottingStatus(int plottingStatus) {
		this.plottingStatus = plottingStatus;
	}

	public int getCertificateStandardLevel() {
		return certificateStandardLevel;
	}

	public void setCertificateStandardLevel(int certificateStandardLevel) {
		this.certificateStandardLevel = certificateStandardLevel;
	}

	public int getActStatus() {
		return actStatus;
	}

	public void setActStatus(int actStatus) {
		this.actStatus = actStatus;
	}

	public List<JSONObject> getFarmJsonObjectList() {
		return farmJsonObjectList;
	}

	public void setFarmJsonObjectList(List<JSONObject> farmJsonObjectList) {
		this.farmJsonObjectList = farmJsonObjectList;
	}

	public String getCreatedUsername() {
		return createdUsername;
	}

	public void setCreatedUsername(String createdUsername) {
		this.createdUsername = createdUsername;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastUpdatedUsername() {
		return lastUpdatedUsername;
	}

	public void setLastUpdatedUsername(String lastUpdatedUsername) {
		this.lastUpdatedUsername = lastUpdatedUsername;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getGcParcelID() {
		return gcParcelID;
	}

	public void setGcParcelID(String gcParcelID) {
		this.gcParcelID = gcParcelID;
	}

	public String getCropTimeline() {
		return cropTimeline;
	}

	public void setCropTimeline(String cropTimeline) {
		this.cropTimeline = cropTimeline;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

    
    
    
    
}
