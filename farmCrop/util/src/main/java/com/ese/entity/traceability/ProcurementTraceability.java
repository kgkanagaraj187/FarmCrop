package com.ese.entity.traceability;

import java.util.Date;
import java.util.Set;

import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.esesw.entity.profile.Farmer;

public class ProcurementTraceability {
	
	public static final String PROCURMEMENT = "PROCUREMENT_TRACEABILITY";
	public static final String PROCUREMENT_AMOUNT = "PROCUREMENT AMOUNT";
	public static final String PROCUREMENT_PAYMENT = "PROCUREMENT PAYMENT";
	
	private long id;
	private Farmer farmer;
	private Warehouse warehouse;
	private int stripPositive;
	private byte[] stripImage;
	private Set<ProcurementTraceabilityDetails> procurmentTraceabilityDetails;
	private String trash;
	private String moisture;
	private String stapleLen;
	private String kowdi_kapas;
	private String yellow_cotton;
	private Long revNo;
	private String branchId;
	private Date createdDate;
	private Date updatedDate;
	private String createdUser;
	private String updatedUser;
	private String receiptNo;
	private double totalProVal;
	private String season;
	private double paymentAmount;
	private AgroTransaction agroTransaction;
	private Date procurementDate;
	private String latitude;
	private String longitude;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Farmer getFarmer() {
		return farmer;
	}

	public void setFarmer(Farmer farmer) {
		this.farmer = farmer;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public Integer getStripPositive() {
		return stripPositive;
	}

	public void setStripPositive(Integer stripPositive) {
		this.stripPositive = stripPositive;
	}

	public byte[] getStripImage() {
		return stripImage;
	}

	public void setStripImage(byte[] stripImage) {
		this.stripImage = stripImage;
	}

	public String getTrash() {
		return trash;
	}

	public void setTrash(String trash) {
		this.trash = trash;
	}

	public String getMoisture() {
		return moisture;
	}

	public void setMoisture(String moisture) {
		this.moisture = moisture;
	}

	public String getStapleLen() {
		return stapleLen;
	}

	public void setStapleLen(String stapleLen) {
		this.stapleLen = stapleLen;
	}

	public String getKowdi_kapas() {
		return kowdi_kapas;
	}

	public void setKowdi_kapas(String kowdi_kapas) {
		this.kowdi_kapas = kowdi_kapas;
	}

	public String getYellow_cotton() {
		return yellow_cotton;
	}

	public void setYellow_cotton(String yellow_cotton) {
		this.yellow_cotton = yellow_cotton;
	}

	public Long getRevNo() {
		return revNo;
	}

	public void setRevNo(Long revNo) {
		this.revNo = revNo;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public String getUpdatedUser() {
		return updatedUser;
	}

	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public Set<ProcurementTraceabilityDetails> getProcurmentTraceabilityDetails() {
		return procurmentTraceabilityDetails;
	}

	public void setProcurmentTraceabilityDetails(Set<ProcurementTraceabilityDetails> procurmentTraceabilityDetails) {
		this.procurmentTraceabilityDetails = procurmentTraceabilityDetails;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public AgroTransaction getAgroTransaction() {
		return agroTransaction;
	}

	public void setAgroTransaction(AgroTransaction agroTransaction) {
		this.agroTransaction = agroTransaction;
	}

	public double getTotalProVal() {
		return totalProVal;
	}

	public void setTotalProVal(double totalProVal) {
		this.totalProVal = totalProVal;
	}

	public double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public void setStripPositive(int stripPositive) {
		this.stripPositive = stripPositive;
	}

	public Date getProcurementDate() {
		return procurementDate;
	}

	public void setProcurementDate(Date procurementDate) {
		this.procurementDate = procurementDate;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

}
