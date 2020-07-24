package com.ese.entity.traceability;

import java.util.Date;

import com.sourcetrace.esesw.entity.profile.Farmer;

public class ProcurementTraceabilityStockDetails {

	public enum TYPE {
		PROCUREMENT_TRACEABILITY,RECEPTION_TRACEABILITY,TRANSFER_TRACEABILITY
	}

	private long id;
	private Date date;
	private ProcurementTraceabilityStock procurementTraceabilityStock;
	private double totalstock;
	private double previousstock;
	private double txnstock;
	private long previousNumberOfBags;
	private long txnNumberOfBags;
	private long totalNumberOfBags;
	private String description;
	private long referenceId;
	private Farmer farmer;
	private String branchId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ProcurementTraceabilityStock getProcurementTraceabilityStock() {
		return procurementTraceabilityStock;
	}

	public void setProcurementTraceabilityStock(ProcurementTraceabilityStock procurementTraceabilityStock) {
		this.procurementTraceabilityStock = procurementTraceabilityStock;
	}

	public double getTotalstock() {
		return totalstock;
	}

	public void setTotalstock(double totalstock) {
		this.totalstock = totalstock;
	}

	public Farmer getFarmer() {
		return farmer;
	}

	public void setFarmer(Farmer farmer) {
		this.farmer = farmer;
	}

	public double getStock() {
		return totalstock;
	}

	public void setStock(double totalstock) {
		this.totalstock = totalstock;
	}

	public long getPreviousNumberOfBags() {
		return previousNumberOfBags;
	}

	public void setPreviousNumberOfBags(long previousNumberOfBags) {
		this.previousNumberOfBags = previousNumberOfBags;
	}

	public double getPreviousstock() {
		return previousstock;
	}

	public void setPreviousstock(double previousstock) {
		this.previousstock = previousstock;
	}

	public long getTxnNumberOfBags() {
		return txnNumberOfBags;
	}

	public void setTxnNumberOfBags(long txnNumberOfBags) {
		this.txnNumberOfBags = txnNumberOfBags;
	}

	public double getTxnstock() {
		return txnstock;
	}

	public void setTxnstock(double txnstock) {
		this.txnstock = txnstock;
	}

	public long getTotalNumberOfBags() {
		return totalNumberOfBags;
	}

	public void setTotalNumberOfBags(long totalNumberOfBags) {
		this.totalNumberOfBags = totalNumberOfBags;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(long referenceId) {
		this.referenceId = referenceId;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

}
