/*
 * Warehouse.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.entity;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.sourcetrace.eses.entity.ColdStorage;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.BankInformation;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.Village;

public class Warehouse {

	public static enum WarehouseTypes {
		COOPERATIVE, SAMITHI, PROCUREMENT_PLACE, GINNER, SPINNER
	}

	public static final int PRODUCT_DISTRIBUTION_CENTER = 1;
	public static final int FIELD_COLLECTION_CENTER = 0;

	public static final Integer COOPERATIVE = 0;
	public static final Integer SAMITHI = 1;
	public static final Integer PROCUREMENT_PLACE = 2;
	public static final Integer GINNER = 3;
	public static final Integer SPINNING=4;
	private long id;
	private String name;
	private String code;
	// private Municipality city;
	private Set<Municipality> citys;
	private Set<WarehouseProduct> warehouseProducts;
	private Set<Village> villages;
	private Warehouse refCooperative;
	private long revisionNo;
	private Integer typez;
	private String location;
	private String address;
	private String phoneNo;
	private String capacityInTonnes;
	private String warehouseInCharge;
	private String storageCommodity;
	private String commodityOthers;
	private String warehouseOwnerShip;
	private String branchId;
	private Date createdDate;
	private Date updatedDate;
	private String createdUsername;
	private String updatedUsername;
	private String multiplecityId;
	private Date formationDate;
	private String sanghamType;
	private Integer warehouse_type;
	private int utzStatus;
	private String groupType;

	// chetna
	private String shgName;
	private Integer totalMembers;
	private Date groupFormationDate;
	private Integer noOfMale;
	private Integer noOfFemale;
	private Set<BankInformation> bankInfo;
	private String presidentName;
	private String presidentMobileNumber;
	private String secretaryName;
	private String secretaryMobileNumber;
	private String treasurer;
	private String treasurerMobileNumber;
	private String latitude;
	private String longitude;
	// transient variable
	private List<String> branchesList;
	private String filterStatus;

	private String jsonString;
	public static final Integer[] WarehouseTypesArray = { 1, 2, 3, 4 };
	public static final Integer[] WarehouseTypeArray = { 0, 2, 3, 4 };
	
	private Set<WarehouseStorageMap> warehouseStorageMap;
	
	
	/*Kenya FPO*/
	private long villageId;
	private String email;

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
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {

		return name;
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
	 * Gets the code.
	 * 
	 * @return the code
	 */
	public String getCode() {

		return code;
	}

	/**
	 * Sets the code.
	 * 
	 * @param code
	 *            the new code
	 */
	public void setCode(String code) {

		this.code = code;
	}

	/**
	 * Gets the city.
	 * 
	 * @return the city
	 */
	// public Municipality getCity() {
	//
	// return city;
	// }

	/**
	 * Sets the city.
	 * 
	 * @param city
	 *            the new city
	 */
	// public void setCity(Municipality city) {
	//
	// this.city = city;
	// }

	/**
	 * Sets the warehouse products.
	 * 
	 * @param warehouseProducts
	 *            the new warehouse products
	 */
	public void setWarehouseProducts(Set<WarehouseProduct> warehouseProducts) {

		this.warehouseProducts = warehouseProducts;
	}

	/**
	 * Gets the warehouse products.
	 * 
	 * @return the warehouse products
	 */
	public Set<WarehouseProduct> getWarehouseProducts() {

		return warehouseProducts;
	}

	/**
	 * Gets the warehouse name.
	 * 
	 * @return the warehouse name
	 */
	public String getWarehouseName() {

		StringBuffer warehouseName = new StringBuffer();
		if (!StringUtil.isEmpty(code)) {
			warehouseName.append(code);
			warehouseName.append(" - ");
		}
		if (!StringUtil.isEmpty(name)) {
			warehouseName.append(name);
		}
		return warehouseName.toString();
	}

	/**
	 * Gets the warehouse name code.
	 * 
	 * @return the warehouse name code
	 */
	public String getWarehouseNameCode() {

		StringBuffer warehouseName = new StringBuffer();
		if (!StringUtil.isEmpty(name)) {
			warehouseName.append(name);
		}
		if (!StringUtil.isEmpty(code)) {
			warehouseName.append(" - ");
			warehouseName.append(code);
		}
		return warehouseName.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return name;
	}

	/**
	 * Gets the villages.
	 * 
	 * @return the villages
	 */
	public Set<Village> getVillages() {

		return villages;
	}

	/**
	 * Sets the villages.
	 * 
	 * @param villages
	 *            the new villages
	 */
	public void setVillages(Set<Village> villages) {

		this.villages = villages;
	}

	/**
	 * Gets the ref cooperative.
	 * 
	 * @return the ref cooperative
	 */
	public Warehouse getRefCooperative() {

		return refCooperative;
	}

	/**
	 * Sets the ref cooperative.
	 * 
	 * @param refCooperative
	 *            the new ref cooperative
	 */
	public void setRefCooperative(Warehouse refCooperative) {

		this.refCooperative = refCooperative;
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

	public Integer getTypez() {

		return typez;
	}

	public void setTypez(Integer typez) {

		this.typez = typez;
	}

	public String getLocation() {

		return location;
	}

	public void setLocation(String location) {

		this.location = location;
	}

	public String getAddress() {

		return address;
	}

	public void setAddress(String address) {

		this.address = address;
	}

	public String getPhoneNo() {

		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {

		this.phoneNo = phoneNo;
	}

	public String getCapacityInTonnes() {

		return capacityInTonnes;
	}

	public void setCapacityInTonnes(String capacityInTonnes) {

		this.capacityInTonnes = capacityInTonnes;
	}

	public String getWarehouseInCharge() {

		return warehouseInCharge;
	}

	public void setWarehouseInCharge(String warehouseInCharge) {

		this.warehouseInCharge = warehouseInCharge;
	}

	public String getStorageCommodity() {

		return storageCommodity;
	}

	public void setStorageCommodity(String storageCommodity) {

		this.storageCommodity = storageCommodity;
	}

	public String getCommodityOthers() {

		return commodityOthers;
	}

	public void setCommodityOthers(String commodityOthers) {

		this.commodityOthers = commodityOthers;
	}

	public String getWarehouseOwnerShip() {

		return warehouseOwnerShip;
	}

	public void setWarehouseOwnerShip(String warehouseOwnerShip) {

		this.warehouseOwnerShip = warehouseOwnerShip;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
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

	public String getCreatedUsername() {
		return createdUsername;
	}

	public void setCreatedUsername(String createdUsername) {
		this.createdUsername = createdUsername;
	}

	public String getUpdatedUsername() {
		return updatedUsername;
	}

	public void setUpdatedUsername(String updatedUsername) {
		this.updatedUsername = updatedUsername;
	}

	public String getMultiplecityId() {
		return multiplecityId;
	}

	public void setMultiplecityId(String multiplecityId) {
		this.multiplecityId = multiplecityId;
	}

	public Set<Municipality> getCitys() {
		return citys;
	}

	public void setCitys(Set<Municipality> citys) {
		this.citys = citys;
	}

	public Date getFormationDate() {
		return formationDate;
	}

	public void setFormationDate(Date formationDate) {
		this.formationDate = formationDate;
	}

	public String getSanghamType() {
		return sanghamType;
	}

	public void setSanghamType(String sanghamType) {
		this.sanghamType = sanghamType;
	}

	public Integer getWarehouse_type() {
		return warehouse_type;
	}

	public void setWarehouse_type(Integer warehouse_type) {
		this.warehouse_type = warehouse_type;
	}

	public int getUtzStatus() {
		return utzStatus;
	}

	public void setUtzStatus(int utzStatus) {
		this.utzStatus = utzStatus;
	}

	public List<String> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getShgName() {
		return shgName;
	}

	public void setShgName(String shgName) {
		this.shgName = shgName;
	}

	public Integer getTotalMembers() {
		return totalMembers;
	}

	public void setTotalMembers(Integer totalMembers) {
		this.totalMembers = totalMembers;
	}

	public Date getGroupFormationDate() {
		return groupFormationDate;
	}

	public void setGroupFormationDate(Date groupFormationDate) {
		this.groupFormationDate = groupFormationDate;
	}

	public Integer getNoOfMale() {
		return noOfMale;
	}

	public void setNoOfMale(Integer noOfMale) {
		this.noOfMale = noOfMale;
	}

	public Integer getNoOfFemale() {
		return noOfFemale;
	}

	public void setNoOfFemale(Integer noOfFemale) {
		this.noOfFemale = noOfFemale;
	}

	public Set<BankInformation> getBankInfo() {
		return bankInfo;
	}

	public void setBankInfo(Set<BankInformation> bankInfo) {
		this.bankInfo = bankInfo;
	}

	public String getPresidentName() {
		return presidentName;
	}

	public void setPresidentName(String presidentName) {
		this.presidentName = presidentName;
	}

	public String getPresidentMobileNumber() {
		return presidentMobileNumber;
	}

	public void setPresidentMobileNumber(String presidentMobileNumber) {
		this.presidentMobileNumber = presidentMobileNumber;
	}

	public String getSecretaryName() {
		return secretaryName;
	}

	public void setSecretaryName(String secretaryName) {
		this.secretaryName = secretaryName;
	}

	public String getSecretaryMobileNumber() {
		return secretaryMobileNumber;
	}

	public void setSecretaryMobileNumber(String secretaryMobileNumber) {
		this.secretaryMobileNumber = secretaryMobileNumber;
	}

	public String getTreasurer() {
		return treasurer;
	}

	public void setTreasurer(String treasurer) {
		this.treasurer = treasurer;
	}

	public String getTreasurerMobileNumber() {
		return treasurerMobileNumber;
	}

	public void setTreasurerMobileNumber(String treasurerMobileNumber) {
		this.treasurerMobileNumber = treasurerMobileNumber;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
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

	public String getFilterStatus() {
		return filterStatus;
	}

	public void setFilterStatus(String filterStatus) {
		this.filterStatus = filterStatus;
	}

	public Set<WarehouseStorageMap> getWarehouseStorageMap() {
		return warehouseStorageMap;
	}

	public void setWarehouseStorageMap(Set<WarehouseStorageMap> warehouseStorageMap) {
		this.warehouseStorageMap = warehouseStorageMap;
	}

	public long getVillageId() {
		return villageId;
	}

	public void setVillageId(long villageId) {
		this.villageId = villageId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	

	
	
	

}
