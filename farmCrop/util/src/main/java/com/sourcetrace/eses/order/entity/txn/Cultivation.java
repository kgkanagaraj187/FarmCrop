package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;
import java.util.List;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * The Class Cultivation.
 */
public class Cultivation {

	private long id;
	private String receiptNo;
	private String txnType;
	private String farmId;
	private String farmName;
	private String farmerId;
    private String farmerName;
	private Date expenseDate;
	
	private String landTotal;
	private String landLabourCost;
	private String totalSowing;
	private String sowingLabourCost;
	private String totalGap;
	private String gapLabourCost;
	private String totalWeed;
	private String weedLabourCost;
	private String totalCulture;
	private String cultureLabourCost;
	private String totalIrrigation;
	private String irriLabourCost;
	private String totalFertilizer;
	private String fertLabourcost;
	private String totalPesticide;
	private String pestLabourCost;
	private String totalHarvest;
	private String harvestLabourCost;
	private String fertilizerCost;
	private String manureLabourCost;
	private Set<CultivationDetail> cultivationDetails;
	private String agriIncome;
	private String interCropIncome;
	private String otherSourcesIncome;
    private String branchId;
    private String totalExpense;
    private String transportCost;
    private String fuelCost;
    private String otherCost;
    private String totalCoc;

    private String villageId;
	private String pesticideCost;
	private String fatherName;
	private String currentSeasonCode;
	private String labourCost;
	private String soilPreparation;
	private String cottonQty;
	private String unitSalePrice;
	private String saleCottonIncome;	
	/**Transient*/
	private String villageName;
	private String cropName;
	private String farmerCode;
	private String manureCostMen;
	private String manureCostWomen;
	private String totalManure;
	private String manureCost;
	private String cropCode;
	private List<Cultivation> cultiList;
	private String latitude;
	private String Longitude;
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return Longitude;
	}

	public void setLongitude(String longitude) {
		Longitude = longitude;
	}

	/**
	 * Transient variable
	 */
	private String stateName;
	private String fpo;
	private String icsName;
	private String totLandPreCst;
	private String totSowCst;
	private String totGapCst;
	private String totWeedCst;
	private String totInputCst;
	private String totIrriCst;
	private String totFerCst;
	private String totPestCst;
	private String totManureCst;
	private String totHarvCst;
	private String samCode;
	
	private List<String> branchesList;
	private String agentId;
	
	

	public String getFertilizerCost() {
		return fertilizerCost;
	}

	public void setFertilizerCost(String fertilizerCost) {
		this.fertilizerCost = fertilizerCost;
	}

	public String getOtherSourcesIncome() {
		return otherSourcesIncome;
	}

	public void setOtherSourcesIncome(String otherSourcesIncome) {
		this.otherSourcesIncome = otherSourcesIncome;
	}


	public String getAgriIncome() {
		return agriIncome;
	}

	public String getInterCropIncome() {
		return interCropIncome;
	}
	public String getLandTotal() {
		return landTotal;
	}

	public void setLandTotal(String landTotal) {
		this.landTotal = landTotal;
	}

	

	public String getTotalSowing() {
		return totalSowing;
	}

	public void setTotalSowing(String totalSowing) {
		this.totalSowing = totalSowing;
	}

	

	public String getTotalGap() {
		return totalGap;
	}

	public void setTotalGap(String totalGap) {
		this.totalGap = totalGap;
	}

	

	public String getTotalWeed() {
		return totalWeed;
	}

	public void setTotalWeed(String totalWeed) {
		this.totalWeed = totalWeed;
	}

	

	public String getTotalCulture() {
		return totalCulture;
	}

	public void setTotalCulture(String totalCulture) {
		this.totalCulture = totalCulture;
	}

	

	public String getTotalIrrigation() {
		return totalIrrigation;
	}

	public void setTotalIrrigation(String totalIrrigation) {
		this.totalIrrigation = totalIrrigation;
	}

	

	public String getTotalFertilizer() {
		return totalFertilizer;
	}

	public void setTotalFertilizer(String totalFertilizer) {
		this.totalFertilizer = totalFertilizer;
	}

	
	public String getTotalPesticide() {
		return totalPesticide;
	}

	public void setTotalPesticide(String totalPesticide) {
		this.totalPesticide = totalPesticide;
	}

	
	public String getTotalHarvest() {
		return totalHarvest;
	}

	public void setTotalHarvest(String totalHarvest) {
		this.totalHarvest = totalHarvest;
	}

	public String getTotalExpense() {
		return totalExpense;
	}

	public void setTotalExpense(String totalExpense) {
		this.totalExpense = totalExpense;
	}

	public String getTotalCoc() {
		return totalCoc;
	}

	public void setTotalCoc(String totalCoc) {
		this.totalCoc = totalCoc;
	}
	public void setAgriIncome(String agriIncome) {
		this.agriIncome = agriIncome;
	}

	public void setInterCropIncome(String interCropIncome) {
		this.interCropIncome = interCropIncome;
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
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {

		return id;
	}

	/**
	 * Sets the receipt no.
	 * 
	 * @param receiptNo
	 *            the new receipt no
	 */
	public void setReceiptNo(String receiptNo) {

		this.receiptNo = receiptNo;
	}

	/**
	 * Gets the receipt no.
	 * 
	 * @return the receipt no
	 */
	public String getReceiptNo() {

		return receiptNo;
	}

	/**
	 * Sets the farm id.
	 * 
	 * @param farmId
	 *            the new farm id
	 */
	public void setFarmId(String farmId) {

		this.farmId = farmId;
	}

	/**
	 * Gets the farm id.
	 * 
	 * @return the farm id
	 */
	public String getFarmId() {

		return farmId;
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
	 * Gets the farm name.
	 * 
	 * @return the farm name
	 */
	public String getFarmName() {

		return farmName;
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
	 * Gets the farmer name.
	 * 
	 * @return the farmer name
	 */
	public String getFarmerName() {

		return farmerName;
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
	 * Gets the farmer id.
	 * 
	 * @return the farmer id
	 */
	public String getFarmerId() {

		return farmerId;
	}

	
	/**
	 * Gets the cultivation details.
	 * 
	 * @return the cultivation details
	 */
	public Set<CultivationDetail> getCultivationDetails() {
		return cultivationDetails;
	}

	/**
	 * Sets the cultivation details.
	 * 
	 * @param cultivationDetails
	 *            the new cultivation details
	 */
	public void setCultivationDetails(Set<CultivationDetail> cultivationDetails) {
		this.cultivationDetails = cultivationDetails;
	}

	public Date getExpenseDate() {
		return expenseDate;
	}

	public void setExpenseDate(Date expenseDate) {
		this.expenseDate = expenseDate;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String getTxnType() {
		return txnType;
	}

	



	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getVillageId() {
		return villageId;
	}

	public void setVillageId(String villageId) {
		this.villageId = villageId;
	}

	public String getPesticideCost() {
		return pesticideCost;
	}

	public void setPesticideCost(String pesticideCost) {
		this.pesticideCost = pesticideCost;
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

	public String getManureCostMen() {
		return manureCostMen;
	}

	public void setManureCostMen(String manureCostMen) {
		this.manureCostMen = manureCostMen;
	}

	public String getManureCostWomen() {
		return manureCostWomen;
	}

	public void setManureCostWomen(String manureCostWomen) {
		this.manureCostWomen = manureCostWomen;
	}

	public String getTotalManure() {
		return totalManure;
	}

	public void setTotalManure(String totalManure) {
		this.totalManure = totalManure;
	}

	public String getManureCost() {
		return manureCost;
	}

	public void setManureCost(String manureCost) {
		this.manureCost = manureCost;
	}

	public String getVillageName() {
		return villageName;
	}

	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}

	public List<String> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}

	public String getCropCode() {
		return cropCode;
	}

	public void setCropCode(String cropCode) {
		this.cropCode = cropCode;
	}

	public String getLabourCost() {
		return labourCost;
	}

	public void setLabourCost(String labourCost) {
		this.labourCost = labourCost;
	}

	public List<Cultivation> getCultiList() {
		return cultiList;
	}

	public void setCultiList(List<Cultivation> cultiList) {
		this.cultiList = cultiList;
	}

	public String getCropName() {
		return cropName;
	}

	public void setCropName(String cropName) {
		this.cropName = cropName;
	}

	public String getFarmerCode() {
		return farmerCode;
	}

	public void setFarmerCode(String farmerCode) {
		this.farmerCode = farmerCode;
	}

	public String getCottonQty() {
		return cottonQty;
	}

	public void setCottonQty(String cottonQty) {
		this.cottonQty = cottonQty;
	}

	public String getUnitSalePrice() {
		return unitSalePrice;
	}

	public void setUnitSalePrice(String unitSalePrice) {
		this.unitSalePrice = unitSalePrice;
	}

	public String getSaleCottonIncome() {
		return saleCottonIncome;
	}

	public void setSaleCottonIncome(String saleCottonIncome) {
		this.saleCottonIncome = saleCottonIncome;
	}

	public String getSoilPreparation() {
		return soilPreparation;
	}

	public void setSoilPreparation(String soilPreparation) {
		this.soilPreparation = soilPreparation;
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

	public String getIcsName() {
		return icsName;
	}

	public void setIcsName(String icsName) {
		this.icsName = icsName;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getLandLabourCost() {
		return landLabourCost;
	}

	public void setLandLabourCost(String landLabourCost) {
		this.landLabourCost = landLabourCost;
	}

	public String getSowingLabourCost() {
		return sowingLabourCost;
	}

	public void setSowingLabourCost(String sowingLabourCost) {
		this.sowingLabourCost = sowingLabourCost;
	}

	public String getGapLabourCost() {
		return gapLabourCost;
	}

	public void setGapLabourCost(String gapLabourCost) {
		this.gapLabourCost = gapLabourCost;
	}

	public String getWeedLabourCost() {
		return weedLabourCost;
	}

	public void setWeedLabourCost(String weedLabourCost) {
		this.weedLabourCost = weedLabourCost;
	}

	public String getCultureLabourCost() {
		return cultureLabourCost;
	}

	public void setCultureLabourCost(String cultureLabourCost) {
		this.cultureLabourCost = cultureLabourCost;
	}

	public String getIrriLabourCost() {
		return irriLabourCost;
	}

	public void setIrriLabourCost(String irriLabourCost) {
		this.irriLabourCost = irriLabourCost;
	}

	public String getFertLabourcost() {
		return fertLabourcost;
	}

	public void setFertLabourcost(String fertLabourcost) {
		this.fertLabourcost = fertLabourcost;
	}

	public String getPestLabourCost() {
		return pestLabourCost;
	}

	public void setPestLabourCost(String pestLabourCost) {
		this.pestLabourCost = pestLabourCost;
	}

	public String getHarvestLabourCost() {
		return harvestLabourCost;
	}

	public void setHarvestLabourCost(String harvestLabourCost) {
		this.harvestLabourCost = harvestLabourCost;
	}

	public String getTransportCost() {
		return transportCost;
	}

	public void setTransportCost(String transportCost) {
		this.transportCost = transportCost;
	}

	public String getFuelCost() {
		return fuelCost;
	}

	public void setFuelCost(String fuelCost) {
		this.fuelCost = fuelCost;
	}

	public String getOtherCost() {
		return otherCost;
	}

	public void setOtherCost(String otherCost) {
		this.otherCost = otherCost;
	}

	public String getManureLabourCost() {
		return manureLabourCost;
	}

	public void setManureLabourCost(String manureLabourCost) {
		this.manureLabourCost = manureLabourCost;
	}

	public String getTotLandPreCst() {
		return totLandPreCst;
	}

	public void setTotLandPreCst(String totLandPreCst) {
		this.totLandPreCst = totLandPreCst;
	}

	public String getTotSowCst() {
		return totSowCst;
	}

	public void setTotSowCst(String totSowCst) {
		this.totSowCst = totSowCst;
	}

	public String getTotGapCst() {
		return totGapCst;
	}

	public void setTotGapCst(String totGapCst) {
		this.totGapCst = totGapCst;
	}

	public String getTotWeedCst() {
		return totWeedCst;
	}

	public void setTotWeedCst(String totWeedCst) {
		this.totWeedCst = totWeedCst;
	}

	public String getTotInputCst() {
		return totInputCst;
	}

	public void setTotInputCst(String totInputCst) {
		this.totInputCst = totInputCst;
	}

	public String getTotIrriCst() {
		return totIrriCst;
	}

	public void setTotIrriCst(String totIrriCst) {
		this.totIrriCst = totIrriCst;
	}

	public String getTotFerCst() {
		return totFerCst;
	}

	public void setTotFerCst(String totFerCst) {
		this.totFerCst = totFerCst;
	}

	public String getTotPestCst() {
		return totPestCst;
	}

	public void setTotPestCst(String totPestCst) {
		this.totPestCst = totPestCst;
	}

	public String getTotManureCst() {
		return totManureCst;
	}

	public void setTotManureCst(String totManureCst) {
		this.totManureCst = totManureCst;
	}

	public String getTotHarvCst() {
		return totHarvCst;
	}

	public void setTotHarvCst(String totHarvCst) {
		this.totHarvCst = totHarvCst;
	}

	public String getSamCode() {
		return samCode;
	}

	public void setSamCode(String samCode) {
		this.samCode = samCode;
	}	
	
	
}
