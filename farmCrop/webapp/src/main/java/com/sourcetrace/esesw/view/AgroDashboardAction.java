/*
 * DashboardAction.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view;

import java.io.IOException;
import static java.util.stream.Collectors.joining;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.apache.poi.util.SystemOutLogger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.ese.view.ESEAction;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.CowInspection;
import com.sourcetrace.eses.order.entity.txn.Cultivation;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.service.IUserService;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.State;
import com.sourcetrace.esesw.entity.profile.Village;

/**
 * @author Administrator
 */
public class AgroDashboardAction extends ESEAction {

	private static final Logger logger = Logger.getLogger(AgroDashboardAction.class);
	private static final long serialVersionUID = 43113144066190107L;

	private String branchIdValue;
	private String selectedTaluk;
	private String selectedVillage;
	private Integer userCount;
	private String selectedYear;
	double totalHousingCost;
	double totalFeedCost;
	double totalTreatmentCost;
	double totalOtherCost;
	private String selectedStapleLen;
	private String totalProposedPlantArea;
	private double totalEstimatedYield;
	List<String> farmCodes = new ArrayList<>();
	List<String> farmCodez = new ArrayList<>();
	private String selectedState;
	private String selectedGender;
	private String selectedVariety;
	private String selectedStatus;
	int count = 0;
	int keyCount = 0;
	int icsTyp = 0;

	// private String
	private Map<Integer, String> listSeedSource = new LinkedHashMap<Integer, String>();

	@Autowired
	private IDeviceService deviceService;
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IAgentService agentService;
	@Autowired
	private IProductService productService;
	@Autowired
	private IPreferencesService preferncesService;
	@Autowired
	private ICatalogueService catalogueService;
	@Autowired
	private IProductDistributionService productDistributionService;

	private String selectedWarehouse;
	private String selectedSeedType;
	private String selectedSeedSource;
	private String selectedBranch;
	private String noData;
	private String selectedProduct;
	private String dateRange;
	List<Municipality> cities = new ArrayList<Municipality>();
	DecimalFormat formatter = new DecimalFormat("0.00");
	private String selectedCrop;
	private String typez;
	private String selectedCooperative;
	Map<String, String> genderList = new LinkedHashMap<String, String>();
	List<Warehouse> samithi = new ArrayList<Warehouse>();
	private String selectedFinYear;
	private String selectedSeason;
	private String agentId;
	private String farmerId;
	private String currentYear;
	private String gramPanchayatEnable;

	public String getSelectedCrop() {
		return selectedCrop;
	}

	public void setSelectedCrop(String selectedCrop) {
		this.selectedCrop = selectedCrop;
	}

	public String getSelectedCooperative() {
		return selectedCooperative;
	}

	public void setSelectedCooperative(String selectedCooperative) {
		this.selectedCooperative = selectedCooperative;
	}

	public String list() throws Exception {
		Calendar currentDate = Calendar.getInstance();
        int iYear = currentDate.get(Calendar.YEAR);
        setCurrentYear(String.valueOf(iYear));
		return LIST;
	}

	@SuppressWarnings("unchecked")
	public void populateMethod() {

		String descStatus = "fa fa-sort-desc text-danger";
		String ascStatus = "fa fa-sort-asc text-success";
		String textClassAsc = "text-success";
		String textClassDesc = "text-danger";
		 

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		JSONObject jsonObject = null;
		jsonObject = new JSONObject();
		localizeMenu();
		request.setAttribute("heading", getText("dashboardlist"));

		branchIdValue = getBranchId();
		if (StringUtil.isEmpty(branchIdValue)) {
			buildBranchMap();
		}
		Integer totalUserCount = userService.findUserCount();
		Integer totalMobileUserCount = agentService.findMobileUserCount();
		Integer totalDeviceCount = deviceService.findDeviceCount();
		Integer totalFarmersCount = farmerService.findFarmersCountByStatus();
		Integer fc = farmerService.findFarmsCount();
		Integer	totalWarehouseCount = locationService.findWarehouseCount();
		
		jsonObject.put("userCount", String.valueOf(totalUserCount));		
		jsonObject.put("mobileUsersCount", String.valueOf(totalMobileUserCount));
		jsonObject.put("deviceCount", String.valueOf(totalDeviceCount));
		jsonObject.put("farmerCount", String.valueOf(totalFarmersCount));
		jsonObject.put("warehouseCount", String.valueOf(totalWarehouseCount));
		jsonObject.put("farmCount", String.valueOf(fc));
		
		jsonObjects.add(jsonObject);
		printAjaxResponse(jsonObjects, "text/html");

	}

	@SuppressWarnings("unchecked")
	public void populateTxnChartData() {
		JSONObject jsonObject = new JSONObject();
		List<List<JSONObject>> jsonObjects = new ArrayList<>();
		List<JSONObject> labelDataJsonObjects = new ArrayList<>();
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.BLRI_TENANT_ID)) {
			List<Object> cowMilkData;
			if (!StringUtil.isEmpty(selectedYear)) {
				cowMilkData = productService.listcowMilkByMonth(
						DateUtil.getFirstDateOfMonth(Integer.valueOf(selectedYear), 0),
						DateUtil.getFirstDateOfMonth(Integer.valueOf(selectedYear), 12));
			} else {
				cowMilkData = productService.listcowMilkByMonth(
						DateUtil.getFirstDateOfMonth(DateUtil.getCurrentYear(), 0),
						DateUtil.getFirstDateOfMonth(DateUtil.getCurrentYear(), 12));
			}

			List<JSONObject> milkDataJsonObjects = new ArrayList<>();

			for (int i = 1; i <= 12; i++) {
				JSONObject supplyDataJson = new JSONObject();
				supplyDataJson.put("id", i);
				supplyDataJson.put("year", "");
				supplyDataJson.put("month", i);
				supplyDataJson.put("Qty", 0);
				milkDataJsonObjects.add(supplyDataJson);
			}

			cowMilkData.stream().forEach(obj -> {
				JSONObject supplyDataJson = new JSONObject();
				Object[] objArr = (Object[]) obj;
				supplyDataJson = milkDataJsonObjects.get(Integer.parseInt(String.valueOf(objArr[1])) - 1);
				supplyDataJson.put("id", objArr[1]);
				supplyDataJson.put("year", objArr[0]);
				supplyDataJson.put("month", objArr[1]);
				supplyDataJson.put("Qty", objArr[2]);
				milkDataJsonObjects.add(supplyDataJson);
			});

			jsonObject.put("Label", getLocaleProperty("groupchart.title"));
			jsonObject.put("disLabel", "");
			jsonObject.put("harvestLabel", getLocaleProperty("cowMilk"));
			jsonObject.put("saleLabel", "");

			labelDataJsonObjects.add(jsonObject);
			jsonObjects.add(new ArrayList<JSONObject>());
			jsonObjects.add(milkDataJsonObjects);
			jsonObjects.add(new ArrayList<JSONObject>());
			jsonObjects.add(labelDataJsonObjects);

		} else {
			if (!StringUtil.isEmpty(getDateRange())) {
				String[] dateSplit = getDateRange().split("-");
				Date sDate = DateUtil.convertStringToDate(dateSplit[0] + " 00:00:00", DateUtil.DATE_TIME_FORMAT);
				Date eDate = DateUtil.convertStringToDate(dateSplit[1] + " 23:59:59", DateUtil.DATE_TIME_FORMAT);

				List<Object> supplyData = productService.listCropSaleQtyByMoth(sDate, eDate,selectedBranch);
				List<Object> harvestData = productService.listCropHarvestByMoth(sDate, eDate,selectedBranch);
				List<Object> distributionData = productService.listDistributionQtyByMoth(sDate, eDate,selectedBranch);

				List<Object> procurementData = productService.listProcurementAmtByMoth(sDate, eDate,selectedBranch);

				List<Object> enrollmentData = productService.listEnrollmentByMoth(sDate, eDate);

				List<JSONObject> supplyDataJsonObjects = new ArrayList<>();
				List<JSONObject> harvestDataJsonObjects = new ArrayList<>();
				List<JSONObject> distributionDataJsonObjects = new ArrayList<>();
				List<JSONObject> procurementDataJsonObjects = new ArrayList<>();
				List<JSONObject> enrollmentDataJsonObjects = new ArrayList<>();
				List<JSONObject> procurementQtyDataJsonObjects = new ArrayList<>();
				for (int i = 1; i <= 12; i++) {
					JSONObject supplyDataJson = new JSONObject();
					supplyDataJson.put("id", i);
					supplyDataJson.put("year", "");
					supplyDataJson.put("month", i);
					supplyDataJson.put("Qty", 0);
					supplyDataJsonObjects.add(supplyDataJson);
				}

				supplyData.stream().forEach(obj -> {
					JSONObject supplyDataJson = new JSONObject();
					Object[] objArr = (Object[]) obj;
					supplyDataJson = supplyDataJsonObjects.get(Integer.parseInt(String.valueOf(objArr[1])) - 1);
					supplyDataJson.put("id", objArr[1]);
					supplyDataJson.put("year", objArr[0]);
					supplyDataJson.put("month", objArr[1]);
					supplyDataJson.put("Qty", objArr[2]);
					// supplyDataJsonObjects.add(supplyDataJson);
					supplyDataJsonObjects.set(Integer.parseInt(String.valueOf(objArr[1])) - 1, supplyDataJson);
				});

				for (int i = 1; i <= 12; i++) {
					JSONObject harvestDataJson = new JSONObject();
					harvestDataJson.put("id", i);
					harvestDataJson.put("year", "");
					harvestDataJson.put("month", i);
					harvestDataJson.put("Qty", 0);
					harvestDataJsonObjects.add(harvestDataJson);
				}

				harvestData.stream().forEach(obj -> {
					JSONObject harvestDataJson = new JSONObject();
					Object[] objArr = (Object[]) obj;
					harvestDataJson = harvestDataJsonObjects.get(Integer.parseInt(String.valueOf(objArr[1])) - 1);
					harvestDataJson.put("year", objArr[0]);
					harvestDataJson.put("month", objArr[1]);
					harvestDataJson.put("Qty", objArr[2]);
					harvestDataJsonObjects.set(Integer.parseInt(String.valueOf(objArr[1])) - 1, harvestDataJson);
				});

				for (int i = 1; i <= 12; i++) {
					JSONObject distributionDataJson = new JSONObject();
					distributionDataJson.put("id", i);
					distributionDataJson.put("year", "");
					distributionDataJson.put("month", i);
					distributionDataJson.put("Qty", 0);
					distributionDataJsonObjects.add(distributionDataJson);
				}

				distributionData.stream().forEach(obj -> {
					JSONObject distributionDataJson = new JSONObject();
					Object[] objArr = (Object[]) obj;
					distributionDataJson = distributionDataJsonObjects
							.get(Integer.parseInt(String.valueOf(objArr[1])) - 1);
					distributionDataJson.put("year", objArr[0]);
					distributionDataJson.put("month", objArr[1]);
					distributionDataJson.put("Qty", objArr[2]);
					distributionDataJsonObjects.set(Integer.parseInt(String.valueOf(objArr[1])) - 1,
							distributionDataJson);
				});

				for (int i = 1; i <= 12; i++) {
					JSONObject procurementDataJson = new JSONObject();
					procurementDataJson.put("year", "");
					procurementDataJson.put("month", i);
					procurementDataJson.put("amt", 0);
					procurementDataJsonObjects.add(procurementDataJson);
				}

				procurementData.stream().forEach(obj -> {
					JSONObject procurementDataJson = new JSONObject();
					Object[] objArr = (Object[]) obj;
					procurementDataJson = procurementDataJsonObjects
							.get(Integer.parseInt(String.valueOf(objArr[1])) - 1);
					procurementDataJson.put("year", objArr[0]);
					procurementDataJson.put("month", objArr[1]);
					procurementDataJson.put("amt", objArr[2]);
					procurementDataJsonObjects.set(Integer.parseInt(String.valueOf(objArr[1])) - 1,
							procurementDataJson);
				});

				for (int i = 1; i <= 12; i++) {
					JSONObject enrollmentDataJson = new JSONObject();
					enrollmentDataJson.put("year", "");
					enrollmentDataJson.put("month", i);
					enrollmentDataJson.put("nos", 0);
					enrollmentDataJsonObjects.add(enrollmentDataJson);
				}

				enrollmentData.stream().forEach(obj -> {
					JSONObject enrollmentDataJson = new JSONObject();
					Object[] objArr = (Object[]) obj;
					enrollmentDataJson = enrollmentDataJsonObjects.get(Integer.parseInt(String.valueOf(objArr[1])) - 1);
					enrollmentDataJson.put("year", objArr[0]);
					enrollmentDataJson.put("month", objArr[1]);
					enrollmentDataJson.put("nos", objArr[2]);
					enrollmentDataJsonObjects.set(Integer.parseInt(String.valueOf(objArr[1])) - 1, enrollmentDataJson);
				});

				for (int i = 1; i <= 12; i++) {
					JSONObject procurementQtyDataJson = new JSONObject();
					procurementQtyDataJson.put("year", "");
					procurementQtyDataJson.put("month", i);
					procurementQtyDataJson.put("qty", 0);
					procurementQtyDataJsonObjects.add(procurementQtyDataJson);
				}

				procurementData.stream().forEach(obj -> {
					JSONObject procurementQtyDataJson = new JSONObject();
					Object[] objArr = (Object[]) obj;
					procurementQtyDataJson = procurementQtyDataJsonObjects
							.get(Integer.parseInt(String.valueOf(objArr[1])) - 1);
					procurementQtyDataJson.put("year", objArr[0]);
					procurementQtyDataJson.put("month", objArr[1]);
					procurementQtyDataJson.put("qty", objArr[3]);
					procurementQtyDataJson.put("unit", "Kgs");
					procurementQtyDataJsonObjects.set(Integer.parseInt(String.valueOf(objArr[1])) - 1,
							procurementQtyDataJson);
				});

				jsonObject.put("Label", getLocaleProperty("groupchart.title"));
				jsonObject.put("disLabel", getLocaleProperty("distribution"));
				jsonObject.put("harvestLabel", getLocaleProperty("cropharvest"));
				jsonObject.put("saleLabel", getLocaleProperty("cropSale"));
				jsonObject.put("procurementLabel", getLocaleProperty("procurement"));
				jsonObject.put("enrollmentLabel", getLocaleProperty("enrollment"));
				jsonObject.put("procurementQtyLabel", getLocaleProperty("procurementQtyLabel"));
				jsonObject.put("unit", "Kg");
				labelDataJsonObjects.add(jsonObject);

				jsonObjects.add(supplyDataJsonObjects);
				jsonObjects.add(harvestDataJsonObjects);
				jsonObjects.add(distributionDataJsonObjects);
				jsonObjects.add(procurementDataJsonObjects);
				jsonObjects.add(enrollmentDataJsonObjects);
				jsonObjects.add(labelDataJsonObjects);
			}
		}

		printAjaxResponse(jsonObjects, "text/html");
	}
	
	@SuppressWarnings("unchecked")
	public void populateFarmersByLocationBranch() {
		JSONArray jsonObjArray = new JSONArray();	
		List<JSONObject> villageCollectorByLocation = new ArrayList<JSONObject>();
		JSONObject final_jsonObject = new JSONObject();
		if (StringUtil.isEmpty(getSelectedBranch())) {
			List<Object[]> farmersByBranch = farmerService.farmersByBranch();
			farmersByBranch.stream().forEach(obj -> {
				Object[] objArr = (Object[]) obj;
				JSONArray jsonArr = new JSONArray();				
				jsonArr.add(objArr[1]);
				jsonArr.add(objArr[0]);				
				jsonObjArray.add(jsonArr);
			});
		}
		final_jsonObject.put("brArray",jsonObjArray);
		villageCollectorByLocation.add(final_jsonObject);
		printAjaxResponse(villageCollectorByLocation, "text/html");
	}
	
	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}

	public String getSelectedFinYear() {
		return selectedFinYear;
	}

	public void setSelectedFinYear(String selectedFinYear) {
		this.selectedFinYear = selectedFinYear;
	}

	public String getCurrentYear() {
		return currentYear;
	}

	public void setCurrentYear(String currentYear) {
		this.currentYear = currentYear;
	}

	public String getSelectedStatus() {
		return selectedStatus;
	}

	public void setSelectedStatus(String selectedStatus) {
		this.selectedStatus = selectedStatus;
	}

	public String getDateRange() {
		return dateRange;
	}

	public void setDateRange(String dateRange) {
		this.dateRange = dateRange;
	}
	
	public String getCurrentSeason() {

		String val = "";
		return val = clientService.findCurrentSeasonCodeByBranchId(getBranchId());
	}
	
	protected void printAjaxResponse(Object value, String contentType) {

		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			if (!StringUtil.isEmpty(contentType)) {
				response.setContentType(contentType);
			}
			out = response.getWriter();
			out.print(value);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getSelectedBranch() {

		if (!StringUtil.isEmpty(getBranchId())) {
			selectedBranch = getBranchId();
		}
		return selectedBranch;
	}
	
	public String getGramPanchayatEnable() {

		return gramPanchayatEnable;
	}

	public void setGramPanchayatEnable(String gramPanchayatEnable) {

		this.gramPanchayatEnable = gramPanchayatEnable;
	}
}
