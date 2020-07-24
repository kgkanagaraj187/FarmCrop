package com.ese.entity.traceability;

import java.util.Set;

import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.Village;

public class ProcurementTraceabilityStock {

	private long id;
	private Municipality city;
	private Village village;
	private Warehouse coOperative;
	private ProcurementProduct procurementProduct;
	private long revNo;
	private String ics;
	private long numberOfBags;
	private double totalStock;
	private String agentId;
	private String grade;
	private String season;
	private Set<ProcurementTraceabilityStockDetails> procurmentTraceabilityStockDetails;
	//private String receiptNo;
	private String quality;
	private String branchId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getRevNo() {
		return revNo;
	}

	public void setRevNo(long revNo) {
		this.revNo = revNo;
	}

	public Set<ProcurementTraceabilityStockDetails> getProcurmentTraceabilityStockDetails() {
		return procurmentTraceabilityStockDetails;
	}

	public void setProcurmentTraceabilityStockDetails(
			Set<ProcurementTraceabilityStockDetails> procurmentTraceabilityStockDetails) {
		this.procurmentTraceabilityStockDetails = procurmentTraceabilityStockDetails;
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

	public Municipality getCity() {
		return city;
	}

	public void setCity(Municipality city) {
		this.city = city;
	}

	public ProcurementProduct getProcurementProduct() {
		return procurementProduct;
	}

	public void setProcurementProduct(ProcurementProduct procurementProduct) {
		this.procurementProduct = procurementProduct;
	}

	public long getNumberOfBags() {
		return numberOfBags;
	}

	public void setNumberOfBags(long numberOfBags) {
		this.numberOfBags = numberOfBags;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public Village getVillage() {
		return village;
	}

	public void setVillage(Village village) {
		this.village = village;
	}

	public Warehouse getCoOperative() {
		return coOperative;
	}

	public void setCoOperative(Warehouse coOperative) {
		this.coOperative = coOperative;
	}

	/*public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}*/

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
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

}
