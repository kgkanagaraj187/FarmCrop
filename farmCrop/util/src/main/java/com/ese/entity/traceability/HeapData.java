package com.ese.entity.traceability;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.Village;

public class HeapData {
	public enum stock{HEAP,GINNING};
	private long id;
	private Date createdDate;
	private String name;
	private String code;
	private Warehouse cooperative;
	private Warehouse ginning;
	private ProcurementProduct procurementProduct;
	private String ics;
	private String farmer;
	private double totalStock;
	private double totLintCotton;
	private double totSeedCotton;
	private double totScrup;
	private Set<HeapDataDetail> heapDataDetail;
	private String branchId;
	private String season;
	private Map<String, String> filterData;
	//private int stockType;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public ProcurementProduct getProcurementProduct() {
		return procurementProduct;
	}
	public void setProcurementProduct(ProcurementProduct procurementProduct) {
		this.procurementProduct = procurementProduct;
	}
	public String getIcs() {
		return ics;
	}
	public void setIcs(String ics) {
		this.ics = ics;
	}
	public double getTotalStock() {
		return totalStock;
	}
	public void setTotalStock(double totalStock) {
		this.totalStock = totalStock;
	}
	public Set<HeapDataDetail> getHeapDataDetail() {
		return heapDataDetail;
	}
	public void setHeapDataDetail(Set<HeapDataDetail> heapDataDetail) {
		this.heapDataDetail = heapDataDetail;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public Warehouse getCooperative() {
		return cooperative;
	}
	public void setCooperative(Warehouse cooperative) {
		this.cooperative = cooperative;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Warehouse getGinning() {
		return ginning;
	}
	public void setGinning(Warehouse ginning) {
		this.ginning = ginning;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public double getTotLintCotton() {
		return totLintCotton;
	}
	public void setTotLintCotton(double totLintCotton) {
		this.totLintCotton = totLintCotton;
	}
	public double getTotSeedCotton() {
		return totSeedCotton;
	}
	public void setTotSeedCotton(double totSeedCotton) {
		this.totSeedCotton = totSeedCotton;
	}
	public double getTotScrup() {
		return totScrup;
	}
	public void setTotScrup(double totScrup) {
		this.totScrup = totScrup;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Map<String, String> getFilterData() {
		return filterData;
	}
	public void setFilterData(Map<String, String> filterData) {
		this.filterData = filterData;
	}
	public String getFarmer() {
		return farmer;
	}
	public void setFarmer(String farmer) {
		this.farmer = farmer;
	}
	public String getSeason() {
		return season;
	}
	public void setSeason(String season) {
		this.season = season;
	}
	
	
}
