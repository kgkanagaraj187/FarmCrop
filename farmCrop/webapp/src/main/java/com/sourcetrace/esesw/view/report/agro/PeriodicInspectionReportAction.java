/*
`tabIndex * ProcurementReportAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view.report.agro;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionContext;
import com.sourcetrace.eses.BreadCrumb;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.AgentType;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.order.entity.profile.InspectionImage;
import com.sourcetrace.eses.order.entity.profile.InspectionImageInfo;
import com.sourcetrace.eses.order.entity.txn.PeriodicInspection;
import com.sourcetrace.eses.order.entity.txn.PeriodicInspectionData;
import com.sourcetrace.eses.order.entity.txn.PeriodicInspectionSymptom;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.ICertificationService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.util.Base64Util;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ExportUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmIcsConversion;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

// TODO: Auto-generated Javadoc
/**
 * The Class PeriodicInspectionReportAction.
 */
@SuppressWarnings("unchecked")
public class PeriodicInspectionReportAction extends BaseReportAction implements IExporter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3225383113359903354L;
	private static final Logger LOGGER = Logger.getLogger(PeriodicInspectionReportAction.class);
	private static final SimpleDateFormat fieldStaffDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
	private Map<Integer, String> icsStatusList = new LinkedHashMap<Integer, String>();
	private IAgentService agentService;
	private ILocationService locationService;
	private IDeviceService deviceService;
	private PeriodicInspection filter;
	private String coordinateValues = null;
	private ICertificationService certificationService;
	private IFarmerService farmerService;
	private PeriodicInspection periodicInspection;
	private String inspectionType;
	private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
	private SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	private String daterange;

	private String tabIndex = "#tabs-1";
	private String tabIndexNeed = "#tabs-2";
	private String REDIRECT = "redirect";
	private String DETAILNEED = "needDetail";
	private String DETAIL = "detail";
	private String UPDATE = "update";
	private String NO_RECORD = "No_Records_Present";
	private String command;

	private List<String> months = new ArrayList<String>();
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	public static final String NA = "NA";
	private String farmerId;
	private String farmId;
	private String branchIdParma;
	private String createdUserName;
	private String mobileUserName;
	private SortedSet<InspectionImage> inspectionImagesSet = new TreeSet<InspectionImage>();
	private Map<String, String> farmerFartherNameList = new LinkedHashMap<String, String>();
	private String farmerFatherId;
	private IClientService clientService;
	@Autowired
	private IPreferencesService preferncesService;
	private String enableMultiProduct;
	private String farmIrrigationDetail;
	private InputStream fileInputStream;
	private String xlsFileName;
	private String samithi;
	private String village;
	private Map<Integer, String> farmIrrigationList = new LinkedHashMap<Integer, String>();
	private String latitude;
	private String longitude;
	private String icsName;
	private String uom;

	public IClientService getClientService() {

		return clientService;
	}

	public void setClientService(IClientService clientService) {

		this.clientService = clientService;
	}

	private String seasonCode;
	@Autowired
	private ICatalogueService catalogueService;
	private List<PeriodicInspectionData> pestRecomList;
	private List<PeriodicInspectionData> fungiList;
	private List<PeriodicInspectionData> manureList;
	private List<PeriodicInspectionData> fertilizerList;
	private List<PeriodicInspectionData> diseaseList;
	private String pestNameValue;
	private String diseaseNameValue;
	private String fungisideValue;
	private String manureValue;
	private String fertilizerValue;
	private String selectedPestName;
	private String selectedPestSymptom;
	private String selectedDiseaseName;
	private String selectedDiseaseSymptom;
	private String selectedActivities;
	private String selectedInterPloughing;
	private String gapPlanting;
	private String selectedOtherPest;
	private String selectedOtherDisease;
	private String pestJson;
	private String diseaseJson;
	private String fertiJson;
	private String manureJson;
	private String farmerCodeName;
	private String farmIdName;
	private String farmerIdName;
	private String inspectionOfDate;
	private String mobileId;
	private String selActiviesValue;
	private File inspectImg1;
	private File inspectImg2;
	private File inspectImg3;
	private InspectionImage photo;
	private InspectionImageInfo inspectionImageInfo;
	List<InspectionImage> periodicInspectionImageList = new ArrayList<>();
	private String cocDone;

	public List<InspectionImage> getPeriodicInspectionImageList() {

		return periodicInspectionImageList;
	}

	public void setPeriodicInspectionImageList(List<InspectionImage> periodicInspectionImageList) {

		this.periodicInspectionImageList = periodicInspectionImageList;
	}

	private String selectedPestVal;
	private String selectedDiseaseVal;
	List<FarmCatalogue> pestSym = new ArrayList<FarmCatalogue>();
	List<FarmCatalogue> diseaseSym = new ArrayList<FarmCatalogue>();
	private String type;
	private File inspectionAudio;
	private String audioByteString;
	private String otherValue;
	private String selectedGrowth;
	private String isSelectedCrop;
	private String isPestNoticed;
	private String isPestETL;
	private String isPestSolved;
	private String isDiseaseETL;
	private String isDiseaseNoticed;
	private String isDiseaseSolved;
	private String inspectDate;
	private String chemicalNameString;
	private Map<String, String> fields = new LinkedHashMap<>();

	/**
	 * @see com.sourcetrace.esesw.view.BaseReportAction#list()
	 */
	public String list() {
		Calendar currentDate = Calendar.getInstance();
		Calendar cal = (Calendar) currentDate.clone();
		cal.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) - 1);
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		super.startDate = df.format(cal.getTime());
		super.endDate = df.format(currentDate.getTime());
		type = request.getParameter("type");
		daterange = super.startDate + " - " + super.endDate;

		request.setAttribute(HEADING, getText(LIST));
		setFilter(new PeriodicInspection());//periodicInspection
		return LIST;
	}

	/**
	 * Detail.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	/**
	 * @return
	 * @throws Exception
	 */
	public String data() throws Exception {

		PeriodicInspection periodicInspection = new PeriodicInspection();
		this.filter = periodicInspection;
		if (!StringUtil.isEmpty(inspectionType) && ObjectUtil.isEmpty(filter.getInspectionType())) {
			filter.setInspectionType(inspectionType);
		}
		if (!StringUtil.isEmpty(branchIdParma)) {
			filter.setBranchId(branchIdParma);
		}

		if (!StringUtil.isEmpty(branchIdParma)) {
			if (!getIsMultiBranch().equalsIgnoreCase("1")) {
				List<String> branchList = new ArrayList<>();
				branchList.add(branchIdParma);
				filter.setBranchesList(branchList);
			} else {
				List<String> branchList = new ArrayList<>();
				List<BranchMaster> branches = clientService.listChildBranchIds(branchIdParma);
				branchList.add(branchIdParma);
				branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
					branchList.add(branch.getBranchId());
				});
				filter.setBranchesList(branchList);
			}
		}

		if (!StringUtil.isEmpty(subBranchIdParma) && !subBranchIdParma.equals("0")) {
			filter.setBranchId(subBranchIdParma);
		}

		if (!StringUtil.isEmpty(seasonCode)) {

			filter.setSeasonCode(seasonCode);
		} /*
			 * else {
			 * filter.setSeasonCode(clientService.findCurrentSeasonCode()); }
			 */

		if (!StringUtil.isEmpty(farmerId)) {
			Farmer farmer = new Farmer();
			farmer.setFarmerId(farmerId);
			Farm farm = new Farm();
			farm.setFarmer(farmer);
			filter.setFarm(farm);
		}
		if (!StringUtil.isEmpty(farmerFatherId)) {
			Farmer farmer = new Farmer();
			farmer.setLastName(farmerFatherId);
			Farm farm = new Farm();
			farm.setFarmer(farmer);
			filter.setFarm(farm);
		}
		if (!StringUtil.isEmpty(farmId)) {

			filter.setFarmId(farmId);
		} /*
			 * /* if (!StringUtil.isEmpty(inspectionType) &&
			 * ObjectUtil.isEmpty(filter.getInspectionType())) {
			 * filter.setInspectionType(inspectionType); } if
			 * (!StringUtil.isEmpty(branchIdParma)) {
			 * filter.setBranchId(branchIdParma); }
			 * 
			 * if (!StringUtil.isEmpty(seasonCode)) {
			 * 
			 * filter.setSeasonCode(seasonCode); } else {
			 * filter.setSeasonCode(clientService.findCurrentSeasonCode()); }
			 */
		if (!StringUtil.isEmpty(createdUserName)) {
			filter.setCreatedUserName(createdUserName);
		}
		if (!StringUtil.isEmpty(icsName)) {
			filter.setIcsName(icsName);

		}
		filter.setIsDeleted("0");
		super.filter = this.filter;
		return sendJSONResponse(readData());
	}

	public String detail() {

		String view = "";
		ESESystem preferences = preferncesService.findPrefernceById("1");
		DateFormat genDate = new SimpleDateFormat(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));

		setEnableMultiProduct(preferncesService.findPrefernceByName(ESESystem.ENABLE_MULTI_PRODUCT));
		if (id != null && !id.equals("")) {
			periodicInspection = agentService.findPeriodicInspectionById(Long.valueOf(id));
			if (periodicInspection == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			periodicInspection.buildMultipleRecords();
			if (!ObjectUtil.isEmpty(preferences) && (!ObjectUtil.isEmpty(periodicInspection.getInspectionDate()))) {
				inspectDate = genDate.format(periodicInspection.getInspectionDate());
			}
			if (!StringUtil.isEmpty(periodicInspection.getCreatedUserName())) {
				String mobileUser = agentService.findAgentNameByAgentId(periodicInspection.getCreatedUserName());
				if (!StringUtil.isEmpty(mobileUser)) {
					setMobileUserName(mobileUser);
				}
			}
			if (getCurrentTenantId().equalsIgnoreCase("lalteer")) {
				if (!StringUtil.isEmpty(periodicInspection.getSurvivalPercentage())) {
					FarmCatalogue cat = catalogueService
							.findCatalogueByCode(periodicInspection.getSurvivalPercentage());
					if (!ObjectUtil.isEmpty(cat)) {
						periodicInspection.setSurvivalPercentage(cat.getName());
					}
				}
			}
			if (!StringUtil.isEmpty(periodicInspection.getSurvivalPercentage())) {
				FarmCatalogue cat = catalogueService.findCatalogueByCode(periodicInspection.getSurvivalPercentage());
				if (!ObjectUtil.isEmpty(cat)) {
					periodicInspection.setSurvivalPercentage(cat.getName());
				}
			}
			if (!StringUtil.isEmpty(periodicInspection.getCurrentStatusOfGrowth())) {
				FarmCatalogue cat = getCatlogueValueByCode(periodicInspection.getCurrentStatusOfGrowth());
				if (!ObjectUtil.isEmpty(cat)) {
					periodicInspection.setCurrentStatusOfGrowth(cat.getName());
				}
			}
			if (!StringUtil.isEmpty(periodicInspection.getChemicalName())) {

				periodicInspection.setChemicalName(periodicInspection.getChemicalName());

			}
			if (!StringUtil.isEmpty(periodicInspection.getCropCode())) {

				String crop = farmerService.findCropNameByCropCode(periodicInspection.getCropCode());
				periodicInspection.setCropCode(!StringUtil.isEmpty(crop) ? crop : "");

			}
			if (!StringUtil.isEmpty(periodicInspection.getCropProtectionPractice())) {
				FarmCatalogue cat = catalogueService.findCatalogueByCode(periodicInspection.getCropProtectionPractice());
				if (!ObjectUtil.isEmpty(cat)) {
					periodicInspection.setCropProtectionPractice(cat.getName());
				}
			}
			Map<String, List<PeriodicInspectionData>> datMap = new HashMap<>();
			if (!ObjectUtil.isEmpty(periodicInspection.getPeriodicInspectionData())) {
				datMap = periodicInspection.getPeriodicInspectionData().stream()
						.collect(Collectors.groupingBy(PeriodicInspectionData::getType));

				if (!ObjectUtil.isEmpty(datMap)) {
					for (Entry<String, List<PeriodicInspectionData>> data : datMap.entrySet()) {
						// Pest Name
						if (!StringUtil.isEmpty(data.getKey()) && data.getKey().equalsIgnoreCase("PEST")) {
							boolean isTrue = true;
							selectedPestName = "";
							List<PeriodicInspectionData> inspectionData = data.getValue();
							for (PeriodicInspectionData periodicInsData : inspectionData) {
								if (!getCurrentTenantId().equalsIgnoreCase("efk")) {
									FarmCatalogue cat = getCatlogueValueByCode(periodicInsData.getCatalogueValue());
									if (!ObjectUtil.isEmpty(cat)) {
										String pestNameValue = cat.getName();
										if (!isTrue) {

											if ("99".equalsIgnoreCase(periodicInsData.getCatalogueValue())
													&& !StringUtil.isEmpty(periodicInsData.getOtherCatalogueValueName())) {
												setSelectedPestName(selectedPestName + "," + pestNameValue + "::"
														+ periodicInsData.getOtherCatalogueValueName());

											} else {
												setSelectedPestName(selectedPestName + "," + pestNameValue);
											}
										} else {
											if ("99".equalsIgnoreCase(periodicInsData.getCatalogueValue())
													&& !StringUtil.isEmpty(periodicInsData.getOtherCatalogueValueName())) {
												setSelectedPestName(pestNameValue + "::"
														+ periodicInsData.getOtherCatalogueValueName());

											} else {
												setSelectedPestName(pestNameValue);
											}
											isTrue = false;
										}
									} else {
										setSelectedPestName("");
									}
								}else{
									
										String pestNameValue = periodicInsData.getCatalogueValue();
										if (!isTrue) {

											if ("99".equalsIgnoreCase(periodicInsData.getCatalogueValue())
													&& !StringUtil.isEmpty(periodicInsData.getOtherCatalogueValueName())) {
												setSelectedPestName(selectedPestName + "," + pestNameValue + "::"
														+ periodicInsData.getOtherCatalogueValueName());

											} else {
												setSelectedPestName(selectedPestName + "," + pestNameValue);
											}
										} else {
											if ("99".equalsIgnoreCase(periodicInsData.getCatalogueValue())
													&& !StringUtil.isEmpty(periodicInsData.getOtherCatalogueValueName())) {
												setSelectedPestName(pestNameValue + "::"
														+ periodicInsData.getOtherCatalogueValueName());

											} else {
												setSelectedPestName(pestNameValue);
											}
											isTrue = false;
										}
									
								}
								
								

							}

						}

						// Disease Name
						if (!StringUtil.isEmpty(data.getKey()) && data.getKey().equalsIgnoreCase("DISEASE")) {
							boolean isTrue = true;
							selectedDiseaseName = "";
							List<PeriodicInspectionData> inspectionData = data.getValue();
							for (PeriodicInspectionData periodicInsData : inspectionData) {
								
								if (!getCurrentTenantId().equalsIgnoreCase("efk")) {
									FarmCatalogue cat = getCatlogueValueByCode(periodicInsData.getCatalogueValue());
									if (!ObjectUtil.isEmpty(cat)) {
										String diseaseValue = cat.getName();
										if (!isTrue) {
											if ("99".equalsIgnoreCase(periodicInsData.getCatalogueValue())
													&& !StringUtil.isEmpty(periodicInsData.getOtherCatalogueValueName())) {
												setSelectedDiseaseName(selectedDiseaseName + "," + diseaseValue + "::"
														+ periodicInsData.getOtherCatalogueValueName());

											} else {
												setSelectedDiseaseName(selectedDiseaseName + "," + diseaseValue);
											}
										} else {
											if ("99".equalsIgnoreCase(periodicInsData.getCatalogueValue())
													&& !StringUtil.isEmpty(periodicInsData.getOtherCatalogueValueName())) {
												setSelectedDiseaseName(
														diseaseValue + "::" + periodicInsData.getOtherCatalogueValueName());

											} else {
												setSelectedDiseaseName(diseaseValue);
											}
											isTrue = false;
										}
									} else {
										setSelectedDiseaseName("");
									}
								}else{
									
										String diseaseValue = periodicInsData.getCatalogueValue();
										if (!isTrue) {
											if ("99".equalsIgnoreCase(periodicInsData.getCatalogueValue())
													&& !StringUtil.isEmpty(periodicInsData.getOtherCatalogueValueName())) {
												setSelectedDiseaseName(selectedDiseaseName + "," + diseaseValue + "::"
														+ periodicInsData.getOtherCatalogueValueName());

											} else {
												setSelectedDiseaseName(selectedDiseaseName + "," + diseaseValue);
											}
										} else {
											if ("99".equalsIgnoreCase(periodicInsData.getCatalogueValue())
													&& !StringUtil.isEmpty(periodicInsData.getOtherCatalogueValueName())) {
												setSelectedDiseaseName(
														diseaseValue + "::" + periodicInsData.getOtherCatalogueValueName());

											} else {
												setSelectedDiseaseName(diseaseValue);
											}
											isTrue = false;
										}
									 
								}
								

							}
						}

						// ActivitiesCarriedOut
						if (!StringUtil.isEmpty(data.getKey()) && data.getKey().equalsIgnoreCase("ACAPV")) {
							boolean isTrue = true;
							List<PeriodicInspectionData> inspectionData = data.getValue();
							for (PeriodicInspectionData periodicInsData : inspectionData) {
								if (!isTrue) {
									setSelectedActivities(selectedActivities + ","
											+ getText("ACAPV" + periodicInsData.getCatalogueValue()));
									setSelActiviesValue(selActiviesValue + "," + periodicInsData.getCatalogueValue());
								} else {
									setSelectedActivities(getText("ACAPV" + periodicInsData.getCatalogueValue()));
									setSelActiviesValue(periodicInsData.getCatalogueValue());
									isTrue = false;
								}

							}
						}
						// Interploughing
						if (!StringUtil.isEmpty(data.getKey()) && data.getKey().equalsIgnoreCase("INTPLUG")) {
							boolean isTrue = true;
							List<PeriodicInspectionData> inspectionData = data.getValue();
							for (PeriodicInspectionData periodicInsData : inspectionData) {
								FarmCatalogue cat = getCatlogueValueByCode(periodicInsData.getCatalogueValue());
								if (!ObjectUtil.isEmpty(cat)) {
									String ploughValue = cat.getName();
									if (!isTrue) {
										setSelectedInterPloughing(selectedInterPloughing + "," + ploughValue);
									} else {
										setSelectedInterPloughing(ploughValue);
										isTrue = false;
									}
								} else {
									setSelectedInterPloughing("");
								}
							}
						}

						// PestRec
						if (!StringUtil.isEmpty(data.getKey()) && data.getKey().equalsIgnoreCase("PESTREC")) {
							boolean isTrue = true;
							List<PeriodicInspectionData> inspectionData = data.getValue();
							for (PeriodicInspectionData periodicInsData : inspectionData) {
								if (!getCurrentTenantId().equalsIgnoreCase("efk")) {
									FarmCatalogue cat = getCatlogueValueByCode(periodicInsData.getCatalogueValue());
									if (!ObjectUtil.isEmpty(cat)) {
										String pesticide = cat.getName();
										if (!isTrue) {
											if (periodicInsData.getCatalogueValue().equals("99")
													&& !StringUtil.isEmpty(periodicInsData.getOtherCatalogueValueName())) {
												pestNameValue = pestNameValue + "," + pesticide + ":"
														+ periodicInsData.getOtherCatalogueValueName();
											} else {
												pestNameValue = pestNameValue + "," + pesticide;
											}
										} else {
											if (periodicInsData.getCatalogueValue().equals("99")
													&& !StringUtil.isEmpty(periodicInsData.getOtherCatalogueValueName())) {
												pestNameValue = pesticide + ":"
														+ periodicInsData.getOtherCatalogueValueName();
											} else {
												pestNameValue = pesticide;
											}
											isTrue = false;
										}
									}
								}else{
										String pesticide = periodicInsData.getCatalogueValue();
										if (!isTrue) {
											if (periodicInsData.getCatalogueValue().equals("99")
													&& !StringUtil.isEmpty(periodicInsData.getOtherCatalogueValueName())) {
												pestNameValue = pestNameValue + "," + pesticide + ":"
														+ periodicInsData.getOtherCatalogueValueName();
											} else {
												pestNameValue = pestNameValue + "," + pesticide;
											}
										} else {
											if (periodicInsData.getCatalogueValue().equals("99")
													&& !StringUtil.isEmpty(periodicInsData.getOtherCatalogueValueName())) {
												pestNameValue = pesticide + ":"
														+ periodicInsData.getOtherCatalogueValueName();
											} else {
												pestNameValue = pesticide;
											}
											isTrue = false;
										}
									
								}
								
								
								cocDone = periodicInsData.getCocDone();
								if (!StringUtil.isEmpty(periodicInsData.getUom())) {
									FarmCatalogue catalogue = catalogueService
											.findCatalogueByCode(periodicInsData.getUom());
									String unit = catalogue.getName();
									uom = unit;
									periodicInsData.setUom(uom);
								}
							}
						}
						// fungicide
						if (!StringUtil.isEmpty(data.getKey()) && data.getKey().equalsIgnoreCase("FUNGISREC")) {
							boolean isTrue = true;
							List<PeriodicInspectionData> inspectionData = data.getValue();
							for (PeriodicInspectionData periodicInsData : inspectionData) {
								
								if (!getCurrentTenantId().equalsIgnoreCase("efk")) {
									FarmCatalogue cat = getCatlogueValueByCode(periodicInsData.getCatalogueValue());
									if (!ObjectUtil.isEmpty(cat)) {
										String fungicide = cat.getName();
										if (!isTrue) {
											if (periodicInsData.getCatalogueValue().equals("99")
													&& !StringUtil.isEmpty(periodicInsData.getOtherCatalogueValueName())) {
												fungisideValue = fungisideValue + "," + fungicide + ":"
														+ periodicInsData.getOtherCatalogueValueName();

											} else {
												fungisideValue = fungisideValue + "," + fungicide;
											}
										} else {
											if (periodicInsData.getCatalogueValue().equals("99")
													&& !StringUtil.isEmpty(periodicInsData.getOtherCatalogueValueName())) {
												fungisideValue = fungicide + ":"
														+ periodicInsData.getOtherCatalogueValueName();
											} else {
												fungisideValue = fungicide;
											}
											isTrue = false;
										}
									}
								}else{
									
										String fungicide = periodicInsData.getCatalogueValue();
										if (!isTrue) {
											if (periodicInsData.getCatalogueValue().equals("99")
													&& !StringUtil.isEmpty(periodicInsData.getOtherCatalogueValueName())) {
												fungisideValue = fungisideValue + "," + fungicide + ":"
														+ periodicInsData.getOtherCatalogueValueName();

											} else {
												fungisideValue = fungisideValue + "," + fungicide;
											}
										} else {
											if (periodicInsData.getCatalogueValue().equals("99")
													&& !StringUtil.isEmpty(periodicInsData.getOtherCatalogueValueName())) {
												fungisideValue = fungicide + ":"
														+ periodicInsData.getOtherCatalogueValueName();
											} else {
												fungisideValue = fungicide;
											}
											isTrue = false;
										}
									
								}
								
								
								
								if (!StringUtil.isEmpty(periodicInsData.getUom())) {
									FarmCatalogue catalogue = catalogueService
											.findCatalogueByCode(periodicInsData.getUom());
									String unit = catalogue.getName();
									uom = unit;
									periodicInsData.setUom(uom);
								}

							}
						}

						// fertilizer
						if (!StringUtil.isEmpty(data.getKey()) && data.getKey().equalsIgnoreCase("FRTATYP")) {
							boolean isTrue = true;
							List<PeriodicInspectionData> inspectionData = data.getValue();
							for (PeriodicInspectionData periodicInsData : inspectionData) {
								FarmCatalogue cat = getCatlogueValueByCode(periodicInsData.getCatalogueValue());
								if (!ObjectUtil.isEmpty(cat)) {
									String ferti = cat.getName();
									if (!isTrue) {
										if (periodicInsData.getCatalogueValue().equals("99")
												&& !StringUtil.isEmpty(periodicInsData.getOtherCatalogueValueName())) {
											fertilizerValue = fertilizerValue + "," + ferti + ":"
													+ periodicInsData.getOtherCatalogueValueName();
										} else {
											fertilizerValue = fertilizerValue + "," + ferti;
										}
									} else {
										if (periodicInsData.getCatalogueValue().equals("99")
												&& !StringUtil.isEmpty(periodicInsData.getOtherCatalogueValueName())) {
											fertilizerValue = ferti + ":"
													+ periodicInsData.getOtherCatalogueValueName();
										} else {
											fertilizerValue = ferti;
										}
										isTrue = false;
									}
								}
								cocDone = periodicInsData.getCocDone();

								if (!StringUtil.isEmpty(periodicInsData.getUom())) {
									FarmCatalogue catalogue = catalogueService
											.findCatalogueByCode(periodicInsData.getUom());
									if (!ObjectUtil.isEmpty(catalogue)) {
										String unit = catalogue.getName();
										uom = unit;
									}

									periodicInsData.setUom(uom);
								}
							}
						}

						// manure
						if (!StringUtil.isEmpty(data.getKey()) && data.getKey().equalsIgnoreCase("MATYP")) {
							boolean isTrue = true;
							List<PeriodicInspectionData> inspectionData = data.getValue();
							for (PeriodicInspectionData periodicInsData : inspectionData) {
								FarmCatalogue cat = getCatlogueValueByCode(periodicInsData.getCatalogueValue());
								if (!ObjectUtil.isEmpty(cat)) {
									String manure = cat.getName();
									if (!isTrue) {
										manureValue = manureValue + "," + manure;
									} else {
										manureValue = manure;
										isTrue = false;
									}
								}
								cocDone = periodicInsData.getCocDone();

								if (!StringUtil.isEmpty(periodicInsData.getUom())) {
									FarmCatalogue catalogue = catalogueService
											.findCatalogueByCode(periodicInsData.getUom());
									String unit = catalogue.getName();
									uom = unit;
									periodicInsData.setUom(uom);
								}
							}
						}

					}
				}
			}

			Map<String, List<PeriodicInspectionSymptom>> symMap = new HashMap<>();
			if (!ObjectUtil.isEmpty(periodicInspection.getPeriodicInspectionSymptoms())) {
				symMap = periodicInspection.getPeriodicInspectionSymptoms().stream()
						.collect(Collectors.groupingBy(PeriodicInspectionSymptom::getType));

				if (!ObjectUtil.isEmpty(symMap)) {
					for (Entry<String, List<PeriodicInspectionSymptom>> symptoms : symMap.entrySet()) {
						// Pest Symptoms
						if ("0".equalsIgnoreCase(symptoms.getKey())) {
							boolean isTrue = true;
							List<PeriodicInspectionSymptom> symData = symptoms.getValue();
							for (PeriodicInspectionSymptom symptomsData : symData) {
								FarmCatalogue cat = getCatlogueValueByCode(symptomsData.getSymCode());
								if (!ObjectUtil.isEmpty(cat)) {
									String sympValue = cat.getName();
									if (!isTrue) {
										setSelectedPestSymptom(selectedPestSymptom + "," + sympValue);
									} else {
										setSelectedPestSymptom(sympValue);
										isTrue = false;
									}
								} else {
									setSelectedPestSymptom("");
								}

							}
						}
						// Disease Symptoms
						if ("1".equalsIgnoreCase(symptoms.getKey())) {
							boolean isTrue = true;
							List<PeriodicInspectionSymptom> symData = symptoms.getValue();
							for (PeriodicInspectionSymptom symptomsData : symData) {
								FarmCatalogue cat = getCatlogueValueByCode(symptomsData.getSymCode());
								if (!ObjectUtil.isEmpty(cat)) {
									String sympValue = cat.getName();
									if (!isTrue) {
										setSelectedDiseaseSymptom(selectedDiseaseSymptom + "," + sympValue);
									} else {
										setSelectedDiseaseSymptom(sympValue);
										isTrue = false;
									}
								} else {
									setSelectedDiseaseSymptom("");
								}
							}
						}

					}

				}
			}

			if (!StringUtil.isEmpty(periodicInspection.getWeeding())) {
				FarmCatalogue farmCat = getCatlogueValueByCode(periodicInspection.getWeeding());
				periodicInspection.setWeeding(farmCat == null ? "" : farmCat.getName());

			}

			if (!StringUtil.isEmpty(periodicInspection.getPicking())) {
				FarmCatalogue farmCat = getCatlogueValueByCode(periodicInspection.getPicking());
				periodicInspection.setPicking(farmCat == null ? "" : farmCat.getName());

			}

			inspectionImagesSet = new TreeSet<InspectionImage>();
			if (!ObjectUtil.isEmpty(periodicInspection.getInspectionImageInfo())) {
				for (InspectionImage insImageObj : periodicInspection.getInspectionImageInfo().getInspectionImages()) {
					if (!StringUtil.isEmpty(insImageObj.getPhoto())) {
						insImageObj.setImageByteString(Base64Util.encoder(insImageObj.getPhoto()));
					}
					inspectionImagesSet.add(insImageObj);
				}
			}
			loadMonths();
			command = UPDATE;
			view = DETAIL;
			request.setAttribute(HEADING, getText(DETAIL));
		} else {
			addActionError(NO_RECORD);
			view = REDIRECT;
		}
		return view;
	}

	public String detailNeed() {

		String view = "";
		if (id != null && !id.equals("")) {
			periodicInspection = agentService.findPeriodicInspectionById(Long.valueOf(id));
			if (periodicInspection == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			if (!StringUtil.isEmpty(periodicInspection.getCreatedUserName())) {
				String mobileUser = agentService.findAgentNameByAgentId(periodicInspection.getCreatedUserName());
				if (!ObjectUtil.isEmpty(mobileUser)) {
					setMobileUserName(mobileUser);
				}
			}
			if (!StringUtil.isEmpty(periodicInspection.getCropCode())) {

				String crop = farmerService.findCropNameByCropCode(periodicInspection.getCropCode());
				periodicInspection.setCropCode(StringUtil.isEmpty(crop) ? "" : crop);

			}
			inspectionImagesSet = new TreeSet<InspectionImage>();
			if (!ObjectUtil.isEmpty(periodicInspection.getInspectionImageInfo())) {
				for (InspectionImage insImageObj : periodicInspection.getInspectionImageInfo().getInspectionImages()) {
					if (!StringUtil.isEmpty(insImageObj.getPhoto()) && insImageObj.getPhoto() != null) {
						insImageObj.setImageByteString(Base64Util.encoder(insImageObj.getPhoto()));
						inspectionImagesSet.add(insImageObj);
					}

				}
			}

			command = UPDATE;
			view = DETAILNEED;
			request.setAttribute(HEADING, getText(DETAILNEED));
		} else {
			addActionError(NO_RECORD);
			view = REDIRECT;
		}
		return view;

	}

	public JSONObject toJSON(Object obj) {

		PeriodicInspection periodicInspection = (PeriodicInspection) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();

		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(!StringUtil
						.isEmpty(getBranchesMap().get(getParentBranchMap().get(periodicInspection.getBranchId())))
								? getBranchesMap().get(getParentBranchMap().get(periodicInspection.getBranchId()))
								: getBranchesMap().get(periodicInspection.getBranchId()));
			}
			rows.add(getBranchesMap().get(periodicInspection.getBranchId()));
 
		} else {

			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(branchesMap.get(periodicInspection.getBranchId()));
			}
		}
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
			rows.add(!ObjectUtil.isEmpty(genDate.format(periodicInspection.getInspectionDate()))
					? genDate.format(periodicInspection.getInspectionDate()).toString() : "");
		}
		if (!StringUtil.isEmpty(periodicInspection.getSeasonCode())) {
			HarvestSeason season = farmerService.findSeasonNameByCode(periodicInspection.getSeasonCode());
			/*
			 * rows.add(!ObjectUtil.isEmpty(season)?season.getName()+"-"+season.
			 * getCode():"");
			 */ rows.add(!ObjectUtil.isEmpty(season) ? season.getName() : "");
		} else {
			rows.add("NA");
		}

		if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {

			if (!StringUtil.isEmpty(periodicInspection.getFarm().getFarmer().getFarmerCode())
					&& periodicInspection.getFarm().getFarmer().getFarmerCode() != null) {

				/*rows.add(!ObjectUtil.isEmpty(periodicInspection.getFarm())
						? periodicInspection.getFarm().getFarmer().getFirstName() : "");*/
				
				String firstName =  String.valueOf( periodicInspection.getFarm().getFarmer().getFirstName());
				String farmerId = String.valueOf(periodicInspection.getFarm().getFarmer().getId());
				if(!StringUtil.isEmpty(farmerId) && farmerId!=null){
					String linkField = "<a href=farmer_detail.action?id="+farmerId+" target=_blank>"+firstName+"</a>";
					rows.add(!ObjectUtil.isEmpty(linkField) ? linkField : "");// Farmer
					}else{
						rows.add(firstName);
					}
			} else {
				/*rows.add(!ObjectUtil.isEmpty(periodicInspection.getFarm())
						? periodicInspection.getFarm().getFarmer().getFirstName() : "");*/
				String firstName =  String.valueOf( periodicInspection.getFarm().getFarmer().getFirstName());
				String farmerId = String.valueOf(periodicInspection.getFarm().getFarmer().getId());
				if(!StringUtil.isEmpty(farmerId) && farmerId!=null){
					String linkField = "<a href=farmer_detail.action?id="+farmerId+" target=_blank>"+firstName+"</a>";
					rows.add(!ObjectUtil.isEmpty(linkField) ? linkField : "");// Farmer
					}else{
						rows.add(firstName);
					}
			}
		} else {
			if (!StringUtil.isEmpty(periodicInspection.getFarm().getFarmer().getFarmerCode())
					&& periodicInspection.getFarm().getFarmer().getFarmerCode() != null) {
				rows.add(!ObjectUtil.isEmpty(periodicInspection.getFarm())
						? periodicInspection.getFarm().getFarmer().getFirstName() : "");
			} else {
				rows.add(!ObjectUtil.isEmpty(periodicInspection.getFarm())
						? periodicInspection.getFarm().getFarmer().getFirstName() + "-"
								+ periodicInspection.getFarm().getFarmer().getLastName()
						: "");
			}
		}
		rows.add(!ObjectUtil.isEmpty(periodicInspection.getFarm())
				? periodicInspection.getFarm().getFarmer().getLastName() != null
						? periodicInspection.getFarm().getFarmer().getLastName() : ""
				: "");
		if (!StringUtil.isEmpty(periodicInspection.getFarmId())) {
			rows.add(!ObjectUtil.isEmpty(periodicInspection.getFarm()) ? periodicInspection.getFarm().getFarmName()
					: "");

		}

		if (!StringUtil.isEmpty(periodicInspection.getCropCode())) {

			String crop = farmerService.findCropNameByCropCode(periodicInspection.getCropCode());
			rows.add(StringUtil.isEmpty(crop) ? "" : crop);

		} else {
			rows.add("NA");
		}
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
			if (StringUtil.isEmpty(getBranchId())
					|| getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_ORGANIC_BRANCH_ID)) {
				/*rows.add((!ObjectUtil.isEmpty(periodicInspection.getFarm())
						&& !ObjectUtil.isEmpty(periodicInspection.getFarm().getFarmDetailedInfo())
						&& !StringUtil
								.isEmpty(periodicInspection.getFarm().getFarmDetailedInfo().getLandUnderICSStatus()))
										? getText("icsStatus" + periodicInspection.getFarm().getFarmDetailedInfo()
												.getLandUnderICSStatus())
										: "");*/
				
				FarmIcsConversion icsType = farmerService.findFarmIcsConversionByFarmId(Long.valueOf(periodicInspection.getFarm().getId())); 
				rows.add(!ObjectUtil.isEmpty(icsType)? getText("icsStatus" +icsType.getIcsType()) : "");
			
			}
		}
		if (!StringUtil.isEmpty(periodicInspection.getCreatedUserName())) {
			/*
			 * rows.add(!ObjectUtil.isEmpty(periodicInspection.
			 * getCreatedUserName()) ?
			 * String.valueOf(periodicInspection.getCreatedUserName()) : "");
			 */
			String mobileUser = agentService.findAgentNameByAgentId(periodicInspection.getCreatedUserName());
			if (!StringUtil.isEmpty(mobileUser)) {
				rows.add(mobileUser);

				/*
				 * rows.add(agent.getProfileId()+"-"+agent.getPersonalInfo().
				 * getFirstName());
				 */ } else {
				rows.add("");
			}
		} else {
			rows.add("");
		}

		/*
		 * if (!ObjectUtil.isEmpty(periodicInspection.geteSETxn())) rows
		 * .add(!ObjectUtil.isEmpty(periodicInspection.geteSETxn().getHeader())
		 * ? periodicInspection .geteSETxn().getHeader().getAgentId() + "-" +
		 * periodicInspection.geteSETxn().getHeader().getAgentName() : "NA");
		 * else rows.add("");
		 */

		if ("1".equals(inspectionType)) {
			if(!getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)){
			if ((!StringUtil.isEmpty(periodicInspection.getCurrentStatusOfGrowth())) && (!"null".equalsIgnoreCase(periodicInspection.getCurrentStatusOfGrowth()))) {
				FarmCatalogue catalogue = catalogueService
						.findCatalogueByCode(periodicInspection.getCurrentStatusOfGrowth());
				if (!ObjectUtil.isEmpty(catalogue)) {
					rows.add(catalogue.getName());
				/*	rows.add(!ObjectUtil.isEmpty(catalogue.getName()) ? catalogue.getName() : getText("NA"));*/
				
					} 
				else
					{
					rows.add("NA");
				    }
			} else {
				rows.add("NA");
			}
			}
			if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)) {
				rows.add(getText("VALUE" + String.valueOf(periodicInspection.getPestProblemNoticed())));
				rows.add(getText("VALUE" + String.valueOf(periodicInspection.getDiseaseProblemNoticed())));
			}
			if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)
					&& !getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)
					&& !getCurrentTenantId().equalsIgnoreCase(ESESystem.INDEV_TENENT)
					&& !getCurrentTenantId().equalsIgnoreCase(ESESystem.CRSDEMO_TENANT_ID)
					&& !getCurrentTenantId().equalsIgnoreCase("iccoa")
				    && !getCurrentTenantId().equalsIgnoreCase("agro"))
{
				rows.add(!StringUtil.isEmpty(periodicInspection.getNameOfInterCrop())
						? periodicInspection.getNameOfInterCrop() : "NA");
			}
			/*
			 * if(!ObjectUtil.isEmpty(periodicInspection.getInspectionImageInfo(
			 * ))){ if(!ObjectUtil.isListEmpty(periodicInspection.
			 * getInspectionImageInfo().getInspectionImages()) ){
			 * periodicInspection.getInspectionImageInfo().getInspectionImages()
			 * .stream().forEach(object->{ latitude = object.getLatitude();
			 * longitude = object.getLongitude(); }); if
			 * ((!StringUtil.isEmpty(latitude) &&
			 * !StringUtil.isEmpty(longitude))) {
			 * rows.add("<button class='faMap' title='" +
			 * getText("farm.map.available.title") +
			 * "' onclick='showFarmMap(\""+ (!ObjectUtil.isEmpty(latitude) ?
			 * latitude : "") + "\",\""+ (!ObjectUtil.isEmpty(longitude) ?
			 * longitude : "")+ "\")'></button>"); } else { // No Latlon
			 * rows.add("<button class='no-latLonIcn' title='" +
			 * getText("farm.map.unavailable.title") + "'></button>"); } //
			 * rows.add(latitude); // rows.add(longitude); } }
			 */
			if(getCurrentTenantId().equalsIgnoreCase(ESESystem.AVT)){
				if (!StringUtil.isEmpty(periodicInspection.getCropProtectionPractice())) {
					FarmCatalogue catalogue = catalogueService
							.findCatalogueByCode(periodicInspection.getCropProtectionPractice());
					if (!ObjectUtil.isEmpty(catalogue)) {
						rows.add(catalogue.getName());
					} else {
						rows.add("NA");
					}
				} else {
					rows.add("NA");
				}
			}
			try {
				if ((!StringUtil.isEmpty(periodicInspection.getLatitude())
						&& !StringUtil.isEmpty(periodicInspection.getLongitude()))
						&& Double.valueOf(periodicInspection.getLatitude()) > 0
						&& Double.valueOf(periodicInspection.getLongitude()) > 0) {
					rows.add("<button class='faMap' title='" + getText("farm.map.available.title")
							+ "' onclick='showFarmMap(\""
							+ (!StringUtil.isEmpty(periodicInspection.getLatitude()) ? periodicInspection.getLatitude()
									: "")
							+ "\",\"" + (!StringUtil.isEmpty(periodicInspection.getLongitude())
									? periodicInspection.getLongitude() : "")
							+ "\")'></button>");
				} else {
					// No Latlon
					rows.add("<button class='no-latLonIcn' title='" + getText("farm.map.unavailable.title")
							+ "'></button>");
				}
			} catch (Exception e) {
				rows.add(
						"<button class='no-latLonIcn' title='" + getText("farm.map.unavailable.title") + "'></button>");
			}
			// rows.add(latitude);
			// rows.add(longitude);

		} else {
			rows.add(!StringUtil.isEmpty(periodicInspection.getPurpose()) ? periodicInspection.getPurpose() : "NA");
			rows.add(!StringUtil.isEmpty(periodicInspection.getRemarks()) ? periodicInspection.getRemarks() : "NA");
			/*
			 * if(!ObjectUtil.isEmpty(periodicInspection.getInspectionImageInfo(
			 * ))){ if(!ObjectUtil.isListEmpty(periodicInspection.
			 * getInspectionImageInfo().getInspectionImages()) ){
			 * periodicInspection.getInspectionImageInfo().getInspectionImages()
			 * .stream().forEach(object->{ latitude = object.getLatitude();
			 * longitude = object.getLongitude(); }); if
			 * ((!StringUtil.isEmpty(latitude) &&
			 * !StringUtil.isEmpty(longitude))) {
			 * rows.add("<button class='faMap' title='" +
			 * getText("farm.map.available.title") +
			 * "' onclick='showFarmMap(\""+ (!ObjectUtil.isEmpty(latitude) ?
			 * latitude : "") + "\",\""+ (!ObjectUtil.isEmpty(longitude) ?
			 * longitude : "")+ "\")'></button>");
			 * 
			 * } else { // No Latlon
			 * rows.add("<button class='no-latLonIcn' title='" +
			 * getText("farm.map.unavailable.title") + "'></button>"); } //
			 * rows.add(latitude); // rows.add(longitude); } }
			 */
		

			if (!ObjectUtil.isEmpty(periodicInspection)) {
				if ((!StringUtil.isEmpty(periodicInspection.getLatitude())
						&& !StringUtil.isEmpty(periodicInspection.getLongitude())
						&& Double.valueOf(periodicInspection.getLatitude()) > 0
						&& Double.valueOf(periodicInspection.getLongitude()) > 0)) {
					rows.add("<button class='faMap' title='" + getText("farm.map.available.title")
							+ "' onclick='showFarmMap(\""
							+ (!StringUtil.isEmpty(periodicInspection.getLatitude()) ? periodicInspection.getLatitude()
									: "0")
							+ "\",\"" + (!StringUtil.isEmpty(periodicInspection.getLongitude())
									? periodicInspection.getLongitude() : "0")
							+ "\")'></button>");
				} else {
					// No Latlon
					rows.add("<button class='no-latLonIcn' title='" + getText("farm.map.unavailable.title")
							+ "'></button>");
				}
			} else {
				// No Latlon
				rows.add(
						"<button class='no-latLonIcn' title='" + getText("farm.map.unavailable.title") + "'></button>");
			}
		}

		byte[] voice = (byte[]) periodicInspection.getFarmerVoice();
		if (!ObjectUtil.isEmpty(voice)) {
			rows.add(
					voice.length != 0
							? rows.add("<button type=\"button\" title='" + getText("audio.download")
									+ "' class=\"fa fa-download\" \" aria-hidden=\"true\" align=\"center\" onclick=\"downloadAudioFile("
									+ periodicInspection.getId() + ")\">" + "</button>" + "&nbsp;&nbsp;&nbsp;"
									+ "<button type=\"button\" title='" + getText("audio.play")
									+ "' class=\"fa fa-play-circle-o\" \" aria-hidden=\"true\"\" align=\"center\" onclick=\"playAudioFiles("
									+ periodicInspection.getId() + ",'"
									+ sdf.format(periodicInspection.getInspectionDate()) + "','"
									+ getFarmerIdName(periodicInspection.getFarm().getFarmer().getFarmerId(),
											periodicInspection.getFarm().getFarmer().getName())
									+ "','"
									+ getFarmIdName(periodicInspection.getFarm().getFarmCode(),
											periodicInspection.getFarm().getFarmName())
									+ "')\">" + "</button>")
							: getText(NA));
		} else {
			rows.add("<button type=\"button\" align=\"center\" title='" + getText("audio.noVoice")
					+ "' class=\"fa fa-stop-circle\">" + "</button>");

		}

		jsonObject.put("id", periodicInspection.getId());
		jsonObject.put("cell", rows);
		return jsonObject;
	}

	public String populateInspectionImage() {

		if (id != null) {
			byte[] imgBlob = certificationService.findInspectionImageById(Long.valueOf(id));
			if (!ObjectUtil.isEmpty(imgBlob)) {
				setInputStream(new ByteArrayInputStream(imgBlob));
			}
		}
		return "image";
	}

	public String getCommaSeparatedValueFromSet(Object object) {

		StringBuffer sb = new StringBuffer();
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof Set) {
				Set periodicInspectionData = (Set) object;
				if (!ObjectUtil.isListEmpty(periodicInspectionData)) {
					for (Object periodicInspectionObject : periodicInspectionData) {
						if (periodicInspectionObject instanceof PeriodicInspectionData) {
							PeriodicInspectionData pInspectionData = (PeriodicInspectionData) periodicInspectionObject;
							sb.append(getText(pInspectionData.getType() + pInspectionData.getCatalogueValue()));
							if (!StringUtil.isEmpty(pInspectionData.getOtherCatalogueValueName())) {
								sb.append(" : ");
								sb.append(pInspectionData.getOtherCatalogueValueName());
							}
							sb.append(", ");
						} else if (periodicInspectionObject instanceof PeriodicInspectionSymptom) {
							PeriodicInspectionSymptom pInspectionSymptomData = (PeriodicInspectionSymptom) periodicInspectionObject;
							/*
							 * if (!ObjectUtil.isEmpty(pInspectionSymptomData.
							 * getSymptom())) {
							 * sb.append(pInspectionSymptomData.getSymptom().
							 * getName()); sb.append(", "); }
							 */
						}
					}
				}
			}
		}
		return (sb.toString().trim().length() > 0)
				? sb.toString().trim().substring(0, sb.toString().trim().length() - 1) : "";
	}

	public String getYearMonthByPeriodicType(Object object) {

		StringBuffer sb = new StringBuffer();
		if (!ObjectUtil.isEmpty(object) && object instanceof String) {
			String yearAndMonth = (String) object;
			String yearMonthArr[] = yearAndMonth.split("-");
			if (yearMonthArr.length >= 2) {
				sb.append(yearMonthArr[0]).append(" ").append(months.get(Integer.valueOf(yearMonthArr[1])));
			}
		}
		return sb.toString();
	}

	public void loadMonths() {

		months.add(getText("jan"));
		months.add(getText("feb"));
		months.add(getText("mar"));
		months.add(getText("apr"));
		months.add(getText("may"));
		months.add(getText("jun"));
		months.add(getText("jul"));
		months.add(getText("aug"));
		months.add(getText("sep"));
		months.add(getText("oct"));
		months.add(getText("nov"));
		months.add(getText("dec"));
	}

	public String populateDownload() {

		try {
			filter = agentService.findPeriodicInspectionById(Long.valueOf(id));
			if (ObjectUtil.isEmpty(filter)) {
				return REDIRECT;
			}
			response.setContentType("audio/mpeg");
			response.setHeader("Content-Disposition", "attachment;filename=" + filter.getFarm().getFarmName() + "_"
					+ fileNameDateFormat.format(new Date()) + ".mp3");
			response.getOutputStream().write(filter.getFarmerVoice());
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	public String populateAudioPlay() {

		try {
			byte[] audioContent = agentService.findPeriodicInspectionFarmerVoiceById(Long.valueOf(id));
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
		return null;
	}

	/*
	 * public Map<String, String> getFarmersList() {
	 * 
	 * Map<String, String> farmerListMap = new LinkedHashMap<String, String>();
	 * 
	 * 
	 * List<Farmer> farmersList = farmerService.listFarmer(); for (Farmer obj :
	 * farmersList) { farmerListMap.put(obj.getFarmerId(), obj.getFirstName() +
	 * " " + obj.getLastName() + " - " + obj.getFarmerId()); }
	 * 
	 * 
	 * List<Object[]> farmersList = farmerService.listFarmerInfo();
	 * farmerListMap = farmersList.stream().collect(Collectors.toMap(obj ->
	 * String.valueOf(obj[1]), obj -> (String.valueOf(obj[3]) +
	 * (!StringUtil.isEmpty(obj[2]) ? "-" + String.valueOf(obj[2]) : ""))));
	 * farmerListMap =
	 * farmerListMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
	 * .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,
	 * e2) -> e1, LinkedHashMap::new)); return farmerListMap;
	 * 
	 * }
	 */

	public void populateFarmersList() {
		JSONArray farmerArr = new JSONArray();
		List<Object[]> farmersList = farmerService.listFarmerInfo();

		farmersList.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			if (!StringUtil.isEmpty(obj[2])) {
				farmerArr.add(getJSONObject(objArr[1].toString(), objArr[3].toString() + "-" + objArr[2].toString()));
			} else
				farmerArr.add(getJSONObject(objArr[1].toString(), objArr[3].toString()));
		});

		sendAjaxResponse(farmerArr);
	}

	/**
	 * Gets the farms list.
	 * 
	 * @return the farms list
	 */
	/*
	 * public Map<String, String> getFarmsList() {
	 * 
	 * Map<String, String> farmListMap = new LinkedHashMap<String, String>();
	 * List<Object[]> farmList = farmerService.listPeriodicInspectionFarm(); //
	 * farmListMap = //
	 * farmList.stream().collect(Collectors.toMap(farmIdObj->farmIdObj[0].
	 * toString(),farmNameObj->(String.valueOf(farmNameObj[0])+"-"+farmNameObj[1
	 * ].toString()))); for (Object[] farm : farmList) {
	 * farmListMap.put(String.valueOf(farm[0]), String.valueOf(farm[1])); }
	 * return farmListMap;
	 * 
	 * }
	 */
	public void populateFarmList() {
		JSONArray farmArr = new JSONArray();
		List<Object[]> farmList = farmerService.listPeriodicInspectionFarm();
		if (!ObjectUtil.isEmpty(farmList)) {
			farmList.forEach(obj -> {
				farmArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(farmArr);
	}

	public Map<String, String> getAgentList() {

		Map<String, String> agentListMap = new LinkedHashMap<String, String>();
		List<Object[]> agentList = agentService.listFieldStaffByTxnType("360");
		for (Object[] objArray : agentList) {
			agentListMap.put((String) objArray[0], (String) objArray[0] + "-" + (String) objArray[1]);
		}

		return agentListMap;

	}

	public Map<String, String> getFieldStaffList() {

		Map<String, String> mobileUserLists = new HashMap<String, String>();

		List<Agent> agents = agentService.listAgentByAgentType(AgentType.FIELD_STAFF);
		if (!ObjectUtil.isEmpty(agents)) {
			for (Agent agent : agents) {
				mobileUserLists.put(agent.getProfileId(),
						agent.getPersonalInfo().getAgentName() + " - " + agent.getProfileId());
			}
		}

		return mobileUserLists;
	}

	/*
	 * public Map<String, String> getUserList() {
	 * 
	 * Map<String, String> userMap = new LinkedHashMap<String, String>();
	 * List<Object[]> agentList = agentService.listMobileUser(); for (Object[]
	 * inspection : agentList) { if(!ObjectUtil.isEmpty(inspection)){
	 * userMap.put(inspection[0].toString(),inspection[1].toString()); }
	 * 
	 * }
	 * 
	 * return userMap;
	 * 
	 * }
	 */

	public void populateUserList() {
		JSONArray userArr = new JSONArray();
		List<Object[]> agentList = agentService.listMobileUser();

		if (!ObjectUtil.isEmpty(agentList)) {
			agentList.forEach(obj -> {
				userArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(userArr);
	}

	/**
	 * Gets the agent list.
	 * 
	 * @return the agent list
	 */
	// public List<Agent> getAgentList() {

	// return agentService.listAgent();
	// }
	/**
	 * Sets the agent service.
	 * 
	 * @param agentService
	 *            the new agent service
	 */
	public void setAgentService(IAgentService agentService) {

		this.agentService = agentService;
	}

	/**
	 * Sets the location service.
	 * 
	 * @param locationService
	 *            the new location service
	 */
	public void setLocationService(ILocationService locationService) {

		this.locationService = locationService;
	}

	/**
	 * Gets the filter.
	 * 
	 * @return the filter
	 */

	/**
	 * Gets the coordinate values.
	 * 
	 * @return the coordinate values
	 */
	public String getCoordinateValues() {

		return coordinateValues;
	}

	/**
	 * Sets the device service.
	 * 
	 * @param deviceService
	 *            the new device service
	 */
	public void setDeviceService(IDeviceService deviceService) {

		this.deviceService = deviceService;
	}

	@Override
	public InputStream getExportDataStream(String exportType) throws IOException {

		// TODO Auto-generated method stub
		return null;
	}

	public ICertificationService getCertificationService() {

		return certificationService;
	}

	public void setCertificationService(ICertificationService certificationService) {

		this.certificationService = certificationService;
	}

	public IFarmerService getFarmerService() {

		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {

		this.farmerService = farmerService;
	}

	public IAgentService getAgentService() {

		return agentService;
	}

	public ILocationService getLocationService() {

		return locationService;
	}

	public IDeviceService getDeviceService() {

		return deviceService;
	}

	public void setCoordinateValues(String coordinateValues) {

		this.coordinateValues = coordinateValues;
	}

	public PeriodicInspection getFilter() {

		return filter;
	}

	public void setFilter(PeriodicInspection filter) {

		this.filter = filter;
	}

	public PeriodicInspection getPeriodicInspection() {

		return periodicInspection;
	}

	public void setPeriodicInspection(PeriodicInspection periodicInspection) {

		this.periodicInspection = periodicInspection;
	}

	public String getInspectionType() {

		return inspectionType;
	}

	public void setInspectionType(String inspectionType) {

		this.inspectionType = inspectionType;
	}

	public String getCommand() {

		return command;
	}

	public void setCommand(String command) {

		this.command = command;
	}

	public String getTabIndex() {

		return URLDecoder.decode(tabIndex);
	}

	public void setTabIndex(String tabIndex) {

		this.tabIndex = tabIndex;
	}

	public String getTabIndexNeed() {

		return tabIndexNeed;
	}

	public void setTabIndexNeed(String tabIndexNeed) {

		this.tabIndexNeed = tabIndexNeed;
	}

	@SuppressWarnings("deprecation")
	public String getFarmerDetailzParams() {

		return "tabIndex=" + URLEncoder.encode(tabIndexNeed) + "&id=" + getId() + "&" + tabIndexNeed;
	}

	private String getFarmerIdName(String farmerId, String name) {

		return (farmerId) + "-" + (name);
	}

	private String getFarmerCodeName(String farmerCode, String name) {

		return (farmerCode) + "-" + (name);
	}

	private String getFarmIdName(String farmCode, String farmName) {

		return (farmCode) + "-" + (farmName);
	}

	public String getFarmerId() {

		return farmerId;
	}

	public void setFarmerId(String farmerId) {

		this.farmerId = farmerId;
	}

	public String getFarmId() {

		return farmId;
	}

	public void setFarmId(String farmId) {

		this.farmId = farmId;
	}

	public String getCreatedUserName() {

		return createdUserName;
	}

	public void setCreatedUserName(String createdUserName) {

		this.createdUserName = createdUserName;
	}

	public SortedSet<InspectionImage> getInspectionImagesSet() {

		return inspectionImagesSet;
	}

	public void setInspectionImagesSet(SortedSet<InspectionImage> inspectionImagesSet) {

		this.inspectionImagesSet = inspectionImagesSet;
	}

	public String getBranchIdParma() {

		return branchIdParma;
	}

	public void setBranchIdParma(String branchIdParma) {

		this.branchIdParma = branchIdParma;
	}

	public String getMobileUserName() {

		return mobileUserName;
	}

	public void setMobileUserName(String mobileUserName) {

		this.mobileUserName = mobileUserName;
	}

	public String getDaterange() {

		return daterange;
	}

	public void setDaterange(String daterange) {

		this.daterange = daterange;
	}

	public String getEnableMultiProduct() {

		return enableMultiProduct;
	}

	public void setEnableMultiProduct(String enableMultiProduct) {

		this.enableMultiProduct = enableMultiProduct;
	}

	public Map<String, String> getFarmerFartherNameList() {

		Map<String, String> fathersNameListMap = new LinkedHashMap<String, String>();

		List<Object> farmersList = farmerService.listPeriodicInsoectionFatherName();

		for (Object obj : farmersList) {
			if (!ObjectUtil.isEmpty(obj))
				fathersNameListMap.put(String.valueOf(obj), String.valueOf(obj));
		}
		return fathersNameListMap;

	}

	public void setFarmerFartherNameList(Map<String, String> farmerFartherNameList) {

		this.farmerFartherNameList = farmerFartherNameList;
	}

	public String getFarmerFatherId() {

		return farmerFatherId;
	}

	public void setFarmerFatherId(String farmerFatherId) {

		this.farmerFatherId = farmerFatherId;
	}

	/*
	 * public Map<String, String> getSeasonList() {
	 * 
	 * Map<String, String> seasonMap = new LinkedHashMap<String, String>();
	 * List<Object[]> seasonList = farmerService.listSeasonCodeAndName(); for
	 * (Object[] obj : seasonList) { // seasonMap.put(String.valueOf(obj[0]),
	 * obj[1] + " - " + obj[0]); seasonMap.put(String.valueOf(obj[0]),
	 * String.valueOf(obj[1])); } return seasonMap;
	 * 
	 * }
	 */

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

	public String getSeasonCode() {

		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {

		this.seasonCode = seasonCode;
	}

	public String populateXLS() throws Exception {
		InputStream is = getPeriodicInspectionDataStream();

		setXlsFileName(getText("xlsreportName") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(getText("xlsreportName") + DateUtil.getRevisionNoDateTimeMilliSec(), is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(
				getText("xlsreportName") + DateUtil.getRevisionNoDateTimeMilliSec(), fileMap, ".xls"));

		return "xls";
	}

	private InputStream getPeriodicInspectionDataStream() throws IOException, ParseException {

		PeriodicInspection periodicInspection = new PeriodicInspection();
		LinkedHashMap<String, String> filters = new LinkedHashMap<String, String>();
		LinkedList<String> headersList = new LinkedList<String>();
		List<List<String>> dataList = new ArrayList<List<String>>();

		this.filter = periodicInspection;
		ESESystem preferences = preferncesService.findPrefernceById("1");

		if (!StringUtil.isEmpty(preferences)) {
			DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
			Date sDate = df.parse(getStartDate());
			Date eDate = df.parse(getEndDate());
			filters.put(getText("startDate"), genDate.format(sDate));
			filters.put(getText("EndingDate"), genDate.format(eDate));
		}

		if (!StringUtil.isEmpty(inspectionType) && ObjectUtil.isEmpty(filter.getInspectionType())) {
			filter.setInspectionType(inspectionType);

		}
		if (!StringUtil.isEmpty(branchIdParma)) {
			filter.setBranchId(branchIdParma);

			filters.put(getText("branch"), branchesMap.get(filter.getBranchId()));
		}

		if (!StringUtil.isEmpty(subBranchIdParma) && !subBranchIdParma.equals("0")) {
			filter.setBranchId(subBranchIdParma);
			filters.put(getText("subOrganization"), getBranchesMap().get(getSubBranchIdParma()));
		}

		if (!StringUtil.isEmpty(farmId)) {

			filter.setFarmId(farmId);
			Farm farm = farmerService.findFarmByfarmId(farmId);
			filters.put(getText("farm"), farm.getFarmIdAndName());
		}

		if (!StringUtil.isEmpty(seasonCode)) {

			filter.setSeasonCode(seasonCode);
			HarvestSeason season = farmerService.findHarvestSeasonByCode(filter.getSeasonCode());
			filters.put(getText("season"), season != null ? season.getName() : "");
		} /*
			 * else {
			 * filter.setSeasonCode(clientService.findCurrentSeasonCode()); }
			 */
		if (!StringUtil.isEmpty(icsName)) {
			filter.setIcsName(icsName);
			//FarmCatalogue farmCatalogue = catalogueService.findCatalogueByCode(icsName);
		//	!ObjectUtil.isEmpty(icsType)? getText("icsStatus" +icsType.getIcsType()
			filters.put(getText("icsName"), !ObjectUtil.isEmpty(icsName) ?  getText("icsStatus" +icsName) : "");
		}

		if (!StringUtil.isEmpty(farmerId)) {
			Farmer farmer = new Farmer();
			farmer.setFarmerId(farmerId);
			Farm farm = new Farm();
			farm.setFarmer(farmer);
			filter.setFarm(farm);
			Object[] farmerArray = farmerService.findFarmerAndFatherNameByFarmerId(farmerId);
			filters.put(getText("farmer"), farmerArray[2] + " " + farmerArray[3]);
		}
		if (!StringUtil.isEmpty(farmerFatherId)) {
			Farmer farmer = new Farmer();
			farmer.setLastName(farmerFatherId);
			Farm farm = new Farm();
			farm.setFarmer(farmer);
			filter.setFarm(farm);
			filters.put(getText("father"), farmerFatherId);
		}

		if (!StringUtil.isEmpty(createdUserName)) {
			String mobileUser = agentService.findAgentNameByAgentId(createdUserName);
			filter.setCreatedUserName(createdUserName);
			filters.put(getText("agent"), mobileUser);
		}
		filter.setIsDeleted("0");
		super.filter = this.filter;
		Map datas = readData();
		branchIdValue = getBranchId();
		buildBranchMap();
		String headers = "";
		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {

				headers = getText("reportHeaderWithBranch" + filter.getInspectionType());
			} else if (!StringUtil.isEmpty(branchIdValue)) {
				headers = getText("reportHeader1ForIndev" + filter.getInspectionType());
			}

			if (headers != null) {

				for (String name : headers.split(",")) {
					headersList.add(getLocaleProperty(name));
				}
			}
		} else {
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
				if ("1".equals(inspectionType)) {
					if (!StringUtil.isEmpty(getBranchId()) && getBranchId().equalsIgnoreCase("bci")) {
						headers = getText("inspectionReportHeader1ForPratibhaBci");
					} else if (!StringUtil.isEmpty(getBranchId())
							&& getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_ORGANIC_BRANCH_ID)) {
						headers = getLocaleProperty("inspectionReportHeaderForPratibhaOrganic");
					} else {
						headers = getText("inspectionReportHeader1ForPratibhaNoBranch");
					}
				} else {
					if (!StringUtil.isEmpty(getBranchId())
							&& getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_ORGANIC_BRANCH_ID)) {
						headers = getText("reportHeaderNeedForOrganic");
					} else if (!StringUtil.isEmpty(getBranchId()) && getBranchId().equalsIgnoreCase("bci")) {
						headers = getText("reportHeaderNeedForBci");
					} else {
						headers = getText("reportHeaderNeedPratibhaNoBranch");
					}
				}
			} else {

				if (StringUtil.isEmpty(getBranchId())) {
					headers = getLocaleProperty("reportHeaderBranch" + filter.getInspectionType());

				} else {
					headers = getLocaleProperty("reportHeader" + filter.getInspectionType());

				}

			}
			/*
			 * if
			 * (getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID
			 * )) { if ("1".equals(inspectionType)) { headers =
			 * getText("reportHeader1ForChetna"); } else { headers =
			 * getText("reportHeader2ForChetna"); } }
			 */
			/*
			 * if (getCurrentTenantId().equalsIgnoreCase(ESESystem.
			 * LALTEER_TENANT_ID)) { headers = getText("reportHeaderLalteer" +
			 * filter.getInspectionType()); } if
			 * (getCurrentTenantId().equalsIgnoreCase(ESESystem.
			 * FINCOCOA_TENANT_ID)) { headers = getText("reportHeaderFincocoa" +
			 * filter.getInspectionType()); }
			 */

			if (headers != null) {

				for (String name : headers.split(",")) {
					headersList.add(getLocaleProperty(name));
				}
			}
		}

		for (Object record : (List) datas.get(ROWS)) {
			List<String> list = getDataForReport(record);

 			dataList.add(list);

		}

		Asset existingAssetLogin = clientService.findAssetsByAssetCode(Asset.APP_LOGO);

		InputStream stream = ExportUtil.exportXLS(dataList, headersList, filters,
				getText("periodicReportName" + filter.getInspectionType()), getText("filter"),
				getText("periodicReportTopic" + filter.getInspectionType()), existingAssetLogin.getFile());
		return stream;
	}

	public InputStream getFileInputStream() {

		return fileInputStream;
	}

	public void setFileInputStream(InputStream fileInputStream) {

		this.fileInputStream = fileInputStream;
	}

	public String getXlsFileName() {

		return xlsFileName;
	}

	public void setXlsFileName(String xlsFileName) {

		this.xlsFileName = xlsFileName;
	}

	Long serialNumber = 0L;

	public List<String> getDataForReport(Object obj) {

		List<String> dataList = new ArrayList<>();
		PeriodicInspection periodicInspection = (PeriodicInspection) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();

		serialNumber++;
		dataList.add(String.valueOf(serialNumber) != null ? String.valueOf(serialNumber) : "");
		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

			if (StringUtil.isEmpty(branchIdValue)) {
				dataList.add(!StringUtil
						.isEmpty(getBranchesMap().get(getParentBranchMap().get(periodicInspection.getBranchId())))
								? getBranchesMap().get(getParentBranchMap().get(periodicInspection.getBranchId()))
								: getBranchesMap().get(periodicInspection.getBranchId()));
			}
			dataList.add(getBranchesMap().get(periodicInspection.getBranchId()));
		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				dataList.add(branchesMap.get(periodicInspection.getBranchId()));
			}
		}

		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
			dataList.add(!ObjectUtil.isEmpty(genDate.format(periodicInspection.getInspectionDate()))
					? genDate.format(periodicInspection.getInspectionDate()).toString() : "");
		}
		if (!StringUtil.isEmpty(periodicInspection.getSeasonCode())) {
			HarvestSeason season = farmerService.findSeasonNameByCode(periodicInspection.getSeasonCode());
			dataList.add(!ObjectUtil.isEmpty(season) ? season.getName() : "");
			/*
			 * dataList.add( !ObjectUtil.isEmpty(season) ? season.getName() +
			 * "-" + season.getCode() : "");
			 */
		} else {
			dataList.add("NA");
		}
		if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {
			if (!StringUtil.isEmpty(periodicInspection.getFarm().getFarmer().getFarmerCode())
					&& periodicInspection.getFarm().getFarmer().getFarmerCode() != null) {
				/*
				 * dataList.add(!ObjectUtil.isEmpty(periodicInspection.getFarm()
				 * ) ? periodicInspection.getFarm().getFarmer().getFarmerCode()
				 * + "-" +
				 * periodicInspection.getFarm().getFarmer().getFirstName() :
				 * "");
				 */
				dataList.add(periodicInspection.getFarm().getFarmer().getFirstName());

			} else {
				dataList.add(!ObjectUtil.isEmpty(periodicInspection.getFarm())
						? periodicInspection.getFarm().getFarmer().getFirstName() : "");
			}
		} else {
			dataList.add(!ObjectUtil.isEmpty(periodicInspection.getFarm())
					? periodicInspection.getFarm().getFarmer().getFarmerCode() + "-"
							+ periodicInspection.getFarm().getFarmer().getFirstName() + " "
							+ periodicInspection.getFarm().getFarmer().getLastName()
					: "");
		}
		if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {
			dataList.add(!ObjectUtil.isEmpty(periodicInspection.getFarm())
					? periodicInspection.getFarm().getFarmer().getLastName() != null
							? periodicInspection.getFarm().getFarmer().getLastName() : ""
					: "");
		}
		dataList.add(
				!ObjectUtil.isEmpty(periodicInspection.getFarm()) ? periodicInspection.getFarm().getFarmName() : "");
		if (!StringUtil.isEmpty(periodicInspection.getCropCode())) {
			String crop = farmerService.findCropNameByCropCode(periodicInspection.getCropCode());
			dataList.add(!StringUtil.isEmpty(crop) ? crop : "");
		} else {
			dataList.add("");
		}

		if (getCurrentTenantId().equals("pratibha")) {
			if (StringUtil.isEmpty(getBranchId())
					|| getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_ORGANIC_BRANCH_ID)) {
				if (!ObjectUtil.isEmpty(periodicInspection) && !ObjectUtil.isEmpty(periodicInspection.getFarm())) {
					FarmIcsConversion icsType = farmerService
							.findFarmIcsConversionByFarmId(Long.valueOf(periodicInspection.getFarm().getId()));
					dataList.add(!ObjectUtil.isEmpty(icsType) ? getText("icsStatus" + icsType.getIcsType()) : "");
				} else {
					dataList.add(getText("NA"));
				}
			}
		}
		FarmIcsConversion icsType = farmerService.findFarmIcsConversionByFarmId(Long.valueOf(periodicInspection.getFarm().getId())); 
		rows.add(!ObjectUtil.isEmpty(icsType)? getText("icsStatus" +icsType.getIcsType()) : "");
		if (!StringUtil.isEmpty(periodicInspection.getCreatedUserName())) {
			String agent = agentService.findAgentNameByAgentId(periodicInspection.getCreatedUserName());
			if (!StringUtil.isEmpty(agent)) {
				/*
				 * dataList.add(agent.getProfileId() + "-" +
				 * agent.getPersonalInfo().getFirstName());
				 */
				dataList.add(agent);
			} else {
				dataList.add("");
			}
		} else {
			dataList.add("");
		}

		if ("1".equals(inspectionType)) {
			if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)) {
				if ((!StringUtil.isEmpty(periodicInspection.getCurrentStatusOfGrowth()))
						&& (!"null".equalsIgnoreCase(periodicInspection.getCurrentStatusOfGrowth()))) {
					FarmCatalogue catalogue = getCatlogueValueByCode(periodicInspection.getCurrentStatusOfGrowth());
					if (!ObjectUtil.isEmpty(catalogue)) {
						dataList.add(catalogue.getName());
					}

				}

				else {
					dataList.add("NA");
				}
			}
			if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)) {
				dataList.add(getText("PESTREC" + String.valueOf(periodicInspection.getPestProblemNoticed())));
				dataList.add(getText("DISEASE" + String.valueOf(periodicInspection.getDiseaseProblemNoticed())));
			}
			if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)
					&& !getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)
					&& !getCurrentTenantId().equalsIgnoreCase(ESESystem.INDEV_TENENT)
					&& !getCurrentTenantId().equalsIgnoreCase("iccoa")
					&& !getCurrentTenantId().equalsIgnoreCase("agro"))
				dataList.add(!StringUtil.isEmpty(periodicInspection.getNameOfInterCrop())
						? periodicInspection.getNameOfInterCrop() : "NA");
		} else {
			dataList.add(!StringUtil.isEmpty(periodicInspection.getPurpose()) ? periodicInspection.getPurpose() : "NA");
			dataList.add(!StringUtil.isEmpty(periodicInspection.getRemarks()) ? periodicInspection.getRemarks() : "NA");
		}

		return dataList;
	}

	public String update() throws Exception {

		boolean first = true;

		ESESystem preferences = preferncesService.findPrefernceById("1");
		DateFormat genDate = new SimpleDateFormat(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));

		if (id != null && !id.equals("")) {
			periodicInspection = clientService.findPeriodicInspectionById(Long.valueOf(id));
			if (periodicInspection == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			if (!StringUtil.isEmpty(periodicInspection.getCreatedUserName())) {
				String mobileUser = agentService.findAgentNameByAgentId(periodicInspection.getCreatedUserName());
				if (!StringUtil.isEmpty(mobileUser)) {
					setMobileUserName(mobileUser);
				}
			}

			if (!StringUtil.isEmpty(periodicInspection.getInspectionDate())) {
				periodicInspection.setInspectionDate(periodicInspection.getInspectionDate());
			}
			if (!ObjectUtil.isEmpty(periodicInspection.getFarm())
					&& !ObjectUtil.isEmpty(periodicInspection.getFarm().getFarmer())
					&& !StringUtil.isEmpty(periodicInspection.getFarm().getFarmer().getFarmerCodeAndName())) {
				setFarmerCodeName(periodicInspection.getFarm().getFarmer().getFarmerCodeAndName());

			}

			if (!ObjectUtil.isEmpty(periodicInspection.getFarm())
					&& !ObjectUtil.isEmpty(periodicInspection.getFarm().getFarmer())
					&& !StringUtil.isEmpty(periodicInspection.getFarm().getFarmer().getFarmerIdAndName())) {
				setFarmerIdName(periodicInspection.getFarm().getFarmer().getFarmerIdAndName());

			}

			if (periodicInspection.getFarmerVoice() != null) {
				setAudioByteString(Base64Util.encoder(periodicInspection.getFarmerVoice()));
			}

			if (!ObjectUtil.isEmpty(periodicInspection.getFarm())
					&& !StringUtil.isEmpty(periodicInspection.getFarm().getFarmIdAndName())) {
				setFarmIdName(periodicInspection.getFarm().getFarmIdAndName());

			}

			if (!StringUtil.isEmpty(periodicInspection.getInspectionDate())) {
				setInspectionOfDate(String.valueOf(periodicInspection.getInspectionDate()));

			}

			if (!StringUtil.isEmpty(periodicInspection.getCreatedUserName())) {
				setMobileId(periodicInspection.getCreatedUserName());

			}

			/*
			 * if (periodicInspection.getGapPlantingDate() != null) {
			 * gapPlanting =
			 * dateFormat.format(periodicInspection.getGapPlantingDate());
			 * 
			 * }
			 */

			if (periodicInspection.getGapPlantingDate() != null) {

				gapPlanting = genDate.format(periodicInspection.getGapPlantingDate());
			}


			if (!StringUtil.isEmpty(periodicInspection.getSurvivalPercentage())) {
				FarmCatalogue cat = catalogueService.findCatalogueByCode(periodicInspection.getSurvivalPercentage());
				if (!ObjectUtil.isEmpty(cat)) {
					periodicInspection.setSurvivalPercentage(cat.getName());
				}
			}
			if (periodicInspection.getCurrentStatusOfGrowth() != null) {
				setSelectedGrowth(periodicInspection.getCurrentStatusOfGrowth());
			}
			setIsSelectedCrop(String.valueOf(periodicInspection.getInterCrop()));

			setIsPestNoticed(String.valueOf(periodicInspection.getPestProblemNoticed()));
			setIsPestETL(String.valueOf(periodicInspection.getPestInfestationETL()));
			setIsPestSolved(String.valueOf(periodicInspection.getPestProblemSolved()));

			setIsDiseaseNoticed(String.valueOf(periodicInspection.getDiseaseProblemNoticed()));
			setIsDiseaseETL(String.valueOf(periodicInspection.getDiseaseInfestationETL()));
			setIsDiseaseSolved(String.valueOf(periodicInspection.getDiseaseProblemSolved()));
			setChemicalNameString(periodicInspection.getChemicalName());

			pestRecomList = farmerService.listPestRecomentedById(Long.valueOf(periodicInspection.getId()));
			if (!StringUtil.isEmpty(pestRecomList)) {
				for (PeriodicInspectionData data : pestRecomList) {
					FarmCatalogue catValue = catalogueService.findCatalogueByCode(data.getCatalogueValue());
					if (!ObjectUtil.isEmpty(catValue)) {
						String value = catValue.getName();

						if (!first) {
							if (data.getCatalogueValue().equals("99")
									&& !StringUtil.isEmpty(data.getOtherCatalogueValueName())) {
								pestNameValue = pestNameValue + "," + value + ":" + data.getOtherCatalogueValueName();
							} else {
								pestNameValue = pestNameValue + "," + value;
							}
						} else {
							if (data.getCatalogueValue().equals("99")
									&& !StringUtil.isEmpty(data.getOtherCatalogueValueName())) {
								pestNameValue = value + ":" + data.getOtherCatalogueValueName();
							} else {
								pestNameValue = value;
							}
							first = false;
						}
					}

				}

			}
			diseaseList = farmerService.listDiseaseById(Long.valueOf(periodicInspection.getId()));
			if (!StringUtil.isEmpty(diseaseList)) {
				for (PeriodicInspectionData data : diseaseList) {
					FarmCatalogue catValue = catalogueService.findCatalogueByCode(data.getCatalogueValue());
					if (!ObjectUtil.isEmpty(catValue)) {
						String value = catValue.getName();

						if (!first) {
							if (data.getCatalogueValue().equals("99")
									&& !StringUtil.isEmpty(data.getOtherCatalogueValueName())) {
								diseaseNameValue = diseaseNameValue + "," + value + ":"
										+ data.getOtherCatalogueValueName();
							} else {
								diseaseNameValue = diseaseNameValue + "," + value;
							}
						} else {
							if (data.getCatalogueValue().equals("99")
									&& !StringUtil.isEmpty(data.getOtherCatalogueValueName())) {
								diseaseNameValue = value + ":" + data.getOtherCatalogueValueName();
							} else {
								diseaseNameValue = value;
							}
							first = false;
						}
					}

				}

			}

			fungiList = farmerService.listFungicideById(Long.valueOf(periodicInspection.getId()));

			if (!StringUtil.isEmpty(fungiList)) {
				boolean isTrue = true;
				for (PeriodicInspectionData data : fungiList) {
					FarmCatalogue catValue = catalogueService.findCatalogueByCode(data.getCatalogueValue());
					if (!ObjectUtil.isEmpty(catValue)) {
						String value = catValue.getName();
						if (!isTrue) {
							if (data.getCatalogueValue().equals("99")
									&& !StringUtil.isEmpty(data.getOtherCatalogueValueName())) {
								fungisideValue = fungisideValue + "," + value + ":" + data.getOtherCatalogueValueName();
							} else {
								fungisideValue = fungisideValue + "," + value;
							}
						} else {
							if (data.getCatalogueValue().equals("99")
									&& !StringUtil.isEmpty(data.getOtherCatalogueValueName())) {
								fungisideValue = value + ":" + data.getOtherCatalogueValueName();

							} else {
								fungisideValue = value;
							}

							isTrue = false;
						}
					}

				}

			}

			manureList = farmerService.listManureById(Long.valueOf(periodicInspection.getId()));

			if (!StringUtil.isEmpty(manureList)) {
				boolean isTrue = true;
				for (PeriodicInspectionData data : manureList) {
					FarmCatalogue catValue = catalogueService.findCatalogueByCode(data.getCatalogueValue());
					if (!ObjectUtil.isEmpty(catValue)) {
						String value = catValue.getName();
						if (!isTrue) {
							manureValue = manureValue + "," + value;
						} else {
							manureValue = value;
							isTrue = false;

						}
					}
				}

			}

			fertilizerList = farmerService.listFertilizerById(Long.valueOf(periodicInspection.getId()));
			if (!StringUtil.isEmpty(fertilizerList)) {
				boolean isTrue = true;
				for (PeriodicInspectionData data : fertilizerList) {
					FarmCatalogue catValue = catalogueService.findCatalogueByCode(data.getCatalogueValue());
					if (!ObjectUtil.isEmpty(catValue)) {
						String value = catValue.getName();
						if (!isTrue) {
							if (data.getCatalogueValue().equals("99")
									&& !StringUtil.isEmpty(data.getOtherCatalogueValueName())) {
								fertilizerValue = fertilizerValue + "," + value + ":"
										+ data.getOtherCatalogueValueName();
							} else {
								fertilizerValue = fertilizerValue + "," + value;
							}
						} else {
							if (data.getCatalogueValue().equals("99")
									&& !StringUtil.isEmpty(data.getOtherCatalogueValueName())) {
								fertilizerValue = value + ":" + data.getOtherCatalogueValueName();
							} else {
								fertilizerValue = value;
							}
							isTrue = false;

						}
					}
				}

			}

			if (!ObjectUtil.isEmpty(periodicInspection.getPeriodicInspectionData())) {

				for (PeriodicInspectionData data : periodicInspection.getPeriodicInspectionData()) {
					// Pest Name
					if (data.getType().equalsIgnoreCase("PEST")) {
						boolean isTrue = true;
						String value = "PEST";
						List<PeriodicInspectionData> inspectionData = farmerService.findPeriodicDataByType(value,
								periodicInspection.getId());
						for (PeriodicInspectionData periodicInsData : inspectionData) {
							String pestNameValue = periodicInsData.getCatalogueValue();
							if (!isTrue) {
								setSelectedPestName(selectedPestName + "," + pestNameValue);
							} else {
								setSelectedPestName(pestNameValue);
								isTrue = false;
							}

							if ("99".equals(selectedPestName)) {
								setSelectedOtherPest(periodicInsData.getOtherCatalogueValueName());

							}
						}

					}
					// Disease Name
					if (data.getType().equalsIgnoreCase("DISEASE")) {
						boolean isTrue = true;
						String value = "DISEASE";
						List<PeriodicInspectionData> inspectionData = farmerService.findPeriodicDataByType(value,
								periodicInspection.getId());
						for (PeriodicInspectionData periodicInsData : inspectionData) {
							String diseaseValue = periodicInsData.getCatalogueValue();
							if (!isTrue) {
								setSelectedDiseaseName(selectedDiseaseName + "," + diseaseValue);
							} else {
								setSelectedDiseaseName(diseaseValue);
								isTrue = false;
							}

							if ("99".equals(selectedDiseaseName)) {
								setSelectedOtherDisease(periodicInsData.getOtherCatalogueValueName());

							}
						}
					}
					// ActivitiesCarriedOut
					if (data.getType().equalsIgnoreCase("ACAPV")) {
						boolean isTrue = true;
						String value = "ACAPV";
						List<PeriodicInspectionData> inspectionData = farmerService.findPeriodicDataByType(value,
								periodicInspection.getId());
						for (PeriodicInspectionData periodicInsData : inspectionData) {
							String acapvValue = periodicInsData.getCatalogueValue();
							if (!isTrue) {
								setSelectedActivities(selectedActivities + "," + acapvValue);
							} else {
								setSelectedActivities(acapvValue);
								isTrue = false;
							}
						}
					}
					// Interploughing
					if (data.getType().equalsIgnoreCase("INTPLUG")) {
						boolean isTrue = true;
						String value = "INTPLUG";
						List<PeriodicInspectionData> inspectionData = farmerService.findPeriodicDataByType(value,
								periodicInspection.getId());
						for (PeriodicInspectionData periodicInsData : inspectionData) {
							String ploughValue = periodicInsData.getCatalogueValue();
							if (!isTrue) {
								setSelectedInterPloughing(selectedInterPloughing + "," + ploughValue);
							} else {
								setSelectedInterPloughing(ploughValue);
								isTrue = false;
							}
						}
					}

				}

			}
			if (!ObjectUtil.isEmpty(periodicInspection.getPeriodicInspectionSymptoms())) {
				for (PeriodicInspectionSymptom symptoms : periodicInspection.getPeriodicInspectionSymptoms()) {
					// Pest Symptoms
					if ("0".equalsIgnoreCase(symptoms.getType())) {
						boolean isTrue = true;
						String value = "0";
						List<PeriodicInspectionSymptom> symData = farmerService.findPeriodicSymptomsByType(value,
								periodicInspection.getId());
						for (PeriodicInspectionSymptom symptomsData : symData) {
							String sympValue = symptomsData.getSymCode();
							if (!isTrue) {
								setSelectedPestSymptom(selectedPestSymptom + "," + sympValue);
							} else {
								setSelectedPestSymptom(sympValue);
								isTrue = false;
							}

						}
					}
					// Disease Symptoms
					if ("1".equalsIgnoreCase(symptoms.getType())) {
						boolean isTrue = true;
						String value = "1";
						List<PeriodicInspectionSymptom> symData = farmerService.findPeriodicSymptomsByType(value,
								periodicInspection.getId());
						for (PeriodicInspectionSymptom symptomsData : symData) {
							String sympValue = symptomsData.getSymCode();
							if (!isTrue) {
								setSelectedDiseaseSymptom(selectedDiseaseSymptom + "," + sympValue);
							} else {
								setSelectedDiseaseSymptom(sympValue);
								isTrue = false;
							}

						}
					}

				}

			}

			setCurrentPage(getCurrentPage());
			id = null;
			command = UPDATE;
			request.setAttribute(HEADING, getText(UPDATE));
		} else {

			String value = validateInspectionData();
			if (value == "true") {
				if (periodicInspection != null) {
					PeriodicInspection existing = clientService.findPeriodicInspectionById(periodicInspection.getId());
					if (existing == null) {
						addActionError(NO_RECORD);
						return REDIRECT;
					}
					setCurrentPage(getCurrentPage());
					existing.setFarmerOpinion(periodicInspection.getFarmerOpinion());
					existing.setRemarks(periodicInspection.getRemarks());
					if (!StringUtil.isEmpty(isPestNoticed != null)) {
						existing.setPestProblemNoticed(isPestNoticed.charAt(0));
					}
					// pest Name
					Set<PeriodicInspectionData> inspectionDataSet = new HashSet<PeriodicInspectionData>();
					Set<PeriodicInspectionSymptom> inspectionSymptomSet = new HashSet<PeriodicInspectionSymptom>();
					if (existing.getPestProblemNoticed() == 'Y') {
						if (StringUtil.isEmpty(selectedPestName)) {
							existing.setPeriodicInspectionData(new HashSet<PeriodicInspectionData>());
						} else {
							String[] dataArr = selectedPestName.split(",");
							for (String dataList : dataArr) {
								PeriodicInspectionData data = new PeriodicInspectionData();
								data.setCatalogueValue(dataList.trim());
								data.setType(PeriodicInspection.PEST_NAME);
								if ("99".equals(dataList.trim())) {
									data.setOtherCatalogueValueName(
											!StringUtil.isEmpty(selectedOtherPest) ? selectedOtherPest : null);
								} else {
									data.setOtherCatalogueValueName(null);
								}
								data.setQuantityValue(null);
								inspectionDataSet.add(data);
								existing.setPeriodicInspectionData(inspectionDataSet);
							}
						}
					}
					// PestSymptom
					if (existing.getPestProblemNoticed() == 'Y') {
						if (StringUtil.isEmpty(selectedPestSymptom)) {
							existing.setPeriodicInspectionSymptoms(new HashSet<PeriodicInspectionSymptom>());
						} else {
							String[] dataArr = selectedPestSymptom.split(",");
							for (String dataList : dataArr) {
								PeriodicInspectionSymptom symptom = new PeriodicInspectionSymptom();
								symptom.setSymCode(dataList.trim());
								symptom.setType("0");
								inspectionSymptomSet.add(symptom);
								existing.setPeriodicInspectionSymptoms(inspectionSymptomSet);
							}
						}
					}
					if (!StringUtil.isEmpty(isDiseaseNoticed != null)) {
						existing.setDiseaseProblemNoticed(isDiseaseNoticed.charAt(0));
					}
					// Disease name
					if (existing.getDiseaseProblemNoticed() == 'Y') {
						if (StringUtil.isEmpty(selectedDiseaseName)) {
							existing.setPeriodicInspectionData(new HashSet<PeriodicInspectionData>());
						} else {
							String[] dataArr = selectedDiseaseName.split(",");
							for (String dataList : dataArr) {
								PeriodicInspectionData data = new PeriodicInspectionData();
								data.setCatalogueValue(dataList.trim());
								data.setType(PeriodicInspection.DISEASE_NAME);
								if ("99".equals(dataList.trim())) {
									data.setOtherCatalogueValueName(
											!StringUtil.isEmpty(selectedOtherDisease) ? selectedOtherDisease : null);
								} else {
									data.setOtherCatalogueValueName(null);
								}
								data.setQuantityValue(null);
								inspectionDataSet.add(data);
								existing.setPeriodicInspectionData(inspectionDataSet);
							}
						}
					}
					// DiseaseSymptoms
					if (existing.getDiseaseProblemNoticed() == 'Y') {
						if (StringUtil.isEmpty(selectedDiseaseSymptom)) {
							existing.setPeriodicInspectionSymptoms(new HashSet<PeriodicInspectionSymptom>());
						} else {
							String[] dataArr = selectedDiseaseSymptom.split(",");
							for (String dataList : dataArr) {
								PeriodicInspectionSymptom symptom = new PeriodicInspectionSymptom();
								symptom.setSymCode(dataList.trim());
								symptom.setType("1");
								inspectionSymptomSet.add(symptom);
								existing.setPeriodicInspectionSymptoms(inspectionSymptomSet);
							}
						}
					}
					// activities Carried
					if (StringUtil.isEmpty(selectedActivities)) {
						existing.setPeriodicInspectionData(new HashSet<PeriodicInspectionData>());
					} else {
						String[] dataArr = selectedActivities.split(",");
						for (String dataList : dataArr) {
							PeriodicInspectionData data = new PeriodicInspectionData();
							data.setCatalogueValue(dataList.trim());
							data.setType(PeriodicInspection.ACTIVITIES_CARRIED_OUT);
							data.setOtherCatalogueValueName(null);
							data.setQuantityValue(null);
							inspectionDataSet.add(data);
							existing.setPeriodicInspectionData(inspectionDataSet);
						}
					}
					// InterPlough
					if (StringUtil.isEmpty(selectedInterPloughing)) {
						existing.setPeriodicInspectionData(new HashSet<PeriodicInspectionData>());
					} else {
						String[] dataArr = selectedInterPloughing.split(",");
						for (String dataList : dataArr) {
							PeriodicInspectionData data = new PeriodicInspectionData();
							data.setCatalogueValue(dataList.trim());
							data.setType(PeriodicInspection.INTERPLOUGHING_WITH);
							data.setOtherCatalogueValueName(null);
							data.setQuantityValue(null);
							inspectionDataSet.add(data);
							existing.setPeriodicInspectionData(inspectionDataSet);
						}
					}
					// PestRecommented
					if (isPestETL.equals("Y")) {
						if (!StringUtil.isEmpty(pestJson)) {
							if (!getCurrentTenantId().equalsIgnoreCase("efk")) {
							Type expenditureType = new TypeToken<List<PeriodicInspectionData>>() {
							}.getType();
							List<PeriodicInspectionData> inspection = new Gson().fromJson(pestJson, expenditureType);
							for (int i = 0; i < inspection.size(); i++) {
								if (!StringUtil.isEmpty(inspection.get(i).getCatalogueValue())) {
									PeriodicInspectionData dataObj = new PeriodicInspectionData();
									FarmCatalogue catvalue = catalogueService
											.findCatalogueByName(inspection.get(i).getCatalogueValue());
									if (!ObjectUtil.isEmpty(catvalue)) {
										dataObj.setCatalogueValue(catvalue.getCode());
									} else {
										dataObj.setCatalogueValue(
												inspection.get(i).getCatalogueValue().split("-")[0].trim());
									}
									dataObj.setType("PESTREC");
									dataObj.setOtherCatalogueValueName(
											!StringUtil.isEmpty(inspection.get(i).getOtherCatalogueValueName())
													? inspection.get(i).getOtherCatalogueValueName() : null);
									dataObj.setQuantityValue(inspection.get(i).getQuantityValue());
									inspectionDataSet.add(dataObj);
								}
							}

							existing.setPeriodicInspectionData(inspectionDataSet);
						}else{
							PeriodicInspectionData dataObj = new PeriodicInspectionData();
							dataObj.setCatalogueValue(pestJson.trim());
							dataObj.setType("PESTREC");
							inspectionDataSet.add(dataObj);
						}
						}
					}
					// Fertilizer
					/* if(isDiseaseETL.equals("Y")){ */
					if (!StringUtil.isEmpty(fertiJson)) {
						
						Type expenditureType = new TypeToken<List<PeriodicInspectionData>>() {
						}.getType();
						List<PeriodicInspectionData> inspection = new Gson().fromJson(fertiJson, expenditureType);
						for (int i = 0; i < inspection.size(); i++) {
							if (!StringUtil.isEmpty(inspection.get(i).getCatalogueValue())) {
								PeriodicInspectionData dataObj = new PeriodicInspectionData();
								FarmCatalogue catvalue = catalogueService
										.findCatalogueByName(inspection.get(i).getCatalogueValue());
								if (!ObjectUtil.isEmpty(catvalue)) {
									dataObj.setCatalogueValue(catvalue.getCode());
								} else {
									dataObj.setCatalogueValue(
											inspection.get(i).getCatalogueValue().split("-")[0].trim());
								}
								dataObj.setType("FRTATYP");
								dataObj.setOtherCatalogueValueName(
										!StringUtil.isEmpty(inspection.get(i).getOtherCatalogueValueName())
												? inspection.get(i).getOtherCatalogueValueName() : null);
								dataObj.setQuantityValue(inspection.get(i).getQuantityValue());
								inspectionDataSet.add(dataObj);
							}
						}
						
						existing.setPeriodicInspectionData(inspectionDataSet);
					}
					/* } */
					// DiseaseRecom
					if (isDiseaseETL.equals("Y")) {
						if (!StringUtil.isEmpty(diseaseJson)) {
							if (!getCurrentTenantId().equalsIgnoreCase("efk")) {
							Type expenditureType = new TypeToken<List<PeriodicInspectionData>>() {
							}.getType();
							List<PeriodicInspectionData> inspection = new Gson().fromJson(diseaseJson, expenditureType);
							for (int i = 0; i < inspection.size(); i++) {
								if (!StringUtil.isEmpty(inspection.get(i).getCatalogueValue())) {
									PeriodicInspectionData dataObj = new PeriodicInspectionData();
									FarmCatalogue catvalue = catalogueService
											.findCatalogueByName(inspection.get(i).getCatalogueValue());
									if (!ObjectUtil.isEmpty(catvalue)) {
										dataObj.setCatalogueValue(catvalue.getCode());
									} else {
										dataObj.setCatalogueValue(
												inspection.get(i).getCatalogueValue().split("-")[0].trim());
									}
									dataObj.setType("FUNGISREC");
									dataObj.setOtherCatalogueValueName(
											!StringUtil.isEmpty(inspection.get(i).getOtherCatalogueValueName())
													? inspection.get(i).getOtherCatalogueValueName() : null);
									dataObj.setQuantityValue(inspection.get(i).getQuantityValue());
									inspectionDataSet.add(dataObj);
								}
							}
							}else{
								PeriodicInspectionData dataObj = new PeriodicInspectionData();
								dataObj.setCatalogueValue(diseaseJson.trim());
								dataObj.setType("FUNGISREC");
								inspectionDataSet.add(dataObj);
							}
							existing.setPeriodicInspectionData(inspectionDataSet);
						}
					}
					/* } */
					// Manure
					if (!StringUtil.isEmpty(selectedActivities)) {
						String[] selArr = selectedActivities.split(",");
						for (String arr : selArr) {
							if ("5".equals(arr.trim())) {

								if (!StringUtil.isEmpty(manureJson)) {
									Type expenditureType = new TypeToken<List<PeriodicInspectionData>>() {
									}.getType();
									List<PeriodicInspectionData> inspection = new Gson().fromJson(manureJson,
											expenditureType);
									for (int i = 0; i < inspection.size(); i++) {
										if (!StringUtil.isEmpty(inspection.get(i).getCatalogueValue())) {
											PeriodicInspectionData dataObj = new PeriodicInspectionData();
											FarmCatalogue catvalue = catalogueService
													.findCatalogueByName(inspection.get(i).getCatalogueValue());
											if (!ObjectUtil.isEmpty(catvalue)) {
												dataObj.setCatalogueValue(catvalue.getCode());
											} else {
												dataObj.setCatalogueValue(
														inspection.get(i).getCatalogueValue().split("-")[0].trim());
											}
											dataObj.setType("MATYP");
											dataObj.setOtherCatalogueValueName(
													!StringUtil.isEmpty(inspection.get(i).getOtherCatalogueValueName())
															? inspection.get(i).getOtherCatalogueValueName() : null);
											dataObj.setQuantityValue(inspection.get(i).getQuantityValue());
											inspectionDataSet.add(dataObj);
										}
									}
									existing.setPeriodicInspectionData(inspectionDataSet);
								}
							}
							/*
							 * if ("5".equals(arr.trim())) {
							 * 
							 * if (!StringUtil.isEmpty(fertiJson)) { Type
							 * expenditureType = new
							 * TypeToken<List<PeriodicInspectionData>>()
							 * {}.getType(); List<PeriodicInspectionData>
							 * inspection = new Gson() .fromJson(fertiJson,
							 * expenditureType); for (int i = 0; i <
							 * inspection.size(); i++) { if
							 * (!StringUtil.isEmpty(inspection.get(i).
							 * getCatalogueValue())) { PeriodicInspectionData
							 * dataObj = new PeriodicInspectionData();
							 * FarmCatalogue catvalue =
							 * catalogueService.findCatalogueByName(inspection.
							 * get(i).getCatalogueValue());
							 * if(!ObjectUtil.isEmpty(catvalue)){
							 * dataObj.setCatalogueValue(catvalue.getCode());
							 * }else{
							 * dataObj.setCatalogueValue(inspection.get(i).
							 * getCatalogueValue().split("-")[0].trim()); }
							 * dataObj.setType("MATYP");
							 * dataObj.setOtherCatalogueValueName(!StringUtil.
							 * isEmpty(
							 * inspection.get(i).getOtherCatalogueValueName()) ?
							 * inspection.get(i) .getOtherCatalogueValueName() :
							 * null); dataObj.setQuantityValue(
							 * inspection.get(i).getQuantityValue());
							 * inspectionDataSet.add(dataObj); } }
							 * existing.setPeriodicInspectionData(
							 * inspectionDataSet); } }
							 */
						}

					}
					if (!StringUtil.isEmpty(isPestETL)) {
						existing.setPestInfestationETL(isPestETL.charAt(0));
					}
					if (isPestSolved != null) {
						existing.setPestProblemSolved(isPestSolved.charAt(0));
					}
					if (!StringUtil.isEmpty(isDiseaseNoticed)) {
						existing.setDiseaseProblemNoticed(isDiseaseNoticed.charAt(0));
					}
					if (!StringUtil.isEmpty(isDiseaseETL)) {
						existing.setDiseaseInfestationETL(isDiseaseETL.charAt(0));
					}
					if (isDiseaseSolved != null) {
						existing.setDiseaseProblemSolved(isDiseaseSolved.charAt(0));
					}
					existing.setSurvivalPercentage(periodicInspection.getSurvivalPercentage());
					existing.setCurrentStatusOfGrowth(selectedGrowth);
					existing.setAverageGirth(periodicInspection.getAverageGirth());
					existing.setAverageHeight(periodicInspection.getAverageHeight());
					existing.setChemicalName(chemicalNameString);
					existing.setNoOfPlantsReplanned(periodicInspection.getNoOfPlantsReplanned());
					if (!StringUtil.isEmpty(isSelectedCrop)) {
						existing.setInterCrop(isSelectedCrop.charAt(0));
					}
					existing.setNameOfInterCrop(periodicInspection.getNameOfInterCrop());
					existing.setYieldObtained(periodicInspection.getYieldObtained());
					existing.setExpenditureIncurred(periodicInspection.getExpenditureIncurred());
					existing.setIncomeGenerated(periodicInspection.getIncomeGenerated());
					existing.setNetProfitOrLoss(periodicInspection.getNetProfitOrLoss());
					existing.setLastUpdatedDT(new Date());
					existing.setLastUpdatedUserName(super.getUsername());
					existing.setLandpreparationCompleted(periodicInspection.getLandpreparationCompleted());
					existing.setNitrogenousFert(periodicInspection.getNitrogenousFert());
					existing.setMonoOrImida(periodicInspection.getMonoOrImida());
					existing.setChemicalSpray(periodicInspection.getChemicalSpray());

					existing.setSingleSprayOrCocktail(periodicInspection.getSingleSprayOrCocktail());
					existing.setRepetitionOfPest(periodicInspection.getRepetitionOfPest());
					existing.setWeeding(periodicInspection.getWeeding());
					existing.setPicking(periodicInspection.getPicking());
					existing.setCropSpacingCurrentYear(periodicInspection.getCropSpacingCurrentYear());
					existing.setCropSpacingLastYear(periodicInspection.getCropSpacingLastYear());

					if (!StringUtil.isEmpty(gapPlanting)) {
						existing.setGapPlantingDate(DateUtil.convertStringToDate(gapPlanting, getGeneralDateFormat()));
					}
					// if (getCurrentTenantId().equalsIgnoreCase("lalteer")) {

					existing.setCropRotation(periodicInspection.getCropRotation());
					existing.setTemperature(periodicInspection.getTemperature());
					existing.setRain(periodicInspection.getRain());
					existing.setHumidity(periodicInspection.getHumidity());
					existing.setWindSpeed(periodicInspection.getWindSpeed());

					// }

					if (this.getInspectionAudio() != null) {
						existing.setFarmerVoice(FileUtil.getBinaryFileContent(this.getInspectionAudio()));

					}
					farmerService.editPeriodicInspection(existing);
				}
			} else {
				request.setAttribute(HEADING, getText(DETAIL));
				processError(periodicInspection);
				periodicInspection = clientService.findPeriodicInspectionById(periodicInspection.getId());
				if (!StringUtil.isEmpty(periodicInspection.getInspectionType())) {
					if (periodicInspection.getInspectionType().equals("1")) {
						return INPUT;
					} else {
						return "inputNeed";
					}
				}
			}

			return REDIRECT;

		}
		return super.execute();
	}

	private void processError(PeriodicInspection periodicInspection) {
		SimpleDateFormat sm = new SimpleDateFormat(getGeneralDateFormat());

		periodicInspection = clientService.findPeriodicInspectionById(periodicInspection.getId());

		if (!StringUtil.isEmpty(periodicInspection.getCreatedUserName())) {
			String mobileUser = agentService.findAgentNameByAgentId(periodicInspection.getCreatedUserName());
			if (!StringUtil.isEmpty(mobileUser)) {
				setMobileUserName(mobileUser);
			}
		}

		if (!StringUtil.isEmpty(periodicInspection.getInspectionDate())) {
			periodicInspection.setInspectionDate(periodicInspection.getInspectionDate());
		}
		if (!ObjectUtil.isEmpty(periodicInspection.getFarm())
				&& !ObjectUtil.isEmpty(periodicInspection.getFarm().getFarmer())
				&& !StringUtil.isEmpty(periodicInspection.getFarm().getFarmer().getFarmerCodeAndName())) {
			setFarmerCodeName(periodicInspection.getFarm().getFarmer().getFarmerCodeAndName());

		}

		if (!ObjectUtil.isEmpty(periodicInspection.getFarm())
				&& !ObjectUtil.isEmpty(periodicInspection.getFarm().getFarmer())
				&& !StringUtil.isEmpty(periodicInspection.getFarm().getFarmer().getFarmerIdAndName())) {
			setFarmerIdName(periodicInspection.getFarm().getFarmer().getFarmerIdAndName());

		}

		if (!ObjectUtil.isEmpty(periodicInspection.getFarm())
				&& !StringUtil.isEmpty(periodicInspection.getFarm().getFarmIdAndName())) {
			setFarmIdName(periodicInspection.getFarm().getFarmIdAndName());

		}

		if (!StringUtil.isEmpty(periodicInspection.getInspectionDate())) {
			setInspectionOfDate(String.valueOf(periodicInspection.getInspectionDate()));

		}

		if (!StringUtil.isEmpty(periodicInspection.getCreatedUserName())) {
			setMobileId(periodicInspection.getCreatedUserName());

		}

		if (periodicInspection.getGapPlantingDate() != null) {
			gapPlanting = sm.format(periodicInspection.getGapPlantingDate());

		}

		periodicInspection.setCurrentStatusOfGrowth(getSelectedGrowth());
		periodicInspection.setChemicalName(getChemicalNameString());

		if (!StringUtil.isEmpty(pestJson) && pestJson.equals("[]")) {
			pestNameValue = "";
		} else {
			// Pest Recommented
			Set<PeriodicInspectionData> inspectionDataSet = new HashSet<PeriodicInspectionData>();
			if (!StringUtil.isEmpty(pestJson)) {
				Type expenditureType = new TypeToken<List<PeriodicInspectionData>>() {
				}.getType();
				List<PeriodicInspectionData> inspection = new Gson().fromJson(pestJson, expenditureType);
				for (int i = 0; i < inspection.size(); i++) {
					if (!StringUtil.isEmpty(inspection.get(i).getCatalogueValue())) {
						PeriodicInspectionData dataObj = new PeriodicInspectionData();
						FarmCatalogue catvalue = catalogueService
								.findCatalogueByName(inspection.get(i).getCatalogueValue());
						if (!ObjectUtil.isEmpty(catvalue)) {
							dataObj.setCatalogueValue(catvalue.getCode());
						} else {
							dataObj.setCatalogueValue(inspection.get(i).getCatalogueValue().split("-")[0].trim());
						}
						dataObj.setType("PESTREC");
						dataObj.setOtherCatalogueValueName(
								!StringUtil.isEmpty(inspection.get(i).getOtherCatalogueValueName())
										? inspection.get(i).getOtherCatalogueValueName() : null);
						dataObj.setQuantityValue(inspection.get(i).getQuantityValue());
						inspectionDataSet.add(dataObj);
					}
				}

				periodicInspection.setPeriodicInspectionData(inspectionDataSet);
			}
			farmerService.editPeriodicInspection(periodicInspection);

			pestRecomList = farmerService.listPestRecomentedById(Long.valueOf(periodicInspection.getId()));
			boolean first = true;
			if (!StringUtil.isEmpty(pestRecomList)) {
				for (PeriodicInspectionData data : pestRecomList) {
					FarmCatalogue catValue = catalogueService.findCatalogueByCode(data.getCatalogueValue());
					if (!ObjectUtil.isEmpty(catValue)) {
						String value = catValue.getName();

						if (!first) {
							if (data.getCatalogueValue().equals("99")
									&& !StringUtil.isEmpty(data.getOtherCatalogueValueName())) {
								pestNameValue = pestNameValue + "," + value + ":" + data.getOtherCatalogueValueName();
							} else {
								pestNameValue = pestNameValue + "," + value;
							}
						} else {
							if (data.getCatalogueValue().equals("99")
									&& !StringUtil.isEmpty(data.getOtherCatalogueValueName())) {
								pestNameValue = value + ":" + data.getOtherCatalogueValueName();
							} else {
								pestNameValue = value;
							}
							first = false;
						}
					}

				}

			}

		}

		if (!StringUtil.isEmpty(diseaseJson) && diseaseJson.equals("[]")) {
			fungisideValue = "";
		} else {
			// DiseaseRecom
			Set<PeriodicInspectionData> inspectionDataSet = new HashSet<PeriodicInspectionData>();
			if (!StringUtil.isEmpty(diseaseJson)) {
				Type expenditureType = new TypeToken<List<PeriodicInspectionData>>() {
				}.getType();
				List<PeriodicInspectionData> inspection = new Gson().fromJson(diseaseJson, expenditureType);
				for (int i = 0; i < inspection.size(); i++) {
					if (!StringUtil.isEmpty(inspection.get(i).getCatalogueValue())) {
						PeriodicInspectionData dataObj = new PeriodicInspectionData();
						FarmCatalogue catvalue = catalogueService
								.findCatalogueByName(inspection.get(i).getCatalogueValue());
						if (!ObjectUtil.isEmpty(catvalue)) {
							dataObj.setCatalogueValue(catvalue.getCode());
						} else {
							dataObj.setCatalogueValue(inspection.get(i).getCatalogueValue().split("-")[0].trim());
						}
						dataObj.setType("FUNGISREC");
						dataObj.setOtherCatalogueValueName(
								!StringUtil.isEmpty(inspection.get(i).getOtherCatalogueValueName())
										? inspection.get(i).getOtherCatalogueValueName() : null);
						dataObj.setQuantityValue(inspection.get(i).getQuantityValue());
						inspectionDataSet.add(dataObj);
					}
				}

				periodicInspection.setPeriodicInspectionData(inspectionDataSet);
			}
			farmerService.editPeriodicInspection(periodicInspection);

			fungiList = farmerService.listFungicideById(Long.valueOf(periodicInspection.getId()));

			if (!StringUtil.isEmpty(fungiList)) {
				boolean isTrue = true;
				for (PeriodicInspectionData data : fungiList) {
					FarmCatalogue catValue = catalogueService.findCatalogueByCode(data.getCatalogueValue());
					if (!ObjectUtil.isEmpty(catValue)) {
						String value = catValue.getName();
						if (!isTrue) {
							if (data.getCatalogueValue().equals("99")
									&& !StringUtil.isEmpty(data.getOtherCatalogueValueName())) {
								fungisideValue = fungisideValue + "," + value + ":" + data.getOtherCatalogueValueName();
							} else {
								fungisideValue = fungisideValue + "," + value;
							}
						} else {
							if (data.getCatalogueValue().equals("99")
									&& !StringUtil.isEmpty(data.getOtherCatalogueValueName())) {
								fungisideValue = value + ":" + data.getOtherCatalogueValueName();

							} else {
								fungisideValue = value;
							}

							isTrue = false;
						}
					}

				}

			}

		}

		if (!StringUtil.isEmpty(manureJson) && manureJson.equals("[]")) {
			manureValue = "";
		} else {
			// manure
			Set<PeriodicInspectionData> inspectionDataSet = new HashSet<PeriodicInspectionData>();
			if (!StringUtil.isEmpty(manureJson)) {
				Type expenditureType = new TypeToken<List<PeriodicInspectionData>>() {
				}.getType();
				List<PeriodicInspectionData> inspection = new Gson().fromJson(manureJson, expenditureType);
				for (int i = 0; i < inspection.size(); i++) {
					if (!StringUtil.isEmpty(inspection.get(i).getCatalogueValue())) {
						PeriodicInspectionData dataObj = new PeriodicInspectionData();
						FarmCatalogue catvalue = catalogueService
								.findCatalogueByName(inspection.get(i).getCatalogueValue());
						if (!ObjectUtil.isEmpty(catvalue)) {
							dataObj.setCatalogueValue(catvalue.getCode());
						} else {
							dataObj.setCatalogueValue(inspection.get(i).getCatalogueValue().split("-")[0].trim());
						}
						dataObj.setType("MATYP");
						dataObj.setOtherCatalogueValueName(
								!StringUtil.isEmpty(inspection.get(i).getOtherCatalogueValueName())
										? inspection.get(i).getOtherCatalogueValueName() : null);
						dataObj.setQuantityValue(inspection.get(i).getQuantityValue());
						inspectionDataSet.add(dataObj);
					}
				}

				periodicInspection.setPeriodicInspectionData(inspectionDataSet);
			}
			farmerService.editPeriodicInspection(periodicInspection);

			manureList = farmerService.listManureById(Long.valueOf(periodicInspection.getId()));

			if (!StringUtil.isEmpty(manureList)) {
				boolean isTrue = true;
				for (PeriodicInspectionData data : manureList) {
					FarmCatalogue catValue = catalogueService.findCatalogueByCode(data.getCatalogueValue());
					if (!ObjectUtil.isEmpty(catValue)) {
						String value = catValue.getName();
						if (!isTrue) {
							manureValue = manureValue + "," + value;
						} else {
							manureValue = value;
							isTrue = false;

						}
					}
				}

			}
		}

		if (!StringUtil.isEmpty(fertiJson) && fertiJson.equals("[]")) {
			fertilizerValue = "";
		} else {
			// Fertilizer
			Set<PeriodicInspectionData> inspectionDataSet = new HashSet<PeriodicInspectionData>();
			if (!StringUtil.isEmpty(fertiJson)) {
				Type expenditureType = new TypeToken<List<PeriodicInspectionData>>() {
				}.getType();
				List<PeriodicInspectionData> inspection = new Gson().fromJson(fertiJson, expenditureType);
				for (int i = 0; i < inspection.size(); i++) {
					if (!StringUtil.isEmpty(inspection.get(i).getCatalogueValue())) {
						PeriodicInspectionData dataObj = new PeriodicInspectionData();
						FarmCatalogue catvalue = catalogueService
								.findCatalogueByName(inspection.get(i).getCatalogueValue());
						if (!ObjectUtil.isEmpty(catvalue)) {
							dataObj.setCatalogueValue(catvalue.getCode());
						} else {
							dataObj.setCatalogueValue(inspection.get(i).getCatalogueValue().split("-")[0].trim());
						}
						dataObj.setType("FRTATYP");
						dataObj.setOtherCatalogueValueName(
								!StringUtil.isEmpty(inspection.get(i).getOtherCatalogueValueName())
										? inspection.get(i).getOtherCatalogueValueName() : null);
						dataObj.setQuantityValue(inspection.get(i).getQuantityValue());
						inspectionDataSet.add(dataObj);
					}
				}
				periodicInspection.setPeriodicInspectionData(inspectionDataSet);
			}
			farmerService.editPeriodicInspection(periodicInspection);

			fertilizerList = farmerService.listFertilizerById(Long.valueOf(periodicInspection.getId()));
			if (!StringUtil.isEmpty(fertilizerList)) {
				boolean isTrue = true;
				for (PeriodicInspectionData data : fertilizerList) {
					FarmCatalogue catValue = catalogueService.findCatalogueByCode(data.getCatalogueValue());
					if (!ObjectUtil.isEmpty(catValue)) {
						String value = catValue.getName();
						if (!isTrue) {
							if (data.getCatalogueValue().equals("99")
									&& !StringUtil.isEmpty(data.getOtherCatalogueValueName())) {
								fertilizerValue = fertilizerValue + "," + value + ":"
										+ data.getOtherCatalogueValueName();
							} else {
								fertilizerValue = fertilizerValue + "," + value;
							}
						} else {
							if (data.getCatalogueValue().equals("99")
									&& !StringUtil.isEmpty(data.getOtherCatalogueValueName())) {
								fertilizerValue = value + ":" + data.getOtherCatalogueValueName();
							} else {
								fertilizerValue = value;
							}
							isTrue = false;

						}
					}
				}

			}
		}

		if (StringUtil.isEmpty(selectedPestName)) {
			selectedPestName = "";
		} else {
			if (!ObjectUtil.isEmpty(periodicInspection.getPeriodicInspectionData())) {

				for (PeriodicInspectionData data : periodicInspection.getPeriodicInspectionData()) {
					// Pest Name
					if (data.getType().equalsIgnoreCase("PEST")) {
						boolean isTrue = true;
						String value = "PEST";
						List<PeriodicInspectionData> inspectionData = farmerService.findPeriodicDataByType(value,
								periodicInspection.getId());
						for (PeriodicInspectionData periodicInsData : inspectionData) {
							String pestNameValue = periodicInsData.getCatalogueValue();
							if (!isTrue) {
								setSelectedPestName(selectedPestName + "," + pestNameValue);
							} else {
								setSelectedPestName(pestNameValue);
								isTrue = false;
							}

							if ("99".equals(selectedPestName)) {
								setSelectedOtherPest(periodicInsData.getOtherCatalogueValueName());

							}
						}

					}
				}
			}

		}

		if (StringUtil.isEmpty(selectedPestSymptom)) {
			selectedPestSymptom = "";
		} else {
			if (!ObjectUtil.isEmpty(periodicInspection.getPeriodicInspectionSymptoms())) {
				for (PeriodicInspectionSymptom symptoms : periodicInspection.getPeriodicInspectionSymptoms()) {
					// Pest Symptoms
					if ("0".equalsIgnoreCase(symptoms.getType())) {
						boolean isTrue = true;
						String value = "0";
						List<PeriodicInspectionSymptom> symData = farmerService.findPeriodicSymptomsByType(value,
								periodicInspection.getId());
						for (PeriodicInspectionSymptom symptomsData : symData) {
							String sympValue = symptomsData.getSymCode();
							if (!isTrue) {
								setSelectedPestSymptom(selectedPestSymptom + "," + sympValue);
							} else {
								setSelectedPestSymptom(sympValue);
								isTrue = false;
							}

						}
					}
				}
			}
		}

		if (StringUtil.isEmpty(selectedDiseaseName)) {
			selectedDiseaseName = "";
		} else {
			if (!ObjectUtil.isEmpty(periodicInspection.getPeriodicInspectionData())) {

				for (PeriodicInspectionData data : periodicInspection.getPeriodicInspectionData()) {

					// Disease Name
					if (data.getType().equalsIgnoreCase("DISEASE")) {
						boolean isTrue = true;
						String value = "DISEASE";
						List<PeriodicInspectionData> inspectionData = farmerService.findPeriodicDataByType(value,
								periodicInspection.getId());
						for (PeriodicInspectionData periodicInsData : inspectionData) {
							String diseaseValue = periodicInsData.getCatalogueValue();
							if (!isTrue) {
								setSelectedDiseaseName(selectedDiseaseName + "," + diseaseValue);
							} else {
								setSelectedDiseaseName(diseaseValue);
								isTrue = false;
							}

							if ("99".equals(selectedDiseaseName)) {
								setSelectedOtherDisease(periodicInsData.getOtherCatalogueValueName());

							}
						}
					}
				}
			}
		}

		if (StringUtil.isEmpty(selectedDiseaseSymptom)) {
			selectedDiseaseSymptom = "";
		} else {
			if (!ObjectUtil.isEmpty(periodicInspection.getPeriodicInspectionSymptoms())) {
				for (PeriodicInspectionSymptom symptoms : periodicInspection.getPeriodicInspectionSymptoms()) {
					// Disease Symptoms
					if ("1".equalsIgnoreCase(symptoms.getType())) {
						boolean isTrue = true;
						String value = "1";
						List<PeriodicInspectionSymptom> symData = farmerService.findPeriodicSymptomsByType(value,
								periodicInspection.getId());
						for (PeriodicInspectionSymptom symptomsData : symData) {
							String sympValue = symptomsData.getSymCode();
							if (!isTrue) {
								setSelectedDiseaseSymptom(selectedDiseaseSymptom + "," + sympValue);
							} else {
								setSelectedDiseaseSymptom(sympValue);
								isTrue = false;
							}

						}
					}
				}
			}
		}

		if (!ObjectUtil.isEmpty(periodicInspection.getPeriodicInspectionData())) {

			for (PeriodicInspectionData data : periodicInspection.getPeriodicInspectionData()) {

				// ActivitiesCarriedOut
				if (data.getType().equalsIgnoreCase("ACAPV")) {
					boolean isTrue = true;
					String value = "ACAPV";
					List<PeriodicInspectionData> inspectionData = farmerService.findPeriodicDataByType(value,
							periodicInspection.getId());
					for (PeriodicInspectionData periodicInsData : inspectionData) {
						String acapvValue = periodicInsData.getCatalogueValue();
						if (!isTrue) {
							setSelectedActivities(selectedActivities + "," + acapvValue);
						} else {
							setSelectedActivities(acapvValue);
							isTrue = false;
						}
					}
				}
				// Interploughing

				if (!StringUtil.isEmpty(selectedInterPloughing)) {
					if (data.getType().equalsIgnoreCase("INTPLUG")) {
						boolean isTrue = true;
						String value = "INTPLUG";
						List<PeriodicInspectionData> inspectionData = farmerService.findPeriodicDataByType(value,
								periodicInspection.getId());
						for (PeriodicInspectionData periodicInsData : inspectionData) {
							String ploughValue = periodicInsData.getCatalogueValue();
							if (!isTrue) {
								setSelectedInterPloughing(selectedInterPloughing + "," + ploughValue);
							} else {
								setSelectedInterPloughing(ploughValue);
								isTrue = false;
							}
						}
					}
				} else {
					setSelectedInterPloughing("");
				}

			}

		}

		// periodicInspection.setInterCrop(isSelectedCrop.charAt(0));

		setCurrentPage(getCurrentPage());
		id = null;
		command = UPDATE;
		request.setAttribute(HEADING, getText(UPDATE));

	}

	public Map<String, String> getRadioValueList() {

		Map<String, String> radioValueList = new HashMap<>();
		radioValueList.put("Y", getText("PESTRECY"));
		radioValueList.put("N", getText("PESTRECN"));
		return radioValueList;
	}

	public Map<String, String> getPestMap() {
		Map<String, String> pestMap = new HashMap<>();
		List<FarmCatalogue> catList = catalogueService.findFarmCatalougeByAlpha(Integer.valueOf(getText("pestName")));
		for (FarmCatalogue catalogue : catList) {
			pestMap.put(catalogue.getCode(), catalogue.getName());
		}
		return pestMap;
	}

	public Map<String, String> getPestSymptomsMap() {
		Map<String, String> pestSymptomsMap = new HashMap<>();
		List<FarmCatalogue> catList = catalogueService
				.findFarmCatalougeByAlpha(Integer.valueOf(getText("pestSymptoms")));
		for (FarmCatalogue catalogue : catList) {
			pestSymptomsMap.put(catalogue.getCode(), catalogue.getName());
		}
		return pestSymptomsMap;
	}

	public Map<String, String> getDiseaseSymptomsMap() {
		Map<String, String> diseaseSymptomsMap = new HashMap<>();
		List<FarmCatalogue> catList = catalogueService
				.findFarmCatalougeByAlpha(Integer.valueOf(getText("symptomDisease")));
		for (FarmCatalogue catalogue : catList) {
			diseaseSymptomsMap.put(catalogue.getCode(), catalogue.getName());
		}
		return diseaseSymptomsMap;
	}

	public Map<String, String> getDiseaseMap() {
		Map<String, String> diseaseMap = new HashMap<>();
		List<FarmCatalogue> catList = catalogueService
				.findFarmCatalougeByAlpha(Integer.valueOf(getText("diseaseNameType")));
		for (FarmCatalogue catalogue : catList) {
			diseaseMap.put(catalogue.getCode(), catalogue.getName());
		}
		return diseaseMap;
	}

	public Map<String, String> getCropList() {
		Map<String, String> cropList = new HashMap<>();
		List<FarmCatalogue> catList = catalogueService
				.findFarmCatalougeByAlpha(Integer.valueOf(getText("inspectionCrop")));
		for (FarmCatalogue catalogue : catList) {
			cropList.put(catalogue.getCode(), catalogue.getName());
		}
		return cropList;
	}

	public Map<String, String> getGrowthList() {
		Map<String, String> growthList = new HashMap<>();
		List<FarmCatalogue> catList = catalogueService.findFarmCatalougeByAlpha(Integer.valueOf(getText("cropGrowth")));
		for (FarmCatalogue catalogue : catList) {
			growthList.put(catalogue.getCode(), catalogue.getName());
		}
		return growthList;
	}

	public Map<String, String> getActivitiesCarriedList() {
		Map<String, String> activitiesCarriedList = new HashMap<>();

		activitiesCarriedList.put("1", getText("ACAPV1"));
		activitiesCarriedList.put("2", getText("ACAPV2"));
		activitiesCarriedList.put("3", getText("ACAPV3"));
		activitiesCarriedList.put("4", getText("ACAPV4"));
		activitiesCarriedList.put("5", getText("ACAPV5"));
		activitiesCarriedList.put("6", getText("ACAPV6"));
		activitiesCarriedList.put("7", getText("ACAPV7"));
		activitiesCarriedList.put("8", getText("ACAPV8"));
		activitiesCarriedList.put("9", getText("ACAPV9"));

		return activitiesCarriedList;
	}

	public Map<String, String> getFertilizerType() {
		Map<String, String> fertilizerType = new HashMap<>();
		List<FarmCatalogue> catList = catalogueService
				.findFarmCatalougeByAlpha(Integer.valueOf(getText("fertlizerTypeT")));
		for (FarmCatalogue catalogue : catList) {
			fertilizerType.put(catalogue.getCode(), catalogue.getName());
		}
		return fertilizerType;
	}

	public Map<String, String> getInterploughingType() {
		Map<String, String> interploughingType = new HashMap<>();
		List<FarmCatalogue> catList = catalogueService
				.findFarmCatalougeByAlpha(Integer.valueOf(getText("interploughType")));
		for (FarmCatalogue catalogue : catList) {
			interploughingType.put(catalogue.getCode(), catalogue.getName());
		}
		return interploughingType;
	}

	public Map<String, String> getPestRecMap() {
		Map<String, String> pestRecMap = new HashMap<>();
		List<FarmCatalogue> catList = catalogueService.findFarmCatalougeByAlpha(Integer.valueOf(getText("pesticideType")));
		for (FarmCatalogue catalogue : catList) {
			pestRecMap.put(catalogue.getCode(), catalogue.getName());
		}

		return pestRecMap;
	}

	public Map<String, String> getFungRecMap() {
		Map<String, String> fungRecMap = new HashMap<>();
		List<FarmCatalogue> catList = catalogueService.findFarmCatalougeByAlpha(Integer.valueOf(getText("fungicide")));
		for (FarmCatalogue catalogue : catList) {
			fungRecMap.put(catalogue.getCode(), catalogue.getName());
		}

		return fungRecMap;
	}

	public Map<String, String> getManureMap() {
		Map<String, String> manureMap = new HashMap<>();

		List<FarmCatalogue> catList = catalogueService.findFarmCatalougeByAlpha(Integer.valueOf(getText("manure")));
		for (FarmCatalogue catalogue : catList) {
			manureMap.put(catalogue.getCode(), catalogue.getName());
		}

		return manureMap;
	}

	public String delete() throws Exception {

		if (this.getId() != null && !(this.getId().equals(EMPTY))) {
			periodicInspection = clientService.findPeriodicInspectionById(Long.valueOf(id));
			if (periodicInspection == null) {
				addActionError(NO_RECORD);
				return list();
			}
			setCurrentPage(getCurrentPage());
			if (periodicInspection != null) {
				periodicInspection.setIsDeleted("1");
				farmerService.editPeriodicInspection(periodicInspection);
			}
		}

		return REDIRECT;
	}

	public String validateInspectionData() {

		String hit = "true";

		if (StringUtil.isEmpty(selectedGrowth)) {
			addActionError(getText("empty.warnGrowth"));
			hit = "false";
		}

		if (!StringUtil.isEmpty(fertiJson) && fertiJson.equals("[]")) {
			addActionError(getText("empty.fertiList"));
			hit = "false";

		}

		if (!StringUtil.isEmpty(isPestNoticed) && "Y".equalsIgnoreCase(isPestNoticed)
				&& StringUtil.isEmpty(selectedPestName)) {
			addActionError(getText("empty.pestName"));
			hit = "false";

		}

		if (!getCurrentTenantId().equalsIgnoreCase("efk")) {
			if ("Y".equalsIgnoreCase(isPestNoticed) && "Y".equalsIgnoreCase(isPestETL) && !StringUtil.isEmpty(pestJson)
					&& pestJson.equals("[]")) {
				addActionError(getText("empty.pestRecSet"));
				hit = "false";
			}
		}
		if ("Y".equalsIgnoreCase(isDiseaseNoticed) && StringUtil.isEmpty(selectedDiseaseName)) {
			addActionError(getText("empty.diseaseName"));
			hit = "false";

		}

		if ("Y".equalsIgnoreCase(isDiseaseNoticed) && "Y".equalsIgnoreCase(isDiseaseETL)
				&& !StringUtil.isEmpty(diseaseJson) && diseaseJson.equals("[]")) {
			addActionError(getText("empty.fungiSet"));
			hit = "false";
		}

		if (!StringUtil.isEmpty(isSelectedCrop) && "Y".equalsIgnoreCase(isSelectedCrop)
				&& StringUtil.isEmpty(periodicInspection.getNameOfInterCrop())) {
			addActionError(getText("empty.cropName"));
			hit = "false";

		}

		if (!StringUtil.isEmpty(selectedActivities)) {
			if (selectedActivities.contains(",")) {
				String[] arr = selectedActivities.split(",");
				for (String dataArr : arr) {
					if ("1".equalsIgnoreCase(dataArr.trim()) && StringUtil.isEmpty(gapPlanting)) {
						addActionError(getText("empty.gapPlantDate"));
						hit = "false";
					}
					if ("2".equalsIgnoreCase(dataArr.trim()) && StringUtil.isEmpty(selectedInterPloughing)) {
						addActionError(getText("empty.interplough"));
						hit = "false";
					}
					if ("8".equalsIgnoreCase(dataArr.trim()) && StringUtil.isEmpty(getChemicalNameString())) {
						addActionError(getText("empty.cheName"));
						hit = "false";
					}
					if ("5".equalsIgnoreCase(dataArr.trim()) && !StringUtil.isEmpty(manureJson)
							&& manureJson.equals("[]")) {
						addActionError(getText("empty.manure"));
						hit = "false";
					}

				}
			} else {
				if ("1".equalsIgnoreCase(selectedActivities) && StringUtil.isEmpty(gapPlanting)) {
					addActionError(getText("empty.gapPlantDate"));
					hit = "false";

				}
				if ("2".equalsIgnoreCase(selectedActivities) && StringUtil.isEmpty(selectedInterPloughing)) {
					addActionError(getText("empty.interplough"));
					hit = "false";

				}
				if ("8".equalsIgnoreCase(selectedActivities) && StringUtil.isEmpty(getChemicalNameString())) {
					addActionError(getText("empty.cheName"));
					hit = "false";
				}
				if ("5".equalsIgnoreCase(selectedActivities) && !StringUtil.isEmpty(manureJson)
						&& manureJson.equals("[]")) {
					addActionError(getText("empty.manure"));
					hit = "false";
				}

			}

		}

		return hit;
	}

	public String updateNeed() throws Exception {

		if (id != null && !id.equals("")) {
			periodicInspection = clientService.findPeriodicInspectionById(Long.valueOf(id));
			if (!StringUtil.isEmpty(periodicInspection.getCreatedUserName())) {
				String mobileUser = agentService.findAgentNameByAgentId(periodicInspection.getCreatedUserName());
				if (!StringUtil.isEmpty(mobileUser)) {
					setMobileUserName(mobileUser);
				}
			}
			if (periodicInspection.getInspectionImageInfo() != null
					&& periodicInspection.getInspectionImageInfo().getInspectionImages() != null
					&& periodicInspection.getInspectionImageInfo().getInspectionImages().size() > 0) {
				for (InspectionImage immp : periodicInspection.getInspectionImageInfo().getInspectionImages()) {
					if (immp.getPhoto() != null)
						immp.setImageByteString(Base64Util.encoder(immp.getPhoto()));
				}
				periodicInspectionImageList = new ArrayList(
						periodicInspection.getInspectionImageInfo().getInspectionImages());
				if (periodicInspectionImageList.size() < 3) {
					for (int i = periodicInspectionImageList.size(); i < 3; i++) {
						InspectionImage imp = new InspectionImage();
						imp.setImageByteString("");
						periodicInspectionImageList.add(i, imp);
					}
				}
			} else {
				for (int i = 0; i < 3; i++) {
					InspectionImage imp = new InspectionImage();
					imp.setImageByteString("");
					periodicInspectionImageList.add(i, imp);
				}
			}
			if (periodicInspection.getFarmerVoice() != null) {
				setAudioByteString(Base64Util.encoder(periodicInspection.getFarmerVoice()));
			}
			if (periodicInspection == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			setCurrentPage(getCurrentPage());
			id = null;
			command = UPDATE;
			request.setAttribute(HEADING, getText(UPDATE));
		} else {
			if (StringUtil.isEmpty(periodicInspection.getPurpose())) {
				addActionError(getText("empty.purpose"));

				request.setAttribute(HEADING, getText(DETAIL));
				periodicInspection = clientService.findPeriodicInspectionById(periodicInspection.getId());
				if (!StringUtil.isEmpty(periodicInspection.getCreatedUserName())) {
					String mobileUser = agentService.findAgentNameByAgentId(periodicInspection.getCreatedUserName());
					if (!StringUtil.isEmpty(mobileUser)) {
						setMobileUserName(mobileUser);
					}
				}
				periodicInspection.setPurpose("");
				if (!ObjectUtil.isEmpty(periodicInspection.getFarm())
						&& !ObjectUtil.isEmpty(periodicInspection.getFarm().getFarmer())
						&& !StringUtil.isEmpty(periodicInspection.getFarm().getFarmer().getFarmerCodeAndName())) {
					setFarmerCodeName(periodicInspection.getFarm().getFarmer().getFarmerCodeAndName());

				}

				if (!ObjectUtil.isEmpty(periodicInspection.getFarm())
						&& !ObjectUtil.isEmpty(periodicInspection.getFarm().getFarmer())
						&& !StringUtil.isEmpty(periodicInspection.getFarm().getFarmer().getFarmerIdAndName())) {
					setFarmerIdName(periodicInspection.getFarm().getFarmer().getFarmerIdAndName());

				}

				if (!ObjectUtil.isEmpty(periodicInspection.getFarm())
						&& !StringUtil.isEmpty(periodicInspection.getFarm().getFarmIdAndName())) {
					setFarmIdName(periodicInspection.getFarm().getFarmIdAndName());

				}

				if (!StringUtil.isEmpty(periodicInspection.getInspectionDate())) {
					setInspectionOfDate(String.valueOf(periodicInspection.getInspectionDate()));

				}
				if (periodicInspection.getInspectionType().equals("1")) {
					return INPUT;
				} else {
					return "inputNeed";
				}
			} else {

				if (periodicInspection != null) {
					PeriodicInspection existing = clientService.findPeriodicInspectionById(periodicInspection.getId());
					if (existing == null) {
						addActionError(NO_RECORD);
						return REDIRECT;
					}
					setCurrentPage(getCurrentPage());
					existing.setPurpose(periodicInspection.getPurpose());
					existing.setIsFertilizerApplied(periodicInspection.getIsFertilizerApplied());
					existing.setIsFieldSafetyProposal(periodicInspection.getIsFieldSafetyProposal());
					existing.setRemarks(periodicInspection.getRemarks());
					for (InspectionImage img : periodicInspectionImageList) {
						if (img.getImageFile() != null) {
						} else if (img.getImageByteString() != null) {
						}
					}
					/*
					 * if(!StringUtil.isEmpty(existing.getInspectionImageInfo().
					 * getId())){if (this.getInspectionImgae() != null)
					 * {photo=clientService.findInspectionImageByImgInfoId(
					 * existing.
					 * getInspectionImageInfo().getId());if(!StringUtil.isEmpty(
					 * photo)){photo.setPhoto(
					 * FileUtil.getBinaryFileContent(this.getInspectionImgae()))
					 * ;Set insImg=new
					 * HashSet();insImg.add(photo);inspectionImageInfo=
					 * clientService. findInspectionImageInfoById(existing.
					 * getInspectionImageInfo().getId());
					 * inspectionImageInfo.setInspectionImages(insImg);existing.
					 * setInspectionImageInfo(
					 * inspectionImageInfo);}else{InspectionImageInfo
					 * insImgInfo=new InspectionImageInfo();Set insImg=new
					 * HashSet();InspectionImage img=new
					 * InspectionImage();img.setPhoto(FileUtil.
					 * getBinaryFileContent(this.
					 * getInspectionImgae()));insImg.add(img);insImgInfo.
					 * setInspectionImages(insImg);
					 * existing.setInspectionImageInfo(insImgInfo);
					 * setInspectionImgae(getInspectionImgae ());}}}
					 */
					if (this.getInspectionAudio() != null) {
						existing.setFarmerVoice(FileUtil.getBinaryFileContent(this.getInspectionAudio()));

					}
					farmerService.editPeriodicInspection(existing);
				}
			}
			return REDIRECT;
		}

		request.setAttribute(HEADING, getText(INPUT));
		if (periodicInspection.getInspectionType().equals("1")) {
			return INPUT;
		} else {
			return "inputNeed";
		}

	}

	public List<PeriodicInspectionData> getPestRecomList() {

		return pestRecomList;
	}

	public void setPestRecomList(List<PeriodicInspectionData> pestRecomList) {

		this.pestRecomList = pestRecomList;
	}

	public List<PeriodicInspectionData> getFungiList() {

		return fungiList;
	}

	public void setFungiList(List<PeriodicInspectionData> fungiList) {

		this.fungiList = fungiList;
	}

	public List<PeriodicInspectionData> getManureList() {

		return manureList;
	}

	public void setManureList(List<PeriodicInspectionData> manureList) {

		this.manureList = manureList;
	}

	public List<PeriodicInspectionData> getFertilizerList() {

		return fertilizerList;
	}

	public void setFertilizerList(List<PeriodicInspectionData> fertilizerList) {

		this.fertilizerList = fertilizerList;
	}

	public String getPestNameValue() {

		return pestNameValue;
	}

	public void setPestNameValue(String pestNameValue) {

		this.pestNameValue = pestNameValue;
	}

	public String getFungisideValue() {

		return fungisideValue;
	}

	public void setFungisideValue(String fungisideValue) {

		this.fungisideValue = fungisideValue;
	}

	public String getManureValue() {

		return manureValue;
	}

	public void setManureValue(String manureValue) {

		this.manureValue = manureValue;
	}

	public String getFertilizerValue() {

		return fertilizerValue;
	}

	public void setFertilizerValue(String fertilizerValue) {

		this.fertilizerValue = fertilizerValue;
	}

	public String getSelectedPestName() {

		return selectedPestName;
	}

	public void setSelectedPestName(String selectedPestName) {

		this.selectedPestName = selectedPestName;
	}

	public String getSelectedPestSymptom() {

		return selectedPestSymptom;
	}

	public void setSelectedPestSymptom(String selectedPestSymptom) {

		this.selectedPestSymptom = selectedPestSymptom;
	}

	public String getSelectedDiseaseName() {

		return selectedDiseaseName;
	}

	public void setSelectedDiseaseName(String selectedDiseaseName) {

		this.selectedDiseaseName = selectedDiseaseName;
	}

	public String getSelectedDiseaseSymptom() {

		return selectedDiseaseSymptom;
	}

	public void setSelectedDiseaseSymptom(String selectedDiseaseSymptom) {

		this.selectedDiseaseSymptom = selectedDiseaseSymptom;
	}

	public String getSelectedActivities() {

		return selectedActivities;
	}

	public void setSelectedActivities(String selectedActivities) {

		this.selectedActivities = selectedActivities;
	}

	public String getSelectedInterPloughing() {

		return selectedInterPloughing;
	}

	public void setSelectedInterPloughing(String selectedInterPloughing) {

		this.selectedInterPloughing = selectedInterPloughing;
	}

	public String getGapPlanting() {

		return gapPlanting;
	}

	public void setGapPlanting(String gapPlanting) {

		this.gapPlanting = gapPlanting;
	}

	public String getSelectedOtherPest() {

		return selectedOtherPest;
	}

	public void setSelectedOtherPest(String selectedOtherPest) {

		this.selectedOtherPest = selectedOtherPest;
	}

	public String getSelectedOtherDisease() {

		return selectedOtherDisease;
	}

	public void setSelectedOtherDisease(String selectedOtherDisease) {

		this.selectedOtherDisease = selectedOtherDisease;
	}

	public String getPestJson() {

		return pestJson;
	}

	public void setPestJson(String pestJson) {

		this.pestJson = pestJson;
	}

	public String getDiseaseJson() {

		return diseaseJson;
	}

	public void setDiseaseJson(String diseaseJson) {

		this.diseaseJson = diseaseJson;
	}

	public String getFertiJson() {

		return fertiJson;
	}

	public void setFertiJson(String fertiJson) {

		this.fertiJson = fertiJson;
	}

	public String getManureJson() {

		return manureJson;
	}

	public void setManureJson(String manureJson) {

		this.manureJson = manureJson;
	}

	public String getFarmerCodeName() {

		return farmerCodeName;
	}

	public void setFarmerCodeName(String farmerCodeName) {

		this.farmerCodeName = farmerCodeName;
	}

	public String getFarmIdName() {

		return farmIdName;
	}

	public void setFarmIdName(String farmIdName) {

		this.farmIdName = farmIdName;
	}

	public String getFarmerIdName() {

		return farmerIdName;
	}

	public void setFarmerIdName(String farmerIdName) {

		this.farmerIdName = farmerIdName;
	}

	public String getInspectionOfDate() {

		return inspectionOfDate;
	}

	public void setInspectionOfDate(String inspectionOfDate) {

		this.inspectionOfDate = inspectionOfDate;
	}

	public String getSelActiviesValue() {

		return selActiviesValue;
	}

	public void setSelActiviesValue(String selActiviesValue) {

		this.selActiviesValue = selActiviesValue;
	}

	public File getInspectImg1() {

		return inspectImg1;
	}

	public void setInspectImg1(File inspectImg1) {

		this.inspectImg1 = inspectImg1;
	}

	public File getInspectImg2() {

		return inspectImg2;
	}

	public void setInspectImg2(File inspectImg2) {

		this.inspectImg2 = inspectImg2;
	}

	public File getInspectImg3() {

		return inspectImg3;
	}

	public void setInspectImg3(File inspectImg3) {

		this.inspectImg3 = inspectImg3;
	}

	public InspectionImage getPhoto() {

		return photo;
	}

	public void setPhoto(InspectionImage photo) {

		this.photo = photo;
	}

	public InspectionImageInfo getInspectionImageInfo() {

		return inspectionImageInfo;
	}

	public void setInspectionImageInfo(InspectionImageInfo inspectionImageInfo) {

		this.inspectionImageInfo = inspectionImageInfo;
	}

	public String getMobileId() {

		return mobileId;
	}

	public void setMobileId(String mobileId) {

		this.mobileId = mobileId;
	}

	public ICatalogueService getCatalogueService() {

		return catalogueService;
	}

	public void setCatalogueService(ICatalogueService catalogueService) {

		this.catalogueService = catalogueService;
	}

	public void populatePestSym() throws Exception {

		if ((!StringUtil.isEmpty(selectedPestVal))) {
			String[] pestArr = selectedPestVal.split(",");
			for (String arr : pestArr) {
				if (!"99".equals(arr)) {
					FarmCatalogue cat = catalogueService.findCatalogueByCode(arr);
					if (!ObjectUtil.isEmpty(cat)) {
						pestSym = catalogueService.findSymptomsByCode(String.valueOf(cat.getCode()));
					}
				}
			}

			JSONArray arr = new JSONArray();
			if (!ObjectUtil.isEmpty(pestSym)) {
				for (FarmCatalogue farmCat : pestSym) {
					arr.add(getJSONObject(farmCat.getCode(), farmCat.getName()));
				}
			}
			sendAjaxResponse(arr);
		} else {
			String result = "0";
			sendAjaxResponse(result);
		}
	}

	public void populateDiseSym() throws Exception {

		if (!StringUtil.isEmpty(selectedDiseaseVal)) {
			String[] pestArr = selectedDiseaseVal.split(",");
			for (String arr : pestArr) {
				if (!"99".equals(arr)) {
					FarmCatalogue cat = catalogueService.findCatalogueByCode(arr);
					if (!ObjectUtil.isEmpty(cat)) {
						diseaseSym = catalogueService.findSymptomsByCode(String.valueOf(cat.getCode()));
					}
				}
			}

			JSONArray arr = new JSONArray();
			if (!ObjectUtil.isEmpty(diseaseSym)) {
				for (FarmCatalogue farmCat : diseaseSym) {
					arr.add(getJSONObject(farmCat.getCode(), farmCat.getName()));
				}
			}
			sendAjaxResponse(arr);
		} else {
			String result = "0";
			sendAjaxResponse(result);
		}
	}

	public String getSelectedPestVal() {

		return selectedPestVal;
	}

	public void setSelectedPestVal(String selectedPestVal) {

		this.selectedPestVal = selectedPestVal;
	}

	public String getSelectedDiseaseVal() {

		return selectedDiseaseVal;
	}

	public void setSelectedDiseaseVal(String selectedDiseaseVal) {

		this.selectedDiseaseVal = selectedDiseaseVal;
	}

	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}

	public Map<String, String> getWeedingMap() {
		Map<String, String> pestMap = new HashMap<>();

		List<FarmCatalogue> catList = catalogueService.findFarmCatalougeByAlpha(Integer.valueOf(getText("weedingType")));
		for (FarmCatalogue catalogue : catList) {
			pestMap.put(catalogue.getCode(), catalogue.getName());
		}

		return pestMap;
	}

	public Map<String, String> getPickMap() {
		Map<String, String> pestMap = new HashMap<>();
		List<FarmCatalogue> catList = catalogueService.findFarmCatalougeByAlpha(Integer.valueOf(getText("picking")));
		for (FarmCatalogue catalogue : catList) {
			pestMap.put(catalogue.getCode(), catalogue.getName());
		}

		return pestMap;
	}

	public Map<String, String> getRadioVal() {

		Map<String, String> radioValue = new HashMap<>();
		radioValue.put("1", getText("PESTRECY"));
		radioValue.put("2", getText("PESTRECN"));
		return radioValue;
	}

	@Override
	public String getCurrentMenu() {
		String type = request.getParameter("type");
		if (!StringUtil.isEmpty(type)) {
			if (type.equalsIgnoreCase("service")) {
				return getText("menu1.select");
			}
		}
		return getText("menu.select");

	}

	public String getType() {

		return type;
	}

	public void setType(String type) {

		this.type = type;
	}

	public void prepare() throws Exception {

		type = request.getParameter("type");
		if (!StringUtil.isEmpty(type)) {
			String actionClassName = ActionContext.getContext().getActionInvocation().getAction().getClass()
					.getSimpleName();
			String content = getLocaleProperty(actionClassName + "." + BreadCrumb.BREADCRUMB+type);
			if (StringUtil.isEmpty(content) || (content.equalsIgnoreCase(actionClassName + "." + BreadCrumb.BREADCRUMB+type))) {
				content = super.getText(BreadCrumb.BREADCRUMB+type, "");
			}
			request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(content));
			
		}else{
			String actionClassName = ActionContext.getContext().getActionInvocation().getAction().getClass()
					.getSimpleName();
			String content = getLocaleProperty(actionClassName + "." + BreadCrumb.BREADCRUMB);
			if (StringUtil.isEmpty(content) || (content.equalsIgnoreCase(actionClassName + "." + BreadCrumb.BREADCRUMB))) {
				content = super.getText(BreadCrumb.BREADCRUMB, "");
			}
			request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(content));
		}
	}

	public File getInspectionAudio() {
		return inspectionAudio;
	}

	public void setInspectionAudio(File inspectionAudio) {
		this.inspectionAudio = inspectionAudio;
	}

	public String getAudioByteString() {
		return audioByteString;
	}

	public void setAudioByteString(String audioByteString) {
		this.audioByteString = audioByteString;
	}

	public String getOtherValue() {
		return otherValue;
	}

	public void setOtherValue(String otherValue) {
		this.otherValue = otherValue;
	}

	public String getSelectedGrowth() {

		return selectedGrowth;
	}

	public void setSelectedGrowth(String selectedGrowth) {

		this.selectedGrowth = selectedGrowth;
	}

	public Map<Integer, String> getIrrigationSourceList() {
		Map<Integer, String> irrigationSourceList = new LinkedHashMap<Integer, String>();
		if (irrigationSourceList.size() == 0) {
			irrigationSourceList = getPropertyData("farmIrrigationList");
			return irrigationSourceList;
		}
		return irrigationSourceList;
	}

	public Map<String, String> getPropertyMap(String text) {

		Map<String, String> propertyDataMap = null;
		String values = getLocaleProperty(text);
		if (!StringUtil.isEmpty(values)) {
			propertyDataMap = new LinkedHashMap();
			String[] valuesArray = values.split(",");
			for (String value : valuesArray) {
				String[] data = value.trim().split("\\~");
				propertyDataMap.put(data[0], data[1]);
			}
		}
		return propertyDataMap;

	}

	public String getIsSelectedCrop() {

		return isSelectedCrop;
	}

	public void setIsSelectedCrop(String isSelectedCrop) {

		this.isSelectedCrop = isSelectedCrop;
	}

	public String getIsPestNoticed() {

		return isPestNoticed;
	}

	public void setIsPestNoticed(String isPestNoticed) {

		this.isPestNoticed = isPestNoticed;
	}

	public String getIsPestETL() {

		return isPestETL;
	}

	public void setIsPestETL(String isPestETL) {

		this.isPestETL = isPestETL;
	}

	public String getIsPestSolved() {

		return isPestSolved;
	}

	public void setIsPestSolved(String isPestSolved) {

		this.isPestSolved = isPestSolved;
	}

	public String getIsDiseaseETL() {

		return isDiseaseETL;
	}

	public void setIsDiseaseETL(String isDiseaseETL) {

		this.isDiseaseETL = isDiseaseETL;
	}

	public String getIsDiseaseNoticed() {

		return isDiseaseNoticed;
	}

	public void setIsDiseaseNoticed(String isDiseaseNoticed) {

		this.isDiseaseNoticed = isDiseaseNoticed;
	}

	public String getIsDiseaseSolved() {

		return isDiseaseSolved;
	}

	public void setIsDiseaseSolved(String isDiseaseSolved) {

		this.isDiseaseSolved = isDiseaseSolved;
	}

	public String getFarmIrrigationDetail() {
		return farmIrrigationDetail;
	}

	public void setFarmIrrigationDetail(String farmIrrigationDetail) {
		this.farmIrrigationDetail = farmIrrigationDetail;
	}

	public Map<Integer, String> getFarmIrrigationList() {

		if (farmIrrigationList.size() == 0) {
			String[] farmIrrigation = getText("farmIrrigationList").split(",");
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

	/*
	 * public Map<String, String> getIcsList() {
	 * 
	 * Map<String, String> icsList = new LinkedHashMap<String, String>();
	 * 
	 * List<Object[]> ics = catalogueService.loadICSName(); for (Object[] obj :
	 * ics) { icsList.put(String.valueOf(obj[0]), String.valueOf(obj[1])); }
	 * return icsList;
	 * 
	 * }
	 */

	public void populateIcsList() {
		JSONArray icsArr = new JSONArray();
		List<Object[]> icsList = catalogueService.loadICSName();
		if (!ObjectUtil.isEmpty(icsList)) {
			icsList.forEach(obj -> {
				icsArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(icsArr);
	}

	public void setFarmIrrigationList(Map<Integer, String> farmIrrigationList) {
		this.farmIrrigationList = farmIrrigationList;
	}

	public String getInspectDate() {
		return inspectDate;
	}

	public void setInspectDate(String inspectDate) {
		this.inspectDate = inspectDate;
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

	public List<FarmCatalogue> getPestSym() {

		return pestSym;
	}

	public void setPestSym(List<FarmCatalogue> pestSym) {

		this.pestSym = pestSym;
	}

	public List<FarmCatalogue> getDiseaseSym() {

		return diseaseSym;
	}

	public void setDiseaseSym(List<FarmCatalogue> diseaseSym) {

		this.diseaseSym = diseaseSym;
	}

	public Map<String, String> getFields() {
		fields.put("1", getText("date"));
		fields.put("2", getText("season"));
		fields.put("3", getText("farmer"));
		fields.put("4", getLocaleProperty("father"));
		fields.put("5", getText("farm"));
		fields.put("6", getText("agent"));
		fields.put("9", getText("icsName"));

		/*
		 * if ((getIsMultiBranch().equalsIgnoreCase("1") &&
		 * (getIsParentBranch().equals("1")||StringUtil.isEmpty(branchIdValue)))
		 * ) { fields.put("8", getText("app.branch")); } else if
		 * (StringUtil.isEmpty(getBranchId())) { fields.put("7",
		 * getText("app.branch")); }
		 */
		if (getIsMultiBranch().equalsIgnoreCase("1")) {
			if (StringUtil.isEmpty(getBranchId())) {
				fields.put("8", getText("app.branch"));
			} else if (getIsParentBranch().equals("1")) {
				fields.put("8", getText("app.subBranch"));
			}

		} else if (StringUtil.isEmpty(getBranchId())) {

			fields.put("7", getText("app.branch"));
		}

		return fields;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}

	public String getIcsName() {
		return icsName;
	}

	public void setIcsName(String icsName) {
		this.icsName = icsName;
	}

	public List<PeriodicInspectionData> getDiseaseList() {

		return diseaseList;
	}

	public void setDiseaseList(List<PeriodicInspectionData> diseaseList) {

		this.diseaseList = diseaseList;
	}

	public String getDiseaseNameValue() {

		return diseaseNameValue;
	}

	public void setDiseaseNameValue(String diseaseNameValue) {

		this.diseaseNameValue = diseaseNameValue;
	}

	public String getChemicalNameString() {
		return chemicalNameString;
	}

	public void setChemicalNameString(String chemicalNameString) {
		this.chemicalNameString = chemicalNameString;
	}

	public String getCocDone() {

		return cocDone;
	}

	public void setCocDone(String cocDone) {

		this.cocDone = cocDone;
	}

	public String getUom() {

		return uom;
	}

	public void setUom(String uom) {

		this.uom = uom;
	}

	public Map<String, String> getUnit() {

		Map<String, String> uomMap = new LinkedHashMap<String, String>();
		List<FarmCatalogue> uomList = farmerService.listCatelogueType(getText("uomType"));
		uomList.stream().collect(Collectors.toMap((FarmCatalogue::getCode), FarmCatalogue::getName)).entrySet().stream()
				.sorted(Map.Entry.comparingByValue(new Comparator<String>() {
					@Override
					public int compare(String o1, String o2) {

						return o1.compareToIgnoreCase(o2);
					}
				})).forEachOrdered(e -> uomMap.put(e.getKey(), e.getValue()));

		return uomMap;
	}
	
	public Map<Integer, String> getIcsStatusList() { 

		String[] icsStatus = getLocaleProperty("icsStatusList").split(",");
		for (int i = 0; i < icsStatus.length; i++)
			icsStatusList.put(i, icsStatus[i]);
	return icsStatusList;
	
}
}