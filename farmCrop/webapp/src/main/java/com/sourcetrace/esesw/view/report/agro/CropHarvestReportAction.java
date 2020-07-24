package com.sourcetrace.esesw.view.report.agro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
import org.apache.poi.ss.usermodel.CellStyle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.profile.CropHarvest;
import com.ese.entity.profile.CropHarvestDetails;
import com.ese.entity.util.ESESystem;
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
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.FarmCatalogueMaster;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.State;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

@SuppressWarnings("unchecked")
public class CropHarvestReportAction extends BaseReportAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ILocationService locationService;
	@Autowired
	private IProductService productService;
	private IProductDistributionService productDistributionService;
	@Autowired
	private IPreferencesService preferncesService;
	@Autowired
	private IAgentService agentService;
	private Map<String, String> fields = new HashMap<>();
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
	public static final String NA = "NA";
	private String farmerId;
	private String farmCode;
	private String cropType;
	private String crop;
	private CropHarvest filter;
	private String selectedFieldStaff;
	// private CropHarvestDetails filter;

	// private Set<CropHarvestDetails> cropHarvestDetails;
	private IFarmerService farmerService;
	private CropHarvest cropHarvest;
	private CropHarvestDetails cropHarvestDetails;
	private String gridDetails;
	private String xlsFileName;

	private String DETAIL = "detail";
	private String UPDATE = "update";
	private String NO_RECORD = "No_Records_Present";
	private String command;
	private String branchIdParma;
	private String enableSubgrid;
	private InputStream fileInputStream;
	private IClientService clientService;
	private String daterange;
	private String seasonCode;
	private String exportLimit;
	private String icsName;
	private String warehouseId;
	private String fpo;
	private String headerFields;
	private String stateName;
	@Autowired
	private ICatalogueService catalogueService;

	// List<Warehouse> samithi = new ArrayList<Warehouse>();
	public IClientService getClientService() {

		return clientService;
	}

	public void setClientService(IClientService clientService) {

		this.clientService = clientService;
	}

	public String list() {

		enableSubgrid = preferncesService.findPrefernceByName(ESESystem.ENABLE_MULTI_PRODUCT);
		/* CropHarvestDetails cropHarvestDetails = new CropHarvestDetails(); */
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
		filter = new CropHarvest();
		return LIST;
	}

	public String data() throws Exception {
		// this.filter = new CropHarvest();
		setFilter(new CropHarvest());

		if (!StringUtil.isEmpty(seasonCode)) {
			filter.setSeasonCode(seasonCode);
		}
		if (!StringUtil.isEmpty(farmerId)) {
			filter.setFarmerId(farmerId);

		}
		if (!StringUtil.isEmpty(farmCode)) {
			filter.setFarmCode(farmCode);
		}
		if (!StringUtil.isEmpty(crop)) {

			filter.setCropName(crop);
		}
		if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
			if (!StringUtil.isEmpty(stateName)) {
				// State s = locationService.findStateByCode(stateName);
				filter.setStateName(stateName);
			}

			if (!StringUtil.isEmpty(fpo)) {
				filter.setFpo(fpo);
			}
			
			if (!StringUtil.isEmpty(icsName)) {
				filter.setIcsname(icsName);
			}
			
		}
		if (getBranchId() != null && getBranchId().equals("organic")) {
		if (!StringUtil.isEmpty(icsName)) {

			filter.setIcsname(icsName);
		}
		}
		if (!StringUtil.isEmpty(selectedFieldStaff)) {
			String agentIds[] = selectedFieldStaff.split("-");
			String agentId = agentIds[0].trim();
			filter.setAgentId(agentId);
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

		super.filter = this.filter;
		Map data = readData("cropHarvest");
		return sendJSONResponse(data);
	}

	public JSONObject toJSON(Object obj) {
		DecimalFormat formatter = new DecimalFormat("0.00");
		DecimalFormat qtyFormat = new DecimalFormat("0.00");
		JSONObject jsonObject = new JSONObject();
		if (obj instanceof CropHarvestDetails) {
			CropHarvestDetails cropHarvestDetails = (CropHarvestDetails) obj;
			JSONArray rows = new JSONArray();
			rows.add(getText("ct" + cropHarvestDetails.getCropType()));
			rows.add(cropHarvestDetails.getCrop().getName());
			rows.add(cropHarvestDetails.getVariety().getName());
			rows.add(cropHarvestDetails.getGrade().getName());
			rows.add(cropHarvestDetails.getCrop().getUnit());
			/* rows.add(formatter.format(cropHarvestDetails.getPrice())); */
			if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {
				double metricTon = 0.00;
				rows.add(CurrencyUtil.getDecimalFormat(cropHarvestDetails.getQty(), "##.00") + "("
						+ cropHarvestDetails.getCrop().getUnit() + ")");

				if (cropHarvestDetails.getCrop().getUnit().equalsIgnoreCase("kg")
						|| cropHarvestDetails.getCrop().getUnit().equalsIgnoreCase("kgs")) {
					metricTon = cropHarvestDetails.getQty() / 1000;
				}
				if (cropHarvestDetails.getCrop().getUnit().equalsIgnoreCase("quintal")
						|| cropHarvestDetails.getCrop().getUnit().equalsIgnoreCase("quintals")) {
					metricTon = cropHarvestDetails.getQty() / 10;
				}

				rows.add(qtyFormat.format(metricTon));
			} else {
				rows.add(CurrencyUtil.getDecimalFormat(cropHarvestDetails.getQty(), "##.00"));
			}
			/* rows.add(formatter.format(cropHarvestDetails.getSubTotal())); */
			jsonObject.put("id", cropHarvestDetails.getId());
			jsonObject.put("cell", rows);
		} else {
			Object[] datas = (Object[]) obj;
			JSONArray rows = new JSONArray();
			Farmer farmer = farmerService.findFarmerByFarmerId(datas[2].toString());
			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(datas[6])))
							? getBranchesMap().get(getParentBranchMap().get(datas[6]))
							: getBranchesMap().get(datas[6]));
				}
				rows.add(getBranchesMap().get(datas[6]));

			} else {
				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(branchesMap.get(datas[6]));
				}
			}

			if (!ObjectUtil.isEmpty(datas[1])) {
				String date = "";
				if (!ObjectUtil.isEmpty(datas[1])) {
					ESESystem preferences = preferncesService.findPrefernceById("1");
					if (!StringUtil.isEmpty(preferences)) {
						DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
						rows.add(genDate.format(datas[1]));
					}
				}
				if (!StringUtil.isEmpty(datas[7])) {
					HarvestSeason season = farmerService.findSeasonNameByCode(datas[7].toString());
					rows.add(season == null ? "" : season.getName());
				} else {
					rows.add("NA");
				}
				if (!StringUtil.isEmpty(datas[9])) {
					 Agent agent = agentService.findAgentByAgentId(datas[9].toString());
					rows.add(agent == null ? "" : agent.getProfileIdWithName());
				} else {
					rows.add("NA");
				}
				
				
				/*rows.add(datas[3].toString());// farmer
*/		         String firstName =  String.valueOf(datas[3]);
				String farmerId =String.valueOf(datas[2]);
				Farmer f=farmerService.findFarmerByFarmerId(farmerId);
				if(!ObjectUtil.isEmpty(f) && f!=null){ // farmer
					String linkField = "<a href=farmer_detail.action?id="+f.getId()+" target=_blank>"+firstName+"</a>";
					rows.add(!ObjectUtil.isEmpty(linkField) ? linkField : "");
					}else{
						rows.add(firstName);
					}
				
				rows.add(datas[5].toString());
				if (datas[2].toString() != "") {
					if (!ObjectUtil.isEmpty(farmer)) {
						rows.add(farmer.getVillage().getName());
					} else {
						rows.add("");
					}
				}

				if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
					if (!ObjectUtil.isEmpty(farmer) && !StringUtil.isEmpty(farmer.getIcsName())) {
						FarmCatalogue cat = catalogueService.findCatalogueByCode(farmer.getIcsName());
						rows.add(!ObjectUtil.isEmpty(cat) ? cat.getName() : "");// ICS
																				// Name
					} else {

						rows.add("");
					}

				} else {
					/*
					 * if(!getCurrentTenantId().equalsIgnoreCase("agro") &&
					 * !getCurrentTenantId().equalsIgnoreCase("crsdemo") &&
					 * !getCurrentTenantId().equalsIgnoreCase("pratibha")){
					 */
					if (!getCurrentTenantId().equalsIgnoreCase("agro")
							&& !getCurrentTenantId().equalsIgnoreCase("crsdemo")) {
						if (getCurrentTenantId().equalsIgnoreCase("pratibha")){
								if (StringUtil.isEmpty(getBranchId()) || getBranchId().equalsIgnoreCase("organic")) {
							rows.add(!StringUtil.isEmpty(farmer.getIcsName()) ? farmer.getIcsName() : "");// ICS
								}																				// Name
						}
					}
				}

				/*
				 * if(getCurrentTenantId().equalsIgnoreCase("pratibha")){ if(
				 * StringUtil.isEmpty(getBranchId()) ||
				 * getBranchId().equalsIgnoreCase("organic")){ if
				 * (!StringUtil.isEmpty(farmer.getIcsName())) { //FarmCatalogue
				 * cat =
				 * catalogueService.findCatalogueByCode(farmer.getIcsName());
				 * //rows.add(!ObjectUtil.isEmpty(cat)?cat.getName():"");
				 * rows.add(farmer.getIcsName());// ICS Name } else{
				 * rows.add(""); } } }
				 */

				if (!getCurrentTenantId().equalsIgnoreCase("pratibha")) {
					rows.add(
							!ObjectUtil.isEmpty(datas[10]) ? qtyFormat.format(Double.valueOf(datas[10].toString())) : "");// qty
				}
				if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {
					double metricTon = 0.00;
					List<CropHarvestDetails> cropHarvestDetails = productService
							.listCropHarvestDetailsByHarvestId(Long.parseLong(datas[0].toString()));
					if (!ObjectUtil.isListEmpty(cropHarvestDetails)) {
						for (CropHarvestDetails crps : cropHarvestDetails) {

							if (crps.getCrop().getUnit().equalsIgnoreCase("kg")
									|| crps.getCrop().getUnit().equalsIgnoreCase("kgs")) {
								metricTon += (crps.getQty() / 1000);
							}
							if (crps.getCrop().getUnit().equalsIgnoreCase("quintal")
									|| crps.getCrop().getUnit().equalsIgnoreCase("quintals")) {
								metricTon += (crps.getQty() / 10);
							}

						}
					}
					rows.add(qtyFormat.format(metricTon));
				}

				rows.add(!ObjectUtil.isEmpty(datas[11]) ? formatter.format(Double.valueOf(datas[11].toString())) : "");// subTotal
				jsonObject.put("id", datas[0]);
				jsonObject.put("cell", rows);
			}
		}
		return jsonObject;
	}

	/*
	 * private Map<String, Object> getTotalYieldOfQuantity(CropHarvest
	 * cropHarvest) { double qty = 0; Map<String, Object> details = new
	 * HashMap<String, Object>(); if
	 * (!ObjectUtil.isListEmpty(cropHarvest.getCropHarvestDetails())) { for
	 * (CropHarvestDetails detail : cropHarvest.getCropHarvestDetails()) { qty =
	 * qty + detail.getQty(); } } details.put(TOTAL_YIELD_QUANTITY, qty); return
	 * details; }
	 */

	public String detail() {

		String view = "";
		if (id != null && !id.equals("")) {
			cropHarvest = productService.findCropHarvestById(Long.valueOf(id));
			if (cropHarvest == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			command = UPDATE;
			view = DETAIL;
			request.setAttribute(HEADING, getText(DETAIL));
		} else {

			request.setAttribute(HEADING, getText(LIST));
			return LIST;
		}
		return view;

	}

	public String subGridDetail() throws Exception {
		CropHarvestDetails cropHarvestDetails = new CropHarvestDetails();
		if (ObjectUtil.isEmpty(this.filter))
			cropHarvestDetails.setCropHarvest(new CropHarvest());
		cropHarvestDetails.getCropHarvest().setId(Long.valueOf(id));
		super.filter = cropHarvestDetails;
		Map data = readData();
		return sendJSONResponse(data);
	}

	/*
	 * public Map<String, String> getFarmersList() {
	 * 
	 * Map<String, String> farmerListMap = new LinkedHashMap<String, String>();
	 * List<Object[]> farmersList = farmerService.listFarmerInfo();
	 * farmerListMap = farmersList.stream().collect(Collectors.toMap(obj ->
	 * String.valueOf(obj[1]), obj -> (String.valueOf(obj[3]) + "-" +
	 * String.valueOf(obj[1])))); farmerListMap =
	 * farmerListMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
	 * .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,
	 * e2) -> e1, LinkedHashMap::new)); return farmerListMap;
	 * 
	 * }
	 */

	/*public Map<String, String> getFarmersList() {
		Map<String, String> farmerMap = new LinkedHashMap<>();
		List<Object[]> farmerList = farmerService.listFarmerInfoByCropHarvest();
		if (!ObjectUtil.isEmpty(farmerList)) {

			farmerMap = farmerList.stream()
					.collect(Collectors.toMap(obj -> (String.valueOf(obj[0])), obj -> (String.valueOf(obj[1]))));

		}
		return farmerMap;
	}*/

	/**
	 * Gets the farms list.
	 * 
	 * @return the farms list
	 */
	/*
	 * public Map<String, String> getFarmsList() {
	 * 
	 * Map<String, String> farmListMap = new LinkedHashMap<String, String>();
	 * List<Object[]> farmList = farmerService.listFarmInfo(); farmListMap =
	 * farmList.stream().filter(obj -> !StringUtil.isEmpty(obj[1]))
	 * .collect(Collectors.toMap(obj -> String.valueOf(obj[1]), obj ->
	 * (String.valueOf(obj[2]) + "-" + String.valueOf(obj[1])))); farmListMap =
	 * farmListMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
	 * .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,
	 * e2) -> e1, LinkedHashMap::new)); return farmListMap;
	 * 
	 * }
	 */

	/*public Map<String, String> getFarmsList() {
		Map<String, String> farmerMap = new LinkedHashMap<>();
		List<Object[]> farmerList = farmerService.listFarmInfoByCropHarvest();
		if (!ObjectUtil.isEmpty(farmerList)) {

			farmerMap = farmerList.stream().collect(Collectors.toMap(obj -> (String.valueOf(obj[0])),
					obj -> (String.valueOf(obj[0]) + " - " + String.valueOf(obj[1]))));

		}
		return farmerMap;
	}*/

	/*
	 * public Map<Long, String> getSamithi() {
	 * 
	 * samithi = locationService.listSamithiesBasedOnType(); Map<Long, String>
	 * samithiMap = new LinkedHashMap<>(); samithiMap =
	 * samithi.stream().collect(Collectors.toMap(Warehouse::getId,
	 * Warehouse::getName)); return samithiMap; }
	 */

	/*public Map<String, String> getWarehouseList() {
		AtomicInteger i = new AtomicInteger(0);
		Map<String, String> warehouseMap = new LinkedHashMap<>();
		FarmCatalogueMaster farmCatalougeMaster = catalogueService
				.findFarmCatalogueMasterByName(getText("cooperative"));
		if (!ObjectUtil.isEmpty(farmCatalougeMaster)) {
			Double d = new Double(farmCatalougeMaster.getId());
			List<FarmCatalogue> farmCatalougeList = catalogueService.findFarmCatalougeByAlpha(d.intValue());

			for (FarmCatalogue catalogue : farmCatalougeList) {
				if (!catalogue.getName().trim().contentEquals(ESESystem.OTHERS)) {
					warehouseMap.put(catalogue.getCode(), catalogue.getName());
				}
			}

		}
		warehouseMap.put("99", "Others");

		return warehouseMap;

	}*/

	/*
	 * public List<String[]> getFathersNameList() {
	 * 
	 * 
	 * Map<String, String> fatherNameListMap = new LinkedHashMap<String,
	 * String>(); List<String[]> farmersList =
	 * farmerService.listByFatherNameList(); for (String[] obj : farmersList) {
	 * if(!StringUtil.isEmpty(obj[0])) { fatherNameListMap.put(obj[0], obj[0]);
	 * } } return fatherNameListMap;
	 * 
	 * 
	 * List<String[]> fatherNameList = farmerService.listByFatherNameList();
	 * 
	 * return fatherNameList; }
	 */

	public Map<String, String> getFathersNameList() {
		Map<String, String> farmerMap = new LinkedHashMap<>();
		List<Object[]> farmerList = farmerService.listFarmerInfoByCropHarvest();
		List<String> testList = new ArrayList<>();
		if (!ObjectUtil.isEmpty(farmerList)) {
			testList = farmerList.stream().filter(obj -> !StringUtil.isEmpty(obj[2].toString()))
					.map(obj -> String.valueOf(obj[2])).distinct().collect(Collectors.toList());
			farmerMap = testList.stream().collect(Collectors.toMap(String::toString, String::toString));

		}
		return farmerMap;
	}

	public Map<Integer, String> getCropTypeList() {

		Map<Integer, String> cropListMap = new LinkedHashMap<Integer, String>();
		cropListMap.put(0, getText("ct0"));
		cropListMap.put(1, getText("ct1"));
		return cropListMap;

	}

	/*
	 * public Map<String, String> getCropList() {
	 * 
	 * Map<String, String> cropTypeNameList = new LinkedHashMap<String,
	 * String>(); List<CropHarvestDetails> nameList =
	 * productService.listOfCrops(); for (CropHarvestDetails cropHarvestDetails
	 * : nameList) {
	 * cropTypeNameList.put(String.valueOf(cropHarvestDetails.getCrop().getCode(
	 * )), cropHarvestDetails.getCrop().getCode() + "-" +
	 * cropHarvestDetails.getCrop().getName());
	 * 
	 * } return cropTypeNameList;
	 * 
	 * }
	 */

	/*public Map<String, String> getCropList() {
		Map<String, String> farmerMap = new LinkedHashMap<>();
		String branchId = getBranchId();
		List<Object[]> farmerList = productService.listOfHarvestedCrops(branchId);
		if (!ObjectUtil.isEmpty(farmerList)) {

			
			 * farmerMap=farmerList.stream().collect(Collectors.toMap(obj->(
			 * String.valueOf(obj[0])),
			 * obj->(String.valueOf(obj[0])+" - "+String.valueOf(obj[1]))));
			 
			farmerMap = farmerList.stream()
					.collect(Collectors.toMap(obj -> (String.valueOf(obj[0])), obj -> (String.valueOf(obj[0]))));

		}
		return farmerMap;
	}*/

	public String populateXLS() throws Exception {
		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getText("CropHarvestReport") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("cropHarvestReport"), fileMap, ".xls"));
		return "xls";
	}

	public InputStream getExportDataStream(String exportType) throws IOException {

		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		boolean flag = true;
		DateFormat filterDateFormat = new SimpleDateFormat(getGeneralDateFormat());
		DecimalFormat df = new DecimalFormat("0.00");
		DecimalFormat df1 = new DecimalFormat("0.00");

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("exportProcurementTitle"));
		HSSFPatriarch drawing = sheet.createDrawingPatriarch();

		HSSFCellStyle style1 = wb.createCellStyle();
		HSSFCellStyle style2 = wb.createCellStyle();
		HSSFCellStyle style3 = wb.createCellStyle();
		HSSFCellStyle style4 = wb.createCellStyle();
		HSSFCellStyle style5 = wb.createCellStyle();
		HSSFCellStyle style6 = wb.createCellStyle();

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
				filterRow6 = null, filterRow7 = null;
		HSSFCell cell;

		int imgRow1 = 0;
		int imgRow2 = 4;
		int imgCol1 = 0;
		int imgCol2 = 0;
		int titleRow1 = 2;
		int titleRow2 = 5;
        int count=0;
		int rowNum = 2;
		int colNum = 0;

		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap(); // build branch map to get branch name form branch id.

		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, titleRow1, titleRow2));

		//sheet.setDefaultColumnWidth(13);

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(2);
		style1.setAlignment(CellStyle.ALIGN_CENTER);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("exportProcurementTitle")));
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
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("StartingDate")));
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
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("EndingDate")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);

			if (!ObjectUtil.isEmpty(geteDate())) {
				cell = filterRow2.createCell(2);
				cell.setCellValue(new HSSFRichTextString(filterDateFormat.format(geteDate())));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			if (!StringUtil.isEmpty(seasonCode)) {
				filterRow1 = sheet.createRow(rowNum++);
				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("cSeasonCode")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				HarvestSeason season = farmerService.findHarvestSeasonByCode(seasonCode);
				cell = filterRow1.createCell(2);
				cell.setCellValue(new HSSFRichTextString(season.getName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			if (!StringUtil.isEmpty(icsName)) {
				filterRow1 = sheet.createRow(rowNum++);
				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("icsName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

			//	HarvestSeason season = farmerService.findHarvestSeasonByCode(icsName);
				cell = filterRow1.createCell(2);
				cell.setCellValue(new HSSFRichTextString(icsName));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			if (!StringUtil.isEmpty(crop)) {
				filterRow1 = sheet.createRow(rowNum++);
				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("Crop Name")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow1.createCell(2);
				// ProcurementProduct
				// farmer=farmerService.findCropByCropCode(crop);
				// cell.setCellValue(new HSSFRichTextString(farmer.getName()));
				cell.setCellValue(new HSSFRichTextString(crop));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			if (!StringUtil.isEmpty(farmerId)) {
				filterRow1 = sheet.createRow(rowNum++);
				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmerName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow1.createCell(2);
				Farmer farmer = farmerService.findFarmerByFarmerId(farmerId);
				cell.setCellValue(new HSSFRichTextString(farmer.getName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(farmCode)) {
				filterRow5 = sheet.createRow(rowNum++);
				cell = filterRow5.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farm")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow5.createCell(2);
				Farm farm = farmerService.findFarmByCode(farmCode);
				cell.setCellValue(new HSSFRichTextString(farm.getFarmName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			
			if (!StringUtil.isEmpty(selectedFieldStaff)) {
				filterRow5 = sheet.createRow(rowNum++);
				cell = filterRow5.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("agentId")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow5.createCell(2);
				 Agent agent = agentService.findAgentByAgentId(selectedFieldStaff);
				cell.setCellValue(new HSSFRichTextString(agent.getProfileIdWithName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			filterRow4 = sheet.createRow(rowNum++);
			filterRow5 = sheet.createRow(rowNum++);

			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
				if (StringUtil.isEmpty(branchIdValue)) {
					filterRow6 = sheet.createRow(rowNum++);
					cell = filterRow6.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("mainOrganization")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow6.createCell(2);
					// cell.setCellValue(new
					// HSSFRichTextString((branchesMap.get(filter.getBranchId()))));
					cell.setCellValue(new HSSFRichTextString(
							getBranchesMap().get(getParentBranchMap().get(filter.getBranchId()))));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					filterRow7 = sheet.createRow(rowNum++);
					cell = filterRow7.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("subOrganization")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow7.createCell(2);
					cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(getSubBranchIdParma())));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

				} else {
					if (!StringUtil.isEmpty(getBranchesMap().get(getSubBranchIdParma()))) {
						filterRow7 = sheet.createRow(rowNum++);
						cell = filterRow7.createCell(1);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("subOrganization")));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						cell = filterRow7.createCell(2);
						cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(getSubBranchIdParma())));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);
					}
				}

			} else {
				if (!StringUtil.isEmpty(branchIdParma)) {
					filterRow7 = sheet.createRow(rowNum++);
					cell = filterRow7.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getText("organization")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow7.createCell(2);
					BranchMaster branch = clientService.findBranchMasterByBranchId(branchIdParma);
					cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(branch) ? branch.getName() : "")));
					// cell.setCellValue(new HSSFRichTextString(branchIdParma));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);
				}
				 HSSFRow totalGridRowHead = sheet.createRow(rowNum++);
				 if(!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
				String[] headerFieldsArr=headerFields.split("###");
				  for(int i=0;i<headerFieldsArr.length;i++)
				  {
					  cell = totalGridRowHead.createCell(count);
						cell.setCellValue(new HSSFRichTextString(String.valueOf(headerFieldsArr[i])));
				//		style4.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
						//style4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
						style4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
						cell.setCellStyle(style4);
						font2.setBoldweight((short) 12);
						font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						style4.setFont(font2);
						sheet.setColumnWidth(count, (15 * 550));
						count++;
					  
				  }
				 }
			}
			filterRow6 = sheet.createRow(rowNum++);
			filterRow7 = sheet.createRow(rowNum++);
		}
		rowNum++;

		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		String mainGridColumnHeaders = "";
		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("ExportColumnCropHarvestHead");
			} else if (!StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("ExportColumnCropHarvestHead");
			}
		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("exportCropHarvestColumnHeaderBranch");
			} else {
				mainGridColumnHeaders = getLocaleProperty("exportCropHarvestColumnHeader");
			}
		}

		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("exportCropHarvestColumnHeaderBranch");
			}	else if (getBranchId().equalsIgnoreCase("organic")) {
				mainGridColumnHeaders = getLocaleProperty("exportCropHarvestColumnHeaderOrganic");
			} else if(getBranchId().equalsIgnoreCase("bci")){
				mainGridColumnHeaders = getLocaleProperty("exportCropHarvestColumnHeaderBCI");
			}
		}

		int mainGridIterator = 0;

		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {

			if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
			} else if (cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());
			}

			if (StringUtil.isEmpty(branchIdValue)) { // Add branch header to
														// export sheet if main
														// branch logged in.
				cell = mainGridRowHead.createCell(mainGridIterator);
				cell.setCellValue(new HSSFRichTextString(cellHeader));
			//	style2.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
				style2.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
				style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cell.setCellStyle(style2);
				font2.setBoldweight((short) 12);
				font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

				style2.setFont(font2);
				sheet.setColumnWidth(mainGridIterator, (15 * 550));
				mainGridIterator++;
			} else {
				if (!cellHeader.equalsIgnoreCase("Organization")) {
					cell = mainGridRowHead.createCell(mainGridIterator);
					cell.setCellValue(new HSSFRichTextString(cellHeader));
					style2.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
					//style2.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
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
		if (ObjectUtil.isEmpty(this.filter))
			this.filter = new CropHarvest();

		if (!StringUtil.isEmpty(seasonCode)) {
			filter.setSeasonCode(seasonCode);

		}

		if (!StringUtil.isEmpty(farmerId)) {
			filter.setFarmerId(farmerId);
		}
		if (!StringUtil.isEmpty(farmCode)) {
			filter.setFarmCode(farmCode);
		}
		if (!StringUtil.isEmpty(crop)) {
			filter.setCropName(crop);
		}
		if (!StringUtil.isEmpty(selectedFieldStaff)) {
			String agentIds[] = selectedFieldStaff.split("-");
			String agentId = agentIds[0].trim();
			filter.setAgentId(agentId);
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
		if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
			if (!StringUtil.isEmpty(stateName)) {
				// State s = locationService.findStateByCode(stateName);
				filter.setStateName(stateName);
			}

			if (!StringUtil.isEmpty(fpo)) {
				filter.setFpo(fpo);
			}

			
		}
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
		if (!StringUtil.isEmpty(icsName)) {

			filter.setIcsname(icsName);
		}
		}
		super.filter = this.filter;

		Map data = readData("cropHarvest");
		Long serialNumber = 0L;
		List<Object[]> dfata = (ArrayList) data.get(ROWS);
		DecimalFormat formatter = new DecimalFormat("0.00");
		DecimalFormat qtyFormat = new DecimalFormat("0.00");
		if (!ObjectUtil.isEmpty(dfata)) {
			for (Object[] datas : dfata) {
				row = sheet.createRow(rowNum++);
				colNum = 0;
				Farmer farmer = farmerService.findFarmerByFarmerId(datas[2].toString());

				serialNumber++;
				cell = row.createCell(colNum++);
				style6.setAlignment(CellStyle.ALIGN_CENTER);
				cell.setCellStyle(style6);
				cell.setCellValue(new HSSFRichTextString(
						String.valueOf(serialNumber) != null ? String.valueOf(serialNumber) : ""));

				if ((getIsMultiBranch().equalsIgnoreCase("1")
						&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
					if (StringUtil.isEmpty(branchIdValue)) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(
								!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(datas[6])))
										? getBranchesMap().get(getParentBranchMap().get(datas[6]))
										: getBranchesMap().get(datas[6])));
					}
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(datas[6])));

				} else {
					if (StringUtil.isEmpty(branchIdValue)) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(branchesMap.get(datas[6])));
					}
				}

				if (!ObjectUtil.isEmpty(datas[1])) {
					String date = "";
					if (!ObjectUtil.isEmpty(datas[1])) {
						ESESystem preferences = preferncesService.findPrefernceById("1");
						if (!StringUtil.isEmpty(preferences)) {
							DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(genDate.format(datas[1])));
						}
					}
					if (!StringUtil.isEmpty(datas[7])) {
						HarvestSeason season = farmerService.findSeasonNameByCode(datas[7].toString());
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(season == null ? "" : season.getName()));
					} else {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString("NA"));
					}
					if (!StringUtil.isEmpty(datas[9])) {
						Agent agent = agentService.findAgentByAgentId(datas[9].toString());
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(agent == null ? "" : agent.getProfileIdWithName()));
					} else {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString("NA"));
					}
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(datas[3].toString()));// farmer
																					// name

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(datas[5].toString()));
					if (datas[2].toString() != "") {
						if (!ObjectUtil.isEmpty(farmer)) {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(farmer.getVillage().getName()));
						} else {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(""));
						}

					}

					if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
						if (!StringUtil.isEmpty(farmer.getIcsName())) {
							FarmCatalogue cat = catalogueService.findCatalogueByCode(farmer.getIcsName());
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(
									!ObjectUtil.isEmpty(cat) && !StringUtil.isEmpty(cat.getName()) ? cat.getName()
											: ""));// ICS Name
						} else {

							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(""));
						}

					} else {
						if (!getCurrentTenantId().equalsIgnoreCase("agro")
								&& (!getCurrentTenantId().equalsIgnoreCase("crsdemo"))) {
							if (getCurrentTenantId().equalsIgnoreCase("pratibha"))
								if (StringUtil.isEmpty(getBranchId()) || getBranchId().equalsIgnoreCase("organic")) {
								cell = row.createCell(colNum++);
								cell.setCellValue(new HSSFRichTextString(
										!StringUtil.isEmpty(farmer.getIcsName()) ? farmer.getIcsName() : ""));// ICS
																												// Name
							}
						}
					}

					/*
					 * if(getCurrentTenantId().equalsIgnoreCase("pratibha")){
					 * if( StringUtil.isEmpty(getBranchId()) ||
					 * getBranchId().equalsIgnoreCase("organic")){ if
					 * (!StringUtil.isEmpty(farmer.getIcsName())) {
					 * FarmCatalogue cat =
					 * catalogueService.findCatalogueByCode(farmer.getIcsName())
					 * ; cell = row.createCell(colNum++); cell.setCellValue(new
					 * HSSFRichTextString(cat.getName()));// ICS Name } else{
					 * cell = row.createCell(colNum++); cell.setCellValue(new
					 * HSSFRichTextString("")); } } }
					 */
					if (!getCurrentTenantId().equalsIgnoreCase("pratibha")) {
						cell = row.createCell(colNum++);
						style5.setAlignment(CellStyle.ALIGN_RIGHT);
						cell.setCellStyle(style5);
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellValue(Double.valueOf(datas[10].toString()));// qty
					}
					if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {
						double metricTon = 0.00;
						List<CropHarvestDetails> cropHarvestDetails = productService
								.listCropHarvestDetailsByHarvestId(Long.parseLong(datas[0].toString()));
						if (!ObjectUtil.isListEmpty(cropHarvestDetails)) {
							for (CropHarvestDetails crps : cropHarvestDetails) {

								if (crps.getCrop().getUnit().equalsIgnoreCase("kg")
										|| crps.getCrop().getUnit().equalsIgnoreCase("kgs")) {
									metricTon += (crps.getQty() / 1000);
								}
								if (crps.getCrop().getUnit().equalsIgnoreCase("quintal")
										|| crps.getCrop().getUnit().equalsIgnoreCase("quintals")) {
									metricTon += (crps.getQty() / 10);
								}

							}
						}
						cell = row.createCell(colNum++);
						//cell.setCellValue(new HSSFRichTextString(qtyFormat.format(metricTon)));
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						//cell.setCellValue(new HSSFRichTextString(
						//		CurrencyUtil.getDecimalFormat(metricTon, "##.00")));
							cell.setCellValue(Double.valueOf(metricTon));	
						
						
					}
					// if(getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
					// if(!getBranchId().equalsIgnoreCase("bci")) {
					/*
					 * cell = row.createCell(colNum++); cell.setCellValue(new
					 * HSSFRichTextString(!ObjectUtil.isEmpty(datas[10]) ?
					 * formatter.format(Double.valueOf(datas[10].toString())) :
					 * ""));// subTotal
					 */ // }
					// }
				}

				HSSFRow subGridRowHead = sheet.createRow(rowNum++);

				String subGridColumnHeaders = getLocaleProperty("exportHarvestSubgridHeadings");
				// String subGridColumnHeaders = " ";
				/*
				 * if (!getCurrentTenantId().equalsIgnoreCase("pratibha")){
				 * subGridColumnHeaders =
				 * getLocaleProperty("exportHarvestSubgridHeadings"); }else{
				 * subGridColumnHeaders =
				 * getLocaleProperty("exportHarvestSubgrid"); }
				 */
				int subGridIterator = 1;

				for (String cellHeader : subGridColumnHeaders.split("\\,")) {

					if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
						cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
					} else if (cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT)) {
						cellHeader = cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());
					}

					cell = subGridRowHead.createCell(subGridIterator);
					cell.setCellValue(new HSSFRichTextString(cellHeader));
					// style2.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
					// style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					// style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
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

				List<CropHarvestDetails> cropHarvestDetails = productService
						.listCropHarvestDetailsByHarvestId(Long.parseLong(datas[0].toString()));
				for (CropHarvestDetails chd : cropHarvestDetails) {
					row = sheet.createRow(rowNum++);
					colNum = 1;

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(getText("ct" + chd.getCropType())));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(chd.getCrop().getName()));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(chd.getVariety().getName()));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(chd.getGrade().getName()));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(chd.getCrop().getUnit()));
					/*
					 * cell = row.createCell(colNum++); cell.setCellValue(new
					 * HSSFRichTextString(formatter.format(chd.getPrice())));
					 */
					if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {
						double metricTon = 0.00;
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(CurrencyUtil.getDecimalFormat(chd.getQty(), "##.000")
								+ "(" + chd.getCrop().getUnit() + ")"));

						if (chd.getCrop().getUnit().equalsIgnoreCase("kg")
								|| chd.getCrop().getUnit().equalsIgnoreCase("kgs")) {
							metricTon = chd.getQty() / 1000;
						}
						if (chd.getCrop().getUnit().equalsIgnoreCase("quintal")
								|| chd.getCrop().getUnit().equalsIgnoreCase("quintals")) {
							metricTon = chd.getQty() / 10;
						}

						cell = row.createCell(colNum++);
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellValue(Double.valueOf((qtyFormat.format(metricTon))));
					} else {
						cell = row.createCell(colNum++);
						style5.setAlignment(CellStyle.ALIGN_RIGHT);
						cell.setCellStyle(style5);
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellValue(Double.valueOf(qtyFormat.format(chd.getQty())));
					}
					/*
					 * cell = row.createCell(colNum++); cell.setCellValue(new
					 * HSSFRichTextString(formatter.format(chd.getSubTotal())));
					 */
				}
				row = sheet.createRow(rowNum++);
			}
		}

		/*for (int i = 0; i <= colNum; i++) {
			sheet.autoSizeColumn(i);
		}*/

		int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		// picture.resize();
		String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
				: request.getSession().getId();
		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("exportProcurementTitle") + fileNameDateFormat.format(new Date()) + ".xls";
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

	public CropHarvest getFilter() {

		return filter;
	}

	public void setFilter(CropHarvest filter) {

		this.filter = filter;
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

	public CropHarvestDetails getCropHarvestDetails() {

		return cropHarvestDetails;
	}

	public void setCropHarvestDetails(CropHarvestDetails cropHarvestDetails) {

		this.cropHarvestDetails = cropHarvestDetails;
	}

	public String getBranchIdParma() {

		return branchIdParma;
	}

	public void setBranchIdParma(String branchIdParma) {

		this.branchIdParma = branchIdParma;
	}

	@Override
	public String populatePDF() throws Exception {
		InputStream is = getPDFExportDataStream();
		setPdfFileName(getText("ListFile") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(getPdfFileName(), is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("ListFile"), fileMap, ".pdf"));
		return "pdf";

	}

	public InputStream getPDFExportDataStream()
			throws IOException, DocumentException, NoSuchFieldException, SecurityException {
		Font cellFont = null; // font for cells.
		Font titleFont = null; // font for title text.
		Paragraph title = null; // to add title for report
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
		String fileName = getText("ListFile") + fileNameDateFormat.format(new Date()) + ".pdf";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		PdfWriter.getInstance(document, fileOut);

		document.open();

		PdfPTable table = null;
		PdfPCell cell = null;
		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap();

		com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance(exportLogo());
		logo.scaleAbsolute(100, 50);
		document.add(logo); // Adding logo in PDF file.
		Long serialNo = 0L;
		// below of code for title text
		titleFont = new Font(FontFamily.HELVETICA, 14, Font.BOLD, GrayColor.GRAYBLACK);
		title = new Paragraph(new Phrase(getText("CropHarvestExportPDFTitle"), titleFont));
		title.setAlignment(Element.ALIGN_CENTER);
		document.add(title);
		document.add(
				new Paragraph(new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
		DateFormat filterDateFormat = new SimpleDateFormat(getGeneralDateFormat());
		if (isMailExport()) {
			document.add(new Paragraph(new Phrase(getLocaleProperty("filter"),
					new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));

			document.add(
					new Paragraph(new Phrase(getLocaleProperty("StartingDate") + " : " + filterDateFormat.format(getsDate()),
							new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
			document.add(
					new Paragraph(new Phrase(getLocaleProperty("EndingDate") + " : " + filterDateFormat.format(geteDate()),
							new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));

			document.add(new Paragraph(
					new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
		}
		if(!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
		if (!StringUtil.isEmpty(headerFields)) {
			String[] headerFieldsArr=headerFields.split("###");
			 
				  document.add(new Paragraph(
							new Phrase(headerFieldsArr[0] + " : " + headerFieldsArr[1],
									new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));
			}
		}
		boolean flag = true;
		DecimalFormat df = new DecimalFormat("0.00");
		String mainGridColumnHeaders = ""; // line
		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("ExportColumnCropHarvestHead");
			} else if (!StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("ExportColumnCropHarvestHead");
			}
		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("exportCropHarvestColumnHeaderBranch");
			}
			/*
			 * else if(getBranchId().equalsIgnoreCase("crsdemo")){
			 * mainGridColumnHeaders =
			 * getLocaleProperty("exportColumnHeadercrscropharvest"); }
			 */
			else {
				mainGridColumnHeaders = getLocaleProperty("exportCropHarvestColumnHeader");
			}
		}
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("exportCropHarvestColumnHeaderBranch");
			}	else if (getBranchId().equalsIgnoreCase("organic")) {
				mainGridColumnHeaders = getLocaleProperty("exportCropHarvestColumnHeaderOrganic");
			} else if(getBranchId().equalsIgnoreCase("bci")){
				mainGridColumnHeaders = getLocaleProperty("exportCropHarvestColumnHeaderBCI");
			}}
		int mainGridIterator = 0;
		table = new PdfPTable(mainGridColumnHeaders.split("\\,").length);
		cellFont = new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK);
		mainGridColWidth = mainGridColumnHeaders.split("\\,").length;
		if (ObjectUtil.isEmpty(this.filter))
			this.filter = new CropHarvest();

		if (!StringUtil.isEmpty(seasonCode)) {
			filter.setSeasonCode(seasonCode);
			HarvestSeason season = farmerService.findHarvestSeasonByCode(seasonCode);
			document.add(new Paragraph(new Phrase(getLocaleProperty("seasonCode") + ": " + season.getName(),
					new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
			document.add(new Paragraph(
					new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
		}
		if (!StringUtil.isEmpty(crop)) {
			filter.setCropName(crop);
			// ProcurementProduct
			// cropname=farmerService.findCropByCropCode(crop);
			// document.add(new Paragraph(new Phrase(getLocaleProperty("crop")+":
			// "+cropname.getName(),new Font(FontFamily.HELVETICA, 10,
			// Font.BOLD, GrayColor.GRAYBLACK))));
			document.add(new Paragraph(new Phrase(getLocaleProperty("crop") + ": " + crop,
					new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
			document.add(new Paragraph(
					new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
		}
		if (!StringUtil.isEmpty(farmerId)) {
			filter.setFarmerId(farmerId);
			Farmer frm = farmerService.findFarmerByFarmerId(farmerId);
			document.add(new Paragraph(new Phrase(getLocaleProperty("farmerName") + ": " + frm.getName(),
					new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
			document.add(new Paragraph(
					new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
		}
		if (!StringUtil.isEmpty(selectedFieldStaff)) {
			filter.setAgentId(selectedFieldStaff);
			Agent agent = agentService.findAgentByAgentId(selectedFieldStaff);
			document.add(new Paragraph(new Phrase(getLocaleProperty("agentName") + ": " + agent.getProfileIdWithName(),
					new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
			document.add(new Paragraph(
					new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
		}
		if (!StringUtil.isEmpty(farmCode)) {
			filter.setFarmCode(farmCode);
			Farm farm = farmerService.findFarmByCode(farmCode);
			document.add(new Paragraph(new Phrase(getLocaleProperty("farm") + ": " + farm.getFarmName(),
					new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
			document.add(new Paragraph(
					new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
		}
		/*
		 * if (!StringUtil.isEmpty(farmerId)) { filter.setFarmerId(farmerId);
		 * Farmer frm=farmerService.findFarmerByFarmerId(farmerId);
		 * document.add(new Paragraph(new
		 * Phrase(getLocaleProperty("farmerName")+": "+frm.getName(),new
		 * Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
		 * document.add( new Paragraph(new Phrase(" ", new
		 * Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK)))); }
		 */

		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (!StringUtil.isEmpty(branchIdParma)) {

				document.add(new Paragraph(
						new Phrase(getLocaleProperty("Organization") + " : " + getBranchesMap().get(getBranchIdParma()),
								new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));

				document.add(new Paragraph(new Phrase(
						getLocaleProperty("Sub Organization") + " : " + getBranchesMap().get(getSubBranchIdParma()),
						new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));
			} else {
				if (!StringUtil.isEmpty(getBranchesMap().get(getSubBranchIdParma()))) {
					document.add(new Paragraph(new Phrase(
							getLocaleProperty("Sub Organization") + " : " + getBranchesMap().get(getSubBranchIdParma()),
							new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));

				}

			}
		}
		if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
			if (!StringUtil.isEmpty(stateName)) {
				// State s = locationService.findStateByCode(stateName);
				filter.setStateName(stateName);
				State s = locationService.findStateByCode(stateName);
				document.add(new Paragraph(new Phrase(getLocaleProperty("stateName") + ": " + s.getName(),
						new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
				document.add(new Paragraph(
						new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
			}

			if (!StringUtil.isEmpty(fpo)) {
				filter.setFpo(fpo);
				FarmCatalogue fc = farmerService.findfarmcatalogueByCode(fpo);
				document.add(new Paragraph(new Phrase(getLocaleProperty("fpo") + ": " + fc.getName(),
						new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
				document.add(new Paragraph(
						new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
			}

			
		}
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
		if (!StringUtil.isEmpty(icsName)) {

			filter.setIcsname(icsName);
			document.add(new Paragraph(new Phrase(getLocaleProperty("icsName") + ": " +icsName,
					new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
			document.add(new Paragraph(
					new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));

		}
	}
		super.filter = this.filter;
		Map data = readData("cropHarvest");
		DecimalFormat formatter = new DecimalFormat("0.00");
		DecimalFormat qtyFormat = new DecimalFormat("0.00");

		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {

			if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
			} else if (cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());
			}

			cell = new PdfPCell(new Phrase(cellHeader, cellFont));
			cell.setBackgroundColor(new BaseColor(144,238,144));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setNoWrap(false); // To set wrapping of text in cell.
			// cell.setColspan(3); // To add column span.
			table.addCell(cell);

		}

		cellFont = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK);
		List<Object[]> mainGridRows = (ArrayList) data.get(ROWS);
		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;
		else {
			for (Object[] datas : mainGridRows) {

				serialNo++;

				cell = new PdfPCell(new Paragraph(new Phrase(String.valueOf(serialNo), cellFont)));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				Farmer farmer = farmerService.findFarmerByFarmerId(datas[2].toString());
				if ((getIsMultiBranch().equalsIgnoreCase("1")
						&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
					if (StringUtil.isEmpty(branchIdValue)) {
						cell = new PdfPCell(
								new Phrase(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(datas[6])))
										? getBranchesMap().get(getParentBranchMap().get(datas[6]))
										: getBranchesMap().get(datas[6]), cellFont));
						table.addCell(cell);
					}
					cell = new PdfPCell(new Phrase(getBranchesMap().get(datas[6]), cellFont));
					table.addCell(cell);

				} else {
					if (StringUtil.isEmpty(branchIdValue)) {
						cell = new PdfPCell(new Paragraph(new Phrase(branchesMap.get(datas[6]), cellFont)));
						table.addCell(cell);
					}
				}

				if (!ObjectUtil.isEmpty(datas[1])) {
					String date = "";
					if (!ObjectUtil.isEmpty(datas[1])) {
						ESESystem preferences = preferncesService.findPrefernceById("1");
						if (!StringUtil.isEmpty(preferences)) {
							DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
							cell = new PdfPCell(new Phrase(genDate.format(datas[1]), cellFont));
							table.addCell(cell);
						}
					}

					if (!StringUtil.isEmpty(datas[7])) {
						HarvestSeason season = farmerService.findSeasonNameByCode(datas[7].toString());
						cell = new PdfPCell(new Phrase(season == null ? "" : season.getName(), cellFont));
						table.addCell(cell);
					} else {
						cell = new PdfPCell(new Phrase("NA", cellFont));
						table.addCell(cell);
					}
					
					if (!StringUtil.isEmpty(datas[9])) {
						Agent agent = agentService.findAgentByAgentId(datas[9].toString());
						cell = new PdfPCell(new Phrase(agent == null ? "" : agent.getProfileIdWithName(), cellFont));
						table.addCell(cell);
					} else {
						cell = new PdfPCell(new Phrase("NA", cellFont));
						table.addCell(cell);
					}
					
					cell = new PdfPCell(new Phrase(datas[3].toString(), cellFont));// farmer
																					// name
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(datas[5].toString(), cellFont));
					table.addCell(cell);
					if (datas[2].toString() != "") {
						if (!ObjectUtil.isEmpty(farmer)) {
							cell = new PdfPCell(new Phrase(farmer.getVillage().getName(), cellFont));
							table.addCell(cell);
						} else {
							cell = new PdfPCell(new Phrase((""), cellFont));
							table.addCell(cell);
						}

					}

					if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
						if (!StringUtil.isEmpty(farmer.getIcsName())) {
							FarmCatalogue cat = catalogueService.findCatalogueByCode(farmer.getIcsName());
							cell = new PdfPCell(new Phrase(
									!ObjectUtil.isEmpty(cat) && !StringUtil.isEmpty(cat.getName()) ? cat.getName() : "",
									cellFont));// ICS Name
							table.addCell(cell);
						} else {

							cell = new PdfPCell(new Phrase((""), cellFont));
							table.addCell(cell);
						}

					} else {
						if (!getCurrentTenantId().equalsIgnoreCase("agro")
								&& (!getCurrentTenantId().equalsIgnoreCase("crsdemo"))) {
							if (getCurrentTenantId().equalsIgnoreCase("pratibha"))
									if (StringUtil.isEmpty(getBranchId()) || getBranchId().equalsIgnoreCase("organic")) {
								cell = new PdfPCell(new Phrase(
										(!StringUtil.isEmpty(farmer.getIcsName()) ? farmer.getIcsName() : ""),
										cellFont));// ICS Name
								table.addCell(cell);
							}
						}
					}
					/*
					 * if(getCurrentTenantId().equalsIgnoreCase("pratibha")){
					 * if(StringUtil.isEmpty(getBranchId()) ||
					 * getBranchId().equalsIgnoreCase(ESESystem.
					 * PRATIBHA_ORGANIC_BRANCH_ID)){ Farm
					 * farm=farmerService.findFarmByCode(datas[4].toString());
					 * if(!ObjectUtil.isEmpty(farm) &&
					 * !ObjectUtil.isEmpty(farm.getFarmDetailedInfo()) &&
					 * !StringUtil.isEmpty(farm.getFarmDetailedInfo().
					 * getLandUnderICSStatus())){ cell=new PdfPCell(new
					 * Phrase(farm.getFarmDetailedInfo().getLandUnderICSStatus()
					 * )); table.addCell(cell); } else{ cell = new PdfPCell(new
					 * Phrase((""),cellFont)); table.addCell(cell); } } }
					 */
					if (!getCurrentTenantId().equalsIgnoreCase("pratibha")) {
						cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(datas[10])
								? qtyFormat.format(Double.valueOf(datas[10].toString())) : "", cellFont));// qty
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						table.addCell(cell);
					}
					if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {
						double metricTon = 0.00;
						List<CropHarvestDetails> cropHarvestDetails = productService
								.listCropHarvestDetailsByHarvestId(Long.parseLong(datas[0].toString()));
						if (!ObjectUtil.isListEmpty(cropHarvestDetails)) {
							for (CropHarvestDetails crps : cropHarvestDetails) {

								if (crps.getCrop().getUnit().equalsIgnoreCase("kg")
										|| crps.getCrop().getUnit().equalsIgnoreCase("kgs")) {
									metricTon += (crps.getQty() / 1000);
								}
								if (crps.getCrop().getUnit().equalsIgnoreCase("quintal")
										|| crps.getCrop().getUnit().equalsIgnoreCase("quintals")) {
									metricTon += (crps.getQty() / 10);
								}

							}
						}
						cell = new PdfPCell(new Phrase(qtyFormat.format(metricTon), cellFont));
						table.addCell(cell);
					}

					// if(getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
					// if(!getBranchId().equalsIgnoreCase("bci")) {
					/*
					 * cell=new PdfPCell(new
					 * Phrase(!ObjectUtil.isEmpty(datas[10]) ?
					 * formatter.format(Double.valueOf(datas[10].toString())) :
					 * "",cellFont));// subTotal table.addCell(cell);
					 */
					/*
					 * cell = new PdfPCell(new Phrase((""),cellFont));
					 * table.addCell(cell);
					 */
					// }
					// }

					if ((getCurrentTenantId().equalsIgnoreCase("crsdemo") && !StringUtil.isEmpty(branchIdValue))) {
						cell = new PdfPCell();
						table.addCell(cell);
					} else if (getCurrentTenantId().equalsIgnoreCase("agro") && (!StringUtil.isEmpty(branchIdValue))) {
						cell = new PdfPCell();
						table.addCell(cell);
					} else if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {
						cell = new PdfPCell(new Phrase("", cellFont));
						table.addCell(cell);
					}
					
					if (getCurrentTenantId().equalsIgnoreCase("sticky")) {
						if (serialNo >= 1) {
							cell = new PdfPCell();
							table.addCell(cell);
						}
					}
					
					
					String subGridHeaders = getLocaleProperty("exportHarvestSubgridHeadings");
					/*
					 * String subGridHeaders = "" ; if
					 * (!getCurrentTenantId().equalsIgnoreCase("pratibha")) {
					 * subGridHeaders =
					 * getLocaleProperty("exportHarvestSubgridHeadings"); }else{
					 * subGridHeaders =
					 * getLocaleProperty("exportHarvestSubgrid"); }
					 */
					int subGridIterator = 1;

					cellFont = new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK);
					subGridColWidth = subGridHeaders.split("\\,").length;

					if (StringUtil.isEmpty(getBranchId())) {
						if (serialNo >= 1) {
							cell = new PdfPCell();
							table.addCell(cell);

						}
					}

					/*if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
						if (getBranchId().equalsIgnoreCase("bci")) {
							if (serialNo >= 1) {
								cell = new PdfPCell();
								table.addCell(cell);

							}

						}
					}*/

					for (String cellHeader : subGridHeaders.split("\\,")) {

						if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
							cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
						} else if (cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT)) {
							cellHeader = cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());
						}

						cell = new PdfPCell(new Phrase(cellHeader, cellFont));
						cell.setBackgroundColor(GrayColor.LIGHT_GRAY);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell.setNoWrap(false); // To set wrapping of text in
												// cell.
						// cell.setColspan(3); // To add column span.
						table.addCell(cell);

					}
					if (serialNo >= 1) {
						for (int i = subGridColWidth + 1; i < table.getNumberOfColumns(); i++) {
							cell = new PdfPCell(new Phrase("", cellFont));
							table.addCell(cell);
						}
					} else {
						for (int i = subGridColWidth; i < table.getNumberOfColumns(); i++) {
							cell = new PdfPCell(new Phrase("", cellFont));
							table.addCell(cell);
						}
					}

					cellFont = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK);
					List<CropHarvestDetails> cropHarvestDetails = productService
							.listCropHarvestDetailsByHarvestId(Long.parseLong(datas[0].toString()));
					for (CropHarvestDetails chd : cropHarvestDetails) {

						if (serialNo >= 1) {
							cell = new PdfPCell();
							table.addCell(cell);

						}

						// if (!getCurrentTenantId().equalsIgnoreCase("indev")
						// &&
						// !getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID))
						// {
						cell = new PdfPCell(new Phrase(getText("ct" + chd.getCropType()), cellFont));
						table.addCell(cell);
						// }
						cell = new PdfPCell(new Phrase(chd.getCrop().getName(), cellFont));
						table.addCell(cell);
						cell = new PdfPCell(new Phrase(chd.getVariety().getName(), cellFont));
						table.addCell(cell);
						cell = new PdfPCell(new Phrase(chd.getGrade().getName(), cellFont));
						table.addCell(cell);
						cell = new PdfPCell(new Phrase(chd.getCrop().getUnit(), cellFont));
						table.addCell(cell);
						
						/*cell = new PdfPCell(new Phrase((""),cellFont));
						table.addCell(cell);*/

						if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {
							double metricTon = 0.00;
							cell = new PdfPCell(new Phrase(CurrencyUtil.getDecimalFormat(chd.getQty(), "##.000") + "("
									+ chd.getCrop().getUnit() + ")", cellFont));
							table.addCell(cell);
							if (!StringUtil.isEmpty(chd.getCrop().getUnit()) && chd.getCrop().getUnit() != null) {
								if (chd.getCrop().getUnit().equalsIgnoreCase("kg")
										|| chd.getCrop().getUnit().equalsIgnoreCase("kgs")) {
									metricTon = chd.getQty() / 1000;
								}
								if (chd.getCrop().getUnit().equalsIgnoreCase("quintal")
										|| chd.getCrop().getUnit().equalsIgnoreCase("quintals")) {
									metricTon = chd.getQty() / 10;
								}
							}
							cell = new PdfPCell(new Phrase(qtyFormat.format(metricTon), cellFont));
							table.addCell(cell);
						} else {
							cell = new PdfPCell(new Phrase(qtyFormat.format(chd.getQty()), cellFont));
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							table.addCell(cell);
						}
						/*cell = new PdfPCell(new Phrase((""),cellFont));
						table.addCell(cell);*/
						/*
						 * cell = new PdfPCell(new
						 * Phrase(formatter.format(chd.getSubTotal()),cellFont))
						 * ; table.addCell(cell);
						 */
						/*
						 * for (int i = subGridColWidth; i <
						 * table.getNumberOfColumns(); i++) { cell = new
						 * PdfPCell(new Phrase("", cellFont));
						 * table.addCell(cell); }
						 */

						if (serialNo >= 1) {
							for (int i = subGridColWidth + 1; i < table.getNumberOfColumns(); i++) {
								cell = new PdfPCell(new Phrase("", cellFont));
								table.addCell(cell);
							}
						} else {
							for (int i = subGridColWidth; i < table.getNumberOfColumns(); i++) {
								cell = new PdfPCell(new Phrase("", cellFont));
								table.addCell(cell);
							}
						}

					}

				}
			}
		}
		document.add(table); // Add table to document.

		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		document.close();
		fileOut.close();
		return stream;
	}

	/*public Map<String, String> getSeasonList() {

		Map<String, String> seasonMap = new LinkedHashMap<String, String>();
		List<Object[]> seasonList = farmerService.listSeasonCodeAndName();
		for (Object[] obj : seasonList) {
			
			 * seasonMap.put(String.valueOf(obj[0]), obj[1] + " - " + obj[0]);
			 
			seasonMap.put(String.valueOf(obj[0]), String.valueOf(obj[1]));
		}
		return seasonMap;

	}*/

	public String getSeasonCode() {

		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {

		this.seasonCode = seasonCode;
	}

	public String getExportLimit() {

		return exportLimit;
	}

	public void setExportLimit(String exportLimit) {

		this.exportLimit = exportLimit;
	}

	public Map<String, String> getFields() {
		fields.put("1", getText("harvestDate"));
		fields.put("2", getText("season"));
		fields.put("3", getText("farmerId"));
		// fields.put("4", getLocaleProperty("fatherName"));
		fields.put("5", getText("farmId"));
		/* fields.put("6", getText("cropType")); */
		fields.put("6", getText("cropName"));

		/*
		 * if ((getIsMultiBranch().equalsIgnoreCase("1") &&
		 * (getIsParentBranch().equals("1") ||
		 * StringUtil.isEmpty(branchIdValue)))) { fields.put("8",
		 * getText("app.branch")); } else if (StringUtil.isEmpty(getBranchId()))
		 * { fields.put("7", getText("app.branch")); }
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

		if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {
			if (StringUtil.isEmpty(getBranchId()) || !getBranchId().equals("bci")) {
				fields.put("9", getText("icsName"));
			}
		}
		fields.put("10", getLocaleProperty("warehouseName"));

		if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
			fields.put("11", getLocaleProperty("state.name"));

			fields.put("12", getLocaleProperty("CropHarvestcooperative"));

			fields.put("13", getLocaleProperty("icsName"));
		}
		return fields;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}

	public ICatalogueService getCatalogueService() {

		return catalogueService;
	}

	public void setCatalogueService(ICatalogueService catalogueService) {

		this.catalogueService = catalogueService;
	}

	/*public Map<String, String> getIcsList() {

		Map<String, String> icsList = new LinkedHashMap<String, String>();

		List<Object[]> ics = catalogueService.loadICSName();
		for (Object[] obj : ics) {
			icsList.put(String.valueOf(obj[0]), String.valueOf(obj[1]));
		}
		return icsList;

	}*/

	public String getIcsName() {

		return icsName;
	}

	public void setIcsName(String icsName) {

		this.icsName = icsName;
	}

	public String getWarehouseId() {

		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {

		this.warehouseId = warehouseId;
	}

	public String getFpo() {
		return fpo;
	}

	public void setFpo(String fpo) {
		this.fpo = fpo;
	}

	public String getCrop() {
		return crop;
	}

	public void setCrop(String crop) {
		this.crop = crop;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
/*
	public Map<String, String> getStateList() {

		Map<String, String> stateMap = new LinkedHashMap<String, String>();

		List<State> stateList = locationService.listOfStates();
		for (State obj : stateList) {
			stateMap.put(obj.getCode(), obj.getCode() + " - " + obj.getName());

		}
		return stateMap;

	}*/

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

	public void populateFarmerList() {
		JSONArray farmerArr = new JSONArray();
		List<Object[]> farmer = farmerService.listFarmerInfoByCropHarvest();
		if (!ObjectUtil.isEmpty(farmer)) {
			farmer.forEach(obj -> {
				farmerArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(farmerArr);
	}

	public void populateFarmCodeList() {
		JSONArray farmArr = new JSONArray();
		List<Object[]> farm = farmerService.listFarmInfoByCropHarvest();
		if (!ObjectUtil.isEmpty(farm)) {
			farm.forEach(obj -> {
				farmArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(farmArr);
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

	public void populateCropList() {
		JSONArray cropArr = new JSONArray();
		String branchId = getBranchId();
		List<Object[]> cropList = productService.listOfHarvestedCrops(branchId);
		if (!ObjectUtil.isEmpty(cropList)) {
			cropList.forEach(obj -> {
				cropArr.add(getJSONObject(obj[0].toString(), obj[0].toString()));
			});
		}
		sendAjaxResponse(cropArr);
	}

	public void populateStateList() {
		JSONArray stateArr = new JSONArray();
		List<State> stateList = locationService.listOfStates();
		if (!ObjectUtil.isEmpty(stateList)) {
			stateList.forEach(obj -> {
				stateArr.add(getJSONObject(obj.getCode(), obj.getCode() + "-" + obj.getName()));
			});
		}
		sendAjaxResponse(stateArr);
	}

	public void populateWarehouseList() {
		JSONArray warehouseArr = new JSONArray();
		FarmCatalogueMaster farmCatalougeMaster = catalogueService
				.findFarmCatalogueMasterByName(getText("CropHarvestcooperative"));
		if (!ObjectUtil.isEmpty(farmCatalougeMaster)) {
			Double d = new Double(farmCatalougeMaster.getId());
			List<FarmCatalogue> farmCatalougeList = catalogueService.findFarmCatalougeByAlpha(d.intValue());
			for (FarmCatalogue catalogue : farmCatalougeList) {
				if (!catalogue.getName().trim().contentEquals(ESESystem.OTHERS)) {
					warehouseArr.add(getJSONObject(catalogue.getCode(), catalogue.getName()));
				}
			}

		}
		warehouseArr.add(getJSONObject("99", "Others"));
		sendAjaxResponse(warehouseArr);
	}

	public void populateIcsNameList() {
		JSONArray icsArr = new JSONArray();
		FarmCatalogueMaster farmCatalougeMaster = catalogueService
				.findFarmCatalogueMasterByName(getLocaleProperty("icsName"));
		if (!ObjectUtil.isEmpty(farmCatalougeMaster)) {
			Double d = new Double(farmCatalougeMaster.getId());
			List<FarmCatalogue> farmCatalougeList = catalogueService.findFarmCatalougeByAlpha(d.intValue());
			for (FarmCatalogue catalogue : farmCatalougeList) {
				if (!catalogue.getName().trim().contentEquals(ESESystem.OTHERS)) {
					icsArr.add(getJSONObject(catalogue.getCode(), catalogue.getName()));
				}
			}
		}
		icsArr.add(getJSONObject("99", "Others"));
		sendAjaxResponse(icsArr);
	}
	
	public void populateQuantity() throws IOException{
		JSONArray jsonArray = new JSONArray(); 
    	JSONObject jsonObject = new JSONObject();
    	List<Object> datas=farmerService.findCropHavestQuantity(farmerId,seasonCode,stateName,farmCode,crop,icsName,branchIdParma,DateUtil.convertStringToDate(startDate, DateUtil.DATE_FORMAT),DateUtil.convertStringToDate(endDate, DateUtil.DATE_FORMAT),selectedFieldStaff);
    	jsonObject.put("totalYieldQuantity", !StringUtil.isEmpty(datas)?datas:"0.0");
    	jsonArray.add(jsonObject);
    	
		response.setContentType("text/JSON");
		PrintWriter out = response.getWriter();
		if (jsonArray.size() > 0)
			out.println(jsonArray.toString());
    	
    	
	}
	 public String getHeaderFields() {
			return headerFields;
		}

		public void setHeaderFields(String headerFields) {
			this.headerFields = headerFields;
		}

		public String getSelectedFieldStaff() {
			return selectedFieldStaff;
		}

		public void setSelectedFieldStaff(String selectedFieldStaff) {
			this.selectedFieldStaff = selectedFieldStaff;
		}
		
		public void populateAgentList() {
			JSONArray agentArr = new JSONArray();
			List<Object[]> agentList = agentService.listAgentIdName();
			if (!ObjectUtil.isEmpty(agentList)) {
				agentList.forEach(obj -> {
					agentArr.add(getJSONObject(obj[0].toString(), obj[1].toString()+" - "+obj[0].toString()));
				});
			}
			sendAjaxResponse(agentArr);
		}
}
