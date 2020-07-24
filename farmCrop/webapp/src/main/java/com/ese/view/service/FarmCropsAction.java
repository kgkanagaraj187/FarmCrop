/* * FarmCropsAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.service;

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
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.FarmCropsCoordinates.typesOFCordinates;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

// TODO: Auto-generated Javadoc
/**
 * The Class FarmCropsAction.
 */
public class FarmCropsAction extends SwitchValidatorAction {
	private static final long serialVersionUID = -9191437334851144561L;
	public static final String FARMER_DETAIL = "farmerDetail";
	public static final String FARM_DETAIL = "farmDetail";
	public static final int SELECT = -1;
	public static final int DYNAMIC_SEED_TYPE = 7;
	private String id;
	private FarmCrops farmCrops;
	private IFarmCropsService farmCropsService;
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

	/**
	 * Data.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 * @see com.sourcetrace.esesw.view.SwitchAction#data()
	 */
	@SuppressWarnings({ "unchecked" })
	public String data() throws Exception {

		setFarmerIdToRequestAttribute();
		Map<String, String> searchRecord = getJQGridRequestParam();

		FarmCrops filter = new FarmCrops();
		/*
		 * if ((!StringUtil.isEmpty(searchRecord.get("fcm.code"))) ||
		 * (!StringUtil.isEmpty(searchRecord.get("fcm.name")))) {
		 * 
		 * FarmCropsMaster farmCropsMaster = new FarmCropsMaster(); if
		 * (!StringUtil.isEmpty(searchRecord.get("fcm.code")))
		 * farmCropsMaster.setCode(searchRecord.get("fcm.code").trim()); if
		 * (!StringUtil.isEmpty(searchRecord.get("fcm.name")))
		 * farmCropsMaster.setName(searchRecord.get("fcm.name").trim());
		 * filter.setFarmCropsMaster(farmCropsMaster); }
		 */

		if (!StringUtil.isEmpty(searchRecord.get("pc.name"))) {
			ProcurementProduct procurementProduct = new ProcurementProduct();
			ProcurementVariety procurementVariety = new ProcurementVariety();
			procurementProduct.setName(searchRecord.get("pc.name").trim());
			procurementVariety.setProcurementProduct(procurementProduct);
			filter.setProcurementVariety(procurementVariety);
		}

		if (!StringUtil.isEmpty(searchRecord.get("pv.name"))) {
			ProcurementVariety procurementVariety = new ProcurementVariety();
			procurementVariety.setName(searchRecord.get("pv.name").trim());
			filter.setProcurementVariety(procurementVariety);
		}

		if (!StringUtil.isEmpty(searchRecord.get("seedSource"))) {
			filter.setSeedSource((searchRecord.get("seedSource").trim()));
		}

		if (!StringUtil.isEmpty(searchRecord.get("type"))) {
			filter.setCategoryFilter(searchRecord.get("type"));
		}

		if (!StringUtil.isEmpty(searchRecord.get("productionPerYear"))) {
			filter.setEstimatedYield(Double.valueOf(searchRecord.get("productionPerYear").trim()));
		}
		/*
		 * if (!StringUtil.isEmpty(farmerId)) { Farm tempFarm = new Farm();
		 * Farmer tempFarmer = new Farmer();
		 * tempFarmer.setId(Long.valueOf(farmerId));
		 * tempFarm.setFarmer(tempFarmer);
		 */

		if (!StringUtil.isEmpty(searchRecord.get("farmName"))) {
			Farm tempFarm = new Farm();
			tempFarm.setFarmName(searchRecord.get("farmName").trim());
			filter.setFarm(tempFarm);
		}

		if (!StringUtil.isEmpty(searchRecord.get("f.farmName"))) {
			if (ObjectUtil.isEmpty(filter.getFarm()))
				filter.setFarm(new Farm());
			filter.getFarm().setFarmName(searchRecord.get("f.farmName").trim());
		}

		if (!StringUtil.isEmpty(this.id)) {
			if (!ObjectUtil.isEmpty(filter.getFarm())) {
				filter.getFarm().setId(Long.parseLong(this.id));
			} else {
				Farm farm = new Farm();
				farm.setId(Long.parseLong(this.id));
				farm.setFarmName(null);
				filter.setFarm(farm);
			}
		}

		if (!StringUtil.isEmpty(searchRecord.get("farmfarmcrops.estimatedYeild.tonnes"))) {
			filter.setEstYldPfx(searchRecord.get("farmfarmcrops.estimatedYeild.tonnes").trim());
		}
		
		if (!StringUtil.isEmpty(searchRecord.get("cultiArea"))) {
			filter.setCultiArea(searchRecord.get("cultiArea").trim());
		}
		
		if (!StringUtil.isEmpty(searchRecord.get("farmcrops.cropSeason"))) {
			filter.setSeason(Long.valueOf(searchRecord.get("farmcrops.cropSeason").trim()));
		}
		
		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		return sendJQGridJSONResponse(data);

	}

	/**
	 * To json.
	 * 
	 * @param obj
	 *            the obj
	 * @return the JSON object
	 * @see com.sourcetrace.esesw.view.SwitchAction#toJSON(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		FarmCrops farmCrops = (FarmCrops) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		/*
		 * rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">" +
		 * farmCrops.getFarmCropsMaster() == null ? "" :
		 * farmCrops.getFarmCropsMaster() .getCode() + "</font>");
		 * rows.add(farmCrops.getFarmCropsMaster() == null ? "" :
		 * farmCrops.getFarmCropsMaster() .getName());
		 * rows.add(farmCrops.getFarm() == null ? "" :
		 * farmCrops.getFarm().getFarmName());
		 * rows.add(farmCrops.getCropArea());
		 */
		rows.add(farmCrops.getProcurementVariety().getProcurementProduct() == null ? ""
				: farmCrops.getProcurementVariety().getProcurementProduct().getName());
		rows.add(farmCrops.getProcurementVariety() == null ? "" : farmCrops.getProcurementVariety().getName());
		if(!getCurrentTenantId().equalsIgnoreCase(ESESystem.GRIFFITH_TENANT_ID)){
		rows.add((getLocaleProperty("cs" + farmCrops.getCropCategory())));
		}
		rows.add(farmCrops.getFarm() == null ? "" : farmCrops.getFarm().getFarmName());
		 if(getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)){
		 rows.add(farmCrops.getEstimatedYield() == null ? "" : farmCrops.getEstimatedYield()); 
		 rows.add(farmCrops.getCultiArea() == null ? "" : farmCrops.getCultiArea()); 
		 } 
		 rows.add(ObjectUtil.isEmpty(farmCrops.getCropSeason())? "" : farmCrops.getCropSeason().getName()); 
		 if(!getCurrentTenantId().equalsIgnoreCase(ESESystem.GRIFFITH_TENANT_ID)){
		rows.add(farmCrops.getEstimatedYield());
		 }rows.add("<button class='faDelete' onclick='deleteFarmCrops(" + farmCrops.getId() + ")'> Delete </button>");
		jsonObject.put("id", farmCrops.getId());
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

		setFarmerIdToRequestAttribute();
		if (farmCrops == null) {
			command = "create";
			Calendar currentDate = Calendar.getInstance();
			sowingDate = df.format(currentDate.getTime());
			request.setAttribute(HEADING, getText(CREATE));
			Farmer farmer = farmerService.findFarmerById(Long.valueOf(this.farmerId));
			// Farm farm =
			// farmerService.findFarmById(Long.valueOf(this.farmId));
			if (getFarmerName() == null || getFarmerName().isEmpty()) {
				setFarmerName(farmer.getName());
			}

			/*
			 * if(getFarmName()==null || getFarmName().isEmpty()){
			 * setFarmName(farm.getFarmName()); }
			 */
			return INPUT;
		} else {
			ProcurementProduct procurementProduct = null;

			if (!StringUtil.isEmpty(selectedFarm)) {
				Farm farm = farmerService.findFarmByfarmId(Long.valueOf(selectedFarm));
				farmCrops.setFarm(farm);
			} else {
				addActionError(getText("farmcropempty.farm"));
				return INPUT;
			}
				farmCrops.setCropCategoryList(selectedCropCategoryList);
			riskAssesment = farmCrops.getRiskAssesment();
			farmCrops.setRiskAssesment(riskAssesment != null ? String.valueOf(riskAssesment) : "");
			farmCrops.setRiskBufferZoneDistanse(farmCrops.getRiskBufferZoneDistanse());
			farmCrops.setSeedTreatmentDetails(farmCrops.getSeedTreatmentDetails());
			farmCrops.setOtherSeedTreatmentDetails(farmCrops.getOtherSeedTreatmentDetails());
			if (!StringUtil.isEmpty(selectedCrop)) {
				procurementProduct = productDistributionService.findProcurementProductByCode(selectedCrop);
			}
			if(ObjectUtil.isEmpty(procurementProduct)){
				addActionError(getLocaleProperty("empty.farmCropName"));
				return INPUT;
			}
			ProcurementVariety procurementVariety = null;
			if (!StringUtil.isEmpty(selectedVariety)) {
				procurementVariety = productDistributionService.findProcurementVariertyByCode(selectedVariety);
			}
			if (!StringUtil.isEmpty(procurementVariety))
				farmCrops.setProcurementVariety(procurementVariety);

			HarvestSeason harvestSeason = farmerService.findHarvestSeasonByCode(this.getCropSeasonCode());
				
			if (!StringUtil.isEmpty(harvestSeason)) {
					farmCrops.setCropSeason(harvestSeason);
				} else {
					if(!getCurrentTenantId().equalsIgnoreCase("wilmar")){
						addActionError(getLocaleProperty("empty.season"));
						return INPUT;
					}
					
				}
			
			
			/*
			 * try{ String sLength=farmCrops.getStapleLengthPfx()+"."+farmCrops.
			 * getStapleLengthSfx();
			 * farmCrops.setStapleLength(Double.parseDouble(sLength)); }
			 * catch(NumberFormatException ex){
			 * 
			 * }
			 */
			SimpleDateFormat sf=new SimpleDateFormat("MM/dd/yyyy");
			sowingDate=sowingDate!=null && !StringUtil.isEmpty(sowingDate)?sowingDate:sf.format(new Date());
			farmCrops.setSowingDate(DateUtil.convertStringToDate(sowingDate, "MM/dd/yyyy"));
			
			 if(getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
				 if (!StringUtil.isEmpty(harvestDate)) {
				 farmCrops.setEstimatedHarvestDate(DateUtil.convertStringToDate(harvestDate, "MM/dd/yyyy"));
				 }
			 }
			 else{
			if (sowingDate != null) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(DateUtil.convertStringToDate(sowingDate, "MM/dd/yyyy"));
				if (!StringUtil.isEmpty(procurementVariety.getHarvestDays())) {
					calendar.add(Calendar.DAY_OF_YEAR, Integer.parseInt(procurementVariety.getHarvestDays()));
					Date harDate = calendar.getTime();
					if (harDate != null) {
						harvestDate = df.format(harDate);
						farmCrops.setEstimatedHarvestDate(DateUtil.convertStringToDate(harvestDate, "MM/dd/yyyy"));
					}
				}
			}
			 }
			 if(getCurrentTenantId().equals(ESESystem.LIVELIHOOD_TENANT_ID)){
				 if (!StringUtil.isEmpty(farmCrops.getSeedQtyUsedPfx())) {
						//String seedCost = farmCrops.getSeedQtyUsedPfx() ;
						farmCrops.setSeedQtyUsed(Double.parseDouble(farmCrops.getSeedQtyUsedPfx()));
					}
				 
				 if (!StringUtil.isEmpty(farmCrops.getEstYldPfx())) {
						//String estYld = farmCrops.getEstYldPfx() ;
							farmCrops.setEstimatedYield(Double.parseDouble(farmCrops.getEstYldPfx()));
					}
				 
			 }else{
			if (!StringUtil.isEmpty(farmCrops.getSeedQtyUsedPfx())
					|| !StringUtil.isEmpty(farmCrops.getSeedQtyUsedSfx())) {
				String seedCost = farmCrops.getSeedQtyUsedPfx() + "." + farmCrops.getSeedQtyUsedSfx();
				farmCrops.setSeedQtyUsed(Double.parseDouble(seedCost));
			}
			if (!StringUtil.isEmpty(farmCrops.getSeedQtyCostPfx())
					|| !StringUtil.isEmpty(farmCrops.getSeedQtyCostSfx())) {
				String cost = farmCrops.getSeedQtyCostPfx() + "." + farmCrops.getSeedQtyCostSfx();
				farmCrops.setSeedQtyCost(Double.parseDouble(cost));
			}
			if (!StringUtil.isEmpty(cropInfoEnabled) && cropInfoEnabled.equals("1")) {
				 if(getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
				if (!StringUtil.isEmpty(farmCrops.getEstYldPfx())) {
					String estYld = farmCrops.getEstYldPfx() + "." + farmCrops.getEstYldSfx();
						farmCrops.setEstimatedYield(Double.parseDouble(estYld)*100);
				}
				else {
					addActionError(getText("farmcropempty.farmEstYield"));
					return INPUT;
				}
				 }
				 else{
					 if (!StringUtil.isEmpty(farmCrops.getEstYldPfx()) && !StringUtil.isEmpty(farmCrops.getEstYldSfx())) {
							String estYld = farmCrops.getEstYldPfx() + "." + farmCrops.getEstYldSfx();
							//String estYld = farmCrops.getEstYldPfx() + "." + !StringUtil.isEmpty(farmCrops.getEstYldSfx()) != null?farmCrops.getEstYldSfx():"0.00";
							farmCrops.setEstimatedYield(Double.parseDouble(estYld));
						} 
				 }
			}
		}
			farmCrops.setBranchId(getBranchId());
			if(!getCurrentTenantId().equalsIgnoreCase(ESESystem.GRIFFITH_TENANT_ID) && !getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID) && !getCurrentTenantId().equalsIgnoreCase(ESESystem.SUSAGRI)
					&& !getCurrentTenantId().equalsIgnoreCase(ESESystem.FRUITMASTER_TENANT_ID) && !getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)){
			 if(!StringUtil.isEmpty(farmCrops.getCultiArea())){
		        	if(!ObjectUtil.isEmpty(farmCrops.getFarm())){
		        		if(!StringUtil.isEmpty(farmCrops.getFarm().getFarmDetailedInfo().getTotalLandHolding())){
		        			cultiVal = farmCrops.getCultiArea();
		        			totalLandHoldVal = farmCrops.getFarm().getFarmDetailedInfo().getTotalLandHolding();
		        			if(Double.valueOf(cultiVal) > Double.valueOf(totalLandHoldVal)){
		        				addActionError(getLocaleProperty("cultAreaLess"));
		        				return INPUT;
		        			}
		        			
		        			}
		        		}
			 }
			 }
		
			/*
			 * if
			 * (!StringUtil.isEmpty(farmCrops.getOtherSeedTreatmentDetails())) {
			 * FarmCatalogue farmCatalogue = new FarmCatalogue();
			 * farmCatalogue.setName(farmCrops.getOtherSeedTreatmentDetails());
			 * farmCatalogue.setTypez(DYNAMIC_SEED_TYPE);
			 * farmCatalogue.setCode(idGenerator.getCatalogueIdSeq());
			 * farmCatalogue.setRevisionNo(DateUtil.getRevisionNumber());
			 * farmCatalogue.setBranchId(getBranchId());
			 * catalogueService.addCatalogue(farmCatalogue); farmCatalogue =
			 * catalogueService.findCatalogueById(farmCatalogue.getId());
			 * farmCrops.setFarmcatalogue(farmCatalogue);
			 * farmCrops.setSeedTreatmentDetails(farmCatalogue.getCode()); }
			 */
			if(getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)){
				FarmCrops farmCrops1 = farmCropsService.findByFarmIdandVarietyIdAndSeason(farmCrops.getFarm().getId(),
						farmCrops.getProcurementVariety().getId(), farmCrops.getCropCategory(),harvestSeason.getId());
				
				if (ObjectUtil.isEmpty(farmCrops1)) {
					farmCrops.setRevisionNo(DateUtil.getRevisionNumber());
					farmCropsService.addFarmCrops(farmCrops);
					saveDynamicField("357",String.valueOf(farmCrops.getId()),farmCrops.getCropSeason().getCode(),String.valueOf(DynamicFeildMenuConfig.EntityTypes.FARM_CROPS.ordinal()));
					/*
					 * 
					 * farmCrops.setFarm(farmerService.findFarmByfarmId(Long.
					 * parseLong(this.getFarmId())));
					 */
					// farmCrops.getFarm().setFarmer(farmerService.findFarmerByFarmerId(this.getFarmerId()));
					/*farmerService.updateFarmerRevisionNo(farmCrops.getFarm().getFarmer().getId(),
							DateUtil.getRevisionNumber());*/
					// return FARMER_DETAIL;
				} else {
					addActionError(getText("cropExists"));
					return INPUT;
				}
			}
			else{
			FarmCrops farmCrops2 = farmCropsService.findByFarmIdandVarietyId(farmCrops.getFarm().getId(),
					farmCrops.getProcurementVariety().getId(), farmCrops.getCropCategory());
			if (ObjectUtil.isEmpty(farmCrops2)) {
				farmCrops.setRevisionNo(DateUtil.getRevisionNumber());
				farmCropsService.addFarmCrops(farmCrops);
				saveDynamicField("357",String.valueOf(farmCrops.getId()),farmCrops.getCropSeason().getCode(),String.valueOf(DynamicFeildMenuConfig.EntityTypes.FARM_CROPS.ordinal()));
				/*
				 * 
				 * farmCrops.setFarm(farmerService.findFarmByfarmId(Long.
				 * parseLong(this.getFarmId())));
				 */
				// farmCrops.getFarm().setFarmer(farmerService.findFarmerByFarmerId(this.getFarmerId()));
				/*farmerService.updateFarmerRevisionNo(farmCrops.getFarm().getFarmer().getId(),
						DateUtil.getRevisionNumber());*/
				// return FARMER_DETAIL;
			} else {
				addActionError(getText("cropExists"));
				return INPUT;
			}
			}
	

			// return REDIRECT;

			return FARM_DETAIL;
		}
	}

	/**
	 * Detail.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String detail() throws Exception {

		String view = LIST;
		request.setAttribute(HEADING, getText(LIST));
		if (id != null && !id.equals("")) {
			farmCrops = farmCropsService.findFarmCropsById(Long.parseLong(id));
			if (farmCrops == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			setFarmId(String.valueOf(farmCrops.getFarm().getId()));
			setFarmerId(String.valueOf(farmCrops.getFarm().getFarmer().getId()));

			ESESystem preferences = preferncesService.findPrefernceById("1");
			setCropInfoEnabled(preferences.getPreferences().get(ESESystem.ENABLE_CROP_INFO));
			if (!ObjectUtil.isEmpty(preferences)) {
				DateFormat genDate = new SimpleDateFormat(
						preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
				
				if (!ObjectUtil.isEmpty(farmCrops.getSowingDate())) {
					if(getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)){
						SimpleDateFormat sf=new SimpleDateFormat("MM-dd-yyyy");
						sowingDate = sf.format(farmCrops.getSowingDate());	
					}else{
					sowingDate = genDate.format(farmCrops.getSowingDate());
					}
					 if(getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
						 if (!StringUtil.isEmpty(farmCrops.getEstimatedHarvestDate())) {
						 harvestDate= genDate.format(farmCrops.getEstimatedHarvestDate());
						 }
					 }
					 else{
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(farmCrops.getSowingDate());
					if (!StringUtil.isEmpty(farmCrops.getProcurementVariety().getHarvestDays())) {
						calendar.add(Calendar.DAY_OF_YEAR,
								Integer.parseInt(farmCrops.getProcurementVariety().getHarvestDays()));
						Date harDate = calendar.getTime();
						if (harDate != null) {
							if(getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)){
								SimpleDateFormat sf=new SimpleDateFormat("MM-dd-yyyy");
								harvestDate = sf.format(harDate);
							}else{
							harvestDate = genDate.format(harDate);
							}
						}
					}
				}
				 }
			}

			// if(getCurrentTenantId().equalsIgnoreCase("chetna")){
			FarmCatalogue typ  =getCatlogueValueByCode(farmCrops.getType());
			if (!ObjectUtil.isEmpty(typ) && typ != null) {
				if (!StringUtil.isEmpty(typ.getCode())&& typ.getCode()!=null && typ.getCode().equals("99")) {
					farmCrops.setType(typ.getName() + ": "
							+ (!ObjectUtil.isEmpty(farmCrops.getOtherType()) ? farmCrops.getOtherType() : ""));
				} else {
					farmCrops.setType(typ.getName());
				}
			} else {
				farmCrops.setType("");
			}
			if(!StringUtil.isEmpty(farmCrops.getSeedSource())){
				FarmCatalogue seedSource  =getCatlogueValueByCode(farmCrops.getSeedSource());
				if (!ObjectUtil.isEmpty(seedSource) && seedSource != null) {
					
						farmCrops.setSeedSource(!StringUtil.isEmpty(seedSource.getName()) ? seedSource.getName() :"NA");
					
				}
			}
			
			// }
			/*
			 * else{ if(!farmCrops.getType().equals("-1")){
			 * farmCrops.setType(farmCrops.getType()); } else{
			 * farmCrops.setType("0"); } }
			 */

			/*if (!StringUtil.isEmpty(farmCrops.getCropCategoryList())
					&& !farmCrops.getCropCategoryList().equals("undefined")) {
				int i = 0;
				for (String categories : getText("cropCategories").split(",")) {
					cropCategories.put(++i, categories);
				}

				farmCrops.setCropCategoryList(cropCategories.get(Integer.valueOf(farmCrops.getCropCategoryList())));
			} else {
				farmCrops.setCropCategoryList("");
			}*/

			if (!StringUtil.isEmpty(farmCrops.getCropCategoryList()) && farmCrops.getCropCategoryList() != "-1") {
				if (farmCrops.getCropCategoryList() != null && farmCrops.getCropCategoryList().length() > 1) {
					FarmCatalogue catalogue = getCatlogueValueByCode(String.valueOf(farmCrops.getCropCategoryList()));
					if (!ObjectUtil.isEmpty(catalogue)) {
						String name = catalogue != null ? catalogue.getName() : "";
						farmCrops.setCropCategoryList(name);
					} else {
						farmCrops.setCropCategoryList("");
					}
				}

			}
			
			if (!StringUtil.isEmpty(farmCrops.getOtherSeedTreatmentDetails())) {
				farmCrops.setOtherSeedTreatmentDetails(farmCrops.getOtherSeedTreatmentDetails());
			}

			/*
			 * Map<Integer, String> ceo =
			 * getPropertyData(getText("seedTreatmentDetails")); if
			 * (!ObjectUtil.isEmpty(farmCrops.getSeedTreatmentDetails()) &&
			 * !farmCrops.getSeedTreatmentDetails().equalsIgnoreCase("99")) {
			 * String farmCatalogueId = farmCrops.getSeedTreatmentDetails(); if
			 * (!StringUtil.isEmpty(farmCatalogueId) &
			 * !farmCatalogueId.equalsIgnoreCase("OTH")) { FarmCatalogue
			 * farmCatalogue =
			 * farmerService.findfarmcatalogueById(farmCatalogueId); farmCrops
			 * .setSeedTreatmentDetails(!ObjectUtil.isEmpty(farmCatalogue) ?
			 * farmCatalogue.getName() : ""); } else{
			 * farmCrops.setSeedTreatmentDetails("");
			 * 
			 * }
			 * 
			 * 
			 * }
			 */
			if (farmCrops.getActiveCoordinates()!=null && farmCrops.getActiveCoordinates().getFarmCropsCoordinates() != null
					&& !ObjectUtil.isListEmpty(farmCrops.getActiveCoordinates().getFarmCropsCoordinates())) {
				jsonObjectList = getFarmCropsJSONObjects(farmCrops.getActiveCoordinates().getFarmCropsCoordinates());
				if (getCurrentTenantId().equalsIgnoreCase("livelihood")&&farmCrops.getFarm().getActiveCoordinates()!=null && farmCrops.getFarm().getActiveCoordinates().getFarmCoordinates() != null
						&& !ObjectUtil.isListEmpty(farmCrops.getFarm().getActiveCoordinates().getFarmCoordinates())) {
					jsonFarmObjectList = getFarmJSONObjects(farmCrops.getFarm().getActiveCoordinates().getFarmCoordinates());
				}else {
					jsonFarmObjectList = new ArrayList();
				}
			} else {
				jsonObjectList = new ArrayList();
			}
			farmCrops.setCultiArea(!StringUtil.isEmpty(farmCrops.getCultiArea())? CurrencyUtil.getDecimalFormat(Double.valueOf(farmCrops.getCultiArea()), "##.00"):"0.0");
			command = UPDATE;
			view = DETAIL;
			request.setAttribute(HEADING, getText(DETAIL));
		}
		return view;
	}

	@SuppressWarnings("unchecked")
	private List<JSONObject> getFarmCropsJSONObjects(Set<FarmCropsCoordinates> coordinates) {
		JSONArray neighbouringDetails = new JSONArray();
		List<JSONObject> returnObjects = new ArrayList<JSONObject>();
		List<FarmCropsCoordinates> listCoordinates = new ArrayList<FarmCropsCoordinates>(coordinates);
		 Collections.sort(listCoordinates);
		
		if (!ObjectUtil.isListEmpty(listCoordinates)) {
			for (FarmCropsCoordinates coordinateObj : listCoordinates) {
				JSONObject jsonObject = new JSONObject();

				// jsonObject.put("orderNo",!ObjectUtil.isEmpty(coordinateObj.getOrderNo())
				// ? coordinateObj.getOrderNo():"");
				if(coordinateObj.getType() == typesOFCordinates.mainCoordinates.ordinal()){
					jsonObject.put("lat",
							!ObjectUtil.isEmpty(coordinateObj.getLatitude()) ? coordinateObj.getLatitude() : "");
					jsonObject.put("lon",
							!ObjectUtil.isEmpty(coordinateObj.getLongitude()) ? coordinateObj.getLongitude() : "");
					returnObjects.add(jsonObject);
				}else if(coordinateObj.getType() == typesOFCordinates.neighbouringDetails_farmcrops.ordinal()){
					jsonObject.put("type",coordinateObj.getType());
					jsonObject.put("title",coordinateObj.getTitle());
					jsonObject.put("description",coordinateObj.getDescription());
					jsonObject.put("lat",!ObjectUtil.isEmpty(coordinateObj.getLatitude()) ? coordinateObj.getLatitude() : "");
					jsonObject.put("lng",!ObjectUtil.isEmpty(coordinateObj.getLongitude()) ? coordinateObj.getLongitude() : "");
					neighbouringDetails.add(jsonObject);
				}
				
			}
		}
		JSONObject neighbouring_jsonObject = new JSONObject();
		neighbouring_jsonObject.put("neighbouringDetails",neighbouringDetails);
		returnObjects.add(neighbouring_jsonObject);
		return returnObjects;
	}
	
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
	 * Update.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String update() throws Exception {

		setFarmerIdToRequestAttribute();

		if (id != null && !id.equals("")) {
			farmCrops = farmCropsService.findFarmCropsById(Long.parseLong(id));
			if (farmCrops == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			setCurrentPage(getCurrentPage());
		
			ESESystem preferences = preferncesService.findPrefernceById("1");
			setCropInfoEnabled(preferences.getPreferences().get(ESESystem.ENABLE_CROP_INFO));
			if (getFarmerName() == null || getFarmerName().isEmpty()) {
				setFarmerName(farmCrops.getFarm().getFarmer().getName());
			}
			setFarmId(String.valueOf(farmCrops.getFarm().getId()));
			setFarmerId(String.valueOf(farmCrops.getFarm().getFarmer().getId()));

			if (getFarmName() == null || getFarmName().isEmpty()) {
				setFarmName(farmCrops.getFarm().getFarmName());
			}
			farmerfarmName=farmCrops.getFarm().getFarmName();
			/*
			 * if(!(farmCrops.getStapleLength()==null) &&
			 * farmCrops.getStapleLength()>0){ String
			 * sLengthValue=String.valueOf(farmCrops.getStapleLength());
			 * sLengthValue=sLengthValue.replace(".", ","); String
			 * []length=sLengthValue.split(",");
			 * farmCrops.setStapleLengthPfx(length[0]);
			 * farmCrops.setStapleLengthSfx(length[1]);
			 * 
			 * }
			 */

			if (!StringUtil.isEmpty(farmCrops.getCropCategoryList())) {
				setSelectedCropCategoryList(farmCrops.getCropCategoryList());
			}

			/*
			 * if (!StringUtil.isEmpty(farmCrops.getCropCategoryList())) {
			 * String cropCategories = getText("cropCategories"); String[]
			 * cropCategory = cropCategories.split(",");
			 * farmCrops.setCropCategoryList((cropCategory[Integer.valueOf(
			 * farmCrops.getCropCategoryList())])); } else {
			 * farmCrops.setCropCategoryList(""); }
			 */

			/*
			 * if(!StringUtil.isEmpty(farmCrops.getCropCategoryList())){
			 * farmCrops.setCropCategoryList(selectedCropCategoryList); }
			 */
			if (!StringUtil.isEmpty(farmCrops.getRiskAssesment() == null)) {
				farmCrops.setRiskAssesment(farmCrops.getRiskAssesment());
			}

			if (!StringUtil.isEmpty(farmCrops.getSeedQtyCost())) {
				if (farmCrops.getSeedQtyCost() > 0) {
					String costValue = String.valueOf(farmCrops.getSeedQtyCost());
					costValue = costValue.replace(".", ",");
					String[] costPrice = costValue.split(",");
					farmCrops.setSeedQtyCostPfx(costPrice[0]);
					farmCrops.setSeedQtyCostSfx(costPrice[1]);
				}
			}
			if (!StringUtil.isEmpty(farmCrops.getEstimatedYield())) {
				 if(getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
					 if (farmCrops.getEstimatedYield() > 0) {
							String estYlds = String.valueOf(farmCrops.getEstimatedYield()/100);
							estYlds = estYlds.replace(".", ",");
							String[] estYeilding = estYlds.split(",");
							farmCrops.setEstYldPfx(estYeilding[0]);
							farmCrops.setEstYldSfx(estYeilding[1]);
						}
						 }
						 else{
							 if (farmCrops.getEstimatedYield() > 0) {
									String estYlds = String.valueOf(farmCrops.getEstimatedYield());
									estYlds = estYlds.replace(".", ",");
									String[] estYeilding = estYlds.split(",");
									farmCrops.setEstYldPfx(estYeilding[0]);
									farmCrops.setEstYldSfx(estYeilding[1]);
								}
						 }
				
			}
			if(!getCurrentTenantId().equals(ESESystem.GRIFFITH_TENANT_ID)){
			if (!StringUtil.isEmpty(farmCrops.getSeedQtyUsed())) {
				if (farmCrops.getSeedQtyUsed() != null && farmCrops.getSeedQtyUsed() > 0) {
					String costValue = String.valueOf(farmCrops.getSeedQtyUsed());
					costValue = costValue.replace(".", ",");
					String[] costPrice = costValue.split(",");
					farmCrops.setSeedQtyUsedPfx(costPrice[0]);
					farmCrops.setSeedQtyUsedSfx(costPrice[1]);
				}
			}
			}
			if (!ObjectUtil.isEmpty(farmCrops.getFarmcatalogue())) {
				//farmCatalogue = catalogueService.findCatalogueById(farmCrops.getFarmcatalogue().getId());
				setSelectedSeedTreatment(farmCrops.getFarmcatalogue().getName());
			}

			if (!StringUtil.isEmpty(farmCrops.getCropCategory())) {
				farmCrops.setCropCategory(farmCrops.getCropCategory());
			}
			
			// selectedFarmCropsMaster =
			// farmCrops.getFarmCropsMaster().getCode();
			selectedFarm = farmCrops.getFarm().getFarmCode();
			selectedCrop = farmCrops.getProcurementVariety().getProcurementProduct().getCode();
			selectedVariety = farmCrops.getProcurementVariety().getCode();
				if (!ObjectUtil.isEmpty(farmCrops.getCropSeason())) {
					this.setCropSeasonCode(farmCrops.getCropSeason().getCode());
				}
			if (farmCrops.getSowingDate() != null) {
				sowingDate = df.format(farmCrops.getSowingDate());
			}
			 if(getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
				 if (farmCrops.getEstimatedHarvestDate() != null) {
					harvestDate = df.format(farmCrops.getEstimatedHarvestDate());
					} 
			 }
			
			FarmCatalogue typ  =getCatlogueValueByCode(farmCrops.getType());
			if (!ObjectUtil.isEmpty(typ) && typ != null) {			
					farmCrops.setType(typ.getCode());
				
			} else {
				farmCrops.setType("");
			}
			id = null;
			command = UPDATE;
			request.setAttribute(HEADING, getText(UPDATE));
		} else {
			if (farmCrops != null) {
				FarmCrops existing = farmCropsService.findFarmCropsById(farmCrops.getId()); // existing
				if (getFarmerName() == null || getFarmerName().isEmpty()) {
					setFarmerName(existing.getFarm().getFarmer().getName());
				}

				if (getFarmName() == null || getFarmName().isEmpty()) {
					setFarmName(existing.getFarm().getFarmName());
				}
				// object
				setCurrentPage(getCurrentPage());
				if (existing == null) {
					addActionError(NO_RECORD);
					return REDIRECT;
				}

				ProcurementVariety procurementVariety = null;
				if (!StringUtil.isEmpty(selectedVariety)) {
					procurementVariety = productDistributionService.findProcurementVariertyByCode(selectedVariety);
				}
				if (!StringUtil.isEmpty(procurementVariety))
					existing.setProcurementVariety(procurementVariety);

				existing.setRiskBufferZoneDistanse(farmCrops.getRiskBufferZoneDistanse());
				existing.setSeedTreatmentDetails(farmCrops.getSeedTreatmentDetails());
				existing.setOtherSeedTreatmentDetails(farmCrops.getOtherSeedTreatmentDetails());
				existing.setStapleLength(farmCrops.getStapleLength());
				existing.setSeedSource(farmCrops.getSeedSource());
				existing.setCropSeason(farmCrops.getCropSeason());
				existing.setCropCategory(farmCrops.getCropCategory());
				existing.setCropCategoryList(selectedCropCategoryList);
				existing.setCultiArea(farmCrops.getCultiArea());
				existing.setOtherType(farmCrops.getOtherType());

				if (farmCrops.getCropCategory() == 0) {
					existing.setLintCotton(farmCrops.getLintCotton());
					existing.setActualSeedYield(farmCrops.getActualSeedYield());
					/*
					 * if (StringUtil.isEmpty(farmCrops.getLintCotton())) {
					 * addActionError(getText("empty.lintCotton")); return
					 * INPUT; } else {
					 * existing.setLintCotton(farmCrops.getLintCotton()); }
					 * 
					 * if (StringUtil.isEmpty(farmCrops.getActualSeedYield())) {
					 * addActionError(getText("empty.actualSeedYield")); return
					 * INPUT; } else {
					 * existing.setActualSeedYield(farmCrops.getActualSeedYield(
					 * )); }
					 */
					if (getEnableMultiProduct().equals("0")) {
						if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)) {
						if (StringUtil.isEmpty(farmCrops.getStapleLength())) {
							addActionError(getText("empty.stapleLength"));
							return INPUT;
						} }else {
							existing.setStapleLength(farmCrops.getStapleLength());
						}
					}
				  
				}
				if(!getCurrentTenantId().equalsIgnoreCase(ESESystem.GRIFFITH_TENANT_ID) && !getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID) && !getCurrentTenantId().equalsIgnoreCase(ESESystem.SUSAGRI)
						&& !getCurrentTenantId().equalsIgnoreCase(ESESystem.FRUITMASTER_TENANT_ID) && !getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)){
				 if(!StringUtil.isEmpty(existing.getCultiArea())){
			        	if(!ObjectUtil.isEmpty(existing.getFarm())){
			        		if(!StringUtil.isEmpty(existing.getFarm().getFarmDetailedInfo().getTotalLandHolding())){
			        			cultiVal = existing.getCultiArea();
			        			totalLandHoldVal = existing.getFarm().getFarmDetailedInfo().getTotalLandHolding();
			        			if(Double.valueOf(cultiVal) > Double.valueOf(totalLandHoldVal)){
			        				addActionError(getLocaleProperty("cultAreaLess"));
			        				return INPUT;
			        			}
			        			
			        			}
			        		}
				 }
				 }
				existing.setSeedCotton(farmCrops.getSeedCotton());
				existing.setLintCotton(farmCrops.getLintCotton());
				existing.setActualSeedYield(farmCrops.getActualSeedYield());
				if (!StringUtil.isEmpty(farmCrops.getEstYldPfx())) {
					if(StringUtil.isEmpty(farmCrops.getEstYldSfx())){
						farmCrops.setEstYldSfx("0");
					}
					String estYld = farmCrops.getEstYldPfx() + "." + farmCrops.getEstYldSfx();
					//String estYld = farmCrops.getEstYldPfx() + "." + !StringUtil.isEmpty(farmCrops.getEstYldSfx()) != null?farmCrops.getEstYldSfx():"0.00";
					farmCrops.setEstimatedYield(Double.parseDouble(estYld));
				}
				existing.setEstimatedYield(farmCrops.getEstimatedYield());
				existing.setEstimatedCotton(farmCrops.getEstimatedCotton());
				existing.setInterType(farmCrops.getInterType());
				existing.setInterAcre(farmCrops.getInterAcre());
				existing.setTotalCropHarv(farmCrops.getTotalCropHarv());
				existing.setGrossIncome(farmCrops.getGrossIncome());

				// existing.setProductionPerYear(farmCrops.getProductionPerYear());

				/*
				 * FarmCropsMaster farmCropsMaster = null; Farm farm = null; if
				 * (!StringUtil.isEmpty(farmCrops.getFarmCropsMaster().getCode()
				 * )) { farmCropsMaster =
				 * farmCropsService.findFarmCropsMasterByCode(farmCrops
				 * .getFarmCropsMaster().getCode()); } if
				 * (!StringUtil.isEmpty(farmCrops.getFarm().getFarmCode())) {
				 * farm =
				 * farmerService.findFarmByCode(farmCrops.getFarm().getFarmCode(
				 * )); } existing.setFarm(farm);
				 * //existing.setFarmCropsMaster(farmCropsMaster);
				 * 
				 * 
				 * existing.setCropArea(farmCrops.getCropArea());
				 * existing.setProductionPerYear(farmCrops.getProductionPerYear(
				 * ));
				 * 
				 * if (!ObjectUtil.isEmpty(farmCrops.getFarm()) &&
				 * !ObjectUtil.isEmpty(farmCrops.getFarm().getFarmer())) { if
				 * (farmCrops.getFarm().getFarmer().getCertificationType() == 0)
				 * { farmCrops.setCropSeason(-1); farmCrops.setCropCategory(-1);
				 * } }
				 * 
				 * existing.setCropSeason(farmCrops.getCropSeason());
				 * existing.setCropCategory(farmCrops.getCropCategory());
				 * 
				 * FarmCrops farmCropsExisting = farmCropsService
				 * .findFarmCropbyProcurementVarietyIdFarmIdAndFarmerId(existing
				 * .getProcurementVariety().getId(), existing.getFarm().getId(),
				 * existing .getFarm().getFarmer().getId()); if
				 * (!ObjectUtil.isEmpty(farmCropsExisting)) { if
				 * (farmCropsExisting.getId() != existing.getId()) {
				 * addActionError(getText("unique.farm"));
				 * request.setAttribute(HEADING, getText(CREATE)); return INPUT;
				 * } }
				 */
				/*
				 * if
				 * (!StringUtil.isEmpty(farmCrops.getOtherSeedTreatmentDetails()
				 * )) { farmCatalogue =
				 * catalogueService.findCatalogueByName(farmCrops.
				 * getOtherSeedTreatmentDetails()); if
				 * (ObjectUtil.isEmpty(farmCatalogue)) { farmCatalogue = new
				 * FarmCatalogue();
				 * farmCatalogue.setName(farmCrops.getOtherSeedTreatmentDetails(
				 * )); farmCatalogue.setTypez(DYNAMIC_SEED_TYPE);
				 * farmCatalogue.setCode(idGenerator.getCatalogueIdSeq());
				 * farmCatalogue.setRevisionNo(DateUtil.getRevisionNumber());
				 * farmCatalogue.setBranchId(getBranchId());
				 * catalogueService.addCatalogue(farmCatalogue); farmCatalogue =
				 * catalogueService.findCatalogueById(farmCatalogue.getId());
				 * farmCrops.setFarmcatalogue(farmCatalogue);
				 * farmCrops.setSeedTreatmentDetails(farmCatalogue.getCode()); }
				 * else{ farmCatalogue.setCode(farmCatalogue.getCode());
				 * farmCatalogue.setName(farmCrops.
				 * getOtherSeedTreatmentDetails());
				 * farmCatalogue.setTypez(DYNAMIC_SEED_TYPE);
				 * farmCatalogue.setRevisionNo(DateUtil. getRevisionNumber());
				 * farmCatalogue.setBranchId(getBranchId());
				 * catalogueService.addCatalogue(farmCatalogue); }
				 * 
				 * 
				 * farmCatalogue =
				 * catalogueService.findCatalogueById(farmCatalogue.getId()) ;
				 * farmCrops.setFarmcatalogue(farmCatalogue);
				 * 
				 * 
				 * }
				 */
				if (!StringUtil.isEmpty(farmCrops.getSeedTreatmentDetails())) {
					existing.setSeedTreatmentDetails(farmCrops.getSeedTreatmentDetails());
				}

				if (!StringUtil.isEmpty(farmCrops.getOtherSeedTreatmentDetails())) {
					existing.setOtherSeedTreatmentDetails(farmCrops.getOtherSeedTreatmentDetails());
				}

				ProcurementProduct procurementProduct = null;
				if (!StringUtil.isEmpty(selectedCrop)) {
					procurementProduct = productDistributionService.findProcurementProductByCode(selectedCrop);
				}

				if (!StringUtil.isEmpty(sowingDate)) {
					existing.setSowingDate(DateUtil.convertStringToDate(sowingDate, "MM/dd/yyyy"));
				}
				 if(getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
					 if (!StringUtil.isEmpty(harvestDate)) {
					 existing.setEstimatedHarvestDate(DateUtil.convertStringToDate(harvestDate, "MM/dd/yyyy"));
					 }
				 }
				 else{

				if (!StringUtil.isEmpty(sowingDate)) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(DateUtil.convertStringToDate(sowingDate, "MM/dd/yyyy"));
					if (!StringUtil.isEmpty(procurementVariety.getHarvestDays())) {
						calendar.add(Calendar.DAY_OF_YEAR, Integer.parseInt(procurementVariety.getHarvestDays()));
						Date harDate = calendar.getTime();
						if (harDate != null) {
							harvestDate = df.format(harDate);
							existing.setEstimatedHarvestDate(DateUtil.convertStringToDate(harvestDate, "MM/dd/yyyy"));
						}
					}
				}
				 }
				HarvestSeason harvestSeason = farmerService.findHarvestSeasonByCode(this.getCropSeasonCode());
				existing.setCropSeason(harvestSeason);
				existing.setCultiArea(farmCrops.getCultiArea());
				existing.setType(farmCrops.getType());
				if(!getCurrentTenantId().equals(ESESystem.GRIFFITH_TENANT_ID)){
					if(StringUtil.isEmpty(farmCrops.getSeedQtyUsedSfx())){
						farmCrops.setSeedQtyUsedSfx("0");
					}
						String seedCost = farmCrops.getSeedQtyUsedPfx() + "." + farmCrops.getSeedQtyUsedSfx();
					existing.setSeedQtyUsed(Double.parseDouble(StringUtil.isDouble(seedCost) ? seedCost : "0"));
					}else{
						existing.setSeedQtyCost(farmCrops.getSeedQtyCost());
						existing.setSeedQtyUsed(farmCrops.getSeedQtyUsed());
					}
				existing.setStapleLength(farmCrops.getStapleLength());
				existing.setRiskAssesment(farmCrops.getRiskAssesment());
				existing.setRiskBufferZoneDistanse(farmCrops.getRiskBufferZoneDistanse());
				existing.setSeedTreatmentDetails(farmCrops.getSeedTreatmentDetails());
				existing.setOtherSeedTreatmentDetails(farmCrops.getOtherSeedTreatmentDetails());
				if (!StringUtil.isEmpty(farmCrops.getSeedQtyCostPfx())
						&& !StringUtil.isEmpty(farmCrops.getSeedQtyCostSfx())) {
					String cost = farmCrops.getSeedQtyCostPfx() + "." + farmCrops.getSeedQtyCostSfx();
					existing.setSeedQtyCost(Double.parseDouble(cost));
				}else{
					
					String cost = farmCrops.getSeedQtyCostPfx() + "." + farmCrops.getSeedQtyCostSfx();
					existing.setSeedQtyCost(Double.parseDouble(StringUtil.isDouble(cost) ? cost : "0"));
					/*existing.setSeedQtyCost(Double.parseDouble(cost));*/
				}
				/*if (!StringUtil.isEmpty(farmCrops.getEstYldPfx()) && !StringUtil.isEmpty(farmCrops.getEstYldSfx())) {
					String estYld = farmCrops.getEstYldPfx() + "." + farmCrops.getEstYldSfx();
					if (cropInfoEnabled.equals("1")) {
						existing.setEstimatedYield(Double.parseDouble(estYld));
					}
				}*/
				 if(getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
						if (!StringUtil.isEmpty(farmCrops.getEstYldPfx())) {
							String estYld = farmCrops.getEstYldPfx() + "." + farmCrops.getEstYldSfx();
							existing.setEstimatedYield(Double.parseDouble(estYld)*100);
						}
						 else {
								addActionError(getText("farmcropempty.farmEstYield"));
								return INPUT;
							}
						 }
						 else{
							 if (!StringUtil.isEmpty(farmCrops.getEstYldPfx()) && !StringUtil.isEmpty(farmCrops.getEstYldSfx())) {
									String estYld = farmCrops.getEstYldPfx() + "." + farmCrops.getEstYldSfx();
									existing.setEstimatedYield(Double.parseDouble(estYld));
								} 
						 }
				existing.setType(farmCrops.getType());
				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID) || getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)) {
					existing.setNoOfTrees(farmCrops.getNoOfTrees());
					existing.setProdTrees(farmCrops.getProdTrees());
					existing.setAffTrees(farmCrops.getAffTrees());
				}
				existing.setRevisionNo(DateUtil.getRevisionNumber());
				farmCropsService.editFarmCrops(existing);
				
					updateDynamicFields("357",String.valueOf(existing.getId()),existing.getCropSeason().getCode(),String.valueOf(DynamicFeildMenuConfig.EntityTypes.FARM_CROPS.ordinal()));
				
					
				farmCrops.setFarm(farmerService.findFarmByfarmId(Long.parseLong(this.getFarmId())));
				/*farmerService.updateFarmerRevisionNo(farmCrops.getFarm().getFarmer().getId(),
						DateUtil.getRevisionNumber());*/
				request.setAttribute(HEADING, getText(LIST));
				// return FARMER_DETAIL;
				// return REDIRECT;
				return FARM_DETAIL;
			}
		}
		return INPUT;
	}

	/**
	 * Delete.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */

	public String delete() throws Exception {

		setFarmerIdToRequestAttribute();
		if (this.getId() != null && !(this.getId().equals(EMPTY))) {
			farmCrops = farmCropsService.findFarmCropsById(Long.valueOf(id));
			if (farmCrops == null) {
				addActionError(NO_RECORD);
				return list();
			}

			if (farmCrops.getSowingDate() != null) {
				sowingDate = df.format(farmCrops.getSowingDate());
			}

			List<CropSupply> cs = productService.listCropSupplyByFarmCode(farmCrops.getFarm().getFarmCode());
			List<CropHarvest> ch = productService.listCropHarvestByFarmCode(farmCrops.getFarm().getFarmCode());
			if (cs.size() > 0) {
				addActionError(getText("cannotDeleteFarmCropWithCropSale"));
				request.setAttribute(HEADING, getText(DETAIL));
				return DETAIL;
			}

			if (ch.size() > 0) {
				addActionError(getText("cannotDeleteFarmCropWithCropHarvest"));
				request.setAttribute(HEADING, getText(DETAIL));
				return DETAIL;
			}

			setCurrentPage(getCurrentPage());
			// farmCropsService.removeFarmCrops(farmCrops);
			farmCropsService.removeFarmCropsById(farmCrops.getId());
		}
		request.setAttribute(HEADING, getText(LIST));
		// return FARMER_DETAIL;
		// return REDIRECT;
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
	@Override
	public Object getData() {

		cropSeasons = productDistributionService.listHarvestSeason();
		if(!getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID) && !getCurrentTenantId().equalsIgnoreCase(ESESystem.GRIFFITH_TENANT_ID)){
		cropCategories.put(0, getText("cs0"));
		cropCategories.put(1, getText("cs1"));
		}
		//if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.KPF_TENANT_ID) && !getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)) {
		
	    if(!getIsKpfBased().equals("1") && !getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)){
		cropCategories.put(2, getText("cs2"));
		}
	    if(getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
	    	cropCategories.put(3, getLocaleProperty("cs3"));
	    	cropCategories.put(4, getLocaleProperty("cs4"));
	    	cropCategories.put(5, getLocaleProperty("cs5"));
	    }
	     if(getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID) ||  getCurrentTenantId().equalsIgnoreCase(ESESystem.GRIFFITH_TENANT_ID)){
	    	cropCategories.put(0, getLocaleProperty("livehoodCrop0"));
	    	cropCategories.put(1, getLocaleProperty("livehoodCrop1"));
	    }
		riskAssType.put(1, getText("rs1"));
		riskAssType.put(2, getText("rs2"));

		ESESystem preferences = preferncesService.findPrefernceById("1");
		setCropInfoEnabled("0");
		if (!StringUtil.isEmpty(preferences)) {
			setLat(preferences.getPreferences().get(ESESystem.DEFAULT_LATITUDE));  
			setLon(preferences.getPreferences().get(ESESystem.DEFAULT_LONGTITUDE));
			if (preferences.getPreferences().get(ESESystem.ENABLE_CROP_INFO) != null) {
				setCropInfoEnabled(preferences.getPreferences().get(ESESystem.ENABLE_CROP_INFO));
			}

		}

		seedTreatmentList = farmerService.listSeedTreatmentDetailsBasedOnType();

		// seedTreatmentDetailsMap = formTreatmentList("seedTreatmentDetails",
		// seedTreatmentDetailsMap);

		
		
		if (!ObjectUtil.isEmpty(farmCrops)) {
			if (!StringUtil.isEmpty(selectedCrop)) {
				if (selectedCrop.contains(",")) {
					Arrays.asList(selectedCrop.split(",")).stream().filter(str -> !StringUtil.isEmpty(str))
							.forEach(obj -> {
								selectedCrop = obj.trim();
							});
				}
			}
			if (!StringUtil.isEmpty(selectedVariety)) {
				ProcurementVariety procurementVariety = new ProcurementVariety();
				procurementVariety.setCode(selectedVariety);
				farmCrops.setProcurementVariety(procurementVariety);
			}
		}
		
		if (!StringUtil.isEmpty(dynamicFieldsArray) && dynamicFieldsArray.length() > 0) {
			//farmer.setValidateDynamicFields(dynamicFieldsArray);
		}
		
		if (!StringUtil.isEmpty(sowingDate)) {
		farmCrops.setSowingDate(DateUtil.convertStringToDate(sowingDate, "MM/dd/yyyy"));}
		return farmCrops;
	}

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
			procurementVarietyMap.put(procurementVariety.getCode(), !StringUtil.isEmpty(procurementVariety.getCode()) ?  procurementVariety.getName() : getLocaleProperty(procurementVariety.getCode()));
			 
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
	
	
	
}
