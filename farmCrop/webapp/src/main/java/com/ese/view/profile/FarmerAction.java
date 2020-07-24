/*
 * FarmerAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.profile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.script.ScriptException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.ese.entity.DynamicData;
import com.ese.entity.txn.mfi.InterestCalcConsolidated;
import com.ese.entity.util.ESESystem;
import com.ese.entity.util.FarmCropsField;
import com.ese.entity.util.FarmField;
import com.ese.entity.util.FarmerField;
import com.ese.util.Base64Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFilesImpl;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.HTML;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.sourcetrace.eses.dao.IReportDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.AgentType;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.DynamicFieldConfig;
import com.sourcetrace.eses.entity.DynamicSectionConfig;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.inspect.agrocert.CertificateCategory;
import com.sourcetrace.eses.inspect.agrocert.CertificateStandard;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.ICardService;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.ICertificationService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IESETxnService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.KMLUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.entity.Image;
import com.sourcetrace.eses.util.entity.ImageInfo;
import com.sourcetrace.eses.util.entity.JsonDataObject;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.BankInformation;
import com.sourcetrace.esesw.entity.profile.Coordinates;
import com.sourcetrace.esesw.entity.profile.Country;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.CustomerProject;
import com.sourcetrace.esesw.entity.profile.DynamicImageData;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.ESECard;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.FarmCropsCoordinates.typesOFCordinates;
import com.sourcetrace.esesw.entity.profile.FarmIcsConversion;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicData;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicFieldsValue;
import com.sourcetrace.esesw.entity.profile.FarmerEconomy;
import com.sourcetrace.esesw.entity.profile.FarmerHealthAsses;
import com.sourcetrace.esesw.entity.profile.FarmerIncomeDetails;
import com.sourcetrace.esesw.entity.profile.FarmerSelfAsses;
import com.sourcetrace.esesw.entity.profile.FarmerSourceIncome;
import com.sourcetrace.esesw.entity.profile.GramPanchayat;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.Locality;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.State;
import com.sourcetrace.esesw.entity.profile.TreeDetail;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.view.SwitchValidatorAction;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import net.glxn.qrgen.QRCode;

public class FarmerAction extends SwitchValidatorAction {

	private static final long serialVersionUID = 1L;
	private final String FILTER_ALL = getText("filter.allStatus");
	public static final String SELECT_MULTI = "-1";
	public static final String EMPTY = "";
	DateFormat df = new SimpleDateFormat(getESEDateFormat());
	private String id;
	private Farmer farmer;
	private ESECard card;
	private ESEAccount account;
	private FarmerEconomy farmerEconomy;
	private String dateOfBirth;
	private String selectedCountry;
	private String selectedState;
	private String selectedLocality;

	private String farmerImageByteString;
	private String fingerPrintImageByteString;
	private String digitalSignatureByteString;
	private String selectedCity;
	private String dateOfJoining;
	private String icsCode;
	private String icsName;
	private String selectedPanchayat;
	private String selectedVillage;
	private String selectedSamithi;
	private String selectedCertificateCategory;
	private String selectedCustomer;
	private String rupee;
	private String paise;
	private String houseHoldDob;
	private String investigatorDate;
	private String exportLimit;
	private String bankAccType;
	private String loanRepaymentDate;
	private ILocationService locationService;
	private IFarmerService farmerService;
	private IUniqueIDGenerator idGenerator;
	private ICardService cardService;
	private IAgentService agentService;
	private IAccountService accountService;
	private IESETxnService txnService;
	private IPreferencesService preferncesService;
	private ICertificationService certificationService;
	private IClientService clientService;
	private IProductDistributionService productDistributionService;
	private String sourceIncomeId;
	private String consElecName;
	private String totalName;
	private String investigatorName;
	private String farmerCropInsurance;
	private String formationDate;
	protected String sidx;
	protected String sord;
	protected Date sDate = null;
	protected Date eDate = null;
	protected int page;
	protected int rows;
	private String selectedCatalogue;
	private String shgName;
	private String selectedMasterData;
	private String selectedVillageCode;
	Map<String, String> genderType = new LinkedHashMap<String, String>();
	List<Locality> listLocalities = new ArrayList<Locality>();
	List<State> states = new ArrayList<State>();
	List<Object[]> farmsCount=new ArrayList<Object []>();
	List<Municipality> cities = new ArrayList<Municipality>();
	List<GramPanchayat> panchayat = new ArrayList<GramPanchayat>();
	List<Warehouse> samithi = new ArrayList<Warehouse>();
	List<Village> villages = new ArrayList<Village>();
	Map<Integer, String> cardRewriteList = new LinkedHashMap<Integer, String>();
	Map<Integer, String> cardStatusList = new LinkedHashMap<Integer, String>();
	Map<Integer, String> listAccountStatus = new LinkedHashMap<Integer, String>();
	Map<Integer, String> farmerStatus = new LinkedHashMap<Integer, String>();
	private List<CertificateStandard> certificateStandards = new ArrayList<CertificateStandard>();
	private List<Customer> customers = new LinkedList<Customer>();
	private List<CustomerProject> customerProjects = new LinkedList<CustomerProject>();
	private Map<Integer, String> certificationTypes = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> certificationLevels = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> maritalSatuses = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> educationList = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> housingOwnerships = new LinkedHashMap<Integer, String>();
	private Map<String, String> farmsizepMap = new LinkedHashMap<String, String>();
	Map<Long, Object[]> farmerMaps = new HashMap<>();
	// private Map<Integer, String> housingTypes = new LinkedHashMap<Integer,
	// String>();
	private Map<Integer, String> inspectionTypes = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> icsStatuses = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> enrollmentMap = new LinkedHashMap<Integer, String>();
	// private Map<Integer, String> socialCategoryList = new
	// LinkedHashMap<Integer, String>();
	private Map<Integer, String> religionList = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> familyTypeList = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> houseHoldLandList = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> houseHoldOccupationList = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> familyMemberList = new LinkedHashMap<Integer, String>();
	private Map<String, String> sourceNameMap = new LinkedHashMap<String, String>();
	private Map<Integer, String> aliedSectorMap = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> employmentMap = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> otherIncomeMap = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> investigatorNameList = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> beneficiaryInGovSchemelist = new LinkedHashMap<Integer, String>();
	List<FarmerSourceIncome> farmerSourceIncomeList = new LinkedList<FarmerSourceIncome>();

	// private Map<Integer, String> categoryList = new LinkedHashMap<Integer,
	// String>();
	private Map<Integer, String> statusList = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> headOfFamilyList = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> grsList = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> paidShareCapitialList = new LinkedHashMap<Integer, String>();
	private Map<String, String> loanSecurityList = new LinkedHashMap<String, String>();
	private Map<String, String> loanPurposeList = new LinkedHashMap<String, String>();
	private Map<Integer, String> interesetPeriod = new LinkedHashMap<Integer, String>();
	private List<Farm> farms;
	private List<FarmCrops> farmCrop=new ArrayList<FarmCrops>() ;
	private Map<Integer, String> processingActList = new LinkedHashMap<Integer, String>();
	// List<Shg> shg = new ArrayList<Shg>();

	private String tabIndex = "#tabs-1";
	private boolean farmerAndContractStatus = false;
	public static final int FAMRMER_ACTIVE = 1;
	public static final int OTHER = 99;
	private String xlsFileName;
	private InputStream fileInputStream;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	private InterestCalcConsolidated interestCalcConsolidated;
	private static final int SELECT = -1;

	private static final String IRP = "irp";
	private static final String FARMER = "farmer";

	private String isInterestModule = "0";

	private String searchPage;
	private File farmerImage;
	private Image image;
	private String isCertifiedFarmerInfoEnabled;

	private JsonDataObject jsonDataObject = new JsonDataObject();
	private Gson gson = new Gson();
	private String cropInfoEnabled;
	private BankInformation bankInfo;
	Map<Integer, String> isFarmerCertified = new LinkedHashMap<Integer, String>();
	private Double balance;
	private Map<Integer, String> isToiletAvailable = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> toiletAvailableFromMap = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> vehicleMap = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> consumerElecMap = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> cellPhoneMap = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> housingOwnershipMap = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> availCellPhone = new LinkedHashMap<Integer, String>();
	// private Map<Integer, String> housingTypeMap = new LinkedHashMap<Integer,
	// String>();
	private Map<String, String> drinkingWSMap = new LinkedHashMap<String, String>();
	private Map<Integer, String> electrifiedHouseMap = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> lifeOrHealthInsuranceMap = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> cropInsuranceMap = new LinkedHashMap<Integer, String>();
	Map<Integer, String> isLoanTakenLastYear = new LinkedHashMap<Integer, String>();
	Map<String, String> catalogueList = new LinkedHashMap<String, String>();
	Map<String, String> catalogueList1 = new LinkedHashMap<String, String>();
	Map<String, String> catalogueList2 = new LinkedHashMap<String, String>();
	Map<String, String> catalogueList3 = new LinkedHashMap<String, String>();
	Map<String, String> catalogueList4 = new LinkedHashMap<String, String>();

	// private Map<Integer, String> loanTakenFromMap = new
	// LinkedHashMap<Integer, String>();
	private String educationName;
	private ICatalogueService catalogueService;
	private String fpoEnabled;
	private String fingerPrintEnabled;
	private String gramPanchayatEnable;
	private String accBalance;
	private String farmerCodeEnabled;
	private String farmerBankInfoEnabled;
	private String selectedSangham;
	private String enableHHCode;
	private String selectedGroupPosition;
	private String sanghamName;
	private String familyMember;
	private String ifs;
	private String agriIncome;
	private String hortiIncome;
	private String aliedIncome;
	private String employmentIncome;
	private String otherIncome;
	private String agriTestJson;

	private String icsString;
	private String icsDropDown;
	private String icsRegNoString;
	private String icsUnitNoString;
	private String idProofEnabled;
	private String insuranceInfoEnabled;


	private String farmerPluginFields;

	Map<Integer, String> listInsurance = new LinkedHashMap<Integer, String>();
	Map<String, String> cookingFuelMap = new LinkedHashMap<String, String>();
	Map<String, String> govtDeptMap = new LinkedHashMap<String, String>();
	Map<String, String> farmerCropNames = new LinkedHashMap<String, String>();

	Map<String, String> homeDifficultyMap = new LinkedHashMap<String, String>();
	Map<String, String> workDiffficultyMap = new LinkedHashMap<String, String>();
	Map<String, String> communitiyDifficultyMap = new LinkedHashMap<String, String>();
	List<Map<String, String>> farmerPrintMap = new ArrayList<>();

	private String accTypeString;
	DateFormat sdf = new SimpleDateFormat(DateUtil.PROFILE_DATE_FORMAT);
	// SimpleDateFormat sm = new SimpleDateFormat("dd/MM/yyyy");
	Map<String, DynamicFieldConfig> fieldConfigMap = new HashMap<>();
	private List<FarmerField> farmerFields;
	private String farmerDynamicDatas;
	private String farmerDynamicValIds;
	private String validateFileds;
	private String selectedDisability;
	private String selectedInputSource;
	private String healthAssesmentJSON;
	private String selfAssesmentJSON;
	private String type;

	private String selectedBranch;
	private String codeForCropChart;
	private String locationLevel;
	private File idProofImg;
	private String idProofImgString;
	private Map<String, String> disabilityList = new LinkedHashMap<String, String>();
	Map<String, String> originTypes = new LinkedHashMap<>();
	private String idImgAvil;
	private String kmlFileName;
	private String kmlZipFileName;
	private String certificationStandardLevel;
	private String enableContractTemplate;
	private String farmerCode;
	private List<TreeDetail> treeDetails;
	private List<TreeDetail> treeDetailss;
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	private String startDate;
	private String endDate;
	private String seasonCode;

	public Map<String, String> getIsLoanTakenSchemeList() {
		Map<String, String> loanMap = new LinkedHashMap<>();
		loanMap.put("0", getText("no"));
		loanMap.put("1", getText("yes"));
		return loanMap;
	}

	public Map<String, String> getIsDisabledList() {
		Map<String, String> loanMap = new LinkedHashMap<>();
		loanMap.put("0", getText("no"));
		loanMap.put("1", getText("yes"));
		return loanMap;

	}

	public String getFarmerDynamicDatas() {
		return farmerDynamicDatas;
	}

	public void setFarmerDynamicDatas(String farmerDynamicDatas) {
		this.farmerDynamicDatas = farmerDynamicDatas;
	}

	private List<DynamicSectionConfig> dynamicFieldsList;

	private String selectedDisabled;

	public Map<String, String> getSamithiSangham() {

		Map<String, String> samithiMap = new HashMap<>();

		if ((!StringUtil.isEmpty(selectedVillage)) && !StringUtil.isEmpty(selectedSangham)
				&& !selectedVillage.equalsIgnoreCase("null")) {
			Village village = locationService.findVillageById(Long.valueOf(selectedVillage));
			if (!ObjectUtil.isEmpty(village))
				// samithi =
				// locationService.listSamithiByVillageId(village.getId());
				if (!ObjectUtil.isEmpty(selectedSangham) && !ObjectUtil.isEmpty(village))
				samithi = locationService.listSamithiBySanghamType(selectedSangham);
			/*
			 * if (!getCurrentTenantId().equalsIgnoreCase("atma")) { samithi =
			 * locationService.listSamithiBySangham(selectedSangham,
			 * village.getCode()); } else { samithi =
			 * locationService.listSamithiBySanghamType(selectedSangham); }
			 */
			samithiMap = samithi.stream().collect(Collectors.toMap(Warehouse::getCode, Warehouse::getName));
		}

		return samithiMap;
	}

	public String getSelectedGroupPosition() {

		return selectedGroupPosition;
	}

	public void setSelectedGroupPosition(String selectedGroupPosition) {

		this.selectedGroupPosition = selectedGroupPosition;
	}

	public String getSelectedSangham() {

		return selectedSangham;
	}

	public void setSelectedSangham(String selectedSangham) {

		this.selectedSangham = selectedSangham;
	}

	public String getGramPanchayatEnable() {

		return gramPanchayatEnable;
	}

	public void setGramPanchayatEnable(String gramPanchayatEnable) {

		this.gramPanchayatEnable = gramPanchayatEnable;
	}

	public ICatalogueService getCatalogueService() {

		return catalogueService;
	}

	public void setCatalogueService(ICatalogueService catalogueService) {

		this.catalogueService = catalogueService;
	}

	public String getEducationName() {

		return educationName;
	}

	public void setEducationName(String educationName) {

		this.educationName = educationName;
	}

	/**
	 * @see com.sourcetrace.esesw.view.SwitchAction#data()
	 */
	public String data() throws Exception {
		
		return sendJQGridJSONResponse(buildFilterDataMap());
	}

	public String list() throws Exception {

		if (getCurrentPage() != null) {
			setCurrentPage(getCurrentPage());
		}
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			setExportLimit(preferences.getPreferences().get(ESESystem.EXPORT_RECORD_LIMIT));
		}

		type = request.getParameter("type");
		request.setAttribute(HEADING, getText("farmerlist"));
		return LIST;
	}

	/**
	 * Builds the filter data map.
	 * 
	 * @return the map
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	private Map buildFilterDataMap() throws ParseException {
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Map<String, String> searchRecord = getJQGridRequestParam(); // get the
																	// search
																	// parameter
																	// with
		// value

		Farmer filter = new Farmer();
		Municipality m=new Municipality();
		Locality ld =new Locality();
		State s=new State();
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

		if (!StringUtil.isEmpty(searchRecord.get("farmerId"))) {
			filter.setFarmerId(searchRecord.get("farmerId").trim());
		}
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
			if (StringUtil.isEmpty(getBranchId()) || getBranchId().equalsIgnoreCase("organic")) {
				if (!StringUtil.isEmpty(searchRecord.get("farmersCodeTracenet"))) {
					filter.setFarmersCodeTracenet(searchRecord.get("farmersCodeTracenet").trim());
				}

			}
		} else {
			if (!StringUtil.isEmpty(searchRecord.get("farmerCode"))) {
				filter.setFarmerCode(searchRecord.get("farmerCode").trim());
			}
		}

		if (!StringUtil.isEmpty(searchRecord.get("firstName")))
			filter.setFirstName(searchRecord.get("firstName").trim());

		if (!StringUtil.isEmpty(searchRecord.get("lastName")))
			filter.setLastName(searchRecord.get("lastName").trim());

		if (!StringUtil.isEmpty(searchRecord.get("surName")))
			filter.setSurName(searchRecord.get("surName").trim());

		if (!StringUtil.isEmpty(searchRecord.get("v.name"))) {
			Village village = new Village();
			village.setName(searchRecord.get("v.name").trim());
			filter.setVillage(village);
		}
		if (!StringUtil.isEmpty(searchRecord.get("s.name"))) {
			Warehouse samithi = new Warehouse();
			samithi.setName(searchRecord.get("s.name").trim());
			filter.setSamithi(samithi);
		}
		if(!StringUtil.isEmpty(searchRecord.get("state.name"))){
			s.setName(searchRecord.get("state.name"));
			ld.setState(s);
			m.setLocality(ld);
			filter.setCity(m);
		}
		if(!StringUtil.isEmpty(searchRecord.get("locality.name"))){
			ld.setName(searchRecord.get("locality.name"));
			m.setLocality(ld);
			filter.setCity(m);
		}
		if(!StringUtil.isEmpty(searchRecord.get("city.name"))){
			m.setName(searchRecord.get("city.name"));
			filter.setCity(m);
		}

		if (!StringUtil.isEmpty(searchRecord.get("cs.name"))) {
			CertificateStandard certificateStandard = new CertificateStandard();
			certificateStandard.setName(searchRecord.get("cs.name").trim());
			filter.setCertificateStandard(certificateStandard);
		}
		if (!StringUtil.isEmpty(searchRecord.get("certificationLevel"))) {
			filter.setCertificateStandardLevel(Integer.valueOf(searchRecord.get("certificationLevel").trim()));

		} else {
			filter.setCertificateStandardLevel(-1);
		}

		if (!StringUtil.isEmpty(searchRecord.get("isCertifiedFarmer"))) {
			filter.setCertificationFilter(searchRecord.get("isCertifiedFarmer"));
		}
		
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.AGRO_TENANT)) {
			if (!StringUtil.isEmpty(searchRecord.get("farmSize"))) {
			if ("1".equals(searchRecord.get("farmSize"))) {
			filter.setFilterStatus("farmSize");
			filter.setFarmSize(Farmer.FARM_SIZE_GREATER_THAN_ONE);
			} else if("2".equals(searchRecord.get("farmSize"))){
			filter.setFilterStatus("farmSize");
			filter.setFarmSize(Farmer.FARM_SIZE_LESS_THAN_ONE);
			} else if("3".equals(searchRecord.get("farmSize"))){
			filter.setFilterStatus("farmSize");
			filter.setFarmSize(Farmer.NO_FARM);
			}
			}
			/*else{
			filter.setFarmSize(-1);
			}*/
			}
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.OCP)) {
			if (!StringUtil.isEmpty(searchRecord.get("farmSize"))) {
			if ("1".equals(searchRecord.get("farmSize"))) {
			filter.setFilterStatus("farmSize");
			filter.setFarmSize(Farmer.FARM_SIZE_GREATER_THAN_ONE);
			} else if("2".equals(searchRecord.get("farmSize"))){
			filter.setFilterStatus("farmSize");
			filter.setFarmSize(Farmer.FARM_SIZE_LESS_THAN_ONE);
			} else if("3".equals(searchRecord.get("farmSize"))){
			filter.setFilterStatus("farmSize");
			filter.setFarmSize(Farmer.NO_FARM);
			}
			}
			/*else{
			filter.setFarmSize(-1);
			}*/
			}
		
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)) {
			if (!StringUtil.isEmpty(searchRecord.get("farmSize")) && StringUtil.isDouble(searchRecord.get("farmSize"))) {
				filter.setFarmSize(Double.valueOf(searchRecord.get("farmSize")));
				}
		}
		if (!StringUtil.isEmpty(searchRecord.get("farmSize")) && StringUtil.isDouble(searchRecord.get("farmSize"))) {
			filter.setFarmSize(Double.valueOf(searchRecord.get("farmSize")));
			}
	
		if (!StringUtil.isEmpty(searchRecord.get("status"))) {
			if ("1".equals(searchRecord.get("status"))) {
				filter.setFilterStatus("status");
				filter.setStatus(Farmer.Status.ACTIVE.ordinal());
			} else if ("0".equals(searchRecord.get("status"))) {
				filter.setFilterStatus("status");
				filter.setStatus(Farmer.Status.INACTIVE.ordinal());
			} else if ("3".equals(searchRecord.get("status"))) {
				filter.setFilterStatus("status");
				filter.setStatus(Farmer.Status.WITHDRAWN.ordinal());
			} else if ("4".equals(searchRecord.get("status"))) {
				filter.setFilterStatus("status");
				filter.setStatus(Farmer.Status.SUSPENDED.ordinal());
			} else if ("5".equals(searchRecord.get("status"))) {
				filter.setFilterStatus("status");
				filter.setStatus(Farmer.Status.VIOLATED.ordinal());
			}
		}

		if (!StringUtil.isEmpty(searchRecord.get("mobileNumber"))) {
			filter.setMobileNumber(searchRecord.get("mobileNumber").trim());
		}

		/*
		 * if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PGSS_TENANT_ID))
		 * { if (!StringUtil.isEmpty(getType()) && getType().equals(Farmer.IRP))
		 * { filter.setTypez(Farmer.IRP); } else {
		 * filter.setTypez(Farmer.FARMER); } }
		 */

		if (!StringUtil.isEmpty(searchRecord.get("createdUsername")))
			filter.setCreatedUsername(searchRecord.get("createdUsername").trim());
		
		if (!StringUtil.isEmpty(searchRecord.get("lastUpdatedUsername")))
			filter.setLastUpdatedUsername(searchRecord.get("lastUpdatedUsername").trim());

		if (!StringUtil.isEmpty(searchRecord.get("createdDate"))) {
			DateFormat dff = new SimpleDateFormat("dd-MM-yyyy");
			filter.setCreatedDate(dff.parse(searchRecord.get("createdDate")));
		}
		if (!StringUtil.isEmpty(searchRecord.get("lastUpdatedDate"))) {
			DateFormat dff = new SimpleDateFormat("dd-MM-yyyy");
			filter.setLastUpdatedDate(dff.parse(searchRecord.get("lastUpdatedDate")));
		}

		if (!StringUtil.isEmpty(searchRecord.get("masterData"))) {
			filter.setMasterData(searchRecord.get("masterData"));
		}

		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.IFFCO_TENANT_ID)
				|| getCurrentTenantId().equalsIgnoreCase(ESESystem.AWI_TENANT_ID)) {
			if (!StringUtil.isEmpty(searchRecord.get("fpo"))) {
				filter.setFpo(searchRecord.get("fpo"));
			}
		}

		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)) {
			if (!StringUtil.isEmpty(searchRecord.get("totalAcreage"))) {
				filter.setTotalAcreage(searchRecord.get("totalAcreage"));
			}
		}
		
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)) {
			if (!StringUtil.isEmpty(searchRecord.get("farmSize"))) {
				filter.setTotalCultivatable(searchRecord.get("farmSize"));
			}
		}
		
		filter = setFarmerCropFilter(filter, searchRecord);
		filter.setSearchPage(searchPage);
		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());
		return data;

	}

	private Map buildExportFilterDataMap() throws ParseException {
		Map data = new LinkedHashMap<>();
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Farmer filter = new Farmer();
		Map<String, String> searchRecord = getJQGridRequestParam();
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

		if (!StringUtil.isEmpty(searchRecord.get("farmerId"))) {
			filter.setFarmerId(searchRecord.get("farmerId").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("farmerCode"))) {
			filter.setFarmerCode(searchRecord.get("farmerCode").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("firstName")))
			filter.setFirstName(searchRecord.get("firstName").trim());

		if (!StringUtil.isEmpty(searchRecord.get("lastName")))
			filter.setLastName(searchRecord.get("lastName").trim());

		if (!StringUtil.isEmpty(searchRecord.get("surName")))
			filter.setSurName(searchRecord.get("surName").trim());

		if (!StringUtil.isEmpty(searchRecord.get("v.name"))) {
			Village village = new Village();
			village.setName(searchRecord.get("v.name").trim());
			filter.setVillage(village);
		}

		if (!StringUtil.isEmpty(searchRecord.get("location"))) {
			Village village = new Village();
			Municipality m = new Municipality();
			Locality loca = new Locality();
			loca.setName(searchRecord.get("location").trim());
			m.setLocality(loca);
			village.setCity(m);
			filter.setVillage(village);
		}

		if (!StringUtil.isEmpty(searchRecord.get("s.name"))) {
			Warehouse samithi = new Warehouse();
			samithi.setName(searchRecord.get("s.name").trim());
			filter.setSamithi(samithi);
		}

		if (!StringUtil.isEmpty(searchRecord.get("cs.name"))) {
			CertificateStandard certificateStandard = new CertificateStandard();
			certificateStandard.setName(searchRecord.get("cs.name").trim());
			filter.setCertificateStandard(certificateStandard);
		}

		if (!StringUtil.isEmpty(searchRecord.get("isCertifiedFarmer"))) {
			filter.setCertificationFilter(searchRecord.get("isCertifiedFarmer"));
		}
		
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.AGRO_TENANT)) {
			if (!StringUtil.isEmpty(searchRecord.get("farmSize"))) {
				if ("1".equals(searchRecord.get("farmSize"))) {
				filter.setFilterStatus("farmSize");
				filter.setFarmSize(Farmer.FARM_SIZE_GREATER_THAN_ONE);
				} else if("2".equals(searchRecord.get("farmSize"))){
				filter.setFilterStatus("farmSize");
				filter.setFarmSize(Farmer.FARM_SIZE_LESS_THAN_ONE);
				} else if("3".equals(searchRecord.get("farmSize"))){
				filter.setFilterStatus("farmSize");
				filter.setFarmSize(Farmer.NO_FARM);
				}
				}
				/*else{
				filter.setFarmSize(-1);
				}*/
				}
		
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.OCP)) {
			if (!StringUtil.isEmpty(searchRecord.get("farmSize"))) {
				if ("1".equals(searchRecord.get("farmSize"))) {
				filter.setFilterStatus("farmSize");
				filter.setFarmSize(Farmer.FARM_SIZE_GREATER_THAN_ONE);
				} else if("2".equals(searchRecord.get("farmSize"))){
				filter.setFilterStatus("farmSize");
				filter.setFarmSize(Farmer.FARM_SIZE_LESS_THAN_ONE);
				} else if("3".equals(searchRecord.get("farmSize"))){
				filter.setFilterStatus("farmSize");
				filter.setFarmSize(Farmer.NO_FARM);
				}
				}
				/*else{
				filter.setFarmSize(-1);
				}*/
				}
		
		if (!StringUtil.isEmpty(searchRecord.get("status"))) {
			if ("1".equals(searchRecord.get("status"))) {
				filter.setFilterStatus("status");
				filter.setStatus(Farmer.Status.ACTIVE.ordinal());
			} else if ("0".equals(searchRecord.get("status"))) {
				filter.setFilterStatus("status");
				filter.setStatus(Farmer.Status.INACTIVE.ordinal());
			} else if ("3".equals(searchRecord.get("status"))) {
				filter.setFilterStatus("status");
				filter.setStatus(Farmer.Status.WITHDRAWN.ordinal());
			} else if ("4".equals(searchRecord.get("status"))) {
				filter.setFilterStatus("status");
				filter.setStatus(Farmer.Status.SUSPENDED.ordinal());
			} else if ("5".equals(searchRecord.get("status"))) {
				filter.setFilterStatus("status");
				filter.setStatus(Farmer.Status.VIOLATED.ordinal());
			}
		}

		if (!StringUtil.isEmpty(searchRecord.get("mobileNumber"))) {
			filter.setMobileNumber(searchRecord.get("mobileNumber").trim());
		}

		/*
		 * if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PGSS_TENANT_ID))
		 * { if (!StringUtil.isEmpty(getType()) && getType().equals(Farmer.IRP))
		 * { filter.setTypez(Farmer.IRP); } else {
		 * filter.setTypez(Farmer.FARMER); } }
		 */

		if (!StringUtil.isEmpty(searchRecord.get("createdUsername")))
			filter.setCreatedUsername(searchRecord.get("createdUsername").trim());

		if (!StringUtil.isEmpty(searchRecord.get("createdDate"))) {
			filter.setCreatedDate(df.parse(searchRecord.get("createdDate")));
		}

		if (!StringUtil.isEmpty(searchRecord.get("masterData"))) {
			filter.setMasterData(searchRecord.get("masterData"));
		}
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)) {
			if (!StringUtil.isEmpty(searchRecord.get("totalAcreage"))) {
				filter.setTotalAcreage(searchRecord.get("totalAcreage"));
			}
		}
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.IFFCO_TENANT_ID)
				|| getCurrentTenantId().equalsIgnoreCase(ESESystem.AWI_TENANT_ID)) {
			if (!StringUtil.isEmpty(searchRecord.get("fpo"))) {
				filter.setFpo(searchRecord.get("fpo"));
			}
		}

		filter.setSearchPage(searchPage);
		super.filter = filter;
		// data = readData("farmerExportProp");
		if (!StringUtil.isEmpty(getType()) && getType().equals(Farmer.IRP)) {
			data = readData("farmerExportProp");
		} else {
			data = readData("farmerExport");
		}
		return data;
	}

	/**
	 * @see com.sourcetrace.esesw.view.SwitchAction#toJSON(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		Farmer farmer = (Farmer) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		DecimalFormat df1 = new DecimalFormat("0.00");		
		
		if (!StringUtil.isEmpty(searchPage) && searchPage.equalsIgnoreCase("smsList")) {

			// rows.add(farmer.getFarmerCode());
			rows.add(farmer.getFirstName() + " " + farmer.getSurName() == null ? "" : farmer.getSurName());
			rows.add(farmer.getMobileNumber());
			rows.add(farmer.getVillage() == null ? "" : farmer.getVillage().getName());
			rows.add(farmer.getSamithi() == null ? "" : farmer.getSamithi().getName());

			rows.add(getLocaleProperty("status" + farmer.getStatus()));
			jsonObject.put("id", farmer.getId());
			jsonObject.put("cell", rows);
		} else {
			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(farmer.getBranchId())))
							? getBranchesMap().get(getParentBranchMap().get(farmer.getBranchId()))
							: getBranchesMap().get(farmer.getBranchId()));
				}
				rows.add(getBranchesMap().get(farmer.getBranchId()));

			} else {
				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(branchesMap.get(farmer.getBranchId()));
				}
			}
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
				if (StringUtil.isEmpty(getBranchId())) {
					rows.add(farmer.getBranchId().equalsIgnoreCase("bci")?farmer.getFarmerCode():farmer.getFarmersCodeTracenet());
				} else if(getBranchId().equalsIgnoreCase("bci")) {
					rows.add(farmer.getFarmerCode());
				}else{
					rows.add(farmer.getFarmersCodeTracenet());
				}
			} else if (!getType().equalsIgnoreCase("2") && farmerCodeEnabled.equals("1")
					|| getEnableHHCode().equalsIgnoreCase("1")) {
				rows.add(farmer.getFarmerCode());
			}
			/*
			 * else { if (farmerCodeEnabled.equals("1") ||
			 * getEnableHHCode().equalsIgnoreCase("1")) {
			 * rows.add(farmer.getFarmerCode()); } }
			 */
			// rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">" +
			// (!StringUtil.isEmpty(farmer.getFarmerCode())?farmer.getFarmerCode():"")+
			// "</font>");

			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.OCP)) {
				rows.add(farmer.getFarmerId());
			}

			rows.add(farmer.getFirstName());
			if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.SYMRISE)
					&& !getCurrentTenantId().equalsIgnoreCase("gsma")
					&& !getCurrentTenantId().equalsIgnoreCase("ocp") && !getCurrentTenantId().equalsIgnoreCase(ESESystem.FARM_AGG)) {
				rows.add(farmer.getLastName());
			}
			if (getCurrentTenantId().equalsIgnoreCase("kpf") || getCurrentTenantId().equalsIgnoreCase("movcd")
					|| getCurrentTenantId().equalsIgnoreCase("simfed")
					|| getCurrentTenantId().equalsIgnoreCase("wub")) {
				rows.add(farmer.getSurName());
			}
			if (getCurrentTenantId().equalsIgnoreCase("livelihood")) {
				rows.add(farmer.getVillage() == null ? "" : farmer.getVillage().getCity().getLocality().getState().getName());
				rows.add(farmer.getVillage() == null ? "" : farmer.getVillage().getCity().getLocality().getName());
				rows.add(farmer.getVillage() == null ? "" : farmer.getVillage().getCity().getName());
			}
			rows.add(farmer.getVillage() == null ? "" : farmer.getVillage().getName());
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.SYMRISE)) {
				//rows.add(farmer.getCertificateStandard() == null ? "" : farmer.getCertificateStandard().getName());
				// rows.add(farmer.getCertificateStandardLevel()==0 ?
				// "Non-Certified" :
				// farmer.getCertificateStandardLevel()==1?"Certified":"Certified
				// With Improvement Needed");
				rows.add(certificationLevels.get(farmer.getCertificateStandardLevel()));
			}

			if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.FARM_AGG)) {
			rows.add(ObjectUtil.isEmpty(farmer.getSamithi()) && farmer.getSamithi() == null ? ""
					: farmer.getSamithi().getName());
			}
			/*
			 * if (getCurrentTenantId().equalsIgnoreCase("kpf")) { if
			 * (!StringUtil.isEmpty(farmer.getFpo())) { FarmCatalogue catalogue
			 * = getCatlogueValueByCode(farmer.getFpo());
			 * rows.add(!ObjectUtil.isEmpty(catalogue) ? catalogue.getName() :
			 * ""); } }
			 */
			
			
			if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID) && !getCurrentTenantId().equalsIgnoreCase("kenyafpo") && !getCurrentTenantId().equalsIgnoreCase(ESESystem.FARM_AGG)) {
				if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)
						|| getCurrentTenantId().equalsIgnoreCase(ESESystem.STICKY_TENANT_ID) ) {
					if (!StringUtil.isEmpty(isCertifiedFarmerInfoEnabled) && isCertifiedFarmerInfoEnabled.equals("1")) {
						rows.add(getPropertyValue("cer", farmer.getIsCertifiedFarmer()));
					}
				}
			}
			
			if (getFpoEnabled().equalsIgnoreCase("1")) {
				rows.add(!StringUtil.isEmpty(farmer.getFpo())?getCatlogueValueByCode(farmer.getFpo()).getName():"");				
			}
			if (getCurrentTenantId().equalsIgnoreCase("avt")){
			rows.add(farmer.getFarms() !=null && !ObjectUtil.isEmpty(farmer.getFarms()) && farmer.getFarms().size()>0  ? farmer.getFarms().iterator().next().getCreatedUsername() : "" );
			}
			if (getCurrentTenantId().equalsIgnoreCase("agro")||getCurrentTenantId().equalsIgnoreCase("ocp")||getCurrentTenantId().equalsIgnoreCase("avt")) {
				rows.add(farmer.getFarmSize() == -1.0 ? "NA"
						: df1.format(Double.valueOf(farmer.getFarmSize())));
				
			}
			if (getCurrentTenantId().equalsIgnoreCase("wilmar")) {
				rows.add(StringUtil.isEmpty(farmer.getMasterData()) ? ""
						: getMasterTypeList().get(farmer.getMasterData()));
				SimpleDateFormat df2 = new SimpleDateFormat("MMM dd, yyyy");
				rows.add(!ObjectUtil.isEmpty(farmer) && (!ObjectUtil.isEmpty(farmer.getDateOfJoining()))
						? df2.format(farmer.getDateOfJoining()) : "");
			}
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.IFFCO_TENANT_ID)
					|| getCurrentTenantId().equalsIgnoreCase("awi")) {
				if (!StringUtil.isEmpty(farmer.getFpo())) {
					FarmCatalogue catalogue = getCatlogueValueByCode(farmer.getFpo());
					rows.add(!ObjectUtil.isEmpty(catalogue) ? catalogue.getName() : "");
				}
			}
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.OCP)) {
				SimpleDateFormat df2 = new SimpleDateFormat("MMM dd, yyyy");
				rows.add(!ObjectUtil.isEmpty(farmer) && (!ObjectUtil.isEmpty(farmer.getCreatedDate()))
						? df2.format(farmer.getCreatedDate()) : "");
				rows.add(!ObjectUtil.isEmpty(farmer.getCreatedUsername()) ? farmer.getCreatedUsername() : "" );
			}
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.GRIFFITH_TENANT_ID) ||getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)  ||getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)) {
			SimpleDateFormat df2 = new SimpleDateFormat(DateUtil.DATE_FORMAT_2);
			rows.add(!ObjectUtil.isEmpty(farmer) && (!ObjectUtil.isEmpty(farmer.getCreatedDate()))
					? df2.format(farmer.getCreatedDate()) : "");
			rows.add(!ObjectUtil.isEmpty(farmer.getCreatedUsername()) ? farmer.getCreatedUsername() : "" );
			}
			
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)) {
				List<String> agent = agentService.findAgentNameByWareHouseId(farmer.getSamithi().getId());
				if (!ObjectUtil.isListEmpty(agent)) {
					String json = new Gson().toJson(agent);
					String result = json.replaceAll("\"", "");

					rows.add(result.substring(1, result.length()-1));
					}
				else{
					rows.add("NA");
				}
				rows.add(farmer.getFarms() !=null && !ObjectUtil.isEmpty(farmer.getFarms()) && farmer.getFarms().size()>0 ? String.valueOf(farmer.getFarms().size()) : "NA" );
				rows.add(ObjectUtil.isEmpty(farmer.getTotalCultivatable()) && farmer.getTotalCultivatable() == null ? "NA"
						: df1.format(Double.valueOf(farmer.getTotalCultivatable())));
			}
			rows.add(getText("status" + farmer.getStatus()));
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)) {
				rows.add(farmer.getTotalAcreage());
			}
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.OCP)) {
				rows.addAll(getFarmCropInfo(farmer));
			}
			if (getCurrentTenantId().equalsIgnoreCase("livelihood")) {
				SimpleDateFormat df2 = new SimpleDateFormat("MMM dd, yyyy");
				rows.add(!ObjectUtil.isEmpty(farmer) && (!ObjectUtil.isEmpty(farmer.getDateOfJoining()))
						? df2.format(farmer.getDateOfJoining()) : "");
				rows.add(!ObjectUtil.isEmpty(farmer.getCreatedUsername()) ? farmer.getCreatedUsername() : "" );
				SimpleDateFormat df3 = new SimpleDateFormat("MMM dd, yyyy");
				rows.add(!ObjectUtil.isEmpty(farmer) && (!ObjectUtil.isEmpty(farmer.getLastUpdatedDate()))
						? df3.format(farmer.getLastUpdatedDate()) : "");
				rows.add(!ObjectUtil.isEmpty(farmer.getLastUpdatedUsername()) ? farmer.getLastUpdatedUsername() : "" );
				if(farmer.getStatus()==1){
				rows.add("<button class='faDelete' title='" + getText("farm.printQr") + "' onclick='printQR("
					+ farmer.getId() + ")'> Print ID </button>");
				}
			}
			// rows.add(getText("<font color=\"#0000FF\">delete</font>"));
			jsonObject.put("id", farmer.getId());
			jsonObject.put("cell", rows);
		}
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

		type = request.getParameter("type");

		if (farmer == null) {
			setCommand(CREATE);

			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat sm = new SimpleDateFormat(getGeneralDateFormat());

			dateOfJoining = sm.format(currentDate.getTime());
			/*
			 * if (getCurrentTenantId().equalsIgnoreCase("atma")) {
			 * investigatorDate = sm.format(currentDate.getTime()); }
			 */
			selectedDisability = "0";
			request.setAttribute(HEADING, getText("farmercreate.page"));
			return INPUT;
		} else {
			if (StringUtil.isEmpty(farmer.getLastName())) {
				farmer.setLastName("");
			} else
				farmer.setLastName(farmer.getLastName());

			farmer.setSurName(StringUtil.isEmpty(farmer.getSurName()) ? "" : farmer.getSurName());

			farmer.setCity(farmer.getVillage().getCity());
			if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
				farmer.setCasteName(!StringUtil.isEmpty(farmer.getCasteName()) ? farmer.getCasteName() : "");

			}
			farmer.setTotalHsldLabel(!StringUtil.isEmpty(farmer.getTotalHsldLabel()) ? farmer.getTotalHsldLabel() : "");

			/*
			 * if (!StringUtil.isEmpty(String.valueOf(farmer.getAge()))) {
			 * farmer.setAge(farmer.getAge()); } else { farmer.setAge(0); }
			 */

			if (!StringUtil.isEmpty(dateOfBirth)) {
				farmer.setDateOfBirth(DateUtil.convertStringToDate(dateOfBirth, getGeneralDateFormat()));
			} else {
				farmer.setDateOfBirth(null);
			}
			if (!StringUtil.isEmpty(loanRepaymentDate)) {
				farmer.setLoanRepaymentDate(DateUtil.convertStringToDate(loanRepaymentDate, getGeneralDateFormat()));
			} else {
				farmer.setLoanRepaymentDate(null);
			}

			if (!StringUtil.isEmpty(dateOfBirth)) {
				Calendar currentDate = Calendar.getInstance();
				int currentYear = currentDate.get(Calendar.YEAR);
				String dateSplit = dateOfBirth.replaceAll("[^a-zA-Z0-9]", "");
				String year = dateSplit.substring(4, 8);
				// String dateSplit[] = dateOfBirth.split("-");
				// String year = dateSplit[2];
				farmer.setAge(currentYear - Integer.parseInt(year));

			}

			if (!StringUtil.isEmpty(farmer.getIcsCode())) {
				farmer.setIcsCode(farmer.getIcsCode());
			}
			if(!getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)){
			if (!StringUtil.isEmpty(farmer.getIcsName())) {
				farmer.setIcsName(farmer.getIcsName());
			}
			}else{
				farmer.setIcsName("NA");
			}
			if (!StringUtil.isEmpty(selectedSamithi)) {
				Warehouse samithi = locationService.findSamithiById(Long.valueOf(selectedSamithi));
				farmer.setSamithi(ObjectUtil.isEmpty(samithi) ? null : samithi);
			}

			/*
			 * if (getCurrentTenantId().equalsIgnoreCase("atma")) { if
			 * (!StringUtil.isEmpty(selectedSamithi)) { Warehouse samithi =
			 * locationService.findSamithiByCode(selectedSamithi);
			 * farmer.setSamithi(ObjectUtil.isEmpty(samithi) ? null : samithi);
			 * } farmer.setSangham(selectedSangham);
			 * farmer.setInvestigatorDate(DateUtil.convertStringToDate(
			 * investigatorDate, getGeneralDateFormat()));
			 * farmer.setFarmerSourceIncome(sourceOfIncomeSet()); } else { if
			 * (!StringUtil.isEmpty(selectedSamithi)) { Warehouse samithi =
			 * locationService.findSamithiById(Long.valueOf(selectedSamithi));
			 * farmer.setSamithi(ObjectUtil.isEmpty(samithi) ? null : samithi);
			 * } }
			 */
			if (getCurrentTenantId().equalsIgnoreCase("livelihood")) {
				farmer.setStatus(0);
				String ld=farmer.getCity().getLocality().getName().replaceAll("\\s","").substring(farmer.getCity().getLocality().getName().replaceAll("\\s","").length()-2).toUpperCase().concat(farmer.getCity().getName().replaceAll("\\s","").substring(farmer.getCity().getName().replaceAll("\\s","").length()-2).toUpperCase()).concat(farmer.getVillage().getName().replaceAll("\\s","").substring(farmer.getVillage().getName().replaceAll("\\s","").length()-2).toUpperCase());
				farmer.setFarmerCode(ld.concat(idGenerator.getFarmerCodeSeq()));
			}
			
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.FARM_AGG)) {
				farmer.setFarmerCode(idGenerator.getFarmerCodeSeq());
			} 
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)||getCurrentTenantId().equalsIgnoreCase("livelihood")) {
				farmer.setIsCertifiedFarmer(1);
			}

			String farmerSeqLength = preferncesService.findPrefernceByName(ESESystem.FARMER_ID_LENGTH);
			String farmerMaxRanges = preferncesService.findPrefernceByName(ESESystem.FARMER_MAX_RANGE);
			if (farmerSeqLength == null || farmerSeqLength.isEmpty()) {
				farmer.setFarmerId(idGenerator.getFarmerWebIdSeq());

			} else {
				long farmerMaxRange = Farmer.FARMER_ID_MAX_RANGE;
				if (farmerMaxRanges != null && !farmerMaxRanges.isEmpty()) {

					farmerMaxRange = Long.valueOf(farmerMaxRanges);
				}

				farmer.setFarmerId(idGenerator.getFarmerWebIdSeq(Integer.valueOf(farmerSeqLength), farmerMaxRange));

			}

			

			if (enableHHCode.equalsIgnoreCase("1")) {
				String codeFormat = selectedSamithi;
				BigInteger codeSeq = new BigInteger(idGenerator.getFarmerHHIdSeq(codeFormat));
				String maxCode = codeSeq.subtract(new BigInteger("1")).toString();
				if (Integer.valueOf(maxCode.substring(8, 10)) >= 99) {
					addActionError(getText("error.farmerExceed"));
					return INPUT;
				}
				farmer.setFarmerCode(idGenerator.getFarmerHHIdSeq(codeFormat));
			}

			if (getCurrentTenantId().equalsIgnoreCase("awi")) {
				/*
				 * if (!ObjectUtil.isEmpty(farmer.getVillage())) { String
				 * codeGen = idGenerator.getFarmerWebCodeIdSeq(
				 * farmer.getVillage().getCity().getCode().substring(0, 1),
				 * farmer.getVillage().getGramPanchayat().getCode().substring(0,
				 * 1)); farmer.setFarmerCode(codeGen); }
				 */

				farmer.setFarmerCode(farmer.getFarmerId());
			}
			int seq=0;
			if (getCurrentTenantId().equalsIgnoreCase("symrise")) {
				farmer.setCertificationType(2);
				farmer.setCertificateStandardLevel(-2);
				seq=Integer.valueOf(farmer.getVillage().getSeq());
				farmer.setFarmerCode(farmer.getVillage().getCode()+"_"+seq);
			}
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.AVT)) {
				seq=Integer.valueOf(farmer.getVillage().getSeq());
				farmer.setFarmerCode("F"+farmer.getVillage().getCode()+seq);
			}
			farmer.setPositionGroup(selectedGroupPosition);
			farmer.setDateOfJoining(DateUtil.convertStringToDate(dateOfJoining, getGeneralDateFormat()));
			farmer.setEnrollDate(DateUtil.convertStringToDate(dateOfJoining, getGeneralDateFormat()));
			Farmer existingFarmer = farmerService.findFarmerByFarmerId(farmer.getFarmerId());
			if (!ObjectUtil.isEmpty(existingFarmer)) {
				addFieldError("farmerId", getText("unique.farmerId"));
				return INPUT;
			}

			if (!ObjectUtil.isEmpty(farmer.getCertificateStandard()) && farmer.getCertificateStandard().getId() > 0)
				farmer.setCertificateStandard(
						certificationService.findCertificateStandardById(farmer.getCertificateStandard().getId()));
			else
				farmer.setCertificateStandard(null);

			if (!ObjectUtil.isEmpty(farmer.getCustomerProject()) && farmer.getCustomerProject().getId() > 0)
				farmer.setCustomerProject(clientService.findCustomerProjectById(farmer.getCustomerProject().getId()));
			else
				farmer.setCustomerProject(null);

			if (!(Farmer.EnrollmentPlaceMaster.OTHER.ordinal() == farmer.getEnrollmentPlace())) {
				farmer.setEnrollmentPlaceOther(farmer.getEnrollmentPlaceOther());
			}

			farmer.setBranchId(getBranchId());

			if (!StringUtil.isEmpty(farmer.getAgriculture()) && StringUtil.isEmpty(farmer.getOtherSource())) {
				// int agri = Integer.valueOf((farmer.getAgriculture()));
				farmer.setTotal(farmer.getAgriculture());
			} else if (StringUtil.isEmpty(farmer.getAgriculture()) && !StringUtil.isEmpty(farmer.getOtherSource())) {
				// Integer oth = Integer.parseInt(farmer.getOtherSource());
				farmer.setTotal(farmer.getOtherSource());
			} else if (!StringUtil.isEmpty(farmer.getAgriculture()) || !StringUtil.isEmpty(farmer.getOtherSource())) {
				Double agri = Double.parseDouble(farmer.getAgriculture());
				Double oth = Double.parseDouble(farmer.getOtherSource());
				BigDecimal total = new BigDecimal(agri + oth);
				farmer.setTotal(String.valueOf(total));
			}

			String jsonString = farmer.getJsonString();
			List<BankInformation> bankInformationList = new ArrayList<BankInformation>();

			if (!StringUtil.isEmpty(jsonString)) {
				GsonBuilder gsonBuilder = new GsonBuilder();
				// gsonBuilder.setDateFormat("M/d/yy hh:mm a"); //Format Dates
				Gson gson = gsonBuilder.create();
				bankInformationList = Arrays.asList(gson.fromJson(jsonString, BankInformation[].class));
			}

			Set<BankInformation> bankInformationSet = new HashSet<BankInformation>();
			for (BankInformation bankInformation : bankInformationList) {
				if (!StringUtil.isEmpty(bankInformation.getAccNo())) {
					bankInformation.setAccType(bankInformation.getAccTypeCode());
					bankInformation.setAccName(!StringUtil.isEmpty(bankInformation.getAccName()) ? bankInformation.getAccName():null);
					bankInformation.setAccNo(bankInformation.getAccNo());
					bankInformation.setBankName(bankInformation.getBankName());
					bankInformation.setBranchName(bankInformation.getBranchName());
					bankInformation.setSortCode(bankInformation.getSortCode());
					bankInformation.setSwiftCode(bankInformation.getSwiftCode());

					farmerService.addBankInformation(bankInformation);
					bankInformationSet.add(bankInformation);
				}
			}
			farmer.setBankInfo(bankInformationSet);
			if (!StringUtil.isEmpty(getAccBalance())) {
				Double accBalance = StringUtil.isDouble(getAccBalance().replace(",", ""))
						? Double.valueOf(getAccBalance().replace(",", "")) : 0D;
				farmer.setAccountBalance(accBalance);
			} else
				farmer.setAccountBalance(0.00);

			if (farmer.getIsCertifiedFarmer() == 1) {
				if (!StringUtil.isEmpty(farmer.getFarmerEconomy().getHousingOwnership())
						|| !StringUtil.isEmpty(farmer.getFarmerEconomy().getHousingType())
						|| !StringUtil.isEmpty(farmer.getFarmerEconomy().getOtherHousingType())) {
					farmer.getFarmerEconomy().setFarmer(farmer);
				}
			} else {
				farmer.setFarmerEconomy(null);
				/*
				 * if (getCurrentTenantId().equalsIgnoreCase(ESESystem.
				 * PGSS_TENANT_ID)) { farmer.setIsCertifiedFarmer(1);
				 * 
				 * }
				 */
			}

			farmer.setSeasonCode(clientService.findCurrentSeasonCode());
			farmer.setIsVerified(0);
			farmer.setCreatedUsername(getUsername());
			farmer.setCreatedDate(new Date());
			if(!getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)){
			farmer.setLastUpdatedUsername(getUsername());
			farmer.setLastUpdatedDate(new Date());
			}
			farmer.setLoanRepaymentAmount(farmer.getLoanRepaymentAmount());
			farmer.setIdProof(farmer.getIdProof());
			farmer.setProofNo(farmer.getProofNo());
			farmer.setOtherIdProof(farmer.getOtherIdProof());
			farmer.setGrsMember(farmer.getGrsMember());
			farmer.setPaidShareCapitial(farmer.getPaidShareCapitial());
			farmer.setYearOfICS(farmer.getYearOfICS());
			farmer.setIsDisable(selectedDisabled);
			/*
			 * Farm farm = new Farm(); FarmDetailedInfo detailedInfo = new
			 * FarmDetailedInfo(); Set<Farm> farmSet = new HashSet<>();
			 * farm.setFarmCode(farmer.getFarmerId());
			 * farm.setFarmName(farmer.getFirstName());
			 * farm.setFarmDetailedInfo(detailedInfo); farmSet.add(farm);
			 * farmer.setFarms(farmSet);
			 */

			if (!StringUtil.isEmpty(selfAssesmentJSON)) {
				try {
					Type listType1 = new TypeToken<List<FarmerSelfAsses>>() {
					}.getType();
					List<FarmerSelfAsses> filtersList = new Gson().fromJson(selfAssesmentJSON, listType1);
					Set<FarmerSelfAsses> assesSet = new LinkedHashSet<>();
					assesSet.addAll(filtersList);
					farmer.setFarmerSelfAsses(assesSet);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (!StringUtil.isEmpty(healthAssesmentJSON)) {
				try {
					Type listType1 = new TypeToken<List<FarmerHealthAsses>>() {
					}.getType();

					List<FarmerHealthAsses> filtersList = new Gson().fromJson(healthAssesmentJSON, listType1);
					Set<FarmerHealthAsses> farmerHealthAsses = new LinkedHashSet<>();
					farmerHealthAsses.addAll(filtersList);
					farmer.setFarmerHealthAsses(farmerHealthAsses);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (this.getIdProofImg() != null) {
				farmer.setIdProofImg(FileUtil.getBinaryFileContent(this.getIdProofImg()));
			}

			/*
			 * if
			 * (getCurrentTenantId().equalsIgnoreCase(ESESystem.PGSS_TENANT_ID))
			 * { if (!StringUtil.isEmpty(getType()) &&
			 * getType().equals(Farmer.IRP)) { farmer.setTypez(Farmer.IRP); }
			 * else { farmer.setTypez(Farmer.FARMER); } }
			 */
			if (getCurrentTenantId().equalsIgnoreCase("wilmar")) {
				farmer.setMasterData(farmer.getMasterData());
			}
			if(getCurrentTenantId().equalsIgnoreCase(ESESystem.GRIFFITH_TENANT_ID)){
				String farmerBranch="";
							
				   BranchMaster branchMaster = StringUtil.isEmpty(farmer.getBranchId()) ? null
		                    : clientService.findBranchMasterByBranchId(farmer.getBranchId());
				   if(!ObjectUtil.isEmpty(branchMaster)){
					   farmerBranch=branchMaster.getContactPerson();
					   }
				
				String vilSeqCode=farmerBranch+"/"+farmer.getVillage().getCity().getLocality().getState().getCountry().getName().substring(0,2).toUpperCase()
						+"/"+farmer.getVillage().getCity().getLocality().getState().getName().substring(0,2).toUpperCase()
						+"/"+farmer.getVillage().getName().substring(0,3).toUpperCase()
						+"/"+farmer.getVillage().getSeq()				
						;
				
				farmer.setFarmerCode(vilSeqCode);
				seq=Integer.parseInt(farmer.getVillage().getSeq());
			}
			if (getCurrentTenantId().equalsIgnoreCase("welspun")) {

				/*
				 * String farmerCode=""; String
				 * districtName=farmer.getVillage().getCity().getLocality().
				 * getName(); districtName = districtName.substring(0, 3);
				 * String cityName=farmer.getVillage().getCity().getName();
				 * cityName = cityName.substring(0, 3); String
				 * doj=DateUtil.getYearByDateTime(farmer.getDateOfJoining());
				 * String villageName=farmer.getVillage().getName(); villageName
				 * = villageName.substring(0, 3); String samithiName=
				 * farmer.getSamithi().getCode(); samithiName =
				 * samithiName.substring(1); String farmerId=
				 * farmer.getFarmerId();
				 * farmerCode=districtName+""+doj+""+cityName+""+villageName+""+
				 * samithiName+""+farmerId; farmer.setFarmerCode(farmerCode);
				 */
				String farmerCode = "A000";
				farmer.setFarmerCode(farmerCode + farmer.getFarmerId());

			}
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.STICKY_TENANT_ID)) {
				String samithiName = farmer.getSamithi().getCode();
				samithiName = samithiName.substring(1);
				farmer.setFarmerCode(getBranchId() + samithiName + farmer.getFarmerId());
			}
			farmerService.addContractForFarmer(farmer, request.getSession().getAttribute("user").toString());
			saveDynamicField("308", String.valueOf(farmer.getId()), farmer.getSeasonCode(), "1");
			/*
			 * if (!StringUtil.isEmpty(dynamicFieldsArray) &&
			 * dynamicFieldsArray.length() > 0) { DynamicFeildMenuConfig dm =
			 * farmerService.findDynamicMenusByMType("308").get(0);
			 * fieldConfigMap = new HashMap<>();
			 * dm.getDynamicFieldConfigs().stream().forEach(section -> {
			 * fieldConfigMap.put(section.getField().getCode(),
			 * section.getField()); }); FarmerDynamicData farmerDynamicData =
			 * new FarmerDynamicData();
			 * farmerDynamicData.setFarmerDynamicFieldsValues(
			 * addFarmerDynamicFieldsSet(farmer)); //
			 * farmerDynamicData.setTxnUniqueId(Long.valueOf(head.getMsgNo()));
			 * farmerDynamicData.setReferenceId(String.valueOf(farmer.getId()));
			 * farmerDynamicData.setTxnType("308");
			 * farmerDynamicData.setDate(new Date(0));
			 * farmerDynamicData.setCreatedDate(new Date(0));
			 * farmerDynamicData.setCreatedUser(getUsername());
			 * farmerDynamicData.setStatus("0");
			 * farmerDynamicData.setBranch(farmer.getBranchId());
			 * farmerDynamicData.setEntityId("1");
			 * farmerDynamicData.setSeason(farmer.getSeasonCode());
			 * farmerService.saveOrUpdate(farmerDynamicData);
			 * farmerService.deleteChildObjects(farmerDynamicData.getTxnType());
			 * 
			 * }
			 */
			

			/** SAVING CARD INFORMATION **/
			agentService.createESECard(farmer.getFarmerId(),
					idGenerator.createFarmerCardIdSequence(IUniqueIDGenerator.WEB_REQUEST,
							IUniqueIDGenerator.ESE_AGENT_INTERNAL_ID),
					new Date(), ESECard.FARMER_CARD);
			if (this.getFarmerImage() != null && farmer.getId() > 0) {
				ImageInfo imageInfo = new ImageInfo();
				Image photo = new Image();
				photo.setImage(FileUtil.getBinaryFileContent(this.getFarmerImage()));
				photo.setImageId(farmer.getId() + "-FP");
				imageInfo.setPhoto(photo);

				farmerService.addImageInfo(imageInfo);
				farmerService.updateFarmerImageInfo(farmer.getId(), imageInfo.getId());
			}

			
if(getCurrentTenantId().equalsIgnoreCase("symrise") || getCurrentTenantId().equalsIgnoreCase("griffith") || getCurrentTenantId().equalsIgnoreCase(ESESystem.AVT) ){
	seq=seq+1;
	farmer.getVillage().setSeq(String.valueOf(seq));
	farmerService.update(farmer.getVillage());
}
			return REDIRECT;
		}
	}

	private List<DynamicSectionConfig> dynamicFields() {
		// TODO Auto-generated method stub
		List<DynamicSectionConfig> dynamicFieldSectionConfigs = new ArrayList<>();

		try {
			List<DynamicSectionConfig> list = farmerService.listDynamicSections();
			if (list.size() > 0) {
				if (!StringUtil.isEmpty(getType()) && getType().equalsIgnoreCase(Farmer.IRP)) {
					list = list.stream().filter(sectionConfig -> sectionConfig.getType().contains(Farmer.IRP))
							.collect(Collectors.toList());
				} else {
					list = list.stream().filter(sectionConfig -> sectionConfig.getType().contains(Farmer.FARMER))
							.collect(Collectors.toList());
				}
				for (DynamicSectionConfig dynamicSectionConfig : list) {
					Set<DynamicFieldConfig> dynamicFieldsSet = new LinkedHashSet();
					for (DynamicFieldConfig config : dynamicSectionConfig.getDynamicFieldConfigs()) {

						if (!StringUtil.isEmpty(config.getComponentId())) {
							config.setComponentId(config.getComponentId());
						}
						dynamicFieldsSet.add(config);
						dynamicSectionConfig.setDynamicFieldConfigs(dynamicFieldsSet);

					}
					dynamicFieldSectionConfigs.add(dynamicSectionConfig);
				}

			}
			return dynamicFieldSectionConfigs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dynamicFieldSectionConfigs;

	}

	/*
	 * public boolean createDynamicProperties(String properties, String value) {
	 * boolean result=false; OutputStream out = null; try {
	 * 
	 * Properties props = new Properties(); String propertiesFilePath=
	 * "../src/main/resources/com/sourcetrace/esesw/view/SwitchAction_en.properties";
	 * File f = new File(propertiesFilePath); if(f.exists()){
	 * 
	 * props.load(new FileReader(f)); //Change your values here
	 * props.setProperty(properties, value); System.out.println("props"+props);
	 * } else{ System.out.println("ese"); //Set default values?
	 * props.setProperty("ServerAddress", "DullDefault");
	 * props.setProperty("ServerPort", "8080"); props.setProperty("ThreadCount",
	 * "456");
	 * 
	 * f.createNewFile(); }
	 * 
	 * 
	 * 
	 * out = new FileOutputStream( f ); props.store(out,
	 * "This is an optional header comment string");
	 * 
	 * result=true;
	 * 
	 * }
	 * 
	 * catch (Exception e ) { e.printStackTrace(); } finally{
	 * 
	 * if(out != null){
	 * 
	 * try {
	 * 
	 * out.close(); } catch (IOException ex) {
	 * 
	 * System.out.
	 * println("IOException: Could not close SwitchAction_en.propeties output stream; "
	 * + ex.getMessage()); ex.printStackTrace(); } } } return result; }
	 */

	/**
	 * Detail.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String detail() throws Exception {

		type = request.getParameter("type");

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
			farmer = farmerService.findFarmerById(Long.valueOf(id));
			/*
			 * if
			 * (getCurrentTenantId().equalsIgnoreCase(ESESystem.PGSS_TENANT_ID)
			 * && !StringUtil.isEmpty(getType()) &&
			 * getType().equals(Farmer.IRP)) { farmer =
			 * farmerService.findFarmerById(Long.valueOf(id), getBranchId()); }
			 */
			card = cardService.findCardByProfileId(farmer.getFarmerId());
			interestCalcConsolidated = farmerService
					.findInterestCalcConsolidatedByfarmerProfileId(farmer.getFarmerId());

			ESESystem preferences = preferncesService.findPrefernceById("1");
			if (!StringUtil.isEmpty(preferences)) {
				if (preferences.getPreferences().get(ESESystem.ENABLE_CROP_INFO) != null) {
					setCropInfoEnabled(preferences.getPreferences().get(ESESystem.ENABLE_CROP_INFO));
				}

			}
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

			/*
			 * account =
			 * accountService.findAccountByProfileIdAndProfileType(farmer.
			 * getFarmerId(), ESEAccount.FARMER_ACCOUNT);
			 */
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
			/*
			 * if (farmer.getDateOfJoining() != null) { dateOfJoining =
			 * df.format(farmer.getDateOfJoining()); }
			 */
			if (farmer.getIcsCode() != null) {
				icsCode = farmer.getIcsCode();
			}
			if (farmer.getIcsName() != null) {

				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)
						|| getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)
						|| getCurrentTenantId().equalsIgnoreCase("iccoa")
						|| getCurrentTenantId().equalsIgnoreCase(ESESystem.SUSAGRI) 
						|| getCurrentTenantId().equalsIgnoreCase(ESESystem.AVT) ) {
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

			/*
			 * if (farmer.getImageInfo() != null &&
			 * farmer.getImageInfo().getPhoto() != null &&
			 * farmer.getImageInfo().getPhoto().getImage() != null &&
			 * farmer.getImageInfo().getPhoto().getImage().length>0) {
			 * setFarmerImageByteString(Base64Util.encoder(farmer.getImageInfo()
			 * .getPhoto().getImage())); }
			 */
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
			/*
			 * if
			 * (getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID
			 * )) { Shg shg =
			 * locationService.findShgById(Long.valueOf(farmer.getShg()));
			 * shgName = shg.getShgName(); }
			 */
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

			// Map<Integer, String> ceo =
			// getPropertyData(getText("consumerElec"));
			/*
			 * if (!StringUtil.isEmpty(farmer.getConsumerElectronics()) &&
			 * !farmer.getConsumerElectronics().equalsIgnoreCase("99")) { String
			 * key = farmer.getConsumerElectronics();
			 * farmer.setConsumerElectronics(ceo.get(Integer.valueOf(key))); }
			 * else {
			 * farmer.setConsumerElectronics(farmer.getConsumerElectronicsOther(
			 * )); }
			 */

			/*
			 * if (!StringUtil.isEmpty(farmer.getConsumerElectronics())) {
			 * String key = farmer.getConsumerElectronics(); String
			 * consumerElecArr[] = farmer.getConsumerElectronics().split("\\,");
			 * for (int i = 0; i < consumerElecArr.length; i++) { String
			 * consumerElecTrim = consumerElecArr[i].replaceAll("\\s+", ""); if
			 * (!StringUtil.isEmpty(consumerElecTrim)) { if
			 * (consumerElecTrim.equalsIgnoreCase("99")) { consumerElec +=
			 * farmer.getConsumerElectronicsOther();
			 * 
			 * } else {
			 * 
			 * consumerElec += ceo.get(Integer.valueOf(consumerElecTrim)); } }
			 * else { consumerElec = ceo.get(Integer.valueOf(consumerElecTrim));
			 * }
			 * 
			 * } }
			 */
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

			/*
			 * if (farmer.getLifeInsurance() != null &&
			 * !StringUtil.isEmpty(farmer.getLifeInsurance()) &&
			 * !farmer.getLifeInsurance().equals("0")) { String val[] =
			 * farmer.getLifeInsurance().split("-");
			 * farmer.setLifeInsure(getText("insure1"));
			 * farmer.setLifeInsurance(val[1]); } else {
			 * farmer.setLifeInsure(getText("insure0"));
			 * farmer.setLifeInsurance(""); } if
			 * (!StringUtil.isEmpty(farmer.getHealthInsurance()) &&
			 * farmer.getHealthInsurance() != null &&
			 * !farmer.getHealthInsurance().equals("0")) { String val[] =
			 * farmer.getHealthInsurance().split("-");
			 * farmer.setHealthInsure(getText("insure1"));
			 * farmer.setHealthInsurance(val[1]); } else {
			 * farmer.setHealthInsure(getText("insure0"));
			 * farmer.setHealthInsurance(""); }
			 */
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

			/*
			 * if (farmer.getCertificateStandardLevel()!=-1) {
			 * farmer.setCertificationStandardLevel(certificationLevels.get(
			 * farmer.getCertificateStandardLevel())); }
			 */

			farmer.setLoanRepaymentAmount(farmer.getLoanRepaymentAmount());
			farmer.setIdProof(farmer.getIdProof());
			farmer.setProofNo(farmer.getProofNo());
			farmer.setOtherIdProof(farmer.getOtherIdProof());
			farmer.setReligion(farmer.getReligion());
			FarmCatalogue catalogue = getCatlogueValueByCode(farmer.getPositionGroup());
			if (!ObjectUtil.isEmpty(catalogue))
				farmer.setPositionGroup(catalogue.getName());
			/*
			 * if (getCurrentTenantId().equalsIgnoreCase("atma")) {
			 * 
			 * Map<String, String> sanghamMap = getSanghamList(); String
			 * sanghamVal = farmer.getSangham(); //
			 * farmer.setSangham(sanghamMap.get(sanghamVal)); sanghamName =
			 * sanghamMap.get(sanghamVal);
			 * 
			 * if (!StringUtil.isEmpty(farmer.getFamilyMember())) {
			 * 
			 * String familyMemberArr[] = farmer.getFamilyMember().split("\\,");
			 * for (int i = 0; i < familyMemberArr.length; i++) { String
			 * familyMemberTrim = familyMemberArr[i].replaceAll("\\s+", ""); if
			 * (!StringUtil.isEmpty(familyMember)) { if
			 * (familyMemberTrim.equalsIgnoreCase("99")) { familyMember +=
			 * farmer.getFamilyMemberOther() + ",";
			 * 
			 * } else {
			 * 
			 * String otherName =
			 * getCatlogueValueByCode(familyMemberTrim)!=null?
			 * getCatlogueValueByCode(familyMemberTrim).getName():"";
			 * familyMember += otherName + ","; } } else { if
			 * (familyMemberTrim.equalsIgnoreCase("99")) { familyMember =
			 * farmer.getFamilyMemberOther() + ","; } else { String otherName =
			 * getCatlogueValueByCode(familyMemberTrim)!=null?
			 * getCatlogueValueByCode(familyMemberTrim).getName():"";
			 * familyMember = otherName + ","; } }
			 * 
			 * } }
			 * 
			 * if (familyMember != null) { familyMember =
			 * familyMember.substring(0, familyMember.length() - 1);
			 * familyMember = StringUtil.removeLastComma(familyMember);
			 * farmer.setFamilyMember(familyMember); }
			 * 
			 * if (farmer.getInvestigatorDate() != null) { investigatorDate =
			 * genDate.format(farmer.getInvestigatorDate()); }
			 * 
			 * if (!ObjectUtil.isEmpty(farmer.getInvestigatorName())) {
			 * List<Agent> agentLists =
			 * agentService.listAgentByAgentType(AgentType.FIELD_STAFF); if
			 * (!ObjectUtil.isListEmpty(agentLists)) { for (Agent agent :
			 * agentLists) { if
			 * ((agent.getProfileId().equals(farmer.getInvestigatorName()))) {
			 * setInvestigatorName(agent.getProfileIdWithName()); }
			 * 
			 * } }
			 * 
			 * }
			 * 
			 * List<FarmerIncomeDetails> tempFarmerIncomeList = new
			 * ArrayList<FarmerIncomeDetails>();
			 * 
			 * farmerSourceIncomeList =
			 * farmerService.listFarmSourceIncomeByFarmerId(String.valueOf(
			 * farmer.getId())); for (FarmerSourceIncome farmerSourceIncome :
			 * farmerSourceIncomeList) { tempFarmerIncomeList = farmerService
			 * .listFarmerIncomeDetailsBySourceIncomeId(String.valueOf(
			 * farmerSourceIncome.getId())); List<FarmerIncomeDetails>
			 * incomeDetailsList = new LinkedList<FarmerIncomeDetails>(); for
			 * (FarmerIncomeDetails farmerIncomeDetails : tempFarmerIncomeList)
			 * { if (farmerSourceIncome.getName().equalsIgnoreCase(Farmer.
			 * ALLIED_SECTOR) &&
			 * !StringUtil.isEmpty(farmerIncomeDetails.getSourceName())) { if
			 * (farmerIncomeDetails.getSourceName().equalsIgnoreCase("99")) {
			 * farmerIncomeDetails.setSourceName(farmerIncomeDetails.
			 * getSourceNameOther()); } else { farmerIncomeDetails
			 * .setSourceName(getText("allied" +
			 * farmerIncomeDetails.getSourceName())); } } else if
			 * (farmerSourceIncome.getName().equalsIgnoreCase(Farmer.EMPLOYMENT)
			 * && !StringUtil.isEmpty(farmerIncomeDetails.getSourceName())) { if
			 * (farmerIncomeDetails.getSourceName().equalsIgnoreCase("99")) {
			 * farmerIncomeDetails.setSourceName(farmerIncomeDetails.
			 * getSourceNameOther()); } else { farmerIncomeDetails
			 * .setSourceName(getText("employment" +
			 * farmerIncomeDetails.getSourceName())); }
			 * 
			 * } else if
			 * (farmerSourceIncome.getName().equalsIgnoreCase(Farmer.OTHERS) &&
			 * !StringUtil.isEmpty(farmerIncomeDetails.getSourceName())) { if
			 * (farmerIncomeDetails.getSourceName().equalsIgnoreCase("99")) {
			 * farmerIncomeDetails.setSourceName(farmerIncomeDetails.
			 * getSourceNameOther()); } else { farmerIncomeDetails
			 * .setSourceName(getText("otherIncome" +
			 * farmerIncomeDetails.getSourceName())); }
			 * 
			 * } incomeDetailsList.add(farmerIncomeDetails); }
			 * farmerSourceIncome.setIncomeDetailsList(incomeDetailsList); }
			 * 
			 * }
			 */

			if (farmer.getInvestigatorDate() != null) {
				investigatorDate = genDate.format(farmer.getInvestigatorDate());
			}

			farmer.setPlaceOfAsss(!StringUtil.isEmpty(farmer.getPlaceOfAsss())
					? getCatlogueValueByCode(farmer.getPlaceOfAsss()).getName() : "");

			farmer.setPrefWrk(!StringUtil.isEmpty(farmer.getPrefWrk())
					? getCatlogueValueByCode(farmer.getPrefWrk()).getName() : "");

			/*
			 * Map<Integer, String> vMap = getPropertyData(getText("vehicle"));
			 * if (!ObjectUtil.isEmpty(farmer.getVehicle()) &&
			 * !farmer.getVehicle().equalsIgnoreCase("99")) { String key =
			 * farmer.getVehicle();
			 * farmer.setVehicle(vMap.get(Integer.valueOf(key))); } else {
			 * farmer.setVehicle(farmer.getVehicleOther()); }
			 */
			/*
			 * if (!StringUtil.isEmpty(farmer.getBankInfo())) { for
			 * (BankInformation bank : farmer.getBankInfo()) { FarmCatalogue
			 * catalogue = catalogueService
			 * .findCatalogueByCode(bank.getAccType());
			 * bank.setAccType(catalogue != null ? catalogue.getName() : "");
			 * 
			 * if(!StringUtil.isEmpty(catalogue)) { String accVal = catalogue!=
			 * null ? catalogue.getName() : ""; if (!first) { accTypeString =
			 * accTypeString + "," + accVal; } else { accTypeString = accVal;
			 * first = false; } }
			 * 
			 * 
			 * }
			 * 
			 * }
			 */

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
			
		//	int i = 0;
			// List<Farm> farm = new ArrayList<Farm>();
			farms = new LinkedList<Farm>();
			List<FarmCrops> farmCropList = new ArrayList<FarmCrops>();
			for (Farm farm : farmer.getFarms()) {
				farm.farmImageFileName = farm.getPhoto()!=null?Base64Util.encoder(farm.getPhoto()):null;
				if (farm.getActiveCoordinates()!= null && farm.getActiveCoordinates().getFarmCoordinates() !=null && !ObjectUtil.isEmpty(farm.getActiveCoordinates()) &&  !ObjectUtil.isListEmpty(farm.getActiveCoordinates().getFarmCoordinates())) {
					farm.farmJsonObjectList = getFarmJSONObjects(farm.getActiveCoordinates().getFarmCoordinates());
				} else {
					farm.farmJsonObjectList = new ArrayList();
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

					farm.getFarmDetailedInfo().setInputSource(inputSource);
				}
				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)) {
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
				treeDetailss=new ArrayList();
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
						treeDetailss.add(detail);
					}
				}
				}
				if (!ObjectUtil.isEmpty(preferences)) {
					DateFormat Date = new SimpleDateFormat(
							preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
					if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)) {
						SimpleDateFormat sf=new SimpleDateFormat("MM-dd-yyyy");
					farm.getFarmDetailedInfo().setLastDateOfChemicalString(StringUtil.isEmpty(farm.getFarmDetailedInfo().getLastDateOfChemical()) ? " "
							: sf.format(farm.getFarmDetailedInfo().getLastDateOfChemical()));
					}else
						farm.getFarmDetailedInfo().setLastDateOfChemicalString(StringUtil.isEmpty(farm.getFarmDetailedInfo().getLastDateOfChemical()) ? " "
								: Date.format(farm.getFarmDetailedInfo().getLastDateOfChemical()));
				}
				/*if (farm.getFarmICSConversion() != null && farm.getFarmICSConversion().size() > 0) {
					for (FarmIcsConversion icd : farm.getFarmICSConversion()) {
				scopeName = !StringUtil.isEmpty(icd.getScope())
						? catalogueService.findCatalogueByCode(icd.getScope()).getName() : "";
					}
					}*/
				
				if (farm.getFarmICSConversion() != null && farm.getFarmICSConversion().size() > 0) {
					for (FarmIcsConversion icd : farm.getFarmICSConversion()) {
						if (!ObjectUtil.isEmpty(preferences)) {

							DateFormat Date = new SimpleDateFormat(
									preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
							if(getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)){
								SimpleDateFormat sf=new SimpleDateFormat("MM-dd-yyyy");
								icd.setInspectionDateString(StringUtil.isEmpty(icd.getInspectionDate()) ? " "
										: sf.format(icd.getInspectionDate()));	
							}else
							icd.setInspectionDateString(StringUtil.isEmpty(icd.getInspectionDate()) ? " "
									: genDate.format(icd.getInspectionDate()));
						}

						/*
						 * farm.setOrganicStatus(!StringUtil.isEmpty(icd.
						 * getOrganicStatus()) ? icd.getOrganicStatus() : "4");
						 */

						icd.setScope(!StringUtil.isEmpty(icd.getScope())
								? catalogueService.findCatalogueByCode(icd.getScope()).getName() : "");

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
			
				farmCropList = farm.getFarmCrops().stream().collect(Collectors.toList());
				farms.add(farm);
				farmCrop.addAll(farmCropList);
			//	i++;
			}

			setCurrentPage(getCurrentPage());
			command = UPDATE;
			view = DETAIL;
			request.setAttribute(HEADING, getText("farmerdetail"));
		} else {
			request.setAttribute(HEADING, getText(LIST));
			return LIST;
		}
		return view;
	}

	public String details() throws Exception {

		type = request.getParameter("type");

		String view = "";
		String vehicle = "";
		String consumerElec = "";
		String cropInsurance = null;
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

			// farmer = farmerService.findFarmerByFarmerId(id);
			/*
			 * if
			 * (getCurrentTenantId().equalsIgnoreCase(ESESystem.PGSS_TENANT_ID)
			 * && !StringUtil.isEmpty(getType()) &&
			 * getType().equals(Farmer.IRP)) { farmer =
			 * farmerService.findFarmerById(Long.valueOf(id), getBranchId()); }
			 */
			farmer = farmerService.findFarmerByFarmerId(id);

			card = cardService.findCardByProfileId(farmer.getFarmerId());
			interestCalcConsolidated = farmerService
					.findInterestCalcConsolidatedByfarmerProfileId(farmer.getFarmerId());

			ESESystem preferences = preferncesService.findPrefernceById("1");

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

			/*
			 * account =
			 * accountService.findAccountByProfileIdAndProfileType(farmer.
			 * getFarmerId(), ESEAccount.FARMER_ACCOUNT);
			 */
			if (farmer == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			if (!ObjectUtil.isEmpty(preferences)) {

				if (!ObjectUtil.isEmpty(farmer.getDateOfBirth()) && farmer.getDateOfBirth() != null) {
					dateOfBirth = genDate.format(farmer.getDateOfBirth());
					// dateOfBirth = df.format(farmer.getDateOfBirth());
				}
				if (!ObjectUtil.isEmpty(farmer.getLoanRepaymentDate()) && farmer.getLoanRepaymentDate() != null) {
					loanRepaymentDate = genDate.format(farmer.getLoanRepaymentDate());
					// dateOfBirth = df.format(farmer.getDateOfBirth());
				}
				if (!ObjectUtil.isEmpty(farmer.getDateOfJoining()) && farmer.getDateOfJoining() != null) {
					if (!StringUtil.isEmpty(preferences)) {

						dateOfJoining = genDate.format(farmer.getDateOfJoining());
					}
				} else {
					if (farmer.getDateOfJoining() != null) {
						dateOfJoining = df.format(farmer.getDateOfJoining());
						;
					}
				}
			}
			/*
			 * if (farmer.getDateOfJoining() != null) { dateOfJoining =
			 * df.format(farmer.getDateOfJoining()); }
			 */
			if (farmer.getIcsCode() != null) {
				icsCode = farmer.getIcsCode();
			}
			if (farmer.getIcsName() != null) {
				icsName = farmer.getIcsName();
			}

			ESEAccount acc = accountService.findAccountByProfileIdAndProfileType(farmer.getFarmerId(),
					ESEAccount.CONTRACT_ACCOUNT);
			if (!ObjectUtil.isEmpty(acc) || acc != null) {
				setAccBalance(String.valueOf(acc.getCashBalance()));
			}

			if (farmer.getImageInfo() != null && farmer.getImageInfo().getPhoto() != null
					&& farmer.getImageInfo().getPhoto().getImage() != null
					&& farmer.getImageInfo().getPhoto().getImage().length > 0) {
				setFarmerImageByteString(Base64Util.encoder(farmer.getImageInfo().getPhoto().getImage()));
			}

			if (farmer.getIdProofImg() != null && farmer.getIdProofImg().length > 0) {
				setIdProofImgString(Base64Util.encoder(farmer.getIdProofImg()));
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

								drinkingWater += getText("drinkingWS" + drinkingTrim) + ",";
							}
						} else {
							drinkingWater = getText("drinkingWS" + drinkingTrim) + ",";
						}

					}
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
					if (!StringUtil.isEmpty(cropInsurance)) {
						// cropIns +=
						// catalogueService.findCatalogueValueByCode(cropInsTrim)
						// + ",";
						ProcurementProduct crop = productDistributionService.findProcurementProductByCode(cropInsTrim);
						if (!ObjectUtil.isEmpty(crop)) {
							cropInsurance += crop.getName() + ",";

						}

					} else {
						ProcurementProduct crop = productDistributionService.findProcurementProductByCode(cropInsTrim);
						if (!ObjectUtil.isEmpty(crop)) {
							cropInsurance = crop.getName() + ",";
						}

					}

				}

			}
			if (cropInsurance != null && !StringUtil.isEmpty(cropInsurance)) {
				cropInsurance = cropInsurance.substring(0, cropInsurance.length() - 1);
				cropInsurance = StringUtil.removeLastComma(cropInsurance);
				farmer.setFarmerCropInsurance(cropInsurance);
			}

			Map<Integer, String> ceo = getPropertyData(getText("consumerElec"));
			/*
			 * if (!StringUtil.isEmpty(farmer.getConsumerElectronics()) &&
			 * !farmer.getConsumerElectronics().equalsIgnoreCase("99")) { String
			 * key = farmer.getConsumerElectronics();
			 * farmer.setConsumerElectronics(ceo.get(Integer.valueOf(key))); }
			 * else {
			 * farmer.setConsumerElectronics(farmer.getConsumerElectronicsOther(
			 * )); }
			 */

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

			/*
			 * if (!StringUtil.isEmpty(farmer.getConsumerElectronics())) {
			 * String electronicsArr[] =
			 * farmer.getConsumerElectronics().split(","); for (int i = 0; i <
			 * electronicsArr.length; i++) { String electronicsTrim =
			 * electronicsArr[i].replaceAll("\\s+", ""); if
			 * (!StringUtil.isEmpty(electronicsTrim)) { if
			 * (electronicsTrim.equalsIgnoreCase("99")) { consumerElec +=
			 * farmer.getConsumerElectronicsOther() + ","; } else {
			 * 
			 * consumerElec += getText("consumerElec" + electronicsTrim) + ", ";
			 * } } else { consumerElec = getText("consumerElec" +
			 * electronicsTrim) + ","; } } } if (consumerElec != null) {
			 * consumerElec = consumerElec.substring(0, consumerElec.length() -
			 * 1); consumerElec = StringUtil.removeLastComma(consumerElec);
			 * farmer.setConsumerElectronics(consumerElec); }
			 * 
			 * if (!StringUtil.isEmpty(farmer.getVehicle())) {
			 * 
			 * String vehicleArr[] = farmer.getVehicle().split("\\,"); for (int
			 * i = 0; i < vehicleArr.length; i++) { String vehicleTrim =
			 * vehicleArr[i].replaceAll("\\s+", ""); if
			 * (!StringUtil.isEmpty(vehicle)) { if
			 * (vehicleTrim.equalsIgnoreCase("99")) { vehicle +=
			 * farmer.getVehicleOther() + ",";
			 * 
			 * } else {
			 * 
			 * vehicle += getText("vehicle" + vehicleTrim) + ","; } } else {
			 * vehicle = getText("vehicle" + vehicleTrim) + ","; }
			 * 
			 * } }
			 * 
			 * if (vehicle != null) { vehicle = vehicle.substring(0,
			 * vehicle.length() - 1); vehicle =
			 * StringUtil.removeLastComma(vehicle); farmer.setVehicle(vehicle);
			 * }
			 */
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

			/*
			 * if (farmer.getLifeInsurance() != null &&
			 * !StringUtil.isEmpty(farmer.getLifeInsurance()) &&
			 * !farmer.getLifeInsurance().equals("0")) { String val[] =
			 * farmer.getLifeInsurance().split("-");
			 * farmer.setLifeInsure(getText("insure1"));
			 * farmer.setLifeInsurance(val[1]); } else {
			 * farmer.setLifeInsure(getText("insure0"));
			 * farmer.setLifeInsurance(""); } if
			 * (!StringUtil.isEmpty(farmer.getHealthInsurance()) &&
			 * farmer.getHealthInsurance() != null &&
			 * !farmer.getHealthInsurance().equals("0")) { String val[] =
			 * farmer.getHealthInsurance().split("-");
			 * farmer.setHealthInsure(getText("insure1"));
			 * farmer.setHealthInsurance(val[1]); } else {
			 * farmer.setHealthInsure(getText("insure0"));
			 * farmer.setHealthInsurance(""); }
			 */
			if (farmer.getLifeInsurance() != null && !StringUtil.isEmpty(farmer.getLifeInsurance())) {
				if (farmer.getLifeInsurance().equalsIgnoreCase("1")) {
					farmer.setLifeInsurance(getText("insure1"));
				} else {
					farmer.setLifeInsurance(getText("insure0"));
				}
			}
			if (farmer.getHealthInsurance() != null && !StringUtil.isEmpty(farmer.getHealthInsurance())) {
				if (farmer.getHealthInsurance().equalsIgnoreCase("1")) {
					farmer.setHealthInsurance(getText("insure1"));
				} else {
					farmer.setHealthInsurance(getText("insure0"));
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
							cookingFuel += getText("farmer.cookingFuelOther") + ":"
									+ farmerEconomy.getCookingFuelSourceOther() + ",";

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
					cookingFuel = cookingFuel.substring(0, cookingFuel.length() - 1);
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

				if (workDifficulty != null && (!homeDifficulty.equals(""))) {

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
					if (farmer.getLoanPupose().trim().charAt(farmer.getLoanPupose().trim().length() - 1) == '-') {
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

			farmer.setIdProof(farmer.getIdProof());
			farmer.setProofNo(farmer.getProofNo());
			farmer.setOtherIdProof(farmer.getOtherIdProof());
			farmer.setReligion(farmer.getReligion());
			/*
			 * if (getCurrentTenantId().equalsIgnoreCase("atma")) {
			 * FarmCatalogue catalogue =
			 * getCatlogueValueByCode(farmer.getPositionGroup()); if
			 * (!ObjectUtil.isEmpty(catalogue))
			 * farmer.setPositionGroup(catalogue.getName()); Map<String, String>
			 * sanghamMap = getSanghamList(); String sanghamVal =
			 * farmer.getSangham(); //
			 * farmer.setSangham(sanghamMap.get(sanghamVal)); sanghamName =
			 * sanghamMap.get(sanghamVal);
			 * 
			 * if (!StringUtil.isEmpty(farmer.getFamilyMember())) {
			 * 
			 * String familyMemberArr[] = farmer.getFamilyMember().split("\\,");
			 * for (int i = 0; i < familyMemberArr.length; i++) { String
			 * familyMemberTrim = familyMemberArr[i].replaceAll("\\s+", ""); if
			 * (!StringUtil.isEmpty(familyMember)) { if
			 * (familyMemberTrim.equalsIgnoreCase("99")) { familyMember +=
			 * farmer.getFamilyMemberOther() + ",";
			 * 
			 * } else {
			 * 
			 * String otherName =
			 * getCatlogueValueByCode(familyMemberTrim)!=null?
			 * getCatlogueValueByCode(familyMemberTrim).getName():"";
			 * familyMember += otherName + ","; } } else { if
			 * (familyMemberTrim.equalsIgnoreCase("99")) { familyMember =
			 * farmer.getFamilyMemberOther() + ","; } else { String otherName =
			 * getCatlogueValueByCode(familyMemberTrim)!=null?
			 * getCatlogueValueByCode(familyMemberTrim).getName():"";
			 * familyMember = otherName + ","; } }
			 * 
			 * } }
			 * 
			 * if (familyMember != null) { familyMember =
			 * familyMember.substring(0, familyMember.length() - 1);
			 * familyMember = StringUtil.removeLastComma(familyMember);
			 * farmer.setFamilyMember(familyMember); }
			 * 
			 * if (farmer.getInvestigatorDate() != null) { investigatorDate =
			 * genDate.format(farmer.getInvestigatorDate()); }
			 * 
			 * if (!ObjectUtil.isEmpty(farmer.getInvestigatorName())) {
			 * List<Agent> agentLists =
			 * agentService.listAgentByAgentType(AgentType.FIELD_STAFF); if
			 * (!ObjectUtil.isListEmpty(agentLists)) { for (Agent agent :
			 * agentLists) { if
			 * ((agent.getProfileId().equals(farmer.getInvestigatorName()))) {
			 * setInvestigatorName(agent.getProfileIdWithName()); }
			 * 
			 * } }
			 * 
			 * }
			 * 
			 * List<FarmerIncomeDetails> tempFarmerIncomeList = new
			 * ArrayList<FarmerIncomeDetails>();
			 * 
			 * farmerSourceIncomeList =
			 * farmerService.listFarmSourceIncomeByFarmerId(String.valueOf(
			 * farmer.getId())); for (FarmerSourceIncome farmerSourceIncome :
			 * farmerSourceIncomeList) { tempFarmerIncomeList = farmerService
			 * .listFarmerIncomeDetailsBySourceIncomeId(String.valueOf(
			 * farmerSourceIncome.getId())); List<FarmerIncomeDetails>
			 * incomeDetailsList = new LinkedList<FarmerIncomeDetails>(); for
			 * (FarmerIncomeDetails farmerIncomeDetails : tempFarmerIncomeList)
			 * { if (farmerSourceIncome.getName().equalsIgnoreCase(Farmer.
			 * ALLIED_SECTOR) &&
			 * !StringUtil.isEmpty(farmerIncomeDetails.getSourceName())) { if
			 * (farmerIncomeDetails.getSourceName().equalsIgnoreCase("99")) {
			 * farmerIncomeDetails.setSourceName(farmerIncomeDetails.
			 * getSourceNameOther()); } else { farmerIncomeDetails
			 * .setSourceName(getText("allied" +
			 * farmerIncomeDetails.getSourceName())); } } else if
			 * (farmerSourceIncome.getName().equalsIgnoreCase(Farmer.EMPLOYMENT)
			 * && !StringUtil.isEmpty(farmerIncomeDetails.getSourceName())) { if
			 * (farmerIncomeDetails.getSourceName().equalsIgnoreCase("99")) {
			 * farmerIncomeDetails.setSourceName(farmerIncomeDetails.
			 * getSourceNameOther()); } else { farmerIncomeDetails
			 * .setSourceName(getText("employment" +
			 * farmerIncomeDetails.getSourceName())); }
			 * 
			 * } else if
			 * (farmerSourceIncome.getName().equalsIgnoreCase(Farmer.OTHERS) &&
			 * !StringUtil.isEmpty(farmerIncomeDetails.getSourceName())) { if
			 * (farmerIncomeDetails.getSourceName().equalsIgnoreCase("99")) {
			 * farmerIncomeDetails.setSourceName(farmerIncomeDetails.
			 * getSourceNameOther()); } else { farmerIncomeDetails
			 * .setSourceName(getText("otherIncome" +
			 * farmerIncomeDetails.getSourceName())); }
			 * 
			 * } incomeDetailsList.add(farmerIncomeDetails); }
			 * farmerSourceIncome.setIncomeDetailsList(incomeDetailsList); }
			 * 
			 * }
			 */

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
						FarmCatalogue catalogue = getCatlogueValueByCode(implementsTrim);
						if (!ObjectUtil.isEmpty(catalogue)) {
							agricultureImplements += catalogue.getName() + ",";
						}
					}
				}
			}
			if (agricultureImplements != null && !StringUtil.isEmpty(agricultureImplements)) {
				agricultureImplements = agricultureImplements.substring(0, agricultureImplements.length());
				agricultureImplements = StringUtil.removeLastComma(agricultureImplements);
				farmer.setAgricultureImplements(agricultureImplements);
			}

			/*
			 * Map<Integer, String> vMap = getPropertyData(getText("vehicle"));
			 * if (!ObjectUtil.isEmpty(farmer.getVehicle()) &&
			 * !farmer.getVehicle().equalsIgnoreCase("99")) { String key =
			 * farmer.getVehicle();
			 * farmer.setVehicle(vMap.get(Integer.valueOf(key))); } else {
			 * farmer.setVehicle(farmer.getVehicleOther()); }
			 */
			/*
			 * if (!StringUtil.isEmpty(farmer.getBankInfo())) { for
			 * (BankInformation bank : farmer.getBankInfo()) { FarmCatalogue
			 * catalogue = catalogueService
			 * .findCatalogueByCode(bank.getAccType());
			 * bank.setAccType(catalogue != null ? catalogue.getName() : "");
			 * 
			 * if(!StringUtil.isEmpty(catalogue)) { String accVal = catalogue!=
			 * null ? catalogue.getName() : ""; if (!first) { accTypeString =
			 * accTypeString + "," + accVal; } else { accTypeString = accVal;
			 * first = false; } }
			 * 
			 * 
			 * }
			 * 
			 * }
			 */
			if (getCurrentTenantId().equalsIgnoreCase("wilmar")) {
				if (!StringUtil.isEmpty(farmer.getMasterData())) {
					MasterData masterData = clientService.findMasterDataByCode(farmer.getMasterData());
					farmer.setMasterData(masterData.getName());
				} else {
					farmer.setMasterData("");
				}
			}
			setCurrentPage(getCurrentPage());
			command = UPDATE;
			view = DETAIL;
			request.setAttribute(HEADING, getText("farmerdetail"));
		} else {
			request.setAttribute(HEADING, getText(LIST));
			return LIST;
		}

		return view;
	}

	private void alert(String string) {

		// TODO Auto-generated method stub

	}

	/**
	 * Gets the current season code.
	 * 
	 * @return the current season code
	 */
	private String getCurrentSeasonCode() {

		ESESystem preference = preferncesService.findPrefernceById(ESESystem.CLIENT);
		return preference.getPreferences().get(ESESystem.CURRENT_SEASON_CODE);
	}

	/**
	 * Update.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */

	Farmer existing = null;

	public String update() throws Exception {
		int seq=0;
		type = request.getParameter("type");

		if (id != null && !id.equals("")) {
			farmer = farmerService.findFarmerById(Long.valueOf(id));

			/*
			 * if
			 * (getCurrentTenantId().equalsIgnoreCase(ESESystem.PGSS_TENANT_ID)
			 * && !StringUtil.isEmpty(getType()) &&
			 * getType().equals(Farmer.IRP)) { farmer =
			 * farmerService.findFarmerById(Long.valueOf(id), getBranchId()); }
			 * else { farmer = farmerService.findFarmerById(Long.valueOf(id)); }
			 */

			card = cardService.findCardByProfileId(farmer.getFarmerId());
			// account =
			// accountService.findAccountByProfileIdAndProfileType(farmer.getFarmerId(),
			// ESEAccount.FARMER_ACCOUNT);
			if (farmer == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			SimpleDateFormat sm = new SimpleDateFormat(getGeneralDateFormat());
			ESESystem preferences = preferncesService.findPrefernceById("1");

			DateFormat genDate = new SimpleDateFormat(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));

			if (!StringUtil.isEmpty(preferences)) {
				setFpoEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FPOFG));
				setGramPanchayatEnable(preferences.getPreferences().get(ESESystem.ENABLE_GRAMPANJAYAT));
				setFarmerCodeEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE));
				setFarmerBankInfoEnabled(preferences.getPreferences().get(ESESystem.ENABLE_BANK_INFO));
				setIdProofEnabled(preferences.getPreferences().get(ESESystem.ID_PROOF));
				setInsuranceInfoEnabled(preferences.getPreferences().get(ESESystem.ENABLE_INSURANCE_INFO));

			}

			/*
			 * if (!ObjectUtil.isListEmpty(farmer.getFarmerHealthAsses())) {
			 * selectedDisability = "0"; } else { selectedDisability = "1"; }
			 */

			setCurrentPage(getCurrentPage());
			if (farmer.getDateOfBirth() != null) {
				dateOfBirth = sm.format(farmer.getDateOfBirth());
			}
			if (farmer.getImageInfo() != null && farmer.getImageInfo().getPhoto() != null
					&& farmer.getImageInfo().getPhoto().getImage() != null) {
				setFarmerImageByteString(Base64Util.encoder(farmer.getImageInfo().getPhoto().getImage()));
			}

			if (farmer.getIdProofImg() != null) {
				setIdProofImgString(Base64Util.encoder(farmer.getIdProofImg()));
			}

			if (farmer.getDateOfJoining() != null) {
				dateOfJoining = sm.format(farmer.getDateOfJoining());
			}

			if (farmer.getIcsCode() != null) {
				setIcsCode(farmer.getIcsCode());
			}
			if (farmer.getIcsName() != null) {
				setIcsName(farmer.getIcsName());
			}
			if (getCurrentTenantId().equalsIgnoreCase("avt")) {
				if (farmer.getFarmerCode() != null) {
					setFarmerCode(farmer.getFarmerCode());
				}
			}
			if (!ObjectUtil.isEmpty(farmer)) {
				if (getGramPanchayatEnable().equalsIgnoreCase("1")) {
					if (!ObjectUtil.isEmpty(farmer.getVillage().getGramPanchayat())) {
						setSelectedCountry(farmer.getVillage().getGramPanchayat().getCity().getLocality().getState()
								.getCountry().getName());
						setSelectedState(String.valueOf(
								farmer.getVillage().getGramPanchayat().getCity().getLocality().getState().getId()));
						setSelectedLocality(
								String.valueOf(farmer.getVillage().getGramPanchayat().getCity().getLocality().getId()));
						setSelectedCity(String.valueOf(farmer.getVillage().getGramPanchayat().getCity().getId()));
						setSelectedPanchayat(String.valueOf(farmer.getVillage().getGramPanchayat().getId()));
					}
				} else {
					if (!ObjectUtil.isEmpty(farmer.getVillage())) {
						setSelectedCountry(
								farmer.getVillage().getCity().getLocality().getState().getCountry().getName());
						setSelectedState(
								String.valueOf(farmer.getVillage().getCity().getLocality().getState().getId()));
						setSelectedLocality(String.valueOf(farmer.getVillage().getCity().getLocality().getId()));
						setSelectedCity(String.valueOf(farmer.getVillage().getCity().getId()));
					}
				}
			}
			if (getCurrentTenantId().equalsIgnoreCase("wilmar")) {
				farmer.setMasterData(!StringUtil.isEmpty(farmer.getMasterData()) ? farmer.getMasterData() : "");
			}
			farmer.setIsCropInsured(farmer.getIsCropInsured());

			farmerCropInsurance = (farmer.getFarmerCropInsurance() == SELECT_MULTI) ? SELECT_MULTI
					: farmer.getFarmerCropInsurance();

			setSelectedVillage(String.valueOf(farmer.getVillage().getId()));

			/*
			 * if (getCurrentTenantId().equalsIgnoreCase("atma")) {
			 * setSelectedSamithi(!ObjectUtil.isEmpty(farmer.getSamithi()) ?
			 * String.valueOf(farmer.getSamithi().getCode()) : null);
			 * setSelectedSangham(farmer.getSangham());
			 * setSelectedGroupPosition(farmer.getPositionGroup()); if
			 * (farmer.getInvestigatorDate() != null) { investigatorDate =
			 * sm.format(farmer.getInvestigatorDate()); }
			 * 
			 * List<FarmerIncomeDetails> tempFarmerIncomeList = new
			 * ArrayList<FarmerIncomeDetails>();
			 * 
			 * farmerSourceIncomeList =
			 * farmerService.listFarmSourceIncomeByFarmerId(String.valueOf(
			 * farmer.getId())); for (FarmerSourceIncome farmerSourceIncome :
			 * farmerSourceIncomeList) { tempFarmerIncomeList = farmerService
			 * .listFarmerIncomeDetailsBySourceIncomeId(String.valueOf(
			 * farmerSourceIncome.getId())); List<FarmerIncomeDetails>
			 * incomeDetailsList = new LinkedList<FarmerIncomeDetails>(); for
			 * (FarmerIncomeDetails farmerIncomeDetails : tempFarmerIncomeList)
			 * {
			 * 
			 * if (farmerSourceIncome.getName().equalsIgnoreCase(Farmer.
			 * ALLIED_SECTOR) &&
			 * !StringUtil.isEmpty(farmerIncomeDetails.getSourceName())) { if
			 * (farmerIncomeDetails.getSourceName().equalsIgnoreCase("99")) {
			 * farmerIncomeDetails.setSourceName(farmerIncomeDetails.
			 * getSourceNameOther() + "-" +
			 * farmerIncomeDetails.getSourceName()); } else {
			 * farmerIncomeDetails .setSourceName(getText("allied" +
			 * farmerIncomeDetails.getSourceName()) + "-" +
			 * farmerIncomeDetails.getSourceName()); } } else if
			 * (farmerSourceIncome.getName().equalsIgnoreCase(Farmer.EMPLOYMENT)
			 * && !StringUtil.isEmpty(farmerIncomeDetails.getSourceName())) { if
			 * (farmerIncomeDetails.getSourceName().equalsIgnoreCase("99")) {
			 * farmerIncomeDetails.setSourceName(farmerIncomeDetails.
			 * getSourceNameOther() + "-" +
			 * farmerIncomeDetails.getSourceName()); } else {
			 * farmerIncomeDetails .setSourceName(getText("employment" +
			 * farmerIncomeDetails.getSourceName()) + "-" +
			 * farmerIncomeDetails.getSourceName()); }
			 * 
			 * } else if
			 * (farmerSourceIncome.getName().equalsIgnoreCase(Farmer.OTHERS) &&
			 * !StringUtil.isEmpty(farmerIncomeDetails.getSourceName())) { if
			 * (farmerIncomeDetails.getSourceName().equalsIgnoreCase("99")) {
			 * farmerIncomeDetails.setSourceName(farmerIncomeDetails.
			 * getSourceNameOther() + "-" +
			 * farmerIncomeDetails.getSourceName()); } else {
			 * farmerIncomeDetails .setSourceName(getText("otherIncome" +
			 * farmerIncomeDetails.getSourceName()) + "-" +
			 * farmerIncomeDetails.getSourceName()); }
			 * 
			 * } incomeDetailsList.add(farmerIncomeDetails);
			 * 
			 * } farmerSourceIncome.setIncomeDetailsList(incomeDetailsList); }
			 * 
			 * }
			 */
			setSelectedSamithi(
					ObjectUtil.isEmpty(farmer.getSamithi()) ? null : String.valueOf(farmer.getSamithi().getId()));

			Set<BankInformation> bankInfoSet = new HashSet<>();
			List<BankInformation> bankInfo = farmerService.findFarmerBankinfo(farmer.getId());
			if (!ObjectUtil.isListEmpty(bankInfo)) {
				bankInfoSet.addAll(bankInfo);
			}
			if (!ObjectUtil.isEmpty(bankInfoSet)) {
				farmer.setJsonString(bankInformationToJson(bankInfoSet));
			}
			if (getCurrentTenantId().equalsIgnoreCase("chetna")) {

				/*
				 * if (farmer.getLifeInsurance() != null &&
				 * !StringUtil.isEmpty(farmer.getLifeInsurance()) &&
				 * !farmer.getLifeInsurance().equals("0")) { String val[] =
				 * farmer.getLifeInsurance().split("-");
				 * farmer.setLifeInsurance(val[1]); } if
				 * (farmer.getHealthInsurance() != null &&
				 * !StringUtil.isEmpty(farmer.getHealthInsurance()) &&
				 * !farmer.getHealthInsurance().equals("0")) { String val1[] =
				 * farmer.getHealthInsurance().split("-");
				 * farmer.setHealthInsurance(val1[1]); } else {
				 * farmer.setHealthInsure("0"); }
				 */

			}
			farmer.setGovtDept(!StringUtil.isEmpty(farmer.getGovtDept()) ? farmer.getGovtDept().trim() : "");
			ESEAccount acc = accountService.findAccountByProfileIdAndProfileType(farmer.getFarmerId(),
					ESEAccount.CONTRACT_ACCOUNT);

			if (!ObjectUtil.isEmpty(acc)) {
				setAccBalance(String.valueOf(acc.getCashBalance()));
			}
			if (farmer.getLoanRepaymentDate() != null) {
				loanRepaymentDate = sm.format(farmer.getLoanRepaymentDate());
			}
			if (farmer.getInvestigatorDate() != null) {
				investigatorDate = genDate.format(farmer.getInvestigatorDate());
			}
			Integer adultCountMale = !StringUtil.isEmpty(farmer.getAdultCountMale())
					? Integer.parseInt(farmer.getAdultCountMale()) : 0;
			Integer adultCountFemale = !StringUtil.isEmpty(farmer.getAdultCountFemale())
					? Integer.parseInt(farmer.getAdultCountFemale()) : 0;
			Integer childCountMale = !StringUtil.isEmpty(farmer.getChildCountMale())
					? Integer.parseInt(farmer.getChildCountMale()) : 0;
			Integer childCountFemale = !StringUtil.isEmpty(farmer.getChildCountFemale())
					? Integer.parseInt(farmer.getChildCountFemale()) : 0;
			farmer.setTotalHsldLabel(
					String.valueOf(adultCountMale + adultCountFemale + childCountMale + childCountFemale));
			/*
			 * if
			 * (getCurrentTenantId().equalsIgnoreCase(ESESystem.PGSS_TENANT_ID))
			 * { farmer.setIsCertifiedFarmer(0);
			 * 
			 * }
			 */

			id = null;
			command = UPDATE;
			request.setAttribute(HEADING, getText("farmerupdate"));
		} else {
			if (farmer != null) {
				existing = farmerService.findFarmerById(farmer.getId());
				/*
				 * if (getCurrentTenantId().equalsIgnoreCase(ESESystem.
				 * PGSS_TENANT_ID) && !StringUtil.isEmpty(getType()) &&
				 * getType().equals(Farmer.IRP)) { existing =
				 * farmerService.findFarmerById(farmer.getId(), getBranchId());
				 * }
				 */
				if (existing == null) {
					addActionError(NO_RECORD);
					return REDIRECT;
				}
				setCurrentPage(getCurrentPage());
				if (!getCurrentTenantId().equalsIgnoreCase("awi") && !getCurrentTenantId().equalsIgnoreCase("atma") && !getCurrentTenantId().equalsIgnoreCase("avt") && !getCurrentTenantId().equalsIgnoreCase("welspun")) {
					existing.setFarmerCode(farmer.getFarmerCode());
				}
			
				existing.setFirstName(farmer.getFirstName());
				existing.setAddress(farmer.getAddress());
				existing.setIcsCode(farmer.getIcsCode());
				existing.setIcsName(farmer.getIcsName());
				existing.setAdhaarNo(!StringUtil.isEmpty(farmer.getAdhaarNo()) ? farmer.getAdhaarNo() : "");
				existing.setPanCardNo(!StringUtil.isEmpty(farmer.getPanCardNo()) ? farmer.getPanCardNo() : "");
				existing.setCasteName(farmer.getCasteName());
				Set<Farm> existingFarms = existing.getFarms();
				for (Farm farm : existingFarms) {
					if (farm.getFarmDetailedInfo().isSameAddressofFarmer()) {
						farm.getFarmDetailedInfo().setFarmAddress(farmer.getAddress());
					}
				}
				/*
				 * if (!StringUtil.isEmpty(dateOfBirth)) {
				 * existing.setDateOfBirth(format.parse(dateOfBirth)); }
				 */

				if (!StringUtil.isEmpty(dateOfBirth)) {
					existing.setDateOfBirth(DateUtil.convertStringToDate(dateOfBirth, getGeneralDateFormat()));
				}
				DateFormat dd = new SimpleDateFormat(getESEDateFormat());

				if (!StringUtil.isEmpty(dateOfBirth)) {
					Calendar currentDate = Calendar.getInstance();
					int currentYear = currentDate.get(Calendar.YEAR);
					String dateSplit = dateOfBirth.replaceAll("[^a-zA-Z0-9]", "");
					String year = dateSplit.substring(4, 8);
					// String dateSplit[] = dateOfBirth.split("-");
					// String year = dateSplit[2];
					existing.setAge(currentYear - Integer.parseInt(year));

				}

				if (StringUtil.isEmpty(farmer.getLastName())) {
					existing.setLastName("");
				} else
					existing.setLastName(farmer.getLastName());

				existing.setSurName(StringUtil.isEmpty(farmer.getSurName()) ? "" : farmer.getSurName());

				if (!StringUtil.isEmpty(dateOfJoining)) {
					existing.setDateOfJoining(DateUtil.convertStringToDate(dateOfJoining, getGeneralDateFormat()));
				}
				existing.setEmail(farmer.getEmail());
				existing.setPhoneNumber(farmer.getPhoneNumber());
				existing.setMobileNumber(farmer.getMobileNumber());
				existing.setPinCode(farmer.getPinCode());
				existing.setPostOffice(farmer.getPostOffice());
				existing.setGender(farmer.getGender());
				existing.setNoOfFamilyMembers(farmer.getNoOfFamilyMembers());
				existing.setAssistiveDeivce(farmer.getAssistiveDeivce());
				existing.setAssistiveDeivceName(farmer.getAssistiveDeivceName());
				existing.setAssistiveDeviceReq(farmer.getAssistiveDeviceReq());
				existing.setHealthIssue(farmer.getHealthIssue());
				existing.setHealthIssueDescribe(farmer.getHealthIssueDescribe());
				if (getCurrentTenantId().equalsIgnoreCase("wilmar")) {
					existing.setMasterData(!StringUtil.isEmpty(farmer.getMasterData()) ? farmer.getMasterData() : "");
				}
				existing.setTypeOfFamily(farmer.getTypeOfFamily());
				/*
				 * if (getCurrentTenantId().equalsIgnoreCase("awi")) { if
				 * (farmer.getVillage().getId()!=existing.getVillage().getId())
				 * { String codeGen = idGenerator.getFarmerWebCodeIdSeq(
				 * farmer.getVillage().getCity().getCode().substring(0, 1),
				 * farmer.getVillage().getGramPanchayat().getCode().substring(0,
				 * 1)); existing.setFarmerCode(codeGen); }else{ } }
				 */
				if (ESESystem.AWI_TENANT_ID.equalsIgnoreCase(getCurrentTenantId())) {
					/*
					 * if (existing.getVillage().getGramPanchayat().getId() !=
					 * farmer.getVillage().getGramPanchayat().getId()) {
					 * Set<Farm> farms = existing.getFarms();
					 * farms.stream().forEach(farm -> { String codeGen =
					 * idGenerator.getFarmWebCodeIdSeq(
					 * farmer.getVillage().getCity().getCode().substring(0, 1),
					 * farmer.getVillage().getGramPanchayat().getCode().
					 * substring(0, 1)); farm.setFarmId(codeGen);
					 * farmerService.editFarm(farm);
					 * farmerService.updateFarmerRevisionNo(Long.valueOf(farmer.
					 * getId()), DateUtil.getRevisionNumber()); }); }
					 */
				}

				existing.setVillage(locationService.findVillageById(farmer.getVillage().getId()));
				existing.setCity(existing.getVillage().getCity());
				existing.setLifeInsurance(farmer.getLifeInsurance());
				existing.setLifeInsAmount(farmer.getLifeInsAmount());
				existing.setLifInsCmpName(farmer.getLifInsCmpName());
				existing.setHealthInsurance(farmer.getHealthInsurance());
				existing.setHealthInsCmpName(farmer.getHealthInsCmpName());
				existing.setHealthInsAmount(farmer.getHealthInsAmount());
				existing.setIsCropInsured(farmer.getIsCropInsured());
				existing.setCrpInsCmpName(farmer.getHealthInsCmpName());
				existing.setLoanRepaymentAmount(farmer.getLoanRepaymentAmount());
				existing.setIdProof(farmer.getIdProof());
				existing.setProofNo(farmer.getProofNo());
				existing.setOtherIdProof(farmer.getOtherIdProof());

				existing.setAge(farmer.getAge());

				/*
				 * if (getCurrentTenantId().equalsIgnoreCase("atma")) { if
				 * (!StringUtil.isEmpty(selectedSamithi)) { Warehouse samithi =
				 * locationService.findSamithiByCode(selectedSamithi);
				 * existing.setSamithi(ObjectUtil.isEmpty(samithi) ? null :
				 * samithi); } existing.setSangham(selectedSangham);
				 * 
				 * existing.setAdhaarNo(!StringUtil.isEmpty(farmer.getAdhaarNo()
				 * ) ? farmer.getAdhaarNo() : "");
				 * existing.setSocialCategory(farmer.getSocialCategory()); if
				 * (!StringUtil.isEmpty(farmer.getReligionOther())) {
				 * existing.setReligion("99");
				 * existing.setReligionOther(farmer.getReligionOther()); } else
				 * { existing.setReligion(farmer.getReligion()); }
				 * 
				 * existing.setTypeOfFamily(farmer.getTypeOfFamily());
				 * existing.setHouseHoldLandDry(farmer.getHouseHoldLandDry());
				 * existing.setHouseHoldLandWet(farmer.getHouseHoldLandWet());
				 * 
				 * if (!StringUtil.isEmpty(farmer.getHouseOccupationPriOther()))
				 * { existing.setHouseOccupationPrimary("99");
				 * existing.setHouseOccupationPriOther(farmer.
				 * getHouseOccupationPriOther()); } else {
				 * existing.setHouseOccupationPrimary(farmer.
				 * getHouseOccupationPrimary()); } if
				 * (!StringUtil.isEmpty(farmer.getHouseOccupationSecOther())) {
				 * existing.setHouseOccupationSecondary("99");
				 * existing.setHouseOccupationSecOther(farmer.
				 * getHouseOccupationSecOther()); } else {
				 * existing.setHouseOccupationSecondary(farmer.
				 * getHouseOccupationSecondary()); }
				 * 
				 * existing.setFamilyMember(farmer.getFamilyMember());
				 * existing.setFamilyMemberOther(farmer.getFamilyMemberOther());
				 * existing.setInvestigatorOpinion(farmer.getInvestigatorOpinion
				 * ());
				 * 
				 * if (!StringUtil.isEmpty(investigatorDate)) {
				 * existing.setInvestigatorDate(DateUtil.convertStringToDate(
				 * investigatorDate, "dd/MM/yyyy")); }
				 * existing.setInvestigatorName(farmer.getInvestigatorName());
				 * 
				 * }
				 */
				if (!StringUtil.isEmpty(selectedSamithi)) {
					Warehouse samithi = locationService.findSamithiById(Long.valueOf(selectedSamithi));
					existing.setSamithi(ObjectUtil.isEmpty(samithi) ? null : samithi);
				}

				existing.setPositionGroup(selectedGroupPosition);
				
				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)) {
					existing.setIsCertifiedFarmer(1);
				} else {
					existing.setIsCertifiedFarmer(farmer.getIsCertifiedFarmer());
				}

				existing.setCertificationType(farmer.getCertificationType());
				existing.setMaritalSatus(farmer.getMaritalSatus());
				existing.setEducation(farmer.getEducation());
				existing.setAnnualIncome(farmer.getAnnualIncome());
				existing.setAdultCountMale(farmer.getAdultCountMale());
				existing.setAdultCountFemale(farmer.getAdultCountFemale());
				existing.setChildCountMale(farmer.getChildCountMale());
				existing.setChildCountFemale(farmer.getChildCountFemale());
				existing.setIsBeneficiaryInGovScheme(farmer.getIsBeneficiaryInGovScheme());
				existing.setNameOfTheScheme(farmer.getNameOfTheScheme());
				existing.setLoanAmount(farmer.getLoanAmount());
				existing.setLoanPupose(farmer.getLoanPupose());
				existing.setLoanIntRate(farmer.getLoanIntRate());
				existing.setLoanSecurity(farmer.getLoanSecurity());
				existing.setIcsUnitNo(farmer.getIcsUnitNo());
				existing.setIcsTracenetRegNo(farmer.getIcsTracenetRegNo());
				existing.setFarmerCodeByIcs(farmer.getFarmerCodeByIcs());
				existing.setFarmersCodeTracenet(farmer.getFarmersCodeTracenet());
				existing.setLoanIntPeriod(farmer.getLoanIntPeriod());
				existing.setCategory(farmer.getCategory());
				existing.setLoanPuposeOther(farmer.getLoanPuposeOther());
				existing.setFarmerCropInsurance(farmer.getFarmerCropInsurance());
				existing.setLoanSecurityOther(farmer.getLoanSecurityOther());
				existing.setPersonalStatus(farmer.getPersonalStatus());
				if (!StringUtil.isEmpty(farmer.getReligionOther())) {
					existing.setReligion("99");
					existing.setReligionOther(farmer.getReligionOther());
				} else {
					existing.setReligion(farmer.getReligion());
				}

				if (!StringUtil.isEmpty(loanRepaymentDate)) {
					existing.setLoanRepaymentDate(
							DateUtil.convertStringToDate(loanRepaymentDate, getGeneralDateFormat()));

				}
				existing.setPersonalStatus(farmer.getPersonalStatus());

				existing.setYearOfICS(farmer.getYearOfICS());

				existing.setInvestigatorOpinion(farmer.getInvestigatorOpinion());

				if (!StringUtil.isEmpty(investigatorDate)) {
					existing.setInvestigatorDate(
							DateUtil.convertStringToDate(investigatorDate, getGeneralDateFormat()));
				}
				existing.setInvestigatorName(farmer.getInvestigatorName());

				if (enableHHCode.equalsIgnoreCase("1")) {
					if (!StringUtil.isEmpty(existing.getFarmerCode())) {
						String groupCode = existing.getFarmerCode().substring(0, 9);
						if (!groupCode.equalsIgnoreCase(selectedSamithi)) {

							existing.setFarmerCode(idGenerator.getFarmerHHIdSeq(selectedSamithi));
						}
					}
				}

				/*
				 * existing.setChildCountonSite(farmer.getChildCountonSite());
				 * existing.setChildCountonSitePrimary(farmer.
				 * getChildCountonSitePrimary());
				 * existing.setChildCountonSiteSecondary(farmer.
				 * getChildCountonSiteSecondary());
				 */
				existing.setInspectionType(farmer.getInspectionType());
				existing.setIcsStatus(farmer.getIcsStatus());
				// Farmer Economy
				if (farmer.getIsCertifiedFarmer() == 1) {
					if (!StringUtil.isEmpty(farmer.getFarmerEconomy().getHousingOwnership())
							|| !StringUtil.isEmpty(farmer.getFarmerEconomy().getHousingType())
							|| !StringUtil.isEmpty(farmer.getFarmerEconomy().getOtherHousingType())) {
						if (ObjectUtil.isEmpty(existing.getFarmerEconomy())) {
							FarmerEconomy economy = new FarmerEconomy();
							economy.setHousingOwnership(!ObjectUtil.isEmpty(farmer.getFarmerEconomy())
									? farmer.getFarmerEconomy().getHousingOwnership() : SELECT);
							economy.setHousingOwnershipOther(!ObjectUtil.isEmpty(farmer.getFarmerEconomy())
									? farmer.getFarmerEconomy().getHousingOwnershipOther() : "");
							economy.setHousingType(!ObjectUtil.isEmpty(farmer.getFarmerEconomy())
									? farmer.getFarmerEconomy().getHousingType() : "-1");
							economy.setOtherHousingType(!ObjectUtil.isEmpty(farmer.getFarmerEconomy())
									? farmer.getFarmerEconomy().getOtherHousingType() : "");
							economy.setDrinkingWaterSource(!ObjectUtil.isEmpty(farmer.getFarmerEconomy())
									? farmer.getFarmerEconomy().getDrinkingWaterSource() : "");
							economy.setLifeOrHealthInsurance(!ObjectUtil.isEmpty(farmer.getFarmerEconomy())
									? farmer.getFarmerEconomy().getLifeOrHealthInsurance() : SELECT);
							/*
							 * economy.setCropInsurance(!ObjectUtil.isEmpty(
							 * farmer.getFarmerEconomy()) ?
							 * farmer.getFarmerEconomy().getCropInsurance() :
							 * SELECT);
							 */
							economy.setElectrifiedHouse(!ObjectUtil.isEmpty(farmer.getFarmerEconomy())
									? farmer.getFarmerEconomy().getElectrifiedHouse() : SELECT);
							economy.setDrinkingWaterSourceOther(!ObjectUtil.isEmpty(farmer.getFarmerEconomy())
									? farmer.getFarmerEconomy().getDrinkingWaterSourceOther() : "");

							economy.setToiletAvailable(farmer.getFarmerEconomy().getToiletAvailable());
							economy.setIfToiletAvailable(farmer.getFarmerEconomy().getIfToiletAvailable());

							// if
							// (getCurrentTenantId().equalsIgnoreCase("chetna"))
							// {
							economy.setCookingFuel(!ObjectUtil.isEmpty(farmer.getFarmerEconomy())
									? farmer.getFarmerEconomy().getCookingFuel() : "");
							economy.setCookingFuelSourceOther(!ObjectUtil.isEmpty(farmer.getFarmerEconomy())
									? farmer.getFarmerEconomy().getCookingFuelSourceOther() : "");
							// }
							economy.setFarmer(farmer);
							existing.setFarmerEconomy(economy);
						} else {
							existing.getFarmerEconomy()
									.setHousingOwnership(!ObjectUtil.isEmpty(farmer.getFarmerEconomy())
											? farmer.getFarmerEconomy().getHousingOwnership() : SELECT);
							existing.getFarmerEconomy()
									.setHousingOwnershipOther(!ObjectUtil.isEmpty(farmer.getFarmerEconomy())
											? farmer.getFarmerEconomy().getHousingOwnershipOther() : "");
							existing.getFarmerEconomy().setHousingType(!ObjectUtil.isEmpty(farmer.getFarmerEconomy())
									? farmer.getFarmerEconomy().getHousingType() : "-1");
							existing.getFarmerEconomy()
									.setOtherHousingType(!ObjectUtil.isEmpty(farmer.getFarmerEconomy())
											? farmer.getFarmerEconomy().getOtherHousingType() : "");
							existing.getFarmerEconomy()
									.setDrinkingWaterSource(!ObjectUtil.isEmpty(farmer.getFarmerEconomy())
											? farmer.getFarmerEconomy().getDrinkingWaterSource() : "");
							existing.getFarmerEconomy()
									.setLifeOrHealthInsurance(!ObjectUtil.isEmpty(farmer.getFarmerEconomy())
											? farmer.getFarmerEconomy().getLifeOrHealthInsurance() : SELECT);
							/*
							 * existing.getFarmerEconomy()
							 * .setCropInsurance(!ObjectUtil.isEmpty(farmer.
							 * getFarmerEconomy()) ?
							 * farmer.getFarmerEconomy().getCropInsurance() :
							 * SELECT);
							 */
							existing.getFarmerEconomy()
									.setElectrifiedHouse(!ObjectUtil.isEmpty(farmer.getFarmerEconomy())
											? farmer.getFarmerEconomy().getElectrifiedHouse() : SELECT);
							existing.getFarmerEconomy()
									.setDrinkingWaterSourceOther(!ObjectUtil.isEmpty(farmer.getFarmerEconomy())
											? farmer.getFarmerEconomy().getDrinkingWaterSourceOther() : "");

							existing.getFarmerEconomy()
									.setToiletAvailable(farmer.getFarmerEconomy().getToiletAvailable());
							existing.getFarmerEconomy()
									.setIfToiletAvailable(farmer.getFarmerEconomy().getIfToiletAvailable());

							// if
							// (getCurrentTenantId().equalsIgnoreCase("chetna"))
							// {
							existing.getFarmerEconomy().setCookingFuel(!ObjectUtil.isEmpty(farmer.getFarmerEconomy())
									? farmer.getFarmerEconomy().getCookingFuel() : "");
							existing.getFarmerEconomy()
									.setCookingFuelSourceOther(!ObjectUtil.isEmpty(farmer.getFarmerEconomy())
											? farmer.getFarmerEconomy().getCookingFuelSourceOther() : "");
							// }
							existing.getFarmerEconomy().setFarmer(farmer);
						}
					}
				} else {
					if (existing.getFarmerEconomy() != null && !ObjectUtil.isEmpty(existing.getFarmerEconomy())) {
						farmerService.removeFarmerEconomyMappingSQL(existing.getFarmerEconomy());
					}
					existing.setFarmerEconomy(null);
				}

				existing.setHomeDifficulty(
						!StringUtil.isEmpty(farmer.getHomeDifficulty()) ? farmer.getHomeDifficulty() : "");

				existing.setWorkDiffficulty(
						!StringUtil.isEmpty(farmer.getWorkDiffficulty()) ? farmer.getWorkDiffficulty() : "");

				existing.setCommunitiyDifficulty(
						!StringUtil.isEmpty(farmer.getCommunitiyDifficulty()) ? farmer.getCommunitiyDifficulty() : "");

				existing.setAgricultureImplements(!StringUtil.isEmpty(farmer.getAgricultureImplements())
						? farmer.getAgricultureImplements() : "");

				if (!StringUtil.isEmpty(existing.getAgriculture()) && StringUtil.isEmpty(existing.getOtherSource())) {
					// Integer agri =
					// Integer.parseInt(existing.getAgriculture());
					existing.setTotal(existing.getAgriculture());
				} else if (StringUtil.isEmpty(existing.getAgriculture())
						&& !StringUtil.isEmpty(existing.getOtherSource())) {
					// Integer oth =
					// Integer.parseInt(existing.getOtherSource());
					existing.setTotal(existing.getOtherSource());
				} else if (!StringUtil.isEmpty(existing.getAgriculture())
						|| !StringUtil.isEmpty(existing.getOtherSource())) {
					BigDecimal agri = new BigDecimal(existing.getAgriculture());
					BigDecimal oth = new BigDecimal(existing.getOtherSource());
					BigDecimal total = agri.add(oth);
					existing.setTotal(String.valueOf(total));
				}
				/*
				 * if (!ObjectUtil.isEmpty(farmer.getCertificateStandard()) &&
				 * farmer.getCertificateStandard().getId() > 0)
				 * existing.setCertificateStandard(certificationService
				 * .findCertificateStandardById(farmer.getCertificateStandard().
				 * getId())); else existing.setCertificateStandard(null); if
				 * (!ObjectUtil.isEmpty(farmer.getCustomerProject()) &&
				 * farmer.getCustomerProject().getId() > 0)
				 * existing.setCustomerProject(clientService.
				 * findCustomerProjectById(farmer
				 * .getCustomerProject().getId())); else
				 * existing.setCustomerProject(null);
				 */

				existing.setStatus(farmer.getStatus());
				existing.setHeadOfFamily(farmer.getHeadOfFamily());
				existing.setGrsMember(farmer.getGrsMember());
				existing.setPaidShareCapitial(farmer.getPaidShareCapitial());
				// Setting certificate Standard
				/*
				 * CertificateStandard certificateStandard =
				 * certificationService
				 * .findCertificateStandardById(farmer.getCertificateStandard().
				 * getId());
				 * existing.setCertificateStandard(certificateStandard);
				 */
				existing.setEnrollmentPlace(farmer.getEnrollmentPlace());
				existing.setEnrollmentPlaceOther(
						StringUtil.isEmpty(farmer.getEnrollmentPlaceOther()) ? null : farmer.getEnrollmentPlaceOther());

				String jsonString = farmer.getJsonString();
				List<BankInformation> bankInformationList = new ArrayList<BankInformation>();

				if (!StringUtil.isEmpty(jsonString)) {
					GsonBuilder gsonBuilder = new GsonBuilder();
					// gsonBuilder.setDateFormat("M/d/yy hh:mm a"); //Format
					// Dates
					Gson gson = gsonBuilder.create();
					bankInformationList = Arrays.asList(gson.fromJson(jsonString, BankInformation[].class));
				} else {
					bankInformationList = new ArrayList<BankInformation>();
				}

				Set<BankInformation> bankInformationSet = new HashSet<BankInformation>();
				if (bankInformationList.isEmpty()) {
					existing.setBankInfo(new HashSet<BankInformation>());
				} else {
					for (BankInformation bankInformation : bankInformationList) {

						bankInformation.setAccType(bankInformation.getAccType());
						bankInformation.setAccNo(bankInformation.getAccNo());
						bankInformation.setBankName(bankInformation.getBankName());
						bankInformation.setBranchName(bankInformation.getBranchName());
						bankInformation.setSortCode(bankInformation.getSortCode());
						bankInformation.setSwiftCode(bankInformation.getSwiftCode());
						bankInformation.setFarmer(bankInformation.getFarmer());

						if (!StringUtil.isEmpty(bankInformation.getAccNo())) {
							bankInformationSet.add(bankInformation);
						}
						existing.setBankInfo(bankInformationSet);
						// }
					}
				}
				if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
					/*
					 * if (farmer.getLifeInsurance() != null &&
					 * !StringUtil.isEmpty(farmer.getLifeInsurance())) {
					 * existing.setLifeInsurance( farmer.getLifeInsure() + "-" +
					 * farmer.getLifeInsurance()); } else {
					 * existing.setLifeInsurance(farmer.getLifeInsure()); } if
					 * (farmer.getHealthInsurance() != null &&
					 * !StringUtil.isEmpty(farmer.getHealthInsurance())) {
					 * existing.setHealthInsurance( farmer.getHealthInsure() +
					 * "-" + farmer.getHealthInsurance()); } else {
					 * existing.setHealthInsurance(farmer.getHealthInsure()); }
					 */

				}
				existing.setGovtDept(farmer.getGovtDept());
				ESEAccount acc = accountService.findAccountByProfileIdAndProfileType(existing.getFarmerId(),
						ESEAccount.CONTRACT_ACCOUNT);
				if (acc != null) {

					if (!StringUtil.isEmpty(accBalance)) {
						Double accBalance = StringUtil.isDouble(getAccBalance().replace(",", ""))
								? Double.valueOf(getAccBalance().replace(",", "")) : 0D;
						acc.setCashBalance(accBalance);
						accountService.update(acc);
					}
				}
				existing.setConsumerElectronics(farmer.getConsumerElectronics());
				existing.setVehicle(farmer.getVehicle());

				if (farmer.getIsCertifiedFarmer() == 1) {
					if (!StringUtil.isEmpty(farmer.getConsumerElectronicsOther())) { // check
																						// if
																						// other
																						// electronics
																						// are
																						// give.
						existing.setConsumerElectronics("99");
						existing.setConsumerElectronicsOther(farmer.getConsumerElectronicsOther());
					} else {
						existing.setConsumerElectronics(farmer.getConsumerElectronics());
					}
					existing.setConsumerElectronicsOther(farmer.getConsumerElectronicsOther());
					existing.setConsumerElectronics(farmer.getConsumerElectronics());
					// Set vehicle other and from list.
					existing.setVehicle(farmer.getVehicle());
					existing.setVehicleOther(farmer.getVehicleOther());
					existing.setLoanTakenFrom(farmer.getLoanTakenFrom());
					/*
					 * if (farmer.getLoanTakenLastYear() == 0) {
					 * existing.setLoanTakenFrom(farmer.getLoanTakenFrom()); }
					 * else {
					 * existing.setLoanTakenFrom(farmer.getLoanTakenFrom()); }
					 */
					existing.setLoanTakenLastYear(farmer.getLoanTakenLastYear());
					existing.setCellPhone(farmer.getCellPhone());
					existing.setSmartPhone(farmer.getSmartPhone());
				}

				else {
					existing.setConsumerElectronicsOther("");
					existing.setConsumerElectronics("-1");
					existing.setVehicleOther("");
					existing.setVehicle("-1");
					existing.setLoanTakenFrom(farmer.getLoanTakenFrom());
					existing.setLoanTakenLastYear(farmer.getLoanTakenLastYear());
					// existing.setLoanTakenLastYear(0);
					// existing.setLoanTakenFrom("-1");
					existing.setCellPhone("");
					existing.setSmartPhone(farmer.getSmartPhone());
				}
				existing.setAgriculture(farmer.getAgriculture());
				existing.setOtherSource(farmer.getOtherSource());
				existing.setTotal(farmer.getTotal());
				existing.setNoOfSchoolChildMale(farmer.getNoOfSchoolChildMale());
				existing.setNoOfSchoolChildFemale(farmer.getNoOfSchoolChildFemale());
				existing.setMaleCnt(farmer.getMaleCnt());
				existing.setFemaleCnt(farmer.getFemaleCnt());
				existing.setNoOfHouseHoldMem(farmer.getNoOfHouseHoldMem());
				existing.setAcresInsured(farmer.getAcresInsured());
				existing.setLastUpdatedUsername(getUsername());
				existing.setLastUpdatedDate(new Date());
				existing.setFpo(farmer.getFpo()); // set FPO value.
				existing.setShg(farmer.getShg());
				existing.setTotalSourceIncome(farmer.getTotalSourceIncome());
				existing.setFarmerSourceIncome(sourceOfIncomeSet());
				existing.setIsDisable(selectedDisabled);
				List<FarmerSourceIncome> sourceIncmeLists = farmerService
						.listFarmSourceIncomeByFarmerId(String.valueOf(farmer.getId()));

				/*
				 * if (!StringUtil.isEmpty(dynamicFieldsArray) &&
				 * dynamicFieldsArray.length() > 0) {
				 * existing.setFarmerDynamicFieldsValues(
				 * updateFarmerDynamicFieldsSet()); }
				 */

				if (!StringUtil.isEmpty(selfAssesmentJSON)) {
					try {
						Type listType1 = new TypeToken<List<FarmerSelfAsses>>() {
						}.getType();
						List<FarmerSelfAsses> filtersList = new Gson().fromJson(selfAssesmentJSON, listType1);
						Set<FarmerSelfAsses> assesSet = new LinkedHashSet<>();
						List<FarmerSelfAsses> farmerSelfAssesList = new LinkedList<>();
						filtersList.stream().forEach(obj -> {
							obj.setFarmer(existing);
							farmerSelfAssesList.add(obj);
						});
						assesSet.addAll(farmerSelfAssesList);
						existing.setFarmerSelfAsses(assesSet);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				if (!StringUtil.isEmpty(healthAssesmentJSON)) {
					try {
						Type listType1 = new TypeToken<List<FarmerHealthAsses>>() {
						}.getType();
						List<FarmerHealthAsses> filtersList = new Gson().fromJson(healthAssesmentJSON, listType1);
						Set<FarmerHealthAsses> farmerHealthAsses = new LinkedHashSet<>();
						List<FarmerHealthAsses> farmerHealthAssesList = new LinkedList<>();
						filtersList.stream().forEach(obj -> {
							obj.setFarmer(existing);
							farmerHealthAssesList.add(obj);
						});
						farmerHealthAsses.addAll(farmerHealthAssesList);
						existing.setFarmerHealthAsses(farmerHealthAsses);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				if (this.getIdProofImg() != null) {
					existing.setIdProofImg(FileUtil.getBinaryFileContent(this.getIdProofImg()));
				}

				/*
				 * if (getCurrentTenantId().equalsIgnoreCase(ESESystem.
				 * PGSS_TENANT_ID)) { existing.setIsCertifiedFarmer(1);
				 * 
				 * }
				 */
				

				existing.setPrefWrk(!StringUtil.isEmpty(farmer.getPrefWrk()) ? farmer.getPrefWrk() : "");
				existing.setPlaceOfAsss(!StringUtil.isEmpty(farmer.getPlaceOfAsss()) ? farmer.getPlaceOfAsss() : "");
				existing.setTotalHsldLabel(farmer.getTotalHsldLabel());
				farmerService.editFarmer(existing);
				updateDynamicFields("308", String.valueOf(existing.getId()), existing.getSeasonCode(), "1");
				/*
				 * if (!StringUtil.isEmpty(dynamicFieldsArray) &&
				 * dynamicFieldsArray.length() > 0) { Map<String,
				 * List<FarmerDynamicFieldsValue>> fdMap = new HashMap<>();
				 * FarmerDynamicData farmerDynamicData = new
				 * FarmerDynamicData(); DynamicFeildMenuConfig dm =
				 * farmerService.findDynamicMenusByType("308").get(0);
				 * fieldConfigMap = new HashMap<>();
				 * dm.getDynamicFieldConfigs().stream().forEach(section -> {
				 * fieldConfigMap.put(section.getField().getCode(),
				 * section.getField()); }); farmerDynamicData =
				 * farmerService.findFarmerDynamicData("308",
				 * String.valueOf(existing.getId())); if (farmerDynamicData !=
				 * null) { fdMap =
				 * farmerDynamicData.getFarmerDynamicFieldsValues().stream()
				 * .collect(Collectors.groupingBy(FarmerDynamicFieldsValue::
				 * getFieldName));
				 * farmerDynamicData.getFarmerDynamicFieldsValues().clear();
				 * farmerDynamicData.setFarmerDynamicFieldsValues(
				 * editFarmerDynamicFieldsSet(fdMap, farmerDynamicData,
				 * existing)); } else { farmerDynamicData = new
				 * FarmerDynamicData();
				 * farmerDynamicData.setFarmerDynamicFieldsValues(
				 * addFarmerDynamicFieldsSet(existing)); //
				 * farmerDynamicData.setTxnUniqueId(Long.valueOf(head.getMsgNo()
				 * )); farmerDynamicData.setReferenceId(String.valueOf(existing.
				 * getId())); farmerDynamicData.setTxnType("308");
				 * farmerDynamicData.setDate(new Date(0));
				 * farmerDynamicData.setCreatedDate(new Date(0));
				 * farmerDynamicData.setCreatedUser(getUsername());
				 * farmerDynamicData.setStatus("0");
				 * farmerDynamicData.setBranch(existing.getBranchId());
				 * farmerDynamicData.setEntityId("1");
				 * farmerDynamicData.setSeason(existing.getSeasonCode());
				 * 
				 * }
				 * 
				 * farmerService.saveOrUpdate(farmerDynamicData);
				 * farmerService.deleteChildObjects(farmerDynamicData.getTxnType
				 * ());
				 * 
				 * }
				 */
				/*
				 * if (!StringUtil.isEmpty(sourceIncomeId)) { String
				 * srcIncomeIds = sourceIncomeId.substring(1,
				 * sourceIncomeId.length()); String[] incomeIds =
				 * srcIncomeIds.split(","); for (String ids : incomeIds) {
				 * FarmerSourceIncome farmerSourceIncome = new
				 * FarmerSourceIncome();
				 * farmerSourceIncome.setId(Long.valueOf(ids)); //
				 * farmerService.deleteFarmerIncomeDetails(ids);
				 * farmerService.removeFarmerSourceIncome(farmerSourceIncome);
				 * farmerService.deleteFarmerIncomeDetails(
				 * "FARMER_INCOME_DETAILS", "FARMER_SOURCE_ID"); for
				 * (FarmerSourceIncome fsi : sourceIncmeLists) { if (fsi.getId()
				 * != Long.valueOf(ids))
				 * farmerService.updateFarmerIdInFarmerSourceIncome(
				 * String.valueOf(fsi.getId()), String.valueOf(farmer.getId()));
				 * } } } else { for (FarmerSourceIncome fsi : sourceIncmeLists)
				 * { farmerService.updateFarmerIdInFarmerSourceIncome(
				 * String.valueOf(fsi.getId()), String.valueOf(farmer.getId()));
				 * } }
				 */
				if (this.getFarmerImage() != null) {
					if (!ObjectUtil.isEmpty(existing.getImageInfo())) {
						existing.getImageInfo().getPhoto()
								.setImage(FileUtil.getBinaryFileContent(this.getFarmerImage()));
						farmerService.updateImageInfo(existing.getImageInfo());
					} else {
						ImageInfo imageInfo = new ImageInfo();
						Image photo = new Image();
						photo.setImage(FileUtil.getBinaryFileContent(this.getFarmerImage()));
						photo.setImageId(existing.getId() + "-FP");
						imageInfo.setPhoto(photo);
						existing.setImageInfo(ObjectUtil.isEmpty(imageInfo) ? null : imageInfo);
						farmerService.addImageInfo(imageInfo);
						farmerService.updateFarmerImageInfo(existing.getId(), imageInfo.getId());
					}

				}

				if (!ObjectUtil.isEmpty(card))
					cardService.updateCardStatus(existing.getFarmerId(), card.getStatus(), card.getCardRewritable());

				/*
				 * if (!ObjectUtil.isListEmpty(existing.
				 * getFarmerDynamicFieldsValues()) &&
				 * existing.getFarmerDynamicFieldsValues().size() > 0) {
				 * farmerService.updateDynamicFarmerFieldComponentType(); }
				 */

			}
			
			request.setAttribute(HEADING,

					getText("farmerlist"));
			command = LIST;
			return LIST;
		}
		return super.execute();

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
			farmer = farmerService.findFarmerById(Long.valueOf(id));
			setCurrentPage(getCurrentPage());
			if (farmer == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			} else {
				/*
				 * String flag =
				 * farmerService.isFarmMappingexist(farmer.getId()); if
				 * (StringUtil.isEmpty(flag) || flag == null) {
				 */
				// setDateOfBirthAndJoining(farmer.getDateOfBirth(),
				// farmer.getDateOfJoining());

				String flag = farmerService.isFarmerMappingexist(farmer.getId());
				if (StringUtil.isEmpty(flag) || flag == null) {
					ESEAccount acc = accountService.findAccountByProfileIdAndProfileType(farmer.getFarmerId(),
							ESEAccount.CONTRACT_ACCOUNT);
					acc.setStatus(2);
					accountService.update(acc);
					farmerService.updateFarmerStatusByFarmerId(farmer.getFarmerId());
				} else {
					addActionError(getText(flag));
					request.setAttribute(HEADING, getText(DETAIL));
					return DETAIL;
				}

				/*
				 * } else { addActionError(getText(flag));
				 * request.setAttribute(HEADING, getText(DETAIL)); return
				 * DETAIL; }
				 */

				/*
				 * if (farmer.getFarms().size() > 0) { card =
				 * cardService.findCardByProfileId(farmer.getFarmerId());
				 * addActionError(getText("farm.exist")); if
				 * (farmer.getDateOfBirth() != null) { dateOfBirth =
				 * df.format(farmer.getDateOfBirth()); } if
				 * (farmer.getDateOfJoining() != null) { dateOfJoining =
				 * df.format(farmer.getDateOfJoining()); }
				 * request.setAttribute(HEADING, getText("farmerdetail"));
				 * return DETAIL; } else if
				 * (txnService.findTxnExistForFarmer(farmer.getFarmerId())) {
				 * card = cardService.findCardByProfileId(farmer.getFarmerId());
				 * addActionError(getText("txn.exist"));
				 * request.setAttribute(HEADING, getText("farmerdetail"));
				 * return DETAIL; } else { farmerService.removeFarmer(farmer);
				 * cardService.removeCardByProfileId(farmer.getFarmerId());
				 * farmerService.removeContractByFarmerId(farmer.getId()); }
				 */
			}
		}
		request.setAttribute(HEADING, getText("farmerlist"));
		return LIST;
	}

	/**
	 * Update farmer economy.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String updateFarmerEconomy() throws Exception {

		if (!ObjectUtil.isEmpty(farmerEconomy) && !ObjectUtil.isEmpty(farmerEconomy.getFarmer())) {
			farmer = farmerService.findFarmerById(farmerEconomy.getFarmer().getId());
			if (!ObjectUtil.isEmpty(farmer)) {
				if (ObjectUtil.isEmpty(farmer.getFarmerEconomy()))
					farmer.setFarmerEconomy(new FarmerEconomy());
				farmer.getFarmerEconomy().setHousingOwnership(farmerEconomy.getHousingOwnership());
				farmer.getFarmerEconomy().setHousingType(farmerEconomy.getHousingType());
				if (!StringUtil.isEmpty(farmer.getFarmerEconomy().getHousingOwnershipOther())) { // check
																									// if
																									// other
																									// vehicle
																									// is
																									// given.
					farmer.getFarmerEconomy()
							.setHousingOwnershipOther(farmer.getFarmerEconomy().getHousingOwnershipOther());
				}
				if (!StringUtil.isEmpty(farmer.getFarmerEconomy().getOtherHousingType())) { // check
																							// if
																							// other
																							// vehicle
																							// is
																							// given.
					farmer.getFarmerEconomy().setOtherHousingType(farmer.getFarmerEconomy().getOtherHousingType());
				}
				farmer.getFarmerEconomy().setDrinkingWaterSource(farmerEconomy.getDrinkingWaterSource());
				// if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
				farmer.getFarmerEconomy().setCookingFuel(farmerEconomy.getCookingFuel());
				// }
				farmer.getFarmerEconomy().setFarmer(farmer);
				farmer.getFarmerEconomy().setAnnualIncome(getRupee() + "." + getPaise());

				farmerService.editFarmer(farmer);
			}
			setId(String.valueOf(farmerEconomy.getFarmer().getId()));
		} else {
			return REDIRECT;
		}

		return detail();

	}

	/**
	 * Delete farmer economy.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String deleteFarmerEconomy() throws Exception {

		if (!ObjectUtil.isEmpty(farmerEconomy) && !ObjectUtil.isEmpty(farmerEconomy.getFarmer())
				&& farmerEconomy.getId() > 0) {
			farmerService.removeFarmerEconomyMappingSQL(farmerEconomy);
			setId(String.valueOf(farmerEconomy.getFarmer().getId()));
		} else {
			return REDIRECT;
		}
		return detail();

	}

	/**
	 * Load customers.
	 */
	private void loadCustomers() {

		List<Customer> customersList = new LinkedList<Customer>();
		customersList = clientService.listOfCustomers();
		if (!ObjectUtil.isListEmpty(customersList))
			setCustomers(customersList);

	}

	/**
	 * Populate customer projects.
	 */
	public void populateCustomerProjects() {

		StringBuffer sb = new StringBuffer();
		if (!StringUtil.isEmpty(selectedCustomer)) {
			List<CustomerProject> customerProjects = clientService
					.listOfCustomerProjectByCustomerId(Long.valueOf(selectedCustomer));

			if (!ObjectUtil.isListEmpty(customerProjects)) {
				for (int count = 0; count < customerProjects.size(); count++) {
					CustomerProject customerProject = customerProjects.get(count);
					if (!ObjectUtil.isEmpty(customerProject)) {
						sb.append(customerProject.getId()).append("~").append(customerProject.getNameOfProject());
					}
					if ((count + 1) != customerProjects.size()) {
						sb.append("||");
					}
				}
			}
		}
		printAjaxResponse(sb.toString(), "text/html");
	}

	/**
	 * Gets the customer projects.
	 * 
	 * @return the customer projects
	 */
	public List<CustomerProject> getCustomerProjects() {

		if (!ObjectUtil.isEmpty(farmer) && !ObjectUtil.isEmpty(farmer.getCustomerProject())
				&& !ObjectUtil.isEmpty(farmer.getCustomerProject().getCustomer())) {
			List<CustomerProject> customerProjects = clientService
					.listOfCustomerProjectByCustomerId(farmer.getCustomerProject().getCustomer().getId());

			if (!ObjectUtil.isListEmpty(customerProjects)) {
				setCustomerProjects(customerProjects);
			}

		}
		return customerProjects;
	}

	/**
	 * Sets the date of birth and joining.
	 * 
	 * @param dob
	 *            the dob
	 * @param doj
	 *            the doj
	 */
	private void setDateOfBirthAndJoining(Date dob, Date doj) {

		if (dob != null) {
			dateOfBirth = df.format(dob);
		}

		if (doj != null)
			dateOfJoining = df.format(doj);
		else
			dateOfJoining = df.format(new Date());
	}

	/**
	 * @see com.opensymphony.xwork2.ActionSupport#getFieldErrors()
	 */
	public Map<String, List<String>> getFieldErrors() {

		Map<String, List<String>> errors = super.getFieldErrors();
		if (errors.containsKey("farmer.noOfFamilyMembers")) {
			errors.remove("emptyFamilyMembers");
		}
		return errors;
	}

	/**
	 * Populate state.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public void populateState() throws Exception {

		if (!selectedCountry.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedCountry))) {
			states = locationService.listStates(selectedCountry);
		}
		JSONArray stateArr = new JSONArray();
		if (!ObjectUtil.isEmpty(states)) {
			for (State state : states) {
				// stateArr.add(getJSONObject(state.getId(), state.getCode() +
				// "-" + state.getName()));
				String name = getLanguagePref(getLoggedInUserLanguage(), state.getCode().trim().toString());
				if (!StringUtil.isEmpty(name) && name != null) {
					stateArr.add(getJSONObject(String.valueOf(state.getId()), state.getCode().toString() + "-"
							+ getLanguagePref(getLoggedInUserLanguage(), state.getCode().toString())));
				} else {
					stateArr.add(getJSONObject(state.getId(), state.getCode() + "-" + state.getName()));
				}
			}
		}
		sendAjaxResponse(stateArr);
	}

	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}

	/**
	 * Gets the countries.
	 * 
	 * @return the countries
	 */
	public Map<String, String> getCountries() {

		Map<String, String> countryMap = new LinkedHashMap<String, String>();
		List<Country> countryList = locationService.listCountries();
		for (Country obj : countryList) {
			countryMap.put(obj.getName(), obj.getCode() + "-" + obj.getName());
		}
		return countryMap;
	}

	@SuppressWarnings("unchecked")
	public void populateCitySave() {
		Municipality city = new Municipality();
		Municipality municipality = locationService.findMunicipalityByName(getSelectedCity());
		if (!ObjectUtil.isEmpty(municipality) && municipality.getLocality().getId() == locationService
				.findLocalityById(Long.valueOf(selectedLocality)).getId()) {
			getJsonObject().put("msg", "0");
			getJsonObject().put("title", getText("unique.ProcurementProductName"));

		} else {
			city.setName(getSelectedCity());
			city.setLocality(locationService.findLocalityById(Long.valueOf(selectedLocality)));
			city.setBranchId(getBranchId());
			city.setCode(idGenerator.getMandalIdSeq());
			locationService.addMunicipality(city);

			getJsonObject().put("msg", getText("msg.added"));
			getJsonObject().put("title", getText("title.success"));
		}

		sendAjaxResponse(getJsonObject());
	}

	@SuppressWarnings("unchecked")
	public void populateVillageSave() {
		Village village = new Village();
		Village vill = locationService.findVillageAndCityByVillName(getSelectedVillage(), Long.valueOf(selectedCity));
		if (!ObjectUtil.isEmpty(vill)) {
			getJsonObject().put("msg", "0");
			getJsonObject().put("title", getText("unique.ProcurementProductName"));
		} else {
			village.setName(getSelectedVillage());
			village.setCity(locationService.findMunicipalityById(Long.valueOf(selectedCity)));
			if (getGramPanchayatEnable().equalsIgnoreCase("1")) {
				GramPanchayat gramPanchayat = locationService.findGramPanchayatById(Long.valueOf(selectedPanchayat));
				village.setGramPanchayat(gramPanchayat);
			}
			
			village.setBranchId(getBranchId());
			village.setCode(idGenerator.getVillageIdSeq());
			if(getCurrentTenantId().equalsIgnoreCase("avt")){
				village.setSeq("1");
				village.setCode(selectedVillageCode);
			}
			locationService.addVillage(village);
		
			getJsonObject().put("msg", getText("msg.added"));
			getJsonObject().put("title", getText("title.success"));
		}

		sendAjaxResponse(getJsonObject());
	}

	public void populateSamithiSave() {
		Warehouse warehouse = new Warehouse();
		warehouse.setCode(idGenerator.getGroupIdSeq());
		warehouse.setName(getSelectedSamithi());
		warehouse.setBranchId(getBranchId());
		warehouse.setTypez(Warehouse.SAMITHI);
		if (!StringUtil.isEmpty(formationDate)) {
			warehouse.setFormationDate(DateUtil.convertStringToDate(formationDate, DateUtil.PROFILE_DATE_FORMAT));
		}
		locationService.addWarehouse(warehouse);

		getJsonObject().put("msg", getText("msg.added"));
		getJsonObject().put("title", getText("title.success"));
		sendAjaxResponse(getJsonObject());
	}

	public void populatePanchayatSave() {
		GramPanchayat gramPanchayat = new GramPanchayat();
		gramPanchayat.setName(getSelectedPanchayat());
		gramPanchayat.setCity(locationService.findMunicipalityById(Long.valueOf(selectedCity)));
		gramPanchayat.setBranchId(getBranchId());
		gramPanchayat.setCode(idGenerator.getGramPanchayatIdSeq());
		locationService.addGramPanchayat(gramPanchayat);

		getJsonObject().put("msg", getText("msg.added"));
		getJsonObject().put("title", getText("title.success"));
		sendAjaxResponse(getJsonObject());
	}

	/**
	 * Gets the account service.
	 * 
	 * @return the account service
	 */
	public IAccountService getAccountService() {

		return accountService;
	}

	/**
	 * Sets the account service.
	 * 
	 * @param accountService
	 *            the new account service
	 */
	public void setAccountService(IAccountService accountService) {

		this.accountService = accountService;
	}

	/**
	 * Populate district.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	/*
	 * public String populateDistrict() throws Exception { if
	 * (!selectedState.equalsIgnoreCase("null") &&
	 * (!StringUtil.isEmpty(selectedState))) { localities =
	 * locationService.findLocalityByStateId(Long.parseLong(selectedState)); }
	 * sendResponse(localities); return null; }
	 */
	/**
	 * Populate locality.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public void populateLocality() throws Exception {

		if (!selectedState.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedState))) {
			listLocalities = locationService.findLocalityByStateId(Long.valueOf(selectedState));
		}
		JSONArray localtiesArr = new JSONArray();
		if (!ObjectUtil.isEmpty(listLocalities)) {
			for (Locality locality : listLocalities) {
				// localtiesArr.add(getJSONObject(locality.getId(),
				// locality.getCode() + "-" + locality.getName()));
				String name = getLanguagePref(getLoggedInUserLanguage(), locality.getCode().trim().toString());
				if (!StringUtil.isEmpty(name) && name != null) {
					localtiesArr.add(getJSONObject(String.valueOf(locality.getId()),
							locality.getCode().trim().toString() + "-" + getLanguagePref(getLoggedInUserLanguage(),
									locality.getCode().trim().toString())));
				} else {
					localtiesArr.add(getJSONObject(locality.getId(), locality.getCode() + "-" + locality.getName()));
				}
			}
		}
		sendAjaxResponse(localtiesArr);

	}

	/**
	 * Populate city.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public void populateCity() throws Exception {

		if (!selectedLocality.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedLocality))) {
			cities = locationService.listMunicipalitiesByLocalityId(Long.parseLong(selectedLocality));
		}
		JSONArray cityArray = new JSONArray();
		if (!ObjectUtil.isEmpty(cities)) {
			for (Municipality municipality : cities) {
				// cityArray.add(getJSONObject(municipality.getId(),
				// municipality.getCode() + "-" + municipality.getName()));

				String name = getLanguagePref(getLoggedInUserLanguage(), municipality.getCode().trim().toString());
				if (!StringUtil.isEmpty(name) && name != null) {
					cityArray.add(getJSONObject(String.valueOf(municipality.getId()), municipality.getCode().toString()
							+ "-" + getLanguagePref(getLoggedInUserLanguage(), municipality.getCode().toString())));
				} else {
					cityArray.add(
							getJSONObject(municipality.getId(), municipality.getCode() + "-" + municipality.getName()));
				}

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
				// villageArr.add(getJSONObject(village.getId(),
				// village.getCode() + "-" + village.getName()));
				String name = getLanguagePref(getLoggedInUserLanguage(), village.getCode().trim().toString());
				if (!StringUtil.isEmpty(name) && name != null) {
					villageArr.add(getJSONObject(String.valueOf(village.getId()), village.getCode().toString() + "-"
							+ getLanguagePref(getLoggedInUserLanguage(), village.getCode().toString())));
				} else {
					villageArr.add(getJSONObject(village.getId(), village.getCode() + "-" + village.getName()));
				}
			}
		}
		sendAjaxResponse(villageArr);

	}

	public void populateVillageByCity() throws Exception {

		if (!selectedCity.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedCity))
				&& !selectedCity.equalsIgnoreCase("0")) {
			villages = locationService.listVillagesByCityId(Long.valueOf(selectedCity));
		}
		JSONArray villageArr = new JSONArray();
		if (!ObjectUtil.isEmpty(villages)) {
			for (Village village : villages) {
				villageArr.add(getJSONObject(village.getId(), village.getCode() + "-" + village.getName()));
			}
		}
		sendAjaxResponse(villageArr);

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

	/**
	 * Gets the farmer.
	 * 
	 * @return the farmer
	 */
	public Farmer getFarmer() {

		return farmer;
	}

	/**
	 * Sets the farmer.
	 * 
	 * @param farmer
	 *            the new farmer
	 */
	public void setFarmer(Farmer farmer) {

		this.farmer = farmer;
	}

	/**
	 * Gets the agent service.
	 * 
	 * @return the agent service
	 */
	public IAgentService getAgentService() {

		return agentService;
	}

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
	 * Gets the list account status.
	 * 
	 * @return the list account status
	 */
	public Map<Integer, String> getListAccountStatus() {

		return listAccountStatus;
	}

	/**
	 * Sets the list account status.
	 * 
	 * @param listAccountStatus
	 *            the list account status
	 */
	public void setListAccountStatus(Map<Integer, String> listAccountStatus) {

		this.listAccountStatus = listAccountStatus;
	}

	/**
	 * Gets the date of birth.
	 * 
	 * @return the date of birth
	 */
	public String getDateOfBirth() {

		return dateOfBirth;
	}

	/**
	 * Sets the date of birth.
	 * 
	 * @param dateOfBirth
	 *            the new date of birth
	 */
	public void setDateOfBirth(String dateOfBirth) {

		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * Gets the selected country.
	 * 
	 * @return the selected country
	 */
	public String getSelectedCountry() {

		return selectedCountry;
	}

	/**
	 * Sets the selected country.
	 * 
	 * @param selectedCountry
	 *            the new selected country
	 */
	public void setSelectedCountry(String selectedCountry) {

		this.selectedCountry = selectedCountry;
	}

	/**
	 * Gets the selected state.
	 * 
	 * @return the selected state
	 */
	public String getSelectedState() {

		return selectedState;
	}

	/**
	 * Sets the selected state.
	 * 
	 * @param selectedState
	 *            the new selected state
	 */
	public void setSelectedState(String selectedState) {

		this.selectedState = selectedState;
	}

	/**
	 * Gets the selected locality.
	 * 
	 * @return the selected locality
	 */
	public String getSelectedLocality() {

		return selectedLocality;
	}

	/**
	 * Sets the selected locality.
	 * 
	 * @param selectedLocality
	 *            the new selected locality
	 */
	public void setSelectedLocality(String selectedLocality) {

		this.selectedLocality = selectedLocality;
	}

	/**
	 * Gets the location service.
	 * 
	 * @return the location service
	 */
	public ILocationService getLocationService() {

		return locationService;
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
	 * Gets the gender type.
	 * 
	 * @return the gender type
	 */
	public Map<String, String> getGenderType() {

		return genderType;
	}

	/**
	 * Sets the gender type.
	 * 
	 * @param genderType
	 *            the gender type
	 */
	public void setGenderType(Map<String, String> genderType) {

		this.genderType = genderType;
	}

	/**
	 * Gets the localities.
	 * 
	 * @return the localities
	 */
	/*
	 * public List<Locality> getLocalities() { if
	 * (!StringUtil.isEmpty(selectedState)) { localities =
	 * locationService.findLocalityByStateId(Long.parseLong(selectedState)); }
	 * return localities; }
	 *//**
		 * Sets the localities.
		 * 
		 * @param localities
		 *            the new localities
		 */

	/*
	 * public void setLocalities(List<Locality> localities) { this.localities =
	 * localities; }
	 */
	/**
	 * Gets the states.
	 * 
	 * @return the states
	 */
	/*
	 * public List<State> getStates() {
	 * 
	 * if (!StringUtil.isEmpty(selectedCountry)) { states =
	 * locationService.listStates(selectedCountry); } return states; }
	 */

	public Map<Long, String> getStates() {

		Map<Long, String> stateMap = new LinkedHashMap<Long, String>();
		if (!StringUtil.isEmpty(selectedCountry)) {
			states = locationService.listStates(selectedCountry);
		}
		if (!ObjectUtil.isEmpty(states)) {
			for (State state : states) {
				stateMap.put(state.getId(), state.getCode() + "-" + state.getName());
			}

		}

		return stateMap;

	}

	/**
	 * Sets the states.
	 * 
	 * @param states
	 *            the new states
	 */
	public void setStates(List<State> states) {

		this.states = states;
	}

	/**
	 * Gets the cities.
	 * 
	 * @return the cities
	 */
	public Map<Long, String> getCities() {
		Map<Long, String> cityMap = new LinkedHashMap<Long, String>();
		if (!StringUtil.isEmpty(selectedLocality)) {
			cities = locationService.listMunicipalitiesByLocalityId(Long.parseLong(selectedLocality));
		}
		if (!ObjectUtil.isEmpty(cities)) {
			for (Municipality municipality : cities) {
				cityMap.put(municipality.getId(), municipality.getCode() + "-" + municipality.getName());
			}
		}
		return cityMap;
	}

	/**
	 * Sets the cities.
	 * 
	 * @param cities
	 *            the new cities
	 */
	public void setCities(List<Municipality> cities) {

		this.cities = cities;
	}

	/**
	 * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getData() {

		// Added Code below to set FPO enabled value.
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			setFpoEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FPOFG));
			setGramPanchayatEnable(preferences.getPreferences().get(ESESystem.ENABLE_GRAMPANJAYAT));
			setFarmerCodeEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE));
			setFarmerBankInfoEnabled(preferences.getPreferences().get(ESESystem.ENABLE_BANK_INFO));
			setIdProofEnabled(preferences.getPreferences().get(ESESystem.ID_PROOF));
			setInsuranceInfoEnabled(preferences.getPreferences().get(ESESystem.ENABLE_INSURANCE_INFO));
			setEnableHHCode(preferncesService.findPrefernceByName(ESESystem.CODE_TYPE));
			setIcsDropDown(preferences.getPreferences().get(ESESystem.ENABLE_ICSNAME));
			setIsCertifiedFarmerInfoEnabled(preferences.getPreferences().get(ESESystem.IS_CERTIFIED_FARMER));
			getCurrentTenantId();
		}
		DateFormat format = new SimpleDateFormat(getGeneralDateFormat());

		if (farmer != null) {
			if (!StringUtil.isEmpty(selectedVillage)) {
				farmer.setVillage(locationService.findVillageBySelectedVillageId(Long.parseLong(selectedVillage)));
			}
			if (!StringUtil.isEmpty(dateOfBirth)) {
				try {
					farmer.setDateOfBirth((Date) format.parse(dateOfBirth));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			if (!StringUtil.isEmpty(dateOfJoining)) {
				try {
					farmer.setDateOfJoining((Date) format.parse(dateOfJoining));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			if (!StringUtil.isEmpty(farmerImage)) {

				farmer.setFarmerImage(farmerImage);

			}

			/*
			 * if (getCurrentTenantId().equalsIgnoreCase("atma")) { if
			 * (!StringUtil.isEmpty(selectedSamithi)) {
			 * farmer.setSamithi(locationService.findSamithiByCode(
			 * selectedSamithi));
			 * 
			 * }
			 * 
			 * }
			 */
			if (!StringUtil.isEmpty(selectedSamithi)) {
				farmer.setSamithi(locationService.findSamithiById(Long.valueOf(selectedSamithi)));

			}

			setSelectedCountry(getSelectedCountry());

			if (!StringUtil.isEmpty(investigatorDate)) {
				try {
					farmer.setInvestigatorDate((Date) format.parse(investigatorDate));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			if (!StringUtil.isEmpty(loanRepaymentDate)) {
				try {
					farmer.setLoanRepaymentDate((Date) format.parse(loanRepaymentDate));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			farmer.setGovtDept(!StringUtil.isEmpty(farmer.getGovtDept()) ? farmer.getGovtDept().trim() : "");

			Set<FarmerSourceIncome> sourceSet = sourceOfIncomeSet();
			for (FarmerSourceIncome farmerSourceIncome : sourceSet) {
				List<FarmerIncomeDetails> incomeDetailsList = new LinkedList<FarmerIncomeDetails>();

				for (FarmerIncomeDetails farmerIncomeDetails : farmerSourceIncome.getFarmerIncomeDetails()) {

					if (farmerSourceIncome.getName().equalsIgnoreCase(Farmer.ALLIED_SECTOR)
							&& !StringUtil.isEmpty(farmerIncomeDetails.getSourceName())) {
						if (farmerIncomeDetails.getSourceName().equalsIgnoreCase("99")) {
							farmerIncomeDetails.setSourceName(farmerIncomeDetails.getSourceNameOther() + "-"
									+ farmerIncomeDetails.getSourceName());
						} else {
							farmerIncomeDetails.setSourceName(getText("allied" + farmerIncomeDetails.getSourceName())
									+ "-" + farmerIncomeDetails.getSourceName());
						}
					} else if (farmerSourceIncome.getName().equalsIgnoreCase(Farmer.EMPLOYMENT)
							&& !StringUtil.isEmpty(farmerIncomeDetails.getSourceName())) {
						if (farmerIncomeDetails.getSourceName().equalsIgnoreCase("99")) {
							farmerIncomeDetails.setSourceName(farmerIncomeDetails.getSourceNameOther() + "-"
									+ farmerIncomeDetails.getSourceName());
						} else {
							farmerIncomeDetails
									.setSourceName(getText("employment" + farmerIncomeDetails.getSourceName()) + "-"
											+ farmerIncomeDetails.getSourceName());
						}

					} else if (farmerSourceIncome.getName().equalsIgnoreCase(Farmer.OTHERS)
							&& !StringUtil.isEmpty(farmerIncomeDetails.getSourceName())) {
						if (farmerIncomeDetails.getSourceName().equalsIgnoreCase("99")) {
							farmerIncomeDetails.setSourceName(farmerIncomeDetails.getSourceNameOther() + "-"
									+ farmerIncomeDetails.getSourceName());
						} else {
							farmerIncomeDetails
									.setSourceName(getText("otherIncome" + farmerIncomeDetails.getSourceName()) + "-"
											+ farmerIncomeDetails.getSourceName());
						}

					}
					incomeDetailsList.add(farmerIncomeDetails);

				}
				farmerSourceIncome.setIncomeDetailsList(incomeDetailsList);
			}
			farmerSourceIncomeList.addAll(sourceSet);

			if (!StringUtil.isEmpty(selfAssesmentJSON)) {
				try {
					Type listType1 = new TypeToken<List<FarmerSelfAsses>>() {
					}.getType();
					List<FarmerSelfAsses> filtersList = new Gson().fromJson(selfAssesmentJSON, listType1);
					Set<FarmerSelfAsses> assesSet = new LinkedHashSet<>();
					assesSet.addAll(filtersList);
					farmer.setFarmerSelfAsses(assesSet);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (!StringUtil.isEmpty(healthAssesmentJSON)) {
				try {
					Type listType1 = new TypeToken<List<FarmerHealthAsses>>() {
					}.getType();
					List<FarmerHealthAsses> filtersList = new Gson().fromJson(healthAssesmentJSON, listType1);
					Set<FarmerHealthAsses> farmerHealthAsses = new LinkedHashSet<>();
					farmerHealthAsses.addAll(filtersList);
					farmer.setFarmerHealthAsses(farmerHealthAsses);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		// To get Card Rewritable Status
		cardRewriteList.put(ESECard.IS_REWRITABLE_NO, getText("cardRewrite0"));
		cardRewriteList.put(ESECard.IS_REWRITABLE_YES, getText("cardRewrite1"));

		// To get Card Status List
		cardStatusList.put(ESECard.ACTIVE, getText("card1"));
		cardStatusList.put(ESECard.INACTIVE, getText("card0"));
		genderType.put(Farmer.SEX_MALE, getText("MALE"));
		genderType.put(Farmer.SEX_FEMALE, getText("FEMALE"));
		if (getCurrentTenantId().equalsIgnoreCase("olivado")) {
			genderType.put(Farmer.SEX_MALE_FEMALE, getText("MALEFEMALE"));
		}

		// account status
		listAccountStatus.put(ESEAccount.ACTIVE, getText("account1"));
		listAccountStatus.put(ESEAccount.INACTIVE, getText("account0"));

		// Farmer Status
		farmerStatus.put(Farmer.Status.ACTIVE.ordinal(), getText("status" + Farmer.Status.ACTIVE.ordinal()));
		farmerStatus.put(Farmer.Status.INACTIVE.ordinal(), getText("status" + Farmer.Status.INACTIVE.ordinal()));
		if (getCurrentTenantId().equalsIgnoreCase("olivado")) {
			farmerStatus.put(Farmer.Status.WITHDRAWN.ordinal(), getText("status" + Farmer.Status.WITHDRAWN.ordinal()));
			farmerStatus.put(Farmer.Status.SUSPENDED.ordinal(), getText("status" + Farmer.Status.SUSPENDED.ordinal()));
			farmerStatus.put(Farmer.Status.VIOLATED.ordinal(), getText("status" + Farmer.Status.VIOLATED.ordinal()));
		}

		loadCustomers();
		certificationTypes = formCertification("fct", certificationTypes);
		certificationLevels = formCertificationLevels("fcl", certificationLevels);
		// maritalSatuses = formMaritialMap("ms", maritalSatuses);
		// educationList = formEducationMap("edu", educationList);
		housingOwnerships = formHousingOwnerShip("hop", housingOwnerships);
		// housingTypes = formHousingType("ht", housingTypes);
		interesetPeriod = formHousingType("interestRatePeriod", interesetPeriod);
		inspectionTypes = formMap("it", inspectionTypes);
		icsStatuses = formMap("ics", icsStatuses);
		enrollmentMap = formEnrollmentPlaceMap("et", enrollmentMap);
		isFarmerCertified = formMap("cer", isFarmerCertified);
		// categoryList = formCategory("cat", categoryList);
		// statusList =formMap("sl",statusList);
		statusList.put(1, getLocaleProperty("status-1"));
		statusList.put(2, getLocaleProperty("status-2"));

		headOfFamilyList.put(1, getText("head-1"));
		headOfFamilyList.put(2, getText("head-2"));
		grsList.put(1, getText("grs-1"));
		grsList.put(2, getText("grs-2"));
		paidShareCapitialList.put(1, getText("share-1"));
		paidShareCapitialList.put(2, getText("share-2"));

		isToiletAvailable.put(1, getText("ita-1"));
		isToiletAvailable.put(2, getText("ita-2"));

		availCellPhone.put(1, getText("acp-1"));
		availCellPhone.put(2, getText("acp-2"));
		// farmer asset Ownership fields.
		// vehicleMap = formHousingType("vehicle", vehicleMap);
		// consumerElecMap = formHousingType("consumerElec", consumerElecMap);
		cellPhoneMap = formHousingType("cellPhone", cellPhoneMap);
		housingOwnershipMap = formHousingType("hop", housingOwnershipMap);
		// housingTypeMap = formHousingType("housingType", housingTypes);
		// drinkingWSMap = formHousingType("drinkingWS", drinkingWSMap);
		electrifiedHouseMap = formHousingType("electrifiedHouse", electrifiedHouseMap);
		lifeOrHealthInsuranceMap = formHousingType("lifeOrHealthInsurance", lifeOrHealthInsuranceMap);
		cropInsuranceMap = formHousingType("cropInsurance", cropInsuranceMap);
		// Farmer Loan Detail Fields.
		isLoanTakenLastYear = formHousingType(getText("loanStatus"), isLoanTakenLastYear);
		// loanTakenFromMap = formHousingType("loanTakenFrom",
		// loanTakenFromMap);
		toiletAvailableFromMap = formHousingType("ifToiletAvailable", toiletAvailableFromMap);
		beneficiaryInGovSchemelist = formBeneficiary("bgs", beneficiaryInGovSchemelist);
		processingActList.put(1, getText("processAct-1"));
		processingActList.put(2, getText("processAct-2"));
		/*
		 * if (getCurrentTenantId().equalsIgnoreCase("atma")) { if
		 * (!StringUtil.isEmpty(getSelectedSangham())) { if
		 * (getSelectedSangham().equalsIgnoreCase("01")) {
		 * sourceNameMap.put(Farmer.AGRICULTURE_ACTIVITIES,
		 * getText("Agriculture Activities"));
		 * sourceNameMap.put(Farmer.HORTICULTURE, getText("Horticulture")); }
		 * sourceNameMap.put(Farmer.ALLIED_SECTOR, getText("Allied Sector"));
		 * sourceNameMap.put(Farmer.EMPLOYMENT, getText("Employment"));
		 * sourceNameMap.put(Farmer.OTHERS,
		 * getText("Income from Social Benefits")); } else {
		 * sourceNameMap.put(Farmer.AGRICULTURE_ACTIVITIES,
		 * getText("Agriculture Activities"));
		 * sourceNameMap.put(Farmer.HORTICULTURE, getText("Horticulture"));
		 * sourceNameMap.put(Farmer.ALLIED_SECTOR, getText("Allied Sector"));
		 * sourceNameMap.put(Farmer.EMPLOYMENT, getText("Employment"));
		 * sourceNameMap.put(Farmer.OTHERS,
		 * getText("Income from Social Benefits")); }
		 * 
		 * aliedSectorMap = formHousingType("aliedSector", aliedSectorMap);
		 * employmentMap = formHousingType("employment", employmentMap);
		 * otherIncomeMap = formHousingType("otherIncome", otherIncomeMap); }
		 */
		/*
		 * if(!StringUtil.isEmpty(regYearString)){
		 * farmer.setYearOfICS(regYearString);
		 * 
		 * }
		 */
		if (!StringUtil.isEmpty(dynamicFieldsArray) && dynamicFieldsArray.length() > 0) {
			farmer.setValidateDynamicFields(dynamicFieldsArray);
		}

		return farmer;
	}

	public String setDynamicFarmerFields(String farmerDynamicDatas2, String name) {
		// int dynamic=0;
		String result = "";

		Type listType1 = new TypeToken<List<DynamicData>>() {
		}.getType();
		List<DynamicData> dynamicList = new Gson().fromJson(dynamicFieldsArray, listType1);

		for (DynamicData dynamicData : dynamicList) {
			if (dynamicData.getName().equalsIgnoreCase(name)) {

				result = dynamicData.getValue();
			}

		}

		return result;

	}

	/**
	 * Form enrollment place map.
	 * 
	 * @param keyProperty
	 *            the key property
	 * @param enrollmentMap
	 *            the enrollment map
	 * @return the map< integer, string>
	 */
	private Map<Integer, String> formEnrollmentPlaceMap(String keyProperty, Map<Integer, String> enrollmentMap) {

		enrollmentMap = getPropertyData(keyProperty);
		return enrollmentMap;
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

	/*
	 * private Map<Integer, String> formMaritialMap(String keyProperty,
	 * Map<Integer, String> enrollmentMap) { maritalSatuses = new
	 * LinkedHashMap(); String values = getText(keyProperty); if
	 * (!StringUtil.isEmpty(values)) { String[] valuesArray = values.split(",");
	 * int i = 0; // Arrays.sort(valuesArray); for (String value : valuesArray)
	 * { maritalSatuses.put(i++, value); } } return maritalSatuses; }
	 */

	private Map<Integer, String> formBeneficiary(String keyProperty, Map<Integer, String> beneficiaryInGovSchemelist) {

		beneficiaryInGovSchemelist = new LinkedHashMap();
		String values = getText(keyProperty);
		if (!StringUtil.isEmpty(values)) {
			String[] valuesArray = values.split(",");
			int i = 2;
			for (String value : valuesArray) {
				beneficiaryInGovSchemelist.put(i--, value);
			}
		}
		return beneficiaryInGovSchemelist;
	}

	private Map<Integer, String> formHousingOwnerShip(String keyProperty, Map<Integer, String> housingOwnerships) {

		housingOwnerships = new LinkedHashMap();
		String values = getText(keyProperty);
		if (!StringUtil.isEmpty(values)) {
			String[] valuesArray = values.split(",");
			int i = 1;
			for (String value : valuesArray) {
				housingOwnerships.put(i++, value);
			}
		}
		return housingOwnerships;
	}

	/**
	 * Form map.
	 * 
	 * @param keyProperty
	 *            the key property
	 * @param dataMap
	 *            the data map
	 * @return the map
	 */
	@SuppressWarnings("unchecked")
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

	/**
	 * Gets the certification types by farmer.
	 * 
	 * @param farmer
	 *            the farmer
	 * @return the certification types by farmer
	 */
	public Map<Integer, String> getCertificationTypesByFarmer(Farmer farmer) {

		Map<Integer, String> map = new LinkedHashMap<Integer, String>(certificationTypes);
		try {
			if (!ObjectUtil.isEmpty(farmer))
				farmer = farmerService.findFarmerById(farmer.getId());
			if (!ObjectUtil.isEmpty(farmer) && certificationTypes.size() > 0) {
				// When farmer certification type is NONE {0}, then by default
				// loads all.
				// Else load that single certification type
				if (farmer.getCertificationType() != 0) {
					map.clear();
					map.put(new Integer(farmer.getCertificationType()),
							certificationTypes.get(farmer.getCertificationType()));
				}
			}
		} catch (Exception e) {
			map = new LinkedHashMap<Integer, String>(certificationTypes);
			e.printStackTrace();
		}
		return map;
	}

	private Map<Integer, String> formSourceNameMap(String keyProperty, Map<Integer, String> sourceNameMap) {

		sourceNameMap = getPropertyData(keyProperty);
		return sourceNameMap;
	}

	/**
	 * Gets the property value.
	 * 
	 * @param propertyKey
	 *            the property key
	 * @param index
	 *            the index
	 * @return the property value
	 */
	public Object getPropertyValue(String propertyKey, int index) {

		String values = getText(propertyKey);
		Object returnObj = "";
		if (!StringUtil.isEmpty(values) && index >= 0) {
			String[] valuesArray = values.split(",");
			if (valuesArray.length > index) {
				returnObj = valuesArray[index];
			}
		}
		return returnObj;
	}

	private Map<Integer, String> formCategory(String keyProperty, Map<Integer, String> categoryList) {

		categoryList = new LinkedHashMap();
		String values = getText(keyProperty);
		if (!StringUtil.isEmpty(values)) {
			String[] valuesArray = values.split(",");
			int i = 1;
			for (String value : valuesArray) {
				categoryList.put(i++, value);
			}
		}
		return categoryList;
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

		return tabIndex;
	}

	/**
	 * Sets the farmer image byte string.
	 * 
	 * @param farmerImageByteString
	 *            the new farmer image byte string
	 */
	public void setFarmerImageByteString(String farmerImageByteString) {

		this.farmerImageByteString = farmerImageByteString;
	}

	/**
	 * Gets the farmer image byte string.
	 * 
	 * @return the farmer image byte string
	 */
	public String getFarmerImageByteString() {

		return farmerImageByteString;
	}

	/**
	 * Sets the selected city.
	 * 
	 * @param selectedCity
	 *            the new selected city
	 */
	public void setSelectedCity(String selectedCity) {

		this.selectedCity = selectedCity;
	}

	/**
	 * Gets the selected city.
	 * 
	 * @return the selected city
	 */
	public String getSelectedCity() {

		return selectedCity;
	}

	/**
	 * Gets the villages.
	 * 
	 * @return the villages
	 */
	public Map<Long, String> getVillages() {
		Map<Long, String> villageMap = new LinkedHashMap<Long, String>();
		if (!StringUtil.isEmpty(selectedCity)) {
			villages = locationService.listVillagesByCityId(Long.valueOf(selectedCity));
		}
		if (!ObjectUtil.isEmpty(villages)) {
			for (Village village : villages) {
				villageMap.put(village.getId(), village.getCode() + "-" + village.getName());
			}
		}
		return villageMap;
	}

	/**
	 * Sets the villages.
	 * 
	 * @param villages
	 *            the new villages
	 */
	public void setVillages(List<Village> villages) {

		this.villages = villages;
	}

	/**
	 * Gets the id generator.
	 * 
	 * @return the id generator
	 */
	public IUniqueIDGenerator getIdGenerator() {

		return idGenerator;
	}

	/**
	 * Sets the id generator.
	 * 
	 * @param idGenerator
	 *            the new id generator
	 */
	public void setIdGenerator(IUniqueIDGenerator idGenerator) {

		this.idGenerator = idGenerator;
	}

	/**
	 * Gets the card rewrite list.
	 * 
	 * @return the card rewrite list
	 */
	public Map<Integer, String> getCardRewriteList() {

		return cardRewriteList;
	}

	/**
	 * Sets the account.
	 * 
	 * @param account
	 *            the new account
	 */
	public void setAccount(ESEAccount account) {

		this.account = account;
	}

	/**
	 * Gets the account.
	 * 
	 * @return the account
	 */
	public ESEAccount getAccount() {

		return account;
	}

	/**
	 * Sets the card rewrite list.
	 * 
	 * @param cardRewriteList
	 *            the card rewrite list
	 */
	public void setCardRewriteList(Map<Integer, String> cardRewriteList) {

		this.cardRewriteList = cardRewriteList;
	}

	/**
	 * Gets the card status list.
	 * 
	 * @return the card status list
	 */
	public Map<Integer, String> getCardStatusList() {

		return cardStatusList;
	}

	/**
	 * Sets the card status list.
	 * 
	 * @param cardStatusList
	 *            the card status list
	 */
	public void setCardStatusList(Map<Integer, String> cardStatusList) {

		this.cardStatusList = cardStatusList;
	}

	/**
	 * Gets the card service.
	 * 
	 * @return the card service
	 */
	public ICardService getCardService() {

		return cardService;
	}

	/**
	 * Sets the card service.
	 * 
	 * @param cardService
	 *            the new card service
	 */
	public void setCardService(ICardService cardService) {

		this.cardService = cardService;
	}

	/**
	 * Sets the card.
	 * 
	 * @param card
	 *            the new card
	 */
	public void setCard(ESECard card) {

		this.card = card;
	}

	/**
	 * Gets the card.
	 * 
	 * @return the card
	 */
	public ESECard getCard() {

		return card;
	}

	/**
	 * Sets the txn service.
	 * 
	 * @param txnService
	 *            the new txn service
	 */
	public void setTxnService(IESETxnService txnService) {

		this.txnService = txnService;
	}

	/**
	 * Gets the txn service.
	 * 
	 * @return the txn service
	 */
	public IESETxnService getTxnService() {

		return txnService;
	}

	/**
	 * Gets the farmer status.
	 * 
	 * @return the farmer status
	 */
	public Map<Integer, String> getFarmerStatus() {

		return farmerStatus;
	}

	/**
	 * Sets the farmer status.
	 * 
	 * @param farmerStatus
	 *            the farmer status
	 */
	public void setFarmerStatus(Map<Integer, String> farmerStatus) {

		this.farmerStatus = farmerStatus;
	}

	/**
	 * Sets the farmer and contract status.
	 * 
	 * @param farmerAndContractStatus
	 *            the new farmer and contract status
	 */
	public void setFarmerAndContractStatus(boolean farmerAndContractStatus) {

		this.farmerAndContractStatus = farmerAndContractStatus;
	}

	/**
	 * Checks if is farmer and contract status.
	 * 
	 * @return true, if is farmer and contract status
	 */
	public boolean isFarmerAndContractStatus() {

		return farmerAndContractStatus;
	}

	/**
	 * Gets the prefernces service.
	 * 
	 * @return the prefernces service
	 */
	public IPreferencesService getPreferncesService() {

		return preferncesService;
	}

	/**
	 * Sets the prefernces service.
	 * 
	 * @param preferncesService
	 *            the new prefernces service
	 */
	public void setPreferncesService(IPreferencesService preferncesService) {

		this.preferncesService = preferncesService;
	}

	/**
	 * Sets the date of join.
	 * 
	 * @param dateOfJoin
	 *            the new date of join
	 */

	/**
	 * Sets the selected panchayat.
	 * 
	 * @param selectedPanchayat
	 *            the new selected panchayat
	 */
	public void setSelectedPanchayat(String selectedPanchayat) {

		this.selectedPanchayat = selectedPanchayat;
	}

	public String getDateOfJoining() {

		return dateOfJoining;
	}

	public void setDateOfJoining(String dateOfJoining) {

		this.dateOfJoining = dateOfJoining;
	}

	/**
	 * Gets the selected panchayat.
	 * 
	 * @return the selected panchayat
	 */
	public String getSelectedPanchayat() {

		return selectedPanchayat;
	}

	/**
	 * Gets the panchayat.
	 * 
	 * @return the panchayat
	 */
	/*
	 * public List<GramPanchayat> getPanchayat() {
	 * 
	 * return panchayat; }
	 */

	/**
	 * Sets the panchayat.
	 * 
	 * @param panchayat
	 *            the new panchayat
	 */
	public void setPanchayat(List<GramPanchayat> panchayat) {

		this.panchayat = panchayat;
	}

	/**
	 * Gets the panchayath.
	 * 
	 * @return the panchayath
	 */
	public Map<Long, String> getPanchayath() {
		Map<Long, String> gramPanchayatMap = new LinkedHashMap<Long, String>();
		if (!StringUtil.isEmpty(selectedCity)) {
			panchayat = locationService.listGramPanchayatsByCityId(Long.valueOf(selectedCity));
		}
		if (!ObjectUtil.isEmpty(panchayat)) {
			for (GramPanchayat gramPanchayat : panchayat) {
				gramPanchayatMap.put(gramPanchayat.getId(), gramPanchayat.getCode() + "-" + gramPanchayat.getName());
			}
		}
		return gramPanchayatMap;
	}

	/**
	 * Populate panchayath.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public void populatePanchayath() throws Exception {

		if (!selectedCity.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedCity))) {
			panchayat = locationService.listGramPanchayatsByCityId(Long.valueOf(selectedCity));
		}
		JSONArray panchayathArr = new JSONArray();
		if (!ObjectUtil.isListEmpty(panchayat)) {
			for (GramPanchayat gramPanchayath : panchayat) {
				// panchayathArr.add(getJSONObject(gramPanchayath.getId(),gramPanchayath.getCode()
				// + "-" + gramPanchayath.getName()));
				String name = getLanguagePref(getLoggedInUserLanguage(), gramPanchayath.getCode().trim().toString());
				if (!StringUtil.isEmpty(name) && name != null) {
					panchayathArr.add(getJSONObject(String.valueOf(gramPanchayath.getId()),
							gramPanchayath.getCode().toString() + "-"
									+ getLanguagePref(getLoggedInUserLanguage(), gramPanchayath.getCode().toString())));
				} else {
					panchayathArr.add(getJSONObject(gramPanchayath.getId(),
							gramPanchayath.getCode() + "-" + gramPanchayath.getName()));
				}
			}
		}
		sendAjaxResponse(panchayathArr);
	}

	/**
	 * Gets the samithi.
	 * 
	 * @return the samithi
	 */
	public Map<Long, String> getSamithi() {

		samithi = locationService.listSamithiesBasedOnType();
		Map<Long, String> samithiMap = new LinkedHashMap<>();
		samithiMap = samithi.stream().collect(Collectors.toMap(Warehouse::getId, Warehouse::getName));
		return samithiMap;
	}

	/*
	 * Populate samithi. up
	 * 
	 * @return the string
	 * 
	 * @throws Exception the exception
	 */
	@SuppressWarnings("unchecked")
	public void populateSamithi() throws Exception {

		/*
		 * if (!selectedVillage.equalsIgnoreCase("null") &&
		 * (!StringUtil.isEmpty(selectedVillage))) { Village village =
		 * locationService.findVillageById(Long.valueOf(selectedVillage)); if
		 * (!ObjectUtil.isEmpty(village)) samithi =
		 * locationService.listSamithiByVillageId(village.getId()); JSONArray
		 * warehouseArr = null; if (!ObjectUtil.isEmpty(samithi)) { warehouseArr
		 * = new JSONArray(); for (Warehouse warehouse : samithi) {
		 * warehouseArr.add(getJSONObject(warehouse.getId(), warehouse.getCode()
		 * + "-" + warehouse.getName())); } } sendAjaxResponse(warehouseArr); }
		 */

		samithi = locationService.listSamithiesBasedOnType();
		JSONArray warehouseArr = new JSONArray();
		;
		samithi.stream().filter(samithi -> !ObjectUtil.isEmpty(samithi)).forEach(samithi -> {
			warehouseArr.add(getJSONObject(samithi.getId(), samithi.getName()));
		});
		sendAjaxResponse(warehouseArr);
	}

	public Map<String, String> getCooperative() {

		AtomicInteger i = new AtomicInteger(0);
		Map<String, String> cooperativeList = new LinkedHashMap<>();
		cooperativeList = getFarmCatalougeMap(Integer.valueOf(getText("cooperativeType")));
		return cooperativeList;

	}

	public Map<String, String> getCategoryList() {
		Map<String, String> categoryList = new LinkedHashMap<>();
		categoryList = getFarmCatalougeMap(FarmCatalogue.CATEGORY);
		return categoryList;
	}

	public Map<String, String> getHousingTypeList() {
		Map<String, String> housingTypeList = new LinkedHashMap<>();
		housingTypeList = getFarmCatalougeMap(Integer.valueOf(getText("housingTypez")));
		return housingTypeList;
	}

	public Map<String, String> getLoanTakenFromList() {
		Map<String, String> loanTakenFromList = new LinkedHashMap<>();
		loanTakenFromList = getFarmCatalougeMap(Integer.valueOf(getText("loanTaken")));

		return loanTakenFromList;
	}

	/**
	 * Sets the selected village.
	 * 
	 * @param selectedVillage
	 *            the new selected village
	 */
	public void setSelectedVillage(String selectedVillage) {

		this.selectedVillage = selectedVillage;
	}

	/**
	 * Gets the selected village.
	 * 
	 * @return the selected village
	 */
	public String getSelectedVillage() {

		return selectedVillage;
	}

	public String getSelectedSamithi() {

		return selectedSamithi;
	}

	public void setSelectedSamithi(String selectedSamithi) {

		this.selectedSamithi = selectedSamithi;
	}

	/**
	 * Sets the samithi.
	 * 
	 * @param samithi
	 *            the new samithi
	 */
	public void setSamithi(List<Warehouse> samithi) {

		this.samithi = samithi;
	}

	/**
	 * Gets the current date.
	 * 
	 * @return the current date
	 */
	public String getCurrentDate() {

		Calendar currentDate = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT_1);
		return df.format(currentDate.getTime());

	}

	/**
	 * Populate xls.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String populateKML() throws Exception {
		response.setContentType("application/vnd.google-earth.kmz");
		response.setHeader("Content-Disposition",
				"attachment;filename=" + getText("farmerList") + fileNameDateFormat.format(new Date()) + ".kmz");
		final String fileName = getLocaleProperty("farmerList") + fileNameDateFormat.format(new Date()) + ".kml";
		ZipEntry entry = new ZipEntry(fileName);
		ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
		zipOutputStream.putNextEntry(entry);
		byte[] stram = IOUtils.toByteArray(getFarmerKMLFile());
		zipOutputStream.write(stram);
		zipOutputStream.closeEntry();
		zipOutputStream.close();
		return null;

	}

	@SuppressWarnings("unchecked")
	private InputStream getFarmerKMLFile()
			throws IOException, ParserConfigurationException, TransformerException, Exception {
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Map<String, String> searchRecord = getJQGridRequestParam();
		Farmer filter = new Farmer();

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

		if (!StringUtil.isEmpty(searchRecord.get("farmerId"))) {
			filter.setFarmerId(searchRecord.get("farmerId").trim());
		}
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
			if (StringUtil.isEmpty(getBranchId()) || getBranchId().equalsIgnoreCase("organic")) {
				if (!StringUtil.isEmpty(searchRecord.get("farmersCodeTracenet"))) {
					filter.setFarmersCodeTracenet(searchRecord.get("farmersCodeTracenet").trim());
				}

			}
		} else {
			if (!StringUtil.isEmpty(searchRecord.get("farmerCode"))) {
				filter.setFarmerCode(searchRecord.get("farmerCode").trim());
			}
		}

		if (!StringUtil.isEmpty(searchRecord.get("firstName")))
			filter.setFirstName(searchRecord.get("firstName").trim());

		if (!StringUtil.isEmpty(searchRecord.get("lastName")))
			filter.setLastName(searchRecord.get("lastName").trim());

		if (!StringUtil.isEmpty(searchRecord.get("surName")))
			filter.setSurName(searchRecord.get("surName").trim());

		if (!StringUtil.isEmpty(searchRecord.get("v.name"))) {
			Village village = new Village();
			village.setName(searchRecord.get("v.name").trim());
			filter.setVillage(village);
		}
		if (!StringUtil.isEmpty(searchRecord.get("s.name"))) {
			Warehouse samithi = new Warehouse();
			samithi.setName(searchRecord.get("s.name").trim());
			filter.setSamithi(samithi);
		}

		if (!StringUtil.isEmpty(searchRecord.get("cs.name"))) {
			CertificateStandard certificateStandard = new CertificateStandard();
			certificateStandard.setName(searchRecord.get("cs.name").trim());
			filter.setCertificateStandard(certificateStandard);
		}

		if (!StringUtil.isEmpty(searchRecord.get("isCertifiedFarmer"))) {
			filter.setCertificationFilter(searchRecord.get("isCertifiedFarmer"));
		}
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.AGRO_TENANT)) {
			if (!StringUtil.isEmpty(searchRecord.get("farmSize"))) {
				if ("1".equals(searchRecord.get("farmSize"))) {
				filter.setFilterStatus("farmSize");
				filter.setFarmSize(Farmer.FARM_SIZE_GREATER_THAN_ONE);
				} else if("2".equals(searchRecord.get("farmSize"))){
				filter.setFilterStatus("farmSize");
				filter.setFarmSize(Farmer.FARM_SIZE_LESS_THAN_ONE);
				} else if("3".equals(searchRecord.get("farmSize"))){
				filter.setFilterStatus("farmSize");
				filter.setFarmSize(Farmer.NO_FARM);
				}
				}
				/*else{
				filter.setFarmSize(-1);
				}*/
				}
		if (!StringUtil.isEmpty(searchRecord.get("status"))) {
			if ("1".equals(searchRecord.get("status"))) {
				filter.setFilterStatus("status");
				filter.setStatus(Farmer.Status.ACTIVE.ordinal());
			} else {
				filter.setFilterStatus("status");
				filter.setStatus(Farmer.Status.INACTIVE.ordinal());
			}
		}
		if (!StringUtil.isEmpty(searchRecord.get("mobileNumber"))) {
			filter.setMobileNumber(searchRecord.get("mobileNumber").trim());
		}
		if (!StringUtil.isEmpty(searchRecord.get("createdUsername")))
			filter.setCreatedUsername(searchRecord.get("createdUsername").trim());

		if (!StringUtil.isEmpty(searchRecord.get("createdDate"))) {
			DateFormat dff = new SimpleDateFormat("dd-MM-yyyy");
			filter.setCreatedDate(dff.parse(searchRecord.get("createdDate")));
		}

		if (!StringUtil.isEmpty(searchRecord.get("masterData"))) {
			filter.setMasterData(searchRecord.get("masterData"));
		}
		filter.setSearchPage(searchPage);
		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());
		List<Farmer> frmr = (List<Farmer>) data.get(ROWS);
		Map<String, Map<String, List<Object[]>>> farmerMap = new LinkedHashMap<String, Map<String, List<Object[]>>>();

		frmr.stream().filter(fa -> fa.getFarms() != null && !ObjectUtil.isListEmpty(fa.getFarms())).forEach(f -> {
			Map<String, List<Object[]>> farmMap = new LinkedHashMap<>();
			f.getFarms().stream()
					.filter(fr -> fr != null && fr.getActiveCoordinates() != null
							&& fr.getActiveCoordinates().getFarmCoordinates() != null
							&& !ObjectUtil.isListEmpty(fr.getActiveCoordinates().getFarmCoordinates()))
					.forEach(frm -> {
						List<Object[]> objArr = new LinkedList<>();
						frm.getActiveCoordinates().getFarmCoordinates().stream()
								.filter(fr -> fr != null && !ObjectUtil.isEmpty(fr)).forEach(co -> {
									Object[] obj = new Object[2];
									obj[0] = co.getLatitude();
									obj[1] = co.getLongitude();
									objArr.add(obj);
								});
						String placeMark = frm.getFarmCode() + "," + frm.getFarmName() + ","
								+ (frm.getFarmDetailedInfo() != null
										&& frm.getFarmDetailedInfo().getTotalLandHolding() != null
										&& !StringUtil.isEmpty(frm.getFarmDetailedInfo().getTotalLandHolding())
												? frm.getFarmDetailedInfo().getTotalLandHolding() : "0.0")+","+(frm.getLatitude()!=null&&frm.getLatitude()!="0"&&frm.getLongitude()!=null&&frm.getLongitude()!="0"?frm.getLatitude()+"-"+frm.getLongitude():"");
						farmMap.put(placeMark, objArr);
					});
			String farmerData = f.getFirstName() + " "
					+ (f.getLastName() != null && !StringUtil.isEmpty(f.getLastName()) ? f.getLastName() : "") + ","
					+ f.getFarmerId() + "," + f.getVillage().getName();
			farmerMap.put(farmerData, farmMap);
		});

		org.w3c.dom.Document doc = KMLUtil.downloadKML(farmerMap);
		TransformerFactory tranFactory = TransformerFactory.newInstance();
		Transformer aTransformer = tranFactory.newTransformer();
		String makeDir = FileUtil.storeXls(request.getSession().getId());
		String fName = getText("farmerList") + fileNameDateFormat.format(new Date()) + ".kml";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fName);
		Source src = new DOMSource(doc);
		Result dest = new StreamResult(fileOut);
		aTransformer.transform(src, dest);
		InputStream stream = new FileInputStream(new File(makeDir + fName));
		fileOut.close();
		return stream;
	}

	public String populateXLS() throws Exception {
		response.setContentType("application/vnd.ms-excel");
		// response.setHeader("Content-Disposition",
		// "attachment;filename=" + getText("farmerList") +
		// fileNameDateFormat.format(new Date()) + ".xls");
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)) {
		farmsCount = farmerService.listFarmscountInfo();
		 farmsizepMap = farmsCount.stream().collect(Collectors.toMap(a -> a[0].toString(), a -> String.valueOf( a[1].toString())));
		}
		 InputStream is = null;
		
		/*
		 * if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PGSS_TENANT_ID)
		 * && !StringUtil.isEmpty(getType()) && getType().equals(Farmer.IRP)) {
		 * is = getFarmerInputStream(); } else { is =
		 * getFarmerFileInputStream(); }
		 */
		is = getFarmerFileInputStream();
		setXlsFileName(getText("farmerList") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getLocaleProperty("farmerList"), fileMap, ".xls"));

		return "xls";
	}

	StringBuffer headerCols = new StringBuffer();
	List<String> dynamicExportFieldList = new ArrayList<>();
	String builder = new String();

	/*
	 * private InputStream getFarmerInputStream() throws IOException,
	 * ParseException { // headerCols.append(getLocaleProperty(""));
	 * 
	 * HSSFWorkbook wb = new HSSFWorkbook(); HSSFSheet sheet =
	 * wb.createSheet(getText("exportXLSIRPTitle")); HSSFPatriarch drawing =
	 * sheet.createDrawingPatriarch();
	 * 
	 * HSSFCellStyle style1 = wb.createCellStyle(); HSSFCellStyle style2 =
	 * wb.createCellStyle();
	 * 
	 * HSSFFont font1 = wb.createFont(); font1.setFontHeightInPoints((short)
	 * 22);
	 * 
	 * HSSFFont font2 = wb.createFont(); font2.setFontHeightInPoints((short)
	 * 12);
	 * 
	 * HSSFFont font3 = wb.createFont(); font3.setFontHeightInPoints((short)
	 * 10);
	 * 
	 * titleRow = sheet.createRow(rowNum++); cell = titleRow.createCell(4);
	 * cell.setCellValue(new HSSFRichTextString(getText("exportXLSIRPTitle")));
	 * cell.setCellStyle(style1); font1.setBoldweight((short) 22);
	 * font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); style1.setFont(font1);
	 * 
	 * titleRow = sheet.createRow(rowNum++);
	 * 
	 * HSSFRow mainGridRowHead = sheet.createRow(rowNum++); int mainGridIterator
	 * = 0; selectedCatalogue = ""; getFarmCatalougeMap(104).forEach((k, v) -> {
	 * selectedCatalogue += "Disability Type" + ","; selectedCatalogue +=
	 * ("Origin,Remarks,Status of professional consultation,Detail of consultation"
	 * ) + ","; }); builder =
	 * getLocaleProperty("exportColumnHeaderIRP").replace("#health_asses#,",
	 * selectedCatalogue); selectedCatalogue = "";
	 * getFarmCatalougeMap(108).forEach((k, v) -> { selectedCatalogue +=
	 * "Activity Type" + ","; selectedCatalogue += ("Value,Remarks") + ","; });
	 * builder = builder.replace("#self_asses#,", selectedCatalogue); builder =
	 * builder.substring(0, builder.length() - 1);
	 * 
	 * // selectedCatalogue=""; headerCols.append(builder);
	 * 
	 * dynamicFields().stream().forEach(dynamicSectionConfig -> {
	 * dynamicSectionConfig.getDynamicFieldConfigs().stream().forEach(
	 * dynamicFieldConfig -> { headerCols.append("," +
	 * dynamicFieldConfig.getComponentName());
	 * dynamicExportFieldList.add(dynamicFieldConfig.getComponentName()); });
	 * });
	 * 
	 * for (String cellHeader : headerCols.toString().split("\\,")) {
	 * 
	 * if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
	 * cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT,
	 * getCurrencyType()); } else if
	 * (cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT)) { cellHeader =
	 * cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType()); }
	 * 
	 * cell = mainGridRowHead.createCell(mainGridIterator);
	 * cell.setCellValue(new HSSFRichTextString(cellHeader));
	 * style2.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
	 * style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	 * cell.setCellStyle(style2); font2.setBoldweight((short) 12);
	 * font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); style2.setFont(font2);
	 * sheet.setColumnWidth(mainGridIterator, (15 * 550)); mainGridIterator++; }
	 * 
	 * List<Object[]> farmersList = getAllExportRecords();
	 * farmersList.stream().forEach(farmer -> { row = sheet.createRow(rowNum++);
	 * row.setHeight((short) 600); colNum = 0; Object[] farmerData = (Object[])
	 * farmer; formFarmerExportCols(farmerData).stream().forEach(data -> { cell
	 * = row.createCell(colNum++); cell.setCellValue(String.valueOf(data)); });
	 * });
	 * 
	 * for (int i = 0; i <= colNum; i++) { sheet.autoSizeColumn(i); }
	 * 
	 * int pictureIdx = getPicIndex(wb); HSSFClientAnchor anchor = new
	 * HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
	 * anchor.setAnchorType(1); HSSFPicture picture =
	 * drawing.createPicture(anchor, pictureIdx); // picture.resize(); String id
	 * = ObjectUtil.isEmpty(request) ?
	 * String.valueOf(DateUtil.getRevisionNumber()) :
	 * request.getSession().getId(); String makeDir = FileUtil.storeXls(id);
	 * String fileName = getLocaleProperty("farmerList") +
	 * fileNameDateFormat.format(new Date()) + ".xls"; FileOutputStream fileOut
	 * = new FileOutputStream(makeDir + fileName); wb.write(fileOut);
	 * InputStream stream = new FileInputStream(new File(makeDir + fileName));
	 * fileOut.close();
	 * 
	 * return stream; }
	 */

	Boolean flag = false;

	private List<Object> formFarmerExportCols(Object[] farmer) {

		List<Object> farmerExportCols = new ArrayList<>();
		farmerExportCols.add(StringUtil.isEmpty(farmer[12]) ? getText("NA") : getText("et" + farmer[12].toString()));

		farmerExportCols.add(StringUtil.isEmpty(farmer[13]) ? getText("NA") : farmer[13].toString());

		farmerExportCols.add(StringUtil.isEmpty(farmer[14]) ? getText("NA") : farmer[14].toString());

		farmerExportCols.add(StringUtil.isEmpty(farmer[15]) ? getText("NA") : farmer[15].toString());

		farmerExportCols.add(StringUtil.isEmpty(farmer[16]) ? getText("NA") : farmer[16].toString());

		FarmCatalogue catalogue = new FarmCatalogue();

		farmerExportCols.add(StringUtil.isEmpty(getCatlogueValueByCode(String.valueOf(farmer[17])))
				|| StringUtil.isEmpty(getCatlogueValueByCode(String.valueOf(farmer[17])).getName()) ? getText("NA")
						: getCatlogueValueByCode(String.valueOf(farmer[17])).getName()); // placeAssessment

		farmerExportCols
				.add(StringUtil.isEmpty(farmer[18]) || farmer[18].equals("-1") ? getText("NA") : farmer[18].toString());// year
		// of
		// ics

		farmerExportCols.add(StringUtil.isEmpty(farmer[3]) ? getText("NA") : farmer[3].toString());

		farmerExportCols.add(StringUtil.isEmpty(farmer[4]) ? getText("NA") : StringUtil.trim(farmer[4].toString()));

		farmerExportCols.add(StringUtil.isEmpty(farmer[5]) ? getText("NA") : StringUtil.trim(farmer[5].toString()));

		farmerExportCols.add(StringUtil.isEmpty(farmer[19]) ? getText("NA") : StringUtil.trim(farmer[19].toString()));// gender

		farmerExportCols.add(StringUtil.isEmpty(farmer[20]) ? getText("NA") : StringUtil.trim(farmer[20].toString()));// age

		farmerExportCols.add(StringUtil.isEmpty(getCatlogueValueByCode(String.valueOf(farmer[21])))
				|| StringUtil.isEmpty(getCatlogueValueByCode(String.valueOf(farmer[21])).getName()) ? getText("NA")
						: getCatlogueValueByCode(String.valueOf(farmer[21])).getName());// education

		farmerExportCols.add(StringUtil.isEmpty(getCatlogueValueByCode(String.valueOf(farmer[22])))
				|| StringUtil.isEmpty(getCatlogueValueByCode(String.valueOf(farmer[22])).getName()) ? getText("NA")
						: getCatlogueValueByCode(String.valueOf(farmer[22])).getName());// mar
																						// status

		farmerExportCols.add(StringUtil.isEmpty(farmer[23]) ? getText("NA") : StringUtil.trim(farmer[23].toString()));// caste

		farmerExportCols.add(StringUtil.isEmpty(farmer[25]) ? getText("NA") : StringUtil.trim(farmer[25].toString()));// farmer
																														// address

		farmerExportCols.add(StringUtil.isEmpty(farmer[26]) ? getText("NA") : StringUtil.trim(farmer[26].toString()));// country

		farmerExportCols.add(StringUtil.isEmpty(farmer[27]) ? getText("NA") : StringUtil.trim(farmer[27].toString()));// state

		farmerExportCols.add(StringUtil.isEmpty(farmer[28]) ? getText("NA") : StringUtil.trim(farmer[28].toString()));// loca

		farmerExportCols.add(StringUtil.isEmpty(farmer[29]) ? getText("NA") : StringUtil.trim(farmer[29].toString()));// city

		farmerExportCols.add(StringUtil.isEmpty(farmer[30]) ? getText("NA") : StringUtil.trim(farmer[30].toString()));// gp

		farmerExportCols.add(StringUtil.isEmpty(farmer[6]) ? getText("NA") : StringUtil.trim(farmer[6].toString()));// village

		farmerExportCols.add(StringUtil.isEmpty(farmer[7]) ? getText("NA") : StringUtil.trim(farmer[7].toString())); // samithi

		farmerExportCols
				.add(StringUtil.isEmpty(farmer[31]) ? getText("NA") : getText("loanStatus" + farmer[31].toString()));// loanTakenLastYear

		farmerExportCols.add(ObjectUtil.isEmpty(getCatlogueValueByCode(String.valueOf(farmer[32])))
				|| StringUtil.isEmpty(getCatlogueValueByCode(String.valueOf(farmer[32])).getName()) ? getText("NA")
						: getCatlogueValueByCode(String.valueOf(farmer[32])).getName());// loanTakenFrom

		farmerExportCols.add(StringUtil.isEmpty(farmer[33]) ? getText("NA") : StringUtil.trim(farmer[33].toString()));// loan
																														// amt

		farmerExportCols.add(ObjectUtil.isEmpty(getCatlogueValueByCode(String.valueOf(farmer[34])))
				|| StringUtil.isEmpty(getCatlogueValueByCode(String.valueOf(farmer[34])).getName()) ? getText("NA")
						: getCatlogueValueByCode(String.valueOf(farmer[34])).getName());// loanPupose

		farmerExportCols.add(StringUtil.isEmpty(farmer[35]) ? getText("NA") : StringUtil.trim(farmer[35].toString()));// loan
																														// repay

		if (!StringUtil.isEmpty(farmer[36])) {
			farmerExportCols.add(farmer[36].equals("1") ? "YES" : "NO");// isLoanTakenScheme
		} else {
			farmerExportCols.add("NA");
		}

		farmerExportCols.add(StringUtil.isEmpty(farmer[37]) ? getText("NA") : StringUtil.trim(farmer[37].toString()));// LoanTakenScheme

		if (!StringUtil.isEmpty(farmer[38])) {
			farmerExportCols.add(farmer[38] != "0" ? getText("yes") : getText("no"));// ass
																						// device
		} else {
			farmerExportCols.add("NA");
		}

		farmerExportCols.add(StringUtil.isEmpty(farmer[39]) ? getText("NA") : StringUtil.trim(farmer[39].toString()));

		if (!StringUtil.isEmpty(farmer[40])) {
			farmerExportCols.add(farmer[40] != "0" ? getText("yes") : getText("no"));
		} else {
			farmerExportCols.add("NA");
		}

		farmerExportCols.add(StringUtil.isEmpty(farmer[41]) ? getText("NA") : StringUtil.trim(farmer[41].toString()));// healthIssueDescribe

		if (!StringUtil.isEmpty(String.valueOf(farmer[42]))) {
			String RecArr[] = farmer[42].toString().split(",");
			String recValue = "";
			for (int i = 0; i < RecArr.length; i++) {
				String recTrim = RecArr[i].replaceAll("\\s+", "");
				// catalogue = catalogueService.findCatalogueByCode(recTrim);
				recValue += getCatlogueValueByCode(recTrim).getName() + ",";
			}
			recValue = recValue.substring(0, recValue.length() - 1);
			farmerExportCols.add(!ObjectUtil.isEmpty(recValue) ? recValue : "NA");

		} else {
			farmerExportCols.add("");
		}

		if (!StringUtil.isEmpty(String.valueOf(farmer[43]))) {
			String RecArr[] = farmer[43].toString().split(",");
			String recValue = "";
			for (int i = 0; i < RecArr.length; i++) {
				String recTrim = RecArr[i].replaceAll("\\s+", "");
				// catalogue = catalogueService.findCatalogueByCode(recTrim);
				recValue += getCatlogueValueByCode(recTrim).getName() + ",";
			}
			recValue = recValue.substring(0, recValue.length() - 1);
			farmerExportCols.add(!ObjectUtil.isEmpty(recValue) ? recValue : "NA");

		} else {
			farmerExportCols.add("");
		}

		if (!StringUtil.isEmpty(String.valueOf(farmer[44]))) {
			String RecArr[] = farmer[44].toString().split(",");
			String recValue = "";
			for (int i = 0; i < RecArr.length; i++) {
				String recTrim = RecArr[i].replaceAll("\\s+", "");
				// catalogue = catalogueService.findCatalogueByCode(recTrim);
				recValue += getCatlogueValueByCode(recTrim).getName() + ",";
			}
			recValue = recValue.substring(0, recValue.length() - 1);
			farmerExportCols.add(!ObjectUtil.isEmpty(recValue) ? recValue : "NA");

		} else {
			farmerExportCols.add("");
		}

		farmerExportCols.add(StringUtil.isEmpty(farmer[45]) ? getText("NA") : StringUtil.trim(farmer[45].toString()));// mbe
																														// num

		farmerExportCols.add(ObjectUtil.isEmpty(getCatlogueValueByCode(String.valueOf(farmer[24])))
				|| StringUtil.isEmpty(getCatlogueValueByCode(String.valueOf(farmer[24])).getName()) ? getText("NA")
						: getCatlogueValueByCode(String.valueOf(farmer[24])).getName()); // prefWork

		List<Object[]> healthAssesObj = getHealthAssesmentList().stream()
				.filter(obj -> String.valueOf(obj[1]).trim().equals(farmer[0].toString().trim()))
				.collect(Collectors.toList());
		// Origin,Remarks,Status of professional consultation,Detail of
		// consultation
		getFarmCatalougeMap(104).forEach((k, v) -> {
			farmerAndContractStatus = false;
			healthAssesObj.stream().filter(obj -> String.valueOf(obj[2]).trim().equals(k)).forEach(obj -> {
				farmerAndContractStatus = true;
				farmerExportCols.add(v);
				farmerExportCols.add(getLocaleProperty("originType" + String.valueOf(obj[3]).trim()));
				farmerExportCols.add(String.valueOf(obj[4]));
				farmerExportCols.add(String.valueOf(obj[5]).equals("0") ? "Yes" : "No");
				farmerExportCols.add(String.valueOf(obj[6]));
			});
			if (!farmerAndContractStatus) {
				farmerExportCols.add("");
				farmerExportCols.add("");
				farmerExportCols.add("");
				farmerExportCols.add("");
				farmerExportCols.add("");
			}
		});

		List<Object[]> selfAssessmentObj = getSelfAssesmentList().stream()
				.filter(obj -> String.valueOf(obj[1]).trim().equals(farmer[0].toString().trim()))
				.collect(Collectors.toList());

		getFarmCatalougeMap(108).forEach((k, v) -> {
			farmerAndContractStatus = false;
			selfAssessmentObj.stream().filter(obj -> String.valueOf(obj[2]).trim().equals(k)).forEach(obj -> {
				farmerAndContractStatus = true;
				farmerExportCols.add(ObjectUtil.isEmpty(getCatlogueValueByCode(String.valueOf(obj[2])))
						|| StringUtil.isEmpty(getCatlogueValueByCode(String.valueOf(obj[2])).getName()) ? getText("NA")
								: getCatlogueValueByCode(String.valueOf(obj[2])).getName());

				farmerExportCols.add(String.valueOf(obj[3]).equals("0") ? "Yes" : "No");

				farmerExportCols.add(StringUtil.isEmpty(obj[4]) ? getText("NA") : StringUtil.trim(obj[4].toString()));
			});
			if (!farmerAndContractStatus) {
				farmerExportCols.add("");
				farmerExportCols.add("");
				farmerExportCols.add("");
			}
		});

		/*
		 * getHealthAssesmentList().stream() .filter(obj ->
		 * String.valueOf(obj[1]).trim().equals(farmer[0].toString().trim())).
		 * forEach(obj -> {
		 * 
		 * farmerExportCols.add(ObjectUtil.isEmpty(getCatlogueValueByCode(String
		 * .valueOf(obj[2]))) ||
		 * StringUtil.isEmpty(getCatlogueValueByCode(String.valueOf(obj[2])).
		 * getName()) ? getText("NA") :
		 * getCatlogueValueByCode(String.valueOf(obj[2])).getName());
		 * 
		 * farmerExportCols .add(StringUtil.isEmpty(obj[3]) ? getText("NA") :
		 * StringUtil.trim(obj[3].toString()));
		 * farmerExportCols.add(getLocaleProperty("originType" +
		 * String.valueOf(obj[3]).trim()));
		 * 
		 * farmerExportCols .add(StringUtil.isEmpty(obj[4]) ? getText("NA") :
		 * StringUtil.trim(obj[4].toString()));
		 * 
		 * farmerExportCols .add(StringUtil.isEmpty(obj[5]) ? getText("NA") :
		 * StringUtil.trim(obj[5].toString()));
		 * 
		 * farmerExportCols .add(StringUtil.isEmpty(obj[6]) ? getText("NA") :
		 * StringUtil.trim(obj[6].toString()));
		 * 
		 * });
		 */

		/*
		 * getSelfAssesmentList().stream().filter(obj ->
		 * String.valueOf(obj[1]).trim().equals(farmer[0].toString().trim()))
		 * .forEach(obj -> {
		 * 
		 * farmerExportCols.add(ObjectUtil.isEmpty(getCatlogueValueByCode(String
		 * .valueOf(obj[2]))) ||
		 * StringUtil.isEmpty(getCatlogueValueByCode(String.valueOf(obj[2])).
		 * getName()) ? getText("NA") :
		 * getCatlogueValueByCode(String.valueOf(obj[2])).getName());
		 * 
		 * farmerExportCols .add(StringUtil.isEmpty(obj[3]) ? getText("NA") :
		 * StringUtil.trim(obj[3].toString()));
		 * 
		 * farmerExportCols .add(StringUtil.isEmpty(obj[4]) ? getText("NA") :
		 * StringUtil.trim(obj[4].toString()));
		 * 
		 * });
		 */

		/*
		 * farmerService.listFarmerDynmaicFieldsByFarmerId(Long.valueOf(farmer[0
		 * ].toString())).stream() .forEach(farmerDynamicFieldsValue -> {
		 * farmerExportCols.add(farmerDynamicFieldsValue.getFieldValue()); });
		 */

		List<FarmerDynamicFieldsValue> farmerDynamicList = getDynamicValues().stream()
				.filter(fdfv -> (!ObjectUtil.isEmpty(fdfv.getFarmerDynamicData().getReferenceId()))
						&& (Long.valueOf(fdfv.getFarmerDynamicData().getReferenceId()) == Long
								.valueOf(farmer[0].toString()))
						&& fdfv.getFarmerDynamicData().getTxnType().equals("308"))
				.collect(Collectors.toList());
		/*
		 * dynamicExportFieldList.stream().forEach(str -> {
		 * farmerDynamicList.stream().filter(fdfv ->
		 * fdfv.getFieldName().equalsIgnoreCase(str)).forEach(obj -> { String
		 * compoType =
		 * getFarmerDynmaicFieldsMap().get(obj.getFieldName()).getComponentType(
		 * ); if (compoType.equals("2")) { if (obj.getFieldValue().equals("1"))
		 * { farmerExportCols.add("Yes"); } else if
		 * (obj.getFieldValue().equals("0")) { farmerExportCols.add("No"); }
		 * else { farmerExportCols.add(" "); } } else if (compoType.equals("4"))
		 * { farmerExportCols.add(!StringUtil.isEmpty(obj.getFieldValue()) ?
		 * getCatlogueValueByCode(obj.getFieldValue()).getName() : " ");
		 * 
		 * } else { if(StringUtil.isEmpty(obj.getFieldValue())){
		 * farmerExportCols.add(" "); }else{
		 * farmerExportCols.add(obj.getFieldValue()); } }
		 * 
		 * });
		 * 
		 * });
		 */

		dynamicFields().stream().forEach(dynamicSectionConfig -> {
			dynamicSectionConfig.getDynamicFieldConfigs().stream().forEach(dynamicFieldConfig -> {
				flag = false;
				farmerDynamicList.stream()
						.filter(fdfv -> fdfv.getFieldName().equalsIgnoreCase(dynamicFieldConfig.getComponentName()))
						.forEach(obj -> {
							String compoType = getFarmerDynmaicFieldsMap().get(obj.getFieldName()).getComponentType();
							flag = true;
							if (compoType.equals("2")) {
								if (obj.getFieldValue().equals("1")) {
									farmerExportCols.add("Yes");
								} else if (obj.getFieldValue().equals("0")) {
									farmerExportCols.add("No");
								} else {
									farmerExportCols.add("");
								}
							} else if (compoType.equals("4")) {
								farmerExportCols.add(!StringUtil.isEmpty(obj.getFieldValue())
										? getCatlogueValueByCode(obj.getFieldValue()).getName() : "");

							} else {
								farmerExportCols.add(obj.getFieldValue());
							}
						});
				if (!flag) {
					farmerExportCols.add(" ");
				}
			});
		});

		/*
		 * farmerService.listFarmerDynmaicFieldsByFarmerId(Long.valueOf(farmer[0
		 * ].toString())).stream() .forEach(farmerDynamicFieldsValue -> {
		 * farmerExportCols.add(farmerDynamicFieldsValue.getFieldValue()); });
		 */
		return farmerExportCols;
	}

	List<Object[]> healthAssess = new ArrayList<>();

	List<Object[]> selfAssess = new ArrayList<>();

	public List<Object[]> getHealthAssesmentList() {

		if (healthAssess.size() <= 0)
			healthAssess = farmerService.ListOfFarmerHealthAssessmentByFarmerId();

		return healthAssess;

	}

	public List<Object[]> getSelfAssesmentList() {

		if (selfAssess.size() <= 0)
			selfAssess = farmerService.ListOfFarmerSelfAssesmentByFarmerId();

		return selfAssess;
	}

	List<FarmerDynamicFieldsValue> dynamicVal = new ArrayList<>();

	public List<FarmerDynamicFieldsValue> getDynamicValues() {

		if (dynamicVal.size() <= 0)

			dynamicVal = farmerService.listDynmaicFieldsInfo();

		return dynamicVal;
	}

	List<DynamicFieldConfig> dynamicFieldList = new ArrayList<>();

	public List<DynamicFieldConfig> getDynamicFieldList() {
		if (dynamicFieldList.size() <= 0) {
			dynamicFieldList = farmerService.listDynamicFields();
		}
		return dynamicFieldList;
	}

	public Map<String, DynamicFieldConfig> getFarmerDynmaicFieldsMap() {
		return getDynamicFieldList().stream()
				.collect(Collectors.toMap(DynamicFieldConfig::getComponentName, obj -> obj));
	}

	/**
	 * Gets the farmer file input stream.
	 * 
	 * @return the farmer file input stream
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	HSSFRow row, titleRow;
	HSSFCell cell;

	int rowNum = 3;
	int colNum = 0;

	private InputStream getFarmerFileInputStream() throws IOException, ParseException {

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getText("exportXLSFarmerTitle"));
		HSSFPatriarch drawing = sheet.createDrawingPatriarch();

		HSSFCellStyle style1 = wb.createCellStyle();
		HSSFCellStyle style2 = wb.createCellStyle();
		HSSFCellStyle style3 = wb.createCellStyle();


		HSSFFont font1 = wb.createFont();
		font1.setFontHeightInPoints((short) 22);

		HSSFFont font2 = wb.createFont();
		font2.setFontHeightInPoints((short) 12);

		HSSFFont font3 = wb.createFont();
		font3.setFontHeightInPoints((short) 10);

		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap();

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(3);
		cell.setCellValue(new HSSFRichTextString(getText("exportXLSFarmerTitle")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		style1.setAlignment(CellStyle.ALIGN_CENTER);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);

		sheet.addMergedRegion(new CellRangeAddress(3, 3, 3, 5));
		int imgRow1 = 0;
		int imgRow2 = 2;
		int imgCol1 = 0;
		int imgCol2 = 1;

		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));

		++rowNum;

		ESESystem preferences = preferncesService.findPrefernceById("1");

		setFarmerCodeEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE));

		/*
		 * String columnHeaders1 = getLocaleProperty("exportXLSFarmerHeading");
		 * 
		 * if (farmerCodeEnabled.equals("1")) { columnHeaders1 =
		 * getLocaleProperty("exportXLSFarmerHeading"); } else { columnHeaders1
		 * = getLocaleProperty("exportXLSFarmerHeading1"); }
		 * 
		 * if (getCurrentTenantId().equalsIgnoreCase("atma")) {
		 * 
		 * columnHeaders1 = getText("exportXLSFarmerHeadingapmas"); }
		 * 
		 * if (getCurrentTenantId().equalsIgnoreCase("blri")) {
		 * 
		 * columnHeaders1 = getText("exportXLSFarmerHeadingblri"); }
		 * 
		 * if ((getCurrentTenantId().equalsIgnoreCase("lalteer")) ||
		 * (getCurrentTenantId().equalsIgnoreCase("laleerqa"))) {
		 * 
		 * columnHeaders1 = getText("exportXLSFarmerHeadinglalteer"); } if
		 */
		rowNum += 2;
		HSSFRow rowHead1 = sheet.createRow(rowNum++);
		rowHead1.setHeight((short) 600);
		/*
		 * if (getCurrentTenantId().equalsIgnoreCase("kpf")) {
		 * 
		 * columnHeaders1 = getText("exportXLSFarmerHeadingkpf"); }
		 * 
		 * if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {
		 * 
		 * columnHeaders1 = getText("exportXLSFarmerHeadingPra" +
		 * getBranchId()); } if
		 * (getCurrentTenantId().equalsIgnoreCase("meridian")) {
		 * 
		 * columnHeaders1 = getText("exportXLSFarmerHeadingMeridian"); }
		 * 
		 * if ((getIsMultiBranch().equalsIgnoreCase("1") &&
		 * (getIsParentBranch().equals("1") ||
		 * StringUtil.isEmpty(branchIdValue)))) {
		 * 
		 * columnHeaders1 = getText("exportXLSFarmerHeadingSubOrg"); }
		 */

		String columnHeaders1 = null;

		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {
				columnHeaders1 = getLocaleProperty("exportFarmerHeadingBranch");
			} else if (!StringUtil.isEmpty(branchIdValue)) {
				columnHeaders1 = getLocaleProperty("exportFarmerHeading");
			}
		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				columnHeaders1 = getLocaleProperty("exportFarmerHeadingBranch");
			} else {
				columnHeaders1 = getLocaleProperty("exportFarmerHeading");
			}
		}
		if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {

			if (StringUtil.isEmpty(branchIdValue)) {
				columnHeaders1 = getLocaleProperty("exportFarmerHeadingOrganicPratibha");
			} else if (getBranchId().equalsIgnoreCase("organic")) {
				columnHeaders1 = getLocaleProperty("exportFarmerHeadingOrganic");
			} else if (getBranchId().equalsIgnoreCase("bci")) {
				columnHeaders1 = getLocaleProperty("exportFarmerHeadingBCI");
			}

		}
		if (getCurrentTenantId().equalsIgnoreCase("welspun")) {

			if (StringUtil.isEmpty(branchIdValue)) {
				columnHeaders1 = getLocaleProperty("exportFarmerListColumnHeaderBranchWelspun");
			} else if (getIsParentBranch().equalsIgnoreCase("1")) {
				columnHeaders1 = getLocaleProperty("exportFarmerHeadingWelspunParent");
			} else
				columnHeaders1 = getLocaleProperty("exportFarmerHeadingWelspun");
		}
		if (getCurrentTenantId().equalsIgnoreCase("ocp")) {

			if (StringUtil.isEmpty(branchIdValue)) {
				columnHeaders1 = getLocaleProperty("exportFarmerListColumnHeaderBranchWelspun");
			} else if (getIsParentBranch().equalsIgnoreCase("1")) {
				columnHeaders1 = getLocaleProperty("exportFarmerHeadingWelspunParent");
			} else
				columnHeaders1 = getLocaleProperty("exportFarmerHeadingWelspun");
		}

		if (getCurrentTenantId().equalsIgnoreCase("ecoagri")) {
			columnHeaders1 = getLocaleProperty("FarmerExportHeader");

		}

		int iterator1 = 1;

		cell = rowHead1.createCell(0);
		cell.setCellValue(new HSSFRichTextString("SNO"));
		style2.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cell.setCellStyle(style2);
		font2.setBoldweight((short) 12);
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style2.setFont(font2);
		style2.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style2.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style2.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style2.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style2.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style2.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style2.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style2.setTopBorderColor(IndexedColors.BLACK.getIndex());

		for (String cellHeader : columnHeaders1.split("\\,")) {

			if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
			} else if (cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());
			}

			if (StringUtil.isEmpty(branchIdValue)) {
				cell = rowHead1.createCell(iterator1);
				cell.setCellValue(new HSSFRichTextString(cellHeader));
				style2.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
				style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				cell.setCellStyle(style2);
				font2.setBoldweight((short) 12);
				font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				style2.setFont(font2);
				if (iterator1 != 7) {
					sheet.setColumnWidth(iterator1, (15 * 450));
				}
				iterator1++;

			} else {
				if (!cellHeader.equalsIgnoreCase(getText("app.branch"))) {
					cell = rowHead1.createCell(iterator1);
					cell.setCellValue(new HSSFRichTextString(cellHeader));
					// style2.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
					// style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					cell.setCellStyle(style2);
					font2.setBoldweight((short) 12);
					font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					style2.setFont(font2);
					sheet.setColumnWidth(iterator1, (15 * 550));
					iterator1++;
				}
			}
		}

		List<Object[]> farmersList = getAllExportRecords();
		if (getCurrentTenantId().equalsIgnoreCase("ocp") || getCurrentTenantId().equalsIgnoreCase("avt")){
			
	List<String> ids = farmersList.stream().map(u -> u[0].toString()).collect(Collectors.toList());
	System.out.println(ids);
	if(ids!=null && !ids.isEmpty()){
		if (getCurrentTenantId().equalsIgnoreCase("ocp")){
		List<Object[]> farmCropDetails=farmerService.ListFarmerCropDetails(ids);
		farmerMaps=farmCropDetails.stream().map(u -> (Object[]) u)
				.collect(Collectors.toMap(u -> Long.valueOf(u[0].toString()), u -> (Object[]) u));
		}else if (getCurrentTenantId().equalsIgnoreCase("avt")){
			List<Object[]> farmCropDetails=farmerService.ListFarmerCropDetailsByFarmer(ids);
			farmerMaps=farmCropDetails.stream().map(u -> (Object[]) u)
					.collect(Collectors.toMap(u -> Long.valueOf(u[0].toString()), u -> (Object[]) u));
					}
		/*List<Farmer> farmers=farmerService.listFarmerByIds(ids);*/
		
		
	}
	}
		// 0=Id,1=Farmer Id,2=Farmer Code,3=First Name,4=Last
		// name,5=surName,6=village name,7=Group name,8=Is certified
		// Farmer,9=Status,10=BranchId
		AtomicInteger snoCount = new AtomicInteger(0);
		farmersList.stream().forEach(farmer -> {
			row = sheet.createRow(rowNum++);
			row.setHeight((short) 400);
			colNum = 0;
			cell = row.createCell(colNum++);
			cell.setCellValue(snoCount.incrementAndGet());
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID) || getCurrentTenantId().equalsIgnoreCase(ESESystem.OCP)) {
				if (StringUtil.isEmpty(branchIdValue)) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(farmer[10])))
									? getBranchesMap().get(getParentBranchMap().get(farmer[10]))
									: getBranchesMap().get(farmer[10])));
				}
			} else {
				if (StringUtil.isEmpty(branchIdValue)) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							!StringUtil.isEmpty(branchesMap.get(farmer[10])) ? (branchesMap.get(farmer[10])) : ""));
				}
			}
			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(farmer[10])));
			}

			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) { //Farmer Code
				if (StringUtil.isEmpty(getBranchId()) || getBranchId().equalsIgnoreCase("organic")) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							StringUtil.isEmpty(farmer[11]) ? getText("NA") : farmer[11].toString()));
				} else {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							StringUtil.isEmpty(farmer[2]) ? getText("NA") : farmer[2].toString()));
				}
			} else {
				if (farmerCodeEnabled.equals("1") || getEnableHHCode().equalsIgnoreCase("1")) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							StringUtil.isEmpty(farmer[2]) ? getText("NA") : farmer[2].toString()));
				}
			}

			if (getCurrentTenantId().equalsIgnoreCase("ocp") || getCurrentTenantId().equalsIgnoreCase("kenyafpo")  ) { //Farmer Id
				cell = row.createCell(colNum++);
				cell.setCellValue(
						new HSSFRichTextString(StringUtil.isEmpty(farmer[1]) ? getText("") : farmer[1].toString()));
			}
			/*
			 * if (farmerCodeEnabled.equals("1")) { cell =
			 * row.createCell(colNum++); cell.setCellValue( new
			 * HSSFRichTextString(StringUtil.isEmpty(farmer[2]) ? getText("NA")
			 * : farmer[2].toString())); }
			 */
			cell = row.createCell(colNum++); //First Name
			cell.setCellValue(new HSSFRichTextString(
					StringUtil.isEmpty(farmer[3]) ? getText("NA") : StringUtil.trim(farmer[3].toString())));
			if (!getCurrentTenantId().equalsIgnoreCase("symrise") && !getCurrentTenantId().equalsIgnoreCase("gsma")
					&& !getCurrentTenantId().equalsIgnoreCase("ocp")) { //Last name
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(
						StringUtil.isEmpty(farmer[4]) ? getText("NA") : StringUtil.trim(farmer[4].toString())));
			}
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.KPF_TENANT_ID)
					|| getCurrentTenantId().equalsIgnoreCase(ESESystem.SIMFED_TENANT_ID)
					|| getCurrentTenantId().equalsIgnoreCase(ESESystem.WUB_TENANT_ID)) { //Sur Name

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(
						StringUtil.isEmpty(farmer[5]) ? getText("NA") : StringUtil.trim(farmer[5].toString())));
			}

			if (getCurrentTenantId().equalsIgnoreCase("livelihood")){
			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(
					(ObjectUtil.isEmpty(farmer[25]) && farmer[25] == null) ? getText("NA") : farmer[25].toString()));
			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(
					(ObjectUtil.isEmpty(farmer[14]) && farmer[14] == null) ? getText("NA") : farmer[14].toString()));
			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(
					(ObjectUtil.isEmpty(farmer[26]) && farmer[26] == null) ? getText("NA") : farmer[26].toString()));
			}
			
			cell = row.createCell(colNum++); //Village
			cell.setCellValue(new HSSFRichTextString(
					(ObjectUtil.isEmpty(farmer[6]) && farmer[6] == null) ? getText("NA") : farmer[6].toString()));

			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.SYMRISE)) {
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString((ObjectUtil.isEmpty(farmer[20]) && farmer[20] == null)
						? getText("NA") : farmer[20].toString()));
				// rows.add(farmer.getCertificateStandard() == null ? "" :
				// farmer.getCertificateStandard().getName());
				// rows.add(certificationLevels.get(farmer[8].toString());
				cell = row.createCell(colNum++);
				if (farmer[21] != null) {
					String certLevel = certificationLevels.get(farmer[21]);
					cell.setCellValue(
							new HSSFRichTextString(StringUtil.isEmpty(certLevel) ? getText("NA") : certLevel));
				} else {
					cell.setCellValue(new HSSFRichTextString(" "));
				}
			}
			cell = row.createCell(colNum++); //Samithi id 
			cell.setCellValue(
					new HSSFRichTextString(ObjectUtil.isEmpty(farmer[7]) ? getText("NA") : farmer[7].toString()));
			if (!getCurrentTenantId().equalsIgnoreCase("livelihood") && !getCurrentTenantId().equalsIgnoreCase("kenyafpo")) {
				if (!StringUtil.isEmpty(isCertifiedFarmerInfoEnabled) && isCertifiedFarmerInfoEnabled.equals("1")) { //Certification
					if (!getCurrentTenantId().equalsIgnoreCase("pratibha")
							&& !getCurrentTenantId().equalsIgnoreCase("blri")
							&& !getCurrentTenantId().equalsIgnoreCase("pgss")
							&& !getCurrentTenantId().equalsIgnoreCase("olivado")
							&& !getCurrentTenantId().equalsIgnoreCase("awi")) {

						cell = row.createCell(colNum++);
						StringBuffer sb1 = new StringBuffer("");
						if (farmer[8] != null) {
							if (StringUtil.isInteger(farmer[8]) && Integer.parseInt(farmer[8].toString()) == 0
									|| Integer.parseInt(farmer[8].toString()) == 1) {
								sb1.append(isFarmerCertified.get(Integer.parseInt(farmer[8].toString())));
								cell.setCellValue(new HSSFRichTextString(sb1.toString()));
							} else if (StringUtil.isInteger(farmer[8])
									&& Integer.parseInt(farmer[8].toString()) == -1) {
								cell.setCellValue(new HSSFRichTextString(" "));
							}
						} else {
							cell.setCellValue(new HSSFRichTextString(" "));
						}
					}
				}
			}
			
			if (getFpoEnabled().equalsIgnoreCase("1")) {
				cell = row.createCell(colNum++); //FPO 
			cell.setCellValue(
					new HSSFRichTextString(ObjectUtil.isEmpty(farmer[19]) ? getText("NA") : getCatlogueValueByCode(farmer[19].toString()).getName()));			
			}
			
			
			if (getCurrentTenantId().equalsIgnoreCase("wilmar")) {
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(
						ObjectUtil.isEmpty(farmer[17]) ? "" : getMasterTypeList().get(farmer[17].toString())));
				cell = row.createCell(colNum++);
				SimpleDateFormat df2 = new SimpleDateFormat("MMM dd, yyyy");
				cell.setCellValue(new HSSFRichTextString(ObjectUtil.isEmpty(farmer[13]) ? "" : df2.format(farmer[13])));
			}
			
				if (getCurrentTenantId().equalsIgnoreCase("agro")) {
					cell = row.createCell(colNum++);
					style3.setAlignment(CellStyle.ALIGN_RIGHT);
					cell.setCellStyle(style3);
					cell.setCellValue(
							new HSSFRichTextString("-1.0".equalsIgnoreCase(farmer[15].toString()) ? getText("NA") : farmer[15].toString()));
				}
		
			if (getCurrentTenantId().equalsIgnoreCase("iffco") || (getCurrentTenantId().equalsIgnoreCase("awi"))) {
				cell = row.createCell(colNum++);
				if (!ObjectUtil.isEmpty(farmer[19])) {
					FarmCatalogue catalogue = catalogueService.findCatalogueByCode(farmer[19].toString());
					cell.setCellValue(
							new HSSFRichTextString(!ObjectUtil.isEmpty(catalogue) ? catalogue.getName() : "NA"));
				} else {
					cell.setCellValue(new HSSFRichTextString("NA"));
				}

			}
			
			if (getCurrentTenantId().equalsIgnoreCase("ocp")) {
				cell = row.createCell(colNum++);
				style3.setAlignment(CellStyle.ALIGN_RIGHT);
				cell.setCellStyle(style3);
				cell.setCellValue(
						new HSSFRichTextString("-1.0".equalsIgnoreCase(farmer[15].toString()) ? getText("NA") : farmer[15].toString()));
			
				cell = row.createCell(colNum++);
				SimpleDateFormat df2 = new SimpleDateFormat("MMM dd, yyyy");
				Date cdt = (Date) farmer[22];
				cell.setCellValue(
						new HSSFRichTextString(!ObjectUtil.isEmpty(cdt) && (cdt!=null)
								? df2.format(cdt) : ""));
				
				cell = row.createCell(colNum++);
				cell.setCellValue(
						new HSSFRichTextString(ObjectUtil.isEmpty(farmer[12]) ? getText(" ") : farmer[12].toString()));
				
				
			}
			if (getCurrentTenantId().equalsIgnoreCase("griffith") || getCurrentTenantId().equalsIgnoreCase("olivado") || getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)) {
			cell = row.createCell(colNum++);
			SimpleDateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT_2);
			cell.setCellValue(new HSSFRichTextString(ObjectUtil.isEmpty(farmer[13]) ? "" : df.format(farmer[13])));
			
			cell = row.createCell(colNum++);
			cell.setCellValue(
					new HSSFRichTextString(ObjectUtil.isEmpty(farmer[12]) ? getText(" ") : farmer[12].toString()));
			}
			
			if ( getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)) {
				List<String> agent = agentService.findAgentNameByWareHouseId(Long.valueOf(farmer[28].toString()));
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
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(farmer[0]) &&  !ObjectUtil.isEmpty(farmsizepMap.get(farmer[0].toString())) ?farmsizepMap.get(farmer[0].toString()) : "" ));
				
				cell = row.createCell(colNum++);
				cell.setCellValue(
						new HSSFRichTextString(ObjectUtil.isEmpty(farmer[27]) ? getText("NA") : farmer[27].toString()));
				}
			if ( getCurrentTenantId().equalsIgnoreCase(ESESystem.AVT)) {
				Object[] details=farmerMaps.get(Long.valueOf(farmer[0].toString()));
				if (!ObjectUtil.isEmpty(details) || details!=null) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(details[4]) && !StringUtil.isEmpty(details[4].toString())) ? details[4].toString() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(details[5]) && !StringUtil.isEmpty(details[5].toString())) ? details[5].toString() : ""));
			
			}else{
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString("NA"));
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString("NA"));
				}
			}
			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(getText("status" + farmer[9].toString())));
			
			if (getCurrentTenantId().equalsIgnoreCase("ocp") && farmer[1]!=null && !StringUtil.isEmpty(farmer[1].toString())) {
			/*JSONArray rows = new JSONArray();
			rows=getFarmCropInfo(farmer[1].toString());
			rows.stream().forEach(obj->{
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(obj) && !StringUtil.isEmpty(obj.toString())) ? obj.toString() : ""));
			}
					);*/
				Object[] details=farmerMaps.get(Long.valueOf(farmer[0].toString()));
				if (!ObjectUtil.isEmpty(details) || details!=null) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(details[3]) && !StringUtil.isEmpty(details[3].toString())) ? details[3].toString() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(details[2]) && !StringUtil.isEmpty(details[2].toString())) ? details[2].toString() : ""));
			
			}else{
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString("NA"));
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString("NA"));
				}
			
			}
			
			if (getCurrentTenantId().equalsIgnoreCase("olivado")) {
				cell = row.createCell(colNum++);
				cell.setCellValue(
						new HSSFRichTextString(ObjectUtil.isEmpty(farmer[18]) ? getText("NA") : farmer[18].toString()));
			}
			
			if (getCurrentTenantId().equalsIgnoreCase("livelihood")) {
				cell = row.createCell(colNum++);
				SimpleDateFormat df2 = new SimpleDateFormat("MMM dd, yyyy");
				cell.setCellValue(new HSSFRichTextString(ObjectUtil.isEmpty(farmer[13]) ? "" : df2.format(farmer[13])));
				
				cell = row.createCell(colNum++);
				cell.setCellValue(
						new HSSFRichTextString(ObjectUtil.isEmpty(farmer[12]) ? getText(" ") : farmer[12].toString()));
				
				cell = row.createCell(colNum++);
				SimpleDateFormat df3 = new SimpleDateFormat("MMM dd, yyyy");
				cell.setCellValue(new HSSFRichTextString(ObjectUtil.isEmpty(farmer[23]) ? "" : df3.format(farmer[23])));
				
				cell = row.createCell(colNum++);
				cell.setCellValue(
						new HSSFRichTextString(ObjectUtil.isEmpty(farmer[24]) ? getText(" ") : farmer[24].toString()));
				
			}
		});

		/*for (int i = 0; i <= colNum; i++) {
			sheet.autoSizeColumn(i);
		}*/
		
		// Add a picture
		int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		/*picture.resize(0.50);*/

		String makeDir = FileUtil.storeXls(request.getSession().getId());
		String fileName = getText("farmerList") + fileNameDateFormat.format(new Date()) + ".xls";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		wb.write(fileOut);
		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		fileOut.close();
		return stream;
	}

	private InputStream getFarmerFileQrCode() throws IOException, ParseException {
		rowNum = 3;
		colNum = 0;

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getText("exportXLSFarmerTitleQr"));
		HSSFPatriarch drawing = sheet.createDrawingPatriarch();

		HSSFCellStyle style1 = wb.createCellStyle();
		HSSFCellStyle style2 = wb.createCellStyle();

		HSSFCellStyle picStyle = wb.createCellStyle();
		picStyle.setWrapText(true);

		HSSFFont font1 = wb.createFont();
		font1.setFontHeightInPoints((short) 22);

		HSSFFont font2 = wb.createFont();
		font2.setFontHeightInPoints((short) 12);

		HSSFFont font3 = wb.createFont();
		font3.setFontHeightInPoints((short) 10);

		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap();

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(3);
		cell.setCellValue(new HSSFRichTextString(getText("exportXLSFarmerTitleQr")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);

		sheet.addMergedRegion(new CellRangeAddress(3, 3, 3, 5));
		int imgRow1 = 0;
		int imgRow2 = 2;
		int imgCol1 = 0;
		int imgCol2 = 1;

		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));

		++rowNum;

		ESESystem preferences = preferncesService.findPrefernceById("1");

		setFarmerCodeEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE));

		rowNum += 2;
		HSSFRow rowHead1 = sheet.createRow(rowNum++);
		rowHead1.setHeight((short) 600);
		String columnHeaders1 = null;

		int iterator1 = 1;

		cell = rowHead1.createCell(0);
		cell.setCellValue(new HSSFRichTextString("SNO"));
		style2.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cell.setCellStyle(style2);
		font2.setBoldweight((short) 12);
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style2.setFont(font2);
		style2.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style2.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style2.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style2.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style2.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style2.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style2.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style2.setTopBorderColor(IndexedColors.BLACK.getIndex());
		columnHeaders1 = getLocaleProperty("qrCodeExport");
		for (String cellHeader : columnHeaders1.split("\\,")) {

			cell = rowHead1.createCell(iterator1);
			cell.setCellValue(new HSSFRichTextString(cellHeader));
			style2.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
			style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cell.setCellStyle(style2);
			font2.setBoldweight((short) 12);
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			style2.setFont(font2);
			if (iterator1 != 7) {
				sheet.setColumnWidth(iterator1, (15 * 450));
			}
			iterator1++;

		}

		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Map<String, String> searchRecord = getJQGridRequestParam(); // get the
																	// search
																	// parameter
																	// with
		// value

		Farmer filter = new Farmer();

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

		if (!StringUtil.isEmpty(searchRecord.get("farmerId"))) {
			filter.setFarmerId(searchRecord.get("farmerId").trim());
		}
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
			if (StringUtil.isEmpty(getBranchId()) || getBranchId().equalsIgnoreCase("organic")) {
				if (!StringUtil.isEmpty(searchRecord.get("farmersCodeTracenet"))) {
					filter.setFarmersCodeTracenet(searchRecord.get("farmersCodeTracenet").trim());
				}

			}
		} else {
			if (!StringUtil.isEmpty(searchRecord.get("farmerCode"))) {
				filter.setFarmerCode(searchRecord.get("farmerCode").trim());
			}
		}

		if (!StringUtil.isEmpty(searchRecord.get("firstName")))
			filter.setFirstName(searchRecord.get("firstName").trim());

		if (!StringUtil.isEmpty(searchRecord.get("lastName")))
			filter.setLastName(searchRecord.get("lastName").trim());

		if (!StringUtil.isEmpty(searchRecord.get("surName")))
			filter.setSurName(searchRecord.get("surName").trim());

		if (!StringUtil.isEmpty(searchRecord.get("v.name"))) {
			Village village = new Village();
			village.setName(searchRecord.get("v.name").trim());
			filter.setVillage(village);
		}
		if (!StringUtil.isEmpty(searchRecord.get("s.name"))) {
			Warehouse samithi = new Warehouse();
			samithi.setName(searchRecord.get("s.name").trim());
			filter.setSamithi(samithi);
		}

		if (!StringUtil.isEmpty(searchRecord.get("cs.name"))) {
			CertificateStandard certificateStandard = new CertificateStandard();
			certificateStandard.setName(searchRecord.get("cs.name").trim());
			filter.setCertificateStandard(certificateStandard);
		}

		if (!StringUtil.isEmpty(searchRecord.get("isCertifiedFarmer"))) {
			filter.setCertificationFilter(searchRecord.get("isCertifiedFarmer"));
		}
		
		if (!StringUtil.isEmpty(searchRecord.get("status"))) {
			if ("1".equals(searchRecord.get("status"))) {
				filter.setFilterStatus("status");
				filter.setStatus(Farmer.Status.ACTIVE.ordinal());
			} else {
				filter.setFilterStatus("status");
				filter.setStatus(Farmer.Status.INACTIVE.ordinal());
			}
		}

		if (!StringUtil.isEmpty(searchRecord.get("mobileNumber"))) {
			filter.setMobileNumber(searchRecord.get("mobileNumber").trim());
		}

		/*
		 * if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PGSS_TENANT_ID))
		 * { if (!StringUtil.isEmpty(getType()) && getType().equals(Farmer.IRP))
		 * { filter.setTypez(Farmer.IRP); } else {
		 * filter.setTypez(Farmer.FARMER); } }
		 */

		if (!StringUtil.isEmpty(searchRecord.get("createdUsername")))
			filter.setCreatedUsername(searchRecord.get("createdUsername").trim());

		if (!StringUtil.isEmpty(searchRecord.get("createdDate"))) {
			filter.setCreatedDate(df.parse(searchRecord.get("createdDate")));
		}

		filter.setSearchPage(searchPage);
		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), 0, filter, getPage());
		AtomicInteger snoCount = new AtomicInteger(0);
		/*
		 * ESESystem pref = preferncesService.findPrefernceById("1"); String
		 * qrText = pref.getPreferences().get(ESESystem.QR_CODE_TEXT);
		 */ Map<String, String> dataMap = new HashMap();

		List<Farmer> farmersList = (List<Farmer>) data.get(ROWS);
		farmersList.stream().forEach(farmer -> {
			if (!StringUtil.isEmpty(farmer.getProofNo())) {
				// String qrCodeText = qrText;
				int rowCol = rowNum++;
				row = sheet.createRow(rowCol);
				row.setHeight((short) 1500);
				colNum = 0;
				cell = row.createCell(colNum++);
				cell.setCellValue(snoCount.incrementAndGet());

				cell = row.createCell(colNum++);
				cell.setCellValue(farmer.getFarmerId());
				cell = row.createCell(colNum++);
				cell.setCellValue(farmer.getFirstName());

				cell = row.createCell(colNum++);
				cell.setCellValue(farmer.getProofNo());

				cell = row.createCell(colNum++);
				cell.setCellValue(farmer.getCity().getName());

				cell = row.createCell(colNum++);
				cell.setCellValue(farmer.getCity().getLocality().getName());

				cell = row.createCell(colNum++);
				cell.setCellValue(farmer.getCity().getLocality().getState().getName());
				int col = colNum++;
				cell = row.createCell(col);
				// sheet.setColumnWidth(col, (250 * 250));

				sheet.addMergedRegion(new CellRangeAddress(rowCol, rowCol, col, col));
				row.setRowStyle(picStyle);
				row.setHeight((short) 3300);
				cell.setCellStyle(picStyle);
				ImageInfo in = farmer.getImageInfo();

				ByteArrayOutputStream stream = QRCode.from(farmer.getProofNo())
						.withErrorCorrection(ErrorCorrectionLevel.L).withHint(EncodeHintType.MARGIN, 2)
						.withSize(250, 250).stream();
				byte[] bytes = stream.toByteArray();

				// Adds a picture to the workbook
				// Dimension dim = ImageUtils.getImageDimension(new
				// ByteArrayInputStream(bytes), 6);
				sheet.setColumnWidth(col, 3000);
				int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);

				HSSFClientAnchor anchor = new HSSFClientAnchor();
				anchor.setCol1(col); // Column B
				anchor.setRow1(rowCol); // Row 3
				anchor.setCol2(col); // Column C
				anchor.setRow2(rowCol); //
				anchor.setAnchorType(1);
				HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);

				picture.resize(0.3);

				col = colNum++;
				cell = row.createCell(col);
				row.setRowStyle(picStyle);
				row.setHeight((short) 3300);
				cell.setCellStyle(picStyle);

				// Dimension farmerPicDim = ImageUtils.getImageDimension(new
				// ByteArrayInputStream(in.getPhoto().getImage()), 6);
				sheet.setColumnWidth(col, 8000);

				sheet.addMergedRegion(new CellRangeAddress(rowCol, rowCol, col, col));

				if (in != null && in.getPhoto() != null && in.getPhoto().getImage() != null
						&& in.getPhoto().getImage().length > 0) {
					try {
						int farmerPic = wb.addPicture(in.getPhoto().getImage(), Workbook.PICTURE_TYPE_PNG);
						HSSFClientAnchor anchorFar = new HSSFClientAnchor();
						anchorFar.setDx1(270);
						anchorFar.setDy1(5);
						anchorFar.setCol1(col); // Column B
						anchorFar.setRow1(rowCol); // Row 3
						anchorFar.setCol2(col); // Column C
						anchorFar.setRow2(rowCol); //
						anchorFar.setAnchorType(1);

						HSSFPicture farmerPict = drawing.createPicture(anchorFar, farmerPic);

						farmerPict.resize(0.359);

					} catch (Exception e) {

					}

				}
			}
		});

		/*
		 * for (int i = 0; i <= colNum; i++) { sheet.autoSizeColumn(i); }
		 */

		// Add a picture
		int pictureIdx = getPicIndex(wb);

		/*
		 * String imageName="C:\\Farmer_Pict\\000020"+ ".jpg"; int pictureIdx =
		 * getPicIndexQR(wb,imageName);
		 */

		HSSFClientAnchor anchor = new HSSFClientAnchor();
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		System.out.println("pictureQR" + picture);
		picture.resize(0.50);

		String makeDir = FileUtil.storeXls(request.getSession().getId());
		String fileName = getText("farmerListQr") + fileNameDateFormat.format(new Date()) + ".xls";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		wb.write(fileOut);
		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		fileOut.close();
		return stream;

	}

	/**
	 * Gets the pic index.
	 * 
	 * @param wb
	 *            the wb
	 * @return the pic index
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public int getPicIndex(HSSFWorkbook wb) throws IOException {

		int index = -1;
		String roleName = (String) request.getSession().getAttribute("role");

		byte[] picData = null;

		picData = clientService.findLogoByCode(Asset.APP_LOGO);

		index = wb.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
	}

	/**
	 * Gets the all records.
	 * 
	 * @return the all records
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	private List<Farmer> getAllRecords() throws ParseException {

		Map data = buildFilterDataMap();
		List<Farmer> list = (List) data.get(ROWS);
		return list;
	}

	@SuppressWarnings("unchecked")
	private List<Object[]> getAllExportRecords() throws ParseException {

		Map data = buildExportFilterDataMap();
		List<Object[]> list = (List) data.get(ROWS);
		return list;
	}

	/**
	 * Gets the certificate categories.
	 * 
	 * @return the certificate categories
	 */
	public List<CertificateCategory> getCertificateCategories() {

		return certificationService.listCertificateCategory();
	}

	/**
	 * Populate certificate standard.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void populateCertificateStandard() throws Exception {

		if (!StringUtil.isEmpty(selectedCertificateCategory)) {
			certificateStandards = certificationService
					.listCertificateStandardByCertificateCategoryId(Long.valueOf(selectedCertificateCategory));
		}
		StringBuffer sb = new StringBuffer();
		for (int count = 0; count < certificateStandards.size(); count++) {
			CertificateStandard certificateStandard = certificateStandards.get(count);
			if (!ObjectUtil.isEmpty(certificateStandard)) {
				sb.append(certificateStandard.getId()).append("~").append(certificateStandard.getName());
			}
			if ((count + 1) != certificateStandards.size()) {
				sb.append("||");
			}
		}
		printAjaxResponse(sb.toString(), "text/html");
	}

	/**
	 * Gets the xls file name.
	 * 
	 * @return the xls file name
	 */
	public String getXlsFileName() {

		return xlsFileName;
	}

	/**
	 * Sets the xls file name.
	 * 
	 * @param xlsFileName
	 *            the new xls file name
	 */
	public void setXlsFileName(String xlsFileName) {

		this.xlsFileName = xlsFileName;
	}

	/**
	 * Sets the file input stream.
	 * 
	 * @param fileInputStream
	 *            the new file input stream
	 */
	public void setFileInputStream(InputStream fileInputStream) {

		this.fileInputStream = fileInputStream;
	}

	/**
	 * Gets the file input stream.
	 * 
	 * @return the file input stream
	 */
	public InputStream getFileInputStream() {

		return fileInputStream;
	}

	/**
	 * Sets the certification service.
	 * 
	 * @param certificationService
	 *            the new certification service
	 */
	public void setCertificationService(ICertificationService certificationService) {

		this.certificationService = certificationService;
	}

	/**
	 * Gets the selected certificate category.
	 * 
	 * @return the selected certificate category
	 */
	public String getSelectedCertificateCategory() {

		return selectedCertificateCategory;
	}

	/**
	 * Sets the selected certificate category.
	 * 
	 * @param selectedCertificateCategory
	 *            the new selected certificate category
	 */
	public void setSelectedCertificateCategory(String selectedCertificateCategory) {

		this.selectedCertificateCategory = selectedCertificateCategory;
	}

	/**
	 * Gets the certificate standards.
	 * 
	 * @return the certificate standards
	 */
	public List<CertificateStandard> getCertificateStandards() {

		if (!ObjectUtil.isEmpty(farmer) && !ObjectUtil.isEmpty(farmer.getCertificateStandard())
				&& !ObjectUtil.isEmpty(farmer.getCertificateStandard().getCategory())) {
			certificateStandards = certificationService.listCertificateStandardByCertificateCategoryId(
					farmer.getCertificateStandard().getCategory().getId());
		}
		return certificateStandards;
	}

	/**
	 * Sets the certificate standards.
	 * 
	 * @param certificateStandards
	 *            the new certificate standards
	 */
	public void setCertificateStandards(List<CertificateStandard> certificateStandards) {

		this.certificateStandards = certificateStandards;
	}

	/**
	 * Gets the certificate standard filter text.
	 * 
	 * @return the certificate standard filter text
	 */
	public String getCertificateStandardFilterText() {

		StringBuffer sb = new StringBuffer();
		List<CertificateStandard> certificateStandards = certificationService.listCertificateStandard();
		sb.append(":").append(FILTER_ALL).append(";");
		certificateStandards.stream().map(certificateStandard -> certificateStandard.getName() + ":"
				+ certificateStandard.getName() + " - " + certificateStandard.getCategory().getName()).distinct()
				.forEach(u -> {
					sb.append(u).append(";");
				});

		String data = sb.toString();
		return data.substring(0, data.length() - 1);
	}

	/**
	 * Gets the certification types filter text.
	 * 
	 * @return the certification types filter text
	 */

	public String getCertificationTypesFilterText() {

		StringBuffer sb = new StringBuffer();
		sb.append("-1").append(":").append(FILTER_ALL).append(";");
		Iterator it = certificationTypes.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			sb.append(entry.getKey()).append(":").append(entry.getValue()).append(";");
		}
		String data = sb.toString();
		return data.substring(0, data.length() - 1);
	}

	/**
	 * Gets the educations filter text.
	 * 
	 * @return the educations filter text
	 */
	public String getEducationsFilterText() {

		StringBuffer sb = new StringBuffer();
		String values = getText("edu");
		String[] valuesArray = values.split(",");
		sb.append("-1:").append(FILTER_ALL).append(";");
		for (int count = 0; count < valuesArray.length; count++) {
			sb.append(count).append(":").append(valuesArray[count]).append(";");
		}
		String data = sb.toString();
		return data.substring(0, data.length() - 1);
	}

	/**
	 * Gets the relations filter text.
	 * 
	 * @return the relations filter text
	 */
	public String getRelationsFilterText() {

		StringBuffer sb = new StringBuffer();
		String values = getText("rel");
		String[] valuesArray = values.split(",");
		sb.append("-1:").append(FILTER_ALL).append(";");
		for (int count = 0; count < valuesArray.length; count++) {
			sb.append(count).append(":").append(valuesArray[count]).append(";");
		}
		String data = sb.toString();
		return data.substring(0, data.length() - 1);
	}

	public String getCertifiedFarmerFilterText() {

		StringBuffer sb = new StringBuffer();
		String values = getText("cer");
		String[] valuesArray = values.split(",");
		sb.append(":").append(FILTER_ALL).append(";");
		for (int count = 0; count < valuesArray.length; count++) {
			sb.append(count).append(":").append(valuesArray[count]).append(";");
		}
		String data = sb.toString();
		return data.substring(0, data.length() - 1);
	}

	private String bankInformationToJson(Set<BankInformation> bankInformation) {

		Set<BankInformation> bank = new HashSet<BankInformation>();
		for (BankInformation bankInfo : bankInformation) {

			BankInformation info = new BankInformation();
			FarmCatalogue cat = getCatlogueValueByCode(bankInfo.getAccType());
			info.setAccType(cat != null ? cat.getName() : "");
			info.setAccNo(bankInfo.getAccNo());
			info.setBankName(bankInfo.getBankName());
			info.setBranchName(bankInfo.getBranchName());
			info.setSortCode(bankInfo.getSortCode());
			info.setAccTypeCode(bankInfo.getAccType());
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.FARM_AGG)) {
			info.setSwiftCode(bankInfo.getSwiftCode());
			info.setAccName(bankInfo.getAccName());
			}
			if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
				FarmCatalogue catalogue = catalogueService.findCatalogueByName(bankInfo.getBankName());
				if (!ObjectUtil.isEmpty(catalogue))
					info.setBankNameCode(catalogue.getCode());
				FarmCatalogue catalogueBranch = catalogueService.findCatalogueByName(bankInfo.getBranchName());
				if (!ObjectUtil.isEmpty(catalogueBranch))
					info.setBranchNameCode(catalogueBranch.getCode());
			}
			bank.add(info);

		}
		Set<BankInformation> bankInformation1 = new HashSet<BankInformation>(bank);
		Gson gson = new Gson();
		String res = gson.toJson(bankInformation1);
		return res;
	}

	public int getFarmerCertifiedDefaultValue() {

		return (ObjectUtil.isEmpty(this.farmer) ? Farmer.CERTIFIED_NO : this.farmer.getIsCertifiedFarmer());
	}

	public int getBeneficiaryGovSchemeDefaultValue() {

		return (!ObjectUtil.isEmpty(this.farmer)
				? !StringUtil.isEmpty(this.farmer.getIsBeneficiaryInGovScheme())
						? Integer.valueOf(this.farmer.getIsBeneficiaryInGovScheme()) : Farmer.CERTIFIED_NO
				: Farmer.CERTIFIED_NO);
	}

	public Map<Long, String> getListLocalities() {
		Map<Long, String> districtMap = new LinkedHashMap<Long, String>();
		if (!StringUtil.isEmpty(selectedState) && StringUtil.isInteger(selectedState)) {
			listLocalities = locationService.findLocalityByStateId(Long.valueOf(selectedState));
		}
		if (!ObjectUtil.isEmpty(listLocalities)) {
			for (Locality locality : listLocalities) {
				districtMap.put(locality.getId(), locality.getCode() + "-" + locality.getName());
			}
		}

		return districtMap;
	}

	public void setListLocalities(List<Locality> listLocalities) {

		this.listLocalities = listLocalities;
	}

	/*
	 * private Map<Integer, String> formEducationMap(String keyProperty,
	 * Map<Integer, String> educationList) { educationList = new
	 * LinkedHashMap(); String values = getText(keyProperty); if
	 * (!StringUtil.isEmpty(values)) { String[] valuesArray = values.split(",");
	 * int i = 1; for (String value : valuesArray) { educationList.put(i++,
	 * value); } } return educationList; }
	 */

	private Map<Integer, String> formHousingType(String keyProperty, Map<Integer, String> housingTypes) {

		housingTypes = getPropertyData(keyProperty);
		return housingTypes;
	}

	private Map<Integer, String> formAssistiveType(String keyProperty, Map<Integer, String> assistiveTypes) {

		assistiveTypes = getPropertyData(keyProperty);
		return assistiveTypes;
	}

	public int getFarmerLoanDefaultValue() {

		// return (ObjectUtil.isEmpty(this.farmer) ? Farmer.CERTIFIED_NO :
		// ((this.farmer
		// .getLoanTakenLastYear()==1) ? Farmer.CERTIFIED_YES :
		// Farmer.CERTIFIED_NO));
		return (ObjectUtil.isEmpty(this.farmer) ? Farmer.CERTIFIED_NO : this.farmer.getLoanTakenLastYear());

	}

	/**
	 * Sets the certification types.
	 * 
	 * @param certificationTypes
	 *            the certification types
	 */
	public void setCertificationTypes(Map<Integer, String> certificationTypes) {

		this.certificationTypes = certificationTypes;
	}

	/**
	 * Gets the certification types.
	 * 
	 * @return the certification types
	 */
	public Map<Integer, String> getCertificationTypes() {

		return certificationTypes;
	}

	/**
	 * Sets the marital satuses.
	 * 
	 * @param maritalSatuses
	 *            the marital satuses
	 */
	public void setMaritalSatuses(Map<Integer, String> maritalSatuses) {

		this.maritalSatuses = maritalSatuses;
	}

	/**
	 * Gets the marital satuses.
	 * 
	 * @return the marital satuses
	 */
	public Map<String, String> getMaritalSatuses() {

		Map<String, String> maritalSatuses = new HashMap<>();
		maritalSatuses = getFarmCatalougeMap(Integer.valueOf(getText("maritalStatus")));
		return maritalSatuses;

	}

	/**
	 * Sets the education list.
	 * 
	 * @param educationList
	 *            the education list
	 */
	public void setEducationList(Map<Integer, String> educationList) {

		this.educationList = educationList;
	}

	/**
	 * Gets the education list.
	 * 
	 * @return the education list
	 */
	/*
	 * public Map<String, String> getEducationList() { Map<String, String>
	 * educationMap = new LinkedHashMap<String, String>(); List<FarmCatalogue>
	 * educationList = farmerService
	 * .listCatelogueType(getText("educationType")); for (FarmCatalogue obj :
	 * educationList) { educationMap.put(obj.getCode(), obj.getName()); } return
	 * educationMap; }
	 */

	public Map<String, String> getEducationList() {
		Map<String, String> educationList = new LinkedHashMap<>();
		educationList = getFarmCatalougeMap(Integer.valueOf(getText("educationType")));
		return educationList;

	}

	public void populateEducation() {

		if (!educationName.equalsIgnoreCase("null") && (!StringUtil.isEmpty(educationName))) {

			FarmCatalogue catalogue = catalogueService.findByNameAndType(educationName,
					Integer.valueOf(getText("educationType")));
			if (catalogue == null) {
				FarmCatalogue farmCatalogue = new FarmCatalogue();
				farmCatalogue.setCode(idGenerator.getCatalogueIdSeq());
				farmCatalogue.setRevisionNo(DateUtil.getRevisionNumber());
				farmCatalogue.setBranchId(getBranchId());
				farmCatalogue.setName(educationName);
				farmCatalogue.setTypez(Integer.valueOf(getText("educationType")));
				catalogueService.addCatalogue(farmCatalogue);
				JSONArray stateArr = new JSONArray();
				List<FarmCatalogue> educationList = farmerService.listCatelogueType(getText("educationType"));
				if (!ObjectUtil.isEmpty(educationList)) {

					for (FarmCatalogue obj : educationList) {
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
	 * Sets the client service.
	 * 
	 * @param clientService
	 *            the new client service
	 */
	public void setClientService(IClientService clientService) {

		this.clientService = clientService;
	}

	/**
	 * Gets the client service.
	 * 
	 * @return the client service
	 */
	public IClientService getClientService() {

		return clientService;
	}

	/**
	 * Sets the selected customer.
	 * 
	 * @param selectedCustomer
	 *            the new selected customer
	 */
	public void setSelectedCustomer(String selectedCustomer) {

		this.selectedCustomer = selectedCustomer;
	}

	/**
	 * Gets the selected customer.
	 * 
	 * @return the selected customer
	 */
	public String getSelectedCustomer() {

		return selectedCustomer;
	}

	/**
	 * Sets the customers.
	 * 
	 * @param customers
	 *            the new customers
	 */
	public void setCustomers(List<Customer> customers) {

		this.customers = customers;
	}

	/**
	 * Gets the customers.
	 * 
	 * @return the customers
	 */
	public List<Customer> getCustomers() {

		return customers;
	}

	/**
	 * Sets the customer projects.
	 * 
	 * @param customerProjects
	 *            the new customer projects
	 */
	public void setCustomerProjects(List<CustomerProject> customerProjects) {

		this.customerProjects = customerProjects;
	}

	/**
	 * Sets the farmer economy.
	 * 
	 * @param farmerEconomy
	 *            the new farmer economy
	 */
	public void setFarmerEconomy(FarmerEconomy farmerEconomy) {

		this.farmerEconomy = farmerEconomy;
	}

	/**
	 * Gets the farmer economy.
	 * 
	 * @return the farmer economy
	 */
	public FarmerEconomy getFarmerEconomy() {

		return farmerEconomy;
	}

	/**
	 * Sets the housing ownerships.
	 * 
	 * @param housingOwnerships
	 *            the housing ownerships
	 */
	public void setHousingOwnerships(Map<Integer, String> housingOwnerships) {

		this.housingOwnerships = housingOwnerships;
	}

	/**
	 * Gets the housing ownerships.
	 * 
	 * @return the housing ownerships
	 */
	public Map<Integer, String> getHousingOwnerships() {

		return housingOwnerships;
	}

	/**
	 * Sets the rupee.
	 * 
	 * @param rupee
	 *            the new rupee
	 */
	public void setRupee(String rupee) {

		this.rupee = rupee;
	}

	/**
	 * Gets the rupee.
	 * 
	 * @return the rupee
	 */
	public String getRupee() {

		return rupee;
	}

	/**
	 * Sets the paise.
	 * 
	 * @param paise
	 *            the new paise
	 */
	public void setPaise(String paise) {

		this.paise = paise;
	}

	/**
	 * Gets the paise.
	 * 
	 * @return the paise
	 */
	public String getPaise() {

		return paise;
	}

	/**
	 * Sets the inspection types.
	 * 
	 * @param inspectionTypes
	 *            the inspection types
	 */
	public void setInspectionTypes(Map<Integer, String> inspectionTypes) {

		this.inspectionTypes = inspectionTypes;
	}

	/**
	 * Gets the inspection types.
	 * 
	 * @return the inspection types
	 */
	public Map<Integer, String> getInspectionTypes() {

		return inspectionTypes;
	}

	/**
	 * Sets the ics statuses.
	 * 
	 * @param icsStatuses
	 *            the ics statuses
	 */
	public void setIcsStatuses(Map<Integer, String> icsStatuses) {

		this.icsStatuses = icsStatuses;
	}

	/**
	 * Gets the ics statuses.
	 * 
	 * @return the ics statuses
	 */
	public Map<Integer, String> getIcsStatuses() {

		return icsStatuses;
	}

	/**
	 * Sets the enrollment map.
	 * 
	 * @param enrollmentMap
	 *            the enrollment map
	 */
	public void setEnrollmentMap(Map<Integer, String> enrollmentMap) {

		this.enrollmentMap = enrollmentMap;
	}

	/**
	 * Gets the enrollment map.
	 * 
	 * @return the enrollment map
	 */
	public Map<Integer, String> getEnrollmentMap() {

		return enrollmentMap;
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

	public String getSearchPage() {

		return searchPage;
	}

	public void setSearchPage(String searchPage) {

		this.searchPage = searchPage;
	}

	public File getFarmerImage() {

		return farmerImage;
	}

	public void setFarmerImage(File farmerImage) {

		this.farmerImage = farmerImage;
	}

	public Image getImage() {

		return image;
	}

	public void setImage(Image image) {

		this.image = image;
	}

	public BankInformation getBankInfo() {

		return bankInfo;
	}

	public void setBankInfo(BankInformation bankInfo) {

		this.bankInfo = bankInfo;
	}

	public Map<Integer, String> getIsFarmerCertified() {

		return isFarmerCertified;
	}

	public void setIsFarmerCertified(Map<Integer, String> isFarmerCertified) {

		this.isFarmerCertified = isFarmerCertified;
	}

	public Double getBalance() {

		return balance;
	}

	public void setBalance(Double balance) {

		this.balance = balance;
	}

	public Map<String, String> getVehicleMap() {

		Map<String, String> farmCatalougeList = new LinkedHashMap<>();
		farmCatalougeList = getFarmCatalougeMap(Integer.valueOf(getLocaleProperty("vechicle").trim()));
		return farmCatalougeList;

	}

	public void setVehicleMap(Map<Integer, String> vehicleMap) {

		this.vehicleMap = vehicleMap;
	}

	public Map<String, String> getConsumerElecMap() {

		Map<String, String> farmCatalougeList = new LinkedHashMap<>();
		farmCatalougeList = getFarmCatalougeMap(Integer.valueOf(getLocaleProperty("consumerElectronicsType").trim()));
		return farmCatalougeList;

	}

	public void setConsumerElecMap(Map<Integer, String> consumerElecMap) {

		this.consumerElecMap = consumerElecMap;
	}

	public Map<Integer, String> getCellPhoneMap() {

		return cellPhoneMap;
	}

	public void setCellPhoneMap(Map<Integer, String> cellPhoneMap) {

		this.cellPhoneMap = cellPhoneMap;
	}

	public Map<Integer, String> getHousingOwnershipMap() {

		return housingOwnershipMap;
	}

	public void setHousingOwnershipMap(Map<Integer, String> housingOwnershipMap) {

		this.housingOwnershipMap = housingOwnershipMap;
	}

	/*
	 * public Map<Integer, String> getHousingTypeMap() { return housingTypeMap;
	 * } public void setHousingTypeMap(Map<Integer, String> housingTypeMap) {
	 * this.housingTypeMap = housingTypeMap; }
	 */

	public Map<String, String> getDrinkingWSMap() {

		String[] icsStatus = getLocaleProperty("drinkingWS").split(",");
		for (int i = 0; i < icsStatus.length; i++)
			drinkingWSMap.put(String.valueOf(i + 1), icsStatus[i]);

		return drinkingWSMap;
	}

	public void setDrinkingWSMap(Map<String, String> drinkingWSMap) {

		this.drinkingWSMap = drinkingWSMap;
	}

	public Map<Integer, String> getElectrifiedHouseMap() {

		return electrifiedHouseMap;
	}

	public void setElectrifiedHouseMap(Map<Integer, String> electrifiedHouseMap) {

		this.electrifiedHouseMap = electrifiedHouseMap;
	}

	public Map<Integer, String> getLifeOrHealthInsuranceMap() {

		return lifeOrHealthInsuranceMap;
	}

	public void setLifeOrHealthInsuranceMap(Map<Integer, String> lifeOrHealthInsuranceMap) {

		this.lifeOrHealthInsuranceMap = lifeOrHealthInsuranceMap;
	}

	public Map<Integer, String> getCropInsuranceMap() {

		return cropInsuranceMap;
	}

	public void setCropInsuranceMap(Map<Integer, String> cropInsuranceMap) {

		this.cropInsuranceMap = cropInsuranceMap;
	}

	public Map<Integer, String> getIsLoanTakenLastYear() {

		return isLoanTakenLastYear;
	}

	public void setIsLoanTakenLastYear(Map<Integer, String> isLoanTakenLastYear) {

		this.isLoanTakenLastYear = isLoanTakenLastYear;
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

	public String getTotalName() {

		return totalName;
	}

	public void setTotalName(String totalName) {

		this.totalName = totalName;
	}

	@Override
	public String populatePDF() throws Exception {

		List<Farmer> farmers = getAllRecords();
		List<Object> farmerObjects = new ArrayList<Object>();
		Map<String, Map> fieldValues = new LinkedHashMap<String, Map>(); // To
																			// send
																			// field
																			// with
																			// map
																			// values
																			// and
																			// assign
																			// it
																			// to
		// the
		// export.
		buildBranchMap();
		fieldValues.put("branchId", branchesMap);
		fieldValues.put("isCertifiedFarmer", isFarmerCertified);
		fieldValues.put("certificationType", certificationTypes);
		fieldValues.put("status", farmerStatus);
		fieldValues.put("masterData", getMasterTypeList());
		fieldValues.put("fpo", getFpo());
		branchIdValue = getBranchId();

		

		for (Farmer farmer : farmers) {
			farmerObjects.add(farmer);
		}
		String entityName = "Farmer";
		if (getCurrentTenantId().equalsIgnoreCase("kpf")) {
			entityName = "FarmerKPF";
		} /*
			 * else if (getCurrentTenantId().equalsIgnoreCase("atma")) {
			 * entityName = "FarmerAPMAS"; }
			 */ else if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
			entityName = "FarmerCHETNA";
		} else if (getCurrentTenantId().equalsIgnoreCase("blri")) {
			entityName = "FarmerBLRI";
		} else if (getCurrentTenantId().equalsIgnoreCase("meridian")) {
			entityName = "FarmerMERIDIAN";
		} else if (getCurrentTenantId().equalsIgnoreCase("lalteer")) {
			entityName = "FarmerLALTEER";
		} /*
			 * else if ((getIsMultiBranch().equalsIgnoreCase("1") &&
			 * (getIsParentBranch().equals("1") ||
			 * StringUtil.isEmpty(branchIdValue)))) { entityName =
			 * "FarmerPdfIndev"; }
			 */ else if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {

			if (StringUtil.isEmpty(branchIdValue)) {
				entityName = "FarmerPRATIBHA";
			} else if (getBranchId().equalsIgnoreCase("organic")) {
				entityName = "FarmerPRATIBHA";
			} else if (getBranchId().equalsIgnoreCase("bci")) {
				entityName = "FarmerPRATIBHABCI";
			}

		} else if (getCurrentTenantId().equalsIgnoreCase("welspun")) {

			if (StringUtil.isEmpty(branchIdValue)) {
				entityName = "FarmerPRATIBHA";
			} else if (getIsParentBranch() != null) {
				entityName = "FarmerWelspun";
			} else
				entityName = "FarmerWelspun";

		} else if (getCurrentTenantId().equalsIgnoreCase("wilmar")) {
			entityName = "FarmerWILMAR";
		} else if (getCurrentTenantId().equalsIgnoreCase("olivado")) {
			entityName = "FarmerOlivado";
		} else if (getCurrentTenantId().equalsIgnoreCase("simfed")) {
			entityName = "FarmerKPF";
		} else if (getCurrentTenantId().equalsIgnoreCase("wub")) {
			entityName = "FarmerKPF";
		} else if (getCurrentTenantId().equalsIgnoreCase("ecoagri")) {
			entityName = "FarmerEcoAgri";
		} else if (getCurrentTenantId().equalsIgnoreCase("livelihood")) {
			entityName = "FarmerLivelihood";
		}else if (getCurrentTenantId().equalsIgnoreCase("kenyafpo")) {
			entityName = "FarmerKenyafpo";
		}

		/*
		 * (getCurrentTenantId().equalsIgnoreCase(ESESystem. PRATIBHA_TENANT_ID)
		 * && getBranchId().equalsIgnoreCase("organic")) { entityName =
		 * "FarmerPRATIBHA"; } else if
		 * (getCurrentTenantId().equalsIgnoreCase(ESESystem. PRATIBHA_TENANT_ID)
		 * && getBranchId().equalsIgnoreCase("bci")) { entityName =
		 * "FarmerPRATIBHABCI"; }
		 */else if (getCurrentTenantId().equalsIgnoreCase("agro")) {
			entityName = "FarmerLISTAGRO";
		} else if (getCurrentTenantId().equalsIgnoreCase("iffco")) {
			entityName = "FarmerLISTIFFCO";
		} else if (getCurrentTenantId().equalsIgnoreCase("awi")) {
			entityName = "FarmerLISTAWI";
		} else if (getCurrentTenantId().equalsIgnoreCase(ESESystem.SYMRISE)) {
			entityName = "FarmerLISTSYMRISE";
		} else {
			entityName = "FarmerLIST";
		}
		InputStream is = getPDFExportDataStream(farmerObjects, entityName, fieldValues);
		setPdfFileName(getText("farmerList") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(getPdfFileName(), is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("farmerList"), fileMap, ".pdf"));
		return "pdf";
	}

	public InputStream getPDFExportDataStream(List<Object> obj, String entityName, Map<String, Map> fieldMaps)
			throws IOException, DocumentException, NoSuchFieldException, SecurityException {

		Font cellFont = null; // font for cells.
		Font titleFont = null; // font for title text.
		Paragraph title = null; // to add title for report
		String columnHeaders = null; // to hold column headers from property
										// file.
		String[] objectFieldNames = null; // to hold property (field) names of
											// Object obj, to be got from
											// property file.
		List<Object[]> entityFieldsList = new ArrayList<Object[]>(); // to hold
																		// properties
																		// of
																		// entity
																		// object
																		// passed
																		// as
																		// list.
		Object entityObject = null; // to hold the entity object type.
		Map<String, Map> tempFieldMaps = null; // to hold field Maps in
												// iteration temporarily.
		List<String> fieldNamesList = new ArrayList<String>(); // to iterate
																// properties of
																// map.
		Iterator<String> iterator; // iterator for looping.
		String iteratorValue = null;
		int objectFieldIndex = 0;
		SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		
		Document document = new Document();

		String makeDir = FileUtil.storeXls(request.getSession().getId());
		String fileName = getText(entityName + "ListFile") + fileNameDateFormat.format(new Date()) + ".pdf";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		PdfWriter.getInstance(document, fileOut);

		document.open();

		PdfPTable table = null;

		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap();

		com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance(exportLogo());
		logo.scaleAbsolute(100, 50);
		; // resizing logo image size.

		document.add(logo); // Adding logo in PDF file.

		// below of code for title text
		titleFont = new Font(FontFamily.HELVETICA, 14, Font.BOLD, GrayColor.GRAYBLACK);
		title = new Paragraph(new Phrase(getText(entityName + "ExportPDFTitle"), titleFont));
		title.setAlignment(Element.ALIGN_CENTER);
		document.add(title);
		document.add(
				new Paragraph(new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK)))); // Add
																														// a
																														// blank
																														// line
																														// after
																														// title.

		columnHeaders = columnHeaders(entityName);

		if (!ObjectUtil.isEmpty(columnHeaders)) {
			if (!ObjectUtil.isEmpty(obj)) {
				PdfPCell cell = null; // cell for table.
				cellFont = new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK); // setting
																								// font
																								// for
																								// header
																								// cells
																								// of
																								// table.
				table = new PdfPTable(columnHeaders.split("\\,").length); // Code
																			// for
																			// setting
																			// table
																			// column
																			// Numbers.
				table.setWidthPercentage(100); // Set Table Width.
				table.getDefaultCell().setUseAscender(true);
				table.getDefaultCell().setUseDescender(true);
				for (String cellHeader : columnHeaders.split("\\,")) {
					cell = new PdfPCell(new Phrase(cellHeader, cellFont));
					cell.setBackgroundColor(new BaseColor(144,238,144));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setNoWrap(false); // To set wrapping of text in cell.
					// cell.setColspan(3); // To add column span.
					table.addCell(cell);
				}

				entityObject = obj.get(0); // set the class type for this entity
											// object property of this class.
				cellFont = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK); // setting
																								// font
																								// for
				// cells.

				if (getCurrentTenantId().equalsIgnoreCase("iccoa"))
					entityName = "FarmerICCOA";
				if (getCurrentTenantId().equalsIgnoreCase("avt"))
					entityName = "FarmerAVT";
				if (getCurrentTenantId().equalsIgnoreCase("kenyafpo"))
					entityName = "FarmerKenyafpo";
				objectFieldNames = objectFieldNames(entityName);
				entityFieldsList = buildPDFData(obj, objectFieldNames);
				if (!ObjectUtil.isEmpty(entityFieldsList)) { // Check if utility
																// returns a
																// null list
																// object.
					for (Object[] entityObj : entityFieldsList) { // iterate
																	// over all
																	// list of
																	// objects.
						tempFieldMaps = new LinkedHashMap<String, Map>(fieldMaps);
						fieldNamesList = new ArrayList<String>(tempFieldMaps.keySet());
						iterator = fieldNamesList.iterator();
						for (Object entityField : entityObj) {

							/*
							 * BEGIN of Code to iterate over maps passed in arg
							 * for fields
							 */
							for (; iterator.hasNext();) { // Iterate over map
															// key.
								iteratorValue = iterator.next();
								String fieldName = iteratorValue.toString();
								fieldName = StringUtil.substringLast(fieldName, ".");
								if (objectFieldNames[objectFieldIndex].equals(fieldName)) { // If
																							// equals
																							// the
																							// change
																							// entity
																							// field
																							// to
																							// the
																							// map
																							// value.

									Map propertyValue = tempFieldMaps.get(iteratorValue);
									entityField = propertyValue.get(entityField);
									iterator.remove();
									break;
								}
							}
							if (ObjectUtil.isEmpty(entityField)) {
								entityField = " ";
							}
							iterator = fieldNamesList.listIterator(); // reset
																		// iterator
																		// to
																		// start
																		// element.
							/*
							 * END of Code to iterate over maps passed in arg
							 * for fields
							 */

							// BEGIN OF CODE FOR A PARTICULAR CELL IN A ROW OF
							// TABLE TO PDF FILE.
							cell = new PdfPCell(new Phrase(
									StringUtil.isEmpty(entityField.toString()) || "-1.0".equalsIgnoreCase(entityField.toString())  ? getText("NA") : entityField.toString(),
									cellFont));
							table.addCell(cell);
							// END OF CODE FOR A PARTICULAR CELL IN A ROW OF
							// TABLE TO PDF FILE.

							if (objectFieldIndex < (columnHeaders.split("\\,").length - 1)) {
								objectFieldIndex++;
							} else {
								objectFieldIndex = 0;

							}

						}

					}

					document.add(table); // Add table to document.
				}
			}
		}

		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		document.close();
		fileOut.close();
		return stream;
	}

	public String columnHeaders(String entityName) {

		String columnHeaders1 = null; // String to get column headers from
										// property file.
		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {
				columnHeaders1 = getLocaleProperty("exportFarmerHeadingBranch");
			} else if (!StringUtil.isEmpty(branchIdValue)) {
				columnHeaders1 = getLocaleProperty("exportFarmerHeading");
			}
		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				columnHeaders1 = getLocaleProperty("exportFarmerHeadingBranch");
			} else {
				columnHeaders1 = getLocaleProperty("exportFarmerHeading");
			}
		}
		if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {

			if (StringUtil.isEmpty(branchIdValue)) {
				columnHeaders1 = getLocaleProperty("exportFarmerHeadingOrganicPratibha");
			} else if (getBranchId().equalsIgnoreCase("organic")) {
				columnHeaders1 = getLocaleProperty("exportFarmerHeadingOrganic");
			} else if (getBranchId().equalsIgnoreCase("bci")) {
				columnHeaders1 = getLocaleProperty("exportFarmerHeadingBciData");
			}

		}

		if (getCurrentTenantId().equalsIgnoreCase("welspun") || getCurrentTenantId().equalsIgnoreCase("ocp")) {

			if (StringUtil.isEmpty(branchIdValue)) {
				columnHeaders1 = getLocaleProperty("exportFarmerColumnHeaderBranchWelspun");
			} else if (getIsParentBranch().equalsIgnoreCase("1")) {
				columnHeaders1 = getLocaleProperty("ParentFarmerExportHeader");
			} else
				columnHeaders1 = getLocaleProperty("FarmerExportHeader");

		}
		if (getCurrentTenantId().equalsIgnoreCase("ecoagri")) {
			columnHeaders1 = getLocaleProperty("FarmerExportHeader");
		}
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.SYMRISE)) {
			columnHeaders1 = getLocaleProperty("FarmerExportHeader");
		}

		/*
		 * if (getCurrentTenantId().equalsIgnoreCase("pratibha") &&
		 * getBranchId().equalsIgnoreCase("organic")) { columnHeaders1 =
		 * getLocaleProperty("exportFarmerHeadingOrganic"); }
		 */

		return columnHeaders1;
	}

	public String getFpoEnabled() {

		return fpoEnabled;
	}

	public void setFpoEnabled(String fpoEnabled) {

		this.fpoEnabled = fpoEnabled;
	}

	public String getFpoList() {
		StringBuffer sb = new StringBuffer();
		List<FarmCatalogue> fpoList = farmerService.listCatelogueType(getText("fpoType"));
		sb.append("-1:").append(FILTER_ALL).append(";");
		for (FarmCatalogue farmCatalogue : fpoList) {
			sb.append(farmCatalogue.getCode()).append(":").append(farmCatalogue.getName()).append(";");
		}
		String data = sb.toString();
		return data.substring(0, data.length() - 1);
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
	/*
	 * public String getFpoFarmerFilterText() { List<FarmCatalogue> fpoList =
	 * farmerService.listCatelogueType(getText("fpoType"));
	 * 
	 * StringBuffer sbf = new StringBuffer(); for (FarmCatalogue catalogue :
	 * fpoList) { sbf.append(catalogue.getName()); } //name =sbf.toString();
	 * String data = sbf.toString(); return data.substring(0, data.length() -
	 * 1); }
	 */

	/*
	 * public Map<Long, String> getShgList() {
	 * 
	 * shg = locationService.listOfShg(); Map<Long, String> shgMap = new
	 * LinkedHashMap<>(); shgMap =
	 * shg.stream().collect(Collectors.toMap(Shg::getId, Shg::getShgName));
	 * return shgMap; }
	 */

	public Map<Integer, String> getBeneficiaryInGovSchemelist() {

		return beneficiaryInGovSchemelist;
	}

	public void setBeneficiaryInGovSchemelist(Map<Integer, String> beneficiaryInGovSchemelist) {

		this.beneficiaryInGovSchemelist = beneficiaryInGovSchemelist;
	}

	public Map<Integer, String> getStatusList() {

		return statusList;
	}

	public void setStatusList(Map<Integer, String> statusList) {

		this.statusList = statusList;
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

	public Map<String, String> getSanghamList() {
		Map<String, String> sanghamList = new LinkedHashMap<>();

		sanghamList = getFarmCatalougeMap(Integer.valueOf(getText("sangamType")));
		return sanghamList;

	}

	public Map<String, String> getPositionGroupList() {

		Map<String, String> groupPositionList = new LinkedHashMap<>();

		groupPositionList = getFarmCatalougeMap(Integer.valueOf(getText("groupPositiontype")));
		return groupPositionList;

	}

	public void populateSangham() throws Exception {
		if (!getCurrentTenantId().equalsIgnoreCase("atma")) {
			if (!selectedVillage.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedVillage))
					&& !StringUtil.isEmpty(selectedSangham)) {
				Village village = locationService.findVillageById(Long.valueOf(selectedVillage));
				JSONArray warehouseSanhamArr = null;
				if (!ObjectUtil.isEmpty(selectedSangham) && !ObjectUtil.isEmpty(village))
					samithi = locationService.listSamithiBySangham(selectedSangham, village.getCode());

				if (!ObjectUtil.isEmpty(samithi)) {
					warehouseSanhamArr = new JSONArray();
					for (Warehouse warehouse : samithi) {
						warehouseSanhamArr.add(getJSONObject(warehouse.getCode(), warehouse.getName()));
					}
				}
				sendAjaxResponse(warehouseSanhamArr);

			}
		} /*
			 * else {
			 * 
			 * if (!ObjectUtil.isEmpty(selectedSangham)) { // samithi = //
			 * locationService.listSamithiByVillageId(village.getId()); samithi
			 * = locationService.listSamithiBySanghamType(selectedSangham);
			 * JSONArray warehouseArr = null; if (!ObjectUtil.isEmpty(samithi))
			 * { warehouseArr = new JSONArray(); for (Warehouse warehouse :
			 * samithi) { warehouseArr.add( getJSONObject(warehouse.getCode(),
			 * warehouse.getCode() + "-" + warehouse.getName())); } }
			 * sendAjaxResponse(warehouseArr); } }
			 */
	}

	public String getEnableHHCode() {

		return enableHHCode;
	}

	public void setEnableHHCode(String enableHHCode) {

		this.enableHHCode = enableHHCode;
	}

	public String getSanghamName() {

		return sanghamName;
	}

	public void setSanghamName(String sanghamName) {

		this.sanghamName = sanghamName;
	}

	/*
	 * public Map<String, String> getSocialCategoryList() {
	 * 
	 * AtomicInteger i = new AtomicInteger(0); Map<String, String>
	 * socialCategoryMap = new LinkedHashMap<>();
	 * 
	 * socialCategoryMap =
	 * getFarmCatalougeMap(Integer.valueOf(getText("socialCatType"))); return
	 * socialCategoryMap;
	 * 
	 * }
	 */

	public Map<String, String> getReligionList() {

		AtomicInteger i = new AtomicInteger(0);
		Map<String, String> religionMap = new LinkedHashMap<>();
		religionMap = getFarmCatalougeMap(Integer.valueOf(getText("religType")));
		return religionMap;

	}

	public Map<String, String> getFamilyTypeList() {

		AtomicInteger i = new AtomicInteger(0);
		Map<String, String> familyTypeMap = new LinkedHashMap<>();
		familyTypeMap = getFarmCatalougeMap(Integer.valueOf(getText("familyMemType")));
		return familyTypeMap;

	}

	/*
	 * public Map<String, String> getHouseHoldLandList() {
	 * 
	 * AtomicInteger i = new AtomicInteger(0); Map<String, String>
	 * houseHoldLandMap = new LinkedHashMap<>();
	 * 
	 * List<FarmCatalogue> farmCatalougeList = catalogueService
	 * .findFarmCatalougeByType(Integer.valueOf(getText(""))); for
	 * (FarmCatalogue catalogue : farmCatalougeList) {
	 * houseHoldLandMap.put(catalogue.getCode(), catalogue.getName());
	 * 
	 * } return houseHoldLandMap; }
	 */
	public Map<String, String> getHouseHoldOccupationList() {

		AtomicInteger i = new AtomicInteger(0);
		Map<String, String> houseHoldOccupationMap = new LinkedHashMap<>();

		houseHoldOccupationMap = getFarmCatalougeMap(Integer.valueOf(getText("houseHoldLdType")));
		return houseHoldOccupationMap;
	}

	public Map<String, String> getFamilyMemberList() {

		AtomicInteger i = new AtomicInteger(0);
		Map<String, String> familyMemberMap = new LinkedHashMap<>();

		familyMemberMap = getFarmCatalougeMap(Integer.valueOf(getText("familyMemberType")));
		return familyMemberMap;

	}

	/*
	 * public Map<String, String> getSourceNameList() {
	 * 
	 * AtomicInteger i = new AtomicInteger(0); Map<String, String> sourceNameMap
	 * = new LinkedHashMap<>();
	 * 
	 * List<FarmCatalogue> farmCatalougeList =
	 * catalogueService.findFarmCatalougeByType(Integer.valueOf(getText(
	 * "sourceName"))); for (FarmCatalogue catalogue : farmCatalougeList) {
	 * sourceNameMap.put(catalogue.getCode(), catalogue.getName()); }
	 * 
	 * return sourceNameMap; }
	 */

	public Map<String, String> getInvestigatorNameList() {

		Map<String, String> returnMap = new LinkedHashMap<String, String>();
		List<Agent> agentLists = agentService.listAgentByAgentType(AgentType.FIELD_STAFF);
		if (!ObjectUtil.isListEmpty(agentLists)) {
			for (Agent agent : agentLists) {
				returnMap.put(agent.getProfileId(), agent.getPersonalInfo().getFirstName() + " "
						+ agent.getPersonalInfo().getLastName() + " - " + agent.getProfileId());
			}
		}

		return returnMap;
	}

	public String getHouseHoldDob() {

		return houseHoldDob;
	}

	public void setHouseHoldDob(String houseHoldDob) {

		this.houseHoldDob = houseHoldDob;
	}

	public String getInvestigatorDate() {

		return investigatorDate;
	}

	public void setInvestigatorDate(String investigatorDate) {

		this.investigatorDate = investigatorDate;
	}

	public Map<String, String> getSourceNameMap() {

		return sourceNameMap;
	}

	public void setSourceNameMap(Map<String, String> sourceNameMap) {

		this.sourceNameMap = sourceNameMap;
	}

	public Map<Integer, String> getAliedSectorMap() {

		return aliedSectorMap;
	}

	public void setAliedSectorMap(Map<Integer, String> aliedSectorMap) {

		this.aliedSectorMap = aliedSectorMap;
	}

	public Map<Integer, String> getEmploymentMap() {

		return employmentMap;
	}

	public void setEmploymentMap(Map<Integer, String> employmentMap) {

		this.employmentMap = employmentMap;
	}

	public Map<Integer, String> getOtherIncomeMap() {

		return otherIncomeMap;
	}

	public void setOtherIncomeMap(Map<Integer, String> otherIncomeMap) {

		this.otherIncomeMap = otherIncomeMap;
	}

	public String getFamilyMember() {

		return familyMember;
	}

	public void setFamilyMember(String familyMember) {

		this.familyMember = familyMember;
	}

	public String getIfs() {

		return ifs;
	}

	public void setIfs(String ifs) {

		this.ifs = ifs;
	}

	public String getAgriIncome() {

		return agriIncome;
	}

	public void setAgriIncome(String agriIncome) {

		this.agriIncome = agriIncome;
	}

	public String getHortiIncome() {

		return hortiIncome;
	}

	public void setHortiIncome(String hortiIncome) {

		this.hortiIncome = hortiIncome;
	}

	public String getAliedIncome() {

		return aliedIncome;
	}

	public void setAliedIncome(String aliedIncome) {

		this.aliedIncome = aliedIncome;
	}

	public String getEmploymentIncome() {

		return employmentIncome;
	}

	public void setEmploymentIncome(String employmentIncome) {

		this.employmentIncome = employmentIncome;
	}

	public String getOtherIncome() {

		return otherIncome;
	}

	public void setOtherIncome(String otherIncome) {

		this.otherIncome = otherIncome;
	}

	/*
	 * public void setSocialCategoryList(Map<Integer, String>
	 * socialCategoryList) {
	 * 
	 * this.socialCategoryList = socialCategoryList; }
	 */
	public void setReligionList(Map<Integer, String> religionList) {

		this.religionList = religionList;
	}

	public void setFamilyTypeList(Map<Integer, String> familyTypeList) {

		this.familyTypeList = familyTypeList;
	}

	public void setHouseHoldLandList(Map<Integer, String> houseHoldLandList) {

		this.houseHoldLandList = houseHoldLandList;
	}

	public void setHouseHoldOccupationList(Map<Integer, String> houseHoldOccupationList) {

		this.houseHoldOccupationList = houseHoldOccupationList;
	}

	public void setFamilyMemberList(Map<Integer, String> familyMemberList) {

		this.familyMemberList = familyMemberList;
	}

	public void setInvestigatorNameList(Map<Integer, String> investigatorNameList) {

		this.investigatorNameList = investigatorNameList;
	}

	public String getSourceIncomeId() {

		return sourceIncomeId;
	}

	public void setSourceIncomeId(String sourceIncomeId) {

		this.sourceIncomeId = sourceIncomeId;
	}

	public List<FarmerSourceIncome> getFarmerSourceIncomeList() {

		return farmerSourceIncomeList;
	}

	public void setFarmerSourceIncomeList(List<FarmerSourceIncome> farmerSourceIncomeList) {

		this.farmerSourceIncomeList = farmerSourceIncomeList;
	}

	/*
	 * public List<FarmerIncomeDetails> getIncomeDetailsList() { return
	 * incomeDetailsList; } public void
	 * setIncomeDetailsList(List<FarmerIncomeDetails> incomeDetailsList) {
	 * this.incomeDetailsList = incomeDetailsList; }
	 */

	public Set<FarmerSourceIncome> sourceOfIncomeSet() {

		Set<FarmerSourceIncome> sourceOfIncomeSet = new LinkedHashSet<>();
		// Set<FarmerIncomeDetails> sourceOfIncomeDetailSet = new
		// LinkedHashSet<>();
		if (!StringUtil.isEmpty(agriTestJson)) {
			Type listType1 = new TypeToken<List<FarmerSourceIncome>>() {
			}.getType();

			Type listType2 = new TypeToken<List<FarmerIncomeDetails>>() {
			}.getType();

			List<FarmerSourceIncome> farmerSourceIncomeList = new Gson().fromJson(agriTestJson, listType1);

			List<FarmerIncomeDetails> farmerIncomeDetails = new Gson().fromJson(agriTestJson, listType2);

			// List<FarmerSourceIncome> temp =
			// farmerSourceIncomeList.stream().filter(o->farmerSourceIncomeList.stream().filter(x->x.getName()==o.getName()).count()<1).collect(Collectors.toList());

			List<FarmerSourceIncome> listTemp = removeDuplicates(farmerSourceIncomeList);
			List<FarmerIncomeDetails> listTempIncome = removeDuplicatesForIncome(farmerIncomeDetails);
			for (FarmerSourceIncome farmerSourceIncome : listTemp) {
				// farmerSourceIncome.setName(removeDuplicates(farmerSourceIncomeList));

				Set<FarmerIncomeDetails> sourceOfIncomeDetailSet1 = new LinkedHashSet<>();
				sourceOfIncomeDetailSet1.addAll(
						listTempIncome.stream().filter(obj -> (farmerSourceIncome.getName().equals(obj.getName())))
								.collect(Collectors.toList()));
				farmerSourceIncome.setFarmerIncomeDetails(sourceOfIncomeDetailSet1);
				sourceOfIncomeSet.add(farmerSourceIncome);
			}

			/*
			 * sourceOfIncomeDetailSet.addAll(farmerIncomeDetails); if
			 * (!ObjectUtil.isListEmpty(farmerSoilTestingList)) {
			 * farmerSoilTestingList.get(0).setFarmerIncomeDetails(
			 * sourceOfIncomeDetailSet); }
			 * sourceOfIncomeSet.addAll(farmerSoilTestingList);
			 */
		}

		return sourceOfIncomeSet;
	}

	public static ArrayList<FarmerSourceIncome> removeDuplicates(List<FarmerSourceIncome> farmerSourceIncomeList) {

		ArrayList<FarmerSourceIncome> result = new ArrayList<>();

		HashSet<String> set = new HashSet<>();
		for (FarmerSourceIncome item : farmerSourceIncomeList) {
			if (!set.contains(item.getName())) {
				// item.setName(item.getName());
				result.add(item);
				set.add(item.getName());
			}
		}
		return result;
	}

	public static ArrayList<FarmerIncomeDetails> removeDuplicatesForIncome(
			List<FarmerIncomeDetails> farmerSourceIncomeList) {

		ArrayList<FarmerIncomeDetails> result = new ArrayList<>();

		HashMap<String, Integer> set = new HashMap<>();
		int i = 0;
		for (FarmerIncomeDetails item : farmerSourceIncomeList) {
			if (!set.containsKey(item.getName() + "," + item.getSourceName() + "," + item.getSourceNameOther())) {
				// item.setName(item.getName());
				result.add(i, item);
				set.put(item.getName() + "," + item.getSourceName() + "," + item.getSourceNameOther(), i);
				i++;
			} else {
				Integer index = set.get(item.getName() + "," + item.getSourceName() + "," + item.getSourceNameOther());
				FarmerIncomeDetails fin = result.get(index);
				fin.setSourceIncome(fin.getSourceIncome() + item.getSourceIncome());

				result.set(index, fin);
			}
		}
		return result;
	}

	public String getAgriTestJson() {

		return agriTestJson;
	}

	public void setAgriTestJson(String agriTestJson) {

		this.agriTestJson = agriTestJson;
	}

	public String getInvestigatorName() {

		return investigatorName;
	}

	public void setInvestigatorName(String investigatorName) {

		this.investigatorName = investigatorName;
	}

	public Map<Integer, String> getListInsurance() {
		listInsurance.put(1, getText("insure1"));
		listInsurance.put(2, getText("insure0"));

		return listInsurance;
	}

	public int getFarmerLifeInsuranceDefaultValue() {

		return (ObjectUtil.isEmpty(this.farmer) ? 2 : Integer.parseInt(this.farmer.getLifeInsurance()));
	}

	public int getFarmerHealthInsuranceDefaultValue() {

		return (ObjectUtil.isEmpty(this.farmer) ? 2 : Integer.parseInt(this.farmer.getHealthInsurance()));
	}

	public Map<String, String> getCookingFuelMap() {
		cookingFuelMap = getFarmCatalougeMap(Integer.valueOf(getText("cookingFuel")));
		cookingFuelMap.put("99", getText("farmer.cookingFuelOther"));
		return cookingFuelMap;

	}

	public Map<String, String> getHomeDifficultyMap() {

		Map<String, String> groupPositionList = new LinkedHashMap<>();

		groupPositionList = getFarmCatalougeMap(Integer.valueOf(getText("homeDiffType").trim()));
		return groupPositionList;

	}

	public Map<String, String> getWorkDiffficultyMap() {

		Map<String, String> groupPositionList = new LinkedHashMap<>();

		groupPositionList = getFarmCatalougeMap(Integer.valueOf(getText("workDiffType").trim()));
		return groupPositionList;

	}

	public Map<String, String> getCommunitiyDifficultyMap() {
		Map<String, String> groupPositionList = new LinkedHashMap<>();

		groupPositionList = getFarmCatalougeMap(Integer.valueOf(getText("commDiffType").trim()));
		return groupPositionList;

	}

	public Map<String, String> getGovtDeptMap() {

		Map<String, String> groupPositionList = new LinkedHashMap<>();

		groupPositionList = getFarmCatalougeMap(Integer.valueOf(getText("govtDept").trim()));
		return groupPositionList;
	}

	public Map<Integer, String> getHeadOfFamilyList() {

		return headOfFamilyList;
	}

	public void setHeadOfFamilyList(Map<Integer, String> headOfFamilyList) {

		this.headOfFamilyList = headOfFamilyList;
	}

	public Map<Integer, String> getIsToiletAvailable() {

		return isToiletAvailable;
	}

	public void setIsToiletAvailable(Map<Integer, String> isToiletAvailable) {

		this.isToiletAvailable = isToiletAvailable;
	}

	public Map<Integer, String> getToiletAvailableFromMap() {

		return toiletAvailableFromMap;
	}

	public void setToiletAvailableFromMap(Map<Integer, String> toiletAvailableFromMap) {

		this.toiletAvailableFromMap = toiletAvailableFromMap;
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

	public Map<String, String> getIcsList() {

		Map<String, String> educationMap = new LinkedHashMap<String, String>();

		List<FarmCatalogue> educationList = farmerService.listCatelogueTypeWithOther(getText("icsNameType"));
		for (FarmCatalogue obj : educationList) {
			educationMap.put(obj.getCode(), obj.getName());
		}

		return educationMap;

	}

	public Map<String, String> getIcsUnit() {

		Map<String, String> educationMap = new LinkedHashMap<String, String>();

		List<FarmCatalogue> educationList = farmerService.listCatelogueTypeWithOther(getText("icsUnitType"));
		for (FarmCatalogue obj : educationList) {
			educationMap.put(obj.getCode(), obj.getName());
		}

		return educationMap;

	}

	public Map<String, String> getIcsRegNo() {

		Map<String, String> educationMap = new LinkedHashMap<String, String>();

		List<FarmCatalogue> educationList = farmerService.listCatelogueTypeWithOther(getText("icsRegNoType"));
		for (FarmCatalogue obj : educationList) {
			educationMap.put(obj.getCode(), obj.getName());
		}

		return educationMap;

	}

	public Map<String, String> getLoanSecurityList() {

		Map<String, String> educationMap = new LinkedHashMap<String, String>();

		List<FarmCatalogue> educationList = farmerService.listCatelogueTypeWithOther(getText("loanSecurityType"));
		for (FarmCatalogue obj : educationList) {
			educationMap.put(obj.getCode(), obj.getName());
		}

		return educationMap;
	}

	public void setLoanSecurityList(Map<String, String> loanSecurityList) {

		this.loanSecurityList = loanSecurityList;
	}

	public Map<String, String> getLoanPurposeList() {

		Map<String, String> educationMap = new LinkedHashMap<String, String>();

		List<FarmCatalogue> educationList = farmerService.listCatelogueTypeWithOther(getText("loanPurposeType"));
		for (FarmCatalogue obj : educationList) {
			educationMap.put(obj.getCode(), obj.getName());
		}

		return educationMap;

	}

	public void setLoanPurposeList(Map<String, String> loanPurposeList) {

		this.loanPurposeList = loanPurposeList;
	}

	public Map<Integer, String> getInteresetPeriod() {

		return interesetPeriod;
	}

	public void setInteresetPeriod(Map<Integer, String> interesetPeriod) {

		this.interesetPeriod = interesetPeriod;
	}

	public Map<String, String> getFarmerCropNames() {

		List<ProcurementProduct> cropsList = productDistributionService.listProcurementProduct();
		for (ProcurementProduct farmCrop : cropsList) {
			farmerCropNames.put(farmCrop.getCode(), farmCrop.getName());
		}
		return farmerCropNames;
	}

	public IProductDistributionService getProductDistributionService() {

		return productDistributionService;
	}

	public void setProductDistributionService(IProductDistributionService productDistributionService) {

		this.productDistributionService = productDistributionService;
	}

	public String getIdProofEnabled() {

		return idProofEnabled;
	}

	public void setIdProofEnabled(String idProofEnabled) {

		this.idProofEnabled = idProofEnabled;
	}

	public Map<String, String> getProofList() {

		Map<String, String> groupPositionList = new LinkedHashMap<>();

		groupPositionList = getFarmCatalougeMap(Integer.valueOf(getText("idProof")));
		if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)) {
			groupPositionList.put("99", "Others");
		}
		return groupPositionList;
	}

	public String getFarmerBankInfoEnabled() {

		return farmerBankInfoEnabled;
	}

	public void setFarmerBankInfoEnabled(String farmerBankInfoEnabled) {

		this.farmerBankInfoEnabled = farmerBankInfoEnabled;
	}

	public int getCropInsuranceDefaultValue() {

		return (ObjectUtil.isEmpty(this.farmer) ? 2 : Integer.valueOf((this.farmer.getIsCropInsured())));
	}

	public String getExportLimit() {

		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			exportLimit = preferences.getPreferences().get(ESESystem.EXPORT_RECORD_LIMIT);
		}
		return exportLimit;
	}

	public void setExportLimit(String exportLimit) {

		this.exportLimit = exportLimit;
	}

	public String getInsuranceInfoEnabled() {

		return insuranceInfoEnabled;
	}

	public void setInsuranceInfoEnabled(String insuranceInfoEnabled) {

		this.insuranceInfoEnabled = insuranceInfoEnabled;
	}

	public Map<String, String> getAccountTypeList() {

		Map<String, String> bankList = new LinkedHashMap<>();

		bankList = getFarmCatalougeMap(Integer.valueOf(getText("bankInformation")));
		return bankList;
	}

	public Map<String, String> getBankNameList() {

		Map<String, String> branchList = new LinkedHashMap<>();

		branchList = getFarmCatalougeMap(Integer.valueOf(getText("bankName")));
		return branchList;
	}

	public Map<String, String> getBranchNameList() {

		Map<String, String> branchNames = new LinkedHashMap<>();

		branchNames = getFarmCatalougeMap(Integer.valueOf(getText("branchName")));
		return branchNames;
	}

	public String getAccTypeString() {

		return accTypeString;
	}

	public void setAccTypeString(String accTypeString) {

		this.accTypeString = accTypeString;
	}

	public Map<Integer, String> getGrsList() {

		return grsList;
	}

	public void setGrsList(Map<Integer, String> grsList) {

		this.grsList = grsList;
	}

	public Map<Integer, String> getPaidShareCapitialList() {

		return paidShareCapitialList;
	}

	public void setPaidShareCapitialList(Map<Integer, String> paidShareCapitialList) {

		this.paidShareCapitialList = paidShareCapitialList;
	}

	public String getBankAccType() {
		return bankAccType;
	}

	public void setBankAccType(String bankAccType) {
		this.bankAccType = bankAccType;
	}

	public String getConsElecName() {

		return consElecName;
	}

	public void setConsElecName(String consElecName) {

		this.consElecName = consElecName;
	}

	public String getLoanRepaymentDate() {

		return loanRepaymentDate;
	}

	public void setLoanRepaymentDate(String loanRepaymentDate) {

		this.loanRepaymentDate = loanRepaymentDate;
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
		reLocalizeMenu();
		request.setAttribute("heading", getText("dashboardlist"));

		branchIdValue = getBranchId();
		if (StringUtil.isEmpty(branchIdValue)) {
			buildBranchMap();
		}

		Integer totalFarmersCount = !StringUtil.isEmpty(farmerService.findFarmersCountByStatus())
				? farmerService.findFarmersCountByStatus() : 0;

		Integer currentMonthFarmerCount = !StringUtil
				.isEmpty(farmerService.findFarmerCountByMonth(DateUtil.getFirstDateOfMonth(), new Date()))
						? farmerService.findFarmerCountByMonth(DateUtil.getFirstDateOfMonth(), new Date()) : 0;

		Integer previousMonthFarmerCount = !StringUtil.isEmpty(
				farmerService.findFarmerCountByMonth(DateUtil.getLastMonthStartDate(), DateUtil.getLastMonthEndDate()))
						? farmerService.findFarmerCountByMonth(DateUtil.getLastMonthStartDate(),
								DateUtil.getLastMonthEndDate())
						: 0;

		Double currentMonthFarmerPercentage = !StringUtil.isEmpty((Double.parseDouble(String.valueOf(totalFarmersCount))
				* Double.parseDouble(String.valueOf(currentMonthFarmerCount))) / 100)
						? (Double.parseDouble(String.valueOf(totalFarmersCount))
								* Double.parseDouble(String.valueOf(currentMonthFarmerCount))) / 100
						: 0;

		Double prevMonthFarmerPercentage = !StringUtil.isEmpty((Double.parseDouble(String.valueOf(totalFarmersCount))
				* Double.parseDouble(String.valueOf(previousMonthFarmerCount))) / 100)
						? (Double.parseDouble(String.valueOf(totalFarmersCount))
								* Double.parseDouble(String.valueOf(previousMonthFarmerCount))) / 100
						: 0;

		jsonObject.put("farmerCount", String.valueOf(totalFarmersCount));
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
			jsonObject.put("cowText", textClassAsc);
		}

		Integer totalFarmCount = !StringUtil.isEmpty(farmerService.findFarmsCount()) ? farmerService.findFarmsCount()
				: 0;

		Integer currentMonthFarmCount = !StringUtil
				.isEmpty(farmerService.findFarmCountByMonth(DateUtil.getFirstDateOfMonth(), new Date()))
						? farmerService.findFarmCountByMonth(DateUtil.getFirstDateOfMonth(), new Date()) : 0;

		Integer previousMonthFarmCount = !StringUtil.isEmpty(
				farmerService.findFarmCountByMonth(DateUtil.getLastMonthStartDate(), DateUtil.getLastMonthEndDate()))
						? farmerService.findFarmCountByMonth(DateUtil.getLastMonthStartDate(),
								DateUtil.getLastMonthEndDate())
						: 0;

		Double currentMonthFarmPercentage = !StringUtil.isEmpty((Double.parseDouble(String.valueOf(totalFarmCount))
				* Double.parseDouble(String.valueOf(currentMonthFarmCount))) / 100)
						? (Double.parseDouble(String.valueOf(totalFarmCount))
								* Double.parseDouble(String.valueOf(currentMonthFarmCount))) / 100
						: 0D;

		Double prevMonthFarmPercentage = !StringUtil.isEmpty((Double.parseDouble(String.valueOf(totalFarmCount))
				* Double.parseDouble(String.valueOf(previousMonthFarmCount))) / 100)
						? (Double.parseDouble(String.valueOf(totalFarmCount))
								* Double.parseDouble(String.valueOf(previousMonthFarmCount))) / 100
						: 0D;

		jsonObject.put("farmCount", String.valueOf(totalFarmCount));
		if ((currentMonthFarmPercentage) < (prevMonthFarmPercentage)) {
			jsonObject.put("farmCountPercentage", String.valueOf(currentMonthFarmPercentage));
			jsonObject.put("farmCountstauts", descStatus);
			jsonObject.put("farmText", textClassDesc);
		} else {
			jsonObject.put("farmCountPercentage", String.valueOf(currentMonthFarmPercentage));
			jsonObject.put("farmCountstauts", ascStatus);
			jsonObject.put("farmText", textClassAsc);
		}

		Integer totalFarmCropCount = farmerService.findFarmCropCount();
		Integer currentMonthFarmCropCount = farmerService.findFarmCropCountByMonth(DateUtil.getFirstDateOfMonth(),
				new Date());
		Integer previousMonthFarmCropCount = farmerService.findFarmCropCountByMonth(DateUtil.getLastMonthStartDate(),
				DateUtil.getLastMonthEndDate());
		Double currentMonthFarmCropPercentage = (Double.parseDouble(String.valueOf(totalFarmCropCount))
				* Double.parseDouble(String.valueOf(currentMonthFarmCropCount))) / 100;
		Double prevMonthFarmCropPercentage = (Double.parseDouble(String.valueOf(totalFarmCropCount))
				* Double.parseDouble(String.valueOf(previousMonthFarmCropCount))) / 100;
		jsonObject.put("farmCropCount", String.valueOf(totalFarmCropCount));
		if ((currentMonthFarmCropPercentage) < (prevMonthFarmCropPercentage)) {
			jsonObject.put("farmCropCountPercentage", String.valueOf(currentMonthFarmCropPercentage));
			jsonObject.put("farmCropCountstauts", descStatus);
			jsonObject.put("farmCropText", textClassDesc);
		} else {
			jsonObject.put("farmCropCountPercentage", String.valueOf(currentMonthFarmCropPercentage));
			jsonObject.put("farmCropCountstauts", ascStatus);
			jsonObject.put("farmCropText", textClassAsc);
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

		// System.out.println(currentMonthUserCount + "-" + prevMonthUserCount);

		jsonObjects.add(jsonObject);
		printAjaxResponse(jsonObjects, "text/html");

	}

	@SuppressWarnings("unchecked")
	public String populateQrCode() throws ParseException, IOException {
		response.setContentType("application/vnd.ms-excel");
		// response.setHeader("Content-Disposition",
		// "attachment;filename=" + getText("farmerList") +
		// fileNameDateFormat.format(new Date()) + ".xls");
		InputStream is = null;
		/*
		 * if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PGSS_TENANT_ID)
		 * && !StringUtil.isEmpty(getType()) && getType().equals(Farmer.IRP)) {
		 * is = getFarmerInputStream(); } else { is =
		 * getFarmerFileInputStream(); }
		 */
		is = getFarmerFileQrCode();
		setXlsFileName(getText("farmerList") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getLocaleProperty("farmerList"), fileMap, ".xls"));

		return "xls";
	}

	public String getFarmerCropInsurance() {

		return farmerCropInsurance;
	}

	public void setFarmerCropInsurance(String farmerCropInsurance) {

		this.farmerCropInsurance = farmerCropInsurance;
	}

	public String getIsCertifiedFarmerInfoEnabled() {
		return isCertifiedFarmerInfoEnabled;
	}

	public void setIsCertifiedFarmerInfoEnabled(String isCertifiedFarmerInfoEnabled) {
		this.isCertifiedFarmerInfoEnabled = isCertifiedFarmerInfoEnabled;
	}

	public String getFarmerPluginFields() {
		return farmerPluginFields;
	}

	public void setFarmerPluginFields(String farmerPluginFields) {
		this.farmerPluginFields = farmerPluginFields;
	}

	public List<FarmerField> getFarmerFields() {
		List<FarmerField> farmerFieldList = farmerService.listRemoveFarmerFields();
		return farmerFieldList;
	}

	public void setFarmerFields(List<FarmerField> farmerFields) {
		this.farmerFields = farmerFields;
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

	public String getCropTypeFilter() {

		StringBuffer sb = new StringBuffer();
		sb.append("-1:").append(FILTER_ALL).append(";");
		String[] values = getLocaleProperty("cropFilterTypes").split("\\,");
		int counter = 0;
		for (String val : values) {
			String concat = (counter++) + ":" + val + ";";
			sb.append(concat);
		}
		String data = sb.toString();
		return data.substring(0, data.length() - 1);
	}

	public Map<String, String> getCatalogueList() {

		Map<String, String> housingTypeList = new LinkedHashMap<>();
		housingTypeList = getFarmCatalougeMap(Integer.valueOf(getText("farmEquip")));
		return housingTypeList;

	}

	public void setCatalogueList(Map<String, String> catalogueList) {
		this.catalogueList = catalogueList;
	}

	public Map<String, String> getCatalogueList1() {
		Map<String, String> housingTypeList = new LinkedHashMap<>();
		housingTypeList = getFarmCatalougeMap(Integer.valueOf(getText("animalFarm")));
		return housingTypeList;
	}

	public void setCatalogueList1(Map<String, String> catalogueList1) {
		this.catalogueList1 = catalogueList1;
	}

	public Map<String, String> getCatalogueList2() {
		Map<String, String> housingTypeList = new LinkedHashMap<>();
		housingTypeList = getFarmCatalougeMap(Integer.valueOf(getText("fodderType")));
		return housingTypeList;
	}

	public void setCatalogueList2(Map<String, String> catalogueList2) {
		this.catalogueList2 = catalogueList2;
	}

	public Map<String, String> getCatalogueList3() {
		Map<String, String> housingTypeList = new LinkedHashMap<>();
		housingTypeList = getFarmCatalougeMap(Integer.valueOf(getText("housingAnimal")));
		return housingTypeList;
	}

	public void setCatalogueList3(Map<String, String> catalogueList3) {
		this.catalogueList3 = catalogueList3;
	}

	public Map<String, String> getCatalogueList4() {
		Map<String, String> housingTypeList = new LinkedHashMap<>();
		housingTypeList = getFarmCatalougeMap(Integer.valueOf(getText("revType")));
		return housingTypeList;
	}

	public void setCatalogueList4(Map<String, String> catalogueList4) {
		this.catalogueList4 = catalogueList4;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getFarmListByFarmer() {
		return farmerService.listFarmInfoByFarmerId(Long.valueOf(id)).stream()
				.collect(Collectors.toMap(obj -> String.valueOf(obj[0]), obj -> String.valueOf(obj[2])));
	}

	public String getFormationDate() {
		return formationDate;
	}

	public void setFormationDate(String formationDate) {
		this.formationDate = formationDate;
	}

	private Set<FarmerDynamicFieldsValue> addFarmerDynamicFieldsSet(Farmer farmer2) {
		Set<FarmerDynamicFieldsValue> dynamicFieldsValuesSet = new HashSet<>();

		if (!StringUtil.isEmpty(dynamicFieldsArray)) {
			try {
				Type listType1 = new TypeToken<List<DynamicData>>() {
				}.getType();
				List<DynamicData> dynamicList = new Gson().fromJson(dynamicFieldsArray, listType1);
				for (DynamicData dynamicData : dynamicList) {
					if (!StringUtil.isEmpty(dynamicData.getValue())
							|| !StringUtil.isEmpty(dynamicData.getImageFile())) {
						FarmerDynamicFieldsValue dynamicFieldsValue = new FarmerDynamicFieldsValue();
						dynamicFieldsValue.setFieldName(dynamicData.getName());
						dynamicFieldsValue.setFieldValue(dynamicData.getValue());
						dynamicFieldsValue.setTypez(Integer.parseInt(dynamicData.getTypez()));
						dynamicFieldsValue.setTxnType(dynamicData.getTxnTypez());
						dynamicFieldsValue.setComponentType(dynamicData.getCompoType());
						dynamicFieldsValue.setCreatedDate(new Date());
						dynamicFieldsValue.setCreatedUser(getUsername());
						dynamicFieldsValue.setTxnUniqueId(DateUtil.getRevisionNumber());
						dynamicFieldsValue.setReferenceId(String.valueOf(farmer2.getId()));

						dynamicFieldsValue.setAccessType(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null
								? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getAccessType() : 0);

						dynamicFieldsValue.setListMethod(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null
								&& fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getCatalogueType() != null
										? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getCatalogueType()
										: "");

						dynamicFieldsValue.setParentId(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null
								&& fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getReferenceId() != null
										? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getReferenceId() : 0);

						if (dynamicData.getCompoType() != null && dynamicData.getCompoType().equals("12")
								&& dynamicData.getImageFile() != null
								&& !StringUtil.isEmpty(dynamicData.getImageFile())) {
							Set<DynamicImageData> imageDataSet = new HashSet<>();
							DynamicImageData dy = new DynamicImageData();
							dy.setImage(Base64.decode(dynamicData.getImageFile().split("base64,")[1]));
							dy.setFarmerDynamicFieldsValue(dynamicFieldsValue);
							dy.setOrder("1");
							imageDataSet.add(dy);
							dynamicFieldsValue.setDymamicImageData(imageDataSet);

						}

						dynamicFieldsValuesSet.add(dynamicFieldsValue);
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return dynamicFieldsValuesSet;

	}

	private Set<FarmerDynamicFieldsValue> editFarmerDynamicFieldsSet(Map<String, List<FarmerDynamicFieldsValue>> fdMap,
			FarmerDynamicData farmerDynamicData, Farmer farmer) {
		Set<FarmerDynamicFieldsValue> dynamicFieldsValuesSet = new HashSet<>();
		dynamicFieldsValuesSet.addAll(farmerDynamicData.getFarmerDynamicFieldsValues());
		if (!StringUtil.isEmpty(dynamicFieldsArray)) {
			try {
				Type listType1 = new TypeToken<List<DynamicData>>() {
				}.getType();
				List<DynamicData> dynamicList = new Gson().fromJson(dynamicFieldsArray, listType1);
				for (DynamicData dynamicData : dynamicList) {
					if (!StringUtil.isEmpty(dynamicData.getValue())
							|| !StringUtil.isEmpty(dynamicData.getImageFile())) {
						FarmerDynamicFieldsValue dynamicFieldsValue = new FarmerDynamicFieldsValue();
						dynamicFieldsValue.setFieldName(dynamicData.getName());
						dynamicFieldsValue.setFieldValue(dynamicData.getValue());
						dynamicFieldsValue.setTypez(Integer.parseInt(dynamicData.getTypez()));
						dynamicFieldsValue.setTxnType(dynamicData.getTxnTypez());
						dynamicFieldsValue.setComponentType(dynamicData.getCompoType());
						dynamicFieldsValue.setCreatedDate(new Date());
						dynamicFieldsValue.setCreatedUser(getUsername());
						dynamicFieldsValue.setTxnUniqueId(DateUtil.getRevisionNumber());
						dynamicFieldsValue.setReferenceId(String.valueOf(farmer.getId()));
						dynamicFieldsValue.setAccessType(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null
								? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getAccessType() : 0);

						dynamicFieldsValue.setListMethod(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null
								&& fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getCatalogueType() != null
										? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getCatalogueType()
										: "");

						dynamicFieldsValue.setParentId(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null
								&& fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getReferenceId() != null
										? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getReferenceId() : 0);

						if (dynamicData.getCompoType() != null
								&& (dynamicData.getCompoType().equals("12") || dynamicData.getCompoType().equals("13"))
								&& dynamicData.getImageFile() != null
								&& !StringUtil.isEmpty(dynamicData.getImageFile())) {
							Set<DynamicImageData> imageDataSet = new HashSet<>();
							DynamicImageData dy = new DynamicImageData();
							dy.setFileExt(dynamicData.getFileExt());

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

							dy.setFarmerDynamicFieldsValue(dynamicFieldsValue);
							dy.setOrder("1");
							imageDataSet.add(dy);
							dynamicFieldsValue.setDymamicImageData(imageDataSet);

						}

						dynamicFieldsValuesSet.add(dynamicFieldsValue);
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return dynamicFieldsValuesSet;

	}

	@SuppressWarnings("unused")
	private Set<FarmerDynamicFieldsValue> updateFarmerDynamicFieldsSet() {
		Set<FarmerDynamicFieldsValue> dynamicFieldsValuesSet = new HashSet<>();

		if (!StringUtil.isEmpty(dynamicFieldsArray)) {
			try {
				Type listType1 = new TypeToken<List<DynamicData>>() {
				}.getType();
				List<DynamicData> dynamicList = new Gson().fromJson(dynamicFieldsArray, listType1);
				for (DynamicData dynamicData : dynamicList) {
					if (!StringUtil.isEmpty(dynamicData.getValue())) {
						FarmerDynamicFieldsValue dynamicFieldsValue = new FarmerDynamicFieldsValue();
						dynamicFieldsValue.setFieldName(dynamicData.getName());
						dynamicFieldsValue.setFieldValue(dynamicData.getValue());
						dynamicFieldsValue.setTypez(Integer.parseInt(dynamicData.getTypez()));
						dynamicFieldsValue.setTxnType(dynamicData.getTxnTypez());
						dynamicFieldsValue.setCreatedDate(new Date());
						dynamicFieldsValue.setCreatedUser(getUsername());
						dynamicFieldsValue.setTxnUniqueId(DateUtil.getRevisionNumber());
						dynamicFieldsValue.setComponentType(dynamicData.getCompoType());
						if (!StringUtil.isEmpty(dynamicData.getId())) {
							dynamicFieldsValue.setId(Long.valueOf(dynamicData.getId()));
						}
						dynamicFieldsValuesSet.add(dynamicFieldsValue);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return dynamicFieldsValuesSet;
		// TODO Auto-generated method stub

	}

	public Map<String, String> getFieldTypeList() {
		String componentsTypes = preferncesService.findPrefernceByName(ESESystem.LABEL_NAMES);
		Map<String, String> labelNameMap = new HashMap<>();
		if (!StringUtil.isEmpty(componentsTypes)) {
			String[] labelArry = componentsTypes.split(",");

			for (int i = 0; i < labelArry.length; i++) {

				String[] compnt = labelArry[i].split("=");
				if (compnt.length > 0) {
					labelNameMap.put(compnt[0], compnt[1]);
				}

			}

		}
		return labelNameMap;

	}

	public List<DynamicSectionConfig> getDynamicFieldsList() {
		return dynamicFieldsList;
	}

	public void setDynamicFieldsList(List<DynamicSectionConfig> dynamicFieldsList) {
		this.dynamicFieldsList = dynamicFieldsList;
	}

	public String getFarmerDynamicValIds() {
		return farmerDynamicValIds;
	}

	public void setFarmerDynamicValIds(String farmerDynamicValIds) {
		this.farmerDynamicValIds = farmerDynamicValIds;
	}

	List<JSONObject> jsonObjects = new ArrayList<JSONObject>();

	@SuppressWarnings("unchecked")
	public void populateDynamicInsertField() {

		List<DynamicSectionConfig> fieldSectionConfigs = farmerService.listDynamicSections();

		if (!ObjectUtil.isEmpty(fieldSectionConfigs)) {
			for (DynamicSectionConfig dynamicSectionConfig : fieldSectionConfigs) {

				for (DynamicFieldConfig dynamicFieldConfig : dynamicSectionConfig.getDynamicFieldConfigs()) {

					JSONObject jsonObject = new JSONObject();
					jsonObject.put("componentName", dynamicFieldConfig.getComponentId());
					jsonObject.put("beforeInsert", "");
					jsonObject.put("afterInsert", "");
					jsonObject.put("componentType", dynamicFieldConfig.getComponentType());
					jsonObject.put("componentId", !StringUtil.isEmpty(dynamicFieldConfig.getComponentId())
							? dynamicFieldConfig.getComponentId().replace(" ", "_") : "");
					jsonObject.put("catType", dynamicFieldConfig.getCatalogueType());
					jsonObjects.add(jsonObject);
				}
			}

		}
		printAjaxResponse(jsonObjects, "text/html");
	}

	@SuppressWarnings("unchecked")
	public void populateCatalogueByType() {
		JSONArray catalogueArr = new JSONArray();
		if (StringUtil.isInteger(getSelectedCatalogue())) {
			Map<String, String> catMap = getFarmCatalougeMap(Integer.parseInt(getSelectedCatalogue()));
			catMap.forEach((k, v) -> {
				catalogueArr.add(getJSONObject(k, v));
			});
		}
		sendAjaxResponse(catalogueArr);
	}

	public String getValidateFileds() {
		return validateFileds;
	}

	public void setValidateFileds(String validateFileds) {
		this.validateFileds = validateFileds;
	}

	/*
	 * public String getDynamicFieldsArray() { return dynamicFieldsArray; }
	 * 
	 * public void setDynamicFieldsArray(String dynamicFieldsArray) {
	 * this.dynamicFieldsArray = dynamicFieldsArray; }
	 */

	public Map<String, String> getDisabilityTypeList() {

		Map<String, String> list = new LinkedHashMap<>();

		list = getFarmCatalougeMap(Integer.valueOf(getText("disability")));
		return list;
	}

	public Map<String, String> getActivityTypeList() {
		Map<String, String> list = new LinkedHashMap<>();

		list = getFarmCatalougeMap(Integer.valueOf(getText("activityType")));
		return list;
	}

	public Map<String, String> getOriginTypesList() {

		originTypes.put("0", getLocaleProperty("originType0"));
		originTypes.put("1", getLocaleProperty("originType1"));
		originTypes.put("2", getLocaleProperty("originType2"));
		return originTypes;
	}

	public String getHealthAssesmentJSON() {
		return healthAssesmentJSON;
	}

	public Map<String, String> getDisabilityList() {
		disabilityList.put("1", getText("no"));
		disabilityList.put("0", getText("yes"));
		return disabilityList;
	}

	public void setDisabilityList(Map<String, String> disabilityList) {
		this.disabilityList = disabilityList;
	}

	public void setHealthAssesmentJSON(String healthAssesmentJSON) {
		this.healthAssesmentJSON = healthAssesmentJSON;
	}

	public String getSelfAssesmentJSON() {
		return selfAssesmentJSON;
	}

	public void setSelfAssesmentJSON(String selfAssesmentJSON) {
		this.selfAssesmentJSON = selfAssesmentJSON;
	}

	public Map<String, String> getOriginTypes() {
		if (originTypes.size() <= 0) {
			getOriginTypesList();
		}
		return originTypes;
	}

	public void setOriginTypes(Map<String, String> originTypes) {
		this.originTypes = originTypes;
	}

	public String getSelectedDisability() {
		return selectedDisability;
	}

	public void setSelectedDisability(String selectedDisability) {
		this.selectedDisability = selectedDisability;
	}

	public Map<String, String> getPreferenceWorkList() {

		Map<String, String> list = new LinkedHashMap<>();

		list = getFarmCatalougeMap(Integer.valueOf(getText("prefernceWrk")));
		return list;

	}

	public Map<String, String> getPlaceAssessmentList() {

		Map<String, String> list = new LinkedHashMap<>();

		list = getFarmCatalougeMap(Integer.valueOf(getText("placeAssessment")));
		return list;

	}

	public Map<String, String> getAgricultureImplementsMap() {

		Map<String, String> list = new LinkedHashMap<>();

		list = getFarmCatalougeMap(Integer.valueOf(getText("farmEquipment")));
		return list;
	}

	public Map<Integer, Integer> getIcsYearList() {

		Map<Integer, Integer> icsYearMap = new LinkedHashMap<Integer, Integer>();

		int startYear = 2000;
		int endYear = DateUtil.getCurrentYear();

		for (int i = startYear; i <= endYear; i++) {
			icsYearMap.put(i, i);
		}

		return icsYearMap;
	}

	public String getSelectedDisabled() {
		return StringUtil.isEmpty(farmer) ? Farmer.DISABLED_NO : farmer.getIsDisable();

	}

	public void setSelectedDisabled(String selectedDisabled) {
		this.selectedDisabled = selectedDisabled;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map readData(String projectionToken) {

		Map<String, String> projectionProperties = !StringUtil.isEmpty(projectionToken)
				? getProjectionProperties(projectionToken) : null;
		Map data = reportService.listWithMultipleFiltering(getSord(), getSidx(), getStartIndex(), getLimit(),
				getsDate(), geteDate(), filter, getPage(), projectionProperties);
		return data;
	}

	@SuppressWarnings("rawtypes")
	public Map getProjectionProperties(String token) {

		Map<String, String> projectionProperties = new HashMap<String, String>();
		projectionProperties.put(IReportDAO.PROJ_GROUP, getLocaleProperty(token + IReportDAO.PROJ_GROUP));
		projectionProperties.put(IReportDAO.PROJ_SUM, getLocaleProperty(token + IReportDAO.PROJ_SUM));
		projectionProperties.put(IReportDAO.PROJ_OTHERS, getLocaleProperty(token + IReportDAO.PROJ_OTHERS));
		return projectionProperties;
	}

	public String getSidx() {
		return sidx;
	}

	public void setSidx(String sidx) {
		this.sidx = sidx;
	}

	public String getSord() {
		return sord;
	}

	public void setSord(String sord) {
		this.sord = sord;
	}

	public int getLimit() {

		return rows;
	}

	public Date getsDate() {

		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		if (!StringUtil.isEmpty(startDate)) {
			try {
				sDate = df.parse(startDate);
			} catch (ParseException e) {
				// LOGGER.error("Error parsing start date" + e.getMessage());
			}
		}
		return sDate;
	}

	/**
	 * Sets the s date.
	 * 
	 * @param sDate
	 *            the new s date
	 */
	public void setsDate(Date sDate) {

		this.sDate = sDate;
	}

	/**
	 * Gets the e date.
	 * 
	 * @return the e date
	 */
	public Date geteDate() {

		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		if (!StringUtil.isEmpty(endDate)) {
			try {
				eDate = df.parse(endDate);
				eDate.setTime(eDate.getTime() + 86399000);
			} catch (ParseException e) {
				// logger.error("Error parsing end date" + e.getMessage());
			}

		}
		return eDate;
	}

	/**
	 * Sets the e date.
	 * 
	 * @param eDate
	 *            the new e date
	 */
	public void seteDate(Date eDate) {

		this.eDate = eDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String plotting() {
		return "plotting";
	}

	public String getSelectedCatalogue() {
		return selectedCatalogue;
	}

	public void setSelectedCatalogue(String selectedCatalogue) {
		this.selectedCatalogue = selectedCatalogue;
	}

	public Map<Integer, String> getAvailCellPhone() {
		return availCellPhone;
	}

	public void setAvailCellPhone(Map<Integer, String> availCellPhone) {
		this.availCellPhone = availCellPhone;
	}

	public String getShgName() {
		return shgName;
	}

	public void setShgName(String shgName) {
		this.shgName = shgName;
	}

	public String getEducationStatusText() {

		String sb = "";
		Map<String, String> values = getFarmCatalougeMap(Integer.valueOf(getText("educationstatus")));
		sb += ":" + FILTER_ALL + ";";
		sb += values.entrySet().stream().map(entry -> entry.getKey() + ":" + entry.getValue())
				.collect(Collectors.joining(";"));
		return sb;
	}

	public String getEducationText() {

		String sb = "";
		Map<String, String> values = getFarmCatalougeMap(Integer.valueOf(getText("educationType")));
		sb += ":" + FILTER_ALL + ";";
		sb += values.entrySet().stream().map(entry -> entry.getKey() + ":" + entry.getValue())
				.collect(Collectors.joining(";"));
		return sb;
	}

	public String getRelationText() {

		String sb = "";
		Map<String, String> values = getFarmCatalougeMap(Integer.valueOf(getText("relationship")));
		sb += ":" + FILTER_ALL + ";";
		sb += values.entrySet().stream().map(entry -> entry.getKey() + ":" + entry.getValue())
				.collect(Collectors.joining(";"));
		return sb;
	}

	public String getMarStText() {

		String sb = "";
		Map<String, String> values = getFarmCatalougeMap(Integer.valueOf(getText("maritalStatus")));
		sb += ":" + FILTER_ALL + ";";
		sb += values.entrySet().stream().map(entry -> entry.getKey() + ":" + entry.getValue())
				.collect(Collectors.joining(";"));
		return sb;
	}

	public String getGenderText() {

		String sb = "";
		Map<String, String> values = new HashMap<>();
		values.put(Farmer.SEX_MALE, getText("MALE"));
		values.put(Farmer.SEX_FEMALE, getText("FEMALE"));
		sb += ":" + FILTER_ALL + ";";
		sb += values.entrySet().stream().map(entry -> entry.getKey() + ":" + entry.getValue())
				.collect(Collectors.joining(";"));
		return sb;
	}

	public String getDisText() {

		String sb = "";
		Map<String, String> values = new HashMap<>();
		values.put(Farmer.DISABLED_YES, getText("yes"));
		values.put(Farmer.DISABLED_NO, getText("no"));
		sb += ":" + FILTER_ALL + ";";
		sb += values.entrySet().stream().map(entry -> entry.getKey() + ":" + entry.getValue())
				.collect(Collectors.joining(";"));
		return sb;
	}

	public Map<String, String> getMasterTypeList() {

		Map<String, String> masterTypeMap = new LinkedHashMap<String, String>();
		List<MasterData> masterTypeList = farmerService.listMasterType();
		for (MasterData obj : masterTypeList) {
			masterTypeMap.put(obj.getCode(), obj.getName());
		}
		return masterTypeMap;
	}

	public String getSelectedMasterData() {
		return selectedMasterData;
	}

	public void setSelectedMasterData(String selectedMasterData) {
		this.selectedMasterData = selectedMasterData;
	}

	public String getFingerPrintImageByteString() {
		return fingerPrintImageByteString;
	}

	public void setFingerPrintImageByteString(String fingerPrintImageByteString) {
		this.fingerPrintImageByteString = fingerPrintImageByteString;
	}

	public String getFingerPrintEnabled() {
		return fingerPrintEnabled;
	}

	public void setFingerPrintEnabled(String fingerPrintEnabled) {
		this.fingerPrintEnabled = fingerPrintEnabled;
	}

	public List<Map<String, String>> getFarmerPrintMap() {
		return farmerPrintMap;
	}

	public void setFarmerPrintMap(List<Map<String, String>> farmerPrintMap) {
		this.farmerPrintMap = farmerPrintMap;
	}

	public void populatefarmersCountByLocationChartValues() {
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		List<Object[]> stateAndFarmerCountList = farmerService.populateStateAndFarmerCountList(getSelectedBranch());

		stateAndFarmerCountList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("farmerCountByState", objArr[0]);
			jsonObj.put("stateList", objArr[1]);
			jsonObj.put("stateCode", objArr[2]);
			jsonList.add(jsonObj);
		});
		printAjaxResponse(jsonList, "text/html");
	}

	public void populatedistrictAndFarmerCountList() {

		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		List<Object[]> districtAndFarmerCountList = farmerService
				.populateDistrictAndFarmerCountList(getSelectedBranch());

		districtAndFarmerCountList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("farmerCountByDistrict", objArr[0]);
			jsonObj.put("districtList", objArr[1]);
			jsonObj.put("districtCode", objArr[2]);
			jsonObj.put("districtRef_state", objArr[3]);
			jsonList.add(jsonObj);
		});
		printAjaxResponse(jsonList, "text/html");
	}

	public void getActiveFarmerCount() {
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		List<Object[]> activeFarmers = farmerService.populateDistrictAndactiveFarmers(getSelectedBranch());

		activeFarmers.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("activeFarmersCount", objArr[0]);
			jsonObj.put("districtName", objArr[1]);
			jsonObj.put("districtCode", objArr[2]);
			jsonObj.put("stateCode", objArr[3]);
			jsonList.add(jsonObj);
		});
		printAjaxResponse(jsonList, "text/html");
	}

	public void getPramidChartDetails() {
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		BigInteger farmerDetails[] = farmerService.getPramidChartDetails();

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("totalFarmers", farmerDetails[0]);
		jsonObj.put("activeFarmers", farmerDetails[1]);
		jsonObj.put("inActiveFarmers", farmerDetails[2]);
		jsonObj.put("maleFarmers", farmerDetails[3]);
		jsonObj.put("femaleFarmers", farmerDetails[4]);
		jsonObj.put("certifiedFarmers", farmerDetails[5]);
		jsonObj.put("nonCertifiedFarmers", farmerDetails[6]);

		printAjaxResponse(jsonObj, "text/html");
	}

	public void getCertifiedFarmerCount() {
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		List<Object[]> certifiedFarmers = farmerService.getCertifiedFarmerCount();

		certifiedFarmers.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("certifiedFarmersCount", objArr[0]);
			jsonObj.put("districtName", objArr[1]);
			jsonObj.put("districtCode", objArr[2]);
			jsonObj.put("stateCode", objArr[3]);
			jsonList.add(jsonObj);
		});
		printAjaxResponse(jsonList, "text/html");
	}

	public void getNonCertifiedFarmerCount() {
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		List<Object[]> noncertifiedFarmers = farmerService.getNonCertifiedFarmerCount();

		noncertifiedFarmers.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("noncertifiedFarmersCount", objArr[0]);
			jsonObj.put("districtName", objArr[1]);
			jsonObj.put("districtCode", objArr[2]);
			jsonObj.put("stateCode", objArr[3]);
			jsonList.add(jsonObj);
		});
		printAjaxResponse(jsonList, "text/html");
	}

	public void getFarmDetailsAndProposedPlantingArea() {
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		List<Object[]> farmDetailsAndProposedPlantingArea = null;
		if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
			farmDetailsAndProposedPlantingArea = farmerService.getFarmDetailsAndProposedPlantingArea(getLocationLevel(),
					getSelectedBranch(), getGramPanchayatEnable());
		} else {
			farmDetailsAndProposedPlantingArea = farmerService.getFarmDetailsAndCultivationArea(getLocationLevel(),
					getSelectedBranch(), getGramPanchayatEnable());
		}

		farmDetailsAndProposedPlantingArea.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("FarmCount", objArr[0] == null ? 0 : objArr[0]);

			DecimalFormat df = new DecimalFormat("0.00");
			String totalArea = (df.format(Double.valueOf(objArr[1].toString())));

			jsonObj.put("Area", objArr[1] == null ? 0 : totalArea);
			jsonObj.put("locationName", objArr[2]);
			// jsonObj.put("locationCode", objArr[3]);
			jsonList.add(jsonObj);
		});
		printAjaxResponse(jsonList, "text/html");
	}

	public void populateFarmersByLocation() {
		List<JSONObject> farmersByLocation = new ArrayList<JSONObject>();
		List<JSONObject> branch = new ArrayList<JSONObject>();
		List<JSONObject> gramPanchayat = new ArrayList<JSONObject>();
		List<JSONObject> village = new ArrayList<JSONObject>();

		if (StringUtil.isEmpty(getSelectedBranch())) {
			List<Object[]> farmersByBranch = farmerService.farmersByBranch();

			farmersByBranch.stream().forEach(obj -> {
				Object[] objArr = (Object[]) obj;
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("count", objArr[0]);
				jsonObj.put("branchName", objArr[1]);
				jsonObj.put("branchId", objArr[2]);
				branch.add(jsonObj);
			});
		}

		List<Object[]> farmersByCountry;
		if (getIsMultiBranch().equalsIgnoreCase("1") && getIsParentBranch().equalsIgnoreCase("1")) {
			farmersByCountry = farmerService.farmersByCountry(EMPTY);
		} else {
			farmersByCountry = farmerService.farmersByCountry(getSelectedBranch());
		}
		List<JSONObject> country = new ArrayList<JSONObject>();
		farmersByCountry.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("count", objArr[0]);
			jsonObj.put("countryName", objArr[1]);
			jsonObj.put("countryCode", objArr[2]);
			jsonObj.put("branchId", objArr[3]);
			country.add(jsonObj);
		});

		List<Object[]> farmersByState;
		if (getIsMultiBranch().equalsIgnoreCase("1") && getIsParentBranch().equalsIgnoreCase("1"))
			farmersByState = farmerService.farmersByState(EMPTY);
		else
			farmersByState = farmerService.farmersByState(getSelectedBranch());
		List<JSONObject> state = new ArrayList<JSONObject>();
		farmersByState.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("count", objArr[0]);
			jsonObj.put("stateName", objArr[1]);
			jsonObj.put("stateCode", objArr[2]);
			jsonObj.put("countryCode", objArr[3]);
			state.add(jsonObj);
		});

		List<Object[]> farmersByLocality;
		if (getIsMultiBranch().equalsIgnoreCase("1") && getIsParentBranch().equalsIgnoreCase("1"))
			farmersByLocality = farmerService.farmersByLocality(EMPTY);
		else
			farmersByLocality = farmerService.farmersByLocality(getSelectedBranch());
		List<JSONObject> locality = new ArrayList<JSONObject>();
		farmersByLocality.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("count", objArr[0]);
			jsonObj.put("localityName", objArr[1]);
			jsonObj.put("localityCode", objArr[2]);
			jsonObj.put("stateCode", objArr[3]);
			locality.add(jsonObj);
		});

		List<Object[]> farmersByMunicipality;
		if (getIsMultiBranch().equalsIgnoreCase("1") && getIsParentBranch().equalsIgnoreCase("1"))
			farmersByMunicipality = farmerService.farmersByMunicipality(EMPTY);
		else
			farmersByMunicipality = farmerService.farmersByMunicipality(getSelectedBranch());
		List<JSONObject> municipality = new ArrayList<JSONObject>();
		farmersByMunicipality.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("count", objArr[0]);
			jsonObj.put("municipalityName", objArr[1]);
			jsonObj.put("municipalityCode", objArr[2]);
			jsonObj.put("localityCode", objArr[3]);
			municipality.add(jsonObj);
		});

		if (getGramPanchayatEnable().equalsIgnoreCase("1")) {
			List<Object[]> farmersByGramPanchayat = farmerService.farmersByGramPanchayat(getSelectedBranch());

			farmersByGramPanchayat.stream().forEach(obj -> {
				Object[] objArr = (Object[]) obj;
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("count", objArr[0]);
				jsonObj.put("gramPanchayatName", objArr[1]);
				jsonObj.put("gramPanchayatCode", objArr[2]);
				jsonObj.put("municipalityCode", objArr[3]);
				gramPanchayat.add(jsonObj);
			});

			List<Object[]> farmersByVillage = farmerService.farmersByVillageWithGramPanchayat(getSelectedBranch());

			farmersByVillage.stream().forEach(obj -> {
				Object[] objArr = (Object[]) obj;
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("count", objArr[0]);
				jsonObj.put("villageName", objArr[1]);
				jsonObj.put("villageCode", objArr[2]);
				jsonObj.put("gramPanchayatCode", objArr[3]);
				village.add(jsonObj);
			});
		} else {
			List<Object[]> farmersByVillage;
			if (getIsMultiBranch().equalsIgnoreCase("1") && getIsParentBranch().equalsIgnoreCase("1"))
				farmersByVillage = farmerService.farmersByVillageWithOutGramPanchayat(EMPTY);
			else
				farmersByVillage = farmerService.farmersByVillageWithOutGramPanchayat(getSelectedBranch());
			farmersByVillage.stream().forEach(obj -> {
				Object[] objArr = (Object[]) obj;
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("count", objArr[0]);
				jsonObj.put("villageName", objArr[1]);
				jsonObj.put("villageCode", objArr[2]);
				jsonObj.put("municipalityCode", objArr[3]);
				village.add(jsonObj);
			});
		}

		List<Object[]> farmerDetailsByVillage;
		if (getIsMultiBranch().equalsIgnoreCase("1") && getIsParentBranch().equalsIgnoreCase("1"))
			farmerDetailsByVillage = farmerService.farmerDetailsByVillage(EMPTY);
		else
			farmerDetailsByVillage = farmerService.farmerDetailsByVillage(getSelectedBranch());
		List<JSONObject> farmerDetails = new ArrayList<JSONObject>();
		farmerDetailsByVillage.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("totalCount", objArr[0]);
			jsonObj.put("active", objArr[1] == null ? 0 : objArr[1]);
			jsonObj.put("inActive", objArr[2] == null ? 0 : objArr[2]);
			jsonObj.put("certified", objArr[3] == null ? 0 : objArr[3]);
			jsonObj.put("nonCertified", objArr[4] == null ? 0 : objArr[4]);
			jsonObj.put("villageCode", objArr[5] == null ? 0 : objArr[5]);
			farmerDetails.add(jsonObj);
		});

		JSONObject final_jsonObject = new JSONObject();
		if (StringUtil.isEmpty(getSelectedBranch())) {
			final_jsonObject.put("branch", branch);
		}
		final_jsonObject.put("country", country);
		final_jsonObject.put("state", state);
		final_jsonObject.put("locality", locality);
		final_jsonObject.put("municipality", municipality);
		if (getGramPanchayatEnable().equalsIgnoreCase("1")) {
			final_jsonObject.put("gramPanchayat", gramPanchayat);
		}
		final_jsonObject.put("village", village);
		final_jsonObject.put("farmerDetails", farmerDetails);
		final_jsonObject.put("getGramPanchayatEnable", getGramPanchayatEnable());

		farmersByLocation.add(final_jsonObject);

		printAjaxResponse(farmersByLocation, "text/html");
	}

	public void populateFarmerLocationCropChart() {
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		List<Object[]> cropChartData = farmerService.populateFarmerLocationCropChart(getCodeForCropChart());

		cropChartData.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("farmCount", objArr[0]);

			String area="0.00";
			DecimalFormat df = new DecimalFormat("0.00");
			if( objArr[1] != null && !StringUtil.isEmpty(objArr[1].toString())){
				 area = (df.format(Double.valueOf(objArr[1].toString())));
			}

			jsonObj.put("Area", area);
			jsonObj.put("productName", objArr[2]);
			jsonObj.put("productCode", objArr[3]);
			jsonList.add(jsonObj);
		});
		printAjaxResponse(jsonList, "text/html");
	}

	public void estimatedAndActualYield() {

		List<Object[]> estimatedYield = farmerService.estimatedYield(getCodeForCropChart());
		List<Object[]> actualYield = farmerService.actualYield(getCodeForCropChart());
		Map<String, String> estimated_map = new HashMap<String, String>();
		Map<String, String> actual_map = new HashMap<String, String>();
		List<JSONObject> jsonList = new ArrayList<JSONObject>();

		for (Object[] objects : actualYield) {
			String val = (String) actual_map.get((String) objects[1]);
			if (StringUtil.isEmpty(val)) {
				actual_map.put((String) objects[1], objects[0] + "," + objects[2]);
			} else {
				String existingVal[] = val.split(",");
				double actual = Double.parseDouble(String.valueOf(existingVal[1] == null ? "0" : existingVal[1]))
						+ Double.parseDouble(objects[2] == null ? "0" : String.valueOf(objects[2]));
				actual_map.put((String) objects[1], objects[0] + "," + actual);
			}
		}

		for (Object[] objects : actualYield) {
			for (Object[] objects2 : estimatedYield) {
				if (((String) objects[1]).equalsIgnoreCase((String) objects2[1])) {

					String val = (String) estimated_map.get((String) objects2[1]);
					if (StringUtil.isEmpty(val)) {
						estimated_map.put((String) objects2[1], objects2[0] + "," + objects2[2]);
					} else {
						String existingVal[] = val.split(",");
						double estimated = Double
								.parseDouble(String.valueOf(existingVal[1] == null ? "0" : existingVal[1]))
								+ Double.parseDouble(objects2[2] == null ? "0" : String.valueOf(objects2[2]));
						estimated_map.put((String) objects2[1], objects2[0] + "," + estimated);
					}
				}
			}
		}

		for (Map.Entry<String, String> entry : actual_map.entrySet()) {
			// System.out.println("Key = " + entry.getKey() +", Value = " +
			// entry.getValue());
			String estimatedYield_val = (String) estimated_map.get((String) entry.getKey());

			String actualValue[] = (entry.getValue()).split(",");
			String estimatedValue[] = estimatedYield_val.split(",");
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("productName", actualValue[0]);
			jsonObj.put("productCode", entry.getKey());
			jsonObj.put("estimated", estimatedValue[1]);
			jsonObj.put("actual", actualValue[1]);
			jsonList.add(jsonObj);

		}

		/*
		 * for (Object[] objects : actualYield) { String actualYield_val =
		 * (String)actual_map.get((String) objects[1]); String
		 * estimatedYield_val = (String)estimated_map.get((String) objects[1]);
		 * 
		 * String actualValue[] = actualYield_val.split(","); String
		 * estimatedValue[] = estimatedYield_val.split(",");
		 * 
		 * if(((String) objects[0]).equalsIgnoreCase(actualValue[0])){
		 * JSONObject jsonObj = new JSONObject(); jsonObj.put("productName",
		 * objects[0]); jsonObj.put("productCode", objects[1]);
		 * jsonObj.put("estimated", estimatedValue[1]); jsonObj.put("actual",
		 * actualValue[1]); jsonList.add(jsonObj); } }
		 */

		printAjaxResponse(jsonList, "text/html");
	}

	public void cropHarvestByFarmerId() {

		Date date1 = null;
		Date date2 = null;

		if (!StringUtil.isEmpty(getStartDate()) && !StringUtil.isEmpty(getEndDate())) {
			try {
				date1 = new SimpleDateFormat("dd-MM-yyyy").parse(getStartDate());
				date2 = new SimpleDateFormat("dd-MM-yyyy").parse(getEndDate());

			} catch (ParseException e1) {
				System.out.println("error occured");
			}
		}

		String start = DateUtil.convertDateToString(date1, DateUtil.DATE);
		String end = DateUtil.convertDateToString(date2, DateUtil.DATE);

		List<Object[]> cropHarvest = farmerService.cropHarvestByFarmerId(String.valueOf(getId()), start, end,
				getSeasonCode());
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		if (StringUtil.isEmpty(getStartDate()) && StringUtil.isEmpty(getEndDate())) {
			for (Object[] objects : cropHarvest) {
				JSONObject jsonObj = new JSONObject();

				try {
					Date date = DateUtil.removeDateDotZero((Date) objects[0]);
					jsonObj.put("HARVEST_DATE", DateUtil.convertDateToString(date, DateUtil.DATE_FORMAT_2));
				} catch (Exception e) {
					jsonObj.put("HARVEST_DATE",
							DateUtil.convertDateToString((Date) objects[0], DateUtil.DATE_FORMAT_2));
				}

				if (!ObjectUtil.isEmpty(objects[7])) {
					HarvestSeason season = farmerService.findSeasonNameByCode(objects[7].toString());
					jsonObj.put("season", !ObjectUtil.isEmpty(season) ? season.getName() : "");
				} else {
					jsonObj.put("season", "");
				}

				jsonObj.put("FARM_NAME", objects[1]);
				jsonObj.put("ProductName", objects[2]);
				jsonObj.put("productPrice", objects[8]);
				jsonObj.put("UNIT", objects[3]);
				jsonObj.put("Variety", objects[4]);
				jsonObj.put("Grade", objects[5]);
				jsonObj.put("Quantity", objects[6]);
				jsonObj.put("agentName", objects[9]);

				jsonList.add(jsonObj);
			}
		} else {
			List<List<String>> mainArray = new ArrayList<>();
			for (Object[] objects : cropHarvest) {
				List<String> subArray = new ArrayList<>();
				String dateString;
				try {
					Date date = DateUtil.removeDateDotZero((Date) objects[0]);
					dateString = DateUtil.convertDateToString(date, DateUtil.DATE_FORMAT_2);
				} catch (Exception e) {
					dateString = DateUtil.convertDateToString((Date) objects[0], DateUtil.DATE_FORMAT_2);
				}

				subArray.add(dateString == null ? "" : dateString);
				subArray.add(objects[1] == null ? "" : objects[1].toString());// FARM_NAME
				subArray.add(objects[9] == null ? "" : objects[9].toString());// agentName
				subArray.add(objects[2] == null ? "" : objects[2].toString());// ProductName
				if (!ObjectUtil.isEmpty(objects[7])) {
					HarvestSeason season = farmerService.findSeasonNameByCode(objects[7].toString());
					subArray.add(!ObjectUtil.isEmpty(season) ? season.getName() : "");
				} else {
					subArray.add("");
				}
				subArray.add(objects[4] == null ? "" : objects[4].toString());// Variety
				subArray.add(objects[5] == null ? "" : objects[5].toString());// Grade
				subArray.add(objects[6] == null ? "" : objects[6].toString());// Quantity
				subArray.add(objects[3] == null ? "" : objects[3].toString());// UNIT
				// subArray.add(objects[8] == null ? "" :
				// objects[8].toString());

				mainArray.add(subArray);
			}

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("recordsTotal", mainArray.size());
			jsonObj.put("recordsFiltered", mainArray.size());
			jsonObj.put("draw", 0);
			jsonObj.put("data", mainArray.toString());
			jsonList.add(jsonObj);

		}
		printAjaxResponse(jsonList, "text/html");
	}

	public void distributionToFarmerByFarmerId() {

		Date date1 = null;
		Date date2 = null;

		if (!StringUtil.isEmpty(getStartDate()) && !StringUtil.isEmpty(getEndDate())) {
			try {
				date1 = new SimpleDateFormat("dd-MM-yyyy").parse(getStartDate());
				date2 = new SimpleDateFormat("dd-MM-yyyy").parse(getEndDate());

			} catch (ParseException e1) {
				System.out.println("error occured");
			}
		}

		String start = DateUtil.convertDateToString(date1, DateUtil.DATE);
		String end = DateUtil.convertDateToString(date2, DateUtil.DATE);

		List<Object[]> distribution = farmerService.distributionToFarmerByFarmerId(String.valueOf(getId()), start, end,
				getSeasonCode());
		List<JSONObject> jsonList = new ArrayList<JSONObject>();

		if (StringUtil.isEmpty(getStartDate()) && StringUtil.isEmpty(getEndDate())) {

			for (Object[] objects : distribution) {
				JSONObject jsonObj = new JSONObject();

				try {
					Date date = DateUtil.removeDateDotZero((Date) objects[0]);
					jsonObj.put("TXN_TIME", DateUtil.convertDateToString(date, DateUtil.DATE_FORMAT_2));
				} catch (Exception e) {
					jsonObj.put("TXN_TIME", DateUtil.convertDateToString((Date) objects[0], DateUtil.DATE_FORMAT_2));
				}

				HarvestSeason season = farmerService.findSeasonNameByCode(objects[9].toString());
				jsonObj.put("season", !ObjectUtil.isEmpty(season) ? season.getName() : "");

				jsonObj.put("SERVICE_POINT_NAME", objects[1]);
				jsonObj.put("AGENT_NAME", objects[2]);
				jsonObj.put("productName", objects[3]);
				jsonObj.put("productPrice", objects[11]);
				/*
				 * if(String.valueOf(objects[4]).equals(1)){
				 * jsonObj.put("IS_FREE_DISTRIBUTION", "Yes"); }else{
				 * jsonObj.put("IS_FREE_DISTRIBUTION", "No"); }
				 */
				jsonObj.put("QUANTITY", objects[5]);
				jsonObj.put("TOTAL_AMOUNT", objects[6]);
				jsonObj.put("FINAL_AMOUNT", objects[7]);
				jsonObj.put("PAYMENT_AMT", objects[8]);
				jsonObj.put("unit", objects[10]);
				jsonObj.put("tax", objects[12]);
				jsonObj.put("category", objects[13]);
				jsonList.add(jsonObj);
			}
		} else {

			List<List<String>> mainArray = new ArrayList<>();
			for (Object[] objects : distribution) {
				List<String> subArray = new ArrayList<>();
				String dateString;
				try {
					Date date = DateUtil.removeDateDotZero((Date) objects[0]);
					dateString = DateUtil.convertDateToString(date, DateUtil.DATE_FORMAT_2);
				} catch (Exception e) {
					dateString = DateUtil.convertDateToString((Date) objects[0], DateUtil.DATE_FORMAT_2);
				}

				subArray.add(dateString == null ? "" : dateString);
				subArray.add(objects[1] == null ? "" : objects[1].toString());// SERVICE_POINT_NAME
				subArray.add(objects[2] == null ? "" : objects[2].toString());// AGENT_NAME
				subArray.add(objects[13] == null ? "" : objects[13].toString());// category
				subArray.add(objects[3] == null ? "" : objects[3].toString());// productName
				/*
				 * if((objects[3].toString()).contains(",")){ String products =
				 * (objects[3].toString()).replaceAll(",", "#");
				 * subArray.add(products);//productName }else{
				 * subArray.add(objects[3] == null ? "" :
				 * objects[3].toString()); }
				 */
				HarvestSeason season = farmerService.findSeasonNameByCode(objects[9].toString());
				subArray.add(!ObjectUtil.isEmpty(season) ? season.getName() : "");
				subArray.add(objects[10] == null ? "" : objects[10].toString());// unit
				subArray.add(objects[5] == null ? "" : objects[5].toString());// QUANTITY
				subArray.add(objects[6] == null ? "" : objects[6].toString());// TOTAL_AMOUNT
				subArray.add(objects[12] == null ? "" : objects[12].toString());// tax
				subArray.add(objects[7] == null ? "" : objects[7].toString());// FINAL_AMOUNT
				subArray.add(objects[8] == null ? "" : objects[8].toString());// PAYMENT_AMT

				// subArray.add(objects[11] == null ? "" :
				// objects[11].toString());//productPrice

				// subArray.add(objects[4] == null ? "" :
				// objects[4].toString());
				mainArray.add(subArray);

			}

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("recordsTotal", mainArray.size());
			jsonObj.put("recordsFiltered", mainArray.size());
			jsonObj.put("draw", 0);
			jsonObj.put("data", mainArray.toString());
			jsonList.add(jsonObj);
		}
		printAjaxResponse(jsonList, "text/html");
	}

	public void productReturnByFarmerId() {

		Date date1 = null;
		Date date2 = null;

		if (!StringUtil.isEmpty(getStartDate()) && !StringUtil.isEmpty(getEndDate())) {
			try {
				date1 = new SimpleDateFormat("dd-MM-yyyy").parse(getStartDate());
				date2 = new SimpleDateFormat("dd-MM-yyyy").parse(getEndDate());

			} catch (ParseException e1) {
				System.out.println("error occured");
			}
		}

		String start = DateUtil.convertDateToString(date1, DateUtil.DATE);
		String end = DateUtil.convertDateToString(date2, DateUtil.DATE);

		List<Object[]> productReturn = farmerService.productReturnByFarmerId(String.valueOf(getId()), start, end,
				getSeasonCode());
		List<JSONObject> jsonList = new ArrayList<JSONObject>();

		if (StringUtil.isEmpty(getStartDate()) && StringUtil.isEmpty(getEndDate())) { // this
																						// is
																						// for
																						// first
																						// time
																						// loading
																						// data
																						// table
			for (Object[] objects : productReturn) {
				JSONObject jsonObj = new JSONObject();

				try {
					Date date = DateUtil.removeDateDotZero((Date) objects[0]);
					jsonObj.put("TXN_TIME", DateUtil.convertDateToString(date, DateUtil.DATE_FORMAT_2));
				} catch (Exception e) {
					jsonObj.put("TXN_TIME", DateUtil.convertDateToString((Date) objects[0], DateUtil.DATE_FORMAT_2));
				}

				HarvestSeason season = farmerService.findSeasonNameByCode(objects[11].toString());
				jsonObj.put("season", !ObjectUtil.isEmpty(season) ? season.getName() : "");

				jsonObj.put("STOCK_TYPE", objects[1]);
				jsonObj.put("SERVICE_POINT_NAME", objects[2]);
				jsonObj.put("AGENT_NAME", objects[3]);
				jsonObj.put("productName", objects[4]);
				jsonObj.put("productPrice", objects[12]);
				jsonObj.put("UNIT", objects[5]);

				jsonObj.put("COST_PRICE", objects[6]);
				jsonObj.put("SELLING_PRICE", objects[7]);
				jsonObj.put("SUB_TOTAL", objects[8]);
				jsonObj.put("EXISTING_QUANTITY", objects[9]);
				jsonObj.put("QUANTITY", objects[10]);
				jsonList.add(jsonObj);
			}
		} else { // this is for filter result loading in data table
			List<List<String>> mainArray = new ArrayList<>();
			for (Object[] objects : productReturn) {
				List<String> subArray = new ArrayList<>();
				String dateString;
				try {
					Date date = DateUtil.removeDateDotZero((Date) objects[0]);
					dateString = DateUtil.convertDateToString(date, DateUtil.DATE_FORMAT_2);
				} catch (Exception e) {
					dateString = DateUtil.convertDateToString((Date) objects[0], DateUtil.DATE_FORMAT_2);
				}

				subArray.add(dateString == null ? "" : dateString);

				HarvestSeason season = farmerService.findSeasonNameByCode(objects[11].toString());
				subArray.add(!ObjectUtil.isEmpty(season) ? season.getName() : "");

				subArray.add(objects[1] == null ? "" : objects[1].toString());
				subArray.add(objects[2] == null ? "" : objects[2].toString());
				subArray.add(objects[3] == null ? "" : objects[3].toString());
				subArray.add(objects[4] == null ? "" : objects[4].toString());
				subArray.add(objects[12] == null ? "" : objects[12].toString());
				// subArray.add(objects[5] == null ? "" :
				// objects[5].toString());

				subArray.add(objects[9] == null ? "" : objects[9].toString());
				subArray.add(objects[10] == null ? "" : objects[10].toString());

				subArray.add(objects[6] == null ? "" : objects[6].toString());
				subArray.add(objects[7] == null ? "" : objects[7].toString());
				subArray.add(objects[8] == null ? "" : objects[8].toString());

				mainArray.add(subArray);

			}

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("recordsTotal", mainArray.size());
			jsonObj.put("recordsFiltered", mainArray.size());
			jsonObj.put("draw", 0);
			jsonObj.put("data", mainArray.toString());
			jsonList.add(jsonObj);
		}
		printAjaxResponse(jsonList, "text/html");
	}

	public void trainingStatusReportByFarmerId() {

		Date date1 = null;
		Date date2 = null;

		if (!StringUtil.isEmpty(getStartDate()) && !StringUtil.isEmpty(getEndDate())) {
			try {
				date1 = new SimpleDateFormat("dd-MM-yyyy").parse(getStartDate());
				date2 = new SimpleDateFormat("dd-MM-yyyy").parse(getEndDate());

			} catch (ParseException e1) {
				System.out.println("error occured");
			}
		}

		String start = DateUtil.convertDateToString(date1, DateUtil.DATE);
		String end = DateUtil.convertDateToString(date2, DateUtil.DATE);

		List<Object[]> trainingStatus = farmerService.trainingStatusReportByFarmerId(String.valueOf(getId()), start,
				end);
		List<JSONObject> jsonList = new ArrayList<JSONObject>();

		if (StringUtil.isEmpty(getStartDate()) && StringUtil.isEmpty(getEndDate())) {
			for (Object[] objects : trainingStatus) {
				JSONObject jsonObj = new JSONObject();

				try {
					Date date = DateUtil.removeDateDotZero((Date) objects[0]);
					jsonObj.put("TRAINING_DATE", DateUtil.convertDateToString(date, DateUtil.DATE_FORMAT_2));
				} catch (Exception e) {
					jsonObj.put("TRAINING_DATE",
							DateUtil.convertDateToString((Date) objects[0], DateUtil.DATE_FORMAT_2));
				}

				jsonObj.put("TRAINING_CODE", objects[1]);
				jsonObj.put("TRAINING_ASSISTANT_NAME", objects[2]);
				jsonObj.put("TIME_TAKEN_FOR_TRAINING", objects[3]);
				jsonObj.put("FARMER_ATTENED", objects[4]);
				jsonObj.put("REMARKS", objects[5]);
				jsonObj.put("agentName", objects[6]);
				jsonList.add(jsonObj);
			}
		} else {
			List<List<String>> mainArray = new ArrayList<>();
			for (Object[] objects : trainingStatus) {
				List<String> subArray = new ArrayList<>();
				String dateString;
				try {
					Date date = DateUtil.removeDateDotZero((Date) objects[0]);
					dateString = DateUtil.convertDateToString(date, DateUtil.DATE_FORMAT_2);
				} catch (Exception e) {
					dateString = DateUtil.convertDateToString((Date) objects[0], DateUtil.DATE_FORMAT_2);
				}

				subArray.add(dateString == null ? "" : dateString);
				subArray.add(objects[6] == null ? "" : objects[6].toString());// agentName
				subArray.add(objects[1] == null ? "" : objects[1].toString());// TRAINING_CODE
				subArray.add(objects[2] == null ? "" : objects[2].toString());// TRAINING_ASSISTANT_NAME
				subArray.add(objects[3] == null ? "" : objects[3].toString());// TIME_TAKEN_FOR_TRAINING
				// subArray.add(objects[4] == null ? "" :
				// objects[4].toString());//FARMER_ATTENED
				subArray.add(objects[5] == null ? "" : objects[5].toString());// REMARKS

				mainArray.add(subArray);

			}

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("recordsTotal", mainArray.size());
			jsonObj.put("recordsFiltered", mainArray.size());
			jsonObj.put("draw", 0);
			jsonObj.put("data", mainArray.toString());
			jsonList.add(jsonObj);
		}
		printAjaxResponse(jsonList, "text/html");
	}

	public void farmerBalanceReportByFarmerId() {

		Date date1 = null;
		Date date2 = null;

		if (!StringUtil.isEmpty(getStartDate()) && !StringUtil.isEmpty(getEndDate())) {
			try {
				date1 = new SimpleDateFormat("dd-MM-yyyy").parse(getStartDate());
				date2 = new SimpleDateFormat("dd-MM-yyyy").parse(getEndDate());

			} catch (ParseException e1) {
				System.out.println("error occured");
			}
		}

		String start = DateUtil.convertDateToString(date1, DateUtil.DATE);
		String end = DateUtil.convertDateToString(date2, DateUtil.DATE);

		List<Object[]> farmerBalanceReport = farmerService.farmerBalanceReportByFarmerId(String.valueOf(getId()), start,
				end);
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		if (StringUtil.isEmpty(getStartDate()) && StringUtil.isEmpty(getEndDate())) {
			for (Object[] objects : farmerBalanceReport) {
				JSONObject jsonObj = new JSONObject();

				try {
					Date date = DateUtil.removeDateDotZero((Date) objects[0]);
					jsonObj.put("TXN_TIME", DateUtil.convertDateToString(date, DateUtil.DATE_FORMAT_2));
				} catch (Exception e) {
					jsonObj.put("TXN_TIME", DateUtil.convertDateToString((Date) objects[0], DateUtil.DATE_FORMAT_2));
				}

				jsonObj.put("TXN_DESC", objects[1]);
				jsonObj.put("RECEIPT_NO", objects[2]);
				jsonObj.put("INT_BAL", objects[3]);
				jsonObj.put("TXN_AMT", objects[4]);
				jsonObj.put("BAL_AMT", objects[5]);
				jsonList.add(jsonObj);
			}
		} else {
			List<List<String>> mainArray = new ArrayList<>();
			for (Object[] objects : farmerBalanceReport) {
				List<String> subArray = new ArrayList<>();
				String dateString;
				try {
					Date date = DateUtil.removeDateDotZero((Date) objects[0]);
					dateString = DateUtil.convertDateToString(date, DateUtil.DATE_FORMAT_2);
				} catch (Exception e) {
					dateString = DateUtil.convertDateToString((Date) objects[0], DateUtil.DATE_FORMAT_2);
				}

				subArray.add(dateString == null ? "" : dateString);
				subArray.add(objects[1] == null ? "" : objects[1].toString());
				subArray.add(objects[2] == null ? "" : objects[2].toString());
				subArray.add(objects[3] == null ? "" : objects[3].toString());
				subArray.add(objects[4] == null ? "" : objects[4].toString());
				subArray.add(objects[5] == null ? "" : objects[5].toString());

				mainArray.add(subArray);

			}

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("recordsTotal", mainArray.size());
			jsonObj.put("recordsFiltered", mainArray.size());
			jsonObj.put("draw", 0);
			jsonObj.put("data", mainArray.toString());
			jsonList.add(jsonObj);
		}
		printAjaxResponse(jsonList, "text/html");
	}

	public void procurementTransactionsByFarmerId() {

		Date date1 = null;
		Date date2 = null;

		if (!StringUtil.isEmpty(getStartDate()) && !StringUtil.isEmpty(getEndDate())) {
			try {
				date1 = new SimpleDateFormat("dd-MM-yyyy").parse(getStartDate());
				date2 = new SimpleDateFormat("dd-MM-yyyy").parse(getEndDate());

			} catch (ParseException e1) {
				System.out.println("error occured");
			}
		}

		String start = DateUtil.convertDateToString(date1, DateUtil.DATE);
		String end = DateUtil.convertDateToString(date2, DateUtil.DATE);

		List<Object[]> procurementTransactions = farmerService
				.procurementTransactionsByFarmerId(String.valueOf(getId()), start, end, getSeasonCode());
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		if (StringUtil.isEmpty(getStartDate()) && StringUtil.isEmpty(getEndDate())) {
			for (Object[] objects : procurementTransactions) {
				JSONObject jsonObj = new JSONObject();

				try {
					Date date = DateUtil.removeDateDotZero((Date) objects[0]);
					jsonObj.put("CREATED_DATE", DateUtil.convertDateToString(date, DateUtil.DATE_FORMAT_2));
				} catch (Exception e) {
					jsonObj.put("CREATED_DATE",
							DateUtil.convertDateToString((Date) objects[0], DateUtil.DATE_FORMAT_2));
				}

				HarvestSeason season = farmerService.findSeasonNameByCode(objects[1].toString());
				jsonObj.put("season", !ObjectUtil.isEmpty(season) ? season.getName() : "");

				jsonObj.put("ProductName", objects[2]);
				jsonObj.put("Unit", objects[3]);
				jsonObj.put("NUMBER_OF_BAGS", objects[4]);
				jsonObj.put("NET_WEIGHT", objects[5]);
				jsonObj.put("TOTAL_AMOUNT", objects[6]);
				jsonObj.put("PAYMENT_AMT", objects[7]);
				jsonObj.put("agentName", objects[8]);
				jsonList.add(jsonObj);
			}
		} else {
			List<List<String>> mainArray = new ArrayList<>();
			for (Object[] objects : procurementTransactions) {
				List<String> subArray = new ArrayList<>();
				String dateString;
				try {
					Date date = DateUtil.removeDateDotZero((Date) objects[0]);
					dateString = DateUtil.convertDateToString(date, DateUtil.DATE_FORMAT_2);
				} catch (Exception e) {
					dateString = DateUtil.convertDateToString((Date) objects[0], DateUtil.DATE_FORMAT_2);
				}

				subArray.add(dateString == null ? "" : dateString);
				subArray.add(objects[8] == null ? "" : objects[8].toString());
				HarvestSeason season = farmerService.findSeasonNameByCode(objects[1].toString());
				subArray.add(!ObjectUtil.isEmpty(season) ? season.getName() : "");

				subArray.add(objects[2] == null ? "" : objects[2].toString());
				subArray.add(objects[3] == null ? "" : objects[3].toString());
				subArray.add(objects[4] == null ? "" : objects[4].toString());
				subArray.add(objects[5] == null ? "" : objects[5].toString());
				subArray.add(objects[6] == null ? "" : objects[6].toString());
				subArray.add(objects[7] == null ? "" : objects[7].toString());
				mainArray.add(subArray);

			}

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("recordsTotal", mainArray.size());
			jsonObj.put("recordsFiltered", mainArray.size());
			jsonObj.put("draw", 0);
			jsonObj.put("data", mainArray.toString());
			jsonList.add(jsonObj);
		}
		printAjaxResponse(jsonList, "text/html");
	}

	public String getSelectedBranch() {
		return selectedBranch;
	}

	public void setSelectedBranch(String selectedBranch) {
		this.selectedBranch = selectedBranch;
	}

	public String getLocationLevel() {
		return locationLevel;
	}

	public void setLocationLevel(String locationLevel) {
		this.locationLevel = locationLevel;
	}

	public String getCodeForCropChart() {
		return codeForCropChart;
	}

	public void setCodeForCropChart(String codeForCropChart) {
		this.codeForCropChart = codeForCropChart;
	}

	public File getIdProofImg() {
		return idProofImg;
	}

	public void setIdProofImg(File idProofImg) {
		this.idProofImg = idProofImg;
	}

	public String getIdProofImgString() {
		return idProofImgString;
	}

	public void setIdProofImgString(String idProofImgString) {
		this.idProofImgString = idProofImgString;
	}

	public String populateImage() {

		try {
			// setImgId(imgId);
			Farmer pmtImg = null;
			if (!StringUtil.isEmpty(id) && StringUtil.isLong(id) && !StringUtil.isEmpty(type) && type != null)
				pmtImg = farmerService.findFarmerById(Long.valueOf(id));
			byte[] image = null;
			if (type.equals("1") && !ObjectUtil.isEmpty(pmtImg) && pmtImg.getImageInfo() != null
					&& pmtImg.getImageInfo().getPhoto() != null) {
				image = pmtImg.getImageInfo().getPhoto().getImage();
			} else if (type.equals("2") && !ObjectUtil.isEmpty(pmtImg) && pmtImg.getIdProof() != null) {

				image = pmtImg.getIdProofImg();
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

	public String getSeasonCode() {
		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
	}

	public String getIdImgAvil() {
		return idImgAvil;
	}

	public void setIdImgAvil(String idImgAvil) {
		this.idImgAvil = idImgAvil;
	}

	@Override
	public String[] objectFieldNames(String entity) {

		String[] entityFieldNames = null;
		String entityProperties = null;
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)) {
			if (StringUtil.isEmpty(getBranchId())) {
				entityProperties = getText(entity + "ExportPropertiesNoBranch");
			} else if (!getIsParentBranch().equalsIgnoreCase("0")) {
				entityProperties = getText(entity + "ExportPropertiesParentBranch");
			} else {
				entityProperties = getText(entity + "ExportProperties");
			}
		} else if (getCurrentTenantId().equalsIgnoreCase(ESESystem.OCP)) {
			if (StringUtil.isEmpty(getBranchId())) {
				entityProperties = getText(entity + "ExportPropertiesNoBranch");
			} else if (!getIsParentBranch().equalsIgnoreCase("0")) {
				entityProperties = getText(entity + "ExportPropertiesParentBranch");
			} else {
				entityProperties = getText(entity + "ExportProperties");
			}
		}

		else {
			if (!StringUtil.isEmpty(getBranchId())) { // Check if branch User is
				// logged in.
				entityProperties = getText(entity + "ExportPropertiesNoBranch");
			} else {
				entityProperties = getText(entity + "ExportProperties");
			}

		}
		entityFieldNames = entityProperties.split("\\,");

		return entityFieldNames;
	}

	public String getMasterDataFilterText() {

		StringBuffer sb = new StringBuffer();
		sb.append(":").append(FILTER_ALL).append(";");
		List<MasterData> masterTypeList = farmerService.listMasterType();

		for (MasterData objects : masterTypeList) {
			sb.append(objects.getId()).append(":").append(objects.getName()).append(";");
		}

		String data = sb.toString();
		return data.substring(0, data.length() - 1);
	}

	public String getSeasonTypeFilter() {

		StringBuffer sb = new StringBuffer();
		sb.append("-1:").append(FILTER_ALL).append(";");
		List<HarvestSeason> masterTypeList = farmerService.listHarvestSeasons();

		for (HarvestSeason objects : masterTypeList) {
			sb.append(objects.getId()).append(":").append(objects.getName()).append(";");
		}
		String data = sb.toString();
		return data.substring(0, data.length() - 1);
	}

	public String getKmlFileName() {
		return kmlFileName;
	}

	public void setKmlFileName(String kmlFileName) {
		this.kmlFileName = kmlFileName;
	}

	public String getKmlZipFileName() {
		return kmlZipFileName;
	}

	public void setKmlZipFileName(String kmlZipFileName) {
		this.kmlZipFileName = kmlZipFileName;
	}

	public void populateDelete() {
		if (!StringUtil.isEmpty(id)) {
			FarmerDynamicData fd = farmerService.findFarmerDynamicData(id);
			productDistributionService.delete(fd);
		}
	}

	public String getCurrentSeason() {
		String seasonName = "";
		ESESystem system = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(system.getPreferences().get(ESESystem.CURRENT_SEASON_CODE))) {
			HarvestSeason hs = clientService
					.findSeasonByCode(system.getPreferences().get(ESESystem.CURRENT_SEASON_CODE));
			seasonName = hs.getName();
		}
		return seasonName;
	}

	public String getCertificationLevelsFilterText() {

		StringBuffer sb = new StringBuffer();
		sb.append("-1").append(":").append(FILTER_ALL).append(";");
		Iterator it = certificationLevels.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			sb.append(entry.getKey()).append(":").append(entry.getValue()).append(";");
		}
		String data = sb.toString();
		return data.substring(0, data.length() - 1);
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
	public List<Object[]> buildPDFData(List objects, String[] fieldNames) {
		List<Object[]> listObjects = new ArrayList<Object[]>();
		for (Object object : objects) {
			Object[] objects2 = new Object[fieldNames.length];
			for (int i = 0; i < fieldNames.length; i++) {				
				objects2[i] = getObjectFieldValue(object, fieldNames[i]);
			}
			listObjects.add(objects2);
		}
		return listObjects;
	}
	
	public  Object getObjectFieldValue(Object object, String fieldName) { // to
		// get
		// object
		// field
		// name
		// from
		// string
		// passed.

		Object objectValue = null;
		if (object instanceof Object[]) {
			if (ObjectUtil.isLong(fieldName)) {
				Object[] objects = (Object[]) object;
				return objects[Integer.parseInt(fieldName)];
			} else if (fieldName.contains(",")) {
				String[] fields = fieldName.split(",", -1);
				Object[] objAry = new Object[fields.length];
				Object[] objects = (Object[]) object;
				int i = 0;
				for (String str : fields) {
					objAry[i] = objects[Integer.parseInt(str)];
					i++;
				}
				return objAry;
			}
		} else {
			String value = null;
			if (fieldName.contains("certificateStandardLevel")) {
			//	objectValue = getObjectField(object, fieldName);
				try {
				objectValue = getObjectList(object, fieldName);
				/*value =certificationLevels.get(Integer.valueOf(object.toString()));
				return value;
				
*/			
				return objectValue;
				}
				catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchFieldError e) {
					e.printStackTrace();
				}
				}
			else{
			
			try {
				objectValue = getObjectField(object, fieldName); // Get object
				// value
				// returned
				// field.setAccessible(true);
				// classObject =
				// Class.forName(field.getClass().getName()).cast(object);
				return objectValue;

			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldError e) {
				e.printStackTrace();
			} /*
				 * catch (IllegalAccessException e) { e.printStackTrace();
				 * }catch (ClassNotFoundException e) { // TODO Auto-generated
				 * catch block e.printStackTrace(); }
				 */
			}
		}
		return null;
	}

	public static Object getObjectField(Object object, String classAndFieldName) {
		String[] fields = null;
		Field field = null;

		fields = classAndFieldName.split("\\.");

		try {
			for (String fieldName : fields) {
				if (!ObjectUtil.isEmpty(object)) {
					field = object.getClass().getDeclaredField(fieldName);
					field.setAccessible(true);
					object = field.get(object);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return object;
	}
	
	
	public  Object getObjectList(Object object, String classAndFieldName) {
		String[] fields = null;
		Field field = null;

		fields = classAndFieldName.split("\\.");

		try {
			for (String fieldName : fields) {
				if (!ObjectUtil.isEmpty(object)) {
					field = object.getClass().getDeclaredField(fieldName);
					field.setAccessible(true);
					//object = field.get(object);
					object =certificationLevels.get(Integer.valueOf(field.get(object).toString()));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return object;
	}

	public String getCertificationStandardLevel() {
		return certificationStandardLevel;
	}

	public void setCertificationStandardLevel(String certificationStandardLevel) {
		this.certificationStandardLevel = certificationStandardLevel;
	}

	public String getEnableContractTemplate() {
		return enableContractTemplate;
	}

	public void setEnableContractTemplate(String enableContractTemplate) {
		this.enableContractTemplate = enableContractTemplate;
	}

	public String getDigitalSignatureByteString() {
		return digitalSignatureByteString;
	}

	public void setDigitalSignatureByteString(String digitalSignatureByteString) {
		this.digitalSignatureByteString = digitalSignatureByteString;
	}
	
	public Farmer setFarmerCropFilter(Farmer filter, Map<String, String> searchRecord) {
		filter.setCropNames(searchRecord.get("crop"));
		return filter;
	}
	
	public JSONArray getFarmCropInfo(Farmer farmer) {
		Farm tmpFarm;
		FarmCrops tmpFarmCrops;
		Set<Farm> farmSet = new LinkedHashSet<Farm>();
		Set<FarmCrops> farmCropSet = new LinkedHashSet<FarmCrops>();
		String farmCropName = "";
		String farmCropYield = "";
		List<Farm> farmList = new ArrayList<Farm>();
		List<FarmCrops> farmCropList = new ArrayList<FarmCrops>();
		JSONArray rows = new JSONArray();

		farmSet = farmer.getFarms();
		if (!ObjectUtil.isListEmpty(farmSet)) {
			farmList = new ArrayList<Farm>(farmSet);
			if (!ObjectUtil.isListEmpty(farmList)) {
				tmpFarm = farmList.get(0);
				if (!ObjectUtil.isEmpty(tmpFarm)) {
					farmCropSet = tmpFarm.getFarmCrops();
					farmCropList = new ArrayList<FarmCrops>(farmCropSet);
					if (!ObjectUtil.isListEmpty(farmCropList)) {
						tmpFarmCrops = farmCropList.get(0);
						if (!ObjectUtil.isEmpty(tmpFarmCrops)) {
							farmCropName = tmpFarmCrops.getProcurementVariety().getProcurementProduct().getName();
							if (!ObjectUtil.isEmpty(tmpFarmCrops.getEstimatedYield())) {
								farmCropYield = String.valueOf(tmpFarmCrops.getEstimatedYield()/1000);
							} else {
								farmCropYield = "";
							}
						}
					}
				}
			}
		}
		rows.add(farmCropName);
		rows.add(farmCropYield);
		return rows;
	}
	
	public JSONArray getFarmCropInfo(String farmerid) {
		Farm tmpFarm;
		FarmCrops tmpFarmCrops;
		Set<Farm> farmSet = new LinkedHashSet<Farm>();
		Set<FarmCrops> farmCropSet = new LinkedHashSet<FarmCrops>();
		String farmCropName = "";
		String farmCropYield = "";
		List<Farm> farmList = new ArrayList<Farm>();
		List<FarmCrops> farmCropList = new ArrayList<FarmCrops>();
		JSONArray rows = new JSONArray();
		Farmer farmer=farmerService.findFarmerByFarmerId(farmerid);
		farmSet = farmer.getFarms();
		if (!ObjectUtil.isListEmpty(farmSet)) {
			farmList = new ArrayList<Farm>(farmSet);
			if (!ObjectUtil.isListEmpty(farmList)) {
				tmpFarm = farmList.get(0);
				if (!ObjectUtil.isEmpty(tmpFarm)) {
					farmCropSet = tmpFarm.getFarmCrops();
					farmCropList = new ArrayList<FarmCrops>(farmCropSet);
					if (!ObjectUtil.isListEmpty(farmCropList)) {
						tmpFarmCrops = farmCropList.get(0);
						if (!ObjectUtil.isEmpty(tmpFarmCrops)) {
							farmCropName = tmpFarmCrops.getProcurementVariety().getProcurementProduct().getName();
							if (!ObjectUtil.isEmpty(tmpFarmCrops.getEstimatedYield())) {
								farmCropYield = String.valueOf(tmpFarmCrops.getEstimatedYield()/1000);
							} else {
								farmCropYield = "";
							}
						}
					}
				}
			}
		}
		rows.add(farmCropName);
		rows.add(farmCropYield);
		return rows;
	}

	public List<Farm> getFarms() {
		return farms;
	}

	public void setFarms(List<Farm> farms) {
		this.farms = farms;
	}

	public List<FarmCrops> getFarmCrop() {
		return farmCrop;
	}

	public void setFarmCrop(List<FarmCrops> farmCrop) {
		this.farmCrop = farmCrop;
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
	
	String aa="";
	@SuppressWarnings("unchecked")
	public void populateFarm() throws ScriptException {
		List<Farm> farm=farmerService.listFarmByFarmerId(Long.valueOf(id));
		farm.forEach(farmField -> {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", farmField.getId());
			jsonObject.put("lat",!ObjectUtil.isEmpty(farmField.getLatitude()) ? farmField.getLatitude() : "" );
			jsonObject.put("lon",!ObjectUtil.isEmpty(farmField.getLongitude()) ? farmField.getLongitude() : ""  );
			//String aa=farmField.getFarmICSConversion().size()>0 ? farmField.getFarmICSConversion().stream().filter(u -> u.getIsActive()==1).findFirst().get().getIcsType() : "" ;
			Optional<FarmIcsConversion> fc= farmField.getFarmICSConversion().stream().filter(u -> u.getIsActive()==1).findFirst();
			if(fc.isPresent()){
					aa=	fc.get().getIcsType();
					}
			jsonObject.put("icsType", aa);
			jsonObjects.add(jsonObject);
		});
		printAjaxResponse(jsonObjects, "text/html");
	}

	public String getCropInfoEnabled() {
		return cropInfoEnabled;
	}

	public void setCropInfoEnabled(String cropInfoEnabled) {
		this.cropInfoEnabled = cropInfoEnabled;
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
	
	public String approve() throws Exception {

		if (id != null && !id.equals("")) {
			farmer = farmerService.findFarmerById(Long.valueOf(id));
			setCurrentPage(getCurrentPage());
			if (farmer == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			} else {
				farmer.setStatus(1);
				farmer.setRevisionNo(DateUtil.getRevisionNumber());
				farmerService.update(farmer);
			}
		}
		request.setAttribute(HEADING, getText("farmerlist"));
		return LIST;
	}
	
	public String populateQRPdf() {
		try {
			ESESystem preferences = preferncesService.findPrefernceById("1");
			if (!StringUtil.isEmpty(id)) {
				Farmer f = farmerService.findFarmerById(Long.valueOf(id));
				if (f != null) {
					String html_str = preferences.getPreferences().get(ESESystem.contracte_template);
					setPdfFileName(getText("farmerListQr") + fileNameDateFormat.format(new Date()));
					Map<String, InputStream> fileMap = new HashMap<String, InputStream>();

					InputStream is = printPDF(f, html_str);
					/*
					 * fileMap.put(getPdfFileName(), is);
					 * setFileInputStream(FileUtil.
					 * createFileInputStreamToZipFile(getText("farmerList"),
					 * fileMap, ".pdf")); return "pdf";
					 */
					setPdfFileName(f.getName());
					/*
					 * Map<String, InputStream> fileMap = new HashMap<String,
					 * InputStream>(); fileMap.put(xlsFileName, is);
					 * setFileInputStream(FileUtil.
					 * createFileInputStreamToZipFile(getLocaleProperty(
					 * "farmerList"), fileMap, ".pdf"));
					 */
					setFileInputStream(is);
					return "pdfFile";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	String pdfT = "";
	public InputStream printPDF(Object farmersList, String html_str) throws DocumentException, IOException {
		ESESystem preferences = preferncesService.findPrefernceById("1");
		String makeDir = FileUtil.storeXls(request.getSession().getId());
		String fileName = getText("FarmerListFile") + fileNameDateFormat.format(new Date()) + ".pdf";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		String width = preferences.getPreferences().get(ESESystem.PDF_WIDTH);
		String height = preferences.getPreferences().get(ESESystem.PDF_HEIGHT);
		Document document = new Document(new RectangleReadOnly(Integer.valueOf(width), Integer.valueOf(height)));// this
																													// will
																													// create
																													// (2.1
																													// X
																													// 3.5)
																													// inch
																													// document
																													// sheet
		document.setMargins(0, 0, 0, 0);// this is for margins
		PdfWriter writer = PdfWriter.getInstance(document, fileOut);
		document.open();
		final TagProcessorFactory tagProcessorFactory = Tags.getHtmlTagProcessorFactory();
		tagProcessorFactory.removeProcessor(HTML.Tag.IMG);
		tagProcessorFactory.addProcessor(new ImageTagProcessor(), HTML.Tag.IMG);
		tagProcessorFactory.removeProcessor(HTML.Tag.TABLE);
		tagProcessorFactory.addProcessor(new TableTagProcessor(), HTML.Tag.TABLE);
		final CssFilesImpl cssFiles = new CssFilesImpl();
		cssFiles.add(XMLWorkerHelper.getInstance().getDefaultCSS());
		StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
		HtmlPipelineContext hpc = new HtmlPipelineContext(new CssAppliersImpl(new XMLWorkerFontProvider()));
		hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(tagProcessorFactory);
		HtmlPipeline htmlPipeline = new HtmlPipeline(hpc, new PdfWriterPipeline(document, writer));
		Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
		XMLWorker worker = new XMLWorker(pipeline, true);
		Charset charset = Charset.forName("UTF-8");
		XMLParser xmlParser = new XMLParser(true, worker, charset);
		if (farmersList instanceof List<?>) {
			List<Farmer> f = (List<Farmer>) farmersList;
			f.stream().forEach(ff -> {
				try {
					pdfT = "";
					pdfT = getTextPdf(html_str, ff) + "<div style='page-break-before: always;'></div>";
					xmlParser.parse(new ByteArrayInputStream(pdfT.getBytes()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			});
			
		} else {
			Farmer f = (Farmer) farmersList;
			try {
				pdfT = "";
				pdfT = getTextPdf(html_str, f) + "<div style='page-break-before: always;'></div>";
				xmlParser.parse(new ByteArrayInputStream(pdfT.getBytes()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		document.close();
		fileOut.close();

		InputStream is = new FileInputStream(new File(makeDir + fileName));
		return is;
	}
	
	public String getTextPdf(String html_str, Farmer f) throws IOException {

		if (html_str.contains("[:farmerName]")) {
			html_str = html_str.replace("[:farmerName]", f.getName());
		}

		if (html_str.contains("[:fatherName]")) {
			html_str = html_str.replace("[:fatherName]", f.getLastName());
		}

		if (html_str.contains("[:village]")) {
			html_str = html_str.replace("[:village]", f.getVillage() == null ? "" : f.getVillage().getName());
		}

		if (html_str.contains("[:season]")) {
			html_str = html_str.replace("[:season]", getCurrentSeasonsCode());
		}

		if (html_str.contains("[:currentYear]")) {
			html_str = html_str.replace("[:currentYear]", String.valueOf((new Date()).getYear()));
		}

		if (html_str.contains("[:currentDate]")) {
			Date d = new Date();
			String strDate = d.getYear() + "/" + (d.getMonth() + 1) + "/" + d.getDate();
			html_str = html_str.replace("[:currentDate]", strDate);
		}

		if (html_str.contains("[:rifanId]")) {
			html_str = html_str.replace("[:rifanId]", f.getProofNo() == null ? "" : f.getProofNo());
		}

		if (html_str.contains("[:state]")) {
			html_str = html_str.replace("[:state]",
					f.getSamithi() == null ? "" : f.getSamithi().getName());
		}

		if (html_str.contains("[:lga]")) {
			html_str = html_str.replace("[:lga]",
					f.getVillage() == null ? "" : f.getVillage().getCity().getLocality().getName());
		}
		if (html_str.contains("[:city]")) {
			html_str = html_str.replace("[:city]",
					f.getVillage() == null ? "" : f.getVillage().getCity().getName());
		}

		if (html_str.contains("[:farmercode]")) {
			html_str = html_str.replace("[:farmercode]", f.getFarmerCode() == null ? "" : f.getFarmerCode());
		}
		if (html_str.contains("[:farmerId]")) {
			html_str = html_str.replace("[:farmerId]", f.getFarmerId() == null ? "" : f.getFarmerId());
		}

		if (html_str.contains("[:farmerAge]")) {
			html_str = html_str.replace("[:farmerAge]", String.valueOf(f.getAge()));
		}

		if (html_str.contains("[:farmerPhoto]")) {
			String photo = "";
			if (f.getImageInfo() != null && f.getImageInfo().getPhoto() != null
					&& f.getImageInfo().getPhoto().getImage() != null) {
				photo = Base64Util.encoder(f.getImageInfo().getPhoto().getImage());
			} else {
				byte[] imageData = new byte[] {};
				String logoPath = request.getSession().getServletContext().getRealPath("/img/avatar-small.jpg");
				File pic = new File(logoPath);
				long length = pic.length();
				imageData = new byte[(int) length];
				FileInputStream picIn = new FileInputStream(pic);
				picIn.read(imageData);
				photo = Base64Util.encoder(imageData);

			}
			html_str = html_str.replace("[:farmerPhoto]", "data:image/png;base64," + photo);
		}

		if (html_str.contains("[:qrCode]")) {
			if (f.getProofNo() != null) {
				ByteArrayOutputStream stream = QRCode.from(f.getFarmerId()).withErrorCorrection(ErrorCorrectionLevel.L)
						.withHint(EncodeHintType.MARGIN, 2).withSize(250, 250).stream();
				String bytes = Base64Util.encoder(stream.toByteArray());
				html_str = html_str.replace("[:qrCode]", "data:image/png;base64," + bytes);
			}
		}
		return html_str;
	}
	public void validateFarmerCode() throws Exception {

	JSONObject jsonObject = new JSONObject();	
		if(!StringUtil.isEmpty(farmerCode)){
			Farmer farmer= farmerService.findOlivadoFarmerByFarmerCode(farmerCode);
			if(!ObjectUtil.isEmpty(farmer)){
				jsonObject.put("status", 1);
			}else{
				jsonObject.put("status", 2);
			}
		}else{
			jsonObject.put("status", 2);
		}
		sendAjaxResponse(jsonObject);
	}

	public String getFarmerCode() {
		return farmerCode;
	}

	public void setFarmerCode(String farmerCode) {
		this.farmerCode = farmerCode;
	}

	public Map<Integer, String> getProcessingActList() {
		return processingActList;
	}

	public void setProcessingActList(Map<Integer, String> processingActList) {
		this.processingActList = processingActList;
	}

	public String getSelectedInputSource() {
		return selectedInputSource;
	}

	public void setSelectedInputSource(String selectedInputSource) {
		this.selectedInputSource = selectedInputSource;
	}
	
	public List<TreeDetail> getTreeDetails() {
		return treeDetails;
	}

	public void setTreeDetails(List<TreeDetail> treeDetails) {
		this.treeDetails = treeDetails;
	}

	public List<TreeDetail> getTreeDetailss() {
		return treeDetailss;
	}

	public void setTreeDetailss(List<TreeDetail> treeDetailss) {
		this.treeDetailss = treeDetailss;
	}

	public String getSelectedVillageCode() {
		return selectedVillageCode;
	}

	public void setSelectedVillageCode(String selectedVillageCode) {
		this.selectedVillageCode = selectedVillageCode;
	}
	 

    
}
