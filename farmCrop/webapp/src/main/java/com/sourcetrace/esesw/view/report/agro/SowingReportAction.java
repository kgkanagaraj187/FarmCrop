package com.sourcetrace.esesw.view.report.agro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
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

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.service.ICatalogueService;
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
import com.sourcetrace.esesw.entity.profile.Coordinates;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

public class SowingReportAction extends BaseReportAction {

	private IFarmerService farmerService;
	private ILocationService locationService;
	private IProductService productService;
	private IProductDistributionService productDistributionService;
	private FarmCrops filter;
	private String gridIdentiy;
	private long procurementproductId;
	private Map<String, String> fields = new HashMap<>();
	@SuppressWarnings("unused")
	private Map<String, String> farmerNameList = new LinkedHashMap<String, String>();
	private Map<Long, String> cropList = new LinkedHashMap<Long, String>();
	private String farmerName;
	private String cropId;
	private String season;
	private String samithi;
	private String icsName;
	private String farmerCodeEnabled;
	private String farmerId;
	private IPreferencesService preferncesService;
	private String branchIdParma;
	@Autowired
	private ICatalogueService catalogueService;
	private String xlsFileName;
	private InputStream fileInputStream;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private String isGenformatDate;
	private String selectedFarm;
	private String selectedSeason;
	private String enableCropCalendar;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1253386206873142128L;

	public String list() {
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			if (preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE) != null) {
				setFarmerCodeEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE));
			}
			if (preferences.getPreferences().get(ESESystem.ENABLE_CROP_CALENDAR) != null) {
			setEnableCropCalendar(preferncesService.findPrefernceByName(ESESystem.ENABLE_CROP_CALENDAR));
			}
		}
		setFilter(new FarmCrops());
		request.setAttribute(HEADING, getText(LIST));
		return LIST;
	}

	public String detail() throws Exception {
		if (ObjectUtil.isEmpty(this.filter))
			this.filter = new FarmCrops();
		/*
		 * HarvestSeason harvestSeason =
		 * farmerService.findHarvestSeasonByCode(getCurrentSeasonsCode()); if
		 * (!ObjectUtil.isEmpty(harvestSeason)) {
		 * filter.setSeason(harvestSeason.getId()); }
		 */
		if (!StringUtil.isEmpty(season)) {
			HarvestSeason harvestSeason = farmerService.findHarvestSeasonByCode(season);
			if (!ObjectUtil.isEmpty(harvestSeason)) {
				filter.setSeason(harvestSeason.getId());
			}
		}
		if (!StringUtil.isEmpty(farmerName))
			filter.setFarmerCode(farmerName);

		if (!StringUtil.isEmpty(icsName)) {
			filter.setIcsName(icsName);

		}

		if (!StringUtil.isEmpty(cropId))
			filter.setCropId(Long.valueOf(cropId));
		if (!StringUtil.isEmpty(samithi))
			filter.setSamithiId(Long.valueOf(samithi));

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
		Map data = readData("farmCrop");
		/*
		 * Map data = reportService.listWithEntityFiltering(getDir(), getSort(),
		 * getStartIndex(), getResults(), filter, getPage());
		 */
		return sendJSONResponse(data);
	}

	public String subGridDetail() throws Exception {

		if (ObjectUtil.isEmpty(this.filter))
			this.filter = new FarmCrops();
		String[] ids = id.split("-");
		filter.setProcurementproductId(Long.valueOf(ids[0]));
		filter.setFarmerId(Long.valueOf(ids[1]));
		super.filter = this.filter;
		gridIdentiy = "SUBGRID";
		if(getCurrentTenantId().equalsIgnoreCase("wilmar")){
			Map data = readData("farmCropSubgridWilmar");
			return sendJSONResponse(data);
		}else{
			Map data = readData("farmCropSubgrid");
			return sendJSONResponse(data);
			}
	
		
	

	}

	@SuppressWarnings("unchecked")

	public JSONObject toJSON(Object obj) {

		JSONObject jsonObject = new JSONObject();
		ESESystem preferences = preferncesService.findPrefernceById("1");
		setFarmerCodeEnabled("0");
		if (!StringUtil.isEmpty(preferences)) {
			if (preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE) != null) {
				setFarmerCodeEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE));
			}
		}
		if (!ObjectUtil.isEmpty(obj)) {
			// FarmCrops crops1 = (FarmCrops) obj;
			Object[] crops = (Object[]) obj;
			if (gridIdentiy != "SUBGRID") {
				if (crops.length > 0) {
					JSONArray rows = new JSONArray();

					if ((getIsMultiBranch().equalsIgnoreCase("1")
							&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

						if (StringUtil.isEmpty(branchIdValue)) {
							rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(crops[15])))
									? getBranchesMap().get(getParentBranchMap().get(crops[15]))
									: getBranchesMap().get(crops[15]));
						}
						rows.add(getBranchesMap().get(crops[15]));

					} else {
						if (StringUtil.isEmpty(branchIdValue)) {
							rows.add(branchesMap.get(crops[15]));
						}
					}
					/*
					 * if (!StringUtil.isEmpty(farmerCodeEnabled) &&
					 * farmerCodeEnabled.equalsIgnoreCase("1")) {
					 * rows.add(crops[2]); }
					 */
					
					String firstName =  String.valueOf(crops[3]);
					String fId = String.valueOf(crops[1]);
					String linkField = "<a href=farmer_detail.action?id="+fId+" target=_blank>"+firstName+"</a>";
					//rows.add(!ObjectUtil.isEmpty(linkField) ? linkField : "");// Farmer
					
					String farmerName = String.valueOf(linkField);
					rows.add(farmerName);
					rows.add(crops[10]);
					rows.add(crops[4]);

					if (getCurrentTenantId().equals("chetna")) {
						if (!StringUtil.isEmpty(crops[14])) {
							FarmCatalogue catalogue = catalogueService.findCatalogueByCode(crops[14].toString());
							rows.add(!ObjectUtil.isEmpty(catalogue) ? catalogue.getName() : "");

						} else {
							rows.add("NA");
						}
					}

					String farmerId = String.valueOf(crops[6]);
					Farm farm = farmerService.findFarmByFarmerId(Long.valueOf(farmerId));

					if (getCurrentTenantId().equals("chetna")) {

						/*
						 * FarmDetailedInfo fdi =
						 * farmerService.findTotalLandHoldingById(farm.
						 * getFarmDetailedInfo().getId());
						 * 
						 * rows.add(fdi.getTotalLandHolding());
						 */
						rows.add(crops[21]);
					}/*else if(getCurrentTenantId().equalsIgnoreCase("ecoagri")){
						rows.add(crops[20]);
					}*//*else if(!getCurrentTenantId().equalsIgnoreCase("ecoagri") ) {
						rows.add((crops[18]!=null ?CurrencyUtil.getDecimalFormat(Double.valueOf(crops[18].toString()), "##.00"):0.00));
					}*/
					rows.add(CurrencyUtil.getDecimalFormat(Double.valueOf(crops[19].toString()), "##.00"));
					if (getCurrentTenantId().equals(ESESystem.PRATIBHA_TENANT_ID)) {
						if (!ObjectUtil.isEmpty(crops[20])) {
							rows.add((double) crops[20] / 100);
						} else {
							rows.add("NA");
						}
					} else if (getCurrentTenantId().equalsIgnoreCase("iccoa")) {
						rows.add(Double.valueOf(crops[20].toString()) / 1000);
					} else if (!getCurrentTenantId().equalsIgnoreCase("ecoagri")) {
						rows.add(CurrencyUtil.getDecimalFormat(Double.valueOf(crops[20].toString()), "##.00"));
					}
					
					
					if (getCurrentTenantId().equals(ESESystem.SUSAGRI)) {
						if (!ObjectUtil.isEmpty(crops[20])) {
							rows.add(crops[20] );
						} else {
							rows.add("NA");
						}
					}
					String seasoncodeName = String.valueOf(crops[8]);

					rows.add(seasoncodeName);

					FarmCrops farmCrop = farmerService.findFarmCropsByFarmCode(Long.valueOf(farm.getId()));
					//rows.add(crops[15]);
					//rows.add(!ObjectUtil.isEmpty(farmCrop) && !ObjectUtil.isEmpty(farmCrop.getFarmCropsCoordinates()) ? farmCrop.getFarmCropsCoordinates().iterator().next().getLatitude() : "");
					//rows.add(!ObjectUtil.isEmpty(farmCrop) && !ObjectUtil.isEmpty(farmCrop.getFarmCropsCoordinates()) ? farmCrop.getFarmCropsCoordinates().iterator().next().getLongitude() : "");
					/* rows.add(crops[6]); */
					/*
					 * rows.add(warehousePaymentDetails.getProduct().
					 * getSubcategory().getCode() + "-" +
					 * warehousePaymentDetails.getProduct().getSubcategory().
					 * getName());
					 */

					if (!ObjectUtil.isEmpty(farmCrop)) {

						if ((!StringUtil.isEmpty(farmCrop.getLatitude())
								&& !StringUtil.isEmpty(farmCrop.getLongitude()))
								|| (!ObjectUtil.isEmpty(farm.getActiveCoordinates())&& !ObjectUtil.isEmpty(farm.getActiveCoordinates().getFarmCoordinates()))) {
							rows.add("<button class='faMap' title='" + getText("farm.map.available.title")
									+ "' onclick='showFarmMap(\"" + farm.getFarmCode() + "\",\"" + farm.getFarmName()
									+ "\",\""
									+ (!ObjectUtil.isEmpty(farmCrop.getLatitude()) ? farmCrop.getLatitude() : "0")
									+ "\",\""
									+ (!ObjectUtil.isEmpty(farmCrop.getLongitude()) ? farmCrop.getLongitude() : "0")
									+ "\"," + getFarmJSONObjects(farm.getActiveCoordinates().getFarmCoordinates()) + ")'></button>");

						} else {
							// No Latlon
							rows.add("<button class='no-latLonIcn' title='" + getText("farm.map.unavailable.title")
									+ "'></button>");
						}
					} else {
						// No Latlon
						rows.add("<button class='no-latLonIcn' title='" + getText("farm.map.unavailable.title")
								+ "'></button>");
					}
					String ids = crops[0] + "-" + crops[1];
					jsonObject.put("id", ids);
					jsonObject.put("cell", rows);
				}
			} else {
				if (crops.length > 0) {
					JSONArray rows = new JSONArray();
					rows.add(!ObjectUtil.isEmpty(crops[0]) ? String.valueOf(crops[0]) : "");
					
					if (!ObjectUtil.isEmpty(crops[6])) {
						String crpCat = String.valueOf(crops[6]);
						if (crpCat.equals("0")) {
							rows.add("Main Crop");
						} else if (crpCat.equals("1")) {
							rows.add("Inter Crop");
						} else if (crpCat.equals("2")) {
							rows.add("Border Crop");
						}
					}
					
					rows.add(!ObjectUtil.isEmpty(crops[5]) ? String.valueOf(crops[5]) : "");
					rows.add(!ObjectUtil.isEmpty(crops[13]) ? String.valueOf(crops[13]) : "");
					rows.add(!ObjectUtil.isEmpty(crops[1]) ? String.valueOf(crops[1]) : "");
					if (getCurrentTenantId().equals(ESESystem.PRATIBHA_TENANT_ID)) {
						if (!ObjectUtil.isEmpty(crops[4])) {
							rows.add((double) crops[4] / 100);
						} else {
							rows.add("NA");
						}
					} 

					else if (getCurrentTenantId().equalsIgnoreCase("iccoa")) {
						rows.add(Double.valueOf(crops[4].toString()) / 1000);
					} else {
						rows.add(CurrencyUtil.getDecimalFormat(Double.valueOf(crops[4].toString()), "##.00"));
					}
					
					if(getCurrentTenantId().equalsIgnoreCase("wilmar")){
						rows.add(!ObjectUtil.isEmpty(crops[3]) ? String.valueOf(crops[3]) : "");
					}else{
						if (!ObjectUtil.isEmpty(preferences)) {
							DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
							rows.add(!ObjectUtil.isEmpty(crops[3]) ? genDate.format(crops[3]).toString() : "");
						}
					}
					if(!StringUtil.isEmpty(crops[2].toString())){
					rows.add((crops[2].toString()!=null ?CurrencyUtil.getDecimalFormat(Double.valueOf(crops[2].toString()), "##.00"):0.00));
				}
					else{
						rows.add("NA");
					}
					setEnableCropCalendar(preferncesService.findPrefernceByName(ESESystem.ENABLE_CROP_CALENDAR));
					if(!StringUtil.isEmpty(getEnableCropCalendar()) && getEnableCropCalendar().equalsIgnoreCase("1")){
					if (!ObjectUtil.isEmpty(crops)) {
						rows.add("<button type='button' class='btn btn-sts' onclick='redirectToCalendarView(\""	+ crops[9] + "\",\""
								+ crops[10] + "\" )'>" + "<i class='fa fa-calendar-check-o' aria-hidden='true'></i></button>");

					} else {
						// No Latlon
						rows.add("<button class='no-latLonIcn' title='" + getText("farm.map.unavailable.title")
								+ "'></button>");
					}
					}
					jsonObject.put("id", crops[0]);
					jsonObject.put("cell", rows);
				}
			}
		}
		return jsonObject;

	}

	public void populateLoadFields() throws IOException {
		/*
		 * if (StringUtil.isEmpty(season)) { season = getCurrentSeasonsCode(); }
		 */

		List<Object[]> objs = productService.findFarmerCountCultivationAreaEsiyield(farmerName, cropId, season, samithi,
				getIcsName());
		/**/

		JSONArray jsonArray = new JSONArray();
		if (!ObjectUtil.isListEmpty(objs)) {
			JSONObject jsonObject = new JSONObject();
			for (Object[] object1 : objs) {
				if (getCurrentTenantId().equalsIgnoreCase("iccoa")) {
					jsonObject.put("estiYield", Double.valueOf(object1[0].toString()) / 1000);
				} else {
					jsonObject.put("estiYield", !StringUtil.isEmpty(object1[0]) ? object1[0] : "0.0");
				}
				jsonObject.put("cultivationArea", !StringUtil.isEmpty(object1[1]) ? NumberFormat.getNumberInstance(Locale.US).format(object1[1]) : "0.0");
				jsonObject.put("farmerCount", !StringUtil.isEmpty(object1[2]) ? NumberFormat.getNumberInstance(Locale.US).format(object1[2]) : "0.0");
				jsonArray.add(jsonObject);
			}
		}
		response.setContentType("text/JSON");
		PrintWriter out = response.getWriter();
		if (jsonArray.size() > 0)
			out.println(jsonArray.toString());
	}

	public void setFarmerNameList(Map<String, String> farmerNameList) {
		this.farmerNameList = farmerNameList;
	}

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	public IProductService getProductService() {
		return productService;
	}

	public void setProductService(IProductService productService) {
		this.productService = productService;
	}

	public FarmCrops getFilter() {
		return filter;
	}

	public void setFilter(FarmCrops filter) {
		this.filter = filter;
	}

	public String getGridIdentiy() {
		return gridIdentiy;
	}

	public void setGridIdentiy(String gridIdentiy) {
		this.gridIdentiy = gridIdentiy;
	}

	public long getProcurementproductId() {
		return procurementproductId;
	}

	public void setProcurementproductId(long procurementproductId) {
		this.procurementproductId = procurementproductId;
	}

	public Map<Long, String> getCropList() {
		Map<Long, String> cropMap = new LinkedHashMap<Long, String>();
		List<ProcurementProduct> cropList = productService.listProcurmentProductsByType("0");
		cropMap = cropList.stream().collect(Collectors.toMap(ProcurementProduct::getId, ProcurementProduct::getName));

		Map<Long, String> orderedCropMap = cropMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
		return orderedCropMap;
	}

	public Map<String, String> getFarmerNameList() {
		Map<String, String> farmerListMap = new LinkedHashMap<String, String>();
		List<Object[]> farmersList = farmerService.listOfFarmersByFarmCrops();
		farmerListMap = farmersList.stream()
				.collect(Collectors.toMap(obj -> String.valueOf(obj[0]), obj -> String.valueOf(obj[1])));

		Map<String, String> orderedFarmerMap = farmerListMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

		return orderedFarmerMap;
	}

	public Map<String, String> getSeasonList() {

		Map<String, String> seasonMap = new LinkedHashMap<String, String>();
		List<Object[]> seasonList = farmerService.listSeasonCodeAndName();
		seasonMap = seasonList.stream()
				.collect(Collectors.toMap(obj -> (String.valueOf(obj[0])), obj -> String.valueOf(obj[1])));
		Map<String, String> orderedMap = seasonMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
		return orderedMap;
	}

	public Map<String, String> getIcsList() {

		Map<String, String> icsList = new LinkedHashMap<String, String>();

		List<Object[]> ics = catalogueService.loadICSName();
		for (Object[] obj : ics) {
			icsList.put(String.valueOf(obj[0]), String.valueOf(obj[1]));
		}
		return icsList;

	}

	public Map<String, String> getSamithiSangham() {

		Map<String, String> samithiMap = new HashMap<>();

		List<Warehouse> samithiSangham = locationService.listSamithiesBasedOnType();

		for (Warehouse warehouse : samithiSangham) {
			samithiMap.put(String.valueOf(warehouse.getId()), warehouse.getName());
		}

		Map<String, String> orderedSanmithi = samithiMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
		return orderedSanmithi;
	}

	public void setCropList(Map<Long, String> cropList) {
		this.cropList = cropList;
	}

	public String getFarmerName() {
		return farmerName;
	}

	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
	}

	public String getCropId() {
		return cropId;
	}

	public void setCropId(String cropId) {
		this.cropId = cropId;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public String getSamithi() {
		return samithi;
	}

	public void setSamithi(String samithi) {
		this.samithi = samithi;
	}

	public ILocationService getLocationService() {
		return locationService;
	}

	public void setLocationService(ILocationService locationService) {
		this.locationService = locationService;
	}

	public String getFarmerCodeEnabled() {
		return farmerCodeEnabled;
	}

	public void setFarmerCodeEnabled(String farmerCodeEnabled) {
		this.farmerCodeEnabled = farmerCodeEnabled;
	}

	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
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
		System.out.print(returnObjects);
		return returnObjects;

	}

	public Map<String, String> getFields() {
		/**
		 * fields.add(getText("season")); fields.add(getText("crop"));
		 * fields.add(getText("group")); fields.add(getText("farmer"));
		 */

		fields.put("1", getText("season"));
		fields.put("2", getText("crop"));
		fields.put("3", getLocaleProperty("samithiName"));
		fields.put("4", getText("farmer"));
		if (getCurrentTenantId().equals("chetna")) {
			fields.put("5", getText("icsName"));
		}

		/*
		 * if ((getIsMultiBranch().equalsIgnoreCase("1") &&
		 * (getIsParentBranch().equals("1")||StringUtil.isEmpty(branchIdValue)))
		 * ) { fields.put("6", getText("app.branch")); }
		 */

		if (getIsMultiBranch().equalsIgnoreCase("1")) {
			if (StringUtil.isEmpty(getBranchId())) {
				fields.put("6", getText("app.branch"));
			} else if (getIsParentBranch().equals("1")) {
				fields.put("6", getText("app.subBranch"));
			}

		} else if (StringUtil.isEmpty(getBranchId())) {

			fields.put("7", getText("app.branch"));
		}

		return fields;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}

	public String populateXLS() throws Exception {

		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getText("sowingReport") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("sowingReport"), fileMap, ".xls"));
		return "xls";
	}

	HSSFRow row;
	int rowNum = 2;
	int serialNo=1;
	@SuppressWarnings("unchecked")
	public InputStream getExportDataStream(String exportType) throws IOException {
		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		setsDate(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));
		boolean flag = true;
		DateFormat filterDateFormat = new SimpleDateFormat(FILTERDATE);

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("exportSowingReportTitle"));
		HSSFPatriarch drawing = sheet.createDrawingPatriarch();

		HSSFCellStyle style1 = wb.createCellStyle();
		HSSFCellStyle style2 = wb.createCellStyle();
		HSSFCellStyle style3 = wb.createCellStyle();
		HSSFCellStyle style4 = wb.createCellStyle();
		HSSFCellStyle style5 = wb.createCellStyle();
		HSSFCellStyle style6 = wb.createCellStyle();
		
		HSSFCellStyle filterStyle = wb.createCellStyle();
		HSSFCellStyle headerStyle = wb.createCellStyle();
		HSSFFont font1 = wb.createFont();
		font1.setFontHeightInPoints((short) 22);

		HSSFFont font2 = wb.createFont();
		font2.setFontHeightInPoints((short) 12);

		HSSFFont font3 = wb.createFont();
		font3.setFontHeightInPoints((short) 10);

		HSSFFont font4 = wb.createFont();
		font3.setFontHeightInPoints((short) 12);
		
		HSSFFont filterFont = wb.createFont();
		filterFont.setFontHeightInPoints((short) 12);

		HSSFRow titleRow, filterRowTitle, filterRow1 = null, filterRow2 = null, filterRow3 = null, filterRow4 = null,
				filterRow5 = null, filterRow6 = null, filterRow7 = null;
		HSSFCell cell;

		int imgRow1 = 0;
		int imgRow2 = 4;
		int imgCol1 = 0;
		int imgCol2 = 0;
		int titleRow1 = 2;
		int titleRow2 = 5;

		/* int rowNum = 2; */
		int colNum = 0;

		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap(); // build branch map to get branch name form branch id.

		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, titleRow1, titleRow2));

		sheet.setDefaultColumnWidth(13);

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(2);
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("exportSowingReportTitle")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);

		if (ObjectUtil.isEmpty(this.filter))
			this.filter = new FarmCrops();

		if (!StringUtil.isEmpty(season)) {
			HarvestSeason harvestSeason = farmerService.findHarvestSeasonByCode(season);
			if (!ObjectUtil.isEmpty(harvestSeason)) {
				filter.setSeason(harvestSeason.getId());
			}
		}
		if (!StringUtil.isEmpty(farmerName))
			filter.setFarmerCode(farmerName);

		if (!StringUtil.isEmpty(icsName)) {
			filter.setIcsName(icsName);

		}

		if (!StringUtil.isEmpty(cropId))
			filter.setCropId(Long.valueOf(cropId));
		if (!StringUtil.isEmpty(samithi))
			filter.setSamithiId(Long.valueOf(samithi));

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

		if (isMailExport()) {
			rowNum++;
			rowNum++;
			
			if (!StringUtil.isEmpty(season) || !StringUtil.isEmpty(cropId) || !StringUtil.isEmpty(samithi) || !StringUtil.isEmpty(farmerName) 
					|| !StringUtil.isEmpty(branchIdValue) || !StringUtil.isEmpty(branchIdParma)) {
			filterRowTitle = sheet.createRow(rowNum++);
			cell = filterRowTitle.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("filter")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(season)) {

				filterRow1 = sheet.createRow(rowNum++);
				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("season")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow1.createCell(2);
				HarvestSeason harvestSeason = farmerService.findHarvestSeasonByCode(season);
				cell.setCellValue(
						new HSSFRichTextString(!ObjectUtil.isEmpty(harvestSeason) ? harvestSeason.getName() : ""));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(cropId)) {
				filterRow2 = sheet.createRow(rowNum++);
				cell = filterRow2.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("cropId")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow2.createCell(2);
				ProcurementProduct procurementProduct = productDistributionService
						.findProcurementProductById(Long.valueOf(filter.getCropId()));
				cell.setCellValue(new HSSFRichTextString(
						!ObjectUtil.isEmpty(procurementProduct) ? procurementProduct.getName() : ""));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(samithi)) {
				filterRow3 = sheet.createRow(rowNum++);
				cell = filterRow3.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("samithi")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow3.createCell(2);
				Warehouse w = locationService.findSamithiById(Long.valueOf(filter.getSamithiId()));
				cell.setCellValue(new HSSFRichTextString(w.getName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(farmerName)) {
				filterRow4 = sheet.createRow(rowNum++);
				cell = filterRow4.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmerName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow4.createCell(2);
				Farmer farmer = farmerService.findFarmerByFarmerId(farmerName);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(farmer) ? farmer.getFirstName() : ""));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
				if (StringUtil.isEmpty(branchIdValue)) {
					filterRow4 = sheet.createRow(rowNum++);
					cell = filterRow4.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("mainOrganization")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow4.createCell(2);
					cell.setCellValue(new HSSFRichTextString(
							getBranchesMap().get(getParentBranchMap().get(filter.getBranchId()))));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					filterRow5 = sheet.createRow(rowNum++);
					cell = filterRow5.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("subOrganization")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow5.createCell(2);
					cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(getSubBranchIdParma())));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

				} else {
					if (!StringUtil.isEmpty(getBranchesMap().get(getSubBranchIdParma()))) {
						filterRow4 = sheet.createRow(rowNum++);
						cell = filterRow4.createCell(1);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("subOrganization")));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						cell = filterRow4.createCell(2);
						cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(getSubBranchIdParma())));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);
					}
				}

			} else {
				if (!StringUtil.isEmpty(branchIdParma)) {
					filterRow5 = sheet.createRow(rowNum++);
					cell = filterRow5.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getText("organization")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow5.createCell(2);
					BranchMaster branch = clientService.findBranchMasterByBranchId(branchIdParma);
					cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(branch) ? branch.getName() : "")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);
				}

			}

		}
		rowNum++;

		List<Object[]> objs = productService.findFarmerCountCultivationAreaEsiyield(farmerName, cropId, season, samithi,
				getIcsName());
		Object farmerCount=null,seedCount=null,totalYield=null;
		for(Object[] obj:objs){
			farmerCount = obj[2];
			seedCount = obj[1];
			totalYield = obj[0];
		}
		
		String CountHeader=getText("sowingReportCountHeader");
		String subHeader[]=CountHeader.split("\\,");
		row = sheet.createRow(rowNum++);
		colNum = 1;
			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(subHeader[0]));
			headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cell.setCellStyle(headerStyle);
			font2.setBoldweight((short) 12);
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			headerStyle.setFont(font2);
			cell = row.createCell(colNum++);
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(farmerCount)? farmerCount.toString() : ""));
			//cell.setCellValue(!ObjectUtil.isEmpty(farmerCount)? Long.valueOf(farmerCount.toString()) : 0);
			headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cell.setCellStyle(headerStyle);
			font2.setBoldweight((short) 12);
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			headerStyle.setFont(font2);
			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(subHeader[1]));
			headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cell.setCellStyle(headerStyle);
			font2.setBoldweight((short) 12);
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			headerStyle.setFont(font2);
			cell = row.createCell(colNum++);
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(seedCount)? seedCount.toString() : ""));
			//cell.setCellValue(!ObjectUtil.isEmpty(seedCount)? Long.valueOf(seedCount.toString()) : 0);
			headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cell.setCellStyle(headerStyle);
			font2.setBoldweight((short) 12);
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			headerStyle.setFont(font2);
			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(subHeader[2]));
			headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cell.setCellStyle(headerStyle);
			font2.setBoldweight((short) 12);
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			headerStyle.setFont(font2);
			cell = row.createCell(colNum++);
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			font2.setBoldweight((short) 12);
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			headerStyle.setFont(font2);
			cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(totalYield)? totalYield.toString() : ""));
			//cell.setCellValue(!ObjectUtil.isEmpty(totalYield)? Double.parseDouble(String.valueOf(totalYield)) : 0.00);
			headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cell.setCellStyle(headerStyle);
			
			rowNum++;
			rowNum++;
		
		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		String mainGridColumnHeaders = "";

		ESESystem preferences = preferncesService.findPrefernceById("1");
		setFarmerCodeEnabled("0");
		if (!StringUtil.isEmpty(preferences)) {
			if (preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE) != null) {
				setFarmerCodeEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE));
			}
			if (preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT) != null) {
				setIsGenformatDate(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
			}
		}

		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getText("SowingExportColumnHeadingBranch");
			} else if (!StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getText("SowingExportColumnHeadingBranch");
			}
		} else if (getIsMultiBranch().equalsIgnoreCase("1") && getIsParentBranch().equals("0")) {
			mainGridColumnHeaders = getLocaleProperty("SowingExportColumnHeader1");
		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				if (farmerCodeEnabled.equals("1")) {
					mainGridColumnHeaders = getLocaleProperty("sowingExportBranchAndCode");
				} else {
					mainGridColumnHeaders = getLocaleProperty("sowingExportBranch");
				}
			} else {
				if (farmerCodeEnabled.equals("1")) {
					mainGridColumnHeaders = getLocaleProperty("sowingExportWithCode");
				} else {
					mainGridColumnHeaders = getLocaleProperty("sowingExport");
				}
			}
		}

		
		if (getCurrentTenantId().equalsIgnoreCase("ocp")) {

			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("exportSowingColumnHeaderBranchWelspun");
			} else if (!getIsParentBranch().equalsIgnoreCase("0")) {
				mainGridColumnHeaders = getLocaleProperty("ParentSowingExportHeader");
			} else
				mainGridColumnHeaders = getLocaleProperty("SowingExportHeader");
		}
		
		int mainGridIterator = 0;

		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {

			if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
			} else if (cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());
			}

			cell = mainGridRowHead.createCell(mainGridIterator);
			cell.setCellValue(new HSSFRichTextString(cellHeader));
			style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cell.setCellStyle(style2);
			style2.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
			style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cell.setCellStyle(style2);
			font2.setBoldweight((short) 12);
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			style2.setFont(font2);
			sheet.setColumnWidth(mainGridIterator, (15 * 550));
			mainGridIterator++;
		}

		if (ObjectUtil.isEmpty(this.filter))
			this.filter = new FarmCrops();
		if (!StringUtil.isEmpty(season)) {
			HarvestSeason harvestSeason = farmerService.findHarvestSeasonByCode(season);
			if (!ObjectUtil.isEmpty(harvestSeason)) {
				filter.setSeason(harvestSeason.getId());
			}
		}
		if (!StringUtil.isEmpty(farmerName))
			filter.setFarmerCode(farmerName);

		if (!StringUtil.isEmpty(icsName)) {
			filter.setIcsName(icsName);

		}

		if (!StringUtil.isEmpty(cropId))
			filter.setCropId(Long.valueOf(cropId));
		if (!StringUtil.isEmpty(samithi))
			filter.setSamithiId(Long.valueOf(samithi));

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
		Map data = readData("farmCrop");
		Map subData;
		List<Object[]> mainGridRows = (List<Object[]>) data.get(ROWS);
		super.filter = new FarmCrops();
		if(getCurrentTenantId().equalsIgnoreCase("wilmar")){
			subData = readData("farmCropSubgridWilmar");
		
		}else{
			subData = readData("farmCropSubgrid");
		
			}
	
		List<Object[]> subGridRows = (List<Object[]>) subData.get(ROWS);

		Map<String, List<Object[]>> subgridgr = subGridRows.stream().collect(
				Collectors.groupingBy(choice -> (choice[7].toString().trim() + "-" + choice[8].toString().trim())));
		Long serialNumber = 0L;

		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;

		for (Object[] obj : mainGridRows) {
			row = sheet.createRow(rowNum++);
			colNum = 0;

			/*
			 * serialNumber++; cell = row.createCell(colNum++);
			 * cell.setCellValue(new
			 * HSSFRichTextString(String.valueOf(serialNumber)!=null ?
			 * String.valueOf(serialNumber) : ""));
			 * 
			 */

			cell = row.createCell(colNum++);
			style5.setAlignment(CellStyle.ALIGN_CENTER);
			cell.setCellStyle(style5);
			cell.setCellValue(serialNo++);
			
			Object[] crops = (Object[]) obj;
			if (crops.length > 0) {
				JSONArray rows = new JSONArray();
				if ((getIsMultiBranch().equalsIgnoreCase("1")
						&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

					if (StringUtil.isEmpty(branchIdValue)) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(
								!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(crops[15])))
										? getBranchesMap().get(getParentBranchMap().get(crops[15]))
										: getBranchesMap().get(crops[15])));
					}
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(crops[15])));
				} else {
					if (StringUtil.isEmpty(branchIdValue)) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(branchesMap.get(crops[15])));
					}
				}
				/*
				 * if (!StringUtil.isEmpty(farmerCodeEnabled) &&
				 * farmerCodeEnabled.equalsIgnoreCase("1")) { cell =
				 * row.createCell(colNum++); cell.setCellValue(new
				 * HSSFRichTextString(!StringUtil.isEmpty(crops[2]) &&
				 * crops[2]!=null ? crops[2].toString() : "")); }
				 */
				if (!getCurrentTenantId().equals("agro")) {
				if (farmerCodeEnabled.equals("1")) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							!StringUtil.isEmpty(crops[2]) && crops[2] != null ? crops[2].toString() : ""));
				}
				}
				String farmerName = String.valueOf(crops[3]);
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(farmerName));
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(crops[10].toString()));
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(crops[4].toString()));

				if (getCurrentTenantId().equals("chetna")) {
					if (!StringUtil.isEmpty(crops[14])) {
						FarmCatalogue catalogue = getCatlogueValueByCode(crops[14].toString());
						cell = row.createCell(colNum++);
						cell.setCellValue(
								new HSSFRichTextString(!ObjectUtil.isEmpty(catalogue) ? catalogue.getName() : ""));

					} else {
						rows.add("NA");
					}
				}
				/*
				 * cell = row.createCell(colNum++); cell.setCellValue(new
				 * HSSFRichTextString(crops[5].toString()));
				 */

				if (getCurrentTenantId().equals("chetna")) {
					/*
					 * FarmDetailedInfo fdi =
					 * farmerService.findTotalLandHoldingById(farm.
					 * getFarmDetailedInfo().getId()); cell =
					 * row.createCell(colNum++); cell.setCellValue(new
					 * HSSFRichTextString(!StringUtil.isEmpty(fdi)?
					 * fdi.getTotalLandHolding() : ""));
					 */

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							!StringUtil.isEmpty(crops[21]) && crops[21] != null ? crops[21].toString() : ""));
				} else {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							!StringUtil.isEmpty(crops[19]) && crops[19] != null ? crops[19].toString() : ""));
					style4.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					cell.setCellStyle(style4);
				}
				if (getCurrentTenantId().equals(ESESystem.PRATIBHA_TENANT_ID)) {
					if (!ObjectUtil.isEmpty(crops[20])) {
						cell = row.createCell(colNum++);
						cell.setCellValue(!ObjectUtil.isEmpty(crops[20]) && crops[20] != null
								? Double.valueOf(crops[20].toString()) / 100 : 0.00);
					} 
				} else if (getCurrentTenantId().equalsIgnoreCase("iccoa")) {
					cell = row.createCell(colNum++);
					cell.setCellValue(!ObjectUtil.isEmpty(crops[20]) && crops[20] != null
							? Double.valueOf(crops[20].toString()) / 1000 : 0.00);
				} else {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							!StringUtil.isEmpty(crops[20]) && crops[20] != null ? crops[20].toString() : "")); 
					style4.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					cell.setCellStyle(style4);
				}

				String seasoncodeName = String.valueOf(crops[8]);

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(seasoncodeName));

				// String procId = String.valueOf(crops[1]);

				HSSFRow subGridRowHead = sheet.createRow(rowNum++);
				String subGridColumnHeaders = "";
				subGridColumnHeaders = getLocaleProperty("sowingSubgrid");				
				int subGridIterator = 1;

				for (String cellHeader : subGridColumnHeaders.split("\\,")) {

					if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
						cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
					} else if (cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT)) {
						cellHeader = cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());
					}

					cell = subGridRowHead.createCell(subGridIterator);
					cell.setCellValue(new HSSFRichTextString(cellHeader));
					style3.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
					style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					cell.setCellStyle(style3);
					font2.setBoldweight((short) 12);
					font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					style3.setFont(font2);
					sheet.setColumnWidth(subGridIterator, (15 * 550));
					subGridIterator++;
				}

				/*
				 * List<Object[]> farmCropsList =
				 * farmerService.findFarmCropsByFarmerIdAndProductId(Long.
				 * valueOf(crops[6].toString()),Long.valueOf(crops[18].toString(
				 * )));
				 * 
				 * 
				 * colNum = 1;
				 * farmCropsList.stream().filter(farmCrop->!ObjectUtil.isEmpty(
				 * farmCrop)).forEach(farmCrop->{ int subcolNum = 1; row =
				 * sheet.createRow(rowNum++); HSSFCell cellz =
				 * row.createCell(subcolNum++); cellz.setCellValue(new
				 * HSSFRichTextString(!ObjectUtil.isEmpty(farmCrop[0]) ?
				 * farmCrop[0].toString() : ""));
				 * 
				 * cellz = row.createCell(subcolNum++); cellz.setCellValue(new
				 * HSSFRichTextString(!ObjectUtil.isEmpty(farmCrop[1]) ?
				 * farmCrop[1].toString() : ""));
				 * 
				 * cellz = row.createCell(subcolNum++); cellz.setCellValue(new
				 * HSSFRichTextString(!ObjectUtil.isEmpty(farmCrop[2]) ?
				 * farmCrop[2].toString() : ""));
				 * 
				 * if(!ObjectUtil.isEmpty(preferences)){ DateFormat genDate =
				 * new SimpleDateFormat(getIsGenformatDate()); cellz =
				 * row.createCell(subcolNum++); cellz.setCellValue(new
				 * HSSFRichTextString(!ObjectUtil.isEmpty(farmCrop[3])?genDate.
				 * format(farmCrop[3]) : "")); }else{ cellz =
				 * row.createCell(subcolNum++); cellz.setCellValue(new
				 * HSSFRichTextString("")); }
				 * 
				 * if (getCurrentTenantId().equalsIgnoreCase("iccoa")) { cellz =
				 * row.createCell(subcolNum++);
				 * cellz.setCellValue(!ObjectUtil.isEmpty(farmCrop)?Double.
				 * valueOf(farmCrop[4].toString())/1000:0.00); }else{ cellz =
				 * row.createCell(subcolNum++); cellz.setCellValue(new
				 * HSSFRichTextString(!ObjectUtil.isEmpty(farmCrop[4]) ?
				 * farmCrop[4].toString() : "")); }
				 * 
				 * cellz = row.createCell(subcolNum++); cellz.setCellValue(new
				 * HSSFRichTextString(!ObjectUtil.isEmpty(crops[5]) ?
				 * String.valueOf(crops[5]) : ""));
				 * 
				 * 
				 * cellz = row.createCell(subcolNum++);
				 * 
				 * if(!ObjectUtil.isEmpty(crops[6])){ String crpCat=
				 * String.valueOf(crops[6]); if(crpCat.equals("0")){
				 * 
				 * cellz.setCellValue("Main Crop");
				 * 
				 * }else if(crpCat.equals("1")){
				 * 
				 * cellz.setCellValue("Inter Crop");
				 * 
				 * }else if(crpCat.equals("2")){
				 * 
				 * cellz.setCellValue("Border Crop");
				 * 
				 * }else{ cellz.setCellValue("Main Crop"); }
				 * 
				 * }
				 * 
				 * 
				 * });
				 */
				List<Object[]> subRows = (List<Object[]>) subgridgr.get(
						Long.valueOf(crops[0].toString().trim()) + "-" + Long.valueOf(crops[1].toString().trim()));
				for (Object[] objSub : subRows) {
					Object[] subCrops = (Object[]) objSub;
					colNum = 1;
					if (subCrops.length > 0) {
						int subcolNum = 1;
						row = sheet.createRow(rowNum++);
						HSSFCell cellz = row.createCell(subcolNum++);
						cellz.setCellValue(new HSSFRichTextString(
								!ObjectUtil.isEmpty(subCrops[0]) ? String.valueOf(subCrops[0]) : ""));
						
						
						cellz = row.createCell(subcolNum++);

						if (!ObjectUtil.isEmpty(subCrops[6])) {
							String crpCat = String.valueOf(subCrops[6]);
							if (crpCat.equals("0")) {
								cellz.setCellValue("Main Crop");

							} else if (crpCat.equals("1")) {

								cellz.setCellValue("Inter Crop");

							} else if (crpCat.equals("2")) {

								cellz.setCellValue("Border Crop");

							} else {
								cellz.setCellValue("Main Crop");
							}
						}
						
						
						cellz = row.createCell(subcolNum++);
						cellz.setCellValue(new HSSFRichTextString(
								!ObjectUtil.isEmpty(subCrops[5]) ? String.valueOf(subCrops[5]) : ""));
						cellz = row.createCell(subcolNum++);
						cellz.setCellValue(new HSSFRichTextString(
								!ObjectUtil.isEmpty(subCrops[13]) ? String.valueOf(subCrops[13]) : ""));
						cellz = row.createCell(subcolNum++);
						cellz.setCellValue(new HSSFRichTextString(
								!ObjectUtil.isEmpty(subCrops[1]) ? String.valueOf(subCrops[1]) : ""));
						

						if (getCurrentTenantId().equals(ESESystem.PRATIBHA_TENANT_ID)) {
							if (!ObjectUtil.isEmpty(crops[20])) {
								cellz = row.createCell(subcolNum++);
								cellz.setCellValue(Double.valueOf(subCrops[4].toString()) / 100);
							} else {
								cellz = row.createCell(subcolNum++);
								cellz.setCellValue("");
							}
						} 
						else if (getCurrentTenantId().equalsIgnoreCase("iccoa")) {
							cellz = row.createCell(subcolNum++);
							cellz.setCellValue(Double.valueOf(subCrops[4].toString()) / 1000);
						} else {
							cellz = row.createCell(subcolNum++);
							//cellz.setCellValue(new HSSFRichTextString(
								//	!ObjectUtil.isEmpty(subCrops[4]) ? String.valueOf(subCrops[4]) : ""));
							
							style6 .setAlignment(CellStyle.ALIGN_RIGHT);
							cellz.setCellStyle(style6);
							//cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						    cellz.setCellValue(Double.valueOf(subCrops[4].toString()));
						}
						if (getCurrentTenantId().equalsIgnoreCase("wilmar")) {
							cellz = row.createCell(subcolNum++);
							cellz.setCellValue(new HSSFRichTextString(
									!ObjectUtil.isEmpty(subCrops[3]) ? String.valueOf(subCrops[3]) : ""));
						}else{
							if (!StringUtil.isEmpty(preferences)) {
								DateFormat genDate = new SimpleDateFormat(getIsGenformatDate());
								cellz = row.createCell(subcolNum++);
								cellz.setCellValue(new HSSFRichTextString(
										!ObjectUtil.isEmpty(subCrops[3]) ? genDate.format(subCrops[3]).toString() : ""));
								
							} else {
								cellz = row.createCell(subcolNum++);
								cellz.setCellValue(new HSSFRichTextString(""));
							}
						}

					
				
						cellz = row.createCell(subcolNum++);
						style6 .setAlignment(CellStyle.ALIGN_RIGHT);
						cellz.setCellStyle(style6);
						cellz.setCellValue(new HSSFRichTextString(
								!ObjectUtil.isEmpty(subCrops[2]) ? String.valueOf(subCrops[2]) : ""));
						
					/*	cellz = row.createCell(subcolNum++);
						cellz.setCellValue(new HSSFRichTextString(
								!ObjectUtil.isEmpty(subCrops[11]) ? String.valueOf(subCrops[11]) : ""));
						
						cellz = row.createCell(subcolNum++);
						cellz.setCellValue(new HSSFRichTextString(
								!ObjectUtil.isEmpty(subCrops[12]) ? String.valueOf(subCrops[12]) : ""));*/

			
					}
				}

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

	public String getBranchIdParma() {
		return branchIdParma;
	}

	public void setBranchIdParma(String branchIdParma) {
		this.branchIdParma = branchIdParma;
	}

	public String getIcsName() {

		return icsName;
	}

	public void setIcsName(String icsName) {

		this.icsName = icsName;
	}

	public String getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
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

	public static SimpleDateFormat getFilenamedateformat() {
		return fileNameDateFormat;
	}

	public IProductDistributionService getProductDistributionService() {
		return productDistributionService;
	}

	public void setProductDistributionService(IProductDistributionService productDistributionService) {
		this.productDistributionService = productDistributionService;
	}

	public String getIsGenformatDate() {
		return isGenformatDate;
	}

	public void setIsGenformatDate(String isGenformatDate) {
		this.isGenformatDate = isGenformatDate;
	}
	public void populateCalendarValues() throws Exception {
		if (!selectedFarm.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedFarm)) 
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

	public String getEnableCropCalendar() {
		return enableCropCalendar;
	}

	public void setEnableCropCalendar(String enableCropCalendar) {
		this.enableCropCalendar = enableCropCalendar;
	}
	
	
	
	
}
