package com.ese.entity.traceability;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.PMTImageDetails;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;

public class SpinningTransfer {
	
	private long id;
	private Date transDate;
	private String invoiceNo;
	private String truckNo;
	private Warehouse ginning;
	private Warehouse spinning;
	private String lotNo;
	private String prNo;
	private Long noBals;
	private Double netWeight;
	private Double rate;
	private Double netAmt;
	private String type;
	private String branchId;
	private Map<String, String> filterData;
	private Set<PMTImageDetails> pmtImageDetails;
	private Set<PMTImageDetails> pmtGmoDetails;
	private Set<BaleGeneration> baleGenerations;
	private String farmer;
	private String season;
	
	public String getFarmer() {
		return farmer;
	}
	public void setFarmer(String farmer) {
		this.farmer = farmer;
	}
	//Transciant Variable
	private String typeName;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getTransDate() {
		return transDate;
	}
	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getTruckNo() {
		return truckNo;
	}
	public void setTruckNo(String truckNo) {
		this.truckNo = truckNo;
	}
	public Warehouse getGinning() {
		return ginning;
	}
	public void setGinning(Warehouse ginning) {
		this.ginning = ginning;
	}
	public Warehouse getSpinning() {
		return spinning;
	}
	public void setSpinning(Warehouse spinning) {
		this.spinning = spinning;
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
	public Long getNoBals() {
		return noBals;
	}
	public void setNoBals(Long noBals) {
		this.noBals = noBals;
	}
	public Double getNetWeight() {
		return netWeight;
	}
	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public Double getNetAmt() {
		return netAmt;
	}
	public void setNetAmt(Double netAmt) {
		this.netAmt = netAmt;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public Map<String, String> getFilterData() {
		return filterData;
	}
	public void setFilterData(Map<String, String> filterData) {
		this.filterData = filterData;
	}
	public Set<PMTImageDetails> getPmtImageDetails() {
		return pmtImageDetails;
	}
	public void setPmtImageDetails(Set<PMTImageDetails> pmtImageDetails) {
		this.pmtImageDetails = pmtImageDetails;
	}
	public Set<BaleGeneration> getBaleGenerations() {
		return baleGenerations;
	}
	public void setBaleGenerations(Set<BaleGeneration> baleGenerations) {
		this.baleGenerations = baleGenerations;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Set<PMTImageDetails> getPmtGmoDetails() {
		return pmtGmoDetails;
	}
	public void setPmtGmoDetails(Set<PMTImageDetails> pmtGmoDetails) {
		this.pmtGmoDetails = pmtGmoDetails;
	}
	public String getSeason() {
		return season;
	}
	public void setSeason(String season) {
		this.season = season;
	}
	
	
}
