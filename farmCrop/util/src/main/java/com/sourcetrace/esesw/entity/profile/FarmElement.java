package com.sourcetrace.esesw.entity.profile;

import java.util.List;

import com.sourcetrace.eses.entity.FarmCatalogue;

public class FarmElement {
	
	private long id;
    private Farm farm;
    private FarmCatalogue catalogueId;
    private String otherName;
    private String count;
    private String catalogueType;
    
    //Transient Var
    
    private List<FarmCatalogue> catalogueIdList;
    private List<String> countList;
    
    private String machStr;
    private String machCount;
    private String houseStr;
    private String houseCnt;
    
    
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Farm getFarm() {
		return farm;
	}
	public void setFarm(Farm farm) {
		this.farm = farm;
	}
	public FarmCatalogue getCatalogueId() {
		return catalogueId;
	}
	public void setCatalogueId(FarmCatalogue catalogueId) {
		this.catalogueId = catalogueId;
	}
	public String getOtherName() {
		return otherName;
	}
	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getCatalogueType() {
		return catalogueType;
	}
	public void setCatalogueType(String catalogueType) {
		this.catalogueType = catalogueType;
	}
	public List<FarmCatalogue> getCatalogueIdList() {
		return catalogueIdList;
	}
	public void setCatalogueIdList(List<FarmCatalogue> catalogueIdList) {
		this.catalogueIdList = catalogueIdList;
	}
	public List<String> getCountList() {
		return countList;
	}
	public void setCountList(List<String> countList) {
		this.countList = countList;
	}
	public String getMachStr() {
		return machStr;
	}
	public void setMachStr(String machStr) {
		this.machStr = machStr;
	}
	public String getMachCount() {
		return machCount;
	}
	public void setMachCount(String machCount) {
		this.machCount = machCount;
	}
	public String getHouseStr() {
		return houseStr;
	}
	public void setHouseStr(String houseStr) {
		this.houseStr = houseStr;
	}
	public String getHouseCnt() {
		return houseCnt;
	}
	public void setHouseCnt(String houseCnt) {
		this.houseCnt = houseCnt;
	}
}
