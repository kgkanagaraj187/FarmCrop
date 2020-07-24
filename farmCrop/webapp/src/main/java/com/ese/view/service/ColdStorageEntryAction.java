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

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.dao.IProductDistributionDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.AgentType;
import com.sourcetrace.eses.entity.ColdStorage;
import com.sourcetrace.eses.entity.ColdStorageDetail;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.entity.WarehouseStorageMap;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.CityWarehouse;
import com.sourcetrace.eses.order.entity.txn.PaymentLedger;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.order.entity.txn.ProcurementDetail;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicData;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicFieldsValue;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.view.WebTransactionAction;

@SuppressWarnings("unchecked")
public class ColdStorageEntryAction extends WebTransactionAction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ColdStorageEntryAction.class);

	public static final String PROCUREMENT_PRODUCT_ENROLLMENT = "316";
	public static final String TRIP_SHEET = "TRIP_SHEET";
	public static final String PROCUREMENT_PRODUCT = "PROCUREMENT_PRODUCT";
	public static final String AGENT = "AGENT";
	public static final String FARMER_ACCOUNTS = "FARMER_ACCOUNTS";
	public static final String PROCUREMENTS = "PROCUREMENTS";
	public static final String REGISTERED_FARMER = "0";
	public static final String un_REGISTERED_FARMER = "1";
	private static final String GRADE_IDS = "gradeId";
	public static final String EMPTY_QTY = "0";
	protected int page;
	protected int rows;
	protected String sidx;
	protected String sord;
	protected Date sDate = null;
	protected Date eDate = null;
	private String selectedCity;
	private String selectedVillage;
	private String selectedProduct;
	private String selectedVariety;
	private String selectedGrade;
	private String selectedPro;
	private String selectedVar;

	private Long seasonId;
	private static final String NO_OF_BAGS = "bags";
	private static final String GROSS_WEIGHT = "grossWt";
	private static final String NET_WEIGHT = "netWt";
	private static final Object GRADE_ID = null;

	private String selectedMobileUser;
	private String selectedWarehouse;
	private String selectedDate;
	private String selectedFarmerType;
	private String selectedFarmer;
	private String productTotalString;
	private String selectedSupplier;
	private String selectedMasterType;
	private String selectedBuyer;
	private String totalNoOfBag;
	private String totalQtyLabel;
	private String batchNo;
	private String bondNo;
	private String coldStorageName;
	private String selectedBatchNo;
	private String dynamic_field_value_id;
	private String bondStatus;
	private String coldStorageId;

	public String getSelectedBuyer() {
		return selectedBuyer;
	}

	public void setSelectedBuyer(String selectedBuyer) {
		this.selectedBuyer = selectedBuyer;
	}

	private String farmerType;
	private String farmerName;
	private String mobileNumber;
	private String paymentAmount;
	private String totalAmount;
	private String enableSupplier;
	private String enableBuyer;
	private String selectedFarmerValue;
	private ESEAccount account;
	private String cashCreditValue;

	/** AWI FIELDS */
	private String roadMapCode;
	private String vehicleNo;
	private String farmerAttnce;
	private String substituteName;
	private String selectedFarm;
	private String supplierMaster;
	private String supplierType;
	private String procurementId;
	private String procurementDetailArray;
	private List<ProcurementVariety> varietyList = new ArrayList<ProcurementVariety>();
	private String enableTraceability;
	private String selectedUom;
	private String enableStorage;
	private String roundOfHarvesting;
	private List<FarmCatalogue> subUomList = new ArrayList<FarmCatalogue>();
	/** Service Injection */
	private ILocationService locationService;
	private IFarmerService farmerService;
	private IProductDistributionService productDistributionService;
	private IAccountService accountService;
	@Autowired
	private IUniqueIDGenerator idGenerator;
	@Autowired
	private IPreferencesService preferncesService;
	@Autowired
	private IProductDistributionDAO productDistributionDAO;
	@Autowired
	private IProductService productService;
	private String id;
	private Procurement procurement;
	private String productAvailableUnit;
	private String productAvailableUnitCode;
	private String selectedCropType;
	private Double transport_cost;
	private String vehicleType;
	private String selectedColdStorageName;
	private String selectedBlockName;
	private String selectedFloorName;
	private String selectedBayNumber;
	private File imgFile;

	List<Warehouse> samithi = new ArrayList<Warehouse>();

	public Procurement getProcurement() {
		return procurement;
	}

	public void setProcurement(Procurement procurement) {
		this.procurement = procurement;
	}

	CityWarehouse cityWarehouse;

	public CityWarehouse getCityWarehouse() {
		return cityWarehouse;
	}

	public void setCityWarehouse(CityWarehouse cityWarehouse) {
		this.cityWarehouse = cityWarehouse;
	}

	private ProcurementProduct procurementProduct;

	public ProcurementProduct getProcurementProduct() {
		return procurementProduct;
	}

	public void setProcurementProduct(ProcurementProduct procurementProduct) {
		this.procurementProduct = procurementProduct;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String procurementDate;
	private List<MasterData> masterDataList = new LinkedList<>();

	private Map<Integer, String> masterTypeList = new HashMap<Integer, String>();
	private Map<String, String> farmerAttence = new HashMap<>();

	@SuppressWarnings("rawtypes")
	public String data() throws Exception {
		getJQGridRequestParam();
		filter = new ColdStorage();
		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		return sendJQGridJSONResponse(data);
	}

	public JSONObject toJSON(Object obj) {
		try {
			ColdStorage coldStorage = (ColdStorage) obj;
			JSONObject jsonObject = new JSONObject();
			JSONArray rows = new JSONArray();
			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(!StringUtil
							.isEmpty(getBranchesMap().get(getParentBranchMap().get(coldStorage.getBranchId())))
									? getBranchesMap().get(getParentBranchMap().get(coldStorage.getBranchId()))
									: getBranchesMap().get(coldStorage.getBranchId()));
				}
				rows.add(getBranchesMap().get(coldStorage.getBranchId()));
			} else {
				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(branchesMap.get(coldStorage.getBranchId()));
				}
			}
			DateFormat genDate = new SimpleDateFormat(
					preferncesService.findPrefernceByName(ESESystem.GENERAL_DATE_FORMAT));
			rows.add(String.valueOf(genDate.format(coldStorage.getCreatedDate())));
			rows.add(!ObjectUtil.isEmpty(coldStorage.getFarmer())
					? coldStorage.getFarmer().getFirstName() + " - " + coldStorage.getFarmer().getLastName() : "NA");
			rows.add(!ObjectUtil.isEmpty(coldStorage.getWarehouse()) ? coldStorage.getWarehouse().getName() : "NA");
			FarmCatalogue farmCatalogue = getCatlogueValueByCode(coldStorage.getColdStorageName());
			rows.add(!ObjectUtil.isEmpty(farmCatalogue) ? farmCatalogue.getName() : "NA");
			if (this.getCurrentTenantId().equalsIgnoreCase("griffith")) {
				rows.add(!StringUtil.isEmpty(coldStorage.getBatchNo()) ? coldStorage.getBatchNo() : "");

				rows.add(!StringUtil.isEmpty(coldStorage.getBondStatus())
						? getLocaleProperty("bondStatus-" + coldStorage.getBondStatus()) : "");
			} else {
				rows.add(!StringUtil.isEmpty(coldStorage.getBatchNo()) ? coldStorage.getBatchNo() : "");
			}

			rows.add(!StringUtil.isEmpty(coldStorage.getBondNo()) ? coldStorage.getBondNo() : "");
			rows.add(!ObjectUtil.isEmpty(coldStorage.getTotalBags()) ? coldStorage.getTotalBags() : "");
			rows.add(!ObjectUtil.isEmpty(coldStorage.getTotalQty()) ? coldStorage.getTotalQty() : "");
			if (this.getCurrentTenantId().equalsIgnoreCase("griffith")) {
				if (coldStorage.getBondStatus()
						.equalsIgnoreCase(String.valueOf(ColdStorage.BONDSTATUS.AWAITING.ordinal()))) {
					rows.add(
							"<button style='border-radius: 10px;' type=button class='btn btn-info btn-sg' data-toggle=modal onclick=populateModalWindow(this) data-target=#myModal data-backend-values="
									+ coldStorage.getBondStatus() + "," + coldStorage.getId()
									+ ">  <span class='glyphicon glyphicon-edit'></span> </button>");
				} else {
					rows.add("");
				}
			}

			jsonObject.put("id", coldStorage.getId());
			jsonObject.put("cell", rows);
			return jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String create() {
		setEnableSupplier(preferncesService.findPrefernceByName(ESESystem.ENABLE_SUPPLIER));
		setEnableTraceability(preferncesService.findPrefernceByName(ESESystem.ENABLE_TRACEABILITY));
		setEnableBuyer(preferncesService.findPrefernceByName(ESESystem.ENABLE_BUYER));
		setEnableStorage(preferncesService.findPrefernceByName(ESESystem.ENABLE_STORAGE));

		return INPUT;
	}

	public String detail() throws Exception {
		setEnableTraceability(preferncesService.findPrefernceByName(ESESystem.ENABLE_TRACEABILITY));
		procurement = productDistributionService.findProcurementById(Long.valueOf(id));

		if (!ObjectUtil.isEmpty(procurement)) {
			for (ProcurementDetail procurementDetail : procurement.getProcurementDetails()) {
				if (!ObjectUtil.isEmpty(procurementDetail.getProcurementProduct())
						&& (!ObjectUtil.isEmpty(procurementDetail.getProcurementGrade()))) {
					cityWarehouse = productDistributionService.findByProdIdAndGradeCode(
							procurementDetail.getProcurementProduct().getId(),
							procurementDetail.getProcurementGrade().getCode(), getCurrentTenantId());

				}
			}

			this.setSupplierMaster(this.procurement.getProcMasterType() != null
					? this.getMasterTypeList().get(Integer.parseInt(this.procurement.getProcMasterType())) : "");

			if (!StringUtil.isEmpty((String) this.procurement.getProcMasterTypeId())) {
				if ("99".equalsIgnoreCase(this.procurement.getProcMasterType())
						|| "11".equalsIgnoreCase(this.procurement.getProcMasterType())) {
					Warehouse warehouse = locationService.findSamithiByCode(this.procurement.getProcMasterTypeId());
					this.setSupplierType(warehouse.getName());
				} else {
					MasterData masterData = this.clientService
							.findMasterDataById(Long.valueOf(Long.parseLong(this.procurement.getProcMasterTypeId())));
					this.setSupplierType(masterData.getName());
				}
			}
			if (!ObjectUtil.isEmpty(cityWarehouse) && !ObjectUtil.isEmpty(cityWarehouse.getCoOperative())) {

				if (!StringUtil.isEmpty(cityWarehouse.getCoOperative().getName())) {
					Warehouse warehouse = locationService
							.findCoOperativeByCode(cityWarehouse.getCoOperative().getCode());
					if (!ObjectUtil.isEmpty(warehouse))
						procurement.setWarehouseName(warehouse.getCode() + "-" + warehouse.getName());
				}
			}

			if (!StringUtil.isEmpty(procurement.getVehicleType())) {
				String catalogueCode[] = procurement.getVehicleType().split(",");
				String vehicleType = "";
				for (int i = 0; i < catalogueCode.length; i++) {
					FarmCatalogue farmCatalogue = getCatlogueValueByCode(catalogueCode[i].trim());
					if (!ObjectUtil.isEmpty(farmCatalogue)) {
						if (catalogueCode.length == i + 1) {
							vehicleType = vehicleType + farmCatalogue.getName();
						} else {
							vehicleType = vehicleType + farmCatalogue.getName() + ",";
						}

					}
				}

				setVehicleType(vehicleType);
			}

		} else {
			return LIST;
		}
		return DETAIL;
	}

	public void populateColdStorageEntry() {

		/** Form Procurement Detail Object */
		Set<ColdStorageDetail> coldStorageDetails = formColdStorageDetail();
		ColdStorage coldStorage = new ColdStorage();
		Farmer farmer=null;
		if (this.getImgFile() != null) {
			coldStorage.setImg(FileUtil.getBinaryFileContent(this.getImgFile()));
		}
		if(!StringUtil.isEmpty(selectedFarmer)){
			farmer = farmerService.findFarmerByFarmerId(selectedFarmer);
		}
		
		coldStorage.setFarmer(farmer);
		Warehouse warehouse = locationService.findWarehouseById(Long.valueOf(selectedWarehouse));
		coldStorage.setColdStorageDetail(coldStorageDetails);
		coldStorage.setWarehouse(warehouse);
		coldStorage.setColdStorageName(coldStorageName);
		
		FarmerDynamicData fdv = farmerService.findFarmerDynamicData(batchNo);
		if (fdv != null && fdv.getFarmerDynamicFieldsValues() != null) {
			FarmerDynamicFieldsValue variety = fdv.getFarmerDynamicFieldsValues().stream().anyMatch(
					f -> f.getFieldName().equalsIgnoreCase(getLocaleProperty("lotCode.fieldName"))) ? fdv.getFarmerDynamicFieldsValues().stream()
							.filter(f -> f.getFieldName().equalsIgnoreCase(getLocaleProperty("lotCode.fieldName")))
							.findFirst().get() : null;
			if (variety != null) {
				batchNo = variety.getFieldValue();
			}
		}
		coldStorage.setBatchNo(batchNo);
		coldStorage.setBondNo(bondNo);
		coldStorage.setBondStatus(bondStatus);
		coldStorage.setTotalBags(Integer.parseInt(totalNoOfBag));
		coldStorage.setTotalQty(Double.valueOf(totalQtyLabel));
		coldStorage.setCreatedUser(getUsername());
		coldStorage.setCreatedDate(DateUtil.convertStringToDate(this.procurementDate, getGeneralDateFormat()));
		coldStorage.setBranchId(getBranchId());
		coldStorage.setRevisionNo(String.valueOf(DateUtil.getRevisionNumber()));
		coldStorage.setRoundOfHarvesting(roundOfHarvesting);
		productDistributionService.addColdStorageEntry(coldStorage);

		JSONArray respArr = new JSONArray();
		respArr.add(getJSONObject("data", "success"));
		// respArr.add(getJSONObject("receiptNumber",
		// procurement.getAgroTransaction().getReceiptNo()));
		sendAjaxResponse(respArr);
	}

	public void populateFarmerAccBalance() throws Exception {

		String cash = "";
		if (!StringUtil.isEmpty(selectedFarmerValue)) {
			Farmer farmer = farmerService.findFarmerById(Long.valueOf(selectedFarmerValue));
			account = accountService.findAccountByProfileIdAndProfileType(farmer.getFarmerId(),
					ESEAccount.CONTRACT_ACCOUNT);
			if (!StringUtil.isEmpty(account)) {
				cashCreditValue = (account.getCashBalance() + "," + account.getCreditBalance());
				cash = cashCreditValue;
			} else {
				cashCreditValue = "";
			}
			cash = cashCreditValue;

		}
		response.getWriter().print(cash);

	}

	private Set<ColdStorageDetail> formColdStorageDetail() {
		Set<ColdStorageDetail> coldStorageDetails = new LinkedHashSet<>();

		if (!StringUtil.isEmpty(getProductTotalString())) {
			List<String> productsList = Arrays.asList(getProductTotalString().split("@"));

			productsList.stream().filter(obj -> !StringUtil.isEmpty(obj)).forEach(products -> {
				ColdStorageDetail coldStorageDetail = new ColdStorageDetail();
				List<String> list = Arrays.asList(products.split("#"));
			//	ProcurementGrade grade = productDistributionService.findProcurementGradeById((Long.valueOf(list.get(0))));
				ProcurementVariety variety=productDistributionService.findProcurementVariertyById((Long.valueOf(list.get(0))));
			//	coldStorageDetail.setProcurementGrade(grade);
				coldStorageDetail.setProcurementVariety(variety);
				coldStorageDetail.setBlockName(String.valueOf(list.get(1)));
				coldStorageDetail.setFloorName(String.valueOf(list.get(2)));
				coldStorageDetail.setBayNumber(String.valueOf(list.get(3)));
				coldStorageDetail.setNoOfBags(Integer.parseInt(list.get(5)));
				coldStorageDetail.setTxnQty(Double.valueOf(list.get(6)));

				coldStorageDetails.add(coldStorageDetail);
			});

		}

		return coldStorageDetails;
	}

	private double calculateNetWeight(double grossWeight, double tareWeight) {

		return grossWeight - tareWeight;
	}

	private double calculateSubTotal(double netWeight, double ratePerUnit) {
		return netWeight * ratePerUnit;
	}

	private double getTransactionAmount(Procurement procurement) {
		double txnAmount = 0;
		if (!ObjectUtil.isEmpty(procurement) && !ObjectUtil.isListEmpty(procurement.getProcurementDetails())) {
			for (ProcurementDetail procurementDetail : procurement.getProcurementDetails()) {
				txnAmount = txnAmount + procurementDetail.getSubTotal();
			}
		}
		return txnAmount;
	}

	private void loadCurrentSeason() {

		ESESystem preference = preferncesService.findPrefernceById(ESESystem.SYSTEM_SWITCH);
		if (!ObjectUtil.isEmpty(preference)) {
			String currentSeasonCode = preference.getPreferences().get(ESESystem.CURRENT_SEASON_CODE);
			if (!StringUtil.isEmpty(currentSeasonCode)) {
				Season currentSeason = productDistributionService.findSeasonBySeasonCode(currentSeasonCode);
				if (!ObjectUtil.isEmpty(currentSeason))
					seasonId = currentSeason.getId();
			}
		}
	}

	/** Default Drop Down values */
	public Map<String, String> getAgentLists() {

		Map<String, String> returnMap = new LinkedHashMap<String, String>();
		List<Agent> agentLists = (List<Agent>) agentService.listAgentByAgentType(AgentType.FIELD_STAFF);
		if (!ObjectUtil.isListEmpty(agentLists)) {
			for (Agent agent : agentLists) {
				returnMap.put(String.valueOf(agent.getId()),
						agent.getProfileId() + "" + agent.getPersonalInfo().getFirstName());
			}
		}

		return returnMap;
	}

	public Map<Long, String> getListWarehouse() {

		List<Warehouse> warehouseList = locationService.listWarehouse();

		Map<Long, String> warehouseDropDownList = new LinkedHashMap<Long, String>();
		for (Warehouse warehouse : warehouseList) {
			warehouseDropDownList.put(warehouse.getId(), warehouse.getName() + " -  " + warehouse.getCode());
		}
		return warehouseDropDownList;
	}

	public Map<Long, String> getListMunicipality() {

		return locationService.listMunicipality().stream()
				.collect(Collectors.toMap(Municipality::getId, mu -> String.join(" ", mu.getName(), mu.getCode())));
	}

	public List<ProcurementProduct> getProductList() {
		List<ProcurementProduct> listProduct = productDistributionService
				.listProcurementProductByType(Procurement.productType.GOODS.ordinal());
		return listProduct;
	}

	public Map<String, String> getRegType() {
		Map<String, String> regType = new LinkedHashMap<>();
		regType.put("0", getText("REG"));
		regType.put("1", getText("UNREG"));

		return regType;
	}

	public Map<String, String> getFarmerAttence() {

		farmerAttence.put("1", getText("ABSENT"));
		farmerAttence.put("0", getText("PRESENT"));

		return farmerAttence;
	}

	/** Populate Methods */
	public void populateVillage() {
		List<Village> villages = new ArrayList<>();
		if (!selectedCity.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedCity))
				&& !selectedCity.equalsIgnoreCase("0")) {
			villages = locationService.listVillagesByCityID(Long.valueOf(selectedCity));
		}
		JSONArray villageArr = new JSONArray();
		if (!ObjectUtil.isEmpty(villages)) {
			villages.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
				villageArr.add(getJSONObject(obj.getCode(), obj.getName() + "-" + obj.getCode()));
			});
		}
		sendAjaxResponse(villageArr);
	}

	public void populateColdStorageName() {
		List<WarehouseStorageMap> coldStorage = new ArrayList<>();
		if (!selectedWarehouse.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedWarehouse))
				&& !selectedWarehouse.equalsIgnoreCase("0")) {
			coldStorage = locationService.listWarehouseStorageMapByWarehouseID(Long.valueOf(selectedWarehouse));
		}
		JSONArray coldStorageArr = new JSONArray();
		if (!ObjectUtil.isEmpty(coldStorage)) {
			coldStorage.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
				FarmCatalogue catalogue = getCatlogueValueByCode(obj.getColdStorageName());
				coldStorageArr.add(getJSONObject(obj.getColdStorageName(), catalogue.getName()));
			});
		}
		sendAjaxResponse(coldStorageArr);
	}

	public void populateFarmer() throws Exception {
		if (!selectedVillage.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedVillage))) {
			List<Object[]> listFarmer = farmerService.listFarmerCodeIdNameByVillageCode(selectedVillage);

			JSONArray farmerArr = new JSONArray();
			listFarmer.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
				farmerArr.add(getJSONObject(obj[4], obj[2] + "" + (!StringUtil.isEmpty(obj[3]) ? (" " + obj[3]) : " ")
						+ (!StringUtil.isEmpty(obj[1]) ? ("-" + obj[1]) : " ")));
			});
			sendAjaxResponse(farmerArr);
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

	public void populateUnit() throws Exception {

		String result = "";
		String code = "";
		JSONArray productArr = new JSONArray();
		if (!StringUtil.isEmpty(selectedProduct)) {
			ProcurementProduct procurementProduct = productService.findUnitByCropId(Long.valueOf(selectedProduct));

			if (!ObjectUtil.isEmpty(procurementProduct) && procurementProduct.getTypes() != null) {
				productAvailableUnit = String.valueOf(procurementProduct.getTypes().getName());
				productAvailableUnitCode = String.valueOf(procurementProduct.getTypes().getName());
				result = productAvailableUnit;
			}

			result = productAvailableUnit == null ? "" : productAvailableUnit;
		}
		productArr.add(getJSONObject("unit", result));

		sendAjaxResponse(productArr);
	}

	public void getMaxBinHold() throws Exception {

		Double maxBayHold = 0.0;
		JSONArray productArr = new JSONArray();
		if (!StringUtil.isEmpty(selectedWarehouse) && !StringUtil.isEmpty(selectedColdStorageName)) {
			WarehouseStorageMap coldStorage = locationService.findMaxBinHoldByWarehouseIdAndColdStorageName(
					Long.valueOf(selectedWarehouse), selectedColdStorageName);

			if (!ObjectUtil.isEmpty(coldStorage)) {
				maxBayHold = coldStorage.getMaxBayHold();
			}

		}
		productArr.add(getJSONObject("maxBayHold", maxBayHold));

		sendAjaxResponse(productArr);
	}

	public String populateEditVariety() throws Exception {
		if (!StringUtil.isEmpty(selectedPro)) {
			Map<String, String> varieties = new LinkedHashMap<>();
			List<ProcurementVariety> varietiesList = productDistributionService
					.listProcurementVarietyByProcurementProductId(Long.valueOf(selectedPro));
			if (!ObjectUtil.isEmpty(varietiesList)) {
				for (ProcurementVariety procurementVariety : varietiesList) {
					// varieties.add(procurementVariety.getName() + " - " +
					// procurementVariety.getCode());
					varieties.put(procurementVariety.getName() + " # " + procurementVariety.getCode(),
							procurementVariety.getName());
					/*
					 * varieties.put( procurementVariety.getName() + " # " +
					 * procurementVariety.getCode(),
					 * procurementVariety.getName() + " - " +
					 * procurementVariety.getCode());
					 */
				}
			}
			printAjaxResponse(varieties, "text/html");
		}
		return null;

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

	public String populateEditGrade() throws Exception {
		if (!StringUtil.isEmpty(selectedVar)) {
			List<String> grades = new ArrayList<String>();
			String[] varArr = selectedVar.split("#");
			String varValue = varArr[1];
			ProcurementVariety varierty = productDistributionService.findProcurementVariertyByCode(varValue.trim());
			List<ProcurementGrade> gradeList = productDistributionService
					.listProcurementGradeByProcurementVarietyId(Long.valueOf(varierty.getId()));
			if (!ObjectUtil.isEmpty(gradeList)) {
				for (ProcurementGrade procurementGrade : gradeList) {
					grades.add(procurementGrade.getName() + " - " + procurementGrade.getCode());
				}
			}
			printAjaxResponse(grades, "text/html");
		}
		return null;
	}

	public void populatePrice() throws Exception {
		if (!StringUtil.isEmpty(selectedGrade)) {
			ProcurementGrade grade = productDistributionService.findProcurementGradeById(Long.valueOf(selectedGrade));
			if (!ObjectUtil.isEmpty(grade)) {
				sendResponse(grade.getPrice());
			}
		}
	}

	public void populateMasterData() {
		JSONArray masterArr = new JSONArray();
		if (!StringUtil.isEmpty(selectedMasterType)) {
			getMasterDataList().stream().filter(masterData -> masterData.getMasterType().equals(selectedMasterType))
					.forEach(masterData -> {
						masterArr.add(getJSONObject(masterData.getCode(), masterData.getName()));
					});
			sendAjaxResponse(masterArr);
		}
	}

	public void populateFarm() {
		JSONArray farmArr = new JSONArray();
		if (!StringUtil.isEmpty(selectedFarmer)) {
			farmerService.findFarmInfoByFarmerId(Long.valueOf(selectedFarmer)).stream().forEach(obj -> {
				farmArr.add(getJSONObject(obj[0].toString(), obj[3].toString()));
			});
		}
		sendAjaxResponse(farmArr);
	}

	public Map<Integer, String> getMasterTypeList() {

		if (masterTypeList.size() == 0) {
			masterTypeList = getPropertyData("masterTypeList");
			return masterTypeList;
		}
		return masterTypeList;

	}

	public void setMasterTypeList(Map<Integer, String> masterTypeList) {

		this.masterTypeList = masterTypeList;
	}

	public Date getsDate() {

		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		if (!StringUtil.isEmpty(startDate)) {
			try {
				sDate = df.parse(startDate);
			} catch (ParseException e) {
				LOGGER.error("Error parsing start date" + e.getMessage());
			}
		}
		return sDate;
	}

	public Date geteDate() {

		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		if (!StringUtil.isEmpty(endDate)) {
			try {
				eDate = df.parse(endDate);
				eDate.setTime(eDate.getTime() + 86399000);
			} catch (ParseException e) {
				LOGGER.error("Error parsing end date" + e.getMessage());
			}

		}
		return eDate;
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

	public String getSelectedCity() {
		return selectedCity;
	}

	public void setSelectedCity(String selectedCity) {
		this.selectedCity = selectedCity;
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

	public String getSelectedMobileUser() {
		return selectedMobileUser;
	}

	public void setSelectedMobileUser(String selectedMobileUser) {
		this.selectedMobileUser = selectedMobileUser;
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

	public String getSelectedFarmerType() {
		return selectedFarmerType;
	}

	public void setSelectedFarmerType(String selectedFarmerType) {
		this.selectedFarmerType = selectedFarmerType;
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

	public String getFarmerType() {
		return farmerType;
	}

	public void setFarmerType(String farmerType) {
		this.farmerType = farmerType;
	}

	public String getFarmerName() {
		return farmerName;
	}

	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Long getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(Long seasonId) {
		this.seasonId = seasonId;
	}

	public String getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getEnableSupplier() {
		return enableSupplier;
	}

	public void setEnableSupplier(String enableSupplier) {
		this.enableSupplier = enableSupplier;
	}

	public List<MasterData> getMasterDataList() {
		if (masterDataList.size() <= 0) {
			buildMasterDataList();
		}
		return masterDataList;
	}

	private void buildMasterDataList() {
		masterDataList = productDistributionService.listMasterDataByRevisionNo(0L);
	}

	public void setMasterDataList(List<MasterData> masterDataList) {
		this.masterDataList = masterDataList;
	}

	public String getSelectedMasterType() {
		return selectedMasterType;
	}

	public void setSelectedMasterType(String selectedMasterType) {
		this.selectedMasterType = selectedMasterType;
	}

	public String getSelectedSupplier() {
		return selectedSupplier;
	}

	public void setSelectedSupplier(String selectedSupplier) {
		this.selectedSupplier = selectedSupplier;
	}

	public String getProcurementDate() {
		return procurementDate;
	}

	public void setProcurementDate(String procurementDate) {
		this.procurementDate = procurementDate;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getFarmerAttnce() {
		return farmerAttnce;
	}

	public void setFarmerAttnce(String farmerAttnce) {
		this.farmerAttnce = farmerAttnce;
	}

	public String getSubstituteName() {
		return substituteName;
	}

	public String getRoadMapCode() {
		return roadMapCode;
	}

	public void setRoadMapCode(String roadMapCode) {
		this.roadMapCode = roadMapCode;
	}

	public String getSelectedFarm() {
		return selectedFarm;
	}

	public void setSelectedFarm(String selectedFarm) {
		this.selectedFarm = selectedFarm;
	}

	public void setSubstituteName(String substituteName) {
		this.substituteName = substituteName;
	}

	private Map<String, Object> getTotalProductDetails(Procurement procurement) {

		long noOfBags = 0;
		double grossWt = 0;
		/* double tareWt = 0; */
		double netWt = 0;
		long gradeId = 0;
		Map<String, Object> details = new HashMap<String, Object>();
		if (!ObjectUtil.isListEmpty(procurement.getProcurementDetails())) {
			for (ProcurementDetail detail : procurement.getProcurementDetails()) {
				noOfBags = noOfBags + detail.getNumberOfBags();
				grossWt = grossWt + detail.getGrossWeight();
				/* tareWt = tareWt + detail.getTareWeight(); */
				netWt = netWt + detail.getNetWeight();

				if (detail.getProcurementGrade() != null) {
					gradeId = detail.getProcurementGrade().getId();
				}
			}
		}
		details.put(NO_OF_BAGS, noOfBags);
		details.put(GROSS_WEIGHT, grossWt);
		/* details.put(TARE_WEIGHT, tareWt); */
		details.put(NET_WEIGHT, netWt);
		details.put(GRADE_IDS, gradeId);

		return details;
	}

	public String update() throws Exception {
		if (!StringUtil.isEmpty((String) this.id)) {
			this.procurement = this.productDistributionService.findProcurementById(Long.valueOf(this.id));
			CityWarehouse cityWarehouse = null;
			if (!ObjectUtil.isEmpty((Object) this.procurement)) {
				for (ProcurementDetail procurementDetail : this.procurement.getProcurementDetails()) {
					if (ObjectUtil.isEmpty((Object) procurementDetail.getProcurementProduct())
							|| ObjectUtil.isEmpty((Object) procurementDetail.getProcurementGrade()))
						continue;
					cityWarehouse = this.productDistributionService.findByProdIdAndGradeCode(
							procurementDetail.getProcurementProduct().getId(),
							procurementDetail.getProcurementGrade().getCode(), this.getCurrentTenantId());
				}
				if (!(ObjectUtil.isEmpty((Object) cityWarehouse)
						|| ObjectUtil.isEmpty((Object) cityWarehouse.getCoOperative())
						|| StringUtil.isEmpty((String) cityWarehouse.getCoOperative().getName()))) {
					Warehouse warehouse = this.locationService
							.findCoOperativeByCode(cityWarehouse.getCoOperative().getCode());
					this.procurement.setWarehouseName(warehouse.getCode() + "-" + warehouse.getName());
				}
			}
			if (!StringUtil.isEmpty((String) this.procurement.getProcMasterType())) {
				this.setSupplierMaster(
						this.getMasterTypeList().get(Integer.parseInt(this.procurement.getProcMasterType())));
			}

			if (!StringUtil.isEmpty((String) this.procurement.getProcMasterTypeId())) {
				if ("99".equalsIgnoreCase(this.procurement.getProcMasterType())
						|| "11".equalsIgnoreCase(this.procurement.getProcMasterType())) {
					Warehouse warehouse = locationService.findSamithiByCode(this.procurement.getProcMasterTypeId());
					this.setSupplierType(warehouse.getName());
				} else {
					MasterData masterData = this.clientService
							.findMasterDataById(Long.valueOf(Long.parseLong(this.procurement.getProcMasterTypeId())));
					this.setSupplierType(masterData.getName());
				}
			}
			/*
			 * if (!StringUtil.isEmpty((String)
			 * this.procurement.getProcMasterTypeId())) { MasterData masterData
			 * = this.clientService
			 * .findMasterDataById(Long.valueOf(Long.parseLong(this.procurement.
			 * getProcMasterTypeId())));
			 * this.setSupplierType(masterData.getName()); }
			 */
		}
		return "update";
	}

	public String getSupplierMaster() {

		return supplierMaster;
	}

	public void setSupplierMaster(String supplierMaster) {

		this.supplierMaster = supplierMaster;
	}

	public String getSupplierType() {

		return supplierType;
	}

	public void setSupplierType(String supplierType) {

		this.supplierType = supplierType;
	}

	public void populateProcurementDetailList() {
		// setEnableTraceability(preferncesService.findPrefernceByName(ESESystem.ENABLE_TRACEABILITY));
		JSONArray array = new JSONArray();
		this.procurement = this.productDistributionService.findProcurementById(Long.valueOf(this.procurementId));
		if (!ObjectUtil.isEmpty((Object) this.procurement)) {
			for (ProcurementDetail procurementDetail : this.procurement.getProcurementDetails()) {
				JSONObject obj = new JSONObject();
				obj.put((Object) "variety",
						(Object) procurementDetail.getProcurementGrade().getProcurementVariety().getName());
				obj.put((Object) "grade", (Object) procurementDetail.getProcurementGrade().getName());
				obj.put((Object) "product", (Object) procurementDetail.getProcurementGrade().getProcurementVariety()
						.getProcurementProduct().getName());
				obj.put((Object) "productId", (Object) procurementDetail.getProcurementGrade().getProcurementVariety()
						.getProcurementProduct().getId());
				obj.put((Object) "price", (Object) procurementDetail.getProcurementGrade().getPrice());
				obj.put((Object) "gradeCode", (Object) procurementDetail.getProcurementGrade().getCode());
				obj.put((Object) "varietyCode",
						(Object) procurementDetail.getProcurementGrade().getProcurementVariety().getCode());
				obj.put((Object) "varietyId",
						(Object) procurementDetail.getProcurementGrade().getProcurementVariety().getId());
				obj.put((Object) "productUnit", (Object) procurementDetail.getProcurementGrade().getProcurementVariety()
						.getProcurementProduct().getUnit());
				obj.put((Object) "subTotal", (Object) procurementDetail.getSubTotal());
				obj.put((Object) "ratePerUnit", (Object) procurementDetail.getRatePerUnit());
				obj.put((Object) "bags", (Object) (!StringUtil.isEmpty((Object) procurementDetail.getNumberOfBags())
						? Long.valueOf(procurementDetail.getNumberOfBags()) : "0.00"));
				// obj.put((Object)"batchNo",
				// (Object)(!StringUtil.isEmpty((Object)procurementDetail.getBatchNo())
				// ? procurementDetail.getBatchNo() : ""));
				obj.put((Object) "grossWeight",
						(Object) (!StringUtil.isEmpty((Object) procurementDetail.getGrossWeight())
								? Double.valueOf(procurementDetail.getGrossWeight()) : "0.00"));
				obj.put((Object) "dryLoss", (Object) (!StringUtil.isEmpty((Object) procurementDetail.getDryLoss())
						? Double.valueOf(procurementDetail.getDryLoss()) : "0.00"));
				obj.put((Object) "gradingLoss",
						(Object) (!StringUtil.isEmpty((Object) procurementDetail.getGradingLoss())
								? Double.valueOf(procurementDetail.getGradingLoss()) : "0.00"));
				obj.put((Object) "netWeight", (Object) (!StringUtil.isEmpty((Object) procurementDetail.getNetWeight())
						? Double.valueOf(procurementDetail.getNetWeight()) : "0.00"));
				obj.put((Object) "id", (Object) procurementDetail.getId());
				array.add((Object) obj);
			}
			JSONObject mainObj = new JSONObject();
			mainObj.put((Object) "data", (Object) array);
			this.printAjaxResponse((Object) mainObj, "text/html");
		}
	}

	public String getProcurementId() {
		return this.procurementId;
	}

	public void setProcurementId(String procurementId) {
		this.procurementId = procurementId;
	}

	public void populateProcurementDetails() {
		// setEnableTraceability(preferncesService.findPrefernceByName(ESESystem.ENABLE_TRACEABILITY));
		String updateUser = (String) this.request.getSession().getAttribute("user");
		double subTot = 0.0;
		String[] details = this.procurementDetailArray.split("###");
		ProcurementDetail procurementDetails = this.productDistributionService
				.findByProcurementDetailId(Long.valueOf(details[2].trim()));
		procurementDetails.setNetWeight(Double.valueOf(details[1].trim()).doubleValue());
		procurementDetails.setGrossWeight(Double.valueOf(details[1].trim()).doubleValue());
		procurementDetails.setNumberOfBags(Long.valueOf(details[0].trim()).longValue());
		procurementDetails.getProcurement().setUpdatedDate(new Date());
		procurementDetails.getProcurement().setUpdatedUser(updateUser);
		if (this.getCurrentTenantId().equalsIgnoreCase("lalteer")
				&& !this.getCurrentTenantId().equalsIgnoreCase("awi")) {
			procurementDetails.setDryLoss(Double.valueOf(details[3].trim()));
			procurementDetails.setGradingLoss(Double.valueOf(details[4].trim()));
			double netWeg = procurementDetails.getDryLoss() + procurementDetails.getGradingLoss();
			subTot = procurementDetails.getGrossWeight() - netWeg;
			procurementDetails.setNetWeight(subTot);
			procurementDetails.setSubTotal(subTot);
		} else {
			subTot = procurementDetails.getRatePerUnit() * procurementDetails.getGrossWeight();
			procurementDetails.setSubTotal(subTot);
		}
		procurementDetails.getProcurement().setTotalProVal(subTot);
		procurementDetails.getProcurement().setInvoiceValue(subTot);

		procurementDetails.setTenantId(this.getCurrentTenantId());
		String nowDate = DateUtil.convertDateToString(new Date(), getGeneralDateFormat());
		procurementDetails.getProcurement()
				.setUpdatedDate(DateUtil.convertStringToDate(nowDate, getGeneralDateFormat()));

		procurementDetails.getProcurement().setUpdatedUser(getUsername());
		this.productDistributionService.updateProcurementDetails(procurementDetails);

		Procurement updatePrcurement = procurementDetails.getProcurement();
		Double totVal = updatePrcurement.getProcurementDetails().stream().mapToDouble(obj -> obj.getSubTotal()).sum();

		updatePrcurement.setTotalProVal(totVal);
		updatePrcurement.setInvoiceValue(totVal);

		/*
		 * updatePrcurement.getAgroTransaction().setIntBalance(totVal);
		 * updatePrcurement.getAgroTransaction().setIntBalance(totVal);
		 * updatePrcurement.getAgroTransaction().getAccount().setCashBalance(
		 * totVal);
		 */

		farmerService.update(updatePrcurement);

		/*
		 * AgroTransaction txn
		 * =productDistributionDAO.findAgroTransactionByProcurementId(
		 * procurementDetails.getProcurement().getId(),procurementDetails.
		 * getProcurement().getAgroTransaction().getId()); AgroTransaction
		 * agrotxn
		 * =productDistributionDAO.findAgroTransactionByRecAndTxnDesc(txn.
		 * getReceiptNo(),Procurement.PROCURMEMENT);
		 * agrotxn.setTxnAmount(procurementDetails.getProcurement().
		 * getTotalProVal());
		 * agrotxn.setBalAmount(agrotxn.getIntBalance()+procurementDetails.
		 * getProcurement().getTotalProVal()); farmerService.update(agrotxn);
		 * 
		 * txn.setIntBalance(agrotxn.getBalAmount());
		 * txn.setBalAmount(agrotxn.getBalAmount() -
		 * procurementDetails.getProcurement().getPaymentAmount());
		 * txn.setCreditAmt(agrotxn.getBalAmount() -
		 * procurementDetails.getProcurement().getPaymentAmount());
		 * farmerService.update(txn); AgroTransaction txn1
		 * =productDistributionDAO.findAgroTransactionByProcurementId(
		 * procurementDetails.getProcurement().getId(),procurementDetails.
		 * getProcurement().getAgroTransaction().getId());
		 * 
		 * PaymentLedger ledger =
		 * productDistributionDAO.findPaymenyLedgerByProcurementId(String.
		 * valueOf(procurementDetails.getProcurement().getId()));
		 * ledger.setPrevValue(txn1.getBalAmount());
		 * ledger.setTxnValue(procurementDetails.getProcurement().
		 * getAgroTransaction().getTxnAmount());
		 * ledger.setNewValue(txn1.getBalAmount()-procurementDetails.
		 * getProcurement().getAgroTransaction().getTxnAmount());
		 * farmerService.update(ledger);
		 * 
		 * 
		 * ESEAccount farmerAccount = productDistributionDAO
		 * .findESEAccountByProfileId(procurementDetails.getProcurement().
		 * getAgroTransaction().getFarmerId(), ESEAccount.CONTRACT_ACCOUNT); if
		 * (!ObjectUtil.isEmpty(farmerAccount)) {
		 * 
		 * farmerAccount.setCashBalance( txn1.getBalAmount());
		 * farmerService.update(farmerAccount); }
		 */
	}

	public void populatePaymentAmt() {

		if (!StringUtil.isEmpty((String) this.id))
			this.procurement = this.productDistributionService.findProcurementById(Long.valueOf(this.id));

		if (!StringUtil.isEmpty(paymentAmount) && paymentAmount != null) {
			procurement.setPaymentAmount(Double.valueOf(paymentAmount));
		} else {
			procurement.setPaymentAmount(0D);
		}

		procurement.setVehicleType(getVehicleType());
		procurement.setTransportCost(getTransport_cost());
		productDistributionService.editProcurement(procurement);

		AgroTransaction txn = productDistributionDAO.findAgroTransactionByProcurementId(procurement.getId(),
				procurement.getAgroTransaction().getId());
		AgroTransaction agrotxn = productDistributionDAO.findAgroTransactionByRecAndTxnDesc(txn.getReceiptNo(),
				Procurement.PROCURMEMENT);
		agrotxn.setTxnAmount(procurement.getTotalProVal());
		agrotxn.setBalAmount(agrotxn.getIntBalance() + procurement.getTotalProVal());
		farmerService.update(agrotxn);

		txn.setIntBalance(agrotxn.getBalAmount());
		txn.setBalAmount(agrotxn.getBalAmount() - procurement.getPaymentAmount());
		txn.setCreditAmt(agrotxn.getBalAmount() - procurement.getPaymentAmount());
		farmerService.update(txn);
		AgroTransaction txn1 = productDistributionDAO.findAgroTransactionByProcurementId(procurement.getId(),
				procurement.getAgroTransaction().getId());

		PaymentLedger ledger = productDistributionDAO
				.findPaymenyLedgerByProcurementId(String.valueOf(procurement.getId()));
		ledger.setPrevValue(txn1.getBalAmount());
		ledger.setTxnValue(procurement.getAgroTransaction().getTxnAmount());
		ledger.setNewValue(txn1.getBalAmount() - procurement.getAgroTransaction().getTxnAmount());
		farmerService.update(ledger);

		ESEAccount farmerAccount = productDistributionDAO
				.findESEAccountByProfileId(procurement.getAgroTransaction().getFarmerId(), ESEAccount.CONTRACT_ACCOUNT);
		if (!ObjectUtil.isEmpty(farmerAccount)) {

			farmerAccount.setCashBalance(txn1.getBalAmount());
			farmerService.update(farmerAccount);
		}

	}

	public String getProcurementDetailArray() {

		return procurementDetailArray;
	}

	public void setProcurementDetailArray(String procurementDetailArray) {

		this.procurementDetailArray = procurementDetailArray;
	}

	public String getSelectedPro() {

		return selectedPro;
	}

	public void setSelectedPro(String selectedPro) {

		this.selectedPro = selectedPro;
	}

	public String getSelectedVar() {

		return selectedVar;
	}

	public void setSelectedVar(String selectedVar) {

		this.selectedVar = selectedVar;
	}

	public List<ProcurementVariety> getVarietyList() {

		return varietyList;
	}

	public void setVarietyList(List<ProcurementVariety> varietyList) {

		this.varietyList = varietyList;
	}

	public String getEnableTraceability() {

		return enableTraceability;
	}

	public void setEnableTraceability(String enableTraceability) {

		this.enableTraceability = enableTraceability;
	}

	public IProductDistributionDAO getProductDistributionDAO() {
		return productDistributionDAO;
	}

	public void setProductDistributionDAO(IProductDistributionDAO productDistributionDAO) {
		this.productDistributionDAO = productDistributionDAO;
	}

	private AgroTransaction processProcurement(Procurement proc) {

		AgroTransaction txn = new AgroTransaction();
		txn.setDeviceId(NOT_APPLICABLE);
		txn.setDeviceName(NOT_APPLICABLE);
		txn.setBranch_id(proc.getBranchId());
		if (!StringUtil.isEmpty(this.procurementDate)) {
			try {
				txn.setTxnTime(DateUtil.convertStringToDate(this.procurementDate, getGeneralDateFormat()));
				// txn.setTxnTime(DateUtil.setTimeToDate(txn.getTxnTime()));
			} catch (Exception e) {
				String date = DateUtil.convertDateToString(new Date(), getGeneralDateFormat());
				txn.setTxnTime(DateUtil.convertStringToDate(date, getGeneralDateFormat()));

				// txn.setTxnTime(DateUtil.setTimeToDate(txn.getTxnTime()));

			}
		}
		txn.setTxnType(PROCUREMENT_PRODUCT_ENROLLMENT);
		txn.setProfType(Profile.CLIENT);
		txn.setOperType(ESETxn.ON_LINE);
		txn.setTxnDesc(Procurement.PROCURMEMENT);

		Double bal;
		if (!ObjectUtil.isEmpty(proc.getFarmer()) && !StringUtil.isEmpty(proc.getFarmer().getFarmerId())) {
			ESEAccount farmerAccount = productDistributionDAO.findESEAccountByProfileId(proc.getFarmer().getFarmerId(),
					ESEAccount.CONTRACT_ACCOUNT);
			if (farmerAccount != null && !ObjectUtil.isEmpty(farmerAccount)) {
				bal = farmerAccount.getCashBalance();
				txn.setIntBalance(bal);
				txn.setTxnAmount(StringUtil.isDouble(getTotalAmount()) ? Double.valueOf(getTotalAmount()) : 0D);
				txn.setBalAmount(bal + proc.getTotalProVal());
				txn.setAccount(farmerAccount);
			} else {
				txn.setIntBalance(0.00);
				txn.setTxnAmount(StringUtil.isDouble(getTotalAmount()) ? Double.valueOf(getTotalAmount()) : 0D);
				txn.setBalAmount(0.00);
			}
		} else {
			txn.setIntBalance(0.00);
			txn.setTxnAmount(StringUtil.isDouble(getTotalAmount()) ? Double.valueOf(getTotalAmount()) : 0D);
			txn.setBalAmount(0.00);
			// txn.setAccount(farmerAccount);
		}

		Warehouse warehouse = locationService.findWarehouseById(Long.valueOf(selectedWarehouse));
		txn.setWarehouse(warehouse);
		txn.setSamithi(warehouse);

		if (!StringUtil.isEmpty(selectedMobileUser)) {
			Agent agent = agentService.findAgent(Long.valueOf(selectedMobileUser));
			txn.setAgentId(agent.getProfileId());
			txn.setAgentName(agent.getPersonalInfo().getFirstName());
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
		txn.setSeasonCode(proc.getSeasonCode());
		productDistributionService.saveAgroTransaction(txn);

		return txn;

	}

	public String getProductAvailableUnit() {
		return productAvailableUnit;
	}

	public void setProductAvailableUnit(String productAvailableUnit) {
		this.productAvailableUnit = productAvailableUnit;
	}

	public String getSelectedCropType() {
		return selectedCropType;
	}

	public void setSelectedCropType(String selectedCropType) {
		this.selectedCropType = selectedCropType;
	}

	public Map<String, String> getCTypes() {
		Map<String, String> cType = new LinkedHashMap<>();
		cType.put("1", getLocaleProperty("cpType1"));
		cType.put("0", getLocaleProperty("cpType0"));
		return cType;
	}

	public void populateFarmerByFPO() {
		JSONArray farmerArr = new JSONArray();
		if (!StringUtil.isEmpty(selectedSupplier)) {
			farmerService.findFarmerByGroup(Long.valueOf(selectedSupplier)).stream()
					.filter(farmer -> (!ObjectUtil.isEmpty(farmer))).forEach(farmer -> {
						farmerArr
								.add(getJSONObject(farmer[0], (!StringUtil.isEmpty(farmer[3]) ? (" " + farmer[3]) : " ")
										+ (!StringUtil.isEmpty(farmer[1]) ? ("-" + farmer[1]) : " ")));
					});
		}
		sendAjaxResponse(farmerArr);
	}

	public String delete() {
		if (!StringUtil.isEmpty(id)) {
			procurement = productDistributionService.findProcurementById(Long.valueOf(id));
			productService.removeProcurment(id, Procurement.DELETE_STATUS);
			AgroTransaction agrotxn = new AgroTransaction();
			agrotxn.setTxnDesc(Procurement.PROCURMEMENT_DELETE);
			agrotxn.setTxnType(procurement.getAgroTransaction().getTxnType() + "D");
			agrotxn.setFarmerId(procurement.getFarmer().getFarmerId());
			agrotxn.setFarmerName(procurement.getFarmer().getFirstName());
			agrotxn.setServicePointId(procurement.getWarehouseId());
			agrotxn.setServicePointName(procurement.getWarehouseName());
			farmerService.addAgroTxn(agrotxn);
		}

		return LIST;

	}

	@SuppressWarnings("unchecked")
	public void populateSamithi() throws Exception {

		samithi = locationService.listSamithiesBasedOnType();
		JSONArray warehouseArr = new JSONArray();

		samithi.stream().filter(samithi -> !ObjectUtil.isEmpty(samithi)).forEach(samithi -> {
			warehouseArr.add(getJSONObject(samithi.getId(), samithi.getName()));
		});
		sendAjaxResponse(warehouseArr);
	}

	public void populateCatalogueData() {
		JSONArray fpoArr = new JSONArray();
		farmerService.listCatelogueType(getLocaleProperty("fpoType")).stream().forEach(catalogue -> {
			fpoArr.add(getJSONObject(catalogue.getCode(), catalogue.getName()));
		});
		sendAjaxResponse(fpoArr);
	}

	public Map<Long, String> getBuyersList() {
		Map<Long, String> listOfBuyers = new HashMap<Long, String>();
		List<Customer> customers = clientService.listOfCustomers();
		for (Customer customer : customers) {
			listOfBuyers.put(customer.getId(), customer.getCustomerName());
		}
		return listOfBuyers;
	}

	public List<Warehouse> getSamithi() {
		return samithi;
	}

	public void setSamithi(List<Warehouse> samithi) {
		this.samithi = samithi;
	}

	public String getProductAvailableUnitCode() {
		return productAvailableUnitCode;
	}

	public void setProductAvailableUnitCode(String productAvailableUnitCode) {
		this.productAvailableUnitCode = productAvailableUnitCode;
	}

	public List<FarmCatalogue> getListUom() {
		List<FarmCatalogue> subUomList = catalogueService.listCataloguesByUnit();

		return subUomList;
	}

	public String getSelectedFarmerValue() {
		return selectedFarmerValue;
	}

	public void setSelectedFarmerValue(String selectedFarmerValue) {
		this.selectedFarmerValue = selectedFarmerValue;
	}

	public ESEAccount getAccount() {
		return account;
	}

	public void setAccount(ESEAccount account) {
		this.account = account;
	}

	public String getCashCreditValue() {
		return cashCreditValue;
	}

	public void setCashCreditValue(String cashCreditValue) {
		this.cashCreditValue = cashCreditValue;
	}

	public String getEnableBuyer() {
		return enableBuyer;
	}

	public void setEnableBuyer(String enableBuyer) {
		this.enableBuyer = enableBuyer;
	}

	public Double getTransport_cost() {
		return transport_cost;
	}

	public void setTransport_cost(Double transport_cost) {
		this.transport_cost = transport_cost;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public Map<String, String> getVehicleList() {
		Map<String, String> waterSourceList = new LinkedHashMap<>();
		waterSourceList = getFarmCatalougeMap(Integer.valueOf("124"));
		return waterSourceList;
	}

	public Map<String, String> getBlockNameList() {

		Map<String, String> farmCatalougeList = new LinkedHashMap<>();
		farmCatalougeList = getFarmCatalougeMap(Integer.valueOf(getLocaleProperty("blockNameCatalog").trim()));
		return farmCatalougeList;

	}

	public Map<String, String> getFloorNameList() {

		Map<String, String> farmCatalougeList = new LinkedHashMap<>();
		farmCatalougeList = getFarmCatalougeMap(Integer.valueOf(getLocaleProperty("floorNameCatalog").trim()));
		return farmCatalougeList;

	}

	public Map<String, String> getBayNumList() {

		Map<String, String> farmCatalougeList = new LinkedHashMap<>();
		farmCatalougeList = getFarmCatalougeMap(Integer.valueOf(getLocaleProperty("bayNumCatalog").trim()));
		return farmCatalougeList;

	}

	public String getSelectedColdStorageName() {
		return selectedColdStorageName;
	}

	public void setSelectedColdStorageName(String selectedColdStorageName) {
		this.selectedColdStorageName = selectedColdStorageName;
	}

	public void populateAvailableQty() throws Exception {

		String result = EMPTY_QTY;
		Double availableQty = 0.00;
		if (!StringUtil.isEmpty(selectedWarehouse) && !StringUtil.isEmpty(selectedColdStorageName)
				&& !StringUtil.isEmpty(selectedProduct) && !StringUtil.isEmpty(selectedBlockName)
				&& !StringUtil.isEmpty(selectedFloorName) && !StringUtil.isEmpty(selectedBayNumber)) {

			ColdStorageDetail coldStorageDetail = null;

			availableQty = productDistributionService.findAvailableQtyByWarehouseColdStorageNameGradeBlockFloorBay(
					Long.valueOf(selectedWarehouse), selectedColdStorageName, selectedBlockName, selectedFloorName,
					selectedBayNumber, Long.valueOf(selectedProduct));

			/*
			 * if (!ObjectUtil.isEmpty(coldStorageDetail)) { availableQty =
			 * coldStorageDetail.getColdStorage().getTotalQty();
			 * 
			 * result = String.valueOf(availableQty); } else { availableQty =
			 * coldStorageDetail.getColdStorage().getMaxBayHold();
			 * 
			 * }
			 */

			result = String.valueOf(availableQty);

		}
		response.getWriter().print(result);
	}

	public String getSelectedBlockName() {
		return selectedBlockName;
	}

	public void setSelectedBlockName(String selectedBlockName) {
		this.selectedBlockName = selectedBlockName;
	}

	public String getSelectedFloorName() {
		return selectedFloorName;
	}

	public void setSelectedFloorName(String selectedFloorName) {
		this.selectedFloorName = selectedFloorName;
	}

	public String getSelectedBayNumber() {
		return selectedBayNumber;
	}

	public void setSelectedBayNumber(String selectedBayNumber) {
		this.selectedBayNumber = selectedBayNumber;
	}

	public String getTotalNoOfBag() {
		return totalNoOfBag;
	}

	public void setTotalNoOfBag(String totalNoOfBag) {
		this.totalNoOfBag = totalNoOfBag;
	}

	public String getTotalQtyLabel() {
		return totalQtyLabel;
	}

	public void setTotalQtyLabel(String totalQtyLabel) {
		this.totalQtyLabel = totalQtyLabel;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getBondNo() {
		return bondNo;
	}

	public void setBondNo(String bondNo) {
		this.bondNo = bondNo;
	}

	public String getColdStorageName() {
		return coldStorageName;
	}

	public void setColdStorageName(String coldStorageName) {
		this.coldStorageName = coldStorageName;
	}

	public String getSelectedBatchNo() {
		return selectedBatchNo;
	}

	public void setSelectedBatchNo(String selectedBatchNo) {
		this.selectedBatchNo = selectedBatchNo;
	}

	public String getEnableStorage() {
		return enableStorage;
	}

	public void setEnableStorage(String enableStorage) {
		this.enableStorage = enableStorage;
	}

	public Map<String, String> getRoundOfHarvestList() {

		AtomicInteger i = new AtomicInteger(0);
		Map<String, String> roundOfHarvestList = new LinkedHashMap<>();
		roundOfHarvestList = getFarmCatalougeMap(Integer.valueOf(getLocaleProperty("roundOfHarvesting")));
		return roundOfHarvestList;

	}

	public String getRoundOfHarvesting() {
		return roundOfHarvesting;
	}

	public void setRoundOfHarvesting(String roundOfHarvesting) {
		this.roundOfHarvesting = roundOfHarvesting;
	}

	public void validateBatchNo() throws Exception {
		JSONArray productArr = new JSONArray();
		if (!StringUtil.isEmpty(selectedFarmer) && !StringUtil.isEmpty(batchNo)) {
			ColdStorage coldStorage = productDistributionService
					.findColdStorageByFarmerAndBatchNo(Long.valueOf(selectedFarmer), batchNo);

			if (!ObjectUtil.isEmpty(coldStorage) && coldStorage.getFarmer().getId() == Long.valueOf(selectedFarmer)) {
				if (!ObjectUtil.isEmpty(coldStorage)) {
					productArr.add(getJSONObject("farmer", "1"));
				}
			} else {

				ColdStorage cs = productDistributionService.findColdStorageByBatchNo(batchNo);
				if (!ObjectUtil.isEmpty(cs)) {
					productArr.add(getJSONObject("farmer", "0"));
				} else {
					productArr.add(getJSONObject("farmer", "1"));
				}

			}

		}

		sendAjaxResponse(productArr);
	}

	public String getDynamic_field_value_id() {
		return dynamic_field_value_id;
	}

	public void setDynamic_field_value_id(String dynamic_field_value_id) {
		this.dynamic_field_value_id = dynamic_field_value_id;
	}

	public Map<String, String> getListLotCode() {
		return farmerService.listValueByFieldName(getLocaleProperty("lotCode.fieldName"),getBranchId()).parallelStream()
				.filter(fil -> fil != null && !ObjectUtil.isEmpty(fil) && fil[2] != null && fil[1] != null)
				.collect(Collectors.toMap(id -> String.valueOf(id[2]), val -> String.valueOf(val[1])));
	}


	public Map<String, String> getListRoundOfHarvesting() {
		return farmerService.listValueByFieldName(getLocaleProperty("roundOfHarvesting.fieldName"),getBranchId()).parallelStream()
				.filter(distinctByKey(d -> d[1]))
				.filter(fil -> fil != null && !ObjectUtil.isEmpty(fil) && fil[0] != null && fil[1] != null)
				.collect(Collectors.toMap(id -> String.valueOf(id[1]),
						val -> (getCatlogueValueByCode(String.valueOf(val[1])) != null
								? getCatlogueValueByCode(String.valueOf(val[1])).getDispName() : "")));
	}

	public Map<String, String> getListBondStatus() {
		Map<String, String> returnMap = new LinkedHashMap<String, String>();

		returnMap.put(String.valueOf(ColdStorage.BONDSTATUS.AWAITING.ordinal()), getLocaleProperty("bondStatus-1"));
		returnMap.put(String.valueOf(ColdStorage.BONDSTATUS.APPROVED.ordinal()), getLocaleProperty("bondStatus-2"));
		returnMap.put(String.valueOf(ColdStorage.BONDSTATUS.REJECTED.ordinal()), getLocaleProperty("bondStatus-3"));

		return returnMap;

	}

	public void populateDataByFarmCropId() {
		JSONObject jsonObject = new JSONObject();
		List<FarmerDynamicFieldsValue> fdfv_List = farmerService
				.listFarmerDynmaicFieldsByTxnId(getDynamic_field_value_id());
		if (fdfv_List != null && !fdfv_List.isEmpty()) {
			Object[] fc = farmerService.findFarmerInfoById(Long.valueOf(fdfv_List.get(0).getReferenceId()));
			FarmerDynamicFieldsValue variety = fdfv_List.stream().anyMatch(
					f -> f.getFieldName().equalsIgnoreCase(getLocaleProperty("variety.fieldName"))) ? fdfv_List.stream()
							.filter(f -> f.getFieldName().equalsIgnoreCase(getLocaleProperty("variety.fieldName")))
							.findFirst().get() : null;
			if (variety != null) {

				ProcurementVariety pv = productDistributionService
						.findProcurementVariertyByCode(variety.getFieldValue());
				jsonObject.put("farmer", getJSONObject(fc[0], fc[1] + " " + fc[2]));
				jsonObject.put("variety", !ObjectUtil.isEmpty(pv) ? getJSONObject(pv.getId(), pv.getName()) : "");
				jsonObject.put("product", !ObjectUtil.isEmpty(pv)
						? getJSONObject(pv.getProcurementProduct().getId(), pv.getProcurementProduct().getName()) : "");
				fdfv_List.stream().filter(
						f -> f.getFieldName().equalsIgnoreCase(getLocaleProperty("roundOfHarvesting.fieldName")))
						.forEach(i -> jsonObject.put("roundOfHarvesting", getJSONObject(i.getFieldValue(),
								getCatlogueValueByCode(i.getFieldValue()).getDispName())));
			}
		}
		sendAjaxResponse(jsonObject);
	}

	public void editColdStorageBondNo() {

		ColdStorage cs = farmerService.findColdStorageById(getColdStorageId());
		if (cs != null && !StringUtil.isEmpty(bondStatus)) {
			cs.setBondStatus(bondStatus);
			cs.setBondNo(bondNo);
			farmerService.update(cs);
			getJsonObject().put("msg", getText("update.success"));
			getJsonObject().put("title", getText("title.success"));
			sendAjaxResponse(getJsonObject());
		} else {
			getJsonObject().put("msg", getText("updated.failure"));
			getJsonObject().put("title", getText("title.error"));
			sendAjaxResponse(getJsonObject());
		}

	}

	public String getBondStatus() {
		return bondStatus;
	}

	public void setBondStatus(String bondStatus) {
		this.bondStatus = bondStatus;
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	public File getImgFile() {
		return imgFile;
	}

	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}

	public String getColdStorageId() {
		return coldStorageId;
	}

	public void setColdStorageId(String coldStorageId) {
		this.coldStorageId = coldStorageId;
	}
	

	
}
