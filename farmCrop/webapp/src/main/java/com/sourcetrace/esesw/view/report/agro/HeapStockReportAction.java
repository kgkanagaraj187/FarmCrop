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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.ese.entity.traceability.HeapData;
import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.profile.GradeMaster;
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
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

public class HeapStockReportAction extends BaseReportAction implements IExporter {

	private static final long serialVersionUID = 1L;
	private Map<String, String> fields = new HashMap<>();
	private HeapData filter;
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
	private String branch;
	private String exportLimit;
	private String headerFields;
	private String icsString;
	private String gining;
	private String procurementProduct;
	private String ics;
	private String heapDataName;
	private String season;
	Map<String, String> icsMap=new HashMap<>();
	/**
	 * @see com.sourcetrace.esesw.view.BaseReportAction#list()
	 */
	public String list() throws Exception {

		request.setAttribute(HEADING, getText(LIST));

		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			setExportLimit(preferences.getPreferences().get(ESESystem.EXPORT_RECORD_LIMIT));
		}
		filter = new HeapData();
	
		
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
	public String detail() throws Exception {
		List<FarmCatalogue> frmCat=catalogueService.findFarmCatalougeByType(Integer.parseInt(getLocaleProperty("icsNameType")));
		icsMap = frmCat.stream().collect(
                Collectors.toMap(FarmCatalogue::getCode, FarmCatalogue::getName));
		filter = new HeapData();

		if (!StringUtil.isEmpty(branchIdParma)) {
			filter.setBranchId(branchIdParma);
		}

		if (!StringUtil.isEmpty(gining)) {
			Warehouse w = new Warehouse();
			w.setId(Long.valueOf(gining));
			filter.setGinning(w);
		}

		if (!StringUtil.isEmpty(procurementProduct)) {
			ProcurementProduct p = new ProcurementProduct();
			p.setId(Long.valueOf(procurementProduct));
			filter.setProcurementProduct(p);
		}

		if (!StringUtil.isEmpty(ics)) {
			filter.setIcs(ics);
		}

		if (!StringUtil.isEmpty(heapDataName)) {
			filter.setName(heapDataName);
		}
		season=season!=null && !StringUtil.isEmpty(season)?season:getCurrentSeasonsCode();
		filter.setSeason(season);
		
		super.filter = this.filter;
		Map data = readData("heapStock");
		return sendJSONResponse(data);
	}

	/**
	 * @see com.sourcetrace.esesw.view.BaseReportAction#toJSON(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		JSONObject jsonObject = new JSONObject();
		Object[] datas = (Object[]) obj;
		JSONArray rows = new JSONArray();
		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(datas[6])))
						? getBranchesMap().get(getParentBranchMap().get(datas[6])) : getBranchesMap().get(datas[6]));
			}
			rows.add(getBranchesMap().get(datas[6]));

		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(branchesMap.get(datas[6]));
			}
		}
		rows.add(datas[8]!=null && !ObjectUtil.isEmpty(datas[8])?farmerService.findSeasonNameByCode(datas[8].toString()).getName():"");
		rows.add(datas[2]);
		rows.add(datas[4]);
		icsString="";
		if (!ObjectUtil.isEmpty(datas[3])) {
			Set<String> icsColl=new HashSet<String>(Arrays.asList(datas[3].toString().split(",")));
			icsString=null;
			icsColl.stream().forEach(ic->{
				icsString=icsString!=null && !StringUtil.isEmpty(icsString)? icsString+","+icsMap.get(ic):icsMap.get(ic);
			});
			icsString=StringUtil.removeLastComma(icsString);
			/*if(datas[3].toString().contains(",")){
				Arrays.asList(datas[3].toString().split(",")).stream().forEach(cat ->{
					FarmCatalogue catalogue = getCatlogueValueByCode(cat);
					if (!ObjectUtil.isEmpty(catalogue)) {
						icsString += catalogue.getName()+",";
					}
				});
				
			}else{
			FarmCatalogue catalogue = getCatlogueValueByCode(datas[3].toString());
			if (!ObjectUtil.isEmpty(catalogue)) {
				icsString = catalogue.getName();
			}
			}*/
		}
		icsString = StringUtil.removeLastComma(icsString);
		rows.add(!StringUtil.isEmpty(datas[3]) ? icsString : "");
		rows.add(datas[7]);
		rows.add(datas[5]);
		jsonObject.put("id", datas[0]);
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
		setXlsFileName(getText("batchStockReport") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("batchStockReport"), fileMap, ".xls"));
		return "xls";
	}

	@SuppressWarnings("unchecked")
	public InputStream getExportDataStream(String exportType) throws IOException {

		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		boolean flag = true;
		DateFormat filterDateFormat = new SimpleDateFormat(getGeneralDateFormat());
		DecimalFormat df = new DecimalFormat("0.000");
		DecimalFormat df1 = new DecimalFormat("0.00");

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("exportHeapStockReportTitle"));
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

		HSSFRow row, titleRow, filterRowTitle, filterRow = null, filterRow2, filterRow3, filterRow4 = null,
				filterRow5 = null, filterRow6 = null, filterRow7 = null, filterRow8 = null, filterRow9 = null;
		HSSFCell cell;

		int imgRow1 = 0;
		int imgRow2 = 4;
		int imgCol1 = 0;
		int imgCol2 = 1;
		int titleRow1 = 2;
		int titleRow2 = 5;
		int count = 0;
		int rowNum = 2;
		int colNum = 0;
		int rowNo=0;
		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap(); // build branch map to get branch name form branch id.

		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, titleRow1, titleRow2));

		sheet.setDefaultColumnWidth(13);

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(2);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("exportHeapStockReportTitle")));
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
			rowNo=rowNum;
			/*filterRow = sheet.createRow(rowNum++);
			filterRow4 = sheet.createRow(rowNum++);
			filterRow5 = sheet.createRow(rowNum++);
			filterRow6 = sheet.createRow(rowNum++);
			filterRow7 = sheet.createRow(rowNum++);
			filterRow8 = sheet.createRow(rowNum++);
*/
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
						filterRow6 = sheet.createRow(rowNum++);
						cell = filterRow6.createCell(1);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("subOrganization")));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						cell = filterRow6.createCell(2);
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
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);
				}

			}

		}
		rowNum++;

		HSSFRow totalGridRowHead = sheet.createRow(rowNum++);
		String[] headerFieldsArr = headerFields.split("###");
		for (int i = 0; i < headerFieldsArr.length; i++) {
			cell = totalGridRowHead.createCell(count);
			cell.setCellValue(new HSSFRichTextString(String.valueOf(headerFieldsArr[i])));
			style4.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
			style4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			style4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cell.setCellStyle(style4);
			font2.setBoldweight((short) 12);
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			style4.setFont(font2);
			sheet.setColumnWidth(count, (15 * 550));
			count++;

		}
		rowNum+=5;
		
		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		String mainGridColumnHeaders = "";

		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("ExportColumnHeapStockBranch");
			} else if (!StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("ExportColumnHeapStock");
			}
		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("exportHeapStockColumnHeaderBranch");
			} else {
				mainGridColumnHeaders = getLocaleProperty("exportHeapStockColumnHeader");
			}
		}
		List<FarmCatalogue> frmCat=catalogueService.findFarmCatalougeByType(Integer.parseInt(getLocaleProperty("icsNameType")));
		icsMap = frmCat.stream().collect(
                Collectors.toMap(FarmCatalogue::getCode, FarmCatalogue::getName));
		int mainGridIterator = 0;

		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {

			if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
			}

			if (StringUtil.isEmpty(branchIdValue)) { // Add branch header to
														// export sheet if main
														// branch logged in.
				cell = mainGridRowHead.createCell(mainGridIterator);
				cell.setCellValue(new HSSFRichTextString(cellHeader));
				style2.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
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
					style2.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
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

		setFilter(new HeapData());

		if (!StringUtil.isEmpty(branchIdParma)) {
			filter.setBranchId(branchIdParma);
		}

		if (!StringUtil.isEmpty(gining)) {
			Warehouse w = new Warehouse();
			w.setId(Long.valueOf(gining));
			filter.setGinning(w);
		}

		if (!StringUtil.isEmpty(procurementProduct)) {
			ProcurementProduct p = new ProcurementProduct();
			p.setId(Long.valueOf(procurementProduct));
			filter.setProcurementProduct(p);
		}

		if (!StringUtil.isEmpty(ics)) {
			filter.setIcs(ics);
		}

		if (!StringUtil.isEmpty(heapDataName)) {
			filter.setName(heapDataName);
		}
		season=season!=null && !StringUtil.isEmpty(season)?season:getCurrentSeasonsCode();
		filter.setSeason(season);
		
		super.filter = this.filter;

		Map data = readData("heapStock");

		List<Object[]> dfata = (ArrayList) data.get(ROWS);

		if (ObjectUtil.isEmpty(dfata)) {
			return null;
		}

		for (Object[] datas : dfata) {
			if (isMailExport() && flag) {

				if (!ObjectUtil.isEmpty(this.filter.getGinning())) {
					if (filter.getGinning().getId() > 0) {
						filterRow4 = sheet.createRow(rowNo++);
						cell = filterRow4.createCell(1);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("gining")));
						cell.setCellStyle(filterStyle);
						font2.setBoldweight((short) 12);
						font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						style2.setFont(font2);

						cell = filterRow4.createCell(2);
						cell.setCellValue(new HSSFRichTextString(String.valueOf(datas[2]).trim()));
						cell.setCellStyle(filterStyle);
						font2.setBoldweight((short) 12);
						font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						style2.setFont(font2);
						flag = false;
					}
				}

				if (!ObjectUtil.isEmpty(this.filter.getProcurementProduct())) {
					if (filter.getProcurementProduct().getId() > 0) {
						filterRow = sheet.createRow(rowNo++);
						cell = filterRow.createCell(1);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("product")));
						cell.setCellStyle(filterStyle);
						font2.setBoldweight((short) 12);
						font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						style2.setFont(font2);

						cell = filterRow.createCell(2);
						cell.setCellValue(new HSSFRichTextString(String.valueOf(datas[4]).trim()));
						cell.setCellStyle(filterStyle);
						font2.setBoldweight((short) 12);
						font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						style2.setFont(font2);

						flag = false;
					}
				}

				if (!ObjectUtil.isEmpty(this.filter.getIcs())) {
					filterRow5 = sheet.createRow(rowNo++);
					cell = filterRow5.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("icssName")));
					cell.setCellStyle(filterStyle);
					font2.setBoldweight((short) 12);
					font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					style2.setFont(font2);

					cell = filterRow5.createCell(2);
					
					icsString=icsMap.get(this.filter.getIcs());
					
					
					cell.setCellValue(new HSSFRichTextString(String.valueOf(icsString)));
					cell.setCellStyle(filterStyle);
					font2.setBoldweight((short) 12);
					font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					style2.setFont(font2);
					flag = false;

				}

				if (!ObjectUtil.isEmpty(this.filter.getName())) {
					filterRow6 = sheet.createRow(rowNo++);
					cell = filterRow6.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("heapName")));
					cell.setCellStyle(filterStyle);
					font2.setBoldweight((short) 12);
					font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					style2.setFont(font2);

					cell = filterRow6.createCell(2);
					cell.setCellValue(new HSSFRichTextString(String.valueOf(datas[7]).trim()));
					cell.setCellStyle(filterStyle);
					font2.setBoldweight((short) 12);
					font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					style2.setFont(font2);
					flag = false;

				}
				if (!ObjectUtil.isEmpty(this.filter.getSeason())) {
					filterRow4 = sheet.createRow(rowNo++);
					cell = filterRow4.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("season")));
					cell.setCellStyle(filterStyle);
					font2.setBoldweight((short) 12);
					font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					style2.setFont(font2);

					cell = filterRow4.createCell(2);
					cell.setCellValue(new HSSFRichTextString(farmerService.findSeasonNameByCode(season).getName()));
					cell.setCellStyle(filterStyle);
					font2.setBoldweight((short) 12);
					font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					style2.setFont(font2);
					flag = false;

				}
			}
				row = sheet.createRow(rowNum++);
				colNum = 0;

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
					cell.setCellValue(getBranchesMap().get(datas[6]));

				} else {
					if (StringUtil.isEmpty(branchIdValue)) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(
								!StringUtil.isEmpty(datas[6]) ? (branchesMap.get(datas[6])) : ""));
					}
				}
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(datas[8]!=null && !ObjectUtil.isEmpty(datas[8])?farmerService.findSeasonNameByCode(datas[8].toString()).getName():""));
				
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(String.valueOf(datas[2])));

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(String.valueOf(datas[4])));

				if (!ObjectUtil.isEmpty(datas[3])) {
					Set<String> icsColl=new HashSet<String>(Arrays.asList(datas[3].toString().split(",")));
					icsString=null;
					icsColl.stream().forEach(ic->{
						icsString=icsString!=null && !StringUtil.isEmpty(icsString)? icsString+","+icsMap.get(ic):icsMap.get(ic);
					});
					icsString=StringUtil.removeLastComma(icsString);
					//array = new HashSet<String>(Arrays.asList(array)).toArray(new String[0]);
					/*if(datas[3].toString().contains(",")){
						Arrays.asList(datas[3].toString().split(",")).stream().forEach(cat ->{
							FarmCatalogue catalogue = getCatlogueValueByCode(cat);
							if (!ObjectUtil.isEmpty(catalogue)) {
								icsString += catalogue.getName()+",";
							}
						});
						icsString=StringUtil.removeLastComma(icsString);
					}else{
					FarmCatalogue catalogue = getCatlogueValueByCode(datas[3].toString());
					if (!ObjectUtil.isEmpty(catalogue)) {
						icsString = catalogue.getName();
					}
					}*/
				}


				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(icsString) ? icsString : ""));

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(String.valueOf(datas[7])));

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(String.valueOf(datas[5])));
			
		}

		for (int i = 0; i <= colNum; i++) {
			sheet.autoSizeColumn(i);
		}

		int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
				: request.getSession().getId();
		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("HeapStock") + fileNameDateFormat.format(new Date()) + ".xls";
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

		byte[] picData = null;
		picData = clientService.findLogoByCode(Asset.APP_LOGO);
		if (!ObjectUtil.isEmpty(picData)) {
			// picData=Base64.decode(picData);
		}

		if (picData != null)
			index = wb.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
	}

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

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
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

	public void populateGiningList() {
		JSONArray giningArr = new JSONArray();
		List<Object[]> coopList = locationService.listOfGiningFromHeap();
		if (!ObjectUtil.isEmpty(coopList)) {
			coopList.forEach(obj -> {
				giningArr.add(getJSONObject(obj[0].toString(),  obj[1].toString()));
			});
		}
		sendAjaxResponse(giningArr);
	}

	public void populateSeasonList(){
		JSONArray seasonArr=new JSONArray();
		List<Object[]> seasonList=farmerService.listSeasonCodeAndName();
		if(!ObjectUtil.isListEmpty(seasonList)){
			seasonList.forEach(ob->{
				seasonArr.add(getJSONObject(ob[0].toString(),ob[1].toString()));
			});
		}
		sendAjaxResponse(seasonArr);
	}
	public void populateICSList() {
		JSONArray icsArr = new JSONArray();
		List<Object[]> icsList = productService.listOfICSFromHeapStock();
		if (!ObjectUtil.isEmpty(icsList)) {
			icsList.stream().filter(obj -> !ObjectUtil.isEmpty(obj[1])).forEach(obj -> {
				icsArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(icsArr);
	}

	public void populateProductList() {
		JSONArray productArr = new JSONArray();
		List<Object[]> productList = productService.listOfProcurementProductFromHeap();
		if (!ObjectUtil.isEmpty(productList)) {
			productList.forEach(obj -> {
				productArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(productArr);
	}

	public void populateHeapNameList() {
		JSONArray heapNameArr = new JSONArray();
		List<Object> heapList = productService.listOfHeapName();
		if (!ObjectUtil.isEmpty(heapList)) {
			heapList.forEach(obj -> {
				heapNameArr.add(getJSONObject(obj.toString(), obj.toString()));
			});
		}
		sendAjaxResponse(heapNameArr);
	}

	public IProductService getProductService() {
		return productService;
	}

	public void setProductService(IProductService productService) {
		this.productService = productService;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getHeaderFields() {
		return headerFields;
	}

	public void setHeaderFields(String headerFields) {
		this.headerFields = headerFields;
	}

	public HeapData getFilter() {
		return filter;
	}

	public void setFilter(HeapData filter) {
		this.filter = filter;
	}

	public String getIcsString() {
		return icsString;
	}

	public void setIcsString(String icsString) {
		this.icsString = icsString;
	}

	public String getProcurementProduct() {
		return procurementProduct;
	}

	public void setProcurementProduct(String procurementProduct) {
		this.procurementProduct = procurementProduct;
	}

	public String getIcs() {
		return ics;
	}

	public void setIcs(String ics) {
		this.ics = ics;
	}

	public String getHeapDataName() {
		return heapDataName;
	}

	public void setHeapDataName(String heapDataName) {
		this.heapDataName = heapDataName;
	}

	public String getGining() {
		return gining;
	}

	public void setGining(String gining) {
		this.gining = gining;
	}

	public void populateHeaderFields() throws IOException {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		season=season!=null && !StringUtil.isEmpty(season)?season:getCurrentSeasonsCode();
		double datas = farmerService.findTotalStockInHeap(gining, procurementProduct, ics, heapDataName, branchIdParma,season);
		jsonObject.put("totalStock", !StringUtil.isEmpty(datas) ? datas : "0.0");
		jsonArray.add(jsonObject);

		response.setContentType("text/JSON");
		PrintWriter out = response.getWriter();
		if (jsonArray.size() > 0)
			out.println(jsonArray.toString());

	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public Map<String, String> getIcsMap() {
		return icsMap;
	}

	public void setIcsMap(Map<String, String> icsMap) {
		this.icsMap = icsMap;
	}
	
}
