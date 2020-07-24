package com.sourcetrace.esesw.entity.profile;

public class HousingInfo {
	
	private long id;
	private String noCowShad;
	private String housingShadType;
	private String spacePerCow;
	private Farm farm;
	private String noCowShadStr;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNoCowShad() {
		return noCowShad;
	}
	public void setNoCowShad(String noCowShad) {
		this.noCowShad = noCowShad;
	}
	public String getHousingShadType() {
		return housingShadType;
	}
	public void setHousingShadType(String housingShadType) {
		this.housingShadType = housingShadType;
	}
	public String getSpacePerCow() {
		return spacePerCow;
	}
	public void setSpacePerCow(String spacePerCow) {
		this.spacePerCow = spacePerCow;
	}
	public Farm getFarm() {
		return farm;
	}
	public void setFarm(Farm farm) {
		this.farm = farm;
	}
    public String getNoCowShadStr() {
    
        return noCowShadStr;
    }
    public void setNoCowShadStr(String noCowShadStr) {
    
        this.noCowShadStr = noCowShadStr;
    }
	
	

}
