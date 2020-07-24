/*
3 * OfflineFarmerEnrollmentReportAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.OfflineFarmerEnrollment;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

public class OfflineFarmerEnrollmentReportAction extends BaseReportAction implements IExporter {

	private static final long serialVersionUID = 1L;
	private Farmer filter;

	private String id;
	private String farmerId;
	private String gridIdentity;
	private String farmCode;
	private String xlsFileName;
	private InputStream fileInputStream;
	private String farmerCodeEnabled;
	private IPreferencesService preferncesService; 
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private Map<String, String> fields = new LinkedHashMap<String, String>();
	Map<Integer, String> statusList = new HashMap<Integer, String>();

	private IProductDistributionService productDistributionService;

	private IClientService clientService;

	private String branchIdParam;
	private String lastName;
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private ILocationService locationService;

	public IClientService getClientService() {
		return clientService;
	}

	public void setClientService(IClientService clientService) {
		this.clientService = clientService;
	}

	
	/**
	 * Sets the status list.
	 * 
	 * @param statusList
	 *            the status list
	 */
	public void setStatusList(Map<Integer, String> statusList) {

		this.statusList = statusList;
	}

	/**
	 * @see com.sourcetrace.esesw.view.BaseReportAction#getId()
	 */
	public String getId() {

		return id;
	}

	/**
	 * @see com.sourcetrace.esesw.view.BaseReportAction#setId(java.lang.String)
	 */
	public void setId(String id) {

		this.id = id;
	}

	/**
	 * Gets the filter.
	 * 
	 * @return the filter
	 */
	public Farmer getFilter() {

		return filter;
	}

	/**
	 * Sets the filter.
	 * 
	 * @param filter
	 *            the new filter
	 */
	public void setFilter(Farmer filter) {

		this.filter = filter;
	}

	/**
	 * Sets the farmer id.
	 * 
	 * @param farmerId
	 *            the new farmer id
	 */
	public void setFarmerId(String farmerId) {

		this.farmerId = farmerId;
	}

	/**
	 * Gets the farmer id.
	 * 
	 * @return the farmer id
	 */
	public String getFarmerId() {

		return farmerId;
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
	 * Sets the farm code.
	 * 
	 * @param farmCode
	 *            the new farm code
	 */
	public void setFarmCode(String farmCode) {
		this.farmCode = farmCode;
	}

	/**
	 * Gets the farm code.
	 * 
	 * @return the farm code
	 */
	public String getFarmCode() {

		return farmCode;
	}

	/**
	 * @see com.sourcetrace.esesw.view.BaseReportAction#list()
	 */
	public String list() {
		ESESystem preferences = preferncesService.findPrefernceById("1");
        if (!StringUtil.isEmpty(preferences)) {
        	if(preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE)!=null){
        	setFarmerCodeEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE));}
         }
		request.setAttribute(HEADING, getText(LIST));
		Calendar currentDate = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		super.startDate = df
				.format(DateUtil.getdateBeforeOneMonth(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
						currentDate.get(Calendar.DATE)));
		super.endDate = df.format(currentDate.getTime());
		request.setAttribute(HEADING, getText(LIST));
		
		filter = new Farmer();
		return LIST;
	}

	/**
	 * Detail.
	 * 
	 * @return the string
	 * 
	 * @throws Exception the exception
	 */
	@SuppressWarnings("unchecked")
	public String detail() throws Exception {
		this.gridIdentity = "detail";
		
		if (!StringUtil.isEmpty(branchIdParam)) { // set filter of branch id and
			filter.setBranchId(branchIdParam);
		}
		
		    if(!StringUtil.isEmpty(startDate))
		    {
			filter.setStartDate(startDate);
			}
			
			if(!StringUtil.isEmpty(endDate))
			{
		    filter.setEndDate(endDate);
		    }
			
		super.filter = this.filter;
		Map data = readData();
		return sendJSONResponse(data);
	}

	/**
	 * @see com.sourcetrace.esesw.view.BaseReportAction#toJSON(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		JSONObject jsonObject = new JSONObject();
		if ("detail".equals(this.gridIdentity)) {

			Farmer farmer = (Farmer) obj;
			ESESystem preferences = preferncesService.findPrefernceById("1");
			 setFarmerCodeEnabled("0");
		        if (!StringUtil.isEmpty(preferences)) {
		        	if(preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE)!=null){
		        	setFarmerCodeEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE));}
		         }
			JSONArray rows = new JSONArray();
			/*if (StringUtil.isEmpty(branchIdValue)) {
			rows.add(branchesMap.get(farmer.getBranchId()));
		}*/
		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(farmer.getBranchId())))
						? getBranchesMap().get(getParentBranchMap().get(farmer.getBranchId())) : getBranchesMap().get(farmer.getBranchId()));
			}
			rows.add(getBranchesMap().get(farmer.getBranchId()));
		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(branchesMap.get(farmer.getBranchId()));
			}
		}

			// rows.add(farmer.getFarmerId()); // Note: commented to fix bug
			// 664.
	        if (!StringUtil.isEmpty(farmerCodeEnabled)&& farmerCodeEnabled.equalsIgnoreCase("1"))
	        {
			rows.add(farmer.getFarmerCode());
	        }
			//rows.add(farmer.getFirstName());
	        String firstName =  String.valueOf(farmer.getFirstName());
			String farmerId = String.valueOf(farmer.getId());
			String linkField = "<a href=farmer_detail.action?id="+farmerId+" target=_blank>"+firstName+"</a>";
			rows.add(!ObjectUtil.isEmpty(linkField) ? linkField : "");// Farmer
			//rows.add(farmer.getLastName());
			rows.add(farmer.getVillage().getName());
			rows.add(farmer.getStatusMsg());

			jsonObject.put("id", farmer.getId());
			jsonObject.put("cell", rows);
		}
		return jsonObject;
	}

	/**
	 * Gets the statuses list.
	 * 
	 * @return the statuses list
	 */
	public Map<Integer, String> getStatusesList() {

		statusList.put(OfflineFarmerEnrollment.FAILED, "FAILED");
		statusList.put(OfflineFarmerEnrollment.PENDING, "PENDING");
		return statusList;
	}

	/**
	 * Populate xls.
	 * 
	 * @return the string
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public String populateXLS() throws Exception {

		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getLocaleProperty("OfflineFarmerEnrollmentReportList") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(
				FileUtil.createFileInputStreamToZipFile(getLocaleProperty("offlineFarmerEnrollmentReportList"), fileMap, ".xls"));
		return "xls";
	}

	/**
	 * @see com.sourcetrace.esesw.view.IExporter#getExportDataStream(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public InputStream getExportDataStream(String exportType) throws IOException {

		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		setsDate(IExporter.EXPORT_DAILY.equalsIgnoreCase(exportType) ? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));
		boolean flag = true;
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("exportOfflineFarmerEnrollmentTitle"));
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

		HSSFRow row, titleRow, filterRowTitle = null, filterRow1 = null, filterRow2 = null;
		HSSFCell cell;

		int imgRow1 = 0;
		int imgRow2 = 4;
		int imgCol1 = 0;
		int imgCol2 = 0;
		int titleRow1 = 2;
		int titleRow2 = 5;

		int rowNum = 2;
		int colNum = 0;

		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, titleRow1, titleRow2));

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(2);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("exportOfflineFarmerEnrollmentTitle")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);

		if (isMailExport()) {
			rowNum++;
			rowNum++;
		filterRowTitle = sheet.createRow(rowNum++);
			filterRow1 = sheet.createRow(rowNum++);
			filterRow2 = sheet.createRow(rowNum++);
		}
		rowNum++;

		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		String mainGridColumnHeaders;
		
		branchIdValue = getBranchId();

		
		if (StringUtil.isEmpty(branchIdValue)) {
			mainGridColumnHeaders = getLocaleProperty("ColumnHeaderOfflineReportFarmerEnrollmentBranch");
		}else {
			 mainGridColumnHeaders = getLocaleProperty("ColumnHeaderOfflineReportFarmerEnrollment");
		}
		
		if(getCurrentTenantId().equalsIgnoreCase("pratibha")){
			
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("ColumnHeaderOfflineReportFarmerEnrollmentBranch");
			}else if(getBranchId().equalsIgnoreCase("organic")){
				mainGridColumnHeaders = getLocaleProperty("OrganiFarmerEnrollmentExportHeaderagent");
			}else if(getBranchId().equalsIgnoreCase("bci")){
				mainGridColumnHeaders = getLocaleProperty("BCIFarmerEnrollmentExportHeaderagent");
			}
			
		}
		
		int mainGridIterator = 0;

		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {

			if(cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT))		
			{		
				cellHeader=cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());		
			}else if(cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT))				
			{				
				cellHeader=cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());				
			}	
						
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

		if (ObjectUtil.isEmpty(this.filter)) {
			this.filter = new Farmer();
			this.filter.setVillage(new Village());
		}
		super.filter = this.filter;

		Map data = isMailExport() ? readData() : readExportData();

		List<Farmer> mainGridRows = (List<Farmer>) data.get(ROWS);
		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;
		
		Long serialNumber = 0L;

		
		for (Farmer farmer : mainGridRows) {

			if ((!StringUtil.isEmpty(filter.getVillage().getCode()) || filter.getStatusCode() > 0) && flag) {

				cell = filterRowTitle.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("filter")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				if (filter.getStatusCode() > 0) {

					cell = filterRow1.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("status")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow1.createCell(2);
					if (filter.getStatusCode() == 1) {
						cell.setCellValue(new HSSFRichTextString("FAILED"));

					} else if (filter.getStatusCode() == 2) {
						cell.setCellValue(new HSSFRichTextString("PENDING"));

					}

					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

				}

				if (!StringUtil.isEmpty(filter.getVillage().getCode())) {

					cell = filterRow2.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("villageCode")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow2.createCell(2);
					cell.setCellValue(new HSSFRichTextString(farmer.getVillage().getCode()));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					if (filter.getStatusCode() == -1) {
						sheet.shiftRows(filterRow1.getRowNum() + 1, filterRow2.getRowNum() + 1, -1);
					}

				}

				flag = false;
			}

			row = sheet.createRow(rowNum++);
			colNum = 0;

			serialNumber++;
			/*cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(String.valueOf(serialNumber)!=null ? String.valueOf(serialNumber) : ""));*/
			
			cell = row.createCell(colNum++);
			style3.setAlignment(CellStyle.ALIGN_CENTER);
			cell.setCellStyle(style3);
			cell.setCellValue(serialNumber);
		
			
			
			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
				if (StringUtil.isEmpty(branchIdValue)) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(farmer.getBranchId())))
							? getBranchesMap().get(getParentBranchMap().get(farmer.getBranchId()))
							: getBranchesMap().get(farmer.getBranchId())));
				}
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(farmer.getBranchId())));

			} else {
				if (StringUtil.isEmpty(branchIdValue)) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(farmer.getBranchId())));
				}
			}
			
			/*cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(farmer.getFarmerId()));*/

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(farmer.getFarmerCode()));

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(farmer.getFirstName()));

			/*cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(farmer.getLastName()));*/

			cell = row.createCell(colNum++);
			/*cell.setCellValue(new HSSFRichTextString(farmer.getVillage().getCode()));*/
			cell.setCellValue(new HSSFRichTextString(farmer.getVillage().getName()));

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(farmer.getStatusMsg()));

		}

		/*for (int i = 0; i <= colNum; i++) {
			sheet.autoSizeColumn(i);
		}
		*/
		
		/*int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		picture.resize();
		String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
				: request.getSession().getId();
		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("offlineFarmerEnrollmentReportList") + fileNameDateFormat.format(new Date())
				+ ".xls";
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
	
	
	/**
	 * Gets the pic index.
	 * 
	 * @param wb
	 *            the wb
	 * 
	 * @return the pic index
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	/*public int getPicIndex(HSSFWorkbook wb) throws IOException {

		int index = -1;

		byte[] picData = null;
		picData = clientService.findLogoByCode(Asset.APP_LOGO);
		if (!ObjectUtil.isEmpty(picData)) {
			// picData=Base64.decode(picData);
		}
*/		
		public int getPicIndex(HSSFWorkbook wb) throws IOException {

			int index = -1;

			byte[] picData = null;
			picData = clientService.findLogoByCode(Asset.APP_LOGO);

			if (picData != null)
				index = wb.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

			return index;
		}


		// if (ObjectUtil.isEmpty(request)) {
		// picData = productDistributionService.findLogoImageById(1L);
		// } else {
		// String client = "sourceTrace-logo.png";
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
		/*if (picData != null)
			index = wb.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
	}
*/
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

	public String getBranchIdParam() {
		return branchIdParam;
	}

	public void setBranchIdParam(String branchIdParam) {
		this.branchIdParam = branchIdParam;
	}

	public Map<String, String> getLastNameList() {

		Map<String, String> farmerListMap = new LinkedHashMap<String, String>();

		List<Object[]> farmersList = farmerService.listFarmerInfo();

		for (Object[] obj : farmersList) {
			if (!ObjectUtil.isEmpty(obj[4])) {
				farmerListMap.put(obj[4].toString(), obj[4].toString());
			}
		}
		return farmerListMap;
	}


/*	public Map<String, String> getVillageList() {

        Map<String, String> villageMap = new LinkedHashMap<String, String>();

        List<Village> villageList = locationService.listVillage();
        for (Village obj : villageList) {
            villageMap.put(obj.getName(), obj.getName());
        }
        return villageMap;

    } */
	public void populateVillageList(){
		JSONArray warehouseArr = new JSONArray();
		List<Village> villageList = locationService.listVillage();
		if (!ObjectUtil.isEmpty(villageList)) {
			villageList.forEach(obj -> {
				warehouseArr.add(getJSONObject(obj.getName(), obj.getName()));
			});
		}
		sendAjaxResponse(warehouseArr);
	}
	  /*public Map<String,String> getVillageList(){
    	  Map<String,String> villageMap = new LinkedHashMap<>();
    	  List<Object[]> farmerList = locationService.listVillage();
    	  List<String> villagesList = new ArrayList<>();
    	  if(!ObjectUtil.isEmpty(farmerList)){
    		  villagesList = farmerList.stream().map(obj->String.valueOf(obj[0])).distinct().collect(Collectors.toList());
    		  villageMap=villagesList.stream().collect(Collectors.toMap(String::toString, String::toString));
    		  
    	  }
    	  return villageMap;
    	 }*/

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	
	
	public Map<String, String> getFields() {
		
		
		fields.put("1",getText("status"));
		fields.put("2",getLocaleProperty("lastName"));
		fields.put("3",getText("villageName"));
		
		if (ObjectUtil.isEmpty(getBranchId())) {
			fields.put("4",getText("app.branch"));
		}
		return fields;
	}
}
