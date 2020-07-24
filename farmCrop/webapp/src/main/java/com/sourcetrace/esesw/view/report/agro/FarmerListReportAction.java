package com.sourcetrace.esesw.view.report.agro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.script.ScriptException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.profile.CropHarvest;
import com.ese.entity.profile.CropHarvestDetails;
import com.ese.entity.profile.CropSupply;
import com.ese.entity.profile.CropSupplyDetails;
import com.ese.entity.txn.mfi.InterestCalcConsolidated;
import com.ese.entity.util.ESESystem;
import com.ese.entity.util.FarmCropsField;
import com.ese.entity.util.FarmField;
import com.ese.entity.util.FarmerField;
import com.ese.util.Base64Util;
import com.ese.view.profile.FarmerAction;
import com.google.gson.Gson;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.FarmCatalogueMaster;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.inspect.agrocert.CertificateCategory;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.ICardService;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.entity.JsonDataObject;
import com.sourcetrace.esesw.entity.profile.AFLExport;
import com.sourcetrace.esesw.entity.profile.AnimalHusbandary;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.BankInformation;
import com.sourcetrace.esesw.entity.profile.Coordinates;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.ESECard;
import com.sourcetrace.esesw.entity.profile.Expenditure;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.FarmCropsCoordinates;
import com.sourcetrace.esesw.entity.profile.FarmICS;
import com.sourcetrace.esesw.entity.profile.FarmIcsConversion;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicData;
import com.sourcetrace.esesw.entity.profile.FarmerEconomy;
import com.sourcetrace.esesw.entity.profile.FarmerLandDetails;
import com.sourcetrace.esesw.entity.profile.FarmerScheme;
import com.sourcetrace.esesw.entity.profile.FarmerSoilTesting;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.HousingInfo;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.State;
import com.sourcetrace.esesw.entity.profile.TreeDetail;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

public class FarmerListReportAction extends BaseReportAction implements IExporter {

	private static final long serialVersionUID = 1L;
	public static final int SELECT = -1;
	private IFarmerService farmerService;
	private IClientService clientService;
	private InputStream fileInputStream;
	private String enrolledDate;
	private String farmerId;
	private String farmerCode;
	private String farmerDetails;
	private Farmer filter;
	private FarmCrops filter1;
	private Farm filter2;
	private Farmer farmer;
	private FarmCrops farmCrops;
	private String organicStatus;
	@Autowired
	private IProductDistributionService productDistributionService;
	@Autowired
	private IAgentService agentService;
	private ILocationService locationService;
	private String firstName;
	private String lastName;
	private String villageName;
	private String branchIdParma;
	private String checkValue;
	private String exportLimit;
	private String cropCategory;
	private String farmerCodeEnabled;
	private String farmerName;
	private String fatherName;
	private String stateName;
	private String fpo;
	private String icsName;
	private String gender;
	private String season;
	private String icsType;
	private Map<String, String> fields = new HashMap<>();
	private Map<String, String> genderList = new LinkedHashMap<String, String>();
	private Map<Integer, String> icsTypeList = new LinkedHashMap<Integer, String>();
	private Map<String, String> seasonList = new LinkedHashMap<String, String>();
	Map<String, String> organicStatuslist = new LinkedHashMap<>();
	private String farmSize;
	private String daterange;
	private String lgId;
	Map<String, String> cropNameMap = new HashMap<>();
	Map<String, String> icsMap = new HashMap<>();
	private String surName;
	private String staticMapUrl;
	private String digitalSignatureEnabled;
	Map<String, String> agentList = new LinkedHashMap<String, String>();
	private String txnType;
	List<Warehouse> samithi = new ArrayList<Warehouse>();
	private String samithiName;
	private String createdUserName;
	@Autowired
	private IPreferencesService preferncesService;
	@Autowired
	private ICatalogueService catalogueService;
	@Autowired
	private IDeviceService deviceService;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	public static SimpleDateFormat getFilenamedateformat() {

		return fileNameDateFormat;
	}

	private String xlsFileName;

	public String getXlsFileName() {

		return xlsFileName;
	}

	public Farmer getFarmer() {

		return farmer;
	}

	public void setFarmer(Farmer farmer) {

		this.farmer = farmer;
	}

	private Farm farm;
	private String searchPage;
	private JsonDataObject jsonDataObject = new JsonDataObject();
	private List<Farm> farmList = new ArrayList<Farm>();
	Map<String, String> farmerCodList = new LinkedHashMap<>();
	Map<String, String> farmerNameList = new LinkedHashMap<>();
	Map<String, String> farmerFarmerIdList = new LinkedHashMap<>();
	Map<String, String> fatherNameList = new LinkedHashMap<>();
	Map<String, String> surNameList = new LinkedHashMap<>();
	Map<String, String> villageMap = new LinkedHashMap<>();
	Map<String, String> lgMap = new LinkedHashMap<>();
	private AFLExport filter3;
	private String insYear;
	private String farmerImageByteString;
	private List<JSONObject> jsonObjectList;

	// export farmer related
	private ICardService cardService;
	private IAccountService accountService;
	private ESECard card;
	private InterestCalcConsolidated interestCalcConsolidated;
	private String isInterestModule = "0";
	private String fpoEnabled;
	private String farmerBankInfoEnabled;
	private String idProofEnabled;
	private String insuranceInfoEnabled;
	private String gramPanchayatEnable;
	private String fingerPrintEnabled;
	private String bankAccType;
	private String dateOfBirth;
	private String loanRepaymentDate;
	private String dateOfJoining;
	private String icsCode;
	private String accBalance;
	private String idProofImgString;
	private String idImgAvil;
	private String fingerPrintImageByteString;
	private boolean farmerAndContractStatus = false;
	private FarmerEconomy farmerEconomy;
	private String rupee;
	private String paise;
	private String icsString;
	private String icsUnitNoString;
	private String icsRegNoString;
	private String investigatorDate;
	private Map<Integer, String> enrollmentMap = new LinkedHashMap<Integer, String>();
	Map<Integer, String> isFarmerCertified = new LinkedHashMap<Integer, String>();
	public static final int OTHER = 99;
	private Map<Integer, String> certificationTypes = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> educationList = new LinkedHashMap<Integer, String>();

	// export Farm related
	private String sangham;
	private List<FarmICS> farmICSs;
	private String farmerUniqueId;
	private boolean isCertifiedFarmer = false;
	private List<CropHarvest> harvest = new ArrayList<CropHarvest>();
	private List<CropHarvestDetails> harvestDetails = new ArrayList<CropHarvestDetails>();
	private HousingInfo housingInfo;
	private List<CropSupply> harvestSupply = new ArrayList<CropSupply>();
	private Set<CropSupplyDetails> cropSupplyDetails;
	private String dateOfInspection;
	private String lastDateChemical;
	private String farmOwnedDetail;
	private String farmIrrigationDetail;
	private Map<Integer, String> farmIrrigationList = new LinkedHashMap<Integer, String>();
	public static final String SELECT_MULTI = "-1";
	private String irrigationSourceDetail;
	private String isIrrigation;
	private String soilTypeDetail;
	private String methodOfIrrigationDetail;
	private String soilFertilityDetail;
	private Map<Integer, String> soilFertilityList = new LinkedHashMap<Integer, String>();
	private String landUnderICSStatusDetail;
	private Map<Integer, String> icsStatusList = new LinkedHashMap<Integer, String>();
	private String soilTextureDetail;
	private String landGradientDetail;
	private String selectedRoadString;
	private String sessionYearString;
	private Map<Integer, String> approadList = new LinkedHashMap<Integer, String>();
	private String regYearString;
	private String benefitaryCode;
	private String benefitary;
	private String schemeCode;
	private String schemeTxt;
	private String selectedWaterSource;
	private String selectedInputSource;
	private String scopeName;
	private String audioDownloadString;
	private String waterHarvests;
	private String presenceOfBanana;
	private Map<Integer, String> bananaTreesList = new LinkedHashMap<Integer, String>();
	private String parallelProduction;
	private Map<Integer, String> parallelProductionList = new LinkedHashMap<Integer, String>();
	private String hiredLabours;
	private Map<Integer, String> hiredLabourList = new LinkedHashMap<Integer, String>();
	private String riskCategory;
	private Map<Integer, String> riskCategoryList = new LinkedHashMap<Integer, String>();
	private List<TreeDetail> treeDetails;
	private String farmImageByteString;
	List<FarmerSoilTesting> farmerSoilTestingList = new LinkedList<>();
	private List<FarmerLandDetails> farmerLandDetailsList = new ArrayList<FarmerLandDetails>();
	private List<Expenditure> expenditureList;
	List<FarmCatalogue> catalogueList = new ArrayList<FarmCatalogue>();
	List<FarmCatalogue> catalogueList1 = new ArrayList<FarmCatalogue>();
	List<FarmCatalogue> catalogueList2 = new ArrayList<FarmCatalogue>();
	List<FarmCatalogue> catalogueList3 = new ArrayList<FarmCatalogue>();
	List<FarmCatalogue> catalogueList4 = new ArrayList<FarmCatalogue>();
	List<FarmCatalogue> machinaryList = new ArrayList<FarmCatalogue>();
	List<FarmCatalogue> polyList = new ArrayList<FarmCatalogue>();

	// export farm crops related
	private String farmId;
	private String cropInfoEnabled;
	private String sowingDate;
	private String harvestDate;

	// export ics related
	private FarmerDynamicData farmerDynamicData;
	private Integer seasonType;
	private String entityType;
	private String farmer_ics;
	private String farmList_ics;
	private String group;
	private String selectedVillage;
	private String correctiveActionPlan;
	private String insDate;
	private String inspectorName;
	private String inspectorMobile;
	private String insType;
	private String scope;
	private String totLand;
	private String orgLand;
	private String totSite;
	private ProcurementVariety farmCropList;
	private String ics_farm_id;
	private String ics_entityType;
	private String ics_dynamic_data_id;

	public String getIcs_dynamic_data_id() {
		return ics_dynamic_data_id;
	}

	public void setIcs_dynamic_data_id(String ics_dynamic_data_id) {
		this.ics_dynamic_data_id = ics_dynamic_data_id;
	}

	public String getIcs_farm_id() {
		return ics_farm_id;
	}

	public void setIcs_farm_id(String ics_farm_id) {
		this.ics_farm_id = ics_farm_id;
	}

	public String getIcs_entityType() {
		return ics_entityType;
	}

	public void setIcs_entityType(String ics_entityType) {
		this.ics_entityType = ics_entityType;
	}

	public String list() throws Exception {

		Calendar currentDate = Calendar.getInstance();
		Calendar cal = (Calendar) currentDate.clone();
		cal.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) - 1);
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		super.startDate = df.format(cal.getTime());
		super.endDate = df.format(currentDate.getTime());
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			setExportLimit(preferences.getPreferences().get(ESESystem.EXPORT_RECORD_LIMIT));
		}
		daterange = super.startDate + " - " + super.endDate;

		request.setAttribute(HEADING, getText(LIST));
		if (!StringUtil.isEmpty(preferences)) {
			setExportLimit(preferences.getPreferences().get(ESESystem.EXPORT_RECORD_LIMIT));
			setFarmerCodeEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE));
		}

		genderList.put("MALE", getText("MALE"));
		genderList.put("FEMALE", getText("FEMALE"));
		/* commented due to performance issue */
		/*
		 * List<Object[]> farmerList = farmerService.listFarmerInfo(); if
		 * (!ObjectUtil.isEmpty(farmerList)) { farmerList.stream().forEach(obj
		 * -> {
		 * 
		 * if (!villageMap.containsKey(obj[11].toString())) {
		 * villageMap.put(obj[11].toString(), obj[6].toString()); }
		 * 
		 * if (obj[2] != null && !farmerCodList.containsKey(obj[2].toString()))
		 * { farmerCodList.put(obj[2].toString(), obj[2].toString()); }
		 * 
		 * if (!farmerNameList.containsKey(obj[3])) {
		 * farmerNameList.put(obj[3].toString(), obj[3].toString()); }
		 * 
		 * if (!farmerFarmerIdList.containsKey(obj[1])) {
		 * farmerFarmerIdList.put(obj[1].toString(), obj[1].toString()); }
		 * 
		 * if (!fatherNameList.containsKey(obj[4]) && obj[4] != null) {
		 * fatherNameList.put(obj[4].toString(), obj[4].toString()); }
		 * 
		 * if (!surNameList.containsKey(obj[5]) && obj[5] != null) {
		 * surNameList.put(obj[5].toString(), obj[5].toString()); }
		 * 
		 * if (!lgMap.containsKey(obj[12].toString())) {
		 * lgMap.put(obj[12].toString(), obj[13].toString()); }
		 * 
		 * }); }
		 */

		icsTypeList.put(FarmIcsConversion.ICSTypes.ICS_1.ordinal(),
				getText("icsStatus" + FarmIcsConversion.ICSTypes.ICS_1.ordinal()));
		icsTypeList.put(FarmIcsConversion.ICSTypes.ICS_2.ordinal(),
				getText("icsStatus" + FarmIcsConversion.ICSTypes.ICS_2.ordinal()));
		icsTypeList.put(FarmIcsConversion.ICSTypes.ICS_3.ordinal(),
				getText("icsStatus" + FarmIcsConversion.ICSTypes.ICS_3.ordinal()));
		icsTypeList.put(FarmIcsConversion.ICSTypes.ORGANIC.ordinal(),
				getText("icsStatus" + FarmIcsConversion.ICSTypes.ORGANIC.ordinal()));

		setFilter1(new FarmCrops());

		return LIST;
	}

	public String data() throws Exception {
		Map data = new LinkedHashMap<>();
		Farm farm = new Farm();
		FarmIcsConversion farmics = new FarmIcsConversion();
		Set<FarmIcsConversion> ics = new HashSet<FarmIcsConversion>();
		Map<String, String> searchRecord = getJQGridRequestParam();
		filter = new Farmer();

		/*
		 * if (!StringUtil.isEmpty(firstName)) { filter.setFirstName(firstName);
		 * }
		 */
		if (!StringUtil.isEmpty(searchRecord.get("farmerCode")))
			filter.setFarmerCode(searchRecord.get("farmerCode").trim());

		if (!StringUtil.isEmpty(searchRecord.get("firstName")))
			filter.setFirstName(searchRecord.get("firstName").trim());

		if (!StringUtil.isEmpty(villageName)) {
			filter.setVillage(locationService.findVillageByCode(villageName.trim()));
		}
		if (!StringUtil.isEmpty(stateName)) {
			filter.setStateName(stateName);
		}
		if (!StringUtil.isEmpty(fpo)) {
			filter.setFpo(fpo);
		}
		if (!StringUtil.isEmpty(icsName)) {
			filter.setIcsName(icsName);
		}
		if (!StringUtil.isEmpty(icsType)) {
			filter.setIcsName(icsType);
		}
		if (getCurrentTenantId().equalsIgnoreCase("avt") || getCurrentTenantId().equalsIgnoreCase("susagri")) {
			if (!StringUtil.isEmpty(samithiName)) {
				filter.setSamithi(locationService.findSamithiById(Long.valueOf(samithiName.trim())));
			}
		}
		if (getCurrentTenantId().equalsIgnoreCase("iccoa")) {
			if (!StringUtil.isEmpty(insYear)) {
				filter.setInsyear(insYear);
			} else {
				filter.setInsyear(DateUtil.getYearByDateTime(new Date()));
			}
		}
		if (!StringUtil.isEmpty(searchRecord.get("farmerId")))
			filter.setFarmerId(searchRecord.get("farmerId").trim());

		if (!StringUtil.isEmpty(gender)) {
			filter.setGender(gender);
		}
		if (!StringUtil.isEmpty(season)) {
			filter.setSeasonCode(season);
		}

		if (!StringUtil.isEmpty(createdUserName)) {
			filter.setCreatedUsername(createdUserName);
		}

		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)) {
			filter.setOrganicStatus("-1");
			if (!StringUtil.isEmpty(organicStatus)) {

				if (!organicStatus.equalsIgnoreCase("conventional")) {
					farmics.setOrganicStatus(organicStatus);
					farm.setFarmIcsConv(farmics);
					ics.add(farm.getFarmIcsConv());
					if (organicStatus.equalsIgnoreCase("3")) {
						filter.setOrganicStatus("3");
						filter.setIsCertifiedFarmer(1);
					} else {
						filter.setOrganicStatus("0");
						filter.setIsCertifiedFarmer(1);
					}

				} else {
					filter.setOrganicStatus("1");
					filter.setIsCertifiedFarmer(0);
				}
			}
		}
		if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
			if (!getIsMultiBranch().equalsIgnoreCase("1")) {
				List<String> branchList = new ArrayList<>();
				branchList.add(searchRecord.get("branchId"));
				filter.setBranchesList(branchList);
			} else {
				List<String> branchList = new ArrayList<>();
				List<BranchMaster> branches = clientService.listChildBranchIds(searchRecord.get("branchId"));
				branchList.add(searchRecord.get("branchId"));
				branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
					branchList.add(branch.getBranchId());
				});
				filter.setBranchesList(branchList);
			}
		}

		if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
			if (!getIsMultiBranch().equalsIgnoreCase("1")) {
				List<String> branchList = new ArrayList<>();
				branchList.add(searchRecord.get("branchId").trim());
				filter.setBranchesList(branchList);
			} else {
				List<String> branchList = new ArrayList<>();
				List<BranchMaster> branches = clientService.listChildBranchIds(searchRecord.get("branchId").trim());
				branchList.add(searchRecord.get("branchId").trim());
				branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
					branchList.add(branch.getBranchId());
				});
				filter.setBranchesList(branchList);
			}
		}

		if (!StringUtil.isEmpty(searchRecord.get("subBranchId"))) {
			filter.setBranchId(searchRecord.get("subBranchId").trim());
		}

		super.filter = this.filter;
		data = readData("farmerProp");

		List<Object[]> icsList = farmerService.listFarmIcsByFarmId();
		// icsMap = icsList.stream().collect(Collectors.toMap(obj ->
		// String.valueOf(obj[0]), obj -> String.valueOf(obj[1])));
		List<Object[]> croipName = farmerService.listCropNamesWithFarm();
		cropNameMap = croipName.stream()
				.collect(Collectors.toMap(obj -> String.valueOf(obj[0]), obj -> String.valueOf(obj[1])));

		return sendJSONResponse(data);
	}

	public String subGridDetail() throws Exception {
		FarmIcsConversion farmics = new FarmIcsConversion();
		Farm farm = new Farm();
		Set<FarmIcsConversion> ics = new HashSet<FarmIcsConversion>();
		if (ObjectUtil.isEmpty(this.filter))
			farm.setFarmer(new Farmer());

		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)) {
			farm.getFarmer().setOrganicStatus("-1");
			if (!StringUtil.isEmpty(organicStatus)) {

				if (!organicStatus.equalsIgnoreCase("conventional")) {
					farmics.setOrganicStatus(organicStatus);
					farm.setFarmIcsConv(farmics);
					ics.add(farm.getFarmIcsConv());
					if (organicStatus.equalsIgnoreCase("3")) {
						farm.getFarmer().setOrganicStatus("3");
						farm.getFarmer().setIsCertifiedFarmer(1);
					} else {
						farm.getFarmer().setOrganicStatus("0");
						farm.getFarmer().setIsCertifiedFarmer(1);
					}

				} else {
					farm.getFarmer().setOrganicStatus("1");
					farm.getFarmer().setIsCertifiedFarmer(0);
				}
			}
		}
		/*
		 * farmer.setIsCertifiedFarmer(-1); if
		 * (!StringUtil.isEmpty(organicStatus)) {
		 * 
		 * if(!organicStatus.equalsIgnoreCase("conventional")){
		 * farmics.setOrganicStatus(organicStatus);
		 * farm.setFarmIcsConv(farmics); ics.add(farm.getFarmIcsConv());
		 * farmer.setIsCertifiedFarmer(1);
		 * 
		 * }else{ farmer.setIsCertifiedFarmer(0); } }
		 */
		farm.getFarmer().setId(Long.valueOf(id));
		super.filter = farm;
		Map data = readData();
		return sendJSONResponse(data);

	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		farmerCodeEnabled = preferncesService.findPrefernceByName(ESESystem.ENABLE_FARMER_CODE);
		JSONObject jsonObject = new JSONObject();
		if (obj instanceof Farm) {
			Farm farm = (Farm) obj;
			JSONArray rows = new JSONArray();
			rows.add(farm.getFarmCode());
			rows.add(farm.getFarmName());
			if (farm.getFarmer().getIsCertifiedFarmer() == 1) {
				FarmIcsConversion fIcs = farmerService
						.findFarmIcsConversionByFarmIdWithActive(Long.valueOf(farm.getId()));
				if (!ObjectUtil.isEmpty(fIcs) && fIcs != null) {
					rows.add(fIcs.getOrganicStatus().equalsIgnoreCase("3") ? getLocaleProperty("alrdyCertified")
							: getLocaleProperty("inprocess"));
				} else {
					rows.add("Conventional");
				}
			} else {
				rows.add("Conventional");
			}
			rows.add(!ObjectUtil.isListEmpty(farm.getFarmICSConversion())
					? !StringUtil.isEmpty(farm.getFarmICSConversion().iterator().next().getIcsType())
							? getText("icsStatus" + farm.getFarmICSConversion().iterator().next().getIcsType()) : ""
					: "");
			rows.add(farm.getFarmDetailedInfo().getTotalLandHolding());
			String prods = farm.getFarmCrops() != null
					? farm.getFarmCrops().stream().map(e -> e.getProcurementVariety().getProcurementProduct().getName())
							.distinct().reduce("", (a, b) -> a + "," + b)
					: null;
			rows.add(!StringUtil.isEmpty(prods) ? prods.substring(1) : "");

			/*
			 * String farmCrops=farm.getFarmCrops().stream().filter(f ->
			 * !StringUtil.isEmpty(f.getProcurementVariety().
			 * getProcurementProduct().getName())) .map(fc ->
			 * fc.getProcurementVariety().getProcurementProduct().getName().
			 * toString()) .collect( Collectors.joining(",") );
			 */
			rows.add(prods);

			jsonObject.put("id", farm.getId());
			jsonObject.put("cell", rows);
		} else {
			Object[] data = (Object[]) obj;
			JSONArray rows = new JSONArray();
			// 0=Id,1=Farmer Id,2=Farmer Code,3=First name,4=Last name,5=sur
			// name,6=village name,7=samithi
			// name,8=isCertifiedFarmer,9=status,10=branchId,11=icsName,12=City
			// name,13=District name,14=State name,15=Cooperative,20=rifanId
			
			if (!ObjectUtil.isEmpty(data[11])) {
				if ((getIsMultiBranch().equalsIgnoreCase("1")
						&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

					if (StringUtil.isEmpty(branchIdValue)) {
						rows.add(
								!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(data[11].toString())))
										? getBranchesMap().get(getParentBranchMap().get(data[11].toString()))
										: getBranchesMap().get(data[11].toString()));
					}
					rows.add(getBranchesMap().get(data[11].toString()));
				} else {
					if (StringUtil.isEmpty(branchIdValue)) {
						rows.add(branchesMap.get(data[11].toString()));
					}
				}
			} else {
				rows.add("");
			}
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.OCP)) {
				rows.add(!ObjectUtil.isEmpty(data[29]) ? data[29] : "");// FarmerId
																		// (OCP)
			}
			if (farmerCodeEnabled.equalsIgnoreCase("1")) {
				rows.add(!ObjectUtil.isEmpty(data[3]) ? data[3] : "");// FarmerCode
			}
			if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)
					&& !getCurrentTenantId().equalsIgnoreCase(ESESystem.OCP)) {
				if (!ObjectUtil.isEmpty(data[20])) {
					HarvestSeason season = getHarvestSeason(String.valueOf(data[20]));
					rows.add(StringUtil.isEmpty(season) ? "" : season.getName());// Season
				} else {
					rows.add("");
				}
			}
		
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.AVT)) {
				rows.add(!ObjectUtil.isEmpty(data[31]) ? data[31] : "");// created
																		// user
			}
			String firstName =  String.valueOf(data[2]);
			String farmerId = String.valueOf(data[0]);
			if(!ObjectUtil.isEmpty(farmerId) && farmerId!=null){
				String linkField = "<a href=farmer_detail.action?id="+farmerId+" target=_blank>"+firstName+"</a>";
				rows.add(!ObjectUtil.isEmpty(linkField) ? linkField : "");
				}else{
					rows.add(firstName);
				}
			/*String linkField = "<a href=farmer_detail.action?id="+farmerId+" target=_blank>"+firstName+"</a>";
			rows.add(!ObjectUtil.isEmpty(linkField) ? linkField : "");// Farmer
*/			if (!getCurrentTenantId().equalsIgnoreCase("gsma")) {
				rows.add(!ObjectUtil.isEmpty(data[4]) ? data[4] : "");
			} // Gender
			if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)
					&& !getCurrentTenantId().equalsIgnoreCase("gsma")) {
				rows.add(!ObjectUtil.isEmpty(data[5]) ? data[5] : "");// Age
			}

			if (getCurrentTenantId().equalsIgnoreCase("ocp")) {
				rows.add(!ObjectUtil.isEmpty(data[8]) ? data[8] : "");
			} // Group

			// ics Name
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)
					&& (StringUtil.isEmpty(getBranchId()) || getBranchId().equalsIgnoreCase("organic"))) {
				rows.add(!ObjectUtil.isEmpty(data[17]) ? data[17].toString() : "");
				rows.add(!ObjectUtil.isEmpty(data[19]) ? data[19].toString() : "");
			}
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)
					&& (StringUtil.isEmpty(getBranchId()) || !getBranchId().equalsIgnoreCase("bci"))) {
				rows.add(!ObjectUtil.isEmpty(data[18]) ? data[18].toString() : "");// Farmer
																					// code
																					// tracenet
			}
			if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
				if (!ObjectUtil.isEmpty(data[12])) {
					FarmCatalogue catalogue = getCatlogueValueByCode(data[12].toString());
					rows.add(!ObjectUtil.isEmpty(catalogue) ? catalogue.getName() : "");
				} else {
					rows.add("");
				}
				if (!ObjectUtil.isEmpty(data[16])) {
					FarmCatalogue cat = getCatlogueValueByCode(data[16].toString());
					rows.add(!ObjectUtil.isEmpty(cat) ? cat.getName() : "");
				} else {
					rows.add("");
				}
			}
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.SUSAGRI)) {
				rows.add(!ObjectUtil.isEmpty(data[8]) ? data[8] : "");
			}
			
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)){
			rows.add(!ObjectUtil.isEmpty(data[25]) ? data[25].toString() : "");
			rows.add(!ObjectUtil.isEmpty(data[31]) ? data[31].toString() : "");
			List<String> agent = agentService.findAgentNameByWareHouseId(Long.valueOf(data[32].toString()));
			if (!ObjectUtil.isListEmpty(agent)) {
				String json = new Gson().toJson(agent);
				String result = json.replaceAll("\"", "");

				rows.add(result.substring(1, result.length()-1));
			//	rows.add(farmer.getFarms() !=null && !ObjectUtil.isEmpty(farmer.getFarms()) && farmer.getFarms().size()>0 ? String.valueOf(farmer.getFarms().size()) : "NA" );
				}
			else{
				rows.add("NA");
			}
			
			
			//rows.add(!ObjectUtil.isEmpty(data[31]) ? data[31].toString() : "");
		}
			rows.add(!ObjectUtil.isEmpty(data[7]) ? data[7] : "");// village
			rows.add(!ObjectUtil.isEmpty(data[13]) ? data[13] : "");
			rows.add(!ObjectUtil.isEmpty(data[14]) ? data[14] : "");
			rows.add(!ObjectUtil.isEmpty(data[15]) ? data[15] : "");
			/*
			 * if (getCurrentTenantId().equalsIgnoreCase("wilmar")) {
			 * rows.add(!ObjectUtil.isEmpty(data[10]) ? getText("status" +
			 * data[10]) : ""); rows.add(!ObjectUtil.isEmpty(data[27]) ?
			 * data[27] : ""); if (!ObjectUtil.isEmpty(data[9]) &&
			 * data[9].equals(1)) { rows.add(!ObjectUtil.isEmpty(data[28]) ?
			 * data[28] : ""); } else { rows.add("Conventional"); } }
			 */
			jsonObject.put("id", data[0]);
			jsonObject.put("cell", rows);
		}
		return jsonObject;
	}

	public String detail() throws Exception {

		String view = "";

		return view;
	}

	public Map<String, String> getFarmerList() {

		Map<String, String> returnMap = new HashMap<String, String>();

		List<Farmer> farmerList = farmerService.listFarmer();
		if (!ObjectUtil.isListEmpty(farmerList)) {
			for (Farmer farmer : farmerList) {
				returnMap.put(farmer.getFarmerCode(),
						farmer.getFarmerCode() + " " + farmer.getFirstName() + " " + farmer.getGender());
			}
		}
		return returnMap;
	}

	public String populateXLS() throws Exception {
		InputStream is;
		is = getFarmFileInputStream(IExporter.EXPORT_MANUAL);
		if (!ObjectUtil.isEmpty(is)) {
			setXlsFileName(getLocaleProperty("FarmerListReport") + fileNameDateFormat.format(new Date()));
			Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
			fileMap.put(xlsFileName, is);
			setFileInputStream(
					FileUtil.createFileInputStreamToZipFile(getLocaleProperty("farmerList").trim(), fileMap, ".xls"));
			return "xls";
		} else {
			return LIST;
		}
	}

	private void setXlsFileName(String xlsFileName) {

		this.xlsFileName = xlsFileName;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Farmer> getAllRecords() {

		filter = new Farmer();
		super.filter = this.filter;
		Map data = readData("farmerList");
		List<Farmer> list = (List) data.get(ROWS);
		return list;
	}

	public Map<String, String> getStateList() {

		Map<String, String> stateMap = new LinkedHashMap<String, String>();

		List<State> stateList = locationService.listOfStates();
		for (State obj : stateList) {
			stateMap.put(obj.getCode(), obj.getName());

		}
		return stateMap;

	}

	public Map<String, String> getWarehouseList() {
		Map<String, String> warehouseMap = new LinkedHashMap<>();
		AtomicInteger i = new AtomicInteger(0);
		FarmCatalogueMaster farmCatalougeMaster = catalogueService.findFarmCatalogueMasterByName("cooperative");
		if (!ObjectUtil.isEmpty(farmCatalougeMaster)) {
			Double d = new Double(farmCatalougeMaster.getId());
			List<FarmCatalogue> farmCatalougeList = catalogueService.findFarmCatalougeByAlpha(d.intValue());

			for (FarmCatalogue catalogue : farmCatalougeList) {
				if (!catalogue.getName().trim().contentEquals(ESESystem.OTHERS)) {
					warehouseMap.put(catalogue.getCode(), catalogue.getName());
				}
			}

		}
		// warehouseMap.put("99", "Others");
		return warehouseMap;

	}

	public Map<String, String> getIcsNameList() {

		AtomicInteger i = new AtomicInteger(0);
		Map<String, String> icsMap = new LinkedHashMap<>();
		FarmCatalogueMaster farmCatalougeMaster = catalogueService
				.findFarmCatalogueMasterByName(getLocaleProperty("icsName"));
		if (!ObjectUtil.isEmpty(farmCatalougeMaster)) {
			Double d = new Double(farmCatalougeMaster.getId());
			List<FarmCatalogue> farmCatalougeList = catalogueService.findFarmCatalougeByAlpha(d.intValue());

			for (FarmCatalogue catalogue : farmCatalougeList) {
				if (!catalogue.getName().trim().contentEquals(ESESystem.OTHERS)) {
					icsMap.put(catalogue.getCode(), catalogue.getName());
				}
			}

		}
		icsMap.put("99", "Others");

		return icsMap;

	}

	public int getPicIndex(HSSFWorkbook wb) throws IOException {

		int index = -1;

		byte[] picData = null;
		picData = clientService.findLogoByCode(Asset.APP_LOGO);

		if (picData != null)
			index = wb.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
	}

	public IProductDistributionService getProductDistributionService() {

		return productDistributionService;
	}

	public void setProductDistributionService(IProductDistributionService productDistributionService) {

		this.productDistributionService = productDistributionService;
	}

	public void setFileInputStream(InputStream fileInputStream) {

		this.fileInputStream = fileInputStream;
	}

	public InputStream getFileInputStream() {

		return fileInputStream;
	}

	public void setEnrolledDate(String enrolledDate) {

		this.enrolledDate = enrolledDate;
	}

	public String getEnrolledDate() {

		return enrolledDate;
	}

	public String getFarmerId() {

		return farmerId;
	}

	public void setFarmerId(String farmerId) {

		this.farmerId = farmerId;
	}

	public void setFilter(Farmer filter) {

		this.filter = filter;
	}

	public Farmer getFilter() {

		return filter;
	}

	public IFarmerService getFarmerService() {

		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {

		this.farmerService = farmerService;
	}

	public void setClientService(IClientService clientService) {

		this.clientService = clientService;
	}

	public IClientService getClientService() {

		return clientService;
	}

	public void setFarmerCode(String farmerCode) {

		this.farmerCode = farmerCode;
	}

	public String getFarmerCode() {

		return farmerCode;
	}

	public void setFarmerDetails(String farmerDetails) {

		this.farmerDetails = farmerDetails;
	}

	public String getFarmerDetails() {

		return farmerDetails;
	}

	public void setFarm(Farm farm) {

		this.farm = farm;
	}

	public Farm getFarm() {

		return farm;
	}

	public FarmCrops getFarmCrops() {

		return farmCrops;
	}

	public void setFarmCrops(FarmCrops farmCrops) {

		this.farmCrops = farmCrops;
	}

	public ILocationService getLocationService() {

		return locationService;
	}

	public void setLocationService(ILocationService locationService) {

		this.locationService = locationService;
	}

	public String getFirstName() {

		return firstName;
	}

	public void setFirstName(String firstName) {

		this.firstName = firstName;
	}

	public String getLastName() {

		return lastName;
	}

	public void setLastName(String lastName) {

		this.lastName = lastName;
	}

	public String getVillageName() {

		return villageName;
	}

	public void setVillageName(String villageName) {

		this.villageName = villageName;
	}

	public String getBranchIdParma() {

		return branchIdParma;
	}

	public void setBranchIdParma(String branchIdParma) {

		this.branchIdParma = branchIdParma;
	}

	public String getCheckValue() {

		return checkValue;
	}

	public void setCheckValue(String checkValue) {

		this.checkValue = checkValue;
	}

	private InputStream getFarmFileInputStream(String exportType) throws IOException {

		Map<String, String> searchRecord = getJQGridRequestParam();

		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		setsDate(IExporter.EXPORT_DAILY.equalsIgnoreCase(exportType) ? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));
		boolean flag = true;
		DateFormat filterDateFormat = new SimpleDateFormat(FILTERDATE);

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("exportXLSFarmerTitle"));
		HSSFPatriarch drawing = sheet.createDrawingPatriarch();

		HSSFCellStyle style1 = wb.createCellStyle();
		HSSFCellStyle style2 = wb.createCellStyle();
		HSSFCellStyle style3 = wb.createCellStyle();
		HSSFCellStyle filterStyle = wb.createCellStyle();

		HSSFFont font1 = wb.createFont();
		font1.setFontHeightInPoints((short) 22);

		HSSFFont font2 = wb.createFont();
		font2.setFontHeightInPoints((short) 12);

		HSSFFont font3 = wb.createFont();
		font3.setFontHeightInPoints((short) 10);

		HSSFFont filterFont = wb.createFont();
		filterFont.setFontHeightInPoints((short) 12);

		HSSFRow row, titleRow, filterRowTitle, filterRow1, filterRow2, filterRow3, filterRow4 = null, filterRow5 = null,
				filterRow6 = null, filterRow7 = null, filterRow8 = null, filterRow9 = null, filterRow10 = null,
				filterRow11 = null, filterRow12 = null;
		;
		HSSFCell cell;

		int imgRow1 = 0;
		int imgRow2 = 4;
		int imgCol1 = 0;
		int imgCol2 = 0;
		int titleRow1 = 2;
		int titleRow2 = 5;

		int rowNum = 2;
		int colNum = 0;

		
		  sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1,imgCol2)); 
		  sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, titleRow1, titleRow2));
		

		branchIdValue = getBranchId();
		buildBranchMap();

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(2);
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cell.setCellValue(new HSSFRichTextString(getText("exportXLSFarmerTitle")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);
		// if
		// (getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)) {
		Map data1;
		Farm farm = new Farm();
		FarmIcsConversion farmics = new FarmIcsConversion();
		Set<FarmIcsConversion> ics = new HashSet<FarmIcsConversion>();
		filter = new Farmer();
		if (!StringUtil.isEmpty(searchRecord.get("firstName")))
			filter.setFirstName(searchRecord.get("firstName").trim());

		if (!StringUtil.isEmpty(searchRecord.get("farmerCode")))
			filter.setFarmerCode(searchRecord.get("farmerCode").trim());

		if (!StringUtil.isEmpty(villageName)) {
			filter.setVillage(locationService.findVillageByCode(villageName.trim()));
		}
		if (!StringUtil.isEmpty(stateName)) {
			filter.setStateName(stateName);
		}
		if (!StringUtil.isEmpty(fpo)) {
			filter.setFpo(fpo);
		}
		if (!StringUtil.isEmpty(icsName)) {
			filter.setIcsName(icsName);
		}
		if (!StringUtil.isEmpty(season)) {
			filter.setSeasonCode(season);
		}

		if (!StringUtil.isEmpty(createdUserName)) {
			filter.setCreatedUsername(createdUserName);
		}

		if (!StringUtil.isEmpty(gender)) {
			filter.setGender(gender);
		}
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)) {
			filter.setOrganicStatus("-1");
			if (!StringUtil.isEmpty(organicStatus)) {

				if (!organicStatus.equalsIgnoreCase("conventional")) {
					farmics.setOrganicStatus(organicStatus);
					farm.setFarmIcsConv(farmics);
					ics.add(farm.getFarmIcsConv());
					if (organicStatus.equalsIgnoreCase("3")) {
						filter.setOrganicStatus("3");
						filter.setIsCertifiedFarmer(1);
					} else {
						filter.setOrganicStatus("0");
						filter.setIsCertifiedFarmer(1);
					}

				} else {
					filter.setOrganicStatus("1");
					filter.setIsCertifiedFarmer(0);
				}
			}
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

		if (!StringUtil.isEmpty(searchRecord.get("farmerId")))
			filter.setFarmerId(searchRecord.get("farmerId").trim());

		if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
			if (!getIsMultiBranch().equalsIgnoreCase("1")) {
				List<String> branchList = new ArrayList<>();
				branchList.add(searchRecord.get("branchId").trim());
				filter.setBranchesList(branchList);
			} else {
				List<String> branchList = new ArrayList<>();
				List<BranchMaster> branches = clientService.listChildBranchIds(searchRecord.get("branchId").trim());
				branchList.add(searchRecord.get("branchId").trim());
				branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
					branchList.add(branch.getBranchId());
				});
				filter.setBranchesList(branchList);
			}
		}

		if (!StringUtil.isEmpty(searchRecord.get("subBranchId"))) {
			filter.setBranchId(searchRecord.get("subBranchId").trim());
		}

		super.filter = this.filter;
		data1 = readData("farmerProp");

		if (isMailExport()) {
			rowNum++;
			rowNum++;
			if (!StringUtil.isEmpty(villageName) || !StringUtil.isEmpty(stateName) || !StringUtil.isEmpty(icsName)
					|| !StringUtil.isEmpty(fpo) || !StringUtil.isEmpty(icsType) || !StringUtil.isEmpty(gender)
					|| !StringUtil.isEmpty(season) || !StringUtil.isEmpty(createdUserName)) {
				filterRowTitle = sheet.createRow(rowNum++);
				cell = filterRowTitle.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("filter")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			filterRow1 = sheet.createRow(rowNum++);

			
			  /*cell = filterRow1.createCell(1); cell.setCellValue(new
			 HSSFRichTextString(getLocaleProperty("StartingDate")));
			  filterFont.setBoldweight((short) 12);
			  filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			  filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
			 
			  cell = filterRow1.createCell(2); cell.setCellValue(new
			  HSSFRichTextString(filterDateFormat.format(getsDate())));
			  filterFont.setBoldweight((short) 12);
			  filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			  filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
			  
			  filterRow2 = sheet.createRow(rowNum++); cell =
			  filterRow2.createCell(1); cell.setCellValue(new
			  HSSFRichTextString(getLocaleProperty("EndingDate")));
			  filterFont.setBoldweight((short) 12);
			  filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			  filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
			 
			 cell = filterRow2.createCell(2); cell.setCellValue(new
			  HSSFRichTextString(filterDateFormat.format(geteDate())));
			  filterFont.setBoldweight((short) 12);
			  filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			  filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
			 */

			filterRow1 = sheet.createRow(rowNum++);
			if (!StringUtil.isEmpty(filter.getFirstName())) {
				filterRow1 = sheet.createRow(rowNum++);

				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmer.firstName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow1.createCell(2);
				// cell.setCellValue(new
				// HSSFRichTextString(filter1.getFarm().getFarmer().getFirstName()));
				cell.setCellValue(new HSSFRichTextString(searchRecord.get("firstName").trim()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			if (!StringUtil.isEmpty(filter.getFarmerCode())) {

				filterRow3 = sheet.createRow(rowNum++);
				cell = filterRow3.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmerCode")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow3.createCell(2);
				cell.setCellValue(new HSSFRichTextString(searchRecord.get("farmerCode").trim()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			if (!StringUtil.isEmpty(villageName)) {

				filterRow4 = sheet.createRow(rowNum++);
				cell = filterRow4.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("villageName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow4.createCell(2);
				cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(villageName)
						? locationService.findVillageByCode(villageName).getName() : getText("NA"))));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

			}

			if (!StringUtil.isEmpty(stateName)) {
				filterRow5 = sheet.createRow(rowNum++);
				cell = filterRow5.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("stateName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow5.createCell(2);
				State s = locationService.findStateByCode(stateName);
				cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(s) ? s.getName() : "")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

			}

			if (!StringUtil.isEmpty(icsName)) {
				filterRow6 = sheet.createRow(rowNum++);
				cell = filterRow6.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("icsName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow6.createCell(2);
				FarmCatalogue fc = farmerService.findfarmcatalogueByCode(icsName);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(fc) ? fc.getName() : ""));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

			}
			if (!StringUtil.isEmpty(fpo)) {
				filterRow7 = sheet.createRow(rowNum++);
				cell = filterRow7.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("cooperative")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow7.createCell(2);
				FarmCatalogue fc = farmerService.findfarmcatalogueByCode(fpo);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(fc) ? fc.getName() : ""));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

			}

			if (!StringUtil.isEmpty(icsType)) {

				filterRow3 = sheet.createRow(rowNum++);
				cell = filterRow3.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("icsType")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow3.createCell(2);
				cell.setCellValue(new HSSFRichTextString(
						(!ObjectUtil.isEmpty(icsType) ? getText("icsStatus" + icsType) : getText("NA"))));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			if (!StringUtil.isEmpty(gender)) {

				filterRow9 = sheet.createRow(rowNum++);
				cell = filterRow9.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("gender")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow9.createCell(2);
				cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(gender) ? gender : getText("NA"))));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(season)) {
				filterRow1 = sheet.createRow(rowNum++);

				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("cSeasonCode")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow1.createCell(2);
				cell.setCellValue((farmerService.findSeasonNameByCode(season)).toString());
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(farmSize)) {
				filterRow10 = sheet.createRow(rowNum++);

				cell = filterRow10.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmSize")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow10.createCell(2);
				cell.setCellValue(getFarmSizeList().get(farmSize));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(filter.getLastName())) {
				filterRow11 = sheet.createRow(rowNum++);
				cell = filterRow11.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmer.lastName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow11.createCell(2);
				// cell.setCellValue(new
				// HSSFRichTextString(filter.getLastName()));
				cell.setCellValue(new HSSFRichTextString(searchRecord.get("lastName").trim()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

			}

			if (!StringUtil.isEmpty(filter.getSurName())) {
				filterRow12 = sheet.createRow(rowNum++);
				cell = filterRow12.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmer.surName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow12.createCell(2);
				// cell.setCellValue(new
				// HSSFRichTextString(filter.getSurName()));
				cell.setCellValue(new HSSFRichTextString(searchRecord.get("surName").trim()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

			}

			if (!StringUtil.isEmpty(filter.getOrganicStatus())) {
				filterRow12 = sheet.createRow(rowNum++);
				cell = filterRow12.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("organicStatus")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow12.createCell(2);
				// cell.setCellValue(new
				// HSSFRichTextString(filter.getSurName()));
				if (!filter.getOrganicStatus().equalsIgnoreCase("1")) {
					cell.setCellValue(new HSSFRichTextString(filter.getOrganicStatus().equalsIgnoreCase("3")
							? getLocaleProperty("alrdyCertified") : getLocaleProperty("inprocess")));

				} else {
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("Conventional")));
				}

				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

			}

			if (!StringUtil.isEmpty(lgId)) {
				filterRow10 = sheet.createRow(rowNum++);

				cell = filterRow10.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("locality.name")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow10.createCell(2);
				cell.setCellValue((locationService.findLocalityByCode(lgId)).getName());
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(createdUserName)) {
				filterRow10 = sheet.createRow(rowNum++);

				cell = filterRow10.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("Created User")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow10.createCell(2);
				cell.setCellValue(createdUserName);
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			/*
			 * if (!StringUtil.isEmpty(branchIdParma)) {
			 * 
			 * filterRow8 = sheet.createRow(rowNum++); cell =
			 * filterRow8.createCell(1); cell.setCellValue(new
			 * HSSFRichTextString(getLocaleProperty("BranchId")));
			 * filterFont.setBoldweight((short) 12);
			 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
			 * 
			 * cell = filterRow8.createCell(2); cell.setCellValue(new
			 * HSSFRichTextString(searchRecord.get("branchId").trim()));
			 * filterFont.setBoldweight((short) 12);
			 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
			 * 
			 * }
			 */
			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
				if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
					filterRow1 = sheet.createRow(rowNum++);
					cell = filterRow1.createCell(2);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("app.branch")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow1.createCell(3);
					// cell.setCellValue(new
					// HSSFRichTextString((branchesMap.get(filter.getBranchId()))));
					cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(searchRecord.get("branchId"))));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					if (!StringUtil.isEmpty(searchRecord.get("subBranchId"))) {

						filterRow1 = sheet.createRow(rowNum++);
						cell = filterRow1.createCell(2);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("app.subBranch")));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						cell = filterRow1.createCell(3);
						cell.setCellValue(
								new HSSFRichTextString(getBranchesMap().get(searchRecord.get("subBranchId"))));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);
					}
				} else {
					if (!StringUtil.isEmpty(searchRecord.get("subBranchId"))) {
						filterRow1 = sheet.createRow(rowNum++);
						cell = filterRow1.createCell(2);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("app.subBranch")));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						cell = filterRow1.createCell(3);
						cell.setCellValue(
								new HSSFRichTextString(getBranchesMap().get(searchRecord.get("subBranchId"))));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);
					}
				}
			}

			// filterRow6 = sheet.createRow(rowNum++);
			// filterRow7 = sheet.createRow(rowNum++);
		}
		++rowNum;

		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		String columnHeaders = "";

		setFarmerCodeEnabled(preferncesService.findPrefernceByName(ESESystem.ENABLE_FARMER_CODE));

		if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {

			if (StringUtil.isEmpty(branchIdValue)) {
				columnHeaders = getLocaleProperty("exportFarmerListColumnHeaderBranchPratibha");
			} else if (getBranchId().equalsIgnoreCase("organic")) {
				columnHeaders = getLocaleProperty("OrganicFarmerListExportHeader");
			} else if (getBranchId().equalsIgnoreCase("bci")) {
				columnHeaders = getLocaleProperty("BCIFarmerListExportHeader");
			}

		} else if (getCurrentTenantId().equalsIgnoreCase("welspun")) {

			if (StringUtil.isEmpty(branchIdValue)) {
				columnHeaders = getLocaleProperty("exportFarmerListColumnHeaderBranchWelspun");
			} else if (getIsParentBranch() != null && getIsParentBranch().equalsIgnoreCase("1")) {
				columnHeaders = getLocaleProperty("ParentFarmerListExportHeader");
			} else
				columnHeaders = getLocaleProperty("FarmerListExportHeader");
		} else if (getCurrentTenantId().equalsIgnoreCase("ocp")) {
			if (StringUtil.isEmpty(branchIdValue)) {
				columnHeaders = getLocaleProperty("exportFarmerListColumnHeaderBranchWelspun");
			} else if (!getIsParentBranch().equalsIgnoreCase("0")) {
				columnHeaders = getLocaleProperty("ParentFarmerListExportHeader");
			} else
				columnHeaders = getLocaleProperty("FarmerListExportHeader");
		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				if (farmerCodeEnabled.equalsIgnoreCase("1"))
					columnHeaders = getLocaleProperty("exportFarmerListColumnHeaderBranchWithFarmerCode");
				else
					columnHeaders = getLocaleProperty("exportFarmerListColumnHeaderBranch");
			} else {
				if (farmerCodeEnabled.equalsIgnoreCase("1"))
					columnHeaders = getLocaleProperty("exportFarmerListColumnHeaderWithFarmerCode");
				else
					columnHeaders = getLocaleProperty("exportFarmerListColumnHeader");
			}
		}

		int mainGridIterator = 0;

		for (String cellHeader : columnHeaders.split("\\,")) {

			if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
			} else if (cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());
			}

			if (StringUtil.isEmpty(branchIdValue)) {
				cell = mainGridRowHead.createCell(mainGridIterator);
				cell.setCellValue(new HSSFRichTextString(cellHeader));
				style2.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
				style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cell.setCellStyle(style2);
				font2.setBoldweight((short) 12);
				font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				style2.setFont(font2);
				if (mainGridIterator != 7) {
					sheet.setColumnWidth(mainGridIterator, (15 * 550));
				}
				mainGridIterator++;
			} else {
				if (!cellHeader.equalsIgnoreCase(getText("app.branch"))) {
					cell = mainGridRowHead.createCell(mainGridIterator);
					cell.setCellValue(new HSSFRichTextString(cellHeader));
					style2.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
					style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					cell.setCellStyle(style2);
					font2.setBoldweight((short) 12);
					font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					style2.setFont(font2);
					sheet.setColumnWidth(mainGridIterator, (15 * 550));
					mainGridIterator++;
				}
			}
		}
		List<Object[]> dfata = (ArrayList) data1.get(ROWS);
		// if (!ObjectUtil.isEmpty(dfata)) {

		Long serialNumber = 0L;

		AtomicInteger snoCount = new AtomicInteger(0);
		for (Object[] obj : dfata) {
			row = sheet.createRow(rowNum++);
			colNum = 0;

			cell = row.createCell(colNum++);
			style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cell.setCellStyle(style3);
			cell.setCellValue(snoCount.incrementAndGet());

			if (!ObjectUtil.isEmpty(obj[11])) {
				if ((getIsMultiBranch().equalsIgnoreCase("1")
						&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
					if (StringUtil.isEmpty(branchIdValue)) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(
								!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(obj[11].toString())))
										? getBranchesMap().get(getParentBranchMap().get(obj[11].toString()))
										: getBranchesMap().get(obj[11].toString())));
					}
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(obj[11].toString())));
				} else {
					if (StringUtil.isEmpty(branchIdValue)) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(branchesMap.get(obj[11].toString())));
					}
				}
			} else {
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(""));
			}

			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.OCP)) {
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[29]) ? obj[29].toString() : ""));
			}

			if (farmerCodeEnabled.equalsIgnoreCase("1")) {
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[3]) ? obj[3].toString() : ""));
			}
			if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)
					&& !getCurrentTenantId().equalsIgnoreCase(ESESystem.OCP)) {
				cell = row.createCell(colNum++);
				if (!ObjectUtil.isEmpty(obj[20])) {
					HarvestSeason season = getHarvestSeason(String.valueOf(obj[20]));
					cell.setCellValue(new HSSFRichTextString(StringUtil.isEmpty(season) ? "" : season.getName()));// Season
				} else {
					cell.setCellValue(new HSSFRichTextString(""));// Season
				}
			}

			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.AVT)) {
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[31]) ? obj[31].toString() : ""));
			}

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[2]) ? obj[2].toString() : ""));

			if (!getCurrentTenantId().equalsIgnoreCase("gsma")) {
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[4]) ? obj[4].toString() : ""));// Gender
			}
			/*
			 * cell.setCellValue(new
			 * HSSFRichTextString(!ObjectUtil.isEmpty(obj[4])?obj[4].toString():
			 * ""));//Age
			 */
			if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)
					&& !getCurrentTenantId().equalsIgnoreCase("gsma")) {
				if (!ObjectUtil.isEmpty(obj[5])) {
					String value = CurrencyUtil.getDecimalFormat(Double.valueOf(obj[5].toString()), "##.000");
					cell = row.createCell(colNum++);
					cell.setCellValue(Double.parseDouble(value));
				} else {
					cell = row.createCell(colNum++);
					cell.setCellValue(0);
				}
			}

			if (getCurrentTenantId().equalsIgnoreCase("ocp")) {
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[8]) ? obj[8].toString() : ""));
			}

			// ics Name

			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)
					&& (StringUtil.isEmpty(getBranchId()) || getBranchId().equalsIgnoreCase("organic"))) {

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[17]) ? obj[17].toString() : ""));

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[19]) ? obj[19].toString() : ""));

			}
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)
					&& (StringUtil.isEmpty(getBranchId()) || !getBranchId().equalsIgnoreCase("bci"))) {
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[18]) ? obj[18].toString() : ""));// Farmer
																													// code
																													// tracenet
			}
			if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
				if (!ObjectUtil.isEmpty(obj[12])) {
					// FarmCatalogue catalogue =
					// catalogueService.findCatalogueByCode(obj[11].toString());
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(getCatlogueValueByCode(obj[12].toString()).getName()));
				} else {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(""));
				}
				if (!ObjectUtil.isEmpty(obj[16])) {
					// FarmCatalogue cat =
					// catalogueService.findCatalogueByCode(obj[15].toString());
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(getCatlogueValueByCode(obj[16].toString()).getName()));

				} else {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(""));
				}
			}
			
			
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)){
		
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[25]) ? obj[25].toString() : ""));

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[31]) ? obj[31].toString() : ""));
				List<String> agent = agentService.findAgentNameByWareHouseId(Long.valueOf(obj[32].toString()));
				if (!ObjectUtil.isListEmpty(agent)) {
					String json = new Gson().toJson(agent);
					String result = json.replaceAll("\"", "");
					cell = row.createCell(colNum++);
					cell.setCellValue(
							new HSSFRichTextString(ObjectUtil.isEmpty(result.substring(1, result.length()-1)) ? getText(" ") : result.substring(1, result.length()-1)));
					}
				else{
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString("NA"));
				}
			}
			
			
			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[7]) ? obj[7].toString() : ""));

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[13]) ? obj[13].toString() : ""));

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[14]) ? obj[14].toString() : ""));

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[15]) ? obj[15].toString() : ""));

			/*
			 * if (getCurrentTenantId().equalsIgnoreCase("wilmar")) {
			 * 
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(!ObjectUtil.isEmpty(obj[10]) ?
			 * getText("status" + obj[10]) : ""));
			 * 
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(!ObjectUtil.isEmpty(obj[27]) ?
			 * obj[27].toString() : "0.00"));
			 * 
			 * if (!ObjectUtil.isEmpty(obj[9]) && obj[9].equals(1)) { cell =
			 * row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(!ObjectUtil.isEmpty(obj[28]) ?
			 * obj[28].toString() : "")); } else { cell =
			 * row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString("Conventional")); } }
			 */
			if (!getCurrentTenantId().equalsIgnoreCase("efk") && !getCurrentTenantId().equalsIgnoreCase("gsma")
					&& !getCurrentTenantId().equalsIgnoreCase("crsdemo")
					&& !getCurrentTenantId().equalsIgnoreCase("ocp")
					&& !getCurrentTenantId().equalsIgnoreCase("symrise")
					&& !getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)
					&& !getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)
					&& !getCurrentTenantId().equalsIgnoreCase("iffco")
					&& !getCurrentTenantId().equalsIgnoreCase(ESESystem.AGRO_TENANT)
					&& !getCurrentTenantId().equalsIgnoreCase(ESESystem.AWI_TENANT_ID)
					&& !getCurrentTenantId().equalsIgnoreCase("kenyafpo")
					) {
				HSSFRow subGridRowHead = sheet.createRow(rowNum++);
				String subGridColumnHeaders = getLocaleProperty("exportFarmerListSubgridHeadings");

				int subGridIterator = 1;

				for (String cellHeader : subGridColumnHeaders.split("\\,")) {

					cell = subGridRowHead.createCell(subGridIterator);
					cell.setCellValue(new HSSFRichTextString(cellHeader));

					style3.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
					style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					cell.setCellStyle(style3);
					font2.setBoldweight((short) 12);
					font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					style2.setFont(font2);
					sheet.setColumnWidth(subGridIterator, (15 * 550));
					subGridIterator++;
				}

				FarmIcsConversion farmIcs = new FarmIcsConversion();
				Farm farmInfo = new Farm();
				Farmer farmer = new Farmer();
				Set<FarmIcsConversion> icsCon = new HashSet<FarmIcsConversion>();

				farmInfo.setFarmer(farmer);

				farmInfo.getFarmer().setOrganicStatus("-1");
				if (!StringUtil.isEmpty(organicStatus)) {

					if (!organicStatus.equalsIgnoreCase("conventional")) {
						farmIcs.setOrganicStatus(organicStatus);
						farmInfo.setFarmIcsConv(farmIcs);
						icsCon.add(farmInfo.getFarmIcsConv());
						if (organicStatus.equalsIgnoreCase("3")) {
							farmInfo.getFarmer().setOrganicStatus("3");
							farmInfo.getFarmer().setIsCertifiedFarmer(1);
						} else {
							farmInfo.getFarmer().setOrganicStatus("0");
							farmInfo.getFarmer().setIsCertifiedFarmer(1);
						}

					} else {
						farmInfo.getFarmer().setOrganicStatus("1");
						farmInfo.getFarmer().setIsCertifiedFarmer(0);
					}
				}

				farmInfo.getFarmer().setId(Long.valueOf(obj[1].toString()));
				super.filter = farmInfo;
				Map data = readData();

				List<Farm> subGridRows = (List<Farm>) data.get(ROWS);
				// List<Farm> farms =
				// farmerService.listFarmByFarmerId(Long.parseLong(obj[0].toString()));
				for (Farm frm : subGridRows) {

					row = sheet.createRow(rowNum++);
					colNum = 1;

					cell = row.createCell(colNum++);
					cell.setCellValue(
							new HSSFRichTextString(!StringUtil.isEmpty(frm.getFarmCode()) ? frm.getFarmCode() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(frm.getFarmName()));
					if (frm.getFarmer().getIsCertifiedFarmer() == 1) {
						FarmIcsConversion fIcs = farmerService
								.findFarmIcsConversionByFarmIdWithActive(Long.valueOf(frm.getId()));
						if (!ObjectUtil.isEmpty(fIcs) && fIcs != null && !StringUtil.isEmpty(fIcs.getOrganicStatus())) {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(fIcs.getOrganicStatus().equalsIgnoreCase("3")
									? getLocaleProperty("alrdyCertified") : getLocaleProperty("inprocess")));
						} else {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString("Conventional"));
						}
					} else {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString("Conventional"));
					}

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							(frm.getFarmICSConversion() != null && !ObjectUtil.isListEmpty(frm.getFarmICSConversion()))
									? (frm.getFarmICSConversion().iterator().next().getIcsType() != null && !StringUtil
											.isEmpty(frm.getFarmICSConversion().iterator().next().getIcsType()))
													? getText("icsStatus"
															+ frm.getFarmICSConversion().iterator().next().getIcsType())
													: ""
									: ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(frm.getFarmDetailedInfo().getTotalLandHolding()));
					if (!ObjectUtil.isEmpty(frm.getFarmCrops())) {
						String prods = frm.getFarmCrops() != null ? frm.getFarmCrops().stream()
								.map(e -> e.getProcurementVariety().getProcurementProduct().getName()).distinct()
								.reduce("", (a, b) -> a + "," + b) : null;
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(prods) ? prods.substring(1) : ""));
					} else {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(""));
					}

				}

			}
		}

		// }
		// else{
		/*
		 * if (!StringUtil.isEmpty(farmerCode)) {
		 * 
		 * filter1.getFarm().getFarmer().setFarmerCode(farmerCode.trim()); } if
		 * (!StringUtil.isEmpty(firstName)) {
		 * filter1.getFarm().getFarmer().setFirstName(firstName.trim()); }
		 * 
		 * if (!StringUtil.isEmpty(lastName)) {
		 * filter1.getFarm().getFarmer().setLastName(lastName.trim()); } if
		 * (!StringUtil.isEmpty(villageName)) {
		 * filter1.getFarm().getFarmer().setVillage(locationService.
		 * findVillageByCode(villageName.trim())); } if
		 * (!getCurrentTenantId().equalsIgnoreCase("pratibha")) { if
		 * (!StringUtil.isEmpty(stateName)) {
		 * 
		 * filter1.getFarm().getFarmer().setStateName(stateName); }
		 * 
		 * if (!StringUtil.isEmpty(fpo)) {
		 * filter1.getFarm().getFarmer().setFpo(fpo); }
		 * 
		 * if (!StringUtil.isEmpty(icsName)) {
		 * 
		 * filter1.getFarm().getFarmer().setIcsName(icsName); } }
		 * 
		 * if (!StringUtil.isEmpty(gender)) {
		 * filter1.getFarm().getFarmer().setGender(gender); } if
		 * (!StringUtil.isEmpty(filter1.getCropSeason().getCode())) {
		 * filter1.getCropSeason().setCode(filter1.getCropSeason().getCode()); }
		 * else { filter1.getCropSeason().setCode(getCurrentSeasonsCode()); } if
		 * (!getCurrentTenantId().equalsIgnoreCase("pratibha")) { if
		 * (!StringUtil.isEmpty(icsType)) {
		 * filter1.getFarm().getFarmer().setIcsType(icsType); } }
		 * 
		 * if (!StringUtil.isEmpty(branchIdParma)) { if
		 * (!getIsMultiBranch().equalsIgnoreCase("1")) { List<String> branchList
		 * = new ArrayList<>(); branchList.add(branchIdParma);
		 * filter1.getFarm().getFarmer().setBranchesList(branchList); } else {
		 * List<String> branchList = new ArrayList<>(); List<BranchMaster>
		 * branches = clientService.listChildBranchIds(branchIdParma);
		 * branchList.add(branchIdParma); branches.stream().filter(branch ->
		 * !StringUtil.isEmpty(branch)).forEach(branch -> {
		 * branchList.add(branch.getBranchId()); });
		 * filter1.getFarm().getFarmer().setBranchesList(branchList); } }
		 * 
		 * if (!StringUtil.isEmpty(subBranchIdParma) &&
		 * !subBranchIdParma.equals("0")) {
		 * filter1.getFarm().getFarmer().setBranchId(subBranchIdParma); }
		 * super.filter = this.filter1; Map data = readData("farmCropsList");
		 * 
		 * if (isMailExport()) { rowNum++; rowNum++; filterRowTitle =
		 * sheet.createRow(rowNum++); cell = filterRowTitle.createCell(1);
		 * cell.setCellValue(new
		 * HSSFRichTextString(getLocaleProperty("filter")));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * filterRow1 = sheet.createRow(rowNum++); if
		 * (!StringUtil.isEmpty(firstName)) { filterRow1 =
		 * sheet.createRow(rowNum++);
		 * 
		 * cell = filterRow1.createCell(1); cell.setCellValue(new
		 * HSSFRichTextString(getLocaleProperty("farmerName")));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * cell = filterRow1.createCell(2); cell.setCellValue(new
		 * HSSFRichTextString(filter1.getFarm().getFarmer().getFirstName()));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle); } if
		 * (!StringUtil.isEmpty(farmerCode)) {
		 * 
		 * filterRow3 = sheet.createRow(rowNum++); cell =
		 * filterRow3.createCell(1); cell.setCellValue(new
		 * HSSFRichTextString(getLocaleProperty("farmerCode")));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * cell = filterRow3.createCell(2); cell.setCellValue(new
		 * HSSFRichTextString( (!ObjectUtil.isEmpty(farmerCode) ?
		 * filter1.getFarmerCode() : getText("NA"))));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle); } if
		 * (!StringUtil.isEmpty(villageName)) {
		 * 
		 * filterRow4 = sheet.createRow(rowNum++); cell =
		 * filterRow4.createCell(1); cell.setCellValue(new
		 * HSSFRichTextString(getLocaleProperty("villageName")));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * cell = filterRow4.createCell(2); cell.setCellValue(new
		 * HSSFRichTextString((!ObjectUtil.isEmpty(villageName) ?
		 * locationService.findVillageByCode(villageName).getName() :
		 * getText("NA")))); filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * }
		 * 
		 * if (!StringUtil.isEmpty(stateName)) { filterRow5 =
		 * sheet.createRow(rowNum++); cell = filterRow5.createCell(1);
		 * cell.setCellValue(new
		 * HSSFRichTextString(getLocaleProperty("stateName")));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * cell = filterRow5.createCell(2); State s =
		 * locationService.findStateByCode(stateName); cell.setCellValue(new
		 * HSSFRichTextString((!ObjectUtil.isEmpty(s) ? s.getName() : "")));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * }
		 * 
		 * if (!StringUtil.isEmpty(icsName)) { filterRow6 =
		 * sheet.createRow(rowNum++); cell = filterRow6.createCell(1);
		 * cell.setCellValue(new
		 * HSSFRichTextString(getLocaleProperty("icsName")));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * cell = filterRow6.createCell(2); FarmCatalogue fc =
		 * farmerService.findfarmcatalogueByCode(icsName); cell.setCellValue(new
		 * HSSFRichTextString(!ObjectUtil.isEmpty(fc) ? fc.getName() : ""));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * }
		 * 
		 * if (!StringUtil.isEmpty(fpo)) { filterRow7 =
		 * sheet.createRow(rowNum++); cell = filterRow7.createCell(1);
		 * cell.setCellValue(new
		 * HSSFRichTextString(getLocaleProperty("cooperative")));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * cell = filterRow7.createCell(2); FarmCatalogue fc =
		 * farmerService.findfarmcatalogueByCode(fpo); cell.setCellValue(new
		 * HSSFRichTextString(!ObjectUtil.isEmpty(fc) ? fc.getName() : ""));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * }
		 * 
		 * if (!StringUtil.isEmpty(icsType)) {
		 * 
		 * filterRow3 = sheet.createRow(rowNum++); cell =
		 * filterRow3.createCell(1); cell.setCellValue(new
		 * HSSFRichTextString(getLocaleProperty("icsType")));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * cell = filterRow3.createCell(2); cell.setCellValue(new
		 * HSSFRichTextString( (!ObjectUtil.isEmpty(icsType) ?
		 * getText("icsStatus" + icsType) : getText("NA"))));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle); } if
		 * (!StringUtil.isEmpty(branchIdParma)) {
		 * 
		 * filterRow5 = sheet.createRow(rowNum++); cell =
		 * filterRow5.createCell(1); cell.setCellValue(new
		 * HSSFRichTextString(getLocaleProperty("BranchId")));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * cell = filterRow5.createCell(2); cell.setCellValue(new
		 * HSSFRichTextString((branchesMap.get(filter1.getBranchId()))));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * }
		 * 
		 * // filterRow6 = sheet.createRow(rowNum++); // filterRow7 =
		 * sheet.createRow(rowNum++); } ++rowNum;
		 * 
		 * HSSFRow mainGridRowHead = sheet.createRow(rowNum++); String
		 * columnHeaders = "";
		 * 
		 * setFarmerCodeEnabled(preferncesService.findPrefernceByName(ESESystem.
		 * ENABLE_FARMER_CODE)); if ((getIsMultiBranch().equalsIgnoreCase("1")
		 * && (getIsParentBranch().equals("1") ||
		 * StringUtil.isEmpty(branchIdValue)))) { if
		 * (StringUtil.isEmpty(branchIdValue)) { columnHeaders =
		 * getLocaleProperty("ExportFarmerListColumnHeadingBranch"); } else if
		 * (!StringUtil.isEmpty(branchIdValue)) { columnHeaders =
		 * getLocaleProperty("ExportFarmerListColumnHeading"); } } else { if
		 * (StringUtil.isEmpty(branchIdValue)) { columnHeaders =
		 * getLocaleProperty("exportFarmerListColumnHeaderBranch"); } else {
		 * columnHeaders = getLocaleProperty("exportFarmerListColumnHeader"); }
		 * }
		 * 
		 * String headerLabel=null;
		 * 
		 * 
		 * int mainGridIterator = 0; for (String cellHeader :
		 * columnHeaders.split("\\,")) {
		 * 
		 * if(cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
		 * headerLabel=cellHeader.trim().replace(ESESystem.
		 * CURRENCY_SYMPOL_EXPORT, getCurrencyType()); } else {
		 * headerLabel=cellHeader.trim(); }
		 * if(cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT)) {
		 * headerLabel=cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT,
		 * getAreaType()); } else { headerLabel=cellHeader.trim(); }
		 * 
		 * 
		 * 
		 * if (StringUtil.isEmpty(branchIdValue)) { cell =
		 * mainGridRowHead.createCell(mainGridIterator); cell.setCellValue(new
		 * HSSFRichTextString(headerLabel));
		 * style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		 * style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		 * style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		 * cell.setCellStyle(style2); font2.setBoldweight((short) 12);
		 * font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); style2.setFont(font2);
		 * if (mainGridIterator != 7) { sheet.setColumnWidth(mainGridIterator,
		 * (15 * 450)); } mainGridIterator++; } else { if
		 * (!cellHeader.equalsIgnoreCase(getText("app.branch"))) { cell =
		 * mainGridRowHead.createCell(mainGridIterator); cell.setCellValue(new
		 * HSSFRichTextString(headerLabel));
		 * style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		 * style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		 * style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		 * cell.setCellStyle(style2); font2.setBoldweight((short) 12);
		 * font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); style2.setFont(font2);
		 * sheet.setColumnWidth(mainGridIterator, (15 * 550));
		 * mainGridIterator++; } } }
		 * 
		 * 
		 * List<Object[]> dfata = (ArrayList) data.get(ROWS); // if
		 * (!ObjectUtil.isEmpty(dfata)) {
		 * 
		 * Long serialNumber = 0L;
		 * 
		 * 
		 * for (Object[] obj : dfata) {
		 * 
		 * row = sheet.createRow(rowNum++); colNum = 0;
		 * 
		 * serialNumber++; cell = row.createCell(colNum++);
		 * cell.setCellValue(new
		 * HSSFRichTextString(String.valueOf(serialNumber)!=null ?
		 * String.valueOf(serialNumber) : ""));
		 * 
		 * 
		 * 
		 * if (StringUtil.isEmpty(branchIdValue)) { cell =
		 * row.createCell(colNum++); cell.setCellValue(new HSSFRichTextString(
		 * ObjectUtil.isEmpty(obj[1]) ? getText("NA") :
		 * branchesMap.get(obj[1].toString()))); }
		 * 
		 * 
		 * if (farmerCodeEnabled.equalsIgnoreCase("1")) { cell =
		 * row.createCell(colNum++); cell.setCellValue( new
		 * HSSFRichTextString(ObjectUtil.isEmpty(obj[2]) ? getText("NA") :
		 * obj[2].toString()));
		 * 
		 * }
		 * 
		 * cell = row.createCell(colNum++); cell.setCellValue(new
		 * HSSFRichTextString(ObjectUtil.isEmpty(obj[3]) ? getText("NA") :
		 * obj[3].toString()));
		 * 
		 * 
		 * cell = row.createCell(colNum++); cell.setCellValue(new
		 * HSSFRichTextString( ObjectUtil.isEmpty(obj[4]) ? getText("NA") :
		 * obj[4].toString()));
		 * 
		 * 
		 * if
		 * (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)
		 * && getBranchId().equalsIgnoreCase("organic")) { // ics Name if
		 * (!StringUtil.isEmpty(obj[21])) { cell = row.createCell(colNum++);
		 * cell.setCellValue(new HSSFRichTextString(ObjectUtil.isEmpty(obj) ?
		 * getText("NA") : obj[21].toString())); } else { cell =
		 * row.createCell(colNum++); cell.setCellValue(new
		 * HSSFRichTextString(getText("NA"))); } // ics Code if
		 * (!StringUtil.isEmpty(obj[30])) { cell = row.createCell(colNum++);
		 * cell.setCellValue(new HSSFRichTextString(ObjectUtil.isEmpty(obj) ?
		 * getText("NA") : obj[30].toString())); } else { cell =
		 * row.createCell(colNum++); cell.setCellValue(new
		 * HSSFRichTextString(getText("NA"))); } }
		 * 
		 * cell = row.createCell(colNum++); cell.setCellValue(new
		 * HSSFRichTextString(ObjectUtil.isEmpty(obj[5]) ? getText("NA") :
		 * obj[5].toString()));
		 * 
		 * cell = row.createCell(colNum++); cell.setCellValue( new
		 * HSSFRichTextString(ObjectUtil.isEmpty(obj[6]) ? getText("NA") :
		 * obj[6].toString()));
		 * 
		 * if (getCurrentTenantId().equals("chetna")) { if
		 * (!ObjectUtil.isEmpty(obj[21])) { FarmCatalogue catalogue =
		 * catalogueService.findCatalogueByCode(obj[21].toString()); if
		 * (catalogue != null) { cell = row.createCell(colNum++);
		 * cell.setCellValue(new HSSFRichTextString(catalogue.getName())); }
		 * else { cell = row.createCell(colNum++); cell.setCellValue(new
		 * HSSFRichTextString(getText("NA"))); } } else { cell =
		 * row.createCell(colNum++); cell.setCellValue(new
		 * HSSFRichTextString(getText("NA"))); } cell =
		 * row.createCell(colNum++); cell.setCellValue( new
		 * HSSFRichTextString(ObjectUtil.isEmpty(obj[22]) ? getText("NA") :
		 * obj[22].toString()));
		 * 
		 * if (!ObjectUtil.isEmpty(obj[23])) { FarmCatalogue cat =
		 * catalogueService.findCatalogueByCode(obj[23].toString()); if (cat !=
		 * null) { cell = row.createCell(colNum++); cell.setCellValue(new
		 * HSSFRichTextString(cat.getName()));
		 * 
		 * } else { cell = row.createCell(colNum++); cell.setCellValue(new
		 * HSSFRichTextString(getText("NA")));
		 * 
		 * } } else { cell = row.createCell(colNum++); cell.setCellValue(new
		 * HSSFRichTextString(getText("NA"))); } cell =
		 * row.createCell(colNum++); cell.setCellValue( new
		 * HSSFRichTextString(ObjectUtil.isEmpty(obj[24]) ? getText("NA") :
		 * obj[24].toString())); cell = row.createCell(colNum++);
		 * cell.setCellValue( new HSSFRichTextString(ObjectUtil.isEmpty(obj[25])
		 * ? getText("NA") : obj[25].toString()));
		 * 
		 * } cell = row.createCell(colNum++); cell.setCellValue(new
		 * HSSFRichTextString(ObjectUtil.isEmpty(obj[7]) ? getText("NA") :
		 * obj[7].toString()));
		 * 
		 * 
		 * if(!getCurrentTenantId().equals("pratibha")){ cell =
		 * row.createCell(colNum++); cell.setCellValue(new HSSFRichTextString(
		 * ObjectUtil.isEmpty(obj[8]) ? getText("NA") :obj[8].toString())); cell
		 * = row.createCell(colNum++); cell.setCellValue(new HSSFRichTextString(
		 * ObjectUtil.isEmpty(obj[9]) ? getText("NA") :obj[9].toString())); cell
		 * = row.createCell(colNum++); cell.setCellValue(new HSSFRichTextString(
		 * ObjectUtil.isEmpty(obj[10]) ? getText("NA") :obj[10].toString())); }
		 * 
		 * if (!getCurrentTenantId().equalsIgnoreCase("atma")) { cell =
		 * row.createCell(colNum++); cell.setCellValue( new
		 * HSSFRichTextString(ObjectUtil.isEmpty(obj[12]) ? getText("NA") :
		 * obj[12].toString()));
		 * 
		 * 
		 * if (!getCurrentTenantId().equalsIgnoreCase("chetna") &&
		 * !getCurrentTenantId().equalsIgnoreCase("pratibha")) { cell =
		 * row.createCell(colNum++); cell.setCellValue( new
		 * HSSFRichTextString(ObjectUtil.isEmpty(obj[13]) ? getText("NA") :
		 * obj[13].toString()));
		 * 
		 * }
		 * 
		 * if
		 * (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID))
		 * { if (!getBranchId().equalsIgnoreCase("bci")) { cell =
		 * row.createCell(colNum++); cell.setCellValue(new HSSFRichTextString(
		 * ObjectUtil.isEmpty(obj[29]) ? getText("NA") : obj[29].toString())); }
		 * 
		 * }
		 * 
		 * cell = row.createCell(colNum++); cell.setCellValue( new
		 * HSSFRichTextString(ObjectUtil.isEmpty(obj[14]) ? getText("NA") :
		 * obj[14].toString())); cell = row.createCell(colNum++);
		 * cell.setCellValue( new HSSFRichTextString(ObjectUtil.isEmpty(obj[15])
		 * ? getText("NA") : obj[15].toString()));
		 * 
		 * if(!getCurrentTenantId().equalsIgnoreCase("nei")&&!
		 * getCurrentTenantId().equalsIgnoreCase("chetna")&&!
		 * getCurrentTenantId().equalsIgnoreCase("pratibha")){
		 * if(!ObjectUtil.isEmpty(obj[26])){ if(
		 * obj[26].toString().equals("0")){ cell = row.createCell(colNum++);
		 * cell.setCellValue(new HSSFRichTextString(getText("NA")));
		 * 
		 * }else{ cell = row.createCell(colNum++); cell.setCellValue(new
		 * HSSFRichTextString(getText("farmOwnedList"+obj[26].toString() )));
		 * 
		 * } }else{ cell = row.createCell(colNum++); cell.setCellValue(new
		 * HSSFRichTextString(getText("NA"))); } }
		 * 
		 * 
		 * if (!ObjectUtil.isEmpty(obj[16])) { // FarmCatalogue catalogue = //
		 * catalogueService.findCatalogueByCode(data[16].toString()); cell =
		 * row.createCell(colNum++); cell.setCellValue(new
		 * HSSFRichTextString(getText("icsStatus" +
		 * obj[16].toString().trim())));
		 * 
		 * } else { cell = row.createCell(colNum++); cell.setCellValue(new
		 * HSSFRichTextString(getText("NA"))); }
		 * 
		 * 
		 * // rows.add(data[18]);//Estimated yiedl
		 * 
		 * cell = row.createCell(colNum++); cell.setCellValue( new
		 * HSSFRichTextString(!ObjectUtil.isEmpty(obj[18]) ? getText("cs" +
		 * obj[18].toString()) : ""));
		 * 
		 * if (!ObjectUtil.isEmpty(obj[18])) { if
		 * (obj[18].toString().equals("0")) { cell = row.createCell(colNum++);
		 * cell.setCellValue(new HSSFRichTextString(obj[17].toString())); } else
		 * if (obj[18].toString().equals("1")) { cell =
		 * row.createCell(colNum++); cell.setCellValue(new
		 * HSSFRichTextString(obj[17].toString()));
		 * 
		 * } else if (obj[18].toString().equals("2")) { cell =
		 * row.createCell(colNum++); cell.setCellValue(new
		 * HSSFRichTextString(obj[17].toString())); } } else { cell =
		 * row.createCell(colNum++); cell.setCellValue( new
		 * HSSFRichTextString(!StringUtil.isEmpty(obj[17]) ? obj[17].toString()
		 * : "N/A")); }
		 * 
		 * if (!StringUtil.isEmpty(obj[27])) { cell = row.createCell(colNum++);
		 * cell.setCellValue( new HSSFRichTextString(StringUtil.isEmpty(obj[27])
		 * ? getText("") : obj[27].toString())); }
		 * 
		 * if (!StringUtil.isEmpty(obj[28])) { cell = row.createCell(colNum++);
		 * cell.setCellValue( new HSSFRichTextString(StringUtil.isEmpty(obj[28])
		 * ? getText("") : obj[28].toString())); }
		 * 
		 * 
		 * if(!StringUtil.isEmpty(getEnableMultiProduct()) &&
		 * getEnableMultiProduct().equalsIgnoreCase("0")){
		 * //if(!getCurrentTenantId().equals("chetna")&&
		 * !getCurrentTenantId().equals("indev")
		 * &&!getCurrentTenantId().equals("sagi")){
		 * 
		 * if(!ObjectUtil.isEmpty(obj[19])){ double estimatedYield =
		 * StringUtil.isDouble(obj[19].toString())?Double.valueOf(obj[19
		 * ].toString()):0D; cell = row.createCell(colNum++);
		 * cell.setCellValue(new
		 * HSSFRichTextString(String.valueOf(estimatedYield)));
		 * 
		 * if(estimatedYield>0D){ Double expCotYield = estimatedYield * 34;
		 * Double expTotCotYield = expCotYield / 100; cell =
		 * row.createCell(colNum++); cell.setCellValue(new
		 * HSSFRichTextString(String.valueOf(expTotCotYield))); }
		 * 
		 * }else{ cell=row.createCell(colNum++); cell.setCellValue(new
		 * HSSFRichTextString(getText("NA")));
		 * 
		 * }
		 * 
		 * //} }
		 * 
		 * 
		 * 
		 * if (!getCurrentTenantId().equals("indev")&&
		 * !getCurrentTenantId().equalsIgnoreCase(ESESystem.
		 * PRATIBHA_TENANT_ID)) { if (!StringUtil.isEmpty(obj[19])) {
		 * 
		 * double estimatedYield = StringUtil.isDouble(obj[19]) ?
		 * Double.valueOf(obj[19].toString()) : 0D; cell =
		 * row.createCell(colNum++); cell.setCellValue(new
		 * HSSFRichTextString(String.valueOf(estimatedYield)));
		 * 
		 * if (estimatedYield > 0D) { Double expCotYield = estimatedYield * 34;
		 * Double expTotCotYield = expCotYield / 100;
		 * 
		 * cell = row.createCell(colNum++); cell.setCellValue(new
		 * HSSFRichTextString(String.valueOf(expTotCotYield)));
		 * 
		 * }
		 * 
		 * } else { cell = row.createCell(colNum++); cell.setCellValue(new
		 * HSSFRichTextString(getText(" "))); } }
		 * 
		 * } }
		 */
		// }
		// }

		/*
		 * for (int i = 0; i <= colNum; i++) { sheet.autoSizeColumn(i); }
		 */

		int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		// picture.resize();
		String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
				: request.getSession().getId();
		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("farmerList") + fileNameDateFormat.format(new Date()) + ".xls";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		wb.write(fileOut);
		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		fileOut.close();

		return stream;
	}

	/*
	 * @Override public String populatePDF() throws Exception { branchIdValue =
	 * getBranchId(); // set value for branch id. buildBranchMap(); List<String>
	 * filters = new ArrayList<String>(); List<Object> fields = new
	 * ArrayList<Object>(); List<List<Object>> entityObject = new
	 * ArrayList<List<Object>>(); Map<String, String> searchRecord =
	 * getJQGridRequestParam(); Map data1;
	 * 
	 * filter = new Farmer();
	 * 
	 * if (!StringUtil.isEmpty(firstName)) { filter.setFirstName(firstName);
	 * filters.add((getLocaleProperty("farmerName") + " : " + firstName)); }
	 * 
	 * if (!StringUtil.isEmpty(filter.getFirstName())){
	 * filter.setFirstName(searchRecord.get("firstName").trim());
	 * filters.add(getLocaleProperty("farmer.firstName") + " : " +
	 * (!ObjectUtil.isEmpty(searchRecord) && !StringUtil.isEmpty(searchRecord) ?
	 * searchRecord.get("firstName") : "")); }
	 * 
	 * if (!StringUtil.isEmpty(villageName)) {
	 * filter.setVillage(locationService.findVillageByCode(villageName.trim()));
	 * filters.add( getLocaleProperty("villageName") + " : " +
	 * locationService.findVillageByCode(villageName.trim())); } if
	 * (!StringUtil.isEmpty(stateName)) { filter.setStateName(stateName);
	 * filters.add(getLocaleProperty("stateName") + " : " +
	 * locationService.findStateByCode(stateName.trim())); } if
	 * (!StringUtil.isEmpty(season)) { filter.setSeasonCode(season);
	 * filters.add(getLocaleProperty("season") + " : " +
	 * farmerService.findSeasonNameByCode(season.trim())); } if
	 * (!StringUtil.isEmpty(fpo)) { filter.setFpo(fpo); } if
	 * (!StringUtil.isEmpty(icsName)) { filter.setIcsName(icsName);
	 * filters.add(getLocaleProperty("icsName") + " : " +
	 * getCatlogueValueByCode(icsName)); } if (!StringUtil.isEmpty(gender)) {
	 * filter.setGender(gender); filters.add(getLocaleProperty("gender") + " : "
	 * + gender); } Farm farm = new Farm(); FarmIcsConversion farmics = new
	 * FarmIcsConversion(); Set<FarmIcsConversion> ics = new
	 * HashSet<FarmIcsConversion>(); if
	 * (getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)){
	 * filter.setOrganicStatus("-1"); if (!StringUtil.isEmpty(organicStatus)) {
	 * 
	 * if(!organicStatus.equalsIgnoreCase("conventional")){
	 * farmics.setOrganicStatus(organicStatus); farm.setFarmIcsConv(farmics);
	 * ics.add(farm.getFarmIcsConv()); if(organicStatus.equalsIgnoreCase("3")){
	 * filter.setOrganicStatus("3"); filter.setIsCertifiedFarmer(1); } else{
	 * filter.setOrganicStatus("0"); filter.setIsCertifiedFarmer(1); }
	 * 
	 * }else{ filter.setOrganicStatus("1"); filter.setIsCertifiedFarmer(0); } }
	 * } if (!StringUtil.isEmpty(branchIdParma)) { if
	 * (!getIsMultiBranch().equalsIgnoreCase("1")) { List<String> branchList =
	 * new ArrayList<>(); branchList.add(branchIdParma);
	 * filter.setBranchesList(branchList);
	 * filters.add(getLocaleProperty("BranchId") + " : " + branchIdParma); }
	 * else { List<String> branchList = new ArrayList<>(); List<BranchMaster>
	 * branches = clientService.listChildBranchIds(branchIdParma);
	 * branchList.add(branchIdParma); branches.stream().filter(branch ->
	 * !StringUtil.isEmpty(branch)).forEach(branch -> {
	 * branchList.add(branch.getBranchId()); });
	 * filter.setBranchesList(branchList);
	 * filters.add(getLocaleProperty("BranchId") + " : " + branchIdParma); } }
	 * super.filter = this.filter; data1 = readData("farmerProp");
	 * 
	 * List<Object[]> dfata = (ArrayList) data1.get(ROWS); if
	 * (!ObjectUtil.isEmpty(dfata)) { farmerCodeEnabled =
	 * preferncesService.findPrefernceByName(ESESystem.ENABLE_FARMER_CODE);
	 * AtomicInteger snoCount = new AtomicInteger(0); for (Object[] obj : dfata)
	 * {
	 * 
	 * fields = new ArrayList<Object>(); fields.add(snoCount.incrementAndGet());
	 * if (StringUtil.isEmpty(branchIdValue)) {
	 * fields.add(branchesMap.get(obj[11])); } if
	 * (!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
	 * if (!ObjectUtil.isEmpty(obj[20])) { HarvestSeason season =
	 * getHarvestSeason(String.valueOf(obj[20]));
	 * fields.add(StringUtil.isEmpty(season) ? "" : season.getName());// Season
	 * } else { fields.add(""); } }
	 * 
	 * fields.add(obj[2]);// Farmer Name fields.add(obj[4]);// Gender if
	 * (!getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)){
	 * fields.add(obj[5]); } if
	 * (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID) &&
	 * (StringUtil.isEmpty(getBranchId()) ||
	 * getBranchId().equalsIgnoreCase("organic"))) { if
	 * (!StringUtil.isEmpty(obj[17])) { fields.add(obj[17]); } else {
	 * fields.add(""); } if (!StringUtil.isEmpty(obj[19])) {
	 * fields.add(obj[19]); } else { fields.add(""); } } if
	 * (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID) &&
	 * (StringUtil.isEmpty(getBranchId()) ||
	 * !getBranchId().equalsIgnoreCase("bci"))) { fields.add(obj[18]); } if
	 * (getCurrentTenantId().equalsIgnoreCase("chetna")) { if
	 * (!StringUtil.isEmpty(obj[12])) { // FarmCatalogue catalogue = //
	 * catalogueService.findCatalogueByCode(obj[11].toString()); // if
	 * (catalogue != null) {
	 * fields.add(getCatlogueValueByCode(obj[12].toString()).getName());//
	 * icsname
	 * 
	 * } else { fields.add(""); }
	 * 
	 * } else { fields.add(""); } if (!StringUtil.isEmpty(obj[16])) { //
	 * FarmCatalogue cat = //
	 * catalogueService.findCatalogueByCode(obj[15].toString()); // if (cat !=
	 * null) {
	 * fields.add(getCatlogueValueByCode(obj[16].toString()).getName());//
	 * cooperative
	 * 
	 * } else { fields.add(""); }
	 * 
	 * } else { fields.add(""); } } fields.add(obj[7]); fields.add(obj[13]);
	 * fields.add(obj[14]); fields.add(obj[15]);
	 * 
	 * if (getCurrentTenantId().equalsIgnoreCase("wilmar")) {
	 * fields.add(!ObjectUtil.isEmpty(obj[26]) ? obj[26].toString() : "");
	 * fields.add(!ObjectUtil.isEmpty(obj[27]) ? obj[27].toString() : "0.00");
	 * fields.add(!ObjectUtil.isEmpty(obj[28]) ? obj[28].toString() : "");
	 * 
	 * }
	 * 
	 * entityObject.add(fields); } String sunGridcolumnHeaders =
	 * getLocaleProperty("exportSubColumnHeaderDistribution" + type); int
	 * subGridIterator = 1;
	 * 
	 * cellFont = new Font(FontFamily.HELVETICA, 10, Font.BOLD,
	 * GrayColor.GRAYBLACK);
	 * 
	 * setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.
	 * ENABLE_BATCH_NO)); if (!StringUtil.isEmpty(enableBatchNo) &&
	 * !enableBatchNo.equals("1")) { sunGridcolumnHeaders =
	 * sunGridcolumnHeaders.replace(",Batch No", ""); }
	 * 
	 * subGridColWidth = sunGridcolumnHeaders.split("\\,").length;
	 * 
	 * cell = new PdfPCell(new Phrase("", cellFont)); table.addCell(cell);
	 * 
	 * for (String cellHeader : sunGridcolumnHeaders.split("\\,")) { if
	 * (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
	 * cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT,
	 * getCurrencyType()); } else { cellHeader = cellHeader.trim(); } cell = new
	 * PdfPCell(new Phrase(cellHeader, cellFont));
	 * cell.setBackgroundColor(GrayColor.LIGHT_GRAY);
	 * cell.setHorizontalAlignment(Element.ALIGN_CENTER); cell.setNoWrap(false);
	 * // To set wrapping of text in cell. // cell.setColspan(3); // To add
	 * column span. table.addCell(cell); }
	 * 
	 * 
	 * if (StringUtil.isEmpty(branchIdValue)) { cols = 7; } else { cols = 6; }
	 * 
	 * 
	 * System.out.println(table.getNumberOfColumns());
	 * 
	 * for (int i = (subGridColWidth + 1); i < table.getNumberOfColumns(); i++)
	 * { cell = new PdfPCell(new Phrase("", cellFont)); table.addCell(cell); }
	 * 
	 * cellFont = new Font(FontFamily.HELVETICA, 8, Font.NORMAL,
	 * GrayColor.GRAYBLACK);
	 * 
	 * InputStream is = getPDFExportStream(entityObject, filters);
	 * setPdfFileName(getText("FLListFile") + fileNameDateFormat.format(new
	 * Date())); Map<String, InputStream> fileMap = new HashMap<String,
	 * InputStream>(); fileMap.put(getPdfFileName(), is);
	 * setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText(
	 * "FLListFile"), fileMap, ".pdf")); return "pdf"; } // }
	 * 
	 * return LIST; }
	 */
	public String getEnableMultiProduct() {

		return preferncesService.findPrefernceByName(ESESystem.ENABLE_MULTI_PRODUCT);
	}

	@Override
	public InputStream getExportDataStream(String exportType) throws IOException {

		// TODO Auto-generated method stub
		return null;
	}

	public String getExportLimit() {

		return exportLimit;
	}

	public void setExportLimit(String exportLimit) {

		this.exportLimit = exportLimit;
	}

	public String getFarmerCodeEnabled() {

		return farmerCodeEnabled;
	}

	public void setFarmerCodeEnabled(String farmerCodeEnabled) {

		this.farmerCodeEnabled = farmerCodeEnabled;
	}

	public String getFarmerName() {

		return farmerName;
	}

	public void setFarmerName(String farmerName) {

		this.farmerName = farmerName;
	}

	public String getFatherName() {

		return fatherName;
	}

	public void setFatherName(String fatherName) {

		this.fatherName = fatherName;
	}

	public Map<String, String> getFields() {
		fields.put("1", getLocaleProperty("farmerName"));
		fields.put("2", getLocaleProperty("FLlastName"));
		fields.put("3", getLocaleProperty("village"));

		if (!StringUtil.isEmpty(farmerCodeEnabled) && farmerCodeEnabled.equalsIgnoreCase("1")) {
			fields.put("5", getText("farmerCode"));
		}

		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			fields.put("6", getText("app.branch"));
		} else if (StringUtil.isEmpty(getBranchId())) {
			fields.put("4", getText("app.branch"));
		}
		if (!getCurrentTenantId().equalsIgnoreCase("pratibha")) {
			fields.put("7", getLocaleProperty("state.name"));
		}

		if (!getCurrentTenantId().equalsIgnoreCase("pratibha")) {
			fields.put("8", getText("cooperative"));

			fields.put("9", getText("icsName"));
		}

		fields.put("10", getText("gender"));
		if (!getCurrentTenantId().equalsIgnoreCase("pratibha")) {
			fields.put("11", getText("icsType"));
		}

		return fields;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getIcsName() {
		return icsName;
	}

	public void setIcsName(String icsName) {
		this.icsName = icsName;
	}

	public String getFpo() {
		return fpo;
	}

	public void setFpo(String fpo) {
		this.fpo = fpo;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Map<String, String> getGenderList() {
		return genderList;
	}

	public void setGenderList(Map<String, String> genderList) {
		this.genderList = genderList;
	}

	public Map<Integer, String> getIcsTypeList() {
		return icsTypeList;
	}

	public void setIcsTypeList(Map<Integer, String> icsTypeList) {
		this.icsTypeList = icsTypeList;
	}

	public String getIcsType() {
		return icsType;
	}

	public void setIcsType(String icsType) {
		this.icsType = icsType;
	}

	public FarmCrops getFilter1() {
		return filter1;
	}

	public LinkedList<String> getFarmerData(Farmer f) {

		LinkedList<String> fmrList = new LinkedList<>();
		fmrList.add(getHarvestSeason(f.getSeasonCode()).getName());
		fmrList.add(f.getFarmerId());
		fmrList.add(f.getFarmerCodeAndName());
		fmrList.add(f.getLastName());
		fmrList.add(f.getGender() != null ? f.getGender().equals("MALE") ? "1" : "2" : "NA");
		fmrList.add(!ObjectUtil.isEmpty(f.getAge()) ? "     " + f.getAge() + "     " : "");
		fmrList.add(!StringUtil.isEmpty(f.getFarmerCode()) ? f.getFarmerCode() : "");
		// fmrList.add(!StringUtil.isEmpty(f.getIcsName()) ?
		// f.getIcsName() : "");
		// fmrList.add(!StringUtil.isEmpty(f.getIcsCode()) ?
		// f.getIcsCode() : "");
		fmrList.add(!StringUtil.isEmpty(f.getYearOfICS()) ? f.getYearOfICS() : "");
		fmrList.add(!StringUtil.isEmpty(f.getMobileNumber()) ? f.getMobileNumber() : "");
		fmrList.add(!ObjectUtil.isEmpty(f.getVillage()) && !StringUtil.isEmpty(f.getVillage().getName())
				? f.getVillage().getName() : "");
		fmrList.add(!ObjectUtil.isEmpty(f.getCity()) && !StringUtil.isEmpty(f.getCity().getName())
				? f.getCity().getName() : "");
		fmrList.add(!ObjectUtil.isEmpty(f.getCity()) && !ObjectUtil.isEmpty(f.getCity().getLocality())
				&& !StringUtil.isEmpty(f.getCity().getLocality().getName()) ? f.getCity().getLocality().getName() : "");
		fmrList.add(!ObjectUtil.isEmpty(f.getCity()) && !ObjectUtil.isEmpty(f.getCity().getLocality())
				&& !ObjectUtil.isEmpty(f.getCity().getLocality().getState())
						? String.valueOf(f.getCity().getLocality().getState().getName()) : "");
		// fmrList.add("");
		fmrList.add(!StringUtil.isEmpty(f.getAdultCountMale()) ? f.getAdultCountMale() : "");
		fmrList.add(!StringUtil.isEmpty(f.getAdultCountFemale()) ? f.getAdultCountFemale() : "");
		fmrList.add(!StringUtil.isEmpty(f.getChildCountMale()) ? f.getChildCountMale() : "");
		fmrList.add(!StringUtil.isEmpty(f.getChildCountFemale()) ? f.getChildCountFemale() : "");
		fmrList.add(!ObjectUtil.isEmpty(f.getNoOfSchoolChildMale()) ? String.valueOf(f.getNoOfSchoolChildMale()) : "");
		fmrList.add(
				!ObjectUtil.isEmpty(f.getNoOfSchoolChildFemale()) ? String.valueOf(f.getNoOfSchoolChildFemale()) : "");
		fmrList.add(!ObjectUtil.isEmpty(f.getNoOfHouseHoldMem())
				? "           " + String.valueOf(f.getNoOfHouseHoldMem()) + "           " : "");
		// fmrList.add("");
		Farm farm = !ObjectUtil.isListEmpty(f.getFarms()) ? f.getFarms().iterator().next() : null;
		if (f != null) {
			fmrList.add(!ObjectUtil.isEmpty(farm.getFarmDetailedInfo())
					? !StringUtil.isEmpty(farm.getFarmDetailedInfo().getTotalLandHolding())
							? farm.getFarmDetailedInfo().getTotalLandHolding() : ""
					: "");
			fmrList.add(!StringUtil.isEmpty(farm.getOwnLand()) ? farm.getOwnLand() : "");
			fmrList.add(!StringUtil.isEmpty(farm.getLeasedLand()) ? farm.getLeasedLand() : "");
			fmrList.add(!StringUtil.isEmpty(farm.getIrrigationLand()) ? farm.getIrrigationLand() : "");

			fmrList.add(!ObjectUtil.isEmpty(farm.getFarmDetailedInfo())
					? !StringUtil.isEmpty(farm.getFarmDetailedInfo().getFarmIrrigation())
							? farm.getFarmDetailedInfo().getFarmIrrigation() : ""
					: "");
			if (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo())
					&& !StringUtil.isEmpty(farm.getFarmDetailedInfo().getIrrigationSource())) {
				FarmCatalogue catalog = getCatlogueValueByCode(farm.getFarmDetailedInfo().getIrrigationSource());
				fmrList.add(!ObjectUtil.isEmpty(catalog) && !StringUtil.isEmpty(catalog.getDispName())
						? catalog.getDispName() : "");
			} else {
				fmrList.add("");
			}

			fmrList.add(!ObjectUtil.isEmpty(farm.getFarmDetailedInfo())
					? !StringUtil.isEmpty(farm.getFarmDetailedInfo().getIrrigatedOther())
							? farm.getFarmDetailedInfo().getIrrigatedOther() : ""
					: "");
		} else {
			fmrList.add("");
			fmrList.add("");
			fmrList.add("");
			fmrList.add("");
			fmrList.add("");
			fmrList.add("");
			fmrList.add("");
		}
		// fmrList.add("");
		Map<String, String> cat3 = getFarmCatalougeMap(Integer.valueOf(getText("animaHusbanday")));
		if (!f.getAnimalHusbandary().isEmpty()) {

			for (Entry<String, String> fm : cat3.entrySet()) {
				int count = 0;
				for (AnimalHusbandary sn : f.getAnimalHusbandary()) {
					if (sn.getFarmAnimal().getCode().equals(fm.getKey())) {
						fmrList.add(!StringUtil.isEmpty(sn.getAnimalCount()) && !sn.getAnimalCount().equals("-1")
								? sn.getAnimalCount() + "           " : "");
						count = 1;
						break;
					}

				}
				if (count == 0) {
					fmrList.add("                      ");

				}

			}

		} else {
			for (int k = 1; k <= cat3.size(); k++)
				fmrList.add("                      ");

		}
		// fmrList.add("");
		String agriImples = null;
		if (!StringUtil.isEmpty(f.getAgricultureImplements())) {
			for (String catCode : f.getAgricultureImplements().split(",")) {
				// getCatlogueValueByCode(catCode);
				if (!StringUtil.isEmpty(agriImples))
					agriImples += "," + getCatlogueValueByCode(catCode) != null
							? getCatlogueValueByCode(catCode).getDispName() : "";
				else
					agriImples = getCatlogueValueByCode(catCode) != null ? getCatlogueValueByCode(catCode).getDispName()
							: "";

				/*
				 * FarmCatalogue val =
				 * catalogueService.findCatalogueByCode(catCode.trim()); if
				 * (!StringUtil.isEmpty(val)) { if
				 * (!StringUtil.isEmpty(agriImples)) agriImples += "," +
				 * val.getDispName(); else agriImples = val.getDispName(); }
				 */
			}

		}
		String consElec = null;
		if (!StringUtil.isEmpty(f.getConsumerElectronics())) {
			for (String catCode : f.getConsumerElectronics().split(",")) {
				// getCatlogueValueByCode(catCode);
				if (!StringUtil.isEmpty(consElec))
					consElec += "," + getCatlogueValueByCode(catCode) != null
							? getCatlogueValueByCode(catCode).getDispName() : "";
				else
					consElec = getCatlogueValueByCode(catCode) != null ? getCatlogueValueByCode(catCode).getDispName()
							: "";
				/*
				 * FarmCatalogue val =
				 * catalogueService.findCatalogueByCode(catCode.trim()); if
				 * (!StringUtil.isEmpty(val)) { if
				 * (!StringUtil.isEmpty(consElec)) consElec += "," +
				 * val.getDispName(); else consElec = val.getDispName(); }
				 */
			}
		}
		String vehicle = null;
		if (!StringUtil.isEmpty(f.getVehicle())) {
			for (String catCode : f.getVehicle().split(",")) {
				// getCatlogueValueByCode(catCode);
				if (!StringUtil.isEmpty(vehicle))
					vehicle += "," + getCatlogueValueByCode(catCode) != null
							? getCatlogueValueByCode(catCode).getDispName() : "";
				else
					vehicle = getCatlogueValueByCode(catCode) != null ? getCatlogueValueByCode(catCode).getDispName()
							: "";
				/*
				 * FarmCatalogue val =
				 * catalogueService.findCatalogueByCode(catCode.trim()); if
				 * (!StringUtil.isEmpty(val)) { if
				 * (!StringUtil.isEmpty(vehicle)) vehicle += "," +
				 * val.getDispName(); else vehicle = val.getDispName(); }
				 */
			}
		}
		fmrList.add(agriImples);
		fmrList.add(consElec);
		fmrList.add(vehicle);
		// fmrList.add("");
		fmrList.add(getCatlogueValueByCode(f.getEducation()) != null
				? getCatlogueValueByCode(f.getEducation()).getDispName() : "");
		fmrList.add(!ObjectUtil.isEmpty(f.getFarmerEconomy()) && f.getFarmerEconomy().getHousingOwnership() != -1
				? String.valueOf(f.getFarmerEconomy().getHousingOwnership()) : "");
		fmrList.add(!ObjectUtil.isEmpty(f.getFarmerEconomy()) && f.getFarmerEconomy().getElectrifiedHouse() != -1
				? String.valueOf(f.getFarmerEconomy().getElectrifiedHouse()) : "");
		/*
		 * fmrList.add(!ObjectUtil.isEmpty(f.getFarmerEconomy()) &&
		 * !f.getFarmerEconomy().getDrinkingWaterSource().
		 * equalsIgnoreCase("null") &&
		 * !f.getFarmerEconomy().getDrinkingWaterSource().equals("-1") ?
		 * f.getFarmerEconomy().getDrinkingWaterSource() : "");
		 */

		fmrList.add(!ObjectUtil.isEmpty(f.getFarmerEconomy()) && f.getFarmerEconomy().getDrinkingWaterSource() != null
				&& !f.getFarmerEconomy().getDrinkingWaterSource().equals("-1")
						? f.getFarmerEconomy().getDrinkingWaterSource() : "");

		fmrList.add(
				!ObjectUtil.isEmpty(f.getFarmerEconomy()) ? f.getFarmerEconomy().getDrinkingWaterSourceOther() : "");

		fmrList.add(!StringUtil.isEmpty(f.getLifeInsurance()) && !f.getLifeInsurance().equals("-1")
				? f.getLifeInsurance() : "");
		fmrList.add(!StringUtil.isEmpty(f.getIsCropInsured()) && !f.getIsCropInsured().equals("-1")
				? f.getIsCropInsured() : "");

		fmrList.add(!StringUtil.isEmpty(f.getHealthInsurance()) && !f.getHealthInsurance().equals("-1")
				? f.getHealthInsurance() : "");
		fmrList.add(!StringUtil.isEmpty(f.getLoanTakenFrom()) && !f.getLoanTakenFrom().equals("-1")
				? f.getLoanTakenFrom() : "");
		return fmrList;

	}

	public void setFilter1(FarmCrops filter1) {
		this.filter1 = filter1;
	}

	public Map<String, String> getSeasonList() {
		List<HarvestSeason> seasons = farmerService.listHarvestSeasons();
		for (HarvestSeason season : seasons) {
			seasonList.put(season.getCode(), season.getName());
		}
		return seasonList;
	}

	public Map<String, String> getFarmSizeList() {
		Map<String, String> farmMap = new LinkedHashMap<String, String>();
		farmMap.put("", getText("all"));
		farmMap.put("1", getText("lessthanone"));
		farmMap.put("2", getText("greaterthanone"));
		return farmMap;
	}

	public void setSeasonList(Map<String, String> seasonList) {
		this.seasonList = seasonList;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	@Override
	public String populatePDF() throws Exception {
		InputStream is = getPDFExportDataStream();
		setPdfFileName(getText("FLListFile") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(getPdfFileName(), is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("FLListFile"), fileMap, ".pdf"));
		return "pdf";

	}

	public InputStream getPDFExportDataStream()
			throws IOException, DocumentException, NoSuchFieldException, SecurityException {
		Map<String, String> searchRecord = getJQGridRequestParam();
		Font cellFont = null; // font for cells.
		Font titleFont = null; // font for title text.
		Paragraph title = null; // to add title for report
		String columnHeaders = null; // to hold column headers from property
										// file.

		int mainGridColWidth = 0;
		int subGridColWidth = 0;

		List<Object[]> entityFieldsList = new ArrayList<Object[]>(); // to hold
																		// properties
																		// of
																		// entity
																		// object
																		// passed
																		// as
																		// list.
		SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Document document = new Document();
		setMailExport(true);
		int cols;

		String makeDir = FileUtil.storeXls(request.getSession().getId());
		String fileName = getText("FLListFile").replace(" ", "_") + fileNameDateFormat.format(new Date()) + ".pdf";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		PdfWriter.getInstance(document, fileOut);

		document.open();

		PdfPTable table = null;
		PdfPCell cell = null;
		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap();

		com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance(exportLogo());
		logo.scaleAbsolute(100, 50);
		; // resizing logo image size.

		document.add(logo); // Adding logo in PDF file.

		// below of code for title text
		titleFont = new Font(FontFamily.HELVETICA, 14, Font.BOLD, GrayColor.GRAYBLACK);
		title = new Paragraph(new Phrase(getLocaleProperty("FLListFile"), titleFont));
		title.setAlignment(Element.ALIGN_CENTER);
		document.add(title);
		document.add(
				new Paragraph(new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK)))); // Add
																														// a
																														// blank
																														// line
																														// after
																														// title.

		// cell for table.

		/* Beginning */
		// setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(IExporter.EXPORT_MANUAL)
		// ? true : false);
		setsDate(IExporter.EXPORT_DAILY.equalsIgnoreCase(IExporter.EXPORT_MANUAL)
				? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));
		boolean flag = true;
		// DecimalFormat df = new DecimalFormat("0.000");
		DateFormat filterDateFormat = new SimpleDateFormat(getGeneralDateFormat());
		Map data1;
		Farm farm = new Farm();
		FarmIcsConversion farmics = new FarmIcsConversion();
		Set<FarmIcsConversion> ics = new HashSet<FarmIcsConversion>();
		filter = new Farmer();
		if (!StringUtil.isEmpty(searchRecord.get("firstName")))
			filter.setFirstName(searchRecord.get("firstName").trim());

		if (!StringUtil.isEmpty(searchRecord.get("farmerCode")))
			filter.setFarmerCode(searchRecord.get("farmerCode").trim());

		if (!StringUtil.isEmpty(villageName)) {
			filter.setVillage(locationService.findVillageByCode(villageName.trim()));
		}
		if (!StringUtil.isEmpty(stateName)) {
			filter.setStateName(stateName);
		}
		if (!StringUtil.isEmpty(fpo)) {
			filter.setFpo(fpo);
		}
		if (!StringUtil.isEmpty(icsName)) {
			filter.setIcsName(icsName);
		}
		if (!StringUtil.isEmpty(season)) {
			filter.setSeasonCode(season);
		}
		if (!StringUtil.isEmpty(createdUserName)) {
			filter.setCreatedUsername(createdUserName);
		}
		if (!StringUtil.isEmpty(gender)) {
			filter.setGender(gender);
		}
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)) {
			filter.setOrganicStatus("-1");
			if (!StringUtil.isEmpty(organicStatus)) {

				if (!organicStatus.equalsIgnoreCase("conventional")) {
					farmics.setOrganicStatus(organicStatus);
					farm.setFarmIcsConv(farmics);
					ics.add(farm.getFarmIcsConv());
					if (organicStatus.equalsIgnoreCase("3")) {
						filter.setOrganicStatus("3");
						filter.setIsCertifiedFarmer(1);
					} else {
						filter.setOrganicStatus("0");
						filter.setIsCertifiedFarmer(1);
					}

				} else {
					filter.setOrganicStatus("1");
					filter.setIsCertifiedFarmer(0);
				}
			}
		}
		if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
			if (!getIsMultiBranch().equalsIgnoreCase("1")) {
				List<String> branchList = new ArrayList<>();
				branchList.add(searchRecord.get("branchId").trim());
				filter.setBranchesList(branchList);
			} else {
				List<String> branchList = new ArrayList<>();
				List<BranchMaster> branches = clientService.listChildBranchIds(searchRecord.get("branchId").trim());
				branchList.add(searchRecord.get("branchId").trim());
				branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
					branchList.add(branch.getBranchId());
				});
				filter.setBranchesList(branchList);
			}
		}

		if (!StringUtil.isEmpty(searchRecord.get("subBranchId"))) {
			filter.setBranchId(searchRecord.get("subBranchId").trim());
		}

		super.filter = this.filter;
		data1 = readData("farmerProp");
		List<Object[]> dfata = (ArrayList) data1.get(ROWS);
		if (isMailExport()) {
			/*document.add(new Paragraph(new Phrase(getLocaleProperty("filter"),
					new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));*/
			
			if (!StringUtil.isEmpty(branchIdParma)) {
				if (!getIsMultiBranch().equalsIgnoreCase("1")) {
					List<String> branchList = new ArrayList<>();
					branchList.add(branchIdParma);
					filter.setBranchesList(branchList);
					document.add(new Paragraph(new Phrase(getLocaleProperty("app.branch") + " : " + branchIdParma,
							new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));
				} else {
					List<String> branchList = new ArrayList<>();
					List<BranchMaster> branches = clientService.listChildBranchIds(branchIdParma);
					branchList.add(branchIdParma);
					branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
						branchList.add(branch.getBranchId());
					});
					filter.setBranchesList(branchList);
					document.add(new Paragraph(new Phrase(getLocaleProperty("BranchId") + " : " + branchIdParma,
							new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));

				}
			}
			
			if (!StringUtil.isEmpty(filter.getFirstName())) {
				document.add(new Paragraph(
						new Phrase(getLocaleProperty("firstName") + " : " + searchRecord.get("firstName").trim())));
			}

			if (!StringUtil.isEmpty(filter.getFarmerCode())) {
				document.add(new Paragraph(
						new Phrase(getLocaleProperty("farmerCode") + " : " + searchRecord.get("farmerCode").trim())));
			}

			if (!StringUtil.isEmpty(villageName)) {
				document.add(new Paragraph(new Phrase(
						getLocaleProperty("villageName") + " : "
								+ (!ObjectUtil.isEmpty(villageName)
										? locationService.findVillageByCode(villageName).getName() : getText("NA")),
						new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
			}

			if (!StringUtil.isEmpty(stateName)) {
				State s = locationService.findStateByCode(stateName);
				document.add(new Paragraph(
						new Phrase(getLocaleProperty("stateName") + " : " + (!ObjectUtil.isEmpty(s) ? s.getName() : ""),
								new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
			}

			if (!StringUtil.isEmpty(organicStatus)) {
				if (!filter.getOrganicStatus().equalsIgnoreCase("1")) {
					document.add(new Paragraph(new Phrase(
							getLocaleProperty("organicStatus") + " : "
									+ (filter.getOrganicStatus().equalsIgnoreCase("3")
											? getLocaleProperty("alrdyCertified") : getLocaleProperty("inprocess")),
							new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
				} else {
					document.add(new Paragraph(
							new Phrase(getLocaleProperty("organicStatus") + " : " + (getLocaleProperty("Conventional")),
									new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
				}
			}

			if (!StringUtil.isEmpty(season)) {
				HarvestSeason seasons = farmerService.findSeasonNameByCode(season);
				document.add(new Paragraph(new Phrase(
						getLocaleProperty("cSeasonCode") + " : "
								+ (!ObjectUtil.isEmpty(seasons) ? seasons.getName() : ""),
						new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
			}

			if (!StringUtil.isEmpty(filter.getGender())) {
				document.add(new Paragraph(new Phrase(
						getLocaleProperty("gender") + " : " + (!ObjectUtil.isEmpty(gender) ? gender : getText("NA")))));
			}

			if (!StringUtil.isEmpty(filter.getCreatedUsername())) {
				document.add(new Paragraph(new Phrase(
						getLocaleProperty("Created User") + " : " + (!ObjectUtil.isEmpty(filter.getCreatedUsername())
								? filter.getCreatedUsername() : getText("NA")))));
			}
			
			  /*document.add(new Paragraph( new
			  Phrase(getLocaleProperty("StartingDate") + " : " +
			  filterDateFormat.format(getsDate()), new
			  Font(FontFamily.HELVETICA, 10, Font.BOLD,
			  GrayColor.GRAYBLACK)))); document.add(new Paragraph( new
			  Phrase(getLocaleProperty("EndingDate") + " : " +
			  filterDateFormat.format(geteDate()), new
			  Font(FontFamily.HELVETICA, 10, Font.BOLD,
			  GrayColor.GRAYBLACK))));*/
			 
			/*
			 * document.add(new Paragraph( new Phrase(" ", new
			 * Font(FontFamily.HELVETICA, 10, Font.NORMAL,
			 * GrayColor.GRAYBLACK))));
			 */ // Add
				// a
				// blank
				// line.
		}

		ESESystem preferences = preferncesService.findPrefernceById("1");
		setFarmerCodeEnabled(preferncesService.findPrefernceByName(ESESystem.ENABLE_FARMER_CODE));
		String mainGridColumnHeaders = "";
		if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {

			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("exportFarmerListColumnHeaderBranchPratibha");
			} else if (getBranchId().equalsIgnoreCase("organic")) {
				mainGridColumnHeaders = getLocaleProperty("OrganicFarmerListExportHeader");
			} else if (getBranchId().equalsIgnoreCase("bci")) {
				mainGridColumnHeaders = getLocaleProperty("BCIFarmerListExportHeader");
			}

		} else if (getCurrentTenantId().equalsIgnoreCase("welspun") || getCurrentTenantId().equalsIgnoreCase("ocp")) {

			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("exportFarmerListColumnHeaderBranchWelspun");
			} else if (getIsParentBranch() != null && getIsParentBranch().equalsIgnoreCase("1")) {
				mainGridColumnHeaders = getLocaleProperty("ParentFarmerListExportHeader");
			} else
				mainGridColumnHeaders = getLocaleProperty("FarmerListExportHeader");
		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("exportFarmerListColumnHeaderBranch");
			} else {
				mainGridColumnHeaders = getLocaleProperty("exportFarmerListColumnHeader");
			}
		}

		int mainGridIterator = 0;

		table = new PdfPTable(mainGridColumnHeaders.split("\\,").length); // Code
																			// for
																			// setting
																			// table
																			// column
																			// Numbers.
		cellFont = new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK); // setting
																						// font
																						// for
																						// header
																						// cells
																						// of
																						// table.

		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {
			if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
			} else {
				cellHeader = cellHeader.trim();
			}
			if (StringUtil.isEmpty(branchIdValue)) { // Add branch header to
														// export sheet if main
														// branch logged in.
				cell = new PdfPCell(new Phrase(cellHeader, cellFont));
				cell.setBackgroundColor(new BaseColor(204,255,204));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setNoWrap(false); // To set wrapping of text in cell.
				// cell.setColspan(3); // To add column span.
				table.addCell(cell);
			} else {
				if (!cellHeader.equalsIgnoreCase(getText("app.branch"))) {
					cell = new PdfPCell(new Phrase(cellHeader, cellFont));
					cell.setBackgroundColor(new BaseColor(204,255,204));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setNoWrap(false); // To set wrapping of text in cell.
					// cell.setColspan(3); // To add column span.
					table.addCell(cell);
				}
			}

		}

		cellFont = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK); // setting
																						// font
																						// for
																						// cells.

		// if (!ObjectUtil.isEmpty(dfata)) {

		Long serialNumber = 0L;

		AtomicInteger snoCount = new AtomicInteger(0);
		for (Object[] obj : dfata) {

			serialNumber++;

			cell = new PdfPCell(new Paragraph(new Phrase(String.valueOf(serialNumber), cellFont)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			if (!ObjectUtil.isEmpty(obj[11])) {
				if ((getIsMultiBranch().equalsIgnoreCase("1")
						&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

					if (StringUtil.isEmpty(branchIdValue)) {
						cell = new PdfPCell(new Phrase(
								!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(obj[11].toString())))
										? getBranchesMap().get(getParentBranchMap().get(obj[11].toString()))
										: getBranchesMap().get(obj[11].toString())));
						table.addCell(cell);
					}
					cell = new PdfPCell(new Phrase(getBranchesMap().get(obj[11].toString())));
					table.addCell(cell);
				} else {
					if (StringUtil.isEmpty(branchIdValue)) {
						cell = new PdfPCell(new Phrase(branchesMap.get(obj[11].toString())));
						table.addCell(cell);
					}
				}
			} else {
				cell = new PdfPCell(new Phrase(""));
				table.addCell(cell);
			}

			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.OCP)) {
				cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(obj[29]) ? obj[29].toString() : ""));
				table.addCell(cell);
			}

			if (farmerCodeEnabled.equalsIgnoreCase("1")) {
				cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(obj[3]) ? obj[3].toString() : ""));
				table.addCell(cell);
			}
			if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)
					&& !getCurrentTenantId().equalsIgnoreCase(ESESystem.OCP)) {

				if (!ObjectUtil.isEmpty(obj[20])) {
					HarvestSeason season = getHarvestSeason(String.valueOf(obj[20]));
					cell = new PdfPCell(new Phrase(StringUtil.isEmpty(season) ? "" : season.getName()));// Season
				} else {
					cell = new PdfPCell(new Phrase(""));// Season
				}
				table.addCell(cell);
			}

			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.AVT)) {
				if (!ObjectUtil.isEmpty(obj[31])) {
					cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(obj[31]) ? obj[31].toString() : ""));
					table.addCell(cell);
				} else {
					cell = new PdfPCell(new Phrase(""));
					table.addCell(cell);
				}
			}

			cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(obj[2]) ? obj[2].toString() : ""));
			table.addCell(cell);
			if (!getCurrentTenantId().equalsIgnoreCase("gsma")) {
				cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(obj[4]) ? obj[4].toString() : ""));// Gender
				table.addCell(cell);
			}
			/*
			 * cell.setCellValue(new
			 * HSSFRichTextString(!ObjectUtil.isEmpty(obj[4])?obj[4].toString():
			 * ""));//Age
			 */
			if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)
					&& !getCurrentTenantId().equalsIgnoreCase("gsma")) {
				if (!ObjectUtil.isEmpty(obj[5])) {
					String value = CurrencyUtil.getDecimalFormat(Double.valueOf(obj[5].toString()), "");
					cell = new PdfPCell(new Phrase(value));
					table.addCell(cell);
				} else {
					cell = new PdfPCell(new Phrase(0));
					table.addCell(cell);
				}
			}

			// Group Name
			if (getCurrentTenantId().equalsIgnoreCase("ocp")) {
				cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(obj[8]) ? obj[8].toString() : ""));
				table.addCell(cell);
			}

			// ics Name

			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)
					&& (StringUtil.isEmpty(getBranchId()) || getBranchId().equalsIgnoreCase("organic"))) {

				cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(obj[17]) ? obj[17].toString() : ""));
				table.addCell(cell);
				cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(obj[19]) ? obj[19].toString() : ""));
				table.addCell(cell);
			}
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)
					&& (StringUtil.isEmpty(getBranchId()) || !getBranchId().equalsIgnoreCase("bci"))) {
				cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(obj[18]) ? obj[18].toString() : ""));// Farmer
				table.addCell(cell); // code
				// tracenet
			}
			if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
				if (!ObjectUtil.isEmpty(obj[12])) {
					// FarmCatalogue catalogue =
					// catalogueService.findCatalogueByCode(obj[11].toString());
					cell = new PdfPCell(new Phrase(getCatlogueValueByCode(obj[12].toString()).getName()));
					table.addCell(cell);
				} else {
					cell = new PdfPCell(new Phrase(""));
					table.addCell(cell);
				}
				if (!ObjectUtil.isEmpty(obj[16])) {
					// FarmCatalogue cat =
					// catalogueService.findCatalogueByCode(obj[15].toString());
					cell = new PdfPCell(new Phrase(getCatlogueValueByCode(obj[16].toString()).getName()));
					table.addCell(cell);

				} else {
					cell = new PdfPCell(new Phrase(""));
					table.addCell(cell);
				}
			}
			
			
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)){
		
				cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(obj[25]) ? obj[25].toString() : ""));
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(obj[31]) ? obj[31].toString() : ""));
				table.addCell(cell);
				List<String> agent = agentService.findAgentNameByWareHouseId(Long.valueOf(obj[32].toString()));
				if (!ObjectUtil.isListEmpty(agent)) {
					String json = new Gson().toJson(agent);
					String result = json.replaceAll("\"", "");
					cell = new PdfPCell(new Phrase(result.substring(1, result.length()-1), cellFont));
					table.addCell(cell);
					
					}
				else{
					cell = new PdfPCell(new Phrase("", cellFont));
					table.addCell(cell);
				}
			}
			cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(obj[7]) ? obj[7].toString() : ""));
			table.addCell(cell);
			cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(obj[13]) ? obj[13].toString() : ""));

			table.addCell(cell);
			cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(obj[14]) ? obj[14].toString() : ""));

			table.addCell(cell);
			cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(obj[15]) ? obj[15].toString() : ""));
			table.addCell(cell);

			// table.addCell(cell);
			/*
			 * if (getCurrentTenantId().equalsIgnoreCase("wilmar")) {
			 * 
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(!ObjectUtil.isEmpty(obj[10]) ?
			 * getText("status" + obj[10]) : ""));
			 * 
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(!ObjectUtil.isEmpty(obj[27]) ?
			 * obj[27].toString() : "0.00"));
			 * 
			 * if (!ObjectUtil.isEmpty(obj[9]) && obj[9].equals(1)) { cell =
			 * row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(!ObjectUtil.isEmpty(obj[28]) ?
			 * obj[28].toString() : "")); } else { cell =
			 * row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString("Conventional")); } }
			 */
			if (!getCurrentTenantId().equalsIgnoreCase("efk") && !getCurrentTenantId().equalsIgnoreCase("ocp")
					&& !getCurrentTenantId().equalsIgnoreCase("gsma")
					&& !getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)
					&& !getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)
					&& !getCurrentTenantId().equalsIgnoreCase(ESESystem.AGRO_TENANT)
					&& !getCurrentTenantId().equalsIgnoreCase("symrise")
					&& !getCurrentTenantId().equalsIgnoreCase(ESESystem.GRIFFITH_TENANT_ID)
					&& !getCurrentTenantId().equalsIgnoreCase(ESESystem.IFFCO_TENANT_ID)
					&& !getCurrentTenantId().equalsIgnoreCase(ESESystem.AWI_TENANT_ID)
					&& !getCurrentTenantId().equalsIgnoreCase("kenyafpo")
					) {
				String subGridColumnHeaders = getLocaleProperty("exportFarmerListSubgridHeadings");
				int subGridIterator = 1;
				cell = new PdfPCell(new Phrase("", cellFont));
				table.addCell(cell);
				cellFont = new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK);

				subGridColWidth = subGridColumnHeaders.split("\\,").length;
				for (int i = (subGridColWidth + 1); i < table.getNumberOfColumns(); i++) {
					cell = new PdfPCell(new Phrase("", cellFont));
					table.addCell(cell);
				}
				for (String cellHeader : subGridColumnHeaders.split("\\,")) {
					if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
						cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
					} else {
						cellHeader = cellHeader.trim();
					}
					cell = new PdfPCell(new Phrase(cellHeader, cellFont));
					cell.setBackgroundColor(GrayColor.LIGHT_GRAY);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setNoWrap(false); // To set wrapping of text in cell.
					// cell.setColspan(3); // To add column span.
					table.addCell(cell);
				}

				FarmIcsConversion farmIcs = new FarmIcsConversion();
				Farm farmInfo = new Farm();
				Farmer farmer = new Farmer();
				Set<FarmIcsConversion> icsCon = new HashSet<FarmIcsConversion>();

				farmInfo.setFarmer(farmer);
				if (!ObjectUtil.isEmpty(farm.getFarmer())) {
					farmInfo.getFarmer().setOrganicStatus("-1");
				}
				if (!StringUtil.isEmpty(organicStatus)) {

					if (!organicStatus.equalsIgnoreCase("conventional")) {
						farmIcs.setOrganicStatus(organicStatus);
						farmInfo.setFarmIcsConv(farmIcs);
						icsCon.add(farmInfo.getFarmIcsConv());
						if (organicStatus.equalsIgnoreCase("3")) {
							farmInfo.getFarmer().setOrganicStatus("3");
							farmInfo.getFarmer().setIsCertifiedFarmer(1);
						} else {
							farmInfo.getFarmer().setOrganicStatus("0");
							farmInfo.getFarmer().setIsCertifiedFarmer(1);
						}

					} else {
						farmInfo.getFarmer().setOrganicStatus("1");
						farmInfo.getFarmer().setIsCertifiedFarmer(0);
					}
				}

				farmInfo.getFarmer().setId(Long.valueOf(obj[1].toString()));
				super.filter = farmInfo;
				Map data = readData();

				List<Farm> subGridRows = (List<Farm>) data.get(ROWS);
				// List<Farm> farms =
				// farmerService.listFarmByFarmerId(Long.parseLong(obj[0].toString()));
				for (Farm frm : subGridRows) {
					// table.addCell(cell);
					cell = new PdfPCell(new Phrase("", cellFont));
					table.addCell(cell);
					cell = new PdfPCell(new Phrase("", cellFont));
					table.addCell(cell);
					cell = new PdfPCell(new Phrase("", cellFont));
					table.addCell(cell);
					cell = new PdfPCell(new Phrase("", cellFont));
					table.addCell(cell);
					cell = new PdfPCell(new Phrase(!StringUtil.isEmpty(frm.getFarmCode()) ? frm.getFarmCode() : ""));
					table.addCell(cell);
					cell = new PdfPCell(new Phrase(frm.getFarmName()));
					table.addCell(cell);
					if (frm.getFarmer().getIsCertifiedFarmer() == 1) {
						FarmIcsConversion fIcs = farmerService
								.findFarmIcsConversionByFarmIdWithActive(Long.valueOf(frm.getId()));
						if (!ObjectUtil.isEmpty(fIcs) && fIcs != null && !StringUtil.isEmpty(fIcs.getOrganicStatus())) {
							cell = new PdfPCell(new Phrase(fIcs.getOrganicStatus().equalsIgnoreCase("3")
									? getLocaleProperty("alrdyCertified") : getLocaleProperty("inprocess")));
							table.addCell(cell);
						} else {
							cell = new PdfPCell(new Phrase("Conventional"));
							table.addCell(cell);
						}
					} else {
						cell = new PdfPCell(new Phrase("Conventional"));
						table.addCell(cell);
					}

					cell = new PdfPCell(new Phrase(
							(frm.getFarmICSConversion() != null && !ObjectUtil.isListEmpty(frm.getFarmICSConversion()))
									? (frm.getFarmICSConversion().iterator().next().getIcsType() != null && !StringUtil
											.isEmpty(frm.getFarmICSConversion().iterator().next().getIcsType()))
													? getText("icsStatus"
															+ frm.getFarmICSConversion().iterator().next().getIcsType())
													: ""
									: ""));
					table.addCell(cell);
					cell = new PdfPCell(new Phrase(frm.getFarmDetailedInfo().getTotalLandHolding()));
					table.addCell(cell);
					if (!ObjectUtil.isEmpty(frm.getFarmCrops())) {
						String prods = frm.getFarmCrops() != null ? frm.getFarmCrops().stream()
								.map(e -> e.getProcurementVariety().getProcurementProduct().getName()).distinct()
								.reduce("", (a, b) -> a + "," + b) : null;
						cell = new PdfPCell(new Phrase(!StringUtil.isEmpty(prods) ? prods.substring(1) : ""));
						table.addCell(cell);
					} else {
						cell = new PdfPCell(new Phrase(""));
						table.addCell(cell);
					}
					if (getCurrentTenantId().equalsIgnoreCase("avt")) {
						cell = new PdfPCell(new Phrase(""));
						table.addCell(cell);
					}
				}

			}
			/*
			 * if (!getCurrentTenantId().equalsIgnoreCase("symrise") &&
			 * !getCurrentTenantId().equalsIgnoreCase("gsma") &&
			 * !getCurrentTenantId().equalsIgnoreCase("ocp")){ cell = new
			 * PdfPCell(new Phrase("", cellFont)); table.addCell(cell);}
			 */

		}

		document.add(table); // Add table to document.

		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		document.close();
		fileOut.close();
		return stream;

	}

	/*
	 * public InputStream getPDFExportStream(List<List<Object>> obj,
	 * List<String> filters) throws IOException, DocumentException,
	 * NoSuchFieldException, SecurityException {
	 * 
	 * Font cellFont = null; // font for cells. Font titleFont = null; // font
	 * for title text. Paragraph title = null; // to add title for report String
	 * columnHeaders = null; // to hold column headers from property // file.
	 * SimpleDateFormat fileNameDateFormat = new
	 * SimpleDateFormat("yyyyMMddHHmmss"); Document document = new Document();
	 * 
	 * HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
	 * 
	 * String makeDir = FileUtil.storeXls(request.getSession().getId()); String
	 * fileName = getText("FLListFile") + fileNameDateFormat.format(new Date())
	 * + ".pdf"; FileOutputStream fileOut = new FileOutputStream(makeDir +
	 * fileName); PdfWriter.getInstance(document, fileOut);
	 * 
	 * document.open();
	 * 
	 * PdfPTable table = null;
	 * 
	 * branchIdValue = getBranchId(); // set value for branch id.
	 * buildBranchMap();
	 * 
	 * com.itextpdf.text.Image logo =
	 * com.itextpdf.text.Image.getInstance(exportLogo());
	 * logo.scaleAbsolute(100, 50); // resizing logo image size.
	 * 
	 * document.add(logo); // Adding logo in PDF file.
	 * 
	 * // below of code for title text titleFont = new
	 * Font(FontFamily.HELVETICA, 14, Font.BOLD, GrayColor.GRAYBLACK); title =
	 * new Paragraph(new Phrase(getText("FLExportPDFTitle"), titleFont));
	 * title.setAlignment(Element.ALIGN_CENTER); document.add(title);
	 * document.add( new Paragraph(new Phrase(" ", new
	 * Font(FontFamily.HELVETICA, 15, Font.NORMAL, GrayColor.GRAYBLACK)))); //
	 * Add // a // blank // line // after // title.
	 * 
	 * for (String filter : filters) { document.add(new Paragraph( new
	 * Phrase(filter, new Font(FontFamily.HELVETICA, 8, Font.NORMAL,
	 * GrayColor.GRAYBLACK)))); document.add(new Paragraph( new Phrase(" ", new
	 * Font(FontFamily.HELVETICA, 4, Font.NORMAL, GrayColor.GRAYBLACK)))); }
	 * 
	 * // columnHeaders = columnHeaders();
	 * 
	 * if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {
	 * 
	 * if (StringUtil.isEmpty(branchIdValue)) { columnHeaders =
	 * getLocaleProperty("exportFarmerListColumnHeaderBranchPratibha"); } else
	 * if (getBranchId().equalsIgnoreCase("organic")) { columnHeaders =
	 * getLocaleProperty("OrganicFarmerListExportHeader"); } else if
	 * (getBranchId().equalsIgnoreCase("bci")) { columnHeaders =
	 * getLocaleProperty("BCIFarmerListExportHeader"); }
	 * 
	 * } else { if (StringUtil.isEmpty(branchIdValue)) { columnHeaders =
	 * getLocaleProperty("exportFarmerListColumnHeaderBranch"); } else {
	 * columnHeaders = getLocaleProperty("exportFarmerListColumnHeader"); } }
	 * 
	 * if (!ObjectUtil.isEmpty(columnHeaders)) { PdfPCell cell = null; // cell
	 * for table. cellFont = new Font(FontFamily.HELVETICA, 10, Font.BOLD,
	 * GrayColor.GRAYBLACK); // setting // font // for // header // cells // of
	 * // table. table = new PdfPTable(columnHeaders.split("\\,").length); //
	 * Code // for // setting // table // column // Numbers.
	 * table.setWidthPercentage(100); // Set Table Width.
	 * table.getDefaultCell().setUseAscender(true);
	 * table.getDefaultCell().setUseDescender(true);
	 * 
	 * for (String cellHeader : columnHeaders.split("\\,")) {
	 * 
	 * if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
	 * cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT,
	 * getCurrencyType()); } else if
	 * (cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT)) { cellHeader =
	 * cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType()); }
	 * 
	 * cell = new PdfPCell(new Phrase(cellHeader, cellFont));
	 * cell.setBackgroundColor(new BaseColor(255, 255, 224));
	 * cell.setHorizontalAlignment(Element.ALIGN_CENTER); cell.setNoWrap(false);
	 * // To set wrapping of text in cell. // cell.setColspan(3); // To add
	 * column span. table.addCell(cell); }
	 * 
	 * cellFont = new Font(FontFamily.HELVETICA, 8, Font.NORMAL,
	 * GrayColor.GRAYBLACK); // setting // font // for // cells.
	 * 
	 * Long serialNo = 0L;
	 * 
	 * for (List<Object> entityObj : obj) { // iterate over all list of //
	 * objects.
	 * 
	 * 
	 * serialNo++;
	 * 
	 * cell=new PdfPCell(new Paragraph(new
	 * Phrase(String.valueOf(serialNo),cellFont))); table.addCell(cell);
	 * 
	 * 
	 * for (Object entityField : entityObj) { // BEGIN OF CODE FOR A PARTICULAR
	 * CELL IN A ROW OF TABLE TO // PDF FILE.
	 * 
	 * if (entityField != null) { cell = new PdfPCell(new Phrase(
	 * StringUtil.isEmpty(entityField.toString()) ? getText("NA") :
	 * entityField.toString(), cellFont)); table.addCell(cell); } else { cell =
	 * new PdfPCell(new Phrase("", cellFont)); table.addCell(cell); } // END OF
	 * CODE FOR A PARTICULAR CELL IN A ROW OF TABLE TO // PDF FILE. } }
	 * 
	 * document.add(table); // Add table to document. } InputStream stream = new
	 * FileInputStream(new File(makeDir + fileName)); document.close();
	 * fileOut.close(); return stream; }
	 */

	public IDeviceService getDeviceService() {
		return deviceService;
	}

	public void setDeviceService(IDeviceService deviceService) {
		this.deviceService = deviceService;
	}

	public List<Farm> getFarmList() {
		return farmList;
	}

	public void setFarmList(List<Farm> farmList) {
		this.farmList = farmList;
	}

	public Map<String, String> getFarmerCodList() {
		return farmerCodList;
	}

	public void setFarmerCodList(Map<String, String> farmerCodList) {
		this.farmerCodList = farmerCodList;
	}

	public Map<String, String> getFarmerNameList() {
		return farmerNameList;
	}

	public void setFarmerNameList(Map<String, String> farmerNameList) {
		this.farmerNameList = farmerNameList;
	}

	public Map<String, String> getFarmerFarmerIdList() {
		return farmerFarmerIdList;
	}

	public void setFarmerFarmerIdList(Map<String, String> farmerFarmerIdList) {
		this.farmerFarmerIdList = farmerFarmerIdList;
	}

	public Map<String, String> getFatherNameList() {
		return fatherNameList;
	}

	public void setFatherNameList(Map<String, String> fatherNameList) {
		this.fatherNameList = fatherNameList;
	}

	public Map<String, String> getVillageMap() {

		// return locationService.listVillageIdAndName();

		locationService.listVillageIdAndName().stream().forEach(u -> {
			villageMap.put(u[1].toString(), u[2].toString());
		});
		return villageMap;
	}

	public void setVillageMap(Map<String, String> villageMap) {
		this.villageMap = villageMap;
	}

	public Farm getFilter2() {
		return filter2;
	}

	public void setFilter2(Farm filter2) {
		this.filter2 = filter2;
	}

	public String getFarmSize() {
		return farmSize;
	}

	public void setFarmSize(String farmSize) {
		this.farmSize = farmSize;
	}

	public Map<String, String> getLgMap() {
		locationService.listLocalityIdCodeAndName().stream().forEach(u -> {
			lgMap.put(u[1].toString(), u[2].toString());
		});
		return lgMap;
	}

	public void setLgMap(Map<String, String> lgMap) {

		this.lgMap = lgMap;
	}

	public String getLgId() {
		return lgId;
	}

	public void setLgId(String lgId) {
		this.lgId = lgId;
	}

	public Map<String, String> getSurNameList() {
		return surNameList;
	}

	public void setSurNameList(Map<String, String> surNameList) {
		this.surNameList = surNameList;
	}

	public String getSurName() {
		return surName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}

	public String populateAFLExport() throws Exception {
		InputStream is = getExportData(IExporter.EXPORT_MANUAL);
		if (!ObjectUtil.isEmpty(is)) {
			setXlsFileName(getText("AFLReport") + fileNameDateFormat.format(new Date()));
			Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
			fileMap.put(xlsFileName, is);
			setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("farmerList").trim(), fileMap, ".xls"));
			return "xls";
		} else {
			return LIST;
		}
	}

	private InputStream getExportData(String exportType) throws Exception {

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getText("exportXLSAFLTitle"));
		HSSFPatriarch drawing = sheet.createDrawingPatriarch();
		HSSFRow titleRow;
		HSSFRow subTitleRow;
		HSSFCell cell;
		HSSFCellStyle style1 = wb.createCellStyle();
		int rowNum = 3;
		HSSFFont font1 = wb.createFont();
		font1.setFontHeightInPoints((short) 15);
		HSSFCellStyle filterData = wb.createCellStyle();
		HSSFCellStyle filterStyle = wb.createCellStyle();
		HSSFCellStyle filterStyle1 = wb.createCellStyle();
		HSSFCellStyle filterStyle2 = wb.createCellStyle();
		HSSFCellStyle cellStyle = wb.createCellStyle();
		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(3);
		cell.setCellValue(new HSSFRichTextString(getText("exportXLSAFLTitle")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 12);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		style1.setFont(font1);

		filterStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		filterStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		filterStyle2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		filterStyle2.setWrapText(true);
		filterStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		filterStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		filterStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		filterStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);

		filterStyle1.setFillForegroundColor(HSSFColor.WHITE.index);
		filterStyle1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		filterStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		filterStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		filterStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		filterStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		filterStyle.setWrapText(true);
		filterStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		filterStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		filterStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		filterStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap();

		filter3 = new AFLExport();
		if (!StringUtil.isEmpty(icsName)) {
			filter3.setIcsName(icsName);
		}

		if (!StringUtil.isEmpty(insYear)) {
			setInsYear(insYear);
		} else {
			setInsYear(DateUtil.getYearByDateTime(new Date()));
		}

		if (!StringUtil.isEmpty(samithiName)) {
			Warehouse w = locationService.findSamithiById(Long.valueOf(samithiName.trim()));
			filter3.setGroup(w.getName());
		}

		if (!StringUtil.isEmpty(icsType)) {
			filter3.setIcsName(icsType);
		}

		if (!StringUtil.isEmpty(gender)) {
			filter3.setGender(gender);
		}

		if (!StringUtil.isEmpty(villageName)) {
			Village v =locationService.findVillageByCode(villageName.trim());
			filter3.setVillageName(v.getName());
		}
		if (!StringUtil.isEmpty(stateName)) {
			State s = locationService.findStateByCode(stateName.trim());
			filter3.setStateName(s.getName());
		}

		filter3.setBranch(branchIdValue);
		if (!StringUtil.isEmpty(branchIdParma)) {
			if (!getIsMultiBranch().equalsIgnoreCase("1")) {
				List<String> branchList = new ArrayList<>();
				branchList.add(branchIdParma);
				filter3.setBranchesList(branchList);
			} else {
				List<String> branchList = new ArrayList<>();
				List<BranchMaster> branches = clientService.listChildBranchIds(branchIdParma);
				branchList.add(branchIdParma);
				branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
					branchList.add(branch.getBranchId());
				});
				filter3.setBranchesList(branchList);
			}
		}
		if (!StringUtil.isEmpty(subBranchIdParma) && !subBranchIdParma.equals("0")) {
			filter3.setBranch(subBranchIdParma);
		}
		super.filter = this.filter3;
		rowNum = rowNum + 3;
		HSSFRow filterRow1, filterRow2, filterRow3, filterRow4, filterRow5,filterRow6,filterRow7,filterRow8;
		HSSFRow filterRowTitle = sheet.createRow(rowNum);
		HSSFRow subFilterRowTitle = sheet.createRow(rowNum);
		HSSFFont filterFont = wb.createFont();
		filterFont.setFontHeightInPoints((short) 8);

		subFilterRowTitle = sheet.createRow(rowNum++);
		cell = subFilterRowTitle.createCell(2);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("projectName")));
		filterFont.setBoldweight((short) 12);
		filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		filterData.setFont(filterFont);
		cell.setCellStyle(filterData);
		filterRow2 = sheet.createRow(rowNum++);

		cell = subFilterRowTitle.createCell(3);
		cell.setCellValue(new HSSFRichTextString(getBranchId()));
		filterFont.setBoldweight((short) 18);
		filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		filterData.setFont(filterFont);
		cell.setCellStyle(filterData);

		filterRowTitle = sheet.createRow(rowNum++);
		cell = filterRowTitle.createCell(2);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("filter")));
		filterFont.setBoldweight((short) 12);
		filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		filterData.setFont(filterFont);
		cell.setCellStyle(filterData);
		filterRow1 = sheet.createRow(rowNum++);
		if (!StringUtil.isEmpty(icsName)) {
			filterRow1 = sheet.createRow(rowNum++);

			cell = filterRow1.createCell(2);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("icsName")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterData.setFont(filterFont);
			cell.setCellStyle(filterData);

			cell = filterRow1.createCell(3);
			cell.setCellValue(new HSSFRichTextString(catalogueService.findCatalogueValueByCode(filter3.getIcsName())));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterData.setFont(filterFont);
			cell.setCellStyle(filterData);
		}

		if (!StringUtil.isEmpty(insYear)) {
			filterRow2 = sheet.createRow(rowNum++);

			cell = filterRow2.createCell(2);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("insYear")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterData.setFont(filterFont);
			cell.setCellStyle(filterData);

			cell = filterRow2.createCell(3);
			cell.setCellValue(new HSSFRichTextString(insYear));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterData.setFont(filterFont);
			cell.setCellStyle(filterData);
		}

		if (!StringUtil.isEmpty(samithiName)) {
			filterRow3 = sheet.createRow(rowNum++);

			cell = filterRow3.createCell(2);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("group")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterData.setFont(filterFont);
			cell.setCellStyle(filterData);

			cell = filterRow3.createCell(3);
			Warehouse w = locationService.findSamithiById(Long.valueOf(samithiName.trim()));
			cell.setCellValue(new HSSFRichTextString(w.getName()));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterData.setFont(filterFont);
			cell.setCellStyle(filterData);
		}
		
		if (!StringUtil.isEmpty(stateName)) {
			filterRow4 = sheet.createRow(rowNum++);
			cell = filterRow4.createCell(2);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("stateName")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterData.setFont(filterFont);
			cell.setCellStyle(filterData);

			cell = filterRow4.createCell(3);
			State s = locationService.findStateByCode(stateName.trim());
			cell.setCellValue(new HSSFRichTextString(s.getName()));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterData.setFont(filterFont);
			cell.setCellStyle(filterData);
		}
		
		if (!StringUtil.isEmpty(villageName)) {
			filterRow5 = sheet.createRow(rowNum++);

			cell = filterRow5.createCell(2);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("villageName")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterData.setFont(filterFont);
			cell.setCellStyle(filterData);

			cell = filterRow5.createCell(3);
			Village v = locationService.findVillageByCode(villageName.trim());
			cell.setCellValue(new HSSFRichTextString(v.getName()));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterData.setFont(filterFont);
			cell.setCellStyle(filterData);
		}
		
		if (!StringUtil.isEmpty(gender)) {
			filterRow5 = sheet.createRow(rowNum++);

			cell = filterRow5.createCell(2);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("gender")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterData.setFont(filterFont);
			cell.setCellStyle(filterData);

			cell = filterRow5.createCell(3);
			cell.setCellValue(new HSSFRichTextString(gender));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterData.setFont(filterFont);
			cell.setCellStyle(filterData);
		}
		
		
		int sColNum = 0;
		int eColNum = 0;
		int eRow = rowNum + 2;
		HSSFRow mainGridRowHead2 = sheet.createRow(rowNum);
		mainGridRowHead2.setRowStyle(filterStyle2);
		sColNum = 0;
		for (String head : getLocaleProperty("exportHead1Iccoa").split(",")) {
			cell = mainGridRowHead2.createCell(sColNum);
			cell.setCellStyle(filterStyle);
			cell.setCellValue(new HSSFRichTextString(head));
			sheet.addMergedRegion(new CellRangeAddress(rowNum, eRow, sColNum, sColNum));
			sColNum++;
		}
		HSSFRow mainGridRowHead1 = sheet.createRow(rowNum + 1);
		cell = mainGridRowHead2.createCell(sColNum);
		cell.setCellStyle(filterStyle);
		// cell.setCellValue(new
		// HSSFRichTextString(getLocaleProperty("landMark")));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum + 1, sColNum, sColNum + 1));

		HSSFRow mainGridRowHead3 = sheet.createRow(rowNum + 2);
		/*
		 * for (String head : getLocaleProperty("latLon").split(",")) { cell =
		 * mainGridRowHead3.createCell(sColNum); cell.setCellStyle(filterStyle);
		 * cell.setCellValue(new HSSFRichTextString(head));
		 * sheet.addMergedRegion(new CellRangeAddress(rowNum + 2, rowNum + 2,
		 * sColNum, sColNum)); sColNum++; }
		 */
		cell = mainGridRowHead2.createCell(sColNum);
		cell.setCellStyle(filterStyle);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("field")));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum + 1, sColNum, sColNum + 1));
		for (String head : getLocaleProperty("latLon").split(",")) {
			cell = mainGridRowHead3.createCell(sColNum);
			cell.setCellStyle(filterStyle);
			cell.setCellValue(new HSSFRichTextString(head));
			sheet.addMergedRegion(new CellRangeAddress(rowNum + 2, rowNum + 2, sColNum, sColNum));
			sColNum++;
		}
		Map<Long, ProcurementProduct> cropMap = getProcurmentProductMap(
				productDistributionService.listProcurementProductFromFarmCrops());

		mainGridRowHead2.setRowStyle(filterStyle2);
		mainGridRowHead1.setRowStyle(filterStyle2);
		mainGridRowHead3.setRowStyle(filterStyle2);
		String[] cropFields = getLocaleProperty("cropMainFlds").split(",");
		eColNum = sColNum + (cropMap.size() * cropFields.length) - 1;
		cell = mainGridRowHead2.createCell(sColNum);
		filterFont.setBoldweight((short) 10);
		filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		filterStyle.setFont(filterFont);
		cell.setCellStyle(filterStyle);
		cell.setCellValue(new HSSFRichTextString(getText("crpWiseYld")));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, sColNum, eColNum));
		mainGridRowHead2.setRowStyle(filterStyle2);

		for (Entry<Long, ProcurementProduct> pp : cropMap.entrySet()) {
			cell = mainGridRowHead1.createCell(sColNum);
			cell.setCellStyle(filterStyle);
			cell.setCellValue(new HSSFRichTextString(pp.getValue().getName()));
			sheet.addMergedRegion(
					new CellRangeAddress(rowNum + 1, rowNum + 1, sColNum, sColNum + cropFields.length - 1));
			sColNum = subHead(cell, sColNum, filterStyle, rowNum, sheet, cropFields, mainGridRowHead3);
		}

		Map<Long, FarmCatalogue> cat1 = getFarmCatalougeMapValue(Integer.valueOf(getText("animaHusbanday")));

		eColNum = sColNum + cat1.size() - 1;
		cell = mainGridRowHead2.createCell(sColNum);
		filterFont.setBoldweight((short) 10);
		filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		filterStyle.setFont(filterFont);
		cell.setCellStyle(filterStyle);
		cell.setCellValue(new HSSFRichTextString(getText("info.animal")));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, sColNum, eColNum));

		mainGridRowHead2.setRowStyle(filterStyle2);

		for (Entry<Long, FarmCatalogue> fm : cat1.entrySet()) {
			cell = mainGridRowHead1.createCell(sColNum);
			cell.setCellStyle(filterStyle);
			cell.setCellValue(new HSSFRichTextString(fm.getValue().getName()));
			sheet.addMergedRegion(new CellRangeAddress(rowNum + 1, rowNum + 2, sColNum, sColNum));
			sColNum++;
		}
		// Map<String, CertificateCategory> certiType =
		// getCertificationMap(certificationService.listCertificateCategory());
		Map<Long, FarmCatalogue> certiType = getFarmCatalougeMapValue(Integer.valueOf(getText("certificationType")));
		Map<Long, FarmCatalogue> insMap = getFarmCatalougeMapValue(Integer.valueOf(getText("insType")));
		eColNum = sColNum + 1 + certiType.size() + (insMap.size() * getLocaleProperty("insFields1").split(",").length);
		cell = mainGridRowHead2.createCell(sColNum);
		filterFont.setBoldweight((short) 10);
		filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		filterStyle.setFont(filterFont);
		cell.setCellStyle(filterStyle);
		cell.setCellValue(new HSSFRichTextString(getText("inspection")));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, sColNum, eColNum));
		mainGridRowHead2.setRowStyle(filterStyle2);

		for (String ani : getLocaleProperty("insFields").split(",")) {
			cell = mainGridRowHead1.createCell(sColNum);
			cell.setCellStyle(filterStyle);
			cell.setCellValue(new HSSFRichTextString(ani));
			sheet.addMergedRegion(new CellRangeAddress(rowNum + 1, rowNum + 2, sColNum, sColNum));
			sColNum++;
		}
		for (Entry<Long, FarmCatalogue> fm : insMap.entrySet()) {
			cell = mainGridRowHead1.createCell(sColNum);
			cell.setCellStyle(filterStyle);
			cell.setCellValue(new HSSFRichTextString(fm.getValue().getName()));
			sheet.addMergedRegion(new CellRangeAddress(rowNum + 1, rowNum + 1, sColNum,
					sColNum + getLocaleProperty("insFields1").split(",").length - 1));
			sColNum = subHead(cell, sColNum, filterStyle, rowNum, sheet, getLocaleProperty("insFields1").split(","),
					mainGridRowHead3);
		}

		cell = mainGridRowHead1.createCell(sColNum);
		cell.setCellStyle(filterStyle);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("status")));
		sheet.addMergedRegion(new CellRangeAddress(rowNum + 1, rowNum + 1, sColNum, sColNum + certiType.size() - 1));
		for (Entry<Long, FarmCatalogue> fm : certiType.entrySet()) {
			cell = mainGridRowHead3.createCell(sColNum);
			cell.setCellStyle(filterStyle);
			cell.setCellValue(new HSSFRichTextString(fm.getValue().getName()));
			sheet.addMergedRegion(new CellRangeAddress(rowNum + 2, rowNum + 2, sColNum, sColNum));
			sColNum++;
		}

		Map data = readData();
		List<AFLExport> eData = (List<AFLExport>) data.get(ROWS);
		int sno = 1;
		rowNum += 2;
		HSSFRow row;
		int col = 0;
		Map<Long, String> animalMap = new HashMap<>();
		Map<Long, String> areaMap = new HashMap<>();
		Map<Long, String> typeMap = new HashMap<>();
		Map<Long, String> varietyMap = new HashMap<>();
		Map<Long, String> treeMap = new HashMap<>();
		Map<Long, String> eYieldMap = new HashMap<>();
		Map<Long, String> aYieldMap = new HashMap<>();
		Map<String, List<FarmIcsConversion>> frmIcsConMap = new HashMap<>();
		Map<Long, List<FarmIcsConversion>> frmIcsConvMap = new HashMap<>();
		final SimpleDateFormat realDateFormat = new SimpleDateFormat(getGeneralDateFormat());
		final SimpleDateFormat cheDateFormat = new SimpleDateFormat("MM-YYYY");

		for (AFLExport aflExport : eData) {
			if (!ObjectUtil.isEmpty(aflExport)) {
				row = sheet.createRow(++rowNum);
				col = 0;
				cell = row.createCell(col++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(String.valueOf(sno++)));

				cell = row.createCell(col++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(
						!StringUtil.isEmpty(aflExport.getFarmerCode()) ? aflExport.getFarmerCode() : ""));

				cell = row.createCell(col++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(
						!StringUtil.isEmpty(aflExport.getTracenetCode()) ? aflExport.getTracenetCode() : ""));

				cell = row.createCell(col++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(
						!StringUtil.isEmpty(aflExport.getApedaNo()) ? aflExport.getApedaNo() : ""));

				cell = row.createCell(col++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(
						!StringUtil.isEmpty(aflExport.getFarmerName()) ? aflExport.getFarmerName() : ""));

				cell = row.createCell(col++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(
						!StringUtil.isEmpty(aflExport.getFatherName()) ? aflExport.getFatherName() : ""));

				cell = row.createCell(col++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(
						!StringUtil.isEmpty(aflExport.getGender()) ? aflExport.getGender() : ""));

				cell = row.createCell(col++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(
						new HSSFRichTextString(!StringUtil.isEmpty(aflExport.getAge()) ? aflExport.getAge() : ""));

				cell = row.createCell(col++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(
						new HSSFRichTextString(!StringUtil.isEmpty(aflExport.getCaste()) ? aflExport.getCaste() : ""));

				cell = row.createCell(col++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(
						!StringUtil.isEmpty(aflExport.getAddress()) ? aflExport.getAddress() : ""));

				cell = row.createCell(col++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(
						!StringUtil.isEmpty(aflExport.getHouseNo()) ? aflExport.getHouseNo() : ""));

				cell = row.createCell(col++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(
						!StringUtil.isEmpty(aflExport.getMobileNo()) ? aflExport.getMobileNo() : ""));

				cell = row.createCell(col++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(
						!StringUtil.isEmpty(aflExport.getAadharNo()) ? aflExport.getAadharNo() : ""));

				cell = row.createCell(col++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(
						new HSSFRichTextString(!StringUtil.isEmpty(aflExport.getGroup()) ? aflExport.getGroup() : ""));

				cell = row.createCell(col++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(
						!StringUtil.isEmpty(aflExport.getVillageName()) ? aflExport.getVillageName() : ""));

				/*
				 * cell = row.createCell(col++); cell.setCellStyle(cellStyle);
				 * cell.setCellValue(new HSSFRichTextString(
				 * !StringUtil.isEmpty(aflExport.getGpsTracking()) ?
				 * aflExport.getGpsTracking() : ""));
				 */

				cell = row.createCell(col++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(
						!StringUtil.isEmpty(aflExport.getCityName()) ? aflExport.getCityName() : ""));

				cell = row.createCell(col++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(
						!StringUtil.isEmpty(aflExport.getDistrictName()) ? aflExport.getDistrictName() : ""));

				cell = row.createCell(col++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(
						!StringUtil.isEmpty(aflExport.getPinCode()) ? aflExport.getPinCode() : ""));

				cell = row.createCell(col++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(
						!StringUtil.isEmpty(aflExport.getLandmark()) ? aflExport.getLandmark() : ""));
				if (!ObjectUtil.isListEmpty(aflExport.getFarmICSConversion())) {
					frmIcsConMap = aflExport.getFarmICSConversion().stream()
							.filter(frmics -> (!ObjectUtil.isEmpty(frmics.getInspectionDate()) && (DateUtil
									.getYearByDateTime(frmics.getInspectionDate()).equalsIgnoreCase(insYear))
									&& !StringUtil.isEmpty(frmics.getInsType())))
							.collect(Collectors.groupingBy(FarmIcsConversion::getInsType));
				}
				if (frmIcsConMap.size() > 0) {
					Set<Map.Entry<String, List<FarmIcsConversion>>> fic = frmIcsConMap.entrySet();
					Map.Entry<String, List<FarmIcsConversion>> ficLastMap = (Map.Entry<String, List<FarmIcsConversion>>) frmIcsConMap
							.entrySet().toArray()[frmIcsConMap.size() - 1];
					List<FarmIcsConversion> ficList = frmIcsConMap.get(ficLastMap.getKey());
					cell = row.createCell(col++);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(
							new HSSFRichTextString(!StringUtil.isEmpty(ficList.iterator().next().getTotalLand())
									? ficList.iterator().next().getTotalLand() : ""));

					cell = row.createCell(col++);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(
							new HSSFRichTextString(!StringUtil.isEmpty(ficList.iterator().next().getOrganicLand())
									? ficList.iterator().next().getOrganicLand() : ""));

					cell = row.createCell(col++);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(
							new HSSFRichTextString(!StringUtil.isEmpty(ficList.iterator().next().getTotalSite())
									? ficList.iterator().next().getTotalSite() : ""));
				} else {
					cell = row.createCell(col++);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(new HSSFRichTextString(
							!StringUtil.isEmpty(aflExport.getTotalArea()) ? aflExport.getTotalArea() : ""));

					cell = row.createCell(col++);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(aflExport.getAreaUnderOrganic())
							? aflExport.getAreaUnderOrganic() : ""));

					cell = row.createCell(col++);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(new HSSFRichTextString(""));
				}

				cell = row.createCell(col++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(
						!StringUtil.isEmpty(aflExport.getSurveyNo()) ? aflExport.getSurveyNo() : ""));

				/*
				 * cell = row.createCell(col++); cell.setCellStyle(cellStyle);
				 * cell.setCellValue(new HSSFRichTextString(
				 * !StringUtil.isEmpty(aflExport.getLandmarkLattitude()) ?
				 * DecimalToDegMinSec(Double.valueOf(aflExport.
				 * getLandmarkLattitude())) : ""));
				 * 
				 * cell = row.createCell(col++); cell.setCellStyle(cellStyle);
				 * cell.setCellValue(new HSSFRichTextString(
				 * !StringUtil.isEmpty(aflExport.getLandmarkLongitude()) ?
				 * DecimalToDegMinSec(Double.valueOf(aflExport.
				 * getLandmarkLongitude())) : ""));
				 */
				cell = row.createCell(col++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(aflExport.getFieldLattitude())
						? DecimalToDegMinSec(Double.valueOf(aflExport.getFieldLattitude())) : ""));

				cell = row.createCell(col++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(aflExport.getFieldLongitude())
						? DecimalToDegMinSec(Double.valueOf(aflExport.getFieldLongitude())) : ""));

				// Area
				areaMap = new HashMap<>();
				if (!StringUtil.isEmpty(aflExport.getCultivationArea())) {
					String[] areas = aflExport.getCultivationArea().split(",");
					if (!StringUtil.isEmpty(areas)) {
						for (String area : areas) {
							if (area.split("-").length > 1)
								areaMap.put(Long.parseLong(area.split("-")[0]), area.split("-")[1]);
						}
					}
				}
				// Type
				typeMap = new HashMap<>();
				if (!StringUtil.isEmpty(aflExport.getCropType())) {
					String[] cropsType = aflExport.getCropType().split(",");
					if (!StringUtil.isEmpty(cropsType)) {
						for (String cropType : cropsType) {
							if (cropType.split("-").length > 1)
								typeMap.put(Long.parseLong(cropType.split("-")[0]), cropType.split("-")[1]);
						}
					}
				}

				// Variety
				varietyMap = new HashMap<>();
				if (!StringUtil.isEmpty(aflExport.getVariety())) {
					String[] varietys = aflExport.getVariety().split(",");
					if (!StringUtil.isEmpty(varietys)) {
						for (String var : varietys) {
							if (var.split("-").length > 1)
								varietyMap.put(Long.parseLong(var.split("-")[0]), var.split("-")[1]);
						}
					}
				}

				// NO of Plants
				treeMap = new HashMap<>();
				if (!StringUtil.isEmpty(aflExport.getNoOfTrees())) {
					String[] trees = aflExport.getNoOfTrees().split(",");
					if (!StringUtil.isEmpty(trees)) {
						for (String t : trees) {
							if (t.split("-").length > 1)
								treeMap.put(Long.parseLong(t.split("-")[0]), t.split("-")[1]);
						}
					}
				}

				// Estimated Yield
				eYieldMap = new HashMap<>();
				if (!StringUtil.isEmpty(aflExport.getEstimatedYield())) {
					String[] eYields = aflExport.getEstimatedYield().split(",");
					if (!StringUtil.isEmpty(eYields)) {
						for (String ey : eYields) {
							if (ey.split("-").length > 1)
								eYieldMap.put(Long.parseLong(ey.split("-")[0]), ey.split("-")[1]);
						}
					}
				}
				// Actual Yiels
				aYieldMap = new HashMap<>();
				if (!StringUtil.isEmpty(aflExport.getActualYield())) {
					String[] aYields = aflExport.getActualYield().split(",");
					if (!StringUtil.isEmpty(aYields)) {
						for (String ay : aYields) {
							if (ay.split("-").length > 1)
								aYieldMap.put(Long.parseLong(ay.split("-")[0]), ay.split("-")[1]);
						}
					}
				}

				for (Entry<Long, ProcurementProduct> pp : cropMap.entrySet()) {
					cell = row.createCell(col++);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(new HSSFRichTextString(
							areaMap.containsKey(pp.getValue().getId()) ? areaMap.get(pp.getValue().getId()) : ""));

					cell = row.createCell(col++);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(new HSSFRichTextString(typeMap.containsKey(pp.getValue().getId())
							? getText("cs" + typeMap.get(pp.getValue().getId())) : ""));

					cell = row.createCell(col++);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(new HSSFRichTextString(varietyMap.containsKey(pp.getValue().getId())
							? varietyMap.get(pp.getValue().getId()) : ""));

					cell = row.createCell(col++);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(new HSSFRichTextString(
							treeMap.containsKey(pp.getValue().getId()) ? treeMap.get(pp.getValue().getId()) : ""));

					cell = row.createCell(col++);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(new HSSFRichTextString(
							eYieldMap.containsKey(pp.getValue().getId()) ? eYieldMap.get(pp.getValue().getId()) : ""));

					cell = row.createCell(col++);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(new HSSFRichTextString(
							aYieldMap.containsKey(pp.getValue().getId()) ? aYieldMap.get(pp.getValue().getId()) : ""));
				}

				animalMap = new HashMap<>();
				if (!StringUtil.isEmpty(aflExport.getAnimalHusbandry())) {
					String[] animalHusbandry = aflExport.getAnimalHusbandry().split(",");
					if (!StringUtil.isEmpty(animalHusbandry)) {
						for (String anh : animalHusbandry) {
							animalMap.put(Long.parseLong(anh.split("-")[0]), anh.split("-")[1]);
						}
					}
				}
				for (Entry<Long, FarmCatalogue> fm : cat1.entrySet()) {
					cell = row.createCell(col++);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(new HSSFRichTextString(
							animalMap.containsKey(fm.getValue().getId()) ? animalMap.get(fm.getValue().getId()) : ""));
				}
				cell = row.createCell(col++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(aflExport.getDateOfReg())
						? realDateFormat.format(
								DateUtil.convertStringToDate(aflExport.getDateOfReg(), DateUtil.DATABASE_DATE_FORMAT))
						: ""));

				cell = row.createCell(col++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(
						!StringUtil.isEmpty(aflExport.getLastDateCheApp()) ? aflExport.getLastDateCheApp() : ""));

				if (!ObjectUtil.isListEmpty(aflExport.getFarmICSConversion())) {
					frmIcsConvMap = aflExport.getFarmICSConversion().stream().filter(
							frmics -> (!ObjectUtil.isEmpty(frmics.getInspectionDate())) && frmics.getIsActive() == 1)
							.sorted().collect(Collectors.groupingBy(FarmIcsConversion::getId));
				} else {
					frmIcsConMap = new HashMap<>();
				}

				if (frmIcsConvMap.size() > 0) {
					/*
					 * Set<Map.Entry<Long, List<FarmIcsConversion>>> fic =
					 * frmIcsConvMap.entrySet(); Map.Entry<Long,
					 * List<FarmIcsConversion>> ficLastMap = (Map.Entry<Long,
					 * List<FarmIcsConversion>>) frmIcsConvMap .entrySet();
					 * List<FarmIcsConversion> ficList =
					 * frmIcsConMap.get(ficLastMap.getKey());
					 */
					for (Map.Entry<Long, List<FarmIcsConversion>> entry : frmIcsConvMap.entrySet()) {

						cell = row.createCell(col++);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(entry)
								? DateUtil.convertDateToString(entry.getValue().iterator().next().getInspectionDate(),
										getGeneralDateFormat())
								: ""));

						cell = row.createCell(col++);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(entry)
								&& !StringUtil.isEmpty(entry.getValue().iterator().next().getInspectorName())
										? entry.getValue().iterator().next().getInspectorName() : ""));

						cell = row.createCell(col++);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(entry)
								&& !StringUtil.isEmpty(entry.getValue().iterator().next().getInspectorMobile())
										? entry.getValue().iterator().next().getInspectorMobile() : ""));

						cell = row.createCell(col++);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(new HSSFRichTextString(""));
					}

				} else {
					cell = row.createCell(col++);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(new HSSFRichTextString(""));

					cell = row.createCell(col++);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(new HSSFRichTextString(""));

					cell = row.createCell(col++);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(new HSSFRichTextString(""));

					cell = row.createCell(col++);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(new HSSFRichTextString(""));
				}

				if (!ObjectUtil.isListEmpty(aflExport.getFarmICSConversion())) {
					frmIcsConMap = aflExport.getFarmICSConversion().stream()
							.filter(frmics -> (!ObjectUtil.isEmpty(frmics.getInspectionDate())
									&& DateUtil.getYearByDateTime(frmics.getInspectionDate()).equalsIgnoreCase(insYear)
									&& !StringUtil.isEmpty(frmics.getInsType())
									&& !frmics.getInsType().equalsIgnoreCase("0")))
							.collect(Collectors.groupingBy(FarmIcsConversion::getInsType));
				} else {
					frmIcsConMap = new HashMap<>();
				}
				for (Entry<Long, FarmCatalogue> cc : certiType.entrySet()) {
					cell = row.createCell(col++);
					cell.setCellStyle(cellStyle);
					if (frmIcsConMap.size() > 0
							&& !ObjectUtil
									.isEmpty(frmIcsConMap.entrySet().iterator().next().getValue().iterator().next())
							&& !StringUtil.isEmpty(frmIcsConMap.entrySet().iterator().next().getValue().iterator()
									.next().getScope())) {
						cell.setCellValue(new HSSFRichTextString(frmIcsConMap.entrySet().iterator().next().getValue()
								.iterator().next().getScope().equalsIgnoreCase(cc.getValue().getCode())
										? getLocaleProperty("icsStatus" + frmIcsConMap.entrySet().iterator().next()
												.getValue().iterator().next().getIcsType())
										: ""));
					} else {
						cell.setCellValue(new HSSFRichTextString(""));
					}
				}

			}
		}
		int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
				: request.getSession().getId();
		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("farmerList") + fileNameDateFormat.format(new Date()) + ".xls";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		wb.write(fileOut);
		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		fileOut.close();
		return stream;
	}

	public AFLExport getFilter3() {
		return filter3;
	}

	public void setFilter3(AFLExport filter3) {
		this.filter3 = filter3;
	}

	public Map<Long, FarmCatalogue> getFarmCatalougeMapValue(Integer type) {
		List<FarmCatalogue> farmCatalougeList = new ArrayList<>();
		List<FarmCatalogue> tempCatalogueList = new ArrayList<>();
		if (ObjectUtil.isListEmpty(farmCatalougeList)) {
			farmCatalougeList = catalogueService.listCatalogues();
		}
		tempCatalogueList = farmCatalougeList.stream().filter(fc -> fc.getTypez() == type).collect(Collectors.toList());
		Map<Long, FarmCatalogue> catList = new LinkedHashMap<>();
		Long i = 1l;
		for (FarmCatalogue fc : tempCatalogueList) {
			catList.put(i, fc);
			i++;
		}
		return catList;

	}

	public Map<Long, ProcurementProduct> getProcurmentProductMap(List<ProcurementProduct> cropList) {
		Map<Long, ProcurementProduct> cropMap = new LinkedHashMap<>();
		Long i = 1l;
		for (ProcurementProduct pp : cropList) {
			cropMap.put(i, pp);
			i++;
		}
		return cropMap;

	}

	public Map<String, CertificateCategory> getCertificationMap(List<CertificateCategory> certCat) {
		Map<String, CertificateCategory> certMap = new LinkedHashMap<>();
		certCat.stream().forEach(cer -> {
			certMap.put(cer.getCode(), cer);
		});
		return certMap;
	}

	public int subHead(HSSFCell cell, int sColNum, HSSFCellStyle filterStyle, int rowNum, HSSFSheet sheet,
			String[] cropFields, HSSFRow mainGridRowHead3) {
		for (String head : cropFields) {
			cell = mainGridRowHead3.createCell(sColNum);
			cell.setCellStyle(filterStyle);
			cell.setCellValue(new HSSFRichTextString(head.trim()));
			sheet.addMergedRegion(new CellRangeAddress(rowNum + 2, rowNum + 2, sColNum, sColNum));
			sColNum++;
		}
		return sColNum;
	}

	public String getInsYear() {
		return insYear;
	}

	public void setInsYear(String insYear) {
		this.insYear = insYear;
	}

	public Map<String, String> getInsYearList() {
		int limit = Integer.parseInt(DateUtil.getYearByDateTime(new Date())) - 2000;
		Map<String, String> yearMap = DateUtil.getYearsListByLimit(limit);
		return yearMap;
	}

	public Map<String, String> getOrganicStatusList() {

		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)) {
			organicStatuslist.put("3", getLocaleProperty("alrdyCertified"));
			organicStatuslist.put("0", getLocaleProperty("inprocess"));
		} else {
			organicStatuslist.put("3", getLocaleProperty("icsStatus3"));
			organicStatuslist.put("0", getLocaleProperty("inconversion"));
		}
		organicStatuslist.put("Conventional", getLocaleProperty("Conventional"));
		return organicStatuslist;
	}

	public String getOrganicStatus() {
		return organicStatus;
	}

	public void setOrganicStatus(String organicStatus) {
		this.organicStatus = organicStatus;
	}

	public String populateFarmerProfileExport() {
		String drinkingWater = "";
		String cropIns = "";
		String consumerElec = "";
		String vehicle = "";
		String govtDept = "";
		String cookingFuel = "";
		String homeDifficulty = "";
		String workDifficulty = "";
		String communityDifficulty = "";
		String agricultureImplements = "";

		// farm related
		boolean first = true;
		String ifs = "";
		String soilConservation = "";
		String waterConservation = "";
		String serviceCenters = "";
		String traningPrgm = "";

		enrollmentMap = formEnrollmentPlaceMap("et", enrollmentMap);
		isFarmerCertified = formMap("cer", isFarmerCertified);
		certificationTypes = formCertification("fct", certificationTypes);

		farmer = farmerService.findFarmerById(Long.valueOf(getId()));
		if (farmer.getImageInfo() != null) {
			setFarmerImageByteString(Base64Util.encoder(farmer.getImageInfo().getPhoto().getImage()));
		}

		if (cardService != null) {
			card = cardService.findCardByProfileId(farmer.getFarmerId());
		}

		interestCalcConsolidated = farmerService.findInterestCalcConsolidatedByfarmerProfileId(farmer.getFarmerId());

		ESESystem preferences = preferncesService.findPrefernceById("1");
		setDigitalSignatureEnabled(preferences.getPreferences().get(ESESystem.ENABLE_DIGITAL_SIGNATURE));
		DateFormat genDate = new SimpleDateFormat(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
		isInterestModule = preferences.getPreferences().get(ESESystem.INTEREST_MODULE);
		setFpoEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FPOFG));
		setFarmerCodeEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE));
		setFarmerBankInfoEnabled(preferences.getPreferences().get(ESESystem.ENABLE_BANK_INFO));
		setIdProofEnabled(preferences.getPreferences().get(ESESystem.ID_PROOF));
		setInsuranceInfoEnabled(preferences.getPreferences().get(ESESystem.ENABLE_INSURANCE_INFO));
		setGramPanchayatEnable(preferences.getPreferences().get(ESESystem.ENABLE_GRAMPANJAYAT));
		setFingerPrintEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FINGER_PRINT));

		if (!StringUtil.isEmpty(farmer.getBankInfo())) {
			for (BankInformation bank : farmer.getBankInfo()) {
				FarmCatalogue catalogue = getCatlogueValueByCode(bank.getAccType());
				if (!StringUtil.isEmpty(catalogue)) {
					bankAccType = catalogue.getName();
					bank.setAccType(bankAccType);
				}
			}

		}

		if (farmer == null) {
			addActionError(NO_RECORD);
			return REDIRECT;
		}
		if (!ObjectUtil.isEmpty(preferences)) {
			SimpleDateFormat sf = new SimpleDateFormat("MM-dd-yyyy");
			if (!ObjectUtil.isEmpty(farmer.getDateOfBirth()) && farmer.getDateOfBirth() != null) {
				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)) {

					dateOfBirth = sf.format(farmer.getDateOfBirth());
				} else
					dateOfBirth = genDate.format(farmer.getDateOfBirth());

			}
			if (!ObjectUtil.isEmpty(farmer.getLoanRepaymentDate()) && farmer.getLoanRepaymentDate() != null) {
				loanRepaymentDate = genDate.format(farmer.getLoanRepaymentDate());

			}
			if (!ObjectUtil.isEmpty(farmer.getDateOfJoining()) && farmer.getDateOfJoining() != null) {
				if (!StringUtil.isEmpty(preferences)) {
					if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)) {
						dateOfJoining = sf.format(farmer.getDateOfJoining());
					} else
						dateOfJoining = genDate.format(farmer.getDateOfJoining());
				}
			} else {
				if (farmer.getDateOfJoining() != null) {
					dateOfJoining = df.format(farmer.getDateOfJoining());

				}
			}
		}

		if (farmer.getIcsCode() != null) {
			icsCode = farmer.getIcsCode();
		}
		if (farmer.getIcsName() != null) {

			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)
					|| getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)) {
				FarmCatalogue catalogue = getCatlogueValueByCode(farmer.getIcsName());
				if (!StringUtil.isEmpty(catalogue)) {
					farmer.setIcsName(catalogue.getName());
				}
			} else
				icsName = farmer.getIcsName();

		}

		if (accountService != null) {

			ESEAccount acc = accountService.findAccountByProfileIdAndProfileType(farmer.getFarmerId(),
					ESEAccount.CONTRACT_ACCOUNT);
			if (!ObjectUtil.isEmpty(acc) || acc != null) {
				setAccBalance(String.valueOf(acc.getCashBalance()));
			}

		}

		if (farmer.getImageInfo() != null && farmer.getImageInfo().getPhoto() != null
				&& farmer.getImageInfo().getPhoto().getImage() != null
				&& farmer.getImageInfo().getPhoto().getImage().length > 0) {
			setFarmerImageByteString(Base64Util.encoder(farmer.getImageInfo().getPhoto().getImage()));
		}
		if (farmer.getIdProofImg() != null && farmer.getIdProofImg().length > 0) {
			setIdProofImgString(Base64Util.encoder(farmer.getIdProofImg()));
			setIdImgAvil("1");
		}

		if (!ObjectUtil.isEmpty(farmer.getFingerPrint())) {
			setFingerPrintImageByteString(Base64Util.encoder(farmer.getFingerPrint()));
		}

		if (farmer.getStatus() == FarmerAction.FAMRMER_ACTIVE) {
			Long seasonId = farmerService.findSeasonBySeasonCode(getCurrentSeasonCode());
			farmerAndContractStatus = farmerService.findFarmerCurrentContractStatusById(Long.valueOf(id), seasonId);
		}
		if (!ObjectUtil.isEmpty(farmer.getFarmerEconomy())) {
			farmerEconomy = farmer.getFarmerEconomy();
			if (!StringUtil.isEmpty(farmerEconomy.getAnnualIncome())) {
				if (farmerEconomy.getAnnualIncome().contains(".")) {
					rupee = farmerEconomy.getAnnualIncome().split("\\.")[0];
					paise = farmerEconomy.getAnnualIncome().split("\\.")[1];
				} else {
					rupee = farmerEconomy.getAnnualIncome();
				}
			}
			if (!StringUtil.isEmpty(farmerEconomy.getDrinkingWaterSource())) {

				String drinkingArr[] = farmerEconomy.getDrinkingWaterSource().split(",");
				for (int i = 0; i < drinkingArr.length; i++) {
					String drinkingTrim = drinkingArr[i].replaceAll("\\s+", "");
					if (!StringUtil.isEmpty(drinkingTrim)) {
						if (drinkingTrim.equalsIgnoreCase("99")) {
							drinkingWater += farmerEconomy.getDrinkingWaterSourceOther() + ",";

						} else {

							drinkingWater += getText("drinkingWS" + drinkingTrim) + ", ";
						}
					} else {
						drinkingWater = getText("drinkingWS" + drinkingTrim) + ",";
					}

				}
				drinkingWater = drinkingWater.substring(0, drinkingWater.length() - 1);

			}

		} else {
			farmerEconomy = new FarmerEconomy();
			farmerEconomy.setFarmer(farmer);
			farmerEconomy.setHousingOwnership(-1);
			farmerEconomy.setHousingType("-1");
			rupee = "";
			paise = "";
		}

		if (!StringUtil.isEmpty(farmer.getMaritalSatus()) && farmer.getMaritalSatus() != "-1") {
			if (farmer.getMaritalSatus() != null && farmer.getMaritalSatus().length() > 1) {
				FarmCatalogue catalogue = getCatlogueValueByCode(String.valueOf(farmer.getMaritalSatus()));
				if (!ObjectUtil.isEmpty(catalogue)) {
					String name = catalogue != null ? catalogue.getName() : "";
					farmer.setMaritalSatus(name);
				} else {
					farmer.setMaritalSatus("");
				}
			}

		}

		if (!StringUtil.isEmpty(farmer.getCategory()) && farmer.getCategory() != "-1") {
			if (farmer.getCategory() != null && farmer.getCategory().length() > 1) {
				FarmCatalogue catalogue = getCatlogueValueByCode(String.valueOf(farmer.getCategory()));
				if (!ObjectUtil.isEmpty(catalogue)) {
					String name = catalogue != null ? catalogue.getName() : "";
					farmer.setCategory(name);
				} else {
					farmer.setCategory("");
				}

			}
		}

		if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
			if (!StringUtil.isEmpty(farmer.getIcsName())) {
				FarmCatalogue catalogue = getCatlogueValueByCode(farmer.getIcsName());
				if (!ObjectUtil.isEmpty(catalogue)) {
					icsString = catalogue.getName();
				}
			} else {
				icsString = "";
			}

			if (!StringUtil.isEmpty(farmer.getIcsUnitNo())) {
				FarmCatalogue catalogue = getCatlogueValueByCode(farmer.getIcsUnitNo());
				if (!ObjectUtil.isEmpty(catalogue)) {
					icsUnitNoString = catalogue.getName();
				}

			} else {
				icsUnitNoString = "";
			}

			if (!StringUtil.isEmpty(farmer.getIcsTracenetRegNo())) {
				FarmCatalogue catalogue = getCatlogueValueByCode(farmer.getIcsTracenetRegNo());
				if (!ObjectUtil.isEmpty(catalogue)) {
					icsRegNoString = catalogue.getName();
				}
			} else {
				icsRegNoString = "";
			}

		} else {
			icsString = farmer.getIcsName();
			icsUnitNoString = farmer.getIcsUnitNo();
			icsRegNoString = farmer.getIcsTracenetRegNo();
		}

		if (!StringUtil.isEmpty(farmer.getFarmerCropInsurance())) {

			String cropInsArr[] = farmer.getFarmerCropInsurance().split(",");
			for (int i = 0; i < cropInsArr.length; i++) {
				String cropInsTrim = cropInsArr[i].replaceAll("\\s+", "");
				if (!StringUtil.isEmpty(cropIns)) {

					ProcurementProduct crop = productDistributionService.findProcurementProductByCode(cropInsTrim);
					if (!ObjectUtil.isEmpty(crop)) {
						cropIns += crop.getName() + ",";

					}

				} else {
					ProcurementProduct crop = productDistributionService.findProcurementProductByCode(cropInsTrim);
					if (!ObjectUtil.isEmpty(crop)) {
						cropIns = crop.getName() + ",";
					}

				}

			}

		}
		if (cropIns != null && !StringUtil.isEmpty(cropIns)) {
			cropIns = cropIns.substring(0, cropIns.length() - 1);
			cropIns = StringUtil.removeLastComma(cropIns);
			farmer.setFarmerCropInsurance(cropIns);
		}

		if (!StringUtil.isEmpty(farmer.getConsumerElectronics())) {
			String electronicsArr[] = farmer.getConsumerElectronics().split("\\,");
			for (int i = 0; i < electronicsArr.length; i++) {
				String electronicsTrim = electronicsArr[i].replaceAll("\\s+", "");
				if (!StringUtil.isEmpty(consumerElec)) {
					if (electronicsTrim.equalsIgnoreCase("99")) {
						consumerElec += farmer.getConsumerElectronicsOther() + ",";
					} else {
						FarmCatalogue catalogue = getCatlogueValueByCode(electronicsTrim);
						if (!ObjectUtil.isEmpty(catalogue)) {
							consumerElec += catalogue.getName() + ",";
						}
					}
				} else {
					FarmCatalogue catalogue = getCatlogueValueByCode(electronicsTrim);
					if (!ObjectUtil.isEmpty(catalogue)) {
						consumerElec += catalogue.getName() + ",";
					}
				}
			}
		}
		if (consumerElec != null) {
			consumerElec = consumerElec.substring(0, consumerElec.length());
			consumerElec = StringUtil.removeLastComma(consumerElec);
			farmer.setConsumerElectronics(consumerElec);
		}

		if (!StringUtil.isEmpty(farmer.getVehicle())) {

			String vehicleArr[] = farmer.getVehicle().split("\\,");
			for (int i = 0; i < vehicleArr.length; i++) {
				String vehicleTrim = vehicleArr[i].replaceAll("\\s+", "");
				if (!StringUtil.isEmpty(vehicle)) {
					if (vehicleTrim.equalsIgnoreCase("99")) {
						vehicle += farmer.getVehicleOther() + ",";

					} else {
						FarmCatalogue catalogue = getCatlogueValueByCode(vehicleTrim);
						if (!ObjectUtil.isEmpty(catalogue)) {
							vehicle += catalogue.getName() + ",";
						}

					}
				} else {
					FarmCatalogue catalogue = getCatlogueValueByCode(vehicleTrim);
					if (!ObjectUtil.isEmpty(catalogue)) {
						vehicle += catalogue.getName() + ",";
					}

				}

			}
		}

		if (vehicle != null) {
			vehicle = vehicle.substring(0, vehicle.length());
			vehicle = StringUtil.removeLastComma(vehicle);
			farmer.setVehicle(vehicle);
		}
		if (drinkingWater != null && (!drinkingWater.equals(""))) { // second
																	// condition
																	// to
																	// avoid
																	// array
																	// -1
																	// index
																	// exception.
			drinkingWater = drinkingWater.substring(0, drinkingWater.length() - 1);
			farmerEconomy.setDrinkingWaterSource(drinkingWater);
		}

		if (farmer.getLifeInsurance() != null && !StringUtil.isEmpty(farmer.getLifeInsurance())) {
			if (farmer.getLifeInsurance().equalsIgnoreCase("1")) {
				farmer.setLifeInsurance(getText("insure1"));
			} else {
				farmer.setLifeInsurance(getText("insure0"));
			}
		}
		if (farmer.getHealthInsurance() != null && !StringUtil.isEmpty(farmer.getHealthInsurance())) {

			if (getCurrentTenantId().equalsIgnoreCase("efk")) {
				if (farmer.getHealthInsurance().equalsIgnoreCase("NHIF")) {
					farmer.setHealthInsurance("NHIF");
				} else if (farmer.getHealthInsurance().equalsIgnoreCase("Other")) {
					farmer.setHealthInsurance("Other");
				}
			} else {
				if (farmer.getHealthInsurance().equalsIgnoreCase("1")) {
					farmer.setHealthInsurance(getText("insure1"));
				} else {
					farmer.setHealthInsurance(getText("insure0"));
				}
			}

		}

		if (!StringUtil.isEmpty(farmer.getGovtDept())) {

			String govtDeptArr[] = farmer.getGovtDept().split(",");
			for (int i = 0; i < govtDeptArr.length; i++) {
				String govtDeptTrim = govtDeptArr[i].replaceAll("\\s+", "");
				if (!StringUtil.isEmpty(govtDeptTrim)) {
					FarmCatalogue catalogue = getCatlogueValueByCode(govtDeptTrim);
					if (!ObjectUtil.isEmpty(catalogue)) {
						govtDept += catalogue.getName() + ",";
					}

				}
			}
			if (govtDept.endsWith(",")) {
				govtDept = govtDept.substring(0, govtDept.length() - 1);
			}
			farmer.setGovtDept(govtDept);
		}

		if (!StringUtil.isEmpty(farmerEconomy.getCookingFuel())) {

			String cookingFuelArr[] = farmerEconomy.getCookingFuel().split(",");
			for (int i = 0; i < cookingFuelArr.length; i++) {
				String cookingFuelTrim = cookingFuelArr[i].replaceAll("\\s+", "");
				if (!StringUtil.isEmpty(cookingFuelTrim)) {
					if (cookingFuelTrim.equalsIgnoreCase("99")) {
						cookingFuel += farmerEconomy.getCookingFuelSourceOther() + ",";
					} else {
						FarmCatalogue catalogue = getCatlogueValueByCode(cookingFuelTrim);
						if (!ObjectUtil.isEmpty(catalogue)) {
							cookingFuel += catalogue.getName() + ",";
						}

					}
				} else {
					FarmCatalogue catalogue = getCatlogueValueByCode(cookingFuelTrim);
					if (!ObjectUtil.isEmpty(catalogue)) {
						cookingFuel += catalogue.getName() + ",";
					}
				}

			}

			if (cookingFuel != null && (!cookingFuel.equals(""))) {
				cookingFuel = cookingFuel.substring(0, cookingFuel.length());
				cookingFuel = StringUtil.removeLastComma(cookingFuel);
				farmerEconomy.setCookingFuel(cookingFuel);
			}
		}

		if (!StringUtil.isEmpty(farmer.getHomeDifficulty())) {

			String homeDifficultyArr[] = farmer.getHomeDifficulty().split(",");
			for (int i = 0; i < homeDifficultyArr.length; i++) {
				String homeDifficultyTrim = homeDifficultyArr[i].replaceAll("\\s+", "");
				if (!StringUtil.isEmpty(homeDifficultyTrim)) {
					FarmCatalogue catalogue = getCatlogueValueByCode(homeDifficultyTrim);
					if (!ObjectUtil.isEmpty(catalogue)) {
						homeDifficulty += catalogue.getName() + ",";
					}

				}

			}

			if (homeDifficulty != null && (!homeDifficulty.equals(""))) {

				homeDifficulty = homeDifficulty.substring(0, homeDifficulty.length());
				homeDifficulty = StringUtil.removeLastComma(homeDifficulty);
				farmer.setHomeDifficulty(homeDifficulty);
			}
		}

		if (!StringUtil.isEmpty(farmer.getWorkDiffficulty())) {

			String workDifficultyArr[] = farmer.getWorkDiffficulty().split(",");
			for (int i = 0; i < workDifficultyArr.length; i++) {
				String workDifficultyTrim = workDifficultyArr[i].replaceAll("\\s+", "");
				if (!StringUtil.isEmpty(workDifficultyTrim)) {
					FarmCatalogue catalogue = getCatlogueValueByCode(workDifficultyTrim);
					if (!ObjectUtil.isEmpty(catalogue)) {
						workDifficulty += catalogue.getName() + ",";
					}

				}

			}

			if (workDifficulty != null && (!workDifficulty.equals(""))) {

				workDifficulty = workDifficulty.substring(0, workDifficulty.length());
				workDifficulty = StringUtil.removeLastComma(workDifficulty);
				farmer.setWorkDiffficulty(workDifficulty);
			}
		}

		if (!StringUtil.isEmpty(farmer.getCommunitiyDifficulty())) {

			String communityDifficultyArr[] = farmer.getCommunitiyDifficulty().split(",");
			for (int i = 0; i < communityDifficultyArr.length; i++) {
				String communityDifficultyTrim = communityDifficultyArr[i].replaceAll("\\s+", "");
				if (!StringUtil.isEmpty(communityDifficultyTrim)) {
					FarmCatalogue catalogue = getCatlogueValueByCode(communityDifficultyTrim);
					if (!ObjectUtil.isEmpty(catalogue)) {
						communityDifficulty += catalogue.getName() + ",";
					}

				}

			}

			if (communityDifficulty != null && (!communityDifficulty.equals(""))) {

				communityDifficulty = communityDifficulty.substring(0, communityDifficulty.length());
				communityDifficulty = StringUtil.removeLastComma(communityDifficulty);
				farmer.setCommunitiyDifficulty(communityDifficulty);
			}
		}

		if (farmer.getLoanPupose() != null && !StringUtil.isEmpty(farmer.getLoanPupose())) {
			FarmCatalogue catalogueLoan = getCatlogueValueByCode(farmer.getLoanPupose());
			if (!ObjectUtil.isEmpty(catalogueLoan)) {
				if (farmer.getLoanPupose().equalsIgnoreCase(String.valueOf(FarmCatalogue.OTHER))) {
					String catName = catalogueLoan == null ? getText("other") : catalogueLoan.getName();

					farmer.setLoanPupose(catName + " - " + farmer.getLoanPuposeOther());
				} else {
					String catName = catalogueLoan == null ? "" : catalogueLoan.getName();
					farmer.setLoanPupose(catName);
				}
				if (!StringUtil.isEmpty(farmer.getLoanPupose())
						&& farmer.getLoanPupose().trim().charAt(farmer.getLoanPupose().trim().length() - 1) == '-') {
					farmer.setLoanPupose(farmer.getLoanPupose()
							.replace(farmer.getLoanPupose().substring(farmer.getLoanPupose().length() - 1), ""));
				}
			}
		}
		if (farmer.getLoanSecurity() != null && !StringUtil.isEmpty(farmer.getLoanSecurity())) {
			FarmCatalogue catalogue1 = getCatlogueValueByCode(farmer.getLoanSecurity());
			if (farmer.getLoanSecurity().equalsIgnoreCase(String.valueOf(FarmCatalogue.OTHER))) {
				String catName = catalogue1 == null ? getText("other") : catalogue1.getName();
				farmer.setLoanSecurity(catName + "-" + farmer.getLoanSecurityOther());
			} else {
				String catName = catalogue1 == null ? "" : catalogue1.getName();
				farmer.setLoanSecurity(catName);
			}
			if (!farmer.getLoanSecurity().isEmpty()
					&& farmer.getLoanSecurity().trim().charAt(farmer.getLoanSecurity().trim().length() - 1) == '-') {
				farmer.setLoanSecurity(farmer.getLoanSecurity()
						.replace(farmer.getLoanSecurity().substring(farmer.getLoanSecurity().length() - 1), ""));
			}

		}
		if (!StringUtil.isEmpty(farmer.getLoanIntPeriod()) || farmer.getLoanIntPeriod() != null) {
			farmer.setLoanIntPeriod(getText("interestPeriod" + farmer.getLoanIntPeriod()));
		}

		farmer.setLoanRepaymentAmount(farmer.getLoanRepaymentAmount());
		farmer.setIdProof(farmer.getIdProof());
		farmer.setProofNo(farmer.getProofNo());
		farmer.setOtherIdProof(farmer.getOtherIdProof());
		farmer.setReligion(farmer.getReligion());
		FarmCatalogue catalogue = getCatlogueValueByCode(farmer.getPositionGroup());
		if (!ObjectUtil.isEmpty(catalogue))
			farmer.setPositionGroup(catalogue.getName());

		if (farmer.getInvestigatorDate() != null) {
			investigatorDate = genDate.format(farmer.getInvestigatorDate());
		}

		farmer.setPlaceOfAsss(!StringUtil.isEmpty(farmer.getPlaceOfAsss())
				? getCatlogueValueByCode(farmer.getPlaceOfAsss()).getName() : "");

		farmer.setPrefWrk(
				!StringUtil.isEmpty(farmer.getPrefWrk()) ? getCatlogueValueByCode(farmer.getPrefWrk()).getName() : "");

		if (!StringUtil.isEmpty(farmer.getAgricultureImplements())) {
			String implementsArr[] = farmer.getAgricultureImplements().split("\\,");
			for (int i = 0; i < implementsArr.length; i++) {
				String implementsTrim = implementsArr[i].replaceAll("\\s+", "");
				if (!StringUtil.isEmpty(implementsTrim)) {
					FarmCatalogue catalogu = getCatlogueValueByCode(implementsTrim);
					if (!ObjectUtil.isEmpty(catalogu)) {
						agricultureImplements += catalogu.getName() + ",";
					}
				}
			}
		}
		if (agricultureImplements != null && !StringUtil.isEmpty(agricultureImplements)) {
			agricultureImplements = agricultureImplements.substring(0, agricultureImplements.length());
			agricultureImplements = StringUtil.removeLastComma(agricultureImplements);
			farmer.setAgricultureImplements(agricultureImplements);
		}
		if (getCurrentTenantId().equalsIgnoreCase("wilmar")) {
			if (!StringUtil.isEmpty(farmer.getMasterData())) {
				MasterData masterData = clientService.findMasterDataByCode(farmer.getMasterData());
				farmer.setMasterData(masterData.getName());
			} else {
				farmer.setMasterData("");
			}
		}
		Integer adultCountMale = !StringUtil.isEmpty(farmer.getAdultCountMale())
				? Integer.parseInt(farmer.getAdultCountMale()) : 0;
		Integer adultCountFemale = !StringUtil.isEmpty(farmer.getAdultCountFemale())
				? Integer.parseInt(farmer.getAdultCountFemale()) : 0;
		Integer childCountMale = !StringUtil.isEmpty(farmer.getChildCountMale())
				? Integer.parseInt(farmer.getChildCountMale()) : 0;
		Integer childCountFemale = !StringUtil.isEmpty(farmer.getChildCountFemale())
				? Integer.parseInt(farmer.getChildCountFemale()) : 0;

		if ((getCurrentTenantId().equalsIgnoreCase("efk"))) {
			farmer.setTotalHsldLabel(String.valueOf(farmer.getTotalHsldLabel()));
		} else {
			farmer.setTotalHsldLabel(
					String.valueOf(adultCountMale + adultCountFemale + childCountMale + childCountFemale));
		}

		// farm start

		farm = farmerService.findFarmByFarmerId(Long.valueOf(farmer.getId()));
		if (farm != null) {

			try {
				String width = "800";
				String height = "350";
				String zoom = "6";
				if (!StringUtil.isEmpty(farm.getLatitude()) && !StringUtil.isEmpty(farm.getLongitude())) {
					staticMapUrl = UrlSigner.getURL(farm.getLatitude(), farm.getLongitude(), width, height, zoom);
					System.out.println(farm.getLatitude().trim() + "," + farm.getLongitude() + "," + width + ","
							+ height + "," + zoom);
					System.out.println("<---------------------------------------------->");
					System.out.println(staticMapUrl);
					System.out.println("<---------------------------------------------->");
				}
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			setSangham(farm.getFarmer().getSangham());
			if (!ObjectUtil.isListEmpty(farm.getFarmICS())) {
				farmICSs = new ArrayList<FarmICS>(farm.getFarmICS());

				farmICSs.sort((FarmICS o1, FarmICS o2) -> o1.getIcsType() - o2.getIcsType());

			}
			setFarmerId(farm.getFarmer().getFarmerId());
			setFarmerUniqueId(String.valueOf(farm.getFarmer().getId()));
			if (!ObjectUtil.isEmpty(farm.getFarmer())) {

				if (farm.getFarmer().getIsCertifiedFarmer() != Farmer.CERTIFIED_NO)
					isCertifiedFarmer = true;

				FarmIcsConversion farmIcs = farmerService.findFarmIcsConversionByFarmId(farm.getId());
				if (!ObjectUtil.isEmpty(farmer.getIsCertifiedFarmer()) && !ObjectUtil.isEmpty(farmIcs)) {
					if (farmer.getIsCertifiedFarmer() == 1) {
						if (!StringUtil.isEmpty(farmIcs.getOrganicStatus())
								&& !farmIcs.getOrganicStatus().equalsIgnoreCase("3")) {
							farm.setOrganicStatus(getLocaleProperty("inconversion"));
						} else {
							farm.setOrganicStatus(getLocaleProperty("alrdyCertified"));
						}

					}
				} else {
					farm.setOrganicStatus(getLocaleProperty("conventional"));
				}

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

				}

			}
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.BLRI_TENANT_ID)) {
				housingInfo = farmerService.findByHousingInfo(farm.getId());
				if (!ObjectUtil.isEmpty(housingInfo) && !StringUtil.isEmpty(housingInfo.getHousingShadType())) {
					FarmCatalogue cat = getCatlogueValueByCode(housingInfo.getHousingShadType());

					housingInfo.setHousingShadType(cat.getName());
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
			if (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo())) {

				if (!StringUtil.isEmpty(farm.getFarmDetailedInfo().getTotalLandHolding())) {
					farm.getFarmDetailedInfo().setTotalLandHolding(CurrencyUtil.getDecimalFormat(
							Double.valueOf(farm.getFarmDetailedInfo().getTotalLandHolding()), "##.00"));
				} else {
					farm.getFarmDetailedInfo().setTotalLandHolding("0.00");
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
				}

				if (!StringUtil.isEmpty(farm.getFarmDetailedInfo().getSoilType())
						&& farm.getFarmDetailedInfo().getSoilType().contains(",")) {

					String soilTypArray[] = farm.getFarmDetailedInfo().getSoilType().split(",");

					for (int i = 0; i < soilTypArray.length; i++) {
						String soilTypTrim = soilTypArray[i].replaceAll("\\s+", "");
						if (!StringUtil.isEmpty(soilTypeDetail)) {
							soilTypeDetail += getSoilTypeList().get(soilTypTrim) + ",";
						} else {
							soilTypeDetail = getSoilTypeList().get(soilTypTrim) + ",";
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

				if (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo())
						&& (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo().getLandGradient()))
						&& farm.getFarmDetailedInfo().getLandGradient().contains(",")) {

					String landGradiArray[] = farm.getFarmDetailedInfo().getLandGradient().split(",");

					for (int i = 0; i < landGradiArray.length; i++) {
						String landGradiTrim = landGradiArray[i].replaceAll("\\s+", "");
						if (!StringUtil.isEmpty(landGradientDetail)) {
							landGradientDetail += getLandGradientList().get(Integer.valueOf(landGradiTrim)) + ",";
						} else {
							landGradientDetail = getLandGradientList().get(Integer.valueOf(landGradiTrim)) + ",";
						}
					}

				} else {
					landGradientDetail = ((StringUtil.isEmpty(farm.getFarmDetailedInfo().getLandGradient()))
							|| farm.getFarmDetailedInfo().getLandGradient() == SELECT_MULTI) ? ""
									: getLandGradientList()
											.get(Integer.valueOf(farm.getFarmDetailedInfo().getLandGradient()));
				}

				if (!StringUtil.isEmpty(landGradientDetail)) {
					if (landGradientDetail.contains(",")) {
						landGradientDetail = StringUtil.isEmpty(landGradientDetail) ? ""
								: landGradientDetail.substring(0, landGradientDetail.length() - 1);
					}
				}

				if (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo())
						&& (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo().getApproachRoad()))
						&& farm.getFarmDetailedInfo().getApproachRoad().contains(",")) {
					String approachRoadArray[] = farm.getFarmDetailedInfo().getApproachRoad().split(",");

					for (int i = 0; i < approachRoadArray.length; i++) {
						String approachRoadTrim = approachRoadArray[i].replaceAll("\\s+", "");
						if (!StringUtil.isEmpty(selectedRoadString)) {
							selectedRoadString += getApproadList().get(Integer.valueOf(approachRoadTrim)) + ",";
						} else {
							selectedRoadString = getApproadList().get(Integer.valueOf(approachRoadTrim)) + ",";
						}
					}

				} else {
					selectedRoadString = ((StringUtil.isEmpty(farm.getFarmDetailedInfo().getApproachRoad()))
							|| farm.getFarmDetailedInfo().getApproachRoad() == SELECT_MULTI) ? ""
									: getApproadList()
											.get(Integer.valueOf(farm.getFarmDetailedInfo().getApproachRoad()));

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

			if (farm.getFarmDetailedInfo().getInputSource() != "-1"
					&& farm.getFarmDetailedInfo().getInputSource() != null) {
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
					ESESystem preferences_farm = preferncesService.findPrefernceById("1");
					if (!ObjectUtil.isEmpty(preferences_farm)) {

						DateFormat genDate_farm = new SimpleDateFormat(
								preferences_farm.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
						if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)) {
							SimpleDateFormat sf = new SimpleDateFormat("MM-dd-yyyy");
							icd.setInspectionDateString(StringUtil.isEmpty(icd.getInspectionDate()) ? " "
									: sf.format(icd.getInspectionDate()));
						} else
							icd.setInspectionDateString(StringUtil.isEmpty(icd.getInspectionDate()) ? " "
									: genDate_farm.format(icd.getInspectionDate()));
					}

					scopeName = !StringUtil.isEmpty(icd.getScope())
							? catalogueService.findCatalogueByCode(icd.getScope()).getName() : "";

					farm.setFarmIcsConv(icd);
				}
			} else {
				FarmIcsConversion fc = new FarmIcsConversion();
				Set<FarmIcsConversion> temp = new LinkedHashSet<>();
				temp.add(fc);
				farm.setFarmICSConversion(temp);
			}
			ESESystem preferences_farm = preferncesService.findPrefernceById("1");
			if (!ObjectUtil.isEmpty(preferences_farm)) {
				DateFormat genDate_farm = new SimpleDateFormat(
						preferences_farm.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)) {
					SimpleDateFormat sf = new SimpleDateFormat("MM-dd-yyyy");
					farm.getFarmDetailedInfo().setLastDateOfChemicalString(
							StringUtil.isEmpty(farm.getFarmDetailedInfo().getLastDateOfChemical()) ? " "
									: sf.format(farm.getFarmDetailedInfo().getLastDateOfChemical()));
				} else
					farm.getFarmDetailedInfo().setLastDateOfChemicalString(
							StringUtil.isEmpty(farm.getFarmDetailedInfo().getLastDateOfChemical()) ? " "
									: genDate_farm.format(farm.getFarmDetailedInfo().getLastDateOfChemical()));
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
					FarmCatalogue catalogue_farm = catalogueService.findCatalogueByCode(catCode.trim());
					if (!ObjectUtil.isEmpty(catalogue_farm))
						waterHarvests += catalogue_farm.getName() + ",";
				}
				waterHarvests = waterHarvests.substring(0, waterHarvests.length() - 1);
			}

			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)) {
				if (!StringUtil.isEmpty(farm.getPresenceBananaTrees())) {
					presenceOfBanana = bananaTreesList.get(Integer.parseInt(farm.getPresenceBananaTrees()));
				}
				if (!StringUtil.isEmpty(farm.getParallelProd())) {
					parallelProduction = parallelProductionList.get(Integer.parseInt(farm.getParallelProd()));
				}
				if (!StringUtil.isEmpty(farm.getPresenceHiredLabour())) {
					hiredLabours = hiredLabourList.get(Integer.parseInt(farm.getPresenceHiredLabour()));
				}
				if (!StringUtil.isEmpty(farm.getRiskCategory())) {
					riskCategory = riskCategoryList.get(Integer.parseInt(farm.getRiskCategory()));
				}

				if (farm.getTreeDetails().size() > 0) {

					for (TreeDetail val : farm.getTreeDetails()) {
						val.setProdStatus(getText("productStatus-" + val.getProdStatus()));
						val.setYears(getCatlogueValueByCode(val.getYears()).getName());
						ProcurementVariety variety = productDistributionService
								.findProcurementVariertyByCode(val.getVariety());
						val.setVariety(!ObjectUtil.isEmpty(variety) ? variety.getName() : "");
					}
				}
				Double totOrgTrees = 0.0;
				Double totConvTrees = 0.0;
				Double totAvocadoTrees = 0.0;
				Double hectarOrgTrees = 0.0;
				Double hectarConvTrees = 0.0;
				Double hectarAvocadoTrees = 0.0;

				List<Object[]> treeDetailsList = productDistributionService.listTreeDetails(farm.getId());
				treeDetails = new ArrayList();
				if (!ObjectUtil.isListEmpty(treeDetailsList)) {

					for (Object[] obj : treeDetailsList) {
						String varietyName = null;
						TreeDetail detail = new TreeDetail();
						if (String.valueOf(obj[2]).equalsIgnoreCase("1")) {
							totOrgTrees += Double.valueOf(String.valueOf(obj[0]));
							detail.setNoOfTrees(String.valueOf(obj[0]));
							varietyName = getLocaleProperty("totalOrgVarty") + " " + String.valueOf(obj[1]);
							detail.setVariety(varietyName);
						} else if (String.valueOf(obj[2]).equalsIgnoreCase("2")) {
							totConvTrees += Double.valueOf(String.valueOf(obj[0]));
							detail.setNoOfTrees(String.valueOf(obj[0]));
							varietyName = getLocaleProperty("totalConvnVarty") + " " + String.valueOf(obj[1]);
							detail.setVariety(varietyName);
						}
						treeDetails.add(detail);
					}
				}
				totAvocadoTrees = totOrgTrees + totConvTrees;
				hectarOrgTrees = totOrgTrees * 7 * 7 / 10000;
				hectarConvTrees = totConvTrees * 7 * 7 / 10000;
				hectarAvocadoTrees = totAvocadoTrees * 7 * 7 / 10000;
				farm.setTotalOrganicTrees(totOrgTrees);
				farm.setTotalConventionalTrees(totConvTrees);
				farm.setTotalAvocadoTrees(totAvocadoTrees);
				farm.setHectarOrganicTrees(hectarOrgTrees);
				farm.setHectarConventionalTrees(hectarConvTrees);
				farm.setHectarAvocadoTrees(hectarAvocadoTrees);

			}
			if (farm != null) {

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

				}
				if (farm.getActiveCoordinates() != null && farm.getActiveCoordinates().getFarmCoordinates() != null
						&& !ObjectUtil.isEmpty(farm.getActiveCoordinates())
						&& !ObjectUtil.isListEmpty(farm.getActiveCoordinates().getFarmCoordinates())) {
					// if (farm.getActiveCoordinates().getFarmCoordinates() !=
					// null &&
					// !ObjectUtil.isListEmpty(farm.getActiveCoordinates().getFarmCoordinates()))
					// {
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

		}

		// Dynamic ics start

		if (farm != null && Long.valueOf(farm.getId()) != null) {

			farmerDynamicData = farmerService.findFarmerDynamicDataByReferenceId(farm.getId());

			if (farmerDynamicData != null) {

				if (farmerDynamicData.getDymamicImageData() != null
						&& farmerDynamicData.getDymamicImageData().size() > 0) {
					farmerDynamicData.setDigSignByteString(
							Base64Util.encoder(farmerDynamicData.getDymamicImageData().iterator().next().getImage()));
				}
				ics_dynamic_data_id = String.valueOf(farmerDynamicData.getId());
				ics_farm_id = String.valueOf(farm.getId());
				ics_entityType = farmerDynamicData.getEntityId();

				seasonType = farmerDynamicData.getIsSeason();
				// entityType = farmerDynamicData.getEntityId();
				farmerDynamicData.setTxnDate(!StringUtil.isEmpty(farmerDynamicData.getDate())
						? DateUtil.convertDateToString(farmerDynamicData.getDate(), DateUtil.DATE_TIME_FORMAT) : "");
				if (!ObjectUtil.isEmpty(farmerDynamicData) && farmerDynamicData.getEntityId().equalsIgnoreCase("4")) {

					if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
						Farm farm = farmerService.findFarmById(Long.valueOf(farmerDynamicData.getReferenceId()));
						setFarmer_ics(String.valueOf(farm.getFarmer().getFirstName()));
						setFarmList_ics(String.valueOf(farm.getFarmName()));
						setGroup(String.valueOf(farm.getFarmer().getSamithi().getName()));
						setSelectedVillage(farm.getFarmer().getVillage().getName());
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

						setScope(getCatlogueValueByCode(farmerDynamicData.getFarmIcs().getScope()).getName());
						setTotLand(farmerDynamicData.getFarmIcs().getTotalLand());
						setOrgLand(farmerDynamicData.getFarmIcs().getOrganicLand());
						setTotSite(farmerDynamicData.getFarmIcs().getTotalSite());
						setSeason(farmerDynamicData.getFarmIcs().getSeason());
					}

				} else if (!ObjectUtil.isEmpty(farmerDynamicData)
						&& farmerDynamicData.getEntityId().equalsIgnoreCase("2")) {
					if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
						Farm farm = farmerService.findFarmById(Long.valueOf(farmerDynamicData.getReferenceId()));
						setFarmer_ics(String.valueOf(farm.getFarmer().getFirstName()));
						setFarmList_ics(String.valueOf(farm.getFarmName()));
						setSelectedVillage(String.valueOf(farm.getFarmer().getVillage().getName()));
					}
				} else if (!ObjectUtil.isEmpty(farmerDynamicData)
						&& farmerDynamicData.getEntityId().equalsIgnoreCase("1")) {
					if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
						Farmer frmr = farmerService.findFarmerById(Long.valueOf(farmerDynamicData.getReferenceId()));
						setFarmer_ics(String.valueOf(frmr.getFirstName()));
						setSelectedVillage(String.valueOf(frmr.getVillage().getName()));
					}
				} else if (!ObjectUtil.isEmpty(farmerDynamicData)
						&& farmerDynamicData.getEntityId().equalsIgnoreCase("5")) {
					if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())
							&& farmerDynamicData.getReferenceId().contains(",")) {
						List<Farmer> frmr = farmerService
								.listFarmerByIds(Arrays.asList(farmerDynamicData.getReferenceId().split(",")));
						setFarmer_ics(String.valueOf(frmr.stream().filter(u -> u.getFirstName() != null)
								.map(p -> String.valueOf(
										p.getFirstName() + (p.getLastName() != null ? ("-" + p.getLastName()) : "")))
								.collect(Collectors.joining(","))));
						setSelectedVillage(String.valueOf(frmr.stream().filter(u -> u.getFirstName() != null)
								.map(p -> String.valueOf(p.getVillage().getName())).distinct()
								.collect(Collectors.joining(","))));
					} else if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
						Farmer frmr = farmerService.findFarmerById(Long.valueOf(farmerDynamicData.getReferenceId()));
						setFarmer_ics(String.valueOf(frmr.getFirstName()));
						setSelectedVillage(String.valueOf(frmr.getVillage().getName()));
					}
				} else if (!ObjectUtil.isEmpty(farmerDynamicData)
						&& farmerDynamicData.getEntityId().equalsIgnoreCase("3")) {
					if (!StringUtil.isEmail(farmerDynamicData.getReferenceId())) {
						Warehouse sam = locationService
								.findSamithiById(Long.valueOf(farmerDynamicData.getReferenceId()));
						setGroup(sam.getName());
					}
				} else if (!ObjectUtil.isEmpty(farmerDynamicData)
						&& farmerDynamicData.getEntityId().equalsIgnoreCase("6")) {
					if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
						FarmCrops farm = farmerService
								.findByFarmCropsId(Long.valueOf(farmerDynamicData.getReferenceId()));
						setFarmer_ics(String.valueOf(farm.getFarm().getFarmer().getFirstName()));
						setFarmList_ics(String.valueOf(farm.getFarm().getFarmName()));
						setSelectedVillage(String.valueOf(farm.getFarm().getFarmer().getVillage().getName()));
						setFarmCropList(farm.getProcurementVariety());

					}
				}

				/*
				 * if (!ObjectUtil.isEmpty(farmerDynamicData)){ if
				 * (farmerDynamicData.getIsSeason()){ if
				 * (farmerDynamicData.getIsSeason() == 1) { HarvestSeason season
				 * = getSeason(farmerDynamicData.getSeason());
				 * setSeason(!ObjectUtil.isEmpty(season) ? season.getName() :
				 * ""); } } }
				 */

			}
		}

		// farm crops start

		if (farm != null) {
			farmCrops = farmerService.findFarmCropsByFarmCode(Long.valueOf(farm.getId()));
		}

		if (farmCrops != null) {

			setFarmId(String.valueOf(farmCrops.getFarm().getId()));
			setFarmerId(String.valueOf(farmCrops.getFarm().getFarmer().getId()));

			ESESystem preferences_farmcrops = preferncesService.findPrefernceById("1");
			setCropInfoEnabled(preferences_farmcrops.getPreferences().get(ESESystem.ENABLE_CROP_INFO));
			if (!ObjectUtil.isEmpty(preferences_farmcrops)) {
				DateFormat genDate_farmcrops = new SimpleDateFormat(
						preferences_farmcrops.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));

				if (!ObjectUtil.isEmpty(farmCrops.getSowingDate())) {
					if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)) {
						SimpleDateFormat sf = new SimpleDateFormat("MM-dd-yyyy");
						setSowingDate(sf.format(farmCrops.getSowingDate()));
					} else {
						setSowingDate(genDate_farmcrops.format(farmCrops.getSowingDate()));
					}
					if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
						if (!StringUtil.isEmpty(farmCrops.getEstimatedHarvestDate())) {
							harvestDate = genDate_farmcrops.format(farmCrops.getEstimatedHarvestDate());
						}
					} else {
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(farmCrops.getSowingDate());
						if (!StringUtil.isEmpty(farmCrops.getProcurementVariety().getHarvestDays())) {
							calendar.add(Calendar.DAY_OF_YEAR,
									Integer.parseInt(farmCrops.getProcurementVariety().getHarvestDays()));
							Date harDate = calendar.getTime();
							if (harDate != null) {
								if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)) {
									SimpleDateFormat sf = new SimpleDateFormat("MM-dd-yyyy");
									harvestDate = sf.format(harDate);
								} else {
									harvestDate = genDate_farmcrops.format(harDate);
								}
							}
						}
					}
				}
			}

			FarmCatalogue typ = getCatlogueValueByCode(farmCrops.getType());
			if (!ObjectUtil.isEmpty(typ) && typ != null) {
				if (!StringUtil.isEmpty(typ.getCode()) && typ.getCode() != null && typ.getCode().equals("99")) {
					farmCrops.setType(typ.getName() + ": "
							+ (!ObjectUtil.isEmpty(farmCrops.getOtherType()) ? farmCrops.getOtherType() : ""));
				} else {
					farmCrops.setType(typ.getName());
				}
			} else {
				farmCrops.setType("");
			}
			if (!StringUtil.isEmpty(farmCrops.getSeedSource())) {
				FarmCatalogue seedSource = getCatlogueValueByCode(farmCrops.getSeedSource());
				if (!ObjectUtil.isEmpty(seedSource) && seedSource != null) {

					farmCrops.setSeedSource(!StringUtil.isEmpty(seedSource.getName()) ? seedSource.getName() : "NA");

				}
			}

			if (!StringUtil.isEmpty(farmCrops.getCropCategoryList()) && farmCrops.getCropCategoryList() != "-1") {
				if (farmCrops.getCropCategoryList() != null && farmCrops.getCropCategoryList().length() > 1) {
					FarmCatalogue catalogue_farmcrops = getCatlogueValueByCode(
							String.valueOf(farmCrops.getCropCategoryList()));
					if (!ObjectUtil.isEmpty(catalogue_farmcrops)) {
						String name = catalogue_farmcrops != null ? catalogue_farmcrops.getName() : "";
						farmCrops.setCropCategoryList(name);
					} else {
						farmCrops.setCropCategoryList("");
					}
				}

			}

			if (!StringUtil.isEmpty(farmCrops.getOtherSeedTreatmentDetails())) {
				farmCrops.setOtherSeedTreatmentDetails(farmCrops.getOtherSeedTreatmentDetails());
			}

			if (farm.getActiveCoordinates() != null && farm.getActiveCoordinates().getFarmCoordinates() != null
					&& !ObjectUtil.isEmpty(farm.getActiveCoordinates())
					&& !ObjectUtil.isListEmpty(farm.getActiveCoordinates().getFarmCoordinates())) {
				jsonObjectList = getFarmJSONObjects_farmCrops(
						farmCrops.getActiveCoordinates().getFarmCropsCoordinates());
			} else {
				jsonObjectList = new ArrayList();
			}

		}

		return "farmerProfileExport";
	}

	public String getScopeName() {
		return scopeName;
	}

	public void setScopeName(String scopeName) {
		this.scopeName = scopeName;
	}

	private HarvestSeason getSeason(String seasonCode) {

		HarvestSeason season = getHarvestSeason(seasonCode);
		return season;

	}

	public ProcurementVariety getFarmCropList() {
		return farmCropList;
	}

	public void setFarmCropList(ProcurementVariety farmCropList) {
		this.farmCropList = farmCropList;
	}

	public String getCorrectiveActionPlan() {
		return correctiveActionPlan;
	}

	public void setCorrectiveActionPlan(String correctiveActionPlan) {
		this.correctiveActionPlan = correctiveActionPlan;
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

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getSelectedVillage() {
		return selectedVillage;
	}

	public void setSelectedVillage(String selectedVillage) {
		this.selectedVillage = selectedVillage;
	}

	public String getFarmList_ics() {
		return farmList_ics;
	}

	public void setFarmList_ics(String farmList_ics) {
		this.farmList_ics = farmList_ics;
	}

	public String getFarmer_ics() {
		return farmer_ics;
	}

	public void setFarmer_ics(String farmer_ics) {
		this.farmer_ics = farmer_ics;
	}

	public String getFarmId() {
		return farmId;
	}

	public void setFarmId(String farmId) {
		this.farmId = farmId;
	}

	public String getCropInfoEnabled() {
		return cropInfoEnabled;
	}

	public void setCropInfoEnabled(String cropInfoEnabled) {
		this.cropInfoEnabled = cropInfoEnabled;
	}

	public String getFarmImageByteString() {
		return farmImageByteString;
	}

	public void setFarmImageByteString(String farmImageByteString) {
		this.farmImageByteString = farmImageByteString;
	}

	public String getSelectedInputSource() {
		return selectedInputSource;
	}

	public void setSelectedInputSource(String selectedInputSource) {
		this.selectedInputSource = selectedInputSource;
	}

	public String getSelectedWaterSource() {
		return selectedWaterSource;
	}

	public void setSelectedWaterSource(String selectedWaterSource) {
		this.selectedWaterSource = selectedWaterSource;
	}

	public List<CropSupply> getHarvestSupply() {
		return harvestSupply;
	}

	public void setHarvestSupply(List<CropSupply> harvestSupply) {
		this.harvestSupply = harvestSupply;
	}

	public String getSangham() {
		return sangham;
	}

	public void setSangham(String sangham) {
		this.sangham = sangham;
	}

	public String getFarmerUniqueId() {
		return farmerUniqueId;
	}

	public void setFarmerUniqueId(String farmerUniqueId) {
		this.farmerUniqueId = farmerUniqueId;
	}

	public List<CropHarvest> getHarvest() {
		return harvest;
	}

	public void setHarvest(List<CropHarvest> harvest) {
		this.harvest = harvest;
	}

	private Map<Integer, String> formEnrollmentPlaceMap(String keyProperty, Map<Integer, String> enrollmentMap) {

		enrollmentMap = getPropertyData(keyProperty);
		return enrollmentMap;
	}

	private Map formMap(String keyProperty, Map dataMap) {

		dataMap = new LinkedHashMap();
		String values = getText(keyProperty);
		if (!StringUtil.isEmpty(values)) {
			String[] valuesArray = values.split(",");
			int i = 0;
			for (String value : valuesArray) {
				if (value.equalsIgnoreCase("other") || value.equalsIgnoreCase("others")) {
					dataMap.put(OTHER, value);
				} else {
					dataMap.put(i++, value);
				}
			}
		}
		return dataMap;
	}

	private Map<Integer, String> formCertification(String keyProperty, Map<Integer, String> enrollmentMap) {

		certificationTypes = new LinkedHashMap();
		String values = getText(keyProperty);
		if (!StringUtil.isEmpty(values)) {
			String[] valuesArray = values.split(",");
			int i = 1;
			// Arrays.sort(valuesArray);
			for (String value : valuesArray) {
				certificationTypes.put(i++, value);
			}
		}
		return certificationTypes;
	}

	public void populateHideFn() throws ScriptException {
		// Type Plays key in this functionality, By using that type we fetch
		// component object by using jquery
		// 1=>By Component Name, it will fetch parent div
		// 2=>By Component Id, it will fetch parent div
		// 3=>By Component Class, it will fetch parent div
		// 4=>By Component Class, usually we use this for hide/show whole div ..
		// it doesn't fetch any parent or child
		// 5=>it will destroy or remove the component, if two component had same
		// name just add class to unwanted component and set that type in farmer
		// field to 5

		Map<String, JSONArray> jsonArray = new LinkedHashMap<>();
		List<FarmerField> farmerFieldList = farmerService.listFarmerFields();

		if ((getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID))
				|| (getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID))) {
			if (!StringUtil.isEmpty(getBranchId())
					&& getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_ORGANIC_BRANCH_ID)) {
				farmerFieldList = farmerFieldList.stream().filter(farmerField -> farmerField.getOthers() == 1)
						.collect(Collectors.toList());
			} else if (!StringUtil.isEmpty(getBranchId())
					&& getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_BCI_BRANCH_ID)) {
				farmerFieldList = farmerFieldList.stream().filter(farmerField -> farmerField.getOthers() == 2)
						.collect(Collectors.toList());
			} else {
				farmerFieldList = farmerFieldList.stream().filter(farmerField -> (farmerField.getOthers() == 1))
						.collect(Collectors.toList());
			}
		} /*
			 * else if
			 * (getCurrentTenantId().equalsIgnoreCase(ESESystem.PGSS_TENANT_ID))
			 * { if (!StringUtil.isEmpty(getType()) &&
			 * getType().equalsIgnoreCase(Farmer.IRP)) { farmerFieldList =
			 * farmerFieldList.stream().filter(farmerField ->
			 * farmerField.getOthers() == 2) .collect(Collectors.toList()); }
			 * else { farmerFieldList =
			 * farmerFieldList.stream().filter(farmerField ->
			 * farmerField.getOthers() == 1) .collect(Collectors.toList()); } }
			 */

		JSONArray activeFiledsArray = new JSONArray();
		JSONArray inActiveFiledsArray = new JSONArray();
		JSONArray destroyFiledsArray = new JSONArray();

		farmerFieldList.stream().filter(farmerField -> farmerField.getFarmerProfileExport() == 1L)
				.forEach(farmerField -> {
					activeFiledsArray.add(getJSONObject(farmerField.getType(), farmerField.getTypeName()));
				});

		farmerFieldList.stream().filter(farmerField -> farmerField.getStatus() == 0L).forEach(farmerField -> {
			inActiveFiledsArray.add(getJSONObject(farmerField.getType(), farmerField.getTypeName()));
		});

		farmerFieldList.stream().filter(farmerField -> farmerField.getType().equals("5")).forEach(farmerField -> {
			destroyFiledsArray.add(getJSONObject(farmerField.getType(), farmerField.getTypeName()));
		});

		jsonArray.put("activeFields", activeFiledsArray);
		jsonArray.put("inActiveFields", inActiveFiledsArray);
		jsonArray.put("destroyFileds", destroyFiledsArray);

		JSONObject jsonobject = new JSONObject();
		jsonobject.putAll(jsonArray);
		printAjaxResponse(jsonobject, "text/html");
	}

	@SuppressWarnings("unchecked")
	private List<JSONObject> getFarmJSONObjects(Set<Coordinates> coordinates) {

		List<JSONObject> returnObjects = new ArrayList<JSONObject>();
		List<Coordinates> listCoordinates = new ArrayList<Coordinates>(coordinates);
		Collections.sort(listCoordinates);
		if (!ObjectUtil.isListEmpty(listCoordinates)) {
			for (Coordinates coordinateObj : listCoordinates) {
				JSONObject jsonObject = new JSONObject();

				// jsonObject.put("orderNo",!ObjectUtil.isEmpty(coordinateObj.getOrderNo())
				// ? coordinateObj.getOrderNo():"");
				jsonObject.put("lat",
						!ObjectUtil.isEmpty(coordinateObj.getLatitude()) ? coordinateObj.getLatitude() : "");
				jsonObject.put("lon",
						!ObjectUtil.isEmpty(coordinateObj.getLongitude()) ? coordinateObj.getLongitude() : "");
				returnObjects.add(jsonObject);
			}
		}
		return returnObjects;
	}

	public String getFarmerImageByteString() {
		return farmerImageByteString;
	}

	public void setFarmerImageByteString(String farmerImageByteString) {
		this.farmerImageByteString = farmerImageByteString;
	}

	public List<JSONObject> getJsonObjectList() {
		return jsonObjectList;
	}

	public void setJsonObjectList(List<JSONObject> jsonObjectList) {
		this.jsonObjectList = jsonObjectList;
	}

	public ICardService getCardService() {
		return cardService;
	}

	public void setCardService(ICardService cardService) {
		this.cardService = cardService;
	}

	public ESECard getCard() {
		return card;
	}

	public void setCard(ESECard card) {
		this.card = card;
	}

	private String getCurrentSeasonCode() {

		ESESystem preference = preferncesService.findPrefernceById(ESESystem.CLIENT);
		return preference.getPreferences().get(ESESystem.CURRENT_SEASON_CODE);
	}

	public InterestCalcConsolidated getInterestCalcConsolidated() {
		return interestCalcConsolidated;
	}

	public void setInterestCalcConsolidated(InterestCalcConsolidated interestCalcConsolidated) {
		this.interestCalcConsolidated = interestCalcConsolidated;
	}

	public String getIsInterestModule() {
		return isInterestModule;
	}

	public void setIsInterestModule(String isInterestModule) {
		this.isInterestModule = isInterestModule;
	}

	public String getFpoEnabled() {
		return fpoEnabled;
	}

	public void setFpoEnabled(String fpoEnabled) {
		this.fpoEnabled = fpoEnabled;
	}

	public String getFarmerBankInfoEnabled() {
		return farmerBankInfoEnabled;
	}

	public void setFarmerBankInfoEnabled(String farmerBankInfoEnabled) {
		this.farmerBankInfoEnabled = farmerBankInfoEnabled;
	}

	public String getIdProofEnabled() {
		return idProofEnabled;
	}

	public void setIdProofEnabled(String idProofEnabled) {
		this.idProofEnabled = idProofEnabled;
	}

	public String getInsuranceInfoEnabled() {
		return insuranceInfoEnabled;
	}

	public void setInsuranceInfoEnabled(String insuranceInfoEnabled) {
		this.insuranceInfoEnabled = insuranceInfoEnabled;
	}

	public String getGramPanchayatEnable() {
		return gramPanchayatEnable;
	}

	public void setGramPanchayatEnable(String gramPanchayatEnable) {
		this.gramPanchayatEnable = gramPanchayatEnable;
	}

	public String getFingerPrintEnabled() {
		return fingerPrintEnabled;
	}

	public void setFingerPrintEnabled(String fingerPrintEnabled) {
		this.fingerPrintEnabled = fingerPrintEnabled;
	}

	public String getBankAccType() {
		return bankAccType;
	}

	public void setBankAccType(String bankAccType) {
		this.bankAccType = bankAccType;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getLoanRepaymentDate() {
		return loanRepaymentDate;
	}

	public void setLoanRepaymentDate(String loanRepaymentDate) {
		this.loanRepaymentDate = loanRepaymentDate;
	}

	public String getDateOfJoining() {
		return dateOfJoining;
	}

	public void setDateOfJoining(String dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}

	public String getIcsCode() {
		return icsCode;
	}

	public void setIcsCode(String icsCode) {
		this.icsCode = icsCode;
	}

	public String getAccBalance() {
		return accBalance;
	}

	public void setAccBalance(String accBalance) {
		this.accBalance = accBalance;
	}

	public String getIdProofImgString() {
		return idProofImgString;
	}

	public void setIdProofImgString(String idProofImgString) {
		this.idProofImgString = idProofImgString;
	}

	public String getIdImgAvil() {
		return idImgAvil;
	}

	public void setIdImgAvil(String idImgAvil) {
		this.idImgAvil = idImgAvil;
	}

	public String getFingerPrintImageByteString() {
		return fingerPrintImageByteString;
	}

	public void setFingerPrintImageByteString(String fingerPrintImageByteString) {
		this.fingerPrintImageByteString = fingerPrintImageByteString;
	}

	public boolean isFarmerAndContractStatus() {
		return farmerAndContractStatus;
	}

	public void setFarmerAndContractStatus(boolean farmerAndContractStatus) {
		this.farmerAndContractStatus = farmerAndContractStatus;
	}

	public FarmerEconomy getFarmerEconomy() {
		return farmerEconomy;
	}

	public void setFarmerEconomy(FarmerEconomy farmerEconomy) {
		this.farmerEconomy = farmerEconomy;
	}

	public String getRupee() {
		return rupee;
	}

	public void setRupee(String rupee) {
		this.rupee = rupee;
	}

	public String getPaise() {
		return paise;
	}

	public void setPaise(String paise) {
		this.paise = paise;
	}

	public String getIcsString() {
		return icsString;
	}

	public void setIcsString(String icsString) {
		this.icsString = icsString;
	}

	public String getIcsUnitNoString() {
		return icsUnitNoString;
	}

	public void setIcsUnitNoString(String icsUnitNoString) {
		this.icsUnitNoString = icsUnitNoString;
	}

	public String getIcsRegNoString() {
		return icsRegNoString;
	}

	public void setIcsRegNoString(String icsRegNoString) {
		this.icsRegNoString = icsRegNoString;
	}

	public String getInvestigatorDate() {
		return investigatorDate;
	}

	public void setInvestigatorDate(String investigatorDate) {
		this.investigatorDate = investigatorDate;
	}

	public Map<Integer, String> getEnrollmentMap() {
		return enrollmentMap;
	}

	public void setEnrollmentMap(Map<Integer, String> enrollmentMap) {
		this.enrollmentMap = enrollmentMap;
	}

	public Map<Integer, String> getIsFarmerCertified() {
		return isFarmerCertified;
	}

	public void setIsFarmerCertified(Map<Integer, String> isFarmerCertified) {
		this.isFarmerCertified = isFarmerCertified;
	}

	public static int getOther() {
		return OTHER;
	}

	public Map<Integer, String> getCertificationTypes() {
		return certificationTypes;
	}

	public void setCertificationTypes(Map<Integer, String> certificationTypes) {
		this.certificationTypes = certificationTypes;
	}

	public void setEducationList(Map<Integer, String> educationList) {
		this.educationList = educationList;
	}

	public Map<String, String> getEducationList() {
		Map<String, String> educationList = new LinkedHashMap<>();
		educationList = getFarmCatalougeMap(Integer.valueOf(getText("educationType")));
		return educationList;

	}

	public Map<String, String> getFarmOwnedList() {

		Map<String, String> list = new LinkedHashMap<>();

		list = getFarmCatalougeMap(Integer.valueOf(getText("landOwnerShip")));
		return list;
	}

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

	public Map<String, String> getSoilTypeList() {

		Map<String, String> soilTypeMap = new LinkedHashMap<String, String>();

		List<FarmCatalogue> soilTypeList = farmerService.listCatelogueType(getText("soilType"));
		for (FarmCatalogue obj : soilTypeList) {
			soilTypeMap.put(obj.getCode(), obj.getName());
		}
		return soilTypeMap;
	}

	public Map<String, String> getMethodOfIrrigationList() {

		Map<String, String> list = new LinkedHashMap<>();

		list = getFarmCatalougeMap(Integer.valueOf(getText("methodOfIrrigation")));
		return list;
	}

	public Map<Integer, String> getSoilFertilityList() {

		if (soilFertilityList.size() == 0) {
			String[] soilFertility = getText("soilFertilityList").split(",");
			int i = 1;
			for (String sfertilityValue : soilFertility)
				soilFertilityList.put(i++, sfertilityValue);
		}
		return soilFertilityList;
	}

	public Map<Integer, String> getIcsStatusList() {

		String[] icsStatus = getLocaleProperty("icsStatusList").split(",");
		for (int i = 0; i < icsStatus.length; i++)
			icsStatusList.put(i, icsStatus[i]);

		return icsStatusList;
	}

	public Map<String, String> getSoilTextureList() {

		Map<String, String> soilTexMap = new LinkedHashMap<String, String>();

		List<FarmCatalogue> soilTexList = farmerService.listCatelogueType(getText("soilTexture").trim());
		for (FarmCatalogue obj : soilTexList) {
			soilTexMap.put(obj.getCode(), obj.getName());
		}
		return soilTexMap;
	}

	public Map<Integer, String> getLandGradientList() {

		Map<Integer, String> landGradientList = new LinkedHashMap<>();
		if (landGradientList.size() == 0) {
			String[] land = getLocaleProperty("landGradientList").split(",");
			int i = 1;
			for (String landGradient : land)
				landGradientList.put(i++, landGradient);
		}

		return landGradientList;
	}

	public Map<Integer, String> getApproadList() {

		if (approadList.size() == 0) {
			String[] road = getText("approadList").split(",");
			int i = 1;
			for (String roadValue : road)
				approadList.put(i++, roadValue);
		}

		return approadList;
	}

	public void populateHideFnFarm() throws ScriptException {
		List<FarmField> farmerFieldList = farmerService.listRemoveFarmFields();
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();

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
			/*
			 * farmerFieldList.forEach(farmerField -> { JSONObject jsonObject =
			 * new JSONObject(); jsonObject.put("type", farmerField.getType());
			 * jsonObject.put("typeName", farmerField.getTypeName());
			 * jsonObjects.add(jsonObject); });
			 */

			farmerFieldList.stream().filter(obj -> obj.getFarmerProfileExport() == 1l).forEach(farmerField -> {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("type", farmerField.getType());
				jsonObject.put("typeName", farmerField.getTypeName());
				jsonObjects.add(jsonObject);
			});

		}

		printAjaxResponse(jsonObjects, "text/html");
	}

	private List<JSONObject> getFarmJSONObjects_farmCrops(Set<FarmCropsCoordinates> coordinates) {

		List<JSONObject> returnObjects = new ArrayList<JSONObject>();
		List<FarmCropsCoordinates> listCoordinates = new ArrayList<FarmCropsCoordinates>(coordinates);
		Collections.sort(listCoordinates);

		if (!ObjectUtil.isListEmpty(listCoordinates)) {
			for (FarmCropsCoordinates coordinateObj : listCoordinates) {
				JSONObject jsonObject = new JSONObject();

				// jsonObject.put("orderNo",!ObjectUtil.isEmpty(coordinateObj.getOrderNo())
				// ? coordinateObj.getOrderNo():"");
				jsonObject.put("lat",
						!ObjectUtil.isEmpty(coordinateObj.getLatitude()) ? coordinateObj.getLatitude() : "");
				jsonObject.put("lon",
						!ObjectUtil.isEmpty(coordinateObj.getLongitude()) ? coordinateObj.getLongitude() : "");
				returnObjects.add(jsonObject);
			}
		}
		return returnObjects;
	}

	public void populateHideFnFarmCrops() throws ScriptException {
		List<FarmCropsField> farmCropsFieldList = farmerService.listFarmCropsFields();
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();

		farmCropsFieldList.stream().filter(obj -> obj.getFarmerProfileExport() == 1l).forEach(farmerField -> {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("type", farmerField.getType());
			jsonObject.put("typeName", farmerField.getTypeName());
			jsonObjects.add(jsonObject);
		});

		printAjaxResponse(jsonObjects, "text/html");
	}

	public String getSowingDate() {
		return sowingDate;
	}

	public void setSowingDate(String sowingDate) {
		this.sowingDate = sowingDate;
	}

	public String getStaticMapUrl() {
		return staticMapUrl;
	}

	public void setStaticMapUrl(String staticMapUrl) {
		this.staticMapUrl = staticMapUrl;
	}

	public String getDigitalSignatureEnabled() {
		return digitalSignatureEnabled;
	}

	public void setDigitalSignatureEnabled(String digitalSignatureEnabled) {
		this.digitalSignatureEnabled = digitalSignatureEnabled;
	}

	public FarmerDynamicData getFarmerDynamicData() {
		return farmerDynamicData;
	}

	public void setFarmerDynamicData(FarmerDynamicData farmerDynamicData) {
		this.farmerDynamicData = farmerDynamicData;
	}

	private String DecimalToDegMinSec(Double decimalNumber) {
		double degrees;
		double minutes, seconds;
		String dmsString;
		degrees = (int) (Math.round(decimalNumber));
		minutes = (int) (Math.round((decimalNumber - degrees) * 60));
		seconds = (((decimalNumber - degrees) * 60) - minutes) * 60;
		DecimalFormat f = new DecimalFormat("##.####");
		dmsString = String.valueOf(degrees) + "Â° " + String.valueOf(minutes) + "' " + String.valueOf(f.format(seconds))
				+ "\"";
		return dmsString;
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

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public void setAgentList(Map<String, String> agentList) {
		this.agentList = agentList;
	}

	public Map<Long, String> getSamithiList() {

		samithi = locationService.listSamithiesBasedOnType();
		Map<Long, String> samithiMap = new LinkedHashMap<>();
		samithiMap = samithi.stream().collect(Collectors.toMap(Warehouse::getId, Warehouse::getName));
		return samithiMap;
	}

	public List<Warehouse> getSamithi() {
		return samithi;
	}

	public void setSamithi(List<Warehouse> samithi) {
		this.samithi = samithi;
	}

	public String getSamithiName() {
		return samithiName;
	}

	public void setSamithiName(String samithiName) {
		this.samithiName = samithiName;
	}

	public Map<String, String> getCreatedUserList() {
		Map<String, String> lotNoMap = new LinkedHashMap<>();
		lotNoMap = farmerService.listFarmerByCreatedUser().stream().filter(f -> f != null && !StringUtil.isEmpty(f))
				.collect(Collectors.toMap(k -> k.toString(), v -> v.toString()));
		return lotNoMap;

	}

	public String getCreatedUserName() {
		return createdUserName;
	}

	public void setCreatedUserName(String createdUserName) {
		this.createdUserName = createdUserName;
	}

}
