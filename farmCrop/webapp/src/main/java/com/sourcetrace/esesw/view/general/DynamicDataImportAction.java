package com.sourcetrace.esesw.view.general;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

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
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.DynamicData;
import com.ese.entity.util.ESESystem;
import com.ese.entity.util.FarmerField;
import com.google.appengine.repackaged.com.google.gson.JsonObject;
import com.google.appengine.repackaged.com.google.gson.JsonParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.BankInformation;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmIcsConversion;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.State;
import com.sourcetrace.esesw.view.IExporter;
import com.sourcetrace.esesw.view.SwitchAction;

public class DynamicDataImportAction extends SwitchAction{
	
	@Autowired
	private IFarmerService farmerService;
	
	private String json_string;
	private String xlsFileName;
	private InputStream fileInputStream;



	public String list() {
		return LIST;
	}
	
	public void populateFarmerFields() {
		List<FarmerField> farmerFieldList = farmerService.listFarmerFields();
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		farmerFieldList.stream().filter(farmerField -> farmerField.getStatus() == 1).forEach(config->{
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("fieldName", config.getName());
			jsonList.add(jsonObj);
		});
		
		printAjaxResponse(jsonList, "text/html");
	}

	
	public String downloadSheet() {
	
		
		List<DynamicData> farmerFields = new ArrayList<DynamicData>();
		
		if (!StringUtil.isEmpty(getJson_string())) {
			GsonBuilder gsonBuilder = new GsonBuilder();
			Gson gson = gsonBuilder.create();
			farmerFields = Arrays.asList(gson.fromJson(getJson_string(), DynamicData[].class));
		}
		
		for (Iterator iterator = farmerFields.iterator(); iterator.hasNext();) {
			DynamicData dynamicData = (DynamicData) iterator.next();
			System.out.println(dynamicData.getFieldName()); 
			System.out.println(dynamicData.getFieldType()); 
			
		}
		
		try {
		String result =	populateXLS();
		return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return LIST;
		}
	}
	
	
	
	HSSFRow row, titleRow;
	HSSFCell cell;

	int rowNum = 3;
	int colNum = 0;
	
	private IPreferencesService preferncesService;
	
	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}



	//xls
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	public String populateXLS() throws Exception {
		response.setContentType("application/vnd.ms-excel");
		// response.setHeader("Content-Disposition",
		// "attachment;filename=" + getText("farmerList") +
		// fileNameDateFormat.format(new Date()) + ".xls");
		InputStream is = null;
		/*
		 * if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PGSS_TENANT_ID)
		 * && !StringUtil.isEmpty(getType()) && getType().equals(Farmer.IRP)) {
		 * is = getFarmerInputStream(); } else { is =
		 * getFarmerFileInputStream(); }
		 */
		is = getFarmerFileInputStream();
		setXlsFileName(getText("farmerList") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getLocaleProperty("farmerList"), fileMap, ".xls"));

		return "xls";
	}
	
	private InputStream getFarmerFileInputStream() throws IOException, ParseException {

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getText("exportXLSFarmerTitle"));
		HSSFPatriarch drawing = sheet.createDrawingPatriarch();

		HSSFCellStyle style1 = wb.createCellStyle();
		HSSFCellStyle style2 = wb.createCellStyle();

		HSSFFont font1 = wb.createFont();
		font1.setFontHeightInPoints((short) 22);

		HSSFFont font2 = wb.createFont();
		font2.setFontHeightInPoints((short) 12);

		HSSFFont font3 = wb.createFont();
		font3.setFontHeightInPoints((short) 10);

		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap();

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(3);
		cell.setCellValue(new HSSFRichTextString(getText("exportXLSFarmerTitle")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);

		sheet.addMergedRegion(new CellRangeAddress(3, 3, 3, 5));
		int imgRow1 = 0;
		int imgRow2 = 2;
		int imgCol1 = 0;
		int imgCol2 = 1;

		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));

		++rowNum;

		//ESESystem preferences = preferncesService.findPrefernceById("1");

		//setFarmerCodeEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE));

		/*
		 * String columnHeaders1 = getLocaleProperty("exportXLSFarmerHeading");
		 * 
		 * if (farmerCodeEnabled.equals("1")) { columnHeaders1 =
		 * getLocaleProperty("exportXLSFarmerHeading"); } else { columnHeaders1
		 * = getLocaleProperty("exportXLSFarmerHeading1"); }
		 * 
		 * if (getCurrentTenantId().equalsIgnoreCase("atma")) {
		 * 
		 * columnHeaders1 = getText("exportXLSFarmerHeadingapmas"); }
		 * 
		 * if (getCurrentTenantId().equalsIgnoreCase("blri")) {
		 * 
		 * columnHeaders1 = getText("exportXLSFarmerHeadingblri"); }
		 * 
		 * if ((getCurrentTenantId().equalsIgnoreCase("lalteer")) ||
		 * (getCurrentTenantId().equalsIgnoreCase("laleerqa"))) {
		 * 
		 * columnHeaders1 = getText("exportXLSFarmerHeadinglalteer"); } if
		 */
		rowNum += 2;
		HSSFRow rowHead1 = sheet.createRow(rowNum++);
		rowHead1.setHeight((short) 600);
		/*
		 * if (getCurrentTenantId().equalsIgnoreCase("kpf")) {
		 * 
		 * columnHeaders1 = getText("exportXLSFarmerHeadingkpf"); }
		 * 
		 * if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {
		 * 
		 * columnHeaders1 = getText("exportXLSFarmerHeadingPra" +
		 * getBranchId()); } if
		 * (getCurrentTenantId().equalsIgnoreCase("meridian")) {
		 * 
		 * columnHeaders1 = getText("exportXLSFarmerHeadingMeridian"); }
		 * 
		 * if ((getIsMultiBranch().equalsIgnoreCase("1") &&
		 * (getIsParentBranch().equals("1") ||
		 * StringUtil.isEmpty(branchIdValue)))) {
		 * 
		 * columnHeaders1 = getText("exportXLSFarmerHeadingSubOrg"); }
		 */

		String columnHeaders1 = null;

		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {
				columnHeaders1 = getLocaleProperty("exportFarmerHeadingBranch");
			} else if (!StringUtil.isEmpty(branchIdValue)) {
				columnHeaders1 = getLocaleProperty("exportFarmerHeading");
			}
		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				columnHeaders1 = getLocaleProperty("exportFarmerHeadingBranch");
			} else {
				columnHeaders1 = getLocaleProperty("exportFarmerHeading");
			}
		}
		if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {

			if (StringUtil.isEmpty(branchIdValue)) {
				columnHeaders1 = getLocaleProperty("exportFarmerHeadingOrganicPratibha");
			} else if (getBranchId().equalsIgnoreCase("organic")) {
				columnHeaders1 = getLocaleProperty("exportFarmerHeadingOrganic");
			} else if (getBranchId().equalsIgnoreCase("bci")) {
				columnHeaders1 = getLocaleProperty("exportFarmerHeadingBCI");
			}

		}
		 if (getCurrentTenantId().equalsIgnoreCase("welspun")) {

			if (StringUtil.isEmpty(branchIdValue)) {
				columnHeaders1 = getLocaleProperty("exportFarmerListColumnHeaderBranchWelspun");
			} else if (getIsParentBranch().equalsIgnoreCase("1")) {
				columnHeaders1 = getLocaleProperty("exportFarmerHeadingWelspunParent");
			} else  
				columnHeaders1 = getLocaleProperty("exportFarmerHeadingWelspun");
		}

		int iterator1 = 1;

		cell = rowHead1.createCell(0);
		cell.setCellValue(new HSSFRichTextString("SNO"));
		style2.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cell.setCellStyle(style2);
		font2.setBoldweight((short) 12);
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style2.setFont(font2);
		style2.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style2.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style2.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style2.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style2.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style2.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style2.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style2.setTopBorderColor(IndexedColors.BLACK.getIndex());

		for (String cellHeader : columnHeaders1.split("\\,")) {

			if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
			} else if (cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());
			}

			if (StringUtil.isEmpty(branchIdValue)) {
				cell = rowHead1.createCell(iterator1);
				cell.setCellValue(new HSSFRichTextString(cellHeader));
				style2.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
				style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				cell.setCellStyle(style2);
				font2.setBoldweight((short) 12);
				font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				style2.setFont(font2);
				if (iterator1 != 7) {
					sheet.setColumnWidth(iterator1, (15 * 450));
				}
				iterator1++;

			} else {
				if (!cellHeader.equalsIgnoreCase(getText("app.branch"))) {
					cell = rowHead1.createCell(iterator1);
					cell.setCellValue(new HSSFRichTextString(cellHeader));
					// style2.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
					// style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					cell.setCellStyle(style2);
					font2.setBoldweight((short) 12);
					font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					style2.setFont(font2);
					sheet.setColumnWidth(iterator1, (15 * 550));
					iterator1++;
				}
			}
		}

		List<Object[]> farmersList = farmerService.listFarmerInfo();
		// 0=Id,1=Farmer Id,2=Farmer Code,3=First Name,4=Last
		// name,5=surName,6=village name,7=Group name,8=Is certified
		// Farmer,9=Status,10=BranchId
		AtomicInteger snoCount = new AtomicInteger(0);
		farmersList.stream().forEach(farmer -> {
			row = sheet.createRow(rowNum++);
			row.setHeight((short) 400);
			colNum = 0;
			cell = row.createCell(colNum++);
			cell.setCellValue(snoCount.incrementAndGet());
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)) {
			if (StringUtil.isEmpty(branchIdValue)) {
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(farmer[10])))
						? getBranchesMap().get(getParentBranchMap().get(farmer[10]))
						: getBranchesMap().get(farmer[10])));
			}}
			else{
			if (StringUtil.isEmpty(branchIdValue)) {
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(
						!StringUtil.isEmpty(branchesMap.get(farmer[10])) ? (branchesMap.get(farmer[10])) : ""));
			}
			}
			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(farmer[10])));
			}

			

			/*
			 * if (farmerCodeEnabled.equals("1")) { cell =
			 * row.createCell(colNum++); cell.setCellValue( new
			 * HSSFRichTextString(StringUtil.isEmpty(farmer[2]) ? getText("NA")
			 * : farmer[2].toString())); }
			 */
			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(
					StringUtil.isEmpty(farmer[3]) ? getText("NA") : StringUtil.trim(farmer[3].toString())));

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(
					StringUtil.isEmpty(farmer[4]) ? getText("NA") : StringUtil.trim(farmer[4].toString())));

			

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(
					(ObjectUtil.isEmpty(farmer[6]) && farmer[6] == null) ? getText("NA") : farmer[6].toString()));
			cell = row.createCell(colNum++);
			cell.setCellValue(
					new HSSFRichTextString(ObjectUtil.isEmpty(farmer[7]) ? getText("NA") : farmer[7].toString()));
			
		});

		for (int i = 0; i <= colNum; i++) {
			sheet.autoSizeColumn(i);
		}

		// Add a picture
		int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		picture.resize(0.50);

		String makeDir = FileUtil.storeXls(request.getSession().getId());
		String fileName = getText("farmerList") + fileNameDateFormat.format(new Date()) + ".xls";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		wb.write(fileOut);
		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		fileOut.close();
		return stream;
	}
	
	public int getPicIndex(HSSFWorkbook wb) throws IOException {

		int index = -1;
		String roleName = (String) request.getSession().getAttribute("role");

		byte[] picData = null;

		picData = clientService.findLogoByCode(Asset.APP_LOGO);

		index = wb.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
	}
	
	
	//
	
	//Getters and setters
	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	public String getJson_string() {
		return json_string;
	}

	public void setJson_string(String json_string) {
		this.json_string = json_string;
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
	
	
	
	
	
}
