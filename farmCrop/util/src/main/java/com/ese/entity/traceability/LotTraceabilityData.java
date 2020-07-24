package com.ese.entity.traceability;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;

public class LotTraceabilityData {
	
	private long id;
	private long farmerId;
	private String tracenetCode;
	private String farmerName;
	private String village;
	private String ics;
	private String shg;
	private String lotNo;
	private String prNo;
	private String season;
	private String branchId;
	private String procurementCenter;
	private String ginning;
	private String spinning;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getFarmerId() {
		return farmerId;
	}
	public void setFarmerId(long farmerId) {
		this.farmerId = farmerId;
	}
	public String getTracenetCode() {
		return tracenetCode;
	}
	public void setTracenetCode(String tracenetCode) {
		this.tracenetCode = tracenetCode;
	}
	public String getFarmerName() {
		return farmerName;
	}
	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
	}
	public String getVillage() {
		return village;
	}
	public void setVillage(String village) {
		this.village = village;
	}
	public String getIcs() {
		return ics;
	}
	public void setIcs(String ics) {
		this.ics = ics;
	}
	public String getShg() {
		return shg;
	}
	public void setShg(String shg) {
		this.shg = shg;
	}
	public String getLotNo() {
		return lotNo;
	}
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	public String getPrNo() {
		return prNo;
	}
	public void setPrNo(String prNo) {
		this.prNo = prNo;
	}
	public String getSeason() {
		return season;
	}
	public void setSeason(String season) {
		this.season = season;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getProcurementCenter() {
		return procurementCenter;
	}
	public void setProcurementCenter(String procurementCenter) {
		this.procurementCenter = procurementCenter;
	}
	public String getGinning() {
		return ginning;
	}
	public void setGinning(String ginning) {
		this.ginning = ginning;
	}
	public String getSpinning() {
		return spinning;
	}
	public void setSpinning(String spinning) {
		this.spinning = spinning;
	}
	
	
	
}
