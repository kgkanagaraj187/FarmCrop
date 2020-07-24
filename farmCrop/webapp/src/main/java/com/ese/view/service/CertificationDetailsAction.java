/* * FarmCropsAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.profile.CropHarvest;
import com.ese.entity.profile.CropSupply;
import com.ese.entity.util.ESESystem;
import com.opensymphony.xwork2.ActionContext;
import com.sourcetrace.eses.BreadCrumb;
import com.sourcetrace.eses.entity.DynamicFeildMenuConfig;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.FarmCatalogueMaster;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IExporter;
import com.sourcetrace.eses.service.IFarmCropsService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Coordinates;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.FarmCropsCoordinates;
import com.sourcetrace.esesw.entity.profile.FarmCropsMaster;
import com.sourcetrace.esesw.entity.profile.FarmDetailedInfo;
import com.sourcetrace.esesw.entity.profile.FarmIcsConversion;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.FarmCropsCoordinates.typesOFCordinates;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

// TODO: Auto-generated Javadoc
/**
 * The Class FarmCropsAction.
 */
public class CertificationDetailsAction  extends BaseReportAction implements IExporter{
	private static final long serialVersionUID = -9191437334851144561L;
	public static final String FARMER_DETAIL = "farmerDetail";
	public static final String FARM_DETAIL = "farmDetail";
	public static final int SELECT = -1;
	public static final int DYNAMIC_SEED_TYPE = 7;
	private String id;
	private FarmCrops farmCrops;
	private IFarmCropsService farmCropsService;
	@Autowired
	private IFarmerService farmerService;
	private String tabIndex = "#tabs-1";
	private String tabIndexz = "#tabs-3";
	private String farmerId;
	private String farmerName;
	private String farmId;
	private String farmName;
	private String selectedFarmCropsMaster;
	private String selectedFarm;
	private List<HarvestSeason> cropSeasons = new ArrayList<HarvestSeason>();
	private IProductDistributionService productDistributionService;
	private String selectedCrop;
	private String selectedVariety;
	List<ProcurementVariety> listProcurementVariety = new ArrayList<ProcurementVariety>();
	List<FarmCatalogue> seedTreatmentList = new ArrayList<FarmCatalogue>();
	// private Map<Integer, String> seedTreatmentDetailsMap = new
	// LinkedHashMap<Integer, String>();
	private FarmCatalogue farmCatalogue;
	private String selectedSeedTreatment;
	private String cropSeasonCode;
	private String sowingDate;
	private String harvestDate;
	DateFormat df = new SimpleDateFormat(getESEDateFormat());
	@Autowired
	private IClientService clientService;
	/** The crop categories. */
	private Map<Integer, String> cropCategories = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> cropNameList = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> listSeedSource = new LinkedHashMap<Integer, String>();
	private Map<String, String> listType = new LinkedHashMap<String, String>();
	private Map<Integer, String> riskAssType = new LinkedHashMap<Integer, String>();
	private String riskAssesment;
	private int bufferZone;
	private String seedSourceDetail;
	private String cropDetail;
	private String varietyDetail;
	@Autowired
	private IProductService productService;
	@Autowired
	private IPreferencesService preferncesService;
	@Autowired
	private ICatalogueService catalogueService;
	@Autowired
	private IUniqueIDGenerator idGenerator;
	private String tabFarmCropIndex = "tabs-3";
	private Map<Integer, String> cropCategoryList;
	private String selectedCropCategoryList;
	String[] cropCategory;
	private List<JSONObject> jsonObjectList;
	private List<JSONObject> jsonFarmObjectList;
	public static final int OTHER = 99;
	private String farmerUniqueId;
	private String farmerfarmName;
	private String cropInfoEnabled;
	private String lat;
	private String lon;
	private Map<String, String> stableLenMap = new LinkedHashMap<String, String>();
	private Map<Integer, Integer> yearList = new LinkedHashMap<Integer, Integer>();
	
	private String cultiVal;
	private String totalLandHoldVal;
	private FarmIcsConversion filter;

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {

		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {

		this.id = id;
	}

	/**
	 * Gets the farm crops.
	 * 
	 * @return the farm crops
	 */
	public FarmCrops getFarmCrops() {

		return farmCrops;
	}

	/**
	 * Sets the farm crops.
	 * 
	 * @param farmCrops
	 *            the new farm crops
	 */
	public void setFarmCrops(FarmCrops farmCrops) {

		this.farmCrops = farmCrops;
	}

	/**
	 * Sets the farm crops service.
	 * 
	 * @param farmCropsService
	 *            the new farm crops service
	 */
	public void setFarmCropsService(IFarmCropsService farmCropsService) {

		this.farmCropsService = farmCropsService;
	}

	public void setProductDistributionService(IProductDistributionService productDistributionService) {

		this.productDistributionService = productDistributionService;
	}

	public String list() {
		Calendar currentDate = Calendar.getInstance();
		Calendar cal = (Calendar) currentDate.clone();
		cal.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) - 1);
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		super.startDate = df.format(cal.getTime());
		super.endDate = df.format(currentDate.getTime());

		
		return LIST;
	}
	
	@SuppressWarnings({ "unchecked" })
	public String detail() throws Exception {
		//String  farmerId =(String)request.getParameter("farmerId");
		Map<String, String> searchRecord = getJQGridRequestParam(); // get the
																	// search
																	// parameter
																	// with
		// value

		 filter = new FarmIcsConversion();

		if (!StringUtil.isEmpty(getFarmerId())) {
			request.getSession().setAttribute("farmerId", getFarmerId());
		} else if (!StringUtil.isEmpty(request.getSession().getAttribute("farmerId"))) {
			request.getSession().setAttribute("farmerId", request.getSession().getAttribute("farmerId"));
			setFarmerId(String.valueOf(request.getSession().getAttribute("farmerId")));
		}
		
		if (!StringUtil.isEmpty(farmerId)) {
			if (!ObjectUtil.isEmpty(filter.getFarmer())) {
				filter.getFarmer().setId(Long.parseLong(farmerId));
			} else {
				Farmer farmer = farmerService.findFarmerById(Long.valueOf(getFarmerId()));
				if(!ObjectUtil.isEmpty(farmer)){
					filter.setFarmer(farmer);
				}
						
			}
		}
	
		super.filter = this.filter;
		Map data = readData();
		return sendJSONResponse(data);
	}

	
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		FarmIcsConversion farmIcs = (FarmIcsConversion) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		rows.add(!ObjectUtil.isEmpty(farmIcs) && !ObjectUtil.isEmpty(farmIcs.getFarm()) ? farmIcs.getFarm().getFarmName():"");
		if(!ObjectUtil.isEmpty(farmIcs) && !StringUtil.isEmpty(farmIcs.getSeason())){
			HarvestSeason season = getHarvestSeason(farmIcs.getSeason());
			rows.add(!ObjectUtil.isEmpty(season) ? season.getName() : "");
		}else{
			rows.add("");
		}
		if(!ObjectUtil.isEmpty(farmIcs) && !StringUtil.isEmpty(farmIcs.getInsType())){
			FarmCatalogue catalogue = getCatlogueValueByCode(farmIcs.getInsType());
			rows.add(!ObjectUtil.isEmpty(catalogue) ? catalogue.getName() : "");
		}else{
			rows.add("");
		}
		if(!ObjectUtil.isEmpty(farmIcs) && !StringUtil.isEmpty(farmIcs.getScope())){
			FarmCatalogue catalogue = getCatlogueValueByCode(farmIcs.getScope());
			rows.add(!ObjectUtil.isEmpty(catalogue) ? catalogue.getName() : "");
		}else{
			rows.add("");
		}
		if(!ObjectUtil.isEmpty(farmIcs) && !StringUtil.isEmpty(farmIcs.getIcsType())){
			if (farmIcs.getIcsType().equalsIgnoreCase("0")) {
				rows.add(getLocaleProperty("farm.ics1"));
			} else if (farmIcs.getIcsType().equalsIgnoreCase("1")) {
				rows.add(getLocaleProperty("farm.ics2"));
			} else if (farmIcs.getIcsType().equalsIgnoreCase("2")) {
				rows.add(getLocaleProperty("farm.ics3"));
			} else if (farmIcs.getIcsType().equalsIgnoreCase("3")) {
				rows.add(getLocaleProperty("farm.organic"));
			}

		}else{
			rows.add("");
		}
		
		rows.add(!ObjectUtil.isEmpty(farmIcs) && !StringUtil.isEmpty(farmIcs.getInspectorName())
				? farmIcs.getInspectorName() : "");
		
		SimpleDateFormat sf=new SimpleDateFormat("MM-dd-yyyy");
		rows.add(!ObjectUtil.isEmpty(farmIcs) && (!StringUtil.isEmpty(farmIcs.getInspectionDate()))
				? sf.format(farmIcs.getInspectionDate()) : "");
		rows.add(!ObjectUtil.isEmpty(farmIcs) && !ObjectUtil.isEmpty(farmIcs.getSanctionDuration()) ? farmIcs.getSanctionDuration():"");
		jsonObject.put("cell", rows);
		return jsonObject;
	}

	/**
	 * Creates the.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String create() throws Exception {

			return FARM_DETAIL;
	
	}

	
	

	

	/**
	 * Gets the list farm crops master.
	 * 
	 * @return the list farm crops master
	 */
	public Map<String, String> getListFarmCropsMaster() {

		Map<String, String> farmCropsMasterMap = new LinkedHashMap<String, String>();
		List<FarmCropsMaster> farmCropsMasterList = farmCropsService.listFarmCropsMaster();
		for (FarmCropsMaster farmCropsMaster : farmCropsMasterList) {
			farmCropsMasterMap.put(farmCropsMaster.getCode(),
					farmCropsMaster.getCode() + " - " + farmCropsMaster.getName());
		}
		return farmCropsMasterMap;
	}

	/**
	 * Gets the list farm.
	 * 
	 * @return the list farm
	 */
	public Map<String, String> getListFarm() {

		Map<String, String> farmMap = new LinkedHashMap<String, String>();
		if (!ObjectUtil.isEmpty(farmCrops) && !ObjectUtil.isEmpty(farmCrops.getFarm())
				&& !ObjectUtil.isEmpty(farmCrops.getFarm().getFarmer())) {
			List<Object[]> farmList = farmerService.listFarmFieldsByFarmerId(farmCrops.getFarm().getFarmer().getId());
			for (Object[] farm : farmList) {
				farmMap.put(farm[1].toString(), farm[1].toString() + " - " + farm[2].toString());
			}
		}
		return farmMap;
	}

	/**
	 * Populate farm by farmer id.
	 */
	public void populateFarmByFarmerId() {

		JSONObject jsonObject = new JSONObject();
		JSONArray farmArray = new JSONArray();
		int certificationType = 0;
		if (!StringUtil.isEmpty(farmerId)) {
			List<Object[]> farmList = farmerService.listFarmFieldsByFarmerId(Long.valueOf(farmerId));
			if (!ObjectUtil.isListEmpty(farmList)) {
				for (Object[] farm : farmList) {
						Farmer farmer =farmerService.findFarmerByFarmerId(farmerId.toString());
						if (!ObjectUtil.isEmpty(farmer))
						certificationType = farmer.getCertificationType();
					JSONObject farmObj = new JSONObject();
					farmObj.put("key", farm[1].toString());
					farmObj.put("value", farm[1].toString() + " - " + farm[2].toString());
					farmArray.add(farmObj);
				}
			}
		}
		jsonObject.put("certificationType", certificationType);
		jsonObject.put("farms", farmArray);
		printAjaxResponse(jsonObject, "text/html");
	}

	/*public Map<String, String> getFarmList() {
		return farmerService.listFarmByFarmerId(Long.valueOf(farmerId)).stream()
				.collect(Collectors.toMap(farm -> String.valueOf(farm.getId()), Farm::getFarmName));
	}*/
	public Map<Long, String> getFarmList() {

		Map<Long, String> farmlist = new HashMap<>();
		List<Object[]> farm = farmerService.listFarmFieldsByFarmerId(Long.valueOf(farmerId));
		for (Object[] farms : farm) {
			farmlist.put(Long.valueOf(farms[0].toString()), farms[2].toString());
		}
		return farmlist;

	}
	

	/**
	 * Form map.
	 * 
	 * @param keyProperty
	 *            the key property
	 * @return the map
	 */
	@SuppressWarnings("unchecked")
	private Map formMap(String keyProperty) {

		Map dataMap = new LinkedHashMap();
		String values = getText(keyProperty);
		if (!StringUtil.isEmpty(values)) {
			String[] valuesArray = values.split(",");
			int i = 1;
			for (String value : valuesArray) {
				dataMap.put(i++, value);
			}
		}
		return dataMap;
	}

	/**
	 * Gets the farmer list.
	 * 
	 * @return the farmer list
	 */
	public Map<String, String> getFarmerList() {

		Map<String, String> farmerMap = new LinkedHashMap<String, String>();
		List<Farmer> farmers = farmerService.listFarmer();
		for (Farmer farmer : farmers) {
			farmerMap.put(String.valueOf(farmer.getId()), farmer.getFirstName() + " - " + farmer.getFarmerId());
		}
		return farmerMap;
	}

	/**
	 * Gets the data.
	 * 
	 * @return the data
	 * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
	 */


	/**
	 * Gets the tab index.
	 * 
	 * @return the tab index
	 */
	public String getTabIndex() {

		return tabIndex;
	}

	/**
	 * Sets the tab index.
	 * 
	 * @param tabIndex
	 *            the new tab index
	 */
	public void setTabIndex(String tabIndex) {

		this.tabIndex = tabIndex;
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
	 * Gets the farm crops service.
	 * 
	 * @return the farm crops service
	 */
	public IFarmCropsService getFarmCropsService() {

		return farmCropsService;
	}

	/**
	 * Sets the selected farm crops master.
	 * 
	 * @param selectedFarmCropsMaster
	 *            the new selected farm crops master
	 */
	public void setSelectedFarmCropsMaster(String selectedFarmCropsMaster) {

		this.selectedFarmCropsMaster = selectedFarmCropsMaster;
	}

	/**
	 * Gets the selected farm crops master.
	 * 
	 * @return the selected farm crops master
	 */
	public String getSelectedFarmCropsMaster() {

		return selectedFarmCropsMaster;
	}

	/**
	 * Sets the selected farm.
	 * 
	 * @param selectedFarm
	 *            the new selected farm
	 */
	public void setSelectedFarm(String selectedFarm) {

		this.selectedFarm = selectedFarm;
	}

	/**
	 * Gets the selected farm.
	 * 
	 * @return the selected farm
	 */
	public String getSelectedFarm() {

		return selectedFarm;
	}

	/**
	 * Sets the farmer service.
	 * 
	 * @param farmerService
	 *            the new farmer service
	 */
	public void setFarmerService(IFarmerService farmerService) {

		this.farmerService = farmerService;
	}

	/**
	 * Gets the farmer service.
	 * 
	 * @return the farmer service
	 */
	public IFarmerService getFarmerService() {

		return farmerService;
	}

	/**
	 * Gets the farmer detail params.
	 * 
	 * @return the farmer detail params
	 */
	@SuppressWarnings("deprecation")
	public String getFarmerDetailParams() {

		return "tabIndex=" + URLEncoder.encode(tabIndexz) + "&id=" + getFarmerId() + "&" + tabIndexz;
	}

	/**
	 * Sets the farmer id to request attribute.
	 */
	private void setFarmerIdToRequestAttribute() {

		if (!StringUtil.isEmpty(getFarmerId())) {
			request.getSession().setAttribute("farmerId", getFarmerId());
		} else if (!StringUtil.isEmpty(request.getSession().getAttribute("farmerId"))) {
			request.getSession().setAttribute("farmerId", request.getSession().getAttribute("farmerId"));
			setFarmerId(String.valueOf(request.getSession().getAttribute("farmerId")));
		}
	}

	/*
	 * public Map<String, String> getListProcurementProduct() {
	 * 
	 * Map<String, String> farmCropsMap = new LinkedHashMap<String, String>();
	 * List<ProcurementProduct> cropsList =
	 * productDistributionService.listProcurementProduct(); for
	 * (ProcurementProduct farmCrop : cropsList) {
	 * farmCropsMap.put(farmCrop.getCode(), farmCrop.getName()); } return
	 * farmCropsMap; }
	 */

	public Map<String, String> getListProcurementProduct() {
		List<ProcurementProduct> cropsList = new ArrayList<ProcurementProduct>();
		Map<String, String> farmCropsMap = new LinkedHashMap<String, String>();
			cropsList = productDistributionService.listProcurementProduct();
		for (ProcurementProduct farmCrop : cropsList) {
			//farmCropsMap.put(farmCrop.getCode(), !StringUtil.isEmpty(getLocaleProperty(farmCrop.getCode())) ? getLocaleProperty(farmCrop.getCode()) : farmCrop.getName());
			String name = getLanguagePref(getLoggedInUserLanguage(), farmCrop.getCode().trim().toString());
			if(!StringUtil.isEmpty(name) && name != null){
				farmCropsMap.put(farmCrop.getCode().toString(), farmCrop.getCode().toString()+ "-" +name);
			}else{
				farmCropsMap.put(farmCrop.getCode(),farmCrop.getName());
			}
		}
		return farmCropsMap;
	}

	public Map<String, String> getListProcurementProductData() {

		Map<String, String> farmCropsCatMap = new LinkedHashMap<String, String>();
		List<ProcurementProduct> cropsListData = productDistributionService.listProcurementProductBasedOnCropCat();
		for (ProcurementProduct farmCrop : cropsListData) {
			farmCropsCatMap.put(farmCrop.getCode(), farmCrop.getName());
		}
		return farmCropsCatMap;
	}

	/**
	 * Sets the crop seasons.
	 * 
	 * @param cropSeasons
	 *            the crop seasons
	 */
	public void setCropSeasons(List<HarvestSeason> cropSeasons) {

		this.cropSeasons = cropSeasons;
	}

	public List<HarvestSeason> getCropSeasons() {

		return cropSeasons;
	}

	/**
	 * Gets the crop categories.
	 * 
	 * @return the crop categories
	 */
	public Map<Integer, String> getCropCategories() {

		return cropCategories;
	}

	/**
	 * Sets the crop categories.
	 * 
	 * @param cropCategories
	 *            the crop categories
	 */
	public void setCropCategories(Map<Integer, String> cropCategories) {

		this.cropCategories = cropCategories;
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
	 * Gets the farm id.
	 * 
	 * @return the farm id
	 */
	public String getFarmId() {

		return farmId;
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
	 * Gets the tab indexz.
	 * 
	 * @return the tab indexz
	 */
	public String getTabIndexz() {

		return tabIndexz;
	}

	/**
	 * Sets the tab indexz.
	 * 
	 * @param tabIndexz
	 *            the new tab indexz
	 */
	public void setTabIndexz(String tabIndexz) {

		this.tabIndexz = tabIndexz;
	}

	public Map<String, String> getListSeedSource() {

		Map<String, String> list = new LinkedHashMap<>();

		list = getFarmCatalougeMap(Integer.valueOf(getText("seedSourceType")));
		return list;

	}

	public void setListSeedSource(Map<Integer, String> listSeedSource) {

		this.listSeedSource = listSeedSource;
	}

	public String getSelectedCrop() {

		return selectedCrop;
	}

	public void setSelectedCrop(String selectedCrop) {

		this.selectedCrop = selectedCrop;
	}

	public List<ProcurementVariety> getListProcurementVariety() {

		return listProcurementVariety;
	}

	public void setListProcurementVariety(List<ProcurementVariety> listProcurementVariety) {

		this.listProcurementVariety = listProcurementVariety;
	}

	public void populateVariety() throws Exception {

		if (!selectedCrop.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedCrop))) {
			listProcurementVariety = productDistributionService.findProcurementVariertyByCropCode(selectedCrop);
		}
		JSONArray varietyArr = new JSONArray();
		if (!ObjectUtil.isEmpty(listProcurementVariety)) {
			for (ProcurementVariety procurementVariety : listProcurementVariety) {
				
				String name = getLanguagePref(getLoggedInUserLanguage(), procurementVariety.getCode().trim().toString());
				if(!StringUtil.isEmpty(name) && name != null){
					varietyArr.add(getJSONObject(procurementVariety.getCode().toString(), procurementVariety.getCode() + " - " +getLanguagePref(getLoggedInUserLanguage(), procurementVariety.getCode().toString())));
				}else{
					varietyArr.add(getJSONObject(procurementVariety.getCode(),procurementVariety.getCode() + " - " + procurementVariety.getName()));
				}
				
			}
		}
		sendAjaxResponse(varietyArr);
	}

	protected JSONObject getJSONObject(Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}

	public String getSelectedVariety() {

		return selectedVariety;
	}

	public void setSelectedVariety(String selectedVariety) {

		this.selectedVariety = selectedVariety;
	}

	public String getSeedSourceDetail() {

		return seedSourceDetail;
	}

	public void setSeedSourceDetail(String seedSourceDetail) {

		this.seedSourceDetail = seedSourceDetail;
	}

	public String getCropDetail() {

		return cropDetail;
	}

	public void setCropDetail(String cropDetail) {

		this.cropDetail = cropDetail;
	}

	public String getVarietyDetail() {

		return varietyDetail;
	}

	public void setVarietyDetail(String varietyDetail) {

		this.varietyDetail = varietyDetail;
	}

	public void prepare() throws Exception {

		if (!StringUtil.isEmpty(farmId)) {
			String farmUniqueId = (String) request.getParameter("farmId");
			String actionClassName = ActionContext.getContext().getActionInvocation().getAction().getClass()
					.getSimpleName();
			String content = getLocaleProperty(actionClassName + "." + BreadCrumb.BREADCRUMB);
			if (StringUtil.isEmpty(content)|| (content.equalsIgnoreCase(actionClassName + "." + BreadCrumb.BREADCRUMB))) {
				content = getText(BreadCrumb.BREADCRUMB, "");
			}
			
			request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(content+ farmUniqueId + "&" + getTabIndexz()));
		
		} else {
			String actionClassName = ActionContext.getContext().getActionInvocation().getAction().getClass()
					.getSimpleName();
			String content = getLocaleProperty(actionClassName + "." + BreadCrumb.BREADCRUMB);
			if (StringUtil.isEmpty(content)|| (content.equalsIgnoreCase(actionClassName + "." + BreadCrumb.BREADCRUMB))) {
				content = getText(BreadCrumb.BREADCRUMB, "");
			}
			request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(content + "&" + getTabIndexz()));
		}

	}

	public Map<String, String> getCropSeasonsMap() {
		Map<String, String> cropSeasonMap = new LinkedHashMap<String, String>();
		if (!StringUtil.isEmpty(selectedCrop)) {
			cropSeasons = farmerService.listHarvestSeasons();
		}
		for (HarvestSeason cropSeason : cropSeasons) {
			cropSeasonMap.put(cropSeason.getCode(), !StringUtil.isEmpty(getLocaleProperty(cropSeason.getCode())) ? getLocaleProperty(cropSeason.getCode()) :  cropSeason.getName());
			
		}
		return cropSeasonMap;
	}

	public Map<String, String> getListProcurementVarietyMap() {

		Map<String, String> procurementVarietyMap = new LinkedHashMap<String, String>();
		if (!StringUtil.isEmpty(selectedCrop)) {
			listProcurementVariety = productDistributionService.findProcurementVariertyByCropCode(selectedCrop);
		}
		for (ProcurementVariety procurementVariety : listProcurementVariety) {
			//procurementVarietyMap.put(procurementVariety.getCode(), procurementVariety.getName());
			procurementVarietyMap.put(procurementVariety.getCode(), !StringUtil.isEmpty(getLocaleProperty(procurementVariety.getCode())) ? getLocaleProperty(procurementVariety.getCode()) :  procurementVariety.getName());
			
		}
		return procurementVarietyMap;
	}

	public String getCropSeasonCode() {

		return cropSeasonCode;
	}

	public void setCropSeasonCode(String cropSeasonCode) {

		this.cropSeasonCode = cropSeasonCode;
	}

	public int getDefaultCropCategoryValue() {

		return (ObjectUtil.isEmpty(this.farmCrops) ? 0 : this.farmCrops.getCropCategory());
	}

	public Map<String, String> getListType() {

		Map<String, String> typeListMap = new LinkedHashMap<>();
	
		List<FarmCatalogue> cropMaster1 = catalogueService
				.listFarmCatalogueWithOther(Integer.valueOf(getText("cropTypeVal")), FarmCatalogue.OTHER);
		if (!ObjectUtil.isEmpty(cropMaster1) && cropMaster1 != null) {
			for (FarmCatalogue obj : cropMaster1) {
				//typeListMap.put(obj.getCode(), obj.getName());
				String name = getLanguagePref(getLoggedInUserLanguage(), obj.getCode().trim().toString());
				if(!StringUtil.isEmpty(name) && name != null){
					typeListMap.put(obj.getCode().toString(), obj.getCode().toString()+ "-" +getLanguagePref(getLoggedInUserLanguage(), obj.getCode().toString()));
				}else{
					typeListMap.put(obj.getCode(), obj.getName());
				}

			}

		}
		
		return typeListMap;
	}

	public void setListType(Map<String, String> listType) {

		this.listType = listType;
	}

	public String getEnableMultiProduct() {
		return preferncesService.findPrefernceByName(ESESystem.ENABLE_MULTI_PRODUCT);
	}

	/**
	 * Gets the current date.
	 * 
	 * @return the current date
	 */
	public String getCurrentDate() {
		Calendar currentDate = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT_2);
		return df.format(currentDate.getTime());
	}

	public String getSowingDate() {
		return sowingDate;
	}

	public void setSowingDate(String sowingDate) {
		this.sowingDate = sowingDate;
	}

	public String getHarvestDate() {
		return harvestDate;
	}

	public void setHarvestDate(String harvestDate) {
		this.harvestDate = harvestDate;
	}

	public String getTabFarmCropIndex() {
		return tabFarmCropIndex;
	}

	public void setTabFarmCropIndex(String tabFarmCropIndex) {
		this.tabFarmCropIndex = tabFarmCropIndex;
	}

	public String getDefaultRiskAssessmentValue() {

		return (ObjectUtil.isEmpty(this.farmCrops) ? "2" : this.farmCrops.getRiskAssesment());

	}

	public Map<Integer, String> getRiskAssType() {

		return riskAssType;
	}

	public void setRiskAssType(Map<Integer, String> riskAssType) {

		this.riskAssType = riskAssType;
	}

	public String getRiskAssesment() {

		return riskAssesment;
	}

	public void setRiskAssesment(String riskAssesment) {

		this.riskAssesment = riskAssesment;
	}

	public int getBufferZone() {

		return bufferZone;
	}

	public void setBufferZone(int bufferZone) {

		this.bufferZone = bufferZone;
	}

	public List<FarmCatalogue> getSeedTreatmentList() {

		seedTreatmentList = farmerService.listSeedTreatmentDetailsBasedOnType();

		return seedTreatmentList;
	}

	public void setSeedTreatmentList(List<FarmCatalogue> seedTreatmentList) {

		this.seedTreatmentList = seedTreatmentList;
	}

	/*
	 * public Map<Integer, String> getSeedTreatmentDetailsMap() {
	 * 
	 * return seedTreatmentDetailsMap; }
	 * 
	 * public void setSeedTreatmentDetailsMap(Map<Integer, String>
	 * seedTreatmentDetailsMap) {
	 * 
	 * this.seedTreatmentDetailsMap = seedTreatmentDetailsMap; }
	 */

	private Map<Integer, String> formTreatmentList(String keyProperty, Map<Integer, String> seedTreatmentDetailsMap) {

		seedTreatmentDetailsMap = getPropertyData(keyProperty);
		return seedTreatmentDetailsMap;
	}

	public FarmCatalogue getFarmCatalogue() {

		return farmCatalogue;
	}

	public void setFarmCatalogue(FarmCatalogue farmCatalogue) {

		this.farmCatalogue = farmCatalogue;
	}

	public String getSelectedSeedTreatment() {

		return selectedSeedTreatment;
	}

	public void setSelectedSeedTreatment(String selectedSeedTreatment) {

		this.selectedSeedTreatment = selectedSeedTreatment;
	}

	/*public Map<Integer, String> getCropCategoryList() {
		Map<Integer, String> cropCategories = new LinkedHashMap<Integer, String>();
		String categories = getText("cropCategories");
		String[] cropCategory = categories.split(",");
		int i = 0;
		for (String cropCat : cropCategory) {
			cropCategories.put(Integer.valueOf(++i), cropCat);
		}
		return cropCategories;
	}*/

	public Map<String, String> getCropCategoryList() {

		Map<String, String> cropCategories = new HashMap<>();
		cropCategories = getFarmCatalougeMap(Integer.valueOf(getText("cropCategoriesTypez")));
		return cropCategories;

	}
	
	public Map<String, String> getSeedTreatmentDetailsList() {

		Map<String, String> seedTreatmentDetailsList = new LinkedHashMap<>();

		seedTreatmentDetailsList = getFarmCatalougeMap(Integer.valueOf(getText("seedTreatment")));

		seedTreatmentDetailsList.put("99", "Others");
		return seedTreatmentDetailsList;
	}

	public void setCropCategoryList(Map<Integer, String> cropCategoryList) {
		this.cropCategoryList = cropCategoryList;
	}

	public String getSelectedCropCategoryList() {
		return selectedCropCategoryList;
	}

	public void setSelectedCropCategoryList(String selectedCropCategoryList) {
		this.selectedCropCategoryList = selectedCropCategoryList;
	}

	public String[] getCropCategory() {
		return cropCategory;
	}

	public void setCropCategory(String[] cropCategory) {
		this.cropCategory = cropCategory;
	}

	public List<JSONObject> getJsonObjectList() {
		return jsonObjectList;
	}

	public void setJsonObjectList(List<JSONObject> jsonObjectList) {
		this.jsonObjectList = jsonObjectList;
	}

	public String getCropInfoEnabled() {
		return cropInfoEnabled;
	}

	public void setCropInfoEnabled(String cropInfoEnabled) {
		this.cropInfoEnabled = cropInfoEnabled;
	}

	public IClientService getClientService() {
		return clientService;
	}

	public void setClientService(IClientService clientService) {
		this.clientService = clientService;
	}

	public String getFarmerUniqueId() {
		return farmerUniqueId;
	}

	public void setFarmerUniqueId(String farmerUniqueId) {
		this.farmerUniqueId = farmerUniqueId;
	}

	public Map<String, String> getStableLenMap() {

		stableLenMap = getFarmCatalougeMap(Integer.valueOf(getText("stapleLength")));
		return stableLenMap;
	}

	public void setStableLenMap(Map<String, String> stableLenMap) {
		this.stableLenMap = stableLenMap;
	}

	public String getFarmerfarmName() {
		return farmerfarmName;
	}

	public void setFarmerfarmName(String farmerfarmName) {
		this.farmerfarmName = farmerfarmName;
	}
	public Map<Integer, Integer> getYearList() {

		int startYear = 1950;
		for (int i = 1; i <= 100; i++) {
			yearList.put(startYear, startYear);
			startYear++;
		}
		return yearList;
	}

	public void setYearList(Map<Integer, Integer> yearList) {
		this.yearList = yearList;
	}

	public String getCultiVal() {
		return cultiVal;
	}

	public void setCultiVal(String cultiVal) {
		this.cultiVal = cultiVal;
	}

	public String getTotalLandHoldVal() {
		return totalLandHoldVal;
	}

	public void setTotalLandHoldVal(String totalLandHoldVal) {
		this.totalLandHoldVal = totalLandHoldVal;
	}

	public List<JSONObject> getJsonFarmObjectList() {
		return jsonFarmObjectList;
	}

	public void setJsonFarmObjectList(List<JSONObject> jsonFarmObjectList) {
		this.jsonFarmObjectList = jsonFarmObjectList;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	@Override
	public InputStream getExportDataStream(String exportType) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public FarmIcsConversion getFilter() {
		return filter;
	}

	public void setFilter(FarmIcsConversion filter) {
		this.filter = filter;
	}
	
	
	
}
