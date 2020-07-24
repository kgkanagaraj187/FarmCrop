package com.sourcetrace.esesw.view.report.agro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import com.sourcetrace.eses.dao.IReportDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.entity.WarehouseProduct;
import com.sourcetrace.eses.order.entity.txn.AgentBalanceReport;
import com.sourcetrace.eses.order.entity.txn.Contract;
import com.sourcetrace.eses.order.entity.txn.PaymentMode;
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
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.profile.Product;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

@SuppressWarnings({ "unchecked", "serial" })
public class FieldStaffStockReportAction extends BaseReportAction {
	private ILocationService locationService;
	private IProductService productService;
	private IFarmerService farmerService;
	private IAgentService agentService;
	private WarehouseProduct filter;
	private String fstaff;
	private String product;
	private String branchIdParma;
	private String seasonCode;
	private IPreferencesService preferncesService;
	private String enableBatchNo;
	private String gridIdentity;
	@Autowired
	private IProductDistributionService productDistributionService;
	private String xlsFileName;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private String exportLimit;
	private InputStream fileInputStream;

	public String list() {

		setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));
		return LIST;
	}

	public String detail() throws Exception {

		setFilter(new WarehouseProduct());
		/*
		 * if(!getCurrentTenantId().equalsIgnoreCase("lalteer")){
		 * filter.setSeasonCode(getCurrentSeasonsCode()); }
		 */
		if (!StringUtil.isEmpty(fstaff)) {
			filter.setAgent(new Agent());
			filter.getAgent().setId(Long.parseLong(fstaff));
		}
		if (!StringUtil.isEmpty(product)) {
			filter.setProduct(new Product());
			filter.getProduct().setCode(product);
		}
		if (!StringUtil.isEmpty(branchIdParma)) {
			filter.setBranchId(branchIdParma);
		}
		if (!StringUtil.isEmpty(seasonCode)) {
			filter.setSeasonCode(seasonCode);
		}

		if (!StringUtil.isEmpty(subBranchIdParma) && !subBranchIdParma.equals("0")) {
			filter.setBranchId(subBranchIdParma);
		}

		super.filter = this.filter;
		setGridIdentity(IReportDAO.MAIN_GRID);
		Map data = readData("fieldStaffStockReport");
		setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));
		return sendJSONResponse(data);
	}

	public String subGridDetail() throws Exception {

		setGridIdentity(IReportDAO.SUB_GRID);
		if (!StringUtil.isEmpty(id)) {
			this.filter = productService.findByWarehouseProductId(Long.valueOf(id));
		}
		if (!ObjectUtil.isEmpty(filter)) {
			super.filter = this.filter;
			Map data = readData("fieldStaffSubStock");
			return sendJSONResponse(data);
		}
		return null;
	}

	public JSONObject toJSON(Object obj) {

		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		Object[] objArr = (Object[]) obj;
		if (IReportDAO.MAIN_GRID.equalsIgnoreCase(getGridIdentity())) {
			/*
			 * if (StringUtil.isEmpty(branchIdValue)) { if(objArr[8]==null){
			 * rows.add(branchesMap.get(objArr[8]));// B }else{
			 * rows.add(branchesMap.get(objArr[8].toString()));// Branch } }
			 */

			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(objArr[8])))
							? getBranchesMap().get(getParentBranchMap().get(objArr[8]))
							: getBranchesMap().get(objArr[8]));
				}
				rows.add(getBranchesMap().get(objArr[8]));
			} else {
				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(branchesMap.get(String.valueOf(objArr[8])));
				}
			}

			rows.add(objArr[0].toString());// Fs Prof Id
			rows.add(objArr[4].toString());// Fs name
			rows.add(objArr[5].toString());// Subcategory Name
			// rows.add(objArr[6].toString());// Product Code
			rows.add(objArr[7].toString());// Product Name
			rows.add(objArr[11].toString());// Unit
			
			/*
			 * if (getEnableBatchNo().equalsIgnoreCase("1"))
			 * rows.add(objArr[9].toString());// batch no
			 */ rows.add(CurrencyUtil.getDecimalFormat(Double.parseDouble(objArr[12].toString()), "##.00"));// Stock
			/*
			 * HarvestSeason
			 * season=farmerService.findSeasonNameByCode(seasonCode);
			 * if(!StringUtil.isEmpty(season)&& season!=null){
			 * rows.add(season.getName());// season code }
			 */
			 if(getCurrentTenantId().equalsIgnoreCase("pratibha"))
				 rows.add(String.valueOf(Double.parseDouble(objArr[10].toString())*Double.parseDouble(objArr[11].toString())));
			if (!StringUtil.isEmpty(objArr[2])) {
				HarvestSeason season = farmerService.findSeasonNameByCode(objArr[2].toString());
				rows.add(season == null ? "" : season.getName());
			} else {
				rows.add("NA");
			}

			jsonObject.put("id", objArr[3]);// Id
			jsonObject.put("cell", rows);
		} else if (IReportDAO.SUB_GRID.equalsIgnoreCase(getGridIdentity())) {

			rows.add(objArr[2].toString());
			rows.add(objArr[4].toString());

			jsonObject.put("id", objArr[3].toString());
			jsonObject.put("cell", rows);
		}
		return jsonObject;
	}

	/*	public Map<String, String> getFieldStaffList() {

		Map<String, String> agentMap = new LinkedHashMap<>();
		List<Object[]> agentList = agentService.listAgentNameProfIdAndIdByBranch(getBranchId());
		if (!ObjectUtil.isListEmpty(agentList)) {
			agentMap = agentList.stream().collect(Collectors.toMap(obj -> String.valueOf(obj[1]),
					obj -> (String.valueOf(obj[2]))));
		}
		return agentMap;
	}

	public Map<String, String> getProductList() {

		Map<String, String> returnMap = new LinkedHashMap<String, String>();

		List<Object[]> productList = productService.listWarehouseStockProducts();
		if (!ObjectUtil.isListEmpty(productList)) {
			returnMap = productList.stream().collect(Collectors.toMap(obj -> String.valueOf(obj[0]),
					obj -> (String.valueOf(obj[1]) )));
		}
		return returnMap;
	}

	public Map<String, String> getSeasonList() {

		Map<String, String> seasonMap = new LinkedHashMap<String, String>();
		List<Object[]> seasonList = farmerService.listSeasonCodeAndName();
		seasonMap = seasonList.stream().collect(
				Collectors.toMap(obj -> (String.valueOf(obj[0])), obj -> String.valueOf(obj[1]) ));
		return seasonMap;
	}*/
	
	public void populateFieldStaffList(){
		  JSONArray fieldStaffArr = new JSONArray();
			List<Object[]> agentList = agentService.listAgentNameProfIdAndIdByBranch(getBranchId());
		  if (!ObjectUtil.isEmpty(agentList)) {
			  agentList.forEach(obj -> {
				  fieldStaffArr.add(getJSONObject(obj[1].toString(), obj[2].toString()));
		   });
		  }
		  sendAjaxResponse(fieldStaffArr);
		 }
	
	public void populateProductList(){
		  JSONArray productArr = new JSONArray();
		  List<Object[]> productList = productService.listWarehouseStockProducts();
		  if (!ObjectUtil.isEmpty(productList)) {
			  productList.forEach(obj -> {
			  productArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
		   });
		  }
		  sendAjaxResponse(productArr);
		 }
	
	public void populateSeasonList(){
		  JSONArray seasonArr = new JSONArray();
		  List<Object[]> seasonList = farmerService.listSeasonCodeAndName();
		  if (!ObjectUtil.isEmpty(seasonList)) {
		   seasonList.forEach(obj -> {
		    seasonArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
		   });
		  }
		  sendAjaxResponse(seasonArr);
		 }
	
	
	public String getFstaff() {

		return fstaff;
	}

	public void setFstaff(String fstaff) {

		this.fstaff = fstaff;
	}

	public String getProduct() {

		return product;
	}

	public String getSeasonCode() {

		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {

		this.seasonCode = seasonCode;
	}

	public void setProduct(String product) {

		this.product = product;
	}

	public WarehouseProduct getFilter() {

		return filter;
	}

	public void setFilter(WarehouseProduct filter) {

		this.filter = filter;
	}

	public ILocationService getLocationService() {

		return locationService;
	}

	public void setLocationService(ILocationService locationService) {

		this.locationService = locationService;
	}

	public IProductService getProductService() {

		return productService;
	}

	public void setProductService(IProductService productService) {

		this.productService = productService;
	}

	public IAgentService getAgentService() {

		return agentService;
	}

	public void setAgentService(IAgentService agentService) {

		this.agentService = agentService;
	}

	public IFarmerService getFarmerService() {

		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {

		this.farmerService = farmerService;
	}

	public String getBranchIdParma() {

		return branchIdParma;
	}

	public void setBranchIdParma(String branchIdParma) {

		this.branchIdParma = branchIdParma;
	}

	public IPreferencesService getPreferncesService() {

		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {

		this.preferncesService = preferncesService;
	}

	public String getEnableBatchNo() {

		return enableBatchNo;
	}

	public void setEnableBatchNo(String enableBatchNo) {

		this.enableBatchNo = enableBatchNo;
	}

	public String getGridIdentity() {

		return gridIdentity;
	}

	public void setGridIdentity(String gridIdentity) {

		this.gridIdentity = gridIdentity;
	}

	public String populateXLS() throws Exception {

		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getText("fieldStaffStockReportList") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(
				FileUtil.createFileInputStreamToZipFile(getText("fieldStaffStockReportList"), fileMap, ".xls"));
		return "xls";
	}

	@SuppressWarnings("unchecked")
	public InputStream getExportDataStream(String exportType) throws IOException {
		
		Long serialNumber = 1L;
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		setsDate(IExporter.EXPORT_DAILY.equalsIgnoreCase(exportType) ? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));
		boolean flag = true;
		DateFormat filterDateFormat = new SimpleDateFormat(getGeneralDateFormat());
		int balType;

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("exportFieldStaffTitle"));
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
		int imgCol2 = 0;
		int titleRow1 = 2;
		int titleRow2 = 5;

		int rowNum = 2;
		int colNum = 0;
		
		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap(); // build branch map to get branch name form branch id.
		
		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, titleRow1, titleRow2));

		
		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(2);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("exportFieldStaffTitle")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);
		if (isMailExport()) {
			rowNum++;
			rowNum++;
			if (!StringUtil.isEmpty(fstaff) || !StringUtil.isEmpty(product) || !StringUtil.isEmpty(seasonCode)){
			filterRowTitle = sheet.createRow(rowNum++);
			cell = filterRowTitle.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("filter")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);
			}
			filterRow1 = sheet.createRow(rowNum++);

			if (!StringUtil.isEmpty(fstaff)) {
				filterRow4 = sheet.createRow(rowNum++);
				cell = filterRow4.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("agent")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
				cell = filterRow4.createCell(2);
				Agent agent = agentService.findAgent(Long.valueOf(fstaff));
				cell.setCellValue(new HSSFRichTextString(agent.getProfileId() + "-"
						+ ((!ObjectUtil.isEmpty(agent)) ? agent.getPersonalInfo().getFirstName() : " ")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(seasonCode)) {
				filterRow4 = sheet.createRow(rowNum++);
				cell = filterRow4.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("seasonCode")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
				cell = filterRow4.createCell(2);
				HarvestSeason season = farmerService.findSeasonNameByCode(seasonCode);
				cell.setCellValue(new HSSFRichTextString(season.getName() + "-" + season.getCode()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(branchIdParma)) {

				filterRow5 = sheet.createRow(rowNum++);
				cell = filterRow5.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("app.branch")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow5.createCell(2);
				cell.setCellValue(new HSSFRichTextString((getBranchesMap().get(branchIdParma))));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(product)) {
				filterRow4 = sheet.createRow(rowNum++);
				cell = filterRow4.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("product")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
				cell = filterRow4.createCell(2);
				Product p = productService.findProductByCode(product);
				cell.setCellValue(new HSSFRichTextString(p.getName() + "-" + p.getCode()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

		//	filterRow4 = sheet.createRow(rowNum++);
		// = sheet.createRow(rowNum++);
		}
		rowNum++;

		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		String mainGridColumnHeaders = getLocaleProperty("exportFieldStaffStockMainColumnHeader");
		
		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("ExportFieldStaffStockColumnHeadingBranch");
			} else if (!StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("ExportFieldStaffStockColumnHeading");
			}
		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("exportMobileUserStockReportColumnHeaderBranch");
			} else {
				mainGridColumnHeaders = getLocaleProperty("exportMobileUserStockReportColumnHeader");
			}
		}
		
		if(getCurrentTenantId().equalsIgnoreCase("pratibha")){
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("MobileUserStockReportExportHeader");
			} 
			else if(getBranchId().equalsIgnoreCase("organic")){
				mainGridColumnHeaders = getLocaleProperty("OrganicMobileUserStockReportExportHeader");
			}else if(getBranchId().equalsIgnoreCase("bci")){
				mainGridColumnHeaders = getLocaleProperty("BCIMobileUserStockReportExportHeader");
			}
			
		}
		
		int mainGridIterator = 0;

		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {
			//branchIdValue = getBranchId();
			if (StringUtil.isEmpty(branchIdValue)) { // Add branch header to
				// export sheet if main
				// branch logged in.

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
		setFilter(new WarehouseProduct());
		/*
		 * if(!getCurrentTenantId().equalsIgnoreCase("lalteer")){
		 * filter.setSeasonCode(getCurrentSeasonsCode()); }
		 */
		if (!StringUtil.isEmpty(fstaff)) {
			filter.setAgent(new Agent());
			filter.getAgent().setId(Long.parseLong(fstaff));
		}
		if (!StringUtil.isEmpty(product)) {
			filter.setProduct(new Product());
			filter.getProduct().setCode(product);
		}
		if (!StringUtil.isEmpty(branchIdParma)) {
			filter.setBranchId(branchIdParma);
		}
		if (!StringUtil.isEmpty(seasonCode)) {
			filter.setSeasonCode(seasonCode);
		}

		if (!StringUtil.isEmpty(subBranchIdParma) && !subBranchIdParma.equals("0")) {
			filter.setBranchId(subBranchIdParma);
		}

		super.filter = this.filter;
		Map data = isMailExport() ? readData("fieldStaffStockReport") : readExportData("fieldStaffStockReport");
		setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));
		List<WarehouseProduct> mainGridRows = (List<WarehouseProduct>) data.get(ROWS);
		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;
		for (Object obj : mainGridRows) {
			Object[] objArr = (Object[]) obj;
			String balance = null;
			String profileId = objArr[0].toString();
			String profType = objArr[1].toString();

			/*
			 * if (!StringUtil.isEmpty(filter.getAgentId())) {
			 * 
			 * cell = filterRow5.createCell(1); cell.setCellValue(new
			 * HSSFRichTextString(getLocaleProperty("agent")));
			 * filterFont.setBoldweight((short) 12);
			 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
			 * 
			 * cell = filterRow5.createCell(2); cell.setCellValue(new
			 * HSSFRichTextString(String.valueOf(object[2]) + " - " +
			 * String.valueOf(object[0]))); filterFont.setBoldweight((short)
			 * 12); filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
			 * 
			 * if (StringUtil.isEmpty(filter.getAccountNumber())) {
			 * sheet.shiftRows(filterRow4.getRowNum() + 1,
			 * filterRow5.getRowNum() + 1, -1); } }
			 */

			row = sheet.createRow(rowNum++);
			colNum = 0;

			
			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

				if (StringUtil.isEmpty(branchIdValue)) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(objArr[8])))
									? getBranchesMap().get(getParentBranchMap().get(objArr[8]))
									: getBranchesMap().get(objArr[8])));
				}
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(objArr[8])));
			} else {
				

				cell = row.createCell(colNum++);
				style4.setAlignment(CellStyle.ALIGN_CENTER);
				cell.setCellStyle(style4);
				cell.setCellValue(serialNumber);
				serialNumber = serialNumber + 1;
				
				
				if (StringUtil.isEmpty(branchIdValue)) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(objArr[8])));
				}
			}

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(String.valueOf(objArr[0])));

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(String.valueOf(objArr[4])));

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(String.valueOf(objArr[5])));

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(String.valueOf(objArr[7])));
			
			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(String.valueOf(objArr[11])));
			cell = row.createCell(colNum++);
			
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.valueOf(objArr[12].toString()));
			style3.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			cell.setCellStyle(style3);
			
			
			/*cell = row.createCell(colNum++);
			HSSFCellStyle style4 = wb.createCellStyle();
			if(StringUtil.isEmpty(globalReport[4])){
				cell.setCellValue("");	
			}else{
				style4.setAlignment(alignStyle.ALIGN_RIGHT);
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				cell.setCellValue(Double.valueOf(globalReport[4].toString()));
			}*/

			if(getCurrentTenantId().equalsIgnoreCase("pratibha")){
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(String.valueOf(Double.parseDouble(objArr[11].toString())*Double.parseDouble(objArr[10].toString()))));
			}
			cell = row.createCell(colNum++);
			HarvestSeason season = objArr[2]!=null && !ObjectUtil.isEmpty(objArr[2])?farmerService.findSeasonNameByCode(objArr[2].toString()):null;
			cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(season) ? season.getName() : "NA"));

			/*HSSFRow subGridRowHead = sheet.createRow(rowNum++);
			String sunGridcolumnHeaders = getLocaleProperty("exportSubColumnHeader");
			int subGridIterator = 1;

			for (String cellHeader : sunGridcolumnHeaders.split("\\,")) {

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

			setGridIdentity(IReportDAO.SUB_GRID);

			if (!StringUtil.isEmpty((objArr[3]).toString())) {
				this.filter = productService.findByWarehouseProductId(Long.valueOf((objArr[3]).toString()));
			}
			super.filter = this.filter;

			Map innerData = readData("fieldStaffSubStock");
  
			List<WarehouseProduct> innerGridRows = (List<WarehouseProduct>) innerData.get(ROWS);
			for (Object object : innerGridRows) {
				Object[] objAr = (Object[]) object;
				row = sheet.createRow(rowNum++);
				colNum = 1;
				String subGridbalance = null;

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(objAr[2].toString()));

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(objAr[4].toString()));
			}
			row = sheet.createRow(rowNum++);	*/
		}
		/*for (int i = 0; i <= colNum; i++) {
			sheet.autoSizeColumn(i);
		}*/
	
		/*int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(400, 10, 655, 200, (short) 0, 0, (short) 0, 0);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		picture.resize();
		String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
				: request.getSession().getId();
		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("fieldStaffStockReportList") + fileNameDateFormat.format(new Date()) + ".xls";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		wb.write(fileOut);
		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		fileOut.close();

		return stream;

	}*/
	
	int pictureIdx = getPicIndex(wb);
	HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
	anchor.setAnchorType(1);
	HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
	String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
			: request.getSession().getId();
	String makeDir = FileUtil.storeXls(id);
	String fileName = getLocaleProperty("globalReportList") + fileNameDateFormat.format(new Date()) + ".xls";
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

	public String getExportLimit() {
		return exportLimit;
	}

	public void setExportLimit(String exportLimit) {
		this.exportLimit = exportLimit;
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

}
