package com.sourcetrace.esesw.view.report.agro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
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
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class FarmerTraceabilityReportAction extends BaseReportAction {
	private static final long serialVersionUID = 1L;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private IFarmerService farmerService;
	Map<Long, String> stateList=new HashMap<>();
	private String filterList;
	private String filterDataStr;
	private String gridColNames; 
	Map<String, Map> filterMap = new HashMap<>();
	Map otherMap = new HashMap<>();
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private String xlsFileName;
	private InputStream fileInputStream;
	int colCount, rowCount, titleRow1, titleRow2;
	HSSFRow row, titleRow, filterRowTitle, filterRow1, filterRow2, filterRow3;
	Cell cell;
	Integer cellIndex;
	@Autowired
	private IProductDistributionService productDistributionService;
	
	public String list() {
		formGridCols();
		return LIST;
	}
	private void formGridCols() {
		gridColNames="";
		if (StringUtil.isEmpty(getBranchId())) {
			gridColNames = getLocaleProperty("branchId") + "#";
		}
		for(String c: getLocaleProperty("traceHead").split(",")){
			gridColNames += c + "#";
		}
	}
	public String data() throws Exception {
		Map<String, String> filtersList=new HashMap<>();
		if (!StringUtil.isEmpty(filterList)) {
			try {
				Type listType1 = new TypeToken<Map<String, String>>() {
				}.getType();
				filtersList = new Gson().fromJson(filterList, listType1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			filterMap.put(DynamicReportConfig.FILTER_FIELDS, filtersList);
			Map data = readTraceabilityData(filterMap,getBranchId());
			return sendTraceabilityJSONResponse(data);
		}
		else
			return null;
		
	}
public InputStream getExportDataStream(String exportType) throws IOException {
	Map<String, String> filtersList=new HashMap<>();
	if (!StringUtil.isEmpty(filterList)) {
		try {
			Type listType1 = new TypeToken<Map<String, String>>() {
			}.getType();
			filtersList = new Gson().fromJson(filterList, listType1);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	filterMap.put(DynamicReportConfig.FILTER_FIELDS, filtersList);
	Map data = readTraceabilityData(filterMap,getBranchId());
	HSSFWorkbook workbook = new HSSFWorkbook();
	HSSFSheet sheet = workbook.createSheet(getLocaleProperty("exportfarmerTraceabilityReportTitle"));
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
	cell.setCellValue(new HSSFRichTextString(getLocaleProperty("exportfarmerTraceabilityReportTitle")));
	cell.setCellStyle(style1);
	font1.setBoldweight((short) 22);
	font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	style1.setFont(font1);
	List<Object[]> mainGridRows = (List<Object[]>) data.get(ROWS);
	if (ObjectUtil.isListEmpty(mainGridRows))
		return null;

	HSSFCellStyle header_style = workbook.createCellStyle();
	header_style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
	header_style.setWrapText(true);
	HSSFCellStyle intFormat = workbook.createCellStyle();
	intFormat.setDataFormat((short) 0);
	rowCount++;
	rowCount++;
	filterRowTitle = sheet.createRow(rowCount++);
	cell = filterRowTitle.createCell(1);
	cell.setCellValue(new HSSFRichTextString(getLocaleProperty("filter")));
	filterFont.setBoldweight((short) 12);
	filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	filterStyle.setFont(filterFont);
	cell.setCellStyle(filterStyle);
	filterRow1 = sheet.createRow(rowCount++);

	if(filterDataStr!=null && !StringUtil.isEmpty(filterDataStr)){
		Map<String, String> filterDataList=new HashMap<>();
		try {
			Type listType1 = new TypeToken<Map<String, String>>() {
			}.getType();
			filterDataList = new Gson().fromJson(filterDataStr, listType1);
					
		} catch (Exception e) {
			e.printStackTrace();
		}
		 for (Map.Entry<String,String> entry : filterDataList.entrySet()){
			 cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty(entry.getKey())));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
				cell = filterRow1.createCell(2);
				// cell.setCellValue(new
				// HSSFRichTextString(filter1.getFarm().getFarmer().getFirstName()));
				cell.setCellValue(new HSSFRichTextString(entry.getValue().trim()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
				row = sheet.createRow(rowCount++);
		 }
	
	
	}
	
	
	
	
	rowCount++;;
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
	for(String hed:getLocaleProperty("traceHead").split(",")){
		cell = row.createCell(colCount++);
		cell.setCellValue(hed);
		cell.setCellStyle(header_style);
		filterFont.setBoldweight((short) 5);
		filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		filterStyle.setFont(filterFont);
		cell.setCellStyle(filterStyle);
	}
	for(Object[] obj:mainGridRows){
		colCount = 0;
		//colCount++;
		row = sheet.createRow(rowCount++);
		if(getBranchId()!=null && !StringUtil.isEmpty(getBranchId())){}
		
		else{
			cell = row.createCell(colCount++);
			cell.setCellValue(new HSSFRichTextString(obj[53]!=null && !ObjectUtil.isEmpty(obj[53])?obj[53].toString():"Chetna Organization"));
		}
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[0]!=null && !ObjectUtil.isEmpty(obj[0])?obj[0].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[1]!=null && !ObjectUtil.isEmpty(obj[1])?obj[1].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[2]!=null && !ObjectUtil.isEmpty(obj[2])?obj[2].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[3]!=null && !ObjectUtil.isEmpty(obj[3])?obj[3].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[4]!=null && !ObjectUtil.isEmpty(obj[4])?obj[4].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[5]!=null && !ObjectUtil.isEmpty(obj[5])?obj[5].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[6]!=null && !ObjectUtil.isEmpty(obj[6])?obj[6].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[7]!=null && !ObjectUtil.isEmpty(obj[7])?obj[7].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[8]!=null && !ObjectUtil.isEmpty(obj[8])?obj[8].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[9]!=null && !ObjectUtil.isEmpty(obj[9])?obj[9].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[10]!=null && !ObjectUtil.isEmpty(obj[10])?obj[10].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[11]!=null && !ObjectUtil.isEmpty(obj[11])?obj[11].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[12]!=null && !ObjectUtil.isEmpty(obj[12])?obj[12].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[13]!=null && !ObjectUtil.isEmpty(obj[13])?obj[13].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[14]!=null && !ObjectUtil.isEmpty(obj[14])?obj[14].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[15]!=null && !ObjectUtil.isEmpty(obj[15])?obj[15].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[16]!=null && !ObjectUtil.isEmpty(obj[16])?obj[16].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[17]!=null && !ObjectUtil.isEmpty(obj[17])?obj[17].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[18]!=null && !ObjectUtil.isEmpty(obj[18])?obj[18].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[19]!=null && !ObjectUtil.isEmpty(obj[19])?obj[19].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[20]!=null && !ObjectUtil.isEmpty(obj[20])?obj[20].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[21]!=null && !ObjectUtil.isEmpty(obj[21])?obj[21].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[22]!=null && !ObjectUtil.isEmpty(obj[22])?obj[22].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[23]!=null && !ObjectUtil.isEmpty(obj[23])?obj[23].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[24]!=null && !ObjectUtil.isEmpty(obj[24])?obj[24].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[25]!=null && !ObjectUtil.isEmpty(obj[25])?obj[25].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[26]!=null && !ObjectUtil.isEmpty(obj[26])?obj[26].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[27]!=null && !ObjectUtil.isEmpty(obj[27])?obj[27].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[28]!=null && !ObjectUtil.isEmpty(obj[28])?obj[28].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[29]!=null && !ObjectUtil.isEmpty(obj[29])?obj[29].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[30]!=null && !ObjectUtil.isEmpty(obj[30])?obj[30].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[31]!=null && !ObjectUtil.isEmpty(obj[31])?obj[31].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[32]!=null && !ObjectUtil.isEmpty(obj[32])?obj[32].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[33]!=null && !ObjectUtil.isEmpty(obj[33])?obj[33].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[34]!=null && !ObjectUtil.isEmpty(obj[34])?obj[34].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[35]!=null && !ObjectUtil.isEmpty(obj[35])?obj[35].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[36]!=null && !ObjectUtil.isEmpty(obj[36])?obj[36].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[37]!=null && !ObjectUtil.isEmpty(obj[37])?obj[37].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[38]!=null && !ObjectUtil.isEmpty(obj[38])?obj[38].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[39]!=null && !ObjectUtil.isEmpty(obj[39])?obj[39].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[40]!=null && !ObjectUtil.isEmpty(obj[40])?obj[40].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[41]!=null && !ObjectUtil.isEmpty(obj[41])?obj[41].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[42]!=null && !ObjectUtil.isEmpty(obj[42])?obj[42].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[43]!=null && !ObjectUtil.isEmpty(obj[43])?obj[43].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[44]!=null && !ObjectUtil.isEmpty(obj[44])?obj[44].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[45]!=null && !ObjectUtil.isEmpty(obj[45])?obj[45].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[46]!=null && !ObjectUtil.isEmpty(obj[46])?obj[46].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[47]!=null && !ObjectUtil.isEmpty(obj[47])?obj[47].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[48]!=null && !ObjectUtil.isEmpty(obj[48])?obj[48].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[49]!=null && !ObjectUtil.isEmpty(obj[49])?obj[49].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[50]!=null && !ObjectUtil.isEmpty(obj[50])?obj[50].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[51]!=null && !ObjectUtil.isEmpty(obj[51])?obj[51].toString():""));
		cell = row.createCell(colCount++);
		cell.setCellValue(new HSSFRichTextString(obj[52]!=null && !ObjectUtil.isEmpty(obj[52])?obj[52].toString():""));
		for (int i = 0; i <= colCount; i++) {
			sheet.autoSizeColumn(i);
		}
	}
	
	int pictureIdx = getPicIndex(workbook);
	HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
	anchor.setAnchorType(1);
	HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
	// picture.resize();
	String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
			: request.getSession().getId();
	String makeDir = FileUtil.storeXls(id);
	String fileName = getLocaleProperty("farmerTraceabilityList") + fileNameDateFormat.format(new Date()) + ".xls";
	FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
	workbook.write(fileOut);
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
	protected String sendTraceabilityJSONResponse(Map data) throws Exception {

		JSONObject gridData = new JSONObject();
		gridData.put(PAGE, getPage());
		totalRecords = (Integer) data.get(RECORD_COUNT);
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
	
	public JSONObject toJSON(Object obj) {
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		Object[] data = (Object[]) obj;
		if(getBranchId()!=null && !StringUtil.isEmpty(getBranchId())){}
		
		else
			rows.add(data[53]!=null && !ObjectUtil.isEmpty(data[53])?data[53].toString():"Chetna Organization");
		rows.add(data[0]!=null && !ObjectUtil.isEmpty(data[0])?data[0]:"0");
		rows.add(data[1]!=null && !ObjectUtil.isEmpty(data[1])?data[1]:"0");
		rows.add(data[2]!=null && !ObjectUtil.isEmpty(data[2])?data[2]:"0");
		rows.add(data[3]!=null && !ObjectUtil.isEmpty(data[3])?data[3]:"0");
		rows.add(data[4]!=null && !ObjectUtil.isEmpty(data[4])?data[4]:"0");
		rows.add(data[5]!=null && !ObjectUtil.isEmpty(data[5])?data[5]:"0");
		rows.add(data[6]!=null && !ObjectUtil.isEmpty(data[6])?data[6]:"0");
		rows.add(data[7]!=null && !ObjectUtil.isEmpty(data[7])?data[7]:"0");
		rows.add(data[8]!=null && !ObjectUtil.isEmpty(data[8])?data[8]:"0");
		rows.add(data[9]!=null && !ObjectUtil.isEmpty(data[9])?data[9]:"0");
		rows.add(data[10]!=null && !ObjectUtil.isEmpty(data[10])?data[10]:"0");
		rows.add(data[11]!=null && !ObjectUtil.isEmpty(data[11])?data[11]:"0");
		rows.add(data[12]!=null && !ObjectUtil.isEmpty(data[12])?data[12]:"0");
		rows.add(data[13]!=null && !ObjectUtil.isEmpty(data[13])?data[13]:"0");
		rows.add(data[14]!=null && !ObjectUtil.isEmpty(data[14])?data[14]:"0");
		rows.add(data[15]!=null && !ObjectUtil.isEmpty(data[15])?data[15]:"0");
		rows.add(data[16]!=null && !ObjectUtil.isEmpty(data[16])?data[16]:"0");
		rows.add(data[17]!=null && !ObjectUtil.isEmpty(data[17])?data[17]:"0");
		rows.add(data[18]!=null && !ObjectUtil.isEmpty(data[18])?data[18]:"0");
		rows.add(data[19]!=null && !ObjectUtil.isEmpty(data[19])?data[19]:"0");
		rows.add(data[20]!=null && !ObjectUtil.isEmpty(data[20])?data[20]:"0");
		rows.add(data[21]!=null && !ObjectUtil.isEmpty(data[21])?data[21]:"0");
		rows.add(data[22]!=null && !ObjectUtil.isEmpty(data[22])?data[22]:"0");
		rows.add(data[23]!=null && !ObjectUtil.isEmpty(data[23])?data[23]:"0");
		rows.add(data[24]!=null && !ObjectUtil.isEmpty(data[24])?data[24]:"0");
		rows.add(data[25]!=null && !ObjectUtil.isEmpty(data[25])?data[25]:"0");
		rows.add(data[26]!=null && !ObjectUtil.isEmpty(data[26])?data[26]:"0");
		rows.add(data[27]!=null && !ObjectUtil.isEmpty(data[27])?data[27]:"0");
		rows.add(data[28]!=null && !ObjectUtil.isEmpty(data[28])?data[28]:"0");
		rows.add(data[29]!=null && !ObjectUtil.isEmpty(data[29])?data[29]:"0");
		rows.add(data[30]!=null && !ObjectUtil.isEmpty(data[30])?data[30]:"0");
		rows.add(data[31]!=null && !ObjectUtil.isEmpty(data[31])?data[31]:"0");
		rows.add(data[32]!=null && !ObjectUtil.isEmpty(data[32])?data[32]:"0");
		rows.add(data[33]!=null && !ObjectUtil.isEmpty(data[33])?data[33]:"0");
		rows.add(data[34]!=null && !ObjectUtil.isEmpty(data[34])?data[34]:"0");
		rows.add(data[35]!=null && !ObjectUtil.isEmpty(data[35])?data[35]:"0");
		rows.add(data[36]!=null && !ObjectUtil.isEmpty(data[36])?data[36]:"0");
		rows.add(data[37]!=null && !ObjectUtil.isEmpty(data[37])?data[37]:"0");
		rows.add(data[38]!=null && !ObjectUtil.isEmpty(data[38])?data[38]:"0");
		rows.add(data[39]!=null && !ObjectUtil.isEmpty(data[39])?data[39]:"0");
		rows.add(data[40]!=null && !ObjectUtil.isEmpty(data[40])?data[40]:"0");
		rows.add(data[41]!=null && !ObjectUtil.isEmpty(data[41])?data[41]:"0");
		rows.add(data[42]!=null && !ObjectUtil.isEmpty(data[42])?data[42]:"0");
		rows.add(data[43]!=null && !ObjectUtil.isEmpty(data[43])?data[43]:"0");
		rows.add(data[44]!=null && !ObjectUtil.isEmpty(data[44])?data[44]:"0");
		rows.add(data[45]!=null && !ObjectUtil.isEmpty(data[45])?data[45]:"0");
		rows.add(data[46]!=null && !ObjectUtil.isEmpty(data[46])?data[46]:"0");
		rows.add(data[47]!=null && !ObjectUtil.isEmpty(data[47])?data[47]:"0");
		rows.add(data[48]!=null && !ObjectUtil.isEmpty(data[48])?data[48]:"0");
		rows.add(data[49]!=null && !ObjectUtil.isEmpty(data[49])?data[49]:"0");
		rows.add(data[50]!=null && !ObjectUtil.isEmpty(data[50])?data[50]:"0");
		rows.add(data[51]!=null && !ObjectUtil.isEmpty(data[51])?data[51]:"0");
		rows.add(data[52]!=null && !ObjectUtil.isEmpty(data[52])?data[52]:"0");
		jsonObject.put("cell", rows);
		return jsonObject;
	}
	public ILocationService getLocationService() {
		return locationService;
	}
	public void setLocationService(ILocationService locationService) {
		this.locationService = locationService;
	}
	public IFarmerService getFarmerService() {
		return farmerService;
	}
	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}
	public void setStateList(Map<Long, String> stateList) {
		this.stateList = stateList;
	}

	public String getFilterList() {
		return filterList;
	}

	public void setFilterList(String filterList) {
		this.filterList = filterList;
	}

	public String getGridColNames() {
		return gridColNames;
	}

	public void setGridColNames(String gridColNames) {
		this.gridColNames = gridColNames;
	}
	public Map<String, Map> getFilterMap() {
		return filterMap;
	}
	public void setFilterMap(Map<String, Map> filterMap) {
		this.filterMap = filterMap;
	}
	public String populateXLS() throws Exception {

		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getText("FarmerTraceabilityReport").replace(" ", "_") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("farmerTraceabilityReportList"), fileMap, ".xls"));
		return "xls";
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
	public String getFilterDataStr() {
		return filterDataStr;
	}
	public void setFilterDataStr(String filterDataStr) {
		this.filterDataStr = filterDataStr;
	}
	public void populateLotNoList(){
		JSONArray lotNoArr=new JSONArray();
		List<String> ltNo=productDistributionService.listOfLotNoFromBaleGeneration();
		if(ltNo!=null && !ObjectUtil.isListEmpty(ltNo)){
			ltNo.stream().distinct().forEach(ln->{
				lotNoArr.add(getJSONObject(ln.toString(),ln.toString()));
			});
		}
		sendAjaxResponse(lotNoArr);
	}
	public void populateStateList(){
		JSONArray stateArr=new JSONArray();
		List<Object[]> stateList=locationService.listStates();
		if(stateList!=null && !ObjectUtil.isListEmpty(stateList)){
			stateList.stream().distinct().forEach(st->{
				stateArr.add(getJSONObject(Long.parseLong(st[2].toString()), st[1].toString()));
			});
		}
		sendAjaxResponse(stateArr);
	}
	public void populateICSList(){
		JSONArray icsArr=new JSONArray();
		List<FarmCatalogue> icsList=farmerService.listCatelogueTypeWithOther(getText("icsNameType"));
		if(icsList!=null && !ObjectUtil.isListEmpty(icsList)){
			icsList.stream().distinct().forEach(ic->{
				icsArr.add(getJSONObject(ic.getCode().toString(), ic.getName().toString()));
			});
		}
		sendAjaxResponse(icsArr);
	}
	public void populateTalukList(){
		JSONArray talukArr=new JSONArray();
		List<Municipality> talukList=locationService.listCityByFarmerCity();
		if(talukList!=null && !ObjectUtil.isListEmpty(talukList)){
			talukList.stream().distinct().forEach(t->{
				talukArr.add(getJSONObject(t.getId(),t.getName()));
			});
		}
		sendAjaxResponse(talukArr);
	}
	public void populateVillageList(){
		JSONArray villageArr=new JSONArray();
		List<Object[]> villageList=locationService.listVillageIdAndName();
		if(villageList!=null && !ObjectUtil.isListEmpty(villageList)){
			villageList.stream().distinct().forEach(v->{
				villageArr.add(getJSONObject(Long.parseLong(v[0].toString()),v[2].toString()));
			});
		}
		sendAjaxResponse(villageArr);
	}
	public void populateSHGList(){
		JSONArray shgArr=new JSONArray();
		List<Warehouse> shgList=farmerService.listSamithiName();
		if(shgList!=null && !ObjectUtil.isListEmpty(shgList)){
			shgList.stream().distinct().forEach(sh->{
				shgArr.add(getJSONObject(sh.getId(),sh.getName()));
			});
		}
		sendAjaxResponse(shgArr);
	}
	public IProductDistributionService getProductDistributionService() {
		return productDistributionService;
	}
	public void setProductDistributionService(IProductDistributionService productDistributionService) {
		this.productDistributionService = productDistributionService;
	}
	
}
