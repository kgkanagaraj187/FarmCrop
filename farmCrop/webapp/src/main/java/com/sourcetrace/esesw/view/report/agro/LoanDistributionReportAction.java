/*
 * ProcurementReportAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view.report.agro;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.annotation.processing.Filer;

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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.usermodel.Workbook;
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

import com.ese.entity.util.ESESystem;
import com.ese.entity.util.FilterFieldData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sourcetrace.eses.dao.IReportDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.profile.GradeMaster;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfig;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfigDetail;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfigFilter;
import com.sourcetrace.eses.order.entity.txn.LoanDistribution;
import com.sourcetrace.eses.order.entity.txn.LoanDistributionDetail;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.order.entity.txn.ProcurementDetail;
import com.sourcetrace.eses.order.entity.txn.SupplierProcurement;
import com.sourcetrace.eses.order.entity.txn.SupplierProcurementDetail;
import com.sourcetrace.eses.service.DynamicReportProperties;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.profile.Product;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.Vendor;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

@SuppressWarnings("unchecked")
public class LoanDistributionReportAction extends BaseReportAction implements IExporter {

	private static final long serialVersionUID = 1L;
	private static final String QUANTITY = "quantity";
	private static final String TOTAL_AMT="totalAmt";
	private String mainGridCols;
	private static String subGridCols;
	
	private List<Object[]> farmList;
	
	private List<Object[]> farmerList;
	
	private String gridIdentity;
	private Map<String, String> warehouseMap = new HashMap<>();
	private String filterList;
	private String seasonCode;
	private String xlsFileName;
	private InputStream fileInputStream;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private LoanDistribution filter;
	private Map<String, String> fpoMap = new HashMap<>();
	private Map<String, String> farmerMap = new HashMap<>();
	private List<DynamicReportConfigFilter> reportConfigFilters = new LinkedList<>();
	protected List<String> fields = new ArrayList<String>();
	Map<String, String> villageMap = new LinkedHashMap<>();
	int serialNo = 1;
	private String daterange;
	private String tenantId;
	
	private String villageName;
	private String farmerId;
	private String groupId;
	
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private IAgentService agentService;
	@Autowired
	private IProductService productService;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private IPreferencesService preferncesService;
	@Autowired
	private IProductDistributionService productDistributionService;
	private String headerFields;

	private String branch;
	
	public String getHeaderFields() {
		return headerFields;
	}

	public void setHeaderFields(String headerFields) {
		this.headerFields = headerFields;
	}
	

	public String list() {
		Calendar currentDate = Calendar.getInstance();
		Calendar cal = (Calendar) currentDate.clone();
		cal.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) - 1);
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		super.startDate = df.format(cal.getTime());
		super.endDate = df.format(currentDate.getTime());		
		daterange = super.startDate + " - " + super.endDate;
		request.setAttribute(HEADING, getText(LIST));		
		ESESystem preferences = preferncesService.findPrefernceById("1");
		filter = new LoanDistribution();
		return LIST;

		
	}
	
	public String subGridDetail() throws Exception {
		
	
			LoanDistributionDetail loanDistributionDetail = new LoanDistributionDetail();
			LoanDistribution loanDistribution = new LoanDistribution();
			loanDistribution.setId(Long.valueOf(id));
			loanDistributionDetail.setLoanDistribution(loanDistribution);
			if (getPage() > 1) {
				serialNo = (getPage() - 1) * 10 + 1;
			}
			super.filter = loanDistributionDetail;
			Map data = readData();
			return sendJSONResponse(data);
		
	}

	public String detail() throws Exception {
		
		setFilter(new LoanDistribution());
			
			if (!StringUtil.isEmpty(farmerId)) {
			filter.setFarmer(farmerService.findFarmerByFarmerId(farmerId.trim()));
				
			}
			if (!StringUtil.isEmpty(villageName)) {
				filter.setVillage(locationService.findVillageByCode(villageName.trim()));
			}
			
			if (!StringUtil.isEmpty(groupId)) {
				filter.setGroup(farmerService.findFarmerByFarmerId(groupId.trim()));
			}
			
			if(getVendorsList().size() == 1){
				getVendorsList().entrySet().stream().forEach(i ->{
				Vendor vendor=new Vendor();
					filter.setVendor(vendor);
					filter.getVendor().setVendorId(i.getKey());
				});
			}
			filter.setBranchId(getBranchId());
			super.filter = this.filter;
			Map data = readData();
			return sendJSONResponse(data);
		
	}

	public JSONObject toJSON(Object obj) {
		JSONObject jsonObject = new JSONObject();
		ESESystem preferences = preferncesService.findPrefernceById("1");
			if (obj instanceof LoanDistribution) {
				LoanDistribution loanDistribution = (LoanDistribution) obj;
				JSONArray rows = new JSONArray();

				if (!StringUtil.isEmpty(preferences)) {
					DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
					rows.add(!ObjectUtil.isEmpty(loanDistribution)
							? (!StringUtil.isEmpty(genDate.format(loanDistribution.getLoanDate()))
									? genDate.format(loanDistribution.getLoanDate()) : "")
							: "");
				}

				rows.add(!ObjectUtil.isEmpty(loanDistribution.getAgroTransaction()) && !ObjectUtil.isEmpty(loanDistribution.getAgroTransaction().getAccount())
						? (!StringUtil.isEmpty(loanDistribution.getAgroTransaction().getAccount().getLoanAccountNo()) ?loanDistribution.getAgroTransaction().getAccount().getLoanAccountNo() : "") : "");

				//rows.add(!ObjectUtil.isEmpty(loanDistribution.getLoanTo()) ? getLocaleProperty("loanTo" + loanDistribution.getLoanTo()) : "");

				rows.add(!ObjectUtil.isEmpty(loanDistribution.getVendor()) ? loanDistribution.getVendor().getVendorName() : "NA");
				if(!ObjectUtil.isEmpty(loanDistribution.getFarmer())&&(!StringUtil.isEmpty(loanDistribution.getFarmer()))){
				rows.add(!ObjectUtil.isEmpty(loanDistribution.getFarmer()) ?(!StringUtil.isEmpty(loanDistribution.getFarmer().getSurName()) ? loanDistribution.getFarmer().getFirstName()+" "+loanDistribution.getFarmer().getSurName()  : loanDistribution.getFarmer().getFirstName()) : "NA" );
				}else{
				rows.add(!ObjectUtil.isEmpty(loanDistribution.getFarmer()) ?(!StringUtil.isEmpty(loanDistribution.getFarmer().getSurName()) ? loanDistribution.getFarmer().getFirstName()+" "+loanDistribution.getFarmer().getSurName(): loanDistribution.getFarmer().getFirstName()) : "NA" );
				}
				//rows.add(!ObjectUtil.isEmpty(loanDistribution.getVillage()) ? loanDistribution.getVillage().getName() : "");
				
				//rows.add(!ObjectUtil.isEmpty(loanDistribution.getLoanCategory()) ? getLocaleProperty("loanCategory" + loanDistribution.getLoanCategory()) : "");

				Map<String, Object> details = getTotalLoanDistributionDetails(loanDistribution);

				rows.add(CurrencyUtil.getDecimalFormat((Double) details.get(QUANTITY), "##.00"));
				
				rows.add(CurrencyUtil.getDecimalFormat((Double) details.get(TOTAL_AMT), "###.00"));
				
				rows.add(CurrencyUtil.getDecimalFormat((Double) loanDistribution.getInterestPercentage(), "##.00"));
				
				rows.add(CurrencyUtil.getDecimalFormat((Double) loanDistribution.getInterestAmt(), "###.00"));
				
				rows.add(!ObjectUtil.isEmpty(loanDistribution.getLoanTenure()) ? loanDistribution.getLoanTenure() : 0);
				
				rows.add(CurrencyUtil.getDecimalFormat((Double) loanDistribution.getLoanTenureAmt(), "###.00"));
				
				rows.add(CurrencyUtil.getDecimalFormat((Double) loanDistribution.getTotalCostToFarmer(), "###.00"));
				
				rows.add(CurrencyUtil.getDecimalFormat((Double) loanDistribution.getMonthlyRepaymentAmt(), "###.00"));

				
				
				jsonObject.put("id", loanDistribution.getId());
				jsonObject.put("cell", rows);
			} else if (obj instanceof LoanDistributionDetail) {

				
				LoanDistributionDetail loanDistributionDetail = (LoanDistributionDetail) obj;

					JSONArray rows = new JSONArray();
			if (!ObjectUtil.isEmpty(loanDistributionDetail.getProduct().getSubcategory())) {

				rows.add(!ObjectUtil.isEmpty(loanDistributionDetail.getProduct().getSubcategory().getName())
						? loanDistributionDetail.getProduct().getSubcategory().getName() : "");
			} else {
				rows.add("");
			}
					if(!ObjectUtil.isEmpty(loanDistributionDetail.getProduct())){
						
						rows.add(!ObjectUtil.isEmpty(loanDistributionDetail.getProduct().getName()) ? loanDistributionDetail.getProduct().getName() : "");
					}else{
						rows.add("");
					}
					rows.add(CurrencyUtil.getDecimalFormat(loanDistributionDetail.getRatePerUnit(), "###.00"));
					
					rows.add(CurrencyUtil.getDecimalFormat(loanDistributionDetail.getQuantity(), "##.00"));
					
					rows.add(CurrencyUtil.getDecimalFormat(loanDistributionDetail.getAmount(), "###.00"));
					
					//rows.add(CurrencyUtil.getDecimalFormat(loanDistributionDetail.getSubsidyInterest(), "##.00"));

					//rows.add(CurrencyUtil.getDecimalFormat(loanDistributionDetail.getSubsidyAmt(), "#,###.00"));

					rows.add(CurrencyUtil.getDecimalFormat(loanDistributionDetail.getTotalAmt(), "###.00"));

					jsonObject.put("id", loanDistributionDetail.getId());
					jsonObject.put("cell", rows);
					serialNo++;
				

			}
		
		return jsonObject;
	}

	
	
/*	public Map<String, String> getFarmersList() {

		Map<String, String> farmerMap = new LinkedHashMap<>();
		List<Object[]> farmerList = farmerService.listFarmerInfoByProcurement();
		List<String> testList = new ArrayList<>();
		if (!ObjectUtil.isEmpty(farmerList)) {

			farmerMap = farmerList.stream()
					.collect(Collectors.toMap(obj -> (String.valueOf(obj[0])), obj -> (String.valueOf(obj[1]))));

		}
		return farmerMap;
	}*/
	public Map<String, String> getFarmersList() {
		Map<String, String> farmerMap = new LinkedHashMap<String, String>();
		
		farmerService.listFarmerByLoanDistribution().stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			/*if(objArr[13]!=null){
				farmerMap.put(String.valueOf(objArr[1]), String.valueOf(objArr[3])+" - "+String.valueOf(objArr[13]));}
				else{*/
			    farmerMap.put(String.valueOf(objArr[1]), !ObjectUtil.isEmpty(objArr[4]) 
			    		? String.valueOf(objArr[3])+" "+String.valueOf(objArr[4])+" - "+String.valueOf(objArr[2]) 
			    		:  String.valueOf(objArr[3])+" - "+String.valueOf(objArr[2]));	
			/*	}*/
		});

		return farmerMap;
	}
	
	/*public Map<String, String> getGroupList() {
		Map<String, String> groupMap = new LinkedHashMap<String, String>();
		
		farmerService.listGroupInfoWithTypez().stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			groupMap.put(String.valueOf(objArr[1]), String.valueOf(objArr[3]));
		});

		return groupMap;
	}*/


	public Map<String, String> getFarmerFirstNameList() {
		Map<String, String> farmerMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(farmerList)) {
			farmerList = farmerService.listFarmerInfo();
		}
		farmerList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			farmerMap.put(String.valueOf(objArr[3]), String.valueOf(objArr[3]));
		});

		return farmerMap;
	}

	public Map<String, String> getFarmsList() {
		Map<String, String> farmMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(farmList)) {
			farmList = farmerService.listFarmInfo();
		}
		farmList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			farmMap.put(String.valueOf(objArr[1]), String.valueOf(objArr[2]));
		});

		return farmMap;
	}

	public String getMainGridCols() {
		return mainGridCols;
	}

	public void setMainGridCols(String mainGridCols) {
		this.mainGridCols = mainGridCols;
	}

	public String getSubGridCols() {
		return subGridCols;
	}

	public void setSubGridCols(String subGridCols) {
		this.subGridCols = subGridCols;
	}

	
	public String getGridIdentity() {
		return gridIdentity;
	}

	public void setGridIdentity(String gridIdentity) {
		this.gridIdentity = gridIdentity;
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

	public List<DynamicReportConfigFilter> getReportConfigFilters() {
		return reportConfigFilters;
	}

	public void setReportConfigFilters(List<DynamicReportConfigFilter> reportConfigFilters) {
		this.reportConfigFilters = reportConfigFilters;
	}

	public String getFilterList() {
		return filterList;
	}

	public void setFilterList(String filterList) {
		this.filterList = filterList;
	}

	
	public String getSeasonCode() {
		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
	}

	public Map<String, String> getWarehouseMap() {
		if (warehouseMap.size() <= 0) {
			warehouseMap = locationService.listWarehouse().stream()
					.collect(Collectors.toMap(warehouse -> String.valueOf(warehouse.getId()), Warehouse::getName));
		}
		return warehouseMap;
	}

	public Map<String, String> getFpoMap() {
		if (fpoMap.size() <= 0) {
			fpoMap = farmerService.listCatelogueType(getText("fpoType")).stream()
					.collect(Collectors.toMap(FarmCatalogue::getCode, FarmCatalogue::getName));
		}
		return fpoMap;
	}

	public Map<String, String> getFarmerMap() {
		if (farmerMap.size() <= 0) {
			farmerService.listFarmerInfo().stream().forEach(farmer -> {
				farmerMap.put(String.valueOf(farmer[1]), String.valueOf(farmer[6]));
			});
		}
		return farmerMap;
	}

	private Map<String, Object> getTotalLoanDistributionDetails(LoanDistribution loanDistribution) {

		
		double qty = 0;
		double totalAmt=0;
		Map<String, Object> details = new HashMap<String, Object>();
		if (!ObjectUtil.isListEmpty(loanDistribution.getLoanDistributionDetail())) {
			for (LoanDistributionDetail detail : loanDistribution.getLoanDistributionDetail()) {
				qty = qty + detail.getQuantity();
				totalAmt=totalAmt + detail.getTotalAmt();
			}
		}
		details.put(QUANTITY, qty);
		details.put(TOTAL_AMT, totalAmt);
		return details;
	}

	public String getDaterange() {
		return daterange;
	}

	public void setDaterange(String daterange) {
		this.daterange = daterange;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public void setWarehouseMap(Map<String, String> warehouseMap) {
		this.warehouseMap = warehouseMap;
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public void setFpoMap(Map<String, String> fpoMap) {
		this.fpoMap = fpoMap;
	}

	public void setFarmerMap(Map<String, String> farmerMap) {
		this.farmerMap = farmerMap;
	}

	
	public Map<String, String> getVillageList() {

		Map<String, String> farmerMap = new LinkedHashMap<>();
		List<Object[]> farmerList = locationService.listOfVillageInfoByProcurement();
		List<String> villagesList = new ArrayList<>();
		if (!ObjectUtil.isEmpty(farmerList)) {
			villagesList = farmerList.stream().map(obj -> String.valueOf(obj[1])).distinct()
					.collect(Collectors.toList());
			farmerMap = villagesList.stream().collect(Collectors.toMap(String::toString, String::toString));

		}
		return farmerMap;
	}

	public Map<String, String> getVillageMap() {

		// return locationService.listVillageIdAndName();

		locationService.listVillageIdAndName().stream().forEach(u -> {
			villageMap.put(u[1].toString(), u[2].toString());
		});
		return villageMap;
	}

	public String getVillageName() {
		return villageName;
	}

	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}

	public String getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}
	
	
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public LoanDistribution getFilter() {
		return filter;
	}

	public void setFilter(LoanDistribution filter) {
		this.filter = filter;
	}

	public String getGeneralDateFormat() {
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			return preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT);
		}
		return null;

	}



	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String populateXLS() throws Exception {

		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getLocaleProperty("loanDistributionReportList") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(
				FileUtil.createFileInputStreamToZipFile(getLocaleProperty("loanDistributionReportList"), fileMap, ".xls"));

		return "xls";
	}

	XSSFRow row, filterRowTitle, filterRow1, filterRow2, filterRow3;
	XSSFRow titleRow;
	int colCount, rowCount, titleRow1 = 4, titleRow2 = 6;
	Cell cell;
	Integer cellIndex;

	@Override
	public InputStream getExportDataStream(String exportType) throws IOException {
		InputStream stream;
		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		setsDate(IExporter.EXPORT_DAILY.equalsIgnoreCase(exportType) ? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));
		boolean flag = true;
		DateFormat filterDateFormat = new SimpleDateFormat(FILTERDATE);

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("exportLoanDistributionTitle"));
		HSSFPatriarch drawing = sheet.createDrawingPatriarch();

		HSSFCellStyle style1 = wb.createCellStyle();
		HSSFCellStyle style2 = wb.createCellStyle();
		HSSFCellStyle style3 = wb.createCellStyle();
		HSSFCellStyle style4 = wb.createCellStyle();
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

		int rowNum = 3;
		int colNum = 0;

		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, titleRow1, titleRow2));

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(2);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("Loan Distribution Report")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);

		setFilter(new LoanDistribution());
		
		if (!StringUtil.isEmpty(farmerId)) {
		filter.setFarmer(farmerService.findFarmerByFarmerId(farmerId.trim()));
			
		}
		if (!StringUtil.isEmpty(villageName)) {
			filter.setVillage(locationService.findVillageByCode(villageName.trim()));
		}
		
		if (!StringUtil.isEmpty(groupId)) {
			filter.setGroup(farmerService.findFarmerByFarmerId(groupId.trim()));
		}
		if(getVendorsList().size() == 1){
			getVendorsList().entrySet().stream().forEach(i ->{
			Vendor vendor=new Vendor();
				filter.setVendor(vendor);
				filter.getVendor().setVendorId(i.getKey());
			});
		}
		filter.setBranchId(getBranchId());
		super.filter = this.filter;
		Map data = readData();

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

			cell = filterRow2.createCell(2);
			if (geteDate() != null) {
				cell.setCellValue(new HSSFRichTextString(filterDateFormat.format(geteDate())));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(farmerId)) {
				filterRow1 = sheet.createRow(rowNum++);
				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmerId")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				Farmer farmer = farmerService.findFarmerByFarmerId(farmerId.trim());
				cell = filterRow1.createCell(2);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(farmer) ?(!StringUtil.isEmpty(farmer.getSurName()) ? farmer.getFirstName()+" "+farmer.getSurName(): farmer.getFirstName()) : "NA" ));
				
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			
			if (!StringUtil.isEmpty(groupId)) {
				filterRow1 = sheet.createRow(rowNum++);
				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("groupId")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				Farmer farmer = farmerService.findFarmerByFarmerId(groupId.trim());
				cell = filterRow1.createCell(2);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(farmer) && !StringUtil.isEmpty(farmer.getLastName()) ? farmer.getFirstName()+" "+farmer.getLastName() :farmer.getFirstName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			if (!StringUtil.isEmpty(villageName)) {
				filterRow1 = sheet.createRow(rowNum++);
				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("villageName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				Village village = locationService.findVillageByCode(villageName.trim());

				cell = filterRow1.createCell(2);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(village) ? village.getName() : ""));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			filterRow4 = sheet.createRow(rowNum++);
			filterRow5 = sheet.createRow(rowNum++);
		}
		rowNum++;

		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		String mainGridColumnHeaders = getLocaleProperty("exportColumnLoanDistributionHeader");
		int mainGridIterator = 0;

		
		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {

			if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
			} else if (cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());
			}

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

		List<LoanDistribution> mainGridRows = (List<LoanDistribution>) data.get(ROWS);

		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;

		ESESystem preferences = preferncesService.findPrefernceById("1");
		DateFormat genDate = new SimpleDateFormat(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
		

		for (LoanDistribution loanDistribution : mainGridRows) {

			row = sheet.createRow(rowNum++);
			colNum = 0;

			
			
			
			if (!StringUtil.isEmpty(preferences)) {
				cell = row.createCell(colNum++);
				cell.setCellValue(!ObjectUtil.isEmpty(loanDistribution)
						? (!StringUtil.isEmpty(genDate.format(loanDistribution.getLoanDate()))
								? genDate.format(loanDistribution.getLoanDate()) : "")
						: "");
				
			}

			cell = row.createCell(colNum++);
			cell.setCellValue(!ObjectUtil.isEmpty(loanDistribution.getAgroTransaction()) && !ObjectUtil.isEmpty(loanDistribution.getAgroTransaction().getAccount())
					? (!StringUtil.isEmpty(loanDistribution.getAgroTransaction().getAccount().getLoanAccountNo()) ?loanDistribution.getAgroTransaction().getAccount().getLoanAccountNo() : "") : "");

			/*cell = row.createCell(colNum++);
			cell.setCellValue(!ObjectUtil.isEmpty(loanDistribution.getLoanTo()) ? getLocaleProperty("loanTo" + loanDistribution.getLoanTo()) : "");

			cell = row.createCell(colNum++);
			cell.setCellValue(!ObjectUtil.isEmpty(loanDistribution.getGroup()) ? loanDistribution.getGroup().getName() : "NA");*/
			
			cell = row.createCell(colNum++);
			cell.setCellValue(!ObjectUtil.isEmpty(loanDistribution.getVendor()) ? loanDistribution.getVendor().getVendorName() : "NA");
			

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(loanDistribution.getFarmer()) ? loanDistribution.getFarmer().getFirstName()+" "+loanDistribution.getFarmer().getLastName(): ""));


			/*cell = row.createCell(colNum++);
			cell.setCellValue(!ObjectUtil.isEmpty(loanDistribution.getVillage()) ? loanDistribution.getVillage().getName() : "");*/
			
			/*cell = row.createCell(colNum++);
			cell.setCellValue(!ObjectUtil.isEmpty(loanDistribution.getLoanCategory()) ? getLocaleProperty("loanCategory" + loanDistribution.getLoanCategory()) : "");*/
					

			Map<String, Object> details = getTotalLoanDistributionDetails(loanDistribution);
			
			cell = row.createCell(colNum++);
			style4.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(style4);
			cell.setCellValue(CurrencyUtil.getDecimalFormat((Double) details.get(QUANTITY), "##.00"));
			
			
			cell = row.createCell(colNum++);
			style4.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(style4);
			cell.setCellValue(CurrencyUtil.getDecimalFormat((Double) details.get(TOTAL_AMT), "###.00"));
			
			cell = row.createCell(colNum++);
			style4.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(style4);
			cell.setCellValue(CurrencyUtil.getDecimalFormat((Double) loanDistribution.getInterestPercentage(), "##.00"));
			
			cell = row.createCell(colNum++);
			style4.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(style4);
			cell.setCellValue(CurrencyUtil.getDecimalFormat((Double) loanDistribution.getInterestAmt(), "###.00"));
			
			cell = row.createCell(colNum++);
			style4.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(style4);
			cell.setCellValue(!ObjectUtil.isEmpty(loanDistribution.getLoanTenure()) ? loanDistribution.getLoanTenure() : 0);
			
			cell = row.createCell(colNum++);
			style4.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(style4);
			cell.setCellValue(CurrencyUtil.getDecimalFormat((Double) loanDistribution.getLoanTenureAmt(), "###.00"));
			
			cell = row.createCell(colNum++);
			style4.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(style4);
			cell.setCellValue(CurrencyUtil.getDecimalFormat((Double) loanDistribution.getTotalCostToFarmer(), "###.00"));
			
			cell = row.createCell(colNum++);
			style4.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(style4);
			cell.setCellValue(CurrencyUtil.getDecimalFormat((Double) loanDistribution.getMonthlyRepaymentAmt(), "###.00"));
			
			
			
			HSSFRow subGridRowHead = sheet.createRow(rowNum++);
			String sunGridcolumnHeaders = getLocaleProperty("exportColumnLoanDistributionSubHeader");
			int subGridIterator = 1;

			for (String cellHeader : sunGridcolumnHeaders.split("\\,")) {

				if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
					cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
				} else if (cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT)) {
					cellHeader = cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());
				}

				cell = subGridRowHead.createCell(subGridIterator);
				cell.setCellValue(new HSSFRichTextString(cellHeader));
				style3.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
				style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				cell.setCellStyle(style3);
				font3.setBoldweight((short) 10);
				font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				style3.setFont(font3);
				subGridIterator++;
			}

			for (LoanDistributionDetail loanDistributionDetail : loanDistribution.getLoanDistributionDetail()) {
				row = sheet.createRow(rowNum++);
				colNum = 1;

				if(!StringUtil.isEmpty(loanDistributionDetail.getProduct().getSubcategory())){
					cell = row.createCell(colNum++);
					cell.setCellValue(!StringUtil.isEmpty(loanDistributionDetail.getProduct().getSubcategory().getName()) ? loanDistributionDetail.getProduct().getSubcategory().getName() : "");
					
				}else{
					cell = row.createCell(colNum++);
					cell.setCellValue("NA");
				}
				
				if(!StringUtil.isEmpty(loanDistributionDetail.getProduct())){
					cell = row.createCell(colNum++);
					cell.setCellValue(!StringUtil.isEmpty(loanDistributionDetail.getProduct().getName()) ? loanDistributionDetail.getProduct().getName() : "");
					
				}else{
					cell = row.createCell(colNum++);
					cell.setCellValue("NA");
				}
				
				cell = row.createCell(colNum++);
				style4.setAlignment(CellStyle.ALIGN_RIGHT);
				cell.setCellStyle(style4);
				cell.setCellValue(CurrencyUtil.getDecimalFormat(loanDistributionDetail.getRatePerUnit(), "###.00"));
				
				cell = row.createCell(colNum++);
				style4.setAlignment(CellStyle.ALIGN_RIGHT);
				cell.setCellStyle(style4);
				cell.setCellValue(CurrencyUtil.getDecimalFormat(loanDistributionDetail.getQuantity(), "##.00"));
				
				cell = row.createCell(colNum++);
				style4.setAlignment(CellStyle.ALIGN_RIGHT);
				cell.setCellStyle(style4);
				cell.setCellValue(CurrencyUtil.getDecimalFormat(loanDistributionDetail.getAmount(), "###.00"));
				
				/*cell = row.createCell(colNum++);
				cell.setCellValue(CurrencyUtil.getDecimalFormat(loanDistributionDetail.getSubsidyInterest(), "##.00"));
				
				cell = row.createCell(colNum++);
				cell.setCellValue(CurrencyUtil.getDecimalFormat(loanDistributionDetail.getSubsidyAmt(), "#,###.00"));*/
				
				cell = row.createCell(colNum++);
				style4.setAlignment(CellStyle.ALIGN_RIGHT);
				cell.setCellStyle(style4);
				cell.setCellValue(CurrencyUtil.getDecimalFormat(loanDistributionDetail.getTotalAmt(), "###.00"));
				
				
			}

			// }

		}

/*		int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);*/
		//HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		// picture.resize();
/*		String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
				: request.getSession().getId();
		File.createTempFile("tmpDir" + id, null);
		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("loanDistributionReportList") + fileNameDateFormat.format(new Date()) + ".xls";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		wb.write(fileOut);
		File file = new File(makeDir + fileName);
		stream = new FileInputStream(file);
		fileOut.close();
		return stream;*/
		
		
		Asset existingAssetLogin = clientService.findAssetsByAssetCode(Asset.APP_LOGO);

		int pictureIdx = getPicIndex(wb,existingAssetLogin.getFile());
        HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 1,
                4);
        anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);

		String makeDir = FileUtil.storeXls(request.getSession().getId());
		String fileName = getText("farmerList") + fileNameDateFormat.format(new Date()) + ".xls";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		wb.write(fileOut);
		stream = new FileInputStream(new File(makeDir + fileName));
		fileOut.close();
		return stream;

	}
	public static int getPicIndex(Workbook wb, byte[] pic) throws IOException {
        int pictureIdx = wb.addPicture(pic, wb.PICTURE_TYPE_JPEG);
        return pictureIdx;
    }

	public void setVillageMap(Map<String, String> villageMap) {
		this.villageMap = villageMap;
	}
	public Map<String, String> getVendorsList() {

		Map<String, String> vendorListMap = new LinkedHashMap<String, String>();
		List<Vendor> vendorsList = productDistributionService.listVendor();
		if (!ObjectUtil.isListEmpty(vendorsList)) {
			for (Vendor vendor : vendorsList) {
				vendorListMap.put(vendor.getVendorId(), vendor.getVendorName());
			}
		}
		return vendorListMap;

	}
	
}