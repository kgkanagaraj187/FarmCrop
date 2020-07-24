package com.ese.entity.traceability;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.PMTDetail;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;

public class LedgerData {
	public static enum type {HEAP,GINNING};
	private long id;
	private double previousStock;
	private double txnStock;
	private double totalStock;
	private PMTDetail pmtDetail;
	private Date mtnrDate;
	private Warehouse coOperative;
	private String ics;
	private String heap;
	private double openStk;
	private double inwardStk;
	private double closStk;
	private Date date;
	private Warehouse ginning;
	private ProcurementProduct product;
	private int ledgerType;
	private String branchId;
	private double lintRecvry;
	private double seedRecvry;
	private double scrap;
	private String season;
	
	private double lintPer;
	private double seedPer;
	private double scrapPer;
	private double issueStk;
	private String farmer;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public double getPreviousStock() {
		return previousStock;
	}
	public void setPreviousStock(double previousStock) {
		this.previousStock = previousStock;
	}
	public double getTxnStock() {
		return txnStock;
	}
	public void setTxnStock(double txnStock) {
		this.txnStock = txnStock;
	}
	public double getTotalStock() {
		return totalStock;
	}
	public void setTotalStock(double totalStock) {
		this.totalStock = totalStock;
	}
	
	public PMTDetail getPmtDetail() {
		return pmtDetail;
	}
	public void setPmtDetail(PMTDetail pmtDetail) {
		this.pmtDetail = pmtDetail;
	}
	public Date getMtnrDate() {
		return mtnrDate;
	}
	public void setMtnrDate(Date mtnrDate) {
		this.mtnrDate = mtnrDate;
	}
	public Warehouse getCoOperative() {
		return coOperative;
	}
	public void setCoOperative(Warehouse coOperative) {
		this.coOperative = coOperative;
	}
	
	public double getOpenStk() {
		return openStk;
	}
	public void setOpenStk(double openStk) {
		this.openStk = openStk;
	}
	public double getInwardStk() {
		return inwardStk;
	}
	public void setInwardStk(double inwardStk) {
		this.inwardStk = inwardStk;
	}
	public double getClosStk() {
		return closStk;
	}
	public void setClosStk(double closStk) {
		this.closStk = closStk;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
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
	public String getIcs() {
		return ics;
	}
	public void setIcs(String ics) {
		this.ics = ics;
	}
	public String getHeap() {
		return heap;
	}
	public void setHeap(String heap) {
		this.heap = heap;
	}
	public int getLedgerType() {
		return ledgerType;
	}
	public void setLedgerType(int ledgerType) {
		this.ledgerType = ledgerType;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public double getLintRecvry() {
		return lintRecvry;
	}
	public void setLintRecvry(double lintRecvry) {
		this.lintRecvry = lintRecvry;
	}
	public double getSeedRecvry() {
		return seedRecvry;
	}
	public void setSeedRecvry(double seedRecvry) {
		this.seedRecvry = seedRecvry;
	}
	public double getScrap() {
		return scrap;
	}
	public void setScrap(double scrap) {
		this.scrap = scrap;
	}
	public double getLintPer() {
		return lintPer;
	}
	public void setLintPer(double lintPer) {
		this.lintPer = lintPer;
	}
	public double getSeedPer() {
		return seedPer;
	}
	public void setSeedPer(double seedPer) {
		this.seedPer = seedPer;
	}
	public double getScrapPer() {
		return scrapPer;
	}
	public void setScrapPer(double scrapPer) {
		this.scrapPer = scrapPer;
	}
	public double getIssueStk() {
		return issueStk;
	}
	public void setIssueStk(double issueStk) {
		this.issueStk = issueStk;
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
