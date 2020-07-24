package com.ese.entity.traceability;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;

public class GinningProcess {
	public static final String LINT_COTTON="0";
	public static final String SEED_COTTON="1";
	private long id;
	private String processDate;
	private Warehouse ginning;
	private ProcurementProduct product;
	
	private String heapCode;
	private Double processQty;
	private long baleCount;
	private Double totlintCotton;
	private Double totseedCotton;
	private Double totscrap;
	private Double lintPer;
	private Double seedPer;
	private Double scrapPer;
	private String branchId;
	private String farmer;
	private String ics;
	private Set<BaleGeneration> baleGenerations;
	private Integer status;
	private String season;
	private Map<String, String> filterData;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getProcessDate() {
		return processDate;
	}
	public void setProcessDate(String processDate) {
		this.processDate = processDate;
	}
	public Warehouse getGinning() {
		return ginning;
	}
	public void setGinning(Warehouse ginning) {
		this.ginning = ginning;
	}
	public ProcurementProduct getProduct() {
		return product;
	}
	public void setProduct(ProcurementProduct product) {
		this.product = product;
	}
	
	public String getHeapCode() {
		return heapCode;
	}
	public void setHeapCode(String heapCode) {
		this.heapCode = heapCode;
	}
	public Double getProcessQty() {
		return processQty;
	}
	public void setProcessQty(Double processQty) {
		this.processQty = processQty;
	}
	public Double getTotlintCotton() {
		return totlintCotton;
	}
	public void setTotlintCotton(Double totlintCotton) {
		this.totlintCotton = totlintCotton;
	}
	public Double getTotseedCotton() {
		return totseedCotton;
	}
	public void setTotseedCotton(Double totseedCotton) {
		this.totseedCotton = totseedCotton;
	}
	public Double getTotscrap() {
		return totscrap;
	}
	public void setTotscrap(Double totscrap) {
		this.totscrap = totscrap;
	}
	public Double getLintPer() {
		return lintPer;
	}
	public void setLintPer(Double lintPer) {
		this.lintPer = lintPer;
	}
	public Double getSeedPer() {
		return seedPer;
	}
	public void setSeedPer(Double seedPer) {
		this.seedPer = seedPer;
	}
	public Double getScrapPer() {
		return scrapPer;
	}
	public void setScrapPer(Double scrapPer) {
		this.scrapPer = scrapPer;
	}
	public Map<String, String> getFilterData() {
		return filterData;
	}
	public void setFilterData(Map<String, String> filterData) {
		this.filterData = filterData;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public Set<BaleGeneration> getBaleGenerations() {
		return baleGenerations;
	}
	public void setBaleGenerations(Set<BaleGeneration> baleGenerations) {
		this.baleGenerations = baleGenerations;
	}
	public long getBaleCount() {
		return baleCount;
	}
	public void setBaleCount(long baleCount) {
		this.baleCount = baleCount;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	public String getIcs() {
		return ics;
	}
	public void setIcs(String ics) {
		this.ics = ics;
	}
}
