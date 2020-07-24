/*
 * FailedInspectionQuestionsReportAction.java
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import org.apache.poi.ss.usermodel.CellStyle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.entity.WarehouseProduct;
import com.sourcetrace.eses.service.ICertificationService;
import com.sourcetrace.eses.txn.agrocert.FailedQuestions;
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

/**
 * The Class FailedInspectionQuestionsReportAction.
 */
@SuppressWarnings("unchecked")
public class FailedInspectionQuestionsReportAction extends BaseReportAction {

    private static final long serialVersionUID = 1L;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String FAIR_TRADE_CATG_CODE = "CC001";
    private static final String ORGANIC_CATG_CODE = "CC002";
    private static final String NPOP_CATG_CODE = "CC003";
    private static final String ICS_CATG_CODE = "CC004";
    private static final String FAIR_TRADE_ANSWER_TYPE = "2";
    private static final String FAIR_TRADE_ANSWER = "24";
    private static final String ORGANIC_ANSWER_TYPE = "0";
    private static final String ORGANIC_ANSWER = "0";
    private static final String NPOP_ANSWER_TYPE = "2";
    private static final String ICS_ANSWER_TYPE = "2";
    private static final String NPOP_ANSWER = "0";
    private static final String ICS_ANSWER = "0";
    private static final int ALL_DATA_LIMIT = 0;

    private String userName;
    private String selectedCatgCode;

    private FailedQuestions filter;
    private String branchIdParma;
    private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private String xlsFileName;
    private InputStream fileInputStream;
    private ICertificationService certificationService;

    private Map<String, String> categoryList = new LinkedHashMap();
    protected List<String> fields = new ArrayList<String>();

    /**
     * List.
     * @return the string
     * @throws Exception the exception
     * @see com.sourcetrace.esesw.view.BaseReportAction#list()
     */
    public String list() throws Exception {

        Calendar currentDate = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
        super.startDate = df.format(DateUtil.getFirstDateOfMonth(currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH)));
        super.endDate = df.format(currentDate.getTime());
        request.setAttribute(HEADING, getText(LIST));
        fields.add(getText("answeredDate"));
        if (ObjectUtil.isEmpty(getBranchId())) {
            fields.add(getText("app.branch"));
        }
      
       // selectedCatgCode = FAIR_TRADE_CATG_CODE;
       // loadProcedureParams();
        return LIST;
    }

    /**
     * Data.
     * @return the string
     * @throws Exception the exception
     */
    public String data() throws Exception {

        this.filter = new FailedQuestions();
        
        if (!StringUtil.isEmpty(branchIdParma)) {
            filter.setBranchId(branchIdParma.trim());
        }
        super.filter = this.filter;
        filter.setUserName(getUserName());
        Map data = readData();
        return sendJSONResponse(data);
    }

    /**
     * Data load.
     */
    public void dataLoad() {

        loadProcedureParams();
        try {
            response.setContentType("text/html");
            response.getWriter().write("");
        } catch (Exception e) {

        }
    }

    /**
     * Load procedure params.
     */
    public void loadProcedureParams() {

        if (selectedCatgCode.equals(FAIR_TRADE_CATG_CODE)) {
            callProcedure(FAIR_TRADE_CATG_CODE, FAIR_TRADE_ANSWER_TYPE, FAIR_TRADE_ANSWER);
        } else if (selectedCatgCode.equals(ORGANIC_CATG_CODE)) {
            callProcedure(ORGANIC_CATG_CODE, ORGANIC_ANSWER_TYPE, ORGANIC_ANSWER);
        } else if (selectedCatgCode.equals(NPOP_CATG_CODE)) {
            callProcedure(NPOP_CATG_CODE, NPOP_ANSWER_TYPE, NPOP_ANSWER);
        }else{
            callProcedure(ICS_CATG_CODE, ICS_ANSWER_TYPE, ICS_ANSWER);
        }
    }

    /**
     * Call procedure.
     * @param catgCode the catg code
     * @param ansType the ans type
     * @param ans the ans
     */
    public void callProcedure(String catgCode, String ansType, String ans) {

        certificationService.callFailedQuestionProcedure(catgCode, ansType, ans, sdf
                .format(getsDate()), sdf.format(geteDate()), getUserName());
    }

    /**
     * To json.
     * @param obj the obj
     * @return the JSON object
     * @see com.sourcetrace.esesw.view.BaseReportAction#toJSON(java.lang.Object)
     */
    public JSONObject toJSON(Object obj) {

        FailedQuestions failedQuestions = (FailedQuestions) obj;
        JSONObject jsonObject = new JSONObject();
        JSONArray rows = new JSONArray();
        if (StringUtil.isEmpty(branchIdValue)) {
            rows.add(branchesMap.get(failedQuestions.getBranchId()));
        }
        rows.add(failedQuestions.getSerialNo());
        rows.add(failedQuestions.getQuestionName());
        rows.add(failedQuestions.getFailedCount());
        jsonObject.put("id", failedQuestions.getId());
        jsonObject.put("cell", rows);

        return jsonObject;
    }

    /**
     * Gets the category list.
     * @return the category list
     */
    public Map<String, String> getCategoryList() {

        categoryList.put(FAIR_TRADE_CATG_CODE, getText("fairTrade"));
        categoryList.put(ORGANIC_CATG_CODE, getText("organic"));
        categoryList.put(NPOP_CATG_CODE, getText("npop"));
        categoryList.put(ICS_CATG_CODE, getText("ics"));
        return categoryList;
    }
    
    public String populateXLS() throws Exception {
		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getText("FailedInspectionQuestionsReport") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("failedInspectionQuestionsReport"), fileMap, ".xls"));
		return "xls";
	}
	int serialNo =1;
	 public InputStream getExportDataStream(String exportType) throws IOException {
		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		boolean flag = true;
		DateFormat filterDateFormat = new SimpleDateFormat(getGeneralDateFormat());
		DecimalFormat df = new DecimalFormat("0.000");
		DecimalFormat df1 = new DecimalFormat("0.00");

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("failedInspectionQuestionsReportTitle"));
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

		HSSFRow row, titleRow, filterRowTitle, filterRow1,filterRow2,filterRow3,filterRow4,filterRow5;
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

		sheet.setDefaultColumnWidth(13);

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(2);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("failedInspectionQuestionsReportTitle")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);
	
		
		   this.filter = new FailedQuestions();
	        
	        if (!StringUtil.isEmpty(branchIdParma)) {
	            filter.setBranchId(branchIdParma.trim());
	        }
	        
	        super.filter = this.filter;
	        filter.setUserName(getUserName());
	        
	        Map data = isMailExport() ? readData() : readExportData();
		
		
		if (isMailExport()) {
			
			rowNum++;
			rowNum++;
			if(!StringUtil.isEmpty(branchIdParma)){
			filterRowTitle = sheet.createRow(rowNum++);
			cell = filterRowTitle.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("filter")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);
			}
			/*filterRow1 = sheet.createRow(rowNum++);

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

			if (!ObjectUtil.isEmpty(geteDate())) {
				cell = filterRow2.createCell(2);
				cell.setCellValue(new HSSFRichTextString(filterDateFormat.format(geteDate())));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}*/
			
			 if(!StringUtil.isEmpty(branchIdParma)){
				   filterRow1 = sheet.createRow(rowNum++);
			    cell = filterRow1.createCell(1);
			    cell.setCellValue(new HSSFRichTextString(getText("organization")));
			    filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);


			    cell = filterRow1.createCell(2);
			    BranchMaster branch = clientService.findBranchMasterByBranchId(branchIdParma);
			    cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(branch) ? branch.getName() : "" )));
			    filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			   }
			 
		}
		
		rowNum++;
		
		
		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		String mainGridColumnHeaders = getLocaleProperty("failedInspectionQuestionReport");
		
					 
		int mainGridIterator = 0;
		
		 for (String cellHeader : mainGridColumnHeaders.split("\\,")) {

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
		 
		
		
         
		List<FailedQuestions> dfata = (List<FailedQuestions>) data.get(ROWS);
		if (ObjectUtil.isListEmpty(dfata)) 
			  return null;
			for (FailedQuestions failedQuestions : dfata) {
				
				 row = sheet.createRow(rowNum++);
		         colNum = 0;
		         
		         cell = row.createCell(colNum++);
					style4.setAlignment(CellStyle.ALIGN_CENTER);
					cell.setCellStyle(style4);
					cell.setCellValue(serialNo++);
					
		         
				if (StringUtil.isEmpty(branchIdValue)) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(branchesMap.get(failedQuestions.getBranchId())));
				}
				
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(failedQuestions.getSerialNo()));
				
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(failedQuestions.getQuestionName()));
				
				cell = row.createCell(colNum++);
				//cell.setCellValue(new HSSFRichTextString(String.valueOf(failedQuestions.getFailedCount())));
				
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				cell.setCellValue(Double.valueOf(failedQuestions.getFailedCount()));
				
			}
		
		
		int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		// picture.resize();
		String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
				: request.getSession().getId();
		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("failedInspectionQuestionsReportTitle") + fileNameDateFormat.format(new Date()) + ".xls";
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
	

    public ICertificationService getCertificationService() {

        return certificationService;
    }

    public void setCertificationService(ICertificationService certificationService) {

        this.certificationService = certificationService;
    }

    /**
     * Gets the filter.
     * @return the filter
     */
    public FailedQuestions getFilter() {

        return filter;
    }

    /**
     * Sets the filter.
     * @param filter the new filter
     */
    public void setFilter(FailedQuestions filter) {

        this.filter = filter;
    }

    /**
     * Gets the user name.
     * @return the user name
     */
    public String getUserName() {

        userName = (String) request.getSession().getAttribute("user");
        return userName;
    }

    /**
     * Sets the user name.
     * @param userName the new user name
     */
    public void setUserName(String userName) {

        this.userName = userName;
    }

    /**
     * Gets the selected catg code.
     * @return the selected catg code
     */
    public String getSelectedCatgCode() {

        return selectedCatgCode;
    }

    /**
     * Sets the selected catg code.
     * @param selectedCatgCode the new selected catg code
     */
    public void setSelectedCatgCode(String selectedCatgCode) {

        this.selectedCatgCode = selectedCatgCode;
    }

    /**
     * Sets the category list.
     * @param categoryList the category list
     */
    public void setCategoryList(Map<String, String> categoryList) {

        this.categoryList = categoryList;
    }

    /**
     * Gets the fields.
     * @return the fields
     */
    public List<String> getFields() {

        return fields;
    }

    /**
     * Sets the fields.
     * @param fields the new fields
     */
    public void setFields(List<String> fields) {

        this.fields = fields;
    }

    public String getBranchIdParma() {

        return branchIdParma;
    }

    public void setBranchIdParma(String branchIdParma) {

        this.branchIdParma = branchIdParma;
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

}
