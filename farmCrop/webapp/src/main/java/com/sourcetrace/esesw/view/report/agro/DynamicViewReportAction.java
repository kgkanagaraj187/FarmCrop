package com.sourcetrace.esesw.view.report.agro;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
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
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.util.CellRangeAddress;
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

import com.ese.entity.profile.CropHarvest;
import com.ese.entity.profile.CropHarvestDetails;
import com.ese.entity.profile.CropSupply;
import com.ese.entity.profile.CropSupplyDetails;
import com.ese.entity.txn.mfi.InterestCalcConsolidated;
import com.ese.entity.util.ESESystem;
import com.ese.entity.util.FarmCropsField;
import com.ese.entity.util.FarmField;
import com.ese.entity.util.FarmerField;
import com.ese.view.profile.FarmerAction;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.pdf.codec.Base64.OutputStream;
import com.sourcetrace.eses.BreadCrumb;
import com.sourcetrace.eses.dao.IReportDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.ColdStorage;
import com.sourcetrace.eses.entity.ColdStorageDetail;
import com.sourcetrace.eses.entity.DynamicFeildMenuConfig;
import com.sourcetrace.eses.entity.DynamicFieldConfig;
import com.sourcetrace.eses.entity.DynamicFieldConfig.LIST_METHOD;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.ColdStorageStockTransfer;
import com.sourcetrace.eses.order.entity.txn.ColdStorageStockTransferDetail;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfig;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfigDetail;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfigFilter;
import com.sourcetrace.eses.order.entity.txn.PMTDetail;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.order.entity.txn.ProcurementDetail;
import com.sourcetrace.eses.service.DynamicReportProperties;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.ICardService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.txn.agrocert.CropYield;
import com.sourcetrace.eses.txn.agrocert.CropYieldDetail;
import com.sourcetrace.eses.service.IFarmCropsService;
import com.sourcetrace.eses.util.Base64Util;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.BankInformation;
import com.sourcetrace.esesw.entity.profile.Coordinates;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.ESECard;
import com.sourcetrace.esesw.entity.profile.Expenditure;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.FarmCropsCoordinates;
import com.sourcetrace.esesw.entity.profile.FarmDetailedInfo;
import com.sourcetrace.esesw.entity.profile.FarmICS;
import com.sourcetrace.esesw.entity.profile.FarmIcsConversion;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicData;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicFieldsValue;
import com.sourcetrace.esesw.entity.profile.FarmerEconomy;
import com.sourcetrace.esesw.entity.profile.FarmerLandDetails;
import com.sourcetrace.esesw.entity.profile.FarmerScheme;
import com.sourcetrace.esesw.entity.profile.FarmerSoilTesting;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.HousingInfo;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.TreeDetail;
import com.sourcetrace.esesw.entity.profile.DynamicImageData.TYPES;
import com.sourcetrace.esesw.entity.profile.FarmCropsCoordinates.typesOFCordinates;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

import antlr.StringUtils;
import sun.misc.BASE64Decoder;

public class DynamicViewReportAction extends BaseReportAction {
	public static final String SELECT_MULTI = "-1";
	public static final int SELECT = -1;
	public static final String FARM_CERTIFIED_DETAIL = "farmCertifiedDetail";
	private String daterange;
	private String mainGridCols;
	private String mainGridColNames;
	private String filterList;
	private String gridIdentity;
	private Object fValue;
	private Object mValue;
	private String expression_result;
	private static DynamicReportConfig dynamicReportConfig;
	private static DynamicReportConfig subDynamicReportConfig;
	private Object filter;
	private String xlsFileName;
	private InputStream fileInputStream;
	private List<String> fvalueByParameters = new ArrayList<String>();
	Map<Integer, Long> dynamic_report_config_detail_ID = new HashMap<Integer, Long>();
	private List<DynamicReportConfigFilter> reportConfigFilters = new LinkedList<>();
	private List<FarmCrops> farmCrops = new ArrayList<FarmCrops>();
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final String String = null;
	Map entityMap = new HashMap<>();
	Map otherMap = new HashMap<>();
	Map<String, Map> mainMap = new HashMap<>();
	private String footerSumCols;
	private String footerTotCol;
	private String query;
	private String header;
	private Object[] param;
	private String paramValue;
	private String csId;
	private String farmId;
	private Farm farm;
	private Farmer farmer;
	private String txnType;
	private int iddCol;
	private Long farmerDynamicDataId;
	private FarmerDynamicData farmerDynamicData = new FarmerDynamicData();
	protected FarmerDynamicData filter1;
	protected FarmerDynamicFieldsValue filter2;
	private FarmCrops farmCrop;
	private HarvestSeason seasons;
	private Farm farms;
	public int view_id;
	private String crop_id;
	String tothead;
	String[] selectedFields = null;
	private Integer seasonType;
	private String entityType;
	private String createdUsername;
	private String selectedVillage;
	private String farmers;
	private String scoreVal;
	private String insDate;
	private Map<Integer, String> certificationLevels = new LinkedHashMap<Integer, String>();
	public Map<String, String> seasonMap = new LinkedHashMap<>();
	int i = 0, j = 0;
	private String csvFileName;
	@Autowired
	private IPreferencesService preferncesService;
	@Autowired
	private IFarmCropsService farmCropsService;
	@Autowired
	private IProductService productService;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private IAgentService agentService;
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private IProductDistributionService productDistributionService;
	@Autowired
	private ICardService cardService;
	@Autowired
	private IAccountService accountService;
	private List<Object[]> farmerList;
	private String fetchType;
	private String groupHeader;
	private String filtersList;
	private String htmlContent;
	private String branchIdParma;
	private String sangham;

	private List<FarmICS> farmICSs;
	private int actStatuss;
	private int cerLevel;
	private String farmerId;
	private String farmerUniqueId;
	private boolean isCertifiedFarmer = false;
	private List<CropHarvest> harvest = new ArrayList<CropHarvest>();
	private List<CropHarvestDetails> harvestDetails = new ArrayList<CropHarvestDetails>();
	private HousingInfo housingInfo;
	private String presenceOfBanana;
	private Map<Integer, String> bananaTreesList = new LinkedHashMap<Integer, String>();
	private List<CropSupply> harvestSupply = new ArrayList<CropSupply>();
	private Set<CropSupplyDetails> cropSupplyDetails;
	private String dateOfInspection;
	private String lastDateChemical;
	private String farmOwnedDetail;
	private String farmIrrigationDetail;
	private String irrigationSourceDetail;
	DateFormat df = new SimpleDateFormat(getESEDateFormat());
	private Map<Integer, String> farmIrrigationList = new LinkedHashMap<Integer, String>();
	private String noOfWineOnPlot;
	private String isIrrigation;
	private String soilTypeDetail;
	private String methodOfIrrigationDetail;
	private Map<Integer, String> soilFertilityList = new LinkedHashMap<Integer, String>();
	private String soilFertilityDetail;
	private String landUnderICSStatusDetail;
	private String soilTextureDetail;
	private String landGradientDetail;
	private String selectedRoadString;
	private Map<Integer, String> icsStatusList = new LinkedHashMap<Integer, String>();
	private Map<String, String> approadList = new LinkedHashMap<String, String>();
	private String sessionYearString;
	private String regYearString;
	private String selectedWaterSource;
	private String selectedInputSource;
	private String scopeName;
	private String audioDownloadString;
	private String waterHarvests;
	private String parallelProduction;
	private String hiredLabours;
	private String riskCategory;
	private Map<Integer, String> parallelProductionList = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> hiredLabourList = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> riskCategoryList = new LinkedHashMap<Integer, String>();
	private List<TreeDetail> treeDetails;
	private String farmImageByteString;
	List<FarmerSoilTesting> farmerSoilTestingList = new LinkedList<>();
	private List<Expenditure> expenditureList;
	private List<FarmerLandDetails> farmerLandDetailsList = new ArrayList<FarmerLandDetails>();
	private List<FarmerLandDetails> updatefarmerLandDetailsList = new ArrayList<FarmerLandDetails>();
	List<FarmCatalogue> catalogueList = new ArrayList<FarmCatalogue>();
	List<FarmCatalogue> catalogueList1 = new ArrayList<FarmCatalogue>();
	List<FarmCatalogue> catalogueList2 = new ArrayList<FarmCatalogue>();
	List<FarmCatalogue> catalogueList3 = new ArrayList<FarmCatalogue>();
	List<FarmCatalogue> catalogueList4 = new ArrayList<FarmCatalogue>();
	List<FarmCatalogue> machinaryList = new ArrayList<FarmCatalogue>();
	List<FarmCatalogue> polyList = new ArrayList<FarmCatalogue>();
	private String type;
	private List<JSONObject> jsonObjectList;

	private List<JSONObject> jsonFarmObjectList;
	private ESECard card;
	private InterestCalcConsolidated interestCalcConsolidated;
	private String enableContractTemplate;
	private String investigatorDate;
	private FarmerEconomy farmerEconomy;
	private String icsString;
	private String icsDropDown;
	private String icsRegNoString;
	private String icsUnitNoString;
	private String idProofEnabled;
	private String insuranceInfoEnabled;
	private String rupee;
	private String paise;
	private boolean farmerAndContractStatus = false;
	private String fingerPrintImageByteString;
	private String digitalSignatureByteString;
	private String idImgAvil;
	private String isInterestModule = "0";
	private String fpoEnabled;
	private String fingerPrintEnabled;
	private String gramPanchayatEnable;
	private String accBalance;
	private String farmerCodeEnabled;
	private String farmerBankInfoEnabled;
	private String dateOfBirth;
	private String dateOfJoining;
	private String bankAccType;
	private String loanRepaymentDate;
	private String icsCode;
	private String icsName;
	private String farmerImageByteString;
	private String idProofImgString;
	private String cropInfoEnabled;
	public String lotCode;
	private String farmCropId;
	private String season;
	private Long farmersId;
	private String defFilters;
	private String detailMethod;
	Type listType1 = new TypeToken<Map<String, String>>() {
	}.getType();
	String defFilter = "";
	boolean hit = true;
	private String pesticideFilterList;
	Map<Long, Map<String, String>> listItem = new HashMap<>();
	private String coldStorageCode;
	private String warehouseCode;
	private String chamberCode;
	private String floorCode;
	private String bayCode;
	private String subGridCols;
	private int isSubGrid;
	private List<Object[]> farmerCultiList;
	private int headerRow=0;
	StringBuilder exportParams = new StringBuilder("");
	private String selectedFarm;
	private String selectedSeason;

	public String list() {
		Calendar currentDate = Calendar.getInstance();
		Calendar cal = (Calendar) currentDate.clone();
		cal.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) - 1);
		DateFormat df = new SimpleDateFormat(DateUtil.DATABASE_DATE_FORMAT);
		super.startDate = df.format(cal.getTime());
		super.endDate = df.format(currentDate.getTime());
		setDaterange(super.startDate + " - " + super.endDate);
		request.setAttribute(HEADING, getText(LIST));

		ESESystem preferences = preferncesService.findPrefernceById("1");

		/*
		 * if (getCurrentTenantId().equalsIgnoreCase(ESESystem.KPF_TENANT_ID))
		 */

		if (!StringUtil.isEmpty(id)) {
			formMainGridCols();
			setFetchType(String.valueOf(dynamicReportConfig.getFetchType()));
			setDetailMethod(dynamicReportConfig.getDetailMethod() != null
					&& !StringUtil.isEmpty(dynamicReportConfig.getDetailMethod())
							? String.valueOf(dynamicReportConfig.getDetailMethod()) : "");
			if (!ObjectUtil.isEmpty(dynamicReportConfig)
					&& !ObjectUtil.isListEmpty(dynamicReportConfig.getDynmaicReportConfigFilters())) {
				setFilterSize(String.valueOf(dynamicReportConfig.getDynmaicReportConfigFilters().size()));

				dynamicReportConfig.getDynmaicReportConfigFilters().stream().filter(f -> f.getStatus() != 0)
						.forEach(reportConfigFilter -> {
							if (reportConfigFilter.getMethod() != null
									&& !StringUtil.isEmpty(reportConfigFilter.getMethod())
									&& reportConfigFilter.getType() != null
									&& (reportConfigFilter.getType() == 3 || reportConfigFilter.getType() == 6
											|| reportConfigFilter.getType() == 7)) {
								Map<String, String> optionMap = (Map<String, String>) getMethodValue(
										reportConfigFilter.getMethod(), null);
								reportConfigFilter.setOptions(optionMap);
							} else if (reportConfigFilter.getMethod() != null
									&& !StringUtil.isEmpty(reportConfigFilter.getMethod())
									&& reportConfigFilter.getType() != null && reportConfigFilter.getType() == 5) {
								Map<String, String> optionMap = (Map<String, String>) getQueryForFilters(
										reportConfigFilter.getMethod(), new Object[] { getBranchId() });
								reportConfigFilter.setOptions(optionMap);
							}
							reportConfigFilters.add(reportConfigFilter);
						});

				/*
				 * dynamicReportConfig.getDynmaicReportConfigFilters().stream().
				 * filter(reportConfigFilter->!StringUtil.isEmpty(
				 * reportConfigFilter.getDefaultFilter())).forEach(
				 * reportConfigFilter -> {
				 * defFilter=defFilter.concat(reportConfigFilter.getField()+":"+
				 * reportConfigFilter.getDefaultFilter()+","); if(hit){
				 * defFilter=defFilter.substring(0, defFilter.length() - 1);
				 * setDefFilters(defFilter); hit=false; } });
				 */

				/*
				 * filterList=filterList.substring(0, filterList.length() - 1);
				 * filterList=filterList+defFilters+"}";
				 */
			}
		}
		return LIST;
	}

	private void formMainGridCols() {
		dynamicReportConfig = clientService.findReportById(id);
		if (!ObjectUtil.isEmpty(dynamicReportConfig)) {
			isSubGrid = 0;
			mainGridCols = "";
			mainGridColNames = "";
			footerSumCols = "";
			footerTotCol = "";
			groupHeader = "";
			/* removing ID column config detail fro iterating */
			dynamicReportConfig.getDynmaicReportConfigDetails()
					.remove(dynamicReportConfig.getDynmaicReportConfigDetails().iterator().next());
			if (!StringUtil.isEmpty(getBranchId())) {
				/*
				 * removing Branch Dynamic Report COnfig Detail From Iterating,
				 * s its value already added to rows
				 */
				dynamicReportConfig.getDynmaicReportConfigDetails()
						.remove(dynamicReportConfig.getDynmaicReportConfigDetails().iterator().next());

			}
			dynamicReportConfig.getDynmaicReportConfigDetails().stream()
					.filter(config -> config.getIsGridAvailabiltiy())
					.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
					.forEach(dynamicReportConfigDetail -> {
						if (dynamicReportConfigDetail.getLabelName().contains("@")) {
							String label = dynamicReportConfigDetail.getLabelName();
							mainGridCols += getLocaleProperty(label) + " (" + getCurrencyType() + ")" + "#" + "" + "#"
									+ dynamicReportConfigDetail.getWidth() + "#"
									+ dynamicReportConfigDetail.getAlignment() + "%";
							mainGridColNames += label + " (" + getCurrencyType() + ")" + "#" + "" + "#"
									+ dynamicReportConfigDetail.getWidth() + "#"
									+ dynamicReportConfigDetail.getAlignment() + "%";
						} else {
							mainGridCols += getLocaleProperty(dynamicReportConfigDetail.getLabelName()) + "#" + "" + "#"
									+ dynamicReportConfigDetail.getWidth() + "#"
									+ dynamicReportConfigDetail.getAlignment() + "%";
							mainGridColNames += dynamicReportConfigDetail.getLabelName() + "#" + "" + "#"
									+ dynamicReportConfigDetail.getWidth() + "#"
									+ dynamicReportConfigDetail.getAlignment() + "%";
						}

						if (dynamicReportConfigDetail.getIsGroupHeader() != null
								&& !StringUtil.isEmpty(dynamicReportConfigDetail.getIsGroupHeader())
								&& dynamicReportConfigDetail.getIsGroupHeader().contains("~")) {
							groupHeader += dynamicReportConfigDetail.getIsGroupHeader() + "#";

						}

					});
			dynamicReportConfig.getDynmaicReportConfigDetails().stream()
					.filter(f -> f.getIsGridAvailabiltiy() && f.getIsFooterSum() != null
							&& (f.getIsFooterSum().equals("1") || f.getIsFooterSum().equals("3")))
					.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder())).forEach(dy -> {
						footerSumCols += dy.getLabelName() + "#";
					});
			dynamicReportConfig.getDynmaicReportConfigDetails().stream().filter(
					f -> f.getIsGridAvailabiltiy() && f.getIsFooterSum() != null && f.getIsFooterSum().equals("2"))
					.forEach(dy -> {
						footerTotCol = dy.getLabelName();
					});

			if (!StringUtil.isEmpty(dynamicReportConfig.getParentId())) {
				subDynamicReportConfig = clientService.findReportById(dynamicReportConfig.getParentId());
				if (!ObjectUtil.isEmpty(subDynamicReportConfig)) {
					isSubGrid = 1;
					subGridCols = "";
					subDynamicReportConfig.getDynmaicReportConfigDetails().stream()
							.filter(config -> config.getIsGridAvailabiltiy())
							.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
							.forEach(dynamicReportConfigDetail -> {
								if (dynamicReportConfigDetail.getLabelName().contains("@")) {
									String label = dynamicReportConfigDetail.getLabelName();
									subGridCols += getLocaleProperty(label) + " (" + getCurrencyType() + ")" + "#"
											+ dynamicReportConfigDetail.getWidth() + "#"
											+ dynamicReportConfigDetail.getAlignment() + "%";
								} else {
									subGridCols += getLocaleProperty(dynamicReportConfigDetail.getLabelName()) + "#"
											+ "" + "#" + dynamicReportConfigDetail.getWidth() + "#"
											+ dynamicReportConfigDetail.getAlignment() + "%";
								}
							});
				}
			}
		}
	}

	/*
	 * private void formMainGridCols1() { dynamicReportConfig =
	 * clientService.findReportById(String.valueOf(view_id)); if
	 * (!ObjectUtil.isEmpty(dynamicReportConfig)) { mainGridCols = "";
	 * mainGridColNames = ""; footerSumCols = ""; footerTotCol = "";
	 * groupHeader=""; removing ID column config detail fro iterating
	 * dynamicReportConfig.getDynmaicReportConfigDetails()
	 * .remove(dynamicReportConfig.getDynmaicReportConfigDetails().iterator().
	 * next()); if (!StringUtil.isEmpty(getBranchId())) {
	 * 
	 * removing Branch Dynamic Report COnfig Detail From Iterating, s its value
	 * already added to rows
	 * 
	 * dynamicReportConfig.getDynmaicReportConfigDetails()
	 * .remove(dynamicReportConfig.getDynmaicReportConfigDetails().iterator().
	 * next());
	 * 
	 * } dynamicReportConfig.getDynmaicReportConfigDetails().stream()
	 * .filter(config -> config.getIsGridAvailabiltiy()) .sorted((f1, f2) ->
	 * Long.compare(f1.getOrder(), f2.getOrder()))
	 * .forEach(dynamicReportConfigDetail -> { if
	 * (dynamicReportConfigDetail.getLabelName().contains("@")) { String label =
	 * dynamicReportConfigDetail.getLabelName(); mainGridCols +=
	 * getLocaleProperty(label) + " (" + getCurrencyType() + ")" + "#" +
	 * dynamicReportConfigDetail.getWidth() + "#" +
	 * dynamicReportConfigDetail.getAlignment() + "%"; mainGridColNames += label
	 * + " (" + getCurrencyType() + ")" + "#" +
	 * dynamicReportConfigDetail.getWidth() + "#" +
	 * dynamicReportConfigDetail.getAlignment() + "%"; } else { mainGridCols +=
	 * getLocaleProperty(dynamicReportConfigDetail.getLabelName()) + "#" +
	 * dynamicReportConfigDetail.getWidth() + "#" +
	 * dynamicReportConfigDetail.getAlignment() + "%"; mainGridColNames +=
	 * dynamicReportConfigDetail.getLabelName() + "#" +
	 * dynamicReportConfigDetail.getWidth() + "#" +
	 * dynamicReportConfigDetail.getAlignment() + "%"; }
	 * 
	 * if (dynamicReportConfigDetail.getIsGroupHeader() != null &&
	 * !StringUtil.isEmpty(dynamicReportConfigDetail.getIsGroupHeader()) &&
	 * dynamicReportConfigDetail.getIsGroupHeader().contains("~")) { groupHeader
	 * += dynamicReportConfigDetail.getIsGroupHeader() + "#";
	 * 
	 * }
	 * 
	 * }); dynamicReportConfig.getDynmaicReportConfigDetails().stream().filter(
	 * f -> f.getIsGridAvailabiltiy() && f.getIsFooterSum() != null &&
	 * (f.getIsFooterSum().equals("1") || f.getIsFooterSum().equals("3")))
	 * .sorted((f1, f2) -> Long.compare(f1.getOrder(),
	 * f2.getOrder())).forEach(dy -> { footerSumCols += dy.getLabelName() + "#";
	 * }); dynamicReportConfig.getDynmaicReportConfigDetails().stream().filter(
	 * f -> f.getIsGridAvailabiltiy() && f.getIsFooterSum() != null &&
	 * f.getIsFooterSum().equals("2")) .forEach(dy -> { footerTotCol =
	 * dy.getLabelName(); }); } }
	 */

	@SuppressWarnings("unused")
	private Object getMethodValue(String methodName, Object param) {
		Object field = null;
		try {
			@SuppressWarnings("rawtypes")
			Class cls = this.getClass();

			if (param != null) {
				if (param instanceof Object[]) {
					Method setNameMethod = this.getClass().getMethod(methodName, Object[].class);
					field = setNameMethod.invoke(this, param);
				} else {
					Method setNameMethod = this.getClass().getMethod(methodName, String.class);
					field = setNameMethod.invoke(this, param);
				}
			} else {
				Method setNameMethod = this.getClass().getMethod(methodName);
				field = setNameMethod.invoke(this);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return field;
	}

	public String detail() throws Exception {
		// startDate=DateUtil.convertDateFormat(startDate,DateUtil.DATE_FORMAT ,
		// toFarmat)
		String breanchidPa = "BRANCH_ID";

		if (!StringUtil.isEmpty(filterList)) {
			try {
				Map<String, String> filtersList = new Gson().fromJson(filterList, listType1);
				if (dynamicReportConfig.getFetchType() == 2L) {
					if (branchIdParma != null && !StringUtil.isEmpty(branchIdParma)) {
						dynamicReportConfig.getDynmaicReportConfigDetails().stream()
						.skip(1).limit(1)
						.forEach(dy -> {
							filtersList.put(String.valueOf(dy.getField()), "9~" + branchIdParma + "");
						});
					} else if (getBranchId() != null) {
						
						dynamicReportConfig.getDynmaicReportConfigDetails().stream()
						.skip(1).limit(1)
						.forEach(dy -> {
							filtersList.put(String.valueOf(dy.getField()), "9~" + getBranchId() + "");
						});
					}

					if (subBranchIdParma != null && !StringUtil.isEmpty(subBranchIdParma)
							&& !subBranchIdParma.equals("0")) {
						filtersList.put("branchId", "9~" + subBranchIdParma + "");
					}

				} else {

					if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.OCP)) {
						if (branchIdParma != null && !StringUtil.isEmpty(branchIdParma)) {
							filtersList.put("BRANCH_ID", " ='" + branchIdParma + "'");
						} else if (getBranchId() != null) {
							filtersList.put("BRANCH_ID", " ='" + getBranchId() + "'");
						}

						if (subBranchIdParma != null && !StringUtil.isEmpty(subBranchIdParma)
								&& !subBranchIdParma.equals("0")) {
							filtersList.put("BRANCH_ID", " ='" + subBranchIdParma + "'");
						}
					} else {
						if (branchIdParma != null && !StringUtil.isEmpty(branchIdParma) && (subBranchIdParma == null
								|| StringUtil.isEmpty(subBranchIdParma) || !subBranchIdParma.equals("0"))) {
							filtersList.put("PARENT_ID", " ='" + branchIdParma + "'");
						} else if (getBranchId() != null) {
							filtersList.put("BRANCH_ID", " ='" + getBranchId() + "'");
						}

						if (subBranchIdParma != null && !StringUtil.isEmpty(subBranchIdParma)
								&& !subBranchIdParma.equals("0")) {
							filtersList.put("BRANCH_ID", " ='" + subBranchIdParma + "'");
						}
					}

				}

				mainMap.put(DynamicReportConfig.FILTER_FIELDS, filtersList);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		otherMap.put(DynamicReportConfig.DYNAMIC_CONFIG_DETAIL, dynamicReportConfig.getDynmaicReportConfigDetails());
		otherMap.put(DynamicReportConfig.ENTITY, dynamicReportConfig.getEntityName());
		otherMap.put(DynamicReportConfig.ALIAS, dynamicReportConfig.getAlias());
		otherMap.put(DynamicReportConfig.GROUP_PROPERTY, dynamicReportConfig.getGroupProperty());
		mainMap.put(DynamicReportConfig.OTHER_FIELD, otherMap);

		Map data = null;
		if (!ObjectUtil.isEmpty(dynamicReportConfig) && dynamicReportConfig.getFetchType() == 2L) {
			data = readProjectionDataStatic(mainMap);
		} else if (!ObjectUtil.isEmpty(dynamicReportConfig) && dynamicReportConfig.getFetchType() == 3L) {

			data = readProjectionDataView(mainMap);
		} else {
			data = readData();
		}
		setGridIdentity(IReportDAO.MAIN_GRID);

		DynamicReportConfigDetail dtt = dynamicReportConfig.getDynmaicReportConfigDetails().iterator().next();
		if (dtt.getField().contains("##")) {
			iddCol = dtt.getField().split("##").length;
		} else {
			iddCol = 0;
		}

		/* removing ID column config detail fro iterating */
		dynamicReportConfig.getDynmaicReportConfigDetails()
				.remove(dynamicReportConfig.getDynmaicReportConfigDetails().iterator().next());
		/*
		 * removing Branch Dynamic Report COnfig Detail From Iterating, s its
		 * value already added to rows
		 */
		dynamicReportConfig.getDynmaicReportConfigDetails()
				.remove(dynamicReportConfig.getDynmaicReportConfigDetails().iterator().next());

		return sendJSONResponse(data);
	}

	String branchId = "";
	AtomicInteger runCount = new AtomicInteger(1);
	private Map<java.lang.String, java.lang.String> plotting;

	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		runCount = new AtomicInteger(1);
		String id = "";
		 exportParams = new StringBuilder("");
		if (obj instanceof FarmerDynamicData) {
			FarmerDynamicData farmerDynamicData = (FarmerDynamicData) obj;
			rows.add(!StringUtil.isEmpty(farmerDynamicData.getDate())
					? DateUtil.convertDateToString(farmerDynamicData.getDate(), DateUtil.STD_DATE_TIME_FORMAT) : "");

			rows.add(getAgentList().get(farmerDynamicData.getCreatedUser()));
			Farm farm = farmerService.findFarmById(Long.valueOf(farmerDynamicData.getReferenceId()));
			rows.add(!ObjectUtil.isEmpty(farm) ? farm.getFarmName() : "");
			jsonObject.put("id", farmerDynamicData.getId());
			jsonObject.put("cell", rows);
		} else if (obj instanceof Object[]) {
			Object[] arr = (Object[]) obj;
			if (iddCol == 0) {
				id = String.valueOf(arr[0]);
				// runCount.getAndIncrement();
			} else {
				for (i = 0; i < iddCol; i++) {
					id = id + String.valueOf(arr[i]) + "-";
					if (i != 0) {
						runCount.getAndIncrement();
					}
				}
				id = StringUtil.removeLastChar(id, '-');

			}
			branchId = getBranchId();
			DynamicReportConfig dtt = dynamicReportConfig;

			if (getGridIdentity().equalsIgnoreCase(IReportDAO.SUB_GRID)) {
				dtt = subDynamicReportConfig;
			}

			if (!StringUtil.isEmpty(dtt)) {
				if (StringUtil.isEmpty(getBranchId())) {
					fValue = ReflectUtil.getObjectFieldValue(arr, String.valueOf(runCount.getAndIncrement()));
					branchId = String.valueOf(fValue);
					rows.add(getBranchesMap().get(fValue));
				} else {
					runCount.getAndIncrement();
				}
				
				dtt.getDynmaicReportConfigDetails().stream().filter(config -> config.getIsGridAvailabiltiy())
						.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
						.forEach(dynamicReportConfigDetail -> {

							String ansVal = getAnswer(dynamicReportConfigDetail, arr,runCount);
							runCount.incrementAndGet();
							rows.add(ansVal);

						});
			}

			jsonObject.put("id", id);

			jsonObject.put("cell", rows);

		} else if (obj instanceof CropYieldDetail) {
			CropYieldDetail cropYieldDetail = (CropYieldDetail) obj;
			/*
			 * rows.add(!StringUtil.isEmpty(farmerDynamicData.getDate()) ?
			 * DateUtil.convertDateToString(farmerDynamicData.getDate(),
			 * DateUtil.STD_DATE_TIME_FORMAT) : "");
			 * 
			 * rows.add(getAgentList().get(farmerDynamicData.getCreatedUser()));
			 * Farm farm
			 * =farmerService.findFarmById(Long.valueOf(farmerDynamicData.
			 * getReferenceId())); rows.add(!ObjectUtil.isEmpty(farm) ?
			 * farm.getFarmName() :"");
			 */
			rows.add(cropYieldDetail.getCropYield().getCropYieldDate().toString());
			rows.add(cropYieldDetail.getCropYield().getLandHolding());
			rows.add(cropYieldDetail.getType() == 1 ? "US MRLS" : "EU MRLS");
			rows.add(cropYieldDetail.getStatus() == 0 ? "Success" : "Failed");
			rows.add("<button class=\"fa fa-download\"\"aria-hidden=\"true\"\" onclick=\"popDownload('"
					+ cropYieldDetail.getCropYield().getId() + "')\"></button>");
			// rows.add(cropYieldDetail.getCropYield().getLandHolding());
			jsonObject.put("id", cropYieldDetail.getId());
			jsonObject.put("cell", rows);
		}
		return jsonObject;

	}
	String imgs="";
	private String getAnswer(DynamicReportConfigDetail dynamicReportConfigDetail, Object[] arr,AtomicInteger at) {
		
		
	
		
		String ansVal = "";
		
		if (dynamicReportConfigDetail.getAccessType() == 1L) {
			fValue = ReflectUtil.getObjectFieldValue(arr, String.valueOf(at.get()));

			if (!ObjectUtil.isEmpty(dynamicReportConfigDetail.getExpression())
					&& !StringUtil.isEmpty(dynamicReportConfigDetail.getExpression())) {
				String expression = dynamicReportConfigDetail.getExpression();
				try {
					expression_result = ReflectUtil.getObjectFieldValueByExpression(arr, expression,
							dynamic_report_config_detail_ID);
					ansVal = expression_result;
				} catch (ScriptException e) {
					ansVal = (String) fValue;
				}

			} else {
				ansVal = (String) fValue;
			}

		} else if (Arrays.asList(2L, 3L).contains(dynamicReportConfigDetail.getAccessType())) {
			fValue = ReflectUtil.getObjectFieldValue(arr, String.valueOf(at.get()));

			if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
				mValue = getMethodValue(dynamicReportConfigDetail.getMethod(), fValue.toString());
				if (!ObjectUtil.isEmpty(mValue) && !StringUtil.isEmpty(mValue)) {
					ansVal = (String) mValue;

				}
			} else {
				ansVal = (String) fValue;
			}

			if (!ObjectUtil.isEmpty(dynamicReportConfigDetail.getExpression())
					&& !StringUtil.isEmpty(dynamicReportConfigDetail.getExpression())) {
				String expression = dynamicReportConfigDetail.getExpression();

				try {
					expression_result = ReflectUtil.getObjectFieldValueByExpression(arr, expression,
							dynamic_report_config_detail_ID);
					ansVal = expression_result;
				} catch (ScriptException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			if (!ObjectUtil.isEmpty(dynamicReportConfigDetail.getParameters())
					&& !StringUtil.isEmpty(dynamicReportConfigDetail.getParameters())) {
				String parameters = dynamicReportConfigDetail.getParameters();
				String[] parametersArray = parameters.split(",");
				fvalueByParameters = ReflectUtil.getObjectFieldValueByParameters(arr, parametersArray,
						dynamic_report_config_detail_ID);

				Object temp = getMethodValue(dynamicReportConfigDetail.getMethod(), fvalueByParameters);
				ansVal = (java.lang.String) temp;
			}

		} else if (dynamicReportConfigDetail.getAccessType() == 4L) {
			fValue = ReflectUtil.getObjectFieldValue(arr, String.valueOf(at.get()));

			if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
				ansVal = "<button class='faMap' title='" + getText("farm.map.available.title")
						+ "' onclick='showFarmMap(\"" + fValue + "\")'></button>";
			} else {
				// No Latlon
				ansVal = "<button class='no-latLonIcn' title='" + getText("farm.map.unavailable.title") + "'></button>";
			}
		} else if (dynamicReportConfigDetail.getAccessType() == 5L) {
			fValue = ReflectUtil.getObjectFieldValue(arr, String.valueOf(at.get()));

			if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue) && (!"0,0".equalsIgnoreCase(String.valueOf(fValue))) && (!",".equalsIgnoreCase(String.valueOf(fValue)))) {
				String[] a = String.valueOf(fValue).split(",");
				ansVal = "<button class='faMap' title='" + getText("farm.map.available.title") + "' onclick='showMap(\""
						+ a[0] + "\",\"" + a[1] + "\")'></button>";
			} else {
				// No Latlon
				ansVal = "<button class='no-latLonIcn' title='" + getText("farm.map.unavailable.title") + "'></button>";
			}
		} else if (dynamicReportConfigDetail.getAccessType() == 6L) {
			fValue = ReflectUtil.getObjectFieldValue(arr, String.valueOf(at.get()));
if(fValue instanceof byte[]){
			if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
				String image = (Base64Util.encoder((byte[]) fValue));
				ansVal = "<button class=\"fa fa-picture-o\"\"aria-hidden=\"true\"\" onclick='popupWindow(\"" + image
						+ "\")'></button>";
			} 
}else {
				// No Latlon
				ansVal = "<button class='no-imgIcn'></button>";
			}
		} else if (dynamicReportConfigDetail.getAccessType() == 7L) {
			if (arr[at.get()] != null) {
				fValue = getQueryForFiltersJSON(dynamicReportConfigDetail.getMethod(),
						new Object[] { arr[at.get()], branchId });
				if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
					ansVal = fValue.toString();

				}
			} else {
				at.get();
			}
		} else if (dynamicReportConfigDetail.getAccessType() == 8L) {
			if (arr[at.get()] != null) {
				fValue = convertToJson(dynamicReportConfigDetail.getMethod(), arr[at.get()]);
				if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
					ansVal = fValue.toString();

				}
			} else {
				at.get();
			}
		} else if (dynamicReportConfigDetail.getAccessType() == 9L) {
			if (arr[at.get()] != null) {
				String par = dynamicReportConfigDetail.getParameters().split("~")[0].trim();
				/*
				 * mValue = fValue =
				 * convertToJson(dynamicReportConfigDetail.getMethod(),
				 * arr[at.get()]);
				 */
				String title = par.split("#")[1].trim();
				String param = par.split("#")[0].trim();
				mValue = ReflectUtil.getObjectFieldValue(arr, String.valueOf(at.get()));
				String par1 = dynamicReportConfigDetail.getParameters().split("~")[1].trim();
				fValue = ReflectUtil.getObjectFieldValue(arr, String.valueOf(param)) + "~" + par1 + "~"
						+ dynamicReportConfigDetail.getMethod() + "~" + title;
				System.out.println("fValue: " + fValue);
				// if (!ObjectUtil.isEmpty(fValue) &&
				// !StringUtil.isEmpty(fValue)) {
				/*
				 * rows.add(mValue
				 * +"<button class=\"fa fa-pencil-square-o\"\"aria-hidden=\"true\"\" onclick=\"detailPopup('"
				 * +fValue + "')\"></button>");
				 */
				if (!ObjectUtil.isEmpty(mValue) && !StringUtil.isEmpty(mValue)) {
					ansVal = "<a href onclick=\"detailPopup('" + fValue + "');return false\"> " + mValue + "</a>";

				} else {
					ansVal = "<button class='no-imgIcn'></button>";

				}
			} else {

				at.get();
			}
		} else if (dynamicReportConfigDetail.getAccessType() == 10L) {

			fValue = ReflectUtil.getObjectFieldValue(arr, String.valueOf(at.get()));
			if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {

				ansVal = "<button class='fa fa-file-pdf-o' style='font-size:18px;color:red' title='"
						+ getText("dynExport") + "' onclick='exportRecPDF(\"" + exportParams + "\")'></button>";

			} else {
				ansVal = "<button class='no-imgIcn'></button>";
			}

		} else if (dynamicReportConfigDetail.getAccessType() == 12L) {
			if (arr[at.get()] != null) {
				String par = dynamicReportConfigDetail.getParameters().split("~")[0].trim();

				String title = par.split("#")[1].trim();
				String param = par.split("#")[0].trim();
				mValue = ReflectUtil.getObjectFieldValue(arr, String.valueOf(at.get()));
				String par1 = dynamicReportConfigDetail.getParameters().split("~")[1].trim();
				fValue = ReflectUtil.getObjectFieldValue(arr, String.valueOf(param)) + "~" + par1 + "~"
						+ dynamicReportConfigDetail.getMethod() + "~" + title;
				System.out.println("fValue: " + fValue);

				if (!ObjectUtil.isEmpty(mValue) && !StringUtil.isEmpty(mValue)) {
					ansVal = "<a href onclick=\"detailPopupDt('" + fValue + "');return false\"target=\"_blank\"> "
							+ mValue + "</a>";

				} else {
					ansVal = "<button class='no-imgIcn'></button>";

				}
			} else {

				at.get();
			}
		} else if (dynamicReportConfigDetail.getAccessType() == 13L) {
			fValue = ReflectUtil.getObjectFieldValue(arr, String.valueOf(at.get()));

			if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
				String image = (Base64Util.encoder((byte[]) fValue));
				ansVal = "<button class='btn btn-info btn-sm' onclick='downloadFile(\"" + arr[0].toString()
						+ "\")'><span class='glyphicon glyphicon-cloud-download'></span></button>";
			}
		}else if (dynamicReportConfigDetail.getAccessType() == 16L) {
			if (arr[at.get()] != null) {
		
				fValue = getQueryForFiltersJSON(dynamicReportConfigDetail.getMethod(),
						new Object[] { arr[at.get()], branchId });
				if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
					
					String [] val =  fValue.toString().split(",");
					ansVal =("<button type='button' class='btn btn-sts' onclick='redirectToCalendarView(\""	+ val[0].toString() + "\",\""
							+ val[1].toString() + "\" )'>" + "<i class='fa fa-calendar-check-o' aria-hidden='true'></i></button>");
					
				}
			} else {
				at.get();
			}
		}
		
		else if (dynamicReportConfigDetail.getAccessType() == 14L) {
			
			if (arr[at.get()] != null) {
				List<byte[]> img = getQueryForImagesJSON(dynamicReportConfigDetail.getMethod(),
						new Object[] { arr[at.get()], branchId });
				if(!ObjectUtil.isEmpty(img)&&img.size()>0){
				img.stream().forEach(u ->{
					if(u instanceof byte[]){
						if (!ObjectUtil.isEmpty(u) && !StringUtil.isEmpty(u)) {
							String image = (Base64Util.encoder((byte[]) u));
							imgs=imgs+",~#"+image;
						} 
			}
				});
				ansVal = "<button class=\"fa fa-picture-o\"\"aria-hidden=\"true\"\" onclick='popupImages(\"" + imgs
						+ "\")'></button>";
				}else{
				
						// No Latlon
						ansVal = "<button class='no-imgIcn'></button>";
				}
					
			} else {
				at.get();
			}
		}
		if (dynamicReportConfigDetail.getDataType() != null
				&& dynamicReportConfigDetail.getDataType().equalsIgnoreCase("2")) {
			ansVal = !StringUtil.isEmpty(ansVal.toString())
					? CurrencyUtil.getDecimalFormat(Double.valueOf(ansVal.toString()), "##.00") : "0.00";

		}
		exportParams.append("~" + ansVal);
		return ansVal;
	
	}

	@SuppressWarnings("unchecked")
	private List<JSONObject> getPlottingCoordinates(Object coordinatesStr) {
		List<JSONObject> returnObjects = new ArrayList<JSONObject>();
		String coorStr = (String) coordinatesStr;
		coorStr = coorStr.substring(0, coorStr.length() - 1);
		String sp[] = coorStr.trim().split("#,");
		for (String az : sp) {
			// String coorArr[]=az.trim().split("~");
			JSONObject jsonObject = new JSONObject();
			if (az.trim() != null && !StringUtil.isEmpty(az.trim())) {
				jsonObject.put("lat", !ObjectUtil.isEmpty(az.trim().split(",")[0]) ? az.trim().split(",")[0] : "");
				jsonObject.put("lon", !ObjectUtil.isEmpty(az.trim().split(",")[1]) ? az.trim().split(",")[1] : "");
				returnObjects.add(jsonObject);
			}
		}
		return returnObjects;
	}

	public String getDaterange() {
		return daterange;
	}

	public void setDaterange(String daterange) {
		this.daterange = daterange;
	}

	public static DynamicReportConfig getDynamicReportConfig() {
		return dynamicReportConfig;
	}

	public static void setDynamicReportConfig(DynamicReportConfig dynamicReportConfig) {
		DynamicViewReportAction.dynamicReportConfig = dynamicReportConfig;
	}

	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}

	public List<DynamicReportConfigFilter> getReportConfigFilters() {
		return reportConfigFilters;
	}

	public void setReportConfigFilters(List<DynamicReportConfigFilter> reportConfigFilters) {
		this.reportConfigFilters = reportConfigFilters;
	}

	public String getMainGridCols() {
		return mainGridCols;
	}

	public void setMainGridCols(String mainGridCols) {
		this.mainGridCols = mainGridCols;
	}

	public String getFilterList() {
		return filterList;
	}

	public void setFilterList(String filterList) {
		this.filterList = filterList;
	}

	public String getGridIdentity() {
		return gridIdentity;
	}

	public void setGridIdentity(String gridIdentity) {
		this.gridIdentity = gridIdentity;
	}

	public String getGeneralDateFormat() {
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			return preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT);
		}
		return null;

	}

	public Map<String, String> getWarehouseList() {
		Map<String, String> warehouseMap = new LinkedHashMap<>();
		warehouseMap = locationService.listWarehouse().stream()
				.collect(Collectors.toMap(warehouse -> String.valueOf(warehouse.getCode()), Warehouse::getName));
		return warehouseMap;
	}

	public Map<String, String> getLotNoList() {
		Map<String, String> lotNoMap = new LinkedHashMap<>();
		lotNoMap = productDistributionService.listOfLotNoFromBaleGeneration().stream()
				.filter(f -> f != null && !StringUtil.isEmpty(f))
				.collect(Collectors.toMap(k -> k.toString(), v -> v.toString()));
		return lotNoMap;
	}

	public Map<String, String> getPlottingList() {
		Map<String, String> plot = new LinkedHashMap<>();
		plot.put(getLocaleProperty("plotStatus0"), getLocaleProperty("plotStatus0"));
		plot.put(getLocaleProperty("plotStatus1"), getLocaleProperty("plotStatus1"));
		return plot;
	}

	public Map<String, String> getBatchNoList() {
		Map<String, String> batchNoMap = new LinkedHashMap<>();
		batchNoMap = productService.listOfHeapFromBale().stream().filter(f -> f != null && !StringUtil.isEmpty(f))
				.collect(Collectors.toMap(k -> k.toString(), v -> v.toString()));
		return batchNoMap;
	}

	public Map<String, String> getSamithiList() {
		Map<String, String> warehouseMap = new LinkedHashMap<>();
		warehouseMap = locationService.listOfSamithi().stream()
				.collect(Collectors.toMap(warehouse -> String.valueOf(warehouse.getId()), Warehouse::getName));
		return warehouseMap;
	}

	public Map<String, String> getBuyerList() {
		Map<String, String> listOfBuyers = new HashMap<String, String>();
		List<Customer> customers = clientService.listOfCustomers();
		for (Customer customer : customers) {
			listOfBuyers.put(String.valueOf(customer.getId()), customer.getCustomerName());
		}
		return listOfBuyers;
	}

	public Map<String, String> getVillageList() {
		Map<String, String> villageMap = new LinkedHashMap<>();
		villageMap = locationService.listVillageIdAndName().stream()
				.collect(Collectors.toMap(k -> String.valueOf(k[0].toString()), v -> String.valueOf(v[2].toString())));
		return villageMap;
	}

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

	public Map<String, String> getFarmsList() {

		Map<String, String> farmsListMap = new LinkedHashMap<String, String>();
		List<Farm> farmsList = farmerService.listFarm();
		if (!ObjectUtil.isListEmpty(farmsList)) {
			for (Farm farm : farmsList) {
				farmsListMap.put(String.valueOf(farm.getId()), farm.getFarmName());
			}
		}
		return farmsListMap;
	}

	public Map<String, String> getAgentList() {
		Map<String, String> agentMap = new LinkedHashMap<>();
		List<Object[]> agentList = agentService.listAgentIdName();
		agentList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			agentMap.put(String.valueOf(objArr[0]), String.valueOf(objArr[1]));
		});
		return agentMap;
	}

	public Map<String, String> getProductList() {
		Map<String, String> prodMap = new HashMap<String, String>();

		List<Object[]> cat = productService.listOfProducts();
		for (Object[] obj : cat) {
			prodMap.put(String.valueOf(obj[0]), String.valueOf(obj[2]));
		}
		return prodMap;
	}

	public Map<String, String> getCategoryList() {
		Map<String, String> cateMap = new HashMap<String, String>();
		List<Object[]> cat = productService.listOfProducts();
		cat.stream().forEach(a -> {
			cateMap.put(a[4].toString(), a[3].toString());
		});
		return cateMap;
	}

	private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	public Object getFilter() {
		return filter;
	}

	public void setFilter(Object filter) {
		this.filter = filter;
	}

	public Map getEntityMap() {
		return entityMap;
	}

	public void setEntityMap(Map entityMap) {
		this.entityMap = entityMap;
	}

	public Map getOtherMap() {
		return otherMap;
	}

	public void setOtherMap(Map otherMap) {
		this.otherMap = otherMap;
	}

	public Map<String, Map> getMainMap() {
		return mainMap;
	}

	public void setMainMap(Map<String, Map> mainMap) {
		this.mainMap = mainMap;
	}

	@Override
	public void prepare() throws Exception {
		if (request.getParameter("id") != null) {
			dynamicReportConfig = clientService.findReportById(request.getParameter("id"));
			String url = "";
			if (dynamicReportConfig != null) {
				url = getLocaleProperty("report") + "~#," + dynamicReportConfig.getReport()
						+ "~dynamicViewReport_list.action?id=" + dynamicReportConfig.getId();

			}
			request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(url));
		}
	}

	public String populateCSV() throws Exception {
		dynamicReportConfig = clientService.findReportById(id);
		String fileName = dynamicReportConfig.getCsvName() != null
				&& !StringUtil.isEmpty(dynamicReportConfig.getCsvName()) ? dynamicReportConfig.getCsvName()
						: getText("csvFile");
		ESESystem preferences = preferncesService.findPrefernceById("1");
		InputStream is = getExportDataCSVStream(IExporter.EXPORT_MANUAL);
		setCsvFileName(fileName + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(csvFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(fileName, fileMap, ".csv"));

		return "csv";
	}

	public String populateXLS() throws Exception {
		dynamicReportConfig = clientService.findReportById(id);
		String fileName = dynamicReportConfig.getXlsName() != null
				&& !StringUtil.isEmpty(dynamicReportConfig.getXlsName()) ? dynamicReportConfig.getXlsName()
						: getText("xlsFile");
		ESESystem preferences = preferncesService.findPrefernceById("1");
		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(fileName + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(fileName, fileMap, ".xls"));

		return "xls";
	}

	XSSFRow row, filterRowTitle, filterRow1, filterRow2, filterRow3, filterRow4;
	XSSFRow titleRow;
	int colCount, rowCount, titleRow1 = 4, titleRow2 = 6;
	Cell cell;
	Integer cellIndex;
	Integer heade = null;
	JSONObject jss = new JSONObject();

	int mainGridIterator = 0;
	int serialNo = 1;

	public InputStream getExportDataStream(String exportType) throws IOException {
		InputStream fileInputStream;
		// dynamicReportConfig =
		// clientService.findReportByName(DynamicReportProperties.MOBILE_USER_SUMMARY_REPORT);
		if (!StringUtil.isEmpty(filterList)) {
			try {
				Type listType1 = new TypeToken<Map<String, String>>() {
				}.getType();
				Map<String, String> filtersList = new Gson().fromJson(filterList, listType1);
				if (getBranchId() != null) {
					filtersList.put("BRANCH_ID", " ='" + getBranchId() + "'");
				}
				mainMap.put(DynamicReportConfig.FILTER_FIELDS, filtersList);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		otherMap.put(DynamicReportConfig.DYNAMIC_CONFIG_DETAIL, dynamicReportConfig.getDynmaicReportConfigDetails());
		otherMap.put(DynamicReportConfig.ENTITY, dynamicReportConfig.getEntityName());
		otherMap.put(DynamicReportConfig.ALIAS, dynamicReportConfig.getAlias());
		otherMap.put(DynamicReportConfig.GROUP_PROPERTY, dynamicReportConfig.getGroupProperty());
		mainMap.put(DynamicReportConfig.OTHER_FIELD, otherMap);

		Map data = null;
		if (!ObjectUtil.isEmpty(dynamicReportConfig) && dynamicReportConfig.getFetchType() == 2L) {

			data = readProjectionDataStatic(mainMap);

		} else if (!ObjectUtil.isEmpty(dynamicReportConfig) && dynamicReportConfig.getFetchType() == 3L) {

			data = readProjectionDataView(mainMap);
		} else {
			data = readData();
		}

		List<Object[]> mainGridRows = (List<Object[]>) data.get(ROWS);
		Object objj = data.get(RECORDS);
		jss = new JSONObject();
		if (objj instanceof JSONObject) {
			JSONObject countart = (JSONObject) objj;
			totalRecords = (Integer) countart.get("count");
			if (countart.containsKey("footers")) {
				jss = (JSONObject) countart.get("footers");
			}
		}
		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;
		String fName = dynamicReportConfig.getXlsName() != null && !StringUtil.isEmpty(dynamicReportConfig.getXlsName())
				? dynamicReportConfig.getXlsName() : getText("xlsFile");
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(fName);

		/** Defining Styles */
		XSSFCellStyle headerStyle = workbook.createCellStyle();
		/*
		 * headerStyle.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
		 * headerStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		 * headerStyle.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
		 * headerStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		 * headerStyle.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);
		 * headerStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		 * headerStyle.setBorderTop(XSSFCellStyle.BORDER_MEDIUM);
		 * headerStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		 */
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		XSSFCellStyle filterStyle = workbook.createCellStyle();
		/*
		 * filterStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		 * filterStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		 * filterStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		 * filterStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		 * filterStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		 * filterStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		 * filterStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		 * filterStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		 */
		filterStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		filterStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);

		XSSFCellStyle headerLabelStyle = workbook.createCellStyle();
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

		XSSFCellStyle subGridHeader = workbook.createCellStyle();
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

		XSSFCellStyle rows = workbook.createCellStyle();
		rows.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		rows.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		rows.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		rows.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		rows.setBorderRight(XSSFCellStyle.BORDER_THIN);
		rows.setRightBorderColor(IndexedColors.BLACK.getIndex());
		rows.setBorderTop(XSSFCellStyle.BORDER_THIN);
		rows.setTopBorderColor(IndexedColors.BLACK.getIndex());

		/** Defining Fonts */
		XSSFFont font1 = workbook.createFont();
		font1.setFontHeightInPoints((short) 22);

		XSSFFont font2 = workbook.createFont();
		font2.setFontHeightInPoints((short) 16);

		XSSFFont font3 = workbook.createFont();
		font3.setFontHeightInPoints((short) 14);

		XSSFFont font4 = workbook.createFont();
		font3.setFontHeightInPoints((short) 10);

		XSSFCellStyle style1 = (XSSFCellStyle) workbook.createCellStyle();
		
		XSSFCellStyle style2 = (XSSFCellStyle) workbook.createCellStyle();

		DecimalFormat df2 = new DecimalFormat("0.00");
		
		int imgRow1 = 0;
		int imgRow2 = 4;
		int imgCol1 = 0;
		int imgCol2 = 0;

		int titleRow1 = 2;
		int titleRow2 = 5;
		int count = 0;
		rowCount = 2;
		colCount = 0;

		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, titleRow1, titleRow2));

		titleRow = sheet.createRow(rowCount++);
		titleRow.setHeight((short) 500);

		cell = titleRow.createCell(2);
		cell.setCellValue(dynamicReportConfig.getReport());
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerStyle.setFont(font1);
		cell.setCellStyle(headerStyle);

		rowCount = 8;
		Map<String, DynamicReportConfigFilter> filterFieldMap = new LinkedHashMap<>();
		dynamicReportConfig.getDynmaicReportConfigFilters().stream().forEach(df -> {
			filterFieldMap.put(df.getField(), df);
		});
		if (!StringUtil.isEmpty(filterList)) {
			try {
				Type listType1 = new TypeToken<Map<String, String>>() {
				}.getType();
				Map<String, String> filtersList = new Gson().fromJson(filterList, listType1);
				if (filtersList.entrySet().stream()
						.anyMatch(filterFieldData -> (filterFieldMap.containsKey(filterFieldData.getKey())))) {
					row = sheet.createRow(rowCount++);
					cell = row.createCell(4);
					cell.setCellValue(getLocaleProperty("filter"));
					cell.setCellStyle(filterStyle);

					filtersList.entrySet().stream()
							.filter(filterFieldData -> (filterFieldMap.containsKey(filterFieldData.getKey())))
							.forEach(filterFieldData -> {
								row = sheet.createRow(rowCount++);

								cell = row.createCell(4);
								cell.setCellValue(
										getLocaleProperty(filterFieldMap.get(filterFieldData.getKey()).getLabel()));
								cell.setCellStyle(rows);

								cell = row.createCell(5);
								if (filterFieldMap.get(filterFieldData.getKey()).getType() == 3) {
									Map<String, String> optMap = getOptions(
											filterFieldMap.get(filterFieldData.getKey()).getMethod());
									String optVal = filterFieldData.getValue();
									optVal = optVal.replaceAll("=", "").replaceAll("'", "");
									if (!StringUtil.isEmpty(optVal) && optVal.contains("~")) {
										cell.setCellValue(optMap.get(optVal.split("~")[1]));
									} else {
										cell.setCellValue(optMap.get(optVal.trim()));
									}
									cell.setCellStyle(rows);
								} else if (filterFieldMap.get(filterFieldData.getKey()).getType() == 5) {
									Map<String, String> optMap = (Map<String, String>) getQueryForFilters(
											filterFieldMap.get(filterFieldData.getKey()).getMethod(),
											new Object[] { getBranchId() });

									String optVal = filterFieldData.getValue();
									optVal = optVal.replaceAll("=", "").replaceAll("'", "");
									if (!StringUtil.isEmpty(optVal) && optVal.contains("~")) {
										cell.setCellValue(optMap.get(optVal.split("~")[1]));
									} else {
										cell.setCellValue(optMap.get(optVal.trim()));
									}
									cell.setCellStyle(rows);

								} else if (filterFieldMap.get(filterFieldData.getKey()).getType() == 4) {
									String dateOrg = filterFieldData.getValue();
									if (!StringUtil.isEmpty(dateOrg) && dateOrg.contains("~")) {
										String datStr=DateUtil.convertDateFormat(dateOrg.split("~")[1].split(" ")[0],DateUtil.DATE, DateUtil.DATE_FORMAT_2)+" | "+DateUtil.convertDateFormat(dateOrg.split("\\|")[1].split(" ")[0],DateUtil.DATE, DateUtil.DATE_FORMAT_2);
										cell.setCellValue(datStr);
									}else {
										dateOrg = dateOrg.replaceAll("between", "From");
										dateOrg = dateOrg.replace("and", "To");
										dateOrg.replaceAll("'", "");
										cell.setCellValue(dateOrg);	
									}
									cell.setCellStyle(rows);

								}

							});
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		row = sheet.createRow(rowCount++);
		row.setHeight((short) 400);

		colCount = 0;
		Map<String, Double> totMap = new LinkedHashMap<>();

		dynamicReportConfig.getDynmaicReportConfigDetails().stream()
				.filter(f -> f.getIsGridAvailabiltiy() && f.getIsFooterSum() != null && f.getIsFooterSum().equals("1"))
				.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder())).forEach(dy -> {
					totMap.put(dy.getLabelName() + "_" + dy.getOrder(), 0.0);
				});
		dynamicReportConfig.getDynmaicReportConfigDetails().stream()
				.filter(f -> f.getIsGridAvailabiltiy() && f.getIsFooterSum() != null && f.getIsFooterSum().equals("2"))
				.forEach(dy -> {
					tothead = String.valueOf(dy.getOrder());
				});

		DynamicReportConfigDetail dtt = dynamicReportConfig.getDynmaicReportConfigDetails().iterator().next();
		if (dtt.getField().contains("##")) {
			iddCol = dtt.getField().split("##").length;
		} else {
			iddCol = 0;
		}

		/* removing ID column config detail fro iterating */
		dynamicReportConfig.getDynmaicReportConfigDetails()
				.remove(dynamicReportConfig.getDynmaicReportConfigDetails().iterator().next());
		/*
		 * removing Branch Dynamic Report COnfig Detail From Iterating, s its
		 * value already added to rows
		 */
		dynamicReportConfig.getDynmaicReportConfigDetails()
				.remove(dynamicReportConfig.getDynmaicReportConfigDetails().iterator().next());
		
		cell = row.createCell(colCount++);
		cell.setCellValue("S.No");
		font2.setBoldweight((short) 5);
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		filterStyle.setFont(font2);
		/* cell.setCellStyle(filterStyle); */
		cell.setCellStyle(filterStyle);

		if (StringUtil.isEmpty(getBranchId())) {
			cell = row.createCell(colCount++);
			cell.setCellValue(getLocaleProperty("branch"));
			font2.setBoldweight((short) 5);
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(font2);
			/* cell.setCellStyle(filterStyle); */
			cell.setCellStyle(filterStyle);

		}

		dynamicReportConfig.getDynmaicReportConfigDetails().stream().filter(config -> config.getIsExportAvailabiltiy())
				.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder())).forEach(dynamicReportConfigDetail -> {
					cell = row.createCell(colCount++);
					String[] groupHeader;
					String heading = "";
					if (!StringUtil.isEmpty(dynamicReportConfigDetail.getIsGroupHeader())) {
						groupHeader = dynamicReportConfigDetail.getIsGroupHeader().split("~");
						heading = groupHeader[2] + " " + getLocaleProperty(dynamicReportConfigDetail.getLabelName());
					} else {
						heading = getLocaleProperty(dynamicReportConfigDetail.getLabelName());
					}
					cell.setCellValue(heading);
					font2.setBoldweight((short) 5);
					font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(font2);
					sheet.setColumnWidth(mainGridIterator, (15 * 550));
					mainGridIterator++;

					if (!StringUtil.isEmpty(dynamicReportConfigDetail.getIsFooterSum())
							&& dynamicReportConfigDetail.getIsFooterSum().equals("2")) {
						heade = colCount;
					}
					/* cell.setCellStyle(filterStyle); */
					cell.setCellStyle(filterStyle);

				});
		sheet.setColumnWidth(mainGridIterator++, (15 * 550));
		sheet.setColumnWidth(mainGridIterator, (15 * 550));
		mainGridRows.stream().forEach(arr -> {
			row = sheet.createRow(rowCount++);
			AtomicInteger col = new AtomicInteger(0);
			row.setHeight((short) 400);
			AtomicInteger colCountRow = new AtomicInteger(1);

			initializeMap(arr);
			
			cell = row.createCell(col.getAndIncrement());
			style1.setAlignment(CellStyle.ALIGN_CENTER);
			cell.setCellStyle(style1);
			cell.setCellValue(serialNo++);

			String parentId = "";

			if (iddCol == 0) {
				parentId = String.valueOf(arr[0]);
			} else {
				for (i = 0; i < iddCol; i++) {
					parentId = parentId + String.valueOf(arr[i]) + "-";
					if (i != 0) {
						colCountRow.getAndIncrement();
					}
				}
				parentId = StringUtil.removeLastChar(parentId, '-');

			}
			if (StringUtil.isEmpty(getBranchId())) {
				cell = row.createCell(col.getAndIncrement());
				fValue = ReflectUtil.getObjectFieldValue(arr, String.valueOf(colCountRow.getAndIncrement()));
				cell.setCellValue(getBranchesMap().get(fValue));
			} else {
				colCountRow.getAndIncrement();
			}
			dynamicReportConfig.getDynmaicReportConfigDetails().stream()
					.filter(config -> config.getIsExportAvailabiltiy())
					.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
					.forEach(dynamicReportConfigDetail -> {
						cell = row.createCell(col.getAndIncrement());

						String ansVal = getAnswer(dynamicReportConfigDetail, arr,colCountRow);
						colCountRow.incrementAndGet();
						
						if (dynamicReportConfigDetail.getAlignment() != null
								&& !StringUtil.isEmpty(dynamicReportConfigDetail.getAlignment())) {
							if(dynamicReportConfigDetail.getAlignment().equalsIgnoreCase("center")){
								style2.setAlignment(CellStyle.ALIGN_CENTER);
								cell.setCellStyle(style2);
							}
							else if (dynamicReportConfigDetail.getAlignment().equalsIgnoreCase("left")) {
								style2.setAlignment(CellStyle.ALIGN_LEFT);
								cell.setCellStyle(style2);
							}else if (dynamicReportConfigDetail.getAlignment().equalsIgnoreCase("right")) {
								style2.setAlignment(CellStyle.ALIGN_RIGHT);
								cell.setCellStyle(style2);
							} 
						}
						
						if (!ObjectUtil.isEmpty(ansVal) && !StringUtil.isEmpty(ansVal)) {
						
							if (dynamicReportConfigDetail.getDataType() != null){
								if(dynamicReportConfigDetail.getDataType().equalsIgnoreCase("2")) {
								cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
								cell.setCellValue(Double.parseDouble(CurrencyUtil.getDecimalFormat(Double.valueOf(ansVal), "##.00")));
							}else
							if (org.apache.commons.lang3.math.NumberUtils.isCreatable(ansVal)) {
								cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
								cell.setCellValue(Long.parseLong(ansVal));
						}
						 else {
							 cell.setCellValue(ansVal);
						 	}
						}else {
							 cell.setCellValue(ansVal);
						}
						}else {
						cell.setCellValue("");
					}

					});

			if (!StringUtil.isEmpty(dynamicReportConfig.getParentId())) {

				try {
					Map<String, String> filtersList = new HashMap<String, String>();
					if (subDynamicReportConfig.getAlias().contains(",")) {
						String par = subDynamicReportConfig.getAlias().split(",")[0];
						filtersList.put(par.split("=")[0] + ".id", "7~" + parentId + "");
					} else {
						filtersList.put(subDynamicReportConfig.getAlias().split("=")[0] + ".id", "7~" + parentId + "");
					}
					mainMap.put(DynamicReportConfig.FILTER_FIELDS, filtersList);

				} catch (Exception e) {
					e.printStackTrace();
				}

				otherMap.put(DynamicReportConfig.DYNAMIC_CONFIG_DETAIL,
						subDynamicReportConfig.getDynmaicReportConfigDetails());
				otherMap.put(DynamicReportConfig.ENTITY, subDynamicReportConfig.getEntityName());
				otherMap.put(DynamicReportConfig.ALIAS, subDynamicReportConfig.getAlias());
				otherMap.put(DynamicReportConfig.GROUP_PROPERTY, subDynamicReportConfig.getGroupProperty());
				mainMap.put(DynamicReportConfig.OTHER_FIELD, otherMap);

				// mainMap.put(DynamicReportConfig.FILTER_FIELDS, filtersList);
				Map subData = null;
				if (!ObjectUtil.isEmpty(subDynamicReportConfig) && subDynamicReportConfig.getFetchType() == 2L) {
					subData = readProjectionDataStatic(mainMap);
				} else {
					subData = readData();
				}

				List<Object[]> subGridRows = (List<Object[]>) subData.get(ROWS);

				if (!ObjectUtil.isListEmpty(subGridRows)) {
					row = sheet.createRow(rowCount++);
					row.setHeight((short) 300);
					AtomicInteger colCount1 = new AtomicInteger(1);
					subDynamicReportConfig.getDynmaicReportConfigDetails().stream()
							.filter(config -> config.getIsGridAvailabiltiy())
							.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
							.forEach(dynamicReportConfigDetail -> {
								cell = row.createCell(colCount1.getAndIncrement());
								cell.setCellValue(getLocaleProperty(dynamicReportConfigDetail.getLabelName()));
								font2.setBoldweight((short) 5);
								font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
								subGridHeader.setFont(font2);
								/* cell.setCellStyle(filterStyle); */
								cell.setCellStyle(subGridHeader);
							});

					// sheet.addMergedRegion(new CellRangeAddress(rowCount,
					// rowCount+subGridRows.size(), 1, colCount1.get()));
				}

				subGridRows.stream().forEach(subArr -> {
					row = sheet.createRow(rowCount++);
					row.setHeight((short) 300);
					AtomicInteger colCount2 = new AtomicInteger(0);
					cellIndex = 0;
					cell = row.createCell(cellIndex++);
					cell.setCellValue("");

					subDynamicReportConfig.getDynmaicReportConfigDetails().stream()
							.filter(config -> config.getIsGridAvailabiltiy())
							.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
							.forEach(dynamicReportConfigDetail -> {
								cell = row.createCell(cellIndex++);
								colCount2.getAndIncrement();
								cell.setCellValue(getAnswer(dynamicReportConfigDetail, subArr, colCount2));
							
								cell.setCellStyle(rows);
							});
				});

				row = sheet.createRow(rowCount++);
				row = sheet.createRow(rowCount++);
			}

		});
		if (jss != null && !jss.isEmpty() && heade != null) {
			row = sheet.createRow(rowCount++);
			cell = row.createCell(heade - 1);
			cell.setCellValue("Total");
			font4.setBoldweight((short) 5);
			font4.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(font4);
			cell.setCellStyle(filterStyle);
			jss.keySet().stream().forEach(u -> {
				cell = row.createCell(heade++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				// cell.setCellValue(u.getValue().toString());
				cell.setCellValue(jss.get(u).toString());
				font4.setBoldweight((short) 5);
				font4.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(font4);
				cell.setCellStyle(filterStyle);

			});

		}
		/*
		 * for (int i = 0; i <= colCount; i++) { sheet.autoSizeColumn(i); }
		 */

		Drawing drawing = sheet.createDrawingPatriarch();
		int pictureIdx = getPicIndex(workbook);
		XSSFClientAnchor anchor = new XSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		Picture picture = drawing.createPicture(anchor, pictureIdx);
		picture.resize();

		String makeDir = FileUtil.storeXls(id);
		String fileName = fName + fileNameDateFormat.format(new Date()) + ".xls";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		workbook.write(fileOut);
		/*
		 * InputStream stream = new FileInputStream(new File(makeDir +
		 * fileName)); fileOut.close();
		 */
		File file = new File(makeDir + fileName);
		fileInputStream = new FileInputStream(file);
		fileOut.close();

		return fileInputStream;
	}

	public String populatePDF() throws Exception {

		dynamicReportConfig = clientService.findReportById(id);
		String fileName = dynamicReportConfig.getXlsName() != null
				&& !StringUtil.isEmpty(dynamicReportConfig.getXlsName()) ? dynamicReportConfig.getXlsName()
						: getText("pdfFile");
		/*
		 * if (getCurrentTenantId().equalsIgnoreCase(ESESystem.KPF_TENANT_ID))
		 */
		ESESystem preferences = preferncesService.findPrefernceById("1");

		InputStream is = getPDFExportStream(IExporter.EXPORT_MANUAL);
		setPdfFileName(fileName + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(pdfFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(fileName, fileMap, ".pdf"));
		return "pdf";

	}

	PdfPCell pdfCell = null;
	PdfPTable table = null;
	Font cellFont = null; // font for cells.
	Font titleFont = null; // font for title text.
	Paragraph title = null; // to add title for report
	String columnHeaders = null; // to hold column headers from property
	Document document = null;

	public InputStream getPDFExportStream(String exportType)
			throws IOException, DocumentException, NoSuchFieldException, SecurityException {

		if (!StringUtil.isEmpty(filterList)) {
			try {
				Type listType1 = new TypeToken<Map<String, String>>() {
				}.getType();
				Map<String, String> filtersList = new Gson().fromJson(filterList, listType1);
				if (getBranchId() != null) {
					filtersList.put("BRANCH_ID", " ='" + getBranchId() + "'");
				}
				mainMap.put(DynamicReportConfig.FILTER_FIELDS, filtersList);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		otherMap.put(DynamicReportConfig.DYNAMIC_CONFIG_DETAIL, dynamicReportConfig.getDynmaicReportConfigDetails());
		otherMap.put(DynamicReportConfig.ENTITY, dynamicReportConfig.getEntityName());
		otherMap.put(DynamicReportConfig.ALIAS, dynamicReportConfig.getAlias());
		otherMap.put(DynamicReportConfig.GROUP_PROPERTY, dynamicReportConfig.getGroupProperty());
		mainMap.put(DynamicReportConfig.OTHER_FIELD, otherMap);
	

		Map data = null;
		if (!ObjectUtil.isEmpty(dynamicReportConfig) && dynamicReportConfig.getFetchType() == 2L) {
			//data = readProjectionData(dynamicReportConfig.getDynmaicReportConfigDetails());
			data = readProjectionDataStatic(mainMap);
		} else if (!ObjectUtil.isEmpty(dynamicReportConfig) && dynamicReportConfig.getFetchType() == 3L) {

			data = readProjectionDataView(mainMap);
		} else {
			data = readData();
		}

		List<Object[]> mainGridRows = (List<Object[]>) data.get(ROWS);
		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;
		String fName = dynamicReportConfig.getPdfName() != null && !StringUtil.isEmpty(dynamicReportConfig.getPdfName())
				? dynamicReportConfig.getPdfName() : getText("pdfFile");

		// file.
		SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		document = new Document();

		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();

		String makeDir = FileUtil.storeXls(request.getSession().getId());
		String fileName = fName + fileNameDateFormat.format(new Date()) + ".pdf";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		PdfWriter.getInstance(document, fileOut);

		document.open();

		document.open();

		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap();

		String serverFilePath = request.getSession().getServletContext().getRealPath("/");
		serverFilePath = serverFilePath.replace('\\', '/');
		String arialFontFileLocation = serverFilePath + "/fonts/ARIAL.TTF";
		arialFontFileLocation = arialFontFileLocation.replace("//", "/");
		BaseFont bf = BaseFont.createFont(arialFontFileLocation, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

		com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance(exportLogo());
		logo.scaleAbsolute(100, 50);
		// resizing logo image size.

		document.add(logo); // Adding logo in PDF file.

		// below of code for title text
		titleFont = new Font(FontFamily.HELVETICA, 14, Font.BOLD, GrayColor.GRAYBLACK);
		title = new Paragraph(new Phrase(dynamicReportConfig.getReport(), titleFont));
		title.setAlignment(Element.ALIGN_CENTER);
		document.add(title);
		document.add(
				new Paragraph(new Phrase(" ", new Font(FontFamily.HELVETICA, 15, Font.NORMAL, GrayColor.GRAYBLACK)))); // Add
		cellFont = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK); // a
		// blank
		// line
		

		Map<String, DynamicReportConfigFilter> filterFieldMap = new LinkedHashMap<>();
		dynamicReportConfig.getDynmaicReportConfigFilters().stream().forEach(df -> {
			filterFieldMap.put(df.getField(), df);
		});
		
		if (!StringUtil.isEmpty(filterList)) {
			try {
				Type listType1 = new TypeToken<Map<String, String>>() {
				}.getType();
				Map<String, String> filtersList = new Gson().fromJson(filterList, listType1);
				if (filtersList.entrySet().stream()
						.anyMatch(filterFieldData -> (filterFieldMap.containsKey(filterFieldData.getKey())))) {
				
				document.add(new Paragraph(new Phrase(getLocaleProperty(getLocaleProperty("filter")),
						new Font(FontFamily.HELVETICA, 8, Font.BOLD, GrayColor.DARK_GRAY))));
				document.add(new Paragraph(
						new Phrase(" ", new Font(FontFamily.HELVETICA, 4, Font.NORMAL, GrayColor.GRAYBLACK))));

				filtersList.entrySet().stream()
				.filter(filterFieldData -> (filterFieldMap.containsKey(filterFieldData.getKey())))
				.forEach(filterFieldData -> {

					try {

					if (filterFieldMap.get(filterFieldData.getKey()).getType() == 3) {
						Map<String, String> optMap = getOptions(
								filterFieldMap.get(filterFieldData.getKey()).getMethod());
						String optVal = filterFieldData.getValue();
						optVal = optVal.replaceAll("=", "").replaceAll("'", "");
						if (!StringUtil.isEmpty(optVal) && optVal.contains("~")) {
							document.add(new Paragraph(new Phrase(getLocaleProperty(filterFieldMap.get(filterFieldData.getKey()).getLabel())+" : "+optMap.get(optVal.split("~")[1]),
									new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK))));
							document.add(new Paragraph(
									new Phrase(" ", new Font(FontFamily.HELVETICA, 4, Font.NORMAL, GrayColor.GRAYBLACK))));
						}else{
						document.add(new Paragraph(new Phrase(getLocaleProperty(filterFieldMap.get(filterFieldData.getKey()).getLabel())+" : "+optMap.get(optVal.trim()),
								new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK))));
						document.add(new Paragraph(
								new Phrase(" ", new Font(FontFamily.HELVETICA, 4, Font.NORMAL, GrayColor.GRAYBLACK))));
						}
					}else if (filterFieldMap.get(filterFieldData.getKey()).getType() == 5) {
						Map<String, String> optMap = (Map<String, String>) getQueryForFilters(
								filterFieldMap.get(filterFieldData.getKey()).getMethod(),
								new Object[] { getBranchId() });

						String optVal = filterFieldData.getValue();
						optVal = optVal.replaceAll("=", "").replaceAll("'", "");
						if (!StringUtil.isEmpty(optVal) && optVal.contains("~")) {
							document.add(new Paragraph(new Phrase(getLocaleProperty(filterFieldMap.get(filterFieldData.getKey()).getLabel())+" : "+optMap.get(optVal.split("~")[1]),
									new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK))));
							document.add(new Paragraph(
									new Phrase(" ", new Font(FontFamily.HELVETICA, 4, Font.NORMAL, GrayColor.GRAYBLACK))));
						
						} else {
							document.add(new Paragraph(new Phrase(getLocaleProperty(filterFieldMap.get(filterFieldData.getKey()).getLabel())+" : "+optMap.get(optVal.trim()),
									new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK))));
							document.add(new Paragraph(
									new Phrase(" ", new Font(FontFamily.HELVETICA, 4, Font.NORMAL, GrayColor.GRAYBLACK))));
						}
					}
					else if (filterFieldMap.get(filterFieldData.getKey()).getType() == 4) {
						String dateOrg = filterFieldData.getValue();
					
						if (!StringUtil.isEmpty(dateOrg) && dateOrg.contains("~")) {
							String datStr=DateUtil.convertDateFormat(dateOrg.split("~")[1].split(" ")[0],DateUtil.DATE, DateUtil.DATE_FORMAT_2)+" | "+DateUtil.convertDateFormat(dateOrg.split("\\|")[1].split(" ")[0],DateUtil.DATE, DateUtil.DATE_FORMAT_2);
							document.add(new Paragraph(new Phrase(getLocaleProperty(filterFieldMap.get(filterFieldData.getKey()).getLabel())+" : "+datStr,
									new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK))));
							document.add(new Paragraph(
									new Phrase(" ", new Font(FontFamily.HELVETICA, 4, Font.NORMAL, GrayColor.GRAYBLACK))));
						}else {
							dateOrg = dateOrg.replaceAll("between", "From");
							dateOrg = dateOrg.replace("and", "To");
							dateOrg.replaceAll("'", "");
							document.add(new Paragraph(new Phrase(getLocaleProperty(filterFieldMap.get(filterFieldData.getKey()).getLabel())+" : "+DateUtil.removeDateDotZeroString(dateOrg),
									new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK))));
							document.add(new Paragraph(
									new Phrase(" ", new Font(FontFamily.HELVETICA, 4, Font.NORMAL, GrayColor.GRAYBLACK))));
						}
					}
					} catch (DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} // after
		Map<String, Double> totMap = new LinkedHashMap<>(); // title.
		dynamicReportConfig.getDynmaicReportConfigDetails().stream()
				.filter(f -> f.getIsGridAvailabiltiy() && f.getIsFooterSum() != null && f.getIsFooterSum().equals("1"))
				.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder())).forEach(dy -> {
					totMap.put(dy.getLabelName() + "_" + dy.getOrder(), 0.0);
				});
		dynamicReportConfig.getDynmaicReportConfigDetails().stream()
				.filter(f -> f.getIsGridAvailabiltiy() && f.getIsFooterSum() != null && f.getIsFooterSum().equals("2"))
				.forEach(dy -> {
					tothead = String.valueOf(dy.getOrder());
				});
		
		
		DynamicReportConfigDetail dtt = dynamicReportConfig.getDynmaicReportConfigDetails().iterator().next();
		if (dtt.getField().contains("##")) {
			iddCol = dtt.getField().split("##").length;
		} else {
			iddCol = 0;
		}
		
		/* removing ID column config detail fro iterating */
		dynamicReportConfig.getDynmaicReportConfigDetails()
				.remove(dynamicReportConfig.getDynmaicReportConfigDetails().iterator().next());
		/*
		 * removing Branch Dynamic Report COnfig Detail From Iterating, s its
		 * value already added to rows
		 */
		dynamicReportConfig.getDynmaicReportConfigDetails()
				.remove(dynamicReportConfig.getDynmaicReportConfigDetails().iterator().next());
		
		dynamicReportConfig.getDynmaicReportConfigDetails().stream()
		.filter(config -> config.getIsGridAvailabiltiy())
		.forEach(dynamicReportConfigDetail -> {
			headerRow++;
		});

		if (StringUtil.isEmpty(getBranchId())) {
		
		table = new PdfPTable(headerRow+1);
		}else {
			table = new PdfPTable(headerRow);
		}
		
		if (StringUtil.isEmpty(getBranchId())) {
		
			table.setWidthPercentage(100); // Set Table Width.
			table.getDefaultCell().setUseAscender(true);
			table.getDefaultCell().setUseDescender(true); 
			pdfCell = new PdfPCell(
					new Phrase(getLocaleProperty("branch"), cellFont));
			pdfCell.setBackgroundColor(new BaseColor(255, 255, 224));
			pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pdfCell.setNoWrap(false); // To set wrapping of text in
			table.addCell(pdfCell);

		}
		
		
		dynamicReportConfig.getDynmaicReportConfigDetails().stream().filter(config -> config.getIsExportAvailabiltiy())
				.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder())).forEach(dynamicReportConfigDetail -> {

					// cell for table.
					table.setWidthPercentage(100);
					table.setTotalWidth(100);// Set Table Width.
					table.getDefaultCell().setUseAscender(true);
					table.getDefaultCell().setUseDescender(true);

					pdfCell = new PdfPCell(
							new Phrase(getLocaleProperty(dynamicReportConfigDetail.getLabelName()), cellFont));
					pdfCell.setBackgroundColor(new BaseColor(255, 255, 224));
					pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pdfCell.setNoWrap(false); // To set wrapping of text in
												// cell.
					// cell.setColspan(3); // To add column span.
					table.addCell(pdfCell);

				});

		// columnHeaders = columnHeaders();

		// setting
		// font
		// for
		// cells.
		mainGridRows.stream().forEach(arr -> {
			AtomicInteger colCount = new AtomicInteger(1);
			
			initializeMap(arr);
			String parentId = "";

			if (iddCol == 0) {
				parentId = String.valueOf(arr[0]);
			} else {
				for (i = 0; i < iddCol; i++) {
					parentId = parentId + String.valueOf(arr[i]) + "-";
					if (i != 0) {
						colCount.getAndIncrement();
					}
				}
				parentId = StringUtil.removeLastChar(parentId, '-');

			}
			
			if (StringUtil.isEmpty(getBranchId())) {
				fValue = ReflectUtil.getObjectFieldValue(arr, String.valueOf(colCount.getAndIncrement()));
				pdfCell = new PdfPCell(new Phrase(String.valueOf(getBranchesMap().get(fValue)), cellFont));
				table.addCell(pdfCell);
			} else {
				colCount.getAndIncrement();
			}
			
			
			dynamicReportConfig.getDynmaicReportConfigDetails().stream()
					.filter(config -> config.getIsExportAvailabiltiy())
					.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
					.forEach(dynamicReportConfigDetail -> {
						
						String ansVal = getAnswer(dynamicReportConfigDetail, arr,colCount);
						colCount.incrementAndGet();
					
						if (!ObjectUtil.isEmpty(ansVal) && !StringUtil.isEmpty(ansVal)) {
							pdfCell = new PdfPCell(new Phrase(String.valueOf(ansVal), cellFont));
						} else {
							pdfCell = new PdfPCell(new Phrase("NA", cellFont));
						}

						if (dynamicReportConfigDetail.getAlignment() != null
								&& !StringUtil.isEmpty(dynamicReportConfigDetail.getAlignment())) {
							if(dynamicReportConfigDetail.getAlignment().equalsIgnoreCase("center")){
								pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							}
							else if (dynamicReportConfigDetail.getAlignment().equalsIgnoreCase("left")) {
								pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
							}else if (dynamicReportConfigDetail.getAlignment().equalsIgnoreCase("right")) {
								pdfCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							} 
						}
						
						table.addCell(pdfCell);
						
					});

		});
		document.add(table);
		/*
		 * mainGridRows.stream().forEach(arr -> {
		 * 
		 * dynamicReportConfig.getDynmaicReportConfigDetails().stream()
		 * .filter(config -> config.getIsExportAvailabiltiy()) .sorted((f1, f2)
		 * -> Long.compare(f1.getOrder(), f2.getOrder()))
		 * .forEach(dynamicReportConfigDetail -> {
		 * 
		 * // BEGIN OF CODE FOR A PARTICULAR CELL IN A ROW OF TABLE TO // PDF
		 * FILE.
		 * 
		 * if (entityField != null) { cell = new PdfPCell(new Phrase(
		 * StringUtil.isEmpty(entityField.toString()) ? getText("NA") :
		 * entityField.toString(), cellFont)); table.addCell(cell); } else {
		 * cell = new PdfPCell(new Phrase("", cellFont)); table.addCell(cell); }
		 * // END OF CODE FOR A PARTICULAR CELL IN A ROW OF TABLE TO // PDF
		 * FILE.
		 * 
		 * });
		 * 
		 * // Add table to document.
		 * 
		 * });
		 */
		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		document.close();
		fileOut.close();
		return stream;
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

	public int getPicIndex(HSSFWorkbook wb) throws IOException {

		int index = -1;

		byte[] picData = null;
		picData = clientService.findLogoByCode(Asset.APP_LOGO);

		if (picData != null)
			index = wb.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
	}

	public int getPicIndex(XSSFWorkbook workbook) throws IOException {

		int index = -1;

		byte[] picData = null;
		picData = clientService.findLogoByCode(Asset.APP_LOGO);

		if (picData != null)
			index = workbook.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
	}

	public String getFooterSumCols() {
		return footerSumCols;
	}

	public void setFooterSumCols(String footerSumCols) {
		this.footerSumCols = footerSumCols;
	}

	public String getMainGridColNames() {
		return mainGridColNames;
	}

	public void setMainGridColNames(String mainGridColNames) {
		this.mainGridColNames = mainGridColNames;
	}

	public String getFooterTotCol() {
		return footerTotCol;
	}

	public void setFooterTotCol(String footerTotCol) {
		this.footerTotCol = footerTotCol;
	}

	public Map<String, String> getSeasonsList() {

		Map<String, String> seasonMap = new LinkedHashMap<String, String>();

		List<Object[]> seasonList = farmerService.listSeasonCodeAndName();

		for (Object[] obj : seasonList) {
			seasonMap.put(String.valueOf(obj[0]), String.valueOf(obj[1]));
		}
		return seasonMap;

	}

	@SuppressWarnings("restriction")
	public String populateImage() {
		BufferedImage image = null;
		byte[] imageByte;
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			imageByte = decoder.decodeBuffer("gdr");
			ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
			image = ImageIO.read(bis);
			bis.close();

			// write the image to a file
			File outputfile = new File("image.png");
			ImageIO.write(image, "png", outputfile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public Map<String, String> getFarmerFirstNameList() {
		Map<String, String> farmerMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(farmerList)) {
			farmerList = farmerService.listFarmerInfo();
		}
		farmerList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			farmerMap.put(String.valueOf(objArr[1]), String.valueOf(objArr[3]));
		});

		return farmerMap;
	}

	public void initializeMap(Object obj) {
	
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		AtomicInteger runCount = new AtomicInteger(0);
		if (iddCol == 0) {
			runCount.getAndIncrement();
			// runCount.getAndIncrement();
		} else {
			runCount.getAndAdd(iddCol-1);

		}
		dynamic_report_config_detail_ID.put(
				runCount.getAndIncrement(),-1l);
		dynamic_report_config_detail_ID.put(
				runCount.getAndIncrement(),-1l);
		
		if (obj instanceof Object[]) {
			Object[] arr = (Object[]) obj;

			dynamicReportConfig.getDynmaicReportConfigDetails().stream()
					.filter(config -> config.getIsGridAvailabiltiy())
					.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
					.forEach(dynamicReportConfigDetail -> {
						dynamic_report_config_detail_ID.put(runCount.getAndIncrement(),
								dynamicReportConfigDetail.getId());
					});
		}
	}

	String footersum = "";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected String sendJSONResponse(Map data) throws Exception {

		JSONObject gridData = new JSONObject();
		gridData.put(PAGE, getPage());
		Object objj = data.get(RECORDS);
		footersum = "";
		if (objj instanceof JSONObject) {
			JSONObject countart = (JSONObject) objj;
			totalRecords = (Integer) countart.get("count");
			if (countart.containsKey("footers")) {
				gridData.put("footersum", countart.get("footers"));
			}
		} else {
			totalRecords = (Integer) data.get(RECORDS);
		}
		gridData.put(TOTAL, java.lang.Math.ceil(totalRecords / Double.valueOf(Integer.toString(getLimit()))));
		gridData.put(RECORDS, totalRecords);
		List list = (List) data.get(ROWS);
		JSONArray rows = new JSONArray();
		if (list != null) {
			branchIdValue = getBranchId();
			if (StringUtil.isEmpty(branchIdValue)) {
				buildBranchMap();
			}
			for (Object record : list) {
				initializeMap(record);
				rows.add(toJSON(record));
			}
		}
		if (totalRecords > 0) {
			gridData.put("userdata", userDataToJSON());
		} else {
			gridData.put("userdata", userDataToJSON());
		}
		gridData.put(ROWS, rows);
		//
		PrintWriter out = response.getWriter();
		out.println(gridData.toString());

		return null;
	}

	@SuppressWarnings("unchecked")
	public String methodQuery() {
		try {
			List<Object[]> Datas = farmerService.getValueListByQuery(query, param, getBranchId());
			List<JSONObject> jsonObjects = new ArrayList<JSONObject>();

			JSONObject jsonObj = new JSONObject();
			Datas.stream().forEach(da -> {
				JSONArray jsonArr = new JSONArray();
				for (i = 0; i < da.length; i++) {
					if (da[i] != null) {
						jsonArr.add(i, da[i].toString());
					} else {
						jsonArr.add(i, "");
					}
				}
				jsonObj.put(j, jsonArr);
				j++;

			});
			System.out.println(jsonObj);
			/*
			 * for(i=0;i<array.length;i++){ Datas.stream().forEach(farmdet -> {
			 * JSONObject jsonObject=new JSONObject(); jsonObject.put(array[i],
			 * farmdet[i].toString()); //jsonObject.put(array[1],
			 * farmdet[1].toString()); //jsonObject.put(array[2],
			 * farmdet[2].toString());
			 * 
			 * jsonObjects.add(jsonObject); }); }
			 */
			printAjaxResponse(jsonObj, "text/html");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getFetchType() {
		return fetchType;
	}

	public void setFetchType(String fetchType) {
		this.fetchType = fetchType;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Object[] getParam() {
		return param;
	}

	public void setParam(Object[] param) {
		this.param = param;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getGroupHeader() {
		return groupHeader;
	}

	public void setGroupHeader(String groupHeader) {
		this.groupHeader = groupHeader;
	}

	public String populateSingleRecordPDF() throws Exception {

		dynamicReportConfig = clientService.findReportById(id);
		String fileName = dynamicReportConfig.getXlsName() != null
				&& !StringUtil.isEmpty(dynamicReportConfig.getXlsName()) ? dynamicReportConfig.getXlsName()
						: getText("pdfFile");
		InputStream is = getPDFSingleExportStream(IExporter.EXPORT_MANUAL);
		setPdfFileName(fileName + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(pdfFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(fileName, fileMap, ".pdf"));
		return "pdf";
	}

	public InputStream getPDFSingleExportStream(String exportType)
			throws IOException, DocumentException, NoSuchFieldException, SecurityException {

		if (!StringUtil.isEmpty(filtersList) && !StringUtil.isEmpty(id)) {
			selectedFields = (filtersList.substring(1)).split("~");
		}

		if (StringUtil.isEmpty(selectedFields))
			return null;

		String fName = dynamicReportConfig.getPdfName() != null && !StringUtil.isEmpty(dynamicReportConfig.getPdfName())
				? dynamicReportConfig.getPdfName() : getText("pdfFile");

		SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		document = new Document();
		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
		String makeDir = FileUtil.storeXls(request.getSession().getId());
		String fileName = fName + fileNameDateFormat.format(new Date()) + ".pdf";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		PdfWriter.getInstance(document, fileOut);

		document.open();

		branchIdValue = getBranchId();
		buildBranchMap();

		com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance(exportLogo());
		logo.scaleAbsolute(100, 50);

		document.add(logo);

		titleFont = new Font(FontFamily.HELVETICA, 20, Font.BOLD, GrayColor.GRAYBLACK);
		title = new Paragraph(new Phrase(dynamicReportConfig.getReport(), titleFont));
		title.setAlignment(Element.ALIGN_CENTER);
		document.add(title);
		document.add(
				new Paragraph(new Phrase(" ", new Font(FontFamily.HELVETICA, 15, Font.NORMAL, GrayColor.GRAYBLACK))));
		cellFont = new Font(FontFamily.HELVETICA, 14, Font.NORMAL, GrayColor.GRAYBLACK);

		if (selectedFields != null && !StringUtil.isEmpty(selectedFields)) {
			try {

				document.add(new Paragraph(
						new Phrase(" ", new Font(FontFamily.HELVETICA, 4, Font.NORMAL, GrayColor.GRAYBLACK))));

				table = new PdfPTable(2);
				AtomicInteger i = new AtomicInteger(0);
				Map<String, String> dataMap = new LinkedHashMap<>();
				dynamicReportConfig.getDynmaicReportConfigDetails().stream()
						.filter(config -> config.getIsExportAvailabiltiy())
						.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
						.forEach(dynamicReportConfigDetail -> {
							String[] groupHeader;
							String heading;
							if (!StringUtil.isEmpty(dynamicReportConfigDetail.getIsGroupHeader())) {
								groupHeader = dynamicReportConfigDetail.getIsGroupHeader().split("~");
								heading = groupHeader[2] + " "
										+ getLocaleProperty(dynamicReportConfigDetail.getLabelName());
							} else {
								heading = getLocaleProperty(dynamicReportConfigDetail.getLabelName());
							}
							dataMap.put(heading, selectedFields[i.getAndIncrement()]);

							/*
							 * table.setWidthPercentage(100);
							 * table.getDefaultCell().setUseAscender(true);
							 * table.getDefaultCell().setUseDescender(true);
							 * 
							 * 
							 * if(!StringUtil.isEmpty(dynamicReportConfigDetail.
							 * getIsGroupHeader())){ groupHeader =
							 * dynamicReportConfigDetail.getIsGroupHeader().
							 * split("~");
							 * heading=groupHeader[2]+" "+getLocaleProperty(
							 * dynamicReportConfigDetail.getLabelName()); }else{
							 * heading=getLocaleProperty(
							 * dynamicReportConfigDetail.getLabelName()); }
							 * 
							 * pdfCell = new PdfPCell( new Phrase(heading,
							 * cellFont)); pdfCell.setBackgroundColor(new
							 * BaseColor(255, 255, 224));
							 * pdfCell.setHorizontalAlignment(Element.
							 * ALIGN_CENTER); pdfCell.setNoWrap(false);
							 * table.addCell(pdfCell);
							 */

						});
				dataMap.entrySet().stream().forEach(d -> {
					pdfCell = new PdfPCell(new Phrase(d.getKey(), cellFont));
					pdfCell.setBackgroundColor(new BaseColor(255, 255, 224));
					pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pdfCell.setNoWrap(false);
					table.addCell(pdfCell);
					if (d.getValue() != null && !StringUtil.isEmpty(d.getValue())) {
						pdfCell = new PdfPCell(new Phrase(d.getValue().toString(), cellFont));
					} else {
						pdfCell = new PdfPCell(new Phrase("NA", cellFont));
					}
					table.addCell(pdfCell);
				});

				/*
				 * for(int i =0 ; i<selectedFields.length ;i++){ if
				 * (!StringUtil.isEmpty(selectedFields[i])) { pdfCell = new
				 * PdfPCell(new Phrase(String.valueOf(selectedFields[i]),
				 * cellFont)); } else { pdfCell = new PdfPCell(new Phrase("NA",
				 * cellFont)); } table.addCell(pdfCell); }
				 */
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		document.add(table);
		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		document.close();
		fileOut.close();
		return stream;

	}

	public String getFiltersList() {
		return filtersList;
	}

	public void setFiltersList(String filtersList) {
		this.filtersList = filtersList;
	}

	public String getCsvFileName() {
		return csvFileName;
	}

	public void setCsvFileName(String csvFileName) {
		this.csvFileName = csvFileName;
	}

	public InputStream getExportDataCSVStream(String exportType) throws IOException {
		InputStream fileInputStream;
		// dynamicReportConfig =
		// clientService.findReportByName(DynamicReportProperties.MOBILE_USER_SUMMARY_REPORT);
		if (!StringUtil.isEmpty(filterList)) {
			try {
				Type listType1 = new TypeToken<Map<String, String>>() {
				}.getType();
				Map<String, String> filtersList = new Gson().fromJson(filterList, listType1);
				if (getBranchId() != null) {
					filtersList.put("BRANCH_ID", " ='" + getBranchId() + "'");
				}
				mainMap.put(DynamicReportConfig.FILTER_FIELDS, filtersList);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		otherMap.put(DynamicReportConfig.DYNAMIC_CONFIG_DETAIL, dynamicReportConfig.getDynmaicReportConfigDetails());
		otherMap.put(DynamicReportConfig.ENTITY, dynamicReportConfig.getEntityName());

		mainMap.put(DynamicReportConfig.OTHER_FIELD, otherMap);

		Map data = null;
		if (!ObjectUtil.isEmpty(dynamicReportConfig) && dynamicReportConfig.getFetchType() == 2L) {
			data = readProjectionData(dynamicReportConfig.getDynmaicReportConfigDetails());
		} else if (!ObjectUtil.isEmpty(dynamicReportConfig) && dynamicReportConfig.getFetchType() == 3L) {

			data = readProjectionDataView(mainMap);
		} else {
			data = readData();
		}

		List<Object[]> mainGridRows = (List<Object[]>) data.get(ROWS);
		Object objj = data.get(RECORDS);
		jss = new JSONObject();
		if (objj instanceof JSONObject) {
			JSONObject countart = (JSONObject) objj;
			totalRecords = (Integer) countart.get("count");
			if (countart.containsKey("footers")) {
				jss = (JSONObject) countart.get("footers");
			}
		}
		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;
		String fName = dynamicReportConfig.getXlsName() != null && !StringUtil.isEmpty(dynamicReportConfig.getXlsName())
				? dynamicReportConfig.getXlsName() : getText("xlsFile");
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(fName);

		/** Defining Styles */
		XSSFCellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
		headerStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		headerStyle.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
		headerStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		headerStyle.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);
		headerStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		headerStyle.setBorderTop(XSSFCellStyle.BORDER_MEDIUM);
		headerStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		XSSFCellStyle filterStyle = workbook.createCellStyle();
		filterStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		filterStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		filterStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		filterStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		filterStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		filterStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		filterStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		filterStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		filterStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		filterStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);

		XSSFCellStyle headerLabelStyle = workbook.createCellStyle();
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

		XSSFColor subGridColor = new XSSFColor(new Color(207, 225, 247));
		filterStyle.setFillForegroundColor(subGridColor);

		XSSFCellStyle subGridHeader = workbook.createCellStyle();
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

		XSSFCellStyle rows = workbook.createCellStyle();
		rows.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		rows.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		rows.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		rows.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		rows.setBorderRight(XSSFCellStyle.BORDER_THIN);
		rows.setRightBorderColor(IndexedColors.BLACK.getIndex());
		rows.setBorderTop(XSSFCellStyle.BORDER_THIN);
		rows.setTopBorderColor(IndexedColors.BLACK.getIndex());

		/** Defining Fonts */
		XSSFFont font1 = workbook.createFont();
		font1.setFontHeightInPoints((short) 22);

		XSSFFont font2 = workbook.createFont();
		font2.setFontHeightInPoints((short) 16);

		XSSFFont font3 = workbook.createFont();
		font3.setFontHeightInPoints((short) 14);

		XSSFFont font4 = workbook.createFont();
		font3.setFontHeightInPoints((short) 10);

		int imgRow1 = 0;
		int imgRow2 = 2;
		int imgCol1 = 0;
		int imgCol2 = 1;

		int titleRow1 = 2;
		int titleRow2 = 5;
		int count = 0;
		rowCount = 2;
		colCount = 1;

		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowCount + 1, rowCount + 1, titleRow1, titleRow2));

		titleRow = sheet.createRow(rowCount++);
		titleRow.setHeight((short) 800);

		cell = titleRow.createCell(4);
		cell.setCellValue(dynamicReportConfig.getReport());
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerStyle.setFont(font1);
		cell.setCellStyle(headerStyle);

		rowCount = 6;
		Map<String, DynamicReportConfigFilter> filterFieldMap = new LinkedHashMap<>();
		dynamicReportConfig.getDynmaicReportConfigFilters().stream().forEach(df -> {
			filterFieldMap.put(df.getField(), df);
		});
		if (!StringUtil.isEmpty(filterList)) {
			try {
				Type listType1 = new TypeToken<Map<String, String>>() {
				}.getType();
				Map<String, String> filtersList = new Gson().fromJson(filterList, listType1);
				if (filtersList.entrySet().stream()
						.anyMatch(filterFieldData -> (filterFieldMap.containsKey(filterFieldData.getKey())))) {
					row = sheet.createRow(rowCount++);
					cell = row.createCell(4);
					cell.setCellValue(getLocaleProperty("filter"));
					cell.setCellStyle(filterStyle);

					filtersList.entrySet().stream()
							.filter(filterFieldData -> (filterFieldMap.containsKey(filterFieldData.getKey())))
							.forEach(filterFieldData -> {
								row = sheet.createRow(rowCount++);

								cell = row.createCell(4);
								cell.setCellValue(
										getLocaleProperty(filterFieldMap.get(filterFieldData.getKey()).getLabel()));
								cell.setCellStyle(rows);

								cell = row.createCell(5);
								if (filterFieldMap.get(filterFieldData.getKey()).getType() == 3
										|| filterFieldMap.get(filterFieldData.getKey()).getType() == 5) {
									Map<String, String> optMap = getOptions(
											filterFieldMap.get(filterFieldData.getKey()).getMethod());
									String optVal = filterFieldData.getValue();
									optVal = optVal.replaceAll("=", "").replaceAll("'", "");
									cell.setCellValue(optMap.get(optVal.trim()));
									cell.setCellStyle(rows);
								} else if (filterFieldMap.get(filterFieldData.getKey()).getType() == 4) {
									String dateOrg = filterFieldData.getValue();
									dateOrg = dateOrg.replaceAll("between", "From");
									dateOrg = dateOrg.replace("and", "To");
									dateOrg.replaceAll("'", "");
									cell.setCellValue(dateOrg);
									cell.setCellStyle(rows);

								}

							});
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		row = sheet.createRow(rowCount++);
		row.setHeight((short) 800);

		colCount = 0;
		Map<String, Double> totMap = new LinkedHashMap<>();

		dynamicReportConfig.getDynmaicReportConfigDetails().stream()
				.filter(f -> f.getIsGridAvailabiltiy() && f.getIsFooterSum() != null && f.getIsFooterSum().equals("1"))
				.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder())).forEach(dy -> {
					totMap.put(dy.getLabelName() + "_" + dy.getOrder(), 0.0);
				});
		dynamicReportConfig.getDynmaicReportConfigDetails().stream()
				.filter(f -> f.getIsGridAvailabiltiy() && f.getIsFooterSum() != null && f.getIsFooterSum().equals("2"))
				.forEach(dy -> {
					tothead = String.valueOf(dy.getOrder());
				});

		/* removing ID column config detail fro iterating */
		dynamicReportConfig.getDynmaicReportConfigDetails()
				.remove(dynamicReportConfig.getDynmaicReportConfigDetails().iterator().next());
		/*
		 * removing Branch Dynamic Report COnfig Detail From Iterating, s its
		 * value already added to rows
		 */
		dynamicReportConfig.getDynmaicReportConfigDetails()
				.remove(dynamicReportConfig.getDynmaicReportConfigDetails().iterator().next());

		if (StringUtil.isEmpty(getBranchId())) {
			cell = row.createCell(colCount++);
			cell.setCellValue(getLocaleProperty("branchId"));
			font2.setBoldweight((short) 5);
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(font2);
			/* cell.setCellStyle(filterStyle); */
			cell.setCellStyle(filterStyle);

		}

		dynamicReportConfig.getDynmaicReportConfigDetails().stream().filter(config -> config.getIsExportAvailabiltiy())
				.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder())).forEach(dynamicReportConfigDetail -> {
					cell = row.createCell(colCount++);
					String[] groupHeader;
					String heading = "";
					if (!StringUtil.isEmpty(dynamicReportConfigDetail.getIsGroupHeader())) {
						groupHeader = dynamicReportConfigDetail.getIsGroupHeader().split("~");
						heading = groupHeader[2] + " " + getLocaleProperty(dynamicReportConfigDetail.getLabelName());
					} else {
						heading = getLocaleProperty(dynamicReportConfigDetail.getLabelName());
					}
					cell.setCellValue(heading);
					font2.setBoldweight((short) 5);
					font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(font2);

					if (!StringUtil.isEmpty(dynamicReportConfigDetail.getIsFooterSum())
							&& dynamicReportConfigDetail.getIsFooterSum().equals("2")) {
						heade = colCount;
					}
					/* cell.setCellStyle(filterStyle); */
					cell.setCellStyle(filterStyle);

				});
		mainGridRows.stream().forEach(arr -> {
			row = sheet.createRow(rowCount++);
			AtomicInteger col = new AtomicInteger(0);
			row.setHeight((short) 600);
			AtomicInteger colCount = new AtomicInteger(1);

			if (StringUtil.isEmpty(getBranchId())) {
				cell = row.createCell(col.getAndIncrement());
				fValue = ReflectUtil.getObjectFieldValue(arr, String.valueOf(colCount.getAndIncrement()));
				cell.setCellValue(getBranchesMap().get(fValue));
			} else {
				colCount.getAndIncrement();
			}

			dynamicReportConfig.getDynmaicReportConfigDetails().stream()
					.filter(config -> config.getIsExportAvailabiltiy())
					.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
					.forEach(dynamicReportConfigDetail -> {
						cell = row.createCell(col.getAndIncrement());

						if (dynamicReportConfigDetail.getAccessType() == 1L) {
							fValue = ReflectUtil.getObjectFieldValue(arr, String.valueOf(colCount.getAndIncrement()));
							if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
								if (fValue instanceof Long) {
									cell.setCellValue((Long) fValue);
								} else if (fValue instanceof Double) {
									cell.setCellValue((Double) fValue);
								} else {
									cell.setCellValue(fValue.toString());
								}
							} else {
								cell.setCellValue("");
							}
						} else if (dynamicReportConfigDetail.getAccessType() == 2L) {
							fValue = ReflectUtil.getObjectFieldValue(arr, String.valueOf(colCount));
							if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
								mValue = getMethodValue(dynamicReportConfigDetail.getMethod(), fValue.toString());
								if (!ObjectUtil.isEmpty(mValue) && !StringUtil.isEmpty(mValue)) {
									if (fValue instanceof Integer) {
										if (mValue instanceof String) {
											cell.setCellValue(mValue.toString());
										} else {
											cell.setCellValue((Integer) mValue);
										}
									} else {
										cell.setCellValue(mValue.toString());
									}
								} else {
									cell.setCellValue("");
								}
							}

						} else if (dynamicReportConfigDetail.getAccessType() == 3L) {
							fValue = ReflectUtil.getObjectFieldValue(arr, dynamicReportConfigDetail.getParameters());
							if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
								mValue = getMethodValue(dynamicReportConfigDetail.getMethod(), fValue);
								if (!ObjectUtil.isEmpty(mValue) && !StringUtil.isEmpty(mValue)) {
									cell.setCellValue((Integer) mValue);
								} else {
									cell.setCellValue(mValue.toString());
								}
							} else {
								cell.setCellValue("");
							}

						} else if (dynamicReportConfigDetail.getAccessType() == 9L) {
							fValue = ReflectUtil.getObjectFieldValue(arr, String.valueOf(colCount.getAndIncrement()));
							if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
								if (fValue instanceof Long) {
									cell.setCellValue((Long) fValue);
								} else if (fValue instanceof Double) {
									cell.setCellValue((Double) fValue);
								} else {
									cell.setCellValue(fValue.toString());
								}
							} else {
								cell.setCellValue("");
							}
						}

					});

		});
		if (jss != null && !jss.isEmpty() && heade != null) {
			row = sheet.createRow(rowCount++);
			cell = row.createCell(heade - 1);
			cell.setCellValue("Total");
			font4.setBoldweight((short) 5);
			font4.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(font4);
			cell.setCellStyle(filterStyle);
			jss.keySet().stream().forEach(u -> {
				cell = row.createCell(heade++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				// cell.setCellValue(u.getValue().toString());
				cell.setCellValue(jss.get(u).toString());
				font4.setBoldweight((short) 5);
				font4.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(font4);
				cell.setCellStyle(filterStyle);

			});

		}
		for (int i = 0; i <= colCount; i++) {
			sheet.autoSizeColumn(i);
		}

		// alternateGreenAndWhiteRows(sheet);

		Drawing drawing = sheet.createDrawingPatriarch();
		int pictureIdx = getPicIndex(workbook);
		XSSFClientAnchor anchor = new XSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		Picture picture = drawing.createPicture(anchor, pictureIdx);
		picture.resize(2.3);

		String makeDir = FileUtil.storeXls(id);
		String fileName = fName + fileNameDateFormat.format(new Date()) + ".csv";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		workbook.write(fileOut);
		/*
		 * InputStream stream = new FileInputStream(new File(makeDir +
		 * fileName)); fileOut.close();
		 */
		File file = new File(makeDir + fileName);
		fileInputStream = new FileInputStream(file);
		fileOut.close();

		return fileInputStream;
	}

	public String populateHTML() {
		// setHtmlContent(htmlContent);
		PrintWriter pw;
		try {
			pw = response.getWriter();
			pw.print(htmlContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "html";
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

	public String populateDownloadCropYield() {

		try {
			CropYield cy = farmerService.findCropYieldById(Long.valueOf(id));
			String fileName = cy.getLandHolding().replaceAll("\\s+", "");
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName + "." + "xlsx");
			response.getOutputStream().write(cy.getImage());
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	public String populateDownload() {

		try {
			ColdStorageDetail dc = farmerService.findColdStorageDetailById(Long.valueOf(csId));
			// ColdStorage cs = farmerService.findColdStorageById(csId);
			String documentName;
			if (dc.getColdStorage().getFarmer() != null) {
				documentName = dc.getColdStorage().getFarmer().getFirstName() + " - "
						+ dc.getColdStorage().getFarmer().getLastName();
			} else {
				FarmerDynamicFieldsValue fdfv = farmerService
						.findFarmerDynamicFieldsValueById(dc.getColdStorage().getBatchNo());
				documentName = fdfv != null ? fdfv.getFieldValue() : "";
			}

			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=" + documentName + new Date() + "." + "pdf");
			response.getOutputStream().write(dc.getColdStorage().getImg());
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	public String getCsId() {
		return csId;
	}

	public void setCsId(String csId) {
		this.csId = csId;
	}

	public IFarmCropsService getFarmCropsService() {
		return farmCropsService;
	}

	public void setFarmCropsService(IFarmCropsService farmCropsService) {
		this.farmCropsService = farmCropsService;
	}

	public String moleculeData() throws Exception {

		CropYieldDetail filter = new CropYieldDetail();

		// if (!StringUtil.isEmpty(searchRecord.get("landHolding"))) {
		filter.setMoleculeValue(lotCode);
		// }

		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		// return sendJQGridJSONResponse(data);
		return sendJSONResponse(data);

	}

	public String cropDetail() throws Exception {
		List<JSONObject> jsonObjectsCoor = new ArrayList<JSONObject>();
		String view = LIST;
		request.setAttribute(HEADING, getText(LIST));
		if (id != null && !id.equals("")) {

			FarmerDynamicFieldsValue fdfv = farmerService.findFarmerDynamicFieldsValueById(id);
			farmerDynamicDataId = fdfv.getFarmerDynamicData().getId();
			/*
			 * if(!StringUtil.isEmpty(farmCropId)){ String[] cropId =
			 * farmCropId.split("-"); farmCrops =
			 * farmCropsService.findFarmCropsById(Long.parseLong(cropId[0]));
			 * farmDetail(farmCrops.getFarm().getId()); }
			 */

			if (!StringUtil.isEmpty(farmCropId)) {
				String[] cropId = farmCropId.split("-");
				farmCrop = farmCropsService.findFarmCropsById(Long.parseLong(cropId[0]));
			}
			// String[] farmId = farmCropId.split("-");
			/*
			 * farmCrops =
			 * farmCropsService.findFarmCropsBy(Long.parseLong(farmId[0]));
			 * farmDetail(farmCrops.getFarm().getId());
			 */
			// List<FarmCrops> farmCropList = new ArrayList<FarmCrops>();
			List<Long> farmIds = (farmerService.listFarmByFarmerId(Long.parseLong(fdfv.getReferenceId()))).stream()
					.map(u -> u.getId()).collect(Collectors.toList());
			if (!StringUtil.isEmpty(season)) {
				String[] seas = season.split("-");
				// farmCrop =
				// farmCropsService.findFarmCropsById(Long.parseLong(seas[0]));
				seasons = farmerService.findSeasonNameByCode(seas[0].toString());
			}

			farmCrops = farmerService.listOfCropsByFarmIdAndSeason(farmIds, seasons.getId());

			farmCrops.stream().forEach(obj -> {
				JSONObject jsonObject = new JSONObject();

				Farm farm = farmerService.findFarmByfarmId(Long.valueOf(obj.getFarm().getId()));
				if (farm.getActiveCoordinates() != null && farm.getActiveCoordinates().getFarmCoordinates() != null
						&& !ObjectUtil.isEmpty(farm.getActiveCoordinates())
						&& !ObjectUtil.isListEmpty(farm.getActiveCoordinates().getFarmCoordinates())) {
					jsonObjectList = getFarmJSONObjects(farm.getActiveCoordinates().getFarmCoordinates(),
							farm.getFarmName());
				} else {
					jsonObjectList = new ArrayList();
				}
				jsonObject.put("jsonObjectList", jsonObjectList);
				jsonObjectsCoor.add(jsonObject);
			});
			jsonObjectList = jsonObjectsCoor;

			/*
			 * if (farmCrop.getActiveCoordinates()!=null &&
			 * farmCrop.getActiveCoordinates().getFarmCropsCoordinates() != null
			 * && !ObjectUtil.isListEmpty(farmCrop.getActiveCoordinates().
			 * getFarmCropsCoordinates())) { jsonObjectList =
			 * getFarmCropsJSONObjects(farmCrop.getActiveCoordinates().
			 * getFarmCropsCoordinates()); }
			 */
			// farmCrop.addAll(farmCropList);

			farmerDetail(Long.valueOf(fdfv.getReferenceId()));

			/*
			 * if (farmCrops == null) { addActionError(NO_RECORD); return
			 * REDIRECT; }
			 */
			// setCropInfoEnabled(preferences.getPreferences().get(ESESystem.ENABLE_CROP_INFO));
			// setFarmId(String.valueOf(farmCrops.getFarm().getId()));
			// setFarmerId(String.valueOf(farmCrops.getFarm().getFarmer().getId()));

			ESESystem preferences = preferncesService.findPrefernceById("1");
			setCropInfoEnabled(preferences.getPreferences().get(ESESystem.ENABLE_CROP_INFO));
			if (!ObjectUtil.isEmpty(preferences)) {
				DateFormat genDate = new SimpleDateFormat(
						preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));

				/*
				 * FarmCatalogue typ
				 * =getCatlogueValueByCode(farmCrops.getType()); if
				 * (!ObjectUtil.isEmpty(typ) && typ != null) { if
				 * (!StringUtil.isEmpty(typ.getCode())&& typ.getCode()!=null &&
				 * typ.getCode().equals("99")) { farmCrops.setType(typ.getName()
				 * + ": " + (!ObjectUtil.isEmpty(farmCrops.getOtherType()) ?
				 * farmCrops.getOtherType() : "")); } else {
				 * farmCrops.setType(typ.getName()); } } else {
				 * farmCrops.setType(""); }
				 * if(!StringUtil.isEmpty(farmCrops.getSeedSource())){
				 * FarmCatalogue seedSource
				 * =getCatlogueValueByCode(farmCrops.getSeedSource()); if
				 * (!ObjectUtil.isEmpty(seedSource) && seedSource != null) {
				 * 
				 * farmCrops.setSeedSource(!StringUtil.isEmpty(seedSource.
				 * getName()) ? seedSource.getName() :"NA");
				 * 
				 * } }
				 * 
				 * 
				 * if (!StringUtil.isEmpty(farmCrops.getCropCategoryList()) &&
				 * farmCrops.getCropCategoryList() != "-1") { if
				 * (farmCrops.getCropCategoryList() != null &&
				 * farmCrops.getCropCategoryList().length() > 1) { FarmCatalogue
				 * catalogue = getCatlogueValueByCode(String.valueOf(farmCrops.
				 * getCropCategoryList())); if (!ObjectUtil.isEmpty(catalogue))
				 * { String name = catalogue != null ? catalogue.getName() : "";
				 * farmCrops.setCropCategoryList(name); } else {
				 * farmCrops.setCropCategoryList(""); } }
				 * 
				 * }
				 * 
				 * if
				 * (!StringUtil.isEmpty(farmCrops.getOtherSeedTreatmentDetails()
				 * )) { farmCrops.setOtherSeedTreatmentDetails(farmCrops.
				 * getOtherSeedTreatmentDetails()); } setLotCode(lotCode); if
				 * (farmCrops.getActiveCoordinates()!=null &&
				 * farmCrops.getActiveCoordinates().getFarmCropsCoordinates() !=
				 * null &&
				 * !ObjectUtil.isListEmpty(farmCrops.getActiveCoordinates().
				 * getFarmCropsCoordinates())) { jsonObjectList =
				 * getFarmCropsJSONObjects(farmCrops.getActiveCoordinates().
				 * getFarmCropsCoordinates()); }
				 */

				// view=populateMoleculeXLS();

				// command = UPDATE;

				farmerDynamicData = farmerService.findFarmerDynamicData(fdfv.getFarmerDynamicData().getId().toString());
				seasonType = farmerDynamicData.getIsSeason();
				entityType = farmerDynamicData.getEntityId();
				// actStatuss=farmerDynamicData.getActStatus();
				farmerDynamicData.setTxnDate(!StringUtil.isEmpty(farmerDynamicData.getDate())
						? DateUtil.convertDateToString(farmerDynamicData.getDate(), DateUtil.STD_DATE_TIME_FORMAT)
						: "");
				Agent agent = agentService.findAgentByAgentId(farmerDynamicData.getCreatedUser());
				if (!ObjectUtil.isEmpty(agent)) {
					setCreatedUsername(agent.getPersonalInfo().getAgentName());
				}
				if (!ObjectUtil.isEmpty(farmerDynamicData) && farmerDynamicData.getEntityId().equalsIgnoreCase("1")) {
					if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
						Farmer frmr = farmerService.findFarmerById(Long.valueOf(farmerDynamicData.getReferenceId()));
						setFarmers(String.valueOf(frmr.getFirstName()));
						setSelectedVillage(String.valueOf(frmr.getVillage().getName()));
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
						setInsDate(DateUtil.convertDateToString(farmerDynamicData.getFollowUpDate(),
								getGeneralDateFormat()));
					}
					farmerDynamicData.setConversionStatus(farmerDynamicData.getConversionStatus() != null
							? "Year " + farmerDynamicData.getConversionStatus() : "");

				}

				// return DETAIL;

				view = DETAIL;
				request.setAttribute(HEADING, getText(DETAIL));
			}
			return view;
		} else {

			// formMainGridCols1();
			dynamicReportConfig = clientService.findReportById(String.valueOf(view_id));
			setFetchType(String.valueOf(dynamicReportConfig.getFetchType()));
			if (!ObjectUtil.isEmpty(dynamicReportConfig)
					&& !ObjectUtil.isListEmpty(dynamicReportConfig.getDynmaicReportConfigFilters())) {
				setFilterSize(String.valueOf(dynamicReportConfig.getDynmaicReportConfigFilters().size()));

				dynamicReportConfig.getDynmaicReportConfigFilters().stream().forEach(reportConfigFilter -> {
					if (reportConfigFilter.getMethod() != null && !StringUtil.isEmpty(reportConfigFilter.getMethod())
							&& reportConfigFilter.getType() != null && (reportConfigFilter.getType() == 3
									|| reportConfigFilter.getType() == 6 || reportConfigFilter.getType() == 7)) {
						Map<String, String> optionMap = (Map<String, String>) getMethodValue(
								reportConfigFilter.getMethod(), null);
						reportConfigFilter.setOptions(optionMap);
					} else if (reportConfigFilter.getMethod() != null
							&& !StringUtil.isEmpty(reportConfigFilter.getMethod())
							&& reportConfigFilter.getType() != null && reportConfigFilter.getType() == 5) {
						Map<String, String> optionMap = (Map<String, String>) getQueryForFilters(
								reportConfigFilter.getMethod(), new Object[] { getBranchId() });
						reportConfigFilter.setOptions(optionMap);
					}
					reportConfigFilters.add(reportConfigFilter);
				});
			}

			if (!StringUtil.isEmpty(filterList)) {
				try {

					Map<String, String> filtersList = new Gson().fromJson(filterList, listType1);
					if (branchIdParma != null && !StringUtil.isEmpty(branchIdParma)) {
						filtersList.put("BRANCH_ID", " ='" + branchIdParma + "'");
					} else if (getBranchId() != null) {
						filtersList.put("BRANCH_ID", " ='" + getBranchId() + "'");
					}

					if (subBranchIdParma != null && !StringUtil.isEmpty(subBranchIdParma)
							&& !subBranchIdParma.equals("0")) {
						filtersList.put("BRANCH_ID", " ='" + subBranchIdParma + "'");
					}
					if (crop_id != null && !StringUtil.isEmpty(crop_id)) {
						filtersList.put("REFERENCE_ID", " ='" + crop_id + "'");
					}
					// setLotCode(lotCode);
					if (lotCode != null && !StringUtil.isEmpty(lotCode)) {
						filtersList.put("FIELD_VALUE", " ='" + lotCode + "'");
					}
					mainMap.put(DynamicReportConfig.FILTER_FIELDS, filtersList);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			otherMap.put(DynamicReportConfig.DYNAMIC_CONFIG_DETAIL,
					dynamicReportConfig.getDynmaicReportConfigDetails());
			otherMap.put(DynamicReportConfig.ENTITY, dynamicReportConfig.getEntityName());
			otherMap.put(DynamicReportConfig.ALIAS, dynamicReportConfig.getAlias());
			otherMap.put(DynamicReportConfig.GROUP_PROPERTY, dynamicReportConfig.getGroupProperty());
			mainMap.put(DynamicReportConfig.OTHER_FIELD, otherMap);

			Map data = null;
			if (!ObjectUtil.isEmpty(dynamicReportConfig) && dynamicReportConfig.getFetchType() == 2L) {
				data = readProjectionDataStatic(mainMap);
			} else if (!ObjectUtil.isEmpty(dynamicReportConfig) && dynamicReportConfig.getFetchType() == 3L) {

				data = readProjectionDataView(mainMap);
			} else {
				data = readData();
			}
			setGridIdentity(IReportDAO.MAIN_GRID);
			/* removing ID column config detail fro iterating */
			if (!ObjectUtil.isEmpty(dynamicReportConfig)
					&& !ObjectUtil.isEmpty(dynamicReportConfig.getDynmaicReportConfigDetails())) {
				dynamicReportConfig.getDynmaicReportConfigDetails()
						.remove(dynamicReportConfig.getDynmaicReportConfigDetails().iterator().next());
				/*
				 * removing Branch Dynamic Report COnfig Detail From Iterating,
				 * s its value already added to rows
				 */
				dynamicReportConfig.getDynmaicReportConfigDetails()
						.remove(dynamicReportConfig.getDynmaicReportConfigDetails().iterator().next());
			}

			return sendJSONResponse(data);
		}

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
				if (coordinateObj.getType() == typesOFCordinates.mainCoordinates.ordinal()) {
					jsonObject.put("lat",
							!ObjectUtil.isEmpty(coordinateObj.getLatitude()) ? coordinateObj.getLatitude() : "");
					jsonObject.put("lon",
							!ObjectUtil.isEmpty(coordinateObj.getLongitude()) ? coordinateObj.getLongitude() : "");
					returnObjects.add(jsonObject);
				} else if (coordinateObj.getType() == typesOFCordinates.neighbouringDetails_farmcrops.ordinal()) {
					jsonObject.put("type", coordinateObj.getType());
					jsonObject.put("title", coordinateObj.getTitle());
					jsonObject.put("description", coordinateObj.getDescription());
					jsonObject.put("lat",
							!ObjectUtil.isEmpty(coordinateObj.getLatitude()) ? coordinateObj.getLatitude() : "");
					jsonObject.put("lng",
							!ObjectUtil.isEmpty(coordinateObj.getLongitude()) ? coordinateObj.getLongitude() : "");
					neighbouringDetails.add(jsonObject);
				}

			}
		}
		JSONObject neighbouring_jsonObject = new JSONObject();
		neighbouring_jsonObject.put("neighbouringDetails", neighbouringDetails);
		returnObjects.add(neighbouring_jsonObject);
		return returnObjects;
	}

	public String populateMoleculeXLS() throws Exception {
		String dataStatus = "";
		try {
			InputStream is = getMoleculeExportDataStream(IExporter.EXPORT_MANUAL);
			/* if(!ObjectUtil.isEmpty(is)){ */
			setXlsFileName(getLocaleProperty("moleculetList") + fileNameDateFormat.format(new Date()));
			Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
			fileMap.put(xlsFileName, is);
			setFileInputStream(
					FileUtil.createFileInputStreamToZipFile(getLocaleProperty("moleculetList"), fileMap, ".xls"));
			dataStatus = "1";
			/*
			 * }else{
			 * 
			 * setFileInputStream(
			 * FileUtil.createFileInputStreamToZipFile(getText("moleculetList"),
			 * null, ".xls")); dataStatus="0"; }
			 */

		} catch (Exception e) {
			e.printStackTrace();
			dataStatus = "0";
			JSONObject jsonobject = new JSONObject();
			jsonobject.put("dataStatus", dataStatus);
			sendAjaxResponse(jsonobject);
			return null;
		}
		JSONObject jsonobject = new JSONObject();
		jsonobject.put("dataStatus", dataStatus);
		// printAjaxResponse(jsonobject, "text/html");
		// sendAjaxResponse(jsonobject);
		return "xls";

	}

	public InputStream getMoleculeExportDataStream(String exportType) throws IOException {
		InputStream stream = null;
		if (!StringUtil.isEmpty(lotCode)) {
			CropYield cropYield = farmerService.findMoleculeDateByLotCode(lotCode);
			if (!ObjectUtil.isEmpty(cropYield)) {
				File.createTempFile("tmpDir" + String.valueOf(cropYield.getId()), null);
				String makeDir = FileUtil.storeXls(String.valueOf(cropYield.getId()));
				String fileName = getLocaleProperty("moleculetList") + fileNameDateFormat.format(new Date()) + ".xls";
				File file = new File(makeDir + fileName);
				FileOutputStream os = new FileOutputStream(file);
				os.write(cropYield.getImage());
				stream = new FileInputStream(file);
				os.close();

			}
		}
		return stream;

	}

	public FarmerDynamicData getFilter1() {
		return filter1;
	}

	public void setFilter1(FarmerDynamicData filter1) {
		this.filter1 = filter1;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String data() throws Exception {
		if (farmId != null && !farmId.equals("")) {
			filter1 = new FarmerDynamicData();
			filter1.setTxnType(txnType);
			filter1.setReferenceId(farmId);
			if (txnType.equalsIgnoreCase("2019")) {
				filter1.setFarmerDynamicFieldValueId(Long.valueOf(id));
			}
			super.filter = this.filter1;
		}
		Map data = readData();
		return sendJSONResponse(data);
	}

	public String getLotCode(String lotCodeId) {
		String lotCode = "";
		if (!StringUtil.isEmpty(lotCodeId)) {
			FarmerDynamicFieldsValue fdfv = farmerService.findLotCodeFromFarmerDynamicFieldsValue(
					Long.valueOf(lotCodeId), getLocaleProperty("lotCode.fieldName"));

			lotCode = !ObjectUtil.isEmpty(fdfv) && !StringUtil.isEmpty(fdfv.getFieldValue()) ? fdfv.getFieldValue()
					: "";
		}

		return lotCode;

	}

	public String getBranchIdParma() {
		return branchIdParma;
	}

	public void setBranchIdParma(String branchIdParma) {
		this.branchIdParma = branchIdParma;
	}

	public String farmDetail(long farmId) throws Exception {
		// Farm farm=null;

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

		farm = farmerService.findFarmById(farmId);
		setSangham(farm.getFarmer().getSangham());
		if (!ObjectUtil.isListEmpty(farm.getFarmICS())) {
			farmICSs = new ArrayList<FarmICS>(farm.getFarmICS());

			farmICSs.sort((FarmICS o1, FarmICS o2) -> o1.getIcsType() - o2.getIcsType());

		}
		actStatuss = farm.getActStatus();
		cerLevel = farm.getCertificateStandardLevel();
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
					cropHarvestDetails
							.setHarvestSeason((!ObjectUtil.isEmpty(crops) && !ObjectUtil.isEmpty(crops.getCropSeason()))
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
			if (!StringUtil.isEmpty(farm.getPresenceBananaTrees())) {
				presenceOfBanana = bananaTreesList.get(Integer.parseInt(farm.getPresenceBananaTrees()));
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
		if (!ObjectUtil.isEmpty(farm.getLatitude()) && farm.getLatitude() != "") {
			farm.setLatitude(CurrencyUtil.getDecimalFormat(Double.valueOf(farm.getLatitude()), "##.000"));
		}
		if (!ObjectUtil.isEmpty(farm.getLongitude()) && farm.getLongitude() != "") {
			farm.setLongitude(CurrencyUtil.getDecimalFormat(Double.valueOf(farm.getLongitude()), "##.000"));
		}
		if (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo())) {

			if (!StringUtil.isEmpty(farm.getFarmDetailedInfo().getTotalLandHolding())) {
				farm.getFarmDetailedInfo().setTotalLandHolding(CurrencyUtil
						.getDecimalFormat(Double.valueOf(farm.getFarmDetailedInfo().getTotalLandHolding()), "##.00"));
			} else {
				farm.getFarmDetailedInfo().setTotalLandHolding("0.00");
			}
			if (!StringUtil.isEmpty(farm.getFarmDetailedInfo().getProposedPlantingArea())) {
				farm.getFarmDetailedInfo().setProposedPlantingArea(CurrencyUtil.getDecimalFormat(
						Double.valueOf(farm.getFarmDetailedInfo().getProposedPlantingArea()), "##.00"));
			} else {
				farm.getFarmDetailedInfo().setProposedPlantingArea("0.00");
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

			if (!StringUtil.isEmpty(farm.getFarmDetailedInfo().getTotalLandHolding())
					&& (!StringUtil.isEmpty(farm.getOwnLand()))) {
				double totalHold = Double.valueOf(farm.getFarmDetailedInfo().getTotalLandHolding());
				double landOwn = Double.valueOf(farm.getOwnLand());
				double temp = totalHold * landOwn * 100;
				noOfWineOnPlot = CurrencyUtil.getDecimalFormat((Double) temp, "##.00");
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
						farmIrrigationDetail = getFarmIrrigationList().get(Integer.valueOf(irrigationSourceTrim)) + ",";
					}
				}
				farmIrrigationDetail = StringUtil.removeLastComma(farmIrrigationDetail);
			} else {
				farmIrrigationDetail = ((StringUtil.isEmpty(farm.getFarmDetailedInfo().getFarmIrrigation()))
						|| farm.getFarmDetailedInfo().getFarmIrrigation() == SELECT_MULTI) ? ""
								: getFarmIrrigationList()
										.get(Integer.valueOf(farm.getFarmDetailedInfo().getFarmIrrigation()));

				if (!ObjectUtil.isEmpty(farm) && !ObjectUtil.isEmpty(farm.getFarmDetailedInfo())
						&& !ObjectUtil.isEmpty(farm.getFarmDetailedInfo().getFarmIrrigation())
						&& farm.getFarmDetailedInfo().getFarmIrrigation().contains("2")) {
					isIrrigation = "1";
				} else {
					isIrrigation = "0";
				}
			}

			if (!StringUtil.isEmpty(farm.getFarmDetailedInfo().getSoilType())
					&& farm.getFarmDetailedInfo().getSoilType().contains(",")) {

				String soilTypArray[] = farm.getFarmDetailedInfo().getSoilType().split(",");

				for (int i = 0; i < soilTypArray.length; i++) {
					String soilTypTrim = soilTypArray[i].replaceAll("\\s+", "");
					FarmCatalogue catalogue = catalogueService.findCatalogueByCode(soilTypTrim);
					if (!StringUtil.isEmpty(soilTypeDetail)) {
						soilTypeDetail += catalogue.getName() + ",";
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
							methodOfIrrigationDetail += getMethodOfIrrigationList().get(methodIrrigationTypTrim) + ",";
						} else {
							methodOfIrrigationDetail = getMethodOfIrrigationList().get(methodIrrigationTypTrim) + ",";
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
							// landGradientDetail +=
							// getLandGradientList().get(Integer.valueOf(landGradiTrim))
							// + ",";
							landGradientDetail += getLandGradientList().get(String.valueOf(landGradiTrim)) + ",";
						} else {
							// landGradientDetail =
							// getLandGradientList().get(Integer.valueOf(landGradiTrim))
							// + ",";
							landGradientDetail = getLandGradientList().get(String.valueOf(landGradiTrim)) + ",";
						}
					}

				} else {
					/*
					 * landGradientDetail =
					 * ((StringUtil.isEmpty(farm.getFarmDetailedInfo().
					 * getLandGradient())) ||
					 * farm.getFarmDetailedInfo().getLandGradient() ==
					 * SELECT_MULTI) ? "" : getLandGradientList()
					 * .get(Integer.valueOf(farm.getFarmDetailedInfo().
					 * getLandGradient()));
					 */
					landGradientDetail = ((StringUtil.isEmpty(farm.getFarmDetailedInfo().getLandGradient()))
							|| farm.getFarmDetailedInfo().getLandGradient() == SELECT_MULTI) ? ""
									: getLandGradientList()
											.get(String.valueOf(farm.getFarmDetailedInfo().getLandGradient()));
				}
			} else {
				/*
				 * FarmCatalogue cat =
				 * getCatlogueValueByCode(farm.getFarmDetailedInfo().
				 * getLandGradient());
				 * 
				 * if (cat != null) { landGradientDetail = cat.getName(); }
				 */

				if (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo())
						&& (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo().getLandGradient()))
						&& farm.getFarmDetailedInfo().getLandGradient().contains(",")) {
					String landGradiArray[] = farm.getFarmDetailedInfo().getLandGradient().split(",");

					for (int i = 0; i < landGradiArray.length; i++) {
						String landGradiTrim = landGradiArray[i].replaceAll("\\s+", "");
						if (!StringUtil.isEmpty(selectedRoadString)) {
							landGradientDetail += getCatlogueValueByCode(landGradiTrim).getName() + ",";
						} else {
							landGradientDetail = getCatlogueValueByCode(landGradiTrim).getName() + ",";
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

				}
			} else {
				/*
				 * FarmCatalogue cat =
				 * getCatlogueValueByCode(farm.getFarmDetailedInfo().
				 * getApproachRoad()); if (cat != null) { selectedRoadString =
				 * cat.getName(); }
				 */

				if (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo())
						&& (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo().getApproachRoad()))
						&& farm.getFarmDetailedInfo().getApproachRoad().contains(",")) {
					String approachRoadArray[] = farm.getFarmDetailedInfo().getApproachRoad().split(",");

					for (int i = 0; i < approachRoadArray.length; i++) {
						String approachRoadTrim = approachRoadArray[i].replaceAll("\\s+", "");
						if (!StringUtil.isEmpty(selectedRoadString)) {
							selectedRoadString += getCatlogueValueByCode(approachRoadTrim).getName() + ",";
						} else {
							selectedRoadString = getCatlogueValueByCode(approachRoadTrim).getName() + ",";
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
				ESESystem preferences = preferncesService.findPrefernceById("1");
				if (!ObjectUtil.isEmpty(preferences)) {

					DateFormat genDate = new SimpleDateFormat(
							preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
					if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)) {
						SimpleDateFormat sf = new SimpleDateFormat("MM-dd-yyyy");
						icd.setInspectionDateString(
								StringUtil.isEmpty(icd.getInspectionDate()) ? " " : sf.format(icd.getInspectionDate()));
					} else
						icd.setInspectionDateString(StringUtil.isEmpty(icd.getInspectionDate()) ? " "
								: genDate.format(icd.getInspectionDate()));
				}

				/*
				 * farm.setOrganicStatus(!StringUtil.isEmpty(icd.
				 * getOrganicStatus()) ? icd.getOrganicStatus() : "4");
				 */

				scopeName = !StringUtil.isEmpty(icd.getScope())
						? catalogueService.findCatalogueByCode(icd.getScope()).getName() : "";

				/*
				 * if(!ObjectUtil.isEmpty(preferences)){ ife
				 * (!ObjectUtil.isEmpty(farm.getFarmIcsConv().
				 * getInspectionDateString()) &&
				 * farm.getFarmIcsConv().getInspectionDateString() != null) {
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
			DateFormat genDate = new SimpleDateFormat(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)) {
				SimpleDateFormat sf = new SimpleDateFormat("MM-dd-yyyy");
				farm.getFarmDetailedInfo().setLastDateOfChemicalString(
						StringUtil.isEmpty(farm.getFarmDetailedInfo().getLastDateOfChemical()) ? " "
								: sf.format(farm.getFarmDetailedInfo().getLastDateOfChemical()));
			} else
				farm.getFarmDetailedInfo().setLastDateOfChemicalString(
						StringUtil.isEmpty(farm.getFarmDetailedInfo().getLastDateOfChemical()) ? " "
								: genDate.format(farm.getFarmDetailedInfo().getLastDateOfChemical()));
		}
		if (!ObjectUtil.isEmpty(farm.getAudio())) {
			this.audioDownloadString = "<button type=\"button\" title='" + getText("audio.download")
					+ "' class=\"fa fa-download\" align=\"center\" onclick=\"downloadAudioFile(" + farm.getId() + ")\">"
					+ "</button>" + "&nbsp;&nbsp;&nbsp;" + "<button type=\"button\" title='" + getText("audio.play")
					+ "' class=\"fa fa-volume-up\" align=\"center\" onclick=\"playAudioFiles(" + farm.getId() + ")\">"
					+ "</button>";
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
				// List<SeedTreatment> seedTreat =
				// farmerService.getSeedTreatmentById()
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
				// command = UPDATE;
				view = FARM_CERTIFIED_DETAIL;
			} else {
				setCurrentPage(getCurrentPage());
				// command = UPDATE;
				view = DETAIL;
			}
			if (farm.getActiveCoordinates() != null && farm.getActiveCoordinates().getFarmCoordinates() != null
					&& !ObjectUtil.isEmpty(farm.getActiveCoordinates())
					&& !ObjectUtil.isListEmpty(farm.getActiveCoordinates().getFarmCoordinates())) {
				jsonFarmObjectList = getFarmJSONObjects(farm.getActiveCoordinates().getFarmCoordinates(),
						farm.getFarmName());
			} else {
				jsonFarmObjectList = new ArrayList();
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

		return view;

	}

	public String getSangham() {
		return sangham;
	}

	public void setSangham(String sangham) {
		this.sangham = sangham;
	}

	public List<FarmICS> getFarmICSs() {
		return farmICSs;
	}

	public void setFarmICSs(List<FarmICS> farmICSs) {
		this.farmICSs = farmICSs;
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

	public String getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}

	public String getFarmerUniqueId() {

		return farmerUniqueId;
	}

	public void setFarmerUniqueId(String farmerUniqueId) {

		this.farmerUniqueId = farmerUniqueId;
	}

	public boolean isCertifiedFarmer() {
		return isCertifiedFarmer;
	}

	public void setCertifiedFarmer(boolean isCertifiedFarmer) {
		this.isCertifiedFarmer = isCertifiedFarmer;
	}

	public List<CropHarvest> getHarvest() {
		return harvest;
	}

	public void setHarvest(List<CropHarvest> harvest) {
		this.harvest = harvest;
	}

	public List<CropHarvestDetails> getHarvestDetails() {
		return harvestDetails;
	}

	public void setHarvestDetails(List<CropHarvestDetails> harvestDetails) {
		this.harvestDetails = harvestDetails;
	}

	public HousingInfo getHousingInfo() {
		return housingInfo;
	}

	public void setHousingInfo(HousingInfo housingInfo) {
		this.housingInfo = housingInfo;
	}

	public String getPresenceOfBanana() {
		return presenceOfBanana;
	}

	public void setPresenceOfBanana(String presenceOfBanana) {
		this.presenceOfBanana = presenceOfBanana;
	}

	public Map<Integer, String> getBananaTreesList() {
		return bananaTreesList;
	}

	public void setBananaTreesList(Map<Integer, String> bananaTreesList) {
		this.bananaTreesList = bananaTreesList;
	}

	public List<CropSupply> getHarvestSupply() {
		return harvestSupply;
	}

	public void setHarvestSupply(List<CropSupply> harvestSupply) {
		this.harvestSupply = harvestSupply;
	}

	public Set<CropSupplyDetails> getCropSupplyDetails() {
		return cropSupplyDetails;
	}

	public void setCropSupplyDetails(Set<CropSupplyDetails> cropSupplyDetails) {
		this.cropSupplyDetails = cropSupplyDetails;
	}

	public String getDateOfInspection() {
		return dateOfInspection;
	}

	public void setDateOfInspection(String dateOfInspection) {
		this.dateOfInspection = dateOfInspection;
	}

	public String getLastDateChemical() {
		return lastDateChemical;
	}

	public void setLastDateChemical(String lastDateChemical) {
		this.lastDateChemical = lastDateChemical;
	}

	public String getFarmOwnedDetail() {
		return farmOwnedDetail;
	}

	public void setFarmOwnedDetail(String farmOwnedDetail) {
		this.farmOwnedDetail = farmOwnedDetail;
	}

	public String getFarmIrrigationDetail() {
		return farmIrrigationDetail;
	}

	public void setFarmIrrigationDetail(String farmIrrigationDetail) {
		this.farmIrrigationDetail = farmIrrigationDetail;
	}

	public String getIrrigationSourceDetail() {
		return irrigationSourceDetail;
	}

	public void setIrrigationSourceDetail(String irrigationSourceDetail) {
		this.irrigationSourceDetail = irrigationSourceDetail;
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

	public List<Object[]> getFarmerList() {
		return farmerList;
	}

	public void setFarmerList(List<Object[]> farmerList) {
		this.farmerList = farmerList;
	}

	public void setFarmIrrigationList(Map<Integer, String> farmIrrigationList) {
		this.farmIrrigationList = farmIrrigationList;
	}

	public String getNoOfWineOnPlot() {
		return noOfWineOnPlot;
	}

	public void setNoOfWineOnPlot(String noOfWineOnPlot) {
		this.noOfWineOnPlot = noOfWineOnPlot;
	}

	public String getIsIrrigation() {
		return isIrrigation;
	}

	public void setIsIrrigation(String isIrrigation) {
		this.isIrrigation = isIrrigation;
	}

	public String getSoilTypeDetail() {
		return soilTypeDetail;
	}

	public void setSoilTypeDetail(String soilTypeDetail) {
		this.soilTypeDetail = soilTypeDetail;
	}

	public Map<String, String> getSoilTypeList() {

		Map<String, String> soilTypeMap = new LinkedHashMap<String, String>();

		List<FarmCatalogue> soilTypeList = farmerService.listCatelogueType(getText("soilType"));
		for (FarmCatalogue obj : soilTypeList) {
			// soilTypeMap.put(obj.getCode(), obj.getName());
			String name = getLanguagePref(getLoggedInUserLanguage(), obj.getCode().trim().toString());
			if (!StringUtil.isEmpty(name) && name != null) {
				soilTypeMap.put(obj.getCode().toString(), obj.getCode().toString() + "-"
						+ getLanguagePref(getLoggedInUserLanguage(), obj.getCode().toString()));
			} else {
				soilTypeMap.put(obj.getCode(), obj.getCode() + "-" + obj.getName());
			}
		}
		return soilTypeMap;
	}

	public String getMethodOfIrrigationDetail() {
		return methodOfIrrigationDetail;
	}

	private HarvestSeason getSeason(String seasonCode) {

		HarvestSeason season = getHarvestSeason(seasonCode);
		return season;

	}

	public void setMethodOfIrrigationDetail(String methodOfIrrigationDetail) {
		this.methodOfIrrigationDetail = methodOfIrrigationDetail;
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

	public String getSoilFertilityDetail() {
		return soilFertilityDetail;
	}

	public void setSoilFertilityDetail(String soilFertilityDetail) {
		this.soilFertilityDetail = soilFertilityDetail;
	}

	public void setSoilFertilityList(Map<Integer, String> soilFertilityList) {
		this.soilFertilityList = soilFertilityList;
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
			// soilTexMap.put(obj.getCode(), obj.getName());
			String name = getLanguagePref(getLoggedInUserLanguage(), obj.getCode().trim().toString());
			if (!StringUtil.isEmpty(name) && name != null) {
				soilTexMap.put(obj.getCode().toString(), obj.getCode().toString() + "-"
						+ getLanguagePref(getLoggedInUserLanguage(), obj.getCode().toString()));
			} else {
				soilTexMap.put(obj.getCode(), obj.getName());
			}
		}
		return soilTexMap;
	}

	public Map<String, String> getLandGradientList() {

		Map<String, String> landGradientList = new LinkedHashMap<>();

		landGradientList = getFarmCatalougeMap(Integer.valueOf(getLocaleProperty("landGradientType").trim()));

		return landGradientList;
	}

	public Map<String, String> getApproadList() {

		approadList = getFarmCatalougeMap(Integer.valueOf(getLocaleProperty("approadListType").trim()));

		return approadList;
	}

	public String getLandUnderICSStatusDetail() {
		return landUnderICSStatusDetail;
	}

	public void setLandUnderICSStatusDetail(String landUnderICSStatusDetail) {
		this.landUnderICSStatusDetail = landUnderICSStatusDetail;
	}

	public String getLandGradientDetail() {
		return landGradientDetail;
	}

	public void setLandGradientDetail(String landGradientDetail) {
		this.landGradientDetail = landGradientDetail;
	}

	public String getSelectedRoadString() {
		return selectedRoadString;
	}

	public void setSelectedRoadString(String selectedRoadString) {
		this.selectedRoadString = selectedRoadString;
	}

	public String getSessionYearString() {
		return sessionYearString;
	}

	public void setSessionYearString(String sessionYearString) {
		this.sessionYearString = sessionYearString;
	}

	public String getRegYearString() {
		return regYearString;
	}

	public void setRegYearString(String regYearString) {
		this.regYearString = regYearString;
	}

	public String getSelectedWaterSource() {
		return selectedWaterSource;
	}

	public void setSelectedWaterSource(String selectedWaterSource) {
		this.selectedWaterSource = selectedWaterSource;
	}

	public String getSelectedInputSource() {
		return selectedInputSource;
	}

	public void setSelectedInputSource(String selectedInputSource) {
		this.selectedInputSource = selectedInputSource;
	}

	public String getScopeName() {
		return scopeName;
	}

	public void setScopeName(String scopeName) {
		this.scopeName = scopeName;
	}

	public void setIcsStatusList(Map<Integer, String> icsStatusList) {
		this.icsStatusList = icsStatusList;
	}

	public void setApproadList(Map<String, String> approadList) {
		this.approadList = approadList;
	}

	public String getAudioDownloadString() {
		return audioDownloadString;
	}

	public void setAudioDownloadString(String audioDownloadString) {
		this.audioDownloadString = audioDownloadString;
	}

	public String getSoilTextureDetail() {
		return soilTextureDetail;
	}

	public void setSoilTextureDetail(String soilTextureDetail) {
		this.soilTextureDetail = soilTextureDetail;
	}

	public String getWaterHarvests() {
		return waterHarvests;
	}

	public void setWaterHarvests(String waterHarvests) {
		this.waterHarvests = waterHarvests;
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

	public List<TreeDetail> getTreeDetails() {
		return treeDetails;
	}

	public void setTreeDetails(List<TreeDetail> treeDetails) {
		this.treeDetails = treeDetails;
	}

	public String getExpression_result() {
		return expression_result;
	}

	public void setExpression_result(String expression_result) {
		this.expression_result = expression_result;
	}

	public String getFarmImageByteString() {
		return farmImageByteString;
	}

	public void setFarmImageByteString(String farmImageByteString) {
		this.farmImageByteString = farmImageByteString;
	}

	public List<FarmerSoilTesting> getFarmerSoilTestingList() {
		return farmerSoilTestingList;
	}

	public void setFarmerSoilTestingList(List<FarmerSoilTesting> farmerSoilTestingList) {
		this.farmerSoilTestingList = farmerSoilTestingList;
	}

	public List<Expenditure> getExpenditureList() {
		return expenditureList;
	}

	public void setExpenditureList(List<Expenditure> expenditureList) {
		this.expenditureList = expenditureList;
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

	@SuppressWarnings("unchecked")
	private List<JSONObject> getFarmJSONObjects(Set<Coordinates> coordinates, String Name) {
		JSONArray neighbouringDetails_farm = new JSONArray();
		List<JSONObject> returnObjects = new ArrayList<JSONObject>();
		List<Coordinates> listCoordinates = new ArrayList<Coordinates>(coordinates);
		Collections.sort(listCoordinates);
		if (!ObjectUtil.isListEmpty(listCoordinates)) {
			for (Coordinates coordinateObj : listCoordinates) {
				JSONObject jsonObject = new JSONObject();

				if (coordinateObj.getType() == typesOFCordinates.mainCoordinates.ordinal()) {
					jsonObject.put("lat",
							!ObjectUtil.isEmpty(coordinateObj.getLatitude()) ? coordinateObj.getLatitude() : "");
					jsonObject.put("lon",
							!ObjectUtil.isEmpty(coordinateObj.getLongitude()) ? coordinateObj.getLongitude() : "");
					jsonObject.put("name", Name);
					returnObjects.add(jsonObject);
				} else if (coordinateObj.getType() == typesOFCordinates.neighbouringDetails_farm.ordinal()) {
					jsonObject.put("type", coordinateObj.getType());
					jsonObject.put("title", coordinateObj.getTitle());
					jsonObject.put("description", coordinateObj.getDescription());
					jsonObject.put("lat",
							!ObjectUtil.isEmpty(coordinateObj.getLatitude()) ? coordinateObj.getLatitude() : "");
					jsonObject.put("lng",
							!ObjectUtil.isEmpty(coordinateObj.getLongitude()) ? coordinateObj.getLongitude() : "");
					neighbouringDetails_farm.add(jsonObject);
				}

			}
		}
		JSONObject neighbouring_jsonObject = new JSONObject();
		// neighbouring_jsonObject.put("neighbouringDetails_farm",neighbouringDetails_farm);
		// returnObjects.add(neighbouring_jsonObject);
		return returnObjects;
	}

	public Farm getFarm() {
		return farm;
	}

	public void setFarm(Farm farm) {
		this.farm = farm;
	}

	public Farmer getFarmer() {
		return farmer;
	}

	public void setFarmer(Farmer farmer) {
		this.farmer = farmer;
	}

	public void setFarmId(String farmId) {
		this.farmId = farmId;
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

	@SuppressWarnings("unchecked")
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

		farmerFieldList.stream().filter(farmerField -> farmerField.getStatus() == 1L).forEach(farmerField -> {
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

	public String farmerDetail(long farmerId) throws Exception {

		// type = request.getParameter("type");

		String view = "";
		String vehicle = "";
		String consumerElec = "";
		String drinkingWater = "";
		String familyMember = null;
		String cookingFuel = "";
		String govtDept = "";
		String cropIns = "";
		String homeDifficulty = "";
		String workDifficulty = "";
		String communityDifficulty = "";
		String agricultureImplements = "";
		boolean first = true;
		if (id != null && !id.equals("")) {
			farmer = farmerService.findFarmerById(farmerId);

			card = cardService.findCardByProfileId(farmer.getFarmerId());
			interestCalcConsolidated = farmerService
					.findInterestCalcConsolidatedByfarmerProfileId(farmer.getFarmerId());

			ESESystem preferences = preferncesService.findPrefernceById("1");
			setEnableContractTemplate(
					!StringUtil.isEmpty(preferences.getPreferences().get(ESESystem.contracte_template_farmer_detail))
							? preferences.getPreferences().get(ESESystem.contracte_template_farmer_detail) : "0");

			DateFormat genDate = new SimpleDateFormat(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
			isInterestModule = preferences.getPreferences().get(ESESystem.INTEREST_MODULE);
			setFpoEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FPOFG));
			setFarmerCodeEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE));
			setFarmerBankInfoEnabled(preferences.getPreferences().get(ESESystem.ENABLE_BANK_INFO));
			setIdProofEnabled(preferences.getPreferences().get(ESESystem.ID_PROOF));
			setInsuranceInfoEnabled(preferences.getPreferences().get(ESESystem.ENABLE_INSURANCE_INFO));
			setGramPanchayatEnable(preferences.getPreferences().get(ESESystem.ENABLE_GRAMPANJAYAT));
			setFingerPrintEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FINGER_PRINT));
			// setIsCertifiedFarmerInfoEnabled(preferences.getPreferences().get(ESESystem.IS_CERTIFIED_FARMER));

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
					// dateOfBirth = df.format(farmer.getDateOfBirth());
				}
				if (!ObjectUtil.isEmpty(farmer.getLoanRepaymentDate()) && farmer.getLoanRepaymentDate() != null) {
					loanRepaymentDate = genDate.format(farmer.getLoanRepaymentDate());
					// dateOfBirth = df.format(farmer.getDateOfBirth());
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
						;
					}
				}
			}

			if (farmer.getIcsCode() != null) {
				icsCode = farmer.getIcsCode();
			}
			if (farmer.getIcsName() != null) {

				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)
						|| getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)
						|| getCurrentTenantId().equalsIgnoreCase("iccoa")
						|| getCurrentTenantId().equalsIgnoreCase(ESESystem.SUSAGRI)) {
					FarmCatalogue catalogue = getCatlogueValueByCode(farmer.getIcsName());
					if (!StringUtil.isEmpty(catalogue)) {
						farmer.setIcsName(catalogue.getName());
					}
				} else
					icsName = farmer.getIcsName();

			}
			ESEAccount acc = accountService.findAccountByProfileIdAndProfileType(farmer.getFarmerId(),
					ESEAccount.CONTRACT_ACCOUNT);
			if (!ObjectUtil.isEmpty(acc) || acc != null) {
				setAccBalance(String.valueOf(acc.getCashBalance()));
			}

			if (farmer.getImageInfo() != null && !ObjectUtil.isEmpty(farmer.getImageInfo())
					&& farmer.getImageInfo().getPhoto() != null && farmer.getImageInfo().getPhoto().getImage() != null
					&& farmer.getImageInfo().getPhoto().getImage().length > 0) {
				setFarmerImageByteString(Base64Util.encoder(farmer.getImageInfo().getPhoto().getImage()));
			}

			if (farmer.getDigitalSign() != null && farmer.getDigitalSign().length > 0) {
				setDigitalSignatureByteString(Base64Util.encoder(farmer.getDigitalSign()));
			}

			if (farmer.getIdProofImg() != null && farmer.getIdProofImg().length > 0) {
				setIdProofImgString(Base64Util.encoder(farmer.getIdProofImg()));
				setIdImgAvil("1");
			}
			if (!ObjectUtil.isEmpty(farmer.getLatitude()) && farmer.getLatitude() != "") {
				farmer.setLatitude(CurrencyUtil.getDecimalFormat(Double.valueOf(farmer.getLatitude()), "##.000"));
			}
			if (!ObjectUtil.isEmpty(farmer.getLongitude()) && farmer.getLongitude() != "") {
				farmer.setLongitude(CurrencyUtil.getDecimalFormat(Double.valueOf(farmer.getLongitude()), "##.000"));
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
					// drinkingWater =
					// StringUtil.removeLastComma(drinkingWater);
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
						// cropIns +=
						// catalogueService.findCatalogueValueByCode(cropInsTrim)
						// + ",";
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
							// consumerElec += getText("consumerElec" +
							// electronicsTrim) + ", ";

						}
					} else {
						FarmCatalogue catalogue = getCatlogueValueByCode(electronicsTrim);
						if (!ObjectUtil.isEmpty(catalogue)) {
							consumerElec += catalogue.getName() + ",";
						}
						// consumerElec = getText("consumerElec" +
						// electronicsTrim) + ",";
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
							// vehicle += getText("vehicle" + vehicleTrim) + ",
							// ";
						}
					} else {
						FarmCatalogue catalogue = getCatlogueValueByCode(vehicleTrim);
						if (!ObjectUtil.isEmpty(catalogue)) {
							vehicle += catalogue.getName() + ",";
						}
						// vehicle = getText("vehicle" + vehicleTrim) + ",";
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
							/*
							 * cookingFuel += getText("farmer.cookingFuelOther")
							 * + ":" + farmerEconomy.getCookingFuelSourceOther()
							 * + ",";
							 */
							// cookingFuel +=
							// getText("farmer.cookingFuelOther");
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

				if (cookingFuel != null && (!cookingFuel.equals(""))) { // second
																		// condition
																		// to
																		// avoid
					// array -1 index exception.
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
					if (!StringUtil.isEmpty(farmer.getLoanPupose()) && farmer.getLoanPupose().trim()
							.charAt(farmer.getLoanPupose().trim().length() - 1) == '-') {
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
				if (!farmer.getLoanSecurity().isEmpty() && farmer.getLoanSecurity().trim()
						.charAt(farmer.getLoanSecurity().trim().length() - 1) == '-') {
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

			farmer.setPrefWrk(!StringUtil.isEmpty(farmer.getPrefWrk())
					? getCatlogueValueByCode(farmer.getPrefWrk()).getName() : "");

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
			if (getCurrentTenantId().equalsIgnoreCase("symrise")) {
				farmer.setCertificateStandardLevel(farmer.getCertificateStandardLevel());
			}

			if ((getCurrentTenantId().equalsIgnoreCase("efk"))) {
				farmer.setTotalHsldLabel(String.valueOf(farmer.getTotalHsldLabel()));
			} else {
				farmer.setTotalHsldLabel(
						String.valueOf(adultCountMale + adultCountFemale + childCountMale + childCountFemale));
			}
			setCurrentPage(getCurrentPage());

			view = DETAIL;
			request.setAttribute(HEADING, getText("farmerdetail"));
		} else {
			request.setAttribute(HEADING, getText(LIST));
			return LIST;
		}
		return view;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private String getCurrentSeasonCode() {

		ESESystem preference = preferncesService.findPrefernceById(ESESystem.CLIENT);
		return preference.getPreferences().get(ESESystem.CURRENT_SEASON_CODE);
	}

	public FarmerDynamicData getFarmerDynamicData() {
		return farmerDynamicData;
	}

	public void setFarmerDynamicData(FarmerDynamicData farmerDynamicData) {
		this.farmerDynamicData = farmerDynamicData;
	}

	public IProductService getProductService() {
		return productService;
	}

	public void setProductService(IProductService productService) {
		this.productService = productService;
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

	public List<JSONObject> getJsonObjectList() {
		return jsonObjectList;
	}

	public void setJsonObjectList(List<JSONObject> jsonObjectList) {
		this.jsonObjectList = jsonObjectList;
	}

	public InterestCalcConsolidated getInterestCalcConsolidated() {
		return interestCalcConsolidated;
	}

	public void setInterestCalcConsolidated(InterestCalcConsolidated interestCalcConsolidated) {
		this.interestCalcConsolidated = interestCalcConsolidated;
	}

	public String getEnableContractTemplate() {
		return enableContractTemplate;
	}

	public void setEnableContractTemplate(String enableContractTemplate) {
		this.enableContractTemplate = enableContractTemplate;
	}

	public String getInvestigatorDate() {
		return investigatorDate;
	}

	public void setInvestigatorDate(String investigatorDate) {
		this.investigatorDate = investigatorDate;
	}

	public FarmerEconomy getFarmerEconomy() {
		return farmerEconomy;
	}

	public void setFarmerEconomy(FarmerEconomy farmerEconomy) {
		this.farmerEconomy = farmerEconomy;
	}

	public String getIcsString() {
		return icsString;
	}

	public void setIcsString(String icsString) {
		this.icsString = icsString;
	}

	public String getIcsDropDown() {
		return icsDropDown;
	}

	public void setIcsDropDown(String icsDropDown) {
		this.icsDropDown = icsDropDown;
	}

	public String getIcsRegNoString() {
		return icsRegNoString;
	}

	public void setIcsRegNoString(String icsRegNoString) {
		this.icsRegNoString = icsRegNoString;
	}

	public String getIcsUnitNoString() {
		return icsUnitNoString;
	}

	public void setIcsUnitNoString(String icsUnitNoString) {
		this.icsUnitNoString = icsUnitNoString;
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

	public boolean isFarmerAndContractStatus() {
		return farmerAndContractStatus;
	}

	public void setFarmerAndContractStatus(boolean farmerAndContractStatus) {
		this.farmerAndContractStatus = farmerAndContractStatus;
	}

	public String getFingerPrintImageByteString() {
		return fingerPrintImageByteString;
	}

	public void setFingerPrintImageByteString(String fingerPrintImageByteString) {
		this.fingerPrintImageByteString = fingerPrintImageByteString;
	}

	public String getDigitalSignatureByteString() {
		return digitalSignatureByteString;
	}

	public void setDigitalSignatureByteString(String digitalSignatureByteString) {
		this.digitalSignatureByteString = digitalSignatureByteString;
	}

	public String getIdImgAvil() {
		return idImgAvil;
	}

	public void setIdImgAvil(String idImgAvil) {
		this.idImgAvil = idImgAvil;
	}

	public String getFpoEnabled() {
		return fpoEnabled;
	}

	public void setFpoEnabled(String fpoEnabled) {
		this.fpoEnabled = fpoEnabled;
	}

	public String getFingerPrintEnabled() {
		return fingerPrintEnabled;
	}

	public void setFingerPrintEnabled(String fingerPrintEnabled) {
		this.fingerPrintEnabled = fingerPrintEnabled;
	}

	public String getGramPanchayatEnable() {
		return gramPanchayatEnable;
	}

	public void setGramPanchayatEnable(String gramPanchayatEnable) {
		this.gramPanchayatEnable = gramPanchayatEnable;
	}

	public String getAccBalance() {
		return accBalance;
	}

	public void setAccBalance(String accBalance) {
		this.accBalance = accBalance;
	}

	public String getFarmerCodeEnabled() {
		return farmerCodeEnabled;
	}

	public void setFarmerCodeEnabled(String farmerCodeEnabled) {
		this.farmerCodeEnabled = farmerCodeEnabled;
	}

	public String getFarmerBankInfoEnabled() {
		return farmerBankInfoEnabled;
	}

	public void setFarmerBankInfoEnabled(String farmerBankInfoEnabled) {
		this.farmerBankInfoEnabled = farmerBankInfoEnabled;
	}

	public String getFarmId() {
		return farmId;
	}

	public ICardService getCardService() {
		return cardService;
	}

	public void setCardService(ICardService cardService) {
		this.cardService = cardService;
	}

	public String getIsInterestModule() {
		return isInterestModule;
	}

	public void setIsInterestModule(String isInterestModule) {
		this.isInterestModule = isInterestModule;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getDateOfJoining() {
		return dateOfJoining;
	}

	public void setDateOfJoining(String dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}

	public String getBankAccType() {
		return bankAccType;
	}

	public void setBankAccType(String bankAccType) {
		this.bankAccType = bankAccType;
	}

	public String getLoanRepaymentDate() {
		return loanRepaymentDate;
	}

	public void setLoanRepaymentDate(String loanRepaymentDate) {
		this.loanRepaymentDate = loanRepaymentDate;
	}

	public String getIcsCode() {
		return icsCode;
	}

	public void setIcsCode(String icsCode) {
		this.icsCode = icsCode;
	}

	public String getIcsName() {
		return icsName;
	}

	public void setIcsName(String icsName) {
		this.icsName = icsName;
	}

	public String getFarmerImageByteString() {
		return farmerImageByteString;
	}

	public void setFarmerImageByteString(String farmerImageByteString) {
		this.farmerImageByteString = farmerImageByteString;
	}

	public String getIdProofImgString() {
		return idProofImgString;
	}

	public void setIdProofImgString(String idProofImgString) {
		this.idProofImgString = idProofImgString;
	}

	public String getCropInfoEnabled() {

		return cropInfoEnabled;
	}

	public void setCropInfoEnabled(String cropInfoEnabled) {

		this.cropInfoEnabled = cropInfoEnabled;
	}

	public String getLotCode() {
		return lotCode;
	}

	public void setLotCode(String lotCode) {
		this.lotCode = lotCode;
	}

	@SuppressWarnings("unchecked")
	public void populateHideFnFarm() throws ScriptException {
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

	@SuppressWarnings("unchecked")
	public void populateHideFnFarmCrop() throws ScriptException {
		List<FarmCropsField> farmerFieldList = farmerService.listRemoveFarmCropFields();
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();

		/*
		 * farmerFieldList.forEach(farmerField -> { JSONObject jsonObject = new
		 * JSONObject(); jsonObject.put("type", farmerField.getType());
		 * jsonObject.put("typeName", farmerField.getTypeName());
		 * jsonObjects.add(jsonObject); });
		 */

		farmerFieldList.forEach(farmerField -> {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("type", farmerField.getType());
			jsonObject.put("typeName", farmerField.getTypeName());
			jsonObjects.add(jsonObject);
		});

		printAjaxResponse(jsonObjects, "text/html");
	}

	public void populateMoleculeData() throws ScriptException {
		JSONObject jsonObject = new JSONObject();
		if (!StringUtil.isEmpty(lotCode)) {
			CropYield cropYield = farmerService.findMoleculeDateByLotCode(lotCode);
			if (!ObjectUtil.isEmpty(cropYield)) {
				jsonObject.put("data", 1);
			} else {
				jsonObject.put("data", 2);
			}

		} else {
			jsonObject.put("data", 2);
		}

		printAjaxResponse(jsonObject, "text/html");
	}

	public String getFarmCropId() {
		return farmCropId;
	}

	public void setFarmCropId(String farmCropId) {
		this.farmCropId = farmCropId;
	}

	public FarmerDynamicFieldsValue getFilter2() {
		return filter2;
	}

	public void setFilter2(FarmerDynamicFieldsValue filter2) {
		this.filter2 = filter2;
	}

	public List<JSONObject> getJsonFarmObjectList() {
		return jsonFarmObjectList;
	}

	public void setJsonFarmObjectList(List<JSONObject> jsonFarmObjectList) {
		this.jsonFarmObjectList = jsonFarmObjectList;
	}

	public int getView_id() {
		return view_id;
	}

	public void setView_id(int view_id) {
		this.view_id = view_id;
	}

	public String getCrop_id() {
		return crop_id;
	}

	public void setCrop_id(String crop_id) {
		this.crop_id = crop_id;
	}

	public void populateColdStorageTransferDetailsData() {
		if (!StringUtil.isEmpty(lotCode) && !StringUtil.isEmpty(coldStorageCode) && !StringUtil.isEmpty(warehouseCode)
				&& !StringUtil.isEmpty(chamberCode) && !StringUtil.isEmpty(floorCode) && !StringUtil.isEmpty(bayCode)) {
			List<ColdStorageStockTransferDetail> coldStoragStockTransferList = productDistributionService
					.listColdStorageStockTransferByLotCodeAndColdStorageCode(lotCode, coldStorageCode, warehouseCode,
							chamberCode, floorCode, bayCode);
			if (!ObjectUtil.isListEmpty(coldStoragStockTransferList)) {
				JSONArray transferArr = new JSONArray();
				for (ColdStorageStockTransferDetail coldTransferDetails : coldStoragStockTransferList) {
					JSONObject transferJSONObject = new JSONObject();
					String sDate = DateUtil.convertDateToString(
							coldTransferDetails.getColdStorageStockTransfer().getCreatedDate(), DateUtil.DATE_FORMAT_2);
					transferJSONObject.put("createdDate", sDate);
					transferJSONObject.put("receiptNo",
							coldTransferDetails.getColdStorageStockTransfer().getReceiptNo());
					transferJSONObject.put("buyer",
							coldTransferDetails.getColdStorageStockTransfer().getBuyer().getCustomerName());
					transferJSONObject.put("truckNo", coldTransferDetails.getColdStorageStockTransfer().getTruckId());
					transferJSONObject.put("driverName",
							coldTransferDetails.getColdStorageStockTransfer().getDriverName());
					transferJSONObject.put("poNo", coldTransferDetails.getColdStorageStockTransfer().getPoNumber());
					transferJSONObject.put("invoiceNo",
							coldTransferDetails.getColdStorageStockTransfer().getInvoiceNumber());
					transferJSONObject.put("noOfBags", coldTransferDetails.getNoOfBags());
					transferJSONObject.put("qty", coldTransferDetails.getQty());
					transferArr.add(transferJSONObject);
				}
				sendAjaxResponse(transferArr);
			}

		}

	}

	public String getDefFilters() {
		return defFilters;
	}

	public void setDefFilters(String defFilters) {
		this.defFilters = defFilters;
	}

	public String pesticideSprayedData() throws Exception {

		String breanchidPa = "BRANCH_ID";
		dynamicReportConfig = clientService.findReportById("13");
		/*
		 * if (!StringUtil.isEmpty(pesticideFilterList)) { try {
		 */
		Map<String, String> filtersList = new Gson().fromJson(pesticideFilterList, listType1);

		if (farmersId != null && !StringUtil.isEmpty(farmersId)) {
			List<Long> farmIds = (farmerService.listFarmByFarmerId(farmersId)).stream().map(u -> u.getId())
					.collect(Collectors.toList());
			String value = farmIds.toString();
			filtersList.put("REFERENCE_ID in", "(" + value.substring(1, value.length() - 1) + ")");
		}
		if (season != null && !StringUtil.isEmpty(season)) {
			filtersList.put("SEASON", "='" + season + "'");
		}

		mainMap.put(DynamicReportConfig.FILTER_FIELDS, filtersList);

		/*
		 * } catch (Exception e) { e.printStackTrace(); } }
		 */

		dynamicReportConfig.getDynmaicReportConfigFilters().stream()
				.filter(reportConfigFilter -> !StringUtil.isEmpty(reportConfigFilter.getDefaultFilter()))
				.forEach(reportConfigFilter -> {
					defFilter = defFilter
							.concat(reportConfigFilter.getField() + ":" + reportConfigFilter.getDefaultFilter() + ",");
					if (hit) {
						defFilter = defFilter.substring(0, defFilter.length() - 1);
						setDefFilters(defFilter);
						hit = false;
					}
				});
		otherMap.put(DynamicReportConfig.DYNAMIC_CONFIG_DETAIL, dynamicReportConfig.getDynmaicReportConfigDetails());
		otherMap.put(DynamicReportConfig.ENTITY, dynamicReportConfig.getEntityName());
		otherMap.put(DynamicReportConfig.ALIAS, dynamicReportConfig.getAlias());
		otherMap.put(DynamicReportConfig.GROUP_PROPERTY, dynamicReportConfig.getGroupProperty());
		mainMap.put(DynamicReportConfig.OTHER_FIELD, otherMap);

		Map data = null;
		if (!ObjectUtil.isEmpty(dynamicReportConfig) && dynamicReportConfig.getFetchType() == 2L) {
			data = readProjectionDataStatic(mainMap);
		} else if (!ObjectUtil.isEmpty(dynamicReportConfig) && dynamicReportConfig.getFetchType() == 3L) {

			data = readProjectionDataView(mainMap);
		} else {
			data = readData();
		}
		setGridIdentity(IReportDAO.MAIN_GRID);
		/* removing ID column config detail fro iterating */
		dynamicReportConfig.getDynmaicReportConfigDetails()
				.remove(dynamicReportConfig.getDynmaicReportConfigDetails().iterator().next());
		/*
		 * removing Branch Dynamic Report COnfig Detail From Iterating, s its
		 * value already added to rows
		 */
		dynamicReportConfig.getDynmaicReportConfigDetails()
				.remove(dynamicReportConfig.getDynmaicReportConfigDetails().iterator().next());

		return sendPesticideSprayedJSONResponse(data);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected String sendPesticideSprayedJSONResponse(Map data) throws Exception {

		JSONObject gridData = new JSONObject();
		gridData.put(PAGE, getPage());
		Object objj = data.get(RECORDS);
		footersum = "";
		if (objj instanceof JSONObject) {
			JSONObject countart = (JSONObject) objj;
			totalRecords = (Integer) countart.get("count");
			if (countart.containsKey("footers")) {
				gridData.put("footersum", countart.get("footers"));
			}
		} else {
			totalRecords = (Integer) data.get(RECORDS);
		}
		gridData.put(TOTAL, java.lang.Math.ceil(totalRecords / Double.valueOf(Integer.toString(getLimit()))));
		gridData.put(RECORDS, totalRecords);
		List list = (List) data.get(ROWS);
		JSONArray rows = new JSONArray();
		if (list != null) {
			branchIdValue = getBranchId();
			if (StringUtil.isEmpty(branchIdValue)) {
				buildBranchMap();
			}
			for (Object record : list) {
				rows.add(toPesticideSprayedJSON(record));
			}
		}
		if (totalRecords > 0) {
			gridData.put("userdata", userDataToJSON());
		} else {
			gridData.put("userdata", userDataToJSON());
		}
		gridData.put(ROWS, rows);
		//
		PrintWriter out = response.getWriter();
		out.println(gridData.toString());

		return null;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toPesticideSprayedJSON(Object obj) {
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		runCount = new AtomicInteger(1);
		String id = "";
		StringBuilder exportParams = new StringBuilder("");
		if (obj instanceof FarmerDynamicData) {
			FarmerDynamicData farmerDynamicData = (FarmerDynamicData) obj;
			rows.add(!StringUtil.isEmpty(farmerDynamicData.getDate())
					? DateUtil.convertDateToString(farmerDynamicData.getDate(), DateUtil.STD_DATE_TIME_FORMAT) : "");

			rows.add(getAgentList().get(farmerDynamicData.getCreatedUser()));
			Farm farm = farmerService.findFarmById(Long.valueOf(farmerDynamicData.getReferenceId()));
			rows.add(!ObjectUtil.isEmpty(farm) ? farm.getFarmName() : "");
			jsonObject.put("id", farmerDynamicData.getId());
			jsonObject.put("cell", rows);
		} else if (obj instanceof Object[]) {
			Object[] arr = (Object[]) obj;
			rows.add(!ObjectUtil.isEmpty(arr) && !ObjectUtil.isEmpty(arr[2]) ? arr[2].toString() : "");
			rows.add(!ObjectUtil.isEmpty(arr) && !ObjectUtil.isEmpty(arr[3]) ? arr[3].toString() : "");
			rows.add(!ObjectUtil.isEmpty(arr) && !ObjectUtil.isEmpty(arr[4]) ? arr[4].toString() : "");
			rows.add(!ObjectUtil.isEmpty(arr) && !ObjectUtil.isEmpty(arr[5]) ? arr[5].toString() : "");
			rows.add(!ObjectUtil.isEmpty(arr) && !ObjectUtil.isEmpty(arr[6]) ? arr[6].toString() : "");
			rows.add(!ObjectUtil.isEmpty(arr) && !ObjectUtil.isEmpty(arr[7]) ? arr[7].toString() : "");
			rows.add(!ObjectUtil.isEmpty(arr) && !ObjectUtil.isEmpty(arr[8]) ? arr[8].toString() : "");
			rows.add(!ObjectUtil.isEmpty(arr) && !ObjectUtil.isEmpty(arr[9]) ? arr[9].toString() : "");
			rows.add(!ObjectUtil.isEmpty(arr) && !ObjectUtil.isEmpty(arr[14]) ? arr[14].toString() : "");
			rows.add(!ObjectUtil.isEmpty(arr) && !ObjectUtil.isEmpty(arr[10]) ? arr[10].toString() : "");
			rows.add(!ObjectUtil.isEmpty(arr) && !ObjectUtil.isEmpty(arr[11]) ? arr[11].toString() : "");
			rows.add(!ObjectUtil.isEmpty(arr) && !ObjectUtil.isEmpty(arr[12]) ? arr[12].toString() : "");
			rows.add(!ObjectUtil.isEmpty(arr) && !ObjectUtil.isEmpty(arr[13]) ? arr[13].toString() : "");

			jsonObject.put("id", id);
			jsonObject.put("cell", rows);

		}
		return jsonObject;

	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getOptions(String methodName) {

		Map<String, String> returnMap = new LinkedHashMap<String, String>();
		try {
			Method method = this.getClass().getMethod(methodName);
			Object returnObj = method.invoke(this);
			if (!ObjectUtil.isEmpty(returnObj)) {
				returnMap = (Map<String, String>) returnObj;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnMap;
	}

	public String getPesticideFilterList() {
		return pesticideFilterList;
	}

	public void setPesticideFilterList(String pesticideFilterList) {
		this.pesticideFilterList = pesticideFilterList;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public List<FarmCrops> getFarmCrops() {
		return farmCrops;
	}

	public void setFarmCrops(List<FarmCrops> farmCrops) {
		this.farmCrops = farmCrops;
	}

	public FarmCrops getFarmCrop() {
		return farmCrop;
	}

	public void setFarmCrop(FarmCrops farmCrop) {
		this.farmCrop = farmCrop;
	}

	public HarvestSeason getSeasons() {
		return seasons;
	}

	public void setSeasons(HarvestSeason seasons) {
		this.seasons = seasons;
	}

	public Long getFarmersId() {
		return farmersId;
	}

	public void setFarmersId(Long farmersId) {
		this.farmersId = farmersId;
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

	public Map<String, String> getProofList() {

		Map<String, String> groupPositionList = new LinkedHashMap<>();

		groupPositionList = getFarmCatalougeMap(Integer.valueOf(getText("idProof")));
		if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)) {
			groupPositionList.put("99", "Others");
		}
		return groupPositionList;
	}

	public Integer getSeasonType() {
		return seasonType;
	}

	public void setSeasonType(Integer seasonType) {
		this.seasonType = seasonType;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getSelectedVillage() {
		return selectedVillage;
	}

	public void setSelectedVillage(String selectedVillage) {
		this.selectedVillage = selectedVillage;
	}

	public String getScoreVal() {
		return scoreVal;
	}

	public void setScoreVal(String scoreVal) {
		this.scoreVal = scoreVal;
	}

	public String getCreatedUsername() {
		return createdUsername;
	}

	public void setCreatedUsername(String createdUsername) {
		this.createdUsername = createdUsername;
	}

	public String getFarmers() {
		return farmers;
	}

	public void setFarmers(String farmers) {
		this.farmers = farmers;
	}

	public String getInsDate() {
		return insDate;
	}

	public void setInsDate(String insDate) {
		this.insDate = insDate;
	}

	public Long getFarmerDynamicDataId() {
		return farmerDynamicDataId;
	}

	public void setFarmerDynamicDataId(Long farmerDynamicDataId) {
		this.farmerDynamicDataId = farmerDynamicDataId;
	}

	String watherInfo = "";

	@SuppressWarnings("unchecked")
	public void populateDynmaicFieldValuesByRefId() {
		if (!StringUtil.isEmpty(getSelectedObject()) && !StringUtil.isEmpty(getTxnTypez())) {
			List<DynamicFeildMenuConfig> dyList = farmerService.findDynamicMenusByType(getTxnTypez());
			if (dyList != null && !dyList.isEmpty()) {
				List<DynamicFieldConfig> dynamicFieldsConfigList = (List<DynamicFieldConfig>) dyList.stream()
						.flatMap(listContainer -> listContainer.getDynamicFieldConfigs().stream())
						.collect(Collectors.toList()).stream().map(u -> u.getField()).collect(Collectors.toList());
				Map<String, String> listFields = new LinkedHashMap<>();
				dynamicFieldsConfigList.stream()
						.filter(dynamicFieldConfig -> !ObjectUtil.isEmpty(dynamicFieldConfig.getReferenceId())
								&& StringUtil.isLong(dynamicFieldConfig.getReferenceId()))
						.forEach(dynamicFieldConfig -> {
							listFields.put(dynamicFieldConfig.getCode(),
									String.valueOf(dynamicFieldConfig.getReferenceId()));
						});

				Map<String, List<JSONObject>> jsonMap = new LinkedHashMap<>();
				List<JSONObject> groupList = new LinkedList<>();
				List<JSONObject> fieldList = new LinkedList<>();

				farmerService.listFarmerDynmaicFieldsByRefId(getSelectedObject(), getTxnTypez(), Long.valueOf(id))
						.stream().forEach(dynmaicFieldConfig -> {

							if (ObjectUtil.isEmpty(dynmaicFieldConfig.getParentId())
									|| dynmaicFieldConfig.getParentId() == 0) {
								JSONObject obj = new JSONObject();
								obj.put("code", dynmaicFieldConfig.getFieldName());
								obj.put("name", dynmaicFieldConfig.getFieldValue() == null ? ""
										: dynmaicFieldConfig.getFieldValue());
								obj.put("componentType", dynmaicFieldConfig.getComponentType());
								if (dynmaicFieldConfig.getFieldValue() != null) {
									if (!StringUtil.isEmpty(dynmaicFieldConfig.getComponentType())
											&& Arrays.asList(
													String.valueOf(
															DynamicFieldConfig.COMPONENT_TYPES.DROP_DOWN.ordinal()),
													String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.RADIO.ordinal()),
													String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT
															.ordinal()),
													String.valueOf(
															DynamicFieldConfig.COMPONENT_TYPES.CHECKBOX.ordinal()))
													.contains(dynmaicFieldConfig.getComponentType())
											&& dynmaicFieldConfig.getAccessType() != null
											&& (dynmaicFieldConfig.getAccessType().equals(1)
													|| dynmaicFieldConfig.getAccessType().equals(2))) {
										if (DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal() == Integer
												.valueOf(dynmaicFieldConfig.getComponentType())
												|| DynamicFieldConfig.COMPONENT_TYPES.CHECKBOX.ordinal() == Integer
														.valueOf(dynmaicFieldConfig.getComponentType())) {
											obj.put("dispName",
													getCatlogueValueByCodeArray(dynmaicFieldConfig.getFieldValue()));
										} else {
											obj.put("dispName",
													getCatlogueValueByCode(dynmaicFieldConfig.getFieldValue())
															.getName());
										}
									} else if (!StringUtil.isEmpty(dynmaicFieldConfig.getComponentType())
											&& Arrays.asList(
													String.valueOf(
															DynamicFieldConfig.COMPONENT_TYPES.DROP_DOWN.ordinal()),
													String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.RADIO.ordinal()),
													String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT
															.ordinal()),
													String.valueOf(
															DynamicFieldConfig.COMPONENT_TYPES.CHECKBOX.ordinal()))
													.contains(dynmaicFieldConfig.getComponentType())
											&& dynmaicFieldConfig.getAccessType() != null
											&& dynmaicFieldConfig.getAccessType().equals(3)) {
										try {
											LIST_METHOD[] methods = DynamicFieldConfig.LIST_METHOD.values();
											Map<String, String> listValeus = getOptions(
													methods[Integer.valueOf(dynmaicFieldConfig.getListMethod())]
															.toString());

											if (DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal() == Integer
													.valueOf(dynmaicFieldConfig.getComponentType())
													&& dynmaicFieldConfig.getFieldValue().contains(",")) {

												obj.put("dispName", listValeus.entrySet().stream()
														.filter(u -> Arrays
																.asList(dynmaicFieldConfig.getFieldValue().split(","))
																.contains(u.getKey()))
														.map(u -> u.getValue()).collect(Collectors.joining(",")));
											} else {
												obj.put("dispName",
														listValeus.containsKey(dynmaicFieldConfig.getFieldValue())
																? listValeus.get(dynmaicFieldConfig.getFieldValue())
																: "");
											}

										} catch (Exception e) {
											obj.put("dispName", "");
										}
									} else if (Integer.parseInt(dynmaicFieldConfig
											.getComponentType()) == DynamicFieldConfig.COMPONENT_TYPES.WEATHER_INFO
													.ordinal()) {
										watherInfo = "";
										if (dynmaicFieldConfig.getFieldValue() != null
												&& !StringUtil.isEmpty(dynmaicFieldConfig.getFieldValue())) {
											String[] arr = dynmaicFieldConfig.getFieldValue().split("\\|");
											AtomicInteger i = new AtomicInteger(0);
											Arrays.asList(getLocaleProperty("temp"), getLocaleProperty("rain"),
													getLocaleProperty("humidity"), getLocaleProperty("windSpeed"))
													.stream().forEach(u -> {
														try {
															watherInfo += u + " : "
																	+ arr[i.getAndIncrement()].toString() + " \n ";
														} catch (ArrayIndexOutOfBoundsException e) {
															watherInfo += u + " : " + " \n ";
														}
													});
										}

										obj.put("dispName", watherInfo);
									} else {
										obj.put("dispName", dynmaicFieldConfig.getFieldValue());
									}
								}
								if (dynmaicFieldConfig.getImageIds() != null
										&& !StringUtil.isEmpty(dynmaicFieldConfig.getImageIds())) {
									obj.put("photoCompoAvailable", "1");
									if (dynmaicFieldConfig.getComponentType().equals("13")
											|| dynmaicFieldConfig.getComponentType().equals("11")) {
										obj.put("photoCompoAvailable", "2");
									}
									obj.put("photoIds", dynmaicFieldConfig.getImageIds());
								} else {
									obj.put("photoCompoAvailable", "0");
									obj.put("photoByteStr", "");
								}
								fieldList.add(obj);
							} else {
								JSONObject obj = new JSONObject();
								obj.put("code", dynmaicFieldConfig.getFieldName());
								obj.put("name", dynmaicFieldConfig.getFieldValue() == null ? ""
										: dynmaicFieldConfig.getFieldValue());
								obj.put("refId", dynmaicFieldConfig.getParentId());
								obj.put("typez",
										dynmaicFieldConfig.getTypez()
												+ (dynmaicFieldConfig.getFarmerDynamicData() == null ? ""
														: "_" + dynmaicFieldConfig.getFarmerDynamicData().getId()));
								obj.put("componentType", dynmaicFieldConfig.getComponentType());
								if (dynmaicFieldConfig.getFieldValue() != null) {
									if (!StringUtil.isEmpty(dynmaicFieldConfig.getComponentType())
											&& Arrays.asList(
													String.valueOf(
															DynamicFieldConfig.COMPONENT_TYPES.DROP_DOWN.ordinal()),
													String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.RADIO.ordinal()),
													String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT
															.ordinal()),
													String.valueOf(
															DynamicFieldConfig.COMPONENT_TYPES.CHECKBOX.ordinal()))
													.contains(dynmaicFieldConfig.getComponentType())
											&& dynmaicFieldConfig.getAccessType() != null
											&& (dynmaicFieldConfig.getAccessType().equals(1)
													|| dynmaicFieldConfig.getAccessType().equals(2))) {
										if (DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal() == Integer
												.valueOf(dynmaicFieldConfig.getComponentType())) {
											obj.put("dispName",
													getCatlogueValueByCodeArray(dynmaicFieldConfig.getFieldValue()));
										} else {
											obj.put("dispName",
													getCatlogueValueByCode(dynmaicFieldConfig.getFieldValue())
															.getName());
										}
									} else if (!StringUtil.isEmpty(dynmaicFieldConfig.getComponentType())
											&& Arrays.asList(
													String.valueOf(
															DynamicFieldConfig.COMPONENT_TYPES.DROP_DOWN.ordinal()),
													String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.RADIO.ordinal()),
													String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT
															.ordinal()),
													String.valueOf(
															DynamicFieldConfig.COMPONENT_TYPES.CHECKBOX.ordinal()))
													.contains(dynmaicFieldConfig.getComponentType())
											&& dynmaicFieldConfig.getAccessType() != null
											&& dynmaicFieldConfig.getAccessType().equals(3)) {
										try {
											LIST_METHOD[] methods = DynamicFieldConfig.LIST_METHOD.values();
											Map<String, String> listValeus = getOptions(
													methods[Integer.valueOf(dynmaicFieldConfig.getListMethod())]
															.toString());

											if (DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal() == Integer
													.valueOf(dynmaicFieldConfig.getComponentType())
													&& dynmaicFieldConfig.getFieldValue().contains(",")) {

												obj.put("dispName", listValeus.entrySet().stream()
														.filter(u -> Arrays
																.asList(dynmaicFieldConfig.getFieldValue().split(","))
																.contains(u.getKey()))
														.map(u -> u.getValue()).collect(Collectors.joining(",")));
											} else {
												obj.put("dispName",
														listValeus.containsKey(dynmaicFieldConfig.getFieldValue())
																? listValeus.get(dynmaicFieldConfig.getFieldValue())
																: "");
											}

										} catch (Exception e) {
											obj.put("dispName", "");
										}
									} else {
										obj.put("dispName", dynmaicFieldConfig.getFieldValue());
									}
								}
								if (dynmaicFieldConfig.getImageIds() != null
										&& !StringUtil.isEmpty(dynmaicFieldConfig.getImageIds())) {
									obj.put("photoCompoAvailable", "1");
									if (dynmaicFieldConfig.getComponentType().equals("13")
											|| dynmaicFieldConfig.getComponentType().equals("11")) {
										obj.put("photoCompoAvailable", "2");
									}
									obj.put("photoIds", dynmaicFieldConfig.getImageIds());
								} else {
									obj.put("photoCompoAvailable", "0");
									obj.put("photoByteStr", "");
								}
								groupList.add(obj);
							}

						});// Date of Illness

				jsonMap.put("fields", fieldList);
				jsonMap.put("groups", groupList);

				JSONObject objects = new JSONObject();
				objects.putAll(jsonMap);

				printAjaxResponse(objects, "text/json");
			}
		}

	}

	public Map<String, String> getGenderList() {
		Map<String, String> genderType = new LinkedHashMap<>();
		genderType.put(Farmer.SEX_MALE, getText("MALE"));
		genderType.put(Farmer.SEX_FEMALE, getText("FEMALE"));
		return genderType;
	}

	public Map<String, String> getCreatedUserList() {
		Map<String, String> lotNoMap = new LinkedHashMap<>();
		lotNoMap = farmerService.listFarmerByCreatedUser().stream().filter(f -> f != null && !StringUtil.isEmpty(f))
				.collect(Collectors.toMap(k -> k.toString(), v -> v.toString()));
		return lotNoMap;

	}

	public String getColdStorageCode() {
		return coldStorageCode;
	}

	public void setColdStorageCode(String coldStorageCode) {
		this.coldStorageCode = coldStorageCode;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getChamberCode() {
		return chamberCode;
	}

	public void setChamberCode(String chamberCode) {
		this.chamberCode = chamberCode;
	}

	public String getFloorCode() {
		return floorCode;
	}

	public void setFloorCode(String floorCode) {
		this.floorCode = floorCode;
	}

	public String getBayCode() {
		return bayCode;
	}

	public void setBayCode(String bayCode) {
		this.bayCode = bayCode;
	}

	public String getSubGridCols() {
		return subGridCols;
	}

	public void setSubGridCols(String subGridCols) {
		this.subGridCols = subGridCols;
	}

	public String subGridDetail() throws Exception {

		/*
		 * ESESystem preferences = preferncesService.findPrefernceById("1");
		 * 
		 * ProcurementDetail procurementDetail = new ProcurementDetail();
		 * Procurement procurement = new Procurement();
		 * procurement.setId(Long.valueOf(id));
		 * procurementDetail.setProcurement(procurement); super.filter =
		 * procurementDetail;
		 */

		try {
			Map<String, String> filtersList = new HashMap<String, String>();
			if (subDynamicReportConfig.getAlias().contains(",")) {
				String par = subDynamicReportConfig.getAlias().split(",")[0];
				filtersList.put(par.split("=")[0] + ".id", "7~" + id + "");
			} else {
				filtersList.put(subDynamicReportConfig.getAlias().split("=")[0] + ".id", "7~" + id + "");
			}
			mainMap.put(DynamicReportConfig.FILTER_FIELDS, filtersList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		otherMap.put(DynamicReportConfig.DYNAMIC_CONFIG_DETAIL, subDynamicReportConfig.getDynmaicReportConfigDetails());
		otherMap.put(DynamicReportConfig.ENTITY, subDynamicReportConfig.getEntityName());
		otherMap.put(DynamicReportConfig.ALIAS, subDynamicReportConfig.getAlias());
		otherMap.put(DynamicReportConfig.GROUP_PROPERTY, subDynamicReportConfig.getGroupProperty());
		mainMap.put(DynamicReportConfig.OTHER_FIELD, otherMap);

		// mainMap.put(DynamicReportConfig.FILTER_FIELDS, filtersList);
		Map data = null;
		if (!ObjectUtil.isEmpty(subDynamicReportConfig) && subDynamicReportConfig.getFetchType() == 2L) {
			data = readProjectionDataStatic(mainMap);
		} else {
			data = readData();
		}
		setGridIdentity(IReportDAO.SUB_GRID);
		return sendJSONResponse(data);
	}

	public int getIsSubGrid() {
		return isSubGrid;
	}

	public void setIsSubGrid(int isSubGrid) {
		this.isSubGrid = isSubGrid;
	}

	public String getDetailMethod() {
		return detailMethod;
	}

	public void setDetailMethod(String detailMethod) {
		this.detailMethod = detailMethod;
	}

	public String getDefaultFilter() {

		dynamicReportConfig.getDynmaicReportConfigFilters().stream()
				.filter(reportConfigFilter -> !StringUtil.isEmpty(reportConfigFilter.getDefaultFilter()))
				.forEach(reportConfigFilter -> {

					if (reportConfigFilter.getStatus() == 1) {
						defFilter = defFilter.concat(
								reportConfigFilter.getField() + ":" + reportConfigFilter.getDefaultFilter() + ":1,");
					} else {
						defFilter = defFilter.concat(
								reportConfigFilter.getField() + ":" + reportConfigFilter.getDefaultFilter() + ":0,");
					}
				});
		
		if (hit) {
			defFilter = defFilter.substring(0, defFilter.length() - 1);
			setDefFilters(defFilter);
			hit = false;
		}
	
		
		return defFilter;
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
	
	public Map<String, String> farmerCultiList() {
		Map<String, String> farmerCultiMap = new LinkedHashMap<>();
		farmerCultiList = farmerService.listCultivationByFarmerIncome(branchId);
		farmerCultiList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			farmerCultiMap.put(String.valueOf(objArr[3]), String.valueOf(objArr[0]));
		});
		return farmerCultiMap;
	}

	public Map<String, String> VillageCultiList() {
		Map<String, String> villageCultiMap = new LinkedHashMap<>();
	if(ObjectUtil.isListEmpty(farmerCultiList))
		farmerCultiList = farmerService.listCultivationByFarmerIncome(branchId);
	farmerCultiList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			villageCultiMap.put(String.valueOf(objArr[2]), String.valueOf(objArr[1]));
		});
		return villageCultiMap;
	}

	public Map<String, String> getListofSamithiCodenName() {
		Map<String, String> warehouseMap = new LinkedHashMap<>();
		warehouseMap = locationService.listOfSamithi().stream()
				.collect(Collectors.toMap(warehouse -> String.valueOf(warehouse.getCode()), Warehouse::getName));
		return warehouseMap;
	}
	public String getSeasonName(String code) {
		String season = getSeasonsList().get(code);
		return season;
	}
	
		public String getDecimalFormat(String value){
		
		
		return CurrencyUtil.getDecimalFormat((Double.valueOf(value)), "#,###.0");
		
	}
		
		public void populateCalendarValues() throws Exception {
			if (!ObjectUtil.isEmpty(selectedFarm) && !ObjectUtil.isEmpty(selectedSeason)
					&&!selectedFarm.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedFarm)) 
					&& !selectedSeason.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedSeason))) {
				List<Object[]> listActivity = productDistributionService.listFarmCropCalendarByFarmAndSeason(Long.valueOf(selectedFarm),selectedSeason);
				String status="";
				JSONArray activityArr = new JSONArray();
				for(Object[] obj : listActivity){
					JSONObject activityJSONObject = new JSONObject();
					activityJSONObject.put("productName", !ObjectUtil.isEmpty(obj[0]) ?  obj[0].toString() : "");
					activityJSONObject.put("varietyName", !ObjectUtil.isEmpty(obj[1]) ?  obj[1].toString() : "");				
					if(!ObjectUtil.isEmpty(obj[2])){
						FarmCatalogue catalogue = getCatlogueValueByCode(obj[2].toString());
						activityJSONObject.put("activityName", catalogue.getName());
					}else{
						activityJSONObject.put("activityName", "");
					}
					
					if(!ObjectUtil.isEmpty(obj[3]) && obj[3].toString().equalsIgnoreCase("1")){
						status="In Complete";
					}else{
						status="Complete";
					}
					activityJSONObject.put("status", status);	
					activityJSONObject.put("date",!ObjectUtil.isEmpty(obj[4])&&(obj[4])!= null ?  obj[4].toString() : "");
					activityJSONObject.put("remarks", !ObjectUtil.isEmpty(obj[5]) ?  obj[5].toString() : "");
					activityArr.add(activityJSONObject);
				}
				sendAjaxResponse(activityArr);
			}
		}

		public String getSelectedFarm() {
			return selectedFarm;
		}

		public void setSelectedFarm(String selectedFarm) {
			this.selectedFarm = selectedFarm;
		}

		public String getSelectedSeason() {
			return selectedSeason;
		}

		public void setSelectedSeason(String selectedSeason) {
			this.selectedSeason = selectedSeason;
		}
		
		public Map<String, String> getProductListByCode() {
			Map<String, String> prodMap = new HashMap<String, String>();

			List<Object[]> cat = productService.listOfProducts();
			for (Object[] obj : cat) {
			prodMap.put(String.valueOf(obj[1]), String.valueOf(obj[2]));
			}
			return prodMap;
			}
		
		public String getTxnType(String txnDesc){
			String value="";
				if(!StringUtil.isEmpty(txnDesc)){
					String[] val=txnDesc.split("\\|");
					
					value=val[0];
			}
				
			return value;
		}
		
		public String getRemarks(String txnDesc){
			String value="";
				if(!StringUtil.isEmpty(txnDesc) && txnDesc.contains("|")){
					String[] val=txnDesc.split("\\|");
					if(val.length>0)
					value=val[1];
			}
				
			return value;
		}
		
}
