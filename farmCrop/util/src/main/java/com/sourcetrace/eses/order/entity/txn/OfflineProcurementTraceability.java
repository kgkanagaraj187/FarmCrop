package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;
import java.util.Set;

import com.ese.entity.traceability.ProcurementTraceabilityDetails;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.profile.Village;

public class OfflineProcurementTraceability
{
	
	private long id;
	private String farmerId;
	private String warehouseId;
	private int stripPositive;
	private byte[] stripImage;
	private Set<OfflineProcurementTraceabilityDetails> offlineprocurTraceabtyDetails;
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
	private String villageCode;
	private int statusCode;
	private String statusMsg;
	private String agentId;
	private String deviceId;
	private String servicePointId;
	private String latitude;
	private String longitude;
	private String procurementCenter;
	private Date procurementDate;
	private String messageNo;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	
	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
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

	

	public Set<OfflineProcurementTraceabilityDetails> getOfflineprocurTraceabtyDetails() {
		return offlineprocurTraceabtyDetails;
	}

	public void setOfflineprocurTraceabtyDetails(Set<OfflineProcurementTraceabilityDetails> offlineprocurTraceabtyDetails) {
		this.offlineprocurTraceabtyDetails = offlineprocurTraceabtyDetails;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMsg() {
		return statusMsg;
	}

	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getServicePointId() {
		return servicePointId;
	}

	public void setServicePointId(String servicePointId) {
		this.servicePointId = servicePointId;
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

	public String getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}

	public String getVillageCode() {
		return villageCode;
	}

	public void setVillageCode(String villageCode) {
		this.villageCode = villageCode;
	}

	public String getProcurementCenter() {
		return procurementCenter;
	}

	public void setProcurementCenter(String procurementCenter) {
		this.procurementCenter = procurementCenter;
	}

	public Date getProcurementDate() {
		return procurementDate;
	}

	public void setProcurementDate(Date procurementDate) {
		this.procurementDate = procurementDate;
	}

	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getMessageNo() {
		return messageNo;
	}

	public void setMessageNo(String messageNo) {
		this.messageNo = messageNo;
	}

	

}
