/*
 * FarmerFailedInspectionReportAction.java
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
import com.sourcetrace.eses.inspect.agrocert.CertificateCategory;
import com.sourcetrace.eses.service.ICertificationService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.txn.agrocert.FailedQuestions;
import com.sourcetrace.eses.txn.agrocert.FarmerFailedQuestions;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

/**
 * The Class FarmerFailedInspectionReportAction.
 */
@SuppressWarnings("serial")
public class FarmerFailedInspectionReportAction extends BaseReportAction {

    private static final String CATEGORY_CODE_FAIR_TRADE = "CC001";
    private static final String CATEGORY_CODE_ORGANIC = "CC002";
    private static final String CATEGORY_CODE_NPOP = "CC003";
    private static final String CATEGORY_CODE_ICS = "CC004";
    private static final String FAIR_TRADE_ANSWER_TYPE = "2";
    private static final String FAIR_TRADE_ANSWER = "24";
    private static final String ORGANIC_ANSWER_TYPE = "0";
    private static final String ORGANIC_ANSWER = "0";
    private static final String NPOP_ANSWER_TYPE = "2";
    private static final String NPOP_ANSWER = "0";
    private static final String ICS_ANSWER_TYPE = "2";
    private static final String ICS_ANSWER = "0";

    DateFormat df = new SimpleDateFormat(getESEDateFormat());
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private ICertificationService certificationService;
    private IFarmerService farmerService;

    private FarmerFailedQuestions filter;
    private FarmerFailedQuestions farmerFailedQuestions;

    private String farmerId;
    private String selectedCategoryCode;
    private String userName;
    private String branchIdParma;

    protected List<String> fields = new ArrayList<String>();
    List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
    List<CertificateCategory> listCertificateCategory = new ArrayList<CertificateCategory>();
    private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private String xlsFileName;
    private InputStream fileInputStream;
    /**
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
       // fields.add(getText("farmer"));
        setFilter(farmerFailedQuestions);
     //   setSelectedCategoryCode(CATEGORY_CODE_FAIR_TRADE);
      //  loadProcedure();
        return LIST;
    }

    /**
     * Data.
     * @return the string
     * @throws Exception the exception
     */
    @SuppressWarnings("unchecked")
    public String data() throws Exception {

        this.filter = new FarmerFailedQuestions();
        this.filter.setUserName(getUserName());
        
        if (!StringUtil.isEmpty(branchIdParma)) {
            filter.setBranchId(branchIdParma.trim());
        }
      /*  if (!StringUtil.isEmpty(farmerId)) {
            Farmer farmer = new Farmer();
            filter.setFarmerId(farmerId);
           
        }*/
        super.filter = this.filter;

        Map data = readData();

        return sendJSONResponse(data);
    }

    /**
     * Data load.
     */
    public void dataLoad() {

        loadProcedure();

        try {
            response.setContentType("text/html");
            response.getWriter().write("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Load procedure.
     */
    private void loadProcedure() {

        if (selectedCategoryCode.equals(CATEGORY_CODE_FAIR_TRADE)) {

            callProcedure(CATEGORY_CODE_FAIR_TRADE, FAIR_TRADE_ANSWER_TYPE, FAIR_TRADE_ANSWER,
                    getUserName(), farmerId, sdf.format(getsDate()), sdf.format(geteDate()));
        } else if (selectedCategoryCode.equals(CATEGORY_CODE_ORGANIC)) {
            callProcedure(CATEGORY_CODE_ORGANIC, ORGANIC_ANSWER_TYPE, ORGANIC_ANSWER,
                    getUserName(), farmerId, sdf.format(getsDate()), sdf.format(geteDate()));

        } else if (selectedCategoryCode.equals(CATEGORY_CODE_NPOP)) {
            callProcedure(CATEGORY_CODE_NPOP, NPOP_ANSWER_TYPE, NPOP_ANSWER, getUserName(),
                    farmerId, sdf.format(getsDate()), sdf.format(geteDate()));
        }else if (selectedCategoryCode.equals(CATEGORY_CODE_ICS)) {
            callProcedure(CATEGORY_CODE_ICS, ICS_ANSWER_TYPE, ICS_ANSWER, getUserName(),
                    farmerId, sdf.format(getsDate()), sdf.format(geteDate()));
        }

    }

    /**
     * Call procedure.
     * @param categoryCode the category code
     * @param answerType the answer type
     * @param answer the answer
     * @param userName the user name
     * @param farmerId the farmer id
     * @param startDate the start date
     * @param endDate the end date
     */
    private void callProcedure(String categoryCode, String answerType, String answer,
            String userName, String farmerId, String startDate, String endDate) {

        certificationService.loadFarmerFailedQuestionProcedure(categoryCode, answerType, answer,
                userName, farmerId, startDate, endDate);

    }

    /**
     * @see com.sourcetrace.esesw.view.BaseReportAction#toJSON(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJSON(Object obj) {

        JSONObject jsonObject = new JSONObject();
        FarmerFailedQuestions farmerFailedQuestionsObj = (FarmerFailedQuestions) obj;

        JSONArray rows = new JSONArray();
        if (StringUtil.isEmpty(branchIdValue)) {
            rows.add(branchesMap.get(farmerFailedQuestionsObj.getBranchId()));
        }
        rows.add(farmerFailedQuestionsObj.getFarmerId());
        rows.add(farmerFailedQuestionsObj.getSerialNo());

        rows.add(farmerFailedQuestionsObj.getQuestionName());
        rows.add(farmerFailedQuestionsObj.getFailedCount());
        jsonObject.put("id", farmerFailedQuestionsObj.getId());

        jsonObject.put("cell", rows);
        return jsonObject;
    }

    /**
     * Gets the farmer id.
     * @return the farmer id
     */
    public String getFarmerId() {

        return farmerId;
    }

    /**
     * Sets the farmer id.
     * @param farmerId the new farmer id
     */
    public void setFarmerId(String farmerId) {

        this.farmerId = farmerId;
    }

    /**
     * Gets the filter.
     * @return the filter
     */
    public FarmerFailedQuestions getFilter() {

        return filter;
    }

    /**
     * Sets the filter.
     * @param filter the new filter
     */
    public void setFilter(FarmerFailedQuestions filter) {

        this.filter = filter;
    }

    /**
     * Gets the farmer failed questions.
     * @return the farmer failed questions
     */
    public FarmerFailedQuestions getFarmerFailedQuestions() {

        return farmerFailedQuestions;
    }

    /**
     * Sets the farmer failed questions.
     * @param farmerFailedQuestions the new farmer failed questions
     */
    public void setFarmerFailedQuestions(FarmerFailedQuestions farmerFailedQuestions) {

        this.farmerFailedQuestions = farmerFailedQuestions;
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

    /**
     * Gets the certification service.
     * @return the certification service
     */
    public ICertificationService getCertificationService() {

        return certificationService;
    }

    /**
     * Sets the certification service.
     * @param certificationService the new certification service
     */
    public void setCertificationService(ICertificationService certificationService) {

        this.certificationService = certificationService;
    }

    /**
     * Gets the certificate category list.
     * @return the certificate category list
     */
    public Map<String, String> getCertificateCategoryList() {

        Map<String, String> categoryListMap = new LinkedHashMap<String, String>();

        List<CertificateCategory> categoryList = certificationService.listCertificateCategory();

        if (!ObjectUtil.isListEmpty(categoryList)) {
            for (CertificateCategory obj : categoryList) {             
                    categoryListMap.put(obj.getCode(), obj.getName());                
            }
        }
        return categoryListMap;

    }

    /**
     * Sets the list certificate category.
     * @param listCertificateCategory the new list certificate category
     */
    public void setListCertificateCategory(List<CertificateCategory> listCertificateCategory) {

        this.listCertificateCategory = listCertificateCategory;
    }

    /**
     * Gets the selected category code.
     * @return the selected category code
     */
    public String getSelectedCategoryCode() {

        return selectedCategoryCode;
    }

    /**
     * Sets the selected category code.
     * @param selectedCategoryCode the new selected category code
     */
    public void setSelectedCategoryCode(String selectedCategoryCode) {

        this.selectedCategoryCode = selectedCategoryCode;
    }

    /**
     * Gets the user name.
     * @return the user name
     */
    public String getUserName() {

        userName = request.getSession().getAttribute("user").toString();
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
     * Gets the farmer service.
     * @return the farmer service
     */
    public IFarmerService getFarmerService() {

        return farmerService;
    }

    /**
     * Sets the farmer service.
     * @param farmerService the new farmer service
     */
    public void setFarmerService(IFarmerService farmerService) {

        this.farmerService = farmerService;
    }

    /**
     * Gets the farmers list.
     * @return the farmers list
     */
    public Map<String, String> getFarmersList() {

        Map<String, String> farmerListMap = new LinkedHashMap<String, String>();

        List<Farmer> farmersList = farmerService.listFarmer();

        for (Farmer obj : farmersList) {
            farmerListMap.put(obj.getFarmerId(), obj.getFirstName() + " " + obj.getLastName()
                    + " - " + obj.getFarmerId());
        }
        return farmerListMap;

    }
    
    public String populateXLS() throws Exception {
		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getText("FailedInspectionQuestionsReport") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("farmerFailedInspectionQuestionsReport"), fileMap, ".xls"));
		return "xls";
	}
	
    int serialNo = 1;
	 public InputStream getExportDataStream(String exportType) throws IOException {
		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		boolean flag = true;
		DateFormat filterDateFormat = new SimpleDateFormat(getGeneralDateFormat());
		DecimalFormat df = new DecimalFormat("0.000");
		DecimalFormat df1 = new DecimalFormat("0.00");

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("farmerFailedInspectionQuestionsReportTitle"));
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
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmerFailedInspectionQuestionsReportTitle")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);
	
		
		 this.filter = new FarmerFailedQuestions();
	        this.filter.setUserName(getUserName());
	        
	        if (!StringUtil.isEmpty(branchIdParma)) {
	            filter.setBranchId(branchIdParma.trim());
	        }
	        
	        super.filter = this.filter;
	      
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
		String mainGridColumnHeaders = getLocaleProperty("farmerFailedInspectionQuestionReport");
		
					 
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
		 
		
		
         
		List<FarmerFailedQuestions> dfata = (List<FarmerFailedQuestions>) data.get(ROWS);
		if (ObjectUtil.isListEmpty(dfata)) 
			  return null;
			for (FarmerFailedQuestions failedQuestions : dfata) {
				
				 row = sheet.createRow(rowNum++);
		         colNum = 0;
		         
		         
		         cell = row.createCell(colNum++);
					style4.setAlignment(CellStyle.ALIGN_CENTER);
					cell.setCellStyle(style4);
					cell.setCellValue(serialNo ++);
					
				if (StringUtil.isEmpty(branchIdValue)) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(branchesMap.get(failedQuestions.getBranchId())));
				}
				
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(failedQuestions.getFarmerId()));
				
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
		String fileName = getLocaleProperty("farmerFailedInspectionQuestionsReportTitle") + fileNameDateFormat.format(new Date()) + ".xls";
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
	

    public String getBranchIdParma() {

        return branchIdParma;
    }

    public void setBranchIdParma(String branchIdParma) {

        this.branchIdParma = branchIdParma;
    }

	public DateFormat getDf() {
		return df;
	}

	public void setDf(DateFormat df) {
		this.df = df;
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
