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
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.order.entity.txn.AgentAccessLog;
import com.sourcetrace.eses.order.entity.txn.AgentAccessLogDetail;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

public class FieldstaffAccessReportAction extends BaseReportAction implements IExporter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object filter;
	private static final String MAIN_GRID = "MAIN_GRID";
	private static final String SUB_GRID = "SUB_GRID";
	private String selectedFieldStafId;
	private String selectedDevice;
	private String daterange;
	private String gridIdentity;
	// protected List<String> fieds = new ArrayList<String>();
	private Map<String, String> fields = new LinkedHashMap<String, String>();

	@Autowired
	private IAgentService agentService;
	@Autowired
	private IDeviceService deviceService;
	@Autowired
	private IPreferencesService preferncesService;
	private String branchIdParam;
	//static String txnTypes[] = { "308", "359", "357" };
	//static String pratibhatxnTypes[] = { "308", "314","363","364","360"};
	//static String neiTxnType[] = {"308"};
 
	private String xlsFileName;
	private InputStream fileInputStream;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private String exportLimit;

	@Override
	public String list() {

		Calendar currentDate = Calendar.getInstance();
		Calendar cal = (Calendar) currentDate.clone();
		cal.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) - 1);
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		super.startDate = df.format(cal.getTime());
		super.endDate = df.format(currentDate.getTime());

		daterange = super.startDate + " - " + super.endDate;

		// fields.add(getLocaleProperty("deviceName"));
		// fields.add(getLocaleProperty("agent"));
		fields.put("1", getText("lastTransactionDate"));
		fields.put("2", getText("agent"));
		// fields.put("3", getText("agent"));
		/*
		 * if ((getIsMultiBranch().equalsIgnoreCase("1") &&
		 * (getIsParentBranch().equals("1") ||
		 * StringUtil.isEmpty(branchIdValue)))) { fields.put("10",
		 * getText("app.branch")); } else if (StringUtil.isEmpty(getBranchId()))
		 * { fields.put("8", getText("app.branch")); }
		 */

		if (getIsMultiBranch().equalsIgnoreCase("1")) {
			if (StringUtil.isEmpty(getBranchId())) {
				fields.put("10", getText("app.branch"));
			} else if (getIsParentBranch().equals("1")) {
				fields.put("10", getText("app.subBranch"));
			}

		} else if (StringUtil.isEmpty(getBranchId())) {

			fields.put("8", getText("app.branch"));
		}

		return LIST;
	}

	public String detail() throws Exception {
		AgentAccessLog agentAccessLog = new AgentAccessLog();
		if (!StringUtil.isEmpty(getSelectedDevice())) {
			agentAccessLog.setSerialNo(getSelectedDevice());
		}
		if (!StringUtil.isEmpty(getSelectedFieldStafId())) {
			agentAccessLog.setProfileId(getSelectedFieldStafId());
		}
		if (!StringUtil.isEmpty(getSelectedDevice())) {
			agentAccessLog.setSerialNo(getSelectedDevice());
		}

		if (!StringUtil.isEmpty(branchIdParam)) {
			if (!getIsMultiBranch().equalsIgnoreCase("1")) {
				List<String> branchList = new ArrayList<>();
				branchList.add(branchIdParam);
				agentAccessLog.setBranchesList(branchList);
			} else {
				List<String> branchList = new ArrayList<>();
				List<BranchMaster> branches = clientService.listChildBranchIds(branchIdParam);
				branchList.add(branchIdParam);
				branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
					branchList.add(branch.getBranchId());
				});
				agentAccessLog.setBranchesList(branchList);
			}

		}

		if (!StringUtil.isEmpty(subBranchIdParma) && !subBranchIdParma.equals("0")) {
			agentAccessLog.setBranchId(subBranchIdParma);

		}

		setFilter(agentAccessLog);
		super.filter = this.filter;
		setGridIdentity(MAIN_GRID);
		Map data = readData("fieldStaffAccess");
		return sendJSONResponse(data);
	}

	public String populateSubGridDetails() throws Exception {

		AgentAccessLogDetail agentAccessLogDetail = new AgentAccessLogDetail();
		agentAccessLogDetail.setAgentAccessLog(new AgentAccessLog());
		agentAccessLogDetail.getAgentAccessLog().setId(Long.valueOf(getId()));
		setFilter(agentAccessLogDetail);

		super.filter = this.filter;
		setGridIdentity(SUB_GRID);
		Map data = readData();
		return sendJSONResponse(data);
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {
		String txnType = preferncesService.findPrefernceByName(ESESystem.TXN_TYPES_MOBILE_ACTIVITY);
		String[] types=txnType.split(",");
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		if (getGridIdentity().equalsIgnoreCase(MAIN_GRID)) {
			Object[] datas = (Object[]) obj;
		//	AgentAccessLog agentAccessLog = (AgentAccessLog) obj;
			/*
			 * if (StringUtil.isEmpty(branchIdValue)) {
			 * rows.add(branchesMap.get(agentAccessLog.getBranchId())); }
			 */

			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(!StringUtil
							.isEmpty(getBranchesMap().get(getParentBranchMap().get(datas[1].toString())))
									? getBranchesMap().get(getParentBranchMap().get(datas[1].toString()))
									: getBranchesMap().get(datas[1].toString()));
				}
				rows.add(getBranchesMap().get(datas[1].toString()));
			} else {
				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(branchesMap.get(datas[1].toString()));
				}
			}

			/*
			 * Device device =
			 * deviceService.findDeviceBySerialNumber(agentAccessLog.getSerialNo
			 * ()); if (!ObjectUtil.isEmpty(device)) {
			 * rows.add(device.getName()); }else { rows.add(""); }
			 */
			ESESystem preferences = preferncesService.findPrefernceById("1");
			if (!StringUtil.isEmpty(preferences)) {
				DateFormat genDate = new SimpleDateFormat(
						preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
				rows.add(String.valueOf(genDate.format(datas[2])));
			}
			rows.add(datas[3].toString());
			Agent agent = agentService.findAgentByAgentId(datas[3].toString());
			if (!ObjectUtil.isEmpty(agent)) {
				
			//	rows.add(agent.getProfileId());
				rows.add(agent.getPersonalInfo().getFirstName());
				
				//rows.add(getText("bod" + agent.getBodStatus()));
			/*	if(getCurrentTenantId().equalsIgnoreCase("pratibha")){
					for (String type : pratibhatxnTypes) {
						accessLogDetail = agentService.listAgnetAccessLogDetailsByIdTxnType(Long.valueOf(datas[0].toString()),
								type.trim());
						if (!ObjectUtil.isEmpty(accessLogDetail)) {
							rows.add(accessLogDetail.getTxnCount());
						} else {
							rows.add("0");
						}
					}
				}
				else{*/
				//String txnType = preferncesService.findPrefernceByName(ESESystem.TXN_TYPES);
				//String txnTypes[]=txnType.split(",");
		

			} else {
				rows.add("");
				//rows.add("");
				//rows.add("");
			}
			AgentAccessLogDetail accessLogDetail;
			for (String type : types) {
				accessLogDetail = agentService.listAgnetAccessLogDetailsByIdTxnType(Long.valueOf(datas[0].toString()),
						type.trim());
				if (!ObjectUtil.isEmpty(accessLogDetail)) {
					rows.add(accessLogDetail.getTxnCount());
				} else {
					rows.add("0");
				}
			}
			if (!ObjectUtil.isEmpty(agent)) {
				rows.add(getText("bod" + agent.getBodStatus()));
			}
			else {
				rows.add("");
				}
			//}
			

			jsonObject.put("id", Long.valueOf(datas[0].toString()));
			jsonObject.put("cell", rows);
		} else if (getGridIdentity().equalsIgnoreCase(SUB_GRID)) {
			AgentAccessLogDetail agentAccessLogDetail = (AgentAccessLogDetail) obj;
			if (!ObjectUtil.isEmpty(agentAccessLogDetail)) {
				
					rows.add(getText(agentAccessLogDetail.getTxnType()));
				
				rows.add(String.valueOf(agentAccessLogDetail.getTxnCount()));
				ESESystem preferences = preferncesService.findPrefernceById("1");
				if (!StringUtil.isEmpty(preferences)) {
					DateFormat genDate = new SimpleDateFormat(
							preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
					rows.add(String.valueOf(genDate.format(agentAccessLogDetail.getTxnDate())));
				}

			}
			jsonObject.put("id", agentAccessLogDetail.getId());
			jsonObject.put("cell", rows);
		}
		return jsonObject;
	}

	public String populateXLS() throws Exception {

		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getText("MobileUserActivityReport") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("MobileUserActivityReport"), fileMap, ".xls"));
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

	public InputStream getExportDataStream(String exportType) throws IOException {

		Long serialNumber = 0L;

		
		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		setsDate(IExporter.EXPORT_DAILY.equalsIgnoreCase(exportType) ? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));
		boolean flag = true;
		DateFormat filterDateFormat = new SimpleDateFormat(FILTERDATE);
		AgentAccessLog agentAccessLog = new AgentAccessLog();

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("mobileUserActivityReportList"));
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

		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap(); // build branch map to get branch name form branch id.
		
		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, titleRow1, titleRow2));

		sheet.setDefaultColumnWidth(13);

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(2);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("mobileUserActivityReportList")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);

		if (isMailExport()) {
			rowNum++;
			rowNum++;
			if (!StringUtil.isEmpty(getSelectedFieldStafId()) || !StringUtil.isEmpty(branchIdParam)){
			filterRowTitle = sheet.createRow(rowNum++);
			cell = filterRowTitle.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("filter")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);
			}
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
			if (!StringUtil.isEmpty(getSelectedFieldStafId())) {
				agentAccessLog.setProfileId(getSelectedFieldStafId());
				filterRow4 = sheet.createRow(rowNum++);
				cell = filterRow4.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("agent")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
				cell = filterRow4.createCell(2);
				Agent agent = agentService.findAgentByAgentId(agentAccessLog.getProfileId());
				cell.setCellValue(new HSSFRichTextString(agentAccessLog.getProfileId()+"-"+ ((!ObjectUtil.isEmpty(agent))?agent.getPersonalInfo().getFirstName() : " ")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			/*
			 * if (!StringUtil.isEmpty(branchIdParam)) { if
			 * (!getIsMultiBranch().equalsIgnoreCase("1")) { List<String>
			 * branchList = new ArrayList<>(); branchList.add(branchIdParam);
			 * agentAccessLog.setBranchesList(branchList); } else { List<String>
			 * branchList = new ArrayList<>(); List<BranchMaster> branches =
			 * clientService.listChildBranchIds(branchIdParam);
			 * branchList.add(branchIdParam); branches.stream().filter(branch ->
			 * !StringUtil.isEmpty(branch)).forEach(branch -> {
			 * branchList.add(branch.getBranchId()); });
			 * agentAccessLog.setBranchesList(branchList); }
			 * 
			 * }
			 * 
			 * if (!StringUtil.isEmpty(subBranchIdParma) &&
			 * !subBranchIdParma.equals("0")) {
			 * agentAccessLog.setBranchId(subBranchIdParma);
			 * 
			 * }
			 */

			if (!StringUtil.isEmpty(branchIdParam)) {
				/*
				 * if (StringUtil.isEmpty(branchIdValue)) {
				 * 
				 * filterRow5 = sheet.createRow(rowNum++); filterRow6 =
				 * sheet.createRow(rowNum++);
				 * 
				 * cell = filterRow5.createCell(1); cell.setCellValue(new
				 * HSSFRichTextString(getLocaleProperty("mainOrganization")));
				 * filterFont.setBoldweight((short) 12);
				 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				 * filterStyle.setFont(filterFont);
				 * cell.setCellStyle(filterStyle);
				 * 
				 * cell = filterRow5.createCell(2); cell.setCellValue(new
				 * HSSFRichTextString(getBranchesMap().get(getBranchIdParma())
				 * )); filterFont.setBoldweight((short) 12);
				 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				 * filterStyle.setFont(filterFont);
				 * cell.setCellStyle(filterStyle);
				 * 
				 * cell = filterRow6.createCell(1); cell.setCellValue(new
				 * HSSFRichTextString(getLocaleProperty("subOrganization")));
				 * filterFont.setBoldweight((short) 12);
				 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				 * filterStyle.setFont(filterFont);
				 * cell.setCellStyle(filterStyle);
				 * 
				 * cell = filterRow6.createCell(2); cell.setCellValue(new
				 * HSSFRichTextString((getBranchesMap().get(getSubBranchIdParma(
				 * ))))); filterFont.setBoldweight((short) 12);
				 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				 * filterStyle.setFont(filterFont);
				 * cell.setCellStyle(filterStyle); branchIdParam } else
				 */
				filterRow5 = sheet.createRow(rowNum++);
				cell = filterRow5.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("app.branch")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow5.createCell(2);
				cell.setCellValue(new HSSFRichTextString((branchesMap.get(branchIdParam))));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			filterRow5 = sheet.createRow(rowNum++);
			//filterRow6 = sheet.createRow(rowNum++);
		}
		rowNum++;

		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		
			//String mainGridColumnHeaders = getText("exportColumnHeader");
		
		String mainGridColumnHeaders = "";
				
		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("ExportFieldStaffAccessColumnHeadingBranch");
			} else if (!StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("ExportFieldStaffAccessColumnHeading");
			}
		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("exportMobileUserActivityReportColumnHeaderBranch");
			} else {
				mainGridColumnHeaders = getLocaleProperty("exportMobileUserActivityReportColumnHeader");
			}
		}
		if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("fieldStaffAccessReporHeader");
			} 
			else if (getBranchId().equalsIgnoreCase("organic")) {
				mainGridColumnHeaders = getLocaleProperty("organicfieldStaffAccessReporHeader");
			} else if (getBranchId().equalsIgnoreCase("bci")) {
				mainGridColumnHeaders = getLocaleProperty("BCIfieldStaffAccessReporHeader");
			}

		}
		
		int mainGridIterator = 0;
		
		String headerLabel=null;

		
		ESESystem preferences = preferncesService.findPrefernceById("1");
		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {
			
			if(cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT))		
			{		
				headerLabel=cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());		
			}		
			else		
			{		
				headerLabel=cellHeader.trim();		
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

		AgentAccessLog log = new AgentAccessLog();
		if (!StringUtil.isEmpty(getSelectedFieldStafId())) {
			log.setProfileId(getSelectedFieldStafId());
		}

		if (!StringUtil.isEmpty(branchIdParam)) {
			if (!getIsMultiBranch().equalsIgnoreCase("1")) {
				List<String> branchList = new ArrayList<>();
				branchList.add(branchIdParam);
				log.setBranchesList(branchList);
			} else {
				List<String> branchList = new ArrayList<>();
				List<BranchMaster> branches = clientService.listChildBranchIds(branchIdParam);
				branchList.add(branchIdParam);
				branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
					branchList.add(branch.getBranchId());
				});
				log.setBranchesList(branchList);
			}
		}

		if (!StringUtil.isEmpty(subBranchIdParma) && !subBranchIdParma.equals("0")) {
			log.setBranchId(subBranchIdParma);
		}
		super.filter = log;
		Map data = readData("fieldStaffAccess");
		List<Object[]> mainGridRows = (ArrayList) data.get(ROWS);
		//List<AgentAccessLog> mainGridRows = (List<AgentAccessLog>) data.get(ROWS);
		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;
		// Long serialNo = 0L;
		for (Object[] datas : mainGridRows) {
			// serialNo++;
			if (isMailExport() && flag) {

				{
					//// Beginning of code:
					row = sheet.createRow(rowNum++);
					colNum = 0;

					/*
					 * Agent agent =
					 * agentService.findAgentByAgentId(agentAccessLog.
					 * getProfileId()); if (!ObjectUtil.isEmpty(agent)) {
					 * ESESystem preferences =
					 * preferncesService.findPrefernceById("1"); if
					 * (!StringUtil.isEmpty(preferences)) { DateFormat
					 * genDate=new
					 * SimpleDateFormat(preferences.getPreferences().get(
					 * ESESystem.GENERAL_DATE_FORMAT));
					 * rows.add(String.valueOf(genDate.format(agentAccessLog.
					 * getLastTxnTime()))); } rows.add(agent.getProfileId());
					 * rows.add(agent.getPersonalInfo().getFirstName());
					 * AgentAccessLogDetail accessLogDetail; for(String
					 * type:txnTypes){ accessLogDetail=agentService.
					 * listAgnetAccessLogDetailsByIdTxnType(agentAccessLog.getId
					 * (),type.trim());
					 * if(!ObjectUtil.isEmpty(accessLogDetail)){
					 * rows.add(accessLogDetail.getTxnCount()); }else{
					 * rows.add("0"); } } } else { rows.add(""); rows.add("");
					 * rows.add(""); }
					 */
					
					serialNumber++;
					cell = row.createCell(colNum++);
					

							style3.setAlignment(CellStyle.ALIGN_CENTER);
							cell.setCellStyle(style3);
							cell.setCellValue(serialNumber);
							
					
					
					
					
					
					if ((getIsMultiBranch().equalsIgnoreCase("1")
							&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
						if (StringUtil.isEmpty(branchIdValue)) {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(!StringUtil
									.isEmpty(getBranchesMap().get(getParentBranchMap().get(datas[1].toString())))
											? getBranchesMap().get(getParentBranchMap().get(datas[1].toString()))
											: getBranchesMap().get(datas[1].toString())));
						}
						cell = row.createCell(colNum++);
						cell.setCellValue(getBranchesMap().get(datas[1].toString()));

					} else {
						if (StringUtil.isEmpty(branchIdValue)) {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(datas[1].toString())
									? (branchesMap.get(datas[1].toString())) : ""));
						}
					}
					cell = row.createCell(colNum++);
					ESESystem preference = preferncesService.findPrefernceById("1");
					if (!StringUtil.isEmpty(preference)) {

						DateFormat genDate = new SimpleDateFormat(
								preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
						cell.setCellValue(
								new HSSFRichTextString(String.valueOf(genDate.format(datas[2]))));
					} else {
						cell.setCellValue(new HSSFRichTextString(""));
					}
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(datas[3].toString()));
					Agent agent = agentService.findAgentByAgentId(datas[3].toString());
					if (!ObjectUtil.isEmpty(agent)) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(agent.getPersonalInfo().getFirstName()));
					} else {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(""));
					}
					AgentAccessLogDetail accessLogDetail;
					/*if(getCurrentTenantId().equalsIgnoreCase("pratibha")){
						for (String type : pratibhatxnTypes) {
							accessLogDetail = agentService.listAgnetAccessLogDetailsByIdTxnType(Long.valueOf(datas[0].toString()),
									type.trim());
							if (!ObjectUtil.isEmpty(accessLogDetail)) {
								cell = row.createCell(colNum++);
								cell.setCellStyle(alignStyle);
								cell.setCellValue(new HSSFRichTextString(accessLogDetail.getTxnCount().toString()));
							} else {
								cell = row.createCell(colNum++);
								cell.setCellStyle(alignStyle);
								cell.setCellValue(new HSSFRichTextString("0"));
							}
						}
						
					}
					else{*/
					String txnType = preferncesService.findPrefernceByName(ESESystem.TXN_TYPES_MOBILE_ACTIVITY);
					String[] types=txnType.split(",");
					for (String type : types) {
						accessLogDetail = agentService.listAgnetAccessLogDetailsByIdTxnType(Long.valueOf(datas[0].toString()),
								type.trim());
						if (!ObjectUtil.isEmpty(accessLogDetail)) {
							cell = row.createCell(colNum++);
							cell.setCellStyle(alignStyle);
							cell.setCellValue(new HSSFRichTextString(accessLogDetail.getTxnCount().toString()));
						} else {
							cell = row.createCell(colNum++);
							cell.setCellStyle(alignStyle);
							cell.setCellValue(new HSSFRichTextString("0"));
						}
					}
					//}

					if (!ObjectUtil.isEmpty(agent)) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(getText("bod" + agent.getBodStatus())));
					}
				}

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
		String fileName = getLocaleProperty("mobileUserActivityReportList") + fileNameDateFormat.format(new Date()) + ".xls";
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

/*	public Map<String, String> getFieldStaffList() {

		Map<String, String> agentMap = new LinkedHashMap<>();
		List<Object[]> agentList = agentService.listAgentNameProfIdAndIdByBranch(getBranchId());
		if (!ObjectUtil.isListEmpty(agentList)) {
			agentMap = agentList.stream().collect(Collectors.toMap(obj -> String.valueOf(obj[0]),
					obj -> (String.valueOf(obj[0]) + "-" + String.valueOf(String.valueOf(obj[2])))));
		}
		return agentMap;
	}*/
	
	public void populateFieldStaffList(){
		JSONArray agentArr = new JSONArray();
		List<Object[]> agentList = agentService.listAgentNameProfIdAndIdByBranch(getBranchId());
		if (!ObjectUtil.isListEmpty(agentList)) {
			agentList.forEach(obj -> {
				agentArr.add(getJSONObject(obj[0].toString(), obj[0].toString()+"-"+obj[2].toString()));
			});
		}
		sendAjaxResponse(agentArr);
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

	public String getBranchIdParam() {
		return branchIdParam;
	}

	public void setBranchIdParam(String branchIdParam) {
		this.branchIdParam = branchIdParam;
	}

	public String getDaterange() {
		return daterange;
	}

	public void setDaterange(String daterange) {
		this.daterange = daterange;
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

	public String getExportLimit() {
		return exportLimit;
	}

	public void setExportLimit(String exportLimit) {
		this.exportLimit = exportLimit;
	}

}
