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
public class DashboardAction extends ESEAction {

	private static final Logger logger = Logger.getLogger(DashboardAction.class);
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
		Integer totalWarehouseCount = 0;

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
		Integer currentMonthUserCount = userService.findUserCountByMonth(DateUtil.getFirstDateOfMonth(), new Date());
		Integer prevMonthUserCount = userService.findUserCountByMonth(DateUtil.getLastMonthStartDate(),
				DateUtil.getLastMonthEndDate());

		Double currentMonthUserPercentage = (Double.parseDouble(String.valueOf(totalUserCount))
				* Double.parseDouble(String.valueOf(currentMonthUserCount))) / 100;

		Double prevMonthUserPercentage = (Double.parseDouble(String.valueOf(totalUserCount))
				* Double.parseDouble(String.valueOf(prevMonthUserCount))) / 100;

		jsonObject.put("userCount", String.valueOf(totalUserCount));
		if ((currentMonthUserPercentage) < (prevMonthUserPercentage)) {
			jsonObject.put("userCountPercentage", String.valueOf(currentMonthUserPercentage));
			jsonObject.put("userCountstauts", descStatus);
			jsonObject.put("userText", textClassDesc);
		} else {
			jsonObject.put("userCountPercentage", String.valueOf(currentMonthUserPercentage));
			jsonObject.put("userCountstauts", ascStatus);
			jsonObject.put("userText", textClassAsc);
		}

		Integer totalMobileUserCount = agentService.findMobileUserCount();
		Integer currentMonthMobileUsersCount = agentService.findMobileUserCountByMonth(DateUtil.getFirstDateOfMonth(),
				new Date());
		Integer prevMonthMobileUserCount = agentService.findMobileUserCountByMonth(DateUtil.getLastMonthStartDate(),
				DateUtil.getLastMonthEndDate());
		Double currentMonthMobileUserPercentage = (Double.parseDouble(String.valueOf(totalMobileUserCount))
				* Double.parseDouble(String.valueOf(currentMonthMobileUsersCount))) / 100;
		Double prevMonthMobileUserPercentage = (Double.parseDouble(String.valueOf(totalMobileUserCount))
				* Double.parseDouble(String.valueOf(prevMonthMobileUserCount))) / 100;

		jsonObject.put("mobileUsersCount", String.valueOf(totalMobileUserCount));
		if ((currentMonthMobileUserPercentage) < (prevMonthMobileUserPercentage)) {
			jsonObject.put("mobileuserCountPercentage", String.valueOf(prevMonthMobileUserPercentage));
			jsonObject.put("mobileuserCountstauts", descStatus);
			jsonObject.put("mobileText", textClassDesc);
		} else {
			jsonObject.put("mobileuserCountPercentage", String.valueOf(currentMonthMobileUserPercentage));
			jsonObject.put("mobileuserCountstauts", ascStatus);
			jsonObject.put("mobileText", textClassAsc);
		}

		Integer totalDeviceCount = deviceService.findDeviceCount();
		Integer currentMonthDeviceCount = deviceService.findDeviceCountByMonth(DateUtil.getFirstDateOfMonth(),
				new Date());
		Integer previousMonthDeviceCount = deviceService.findDeviceCountByMonth(DateUtil.getLastMonthStartDate(),
				DateUtil.getLastMonthEndDate());
		Double currentMonthDevicePercentage = (Double.parseDouble(String.valueOf(totalDeviceCount))
				* Double.parseDouble(String.valueOf(currentMonthDeviceCount))) / 100;
		Double prevMonthDevicePercentage = (Double.parseDouble(String.valueOf(totalDeviceCount))
				* Double.parseDouble(String.valueOf(previousMonthDeviceCount))) / 100;

		jsonObject.put("deviceCount", String.valueOf(totalDeviceCount));
		if ((currentMonthDevicePercentage) < (prevMonthDevicePercentage)) {
			jsonObject.put("deviceCountPercentage", String.valueOf(currentMonthDevicePercentage));
			jsonObject.put("deviceCountstauts", descStatus);
			jsonObject.put("deviceText", textClassDesc);
		} else {
			jsonObject.put("deviceCountPercentage", String.valueOf(currentMonthDevicePercentage));
			jsonObject.put("deviceCountstauts", ascStatus);
			jsonObject.put("deviceText", textClassAsc);
		}

		Integer totalFarmersCount = farmerService.findFarmersCountByStatus();

		Integer totalFarmersCountBySeason = farmerService.findFarmersCountByStatusAndSeason(getCurrentSeason());

		Integer currentMonthFarmerCount = farmerService.findFarmerCountByMonth(DateUtil.getFirstDateOfMonth(),
				new Date());
		Integer previousMonthFarmerCount = farmerService.findFarmerCountByMonth(DateUtil.getLastMonthStartDate(),
				DateUtil.getLastMonthEndDate());
		Double currentMonthFarmerPercentage = (Double.parseDouble(String.valueOf(totalFarmersCount))
				* Double.parseDouble(String.valueOf(currentMonthFarmerCount))) / 100;
		Double prevMonthFarmerPercentage = (Double.parseDouble(String.valueOf(totalFarmersCount))
				* Double.parseDouble(String.valueOf(previousMonthFarmerCount))) / 100;
		jsonObject.put("farmerCount", String.valueOf(totalFarmersCount));
		// jsonObject.put("farmerCount", String.valueOf(totalFarmersCount));
		if ((currentMonthFarmerPercentage) < (prevMonthFarmerPercentage)) {
			jsonObject.put("farmerCountPercentage", String.valueOf(currentMonthFarmerPercentage));
			jsonObject.put("farmerCountstauts", descStatus);
			jsonObject.put("farmerText", textClassDesc);
		} else {
			jsonObject.put("farmerCountPercentage", String.valueOf(currentMonthFarmerPercentage));
			jsonObject.put("farmerCountstauts", ascStatus);
			jsonObject.put("farmerText", textClassAsc);
		}
		/** Cow count **/
		Integer totalCowCount = farmerService.findCowCount();

		Integer currentMonthCowCount = farmerService.findCowCountByMonth(DateUtil.getFirstDateOfMonth(), new Date());

		Integer previousMonthCowCount = farmerService.findCowCountByMonth(DateUtil.getLastMonthStartDate(),
				DateUtil.getLastMonthEndDate());

		Double prevMonthCowPercentage = (Double.parseDouble(String.valueOf(totalCowCount))
				* Double.parseDouble(String.valueOf(previousMonthCowCount))) / 100;

		Double currentMonthCowPercentage = (Double.parseDouble(String.valueOf(totalCowCount))
				* Double.parseDouble(String.valueOf(currentMonthCowCount))) / 100;

		jsonObject.put("cowCount", String.valueOf(totalCowCount));

		if ((currentMonthCowPercentage) < (prevMonthCowPercentage)) {
			jsonObject.put("cowCountPercentage", String.valueOf(currentMonthCowPercentage));
			jsonObject.put("cowCountstauts", descStatus);
			jsonObject.put("cowText", textClassDesc);
		} else {
			jsonObject.put("cowCountPercentage", String.valueOf(currentMonthCowPercentage));
			jsonObject.put("cowCountstauts", ascStatus);
			jsonObject.put("warehouseText", textClassAsc);
		}

		/** warehouse count **/

		if (getCurrentTenantId().equalsIgnoreCase("chetna"))
			totalWarehouseCount = locationService.findFacilitiesCount();
		else
			totalWarehouseCount = locationService.findWarehouseCount();
		Integer currentMonthWarehouseCount = locationService.findWarehouseCountByMonth(DateUtil.getFirstDateOfMonth(),
				new Date());
		Integer previousMonthWarehouseCount = locationService
				.findWarehouseCountByMonth(DateUtil.getLastMonthStartDate(), DateUtil.getLastMonthEndDate());
		Double currentMonthWarehousePercentage = (Double.parseDouble(String.valueOf(totalWarehouseCount))
				* Double.parseDouble(String.valueOf(currentMonthWarehouseCount))) / 100;
		Double prevMonthWarehousePercentage = (Double.parseDouble(String.valueOf(totalWarehouseCount))
				* Double.parseDouble(String.valueOf(previousMonthWarehouseCount))) / 100;
		jsonObject.put("warehouseCount", String.valueOf(totalWarehouseCount));

		if ((currentMonthWarehousePercentage) < (prevMonthWarehousePercentage)) {
			jsonObject.put("warehouseCountPercentage", String.valueOf(currentMonthWarehousePercentage));
			jsonObject.put("warehouseCountstauts", descStatus);
			jsonObject.put("warehouseText", textClassDesc);
		} else {
			jsonObject.put("warehouseCountPercentage", String.valueOf(currentMonthWarehousePercentage));
			jsonObject.put("warehouseCountstauts", ascStatus);
			jsonObject.put("warehouseText", textClassAsc);
		}
		/** Farmer Cotton Count */

		Integer totalFarmerCottonCount = userService.findFarmerCottonCount();
		Integer currentMonthFarmerCottonCount = userService.findFarmerCottonCountByMonth(DateUtil.getFirstDateOfMonth(),
				new Date());
		Integer prevMonthFarmerCottonCount = userService.findFarmerCottonCountByMonth(DateUtil.getLastMonthStartDate(),
				DateUtil.getLastMonthEndDate());

		Double currentMonthFarmerCottonPercentage = (Double.parseDouble(String.valueOf(totalFarmerCottonCount))
				* Double.parseDouble(String.valueOf(currentMonthFarmerCottonCount))) / 100;

		Double prevMonthFarmerCottonPercentage = (Double.parseDouble(String.valueOf(totalFarmerCottonCount))
				* Double.parseDouble(String.valueOf(prevMonthFarmerCottonCount))) / 100;

		jsonObject.put("farmerCottonCount", String.valueOf(totalFarmerCottonCount));
		if ((currentMonthFarmerCottonPercentage) < (prevMonthFarmerCottonPercentage)) {
			jsonObject.put("farmerCottonPercentage", String.valueOf(currentMonthFarmerCottonPercentage));
			jsonObject.put("farmerCottonStatus", descStatus);
			jsonObject.put("userText", textClassDesc);
		} else {
			jsonObject.put("farmerCottonPercentage", String.valueOf(currentMonthFarmerCottonPercentage));
			jsonObject.put("farmerCottonStatus", ascStatus);
			jsonObject.put("userText", textClassAsc);
		}

		DecimalFormat df = new DecimalFormat("0.00");
		String totalFarmLandCount = !StringUtil.isEmpty(farmerService.findFarmTotalLandAreaCount())
				? farmerService.findFarmTotalLandAreaCount() : "0";
		String count = (df.format(Double.valueOf(totalFarmLandCount)));
		Integer currentMonthTotalFarmLandCount = farmerService.findFarmCountByMonth(DateUtil.getFirstDateOfMonth(),
				new Date());
		Integer previousMonthTotalFarmLandCount = farmerService.findFarmCountByMonth(DateUtil.getLastMonthStartDate(),
				DateUtil.getLastMonthEndDate());
		Double currentMonthTotalFarmLandPercentage = (Double.parseDouble(totalFarmLandCount)
				* Double.parseDouble(String.valueOf(currentMonthTotalFarmLandCount))) / 100;
		Double prevMonthTotalFarmLandPercentage = (Double.parseDouble(totalFarmLandCount)
				* Double.parseDouble(String.valueOf(previousMonthTotalFarmLandCount))) / 100;
		jsonObject.put("farmLandCount", String.valueOf(count));

		if ((currentMonthTotalFarmLandPercentage) < (prevMonthTotalFarmLandPercentage)) {
			jsonObject.put("farmLandCountPercentage", String.valueOf(df.format(currentMonthTotalFarmLandPercentage)));
			jsonObject.put("farmLandCountstauts", descStatus);
			jsonObject.put("farmLandText", textClassDesc);
		} else {
			jsonObject.put("farmLandCountPercentage", String.valueOf(df.format(currentMonthTotalFarmLandPercentage)));
			jsonObject.put("farmLandCountstauts", ascStatus);
			jsonObject.put("farmLandText", textClassAsc);
		}

		Double totalCottonAreaCount = farmerService.findTotalCottonAreaCount();

		Double currentMonthCottonAreaCount = !StringUtil
				.isEmpty(farmerService.findTotalCottonAreaCountByMonth(DateUtil.getFirstDateOfMonth(), new Date()))
						? farmerService.findTotalCottonAreaCountByMonth(DateUtil.getFirstDateOfMonth(), new Date()) : 0;

		Double previousMonthCottonAreaCount = !StringUtil.isEmpty(farmerService
				.findTotalCottonAreaCountByMonth(DateUtil.getLastMonthStartDate(), DateUtil.getLastMonthEndDate()))
						? farmerService.findTotalCottonAreaCountByMonth(DateUtil.getLastMonthStartDate(),
								DateUtil.getLastMonthEndDate())
						: 0;

		Double currentMonthCottonAreaPercentage = !StringUtil
				.isEmpty((Double.parseDouble(String.valueOf(totalCottonAreaCount))
						* Double.parseDouble(String.valueOf(currentMonthCottonAreaCount))) / 100)
								? (Double.parseDouble(String.valueOf(totalCottonAreaCount))
										* Double.parseDouble(String.valueOf(currentMonthCottonAreaCount))) / 100
								: 0D;

		Double prevMonthFarmPercentage = !StringUtil.isEmpty((Double.parseDouble(String.valueOf(totalCottonAreaCount))
				* Double.parseDouble(String.valueOf(previousMonthCottonAreaCount))) / 100)
						? (Double.parseDouble(String.valueOf(totalCottonAreaCount))
								* Double.parseDouble(String.valueOf(previousMonthCottonAreaCount))) / 100
						: 0D;
		Integer fc = farmerService.findFarmsCount();

		jsonObject.put("farmCount", String.valueOf(fc));
		if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
			jsonObject.put("farmCount", df.format(totalCottonAreaCount));
		} else {
			Integer totalFarmCount = farmerService.findFarmsCount();
			jsonObject.put("farmCount", String.valueOf(totalFarmCount));
		}
		if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
			jsonObject.put("farmCount", df.format(totalCottonAreaCount));
		} else {
			Integer totalFarmCount = farmerService.findFarmsCount();
			jsonObject.put("farmCount", String.valueOf(totalFarmCount));
		}

		if ((currentMonthCottonAreaPercentage) < (prevMonthFarmPercentage)) {
			jsonObject.put("farmCountPercentage", String.valueOf(currentMonthCottonAreaPercentage));
			jsonObject.put("farmCountstauts", descStatus);
			jsonObject.put("farmText", textClassDesc);
		} else {
			jsonObject.put("farmCountPercentage", String.valueOf(currentMonthCottonAreaPercentage));
			jsonObject.put("farmCountstauts", ascStatus);
			jsonObject.put("farmText", textClassAsc);
		}

		/** Crop Count **/

		Integer totalCropCount = farmerService.findCropCount();

		jsonObject.put("cropCount", String.valueOf(totalCropCount));

		/** Group Count **/

		Integer totalGroupCount = locationService.findGroupCount();
		Integer currentMonthGroupCount = locationService.findGroupCountByMonth(DateUtil.getFirstDateOfMonth(),
				new Date());
		Integer previousMonthGroupCount = locationService.findGroupCountByMonth(DateUtil.getLastMonthStartDate(),
				DateUtil.getLastMonthEndDate());
		Double currentMonthGroupPercentage = (Double.parseDouble(String.valueOf(totalGroupCount))
				* Double.parseDouble(String.valueOf(currentMonthGroupCount))) / 100;
		Double prevMonthGroupPercentage = (Double.parseDouble(String.valueOf(totalGroupCount))
				* Double.parseDouble(String.valueOf(previousMonthGroupCount))) / 100;
		jsonObject.put("groupCount", String.valueOf(totalGroupCount));

		if ((currentMonthGroupPercentage) < (prevMonthGroupPercentage)) {
			jsonObject.put("groupCountPercentage", String.valueOf(currentMonthGroupPercentage));
			jsonObject.put("groupCountstauts", descStatus);
			jsonObject.put("groupText", textClassDesc);
		} else {
			jsonObject.put("groupCountPercentage", String.valueOf(currentMonthGroupPercentage));
			jsonObject.put("groupCountstauts", ascStatus);
			jsonObject.put("groupText", textClassAsc);
		}

		// System.out.println(currentMonthUserCount + "-" + prevMonthUserCount);

		jsonObjects.add(jsonObject);
		printAjaxResponse(jsonObjects, "text/html");

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

	@SuppressWarnings("unchecked")
	public void populateWarehouseStockChartData() {
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();

		List<Object> stockData = new LinkedList<>();
		if (!StringUtil.isEmpty(getSelectedWarehouse())) {
			stockData = locationService.listWarehouseProductAndStockByWarehouseId(Long.valueOf(getSelectedWarehouse()));
		} else {
			stockData = locationService.listWarehouseProductAndStock();
		}

		JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("Label", getLocaleProperty("donutChart.stock"));

		jsonObjects.add(jsonObject1);

		JSONObject jsonobj2 = new JSONObject();
		jsonobj2.put("Save", getLocaleProperty("save"));
		jsonObjects.add(jsonobj2);

		JSONObject jsonObject3 = new JSONObject();
		jsonObject3.put("refresh", getLocaleProperty("refresh"));
		jsonObjects.add(jsonObject3);
		if (stockData.size() > 5) {
			stockData.stream().limit(5).forEach(obj -> {
				JSONObject jsonObject = new JSONObject();
				Object[] objArr = (Object[]) obj;
				jsonObject.put("stock", objArr[0]);
				if (!getCurrentTenantId().equals("chetna")) {
					jsonObject.put("name", objArr[1]);
				} else {
					jsonObject.put("name", objArr[1]);
				}

				jsonObjects.add(jsonObject);
			});

			List<Object> subStockData = stockData.subList(5, (stockData.size()));
			Double totalStock = 0d;
			for (Object obj : subStockData) {
				Object[] objArr = (Object[]) obj;
				String val = String.valueOf(objArr[0]);
				totalStock += Double.parseDouble(val.trim());
			}

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("stock", totalStock);
			jsonObject.put("name", "Others");
			jsonObject.put("save", getLocaleProperty("save"));
			jsonObject.put("refresh", getLocaleProperty("refresh"));
			jsonObjects.add(jsonObject);
			printAjaxResponse(jsonObjects, "text/html");

		} else {
			for (Object obj : stockData) {
				JSONObject jsonObject = new JSONObject();
				Object[] objArr = (Object[]) obj;
				jsonObject.put("stock", objArr[0]);
				jsonObject.put("name", objArr[1]);
				jsonObjects.add(jsonObject);
			}
			printAjaxResponse(jsonObjects, "text/html");
		}
	}

	@SuppressWarnings("unchecked")
	public void populateFarmerGroupChartData() {

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();

		JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("Label", getLocaleProperty("donutChart.farmerGroup"));
		jsonObjects.add(jsonObject1);

		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put("save", getLocaleProperty("save"));
		jsonObjects.add(jsonObject2);
		JSONObject jsonObject3 = new JSONObject();
		jsonObject3.put("refresh", getLocaleProperty("refresh"));
		jsonObjects.add(jsonObject3);
		/*
		 * if (getCurrentTenantId().equalsIgnoreCase(ESESystem.KPF_TENANT_ID) ||
		 * getCurrentTenantId().equalsIgnoreCase(ESESystem.CABI_TENANT_ID))
		 */
		if (getIsKpfBased().equals("1")) {

			List<Object> farmerData = farmerService.listFarmerCountByFpoGroup();
			if (farmerData.size() > 5) {
				farmerData.stream().limit(5).forEach(obj -> {
					JSONObject jsonObject = new JSONObject();
					Object[] objArr = (Object[]) obj;
					jsonObject.put("count", objArr[0]);
					jsonObject.put("group", objArr[1] + "(" + objArr[0] + ")");
					jsonObjects.add(jsonObject);
				});

				List<Object> subFarmerData = farmerData.subList(5, farmerData.size());

				Double toatlFarmerCount = 0d;
				for (Object obj : subFarmerData) {
					Object[] objArr = (Object[]) obj;
					String val = String.valueOf(objArr[0]);
					toatlFarmerCount += Double.parseDouble(val.trim());
				}

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("count", toatlFarmerCount);
				jsonObject.put("group", "Others");
				jsonObject.put("save", getLocaleProperty("save"));
				jsonObject.put("refresh", getLocaleProperty("refresh"));
				jsonObjects.add(jsonObject);
				printAjaxResponse(jsonObjects, "text/html");
			} else {

				farmerData.stream().forEach(obj -> {
					JSONObject jsonObject = new JSONObject();
					Object[] objArr = (Object[]) obj;
					jsonObject.put("count", objArr[0]);
					jsonObject.put("group", objArr[1]);
					jsonObjects.add(jsonObject);
				});

				printAjaxResponse(jsonObjects, "text/html");
			}

		} else if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PGSS_TENANT_ID)) {

			List<Object> farmerData = farmerService.listFarmerCountByGroupByType();
			if (farmerData.size() > 5) {
				farmerData.stream().limit(5).forEach(obj -> {
					JSONObject jsonObject = new JSONObject();
					Object[] objArr = (Object[]) obj;
					jsonObject.put("count", objArr[0]);
					jsonObject.put("group", objArr[1] + "(" + objArr[0] + ")");
					jsonObjects.add(jsonObject);
				});

				List<Object> subFarmerData = farmerData.subList(5, farmerData.size());

				Double toatlFarmerCount = 0d;
				for (Object obj : subFarmerData) {
					Object[] objArr = (Object[]) obj;
					String val = String.valueOf(objArr[0]);
					toatlFarmerCount += Double.parseDouble(val.trim());
				}

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("count", toatlFarmerCount);
				jsonObject.put("group", "Others");
				jsonObject.put("save", getLocaleProperty("save"));
				jsonObject.put("refresh", getLocaleProperty("refresh"));
				jsonObjects.add(jsonObject);
				printAjaxResponse(jsonObjects, "text/html");
			} else {

				farmerData.stream().forEach(obj -> {
					JSONObject jsonObject = new JSONObject();
					Object[] objArr = (Object[]) obj;
					jsonObject.put("count", objArr[0]);
					jsonObject.put("group", objArr[1]);
					jsonObjects.add(jsonObject);
				});

				printAjaxResponse(jsonObjects, "text/html");
			}

		} else {

			List<Object> farmerData = farmerService.listFarmerCountByGroup();
			if (farmerData.size() > 5) {
				farmerData.stream().limit(5).forEach(obj -> {
					JSONObject jsonObject = new JSONObject();
					Object[] objArr = (Object[]) obj;
					jsonObject.put("count", objArr[0]);
					jsonObject.put("group", objArr[1] + "(" + objArr[0] + ")");
					jsonObjects.add(jsonObject);
				});

				List<Object> subFarmerData = farmerData.subList(5, farmerData.size());

				Double toatlFarmerCount = 0d;
				for (Object obj : subFarmerData) {
					Object[] objArr = (Object[]) obj;
					String val = String.valueOf(objArr[0]);
					toatlFarmerCount += Double.parseDouble(val.trim());
				}

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("count", toatlFarmerCount);
				jsonObject.put("group", "Others");
				jsonObject.put("save", getLocaleProperty("save"));
				jsonObject.put("refresh", getLocaleProperty("refresh"));
				jsonObjects.add(jsonObject);
				printAjaxResponse(jsonObjects, "text/html");
			} else {

				farmerData.stream().forEach(obj -> {
					JSONObject jsonObject = new JSONObject();
					Object[] objArr = (Object[]) obj;
					jsonObject.put("count", objArr[0]);
					jsonObject.put("group", objArr[1]);
					jsonObjects.add(jsonObject);
				});

				printAjaxResponse(jsonObjects, "text/html");
			}

		}

	}

	public void populateCowCountByVillageBarChartData() {

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<Object> cowData = farmerService.listCowCountByVillage();

		JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("Label", getLocaleProperty("donutChart.cowVillage"));
		jsonObjects.add(jsonObject1);

		if (cowData.size() > 5) {
			cowData.stream().limit(5).forEach(obj -> {
				JSONObject jsonObject = new JSONObject();
				Object[] objArr = (Object[]) obj;
				jsonObject.put("count", objArr[0]);
				jsonObject.put("village", objArr[1]);
				jsonObjects.add(jsonObject);
			});

			List<Object> subCowVillageData = cowData.subList(5, (cowData.size()) - 1);

			Double toatlCowCount = 0d;
			for (Object obj : subCowVillageData) {
				Object[] objArr = (Object[]) obj;
				String val = String.valueOf(objArr[0]);
				toatlCowCount += Double.parseDouble(val.trim());
			}

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("count", toatlCowCount);
			jsonObject.put("village", "Others");
			jsonObjects.add(jsonObject);
			printAjaxResponse(jsonObjects, "text/html");
		} else {

			cowData.stream().forEach(obj -> {
				JSONObject jsonObject = new JSONObject();
				Object[] objArr = (Object[]) obj;
				jsonObject.put("count", objArr[0]);
				jsonObject.put("village", objArr[1]);
				jsonObjects.add(jsonObject);
			});
			if (!ObjectUtil.isListEmpty(cowData)) {
				printAjaxResponse(jsonObjects, "text/html");
			} else {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("count", "");
				jsonObject.put("village", "");
				jsonObjects.add(jsonObject);
				printAjaxResponse(jsonObjects, "text/html");
			}
		}
	}

	public void populateCowCountByRSBarChartData() {

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<Object> cowRSData = farmerService.listCowCountByRS();

		JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("Label", getLocaleProperty("donutChart.cowRS"));
		jsonObjects.add(jsonObject1);

		if (cowRSData.size() > 5) {
			cowRSData.stream().limit(5).forEach(obj -> {
				JSONObject jsonObject = new JSONObject();
				Object[] objArr = (Object[]) obj;
				jsonObject.put("count", objArr[0]);
				jsonObject.put("researchStation", objArr[1]);
				jsonObjects.add(jsonObject);
			});

			List<Object> subCowRSData = cowRSData.subList(5, (cowRSData.size()) - 1);

			Double toatlCowRSCount = 0d;
			for (Object obj : subCowRSData) {
				Object[] objArr = (Object[]) obj;
				String val = String.valueOf(objArr[0]);
				toatlCowRSCount += Double.parseDouble(val.trim());
			}

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("count", toatlCowRSCount);
			jsonObject.put("researchStation", "Others");
			jsonObjects.add(jsonObject);
			printAjaxResponse(jsonObjects, "text/html");
		} else {

			cowRSData.stream().forEach(obj -> {
				JSONObject jsonObject = new JSONObject();
				Object[] objArr = (Object[]) obj;
				jsonObject.put("count", objArr[0]);
				jsonObject.put("researchStation", objArr[1]);
				jsonObjects.add(jsonObject);
			});
			if (!ObjectUtil.isListEmpty(cowRSData)) {
				printAjaxResponse(jsonObjects, "text/html");
			} else {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("count", "");
				jsonObject.put("researchStation", "");
				jsonObjects.add(jsonObject);
				printAjaxResponse(jsonObjects, "text/html");
			}
		}
	}

	public void populateFarmerCountByGroupBarChartData() {

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<Object> farmerData;
		int countGroup = 10;
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PGSS_TENANT_ID)) {
			typez = Farmer.FARMER;
		}
		farmerData = farmerService.listFarmerCountByGroupAndBranchStateCoop(getSelectedBranch(), selectedState,
				selectedCooperative, selectedGender, selectedCrop, typez, selectedVillage);
		/*
		 * else { farmerData =
		 * farmerService.listFarmerCountByGroupAndBranch(getSelectedBranch(),
		 * Integer.parseInt(selectedYear)); countGroup=5; }
		 */

		/*JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("Label", getLocaleProperty("donutChart.farmerGroup"));
		jsonObjects.add(jsonObject1);

		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put("save", getLocaleProperty("save"));
		jsonObjects.add(jsonObject2);
		JSONObject jsonObject3 = new JSONObject();
		jsonObject3.put("refresh", getLocaleProperty("refresh"));
		jsonObjects.add(jsonObject3);*/

		/*if (farmerData.size() > countGroup) {
			farmerData.stream().limit(countGroup).forEach(obj -> {
				JSONObject jsonObject = new JSONObject();
				Object[] objArr = (Object[]) obj;
				jsonObject.put("count", objArr[0]);
				jsonObject.put("group", objArr[1]);
				jsonObject.put("village", objArr[2]);
				jsonObjects.add(jsonObject);
			});

			List<Object> subFarmerData = farmerData.subList(countGroup, farmerData.size());

			Double toatlFarmerCount = 0d;
			for (Object obj : subFarmerData) {
				Object[] objArr = (Object[]) obj;
				String val = String.valueOf(objArr[0]);
				toatlFarmerCount += Double.parseDouble(val.trim());
			}

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("count", toatlFarmerCount);
			jsonObject.put("group", "Others");
			jsonObject.put("save", getLocaleProperty("save"));
			jsonObject.put("refresh", getLocaleProperty("refresh"));
			jsonObjects.add(jsonObject);
			printAjaxResponse(jsonObjects, "text/html");
		} else {

			farmerData.stream().forEach(obj -> {
				JSONObject jsonObject = new JSONObject();
				Object[] objArr = (Object[]) obj;
				jsonObject.put("count", objArr[0]);
				jsonObject.put("group", objArr[1]);
				jsonObject.put("village", objArr[2]);
				jsonObjects.add(jsonObject);
			});
			if (!ObjectUtil.isListEmpty(farmerData)) {
				printAjaxResponse(jsonObjects, "text/html");
			} else {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("count", "");
				jsonObject.put("group", "");
				jsonObject.put("village", "");
				jsonObjects.add(jsonObject);
				printAjaxResponse(jsonObjects, "text/html");
			}
		}*/
		
		farmerData.stream().forEach(obj -> {
			JSONObject jsonObject = new JSONObject();
			Object[] objArr = (Object[]) obj;
			jsonObject.put("count", objArr[0]);
			jsonObject.put("group", objArr[1]);
			jsonObject.put("village", objArr[2]);
			jsonObjects.add(jsonObject);
		});
		/*if (!ObjectUtil.isListEmpty(farmerData)) {
			printAjaxResponse(jsonObjects, "text/html");
		}*/ /*else {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("count", "");
			jsonObject.put("group", "");
			jsonObject.put("village", "");
			jsonObjects.add(jsonObject);
			printAjaxResponse(jsonObjects, "text/html");
		}*/
		/*JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("label", getLocaleProperty("farmerDetailsChart"));
		jsonObjects.add(jsonObject1);
		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put("save", getLocaleProperty("save"));
		jsonObjects.add(jsonObject2);
		JSONObject jsonObject3 = new JSONObject();
		jsonObject3.put("refresh", getLocaleProperty("refresh"));
		jsonObjects.add(jsonObject3);*/
		printAjaxResponse(jsonObjects, "text/html");
		
	}
	
	
	
	public void populateFarmerCountByGroupTraderBarChartData() {

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<Object> farmerData;
		Map<String, String> fdMap = new HashMap<>();
		int countGroup = 10;
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PGSS_TENANT_ID)) {
			typez = Farmer.FARMER;
		}
		farmerData = farmerService.listFarmerCountByGroupTraderAndBranchStateCoop(getSelectedBranch(), selectedState,
				selectedCooperative, selectedGender, selectedCrop, typez, selectedVillage);
		
		List<MasterData> masterData = farmerService.listMasterType();
		masterData.stream().forEach(u -> {
		fdMap.put(u.getCode(),u.getName());
		});
		farmerData.stream().forEach(obj -> {
			JSONObject jsonObject = new JSONObject();
			Object[] objArr = (Object[]) obj;
			jsonObject.put("count", objArr[0]);
			jsonObject.put("group", fdMap.get(objArr[1]));
			jsonObject.put("village", objArr[2]);
			jsonObjects.add(jsonObject);
		});
		if (!ObjectUtil.isListEmpty(farmerData)) {
			printAjaxResponse(jsonObjects, "text/html");
		}
		
	}
	
	
	public void populateFarmerCountByFarmInspectionBarChartData() {

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<Object> farmerData;
		int countGroup = 10;
		
		/*farmerData = farmerService.listFarmerCountByGroupTraderAndBranchStateCoop(getSelectedBranch(), selectedState,
				selectedCooperative, selectedGender, selectedCrop, typez, selectedVillage);*/
		if (!StringUtil.isEmpty(getDateRange())) {
			String[] dateSplit = getDateRange().split("-");
			Date sDate = DateUtil.convertStringToDate(dateSplit[0] + " 00:00:00", DateUtil.DATE_TIME_FORMAT);
			Date eDate = DateUtil.convertStringToDate(dateSplit[1] + " 23:59:59", DateUtil.DATE_TIME_FORMAT);
		farmerData=farmerService.listFarmerCountByFarmInspection(sDate,eDate);
		farmerData.stream().forEach(obj -> {
			JSONObject jsonObject = new JSONObject();
			Object[] objArr = (Object[]) obj;
			jsonObject.put("count", objArr[0]);
			jsonObject.put("group", objArr[1]);
			//jsonObject.put("village", objArr[2]);
			jsonObjects.add(jsonObject);
		});
			printAjaxResponse(jsonObjects, "text/html");
		}	
	}


	public void populateCowCountByDiseaseBarChartData() {

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<Object> cowDiseaseData = farmerService.listCowCountByDiseaseName();

		JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("Label", getLocaleProperty("donutChart.cowDisease"));
		jsonObjects.add(jsonObject1);

		if (cowDiseaseData.size() > 5) {

			cowDiseaseData.stream().forEach(obj -> {
				JSONObject jsonObject = new JSONObject();
				Object[] objArr = (Object[]) obj;
				FarmCatalogue catalogue = getCatlogueValueByCode(String.valueOf(objArr[1]));
				if (!ObjectUtil.isEmpty(catalogue)) {
					jsonObject.put("count", objArr[0]);
					jsonObject.put("diseaseNameType", catalogue.getName());
				}

				jsonObjects.add(jsonObject);
			});
			List<Object> subCowRSData = cowDiseaseData.subList(5, (cowDiseaseData.size()) - 1);

			Double toatlCowRSCount = 0d;
			for (Object obj : subCowRSData) {
				Object[] objArr = (Object[]) obj;
				String val = String.valueOf(objArr[0]);
				toatlCowRSCount += Double.parseDouble(val.trim());
			}

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("count", toatlCowRSCount);
			jsonObject.put("diseaseNameType", "Others");
			jsonObjects.add(jsonObject);
			printAjaxResponse(jsonObjects, "text/html");

		} else {

			cowDiseaseData.stream().forEach(obj -> {
				JSONObject jsonObject = new JSONObject();
				Object[] objArr = (Object[]) obj;
				FarmCatalogue catalogue = getCatlogueValueByCode(String.valueOf(objArr[1]));
				if (!ObjectUtil.isEmpty(catalogue)) {
					jsonObject.put("count", objArr[0]);
					jsonObject.put("diseaseNameType", catalogue.getName());
				}
				jsonObjects.add(jsonObject);
			});
			if (!ObjectUtil.isListEmpty(cowDiseaseData)) {
				printAjaxResponse(jsonObjects, "text/html");
			} else {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("count", "");
				jsonObject.put("diseaseNameType", "");
				jsonObjects.add(jsonObject);
				printAjaxResponse(jsonObjects, "text/html");
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void populateCowCostChartData() {

		List<Object> cowCost = farmerService.findByCowCost();
		cowCost.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			if (!StringUtil.isEmpty(objArr[0]) && !StringUtil.isEmpty(objArr[1]) && !StringUtil.isEmpty(objArr[2])
					&& !StringUtil.isEmpty(objArr[3])) {
				totalHousingCost += Double.valueOf(objArr[0].toString());
				totalFeedCost += Double.valueOf(objArr[1].toString());
				totalTreatmentCost += Double.valueOf(objArr[2].toString());
				totalOtherCost += Double.valueOf(objArr[3].toString());

			}
		});
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("totalHousingCost", (totalHousingCost > 0L) ? formatter.format(totalHousingCost) : "-");
		jsonObject.put("totalFeedCost", (totalFeedCost > 0L) ? formatter.format(totalFeedCost) : "-");
		jsonObject.put("totalTreatmentCost", (totalTreatmentCost > 0L) ? formatter.format(totalTreatmentCost) : "-");
		jsonObject.put("totalOtherCost", (totalOtherCost > 0L) ? formatter.format(totalOtherCost) : "-");
		jsonObject.put("label", getLocaleProperty("cowCostChart"));
		jsonObject.put("housingCostLabel", getLocaleProperty("donutChart.totalHousingCost"));
		jsonObject.put("feedCostLabel", getLocaleProperty("donutChart.totalFeedCost"));
		jsonObject.put("treatmentCostLabel", getLocaleProperty("donutChart.totalTreatmentCost"));
		jsonObject.put("otherLabel", getLocaleProperty("donutChart.totalOtherCost"));

		printAjaxResponse(jsonObject, "text/html");

	}

	public void populateFarmerDetailsBarChartData() {
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<String> combineValues = new LinkedList<>();

		Integer farmerData = new Integer(0);

		String selectedState = selectedYear;
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
		farmerData = farmerService.findFarmerCountByStateAndCrop(selectedBranch,
				!StringUtil.isEmpty(selectedYear) && selectedYear != "" ? Integer.parseInt(selectedYear) : 0,
				selectedCrop, selectedCooperative, selectedGender,selectedFinYear);
		}else if(getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)) {
			farmerData = farmerService.findFarmerCountByStateAndCropbyFarmerStatus(selectedBranch,
					!StringUtil.isEmpty(selectedYear) && selectedYear != "" ? Integer.parseInt(selectedYear) : 0,
					selectedCrop, selectedCooperative, selectedGender,selectedStatus);
		}
		else{
		farmerData = farmerService.findFarmerCountByStateAndCrop(selectedBranch,
				!StringUtil.isEmpty(selectedYear) && selectedYear != "" ? Integer.parseInt(selectedYear) : 0,
				selectedCrop, selectedCooperative, selectedGender);
		}
		combineValues.add("donutChart.farmer-" + farmerData.toString());
		List<Object> totalLandDetails;
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
			totalLandDetails = farmerService.findFarmerDetailsByStateAndCrop(selectedBranch,
					Long.valueOf(!StringUtil.isEmpty(selectedState) ? selectedState : "0"), selectedCrop,
					selectedCooperative, selectedGender,selectedFinYear);
		} else if(getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)) {
			totalLandDetails = farmerService.findFarmerDetailsByStateAndCropbyFarmerStatus(selectedBranch,
					Long.valueOf(!StringUtil.isEmpty(selectedState) ? selectedState : "0"), selectedCrop,
					selectedCooperative, selectedGender,selectedStatus);
		}
		else {
			totalLandDetails = farmerService.findFarmerDetailsByStateAndCrop(selectedBranch,
					Long.valueOf(!StringUtil.isEmpty(selectedState) ? selectedState : "0"), selectedCrop,
					selectedCooperative, selectedGender);
		}
		totalLandDetails.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			if (!StringUtil.isEmpty(objArr[0]) && !StringUtil.isEmpty(objArr[1])) {
				combineValues.add(getLocaleProperty("donutChart.totalLandProduction") + "(" + getAreaType() + ")" + "-"
						+ formatter.format(Double.parseDouble(objArr[1].toString())));
				/*
				 * combineValues.add("donutChart.totalEstimatedYield-" +
				 * String.valueOf(Math.round(Double.parseDouble(objArr[0].
				 * toString()))));
				 */	if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)) {
					combineValues.add(getLocaleProperty("donutChart.totalEstimatedYield") + "(" + getLocaleProperty("units") + ")" + "-"
							+ String.valueOf(Math.round(Double.parseDouble(objArr[0].toString()))/100));
				 }else{
			
				     combineValues.add(getLocaleProperty("donutChart.totalEstimatedYield") + "(" + getLocaleProperty("units") + ")" + "-"
						+ String.valueOf(Math.round(Double.parseDouble(objArr[0].toString()))));
				
				 }

			}
		});
		/*
		 * else {
		 * 
		 * farmerData = farmerService.findTotalFarmerCount(selectedBranch,
		 * !StringUtil.isEmpty(selectedYear) ? Integer.parseInt(selectedYear) :
		 * 0);
		 * 
		 * combineValues.add("donutChart.farmer-" + farmerData.toString());
		 * totalProposedPlantArea =
		 * farmerService.findTotalLandAcre(selectedBranch,
		 * Integer.parseInt(selectedYear)); totalEstimatedYield =
		 * farmerService.findTotalYieldByBranch(selectedBranch,
		 * Integer.parseInt(selectedYear));
		 * 
		 * combineValues.add("donutChart.totalLandProduction-" +
		 * formatter.format(
		 * Double.parseDouble(!StringUtil.isEmpty(totalProposedPlantArea) ?
		 * totalProposedPlantArea : "0.0")));
		 * combineValues.add("donutChart.totalEstimatedYield-" +
		 * String.valueOf(Math.round(StringUtil.isDouble(totalEstimatedYield) ?
		 * totalEstimatedYield : 0.0)));
		 * 
		 * 
		 * 
		 * }
		 */

		combineValues.stream().forEach(obj -> {
			JSONObject jsonObject = new JSONObject();
			String value = (String) obj;
			String[] split = value.split("-");
			jsonObject.put("item", getLocaleProperty(split[0]));
			jsonObject.put("values", Double.valueOf(split[1]));
			jsonObjects.add(jsonObject);
		});

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("label", getLocaleProperty("farmerDetailsChart"));
		jsonObjects.add(jsonObject);
		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put("save", getLocaleProperty("save"));
		jsonObjects.add(jsonObject2);
		JSONObject jsonObject3 = new JSONObject();
		jsonObject3.put("refresh", getLocaleProperty("refresh"));
		jsonObjects.add(jsonObject3);
		printAjaxResponse(jsonObjects, "text/html");
	}

	public void populateCropsDetailChartData() {
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<String> combineValues = new LinkedList<>();
		Double inputCost = 0.0;

		inputCost = farmerService.findCocCostByCropBranchCooperativeAndGender(selectedBranch, selectedCooperative,
				selectedCrop, selectedGender, selectedYear, selectedState);
		Integer totalCultivationLandProduction = new Integer(0);

		if (!getCurrentTenantId().equals("chetna")) {
			Long incomeFromCotton = new Long(0);
			if (!StringUtil.isEmpty(selectedYear)) {
				totalCultivationLandProduction = farmerService.findTotalCultivationProdLandByBranch(selectedBranch,
						Integer.valueOf(selectedYear));

				incomeFromCotton = farmerService.findTotalIncomeFromCottonByBranch(selectedBranch,
						Integer.parseInt(selectedYear));
			}
			Long cultivatedFarmersCount = new Long(0);
			if (!StringUtil.isEmpty(selectedBranch)) {
				cultivatedFarmersCount = farmerService.findCultivatedFarmersCountByBranch(selectedBranch);
			} else {
				cultivatedFarmersCount = farmerService.findCultivatedFarmersCount();
			}

			if (totalCultivationLandProduction > 0 && !StringUtil.isEmpty(inputCost) && inputCost > 0.0) {
				Double costPerUnitLand = inputCost / Double.valueOf(totalCultivationLandProduction);
				combineValues.add(getText("costPerUnit") + "-" + costPerUnitLand);

				Double incomePerUnitLand = Double.valueOf(incomeFromCotton)
						/ Double.valueOf(totalCultivationLandProduction);
				combineValues.add(getLocaleProperty("incomePerUnit") + "-" + incomePerUnitLand);
			} else {
				combineValues.add(getText("costPerUnit") + "-" + "0");
				combineValues.add(getLocaleProperty("incomePerUnit") + "-" + "0");
			}

			if (cultivatedFarmersCount > 0 && !StringUtil.isEmpty(inputCost) && inputCost > 0.0) {
				Double costPerCaptia = inputCost / Double.valueOf(cultivatedFarmersCount);
				combineValues.add(getLocaleProperty("costPerCapita") + "-" + costPerCaptia);

				Double avgIncomeFromCotton = Double.valueOf(incomeFromCotton) / Double.valueOf(cultivatedFarmersCount);
				combineValues.add(getLocaleProperty("avgIncomeCotton") + "-" + avgIncomeFromCotton);
			} else {
				combineValues.add(getLocaleProperty("costPerCapita") + "-" + "0");
				combineValues.add(getLocaleProperty("avgIncomeCotton") + "-" + "0");
			}
		} else {
			totalCultivationLandProduction = farmerService.findTotalCultivationProdLandByState(selectedBranch,
					selectedState, selectedCrop, selectedCooperative, selectedGender);
			Long incomeFromCotton = new Long(0);
			incomeFromCotton = farmerService.findTotalIncomeFromCottonByState(selectedBranch, selectedState,
					selectedCrop, selectedCooperative, selectedGender);

			Long cultivatedFarmersCount = new Long(0);
			if (!StringUtil.isEmpty(selectedBranch)) {
				cultivatedFarmersCount = farmerService.findCultivatedFarmersCountByBranch(selectedBranch);
			} else {
				cultivatedFarmersCount = farmerService.findCultivatedFarmersCount();
			}

			if (totalCultivationLandProduction > 0 && !StringUtil.isEmpty(inputCost) && inputCost > 0.0) {
				Double costPerUnitLand = inputCost / Double.valueOf(totalCultivationLandProduction);
				combineValues.add(getText("costPerUnit") + "-" + costPerUnitLand);

				Double incomePerUnitLand = Double.valueOf(incomeFromCotton)
						/ Double.valueOf(totalCultivationLandProduction);
				combineValues.add(getLocaleProperty("incomePerUnit") + "-" + incomePerUnitLand);
			} else {
				combineValues.add(getText("costPerUnit") + "-" + "0");
				combineValues.add(getLocaleProperty("incomePerUnit") + "-" + "0");
			}

			if (cultivatedFarmersCount > 0 && !StringUtil.isEmpty(inputCost) && inputCost > 0.0) {
				Double costPerCaptia = inputCost / Double.valueOf(cultivatedFarmersCount);
				combineValues.add(getLocaleProperty("costPerCapita") + "-" + costPerCaptia);

				Double avgIncomeFromCotton = Double.valueOf(incomeFromCotton) / Double.valueOf(cultivatedFarmersCount);
				combineValues.add(getLocaleProperty("avgIncomeCotton") + "-" + avgIncomeFromCotton);
			} else {
				combineValues.add(getLocaleProperty("costPerCapita") + "-" + "0");
				combineValues.add(getLocaleProperty("avgIncomeCotton") + "-" + "0");
			}
		}

		combineValues.stream().forEach(obj -> {
			JSONObject jsonObject = new JSONObject();
			String value = (String) obj;
			String[] split = value.split("-");
			jsonObject.put("item", split[0]);
			jsonObject.put("values",
					StringUtil.isDouble(split[1]) ? Double.valueOf(formatter.format(Double.valueOf(split[1])))
							: Double.valueOf(String.valueOf(split[1])));
			jsonObjects.add(jsonObject);
		});

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("label", getLocaleProperty("farmercropDetailsChart"));

		JSONObject jsonobj2 = new JSONObject();
		jsonobj2.put("save", getLocaleProperty("save"));

		JSONObject jsonObject3 = new JSONObject();
		jsonObject3.put("refresh", getLocaleProperty("refresh"));

		jsonObjects.add(jsonObject);
		jsonObjects.add(jsonobj2);
		jsonObjects.add(jsonObject3);

		printAjaxResponse(jsonObjects, "text/html");
	}

	@SuppressWarnings("unchecked")
	public void populateFarmerCountChartData() {

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<Object> farmerData = farmerService.listFarmerCountByBranch();
		JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("Label", getLocaleProperty("donutChart.farmerBranchCount"));
		jsonObjects.add(jsonObject1);

		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put("save", getLocaleProperty("save"));
		jsonObjects.add(jsonObject2);
		JSONObject jsonObject3 = new JSONObject();
		jsonObject3.put("refresh", getLocaleProperty("refresh"));
		jsonObjects.add(jsonObject3);

		farmerData.stream().forEach(obj -> {
			JSONObject jsonObject = new JSONObject();
			Object[] objArr = (Object[]) obj;
			jsonObject.put("count", objArr[0]);
			jsonObject.put("group", objArr[1] + "(" + objArr[0] + ")");
			jsonObjects.add(jsonObject);
		});

		printAjaxResponse(jsonObjects, "text/html");
	}

	public void populateSeedChartData() {

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<Object> seedData = new ArrayList<>();
		/*
		 * if (StringUtil.isEmpty(selectedSeedType)) { seedData =
		 * farmerService.listSeedTypeCount(); } else { seedData =
		 * farmerService.listSeedTypeCountByBranch(selectedSeedType,selectedCrop
		 * ,selectedCooperative,selectedGender); }
		 */
		seedData = farmerService.listSeedTypeCountByBranch(selectedSeedType, selectedCrop, selectedCooperative,
				selectedGender);
		JSONObject jsonObject1 = new JSONObject();
		// jsonObject1.put("Label", getLocaleProperty("donutChart.seedType"));
		jsonObjects.add(jsonObject1);

		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put("save", getLocaleProperty("save"));
		jsonObjects.add(jsonObject2);
		JSONObject jsonObject3 = new JSONObject();
		jsonObject3.put("refresh", getLocaleProperty("refresh"));
		jsonObjects.add(jsonObject3);

		seedData.stream().forEach(obj -> {
			JSONObject jsonObject = new JSONObject();
			Object[] objArr = (Object[]) obj;
			jsonObject.put("count", objArr[0]);
			// jsonObject.put("group", getText("type" + objArr[1].toString()));
			FarmCatalogue type = getCatlogueValueByCode(objArr[1].toString());

			if (!ObjectUtil.isEmpty(type)) {
				if (!getCurrentTenantId().equals(ESESystem.CHETNA_TENANT_ID)) {
					jsonObject.put("group", type.getName() + "(" + objArr[0] + ")");
				} else {
					jsonObject.put("group", type.getName());
				}
				jsonObjects.add(jsonObject);
			}
		});

		printAjaxResponse(jsonObjects, "text/html");
	}

	public void populateSeedSourceChartData() {

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<Object> seedData = new ArrayList<>();
		/*
		 * if (StringUtil.isEmpty(selectedSeedSource)) { seedData =
		 * farmerService.listSeedSourceCount(); } else { seedData =
		 * farmerService.listSeedSourceCountBySource(selectedSeedSource); }
		 */

		seedData = farmerService.listSeedSourceCountBySource(getBranchId(), selectedCrop, selectedCooperative,
				selectedGender, selectedVariety, selectedState);
		JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("Label", getLocaleProperty("donutChart.seedSource"));
		jsonObjects.add(jsonObject1);

		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put("save", getLocaleProperty("save"));
		jsonObjects.add(jsonObject2);
		JSONObject jsonObject3 = new JSONObject();
		jsonObject3.put("refresh", getLocaleProperty("refresh"));
		jsonObjects.add(jsonObject3);

		seedData.stream().forEach(obj -> {
			JSONObject jsonObject = new JSONObject();
			Object[] objArr = (Object[]) obj;
			jsonObject.put("count", objArr[0]);
			if (!StringUtil.isEmpty(getListSeedSource().get(objArr[1]))) {
				if (!getCurrentTenantId().equals("chetna")) {
					jsonObject.put("group", getListSeedSource().get(objArr[1]) + "(" + objArr[0] + ")");
				} else {
					jsonObject.put("group", getListSeedSource().get(objArr[1]));
				}
			}
			jsonObjects.add(jsonObject);
		});

		printAjaxResponse(jsonObjects, "text/html");
	}

	public void populateTotalAcreByVillageChartData() {

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<Object> farmerData = null;

		farmerData = farmerService.listTotalFarmAcreByVillage();
		JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("Label", getLocaleProperty("donutChart.totalLandAcreByVillage"));
		jsonObjects.add(jsonObject1);

		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put("save", getLocaleProperty("save"));
		jsonObjects.add(jsonObject2);
		JSONObject jsonObject3 = new JSONObject();
		jsonObject3.put("refresh", getLocaleProperty("refresh"));
		jsonObjects.add(jsonObject3);

		farmerData.stream().forEach(obj -> {
			JSONObject jsonObject = new JSONObject();
			Object[] objArr = (Object[]) obj;
			jsonObject.put("count", objArr[0]);
			jsonObject.put("proposed", objArr[1]);
			jsonObject.put("group", objArr[2]);
			jsonObjects.add(jsonObject);
		});

		printAjaxResponse(jsonObjects, "text/html");
	}

	@SuppressWarnings("unchecked")
	public void populateTotalAcreChartData() {

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<Object> farmerData = null;

		/*
		 * farmerData = farmerService.listTotalFarmAcreByBranch(); JSONObject
		 * jsonObject1 = new JSONObject(); // if jsonObject1.put("Label",
		 * getLocaleProperty("donutChart.totalLandAcre")); //
		 * jsonObjects.add(jsonObject1);
		 * 
		 * 
		 * JSONObject jsonObjectLabelProd = new JSONObject();
		 * jsonObjectLabelProd.put("Label",
		 * getLocaleProperty("donutChart.totalLandAcre"));
		 * jsonObjects.add(jsonObjectLabelProd);
		 */
		/*
		 * if (!getCurrentTenantId().equalsIgnoreCase("kpf") &&
		 * !getCurrentTenantId().equalsIgnoreCase(ESESystem.CABI_TENANT_ID))
		 */
		if (!getIsKpfBased().equals("1")) {
			farmerData = farmerService.listTotalFarmAcreByBranch();
			farmerData.stream().forEach(obj -> {
				JSONObject jsonObject = new JSONObject();
				Object[] objArr = (Object[]) obj;
				jsonObject.put("count", objArr[0]);
				jsonObject.put("proposed", objArr[1]);
				jsonObject.put("group", objArr[2]);
				jsonObjects.add(jsonObject);
			});
			JSONObject jsonObject1 = new JSONObject();
			if (!getCurrentTenantId().equalsIgnoreCase("fincocoa")) {
				jsonObject1.put("Label", getLocaleProperty("donutChart.totalLandAcre"));
			} else {
				jsonObject1.put("Label", getLocaleProperty("donutChart.totalLandHectare"));
			}
			jsonObjects.add(jsonObject1);

			JSONObject jsonObjectLabelProd = new JSONObject();
			// jsonObjectLabelProd.put("Label",
			// getLocaleProperty("donutChart.totalLandAcre"));
			if (!getCurrentTenantId().equalsIgnoreCase("fincocoa")) {
				jsonObjectLabelProd.put("Label", getLocaleProperty("donutChart.totalLandAcre"));
			} else {
				jsonObjectLabelProd.put("Label", getLocaleProperty("donutChart.totalLandHectare"));
			}
			jsonObjects.add(jsonObjectLabelProd);

			JSONObject jsonObjectProdLabel = new JSONObject();
			jsonObjectProdLabel.put("LabelProduction", getLocaleProperty("donutChart.totalLandProduction"));
			jsonObjects.add(jsonObjectProdLabel);

			JSONObject jsonObject2 = new JSONObject();
			jsonObject2.put("save", getLocaleProperty("save"));
			jsonObjects.add(jsonObject2);
			JSONObject jsonObject3 = new JSONObject();
			jsonObject3.put("refresh", getLocaleProperty("refresh"));
			jsonObjects.add(jsonObject3);

			printAjaxResponse(jsonObjects, "text/html");
		} else {
			farmerData = farmerService.listTotalFarmAcreByFpo();

			farmerData.stream().forEach(obj -> {
				JSONObject jsonObject = new JSONObject();
				Object[] objArr = (Object[]) obj;
				jsonObject.put("landholding", objArr[0]);
				jsonObject.put("proposed", objArr[1]);
				jsonObject.put("group", objArr[2]);
				// jsonObject.put("group", objArr[2]+"("+objArr[0]+")");
				jsonObjects.add(jsonObject);
			});
			JSONObject jsonObject1 = new JSONObject();
			if (!getCurrentTenantId().equalsIgnoreCase("fincocoa")) {
				jsonObject1.put("Label", getLocaleProperty("donutChart.totalLandAcre"));
			} else {
				jsonObject1.put("Label", getLocaleProperty("donutChart.totalLandHectare"));
			}
			jsonObjects.add(jsonObject1);

			JSONObject jsonObjectLabelProd = new JSONObject();
			// jsonObjectLabelProd.put("Label",
			// getLocaleProperty("donutChart.totalLandAcre"));
			if (!getCurrentTenantId().equalsIgnoreCase("fincocoa")) {
				jsonObjectLabelProd.put("Label", getLocaleProperty("donutChart.totalLandAcre"));
			} else {
				jsonObjectLabelProd.put("Label", getLocaleProperty("donutChart.totalLandHectare"));
			}
			jsonObjects.add(jsonObjectLabelProd);

			JSONObject jsonObjectProdLabel = new JSONObject();
			jsonObjectProdLabel.put("LabelProduction", getLocaleProperty("donutChart.totalLandProduction"));
			jsonObjects.add(jsonObjectProdLabel);

			JSONObject jsonObject2 = new JSONObject();
			jsonObject2.put("save", getLocaleProperty("save"));
			jsonObjects.add(jsonObject2);
			JSONObject jsonObject3 = new JSONObject();
			jsonObject3.put("refresh", getLocaleProperty("refresh"));
			jsonObjects.add(jsonObject3);

			printAjaxResponse(jsonObjects, "text/html");
		}

	}

	@SuppressWarnings("unchecked")
	public void populateDeviceChartData() {

		List<Device> deviceList = deviceService.listDevices();
		Long onlineDeviceCount = deviceList.stream().filter(device -> !ObjectUtil.isEmpty(device.getAgent())
				&& device.isEnabled() && device.getAgent().getBodStatus() == 1).count();
		Long offlineDeviceCount = deviceList.stream().filter(device -> !ObjectUtil.isEmpty(device.getAgent())
				&& device.isEnabled() && device.getAgent().getBodStatus() == 0).count();
		Long inActiveDeviceCount = deviceList.stream()
				.filter(device -> ObjectUtil.isEmpty(device.getAgent()) && device.isEnabled()).count();
		Long disabledDeviceCount = deviceList.stream().filter(device -> !device.isEnabled()).count();

		JSONObject jsonObject = new JSONObject();

		jsonObject.put("onlineDeviceCount", (onlineDeviceCount > 0L) ? onlineDeviceCount : "-");
		jsonObject.put("offlineDeviceCount", (offlineDeviceCount > 0L) ? offlineDeviceCount : "-");
		jsonObject.put("inActiveDeviceCount", (inActiveDeviceCount > 0L) ? inActiveDeviceCount : "-");
		jsonObject.put("disabledDeviceCount", (disabledDeviceCount > 0L) ? disabledDeviceCount : "-");
		jsonObject.put("Label", getLocaleProperty("txt.devices"));
		jsonObject.put("inactiveLabel", getLocaleProperty("inactive") + "(" + inActiveDeviceCount + ")");
		jsonObject.put("activeLabel", getLocaleProperty("active") + "(" + inActiveDeviceCount + ")");
		jsonObject.put("disabledLabel", getLocaleProperty("disabled") + "(" + disabledDeviceCount + ")");
		jsonObject.put("onlineLabel", getLocaleProperty("online") + "(" + onlineDeviceCount + ")");
		jsonObject.put("offlineLabel", getLocaleProperty("offline") + "(" + offlineDeviceCount + ")");

		printAjaxResponse(jsonObject, "text/html");
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
	public void populateNswitchTxnChartData() {
		JSONObject jsonObject = new JSONObject();
		List<List<JSONObject>> jsonObjects = new ArrayList<>();
		List<JSONObject> labelDataJsonObjects = new ArrayList<>();
		if (!StringUtil.isEmpty(getDateRange())) {
			String[] dateSplit = getDateRange().split("-");
			Date sDate = DateUtil.convertStringToDate(dateSplit[0] + " 00:00:00", DateUtil.DATE_TIME_FORMAT);
			Date eDate = DateUtil.convertStringToDate(dateSplit[1] + " 23:59:59", DateUtil.DATE_TIME_FORMAT);

			List<Object> enrollmentData = productService.listEnrollmentByMoth(sDate, eDate);

			List<JSONObject> enrollmentDataJsonObjects = new ArrayList<>();

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

			jsonObject.put("Label", getLocaleProperty("groupchart.title"));
			jsonObject.put("enrollmentLabel", getLocaleProperty("enrollment"));
			labelDataJsonObjects.add(jsonObject);

			jsonObjects.add(enrollmentDataJsonObjects);
			jsonObjects.add(labelDataJsonObjects);
		}
		printAjaxResponse(jsonObjects, "text/html");
	}

	@SuppressWarnings("unchecked")
	public void populateCowMilkChartData() {

		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.BLRI_TENANT_ID)) {

			List<Object> milkingCount = farmerService.findMilkingCountByCow(CowInspection.IS_MILKING);
			List<Object> nonMilkingCount = farmerService.findMilkingCountByCow(CowInspection.NON_MILKING);

			Double toatlMilkingCount = 0d;
			toatlMilkingCount = (double) milkingCount.size();
			Double toatlNonMilkingCount = 0d;
			toatlNonMilkingCount = (double) nonMilkingCount.size();

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("totalMilkCount", (toatlMilkingCount > 0L) ? toatlMilkingCount : "-");
			jsonObject.put("totalNonMilkCount", (toatlNonMilkingCount > 0L) ? toatlNonMilkingCount : "-");
			jsonObject.put("Label", getLocaleProperty("cowMilk"));
			jsonObject.put("isMilkingCow", getLocaleProperty("isMilkingCow") + "(" + toatlMilkingCount + ")");
			jsonObject.put("isNonMilkingCow", getLocaleProperty("isNonMilkingCow") + "(" + toatlNonMilkingCount + ")");

			printAjaxResponse(jsonObject, "text/html");

		}

	}

	public String getBranchIdValue() {

		return branchIdValue;
	}

	public void setBranchIdValue(String branchIdValue) {

		this.branchIdValue = branchIdValue;
	}

	public String getSelectedTaluk() {

		return selectedTaluk;
	}

	public void setSelectedTaluk(String selectedTaluk) {

		this.selectedTaluk = selectedTaluk;
	}

	public String getSelectedVillage() {

		return selectedVillage;
	}

	public void setSelectedVillage(String selectedVillage) {

		this.selectedVillage = selectedVillage;
	}

	public Integer getUserCount() {

		return userCount;
	}

	public void setUserCount(Integer userCount) {

		this.userCount = userCount;
	}

	public Map<Long, String> getWarehouseList() {

		Map<Long, String> warehouseLists = new LinkedHashMap<>();
		List<Warehouse> warehouseList = locationService.listWarehouse();
		warehouseList.stream().collect(Collectors.toMap(Warehouse::getId, Warehouse::getName)).entrySet().stream()
				.sorted(Map.Entry.comparingByValue(new Comparator<String>() {
					@Override
					public int compare(String o1, String o2) {

						return o1.compareToIgnoreCase(o2);
					}
				})).forEachOrdered(e -> warehouseLists.put(e.getKey(), e.getValue()));

		return warehouseLists;
	}

	public Map<String, String> getBranchList() {

		List<Object[]> branchMasters = getBranchesInfo();
		Map<String, String> branchList = new HashMap<String, String>();

		branchList = branchMasters.stream()
				.collect(Collectors.toMap(obj -> String.valueOf(obj[0]), obj -> String.valueOf(obj[1])));
		return branchList;
	}

	public List<Integer> getYearList() {
		List<Integer> yearList = new LinkedList<>();
		for (int i = 2016; i < DateUtil.getCurrentYear(); i++) {
			yearList.add(i);
		}
		return yearList;
	}

	public String getSelectedWarehouse() {

		return selectedWarehouse;
	}

	public void setSelectedWarehouse(String selectedWarehouse) {

		this.selectedWarehouse = selectedWarehouse;
	}

	public String getSelectedSeedType() {

		return selectedSeedType;
	}

	public void setSelectedSeedType(String selectedSeedType) {

		this.selectedSeedType = selectedSeedType;
	}

	public String getSelectedSeedSource() {

		return selectedSeedSource;
	}

	public void setSelectedSeedSource(String selectedSeedSource) {

		this.selectedSeedSource = selectedSeedSource;
	}

	public Map<String, String> getListSeedSource() {

		Map<String, String> warehouseMap = new LinkedHashMap<>();

		warehouseMap = getFarmCatalougeMap(Integer.valueOf(getText("seedSourceType")));

		return warehouseMap;
	}

	public void populateVillageByCity() throws Exception {

		List<Village> villages = new ArrayList<Village>();

		if (!selectedTaluk.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedTaluk))
				&& !selectedTaluk.equalsIgnoreCase("0")) {
			villages = locationService.listVillagesByCityId(Long.valueOf(selectedTaluk));
		}
		JSONArray villageArr = new JSONArray();
		if (!ObjectUtil.isEmpty(villages)) {
			for (Village village : villages) {
				villageArr.add(getJSONObject(village.getId(), village.getName()));
			}
		}
		sendAjaxResponse(villageArr);

	}

	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}

	public String getSelectedBranch() {

		if (!StringUtil.isEmpty(getBranchId())) {
			selectedBranch = getBranchId();
		}

		return selectedBranch;
	}

	public void setSelectedBranch(String selectedBranch) {

		this.selectedBranch = selectedBranch;
	}

	public String getNoData() {

		return noData;
	}

	public void setNoData(String noData) {

		this.noData = getText("noData");
	}

	public String getPreferenceValue(String key) {

		String value = preferncesService.findPrefernceByName(key);
		return value;
	}

	public String getSelectedYear() {
		return selectedYear;
	}

	public void setSelectedYear(String selectedYear) {
		this.selectedYear = selectedYear;
	}

	public Map<String, String> getTimelineYear() {

		Map<String, String> yearList = new HashMap<String, String>();
		for (int i = DateUtil.getCurrentYear() + 1; i >= 2010; i--) {
			yearList.put(String.valueOf(i), String.valueOf(i - 1) + " - " + String.valueOf(i));
		}

		return yearList;
	}

	public Map<String, String> getListOfState() {

		Map<String, String> stateList = new HashMap<String, String>();

		return stateList;
	}

	public void populateFarmerEconomyExpenses() {

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<Cultivation> cultivations = farmerService.listCultivationExpenses();

		JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("Label", getLocaleProperty("farmer.cultivationExpense"));
		jsonObjects.add(jsonObject1);
		Double expenses = 0.0D;
		Double revenues = 0.0D;
		Double totalIncome = 0.0D;
		String cultivationBranchId = "";
		Calendar cal = Calendar.getInstance();
		Set<String> farmCodes = new HashSet<String>();
		Farm tmpFarm;
		Object cottonIncome;

		for (Cultivation cultivation : cultivations) {
			JSONObject jsonObject = new JSONObject();

			totalIncome = 0.00D;
			totalIncome += !StringUtil.isEmpty(cultivation.getAgriIncome())
					? Double.valueOf(cultivation.getAgriIncome()) : 0;
			totalIncome += !StringUtil.isEmpty(cultivation.getInterCropIncome())
					? Double.valueOf(cultivation.getInterCropIncome()) : 0;
			totalIncome += !StringUtil.isEmpty(cultivation.getOtherSourcesIncome())
					? Double.valueOf(cultivation.getOtherSourcesIncome()) : 0;

			if (!StringUtil.isEmpty(selectedYear) && !ObjectUtil.isEmpty(cultivation)) {
				cal.setTime((!ObjectUtil.isEmpty(cultivation.getExpenseDate()) ? cultivation.getExpenseDate()
						: new Date()));
				cultivationBranchId = (!StringUtil.isEmpty(cultivation.getBranchId()) ? cultivation.getBranchId() : "");
				if ((cal.get(Calendar.YEAR) == Integer.parseInt(getSelectedYear())
						|| cal.get(Calendar.YEAR) == (Integer.parseInt(getSelectedYear()) - 1))
						&& cultivationBranchId.equals(!StringUtil.isEmpty(selectedBranch) ? selectedBranch : "")) {
					if (cultivation.getTxnType().equals("1")) {
						farmCodes.add(!StringUtil.isEmpty(cultivation.getFarmId()) ? cultivation.getFarmId() : "");
						revenues += (!ObjectUtil.isEmpty(totalIncome) ? totalIncome : 0.0);
					} else if (cultivation.getTxnType().equals("2")) {
						expenses += (!StringUtil.isEmpty(cultivation.getTotalCoc())
								? Double.parseDouble(cultivation.getTotalCoc()) : 0.0);
					}
				} else if (StringUtil.isEmpty(selectedBranch)
						&& ((cal.get(Calendar.YEAR) == Integer.parseInt(getSelectedYear()))
								|| cal.get(Calendar.YEAR) == (Integer.parseInt(getSelectedYear()) - 1))) {
					if (cultivation.getTxnType().equals("1")) {
						farmCodes.add(!StringUtil.isEmpty(cultivation.getFarmId()) ? cultivation.getFarmId() : "");
						revenues += (!ObjectUtil.isEmpty(totalIncome) ? totalIncome : 0.0);
					} else if (cultivation.getTxnType().equals("2")) {
						expenses += (!StringUtil.isEmpty(cultivation.getTotalCoc())
								? Double.parseDouble(cultivation.getTotalCoc()) : 0.0);
					}
				}
			}
		}

		for (String farmCode : farmCodes) {
			if (!StringUtil.isEmpty(farmCode)) {
				tmpFarm = farmerService.findFarmByCode(farmCode);
				if (!ObjectUtil.isEmpty(tmpFarm)) {
					cottonIncome = farmerService.findCottonIncomeByFarmerCode(tmpFarm.getFarmer().getFarmerId(),
							tmpFarm.getFarmCode());
					revenues += !ObjectUtil.isEmpty(cottonIncome)
							? Double.valueOf(!ObjectUtil.isEmpty(cottonIncome) ? cottonIncome.toString() : "0.00") : 0;
				}
			}
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("item", getLocaleProperty("expense"));
		jsonObject.put("values", String.valueOf(expenses));
		jsonObjects.add(jsonObject);

		jsonObject = new JSONObject();
		jsonObject.put("item", getLocaleProperty("revenue"));
		jsonObject.put("values", String.valueOf(revenues));
		jsonObjects.add(jsonObject);
		printAjaxResponse(jsonObjects, "text/html");

	}

	public void populateFarmerDataStatistics() {

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		Integer totalFarmerCount;
		Integer totalFarmCount;

		JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("Label", getLocaleProperty("farmer.dataStatistics"));
		jsonObjects.add(jsonObject1);
		Double farmersWithPhotos = 0.0D;
		Double farmsWithPhotos = 0.0D;
		Double farmsWithGpsTagged = 0.0D;
		String cultivationBranchId = "";
		Calendar cal = Calendar.getInstance();
		List<Object[]> tmpValues;
		Date tmpDate;

		totalFarmerCount = farmerService.findTotalFarmerCount(selectedBranch, selectedStapleLen, selectedSeason,
				selectedGender, selectedState);
		totalFarmCount = farmerService.findTotalFarmCount(selectedBranch,
				!StringUtil.isEmpty(selectedYear) ? Integer.parseInt(selectedYear) : 0);

		if (StringUtil.isEmpty(selectedYear)) {
			selectedYear = String.valueOf(cal.get(Calendar.YEAR));
		}
		if (ObjectUtil.isEmpty(selectedBranch)) {
			selectedBranch = "";
		}

		tmpValues = farmerService.findFarmerCountWithPhotos(selectedBranch);
		if (!ObjectUtil.isListEmpty(tmpValues)) {
			for (Object dtObj : tmpValues) {
				if (!ObjectUtil.isEmpty(dtObj)) {
					tmpDate = (Date) dtObj;
					cal.setTime(tmpDate);
					if ((cal.get(Calendar.YEAR) == Integer.parseInt(selectedYear))
							|| (cal.get(Calendar.YEAR) == (Integer.parseInt(selectedYear) - 1))) {
						farmersWithPhotos += 1;
					}
				}
			}
		}

		tmpValues = farmerService.findFarmCountWithPhotos(selectedBranch);
		if (!ObjectUtil.isListEmpty(tmpValues)) {
			for (Object dtObj : tmpValues) {
				if (!ObjectUtil.isEmpty(dtObj)) {
					tmpDate = (Date) dtObj;
					cal.setTime(tmpDate);
					if ((cal.get(Calendar.YEAR) == Integer.parseInt(selectedYear))
							|| (cal.get(Calendar.YEAR) == (Integer.parseInt(selectedYear) - 1))) {
						farmsWithPhotos += 1;
					}
				}
			}
		}

		tmpValues = farmerService.findFarmCountWithGPSTag(selectedBranch);
		if (!ObjectUtil.isListEmpty(tmpValues)) {
			for (Object dtObj : tmpValues) {
				if (!ObjectUtil.isEmpty(dtObj)) {
					tmpDate = (Date) dtObj;
					cal.setTime(tmpDate);
					if ((cal.get(Calendar.YEAR) == Integer.parseInt(selectedYear))
							|| (cal.get(Calendar.YEAR) == (Integer.parseInt(selectedYear) - 1))) {
						farmsWithGpsTagged += 1;
					}
				}
			}
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("item", getLocaleProperty("farmersWithPhotos"));
		jsonObject.put("values", Double.valueOf(String.valueOf(farmersWithPhotos)));
		jsonObjects.add(jsonObject);

		jsonObject = new JSONObject();
		jsonObject.put("item", getLocaleProperty("farmsWithPhotos"));
		jsonObject.put("values", Double.valueOf(String.valueOf(farmsWithPhotos)));
		jsonObjects.add(jsonObject);

		jsonObject = new JSONObject();
		jsonObject.put("item", getLocaleProperty("farmsWithGPSTag"));
		jsonObject.put("values", Double.valueOf(String.valueOf(farmsWithGpsTagged)));
		jsonObjects.add(jsonObject);

		/*
		 * jsonObject = new JSONObject(); jsonObject.put("item",
		 * getLocaleProperty("totalFarmersCount")); jsonObject.put("values",
		 * String.valueOf(totalFarmerCount)); jsonObjects.add(jsonObject);
		 * 
		 * jsonObject = new JSONObject(); jsonObject.put("item",
		 * getLocaleProperty("totalFarmCount")); jsonObject.put("values",
		 * String.valueOf(totalFarmCount)); jsonObjects.add(jsonObject);
		 */

		printAjaxResponse(jsonObjects, "text/html");
	}

	@SuppressWarnings("unchecked")
	public void populateFarmerICSDetailsBarChartData() {
		List<JSONObject> farmerIcsDataJsonObjects = new ArrayList<>();
		List<JSONObject> farmerIcsGroupJsonObjects = new ArrayList<>();

		List<List<JSONObject>> jsonObjects = new ArrayList<>();
		List<String> branchList = new ArrayList<>();

		if (!StringUtil.isEmpty(getBranchId())) {

			if (StringUtil.isEmpty(selectedBranch)) {
				selectedBranch = getBranchId();
			}

			Integer totalFarmerCount = farmerService.findTotalFarmerCount(selectedBranch, selectedStapleLen,
					selectedSeason, selectedGender, selectedState);

			List<Object> combineValues = farmerService.findFarmerCountByICconversion(selectedBranch,
					!StringUtil.isEmpty(selectedYear) ? Integer.parseInt(selectedYear) : 0, selectedSeason,
					selectedGender, selectedState);
			JSONObject farmerIcsDataJson = new JSONObject();
			farmerIcsDataJson.put("item", getLocaleProperty("donutChart.farmer"));
			farmerIcsDataJson.put("values", totalFarmerCount);
			farmerIcsDataJsonObjects.add(count, farmerIcsDataJson);
			/*
			 * List<Object> enrollmentData = productService
			 * .listEnrollmentByMoth(DateUtil.getFirstDateOfMonth(DateUtil.
			 * getCurrentYear(), 0), new Date());
			 */

			combineValues.stream().forEach(obj -> {
				JSONObject farmerIcsDataJson1 = new JSONObject();
				Object[] objArr = (Object[]) obj;

				if (count == 0) {
					farmerIcsDataJson1.put("item", getText("icsStatus0"));
				} else {
					farmerIcsDataJson1.put("item", getText("icsStatus" + objArr[0]));
					icsTyp = Integer.valueOf(String.valueOf(objArr[0]));
				}
				farmerIcsDataJson1.put("values", objArr[1]);

				List<Object> icsFarmerGroupList = farmerService.findICSFarmerCountByGroup(selectedBranch,
						!StringUtil.isEmpty(selectedYear) ? Integer.parseInt(selectedYear) : 0, selectedSeason,
						Integer.valueOf(String.valueOf(objArr[0])));

				icsFarmerGroupList.stream().forEach(obj1 -> {
					JSONObject farmerIcsGroupDataJson = new JSONObject();
					Object[] objArr1 = (Object[]) obj1;
					farmerIcsGroupDataJson.put("item", getText("icsStatus" + icsTyp));

					farmerIcsGroupDataJson.put("icsFarmerGroup", objArr1[2]);
					farmerIcsGroupDataJson.put("farmerCount", objArr1[1]);
					farmerIcsGroupJsonObjects.add(keyCount, farmerIcsGroupDataJson);
					keyCount++;

				});

				/*
				 * if (!StringUtil.isListEmpty(branchList)) { String branch =
				 * branchList.get(count); int totalBranchFarmerCount =
				 * farmerService.findTotalFarmerCount(branch,
				 * !StringUtil.isEmpty(selectedYear) ?
				 * Integer.parseInt(selectedYear) : 0, selectedSeason);
				 * farmerIcsDataJson1.put("branch", branch);
				 * farmerIcsDataJson1.put("farmerCount",
				 * totalBranchFarmerCount);
				 * 
				 * }
				 */
				farmerIcsDataJsonObjects.add(++count, farmerIcsDataJson1);
			});

			jsonObjects.add(farmerIcsDataJsonObjects);
			jsonObjects.add(farmerIcsGroupJsonObjects);
		} else {

			int keyIndex = 0;
			List<JSONObject> farmerBranchJsonObjects = new ArrayList<>();
			branchList = clientService.findBranchList();

			for (String branch : branchList) {

				Integer totalBranchFarmerCount = farmerService.findTotalFarmerCount(branch, selectedStapleLen,
						selectedSeason, selectedGender, selectedState);
				List<Object> combineValues = farmerService.findFarmerCountByICconversion(branch,
						!StringUtil.isEmpty(selectedYear) ? Integer.parseInt(selectedYear) : 0, selectedSeason,
						selectedGender, selectedState);

				JSONObject farmerIcsDataJson = new JSONObject();
				farmerIcsDataJson.put("item", getLocaleProperty("donutChart.farmer"));
				farmerIcsDataJson.put("branch", branch);
				farmerIcsDataJson.put("values", totalBranchFarmerCount);
				farmerIcsDataJsonObjects.add(count, farmerIcsDataJson);

				JSONObject farmerBranchDataJson = new JSONObject();
				farmerBranchDataJson.put("branch", branch);
				farmerBranchDataJson.put("branchFarmer", totalBranchFarmerCount);
				farmerBranchJsonObjects.add(keyIndex, farmerBranchDataJson);

				combineValues.stream().forEach(obj -> {
					JSONObject farmerIcsDataJson1 = new JSONObject();
					Object[] objArr = (Object[]) obj;

					/*
					 * if (count == 0) { farmerIcsDataJson1.put("item",
					 * getText("icsStatus0")); } else {
					 * farmerIcsDataJson1.put("item", getText("icsStatus" +
					 * objArr[0])); icsTyp =
					 * Integer.valueOf(String.valueOf(objArr[0])); }
					 */
					if (combineValues.size() > 1) {
						icsTyp = Integer.valueOf(String.valueOf(objArr[0]));
						farmerIcsDataJson1.put("item", getText("icsStatus" + objArr[0]));
					} else {
						icsTyp = 0;
						farmerIcsDataJson1.put("item", getText("icsStatus0"));
					}

					farmerIcsDataJson1.put("values", objArr[1]);
					farmerIcsDataJson1.put("branch", branch);
					List<Object> icsFarmerGroupList = farmerService.findICSFarmerCountByGroup(branch,
							!StringUtil.isEmpty(selectedYear) ? Integer.parseInt(selectedYear) : 0, selectedSeason,
							Integer.valueOf(String.valueOf(objArr[0])));

					icsFarmerGroupList.stream().forEach(obj1 -> {
						JSONObject farmerIcsGroupDataJson = new JSONObject();
						Object[] objArr1 = (Object[]) obj1;
						farmerIcsGroupDataJson.put("item", getText("icsStatus" + icsTyp));
						farmerIcsGroupDataJson.put("branch", branch);
						farmerIcsGroupDataJson.put("icsFarmerGroup", objArr1[2]);
						farmerIcsGroupDataJson.put("farmerCount", objArr1[1]);
						farmerIcsGroupJsonObjects.add(keyCount, farmerIcsGroupDataJson);
						keyCount++;

					});

					/*
					 * if (!StringUtil.isListEmpty(branchList)) { String branch
					 * = branchList.get(count); int totalBranchFarmerCount =
					 * farmerService.findTotalFarmerCount(branch,
					 * !StringUtil.isEmpty(selectedYear) ?
					 * Integer.parseInt(selectedYear) : 0, selectedSeason);
					 * farmerIcsDataJson1.put("branch", branch);
					 * farmerIcsDataJson1.put("farmerCount",
					 * totalBranchFarmerCount);
					 * 
					 * }
					 */
					farmerIcsDataJsonObjects.add(++count, farmerIcsDataJson1);
				});
				keyIndex++;
			}
			jsonObjects.add(farmerIcsDataJsonObjects);
			jsonObjects.add(farmerIcsGroupJsonObjects);
			jsonObjects.add(farmerBranchJsonObjects);
		}

		printAjaxResponse(jsonObjects, "text/html");

	}

	public void populateCropHarvestAndSaleChart() {

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		// List<CropHarvest> harvest = new ArrayList<CropHarvest>();
		// List<CropSupply> sale = new ArrayList<CropSupply>();
		Double harvestQty = 0.0D;
		Double saleQty = 0.0D;
		/*
		 * if (StringUtil.isEmpty(selectedBranch)) { selectedBranch =
		 * getBranchId(); }
		 */
		String harveQty = farmerService.findCropHarvestByYearAndBranch(selectedBranch, Integer.parseInt(selectedYear));
		saleQty = farmerService.findCropSupplyByYearAndBranch(selectedBranch, Integer.parseInt(selectedYear));

		harvestQty = Double.valueOf(StringUtil.isEmpty(harveQty) ? "0.0" : harveQty);

		/*
		 * if (!ObjectUtil.isEmpty(harvest) && harvest.size() > 0) { for
		 * (CropHarvest crophar : harvest) { harvestQty +=
		 * Double.valueOf(crophar.getTotalQty()); } } if
		 * (!ObjectUtil.isEmpty(sale) && sale.size() > 0) { for (CropSupply
		 * supply : sale) { List<CropSupplyDetails> cropSupply =
		 * farmerService.findCropSupplyDetailId(supply.getId()); for
		 * (CropSupplyDetails supplyDet : cropSupply) { saleQty +=
		 * supplyDet.getQty(); } } }
		 */

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("label", getLocaleProperty("farmerCropharvestSale"));
		jsonObjects.add(jsonObject);

		jsonObject = new JSONObject();
		jsonObject.put("values", String.valueOf(harvestQty));
		jsonObjects.add(jsonObject);

		jsonObject = new JSONObject();
		jsonObject.put("values", String.valueOf(saleQty));
		jsonObjects.add(jsonObject);

		printAjaxResponse(jsonObjects, "text/html");

	}

	@SuppressWarnings("unchecked")
	public void populateCultivationDetails() {

		List<Cultivation> cultivations = farmerService.listCultivationExpenses();

		Double expenses = 0.0D;
		Double revenues = 0.0D;
		Double totalIncome = 0.0D;
		String cultivationBranchId = "";
		Calendar cal = Calendar.getInstance();
		Set<String> farmCodes = new HashSet<String>();
		Farm tmpFarm;
		Object cottonIncome;
		// Double landPloug = 0.0D;
		Double landTotl = 0.0D;
		/*
		 * Double ridgeFurrow = 0.0D; Double seedCost = 0.0D;
		 */
		Double totSow = 0.0D;
		Double totGap = 0.0D;
		Double totWeed = 0.0D;
		Double totCul = 0.0D;
		Double totIrr = 0.0D;
		Double totFer = 0.0D;
		Double totPes = 0.0D;
		Double tothar = 0.0D;
		/*
		 * Double pack = 0.0D; Double tran = 0.0D; Double miscellaneous = 0.0D;
		 */
		Double totExp = 0.0D;

		for (Cultivation cultivation : cultivations) {
			totalIncome = 0.00D;
			totalIncome += !StringUtil.isEmpty(cultivation.getAgriIncome())
					? Double.valueOf(cultivation.getAgriIncome()) : 0;
			totalIncome += !StringUtil.isEmpty(cultivation.getInterCropIncome())
					? Double.valueOf(cultivation.getInterCropIncome()) : 0;
			totalIncome += !StringUtil.isEmpty(cultivation.getOtherSourcesIncome())
					? Double.valueOf(cultivation.getOtherSourcesIncome()) : 0;

			if (!StringUtil.isEmpty(selectedYear) && !ObjectUtil.isEmpty(cultivation)) {
				cal.setTime((!ObjectUtil.isEmpty(cultivation.getExpenseDate()) ? cultivation.getExpenseDate()
						: new Date()));
				cultivationBranchId = (!StringUtil.isEmpty(cultivation.getBranchId()) ? cultivation.getBranchId() : "");
				if ((cal.get(Calendar.YEAR) == Integer.parseInt(getSelectedYear())
						|| cal.get(Calendar.YEAR) == (Integer.parseInt(getSelectedYear()) - 1))
						&& cultivationBranchId.equals(!StringUtil.isEmpty(selectedBranch) ? selectedBranch : "")) {
					if (cultivation.getTxnType().equals("1")) {
						farmCodes.add(!StringUtil.isEmpty(cultivation.getFarmId()) ? cultivation.getFarmId() : "");
						revenues += (!ObjectUtil.isEmpty(totalIncome) ? totalIncome : 0.0);
					} else if (cultivation.getTxnType().equals("2")) {
						expenses += (!StringUtil.isEmpty(cultivation.getTotalCoc())
								? Double.parseDouble(cultivation.getTotalCoc()) : 0.0);
						/*
						 * landPloug +=
						 * (!StringUtil.isEmpty(cultivation.getLandPloughing())
						 * ? Double.parseDouble(cultivation.getLandPloughing())
						 * : 0.0);
						 */
						landTotl += (!StringUtil.isEmpty(cultivation.getLandTotal())
								? Double.parseDouble(cultivation.getLandTotal()) : 0.0);
						/*
						 * ridgeFurrow +=
						 * (!StringUtil.isEmpty(cultivation.getRidgeFurrow()) ?
						 * Double.parseDouble(cultivation.getRidgeFurrow()) :
						 * 0.0);
						 */
						/*
						 * seedCost +=
						 * (!StringUtil.isEmpty(cultivation.getSeedCost()) ?
						 * Double.parseDouble(cultivation.getSeedCost()) : 0.0);
						 */
						totSow += (!StringUtil.isEmpty(cultivation.getTotalSowing())
								? Double.parseDouble(cultivation.getTotalSowing()) : 0.0);
						totGap += (!StringUtil.isEmpty(cultivation.getTotalGap())
								? Double.parseDouble(cultivation.getTotalGap()) : 0.0);
						totWeed += (!StringUtil.isEmpty(cultivation.getTotalWeed())
								? Double.parseDouble(cultivation.getTotalWeed()) : 0.0);
						totCul += (!StringUtil.isEmpty(cultivation.getTotalCulture())
								? Double.parseDouble(cultivation.getTotalCulture()) : 0.0);
						totIrr += (!StringUtil.isEmpty(cultivation.getTotalIrrigation())
								? Double.parseDouble(cultivation.getTotalIrrigation()) : 0.0);
						totFer += (!StringUtil.isEmpty(cultivation.getTotalFertilizer())
								? Double.parseDouble(cultivation.getTotalFertilizer()) : 0.0);
						totPes += (!StringUtil.isEmpty(cultivation.getTotalPesticide())
								? Double.parseDouble(cultivation.getTotalPesticide()) : 0.0);
						tothar += (!StringUtil.isEmpty(cultivation.getTotalHarvest())
								? Double.parseDouble(cultivation.getTotalHarvest()) : 0.0);
						/*
						 * pack +=
						 * (!StringUtil.isEmpty(cultivation.getPackingMaterial()
						 * ) ?
						 * Double.parseDouble(cultivation.getPackingMaterial())
						 * : 0.0); tran +=
						 * (!StringUtil.isEmpty(cultivation.getTransport()) ?
						 * Double.parseDouble(cultivation.getTransport()) :
						 * 0.0); miscellaneous +=
						 * (!StringUtil.isEmpty(cultivation.getMiscellaneous())
						 * ? Double.parseDouble(cultivation.getMiscellaneous())
						 * : 0.0);
						 */
						totExp += (!StringUtil.isEmpty(cultivation.getTotalExpense())
								? Double.parseDouble(cultivation.getTotalExpense()) : 0.0);
					}
				} else if (StringUtil.isEmpty(selectedBranch)
						&& ((cal.get(Calendar.YEAR) == Integer.parseInt(getSelectedYear()))
								|| cal.get(Calendar.YEAR) == (Integer.parseInt(getSelectedYear()) - 1))) {
					if (cultivation.getTxnType().equals("1")) {
						farmCodes.add(!StringUtil.isEmpty(cultivation.getFarmId()) ? cultivation.getFarmId() : "");
						revenues += (!ObjectUtil.isEmpty(totalIncome) ? totalIncome : 0.0);
					} else if (cultivation.getTxnType().equals("2")) {
						expenses += (!StringUtil.isEmpty(cultivation.getTotalCoc())
								? Double.parseDouble(cultivation.getTotalCoc()) : 0.0);
						/*
						 * landPloug +=
						 * (!StringUtil.isEmpty(cultivation.getLandPloughing())
						 * ? Double.parseDouble(cultivation.getLandPloughing())
						 * : 0.0);
						 */
						landTotl += (!StringUtil.isEmpty(cultivation.getLandTotal())
								? Double.parseDouble(cultivation.getLandTotal()) : 0.0);
						/*
						 * ridgeFurrow +=
						 * (!StringUtil.isEmpty(cultivation.getRidgeFurrow()) ?
						 * Double.parseDouble(cultivation.getRidgeFurrow()) :
						 * 0.0); seedCost +=
						 * (!StringUtil.isEmpty(cultivation.getSeedCost()) ?
						 * Double.parseDouble(cultivation.getSeedCost()) : 0.0);
						 */
						totSow += (!StringUtil.isEmpty(cultivation.getTotalSowing())
								? Double.parseDouble(cultivation.getTotalSowing()) : 0.0);
						totGap += (!StringUtil.isEmpty(cultivation.getTotalGap())
								? Double.parseDouble(cultivation.getTotalGap()) : 0.0);
						totWeed += (!StringUtil.isEmpty(cultivation.getTotalWeed())
								? Double.parseDouble(cultivation.getTotalWeed()) : 0.0);
						totCul += (!StringUtil.isEmpty(cultivation.getTotalCulture())
								? Double.parseDouble(cultivation.getTotalCulture()) : 0.0);
						totIrr += (!StringUtil.isEmpty(cultivation.getTotalIrrigation())
								? Double.parseDouble(cultivation.getTotalIrrigation()) : 0.0);
						totFer += (!StringUtil.isEmpty(cultivation.getTotalFertilizer())
								? Double.parseDouble(cultivation.getTotalFertilizer()) : 0.0);
						totPes += (!StringUtil.isEmpty(cultivation.getTotalPesticide())
								? Double.parseDouble(cultivation.getTotalPesticide()) : 0.0);
						tothar += (!StringUtil.isEmpty(cultivation.getTotalHarvest())
								? Double.parseDouble(cultivation.getTotalHarvest()) : 0.0);
						/*
						 * pack +=
						 * (!StringUtil.isEmpty(cultivation.getPackingMaterial()
						 * ) ?
						 * Double.parseDouble(cultivation.getPackingMaterial())
						 * : 0.0); tran +=
						 * (!StringUtil.isEmpty(cultivation.getTransport()) ?
						 * Double.parseDouble(cultivation.getTransport()) :
						 * 0.0); miscellaneous +=
						 * (!StringUtil.isEmpty(cultivation.getMiscellaneous())
						 * ? Double.parseDouble(cultivation.getMiscellaneous())
						 * : 0.0);
						 */
						totExp += (!StringUtil.isEmpty(cultivation.getTotalExpense())
								? Double.parseDouble(cultivation.getTotalExpense()) : 0.0);
					}
				}
			}
		}

		for (String farmCode : farmCodes) {
			if (!StringUtil.isEmpty(farmCode)) {
				tmpFarm = farmerService.findFarmByCode(farmCode);
				if (!ObjectUtil.isEmpty(tmpFarm)) {
					cottonIncome = farmerService.findCottonIncomeByFarmerCode(tmpFarm.getFarmer().getFarmerId(),
							tmpFarm.getFarmCode());
					revenues += !ObjectUtil.isEmpty(cottonIncome)
							? Double.valueOf(!ObjectUtil.isEmpty(cottonIncome) ? cottonIncome.toString() : "0.00") : 0;
				}
			}
		}
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("Label", getLocaleProperty("farmer.expenceDetails"));

		jsonObject.put("expenses", expenses > 0.0D ? String.valueOf(expenses) : "0.0");
		jsonObject.put("revenues", revenues > 0.0D ? String.valueOf(revenues) : "0.0");
		// jsonObject.put("landPloug", landPloug > 0.0D ?
		// String.valueOf(landPloug) : "0.0");
		jsonObject.put("landTotl", landTotl > 0.0D ? String.valueOf(landTotl) : "0.0");
		/*
		 * jsonObject.put("ridgeFurrow", (ridgeFurrow > 0.0D) ?
		 * String.valueOf(ridgeFurrow) : "0.0"); jsonObject.put("seedCost",
		 * (seedCost > 0.0D) ? String.valueOf(seedCost) : "0.0");
		 */
		jsonObject.put("totSow", (totSow > 0.0D) ? String.valueOf(totSow) : "0.0");
		jsonObject.put("totGap", (totGap > 0.0D) ? String.valueOf(totGap) : "0.0");
		jsonObject.put("totWeed", (totWeed > 0.0D) ? String.valueOf(totWeed) : "0.0");
		jsonObject.put("totCul", (totCul > 0.0D) ? String.valueOf(totCul) : "0.0");
		jsonObject.put("totIrr", (totIrr > 0.0D) ? String.valueOf(totIrr) : "0.0");
		jsonObject.put("totFer", (totFer > 0.0D) ? String.valueOf(totFer) : "0.0");
		jsonObject.put("totPes", (totPes > 0.0D) ? String.valueOf(totPes) : "0.0");
		jsonObject.put("tothar", (tothar > 0.0D) ? String.valueOf(tothar) : "0.0");

		jsonObject.put("expLabel", getLocaleProperty("farmer.expense"));
		jsonObject.put("revLabel", getLocaleProperty("farmer.revenue"));
		// jsonObject.put("plougLabel", getLocaleProperty("farmer.plough"));
		jsonObject.put("totalLabel", getLocaleProperty("farmer.land"));
		/*
		 * jsonObject.put("ridgeLabel", getLocaleProperty("farmer.ridge"));
		 * jsonObject.put("seedLabel", getLocaleProperty("farmer.seedCost"));
		 */
		jsonObject.put("sowLabel", getLocaleProperty("farmer.sowing"));
		jsonObject.put("gapLabel", getLocaleProperty("farmer.gap"));
		jsonObject.put("weedLabel", getLocaleProperty("farmer.weed"));
		jsonObject.put("culLabel", getLocaleProperty("farmer.cultivation"));
		jsonObject.put("irrLabel", getLocaleProperty("farmer.irrigation"));
		jsonObject.put("ferLabel", getLocaleProperty("farmer.fertilizer"));
		jsonObject.put("pesLabel", getLocaleProperty("farmer.pesticide"));
		jsonObject.put("harvestLabel", getLocaleProperty("farmer.harvest"));
		printAjaxResponse(jsonObject, "text/html");

	}

	public void populateCocCharts() {

		List<Cultivation> cultivations = farmerService.listCultivationExpenses();

		// Double expenses = 0.0D;
		// Double revenues = 0.0D;
		Double totalIncome = 0.0D;
		String cultivationBranchId = "";
		Calendar cal = Calendar.getInstance();
		Set<String> farmCodes = new HashSet<String>();
		Farm tmpFarm;
		Object cottonIncome;
		// Double landPloug = 0.0D;
		Double landTotl = 0.0D;
		// Double ridgeFurrow = 0.0D;
		// Double seedCost = 0.0D;
		Double totSow = 0.0D;
		Double totGap = 0.0D;
		Double totWeed = 0.0D;
		Double totCul = 0.0D;
		Double totIrr = 0.0D;
		Double totFer = 0.0D;
		Double totPes = 0.0D;
		Double tothar = 0.0D;
		/*
		 * Double pack = 0.0D; Double tran = 0.0D; Double miscellaneous = 0.0D;
		 */
		Double totExp = 0.0D;

		Double agriIncome = 0.0D;
		Double interCropIncome = 0.0D;
		Double otherSourceIncome = 0.0D;
		Double totalCottonIncome = 0.0D;
		Double totalManure = 0.0D;

		for (Cultivation cultivation : cultivations) {

			if (!StringUtil.isEmpty(selectedYear) && !ObjectUtil.isEmpty(cultivation)) {
				cal.setTime((!ObjectUtil.isEmpty(cultivation.getExpenseDate()) ? cultivation.getExpenseDate()
						: new Date()));
				cultivationBranchId = (!StringUtil.isEmpty(cultivation.getBranchId()) ? cultivation.getBranchId() : "");
				if ((cal.get(Calendar.YEAR) == Integer.parseInt(getSelectedYear())
						|| cal.get(Calendar.YEAR) == (Integer.parseInt(getSelectedYear()) - 1))
						&& cultivationBranchId.equals(!StringUtil.isEmpty(selectedBranch) ? selectedBranch : "")) {
					if (cultivation.getTxnType().equals("1")) {
						farmCodes.add(!StringUtil.isEmpty(cultivation.getFarmId()) ? cultivation.getFarmId() : "");
						agriIncome += !StringUtil.isEmpty(cultivation.getAgriIncome())
								? Double.valueOf(cultivation.getAgriIncome()) : 0;
						interCropIncome += !StringUtil.isEmpty(cultivation.getInterCropIncome())
								? Double.valueOf(cultivation.getInterCropIncome()) : 0;
						otherSourceIncome += !StringUtil.isEmpty(cultivation.getOtherSourcesIncome())
								? Double.valueOf(cultivation.getOtherSourcesIncome()) : 0;
					} else if (cultivation.getTxnType().equals("2")) {
						landTotl += (!StringUtil.isEmpty(cultivation.getLandTotal())
								? Double.parseDouble(cultivation.getLandTotal()) : 0.0);
						totSow += (!StringUtil.isEmpty(cultivation.getTotalSowing())
								? Double.parseDouble(cultivation.getTotalSowing()) : 0.0);
						totGap += (!StringUtil.isEmpty(cultivation.getTotalGap())
								? Double.parseDouble(cultivation.getTotalGap()) : 0.0);
						totWeed += (!StringUtil.isEmpty(cultivation.getTotalWeed())
								? Double.parseDouble(cultivation.getTotalWeed()) : 0.0);
						totCul += (!StringUtil.isEmpty(cultivation.getTotalCulture())
								? Double.parseDouble(cultivation.getTotalCulture()) : 0.0);
						totIrr += (!StringUtil.isEmpty(cultivation.getTotalIrrigation())
								? Double.parseDouble(cultivation.getTotalIrrigation()) : 0.0);
						totFer += (!StringUtil.isEmpty(cultivation.getTotalFertilizer())
								? Double.parseDouble(cultivation.getTotalFertilizer()) : 0.0);
						totPes += (!StringUtil.isEmpty(cultivation.getTotalPesticide())
								? Double.parseDouble(cultivation.getTotalPesticide()) : 0.0);
						tothar += (!StringUtil.isEmpty(cultivation.getTotalHarvest())
								? Double.parseDouble(cultivation.getTotalHarvest()) : 0.0);
						/*
						 * pack +=
						 * (!StringUtil.isEmpty(cultivation.getPackingMaterial()
						 * ) ?
						 * Double.parseDouble(cultivation.getPackingMaterial())
						 * : 0.0); tran +=
						 * (!StringUtil.isEmpty(cultivation.getTransport()) ?
						 * Double.parseDouble(cultivation.getTransport()) :
						 * 0.0); miscellaneous +=
						 * (!StringUtil.isEmpty(cultivation.getMiscellaneous())
						 * ? Double.parseDouble(cultivation.getMiscellaneous())
						 * : 0.0);
						 */
						totExp += (!StringUtil.isEmpty(cultivation.getTotalExpense())
								? Double.parseDouble(cultivation.getTotalExpense()) : 0.0);
						totalManure += (!StringUtil.isEmpty(cultivation.getTotalManure())
								? Double.parseDouble(cultivation.getTotalManure()) : 0.0);
					}
				} else if (StringUtil.isEmpty(selectedBranch)
						&& ((cal.get(Calendar.YEAR) == Integer.parseInt(getSelectedYear()))
								|| cal.get(Calendar.YEAR) == (Integer.parseInt(getSelectedYear()) - 1))) {
					if (cultivation.getTxnType().equals("1")) {
						farmCodes.add(!StringUtil.isEmpty(cultivation.getFarmId()) ? cultivation.getFarmId() : "");
						agriIncome += !StringUtil.isEmpty(cultivation.getAgriIncome())
								? Double.valueOf(cultivation.getAgriIncome()) : 0;
						interCropIncome += !StringUtil.isEmpty(cultivation.getInterCropIncome())
								? Double.valueOf(cultivation.getInterCropIncome()) : 0;
						otherSourceIncome += !StringUtil.isEmpty(cultivation.getOtherSourcesIncome())
								? Double.valueOf(cultivation.getOtherSourcesIncome()) : 0;
					} else if (cultivation.getTxnType().equals("2")) {
						landTotl += (!StringUtil.isEmpty(cultivation.getLandTotal())
								? Double.parseDouble(cultivation.getLandTotal()) : 0.0);
						totSow += (!StringUtil.isEmpty(cultivation.getTotalSowing())
								? Double.parseDouble(cultivation.getTotalSowing()) : 0.0);
						totGap += (!StringUtil.isEmpty(cultivation.getTotalGap())
								? Double.parseDouble(cultivation.getTotalGap()) : 0.0);
						totWeed += (!StringUtil.isEmpty(cultivation.getTotalWeed())
								? Double.parseDouble(cultivation.getTotalWeed()) : 0.0);
						totCul += (!StringUtil.isEmpty(cultivation.getTotalCulture())
								? Double.parseDouble(cultivation.getTotalCulture()) : 0.0);
						totIrr += (!StringUtil.isEmpty(cultivation.getTotalIrrigation())
								? Double.parseDouble(cultivation.getTotalIrrigation()) : 0.0);
						totFer += (!StringUtil.isEmpty(cultivation.getTotalFertilizer())
								? Double.parseDouble(cultivation.getTotalFertilizer()) : 0.0);
						totPes += (!StringUtil.isEmpty(cultivation.getTotalPesticide())
								? Double.parseDouble(cultivation.getTotalPesticide()) : 0.0);
						tothar += (!StringUtil.isEmpty(cultivation.getTotalHarvest())
								? Double.parseDouble(cultivation.getTotalHarvest()) : 0.0);
						/*
						 * pack +=
						 * (!StringUtil.isEmpty(cultivation.getPackingMaterial()
						 * ) ?
						 * Double.parseDouble(cultivation.getPackingMaterial())
						 * : 0.0); tran +=
						 * (!StringUtil.isEmpty(cultivation.getTransport()) ?
						 * Double.parseDouble(cultivation.getTransport()) :
						 * 0.0); miscellaneous +=
						 * (!StringUtil.isEmpty(cultivation.getMiscellaneous())
						 * ? Double.parseDouble(cultivation.getMiscellaneous())
						 * : 0.0);
						 */
						totExp += (!StringUtil.isEmpty(cultivation.getTotalExpense())
								? Double.parseDouble(cultivation.getTotalExpense()) : 0.0);
						totalManure += (!StringUtil.isEmpty(cultivation.getTotalManure())
								? Double.parseDouble(cultivation.getTotalManure()) : 0.0);
					}
				}
			}
		}

		for (String farmCode : farmCodes) {
			if (!StringUtil.isEmpty(farmCode)) {
				tmpFarm = farmerService.findFarmByCode(farmCode);
				if (!ObjectUtil.isEmpty(tmpFarm)) {
					cottonIncome = farmerService.findCottonIncomeByFarmerCode(tmpFarm.getFarmer().getFarmerId(),
							tmpFarm.getFarmCode());
					totalCottonIncome += !ObjectUtil.isEmpty(cottonIncome)
							? Double.valueOf(!ObjectUtil.isEmpty(cottonIncome) ? cottonIncome.toString() : "0.00") : 0;
				}
			}
		}
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("Label", getLocaleProperty("farmer.cocSegregateTitle"));

		jsonObject.put("landTotl", landTotl > 0.0D ? String.valueOf(landTotl) : "0.0");
		jsonObject.put("totSow", (totSow > 0.0D) ? String.valueOf(totSow) : "0.0");
		jsonObject.put("totGap", (totGap > 0.0D) ? String.valueOf(totGap) : "0.0");
		jsonObject.put("totWeed", (totWeed > 0.0D) ? String.valueOf(totWeed) : "0.0");
		jsonObject.put("totCul", (totCul > 0.0D) ? String.valueOf(totCul) : "0.0");
		jsonObject.put("totIrr", (totIrr > 0.0D) ? String.valueOf(totIrr) : "0.0");
		jsonObject.put("totFer", (totFer > 0.0D) ? String.valueOf(totFer) : "0.0");
		jsonObject.put("totPes", (totPes > 0.0D) ? String.valueOf(totPes) : "0.0");
		jsonObject.put("tothar", (tothar > 0.0D) ? String.valueOf(tothar) : "0.0");
		jsonObject.put("totalManure", totalManure > 0.0D ? String.valueOf(totalManure) : "0.0");

		jsonObject.put("agriIncome", (agriIncome > 0.0D) ? String.valueOf(agriIncome) : "0.0");
		jsonObject.put("interCropIncome", (interCropIncome > 0.0D) ? String.valueOf(interCropIncome) : "0.0");
		jsonObject.put("otherSourceIncome", (otherSourceIncome > 0.0D) ? String.valueOf(otherSourceIncome) : "0.0");
		jsonObject.put("totalCottonIncome", (totalCottonIncome > 0.0D) ? String.valueOf(totalCottonIncome) : "0.0");

		jsonObject.put("expLabel", getLocaleProperty("farmer.expense"));
		jsonObject.put("revLabel", getLocaleProperty("farmer.revenue"));
		// jsonObject.put("plougLabel", getLocaleProperty("farmer.plough"));
		jsonObject.put("totalLandLabel", getLocaleProperty("farmer.land"));
		/*
		 * jsonObject.put("ridgeLabel", getLocaleProperty("farmer.ridge"));
		 * jsonObject.put("seedLabel", getLocaleProperty("farmer.seedCost"));
		 */
		jsonObject.put("sowLabel", getLocaleProperty("farmer.sowing"));
		jsonObject.put("gapLabel", getLocaleProperty("farmer.gap"));
		jsonObject.put("weedLabel", getLocaleProperty("farmer.weed"));
		jsonObject.put("culLabel", getLocaleProperty("farmer.cultivation"));
		jsonObject.put("irrLabel", getLocaleProperty("farmer.irrigation"));
		jsonObject.put("ferLabel", getLocaleProperty("farmer.fertilizer"));
		jsonObject.put("pesLabel", getLocaleProperty("farmer.pesticide"));
		jsonObject.put("harvestLabel", getLocaleProperty("farmer.harvest"));
		jsonObject.put("totalManureLabel", getLocaleProperty("farmer.totalManure"));

		jsonObject.put("agriIncomeLabel", getLocaleProperty("farmer.agriIncome"));
		jsonObject.put("interCropIncomeLabel", getLocaleProperty("farmer.interCropIncome"));
		jsonObject.put("otherSourceIncomeLabel", getLocaleProperty("farmer.otherSourceIncome"));
		jsonObject.put("totalCottonIncomeLabel", getLocaleProperty("farmer.totalCottonIncome"));
		printAjaxResponse(jsonObject, "text/html");

	}

	@SuppressWarnings("unchecked")
	public void populateAgentTrainings(){
		List<JSONObject> agentTrainVal=new ArrayList<JSONObject>();
		//Map<String,Long> traineeDet=new LinkedHashMap<>(); 
		if(selectedWarehouse!=null && !StringUtil.isEmpty(selectedWarehouse)){
			List<Object[]> agentTrainings=farmerService.findAgentTrainingData(Long.parseLong(selectedWarehouse),selectedFinYear);
			if(agentTrainings!=null && !ObjectUtil.isListEmpty(agentTrainings)){
				agentTrainings.stream().forEach(d->{
					Map<String,Long> traineeDet=new LinkedHashMap<>();
					Map<String,Long> traineeCountDet=new LinkedHashMap<>();
					Object[] vArr=(Object[])d;
					JSONObject jObj=new JSONObject();
					Long trainDetLen=0l;
					jObj.put("agentId",vArr[0]);
					jObj.put("agentName",vArr[2]);
					jObj.put("agentType",vArr[1]);
					if(!ObjectUtil.isEmpty(vArr[4])&& vArr[4]!=null){
					String fLevel[]=vArr[4].toString().split("#");
					String sLevel[]=fLevel[1].split("\\$");
					for(String slData: sLevel){
						String trdLevel[]=slData.split("~");
						List <String>farmersLevel=Arrays.asList(trdLevel[2].split(","));
						trainDetLen+=(Arrays.asList(trdLevel[1].split(","))).size();
						Long cnt=farmersLevel.stream().collect(Collectors.counting());
						 String frLevel=trdLevel[3];
						 if(frLevel.contains("@")){
							String multiTrain[]=frLevel.split("@");
							for(String mtName:multiTrain){
							 if(traineeDet.containsKey(mtName.trim())){
								 traineeDet.put(mtName.trim(), traineeDet.get(mtName) + 1);
							 }
							 else{
								 traineeDet.put(mtName.trim(),1l);
							 }
							 if(traineeCountDet.containsKey(mtName.trim())){
								 traineeCountDet.put(mtName.trim(), traineeCountDet.get(mtName) + cnt);
							 }
							 else{
								 traineeCountDet.put(mtName.trim(),cnt);
							 }
							}
						 }
						 else{
						 if(traineeDet.containsKey(frLevel.trim())){
							 traineeDet.put(frLevel, traineeDet.get(frLevel) + 1);
						 }
						 else{
							 traineeDet.put(frLevel,1l);
						 }
						 if(traineeCountDet.containsKey(frLevel.trim())){
							 traineeCountDet.put(frLevel, traineeCountDet.get(frLevel) + cnt);
						 }
						 else{
							 traineeCountDet.put(frLevel,cnt);
						 }
						 }
						 
					}
					}
					 String trData =traineeDet.entrySet().stream().map(e -> e.getKey()+"="+e.getValue()).collect(joining("^"));
					String trineeData=traineeCountDet.entrySet().stream().map(e -> e.getKey()+"="+e.getValue()).collect(joining("^"));
 					jObj.put("trainingCount", traineeDet.size());
					jObj.put("trainData",trainDetLen);
					jObj.put("trainingData",trData);
					jObj.put("traineeData", trineeData);
					System.out.println("jObj: "+jObj);
					agentTrainVal.add(jObj);
				});
			}
			
		}
		printAjaxResponse(agentTrainVal, "text/html");
	}

	@SuppressWarnings("unchecked")
	public void populateTrainingChart() {
		List<JSONObject> trainingDet=new ArrayList<JSONObject>();
		List<JSONObject> branches=new ArrayList<JSONObject>();
		//if(StringUtil.isEmpty(getSelectedBranch())){
		   List<Object[]> trainingByBranch = farmerService.populateTrainingChart(getSelectedBranch(), selectedFinYear);
			if(trainingByBranch!=null && !ObjectUtil.isListEmpty(trainingByBranch)){
			trainingByBranch.stream().forEach(t->{
				Object[] tArr=(Object[]) t;
				JSONObject jsonObj=new JSONObject();
				String []trCount=tArr[0].toString().split(",");
				jsonObj.put("totTraining", trCount.length);
				jsonObj.put("branchName", tArr[1]);
				jsonObj.put("branchId", tArr[2]);
				jsonObj.put("warehouseVal", tArr[3]);
				branches.add(jsonObj);
			});
			}
	//}
		printAjaxResponse(branches, "text/html");
	}
	Double totalLand = 0.0D;
	Double totalCottonIncome = 0.0D;
	Double totalRevenue = 0.0D;
	Double totalExpense = 0.0D;

	@SuppressWarnings("unchecked")
	public void populateCocSegregate() {

		Date sDate = null;
		Date eDate = null;
		String[] dateArry = null;
		Object[] farmObject;
		// Double expenses = 0.0D;
		// Double revenues = 0.0D;
		Double totalIncome = 0.0D;
		String cultivationBranchId = "";
		Calendar cal = Calendar.getInstance();

		Farm tmpFarm;
		Object cottonIncome;
		// Double landPloug = 0.0D;
		Double totalSoilPrepartion = 0.0D;
		// Double ridgeFurrow = 0.0D;
		// Double seedCost = 0.0D;

		Double agriIncome = 0.0D;
		Double interCropIncome = 0.0D;
		Double otherSourceIncome = 0.0D;

		Double totalManure = 0.0D;
		Double labourCost = 0.0D;
		Double totSeed = 0.0D;

		Double totalInput = 0.0D;
		Double otherCosts = 0.0D;
		Double extraCost = 0.0D;

		if (!StringUtil.isEmpty(selectedSeason) && selectedSeason.contains("-")) {
			HarvestSeason harvestSeason = clientService.findSeasonByCode(selectedSeason);
			dateArry = harvestSeason.getName().split("-");
		}

		/*
		 * if (!ObjectUtil.isEmpty(farmerIds) && farmerIds.size() > 0) { if
		 * (!getCurrentTenantId().equalsIgnoreCase(ESESystem.CANDA_TENANT_ID)) {
		 * totalCottonIncome =
		 * farmerService.findCottonIncByFarmerCode(farmerIds, farmCodes); } else
		 * { totalCottonIncome = farmerService.findSaleCottonByCoc(farmerIds,
		 * farmCodes, selectedSeason); } String totLand =
		 * farmerService.findTotalLandByFarmCode(farmerIds, farmCodes);
		 * 
		 * totalLand = Double.valueOf(!StringUtil.isEmpty(totLand) ? totLand :
		 * "0.0");
		 * 
		 * if (totalLand == 0) { totalLand = 1D; } }
		 */
		List<JSONObject> totExpRevObjects = new ArrayList<>();
		List<JSONObject> totalNetObjects = new ArrayList<>();
		List<List<JSONObject>> jsonObjects = new ArrayList<>();
		List<JSONObject> cocDataJsonObjects = new ArrayList<>();
		List<JSONObject> reveProdJsonList = new ArrayList<>();
		List<Object[]> seasonList = farmerService.findSeasonCodeList(selectedBranch);
		if (seasonList.size() > 0) {
			seasonList.stream().forEach(obj -> {
				Double totalRevPerAcre = 0.0;
				Double totExpPerAcre = 0.0;
				List<String> farmerIds = new ArrayList();
				JSONObject cocExpDataJson = new JSONObject();
				JSONObject cocDatas = new JSONObject();
				Object[] seasonObj = (Object[]) obj;
				cocExpDataJson.put("season", String.valueOf(seasonObj[1]));
				// List<Object[]> salesIncomeList=null;
				Object[] objArr = farmerService.listObjectCultivationExpenses(selectedBranch,
						String.valueOf(seasonObj[0]),
						!StringUtil.isEmpty(selectedYear) && selectedYear != "" ? selectedYear : "");
				if (objArr!=null && !ObjectUtil.isEmpty(objArr) && objArr.length > 0 && objArr[0]!=null && !ObjectUtil.isEmpty(objArr[0])) {

					farmCodes = farmerService.findFarmCodesByCultivation(selectedBranch, String.valueOf(seasonObj[0]),
							!StringUtil.isEmpty(selectedYear) && selectedYear != "" ? selectedYear : "");
					if (farmCodes.size() > 0) {
						farmerIds = farmerService.findFarmerIdsByfarmCode(farmCodes);
					}

					// totalCottonIncome =
					// farmerService.findCottonIncByFarmerCode(farmerIds,
					// farmCodes);
					Object[] objAr = farmerService.findSaleIncomeByFarmer(farmerIds, farmCodes);

					String totLand = farmerService.findTotalLandByFarmCode(farmerIds, farmCodes);

					totalLand = Double.valueOf(!StringUtil.isEmpty(totLand) ? totLand : "0.0");

					double totalSoil = (StringUtil.isDouble(String.valueOf(objArr[5]))
							? Double.parseDouble(String.valueOf(objArr[5])) : 0.0);

					double totalWeed = (StringUtil.isDouble(objArr[6])

							? Double.parseDouble(String.valueOf(objArr[6])) : 0.0);
					double totalIrr = (StringUtil.isDouble(objArr[7]) ? Double.parseDouble(String.valueOf(objArr[7]))
							: 0.0);
					double totalFer = (StringUtil.isDouble(String.valueOf(objArr[8]))
							? Double.parseDouble(String.valueOf(objArr[8])) : 0.0);
					double totalPes = (StringUtil.isDouble(String.valueOf(objArr[9]))
							? Double.parseDouble(String.valueOf(objArr[9])) : 0.0);
					double totalExtraCost = (StringUtil.isDouble(String.valueOf(objArr[10]))
							? Double.parseDouble(String.valueOf(objArr[10])) : 0.0);

					double totManure = (StringUtil.isDouble(String.valueOf(objArr[11]))
							? Double.parseDouble(String.valueOf(objArr[11])) : 0.0);

					double totalSeed = (StringUtil.isDouble(String.valueOf(objArr[12]))
							? Double.parseDouble(String.valueOf(objArr[12])) : 0.0);

					double totalLabourCost = (StringUtil.isDouble(String.valueOf(objArr[13]))
							? Double.parseDouble(String.valueOf(objArr[13])) : 0.0);

					double totInput = (StringUtil.isDouble(String.valueOf(objArr[14]))
							? Double.parseDouble(String.valueOf(objArr[14])) : 0.0);
					double totGap = (StringUtil.isDouble(String.valueOf(objArr[16]))
							? Double.parseDouble(String.valueOf(objArr[16])) : 0.0);
					double totalHarvest = (StringUtil.isDouble(String.valueOf(objArr[17]))
							? Double.parseDouble(String.valueOf(objArr[17])) : 0.0);
					Double totalExp = totalSoil + totalWeed + totalIrr + totalFer + totalPes + totalExtraCost
							+ totManure + totalSeed + totalLabourCost + totInput + totGap + totalHarvest;
					totExpPerAcre = totalExp;

					double totAgriIncome = (StringUtil.isDouble(String.valueOf(objAr[2]))
							? Double.parseDouble(String.valueOf(objAr[2])) : 0.0);
					double totInterIncome = (StringUtil.isDouble(String.valueOf(objAr[3]))
							? Double.parseDouble(String.valueOf(objAr[3])) : 0.0);
					double totOthrIncome = (StringUtil.isDouble(String.valueOf(objAr[4]))
							? Double.parseDouble(String.valueOf(objAr[4])) : 0.0);
					Double revenue = totAgriIncome + totInterIncome + totOthrIncome;

					Double[] value = { totAgriIncome, totInterIncome, totOthrIncome };
					// addCocJsonObject(values, String.valueOf(seasonObj[1]));
					String[] product = { this.getLocaleProperty("farmer.agriIncome"),
							this.getLocaleProperty("farmer.InterIncome"), this.getLocaleProperty("farmer.OthrIncome") };

					for (int j = 0; j < value.length; ++j) {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("values",
								value[j] > 0.0
										? Double.valueOf(CurrencyUtil
												.getDecimalFormat(Double.valueOf(String.valueOf(value[j])), "##.00"))
										: "0.0");
						jsonObject.put("product", StringUtil.isEmpty(product[j]) ? "" : String.valueOf(product[j]));
						HarvestSeason harvestSeason = farmerService.findHarvestSeasonByCode(String.valueOf(objAr[5]));
						jsonObject.put("season", harvestSeason.getName());
						// jsonObject.put("season", seasonObj[1]);
						// totalCottonIncome+=Double.valueOf(String.valueOf(objAr[5]));

						reveProdJsonList.add(jsonObject);
					}

					totalRevPerAcre = revenue;
					totalRevenue += totalRevPerAcre;
					totalExpense += totExpPerAcre;
					String exp = formatter.format(totExpPerAcre);
					String rev = formatter.format(totalRevPerAcre);
					cocExpDataJson.put("expense", Double.valueOf(exp));
					cocExpDataJson.put("revenue", Double.valueOf(rev));
					// supplyDataJsonObjects.add(supplyDataJson);
					totExpRevObjects.add(cocExpDataJson);
					Double[] values = { totalSeed, totalSoil, totalIrr, totalWeed, totInput, totalLabourCost, totManure,
							totalPes, totalFer, totGap, totalHarvest, totalExtraCost };
					// addCocJsonObject(values, String.valueOf(seasonObj[1]));
					String[] items = { this.getLocaleProperty("farmer.seedCost"),
							this.getLocaleProperty("farmer.soilPrepar"), this.getLocaleProperty("farmer.irrigation"),
							this.getLocaleProperty("farmer.weed"), this.getLocaleProperty("farmer.input"),
							this.getLocaleProperty("farmer.labourCost"), this.getLocaleProperty("farmer.totalManure"),
							this.getLocaleProperty("farmer.pesticide"), this.getLocaleProperty("farmer.fertilizer"),
							this.getLocaleProperty("farmer.totalGap"), this.getLocaleProperty("farmer.totalHarvest"),
							this.getLocaleProperty("farmer.otherCosts") };

					for (int j = 0; j < values.length; ++j) {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("values",
								values[j] > 0.0
										? Double.valueOf(CurrencyUtil
												.getDecimalFormat(Double.valueOf(String.valueOf(values[j])), "##.00"))
										: "0.0");
						jsonObject.put("items", StringUtil.isEmpty(items[j]) ? "" : String.valueOf(items[j]));
						jsonObject.put("season", seasonObj[1]);
						cocDataJsonObjects.add(jsonObject);
					}
				}

			});
			// double perAcre = totalRevenue - totalExpense;
			// String perAcreStr = String.valueOf(perAcre).replace("-", "");
			/*
			 * String[] items = { getLocaleProperty("farmer.expense"),
			 * getLocaleProperty("farmer.revenue"),
			 * getLocaleProperty("farmer.totalAcre") }; Double[] values = {
			 * totalExpense, totalRevenue, Double.valueOf(perAcreStr) };
			 */
			/*
			 * for (int j = 0; j < values.length; ++j) { JSONObject jsonObject =
			 * new JSONObject(); jsonObject.put("amounts", Double
			 * .valueOf(CurrencyUtil.getDecimalFormat(Double.valueOf(String.
			 * valueOf(values[j])), "##.00"))); jsonObject.put("labels",
			 * StringUtil.isEmpty(items[j]) ? "" : String.valueOf(items[j]));
			 * totalNetObjects.add(jsonObject); }
			 */
			if(!ObjectUtil.isListEmpty(cocDataJsonObjects) && cocDataJsonObjects.size()>0)
				jsonObjects.add(cocDataJsonObjects);
			if(!ObjectUtil.isListEmpty(totExpRevObjects) && totExpRevObjects.size()>0)
			jsonObjects.add(totExpRevObjects);
			if(!ObjectUtil.isListEmpty(reveProdJsonList) && reveProdJsonList.size()>0)
			// jsonObjects.add(totalNetObjects);
			jsonObjects.add(reveProdJsonList);

		}
		printAjaxResponse(jsonObjects, "text/html");

	}

	Double totQty = 0.0;
	double tot;

	@SuppressWarnings("unchecked")
	public void populateFarmerProdDetailsBarChartData() {

		Double totalYield = 0.0;

		JSONObject jsonObject = new JSONObject();

		Integer farmerData = new Integer(0);
		if (!StringUtil.isEmpty(getBranchId())) {
			selectedBranch = getBranchId();
		}
		farmerData = farmerService.findTotalFarmerCount(selectedBranch, selectedStapleLen, selectedSeason,
				selectedGender, selectedState);

		/*
		 * farmCodes = farmerService.findTotalFarmCodeByBranch(selectedBranch,
		 * !StringUtil.isEmpty(selectedYear) ? Integer.parseInt(selectedYear) :
		 * 0, selectedSeason);
		 */
		totalEstimatedYield = farmerService.findTotalYieldByBranch(selectedBranch, selectedStapleLen, selectedSeason,
				selectedState);
		Object[] obj = farmerService.findTotalQtyByCropHarvest(selectedBranch, selectedSeason, selectedStapleLen,
				selectedState);
		/*
		 * totalProposedPlantArea =
		 * farmerService.findTotalLandByPostHarvest(selectedBranch,
		 * !StringUtil.isEmpty(selectedYear) ? Integer.parseInt(selectedYear) :
		 * 0, selectedSeason);
		 */
		if (!ObjectUtil.isEmpty(obj[0])) {
			totalYield = Double.valueOf(!StringUtil.isEmpty(String.valueOf(obj[1])) && String.valueOf(obj[1]) != "null"
					? String.valueOf(obj[1]) : "0") / Double.parseDouble(String.valueOf(obj[0]));
			totQty = Double.valueOf(!StringUtil.isEmpty(String.valueOf(obj[1])) && String.valueOf(obj[1]) != "null"
					? String.valueOf(obj[1]) : "0");
			totalProposedPlantArea = String.valueOf(obj[0]);
		}

		jsonObject.put("Label", getLocaleProperty("farmerProdDetailsChart"));

		jsonObject.put("farmerCount", farmerData > 0 ? String.valueOf(farmerData) : "0.0");
		jsonObject.put("totalProposedPlantArea", !StringUtil.isEmpty(totalProposedPlantArea)
				? formatter.format(Double.valueOf(totalProposedPlantArea)) : "0.0");
		jsonObject.put("totalEstimatedYield", !StringUtil.isEmpty(totalEstimatedYield)
				? formatter.format(Double.valueOf(totalEstimatedYield)) : "0.0");
		jsonObject.put("totQty", formatter.format(totQty));
		jsonObject.put("totalYield", totalYield > 0.0D ? formatter.format(Double.valueOf(totalYield)) : "0.0");
		printAjaxResponse(jsonObject, "text/html");

	}

	public List<Object[]> getBranchesInfo() {
		List<Object[]> branchMasters = new ArrayList<>();
		Object[] arry = new Object[2];
		arry[0] = "";
		arry[1] = getLocaleProperty("select");

		branchMasters.addAll(clientService.listBranchMastersInfo());
		return branchMasters;
	}

	public Map<Integer, String> getStateList() {

		Map<Integer, String> stateMap = new LinkedHashMap<Integer, String>();

		List<State> stateList = locationService.listOfStatesByBranch(selectedBranch);
		for (State obj : stateList) {
			stateMap.put((int) obj.getId(), obj.getCode() + " - " + obj.getName());

		}
		return stateMap;

	}
	


	public String getSelectedStapleLen() {
		return selectedStapleLen;
	}

	public void setSelectedStapleLen(String selectedStapleLen) {
		this.selectedStapleLen = selectedStapleLen;
	}

	@SuppressWarnings("unchecked")
	public void populateStapleLenList() {

		JSONArray stapleArr = new JSONArray();
		List<Object[]> stapleList = catalogueService
				.findCatalogueCodeAndDisNameByType(Integer.valueOf(getText("stapleLength")));

		if (!ObjectUtil.isEmpty(stapleList)) {
			stapleList.forEach(obj -> {
				stapleArr.add(getJSONObject(obj[0].toString(), getText("stapleLenEng" + obj[2].toString())));
			});
		}
		sendAjaxResponse(stapleArr);

	}

	public String getTotalProposedPlantArea() {
		return totalProposedPlantArea;
	}

	public void setTotalProposedPlantArea(String totalProposedPlantArea) {
		this.totalProposedPlantArea = totalProposedPlantArea;
	}

	public Double getTotalEstimatedYield() {
		return totalEstimatedYield;
	}

	public void setTotalEstimatedYield(double totalEstimatedYield) {
		this.totalEstimatedYield = totalEstimatedYield;
	}

	@SuppressWarnings("unchecked")
	public void populateProcurementDeatilsBarChart() {
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<Object[]> procurementData = new LinkedList<>();
		/*
		 * if
		 * (!getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)
		 * && !getCurrentTenantId().equalsIgnoreCase(ESESystem.KPF_TENANT_ID) &&
		 * !getCurrentTenantId().equalsIgnoreCase(ESESystem.CABI_TENANT_ID)) {
		 */
		 if(!getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)){
				if (!getIsKpfBased().equals("1")) {
					if (!StringUtil.isEmpty(getSelectedProduct()) || !StringUtil.isEmpty(getDateRange())) {
						procurementData = productService.findProcurementDataByFilter(
								!StringUtil.isEmpty(getSelectedProduct()) ? Long.valueOf(selectedProduct) : 0L, getDateRange());
					} else {
						procurementData = productService.findProcurementCummulativeData();
					}
				} else {
					if (!StringUtil.isEmpty(getSelectedProduct()) || !StringUtil.isEmpty(getDateRange())) {
						procurementData = productService.findSupplierProcurementDataByFilter(
								!StringUtil.isEmpty(getSelectedProduct()) ? Long.valueOf(selectedProduct) : 0L, getDateRange());
					} else {
						procurementData = productService.findSupplierProcurementCummulativeData();
					}
				}
		 }else{
			 if (!StringUtil.isEmpty(getSelectedProduct()) || !StringUtil.isEmpty(getDateRange())) {
					procurementData = productService.findProcurementTraceabilityDataByFilter(
							!StringUtil.isEmpty(getSelectedProduct()) ? Long.valueOf(selectedProduct) : 0L, getDateRange());
				} else {
					procurementData = productService.findProcurementTraceabilityCummulativeData();
				}
		 }
		
	
		procurementData.stream().forEach(obj -> {
			if (!StringUtil.isEmpty(obj[0])) {
				JSONObject noOfBags = new JSONObject();
				noOfBags.put("item", getLocaleProperty("noOfBags"));
				noOfBags.put("values", obj[0]);
				jsonObjects.add(noOfBags);
			} else {
				JSONObject noOfBags = new JSONObject();
				noOfBags.put("item", getLocaleProperty("noOfBags"));
				noOfBags.put("values", 0);
				jsonObjects.add(noOfBags);
			}
			if (!StringUtil.isEmpty(obj[1])) {
				JSONObject grossWeight = new JSONObject();
				grossWeight.put("item", getLocaleProperty("grossWeight") + "(" + getLocaleProperty("units")+ ")");
				grossWeight.put("values", obj[1]);
				jsonObjects.add(grossWeight);
			} else {
				JSONObject grossWeight = new JSONObject();
				grossWeight.put("item", getLocaleProperty("grossWeight") + "(" + getLocaleProperty("units")+ ")");
				grossWeight.put("values", 0);
				jsonObjects.add(grossWeight);
			}
			if (!StringUtil.isEmpty(obj[2])) {
				JSONObject subTotal = new JSONObject();
				subTotal.put("item", getLocaleProperty("subTotal") + "(" + getCurrencyType()+ ")");
				subTotal.put("values", obj[2]);
				jsonObjects.add(subTotal);
			} else {
				JSONObject subTotal = new JSONObject();
				subTotal.put("item", getLocaleProperty("subTotal") + "(" + getCurrencyType() + ")");
				subTotal.put("values", 0);
				jsonObjects.add(subTotal);
			}
		});
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("label", getLocaleProperty("procurementDeatilsChart"));
		jsonObjects.add(jsonObject);

		printAjaxResponse(jsonObjects, "text/html");
	}

	public String getSelectedProduct() {
		return selectedProduct;
	}

	public void setSelectedProduct(String selectedProduct) {
		this.selectedProduct = selectedProduct;
	}

	public String getDateRange() {
		return dateRange;
	}

	public void setDateRange(String dateRange) {
		this.dateRange = dateRange;
	}

	public Map<String, String> getProductsList() {
		return productDistributionService.listProcurementProductByType(Procurement.productType.GOODS.ordinal()).stream()
				.collect(Collectors.toMap(product -> String.valueOf(product.getId()), ProcurementProduct::getName));
	}

	public Map<String, String> getYearTimeList() {
		Map<String, String> yearList = new HashMap();
		for (int i = 2016; i <= DateUtil.getCurrentYear() + 1; i++) {

			yearList.put(String.valueOf(i - 1), String.valueOf(i - 1) + " - " + String.valueOf(i));

		}
		return yearList;
	}

	public Map<String, String> getCultiYearList() {
		Map<String, String> yearList = new HashMap();
		for (int i = 2014; i <= DateUtil.getCurrentYear(); i++) {
			yearList.put(String.valueOf(i), String.valueOf(i));
		}
		return yearList;
	}

	public Map<String, String> getListProcurementProduct() {

		Map<String, String> farmCropsMap = new LinkedHashMap<String, String>();
		List<ProcurementProduct> cropsList = productDistributionService.listProcurementProduct();
		for (ProcurementProduct farmCrop : cropsList) {
			farmCropsMap.put(farmCrop.getCode(), farmCrop.getName());
		}
		return farmCropsMap;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getCooperativeList() {
		Map<String, String> warehouseMap = new LinkedHashMap<>();

		warehouseMap = getFarmCatalougeMap(Integer.valueOf(getText("cooperativeType")));
		return warehouseMap;

	}

	public Map<Long, String> getSamithiList() {

		samithi = locationService.listSamithiesBasedOnType();
		Map<Long, String> samithiMap = new LinkedHashMap<>();
		samithiMap = samithi.stream().collect(Collectors.toMap(Warehouse::getId, Warehouse::getName));
		return samithiMap;
	}

	public String getSelectedState() {
		return selectedState;
	}

	public void setSelectedState(String selectedState) {
		this.selectedState = selectedState;
	}

	public String getSelectedGender() {
		return selectedGender;
	}

	public void setSelectedGender(String selectedGender) {
		this.selectedGender = selectedGender;
	}

	public Map<String, String> getGenderList() {
		genderList.put(Farmer.SEX_MALE, getText("MALE"));
		genderList.put(Farmer.SEX_FEMALE, getText("FEMALE"));
		return genderList;
	}

	public void setGenderList(Map<String, String> genderList) {

		this.genderList = genderList;
	}

	public String getSelectedSeason() {
		return selectedSeason;
	}

	public void setSelectedSeason(String selectedSeason) {
		this.selectedSeason = selectedSeason;
	}

	public Map<String, String> getSeasonList() {

		Map<String, String> seasonMap = new LinkedHashMap<String, String>();
		List<Object[]> seasonList = farmerService.listSeasonCodeAndName();
		for (Object[] obj : seasonList) {

			seasonMap.put(String.valueOf(obj[0]), String.valueOf(obj[1]));
		}
		return seasonMap;

	}

	public List<Warehouse> getSamithi() {
		return samithi;
	}

	public void setSamithi(List<Warehouse> samithi) {
		this.samithi = samithi;
	}

	@SuppressWarnings("unchecked")
	public void populateKPFTxnChartData() {
		JSONObject jsonObject = new JSONObject();
		List<List<JSONObject>> jsonObjects = new ArrayList<>();
		List<JSONObject> labelDataJsonObjects = new ArrayList<>();
		List<Object> procurementQtyData = new ArrayList<>();
		List<Object> procurementData = new ArrayList<>();
		/*
		 * if (getCurrentTenantId().equalsIgnoreCase(ESESystem.KPF_TENANT_ID) ||
		 * getCurrentTenantId().equalsIgnoreCase(ESESystem.CABI_TENANT_ID))
		 */
		if (getIsKpfBased().equals("1")) {
			procurementData = productService.listSupplierProcurementAmtByMoth(
					DateUtil.getFirstDateOfMonth(DateUtil.getCurrentYear(), 0), new Date());

			procurementQtyData = productService.listSupplierProcurementQtyByMoth(
					DateUtil.getFirstDateOfMonth(DateUtil.getCurrentYear(), 0), new Date());

			List<JSONObject> procurementDataJsonObjects = new ArrayList<>();
			List<JSONObject> enrollmentDataJsonObjects = new ArrayList<>();
			List<JSONObject> procurementQtyJsonObjects = new ArrayList<>();
			List<JSONObject> procurementGroupJsonObjects = new ArrayList<>();
			for (int i = 1; i <= 12; i++) {
				JSONObject procurementDataJson = new JSONObject();
				procurementDataJson.put("year", "");
				procurementDataJson.put("month", i);
				procurementDataJson.put("amt", 0);
				// procurementDataJson.put("fpo", "");
				procurementDataJsonObjects.add(procurementDataJson);
			}

			procurementData.stream().forEach(obj -> {
				JSONObject procurementDataJson = new JSONObject();
				Object[] objArr = (Object[]) obj;
				procurementDataJson = procurementDataJsonObjects.get(Integer.parseInt(String.valueOf(objArr[1])) - 1);
				procurementDataJson.put("year", objArr[0]);
				procurementDataJson.put("month", objArr[1]);
				procurementDataJson.put("amt", objArr[2]);
				// procurementDataJson.put("fpo",
				// getCatalouge(String.valueOf(objArr[3])));
				procurementDataJsonObjects.set(Integer.parseInt(String.valueOf(objArr[1])) - 1, procurementDataJson);
			});

			for (int i = 1; i <= 12; i++) {
				JSONObject procurementQtyDataJson = new JSONObject();
				procurementQtyDataJson.put("year", "");
				procurementQtyDataJson.put("month", i);
				procurementQtyDataJson.put("qty", 0);

				procurementQtyJsonObjects.add(procurementQtyDataJson);
			}

			procurementQtyData.stream().forEach(obj -> {
				JSONObject procurementQtyDataJson = new JSONObject();
				Object[] objArr = (Object[]) obj;
				procurementQtyDataJson = procurementQtyJsonObjects.get(Integer.parseInt(String.valueOf(objArr[1])) - 1);
				procurementQtyDataJson.put("year", objArr[0]);
				procurementQtyDataJson.put("month", objArr[1]);
				procurementQtyDataJson.put("qty", objArr[2]);
				procurementQtyJsonObjects.set(Integer.parseInt(String.valueOf(objArr[1])) - 1, procurementQtyDataJson);
			});

			procurementData.stream().forEach(obj -> {
				JSONObject procurementDataJson = new JSONObject();
				Object[] objArr = (Object[]) obj;
				if (objArr.length > 0 && !StringUtil.isEmpty(objArr[0]) && !StringUtil.isEmpty(objArr[1])) {
					List<Object> farmerGroupData = productService.listSupplierProcurementGroupByMoth(
							Integer.valueOf(String.valueOf(objArr[0])), Integer.valueOf(String.valueOf(objArr[1])));

					farmerGroupData.stream().forEach(objGroup -> {
						JSONObject procurementGroupDataJson = new JSONObject();
						Object[] objArrGroup = (Object[]) objGroup;
						procurementGroupDataJson.put("year", objArrGroup[0]);
						procurementGroupDataJson.put("month", objArrGroup[1]);
						procurementGroupDataJson.put("amt", objArrGroup[2]);
						procurementGroupDataJson.put("qty", objArrGroup[3]);

						procurementGroupDataJson.put("fpo", getCatalouge(String.valueOf(objArrGroup[4])));
						procurementGroupJsonObjects.add(procurementGroupDataJson);
					});
				}

			});

			jsonObject.put("Label", getLocaleProperty("groupchart.title"));
			jsonObject.put("procurementLabel", getLocaleProperty("procurement") + " (" + getCurrencyType() + ")");
			jsonObject.put("procurementQtyLabel",
					getLocaleProperty("procurementQtyLabel") + getLocaleProperty("units"));
			labelDataJsonObjects.add(jsonObject);
			jsonObjects.add(procurementDataJsonObjects);
			jsonObjects.add(procurementQtyJsonObjects);
			jsonObjects.add(procurementGroupJsonObjects);
			jsonObjects.add(labelDataJsonObjects);

			printAjaxResponse(jsonObjects, "text/html");

		} else {
			procurementData = productService.listSupplierProcurementAmtByMoth(
					DateUtil.getFirstDateOfMonth(DateUtil.getCurrentYear(), 0), new Date());

			procurementQtyData = productService.listSupplierProcurementQtyByMoth(
					DateUtil.getFirstDateOfMonth(DateUtil.getCurrentYear(), 0), new Date());
			List<JSONObject> procurementDataJsonObjects = new ArrayList<>();
			List<JSONObject> enrollmentDataJsonObjects = new ArrayList<>();
			List<JSONObject> procurementQtyJsonObjects = new ArrayList<>();
			List<JSONObject> procurementGroupJsonObjects = new ArrayList<>();
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
				procurementDataJson = procurementDataJsonObjects.get(Integer.parseInt(String.valueOf(objArr[1])) - 1);
				procurementDataJson.put("year", objArr[0]);
				procurementDataJson.put("month", objArr[1]);
				procurementDataJson.put("amt", objArr[2]);
				procurementDataJsonObjects.set(Integer.parseInt(String.valueOf(objArr[1])) - 1, procurementDataJson);
			});

			for (int i = 1; i <= 12; i++) {
				JSONObject procurementQtyDataJson = new JSONObject();
				procurementQtyDataJson.put("year", "");
				procurementQtyDataJson.put("month", i);
				procurementQtyDataJson.put("qty", 0);
				procurementQtyJsonObjects.add(procurementQtyDataJson);
			}

			procurementQtyData.stream().forEach(obj -> {
				JSONObject procurementQtyDataJson = new JSONObject();
				Object[] objArr = (Object[]) obj;
				procurementQtyDataJson = procurementQtyJsonObjects.get(Integer.parseInt(String.valueOf(objArr[1])) - 1);
				procurementQtyDataJson.put("year", objArr[0]);
				procurementQtyDataJson.put("month", objArr[1]);
				procurementQtyDataJson.put("qty", objArr[2]);
				procurementQtyJsonObjects.set(Integer.parseInt(String.valueOf(objArr[1])) - 1, procurementQtyDataJson);
			});

			procurementData.stream().forEach(obj -> {
				JSONObject procurementDataJson = new JSONObject();
				Object[] objArr = (Object[]) obj;
				if (objArr.length > 0 && !StringUtil.isEmpty(objArr[0]) && !StringUtil.isEmpty(objArr[1])) {
					List<Object> farmerGroupData = productService.listProcurementGroupByMoth(
							Integer.valueOf(String.valueOf(objArr[0])), Integer.valueOf(String.valueOf(objArr[1])));

					farmerGroupData.stream().forEach(objGroup -> {
						JSONObject procurementGroupDataJson = new JSONObject();
						Object[] objArrGroup = (Object[]) objGroup;
						procurementGroupDataJson.put("year", objArrGroup[0]);
						procurementGroupDataJson.put("month", objArrGroup[1]);
						procurementGroupDataJson.put("amt", objArrGroup[2]);
						procurementGroupDataJson.put("qty", objArrGroup[3]);
						procurementGroupJsonObjects.add(procurementGroupDataJson);
					});
				}

			});

			jsonObject.put("Label", getLocaleProperty("groupchart.title"));
			jsonObject.put("procurementLabel", getLocaleProperty("procurement") + " (" + getCurrencyType() + ")");
			jsonObject.put("procurementQtyLabel", getLocaleProperty("procurementQtyLabel"));
			labelDataJsonObjects.add(jsonObject);
			jsonObjects.add(procurementDataJsonObjects);
			jsonObjects.add(procurementQtyJsonObjects);
			jsonObjects.add(procurementGroupJsonObjects);
			jsonObjects.add(labelDataJsonObjects);

			printAjaxResponse(jsonObjects, "text/html");
		}

	}

	public void populateSowingHavstBarChart() {
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<String> combineValues = new LinkedList<>();

		Integer totalFarmerCount = farmerService.findTotalFarmerCount(selectedBranch, selectedStapleLen, selectedSeason,
				selectedGender, selectedState);
		combineValues.add("donutChart.farmer-" + totalFarmerCount);

		combineValues.add("donutChart.preSowing-" + totalFarmerCount);

		Integer farmerPreHarvest = farmerService.findFarmerPreHarvestCount(selectedBranch,
				!StringUtil.isEmpty(selectedYear) ? Integer.parseInt(selectedYear) : 0, selectedSeason, selectedGender,
				selectedState);
		combineValues.add("donutChart.preHavst-" + farmerPreHarvest);

		Integer farmerPostHarvest = farmerService.findFarmerPostHarvestCount(selectedBranch,
				!StringUtil.isEmpty(selectedYear) ? Integer.parseInt(selectedYear) : 0, selectedSeason, selectedGender,
				selectedState);
		combineValues.add("donutChart.postHavst-" + farmerPostHarvest);

		combineValues.stream().forEach(obj -> {
			JSONObject jsonObject = new JSONObject();
			String value = (String) obj;
			String[] split = value.split("-");
			jsonObject.put("item", getLocaleProperty(split[0]));
			jsonObject.put("values", Double.valueOf(split[1]));
			jsonObjects.add(jsonObject);
		});

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("label", getLocaleProperty("farmerSowingHarvestChart"));
		jsonObject.put("save", getLocaleProperty("save"));
		jsonObject.put("refresh", getLocaleProperty("refresh"));
		jsonObjects.add(jsonObject);
		printAjaxResponse(jsonObjects, "text/html");

	}

	public void populateAreaProdByOrgBarChart() {

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<Object[]> totalAreaObj = farmerService.findTotalAreaProdByOrg(selectedSeason, selectedGender,
				selectedState, getBranchId());

		totalAreaObj.stream().forEach(obj -> {
			JSONObject jsonObject = new JSONObject();
			Object[] value = (Object[]) obj;
			jsonObject.put("item", String.valueOf(value[0]));
			jsonObject.put("values", !ObjectUtil.isEmpty(value[1]) && value[1] != "null"
					? formatter.format(Double.valueOf(String.valueOf(value[1]))) : "");
			jsonObjects.add(jsonObject);
		});

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("label", getLocaleProperty("areaUnderProd"));
		jsonObject.put("save", getLocaleProperty("save"));
		jsonObject.put("refresh", getLocaleProperty("refresh"));
		jsonObjects.add(jsonObject);
		printAjaxResponse(jsonObjects, "text/html");
	}

	public void populateGinnerQtyBarChart() {

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();

		List<Object[]> totalGinnerQtyObj = farmerService.findTotalGinnerQty(selectedSeason, selectedBranch);

		totalGinnerQtyObj.stream().forEach(obj -> {
			JSONObject jsonObject = new JSONObject();
			Object[] objArr = (Object[]) obj;
			jsonObject.put("item", objArr[0]);
			jsonObject.put("values", objArr[1]);
			jsonObject.put("address", objArr[2]);
			jsonObject.put("branchId", !StringUtil.isEmpty(objArr[3]) ? objArr[3] : "");
			jsonObjects.add(jsonObject);
		});

		if (totalGinnerQtyObj.size() > 0) {
			JSONObject jsonObject1 = new JSONObject();
			jsonObject1.put("label", getLocaleProperty("ginnerQtySold"));
			jsonObjects.add(jsonObject1);

			JSONObject jsonObject2 = new JSONObject();
			jsonObject2.put("save", getLocaleProperty("save"));
			jsonObjects.add(jsonObject2);
			JSONObject jsonObject3 = new JSONObject();
			jsonObject3.put("refresh", getLocaleProperty("refresh"));
			jsonObjects.add(jsonObject3);
		}

		printAjaxResponse(jsonObjects, "text/html");

	}

	double totPercentage = 0.0;

	public void populateGMOBarChart() {

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<Object[]> totalGmoPercentObj = farmerService.findGmoPercentage(selectedSeason, selectedBranch);

		totalGmoPercentObj.stream().forEach(obj -> {
			JSONObject jsonObject = new JSONObject();
			Object[] value = (Object[]) obj;
			jsonObject.put("item", String.valueOf(getText("gmoType" + value[0])));
			if (StringUtil.isEmpty(getBranchId()) && StringUtil.isEmpty(selectedBranch)) {
				totPercentage = (Double.valueOf(String.valueOf(value[3])) / Double.valueOf(String.valueOf(value[2])))
						* 100;
				jsonObject.put("values", formatter.format(totPercentage));
			} else {
				jsonObject.put("values", formatter.format(Double.valueOf(String.valueOf(value[1]))));
			}
			jsonObject.put("percentage", 100);
			jsonObjects.add(jsonObject);
		});

		if (totalGmoPercentObj.size() > 0) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("label", getLocaleProperty("gmoBarCharts"));
			jsonObject.put("save", getLocaleProperty("save"));
			jsonObject.put("refresh", getLocaleProperty("refresh"));
			jsonObjects.add(jsonObject);
		}
		printAjaxResponse(jsonObjects, "text/html");

	}

	public void populateAreaProdByIcsBarChart() {

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<Object[]> totalAreaIcsObj = farmerService.findTotalAreaProdByIcs(selectedSeason);

		totalAreaIcsObj.stream().forEach(obj -> {
			JSONObject jsonObject = new JSONObject();
			Object[] value = (Object[]) obj;
			jsonObject.put("item", String.valueOf(getText("icsStatus" + value[0])));
			jsonObject.put("values", formatter.format(Double.valueOf(String.valueOf(value[1]))));
			jsonObjects.add(jsonObject);
		});

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("label", getLocaleProperty("areaUnderProdByIcs"));
		jsonObject.put("save", getLocaleProperty("save"));
		jsonObject.put("refresh", getLocaleProperty("refresh"));
		jsonObjects.add(jsonObject);
		printAjaxResponse(jsonObjects, "text/html");
	}

	public String getCurrencyType() {
		String result = null;
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			result = preferences.getPreferences().get(ESESystem.CURRENCY_TYPE);
		}

		return !StringUtil.isEmpty(result) ? result : ESESystem.CURRENCY_TYPE;

	}
	
	public String getAreaType() {
	
			String result = null;
			ESESystem preferences = preferncesService.findPrefernceById("1");
			if (!StringUtil.isEmpty(preferences) && preferences.getPreferences().get(ESESystem.AREA_TYPE) != null) {
				result = preferences.getPreferences().get(ESESystem.AREA_TYPE);
				result = result.contains("-") ? result.split("-")[1] : result;

			}
			return !StringUtil.isEmpty(result) ? result : ESESystem.AREA_TYPE;

	
	}

	List<String> farmCodesList = null;

	public void populateCottonPriceBarChart() {

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();

		if (StringUtil.isEmpty(getBranchId())) {
			List<String> branchList = clientService.findBranchList();
			if (!ObjectUtil.isListEmpty(branchList)) {
				for (String branch : branchList) {
					farmCodesList = farmerService.findFarmCodsByStapleLen(selectedSeason, selectedStapleLen, branch);

					if (farmCodesList.size() > 0) {

						List<Object[]> priceObj = farmerService.findSalePriceByFarmCodes(farmCodesList);
						if (!ObjectUtil.isListEmpty(priceObj)) {
							priceObj.stream().forEach(obj -> {
								JSONObject jsonObject = new JSONObject();
								Object[] value = (Object[]) obj;
								Double price = Double.valueOf(String.valueOf(value[0])) / farmCodesList.size();
								jsonObject.put("price", formatter.format(price));
								jsonObject.put("item", String.valueOf(value[1]));
								jsonObjects.add(jsonObject);
							});
						}
					} else {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("item", branch);
						jsonObject.put("price", "0");
						jsonObjects.add(jsonObject);
					}

				}
			}
		} else {
			farmCodesList = farmerService.findFarmCodsByStapleLen(selectedSeason, selectedStapleLen, getBranchId());
			if (farmCodesList.size() > 0) {
				List<Object[]> priceObj = farmerService.findSalePriceByFarmCodes(farmCodesList);

				if (!ObjectUtil.isListEmpty(priceObj)) {
					priceObj.stream().forEach(obj -> {
						JSONObject jsonObject = new JSONObject();
						Object[] value = (Object[]) obj;
						Double price = Double.valueOf(String.valueOf(value[0])) / farmCodesList.size();
						jsonObject.put("price", formatter.format(price));
						jsonObject.put("item", String.valueOf(value[1]));
						jsonObjects.add(jsonObject);
					});
				}
			} else {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("item", getBranchId());
				jsonObject.put("price", "0");
				jsonObjects.add(jsonObject);
			}
		}
		String msp = farmerService.findMspByStapleLen(selectedSeason, selectedStapleLen);
		JSONObject jsonObjectMsp = new JSONObject();
		jsonObjectMsp.put("msp", formatter.format(!StringUtil.isEmpty(msp) ? Double.valueOf(msp) : 0.0));
		jsonObjects.add(jsonObjectMsp);

		/*
		 * if(ObjectUtil.isListEmpty(farmCodesList)) {
		 * 
		 * if (StringUtil.isEmpty(getBranchId())) { List<String> branchList =
		 * clientService.findBranchList(); if
		 * (!ObjectUtil.isListEmpty(branchList)) { for (String branch :
		 * branchList) { JSONObject jsonObjectBranch = new JSONObject();
		 * jsonObjectBranch.put("item", branch);
		 * jsonObjects.add(jsonObjectBranch); }
		 * 
		 * } } else { JSONObject jsonObjectBranch = new JSONObject();
		 * jsonObjectBranch.put("item", getBranchId());
		 * jsonObjects.add(jsonObjectBranch); }
		 * 
		 * 
		 * }
		 */

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("label", getLocaleProperty("cottonPriceLabel"));
		jsonObject.put("save", getLocaleProperty("save"));
		jsonObject.put("refresh", getLocaleProperty("refresh"));
		jsonObjects.add(jsonObject);
		printAjaxResponse(jsonObjects, "text/html");
	}

	String catalougeValues = "";

	public String getCatalouge(String code) {

		if (!StringUtil.isEmpty(code)) {
			// String[] codes = code.split(",");
			FarmCatalogue catalogue = getCatlogueValueByCode(code);
			if (!ObjectUtil.isEmpty(catalogue)) {
				catalougeValues = catalogue.getName();
			}
		}

		return !StringUtil.isEmpty(catalougeValues) ? StringUtil.removeLastComma(catalougeValues) : "";
	}

	public String getTypez() {
		return typez;
	}

	public void setTypez(String typez) {
		this.typez = typez;
	}

	public String getCurrentSeason() {

		String val = "";
		return val = clientService.findCurrentSeasonCodeByBranchId(getBranchId());
	}
	public void populateFinancialYearList() {
		JSONArray fYearArr = new JSONArray();
		int startYear=Integer.valueOf(getLocaleProperty("financialStartYear"));
		int currentMonth=DateUtil.getCurrentMonth();
		int currentYear=currentMonth<=3?DateUtil.getCurrentYear()-1:DateUtil.getCurrentYear();
		for(int i=startYear;i<=currentYear;i++){
			//System.out.println(i+"-"+(i+1));
			fYearArr.add(getJSONObject(String.valueOf(i), String.valueOf(i+"-"+(i+1))));
		}
		sendAjaxResponse(fYearArr);
	}

		
	public void populateSeasonList() {

		JSONArray seasonArr = new JSONArray();
		List<Object[]> seasonList = farmerService.listSeasonCodeAndName();
		if (!ObjectUtil.isEmpty(seasonList)) {
			seasonList.forEach(obj -> {
				seasonArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(seasonArr);
	}

	public void populateBranchList() {
		JSONArray branchArr = new JSONArray();
		List<Object[]> branchList = getBranchesInfo();
		if (!ObjectUtil.isEmpty(branchList)) {
			branchList.forEach(obj -> {
				branchArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(branchArr);
	}

	public void populateStateList() {
		List<State> stateList = locationService.listOfStatesByBranch(selectedBranch);
		JSONArray stateArr = new JSONArray();
		if (!ObjectUtil.isListEmpty(stateList)) {
			stateList.forEach(obj -> {
				/*
				 * if (StringUtil.isEmpty(getBranchId())) {
				 * stateArr.add(getJSONObject(obj.getName(), obj.getName())); }
				 * else {
				 */
				stateArr.add(getJSONObject((int) obj.getId(), obj.getName()));
				// }

			});
		}
		sendAjaxResponse(stateArr);
	}

	public void populateProcurementProductList() {
		List<ProcurementProduct> procurementProductList = productDistributionService.listProcurementProduct();
		JSONArray procurementProductArr = new JSONArray();
		if (!ObjectUtil.isListEmpty(procurementProductList)) {
			procurementProductList.forEach(obj -> {
				procurementProductArr.add(getJSONObject(obj.getCode(), obj.getName()));
			});
		}
		sendAjaxResponse(procurementProductArr);
	}

	public void populateSamithiList() {
		List<Warehouse> samithiList = locationService.listSamithiesBasedOnType();
		JSONArray samithiArr = new JSONArray();
		if (!ObjectUtil.isListEmpty(samithiList)) {
			samithiList.forEach(obj -> {
				samithiArr.add(getJSONObject(obj.getCode(), obj.getName()));
			});
		}
		sendAjaxResponse(samithiArr);
	}

	public void populateWarehouseList() {
		List<Warehouse> warehouseList = locationService.listWarehouse();
		JSONArray warehouseArr = new JSONArray();
		if (!ObjectUtil.isListEmpty(warehouseList)) {
			warehouseList.forEach(obj -> {
				warehouseArr.add(getJSONObject(obj.getId(), obj.getName()));
			});
		}
		sendAjaxResponse(warehouseArr);
	}

	public void populateCooperativeList() {
		JSONArray farmCatalougeArr = new JSONArray();
		if (!getCurrentTenantId().equals("chetna")) {
			List<FarmCatalogue> farmCatalougeList = catalogueService
					.findFarmCatalougeByType(Integer.valueOf(getText("cooperativeType")));
			Class clazz = FarmCatalogue.class;
			Field field = null;
			try {
				field = clazz.getDeclaredField(FarmCatalogue.NAME);
			} catch (NoSuchFieldException e1) {

			} catch (SecurityException e1) {

			}
			if (!ObjectUtil.isListEmpty(farmCatalougeList)) {
				farmCatalougeList.forEach(obj -> {
					farmCatalougeArr.add(getJSONObject(obj.getCode(), obj.getName()));
				});
			}
		} else if (getCurrentTenantId().equals("chetna")) {
			List<FarmCatalogue> farmCatalougeList = catalogueService
					.findFarmCatalougeByType(Integer.valueOf(getText("fpoType")));
			Class clazz = FarmCatalogue.class;
			Field field = null;
			try {

				field = clazz.getDeclaredField(FarmCatalogue.NAME);
			} catch (NoSuchFieldException e1) {

			} catch (SecurityException e1) {

			}
			if (!ObjectUtil.isListEmpty(farmCatalougeList)) {
				farmCatalougeList.forEach(obj -> {
					farmCatalougeArr.add(getJSONObject(obj.getCode(), obj.getName()));
				});
			}
		} else {
			List<Warehouse> farmCatalougeList = locationService.listOfSamithi();
			if (!ObjectUtil.isEmpty(farmCatalougeList)) {
				farmCatalougeList.forEach(obj -> {
					farmCatalougeArr.add(getJSONObject(obj.getCode(), obj.getName()));
				});
			}
		}
		sendAjaxResponse(farmCatalougeArr);

	}

	public void populateVillageList() {

		if (!StringUtil.isEmpty(selectedState) && selectedState != null) {
			List<Village> villList = locationService.listVillageByBranch(selectedBranch, Long.valueOf(selectedState));
			JSONArray villArr = new JSONArray();
			if (!ObjectUtil.isListEmpty(villList)) {
				villList.forEach(obj -> {
					/*
					 * if (StringUtil.isEmpty(getBranchId())) {
					 * villArr.add(getJSONObject(obj.getName(), obj.getName()));
					 * } else {
					 */
					villArr.add(getJSONObject((int) obj.getId(), obj.getName()));
					// }

				});
			}
			sendAjaxResponse(villArr);
		}

	}

	public void populateVarietylist() throws Exception {

		if (!selectedCrop.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedCrop))) {
			List<ProcurementVariety> listProcurementVariety = productDistributionService
					.findProcurementVariertyByCropCode(selectedCrop);

			JSONArray varietyArr = new JSONArray();
			if (!ObjectUtil.isListEmpty(listProcurementVariety)) {
				listProcurementVariety.forEach(procurementVariety -> {
					varietyArr.add(getJSONObject(procurementVariety.getCode(),
							procurementVariety.getCode() + " - " + procurementVariety.getName()));

				});
			}
			sendAjaxResponse(varietyArr);
		}
	}

	public String getSelectedVariety() {
		return selectedVariety;
	}

	public void setSelectedVariety(String selectedVariety) {
		this.selectedVariety = selectedVariety;
	}

	double totalAmt = 0.0;
	double totalQty = 0.0;
	double totPayment = 0.0;

	@SuppressWarnings("unchecked")
	public void populateProcurementCharts() {
		Date sDate = null;
		Date eDate = null;
		if (!StringUtil.isEmpty(getDateRange())) {
			String[] dateSplit = getDateRange().split("-");
			 sDate = DateUtil.convertStringToDate(dateSplit[0] + " 00:00:00", DateUtil.DATE_TIME_FORMAT);
			 eDate = DateUtil.convertStringToDate(dateSplit[1] + " 23:59:59", DateUtil.DATE_TIME_FORMAT);
		}
		String currency= getCurrencyType();
		List<Object[]> procurementDatas = new ArrayList<>();
		if(!getCurrentTenantId().equalsIgnoreCase("livelihood")){
			procurementDatas = farmerService.findAmtAndQtyByProcurment(selectedBranch, selectedState,
					selectedSeason,sDate,eDate);
		}else{
			procurementDatas = farmerService.findAmtAndQtyByProcurmentTraceability(selectedBranch, selectedState,
					selectedSeason,sDate,eDate);
		}
		
		List<JSONObject> procurementQtyJsonList = new ArrayList<>();
		List<JSONObject> procurementTotAmtJsonList = new ArrayList<>();
		List<JSONObject> procurementDataJsonList = new ArrayList<>();
		List<JSONObject> procurementPaymtJsonList = new ArrayList<>();
		List<List<JSONObject>> jsonObjectsList = new ArrayList<>();
		if (!ObjectUtil.isListEmpty(procurementDatas)) {
			procurementDatas.stream().forEach(obj -> {
				Object[] objectArr = (Object[]) obj;
				JSONObject qtyJsonObject = new JSONObject();
				JSONObject amtJsonObject = new JSONObject();
				JSONObject paymntJsonObject = new JSONObject();
				qtyJsonObject.put("productName", objectArr[0]);
				qtyJsonObject.put("values", objectArr[1]);
				qtyJsonObject.put("totalQty", "Total Quantity");

				amtJsonObject.put("productName", objectArr[0]);
				amtJsonObject.put("values", objectArr[2]);
				amtJsonObject.put("totalAmt", "Total Amount");

				paymntJsonObject.put("productName", objectArr[0]);
				paymntJsonObject.put("values", objectArr[3]);
				paymntJsonObject.put("totalPay", "Total Payment");

				totalAmt += Double.valueOf(String.valueOf(objectArr[2]));
				totalQty += Double.valueOf(String.valueOf(objectArr[1]));
				totPayment += Double.valueOf(String.valueOf(objectArr[3]));
				procurementQtyJsonList.add(qtyJsonObject);
				procurementTotAmtJsonList.add(amtJsonObject);
				procurementPaymtJsonList.add(paymntJsonObject);
			});

			String items[] = { "Total Amount", "Total Quantity", "Total Payment" };
			Double values[] = { totalAmt, totalQty, totPayment };
			for (int i = 0; i < values.length; i++) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("items", items[i]);
				jsonObject.put("values", values[i]);
				jsonObject.put("units", currency);
				procurementDataJsonList.add(jsonObject);
			}

			jsonObjectsList.add(procurementDataJsonList);
			jsonObjectsList.add(procurementQtyJsonList);
			jsonObjectsList.add(procurementTotAmtJsonList);
			jsonObjectsList.add(procurementPaymtJsonList);
		}
		printAjaxResponse(jsonObjectsList, "text/html");
	}
	
	public void warehouseToMobileUserChart(){
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		List<Object[]> chartData = farmerService.warehouseToMobileUserChart(getSelectedBranch());
		if(!ObjectUtil.isListEmpty(chartData)){
			chartData.stream().forEach(obj -> {
				Object[] objArr = (Object[]) obj;
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("servicePointId", objArr[0]);
				jsonObj.put("servicePointName", objArr[1]);
				jsonObj.put("amount", objArr[2]);
				jsonList.add(jsonObj);
			});

		}
				printAjaxResponse(jsonList, "text/html");
	}
	
	public void warehouseToMobileUser_AgentChart(){
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		List<Object[]> chartData = farmerService.warehouseToMobileUser_AgentChart(getSelectedBranch(),getSelectedWarehouse());
		
		chartData.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("agentId", objArr[0]);
			jsonObj.put("agentName", objArr[1]);
			jsonObj.put("amount", objArr[2]);
			jsonList.add(jsonObj);
		});
		printAjaxResponse(jsonList, "text/html");
	}
	
	public void warehouseToMobileUser_ProductChart(){
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		List<Object[]> chartData = farmerService.warehouseToMobileUser_ProductChart(getSelectedBranch(),getSelectedWarehouse(),getAgentId());
		
		chartData.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("productName", objArr[0]);
			jsonObj.put("amount", objArr[1]);
			jsonObj.put("quantity", objArr[2]);
			jsonObj.put("unit", objArr[3]);
			jsonList.add(jsonObj);
		});
		printAjaxResponse(jsonList, "text/html");
	}
	
	public void populateMobileUserToFarmer_AgentChart(){
		//if(!StringUtil.isEmpty(selectedSeason) && selectedSeason != "" ){}
		List<JSONObject> warehouseList = new ArrayList<JSONObject>();
		List<Object[]> chartData;
		if(getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
		chartData = farmerService.populateWarehouseMobileUserToFarmer_AgentChart(getSelectedBranch(),selectedSeason,selectedFinYear);}else{
		chartData = farmerService.populateWarehouseMobileUserToFarmer_AgentChart(getSelectedBranch(),selectedSeason);}
		chartData.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("warehouseId", objArr[0]);
			jsonObj.put("warehouseName", objArr[1]);
			jsonObj.put("warehouseCode", objArr[2]);
			jsonObj.put("amount", objArr[3]);
			warehouseList.add(jsonObj);
		});
		List<JSONObject> agentList = new ArrayList<JSONObject>();
		List<Object[]> agentData = farmerService.populateMobileUserToFarmer_AgentChart(getSelectedBranch(),selectedSeason);
		agentData.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("agentId", objArr[0]);
			jsonObj.put("agentName", objArr[1]);
			jsonObj.put("amount", objArr[2]);
			jsonObj.put("warehouseCode", objArr[3]);
			agentList.add(jsonObj);
		});
		JSONObject jsonList = new JSONObject();
		jsonList.put("warehouse", warehouseList);
		jsonList.put("mobileUser", agentList);
		List<JSONObject> populateMobileUserToFarmer = new ArrayList<JSONObject>();
		populateMobileUserToFarmer.add(jsonList);
		printAjaxResponse(populateMobileUserToFarmer, "text/html");
	}
	
	public void populateMobileUserToFarmer_FarmerChart(){
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		List<Object[]> chartData = farmerService.populateMobileUserToFarmer_FarmerChart(getSelectedBranch(),getAgentId());
		
		chartData.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("farmerId", objArr[0]);
			jsonObj.put("farmerName", objArr[1]);
			jsonObj.put("amount", objArr[2]);
			jsonList.add(jsonObj);
		});
		printAjaxResponse(jsonList, "text/html");
	}
	
	public void populateMobileUserToFarmer_ProductChart(){
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		List<Object[]> chartData = farmerService.populateMobileUserToFarmer_ProductChart(getSelectedBranch(),getAgentId(),getFarmerId());
		
		chartData.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("productNAme", objArr[0]);
			jsonObj.put("productCode", objArr[1]);
			jsonObj.put("amount", objArr[2]);
			jsonList.add(jsonObj);
		});
		printAjaxResponse(jsonList, "text/html");
	}
	
	public void populateWarehouseToFarmer_WarehouseChart(){
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		List<Object[]> chartData = farmerService.populateWarehouseToFarmer_WarehouseChart(getSelectedBranch());
		
		chartData.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("servicePointId", objArr[0]);
			jsonObj.put("servicePointName", objArr[1]);
			jsonObj.put("amount", objArr[2]);
			jsonList.add(jsonObj);
		});
		printAjaxResponse(jsonList, "text/html");
	}
	
	public void populateWarehouseToFarmer_FarmerChart(){
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		List<Object[]> chartData = farmerService.populateWarehouseToFarmer_FarmerChart(getSelectedBranch(),getSelectedWarehouse());
		
		chartData.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("farmerId", objArr[0]);
			jsonObj.put("farmerName", objArr[1]);
			jsonObj.put("amount", objArr[2]);
			jsonList.add(jsonObj);
		});
		printAjaxResponse(jsonList, "text/html");
	}
	
	public void populateWarehouseToFarmer_ProductChart(){
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		List<Object[]> chartData = farmerService.populateWarehouseToFarmer_ProductChart(getSelectedBranch(),getSelectedWarehouse(),getFarmerId());
		
		chartData.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("productName", objArr[0]);
			jsonObj.put("productCode", objArr[1]);
			jsonObj.put("amount", objArr[2]);
			jsonObj.put("quantity", objArr[3]);
			jsonObj.put("unit", objArr[4]);
			jsonList.add(jsonObj);
		});
		printAjaxResponse(jsonList, "text/html");
	}
	
	public void productChartByWarehouseToMobileUser(){
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		List<Object[]> chartData = farmerService.productChartByWarehouseToMobileUser(getSelectedBranch(),getSelectedWarehouse());
		
		chartData.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("productName", objArr[0]);
			jsonObj.put("amount", objArr[1]);
			jsonObj.put("quantity", objArr[2]);
			jsonObj.put("unit", objArr[3]);
			jsonList.add(jsonObj);
		});
		printAjaxResponse(jsonList, "text/html");
	}
	
	public void productChartByMobileUserToFarmer(){
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		List<Object[]> chartData;
		if(getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
		chartData = farmerService.productChartByMobileUserToFarmer(getSelectedBranch(),getAgentId(),selectedSeason,selectedFinYear);
		}else{
	   chartData = farmerService.productChartByMobileUserToFarmer(getSelectedBranch(),getAgentId(),selectedSeason);
		}
		chartData.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("productNAme", objArr[0]);
			jsonObj.put("productCode", objArr[1]);
			jsonObj.put("amount", objArr[2]);
			jsonObj.put("quantity", objArr[3]);
			jsonObj.put("unit", objArr[4]);
			jsonList.add(jsonObj);
		});
		printAjaxResponse(jsonList, "text/html");
	}
	
	public void productChartByWarehouseToFarmer(){
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		List<Object[]> chartData = farmerService.productChartByWarehouseToFarmer(getSelectedBranch(),getSelectedWarehouse());
		
		chartData.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("productName", objArr[0]);
			jsonObj.put("productCode", objArr[1]);
			jsonObj.put("amount", objArr[2]);
			jsonObj.put("quantity", objArr[3]);
			jsonObj.put("unit", objArr[4]);
			jsonList.add(jsonObj);
		});
		printAjaxResponse(jsonList, "text/html");
	}
	
	public void populateStatusList() {
		JSONArray warehouseArr = new JSONArray();
		Map<Integer, String> statusList = new LinkedHashMap<Integer, String>();
		statusList.put(Farmer.Status.ACTIVE.ordinal(), getText("status" + Farmer.Status.ACTIVE.ordinal()));
		statusList.put(Farmer.Status.INACTIVE.ordinal(), getText("status" + Farmer.Status.INACTIVE.ordinal()));
		
		statusList.forEach((k,v)->{
			warehouseArr.add(getJSONObject(String.valueOf( k), v));
		});
	
		sendAjaxResponse(warehouseArr);
	}
	
	
	@SuppressWarnings("unchecked")
	public void populateSowingSegregate() {
		List<JSONObject> plottingAreaBySowing = new ArrayList<JSONObject>();
		List<JSONObject> branch = new ArrayList<JSONObject>();
	
		List<Object[]> plottingAreaByBranch;
		if(!StringUtil.isEmpty(selectedSeason) && selectedSeason != "" ){
	 plottingAreaByBranch = farmerService.plottingAreaByBranch(selectedSeason);
		}else{
			 plottingAreaByBranch = farmerService.plottingAreaByBranch();
		}
		plottingAreaByBranch.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("acres", objArr[0]);
			jsonObj.put("branchName", objArr[1]);
			jsonObj.put("branchId", objArr[1]);
			jsonObj.put("count", objArr[2]);
			branch.add(jsonObj);
		});
		
		List<Object[]> plottingByWarehouse;
		if(getIsMultiBranch().equalsIgnoreCase("1") && getIsParentBranch().equalsIgnoreCase("1")){
			plottingByWarehouse = farmerService.plottingByWarehouse(EMPTY);
		}else{
			plottingByWarehouse = farmerService.plottingByWarehouse(getSelectedBranch());
		}
			List<JSONObject> warehouse = new ArrayList<JSONObject>();
			plottingByWarehouse.stream().forEach(obj -> {
				Object[] objArr = (Object[]) obj;
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("acres", objArr[0]);
				jsonObj.put("warehouseName", objArr[1]);
				jsonObj.put("warehouseCode", objArr[2]);
				jsonObj.put("branchId", objArr[3]);
				jsonObj.put("profileId", objArr[2]);
				jsonObj.put("count", objArr[4]);
				warehouse.add(jsonObj);
			});
			
			
			List<Object[]> plottingByMobileUser;
			if(getIsMultiBranch().equalsIgnoreCase("1") && getIsParentBranch().equalsIgnoreCase("1")){
				plottingByMobileUser = farmerService.plottingByMobileUser(EMPTY);
			}else{
				plottingByMobileUser = farmerService.plottingByMobileUser(getSelectedBranch());
			}
				List<JSONObject> mobileUser = new ArrayList<JSONObject>();
				plottingByMobileUser.stream().forEach(obj -> {
					Object[] objArr = (Object[]) obj;
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("acres", objArr[0]);
					jsonObj.put("mobileUserName", objArr[1]);
					jsonObj.put("mobileUserCode", objArr[2]);
					jsonObj.put("branchId", objArr[3]);
					jsonObj.put("count", objArr[4]);
					jsonObj.put("warehouseCode", objArr[5]);
					mobileUser.add(jsonObj);
				});

			
		
			JSONObject final_jsonObject = new JSONObject();
			if (StringUtil.isEmpty(getSelectedBranch())) {
				final_jsonObject.put("branch", branch);
			}
			final_jsonObject.put("warehouse", warehouse);
			final_jsonObject.put("mobileUser", mobileUser);
			plottingAreaBySowing.add(final_jsonObject);

			printAjaxResponse(plottingAreaBySowing, "text/html");
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

}
