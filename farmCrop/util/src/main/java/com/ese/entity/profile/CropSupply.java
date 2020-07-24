package com.ese.entity.profile;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.sourcetrace.eses.order.entity.txn.CropSupplyImages;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Village;

// TODO: Auto-generated Javadoc
public class CropSupply {

	private long id;
	private Date dateOfSale;
	private Customer buyerInfo;
	private String farmerId;
	private String farmerName;
	private String farmCode;
	private String farmId;
	private String farmName;
	private String vehicleNo;
	private String transportDetail;
	private String receiptInfor;
	private String paymentMode;
	private String paymentInfo;
	private Double totalSaleValu;
	private Set<CropSupplyDetails> cropSupplyDetails;
	private Village village;
	private Farm farm;
	private String invoiceDetail;
	private String branchId;
	private String currentSeasonCode;
	private String cooperative;
	private String icsName;
	private Set<CropSupplyImages> cropSupplyImages;
	private String agentId;
	private String poNumber;
	private String latitude;
	private String longitude;
	

	/** Transient Variable */
	private Integer cropType;
	private Integer totalQty;
	private String villageName;
	private String searchPage;
	private String fatherName;
	private double totalQuantity;	
	private List<String> branchesList;
	private String stateName;
    private String fpo;
    private String batchLotNo;
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {

		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(long id) {

		this.id = id;
	}

	/**
	 * Gets the date of sale.
	 * 
	 * @return the date of sale
	 */
	public Date getDateOfSale() {

		return dateOfSale;
	}

	/**
	 * Sets the date of sale.
	 * 
	 * @param dateOfSale
	 *            the new date of sale
	 */
	public void setDateOfSale(Date dateOfSale) {

		this.dateOfSale = dateOfSale;
	}

	/**
	 * Gets the buyer info.
	 * 
	 * @return the buyer info
	 */
	public Customer getBuyerInfo() {

		return buyerInfo;
	}

	/**
	 * Sets the buyer info.
	 * 
	 * @param buyerInfo
	 *            the new buyer info
	 */
	public void setBuyerInfo(Customer buyerInfo) {

		this.buyerInfo = buyerInfo;
	}

	/**
	 * Gets the farmer id.
	 * 
	 * @return the farmer id
	 */
	public String getFarmerId() {

		return farmerId;
	}

	/**
	 * Sets the farmer id.
	 * 
	 * @param farmerId
	 *            the new farmer id
	 */
	public void setFarmerId(String farmerId) {

		this.farmerId = farmerId;
	}

	/**
	 * Gets the farmer name.
	 * 
	 * @return the farmer name
	 */
	public String getFarmerName() {

		return farmerName;
	}

	/**
	 * Sets the farmer name.
	 * 
	 * @param farmerName
	 *            the new farmer name
	 */
	public void setFarmerName(String farmerName) {

		this.farmerName = farmerName;
	}

	/**
	 * Gets the farm code.
	 * 
	 * @return the farm code
	 */
	public String getFarmCode() {

		return farmCode;
	}

	/**
	 * Sets the farm code.
	 * 
	 * @param farmCode
	 *            the new farm code
	 */
	public void setFarmCode(String farmCode) {

		this.farmCode = farmCode;
	}

	/**
	 * Gets the farm name.
	 * 
	 * @return the farm name
	 */
	public String getFarmName() {

		return farmName;
	}

	/**
	 * Sets the farm name.
	 * 
	 * @param farmName
	 *            the new farm name
	 */
	public void setFarmName(String farmName) {

		this.farmName = farmName;
	}

	/**
	 * Gets the vehicle no.
	 * 
	 * @return the vehicle no
	 */
	public String getVehicleNo() {

		return vehicleNo;
	}

	/**
	 * Sets the vehicle no.
	 * 
	 * @param vehicleNo
	 *            the new vehicle no
	 */
	public void setVehicleNo(String vehicleNo) {

		this.vehicleNo = vehicleNo;
	}

	/**
	 * Gets the transport detail.
	 * 
	 * @return the transport detail
	 */
	public String getTransportDetail() {

		return transportDetail;
	}

	/**
	 * Sets the transport detail.
	 * 
	 * @param transportDetail
	 *            the new transport detail
	 */
	public void setTransportDetail(String transportDetail) {

		this.transportDetail = transportDetail;
	}

	/**
	 * Gets the receipt infor.
	 * 
	 * @return the receipt infor
	 */
	public String getReceiptInfor() {

		return receiptInfor;
	}

	/**
	 * Sets the receipt infor.
	 * 
	 * @param receiptInfor
	 *            the new receipt infor
	 */
	public void setReceiptInfor(String receiptInfor) {

		this.receiptInfor = receiptInfor;
	}

	/**
	 * Gets the payment info.
	 * 
	 * @return the payment info
	 */
	public String getPaymentInfo() {

		return paymentInfo;
	}

	/**
	 * Sets the payment info.
	 * 
	 * @param paymentInfo
	 *            the new payment info
	 */
	public void setPaymentInfo(String paymentInfo) {

		this.paymentInfo = paymentInfo;
	}

	/**
	 * Gets the total sale valu.
	 * 
	 * @return the total sale valu
	 */
	public Double getTotalSaleValu() {

		return totalSaleValu;
	}

	/**
	 * Sets the total sale valu.
	 * 
	 * @param totalSaleValu
	 *            the new total sale valu
	 */
	public void setTotalSaleValu(Double totalSaleValu) {

		this.totalSaleValu = totalSaleValu;
	}

	/**
	 * Gets the crop supply details.
	 * 
	 * @return the crop supply details
	 */
	public Set<CropSupplyDetails> getCropSupplyDetails() {

		return cropSupplyDetails;
	}

	/**
	 * Sets the crop supply details.
	 * 
	 * @param cropSupplyDetails
	 *            the new crop supply details
	 */
	public void setCropSupplyDetails(Set<CropSupplyDetails> cropSupplyDetails) {

		this.cropSupplyDetails = cropSupplyDetails;
	}

	public Village getVillage() {
		return village;
	}

	public void setVillage(Village village) {
		this.village = village;
	}

	public Farm getFarm() {
		return farm;
	}

	public void setFarm(Farm farm) {
		this.farm = farm;
	}

	public String getFarmId() {
		return farmId;
	}

	public void setFarmId(String farmId) {
		this.farmId = farmId;
	}

	public Integer getCropType() {
		return cropType;
	}

	public void setCropType(Integer cropType) {
		this.cropType = cropType;
	}

	public Integer getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(Integer totalQty) {
		this.totalQty = totalQty;
	}

	public String getVillageName() {
		return villageName;
	}

	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}

	public String getInvoiceDetail() {
		return invoiceDetail;
	}

	public void setInvoiceDetail(String invoiceDetail) {
		this.invoiceDetail = invoiceDetail;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getSearchPage() {
		return searchPage;
	}

	public void setSearchPage(String searchPage) {
		this.searchPage = searchPage;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getCurrentSeasonCode() {
		return currentSeasonCode;
	}

	public void setCurrentSeasonCode(String currentSeasonCode) {
		this.currentSeasonCode = currentSeasonCode;
	}

	public String getCooperative() {
		return cooperative;
	}

	public void setCooperative(String cooperative) {
		this.cooperative = cooperative;
	}

    public String getIcsName() {
    
        return icsName;
    }

    public void setIcsName(String icsName) {
    
        this.icsName = icsName;
    }

	public double getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(double totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public List<String> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getFpo() {
		return fpo;
	}

	public void setFpo(String fpo) {
		this.fpo = fpo;
	}

	public Set<CropSupplyImages> getCropSupplyImages() {
		return cropSupplyImages;
	}

	public void setCropSupplyImages(Set<CropSupplyImages> cropSupplyImages) {
		this.cropSupplyImages = cropSupplyImages;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getBatchLotNo() {
		return batchLotNo;
	}

	public void setBatchLotNo(String batchLotNo) {
		this.batchLotNo = batchLotNo;
	}
	
	
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
}