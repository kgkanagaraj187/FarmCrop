/*
F * ProductReturnReportAction.java
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
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.FilterFieldData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sourcetrace.eses.dao.IReportDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.AgentType;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfig;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfigFilter;
import com.sourcetrace.eses.order.entity.txn.ProductReturn;
import com.sourcetrace.eses.order.entity.txn.ProductReturnDetail;
import com.sourcetrace.eses.service.DynamicReportProperties;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

/**
 * The Class DistributionReportAction.
 */
public class ProductReturnFromFieldStaffReportAction extends BaseReportAction implements IExporter {

	private static final long serialVersionUID = 4897676200025482185L;

	private static final String FARMER = "farmer";
	private static final String AGENT = "agent";
	private static DynamicReportConfig dynamicReportConfig;
	private static DynamicReportConfig subDynamicReportConfig;
	private List<DynamicReportConfigFilter> reportConfigFilters = new LinkedList<>();
	private String gridIdentity;
	private String mainGridCols;
	private static String subGridCols;
	
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private Object fValue;
	private Object mValue;
	private IFarmerService farmerService;
	private ILocationService locationService;
	private IAgentService agentService;
	private IClientService clientService;
	private IPreferencesService preferncesService;
	private IProductDistributionService productDistributionService;
	private String type;
	private String filterList;
	private InputStream fileInputStream;
	@Autowired
	private IProductService productService;
	
	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}

	public String getXlsFileName() {
		return xlsFileName;
	}

	public void setXlsFileName(String xlsFileName) {
		this.xlsFileName = xlsFileName;
	}

	private String xlsFileName;
	
	public String getFilterList() {
		return filterList;
	}

	public void setFilterList(String filterList) {
		this.filterList = filterList;
	}
	
	public String getSubGridCols() {
		return subGridCols;
	}

	public void setSubGridCols(String subGridCols) {
		this.subGridCols = subGridCols;
	}

	HSSFRow row, titleRow, filterRowTitle, filterRow1, filterRow2, filterRow3, filterRow4, filterRow5, filterRow6,
	filterRow7;
int colCount, rowCount, titleRow1, titleRow2;
Cell cell;
Integer cellIndex;

	public String list() {
		formMainGridCols();
		if(!ObjectUtil.isEmpty(dynamicReportConfig)){
		setFilterSize(String.valueOf(dynamicReportConfig.getDynmaicReportConfigFilters().size()));
		dynamicReportConfig.getDynmaicReportConfigFilters().stream().forEach(reportConfigFilter -> {
			Map<String, String> optionMap = (Map<String, String>) getMethodValue(reportConfigFilter.getMethod(), "");
			reportConfigFilter.setOptions(optionMap);
			reportConfigFilters.add(reportConfigFilter);
		});
	}
		return LIST;
	}

	public String detail() throws Exception {
		
		ProductReturnDetail productReturnDetail = new ProductReturnDetail();
		if (!StringUtil.isEmpty(filterList)) {
			try {
				Type listType1 = new TypeToken<List<FilterFieldData>>() {
				}.getType();
				List<FilterFieldData> filtersList = new Gson().fromJson(filterList, listType1);
				productReturnDetail.setFilterData(filtersList.stream()
						.collect(Collectors.toMap(FilterFieldData::getName, obj -> obj.getValue().split("~")[0].trim())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(!StringUtil.isEmpty(type) && type.equalsIgnoreCase(AGENT))
		{
			
			ProductReturn productReturn=new ProductReturn();
			productReturn.setFarmerId("");
			productReturn.setTxnType(AGENT);			
			productReturn.setBranchId(getBranchId());
			productReturnDetail.setProductReturn(productReturn);
		}
		
		super.filter = productReturnDetail;

		Map data = null;
	
			if (dynamicReportConfig.getFetchType() == 2L) {
				data = readProjectionData(dynamicReportConfig.getDynmaicReportConfigDetails());
			} else {
				data = readData();
			}
	
		setGridIdentity(IReportDAO.MAIN_GRID);
		return sendJSONResponse(data);
	}

	@SuppressWarnings("unchecked")
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
					if (StringUtil.isEmpty(getBranchId())) {  
						dynamicReportConfig.getDynmaicReportConfigDetails().stream()
								.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder())).limit(1)
								.forEach(dynamicReportConfigDetail -> {
									fValue = ReflectUtil.getObjectFieldValue(arr,
											String.valueOf(runCount.getAndIncrement()));

									rows.add(getBranchesMap().get(fValue));
								});
					}else{
						runCount.getAndIncrement();
					}
					
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
								}
							});
				}
			} else if (!StringUtil.isEmpty(dynamicReportConfig)) {
				subDynamicReportConfig.getDynmaicReportConfigDetails().stream()
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
									mValue = getMethodValue(dynamicReportConfigDetail.getMethod(), fValue.toString());
									if (!ObjectUtil.isEmpty(mValue) && !StringUtil.isEmpty(mValue)) {
										rows.add(mValue.toString());
									} else {
										rows.add("");
									}
								} else {
									rows.add("");
								}
							}
						});

			}
			jsonObject.put("id", id);
			jsonObject.put("cell", rows);

		}
		return jsonObject;

	}

	public String getStockType(String stockType) {

		if (stockType != null) {
			stockType = ProductReturn.WAREHOUSE_STOCK;
		} else {
			stockType = ProductReturn.MOBILE_STOCK;
		}
		return stockType;

	}

	public String getFarmerType(String farmerId) {

		String farmerType = null;

		if (farmerId != null) {
			Farmer farmer = farmerService.findFarmerByFarmerId(farmerId);
			if (!StringUtil.isEmpty(farmer)) {

				farmerType = ProductReturn.FARMER_REGISTERED;
			} else {
				farmerType = ProductReturn.FARMER_UN_REGISTERD;
			}
		}
		return farmerType;

	}

	double totQty = 0.0;

	public String getTotalQty(String id) {
		totQty=0.0;
		if (id != null) {

			ProductReturn productReturn = productDistributionService.findProductReturnById(Long.valueOf(id));

			productReturn.getProductReturnDetail().forEach(item -> totQty += item.getQuantity());

		}

		return CurrencyUtil.getDecimalFormat(totQty, "##.0000");

	}
	
	public String getSeasonName(String code) {
		String season = getSeasonsList().get(code);
		return season;
	}

	private void formMainGridCols() {
		dynamicReportConfig = clientService.findReportByName(DynamicReportProperties.PRODUCT_RETURN_FROM_FIELDSTAFF_REPORT);
		if (!ObjectUtil.isEmpty(dynamicReportConfig)) {
			mainGridCols = "";
			if (StringUtil.isEmpty(getBranchId())) {
				mainGridCols = getLocaleProperty("branchId") + "#" + "150%";
			}
			dynamicReportConfig.getDynmaicReportConfigDetails().stream()
					.filter(config -> config.getIsGridAvailabiltiy())
					.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
					.forEach(dynamicReportConfigDetail -> {
						mainGridCols += getLocaleProperty(dynamicReportConfigDetail.getLabelName()) + "#"
								+ dynamicReportConfigDetail.getWidth() + "%";
					});
		}

		/*subDynamicReportConfig = clientService.findReportByName(DynamicReportProperties.PRODUCT_RETURN_FROM_FIELDSTAFF_REPORT_SUB);
		if (!ObjectUtil.isEmpty(subDynamicReportConfig)) {
			subGridCols = "";
			subDynamicReportConfig.getDynmaicReportConfigDetails().stream()
					.filter(config -> config.getIsGridAvailabiltiy())
					.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
					.forEach(dynamicReportConfigDetail -> {
						subGridCols += getLocaleProperty(dynamicReportConfigDetail.getLabelName()) + "#"
								+ dynamicReportConfigDetail.getWidth() + "%";
					});
		}*/
	}

	
	
	public static DynamicReportConfig getDynamicReportConfig() {
		return dynamicReportConfig;
	}

	public static void setDynamicReportConfig(DynamicReportConfig dynamicReportConfig) {
		ProductReturnFromFieldStaffReportAction.dynamicReportConfig = dynamicReportConfig;
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

	public String getMainGridCols() {
		return mainGridCols;
	}

	public void setMainGridCols(String mainGridCols) {
		this.mainGridCols = mainGridCols;
	}

	

	public List<DynamicReportConfigFilter> getReportConfigFilters() {
		return reportConfigFilters;
	}

	public void setReportConfigFilters(List<DynamicReportConfigFilter> reportConfigFilters) {
		this.reportConfigFilters = reportConfigFilters;
	}

	public String getGridIdentity() {
		return gridIdentity;
	}

	public void setGridIdentity(String gridIdentity) {
		this.gridIdentity = gridIdentity;
	}

	public Object getfValue() {
		return fValue;
	}

	public void setfValue(Object fValue) {
		this.fValue = fValue;
	}

	public Object getmValue() {
		return mValue;
	}

	public void setmValue(Object mValue) {
		this.mValue = mValue;
	}

	public Map<String, String> getSeasonsList() {

		Map<String, String> seasonMap = new LinkedHashMap<String, String>();
		List<Object[]> seasonList = farmerService.listSeasonCodeAndName();

		seasonMap = seasonList.stream()
				.collect(Collectors.toMap(obj -> (String.valueOf(obj[0])), obj -> String.valueOf(obj[1])));
		return seasonMap;
	}
	
	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	public ILocationService getLocationService() {
		return locationService;
	}

	public void setLocationService(ILocationService locationService) {
		this.locationService = locationService;
	}

	public IAgentService getAgentService() {
		return agentService;
	}

	public void setAgentService(IAgentService agentService) {
		this.agentService = agentService;
	}

	public IClientService getClientService() {
		return clientService;
	}

	public void setClientService(IClientService clientService) {
		this.clientService = clientService;
	}

	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public IProductDistributionService getProductDistributionService() {
		return productDistributionService;
	}

	public void setProductDistributionService(IProductDistributionService productDistributionService) {
		this.productDistributionService = productDistributionService;
	}
	
	public Map<String, String> getCooperativeManagetList() {
		
		Map<String, String> farmerMap = new LinkedHashMap<>();
		List<Object[]> farmerList = locationService.listOfCooperativesByDistribution();
		List<String> testList = new ArrayList<>();
		if (!ObjectUtil.isListEmpty(farmerList)) {
			testList = farmerList.stream().filter(obj -> (obj[1])!=null && !StringUtil.isEmpty(obj[1].toString())).map(obj -> String.valueOf(obj[1])).distinct().collect(Collectors.toList());
			farmerMap = testList.stream().collect(Collectors.toMap(String::toString, String::toString));

		}
		return farmerMap;
	}
	
	public Map<String, String> getFieldStaffList() {
		Map<String, String> farmerMap = new LinkedHashMap<>();
		List<Object[]> farmerList = locationService.listOfCooperativesByDistribution();
		List<String> testList = new ArrayList<>();
		if (!ObjectUtil.isListEmpty(farmerList)) {
			testList = farmerList.stream().filter(obj -> (obj[3])!=null && !StringUtil.isEmpty(obj[3].toString())).map(obj -> String.valueOf(obj[3])).distinct().collect(Collectors.toList());
			farmerMap = testList.stream().collect(Collectors.toMap(String::toString, String::toString));

		}
		return farmerMap;
	}
	
	/**
	 * Gets the season list.
	 * 
	 * @return the season list
	 */
	public Map<String, String> getSeasonList() {

		List<Season> seasonList = productDistributionService.listSeasons();

		Map<String, String> seasonYearList = new LinkedHashMap<String, String>();
		for (Season season : seasonList) {
			seasonYearList.put(season.getCode(), season.getName());
			/*
			 * seasonYearList.put(season.getCode(), season.getName() + " - " +
			 * season.getYear());
			 */
		}

		return seasonYearList;
	}
	
	int mainGridIterator = 0;
	Long serialNumber = 0L;
	@Override
	public InputStream getExportDataStream(String exportType) throws IOException {

		ProductReturnDetail productReturnDetail = new ProductReturnDetail();
		if (!StringUtil.isEmpty(filterList)) {
			try {
				Type listType1 = new TypeToken<List<FilterFieldData>>() {
				}.getType();
				List<FilterFieldData> filtersList = new Gson().fromJson(filterList, listType1);
				productReturnDetail.setFilterData(filtersList.stream()
						.collect(Collectors.toMap(FilterFieldData::getName, obj -> obj.getValue().split("~")[0].trim())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		ProductReturn productReturn=new ProductReturn();
		productReturn.setFarmerId("");
		productReturn.setTxnType(AGENT);			
		productReturn.setBranchId(getBranchId());
		productReturnDetail.setProductReturn(productReturn);
		super.filter = productReturnDetail;
		Map data = null;
		if (dynamicReportConfig.getFetchType() == 2L) {
			data = readProjectionData(dynamicReportConfig.getDynmaicReportConfigDetails());
		} else {
			data = readData();
		}
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(getText("productReturnAgentReportList"));
		HSSFPatriarch drawing = sheet.createDrawingPatriarch();
		sheet.setDefaultColumnWidth(13);

		DataFormat format = workbook.createDataFormat();

		HSSFCellStyle style1 = workbook.createCellStyle();
		HSSFCellStyle style2 = workbook.createCellStyle();
		HSSFCellStyle style3 = workbook.createCellStyle();
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
		titleRow1=2;
		titleRow2=5;
		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, titleRow1, titleRow2));
		
		sheet.setDefaultColumnWidth(13);
		
		titleRow = sheet.createRow(rowCount++);
		cell = titleRow.createCell(2);
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("productReturnAgentReportList")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);
		
		rowCount++;
		rowCount++;
		if (!StringUtil.isEmpty(filterList)) {
			try {
				Type listType1 = new TypeToken<List<FilterFieldData>>() {
				}.getType();
				List<FilterFieldData> filtersList = new Gson().fromJson(filterList, listType1);
				productReturnDetail.setFilterData(filtersList.stream()
						.collect(Collectors.toMap(FilterFieldData::getName, obj -> obj.getValue())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(!StringUtil.isEmpty(productReturnDetail.getFilterData()) || productReturnDetail.getFilterData()!=null){
			// Filter Fields
			
			rowCount++;
			rowCount++;
			filterRowTitle = sheet.createRow(rowCount++);
			cell = filterRowTitle.createCell(2);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("filter")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);
			Map<String, String> tempMap=new LinkedHashMap<>();
			dynamicReportConfig.getDynmaicReportConfigFilters().stream().forEach(a->{
				tempMap.put(a.getField(), a.getLabel());
			});
			productReturnDetail.getFilterData().entrySet().stream().filter(f -> f.getValue().split("~")[0]!="" && !StringUtil.isEmpty(f.getValue().split("~")[0].trim())).forEach(e -> {
				filterRow1 = sheet.createRow(rowCount++);
				cell = filterRow1.createCell(2);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty(tempMap.get(e.getKey()))));
				filterFont.setBoldweight((short) 5);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
				
				cell = filterRow1.createCell(3);
				cell.setCellValue(new HSSFRichTextString(e.getValue().split("~")[1].trim()));
				filterFont.setBoldweight((short) 5);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			});
			filterRow1 = sheet.createRow(rowCount++);
			cell = filterRow1.createCell(2);
			cell.setCellValue(new HSSFRichTextString(""));
			filterFont.setBoldweight((short) 5);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);
			
			cell = filterRow1.createCell(3);
			cell.setCellValue(new HSSFRichTextString(""));
			filterFont.setBoldweight((short) 5);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);
			rowCount++;
			
/*for (Map.Entry<String, String> entry : productReturnDetail.getFilterData().entrySet()) {
				if (entry.getKey().contains("seasonCode")) {

					filterRow1 = sheet.createRow(rowCount++);
					cell = filterRow1.createCell(2);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("seasonCode")));
					filterFont.setBoldweight((short) 5);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow1.createCell(3);
					cell.setCellValue(new HSSFRichTextString(farmerService.findBySeasonCode(entry.getValue())));
					filterFont.setBoldweight((short) 5);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

				}
				if (entry.getKey().contains("w.name")) {

					filterRow3 = sheet.createRow(rowCount++);
					cell = filterRow3.createCell(2);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("w.name")));
					filterFont.setBoldweight((short) 5);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow3.createCell(3);
					cell.setCellValue(new HSSFRichTextString(entry.getValue()));
					filterFont.setBoldweight((short) 5);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);
				}
				
				
				
			}*/
			
		}
		List<Object[]> mainGridRows = (List<Object[]>) data.get(ROWS);

		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;

		HSSFCellStyle header_style = workbook.createCellStyle();
		//header_style.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
		header_style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		header_style.setWrapText(true);
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);

		HSSFCellStyle sub_header_style = workbook.createCellStyle();
		sub_header_style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		sub_header_style.setWrapText(true);

		HSSFCellStyle sub_Cell_style = workbook.createCellStyle();
		sub_Cell_style.setWrapText(true);

		HSSFCellStyle intFormat = workbook.createCellStyle();
		intFormat.setDataFormat((short) 0);

		
		mainGridCols = "";
		row = sheet.createRow(rowCount++);
		colCount = 0;
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
		
		if (StringUtil.isEmpty(getBranchId())) {
			cell = row.createCell(colCount++);
			cell.setCellValue(getLocaleProperty("branchId"));
			cell.setCellStyle(filterStyle);
			font2.setBoldweight((short) 5);
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			style3.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
			style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			filterStyle.setFont(font2);
			//cell.setCellStyle(filterStyle);
			cell.setCellStyle(style3);
		}/* else {
			colCount++;
		}*/
	
		
		dynamicReportConfig.getDynmaicReportConfigDetails().stream().filter(config -> config.getIsGridAvailabiltiy())
				.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder())).forEach(dynamicReportConfigDetail -> {
					/*cell = row.createCell(colCount++);
					cell.setCellValue(getLocaleProperty(dynamicReportConfigDetail.getLabelName()));
					///header_style.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
					header_style.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
					header_style.setWrapText(true);
					cell.setCellStyle(header_style);
					filterFont.setBoldweight((short) 5);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					//cell.setCellStyle(filterStyle);
					sheet.setColumnWidth(mainGridIterator, (15 * 550));
					mainGridIterator++;*/
					
					
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
	
		
		mainGridRows.stream().forEach(arr -> {
			serialNumber++;
			row = sheet.createRow(rowCount++);
			AtomicInteger colCount = new AtomicInteger(0);
			Long procurmentId = (Long) arr[0];
			cell=row.createCell(0);
			cell.setCellValue(new HSSFRichTextString(String.valueOf(serialNumber)!=null ? String.valueOf(serialNumber) : ""));
			style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cell.setCellStyle(style2);
			if (StringUtil.isEmpty(getBranchId())) {
				dynamicReportConfig.getDynmaicReportConfigDetails().stream()
						.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder())).limit(1)
						.forEach(dynamicReportConfigDetail -> {
							
							cell = row.createCell(colCount.getAndIncrement()+1);
							fValue = ReflectUtil.getObjectFieldValue(arr, String.valueOf(colCount));

							cell.setCellValue(getBranchesMap().get(fValue));
						});
			} else {
				colCount.getAndIncrement();
			}
			
			dynamicReportConfig.getDynmaicReportConfigDetails().stream()
					.filter(config -> config.getIsGridAvailabiltiy())
					.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
					.forEach(dynamicReportConfigDetail -> {
						if (StringUtil.isEmpty(getBranchId())) {
							cell = row.createCell(colCount.getAndIncrement()+1);
						}else{
							cell = row.createCell(colCount.getAndIncrement());
						}
						
						if (dynamicReportConfigDetail.getAccessType() == 1L) {
							fValue = ReflectUtil.getObjectFieldValue(arr, String.valueOf(colCount));
							if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
								if (fValue instanceof Double) {
									cell.setCellValue(Double.parseDouble(fValue.toString()));
									cell.setCellStyle(intFormat);
								}  else if(fValue.toString().contains(".")){
									if(!(fValue.toString().contains(":"))){
									cell.setCellValue(Double.valueOf(fValue.toString()));
									cell.setCellStyle(intFormat);
									}else {
										cell.setCellValue(fValue.toString());
									}
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

		
		});

		/*for (int i = 0; i <= colCount; i++) {
			sheet.autoSizeColumn(i);
		}*/

		// alternateGreenAndWhiteRows(sheet);
		int pictureIdx = getPicIndex(workbook);
		HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		// picture.resize();

		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("ProductReturnAgentReportList")+ fileNameDateFormat.format(new Date()) + ".xls";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		workbook.write(fileOut);
		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		fileOut.close();
		return stream;	}

	public int getPicIndex(HSSFWorkbook workbook) throws IOException {

		int index = -1;

		byte[] picData = null;
		picData = clientService.findLogoByCode(Asset.APP_LOGO);

		if (picData != null)
			index = workbook.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
	}
	
	public String populateXLS() throws Exception {

		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(
				getText("ProductReturnReportMobileUser").replace(" ", "_") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(
				FileUtil.createFileInputStreamToZipFile(getText("productReturnAgentReportList"), fileMap, ".xls"));
		return "xls";
	}
	
	public static String removeDateDotZero(String inputDate) throws ParseException
	{ 	
	Date day = null;
		//DateFormat genDate = new SimpleDateFormat(ESESystem.GENERAL_DATE_FORMAT);
		String replaceDot=inputDate.toString().replace(".0", "");
		day= DateUtil.convertStringToDate(replaceDot, DateUtil.TXN_DATE_TIME);
		
		return String.valueOf(new SimpleDateFormat("dd-MM-yyyy").format(day));
		
	}
	public Map<String, String> getProductList() {

		Map<String, String> returnMap = new LinkedHashMap<String, String>();

		// List<Object[]> productList =
		// productService.listWarehouseStockProducts();
		List<Object[]> productList = productService.listFarmProducts();
		if (!ObjectUtil.isListEmpty(productList)) {
			for (Object[] productObj : productList) {
				// returnMap.put(productObj[0].toString(),
				// productObj[1].toString() + " - " + productObj[0].toString());
				returnMap.put(productObj[0].toString(), productObj[0].toString());
			}
		}
		return returnMap;
	}

	public IProductService getProductService() {
		return productService;
	}

	public void setProductService(IProductService productService) {
		this.productService = productService;
	}
	
}
