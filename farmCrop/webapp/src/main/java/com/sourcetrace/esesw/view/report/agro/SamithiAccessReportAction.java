package com.sourcetrace.esesw.view.report.agro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.AgentAccessLog;
import com.sourcetrace.eses.order.entity.txn.AgentAccessLogDetail;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

public class SamithiAccessReportAction extends BaseReportAction implements IExporter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object filter;
	private static final String MAIN_GRID = "MAIN_GRID";
	private static final String SUB_GRID = "SUB_GRID";
	private String selectedFieldStafId;
	private String selectedDevice;

	private String gridIdentity;
	// protected List<String> fieds = new ArrayList<String>();
	private Map<String, String> fields = new LinkedHashMap<String, String>();

	@Autowired
	private IAgentService agentService;
	private ILocationService locationService;
	private IFarmerService farmerService;
	@Autowired
	private IDeviceService deviceService;
	private IPreferencesService preferncesService;
	List<Warehouse> samithi = new ArrayList<Warehouse>();
	private String warehouse;
	List<String> agent = new ArrayList<>();
	List<String> txnList = new ArrayList<>();
	static String txnTypes[] = {"314", "316", "334", "360"};
	private InputStream fileInputStream;
	private String exportLimit;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private String xlsFileName;

	@Override
	public String list() {
		// fields.add(getLocaleProperty("deviceName"));
		// fields.add(getLocaleProperty("agent"));
		fields.put("1", getText("group"));
		return LIST;
	}

	public String detail() throws Exception {
		Warehouse samithiAccessLog = new Warehouse();

		if (!StringUtil.isEmpty(warehouse)) {
			samithiAccessLog.setId(Long.valueOf(warehouse));
		}

		setFilter(samithiAccessLog);
		super.filter = this.filter;
		setGridIdentity(MAIN_GRID);
		Map data = readData();
		return sendJSONResponse(data);
	}

	public String populateSubGridDetails() throws Exception {

		AgroTransaction agrotxnDetail = new AgroTransaction();
		Warehouse samithi = locationService.findSamithiById(Long.valueOf(id));
		List<String> agent = agentService.findAgentByWareHouseId(samithi.getId());
		// List<String> txnList = agentService.listTxnTypeByAgentId(agent);
		// agrotxnDetail.setTxnTypeList(txnList);
		agrotxnDetail.setAgentList(agent);
		setFilter(agrotxnDetail);

		super.filter = this.filter;
		setGridIdentity(SUB_GRID);
		Map data = readData();
		return sendJSONResponse(data);
	}

	@SuppressWarnings("unchecked")
	public List<JSONObject> toJSONs(Object obj) {
		List<JSONObject> jsonObjects = new ArrayList<>();
		if (getGridIdentity().equalsIgnoreCase(MAIN_GRID)) {
			JSONObject jsonObject = new JSONObject();
			JSONArray rows = new JSONArray();
			Warehouse warehouseAccessLog = (Warehouse) obj;
			if (warehouseAccessLog.getTypez() != 0) {
				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(branchesMap.get(warehouseAccessLog.getBranchId()));
				}
				Warehouse samithi = locationService.findSamithiById(warehouseAccessLog.getId());
				rows.add(!ObjectUtil.isEmpty(samithi) ? samithi.getName() : "NA");

				long agentCount = agentService.findAgentCountByWarehouseId(samithi.getId());
				rows.add(!ObjectUtil.isEmpty(samithi) ? agentCount : "0");

				long farmerCount = farmerService.listOfFarmerCountBySamithiId(samithi.getId());
				rows.add(!ObjectUtil.isEmpty(samithi) ? farmerCount : "0");
				
				if (agentCount != 0) {
					List<String> agent = agentService.findAgentByWareHouseId(samithi.getId());
					List<Object[]>  enrollArr=farmerService.findCountOfFarmerEnrollment(agent);
					if(!ObjectUtil.isListEmpty(enrollArr)){
						for(Object[] object:enrollArr){
							if(!ObjectUtil.isEmpty(object[1])){
								rows.add(object[1].toString());
							}else{
								rows.add("0");
							}
						}
					}else{
						rows.add("0");
					}
					// List<String> txnList =
					// agentService.listTxnTypeByAgentId(agent);
					for (String txnType : txnTypes) {
						List<String> txnList = new LinkedList<>();
						txnList.add(txnType.trim());
						if(txnType.equals("334")){
							txnList.add("334P");
							txnList.add("334D");
						}
						List<Object[]> agrotrxn = agentService.listAgrotxnDetailsByAgentIdAndTxnType(agent, txnList);
						if (!ObjectUtil.isListEmpty(agrotrxn)) {
							for (Object agro[] : agrotrxn) {
								if (agro[0] != null) {
									// rows.add(!ObjectUtil.isEmpty(agro[0])?
									// getText(agro[0].toString()) : "NA");
									rows.add(!ObjectUtil.isEmpty(agro[1]) ? agro[1] : "0");

								}else{
									rows.add("0");
								}
							}
						}else{
							rows.add("0");
						}
					}
				}else{
					rows.add("0");
					for(String row:txnTypes){
					rows.add("0");
					}
				}

				jsonObject.put("id", samithi.getId());
				jsonObject.put("cell", rows);

				jsonObjects.add(jsonObject);
			}
		} else if (getGridIdentity().equalsIgnoreCase(SUB_GRID)) {

			String agrotxn = (String) obj;

			Warehouse samithi = locationService.findSamithiById(Long.valueOf(id));
			List<String> agent = agentService.findAgentByWareHouseId(samithi.getId());
			List<String> txnList = agentService.listTxnTypeByAgentId(agent);
			List<Object[]> agrotrxn = agentService.listAgrotxnDetailsByAgentId(agent, txnList);
			if (!ObjectUtil.isListEmpty(agrotrxn)) {

				for (Object agro[] : agrotrxn) {
					JSONObject jsonObject = new JSONObject();
					JSONArray rows1 = new JSONArray();
					if (agro[0] != null) {
						rows1.add(!ObjectUtil.isEmpty(agro[0]) ? getText(agro[0].toString()) : "NA");
						rows1.add(!ObjectUtil.isEmpty(agro[1]) ? agro[1] : "0");
						jsonObject.put("id", agent.get(0));
						jsonObject.put("cell", rows1);
						jsonObjects.add(jsonObject);

					}

				}

			}

		}

		return jsonObjects;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected String sendJSONResponse(Map data) throws Exception {

		JSONObject gridData = new JSONObject();
		gridData.put(PAGE, getPage());
		totalRecords = (Integer) data.get(RECORDS);
		gridData.put(TOTAL, java.lang.Math.ceil(totalRecords / Double.valueOf(Integer.toString(getLimit()))));
		gridData.put(RECORDS, totalRecords);

		List list = (List) data.get(ROWS);
		if (getGridIdentity().equalsIgnoreCase(SUB_GRID)) {
			if (!ObjectUtil.isListEmpty(list)) {
				String ss = list.get(0).toString();
				list.removeAll(list);
				list.add(ss);
			}
		}
		JSONArray rows = new JSONArray();
		if (list != null) {
			branchIdValue = getBranchId();
			if (StringUtil.isEmpty(branchIdValue)) {
				buildBranchMap();
			}

			for (Object record : list) {
				// rows.add(toJSON(record));

				toJSONs(record).stream().forEach(jsonboject -> {
					rows.add(jsonboject);
				});

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

	public String populateXLS() throws Exception {

		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getText("GroupActivityReport") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("groupActivityReport"), fileMap, ".xls"));
		return "xls";
	}

	public int getPicIndex(HSSFWorkbook wb) throws IOException {

		int index = -1;

		byte[] picData = null;
		picData = clientService.findLogoByCode(Asset.APP_LOGO);

		if (picData != null)
			index = wb.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
	}

	int serialNo =1;
	public InputStream getExportDataStream(String exportType) throws IOException {

		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		setsDate(IExporter.EXPORT_DAILY.equalsIgnoreCase(exportType) ? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));
		boolean flag = true;
		DateFormat filterDateFormat = new SimpleDateFormat(FILTERDATE);
		AgentAccessLog agentAccessLog = new AgentAccessLog();

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("groupActivityReportList"));
		HSSFPatriarch drawing = sheet.createDrawingPatriarch();

		HSSFCellStyle style1 = wb.createCellStyle();
		HSSFCellStyle style2 = wb.createCellStyle();
		HSSFCellStyle style3 = wb.createCellStyle();
		HSSFCellStyle alignStyle = wb.createCellStyle();
		alignStyle.setAlignment(alignStyle.ALIGN_RIGHT);
		HSSFCellStyle filterStyle = wb.createCellStyle();

		HSSFFont font1 = wb.createFont();
		font1.setFontHeightInPoints((short) 22);

		HSSFFont font2 = wb.createFont();
		font2.setFontHeightInPoints((short) 12);

		HSSFFont font3 = wb.createFont();
		font3.setFontHeightInPoints((short) 10);

		HSSFFont filterFont = wb.createFont();
		filterFont.setFontHeightInPoints((short) 12);

		HSSFRow row, titleRow, filterRowTitle, filterRow1, filterRow2, filterRow3, filterRow4 = null, filterRow5 = null,
				filterRow6 = null, filterRow7 = null;
		HSSFCell cell;

		
		int imgRow1 = 0;
		int imgRow2 = 4;
		int imgCol1 = 0;
		int imgCol2 = 0;
		int titleRow1 = 2;
		int titleRow2 = 5;

		int rowNum = 2;
		int colNum = 0;
		
		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, titleRow1, titleRow2));

		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap(); // build branch map to get branch name form branch id.

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(2);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("groupActivityReportList")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);

		if (isMailExport()) {
			rowNum++;
			rowNum++;
			if(!StringUtil.isEmpty(getWarehouse())){
			filterRowTitle = sheet.createRow(rowNum++);
			cell = filterRowTitle.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("filter")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);
			}
			
			if (!StringUtil.isEmpty(getWarehouse())) {
				agentAccessLog.setProfileId(getSelectedFieldStafId());
				filterRow1 = sheet.createRow(rowNum++);
				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("group")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
				cell = filterRow1.createCell(2);
				Warehouse samithi = locationService.findSamithiById(!StringUtil.isEmpty(getWarehouse())?Long.valueOf(getWarehouse()):0);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(samithi)?samithi.getName():""));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			filterRow5 = sheet.createRow(rowNum++);
			filterRow6 = sheet.createRow(rowNum++);
		}
		rowNum++;

		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		String mainGridColumnHeaders = getLocaleProperty("SamithiExportColumnHeader");
		int mainGridIterator = 0;
		ESESystem preferences = preferncesService.findPrefernceById("1");
		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {

			if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
			} else if (cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());
			}
			
			if (StringUtil.isEmpty(branchIdValue)) { // Add branch header to
														// export sheet if main
														// branch logged in.

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
			} else {
				if (!cellHeader.equalsIgnoreCase(getText("app.branch"))) {
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
			}

		}

		Warehouse samithi = new Warehouse();
		
		if (!StringUtil.isEmpty(warehouse)) {
			samithi.setId(Long.valueOf(warehouse));
		}
		 

		super.filter = samithi;
		Map data = isMailExport() ? readData() : readExportData();
		List<Warehouse> mainGridRows = (List<Warehouse>) data.get(ROWS);
		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;
		// Long serialNo = 0L;
		for (Warehouse warehouse : mainGridRows) {
			// serialNo++;
			if (isMailExport() && flag) {
	//// Beginning of code:
				
				
					row = sheet.createRow(rowNum++);
					colNum = 0;

					cell = row.createCell(colNum++);
					style3.setAlignment(CellStyle.ALIGN_CENTER);
					cell.setCellStyle(style3);
					cell.setCellValue(serialNo);
					serialNo = serialNo + 1;
					if ((getIsMultiBranch().equalsIgnoreCase("1")
							&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
						if (StringUtil.isEmpty(branchIdValue)) {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(!StringUtil
									.isEmpty(getBranchesMap().get(getParentBranchMap().get(warehouse.getBranchId())))
											? getBranchesMap().get(getParentBranchMap().get(warehouse.getBranchId()))
											: getBranchesMap().get(warehouse.getBranchId())));
						}
						cell = row.createCell(colNum++);
						cell.setCellValue(getBranchesMap().get(warehouse.getBranchId()));

					} else {
						if (StringUtil.isEmpty(branchIdValue)) {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(warehouse.getBranchId())
									? (branchesMap.get(warehouse.getBranchId())) : ""));
						}
					}
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(warehouse.getName()));

					cell = row.createCell(colNum++);
					long agentCount = agentService.findAgentCountByWarehouseId(warehouse.getId());
					cell.setCellStyle(alignStyle);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(warehouse) ? String.valueOf(agentCount) : "0"));
					cell = row.createCell(colNum++);
					long farmerCount = farmerService.listOfFarmerCountBySamithiId(warehouse.getId());
					cell.setCellStyle(alignStyle);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(warehouse) ? String.valueOf(farmerCount) : "0"));
	
					if (agentCount != 0) {
						List<String> agt = agentService.findAgentByWareHouseId(warehouse.getId());
						List<Object[]>  enrollArr=farmerService.findCountOfFarmerEnrollment(agt);
						if(!ObjectUtil.isListEmpty(enrollArr)){
							for(Object[] object:enrollArr){
								cell = row.createCell(colNum++);
								cell.setCellStyle(alignStyle);
								cell.setCellValue(new HSSFRichTextString(
										!ObjectUtil.isEmpty(object[1]) ? String.valueOf(object[1]) : "0"));
							}}
						/*}*/
							
					/*if (agentCount != 0) {*/
						List<String> agent = agentService.findAgentByWareHouseId(warehouse.getId());
						for (String txnType : txnTypes) {
							List<String> txnList = new LinkedList<>();
							txnList.add(txnType.trim());
							if(txnType.equals("334")){
								txnList.add("334P");
								txnList.add("334D");
							}
							List<Object[]> agrotrxn = agentService.listAgrotxnDetailsByAgentIdAndTxnType(agent, txnList);
							if (!ObjectUtil.isListEmpty(agrotrxn)) {
								for (Object agro[] : agrotrxn) {
									if (agro[0] != null) {
										cell = row.createCell(colNum++);
										cell.setCellStyle(alignStyle);
										cell.setCellValue(new HSSFRichTextString(
												!ObjectUtil.isEmpty(agro[1]) ? String.valueOf(agro[1]) : "0"));
									}else{
										cell = row.createCell(colNum++);
										cell.setCellValue(new HSSFRichTextString("0"));
										cell.setCellStyle(alignStyle);
									}
								}
							} else {
								cell = row.createCell(colNum++);
								cell.setCellValue(new HSSFRichTextString("0"));
								cell.setCellStyle(alignStyle);
							}
						}
					} else {
						cell = row.createCell(colNum++);
						cell.setCellStyle(alignStyle);
						cell.setCellValue(new HSSFRichTextString("0"));
						for (String rows : txnTypes) {
							cell = row.createCell(colNum++);
							cell.setCellStyle(alignStyle);
							cell.setCellValue(new HSSFRichTextString("0"));
						}
					}

				}

			
		}
		int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		// picture.resize();
		String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
				: request.getSession().getId();
		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("groupActivityReportList") + fileNameDateFormat.format(new Date()) + ".xls";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		wb.write(fileOut);
		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		fileOut.close();

		return stream;

	}

	public Map<String, String> getDeviceList() {

		Map<String, String> deviceList = new LinkedHashMap<>();
		List<Device> device = deviceService.listDevices();
		deviceList = device.stream().collect(Collectors.toMap(Device::getSerialNumber, Device::getName));
		return deviceList;
	}

	public Map<String, String> getFieldStaffList() {

		Map<String, String> agentMap = new LinkedHashMap<>();
		List<Object[]> agentList = agentService.listAgentNameProfIdAndIdByBranch(getBranchId());
		if (!ObjectUtil.isListEmpty(agentList)) {
			agentMap = agentList.stream().collect(Collectors.toMap(obj -> String.valueOf(obj[0]),
					obj -> (String.valueOf(obj[0]) + "-" + String.valueOf(String.valueOf(obj[2])))));
		}
		return agentMap;
	}

	public Object getFilter() {

		return filter;
	}

	public void setFilter(Object filter) {

		this.filter = filter;
	}

	public String getGridIdentity() {

		return gridIdentity;
	}

	public void setGridIdentity(String gridIdentity) {

		this.gridIdentity = gridIdentity;
	}

	public String getSelectedFieldStafId() {

		return selectedFieldStafId;
	}

	public void setSelectedFieldStafId(String selectedFieldStafId) {

		this.selectedFieldStafId = selectedFieldStafId;
	}

	public String getSelectedDevice() {

		return selectedDevice;
	}

	public void setSelectedDevice(String selectedDevice) {

		this.selectedDevice = selectedDevice;
	}

	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}

	public Map<String, String> getFields() {
		return fields;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
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

	/*public Map<Long, String> getWarehouseList() {
		Map<Long, String> samithiMap = new LinkedHashMap<>();
		samithi = locationService.listSamithiesBasedOnType();
		samithiMap = samithi.stream().collect(Collectors.toMap(Warehouse::getId, Warehouse::getName));
		return samithiMap;
	}
*/
	public List<Warehouse> getSamithi() {

		return samithi;
	}

	public void setSamithi(List<Warehouse> samithi) {

		this.samithi = samithi;
	}

	public String getWarehouse() {

		return warehouse;
	}

	public void setWarehouse(String warehouse) {

		this.warehouse = warehouse;
	}

	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}

	public String getExportLimit() {
		return exportLimit;
	}

	public void setExportLimit(String exportLimit) {
		this.exportLimit = exportLimit;
	}

	public String getXlsFileName() {
		return xlsFileName;
	}

	public void setXlsFileName(String xlsFileName) {
		this.xlsFileName = xlsFileName;
	}

	public void populateWarehouseList(){
		JSONArray warehouseArr = new JSONArray();
		  List<Warehouse> warehouseList = locationService.listSamithiesBasedOnType();
		  if (!ObjectUtil.isEmpty(warehouseList)) {
			  warehouseList.forEach(obj -> {
				  warehouseArr.add(getJSONObject(obj.getId(), obj.getName()));
		   });
		  }
		  sendAjaxResponse(warehouseArr);
	}
}
