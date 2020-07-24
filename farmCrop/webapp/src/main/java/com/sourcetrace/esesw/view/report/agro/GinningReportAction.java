package com.sourcetrace.esesw.view.report.agro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.traceability.BaleGeneration;
import com.ese.entity.traceability.GinningProcess;
import com.ese.entity.traceability.HeapData;
import com.ese.entity.util.ESESystem;
import com.ese.entity.util.FilterFieldData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sourcetrace.eses.dao.IReportDAO;
import com.sourcetrace.eses.entity.FarmCatalogue;
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
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

public class GinningReportAction extends BaseReportAction{
	
	private static DynamicReportConfig dynamicReportConfig;
	private List<DynamicReportConfigFilter> reportConfigFilters = new LinkedList<>();
	private String mainGridCols;
	private String filterSize;
	private IFarmerService farmerService;
	private ILocationService locationService;
	private String gridIdentity;
	private Object fValue;
	private Object mValue;
	private String filterList;
	private IPreferencesService preferncesService;
	private InputStream fileInputStream;
	private String xlsFileName;
	HSSFRow row, titleRow, filterRowTitle, filterRow1, filterRow2, filterRow3;
	int colCount, rowCount,titleRow1, titleRow2;;
	Cell cell;
	Integer cellIndex;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	  private static final SimpleDateFormat SDF = new SimpleDateFormat("dd-MMM-yyyy");
	private String heapName;
	private String ginningName;
	private String ginningdate;
	List<BaleGeneration> baleGeneration;
	private String icsName;
	private String totBaleWeight;
	private String farmerName;
	List<Object[]> farmerInfo;
	@Autowired
	private IProductDistributionService productDistributionService;
	  
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
	public String list() {
		formMainGridCols();
		setFilterSize(String.valueOf(dynamicReportConfig.getDynmaicReportConfigFilters().size()));
		dynamicReportConfig.getDynmaicReportConfigFilters().stream().forEach(reportConfigFilter -> {
			Map<String, String> optionMap = (Map<String, String>) getMethodValue(reportConfigFilter.getMethod(), "");
			reportConfigFilter.setOptions(optionMap);
			reportConfigFilters.add(reportConfigFilter);
		});
		return LIST;
	}
	
	public String detail() throws Exception {
		GinningProcess ginningProcess=new GinningProcess();
		if (!StringUtil.isEmpty(filterList)) {
			try {
				Type listType1 = new TypeToken<List<FilterFieldData>>() {
				}.getType();
				List<FilterFieldData> filtersList = new Gson().fromJson(filterList, listType1);
				ginningProcess.setFilterData(filtersList.stream()
						.collect(Collectors.toMap(FilterFieldData::getName, obj -> obj.getValue())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else{
			Map<String,String> filMap=new HashMap<>();
			filMap.put("season", getCurrentSeasonsCode());
			ginningProcess.setFilterData(filMap);
		}
		if(!ginningProcess.getFilterData().containsKey("season")){
			ginningProcess.getFilterData().put("season", getCurrentSeasonsCode());
		}
		super.filter = ginningProcess;
		Map data = null;
		if (dynamicReportConfig.getFetchType() == 2L) {
			data = readProjectionData(dynamicReportConfig.getDynmaicReportConfigDetails());
		} else {
			data = readData();
		}
		
		setGridIdentity(IReportDAO.MAIN_GRID);
		return sendJSONResponse(data);
	}
	public String detail1() throws Exception {
		baleGeneration=productDistributionService.findBaleGenerationByGinningProcessId(id);
		GinningProcess ginningProcess = productDistributionService.findGinningByGinningId(Long.valueOf(id));
		ginningName=ginningProcess.getGinning().getName();
		ginningdate=DateUtil.convertDateFormat(ginningProcess.getProcessDate(), DateUtil.DATE, DateUtil.DATE_FORMAT_2);
		heapName=getCatlogueValueByCode(ginningProcess.getHeapCode()).getName();
		HeapData hd=productDistributionService.findHeapDataByGinningHeapCodeAndProduct(ginningProcess.getHeapCode() ,ginningProcess.getGinning().getId(),ginningProcess.getProduct().getId(), getCurrentTenantId(),getCurrentSeasonsCode());
		//icsName=null;
		icsName = productDistributionService.findIcsNameByIcsCode(ginningProcess.getIcs());
		//farmerName = productDistributionService.findFarmerNameByFarmerId(ginningProcess.getFarmer());
		
		farmerInfo= productDistributionService.findFarmerNameByFarmerId(ginningProcess.getFarmer());
	
		if(!ObjectUtil.isListEmpty(baleGeneration)){
		//ginningName=baleGeneration.iterator().next().getGinning().getName();
		//ginningdate=baleGeneration.iterator().next().getGinningProcess().getProcessDate();
		//heapName=catalogueService.findCatalogueByCode(baleGeneration.iterator().next().getHeap()).getName();
		/*HeapData hd=productDistributionService.findHeapDataByHeapCode(baleGeneration.iterator().next().getGinning().getId(),baleGeneration.iterator().next().getHeap() , getCurrentTenantId());
		icsName=null;
		if(hd.getIcs()!=null && !StringUtil.isEmpty(hd.getIcs())){
		if(hd.getIcs().contains(",")){
			for(String code: hd.getIcs().split(",")){
			if(icsName!=null && !StringUtil.isEmpty(icsName)){
				icsName=icsName+", "+catalogueService.findCatalogueValueByCode(code);
			}else{
				icsName=catalogueService.findCatalogueValueByCode(code);
			}
			}
		}
		else{
			icsName=catalogueService.findCatalogueValueByCode(hd.getIcs());
		}
		}*/
		DoubleSummaryStatistics  temp=baleGeneration.stream().collect(Collectors.summarizingDouble(BaleGeneration::getBaleWeight));
		totBaleWeight=(temp.getSum())/100+" (Quintals)";
		System.out.println("totBaleWeight: "+totBaleWeight);
		}
		return "detail1";
	}
	public String populateXLS() throws Exception {

		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(
				getText("GinningReturnReport").replace(" ", "_") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(
				FileUtil.createFileInputStreamToZipFile(getText("GinningReturnReportList"), fileMap, ".xls"));
		return "xls";
	}
	public InputStream getExportDataStream(String exportType) throws IOException {
		List<FilterFieldData> filtersList=new ArrayList<>(); 
		GinningProcess ginningProcess=new GinningProcess();
		if (!StringUtil.isEmpty(filterList)) {
			try {
				Type listType1 = new TypeToken<List<FilterFieldData>>() {
				}.getType();
				filtersList = new Gson().fromJson(filterList, listType1);
				ginningProcess.setFilterData(filtersList.stream()
						.collect(Collectors.toMap(FilterFieldData::getName, obj -> obj.getValue())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else{
			Map<String,String> filMap=new HashMap<>();
			filMap.put("season", getCurrentSeasonsCode());
			ginningProcess.setFilterData(filMap);
		}
		super.filter = ginningProcess;
		Map data = null;
		if (dynamicReportConfig.getFetchType() == 2L) {
			data = readProjectionData(dynamicReportConfig.getDynmaicReportConfigDetails());
		} else {
			data = readData();
		}
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(getLocaleProperty("exportProductReturnTitle"));
		HSSFPatriarch drawing = sheet.createDrawingPatriarch();

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
		int imgRow2 = 3;
		int imgCol1 = 0;
		int imgCol2 = 1;
		rowCount = 3;
		colCount = 0;

		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, titleRow1, titleRow2));
		
		titleRow = sheet.createRow(rowCount++);
		cell = titleRow.createCell(4);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("exportProductReturnTitle")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);
		
		List<Object[]> mainGridRows = (List<Object[]>) data.get(ROWS);

		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;

		HSSFCellStyle header_style = workbook.createCellStyle();
		header_style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		/*header_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);*/
		header_style.setWrapText(true);

		HSSFCellStyle sub_header_style = workbook.createCellStyle();
		/*sub_header_style.setBorderTop(BorderStyle.MEDIUM);
		sub_header_style.setBorderBottom(BorderStyle.MEDIUM);
		sub_header_style.setBorderLeft(BorderStyle.MEDIUM);
		sub_header_style.setBorderRight(BorderStyle.MEDIUM);*/
		sub_header_style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		//sub_header_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		sub_header_style.setWrapText(true);

		HSSFCellStyle sub_Cell_style = workbook.createCellStyle();
		/*sub_Cell_style.setBorderTop(BorderStyle.MEDIUM);
		sub_Cell_style.setBorderBottom(BorderStyle.MEDIUM);
		sub_Cell_style.setBorderLeft(BorderStyle.MEDIUM);
		sub_Cell_style.setBorderRight(BorderStyle.MEDIUM);
		sub_Cell_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);*/
		sub_Cell_style.setWrapText(true);

		HSSFCellStyle intFormat = workbook.createCellStyle();
		intFormat.setDataFormat((short) 0);
		if(filtersList!=null && !filtersList.isEmpty()){
			row = sheet.createRow(rowCount++);
			cell = row.createCell(2);
			cell.setCellValue(getLocaleProperty("filter"));
			cell.setCellStyle(header_style);
			filterFont.setBoldweight((short) 5);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);
			
			
			filtersList.stream().forEach(u ->{
				row = sheet.createRow(rowCount++);
				cell = row.createCell(2);
				cell.setCellValue(getLocaleProperty(u.getLabel()));
				cell.setCellStyle(header_style);
				filterFont.setBoldweight((short) 5);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
				
				cell = row.createCell(3);
				cell.setCellValue(u.getFieldtxt());
				cell.setCellStyle(header_style);
			/*	filterFont.setBoldweight((short) 5);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			*/	filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			});
		}

		mainGridCols = "";
		row = sheet.createRow(rowCount++);
		
		colCount = 0;
		if (StringUtil.isEmpty(getBranchId())) {
			cell = row.createCell(colCount++);
			cell.setCellValue(getLocaleProperty("branchId"));
			cell.setCellStyle(filterStyle);
			font2.setBoldweight((short) 5);
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(font2);
			cell.setCellStyle(filterStyle);
		} else {
			colCount++;
		}

		
		dynamicReportConfig.getDynmaicReportConfigDetails().stream().filter(config -> config.getIsGridAvailabiltiy())
				.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder())).forEach(dynamicReportConfigDetail -> {
					cell = row.createCell(colCount++);
					cell.setCellValue(getLocaleProperty(dynamicReportConfigDetail.getLabelName()));
					cell.setCellStyle(header_style);
					filterFont.setBoldweight((short) 5);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);
				});
		
	
		mainGridRows.stream().forEach(arr -> {
			row = sheet.createRow(rowCount++);
			AtomicInteger colCount = new AtomicInteger(0);
			Long procurmentId = (Long) arr[0];
			if (StringUtil.isEmpty(getBranchId())) {
				dynamicReportConfig.getDynmaicReportConfigDetails().stream()
						.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder())).limit(1)
						.forEach(dynamicReportConfigDetail -> {
							cell = row.createCell(colCount.getAndIncrement());
							fValue = ReflectUtil.getObjectFieldValue(arr, String.valueOf(colCount));

							cell.setCellValue(getBranchesMap().get(fValue));
						});
				// cell.setCellStyle(rows);
			} else {
				colCount.getAndIncrement();
			}
			dynamicReportConfig.getDynmaicReportConfigDetails().stream()
					.filter(config -> config.getIsGridAvailabiltiy())
					.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
					.forEach(dynamicReportConfigDetail -> {
						cell = row.createCell(colCount.getAndIncrement());
						if (dynamicReportConfigDetail.getAccessType() == 1L) {
							fValue = ReflectUtil.getObjectFieldValue(arr, String.valueOf(colCount));
							if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
								if (fValue instanceof Long) {
									cell.setCellValue((Long) fValue);
									cell.setCellStyle(intFormat);
								} else {
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

		for (int i = 0; i <= colCount; i++) {
			sheet.autoSizeColumn(i);
		}

		// alternateGreenAndWhiteRows(sheet);
		int pictureIdx = getPicIndex(workbook);
		HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		// picture.resize();

		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("ginningReportList") + fileNameDateFormat.format(new Date()) + ".xls";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		workbook.write(fileOut);
		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		fileOut.close();

				return stream;}

	public int getPicIndex(HSSFWorkbook workbook) throws IOException {

		int index = -1;

		byte[] picData = null;
		picData = clientService.findLogoByCode(Asset.APP_LOGO);

		if (picData != null)
			index = workbook.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
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
					} else {
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
								} else if (dynamicReportConfigDetail.getAccessType() == 3L) {
									fValue = ReflectUtil.getObjectFieldValue(arr,
											dynamicReportConfigDetail.getParameters());
									if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
										mValue = getMethodValue(dynamicReportConfigDetail.getMethod(), fValue);
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
			} 
			jsonObject.put("id", id);
			jsonObject.put("cell", rows);

		}
		return jsonObject;
	}
	
	public String getFilterSize() {
		return filterSize;
	}

	public void setFilterSize(String filterSize) {
		this.filterSize = filterSize;
	}

	private void formMainGridCols() {
		dynamicReportConfig = clientService.findReportByName(DynamicReportProperties.GINNING_REPORT);
		if (!ObjectUtil.isEmpty(dynamicReportConfig)) {
			mainGridCols = "";
			if (StringUtil.isEmpty(getBranchId())) {
				mainGridCols = getLocaleProperty("branchId") + "#" + "50%";
			}
			dynamicReportConfig.getDynmaicReportConfigDetails().stream()
					.filter(config -> config.getIsGridAvailabiltiy())
					.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
					.forEach(dynamicReportConfigDetail -> {
						if(dynamicReportConfigDetail.getLabelName().contains("@"))
						{
							String label=dynamicReportConfigDetail.getLabelName();
							mainGridCols += getLocaleProperty(label) +" ("+getCurrencyType()+")" + "#"
									+ dynamicReportConfigDetail.getWidth() + "%";
						}
						else
						{
							mainGridCols += getLocaleProperty(dynamicReportConfigDetail.getLabelName()) + "#"
									+ dynamicReportConfigDetail.getWidth() + "%";
						}
						
					});
		}

	
	}
	public Map<String, String> getSeasonsList() {

		Map<String, String> seasonMap = new LinkedHashMap<String, String>();
		List<Object[]> seasonList = farmerService.listSeasonCodeAndName();

		seasonMap = seasonList.stream()
				.collect(Collectors.toMap(obj -> (String.valueOf(obj[0])), obj -> String.valueOf(obj[1])));
		return seasonMap;
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
	@SuppressWarnings("unused")
	private Object getMethodValue(String methodName, Object param) {
		Object field = null;
		try {
			@SuppressWarnings("rawtypes")
			Class cls = this.getClass();

			if (param != null) {
				if (param instanceof Object[]) {
					Method setNameMethod = this.getClass().getMethod(methodName, Object[].class);
					field = setNameMethod.invoke(this, param);
				} else {
					Method setNameMethod = this.getClass().getMethod(methodName, String.class);
					field = setNameMethod.invoke(this, param);
				}
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
	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
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
	
	
	public ILocationService getLocationService() {
		return locationService;
	}

	public void setLocationService(ILocationService locationService) {
		this.locationService = locationService;
	}
	public String getFilterList() {
		return filterList;
	}

	public void setFilterList(String filterList) {
		this.filterList = filterList;
	}
	
	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}

	public Double getProcessingQty(String id) {
		Double txnstock =locationService.findTxnStockByGinningId(Long.valueOf(id));
         return txnstock;
	}
	
	public String getIcsName(String code){
		FarmCatalogue cat = catalogueService.findCatalogueByCode(code);
		String catalogueName=cat.getName();
		return catalogueName;
		
	}
	
	public String getSeasonName(String code){
		HarvestSeason hs=farmerService.findSeasonNameByCode(code);
		String catalogueName=hs.getName();
		return catalogueName;
		
	}
	
	String seedQty;
	public String getSeedQty(String id){
		double lint = 0.0;
		
		List<Object[]> lintlist =locationService.findStockByGinningId(Long.valueOf(id));
		
		for (Object[] objects : lintlist) {
			 lint=(Double.valueOf(String.valueOf(objects[2]))/Double.valueOf(String.valueOf(objects[0]))*100);
			 lintQty=String.valueOf(objects[2]);
		}
		
		String seeds=lintQty +"("+ String.valueOf(lint)+"%)";
		return seeds;
	}		
	String lintQty;
	public String getLintQty(String id){
		double lint = 0.0;
		
		List<Object[]> lintlist =locationService.findStockByGinningId(Long.valueOf(id));
		
		for (Object[] objects : lintlist) {
			 lint=(Double.valueOf(String.valueOf(objects[1]))/Double.valueOf(String.valueOf(objects[0]))*100);
			 lintQty=String.valueOf(objects[1]);
		}
		
		String lints=lintQty +"("+ String.valueOf(lint)+"%)";
		return lints;
	}	
	
	String scrapQty;
	public String getScrapQty(String id){
		double lint = 0.0;
		
		List<Object[]> lintlist =locationService.findStockByGinningId(Long.valueOf(id));
		
		for (Object[] objects : lintlist) {
			 lint=(Double.valueOf(String.valueOf(objects[3]))/Double.valueOf(String.valueOf(objects[0]))*100);
			 lintQty=String.valueOf(objects[3]);
		}
		
		String scraps=lintQty +"("+ String.valueOf(lint)+"%)";
		return scraps;
	}		
public Map<String, String> getCooperativeManagetList() {
	Map<String, String> farmerMap = new LinkedHashMap<>();
		List<Object[]> farmerList = locationService.listOfCooperativesByGinningProcess();
		if (!ObjectUtil.isListEmpty(farmerList)) {
			farmerList.stream().forEach(obj->{
				if(!farmerMap.containsKey(obj[1])){ 
					farmerMap.put(String.valueOf(obj[1]),String.valueOf(obj[1]));
				}
			});
		}
		return farmerMap;
}
public Map<String, String> getHeapNameList() {
	Map<String, String> heapMap = new LinkedHashMap<>();
		List<Object[]> heapList = locationService.listOfHeapNameByGinningProcess();
		if (!ObjectUtil.isListEmpty(heapList)) {
				heapList.stream().forEach(heap->heapMap.put(String.valueOf(heap[0]), String.valueOf(heap[1])));	
		}
		return heapMap;
}

public String getDateFormat(String inputDate) {
	return DateUtil.convertDateFormat(inputDate, DateUtil.DATE, DateUtil.DATE_FORMAT_2);
}
public String getHeapName() {
	return heapName;
}
public void setHeapName(String heapName) {
	this.heapName = heapName;
}
public List<BaleGeneration> getBaleGeneration() {
	return baleGeneration;
}
public void setBaleGeneration(List<BaleGeneration> baleGeneration) {
	this.baleGeneration = baleGeneration;
}
public String getGinningName() {
	return ginningName;
}
public void setGinningName(String ginningName) {
	this.ginningName = ginningName;
}
public String getGinningdate() {
	return ginningdate;
}
public void setGinningdate(String ginningdate) {
	this.ginningdate = ginningdate;
}
public String getIcsName() {
	return icsName;
}
public void setIcsName(String icsName) {
	this.icsName = icsName;
}
public String getTotBaleWeight() {
	return totBaleWeight;
}
public void setTotBaleWeight(String totBaleWeight) {
	this.totBaleWeight = totBaleWeight;
}
public String getFarmerName() {
	return farmerName;
}
public void setFarmerName(String farmerName) {
	this.farmerName = farmerName;
}
public List<Object[]> getFarmerInfo() {
	return farmerInfo;
}
public void setFarmerInfo(List<Object[]> farmerInfo) {
	this.farmerInfo = farmerInfo;
}


	
}
