package com.sourcetrace.esesw.view.report.agro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.ese.entity.util.LoanLedger;
import com.sourcetrace.eses.dao.IReportDAO;
import com.sourcetrace.eses.order.entity.txn.LoanDistribution;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ExportUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

public class LoanManagementReportAction extends BaseReportAction implements IExporter {
	@Autowired
	private IPreferencesService preferncesService;
	@Autowired
	private IFarmerService farmerService;
	private static final SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.TXN_TIME_FORMAT);
	private String daterange;
	private String tenantId;
	private String xlsFileName;
	private InputStream fileInputStream;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private LoanLedger filter;

	public String list() throws Exception {
		Calendar currentDate = Calendar.getInstance();
		Calendar cal = (Calendar) currentDate.clone();
		cal.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) - 1);
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		super.startDate = df.format(cal.getTime());
		super.endDate = df.format(currentDate.getTime());
		daterange = super.startDate + " - " + super.endDate;
		request.setAttribute(HEADING, getText(LIST));
		ESESystem preferences = preferncesService.findPrefernceById("1");
		filter = new LoanLedger();

		return LIST;

	}

	public String data() throws Exception {
		if (startDate != null && endDate != null) {
			setFilter(new LoanLedger());
			filter.setBranchId(getBranchId());
			DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
			JSONObject gridData = new JSONObject();
			JSONArray rows = new JSONArray();
			super.filter = this.filter;
			Date sdate = DateUtil.getDateWithoutTime(df.parse(startDate));
			Date edate = DateUtil.getDateWithoutTime(df.parse(endDate));
			List<Object[]> list = farmerService.findDateOfLoanLedger(sdate, edate);
			List<Object[]> lists = farmerService.findDateOfLoanLedger(sdate, edate, getStartIndex(), getLimit());

			if (!ObjectUtil.isListEmpty(lists)) {
				for (Object object : lists) {
					String txnTime = object.toString();
					Date date = sdf.parse(txnTime);
					String res = txnTime.substring(0, 18);
					List<Object[]> listLoanLedgerLimitList = farmerService.listLoanLedgerByDate(res,getBranchId());

					// List<Object[]> listLoanLedgerLimitListt =
					// farmerService.listLoanLedgerByDate(res, getStartIndex(),
					// getLimit());
					if (!ObjectUtil.isListEmpty(listLoanLedgerLimitList)) {
						for (Object[] obj : listLoanLedgerLimitList) {
							if(!obj[0].toString().equalsIgnoreCase("0") || !obj[1].toString().equalsIgnoreCase("0") ){
							LoanLedger loanLedgerObj = new LoanLedger();
							loanLedgerObj.setTxnTime(date);
							loanLedgerObj.setActualAmount(!ObjectUtil.isEmpty(obj[0]) && (obj[0] != null)
									? Double.valueOf(obj[0].toString()) : 0.0);
							loanLedgerObj.setNewFarmerBal(!ObjectUtil.isEmpty(obj[1]) && (obj[1] != null)
									? Double.valueOf(obj[1].toString()) : 0.0);
							rows.add(toJSON(loanLedgerObj, IReportDAO.MAIN_GRID));
						}}
					}
				}
			}
			gridData.put(PAGE, getPage());

			totalRecords = list.size();
			gridData.put(TOTAL, java.lang.Math.ceil(totalRecords / Double.valueOf(Integer.toString(getLimit()))));
			gridData.put(IReportDAO.START_INDEX, getStartIndex());
			gridData.put(IReportDAO.LIMIT, list.size());
			gridData.put(IReportDAO.RECORD_COUNT, list.size());
			gridData.put(ROWS, rows);

			PrintWriter out = response.getWriter();
			out.println(gridData.toString());
		}

		return null;

	}

	public String populateXLS() throws Exception {

		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getLocaleProperty("loanManagementReportList") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getLocaleProperty("loanManagementReportList"),
				fileMap, ".xls"));

		return "xls";
	}
	XSSFRow row, filterRowTitle, filterRow1, filterRow2, filterRow3;
	XSSFRow titleRow;
	int colCount, rowCount, titleRow1 = 4, titleRow2 = 6;
	Cell cell;
	Integer cellIndex;
	public InputStream getExportDataStream(String exportType) throws IOException {
		LinkedHashMap<String, String> filters = new LinkedHashMap<String, String>();
		LinkedList<String> headersList = new LinkedList<String>();
		List<List<String>> dataList = new ArrayList<List<String>>();
		Map<Long, List<List<String>>> subListMap = new LinkedHashMap<Long, List<List<String>>>();
		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		setsDate(IExporter.EXPORT_DAILY.equalsIgnoreCase(exportType) ? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));
		boolean flag = true;
		DateFormat filterDateFormat = new SimpleDateFormat(FILTERDATE);

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("exportLoanManagementTitle"));
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
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("exportLoanManagementTitle")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);
			rowNum++;
			rowNum++;
			filterRow1 = sheet.createRow(rowNum++);
			
				filters.put(getLocaleProperty("StartingDate"), filterDateFormat.format(getsDate()));
				filters.put(getLocaleProperty("EndingDate"), filterDateFormat.format(geteDate()));

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
			if (geteDate() != null) {
				cell.setCellValue(new HSSFRichTextString(filterDateFormat.format(geteDate())));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
		rowNum++;
		//setFilter(new LoanLedger());

		String headers = getLocaleProperty("exportLoanManagementHeader");
		if (headers != null) {

			for (String name : headers.split(",")) {

				if (name.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
					name = name.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
				} else if (name.trim().contains(ESESystem.AREA_TYPE_EXPORT)) {
					name = name.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());
				}

				headersList.add(getLocaleProperty(name));
			}
		}

		if (startDate != null && endDate != null) {
			setFilter(new LoanLedger());
			filter.setBranchId(getBranchId());
			// JSONObject gridData = new JSONObject();
			JSONArray rows = new JSONArray();
			super.filter = this.filter;
			DateFormat dfs = new SimpleDateFormat(DateUtil.DATE_FORMAT);
			Date sdate = null;
			try {
				sdate = DateUtil.getDateWithoutTime(dfs.parse(startDate));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Date edate = null;
			try {
				edate = DateUtil.getDateWithoutTime(dfs.parse(endDate));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			JSONObject gridData = new JSONObject();
			List<Object[]> lists = farmerService.findDateOfLoanLedger(sdate, edate, getStartIndex(), 10000);
			List<LoanLedger> sublist = new ArrayList<>();
			if (!ObjectUtil.isListEmpty(lists)) {
				for (Object object : lists) {
					String txnTime = object.toString();
					Date date = null;
					try {
						date = sdf.parse(txnTime);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String res = txnTime.substring(0, 18);
					List<Object[]> listLoanLedgerLimitList = farmerService.listLoanLedgerByDate(res,getBranchId());

					// List<Object[]> listLoanLedgerLimitListt =
					// farmerService.listLoanLedgerByDate(res, getStartIndex(),
					// getLimit());
					if (!ObjectUtil.isListEmpty(listLoanLedgerLimitList)) {
						for (Object[] obj : listLoanLedgerLimitList) {
							if(!obj[0].toString().equalsIgnoreCase("0") || !obj[1].toString().equalsIgnoreCase("0") ){
							LoanLedger loanLedgerObj = new LoanLedger();
							loanLedgerObj.setTxnTime(date);
							loanLedgerObj.setActualAmount(!ObjectUtil.isEmpty(obj[0]) && (obj[0] != null)
									? Double.valueOf(obj[0].toString()) : 0.0);
							loanLedgerObj.setNewFarmerBal(!ObjectUtil.isEmpty(obj[1]) && (obj[1] != null)
									? Double.valueOf(obj[1].toString()) : 0.0);
							sublist.add(loanLedgerObj);
						}}

					}
				}
				
			}
			
	
			for (Object record : (List) sublist) {
				try {
					List<String> list = getGridDataForReport(record);
					dataList.add(list);
				} catch (ParseException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
			}
			

		}

		Asset existingAssetLogin = clientService.findAssetsByAssetCode(Asset.APP_LOGO);
		String title = getText("loanManagementReportList");
		String reportName = getText("exportloanManagementTitle");

		InputStream stream = ExportUtil.exportXLS(dataList, headersList, filters, title, getText("filter"), reportName,
				existingAssetLogin.getFile());

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

	public List<String> getGridDataForReport(Object obj) throws ParseException {

		List<String> dataList = new ArrayList<>();

		LoanLedger loanLedger = (LoanLedger) obj;
		ESESystem preferences = preferncesService.findPrefernceById("1");

		if (!StringUtil.isEmpty(preferences)) {
			DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
			dataList.add(!ObjectUtil.isEmpty(loanLedger) ? (!StringUtil.isEmpty(genDate.format(loanLedger.getTxnTime()))
					? genDate.format(loanLedger.getTxnTime()) : "") : "");
		}

		dataList.add((CurrencyUtil.getDecimalFormat((Double) loanLedger.getActualAmount(), "###.00")));
		dataList.add((CurrencyUtil.getDecimalFormat((Double) loanLedger.getNewFarmerBal(), "###.00")));
		// rows.add(!ObjectUtil.isEmpty(loanLedger.getPreFarmerBal()) ?
		// loanLedger.getPreFarmerBal() : "NA");
		//dataList.add((CurrencyUtil.getDecimalFormat((Double) loanLedger.getActualAmount() - loanLedger.getNewFarmerBal(), "##.0")));

		/*
		 * if (!ObjectUtil.isEmpty(loanLedger)) { if
		 * (!StringUtil.isEmpty(preferences)) { DateFormat genDate = new
		 * SimpleDateFormat(getGeneralDateFormat());
		 * subGridDataList.add(!ObjectUtil.isEmpty(loanLedger) ?
		 * (!StringUtil.isEmpty(genDate.format(loanLedger.getTxnTime())) ?
		 * genDate.format(loanLedger.getTxnTime()) : "") : ""); }
		 * subGridDataList.add(!StringUtil.isEmpty(loanLedger.getReceiptNo())
		 * ?loanLedger.getReceiptNo() : "" );
		 * subGridDataList.add(!StringUtil.isEmpty(loanLedger.getLoanDesc())
		 * ?loanLedger.getLoanDesc() : "" );
		 * 
		 * subGridDataList.add(CurrencyUtil.getDecimalFormat(loanLedger.
		 * getActualAmount(), "##.00"));
		 */
		return dataList;
	}

	public JSONObject toJSON(Object obj, String grid) {
		JSONObject jsonObject = new JSONObject();
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (obj instanceof LoanLedger) {
			LoanLedger loanLedger = (LoanLedger) obj;
			JSONArray rows = new JSONArray();

			if (!StringUtil.isEmpty(preferences)) {
				DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
				rows.add(!ObjectUtil.isEmpty(loanLedger) ? (!StringUtil.isEmpty(genDate.format(loanLedger.getTxnTime()))
						? genDate.format(loanLedger.getTxnTime()) : "") : "");
			}

			//rows.add(!ObjectUtil.isEmpty(loanLedger.getActualAmount()) ? loanLedger.getActualAmount() : "0");
			//rows.add(!ObjectUtil.isEmpty(loanLedger.getNewFarmerBal()) ? loanLedger.getNewFarmerBal() : "0");
			rows.add(( CurrencyUtil.getDecimalFormat (loanLedger.getActualAmount(),"###.00")));
			rows.add(( CurrencyUtil.getDecimalFormat (loanLedger.getNewFarmerBal(),"###.00")));
			
			// rows.add(!ObjectUtil.isEmpty(loanLedger.getPreFarmerBal()) ?
			// loanLedger.getPreFarmerBal() : "NA");
		//	rows.add(loanLedger.getActualAmount() - loanLedger.getNewFarmerBal());
			jsonObject.put("id", loanLedger.getId());
			jsonObject.put("cell", rows);
		}
		return jsonObject;
	}

	public LoanLedger getFilter() {
		return filter;
	}

	public void setFilter(LoanLedger filter) {
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

}
