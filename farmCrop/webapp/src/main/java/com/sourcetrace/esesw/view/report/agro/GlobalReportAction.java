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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

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

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.order.entity.txn.AgentAccessLog;
import com.sourcetrace.eses.order.entity.txn.GlobalReport;
import com.sourcetrace.eses.order.entity.txn.Sensitizing;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;
import com.sourcetrace.esesw.view.general.VillageAction;

public class GlobalReportAction extends BaseReportAction {

	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(GlobalReportAction.class);
	private GlobalReport filter;

	private IFarmerService farmerService;
	private IProductDistributionService productDistributionService;
	private IPreferencesService preferncesService;
	private String farmerId;
	private String seasonCode;
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	private Map<String, String> fields = new LinkedHashMap<String, String>();
	private InputStream fileInputStream;
	private String exportLimit;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private String xlsFileName;
	
	@Override
	public String list() throws Exception {
		fields.put("1", getText("season"));
		fields.put("2", getText("farmer"));

		filter = new GlobalReport();
		return LIST;
	}

	public String data() throws Exception {
		filter=new GlobalReport();
		if(!StringUtil.isEmpty(getFarmerId()))
			filter.setFarmerId(getFarmerId());
		if(!StringUtil.isEmpty(getSeasonCode())){
			filter.setSeasonCode(getSeasonCode());
		}/*else{
			HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
			Object branchObject = httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
			String currentBranch = ObjectUtil.isEmpty(branchObject) ? ESESystem.SYSTEM_ESE_NAME : branchObject.toString();
			ESESystem ese = preferncesService.findPrefernceByOrganisationId(currentBranch);
			String currentSeasonCode = "";
			if (!ObjectUtil.isEmpty(ese)) {
				ESESystem preference = preferncesService.findPrefernceById(String.valueOf(ese.getId()));
				currentSeasonCode = preference.getPreferences().get(ESESystem.CURRENT_SEASON_CODE);

				if (StringUtil.isEmpty(currentSeasonCode)) {
					currentSeasonCode = "";
				}
			}

			filter.setSeasonCode(currentSeasonCode);
			}*/
		super.filter = this.filter;
		Map data = readData("globalReport");
		return sendJSONResponse(data);
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		JSONObject jsonObject = new JSONObject();
		{
			Object[] globalReport = (Object[]) obj;
			JSONArray rows = new JSONArray();
			if (globalReport.length>0) {
				if(globalReport[2]!=null){
				/*String date=globalReport[2].toString();
				if(!StringUtil.isEmpty(date)){
				rows.add(date);//Date
				}*/
					ESESystem preferences = preferncesService.findPrefernceById("1");
					if (!StringUtil.isEmpty(preferences)) {
						DateFormat genDate = new SimpleDateFormat(
								preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
						rows.add(!StringUtil.isEmpty(genDate.format(globalReport[2])) ? genDate.format(globalReport[2]) : ""); // Trxn
					} 
				}
				else{
					rows.add("");
				}
				if(!StringUtil.isEmpty(globalReport[0])){
				HarvestSeason harvestSeason=productDistributionService.findHarvestSeasonBySeasonCode(globalReport[0].toString());
				rows.add(!ObjectUtil.isEmpty(harvestSeason)?harvestSeason.getName():"");//season
				}else{
					rows.add("");//season
				}
				
				rows.add(String.valueOf(globalReport[1]));//farmerId
				Warehouse samithi=farmerService.findSamithiByFarmerId(!ObjectUtil.isEmpty(globalReport[1].toString())?globalReport[1].toString():"");
				//rows.add(String.valueOf(globalReport[]));
				//rows.add(String.valueOf(globalReport[3]));//farmerName
				Farmer f = farmerService.findFarmerByFarmerId(String.valueOf(globalReport[1].toString()));
				String firstName =  String.valueOf(globalReport[3]);
				String linkField = "<a href=farmer_detail.action?id="+f.getId()+" target=_blank>"+firstName+"</a>";
				rows.add(!ObjectUtil.isEmpty(linkField) ? linkField : "");// Farmer
				rows.add(!ObjectUtil.isEmpty(samithi)?samithi.getName():"");
				rows.add(String.valueOf(globalReport[4]));//Dis
				rows.add(String.valueOf(globalReport[5]));//proc
				//rows.add(String.valueOf(globalReport[5]));
				jsonObject.put("id", String.valueOf(globalReport[1]));
				jsonObject.put("cell", rows);
			}
		}
		return jsonObject;
	}
	public Map<String, String> getSeasonList() {

		Map<String, String> seasonListListMap = new LinkedHashMap<String, String>();

		List<HarvestSeason> seasonList = farmerService.listHarvestSeasons();

		for (HarvestSeason obj : seasonList) {
			seasonListListMap.put(obj.getCode(), obj.getName());
		}
		return seasonListListMap;

	}
	/*public Map<String, String> getFarmersList() {

		Map<String, String> farmerListMap = new LinkedHashMap<String, String>();

		List<Farmer> farmersList = farmerService.listFarmer();

		for (Farmer obj : farmersList) {
			farmerListMap.put(obj.getFarmerId(),
					obj.getFirstName());
		}
		return farmerListMap;

	}*/
	public GlobalReport getFilter() {
		return filter;
	}

	public void setFilter(GlobalReport filter) {
		this.filter = filter;
	}

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	public IProductDistributionService getProductDistributionService() {
		return productDistributionService;
	}

	public void setProductDistributionService(IProductDistributionService productDistributionService) {
		this.productDistributionService = productDistributionService;
	}

	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}

	public Map<String, String> getFields() {
		return fields;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}

	public String getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}

	public String getSeasonCode() {
		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
	}

	public String populateXLS() throws Exception {

		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getText("GlobalReport") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("globalReport"), fileMap, ".xls"));
		return "xls";
	}

	public int getPicIndex(HSSFWorkbook wb) throws IOException {

		int index = -1;

		byte[] picData = null;
		picData = clientService.findLogoByCode(Asset.APP_LOGO);

		if (picData != null)
			index = wb.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
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
		AgentAccessLog agentAccessLog = new AgentAccessLog();

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("globalReportList"));
		HSSFPatriarch drawing = sheet.createDrawingPatriarch();

		HSSFCellStyle style1 = wb.createCellStyle();
		HSSFCellStyle style2 = wb.createCellStyle();
		HSSFCellStyle style3 = wb.createCellStyle();
		HSSFCellStyle style4 = wb.createCellStyle();
		HSSFCellStyle style5 = wb.createCellStyle();
		
		HSSFCellStyle alignStyle = wb.createCellStyle();
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
		int titleRow1 = 1;
		int titleRow2 = 5;

		int rowNum = 3;
		int colNum = 0;

		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap(); // build branch map to get branch name form branch id.

/*		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(2);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("globalReportList")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);*/
		
		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, titleRow1, titleRow2));

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(1);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("globalReportList")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);

		
		if (isMailExport()) {
			rowNum++;
			rowNum++;
			if (!StringUtil.isEmpty(getFarmerId()) || !StringUtil.isEmpty(getSeasonCode())) {
			filterRowTitle = sheet.createRow(rowNum++);
			cell = filterRowTitle.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("filter")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);
			}
			
			if (!StringUtil.isEmpty(getFarmerId())) {
				filterRow1 = sheet.createRow(rowNum++);
				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmer")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
				cell = filterRow1.createCell(2);
				Farmer farmer =farmerService.findFarmerByFarmerId(getFarmerId());
				cell.setCellValue(new HSSFRichTextString(farmer.getName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			
			if (!StringUtil.isEmpty(getSeasonCode())) {
				filterRow1 = sheet.createRow(rowNum++);
				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("season")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
				cell = filterRow1.createCell(2);
				HarvestSeason harvestSeason=productDistributionService.findHarvestSeasonBySeasonCode(getSeasonCode());
				cell.setCellValue(new HSSFRichTextString(harvestSeason.getName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			filterRow5 = sheet.createRow(rowNum++);
			filterRow6 = sheet.createRow(rowNum++);
		}
		rowNum++;

		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		String mainGridColumnHeaders = getText("exportColumnHeaderGlobalReport");
		int mainGridIterator = 0;
		ESESystem preferences = preferncesService.findPrefernceById("1");
		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {
			if(cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT))
			{
				cellHeader=cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
			}
			else
			{
				cellHeader=cellHeader.trim();
			}
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

		filter=new GlobalReport();
		if(!StringUtil.isEmpty(getFarmerId()))
			filter.setFarmerId(getFarmerId());
		if(!StringUtil.isEmpty(getSeasonCode())){
			filter.setSeasonCode(getSeasonCode());
		}else{
			HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
			Object branchObject = httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
			String currentBranch = ObjectUtil.isEmpty(branchObject) ? ESESystem.SYSTEM_ESE_NAME : branchObject.toString();
			ESESystem ese = preferncesService.findPrefernceByOrganisationId(currentBranch);
			String currentSeasonCode = "";
			if (!ObjectUtil.isEmpty(ese)) {
				ESESystem preference = preferncesService.findPrefernceById(String.valueOf(ese.getId()));
				currentSeasonCode = preference.getPreferences().get(ESESystem.CURRENT_SEASON_CODE);

				if (StringUtil.isEmpty(currentSeasonCode)) {
					currentSeasonCode = "";
				}
			}

			filter.setSeasonCode(currentSeasonCode);
			}
		super.filter = this.filter;
		Map data = readData("globalReport");
		List<GlobalReport[]> mainGridRows = (List<GlobalReport[]>) data.get(ROWS);
		
		
		Long serialNumber = 0L;

		
		for (Object[]  globalReport : mainGridRows) {
			if (isMailExport() && flag) {
			
					row = sheet.createRow(rowNum++);
					colNum = 0;
					

					serialNumber++;
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(String.valueOf(serialNumber)!=null ? String.valueOf(serialNumber) : ""));
					style5.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					cell.setCellStyle(style5);
					

					/*ESESystem preference = preferncesService.findPrefernceById("1");
					if (!StringUtil.isEmpty(preference)) {
						DateFormat genDate = new SimpleDateFormat(
								preference.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(genDate.format(globalReport[2])) ? genDate.format(globalReport[2]) : "")); // Trxn
						
					} */
					if (!ObjectUtil.isEmpty(globalReport[2])) {
						if (!ObjectUtil.isEmpty(globalReport[2])) {
							ESESystem preference = preferncesService.findPrefernceById("1");
							if (!StringUtil.isEmpty(preference)) {
								DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
								cell = row.createCell(colNum++);
								cell.setCellValue(new HSSFRichTextString(genDate.format(globalReport[2])));
							}
						}
					}
					HarvestSeason harvestSeason=productDistributionService.findHarvestSeasonBySeasonCode(globalReport[0].toString());
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!StringUtil
							.isEmpty(harvestSeason) ?harvestSeason.getName():"NA"));
					
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!StringUtil
							.isEmpty(globalReport[1].toString()) ?globalReport[1].toString():"NA"));
					
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!StringUtil
							.isEmpty(globalReport[3].toString()) ?globalReport[3].toString():"NA"));
					
					Warehouse samithi=farmerService.findSamithiByFarmerId(globalReport[1].toString());
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!StringUtil
							.isEmpty(samithi) ?samithi.getName():"NA"));
					
					cell = row.createCell(colNum++);
					
					if(StringUtil.isEmpty(globalReport[4])){
						cell.setCellValue("");	
					}else{
						style4.setAlignment(alignStyle.ALIGN_RIGHT);
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellValue(Double.valueOf(globalReport[4].toString()));
					}
					
					cell.setCellStyle(style4);
					
					cell = row.createCell(colNum++);
	
					if(StringUtil.isEmpty(globalReport[5])){
						cell.setCellValue("");	
					}else{
						style3.setAlignment(alignStyle.ALIGN_RIGHT);
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellValue(Double.valueOf(globalReport[5].toString()));
					}
					cell.setCellStyle(style3);
			}

			
		}
		
		/*for (int i = 0; i <= colNum; i++) {
			sheet.autoSizeColumn(i);
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
		
		/*int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		// picture.resize();
		String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
				: request.getSession().getId();
		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("distributionReportList" + type) + fileNameDateFormat.format(new Date())
				+ ".xls";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		wb.write(fileOut);
		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		fileOut.close();

		return stream;*/

	

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

	public String getXlsFileName() {
		return xlsFileName;
	}

	public void setXlsFileName(String xlsFileName) {
		this.xlsFileName = xlsFileName;
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
	public void populateFarmerList(){
		JSONArray farmerArr = new JSONArray();
		String catCode;
		List<Object[]> fcpList =farmerService.listFarmerInfo();
		if (!ObjectUtil.isEmpty(fcpList)) {
			fcpList.forEach(obj -> {
				farmerArr.add(getJSONObject(obj[1].toString(), obj[3].toString()));
			});
		}
		sendAjaxResponse(farmerArr);
	}
	
}