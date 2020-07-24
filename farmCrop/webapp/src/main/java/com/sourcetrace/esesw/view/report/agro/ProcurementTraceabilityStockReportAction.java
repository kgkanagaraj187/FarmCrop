/*
 * ProcurementStockReportAction.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view.report.agro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.ese.entity.traceability.ProcurementTraceability;
import com.ese.entity.traceability.ProcurementTraceabilityStock;
import com.ese.entity.traceability.ProcurementTraceabilityStockDetails;
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
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.profile.GradeMaster;
import com.sourcetrace.eses.order.entity.txn.CityWarehouse;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.profile.Product;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Country;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.entity.profile.WarehousePayment;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

public class ProcurementTraceabilityStockReportAction extends BaseReportAction implements IExporter {

	private static final long serialVersionUID = 1L;
	private Map<String, String> fields = new HashMap<>();
	private ProcurementTraceabilityStockDetails filter;
	private String areaCode;
	private String gridIdentity;
	private ILocationService locationService;
	private IFarmerService farmerService;
	private IPreferencesService preferncesService;
	private IProductDistributionService productDistributionService;
	private IProductService productService;
	private Map<String, GradeMaster> gradeMasterMap;
	private Map<String, ProcurementGrade> procurememtGradeMap;
	private String xlsFileName;
	private InputStream fileInputStream;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private IClientService clientService;
	private String branchIdParma;
	private String exportLimit;
	private String farmerId;
	private String productId;
	private String coOperative;
	private String procurementProductId;
	private Map<String, CityWarehouse> cityWarehouseGradeMap;
	private String farmerName;
	private String product;
	private String warehouse;
	private String village;
	private String city;
	private String fpo;
	private String ics;
	private String selectedSeason;
	Map<String, String> farmerIdAndVillageMap = new LinkedHashMap<>();
	private String farmerVill;
	Map<String, String> gradeVillMap = new LinkedHashMap<>();
	private String gradeVill;
	/**
	 * @see com.sourcetrace.esesw.view.BaseReportAction#list()
	 */
	public String list() throws Exception {
		request.setAttribute(HEADING, getText(LIST));
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			setExportLimit(preferences.getPreferences().get(ESESystem.EXPORT_RECORD_LIMIT));
		}
		filter = new ProcurementTraceabilityStockDetails();
		ProcurementTraceabilityStock ps=new ProcurementTraceabilityStock();
		ps.setCoOperative(new Warehouse());
		ps.setProcurementProduct(new ProcurementProduct());
		filter.setProcurementTraceabilityStock(ps);
		
		return LIST;
	}

	/**
	 * Detail.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings("unchecked")
	public String data() throws Exception {
		if(!ObjectUtil.isEmpty(filter)){
			if (!ObjectUtil.isEmpty(this.filter.getProcurementTraceabilityStock())){
			if (!ObjectUtil.isEmpty(this.filter.getProcurementTraceabilityStock().getCoOperative())) {
				this.filter.getProcurementTraceabilityStock().getCoOperative().setId(this.filter.getProcurementTraceabilityStock().getCoOperative().getId());
			}
			if (!ObjectUtil.isEmpty(this.filter.getProcurementTraceabilityStock().getProcurementProduct())) {
				this.filter.getProcurementTraceabilityStock().getProcurementProduct().setId(this.filter.getProcurementTraceabilityStock().getProcurementProduct().getId());
			}
			if (!ObjectUtil.isEmpty(this.filter.getProcurementTraceabilityStock().getVillage())) {
				this.filter.getProcurementTraceabilityStock().getVillage().setId(this.filter.getProcurementTraceabilityStock().getVillage().getId());
			}
			if (!ObjectUtil.isEmpty(this.filter.getProcurementTraceabilityStock().getCity())) {
				this.filter.getProcurementTraceabilityStock().getCity().setId(this.filter.getProcurementTraceabilityStock().getCity().getId());
			}
			}
		}
		else{
			this.filter = new ProcurementTraceabilityStockDetails();
		}
		if (!StringUtil.isEmpty(branchIdParma)) {
			filter.setBranchId(branchIdParma);
		}
		/*ProcurementTraceabilityStock pts =new ProcurementTraceabilityStock();
		
		if (!StringUtil.isEmpty(coOperative)) {
			Warehouse w= new Warehouse();
			w.setId(Long.valueOf(coOperative));
			pts.setCoOperative(w);
			filter.setProcurementTraceabilityStock(pts);
		}
		
		if (!StringUtil.isEmpty(procurementProductId)) {
		  ProcurementProduct pp = new ProcurementProduct();
		  pp.setId(Long.valueOf(procurementProductId));
	      pts.setProcurementProduct(pp);
	      filter.setProcurementTraceabilityStock(pts);
		}*/
		selectedSeason=selectedSeason!=null && !StringUtil.isEmpty(selectedSeason)?selectedSeason:getCurrentSeasonsCode();
		if(selectedSeason!=null && !StringUtil.isEmpty(selectedSeason)){
			filter.getProcurementTraceabilityStock().setSeason(selectedSeason);
		}
		
		super.filter = this.filter;
		Map data =readData();

		return sendJSONResponse(data);
	}

	/**
	 * Sub grid detail.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	
	/**
	 * @see com.sourcetrace.esesw.view.BaseReportAction#toJSON(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {
		ProcurementTraceabilityStockDetails proTraceStock = (ProcurementTraceabilityStockDetails) obj;
		JSONObject jsonObject = new JSONObject();
		DecimalFormat formatter = new DecimalFormat("###.000");
		JSONArray rows = new JSONArray();
		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(proTraceStock.getBranchId())))
						? getBranchesMap().get(getParentBranchMap().get(proTraceStock.getBranchId()))
						: getBranchesMap().get(proTraceStock.getBranchId()));
			}
			rows.add(getBranchesMap().get(proTraceStock.getBranchId()));

		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(branchesMap.get(proTraceStock.getBranchId()));
			}
		}
		if(getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)){
		if(proTraceStock.getProcurementTraceabilityStock().getSeason()!=null && !StringUtil.isEmpty(proTraceStock.getProcurementTraceabilityStock().getSeason())){
			HarvestSeason hs=farmerService.findSeasonNameByCode(proTraceStock.getProcurementTraceabilityStock().getSeason());
				rows.add(hs!=null && !ObjectUtil.isEmpty(hs)?hs.getName():"");
			}
		else{
			rows.add("");
		}
		}
		rows.add(proTraceStock.getProcurementTraceabilityStock().getCoOperative().getName());
		if(getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)){
		FarmCatalogue cat = catalogueService.findCatalogueByCode(proTraceStock.getProcurementTraceabilityStock().getIcs());
		rows.add(!ObjectUtil.isEmpty(cat) ? cat.getName() : "");
		}
		if(!ObjectUtil.isEmpty(proTraceStock.getFarmer())){
			Farmer farmer=	farmerService.findFarmerById(Long.valueOf(proTraceStock.getFarmer().getId()));
			if(getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)){
			FarmCatalogue fpo= getCatlogueValueByCode(farmer.getFpo());
			rows.add(fpo.getName());
			}
			//rows.add(farmer.getCity().getName());
			//rows.add(farmer.getVillage().getName());
			rows.add((!StringUtil.isEmpty(proTraceStock.getProcurementTraceabilityStock()) 
					&&!StringUtil.isEmpty(proTraceStock.getProcurementTraceabilityStock().getVillage()) 
					&& !StringUtil.isEmpty(proTraceStock.getProcurementTraceabilityStock().getVillage().getCity()))
					?proTraceStock.getProcurementTraceabilityStock().getVillage().getCity().getName() :"");
			rows.add((!StringUtil.isEmpty(proTraceStock.getProcurementTraceabilityStock()) 
					&&!StringUtil.isEmpty(proTraceStock.getProcurementTraceabilityStock().getVillage()))
					?proTraceStock.getProcurementTraceabilityStock().getVillage().getName() :"");
		}
		//rows.add(proTraceStock.getDate().toString());
		ESESystem preferences = preferncesService.findPrefernceById("1");
		  if (!StringUtil.isEmpty(preferences)) {
		   DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
		   rows.add(!ObjectUtil.isEmpty(proTraceStock)
		     ? (!StringUtil.isEmpty(genDate
		       .format(proTraceStock.getDate()))
		         ? genDate.format(proTraceStock.getDate())
		         : "")
		     : "");
		  }
		rows.add(!ObjectUtil.isEmpty(proTraceStock.getFarmer()) ? proTraceStock.getFarmer().getName() : "");
		rows.add(proTraceStock.getProcurementTraceabilityStock().getProcurementProduct().getName());
		Object[]values=productService.findGradeNameAndVarietyByGradeCode(proTraceStock.getProcurementTraceabilityStock().getGrade());
		if(!ObjectUtil.isEmpty(values)){
		rows.add(!ObjectUtil.isEmpty(values[1])? values[1].toString():"");
		rows.add(!ObjectUtil.isEmpty(values[0])?values[0].toString():"");
	}
		else{
			rows.add("");
			rows.add("");
		}
		if(getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)){
		rows.add(proTraceStock.getTotalNumberOfBags());
		}
		rows.add(proTraceStock.getTotalstock());
		jsonObject.put("id", proTraceStock.getId());
		jsonObject.put("cell", rows);
		return jsonObject;
	}

	/**
	 * Gets the city list.
	 * 
	 * @return the city list
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getCityList() {

		Map<String, String> cityList = new LinkedHashMap<String, String>();

		List<Municipality> cities = locationService.listCity();
		if (!ObjectUtil.isEmpty(cities)) {
			for (Municipality city : cities) {
				cityList.put(city.getCode(), city.getName() + "-" + city.getCode());
			}
		}

		return cityList;
	}
	
	/**
	 * Gets the co operative list.
	 * 
	 * @return the co operative list
	 */
	/*
	 * public List<Warehouse> getCoOperativeList() {
	 * 
	 * return locationService.listOfCooperatives(); }
	 */

	/*
	 * public Map<String, String> getCoOperativeList() { Map<String, String>
	 * farmerMap = new LinkedHashMap<>(); List<Object[]> farmerList =
	 * locationService.listOfCooperativeByProcurement(); if
	 * (!ObjectUtil.isEmpty(farmerList)) {
	 * 
	 * farmerMap = farmerList.stream().collect(Collectors.toMap(obj ->
	 * (String.valueOf(obj[0])), obj -> (String.valueOf(obj[2]) + " - " +
	 * String.valueOf(obj[1]))));
	 * 
	 * } return farmerMap; }
	 * 
	 * public Map<String, String> getFarmersList() {
	 * 
	 * Map<String, String> farmerMap = new LinkedHashMap<>(); List<Object[]>
	 * farmerList = farmerService.listFarmerInfoByProcurement(); if
	 * (!ObjectUtil.isEmpty(farmerList)) {
	 * 
	 * farmerMap = farmerList.stream() .collect(Collectors.toMap(obj ->
	 * (String.valueOf(obj[0])), obj -> (String.valueOf(obj[1]))));
	 * 
	 * } return farmerMap;
	 * 
	 * 
	 * }
	 */

	/**
	 * Gets the grade master map.
	 * 
	 * @return the grade master map
	 */
	public Map<String, GradeMaster> getGradeMasterMap() {

		if (ObjectUtil.isEmpty(gradeMasterMap)) {
			gradeMasterMap = new LinkedHashMap<String, GradeMaster>();
			List<GradeMaster> gradeMasterList = productDistributionService.listGradeMaster();
			for (GradeMaster gradeMaster : gradeMasterList)
				gradeMasterMap.put(gradeMaster.getCode(), gradeMaster);
		}
		return gradeMasterMap;
	}

	/**
	 * Populate central warehouse.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void populateCentralWarehouse() throws Exception {

		StringBuffer sb = new StringBuffer();
		NumberFormat formatter = new DecimalFormat();
		List<Object[]> listCityWarehouse = productDistributionService.listCentralWarehouse();
		if (!ObjectUtil.isListEmpty(listCityWarehouse)) {
			ESESystem system = preferncesService.findPrefernceById(ESESystem.SYSTEM_SWITCH);
			String tareWeight = system.getPreferences().get(ESESystem.TARE_WEIGHT);
			Double totalNetWeight = 0.0;
			for (Object[] object : listCityWarehouse) {
				Double totalTareWeight = Double.parseDouble(object[1].toString()) * Double.parseDouble(tareWeight);
				totalNetWeight += Double.parseDouble(object[2].toString()) - totalTareWeight;
				GradeMaster grade = productDistributionService.findGradeByCode(object[0].toString());
				sb.append("<tr><td>" + grade.getName() + "</td><td>" + object[1] + "</td><td>"
						+ formatter.format(object[2]) + "</td></tr>");
			}
			sb.append("<tr></tr>");
			sb.append("<tr bgcolor='#E8FAE6'><td><b>" + getText("netWeight") + "</td><td><b>-</td><td><b>"
					+ formatter.format(totalNetWeight) + "</td></tr>");
		}
		response.getWriter().print(sb);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");

	}

	/**
	 * Sets the filter.
	 * 
	 * @param filter
	 *            the new filter
	 */
	

	/**
	 * Sets the area code.
	 * 
	 * @param areaCode
	 *            the new area code
	 */
	public void setAreaCode(String areaCode) {

		this.areaCode = areaCode;
	}

	/**
	 * Gets the area code.
	 * 
	 * @return the area code
	 */
	public String getAreaCode() {

		return areaCode;
	}

	/**
	 * Gets the grid identity.
	 * 
	 * @return the grid identity
	 */
	public String getGridIdentity() {

		return gridIdentity;
	}

	/**
	 * Sets the grid identity.
	 * 
	 * @param gridIdentity
	 *            the new grid identity
	 */
	public void setGridIdentity(String gridIdentity) {

		this.gridIdentity = gridIdentity;
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
	 * Gets the location service.
	 * 
	 * @return the location service
	 */
	public ILocationService getLocationService() {

		return locationService;
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
	 * Gets the product distribution service.
	 * 
	 * @return the product distribution service
	 */
	public IProductDistributionService getProductDistributionService() {

		return productDistributionService;
	}

	/**
	 * Sets the product distribution service.
	 * 
	 * @param productDistributionService
	 *            the new product distribution service
	 */
	public void setProductDistributionService(IProductDistributionService productDistributionService) {

		this.productDistributionService = productDistributionService;
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
	 * Gets the file input stream.
	 * 
	 * @return the file input stream
	 */
	public InputStream getFileInputStream() {

		return fileInputStream;
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

	public String getBranchIdParma() {

		return branchIdParma;
	}

	public void setBranchIdParma(String branchIdParma) {

		this.branchIdParma = branchIdParma;
	}

	/**
	 * Populate xls.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String populateXLS() throws Exception {

		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getText("procurementTraceabilityStockReportList") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(
				FileUtil.createFileInputStreamToZipFile(getText("procurementTraceabilityStockReportList"), fileMap, ".xls"));
		return "xls";
	}

	@SuppressWarnings("unchecked")
	public InputStream getExportDataStream(String exportType) throws IOException {

		Long serialNumber = 0L;

		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		setsDate(IExporter.EXPORT_DAILY.equalsIgnoreCase(exportType) ? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));
		DecimalFormat formatter = new DecimalFormat("###.000");
		Double totalTareWt = 0.0;
		boolean flag = true;

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("exportXLSProcurmentTraceabilityStockTitle"));
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

		HSSFRow row, titleRow, filterRowTitle = null, filterRow = null,filterRow1,filterRow2,filterRow3,filterRow4,filterRow5,filterRow6,filterRow7,filterRow8;
		HSSFCell cell;

		int imgRow1 = 0;
		int imgRow2 = 4;
		int imgCol1 = 0;
		int imgCol2 = 1;
		int titleRow1 = 2;
		int titleRow2 = 5;

		int rowNum = 3;
		int colNum = 0;
		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap(); // build branch map to get branch name form branch id.

		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, titleRow1, titleRow2));

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(2);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("exportXLSProcurmentTraceabilityStockTitle")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);
		if (isMailExport()) {
			rowNum++;
			rowNum++;
			filterRowTitle = sheet.createRow(rowNum++);
			filterRow = sheet.createRow(rowNum++);
			cell = filterRowTitle.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("filter")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);
			
		}
		
	
										
		if(!ObjectUtil.isEmpty(filter)){
			if (!ObjectUtil.isEmpty(this.filter.getProcurementTraceabilityStock())){
			if (!ObjectUtil.isEmpty(this.filter.getProcurementTraceabilityStock().getCoOperative())) {
				this.filter.getProcurementTraceabilityStock().getCoOperative().setId(this.filter.getProcurementTraceabilityStock().getCoOperative().getId());
				Warehouse w=locationService.findProcurementWarehouseById(this.filter.getProcurementTraceabilityStock().getCoOperative().getId());
				if(!ObjectUtil.isEmpty(w)){
					filterRow1 = sheet.createRow(rowNum++);
					cell = filterRow1.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("co.name")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow1.createCell(2);
					
					cell.setCellValue(new HSSFRichTextString(w.getName()));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);
				}
			}
			if (!ObjectUtil.isEmpty(this.filter.getProcurementTraceabilityStock().getProcurementProduct())) {
				this.filter.getProcurementTraceabilityStock().getProcurementProduct().setId(this.filter.getProcurementTraceabilityStock().getProcurementProduct().getId());
				ProcurementProduct pp=productService.findProcurementProductById(this.filter.getProcurementTraceabilityStock().getProcurementProduct().getId());
				if(!ObjectUtil.isEmpty(pp)){
				filterRow2 = sheet.createRow(rowNum++);
				cell = filterRow2.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("pp.name")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow2.createCell(2);
				cell.setCellValue(new HSSFRichTextString(pp.getName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
				}
			}
			if(!StringUtil.isEmpty(this.filter.getProcurementTraceabilityStock().getIcs())){
				FarmCatalogue fc=catalogueService.findCatalogueByCode(this.filter.getProcurementTraceabilityStock().getIcs());
				if(!ObjectUtil.isEmpty(fc)){
				filterRow3 = sheet.createRow(rowNum++);
				cell = filterRow3.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("ics")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow3.createCell(2);
				cell.setCellValue(new HSSFRichTextString(fc.getName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
				}
			}
			}
			if(!StringUtil.isEmpty(this.filter.getFarmer())){
				if(!StringUtil.isEmpty(this.filter.getFarmer().getFarmerId()) && this.filter.getFarmer().getFarmerId()!=null){
				
				Object[] obj=farmerService.findFarmerAndFatherNameByFarmerId(this.filter.getFarmer().getFarmerId());
				if(!ObjectUtil.isEmpty(obj)){
				filterRow4 = sheet.createRow(rowNum++);
				cell = filterRow4.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmer")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow4.createCell(2);
				cell.setCellValue(new HSSFRichTextString(obj[2].toString()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
				}
			  }
			}
			if(!StringUtil.isEmpty(this.filter.getFarmer())){
				FarmCatalogue fc=catalogueService.findCatalogueByCode(this.filter.getFarmer().getFpo());
				if(!ObjectUtil.isEmpty(fc)){
				filterRow5 = sheet.createRow(rowNum++);
				cell = filterRow5.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("fpo")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow5.createCell(2);
				cell.setCellValue(new HSSFRichTextString(fc.getName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
				}
			}
			if (!ObjectUtil.isEmpty(this.filter.getProcurementTraceabilityStock().getVillage())) {
				Village v = locationService.findVillageById(Long.valueOf(this.filter.getProcurementTraceabilityStock().getVillage().getId()));
				if(!ObjectUtil.isEmpty(v)){
				filterRow6 = sheet.createRow(rowNum++);
				cell = filterRow6.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("village")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow6.createCell(2);
				
				cell.setCellValue(new HSSFRichTextString((ObjectUtil.isEmpty(v)|| v==null)?"": v.getName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			}
			
			if (!ObjectUtil.isEmpty(this.filter.getProcurementTraceabilityStock().getCity())) {
				Municipality catalogue = locationService.findMunicipalityById(Long.valueOf(this.filter.getProcurementTraceabilityStock().getCity().getId()));
				if(!ObjectUtil.isEmpty(catalogue)){
				filterRow7 = sheet.createRow(rowNum++);
				cell = filterRow7.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("city")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow7.createCell(2);
				
				cell.setCellValue(new HSSFRichTextString((ObjectUtil.isEmpty(catalogue)||catalogue==null)?"":catalogue.getName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			}
			if(!getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)){
			selectedSeason=selectedSeason!=null && !StringUtil.isEmpty(selectedSeason)?selectedSeason:getCurrentSeasonsCode();
			if (!ObjectUtil.isEmpty(selectedSeason)) {
				HarvestSeason catalogue = farmerService.findSeasonNameByCode(selectedSeason);
				if(!ObjectUtil.isEmpty(catalogue)){
				filterRow8 = sheet.createRow(rowNum++);
				cell = filterRow8.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("season")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow8.createCell(2);
				
				cell.setCellValue(new HSSFRichTextString((ObjectUtil.isEmpty(catalogue)||catalogue==null)?"":catalogue.getName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			}
			}
			
		}
		else{
			this.filter = new ProcurementTraceabilityStockDetails();
		}
		if (!StringUtil.isEmpty(branchIdParma)) {
			filter.setBranchId(branchIdParma);
		}
	//	selectedSeason=selectedSeason!=null && !StringUtil.isEmpty(selectedSeason)?selectedSeason:getCurrentSeasonsCode();
		if(selectedSeason!=null && !StringUtil.isEmpty(selectedSeason)){
			filter.getProcurementTraceabilityStock().setSeason(selectedSeason);
		}
		super.filter = this.filter;
		Map data =readData();
		rowNum++;

		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		String mainGridColumnHeaders;
		if (StringUtil.isEmpty(branchIdValue)) {
			mainGridColumnHeaders = getLocaleProperty("ExportMainGridHeadingProcurmentTraceabilityStockBranch");

		} else {
			mainGridColumnHeaders = getLocaleProperty("ExportMainGridHeadingProcurmentTraceabilityStock");

		}
		int mainGridIterator = 0;

		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {
			if (StringUtil.isEmpty(branchIdValue)) { // Add branch header to
														// export sheet if main
														// branch logged in.
				cell = mainGridRowHead.createCell(mainGridIterator);
				cell.setCellValue(new HSSFRichTextString(cellHeader));
				style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
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
					style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
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

		List<ProcurementTraceabilityStockDetails> mainGridRows = (List<ProcurementTraceabilityStockDetails>) data.get(ROWS);
		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;

		ESESystem preferences = preferncesService.findPrefernceById("1");
		DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
		//6-Village,14-Fpo,15-City
		List<Object[]> farmerIdVill = farmerService.listFarmerInfo();
		if (!ObjectUtil.isEmpty(farmerIdVill)) {
			farmerIdVill.stream().forEach(obj -> {
				if(!farmerIdAndVillageMap.containsKey(obj[0].toString())){
					farmerIdAndVillageMap.put(obj[0].toString(), obj[6].toString()+"-"+obj[14].toString()+"-"+obj[15].toString());
					}
			});
		}
		//2-Grade Name,5-Var Name
		List<Object[]> gradeVar = productService.listProcurementGradeInfo();
		if (!ObjectUtil.isEmpty(gradeVar)) {
			gradeVar.stream().forEach(obj -> {
				if(!gradeVillMap.containsKey(obj[1].toString())){
					gradeVillMap.put(obj[1].toString(), obj[2].toString()+"-"+obj[5].toString());
					}
			});
		}
		
		for (ProcurementTraceabilityStockDetails proTraceStock : mainGridRows) {
			 row = sheet.createRow(rowNum++); 
			 colNum = 0;
			
			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

				if (StringUtil.isEmpty(branchIdValue)) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new
							  HSSFRichTextString(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(proTraceStock.getBranchId())))
							? getBranchesMap().get(getParentBranchMap().get(proTraceStock.getBranchId()))
							: getBranchesMap().get(proTraceStock.getBranchId())));
					
				}
				cell = row.createCell(colNum++);
				cell.setCellValue(new
						  HSSFRichTextString(getBranchesMap().get(proTraceStock.getBranchId())));

			} else {
				if (StringUtil.isEmpty(branchIdValue)) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new
							  HSSFRichTextString(branchesMap.get(proTraceStock.getBranchId())));
				}
			}
			if(getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)){
			if (!ObjectUtil.isEmpty(selectedSeason)) {
			HarvestSeason catalogue = farmerService.findSeasonNameByCode(selectedSeason);
            cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString((ObjectUtil.isEmpty(catalogue)||catalogue==null)?"":catalogue.getName()));
			}
			}
			cell = row.createCell(colNum++);
			cell.setCellValue(new
					  HSSFRichTextString(proTraceStock.getProcurementTraceabilityStock().getCoOperative().getName()));
			//FarmCatalogue cat = catalogueService.findCatalogueByCode(proTraceStock.getProcurementTraceabilityStock().getIcs());
			if(getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)){
			cell = row.createCell(colNum++);
			cell.setCellValue(new
					  HSSFRichTextString(getCatlogueValueByCode(proTraceStock.getProcurementTraceabilityStock().getIcs()).getName()));
			}
			cell = row.createCell(colNum++);
			if(!ObjectUtil.isEmpty(proTraceStock.getFarmer())){
				//Farmer farmer=	farmerService.findFarmerById(Long.valueOf(proTraceStock.getFarmer().getId()));
				//FarmCatalogue fpo= getCatlogueValueByCode(farmer.getFpo());
				 if(farmerIdAndVillageMap.containsKey(String.valueOf(proTraceStock.getFarmer().getId())))
					 farmerVill= farmerIdAndVillageMap.get(String.valueOf(proTraceStock.getFarmer().getId()));
				 if(getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)){
				 cell.setCellValue(new
						  HSSFRichTextString(!StringUtil.isEmpty(farmerVill)?getCatlogueValueByCode(farmerVill.split("-")[1]).getName() :""));
				 
				cell = row.createCell(colNum++);
				 }
				cell.setCellValue(new
						  HSSFRichTextString((!StringUtil.isEmpty(farmerVill) ? farmerVill.split("-")[2]: "N/A")));
				cell = row.createCell(colNum++);
				cell.setCellValue(new
						  HSSFRichTextString((!StringUtil.isEmpty(farmerVill) ? farmerVill.split("-")[0]: "N/A")));
			}
			/*cell = row.createCell(colNum++);
			cell.setCellValue(new
					  HSSFRichTextString(!ObjectUtil.isEmpty(proTraceStock) ?proTraceStock.getDate().toString(): ""));
			*/
			

			if (!StringUtil.isEmpty(preferences)) {
				
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil
						.isEmpty(proTraceStock)
								? (!StringUtil.isEmpty(genDate
										.format(proTraceStock.getDate()))
														? genDate.format(
																proTraceStock.getDate())
														: "")
								: ""));
			}

			cell = row.createCell(colNum++);
			
			cell.setCellValue(new
					  HSSFRichTextString(proTraceStock.getFarmer().getName()));
			cell = row.createCell(colNum++);
			cell.setCellValue(new
					  HSSFRichTextString(proTraceStock.getProcurementTraceabilityStock().getProcurementProduct().getName()));
			//Object[]values=productService.findGradeNameAndVarietyByGradeCode(proTraceStock.getProcurementTraceabilityStock().getGrade());
			 if(gradeVillMap.containsKey(String.valueOf(proTraceStock.getProcurementTraceabilityStock().getGrade())))
				 gradeVill= gradeVillMap.get(String.valueOf(proTraceStock.getProcurementTraceabilityStock().getGrade()));
			if(!ObjectUtil.isEmpty(gradeVill)){
				cell = row.createCell(colNum++);
				cell.setCellValue(new
						  HSSFRichTextString(!ObjectUtil.isEmpty(gradeVill)?gradeVill.split("-")[1].toString():""));
				cell = row.createCell(colNum++);
				cell.setCellValue(new
						  HSSFRichTextString(!ObjectUtil.isEmpty(gradeVill)?gradeVill.split("-")[0].toString():""));
		}
			else{
				cell = row.createCell(colNum++);
				cell.setCellValue(new
						  HSSFRichTextString(""));
				cell = row.createCell(colNum++);
				cell.setCellValue(new
						  HSSFRichTextString(""));
			}
			if(getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)){
				cell = row.createCell(colNum++);
				cell.setCellValue(new
						  HSSFRichTextString(String.valueOf(proTraceStock.getTotalNumberOfBags())));
			}
				cell = row.createCell(colNum++);
				cell.setCellValue(new
						  HSSFRichTextString(String.valueOf(proTraceStock.getTotalstock())));
		
		}
		
		for (int i = 0; i <= colNum; i++) {
			sheet.autoSizeColumn(i);
		}

		int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		// picture.resize();
		InputStream stream = null;
		String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
				: request.getSession().getId();
		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("procurementTraceabilityStockReportList") + fileNameDateFormat.format(new Date()) + ".xls";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		wb.write(fileOut);
		stream = new FileInputStream(new File(makeDir + fileName));
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

		byte[] picData = null;
		picData = clientService.findLogoByCode(Asset.APP_LOGO);
		if (!ObjectUtil.isEmpty(picData)) {
			// picData=Base64.decode(picData);
		}
		// if (ObjectUtil.isEmpty(request)) {
		// picData = productDistributionService.findLogoImageById(1L);
		// } else {
		// String client = "basix-logo.png";
		// String logoPath =
		// request.getSession().getServletContext().getRealPath(
		// "/assets/client/demo/images/" + client);
		// File pic = new File(logoPath);
		// long length = pic.length();
		// picData = new byte[(int) length];
		//
		// FileInputStream picIn = new FileInputStream(pic);
		// picIn.read(picData);
		// }
		if (picData != null)
			index = wb.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
	}

	/**
	 * Load procurement grades.
	 */
	public void loadProcurementGrades() {

		if (ObjectUtil.isEmpty(procurememtGradeMap)) {
			procurememtGradeMap = new LinkedHashMap<String, ProcurementGrade>();
			List<ProcurementGrade> procurementGradeList = productDistributionService.listProcurementGrade();
			for (ProcurementGrade procurementGradeObj : procurementGradeList)
				procurememtGradeMap.put(procurementGradeObj.getCode(), procurementGradeObj);
		}

	}

	/*
	 * @Override public String populatePDF() throws Exception { List <String>
	 * filters = new ArrayList<String>(); List <Object> fields = new
	 * ArrayList<Object>(); List <List <Object>> entityObject = new
	 * ArrayList<List <Object>>(); DecimalFormat format = new
	 * DecimalFormat("0.00"); branchIdValue = getBranchId(); //set value for
	 * branch id. buildBranchMap(); // build branch map to get branch name form
	 * branch id. setMailExport(true);
	 * setsDate(IExporter.EXPORT_MANUAL.equalsIgnoreCase(IExporter.
	 * EXPORT_MANUAL) ? DateUtil .getDateWithoutTime(new Date()) :
	 * DateUtil.convertStringToDate(DateUtil.minusWeek( df.format(new Date()),
	 * 1, DateUtil.DATABASE_DATE_FORMAT), DateUtil.DATABASE_DATE_FORMAT));
	 * filters.add(getLocaleProperty("filter")); if(!StringUtil.isEmpty(filter)) {
	 * filters.add((getLocaleProperty("filter")+" : "+filter)); } String coOperative;
	 * if(!StringUtil.isEmpty(coOperative)) {
	 * filters.add((getLocaleProperty("coOperative")+" : "+coOperative)); }
	 * if(!StringUtil.isEmpty(village)) {
	 * filters.add(getLocaleProperty("village")+" : "+village); }
	 * if(!StringUtil.isEmpty(branchIdParma)) {
	 * filters.add(getLocaleProperty("branchIdParma")+" : "+branchIdParma); }
	 * //Beginning of setting filter values. if
	 * (!ObjectUtil.isEmpty(this.filter)){
	 * if(!ObjectUtil.isEmpty(this.filter.getProcurementProduct()) &&
	 * this.filter.getProcurementProduct().getId() == 0) {
	 * this.filter.setProcurementProduct(null); } } else { this.filter = new
	 * CityWarehouse(); this.filter.setCoOperative(new Warehouse()); }
	 * if(!StringUtil.isEmpty(branchIdParma)){
	 * filter.setBranchId(branchIdParma); } super.filter = this.filter; //End of
	 * setting filter values. Map data = isMailExport() ?
	 * readData("coOperativeAndProcurmentProduct") :
	 * readExportData("coOperativeAndProcurmentProduct"); //Map data =
	 * isMailExport() ? readData() : readExportData(); List<Object[]>
	 * mainGridRows = (List<Object[]>) data.get(ROWS); if
	 * (ObjectUtil.isListEmpty(mainGridRows)) return null; for (Object[] datas :
	 * mainGridRows) { fields = new ArrayList<Object>();
	 * if(StringUtil.isEmpty(branchIdValue)){ //Check if non-main branch has
	 * logged in as for farmer.
	 * fields.add(!StringUtil.isEmpty(branchesMap.get(datas[8])) ?
	 * (branchesMap.get(datas[8])) : ""); }
	 * fields.add(StringUtil.isEmpty(String.valueOf(datas[0]) .trim()) ?
	 * String.valueOf(datas[0]).trim(): "") ;
	 * fields.add(StringUtil.isEmpty(String.valueOf(datas[2]) .trim()) ?
	 * String.valueOf(datas[2]).trim(): "") ;
	 * fields.add(StringUtil.isEmpty(String.valueOf(datas[3]) .trim()) ?
	 * String.valueOf(datas[1]).trim(): "") ;
	 * fields.add(StringUtil.isEmpty(String.valueOf(datas[1]) .trim()) ?
	 * String.valueOf(datas[3]).trim(): "") ;
	 * fields.add(StringUtil.isEmpty(String.valueOf(datas[4]) .trim()) ?
	 * String.valueOf(datas[6]).trim(): "") ;
	 * fields.add(StringUtil.isEmpty(String.valueOf(datas[7]) .trim()) ?
	 * String.valueOf(datas[7]).trim(): "") ;
	 * fields.add(StringUtil.isEmpty(String.valueOf(datas[7]) .trim()) ?
	 * String.valueOf(datas[7]).trim(): "") ; CityWarehouse subFilter = new
	 * CityWarehouse(); Warehouse coOp = new Warehouse();
	 * coOp.setId(Long.valueOf(String.valueOf(datas[5])));
	 * subFilter.setCoOperative(coOp); ProcurementProduct pp = new
	 * ProcurementProduct(); pp.setId(Long.valueOf(String.valueOf(datas[6])));
	 * subFilter.setProcurementProduct(pp); super.filter = subFilter;
	 * loadProcurementGrades(); Map subData = isMailExport() ?
	 * readData("coOperativeProcurmentProductAndGrade") :
	 * readExportData("coOperativeProcurmentProductAndGrade"); List<Object[]>
	 * subGridRows = (List<Object[]>) subData.get(ROWS); for (Object[] subDatas
	 * : subGridRows) { DecimalFormat formatter = new DecimalFormat("###.000");
	 * Double totalTareWt = 0.0; String quality = (String) subDatas[2]; if
	 * (getGradeMasterMap().containsKey(quality)) quality =
	 * getGradeMasterMap().get(quality).getName(); String
	 * procurementProductGrade = (String) subDatas[2]; if
	 * (procurememtGradeMap.containsKey(procurementProductGrade)) {
	 * ProcurementGrade procurementGrade = procurememtGradeMap
	 * .get(procurementProductGrade); if (!ObjectUtil.isEmpty(procurementGrade))
	 * { procurementProductGrade = procurementGrade
	 * .getProcurementVariety().getName(); } else { procurementProductGrade =
	 * ""; } } fields.add(StringUtil.isEmpty(procurementProductGrade .trim()) ?
	 * procurementProductGrade.trim(): "") ; String procurementProductGradeName
	 * = (String) subDatas[2]; if
	 * (procurememtGradeMap.containsKey(procurementProductGradeName)) {
	 * ProcurementGrade procurementGrade = procurememtGradeMap
	 * .get(procurementProductGradeName); if
	 * (!ObjectUtil.isEmpty(procurementGrade)) { procurementProductGradeName =
	 * procurementGrade.getName(); } else { procurementProductGradeName = ""; }
	 * } fields.add(StringUtil.isEmpty(procurementProductGradeName .trim()) ?
	 * procurementProductGradeName.trim() : "") ;
	 * fields.add(StringUtil.isEmpty(String.valueOf( subDatas[5]).trim()) ?
	 * String.valueOf(subDatas[5]) .trim(): "") ;
	 * fields.add(formatter.format(Double
	 * .parseDouble(formatter.format(Double.parseDouble(String
	 * .valueOf(subDatas[6])))) - totalTareWt)); } entityObject.add(fields); }
	 * InputStream is = getPDFExportStream(entityObject,filters);
	 * setPdfFileName(getText("ListFile") + fileNameDateFormat.format(new
	 * Date())); Map<String, InputStream> fileMap = new HashMap<String,
	 * InputStream>(); fileMap.put(getPdfFileName(), is);
	 * setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText(
	 * "ListFile"), fileMap, ".pdf")); return "pdf"; }
	 */
	public IClientService getClientService() {

		return clientService;
	}

	public void setClientService(IClientService clientService) {

		this.clientService = clientService;
	}

	public String getExportLimit() {

		return exportLimit;
	}

	public void setExportLimit(String exportLimit) {

		this.exportLimit = exportLimit;
	}

	public String getFarmerId() {

		return farmerId;
	}

	public void setFarmerId(String farmerId) {

		this.farmerId = farmerId;
	}

	public String getProductId() {

		return productId;
	}

	public void setProductId(String productId) {

		this.productId = productId;
	}

	public Map<String, CityWarehouse> getCityWarehouseGradeMap() {

		return cityWarehouseGradeMap;
	}

	public void setCityWarehouseGradeMap(Map<String, CityWarehouse> cityWarehouseGradeMap) {

		this.cityWarehouseGradeMap = cityWarehouseGradeMap;
	}

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	public Map<String, String> getFields() {

		fields.put("1", getText("coOperative"));
		fields.put("2", getLocaleProperty("farmer"));

		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			fields.put("4", getText("app.branch"));
		} else if (StringUtil.isEmpty(getBranchId())) {
			fields.put("3", getText("app.branch"));
		}

		return fields;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}

	public Map<String, ProcurementGrade> getProcurememtGradeMap() {

		return procurememtGradeMap;
	}

	public void setProcurememtGradeMap(Map<String, ProcurementGrade> procurememtGradeMap) {

		this.procurememtGradeMap = procurememtGradeMap;
	}

	public void populateCooperativeList() {
		JSONArray coopArr = new JSONArray();
		List<Object[]> coopList = productService.listProductionCenterByTraceabilityStock();
		if (!ObjectUtil.isEmpty(coopList)) {
			coopList.forEach(obj -> {
				coopArr.add(getJSONObject(obj[0].toString(),obj[1].toString()));
			});
		}
		sendAjaxResponse(coopArr);
	}

	public void populateFarmerList() {
		JSONArray farmerArr = new JSONArray();
		List<Object[]> farmerList = productService.listFarmerByTraceabilityStock();
		if (!ObjectUtil.isEmpty(farmerList)) {
			farmerList.forEach(obj -> {
				farmerArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(farmerArr);
	}
	
	public void populateProductList(){
		JSONArray productArr = new JSONArray();
		List<Object[]> productList = productService.listOfProcurementProductByTraceabilityStock();
		if (!ObjectUtil.isEmpty(productList)) {
			productList.forEach(obj -> {
				productArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(productArr);
	}

	public IProductService getProductService() {
		return productService;
	}

	public void setProductService(IProductService productService) {
		this.productService = productService;
	}

	public ProcurementTraceabilityStockDetails getFilter() {
		return filter;
	}

	public void setFilter(ProcurementTraceabilityStockDetails filter) {
		this.filter = filter;
	}

	public String getCoOperative() {
		return coOperative;
	}

	public void setCoOperative(String coOperative) {
		this.coOperative = coOperative;
	}

	public String getProcurementProductId() {
		return procurementProductId;
	}

	public void setProcurementProductId(String procurementProductId) {
		this.procurementProductId = procurementProductId;
	}

	public void populateICSList(){
		JSONArray icsArr = new JSONArray();
		List<Object[]> icsList = productService.listOfICSFromTraceabilityStock();
		if (!ObjectUtil.isEmpty(icsList)) {
			icsList.stream().filter(obj->!ObjectUtil.isEmpty(obj[1])).forEach(obj -> {
				icsArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(icsArr);
	}
	public void populateVillageList(){
		JSONArray villageArr = new JSONArray();
		List<Object[]> villageList=productService.listOfVillageFromTraceability();
		if (!ObjectUtil.isEmpty(villageList)) {
			villageList.stream().filter(obj->!ObjectUtil.isEmpty(obj[1])).forEach(obj -> {
				villageArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(villageArr);
 	}
	
	public void populateCityList(){
		JSONArray cityArr = new JSONArray();
		List<Object[]> cityList=productService.listOfcityFromTraceability();
		if (!ObjectUtil.isEmpty(cityList)) {
			cityList.stream().filter(obj->!ObjectUtil.isEmpty(obj[1])).forEach(obj -> {
				cityArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(cityArr);
 	}
	public void populateFpoList(){
		JSONArray fpoArr = new JSONArray();
		List<Object> fpoList=farmerService.listFarmerFpoFromTraceabilityStock();
		if (!ObjectUtil.isEmpty(fpoList)) {
			/*fpoList.stream().filter(obj->!ObjectUtil.isEmpty(obj[1])).forEach(obj -> {
				fpoArr.add(getJSONObject(obj[0].toString(),obj[1].toString()));
			});*/
			for(Object o:fpoList)
			if(!ObjectUtil.isEmpty(o) && !StringUtil.isEmpty(o.toString())){
				FarmCatalogue f= catalogueService.findCatalogueByCode(o.toString());
			fpoArr.add(getJSONObject(f.getCode(),f.getName()));
			}
		}
		sendAjaxResponse(fpoArr);
 	}
	
	public void populateSeasonList(){
		JSONArray seasonArr = new JSONArray();
		List<HarvestSeason> seasonList=farmerService.listHarvestSeasons();
		if (!ObjectUtil.isEmpty(seasonList)) {
			for(HarvestSeason o:seasonList)
			if(!ObjectUtil.isEmpty(o)){
				seasonArr.add(getJSONObject(o.getCode().toString(),o.getName().toString()));
			}
		}
		sendAjaxResponse(seasonArr);
		
		
		
	}

	 public void populateFarmerMethod() throws IOException
	    {
	    	JSONArray jsonArray = new JSONArray();
	    	JSONObject jsonObject = new JSONObject();
	    	selectedSeason=selectedSeason!=null && !StringUtil.isEmpty(selectedSeason)?selectedSeason:getCurrentSeasonsCode();
	    	Object[] datas=productService.findTotalQtyAndAmtFromProcurementStock(farmerName,product,warehouse,branchIdParma,village,city,fpo,ics,selectedSeason);
	    	jsonObject.put("totalQty", !StringUtil.isEmpty(datas[0])?datas[0]:"0.0");
			jsonObject.put("totalNoOfBags",  !StringUtil.isEmpty(datas[1])?datas[1]:"0.0");
			
			jsonArray.add(jsonObject);
	    	
			response.setContentType("text/JSON");
			PrintWriter out = response.getWriter();
			if (jsonArray.size() > 0)
				out.println(jsonArray.toString());
	    	
	    }

	public String getFarmerName() {
		return farmerName;
	}

	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public String getVillage() {
		return village;
	}

	public void setVillage(String village) {
		this.village = village;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getFpo() {
		return fpo;
	}

	public void setFpo(String fpo) {
		this.fpo = fpo;
	}

	public String getIcs() {
		return ics;
	}

	public void setIcs(String ics) {
		this.ics = ics;
	}

	public Map<String, String> getFarmerIdAndVillageMap() {
		return farmerIdAndVillageMap;
	}

	public void setFarmerIdAndVillageMap(Map<String, String> farmerIdAndVillageMap) {
		this.farmerIdAndVillageMap = farmerIdAndVillageMap;
	}

	public String getFarmerVill() {
		return farmerVill;
	}

	public void setFarmerVill(String farmerVill) {
		this.farmerVill = farmerVill;
	}

	public Map<String, String> getGradeVillMap() {
		return gradeVillMap;
	}

	public void setGradeVillMap(Map<String, String> gradeVillMap) {
		this.gradeVillMap = gradeVillMap;
	}

	public String getGradeVill() {
		return gradeVill;
	}

	public void setGradeVill(String gradeVill) {
		this.gradeVill = gradeVill;
	}

	public String getSelectedSeason() {
		return selectedSeason;
	}

	public void setSelectedSeason(String selectedSeason) {
		this.selectedSeason = selectedSeason;
	}
	
	 
}
