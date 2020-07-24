/*
 * OfflineTrainingCompletionReportAction.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.ese.entity.txn.training.OfflineTrainingStatus;
import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

public class OfflineTrainingCompletionReportAction extends BaseReportAction implements IExporter {

	private static final long serialVersionUID = 5753871231037958377L;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	private String xlsFileName;
	private String gridIdentity;
	private InputStream fileInputStream;
	private OfflineTrainingStatus filter;
	private Map<String, String> fields = new HashMap<>();
	private IProductDistributionService productDistributionService;
	private IPreferencesService preferncesService;
	@Autowired
	private ILocationService locationService;

	private String branchIdParma;
	private String daterange;
	private String exportLimit;

	/**
	 * @see com.sourcetrace.esesw.view.BaseReportAction#list()
	 */
	public String list() throws Exception {

		Calendar currentDate = Calendar.getInstance();
		Calendar cal = (Calendar) currentDate.clone();
		cal.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) - 1);
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		super.startDate = df.format(cal.getTime());
		super.endDate = df.format(currentDate.getTime());

		daterange = super.startDate + " - " + super.endDate;
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			setExportLimit(preferences.getPreferences().get(ESESystem.EXPORT_RECORD_LIMIT));
		}

		setFilter(new OfflineTrainingStatus());
		request.setAttribute(HEADING, getText(LIST));
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
		filter = new OfflineTrainingStatus();

		if (!StringUtil.isEmpty(branchIdParma)) {
			 if (!getIsMultiBranch().equalsIgnoreCase("1")) {
	                List<String> branchList = new ArrayList<>();
	                branchList.add(branchIdParma);
	                filter.setBranchesList(branchList);
	            } else {
	                List<String> branchList = new ArrayList<>();
	                List<BranchMaster> branches = clientService
	                        .listChildBranchIds(branchIdParma);
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
		this.gridIdentity = "detail";

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
			OfflineTrainingStatus offlineTrainingStatus = (OfflineTrainingStatus) obj;
			JSONArray rows = new JSONArray();

		/*	if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(branchesMap.get(offlineTrainingStatus.getBranchId()));
			}
*/
			
			if ((getIsMultiBranch().equalsIgnoreCase("1") && (getIsParentBranch().equals("1")||StringUtil.isEmpty(branchIdValue)))) {
				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(offlineTrainingStatus.getBranchId())))
							? getBranchesMap().get(getParentBranchMap().get(offlineTrainingStatus.getBranchId()))
							: getBranchesMap().get(offlineTrainingStatus.getBranchId()));
				}
				rows.add(getBranchesMap().get(offlineTrainingStatus.getBranchId()));
		   
		     } else {
		         if (StringUtil.isEmpty(branchIdValue)) {
		             rows.add(branchesMap.get(offlineTrainingStatus.getBranchId()));
		         }
		     }
			
			ESESystem preferences = preferncesService.findPrefernceById("1");

			if (!StringUtil.isEmpty(preferences)) {
				DateFormat genDate = new SimpleDateFormat(
						preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
				// String dateValue =
				// offlineTrainingStatus.getTrainingDate().toString();
				// String[] dateArr = dateValue.split(" ");
				// rows.add(!StringUtil.isEmpty(offlineTrainingStatus.getTrainingDate().toString())
				// ? DateUtil.convertStringToDate(dateArr[0],"yyyy-MM-dd"):"");
				// DateUtil.convertStringToDate(offlineTrainingStatus.getTrainingDate(),
				// "dd/MM/yyyy")
				try {
					DateFormat outputFormat = new SimpleDateFormat(
							preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
					DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					SimpleDateFormat df = new SimpleDateFormat(
							preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
					rows.add(!StringUtil.isEmpty(offlineTrainingStatus.getTrainingDate())
							? outputFormat.format(inputFormat.parse(offlineTrainingStatus.getTrainingDate())) : "");
				} catch (ParseException e) {
					rows.add("");
				}
			}

			rows.add(!StringUtil.isEmpty(offlineTrainingStatus.getAgentId()) ? offlineTrainingStatus.getAgentId() : "");
			Warehouse wh = locationService.findWarehouseByCode(offlineTrainingStatus.getLearningGroupCode());
			/*rows.add(wh!=null 
					? wh.getCode()+" - "+wh.getName() : "");*/
			
			rows.add(!StringUtil.isEmpty(wh)&& wh!=null ? wh.getName()
					: "");
			
			rows.add(!StringUtil.isEmpty(offlineTrainingStatus.getReceiptNo()) ? offlineTrainingStatus.getReceiptNo()
					: "");
			/*rows.add(!StringUtil.isEmpty(offlineTrainingStatus.getFarmerAttended())
					? offlineTrainingStatus.getFarmerAttended() : "");*/
			rows.add(!StringUtil.isEmpty(offlineTrainingStatus.getRemarks()) ? offlineTrainingStatus.getRemarks() : "");
			rows.add(offlineTrainingStatus.getStatusMessage());
			jsonObject.put("id", offlineTrainingStatus.getId());
			jsonObject.put("cell", rows);
		}

		return jsonObject;
	}

	/**
	 * Sets the filter.
	 * 
	 * @param filter
	 *            the new filter
	 */
	public void setFilter(OfflineTrainingStatus filter) {

		this.filter = filter;
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
		setXlsFileName(getText("OfflineTrainingStatusReport") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("offlineTrainingStatusReportFileName"),
				fileMap, ".xls"));
		return "xls";
	}

	/**
	 * Gets the excel export input stream.
	 * 
	 * @param manual
	 *            the manual
	 * @return the excel export input stream
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("unchecked")
	public InputStream getExportDataStream(String exportType) throws IOException {
		
		Long serialNumber = 0L;


		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		setsDate(IExporter.EXPORT_DAILY.equalsIgnoreCase(exportType) ? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("offlineTrainingStatusReportTitle"));
		HSSFPatriarch drawing = sheet.createDrawingPatriarch();

		HSSFCellStyle style1 = wb.createCellStyle();
		HSSFCellStyle titleStyle = wb.createCellStyle();
		HSSFCellStyle headerStyle = wb.createCellStyle();
		HSSFCellStyle dataStyle = wb.createCellStyle();

		HSSFFont titleFont = wb.createFont();
		titleFont.setFontHeightInPoints((short) 22);
		titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		HSSFFont headerFont = wb.createFont();
		headerFont.setFontHeightInPoints((short) 12);
		headerFont.setBoldweight((short) 12);
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		HSSFFont dataFont = wb.createFont();
		dataFont.setFontHeightInPoints((short) 10);

		HSSFRow row, titleRow, filterRowTitle, filterRow;
		HSSFCell cell;

		int imgRow1 = 0;
		int imgRow2 = 4;
		int imgCol1 = 0;
		int imgCol2 = 0;
		int titleRow1 = 2;
		int titleRow2 = 5;

		int rowNum = 2;
		int colNum = 0;


		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2)); // LogoCells
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, titleRow1, titleRow2)); // Title
																							// Cells

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(2);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("offlineTrainingStatusReportTitle")));
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cell.setCellStyle(titleStyle);
		titleStyle.setFont(titleFont);

		rowNum++;
		rowNum++;
		setFilter(new OfflineTrainingStatus());
		super.filter = this.filter;

		if (isMailExport() && super.filter != null) {
			if(!StringUtil.isEmpty(branchIdParma)){
			filterRowTitle = sheet.createRow(rowNum++);
			cell = filterRowTitle.createCell(0);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("exportFilter")));
			cell.setCellStyle(headerStyle);
			headerStyle.setFont(headerFont);
			}
			filterRow = sheet.createRow(rowNum++);
			cell = filterRow.createCell(0);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("StartingDate")));
			cell.setCellStyle(dataStyle);
			dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			dataStyle.setFont(dataFont);

			cell = filterRow.createCell(1);
			String startDateVal = String.valueOf(super.startDate);
			cell.setCellValue(new HSSFRichTextString(startDateVal));
			cell.setCellStyle(dataStyle);
			dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			dataStyle.setFont(dataFont);

			filterRow = sheet.createRow(rowNum++);
			cell = filterRow.createCell(0);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("EndingDate")));
			cell.setCellStyle(dataStyle);
			dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			dataStyle.setFont(dataFont);

			cell = filterRow.createCell(1);
			String endDateVal = String.valueOf(super.endDate);
			cell.setCellValue(new HSSFRichTextString(endDateVal));
			cell.setCellStyle(dataStyle);
			dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			dataStyle.setFont(dataFont);

			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
				   if (StringUtil.isEmpty(branchIdValue)) {
					   filterRow = sheet.createRow(rowNum++);
				    cell = filterRow.createCell(1);
				    cell.setCellValue(new HSSFRichTextString(getLocaleProperty("mainOrganization")));
				    dataFont.setBoldweight((short) 12);
				    dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				    dataStyle.setFont(dataFont);
				    cell.setCellStyle(dataStyle);

				    cell = filterRow.createCell(2);
				    //cell.setCellValue(new HSSFRichTextString((branchesMap.get(filter.getBranchId()))));
				    cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(getParentBranchMap().get(filter.getBranchId()))));
				    dataFont.setBoldweight((short) 12);
				    dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				    dataStyle.setFont(dataFont);
				    cell.setCellStyle(dataStyle);
				  
				    filterRow = sheet.createRow(rowNum++);
				    cell = filterRow.createCell(1);
				    cell.setCellValue(new HSSFRichTextString(getLocaleProperty("subOrganization")));
				    dataFont.setBoldweight((short) 12);
				    dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				    dataStyle.setFont(dataFont);
				    cell.setCellStyle(dataStyle);

				    cell = filterRow.createCell(2);
				    cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(getSubBranchIdParma())));
				    dataFont.setBoldweight((short) 12);
				    dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				    dataStyle.setFont(dataFont);
				    cell.setCellStyle(dataStyle);
				    
				   } else{
					   if(!StringUtil.isEmpty(getBranchesMap().get(getSubBranchIdParma()))){
						   filterRow = sheet.createRow(rowNum++);
					    cell = filterRow.createCell(1);
					    cell.setCellValue(new HSSFRichTextString(getLocaleProperty("subOrganization")));
					    dataFont.setBoldweight((short) 12);
					    dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					    dataStyle.setFont(dataFont);
					    cell.setCellStyle(dataStyle);

					    cell = filterRow.createCell(2);
					    cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(getSubBranchIdParma())));
					    dataFont.setBoldweight((short) 12);
					    dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					    dataStyle.setFont(dataFont);
					    cell.setCellStyle(dataStyle);
					   }
				   }

				   }else{
					   if(!StringUtil.isEmpty(branchIdParma)){
						   filterRow = sheet.createRow(rowNum++);
					    cell = filterRow.createCell(1);
					    cell.setCellValue(new HSSFRichTextString(getText("organization")));
					    dataFont.setBoldweight((short) 12);
					    dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					    dataStyle.setFont(dataFont);
					    cell.setCellStyle(dataStyle);

					    cell = filterRow.createCell(2);
					    BranchMaster branch = clientService.findBranchMasterByBranchId(branchIdParma);
					    cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(branch) ? branch.getName() : "" )));
					    //cell.setCellValue(new HSSFRichTextString(branchIdParma));
					    dataFont.setBoldweight((short) 12);
					    dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					    dataStyle.setFont(dataFont);
					    cell.setCellStyle(dataStyle);
					   }
				   
				   }
			
			++rowNum;
		}

		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		//String mainGridColumnHeaders = getLocaleProperty("offlineTrainingStatusReportColumnHeaders");
		String mainGridColumnHeaders = "";
		
		branchIdValue = getBranchId(); // set value for branch id.

		
		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("offlineTrainingcompletionHeaderBranch");
			} else if (!StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("offlineTrainingcompletionBranch");
			}
		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("offlineTrainingcompletionReportColumnHeaderBranch");
			} else {
				mainGridColumnHeaders = getLocaleProperty("offlineTrainingcompletionReportColumnHeader");
			}
		}
		
		if(getCurrentTenantId().equalsIgnoreCase("pratibha")){
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("offlineStatusTrainingExportHeader");
			} 
			else if(getBranchId().equalsIgnoreCase("organic")){
				mainGridColumnHeaders = getLocaleProperty("OrganicofflineTrainingExportHeader");
			}else if(getBranchId().equalsIgnoreCase("bci")){
				mainGridColumnHeaders = getLocaleProperty("BCIofflineTrainingExportHeader");
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
			headerStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
			headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cell.setCellStyle(headerStyle);
			headerStyle.setFont(headerFont);
			sheet.setColumnWidth(mainGridIterator, (15 * 450));
			mainGridIterator++;
		}


		
		Map data = isMailExport() ? readData() : readExportData();
		List<OfflineTrainingStatus> mainGridRows = (List<OfflineTrainingStatus>) data.get(ROWS);
		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;
		for (OfflineTrainingStatus offlineTrainingStatus : mainGridRows) {

			row = sheet.createRow(rowNum++);
			colNum = 0;

			/*if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
				if (StringUtil.isEmpty(branchIdValue)) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(offlineTrainingStatus.getBranchId())))
							? getBranchesMap().get(getParentBranchMap().get(offlineTrainingStatus.getBranchId()))
							: getBranchesMap().get(offlineTrainingStatus.getBranchId())));
				}
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(offlineTrainingStatus.getBranchId())));

			} else {
				if (StringUtil.isEmpty(branchIdValue)) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(offlineTrainingStatus.getBranchId())));
				}
			}*/
			
			serialNumber++;		
			cell = row.createCell(colNum++);		
			cell.setCellValue(new HSSFRichTextString(String.valueOf(serialNumber)!=null ? String.valueOf(serialNumber) : ""));	
			style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cell.setCellStyle(style1);
			
			if (StringUtil.isEmpty(branchIdValue)) {
				cell = row.createCell(colNum++);		
				cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(offlineTrainingStatus.getBranchId())));		
					
			}
		
			
			
			cell = row.createCell(colNum++);
			
			ESESystem preferences = preferncesService.findPrefernceById("1");
			DateFormat outputFormat = new SimpleDateFormat(
					preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
			DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			SimpleDateFormat df = new SimpleDateFormat(
					preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
			
			try {
				cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(offlineTrainingStatus.getTrainingDate())
						? outputFormat.format(inputFormat.parse(offlineTrainingStatus.getTrainingDate())) : ""));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(offlineTrainingStatus.getAgentId())
					? offlineTrainingStatus.getAgentId() : getLocaleProperty("NA")));

			

			Warehouse wh = locationService.findWarehouseByCode(offlineTrainingStatus.getLearningGroupCode());
			
			cell = row.createCell(colNum++);
			/*cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(offlineTrainingStatus.getLearningGroupCode())
					? offlineTrainingStatus.getLearningGroupCode() : getLocaleProperty("NA"))+" - "+wh.getName());*/
			cell.setCellValue(new HSSFRichTextString(wh!=null && !StringUtil.isEmpty(wh)?wh.getName():""));

			
			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(offlineTrainingStatus.getReceiptNo())
					? offlineTrainingStatus.getReceiptNo() : getLocaleProperty("NA")));

			/*cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(offlineTrainingStatus.getFarmerAttended())
					? offlineTrainingStatus.getFarmerAttended() : getLocaleProperty("NA")));*/

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(offlineTrainingStatus.getRemarks())
					? offlineTrainingStatus.getRemarks() : getLocaleProperty("NA")));

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(offlineTrainingStatus.getStatusMessage())
					? offlineTrainingStatus.getStatusMessage() : getLocaleProperty("NA")));

		}
/*
		for (int i = 0; i <= colNum; i++) {		
			sheet.autoSizeColumn(i);		
		}*/
		
		int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		/*picture.resize();*/
		String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
				: request.getSession().getId();
		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("offlineTrainingStatusReportFileName") + fileNameDateFormat.format(new Date())
				+ ".xls";
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

		if (picData != null)
			index = wb.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
	}

	/**
	 * Gets the filter.
	 * 
	 * @return the filter
	 */
	public OfflineTrainingStatus getFilter() {

		return filter;
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
	 * Gets the xls file name.
	 * 
	 * @return the xls file name
	 */
	public String getXlsFileName() {

		return xlsFileName;
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

	public IProductDistributionService getProductDistributionService() {

		return productDistributionService;
	}

	public void setProductDistributionService(IProductDistributionService productDistributionService) {

		this.productDistributionService = productDistributionService;
	}

	public String getBranchIdParma() {
		return branchIdParma;
	}

	public void setBranchIdParma(String branchIdParma) {
		this.branchIdParma = branchIdParma;
	}

	public String getDaterange() {
		return daterange;
	}

	public void setDaterange(String daterange) {
		this.daterange = daterange;
	}

	public String getExportLimit() {
		return exportLimit;
	}

	public void setExportLimit(String exportLimit) {
		this.exportLimit = exportLimit;
	}

	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}

	public Map<String, String> getFields() {
		/**
		 * fields.add(getText("trainingDate")); if
		 * (ObjectUtil.isEmpty(getBranchId())) {
		 * fields.add(getText("app.branch")); }
		 */

		fields.put("1", getText("trainingDate"));
		
		/*if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			fields.put("3", getText("app.branch"));
		} else if (StringUtil.isEmpty(getBranchId())) {
			fields.put("2", getText("app.branch"));
		}*/
		
		if (getIsMultiBranch().equalsIgnoreCase("1")){
			if(StringUtil.isEmpty(getBranchId())){
				fields.put("3", getText("app.branch"));
			}else if(getIsParentBranch().equals("1")) {
				fields.put("3", getText("app.subBranch"));
				}
			
		} else if (StringUtil.isEmpty(getBranchId())) {
		
			fields.put("2", getText("app.branch"));
		}
		return fields;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}

}
