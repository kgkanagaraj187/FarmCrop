package com.sourcetrace.esesw.entity.profile;

import java.util.Date;

public class FarmIcsConversion {

    public enum ICSTypes {
        ICS_1, ICS_2, ICS_3, ORGANIC
    }

    private Long id;
    private Farm farm;
    private Date inspectionDate;
    private String inspectorName;
    private int qualified;
   
    private String sanctionreason;
    private String sanctionDuration;
    private String icsType;
    private int status;
    private String season;
    private String inspectorMobile;
    private String scope;
    private String totalLand;
    private String organicLand;
    private String totalSite;
    private String insType;
    private int isActive;
    private Farmer farmer;
    private String certType;
    
    //Wilmar
    private String organicStatus;
 // Transient variable
    private String formatInspectionDate;
    
 
    private String inspectionDateString;
    
    
    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public Farm getFarm() {

        return farm;
    }

    public void setFarm(Farm farm) {

        this.farm = farm;
    }

    
    public Date getInspectionDate() {

        return inspectionDate;
    }

    public void setInspectionDate(Date inspectionDate) {

        this.inspectionDate = inspectionDate;
    }

    public String getInspectorName() {

        return inspectorName;
    }

    public void setInspectorName(String inspectorName) {

        this.inspectorName = inspectorName;
    }

    public int getQualified() {

        return qualified;
    }

    public void setQualified(int qualified) {

        this.qualified = qualified;
    }

   
    public String getSanctionreason() {

        return sanctionreason;
    }

    public void setSanctionreason(String sanctionreason) {

        this.sanctionreason = sanctionreason;
    }

    public String getSanctionDuration() {

        return sanctionDuration;
    }

    public void setSanctionDuration(String sanctionDuration) {

        this.sanctionDuration = sanctionDuration;
    }

    public String getIcsType() {

        return icsType;
    }

    public void setIcsType(String icsType) {

        this.icsType = icsType;
    }

    public int getStatus() {

        return status;
    }

    public void setStatus(int status) {

        this.status = status;
    }
  

    public String getInspectionDateString() {
    
        return inspectionDateString;
    }

    public void setInspectionDateString(String inspectionDateString) {
    
        this.inspectionDateString = inspectionDateString;
    }

	
	public String getFormatInspectionDate() {
		return formatInspectionDate;
	}

	public void setFormatInspectionDate(String formatInspectionDate) {
		this.formatInspectionDate = formatInspectionDate;
	}

	public String getOrganicStatus() {
		return organicStatus;
	}

	public void setOrganicStatus(String organicStatus) {
		this.organicStatus = organicStatus;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public String getInspectorMobile() {
		return inspectorMobile;
	}

	public void setInspectorMobile(String inspectorMobile) {
		this.inspectorMobile = inspectorMobile;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getTotalLand() {
		return totalLand;
	}

	public void setTotalLand(String totalLand) {
		this.totalLand = totalLand;
	}

	public String getOrganicLand() {
		return organicLand;
	}

	public void setOrganicLand(String organicLand) {
		this.organicLand = organicLand;
	}

	public String getTotalSite() {
		return totalSite;
	}

	public void setTotalSite(String totalSite) {
		this.totalSite = totalSite;
	}

	public Farmer getFarmer() {
		return farmer;
	}

	public void setFarmer(Farmer farmer) {
		this.farmer = farmer;
	}

	public String getCertType() {
		return certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
	}

	public String getInsType() {
		return insType;
	}

	public void setInsType(String insType) {
		this.insType = insType;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
    
    

}
