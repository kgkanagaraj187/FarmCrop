/*
 * Farmer.java
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

import javax.validation.constraints.NotNull;

import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.inspect.agrocert.CertificateStandard;
import com.sourcetrace.eses.order.entity.txn.Contract;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.entity.ImageInfo;

// TODO: Auto-generated Javadoc
/**
 * The Class Farmer.
 */
public class Farmer {

	public static enum Status {
		INACTIVE, ACTIVE, DELETED,WITHDRAWN,SUSPENDED,VIOLATED
	}
	public static enum EnrollmentPlaceMaster {
		NA, FARMER_PLACE, RECEIVING_SATION, WAREHOUSE, OFFICE, OTHER
	}

	public static final String SEX_MALE = "MALE";
	public static final String SEX_FEMALE = "FEMALE";
	public static final String SEX_MALE_FEMALE = "MALE/FEMALE";
	public static final int FARMER_SEQ_RANGE = 1000;
	public static final int FARMER_RESERVE_INDEX = 100;
	public static final int FARMER_ID_LENGTH = 6;
	public static final int FARMER_ID_MAX_RANGE = 999999;

	public static final int CERTIFICATION_TYPE_NONE = 0;
	public static final int CERTIFICATION_TYPE_INDIVIDUAL = 1;
	public static final int CERTIFICATION_TYPE_GROUP = 2;

	public static final int CERTIFIED_YES = 1;
	public static final int CERTIFIED_NO = 0;

	public static final String AGRICULTURE_ACTIVITIES = "AGRICULTURE ACTIVITIES";
	public static final String HORTICULTURE = "HORTICULTURE";
	public static final String ALLIED_SECTOR = "ALLIED SECTOR";
	public static final String EMPLOYMENT = "EMPLOYMENT";
	public static final String OTHERS = "INCOME FROM SOCIAL BENEFITS";

	public static final String TABLE_ID = "1";

	public static final String DISABLED_YES = "1";
	public static final String DISABLED_NO = "2";

	public static final String IRP = "2";
	public static final String FARMER = "1";
	
	public static final int PLOTTED_YES = 1;
	public static final int PLOTTED_NO = 0;
	
	public static final double FARM_SIZE_LESS_THAN_ONE = 2;
	public static final double FARM_SIZE_GREATER_THAN_ONE = 1;
	public static final double NO_FARM = 3;
	
	private long id;
	private String farmerId;
	private String firstName;
	private String lastName;
	private String surName;
	private String gender;
	private String farmerCode;
	private String address;
	private String pinCode;
	private String postOffice;
	private Date enrollDate;
	private String email;
	private String mobileNumber;
	private String phoneNumber;
	private String latitude;
	private String longitude;
	private String icsName;
	private String icsCode;
	private Integer age;
	private Date dateOfBirth;
	private Date dateOfJoining;
	private Date photoCaptureTime;

	private long revisionNo;
	private int noOfFamilyMembers;
	private int status;
	private int certificateStandardLevel;
	private int certificationType;
	private String education;
	private String maritalSatus;
	private int childCountonSite;
	private int childCountonSitePrimary;
	private int childCountonSiteSecondary;
	private int inspectionType;
	private int icsStatus;

	private Village village;
	private Municipality city;
	private ImageInfo imageInfo;
	private Warehouse samithi;
	private CertificateStandard certificateStandard;
	private FarmerEconomy farmerEconomy;
	private CustomerProject customerProject;

	private Integer enrollmentPlace;
	private String enrollmentPlaceOther;

	private Set<Contract> contracts;
	private Set<Farm> farms;
	private Set<FarmerFamily> farmerFamilies;
	private Set<BankInformation> bankInfo;
	private Set<FarmerSourceIncome> farmerSourceIncome;

	private int statusCode;
	private String statusMsg;

	private String traceId;
	private String fatherHusbandName;
	private String policeStation;

	private String adultCountMale;
	private String adultCountFemale;
	private String childCountMale;
	private String childCountFemale;
	private String annualIncome;
	private int isCertifiedFarmer;
	private Integer isVerified;
	private Date verifiedDate;
	private String verifiedAgentId;
	private String verifiedAgentName;
	private String consumerElectronics;
	private String vehicle;
	private String cellPhone;
	private Integer smartPhone;
	private String consumerElectronicsOther;
	private String vehicleOther;

	private String Agriculture;
	private String OtherSource;
	private String Total;
	private int loanTakenLastYear;
	private String loanTakenFrom;
	private Integer noOfHouseHoldMem;
	private Integer noOfSchoolChildMale;
	private Integer noOfSchoolChildFemale;
	private Integer maleCnt;
	private Integer femaleCnt;
	private String branchId;

	private String createdUsername;
	private Date createdDate;
	private String lastUpdatedUsername;
	private Date lastUpdatedDate;
	private String fpo;
	private String category;
	private Integer personalStatus;
	private String isBeneficiaryInGovScheme;
	private String nameOfTheScheme;
	private String otherLoanTakenFrom;
	private String loanAmount;
	private String loanPupose;
	private String loanIntRate;
	private String loanSecurity;
	private String icsUnitNo;
	private String icsTracenetRegNo;
	private String farmerCodeByIcs;
	private String farmersCodeTracenet;
	private String seasonCode;
	private String adhaarNo;
	private String panCardNo;
	private String govtDept;
	private String lifeInsurance;
	private String healthInsurance;
	private Integer headOfFamily;
	private String farmerCropInsurance;
	private String acresInsured;
	private String lifeInsAmount;
	private String healthInsAmount;
	private String isCropInsured;
	private String lifInsCmpName;
	private String healthInsCmpName;
	private String crpInsCmpName;

	private Long refId;
	private Set<FarmInventory> farmInventory;
	private Set<AnimalHusbandary> animalHusbandary;
	private String msgNo;

	private byte[] fingerPrint;
	private byte[] idProofImg;
	private byte[] digitalSign;
    
	// Apmas Changes

	private String socialCategory;
	private String religion;
	private String religionOther;
	private String typeOfFamily;
	private String houseHoldLandWet;
	private String houseHoldLandDry;
	private String houseOccupationPrimary;
	private String houseOccupationSecondary;
	private String houseOccupationPriOther;
	private String houseOccupationSecOther;
	private String familyMember;
	private String familyMemberOther;
	private String investigatorName;
	private Date investigatorDate;
	private String investigatorOpinion;
	private String sangham;
	private Double totalSourceIncome;
	private String loanPuposeOther;
	private String loanIntPeriod;
	private String loanSecurityOther;
	private String casteName;
	private int grsMember;
	private int paidShareCapitial;
	private int utzStatus;

	// Pratibha Syntex
	private int basicInfo;

	// chetna fields
	private String loanRepaymentAmount;
	private Date loanRepaymentDate;

	// pgss
	private String isLoanTakenScheme;
	private String loanTakenScheme;
	private String isDisable;
	// transient variable
	private String filterStatus;
	private String searchPage;
	private String name;
	private String jsonString;
	private double accountBalance;
	private String certificationFilter;
	private String positionGroup;
	private String idProof;
	private String proofNo;
	private String otherIdProof;
	private List<String> branchesList;
	private String stateName;
	private String icsType;
	private String validateDynamicFields;
	private File farmerImage;
	private String startDate;
	private String endDate;
		
	private Set<FarmerSelfAsses> farmerSelfAsses;
	private Set<FarmerHealthAsses> farmerHealthAsses;

	private String homeDifficulty;
	private String communitiyDifficulty;
	private String workDiffficulty;
	private String assistiveDeivce;
	private String assistiveDeivceName;
	private String assistiveDeviceReq;
	private String healthIssue;
	private String healthIssueDescribe;
	private String prefWrk;

	private String formFilledBy;
	private String assess;
	private String placeOfAsss;
	private String objective;
	private Double farmSize;
	private String farmNames;
	private Integer isPlotting;
	private long stateId;
	private String totalAcreage;
	private String totalFarm;
	// canda
	private String yearOfICS;
	private String agricultureImplements;
	private String totalHsldLabel;
	private int existingFarmerFlag;
	// pgss
	private String typez;

	// chetna
	private Integer shg;

	// Wilmar
	private String masterData;
	private String organicStatus;

	//NSWITCH
	private Set<DistributionBalance> distributionBalance;
	
	//OCP
	private String cropNames;
	
	
	// transient variable
	private String insyear;
	private String certificationStandardLevel;
	private String totalCultivatable;
	private String cityName;
	private String villageName;
	private String farmer_status;
	private String farmer_fpo;
	private String selectedFarmerIds = "";

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {

		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(long id) {

		this.id = id;
	}

	/**
	 * Gets the farmer id.
	 * 
	 * @return the farmer id
	 */
	// @Pattern(regex = "[^\\p{Punct}]+$", message = "pattern.farmerId")
	// @NotEmpty(message = "empty.farmerId")
	public String getFarmerId() {

		return farmerId;
	}

	/**
	 * Sets the farmer id.
	 * 
	 * @param farmerId
	 *            the new farmer id
	 */
	public void setFarmerId(String farmerId) {

		this.farmerId = farmerId;
	}

	/**
	 * Gets the date of birth.
	 * 
	 * @return the date of birth
	 */
	public Date getDateOfBirth() {

		return dateOfBirth;
	}

	/**
	 * Sets the date of birth.
	 * 
	 * @param dateOfBirth
	 *            the new date of birth
	 */
	public void setDateOfBirth(Date dateOfBirth) {

		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * Gets the first name.
	 * 
	 * @return the first name
	 */
	// @Pattern(regex = "[^\\p{Punct}]+$", message = "pattern.firstName")
	// @NotEmpty(message = "empty.firstName")
	public String getFirstName() {

		return firstName;
	}

	/**
	 * Sets the first name.
	 * 
	 * @param firstName
	 *            the new first name
	 */
	public void setFirstName(String firstName) {

		this.firstName = firstName;
	}

	/**
	 * Gets the last name.
	 * 
	 * @return the last name
	 */
	// @Pattern(regex = "[^\\p{Punct}]*$", message = "pattern.lastName")
	// @NotEmpty(message = "empty.lastName")
	public String getLastName() {

		return lastName;
	}

	/**
	 * Sets the last name.
	 * 
	 * @param lastName
	 *            the new last name
	 */
	public void setLastName(String lastName) {

		this.lastName = lastName;
	}

	/**
	 * Gets the gender.
	 * 
	 * @return the gender
	 */
	// @NotEmpty(message = "empty.gender")
	public String getGender() {

		return gender;
	}

	/**
	 * Sets the gender.
	 * 
	 * @param gender
	 *            the new gender
	 */
	public void setGender(String gender) {

		this.gender = gender;
	}

	/**
	 * Sets the city.
	 * 
	 * @param city
	 *            the new city
	 */
	public void setCity(Municipality city) {

		this.city = city;
	}

	/**
	 * Gets the city.
	 * 
	 * @return the city
	 */
	public Municipality getCity() {

		return city;
	}

	/**
	 * Gets the image info.
	 * 
	 * @return the image info
	 */
	public ImageInfo getImageInfo() {

		return imageInfo;
	}

	/**
	 * Sets the image info.
	 * 
	 * @param imageInfo
	 *            the new image info
	 */
	public void setImageInfo(ImageInfo imageInfo) {

		this.imageInfo = imageInfo;
	}

	/**
	 * Gets the address.
	 * 
	 * @return the address
	 */
	// @Pattern(regexp = "[^@#!$%^&*_]+$", message = "pattern.address")
	// @NotEmpty(message = "empty.address")
	public String getAddress() {

		return address;
	}

	/**
	 * Sets the address.
	 * 
	 * @param address
	 *            the new address
	 */
	public void setAddress(String address) {

		this.address = address;
	}

	/**
	 * Gets the email.
	 * 
	 * @return the email
	 */
	// @Email(message = "pattern.email")
	public String getEmail() {

		return email;
	}

	/**
	 * Sets the email.
	 * 
	 * @param email
	 *            the new email
	 */
	public void setEmail(String email) {

		this.email = email;
	}

	/**
	 * Gets the mobile number.
	 * 
	 * @return the mobile number
	 */
	// @Pattern(regex = "[0-9]*", message = "pattern.mobileNumber")
	// @NotEmpty(message = "empty.mobileNumber")
	public String getMobileNumber() {

		return mobileNumber;
	}

	/**
	 * Sets the mobile number.
	 * 
	 * @param mobileNumber
	 *            the new mobile number
	 */
	public void setMobileNumber(String mobileNumber) {

		this.mobileNumber = mobileNumber;
	}

	/**
	 * Gets the phone number.
	 * 
	 * @return the phone number
	 */
	// @Pattern(regex = "[0-9]*", message = "pattern.phoneNumber")
	public String getPhoneNumber() {

		return phoneNumber;
	}

	/**
	 * Sets the phone number.
	 * 
	 * @param phoneNumber
	 *            the new phone number
	 */
	public void setPhoneNumber(String phoneNumber) {

		this.phoneNumber = phoneNumber;
	}

	/**
	 * Gets the pin code.
	 * 
	 * @return the pin code
	 */
	// @Pattern(regex = "[0-9]+$", message = "pattern.pinCode")
	// @NotEmpty(message = "empty.pinCode")
	public String getPinCode() {

		return pinCode;
	}

	/**
	 * Sets the pin code.
	 * 
	 * @param pinCode
	 *            the new pin code
	 */
	public void setPinCode(String pinCode) {

		this.pinCode = pinCode;
	}

	/**
	 * Gets the no of family members.
	 * 
	 * @return the no of family members
	 */
	public int getNoOfFamilyMembers() {

		return noOfFamilyMembers;
	}

	/**
	 * Sets the no of family members.
	 * 
	 * @param noOfFamilyMembers
	 *            the new no of family members
	 */
	public void setNoOfFamilyMembers(int noOfFamilyMembers) {

		this.noOfFamilyMembers = noOfFamilyMembers;
	}

	/**
	 * Sets the farms.
	 * 
	 * @param farms
	 *            the new farms
	 */
	public void setFarms(Set<Farm> farms) {

		this.farms = farms;
	}

	/**
	 * Sets the revision no.
	 * 
	 * @param revisionNo
	 *            the new revision no
	 */
	public void setRevisionNo(long revisionNo) {

		this.revisionNo = revisionNo;
	}

	/**
	 * Gets the revision no.
	 * 
	 * @return the revision no
	 */
	public long getRevisionNo() {

		return revisionNo;
	}

	/**
	 * Gets the farms.
	 * 
	 * @return the farms
	 */
	public Set<Farm> getFarms() {

		return farms;
	}

	/**
	 * Sets the village.
	 * 
	 * @param village
	 *            the new village
	 */
	public void setVillage(Village village) {

		this.village = village;
	}

	/**
	 * Gets the village.
	 * 
	 * @return the village
	 */
	@NotNull(message = "empty.village")
	public Village getVillage() {

		return village;
	}

	/**
	 * Gets the latitude.
	 * 
	 * @return the latitude
	 */
	// @Length(max = Municipality.MAX_LENGTH_LANANDLOG, message =
	// "length.latitude")
	// @Pattern(regex = Municipality.EXPRESSION_LATITUDE, message =
	// "pattern.latitude")
	/* @NotEmpty(message = "empty.latitude") */
	public String getLatitude() {

		return latitude;
	}

	/**
	 * Sets the latitude.
	 * 
	 * @param latitude
	 *            the new latitude
	 */
	public void setLatitude(String latitude) {

		this.latitude = latitude;
	}

	/**
	 * Gets the longitude.
	 * 
	 * @return the longitude
	 */
	// @Length(max = Municipality.MAX_LENGTH_LANANDLOG, message =
	// "length.longitude")
	// @Pattern(regex = Municipality.EXPRESSION_LONGITUDE, message =
	// "pattern.longitude")
	/* @NotEmpty(message = "empty.longitude") */
	public String getLongitude() {

		return longitude;
	}

	/**
	 * Sets the longitude.
	 * 
	 * @param longitude
	 *            the new longitude
	 */
	public void setLongitude(String longitude) {

		this.longitude = longitude;
	}

	/**
	 * Gets the photo capture time.
	 * 
	 * @return the photo capture time
	 */
	public Date getPhotoCaptureTime() {

		return photoCaptureTime;
	}

	/**
	 * Sets the photo capture time.
	 * 
	 * @param photoCaptureTime
	 *            the new photo capture time
	 */
	public void setPhotoCaptureTime(Date photoCaptureTime) {

		this.photoCaptureTime = photoCaptureTime;
	}

	/**
	 * Gets the contracts.
	 * 
	 * @return the contracts
	 */
	public Set<Contract> getContracts() {

		return contracts;
	}

	/**
	 * Sets the contracts.
	 * 
	 * @param contracts
	 *            the new contracts
	 */
	public void setContracts(Set<Contract> contracts) {

		this.contracts = contracts;
	}

	/**
	 * Gets the status.
	 * 
	 * @return the status
	 */
	public int getStatus() {

		return status;
	}

	/**
	 * Sets the status.
	 * 
	 * @param status
	 *            the new status
	 */
	public void setStatus(int status) {

		this.status = status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return firstName;
	}

	/**
	 * Gets the filter status.
	 * 
	 * @return the filter status
	 */
	public String getFilterStatus() {

		return filterStatus;
	}

	/**
	 * Sets the filter status.
	 * 
	 * @param filterStatus
	 *            the new filter status
	 */
	public void setFilterStatus(String filterStatus) {

		this.filterStatus = filterStatus;
	}

	/**
	 * Gets the samithi.
	 * 
	 * @return the samithi
	 */
	public Warehouse getSamithi() {

		return samithi;
	}

	/**
	 * Sets the samithi.
	 * 
	 * @param samithi
	 *            the new samithi
	 */
	public void setSamithi(Warehouse samithi) {

		this.samithi = samithi;
	}

	/**
	 * Sets the post office.
	 * 
	 * @param postOffice
	 *            the new post office
	 */
	public void setPostOffice(String postOffice) {

		this.postOffice = postOffice;
	}

	/**
	 * Gets the post office.
	 * 
	 * @return the post office
	 */
	// @Pattern(regex = "[^@#!$%^&*_]+$", message = "pattern.postOffice")
	public String getPostOffice() {

		return postOffice;
	}

	/**
	 * Sets the date of joining.
	 * 
	 * @param dateOfJoining
	 *            the new date of joining
	 */
	public void setDateOfJoining(Date dateOfJoining) {

		this.dateOfJoining = dateOfJoining;
	}

	/**
	 * Gets the date of joining.
	 * 
	 * @return the date of joining
	 */
	// @NotNull(message = "empty.dateOfJoining")
	public Date getDateOfJoining() {

		return dateOfJoining;
	}

	/**
	 * Sets the farmer code.
	 * 
	 * @param farmerCode
	 *            the new farmer code
	 */
	public void setFarmerCode(String farmerCode) {

		this.farmerCode = farmerCode;
	}

	/**
	 * Gets the farmer code.
	 * 
	 * @return the farmer code
	 */
	// @Pattern(regex = "[^@#!$%^&*_]+$", message = "pattern.farmerCode")
	public String getFarmerCode() {

		return farmerCode;
	}

	/**
	 * Gets the certificate standard.
	 * 
	 * @return the certificate standard
	 */
	public CertificateStandard getCertificateStandard() {

		return certificateStandard;
	}

	/**
	 * Sets the certificate standard.
	 * 
	 * @param certificateStandard
	 *            the new certificate standard
	 */
	public void setCertificateStandard(CertificateStandard certificateStandard) {

		this.certificateStandard = certificateStandard;
	}

	/**
	 * Gets the certificate standard level.
	 * 
	 * @return the certificate standard level
	 */
	public int getCertificateStandardLevel() {

		return certificateStandardLevel;
	}

	/**
	 * Sets the certificate standard level.
	 * 
	 * @param certificateStandardLevel
	 *            the new certificate standard level
	 */
	public void setCertificateStandardLevel(int certificateStandardLevel) {

		this.certificateStandardLevel = certificateStandardLevel;
	}

	/**
	 * Gets the education.
	 * 
	 * @return the education
	 */
	public String getEducation() {

		return education;
	}

	/**
	 * Sets the education.
	 * 
	 * @param education
	 *            the new education
	 */
	public void setEducation(String education) {

		this.education = education;
	}

	/**
	 * Gets the marital satus.
	 * 
	 * @return the marital satus
	 */
	public String getMaritalSatus() {

		return maritalSatus;
	}

	/**
	 * Sets the marital satus.
	 * 
	 * @param maritalSatus
	 *            the new marital satus
	 */
	public void setMaritalSatus(String maritalSatus) {

		this.maritalSatus = maritalSatus;
	}

	/**
	 * Gets the child counton site.
	 * 
	 * @return the child counton site
	 */
	public int getChildCountonSite() {

		return childCountonSite;
	}

	/**
	 * Sets the child counton site.
	 * 
	 * @param childCountonSite
	 *            the new child counton site
	 */
	public void setChildCountonSite(int childCountonSite) {

		this.childCountonSite = childCountonSite;
	}

	/**
	 * Gets the child counton site primary.
	 * 
	 * @return the child counton site primary
	 */
	public int getChildCountonSitePrimary() {

		return childCountonSitePrimary;
	}

	/**
	 * Sets the child counton site primary.
	 * 
	 * @param childCountonSitePrimary
	 *            the new child counton site primary
	 */
	public void setChildCountonSitePrimary(int childCountonSitePrimary) {

		this.childCountonSitePrimary = childCountonSitePrimary;
	}

	/**
	 * Gets the child counton site secondary.
	 * 
	 * @return the child counton site secondary
	 */
	public int getChildCountonSiteSecondary() {

		return childCountonSiteSecondary;
	}

	/**
	 * Sets the child counton site secondary.
	 * 
	 * @param childCountonSiteSecondary
	 *            the new child counton site secondary
	 */
	public void setChildCountonSiteSecondary(int childCountonSiteSecondary) {

		this.childCountonSiteSecondary = childCountonSiteSecondary;
	}

	/**
	 * Sets the farmer families.
	 * 
	 * @param farmerFamilies
	 *            the new farmer families
	 */
	public void setFarmerFamilies(Set<FarmerFamily> farmerFamilies) {

		this.farmerFamilies = farmerFamilies;
	}

	/**
	 * Gets the farmer families.
	 * 
	 * @return the farmer families
	 */
	public Set<FarmerFamily> getFarmerFamilies() {

		return farmerFamilies;
	}

	/**
	 * Sets the farmer economy.
	 * 
	 * @param farmerEconomy
	 *            the new farmer economy
	 */
	public void setFarmerEconomy(FarmerEconomy farmerEconomy) {

		this.farmerEconomy = farmerEconomy;
	}

	/**
	 * Gets the farmer economy.
	 * 
	 * @return the farmer economy
	 */
	public FarmerEconomy getFarmerEconomy() {

		return farmerEconomy;
	}

	/**
	 * Sets the customer project.
	 * 
	 * @param customerProject
	 *            the new customer project
	 */
	public void setCustomerProject(CustomerProject customerProject) {

		this.customerProject = customerProject;
	}

	/**
	 * Gets the customer project.
	 * 
	 * @return the customer project
	 */
	public CustomerProject getCustomerProject() {

		return customerProject;
	}

	/**
	 * Sets the certification type.
	 * 
	 * @param certificationType
	 *            the new certification type
	 */
	public void setCertificationType(int certificationType) {

		this.certificationType = certificationType;
	}

	/**
	 * Gets the certification type.
	 * 
	 * @return the certification type
	 */
	public int getCertificationType() {

		return certificationType;
	}

	/**
	 * Sets the inspection type.
	 * 
	 * @param inspectionType
	 *            the new inspection type
	 */
	public void setInspectionType(int inspectionType) {

		this.inspectionType = inspectionType;
	}

	/**
	 * Gets the inspection type.
	 * 
	 * @return the inspection type
	 */
	public int getInspectionType() {

		return inspectionType;
	}

	/**
	 * Sets the ics status.
	 * 
	 * @param icsStatus
	 *            the new ics status
	 */
	public void setIcsStatus(int icsStatus) {

		this.icsStatus = icsStatus;
	}

	/**
	 * Gets the ics status.
	 * 
	 * @return the ics status
	 */
	public int getIcsStatus() {

		return icsStatus;
	}

	/**
	 * Sets the status code.
	 * 
	 * @param statusCode
	 *            the new status code
	 */
	public void setStatusCode(int statusCode) {

		this.statusCode = statusCode;
	}

	/**
	 * Gets the status code.
	 * 
	 * @return the status code
	 */
	public int getStatusCode() {

		return statusCode;
	}

	/**
	 * Sets the status msg.
	 * 
	 * @param statusMsg
	 *            the new status msg
	 */
	public void setStatusMsg(String statusMsg) {

		this.statusMsg = statusMsg;
	}

	/**
	 * Gets the status msg.
	 * 
	 * @return the status msg
	 */
	public String getStatusMsg() {

		return statusMsg;
	}

	/**
	 * Sets the enrollment place.
	 * 
	 * @param enrollmentPlace
	 *            the new enrollment place
	 */
	public void setEnrollmentPlace(Integer enrollmentPlace) {

		this.enrollmentPlace = enrollmentPlace;
	}

	/**
	 * Gets the enrollment place.
	 * 
	 * @return the enrollment place
	 */
	public Integer getEnrollmentPlace() {

		return enrollmentPlace;
	}

	/**
	 * Sets the enrollment place other.
	 * 
	 * @param enrollmentPlaceOther
	 *            the new enrollment place other
	 */
	public void setEnrollmentPlaceOther(String enrollmentPlaceOther) {

		this.enrollmentPlaceOther = enrollmentPlaceOther;
	}

	/**
	 * Gets the enrollment place other.
	 * 
	 * @return the enrollment place other
	 */
	public String getEnrollmentPlaceOther() {

		return enrollmentPlaceOther;
	}

	/**
	 * Gets the trace id.
	 * 
	 * @return the trace id
	 */
	public String getTraceId() {

		return traceId;
	}

	/**
	 * Sets the trace id.
	 * 
	 * @param traceId
	 *            the new trace id
	 */
	public void setTraceId(String traceId) {

		this.traceId = traceId;
	}

	/**
	 * Gets the father husband name.
	 * 
	 * @return the father husband name
	 */
	public String getFatherHusbandName() {

		return fatherHusbandName;
	}

	/**
	 * Sets the father husband name.
	 * 
	 * @param fatherHusbandName
	 *            the new father husband name
	 */
	public void setFatherHusbandName(String fatherHusbandName) {

		this.fatherHusbandName = fatherHusbandName;
	}

	/**
	 * Gets the police station.
	 * 
	 * @return the police station
	 */
	public String getPoliceStation() {

		return policeStation;
	}

	/**
	 * Sets the police station.
	 * 
	 * @param policeStation
	 *            the new police station
	 */
	public void setPoliceStation(String policeStation) {

		this.policeStation = policeStation;
	}

	/**
	 * Gets the search page.
	 * 
	 * @return the search page
	 */
	public String getSearchPage() {

		return searchPage;
	}

	/**
	 * Sets the search page.
	 * 
	 * @param searchPage
	 *            the new search page
	 */
	public void setSearchPage(String searchPage) {

		this.searchPage = searchPage;
	}

	/**
	 * Gets the bank info.
	 * 
	 * @return the bank info
	 */
	public Set<BankInformation> getBankInfo() {

		return bankInfo;
	}

	/**
	 * Sets the bank info.
	 * 
	 * @param bankInfo
	 *            the new bank info
	 */
	public void setBankInfo(Set<BankInformation> bankInfo) {

		this.bankInfo = bankInfo;
	}

	/**
	 * Gets the json string.
	 * 
	 * @return the json string
	 */
	public String getJsonString() {

		return jsonString;
	}

	/**
	 * Sets the json string.
	 * 
	 * @param jsonString
	 *            the new json string
	 */
	public void setJsonString(String jsonString) {

		this.jsonString = jsonString;
	}

	/**
	 * Gets the annual income.
	 * 
	 * @return the annual income
	 */
	public String getAnnualIncome() {

		return annualIncome;
	}

	/**
	 * Sets the annual income.
	 * 
	 * @param annualIncome
	 *            the new annual income
	 */
	public void setAnnualIncome(String annualIncome) {

		this.annualIncome = annualIncome;
	}

	/**
	 * Gets the checks if is certified farmer.
	 * 
	 * @return the checks if is certified farmer
	 */
	public int getIsCertifiedFarmer() {

		return isCertifiedFarmer;
	}

	/**
	 * Sets the checks if is certified farmer.
	 * 
	 * @param isCertifiedFarmer
	 *            the new checks if is certified farmer
	 */
	public void setIsCertifiedFarmer(int isCertifiedFarmer) {

		this.isCertifiedFarmer = isCertifiedFarmer;
	}

	/**
	 * Gets the checks if is verified.
	 * 
	 * @return the checks if is verified
	 */
	public Integer getIsVerified() {

		return isVerified;
	}

	/**
	 * Sets the checks if is verified.
	 * 
	 * @param isVerified
	 *            the new checks if is verified
	 */
	public void setIsVerified(Integer isVerified) {

		this.isVerified = isVerified;
	}

	/**
	 * Gets the verified date.
	 * 
	 * @return the verified date
	 */
	public Date getVerifiedDate() {

		return verifiedDate;
	}

	/**
	 * Sets the verified date.
	 * 
	 * @param verifiedDate
	 *            the new verified date
	 */
	public void setVerifiedDate(Date verifiedDate) {

		this.verifiedDate = verifiedDate;
	}

	/**
	 * Gets the verified agent id.
	 * 
	 * @return the verified agent id
	 */
	public String getVerifiedAgentId() {

		return verifiedAgentId;
	}

	/**
	 * Sets the verified agent id.
	 * 
	 * @param verifiedAgentId
	 *            the new verified agent id
	 */
	public void setVerifiedAgentId(String verifiedAgentId) {

		this.verifiedAgentId = verifiedAgentId;
	}

	/**
	 * Gets the verified agent name.
	 * 
	 * @return the verified agent name
	 */
	public String getVerifiedAgentName() {

		return verifiedAgentName;
	}

	/**
	 * Sets the verified agent name.
	 * 
	 * @param verifiedAgentName
	 *            the new verified agent name
	 */
	public void setVerifiedAgentName(String verifiedAgentName) {

		this.verifiedAgentName = verifiedAgentName;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {

		StringBuilder builder = new StringBuilder();
		if (!StringUtil.isEmpty(this.firstName))
			builder.append(this.firstName);

		if (!StringUtil.isEmpty(this.lastName)) {
			builder.append(" ");
			builder.append(this.lastName);
		}
		return builder.toString();
	}

	/**
	 * Gets the farmer id and name.
	 * 
	 * @return the farmer id and name
	 */
	public String getFarmerIdAndName() {

		StringBuffer sb = new StringBuffer();
		if (!StringUtil.isEmpty(farmerId)) {
			sb.append(farmerId);
			if (!StringUtil.isEmpty(getName())) {
				sb.append(" - ");
			}
		}
		if (!StringUtil.isEmpty(getName())) {
			sb.append(getName());
		}
		return sb.toString();
	}

	public String getFarmerCodeAndName() {

		StringBuffer sb = new StringBuffer();
		if (!StringUtil.isEmpty(farmerCode)) {
			sb.append(farmerCode);
			if (!StringUtil.isEmpty(getName())) {
				sb.append(" - ");
			}
		}
		if (!StringUtil.isEmpty(getName())) {
			sb.append(getName());
		}
		return sb.toString();
	}

	/**
	 * Gets the consumer electronics.
	 * 
	 * @return the consumer electronics
	 */
	public String getConsumerElectronics() {

		return consumerElectronics;
	}

	/**
	 * Sets the consumer electronics.
	 * 
	 * @param consumerElectronics
	 *            the new consumer electronics
	 */
	public void setConsumerElectronics(String consumerElectronics) {

		this.consumerElectronics = consumerElectronics;
	}

	/**
	 * Gets the vehicle.
	 * 
	 * @return the vehicle
	 */
	public String getVehicle() {

		return vehicle;
	}

	/**
	 * Sets the vehicle.
	 * 
	 * @param vehicle
	 *            the new vehicle
	 */
	public void setVehicle(String vehicle) {

		this.vehicle = vehicle;
	}

	/**
	 * Gets the cell phone.
	 * 
	 * @return the cell phone
	 */
	public String getCellPhone() {

		return cellPhone;
	}

	/**
	 * Sets the cell phone.
	 * 
	 * @param cellPhone
	 *            the new cell phone
	 */
	public void setCellPhone(String cellPhone) {

		this.cellPhone = cellPhone;
	}

	/**
	 * Gets the consumer electronics other.
	 * 
	 * @return the consumer electronics other
	 */
	public String getConsumerElectronicsOther() {

		return consumerElectronicsOther;
	}

	/**
	 * Sets the consumer electronics other.
	 * 
	 * @param consumerElectronicsOther
	 *            the new consumer electronics other
	 */
	public void setConsumerElectronicsOther(String consumerElectronicsOther) {

		this.consumerElectronicsOther = consumerElectronicsOther;
	}

	/**
	 * Gets the vehicle other.
	 * 
	 * @return the vehicle other
	 */
	public String getVehicleOther() {

		return vehicleOther;
	}

	/**
	 * Sets the vehicle other.
	 * 
	 * @param vehicleOther
	 *            the new vehicle other
	 */
	public void setVehicleOther(String vehicleOther) {

		this.vehicleOther = vehicleOther;
	}

	/**
	 * Gets the agriculture.
	 * 
	 * @return the agriculture
	 */
	public String getAgriculture() {

		return Agriculture;
	}

	/**
	 * Sets the agriculture.
	 * 
	 * @param agriculture
	 *            the new agriculture
	 */
	public void setAgriculture(String agriculture) {

		Agriculture = agriculture;
	}

	/**
	 * Gets the other source.
	 * 
	 * @return the other source
	 */
	public String getOtherSource() {

		return OtherSource;
	}

	/**
	 * Sets the other source.
	 * 
	 * @param otherSource
	 *            the new other source
	 */
	public void setOtherSource(String otherSource) {

		OtherSource = otherSource;
	}

	/**
	 * Gets the total.
	 * 
	 * @return the total
	 */
	public String getTotal() {

		return Total;
	}

	/**
	 * Sets the total.
	 * 
	 * @param total
	 *            the new total
	 */
	public void setTotal(String total) {

		Total = total;
	}

	/**
	 * Gets the loan taken from.
	 * 
	 * @return the loan taken from
	 */
	public String getLoanTakenFrom() {

		return loanTakenFrom;
	}

	/**
	 * Sets the loan taken from.
	 * 
	 * @param loanTakenFrom
	 *            the new loan taken from
	 */
	public void setLoanTakenFrom(String loanTakenFrom) {

		this.loanTakenFrom = loanTakenFrom;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {

		this.name = name;
	}

	/**
	 * Gets the ics name.
	 * 
	 * @return the ics name
	 */
	public String getIcsName() {

		return icsName;
	}

	/**
	 * Sets the ics name.
	 * 
	 * @param icsName
	 *            the new ics name
	 */
	public void setIcsName(String icsName) {

		this.icsName = icsName;
	}

	/**
	 * Gets the ics code.
	 * 
	 * @return the ics code
	 */
	public String getIcsCode() {

		return icsCode;
	}

	/**
	 * Sets the ics code.
	 * 
	 * @param icsCode
	 *            the new ics code
	 */
	public void setIcsCode(String icsCode) {

		this.icsCode = icsCode;
	}

	/**
	 * Gets the enroll date.
	 * 
	 * @return the enroll date
	 */
	public Date getEnrollDate() {

		return enrollDate;
	}

	/**
	 * Sets the enroll date.
	 * 
	 * @param enrollDate
	 *            the new enroll date
	 */
	public void setEnrollDate(Date enrollDate) {

		this.enrollDate = enrollDate;
	}

	/**
	 * Gets the no of house hold mem.
	 * 
	 * @return the no of house hold mem
	 */
	public Integer getNoOfHouseHoldMem() {

		return noOfHouseHoldMem;
	}

	/**
	 * Sets the no of house hold mem.
	 * 
	 * @param noOfHouseHoldMem
	 *            the new no of house hold mem
	 */
	public void setNoOfHouseHoldMem(Integer noOfHouseHoldMem) {

		this.noOfHouseHoldMem = noOfHouseHoldMem;
	}

	/**
	 * Gets the male cnt.
	 * 
	 * @return the male cnt
	 */
	public Integer getMaleCnt() {

		return maleCnt;
	}

	/**
	 * Sets the male cnt.
	 * 
	 * @param maleCnt
	 *            the new male cnt
	 */
	public void setMaleCnt(Integer maleCnt) {

		this.maleCnt = maleCnt;
	}

	/**
	 * Gets the female cnt.
	 * 
	 * @return the female cnt
	 */
	public Integer getFemaleCnt() {

		return femaleCnt;
	}

	/**
	 * Sets the female cnt.
	 * 
	 * @param femaleCnt
	 *            the new female cnt
	 */
	public void setFemaleCnt(Integer femaleCnt) {

		this.femaleCnt = femaleCnt;
	}

	public int getLoanTakenLastYear() {

		return loanTakenLastYear;
	}

	public void setLoanTakenLastYear(int loanTakenLastYear) {

		this.loanTakenLastYear = loanTakenLastYear;
	}

	public Integer getAge() {

		return age;
	}

	public void setAge(Integer age) {

		this.age = age;
	}

	public String getBranchId() {

		return branchId;
	}

	public void setBranchId(String branchId) {

		this.branchId = branchId;
	}

	public String getCertificationFilter() {

		return certificationFilter;
	}

	public void setCertificationFilter(String certificationFilter) {

		this.certificationFilter = certificationFilter;
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

	public String getFpo() {

		return fpo;
	}

	public void setFpo(String fpo) {

		this.fpo = fpo;
	}

	public Integer getPersonalStatus() {

		return personalStatus;
	}

	public void setPersonalStatus(Integer personalStatus) {

		this.personalStatus = personalStatus;
	}

	public String getIsBeneficiaryInGovScheme() {

		return isBeneficiaryInGovScheme;
	}

	public void setIsBeneficiaryInGovScheme(String isBeneficiaryInGovScheme) {

		this.isBeneficiaryInGovScheme = isBeneficiaryInGovScheme;
	}

	public String getNameOfTheScheme() {

		return nameOfTheScheme;
	}

	public void setNameOfTheScheme(String nameOfTheScheme) {

		this.nameOfTheScheme = nameOfTheScheme;
	}

	public String getOtherLoanTakenFrom() {

		return otherLoanTakenFrom;
	}

	public void setOtherLoanTakenFrom(String otherLoanTakenFrom) {

		this.otherLoanTakenFrom = otherLoanTakenFrom;
	}

	public String getLoanAmount() {

		return loanAmount;
	}

	public void setLoanAmount(String loanAmount) {

		this.loanAmount = loanAmount;
	}

	public String getLoanPupose() {

		return loanPupose;
	}

	public void setLoanPupose(String loanPupose) {

		this.loanPupose = loanPupose;
	}

	public String getLoanIntRate() {

		return loanIntRate;
	}

	public void setLoanIntRate(String loanIntRate) {

		this.loanIntRate = loanIntRate;
	}

	public String getLoanSecurity() {

		return loanSecurity;
	}

	public void setLoanSecurity(String loanSecurity) {

		this.loanSecurity = loanSecurity;
	}

	public String getIcsUnitNo() {

		return icsUnitNo;
	}

	public void setIcsUnitNo(String icsUnitNo) {

		this.icsUnitNo = icsUnitNo;
	}

	public String getIcsTracenetRegNo() {

		return icsTracenetRegNo;
	}

	public void setIcsTracenetRegNo(String icsTracenetRegNo) {

		this.icsTracenetRegNo = icsTracenetRegNo;
	}

	public String getFarmerCodeByIcs() {

		return farmerCodeByIcs;
	}

	public void setFarmerCodeByIcs(String farmerCodeByIcs) {

		this.farmerCodeByIcs = farmerCodeByIcs;
	}

	public String getFarmersCodeTracenet() {

		return farmersCodeTracenet;
	}

	public void setFarmersCodeTracenet(String farmersCodeTracenet) {

		this.farmersCodeTracenet = farmersCodeTracenet;
	}

	public String getSurName() {

		return surName;
	}

	public void setSurName(String surName) {

		this.surName = surName;
	}

	public double getAccountBalance() {

		return accountBalance;
	}

	public void setAccountBalance(double accountBalance) {

		this.accountBalance = accountBalance;
	}

	public String getSeasonCode() {

		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {

		this.seasonCode = seasonCode;
	}

	public String getAdhaarNo() {
		return adhaarNo;
	}

	public void setAdhaarNo(String adhaarNo) {
		this.adhaarNo = adhaarNo;
	}

	public String getReligion() {
		return religion;
	}

	public void setReligion(String religion) {
		this.religion = religion;
	}

	public String getTypeOfFamily() {
		return typeOfFamily;
	}

	public void setTypeOfFamily(String typeOfFamily) {
		this.typeOfFamily = typeOfFamily;
	}

	public String getHouseOccupationPrimary() {
		return houseOccupationPrimary;
	}

	public void setHouseOccupationPrimary(String houseOccupationPrimary) {
		this.houseOccupationPrimary = houseOccupationPrimary;
	}

	public String getHouseOccupationSecondary() {
		return houseOccupationSecondary;
	}

	public void setHouseOccupationSecondary(String houseOccupationSecondary) {
		this.houseOccupationSecondary = houseOccupationSecondary;
	}

	public String getHouseOccupationPriOther() {
		return houseOccupationPriOther;
	}

	public void setHouseOccupationPriOther(String houseOccupationPriOther) {
		this.houseOccupationPriOther = houseOccupationPriOther;
	}

	public String getHouseOccupationSecOther() {
		return houseOccupationSecOther;
	}

	public void setHouseOccupationSecOther(String houseOccupationSecOther) {
		this.houseOccupationSecOther = houseOccupationSecOther;
	}

	public String getFamilyMember() {
		return familyMember;
	}

	public void setFamilyMember(String familyMember) {
		this.familyMember = familyMember;
	}

	public String getFamilyMemberOther() {
		return familyMemberOther;
	}

	public void setFamilyMemberOther(String familyMemberOther) {
		this.familyMemberOther = familyMemberOther;
	}

	public String getInvestigatorName() {
		return investigatorName;
	}

	public void setInvestigatorName(String investigatorName) {
		this.investigatorName = investigatorName;
	}

	public Date getInvestigatorDate() {
		return investigatorDate;
	}

	public void setInvestigatorDate(Date investigatorDate) {
		this.investigatorDate = investigatorDate;
	}

	public String getInvestigatorOpinion() {
		return investigatorOpinion;
	}

	public void setInvestigatorOpinion(String investigatorOpinion) {
		this.investigatorOpinion = investigatorOpinion;
	}

	public String getSocialCategory() {
		return socialCategory;
	}

	public void setSocialCategory(String socialCategory) {
		this.socialCategory = socialCategory;
	}

	public Set<FarmerSourceIncome> getFarmerSourceIncome() {
		return farmerSourceIncome;
	}

	public void setFarmerSourceIncome(Set<FarmerSourceIncome> farmerSourceIncome) {
		this.farmerSourceIncome = farmerSourceIncome;
	}

	public String getPositionGroup() {
		return positionGroup;
	}

	public void setPositionGroup(String positionGroup) {
		this.positionGroup = positionGroup;
	}

	public String getSangham() {

		return sangham;
	}

	public void setSangham(String sangham) {

		this.sangham = sangham;
	}

	public String getReligionOther() {
		return religionOther;
	}

	public void setReligionOther(String religionOther) {
		this.religionOther = religionOther;
	}

	public Double getTotalSourceIncome() {
		return totalSourceIncome;
	}

	public void setTotalSourceIncome(Double totalSourceIncome) {
		this.totalSourceIncome = totalSourceIncome;
	}

	public String getHouseHoldLandWet() {

		return houseHoldLandWet;
	}

	public void setHouseHoldLandWet(String houseHoldLandWet) {

		this.houseHoldLandWet = houseHoldLandWet;
	}

	public String getHouseHoldLandDry() {

		return houseHoldLandDry;
	}

	public void setHouseHoldLandDry(String houseHoldLandDry) {

		this.houseHoldLandDry = houseHoldLandDry;
	}

	public String getPanCardNo() {

		return panCardNo;
	}

	public void setPanCardNo(String panCardNo) {

		this.panCardNo = panCardNo;
	}

	public String getGovtDept() {

		return govtDept;
	}

	public void setGovtDept(String govtDept) {

		this.govtDept = govtDept;
	}

	public String getLoanPuposeOther() {
		return loanPuposeOther;
	}

	public void setLoanPuposeOther(String loanPuposeOther) {
		this.loanPuposeOther = loanPuposeOther;
	}

	public String getLoanIntPeriod() {
		return loanIntPeriod;
	}

	public void setLoanIntPeriod(String loanIntPeriod) {
		this.loanIntPeriod = loanIntPeriod;
	}

	public String getLoanSecurityOther() {
		return loanSecurityOther;
	}

	public void setLoanSecurityOther(String loanSecurityOther) {
		this.loanSecurityOther = loanSecurityOther;
	}

	public String getLifeInsurance() {
		return lifeInsurance;
	}

	public void setLifeInsurance(String lifeInsurance) {
		this.lifeInsurance = lifeInsurance;
	}

	public String getHealthInsurance() {
		return healthInsurance;
	}

	public void setHealthInsurance(String healthInsurance) {
		this.healthInsurance = healthInsurance;
	}

	public Integer getHeadOfFamily() {
		return headOfFamily;
	}

	public void setHeadOfFamily(Integer headOfFamily) {
		this.headOfFamily = headOfFamily;
	}

	public String getAcresInsured() {
		return acresInsured;
	}

	public void setAcresInsured(String acresInsured) {
		this.acresInsured = acresInsured;
	}

	public String getFarmerCropInsurance() {
		return farmerCropInsurance;
	}

	public void setFarmerCropInsurance(String farmerCropInsurance) {
		this.farmerCropInsurance = farmerCropInsurance;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public String getOtherIdProof() {
		return otherIdProof;
	}

	public void setOtherIdProof(String otherIdProof) {
		this.otherIdProof = otherIdProof;
	}

	public String getLifeInsAmount() {

		return lifeInsAmount;
	}

	public void setLifeInsAmount(String lifeInsAmount) {

		this.lifeInsAmount = lifeInsAmount;
	}

	public String getHealthInsAmount() {

		return healthInsAmount;
	}

	public void setHealthInsAmount(String healthInsAmount) {

		this.healthInsAmount = healthInsAmount;
	}

	public String getCasteName() {

		return casteName;
	}

	public void setCasteName(String casteName) {

		this.casteName = casteName;
	}

	public String getIsCropInsured() {

		return isCropInsured;
	}

	public void setIsCropInsured(String isCropInsured) {

		this.isCropInsured = isCropInsured;
	}

	public Integer getNoOfSchoolChildMale() {
		return noOfSchoolChildMale;
	}

	public void setNoOfSchoolChildMale(Integer noOfSchoolChildMale) {
		this.noOfSchoolChildMale = noOfSchoolChildMale;
	}

	public Integer getNoOfSchoolChildFemale() {
		return noOfSchoolChildFemale;
	}

	public void setNoOfSchoolChildFemale(Integer noOfSchoolChildFemale) {
		this.noOfSchoolChildFemale = noOfSchoolChildFemale;
	}

	public String getAdultCountMale() {
		return adultCountMale;
	}

	public void setAdultCountMale(String adultCountMale) {
		this.adultCountMale = adultCountMale;
	}

	public String getAdultCountFemale() {
		return adultCountFemale;
	}

	public void setAdultCountFemale(String adultCountFemale) {
		this.adultCountFemale = adultCountFemale;
	}

	public String getChildCountMale() {
		return childCountMale;
	}

	public void setChildCountMale(String childCountMale) {
		this.childCountMale = childCountMale;
	}

	public String getChildCountFemale() {
		return childCountFemale;
	}

	public void setChildCountFemale(String childCountFemale) {
		this.childCountFemale = childCountFemale;
	}

	public int getGrsMember() {
		return grsMember;
	}

	public void setGrsMember(int grsMember) {
		this.grsMember = grsMember;
	}

	public int getPaidShareCapitial() {
		return paidShareCapitial;
	}

	public void setPaidShareCapitial(int paidShareCapitial) {
		this.paidShareCapitial = paidShareCapitial;
	}

	public int getUtzStatus() {
		return utzStatus;
	}

	public void setUtzStatus(int utzStatus) {
		this.utzStatus = utzStatus;
	}

	public int getBasicInfo() {
		return basicInfo;
	}

	public void setBasicInfo(int basicInfo) {
		this.basicInfo = basicInfo;
	}

	public String getLoanRepaymentAmount() {

		return loanRepaymentAmount;
	}

	public void setLoanRepaymentAmount(String loanRepaymentAmount) {

		this.loanRepaymentAmount = loanRepaymentAmount;
	}

	public Date getLoanRepaymentDate() {

		return loanRepaymentDate;
	}

	public void setLoanRepaymentDate(Date loanRepaymentDate) {

		this.loanRepaymentDate = loanRepaymentDate;
	}

	public List<String> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getIcsType() {
		return icsType;
	}

	public void setIcsType(String icsType) {
		this.icsType = icsType;
	}

	

	public String getValidateDynamicFields() {
		return validateDynamicFields;
	}

	public void setValidateDynamicFields(String validateDynamicFields) {
		this.validateDynamicFields = validateDynamicFields;
	}

	public Set<FarmInventory> getFarmInventory() {
		return farmInventory;
	}

	public void setFarmInventory(Set<FarmInventory> farmInventory) {
		this.farmInventory = farmInventory;
	}

	public Set<AnimalHusbandary> getAnimalHusbandary() {
		return animalHusbandary;
	}

	public void setAnimalHusbandary(Set<AnimalHusbandary> animalHusbandary) {
		this.animalHusbandary = animalHusbandary;
	}

	public String getLoanTakenScheme() {
		return loanTakenScheme;
	}

	public void setLoanTakenScheme(String loanTakenScheme) {
		this.loanTakenScheme = loanTakenScheme;
	}

	public String getIsLoanTakenScheme() {
		return isLoanTakenScheme;
	}

	public void setIsLoanTakenScheme(String isLoanTakenScheme) {
		this.isLoanTakenScheme = isLoanTakenScheme;
	}

	public Set<FarmerSelfAsses> getFarmerSelfAsses() {
		return farmerSelfAsses;
	}

	public void setFarmerSelfAsses(Set<FarmerSelfAsses> farmerSelfAsses) {
		this.farmerSelfAsses = farmerSelfAsses;
	}

	public Set<FarmerHealthAsses> getFarmerHealthAsses() {
		return farmerHealthAsses;
	}

	public void setFarmerHealthAsses(Set<FarmerHealthAsses> farmerHealthAsses) {
		this.farmerHealthAsses = farmerHealthAsses;
	}

	public String getHomeDifficulty() {
		return homeDifficulty;
	}

	public void setHomeDifficulty(String homeDifficulty) {
		this.homeDifficulty = homeDifficulty;
	}

	public String getCommunitiyDifficulty() {
		return communitiyDifficulty;
	}

	public void setCommunitiyDifficulty(String communitiyDifficulty) {
		this.communitiyDifficulty = communitiyDifficulty;
	}

	public String getWorkDiffficulty() {
		return workDiffficulty;
	}

	public void setWorkDiffficulty(String workDiffficulty) {
		this.workDiffficulty = workDiffficulty;
	}

	public String getAssistiveDeivce() {
		return assistiveDeivce;
	}

	public void setAssistiveDeivce(String assistiveDeivce) {
		this.assistiveDeivce = assistiveDeivce;
	}

	public String getAssistiveDeivceName() {
		return assistiveDeivceName;
	}

	public void setAssistiveDeivceName(String assistiveDeivceName) {
		this.assistiveDeivceName = assistiveDeivceName;
	}

	public String getAssistiveDeviceReq() {
		return assistiveDeviceReq;
	}

	public void setAssistiveDeviceReq(String assistiveDeviceReq) {
		this.assistiveDeviceReq = assistiveDeviceReq;
	}

	public String getHealthIssue() {
		return healthIssue;
	}

	public void setHealthIssue(String healthIssue) {
		this.healthIssue = healthIssue;
	}

	public String getHealthIssueDescribe() {
		return healthIssueDescribe;
	}

	public void setHealthIssueDescribe(String healthIssueDescribe) {
		this.healthIssueDescribe = healthIssueDescribe;
	}

	public String getPrefWrk() {
		return prefWrk;
	}

	public void setPrefWrk(String prefWrk) {
		this.prefWrk = prefWrk;
	}

	public String getFormFilledBy() {
		return formFilledBy;
	}

	public void setFormFilledBy(String formFilledBy) {
		this.formFilledBy = formFilledBy;
	}

	public String getAssess() {
		return assess;
	}

	public void setAssess(String assess) {
		this.assess = assess;
	}

	public String getPlaceOfAsss() {
		return placeOfAsss;
	}

	public void setPlaceOfAsss(String placeOfAsss) {
		this.placeOfAsss = placeOfAsss;
	}

	public String getObjective() {
		return objective;
	}

	public void setObjective(String objective) {
		this.objective = objective;
	}

	public String getYearOfICS() {
		return yearOfICS;
	}

	public void setYearOfICS(String yearOfICS) {
		this.yearOfICS = yearOfICS;
	}

	public String getAgricultureImplements() {
		return agricultureImplements;
	}

	public void setAgricultureImplements(String agricultureImplements) {
		this.agricultureImplements = agricultureImplements;
	}

	public String getIsDisable() {
		return isDisable;
	}

	public void setIsDisable(String isDisable) {
		this.isDisable = isDisable;
	}

	public String getTotalHsldLabel() {
		return totalHsldLabel;
	}

	public void setTotalHsldLabel(String totalHsldLabel) {
		this.totalHsldLabel = totalHsldLabel;
	}

	public String getTypez() {
		return typez;
	}

	public void setTypez(String typez) {
		this.typez = typez;
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
	}

	public Integer getSmartPhone() {
		return smartPhone;
	}

	public void setSmartPhone(Integer smartPhone) {
		this.smartPhone = smartPhone;
	}

	public Integer getShg() {
		return shg;
	}

	public void setShg(Integer shg) {
		this.shg = shg;
	}

	public String getMsgNo() {
		return msgNo;
	}

	public void setMsgNo(String msgNo) {
		this.msgNo = msgNo;
	}

	public String getMasterData() {
		return masterData;
	}

	public void setMasterData(String masterData) {
		this.masterData = masterData;
	}

	public byte[] getFingerPrint() {
		return fingerPrint;
	}

	public void setFingerPrint(byte[] fingerPrint) {
		this.fingerPrint = fingerPrint;
	}

	
	public Double getFarmSize() {
		return farmSize;
	}

	public void setFarmSize(Double farmSize) {
		this.farmSize = farmSize;
	}

	public String getFarmNames() {
		return farmNames;
	}

	public void setFarmNames(String farmNames) {
		this.farmNames = farmNames;
	}

	public Integer getIsPlotting() {
		return isPlotting;
	}

	public void setIsPlotting(Integer isPlotting) {
		this.isPlotting = isPlotting;
	}

	public int getExistingFarmerFlag() {
		return existingFarmerFlag;
	}

	public void setExistingFarmerFlag(int existingFarmerFlag) {
		this.existingFarmerFlag = existingFarmerFlag;
	}

	public Set<DistributionBalance> getDistributionBalance() {
		return distributionBalance;
	}

	public void setDistributionBalance(Set<DistributionBalance> distributionBalance) {
		this.distributionBalance = distributionBalance;
	}

	public String getInsyear() {
		return insyear;
	}

	public void setInsyear(String insyear) {
		this.insyear = insyear;
	}

	public long getStateId() {
		return stateId;
	}

	public void setStateId(long stateId) {
		this.stateId = stateId;
	}

	public byte[] getIdProofImg() {
		return idProofImg;
	}

	public void setIdProofImg(byte[] idProofImg) {
		this.idProofImg = idProofImg;
	}

	public String getOrganicStatus() {
		return organicStatus;
	}

	public void setOrganicStatus(String organicStatus) {
		this.organicStatus = organicStatus;
	}

	public File getFarmerImage() {
		return farmerImage;
	}

	public void setFarmerImage(File farmerImage) {
		this.farmerImage = farmerImage;
	}

	public String getTotalAcreage() {
		return totalAcreage;
	}

	public void setTotalAcreage(String totalAcreage) {
		this.totalAcreage = totalAcreage;
	}

	public String getCertificationStandardLevel() {
		return certificationStandardLevel;
	}

	public void setCertificationStandardLevel(String certificationStandardLevel) {
		this.certificationStandardLevel = certificationStandardLevel;
	}

	public byte[] getDigitalSign() {
		return digitalSign;
	}

	public void setDigitalSign(byte[] digitalSign) {
		this.digitalSign = digitalSign;
	}

	public String getCropNames() {
		return cropNames;
	}

	public void setCropNames(String cropNames) {
		this.cropNames = cropNames;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getTotalCultivatable() {
		return totalCultivatable;
	}

	public void setTotalCultivatable(String totalCultivatable) {
		this.totalCultivatable = totalCultivatable;
	}

	public String getTotalFarm() {
		return totalFarm;
	}

	public void setTotalFarm(String totalFarm) {
		this.totalFarm = totalFarm;
	}

	public String getLifInsCmpName() {
		return lifInsCmpName;
	}

	public void setLifInsCmpName(String lifInsCmpName) {
		this.lifInsCmpName = lifInsCmpName;
	}

	public String getHealthInsCmpName() {
		return healthInsCmpName;
	}

	public void setHealthInsCmpName(String healthInsCmpName) {
		this.healthInsCmpName = healthInsCmpName;
	}

	public String getCrpInsCmpName() {
		return crpInsCmpName;
	}

	public void setCrpInsCmpName(String crpInsCmpName) {
		this.crpInsCmpName = crpInsCmpName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getVillageName() {
		return villageName;
	}

	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}

	public String getFarmer_status() {
		return farmer_status;
	}

	public void setFarmer_status(String farmer_status) {
		this.farmer_status = farmer_status;
	}

	public String getFarmer_fpo() {
		return farmer_fpo;
	}

	public void setFarmer_fpo(String farmer_fpo) {
		this.farmer_fpo = farmer_fpo;
	}

	public String getSelectedFarmerIds() {
		return selectedFarmerIds;
	}

	public void setSelectedFarmerIds(String selectedFarmerIds) {
		this.selectedFarmerIds = selectedFarmerIds;
	}
	
	
}
