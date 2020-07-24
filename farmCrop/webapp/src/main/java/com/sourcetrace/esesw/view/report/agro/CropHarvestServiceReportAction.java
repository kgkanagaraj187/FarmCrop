package com.sourcetrace.esesw.view.report.agro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
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
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.profile.CropHarvest;
import com.ese.entity.profile.CropHarvestDetails;
import com.ese.entity.util.ESESystem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sourcetrace.eses.dao.IReportDAO;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.FarmCatalogueMaster;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IFarmCropsService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.view.IExporter;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

public class CropHarvestServiceReportAction extends SwitchValidatorAction {

	private static final Logger LOGGER = Logger.getLogger(CropHarvestServiceReportAction.class);
	private static final SimpleDateFormat fieldStaffDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	@Autowired
	private ILocationService locationService;
	@Autowired
	private IProductService productService;
	@Autowired
	private IFarmCropsService farmCropsService;

	private IPreferencesService preferncesService;

	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}

	@Autowired
	private IProductDistributionService productDistributionService;
	@Autowired
	private ICatalogueService catalogueService;
	private List<String> fields = new ArrayList<String>();
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
	public static final String NA = "NA";
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	private String harvestDate;
	private String farmerId;
	private String farmCode;
	private String cropType;
	// private CropHarvest filter;
	private Map<String, String> villages;
	private Map<String, String> farmerList;
	private Map<String, String> farmList;
	private Map<Integer, String> cropTypeList;
	private Map<Integer, String> cropList;
	private Map<Integer, String> varietyList;
	private Map<Integer, String> gradeList;
	private String cropTypeCode;
	private String cropId;
	private String varietyId;
	private long cropHarvestId;
	private String selectedCropType;
	private String selectedCropName;
	private String selectedVariety;
	private String selectedBuyer;
	// private Set<CropHarvestDetails> cropHarvestDetails;
	@Autowired
	private IFarmerService farmerService;
	private CropHarvest cropHarvest;
	private String xlsFileName;
	private String harvestDeatailJsonString;
	private long harvestId;
	private long templateId;
	// private static final String TOTAL_YIELD_QUANTITY = "totalQty";
	private static final String TOTAL_YIELD_PRICE = "totalPrice";
	private String DETAILNEED = "needDetail";
	private String DETAIL = "detail";
	private String UPDATE = "update";
	private String NO_RECORD = "No_Records_Present";
	private String command;
	private InputStream fileInputStream;
	private List<CropHarvestDetails> cropHarvestDetailsList = new ArrayList<CropHarvestDetails>();
	@Autowired
	private IUniqueIDGenerator idGenerator;
	private String productTotalString;
	private String selectedFarmer;
	private String selectedFarm;
	private String totalSaleQty;
	private String selectedVillage;
	private String searchPage;
	private String id;
	protected Date sDate = null;
	protected Date eDate = null;
	private boolean mailExport;
	protected static final String FILTERDATE = "MM/dd/yyyy";
	private Properties errors;
	protected String sidx;
	protected String sord;
	protected int rows;
	private String storage;
	private String packed;
	private String otherStorageInType;
	private String otherPackedInType;
	private Double metricTon;
	private String dateFormat;
	
	private String productAvailableUnit;
	
	private String sowingDate;
	
	List<Farmer> farmers = new ArrayList<>();
	Map <String,String>storageId=new LinkedHashMap<String,String>();
	Map <String,String>packId=new LinkedHashMap<String,String>();

	public List<Farmer> getFarmers() {
		return farmers;
	}

	public void setFarmers(List<Farmer> farmers) {
		this.farmers = farmers;
	}

	List<FarmCrops> varietysList = new ArrayList<FarmCrops>();
	private String selectedGrade;
	private String selectedCrop;
	List<ProcurementGrade> gradesList = new ArrayList<ProcurementGrade>();
	List<FarmCrops> cropNamesList = new ArrayList<FarmCrops>();
	List<Farm> farms = new ArrayList<>();
	private String receiptNumber;

	public String list() {
		CropHarvest cropHarvest = new CropHarvest();
		
		Calendar currentDate = Calendar.getInstance();
		//DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		DateFormat df = new SimpleDateFormat(getGeneralDateFormat());
		super.startDate = df
				.format(DateUtil.getFirstDateOfMonth(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH)));
		super.endDate = df.format(currentDate.getTime());
		fields.add(getText("harvestDate"));
		fields.add(getText("farmerId"));
		fields.add(getText("farmId"));
		fields.add(getText("cropType"));
		fields.add(getText("cropName"));
		request.setAttribute(HEADING, getText("cropHarvestSRlist"));
		setFilter(cropHarvest);
		return LIST;
	}

	private void setFilter(CropHarvest cropHarvest2) {
		// TODO Auto-generated method stub

	}

	public String getSelectedVillage() {
		return selectedVillage;
	}

	public void setSelectedVillage(String selectedVillage) {
		this.selectedVillage = selectedVillage;
	}

	public List<FarmCrops> getVarietysList() {
		return varietysList;
	}

	public void setVarietysList(List<FarmCrops> varietysList) {
		this.varietysList = varietysList;
	}

	public List<ProcurementGrade> getGradesList() {
		return gradesList;
	}

	public void setGradesList(List<ProcurementGrade> gradesList) {
		this.gradesList = gradesList;
	}

	public List<FarmCrops> getCropNamesList() {
		return cropNamesList;
	}

	public void setCropNamesList(List<FarmCrops> cropNamesList) {
		this.cropNamesList = cropNamesList;
	}

	public List<Farm> getFarms() {
		return farms;
	}

	public void setFarms(List<Farm> farms) {
		this.farms = farms;
	}

	public String data() throws Exception {

		// super.filter = this.filter;

		// return sendJSONResponse(readData());
		return sendJQGridJSONResponse(buildFilterDataMap());
	}

	@SuppressWarnings("unchecked")
	private Map buildFilterDataMap() {

		Map<String, String> searchRecord = getJQGridRequestParam(); // get the
																	// search
																	// parameter
																	// with
		// value

		CropHarvest filter = new CropHarvest();

		/*
		 * if (!StringUtil.isEmpty(searchRecord.get("harvestDate"))) {
		 * filter.setBranchId(searchRecord.get("harvestDate").trim()); }
		 */

		if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
			filter.setBranchId(searchRecord.get("branchId").trim());
		}
		
		if (!StringUtil.isEmpty(searchRecord.get("subBranchId"))) {
			filter.setBranchId(searchRecord.get("subBranchId").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("farmerId"))) {
			filter.setFarmerName(searchRecord.get("farmerId").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("farmCode"))) {
			filter.setFarmName(searchRecord.get("farmCode").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("receiptNo"))) {
			filter.setReceiptNo(searchRecord.get("receiptNo").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("village"))) {
			filter.setVillage(searchRecord.get("village").trim());
		}
		if(!StringUtil.isEmpty(searchRecord.get("seasonCode"))){
			filter.setSeasonCode(searchRecord.get("seasonCode").trim());
		}

		/*
		 * Farmer farmer =
		 * farmerService.findFarmerByFarmerId(cropHarvest.getFarmerId());
		 * 
		 * if (!StringUtil.isEmpty(searchRecord.get("v.name"))) { Village
		 * village = new Village();
		 * village.setName(searchRecord.get("v.name").trim());
		 * filter.farmer.setVillage(village); }
		 */
		filter.setSearchPage(searchPage);
		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());
		return data;

	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {
		JSONObject jsonObject = new JSONObject();

		JSONArray rows = new JSONArray();
		CropHarvest cropHarvest = (CropHarvest) obj;
		ESESystem preferences = preferncesService.findPrefernceById("1");
        DateFormat genDate=new SimpleDateFormat(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
		
		if ((getIsMultiBranch().equalsIgnoreCase("1") && (getIsParentBranch().equals("1")||StringUtil.isEmpty(branchIdValue)))) {

	          if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(cropHarvest.getBranchId())))
							? getBranchesMap().get(getParentBranchMap().get(cropHarvest.getBranchId()))
							: getBranchesMap().get(cropHarvest.getBranchId()));
				}
				rows.add(getBranchesMap().get(cropHarvest.getBranchId()));
	     } else {
	         if (StringUtil.isEmpty(branchIdValue)) {
	             rows.add(branchesMap.get(cropHarvest.getBranchId()));
	         }
	     }
		if(!ObjectUtil.isEmpty(preferences)){
			if(!ObjectUtil.isEmpty(cropHarvest.getHarvestDate())){
				rows.add(!ObjectUtil.isEmpty(cropHarvest.getHarvestDate())
						? genDate.format(cropHarvest.getHarvestDate()).toString() : "");
					
			}
				
		}
		 
   	    HarvestSeason season = farmerService.findSeasonNameByCode(cropHarvest.getSeasonCode());
		rows.add(!ObjectUtil.isEmpty(season)?season.getName():"");//Season Code

		Farmer farmer = farmerService.findFarmerByFarmerId(cropHarvest.getFarmerId());

		rows.add(ObjectUtil.isEmpty(farmer) ? "" : farmer.getVillage().getCode()+"-"+farmer.getVillage().getName());
		
		rows.add(cropHarvest.getFarmerName());
		rows.add(cropHarvest.getFarmName());
		//rows.add(cropHarvest.getFarmerId() + "-" + cropHarvest.getFarmerName());

		//rows.add(cropHarvest.getFarmCode() + "-" + cropHarvest.getFarmerName());

		//rows.add(StringUtil.isEmpty(cropHarvest.getReceiptNo()) ? "" : cropHarvest.getReceiptNo());
		/*rows.add(cropHarvest.getTotalQty());*/
		 double metricTon  = 0.0;
         double qty = 0.0;
         double kgQty=0.0;
         double quintalQty=0.0;
		 for(CropHarvestDetails cd : cropHarvest.getCropHarvestDetails()){
			 if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {
				 if(!ObjectUtil.isEmpty(cd.getCrop()) && !StringUtil.isEmpty(cd.getCrop().getUnit())){
				 if(cd.getCrop().getUnit().equalsIgnoreCase("kg") || cd.getCrop().getUnit().equalsIgnoreCase("kgs")){
					 kgQty+=cd.getQty(); 
				 }
				 if(cd.getCrop().getUnit().equalsIgnoreCase("quintal") || cd.getCrop().getUnit().equalsIgnoreCase("quintals")){
					 quintalQty+=cd.getQty();
				 }
				 }
			 }
			 else{
		    metricTon += cd.getQty() / 1000;
		    qty +=   cd.getQty();
			 }
		 }
		 if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {
		 metricTon=(kgQty/1000)+(quintalQty/10);
		 rows.add(CurrencyUtil.getDecimalFormat(metricTon,"##.00"));
		 }
		 else{
			 rows.add(CurrencyUtil.getDecimalFormat(qty,"##.00"));
		 }
		Double estYld = (Double) productDistributionService.findTotalYieldPriceByHarvestId(cropHarvest.getId());
        BigDecimal bdVal = null;
        if (!ObjectUtil.isEmpty(estYld)) {
            bdVal = new BigDecimal(estYld);
            int decimalPlaces = 2;
            bdVal = bdVal.setScale(decimalPlaces, BigDecimal.ROUND_DOWN);
        }
		rows.add(!ObjectUtil.isEmpty(bdVal) ? String.valueOf(bdVal) : "");

		// rows.add(!ObjectUtil.isEmpty(cropHarvest.getTotalQty()) ?
		// cropHarvest.getTotalQty() : "");

		jsonObject.put("cell", rows);
		jsonObject.put("id", cropHarvest.getId());

		// rows.add(StringUtil.isEmpty(cropHarvest.getCropHarvestDetails().getPrice())
		// ? "" : cropHarvest.getCropHarvestDetails().getCropType());

		return jsonObject;
	}

	public String create() throws ParseException {
		String result = "";
		double totQty = 0;

		if (productTotalString == null) {
			getCurrentSeasonsCode();
			getCurrentSeasonsName();
			result = "create";

		} else {
			String receiptnumberSeq = StringUtil.isEmpty(receiptNumber) ? idGenerator.getCropHarvestReceiptNumberSeq()
					: receiptNumber;
			receiptNumber = receiptnumberSeq;
			Set<CropHarvestDetails> cropHarvestDetailSet = new HashSet<>();
			cropHarvest = new CropHarvest();
			cropHarvest.setFarmerId(selectedFarmer);
			Farm farm = farmerService.findFarmByCode(selectedFarm);
			if (!ObjectUtil.isEmpty(farm)) {
				cropHarvest.setFarmCode(farm.getFarmCode());
				cropHarvest.setFarmName(farm.getFarmName());
			}
			cropHarvest.setFarmerName(farm.getFarmer().getName());
			//cropHarvest.setHarvestDate(DateUtil.convertStringToDate(harvestDate, DateUtil.DATE_FORMAT_1));
			cropHarvest.setHarvestDate(DateUtil.convertStringToDate(harvestDate, getGeneralDateFormat()));
			cropHarvest.setHarvestDate(DateUtil.setTimeToDate(cropHarvest.getHarvestDate()));
			cropHarvest.setBranchId(getBranchId());
			cropHarvest.setReceiptNo(receiptNumber);
			if(getStorage()!=null){
			cropHarvest.setStorageIn(getStorage());
			 if (storage.equalsIgnoreCase("99")) {
			    	cropHarvest.setOtherStorageInType(getOtherStorageInType());
			    }
			}
			if(getPacked()!=null){
			cropHarvest.setPackedIn(getPacked());
			  if(packed.equalsIgnoreCase("99"))
			  {
				  cropHarvest.setOtherPackedInType(getOtherPackedInType());
			  }
			}
			String[] productArray = productTotalString.split("\\|");
			for (String productData : productArray) {

				String[] productDataArray = productData.split("\\#");

				CropHarvestDetails cropHarvestDetails = new CropHarvestDetails();
				cropHarvestDetails.setCropType(Integer.valueOf(productDataArray[0]));
				ProcurementProduct crop = new ProcurementProduct();
				crop.setId(Long.valueOf(productDataArray[1]));
				cropHarvestDetails.setCrop(crop);
				ProcurementVariety variety = new ProcurementVariety();
				variety.setId(Long.valueOf(productDataArray[2]));
				cropHarvestDetails.setVariety(variety);
				ProcurementGrade grade = new ProcurementGrade();
				grade.setId(Long.valueOf(productDataArray[3]));
				cropHarvestDetails.setGrade(grade);
				cropHarvestDetails.setQty(Double.valueOf(productDataArray[5]));
				cropHarvestDetails.setPrice(0.00);
				cropHarvestDetails.setSubTotal(0.00);
				cropHarvestDetails.setCropHarvest(cropHarvest);
				cropHarvestDetailSet.add(cropHarvestDetails);
				double qty = Double.valueOf(productDataArray[5]);
				totQty += qty;
				cropHarvest.setTotalQty(String.valueOf(totQty));

			}		
			cropHarvest.setCropHarvestDetails(cropHarvestDetailSet);
			 if(!ObjectUtil.isEmpty(getCurrentSeasonsCode()) && !StringUtil.isEmpty(getCurrentSeasonsCode())){
		            cropHarvest.setSeasonCode(getCurrentSeasonsCode());
		            }
			farmerService.saveCropHarvest(cropHarvest);
			result = "redirect";
		}
		printAjaxResponse(receiptNumber,"");
		return result;

	}

	/*
	 * private Map<String, Object> getTotalYieldOfQuantity(CropHarvest
	 * cropHarvest) {
	 * 
	 * double qty = 0;
	 * 
	 * Map<String, Object> details = new HashMap<String, Object>(); if
	 * (!ObjectUtil.isListEmpty(cropHarvest.getCropHarvestDetails())) { for
	 * (CropHarvestDetails detail : cropHarvest.getCropHarvestDetails()) {
	 * 
	 * qty = qty + detail.getQty();
	 * 
	 * } } details.put(TOTAL_YIELD_QUANTITY, qty);
	 * 
	 * 
	 * return details; }
	 */

	public String getHarvestDate() {
		return harvestDate;
	}

	public void setHarvestDate(String harvestDate) {
		this.harvestDate = harvestDate;
	}

	public String getProductTotalString() {
		return productTotalString;
	}

	public void setProductTotalString(String productTotalString) {
		this.productTotalString = productTotalString;
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

	public String getTotalSaleQty() {
		return totalSaleQty;
	}

	public void setTotalSaleQty(String totalSaleQty) {
		this.totalSaleQty = totalSaleQty;
	}

	public String detail() {

		String view = "";

		if (id != null && !id.equals("")) {
			getCurrentSeasonsCode();
			getCurrentSeasonsName();
			cropHarvest = productService.findCropHarvestById(Long.valueOf(id));
			
			if (cropHarvest == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			 
			  metricTon = Double.parseDouble(cropHarvest.getTotalQty());
			/*
			 * List<CropHarvestDetails> CropHarvestDetailsList = productService
			 * .listCropHarvestDetails(filter.getCropHarvest().getId());
			 */
			setCurrentPage(getCurrentPage());
			command = UPDATE;
			view = DETAIL;
			request.setAttribute(HEADING, getText("cropHarvestSRdetail"));
		} else {
			request.setAttribute(HEADING, getText("cropHarvestSRlist"));
			return LIST;
		}
		return view;
	}

	public String update() throws Exception {

		if (id != null && !id.equals("")) {
			getCurrentSeasonsCode();
			getCurrentSeasonsName();
			cropHarvest = productService.findCropHarvestById(Long.valueOf(id));
			cropHarvest.setHarvestDate(DateUtil.getDateWithoutTime(cropHarvest.getHarvestDate()));
			if (cropHarvest == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			setCurrentPage(getCurrentPage());
			id = null;
			command = UPDATE;
			request.setAttribute(HEADING, getText(UPDATE));
		} else {
			if (cropHarvest != null) {
				CropHarvest temp = productService.findCropHarvestById(cropHarvest.getId());
				if (temp == null) {
					addActionError(NO_RECORD);
					return REDIRECT;
				}
				setCurrentPage(getCurrentPage());
				temp.setFarmCode(cropHarvest.getFarmCode());
				temp.setFarmerId(cropHarvest.getFarmerId());
				temp.setFarmerName(cropHarvest.getFarmerName());
				temp.setFarmName(cropHarvest.getFarmName());
				temp.setHarvestDate(cropHarvest.getHarvestDate());
				temp.setReceiptNo(cropHarvest.getReceiptNo());
				temp.setTotalQty(cropHarvest.getTotalQty());
				temp.setStorageIn(getStorage());
				temp.setPackedIn(getPacked());
				// temp.setCode(country.getCode());
				productService.updateCropHarvest(temp);
			}
			request.setAttribute(HEADING, getText("cropHarvestSRlist"));
			return LIST;
		}
		return super.execute();
	}

	public String delete() {
		if (!StringUtil.isEmpty(id)) {
			CropHarvest cropHarvest = productService.findCropHarvestById(Long.valueOf(id));
			productService.removeCropHarvest(cropHarvest);
			setCurrentPage(getCurrentPage());
		}
		return LIST;
	}

	private void setCurrentPage(Object currentPage) {
		// TODO Auto-generated method stub

	}

	public String getCurrentPage() {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<Integer, String> getCropTypeList() {

		Map<Integer, String> cropListMap = new LinkedHashMap<Integer, String>();
		cropListMap.put(0, getText("ct0"));
		 if(getCurrentTenantId().equalsIgnoreCase("chetna")){
		 cropListMap.put(1, getText("ct1"));}
		 if(getCurrentTenantId().equalsIgnoreCase("pratibha")){
			 cropListMap.put(1, getText("cs1"));
			 cropListMap.put(2, getText("cs2"));
			 cropListMap.put(3, getText("cs3"));
			 cropListMap.put(4, getText("cs4"));
			 cropListMap.put(5, getText("cs5"));
			 }
		/*cropListMap.put(2, getText("ct2"));*/
		return cropListMap;

	}

	public Map<String, String> getCropList() {

		Map<String, String> cropTypeNameList = new LinkedHashMap<String, String>();
		List<CropHarvestDetails> nameList = productService.listOfCrops();
		for (CropHarvestDetails cropHarvestDetails : nameList) {
			cropTypeNameList.put(String.valueOf(cropHarvestDetails.getCrop().getId()),
					cropHarvestDetails.getCrop().getCode() + "-" + cropHarvestDetails.getCrop().getName());

		}
		return cropTypeNameList;

	}

	protected void printAjaxResponse(Object value, String contentType) {

		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			if (!StringUtil.isEmpty(contentType))
				response.setContentType(contentType);
			out = response.getWriter();
			out.print(value);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void populateCrop() {
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<ProcurementProduct> procurementProducts = null;
		if (!StringUtil.isEmpty(cropTypeCode)) {
			procurementProducts = productService.listProcurmentProductsByType(cropTypeCode);
		}
		if (!ObjectUtil.isEmpty(procurementProducts)) {
			JSONObject jsonObject = new JSONObject();
			for (ProcurementProduct procurementProduct : procurementProducts) {
				jsonObject.put(procurementProduct.getId(), procurementProduct.getName());
				jsonObjects.add(jsonObject);
			}
		}
		printAjaxResponse(jsonObjects, "text/html");
	}

	public void populateVariety() {
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<ProcurementVariety> procurementVarieties = null;
		if (!StringUtil.isEmpty(cropId)) {
			procurementVarieties = productService.listProcurmentVarirtyByProcurementProductId(cropId);
		}
		if (!ObjectUtil.isEmpty(procurementVarieties)) {
			JSONObject jsonObject = new JSONObject();
			for (ProcurementVariety procurementVarietiy : procurementVarieties) {
				jsonObject.put(procurementVarietiy.getId(), procurementVarietiy.getName());
				jsonObjects.add(jsonObject);
			}
		}
		printAjaxResponse(jsonObjects, "text/html");

	}

	public void populateGrade() {

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<ProcurementGrade> procurementGrades = null;
		if (!StringUtil.isEmpty(varietyId)) {
			procurementGrades = productService.listProcurmentGradeByVarityId(varietyId);
		}
		if (!ObjectUtil.isEmpty(procurementGrades)) {
			JSONObject jsonObject = new JSONObject();
			for (ProcurementGrade procurementGrade : procurementGrades) {
				jsonObject.put(procurementGrade.getId(), procurementGrade.getName());
				jsonObjects.add(jsonObject);
			}
		}
		printAjaxResponse(jsonObjects, "text/html");

	}

	@SuppressWarnings("unchecked")
	public void populateCropHarvestDetailList() {
		JSONArray array = new JSONArray();
		List<CropHarvestDetails> cropHarvestDetails = productService.listCropHarvestDetailsByHarvestId(cropHarvestId);
		if (!ObjectUtil.isEmpty(cropHarvestDetails)) {
			for (CropHarvestDetails harvestDetails : cropHarvestDetails) {
				JSONObject obj = new JSONObject();
				obj.put("cropType", getText("ct" + harvestDetails.getCropType()));
				obj.put("crop", harvestDetails.getCrop().getName());
					obj.put("variety", harvestDetails.getVariety().getName());
				obj.put("grade", harvestDetails.getGrade().getName());
				if(harvestDetails.getCrop().getUnit()!=null && !StringUtil.isEmpty(harvestDetails.getCrop().getUnit())){
				obj.put("unit", harvestDetails.getCrop().getUnit());
				if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {
					if(harvestDetails.getCrop().getUnit().equalsIgnoreCase("kgs") || harvestDetails.getCrop().getUnit().equalsIgnoreCase("kg")){
						obj.put("quantityMT", (new DecimalFormat("#0.000").format(harvestDetails.getQty()/1000)));	
					}
					if(harvestDetails.getCrop().getUnit().equalsIgnoreCase("Quintals") || harvestDetails.getCrop().getUnit().equalsIgnoreCase("Quintal")){
						obj.put("quantityMT", (new DecimalFormat("#0.000").format(harvestDetails.getQty()/10)));	
					}
				}
				}else
				{
					obj.put("unit"," ");
				}
				obj.put("quantity", harvestDetails.getQty());
				
				obj.put("price", harvestDetails.getPrice());
				obj.put("subTotal", harvestDetails.getSubTotal());
				obj.put("id", harvestDetails.getId());

				array.add(obj);
			}
			JSONObject mainObj = new JSONObject();
			mainObj.put("data", array);
			printAjaxResponse(mainObj, "text/html");
		}
	}

	public void populateHarvestDetail() {
		JSONObject jsonObject = new JSONObject();
		String msg = "";
		Type listType1 = new TypeToken<List<CropHarvestDetails>>() {
		}.getType();

		List<CropHarvestDetails> cropHarvestDetailsList = new Gson().fromJson(harvestDeatailJsonString, listType1);
		if (!ObjectUtil.isEmpty(cropHarvestDetailsList)) {
			for (int j = 0; j < cropHarvestDetailsList.size(); j++) {
				CropHarvestDetails newCropHarvestDetails = new CropHarvestDetails();
				CropHarvestDetails cropHarvestDetails = productService.findCropHarvestDetailsbyHarvestIdandtherItems(
						harvestId, cropHarvestDetailsList.get(j).getCropType(),
						cropHarvestDetailsList.get(j).getCropId(), cropHarvestDetailsList.get(j).getVarietyId(),
						cropHarvestDetailsList.get(j).getGradeId());
				if (cropHarvestDetails != null && !ObjectUtil.isEmpty(cropHarvestDetails)) {
					// Double valeu =
					// cropHarvestDetails.getPrice()+cropHarvestDetailsList.get(j).getPrice(),cropHarvestDetails.getQty()+cropHarvestDetailsList.get(j).getQty()+cropHarvestDetails.getSubTotal()+cropHarvestDetailsList.get(j).getSubTotal();
					cropHarvestDetails.setPrice(0.00);
					cropHarvestDetails.setQty(cropHarvestDetailsList.get(j).getQty());
					//cropHarvestDetails.setQty(cropHarvestDetails.getQty() + cropHarvestDetailsList.get(j).getQty());
					//cropHarvestDetails.setQty(cropHarvestDetailsList.get(j).getQty());   Commented as per bug#401
					cropHarvestDetails.setSubTotal(0.00);
					productService.updateCropHarvestDetails(cropHarvestDetails);
				} else {

					CropHarvest cropHarvest = productService.findCropHarvestById(harvestId);
					newCropHarvestDetails.setCropType(cropHarvestDetailsList.get(j).getCropType());
					ProcurementProduct crop = productService
							.findProcurementProductById(cropHarvestDetailsList.get(j).getCropId());
					ProcurementVariety procurementVariety = productService
							.findProcurementVarietyById(cropHarvestDetailsList.get(j).getVarietyId());
					ProcurementGrade procurementGrade = productService
							.findProcurementGradeById(cropHarvestDetailsList.get(j).getGradeId());
					newCropHarvestDetails.setCrop(crop);
					newCropHarvestDetails.setVariety(procurementVariety);
					newCropHarvestDetails.setGrade(procurementGrade);
					newCropHarvestDetails.setPrice(0.00);
					newCropHarvestDetails.setQty(cropHarvestDetailsList.get(j).getQty());
					newCropHarvestDetails
							.setSubTotal(0.00);
					newCropHarvestDetails.setCropHarvest(cropHarvest);
					productService.saveCropHarvestDetails(newCropHarvestDetails);
				}

				// inventryArr.add(getJSONObject(state.getId(),
				// state.getName()));
			}
			jsonObject.put("msg", "success");
		}

		printAjaxResponse(jsonObject, "text/html");
	}

	public String deleteCropHarvestDetail() {

		CropHarvestDetails cropHarvestDetails = productService.findCropHarvestDetailsById(templateId);
		if (!ObjectUtil.isEmpty(cropHarvestDetails)) {
			productService.removeCropHarvestDetails(cropHarvestDetails);
		}
		try {
			JSONObject js = new JSONObject();
			js.put("msg", getText("msg.removed"));
			printAjaxResponse(js, "text/html");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public String populateXLS() throws Exception {

		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getText("cropHarvestReport") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("cropHarvestReport"), fileMap, ".xls"));
		return "xls";
	}

	@SuppressWarnings("unchecked")
	public InputStream getExportDataStream(String exportType) throws IOException {

		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);

		DateFormat df = new SimpleDateFormat(getGeneralDateFormat());

		setsDate(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));

		boolean flag = true;
		DateFormat filterDateFormat = new SimpleDateFormat(FILTERDATE);

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("exportCropHarvestTitle"));
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

		HSSFRow row, titleRow, filterRowTitle, filterRow1, filterRow2, filterRow3, filterRow4 = null, filterRow5 = null;
		HSSFCell cell;

		int imgRow1 = 0;
		int imgRow2 = 4;
		int imgCol1 = 0;
		int imgCol2 = 1;
		int titleRow1 = 2;
		int titleRow2 = 3;

		int rowNum = 2;
		int colNum = 0;

		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, titleRow1, titleRow2));

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(2);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("exportCropHarvestTitle")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);
		if (isMailExport()) {
			rowNum++;
			rowNum++;
			filterRowTitle = sheet.createRow(rowNum++);

			cell = filterRowTitle.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("filter")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);

			filterRow1 = sheet.createRow(rowNum++);

			cell = filterRow1.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("startingDate")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);

			cell = filterRow1.createCell(2);
			cell.setCellValue(new HSSFRichTextString(filterDateFormat.format(getsDate())));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);

			filterRow2 = sheet.createRow(rowNum++);

			cell = filterRow2.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("endingDate")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);

			cell = filterRow2.createCell(2);
			cell.setCellValue(new HSSFRichTextString(filterDateFormat.format(geteDate())));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);

			filterRow3 = sheet.createRow(rowNum++);
			filterRow4 = sheet.createRow(rowNum++);

		}
		rowNum++;

		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		String mainGridColumnHeaders = getLocaleProperty("harvestExportColumnHeader");
		int mainGridIterator = 0;

		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {

			cell = mainGridRowHead.createCell(mainGridIterator);
			cell.setCellValue(new HSSFRichTextString(cellHeader));
			style2.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
			style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cell.setCellStyle(style2);
			font2.setBoldweight((short) 12);
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			style2.setFont(font2);
			sheet.setColumnWidth(mainGridIterator, (15 * 550));
			mainGridIterator++;
		}
		if (ObjectUtil.isEmpty(this.cropHarvest))
			this.cropHarvest = new CropHarvest();

		super.filter = this.filter;

		Map data = isMailExport() ? readData() : readExportData();

		List<CropHarvestDetails> mainGridRows = (List<CropHarvestDetails>) data.get(ROWS);

		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;

		for (CropHarvestDetails cropHarvestDetails : mainGridRows) {

			if (!ObjectUtil.isEmpty(filter) && !StringUtil.isEmpty(cropHarvest.getFarmerId()) && flag) {
				cell = filterRow4.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmerId")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow4.createCell(2);
				cell.setCellValue(
						new HSSFRichTextString(cropHarvest.getFarmerName() + " - " + cropHarvest.getFarmerId()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if (!ObjectUtil.isEmpty(cropHarvest.getFarmCode()) && !StringUtil.isEmpty(cropHarvest.getFarmCode())
					&& flag) {
				cell = filterRow4.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmCode")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow4.createCell(2);
				cell.setCellValue(
						new HSSFRichTextString(cropHarvest.getFarmName() + " - " + cropHarvest.getFarmCode()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				flag = false;
			}

			row = sheet.createRow(rowNum++);
			colNum = 0;

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(
					!ObjectUtil.isEmpty(sdf.format(cropHarvestDetails.getCropHarvest().getHarvestDate()))
							? sdf.format(cropHarvestDetails.getCropHarvest().getHarvestDate()).toString() : ""));

			cell = row.createCell(colNum++);
			cell.setCellValue(
					new HSSFRichTextString((!StringUtil.isEmpty(cropHarvestDetails.getCropHarvest().getFarmerId() + "-"
							+ cropHarvestDetails.getCropHarvest().getFarmerName())
									? cropHarvestDetails.getCropHarvest().getFarmerId() + "-"
											+ cropHarvestDetails.getCropHarvest().getFarmerName()
									: "")));

			cell = row.createCell(colNum++);
			cell.setCellValue(
					new HSSFRichTextString((!StringUtil.isEmpty(cropHarvestDetails.getCropHarvest().getFarmCode() + "-"
							+ cropHarvestDetails.getCropHarvest().getFarmName())
									? cropHarvestDetails.getCropHarvest().getFarmCode() + "-"
											+ cropHarvestDetails.getCropHarvest().getFarmName()
									: "")));

			Farmer farmer = farmerService.findFarmerByFarmerId(cropHarvestDetails.getCropHarvest().getFarmerId());

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(
					(!StringUtil.isEmpty(farmer.getVillage().getName()) ? farmer.getVillage().getName() : "")));

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(String.valueOf(getText("ct" + cropHarvestDetails.getCropType()))));

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString((!StringUtil
					.isEmpty(cropHarvestDetails.getCrop().getCode() + "-" + cropHarvestDetails.getCrop().getName())
							? cropHarvestDetails.getCrop().getCode() + "-" + cropHarvestDetails.getCrop().getName()
							: "")));

			cell = row.createCell(colNum++);
			cell.setCellValue(
					new HSSFRichTextString(String.valueOf(cropHarvestDetails.getCropHarvest().getTotalQty())));

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(
					String.valueOf(CurrencyUtil.getDecimalFormat((cropHarvestDetails.getSubTotal() * cropHarvestDetails.getPrice()),"##.00"))));

			// CropHarvestDetails cropHarvestdetails =
			// productService.findCropHarvestDetailsById(Long.valueOf(id));

			/*
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(String.valueOf(cropHarvestdetails.getCropType(
			 * ))));
			 */

			/*
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(String.valueOf(cropHarvestdetails.getCrop())))
			 * ;
			 */

		}

		int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(400, 10, 655, 200, (short) 0, 0, (short) 0, 0);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		picture.resize();
		String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
				: request.getSession().getId();
		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("cropHarvestReport") + fileNameDateFormat.format(new Date()) + ".xls";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		wb.write(fileOut);
		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		fileOut.close();

		return stream;
	}

	public int getPicIndex(HSSFWorkbook wb) throws IOException {

		int index = -1;

		byte[] picData = null;
		picData = clientService.findLogoByCode(Asset.APP_LOGO);
		if (picData != null)
			index = wb.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
	}

	/**
	 * Populate populateFarmer.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public void populateFarmer() throws Exception {

		farmers = new ArrayList<Farmer>();
		if (!selectedVillage.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedVillage))) {

			farmers = farmerService.listOfFarmers(selectedVillage);
		}
		JSONArray farmerArr = new JSONArray();
		if (!ObjectUtil.isEmpty(farmers)) {

			// farmerArr.add(farmers.stream().forEach(obj->getJSONObject(obj.getFarmerId(),
			// obj.getName()+""+obj.getFarmerId()));

			for (Farmer farmer : farmers) {
				if(farmer.getFarmerCode()==null){
					farmerArr.add(getJSONObject(farmer.getFarmerId(), farmer.getName()));
				}
				else{
					farmerArr.add(getJSONObject(farmer.getFarmerId(), farmer.getName() + "-" + farmer.getFarmerCode()));
				}
				
			}
		}
		sendAjaxResponse(farmerArr);
	}

	public ILocationService getLocationService() {

		return locationService;
	}

	public void setLocationService(ILocationService locationService) {

		this.locationService = locationService;
	}

	public String getFarmerId() {

		return farmerId;
	}

	public void setFarmerId(String farmerId) {

		this.farmerId = farmerId;
	}

	public String getFarmCode() {

		return farmCode;
	}

	public void setFarmCode(String farmCode) {

		this.farmCode = farmCode;
	}

	public String getCropType() {

		return cropType;
	}

	public void setCropType(String cropType) {

		this.cropType = cropType;
	}

	public IFarmerService getFarmerService() {

		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {

		this.farmerService = farmerService;
	}

	public List<String> getFields() {

		return fields;
	}

	public void setFields(List<String> fields) {

		this.fields = fields;
	}

	public IProductService getProductService() {

		return productService;
	}

	public void setProductService(IProductService productService) {

		this.productService = productService;
	}

	public InputStream getFileInputStream() {

		return fileInputStream;
	}

	public void setFileInputStream(InputStream fileInputStream) {

		this.fileInputStream = fileInputStream;
	}

	public IProductDistributionService getProductDistributionService() {

		return productDistributionService;
	}

	public void setProductDistributionService(IProductDistributionService productDistributionService) {

		this.productDistributionService = productDistributionService;
	}

	public String getXlsFileName() {

		return xlsFileName;
	}

	public void setXlsFileName(String xlsFileName) {

		this.xlsFileName = xlsFileName;
	}

	/**
	 * populateCropName.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public void populateCropName() throws Exception {
		List<Object[]>	cropNamesList  =new ArrayList<>();
	if (!selectedCropType.equalsIgnoreCase("null") && !StringUtil.isEmpty(selectedCropType) && !selectedFarm.equalsIgnoreCase("null") && !StringUtil.isEmpty(selectedFarm) )
	{
		
		//Farm farm=farmerService.findFarmByCode(selectedFarm);
   cropNamesList = farmerService.listOfCropNamesByCropTypeAndFarm(selectedCropType,selectedFarm);
	}
	
	JSONArray cropsArr = new JSONArray();
	if (!ObjectUtil.isEmpty(cropNamesList)) {
		for (Object[] crops : cropNamesList) {
			
			cropsArr.add(getJSONObject(crops[0], crops[1]));
		}
	}
	sendAjaxResponse(cropsArr);
}

	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}

	/**
	 * populateVarietyName.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public void populateVarietyName() throws Exception {
		if (!selectedCropType.equalsIgnoreCase("null") && !StringUtil.isEmpty(selectedCropType)
				&& !selectedFarm.equalsIgnoreCase("null") && !StringUtil.isEmpty(selectedFarm) && 
				!selectedCropName.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedCropName))) {
			varietysList = farmerService.listOfVarietyByCropTypeFarmCodeAndCrop(selectedCropType, selectedFarm,selectedCropName);
		}
		
		DateFormat df = new SimpleDateFormat(getGeneralDateFormat());
		
		
		JSONArray varietyArr = new JSONArray();
		if (!ObjectUtil.isEmpty(varietysList)) {
			for (FarmCrops variety : varietysList) { 
				String sow = variety.getSowingDate()==null ||StringUtil.isEmpty(variety.getSowingDate()) ? "" : df.format(variety.getSowingDate());
				//String sow = variety.getSowingDate()==null ||StringUtil.isEmpty(variety.getSowingDate()) ? "" : df.format(variety.getSowingDate());
				//	varietyArr.add(getJSONObject(variety.getProcurementVariety().getId(), variety.getProcurementVariety().getName()+"~"+sow));
				varietyArr.add(getJSONObject(variety.getProcurementVariety().getId(), variety.getProcurementVariety().getName()+"~"+sow));
			}
		}
		sendAjaxResponse(varietyArr);
	}
	/**
	 * populateGradeName.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public void populateGradeName() throws Exception {
		if ( (!StringUtil.isEmpty(selectedVariety) && !selectedVariety.equalsIgnoreCase("null"))) {
			gradesList = farmerService.listOfGrade(selectedVariety);
		}

		JSONArray gradeArr = new JSONArray();
		if (!ObjectUtil.isEmpty(gradesList)) {
			for (ProcurementGrade grade : gradesList) {
				gradeArr.add(getJSONObject(grade.getId(), grade.getName()));
			}
		}
		sendAjaxResponse(gradeArr);
	}
	
	
	/*public void populateGradeUnit() throws Exception {

		String result = "";
		
		JSONArray productArr = new JSONArray();
		if (!StringUtil.isEmpty(selectedGrade)) {
			ProcurementGrade grade = productService.findGradeUnitByGradeId(Long.valueOf(selectedGrade));

			if (!ObjectUtil.isEmpty(grade) && grade.getType() != null) {
				productAvailableUnit = String.valueOf(grade.getType().getName());
				result = productAvailableUnit;
			}
			
			result = productAvailableUnit == null ? "" : productAvailableUnit;
		}
		productArr.add(getJSONObject("unit", result));
		
		sendAjaxResponse(productArr);
	}*/
	

	public void populatePrice() throws Exception {
		Double gradePrice = 0.0;
		if (!selectedGrade.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedGrade))) {
			gradePrice = farmerService.findGradePrice(selectedGrade);
		}
		JSONArray gradeArr = new JSONArray();
		if (!ObjectUtil.isEmpty(gradePrice)) {

			gradeArr.add(CurrencyUtil.getDecimalFormat(gradePrice, "##.00"));

		}
		sendAjaxResponse(gradeArr);
	}

	/**
	 * Gets the village list.
	 * 
	 * @return the village list
	 */
	public Map<Long, String> getVillageList() {

		Map<Long, String> villageMap = new LinkedHashMap<Long, String>();

		List<Village> villageList = locationService.listVillage();
		for (Village obj : villageList) {
		//	villageMap.put(obj.getId(), obj.getName());
			villageMap.put(obj.getId(), obj.getName() + " - " + obj.getCode());
		}
		return villageMap;

	}

	public void setVillages(Map<String, String> villages) {
		this.villages = villages;
	}

	public Map<String, String> getFarmerList() {
		Map<String, String> FarmerList = new HashMap<String, String>();
		List<Farmer> farmers = farmerService.findFarmerList();
		farmers.forEach(farmer -> FarmerList.put(String.valueOf(farmer.getFarmerCode()) + "-" + farmer.getName(),
				farmer.getName()));
		farmers.forEach(farmer -> FarmerList.put(String.valueOf(farmer.getFarmerCode()) + "-" + farmer.getName(),
				farmer.getName()));
		return FarmerList;
	}

	public void setFarmerList(Map<String, String> farmerList) {
		this.farmerList = farmerList;
	}

	public void setFarmList(Map<String, String> farmList) {
		this.farmList = farmList;
	}

	public Map<Integer, String> getVarietyList() {
		Map<Integer, String> varities = new HashMap<Integer, String>();
		List<ProcurementVariety> procurementVarieties = productService.listProcurementVariety();
		for (ProcurementVariety procurementVariety : procurementVarieties) {
			varities.put(Integer.valueOf(String.valueOf(procurementVariety.getId())), procurementVariety.getName());
		}
		return varities;
	}

	public void setVarietyList(Map<Integer, String> varietyList) {
		this.varietyList = varietyList;
	}

	public Map<Integer, String> getGradeList() {
		Map<Integer, String> varities = new HashMap<Integer, String>();
		List<ProcurementGrade> procurementgrades = productService.listProcurementGrade();
		for (ProcurementGrade procurementgrade : procurementgrades) {
			varities.put(Integer.valueOf(String.valueOf(procurementgrade.getId())), procurementgrade.getName());
		}
		return varities;
	}

	/**
	 * Populate Farm.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public void populateFarm() throws Exception {

		if (!selectedFarmer.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedFarmer))) {
			String farmerId[] = selectedFarmer.split("-");
			
			farms = farmerService.listFarmerFarmByFarmerId(selectedFarmer);
		}
		JSONArray farmsArr = new JSONArray();
		if (!ObjectUtil.isEmpty(farms)) {
			for (Farm farm : farms) {
				farmsArr.add(getJSONObject(farm.getFarmCode(), farm.getFarmName() + "-" + farm.getFarmCode()));
			}
		}
		sendAjaxResponse(farmsArr);
	}
	 
	 /*public Map<String, String> getStorageId() {
			
			Map<String, String> storageIdList = new LinkedHashMap<>();
			FarmCatalogueMaster farmCatalougeMaster = catalogueService.findFarmCatalogueMasterByName(getText("storageIdType"));
			if (!ObjectUtil.isEmpty(farmCatalougeMaster)) {
				Double d = new Double(farmCatalougeMaster.getId());
				List<FarmCatalogue> farmCatalougeList = catalogueService.findFarmCatalougeByType(d.intValue());
				storageIdList = farmCatalougeList.stream().collect(Collectors.toMap(
						FarmCatalogue::getCode,FarmCatalogue::getName));
			}
			return storageIdList;
		}
	 */
	 
	public void setGradeList(Map<Integer, String> gradeList) {
		this.gradeList = gradeList;
	}

	public void setCropTypeList(Map<Integer, String> cropTypeList) {
		this.cropTypeList = cropTypeList;
	}

	public void setCropList(Map<Integer, String> cropList) {
		this.cropList = cropList;
	}

	public String getCropTypeCode() {
		return cropTypeCode;
	}

	public void setCropTypeCode(String cropTypeCode) {
		this.cropTypeCode = cropTypeCode;
	}

	public String getCropId() {
		return cropId;
	}

	public void setCropId(String cropId) {
		this.cropId = cropId;
	}

	public String getVarietyId() {
		return varietyId;
	}

	public void setVarietyId(String varietyId) {
		this.varietyId = varietyId;
	}

	public long getCropHarvestId() {
		return cropHarvestId;
	}

	public void setCropHarvestId(long cropHarvestId) {
		this.cropHarvestId = cropHarvestId;
	}

	public CropHarvest getCropHarvest() {
		return cropHarvest;
	}

	public void setCropHarvest(CropHarvest cropHarvest) {
		this.cropHarvest = cropHarvest;
	}

	public List<CropHarvestDetails> getCropHarvestDetailsList() {
		return cropHarvestDetailsList;
	}

	public void setCropHarvestDetailsList(List<CropHarvestDetails> cropHarvestDetailsList) {
		this.cropHarvestDetailsList = cropHarvestDetailsList;
	}

	public String getHarvestDeatailJsonString() {
		return harvestDeatailJsonString;
	}

	public void setHarvestDeatailJsonString(String harvestDeatailJsonString) {
		this.harvestDeatailJsonString = harvestDeatailJsonString;
	}

	public long getHarvestId() {
		return harvestId;
	}

	public void setHarvestId(long harvestId) {
		this.harvestId = harvestId;
	}

	public long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}

	public String getSelectedBuyer() {
		return selectedBuyer;
	}

	public void setSelectedBuyer(String selectedBuyer) {
		this.selectedBuyer = selectedBuyer;
	}

	public String getSelectedGrade() {
		return selectedGrade;
	}

	public void setSelectedGrade(String selectedGrade) {
		this.selectedGrade = selectedGrade;
	}

	public String getSelectedCropType() {
		return selectedCropType;
	}

	public void setSelectedCropType(String selectedCropType) {
		this.selectedCropType = selectedCropType;
	}

	public String getSelectedCropName() {
		return selectedCropName;
	}

	public void setSelectedCropName(String selectedCropName) {
		this.selectedCropName = selectedCropName;
	}

	public String getSelectedVariety() {
		return selectedVariety;
	}

	public void setSelectedVariety(String selectedVariety) {
		this.selectedVariety = selectedVariety;
	}

	public String getSearchPage() {
		return searchPage;
	}

	public void setSearchPage(String searchPage) {
		this.searchPage = searchPage;
	}

	@Override
	public Object getData() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMailExport(boolean mailExport) {

		this.mailExport = mailExport;
	}

	public Date getsDate() {

		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		if (!StringUtil.isEmpty(startDate)) {
			try {
				sDate = df.parse(startDate);
			} catch (ParseException e) {
				LOGGER.error("Error parsing start date" + e.getMessage());
			}
		}
		return sDate;
	}

	public void setsDate(Date sDate) {

		this.sDate = sDate;
	}

	public String getProperty(String key) {

		if (mailExport) {
			return getText(key);
		}
		// Scheduler
		if (ObjectUtil.isEmpty(errors)) {
			errors = new Properties();
			try {
				errors.load(getClass().getResourceAsStream(getClass().getSimpleName() + "_en.properties"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return errors.getProperty(key);
	}

	public boolean isMailExport() {

		return mailExport;
	}

	public Date geteDate() {

		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		if (!StringUtil.isEmpty(endDate)) {
			try {
				eDate = df.parse(endDate);
				eDate.setTime(eDate.getTime() + 86399000);
			} catch (ParseException e) {
				LOGGER.error("Error parsing end date" + e.getMessage());
			}

		}
		return eDate;
	}

	/**
	 * Read data.
	 * 
	 * @return the map
	 */
	@SuppressWarnings("rawtypes")
	public Map readData() {

		Map data = reportService.listWithMultipleFiltering(getSord(), getSidx(), getStartIndex(), getLimit(),
				getsDate(), geteDate(), filter, getPage(), null);
		return data;
	}

	/**
	 * Read data.
	 * 
	 * @param projectionToken
	 *            the projection token
	 * @return the map
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map readData(String projectionToken) {

		Map<String, String> projectionProperties = !StringUtil.isEmpty(projectionToken)
				? getProjectionProperties(projectionToken) : null;
		Map data = reportService.listWithMultipleFiltering(getSord(), getSidx(), getStartIndex(), getLimit(),
				getsDate(), geteDate(), filter, getPage(), projectionProperties);
		return data;
	}

	/**
	 * Read export data.
	 * 
	 * @return the map
	 */
	public Map readExportData() {

		Map data = reportService.listWithMultipleFiltering("desc", "id", getStartIndex(), getLimit(), getsDate(),
				new Date(), filter, getPage(), null);
		return data;
	}

	/**
	 * Gets the sord.
	 * 
	 * @return the sord
	 */
	public String getSord() {

		return sord;
	}

	/**
	 * Sets the sord.
	 * 
	 * @param sord
	 *            the new sord
	 */
	public void setSord(String sord) {

		this.sord = sord;
	}

	/**
	 * Gets the sidx.
	 * 
	 * @return the sidx
	 */
	public String getSidx() {

		return sidx;
	}

	/**
	 * Sets the sidx.
	 * 
	 * @param sidx
	 *            the new sidx
	 */
	public void setSidx(String sidx) {

		this.sidx = sidx;
	}

	/**
	 * Gets the projection properties.
	 * 
	 * @param token
	 *            the token
	 * @return the projection properties
	 */
	@SuppressWarnings("rawtypes")
	public Map getProjectionProperties(String token) {

		Map<String, String> projectionProperties = new HashMap<String, String>();
		projectionProperties.put(IReportDAO.PROJ_GROUP, getText(token + IReportDAO.PROJ_GROUP));
		projectionProperties.put(IReportDAO.PROJ_SUM, getText(token + IReportDAO.PROJ_SUM));
		projectionProperties.put(IReportDAO.PROJ_OTHERS, getText(token + IReportDAO.PROJ_OTHERS));
		return projectionProperties;
	}

	/**
	 * Gets the limit.
	 * 
	 * @return the limit
	 */
	public int getLimit() {

		return rows;
	}

	public ICatalogueService getCatalogueService() {
		return catalogueService;
	}

	public void setCatalogueService(ICatalogueService catalogueService) {
		this.catalogueService = catalogueService;
	}

	public Map<String, String> getStorageId() {
		
		FarmCatalogueMaster farmCatalougeMaster = catalogueService.findFarmCatalogueMasterByName(getText("Stored In"));
		if (!ObjectUtil.isEmpty(farmCatalougeMaster)) {
			Double d = new Double(farmCatalougeMaster.getId());
			List<FarmCatalogue> farmCatalougeList = catalogueService.findFarmCatalougeByType(d.intValue());
			for(FarmCatalogue fc:farmCatalougeList){
				if(!fc.getName().trim().contentEquals(ESESystem.OTHERS))
				{
					storageId.put(fc.getCode(), fc.getName());
				}
				
			}
			storageId.put("99", getText("cropHarvest.others"));
		}
		storageId.put("99","Others");
		return storageId;
	}
	
	
	 public Map<String, String> getPackId() {
		 
			FarmCatalogueMaster farmCatalougeMaster = catalogueService.findFarmCatalogueMasterByName(getText("Packed In"));
			if (!ObjectUtil.isEmpty(farmCatalougeMaster)) {
				Double d = new Double(farmCatalougeMaster.getId());
				List<FarmCatalogue> farmCatalougeList = catalogueService.findFarmCatalougeByType(d.intValue());
				for(FarmCatalogue fc:farmCatalougeList){
					if(!fc.getName().trim().contentEquals(ESESystem.OTHERS))
					{
						packId.put(fc.getCode(), fc.getName());
					}
			}
			}
				packId.put("99","Others");
				
			return packId;
			}

	public String getOtherStorageInType() {
		return otherStorageInType;
	}

	public void setOtherStorageInType(String otherStorageInType) {
		this.otherStorageInType = otherStorageInType;
	}

	public String getOtherPackedInType() {
		return otherPackedInType;
	}

	public void setOtherPackedInType(String otherPackedInType) {
		this.otherPackedInType = otherPackedInType;
	}

	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public String getPacked() {
		return packed;
	}

	public void setPacked(String packed) {
		this.packed = packed;
	}

	public Double getMetricTon() {
		return metricTon;
	}

	public void setMetricTon(Double metricTon) {
		this.metricTon = metricTon;
	}

	public String getProductAvailableUnit() {
		return productAvailableUnit;
	}

	public void setProductAvailableUnit(String productAvailableUnit) {
		this.productAvailableUnit = productAvailableUnit;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getSowingDate() {
		return sowingDate;
	}

	public void setSowingDate(String sowingDate) {
		this.sowingDate = sowingDate;
	}

	public void populateUnit() throws Exception {

		String result = "";
		
		JSONArray productArr = new JSONArray();
		if (!StringUtil.isEmpty(selectedCrop)) {
			ProcurementProduct procurementProduct = productService.findUnitByCropId(Long.valueOf(selectedCrop));

			if (!ObjectUtil.isEmpty(procurementProduct) && procurementProduct.getTypes() != null) {
				productAvailableUnit = String.valueOf(procurementProduct.getTypes().getName());
				result = productAvailableUnit;
			}
			
			result = productAvailableUnit == null ? "" : productAvailableUnit;
		}
		productArr.add(getJSONObject("unit", result));
		
		sendAjaxResponse(productArr);
	}

	public String getSelectedCrop() {
		return selectedCrop;
	}

	public void setSelectedCrop(String selectedCrop) {
		this.selectedCrop = selectedCrop;
	}	
	
	 
}


