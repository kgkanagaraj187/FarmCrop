/*
 * OfflineProcurementReportAction.java
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

import com.sourcetrace.eses.order.entity.txn.OfflineProcurement;
import com.sourcetrace.eses.order.entity.txn.OfflineProcurementDetail;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

public class OfflineProcurementReportAction extends BaseReportAction implements IExporter{

    private static final long serialVersionUID = -7954210219699296811L;
    private List<String> fields = new ArrayList<String>();
    private OfflineProcurement filter;
    private IProductDistributionService productDistributionService;
    private String id;
    private String status;
    private String xlsFileName;
    private InputStream fileInputStream;
    private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat(
            "yyyyMMddHHmmss");

    /**
     * Sets the fields.
     * @param fields the new fields
     */
    public void setFields(List<String> fields) {

        this.fields = fields;
    }

    /**
     * Gets the filter.
     * @return the filter
     */
    public OfflineProcurement getFilter() {

        return filter;
    }

    /**
     * Sets the filter.
     * @param filter the new filter
     */
    public void setFilter(OfflineProcurement filter) {

        this.filter = filter;
    }

    /**
     * Gets the fields.
     * @return the fields
     */
    public List<String> getFields() {

        return fields;
    }

    /**
     * @see com.sourcetrace.esesw.view.BaseReportAction#getId()
     */
    public String getId() {

        return id;
    }

    /**
     * Sets the status.
     * @param status the new status
     */
    public void setStatus(String status) {

        this.status = status;
    }

    /**
     * Gets the status.
     * @return the status
     */
    public String getStatus() {

        return status;
    }

    /**
     * @see com.sourcetrace.esesw.view.BaseReportAction#setId(java.lang.String)
     */
    public void setId(String id) {

        this.id = id;
    }

    /**
     * Sets the product distribution service.
     * @param productDistributionService the new product distribution service
     */
    public void setProductDistributionService(IProductDistributionService productDistributionService) {

        this.productDistributionService = productDistributionService;
    }

    /**
     * Gets the product distribution service.
     * @return the product distribution service
     */
    public IProductDistributionService getProductDistributionService() {

        return productDistributionService;
    }

    /**
     * @see com.sourcetrace.esesw.view.BaseReportAction#list()
     */
    public String list() {

        Calendar currentDate = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
        super.startDate = df.format(DateUtil.getFirstDateOfMonth(currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH)));
        super.endDate = df.format(currentDate.getTime());
        request.setAttribute(HEADING, getText(LIST));
        fields.add(getText("farmerId"));
        fields.add(getText("date"));
        // fields.add(getText("type"));
        fields.add(getText("receiptNo"));
        fields.add(getText("status"));
        filter = new OfflineProcurement();
        return LIST;
    }

    /**
     * Detail.
     * @return the string
     * @throws Exception the exception
     */
    @SuppressWarnings("unchecked")
    public String detail() throws Exception {

        super.filter = this.filter;
        if (status.equalsIgnoreCase("FAILED")) {
            filter.setStatusCode(1);
        } else if (status.equalsIgnoreCase("PENDING")) {
            filter.setStatusCode(2);
        }
        Map data = readData();
        return sendJSONResponse(data);
    }

    /**
     * Sub grid detail.
     * @return the string
     * @throws Exception the exception
     */
    @SuppressWarnings("unchecked")
    public String subGridDetail() throws Exception {

        OfflineProcurementDetail procurementDetail = new OfflineProcurementDetail();
        OfflineProcurement procurement = new OfflineProcurement();
        procurement.setId(Long.valueOf(id));
        procurementDetail.setOfflineProcurement(procurement);
        super.filter = procurementDetail;
        Map data = readData();
        return sendJSONResponse(data);

    }

    /**
     * @see com.sourcetrace.esesw.view.BaseReportAction#toJSON(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJSON(Object obj) {

        JSONObject jsonObject = new JSONObject();
        if (obj instanceof OfflineProcurement) {
            OfflineProcurement procurement = (OfflineProcurement) obj;
            JSONArray rows = new JSONArray();
            rows.add(procurement.getProcurementDate());
            // rows.add(getText("productType" + procurement.getType()));
            rows.add(procurement.getReceiptNo());
            rows.add(procurement.getFarmerId());
            rows.add(CurrencyUtil.getDecimalFormat(Double.valueOf(procurement.getTotalAmount()),
                    "##.00"));
            rows.add(procurement.getStatusMsg());
            jsonObject.put("id", procurement.getId());
            jsonObject.put("cell", rows);
        } else if (obj instanceof OfflineProcurementDetail) {
            OfflineProcurementDetail procurementDetail = (OfflineProcurementDetail) obj;
            JSONArray rows = new JSONArray();
            rows.add(procurementDetail.getProductCode());
            rows.add(procurementDetail.getQuality());
            rows.add(procurementDetail.getNumberOfBags());
            rows.add(CurrencyUtil.getDecimalFormat(Double.valueOf(procurementDetail
                    .getGrossWeight()), "##.000"));
            rows.add(CurrencyUtil.getDecimalFormat(Double
                    .valueOf(procurementDetail.getTareWeight()), "##.000"));
            rows.add(CurrencyUtil.getDecimalFormat(
                    Double.valueOf(procurementDetail.getNetWeight()), "##.000"));
            rows.add(CurrencyUtil.getDecimalFormat(Double.valueOf(procurementDetail
                    .getRatePerUnit()), "##.00"));
            rows.add(CurrencyUtil.getDecimalFormat(Double.valueOf(procurementDetail.getSubTotal()),
                    "##.00"));
            jsonObject.put("id", procurementDetail.getId());
            jsonObject.put("cell", rows);
        }
        return jsonObject;
    }

    /**
     * Gets the type list.
     * @return the type list
     */
    public Map<Integer, String> getTypeList() {

        Map<Integer, String> returnValue = new LinkedHashMap<Integer, String>();
        // Add value as alphabetical order
        returnValue.put(Procurement.productType.CARRIER.ordinal(), Procurement.productType.CARRIER
                .toString());
        returnValue.put(Procurement.productType.GOODS.ordinal(), Procurement.productType.GOODS
                .toString());

        return returnValue;

    }

    /**
     * Gets the status list.
     * @return the status list
     */
    public List<String> getStatusList() {

        List<String> returnValue = new ArrayList<String>();
        returnValue.add("FAILED");
        returnValue.add("PENDING");
        return returnValue;
    }

    /**
     * Reconcile.
     * @return the string
     * @throws Exception the exception
     */
    public String reconcile() throws Exception {

        if (!StringUtil.isEmpty(this.getId())) {
            OfflineProcurement existing = productDistributionService
                    .findOfflineProcurementById(Long.parseLong(id));
            if (!ObjectUtil.isEmpty(existing)) {
                existing.setStatusCode(ESETxnStatus.PENDING.ordinal());
                existing.setStatusMsg(ESETxnStatus.PENDING.toString());
                productDistributionService.editOfflineProcurement(existing);
            }
        }
        return null;
    }

    /**
     * Populate xls.
     * @return the string
     * @throws Exception the exception
     */
    public String populateXLS() throws Exception {

        InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
        setXlsFileName(getText("offlineProcurementReportList")
                + fileNameDateFormat.format(new Date()));
        Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
        fileMap.put(xlsFileName, is);
        setFileInputStream(FileUtil.createFileInputStreamToZipFile(
                getText("offlineProcurementReportList"), fileMap, ".xls"));
        return "xls";
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
        DateFormat filterDateFormat = new SimpleDateFormat(FILTERDATE);

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(getLocaleProperty("exportOfflineProcurementTitle"));
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

        HSSFRow row, titleRow, filterRowTitle, filterRow1, filterRow2, filterRow3 = null, filterRow4 = null, filterRow5 = null;
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
        cell.setCellValue(new HSSFRichTextString(getLocaleProperty("exportOfflineProcurementTitle")));
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
        filterRow4 = sheet.createRow(rowNum++);
        filterRow5 = sheet.createRow(rowNum++);
        }
        rowNum++;

        HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
        String mainGridColumnHeaders = getLocaleProperty("exportColumnHeader");
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
            this.filter = new OfflineProcurement();
        if(!StringUtil.isEmpty(status))
            this.filter.setStatusCode(status.equalsIgnoreCase("FAILED") ? 1 : 2);
        super.filter = this.filter;

        Map data = isMailExport() ? readData() : readExportData();

        List<OfflineProcurement> mainGridRows = (List<OfflineProcurement>) data.get(ROWS);
        if(ObjectUtil.isListEmpty(mainGridRows))
            return null;
        for (OfflineProcurement procurement : mainGridRows) {

            if ((!StringUtil.isEmpty(filter.getFarmerId())
                    || !StringUtil.isEmpty(filter.getReceiptNo()) || filter.getStatusCode() > 0)
                    && flag) {

                if (!StringUtil.isEmpty(filter.getFarmerId())) {

                    cell = filterRow3.createCell(1);
                    cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmerId")));
                    filterFont.setBoldweight((short) 12);
                    filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                    filterStyle.setFont(filterFont);
                    cell.setCellStyle(filterStyle);

                    cell = filterRow3.createCell(2);
                    cell.setCellValue(new HSSFRichTextString(filter.getFarmerId()));
                    filterFont.setBoldweight((short) 12);
                    filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                    filterStyle.setFont(filterFont);
                    cell.setCellStyle(filterStyle);

                }

                if (!StringUtil.isEmpty(filter.getReceiptNo())) {

                    cell = filterRow4.createCell(1);
                    cell.setCellValue(new HSSFRichTextString(getLocaleProperty("receiptNo")));
                    filterFont.setBoldweight((short) 12);
                    filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                    filterStyle.setFont(filterFont);
                    cell.setCellStyle(filterStyle);

                    cell = filterRow4.createCell(2);
                    cell.setCellValue(new HSSFRichTextString(filter.getReceiptNo()));
                    filterFont.setBoldweight((short) 12);
                    filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                    filterStyle.setFont(filterFont);
                    cell.setCellStyle(filterStyle);

                    if (StringUtil.isEmpty(filter.getFarmerId())) {
                        sheet.shiftRows(filterRow3.getRowNum() + 1, filterRow4.getRowNum() + 1, -1);
                    }
                }

                if (filter.getStatusCode() > 0) {

                    cell = filterRow5.createCell(1);
                    cell.setCellValue(new HSSFRichTextString(getLocaleProperty("status")));
                    filterFont.setBoldweight((short) 12);
                    filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                    filterStyle.setFont(filterFont);
                    cell.setCellStyle(filterStyle);

                    cell = filterRow5.createCell(2);
                    if (filter.getStatusCode() == 1) {
                        cell.setCellValue(new HSSFRichTextString("FAILED"));

                    } else if (filter.getStatusCode() == 2) {
                        cell.setCellValue(new HSSFRichTextString("PENDING"));

                    }
                    filterFont.setBoldweight((short) 12);
                    filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                    filterStyle.setFont(filterFont);
                    cell.setCellStyle(filterStyle);

                    if (StringUtil.isEmpty(filter.getFarmerId())
                            && StringUtil.isEmpty(filter.getReceiptNo())) {
                        sheet.shiftRows(filterRow4.getRowNum() + 1, filterRow5.getRowNum() + 1, -2);
                    } else if (StringUtil.isEmpty(filter.getFarmerId())
                            || StringUtil.isEmpty(filter.getReceiptNo())) {
                        sheet.shiftRows(filterRow4.getRowNum() + 1, filterRow5.getRowNum() + 1, -1);
                    }
                }
                flag = false;

            }

            row = sheet.createRow(rowNum++);
            colNum = 0;

            cell = row.createCell(colNum++);
            cell.setCellValue(new HSSFRichTextString(procurement.getProcurementDate()));

            cell = row.createCell(colNum++);
            cell.setCellValue(new HSSFRichTextString(procurement.getReceiptNo()));

            cell = row.createCell(colNum++);
            cell.setCellValue(new HSSFRichTextString(procurement.getFarmerId()));

            cell = row.createCell(colNum++);
            cell.setCellValue(new HSSFRichTextString(CurrencyUtil.getDecimalFormat(Double
                    .valueOf(procurement.getTotalAmount()), "##.00")));

            cell = row.createCell(colNum++);
            cell.setCellValue(new HSSFRichTextString(procurement.getStatusMsg()));

            HSSFRow subGridRowHead = sheet.createRow(rowNum++);
            String sunGridcolumnHeaders = getLocaleProperty("exportSubColumnHeader");
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
                sheet.setColumnWidth(mainGridIterator, (15 * 550));
                subGridIterator++;
            }

            for (OfflineProcurementDetail procurementDetail : procurement
                    .getOfflineProcurementDetails()) {

                row = sheet.createRow(rowNum++);
                colNum = 1;

                cell = row.createCell(colNum++);
                cell.setCellValue(new HSSFRichTextString(procurementDetail.getProductCode()));

                cell = row.createCell(colNum++);
                cell.setCellValue(new HSSFRichTextString(procurementDetail.getQuality()));

                cell = row.createCell(colNum++);
                cell.setCellValue(new HSSFRichTextString(procurementDetail.getNumberOfBags()));

                cell = row.createCell(colNum++);
                cell.setCellValue(new HSSFRichTextString(CurrencyUtil.getDecimalFormat(Double
                        .valueOf(procurementDetail.getGrossWeight()), "##.000")));

                cell = row.createCell(colNum++);
                cell.setCellValue(new HSSFRichTextString(CurrencyUtil.getDecimalFormat(Double
                        .valueOf(procurementDetail.getTareWeight()), "##.000")));

                cell = row.createCell(colNum++);
                cell.setCellValue(new HSSFRichTextString(CurrencyUtil.getDecimalFormat(Double
                        .valueOf(procurementDetail.getNetWeight()), "##.000")));

                cell = row.createCell(colNum++);
                cell.setCellValue(new HSSFRichTextString(CurrencyUtil.getDecimalFormat(Double
                        .valueOf(procurementDetail.getRatePerUnit()), "##.00")));

                cell = row.createCell(colNum++);
                cell.setCellValue(new HSSFRichTextString(CurrencyUtil.getDecimalFormat(Double
                        .valueOf(procurementDetail.getSubTotal()), "##.00")));

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
        String fileName = getLocaleProperty("offlineProcurementReportList")
                + fileNameDateFormat.format(new Date()) + ".xls";
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

    /**
     * Gets the file input stream.
     * @return the file input stream
     */
    public InputStream getFileInputStream() {

        return fileInputStream;
    }

    /**
     * Sets the file input stream.
     * @param fileInputStream the new file input stream
     */
    public void setFileInputStream(InputStream fileInputStream) {

        this.fileInputStream = fileInputStream;
    }

}
