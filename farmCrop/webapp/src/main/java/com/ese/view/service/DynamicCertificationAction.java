/*
 * WarehouseProductAction.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.service;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.ese.entity.DynamicData;
import com.ese.entity.util.ESESystem;
import com.ese.util.Base64Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sourcetrace.eses.BreadCrumb;
import com.sourcetrace.eses.dao.IReportDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.DynamicFeildMenuConfig;
import com.sourcetrace.eses.entity.DynamicFieldConfig;
import com.sourcetrace.eses.entity.DynamicFieldConfig.LIST_METHOD;
import com.sourcetrace.eses.entity.DynamicFieldReportConfig;
import com.sourcetrace.eses.entity.DynamicFieldReportJoinMap;
import com.sourcetrace.eses.entity.DynamicFieldScoreMap;
import com.sourcetrace.eses.entity.DynamicMenuFieldMap;
import com.sourcetrace.eses.entity.DynamicMenuSectionMap;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.ICertificationService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.DynamicImageData;
import com.sourcetrace.esesw.entity.profile.DynamicImageData.TYPES;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.FarmIcsConversion;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicData;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicFieldsValue;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

// TODO: Auto-generated Javadoc
@Scope("prototype")
public class DynamicCertificationAction extends BaseReportAction {

	private static final long serialVersionUID = 1L;
	private String id;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private String xlsFileName;
	private InputStream fileInputStream;
	private String daterange;
	private FarmerDynamicData filter;
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private IProductDistributionService productDistributionService;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private ICatalogueService catalogueService;
	@Autowired
	private IAgentService agentService;
	List<Object[]> dynamicData = new LinkedList<>();
	private Map<Integer, String> inspectionStatuslist = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> icsStatusList = new LinkedHashMap<Integer, String>();
	private String txnType;
	private String entityType;
	private String mainGridCols;
	private JSONArray filterArray;
	private Map<Integer, String> certificationLevels = new LinkedHashMap<Integer, String>();
	private List<Object[]> dynamicSectionConfigHeader;
	private FarmerDynamicData farmerDynamicData;
	private String selectedFarmer;
	private String selectedFarm;
	private String inspectionStatus;
	private String icsType;
	private String correctiveActionPlan;
	private String selectedId;
	private String farmList;
	private ProcurementVariety farmCropList;
	private String farmer;
	private String address;
	private String area;
	private String group;
	private List<DynamicFieldReportJoinMap> dynamicFieldReportJoinMapList;
	private List<DynamicFieldReportConfig> dynamicFieldReportConfigList;
	private List<DynamicFeildMenuConfig> dynamicMenuConfigList;
	List<Object[]> farms = new ArrayList<Object[]>();
	private String condtion;
	private String command;
	private String season;
	private String fieldCondition;
	Map<String, List<FarmerDynamicFieldsValue>> fdMap = new HashMap<>();
	DynamicFeildMenuConfig dm = new DynamicFeildMenuConfig();
	LinkedHashMap<String, DynamicFieldConfig> fieldConfigMap = new LinkedHashMap<>();
	List<Warehouse> samithi = new ArrayList<Warehouse>();
	private String seasonCode;
	private Integer seasonType;
	private String txType;
	Map<String, String> fieldTypeMap = new HashMap<>();
	Map<String, String> filterMap = new HashMap<>();
	private String filters;
	private String filterCols;
	private String infoName;
	private String insDate;
	private String inspectorName;
	private String inspectorMobile;
	private String insType;
	private String scope;
	private String score;
	private String totLand;
	private String orgLand;
	private String totSite;
	private FarmIcsConversion farmIcsConversion;
	private String inspectionDateYear;
	Map<String, String> agentList = new LinkedHashMap<String, String>();
	@Autowired
	private ICertificationService certificationService;
	@Autowired
	private IPreferencesService preferncesService;
	private String digitalSignatureEnabled;
	private String digitalSignatureByteString;
	private String agentSignatureByteString;
	private String scoreVal;
	private Integer actStatuss;

	public enum ENTITYTYPES {
		NA, FARMER, FARM, GROUP, CERTIFICATION
	}

	private String branchIdParma;
	private String subBranchIdParam;
	private String createdUsername;
	private String farmAcerage;
	@Autowired
	private IUniqueIDGenerator idGenerator;

	public String list() {
		Calendar currentDate = Calendar.getInstance();
		Calendar cal = (Calendar) currentDate.clone();
		cal.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) - 1);
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);

		super.startDate = df.format(cal.getTime());
		super.endDate = df.format(currentDate.getTime());
		formCertificationLevels();
		daterange = super.startDate + " - " + super.endDate;
		DynamicFeildMenuConfig dy = getDynamicMenuConfigList().get(0);
		seasonType = dy.getIsSeason();
		entityType = dy.getEntity();
		score = String.valueOf(dy.getIsScore());
		List<DynamicFieldReportJoinMap> joinList = getDynamicFieldReportJoinMapList();

		filterCols = joinList.stream().filter(u -> u.getFilterId() != null)
				.map(p -> String.valueOf(p.getFilterName() + "-" + p.getFilterId())).collect(Collectors.joining(","));

		formMainGridCols();
		System.out.println(getEntityType());
		return LIST;
	}

	int cnt = 0;

	private void formMainGridCols() {

		DynamicFeildMenuConfig dy = getDynamicMenuConfigList().get(0);
		cnt = 1;
		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(getBranchId())))) {

			if (StringUtil.isEmpty(getBranchId())) {
				mainGridCols = getLocaleProperty("Organization") + "|";
				mainGridCols += getLocaleProperty("Sub Organization") + "|";
				cnt = cnt + 2;
			} else {
				mainGridCols = getLocaleProperty("Sub Organization") + "|";
				cnt = cnt + 1;
			}

		} else {

			if (StringUtil.isEmpty(getBranchId())) {
				mainGridCols = getLocaleProperty("Organization") + "|";
				cnt = cnt + 1;
			}
		}
		if (mainGridCols == null) {
			mainGridCols = getLocaleProperty("date") + "|" + getLocaleProperty("createdUser") + "|";
		} else {
			mainGridCols += getLocaleProperty("date") + "|" + getLocaleProperty("createdUser") + "|";
		}
		cnt = cnt + 2;
		if (dy.getIsSeason() == 1) {
			mainGridCols += getLocaleProperty("season") + "|";
			cnt++;
		}
		List<DynamicFieldReportConfig> DynamicFieldReportConfigList = farmerService
				.findDynamicFieldReportConfigByEntity(entityType);

		if (!ObjectUtil.isListEmpty(DynamicFieldReportConfigList)) {
			StringBuilder sb = new StringBuilder();
			DynamicFieldReportConfigList.stream().forEach(dynamicFieldReportConfig -> {
				sb.append(getLocaleProperty(dynamicFieldReportConfig.getLabel()) + "|");
				cnt++;
			});
			mainGridCols += sb.toString();
		}
		filterArray = new JSONArray();
		if (!ObjectUtil.isEmpty(dy) && !ObjectUtil.isListEmpty(dy.getDynamicFieldConfigs())) {
			StringBuilder sb = new StringBuilder();
			dy.getDynamicFieldConfigs().stream()
					.filter(u -> u.getField().getIsReportAvail() != null && u.getField().getIsReportAvail().equals("1"))
					.forEach(dynamicFieldReportConfig -> {
						sb.append(getLocaleProperty(
								dynamicFieldReportConfig.getField().getLangName(getLoggedInUserLanguage())) + "|");
					});

			dy.getDynamicFieldConfigs().stream().filter(u -> (u.getIsFilter() != null && u.getIsFilter() == 1))
					.forEach(dynamicFieldReportConfig -> {
						JSONObject js = new JSONObject();
						js.put("name", getLocaleProperty(
								dynamicFieldReportConfig.getField().getLangName(getLoggedInUserLanguage())));
						js.put("compCode", dynamicFieldReportConfig.getField().getComponentType());
						js.put("code", dynamicFieldReportConfig.getField().getCode());
						js.put("catVal", "");

						js.put("val", dynamicFieldReportConfig.getField().getValidation());
						String type = "";
						if (Arrays
								.asList(DynamicFieldConfig.COMPONENT_TYPES.TEXT_AREA.ordinal(),
										DynamicFieldConfig.COMPONENT_TYPES.TEXTBOX.ordinal())
								.contains(Integer.valueOf(dynamicFieldReportConfig.getField().getComponentType()))) {
							type = "1";
						} else if (Arrays.asList(DynamicFieldConfig.COMPONENT_TYPES.DATE_PICKER.ordinal())
								.contains(Integer.valueOf(dynamicFieldReportConfig.getField().getComponentType()))) {
							type = "2";
						} else if (Arrays
								.asList(DynamicFieldConfig.COMPONENT_TYPES.RADIO.ordinal(),
										DynamicFieldConfig.COMPONENT_TYPES.DROP_DOWN.ordinal(),
										DynamicFieldConfig.COMPONENT_TYPES.CHECKBOX.ordinal(),
										DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal())
								.contains(Integer.valueOf(dynamicFieldReportConfig.getField().getComponentType()))) {
							List<FarmCatalogue> fc = catalogueService
									.listCataloguesByType(dynamicFieldReportConfig.getField().getCatalogueType());
							fc.stream().forEach(u -> {
								String s = (String) js.get("catVal");
								s += (u.getCode() + "~" + u.getDispName()) + "|";
								js.put("catVal", s);
							});
							type = "3";
							if (js.get("catVal") != null && !StringUtil.isEmpty((String) js.get("catVal")))
								js.put("catVal", StringUtil.removeLastComma((String) js.get("catVal")));
						}
						js.put("type", type);
						filterArray.add(js);
					});

			mainGridCols += sb.toString();
		}
		setFilters(filterArray != null ? filterArray.toString() : "");
		String url = request.getRequestURL().toString();

		url = url == null || StringUtil.isEmpty(url) ? request.getHeader("referer") : url;

		if (url != null && url.contains("Report")) {
			if (dy.getIsScore() != null && dy.getIsScore() == 1) {
				mainGridCols += getLocaleProperty("totalScore") + "|";
			} else if (dy.getIsScore() != null && dy.getIsScore() == 2) {
				mainGridCols += getLocaleProperty("Result") + "|" + getLocaleProperty("Year") + "|";
			}
			mainGridCols += getLocaleProperty("action") + "|" + getLocaleProperty("locationDyn") + "|";
		}
		mainGridCols = StringUtil.removeLastChar(mainGridCols.toString(), '|').trim();
	}

	public String detail() {

		ESESystem preferences = preferncesService.findPrefernceById("1");
		setDigitalSignatureEnabled(preferences.getPreferences().get(ESESystem.ENABLE_DIGITAL_SIGNATURE));

		if (id != null && !id.equals("")) {
			farmerDynamicData = farmerService.findFarmerDynamicData(id);
			seasonType = farmerDynamicData.getIsSeason();
			entityType = farmerDynamicData.getEntityId();
			actStatuss = farmerDynamicData.getActStatus();
			farmerDynamicData.setTxnDate(!StringUtil.isEmpty(farmerDynamicData.getDate())
					? DateUtil.convertDateToString(farmerDynamicData.getDate(), DateUtil.STD_DATE_TIME_FORMAT) : "");
			Agent agent = agentService.findAgentByAgentId(farmerDynamicData.getCreatedUser());
			if (!ObjectUtil.isEmpty(agent)) {
				setCreatedUsername(agent.getPersonalInfo().getAgentName());
			}
			if (!ObjectUtil.isEmpty(farmerDynamicData) && farmerDynamicData.getEntityId().equalsIgnoreCase("4")) {

				if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
					Farm farm = farmerService.findFarmById(Long.valueOf(farmerDynamicData.getReferenceId()));
					setFarmer(String.valueOf(farm.getFarmer().getFirstName()));
					setFarmList(String.valueOf(farm.getFarmName()));
					setGroup(String.valueOf(farm.getFarmer().getSamithi().getName()));
					setSelectedVillage(farm.getFarmer().getVillage().getName());
					setSelectedState(farm.getFarmer().getVillage().getCity().getLocality().getState().getName());

				}
				farmerDynamicData.setInspectionStatus(farmerDynamicData.getConversionStatus());

				if (farmerDynamicData.getConversionStatus() != null
						&& !StringUtil.isEmpty(farmerDynamicData.getConversionStatus())) {
					if (farmerDynamicData.getConversionStatus().equalsIgnoreCase("1")) {
						farmerDynamicData.setInspectionStatus(getLocaleProperty("Approved"));
					} else {
						farmerDynamicData.setInspectionStatus(getLocaleProperty("Declined"));
					}
				}
				if (farmerDynamicData.getConversionStatus() != null
						&& !StringUtil.isEmpty(farmerDynamicData.getConversionStatus())
						&& farmerDynamicData.getConversionStatus().equalsIgnoreCase("1")) {
					if (!StringUtil.isEmpty(farmerDynamicData.getIcsName())
							&& farmerDynamicData.getIcsName().equalsIgnoreCase("0")) {
						setIcsType(getLocaleProperty("farm.ics1"));
					} else if (!StringUtil.isEmpty(farmerDynamicData.getIcsName())
							&& farmerDynamicData.getIcsName().equalsIgnoreCase("1")) {
						setIcsType(getLocaleProperty("farm.ics2"));
					} else if (!StringUtil.isEmpty(farmerDynamicData.getIcsName())
							&& farmerDynamicData.getIcsName().equalsIgnoreCase("2")) {
						setIcsType(getLocaleProperty("farm.ics3"));
					} else if (!StringUtil.isEmpty(farmerDynamicData.getIcsName())
							&& farmerDynamicData.getIcsName().equalsIgnoreCase("3")) {
						setIcsType(getLocaleProperty("farm.organic"));
					}

				} else {
					setCorrectiveActionPlan(!StringUtil.isEmpty(farmerDynamicData.getCorrectiveActionPlan())
							? farmerDynamicData.getCorrectiveActionPlan() : "");
				}
				if (!ObjectUtil.isEmpty(farmerDynamicData.getFarmIcs())) {
					setInsDate(DateUtil.convertDateToString(farmerDynamicData.getFarmIcs().getInspectionDate(),
							getGeneralDateFormat()));
					setInspectorName(farmerDynamicData.getFarmIcs().getInspectorName());
					setInspectorMobile(farmerDynamicData.getFarmIcs().getInspectorMobile());
					setInsType(!StringUtil.isEmpty(farmerDynamicData.getFarmIcs().getInsType())
							? getCatlogueValueByCode(farmerDynamicData.getFarmIcs().getInsType()).getName() : "");
					// CertificateCategory cc =
					// certificationService.findCertificateCategoryByCode(farmerDynamicData.getFarmIcs().getScope());
					setScope(getCatlogueValueByCode(farmerDynamicData.getFarmIcs().getScope()).getName());
					setTotLand(farmerDynamicData.getFarmIcs().getTotalLand());
					setOrgLand(farmerDynamicData.getFarmIcs().getOrganicLand());
					setTotSite(farmerDynamicData.getFarmIcs().getTotalSite());
					setSeason(farmerDynamicData.getFarmIcs().getSeason());
				}

				if (farmerDynamicData.getDymamicImageData() != null
						&& !ObjectUtil.isEmpty(farmerDynamicData.getDymamicImageData())
						&& farmerDynamicData.getDymamicImageData().size() > 0
						&& !ObjectUtil.isEmpty(farmerDynamicData.getDymamicImageData().iterator().next().getImage())) {

					if (farmerDynamicData.getDymamicImageData() != null) {

						farmerDynamicData.getDymamicImageData().stream().forEach(i -> {
							if (i.getTypez() == TYPES.digitalSignature.ordinal()) {
								setDigitalSignatureByteString(Base64Util.encoder(i.getImage()));

							}

							if (i.getTypez() == TYPES.agentSignature.ordinal()) {
								setAgentSignatureByteString(Base64Util.encoder(i.getImage()));

							}
						});

						// List<DynamicImageData> imgList = new
						// ArrayList<>(farmerDynamicData.getDymamicImageData());

						/*
						 * if(imgList.get(TYPES.digitalSignature.ordinal()) !=
						 * null &&
						 * imgList.get(TYPES.digitalSignature.ordinal()).
						 * getImage() != null){
						 * setDigitalSignatureByteString(Base64Util.encoder(
						 * imgList.get(TYPES.digitalSignature.ordinal()).
						 * getImage())); }
						 * if(imgList.get(TYPES.agentSignature.ordinal()) !=
						 * null &&
						 * imgList.get(TYPES.agentSignature.ordinal()).getImage(
						 * ) != null){
						 * setAgentSignatureByteString(Base64Util.encoder(
						 * imgList.get(TYPES.agentSignature.ordinal()).getImage(
						 * ))); }
						 */

					}

				}

			} else if (!ObjectUtil.isEmpty(farmerDynamicData)
					&& farmerDynamicData.getEntityId().equalsIgnoreCase("2")) {
				if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
					Farm farm = farmerService.findFarmById(Long.valueOf(farmerDynamicData.getReferenceId()));
					setFarmer(String.valueOf(farm.getFarmer().getFirstName()));
					setFarmList(String.valueOf(farm.getFarmName()));
					setAddress(farm != null ? String.valueOf(farm.getFarmer().getAddress() + ","
							+ farm.getFarmer().getCity().getName() + "," + farm.getFarmer().getVillage().getName())
							: "");
					setArea(farm.getFarmDetailedInfo().getProposedPlantingArea());
					setSelectedVillage(String.valueOf(farm.getFarmer().getVillage().getName()));
					setSelectedState(farm.getFarmer().getVillage().getCity().getLocality().getState().getName());
					setFarmAcerage(farm.getFarmDetailedInfo().getTotalLandHolding());
				}
				if (farmerDynamicData.getDymamicImageData() != null
						&& !ObjectUtil.isEmpty(farmerDynamicData.getDymamicImageData())
						&& farmerDynamicData.getDymamicImageData().size() > 0
						&& !ObjectUtil.isEmpty(farmerDynamicData.getDymamicImageData().iterator().next().getImage())) {

					if (farmerDynamicData.getDymamicImageData() != null) {

						farmerDynamicData.getDymamicImageData().stream().forEach(i -> {
							if (i.getTypez() == TYPES.digitalSignature.ordinal()) {
								setDigitalSignatureByteString(Base64Util.encoder(i.getImage()));

							}

							if (i.getTypez() == TYPES.agentSignature.ordinal()) {
								setAgentSignatureByteString(Base64Util.encoder(i.getImage()));

							}
						});

					}

				}
			} else if (!ObjectUtil.isEmpty(farmerDynamicData)
					&& farmerDynamicData.getEntityId().equalsIgnoreCase("1")) {
				if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
					Farmer frmr = farmerService.findFarmerById(Long.valueOf(farmerDynamicData.getReferenceId()));
					setFarmer(String.valueOf(frmr.getFirstName()));
					setSelectedVillage(String.valueOf(frmr.getVillage().getName()));
				}
			} else if (!ObjectUtil.isEmpty(farmerDynamicData)
					&& farmerDynamicData.getEntityId().equalsIgnoreCase("5")) {
				if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())
						&& farmerDynamicData.getReferenceId().contains(",")) {
					List<Farmer> frmr = farmerService
							.listFarmerByIds(Arrays.asList(farmerDynamicData.getReferenceId().split(",")));
					setFarmer(String.valueOf(frmr.stream().filter(u -> u.getFirstName() != null)
							.map(p -> String.valueOf(
									p.getFirstName() + (p.getLastName() != null ? ("-" + p.getLastName()) : "")))
							.collect(Collectors.joining(","))));
					setSelectedVillage(String.valueOf(frmr.stream().filter(u -> u.getFirstName() != null)
							.map(p -> String.valueOf(p.getVillage().getName())).distinct()
							.collect(Collectors.joining(","))));
				} else if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
					Farmer frmr = farmerService.findFarmerById(Long.valueOf(farmerDynamicData.getReferenceId()));
					setFarmer(String.valueOf(frmr.getFirstName()));
					setSelectedVillage(String.valueOf(frmr.getVillage().getName()));
				}
			} else if (!ObjectUtil.isEmpty(farmerDynamicData)
					&& farmerDynamicData.getEntityId().equalsIgnoreCase("3")) {
				if (!StringUtil.isEmail(farmerDynamicData.getReferenceId())) {
					Warehouse sam = locationService.findSamithiById(Long.valueOf(farmerDynamicData.getReferenceId()));
					setGroup(sam.getName());
				}
			} else if (!ObjectUtil.isEmpty(farmerDynamicData)
					&& farmerDynamicData.getEntityId().equalsIgnoreCase("6")) {
				if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
					FarmCrops farm = farmerService.findByFarmCropsId(Long.valueOf(farmerDynamicData.getReferenceId()));
					setFarmer(String.valueOf(farm.getFarm().getFarmer().getFirstName()));
					setFarmList(String.valueOf(farm.getFarm().getFarmName()));
					setSelectedVillage(String.valueOf(farm.getFarm().getFarmer().getVillage().getName()));
					setFarmCropList(farm.getProcurementVariety());

				}
			}

			if (farmerDynamicData.getIsSeason() == 1) {
				HarvestSeason season = getSeason(farmerDynamicData.getSeason());
				setSeason(!ObjectUtil.isEmpty(season) ? season.getName() : "");
			}

			if (farmerDynamicData.getIsScore() != null
					&& (farmerDynamicData.getIsScore() == 2 || farmerDynamicData.getIsScore() == 3)) {
				setScoreVal(farmerDynamicData.getTotalScore() != null
						&& StringUtil.isDouble(farmerDynamicData.getTotalScore())
								? formCertificationLevels().get(farmerDynamicData.getTotalScore().intValue()) : "");
				if (farmerDynamicData.getFollowUpDate() != null) {
					setInsDate(
							DateUtil.convertDateToString(farmerDynamicData.getFollowUpDate(), getGeneralDateFormat()));
				}
				farmerDynamicData.setConversionStatus(farmerDynamicData.getConversionStatus() != null
						? "Year " + farmerDynamicData.getConversionStatus() : "");

			}

			return DETAIL;
		}
		return LIST;
	}

	private HarvestSeason getSeason(String seasonCode) {

		HarvestSeason season = getHarvestSeason(seasonCode);
		return season;

	}

	public String data() throws Exception {

		Map entityMap = new HashMap<>();

		Map<String, String> condMapc = new HashMap<>();

		/*
		 * Included all these filters oin condition based on Dynamic Field
		 * report join map if (!StringUtil.isEmpty(farmer)) {
		 * condMapc.put("f.id", farmer); }
		 * 
		 * if (!StringUtil.isEmpty(farmList)) { condMapc.put("farmList",
		 * farmList); }
		 * 
		 * if (!StringUtil.isEmpty(group)) { condMapc.put("g.id", group); }
		 * 
		 * if (!StringUtil.isEmpty(selectedVillage)) { condMapc.put("v.code",
		 * selectedVillage); }
		 */
		if (!StringUtil.isEmpty(condtion) && !condtion.equals("{}")) {
			Type listType1 = new TypeToken<Map<String, String>>() {
			}.getType();
			Map<String, String> dynamicList = new Gson().fromJson(condtion, listType1);
			entityMap.put("filters", dynamicList);

			entityMap.put(IReportDAO.JOIN_MAP, getDynamicFieldReportJoinMapList());
			entityMap.put(IReportDAO.ENTITY, entityType);
		}

		if (!StringUtil.isEmpty(fieldCondition) && !fieldCondition.equals("{}")) {
			Type listType1 = new TypeToken<Map<String, String>>() {
			}.getType();
			Map<String, String> dynamicList = new Gson().fromJson(fieldCondition, listType1);

			entityMap.put(IClientService.PROJECTIONS_CONDITION, dynamicList);
			entityMap.put(IReportDAO.JOIN_MAP, getDynamicFieldReportJoinMapList());
			entityMap.put(IReportDAO.ENTITY, entityType);
		}

		if (filter != null && filter.getReferenceId() != null) {

		} else {
			setFilter(new FarmerDynamicData());
		}

		if (!StringUtil.isEmpty(seasonCode)) {

			filter.setSeason(seasonCode);
		}
		filter.setTxnType(getTxnType());
		filter.setEntityMap(entityMap);

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

		if (!StringUtil.isEmpty(subBranchIdParam) && !subBranchIdParam.equals("0")) {
			filter.setBranch(subBranchIdParam);
		}

		super.filter = filter;

		Map data = readData();

		return sendJSONResponse(data);

	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		FarmerDynamicData farmerDynamicData = (FarmerDynamicData) obj;
		DynamicFeildMenuConfig dy = getDynamicMenuConfigList().get(0);
		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(!StringUtil
						.isEmpty(getBranchesMap().get(getParentBranchMap().get(farmerDynamicData.getBranch())))
								? getBranchesMap().get(getParentBranchMap().get(farmerDynamicData.getBranch()))
								: getBranchesMap().get(farmerDynamicData.getBranch()));
			}
			rows.add(getBranchesMap().get(farmerDynamicData.getBranch()));

		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(branchesMap.get(farmerDynamicData.getBranch()));
			}
		}
		if (!ObjectUtil.isEmpty(farmerDynamicData)) {
			String entu = "";
			rows.add(!StringUtil.isEmpty(farmerDynamicData.getDate())
					? DateUtil.convertDateToString(farmerDynamicData.getDate(), DateUtil.STD_DATE_TIME_FORMAT) : "");

			rows.add(getAgentList().get(farmerDynamicData.getCreatedUser()));

			if (dy.getIsSeason() != null && dy.getIsSeason() == 1) {

				HarvestSeason season = getHarvestSeason(farmerDynamicData.getSeason());
				rows.add(!ObjectUtil.isEmpty(season) ? season.getName() : "");
			}

			if (!StringUtil.isEmpty(entityType)) {
				int rType = Integer.parseInt(entityType);
				/*
				 * if (rType ==
				 * DynamicFeildMenuConfig.EntityTypes.FARMER.ordinal()) { Farmer
				 * farmer = getFarmer(farmerDynamicData.getReferenceId());
				 * rows.add(farmer.getFirstName());
				 * rows.add(farmer.getVillage().getName());
				 * rows.add(farmer.getSamithi().getName()); } else if (rType ==
				 * DynamicFeildMenuConfig.EntityTypes.FARM.ordinal() || rType ==
				 * DynamicFeildMenuConfig.EntityTypes.CERTIFICATION.ordinal()) {
				 */
				Map criteriaMap = new HashMap<>();
				if (rType == DynamicFeildMenuConfig.EntityTypes.FARMER.ordinal()
						|| rType == DynamicFeildMenuConfig.EntityTypes.TRAINING.ordinal()) {
					criteriaMap.put(IClientService.ENTITY, Farmer.class);
				} else if (rType == DynamicFeildMenuConfig.EntityTypes.FARM.ordinal()
						|| rType == DynamicFeildMenuConfig.EntityTypes.CERTIFICATION.ordinal()) {
					criteriaMap.put(IClientService.ENTITY, Farm.class);
				} else if (rType == DynamicFeildMenuConfig.EntityTypes.GROUP.ordinal()) {
					criteriaMap.put(IClientService.ENTITY, Warehouse.class);
				} else if (rType == DynamicFeildMenuConfig.EntityTypes.FARM_CROPS.ordinal()) {
					criteriaMap.put(IClientService.ENTITY, FarmCrops.class);
				}

				StringBuilder list = new StringBuilder();
				StringBuilder alias = new StringBuilder();

				getDynamicFieldReportJoinMapList().stream().forEach(join -> {
					alias.append(join.getProperties() + ",");
				});

				criteriaMap.put(IClientService.CRITERIA_ALIAS, StringUtil.removeLastComma(alias.toString()));

				getDynamicFieldReportConfigList().stream()
						.filter(dReportConfig -> ObjectUtil.isEmpty(dReportConfig.getGroupProp())
								|| !dReportConfig.getGroupProp().equals("1"))
						.forEach(dReportConfig -> {
							list.append(dReportConfig.getField() + ",");
						});

				criteriaMap.put(IClientService.PROJECTIONS_LIST, StringUtil.removeLastComma(list.toString()));
				List<Object> valueListc = clientService.getMappedValues(criteriaMap,
						farmerDynamicData.getReferenceId());
				if (valueListc.size() > 0 && valueListc.get(0) instanceof String) {
					rows.add(valueListc.get(0));
					entu = String.valueOf(valueListc.get(0));
				} else if (valueListc.size() > 0 && valueListc.get(0) instanceof Object[]) {
					Object[] objList = (Object[]) valueListc.get(0);
					for (Object obje : objList) {

						rows.add(String.valueOf(obje));

					}
					if (objList.length > 0) {
						entu = String.valueOf(objList[0]);
					}
				}

				/*
				 * } else if (rType ==
				 * DynamicFeildMenuConfig.EntityTypes.GROUP.ordinal()) {
				 * Warehouse warehouse =
				 * getWarehouse(farmerDynamicData.getReferenceId());
				 * rows.add(warehouse.getName()); }
				 */
			}
			if (dy.getDynamicFieldConfigs().stream().anyMatch(
					u -> u.getField().getIsReportAvail() != null && u.getField().getIsReportAvail().equals("1"))) {
				Map<String, String> valuesMap = new LinkedHashMap<>();
				farmerDynamicData.getFarmerDynamicFieldsValues().stream().forEach(dynamicFieldValues -> {

					if (!StringUtil.isEmpty(dynamicFieldValues.getComponentType())
							&& Arrays
									.asList(String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.DROP_DOWN.ordinal()),
											String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.RADIO.ordinal()),
											String.valueOf(
													DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal()),
											String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.CHECKBOX.ordinal()))
									.contains(dynamicFieldValues.getComponentType())) {
						valuesMap.put(dynamicFieldValues.getFieldName(),
								getCatlogueValueByCode(dynamicFieldValues.getFieldValue()).getName());
					} else {
						valuesMap.put(dynamicFieldValues.getFieldName(), dynamicFieldValues.getFieldValue());
					}

				});
				dy.getDynamicFieldConfigs().stream().filter(
						u -> u.getField().getIsReportAvail() != null && u.getField().getIsReportAvail().equals("1"))
						.forEach(dynamicFieldReportConfig -> {
							if (valuesMap.containsKey(dynamicFieldReportConfig.getField().getCode())) {
								rows.add(valuesMap.get(dynamicFieldReportConfig.getField().getCode()));
							} else {
								rows.add("");
							}
						});
			}
			if (dy.getIsScore() != null && dy.getIsScore() == 1) {
				rows.add(farmerDynamicData.getTotalScore() + "%");
			} else if (dy.getIsScore() != null && dy.getIsScore() == 2) {
				rows.add(farmerDynamicData.getTotalScore() != null
						&& StringUtil.isDouble(farmerDynamicData.getTotalScore())
								? formCertificationLevels().get(farmerDynamicData.getTotalScore().intValue()) : "");
				rows.add(farmerDynamicData.getConversionStatus() != null
						? "Year " + farmerDynamicData.getConversionStatus() : "");
			}

			if (txType != null && txType.equalsIgnoreCase("report")) {
				rows.add("<button class='xlsIcon' title='" + getText("dynExport", new String[] { entu }) + " "
						+ dy.getLangName(getLoggedInUserLanguage()) + " for the "
						+ DateUtil.convertDateToString(farmerDynamicData.getDate(), DateUtil.DATE_TIME_FORMAT)
						+ "' onclick='exportXLS(\"" + farmerDynamicData.getId() + "\")'></button>");

				if ((!StringUtil.isEmpty(farmerDynamicData.getLatitude())
						&& !StringUtil.isEmpty(farmerDynamicData.getLongitude()))) {
					rows.add("<button class='faMap' title='" + getText("farm.map.available.title")
							+ "' onclick='showFarmMap(\""
							+ (!StringUtil.isEmpty(farmerDynamicData.getLatitude()) ? farmerDynamicData.getLatitude()
									: "")
							+ "\",\"" + (!StringUtil.isEmpty(farmerDynamicData.getLongitude())
									? farmerDynamicData.getLongitude() : "")
							+ "\")'></button>");
				} else {
					// No Latlon
					rows.add("<button class='no-latLonIcn' title='" + getText("farm.map.unavailable.title")
							+ "'></button>");
				}

			}

			jsonObject.put("id", farmerDynamicData.getId());
			jsonObject.put("cell", rows);
		}
		return jsonObject;

	}

	private Map<Integer, String> formCertificationLevels() {

		if (certificationLevels.isEmpty()) {
			String values = getText("fcl");
			if (!StringUtil.isEmpty(values)) {
				String[] valuesArray = values.split(",");
				int i = -1;
				// Arrays.sort(valuesArray);
				for (String value : valuesArray) {
					certificationLevels.put(i++, value);
				}
			}
		}
		return certificationLevels;
	}

	/**
	 * Creates the.
	 * 
	 * @return
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public String create() throws Exception {

		if (farmerDynamicData == null) {
			command = "create";
			if (!ObjectUtil.isListEmpty(getDynamicMenuConfigList()) || getDynamicMenuConfigList().size() > 0) {
				DynamicFeildMenuConfig dy = getDynamicMenuConfigList().get(0);
				seasonType = dy.getIsSeason();
				entityType = dy.getEntity();
			}
			request.setAttribute(HEADING, getText(CREATE));
			return INPUT;

		} else {

			fdMap = new HashMap<>();
			dm = farmerService.findDynamicMenusByMType(txnType).get(0);
			fieldConfigMap = new LinkedHashMap<>();
			Map<String, String> ActMap = new HashMap<>();
			if (dm.getIsScore() != null && (dm.getIsScore() == 1 || dm.getIsScore() == 2 || dm.getIsScore() == 3)) {
				farmerDynamicData.setIsScore(dm.getIsScore());
				farmerDynamicData.setScoreValue(new HashMap<>());
				farmerDynamicData.setActStatus(0);
			}
			dm.getDynamicFieldConfigs().stream().forEach(section -> {
				section.getField().setfOrder(section.getOrder());
				fieldConfigMap.put(section.getField().getCode(), section.getField());
				if (section.getDynamicFieldScoreMap() != null && !section.getDynamicFieldScoreMap().isEmpty()) {
					farmerDynamicData.getScoreValue().put(section.getField().getCode(), section
							.getDynamicFieldScoreMap().stream()
							.collect(Collectors.toMap(DynamicFieldScoreMap::getCatalogueCode, u -> String
									.valueOf(String.valueOf(u.getScore()) + "~" + String.valueOf(u.getPercentage())))));
				}
				if (section.getField().getFollowUp() == 1 || section.getField().getFollowUp() == 2) {
					ActMap.put(section.getField().getCode(), section.getField().getParentActField());
				}
			});

			fieldTypeMap = new HashMap<>();
			if (dm.getIsSingleRecord() == 1) {
				if (dm.getIsSeason() != null && dm.getIsSeason() == 1) {
					farmerDynamicData = farmerService.findFarmerDynamicDataBySeason(txnType, selectedId, season);
				} else {
					farmerDynamicData = farmerService.findFarmerDynamicData(txnType, selectedId);
				}
				if (farmerDynamicData != null) {
					fdMap = farmerDynamicData.getFarmerDynamicFieldsValues().stream()
							.collect(Collectors.groupingBy(FarmerDynamicFieldsValue::getFieldName));
					farmerDynamicData.getFarmerDynamicFieldsValues()
							.removeAll(farmerDynamicData.getFarmerDynamicFieldsValues().stream().filter(
									map -> (map.getIsMobileAvail() != null && map.getIsMobileAvail().equals("2")))
									.collect(Collectors.toList()));
					List<Object[]> fdfvList = clientService.listMaxTypeByRefId(selectedId);
					fieldTypeMap = fdfvList.stream()
							.collect(Collectors.toMap(obj -> String.valueOf(obj[0]), obj -> String.valueOf(obj[1])));

				} else {

					farmerDynamicData = new FarmerDynamicData();

				}
			}
			// farmerDynamicData = new FarmerDynamicData();
			farmerDynamicData.setEntityId(getEntityType());
			farmerDynamicData.setStatus("0");
			farmerDynamicData.setCreatedDate(new Date());
			farmerDynamicData.setCreatedUser(getUsername());
			String nowDate = DateUtil.convertDateToString(new Date(), (getGeneralDateFormat() + " HH:mm:ss"));
			farmerDynamicData.setDate(DateUtil.convertStringToDate(nowDate, (getGeneralDateFormat() + " HH:mm:ss")));
			farmerDynamicData.setEntityId(entityType);
			farmerDynamicData.setTxnType(txnType);
			if (StringUtil.isEmpty(season)) {
				farmerDynamicData.setSeason(getCurrentSeasonsCode());
			} else {
				farmerDynamicData.setSeason(season);
			}
			farmerDynamicData.setTxnUniqueId(DateUtil.getRevisionNumber());
			farmerDynamicData.setReferenceId(selectedId);
			// farmerDynamicData.setFarmerDynamicFieldsValues(getDynamicFieldValues(Long.valueOf(farmerDynamicData.getReferenceId())));
			farmerDynamicData.setBranch(getBranchId());

			if (getEntityType().equalsIgnoreCase("4")) {
				FarmIcsConversion existing = new FarmIcsConversion();
				Date df = DateUtil.convertStringToDate(insDate, getGeneralDateFormat());
				inspectionDateYear = DateUtil.getYearByDateTime(df);
				existing = farmerService
						.findFarmIcsConversionByFarmIdAndInspectionTypeAndScopeOperationWithInspectionDate(
								Long.valueOf(farmerDynamicData.getReferenceId()), insType, scope,
								Integer.parseInt(inspectionDateYear));

				farmerDynamicData.setConversionStatus(getInspectionStatus());
				farmerDynamicData.setCorrectiveActionPlan(getCorrectiveActionPlan());
				// farmerDynamicData.setIcsName(!ObjectUtil.isEmpty(existing) &&
				// !StringUtil.isEmpty(getIcsType())?getIcsType():"0");
				farmerDynamicData.setIcsName(getIcsType());
				farmerDynamicData.setCorrectiveActionPlan(getCorrectiveActionPlan());
				farmerDynamicData.setFarmerId(getSelectedFarmer());

				Farm frm = farmerService.findFarmByID(Long.parseLong(farmerDynamicData.getReferenceId()));
				if (ObjectUtil.isEmpty(existing)) {
					FarmIcsConversion farmIcsconversion = new FarmIcsConversion();
					farmIcsconversion.setFarm(frm);
					farmIcsconversion.setIcsType(!StringUtil.isEmpty(icsType) ? icsType
							: farmerService.findFarmIcsConversionByFarmId(frm.getId()).getIcsType());
					farmIcsconversion.setInspectionDate(DateUtil.convertStringToDate(insDate, getGeneralDateFormat()));
					farmIcsconversion.setInspectorMobile(inspectorMobile);
					farmIcsconversion.setInspectorName(inspectorName);
					farmIcsconversion.setOrganicLand(orgLand);
					farmIcsconversion.setScope(scope);
					farmIcsconversion.setSeason(season);
					farmIcsconversion.setStatus(Integer.parseInt(inspectionStatus));
					farmIcsconversion.setTotalLand(totLand);
					farmIcsconversion.setInsType(insType);
					farmIcsconversion.setTotalSite(totSite);
					farmIcsconversion.setIsActive(1);
					farmIcsconversion.setQualified(Integer.valueOf(getInspectionStatus()));
					farmIcsconversion.setFarmer(frm.getFarmer());
					if (getCurrentTenantId().equalsIgnoreCase("wilmar")) {
						if (!ObjectUtil.isEmpty(frm) && !ObjectUtil.isEmpty(frm.getFarmer().getIsCertifiedFarmer())
								&& frm.getFarmer().getIsCertifiedFarmer() == 1
								&& !StringUtil.isEmpty(farmIcsconversion.getIcsType())
								&& farmIcsconversion.getIcsType().equalsIgnoreCase("3")) {
							farmIcsconversion.setOrganicStatus("3");
						} else {
							farmIcsconversion.setOrganicStatus("0");
						}
					}
					farmerService.saveFarmIcsConversionByFarmId(Long.parseLong(farmerDynamicData.getReferenceId()),
							farmIcsconversion);

					FarmIcsConversion fIcs = farmerService.findFarmIcsConversionById(farmIcsconversion.getId());
					farmerDynamicData.setFarmIcs(fIcs);
					if (getCurrentTenantId().equalsIgnoreCase("wilmar")) {
						Farmer farmer = farmerService.findFarmerById(Long.valueOf(frm.getFarmer().getId()));
						farmer.setIsCertifiedFarmer(1);
						farmer.setCertificationType(1);
						farmerService.update(farmer);
					}

				} else {
					existing.setFarm(frm);
					existing.setIcsType(!StringUtil.isEmpty(icsType) ? icsType : existing.getIcsType());
					existing.setInspectionDate(DateUtil.convertStringToDate(insDate, getGeneralDateFormat()));
					existing.setInspectorMobile(inspectorMobile);
					existing.setInspectorName(inspectorName);
					existing.setOrganicLand(orgLand);
					existing.setScope(scope);
					existing.setSeason(season);
					existing.setStatus(Integer.parseInt(inspectionStatus));
					existing.setTotalLand(totLand);
					existing.setInsType(insType);
					existing.setTotalSite(totSite);
					existing.setIsActive(1);
					existing.setQualified(Integer.valueOf(getInspectionStatus()));
					existing.setFarmer(frm.getFarmer());
					if (getCurrentTenantId().equalsIgnoreCase("wilmar")) {
						if (!ObjectUtil.isEmpty(frm) && !ObjectUtil.isEmpty(frm.getFarmer().getIsCertifiedFarmer())
								&& frm.getFarmer().getIsCertifiedFarmer() == 1
								&& !StringUtil.isEmpty(existing.getIcsType())
								&& existing.getIcsType().equalsIgnoreCase("3")) {
							existing.setOrganicStatus("3");
						} else {
							existing.setOrganicStatus("0");
						}
					}
					// farmerService.updateFarmICSStatusByFarmIdInsTypeAndIcsType(Long.valueOf(farmList),
					// getInsType(),getIcsType());
					// farmerService.updateFarmIcsConversionByFarmId(Long.parseLong(farmerDynamicData.getReferenceId()),existing);
					farmerService.update(existing);

					FarmIcsConversion fIcs = farmerService.findFarmIcsConversionById(existing.getId());
					farmerDynamicData.setFarmIcs(fIcs);

				}
			}
			if (farmerDynamicData != null && farmerDynamicData.getId() != null && farmerDynamicData.getId() > 0) {
				// Set<FarmerDynamicFieldsValue> fdvdLIst =new HashSe

				farmerDynamicData.setFarmerDynamicFieldsValues(
						getDynamicFieldValues(selectedId, farmerDynamicData.getFarmerDynamicFieldsValues(), "1"));
			} else {
				farmerDynamicData.setFarmerDynamicFieldsValues(getDynamicFieldValues(selectedId, new HashSet<>(), "1"));
			}

			farmerService.saveOrUpdate(farmerDynamicData, fdMap, fieldConfigMap);
			if (fieldConfigMap.values().stream().anyMatch(p -> Arrays.asList("4").contains(p.getIsMobileAvail())
					&& p.getFormula() != null && !StringUtil.isEmpty(p.getFormula()))) {
				farmerService.processCustomisedFormula(farmerDynamicData, fieldConfigMap, fdMap);
			}
			if (!farmerDynamicData.getTxnType().equals("381")) {
				farmerService.deleteChildObjects(farmerDynamicData.getTxnType());
			}

			/* farmerService.updateDynamicFarmerFieldComponentType(); */

			if (!StringUtil.isEmpty(getIcsType())) {
				farmerService.updateFarmICSStatusByFarmId(Long.valueOf(farmerDynamicData.getReferenceId()),
						getIcsType());
			}
			command = "list";
			return REDIRECT;
		}

	}

	Map<String, Object> profileUpdateFields = new HashMap<>();

	protected Set<FarmerDynamicFieldsValue> getDynamicFieldValues(String id, Set<FarmerDynamicFieldsValue> fdfvSet,
			String action) {
		profileUpdateFields = new HashMap<>();
		if (!StringUtil.isEmpty(dynamicFieldsArray)) {
			try {
				List<FarmerDynamicFieldsValue> farmerDynamicFieldValuesList = new ArrayList<>();
				Type listType1 = new TypeToken<List<DynamicData>>() {
				}.getType();
				List<DynamicData> dynamicList = new Gson().fromJson(dynamicFieldsArray, listType1);
				for (DynamicData dynamicData : dynamicList) {

					if (!StringUtil.isEmpty(dynamicData.getValue())
							|| !StringUtil.isEmpty(dynamicData.getImageFile())) {
						if (dm.getIsSingleRecord() == 1 && fdMap.containsKey(dynamicData.getName())
								&& !ObjectUtil.isEmpty(farmerDynamicData)
								&& StringUtil.isEmpty(dynamicData.getTypez())) {
							fdfvSet.removeIf(e -> (e.getFieldName().equals(dynamicData.getName())
									&& StringUtil.isEmpty(e.getTypez())));
							/*
							 * fdfvSet
							 * .remove(fdMap.get(dynamicData.getName()));
							 */
						}
						FarmerDynamicFieldsValue dynamicFieldsValue = new FarmerDynamicFieldsValue();
						dynamicFieldsValue.setFieldName(dynamicData.getName());

						dynamicFieldsValue
								.setComponentType(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null
										? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getComponentType()
										: "0");

						if (fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getIsOther() == 1) {
							if (Integer.valueOf(fieldConfigMap.get(dynamicFieldsValue.getFieldName())
									.getAccessType()) == DynamicFieldConfig.ACCESS_TYPE.CATALOGUE_TYPE.ordinal()) {
								if (dynamicData.getValue().contains(",")) {
									Arrays.asList(dynamicData.getValue().split(",")).stream().forEach(u -> {
										FarmCatalogue fm = catalogueService.findCatalogueByCode(u);
										if (fm == null) {
											fm = new FarmCatalogue();
											fm.setCode(idGenerator.getCatalogueIdSeq());
											fm.setName(u);
											fm.setRevisionNo(DateUtil.getRevisionNumber());
											fm.setStatus("1");
											fm.setTypez(Integer.valueOf(fieldConfigMap
													.get(dynamicFieldsValue.getFieldName()).getCatalogueType()));
											catalogueService.addCatalogue(fm);
											dynamicFieldsValue
													.setFieldValue(dynamicData.getValue().replaceAll(u, fm.getCode()));
										}

									});
								} else {
									FarmCatalogue fm = catalogueService.findCatalogueByCode(dynamicData.getValue());
									if (fm == null) {
										fm = new FarmCatalogue();
										fm.setCode(idGenerator.getCatalogueIdSeq());
										fm.setName(dynamicData.getValue());
										fm.setRevisionNo(DateUtil.getRevisionNumber());
										fm.setStatus("1");
										fm.setTypez(Integer.valueOf(fieldConfigMap
												.get(dynamicFieldsValue.getFieldName()).getCatalogueType()));
										catalogueService.addCatalogue(fm);
									}
									dynamicFieldsValue.setFieldValue(fm.getCode());
								}
							}
						} else {

							dynamicFieldsValue.setFieldValue(dynamicData.getValue());
						}
						try {
							dynamicFieldsValue.setTypez(Integer.valueOf(dynamicData.getTypez()));
						} catch (Exception e) {
							dynamicFieldsValue.setTypez(null);
						}
						dynamicFieldsValue.setAccessType(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null
								? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getAccessType() : 0);

						dynamicFieldsValue.setListMethod(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null
								&& fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getCatalogueType() != null
										? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getCatalogueType()
										: "");

						dynamicFieldsValue.setParentId(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null
								&& fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getReferenceId() != null
										? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getReferenceId() : 0);
						dynamicFieldsValue
								.setIsUpdateProfile(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null
										? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getIsUpdateProfile()
										: "0");
						dynamicFieldsValue.setProfileField(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null
								? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getProfileField() : "0");

						if (fieldTypeMap != null && fieldTypeMap.containsKey(dynamicFieldsValue.getFieldName())) {

							String iterateCount = fieldTypeMap.get(dynamicFieldsValue.getFieldName());

							if (!StringUtil.isEmpty(iterateCount) && StringUtil.isInteger(iterateCount)) {
								Integer type = Integer.valueOf(iterateCount) + Integer.valueOf(dynamicData.getTypez());
								dynamicFieldsValue.setTypez(type);
							} else {
								Integer type = Integer.valueOf(dynamicData.getTypez());
								dynamicFieldsValue.setTypez(type);
							}
						}

						dynamicFieldsValue.setReferenceId(String.valueOf(id));
						dynamicFieldsValue.setTxnType(dynamicData.getTxnTypez());
						dynamicFieldsValue.setTxnUniqueId((DateUtil.getRevisionNumber()));
						dynamicFieldsValue.setCreatedDate(new Date());
						dynamicFieldsValue.setCreatedUser(getUsername());
						dynamicFieldsValue.setFollowUp(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null

								? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getFollowUp() : 0);
						if (dynamicFieldsValue.getFollowUp() == 3) {
							farmerDynamicData.getFarmerDynamicFieldsValues()
									.removeIf(u -> u.getFieldName().equals(dynamicData.getName()));
						}
						dynamicFieldsValue.setGrade(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null
								? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getGrade() : null);
						dynamicFieldsValue.setParentActField(fieldConfigMap
								.get(dynamicFieldsValue.getFieldName()) != null
								&& fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getParentActField() != null
										? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getParentActField()
										: null);
						dynamicFieldsValue.setParentActKey(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null
								&& fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getParentActKey() != null
										? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getParentActKey()
										: null);

						dynamicFieldsValue
								.setIsMobileAvail(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null
										? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getIsMobileAvail()
										: "0");
						if (dynamicFieldsValue.getComponentType().equals("15")) {
							dynamicFieldsValue.setFieldValue(dynamicData.getValue());
							profileUpdateFields.put(dynamicFieldsValue.getFieldName(), dynamicData.getValue());
						} else if (dynamicFieldsValue.getIsUpdateProfile() != null
								&& dynamicFieldsValue.getIsUpdateProfile().equals("1")) {
							profileUpdateFields.put(dynamicFieldsValue.getFieldName(),
									dynamicFieldsValue.getFieldValue());
						}
						if (dynamicData.getCompoType() != null
								&& (dynamicData.getCompoType().equals("12") || dynamicData.getCompoType().equals("13")
										|| dynamicData.getCompoType().equals("11"))
								&& dynamicData.getImageFile() != null
								&& !StringUtil.isEmpty(dynamicData.getImageFile())) {
							Set<DynamicImageData> imageDataSet = new HashSet<>();
							DynamicImageData dy = new DynamicImageData();
							dy.setFileExt(dynamicData.getFileExt());
							if (action != null && action.equals("2")) {
								try {
									Long ids = Long.parseLong(dynamicData.getImageFile());
									DynamicImageData dt = farmerService.findDynamicImageDataById(ids);
									if (dt != null) {
										dy.setImage(dt.getImage());
										dy.setFileExt(dt.getFileExt());
									}

								} catch (Exception e) {

									dy.setImage(Base64.decode(dynamicData.getImageFile().split("base64,")[1]));

								}
							} else {
								dy.setImage(Base64.decode(dynamicData.getImageFile().split("base64,")[1]));
							}
							dy.setFarmerDynamicFieldsValue(dynamicFieldsValue);
							dy.setOrder("1");
							imageDataSet.add(dy);
							dynamicFieldsValue.setDymamicImageData(imageDataSet);
							if (dynamicFieldsValue.getIsUpdateProfile() != null
									&& dynamicFieldsValue.getIsUpdateProfile().equals("1")) {
								profileUpdateFields.put(dynamicFieldsValue.getFieldName(), dy.getImage());
							}
						}

						if (fieldConfigMap.containsKey(dynamicFieldsValue.getFieldName())

								&& fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null
								&& (dynamicFieldsValue.getIsMobileAvail() != null
										&& !dynamicFieldsValue.getIsMobileAvail().equals("2"))) {
							farmerDynamicFieldValuesList.add(dynamicFieldsValue);
							// To get the values for formula calculation
							if (StringUtil.isEmpty(dynamicFieldsValue.getTypez())) {
								fdMap.put(dynamicFieldsValue.getFieldName(), new ArrayList<FarmerDynamicFieldsValue>() {
									{
										add(dynamicFieldsValue);

									}
								});
							} else {
								if (fdMap.containsKey(dynamicFieldsValue.getFieldName())) {
									List<FarmerDynamicFieldsValue> dy = fdMap.get(dynamicFieldsValue.getFieldName());
									dy.add(dynamicFieldsValue);
									fdMap.put(dynamicFieldsValue.getFieldName(), dy);
								} else {
									fdMap.put(dynamicFieldsValue.getFieldName(),
											new ArrayList<FarmerDynamicFieldsValue>() {
												{
													add(dynamicFieldsValue);

												}
											});
								}
							}
						}
						// farmerDynamicFieldValuesList.add(dynamicFieldsValue);

					}
				}
				// clientService.saveFarmerDynmaicList(farmerDynamicFieldValuesList);
				fdfvSet.addAll(farmerDynamicFieldValuesList);
				farmerDynamicFieldValuesList.stream()
						.filter(farmerDynamicFieldsValue -> farmerDynamicFieldsValue.getFollowUp() == 1
								|| farmerDynamicFieldsValue.getFollowUp() == 2)
						.forEach(farmerDynamicFieldsValue -> {
							if (farmerDynamicFieldsValue.getFollowUp() == 1 && fdfvSet.stream().anyMatch(
									u -> u.getFieldName().equals(farmerDynamicFieldsValue.getParentActField()))) {
								FarmerDynamicFieldsValue ParentACt = fdfvSet.stream().filter(
										u -> u.getFieldName().equals(farmerDynamicFieldsValue.getParentActField()))
										.findFirst().get();

								fdfvSet.remove(ParentACt);
								ParentACt.setActionPlan(farmerDynamicFieldsValue);
								fdfvSet.add(ParentACt);

							} else if (farmerDynamicFieldsValue.getFollowUp() == 2 && fdfvSet.stream().anyMatch(
									u -> u.getFieldName().equals(farmerDynamicFieldsValue.getParentActField()))) {
								FarmerDynamicFieldsValue ParentACt = fdfvSet.stream().filter(
										u -> u.getFieldName().equals(farmerDynamicFieldsValue.getParentActField()))
										.findFirst().get();
								fdfvSet.remove(ParentACt);
								ParentACt.setDeadline(farmerDynamicFieldsValue);
								fdfvSet.add(ParentACt);

							}
						});
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
		farmerDynamicData.setProfileUpdateFields(profileUpdateFields);
		return fdfvSet;
	}

	public String update() throws Exception {
		if (id != null && !id.equals("")) {
			farmerDynamicData = farmerService.findFarmerDynamicData(id);
			if (farmerDynamicData == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			seasonType = farmerDynamicData.getIsSeason();
			entityType = farmerDynamicData.getEntityId();
			// Farm
			// farm=farmerService.findFarmById(Long.valueOf(farmerDynamicData.getReferenceId()));
			if (!ObjectUtil.isEmpty(farmerDynamicData) && farmerDynamicData.getEntityId().equalsIgnoreCase("1")) {
				if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
					Farmer farm = getFarmer(farmerDynamicData.getReferenceId());
					setFarmer(farm != null ? String.valueOf(farm.getFirstName()) : "");
					setSelectedVillage(farm.getVillage().getCode());
				}
			} else if (!ObjectUtil.isEmpty(farmerDynamicData)
					&& farmerDynamicData.getEntityId().equalsIgnoreCase("5")) {
				if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())
						&& farmerDynamicData.getReferenceId().contains(",")) {
					List<Farmer> frmr = farmerService
							.listFarmerByIds(Arrays.asList(farmerDynamicData.getReferenceId().split(",")));
					setFarmer(String.valueOf(frmr.stream().filter(u -> u.getFirstName() != null)
							.map(p -> String.valueOf(
									p.getFirstName() + (p.getLastName() != null ? ("-" + p.getLastName()) : "")))
							.collect(Collectors.joining(","))));

					setSelectedVillage(frmr != null && !frmr.isEmpty() ? frmr.get(0).getVillage().getCode() : "");
				} else if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
					Farmer frmr = farmerService.findFarmerById(Long.valueOf(farmerDynamicData.getReferenceId()));
					setFarmer(String.valueOf(frmr.getFirstName()));
					setSelectedVillage(frmr != null && frmr.getVillage() != null ? frmr.getVillage().getCode() : "");
				}
			} else if (!ObjectUtil.isEmpty(farmerDynamicData)
					&& farmerDynamicData.getEntityId().equalsIgnoreCase("2")) {
				farmerDynamicData.setTxnDate(!StringUtil.isEmpty(farmerDynamicData.getDate())
						? DateUtil.convertDateToString(farmerDynamicData.getDate(), DateUtil.DATE_TIME_FORMAT) : "");
				if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
					Farm farm = farmerService.findFarmById(Long.valueOf(farmerDynamicData.getReferenceId()));
					setFarmer(farm != null ? String.valueOf(farm.getFarmer().getFirstName()) : "");
					setAddress(farm != null ? String.valueOf(farm.getFarmer().getAddress() + ","
							+ farm.getFarmer().getCity().getName() + "," + farm.getFarmer().getVillage().getName())
							: "");
					setArea(farm.getFarmDetailedInfo().getProposedPlantingArea());
					setFarmList(farm != null ? String.valueOf(farm.getFarmName()) : "");
					setSelectedVillage(farm.getFarmer().getVillage().getName());
				}
			} else if (!ObjectUtil.isEmpty(farmerDynamicData)
					&& farmerDynamicData.getEntityId().equalsIgnoreCase("3")) {
				if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
					Warehouse w = getWarehouse(String.valueOf(farmerDynamicData.getReferenceId()));
					setGroup(w != null ? w.getName() : "");

				}
			} else if (!ObjectUtil.isEmpty(farmerDynamicData)
					&& farmerDynamicData.getEntityId().equalsIgnoreCase("4")) {
				farmerDynamicData.setTxnDate(!StringUtil.isEmpty(farmerDynamicData.getDate())
						? DateUtil.convertDateToString(farmerDynamicData.getDate(), DateUtil.DATE_TIME_FORMAT) : "");
				if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
					Farm farm = farmerService.findFarmById(Long.valueOf(farmerDynamicData.getReferenceId()));
					setFarmer(String.valueOf(farm.getFarmer().getFirstName()));
					setFarmList(String.valueOf(farm.getFarmName()));
					setGroup(String.valueOf(farm.getFarmer().getSamithi().getName()));
					setSelectedVillage(farm.getFarmer().getVillage().getName());
				}
				setInspectionStatus(farmerDynamicData.getConversionStatus());

				/*
				 * if (farmerDynamicData.getConversionStatus() != null &&
				 * !StringUtil.isEmpty(farmerDynamicData.getConversionStatus()))
				 * { if
				 * (farmerDynamicData.getConversionStatus().equalsIgnoreCase("1"
				 * )) { farmerDynamicData.setInspectionStatus(getLocaleProperty(
				 * "Approved")); } else {
				 * farmerDynamicData.setInspectionStatus(getLocaleProperty(
				 * "Declined")); } }
				 */
				if (farmerDynamicData.getConversionStatus() != null
						&& !StringUtil.isEmpty(farmerDynamicData.getConversionStatus())
						&& farmerDynamicData.getConversionStatus().equalsIgnoreCase("1")) {

					setIcsType(farmerDynamicData.getIcsName());
				} else {
					setCorrectiveActionPlan(!StringUtil.isEmpty(farmerDynamicData.getCorrectiveActionPlan())
							? farmerDynamicData.getCorrectiveActionPlan() : "");
				}
				if (!ObjectUtil.isEmpty(farmerDynamicData.getFarmIcs())) {
					setInsDate(DateUtil.convertDateToString(farmerDynamicData.getFarmIcs().getInspectionDate(),
							getGeneralDateFormat()));
					setInspectorName(farmerDynamicData.getFarmIcs().getInspectorName());
					setInspectorMobile(farmerDynamicData.getFarmIcs().getInspectorMobile());
					setInsType(!StringUtil.isEmpty(farmerDynamicData.getFarmIcs().getInsType())
							? getCatlogueValueByCode(farmerDynamicData.getFarmIcs().getInsType()).getName() : "");
					// CertificateCategory cc =
					// certificationService.findCertificateCategoryByCode(farmerDynamicData.getFarmIcs().getScope());
					setScope(getCatlogueValueByCode(farmerDynamicData.getFarmIcs().getScope()).getName());
					setTotLand(farmerDynamicData.getFarmIcs().getTotalLand());
					setOrgLand(farmerDynamicData.getFarmIcs().getOrganicLand());
					setTotSite(farmerDynamicData.getFarmIcs().getTotalSite());
					setSeason(farmerDynamicData.getFarmIcs().getSeason());
				}
			} else if (!ObjectUtil.isEmpty(farmerDynamicData)
					&& farmerDynamicData.getEntityId().equalsIgnoreCase("1")) {
				if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
					Farm farm = farmerService.findFarmById(Long.valueOf(farmerDynamicData.getReferenceId()));
					setFarmer(String.valueOf(farm.getFarmer().getFirstName()));
				}
			} else if (!ObjectUtil.isEmpty(farmerDynamicData)
					&& farmerDynamicData.getEntityId().equalsIgnoreCase("3")) {
				if (!StringUtil.isEmail(farmerDynamicData.getReferenceId())) {
					Warehouse sam = locationService.findSamithiById(Long.valueOf(farmerDynamicData.getReferenceId()));
					setGroup(sam.getName());
				}
			} else if (!ObjectUtil.isEmpty(farmerDynamicData)
					&& farmerDynamicData.getEntityId().equalsIgnoreCase("6")) {
				if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
					FarmCrops farm = farmerService.findByFarmCropsId(Long.valueOf(farmerDynamicData.getReferenceId()));
					setFarmer(String.valueOf(farm.getFarm().getFarmer().getFirstName()));
					setFarmList(String.valueOf(farm.getFarm().getFarmName()));
					setSelectedVillage(String.valueOf(farm.getFarm().getFarmer().getVillage().getName()));
					setFarmCropList(farm.getProcurementVariety());

				}
			}

			if (farmerDynamicData.getSeason() != null) {
				// HarvestSeason season =
				// getSeason(farmerDynamicData.getSeason());

				HarvestSeason season = getSeason(farmerDynamicData.getSeason());
				setSeason(season.getName());
			}
			setCurrentPage(getCurrentPage());
			id = null;
			command = UPDATE;
			request.setAttribute(HEADING, getText(UPDATE));
			return INPUT;
		} else {
			if (farmerDynamicData != null) {

				FarmerDynamicData temp = farmerService.findFarmerDynamicData(String.valueOf(farmerDynamicData.getId()));
				if (temp == null) {
					addActionError(NO_RECORD);
					return REDIRECT;
				}
				fdMap = new HashMap<>();
				dm = farmerService.findDynamicMenusByMType(txnType).get(0);
				fieldConfigMap = new LinkedHashMap<>();
				Map<String, String> ActMap = new HashMap<>();
				if (dm.getIsScore() != null && (dm.getIsScore() == 1 || dm.getIsScore() == 2 || dm.getIsScore() == 3)) {
					farmerDynamicData.setIsScore(dm.getIsScore());
					farmerDynamicData.setScoreValue(new HashMap<>());
				}
				dm.getDynamicFieldConfigs().stream().forEach(section -> {
					section.getField().setfOrder(section.getOrder());
					fieldConfigMap.put(section.getField().getCode(), section.getField());
					if (section.getDynamicFieldScoreMap() != null && !section.getDynamicFieldScoreMap().isEmpty()) {
						farmerDynamicData.getScoreValue().put(section.getField().getCode(), section
								.getDynamicFieldScoreMap().stream()
								.collect(Collectors.toMap(DynamicFieldScoreMap::getCatalogueCode, u -> String.valueOf(
										String.valueOf(u.getScore()) + "~" + String.valueOf(u.getPercentage())))));
					}
					if (section.getField().getFollowUp() == 1 || section.getField().getFollowUp() == 2) {
						ActMap.put(section.getField().getCode(), section.getField().getParentActField());
					}
				});

				fieldTypeMap = new HashMap<>();
				// farmerDynamicData =
				// farmerService.findFarmerDynamicDataBySeason(txnType,
				// selectedId, season);

				/*
				 * if (temp != null) { fdMap =
				 * temp.getFarmerDynamicFieldsValues().stream()
				 * .collect(Collectors.groupingBy(FarmerDynamicFieldsValue::
				 * getFieldName)); temp.getFarmerDynamicFieldsValues()
				 * .removeAll(temp.getFarmerDynamicFieldsValues().stream().
				 * filter( map -> (map.getIsMobileAvail() != null &&
				 * map.getIsMobileAvail().equals("2")))
				 * .collect(Collectors.toList())); List<Object[]> fdfvList =
				 * clientService.listMaxTypeByRefId(temp.getReferenceId());
				 * fieldTypeMap = fdfvList.stream()
				 * .collect(Collectors.toMap(obj -> String.valueOf(obj[0]), obj
				 * -> String.valueOf(obj[1])));
				 * 
				 * }
				 */

				if (getEntityType().equalsIgnoreCase("4")) {
					temp.setConversionStatus(getInspectionStatus());
					temp.setCorrectiveActionPlan(getCorrectiveActionPlan());
					temp.setIcsName(getIcsType());

					FarmIcsConversion exist = new FarmIcsConversion();
					if (!ObjectUtil.isEmpty(temp.getFarmIcs())) {

						exist = farmerService.findFarmIcsConversionByFarmIcsConvId(temp.getFarmIcs().getId());

						// exist.setInspectionDate(DateUtil.convertStringToDate(insDate,
						// getGeneralDateFormat()));
						// exist.setInspectorMobile(inspectorMobile);
						// exist.setInspectorName(inspectorName);
						// exist.setInsType(insType);
						// exist.setScope(scope);
						// exist.setIcsType(icsType);
						exist.setIcsType(!StringUtil.isEmpty(icsType) ? icsType : "-1");
						exist.setStatus(Integer.parseInt(inspectionStatus));
						exist.setQualified(Integer.valueOf(inspectionStatus));
						exist.setSeason(season);
						/*
						 * exist.setInsType(!StringUtil.isEmpty(temp.getFarmIcs(
						 * ).getInsType()) ?
						 * getCatlogueValueByCode(temp.getFarmIcs().getInsType()
						 * ).getName() : "");
						 */
						// exist.setScope(getCatlogueValueByCode(temp.getFarmIcs().getScope()).getName());
						// exist.setTotalLand(totLand);
						// exist.setOrganicLand(orgLand);
						// exist.setTotalSite(totSite);
						if (getCurrentTenantId().equalsIgnoreCase("wilmar")) {
							if (!ObjectUtil.isEmpty(exist.getFarm())
									&& !ObjectUtil.isEmpty(exist.getFarm().getFarmer().getIsCertifiedFarmer())
									&& exist.getFarm().getFarmer().getIsCertifiedFarmer() == 1
									&& !StringUtil.isEmpty(exist.getIcsType())
									&& exist.getIcsType().equalsIgnoreCase("3")) {
								exist.setOrganicStatus("3");
							} else {
								exist.setOrganicStatus("0");
							}
						}

					}
					farmerService.update(exist);
				}

				// temp.setReferenceId(farmList);
				temp.setTxnUniqueId(DateUtil.getRevisionNumber());
				// temp.setSeason(season);
				// temp.setStatus("0");
				// temp.setCreatedDate(new Date());
				// temp.setCreatedUser(getUsername());
				String nowDate = DateUtil.convertDateToString(new Date(), (getGeneralDateFormat() + " HH:mm:ss"));
				temp.setUpdatedDate(DateUtil.convertStringToDate(nowDate, (getGeneralDateFormat() + " HH:mm:ss")));
				temp.setUpdatedUser(getUsername());
				/*
				 * temp.setEntityId(entityType); temp.setTxnType(txnType);
				 */ temp.setFarmerDynamicFieldsValues(
						getDynamicFieldValues(temp.getReferenceId(), new HashSet<>(), "2"));
				temp.setProfileUpdateFields(farmerDynamicData.getProfileUpdateFields());

				farmerService.saveOrUpdate(temp, fdMap, fieldConfigMap);
				if (fieldConfigMap.values().stream().anyMatch(p -> Arrays.asList("4").contains(p.getIsMobileAvail())
						&& p.getFormula() != null && !StringUtil.isEmpty(p.getFormula()))) {
					farmerService.processCustomisedFormula(temp, fieldConfigMap);
				}

				if (!temp.getTxnType().equals("381")) {
					farmerService.deleteChildObjects(farmerDynamicData.getTxnType());
				}

				/* farmerService.updateDynamicFarmerFieldComponentType(); */

				/*
				 * if (!StringUtil.isEmpty(getIcsType())) {
				 * farmerService.updateFarmICSStatusByFarmId(Long.valueOf(temp.
				 * getReferenceId()), getIcsType()); }
				 */
				// farmerService.update(temp);

			}
		}
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		Calendar currentDate = Calendar.getInstance();
		Calendar cal = (Calendar) currentDate.clone();
		cal.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) - 1);
		super.startDate = df.format(cal.getTime());
		super.endDate = df.format(currentDate.getTime());

		daterange = super.startDate + " - " + super.endDate;
		command = "list";
		return LIST;
	}

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

	public Map<Integer, String> getInspectionStatusList() {
		inspectionStatuslist.put(1, getLocaleProperty("Approved"));
		inspectionStatuslist.put(0, getLocaleProperty("Declined"));
		return inspectionStatuslist;
	}

	public FarmerDynamicData getFilter() {
		return filter;
	}

	public void setFilter(FarmerDynamicData filter) {
		this.filter = filter;
	}

	public ICatalogueService getCatalogueService() {
		return catalogueService;
	}

	public void setCatalogueService(ICatalogueService catalogueService) {
		this.catalogueService = catalogueService;
	}

	public List<Object[]> getDynamicData() {
		return dynamicData;
	}

	public void setDynamicData(List<Object[]> dynamicData) {
		this.dynamicData = dynamicData;
	}

	public String getTxnType() {
		if (request.getParameter("txnType") != null && !StringUtil.isEmpty(request.getParameter("txnType"))) {
			txnType = request.getParameter("txnType");
		}
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getMainGridCols() {
		if (StringUtil.isEmpty(mainGridCols)) {
			formMainGridCols();
		}
		return mainGridCols;
	}

	public void setMainGridCols(String mainGridCols) {
		this.mainGridCols = mainGridCols;
	}

	public List<Object[]> getDynamicSectionConfigHeader() {
		return dynamicSectionConfigHeader;
	}

	public void setDynamicSectionConfigHeader(List<Object[]> dynamicSectionConfigHeader) {
		this.dynamicSectionConfigHeader = dynamicSectionConfigHeader;
	}

	public FarmerDynamicData getFarmerDynamicData() {
		return farmerDynamicData;
	}

	public void setFarmerDynamicData(FarmerDynamicData farmerDynamicData) {
		this.farmerDynamicData = farmerDynamicData;
	}

	public Map<Integer, String> getInspectionStatuslist() {
		return inspectionStatuslist;
	}

	public void setInspectionStatuslist(Map<Integer, String> inspectionStatuslist) {
		this.inspectionStatuslist = inspectionStatuslist;
	}

	public Map<Integer, String> getIcsStatusList() {
		String[] icsStatus = getLocaleProperty("icsStatusList").split(",");
		for (int i = 0; i < icsStatus.length; i++)
			icsStatusList.put(i, icsStatus[i]);

		return icsStatusList;
	}

	public void setIcsStatusList(Map<Integer, String> icsStatusList) {
		this.icsStatusList = icsStatusList;
	}

	public Map<String, String> getFarmerList() {

		Map<String, String> returnMap = new HashMap<String, String>();

		List<Object[]> farmerList = new ArrayList<Object[]>();
		farmerList = farmerService.listFarmerIDAndName();
		if (!ObjectUtil.isListEmpty(farmerList)) {
			for (Object[] farmerObj : farmerList) {
				returnMap.put(String.valueOf(farmerObj[0]), String.valueOf(farmerObj[2]));
			}
		}
		return returnMap;
	}

	public void populateFarm() throws Exception {

		if (getCurrentTenantId().equalsIgnoreCase("wilmar")) {
			/*
			 * if(command == "create"){ if (!StringUtil.isEmpty(selectedFarmer))
			 * { List<Object[]> farmList =
			 * farmerService.listFarmFieldsByFarmerIdAndNonOrganic(Long.valueOf(
			 * selectedFarmer)); JSONArray farmArr = new JSONArray();
			 * farmList.stream().filter(obj ->
			 * !ObjectUtil.isEmpty(obj)).forEach(obj -> {
			 * farmArr.add(getJSONObject(obj[0].toString(), obj[2].toString()));
			 * }); sendAjaxResponse(farmArr); } } else{
			 */
			if (!StringUtil.isEmpty(selectedFarmer)) {
				Farmer farmer = farmerService.findFarmerById(Long.valueOf(selectedFarmer));
				if (farmer.getIsCertifiedFarmer() == 1) {
					List<Object[]> farmList = farmerService
							.listFarmFieldsByFarmerIdAndNonOrganic(Long.valueOf(selectedFarmer));
					JSONArray farmArr = new JSONArray();
					farmList.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
						farmArr.add(getJSONObject(obj[0].toString(), obj[2].toString()));
					});
					sendAjaxResponse(farmArr);
				} else {

					List<Object[]> farmList = farmerService
							.listFarmFieldsByFarmerIdNonCertified(Long.valueOf(selectedFarmer));
					JSONArray farmArr = new JSONArray();
					farmList.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
						farmArr.add(getJSONObject(obj[0].toString(), obj[2].toString()));
					});
					sendAjaxResponse(farmArr);

				}

			}
		} else {
			if (!StringUtil.isEmpty(selectedFarmer)) {
				List<Object[]> farmList = farmerService.listFarmFieldsByFarmerId(Long.valueOf(selectedFarmer));
				JSONArray farmArr = new JSONArray();
				farmList.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
					farmArr.add(getJSONObject(obj[0].toString(), obj[2].toString()));
				});
				sendAjaxResponse(farmArr);
			}
		}
	}

	public void populateFarmCrops() throws Exception {

		if (!StringUtil.isEmpty(selectedFarm)) {
			List<FarmCrops> farmList = farmerService.listFarmCropsByFarmId(Long.valueOf(selectedFarm));
			JSONArray farmArr = new JSONArray();
			farmList.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
				farmArr.add(getJSONObject(obj.getId(), obj.getProcurementVariety().getProcurementProduct().getName()
						+ " - " + obj.getProcurementVariety().getName().toString()));
			});
			sendAjaxResponse(farmArr);
		}

	}

	FarmIcsConversion icsList = new FarmIcsConversion();

	public void populateICSType() throws Exception {

		if (!StringUtil.isEmpty(selectedFarm)) {
			icsList = farmerService.findFarmIcsConversionByFarmId(Long.valueOf(selectedFarm));
			JSONArray icsArr = new JSONArray();
			if (ObjectUtil.isEmpty(icsList)) {
				FarmIcsConversion fic = new FarmIcsConversion();
				Farm farm = farmerService.findFarmByID(Long.valueOf(selectedFarm));
				fic.setFarm(!ObjectUtil.isEmpty(farm) ? farm : null);
				fic.setIcsType("0");
				fic.setStatus(0);
				farmerService.save(fic);
				icsList = farmerService.findFarmIcsConversionByFarmId(Long.valueOf(farm.getId()));
			}

			if (!ObjectUtil.isEmpty(icsList)) {
				String[] icsStatus = getLocaleProperty("icsStatusList").split(",");
				AtomicInteger countr = new AtomicInteger(0);
				if (Integer.parseInt(icsList.getIcsType()) == (icsStatus.length - 1)) {
					icsArr.add(getJSONObject("3", icsStatus[3]));

				} else {
					Arrays.asList(icsStatus).stream().forEach(str -> {
						if (countr.get() > Integer.parseInt(icsList.getIcsType())) {
							icsArr.add(getJSONObject(countr.get(), str));
							countr.incrementAndGet();
						} else {
							countr.incrementAndGet();
						}
					});
				}
				sendAjaxResponse(icsArr);
			}

		}

	}

	public String getSelectedFarmer() {
		return selectedFarmer;
	}

	public void setSelectedFarmer(String selectedFarmer) {
		this.selectedFarmer = selectedFarmer;
	}

	public String getSelectedFarm() {
		return selectedFarm;
	}

	public void setSelectedFarm(String selectedFarm) {
		this.selectedFarm = selectedFarm;
	}

	public String getInspectionStatus() {
		return inspectionStatus;
	}

	public void setInspectionStatus(String inspectionStatus) {
		this.inspectionStatus = inspectionStatus;
	}

	public String getIcsType() {
		return icsType;
	}

	public void setIcsType(String icsType) {
		this.icsType = icsType;
	}

	public String getCorrectiveActionPlan() {
		return correctiveActionPlan;
	}

	public void setCorrectiveActionPlan(String correctiveActionPlan) {
		this.correctiveActionPlan = correctiveActionPlan;
	}

	public String getSelectedId() {
		return selectedId;
	}

	public void setSelectedId(String selectedId) {
		this.selectedId = selectedId;
	}

	public String getFarmList() {
		return farmList;
	}

	public void setFarmList(String farmList) {
		this.farmList = farmList;
	}

	private Farmer getFarmer(String farmerId) {
		Farmer farmer = farmerService.findFarmerById(Long.valueOf(farmerId));
		return farmer;
	}

	private Warehouse getWarehouse(String warehouseId) {
		// Warehouse warehouse = locationService.findSamithiByCode(warehouseId);
		Warehouse warehouse = locationService.findSamithiById(Long.valueOf(warehouseId));
		return warehouse;
	}

	private Farm getFarm(String farmId) {
		Farm farm = farmerService.findFarmById(Long.valueOf(farmId));
		return farm;
	}

	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(String id, String name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name.trim());
		return jsonObject;
	}

	public List<DynamicFieldReportJoinMap> getDynamicFieldReportJoinMapList() {
		if (ObjectUtil.isListEmpty(dynamicFieldReportJoinMapList)) {
			dynamicFieldReportJoinMapList = new ArrayList<>();
			/*
			 * dynamicFieldReportJoinMapList = farmerService
			 * .findDynamicFieldReportJoinMapByEntityAndTxn(getEntityType(),
			 * getTxnType()).stream() .collect(Collectors.toList());
			 */
			dynamicFieldReportJoinMapList = farmerService.findDynamicFieldReportJoinMapByEntity(getEntityType())
					.stream().collect(Collectors.toList());
		}
		return dynamicFieldReportJoinMapList;
	}

	public List<DynamicFieldReportConfig> getDynamicFieldReportConfigList() {
		if (ObjectUtil.isListEmpty(dynamicFieldReportConfigList)) {
			dynamicFieldReportConfigList = new ArrayList<>();
			/*
			 * dynamicFieldReportConfigList = farmerService
			 * .findDynamicFieldReportConfigByEntityAndTxn(getEntityType(),
			 * getTxnType()).stream() .collect(Collectors.toList());
			 */

			dynamicFieldReportConfigList = farmerService.findDynamicFieldReportConfigByEntity(getEntityType()).stream()
					.collect(Collectors.toList());
		}
		return dynamicFieldReportConfigList;
	}

	public List<DynamicFeildMenuConfig> getDynamicMenuConfigList() {
		if (ObjectUtil.isListEmpty(dynamicMenuConfigList)) {
			dynamicMenuConfigList = new ArrayList<>();
			dynamicMenuConfigList = farmerService.findDynamicMenusByType(getTxnType()).stream()
					.collect(Collectors.toList());
			dynamicMenuConfigList.forEach(u -> {
				u.setName(u.getLangName(getLoggedInUserLanguage()));
			});
		}
		return dynamicMenuConfigList;
	}

	public String getFarmer() {
		return farmer;
	}

	public void setFarmer(String farmer) {
		this.farmer = farmer;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Map<Long, String> getFarmListt() {

		Map<Long, String> stateMap = new LinkedHashMap<Long, String>();
		if (!StringUtil.isEmpty(getFarmer()) && StringUtil.isLong(getFarmer())) {
			farms = farmerService.listFarmFieldsByFarmerId(Long.valueOf(getFarmer()));
		} else {
			farms = farmerService.listFarmInfo();
		}
		if (!ObjectUtil.isEmpty(farms)) {
			for (Object[] farm : farms) {
				if (farm[2] != null) {
					stateMap.put(Long.valueOf(farm[0].toString()), farm[2].toString());
				}
			}

		}

		return stateMap;

	}

	public Map<Long, String> getGroupList() {

		samithi = locationService.listSamithiesBasedOnType();
		Map<Long, String> samithiMap = new LinkedHashMap<>();
		samithiMap = samithi.stream().collect(Collectors.toMap(Warehouse::getId, Warehouse::getName));
		return samithiMap;
	}

	public Map<Long, String> getCropList() {

		List<ProcurementProduct> psList = productDistributionService.listProcurementProduct();
		Map<Long, String> samithiMap = new LinkedHashMap<>();
		samithiMap = psList.stream().collect(Collectors.toMap(ProcurementProduct::getId, ProcurementProduct::getName));
		return samithiMap;
	}

	public Map<Long, String> getVarietyList() {

		List<ProcurementVariety> psList = productDistributionService.listProcurementVariety();
		Map<Long, String> samithiMap = new LinkedHashMap<>();
		samithiMap = psList.stream().collect(Collectors.toMap(ProcurementVariety::getId, ProcurementVariety::getName));
		return samithiMap;
	}

	public Map<String, String> getSeasonList() {

		Map<String, String> seasonMap = new LinkedHashMap<String, String>();

		List<Object[]> seasonList = farmerService.listSeasonCodeAndName();

		for (Object[] obj : seasonList) {
			seasonMap.put(String.valueOf(obj[0]), String.valueOf(obj[1]));
		}
		return seasonMap;

	}

	public int getInspectionStatusDefaultValue() {

		return (!ObjectUtil.isEmpty(this.farmerDynamicData)
				? !StringUtil.isEmpty(this.farmerDynamicData.getConversionStatus())
						? Integer.valueOf(this.farmerDynamicData.getConversionStatus()) : FarmerDynamicData.APPROVED
				: FarmerDynamicData.APPROVED);
	}

	public Map<String, String> getIcsList() {
		// List<Object> farms = new ArrayList<Object>();
		Map<String, String> icsMap = new LinkedHashMap<String, String>();
		/*
		 * if (!ObjectUtil.isEmpty(farmerDynamicData) &&
		 * !StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
		 * FarmIcsConversion icsList = farmerService
		 * .findFarmIcsConversionByFarmId(Long.valueOf(farmerDynamicData.
		 * getReferenceId())); if
		 * (!StringUtil.isEmpty(farmerDynamicData.getIcsName())) { String[]
		 * icsStatus = getLocaleProperty("icsStatusList").split(",");
		 * AtomicInteger countr = new AtomicInteger(0);
		 * 
		 * if (Integer.parseInt(farmerDynamicData.getIcsName()) ==
		 * (icsStatus.length - 1)) {
		 * 
		 * icsMap.put("3", icsStatus[3]);
		 * 
		 * } else { Arrays.asList(icsStatus).stream().forEach(str -> { if
		 * (countr.get() >= Integer.parseInt(farmerDynamicData.getIcsName())) {
		 * // icsArr.add(getJSONObject(countr.get(), str)); //
		 * countr.incrementAndGet(); icsMap.put(String.valueOf(countr.get()),
		 * str); } countr.incrementAndGet(); });
		 * 
		 * } } else if( icsList!=null) {
		 * 
		 * String[] icsStatus = getLocaleProperty("icsStatusList").split(",");
		 * AtomicInteger countr = new AtomicInteger(0);
		 * 
		 * if ( Integer.parseInt(icsList.getIcsType()) == (icsStatus.length -
		 * 1)) {
		 * 
		 * icsMap.put("3", icsStatus[3]);
		 * 
		 * } else { Arrays.asList(icsStatus).stream().forEach(str -> { if
		 * (countr.get() > Integer.parseInt(icsList.getIcsType())) { //
		 * icsArr.add(getJSONObject(countr.get(), str)); //
		 * countr.incrementAndGet(); icsMap.put(String.valueOf(countr.get()),
		 * str); } countr.incrementAndGet(); });
		 * 
		 * }
		 * 
		 * }
		 * 
		 * }
		 */
		String[] icsStatus = getLocaleProperty("icsStatusList").split(",");
		for (int i = 0; i < icsStatus.length; i++)
			icsMap.put(String.valueOf(i), icsStatus[i]);

		return icsMap;

	}

	public Integer getSeasonType() {
		return seasonType;
	}

	public void setSeasonType(Integer seasonType) {
		this.seasonType = seasonType;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	@Override
	public void prepare() throws Exception {
		List<DynamicFeildMenuConfig> syList = getDynamicMenuConfigList();
		String name = syList != null && syList.size() > 0 ? syList.get(0).getName() : "";
		String url = request.getRequestURL().toString();

		url = url == null || StringUtil.isEmpty(url) ? request.getHeader("referer") : url;

		if (url != null && url.contains("Report")) {
			url = getLocaleProperty("report") + "~#," + name + "~dynmaicCertificationReport_list.action?txnType="
					+ getTxnType();
		} else {
			url = getLocaleProperty("service") + "~#," + name + "~dynamicCertification_list.action?txnType="
					+ getTxnType();
		}
		// url = url == null || StringUtil.isEmpty(url) ?
		// request.getHeader("referer") : url;
		setInfoName(name);
		request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(url));
	}

	public List<Warehouse> getSamithi() {
		return samithi;
	}

	public void setSamithi(List<Warehouse> samithi) {
		this.samithi = samithi;
	}

	public String getInfoName() {
		return infoName;
	}

	public void setInfoName(String infoName) {
		this.infoName = infoName;
	}

	public String getSeasonCode() {
		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
	}

	public String populateXLS() throws IOException {
		InputStream is = getExportDataStream();
		// setXlsFileName(getText("dynamicReportList") +
		// fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		xlsFileName = xlsFileName.replaceAll("\\s", "");
		fileMap.put(xlsFileName.trim(), is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("dynamicReportList"), fileMap, ".xls"));
		return "xls";
	}

	private String selectedVillage;
	private String selectedState;
	private String selectedFarmerId;
	@SuppressWarnings("unchecked")
	int lastCol = 0;
	int mainGridIterator = 0;
int rowCount=0;
String secCols = "";
	public InputStream getExportDataStream() throws IOException {
		LinkedHashMap<String, Integer> fiedlList = new LinkedHashMap<>();
		Map<String, List<DynamicFieldConfig>> parentMap = new HashMap<>();
		secCols = "";
		Row titleRow;
		int colCount, titleRow1 = 4, titleRow2 = 6;
		AtomicInteger l = new AtomicInteger(1);
		AtomicInteger cnt = new AtomicInteger(0);
		AtomicInteger rowCnt = new AtomicInteger(1);
		AtomicInteger rownum = new AtomicInteger(1);

		int secount = 0;
		//StringBuilder secCols = new StringBuilder();
		final Map<String, Object[]> values = new HashMap<>();
		final Map<String, String> fieldsMap = new HashMap<>();

		final List<Object[]> list = new ArrayList<>();
		final Map<String, String> expDatas = new HashMap<>();
		final Map<String, String> expDatasOthe = new HashMap<>();
		final Map<Long, Map<String, String>> listItem = new HashMap<>();
		final Map<Long, Map<String, String>> other = new HashMap<>();
		final Map<String, String> fcm = new HashMap<>();
		final LinkedList<DynamicMenuFieldMap> dfm = new LinkedList();
		final Map entityMap = new HashMap<>();

		final Map<String, String> filterMap = new LinkedHashMap<>();

		if (id != null && !StringUtil.isEmpty(id)) {
			filter.setId(Long.parseLong(id));
		} else {
			if (!StringUtil.isEmpty(startDate) && !StringUtil.isEmpty(endDate)) {
				filterMap.put(getLocaleProperty("startDate"), startDate);
				filterMap.put(getLocaleProperty("endDate"), endDate);
			}

			if (!StringUtil.isEmpty(condtion) && !condtion.equals("{}")) {
				Type listType1 = new TypeToken<Map<String, String>>() {
				}.getType();
				Map<String, String> dynamicList = new Gson().fromJson(condtion, listType1);
				entityMap.put(IReportDAO.JOIN_MAP, getDynamicFieldReportJoinMapList());
				entityMap.put(IReportDAO.ENTITY, entityType);
			}

			if (!StringUtil.isEmpty(fieldCondition) && !fieldCondition.equals("{}")) {
				Type listType1 = new TypeToken<Map<String, String>>() {
				}.getType();
				Map<String, String> dynamicList = new Gson().fromJson(fieldCondition, listType1);
				dynamicList.entrySet().stream().forEach(dy -> {
					filterMap.put(dy.getKey().split("~")[1], dy.getValue().split("~")[1]);
				});

				entityMap.put("filters", dynamicList);

				entityMap.put(IClientService.PROJECTIONS_CONDITION, dynamicList);
				entityMap.put(IReportDAO.JOIN_MAP, getDynamicFieldReportJoinMapList());
				entityMap.put(IReportDAO.ENTITY, entityType);
			}

			if (filter != null && filter.getReferenceId() != null) {

			} else {
				setFilter(new FarmerDynamicData());
			}

			if (!StringUtil.isEmpty(seasonCode)) {

				filter.setSeason(seasonCode);
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

			if (!StringUtil.isEmpty(subBranchIdParam) && !subBranchIdParam.equals("0")) {
				filter.setBranch(subBranchIdParam);
			}

			filter.setTxnType(getTxnType());
			entityMap.put("isExport", "1");
			filter.setEntityMap(entityMap);
			filterMap.put(setBranchFilterMapKey(), setBranchFilterMapValue());
		}
		super.filter = filter;

		Map data = readData();
		List<FarmerDynamicData> mainGridRows = (List<FarmerDynamicData>) data.get(ROWS);
		List<Long> mainIds = mainGridRows.stream().map(u -> u.getId()).collect(Collectors.toList());
		DynamicFeildMenuConfig dy = getDynamicMenuConfigList().get(0);
		if (filter != null && filter.getReferenceId() != null && !StringUtil.isEmpty(filter.getReferenceId())) {
			if (dy.getEntity().equalsIgnoreCase("2") || dy.getEntity().equalsIgnoreCase("4")) {
				Farm f = farmerService.findFarmById(Long.valueOf(filter.getReferenceId()));
				filterMap.put(getLocaleProperty("farm"), f != null ? f.getFarmIdAndName() : "");
			} else if (dy.getEntity().equalsIgnoreCase("5") || dy.getEntity().equalsIgnoreCase("1")) {
				Farmer f = farmerService.findFarmerById(Long.valueOf(filter.getReferenceId()));
				filterMap.put(getLocaleProperty("farmer"), f != null ? f.getFirstName() + " " + f.getLastName() : "");
			} else if (dy.getEntity().equalsIgnoreCase("3")) {
				Warehouse w = locationService.findSamithiById(Long.valueOf(filter.getReferenceId()));
				filterMap.put(getLocaleProperty("profile.samithi"), w != null ? w.getName() : "");
			}
		}

		String mainGridCols = getLocaleProperty("sno") + "|";
		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(getBranchId())))) {

			if (StringUtil.isEmpty(getBranchId())) {
				mainGridCols += getLocaleProperty("Organization") + "|";
				mainGridCols += getLocaleProperty("Sub Organization") + "|";
				cnt.addAndGet(2);
			} else {
				mainGridCols += getLocaleProperty("Sub Organization") + "|";
				cnt.incrementAndGet();
			}
		} else {
			if (StringUtil.isEmpty(getBranchId())) {
				mainGridCols += getLocaleProperty("Organization") + "|";
			}
		}
		mainGridCols += getLocaleProperty("date") + "|" + getLocaleProperty("createdUser") + "|";
		cnt.addAndGet(2);
		if (dy.getIsSeason() != null && dy.getIsSeason() == 1) {
			mainGridCols += getLocaleProperty("season") + "|";

			cnt.incrementAndGet();
		}

		List<DynamicFieldReportConfig> DynamicFieldReportConfigList = farmerService
				.findDynamicFieldReportConfigByEntity(dy.getEntity());

		if (!ObjectUtil.isListEmpty(DynamicFieldReportConfigList)) {
			StringBuilder sb = new StringBuilder();
			DynamicFieldReportConfigList.stream().forEach(dynamicFieldReportConfig -> {
				cnt.incrementAndGet();
				sb.append(getLocaleProperty(dynamicFieldReportConfig.getLabel()) + "|");
			});
			mainGridCols += sb.toString();
		}
		if (dy.getIsScore() != null && dy.getIsScore() == 1) {
			mainGridCols += getLocaleProperty("totalScore") + "|";
			cnt.incrementAndGet();
		} else if (dy.getIsScore() != null && dy.getIsScore() == 2) {
			mainGridCols += getLocaleProperty("Result") + "|" + getLocaleProperty("Year") + "|";
			cnt.addAndGet(2);
		}

		secount = cnt.get();
		List<String> branches = getBranches(getBranchId());
		dfm.clear();
		if (branches != null && !branches.isEmpty()) {
			dfm.addAll(
					getDynamicMenuConfigList().stream()
							.flatMap(u -> u.getDynamicFieldConfigs().stream().filter(fff -> fff.getBranchId() == null
									|| !Collections.disjoint(Arrays.asList(fff.getBranchId().split(",")), branches)))
							.collect(Collectors.toCollection(LinkedList::new)));
		} else {

			dfm.addAll(getDynamicMenuConfigList().stream().flatMap(u -> u.getDynamicFieldConfigs().stream())
					.collect(Collectors.toCollection(LinkedList::new)));
		}
		Map<Long, String> dmFieldMap = dfm.stream().collect(
				Collectors.toMap(obj -> Long.valueOf(obj.getField().getId()), obj -> obj.getField().getCode()));
		dfm.stream().forEach(dynamicFieldReportConfig -> {

			if (!ObjectUtil.isEmpty(dynamicFieldReportConfig.getField().getReferenceId())
					&& dmFieldMap.containsKey(dynamicFieldReportConfig.getField().getReferenceId())
					&& !Arrays
							.asList(DynamicFieldConfig.COMPONENT_TYPES.BUTTON.ordinal(),
									DynamicFieldConfig.COMPONENT_TYPES.VIDEO.ordinal(),
									DynamicFieldConfig.COMPONENT_TYPES.AUDIO.ordinal(),
									DynamicFieldConfig.COMPONENT_TYPES.PHOTO_CERTIFICATION.ordinal())
							.contains(Integer.valueOf(dynamicFieldReportConfig.getField().getComponentType()))) {
				if (parentMap.containsKey(dmFieldMap.get(dynamicFieldReportConfig.getField().getReferenceId()))) {
					List<DynamicFieldConfig> li = parentMap
							.get(dmFieldMap.get(dynamicFieldReportConfig.getField().getReferenceId()));
					li.add(dynamicFieldReportConfig.getField());
					parentMap.put(dmFieldMap.get(dynamicFieldReportConfig.getField().getReferenceId()), li);
				} else {
					List<DynamicFieldConfig> li = new ArrayList<>();
					li.add(dynamicFieldReportConfig.getField());
					parentMap.put(dmFieldMap.get(dynamicFieldReportConfig.getField().getReferenceId()), li);
				}

			}
		});

		List<Object[]> maxLis = farmerService.listMaxTypez(mainGridRows.stream().map(u -> u.getId())
				.collect(Collectors.toList()).stream().collect(Collectors.toList()), txnType);

		final Map<Long, Integer> fieldDetaMap = maxLis.stream()
				.collect(Collectors.toMap(u -> Long.valueOf(u[0].toString()), u -> Integer.valueOf(u[1].toString())));

		/*
		 * fieldDetaMap = fdv.stream().filter(u -> u.getParentId() != null &&
		 * u.getParentId() != 0) .collect(Collectors.groupingBy(u ->
		 * u.getFieldName(), Collectors.groupingBy(u -> u.getReferenceId(),
		 * Collectors.mapping(u -> u.getTypez(), Collectors.toList()))));
		 */ if (!ObjectUtil.isEmpty(dy) && !ObjectUtil.isListEmpty(dfm)) {
			StringBuilder sb = new StringBuilder();
			dfm.stream().forEach(dynamicFieldReportConfig -> {

				if (dynamicFieldReportConfig.getField().getComponentType().equals("8")) {
					if (parentMap.containsKey(dynamicFieldReportConfig.getField().getCode())) {
						List<DynamicFieldConfig> li = parentMap.get(dynamicFieldReportConfig.getField().getCode());
						l.set(1);
						if (fieldDetaMap.containsKey(dynamicFieldReportConfig.getField().getId())) {
							Integer tem = fieldDetaMap.get(dynamicFieldReportConfig.getField().getId());
							if (tem > l.intValue()) {
								l.set(tem);
							}

						}

						fiedlList.put(dynamicFieldReportConfig.getField().getCode(), cnt.incrementAndGet());

						for (rowCnt.set(1); rowCnt.intValue() <= l.intValue(); rowCnt.incrementAndGet()) {
							li.stream().forEach(u -> {
								if (!u.getComponentType().equals(DynamicFieldConfig.COMPONENT_TYPES.BUTTON)) {
									secCols += u.getDynamicSectionConfig().getLangName(getLoggedInUserLanguage()) + ",";
									sb.append(getLocaleProperty(u.getLangName(getLoggedInUserLanguage())) + "_"
											+ rowCnt.intValue() + "|");
								}
							});
						}
						cnt.set(cnt.intValue() + ((li.size()) * l.intValue()) - 1);
					}

				} else if (ObjectUtil.isEmpty(dynamicFieldReportConfig.getField().getReferenceId()) && !Arrays
						.asList(DynamicFieldConfig.COMPONENT_TYPES.BUTTON.ordinal(),
								DynamicFieldConfig.COMPONENT_TYPES.VIDEO.ordinal(),
								DynamicFieldConfig.COMPONENT_TYPES.AUDIO.ordinal(),
								DynamicFieldConfig.COMPONENT_TYPES.PHOTO_CERTIFICATION.ordinal())
						.contains(Integer.valueOf(dynamicFieldReportConfig.getField().getComponentType()))) {

					fiedlList.put(dynamicFieldReportConfig.getField().getCode(), cnt.incrementAndGet());
					secCols += dynamicFieldReportConfig.getField().getDynamicSectionConfig()
							.getLangName(getLoggedInUserLanguage()) + ",";
					sb.append(getLocaleProperty(
							dynamicFieldReportConfig.getField().getLangName(getLoggedInUserLanguage())) + "|");
				}

			});

			mainGridCols += sb.toString().trim();
		}

		mainGridCols = StringUtil.removeLastChar(mainGridCols.toString(), '|').trim();
		secCols = StringUtil.removeLastComma(secCols.toString()).trim();
		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(dy.getLangName(getLoggedInUserLanguage()));
		setXlsFileName(dy.getLangName(getLoggedInUserLanguage()) + fileNameDateFormat.format(new Date()));
		/** Defining Style7s */
		XSSFCellStyle headerStyle = (XSSFCellStyle) workbook.createCellStyle();
	
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		XSSFCellStyle filterStyle = (XSSFCellStyle) workbook.createCellStyle();
		filterStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		filterStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		filterStyle.setWrapText(true);
		XSSFCellStyle answerStyle = (XSSFCellStyle) workbook.createCellStyle();
		answerStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		answerStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		answerStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		answerStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		answerStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		answerStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		answerStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		answerStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		answerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		XSSFCellStyle sectionStyle = (XSSFCellStyle) workbook.createCellStyle();
		sectionStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		sectionStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		sectionStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		sectionStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		sectionStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		sectionStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		sectionStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		sectionStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		sectionStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		sectionStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);

		XSSFCellStyle answerSubHeader = (XSSFCellStyle) workbook.createCellStyle();
		answerSubHeader.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		answerSubHeader.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		answerSubHeader.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		answerSubHeader.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		answerSubHeader.setBorderRight(XSSFCellStyle.BORDER_THIN);
		answerSubHeader.setRightBorderColor(IndexedColors.BLACK.getIndex());
		answerSubHeader.setBorderTop(XSSFCellStyle.BORDER_THIN);
		answerSubHeader.setTopBorderColor(IndexedColors.BLACK.getIndex());
		answerSubHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		answerSubHeader.setFillForegroundColor(HSSFColor.YELLOW.index);

		XSSFCellStyle headerLabelStyle = (XSSFCellStyle) workbook.createCellStyle();
		headerLabelStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		headerLabelStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		headerLabelStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		headerLabelStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		headerLabelStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		headerLabelStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		headerLabelStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		headerLabelStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		headerLabelStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		headerLabelStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);

		XSSFColor subGridColor = new XSSFColor(new Color(204, 255, 204));
		filterStyle.setFillForegroundColor(subGridColor);
		filterStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		XSSFCellStyle subGridHeader = (XSSFCellStyle) workbook.createCellStyle();
		subGridHeader.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		subGridHeader.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		subGridHeader.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		subGridHeader.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		subGridHeader.setBorderRight(XSSFCellStyle.BORDER_THIN);
		subGridHeader.setRightBorderColor(IndexedColors.BLACK.getIndex());
		subGridHeader.setBorderTop(XSSFCellStyle.BORDER_THIN);
		subGridHeader.setTopBorderColor(IndexedColors.BLACK.getIndex());
		subGridHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		XSSFColor myColor = new XSSFColor(new Color(237, 237, 237));
		subGridHeader.setFillForegroundColor(myColor);

		XSSFCellStyle rows = (XSSFCellStyle) workbook.createCellStyle();
	
		rows.setWrapText(true);
		/** Defining Fonts */
		XSSFFont font1 = (XSSFFont) workbook.createFont();
		font1.setFontHeightInPoints((short) 22);

		XSSFFont font2 = (XSSFFont) workbook.createFont();
		font2.setFontHeightInPoints((short) 16);

		XSSFFont font3 = (XSSFFont) workbook.createFont();
		font3.setFontHeightInPoints((short) 14);

		int imgRow1 = 0;
		int imgRow2 = 4;
		int imgCol1 = 0;
		int imgCol2 = 0;

		titleRow1 = 2;
		titleRow2 = 5;
		int count = 0;
		rowCount = 2;
		colCount = 0;
		Map<String, Long> counted = Arrays.asList(secCols.toString().split(",")).stream()
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, titleRow1, titleRow2));
		titleRow = sheet.createRow(rowCount++);
		Cell cell = titleRow.createCell(2);
		cell.setCellValue(dy.getLangName(getLoggedInUserLanguage()));
		cell.setCellStyle(headerStyle);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerStyle.setFont(font1);

		rowCount = 5;

		if (!filterMap.isEmpty()) {
			rowCount++;
			titleRow = sheet.createRow(rowCount++);
			titleRow.setHeight((short) 400);

			cell = titleRow.createCell(0);
			cell.setCellValue(getLocaleProperty("exportFilter"));
			font1.setBoldweight((short) 18);
			font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			headerStyle.setFont(font1);
			cell.setCellStyle(headerStyle);

			if (!StringUtil.isEmpty(branchIdParma)) {
				titleRow = sheet.createRow(rowCount++);
				cell = titleRow.createCell(0);
				cell.setCellValue(getLocaleProperty("BranchId"));
				cell.setCellStyle(rows);

				cell = titleRow.createCell(1);
				cell.setCellValue(branchIdParma);
				cell.setCellStyle(rows);
			}

			for (Entry<String, String> filter : filterMap.entrySet()) {
				titleRow = sheet.createRow(rowCount++);
				titleRow.setHeight((short) 400);

				cell = titleRow.createCell(0);
				cell.setCellValue(filter.getKey());
				cell.setCellStyle(rows);

				cell = titleRow.createCell(1);
				cell.setCellValue(filter.getValue());
				cell.setCellStyle(rows);

			}
		}

		if (!StringUtil.isEmpty(filter.getSeason())) {
			titleRow = sheet.createRow(rowCount++);
			titleRow.setHeight((short) 400);

			cell = titleRow.createCell(0);
			cell.setCellValue(getText("season"));
			cell.setCellStyle(rows);

			cell = titleRow.createCell(1);
			cell.setCellValue(getSeasonList().get(filter.getSeason()));
			cell.setCellStyle(rows);
		}

		if (!StringUtil.isEmpty(filter.getCreatedUser()) && !filter.getCreatedUser().equalsIgnoreCase("~Select")) {
			titleRow = sheet.createRow(rowCount++);
			titleRow.setHeight((short) 400);

			cell = titleRow.createCell(0);
			cell.setCellValue(getLocaleProperty("agent"));
			cell.setCellStyle(rows);

			cell = titleRow.createCell(1);
			cell.setCellValue(filter.getCreatedUser().split("~")[1]);
			cell.setCellStyle(rows);
		}

		// rowCount++;

		titleRow = sheet.createRow(rowCount++);
		titleRow.setHeight((short) 400);
		cell = titleRow.createCell(secount);
		cell.setCellValue(getLocaleProperty("dysection"));
		font2.setBoldweight((short) 5);
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		filterStyle.setFont(font2);

		cell.setCellStyle(filterStyle);
		Set<DynamicMenuSectionMap> dsMap = dy.getDynamicSectionConfigs();
		if (branches != null && !branches.isEmpty()) {
			dsMap = dy.getDynamicSectionConfigs().stream().sorted((f1, f2) -> f1.getOrder().compareTo(f2.getOrder()))
					.filter(fff -> fff.getBranchId() == null
							|| !Collections.disjoint(Arrays.asList(fff.getBranchId().split(",")), branches))
					.collect(Collectors.toSet());
		}
		for (DynamicMenuSectionMap label : dy.getDynamicSectionConfigs()) {
			secount++;
			// int sec = secount++;
			int end = (secount + counted.get(label.getSection().getLangName(getLoggedInUserLanguage())).intValue());
			sheet.addMergedRegion(new CellRangeAddress(rowCount - 1, rowCount - 1, secount, end));
			cell = titleRow.createCell(secount);
			cell.setCellValue((label.getSection().getLangName(getLoggedInUserLanguage())));
			font2.setBoldweight((short) 2);
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(font2);
			cell.setCellStyle(filterStyle);
			secount = end;
		}
		// titleRow.setRowStyle(filterStyle);
		// formMainGridCols();
		String[] headerFieldsArr = mainGridCols.split(Pattern.quote("|"));

		titleRow = sheet.createRow(rowCount++);

		cell = titleRow.createCell(colCount);

		cell.setCellValue(getLocaleProperty("sno"));
		font2.setBoldweight((short) 5);
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		filterStyle.setFont(font2);
		//System.out.println("valuse :"+titleRow.getRowNum());
		cell.setCellStyle(filterStyle);
		for (String label : headerFieldsArr) {
			cell = titleRow.createCell(colCount++);
			cell.setCellValue((label));
			font2.setBoldweight((short) 2);
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(font2);
			sheet.setColumnWidth(colCount, 7000);
			cell.setCellStyle(filterStyle);
		}
		rowCnt.getAndIncrement();
		int numMerged = sheet.getNumMergedRegions();

		// titleRow.setRowStyle(filterStyle);
		for (int i = 0; i < numMerged; i++) {
			CellRangeAddress mergedRegions = sheet.getMergedRegion(i);
			/*
			 * RegionUtil.setBorderTop(CellStyle.BORDER_THIN, mergedRegions,
			 * sheet, workbook); RegionUtil.setBorderLeft(CellStyle.BORDER_THIN,
			 * mergedRegions, sheet, workbook);
			 * RegionUtil.setBorderRight(CellStyle.BORDER_THIN, mergedRegions,
			 * sheet, workbook);
			 * RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, mergedRegions,
			 * sheet, workbook);
			 */
		}

		if (!StringUtil.isEmpty(entityType)) {
			int rType = Integer.parseInt(entityType);
			Map criteriaMap = new HashMap<>();
			if (rType == DynamicFeildMenuConfig.EntityTypes.FARMER.ordinal()
					|| rType == DynamicFeildMenuConfig.EntityTypes.TRAINING.ordinal()) {
				criteriaMap.put(IClientService.ENTITY, Farmer.class);
			} else if (rType == DynamicFeildMenuConfig.EntityTypes.FARM.ordinal()
					|| rType == DynamicFeildMenuConfig.EntityTypes.CERTIFICATION.ordinal()) {
				criteriaMap.put(IClientService.ENTITY, Farm.class);
			} else if (rType == DynamicFeildMenuConfig.EntityTypes.GROUP.ordinal()) {
				criteriaMap.put(IClientService.ENTITY, Warehouse.class);
			} else if (rType == DynamicFeildMenuConfig.EntityTypes.FARM_CROPS.ordinal()) {
				criteriaMap.put(IClientService.ENTITY, FarmCrops.class);
			}

			StringBuilder listt = new StringBuilder();
			StringBuilder alias = new StringBuilder();

			getDynamicFieldReportJoinMapList().stream().forEach(join -> {
				alias.append(join.getProperties() + ",");
			});

			criteriaMap.put(IClientService.CRITERIA_ALIAS, StringUtil.removeLastComma(alias.toString()));
			listt.append("id,");
			getDynamicFieldReportConfigList().stream()
					.filter(dReportConfig -> ObjectUtil.isEmpty(dReportConfig.getGroupProp())
							|| !dReportConfig.getGroupProp().equals("1"))
					.forEach(dReportConfig -> {
						listt.append(dReportConfig.getField() + ",");
					});
			List<Long> isList = new ArrayList<>();
			mainGridRows.stream().forEachOrdered(u -> {
				if (u.getReferenceId().contains(",")) {
					isList.addAll(ObjectUtil.stringListToLongList(Arrays.asList(u.getReferenceId().split(","))));
				} else {
					isList.add(Long.valueOf(u.getReferenceId()));
				}

			});
			// exportdAt = mainGridRows.stream().collect(Collectors.toMap(u ->
			// u.getId(), u -> u.getExportDatas()));
			criteriaMap.put(IClientService.PROJECTIONS_LIST, StringUtil.removeLastComma(listt.toString()));
			if (!StringUtil.isListEmpty(isList)) {
				List<Object[]> valueListc = clientService.getMappedValuesAll(criteriaMap, isList);
				values.putAll(valueListc.stream().map(u -> (Object[]) u)
						.collect(Collectors.toMap(u -> String.valueOf(u[0].toString()), v -> v)));
			}
			/*
			 * List<DynamicImageData> dyIds = new ArrayList<>();
			 * mainGridRows.stream().map(doc ->
			 * doc.getFarmerDynamicFieldsValues()).flatMap(Set::stream) .map(u
			 * -> u.getDymamicImageData()).forEach(u -> { dyIds.addAll(u); });
			 */
			/*
			 * iamgeDt = dyIds.stream().collect( Collectors.groupingBy(u ->
			 * u.getFarmerDynamicFieldsValue().getFarmerDynamicData().getId()));
			 */
		}

		// final AtomicInteger rowCnt = new AtomicInteger(0);

		final AtomicInteger counter = new AtomicInteger();
		Collection<List<FarmerDynamicData>> result = mainGridRows.stream().collect(Collectors.toList()).stream()
				.collect(Collectors.groupingBy(it -> counter.getAndIncrement() / 2000)).values();

		result.stream().forEach(v -> {
			List<FarmerDynamicFieldsValue> fdv = farmerService
					.listFarmerDynamicFieldByFdIds(v.stream().map(u -> u.getId()).collect(Collectors.toList()));
			// List<FarmerDynamicFieldsValue> fdv =
			// mainGridRows.stream().flatMap(u
			// ->
			// u.getFarmerDynamicFieldsValues().stream()).collect(Collectors.toList());
			String codes = fdv.stream()
					.filter(u -> u.getAccessType() == 1 || u.getAccessType() == 2 && u.getFieldValue() != null)
					.map(u -> u.getFieldValue()).collect(Collectors.joining(","));
			List<Integer> litcodes = fdv.stream().filter(u -> u.getAccessType() == 3 && u.getListMethod() != null)
					.map(u -> Integer.valueOf(u.getListMethod())).collect(Collectors.toList());

			List<Object[]> fcL = catalogueService.listCataloguesByCodes(Arrays.asList(codes.split(",")),
					getLoggedInUserLanguage());

			fcm.putAll(fcL.stream()
					.collect(Collectors.toMap(u -> u[0].toString(), u -> u[1].toString(), (address1, address2) -> {
						return address1;
					})));
			if (litcodes != null && !litcodes.isEmpty()) {
				fcm.putAll(processListMethod(litcodes, fcm));
			}
			listItem.putAll(
					(Map<Long, Map<String, String>>) fdv.stream()
							.filter(u -> u.getParentId() != null && u.getParentId() != 0)
							.collect(Collectors.groupingBy(u -> u.getFarmerDynamicData().getId(),
									Collectors.groupingBy(u -> String.valueOf(u.getParentId()), Collectors.mapping(
											u -> u.getTypez() + "~" + u.getFieldName() + "#"
													+ u.getFieldValue(),
											Collectors.joining("|"))))));

			other.putAll((Map<Long, Map<String, String>>) fdv.stream()
					.filter(u -> u.getParentId() == null || u.getParentId() == 0 && u.getFarmerDynamicData() != null)
					.collect(Collectors.groupingBy(u -> u.getFarmerDynamicData().getId(), Collectors.toMap(
							u -> String.valueOf(u.getFieldName()), u -> u.getFieldValue(), (address1, address2) -> {
								System.out.println(address1 + "," + address2);
								return address1;
							}))));

			other.putAll(fdv.stream()
					.filter(u -> u.getParentId() == null || u.getParentId() == 0 && u.getFarmerDynamicData() == null
							&& u.getFollowUpParent() != null)
					.collect(Collectors.groupingBy(u -> u.getFollowUpParent().getFarmerDynamicData().getId(),
							Collectors.toMap(u -> String.valueOf(u.getFieldName()), u -> u.getFieldValue(),
									(address1, address2) -> {
										System.out.println(address1 + "," + address2);
										return address1;
									}))));
			AtomicInteger rowCnt1 = new AtomicInteger(0);
			for (FarmerDynamicData farmerDynamicData : v) {
				// v.stream().forEach(farmerDynamicData -> {
				AtomicInteger colCount1 = new AtomicInteger(0);
				Row row = sheet.createRow(rowCount++);
				//System.out.println("valuse :"+row.getRowNum());
				Cell cell1 = row.createCell(colCount1.getAndIncrement());
				cell1.setCellValue(String.valueOf(rowCnt1.incrementAndGet()));
				sheet.setColumnWidth(mainGridIterator, (15 * 550));

				cell1.setCellStyle(rows);
				if ((getIsMultiBranch().equalsIgnoreCase("1")
						&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(getBranchId())))) {

					if (StringUtil.isEmpty(getBranchId())) {
						/*
						 * rows.add(!StringUtil.isEmpty(getBranchesMap().get(
						 * getParentBranchMap().get(farmerDynamicData.getBranch(
						 * )))) ? getBranchesMap().get(getParentBranchMap().get(
						 * farmerDynamicData.getBranch())) :
						 * getBranchesMap().get(farmerDynamicData.getBranch()));
						 */
						cell1 = row.createCell(colCount1.getAndIncrement());
						cell1.setCellValue(!StringUtil
								.isEmpty(getBranchesMap().get(getParentBranchMap().get(farmerDynamicData.getBranch())))
										? getBranchesMap().get(getParentBranchMap().get(farmerDynamicData.getBranch()))
										: getBranchesMap().get(farmerDynamicData.getBranch()));
						cell1.setCellStyle(rows);
					}
					// rows.add(getBranchesMap().get(farmerDynamicData.getBranch()));
					cell1 = row.createCell(colCount1.getAndIncrement());
					cell1.setCellValue(getBranchesMap().get(farmerDynamicData.getBranch()));
					cell1.setCellStyle(rows);

				} else {
					if (StringUtil.isEmpty(getBranchId())) {
						// rows.add(branchesMap.get(farmerDynamicData.getBranch()));
						cell1 = row.createCell(colCount1.getAndIncrement());
						cell1.setCellValue(getBranchesMap().get(farmerDynamicData.getBranch()));
						cell1.setCellStyle(rows);
					}
				}

				if (!ObjectUtil.isEmpty(farmerDynamicData)) {
					cell1 = row.createCell(colCount1.getAndIncrement());
					cell1.setCellValue(!StringUtil.isEmpty(farmerDynamicData.getDate())
							? DateUtil.convertDateToString(farmerDynamicData.getDate(), DateUtil.DATE_TIME_FORMAT)
							: "");
					cell1.setCellStyle(rows);

					cell1 = row.createCell(colCount1.getAndIncrement());
					cell1.setCellValue(getAgentList().get(farmerDynamicData.getCreatedUser()));
					cell1.setCellStyle(rows);

					if (dy.getIsSeason() != null && dy.getIsSeason() == 1) {

						HarvestSeason season = getHarvestSeason(farmerDynamicData.getSeason());
						cell1 = row.createCell(colCount1.getAndIncrement());
						cell1.setCellValue(!ObjectUtil.isEmpty(season) ? season.getName() : "");
						cell1.setCellStyle(rows);

					}
					list.clear();
					if (!ObjectUtil.isEmpty(values)) {
						if (farmerDynamicData.getReferenceId().contains(",")) {
							Arrays.asList(farmerDynamicData.getReferenceId().split(",")).stream().forEach(u -> {
								if (values.containsKey(String.valueOf(u))) {
									list.add(values.get(String.valueOf(u)));
								}
							});
							Object[] strRy = new Object[list.get(0).length];
							for (int i = 1; i < strRy.length; i++) {
								strRy[l.intValue()] = list.stream().map(e -> e[l.intValue()].toString()).distinct()
										.collect(Collectors.joining(","));
							}
							for (int i = 1; i < strRy.length; i++) {
								cell1 = row.createCell(colCount1.getAndIncrement());
								cell1.setCellValue(strRy[i].toString());

								cell1.setCellStyle(rows);
							}
						} else {
							if (values.containsKey(String.valueOf(farmerDynamicData.getReferenceId()))) {
								Object[] arr = values.get(String.valueOf(farmerDynamicData.getReferenceId()));
								for (int i = 1; i < arr.length; i++) {
									cell1 = row.createCell(colCount1.getAndIncrement());
									cell1.setCellValue(String.valueOf(arr[i]));

									cell1.setCellStyle(rows);
								}

							}
						}
					}

					if (farmerDynamicData.getIsScore() != null && farmerDynamicData.getIsScore() == 1) {
						cell1 = row.createCell(colCount1.getAndIncrement());
						cell1.setCellValue(farmerDynamicData.getTotalScore() + "%");
						cell1.setCellStyle(rows);
					} else if (farmerDynamicData.getIsScore() != null && farmerDynamicData.getIsScore() == 2) {
						cell1 = row.createCell(colCount1.getAndIncrement());
						cell1.setCellValue(farmerDynamicData.getTotalScore() != null
								&& StringUtil.isDouble(farmerDynamicData.getTotalScore())
										? formCertificationLevels().get(farmerDynamicData.getTotalScore().intValue())
										: "");
						cell1.setCellStyle(rows);

						cell1 = row.createCell(colCount1.getAndIncrement());
						cell1.setCellValue(farmerDynamicData.getConversionStatus() != null
								? "Year " + farmerDynamicData.getConversionStatus() : "");
						cell1.setCellStyle(rows);

					}

					FarmerDynamicData dt = farmerDynamicData;
					expDatas.clear();
					expDatasOthe.clear();
					if(listItem.get(dt.getId())!=null&&!StringUtil.isEmpty(listItem.get(dt.getId())))
					expDatas.putAll(listItem.get(dt.getId()));
					if(other.get(dt.getId())!=null&&!StringUtil.isEmpty(other.get(dt.getId())))
					expDatasOthe.putAll(other.get(dt.getId()));
					fieldsMap.clear();
					List<DynamicFieldConfig> fldeie = dfm
							.stream().map(
									u -> u.getField())
							.filter(u -> (!Arrays
									.asList(DynamicFieldConfig.COMPONENT_TYPES.BUTTON.ordinal(),
											DynamicFieldConfig.COMPONENT_TYPES.VIDEO.ordinal(),
											DynamicFieldConfig.COMPONENT_TYPES.AUDIO.ordinal(),
											DynamicFieldConfig.COMPONENT_TYPES.PHOTO_CERTIFICATION.ordinal())
									.contains(Integer.valueOf(u.getComponentType()))))
							.collect(Collectors.toList());
					for (DynamicFieldConfig secCode : fldeie) {

						if (secCode.getComponentType().equals("8")) {
							rownum.set(fiedlList.get(secCode.getCode()));
							if (expDatas != null && expDatas.containsKey(String.valueOf(secCode.getId()))) {
								String fieldValue = expDatas.get(String.valueOf(secCode.getId()));
								if (fieldValue.contains("|")) {
									String[] str = fieldValue.split("\\|");
									Map<Integer, List<String[]>> typeMap = Arrays.asList(str).stream()
											.map(elem -> elem.split("~"))
											.collect(Collectors.groupingBy(e -> Integer.valueOf(e[0])));
									typeMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
											.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
													(oldValue, newValue) -> oldValue, LinkedHashMap::new));
									for (Entry<Integer, List<String[]>> enty : typeMap.entrySet()) {
										List<String[]> fields = enty.getValue();
										fieldsMap.putAll(fields.stream()
												.collect(Collectors.toMap(
														obj -> String.valueOf(obj[1].split("#")[0].trim()),
														obj -> String.valueOf(obj[1].split("#")[1].trim()),
														(address1, address2) -> {
															// System.out.println("duplicate
															// key found!");
															return address1;
														})));
										if (parentMap.containsKey(secCode.getCode())) {
											List<DynamicFieldConfig> smLis = parentMap.get(secCode.getCode());
											for (DynamicFieldConfig u : smLis) {

												cell1 = row.createCell(rownum.intValue());
												cell1.setCellValue(fcm.containsKey(fieldsMap.get(u.getCode()))
														? fcm.get(fieldsMap.get(u.getCode()))
														: fieldsMap.get(u.getCode()));
												cell1.setCellStyle(rows);

												rownum.incrementAndGet();
											}

										}
									}
								} else {
									fieldsMap.put(String.valueOf(fieldValue.split("~")[1].split("#")[0].trim()),
											String.valueOf(fieldValue.split("~")[1].split("#")[1].trim()));
									if (parentMap.containsKey(secCode.getCode())) {
										List<DynamicFieldConfig> smLis = parentMap.get(secCode.getCode());
										for (DynamicFieldConfig u : smLis) {

											cell1 = row.createCell(rownum.intValue());
											cell1.setCellValue(fcm.containsKey(fieldsMap.get(u.getCode()))
													? fcm.get(fieldsMap.get(u.getCode())) : fieldsMap.get(u.getCode()));
											cell1.setCellStyle(rows);

											rownum.incrementAndGet();
										}

									}

								}

							} else {
								if (parentMap.containsKey(secCode.getCode())) {
									List<DynamicFieldConfig> smLis = parentMap.get(secCode.getCode());
									for (DynamicFieldConfig u : smLis) {
										cell1 = row.createCell(rownum.intValue());
										cell1.setCellValue("");
										cell1.setCellStyle(rows);

										rownum.incrementAndGet();
									}

								}
							}

							/*
							 * if (parentMap.containsKey(secCode.getCode())) {
							 * List<DynamicFieldConfig> smLis =
							 * parentMap.get(secCode.getCode());
							 * smLis.stream().forEach(u -> { cell1 =
							 * row.createCell(rownum);
							 * cell1.setCellValue(valueListMap.get(u + "_" +
							 * rownum)); });
							 * 
							 * }
							 */
						} else if (secCode.getReferenceId() == null) {
							if (expDatasOthe != null && expDatasOthe.containsKey(secCode.getCode())) {
								cell1 = row.createCell(fiedlList.get(secCode.getCode()));
								cell1.setCellValue(fcm.containsKey(expDatasOthe.get(secCode.getCode()))
										? fcm.get(expDatasOthe.get(secCode.getCode()))
										: expDatasOthe.get(secCode.getCode()));

								cell1.setCellStyle(rows);
							} else {
								cell1 = row.createCell(fiedlList.get(secCode.getCode()));
								cell1.setCellValue("");
								cell1.setCellStyle(rows);
							}
						}

					}
				}
				row.setRowStyle(rows);
				lastCol = row.getLastCellNum();
			}
			listItem.clear();
			other.clear();
			fcm.clear();
		});

		// alternateGreenAndWhiteRows(sheet);
		// sheet.autoSizeColumn(25000000);
		Drawing drawing = sheet.createDrawingPatriarch();
		int pictureIdx = getPicIndex(workbook);
		XSSFClientAnchor anchor = new XSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		Picture picture = drawing.createPicture(anchor, pictureIdx);
		picture.resize(0.600);

		String makeDir = FileUtil.storeXls(id);
		String fileName = dy.getLangName(getLoggedInUserLanguage()) + fileNameDateFormat.format(new Date()) + ".xls";
		System.out.println("-------------------------- Write -------------------------------");
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		OutputStream os = new BufferedOutputStream(new FileOutputStream(makeDir + fileName));
		workbook.write(os);
		//workbook.dispose();
		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		fileOut.close();

		return stream;
	}

	private Map<String, String> processListMethod(List<Integer> litcodes, Map<String, String> fcm) {
		LIST_METHOD[] methods = DynamicFieldConfig.LIST_METHOD.values();

		litcodes.stream().forEach(u -> {
			fcm.putAll(getOptions(methods[Integer.valueOf(u)].toString()));
		});
		return fcm;
	}

	private static byte[] resize(byte[] img, int height, int width) throws IOException {
		byte[] imageBytes = null;
		BufferedImage bs = ImageIO.read(new ByteArrayInputStream(img));
		if (bs.getHeight() >= height || bs.getWidth() >= width) {
			Image tmp = bs.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = resized.createGraphics();
			g2d.drawImage(tmp, 0, 0, null);
			g2d.dispose();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				ImageIO.write(resized, "jpg", baos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			imageBytes = baos.toByteArray();
		} else {
			imageBytes = img;
		}
		return imageBytes;
	}

	public int getPicIndex(XSSFWorkbook workbook) throws IOException {

		int index = -1;

		byte[] picData = null;
		picData = clientService.findLogoByCode(Asset.APP_LOGO);

		if (picData != null)
			index = workbook.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
	}

	public String getXlsFileName() {
		return xlsFileName;
	}

	public void setXlsFileName(String xlsFileName) {
		this.xlsFileName = xlsFileName;
	}

	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}

	public Map<String, String> getFarmerNameList() {

		Map<String, String> farmerMap = new LinkedHashMap<String, String>();

		List<Object[]> farmerDynamicDataList = farmerService.listFarmerInfo();

		for (Object[] obj : farmerDynamicDataList) {
			farmerMap.put(String.valueOf(obj[0]), !ObjectUtil.isEmpty(obj[4])
					? String.valueOf(obj[3]) + " " + String.valueOf(obj[4]) : String.valueOf(obj[3]));
		}

		return farmerMap;

	}

	public String getFilters() {
		return filters;
	}

	public void setFilters(String filters) {
		this.filters = filters;
	}

	public String getDaterange() {
		return daterange;
	}

	public void setDaterange(String daterange) {
		this.daterange = daterange;
	}

	public String getInsDate() {
		return insDate;
	}

	public void setInsDate(String insDate) {
		this.insDate = insDate;
	}

	public String getInspectorName() {
		return inspectorName;
	}

	public void setInspectorName(String inspectorName) {
		this.inspectorName = inspectorName;
	}

	public String getInspectorMobile() {
		return inspectorMobile;
	}

	public void setInspectorMobile(String inspectorMobile) {
		this.inspectorMobile = inspectorMobile;
	}

	public String getInsType() {
		return insType;
	}

	public void setInsType(String insType) {
		this.insType = insType;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getTotLand() {
		return totLand;
	}

	public void setTotLand(String totLand) {
		this.totLand = totLand;
	}

	public String getOrgLand() {
		return orgLand;
	}

	public void setOrgLand(String orgLand) {
		this.orgLand = orgLand;
	}

	public String getTotSite() {
		return totSite;
	}

	public void setTotSite(String totSite) {
		this.totSite = totSite;
	}

	public FarmIcsConversion getFarmIcsConversion() {
		return farmIcsConversion;
	}

	public void setFarmIcsConversion(FarmIcsConversion farmIcsConversion) {
		this.farmIcsConversion = farmIcsConversion;
	}

	public String getInspectionDateYear() {
		return inspectionDateYear;
	}

	public void setInspectionDateYear(String inspectionDateYear) {
		this.inspectionDateYear = inspectionDateYear;
	}

	public ICertificationService getCertificationService() {
		return certificationService;
	}

	public void setCertificationService(ICertificationService certificationService) {
		this.certificationService = certificationService;
	}

	public Map<String, String> getScopeList() {
		Map<String, String> scopeMap = new LinkedHashMap<String, String>();
		scopeMap = getFarmCatalougeMap(Integer.valueOf(getText("certificationType")));

		return scopeMap;
	}

	public Map<String, String> getInsTypeList() {

		AtomicInteger i = new AtomicInteger(0);
		Map<String, String> insTypeMap = new LinkedHashMap<>();
		insTypeMap = getFarmCatalougeMap(Integer.valueOf(getText("insType")));
		return insTypeMap;
	}

	public String getTxType() {
		return txType;
	}

	public void setTxType(String txType) {
		this.txType = txType;
	}

	public void populateFarmer() throws Exception {
		if (!selectedVillage.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedVillage))) {
			List<Object[]> listFarmer = farmerService.listFarmerCodeIdNameByVillageCode(selectedVillage);

			JSONArray farmerArr = new JSONArray();
	if (getCurrentTenantId().equalsIgnoreCase("kenyafpo")) {
		listFarmer.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
			farmerArr.add(getJSONObject(obj[4], obj[2] + "-" + (!StringUtil.isEmpty(obj[1]) ? (obj[1]) : " ")));
		});
			}
			else{
			listFarmer.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
				farmerArr.add(getJSONObject(obj[4], obj[2] + "" + (!StringUtil.isEmpty(obj[3]) ? (obj[3]) : " ") + ""
						+ (!StringUtil.isEmpty(obj[5]) ? (obj[5]) : " ")));
			});
			}
			sendAjaxResponse(farmerArr);
		}
	}

	public void populateFarmerWilmar() throws Exception {
		if (!selectedState.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedState))) {
			List<Object[]> listFarmer = farmerService.listFarmerCodeIdNameByStateCode(selectedState);

			JSONArray farmerArr = new JSONArray();
			listFarmer.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
				farmerArr.add(getJSONObject(obj[4], obj[2] + "" + (!StringUtil.isEmpty(obj[3]) ? (obj[3]) : " ") + ""
						+ (!StringUtil.isEmpty(obj[5]) ? (obj[5]) : " ")));
			});
			sendAjaxResponse(farmerArr);
		}
	}

	public Map<String, String> getVillageList() {
		Map<String, String> villageMap = new LinkedHashMap<String, String>();

		List<Object[]> villages = locationService.listVillageIdAndName();
		villages.stream().forEach(u -> {
			villageMap.put(u[1].toString(), u[2].toString());

		});
		return villageMap;
	}

	public Map<String, String> getStateList() {
		Map<String, String> stateMap = new LinkedHashMap<String, String>();

		List<Object[]> states = locationService.listStates();
		states.stream().forEach(u -> {
			stateMap.put(u[0].toString(), u[1].toString());

		});
		return stateMap;
	}

	public String getSelectedVillage() {
		return selectedVillage;
	}

	public void setSelectedVillage(String selectedVillage) {
		this.selectedVillage = selectedVillage;
	}

	public JSONArray getFilterArray() {
		return filterArray;
	}

	public void setFilterArray(JSONArray filterArray) {
		this.filterArray = filterArray;
	}

	public Map<String, String> getFilterMap() {
		return filterMap;
	}

	public void setFilterMap(Map<String, String> filterMap) {
		this.filterMap = filterMap;
	}

	public String getCondtion() {
		return condtion;
	}

	public void setCondtion(String condtion) {
		this.condtion = condtion;
	}

	public Map<String, String> getAgentList() {
		String subBranch = null;
		if (agentList.isEmpty()) {
			if (!StringUtil.isEmpty(txnType) && agentList.isEmpty()) {
				List<String> branche = getBranches(getBranchId());
				List<Object[]> fd = farmerService.ListFarmerDynamicDataAgentByTxnType(txnType,
						branche.stream().collect(Collectors.joining(",")));
				agentList = fd.stream().collect(Collectors.toMap(u -> u[0].toString(),
						u -> (u[0].toString() + (u[1] != null ? " - " + u[1].toString() : ""))));
			}
		}
		return agentList;
	}

	public ProcurementVariety getFarmCropList() {
		return farmCropList;
	}

	public void setFarmCropList(ProcurementVariety farmCropList) {
		this.farmCropList = farmCropList;
	}

	public String getFilterCols() {
		return filterCols;
	}

	public void setFilterCols(String filterCols) {
		this.filterCols = filterCols;
	}

	public String getFieldCondition() {
		return fieldCondition;
	}

	public void setFieldCondition(String fieldCondition) {
		this.fieldCondition = fieldCondition;
	}

	public String getDigitalSignatureByteString() {
		return digitalSignatureByteString;
	}

	public void setDigitalSignatureByteString(String digitalSignatureByteString) {
		this.digitalSignatureByteString = digitalSignatureByteString;
	}

	public String getDigitalSignatureEnabled() {
		return digitalSignatureEnabled;
	}

	public void setDigitalSignatureEnabled(String digitalSignatureEnabled) {
		this.digitalSignatureEnabled = digitalSignatureEnabled;
	}

	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public Map<String, String> getYearList() {
		Map<String, String> map = new HashMap<>();
		map.put("0", "Year 0");
		map.put("1", "Year 1");
		map.put("2", "Year 2");
		map.put("3", "Year 3");
		map.put("4", "Year 4");
		map.put("5", "Year 5");
		map.put("6", "Year 6");
		return map;
	}

	public Map<Integer, String> getCertificationLevels() {
		return certificationLevels;
	}

	public void setCertificationLevels(Map<Integer, String> certificationLevels) {
		this.certificationLevels = certificationLevels;
	}

	public String getScoreVal() {
		return scoreVal;
	}

	public void setScoreVal(String scoreVal) {
		this.scoreVal = scoreVal;
	}

	public String getAgentSignatureByteString() {
		return agentSignatureByteString;
	}

	public void setAgentSignatureByteString(String agentSignatureByteString) {
		this.agentSignatureByteString = agentSignatureByteString;
	}

	public String getSelectedState() {
		return selectedState;
	}

	public void setSelectedState(String selectedState) {
		this.selectedState = selectedState;
	}

	public String setBranchFilterMapKey() {
		String retVal = "";
		if (!ObjectUtil.isEmpty(filter) && !StringUtil.isEmpty(filter.getBranch())) {
			retVal = getLocaleProperty("subOrganisationFilter");
		}
		return retVal;
	}

	public String setBranchFilterMapValue() {
		String retVal = "";
		if (!ObjectUtil.isEmpty(filter) && !StringUtil.isEmpty(filter.getBranch())) {
			retVal = filter.getBranch();
		}
		return retVal;
	}

	@Override
	public Map<String, String> getBranchesMap() {

		if (branchesMap.size() == 0) {
			buildBranchMap();
		}
		return branchesMap;
	}

	@Override
	public void buildBranchMap() {

		List<Object[]> branchMasters = getBranchesInfo();
		branchesMap = new LinkedHashMap<String, String>();
		for (Object[] objects : branchMasters) {
			branchesMap.put(objects[0].toString(), objects[1].toString());
		}
	}

	public String getBranchIdParma() {
		return branchIdParma;
	}

	public void setBranchIdParma(String branchIdParma) {
		this.branchIdParma = branchIdParma;
	}

	public String getSubBranchIdParam() {
		return subBranchIdParam;
	}

	public void setSubBranchIdParam(String subBranchIdParam) {
		this.subBranchIdParam = subBranchIdParam;
	}

	public String updateActPlan() throws Exception {
		if (id != null && !id.equals("")) {
			farmerDynamicData = farmerService.findFarmerDynamicData(id);
			if (farmerDynamicData == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			seasonType = farmerDynamicData.getIsSeason();
			entityType = farmerDynamicData.getEntityId();
			// Farm
			// farm=farmerService.findFarmById(Long.valueOf(farmerDynamicData.getReferenceId()));
			if (!ObjectUtil.isEmpty(farmerDynamicData) && farmerDynamicData.getEntityId().equalsIgnoreCase("1")) {
				if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
					Farmer farm = getFarmer(farmerDynamicData.getReferenceId());
					setFarmer(farm != null ? String.valueOf(farm.getFirstName()) : "");
					setSelectedVillage(farm.getVillage().getCode());
				}
			} else if (!ObjectUtil.isEmpty(farmerDynamicData)
					&& farmerDynamicData.getEntityId().equalsIgnoreCase("5")) {
				if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())
						&& farmerDynamicData.getReferenceId().contains(",")) {
					List<Farmer> frmr = farmerService
							.listFarmerByIds(Arrays.asList(farmerDynamicData.getReferenceId().split(",")));
					setFarmer(String.valueOf(frmr.stream().filter(u -> u.getFirstName() != null)
							.map(p -> String.valueOf(
									p.getFirstName() + (p.getLastName() != null ? ("-" + p.getLastName()) : "")))
							.collect(Collectors.joining(","))));

					setSelectedVillage(frmr != null && !frmr.isEmpty() ? frmr.get(0).getVillage().getCode() : "");
				} else if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
					Farmer frmr = farmerService.findFarmerById(Long.valueOf(farmerDynamicData.getReferenceId()));
					setFarmer(String.valueOf(frmr.getFirstName()));
					setSelectedVillage(frmr != null && frmr.getVillage() != null ? frmr.getVillage().getCode() : "");
				}
			} else if (!ObjectUtil.isEmpty(farmerDynamicData)
					&& farmerDynamicData.getEntityId().equalsIgnoreCase("2")) {
				if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
					Farm farm = farmerService.findFarmById(Long.valueOf(farmerDynamicData.getReferenceId()));
					setFarmer(farm != null ? String.valueOf(farm.getFarmer().getFirstName()) : "");
					setFarmList(farm != null ? String.valueOf(farm.getFarmName()) : "");
				}
			} else if (!ObjectUtil.isEmpty(farmerDynamicData)
					&& farmerDynamicData.getEntityId().equalsIgnoreCase("3")) {
				if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
					Warehouse w = getWarehouse(String.valueOf(farmerDynamicData.getReferenceId()));
					setGroup(w != null ? w.getName() : "");

				}
			} else if (!ObjectUtil.isEmpty(farmerDynamicData)
					&& farmerDynamicData.getEntityId().equalsIgnoreCase("4")) {
				farmerDynamicData.setTxnDate(!StringUtil.isEmpty(farmerDynamicData.getDate())
						? DateUtil.convertDateToString(farmerDynamicData.getDate(), DateUtil.DATE_TIME_FORMAT) : "");
				if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
					Farm farm = farmerService.findFarmById(Long.valueOf(farmerDynamicData.getReferenceId()));
					setFarmer(String.valueOf(farm.getFarmer().getFirstName()));
					setFarmList(String.valueOf(farm.getFarmName()));
					setGroup(String.valueOf(farm.getFarmer().getSamithi().getName()));
					setSelectedVillage(farm.getFarmer().getVillage().getName());
				}
				setInspectionStatus(farmerDynamicData.getConversionStatus());

				/*
				 * if (farmerDynamicData.getConversionStatus() != null &&
				 * !StringUtil.isEmpty(farmerDynamicData.getConversionStatus()))
				 * { if
				 * (farmerDynamicData.getConversionStatus().equalsIgnoreCase("1"
				 * )) { farmerDynamicData.setInspectionStatus(getLocaleProperty(
				 * "Approved")); } else {
				 * farmerDynamicData.setInspectionStatus(getLocaleProperty(
				 * "Declined")); } }
				 */
				if (farmerDynamicData.getConversionStatus() != null
						&& !StringUtil.isEmpty(farmerDynamicData.getConversionStatus())
						&& farmerDynamicData.getConversionStatus().equalsIgnoreCase("1")) {

					setIcsType(farmerDynamicData.getIcsName());
				} else {
					setCorrectiveActionPlan(!StringUtil.isEmpty(farmerDynamicData.getCorrectiveActionPlan())
							? farmerDynamicData.getCorrectiveActionPlan() : "");
				}
				if (!ObjectUtil.isEmpty(farmerDynamicData.getFarmIcs())) {
					setInsDate(DateUtil.convertDateToString(farmerDynamicData.getFarmIcs().getInspectionDate(),
							getGeneralDateFormat()));
					setInspectorName(farmerDynamicData.getFarmIcs().getInspectorName());
					setInspectorMobile(farmerDynamicData.getFarmIcs().getInspectorMobile());
					setInsType(!StringUtil.isEmpty(farmerDynamicData.getFarmIcs().getInsType())
							? getCatlogueValueByCode(farmerDynamicData.getFarmIcs().getInsType()).getName() : "");
					// CertificateCategory cc =
					// certificationService.findCertificateCategoryByCode(farmerDynamicData.getFarmIcs().getScope());
					setScope(getCatlogueValueByCode(farmerDynamicData.getFarmIcs().getScope()).getName());
					setTotLand(farmerDynamicData.getFarmIcs().getTotalLand());
					setOrgLand(farmerDynamicData.getFarmIcs().getOrganicLand());
					setTotSite(farmerDynamicData.getFarmIcs().getTotalSite());
					setSeason(farmerDynamicData.getFarmIcs().getSeason());
				}
			} else if (!ObjectUtil.isEmpty(farmerDynamicData)
					&& farmerDynamicData.getEntityId().equalsIgnoreCase("1")) {
				if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
					Farm farm = farmerService.findFarmById(Long.valueOf(farmerDynamicData.getReferenceId()));
					setFarmer(String.valueOf(farm.getFarmer().getFirstName()));
				}
			} else if (!ObjectUtil.isEmpty(farmerDynamicData)
					&& farmerDynamicData.getEntityId().equalsIgnoreCase("3")) {
				if (!StringUtil.isEmail(farmerDynamicData.getReferenceId())) {
					Warehouse sam = locationService.findSamithiById(Long.valueOf(farmerDynamicData.getReferenceId()));
					setGroup(sam.getName());
				}
			} else if (!ObjectUtil.isEmpty(farmerDynamicData)
					&& farmerDynamicData.getEntityId().equalsIgnoreCase("6")) {
				if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
					FarmCrops farm = farmerService.findByFarmCropsId(Long.valueOf(farmerDynamicData.getReferenceId()));
					setFarmer(String.valueOf(farm.getFarm().getFarmer().getFirstName()));
					setFarmList(String.valueOf(farm.getFarm().getFarmName()));
					setSelectedVillage(String.valueOf(farm.getFarm().getFarmer().getVillage().getName()));
					setFarmCropList(farm.getProcurementVariety());

				}
			}

			if (farmerDynamicData.getSeason() != null) {
				// HarvestSeason season =
				// getSeason(farmerDynamicData.getSeason());

				HarvestSeason season = getSeason(farmerDynamicData.getSeason());
				setSeason(season.getName());
			}
			setCurrentPage(getCurrentPage());
			id = null;
			command = ACTPLAN;
			request.setAttribute(HEADING, getText(ACTPLAN));
			return INPUT;
		} else {
			if (farmerDynamicData != null) {

				farmerDynamicData = farmerService.findFarmerDynamicData(String.valueOf(farmerDynamicData.getId()));
				if (farmerDynamicData == null) {
					addActionError(NO_RECORD);
					return REDIRECT;
				}
				fdMap = new HashMap<>();
				dm = farmerService.findDynamicMenusByMType(txnType).get(0);
				fieldConfigMap = new LinkedHashMap<>();
				Map<String, String> ActMap = new HashMap<>();
				if (dm.getIsScore() != null && (dm.getIsScore() == 1 || dm.getIsScore() == 2 || dm.getIsScore() == 3)) {
					farmerDynamicData.setIsScore(dm.getIsScore());
					farmerDynamicData.setScoreValue(new HashMap<>());
				}
				dm.getDynamicFieldConfigs().stream().forEach(section -> {
					section.getField().setfOrder(section.getOrder());
					fieldConfigMap.put(section.getField().getCode(), section.getField());
					if (section.getDynamicFieldScoreMap() != null && !section.getDynamicFieldScoreMap().isEmpty()) {
						farmerDynamicData.getScoreValue().put(section.getField().getCode(), section
								.getDynamicFieldScoreMap().stream()
								.collect(Collectors.toMap(DynamicFieldScoreMap::getCatalogueCode, u -> String.valueOf(
										String.valueOf(u.getScore()) + "~" + String.valueOf(u.getPercentage())))));
					}
					if (section.getField().getFollowUp() == 1 || section.getField().getFollowUp() == 2) {
						ActMap.put(section.getField().getCode(), section.getField().getParentActField());
					}
				});

				fieldTypeMap = new HashMap<>();
				// farmerDynamicData =
				// farmerService.findFarmerDynamicDataBySeason(txnType,
				// selectedId, season);

				/*
				 * if (temp != null) { fdMap =
				 * temp.getFarmerDynamicFieldsValues().stream()
				 * .collect(Collectors.groupingBy(FarmerDynamicFieldsValue::
				 * getFieldName)); temp.getFarmerDynamicFieldsValues()
				 * .removeAll(temp.getFarmerDynamicFieldsValues().stream().
				 * filter( map -> (map.getIsMobileAvail() != null &&
				 * map.getIsMobileAvail().equals("2")))
				 * .collect(Collectors.toList())); List<Object[]> fdfvList =
				 * clientService.listMaxTypeByRefId(temp.getReferenceId());
				 * fieldTypeMap = fdfvList.stream()
				 * .collect(Collectors.toMap(obj -> String.valueOf(obj[0]), obj
				 * -> String.valueOf(obj[1])));
				 * 
				 * }
				 */

				// temp.setReferenceId(farmList);
				farmerDynamicData.setTxnUniqueId(DateUtil.getRevisionNumber());
				// temp.setSeason(season);
				// temp.setStatus("0");
				// temp.setCreatedDate(new Date());
				// temp.setCreatedUser(getUsername());
				String nowDate = DateUtil.convertDateToString(new Date(), (getGeneralDateFormat() + " HH:mm:ss"));
				farmerDynamicData
						.setUpdatedDate(DateUtil.convertStringToDate(nowDate, (getGeneralDateFormat() + " HH:mm:ss")));
				farmerDynamicData.setUpdatedUser(getUsername());
				farmerDynamicData.setActStatus(1);
				/*
				 * temp.setEntityId(entityType); temp.setTxnType(txnType);
				 */ /*
					 * temp.setFarmerDynamicFieldsValues(
					 * getDynamicFieldValues(temp.getReferenceId(), new
					 * HashSet<>(), "2"));
					 */
				Set<FarmerDynamicFieldsValue> fdvLi = getDynamicFieldValues(farmerDynamicData.getReferenceId(),
						new HashSet<>(), "2");
				// farmerDynamicData.getFarmerDynamicFieldsValues().removeIf(u
				// -> fdvLi.stream().map(ff ->
				// ff.getFieldName()).collect(Collectors.toList()).contains(u.getFieldName()));
				farmerDynamicData.getFarmerDynamicFieldsValues().addAll(fdvLi);
				farmerDynamicData.setProfileUpdateFields(farmerDynamicData.getProfileUpdateFields());

				farmerService.saveOrUpdate(farmerDynamicData, fdMap, fieldConfigMap);
				if (fieldConfigMap.values().stream().anyMatch(p -> Arrays.asList("4").contains(p.getIsMobileAvail())
						&& p.getFormula() != null && !StringUtil.isEmpty(p.getFormula()))) {
					farmerService.processCustomisedFormula(farmerDynamicData, fieldConfigMap);
				}

				if (!farmerDynamicData.getTxnType().equals("381")) {
					farmerService.deleteChildObjects(farmerDynamicData.getTxnType());
				}

				/* farmerService.updateDynamicFarmerFieldComponentType(); */

				/*
				 * if (!StringUtil.isEmpty(getIcsType())) {
				 * farmerService.updateFarmICSStatusByFarmId(Long.valueOf(temp.
				 * getReferenceId()), getIcsType()); }
				 */
				// farmerService.update(temp);

			}
		}
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		Calendar currentDate = Calendar.getInstance();
		Calendar cal = (Calendar) currentDate.clone();
		cal.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) - 1);
		super.startDate = df.format(cal.getTime());
		super.endDate = df.format(currentDate.getTime());

		daterange = super.startDate + " - " + super.endDate;
		command = "list";
		return LIST;
	}

	public Integer getActStatuss() {
		return actStatuss;
	}

	public void setActStatuss(Integer actStatuss) {
		this.actStatuss = actStatuss;
	}

	public String getSelectedFarmerId() {
		return selectedFarmerId;
	}

	public void setSelectedFarmerId(String selectedFarmerId) {
		this.selectedFarmerId = selectedFarmerId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public void detailFarmAddressSameAsFarmer() throws Exception {

		String result = "";
		if (!StringUtil.isEmpty(selectedFarmerId)) {
			List<Object[]> farmer = farmerService.listFarmerAddressById(Long.valueOf(selectedFarmerId));
			if (!ObjectUtil.isEmpty(farmer)) {
				for (Object[] obj : farmer) {
					if (!ObjectUtil.isEmpty(obj[0])) {
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

	public void detailFarmArea() throws Exception {

		String result = "";
		if (!StringUtil.isEmpty(selectedFarmerId)) {
			Farm farm = farmerService.findFarmByID(Long.valueOf(selectedFarmerId));
			if (!ObjectUtil.isEmpty(farm)) {
				/*
				 * for (Object[] obj : farmer) { if
				 * (!ObjectUtil.isEmpty(obj[0])) { result = obj[0] + "," +
				 * obj[1] + "," + obj[2]; } else { result = obj[1] + "," +
				 * obj[2]; }
				 * 
				 * if (result.trim().length() > 255) result = obj[0].toString();
				 * }
				 */
				result = farm.getFarmDetailedInfo().getProposedPlantingArea();
			}

		}
		response.setContentType("text/html");
		response.getWriter().print(result);
	}

	public String getCreatedUsername() {
		return createdUsername;
	}

	public void setCreatedUsername(String createdUsername) {
		this.createdUsername = createdUsername;
	}

	public String getFarmAcerage() {
		return farmAcerage;
	}

	public void setFarmAcerage(String farmAcerage) {
		this.farmAcerage = farmAcerage;
	}

	public IAgentService getAgentService() {
		return agentService;
	}

	public void setAgentService(IAgentService agentService) {
		this.agentService = agentService;
	}

}
