package com.ese.entity.traceability;

import java.util.Date;
import java.util.Set;

import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.PMTDetail;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.Village;

public class HeapDataDetail {
	private long id;
	private Date date;
	private HeapData heapData;
	private double totalStock;
	private double previousStock;
	private double txnStock;
	private String description;
	private long pmtDetailId;
	private double lintCotton;
	private double seedCotton;
	private double scrup;
	private Integer stockType;
	private String farmer;
	private String ics;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public HeapData getHeapData() {
		return heapData;
	}
	public void setHeapData(HeapData heapData) {
		this.heapData = heapData;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public double getTotalStock() {
		return totalStock;
	}
	public void setTotalStock(double totalStock) {
		this.totalStock = totalStock;
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
	
	public long getPmtDetailId() {
		return pmtDetailId;
	}
	public void setPmtDetailId(long pmtDetailId) {
		this.pmtDetailId = pmtDetailId;
	}
	public double getLintCotton() {
		return lintCotton;
	}
	public void setLintCotton(double lintCotton) {
		this.lintCotton = lintCotton;
	}
	public double getSeedCotton() {
		return seedCotton;
	}
	public void setSeedCotton(double seedCotton) {
		this.seedCotton = seedCotton;
	}
	public double getScrup() {
		return scrup;
	}
	public void setScrup(double scrup) {
		this.scrup = scrup;
	}
	public Integer getStockType() {
		return stockType;
	}
	public void setStockType(Integer stockType) {
		this.stockType = stockType;
	}
	public String getFarmer() {
		return farmer;
	}
	public void setFarmer(String farmer) {
		this.farmer = farmer;
	}
	public String getIcs() {
		return ics;
	}
	public void setIcs(String ics) {
		this.ics = ics;
	}
	
}
