/*
 * ProcurementProductAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.traceability.ProcurementTraceability;
import com.ese.entity.traceability.ProcurementTraceabilityDetails;
import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.dao.IProductDistributionDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.view.WebTransactionAction;

@SuppressWarnings("unchecked")
public class ProcurementTraceabilityAction extends WebTransactionAction {

	private static final long serialVersionUID = 1L;
	public static final String PROCUREMENT_ENROLLMENT = "383";
	protected int page;
	protected int rows;
	protected String sidx;
	protected String sord;
	private String id;
	private String selectedGroup;
	private String selectedVillage;
	private String selectedProduct;
	private String selectedVariety;
	private String selectedGrade;
	private String selectedFarmer;
	private String selectedCrop;
	private String selectedStripTestType;
	private String selectedTrashValue;
	private String selectedMoistureValue;
	private String selectedStableValue;
	private String selectedKowdiKapasValue;
	private String selectedYellowCottonValue;
	private String selectedProcurementCenter;
	private String selectedMobileUser;
	private String totalAmount;
	private String finalAmount;
	private String paymentAmount;
	private String seasonId;
	private String selectedWarehouse;
	private String selectedDate;
	private String selectedLatitude;
	private String selectedLongitude;
	private String productTotalString;
	private String farmerName;
	private String procurementId;
	private ProcurementTraceability procurement;
	private String productAvailableUnit;
	private String procurementDetailArray;
	private String procurementDate;
	private String selectedCity;
	private ILocationService locationService;
	private IFarmerService farmerService;
	private IProductDistributionService productDistributionService;
	private IAccountService accountService;
	@Autowired
	private IProductDistributionDAO productDistributionDAO;
	@Autowired
	private IUniqueIDGenerator idGenerator;
	private String stripImage;
	private List<ProcurementVariety> varietyList = new ArrayList<ProcurementVariety>();
	Map<Integer, String> stripList = new LinkedHashMap<Integer, String>();
	List<Warehouse> samithi = new ArrayList<Warehouse>();

	@SuppressWarnings("rawtypes")
	public String data() throws Exception {
		getJQGridRequestParam();
		filter = new ProcurementTraceability();
		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		return sendJQGridJSONResponse(data);
	}

	public JSONObject toJSON(Object obj) {
		try {
			ProcurementTraceability procurement = (ProcurementTraceability) obj;
			JSONObject jsonObject = new JSONObject();
			JSONArray rows = new JSONArray();

			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(!StringUtil
							.isEmpty(getBranchesMap().get(getParentBranchMap().get(procurement.getBranchId())))
									? getBranchesMap().get(getParentBranchMap().get(procurement.getBranchId()))
									: getBranchesMap().get(procurement.getBranchId()));
				}
				rows.add(getBranchesMap().get(procurement.getBranchId()));
			} else {
				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(branchesMap.get(procurement.getBranchId()));
				}
			}

			DateFormat genDate = new SimpleDateFormat(
					preferncesService.findPrefernceByName(ESESystem.GENERAL_DATE_FORMAT));
			rows.add(String.valueOf(genDate.format(procurement.getAgroTransaction().getTxnTime())));

			rows.add(!StringUtil.isEmpty(procurement.getSeason()) ? procurement.getSeason() : "");

			rows.add(procurement.getFarmer() != null ? procurement.getFarmer().getFarmerCodeAndName()
					: procurement.getAgroTransaction().getFarmerName() != null
							? procurement.getAgroTransaction().getFarmerName() : "N/A");

			rows.add(CurrencyUtil.getDecimalFormat(procurement.getTotalProVal(), "##.000"));

			jsonObject.put("id", procurement.getId());
			jsonObject.put("cell", rows);
			return jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String create() {
		return INPUT;
	}

	public String detail() throws Exception {
		return DETAIL;
	}

	public String update() throws Exception {
		if (!StringUtil.isEmpty((String) this.id)) {
		}
		return "update";
	}

	public Map<String, String> getListshg() {
		Map<String, String> shgMap = new HashMap<>();
		List<Object[]> shgList = locationService.listOfGroup();
		for (Object[] obj : shgList) {
			shgMap.put(String.valueOf(obj[2]), String.valueOf(obj[1]));
		}
		return shgMap;
	}

	public Map<Long, String> getListProcurementCenter() {

		List<Warehouse> procurementCenterList = locationService.listProcurementCenter();

		Map<Long, String> procurementCenterDropDownList = new LinkedHashMap<Long, String>();
		for (Warehouse warehouse : procurementCenterList) {
			procurementCenterDropDownList.put(warehouse.getId(), warehouse.getName());
		}
		return procurementCenterDropDownList;
	}

	public Map<Long, String> getListMunicipality() {

		return locationService.listMunicipality().stream()
				.collect(Collectors.toMap(Municipality::getId, mu -> String.join(" ", mu.getName(), "")));
	}

	public List<ProcurementProduct> getProductList() {
		List<ProcurementProduct> listProduct = productDistributionService
				.listProcurementProductByType(Procurement.productType.GOODS.ordinal());
		return listProduct;
	}

	public void populateFarmer() throws Exception {
		if (!selectedGroup.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedGroup))) {
			List<Object[]> listFarmer = farmerService.listFarmerBySamithi(Long.valueOf(selectedGroup));

			JSONArray farmerArr = new JSONArray();
			if (!ObjectUtil.isEmpty(listFarmer)) {
				for (Object[] farmer : listFarmer) {
					farmerArr.add(getJSONObject(String.valueOf(farmer[3]),
							String.valueOf(farmer[1]) + " " + String.valueOf(farmer[2])));
				}
			}
			sendAjaxResponse(farmerArr);
		}
		else
			if(!selectedVillage.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedVillage))) {
			List<Farmer> listFarmer = farmerService.listActiveFarmersByVillageCode(String.valueOf(selectedVillage));

			JSONArray farmerArr = new JSONArray();
			if (!ObjectUtil.isEmpty(listFarmer)) {
				for (Farmer farmer : listFarmer) {
					farmerArr.add(getJSONObject(String.valueOf(farmer.getId()),
							String.valueOf(farmer.getFirstName()) + " " + String.valueOf(farmer.getLastName())));
				}
			}
			sendAjaxResponse(farmerArr);
		}
		
	}

	public void populateIcs() throws Exception {
		if (!selectedFarmer.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedFarmer))) {
			JSONArray icsArr = new JSONArray();
			List<Object[]> listIcs = farmerService.listIcNameByFarmer(selectedFarmer);
				if (!ObjectUtil.isListEmpty(listIcs)) {
				for (Object ics : listIcs) {
					if (!ObjectUtil.isEmpty(ics)) {
						FarmCatalogue cat = getCatlogueValueByCode(ics.toString());
						if(!ObjectUtil.isEmpty(cat))
						icsArr.add(getJSONObject("icsName", cat.getName()));
					}
				}}
			List<Object[]> icsStatus = farmerService.listIcsStatusByFarmer(selectedFarmer);
			if (!ObjectUtil.isListEmpty(icsStatus)) {
				for (Object status : icsStatus) {
					if (!ObjectUtil.isEmpty(status)) {
						icsArr.add(getJSONObject("icsStatus", getLocaleProperty("icsStatus" + status.toString())));
					}
				}}
			List<Object[]> cooperative=farmerService.listcooperativeByFarmer(selectedFarmer);
			if (!ObjectUtil.isListEmpty(cooperative)) {
				for (Object status : cooperative) {
					if (!ObjectUtil.isEmpty(status)) {
						FarmCatalogue cat = getCatlogueValueByCode(status.toString());
						if(!ObjectUtil.isEmpty(cat)){
						icsArr.add(getJSONObject("icsCoop", cat.getName()));
					}
					}}
					}
			sendAjaxResponse(icsArr);
	  }
	}

	public void populateVariety() throws Exception {

		if (!StringUtil.isEmpty(selectedProduct)) {
			List<ProcurementVariety> varietiesList = productDistributionService
					.listProcurementVarietyByProcurementProductId(Long.valueOf(selectedProduct));
			JSONArray varietyArr = new JSONArray();
			varietiesList.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
				varietyArr.add(getJSONObject(obj.getId(), obj.getName()));
			});
			sendAjaxResponse(varietyArr);
		}

	}

	public void populateGrade() throws Exception {

		if (!StringUtil.isEmpty(selectedVariety)) {
			ProcurementVariety varierty = productDistributionService
					.findProcurementVariertyById(Long.valueOf(selectedVariety.trim()));

			if (!ObjectUtil.isEmpty(varierty)) {
				List<ProcurementGrade> gradeList = productDistributionService
						.listProcurementGradeByProcurementVarietyId(Long.valueOf(varierty.getId()));

				JSONArray gradeArr = new JSONArray();

				gradeList.stream().forEach(obj -> {
					gradeArr.add(getJSONObject(obj.getId(), obj.getName()));
				});
				sendAjaxResponse(gradeArr);
			}
		}
	}

	public void populatePricePremium() throws Exception {
		if (!StringUtil.isEmpty(selectedCrop)) {
			JSONArray productArr = new JSONArray();
			ProcurementProduct crop = productDistributionService
					.findProcurementProductPrice(Long.valueOf(selectedCrop));
			if (!ObjectUtil.isEmpty(crop)) {
				productArr.add(getJSONObject("unit", crop.getUnit()));
				productArr.add(getJSONObject("price", crop.getMspRate()));
				productArr.add(getJSONObject("premium", crop.getMspPercentage()));
			}
			sendAjaxResponse(productArr);
		}
	}

	public void populateProcurement() {

		String receiptNumber;
		//loadCurrentSeason();
		Set<ProcurementTraceabilityDetails> procurementDetails = formProcurementDetails();
		ProcurementTraceability procurement = new ProcurementTraceability();
		procurement.setProcurmentTraceabilityDetails(procurementDetails);
		procurement.setSeason(getCurrentSeasonsCode());
		procurement.setCreatedUser(getUsername());
		procurement.setCreatedDate(new Date());
		procurement.setProcurementDate(DateUtil.convertStringToDate(this.procurementDate, getGeneralDateFormat()));
		procurement.setBranchId(getBranchId());
		Warehouse warehouse = locationService.findWarehouseWithoutTypeById(Long.valueOf(selectedProcurementCenter));
		procurement.setWarehouse(warehouse);
		procurement.setAgroTransaction(formAgroTxn(procurement));
		Farmer farmer = farmerService.findFarmerById(Long.valueOf(getSelectedFarmer()));
		procurement.setFarmer(farmer);
		procurement.setStripPositive(
				!selectedStripTestType.equalsIgnoreCase("undefined") ? Integer.parseInt(selectedStripTestType) : -1);
		procurement.setPaymentAmount(StringUtil.isDouble(getPaymentAmount()) ? Double.valueOf(getPaymentAmount()) : 0D);
		if(!stripImage.equalsIgnoreCase("undefined")){
		procurement.setStripImage(FileUtil.getBinaryFileContent(stripImage));}
		procurement.setReceiptNo(procurement.getAgroTransaction().getReceiptNo());
		procurement.setTrash(!StringUtil.isEmpty(selectedTrashValue) ? selectedTrashValue : "");
		procurement.setMoisture(!StringUtil.isEmpty(selectedMoistureValue) ? selectedMoistureValue : "");
		procurement.setStapleLen(!StringUtil.isEmpty(selectedStableValue) ? selectedStableValue : "");
		procurement.setKowdi_kapas(!StringUtil.isEmpty(selectedKowdiKapasValue) ? selectedKowdiKapasValue : "");
		procurement.setYellow_cotton(!StringUtil.isEmpty(selectedYellowCottonValue) ? selectedYellowCottonValue : "");
		procurement.setRevNo(DateUtil.getRevisionNumber());
		procurement.setBranchId(getBranchId());
		procurement.setTotalProVal(!StringUtil.isEmpty(finalAmount) ? Double.valueOf(finalAmount) : 0D);
		processProcurement(procurement);
		String receipNo = procurement.getAgroTransaction().getReceiptNo();
		productDistributionService.addProcurementTraceability(procurement);

	
		JSONArray respArr = new JSONArray();
		respArr.add(getJSONObject("data", "success"));
		respArr.add(getJSONObject("receiptNumber", receipNo));
		sendAjaxResponse(respArr);
	}

	private AgroTransaction processProcurement(ProcurementTraceability proc) {
		AgroTransaction txn = new AgroTransaction();
		txn.setDeviceId(NOT_APPLICABLE);
		txn.setDeviceName(NOT_APPLICABLE);
		txn.setBranch_id(proc.getBranchId());
		if (!StringUtil.isEmpty(this.procurementDate)) {
			try {
				txn.setTxnTime(DateUtil.convertStringToDate(this.procurementDate, getGeneralDateFormat()));
			} catch (Exception e) {
				String date = DateUtil.convertDateToString(new Date(), getGeneralDateFormat());
				txn.setTxnTime(DateUtil.convertStringToDate(date, getGeneralDateFormat()));
			}
		}
		txn.setTxnType(PROCUREMENT_ENROLLMENT);
		txn.setProfType(Profile.CLIENT);
		txn.setOperType(ESETxn.ON_LINE);
		txn.setTxnDesc(ProcurementTraceability.PROCURMEMENT);

		Double bal;
		if (!ObjectUtil.isEmpty(proc.getFarmer()) && !StringUtil.isEmpty(proc.getFarmer().getFarmerId())) {
			ESEAccount farmerAccount = productDistributionDAO.findESEAccountByProfileId(proc.getFarmer().getFarmerId(),
					ESEAccount.CONTRACT_ACCOUNT);
			bal = farmerAccount.getCashBalance();
			txn.setIntBalance(bal);
			txn.setTxnAmount(StringUtil.isDouble(getTotalAmount()) ? Double.valueOf(getTotalAmount()) : 0D);
			txn.setBalAmount(bal + proc.getTotalProVal());
			txn.setAccount(farmerAccount);
			txn.setPaidAmount(StringUtil.isDouble(getPaymentAmount()) ? Double.valueOf(getPaymentAmount()) : 0D);
		} else {
			txn.setIntBalance(0.00);
			txn.setTxnAmount(StringUtil.isDouble(getTotalAmount()) ? Double.valueOf(getTotalAmount()) : 0D);
			txn.setBalAmount(0.00);
		}

		Warehouse warehouse = locationService.findWarehouseWithoutTypeById(Long.valueOf(selectedProcurementCenter));
		txn.setWarehouse(warehouse);
		txn.setSamithi(warehouse);

		if (!StringUtil.isEmpty(selectedMobileUser)) {
			Agent agent = agentService.findAgent(Long.valueOf(selectedMobileUser));
			txn.setAgentId(agent.getProfileId());
			txn.setAgentName(agent.getPersonalInfo().getAgentName());
		} else {
			txn.setAgentId(null);
			txn.setAgentName(null);
		}

		Farmer farmer = null;
		if (!StringUtil.isEmpty(selectedFarmer)) {
			farmer = farmerService.findFarmerById(Long.valueOf(selectedFarmer));
		}

		if (!ObjectUtil.isEmpty(farmer)) {
			txn.setFarmerId(farmer.getFarmerId());
			txn.setFarmerName(farmer.getFirstName() + " " + farmer.getLastName());
		} else {
			txn.setFarmerId(NOT_APPLICABLE);
			txn.setFarmerName(getFarmerName());
		}
		txn.setReceiptNo(proc.getAgroTransaction().getReceiptNo());
		txn.setSeasonCode(proc.getSeason());
		productDistributionService.saveAgroTransaction(txn);

		return txn;
	}

	private AgroTransaction formAgroTxn(ProcurementTraceability procurement) {
		AgroTransaction agroTransaction = new AgroTransaction();
		agroTransaction.setDeviceId(NOT_APPLICABLE);
		agroTransaction.setDeviceName(NOT_APPLICABLE);
		agroTransaction.setBranch_id(procurement.getBranchId());
/*		if (!StringUtil.isEmpty(this.procurementDate)) {
			try {
				agroTransaction.setTxnTime(DateUtil.convertStringToDate(this.procurementDate, getGeneralDateFormat()));
			} catch (Exception e) {*/
				String nowDate = DateUtil.convertDateToString(new Date(), getGeneralDateFormat());
				agroTransaction.setTxnTime(DateUtil.convertStringToDate(nowDate, getGeneralDateFormat()));
	/*		}
		}*/
		agroTransaction.setTxnType(PROCUREMENT_ENROLLMENT);
		agroTransaction.setProfType(Profile.CLIENT);
		agroTransaction.setOperType(ESETxn.ON_LINE);
		agroTransaction.setTxnDesc(Procurement.PROCUREMENT_AMOUNT);
		agroTransaction.setTxnAmount(getTransactionAmount(procurement));

		Warehouse warehouse = locationService.findWarehouseWithoutTypeById(Long.valueOf(selectedProcurementCenter));
		agroTransaction.setWarehouse(warehouse);
		agroTransaction.setSamithi(warehouse);

		if (!StringUtil.isEmpty(selectedMobileUser)) {
			Agent agent = agentService.findAgent(Long.valueOf(selectedMobileUser));
			agroTransaction.setAgentId(agent.getProfileId());
			agroTransaction.setAgentName(agent.getPersonalInfo().getAgentName());
		} else {
			agroTransaction.setAgentId(null);
			agroTransaction.setAgentName(null);
		}

		Farmer farmer = null;
		if (!StringUtil.isEmpty(selectedFarmer)) {
			farmer = farmerService.findFarmerById(Long.valueOf(selectedFarmer));
		}

		if (!ObjectUtil.isEmpty(farmer)) {
			agroTransaction.setFarmerId(farmer.getFarmerId());
			agroTransaction.setFarmerName(farmer.getFirstName() + " " + farmer.getLastName());
		} else {
			agroTransaction.setFarmerId(NOT_APPLICABLE);
			agroTransaction.setFarmerName(getFarmerName());
		}
		agroTransaction.setReceiptNo(idGenerator.getProcurementTraceabilityReceiptNoSeq());

		return agroTransaction;
	}

	private double getTransactionAmount(ProcurementTraceability procurement) {
		double txnAmount = 0;
		if (!ObjectUtil.isEmpty(procurement)
				&& !ObjectUtil.isListEmpty(procurement.getProcurmentTraceabilityDetails())) {
			for (ProcurementTraceabilityDetails procurementDetail : procurement.getProcurmentTraceabilityDetails()) {
				txnAmount = txnAmount + procurementDetail.getTotalPrice();
			}
		}
		return txnAmount;
	}

	private Set<ProcurementTraceabilityDetails> formProcurementDetails() {

		Set<ProcurementTraceabilityDetails> procurementDetails = new LinkedHashSet<>();

		if (!StringUtil.isEmpty(getProductTotalString())) {
			List<String> productsList = Arrays.asList(getProductTotalString().split("@"));

			productsList.stream().filter(obj -> !StringUtil.isEmpty(obj)).forEach(products -> {
				ProcurementTraceabilityDetails procurementDetail = new ProcurementTraceabilityDetails();
				List<String> list = Arrays.asList(products.split("#"));

				ProcurementGrade grade = productDistributionService.findProcurementGradeById(Long.valueOf(list.get(2)));
				procurementDetail.setProcuremntGrade(grade);
				procurementDetail.setProcurementProduct(grade.getProcurementVariety().getProcurementProduct());
				procurementDetail.setPrice(Double.valueOf(list.get(5)));
				procurementDetail.setUnit(list.get(4));
				procurementDetail.setTotalPrice(Double.valueOf(list.get(9)));
				procurementDetail.setNetWeight(Double.valueOf(list.get(6)));
				procurementDetail.setNumberOfBags(Long.valueOf(list.get(3)));
				procurementDetail.setPremiumPrice(Double.valueOf(list.get(7)));
				procurementDetail.setTotalPricepremium(Double.valueOf(list.get(8)));
				procurementDetails.add(procurementDetail);
			});

		}

		return procurementDetails;
	}

	public String getCurrentDate() {

		Calendar currentDate = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat(getGeneralDateFormat());
		return df.format(DateUtil.setTimeToDate(new Date()));
	}

	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object code, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", code);
		jsonObject.put("name", name);
		return jsonObject;
	}

	public ILocationService getLocationService() {
		return locationService;
	}

	public void setLocationService(ILocationService locationService) {
		this.locationService = locationService;
	}

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	public IProductDistributionService getProductDistributionService() {
		return productDistributionService;
	}

	public void setProductDistributionService(IProductDistributionService productDistributionService) {
		this.productDistributionService = productDistributionService;
	}

	public IAccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}

	public String getSelectedGroup() {
		return selectedGroup;
	}

	public void setSelectedGroup(String selectedGroup) {
		this.selectedGroup = selectedGroup;
	}
    
	public String getSelectedVillage() {
		return selectedVillage;
	}

	public void setSelectedVillage(String selectedVillage) {
		this.selectedVillage = selectedVillage;
	}

	public String getSelectedProduct() {
		return selectedProduct;
	}

	public void setSelectedProduct(String selectedProduct) {
		this.selectedProduct = selectedProduct;
	}

	public String getSelectedVariety() {
		return selectedVariety;
	}

	public void setSelectedVariety(String selectedVariety) {
		this.selectedVariety = selectedVariety;
	}

	public String getSelectedGrade() {
		return selectedGrade;
	}

	public void setSelectedGrade(String selectedGrade) {
		this.selectedGrade = selectedGrade;
	}

	public String getSelectedWarehouse() {
		return selectedWarehouse;
	}

	public void setSelectedWarehouse(String selectedWarehouse) {
		this.selectedWarehouse = selectedWarehouse;
	}

	public String getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(String selectedDate) {
		this.selectedDate = selectedDate;
	}

	public String getSelectedFarmer() {
		return selectedFarmer;
	}

	public void setSelectedFarmer(String selectedFarmer) {
		this.selectedFarmer = selectedFarmer;
	}

	public String getProductTotalString() {
		return productTotalString;
	}

	public void setProductTotalString(String productTotalString) {
		this.productTotalString = productTotalString;
	}

	public String getFarmerName() {
		return farmerName;
	}

	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
	}

	public String getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(String seasonId) {
		this.seasonId = seasonId;
	}

	public String getProcurementDate() {
		return procurementDate;
	}

	public void setProcurementDate(String procurementDate) {
		this.procurementDate = procurementDate;
	}

	public String getProcurementId() {
		return this.procurementId;
	}

	public void setProcurementId(String procurementId) {
		this.procurementId = procurementId;
	}

	public String getProcurementDetailArray() {

		return procurementDetailArray;
	}

	public void setProcurementDetailArray(String procurementDetailArray) {

		this.procurementDetailArray = procurementDetailArray;
	}

	public List<ProcurementVariety> getVarietyList() {

		return varietyList;
	}

	public void setVarietyList(List<ProcurementVariety> varietyList) {

		this.varietyList = varietyList;
	}

	public IProductDistributionDAO getProductDistributionDAO() {
		return productDistributionDAO;
	}

	public void setProductDistributionDAO(IProductDistributionDAO productDistributionDAO) {
		this.productDistributionDAO = productDistributionDAO;
	}

	public String getProductAvailableUnit() {
		return productAvailableUnit;
	}

	public void setProductAvailableUnit(String productAvailableUnit) {
		this.productAvailableUnit = productAvailableUnit;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<FarmCatalogue> getListUom() {
		List<FarmCatalogue> subUomList = catalogueService.listCataloguesByUnit();

		return subUomList;
	}

	public Map<Integer, String> getStripList() {
		stripList.put(1, getText("positive"));
		stripList.put(2, getText("negative"));
		return stripList;
	}

	public void setStripList(Map<Integer, String> stripList) {
		this.stripList = stripList;
	}

	public ProcurementTraceability getProcurementTraceability() {
		return procurement;
	}

	public void setProcurementTraceability(ProcurementTraceability procurementTraceability) {
		this.procurement = procurement;
	}

	private void loadCurrentSeason() {

		ESESystem preference = preferncesService.findPrefernceById(ESESystem.SYSTEM_SWITCH);
		if (!ObjectUtil.isEmpty(preference)) {
			String currentSeasonCode = preference.getPreferences().get(ESESystem.CURRENT_SEASON_CODE);
			if (!StringUtil.isEmpty(currentSeasonCode)) {
				Season currentSeason = productDistributionService.findSeasonBySeasonCode(currentSeasonCode);
				if (!ObjectUtil.isEmpty(currentSeason))
					seasonId = currentSeason.getCode();
			}
		}
	}

	public String getSelectedLatitude() {
		return selectedLatitude;
	}

	public void setSelectedLatitude(String selectedLatitude) {
		this.selectedLatitude = selectedLatitude;
	}

	public String getSelectedLongitude() {
		return selectedLongitude;
	}

	public void setSelectedLongitude(String selectedLongitude) {
		this.selectedLongitude = selectedLongitude;
	}

	public String getSelectedCrop() {
		return selectedCrop;
	}

	public void setSelectedCrop(String selectedCrop) {
		this.selectedCrop = selectedCrop;
	}

	public String getSelectedStripTestType() {
		return selectedStripTestType;
	}

	public void setSelectedStripTestType(String selectedStripTestType) {
		this.selectedStripTestType = selectedStripTestType;
	}

	public String getSelectedTrashValue() {
		return selectedTrashValue;
	}

	public void setSelectedTrashValue(String selectedTrashValue) {
		this.selectedTrashValue = selectedTrashValue;
	}

	public String getSelectedMoistureValue() {
		return selectedMoistureValue;
	}

	public void setSelectedMoistureValue(String selectedMoistureValue) {
		this.selectedMoistureValue = selectedMoistureValue;
	}

	public String getSelectedStableValue() {
		return selectedStableValue;
	}

	public void setSelectedStableValue(String selectedStableValue) {
		this.selectedStableValue = selectedStableValue;
	}

	public String getSelectedKowdiKapasValue() {
		return selectedKowdiKapasValue;
	}

	public void setSelectedKowdiKapasValue(String selectedKowdiKapasValue) {
		this.selectedKowdiKapasValue = selectedKowdiKapasValue;
	}

	public String getSelectedYellowCottonValue() {
		return selectedYellowCottonValue;
	}

	public void setSelectedYellowCottonValue(String selectedYellowCottonValue) {
		this.selectedYellowCottonValue = selectedYellowCottonValue;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public IUniqueIDGenerator getIdGenerator() {
		return idGenerator;
	}

	public void setIdGenerator(IUniqueIDGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	public String getSelectedProcurementCenter() {
		return selectedProcurementCenter;
	}

	public void setSelectedProcurementCenter(String selectedProcurementCenter) {
		this.selectedProcurementCenter = selectedProcurementCenter;
	}

	public String getSelectedMobileUser() {
		return selectedMobileUser;
	}

	public void setSelectedMobileUser(String selectedMobileUser) {
		this.selectedMobileUser = selectedMobileUser;
	}

	public String getStripImage() {
		return stripImage;
	}

	public void setStripImage(String stripImage) {
		this.stripImage = stripImage;
	}

	public ProcurementTraceability getProcurement() {
		return procurement;
	}

	public void setProcurement(ProcurementTraceability procurement) {
		this.procurement = procurement;
	}

	public String getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(String finalAmount) {
		this.finalAmount = finalAmount;
	}

	public String getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public void setFilter(ProcurementTraceability filter) {
		this.filter = filter;
	}
	public String getSelectedCity() {
		return selectedCity;
	}

	public void setSelectedCity(String selectedCity) {
		this.selectedCity = selectedCity;
	}

	public Map<String, String> getCities() {
		Map<String, String> citiesMap = new LinkedHashMap<>();
		List<Object[]> cityCodeAndName = locationService.listCityCodeAndName();
		cityCodeAndName.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			citiesMap.put(objArr[0].toString(), objArr[0].toString() + "-" + objArr[1].toString());
			// citiesMap.put(objArr[0].toString(),objArr[1].toString());
		});
		return citiesMap;
	}
	public void populateVillageByCity() throws Exception {
		List<Object[]> villageList = new LinkedList<>();
		if (!selectedCity.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedCity))
				&& !selectedCity.equalsIgnoreCase("0")) {
			villageList = locationService.listVillageCodeAndNameByCityCode(selectedCity);
		}
		JSONArray villageArr = new JSONArray();
		if (!ObjectUtil.isEmpty(villageList)) {
			/*
			 * for (Village village : villages) {
			 * villageArr.add(getJSONObject(village.getId(),
			 * village.getName())); }
			 */
			villageList.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
				Object[] objArr = (Object[]) obj;
				villageArr.add(getJSONObject(objArr[0].toString(), objArr[0].toString() + "-" + objArr[1].toString()));
				// villageArr.add(getJSONObject(objArr[0].toString(),objArr[1].toString()));
			});
		}
		sendAjaxResponse(villageArr);

	}

		
}
