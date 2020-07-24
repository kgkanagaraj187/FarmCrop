package com.sourcetrace.esesw.view.report.agro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
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
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.AgentAccessLog;
import com.sourcetrace.eses.order.entity.txn.Sensitizing;
import com.sourcetrace.eses.order.entity.txn.SensitizingImg;
import com.sourcetrace.eses.service.CatalogueService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

public class SensitizingReportAction extends BaseReportAction implements IExporter {

	private String imagesId;
	private Map<String, String> fields = new HashMap<>();

	@Autowired
	private IClientService clientService;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private IPreferencesService preferncesService;
	private String selectedSamithi;
	private Sensitizing filter;
	private String xlsFileName;
	private InputStream fileInputStream;
	private String exportLimit;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	
	public String list() {
		return LIST;
	}

	public String data() throws Exception {
		this.filter = new Sensitizing(); 
		if(!StringUtil.isEmpty(getSelectedSamithi())){
			this.filter.setGroupId(getSelectedSamithi());
		}
		
		super.filter = this.filter;
		return sendJSONResponse(readData());
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object record) {
		Sensitizing sensitizing = (Sensitizing) record;
		JSONObject jsonObject = new JSONObject();
		if (!ObjectUtil.isEmpty(sensitizing)) {
			JSONArray rows = new JSONArray();

			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(!StringUtil
							.isEmpty(getBranchesMap().get(getParentBranchMap().get(sensitizing.getBranchId())))
									? getBranchesMap().get(getParentBranchMap().get(sensitizing.getBranchId()))
									: getBranchesMap().get(sensitizing.getBranchId()));
				}
				rows.add(getBranchesMap().get(sensitizing.getBranchId()));

			} else {
				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(branchesMap.get(sensitizing.getBranchId()));
				}
			}
			ESESystem preferences = preferncesService.findPrefernceById("1");
			if (!StringUtil.isEmpty(preferences)) {
			rows.add(sensitizing.getGroupName());
			if(!getCurrentTenantId().equalsIgnoreCase("olivado")){
				rows.add(!StringUtil.isEmpty(sensitizing.getVillage())?locationService.findVillageByCode(sensitizing.getVillage()).getName():"");
			}
			
			rows.add(sensitizing.getFarmerCount());
			rows.add(sensitizing.getRemarks());
			

			if ((!StringUtil.isEmpty(sensitizing.getLatitude()) && !StringUtil.isEmpty(sensitizing.getLongitude()))) {
				rows.add("<button class='faMap' title='" + getText("farm.map.available.title")
						+ "' onclick='showFarmMap(\""
						+ (!StringUtil.isEmpty(sensitizing.getLatitude()) ? sensitizing.getLatitude() : "") + "\",\""
						+ (!StringUtil.isEmpty(sensitizing.getLongitude()) ? sensitizing.getLongitude() : "")
						+ "\")'></button>");
			} else {
				// No Latlon
				rows.add(
						"<button class='no-latLonIcn' title='" + getText("farm.map.unavailable.title") + "'></button>");
			}

			if (!ObjectUtil.isListEmpty(sensitizing.getSentizingImages())) {
				imagesId = "";
				sensitizing.getSentizingImages().stream().forEach(sensitizingImg -> {
					imagesId += String.valueOf(sensitizingImg.getId()) + ",";
				});
				rows.add("<button class=\"fa fa-picture-o\"\"aria-hidden=\"true\"\" onclick=\"popupWindow('"
						+ StringUtil.removeLastComma(imagesId) + "')\"></button>");
			} else {
				rows.add("<button class='no-imgIcn'></button>");
			}
			}
			jsonObject.put("id", sensitizing.getId());
			jsonObject.put("cell", rows);
			return jsonObject;
		}
		return jsonObject;
	}

	public String populateImage() {

		try {
			setId(id);
			SensitizingImg imageDetail = clientService.findSensitizingImgById(Long.valueOf(id));
			byte[] image = imageDetail.getPhoto();
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

	public Map<String, String> getFields() {
		fields.put("1", getText("profile.samithi"));
		return fields;
	}

/*	public Map<String, String> getSamithis() {
		return locationService.listSamithiesBasedOnType().stream()
				.collect(Collectors.toMap(Warehouse::getCode, Warehouse::getName));
	}
*/
	public void populateGroupList(){
		JSONArray warehouseArr = new JSONArray();
		List<Warehouse> warehouseList = locationService.listSamithiesBasedOnType();
		if (!ObjectUtil.isEmpty(warehouseList)) {
			warehouseList.forEach(obj -> {
				warehouseArr.add(getJSONObject(obj.getCode(), obj.getName()));
			});
		}
		sendAjaxResponse(warehouseArr);
	}

	
	public String getSelectedSamithi() {
		return selectedSamithi;
	}

	public void setSelectedSamithi(String selectedSamithi) {
		this.selectedSamithi = selectedSamithi;
	}

	public Sensitizing getFilter() {
		return filter;
	}

	public void setFilter(Sensitizing filter) {
		this.filter = filter;
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

	public String getExportLimit() {
		return exportLimit;
	}

	public void setExportLimit(String exportLimit) {
		this.exportLimit = exportLimit;
	}

	public String populateXLS() throws Exception {

		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getText("SensitizingReport") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("sensitizingReport"), fileMap, ".xls"));
		return "xls";
	}
	
	public InputStream getExportDataStream(String exportType) throws IOException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		setsDate(IExporter.EXPORT_DAILY.equalsIgnoreCase(exportType) ? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));
		boolean flag = true;
		DateFormat filterDateFormat = new SimpleDateFormat(FILTERDATE);

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("sensitizingReportList"));
		HSSFPatriarch drawing = sheet.createDrawingPatriarch();

		HSSFCellStyle style1 = wb.createCellStyle();
		HSSFCellStyle style2 = wb.createCellStyle();
		HSSFCellStyle style3 = wb.createCellStyle();
		HSSFCellStyle style4 = wb.createCellStyle();

		HSSFCellStyle alignStyle = wb.createCellStyle();
		alignStyle.setAlignment(alignStyle.ALIGN_RIGHT);
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

		int rowNum = 2;
		int colNum = 0;

		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap(); // build branch map to get branch name form branch id.
		
		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, titleRow1, titleRow2));


		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(2);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("sensitizingReportList")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);

		if (isMailExport()) {
			rowNum++;
			rowNum++;
			if (!StringUtil.isEmpty(getSelectedSamithi())){
			filterRowTitle = sheet.createRow(rowNum++);
			cell = filterRowTitle.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("filter")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);
			}
			
			if (!StringUtil.isEmpty(getSelectedSamithi())) {
				filterRow1 = sheet.createRow(rowNum++);
				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("group")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
				cell = filterRow1.createCell(2);
				Warehouse samithi = locationService.findSamithiByCode(getSelectedSamithi());
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(samithi)?samithi.getName():""));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			//filterRow5 = sheet.createRow(rowNum++);
			//filterRow6 = sheet.createRow(rowNum++);
		}
		rowNum++;

		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		
		String mainGridColumnHeaders;
		if(StringUtil.isEmpty(getBranchId())){
		mainGridColumnHeaders = getLocaleProperty("exportColumnHeaderSensitizingReportBranch");
		}
		else{
			mainGridColumnHeaders = getLocaleProperty("exportColumnHeaderSensitizingReport");
		}
		int mainGridIterator = 0;
		ESESystem preferences = preferncesService.findPrefernceById("1");
		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {
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

			 
		this.filter = new Sensitizing(); 
		if(!StringUtil.isEmpty(getSelectedSamithi())){
			this.filter.setGroupId(getSelectedSamithi());
		}
		super.filter = this.filter;

		Map data = isMailExport() ? readData() : readExportData();
		List<Sensitizing> mainGridRows = (List<Sensitizing>) data.get(ROWS);
		
		Long serialNumber = 0L;

		
		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;
		for (Sensitizing sensitizing : mainGridRows) {
			if (isMailExport() && flag) {
					row = sheet.createRow(rowNum++);
					colNum = 0;

					
					serialNumber++;
					cell = row.createCell(colNum++);
					style4.setAlignment(CellStyle.ALIGN_CENTER);
					cell.setCellStyle(style4);
					cell.setCellValue(serialNumber);
					
					
					if ((getIsMultiBranch().equalsIgnoreCase("1")
							&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
						if (StringUtil.isEmpty(branchIdValue)) {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(!StringUtil
									.isEmpty(getBranchesMap().get(getParentBranchMap().get(sensitizing.getBranchId())))
											? getBranchesMap().get(getParentBranchMap().get(sensitizing.getBranchId()))
											: getBranchesMap().get(sensitizing.getBranchId())));
						}
						cell = row.createCell(colNum++);
						cell.setCellValue(getBranchesMap().get(sensitizing.getBranchId()));

					} else {
						if (StringUtil.isEmpty(branchIdValue)) {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(sensitizing.getBranchId())
									? (branchesMap.get(sensitizing.getBranchId())) : ""));
						}
					}
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!StringUtil
							.isEmpty(sensitizing.getGroupName()) ?sensitizing.getGroupName():"NA"));
					
					if(!getCurrentTenantId().equalsIgnoreCase("olivado"))
					{	
						cell=row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(sensitizing.getVillage())?locationService.findVillageByCode(sensitizing.getVillage()).getName():""));	
					}
					
					/*cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!StringUtil
							.isEmpty(sensitizing.getFarmerCount()) ?sensitizing.getFarmerCount():"NA"));
					style3.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					cell.setCellStyle(style3);*/
						
					cell = row.createCell(colNum++);
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
 					cell.setCellValue(Double.valueOf(sensitizing.getFarmerCount()));
 					style3.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
 					cell.setCellStyle(style3);
					
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!StringUtil
							.isEmpty(sensitizing.getRemarks()) ?sensitizing.getRemarks():"NA"));

			}

			
		}
		
		/*for (int i = 0; i <= colNum; i++) {
			sheet.autoSizeColumn(i);
		}
		*/
		
		int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		// picture.resize();
		String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
				: request.getSession().getId();
		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("sensitizingReportList") + fileNameDateFormat.format(new Date()) + ".xls";
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

	
	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}

}
