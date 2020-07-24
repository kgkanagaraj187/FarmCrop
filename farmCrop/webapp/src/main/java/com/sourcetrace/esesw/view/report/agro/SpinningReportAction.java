package com.sourcetrace.esesw.view.report.agro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import com.ese.entity.traceability.SpinningTransfer;
import com.ese.entity.util.ESESystem;
import com.ese.entity.util.FilterFieldData;
import com.ese.util.Base64Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.tool.xml.svg.graphic.Line;
import com.opensymphony.xwork2.ActionContext;
import com.sourcetrace.eses.BreadCrumb;
import com.sourcetrace.eses.dao.ILocationDAO;
import com.sourcetrace.eses.dao.IReportDAO;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfig;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfigFilter;
import com.sourcetrace.eses.order.entity.txn.OfflineSpinningTransfer;
import com.sourcetrace.eses.order.entity.txn.PMTImageDetails;
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
import com.sourcetrace.esesw.entity.profile.AnimalHusbandary;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class SpinningReportAction extends BaseReportAction {

	private static DynamicReportConfig dynamicReportConfig;
	private List<DynamicReportConfigFilter> reportConfigFilters = new LinkedList<>();
	private String mainGridCols;
	private String filterSize;
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private ILocationDAO locationDAO;
	private String gridIdentity;
	private Object fValue;
	private Object mValue;
	private String filterList;
	@Autowired
	private IPreferencesService preferncesService;
	private InputStream fileInputStream;
	private String xlsFileName;
	HSSFRow row, titleRow, filterRowTitle, filterRow1, filterRow2, filterRow3;
	int colCount, rowCount, titleRow1, titleRow2;;
	Cell cell;
	Integer cellIndex;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd-MMM-yyyy");
	private String heapName;
	private String ginningName;
	private String ginningdate;
	List<BaleGeneration> baleGeneration;
	private GinningProcess ginningData;
	private String selectedBales;
	private String gmoFile;
	private List<PMTImageDetails> imgSet = new ArrayList<>();
	private SpinningTransfer spg;
	private String txnDate;
	private String editType;
	private String imgId;
	private String selectedGinning;
	private String selectedHeaps;
	private String processDate;
	private String invoiceNo;
	private String truckNo;
	private String selectedSpinning;
	private String selectedtype;
	private String selectedGining;
	private String price;
	
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
	
/*	public String create() {
		return INPUT;
	}*/

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

	public String data() throws Exception {
		SpinningTransfer spinningTransfer = new SpinningTransfer();
		if (!StringUtil.isEmpty(filterList)) {
			try {
				Type listType1 = new TypeToken<List<FilterFieldData>>() {
				}.getType();
				List<FilterFieldData> filtersList = new Gson().fromJson(filterList, listType1);
				spinningTransfer.setFilterData(filtersList.stream()
						.collect(Collectors.toMap(FilterFieldData::getName, obj -> obj.getValue())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else{
			Map<String,String> filMap=new HashMap<>();
			filMap.put("season", getCurrentSeasonsCode());
			spinningTransfer.setFilterData(filMap);
		}
		if(!spinningTransfer.getFilterData().containsKey("season")){
			spinningTransfer.getFilterData().put("season", getCurrentSeasonsCode());
		}
		super.filter = spinningTransfer;
		Map data = null;
		if (dynamicReportConfig.getFetchType() == 2L) {
			data = readProjectionData(dynamicReportConfig.getDynmaicReportConfigDetails());
		} else {
			data = readData();
		}

		setGridIdentity(IReportDAO.MAIN_GRID);
		return sendJSONResponse(data);
	}

	public String populateXLS() throws Exception {

		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getText("SpinningReport").replace(" ", "_") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("spinningReportList"), fileMap, ".xls"));
		return "xls";
	}

	public InputStream getExportDataStream(String exportType) throws IOException {
		List<FilterFieldData> filtersList =new ArrayList<>();
		SpinningTransfer spinningTransfer = new SpinningTransfer();
		if (!StringUtil.isEmpty(filterList)) {
			try {
				Type listType1 = new TypeToken<List<FilterFieldData>>() {
				}.getType();
			filtersList = new Gson().fromJson(filterList, listType1);
				spinningTransfer.setFilterData(filtersList.stream()
						.collect(Collectors.toMap(FilterFieldData::getName, obj -> obj.getValue())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else{
			Map<String,String> filMap=new HashMap<>();
			filMap.put("season", getCurrentSeasonsCode());
			spinningTransfer.setFilterData(filMap);
		}
		super.filter = spinningTransfer;
		Map data = null;
		if (dynamicReportConfig.getFetchType() == 2L) {
			data = readProjectionData(dynamicReportConfig.getDynmaicReportConfigDetails());
		} else {
			data = readData();
		}

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(getLocaleProperty("exportSpinningReportTitle"));
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
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("exportSpinningReportTitle")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);

		List<Object[]> mainGridRows = (List<Object[]>) data.get(ROWS);

		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;

		HSSFCellStyle header_style = workbook.createCellStyle();
		header_style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		/* header_style.setFillPattern(FillPatternType.SOLID_FOREGROUND); */
		header_style.setWrapText(true);

		HSSFCellStyle sub_header_style = workbook.createCellStyle();
		/*
		 * sub_header_style.setBorderTop(BorderStyle.MEDIUM);
		 * sub_header_style.setBorderBottom(BorderStyle.MEDIUM);
		 * sub_header_style.setBorderLeft(BorderStyle.MEDIUM);
		 * sub_header_style.setBorderRight(BorderStyle.MEDIUM);
		 */
		sub_header_style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		// sub_header_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		sub_header_style.setWrapText(true);

		HSSFCellStyle sub_Cell_style = workbook.createCellStyle();
		/*
		 * sub_Cell_style.setBorderTop(BorderStyle.MEDIUM);
		 * sub_Cell_style.setBorderBottom(BorderStyle.MEDIUM);
		 * sub_Cell_style.setBorderLeft(BorderStyle.MEDIUM);
		 * sub_Cell_style.setBorderRight(BorderStyle.MEDIUM);
		 * sub_Cell_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		 */
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

		dynamicReportConfig.getDynmaicReportConfigDetails().stream().filter(config -> config.getIsExportAvailabiltiy())
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
					.filter(config -> config.getIsExportAvailabiltiy())
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
		String fileName = getLocaleProperty("spinningReportList") + fileNameDateFormat.format(new Date()) + ".xls";
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

	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		AtomicInteger runCount = new AtomicInteger(1);
		String id = "";
		if (obj instanceof Object[]) {
			Object[] arr = (Object[]) obj;
			id = String.valueOf(arr[0]) + "-" + String.valueOf(arr[6]);
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
		dynamicReportConfig = clientService.findReportByName(DynamicReportProperties.SPINNING_REPORT);
		if (!ObjectUtil.isEmpty(dynamicReportConfig)) {
			mainGridCols = "";
			if (StringUtil.isEmpty(getBranchId())) {
				mainGridCols = getLocaleProperty("branchId") + "#" + "50%";
			}
			dynamicReportConfig.getDynmaicReportConfigDetails().stream()
					.filter(config -> config.getIsGridAvailabiltiy())
					.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
					.forEach(dynamicReportConfigDetail -> {
						if (dynamicReportConfigDetail.getLabelName().contains("@")) {
							String label = dynamicReportConfigDetail.getLabelName();
							mainGridCols += getLocaleProperty(label) + " (" + getCurrencyType() + ")" + "#"
									+ dynamicReportConfigDetail.getWidth() + "%";
						} else {
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

	public String getGeneralDateFormat() {
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			return preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT);
		}
		return null;
	}

	public Map<String, String> getGinningList() {
		Map<String, String> ginningMap = new LinkedHashMap<>();
		List<Object[]> ginningList = productDistributionService.listOfGinningFromSpinningTransfer();
		if (!ObjectUtil.isListEmpty(ginningList)) {
			ginningList.stream().forEach(gin -> ginningMap.put(String.valueOf(gin[0]), String.valueOf(gin[1])));
		}
		return ginningMap;
	}

	public Map<String, String> getSpinningList() {
		Map<String, String> spinMap = new LinkedHashMap<>();
		List<Object[]> spinList = productDistributionService.listOfSpinningFromSpinningTransfer();
		if (!ObjectUtil.isListEmpty(spinList)) {
			spinList.stream().forEach(spin -> spinMap.put(String.valueOf(spin[0]), String.valueOf(spin[1])));
		}
		return spinMap;
	}

	public Map<String, String> getLotNoList() {
		Map<String, String> lotMap = new LinkedHashMap<>();
		List<Object[]> lotList = productDistributionService.listOfLotNoPrNoAndTypeFromSpinningTransfer();
		if (!ObjectUtil.isListEmpty(lotList)) {
			lotList.stream().forEach(lot -> lotMap.put(String.valueOf(lot[0]), String.valueOf(lot[0])));
		}
		return lotMap;
	}

	public Map<String, String> getPrNoList() {
		Map<String, String> prMap = new LinkedHashMap<>();
		List<Object[]> prList = productDistributionService.listOfLotNoPrNoAndTypeFromSpinningTransfer();
		if (!ObjectUtil.isListEmpty(prList)) {
			prList.stream().forEach(pr -> prMap.put(String.valueOf(pr[1]), String.valueOf(pr[1])));
		}
		return prMap;
	}

	public Map<String, String> getTypeList() {
		Map<String, String> typeMap = new LinkedHashMap<>();
		List<Object[]> typeList = productDistributionService.listOfLotNoPrNoAndTypeFromSpinningTransfer();
		if (!ObjectUtil.isListEmpty(typeList)) {
			typeList.stream().forEach(type -> typeMap.put(String.valueOf(type[2]), String.valueOf(type[3])));
		}
		return typeMap;
	}
	
	public String getSeasonName(String code){
		HarvestSeason hs=farmerService.findSeasonNameByCode(code);
		String catalogueName=hs.getName();
		return catalogueName;
		
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

	public String getName(String id) {
		Warehouse ware = locationDAO.findWarehouseById(Long.parseLong(id), getCurrentTenantId());
		return ware.getName();
	}

	@Override
	public void prepare() throws Exception {

		editType = request.getParameter("edit");
		if (!StringUtil.isEmpty(editType)) {
			String actionClassName = ActionContext.getContext().getActionInvocation().getAction().getClass()
					.getSimpleName();
			String content = getLocaleProperty(actionClassName + "." + BreadCrumb.BREADCRUMB + editType);
			if (StringUtil.isEmpty(content)
					|| (content.equalsIgnoreCase(actionClassName + "." + BreadCrumb.BREADCRUMB + editType))) {
				content = super.getText(BreadCrumb.BREADCRUMB + editType, "");
			}
			request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(content));

		} else {
			String actionClassName = ActionContext.getContext().getActionInvocation().getAction().getClass()
					.getSimpleName();
			String content = getLocaleProperty(actionClassName + "." + BreadCrumb.BREADCRUMB);
			if (StringUtil.isEmpty(content)
					|| (content.equalsIgnoreCase(actionClassName + "." + BreadCrumb.BREADCRUMB))) {
				content = super.getText(BreadCrumb.BREADCRUMB, "");
			}
			request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(content));
		}

	}

	public String getPhoto(String id) {
		List<Object> ware = productDistributionService.listPMTImageDetailById(Long.valueOf(id),Arrays.asList(2));
		String but = "<button class='no-imgIcn'></button>";
		if (ware != null && !ware.isEmpty()) {
			but = "<button class=\"fa fa-picture-o\"\"aria-hidden=\"true\"\" onclick=\"popupWindow('"
					+ ware.stream().map(e->e.toString()).collect(Collectors.joining(",")) + "')\"></button>";
		}
		return but;
	}

	public String populateImage() {

		try {
			setImgId(imgId);
			PMTImageDetails pmtImg = null;
			if (!StringUtil.isEmpty(imgId))
				pmtImg = productDistributionService.findPMTImageDetailById(Long.valueOf(imgId));
			byte[] image = null;
			if (!ObjectUtil.isEmpty(pmtImg) && pmtImg.getPhoto() != null) {
				image = pmtImg.getPhoto();
			}
			response.setContentType("multipart/form-data");
			OutputStream out = response.getOutputStream();
			out.write(image);
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public ILocationDAO getLocationDAO() {
		return locationDAO;
	}

	public void setLocationDAO(ILocationDAO locationDAO) {
		this.locationDAO = locationDAO;
	}

	public String getIcsName(String code) {
		FarmCatalogue cat = catalogueService.findCatalogueByCode(code);
		String catalogueName = cat.getName();
		return catalogueName;

	}
	public String getHeapName(String lotNo){
		heapName="";
		if(lotNo.contains(",")){
			for(String lot: lotNo.split(",")){
					List<BaleGeneration> heapList=productDistributionService.findBaleGenerationByLotNo(lot.trim(),getCurrentTenantId());
				heapList.stream().forEach(a-> {
					if(!heapName.contains(a.getHeapName()))
						heapName+=a.getHeapName()+", ";	
				});
				
			}
			
			heapName=StringUtil.removeLastComma(heapName.trim());
		}
		else{
			//heapName=productDistributionService.findBaleGenerationByLotNo(lotNo.trim()).iterator().next().getHeapName();
			
			List<BaleGeneration> heapList=productDistributionService.findBaleGenerationByLotNo(lotNo.trim(),getCurrentTenantId());
			heapList.stream().forEach(a-> {
				if(!heapName.contains(a.getHeapName()))
					heapName+=a.getHeapName()+", ";	
			});
			heapName=StringUtil.removeLastComma(heapName.trim());
		}
		return heapName;
	}
	

	public String detail() throws Exception {
		if (!StringUtil.isEmpty(id)) {
			spg = productDistributionService.findSpinningTransferById(Long.parseLong(id.split("-")[0].trim()));
			txnDate = DateUtil.convertDateToString(spg.getTransDate(), getGeneralDateFormat());
			baleGeneration = new ArrayList<>(spg.getBaleGenerations());
			if (spg != null && spg.getPmtGmoDetails() != null && !spg.getPmtGmoDetails().isEmpty()) {
				spg.getPmtGmoDetails().stream().forEach(u -> {
					if (u.getPhoto() != null) {
						u.setImageByteString(Base64Util.encoder(u.getPhoto()));
					}
				});
				imgSet.addAll(spg.getPmtGmoDetails());
			}
			if(spg !=null && !StringUtil.isEmpty(spg) && spg.getLotNo()!=null && !StringUtil.isEmpty(spg.getLotNo())){
				heapName="";
				if(spg.getLotNo().contains(",")){
					for(String lot: spg.getLotNo().split(",")){
							List<BaleGeneration> heapList=productDistributionService.findBaleGenerationByLotNo(lot.trim(),getCurrentTenantId());
						heapList.stream().forEach(a-> {
							if(!heapName.contains(a.getHeapName()))
							heapName+=a.getHeapName()+", ";	
						});
						
					}
					heapName=StringUtil.removeLastComma(heapName.trim());
				}
				else{
					List<BaleGeneration> heapList=productDistributionService.findBaleGenerationByLotNo(spg.getLotNo().trim(),getCurrentTenantId());
					heapList.stream().forEach(a-> {
						if(!heapName.contains(a.getHeapName()))
						heapName+=a.getHeapName()+", ";	
					});
					heapName=StringUtil.removeLastComma(heapName.trim());
				}
			}
		}
		return "detail";
	}

	public String detailUpdate() throws Exception {
		
		int status = 0;
		if (!StringUtil.isEmpty(id))
			baleGeneration = productDistributionService
					.findBaleGenerationByGinningId(Long.parseLong(id.split("-")[1].trim()), status);

		spg = productDistributionService.findSpinningTransferById(Long.parseLong(id.split("-")[0].trim()));
		txnDate = DateUtil.convertDateToString(spg.getTransDate(), getGeneralDateFormat());
		if (spg != null && spg.getBaleGenerations() != null && !spg.getBaleGenerations().isEmpty()) {
			spg.getBaleGenerations().stream().filter(bg -> bg.getStatus() == 1).forEach(b -> {
				baleGeneration.add(b);
			});
		}
		if (spg != null && spg.getPmtGmoDetails() != null && !spg.getPmtGmoDetails().isEmpty()) {
			spg.getPmtGmoDetails().stream().forEach(u -> {
				if (u.getPhoto() != null) {
					u.setImageByteString(Base64Util.encoder(u.getPhoto()));
				}
				imgSet.removeAll(imgSet.stream().filter(line -> (u.getFileName()!=null && line.getFileName()!=null && u.getFileName().trim().equals(line.getFileName().trim()))).collect(Collectors.toList()));
				
			});
		
			imgSet.addAll(spg.getPmtGmoDetails());
		} else {
			if(imgSet.isEmpty()){
			PMTImageDetails pmt = new PMTImageDetails();
			imgSet.add(pmt);
			}
		}

		return "detailUpdate";
	}

	public GinningProcess getGinningData() {
		return ginningData;
	}

	public void setGinningData(GinningProcess ginningData) {
		this.ginningData = ginningData;
	}
	public String create() throws Exception {
		if (selectedBales != null && !StringUtil.isEmpty(selectedBales)) {
			selectedBales = selectedBales.replaceAll(",$", "");
			SpinningTransfer ost=new SpinningTransfer();
			ost.setTransDate( DateUtil.convertStringToDate(processDate, getGeneralDateFormat()));
			ost.setInvoiceNo(invoiceNo);
			ost.setSeason(getCurrentSeasonsCode());
			ost.setTruckNo(truckNo);
			ost.setType(selectedtype);
			ost.setRate(Double.valueOf(price));
			ost.setBranchId(getBranchId());
			Warehouse spinning= locationService.findSpinningById(Long.parseLong(selectedSpinning));
			Warehouse ginning= locationService.findGinningById(Long.parseLong(selectedGining));
			ost.setSpinning(spinning);
			ost.setGinning(ginning);
			List<Object[]> objFarm = farmerService.updateLotStatus(selectedBales);
			objFarm.forEach(obj -> {
			ost.setLotNo(obj[0].toString());
			ost.setPrNo(obj[1].toString());
			ost.setNetWeight(Double.valueOf(obj[2].toString()));
			ost.setNoBals(Long.valueOf( obj[3].toString()));
			ost.setNetAmt(Double.valueOf(price)*Double.valueOf(obj[2].toString()));
			});
			if (imgSet != null && !imgSet.isEmpty()) {
				imgSet.stream().filter(im-> (im.getImageFile()!=null || im.getImageByteString() != null)).forEach(u -> {
					if (u.getImageFile() != null) {
						u.setPmt(ost.getId());
						u.setType(PMTImageDetails.Type.GMO.ordinal());
						u.setPhoto(FileUtil.getBinaryFileContent(u.getImageFile()));

					} else if (u.getImageByteString() != null) {
						u.setPmt(ost.getId());
						u.setType(PMTImageDetails.Type.GMO.ordinal());
						u.setPhoto(Base64.decode(u.getImageByteString()));

					} else {
						imgSet.remove(u);
					}
				});
			} else {
				imgSet = new ArrayList<>();
			}
			ost.setPmtGmoDetails(new HashSet<>(imgSet));
			farmerService.save(ost);
			productDistributionService.updateBaleStatusById(selectedBales, ost.getId());
		
		}
		return INPUT;
	
		
	}

	public String update() throws Exception {
		if (selectedBales != null && !StringUtil.isEmpty(selectedBales)) {
			selectedBales = selectedBales.replaceAll(",$", "");
			productDistributionService.updateBaleStatusById(selectedBales, Long.parseLong(id.split("-")[0].trim()));
			SpinningTransfer spg = productDistributionService.findSpinningTransferById(Long.parseLong(id.split("-")[0].trim()));
			if (imgSet != null && !imgSet.isEmpty()) {
				imgSet.stream().filter(im-> (im.getImageFile()!=null || im.getImageByteString() != null)).forEach(u -> {
					if (u.getImageFile() != null) {
						u.setPmt(Long.parseLong(id.split("-")[0].trim()));
						u.setType(PMTImageDetails.Type.GMO.ordinal());
						u.setPhoto(FileUtil.getBinaryFileContent(u.getImageFile()));

					} else if (u.getImageByteString() != null) {
						u.setPmt(Long.parseLong(id.split("-")[0].trim()));
						u.setType(PMTImageDetails.Type.GMO.ordinal());
						u.setPhoto(Base64.decode(u.getImageByteString()));

					} else {
						imgSet.remove(u);
					}
				});
			} else {
				imgSet = new ArrayList<>();
			}
			spg.setPmtGmoDetails(new HashSet<>(imgSet));
			farmerService.update(spg);
		} else {

			addActionError(getText("error.emptyBale"));
			if (imgSet != null && !imgSet.isEmpty()) {
				imgSet.stream().filter(im-> (im.getImageFile()!=null || im.getImageByteString() != null)).forEach(u -> {
					if (u.getImageFile() != null) {
						u.setPmt(Long.parseLong(id.split("-")[0].trim()));
						u.setType(PMTImageDetails.Type.GMO.ordinal());
						u.setPhoto(FileUtil.getBinaryFileContent(u.getImageFile()));
						u.setImageByteString(Base64Util.encoder(u.getPhoto()));

					} else if (u.getImageByteString() != null) {
						u.setPmt(Long.parseLong(id.split("-")[0].trim()));
						u.setType(PMTImageDetails.Type.GMO.ordinal());
						u.setPhoto(Base64.decode(u.getImageByteString()));

					} else {
						imgSet.remove(u);
					}
				});
			} else {
				imgSet = new ArrayList<>();
			}
			return detailUpdate();

		}
		//request.setAttribute(HEADING, getText("farmerlist"));
		return REDIRECT;
	}

	public String getSelectedBales() {
		return selectedBales;
	}

	public void setSelectedBales(String selectedBales) {
		this.selectedBales = selectedBales;
	}

	public String getGmoFile() {
		return gmoFile;
	}

	public void setGmoFile(String gmoFile) {
		this.gmoFile = gmoFile;
	}

	public List<PMTImageDetails> getImgSet() {
		return imgSet;
	}

	public void setImgSet(List<PMTImageDetails> imgSet) {
		this.imgSet = imgSet;
	}

	public String populateDownload() {

		try {
			PMTImageDetails pmt = productDistributionService.findPMTImageDetailById(Long.valueOf(id));

			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + pmt.getFileName() + "." + pmt.getFileType());
			response.getOutputStream().write(pmt.getPhoto());
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	public SpinningTransfer getSpg() {
		return spg;
	}

	public void setSpg(SpinningTransfer spg) {
		this.spg = spg;
	}

	public String getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}

	public String getEditType() {
		return editType;
	}

	public void setEditType(String editType) {
		this.editType = editType;
	}

	public String getImgId() {
		return imgId;
	}

	public void setImgId(String imgId) {
		this.imgId = imgId;
	}
	public void populateGinningList(){
		JSONArray ginningArr = new JSONArray();
		List<Object[]> ginnings =locationService.listOfGinningFromBaleGeneration(getCurrentSeasonsCode());
		if (!ObjectUtil.isEmpty(ginnings)) {
			ginnings.forEach(obj -> {
				ginningArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(ginningArr);
	}
	public void populateSpinningList(){
		JSONArray spinningArr = new JSONArray();
		List<Object[]> spinnings =locationService.listOfSpinning();
		if (!ObjectUtil.isEmpty(spinnings)) {
			spinnings.forEach(obj -> {
				spinningArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(spinningArr);
	}
	public void populateTypeList(){
		JSONArray typeArr = new JSONArray();
		List<FarmCatalogue> types = catalogueService.listCataloguesByType(getLocaleProperty("CottonType"));
		if(!ObjectUtil.isListEmpty(types)){
			types.forEach(t-> {
				typeArr.add(getJSONObject(t.getCode(),t.getName()));
			});
		}
		sendAjaxResponse(typeArr);
	}
	public void populateHeapList(){
		JSONArray heapArr = new JSONArray();
		List<Object[]> heaps = locationService.listOfHeapByGinningFromBaleGeneration(selectedGinning,getCurrentSeasonsCode());
		if(!ObjectUtil.isListEmpty(heaps)){
			heaps.forEach(hep-> {
				heapArr.add(getJSONObject(hep[0].toString(),hep[1].toString()));
			});
		}
		System.out.println(heapArr);
		sendAjaxResponse(heapArr);
	}

	public String getSelectedGinning() {
		return selectedGinning;
	}

	public void setSelectedGinning(String selectedGinning) {
		this.selectedGinning = selectedGinning;
	}
	public String populateBaleData() throws Exception{
		selectedHeaps=StringUtil.removeLastComma(selectedHeaps);
		JSONArray array = new JSONArray();
		int status = 0;
		AtomicInteger i=new AtomicInteger(0);
		JSONObject jObj = new JSONObject();
		List<BaleGeneration> invList1 = productDistributionService.findBaleGenerationByGinningIdAndHeap(Long.parseLong(selectedGinning.trim()),selectedHeaps.trim(), status);
		for (BaleGeneration bh : invList1) {
	           // Catalogue cat =  catalogueService.findCatalogueById(Long.valueOf(fm.getInventoryItem()));
	            JSONObject obj = new JSONObject();
	            obj.put("sNo",i.incrementAndGet());
	            obj.put("id", bh.getId());
	            FarmCatalogue cat =  catalogueService.findCatalogueByCode(bh.getHeap());
	            obj.put("heap", cat.getName());
	            obj.put("lot",bh.getLotNo()); 
	            //obj.put("farmerName", ah.getFarmer());
	            obj.put("pr", bh.getPrNo());
	            obj.put("bale", bh.getBaleWeight());
	            obj.put("ststus", bh.getStatus());
	                
	            array.add(obj);
	        }
		 JSONObject mainObj = new JSONObject();
	        mainObj.put("data", array);
	        sendResponse(mainObj);
	        return null;
	}

	public String getSelectedHeaps() {
		return selectedHeaps;
	}

	public void setSelectedHeaps(String selectedHeaps) {
		this.selectedHeaps = selectedHeaps;
	}

	public String getProcessDate() {
		return processDate;
	}

	public void setProcessDate(String processDate) {
		this.processDate = processDate;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getTruckNo() {
		return truckNo;
	}

	public void setTruckNo(String truckNo) {
		this.truckNo = truckNo;
	}

	public String getSelectedSpinning() {
		return selectedSpinning;
	}

	public void setSelectedSpinning(String selectedSpinning) {
		this.selectedSpinning = selectedSpinning;
	}

	public String getSelectedtype() {
		return selectedtype;
	}

	public void setSelectedtype(String selectedtype) {
		this.selectedtype = selectedtype;
	}

	public String getSelectedGining() {
		return selectedGining;
	}

	public void setSelectedGining(String selectedGining) {
		this.selectedGining = selectedGining;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
	

	

	
}
