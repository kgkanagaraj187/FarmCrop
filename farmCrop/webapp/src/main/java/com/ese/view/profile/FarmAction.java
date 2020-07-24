/*

 * FarmAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

package com.ese.view.profile;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.script.ScriptException;

import org.apache.cxf.jaxrs.impl.ServletRequestPropertyHolder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.profile.CropHarvest;
import com.ese.entity.profile.CropHarvestDetails;
import com.ese.entity.profile.CropSupply;
import com.ese.entity.profile.CropSupplyDetails;
import com.ese.entity.util.ESESystem;
import com.ese.entity.util.FarmField;
import com.ese.util.Base64Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionContext;
import com.sourcetrace.eses.BreadCrumb;
import com.sourcetrace.eses.entity.DynamicFeildMenuConfig;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.ICertificationService;
import com.sourcetrace.eses.service.IFarmCropsService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Coordinates;
import com.sourcetrace.esesw.entity.profile.DocumentUpload;
import com.sourcetrace.esesw.entity.profile.Expenditure;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.FarmDetailedInfo;
import com.sourcetrace.esesw.entity.profile.FarmICS;
import com.sourcetrace.esesw.entity.profile.FarmIcsConversion;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicFieldsValue;
import com.sourcetrace.esesw.entity.profile.FarmerLandDetails;
import com.sourcetrace.esesw.entity.profile.FarmerPlants;
import com.sourcetrace.esesw.entity.profile.FarmerScheme;
import com.sourcetrace.esesw.entity.profile.FarmerSoilTesting;
import com.sourcetrace.esesw.entity.profile.GramPanchayat;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.HousingInfo;
import com.sourcetrace.esesw.entity.profile.Locality;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.TreeDetail;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.entity.profile.FarmCropsCoordinates.typesOFCordinates;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

/**
 * The Class FarmAction.
 */
public class FarmAction extends SwitchValidatorAction {

	public static final int SELECT = -1;
	public static final String SELECT_MULTI = "-1";
	private static final long serialVersionUID = 1L;
	public static final int OTHER = 99;
	public static final String THREE_DECIMAL_FORMATTER = "##.000";
	public static final String FARMER_DETAIL = "farmerDetail";
	public static final String FARM_CERTIFIED_DETAIL = "farmCertifiedDetail";
	private final String FILTER_ALL = getText("filter.all");
	private int mainCrop = 0;

	DateFormat df = new SimpleDateFormat(getESEDateFormat());

	private String id;
	private String farmerId;
	private String farmerName;
	private String landInProduction;
	private String landNotInProduction;
	private String totalArea;
	private String tabIndex = "#tabs-1";
	private String tabIndexFarmer = "#tabs-2";
	private String tabIndexFarmerZ = "#tabs-2";
	private String tabIndexFarm = "#tabs-5";
	private String farmImageByteString;
	private String selectedFarmerId;
	private String selectedFarmer;
	private String selectedLandMeasurement;
	private String dateOfInspection;
	private String farmOwnedDetail;
	private String areaUnderIrrigationDetail;
	private String farmIrrigationDetail;
	private String irrigationSourceDetail;
	private String irrigationMethodDetail;
	private String soilTypeDetail;
	private String methodOfIrrigationDetail;
	private String soilFertilityDetail;
	private String landUnderICSStatusDetail;
	private String farmerUniqueId;
	private String farmId;
	private String landTopologyDetail;

	private double totalLandHoldingVal;
	private String selectedFarmOwned;
	private String processingActivity;
	private String selectedIrrigation;
	private String selectedIrrigationSource;
	// private int selectedIrrigationMethod;
	private String selectedSoilType;
	private String selectedMethodOfIrrigation;
	private int selectedSoilFertility;
	private int selectedICSStatus;
	private boolean isCertifiedFarmer = false;
	private Map<String, String> landMeasurementList = new LinkedHashMap<String, String>();
	private Map<String, String> farmOwnedList = new LinkedHashMap<String, String>();
	private Map<Integer, String> farmIrrigationList = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> irrigationSourceList = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> irrigationMethodList = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> soilTypeList = new LinkedHashMap<Integer, String>();
	// private Map<Integer, String> methodOfIrrigationList = new
	// LinkedHashMap<Integer, String>();
	private Map<Integer, String> soilFertilityList = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> icsStatusList = new LinkedHashMap<Integer, String>();
	private Farm farm;
	private IFarmerService farmerService;
	private Map<Integer, String> landGradientList = new LinkedHashMap<Integer, String>();

	private Map<Integer, String> soilTextureList = new LinkedHashMap<Integer, String>();
	private Map<String, String> approadList = new LinkedHashMap<String, String>();
	private Map<Integer, String> milletList = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> borewellList = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> processingActList = new LinkedHashMap<Integer, String>();
	private String selectedTexture;
	private String selectedGradient;
	private String selectedRoad;
	private String soilTextureDetail;
	private String landGradientDetail;
	private List<JSONObject> jsonObjectList;
	private String fileid;
	private String audioDownloadString;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private String sessionYearString;
	private String regYearString;
	// private String chemicalString;
	private String selectedRoadString;
	List<FarmCatalogue> catalogueList = new ArrayList<FarmCatalogue>();
	List<FarmCatalogue> catalogueList1 = new ArrayList<FarmCatalogue>();
	List<FarmCatalogue> catalogueList2 = new ArrayList<FarmCatalogue>();
	List<FarmCatalogue> catalogueList3 = new ArrayList<FarmCatalogue>();
	List<FarmCatalogue> catalogueList4 = new ArrayList<FarmCatalogue>();
	List<FarmCatalogue> machinaryList = new ArrayList<FarmCatalogue>();
	List<FarmCatalogue> polyList = new ArrayList<FarmCatalogue>();
	private List<CropHarvest> harvest = new ArrayList<CropHarvest>();
	private List<CropHarvestDetails> harvestDetails = new ArrayList<CropHarvestDetails>();
	Map<String, String> isSoilTested = new LinkedHashMap<String, String>();
	private Map<Integer, String> ffsList = new LinkedHashMap<Integer, String>();
	private List<CropSupply> harvestSupply = new ArrayList<CropSupply>();
	private Set<CropSupplyDetails> cropSupplyDetails;
	private List<FarmICS> farmICSs;
	private Map<Integer, String> ifsList = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> soilConservationList = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> waterConservationList = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> serviceCentresList = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> trainingProgramList = new LinkedHashMap<Integer, String>();

	private List<Expenditure> expenditureList;
	private Map<String, String> harvestseasondetail = new LinkedHashMap<String, String>();
	@Autowired
	private IPreferencesService preferncesService;
	private String soilTypeName;
	private String soilTexName;
	private String farmerPlantsJsonString = "";

	private String benefitaryString;
	private String benefitary;
	private String benefitaryCode;
	private String schemeTxt;
	private String schemeCode;
	private String ProdString;
	private String sangham;
	private String prodEditArray;
	DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
	private JSONObject jsonObject = new JSONObject();
	private String plantDetailString;
	private String getplantDetailsList;
	private String soilTestJson;
	private String expenditureJson;
	Map<Integer, String> soilTestMap = new LinkedHashMap<Integer, String>();
	Map<Integer, String> qualifiedTestMap = new LinkedHashMap<Integer, String>();
	private List<FarmerLandDetails> farmerLandDetailsList = new ArrayList<FarmerLandDetails>();
	private List<FarmerLandDetails> updatefarmerLandDetailsList = new ArrayList<FarmerLandDetails>();
	private long farmerLandId;
	@Autowired
	private ICatalogueService catalogueService;
	@Autowired
	private IUniqueIDGenerator idGenerator;
	List<FarmerSoilTesting> farmerSoilTestingList = new LinkedList<>();

	private String irrigateDetail;
	private String rainFeedDetail;
	private long farmingseason;
	private String FarmerseasonJsonString = "";
	private Set<FarmerLandDetails> farmerLandDetails;

	private Map<Integer, Integer> yearList = new LinkedHashMap<Integer, Integer>();
	Map<Integer, String> soilList = new LinkedHashMap<Integer, String>();
	private String enableSoliTesting;
	private String documentFileId;
	private String harvestSeason;
	private String cropInfoEnabled;

	DecimalFormat formatter = new DecimalFormat("0.00");

	List<Village> villages = new ArrayList<>();
	List<Municipality> cities = new ArrayList<>();
	List<GramPanchayat> panchayat = new ArrayList<>();
	List<Warehouse> samithi = new ArrayList<Warehouse>();
	private String selectedLocality;
	private String selectedCity;
	private String selectedPanchayat;
	private String selectedVillage;
	private String gramPanchayatEnable;
	private String selectedSamithi;
	private String selectedFpo;
	@Autowired
	private ILocationService locationService;
	private HousingInfo housingInfo;

	private String formatInspectionDate;

	private String testDate;
	private String icsValue;
	@Autowired
	private IProductDistributionService productDistributionService;
	private String selectedCrop;
	List<ProcurementVariety> listProcurementVariety = new ArrayList<ProcurementVariety>();
	private String selectedVariety;
	@Autowired
	private IFarmCropsService farmCropsService;

	public Map<String, String> seasonMap = new LinkedHashMap<>();
	private String waterHarvest;
	private Map<String, String> waterHarvestList = new LinkedHashMap<String, String>();
	private String waterHarvests;
	private String isIrrigation;
	// Wilmar
	private String organicStatus;
	private String scopeName;
	private String lastDateChemical;
	private String selectedWaterSource;
	private String selectedInputSource;
	@Autowired
	private ICertificationService certificationService;
	
	private String noOfWineOnPlot;
	//olivado
    private Map<Integer, String> parallelProductionList = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> hiredLabourList = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> riskCategoryList = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> bananaTreesList = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> cropTypeList = new LinkedHashMap<Integer, String>();
	private String presenceOfBanana;
	private String parallelProduction;
	private String hiredLabours;
	private String riskCategory;
	private String treeDetailJsonString="";
	private Farm farms;
	private List<TreeDetail> treeDetails;
	private String isFollowUp;
	private int actStatuss;
	private int cerLevel;
	private String lat;
	private String lon;
	private List<FarmerDynamicFieldsValue> orgnaicVarietyList;
	private List<FarmerDynamicFieldsValue> conventionalVarietyList;
	
	private Map<Integer, String> certificationLevels = new LinkedHashMap<Integer, String>();
	List<String> farmCodeFmt = new ArrayList<>(Arrays.asList("A", "B","C","D","E","F","G","H","I","J","K","L","M","N","O","P"));
	
	 private Map<String, String> insTypeList = new LinkedHashMap();
	 private String inspType;
	 private String certificationType;
	/**
	 * @see com.sourcetrace.esesw.view.SwitchAction#data()
	 */
	public String data() throws Exception {

		Map<String, String> searchRecord = getJQGridRequestParam(); // get the
																	// search
																	// parameter
																	// with
		// value

		Farm filter = new Farm();

		if (!StringUtil.isEmpty(searchRecord.get("farmCode")))
			filter.setFarmCode(searchRecord.get("farmCode").trim());

		if (!StringUtil.isEmpty(searchRecord.get("farmName")))
			filter.setFarmName(searchRecord.get("farmName").trim());

		/*
		 * if (!StringUtil.isEmpty(searchRecord.get("fdi.regYear"))){
		 * FarmDetailedInfo farmDetailedInfo = new FarmDetailedInfo();
		 * farmDetailedInfo.setRegYear(searchRecord.get("fdi.regYear").trim());
		 * filter.setFarmDetailedInfo( farmDetailedInfo); }
		 */

		if (!StringUtil.isEmpty(searchRecord.get("fdi.surveyNumber"))) {
			FarmDetailedInfo farmDetailedInfo = new FarmDetailedInfo();
			farmDetailedInfo.setSurveyNumber(searchRecord.get("fdi.surveyNumber").trim());
			filter.setFarmDetailedInfo(farmDetailedInfo);
		}

		if (!StringUtil.isEmpty(searchRecord.get("f.firstName"))) {
			Farmer farmer = new Farmer();
			farmer.setFirstName(searchRecord.get("f.firstName").trim());
			filter.setFarmer(farmer);
		}

		if (!StringUtil.isEmpty(searchRecord.get("hectares")))
			filter.setHectares(searchRecord.get("hectares").trim());

		if (!StringUtil.isEmpty(searchRecord.get("landInProduction")))
			filter.setLandInProduction(searchRecord.get("landInProduction").trim());

		if (!StringUtil.isEmpty(searchRecord.get("landNotInProduction")))
			filter.setLandNotInProduction(searchRecord.get("landNotInProduction").trim());

		if (!StringUtil.isEmpty(this.farmerId)) {
			if (!ObjectUtil.isEmpty(filter.getFarmer())) {
				filter.getFarmer().setId(Long.parseLong(this.farmerId));
			} else {
				Farmer farmer = new Farmer();
				farmer.setId(Long.parseLong(this.farmerId));
				farmer.setFirstName(null);
				filter.setFarmer(farmer);
			}
		}

		if (!StringUtil.isEmpty(searchRecord.get("nameOfInspector"))) {
			filter.setLocalNameOfCrotenTree(searchRecord.get("nameOfInspector"));
		}

		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		return sendJQGridJSONResponse(data);
	}

	/**
	 * @see com.sourcetrace.esesw.view.SwitchAction#toJSON(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		Farm farm = (Farm) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();

		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.AWI_TENANT_ID) || getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)) {
			rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">"
					+ ((!StringUtil.isEmpty(farm.getFarmId()) ? farm.getFarmId() : "NA") + "</font>"));
		} else {
			rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">"
					+ ((!StringUtil.isEmpty(farm.getFarmCode()) ? farm.getFarmCode() : "NA") + "</font>"));
		}

		rows.add(farm.getFarmName());
		// rows.add( farm.getFarmDetailedInfo().getRegYear());
		if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.BLRI_TENANT_ID)
				&& !getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)&& !getCurrentTenantId().equalsIgnoreCase("griffith") && !getCurrentTenantId().equalsIgnoreCase("gsma") && !getCurrentTenantId().equalsIgnoreCase(ESESystem.COFBOARD_TENANT_ID)&& !getCurrentTenantId().equalsIgnoreCase("symrise")){
			if (!StringUtil.isEmpty(farm.getFarmDetailedInfo().getSurveyNumber())) {
				rows.add(farm.getFarmDetailedInfo().getSurveyNumber());
			} else {
				rows.add("" + getText("farm.na"));
			}
		}
		
	
		
		/*
		 * if(farm.getFarmer().getIsCertifiedFarmer()==1){ FarmIcsConversion
		 * fIcs =
		 * farmerService.findFarmIcsConversionByFarmIdWithActive(Long.valueOf(
		 * farm.getId()));
		 * rows.add(!ObjectUtil.isEmpty(fIcs)&&!StringUtil.isEmpty(fIcs.
		 * getOrganicStatus())?fIcs.getOrganicStatus():"NA"); }else{
		 * rows.add("Conventional"); }
		 */
		
		
		if (getCurrentTenantId().equalsIgnoreCase("griffith")){
			if (!StringUtil.isEmpty(farm.getFarmDetailedInfo().getTotalLandHolding())) {
				rows.add(farm.getFarmDetailedInfo().getTotalLandHolding());
			} else {
				rows.add("" + getText("farm.na"));
			}
		}
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.SYMRISE)) {
			
			rows.add(certificationLevels.get(farm.getCertificateStandardLevel()));
		}

		/*if (farm.getFarmer().getIsCertifiedFarmer() == 1) {
			FarmIcsConversion fIcs = farmerService.findFarmIcsConversionByFarmIdWithActive(Long.valueOf(farm.getId()));

			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)) {
				rows.add(!ObjectUtil.isEmpty(fIcs) && !StringUtil.isEmpty(fIcs.getInspectorName())
						? fIcs.getInspectorName() : "");
				//DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
				SimpleDateFormat sf=new SimpleDateFormat("MM-dd-yyyy");
				rows.add(!ObjectUtil.isEmpty(fIcs) && (!StringUtil.isEmpty(fIcs.getInspectionDate()))
						? sf.format(fIcs.getInspectionDate()) : "");
				rows.add(!ObjectUtil.isEmpty(farm.getFarmDetailedInfo())
						&& !StringUtil.isEmpty(farm.getFarmDetailedInfo().getProposedPlantingArea())
								? farm.getFarmDetailedInfo().getProposedPlantingArea() : "");
			}
			if (!ObjectUtil.isEmpty(fIcs) && fIcs != null) {
				rows.add(!StringUtil.isEmpty(fIcs.getOrganicStatus()) && fIcs.getOrganicStatus().equalsIgnoreCase("3")
						? getLocaleProperty("alrdyCertified") : getLocaleProperty("inprocess"));
			} else {
				rows.add("Conventional");
			}
		} else {
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)) {
				rows.add("");
				rows.add("");
				rows.add("");
			}
			rows.add("Conventional");
		}*/

		/*
		 * rows.add(!ObjectUtil.isEmpty(farm.getFarmer()) ?
		 * farm.getFarmer().getFirstName() + " " +
		 * (!StringUtil.isEmpty(farm.getFarmer().getLastName()) ?
		 * farm.getFarmer() .getLastName() : "") : "");
		 
		if (farm.getPhoto() != null && farm.getPhoto().length > 1) {
			String photoCaptureTime = "";
			if (!ObjectUtil.isEmpty(farm.getPhotoCaptureTime())) {
				photoCaptureTime = DateUtil.getDateInFormat(DateUtil.TXN_DATE_TIME, farm.getPhotoCaptureTime());
			}
			rows.add("<button class='imgIcn' title='" + getText("farm.photos.available.title")
					+ "' onclick='showFarmPhoto(\"" + farm.getId() + "\",\"" + farm.getFarmCode() + "\",\""
					+ farm.getFarmName() + "\",\"" + photoCaptureTime + "\")'></button>");
		} else {
			// No Image
			rows.add("<button class='no-imgIcn' title='" + getText("farm.photo.unavailable.title") + "'></button>");
		}

		if ((!StringUtil.isEmpty(farm.getLatitude()) && !StringUtil.isEmpty(farm.getLongitude()))
				&& farm.getActiveCoordinates() !=null && !ObjectUtil.isEmpty(farm.getActiveCoordinates()) && farm.getActiveCoordinates().getFarmCoordinates()!=null && !ObjectUtil.isListEmpty(farm.getActiveCoordinates().getFarmCoordinates())) {
			rows.add("<button class='faMap' title='" + getText("farm.map.available.title") + "' onclick='showFarmMap(\""
					+ farm.getFarmCode() + "\",\"" + farm.getFarmName() + "\",\""
					+ (!ObjectUtil.isEmpty(farm.getLatitude()) ? farm.getLatitude() : "") + "\",\""
					+ (!ObjectUtil.isEmpty(farm.getLongitude()) ? farm.getLongitude() : "") + "\","
					+ getFarmJSONObjects(farm.getActiveCoordinates().getFarmCoordinates()) + ")'></button>");
		} else {
			// No Latlon
			rows.add("<button class='no-latLonIcn' title='" + getText("farm.map.unavailable.title") + "'></button>");
		}
*/
	
			if (!StringUtil.isEmpty(farm.getPlottingStatus()) && farm.getPlottingStatus()==1 ) {
				rows.add("Completed");
			} else {
				rows.add("Pending");
			}
	
		
		rows.add("<button class='faDelete' title='" + getText("farm.delete.title") + "' onclick='deleteFarm("
				+ farm.getId() + ")'> Delete </button>");

		jsonObject.put("id", farm.getId());
		jsonObject.put("cell", rows);
		return jsonObject;
	}

	/**
	 * Gets the farm json objects.
	 * 
	 * @param coordinates
	 *            the coordinates
	 * @return the farm json objects
	 */
	@SuppressWarnings("unchecked")
	private List<JSONObject> getFarmJSONObjects(Set<Coordinates> coordinates) {
		JSONArray neighbouringDetails_farm = new JSONArray();
		List<JSONObject> returnObjects = new ArrayList<JSONObject>();
		List<Coordinates> listCoordinates = new ArrayList<Coordinates>(coordinates);
		Collections.sort(listCoordinates);
		if (!ObjectUtil.isListEmpty(listCoordinates)) {
			for (Coordinates coordinateObj : listCoordinates) {
				JSONObject jsonObject = new JSONObject();

				// jsonObject.put("orderNo",!ObjectUtil.isEmpty(coordinateObj.getOrderNo())
				// ? coordinateObj.getOrderNo():"");
				/*jsonObject.put("lat",
						!ObjectUtil.isEmpty(coordinateObj.getLatitude()) ? coordinateObj.getLatitude() : "");
				jsonObject.put("lon",
						!ObjectUtil.isEmpty(coordinateObj.getLongitude()) ? coordinateObj.getLongitude() : "");
				returnObjects.add(jsonObject);*/
				
				if(coordinateObj.getType() == typesOFCordinates.mainCoordinates.ordinal()){
					jsonObject.put("lat",!ObjectUtil.isEmpty(coordinateObj.getLatitude()) ? coordinateObj.getLatitude() : "");
					jsonObject.put("lon",!ObjectUtil.isEmpty(coordinateObj.getLongitude()) ? coordinateObj.getLongitude() : "");
					returnObjects.add(jsonObject);
				}else if(coordinateObj.getType() == typesOFCordinates.neighbouringDetails_farm.ordinal()){
					jsonObject.put("type",coordinateObj.getType());
					jsonObject.put("title",coordinateObj.getTitle());
					jsonObject.put("description",coordinateObj.getDescription());
					jsonObject.put("lat",!ObjectUtil.isEmpty(coordinateObj.getLatitude()) ? coordinateObj.getLatitude() : "");
					jsonObject.put("lng",!ObjectUtil.isEmpty(coordinateObj.getLongitude()) ? coordinateObj.getLongitude() : "");
					neighbouringDetails_farm.add(jsonObject);
				}
				
			}
		}
		JSONObject neighbouring_jsonObject = new JSONObject();
		neighbouring_jsonObject.put("neighbouringDetails_farm",neighbouringDetails_farm);
		returnObjects.add(neighbouring_jsonObject);
		return returnObjects;
	}

	/**
	 * Creates the.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String create() throws Exception {
		int seq=0;
		int farmNameSeq=1;
		if (farm == null) {
			setCommand(CREATE);
			// System.out.println("---" + this.farmerId);

			if (StringUtil.isEmpty(this.farmerId)) {
				return LIST;
			}

			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.BLRI_TENANT_ID)) {

				return "blriFarm";
			} else {
				Farmer farmer = new Farmer();
				Farm farm = new Farm();
				// farmer.setFarmerId(this.farmerId);
				farmer = farmerService.findFarmerByFarmerId(this.farmerId);
				farm.setFarmer(farmer);
				setFarmerUniqueId(String.valueOf(farmer.getId()));
				if (getFarmerName() == null || getFarmerName().isEmpty()) {
					setFarmerName(farmer.getName());
				}

				request.setAttribute(HEADING, getText("farmcreate.page"));
				setSelectedFarmOwned(SELECT_MULTI);
				setSelectedIrrigation(SELECT_MULTI);
				setSelectedIrrigationSource(SELECT_MULTI);
				// setSelectedIrrigationMethod(SELECT);
				setSelectedSoilType(SELECT_MULTI);
				setSelectedMethodOfIrrigation(SELECT_MULTI);
				setSelectedSoilFertility(SELECT);
				setSelectedICSStatus(SELECT);
				setSelectedGradient(SELECT_MULTI);
				setSelectedTexture(SELECT_MULTI);
				return INPUT;
			}
		} else {
			// try {
			Farmer farmerExist = null;
			if (!StringUtil.isEmpty(this.farmerId))
				farmerExist = farmerService.findFarmerByFarmerId(farmerId);
			else
				return "farmerDetail";

			if (!ObjectUtil.isEmpty(farmerExist))
				farm.setFarmer(farmerExist);
			else
				return "farmerDetail";
			/*
			 * if (ObjectUtil.isEmpty(farm.getFarmImage())) {
			 * addActionError(getText("empty.photo")); return INPUT; }
			 */

			/*
			 * if (farm.getFarmer().getCertificationType() ==
			 * Farmer.CERTIFICATION_TYPE_NONE) { Double totalAreaOfProduction =
			 * Double.parseDouble(landInProduction) +
			 * (StringUtil.isEmpty(landNotInProduction) ? 0 : Double
			 * .parseDouble(landNotInProduction)); this.totalArea =
			 * CurrencyUtil.getDecimalFormat(totalAreaOfProduction,
			 * THREE_DECIMAL_FORMATTER); farm.setHectares(totalArea);
			 * farm.setFarmDetailedInfo(null); }
			 */

			// } catch (Exception e) {
			// this.totalArea = "0";
			// farm.setHectares(this.totalArea);
			// }

			/*
			 * if (StringUtil.isEmpty(farm.getFarmName())) {
			 * farm.setFarmName(farm.getFarmer().getFirstName()+" "+farm.
			 * getFarmer().getLastName()); }
			 */
			
			farm.setCreatedUsername(getUsername());
			farm.setCreatedDate(new Date()); 
			
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.BLRI_TENANT_ID)) {
				farm.setHousingInfos(setHousingInfo());
			}

			if ("1".equalsIgnoreCase(enableSoliTesting)) {
				Set<DocumentUpload> documentUploadSet = new HashSet<DocumentUpload>();
				if (!ObjectUtil.isListEmpty(farm.getDocUploadList())) {
					for (DocumentUpload document : farm.getDocUploadList()) {
						if (!StringUtil.isEmpty(document.getDocFile())) {
							document.setFarm(farm);
							document.setContent(FileUtil.getBinaryFileContent(document.getDocFile()));
							document.setName(document.getDocFileFileName());
							documentUploadSet.add(document);

						}

					}
					farm.setDocUpload(null);
					if (!ObjectUtil.isListEmpty(documentUploadSet))
						farm.setDocUpload(documentUploadSet);

				}
			}
			Set<FarmerLandDetails> framerLandDetails = new LinkedHashSet<FarmerLandDetails>();
			if (!ObjectUtil.isListEmpty(farmerLandDetailsList)) {
				for (FarmerLandDetails farmerLandDetail : farmerLandDetailsList) {
					if (!ObjectUtil.isEmpty(farmerLandDetail)) {
						FarmerLandDetails LandDetails = new FarmerLandDetails();
						LandDetails.setIrrigatedTotLand(!StringUtil.isEmpty(farmerLandDetail.getIrrigatedLand())
								? Double.parseDouble(farmerLandDetail.getIrrigatedLand()) : 0.00);
						LandDetails.setIrrigatedIfsPractices(farmerLandDetail.getIrrigatedFarmingLand());
						LandDetails.setRainfedTotLand(!StringUtil.isEmpty(farmerLandDetail.getFedtotaland())
								? Double.parseDouble(farmerLandDetail.getFedtotaland()) : 0.00);
						LandDetails.setRanifedIfsPractices(farmerLandDetail.getFedtotalics());
						LandDetails.setSeasonCode(farmerLandDetail.getTempSeasonCode());
						framerLandDetails.add(LandDetails);
					}
				}
				farm.setFarmerLandDetails(framerLandDetails);
			}
			
			farm.setFarmCode(String.valueOf(DateUtil.getRevisionNoDateTimeMilliSec()));
			

			if (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo())) {
				if (farm.getFarmDetailedInfo().isSameAddressofFarmer()) {
					farm.getFarmDetailedInfo().setFarmAddress((farm.getFarmer().getAddress()!=null && !StringUtil.isEmpty(farm.getFarmer().getAddress()) ? farm.getFarmer().getAddress()+",":"") + " "
							+ farm.getFarmer().getCity().getName() + "," + farm.getFarmer().getVillage().getName());
					if (farm.getFarmDetailedInfo().getFarmAddress().trim().length() > 255) {
						farm.getFarmDetailedInfo().setFarmAddress(!StringUtil.isEmpty(farm.getFarmer().getAddress())
								? farm.getFarmer().getAddress() : "");
					}
				}

				// farm.getFarmDetailedInfo().setFarmerFieldSchool(Integer.valueOf(farm.getFarmerFieldSchool()));

				farm.getFarmDetailedInfo().setIsFFSBenifited(farm.getFarmDetailedInfo().getIsFFSBenifited());
				if(!getCurrentTenantId().equals(ESESystem.LIVELIHOOD_TENANT_ID)){
					if (!StringUtil.isEmpty(selectedFarmOwned))
					farm.getFarmDetailedInfo()
							.setFarmOwned((selectedFarmOwned != SELECT_MULTI) ? selectedFarmOwned : SELECT_MULTI);
				}
				if (!StringUtil.isEmpty(selectedIrrigation))
					farm.getFarmDetailedInfo().setFarmIrrigation(
							(selectedIrrigation != SELECT_MULTI) ? selectedIrrigation : SELECT_MULTI);
				if (!StringUtil.isEmpty(selectedIrrigationSource))
					farm.getFarmDetailedInfo().setIrrigationSource(
							(selectedIrrigationSource != SELECT_MULTI) ? selectedIrrigationSource : SELECT_MULTI);
				// farm.getFarmDetailedInfo().setIrrigationMethod((selectedIrrigationMethod
				// !=
				// SELECT ? selectedIrrigationMethod : SELECT));
				if (!StringUtil.isEmpty(selectedSoilType))
					farm.getFarmDetailedInfo()
							.setSoilType((selectedSoilType != SELECT_MULTI) ? selectedSoilType : SELECT_MULTI);
				if (!StringUtil.isEmpty(selectedMethodOfIrrigation))
					farm.getFarmDetailedInfo().setMethodOfIrrigation(
							(selectedMethodOfIrrigation != SELECT_MULTI) ? selectedMethodOfIrrigation : SELECT_MULTI);
				if (!StringUtil.isEmpty(selectedSoilFertility))
					farm.getFarmDetailedInfo()
							.setSoilFertility((selectedSoilFertility != SELECT) ? selectedSoilFertility : SELECT);
				if (!StringUtil.isEmpty(selectedICSStatus))
					farm.getFarmDetailedInfo()
							.setLandUnderICSStatus((selectedICSStatus != SELECT) ? selectedICSStatus : SELECT);
				if (!StringUtil.isEmpty(selectedGradient))
					farm.getFarmDetailedInfo()
							.setLandGradient((selectedGradient != SELECT_MULTI) ? selectedGradient : SELECT_MULTI);
				if (!StringUtil.isEmpty(selectedTexture))
					farm.getFarmDetailedInfo()
							.setSoilTexture((selectedTexture != SELECT_MULTI) ? selectedTexture : SELECT_MULTI);
				if (!StringUtil.isEmpty(selectedRoad))
					farm.getFarmDetailedInfo()
							.setApproachRoad((selectedRoad != SELECT_MULTI) ? selectedRoad : SELECT_MULTI);
				if (!StringUtil.isEmpty(sessionYearString))
					farm.getFarmDetailedInfo().setSessionYear(sessionYearString);

				if (!StringUtil.isEmpty(regYearString))
					farm.getFarmDetailedInfo().setRegYear(regYearString);
				if (!StringUtil.isEmpty(farm.getFarmDetailedInfo().getTotalLandHolding())) {
					farm.getFarmDetailedInfo().setTotalLandHolding(
							formatter.format(Double.valueOf(farm.getFarmDetailedInfo().getTotalLandHolding())));
				} else {
					farm.getFarmDetailedInfo().setTotalLandHolding("0.000");
				}
				if (!StringUtil.isEmpty(farm.getFarmDetailedInfo().getProposedPlantingArea())) {
					farm.getFarmDetailedInfo().setProposedPlantingArea(
							formatter.format(Double.valueOf(farm.getFarmDetailedInfo().getProposedPlantingArea())));
				} else {
					farm.getFarmDetailedInfo().setProposedPlantingArea("0.000");
				}
				
				///if(!StringUtil.isEmpty(processingActivity)){
					farm.getFarmDetailedInfo().setProcessingActivity(farm.getFarmDetailedInfo().getProcessingActivity());
				///}
				ESESystem preferences = preferncesService.findPrefernceById("1");
				if (!ObjectUtil.isEmpty(preferences)) {
					if (!StringUtil.isEmpty(farm.getFarmDetailedInfo().getLastDateOfChemicalString())
							&& farm.getFarmDetailedInfo().getLastDateOfChemicalString()!= null) {

						farm.getFarmDetailedInfo().setLastDateOfChemical(DateUtil.convertStringToDate(
										 farm.getFarmDetailedInfo().getLastDateOfChemicalString(), preferences
												.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT)));
					}

				} 
				
				/*
				 * if(!StringUtil.isEmpty(chemicalString))
				 * farm.getFarmDetailedInfo().setLastDateOfChemicalApplication(
				 * chemicalString);
				 */

				/*
				 * if (!StringUtil.isEmpty(dateOfInspection)) { DateFormat
				 * format = new SimpleDateFormat("MM/dd/yyyy"); try {
				 * farm.getFarmDetailedInfo().setInternalInspectionDate( (Date)
				 * format.parse(dateOfInspection)); } catch (ParseException e) {
				 * e.printStackTrace(); } }
				 */

				farm.setFarmImageExist("0");
				if (getFarm().getFarmImage() != null) {
					farm.setPhoto(FileUtil.getBinaryFileContent(getFarm().getFarmImage()));
					farm.setFarmImageExist("1");
					farm.getFarmer().setBasicInfo(1);

				}
				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.COFBOARD_TENANT_ID)) {
					farm.setPresenceBananaTrees(farm.getPresenceBananaTrees());
					farm.setDistanceProcessingUnit(farm.getDistanceProcessingUnit());
					}

				if (getCurrentTenantId().equalsIgnoreCase("chetna")) {

					farm.setLandTopology(farm.getLandTopology());

				}

				Set<FarmICS> farmICSSet = new LinkedHashSet<FarmICS>();
				if (!ObjectUtil.isListEmpty(farm.getFarmICSList())) {
					for (FarmICS farmICS : farm.getFarmICSList()) {
						if (!StringUtil.isEmpty(farmICS.getLandIcsDetails())
								|| !StringUtil.isEmpty(farmICS.getSurveyNo())
								|| !StringUtil.isEmpty(farmICS.getBeginOfConversionString())) {
							farmICS.setLandIcsDetails(farmICS.getLandIcsDetails());
							farmICS.setSurveyNo(farmICS.getSurveyNo());

							if (!ObjectUtil.isEmpty(preferences)) {
								DateFormat genDate = new SimpleDateFormat(
										preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
								if (!ObjectUtil.isEmpty(farmICS.getBeginOfConversion())
										&& farmICS.getBeginOfConversion() != null) {

									farmICS.setBeginOfConversionString(genDate.format(farmICS.getBeginOfConversion()));
								}

								/*
								 * if (!StringUtil.isEmpty(farmICS.
								 * getBeginOfConversionString())) {
								 * farmICS.setBeginOfConversion( (Date)
								 * format.parse(farmICS.
								 * getBeginOfConversionString()));
								 */
							} else {
								farmICS.setBeginOfConversion(null);
							}

							farmICSSet.add(farmICS);
						}
					}
				}
				Set<FarmIcsConversion> ics = new HashSet<FarmIcsConversion>();
				if (farm.getFarmer().getIsCertifiedFarmer() == 1) {
					if (farm.getFarmIcsConv() != null && !ObjectUtil.isEmpty(farm.getFarmIcsConv())) {

						farm.getFarmIcsConv().setIsActive(1);
						if (!StringUtil.isEmpty(farm.getFarmIcsConv().getIcsType())) {
							farm.getFarmIcsConv().setIcsType(farm.getFarmIcsConv().getIcsType());
						} else {
							farm.getFarmIcsConv().setIcsType("0");
						}
						farm.getFarmIcsConv().setInsType("0");
						farm.getFarmIcsConv().setFarmer(farm.getFarmer());

						farm.getFarmIcsConv().setScope(!StringUtil.isEmpty(farm.getFarmIcsConv().getScope())
								? farm.getFarmIcsConv().getScope() : "");
						if(getCurrentTenantId().equalsIgnoreCase(ESESystem.SUSAGRI)){
						farm.getFarmIcsConv().setInsType(!StringUtil.isEmpty(getInspType()) ? getInspType() : "");
						}
						
						/*
						 * if (farm.getFarmIcsConv().getInspectionDateString()
						 * != null &&
						 * !farm.getFarmIcsConv().getInspectionDateString().
						 * isEmpty()) {
						 */

						if (!ObjectUtil.isEmpty(preferences)) {
							if (!StringUtil.isEmpty(farm.getFarmIcsConv().getInspectionDateString())
									&& farm.getFarmIcsConv().getInspectionDateString() != null) {

								farm.getFarmIcsConv()
										.setInspectionDate(DateUtil.convertStringToDate(
												farm.getFarmIcsConv().getInspectionDateString(), preferences
														.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT)));
							}

						} else {
							farm.getFarmIcsConv().setInspectionDate(DateUtil.convertStringToDate(
									farm.getFarmIcsConv().getInspectionDateString(), "MM/dd/yyyy"));
						}

						/* } */
						if (farm.getFarmer().getIsCertifiedFarmer() == 1
								&& farm.getFarmIcsConv().getIcsType().equalsIgnoreCase("3")) {
							farm.getFarmIcsConv().setOrganicStatus("3");
						} else {
							farm.getFarmIcsConv().setOrganicStatus("0");
						}
						ics.add(farm.getFarmIcsConv());
					}

				}else if(getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)){
					if (farm.getFarmIcsConv() != null && !ObjectUtil.isEmpty(farm.getFarmIcsConv())) {

						farm.getFarmIcsConv().setIsActive(1);
						if (!StringUtil.isEmpty(farm.getFarmIcsConv().getIcsType())) {
							farm.getFarmIcsConv().setIcsType(farm.getFarmIcsConv().getIcsType());
						} else {
							farm.getFarmIcsConv().setIcsType("0");
						}
						farm.getFarmIcsConv().setInsType("0");
						farm.getFarmIcsConv().setFarmer(farm.getFarmer());

						farm.getFarmIcsConv().setScope(!StringUtil.isEmpty(farm.getFarmIcsConv().getScope())
								? farm.getFarmIcsConv().getScope() : "");
						/*
						 * if (farm.getFarmIcsConv().getInspectionDateString()
						 * != null &&
						 * !farm.getFarmIcsConv().getInspectionDateString().
						 * isEmpty()) {
						 */

						if (!ObjectUtil.isEmpty(preferences)) {
							if (!StringUtil.isEmpty(farm.getFarmIcsConv().getInspectionDateString())
									&& farm.getFarmIcsConv().getInspectionDateString() != null) {

								farm.getFarmIcsConv()
										.setInspectionDate(DateUtil.convertStringToDate(
												farm.getFarmIcsConv().getInspectionDateString(), preferences
														.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT)));
							}

						} else {
							farm.getFarmIcsConv().setInspectionDate(DateUtil.convertStringToDate(
									farm.getFarmIcsConv().getInspectionDateString(), "MM/dd/yyyy"));
						}

						/* } */
						if (farm.getFarmer().getIsCertifiedFarmer() == 1
								&& farm.getFarmIcsConv().getIcsType().equalsIgnoreCase("3")) {
							farm.getFarmIcsConv().setOrganicStatus("3");
						} else {
							farm.getFarmIcsConv().setOrganicStatus("0");
						}
						ics.add(farm.getFarmIcsConv());
					}

				
					
				}
				if (ics.size() > 0)
					farm.setFarmICSConversion(ics);
				farm.setFarmICS(farmICSSet);
				farm.setExpenditures(formExpenditureSet());
				farm.setIsVerified(0);
				farmerService.addFarmDetailedInfo(farm.getFarmDetailedInfo());
				farm.setFarmDetailedInfo(farm.getFarmDetailedInfo());
			} else
				farm.setFarmDetailedInfo(null);

			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.AWI_TENANT_ID)) {
				if (!ObjectUtil.isEmpty(farm.getVillage())) {
					String codeGen = idGenerator.getFarmWebCodeIdSeq(
							farm.getVillage().getCity().getCode().substring(0, 1),
							farm.getVillage().getGramPanchayat().getCode().substring(0, 1));
					farm.setFarmId(codeGen);
				}

				if (!StringUtil.isEmpty(selectedSamithi)) {
					Warehouse samithi = locationService.findSamithiById(Long.valueOf(selectedSamithi));
					farm.setSamithi(ObjectUtil.isEmpty(samithi) ? null : samithi);
				}
				if (!StringUtil.isEmpty(selectedFpo)) {
					farm.setFpo(ObjectUtil.isEmpty(selectedFpo) ? null : selectedFpo);
				}
			}
			if (!StringUtil.isEmpty(waterHarvest)) {
				farm.setWaterHarvest(waterHarvest);
			}
			farm.setRevisionNo(DateUtil.getRevisionNumber());
			if(getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)){
				//farm.setFarmCode(farmerExist.getFarmerCode()+"-"+farmCodeFmt.get(farmerExist.getFarms().size()));
				if(farm.getFarmer().getFarms().size()>0){
				farm.setFarmId(String.valueOf(farmerExist.getFarmerCode()+"-"+farmCodeFmt.get(farmerExist.getFarms().size())));}else{
				farm.setFarmId(String.valueOf(farm.getFarmer().getFarmerCode()+"-"+farmCodeFmt.get(0)));		
				}
			}
			if(getCurrentTenantId().equalsIgnoreCase(ESESystem.GRIFFITH_TENANT_ID)){
				int farmSize=0;
				if(!ObjectUtil.isEmpty(farmerExist.getFarms().size())){
					farmSize=farmerExist.getFarms().size();
				}
				
				String vilSeqCode=(farm.getFarmer().getFarmerCode()+"/"+(farmSize+farmNameSeq));
				
				farm.setFarmName(vilSeqCode);
			
			}
			
			
			farmerService.addFarm(farm);
			saveDynamicField("359",String.valueOf(farm.getId()),farm.getFarmer().getSeasonCode(),String.valueOf(DynamicFeildMenuConfig.EntityTypes.FARM.ordinal()));
			
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)) {
				Type listType1 = new TypeToken<List<TreeDetail>>() {}.getType();
				
				   List<TreeDetail> treeDetailList = new Gson().fromJson(treeDetailJsonString, listType1);
			       if (!ObjectUtil.isEmpty(treeDetailList)) {
			    	   for (int i = 0; i < treeDetailList.size(); i++) {
			    		
			    		   TreeDetail treeDetail = new TreeDetail(); 
			    		   treeDetail.setProdStatus(treeDetailList.get(i).getProdStatus());
			    		   treeDetail.setVariety(treeDetailList.get(i).getVariety());
			    		   treeDetail.setYears(treeDetailList.get(i).getYears());
			    		   treeDetail.setNoOfTrees(treeDetailList.get(i).getNoOfTrees());
			    		   treeDetail.setFarm(farm);
			    		   farmerService.addTreeDetails(treeDetail);
			    		   JSONObject mainObj = new JSONObject();
				           mainObj.put("msg","success");
				           sendResponse(mainObj);
			    		  
			    	   }
			       }
				}

			/*
			 * if (farm.getFarmer().getBasicInfo() == 0) {
			 * farmerService.updateFarmerRevisionNo(Long.valueOf(farm.getFarmer(
			 * ).getId()), DateUtil.getRevisionNumber());
			 * 
			 * } else {
			 * farmerService.updateFarmerRevisionNoAndBasicInfo(Long.valueOf(
			 * farm.getFarmer().getId()), DateUtil.getRevisionNumber(),
			 * farm.getFarmer().getBasicInfo()); }
			 */
			
			return "farmerDetail";
		}
	}

	public Set<HousingInfo> setHousingInfo() {

		Set<HousingInfo> housiInfo = new HashSet<>();
		housiInfo.add(housingInfo);

		return housiInfo;

	}

	/*
	 * public Set<FarmerLandDetails>farmerLandDetails(){ Set<FarmerLandDetails>
	 * farmerLandDetails = new LinkedHashSet<>(); if
	 * (!StringUtil.isEmpty(farmerLandDetails)) { Type listType1 = new
	 * TypeToken<List<FarmerLandDetails>>() { }.getType(); String jsonString =
	 * farm.getJsonString();
	 * System.out.println("**********************"+FarmerseasonJsonString);
	 * List<FarmerLandDetails> farmerLandDetailsList = new
	 * Gson().fromJson(FarmerseasonJsonString, listType1); for (int i = 0; i<
	 * farmerLandDetailsList.size(); i++) { FarmerLandDetails fmd = new
	 * FarmerLandDetails();
	 * fmd.setIrrigatedTotLand(farmerLandDetailsList.get(i).getIrrigatedTotLand(
	 * )); fmd.setIrrigatedIfsPractices(farmerLandDetailsList.get(i).
	 * getIrrigatedIfsPractices());
	 * fmd.setRainfedTotLand(farmerLandDetailsList.get(i).getRainfedTotLand());
	 * fmd.setRanifedIfsPractices(farmerLandDetailsList.get(i).
	 * getRanifedIfsPractices()); fmd.setSeasonCode(
	 * farmerService.findHarvestSeasonBycodeusingname(farmerLandDetailsList.get(
	 * i).getSeasonCode())) ; farmerLandDetails.add(fmd); } } return
	 * farmerLandDetails; }
	 */
	public Set<FarmerSoilTesting> formSoilTestingSet() {

		Set<FarmerSoilTesting> formSoilTestingSet = new LinkedHashSet<>();
		if (!StringUtil.isEmpty(soilTestJson)) {
			Type listType1 = new TypeToken<List<FarmerSoilTesting>>() {
			}.getType();
			List<FarmerSoilTesting> farmerSoilTestingList = new Gson().fromJson(soilTestJson, listType1);
			formSoilTestingSet.addAll(farmerSoilTestingList);
		}

		return formSoilTestingSet;
	}

	public Set<Expenditure> formExpenditureSet() {

		Set<Expenditure> formExpenditureSet = new LinkedHashSet<>();
		if (!StringUtil.isEmpty(expenditureJson)) {
			Type expenditureType = new TypeToken<List<Expenditure>>() {
			}.getType();
			List<Expenditure> farmExpenditureList = new Gson().fromJson(expenditureJson, expenditureType);

			formExpenditureSet.addAll(farmExpenditureList);
		}

		return formExpenditureSet;
	}

	/**
	 * Update.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String updateActPlan() throws Exception {

		if (id != null && !id.equals("")) {
			farm = farmerService.findFarmById(Long.valueOf(id));
			if (farm == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			this.farmerId = String.valueOf(farm.getFarmer().getFarmerId());
			this.farmId = String.valueOf(farm.getId());
			setFarmerUniqueId(String.valueOf(farm.getFarmer().getId()));
			this.selectedFarmerId = farm.getFarmer().getFarmerId();
			setCurrentPage(getCurrentPage());
			id = null;
			command = ACTPLAN;
			request.setAttribute(HEADING, getText(ACTPLAN));

			farm.setFarmImageExist("0");
			if (farm.getPhoto() != null) {
				setFarmImageByteString(Base64Util.encoder(farm.getPhoto()));
				farm.setFarmImageExist("1");
			}
			if (getFarmerName() == null || getFarmerName().isEmpty()) {
				setFarmerName(farm.getFarmer().getName());
			}
			if ("1".equalsIgnoreCase(enableSoliTesting)) {
				if (!ObjectUtil.isListEmpty(farm.getDocUpload())) {
					farm.setDocUploadList(new ArrayList<DocumentUpload>(farm.getDocUpload()));
				}
			}

			if (!ObjectUtil.isEmpty(farm)) {
				if (!ObjectUtil.isEmpty(farm.getVillage())
						&& !ObjectUtil.isEmpty(farm.getVillage().getGramPanchayat())) {
					if (!ObjectUtil.isEmpty(farm.getVillage().getGramPanchayat().getCity())
							&& !ObjectUtil.isEmpty(farm.getVillage().getGramPanchayat().getCity().getLocality())) {
						setSelectedLocality(
								String.valueOf(farm.getVillage().getGramPanchayat().getCity().getLocality().getId()));
					}
					if (!ObjectUtil.isEmpty(farm.getVillage().getGramPanchayat().getCity())) {
						setSelectedCity(String.valueOf(farm.getVillage().getGramPanchayat().getCity().getId()));
					}
					setSelectedPanchayat(String.valueOf(farm.getVillage().getGramPanchayat().getId()));

					setSelectedVillage(String.valueOf(farm.getVillage().getId()));
					if (farm.getSamithi() != null) {
						setSelectedSamithi(String.valueOf(farm.getSamithi().getId()));
					}
					if (farm.getFpo() != null) {
						setSelectedFpo(String.valueOf(farm.getFpo()));
					}
				}

			}

			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.BLRI_TENANT_ID)) {
				housingInfo = farmerService.findByHousingInfo(farm.getId());

			}
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.COFBOARD_TENANT_ID)) {
			farm.setPresenceBananaTrees(farm.getPresenceBananaTrees());
			farm.setDistanceProcessingUnit(farm.getDistanceProcessingUnit());
			}
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)) {
				
					
				if(farm.getTreeDetails().size()>0){
						//List<SeedTreatment> seedTreat = farmerService.getSeedTreatmentById()
						for(TreeDetail val :farm.getTreeDetails()){
							val.setVarietyId(val.getVariety());
							val.setProdStatusId(val.getProdStatus());
							val.setYearsId(val.getYears());
							val.setProdStatus(getText("productStatus-"+val.getProdStatus()));
							val.setYears(getCatlogueValueByCode(val.getYears()).getName());
							ProcurementVariety variety=productDistributionService.findProcurementVariertyByCode(val.getVariety());
							val.setVariety(variety.getName());
						
						}
					}	
					farm.setPresenceBananaTrees(farm.getPresenceBananaTrees());
					farm.setParallelProd(farm.getParallelProd());
					farm.setPresenceHiredLabour(farm.getPresenceHiredLabour());
					farm.setRiskCategory(farm.getRiskCategory());
					farm.setInputOrganicUnit(farm.getInputOrganicUnit());
					//farm.setLeasedLand(farm.getLeasedLand());
				}

			if (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo())) {

				setSelectedLandMeasurement(farm.getFarmDetailedInfo().getLandMeasurement());
				selectedFarmOwned = (farm.getFarmDetailedInfo().getFarmOwned() == SELECT_MULTI) ? SELECT_MULTI
						: farm.getFarmDetailedInfo().getFarmOwned();
				selectedIrrigation = (farm.getFarmDetailedInfo().getFarmIrrigation() == SELECT_MULTI) ? SELECT_MULTI
						: farm.getFarmDetailedInfo().getFarmIrrigation();
				selectedIrrigationSource = (farm.getFarmDetailedInfo().getIrrigationSource() == SELECT_MULTI)
						? SELECT_MULTI : farm.getFarmDetailedInfo().getIrrigationSource();
				// selectedIrrigationMethod =
				// (farm.getFarmDetailedInfo().getIrrigationMethod() ==
				// SELECT) ? SELECT :
				// farm.getFarmDetailedInfo().getIrrigationMethod();
				selectedSoilType = (farm.getFarmDetailedInfo().getSoilType() == SELECT_MULTI) ? SELECT_MULTI
						: farm.getFarmDetailedInfo().getSoilType();

				selectedMethodOfIrrigation = (farm.getFarmDetailedInfo().getMethodOfIrrigation() == SELECT_MULTI)
						? SELECT_MULTI : farm.getFarmDetailedInfo().getMethodOfIrrigation();

				selectedSoilFertility = (farm.getFarmDetailedInfo().getSoilFertility() == SELECT) ? SELECT
						: farm.getFarmDetailedInfo().getSoilFertility();
				selectedICSStatus = (farm.getFarmDetailedInfo().getLandUnderICSStatus() == SELECT) ? SELECT
						: farm.getFarmDetailedInfo().getLandUnderICSStatus();
				selectedGradient = (farm.getFarmDetailedInfo().getLandGradient() == SELECT_MULTI) ? SELECT_MULTI
						: farm.getFarmDetailedInfo().getLandGradient();
				selectedTexture = (farm.getFarmDetailedInfo().getSoilTexture() == SELECT_MULTI) ? SELECT_MULTI
						: farm.getFarmDetailedInfo().getSoilTexture();
				selectedRoad = (farm.getFarmDetailedInfo().getApproachRoad() == SELECT_MULTI) ? SELECT_MULTI
						: farm.getFarmDetailedInfo().getApproachRoad();

				if (farm.getFarmDetailedInfo().getSessionYear() != null) {
					sessionYearString = farm.getFarmDetailedInfo().getSessionYear();
				}
				if (farm.getFarmDetailedInfo().getRegYear() != null) {
					regYearString = farm.getFarmDetailedInfo().getRegYear();
				}

				/* APMAS Plant Information */
				if (getCurrentTenantId().equalsIgnoreCase("atma")) {

					farmerSoilTestingList = farmerService.listFarmerSoilTestingByFarmId(String.valueOf(farm.getId()));

					JSONArray objArray = new JSONArray();
					FarmerPlants plantList = new FarmerPlants();

					if (!ObjectUtil.isEmpty(farm.getFarmerPlants())) {

						for (FarmerPlants plantLists : farm.getFarmerPlants()) {

							JSONObject getObj = new JSONObject();

							if (!StringUtil.isEmpty(plantLists.getPlants())) {
								getObj.put("selectedfarmerPlants", plantLists.getPlants());
							}

							if (!StringUtil.isEmpty(plantLists.getNoOfPlants())) {
								getObj.put("selectedplantedPlants", plantLists.getNoOfPlants());
							}

							if (!StringUtil.isEmpty(plantLists.getNoOfLive())) {
								getObj.put("selectednoOfLivePlants", plantLists.getNoOfLive());
							}

							objArray.add(getObj);
						}
						getplantDetailsList = new Gson().toJson(objArray);
					}

					JSONArray array = new JSONArray();
					if (!ObjectUtil.isEmpty(farm.getFarmerScheme())) {
						for (FarmerScheme scheme : farm.getFarmerScheme()) {
							FarmCatalogue cat = getCatlogueValueByCode(scheme.getBenefitDetails());
							FarmCatalogue depSch = getCatlogueValueByCode(scheme.getDepartmentName());
							JSONObject obj = new JSONObject();
							SimpleDateFormat dt1 = new SimpleDateFormat("MM/dd/yyyy");
							SimpleDateFormat dt2 = new SimpleDateFormat("yyyy-MM-dd");

							if (!ObjectUtil.isEmpty(cat)) {
								obj.put("selectedBenefitFarmer", cat.getCode() + "-" + cat.getName());
							}
							if (!StringUtil.isEmpty(scheme.getNoOfKgs())) {
								obj.put("selectedkgs", scheme.getNoOfKgs());
							}
							if (!ObjectUtil.isEmpty(depSch)) {
								obj.put("selectedScheme", depSch.getCode() + "-" + depSch.getName());
							}
							if (!StringUtil.isEmpty(scheme.getReceivedDate())) {
								String dateString = scheme.getReceivedDate().toString().split("\\ ")[0];
								Date date = dt2.parse(dateString);
								obj.put("receivedDate", dt1.format(date));
							}
							if (!StringUtil.isEmpty(scheme.getReceivedAmt())) {
								obj.put("selectedAmt", scheme.getReceivedAmt());
							}
							if (!StringUtil.isEmpty(scheme.getContributionAmt())) {
								obj.put("selectedcontribute", scheme.getContributionAmt());
							}
							array.add(obj);

						}
						prodEditArray = new Gson().toJson(array);

					}
				}

				/*
				 * if
				 * (farm.getFarmDetailedInfo().getLastDateOfChemicalApplication(
				 * ) != null) { chemicalString =
				 * farm.getFarmDetailedInfo().getLastDateOfChemicalApplication()
				 * ; }
				 */
				FarmDetailedInfo farmDetailedInfo = new FarmDetailedInfo();
				if (farm.getFarmDetailedInfo().isSameAddressofFarmer()) {
					farmDetailedInfo.setSameAddressofFarmer(true);
					farmDetailedInfo.setFarmAddress(
							farm.getFarmer().getAddress() + " " + farm.getFarmer().getVillage().getCity().getName() + ""
									+ farm.getFarmer().getVillage().getName());
					if (farmDetailedInfo.getFarmAddress().trim().length() > 255)
						farmDetailedInfo.setFarmAddress(!StringUtil.isEmpty(farm.getFarmer().getAddress())
								? farm.getFarmer().getAddress() : "");
				} else
					farmDetailedInfo.setFarmAddress(!StringUtil.isEmpty(farm.getFarmDetailedInfo().getFarmAddress())
							? farm.getFarmDetailedInfo().getFarmAddress() : "");

				if (farm.getFarmDetailedInfo() != null
						&& StringUtil.isEmpty(farm.getFarmDetailedInfo().getProposedPlantingArea())) {
					farm.getFarmDetailedInfo().setProposedPlantingArea("0");
				}
				
				Format format = new SimpleDateFormat("dd-MM-yyyy");
				farm.getFarmDetailedInfo().setLastDateOfChemicalString(StringUtil.isEmpty(farm.getFarmDetailedInfo().getLastDateOfChemical()) ? ""
						: format.format(farm.getFarmDetailedInfo().getLastDateOfChemical()));

				/*
				 * if (farm.getFarmDetailedInfo().getInternalInspectionDate() !=
				 * null) { dateOfInspection =
				 * df.format(farm.getFarmDetailedInfo()
				 * .getInternalInspectionDate()); }
				 */

				if (!ObjectUtil.isListEmpty(farm.getFarmICS())) {
					List<FarmICS> tempListICS = Arrays.asList(new FarmICS[4]);
					farm.setFarmICSList(new LinkedList<FarmICS>(farm.getFarmICS()));
					for (FarmICS farmICS : farm.getFarmICS()) {
						if (!StringUtil.isEmpty(farmICS.getBeginOfConversion())) {
							Format formatter = new SimpleDateFormat("MM/dd/yyyy");
							String date = formatter.format(farmICS.getBeginOfConversion());
							farmICS.setBeginOfConversionString(date);

						}
						tempListICS.set(farmICS.getIcsType(), farmICS);
					}
					farm.setFarmICSList(tempListICS);
				}
				FarmIcsConversion icds = new FarmIcsConversion();
				if (farm.getFarmICSConversion() != null && farm.getFarmICSConversion().size() > 0) {
					for (FarmIcsConversion icd : farm.getFarmICSConversion()) {
						Format formatter = new SimpleDateFormat("dd-MM-yyyy");

						icd.setInspectionDateString(StringUtil.isEmpty(icd.getInspectionDate()) ? ""
								: formatter.format(icd.getInspectionDate()));
						/*
						 * icd.setLastDateChemicalAppString(StringUtil.isEmpty(
						 * icd. getLastDateChemicalApp()) ? "" :
						 * formatter.format(icd.getLastDateChemicalApp()));
						 */

						icd.setOrganicStatus(icd.getOrganicStatus());

						farm.setFarmIcsConv(icd);
					}
				} else {

					// icds.setOrganicStatus(getLocaleProperty("inconversion"));

					farm.setFarmIcsConv(icds);
				}

			} else {
				setSelectedLandMeasurement(FarmDetailedInfo.HECTARES);
				setSelectedFarmOwned(SELECT_MULTI);
				setSelectedIrrigation(SELECT_MULTI);
				setSelectedIrrigationSource(SELECT_MULTI);
				// setSelectedIrrigationMethod(SELECT);
				setSelectedSoilType(SELECT_MULTI);
				setSelectedMethodOfIrrigation(SELECT_MULTI);
				setSelectedSoilFertility(SELECT);
				setSelectedICSStatus(SELECT);
			}

			if (getCurrentTenantId().equalsIgnoreCase("atma")) {
				updatefarmerLandDetailsList = farmerService.listFarmingSystemByFarmId(farm.getId());
				for (FarmerLandDetails details : updatefarmerLandDetailsList) {
					setHarvestSeason(details.getSeasonCode());
				}
			}
			
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)) {
				Set<TreeDetail> treeSet = new HashSet<>();
				List<TreeDetail> tree = farmerService.findTreeDetailByFarmId(farm.getId());
				if (!ObjectUtil.isListEmpty(tree)) {
					treeSet.addAll(tree);
				}
				if (!ObjectUtil.isEmpty(treeSet)) {
					farm.setJsonString(treeDetailToJson(treeSet));
				}
				
			}
			expenditureList = farmerService.listExpentitureListByFarmId(Long.valueOf(farm.getId()));
		} else {
			if (farm != null) {
				Farm existing = farmerService.findFarmById(farm.getId());
				if (getFarmerName() == null || getFarmerName().isEmpty()) {
					setFarmerName(farm.getFarmer().getName());
				}
				if (existing == null) {
					addActionError(NO_RECORD);
					return REDIRECT;
				}
				setCurrentPage(getCurrentPage());
				if (StringUtil.isEmpty(existing.getFarmCode()))
					existing.setFarmCode(String.valueOf(DateUtil.getRevisionNoDateTimeMilliSec()));
				existing.setFarmName(farm.getFarmName());

				// if (getFarm().getFarmImage()== null ||
				// getFarm().getFarmImageExist()=="0") {
				/*
				 * if (getFarm().getFarmImageExist().equals("0")) {
				 * addActionError(getText("empty.photo")); return INPUT; }
				 */

				if (getFarm().getFarmImage() != null) {
					existing.setPhoto(FileUtil.getBinaryFileContent(getFarm().getFarmImage()));
					existing.setFarmImageFileName(getFarm().getFarmImageFileName());
					existing.getFarmer().setBasicInfo(1);

				}
				existing.setLandmark(farm.getLandmark()); // set farm landmark
															// while updating.
				if ("1".equalsIgnoreCase(enableSoliTesting)) {
					Set<DocumentUpload> documentUploadSet = new HashSet<DocumentUpload>();
					if (!ObjectUtil.isListEmpty(farm.getDocUploadList())) {
						for (DocumentUpload document : farm.getDocUploadList()) {
							if (!StringUtil.isEmpty(document.getDocFile())) {
								document.setFarm(farm);
								document.setContent(FileUtil.getBinaryFileContent(document.getDocFile()));
								document.setName(document.getDocFileFileName());
								documentUploadSet.add(document);

							}

						}
						existing.setDocUpload(null);
						if (!ObjectUtil.isListEmpty(documentUploadSet))
							existing.setDocUpload(documentUploadSet);

					}
				}

				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.BLRI_TENANT_ID)) {
					existing.setHousingInfos(setHousingInfo());
				}
				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.COFBOARD_TENANT_ID)) {
					existing.setPresenceBananaTrees(getFarm().getPresenceBananaTrees());
					existing.setDistanceProcessingUnit(getFarm().getDistanceProcessingUnit());
					}
				/*
				 * if (existing.getFarmer().getCertificationType() ==
				 * Farmer.CERTIFICATION_TYPE_NONE) { if
				 * (!ObjectUtil.isListEmpty(existing.getFarmCrops())) {//
				 * Validation with Farm // Crops try { Double areaInProduction =
				 * Double.parseDouble(landInProduction); List<FarmCrops>
				 * farmCrops = new ArrayList<FarmCrops>(existing
				 * .getFarmCrops()); Double cropArea =
				 * Double.parseDouble(farmCrops.get(0).getCropArea()); for
				 * (FarmCrops farmCrop : existing.getFarmCrops()) { Double
				 * innerCropArea = Double.parseDouble(farmCrop.getCropArea());
				 * if (innerCropArea > cropArea) { cropArea = innerCropArea; } }
				 * if (cropArea > areaInProduction) {
				 * addActionError(getText("can't.update")); return INPUT; } }
				 * catch (Exception e) {
				 * addActionError(getText("error.processing")); return INPUT; }
				 * }// end try {// Total Area Double totalAreaOfProduction =
				 * Double.parseDouble(landInProduction) +
				 * (StringUtil.isEmpty(landNotInProduction) ? 0 : Double
				 * .parseDouble(landNotInProduction)); this.totalArea =
				 * CurrencyUtil.getDecimalFormat(totalAreaOfProduction,
				 * THREE_DECIMAL_FORMATTER); existing.setHectares(totalArea); }
				 * catch (Exception e) { this.totalArea = "0";
				 * existing.setHectares(this.totalArea); }
				 * existing.setLandInProduction(landInProduction);
				 * existing.setLandNotInProduction(landNotInProduction);
				 * existing.setFarmDetailedInfo(null); command = INPUT; }
				 */
				if (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo())) {

					FarmDetailedInfo farmDetailedInfo = new FarmDetailedInfo();
					if (farm.getFarmDetailedInfo().isSameAddressofFarmer()) {
						farmDetailedInfo.setSameAddressofFarmer(true);
						farmDetailedInfo.setFarmAddress(farm.getFarmer().getAddress() + " "
								+ farm.getFarmer().getCity().getName() + "" + farm.getFarmer().getVillage().getName());
						if (farmDetailedInfo.getFarmAddress().trim().length() > 255)
							farmDetailedInfo.setFarmAddress(farm.getFarmer().getAddress());
					} else
						farmDetailedInfo.setFarmAddress(!StringUtil.isEmpty(farm.getFarmDetailedInfo().getFarmAddress())
								? farm.getFarmDetailedInfo().getFarmAddress() : "");

					farmDetailedInfo
							.setLandMeasurement(!StringUtil.isEmpty(farm.getFarmDetailedInfo().getLandMeasurement())
									? farm.getFarmDetailedInfo().getLandMeasurement() : "");
					farmDetailedInfo.setFarmArea(!StringUtil.isEmpty(farm.getFarmDetailedInfo().getFarmArea())
							? farm.getFarmDetailedInfo().getFarmArea() : "");
					farmDetailedInfo.setFarmProductiveArea(
							!StringUtil.isEmpty(farm.getFarmDetailedInfo().getFarmProductiveArea())
									? farm.getFarmDetailedInfo().getFarmProductiveArea() : "");
					farmDetailedInfo.setFarmConservationArea(
							!StringUtil.isEmpty(farm.getFarmDetailedInfo().getFarmConservationArea())
									? farm.getFarmDetailedInfo().getFarmConservationArea() : "");
					farmDetailedInfo
							.setWaterBodiesCount(!StringUtil.isEmpty(farm.getFarmDetailedInfo().getWaterBodiesCount())
									? farm.getFarmDetailedInfo().getWaterBodiesCount() : "");
					farmDetailedInfo.setFullTimeWorkersCount(
							!StringUtil.isEmpty(farm.getFarmDetailedInfo().getFullTimeWorkersCount())
									? farm.getFarmDetailedInfo().getFullTimeWorkersCount() : "");
					farmDetailedInfo.setPartTimeWorkersCount(
							!StringUtil.isEmpty(farm.getFarmDetailedInfo().getPartTimeWorkersCount())
									? farm.getFarmDetailedInfo().getPartTimeWorkersCount() : "");
					farmDetailedInfo.setSeasonalWorkersCount(
							!StringUtil.isEmpty(farm.getFarmDetailedInfo().getSeasonalWorkersCount())
									? farm.getFarmDetailedInfo().getSeasonalWorkersCount() : "");
					farmDetailedInfo.setFarmProductiveArea(
							!StringUtil.isEmpty(farm.getFarmDetailedInfo().getFarmProductiveArea())
									? farm.getFarmDetailedInfo().getFarmProductiveArea() : "");
					farmDetailedInfo
							.setFarmOwned((selectedFarmOwned != SELECT_MULTI) ? selectedFarmOwned : SELECT_MULTI);
					farmDetailedInfo.setFarmIrrigation(
							(selectedIrrigation != SELECT_MULTI) ? selectedIrrigation : SELECT_MULTI);
					farmDetailedInfo.setIrrigationSource(
							(selectedIrrigationSource != SELECT_MULTI) ? selectedIrrigationSource : SELECT_MULTI);
					// farmDetailedInfo.setIrrigationMethod((selectedIrrigationMethod
					// != SELECT ?
					// selectedIrrigationMethod : SELECT));
					farmDetailedInfo.setSoilType((selectedSoilType != SELECT_MULTI) ? selectedSoilType : SELECT_MULTI);
					farmDetailedInfo.setMethodOfIrrigation(
							(selectedMethodOfIrrigation != SELECT_MULTI) ? selectedMethodOfIrrigation : SELECT_MULTI);
					farmDetailedInfo
							.setSoilFertility((selectedSoilFertility != SELECT) ? selectedSoilFertility : SELECT);

					farmDetailedInfo
							.setLandGradient((selectedGradient != SELECT_MULTI) ? selectedGradient : SELECT_MULTI);

					farmDetailedInfo.setSoilTexture((selectedTexture != SELECT_MULTI) ? selectedTexture : SELECT_MULTI);
					farmDetailedInfo.setApproachRoad((selectedRoad != SELECT_MULTI) ? selectedRoad : SELECT_MULTI);

					farmDetailedInfo.setAreaUnderIrrigation(
							!StringUtil.isEmpty(farm.getFarmDetailedInfo().getAreaUnderIrrigation())
									? farm.getFarmDetailedInfo().getAreaUnderIrrigation() : "");
					farmDetailedInfo.setLastDateOfChemicalApplication(
							!StringUtil.isEmpty(farm.getFarmDetailedInfo().getLastDateOfChemicalApplication())
									? farm.getFarmDetailedInfo().getLastDateOfChemicalApplication() : "");
					farmDetailedInfo
							.setBeginOfConversion(!StringUtil.isEmpty(farm.getFarmDetailedInfo().getBeginOfConversion())
									? farm.getFarmDetailedInfo().getBeginOfConversion() : "");
					/*
					 * if (!StringUtil.isEmpty(dateOfInspection)) { DateFormat
					 * format = new SimpleDateFormat("MM/dd/yyyy"); try {
					 * farmDetailedInfo.setInternalInspectionDate((Date) format
					 * .parse(dateOfInspection)); } catch (ParseException e) {
					 * e.printStackTrace(); } }
					 */
					farmDetailedInfo.setInternalInspectorName(
							!StringUtil.isEmpty(farm.getFarmDetailedInfo().getInternalInspectorName())
									? farm.getFarmDetailedInfo().getInternalInspectorName() : "");
					farmDetailedInfo.setSurveyNumber(!StringUtil.isEmpty(farm.getFarmDetailedInfo().getSurveyNumber())
							? farm.getFarmDetailedInfo().getSurveyNumber() : "");
					farmDetailedInfo.setLandUnderICSStatus((selectedICSStatus != SELECT) ? selectedICSStatus : SELECT);
					farmDetailedInfo.setFallowOrPastureLand(
							!StringUtil.isEmpty(farm.getFarmDetailedInfo().getFallowOrPastureLand())
									? farm.getFarmDetailedInfo().getFallowOrPastureLand() : "");
					farmDetailedInfo
							.setConventionalLand(!StringUtil.isEmpty(farm.getFarmDetailedInfo().getConventionalLand())
									? farm.getFarmDetailedInfo().getConventionalLand() : "");
					farmDetailedInfo
							.setConventionalCrops(!StringUtil.isEmpty(farm.getFarmDetailedInfo().getConventionalCrops())
									? farm.getFarmDetailedInfo().getConventionalCrops() : "");
					farmDetailedInfo.setConventionalEstimatedYield(
							!StringUtil.isEmpty(farm.getFarmDetailedInfo().getConventionalEstimatedYield())
									? farm.getFarmDetailedInfo().getConventionalEstimatedYield() : "");
					farmDetailedInfo.setNc(!StringUtil.isEmpty(farm.getFarmDetailedInfo().getNc())
							? farm.getFarmDetailedInfo().getNc() : "");

					farmDetailedInfo
							.setTotalLandHolding(!StringUtil.isEmpty(farm.getFarmDetailedInfo().getTotalLandHolding())
									? formatter.format(Double.valueOf(farm.getFarmDetailedInfo().getTotalLandHolding()))
									: "");

					farmDetailedInfo.setProposedPlantingArea(
							!StringUtil.isEmpty(farm.getFarmDetailedInfo().getProposedPlantingArea()) ? formatter
									.format(Double.valueOf(farm.getFarmDetailedInfo().getProposedPlantingArea())) : "");

					farmDetailedInfo.setRainFedValue(!StringUtil.isEmpty(farm.getFarmDetailedInfo().getRainFedValue())
							? farm.getFarmDetailedInfo().getRainFedValue() : "");

					farmDetailedInfo
							.setIrrigatedOther(!StringUtil.isEmpty(farm.getFarmDetailedInfo().getIrrigatedOther())
									? farm.getFarmDetailedInfo().getIrrigatedOther() : "");

					farmDetailedInfo.setFarmerFieldSchool(farm.getFarmDetailedInfo().getFarmerFieldSchool());
					farmDetailedInfo.setIsFFSBenifited(farm.getFarmDetailedInfo().getIsFFSBenifited());

					farmDetailedInfo.setMilletCultivated(farm.getFarmDetailedInfo().getMilletCultivated());
					farmDetailedInfo.setMilletCropType(farm.getFarmDetailedInfo().getMilletCropType());
					farmDetailedInfo.setMilletCropCount(farm.getFarmDetailedInfo().getMilletCropCount());

					farmDetailedInfo
							.setBoreWellRechargeStructure(farm.getFarmDetailedInfo().getBoreWellRechargeStructure());

					farmDetailedInfo.setFieldName(farm.getFarmDetailedInfo().getFieldName());

					farmDetailedInfo.setFieldCrop(farm.getFarmDetailedInfo().getFieldCrop());

					farmDetailedInfo.setFieldArea(farm.getFarmDetailedInfo().getFieldArea());

					farmDetailedInfo.setInputApplied(farm.getFarmDetailedInfo().getInputApplied());

					farmDetailedInfo.setQuantityApplied(farm.getFarmDetailedInfo().getQuantityApplied());

					farmDetailedInfo.setInputSource(farm.getFarmDetailedInfo().getInputSource());

					farmDetailedInfo
							.setActivitiesInCoconutFarming(farm.getFarmDetailedInfo().getActivitiesInCoconutFarming());

					farmDetailedInfo.setProcessingActivity(farm.getFarmDetailedInfo().getProcessingActivity());
					farmDetailedInfo.setProcessingActivity(farm.getFarmDetailedInfo().getProcessingActivity());
					if (farm.getFarmDetailedInfo().getLastDateOfChemicalString() != null
							&& !farm.getFarmDetailedInfo().getLastDateOfChemicalString().isEmpty()) {
						ESESystem preferences = preferncesService.findPrefernceById("1");
						farmDetailedInfo
								.setLastDateOfChemical(DateUtil.convertStringToDate(
										farm.getFarmDetailedInfo().getLastDateOfChemicalString(), preferences
												.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT)));
					}
					/*
					 * farmDetailedInfo
					 * .setApproachRoad(!StringUtil.isEmpty(farm.
					 * getFarmDetailedInfo() .getApproachRoad()) ?
					 * farm.getFarmDetailedInfo() .getApproachRoad() : "");
					 */
					/*
					 * farmDetailedInfo.setRegYear(!StringUtil.isEmpty(farm.
					 * getFarmDetailedInfo() .getRegYear()) ?
					 * farm.getFarmDetailedInfo().getRegYear() : "");
					 */
					if (!StringUtil.isEmpty(sessionYearString))
						farmDetailedInfo.setSessionYear(sessionYearString);
					if (!StringUtil.isEmpty(regYearString))
						farmDetailedInfo.setRegYear(regYearString);
					/*
					 * if(!StringUtil.isEmpty(chemicalString))
					 * farmDetailedInfo.setLastDateOfChemicalApplication(
					 * chemicalString);
					 */

					if (!ObjectUtil.isListEmpty(farm.getFarmICSList())) {
						Set<FarmICS> icsUpdatedSet = new LinkedHashSet<FarmICS>();
						int i = 0;// Initializing Type Value
						boolean icsExist = false;
						for (int j = 0; j < farm.getFarmICSList().size(); j++) {
							if (!StringUtil.isEmpty(farm.getFarmICSList().get(j).getLandIcsDetails())
									|| !StringUtil.isEmpty(farm.getFarmICSList().get(j).getSurveyNo())
									|| !StringUtil.isEmpty(farm.getFarmICSList().get(j).getBeginOfConversionString())) {
								icsExist = true;
							}
						}
						if (icsExist) {

							for (FarmICS newFarmICS : farm.getFarmICSList()) {

								newFarmICS.setIcsType(i);
								boolean isExist = false;
								for (FarmICS existingFarmICS : existing.getFarmICS()) {
									if (newFarmICS.getIcsType() == existingFarmICS.getIcsType()) {
										isExist = true;

										if (!StringUtil.isEmpty(newFarmICS.getLandIcsDetails())
												|| !StringUtil.isEmpty(newFarmICS.getBeginOfConversionString())
												|| !StringUtil.isEmpty(newFarmICS.getSurveyNo())) {
											existingFarmICS.setLandIcsDetails(newFarmICS.getLandIcsDetails());

											if (!StringUtil.isEmpty(newFarmICS.getBeginOfConversionString()))
												existingFarmICS.setBeginOfConversion(DateUtil.convertStringToDate(
														newFarmICS.getBeginOfConversionString(), "MM/dd/yyyy"));

											existingFarmICS.setSurveyNo(newFarmICS.getSurveyNo());
											icsUpdatedSet.add(existingFarmICS);
										}
									}
								}
								if (!isExist) {// doesnt exist
									if (!StringUtil.isEmpty(newFarmICS.getLandIcsDetails())
											|| !StringUtil.isEmpty(newFarmICS.getBeginOfConversionString())
											|| !StringUtil.isEmpty(newFarmICS.getSurveyNo())) {

										if (!StringUtil.isEmpty(newFarmICS.getBeginOfConversionString()))
											newFarmICS.setBeginOfConversion(DateUtil.convertStringToDate(
													newFarmICS.getBeginOfConversionString(), "MM/dd/yyyy"));

										icsUpdatedSet.add(newFarmICS);
									}
								}
								i++;
							}
						}
						existing.setFarmICS(icsUpdatedSet);

					}
					ESESystem preferences = preferncesService.findPrefernceById("1");
					Set<FarmIcsConversion> ics = new HashSet<FarmIcsConversion>();
					if (farm.getFarmer().getIsCertifiedFarmer() == 1) {
						if (farm.getFarmIcsConv() != null) {

							existing.getFarmICSConversion().stream().forEach(u -> {
								u.setIsActive(0);
							});
							farm.getFarmIcsConv().setIsActive(1);
							if (!StringUtil.isEmpty(farm.getFarmIcsConv().getIcsType())
									&& !StringUtil.isEmpty(farm.getFarmIcsConv().getScope())
									&& !StringUtil.isEmpty(farm.getFarmIcsConv().getInspectionDateString())) {
								List<FarmIcsConversion> fcExis = existing.getFarmICSConversion().stream()
										.filter(u -> u.getIcsType().equalsIgnoreCase(farm.getFarmIcsConv().getIcsType())
												&& u.getScope().equalsIgnoreCase(farm.getFarmIcsConv().getScope())
												&& u.getInspectionDate()!=null && DateUtil.getYearByDateTime(u.getInspectionDate()).equalsIgnoreCase(
														DateUtil.getYearByDateTime(DateUtil.convertStringToDate(
																farm.getFarmIcsConv().getInspectionDateString(),
																getGeneralDateFormat()))))
										.collect(Collectors.toList());
								existing.getFarmICSConversion().removeAll(fcExis);
							}

							if (farm.getFarmIcsConv().getInspectionDateString() != null
									&& !farm.getFarmIcsConv().getInspectionDateString().isEmpty()) {
								farm.getFarmIcsConv()
										.setInspectionDate(DateUtil.convertStringToDate(
												farm.getFarmIcsConv().getInspectionDateString(), preferences
														.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT)));
							} else {
								farm.getFarmIcsConv().setInspectionDate(new Date());
							}
							if (farm.getFarmer().getIsCertifiedFarmer() == 1
									&& !ObjectUtil.isEmpty(farm.getFarmIcsConv().getIcsType())&& farm.getFarmIcsConv().getIcsType().equalsIgnoreCase("3")) {
								farm.getFarmIcsConv().setOrganicStatus("3");
							} else {
								farm.getFarmIcsConv().setOrganicStatus("0");
							}
							farm.getFarmIcsConv().setFarmer(farm.getFarmer());
							
							if (getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)){
								farm.getFarmIcsConv().setIcsType("0");
							}
							
							existing.getFarmICSConversion().add(farm.getFarmIcsConv());

							/*
							 * 
							 * else{ if (farm.getFarmIcsConv().getIcsType() !=
							 * null &&
							 * !farm.getFarmIcsConv().getIcsType().isEmpty()) {
							 * 
							 * if(farm.getFarmIcsConv().
							 * getLastDateChemicalAppString()!=null &&
							 * !farm.getFarmIcsConv().
							 * getLastDateChemicalAppString().isEmpty()){
							 * farm.getFarmIcsConv().setLastDateChemicalApp(
							 * DateUtil.
							 * convertStringToDate(farm.getFarmIcsConv().
							 * getLastDateChemicalAppString(), "MM/dd/yyyy")); }
							 * 
							 * if
							 * (farm.getFarmIcsConv().getInspectionDateString()
							 * != null &&
							 * !farm.getFarmIcsConv().getInspectionDateString().
							 * isEmpty()) { farm.getFarmIcsConv()
							 * .setInspectionDate(DateUtil.convertStringToDate(
							 * farm.getFarmIcsConv().getInspectionDateString(),
							 * preferences .getPreferences().get(ESESystem.
							 * GENERAL_DATE_FORMAT))); } else {
							 * farm.getFarmIcsConv().setInspectionDate(null); }
							 * 
							 * if
							 * (getCurrentTenantId().equalsIgnoreCase("wilmar"))
							 * { if (farm.getFarmer().getIsCertifiedFarmer() ==
							 * 1 && farm.getFarmIcsConv().getIcsType().
							 * equalsIgnoreCase("3")) {
							 * farm.getFarmIcsConv().setOrganicStatus(getText(
							 * "icsStatus3")); } else {
							 * farm.getFarmIcsConv().setOrganicStatus(getText(
							 * "inconversion")); } }
							 * ics.add(farm.getFarmIcsConv()); }
							 * existing.setFarmICSConversion(ics); }
							 */

						}
					}

					if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)
							|| getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID) || getCurrentTenantId().equalsIgnoreCase(ESESystem.COFBOARD_TENANT_ID)) {
						existing.setDistanceProcessingUnit(farm.getDistanceProcessingUnit());
						farmDetailedInfo.setProcessingActivity(farm.getFarmDetailedInfo().getProcessingActivity());
						existing.setAvgStore(farm.getAvgStore());
						existing.setTreeName(farm.getTreeName());
					}
					existing.setSoilTesting(farm.getSoilTesting());
					farmerService.addFarmDetailedInfo(farmDetailedInfo);
					existing.setFarmDetailedInfo(farmDetailedInfo);
					command = "farmerDetail";
				}
				existing.setExpenditures(formExpenditureSet());
				existing.setLatitude(farm.getLatitude());
				existing.setLongitude(farm.getLongitude());
				List<FarmerLandDetails> existingLandDetailList = new ArrayList<FarmerLandDetails>();
				Set<FarmerLandDetails> updatableSet = new HashSet<FarmerLandDetails>();
				Set<FarmerLandDetails> updatableList = null;
				if (!ObjectUtil.isListEmpty(updatefarmerLandDetailsList)) {
					existingLandDetailList = farmerService.listFarmingSystemByFarmId(farm.getId());
					for (FarmerLandDetails farmerLandDetail : updatefarmerLandDetailsList) {
						if (!ObjectUtil.isEmpty(farmerLandDetail)) {
							FarmerLandDetails existingLandDetail = farmerService
									.findFarmerLandDetailsById(farmerLandDetail.getId());
							if (!ObjectUtil.isEmpty(existingLandDetail)) {
								existingLandDetail.setIrrigatedTotLand(farmerLandDetail.getIrrigatedTotLand());
								existingLandDetail
										.setIrrigatedIfsPractices(farmerLandDetail.getIrrigatedIfsPractices());
								existingLandDetail.setRainfedTotLand(farmerLandDetail.getRainfedTotLand());
								existingLandDetail.setRanifedIfsPractices(farmerLandDetail.getRanifedIfsPractices());
								existingLandDetail.setSeasonCode(harvestSeason);
								updatableSet.add(existingLandDetail);
							}
						}
					}
					// updatableSet=new HashSet<>(existingLandDetailList);
					// tempFarmerLandDetailList.addAll(existingLandDetail);
				}
				if (!ObjectUtil.isListEmpty(farmerLandDetailsList)) {
					Set<FarmerLandDetails> framerLandDetails = new HashSet<FarmerLandDetails>();
					for (FarmerLandDetails farmerLandDetail : farmerLandDetailsList) {
						if (!ObjectUtil.isEmpty(farmerLandDetail)) {
							FarmerLandDetails LandDetails = new FarmerLandDetails();
							LandDetails.setIrrigatedTotLand(!StringUtil.isEmpty(farmerLandDetail.getIrrigatedLand())
									? Double.parseDouble(farmerLandDetail.getIrrigatedLand()) : 0.00);
							LandDetails.setIrrigatedIfsPractices(farmerLandDetail.getIrrigatedFarmingLand());
							LandDetails.setRainfedTotLand(!StringUtil.isEmpty(farmerLandDetail.getFedtotaland())
									? Double.parseDouble(farmerLandDetail.getFedtotaland()) : 0.00);
							LandDetails.setRanifedIfsPractices(farmerLandDetail.getFedtotalics());
							LandDetails.setSeasonCode(farmerLandDetail.getTempSeasonCode());
							updatableSet.add(LandDetails);
						}
					}
					// tempFarmerLandDetailList.addAll(existingLandDetail);
					// existing.setFarmerLandDetails((Set<FarmerLandDetails>)
					// existingLandDetail);
				}
				// updatableSet=new HashSet<>(existingLandDetail);
				existing.setFarmerLandDetails(updatableSet);
				existing.setLandTopology(farm.getLandTopology());
				// if (getCurrentTenantId().equalsIgnoreCase("Sierra")) {
				existing.setCertYear(farm.getCertYear());
				// }
				existing.setFarmPlatNo(farm.getFarmPlatNo());
				existing.setFarmRegNumber(farm.getFarmRegNumber());
				existing.setFarmOther(farm.getFarmOther());
				existing.setOwnLand(farm.getOwnLand());
				existing.setLeasedLand(farm.getLeasedLand());
				existing.setIrrigationLand(farm.getIrrigationLand());
				existing.setWaterSource(farm.getWaterSource());
				existing.setLocalNameOfCrotenTree(farm.getLocalNameOfCrotenTree());
				existing.setNoOfCrotenTrees(farm.getNoOfCrotenTrees());
				/*
				 * if (existing.getFarmer().getBasicInfo() == 1) {
				 * farmerService.updateFarmerRevisionNoAndBasicInfo(existing.
				 * getFarmer().getId(), DateUtil.getRevisionNumber(),
				 * existing.getFarmer().getBasicInfo()); } else {
				 * farmerService.updateFarmerRevisionNo(Long.valueOf(this.
				 * farmerId), DateUtil.getRevisionNumber()); }
				 */
				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.AWI_TENANT_ID)) {
					if (existing.getVillage().getGramPanchayat().getId() != farm.getVillage().getGramPanchayat()
							.getId()) {
						if (!ObjectUtil.isEmpty(farm.getVillage())) {
							String codeGen = idGenerator.getFarmWebCodeIdSeq(
									farm.getVillage().getCity().getCode().substring(0, 1),
									farm.getVillage().getGramPanchayat().getCode().substring(0, 1));
							existing.setFarmId(codeGen);
						}
					}
					existing.setVillage(farm.getVillage());

					existing.setSamithi(farm.getSamithi());
					existing.setFpo(farm.getFpo());
				}
				existing.setWaterHarvest(waterHarvest);
				existing.setRevisionNo(DateUtil.getRevisionNumber());
				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)) {
				       existing.setPresenceBananaTrees(farm.getPresenceBananaTrees());
				       existing.setParallelProd(farm.getParallelProd());
				       existing.setPresenceHiredLabour(farm.getPresenceHiredLabour());
				       existing.setRiskCategory(farm.getRiskCategory());
				       existing.setInputOrganicUnit(farm.getInputOrganicUnit());

				      Type listType1 = new TypeToken<List<TreeDetail>>() {}.getType();
						Set<TreeDetail> treeSet=new HashSet<>();
					   List<TreeDetail> treeDetailList = new Gson().fromJson(treeDetailJsonString, listType1);
				       if (!ObjectUtil.isEmpty(treeDetailList)) {
				    	   for (int i = 0; i < treeDetailList.size(); i++) {
				    		
				    		   TreeDetail treeDetail = new TreeDetail(); 
				    		   treeDetail.setProdStatus(treeDetailList.get(i).getProdStatus());
				    		   treeDetail.setVariety(treeDetailList.get(i).getVariety());
				    		   treeDetail.setYears(treeDetailList.get(i).getYears());
				    		   treeDetail.setNoOfTrees(treeDetailList.get(i).getNoOfTrees());
				    		   treeDetail.setFarm(farm);
				    		   treeSet.add(treeDetail);
				    		   JSONObject mainObj = new JSONObject();
					           mainObj.put("msg","success");
					           sendResponse(mainObj);
				    		  
				    	   }
				    	   existing.setTreeDetails(treeSet); 
				       }
				}
				existing.setActStatus(1);
				farmerService.editFarm(existing);
				updateDynamicFieldsWithActPlan("359",String.valueOf(existing.getId()),existing.getFarmer().getSeasonCode(),String.valueOf(DynamicFeildMenuConfig.EntityTypes.FARM.ordinal()));
				
			}
			return command;

		}

		if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.BLRI_TENANT_ID)) {
			return super.execute();
		} else {
			return "blriFarm";
		}

	}
	
	public String update() throws Exception {
		int seq=0;
		if (id != null && !id.equals("")) {
			farm = farmerService.findFarmById(Long.valueOf(id));
			if (farm == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			this.farmerId = String.valueOf(farm.getFarmer().getFarmerId());
			this.farmId = String.valueOf(farm.getId());
			setFarmerUniqueId(String.valueOf(farm.getFarmer().getId()));
			this.selectedFarmerId = farm.getFarmer().getFarmerId();
			setCurrentPage(getCurrentPage());
			id = null;

			command = UPDATE;
			request.setAttribute(HEADING, getText(UPDATE));


			farm.setFarmImageExist("0");
			if (farm.getPhoto() != null) {
				setFarmImageByteString(Base64Util.encoder(farm.getPhoto()));
				farm.setFarmImageExist("1");
			}
			if (getFarmerName() == null || getFarmerName().isEmpty()) {
				setFarmerName(farm.getFarmer().getName());
			}
			if ("1".equalsIgnoreCase(enableSoliTesting)) {
				if (!ObjectUtil.isListEmpty(farm.getDocUpload())) {
					farm.setDocUploadList(new ArrayList<DocumentUpload>(farm.getDocUpload()));
				}
			}

			if (!ObjectUtil.isEmpty(farm)) {
				if (!ObjectUtil.isEmpty(farm.getVillage())
						&& !ObjectUtil.isEmpty(farm.getVillage().getGramPanchayat())) {
					if (!ObjectUtil.isEmpty(farm.getVillage().getGramPanchayat().getCity())
							&& !ObjectUtil.isEmpty(farm.getVillage().getGramPanchayat().getCity().getLocality())) {
						setSelectedLocality(
								String.valueOf(farm.getVillage().getGramPanchayat().getCity().getLocality().getId()));
					}
					if (!ObjectUtil.isEmpty(farm.getVillage().getGramPanchayat().getCity())) {
						setSelectedCity(String.valueOf(farm.getVillage().getGramPanchayat().getCity().getId()));
					}
					setSelectedPanchayat(String.valueOf(farm.getVillage().getGramPanchayat().getId()));

					setSelectedVillage(String.valueOf(farm.getVillage().getId()));
					if (farm.getSamithi() != null) {
						setSelectedSamithi(String.valueOf(farm.getSamithi().getId()));
					}
					if (farm.getFpo() != null) {
						setSelectedFpo(String.valueOf(farm.getFpo()));
					}
				}

			}

			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.BLRI_TENANT_ID)) {
				housingInfo = farmerService.findByHousingInfo(farm.getId());

			}
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.COFBOARD_TENANT_ID)) {
			farm.setPresenceBananaTrees(farm.getPresenceBananaTrees());
			farm.setDistanceProcessingUnit(farm.getDistanceProcessingUnit());
			}
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)) {
				
					
				if(farm.getTreeDetails().size()>0){
						//List<SeedTreatment> seedTreat = farmerService.getSeedTreatmentById()
						for(TreeDetail val :farm.getTreeDetails()){
							val.setVarietyId(val.getVariety());
							val.setProdStatusId(val.getProdStatus());
							val.setYearsId(val.getYears());
							val.setProdStatus(getText("productStatus-"+val.getProdStatus()));
							val.setYears(getCatlogueValueByCode(val.getYears()).getName());
							ProcurementVariety variety=productDistributionService.findProcurementVariertyByCode(val.getVariety());
							val.setVariety(variety.getName());
						
						}
					}	
					farm.setPresenceBananaTrees(farm.getPresenceBananaTrees());
					farm.setParallelProd(farm.getParallelProd());
					farm.setPresenceHiredLabour(farm.getPresenceHiredLabour());
					farm.setRiskCategory(farm.getRiskCategory());
					farm.setInputOrganicUnit(farm.getInputOrganicUnit());
					//farm.setLeasedLand(farm.getLeasedLand());
				}

			if (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo())) {

				setSelectedLandMeasurement(farm.getFarmDetailedInfo().getLandMeasurement());
				selectedFarmOwned = (farm.getFarmDetailedInfo().getFarmOwned() == SELECT_MULTI) ? SELECT_MULTI
						: farm.getFarmDetailedInfo().getFarmOwned();
				selectedIrrigation = (farm.getFarmDetailedInfo().getFarmIrrigation() == SELECT_MULTI) ? SELECT_MULTI
						: farm.getFarmDetailedInfo().getFarmIrrigation();
				selectedIrrigationSource = (farm.getFarmDetailedInfo().getIrrigationSource() == SELECT_MULTI)
						? SELECT_MULTI : farm.getFarmDetailedInfo().getIrrigationSource();
				// selectedIrrigationMethod =
				// (farm.getFarmDetailedInfo().getIrrigationMethod() ==
				// SELECT) ? SELECT :
				// farm.getFarmDetailedInfo().getIrrigationMethod();
				selectedSoilType = (farm.getFarmDetailedInfo().getSoilType() == SELECT_MULTI) ? SELECT_MULTI
						: farm.getFarmDetailedInfo().getSoilType();

				selectedMethodOfIrrigation = (farm.getFarmDetailedInfo().getMethodOfIrrigation() == SELECT_MULTI)
						? SELECT_MULTI : farm.getFarmDetailedInfo().getMethodOfIrrigation();

				selectedSoilFertility = (farm.getFarmDetailedInfo().getSoilFertility() == SELECT) ? SELECT
						: farm.getFarmDetailedInfo().getSoilFertility();
				selectedICSStatus = (farm.getFarmDetailedInfo().getLandUnderICSStatus() == SELECT) ? SELECT
						: farm.getFarmDetailedInfo().getLandUnderICSStatus();
				selectedGradient = (farm.getFarmDetailedInfo().getLandGradient() == SELECT_MULTI) ? SELECT_MULTI
						: farm.getFarmDetailedInfo().getLandGradient();
				selectedTexture = (farm.getFarmDetailedInfo().getSoilTexture() == SELECT_MULTI) ? SELECT_MULTI
						: farm.getFarmDetailedInfo().getSoilTexture();
				selectedRoad = (farm.getFarmDetailedInfo().getApproachRoad() == SELECT_MULTI) ? SELECT_MULTI
						: farm.getFarmDetailedInfo().getApproachRoad();

				if (farm.getFarmDetailedInfo().getSessionYear() != null) {
					sessionYearString = farm.getFarmDetailedInfo().getSessionYear();
				}
				if (farm.getFarmDetailedInfo().getRegYear() != null) {
					regYearString = farm.getFarmDetailedInfo().getRegYear();
				}

				/* APMAS Plant Information */
				if (getCurrentTenantId().equalsIgnoreCase("atma")) {

					farmerSoilTestingList = farmerService.listFarmerSoilTestingByFarmId(String.valueOf(farm.getId()));

					JSONArray objArray = new JSONArray();
					FarmerPlants plantList = new FarmerPlants();

					if (!ObjectUtil.isEmpty(farm.getFarmerPlants())) {

						for (FarmerPlants plantLists : farm.getFarmerPlants()) {

							JSONObject getObj = new JSONObject();

							if (!StringUtil.isEmpty(plantLists.getPlants())) {
								getObj.put("selectedfarmerPlants", plantLists.getPlants());
							}

							if (!StringUtil.isEmpty(plantLists.getNoOfPlants())) {
								getObj.put("selectedplantedPlants", plantLists.getNoOfPlants());
							}

							if (!StringUtil.isEmpty(plantLists.getNoOfLive())) {
								getObj.put("selectednoOfLivePlants", plantLists.getNoOfLive());
							}

							objArray.add(getObj);
						}
						getplantDetailsList = new Gson().toJson(objArray);
					}

					JSONArray array = new JSONArray();
					if (!ObjectUtil.isEmpty(farm.getFarmerScheme())) {
						for (FarmerScheme scheme : farm.getFarmerScheme()) {
							FarmCatalogue cat = getCatlogueValueByCode(scheme.getBenefitDetails());
							FarmCatalogue depSch = getCatlogueValueByCode(scheme.getDepartmentName());
							JSONObject obj = new JSONObject();
							SimpleDateFormat dt1 = new SimpleDateFormat("MM/dd/yyyy");
							SimpleDateFormat dt2 = new SimpleDateFormat("yyyy-MM-dd");

							if (!ObjectUtil.isEmpty(cat)) {
								obj.put("selectedBenefitFarmer", cat.getCode() + "-" + cat.getName());
							}
							if (!StringUtil.isEmpty(scheme.getNoOfKgs())) {
								obj.put("selectedkgs", scheme.getNoOfKgs());
							}
							if (!ObjectUtil.isEmpty(depSch)) {
								obj.put("selectedScheme", depSch.getCode() + "-" + depSch.getName());
							}
							if (!StringUtil.isEmpty(scheme.getReceivedDate())) {
								String dateString = scheme.getReceivedDate().toString().split("\\ ")[0];
								Date date = dt2.parse(dateString);
								obj.put("receivedDate", dt1.format(date));
							}
							if (!StringUtil.isEmpty(scheme.getReceivedAmt())) {
								obj.put("selectedAmt", scheme.getReceivedAmt());
							}
							if (!StringUtil.isEmpty(scheme.getContributionAmt())) {
								obj.put("selectedcontribute", scheme.getContributionAmt());
							}
							array.add(obj);

						}
						prodEditArray = new Gson().toJson(array);

					}
				}

				/*
				 * if
				 * (farm.getFarmDetailedInfo().getLastDateOfChemicalApplication(
				 * ) != null) { chemicalString =
				 * farm.getFarmDetailedInfo().getLastDateOfChemicalApplication()
				 * ; }
				 */
				FarmDetailedInfo farmDetailedInfo = new FarmDetailedInfo();
				if (farm.getFarmDetailedInfo().isSameAddressofFarmer()) {
					farmDetailedInfo.setSameAddressofFarmer(true);
					farmDetailedInfo.setFarmAddress((farm.getFarmer().getAddress()!=null && !StringUtil.isEmpty(farm.getFarmer().getAddress()) ? farm.getFarmer().getAddress()+",":"") + "" + farm.getFarmer().getVillage().getCity().getName() + ","
									+ farm.getFarmer().getVillage().getName());
					if (farmDetailedInfo.getFarmAddress().trim().length() > 255)
						farmDetailedInfo.setFarmAddress(!StringUtil.isEmpty(farm.getFarmer().getAddress())
								? farm.getFarmer().getAddress() : "");
				} else
					farmDetailedInfo.setFarmAddress(!StringUtil.isEmpty(farm.getFarmDetailedInfo().getFarmAddress())
							? farm.getFarmDetailedInfo().getFarmAddress() : "");

				if (farm.getFarmDetailedInfo() != null
						&& StringUtil.isEmpty(farm.getFarmDetailedInfo().getProposedPlantingArea())) {
					farm.getFarmDetailedInfo().setProposedPlantingArea("0");
				}
				
				Format format = new SimpleDateFormat("dd-MM-yyyy");
				farm.getFarmDetailedInfo().setLastDateOfChemicalString(StringUtil.isEmpty(farm.getFarmDetailedInfo().getLastDateOfChemical()) ? ""
						: format.format(farm.getFarmDetailedInfo().getLastDateOfChemical()));

				/*
				 * if (farm.getFarmDetailedInfo().getInternalInspectionDate() !=
				 * null) { dateOfInspection =
				 * df.format(farm.getFarmDetailedInfo()
				 * .getInternalInspectionDate()); }
				 */

				if (!ObjectUtil.isListEmpty(farm.getFarmICS())) {
					List<FarmICS> tempListICS = Arrays.asList(new FarmICS[4]);
					farm.setFarmICSList(new LinkedList<FarmICS>(farm.getFarmICS()));
					for (FarmICS farmICS : farm.getFarmICS()) {
						if (!StringUtil.isEmpty(farmICS.getBeginOfConversion())) {
							Format formatter = new SimpleDateFormat("MM/dd/yyyy");
							String date = formatter.format(farmICS.getBeginOfConversion());
							farmICS.setBeginOfConversionString(date);

						}
						tempListICS.set(farmICS.getIcsType(), farmICS);
					}
					farm.setFarmICSList(tempListICS);
				}
				FarmIcsConversion icds = new FarmIcsConversion();
				if (farm.getFarmICSConversion() != null && farm.getFarmICSConversion().size() > 0) {
					for (FarmIcsConversion icd : farm.getFarmICSConversion()) {
						Format formatter = new SimpleDateFormat("dd-MM-yyyy");

						icd.setInspectionDateString(StringUtil.isEmpty(icd.getInspectionDate()) ? ""
								: formatter.format(icd.getInspectionDate()));
						/*
						 * icd.setLastDateChemicalAppString(StringUtil.isEmpty(
						 * icd. getLastDateChemicalApp()) ? "" :
						 * formatter.format(icd.getLastDateChemicalApp()));
						 */
						icd.setInsType(icd.getInsType());
						setInspType(icd.getInsType());
						icd.setOrganicStatus(icd.getOrganicStatus());
						
						farm.setFarmIcsConv(icd);
					}
				} else {

					// icds.setOrganicStatus(getLocaleProperty("inconversion"));

					farm.setFarmIcsConv(icds);
				}

			} else {
				setSelectedLandMeasurement(FarmDetailedInfo.HECTARES);
				setSelectedFarmOwned(SELECT_MULTI);
				setSelectedIrrigation(SELECT_MULTI);
				setSelectedIrrigationSource(SELECT_MULTI);
				// setSelectedIrrigationMethod(SELECT);
				setSelectedSoilType(SELECT_MULTI);
				setSelectedMethodOfIrrigation(SELECT_MULTI);
				setSelectedSoilFertility(SELECT);
				setSelectedICSStatus(SELECT);
			}

			if (getCurrentTenantId().equalsIgnoreCase("atma")) {
				updatefarmerLandDetailsList = farmerService.listFarmingSystemByFarmId(farm.getId());
				for (FarmerLandDetails details : updatefarmerLandDetailsList) {
					setHarvestSeason(details.getSeasonCode());
				}
			}
			
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)) {
				Set<TreeDetail> treeSet = new HashSet<>();
				List<TreeDetail> tree = farmerService.findTreeDetailByFarmId(farm.getId());
				if (!ObjectUtil.isListEmpty(tree)) {
					treeSet.addAll(tree);
				}
				if (!ObjectUtil.isEmpty(treeSet)) {
					farm.setJsonString(treeDetailToJson(treeSet));
				}
				
			}
			expenditureList = farmerService.listExpentitureListByFarmId(Long.valueOf(farm.getId()));
		} else {
			if (farm != null) {
				Farm existing = farmerService.findFarmById(farm.getId());
				if (getFarmerName() == null || getFarmerName().isEmpty()) {
					setFarmerName(farm.getFarmer().getName());
				}
				if (existing == null) {
					addActionError(NO_RECORD);
					return REDIRECT;
				}
				setCurrentPage(getCurrentPage());
				if (StringUtil.isEmpty(existing.getFarmCode()))
					existing.setFarmCode(String.valueOf(DateUtil.getRevisionNoDateTimeMilliSec()));
				existing.setFarmName(farm.getFarmName());
				existing.setSeason(farm.getSeason());

				// if (getFarm().getFarmImage()== null ||
				// getFarm().getFarmImageExist()=="0") {
				/*
				 * if (getFarm().getFarmImageExist().equals("0")) {
				 * addActionError(getText("empty.photo")); return INPUT; }
				 */

				if (getFarm().getFarmImage() != null) {
					existing.setPhoto(FileUtil.getBinaryFileContent(getFarm().getFarmImage()));
					existing.setFarmImageFileName(getFarm().getFarmImageFileName());
					existing.getFarmer().setBasicInfo(1);

				}
				existing.setLastUpdatedUsername(getUsername());
				existing.setLastUpdatedDate(new Date());
				existing.setLandmark(farm.getLandmark()); // set farm landmark
															// while updating.
				if ("1".equalsIgnoreCase(enableSoliTesting)) {
					Set<DocumentUpload> documentUploadSet = new HashSet<DocumentUpload>();
					if (!ObjectUtil.isListEmpty(farm.getDocUploadList())) {
						for (DocumentUpload document : farm.getDocUploadList()) {
							if (!StringUtil.isEmpty(document.getDocFile())) {
								document.setFarm(farm);
								document.setContent(FileUtil.getBinaryFileContent(document.getDocFile()));
								document.setName(document.getDocFileFileName());
								documentUploadSet.add(document);

							}

						}
						existing.setDocUpload(null);
						if (!ObjectUtil.isListEmpty(documentUploadSet))
							existing.setDocUpload(documentUploadSet);

					}
				}

				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.BLRI_TENANT_ID)) {
					existing.setHousingInfos(setHousingInfo());
				}
				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.COFBOARD_TENANT_ID)) {
					existing.setPresenceBananaTrees(getFarm().getPresenceBananaTrees());
					existing.setDistanceProcessingUnit(getFarm().getDistanceProcessingUnit());
					}
				/*
				 * if (existing.getFarmer().getCertificationType() ==
				 * Farmer.CERTIFICATION_TYPE_NONE) { if
				 * (!ObjectUtil.isListEmpty(existing.getFarmCrops())) {//
				 * Validation with Farm // Crops try { Double areaInProduction =
				 * Double.parseDouble(landInProduction); List<FarmCrops>
				 * farmCrops = new ArrayList<FarmCrops>(existing
				 * .getFarmCrops()); Double cropArea =
				 * Double.parseDouble(farmCrops.get(0).getCropArea()); for
				 * (FarmCrops farmCrop : existing.getFarmCrops()) { Double
				 * innerCropArea = Double.parseDouble(farmCrop.getCropArea());
				 * if (innerCropArea > cropArea) { cropArea = innerCropArea; } }
				 * if (cropArea > areaInProduction) {
				 * addActionError(getText("can't.update")); return INPUT; } }
				 * catch (Exception e) {
				 * addActionError(getText("error.processing")); return INPUT; }
				 * }// end try {// Total Area Double totalAreaOfProduction =
				 * Double.parseDouble(landInProduction) +
				 * (StringUtil.isEmpty(landNotInProduction) ? 0 : Double
				 * .parseDouble(landNotInProduction)); this.totalArea =
				 * CurrencyUtil.getDecimalFormat(totalAreaOfProduction,
				 * THREE_DECIMAL_FORMATTER); existing.setHectares(totalArea); }
				 * catch (Exception e) { this.totalArea = "0";
				 * existing.setHectares(this.totalArea); }
				 * existing.setLandInProduction(landInProduction);
				 * existing.setLandNotInProduction(landNotInProduction);
				 * existing.setFarmDetailedInfo(null); command = INPUT; }
				 */
				if (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo())) {

					FarmDetailedInfo farmDetailedInfo = new FarmDetailedInfo();
					if (farm.getFarmDetailedInfo().isSameAddressofFarmer()) {
						farmDetailedInfo.setSameAddressofFarmer(true);
						farmDetailedInfo.setFarmAddress((farm.getFarmer().getAddress()!=null && !StringUtil.isEmpty(farm.getFarmer().getAddress()) ? farm.getFarmer().getAddress()+",":"") + " "
								+ farm.getFarmer().getCity().getName() + "," + farm.getFarmer().getVillage().getName());
						if (farmDetailedInfo.getFarmAddress().trim().length() > 255)
							farmDetailedInfo.setFarmAddress(farm.getFarmer().getAddress());
					} else
						farmDetailedInfo.setFarmAddress(!StringUtil.isEmpty(farm.getFarmDetailedInfo().getFarmAddress())
								? farm.getFarmDetailedInfo().getFarmAddress() : "");

					farmDetailedInfo
							.setLandMeasurement(!StringUtil.isEmpty(farm.getFarmDetailedInfo().getLandMeasurement())
									? farm.getFarmDetailedInfo().getLandMeasurement() : "");
					farmDetailedInfo.setFarmArea(!StringUtil.isEmpty(farm.getFarmDetailedInfo().getFarmArea())
							? farm.getFarmDetailedInfo().getFarmArea() : "");
					farmDetailedInfo.setFarmProductiveArea(
							!StringUtil.isEmpty(farm.getFarmDetailedInfo().getFarmProductiveArea())
									? farm.getFarmDetailedInfo().getFarmProductiveArea() : "");
					farmDetailedInfo.setFarmConservationArea(
							!StringUtil.isEmpty(farm.getFarmDetailedInfo().getFarmConservationArea())
									? farm.getFarmDetailedInfo().getFarmConservationArea() : "");
					farmDetailedInfo
							.setWaterBodiesCount(!StringUtil.isEmpty(farm.getFarmDetailedInfo().getWaterBodiesCount())
									? farm.getFarmDetailedInfo().getWaterBodiesCount() : "");
					farmDetailedInfo.setFullTimeWorkersCount(
							!StringUtil.isEmpty(farm.getFarmDetailedInfo().getFullTimeWorkersCount())
									? farm.getFarmDetailedInfo().getFullTimeWorkersCount() : "");
					farmDetailedInfo.setPartTimeWorkersCount(
							!StringUtil.isEmpty(farm.getFarmDetailedInfo().getPartTimeWorkersCount())
									? farm.getFarmDetailedInfo().getPartTimeWorkersCount() : "");
					farmDetailedInfo.setSeasonalWorkersCount(
							!StringUtil.isEmpty(farm.getFarmDetailedInfo().getSeasonalWorkersCount())
									? farm.getFarmDetailedInfo().getSeasonalWorkersCount() : "");
					farmDetailedInfo.setFarmProductiveArea(
							!StringUtil.isEmpty(farm.getFarmDetailedInfo().getFarmProductiveArea())
									? farm.getFarmDetailedInfo().getFarmProductiveArea() : "");
					if(!getCurrentTenantId().equals(ESESystem.LIVELIHOOD_TENANT_ID)){
					farmDetailedInfo
							.setFarmOwned((selectedFarmOwned != SELECT_MULTI) ? selectedFarmOwned : SELECT_MULTI);
					}
					else{
						farmDetailedInfo.setFarmOwned(
								!StringUtil.isEmpty(farm.getFarmDetailedInfo().getFarmOwned())
										? farm.getFarmDetailedInfo().getFarmOwned() : "");
					}
					farmDetailedInfo.setFarmIrrigation(
							(selectedIrrigation != SELECT_MULTI) ? selectedIrrigation : SELECT_MULTI);
					farmDetailedInfo.setIrrigationSource(
							(selectedIrrigationSource != SELECT_MULTI) ? selectedIrrigationSource : SELECT_MULTI);
					// farmDetailedInfo.setIrrigationMethod((selectedIrrigationMethod
					// != SELECT ?
					// selectedIrrigationMethod : SELECT));
					farmDetailedInfo.setSoilType((selectedSoilType != SELECT_MULTI) ? selectedSoilType : SELECT_MULTI);
					farmDetailedInfo.setMethodOfIrrigation(
							(selectedMethodOfIrrigation != SELECT_MULTI) ? selectedMethodOfIrrigation : SELECT_MULTI);
					farmDetailedInfo
							.setSoilFertility((selectedSoilFertility != SELECT) ? selectedSoilFertility : SELECT);

					farmDetailedInfo
							.setLandGradient((selectedGradient != SELECT_MULTI) ? selectedGradient : SELECT_MULTI);

					farmDetailedInfo.setSoilTexture((selectedTexture != SELECT_MULTI) ? selectedTexture : SELECT_MULTI);
					farmDetailedInfo.setApproachRoad((selectedRoad != SELECT_MULTI) ? selectedRoad : SELECT_MULTI);

					farmDetailedInfo.setAreaUnderIrrigation(
							!StringUtil.isEmpty(farm.getFarmDetailedInfo().getAreaUnderIrrigation())
									? farm.getFarmDetailedInfo().getAreaUnderIrrigation() : "");
					farmDetailedInfo.setLastDateOfChemicalApplication(
							!StringUtil.isEmpty(farm.getFarmDetailedInfo().getLastDateOfChemicalApplication())
									? farm.getFarmDetailedInfo().getLastDateOfChemicalApplication() : "");
					farmDetailedInfo
							.setBeginOfConversion(!StringUtil.isEmpty(farm.getFarmDetailedInfo().getBeginOfConversion())
									? farm.getFarmDetailedInfo().getBeginOfConversion() : "");
					/*
					 * if (!StringUtil.isEmpty(dateOfInspection)) { DateFormat
					 * format = new SimpleDateFormat("MM/dd/yyyy"); try {
					 * farmDetailedInfo.setInternalInspectionDate((Date) format
					 * .parse(dateOfInspection)); } catch (ParseException e) {
					 * e.printStackTrace(); } }
					 */
					farmDetailedInfo.setInternalInspectorName(
							!StringUtil.isEmpty(farm.getFarmDetailedInfo().getInternalInspectorName())
									? farm.getFarmDetailedInfo().getInternalInspectorName() : "");
					farmDetailedInfo.setSurveyNumber(!StringUtil.isEmpty(farm.getFarmDetailedInfo().getSurveyNumber())
							? farm.getFarmDetailedInfo().getSurveyNumber() : "");
					farmDetailedInfo.setLandUnderICSStatus((selectedICSStatus != SELECT) ? selectedICSStatus : SELECT);
					farmDetailedInfo.setFallowOrPastureLand(
							!StringUtil.isEmpty(farm.getFarmDetailedInfo().getFallowOrPastureLand())
									? farm.getFarmDetailedInfo().getFallowOrPastureLand() : "");
					farmDetailedInfo
							.setConventionalLand(!StringUtil.isEmpty(farm.getFarmDetailedInfo().getConventionalLand())
									? farm.getFarmDetailedInfo().getConventionalLand() : "");
					farmDetailedInfo
							.setConventionalCrops(!StringUtil.isEmpty(farm.getFarmDetailedInfo().getConventionalCrops())
									? farm.getFarmDetailedInfo().getConventionalCrops() : "");
					farmDetailedInfo.setConventionalEstimatedYield(
							!StringUtil.isEmpty(farm.getFarmDetailedInfo().getConventionalEstimatedYield())
									? farm.getFarmDetailedInfo().getConventionalEstimatedYield() : "");
					farmDetailedInfo.setNc(!StringUtil.isEmpty(farm.getFarmDetailedInfo().getNc())
							? farm.getFarmDetailedInfo().getNc() : "");

					farmDetailedInfo
							.setTotalLandHolding(!StringUtil.isEmpty(farm.getFarmDetailedInfo().getTotalLandHolding())
									? formatter.format(Double.valueOf(farm.getFarmDetailedInfo().getTotalLandHolding()))
									: "");

					farmDetailedInfo.setProposedPlantingArea(
							!StringUtil.isEmpty(farm.getFarmDetailedInfo().getProposedPlantingArea()) ? formatter
									.format(Double.valueOf(farm.getFarmDetailedInfo().getProposedPlantingArea())) : "");

					farmDetailedInfo.setRainFedValue(!StringUtil.isEmpty(farm.getFarmDetailedInfo().getRainFedValue())
							? farm.getFarmDetailedInfo().getRainFedValue() : "");

					farmDetailedInfo
							.setIrrigatedOther(!StringUtil.isEmpty(farm.getFarmDetailedInfo().getIrrigatedOther())
									? farm.getFarmDetailedInfo().getIrrigatedOther() : "");

					farmDetailedInfo.setFarmerFieldSchool(farm.getFarmDetailedInfo().getFarmerFieldSchool());
					farmDetailedInfo.setIsFFSBenifited(farm.getFarmDetailedInfo().getIsFFSBenifited());

					farmDetailedInfo.setMilletCultivated(farm.getFarmDetailedInfo().getMilletCultivated());
					farmDetailedInfo.setMilletCropType(farm.getFarmDetailedInfo().getMilletCropType());
					farmDetailedInfo.setMilletCropCount(farm.getFarmDetailedInfo().getMilletCropCount());

					farmDetailedInfo
							.setBoreWellRechargeStructure(farm.getFarmDetailedInfo().getBoreWellRechargeStructure());

					farmDetailedInfo.setFieldName(farm.getFarmDetailedInfo().getFieldName());

					farmDetailedInfo.setFieldCrop(farm.getFarmDetailedInfo().getFieldCrop());

					farmDetailedInfo.setFieldArea(farm.getFarmDetailedInfo().getFieldArea());

					farmDetailedInfo.setInputApplied(farm.getFarmDetailedInfo().getInputApplied());

					farmDetailedInfo.setQuantityApplied(farm.getFarmDetailedInfo().getQuantityApplied());

					farmDetailedInfo.setInputSource(farm.getFarmDetailedInfo().getInputSource());

					farmDetailedInfo
							.setActivitiesInCoconutFarming(farm.getFarmDetailedInfo().getActivitiesInCoconutFarming());

					farmDetailedInfo.setProcessingActivity(farm.getFarmDetailedInfo().getProcessingActivity());
					farmDetailedInfo.setProcessingActivity(farm.getFarmDetailedInfo().getProcessingActivity());
					if (farm.getFarmDetailedInfo().getLastDateOfChemicalString() != null
							&& !farm.getFarmDetailedInfo().getLastDateOfChemicalString().isEmpty()) {
						ESESystem preferences = preferncesService.findPrefernceById("1");
						farmDetailedInfo
								.setLastDateOfChemical(DateUtil.convertStringToDate(
										farm.getFarmDetailedInfo().getLastDateOfChemicalString(), preferences
												.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT)));
					}
					/*
					 * farmDetailedInfo
					 * .setApproachRoad(!StringUtil.isEmpty(farm.
					 * getFarmDetailedInfo() .getApproachRoad()) ?
					 * farm.getFarmDetailedInfo() .getApproachRoad() : "");
					 */
					/*
					 * farmDetailedInfo.setRegYear(!StringUtil.isEmpty(farm.
					 * getFarmDetailedInfo() .getRegYear()) ?
					 * farm.getFarmDetailedInfo().getRegYear() : "");
					 */
					if (!StringUtil.isEmpty(sessionYearString))
						farmDetailedInfo.setSessionYear(sessionYearString);
					if (!StringUtil.isEmpty(regYearString))
						farmDetailedInfo.setRegYear(regYearString);
					/*
					 * if(!StringUtil.isEmpty(chemicalString))
					 * farmDetailedInfo.setLastDateOfChemicalApplication(
					 * chemicalString);
					 */

					if (!ObjectUtil.isListEmpty(farm.getFarmICSList())) {
						Set<FarmICS> icsUpdatedSet = new LinkedHashSet<FarmICS>();
						int i = 0;// Initializing Type Value
						boolean icsExist = false;
						for (int j = 0; j < farm.getFarmICSList().size(); j++) {
							if (!StringUtil.isEmpty(farm.getFarmICSList().get(j).getLandIcsDetails())
									|| !StringUtil.isEmpty(farm.getFarmICSList().get(j).getSurveyNo())
									|| !StringUtil.isEmpty(farm.getFarmICSList().get(j).getBeginOfConversionString())) {
								icsExist = true;
							}
						}
						if (icsExist) {

							for (FarmICS newFarmICS : farm.getFarmICSList()) {

								newFarmICS.setIcsType(i);
								boolean isExist = false;
								for (FarmICS existingFarmICS : existing.getFarmICS()) {
									if (newFarmICS.getIcsType() == existingFarmICS.getIcsType()) {
										isExist = true;

										if (!StringUtil.isEmpty(newFarmICS.getLandIcsDetails())
												|| !StringUtil.isEmpty(newFarmICS.getBeginOfConversionString())
												|| !StringUtil.isEmpty(newFarmICS.getSurveyNo())) {
											existingFarmICS.setLandIcsDetails(newFarmICS.getLandIcsDetails());

											if (!StringUtil.isEmpty(newFarmICS.getBeginOfConversionString()))
												existingFarmICS.setBeginOfConversion(DateUtil.convertStringToDate(
														newFarmICS.getBeginOfConversionString(), "MM/dd/yyyy"));

											existingFarmICS.setSurveyNo(newFarmICS.getSurveyNo());
											icsUpdatedSet.add(existingFarmICS);
										}
									}
								}
								if (!isExist) {// doesnt exist
									if (!StringUtil.isEmpty(newFarmICS.getLandIcsDetails())
											|| !StringUtil.isEmpty(newFarmICS.getBeginOfConversionString())
											|| !StringUtil.isEmpty(newFarmICS.getSurveyNo())) {

										if (!StringUtil.isEmpty(newFarmICS.getBeginOfConversionString()))
											newFarmICS.setBeginOfConversion(DateUtil.convertStringToDate(
													newFarmICS.getBeginOfConversionString(), "MM/dd/yyyy"));

										icsUpdatedSet.add(newFarmICS);
									}
								}
								i++;
							}
						}
						existing.setFarmICS(icsUpdatedSet);

					}
					ESESystem preferences = preferncesService.findPrefernceById("1");
					Set<FarmIcsConversion> ics = new HashSet<FarmIcsConversion>();
					if (farm.getFarmer().getIsCertifiedFarmer() == 1) {
						if (farm.getFarmIcsConv() != null) {

							existing.getFarmICSConversion().stream().forEach(u -> {
								u.setIsActive(0);
							});
							farm.getFarmIcsConv().setIsActive(1);
							if (!StringUtil.isEmpty(farm.getFarmIcsConv().getIcsType())
									&& !StringUtil.isEmpty(farm.getFarmIcsConv().getScope())
									&& !StringUtil.isEmpty(farm.getFarmIcsConv().getInspectionDateString())) {
								List<FarmIcsConversion> fcExis = existing.getFarmICSConversion().stream()
										.filter(u -> u.getIcsType().equalsIgnoreCase(farm.getFarmIcsConv().getIcsType())
												&& u.getScope().equalsIgnoreCase(farm.getFarmIcsConv().getScope())
												&& u.getInspectionDate()!=null && DateUtil.getYearByDateTime(u.getInspectionDate()).equalsIgnoreCase(
														DateUtil.getYearByDateTime(DateUtil.convertStringToDate(
																farm.getFarmIcsConv().getInspectionDateString(),
																getGeneralDateFormat()))))
										.collect(Collectors.toList());
								existing.getFarmICSConversion().removeAll(fcExis);
							}
							if(!StringUtil.isEmpty(farm.getFarmIcsConv().getInsType())){
								farm.getFarmIcsConv().setInsType(getInspType());
							}

							if (farm.getFarmIcsConv().getInspectionDateString() != null
									&& !farm.getFarmIcsConv().getInspectionDateString().isEmpty()) {
								farm.getFarmIcsConv()
										.setInspectionDate(DateUtil.convertStringToDate(
												farm.getFarmIcsConv().getInspectionDateString(), preferences
														.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT)));
							} else {
								farm.getFarmIcsConv().setInspectionDate(new Date());
							}
							if (farm.getFarmer().getIsCertifiedFarmer() == 1
									&& !ObjectUtil.isEmpty(farm.getFarmIcsConv().getIcsType())&& farm.getFarmIcsConv().getIcsType().equalsIgnoreCase("3")) {
								farm.getFarmIcsConv().setOrganicStatus("3");
							} else {
								farm.getFarmIcsConv().setOrganicStatus("0");
							}
							farm.getFarmIcsConv().setFarmer(farm.getFarmer());
							
							if (getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)){
								farm.getFarmIcsConv().setIcsType("0");
							}
							
							existing.getFarmICSConversion().add(farm.getFarmIcsConv());

							/*
							 * 
							 * else{ if (farm.getFarmIcsConv().getIcsType() !=
							 * null &&
							 * !farm.getFarmIcsConv().getIcsType().isEmpty()) {
							 * 
							 * if(farm.getFarmIcsConv().
							 * getLastDateChemicalAppString()!=null &&
							 * !farm.getFarmIcsConv().
							 * getLastDateChemicalAppString().isEmpty()){
							 * farm.getFarmIcsConv().setLastDateChemicalApp(
							 * DateUtil.
							 * convertStringToDate(farm.getFarmIcsConv().
							 * getLastDateChemicalAppString(), "MM/dd/yyyy")); }
							 * 
							 * if
							 * (farm.getFarmIcsConv().getInspectionDateString()
							 * != null &&
							 * !farm.getFarmIcsConv().getInspectionDateString().
							 * isEmpty()) { farm.getFarmIcsConv()
							 * .setInspectionDate(DateUtil.convertStringToDate(
							 * farm.getFarmIcsConv().getInspectionDateString(),
							 * preferences .getPreferences().get(ESESystem.
							 * GENERAL_DATE_FORMAT))); } else {
							 * farm.getFarmIcsConv().setInspectionDate(null); }
							 * 
							 * if
							 * (getCurrentTenantId().equalsIgnoreCase("wilmar"))
							 * { if (farm.getFarmer().getIsCertifiedFarmer() ==
							 * 1 && farm.getFarmIcsConv().getIcsType().
							 * equalsIgnoreCase("3")) {
							 * farm.getFarmIcsConv().setOrganicStatus(getText(
							 * "icsStatus3")); } else {
							 * farm.getFarmIcsConv().setOrganicStatus(getText(
							 * "inconversion")); } }
							 * ics.add(farm.getFarmIcsConv()); }
							 * existing.setFarmICSConversion(ics); }
							 */

						}
					}

					if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)
							|| getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID) || getCurrentTenantId().equalsIgnoreCase(ESESystem.COFBOARD_TENANT_ID)) {
						existing.setDistanceProcessingUnit(farm.getDistanceProcessingUnit());
						farmDetailedInfo.setProcessingActivity(farm.getFarmDetailedInfo().getProcessingActivity());
						existing.setAvgStore(farm.getAvgStore());
						existing.setTreeName(farm.getTreeName());
					}
					existing.setSoilTesting(farm.getSoilTesting());
					farmerService.addFarmDetailedInfo(farmDetailedInfo);
					existing.setFarmDetailedInfo(farmDetailedInfo);
					command = "farmerDetail";
				}
				existing.setExpenditures(formExpenditureSet());
				existing.setLatitude(farm.getLatitude());
				existing.setLongitude(farm.getLongitude());
				List<FarmerLandDetails> existingLandDetailList = new ArrayList<FarmerLandDetails>();
				Set<FarmerLandDetails> updatableSet = new HashSet<FarmerLandDetails>();
				Set<FarmerLandDetails> updatableList = null;
				if (!ObjectUtil.isListEmpty(updatefarmerLandDetailsList)) {
					existingLandDetailList = farmerService.listFarmingSystemByFarmId(farm.getId());
					for (FarmerLandDetails farmerLandDetail : updatefarmerLandDetailsList) {
						if (!ObjectUtil.isEmpty(farmerLandDetail)) {
							FarmerLandDetails existingLandDetail = farmerService
									.findFarmerLandDetailsById(farmerLandDetail.getId());
							if (!ObjectUtil.isEmpty(existingLandDetail)) {
								existingLandDetail.setIrrigatedTotLand(farmerLandDetail.getIrrigatedTotLand());
								existingLandDetail
										.setIrrigatedIfsPractices(farmerLandDetail.getIrrigatedIfsPractices());
								existingLandDetail.setRainfedTotLand(farmerLandDetail.getRainfedTotLand());
								existingLandDetail.setRanifedIfsPractices(farmerLandDetail.getRanifedIfsPractices());
								existingLandDetail.setSeasonCode(harvestSeason);
								updatableSet.add(existingLandDetail);
							}
						}
					}
					// updatableSet=new HashSet<>(existingLandDetailList);
					// tempFarmerLandDetailList.addAll(existingLandDetail);
				}
				if (!ObjectUtil.isListEmpty(farmerLandDetailsList)) {
					Set<FarmerLandDetails> framerLandDetails = new HashSet<FarmerLandDetails>();
					for (FarmerLandDetails farmerLandDetail : farmerLandDetailsList) {
						if (!ObjectUtil.isEmpty(farmerLandDetail)) {
							FarmerLandDetails LandDetails = new FarmerLandDetails();
							LandDetails.setIrrigatedTotLand(!StringUtil.isEmpty(farmerLandDetail.getIrrigatedLand())
									? Double.parseDouble(farmerLandDetail.getIrrigatedLand()) : 0.00);
							LandDetails.setIrrigatedIfsPractices(farmerLandDetail.getIrrigatedFarmingLand());
							LandDetails.setRainfedTotLand(!StringUtil.isEmpty(farmerLandDetail.getFedtotaland())
									? Double.parseDouble(farmerLandDetail.getFedtotaland()) : 0.00);
							LandDetails.setRanifedIfsPractices(farmerLandDetail.getFedtotalics());
							LandDetails.setSeasonCode(farmerLandDetail.getTempSeasonCode());
							updatableSet.add(LandDetails);
						}
					}
					// tempFarmerLandDetailList.addAll(existingLandDetail);
					// existing.setFarmerLandDetails((Set<FarmerLandDetails>)
					// existingLandDetail);
				}
				// updatableSet=new HashSet<>(existingLandDetail);
				//existing.setFarmerLandDetails(updatableSet);
				existing.setLandTopology(farm.getLandTopology());
				// if (getCurrentTenantId().equalsIgnoreCase("Sierra")) {
				existing.setCertYear(farm.getCertYear());
				// }
				existing.setFarmPlatNo(farm.getFarmPlatNo());
				existing.setFarmRegNumber(farm.getFarmRegNumber());
				existing.setFarmOther(farm.getFarmOther());
				existing.setOwnLand(farm.getOwnLand());
				existing.setLeasedLand(farm.getLeasedLand());
				existing.setIrrigationLand(farm.getIrrigationLand());
				existing.setWaterSource(farm.getWaterSource());
				existing.setLocalNameOfCrotenTree(farm.getLocalNameOfCrotenTree());
				existing.setNoOfCrotenTrees(farm.getNoOfCrotenTrees());
				/*
				 * if (existing.getFarmer().getBasicInfo() == 1) {
				 * farmerService.updateFarmerRevisionNoAndBasicInfo(existing.
				 * getFarmer().getId(), DateUtil.getRevisionNumber(),
				 * existing.getFarmer().getBasicInfo()); } else {
				 * farmerService.updateFarmerRevisionNo(Long.valueOf(this.
				 * farmerId), DateUtil.getRevisionNumber()); }
				 */
				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.AWI_TENANT_ID)) {
					if (existing.getVillage().getGramPanchayat().getId() != farm.getVillage().getGramPanchayat()
							.getId()) {
						if (!ObjectUtil.isEmpty(farm.getVillage())) {
							String codeGen = idGenerator.getFarmWebCodeIdSeq(
									farm.getVillage().getCity().getCode().substring(0, 1),
									farm.getVillage().getGramPanchayat().getCode().substring(0, 1));
							existing.setFarmId(codeGen);
						}
					}
					existing.setVillage(farm.getVillage());

					existing.setSamithi(farm.getSamithi());
					existing.setFpo(farm.getFpo());
				}
				existing.setWaterHarvest(waterHarvest);
				existing.setRevisionNo(DateUtil.getRevisionNumber());
				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)) {
				       existing.setPresenceBananaTrees(farm.getPresenceBananaTrees());
				       existing.setParallelProd(farm.getParallelProd());
				       existing.setPresenceHiredLabour(farm.getPresenceHiredLabour());
				       existing.setRiskCategory(farm.getRiskCategory());
				       existing.setInputOrganicUnit(farm.getInputOrganicUnit());

				      Type listType1 = new TypeToken<List<TreeDetail>>() {}.getType();
						Set<TreeDetail> treeSet=new HashSet<>();
					   List<TreeDetail> treeDetailList = new Gson().fromJson(treeDetailJsonString, listType1);
				       if (!ObjectUtil.isEmpty(treeDetailList)) {
				    	   for (int i = 0; i < treeDetailList.size(); i++) {
				    		
				    		   TreeDetail treeDetail = new TreeDetail(); 
				    		   treeDetail.setProdStatus(treeDetailList.get(i).getProdStatus());
				    		   treeDetail.setVariety(treeDetailList.get(i).getVariety());
				    		   treeDetail.setYears(treeDetailList.get(i).getYears());
				    		   treeDetail.setNoOfTrees(treeDetailList.get(i).getNoOfTrees());
				    		   treeDetail.setFarm(farm);
				    		   treeSet.add(treeDetail);
				    		   JSONObject mainObj = new JSONObject();
					           mainObj.put("msg","success");
					           sendResponse(mainObj);
				    		  
				    	   }
				    	   existing.setTreeDetails(treeSet); 
				       }
				}
				
				farmerService.editFarm(existing);
				updateDynamicFields("359",String.valueOf(existing.getId()),existing.getFarmer().getSeasonCode(),String.valueOf(DynamicFeildMenuConfig.EntityTypes.FARM.ordinal()));
				
			}
			return command;

		}

		if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.BLRI_TENANT_ID)) {
			return super.execute();
		} else {
			return "blriFarm";
		}

	}

	public String getHarvestSeason() {

		return harvestSeason;
	}

	public void setHarvestSeason(String harvestSeason) {

		this.harvestSeason = harvestSeason;
	}

	/**
	 * Detail.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String detail() throws Exception {

		this.prepare();
		String view = "";
		boolean first = true;
		first = true;
		String ifs = "";
		String soilConservation = "";
		String waterConservation = "";
		String serviceCenters = "";
		String traningPrgm = "";
		String irrigationSource = "";
		/*
		 * Long chId; String cropHarvestId; if
		 * (!ObjectUtil.isEmpty(farm.getCropHarvest())) {
		 * chId=farm.getCropHarvest().getId(); cropHarvestId=chId.toString(); }
		 */

		System.out.println("id------------------" + id);
		if (id != null && !id.equals("") && !id.equals("null")) {
			farm = farmerService.findFarmById(Long.valueOf(id));
			setSangham(farm.getFarmer().getSangham());
			if (!ObjectUtil.isListEmpty(farm.getFarmICS())) {
				farmICSs = new ArrayList<FarmICS>(farm.getFarmICS());

				farmICSs.sort((FarmICS o1, FarmICS o2) -> o1.getIcsType() - o2.getIcsType());

			}
			actStatuss=farm.getActStatus();
			cerLevel=farm.getCertificateStandardLevel();
			setFarmerId(farm.getFarmer().getFarmerId());
			setFarmerUniqueId(String.valueOf(farm.getFarmer().getId()));
			if (!ObjectUtil.isEmpty(farm.getFarmer())) {
				// if (farm.getFarmer().getCertificationType() !=
				// Farmer.CERTIFICATION_TYPE_NONE)
				if (farm.getFarmer().getIsCertifiedFarmer() != Farmer.CERTIFIED_NO)
					isCertifiedFarmer = true;
			}
			if (farm.getFarmCode() != null) {

				setHarvest(farmerService.findCropHarvestByFarmCode(farm.getFarmCode()));
			}

			for (CropHarvest harDet : harvest) {
				Farm farmTemp = farmerService.findFarmByCode(harDet.getFarmCode());
				FarmCrops crops = farmerService.findFarmCropsByFarmCode(farmTemp.getId());
				if (!ObjectUtil.isEmpty(crops)) {
					for (CropHarvestDetails cropHarvestDetails : harDet.getCropHarvestDetails()) {
						cropHarvestDetails.setHarvestSeason(
								(!ObjectUtil.isEmpty(crops) && !ObjectUtil.isEmpty(crops.getCropSeason()))
										? crops.getCropSeason().getName() : "");
						harvestDetails.add(cropHarvestDetails);
					}
					// harvestDetails.addAll(new
					// ArrayList<CropHarvestDetails>(harDet.getCropHarvestDetails()));
				}

			}
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.BLRI_TENANT_ID)) {
				housingInfo = farmerService.findByHousingInfo(farm.getId());
				if (!ObjectUtil.isEmpty(housingInfo) && !StringUtil.isEmpty(housingInfo.getHousingShadType())) {
					FarmCatalogue cat = getCatlogueValueByCode(housingInfo.getHousingShadType());

					housingInfo.setHousingShadType(cat.getName());
				}
			}
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.COFBOARD_TENANT_ID)) {
				farm.setPhotoId(farm.getPhotoId());
				if(!StringUtil.isEmpty(farm.getPresenceBananaTrees())){
					presenceOfBanana=bananaTreesList.get(Integer.parseInt(farm.getPresenceBananaTrees()));
				}
			}

			if (harvestDetails.size() > 0) {
				Collections.sort(harvestDetails, new Comparator<CropHarvestDetails>() {
					@Override
					public int compare(final CropHarvestDetails object1, final CropHarvestDetails object2) {

						return object1.getCrop().getName().compareTo(object2.getCrop().getName());
					}
				});
			}

			setHarvestSupply(farmerService.findCropSupplyByFarmCode(farm.getFarmCode()));
			cropSupplyDetails = new HashSet<CropSupplyDetails>();
			for (CropSupply supDet : harvestSupply) {
				cropSupplyDetails.addAll(supDet.getCropSupplyDetails());

			}
			if (!ObjectUtil.isEmpty(farm.getLatitude()) && farm.getLatitude()!="") {
				farm.setLatitude(farm.getLatitude());
			}
			if (!ObjectUtil.isEmpty(farm.getLongitude()) && farm.getLongitude()!="") {
				farm.setLongitude(farm.getLongitude());
			}
			if (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo())) {

				if (!StringUtil.isEmpty(farm.getFarmDetailedInfo().getTotalLandHolding())) {
					farm.getFarmDetailedInfo().setTotalLandHolding(CurrencyUtil.getDecimalFormat(
							Double.valueOf(farm.getFarmDetailedInfo().getTotalLandHolding()), "##.000"));
				} else {
					farm.getFarmDetailedInfo().setTotalLandHolding("0.000");
				}
				if (!StringUtil.isEmpty(farm.getFarmDetailedInfo().getProposedPlantingArea())) {
					farm.getFarmDetailedInfo().setProposedPlantingArea(CurrencyUtil.getDecimalFormat(
							Double.valueOf(farm.getFarmDetailedInfo().getProposedPlantingArea()), "##.000"));
				} else {
					farm.getFarmDetailedInfo().setProposedPlantingArea("0.000");
				}

				if (farm.getFarmDetailedInfo().getInternalInspectionDate() != null)
					dateOfInspection = df.format(farm.getFarmDetailedInfo().getInternalInspectionDate());
				if (farm.getFarmDetailedInfo().getLastDateOfChemical() != null)
					lastDateChemical = df.format(farm.getFarmDetailedInfo().getLastDateOfChemical());
				farmOwnedDetail = (farm.getFarmDetailedInfo().getFarmOwned() == SELECT_MULTI) ? ""
						: getFarmOwnedList().get(farm.getFarmDetailedInfo().getFarmOwned());
				if (!StringUtil.isEmpty(farm.getFarmDetailedInfo().getFarmIrrigation())) {
					farmIrrigationDetail = (farm.getFarmDetailedInfo().getFarmIrrigation() == SELECT_MULTI) ? ""
							: getFarmIrrigationList().get(farm.getFarmDetailedInfo().getFarmIrrigation());
				}
				if (!StringUtil.isEmpty(farm.getFarmDetailedInfo().getIrrigationSource())) {

					FarmCatalogue cat = getCatlogueValueByCode(farm.getFarmDetailedInfo().getIrrigationSource());

					if (cat != null) {
						irrigationSourceDetail = cat.getName();
					}

					/*
					 * irrigationSourceDetail =
					 * (farm.getFarmDetailedInfo().getIrrigationSource() ==
					 * SELECT_MULTI) ? "" : getIrrigationSourceList()
					 * .get(String.valueOf(cat.getName()));
					 */
				}

				/*
				 * if(farm.getFarmDetailedInfo().getIrrigationSource().contains(
				 * ",")) { String
				 * irrigationSourceArray[]=farm.getFarmDetailedInfo().
				 * getIrrigationSource().split(","); for(int
				 * i=0;i<irrigationSourceArray.length;i++) { String
				 * irrigationSourceTrim=irrigationSourceArray[i].replaceAll(
				 * "\\s+",""); if(!StringUtil.isEmpty(irrigationSourceDetail)) {
				 * irrigationSourceDetail+=getIrrigationSourceList().get(Integer
				 * .valueOf(irrigationSourceTrim))+","; } else {
				 * irrigationSourceDetail=getIrrigationSourceList().get(Integer.
				 * valueOf(irrigationSourceTrim))+","; } } }
				 */
				/*
				 * else { irrigationSourceDetail =
				 * (farm.getFarmDetailedInfo().getIrrigationSource() ==
				 * SELECT_MULTI) ? "" :
				 * getIrrigationSourceList().get(farm.getFarmDetailedInfo().
				 * getIrrigationSource()); }
				 */
				if (!StringUtil.isEmpty(farm.getFarmDetailedInfo().getTotalLandHolding()) &&(!StringUtil.isEmpty(farm.getOwnLand()))) {
					double totalHold = Double.valueOf(farm.getFarmDetailedInfo().getTotalLandHolding());
					double landOwn = Double.valueOf(farm.getOwnLand());
					double temp=totalHold*landOwn*100;
					noOfWineOnPlot=CurrencyUtil.getDecimalFormat((Double)temp, "##.00");
				}
 
				
				if (!StringUtil.isEmpty(irrigationSourceDetail)) {
					if (irrigationSourceDetail.contains(",")) {
						irrigationSourceDetail = StringUtil.isEmpty(irrigationSourceDetail) ? ""
								: irrigationSourceDetail.substring(0, irrigationSourceDetail.length() - 1);
					}
				}

				if (!StringUtil.isEmpty(farm.getFarmDetailedInfo().getFarmIrrigation())
						&& farm.getFarmDetailedInfo().getFarmIrrigation() != null
						&& farm.getFarmDetailedInfo().getFarmIrrigation().contains(",")) {
					String irrigationSourceArray[] = farm.getFarmDetailedInfo().getFarmIrrigation().split(",");
					if (farm.getFarmDetailedInfo().getFarmIrrigation().contains("2")) {
						isIrrigation = "1";
					} else {
						isIrrigation = "0";
					}
					for (int i = 0; i < irrigationSourceArray.length; i++) {
						String irrigationSourceTrim = irrigationSourceArray[i].replaceAll("\\s+", "");
						if (!StringUtil.isEmpty(farmIrrigationDetail)) {
							farmIrrigationDetail += getFarmIrrigationList().get(Integer.valueOf(irrigationSourceTrim))
									+ ",";
						} else {
							farmIrrigationDetail = getFarmIrrigationList().get(Integer.valueOf(irrigationSourceTrim))
									+ ",";
						}
					}
					farmIrrigationDetail = StringUtil.removeLastComma(farmIrrigationDetail);
				} else {
					farmIrrigationDetail = ((StringUtil.isEmpty(farm.getFarmDetailedInfo().getFarmIrrigation()))
							|| farm.getFarmDetailedInfo().getFarmIrrigation() == SELECT_MULTI) ? ""
									: getFarmIrrigationList()
											.get(Integer.valueOf(farm.getFarmDetailedInfo().getFarmIrrigation()));
					
					if (!ObjectUtil.isEmpty(farm)&& !ObjectUtil.isEmpty(farm.getFarmDetailedInfo()) 
							&& !ObjectUtil.isEmpty(farm.getFarmDetailedInfo().getFarmIrrigation()) 
							&& farm.getFarmDetailedInfo().getFarmIrrigation().contains("2")) {
						isIrrigation = "1";
					} else {
						isIrrigation = "0";
					}
				}

				/*
				 * if (!StringUtil.isEmpty(farmIrrigationDetail)) { if
				 * (farmIrrigationDetail.contains(",")) { farmIrrigationDetail =
				 * StringUtil.isEmpty(farmIrrigationDetail) ? "" :
				 * farmIrrigationDetail.substring(0,
				 * farmIrrigationDetail.length() - 1); } }
				 * irrigationMethodDetail =
				 * (farm.getFarmDetailedInfo().getIrrigationMethod() == SELECT)
				 * ? "" :
				 * getIrrigationMethodList().get(farm.getFarmDetailedInfo().
				 * getIrrigationMethod());
				 */

				if (!StringUtil.isEmpty(farm.getFarmDetailedInfo().getSoilType())
						&& farm.getFarmDetailedInfo().getSoilType().contains(",")) {

					String soilTypArray[] = farm.getFarmDetailedInfo().getSoilType().split(",");

					for (int i = 0; i < soilTypArray.length; i++) {
						String soilTypTrim = soilTypArray[i].replaceAll("\\s+", "");
						FarmCatalogue catalogue = catalogueService.findCatalogueByCode(soilTypTrim);
						if (!StringUtil.isEmpty(soilTypeDetail)) {
							soilTypeDetail += catalogue.getName()+ ",";
						} else {
							soilTypeDetail = catalogue.getName() + ",";
						}
					}

				} else {
					soilTypeDetail = (farm.getFarmDetailedInfo().getSoilType() == SELECT_MULTI) ? ""
							: getSoilTypeList().get(farm.getFarmDetailedInfo().getSoilType());
				}

				if (!StringUtil.isEmpty(soilTypeDetail)) {
					if (soilTypeDetail.contains(",")) {
						soilTypeDetail = StringUtil.isEmpty(soilTypeDetail) ? ""
								: soilTypeDetail.substring(0, soilTypeDetail.length() - 1);
					}
				}

				if (!StringUtil.isEmpty(farm.getFarmDetailedInfo().getMethodOfIrrigation())
						&& farm.getFarmDetailedInfo().getMethodOfIrrigation().contains(",")) {

					String methodIrrigationTypArray[] = farm.getFarmDetailedInfo().getMethodOfIrrigation().split(",");

					for (int i = 0; i < methodIrrigationTypArray.length; i++) {
						String methodIrrigationTypTrim = methodIrrigationTypArray[i].replaceAll("\\s+", "");
						if (!StringUtil.isEmpty(methodIrrigationTypTrim) && methodIrrigationTypTrim != null) {
							if (!StringUtil.isEmpty(methodOfIrrigationDetail) && methodOfIrrigationDetail != null) {
								methodOfIrrigationDetail += getMethodOfIrrigationList().get(methodIrrigationTypTrim)
										+ ",";
							} else {
								methodOfIrrigationDetail = getMethodOfIrrigationList().get(methodIrrigationTypTrim)
										+ ",";
							}
						}
					}

				} else {
					methodOfIrrigationDetail = (farm.getFarmDetailedInfo().getMethodOfIrrigation() == SELECT_MULTI) ? ""
							: getMethodOfIrrigationList().get(farm.getFarmDetailedInfo().getMethodOfIrrigation());
				}

				if (!StringUtil.isEmpty(methodOfIrrigationDetail)) {
					if (methodOfIrrigationDetail.contains(",")) {
						methodOfIrrigationDetail = StringUtil.isEmpty(methodOfIrrigationDetail) ? ""
								: methodOfIrrigationDetail.substring(0, methodOfIrrigationDetail.length() - 1);
					}
				}

				soilFertilityDetail = (farm.getFarmDetailedInfo().getSoilFertility() == SELECT) ? ""
						: getSoilFertilityList().get(farm.getFarmDetailedInfo().getSoilFertility());
				landUnderICSStatusDetail = (farm.getFarmDetailedInfo().getLandUnderICSStatus() == SELECT) ? ""
						: getIcsStatusList().get(farm.getFarmDetailedInfo().getLandUnderICSStatus());

				if (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo())
						&& (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo().getSoilTexture()))
						&& farm.getFarmDetailedInfo().getSoilTexture().contains(",")) {

					String soilTexArray[] = farm.getFarmDetailedInfo().getSoilTexture().split(",");

					for (int i = 0; i < soilTexArray.length; i++) {
						String soilTexTrim = soilTexArray[i].replaceAll("\\s+", "");
						if (!StringUtil.isEmpty(soilTextureDetail)) {
							soilTextureDetail += getSoilTextureList().get(soilTexTrim) + ",";
						} else {
							soilTextureDetail = getSoilTextureList().get(soilTexTrim) + ",";
						}
					}

				} else {
					soilTextureDetail = (farm.getFarmDetailedInfo().getSoilTexture() == SELECT_MULTI) ? ""
							: getSoilTextureList().get(farm.getFarmDetailedInfo().getSoilTexture());
				}

				if (!StringUtil.isEmpty(soilTextureDetail)) {
					if (soilTextureDetail.contains(",")) {
						soilTextureDetail = StringUtil.isEmpty(soilTextureDetail) ? ""
								: soilTextureDetail.substring(0, soilTextureDetail.length() - 1);
					}
				}
				if (!getCurrentTenantId().equalsIgnoreCase("symrise")) {
				if (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo())
						&& (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo().getLandGradient()))
						&& farm.getFarmDetailedInfo().getLandGradient().contains(",")) {

					String landGradiArray[] = farm.getFarmDetailedInfo().getLandGradient().split(",");

					for (int i = 0; i < landGradiArray.length; i++) {
						String landGradiTrim = landGradiArray[i].replaceAll("\\s+", "");
						if (!StringUtil.isEmpty(landGradientDetail)) {
							//landGradientDetail += getLandGradientList().get(Integer.valueOf(landGradiTrim)) + ",";
							//landGradientDetail += getLandGradientList().get(String.valueOf(landGradiTrim)) + ",";
							landGradientDetail =   getCatlogueValueByCode(farm.getFarmDetailedInfo().getLandGradient().trim()).getName()+ ",";
						} else {
							//landGradientDetail = getLandGradientList().get(Integer.valueOf(landGradiTrim)) + ",";
							//landGradientDetail = getLandGradientList().get(String.valueOf(landGradiTrim)) + ",";
							landGradientDetail =   getCatlogueValueByCode(farm.getFarmDetailedInfo().getLandGradient().trim()).getName()+ ",";
						}
					}

				} else {
					/*landGradientDetail = ((StringUtil.isEmpty(farm.getFarmDetailedInfo().getLandGradient()))
							|| farm.getFarmDetailedInfo().getLandGradient() == SELECT_MULTI) ? ""
									: getLandGradientList()
											.get(Integer.valueOf(farm.getFarmDetailedInfo().getLandGradient()));*/
					/*landGradientDetail = ((StringUtil.isEmpty(farm.getFarmDetailedInfo().getLandGradient()))
							|| farm.getFarmDetailedInfo().getLandGradient() == SELECT_MULTI) ? ""
									: getLandGradientList()
											.get(String.valueOf(farm.getFarmDetailedInfo().getLandGradient()));*/
					
					landGradientDetail = ((StringUtil.isEmpty(farm.getFarmDetailedInfo().getLandGradient()))
							|| farm.getFarmDetailedInfo().getLandGradient() == SELECT_MULTI) ? ""
									: getCatlogueValueByCode(farm.getFarmDetailedInfo().getLandGradient().trim()).getName();
				}}
				else{
					/*FarmCatalogue cat = getCatlogueValueByCode(farm.getFarmDetailedInfo().getLandGradient());

					if (cat != null) {
						landGradientDetail = cat.getName();
					}*/
					
					if (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo())
							&& (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo().getLandGradient()))
							&& farm.getFarmDetailedInfo().getLandGradient().contains(",")) {
						String landGradiArray[] = farm.getFarmDetailedInfo().getLandGradient().split(",");

						for (int i = 0; i < landGradiArray.length; i++) {
							String landGradiTrim = landGradiArray[i].replaceAll("\\s+", "");
							if (!StringUtil.isEmpty(selectedRoadString)) {
								landGradientDetail += getCatlogueValueByCode(landGradiTrim).getName() + ",";
							} else {
								landGradientDetail =  getCatlogueValueByCode(landGradiTrim).getName() + ",";
							}
						}

					} else {
						landGradientDetail = ((StringUtil.isEmpty(farm.getFarmDetailedInfo().getLandGradient()))
								|| farm.getFarmDetailedInfo().getApproachRoad() == SELECT_MULTI) ? ""
										: getCatlogueValueByCode(farm.getFarmDetailedInfo().getLandGradient()).getName();

					}

				}

				if (!StringUtil.isEmpty(landGradientDetail)) {
					if (landGradientDetail.contains(",")) {
						landGradientDetail = StringUtil.isEmpty(landGradientDetail) ? ""
								: landGradientDetail.substring(0, landGradientDetail.length() - 1);
					}
				}
				if (!getCurrentTenantId().equalsIgnoreCase("symrise")) {
				if (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo())
						&& (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo().getApproachRoad()))
						&& farm.getFarmDetailedInfo().getApproachRoad().contains(",")) {
					String approachRoadArray[] = farm.getFarmDetailedInfo().getApproachRoad().split(",");

					for (int i = 0; i < approachRoadArray.length; i++) {
						String approachRoadTrim = approachRoadArray[i].replaceAll("\\s+", "");
						if (!StringUtil.isEmpty(selectedRoadString)) {
							selectedRoadString += getApproadList().get(String.valueOf(approachRoadTrim)) + ",";
						} else {
							selectedRoadString = getApproadList().get(String.valueOf(approachRoadTrim)) + ",";
						}
					}

				} else {
					selectedRoadString = ((StringUtil.isEmpty(farm.getFarmDetailedInfo().getApproachRoad()))
							|| farm.getFarmDetailedInfo().getApproachRoad() == SELECT_MULTI) ? ""
									: getApproadList()
											.get(String.valueOf(farm.getFarmDetailedInfo().getApproachRoad()));

				}}else{
				/*	FarmCatalogue cat = getCatlogueValueByCode(farm.getFarmDetailedInfo().getApproachRoad());
					if (cat != null) {
						selectedRoadString = cat.getName();
					}*/
					
					if (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo())
							&& (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo().getApproachRoad()))
							&& farm.getFarmDetailedInfo().getApproachRoad().contains(",")) {
						String approachRoadArray[] = farm.getFarmDetailedInfo().getApproachRoad().split(",");

						for (int i = 0; i < approachRoadArray.length; i++) {
							String approachRoadTrim = approachRoadArray[i].replaceAll("\\s+", "");
							if (!StringUtil.isEmpty(selectedRoadString)) {
								selectedRoadString += getCatlogueValueByCode(approachRoadTrim).getName() + ",";
							} else {
								selectedRoadString =  getCatlogueValueByCode(approachRoadTrim).getName() + ",";
							}
						}

					} else {
						selectedRoadString = ((StringUtil.isEmpty(farm.getFarmDetailedInfo().getApproachRoad()))
								|| farm.getFarmDetailedInfo().getApproachRoad() == SELECT_MULTI) ? ""
										: getCatlogueValueByCode(farm.getFarmDetailedInfo().getApproachRoad()).getName();

					}
				}

				if (!StringUtil.isEmpty(selectedRoadString)) {
					if (selectedRoadString.contains(",")) {
						selectedRoadString = StringUtil.isEmpty(selectedRoadString) ? ""
								: selectedRoadString.substring(0, selectedRoadString.length() - 1);
					}
				}

				if (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo())
						&& (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo().getSessionYear()))) {
					sessionYearString = farm.getFarmDetailedInfo().getSessionYear();
				}

				if (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo())
						&& (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo().getRegYear()))) {
					regYearString = farm.getFarmDetailedInfo().getRegYear();
				}
				/*
				 * if
				 * (farm.getFarmDetailedInfo().getLastDateOfChemicalApplication(
				 * )!= null) { chemicalString =
				 * farm.getFarmDetailedInfo().getLastDateOfChemicalApplication()
				 * ; }
				 */

			}

			if (getCurrentTenantId().equalsIgnoreCase("atma")) {

				if (!ObjectUtil.isEmpty(farm.getFarmerScheme())) {
					for (FarmerScheme scheme : farm.getFarmerScheme()) {
						FarmCatalogue cat = getCatlogueValueByCode(scheme.getBenefitDetails());
						benefitaryCode = cat.getCode() + "-" + cat.getName();
						if (!first) {
							benefitary = benefitary + "," + benefitaryCode;
						} else {
							benefitary = benefitaryCode;
							first = false;
						}
					}
				}

				if (!ObjectUtil.isEmpty(farm.getFarmerScheme())) {
					boolean first2 = true;
					for (FarmerScheme scheme : farm.getFarmerScheme()) {
						FarmCatalogue dep = getCatlogueValueByCode(scheme.getDepartmentName());
						if (!ObjectUtil.isEmpty(dep)) {
							schemeCode = dep.getCode() + "-" + dep.getName();
						}
						if (!first2) {
							schemeTxt = schemeTxt + "," + schemeCode;
						} else {
							schemeTxt = schemeCode;
							first2 = false;
						}

					}

				}

				if (!StringUtil.isEmpty(farm.getIfs())) {

					String ifsArr[] = farm.getIfs().split("\\,");
					for (int i = 0; i < ifsArr.length; i++) {
						String ifsTrim = ifsArr[i].replaceAll("\\s+", "");
						if (!StringUtil.isEmpty(ifsTrim)) {
							if (ifsTrim.equalsIgnoreCase("99")) {
								String otherName = farm.getIfsOther() != null ? farm.getIfsOther() : "";
								ifs += otherName + ",";

							} else {

								FarmCatalogue otherName = getCatlogueValueByCode(ifsTrim);
								ifs += otherName.getName() + ",";
							}
						}

					}
				}

				if (ifs != null && !StringUtil.isEmpty(ifs)) {
					ifs = ifs.substring(0, ifs.length() - 1);
					farm.setIfs(ifs);
				} else {
					farm.setIfs("");
				}

				// Soil Conservation

				if (!StringUtil.isEmpty(farm.getSoilConservation())) {

					String soilConservationArr[] = farm.getSoilConservation().split("\\,");
					for (int i = 0; i < soilConservationArr.length; i++) {
						String soilConservationTrim = soilConservationArr[i].replaceAll("\\s+", "");
						if (!StringUtil.isEmpty(soilConservationTrim)) {
							if (soilConservationTrim.equalsIgnoreCase("99")) {
								String otherName = farm.getSoilConservationOther() != null
										? farm.getSoilConservationOther() : "";
								soilConservation += otherName + ",";

							} else {

								FarmCatalogue otherName = getCatlogueValueByCode(soilConservationTrim);
								soilConservation += otherName.getName() + ",";
							}
						}

					}
				}

				if (soilConservation != null && !StringUtil.isEmpty(soilConservation)) {
					soilConservation = soilConservation.substring(0, soilConservation.length() - 1);
					farm.setSoilConservation(soilConservation);
				} else {
					farm.setSoilConservation("");
				}

				// Water Conservation

				if (!StringUtil.isEmpty(farm.getWaterConservation())) {

					String waterConservationArr[] = farm.getWaterConservation().split("\\,");
					for (int i = 0; i < waterConservationArr.length; i++) {
						String waterConservationTrim = waterConservationArr[i].replaceAll("\\s+", "");
						if (!StringUtil.isEmpty(waterConservationTrim)) {
							if (waterConservationTrim.equalsIgnoreCase("99")) {
								String otherName = farm.getWaterConservationOther() != null
										? farm.getWaterConservationOther() : "";
								waterConservation += otherName + ",";

							} else {

								FarmCatalogue otherName = getCatlogueValueByCode(waterConservationTrim);
								waterConservation += otherName.getName() + ",";
							}
						}
					}
				}

				if (waterConservation != null && !StringUtil.isEmpty(waterConservation)) {
					waterConservation = waterConservation.substring(0, waterConservation.length() - 1);
					farm.setWaterConservation(waterConservation);
				} else {
					farm.setWaterConservation("");
				}

				// Farmer Service
				if (!StringUtil.isEmpty(farm.getServiceCentres())) {

					String serviceCentersArr[] = farm.getServiceCentres().split("\\,");
					for (int i = 0; i < serviceCentersArr.length; i++) {
						String serviceCentersTrim = serviceCentersArr[i].replaceAll("\\s+", "");
						if (!StringUtil.isEmpty(serviceCentersTrim)) {
							if (serviceCentersTrim.equalsIgnoreCase("99")) {
								String otherName = farm.getServiceCentresOther() != null ? farm.getServiceCentresOther()
										: "";
								serviceCenters += otherName + ",";

							} else {

								FarmCatalogue otherName = getCatlogueValueByCode(serviceCentersTrim);
								serviceCenters += otherName.getName() + ",";
							}
						}

					}
				}

				if (serviceCenters != null && !StringUtil.isEmpty(serviceCenters)) {
					serviceCenters = serviceCenters.substring(0, serviceCenters.length() - 1);
					farm.setServiceCentres(serviceCenters);
				} else {
					farm.setServiceCentres("");
				}

				// Traning Program

				if (!ObjectUtil.isEmpty(farm.getTrainingProgram())) {

					/*
					 * if(farm.getTrainingProgram().contains(",")) {
					 */
					String trainingArray[] = farm.getTrainingProgram().split(",");

					for (int i = 0; i < trainingArray.length; i++) {
						String traningTrim = trainingArray[i].replaceAll("\\s+", "");
						if (!StringUtil.isEmpty(traningTrim)) {
							if (!traningTrim.equalsIgnoreCase("99")) {
								FarmCatalogue otherName = getCatlogueValueByCode(traningTrim);
								traningPrgm += otherName.getName() + ",";
							} else {
								String otherName = farm.getTrainingProgramOther() != null
										? farm.getTrainingProgramOther() : "";
								traningPrgm += otherName + ",";
							}
						}
					}

					traningPrgm = StringUtil.removeLastComma(traningPrgm);

					/*
					 * } else { traningPrgm =
					 * ((StringUtil.isEmpty(farm.getTrainingProgram())) ||
					 * farm.getTrainingProgram() == SELECT_MULTI) ? "" :
					 * getTrainingProgramList().get(farm.getTrainingProgram());
					 * }
					 */
				}
				if (!StringUtil.isEmpty(traningPrgm) && traningPrgm != null) {
					if (traningPrgm.contains(",")) {
						traningPrgm = StringUtil.isEmpty(traningPrgm) ? ""
								: traningPrgm.substring(0, traningPrgm.length() - 1);
						farm.setTrainingProgram(traningPrgm);
					} else {
						farm.setTrainingProgram(traningPrgm);
					}
				} else {
					farm.setTrainingProgram("");
				}

			}

			if (farm.getLandTopology() != "-1" && farm.getLandTopology() != null) {
				FarmCatalogue farmCatalogue = getCatlogueValueByCode(farm.getLandTopology());
				if (!ObjectUtil.isEmpty(farmCatalogue)) {
					farm.setLandTopology(farmCatalogue.getName());
				} else {
					farm.setLandTopology("");
				}
			}

			if (farm.getWaterSource() != "-1" && farm.getWaterSource() != null) {
				String catalogueCode[] = farm.getWaterSource().split(",");
				String waterSource = "";
				for (int i = 0; i < catalogueCode.length; i++) {
					FarmCatalogue farmCatalogue = getCatlogueValueByCode(catalogueCode[i].trim());
					if (!ObjectUtil.isEmpty(farmCatalogue)) {
						if (catalogueCode.length == i + 1) {
							waterSource = waterSource + farmCatalogue.getName();
						} else {
							waterSource = waterSource + farmCatalogue.getName() + ",";
						}

					}
				}

				setSelectedWaterSource(waterSource);
			}
			
			
			if (farm.getFarmDetailedInfo().getInputSource() != "-1" && farm.getFarmDetailedInfo().getInputSource() != null) {
				String catalogueCode[] = farm.getFarmDetailedInfo().getInputSource().split(",");
				String inputSource = "";
				for (int i = 0; i < catalogueCode.length; i++) {
					FarmCatalogue farmCatalogue = getCatlogueValueByCode(catalogueCode[i].trim());
					if (!ObjectUtil.isEmpty(farmCatalogue)) {
						if (catalogueCode.length == i + 1) {
							inputSource = inputSource + farmCatalogue.getName();
						} else {
							inputSource = inputSource + farmCatalogue.getName() + ",";
						}

					}
				}

				setSelectedInputSource(inputSource);
			}

			if (farm.getFarmICSConversion() != null && farm.getFarmICSConversion().size() > 0) {
				for (FarmIcsConversion icd : farm.getFarmICSConversion()) {
					ESESystem preferences = preferncesService.findPrefernceById("1");
					if (!ObjectUtil.isEmpty(preferences)) {

						DateFormat genDate = new SimpleDateFormat(
								preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
						if(getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)){
							SimpleDateFormat sf=new SimpleDateFormat("MM-dd-yyyy");
							icd.setInspectionDateString(StringUtil.isEmpty(icd.getInspectionDate()) ? " "
									: sf.format(icd.getInspectionDate()));	
						}else
						icd.setInspectionDateString(StringUtil.isEmpty(icd.getInspectionDate()) ? " "
								: genDate.format(icd.getInspectionDate()));
					}

					
					if(!StringUtil.isEmpty(icd.getIcsType())){
						if(icd.getIcsType().equalsIgnoreCase("0")){
							setCertificationType(getLocaleProperty("icsStatus0"));
						}else if(icd.getIcsType().equalsIgnoreCase("1")){
							setCertificationType(getLocaleProperty("icsStatus1"));
						}else if(icd.getIcsType().equalsIgnoreCase("2")){
							setCertificationType(getLocaleProperty("icsStatus2"));
						}else{
							setCertificationType(getLocaleProperty("icsStatus3"));
						}
					}
					
					/*
					 * farm.setOrganicStatus(!StringUtil.isEmpty(icd.
					 * getOrganicStatus()) ? icd.getOrganicStatus() : "4");
					 */

					scopeName = !StringUtil.isEmpty(icd.getScope())
							? catalogueService.findCatalogueByCode(icd.getScope()).getName() : "";
							if(getCurrentTenantId().equalsIgnoreCase(ESESystem.SUSAGRI)){	
							
							if (!StringUtil.isEmpty(icd.getInsType())) {
		                           /*if (icd.getInsType().equalsIgnoreCase("0")) {
		                              setInspType("Group");
		                           } else {
		                              setInspType("Processing");
		                           }*/
								
								inspType =!StringUtil.isEmpty(icd.getInsType())
										? catalogueService.findCatalogueByCode(icd.getInsType()).getName() : "";
		                        }
							}
					/*
					 * if(!ObjectUtil.isEmpty(preferences)){ ife
					 * (!ObjectUtil.isEmpty(farm.getFarmIcsConv().
					 * getInspectionDateString()) &&
					 * farm.getFarmIcsConv().getInspectionDateString() != null)
					 * {
					 * 
					 * farm.getFarmIcsConv().setInspectionDate(DateUtil.
					 * convertStringToDate(farm.getFarmIcsConv().
					 * getInspectionDateString(),
					 * preferences.getPreferences().get(ESESystem.
					 * GENERAL_DATE_FORMAT))); }
					 * 
					 * }else { farm.getFarmIcsConv().setInspectionDate(DateUtil.
					 * convertStringToDate(
					 * farm.getFarmIcsConv().getInspectionDateString(),
					 * "MM/dd/yyyy")); }
					 */

					/*
					 * Format formatter = new SimpleDateFormat("MM/dd/yyyy");
					 * icd.setInspectionDateString(StringUtil.isEmpty(icd.
					 * getInspectionDate()) ? "" :
					 * formatter.format(icd.getInspectionDate()));
					 * farm.setFarmIcsConv(icd);
					 */
					farm.setFarmIcsConv(icd);
				}
			} else {
				FarmIcsConversion fc = new FarmIcsConversion();
				Set<FarmIcsConversion> temp = new LinkedHashSet<>();

				// farm.setOrganicStatus("4");

				temp.add(fc);
				farm.setFarmICSConversion(temp);
			}
			ESESystem preferences = preferncesService.findPrefernceById("1");
			if (!ObjectUtil.isEmpty(preferences)) {
				DateFormat genDate = new SimpleDateFormat(
						preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)) {
					SimpleDateFormat sf=new SimpleDateFormat("MM-dd-yyyy");
				farm.getFarmDetailedInfo().setLastDateOfChemicalString(StringUtil.isEmpty(farm.getFarmDetailedInfo().getLastDateOfChemical()) ? " "
						: sf.format(farm.getFarmDetailedInfo().getLastDateOfChemical()));
				}else
					farm.getFarmDetailedInfo().setLastDateOfChemicalString(StringUtil.isEmpty(farm.getFarmDetailedInfo().getLastDateOfChemical()) ? " "
							: genDate.format(farm.getFarmDetailedInfo().getLastDateOfChemical()));
			}
			if (!ObjectUtil.isEmpty(farm.getAudio())) {
				this.audioDownloadString = "<button type=\"button\" title='" + getText("audio.download")
						+ "' class=\"fa fa-download\" align=\"center\" onclick=\"downloadAudioFile(" + farm.getId()
						+ ")\">" + "</button>" + "&nbsp;&nbsp;&nbsp;" + "<button type=\"button\" title='"
						+ getText("audio.play")
						+ "' class=\"fa fa-volume-up\" align=\"center\" onclick=\"playAudioFiles(" + farm.getId()
						+ ")\">" + "</button>";
			} else {
				this.audioDownloadString = "<button type=\"button\" align=\"center\" title='" + getText("audio.noVoice")
						+ "' class=\"btn noVoiceDowmloadBtnSytle\">" + "</button>";
			}
			if (!StringUtil.isEmpty(farm.getWaterHarvest())) {
				waterHarvests = "";
				for (String catCode : farm.getWaterHarvest().split(",")) {
					FarmCatalogue catalogue = catalogueService.findCatalogueByCode(catCode.trim());
					if (!ObjectUtil.isEmpty(catalogue))
						waterHarvests += catalogue.getName() + ",";
				}
				waterHarvests = waterHarvests.substring(0, waterHarvests.length() - 1);
			}
		
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)) {
			if(!StringUtil.isEmpty(farm.getPresenceBananaTrees())){
				presenceOfBanana=bananaTreesList.get(Integer.parseInt(farm.getPresenceBananaTrees()));
			}
			if(!StringUtil.isEmpty(farm.getParallelProd())){
				parallelProduction=parallelProductionList.get(Integer.parseInt(farm.getParallelProd()));
			}
			if(!StringUtil.isEmpty(farm.getPresenceHiredLabour())){
				hiredLabours=hiredLabourList.get(Integer.parseInt(farm.getPresenceHiredLabour()));
			}
			if(!StringUtil.isEmpty(farm.getRiskCategory())){
				riskCategory=riskCategoryList.get(Integer.parseInt(farm.getRiskCategory()));
			}
			
			if(farm.getTreeDetails().size()>0){
				//List<SeedTreatment> seedTreat = farmerService.getSeedTreatmentById()
				for(TreeDetail val :farm.getTreeDetails()){
					val.setProdStatus(getText("productStatus-"+val.getProdStatus()));
					val.setYears(getCatlogueValueByCode(val.getYears()).getName());
					ProcurementVariety variety=productDistributionService.findProcurementVariertyByCode(val.getVariety());
					val.setVariety(!ObjectUtil.isEmpty(variety)? variety.getName():"");
				}
			}	
				Double totOrgTrees = 0.0;
				Double totConvTrees = 0.0;
				Double totAvocadoTrees=0.0;
				Double hectarOrgTrees=0.0;
				Double hectarConvTrees=0.0;
				Double hectarAvocadoTrees=0.0;
				
				List<Object[]> treeDetailsList=productDistributionService.listTreeDetails(farm.getId());
				treeDetails=new ArrayList();
				if(!ObjectUtil.isListEmpty(treeDetailsList))
				{
					
					for(Object[] obj:treeDetailsList)
					{
						String varietyName=null;
						TreeDetail detail=new TreeDetail();
						if(String.valueOf(obj[2]).equalsIgnoreCase("1"))
						{
							totOrgTrees+=Double.valueOf(String.valueOf(obj[0]));
							detail.setNoOfTrees(String.valueOf(obj[0]));
							 varietyName=getLocaleProperty("totalOrgVarty")+" "+String.valueOf(obj[1]);
							detail.setVariety(varietyName);
						}
						else if(String.valueOf(obj[2]).equalsIgnoreCase("2"))
						{
							totConvTrees+=Double.valueOf(String.valueOf(obj[0]));
							detail.setNoOfTrees(String.valueOf(obj[0]));
							 varietyName=getLocaleProperty("totalConvnVarty")+" "+String.valueOf(obj[1]);
							detail.setVariety(varietyName);
						}
						treeDetails.add(detail);
					}
				}
				totAvocadoTrees=totOrgTrees+totConvTrees;
				hectarOrgTrees=totOrgTrees*7*7/10000;
				hectarConvTrees=totConvTrees*7*7/10000;
				hectarAvocadoTrees=totAvocadoTrees*7*7/10000;
				farm.setTotalOrganicTrees(totOrgTrees);
				farm.setTotalConventionalTrees(totConvTrees);
				farm.setTotalAvocadoTrees(totAvocadoTrees);
				farm.setHectarOrganicTrees(hectarOrgTrees);
				farm.setHectarConventionalTrees(hectarConvTrees);
				farm.setHectarAvocadoTrees(hectarAvocadoTrees);
				
			
			
		/*	String organicVariety=productDistributionService.findOrganicVartyByFarm(farm.getId(),getLocaleProperty("organicVarieyCode"));
			String convenalVariety=productDistributionService.findConvnVartyByFarm(farm.getId(),getLocaleProperty("convenVarieyCode"));
				
			if(!StringUtil.isEmpty(organicVariety))
			{
				orgnaicVarietyList=new ArrayList<>();
				String spiltVarty[]=organicVariety.split("\\s*,\\s*");
				
				for(int i=0;i<spiltVarty.length;i++)
				{
					FarmerDynamicFieldsValue dynamicFieldsValue=new FarmerDynamicFieldsValue();
					String datas[]=spiltVarty[i].split("~");
					String fieldName=getLocaleProperty("totalOrgVarty")+" "+getCatlogueValueByCode(String.valueOf(datas[0])).getName();
					String noOfTrees=datas[1];
					dynamicFieldsValue.setFieldName(fieldName);
					dynamicFieldsValue.setFieldValue(noOfTrees);
					orgnaicVarietyList.add(dynamicFieldsValue);
				}
				
			}
			
			
			if(!StringUtil.isEmpty(convenalVariety))
			{
				conventionalVarietyList=new ArrayList<>();
				String spiltVarty[]=convenalVariety.split("\\s*,\\s*");
				
				for(int i=0;i<spiltVarty.length;i++)
				{
					FarmerDynamicFieldsValue dynamicFieldsValue=new FarmerDynamicFieldsValue();
					String datas[]=spiltVarty[i].split("~");
					String fieldName=getLocaleProperty("totalConvnVarty")+" "+getCatlogueValueByCode(String.valueOf(datas[0])).getName();
					String noOfTrees=datas[1];
					dynamicFieldsValue.setFieldName(fieldName);
					dynamicFieldsValue.setFieldValue(noOfTrees);
					conventionalVarietyList.add(dynamicFieldsValue);
				}
				
			}
			*/
			
				
			}
			if (farm == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			} else {

				if (farm.getPhoto() != null)
					setFarmImageByteString(Base64Util.encoder(farm.getPhoto()));

				if (getCurrentTenantId().equalsIgnoreCase("atma")) {
					farmerSoilTestingList = farmerService.listFarmerSoilTestingByFarmId(String.valueOf(farm.getId()));
					List<FarmerLandDetails> Temp = farmerService.listFarmingSystemByFarmId(farm.getId());
					for (FarmerLandDetails details : Temp) {
						String seasonCode = farmerService.findBySeasonCode(details.getSeasonCode());
						details.setSeasonCode(seasonCode);
						farmerLandDetailsList.add(details);
					}
				}
	
				expenditureList = farmerService.listExpentitureListByFarmId(Long.valueOf(farm.getId()));

				if (isCertifiedFarmer || getCurrentTenantId().equalsIgnoreCase("pratibha")) {
					if (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo())) {
						if (farm.getFarmDetailedInfo().getInternalInspectionDate() != null)
							dateOfInspection = df.format(farm.getFarmDetailedInfo().getInternalInspectionDate());
						farmOwnedDetail = (farm.getFarmDetailedInfo().getFarmOwned() == SELECT_MULTI) ? ""
								: getFarmOwnedList().get(farm.getFarmDetailedInfo().getFarmOwned());
						soilFertilityDetail = (farm.getFarmDetailedInfo().getSoilFertility() == SELECT) ? ""
								: getSoilFertilityList().get(farm.getFarmDetailedInfo().getSoilFertility());
						landUnderICSStatusDetail = (farm.getFarmDetailedInfo().getLandUnderICSStatus() == SELECT) ? ""
								: getIcsStatusList().get(farm.getFarmDetailedInfo().getLandUnderICSStatus());

						catalogueList = farmerService.listFarmEquipmentBasedOnType();
						catalogueList1 = farmerService.listFarmAnimalBasedOnType();
						catalogueList2 = farmerService.listFodderBasedOnType();
						catalogueList3 = farmerService.listAnimalHousingBasedOnType();
						catalogueList4 = farmerService.listRevenueBasedOnType();
						machinaryList = farmerService.listMachinary();
						polyList = farmerService.listPolyHouse();
					}
					setCurrentPage(getCurrentPage());
					command = UPDATE;
					view = FARM_CERTIFIED_DETAIL;
				} else {
					setCurrentPage(getCurrentPage());
					command = UPDATE;
					view = DETAIL;
				}
				if (farm.getActiveCoordinates()!= null && farm.getActiveCoordinates().getFarmCoordinates() !=null && !ObjectUtil.isEmpty(farm.getActiveCoordinates()) &&  !ObjectUtil.isListEmpty(farm.getActiveCoordinates().getFarmCoordinates())) {
					jsonObjectList = getFarmJSONObjects(farm.getActiveCoordinates().getFarmCoordinates());
				} else {
					jsonObjectList = new ArrayList();
				}

				if (!StringUtil.isEmpty(farm.getFarmDetailedInfo())) {
					if (!StringUtil.isEmpty(farm.getFarmDetailedInfo().getFarmOwned())) {
						if (farm.getFarmDetailedInfo().getFarmOwned().equals("99")) {
							FarmCatalogue frmOwnStr = getCatlogueValueByCode(farm.getFarmDetailedInfo().getFarmOwned());
							farmOwnedDetail = frmOwnStr.getName() + ": " + farm.getFarmOther();
						}
					}
				}

				request.setAttribute(HEADING, getText(DETAIL));
			}

			/*
			 * if (!StringUtil.isEmpty(farm.getLockExist())) {
			 * addActionError(FARMLOCKED); }
			 */
		} else {
			request.setAttribute(HEADING, getText(LIST));
			return LIST;
		}
		return view;
	}

	private String treeDetailToJson(Set<TreeDetail> treeSet) {
		Set<TreeDetail> treeDetail = new HashSet<TreeDetail>();
		for (TreeDetail exp : treeSet) {

			TreeDetail treeDetailVal = new TreeDetail();
			treeDetailVal.setProdStatus(getText("productStatus-"+exp.getProdStatus()));
			ProcurementVariety variety=productDistributionService.findProcurementVariertyByCode(exp.getVariety());
			treeDetailVal.setVariety(variety.getName());
			treeDetailVal.setYears(getCatlogueValueByCode(exp.getYears()).getName());
			treeDetail.add(treeDetailVal);
		}
		Set<TreeDetail> treeDetailValuSet = new HashSet<TreeDetail>(treeDetail);
		Gson gson = new Gson();
		String res = gson.toJson(treeDetailValuSet);
		return res;
	}

	/**
	 * Delete.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String delete() throws Exception {

		if (id != null && !id.equals("")) {
			farm = farmerService.findFarmById(Long.valueOf(id));
			setCurrentPage(getCurrentPage());
			if (farm == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			} else {

				//String flag = farmerService.isFarmMappingexist(farm.getFarmer().getId());
				String farmcropflag = farmerService.isFarmInventoryMappingexist(farm.getId());
				if (StringUtil.isEmpty(farmcropflag)) {

					farmerService.removeFarm(farm);
				} else {
					
					if(farmcropflag!=null){
					addActionError(getText(farmcropflag));}
					if (farm.getFarmer().getIsCertifiedFarmer() == Farmer.CERTIFIED_NO) {
						request.setAttribute(HEADING, getText(DETAIL));
						return DETAIL;
					} else {
						request.setAttribute(HEADING, getText(FARM_CERTIFIED_DETAIL));
						return FARM_CERTIFIED_DETAIL;
					}
				}
			}
		}
		request.setAttribute(HEADING, getText(LIST));
		return "farmerDetail";
	}

	/**
	 * Gets the farm photo.
	 * 
	 * @return the farm photo
	 */
	public void getFarmPhoto() {

		if (!StringUtil.isEmpty(id)) {
			try {
				byte[] farmPhoto = farmerService.findFarmPhotoById(Long.valueOf(id));
				response.setContentType("image/jpeg");
				OutputStream out = response.getOutputStream();
				out.write(farmPhoto);
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Gets the farmers list.
	 * 
	 * @return the farmers list
	 */
	public Map<String, String> getFarmersList() {

		Map<String, String> farmersListMap = new LinkedHashMap<String, String>();
		List<Farmer> farmersList = farmerService.listFarmer();
		if (!ObjectUtil.isListEmpty(farmersList)) {
			for (Farmer farmer : farmersList) {
				farmersListMap.put(farmer.getFarmerId(), farmer.getFirstName() + "-" + farmer.getFarmerId());
			}
		}
		return farmersListMap;
	}

	/**
	 * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
	 */
	@SuppressWarnings("static-access")
	@Override
	public Object getData() {

		landMeasurementList.put(FarmDetailedInfo.HECTARES, getText("Hectares"));
		landMeasurementList.put(FarmDetailedInfo.ACRES, getText("Acres"));
		certificationLevels = formCertificationLevels("fcl", certificationLevels);
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			setEnableSoliTesting(preferences.getPreferences().get(ESESystem.SOIL_TESTING));
			setCropInfoEnabled(preferences.getPreferences().get(ESESystem.ENABLE_CROP_INFO));
			setGramPanchayatEnable(preferences.getPreferences().get(ESESystem.ENABLE_GRAMPANJAYAT));
			setLat(preferences.getPreferences().get(ESESystem.DEFAULT_LATITUDE));  
			setLon(preferences.getPreferences().get(ESESystem.DEFAULT_LONGTITUDE));
		}

		ffsList.put(1, getText("ffs-1"));
		ffsList.put(2, getText("ffs-2"));

		milletList.put(1, getText("millest-1"));
		milletList.put(2, getText("millest-2"));

		borewellList.put(1, getText("bore-1"));
		borewellList.put(2, getText("bore-2"));

		processingActList.put(1, getText("processAct-1"));
		processingActList.put(2, getText("processAct-2"));

		soilTestMap = formHousingType("soilTesting", soilTestMap);

		qualifiedTestMap.put(1, getText("qualified-1"));
		qualifiedTestMap.put(0, getText("qualified-0"));
		
		parallelProductionList.put(1,getText("parallel-1"));
		parallelProductionList.put(2,getText("parallel-2"));
		
		hiredLabourList.put(1, getText("hired-1"));
		hiredLabourList.put(2, getText("hired-2"));
		
		riskCategoryList.put(1, getText("risk-1"));
		riskCategoryList.put(2, getText("risk-2"));
		
		bananaTreesList.put(1, getText("banana-1"));
		bananaTreesList.put(2, getText("banana-2"));
		
		cropTypeList.put(1, getText("productStatus-1"));
		cropTypeList.put(2, getText("productStatus-2"));

		soilList = formHousingType("soli", soilList);
		if (!ObjectUtil.isEmpty(farm)) {

			if (!ObjectUtil.isEmpty(farmerId)) {
				Farmer farmer1 = farmerService.findFarmerByFarmerId(farmerId);
				farm.setFarmer(farmer1);
			}

			if (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo()) && farm.getFarmDetailedInfo().isSameAddressofFarmer()) {
				farm.getFarmDetailedInfo().setFarmAddress(farm.getFarmer().getAddress() + " "
						+ farm.getFarmer().getCity().getName() + " " + farm.getFarmer().getVillage().getName());

				if (farm.getFarmDetailedInfo().getFarmAddress().trim().length() > 255)
					farm.getFarmDetailedInfo().setFarmAddress(farm.getFarmer().getAddress());
			}

			if (!StringUtil.isEmpty(landInProduction))
				farm.setLandInProduction(landInProduction);

			if (!StringUtil.isEmpty(landInProduction))
				farm.setLandNotInProduction(landNotInProduction);

			if (FarmDetailedInfo.HECTARES.equals(selectedLandMeasurement)) {
				farm.getFarmDetailedInfo().setLandMeasurement(FarmDetailedInfo.HECTARES);
			} else if (FarmDetailedInfo.ACRES.equals(selectedLandMeasurement)) {
				farm.getFarmDetailedInfo().setLandMeasurement(FarmDetailedInfo.ACRES);
			}

			if (!StringUtil.isEmpty(selectedVillage)) {
				Village village = locationService.findVillageById(Long.valueOf(selectedVillage));
				farm.setVillage(village);
			}
			if (!StringUtil.isEmpty(selectedSamithi)) {
				Warehouse samithi = locationService.findSamithiById(Long.valueOf(selectedSamithi));
				farm.setSamithi(samithi);
			}
			if (!StringUtil.isEmpty(selectedFpo)) {
				farm.setFpo(selectedFpo);
			}
			if (!StringUtil.isEmpty(regYearString)) {
				farm.getFarmDetailedInfo().setRegYear(regYearString);

			}
			isSoilTested.put(farm.SOILTESTED_NO, getText("no"));
			isSoilTested.put(farm.SOILTESTED_YES, getText("yes"));
			farm.setTenantId(getCurrentTenantId());
		}

		return farm;
	}

	/**
	 * Detail check for certified farmer.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void detailCheckForCertifiedFarmer() throws Exception {

		String result = "NO";
		if (!StringUtil.isEmpty(selectedFarmerId)) {
			Farmer farmer = farmerService.findFarmerByFarmerId(selectedFarmerId);
			// if (farmer.getCertificationType() !=
			// Farmer.CERTIFICATION_TYPE_NONE)
			if (farmer.getIsCertifiedFarmer() != Farmer.CERTIFIED_NO)
				result = "YES";
		}
		response.setContentType("text/html");
		response.getWriter().print(result);
	}

	/**
	 * Detail farm address same as farmer.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void detailFarmAddressSameAsFarmer() throws Exception {

		String result = "";
		if (!StringUtil.isEmpty(selectedFarmerId)) {
			List<Object[]> farmer = farmerService.listFarmerAddressByFarmerId(selectedFarmerId);
			if (!ObjectUtil.isEmpty(farmer)) {
				for (Object[] obj : farmer) {
					if (!ObjectUtil.isEmpty(obj[0]) && !"".equalsIgnoreCase(obj[0].toString()) ) {
						result = obj[0] + "," + obj[1] + "," + obj[2];
					} else {
						result = obj[1] + "," + obj[2];
					}

					if (result.trim().length() > 255)
						result = obj[0].toString();
				}

			}

		}
		response.setContentType("text/html");
		response.getWriter().print(result);
	}

	public void populateSanghamType() throws IOException {

		String result = "";
		if (!StringUtil.isEmpty(farmerId)) {
			Farmer farmer = farmerService.findFarmerByFarmerId(farmerId);
			result = farmer.getSangham();
		}
		response.setContentType("text/html");
		response.getWriter().print(result);
	}

	/**
	 * Gets the inventory item filter.
	 * 
	 * @return the inventory item filter
	 */
	public String getInventoryItemFilter() {

		StringBuffer sb = new StringBuffer();
		String values = getText("inventoryItemsList");
		String[] valuesArray = values.split(",");
		sb.append("-1:").append(FILTER_ALL).append(";");
		for (int count = 0; count < valuesArray.length; count++) {
			String[] data = valuesArray[count].split("\\~");
			sb.append(data[0]).append(":").append(data[1]).append(";");
		}
		String data = sb.toString();
		return data.substring(0, data.length() - 1);
	}

	/**
	 * Gets the farm animal filter.
	 * 
	 * @return the farm animal filter
	 */
	public String getFarmAnimalFilter() {

		StringBuffer sb = new StringBuffer();
		String values = getText("farmAnimalList");
		String[] valuesArray = values.split(",");
		sb.append("-1:").append(FILTER_ALL).append(";");
		for (int count = 0; count < valuesArray.length; count++) {
			String[] data = valuesArray[count].split("\\~");
			sb.append(data[0]).append(":").append(data[1]).append(";");
		}
		String data = sb.toString();
		return data.substring(0, data.length() - 1);
	}

	/**
	 * Gets the feeding used filter.
	 * 
	 * @return the feeding used filter
	 */
	public String getFeedingUsedFilter() {

		StringBuffer sb = new StringBuffer();
		String values = getText("feedingUsedList");
		String[] valuesArray = values.split(",");
		sb.append("-1:").append(FILTER_ALL).append(";");
		for (int count = 0; count < valuesArray.length; count++) {
			sb.append(count).append(":").append(valuesArray[count]).append(";");
		}
		String data = sb.toString();
		return data.substring(0, data.length() - 1);
	}

	/**
	 * Gets the animal housing filter.
	 * 
	 * @return the animal housing filter
	 */
	public String getAnimalHousingFilter() {

		StringBuffer sb = new StringBuffer();
		String values = getText("animalHousingList");
		String[] valuesArray = values.split(",");
		sb.append("-1:").append(FILTER_ALL).append(";");
		for (int count = 0; count < valuesArray.length; count++) {
			String[] data = valuesArray[count].split("\\~");
			sb.append(data[0]).append(":").append(data[1]).append(";");
		}
		String data = sb.toString();
		return data.substring(0, data.length() - 1);
	}

	/**
	 * Gets the farm owned list.
	 * 
	 * @return the farm owned list
	 */
	public Map<String, String> getFarmOwnedList() {

		Map<String, String> list = new LinkedHashMap<>();

		list = getFarmCatalougeMap(Integer.valueOf(getText("landOwnerShip")));
		return list;
	}

	/**
	 * Gets the farm irrigation list.
	 * 
	 * @return the farm irrigation list
	 */
	public Map<Integer, String> getFarmIrrigationList() {

		if (farmIrrigationList.size() == 0) {
			String[] farmIrrigation = getLocaleProperty("farmIrrigationList").split(",");
			int i = 1;
			for (String farmIrrValue : farmIrrigation) {
				if (!farmIrrValue.equals("Others")) {
					farmIrrigationList.put(i++, farmIrrValue);
				} else {
					farmIrrigationList.put(99, farmIrrValue);
				}
			}
		}

		return farmIrrigationList;
	}

	/**
	 * Gets the irrigation source list.
	 * 
	 * @return the irrigation source list
	 */
	/*
	 * public Map<Integer, String> getIrrigationSourceList() {
	 * 
	 * if (irrigationSourceList.size() == 0) { irrigationSourceList =
	 * getPropertyData("irrigationSourceList"); return irrigationSourceList; }
	 * return irrigationSourceList; }
	 */

	public Map<String, String> getIrrigationSourceList() {

		Map<String, String> list = new LinkedHashMap<>();

		list = getFarmCatalougeMap(Integer.valueOf(getText("irrigationSourceList")));
		return list;

	}

	/**
	 * Gets the irrigation method list.
	 * 
	 * @return the irrigation method list
	 */
	public Map<Integer, String> getIrrigationMethodList() {

		if (irrigationMethodList.size() == 0) {
			String[] irrigationMethod = getText("irrigationMethodList").split(",");
			int i = 1;
			for (String irriMethodValue : irrigationMethod)
				irrigationMethodList.put(i++, irriMethodValue);

		}
		return irrigationMethodList;
	}

	/**
	 * Gets the soil type list.
	 * 
	 * @return the soil type list
	 */

	public Map<String, String> getSoilTypeList() {

		/*
		 * if (soilTypeList.size() == 0) { String[] soilType =
		 * getText("soilTypeList").split(","); int i = 1; for (String
		 * soilTypeValue : soilType) soilTypeList.put(i++, soilTypeValue); }
		 */

		Map<String, String> soilTypeMap = new LinkedHashMap<String, String>();

		List<FarmCatalogue> soilTypeList = farmerService.listCatelogueType(getText("soilType"));
		for (FarmCatalogue obj : soilTypeList) {
			//soilTypeMap.put(obj.getCode(), obj.getName());
			String name = getLanguagePref(getLoggedInUserLanguage(), obj.getCode().trim().toString());
			if(!StringUtil.isEmpty(name) && name != null){
				soilTypeMap.put(obj.getCode().toString(), obj.getCode().toString()+ "-" +getLanguagePref(getLoggedInUserLanguage(), obj.getCode().toString()));
			}else{
				/*soilTypeMap.put(obj.getCode(), obj.getCode() + "-" + obj.getName());*/
				soilTypeMap.put(obj.getCode(), obj.getName());
			}
		}
		return soilTypeMap;
	}

	/*
	 * public Map<String, String> getMethodOfIrrigationList() { Map<String,
	 * String> methodOfIrrigationMap = new LinkedHashMap<String, String>();
	 * List<FarmCatalogue> methodOfIrrigationList =
	 * farmerService.listCatelogueType(getText("methodOfIrrigation")); for
	 * (FarmCatalogue obj : methodOfIrrigationList) {
	 * methodOfIrrigationMap.put(obj.getCode(), obj.getName()); } return
	 * methodOfIrrigationMap; }
	 */

	public Map<String, String> getMethodOfIrrigationList() {

		Map<String, String> list = new LinkedHashMap<>();

		list = getFarmCatalougeMap(Integer.valueOf(getText("methodOfIrrigation")));
		return list;
	}

	/**
	 * Gets the soil fertility list.
	 * 
	 * @return the soil fertility list
	 */
	public Map<Integer, String> getSoilFertilityList() {

		if (soilFertilityList.size() == 0) {
			String[] soilFertility = getText("soilFertilityList").split(",");
			int i = 1;
			for (String sfertilityValue : soilFertility)
				soilFertilityList.put(i++, sfertilityValue);
		}
		return soilFertilityList;
	}

	/**
	 * Gets the ics status list.
	 * 
	 * @return the ics status list
	 */
	public Map<Integer, String> getIcsStatusList() {

		String[] icsStatus = getLocaleProperty("icsStatusList").split(",");
		for (int i = 0; i < icsStatus.length; i++)
			icsStatusList.put(i, icsStatus[i]);

		return icsStatusList;
	}
	
	public Map<String, String> getListYears() {
		Map<String, String> yearList = getFarmCatalougeMap(Integer.valueOf(getText("listYears")));

		return yearList;
	}
		
	public Map<String, String> getListOfProcurementVariety() {

		Map<String, String> procurementVarietyList = new LinkedHashMap<String, String>();
			listProcurementVariety = productDistributionService.listProcurementVariety();
		for (ProcurementVariety procurementVariety : listProcurementVariety) {
			procurementVarietyList.put(procurementVariety.getCode(), procurementVariety.getName());
		}
		return procurementVarietyList;
	}

	public String populateDownload() {

		if (Long.valueOf(id) > 0) {
			try {
				byte[] audioContent = farmerService.findfarmerVerificationFarmerVoiceById(Long.valueOf(id));
				if (ObjectUtil.isEmpty(audioContent)) {
					return REDIRECT;
				}
				response.setContentType("audio/mpeg");
				response.setHeader("Content-Disposition",
						"attachment;filename=" + "_" + fileNameDateFormat.format(new Date()) + ".mp3");
				response.getOutputStream().write(audioContent);
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
		return null;
	}

	public String populateAudioPlay() {

		if (Long.valueOf(id) > 0) {
			try {

				byte[] audioContent = farmerService.findfarmerVerificationFarmerVoiceById(Long.valueOf(id));
				if (ObjectUtil.isEmpty(audioContent)) {
					return REDIRECT;
				}
				response.setContentType("audio/mpeg");
				response.setContentLength(audioContent.length);
				response.setHeader("Content-Disposition", "attachment;filename=" + String.valueOf(this.getId()) + "_"
						+ fileNameDateFormat.format(new Date()) + ".mp3");
				response.getOutputStream().write(audioContent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;

	}
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	// check farm crops exist for farm by using ajax call
	/*
	 * public String findFarmCropsExist() throws Exception { PrintWriter out =
	 * null; if (id != null && !id.equals("")) { isFarmCropsExist =
	 * farmerService.findFarmCropsExistForFarmByFarmId(Long.parseLong(id)); try
	 * { response.setContentType("text/html"); out = response.getWriter();
	 * out.write(String.valueOf(isFarmCropsExist)); } catch (Exception e) {
	 * e.printStackTrace(); } finally { if (out != null) { out.flush();
	 * out.close(); } } } return null; }
	 */

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
	 * Gets the farm.
	 * 
	 * @return the farm
	 */
	public Farm getFarm() {

		return farm;
	}

	/**
	 * Sets the farm.
	 * 
	 * @param farm
	 *            the new farm
	 */
	public void setFarm(Farm farm) {

		this.farm = farm;
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
	 * Sets the tab index.
	 * 
	 * @param tabIndex
	 *            the new tab index
	 */
	public void setTabIndex(String tabIndex) {

		this.tabIndex = tabIndex;
	}

	/**
	 * Gets the tab index.
	 * 
	 * @return the tab index
	 */
	public String getTabIndex() {

		return URLDecoder.decode(tabIndex);
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
	 * Sets the land in production.
	 * 
	 * @param landInProduction
	 *            the new land in production
	 */
	public void setLandInProduction(String landInProduction) {

		this.landInProduction = landInProduction;
	}

	/**
	 * Gets the land in production.
	 * 
	 * @return the land in production
	 */
	public String getLandInProduction() {

		return landInProduction;
	}

	/**
	 * Sets the land not in production.
	 * 
	 * @param landNotInProduction
	 *            the new land not in production
	 */
	public void setLandNotInProduction(String landNotInProduction) {

		this.landNotInProduction = landNotInProduction;
	}

	/**
	 * Gets the land not in production.
	 * 
	 * @return the land not in production
	 */
	public String getLandNotInProduction() {

		return landNotInProduction;
	}

	/**
	 * Gets the farmer detail params.
	 * 
	 * @return the farmer detail params
	 */
	public String getFarmerDetailParams() {

		return "tabIndex=" + URLEncoder.encode(tabIndex) + "&id=" + getId() + "&" + tabIndex;
	}

	/**
	 * Sets the total area.
	 * 
	 * @param totalArea
	 *            the new total area
	 */
	public void setTotalArea(String totalArea) {

		this.totalArea = totalArea;
	}

	/**
	 * Gets the total area.
	 * 
	 * @return the total area
	 */
	public String getTotalArea() {

		return totalArea;
	}

	/**
	 * Checks if is certified farmer.
	 * 
	 * @return true, if is certified farmer
	 */
	public boolean isCertifiedFarmer() {

		return isCertifiedFarmer;
	}

	/**
	 * Sets the certified farmer.
	 * 
	 * @param isCertifiedFarmer
	 *            the new certified farmer
	 */
	public void setCertifiedFarmer(boolean isCertifiedFarmer) {

		this.isCertifiedFarmer = isCertifiedFarmer;
	}

	/**
	 * Gets the selected farmer id.
	 * 
	 * @return the selected farmer id
	 */
	public String getSelectedFarmerId() {

		return selectedFarmerId;
	}

	/**
	 * Sets the selected farmer id.
	 * 
	 * @param selectedFarmerId
	 *            the new selected farmer id
	 */
	public void setSelectedFarmerId(String selectedFarmerId) {

		this.selectedFarmerId = selectedFarmerId;
	}

	/**
	 * Gets the land measurement list.
	 * 
	 * @return the land measurement list
	 */
	public Map<String, String> getLandMeasurementList() {

		return landMeasurementList;
	}

	/**
	 * Sets the land measurement list.
	 * 
	 * @param landMeasurementList
	 *            the land measurement list
	 */
	public void setLandMeasurementList(Map<String, String> landMeasurementList) {

		this.landMeasurementList = landMeasurementList;
	}

	/**
	 * Sets the farm owned list.
	 * 
	 * @param farmOwnedList
	 *            the farm owned list
	 */

	public String getSelectedFarmOwned() {

		return selectedFarmOwned;
	}

	public void setFarmOwnedList(Map<String, String> farmOwnedList) {

		this.farmOwnedList = farmOwnedList;
	}

	public void setSelectedFarmOwned(String selectedFarmOwned) {

		this.selectedFarmOwned = selectedFarmOwned;
	}

	/**
	 * Sets the selected irrigation.
	 * 
	 * @param selectedIrrigation
	 *            the new selected irrigation
	 */
	public void setSelectedIrrigation(String selectedIrrigation) {

		this.selectedIrrigation = selectedIrrigation;
	}

	/**
	 * Gets the selected irrigation.
	 * 
	 * @return the selected irrigation
	 */
	public String getSelectedIrrigation() {

		return selectedIrrigation;
	}

	/**
	 * Sets the farm irrigation list.
	 * 
	 * @param farmIrrigationList
	 *            the farm irrigation list
	 */
	public void setFarmIrrigationList(Map<Integer, String> farmIrrigationList) {

		this.farmIrrigationList = farmIrrigationList;
	}

	/**
	 * Sets the selected irrigation source.
	 * 
	 * @param selectedIrrigationSource
	 *            the new selected irrigation source
	 */
	public void setSelectedIrrigationSource(String selectedIrrigationSource) {

		this.selectedIrrigationSource = selectedIrrigationSource;
	}

	/**
	 * Gets the selected irrigation source.
	 * 
	 * @return the selected irrigation source
	 */
	public String getSelectedIrrigationSource() {

		return selectedIrrigationSource;
	}

	/**
	 * Sets the irrigation source list.
	 * 
	 * @param irrigationSourceList
	 *            the irrigation source list
	 */
	public void setIrrigationSourceList(Map<Integer, String> irrigationSourceList) {

		this.irrigationSourceList = irrigationSourceList;
	}

	/*	*//**
			 * Sets the selected irrigation method.
			 * 
			 * @param selectedIrrigationMethod
			 *            the new selected irrigation method
			 */
	/*
	 * public void setSelectedIrrigationMethod(int selectedIrrigationMethod) {
	 * this.selectedIrrigationMethod = selectedIrrigationMethod; }
	 *//**
		 * Gets the selected irrigation method.
		 * 
		 * @return the selected irrigation method
		 *//*
		 * public int getSelectedIrrigationMethod() { return
		 * selectedIrrigationMethod; }
		 */

	/**
	 * Sets the irrigation method list.
	 * 
	 * @param irrigationMethodList
	 *            the irrigation method list
	 */
	public void setIrrigationMethodList(Map<Integer, String> irrigationMethodList) {

		this.irrigationMethodList = irrigationMethodList;
	}

	/**
	 * Sets the selected soil type.
	 * 
	 * @param selectedSoilType
	 *            the new selected soil type
	 */
	public void setSelectedSoilType(String selectedSoilType) {

		this.selectedSoilType = selectedSoilType;
	}

	/**
	 * Gets the selected soil type.
	 * 
	 * @return the selected soil type
	 */
	public String getSelectedSoilType() {

		return selectedSoilType;
	}

	/**
	 * Sets the soil type list.
	 * 
	 * @param soilTypeList
	 *            the soil type list
	 */
	public void setSoilTypeList(Map<Integer, String> soilTypeList) {

		this.soilTypeList = soilTypeList;
	}

	/**
	 * Sets the selected soil fertility.
	 * 
	 * @param selectedSoilFertility
	 *            the new selected soil fertility
	 */
	public void setSelectedSoilFertility(int selectedSoilFertility) {

		this.selectedSoilFertility = selectedSoilFertility;
	}

	/**
	 * Gets the selected soil fertility.
	 * 
	 * @return the selected soil fertility
	 */
	public int getSelectedSoilFertility() {

		return selectedSoilFertility;
	}

	/**
	 * Sets the soil fertility list.
	 * 
	 * @param soilFertilityList
	 *            the soil fertility list
	 */
	public void setSoilFertilityList(Map<Integer, String> soilFertilityList) {

		this.soilFertilityList = soilFertilityList;
	}

	/**
	 * Sets the selected ics status.
	 * 
	 * @param selectedICSStatus
	 *            the new selected ics status
	 */
	public void setSelectedICSStatus(int selectedICSStatus) {

		this.selectedICSStatus = selectedICSStatus;
	}

	/**
	 * Gets the selected ics status.
	 * 
	 * @return the selected ics status
	 */
	public int getSelectedICSStatus() {

		return selectedICSStatus;
	}

	/**
	 * Sets the ics status list.
	 * 
	 * @param icsStatusList
	 *            the ics status list
	 */
	public void setIcsStatusList(Map<Integer, String> icsStatusList) {

		this.icsStatusList = icsStatusList;
	}

	/**
	 * Sets the selected farmer.
	 * 
	 * @param selectedFarmer
	 *            the new selected farmer
	 */
	public void setSelectedFarmer(String selectedFarmer) {

		this.selectedFarmer = selectedFarmer;
	}

	/**
	 * Gets the selected farmer.
	 * 
	 * @return the selected farmer
	 */
	public String getSelectedFarmer() {

		return selectedFarmer;
	}

	/**
	 * Gets the selected land measurement.
	 * 
	 * @return the selected land measurement
	 */
	public String getSelectedLandMeasurement() {

		return selectedLandMeasurement;
	}

	/**
	 * Sets the selected land measurement.
	 * 
	 * @param selectedLandMeasurement
	 *            the new selected land measurement
	 */
	public void setSelectedLandMeasurement(String selectedLandMeasurement) {

		this.selectedLandMeasurement = selectedLandMeasurement;
	}

	/**
	 * Sets the date of inspection.
	 * 
	 * @param dateOfInspection
	 *            the new date of inspection
	 */
	public void setDateOfInspection(String dateOfInspection) {

		this.dateOfInspection = dateOfInspection;
	}

	/**
	 * Gets the date of inspection.
	 * 
	 * @return the date of inspection
	 */
	public String getDateOfInspection() {

		return dateOfInspection;
	}

	/**
	 * Sets the farm owned detail.
	 * 
	 * @param farmOwnedDetail
	 *            the new farm owned detail
	 */
	public void setFarmOwnedDetail(String farmOwnedDetail) {

		this.farmOwnedDetail = farmOwnedDetail;
	}

	/**
	 * Gets the farm owned detail.
	 * 
	 * @return the farm owned detail
	 */
	public String getFarmOwnedDetail() {

		return farmOwnedDetail;
	}

	/**
	 * Sets the area under irrigation detail.
	 * 
	 * @param areaUnderIrrigationDetail
	 *            the new area under irrigation detail
	 */
	public void setAreaUnderIrrigationDetail(String areaUnderIrrigationDetail) {

		this.areaUnderIrrigationDetail = areaUnderIrrigationDetail;
	}

	/**
	 * Gets the area under irrigation detail.
	 * 
	 * @return the area under irrigation detail
	 */
	public String getAreaUnderIrrigationDetail() {

		return areaUnderIrrigationDetail;
	}

	/**
	 * Sets the farm irrigation detail.
	 * 
	 * @param farmIrrigationDetail
	 *            the new farm irrigation detail
	 */
	public void setFarmIrrigationDetail(String farmIrrigationDetail) {

		this.farmIrrigationDetail = farmIrrigationDetail;
	}

	/**
	 * Gets the farm irrigation detail.
	 * 
	 * @return the farm irrigation detail
	 */
	public String getFarmIrrigationDetail() {

		return farmIrrigationDetail;
	}

	/**
	 * Sets the irrigation source detail.
	 * 
	 * @param irrigationSourceDetail
	 *            the new irrigation source detail
	 */
	public void setIrrigationSourceDetail(String irrigationSourceDetail) {

		this.irrigationSourceDetail = irrigationSourceDetail;
	}

	/**
	 * Gets the irrigation source detail.
	 * 
	 * @return the irrigation source detail
	 */
	public String getIrrigationSourceDetail() {

		return irrigationSourceDetail;
	}

	/**
	 * Sets the irrigation method detail.
	 * 
	 * @param irrigationMethodDetail
	 *            the new irrigation method detail
	 */
	public void setIrrigationMethodDetail(String irrigationMethodDetail) {

		this.irrigationMethodDetail = irrigationMethodDetail;
	}

	/**
	 * Gets the irrigation method detail.
	 * 
	 * @return the irrigation method detail
	 */
	public String getIrrigationMethodDetail() {

		return irrigationMethodDetail;
	}

	/**
	 * Sets the soil type detail.
	 * 
	 * @param soilTypeDetail
	 *            the new soil type detail
	 */
	public void setSoilTypeDetail(String soilTypeDetail) {

		this.soilTypeDetail = soilTypeDetail;
	}

	/**
	 * Gets the soil type detail.
	 * 
	 * @return the soil type detail
	 */
	public String getSoilTypeDetail() {

		return soilTypeDetail;
	}

	/**
	 * Sets the soil fertility detail.
	 * 
	 * @param soilFertilityDetail
	 *            the new soil fertility detail
	 */
	public void setSoilFertilityDetail(String soilFertilityDetail) {

		this.soilFertilityDetail = soilFertilityDetail;
	}

	/**
	 * Gets the soil fertility detail.
	 * 
	 * @return the soil fertility detail
	 */
	public String getSoilFertilityDetail() {

		return soilFertilityDetail;
	}

	/**
	 * Sets the land under ics status detail.
	 * 
	 * @param landUnderICSStatusDetail
	 *            the new land under ics status detail
	 */
	public void setLandUnderICSStatusDetail(String landUnderICSStatusDetail) {

		this.landUnderICSStatusDetail = landUnderICSStatusDetail;
	}

	/**
	 * Gets the land under ics status detail.
	 * 
	 * @return the land under ics status detail
	 */
	public String getLandUnderICSStatusDetail() {

		return landUnderICSStatusDetail;
	}

	public String getTabIndexFarmer() {

		return tabIndexFarmer;
	}

	public void setTabIndexFarmer(String tabIndexFarmer) {

		this.tabIndexFarmer = tabIndexFarmer;
	}

	/**
	 * Gets the farmer detail params.
	 * 
	 * @return the farmer detail params
	 */
	@SuppressWarnings("deprecation")
	public String getFarmerDetailzParams() {

		return "tabIndex=" + URLEncoder.encode(tabIndexFarmer) + "&id=" + getFarmerUniqueId() + "&" + tabIndexFarmer;
	}

	public String getFarmerName() {

		return farmerName;
	}

	public void setFarmerName(String farmerName) {

		this.farmerName = farmerName;
	}

	public String getTabIndexFarm() {

		return tabIndexFarm;
	}

	public void setTabIndexFarm(String tabIndexFarm) {

		this.tabIndexFarm = tabIndexFarm;
	}

	public Map<String, String> getLandGradientList() {

		/*
		 * AtomicInteger i = new AtomicInteger(0); Map<String, String>
		 * landGradientList = new LinkedHashMap<>(); FarmCatalogueMaster
		 * farmCatalougeMaster =
		 * catalogueService.findFarmCatalogueMasterByName(getText(
		 * "farm.landGradient")); if (!ObjectUtil.isEmpty(farmCatalougeMaster))
		 * { Double d = new Double(farmCatalougeMaster.getId());
		 * List<FarmCatalogue> farmCatalougeList =
		 * catalogueService.findFarmCatalougeByAlpha(d.intValue());
		 * for(FarmCatalogue catalogue:farmCatalougeList) {
		 * if(!catalogue.getName().trim().contentEquals(ESESystem.OTHERS)) {
		 * landGradientList.put(catalogue.getCode(), catalogue.getName()); } } }
		 * landGradientList.put("99", "Others");
		 */
		Map<String, String> landGradientList = new LinkedHashMap<>();
	
			landGradientList = getFarmCatalougeMap(Integer.valueOf(getLocaleProperty("landGradientType").trim()));
		
		return landGradientList;
	}

	public Map<String, String> getWaterSourceList() {
		Map<String, String> waterSourceList = new LinkedHashMap<>();
		waterSourceList = getFarmCatalougeMap(Integer.valueOf("122"));
		return waterSourceList;
	}

	public void setLandGradientList(Map<Integer, String> landGradientList) {

		this.landGradientList = landGradientList;
	}

	public Map<String, String> getSoilTextureList() {

		/*
		 * if (soilTextureList.size() == 0) { String[] texture =
		 * getText("soilTextureList").split(","); int i = 1; for (String
		 * textureValue : texture) soilTextureList.put(i++, textureValue); }
		 * return soilTextureList;
		 */

		Map<String, String> soilTexMap = new LinkedHashMap<String, String>();

		List<FarmCatalogue> soilTexList = farmerService.listCatelogueType(getText("soilTexture").trim());
		for (FarmCatalogue obj : soilTexList) {
			//soilTexMap.put(obj.getCode(), obj.getName());
			String name = getLanguagePref(getLoggedInUserLanguage(), obj.getCode().trim().toString());
			if(!StringUtil.isEmpty(name) && name != null){
				soilTexMap.put(obj.getCode().toString(), obj.getCode().toString()+ "-" +getLanguagePref(getLoggedInUserLanguage(), obj.getCode().toString()));
			}else{
				soilTexMap.put(obj.getCode(), obj.getName());
			}
		}
		return soilTexMap;
	}

	public void setSoilTextureList(Map<Integer, String> soilTextureList) {

		this.soilTextureList = soilTextureList;
	}

	public List<String> getRegYearList() {

		int curYear = DateUtil.getCurrentYear();
		int startYear = curYear - 65;
		int endYear = curYear + 1;
		List<String> list = new ArrayList<String>();
		for (int i = startYear; i < endYear; i++) {
			list.add(String.valueOf(i));
		}
		return list;
	}

	public String deletefile() {

		try {
			farmerService.deleteelemetbyId(Long.parseLong(fileid));
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", fileid);
			jsonObject.put("success", getText("successful"));
			PrintWriter out = response.getWriter();
			out.println(jsonObject.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getFileid() {

		return fileid;
	}

	public void setFileid(String fileid) {

		this.fileid = fileid;
	}

	public String getSelectedTexture() {

		return selectedTexture;
	}

	public void setSelectedTexture(String selectedTexture) {

		this.selectedTexture = selectedTexture;
	}

	public String getSelectedGradient() {

		return selectedGradient;
	}

	public void setSelectedGradient(String selectedGradient) {

		this.selectedGradient = selectedGradient;
	}

	public String getSoilTextureDetail() {

		return soilTextureDetail;
	}

	public void setSoilTextureDetail(String soilTextureDetail) {

		this.soilTextureDetail = soilTextureDetail;
	}

	public String getLandGradientDetail() {

		return landGradientDetail;
	}

	public void setLandGradientDetail(String landGradientDetail) {

		this.landGradientDetail = landGradientDetail;
	}

	public String getFarmImageByteString() {

		return farmImageByteString;
	}

	public void setFarmImageByteString(String farmImageByteString) {

		this.farmImageByteString = farmImageByteString;
	}

	public List<JSONObject> getJsonObjectList() {

		return jsonObjectList;
	}

	public void setJsonObjectList(List<JSONObject> jsonObjectList) {

		this.jsonObjectList = jsonObjectList;
	}

	public String getAudioDownloadString() {

		return audioDownloadString;
	}

	public void setAudioDownloadString(String audioDownloadString) {

		this.audioDownloadString = audioDownloadString;
	}

	public void prepare() throws Exception {
		Farmer f;
		String actionClassName = ActionContext.getContext().getActionInvocation().getAction().getClass()
				.getSimpleName();
		String content = getLocaleProperty(actionClassName + "." + BreadCrumb.BREADCRUMB);
		if (StringUtil.isEmpty(content) || (content.equalsIgnoreCase(actionClassName + "." + BreadCrumb.BREADCRUMB))) {
			content = getText(BreadCrumb.BREADCRUMB, "");
		}
		if (!StringUtil.isEmpty(farmerId)) {
			f = farmerService.findFarmerByFarmerId(farmerId);
			request.setAttribute(BreadCrumb.BREADCRUMB,
					BreadCrumb.getBreadCrumb(content + "=" + f.getId() + "&tabValue=tabs-2"));

			/*
			 * request.setAttribute(BreadCrumb.BREADCRUMB,
			 * BreadCrumb.getBreadCrumb(content + f.getId() +
			 * "&tabValue=tabs-2"));
			 */

		} else {

			request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(content + "&tabValue=tabs-2"));
		}
	}

	public Map<String, String> getApproadList() {
		
			approadList = getFarmCatalougeMap(Integer.valueOf(getLocaleProperty("approadListType").trim()));
		
		return approadList;
	}

	/**
	 * populateSoilType
	 */

	public void populateSoilType() {

		if (!soilTypeName.equalsIgnoreCase("null") && (!StringUtil.isEmpty(soilTypeName))) {

			FarmCatalogue catalogue = catalogueService.findByNameAndType(soilTypeName,
					Integer.valueOf(getText("soilType")));
			if (catalogue == null) {
				FarmCatalogue farmCatalogue = new FarmCatalogue();
				farmCatalogue.setCode(idGenerator.getCatalogueIdSeq());
				farmCatalogue.setRevisionNo(DateUtil.getRevisionNumber());
				farmCatalogue.setBranchId(getBranchId());
				farmCatalogue.setName(soilTypeName);
				farmCatalogue.setTypez(Integer.valueOf(getText("soilType")));
				farmCatalogue.setStatus(FarmCatalogue.ACTIVE);
				catalogueService.addCatalogue(farmCatalogue);
				JSONArray stateArr = new JSONArray();
				List<FarmCatalogue> soilTypeList = farmerService.listCatelogueType(getText("soilType"));
				if (!ObjectUtil.isEmpty(soilTypeList)) {

					for (FarmCatalogue obj : soilTypeList) {
						stateArr.add(getJSONObject(obj.getCode(), obj.getName()));
					}
				}
				sendAjaxResponse(stateArr);
			} else {
				String result = "0";
				sendAjaxResponse(result);
			}
		}

	}

	/**
	 * populateSoilTex
	 * 
	 * @param id
	 * @param name
	 *            soilTypeName
	 * @return
	 */

	public void populateSoilTex() {

		if (!soilTexName.equalsIgnoreCase("null") && (!StringUtil.isEmpty(soilTexName))) {

			FarmCatalogue catalogue = catalogueService.findByNameAndType(soilTexName,
					Integer.valueOf(getText("soilTexture")));
			if (catalogue == null) {
				FarmCatalogue farmCatalogue = new FarmCatalogue();
				farmCatalogue.setCode(idGenerator.getCatalogueIdSeq());
				farmCatalogue.setRevisionNo(DateUtil.getRevisionNumber());
				farmCatalogue.setBranchId(getBranchId());
				farmCatalogue.setName(soilTexName);
				farmCatalogue.setTypez(Integer.valueOf(getText("soilTexture")));
				farmCatalogue.setStatus(FarmCatalogue.ACTIVE);
				catalogueService.addCatalogue(farmCatalogue);
				JSONArray stateArr = new JSONArray();
				List<FarmCatalogue> soilTexList = farmerService.listCatelogueType(getText("soilTexture"));
				if (!ObjectUtil.isEmpty(soilTexList)) {

					for (FarmCatalogue obj : soilTexList) {
						stateArr.add(getJSONObject(obj.getCode(), obj.getName()));
					}
				}
				sendAjaxResponse(stateArr);
			} else {
				String result = "0";
				sendAjaxResponse(result);
			}
		}

	}

	public void populateDeleteFarmerland() {

		farmerService.deleteFarmerLandDetailById(farmerLandId);
	}

	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}

	public void setApproadList(Map<String, String> approadList) {

		this.approadList = approadList;
	}

	public String getSelectedRoad() {

		return selectedRoad;
	}

	public void setSelectedRoad(String selectedRoad) {

		this.selectedRoad = selectedRoad;
	}

	public String getSessionYearString() {

		return sessionYearString;
	}

	public void setSessionYearString(String sessionYearString) {

		this.sessionYearString = sessionYearString;
	}

	public String getSelectedRoadString() {

		return selectedRoadString;
	}

	public void setSelectedRoadString(String selectedRoadString) {

		this.selectedRoadString = selectedRoadString;
	}

	public List<FarmCatalogue> getCatalogueList() {

		return catalogueList;
	}

	public void setCatalogueList(List<FarmCatalogue> catalogueList) {

		this.catalogueList = catalogueList;
	}

	public List<FarmCatalogue> getCatalogueList1() {

		return catalogueList1;
	}

	public void setCatalogueList1(List<FarmCatalogue> catalogueList1) {

		this.catalogueList1 = catalogueList1;
	}

	public List<FarmCatalogue> getCatalogueList2() {

		return catalogueList2;
	}

	public void setCatalogueList2(List<FarmCatalogue> catalogueList2) {

		this.catalogueList2 = catalogueList2;
	}

	public List<FarmCatalogue> getCatalogueList3() {

		return catalogueList3;
	}

	public void setCatalogueList3(List<FarmCatalogue> catalogueList3) {

		this.catalogueList3 = catalogueList3;
	}

	public List<FarmCatalogue> getCatalogueList4() {

		return catalogueList4;
	}

	public void setCatalogueList4(List<FarmCatalogue> catalogueList4) {

		this.catalogueList4 = catalogueList4;
	}

	public void setHarvest(List<CropHarvest> harvest) {

		this.harvest = harvest;
	}

	public List<CropHarvest> getHarvest() {

		return harvest;
	}

	public void setHarvestSupply(List<CropSupply> harvestSupply) {

		this.harvestSupply = harvestSupply;
	}

	public List<CropSupply> getHarvestSupply() {

		return harvestSupply;
	}

	public Set<CropSupplyDetails> getCropSupplyDetails() {

		return cropSupplyDetails;
	}

	public void setCropSupplyDetails(Set<CropSupplyDetails> cropSupplyDetails) {

		this.cropSupplyDetails = cropSupplyDetails;
	}

	public String getRegYearString() {

		return regYearString;
	}

	public void setRegYearString(String regYearString) {

		this.regYearString = regYearString;
	}

	public List<FarmICS> getFarmICSs() {

		return farmICSs;
	}

	public void setFarmICSs(List<FarmICS> farmICSs) {

		this.farmICSs = farmICSs;
	}

	public String getTabIndexFarmerZ() {

		return tabIndexFarmerZ;
	}

	public void setTabIndexFarmerZ(String tabIndexFarmerZ) {

		this.tabIndexFarmerZ = tabIndexFarmerZ;
	}

	/*
	 * public String getChemicalString() { return chemicalString; } public void
	 * setChemicalString(String chemicalString) { this.chemicalString =
	 * chemicalString; }
	 */

	public String getEnableMultiProduct() {

		return preferncesService.findPrefernceByName(ESESystem.ENABLE_MULTI_PRODUCT);
	}

	public String getCropTypeFilter() {

		StringBuffer sb = new StringBuffer();
		sb.append("-1:").append(FILTER_ALL).append(";");
		String[] values = getText("cropFilterTypes").split("\\,");
		int counter = 0;
		for (String val : values) {
			String concat = (counter++) + ":" + val + ";";
			sb.append(concat);
		}

		// sb.append(Arrays.asList(values).stream());
		/*
		 * List<Object[]> branchMasters = getBranchesInfo(); for (Object[]
		 * objects : branchMasters) {
		 * sb.append(objects[0].toString()).append(":").append(objects[1].
		 * toString()).append(";"); }
		 */
		String data = sb.toString();
		return data.substring(0, data.length() - 1);
	}

	/*
	 * public String getSoilTestingDefaultValue() { return
	 * (ObjectUtil.isEmpty(this.farm) ? farm.SOILTESTED_NO :
	 * this.farm.getSoilTesting()); }
	 */
	public List<CropHarvestDetails> getHarvestDetails() {

		return harvestDetails;
	}

	public void setHarvestDetails(List<CropHarvestDetails> harvestDetails) {

		this.harvestDetails = harvestDetails;
	}

	public String getSoilTypeName() {

		return soilTypeName;
	}

	public void setSoilTypeName(String soilTypeName) {

		this.soilTypeName = soilTypeName;
	}

	public String getSoilTexName() {

		return soilTexName;
	}

	public void setSoilTexName(String soilTexName) {

		this.soilTexName = soilTexName;
	}

	public String getFarmerUniqueId() {

		return farmerUniqueId;
	}

	public void setFarmerUniqueId(String farmerUniqueId) {

		this.farmerUniqueId = farmerUniqueId;
	}

	public List<FarmCatalogue> getMachinaryList() {

		return machinaryList;
	}

	public void setMachinaryList(List<FarmCatalogue> machinaryList) {

		this.machinaryList = machinaryList;
	}

	public List<FarmCatalogue> getPolyList() {

		return polyList;
	}

	public void setPolyList(List<FarmCatalogue> polyList) {

		this.polyList = polyList;
	}

	public Map<String, String> getIsSoilTested() {

		return isSoilTested;
	}

	public void setIsSoilTested(Map<String, String> isSoilTested) {

		this.isSoilTested = isSoilTested;
	}

	public String getFarmId() {

		return farmId;
	}

	public void setFarmId(String farmId) {

		this.farmId = farmId;
	}

	public String getFarmerPlantsJsonString() {

		return farmerPlantsJsonString;
	}

	public void setFarmerPlantsJsonString(String farmerPlantsJsonString) {

		this.farmerPlantsJsonString = farmerPlantsJsonString;
	}

	public Map<String, String> getBenefitFarmer() {

		Map<String, String> list = new LinkedHashMap<>();

		list = getFarmCatalougeMap(Integer.valueOf(getText("nbenefitFarmer")));
		return list;
	}

	public Map<String, String> getSchemeList() {

		Map<String, String> list = new LinkedHashMap<>();

		list = getFarmCatalougeMap(Integer.valueOf(getText("schemaBased")));
		return list;

	}

	public String getBenefitaryString() {

		return benefitaryString;
	}

	public void setBenefitaryString(String benefitaryString) {

		this.benefitaryString = benefitaryString;
	}

	public void sanghamDetail() throws Exception {

		String result = "NO";
		if (!StringUtil.isEmpty(selectedFarmerId)) {
			Farmer farmer = farmerService.findFarmerByFarmerId(selectedFarmerId);
			// if (farmer.getCertificationType() !=
			// Farmer.CERTIFICATION_TYPE_NONE)
			if (farmer.getSamithi().getId() == '1')
				result = "1";
		}
		response.setContentType("text/html");
		response.getWriter().print(result);
	}

	public Map<String, String> getBenefitLandless() {

		Map<String, String> list = new LinkedHashMap<>();

		list = getFarmCatalougeMap(Integer.valueOf(getText("landLessBenefit")));
		return list;

	}

	public String getBenefitary() {

		return benefitary;
	}

	public void setBenefitary(String benefitary) {

		this.benefitary = benefitary;
	}

	public String getBenefitaryCode() {

		return benefitaryCode;
	}

	public void setBenefitaryCode(String benefitaryCode) {

		this.benefitaryCode = benefitaryCode;
	}

	public String getSchemeTxt() {

		return schemeTxt;
	}

	public void setSchemeTxt(String schemeTxt) {

		this.schemeTxt = schemeTxt;
	}

	public String getSchemeCode() {

		return schemeCode;
	}

	public void setSchemeCode(String schemeCode) {

		this.schemeCode = schemeCode;
	}

	public String getProdString() {

		return ProdString;
	}

	public void setProdString(String prodString) {

		ProdString = prodString;
	}

	public String getSangham() {

		return sangham;
	}

	public void setSangham(String sangham) {

		this.sangham = sangham;
	}

	public String getProdEditArray() {

		return prodEditArray;
	}

	public void setProdEditArray(String prodEditArray) {

		this.prodEditArray = prodEditArray;
	}

	public Map<String, String> getIfsList() {

		Map<String, String> ifsMap = new LinkedHashMap<>();

		ifsMap = getFarmCatalougeMap(Integer.valueOf(getText("ifsType")));

		ifsMap.put("99", "Others");

		return ifsMap;
	}

	public Map<String, String> getSoilConservationList() {

		Map<String, String> soilConservationMap = new LinkedHashMap<>();

		soilConservationMap = getFarmCatalougeMap(Integer.valueOf(getText("soilConservationType")));

		soilConservationMap.put("99", "Others");
		return soilConservationMap;
	}

	public Map<String, String> getWaterConservationList() {

		Map<String, String> waterConservationMap = new LinkedHashMap<>();

		waterConservationMap = getFarmCatalougeMap(Integer.valueOf(getText("waterConservationTypez")));

		waterConservationMap.put("99", "Others");
		return waterConservationMap;
	}

	public Map<String, String> getServiceCentresList() {

		Map<String, String> serviceCentresMap = new LinkedHashMap<>();

		serviceCentresMap = getFarmCatalougeMap(Integer.valueOf(getText("serviceCentresType")));

		serviceCentresMap.put("99", "Others");

		return serviceCentresMap;
	}

	public Map<String, String> getTrainingProgramList() {

		Map<String, String> trainingProgramMap = new LinkedHashMap<>();

		trainingProgramMap = getFarmCatalougeMap(Integer.valueOf(getText("traningProgramType")));

		trainingProgramMap.put("99", "Others");
		return trainingProgramMap;
	}

	public void setIfsList(Map<Integer, String> ifsList) {

		this.ifsList = ifsList;
	}

	public void setSoilConservationList(Map<Integer, String> soilConservationList) {

		this.soilConservationList = soilConservationList;
	}

	public void setWaterConservationList(Map<Integer, String> waterConservationList) {

		this.waterConservationList = waterConservationList;
	}

	public void setServiceCentresList(Map<Integer, String> serviceCentresList) {

		this.serviceCentresList = serviceCentresList;
	}

	public void setTrainingProgramList(Map<Integer, String> trainingProgramList) {

		this.trainingProgramList = trainingProgramList;
	}

	public String getPlantDetailString() {

		return plantDetailString;
	}

	public void setPlantDetailString(String plantDetailString) {

		this.plantDetailString = plantDetailString;
	}

	public String getGetplantDetailsList() {

		return getplantDetailsList;
	}

	public void setGetplantDetailsList(String getplantDetailsList) {

		this.getplantDetailsList = getplantDetailsList;
	}

	public List<FarmerSoilTesting> getFarmerSoilTestingList() {

		return farmerSoilTestingList;
	}

	public void setFarmerSoilTestingList(List<FarmerSoilTesting> farmerSoilTestingList) {

		this.farmerSoilTestingList = farmerSoilTestingList;
	}

	public String getSoilTestJson() {

		return soilTestJson;
	}

	public void setSoilTestJson(String soilTestJson) {

		this.soilTestJson = soilTestJson;
	}

	public String getFarmerseasonJsonString() {

		return FarmerseasonJsonString;
	}

	public void setFarmerseasonJsonString(String farmerseasonJsonString) {

		FarmerseasonJsonString = farmerseasonJsonString;
	}

	/*
	 * public Set<FarmerLandDetails> getFarmerLandDetails() { return
	 * farmerLandDetails; }
	 */
	public void setFarmerLandDetails(Set<FarmerLandDetails> farmerLandDetails) {

		this.farmerLandDetails = farmerLandDetails;
	}

	private Map<Integer, String> formHousingType(String keyProperty, Map<Integer, String> housingTypes) {

		housingTypes = getPropertyData(keyProperty);
		return housingTypes;
	}

	public List<FarmerLandDetails> getFarmerLandDetailsList() {

		return farmerLandDetailsList;
	}

	public void setFarmerLandDetailsList(List<FarmerLandDetails> farmerLandDetailsList) {

		this.farmerLandDetailsList = farmerLandDetailsList;
	}

	public List<FarmerLandDetails> getUpdatefarmerLandDetailsList() {

		return updatefarmerLandDetailsList;
	}

	public void setUpdatefarmerLandDetailsList(List<FarmerLandDetails> updatefarmerLandDetailsList) {

		this.updatefarmerLandDetailsList = updatefarmerLandDetailsList;
	}

	public long getFarmerLandId() {

		return farmerLandId;
	}

	public void setFarmerLandId(long farmerLandId) {

		this.farmerLandId = farmerLandId;
	}

	public Map<Integer, String> getSoilTestMap() {

		return soilTestMap;
	}

	public void setSoilTestMap(Map<Integer, String> soilTestMap) {

		this.soilTestMap = soilTestMap;
	}

	public Map<Integer, Integer> getYearList() {

		int startYear = 2001;
		for (int i = 1; i <= 100; i++) {
			yearList.put(startYear, startYear);
			startYear++;
		}
		return yearList;
	}

	public String getExpenditureJson() {

		return expenditureJson;
	}

	public void setExpenditureJson(String expenditureJson) {

		this.expenditureJson = expenditureJson;
	}

	public List<Expenditure> getExpenditureList() {

		return expenditureList;
	}

	public void setExpenditureList(List<Expenditure> expenditureList) {

		this.expenditureList = expenditureList;
	}

	public Map<String, String> getLandTopographyList() {

		Map<String, String> list = new LinkedHashMap<>();

		list = getFarmCatalougeMap(Integer.valueOf(getText("landTopography")));
		return list;
	}

	public Map<String, String> getHarvestseasondetail() {

		Map<String, String> seasonMap = new HashMap<>();
		List<Object[]> listTemp = farmerService.listfarmingseasonlist();
		for (Object[] obj : listTemp) {
			seasonMap.put(obj[0].toString(), obj[1].toString());
		}
		return seasonMap;
	}

	public Map<String, String> getHousingShadTypeList() {

		Map<String, String> list = new LinkedHashMap<>();

		list = getFarmCatalougeMap(Integer.valueOf(getText("housingShadType")));
		return list;
	}

	public Map<Integer, String> getSoilList() {

		return soilList;
	}

	public void setSoilList(Map<Integer, String> soilList) {

		this.soilList = soilList;
	}

	public String getEnableSoliTesting() {

		return enableSoliTesting;
	}

	public void setEnableSoliTesting(String enableSoliTesting) {

		this.enableSoliTesting = enableSoliTesting;
	}

	public String getDocumentFileId() {

		return documentFileId;
	}

	public void setDocumentFileId(String documentFileId) {

		this.documentFileId = documentFileId;
	}

	public Map<String, String> getTopologyList() {
		Map<String, String> topoList = getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)?getFarmCatalougeMap(Integer.valueOf(getLocaleProperty("cocoType"))):getFarmCatalougeMap(Integer.valueOf(getLocaleProperty("landTopography")));

		return topoList;
	}

	public String populateDownloadFile() {

		try {
			if (StringUtil.isEmpty(documentFileId))
				return REDIRECT;

			DocumentUpload document = farmerService.findDocumentById(Long.valueOf(documentFileId));
			if (ObjectUtil.isEmpty(document))
				return REDIRECT;
			if (!StringUtil.isEmpty(document.getName())) {
				String[] documentNameInfo = document.getName().split("\\.");
				String extension = documentNameInfo[documentNameInfo.length - 1];

				if ("pdf".equalsIgnoreCase(extension)) {
					response.setContentType("application/pdf");
					response.setHeader("Content-Disposition",
							"attachment;filename=" + document.getName().replace(' ', '_'));
					response.getOutputStream().write(document.getContent());
				} else if ("doc".equalsIgnoreCase(extension)) {
					response.setContentType("application/msword");
					response.setHeader("Content-Disposition",
							"attachment;filename=" + document.getName().replace(' ', '_'));
					response.getOutputStream().write(document.getContent());
				} else if ("xlsx".equalsIgnoreCase(extension)) {
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition",
							"attachment;filename=" + document.getName().replace(' ', '_'));
					response.getOutputStream().write(document.getContent());
				} else if ("docx".equalsIgnoreCase(extension)) {
					response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
					response.setHeader("Content-Disposition",
							"attachment;filename=" + document.getName().replace(' ', '_'));
					response.getOutputStream().write(document.getContent());
				} else if ("xls".equalsIgnoreCase(extension)) {
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition",
							"attachment;filename=" + document.getName().replace(' ', '_'));
					response.getOutputStream().write(document.getContent());
				} else if ("png".equalsIgnoreCase(extension)) {
					response.setContentType("image/png");
					response.setHeader("Content-Disposition",
							"attachment;filename=" + document.getName().replace(' ', '_'));
					response.getOutputStream().write(document.getContent());
				} else if ("jpg".equalsIgnoreCase(extension)) {
					response.setContentType("image/png");
					response.setHeader("Content-Disposition",
							"attachment;filename=" + document.getName().replace(' ', '_'));
					response.getOutputStream().write(document.getContent());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getCropInfoEnabled() {

		return cropInfoEnabled;
	}

	public void setCropInfoEnabled(String cropInfoEnabled) {

		this.cropInfoEnabled = cropInfoEnabled;
	}

	public String getMethodOfIrrigationDetail() {

		return methodOfIrrigationDetail;
	}

	public void setMethodOfIrrigationDetail(String methodOfIrrigationDetail) {

		this.methodOfIrrigationDetail = methodOfIrrigationDetail;
	}

	public String getSelectedMethodOfIrrigation() {

		return selectedMethodOfIrrigation;
	}

	public void setSelectedMethodOfIrrigation(String selectedMethodOfIrrigation) {

		this.selectedMethodOfIrrigation = selectedMethodOfIrrigation;
	}

	public Map<Integer, String> getFfsList() {

		return ffsList;
	}

	public void setFfsList(Map<Integer, String> ffsList) {

		this.ffsList = ffsList;
	}

	public Map<Integer, String> getMilletList() {

		return milletList;
	}

	public void setMilletList(Map<Integer, String> milletList) {

		this.milletList = milletList;
	}

	public Map<Integer, String> getBorewellList() {

		return borewellList;
	}

	public void setBorewellList(Map<Integer, String> borewellList) {

		this.borewellList = borewellList;
	}

	public String getLandTopologyDetail() {

		return landTopologyDetail;
	}

	public void setLandTopologyDetail(String landTopologyDetail) {

		this.landTopologyDetail = landTopologyDetail;
	}

	public Map<Long, String> getListLocalities() {

		List<Locality> localities = locationService.listLocalitiesByRevisionNo(0L);
		Map<Long, String> localititesMap = new LinkedHashMap<>();
		localititesMap = localities.stream().collect(Collectors.toMap(Locality::getId, Locality::getName));
		return localititesMap;
	}

	@SuppressWarnings("unchecked")
	public void populateCity() throws Exception {

		if (!selectedLocality.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedLocality))) {
			cities = locationService.listMunicipalitiesByLocalityId(Long.parseLong(selectedLocality));
		}
		JSONArray cityArray = new JSONArray();
		if (!ObjectUtil.isEmpty(cities)) {
			for (Municipality municipality : cities) {
				cityArray.add(getJSONObject(municipality.getId(), municipality.getName()));
			}
		}
		sendAjaxResponse(cityArray);
	}

	/**
	 * Populate village.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings("unchecked")
	public void populateVillage() throws Exception {

		if (!selectedPanchayat.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedPanchayat))
				&& !selectedPanchayat.equalsIgnoreCase("0")) {
			if (getGramPanchayatEnable().equalsIgnoreCase("1")) {
				villages = locationService.listVillagesByPanchayatId(Long.valueOf(selectedPanchayat));
			} else {
				villages = locationService.listVillageByCityId(Long.valueOf(selectedPanchayat));
			}
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
	public void populatePanchayath() throws Exception {

		if (!selectedCity.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedCity))) {
			panchayat = locationService.listGramPanchayatsByCityId(Long.valueOf(selectedCity));
		}
		JSONArray panchayathArr = new JSONArray();
		if (!ObjectUtil.isListEmpty(panchayat)) {
			for (GramPanchayat gramPanchayath : panchayat) {
				panchayathArr.add(getJSONObject(gramPanchayath.getId(), gramPanchayath.getName()));
			}
		}
		sendAjaxResponse(panchayathArr);
	}

	public String getSelectedLocality() {

		return selectedLocality;
	}

	public void setSelectedLocality(String selectedLocality) {

		this.selectedLocality = selectedLocality;
	}

	public String getSelectedCity() {

		return selectedCity;
	}

	public void setSelectedCity(String selectedCity) {

		this.selectedCity = selectedCity;
	}

	public String getSelectedPanchayat() {

		return selectedPanchayat;
	}

	public void setSelectedPanchayat(String selectedPanchayat) {

		this.selectedPanchayat = selectedPanchayat;
	}

	public String getSelectedVillage() {

		return selectedVillage;
	}

	public void setSelectedVillage(String selectedVillage) {

		this.selectedVillage = selectedVillage;
	}

	public String getGramPanchayatEnable() {

		return gramPanchayatEnable;
	}

	public void setGramPanchayatEnable(String gramPanchayatEnable) {

		this.gramPanchayatEnable = gramPanchayatEnable;
	}

	public List<Village> getVillages() {
		if (!StringUtil.isEmpty(selectedCity)) {
			villages = locationService.listVillagesByCityId(Long.valueOf(selectedCity));
		}
		return villages;
	}

	public void setVillages(List<Village> villages) {
		this.villages = villages;
	}

	public List<Municipality> getCities() {

		if (!StringUtil.isEmpty(selectedLocality)) {
			cities = locationService.listMunicipalitiesByLocalityId(Long.parseLong(selectedLocality));
		}
		return cities;
	}

	public void setCities(List<Municipality> cities) {

		this.cities = cities;
	}

	public List<GramPanchayat> getPanchayat() {

		if (!StringUtil.isEmpty(selectedCity)) {
			panchayat = locationService.listGramPanchayatsByCityId(Long.valueOf(selectedCity));
		}
		return panchayat;
	}

	public void setPanchayat(List<GramPanchayat> panchayat) {

		this.panchayat = panchayat;
	}

	public Map<Long, String> getSamithi() {

		samithi = locationService.listSamithiesBasedOnType();
		Map<Long, String> samithiMap = new LinkedHashMap<>();
		samithiMap = samithi.stream().collect(Collectors.toMap(Warehouse::getId, Warehouse::getName));
		return samithiMap;
	}

	public Map<String, String> getFpo() {

		Map<String, String> fpoMap = new LinkedHashMap<String, String>();
		List<FarmCatalogue> fpoList = farmerService.listCatelogueType(getText("fpoType"));
		fpoList.stream().collect(Collectors.toMap((FarmCatalogue::getCode), FarmCatalogue::getName)).entrySet().stream()
				.sorted(Map.Entry.comparingByValue(new Comparator<String>() {
					@Override
					public int compare(String o1, String o2) {

						return o1.compareToIgnoreCase(o2);
					}
				})).forEachOrdered(e -> fpoMap.put(e.getKey(), e.getValue()));

		return fpoMap;
	}

	public String getSelectedSamithi() {

		return selectedSamithi;
	}

	public void setSelectedSamithi(String selectedSamithi) {

		this.selectedSamithi = selectedSamithi;
	}

	public void setSamithi(List<Warehouse> samithi) {

		this.samithi = samithi;
	}

	public String getSelectedFpo() {

		return selectedFpo;
	}

	public void setSelectedFpo(String selectedFpo) {

		this.selectedFpo = selectedFpo;
	}

	public HousingInfo getHousingInfo() {
		return housingInfo;
	}

	public void setHousingInfo(HousingInfo housingInfo) {
		this.housingInfo = housingInfo;
	}

	public String getFormatInspectionDate() {
		return formatInspectionDate;
	}

	public void setFormatInspectionDate(String formatInspectionDate) {
		this.formatInspectionDate = formatInspectionDate;
	}

	public String getTestDate() {

		ESESystem preferences = preferncesService.findPrefernceById("1");
		String dateFormat = "";
		if (preferences != null && preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT) != null) {
			dateFormat = preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT);

		} else {
			dateFormat = DateUtil.DATE_FORMAT;

		}
		return dateFormat;

	}

	public void setTestDate(String testDate) {
		this.testDate = testDate;
	}

	@SuppressWarnings("unchecked")
	public void populateHideFn() throws ScriptException {
		List<FarmField> farmerFieldList = farmerService.listRemoveFarmFields();
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();

		/*
		 * farmerFieldList.forEach(farmerField -> { JSONObject jsonObject = new
		 * JSONObject(); jsonObject.put("type", farmerField.getType());
		 * jsonObject.put("typeName", farmerField.getTypeName());
		 * jsonObjects.add(jsonObject); });
		 */

		if ((getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID))
				|| (getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID))) {
			if (!StringUtil.isEmpty(getBranchId())
					&& getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_ORGANIC_BRANCH_ID)) {

				farmerFieldList.stream().filter(obj -> obj.getOthers() == 1).forEach(farmerField -> {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("type", farmerField.getType());
					jsonObject.put("typeName", farmerField.getTypeName());
					jsonObjects.add(jsonObject);
				});
			} else {
				farmerFieldList.stream().filter(obj -> obj.getOthers() == 2).forEach(farmerField -> {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("type", farmerField.getType());
					jsonObject.put("typeName", farmerField.getTypeName());
					jsonObjects.add(jsonObject);
				});
			}
		} else {
			farmerFieldList.forEach(farmerField -> {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("type", farmerField.getType());
				jsonObject.put("typeName", farmerField.getTypeName());
				jsonObjects.add(jsonObject);
			});
		}

		printAjaxResponse(jsonObjects, "text/html");
	}

	public String getIcsValue() {
		return icsValue;
	}

	public void setIcsValue(String icsValue) {
		this.icsValue = icsValue;
	}

	public Map<String, String> getListProcurementProduct() {

		Map<String, String> farmCropsMap = new LinkedHashMap<String, String>();
		List<ProcurementProduct> cropsList = productDistributionService.listProcurementProductBasedOnCropCat();
		for (ProcurementProduct farmCrop : cropsList) {
			farmCropsMap.put(farmCrop.getCode(), farmCrop.getName());
		}
		return farmCropsMap;
	}

	public IProductDistributionService getProductDistributionService() {
		return productDistributionService;
	}

	public void setProductDistributionService(IProductDistributionService productDistributionService) {
		this.productDistributionService = productDistributionService;
	}

	public void populateVariety() throws Exception {

		if (!selectedCrop.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedCrop))) {
			listProcurementVariety = productDistributionService.findProcurementVariertyByCropCode(selectedCrop);
		}
		JSONArray varietyArr = new JSONArray();
		if (!ObjectUtil.isEmpty(listProcurementVariety)) {
			for (ProcurementVariety procurementVariety : listProcurementVariety) {
				varietyArr.add(getJSONObject(procurementVariety.getCode(),
						procurementVariety.getCode() + " - " + procurementVariety.getName()));
			}
		}
		sendAjaxResponse(varietyArr);
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

	public String getSelectedVariety() {
		return selectedVariety;
	}

	public void setSelectedVariety(String selectedVariety) {
		this.selectedVariety = selectedVariety;
	}

	public IFarmCropsService getFarmCropsService() {
		return farmCropsService;
	}

	public void setFarmCropsService(IFarmCropsService farmCropsService) {
		this.farmCropsService = farmCropsService;
	}

	public Map<String, String> getListProcurementVarietyMap() {

		Map<String, String> procurementVarietyMap = new LinkedHashMap<String, String>();
		if (!StringUtil.isEmpty(selectedCrop)) {
			listProcurementVariety = productDistributionService.findProcurementVariertyByCropCode(selectedCrop);
		}
		for (ProcurementVariety procurementVariety : listProcurementVariety) {
			procurementVarietyMap.put(procurementVariety.getCode(), procurementVariety.getName());
		}
		return procurementVarietyMap;
	}

	public String getSeasonByCode(String seasonCode) {
		String harvestName = "";
		if (seasonMap.size() <= 0) {
			seasonMap = farmerService.listHarvestSeasons().stream()
					.collect(Collectors.toMap(HarvestSeason::getCode, HarvestSeason::getName));
		}
		harvestName = seasonMap.get(seasonCode);
		return harvestName;
	}

	public String getWaterHarvest() {
		return waterHarvest;
	}

	public void setWaterHarvest(String waterHarvest) {
		this.waterHarvest = waterHarvest;
	}

	public Map<String, String> getWaterHarvestList() {
		List<Object[]> waterHarvests = catalogueService
				.findCatalogueCodeAndNameByType(Integer.parseInt(getText("waterHarvest")));
		waterHarvests.stream().filter(obj -> !ObjectUtil.isEmpty(waterHarvests)).forEach(obj -> {
			waterHarvestList.put(obj[0].toString(), obj[1].toString());
		});
		return waterHarvestList;
	}

	public void setWaterHarvestList(Map<String, String> waterHarvestList) {
		this.waterHarvestList = waterHarvestList;
	}

	public String getWaterHarvests() {
		return waterHarvests;
	}

	public void setWaterHarvests(String waterHarvests) {
		this.waterHarvests = waterHarvests;
	}

	public String getIsIrrigation() {
		return isIrrigation;
	}

	public void setIsIrrigation(String isIrrigation) {
		this.isIrrigation = isIrrigation;
	}

	public Map<Integer, String> getProcessingActList() {
		return processingActList;
	}

	public void setProcessingActList(Map<Integer, String> processingActList) {
		this.processingActList = processingActList;
	}

	public void organicStatusFarm() throws Exception {

		String result = "";
		if (!StringUtil.isEmpty(selectedFarmerId)) {
			Farmer farmer = farmerService.findFarmerByFarmerId(selectedFarmerId);
			if (!StringUtil.isEmpty(id) && id != null) {
				FarmIcsConversion farmIcs = farmerService.findFarmIcsConversionByFarmId(Long.valueOf(id));
				if (!ObjectUtil.isEmpty(farmer)) {
					if (!ObjectUtil.isEmpty(farmer.getIsCertifiedFarmer()) && !ObjectUtil.isEmpty(farmIcs)) {
						if (farmer.getIsCertifiedFarmer() == 1) {
							if (!StringUtil.isEmpty(farmIcs.getOrganicStatus())
									&& !farmIcs.getOrganicStatus().equalsIgnoreCase("3")) {
								result = getLocaleProperty("inconversion");
							} else {
								result = getLocaleProperty("alrdyCertified");
							}

						}
					} else {
						result = getLocaleProperty("conventional");
					}
				}
			}
		}
		response.setContentType("text/html");
		response.getWriter().print(result);
	}

	public String getOrganicStatus() {
		return organicStatus;
	}

	public void setOrganicStatus(String organicStatus) {
		this.organicStatus = organicStatus;
	}

	public double getTotalLandHoldingVal() {
		return totalLandHoldingVal;
	}

	public void setTotalLandHoldingVal(double totalLandHoldingVal) {
		this.totalLandHoldingVal = totalLandHoldingVal;
	}

	public String getScopeName() {
		return scopeName;
	}

	public void setScopeName(String scopeName) {
		this.scopeName = scopeName;
	}

	public ICertificationService getCertificationService() {
		return certificationService;
	}

	public void setCertificationService(ICertificationService certificationService) {
		this.certificationService = certificationService;
	}

	public Map<String, String> getCertList() {
		Map<String, String> scopeMap = new LinkedHashMap<String, String>();
		scopeMap = getFarmCatalougeMap(Integer.valueOf(getText("certificationType")));

		return scopeMap;
	}

	public Map<String, String> getInputSourceList() {
		List<Object[]> waterHarvests = catalogueService.findCatalogueCodeAndNameByType(Integer.parseInt(getText("inputSource")));
			waterHarvests.stream().filter(obj -> !ObjectUtil.isEmpty(waterHarvests)).forEach(obj -> {
	    waterHarvestList.put(obj[0].toString(), obj[1].toString());
		});
		return waterHarvestList;
	}

	public Map<Integer, String> getQualifiedTestMap() {
		return qualifiedTestMap;
	}

	public void setQualifiedTestMap(Map<Integer, String> qualifiedTestMap) {
		this.qualifiedTestMap = qualifiedTestMap;
	}

	public String getSelectedWaterSource() {
		return selectedWaterSource;
	}

	public void setSelectedWaterSource(String selectedWaterSource) {
		this.selectedWaterSource = selectedWaterSource;
	}

	public String getLastDateChemical() {
		return lastDateChemical;
	}

	public void setLastDateChemical(String lastDateChemical) {
		this.lastDateChemical = lastDateChemical;
	}

	public String getSelectedInputSource() {
		return selectedInputSource;
	}

	public void setSelectedInputSource(String selectedInputSource) {
		this.selectedInputSource = selectedInputSource;
	}
	
	public Map<Integer, String> getParallelProductionList() {
		return parallelProductionList;
	}

	public void setParallelProductionList(Map<Integer, String> parallelProductionList) {
		this.parallelProductionList = parallelProductionList;
	}

	public Map<Integer, String> getHiredLabourList() {
		return hiredLabourList;
	}

	public void setHiredLabourList(Map<Integer, String> hiredLabourList) {
		this.hiredLabourList = hiredLabourList;
	}

	public Map<Integer, String> getRiskCategoryList() {
		return riskCategoryList;
	}

	public void setRiskCategoryList(Map<Integer, String> riskCategoryList) {
		this.riskCategoryList = riskCategoryList;
	}

	public Map<Integer, String> getBananaTreesList() {
		return bananaTreesList;
	}

	public void setBananaTreesList(Map<Integer, String> bananaTreesList) {
		this.bananaTreesList = bananaTreesList;
	}

	public Map<Integer, String> getCropTypeList() {
		return cropTypeList;
	}

	public void setCropTypeList(Map<Integer, String> cropTypeList) {
		this.cropTypeList = cropTypeList;
	}

	public String getTreeDetailJsonString() {
		return treeDetailJsonString;
	}

	public void setTreeDetailJsonString(String treeDetailJsonString) {
		this.treeDetailJsonString = treeDetailJsonString;
	}

	public String getPresenceOfBanana() {
		return presenceOfBanana;
	}

	public void setPresenceOfBanana(String presenceOfBanana) {
		this.presenceOfBanana = presenceOfBanana;
	}

	public String getParallelProduction() {
		return parallelProduction;
	}

	public void setParallelProduction(String parallelProduction) {
		this.parallelProduction = parallelProduction;
	}

	public String getHiredLabours() {
		return hiredLabours;
	}

	public void setHiredLabours(String hiredLabours) {
		this.hiredLabours = hiredLabours;
	}

	public String getRiskCategory() {
		return riskCategory;
	}

	public void setRiskCategory(String riskCategory) {
		this.riskCategory = riskCategory;
	}

	public List<TreeDetail> getTreeDetails() {
		return treeDetails;
	}

	public void setTreeDetails(List<TreeDetail> treeDetails) {
		this.treeDetails = treeDetails;
	}

	public String getProcessingActivity() {
		return processingActivity;
	}

	public void setProcessingActivity(String processingActivity) {
		this.processingActivity = processingActivity;
	}
	
	public List<FarmerDynamicFieldsValue> getOrgnaicVarietyList() {
		return orgnaicVarietyList;
	}

	public void setOrgnaicVarietyList(List<FarmerDynamicFieldsValue> orgnaicVarietyList) {
		this.orgnaicVarietyList = orgnaicVarietyList;
	}

	public List<FarmerDynamicFieldsValue> getConventionalVarietyList() {
		return conventionalVarietyList;
	}

	public void setConventionalVarietyList(List<FarmerDynamicFieldsValue> conventionalVarietyList) {
		this.conventionalVarietyList = conventionalVarietyList;
	}

	public String getNoOfWineOnPlot() {
		return noOfWineOnPlot;
	}

	public void setNoOfWineOnPlot(String noOfWineOnPlot) {
		this.noOfWineOnPlot = noOfWineOnPlot;
	}

	private Map<Integer, String> formCertificationLevels(String keyProperty, Map<Integer, String> enrollmentMap) {

		certificationLevels = new LinkedHashMap();
		String values = getText(keyProperty);
		if (!StringUtil.isEmpty(values)) {
			String[] valuesArray = values.split(",");
			int i = -1;
			// Arrays.sort(valuesArray);
			for (String value : valuesArray) {
				certificationLevels.put(i++, value);
			}
		}
		return certificationLevels;
	}

	public String getIsFollowUp() {
		return isFollowUp;
	}

	public void setIsFollowUp(String isFollowUp) {
		this.isFollowUp = isFollowUp;
	}

	public int getActStatuss() {
		return actStatuss;
	}

	public void setActStatuss(int actStatuss) {
		this.actStatuss = actStatuss;
	}

	public int getCerLevel() {
		return cerLevel;
	}

	public void setCerLevel(int cerLevel) {
		this.cerLevel = cerLevel;
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

	public String approve() throws Exception {

		if (id != null && !id.equals("")) {
			farm = farmerService.findFarmById(Long.valueOf(id));
			setCurrentPage(getCurrentPage());
			if (farm == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			} else {
				farm.setStatus(1);
				farm.setRevisionNo(DateUtil.getRevisionNumber());
				farmerService.update(farm);
			}
			//id = String.valueOf(farm.getFarmer().getId());
		}
		

		request.setAttribute(HEADING, getText("farmlist"));
		return "farmCertifiedDetail";
	}
	
	public Map<String, String> getInspectionTypeList() {
	      insTypeList.put("0", this.getLocaleProperty("Group"));
	      insTypeList.put("1", this.getLocaleProperty("Processing"));
	      return insTypeList;
	   }

	public String getInspType() {
		return inspType;
	}

	public void setInspType(String inspType) {
		this.inspType = inspType;
	}

	public String populateImage() {

		try {
			// setImgId(imgId);
			Farm pmtImg = null;
			if (!StringUtil.isEmpty(id) && StringUtil.isLong(id))
				pmtImg = farmerService.findFarmById(Long.valueOf(id));
			byte[] image = null;
			if (!ObjectUtil.isEmpty(pmtImg) && pmtImg.getPhoto() != null) {
				image = pmtImg.getPhoto();
			} 
			response.setContentType("multipart/form-data");
			OutputStream out = response.getOutputStream();
			out.write(image);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	public Map<String, String> getInspTypeList() {
		Map<String, String> scopeMap = new LinkedHashMap<String, String>();
		scopeMap = getFarmCatalougeMap(Integer.valueOf(getLocaleProperty("inspectionType")));

		return scopeMap;
	}
	
	public Map<String, String> getSeasonList() {

		Map<String, String> seasonMap = new LinkedHashMap<String, String>();

		List<Object[]> seasonList = farmerService.listSeasonCodeAndName();

		for (Object[] obj : seasonList) {
			seasonMap.put(String.valueOf(obj[0]), String.valueOf(obj[1]));
		}
		return seasonMap;

	}
	
	public String getCertificationType() {
		return certificationType;
	}

	public void setCertificationType(String certificationType) {
		this.certificationType = certificationType;
	}
}
