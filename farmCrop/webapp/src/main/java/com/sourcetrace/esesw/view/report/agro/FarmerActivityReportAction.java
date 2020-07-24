package com.sourcetrace.esesw.view.report.agro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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

import com.ese.entity.profile.ViewFarmerActivity;
import com.ese.entity.txn.training.TrainingStatus;
import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.DynamicFeildMenuConfig;
import com.sourcetrace.eses.entity.DynamicFieldConfig;
import com.sourcetrace.eses.order.entity.txn.Distribution;
import com.sourcetrace.eses.order.entity.txn.DistributionDetail;
import com.sourcetrace.eses.order.entity.txn.PeriodicInspection;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.order.entity.txn.ProcurementDetail;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.ITrainingService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicData;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

public class FarmerActivityReportAction extends BaseReportAction implements IExporter {

	private static final long serialVersionUID = 1L;
	private ViewFarmerActivity filter;
	private String selectedFieldStafId;
	private String selectedDevice;

	private String branchIdParma;
	private String farmerId;
	private String firstName;
	private String daterange;
	private String mainGridCols;
	@Autowired
	private IAgentService agentService;
	private ILocationService locationService;
	private IFarmerService farmerService;
	@Autowired
	private IDeviceService deviceService;
	private IPreferencesService preferncesService;
	private IProductDistributionService productDistributionService;
	private ITrainingService trainingService;
	private IClientService clientService;

	private List<Procurement> procurementList;
	private List<ProcurementDetail> procurementDetails;

	private List<Distribution> distributionList;
	private List<DistributionDetail> distributionDetails;

	private List<TrainingStatus> trainingList;
	private Map<String, List<FarmerDynamicData>> dynamicMenuList;

	private List<PeriodicInspection> periodicInspectionList;

	private Map<String, Object> txnMap = new LinkedHashMap<String, Object>();
	private Map<String, String> menuNames = new LinkedHashMap<String, String>();
	private String xlsFileName;
	private InputStream fileInputStream;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	@Override
	public String list() {
		Calendar currentDate = Calendar.getInstance();
		Calendar cal = (Calendar) currentDate.clone();
		cal.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) - 1);
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		super.startDate = df.format(cal.getTime());
		super.endDate = df.format(currentDate.getTime());

		daterange = super.startDate + " - " + super.endDate;
		request.setAttribute(HEADING, getText(LIST));
		//filter = new ViewFarmerActivity();
		formMainGridCols();
		setFilter(filter);//filter
		return LIST;
	}

	public String data() throws Exception {

		filter = new ViewFarmerActivity();

		if (!StringUtil.isEmpty(farmerId)) {
			filter.setFid(Long.valueOf(farmerId));
		}

		if (!StringUtil.isEmpty(getBranchIdParma())) {
			if (!getIsMultiBranch().equalsIgnoreCase("1")) {
				List<String> branchList = new ArrayList<>();
				branchList.add(branchIdParma);
				filter.setBranchesList(branchList);
			} else {
				List<String> branchList = new ArrayList<>();
				List<BranchMaster> branches = clientService.listChildBranchIds(branchIdParma);
				branchList.add(branchIdParma);
				branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
					branchList.add(branch.getBranchId());
				});
				filter.setBranchesList(branchList);
			}

		}

		getStartDate();
		getEndDate();
		List<DynamicFeildMenuConfig> dy = farmerService.listDynamicMenus();
		dy.stream().forEach(u -> {
			menuNames.put(u.getTxnType(), u.getLangName(getLoggedInUserLanguage()));
		});
		super.filter = this.filter;
		// Map data = readData("farmerData");
		Map data = readData();
		return sendJSONResponse(data);
	}

	@SuppressWarnings("rawtypes")
	public Map readData() {

		Map data = farmerService.listActivityReport(getSord(), getSidx(), getStartIndex(), getLimit(), getsDate(),
				geteDate(), filter, getPage(), null);
		return data;
	}

	public String detail() throws Exception {

		if (this.id != null && !this.id.equals("")) {

			procurementDetails = new ArrayList<ProcurementDetail>();

			distributionDetails = new ArrayList<DistributionDetail>();

			trainingList = new ArrayList<TrainingStatus>();

			dynamicMenuList = new HashMap<>();

			Farmer farmer = farmerService.findFarmerById(Long.valueOf(this.id));

			if (!ObjectUtil.isEmpty(farmer)) {

				procurementList = productDistributionService.findProcurementByFarmerId(farmer.getId());

				for (Procurement procure : procurementList) {

					procurementDetails.addAll(procure.getProcurementDetails());

					/*
					 * * List<ProcurementDetail> procurementDetail
					 * =productDistributionService.
					 * findProcurementDetailByProcurementId(procure.getId());
					 * 
					 * if (!ObjectUtil.isListEmpty(procurementDetail)) {
					 * procurementDetails.addAll(procurementDetail); }
					 */

				}

				distributionList = productDistributionService.findDistributionByFarmerId(farmer.getFarmerId());
				for (Distribution dis : distributionList) {

					distributionDetails.addAll(dis.getDistributionDetails());

					/**
					 * List<DistributionDetail> distributionDet =
					 * productDistributionService.
					 * findDistributionDetailByDistributionId(dis.getId());
					 * 
					 * if (!ObjectUtil.isListEmpty(distributionDet)) {
					 * distributionDetails.addAll(distributionDet); }
					 */

				}

				periodicInspectionList = clientService.findPeriodicInspectionByFarmerId(farmer.getFarmerId());
				List<FarmerDynamicData> fds = farmerService.ListFarmerDynamicDatas(farmer.getId());
				List<DynamicFeildMenuConfig> dy = farmerService.listDynamicMenus();

				dy.stream().forEach(dys -> {
					StringBuilder sb = new StringBuilder();
					dys.getDynamicFieldConfigs().stream().filter(
							u -> u.getField().getIsReportAvail() != null && u.getField().getIsReportAvail().equals("1"))
							.forEach(dynamicFieldReportConfig -> {
								sb.append(getLocaleProperty(
										dynamicFieldReportConfig.getField().getLangName(getLoggedInUserLanguage()))
										+ "|");
							});
					menuNames.put(dys.getTxnType(), dys.getLangName(getLoggedInUserLanguage()) + "~" + sb.toString());

				});
				fds.stream().forEach(u -> {
					DynamicFeildMenuConfig dyMenu = dy.stream().filter(p -> (p.getTxnType().equals(u.getTxnType())))
							.findAny().orElse(null);
					if (dyMenu != null) {
						u.setReportValues(getDynamicFieldValue(u, dyMenu));
					}
					if (dynamicMenuList.containsKey(menuNames.get(u.getTxnType()))) {
						List<FarmerDynamicData> fd = dynamicMenuList.get(menuNames.get(u.getTxnType()));

						fd.add(u);
						dynamicMenuList.put(menuNames.get(u.getTxnType()), fd);

					} else {
						List<FarmerDynamicData> fd = new ArrayList<>();

						fd.add(u);
						dynamicMenuList.put(menuNames.get(u.getTxnType()), fd);
					}
				});

				dynamicMenuList = fds.stream().collect(Collectors.groupingBy(u -> menuNames.get(u.getTxnType())));

				List<TrainingStatus> trainingListData = trainingService
						.findTrainingStatusByFarmerId(farmer.getFarmerId());

				if (!ObjectUtil.isListEmpty(trainingListData)) {
					trainingList.addAll(trainingListData);
				}
			}
			return "detail";
		} else {
			addActionError(NO_RECORD);
			return REDIRECT;
		}

	}

	LinkedList<String> result = new LinkedList();

	public LinkedList<String> getDynamicFieldValue(FarmerDynamicData farmerDynamicData, DynamicFeildMenuConfig dy) {
		Map<String, String> valuesMap = new LinkedHashMap<>();
		result = new LinkedList();
		farmerDynamicData.getFarmerDynamicFieldsValues().stream().forEach(dynamicFieldValues -> {

			if (!StringUtil.isEmpty(dynamicFieldValues.getComponentType()) && Arrays
					.asList(String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.DROP_DOWN.ordinal()),
							String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.RADIO.ordinal()),
							String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal()),
							String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.CHECKBOX.ordinal()))
					.contains(dynamicFieldValues.getComponentType())) {
				valuesMap.put(dynamicFieldValues.getFieldName(),
						getCatlogueValueByCode(dynamicFieldValues.getFieldValue()).getName());
			} else {
				valuesMap.put(dynamicFieldValues.getFieldName(), dynamicFieldValues.getFieldValue());
			}

		});
		dy.getDynamicFieldConfigs().stream()
				.filter(u -> u.getField().getIsReportAvail() != null && u.getField().getIsReportAvail().equals("1"))
				.forEach(dynamicFieldReportConfig -> {
					if (valuesMap.containsKey(dynamicFieldReportConfig.getField().getCode())) {
						result.add(valuesMap.get(dynamicFieldReportConfig.getField().getCode()));
					}
				});

		return result;
	}
 	
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		Map<String, String> dynamap = new HashMap<>();	
		Object[] farmer = (Object[]) obj;		
		if (!ObjectUtil.isEmpty(farmer)) {

			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(farmer[3])))
							? getBranchesMap().get(getParentBranchMap().get(farmer[3]))
							: getBranchesMap().get(farmer[3]));
				}
				rows.add(getBranchesMap().get(farmer[3]));
			} else {
				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(branchesMap.get(farmer[3]));
				}
			}
			String firstName =  String.valueOf(farmer[2]);
			String farmerId = String.valueOf(farmer[0]);
			String linkField = "<a href=farmer_detail.action?id="+farmerId+" target=_blank>"+firstName+"</a>";
			rows.add(!ObjectUtil.isEmpty(linkField) ? linkField : "");// Farmer
			//rows.add(!StringUtil.isEmpty(farmer[2]) ? farmer[2] : "0");

			rows.add(!StringUtil.isEmpty(farmer[4]) ? farmer[4] : "0");
			rows.add(!StringUtil.isEmpty(farmer[5]) ? farmer[5] : "0");
			rows.add(!StringUtil.isEmpty(farmer[6]) ? farmer[6] : "0");
			rows.add(!StringUtil.isEmpty(farmer[7]) ? farmer[7] : "0");
			
			 rows.add(!StringUtil.isEmpty(farmer[8])? farmer[8] : "");
			 rows.add(!StringUtil.isEmpty(farmer[9])? farmer[9] : "");
			 if (farmer[10] != null) {
				if (farmer[10].toString().contains(",")) {
					Arrays.asList(farmer[10].toString().split(",")).stream().forEach(u -> {
						dynamap.put(u.trim().split("-")[0].trim().toString(), u.trim().split("-")[1].trim().toString());
					});
				} else {
					dynamap.put(farmer[10].toString().split("-")[0].trim().toString(),
							farmer[10].toString().split("-")[1].trim().toString());
				}

				menuNames.entrySet().stream().forEach(u -> {
					rows.add(dynamap.containsKey(u.getKey()) ? dynamap.get(u.getKey()) : "0");
				});

			} else {
				menuNames.entrySet().stream().forEach(u -> {
					rows.add("0");
				});
			}

		}

		jsonObject.put("id", farmer[0]);
		jsonObject.put("cell", rows);
		return jsonObject;
	}

	/*
	 * private Map<String, Object> txnCountByFarmer(){ if(txnMap.size()<=0){
	 * txnMap =
	 * productDistributionService.findDistributionAndProcurmentCountByFarmers().
	 * stream().collect(Collectors.toMap(obj->StringUtil.
	 * trimIncludingNonbreakingSpace(String.valueOf(obj[0])),obj->(Object[])obj)
	 * ); } return txnMap; }
	 */

	public String populateXLS() throws Exception {

		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getLocaleProperty("FarmerActivityReport") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("FarmerActivityReport"), fileMap, ".xls"));
		return "xls";
	}

	HSSFCell cell;
	int colNum = 0;
	HSSFRow row;

	public InputStream getExportDataStream(String exportType) throws IOException {

		Long serialNumber = 0L;

		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		setsDate(IExporter.EXPORT_DAILY.equalsIgnoreCase(exportType) ? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));
		boolean flag = true;

		DateFormat filterDateFormat = new SimpleDateFormat(getGeneralDateFormat());
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("farmerActivityReportList"));
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

		HSSFRow titleRow, filterRowTitle, filterRow1, filterRow2, filterRow3, filterRow4;
		colNum = 0;
		int imgRow1 = 0;
		int imgRow2 = 4;
		int imgCol1 = 0;
		int imgCol2 = 0;
		int titleRow1 = 2;
		int titleRow2 = 5;
		int rowNum = 2;

		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap(); // build branch map to get branch name form branch id.

		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, titleRow1, titleRow2));

		sheet.setDefaultColumnWidth(13);

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(2);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmerActivityReportList")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);
		formMainGridCols();
		if (isMailExport()) {
			rowNum++;
			rowNum++;
			filterRowTitle = sheet.createRow(rowNum++);
			cell = filterRowTitle.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("filter")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);

			//filterRow1 = sheet.createRow(rowNum++);

			if (!StringUtil.isEmpty(farmerId)) {
				filterRow1 = sheet.createRow(rowNum++);

				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmerName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
				Farmer f = farmerService.findFarmerById(Long.valueOf(farmerId));
				cell = filterRow1.createCell(2);
				cell.setCellValue(new HSSFRichTextString(f == null ? "" : f.getFirstName() + " " + f.getLastName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			filterRow2 = sheet.createRow(rowNum++);

			if (!StringUtil.isEmpty(branchIdParma)) {
				filterRow2 = sheet.createRow(rowNum++);
				cell = filterRow2.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("app.branch")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow2.createCell(2);
				cell.setCellValue(new HSSFRichTextString((branchesMap.get(branchIdParma))));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			filterRow3 = sheet.createRow(rowNum++);

			cell = filterRow3.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("StartingDate")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);

			cell = filterRow3.createCell(2);
			cell.setCellValue(new HSSFRichTextString(filterDateFormat.format(getsDate())));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);

			filterRow4 = sheet.createRow(rowNum++);

			cell = filterRow4.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("EndingDate")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);

			cell = filterRow4.createCell(2);
			cell.setCellValue(new HSSFRichTextString(filterDateFormat.format(geteDate())));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);

		}
		rowNum++;

		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);

		int mainGridIterator = 0;

		String headerLabel = null;

		mainGridCols = "S.No" +"|"+mainGridCols;
		for (String cellHeader : mainGridCols.split("\\|")) {

			if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
				headerLabel = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
			} else {
				headerLabel = cellHeader.trim();
			}

			if (StringUtil.isEmpty(branchIdValue)) { // Add branch header to
														// export sheet if main
														// branch logged in.

				cell = mainGridRowHead.createCell(mainGridIterator);
				cell.setCellValue(new HSSFRichTextString(headerLabel));
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
					cell.setCellValue(new HSSFRichTextString(headerLabel));
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

		filter = new ViewFarmerActivity();

		/*if (!StringUtil.isEmpty(farmerId)) {
			filter.setFirstName(farmerId);
		}*/
		

		if (!StringUtil.isEmpty(farmerId)) {
			filter.setFid(Long.valueOf(farmerId));
		}

		if (!StringUtil.isEmpty(getBranchIdParma())) {
			if (!getIsMultiBranch().equalsIgnoreCase("1")) {
				List<String> branchList = new ArrayList<>();
				branchList.add(branchIdParma);
				filter.setBranchesList(branchList);
			} else {
				List<String> branchList = new ArrayList<>();
				List<BranchMaster> branches = clientService.listChildBranchIds(branchIdParma);
				branchList.add(branchIdParma);
				branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
					branchList.add(branch.getBranchId());
				});
				filter.setBranchesList(branchList);
			}
		}

		getStartDate();
		getEndDate();
		List<DynamicFeildMenuConfig> dy = farmerService.listDynamicMenus();
		dy.stream().forEach(u -> {
			menuNames.put(u.getTxnType(), u.getLangName(getLoggedInUserLanguage()));
		});

		super.filter = filter;

		Map data1 = readData();

		Map data = isMailExport() ? readData() : readExportData();
		List<Farmer> mainGridRows = (List<Farmer>) data.get(ROWS);
		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;

		List<Object[]> dfata = (ArrayList) data1.get(ROWS);

		Long serialNo = 0L;

		for (Object[] obj : dfata) {
			Map<String, String> dynamap = new HashMap<>();
			row = sheet.createRow(rowNum++);
			colNum = 0;

			serialNumber++;
			cell = row.createCell(colNum++);
			//cell.setCellValue(
			//		new HSSFRichTextString(String.valueOf(serialNumber) != null ? String.valueOf(serialNumber) : ""));
			style3.setAlignment(CellStyle.ALIGN_CENTER);
			cell.setCellStyle(style3);
			cell.setCellValue(serialNumber);
			

			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

				if (StringUtil.isEmpty(branchIdValue)) {

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(obj[3])))
									? getBranchesMap().get(getParentBranchMap().get(obj[3]))
									: getBranchesMap().get(obj[3])));

				}

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(obj[3])));

			} else {
				if (StringUtil.isEmpty(branchIdValue)) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(obj[3])));
				}
			}

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(obj[2]) ? obj[2].toString() : "0"));

			// Object[] datas
			// =txnCountByFarmer().containsKey(obj[0].toString().trim())?(Object[])
			// txnCountByFarmer().get(obj[0].toString().trim()):null;

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(obj[4]) ? obj[4].toString() : "0"));

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(obj[5]) ? obj[5].toString() : "0"));
			
			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(obj[6]) ? obj[6].toString() : "0"));


			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(obj[7]) ? obj[7].toString() : "0"));

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(obj[8]) ? obj[8].toString() : "0"));

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(obj[9]) ? obj[9].toString() : "0"));

			if (obj[10] != null) {
				if (obj[10].toString().contains(",")) {
					Arrays.asList(obj[10].toString().split(",")).stream().forEach(u -> {
						dynamap.put(u.trim().split("-")[0].trim().toString(), u.trim().split("-")[1].trim().toString());
					});
				} else {
					dynamap.put(obj[10].toString().split("-")[0].trim().toString(),
							obj[10].toString().split("-")[1].trim().toString());
				}

				menuNames.entrySet().stream().forEach(u -> {
					cell = row.createCell(colNum++);
					cell.setCellValue(
							new HSSFRichTextString(dynamap.containsKey(u.getKey()) ? dynamap.get(u.getKey()) : "0"));

				});

			} else {
				menuNames.entrySet().stream().forEach(u -> {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString("0"));

				});
			}
		}

		/*for (int i = 0; i <= colNum; i++) {
			sheet.autoSizeColumn(i);
		}*/
		int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		// picture.resize();
		String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
				: request.getSession().getId();
		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("mobileUserActivityReportList") + fileNameDateFormat.format(new Date())
				+ ".xls";
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

	public IAgentService getAgentService() {
		return agentService;
	}

	public void setAgentService(IAgentService agentService) {
		this.agentService = agentService;
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

	public IDeviceService getDeviceService() {
		return deviceService;
	}

	public void setDeviceService(IDeviceService deviceService) {
		this.deviceService = deviceService;
	}

	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}

	public String getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}

	public Map<String, String> getFarmerList() {
		Map<String, String> farmerListMap = new LinkedHashMap<String, String>();
		List<Object[]> farmersList = farmerService.listFarmerInfo();
		/*
		 * farmerListMap = farmersList.stream() .collect(Collectors.toMap(obj ->
		 * String.valueOf(obj[3]), obj -> String.valueOf(obj[3])));
		 */
		farmersList.stream().forEach(farmer -> {
			if (!farmerListMap.containsKey(String.valueOf(farmer[3]))) {
				farmerListMap.put(String.valueOf(farmer[0]),
						String.valueOf(farmer[3]) + " " + String.valueOf(farmer[4]));
			}
		});

		return farmerListMap;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public List<Procurement> getProcurementList() {
		return procurementList;
	}

	public void setProcurementList(List<Procurement> procurementList) {
		this.procurementList = procurementList;
	}

	public List<ProcurementDetail> getProcurementDetails() {
		return procurementDetails;
	}

	public void setProcurementDetails(List<ProcurementDetail> procurementDetails) {
		this.procurementDetails = procurementDetails;
	}

	public List<Distribution> getDistributionList() {
		return distributionList;
	}

	public void setDistributionList(List<Distribution> distributionList) {
		this.distributionList = distributionList;
	}

	public List<DistributionDetail> getDistributionDetails() {
		return distributionDetails;
	}

	public void setDistributionDetails(List<DistributionDetail> distributionDetails) {
		this.distributionDetails = distributionDetails;
	}

	public List<TrainingStatus> getTrainingList() {
		return trainingList;
	}

	public void setTrainingList(List<TrainingStatus> trainingList) {
		this.trainingList = trainingList;
	}

	public List<PeriodicInspection> getPeriodicInspectionList() {
		return periodicInspectionList;
	}

	public void setPeriodicInspectionList(List<PeriodicInspection> periodicInspectionList) {
		this.periodicInspectionList = periodicInspectionList;
	}

	public IProductDistributionService getProductDistributionService() {
		return productDistributionService;
	}

	public void setProductDistributionService(IProductDistributionService productDistributionService) {
		this.productDistributionService = productDistributionService;
	}

	public ITrainingService getTrainingService() {
		return trainingService;
	}

	public void setTrainingService(ITrainingService trainingService) {
		this.trainingService = trainingService;
	}

	public IClientService getClientService() {
		return clientService;
	}

	public void setClientService(IClientService clientService) {
		this.clientService = clientService;
	}

	/*
	 * public Map<String, String> getDistributionMap() { if
	 * (distributionMap.size() <= 0) { List<Object[]> distribuionData =
	 * productDistributionService.findDistributionCount(); distributionMap =
	 * distribuionData.stream().collect(Collectors .toMap(disFarmer ->
	 * String.valueOf(disFarmer[0]), disFarmer ->
	 * String.valueOf(disFarmer[1]))); } return distributionMap; }
	 * 
	 * public void setDistributionMap(Map<String, String> distributionMap) {
	 * this.distributionMap = distributionMap; }
	 * 
	 * public Map<String, String> getProcurementMap() { if
	 * (procurementMap.size() <= 0) { List<Object[]> procurementData =
	 * productDistributionService.findProcurementCount();
	 * procurementData.stream().forEach(obj -> { if
	 * (!procurementMap.containsKey(StringUtil.trim(String.valueOf(obj[0])))) {
	 * procurementMap.put(StringUtil.trim(String.valueOf(obj[0])),
	 * StringUtil.trim(String.valueOf(obj[1]))); } }); } return procurementMap;
	 * }
	 * 
	 * public void setProcurementMap(Map<String, String> procurementMap) {
	 * this.procurementMap = procurementMap; }
	 */

	public String getBranchIdParma() {
		return branchIdParma;
	}

	public void setBranchIdParma(String branchIdParma) {
		this.branchIdParma = branchIdParma;
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

	public ViewFarmerActivity getFilter() {
		return filter;
	}

	public void setFilter(ViewFarmerActivity filter) {
		this.filter = filter;
	}

	public String getDaterange() {
		return daterange;
	}

	public void setDaterange(String daterange) {
		this.daterange = daterange;
	}

	private void formMainGridCols() {
		mainGridCols = "";
		List<DynamicFeildMenuConfig> dy = farmerService.listDynamicMenus();
		//mainGridCols += "S.No" +"|";
		if(getBranchId()==null){
			mainGridCols += getLocaleProperty("barnchId") +"|";
		}

		mainGridCols += getLocaleProperty("firstName") + "|" + getLocaleProperty("lastName") + "|"
				+ getLocaleProperty("farmerCode")+"|"+getLocaleProperty("distribution") + "|" + getLocaleProperty("procurement") + "|"
				+ getLocaleProperty("periodicInspection") + "|" + getLocaleProperty("training") + "|";
		mainGridCols += dy.stream().map(p -> String.valueOf(p.getLangName(getLoggedInUserLanguage())))
				.collect(Collectors.joining("|"));

	}

	public String getMainGridCols() {
		return mainGridCols;
	}

	public void setMainGridCols(String mainGridCols) {
		this.mainGridCols = mainGridCols;
	}

	public Map<String, List<FarmerDynamicData>> getDynamicMenuList() {
		return dynamicMenuList;
	}

	public void setDynamicMenuList(Map<String, List<FarmerDynamicData>> dynamicMenuList) {
		this.dynamicMenuList = dynamicMenuList;
	}

}
