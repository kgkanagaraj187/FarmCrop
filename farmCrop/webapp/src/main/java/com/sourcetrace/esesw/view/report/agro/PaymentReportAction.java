

package com.sourcetrace.esesw.view.report.agro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.script.ScriptException;

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
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.ese.entity.util.FilterFieldData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sourcetrace.eses.dao.IReportDAO;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfig;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfigFilter;
import com.sourcetrace.eses.service.DynamicReportProperties;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

public class PaymentReportAction extends BaseReportAction implements IExporter{
	
	private static final long serialVersionUID = 1L;
	private String mainGridCols;
	private static String subGridCols;
	private static DynamicReportConfig dynamicReportConfig;
	
	private Object fValue;
	private Object mValue;
	
	private String filterList;
	private String gridIdentity;
	
	private static List<Object[]> seasonList;
	private static List<Object[]> farmerList;
	
	
	private List<DynamicReportConfigFilter> reportConfigFilters = new LinkedList<>();

	private String xlsFileName;
	private InputStream fileInputStream;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private String exportLimit;
	private String daterange;
	
	//private AgroTransaction agroTxn;
	
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private ILocationService locationService;
	@Autowired
    private IProductDistributionService productDistributionService;
	@Autowired
	private IPreferencesService preferncesService;
	
	
	//dynamic_report_config_detail accept multiple parameters
		Map<Integer,Long> dynamic_report_config_detail_ID =new HashMap<Integer,Long>();  
		private List<String> fvalueByParameters = new ArrayList<String>();
		private String expression_result;
	
	public String list() {
		
		Calendar currentDate = Calendar.getInstance();
		Calendar cal = (Calendar) currentDate.clone();
		cal.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) - 1);
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		startDate = df.format(cal.getTime());
		endDate = df.format(currentDate.getTime());
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			setExportLimit(preferences.getPreferences().get(ESESystem.EXPORT_RECORD_LIMIT));
		}
		daterange =startDate + " - " + endDate;
		
		formMainGridCols();
		setFilterSize(String.valueOf(dynamicReportConfig.getDynmaicReportConfigFilters().size()));
		dynamicReportConfig.getDynmaicReportConfigFilters().stream().forEach(reportConfigFilter -> {
			Map<String, String> optionMap = (Map<String, String>) getMethodValue(reportConfigFilter.getMethod(), "");
			reportConfigFilter.setOptions(optionMap);
			reportConfigFilters.add(reportConfigFilter);
		});
		return LIST;
	}
	
	
	private void formMainGridCols() {
		dynamicReportConfig = clientService.findReportByName(DynamicReportProperties.PAYMENT_REPORT);
		if (!ObjectUtil.isEmpty(dynamicReportConfig)) {
			mainGridCols = "";
			/*if (StringUtil.isEmpty(getBranchId())) {
				mainGridCols = getLocaleProperty("branchId") + "#" + "50%";
			}
			 */			
			dynamicReportConfig.getDynmaicReportConfigDetails().stream()
					.filter(config -> config.getIsGridAvailabiltiy())
					.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
					.forEach(dynamicReportConfigDetail -> {
						mainGridCols += getLocaleProperty(dynamicReportConfigDetail.getLabelName()) + "#"
								+ dynamicReportConfigDetail.getWidth() + "#"+(!StringUtil.isEmpty(dynamicReportConfigDetail.getAlignment())?dynamicReportConfigDetail.getAlignment():"left")+ "%";
					});
		}

	
	}

	
	@SuppressWarnings("unused")
	private Object getMethodValue(String methodName, String param) {
		Object field = null;
		try {
			@SuppressWarnings("rawtypes")
			Class cls = this.getClass();

			if (!StringUtil.isEmpty(param)) {
				Method setNameMethod = this.getClass().getMethod(methodName, String.class);
				field = setNameMethod.invoke(this, param);
			} else {
				Method setNameMethod = this.getClass().getMethod(methodName);
				field = setNameMethod.invoke(this);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return field;
	}
	
	private Object getMethodValue(String methodName, List<String> param) {
		Object field = null;
		try {
			@SuppressWarnings("rawtypes")
			Class cls = this.getClass();

			if (!StringUtil.isEmpty(param)) {
				Method setNameMethod = this.getClass().getMethod(methodName, List.class);
				field = setNameMethod.invoke(this, param);
			} else {
				Method setNameMethod = this.getClass().getMethod(methodName);
				field = setNameMethod.invoke(this);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return field;
	}
	
	public String detail() throws Exception {
		AgroTransaction agroTxn = new AgroTransaction();
		if (!StringUtil.isEmpty(filterList)) {
			try {
				Type listType1 = new TypeToken<List<FilterFieldData>>() {
				}.getType();
				List<FilterFieldData> filtersList = new Gson().fromJson(filterList, listType1);
				agroTxn.setFilterData(filtersList.stream()
						.collect(Collectors.toMap(FilterFieldData::getName, obj -> obj.getValue())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(!StringUtil.isEmpty(startDate)){
		agroTxn.setStartDate(startDate);
		}
		if(!StringUtil.isEmpty(endDate)){
		agroTxn.setEndDate(endDate);
		}
		super.filter = agroTxn;
		

		Map data = null;
		if (dynamicReportConfig.getFetchType() == 2L) {
			data = readProjectionData(dynamicReportConfig.getDynmaicReportConfigDetails());
		} else {
			data = readData();
		}
		setGridIdentity(IReportDAO.MAIN_GRID);
		return sendJSONResponse(data);
	}
	
	protected String sendJSONResponse(Map data) throws Exception {

		JSONObject gridData = new JSONObject();
		gridData.put(PAGE, getPage());
		totalRecords = (Integer) data.get(RECORDS);
		gridData.put(TOTAL, java.lang.Math.ceil(totalRecords / Double.valueOf(Integer.toString(getLimit()))));
		gridData.put(RECORDS, totalRecords);
		List list = (List) data.get(ROWS);
		JSONArray rows = new JSONArray();
		if (list != null) {
			branchIdValue = getBranchId();
			if (StringUtil.isEmpty(branchIdValue)) {
				buildBranchMap();
			}
			for (Object record : list) {
				initializeMap(record);
				rows.add(toJSON(record));
			}
		}
		if (totalRecords > 0) {
			gridData.put("userdata", userDataToJSON());
		} else {
			gridData.put("userdata", userDataToJSON());
		}
		gridData.put(ROWS, rows);
		//
		PrintWriter out = response.getWriter();
		out.println(gridData.toString());

		return null;
	}
	
	public void initializeMap(Object obj){
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		AtomicInteger runCount = new AtomicInteger(1);
		
		if (obj instanceof Object[]) {
			Object[] arr = (Object[]) obj;
		
			dynamicReportConfig.getDynmaicReportConfigDetails().stream()
			.filter(config -> config.getIsGridAvailabiltiy())
			.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
			.forEach(dynamicReportConfigDetail -> {
				dynamic_report_config_detail_ID.put(runCount.getAndIncrement(),dynamicReportConfigDetail.getId()); 
			});
	}
   }
	
	public JSONObject toJSON(Object obj) {
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		AtomicInteger runCount = new AtomicInteger(1);
		String id = "";
		if (obj instanceof Object[]) {
			Object[] arr = (Object[]) obj;
			id = String.valueOf(arr[0]);
			if (getGridIdentity().equalsIgnoreCase(IReportDAO.MAIN_GRID)) {
				if (!StringUtil.isEmpty(dynamicReportConfig)) {
					
					dynamicReportConfig.getDynmaicReportConfigDetails().stream()
							.filter(config -> config.getIsGridAvailabiltiy())
							.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
							.forEach(dynamicReportConfigDetail -> {
						
								if (dynamicReportConfigDetail.getAccessType() == 1L) {
									fValue = ReflectUtil.getObjectFieldValue(arr,
											String.valueOf(runCount.getAndIncrement()));
									if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
										rows.add(fValue.toString());
									} else {
										rows.add("");
									}
								} else if (dynamicReportConfigDetail.getAccessType() == 2L) {
									fValue = ReflectUtil.getObjectFieldValue(arr,
											String.valueOf(runCount.getAndIncrement()));
									if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
										mValue = getMethodValue(dynamicReportConfigDetail.getMethod(),
												fValue.toString());
										if (!ObjectUtil.isEmpty(mValue) && !StringUtil.isEmpty(mValue)) {
											rows.add(mValue.toString());
										} else {
											rows.add("");
										}
									} else {
										rows.add("");
									}
								} else if (dynamicReportConfigDetail.getAccessType() == 3L) {
									fValue = ReflectUtil.getObjectFieldValue(arr,String.valueOf(runCount.getAndIncrement()));
									
									if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
										mValue = getMethodValue(dynamicReportConfigDetail.getMethod(),fValue.toString());
									}
									
									if(!ObjectUtil.isEmpty(dynamicReportConfigDetail.getExpression()) && !StringUtil.isEmpty(dynamicReportConfigDetail.getExpression())){
										String expression = dynamicReportConfigDetail.getExpression();
										
										try {
											expression_result = ReflectUtil.getObjectFieldValueByExpression(arr,expression,dynamic_report_config_detail_ID);
										} catch (ScriptException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										}
									
									
								if(!ObjectUtil.isEmpty(dynamicReportConfigDetail.getParameters()) && !StringUtil.isEmpty(dynamicReportConfigDetail.getParameters())){
									String parameters = dynamicReportConfigDetail.getParameters();
									String[] parametersArray = parameters.split(",");
									fvalueByParameters = ReflectUtil.getObjectFieldValueByParameters(arr,parametersArray,dynamic_report_config_detail_ID);
									
									
										Object temp = getMethodValue(dynamicReportConfigDetail.getMethod(),fvalueByParameters);
									
									
									
									
							/*		if (!ObjectUtil.isListEmpty(fvalueByParameters) && !ObjectUtil.isEmpty(dynamicReportConfigDetail.getParameters()) && !StringUtil.isEmpty(dynamicReportConfigDetail.getParameters()) ) {
										
										String	final_fvalue = "" ;
											Iterator<String> iterator = fvalueByParameters.iterator();
											while (iterator.hasNext()) {
												final_fvalue = final_fvalue +" - "+(iterator.next()).toString();
											}
											
										String  mv = (mValue.toString())+final_fvalue;
										rows.add(mv);
										
									}*/
								}else 
								if( !ObjectUtil.isEmpty(expression_result) && !StringUtil.isEmpty(expression_result)  && !ObjectUtil.isEmpty(dynamicReportConfigDetail.getExpression()) && !StringUtil.isEmpty(dynamicReportConfigDetail.getExpression()) ){
									rows.add(expression_result);
								}else if(!ObjectUtil.isEmpty(mValue) && !StringUtil.isEmpty(mValue)) {
									rows.add(mValue.toString());
								}else {
									rows.add("");
								}
							
								}
								 else if (dynamicReportConfigDetail.getAccessType() == 5L) {
										fValue = ReflectUtil.getObjectFieldValue(arr, String.valueOf(runCount.getAndIncrement()));

										if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue) && (!"0,0".equalsIgnoreCase(String.valueOf(fValue))) && (!",".equalsIgnoreCase(String.valueOf(fValue)))) {
											String[] a = String.valueOf(fValue).split(",");
											//ansVal = "<button class='faMap' title='" + getText("farm.map.available.title") + "' onclick='showMap(\""
													
											rows.add("<button class='faMap' title='" + getText("farm.map.available.title")
											+ "' onclick='showMap(\""+ a[0] + "\",\"" + a[1] + "\")'></button>");
											
										} else {
											// No Latlon
											rows.add("<button class='no-latLonIcn' title='" + getText("farm.map.unavailable.title") + "'></button>");
										}
									}
							});
				}
			}
			
			jsonObject.put("id", id);
			jsonObject.put("cell", rows);

		}
		return jsonObject;

	}
	
	public String getSeasonByCode(String code) {
		String season = getSeasonList().get(code);
		return season;
	}
	
	public Map<String, String> getSeasonList() {
		Map<String, String> seasonMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(seasonList)) {
			seasonList = farmerService.listSeasonCodeAndName();
		}
		for (Object[] obj : seasonList) {
			seasonMap.put(String.valueOf(obj[0]), obj[1] + " - " + obj[0]);
		}
		return seasonMap;
	}
	
	/*public String getTxnType(String txnType) {
		String val = "";
		if (!StringUtil.isEmpty(txnType)) {
			if (txnType.equalsIgnoreCase(PaymentMode.DISTIBUTION_PAYMENT_TXN)) {
				val = getText("distributionPayment");
			} else if (txnType.equalsIgnoreCase(PaymentMode.PROCURMENT_PAYMENT_TXN)) {
				val = getText("procurementPayment");
			}
		}
		return val;

	}*/
	
	public String getTxnType(String txnDesc){
		String value="";
			if(!StringUtil.isEmpty(txnDesc)){
				String[] val=txnDesc.split("\\|");
				
				value=val[0];
		}
			
		return value;
	}
	
	public String getRemarks(String txnDesc){
		String value="";
			if(!StringUtil.isEmpty(txnDesc) && txnDesc.contains("|")){
				String[] val=txnDesc.split("\\|");
				if(val.length>0)
				if(!ObjectUtil.isEmpty(val[1])) {
					value=val[1];
				}
					
		}
			
		return value;
	}
	
	
	
	
	public Map<String, String> getAgentList() {
		Map<String, String> farmerMap = new LinkedHashMap<>();
		List<Object[]> farmerList = locationService.listOfAgentsByAgroTxn();
		List<String> testList = new ArrayList<>();
		if (!ObjectUtil.isListEmpty(farmerList)) {
			testList = farmerList.stream().filter(obj -> (obj[1]) != null && !StringUtil.isEmpty(obj[1].toString()))
					.map(obj -> String.valueOf(obj[1])).distinct().collect(Collectors.toList());
			farmerMap = testList.stream().collect(Collectors.toMap(String::toString, String::toString));

		}
		return farmerMap;
	}
	
	/*public Map<String, String> getFarmerFirstNameList() {
		Map<String, String> farmerMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(farmerList)) {
			farmerList = farmerService.listFarmerInfo();
		}
		farmerList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			farmerMap.put(String.valueOf(objArr[3]), String.valueOf(objArr[3]));
		});

		return farmerMap;
	}*/
	
	public Map<String, String> getFarmerFirstNameList() {
		Map<String, String> farmerMap = new LinkedHashMap<>();
		List<Object[]> farmerList = locationService.listOfFarmersByAgroTxn();
		List<String> testList = new ArrayList<>();
		if (!ObjectUtil.isListEmpty(farmerList)) {
			testList = farmerList.stream().filter(obj -> (obj[1]) != null && !StringUtil.isEmpty(obj[1].toString()))
					.map(obj -> String.valueOf(obj[1])).distinct().collect(Collectors.toList());
			farmerMap = testList.stream().collect(Collectors.toMap(String::toString, String::toString));

		}
		return farmerMap;
	}
	
	public String populateXLS() throws Exception {

		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getText("PaymentReportList") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("paymentReportList"), fileMap, ".xls"));
		return "xls";
	}
	

	HSSFRow row, titleRow, filterRowTitle, filterRow1, filterRow2, filterRow3;
	int colCount, rowCount,titleRow1, titleRow2;;
	Cell cell;
	Integer cellIndex;
	Long serialNumber = 0L;
	int mainGridIterator=0;
	@Override
	public InputStream getExportDataStream(String exportType) throws IOException {
		AgroTransaction agroTxn = new AgroTransaction();
		
		if (!StringUtil.isEmpty(filterList)) {
			try {
				Type listType1 = new TypeToken<List<FilterFieldData>>() {
				}.getType();
				List<FilterFieldData> filtersList = new Gson().fromJson(filterList, listType1);
				agroTxn.setFilterData(filtersList.stream()
						.collect(Collectors.toMap(FilterFieldData::getName, obj -> obj.getValue())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.filter = agroTxn;

		Map data = null;
		if (dynamicReportConfig.getFetchType() == 2L) {
			data = readProjectionData(dynamicReportConfig.getDynmaicReportConfigDetails());
		} else {
			data = readData();
		}
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(getLocaleProperty("exportPaymentTitle"));
		HSSFPatriarch drawing = sheet.createDrawingPatriarch();
		
		DataFormat format = workbook.createDataFormat();

		HSSFCellStyle style1 = workbook.createCellStyle();
		HSSFCellStyle style2 = workbook.createCellStyle();
		HSSFCellStyle style3 = workbook.createCellStyle();
		HSSFCellStyle style4 = workbook.createCellStyle();
		HSSFCellStyle filterStyle = workbook.createCellStyle();

		HSSFFont font1 = workbook.createFont();
		font1.setFontHeightInPoints((short) 22);

		HSSFFont font2 = workbook.createFont();
		font2.setFontHeightInPoints((short) 12);

		HSSFFont font3 = workbook.createFont();
		font3.setFontHeightInPoints((short) 10);

		HSSFFont filterFont = workbook.createFont();
		filterFont.setFontHeightInPoints((short) 12);

		int imgRow1 = 0;
		int imgRow2 = 4;
		int imgCol1 = 0;
		int imgCol2 = 0;
		rowCount = 2;
		colCount = 0;
		int titleRow1 = 2;
		int titleRow2 = 5;

		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, titleRow1, titleRow2));

		titleRow = sheet.createRow(rowCount++);
		cell = titleRow.createCell(2);
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cell.setCellStyle(style1);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("exportPaymentTitle")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);
		rowCount++;
		rowCount++;
	
			if(!StringUtil.isEmpty(startDate) || !StringUtil.isEmpty(endDate) ){
				
				// Filter Fields
				filterRowTitle = sheet.createRow(rowCount++);
				cell = filterRowTitle.createCell(0);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("filter")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
				
				filterRow1 = sheet.createRow(rowCount++);
				cell = filterRow1.createCell(0);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("StartingDate")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(startDate));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				filterRow2 = sheet.createRow(rowCount++);
				cell = filterRow2.createCell(0);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("EndingDate")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow2.createCell(1);
				cell.setCellValue(new HSSFRichTextString(endDate));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
				
			}
			if(!StringUtil.isEmpty(agroTxn.getFilterData()) || agroTxn.getFilterData()!=null){
			for (Map.Entry<String, String> entry : agroTxn.getFilterData().entrySet()) {
				if (entry.getKey().contains("seasonCode")) {

					filterRow1 = sheet.createRow(rowCount++);
					cell = filterRow1.createCell(0);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("season")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow1.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getSeasonByCode(entry.getValue())));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);
					
					
				

				}

				if (entry.getKey().contains("farmerName")) {

					filterRow2 = sheet.createRow(rowCount++);
					cell = filterRow2.createCell(0);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmer")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow2.createCell(1);
					cell.setCellValue(new HSSFRichTextString(entry.getValue()));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

				}
				
				if (entry.getKey().contains("agentName")) {

					filterRow2 = sheet.createRow(rowCount++);
					cell = filterRow2.createCell(0);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("agent")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow2.createCell(1);
					cell.setCellValue(new HSSFRichTextString(entry.getValue()));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

				}
			}
			
		
		}

		List<Object[]> mainGridRows = (List<Object[]>) data.get(ROWS);

		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;
		
		HSSFCellStyle header_style = workbook.createCellStyle();
		header_style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		
		header_style.setWrapText(true);

		HSSFCellStyle sub_header_style = workbook.createCellStyle();
		
		sub_header_style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		
		sub_header_style.setWrapText(true);

		HSSFCellStyle sub_Cell_style = workbook.createCellStyle();
		
		sub_Cell_style.setWrapText(true);
		Map<String,HSSFCellStyle> align = new HashMap<String,HSSFCellStyle>();
		HSSFCellStyle intFormat = workbook.createCellStyle();
		intFormat.setDataFormat((short) 0);
		intFormat.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCellStyle intFormatRight = workbook.createCellStyle();
		intFormatRight.setDataFormat((short) 0);
		intFormatRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		HSSFCellStyle intFormatCenter = workbook.createCellStyle();
		intFormatCenter.setDataFormat((short) 0);
		intFormatCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		align.put("", intFormat);
		align.put(null, intFormat);
		align.put("left", intFormat);
		align.put("right", intFormatRight);
		align.put("center", intFormatCenter);
		mainGridCols = "";
		row = sheet.createRow(rowCount++);
		
		/*if (StringUtil.isEmpty(getBranchId())) {
			mainGridCols = getLocaleProperty("branchId") + "#" + "50%";
		}*/
		
		cell = row.createCell(colCount++);
		cell.setCellValue("S.No");
		cell.setCellStyle(header_style);
		filterFont.setBoldweight((short) 5);
		filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		filterStyle.setFont(filterFont);
		cell.setCellStyle(filterStyle);
		
		style3.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
		style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cell.setCellStyle(style3);
		style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cell.setCellStyle(style3);
		font3.setBoldweight((short) 10);
		font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style3.setFont(font3);
		sheet.setColumnWidth(mainGridIterator, (15 * 550));
		
		dynamicReportConfig.getDynmaicReportConfigDetails().stream().filter(config -> config.getIsGridAvailabiltiy())
				.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder())).forEach(dynamicReportConfigDetail -> {
					cell = row.createCell(colCount++);
					cell.setCellValue(getLocaleProperty(dynamicReportConfigDetail.getLabelName()));
					cell.setCellStyle(header_style);
					filterFont.setBoldweight((short) 5);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);
					
					style3.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
					style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			        cell.setCellStyle(style3);
					style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					cell.setCellStyle(style3);
					font3.setBoldweight((short) 10);
					font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					style3.setFont(font3);
					sheet.setColumnWidth(mainGridIterator, (15 * 550));
					mainGridIterator++;
					
				});
		sheet.setColumnWidth(mainGridIterator, (15 * 550));

		mainGridRows.stream().forEach(arr -> {
			serialNumber++;
			row = sheet.createRow(rowCount++);
			AtomicInteger colCount = new AtomicInteger(0);
			Long agroTxnId = (Long) arr[0];
			dynamicReportConfig.getDynmaicReportConfigDetails().stream()
					.filter(config -> config.getIsGridAvailabiltiy())
					.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
					.forEach(dynamicReportConfigDetail -> {
						cell = row.createCell(0);
						cell.setCellValue(new HSSFRichTextString(String.valueOf(serialNumber)!=null ? String.valueOf(serialNumber) : ""));
						style4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
						cell.setCellStyle(style4);
						cell = row.createCell(colCount.getAndIncrement()+1);
						if (dynamicReportConfigDetail.getAlignment() != null
								&& !StringUtil.isEmpty(dynamicReportConfigDetail.getAlignment())) {
							if(dynamicReportConfigDetail.getAlignment().equalsIgnoreCase("center")){
								style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
								cell.setCellStyle(style2);
							}
							else if (dynamicReportConfigDetail.getAlignment().equalsIgnoreCase("left")) {
								style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
								cell.setCellStyle(style2);
							}else if (dynamicReportConfigDetail.getAlignment().equalsIgnoreCase("right")) {
								style2.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
								cell.setCellStyle(style2);
							} 
						}
						if (dynamicReportConfigDetail.getAccessType() == 1L) {
							fValue = ReflectUtil.getObjectFieldValue(arr, String.valueOf(colCount));
							if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
								if (fValue instanceof Long) {
									cell.setCellValue((Long) fValue);															
								}else if (fValue instanceof Double) {
									cell.setCellValue((Double) fValue);
									//dynamicReportConfigDetail.setAlignment(dynamicReportConfigDetail.getAlignment()==null || StringUtil.isEmpty(dynamicReportConfigDetail.getAlignment())?"left":dynamicReportConfigDetail.getAlignment());
									cell.setCellStyle(align.get(dynamicReportConfigDetail.getAlignment().toLowerCase().trim()));
									/*if(StringUtil.isEmpty(dynamicReportConfigDetail.getAlignment()) && dynamicReportConfigDetail.getAlignment().equalsIgnoreCase("left")){
									cell.setCellStyle(intFormat);}
									else{
									cell.setCellStyle(intFormatRight);	
									}	*/								
								}  else {
									cell.setCellValue(fValue.toString());
								}
							} else {
								cell.setCellValue("");
							}
						} else if (dynamicReportConfigDetail.getAccessType() == 2L) {
							fValue = ReflectUtil.getObjectFieldValue(arr, String.valueOf(colCount));
							if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
								mValue = getMethodValue(dynamicReportConfigDetail.getMethod(), fValue.toString());
								if (!ObjectUtil.isEmpty(mValue) && !StringUtil.isEmpty(mValue)) {
									if (fValue instanceof Integer) {
										cell.setCellValue((Integer) mValue);
									} else {
										cell.setCellValue(mValue.toString());
									}
								} else {
									cell.setCellValue("");
								}
							}
						}
					});

			
			AgroTransaction agroTxns = new AgroTransaction();
			agroTxns.setId(agroTxnId);
			super.filter = agroTxns;
			
			
			
		});

		/*for (int i = 0; i <= colCount; i++) {
			sheet.autoSizeColumn(i);
		}*/

		int pictureIdx = getPicIndex(workbook);
		HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		// picture.resize();

		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("paymentReportList") + fileNameDateFormat.format(new Date()) + ".xls";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		workbook.write(fileOut);
		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		fileOut.close();

				return stream;
	}
	
	
	public int getPicIndex(HSSFWorkbook workbook) throws IOException {

		int index = -1;

		byte[] picData = null;
		picData = clientService.findLogoByCode(Asset.APP_LOGO);

		if (picData != null)
			index = workbook.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
	}

	
	
	public String getMainGridCols() {
		return mainGridCols;
	}

	public void setMainGridCols(String mainGridCols) {
		this.mainGridCols = mainGridCols;
	}

	public static String getSubGridCols() {
		return subGridCols;
	}

	public static void setSubGridCols(String subGridCols) {
		PaymentReportAction.subGridCols = subGridCols;
	}

	public static DynamicReportConfig getDynamicReportConfig() {
		return dynamicReportConfig;
	}

	public static void setDynamicReportConfig(DynamicReportConfig dynamicReportConfig) {
		PaymentReportAction.dynamicReportConfig = dynamicReportConfig;
	}

	public List<DynamicReportConfigFilter> getReportConfigFilters() {
		return reportConfigFilters;
	}

	public void setReportConfigFilters(List<DynamicReportConfigFilter> reportConfigFilters) {
		this.reportConfigFilters = reportConfigFilters;
	}


	public String getFilterList() {
		return filterList;
	}


	public void setFilterList(String filterList) {
		this.filterList = filterList;
	}


	public String getGridIdentity() {
		return gridIdentity;
	}


	public void setGridIdentity(String gridIdentity) {
		this.gridIdentity = gridIdentity;
	}


	public static SimpleDateFormat getFilenamedateformat() {
		return fileNameDateFormat;
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


	public String getExportLimit() {
		return exportLimit;
	}


	public void setExportLimit(String exportLimit) {
		this.exportLimit = exportLimit;
	}


	public String getDaterange() {
		return daterange;
	}


	public void setDaterange(String daterange) {
		this.daterange = daterange;
	}

	
	
}
