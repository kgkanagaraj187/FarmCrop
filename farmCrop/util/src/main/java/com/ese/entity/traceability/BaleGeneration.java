package com.ese.entity.traceability;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;

public class BaleGeneration {
	
	private long id;
	private Warehouse ginning;
	private Date genDate;
	private Date baleDate;
	private String heap;
	private String lotNo;
	private String prNo;
	private Double baleWeight;
	private String branchId;
	private GinningProcess ginningProcess;
	private Integer status;
	private SpinningTransfer spinningTransfer;
	private Map<String, String> filterData;
	private String season;
	
	//Transciant variable
	private String heapName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Warehouse getGinning() {
		return ginning;
	}

	public void setGinning(Warehouse ginning) {
		this.ginning = ginning;
	}

	public Date getGenDate() {
		return genDate;
	}

	public void setGenDate(Date genDate) {
		this.genDate = genDate;
	}

	public Date getBaleDate() {
		return baleDate;
	}

	public void setBaleDate(Date baleDate) {
		this.baleDate = baleDate;
	}

	public String getHeap() {
		return heap;
	}

	public void setHeap(String heap) {
		this.heap = heap;
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

	public Double getBaleWeight() {
		return baleWeight;
	}

	public void setBaleWeight(Double baleWeight) {
		this.baleWeight = baleWeight;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public GinningProcess getGinningProcess() {
		return ginningProcess;
	}

	public void setGinningProcess(GinningProcess ginningProcess) {
		this.ginningProcess = ginningProcess;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public SpinningTransfer getSpinningTransfer() {
		return spinningTransfer;
	}

	public void setSpinningTransfer(SpinningTransfer spinningTransfer) {
		this.spinningTransfer = spinningTransfer;
	}

	public Map<String, String> getFilterData() {
		return filterData;
	}

	public void setFilterData(Map<String, String> filterData) {
		this.filterData = filterData;
	}

	public String getHeapName() {
		return heapName;
	}

	public void setHeapName(String heapName) {
		this.heapName = heapName;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}
	
}
