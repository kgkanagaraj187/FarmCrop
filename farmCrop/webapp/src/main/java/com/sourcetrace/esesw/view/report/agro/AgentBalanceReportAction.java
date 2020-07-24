/*
 * AgentBalanceReportAction.java
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
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.order.entity.txn.AgentBalanceReport;
import com.sourcetrace.eses.order.entity.txn.Contract;
import com.sourcetrace.eses.order.entity.txn.PaymentMode;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;
//import com.sourcetrace.esesw.view.report.util.BaseDynamicJasperReport;
//import com.sourcetrace.esesw.view.report.util.BaseDynamicJasperReport.ExportType;

public class AgentBalanceReportAction extends BaseReportAction implements IExporter{

    private static final long serialVersionUID = 1L;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private String profileId;
    private String gridIdentity;
    private String pdfFileName;

    private AgentBalanceReport filter;
    private IAgentService agentService;
    private IProductDistributionService productDistributionService;
    private IPreferencesService preferncesService;

    private String exportLimit;
	private InputStream fileInputStream;

    protected List<String> fields = new ArrayList<String>();
    Map<Integer, String> balanceTypeList = new HashMap<Integer, String>();

    private String xlsFileName;
    private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat(
            "yyyyMMddHHmmss");

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
     * Sets the filter.
     * @param filter the new filter
     */
    public void setFilter(AgentBalanceReport filter) {

        this.filter = filter;
    }

    /**
     * Gets the filter.
     * @return the filter
     */
    public AgentBalanceReport getFilter() {

        return filter;
    }

    /**
     * Sets the profile id.
     * @param profileId the new profile id
     */
    public void setProfileId(String profileId) {

        this.profileId = profileId;
    }

    /**
     * Gets the profile id.
     * @return the profile id
     */
    public String getProfileId() {

        return profileId;
    }

    /**
     * Sets the grid identity.
     * @param gridIdentity the new grid identity
     */
    public void setGridIdentity(String gridIdentity) {

        this.gridIdentity = gridIdentity;
    }

    /**
     * Gets the grid identity.
     * @return the grid identity
     */
    public String getGridIdentity() {

        return gridIdentity;
    }

    /**
     * Sets the pdf file name.
     * @param pdfFileName the new pdf file name
     */
    public void setPdfFileName(String pdfFileName) {

        this.pdfFileName = pdfFileName;
    }

    /**
     * Gets the pdf file name.
     * @return the pdf file name
     */
    public String getPdfFileName() {

        return pdfFileName;
    }

    /**
     * Sets the file input stream.
     * @param fileInputStream the new file input stream
     */
    public void setFileInputStream(InputStream fileInputStream) {

        this.fileInputStream = fileInputStream;
    }

    /**
     * Gets the file input stream.
     * @return the file input stream
     */
    public InputStream getFileInputStream() {

        return fileInputStream;
    }

    /**
     * @see com.sourcetrace.esesw.view.BaseReportAction#list()
     */
    @Override
    public String list() throws Exception {

        Calendar currentDate = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
        super.startDate = df.format(DateUtil.getFirstDateOfMonth(currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH)));
        super.endDate = df.format(currentDate.getTime());
        ESESystem preferences = preferncesService.findPrefernceById("1");
        if (!StringUtil.isEmpty(preferences)) {
        	setExportLimit(preferences.getPreferences().get(ESESystem.EXPORT_RECORD_LIMIT));
         }
        balanceTypeList.put(1, getText("procurement"));
        balanceTypeList.put(2, getText("distribution"));
        request.setAttribute(HEADING, getText(LIST));
        fields.add(getText("date"));
        fields.add(getText("accountNumberGrid"));
        fields.add(getText("agent"));
        filter = new AgentBalanceReport();
        return LIST;
    }

    /**
     * Detail.
     * @return the string
     * @throws Exception the exception
     */
    @SuppressWarnings("unchecked")
    public String detail() throws Exception {

        this.gridIdentity = "detail";
        // set profile id(transient variable) null for parent grid for calling Projections(grouping
        // by agentId)
        this.filter.setProfileId(null);
        super.filter = this.filter;
        Map data = readData("agentId");
        return sendJSONResponse(data);
    }

    /**
     * Sub grid detail.
     * @return the string
     * @throws Exception the exception
     */
    @SuppressWarnings("unchecked")
    public String subGridDetail() throws Exception {

        this.gridIdentity = "subDetail";
        int balType = filter.getBalanceType();
        filter = new AgentBalanceReport();
        this.filter.setProfileId(profileId);
        this.filter.setBalanceType(balType);
        super.filter = this.filter;
        Map data = readData();
        return sendJSONResponse(data);
    }

    /**
     * @see com.sourcetrace.esesw.view.BaseReportAction#toJSON(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJSON(Object obj) {

        JSONObject jsonObject = new JSONObject();
        String balance;
        String profType = null;
        if ("detail".equals(this.gridIdentity)) {
            Object[] datas = (Object[]) obj;
            JSONArray rows = new JSONArray();
            int i = 0;
            for (Object data : datas) {
                
               if(i==0 || i==4){
                   rows.add(data); 
               }
               else if (i == 1)
                {
                    profType = data.toString();
                }
               else if (i == 2) {
                   String agentName = data.toString();
                   if (Profile.CO_OPEARATIVE_MANAGER.equalsIgnoreCase(profType)) {
                       agentName += "*";
                   }
                   rows.add(agentName);
                   profType = null;
               }else if(i==3){
                  /* i++;
                   continue;*/
            	   String servicePointName = data.toString();
            	   //servicePointName += "*";
            	 rows.add(servicePointName);
               }              
                else if (i == 5) {
                    balance = CurrencyUtil.thousandSeparator(Math.abs((Double) data));
                    balance = (Double) datas[5] < 0 ? balance + " - DR"
                            : (Double) datas[5] > 0 ? balance + " - CR" : balance + "        ";
                    rows.add(balance);
                }
               
                jsonObject.put(i, data);
                i++;
            }
            jsonObject.put("id", datas[0]);
            jsonObject.put("cell", rows);
        } else {
            AgentBalanceReport agentBalanceReport = (AgentBalanceReport) obj;
            JSONArray rows = new JSONArray();

           /* rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">"
                    + sdf.format(agentBalanceReport.getTxnTime()) + "</font>");*/
            
            rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">"
                    + DateUtil.convertDateToString(agentBalanceReport.getTxnTime(), getGeneralDateFormat().concat(" HH:mm:ss")) + "</font>");
            
            rows.add(agentBalanceReport.getReceiptNo());
            String txnDesc = agentBalanceReport.getTxnDesc();
            if (PaymentMode.DISTIBUTION_PAYMENT_TXN.equalsIgnoreCase(agentBalanceReport
                    .getTxnType())
                    || PaymentMode.PROCURMENT_PAYMENT_TXN.equalsIgnoreCase(agentBalanceReport
                            .getTxnType())
                    || Contract.CONTRACT_TXN.equalsIgnoreCase(agentBalanceReport.getTxnType())) {
                txnDesc = "";
                String[] trxnDesc = agentBalanceReport.getTxnDesc().split("\\|");
                for (int i = 0; i <= 1; i++) {
                    txnDesc += trxnDesc[i].toUpperCase() + " ";
                }
            }
            if (!StringUtil.isEmpty(agentBalanceReport.getFarmerName()))
                txnDesc = txnDesc + " ( " + agentBalanceReport.getFarmerName() + " ) ";
            //rows.add(txnDesc);
            rows.add(CurrencyUtil.thousandSeparator(Math
                    .abs(agentBalanceReport.getInitialBalance())));
            rows.add(CurrencyUtil.thousandSeparator(Math.abs(agentBalanceReport.getTxnAmount())));
            balance = CurrencyUtil.thousandSeparator(Math
                    .abs(agentBalanceReport.getBalanceAmount()));
            balance = agentBalanceReport.getBalanceAmount() < 0 ? balance + " - DR"
                    : agentBalanceReport.getBalanceAmount() > 0 ? balance + " - CR" : balance
                            + "        ";
            rows.add(balance);
            jsonObject.put("id", agentBalanceReport.getId());
            jsonObject.put("cell", rows);
        }

        return jsonObject;
    }

    /**
     * Populate pdf.
     * @return the string
     * @throws Exception the exception
     */
    @SuppressWarnings("unchecked")
    public String populatePDF() throws Exception {

        setPdfFileName(getText("agentBalanceReport") + DateUtil.getDate());
        String title = getText("agentReportTitle");
        String headers = getText("agentbalanceviewProperties");
        String[] headerText = headers.split(",");
        /** SETTING FILTERING AND SORTING VALUES **/
        this.filter.setProfileId(null);
        super.filter = this.filter;
        setSord("desc");
        setSidx("id");
        /** FETCHING MAIN GRID DATA **/
        Map data = readData("agentId");
        List<AgentBalanceReport> mainGridRows = (List<AgentBalanceReport>) data.get(ROWS);
        List<Map> agentDataMapList = new ArrayList<Map>();
        for (Object obj : mainGridRows) {
            Object[] object = (Object[]) obj;
            /** SETTING FILTERING AND SORTING VALUES **/
            filter = new AgentBalanceReport();
            this.filter.setProfileId(object[0].toString());
            super.filter = this.filter;
            /** FETCHING INNER GRID DATA **/
            Map innerData = readData();
            List<AgentBalanceReport> innerGridRows = (List<AgentBalanceReport>) innerData.get(ROWS);
            for (AgentBalanceReport agentBalance : innerGridRows) {
                Map dataMap = new LinkedHashMap();
                dataMap.put(getText("agentId"), object[0]);
                dataMap.put(getText("agentName"), object[1]);
                dataMap.put(getText("servicePointName"), object[2]);
                dataMap.put(getText("accountNumber"), object[3]);
                dataMap.put(getText("finalBalance"), object[5]);
                dataMap.put(getText("txnTime"), sdf.format(agentBalance.getTxnTime()));
                dataMap.put(getText("receiptNo"), agentBalance.getReceiptNo());
                dataMap.put(getText("initialBalance"), agentBalance.getInitialBalance());
                dataMap.put(getText("txnAmount"), agentBalance.getTxnAmount());
                dataMap.put(getText("balanceAmount"), agentBalance.getBalanceAmount());
                dataMap.put(getText("txnDesc"), agentBalance.getTxnDesc());
                agentDataMapList.add(dataMap);
            }

        }
//        String logoPath = request.getSession().getServletContext().getRealPath(
//                "/assets/client/demo/header/stsLogo.png");
//        InputStream is = BaseDynamicJasperReport.fileExporter(title, agentDataMapList,
//                ExportType.PDF.ordinal(), Arrays.asList(headerText), logoPath);
//        Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
//        fileMap.put(pdfFileName, is);
//        fileInputStream = FileUtil.createFileInputStreamToZipFile(getText("agentBalanceReport"),
//                fileMap, ".pdf");
        return "pdf";
    }

    /**
     * Populate xl s1.
     * @return the string
     * @throws Exception the exception
     */
    @SuppressWarnings("unchecked")
    public String populateXLS1() throws Exception {

        setPdfFileName(getText("agentBalanceReport") + DateUtil.getDate());
        String title = getText("agentReportTitle");
        String headers = getText("agentbalanceviewProperties");
        String[] headerText = headers.split(",");
        /** SETTING FILTERING AND SORTING VALUES **/
        this.filter.setProfileId(null);
        super.filter = this.filter;
        setSord("desc");
        setSidx("id");
        /** FETCHING MAIN GRID DATA **/
        Map data = readData("agentId");
        List<AgentBalanceReport> mainGridRows = (List<AgentBalanceReport>) data.get(ROWS);
        List<Map> agentDataMapList = new ArrayList<Map>();
        for (Object obj : mainGridRows) {
            Object[] object = (Object[]) obj;
            /** SETTING FILTERING AND SORTING VALUES **/
            filter = new AgentBalanceReport();
            this.filter.setProfileId(object[0].toString());
            super.filter = this.filter;
            /** FETCHING INNER GRID DATA **/
            Map innerData = readData();
            List<AgentBalanceReport> innerGridRows = (List<AgentBalanceReport>) innerData.get(ROWS);
            for (AgentBalanceReport agentBalance : innerGridRows) {
                Map dataMap = new LinkedHashMap();
                dataMap.put(getText("agentId"), object[0]);
                dataMap.put(getText("agentName"), object[1]);
                dataMap.put(getText("servicePointName"), object[2]);
                dataMap.put(getText("accountNumber"), object[3]);
                dataMap.put(getText("finalBalance"), object[5]);
                dataMap.put(getText("txnTime"), sdf.format(agentBalance.getTxnTime()));
                dataMap.put(getText("receiptNo"), agentBalance.getReceiptNo());
                dataMap.put(getText("initialBalance"), agentBalance.getInitialBalance());
                dataMap.put(getText("txnAmount"), agentBalance.getTxnAmount());
                dataMap.put(getText("balanceAmount"), agentBalance.getBalanceAmount());
                dataMap.put(getText("txnDesc"), agentBalance.getTxnDesc());
                agentDataMapList.add(dataMap);
            }

        }
//        String logoPath = request.getSession().getServletContext().getRealPath(
//                "/assets/client/demo/header/stsLogo.png");
//        InputStream is = BaseDynamicJasperReport.fileExporter(title, agentDataMapList,
//                ExportType.XLS.ordinal(), Arrays.asList(headerText), logoPath);
//        Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
//        fileMap.put(pdfFileName, is);
//        fileInputStream = FileUtil.createFileInputStreamToZipFile(getText("agentBalanceReport"),
//                fileMap, ".xls");
        return "xls";
    }

    /**
     * Gets the agent service.
     * @return the agent service
     */
    public IAgentService getAgentService() {

        return agentService;
    }

    /**
     * Sets the agent service.
     * @param agentService the new agent service
     */
    public void setAgentService(IAgentService agentService) {

        this.agentService = agentService;
    }

    /**
     * Gets the agent list.
     * @return the agent list
     */
    public Map<String, String> getAgentList() {

        Map<String, String> agentList = new LinkedHashMap<String, String>();

        List<Agent> agents = agentService.listAgent();
        if (!ObjectUtil.isEmpty(agents)) {
            for (Agent agent : agents) {
                agentList.put(agent.getProfileId(), agent.getPersonalInfo().getAgentName() + " - "
                        + agent.getProfileId());
            }
        }

        return agentList;
    }

    /**
     * Gets the balance type list.
     * @return the balance type list
     */
    public Map<Integer, String> getBalanceTypeList() {

        return balanceTypeList;
    }

    /**
     * Sets the balance type list.
     * @param balanceTypeList the balance type list
     */
    public void setBalanceTypeList(Map<Integer, String> balanceTypeList) {

        this.balanceTypeList = balanceTypeList;
    }

    /**
     * Populate xls.
     * @return the string
     * @throws Exception the exception
     */
    public String populateXLS() throws Exception {

        InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
        setXlsFileName(getText("MobileUserReport") + fileNameDateFormat.format(new Date()));
        Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
        fileMap.put(xlsFileName, is);
        setFileInputStream(FileUtil.createFileInputStreamToZipFile(
                getText("agentBalanceReportList"), fileMap, ".xls"));
        return "xls";
    }

    public IProductDistributionService getProductDistributionService() {
    
        return productDistributionService;
    }

    public void setProductDistributionService(IProductDistributionService productDistributionService) {
    
        this.productDistributionService = productDistributionService;
    }

    /**
     * Gets the excel export input stream.
     * @param manual the manual
     * @return the excel export input stream
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @SuppressWarnings("unchecked")
    public InputStream getExportDataStream(String exportType) throws IOException {

        setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
        setsDate(IExporter.EXPORT_DAILY.equalsIgnoreCase(exportType) ? DateUtil.getDateWithoutTime(new Date()) : DateUtil.convertStringToDate(DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT) , DateUtil.DATABASE_DATE_FORMAT));
        boolean flag = true;
        DateFormat filterDateFormat = new SimpleDateFormat(getGeneralDateFormat());
        int balType;

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(getLocaleProperty("AgentBalanceExportAgentBalanceTitle"));
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

        HSSFRow row, titleRow, filterRowTitle, filterRow1, filterRow2, filterRow3, filterRow4 = null, filterRow5 = null;
        HSSFCell cell;

        int imgRow1 = 0;
        int imgRow2 = 4;
        int imgCol1 = 0;
        int imgCol2 = 1;
        int titleRow1 = 2;
        int titleRow2 = 3;

        int rowNum = 3;
        int colNum = 0;

        sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, titleRow1, titleRow2));

        titleRow = sheet.createRow(rowNum++);
        cell = titleRow.createCell(2);
        cell.setCellValue(new HSSFRichTextString(getLocaleProperty("AgentBalanceExportAgentBalanceTitle")));
        cell.setCellStyle(style1);
        font1.setBoldweight((short) 22);
        font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style1.setFont(font1);
        if(isMailExport()){
        rowNum++;
        rowNum++;
        filterRowTitle = sheet.createRow(rowNum++);
        
        cell = filterRowTitle.createCell(1);
        cell.setCellValue(new HSSFRichTextString(getLocaleProperty("filter")));
        filterFont.setBoldweight((short) 12);
        filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        filterStyle.setFont(filterFont);
        cell.setCellStyle(filterStyle);

        filterRow1 = sheet.createRow(rowNum++);

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

        cell = filterRow2.createCell(2);
        cell.setCellValue(new HSSFRichTextString(filterDateFormat.format(geteDate())));
        filterFont.setBoldweight((short) 12);
        filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        filterStyle.setFont(filterFont);
        cell.setCellStyle(filterStyle);

        filterRow3 = sheet.createRow(rowNum++);
        cell = filterRow3.createCell(1);
        cell.setCellValue(new HSSFRichTextString(getLocaleProperty("balanceType")));
        filterFont.setBoldweight((short) 12);
        filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        filterStyle.setFont(filterFont);
        cell.setCellStyle(filterStyle);

        cell = filterRow3.createCell(2);
        if (filter.getBalanceType() == 1) {
            cell.setCellValue(new HSSFRichTextString(getLocaleProperty("procurement")));
        } else if (filter.getBalanceType() == 2) {
            cell.setCellValue(new HSSFRichTextString(getLocaleProperty("distribution")));
        }
        filterFont.setBoldweight((short) 12);
        filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        filterStyle.setFont(filterFont);
        cell.setCellStyle(filterStyle);
        
        filterRow4 = sheet.createRow(rowNum++);
        filterRow5 = sheet.createRow(rowNum++);
        }
        rowNum++;

        HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
        String mainGridColumnHeaders = getLocaleProperty("AgentBalanceExportColumnHeader");
        int mainGridIterator = 0;

        for (String cellHeader : mainGridColumnHeaders.split("\\,")) {

            cell = mainGridRowHead.createCell(mainGridIterator);
            cell.setCellValue(new HSSFRichTextString(cellHeader));
            style2.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
            style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            cell.setCellStyle(style2);
            font2.setBoldweight((short) 12);
            font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style2.setFont(font2);
            sheet.setColumnWidth(mainGridIterator, (15 * 550));
            mainGridIterator++;
        }
        if(ObjectUtil.isEmpty(this.filter))
            this.filter = new AgentBalanceReport();
        this.filter.setProfileId(null);
        balType = filter.getBalanceType();
        this.filter.setBalanceType(balType);

        setSord("desc");
        setSidx("id");

        super.filter = this.filter;

        Map data = isMailExport() ? readData("agentId") : readExportData("agentId");

        List<AgentBalanceReport> mainGridRows = (List<AgentBalanceReport>) data.get(ROWS);
        if(ObjectUtil.isListEmpty(mainGridRows))
            return null;
        for (Object obj : mainGridRows) {

            Object[] object = (Object[]) obj;
            String balance = null;
            String profileId = object[0].toString();
            String profType = object[1].toString();

            if ((!StringUtil.isEmpty(filter.getAccountNumber()) || !StringUtil.isEmpty(filter
                    .getAgentId()))
                    && flag) {

                if (!StringUtil.isEmpty(filter.getAccountNumber())) {

                    cell = filterRow4.createCell(1);
                    cell.setCellValue(new HSSFRichTextString(getLocaleProperty("accountNumberGrid")));
                    filterFont.setBoldweight((short) 12);
                    filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                    filterStyle.setFont(filterFont);
                    cell.setCellStyle(filterStyle);

                    cell = filterRow4.createCell(2);
                    cell.setCellValue(new HSSFRichTextString(String.valueOf(object[4])));
                    filterFont.setBoldweight((short) 12);
                    filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                    filterStyle.setFont(filterFont);
                    cell.setCellStyle(filterStyle);
                }

                if (!StringUtil.isEmpty(filter.getAgentId())) {

                    cell = filterRow5.createCell(1);
                    cell.setCellValue(new HSSFRichTextString(getLocaleProperty("agent")));
                    filterFont.setBoldweight((short) 12);
                    filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                    filterStyle.setFont(filterFont);
                    cell.setCellStyle(filterStyle);

                    cell = filterRow5.createCell(2);
                    cell.setCellValue(new HSSFRichTextString(String.valueOf(object[2]) + " - "
                            + String.valueOf(object[0])));
                    filterFont.setBoldweight((short) 12);
                    filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                    filterStyle.setFont(filterFont);
                    cell.setCellStyle(filterStyle);

                    if (StringUtil.isEmpty(filter.getAccountNumber())) {
                        sheet.shiftRows(filterRow4.getRowNum() + 1, filterRow5.getRowNum() + 1, -1);
                    }
                }

                flag = false;
            }

            row = sheet.createRow(rowNum++);
            colNum = 0;

            cell = row.createCell(colNum++);
            cell.setCellValue(new HSSFRichTextString(String.valueOf(object[0])));

            cell = row.createCell(colNum++);
            if (Profile.CO_OPEARATIVE_MANAGER.equalsIgnoreCase(profType)) {
                cell.setCellValue(new HSSFRichTextString(String.valueOf(object[2]) + "*"));
            } else {
                cell.setCellValue(new HSSFRichTextString(String.valueOf(object[2])));
            }

            cell = row.createCell(colNum++);
            cell.setCellValue(new HSSFRichTextString(String.valueOf(object[3])));

            cell = row.createCell(colNum++);
            cell.setCellValue(new HSSFRichTextString(String.valueOf(object[4])));

            balance = CurrencyUtil.thousandSeparator(Math.abs((Double) object[5]));
            balance = (Double) object[5] < 0 ? balance + " - DR" : (Double) object[5] > 0 ? balance
                    + " - CR" : balance + "        ";

            cell = row.createCell(colNum++);
            cell.setCellValue(new HSSFRichTextString(String.valueOf(balance)));

            HSSFRow subGridRowHead = sheet.createRow(rowNum++);
            String sunGridcolumnHeaders = getLocaleProperty("AgentBalanceExportSubColumnHeader");
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

            this.filter.setProfileId(profileId);
            super.filter = this.filter;

            Map innerData = isMailExport() ? readData() : readExportData();

            List<AgentBalanceReport> innerGridRows = (List<AgentBalanceReport>) innerData.get(ROWS);
            for (AgentBalanceReport agentBalance : innerGridRows) {

                row = sheet.createRow(rowNum++);
                colNum = 1;
                String subGridbalance = null;

                /*cell = row.createCell(colNum++);
                cell.setCellValue(new HSSFRichTextString(sdf.format(agentBalance.getTxnTime())));*/
                
                cell = row.createCell(colNum++);
                cell.setCellValue(new HSSFRichTextString(DateUtil.convertDateToString(agentBalance.getTxnTime(), getGeneralDateFormat().concat(" HH:mm:ss"))));
                
                cell = row.createCell(colNum++);
                cell.setCellValue(new HSSFRichTextString(agentBalance.getReceiptNo()));

                String txnDesc = agentBalance.getTxnDesc();
                if (PaymentMode.DISTIBUTION_PAYMENT_TXN.equalsIgnoreCase(agentBalance.getTxnType())
                        || PaymentMode.PROCURMENT_PAYMENT_TXN.equalsIgnoreCase(agentBalance
                                .getTxnType())
                        || Contract.CONTRACT_TXN.equalsIgnoreCase(agentBalance.getTxnType())) {
                    txnDesc = "";
                    String[] trxnDesc = agentBalance.getTxnDesc().split("\\|");
                    for (int i = 0; i <= 1; i++) {
                        txnDesc += trxnDesc[i].toUpperCase() + " ";
                    }
                }
                if (!StringUtil.isEmpty(agentBalance.getFarmerName()))
                    txnDesc = txnDesc + " ( " + agentBalance.getFarmerName() + " ) ";

                cell = row.createCell(colNum++);
                cell.setCellValue(new HSSFRichTextString(txnDesc));

                cell = row.createCell(colNum++);
                cell.setCellValue(new HSSFRichTextString(String.valueOf(Math.abs(agentBalance
                        .getInitialBalance()))));

                cell = row.createCell(colNum++);
                cell.setCellValue(new HSSFRichTextString(String.valueOf(Math.abs(agentBalance
                        .getTxnAmount()))));

                subGridbalance = CurrencyUtil.thousandSeparator(Math.abs(agentBalance
                        .getBalanceAmount()));
                subGridbalance = agentBalance.getBalanceAmount() < 0 ? subGridbalance + " - DR"
                        : agentBalance.getBalanceAmount() > 0 ? subGridbalance + " - CR"
                                : subGridbalance + "        ";

                cell = row.createCell(colNum++);
                cell.setCellValue(new HSSFRichTextString(subGridbalance));

            }

        }

        int pictureIdx = getPicIndex(wb);
        HSSFClientAnchor anchor = new HSSFClientAnchor(400, 10, 655, 200, (short) 0, 0, (short) 0,
                0);
        anchor.setAnchorType(1);
        HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
        picture.resize();
        String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
                : request.getSession().getId();
        String makeDir = FileUtil.storeXls(id);
        String fileName = getLocaleProperty("agentagentBalanceReportList") + fileNameDateFormat.format(new Date())
                + ".xls";
        FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
        wb.write(fileOut);
        InputStream stream = new FileInputStream(new File(makeDir + fileName));
        fileOut.close();

        return stream;

    }

    /**
     * Gets the pic index.
     * @param wb the wb
     * @return the pic index
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public int getPicIndex(HSSFWorkbook wb) throws IOException {

        int index = -1;

        byte[] picData = null;
        if (ObjectUtil.isEmpty(request)) {
            picData = productDistributionService.findLogoImageById(1L);
        } else {
            String client = "sourceTrace-logo.png";
            String logoPath = request.getSession().getServletContext().getRealPath(
                    "/img/" + client);
            File pic = new File(logoPath);
            long length = pic.length();
            picData = new byte[(int) length];

            FileInputStream picIn = new FileInputStream(pic);
            picIn.read(picData);
        }
        if(picData != null)
        index = wb.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

        return index;
    }

    /**
     * Gets the xls file name.
     * @return the xls file name
     */
    public String getXlsFileName() {

        return xlsFileName;
    }

    /**
     * Sets the xls file name.
     * @param xlsFileName the new xls file name
     */
    public void setXlsFileName(String xlsFileName) {

        this.xlsFileName = xlsFileName;
    }
    public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}
	public String getExportLimit() {
  		return exportLimit;
  	}

  	public void setExportLimit(String exportLimit) {
  		this.exportLimit = exportLimit;
  	}


}
