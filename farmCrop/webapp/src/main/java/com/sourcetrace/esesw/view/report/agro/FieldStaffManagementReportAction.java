/* * FieldStaffManagementReportAction.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
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

import com.ese.entity.util.ESESystem;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import com.itextpdf.text.Element;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.LocationHistory;
import com.sourcetrace.eses.entity.LocationHistoryDetail;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.IExporter;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.view.BaseReportAction;


@SuppressWarnings("serial")
public class FieldStaffManagementReportAction extends BaseReportAction {

	private static final Logger LOGGER = Logger.getLogger(FieldStaffManagementReportAction.class);
	private static final SimpleDateFormat fieldStaffDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private Map<String, String> fields = new HashMap<>();
	private IAgentService agentService;
	private ILocationService locationService;
	private IDeviceService deviceService;
	private IPreferencesService preferncesService;
	private LocationHistory filter;
	private String coordinateValues = null;
	private String branchIdParma;
	private String daterange;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private String xlsFileName;
	private InputStream fileInputStream;
	private String agentId;
	DecimalFormat df = new DecimalFormat("0.0");
	private String totalDist;
	private String txnDate;
	Map<String, Device> deviceMap = new HashMap<>();
	private String coordinateStr = null;
	/**
	 * @see com.sourcetrace.esesw.view.BaseReportAction#list()
	 */
	public String list() {

		Calendar currentDate = Calendar.getInstance();
		Calendar cal = (Calendar) currentDate.clone();
		cal.set(Calendar.DATE, currentDate.get(Calendar.DATE) - 1);
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		super.startDate = df.format(cal.getTime());
		super.endDate = df.format(currentDate.getTime());

		daterange = super.startDate + " - " + super.endDate;
		request.setAttribute(HEADING, getText(LIST));

		return LIST;
	}

	/**
	 * Detail.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String detail() throws Exception {

		if (!StringUtil.isEmpty(branchIdParma)) {
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

		if (!StringUtil.isEmpty(subBranchIdParma) && !subBranchIdParma.equals("0")) {
			filter.setBranchId(subBranchIdParma);
		}

		super.filter = this.filter;
		return sendJSONResponse(readData());
	}

	/**
	 * Map view.
	 * 
	 * @return the string
	 */
	public String mapView() {

		try {
			StringBuffer coordinateBuffer = new StringBuffer();
			coordinateValues = new String();
			StringBuffer totalDist = new StringBuffer();
			Date startingDate = DateUtil.convertStringToDate(startDate, "MM/dd/yyyy");
			Date endingDate = DateUtil.convertStringToDate(endDate, "MM/dd/yyyy");
			Date dayStartDateTime = DateUtil.getDateWithoutTime(startingDate);
			Date dayEndDateTime = DateUtil.getDateWithLastMinuteofDay(endingDate);
			String accuracy=preferncesService.findPrefernceByName(ESESystem.IS_ACCURACY_VALUE);
			List<LocationHistoryDetail> locationHistoryDataList  = locationService.findLocationHistoryDetailById(Long.valueOf(id));
			//locationHistoryDataList = locHis.getLocHis
					
			if (locationHistoryDataList != null && locationHistoryDataList.size() > 0) {
				// Double totalDist = 0D;
				// totalDist = locationHistoryDataList.stream().mapToDouble(loc
				// -> Double.valueOf(loc.getDistance()))
				// .sum();
				// this.totalDist = df.format(totalDist).toString() + " Km";
				int i = 0;
				//if (locationHistoryDataList.size() <= 25) {*/
					for (LocationHistoryDetail locationHistory : locationHistoryDataList) {
						Device device = deviceList().get(locationHistory.getSerialNumber());
						coordinateBuffer.append(locationHistory.getLatitude()).append("|");
						coordinateBuffer.append(locationHistory.getLongitude()).append("|");
						coordinateBuffer.append(
								DateUtil.convertDateToString(locationHistory.getTxnTime(), "dd-MMM-yyyy HH:mm:ss"))
								.append("|");
						coordinateBuffer.append(locationHistory.getSerialNumber()).append("|");
						coordinateBuffer.append(device.getAgent().getPersonalInfo().getAgentName()).append("|");
						coordinateBuffer.append(device.getName()).append("|");
						coordinateBuffer.append(locationHistory.getAddress()).append("|");
						coordinateBuffer.append(locationHistory.getDistance()).append("#");
					}
					 /*	}else {
					for (LocationHistory locationHistory : locationHistoryDataList) {
						Device device = deviceList().get(locationHistory.getSerialNumber());
						Double dist = 0.0;

						if (!ObjectUtil.isEmpty(device) && !StringUtil.isEmpty(locationHistory.getLatitude())
								&& !StringUtil.isEmpty(locationHistory.getLongitude())) {
							if (i > 0) {
								dist = getGeoCodeDistance(
										Double.valueOf(locationHistoryDataList.get(i - 1).getLatitude()),
										Double.valueOf(locationHistoryDataList.get(i - 1).getLongitude()),
										Double.valueOf(locationHistoryDataList.get(i).getLatitude()),
										Double.valueOf(locationHistoryDataList.get(i).getLongitude()));
							}
							if ((dist > 0.05 || i == 0)) {

								coordinateBuffer.append(locationHistory.getLatitude()).append("|");
								coordinateBuffer.append(locationHistory.getLongitude()).append("|");
								coordinateBuffer.append(DateUtil.convertDateToString(locationHistory.getTxnTime(),
										"dd-MMM-yyyy HH:mm:ss")).append("|");
								coordinateBuffer.append(locationHistory.getSerialNumber()).append("|");
								coordinateBuffer.append(device.getAgent().getPersonalInfo().getAgentName()).append("|");
								coordinateBuffer.append(device.getName()).append("|");
								coordinateBuffer.append(locationHistory.getAddress()).append("|");
								coordinateBuffer.append(locationHistory.getDistance()).append("#");
							}
						}
						i++;
					}
				}*/

				coordinateValues = coordinateBuffer.toString();
				coordinateStr = totalDist.toString();
			//	filter = locationHistoryDataList.get(locationHistoryDataList.size() - 1);
				filter.setAgentName(agentService.findAgentNameByAgentId(filter.getAgentId()));
				
			}
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			e.printStackTrace();
		}
		return "mapView";
	}

	public Map<String, Device> deviceList() {
		if (deviceMap.size() == 0) {
			deviceMap = deviceService.listDevices().stream()
					.collect(Collectors.toMap(Device::getSerialNumber, dev -> dev));
		}
		return deviceMap;
	}

	private Double toRad(Double value) {
		return value * Math.PI / 180;
	}

	/**
	 * @see com.sourcetrace.esesw.view.BaseReportAction#toJSON(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		JSONObject jsonObject = new JSONObject();
		if (obj instanceof Object[]) {
			Object[] results = (Object[]) obj;
			JSONArray rows = new JSONArray();
			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(results[1])))
							? getBranchesMap().get(getParentBranchMap().get(results[1]))
							: getBranchesMap().get(results[1]));
				}
				rows.add(getBranchesMap().get(results[1]));
			} else {
				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(branchesMap.get(ObjectUtil.isEmpty(results[1]) ? null : results[1].toString()));
				}
			}
			ESESystem preferences = preferncesService.findPrefernceById("1");
			if (!StringUtil.isEmpty(preferences)) {
				DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
				rows.add(genDate.format(results[2]));
			}

			rows.add(results[3]);
			String agentName = agentService.findAgentNameByAgentId(String.valueOf(results[3]));
			rows.add(agentName);

			/*Date startingDate = DateUtil.convertStringToDate(fieldStaffDateFormat.format(results[2]), "yyyy-MM-dd");
			Date endingDate = DateUtil.convertStringToDate(fieldStaffDateFormat.format(results[2]), "yyyy-MM-dd");
			Date dayStartDateTime = DateUtil.getDateWithoutTime(startingDate);
			Date dayEndDateTime = DateUtil.getDateWithLastMinuteofDay(endingDate);*/
			
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)){	
			
				if (results[9] != null && results[10] != null && !StringUtil.isEmpty(results[9])
					&& !StringUtil.isEmpty(results[10])) {
				rows.add(results[9].toString());
				rows.add(results[10].toString());
			} else {
				String lat = "";
				String lon = "";
				GeoApiContext context = new GeoApiContext().setEnterpriseCredentials("gme-sourcetrace",
						"UCvsZ3GFpyuwhClHOLePJJT4s0A=");
				try {
					LatLng latlng = new LatLng(Double.valueOf(String.valueOf(results[5])),
							Double.valueOf(String.valueOf(results[6])));
					GeocodingResult[] resultz = GeocodingApi.newRequest(context).latlng(latlng).await();
					rows.add(resultz[1].formattedAddress);
					lat = resultz[1].formattedAddress;
				} catch (Exception e) {
					rows.add("");
				}
				try {
					LatLng detsLatLng = new LatLng(Double.valueOf(String.valueOf(results[7])),
							Double.valueOf(String.valueOf(results[8])));
					GeocodingResult[] dresultz = GeocodingApi.newRequest(context).latlng(detsLatLng).await();
					rows.add(dresultz[1].formattedAddress);
					lon = dresultz[1].formattedAddress;
				} catch (Exception e) {
					rows.add("");
				}
				locationService.updateLocHistory(Long.valueOf(results[0].toString()), lat, lon);
				}
			}	
			
			
			
			
			

			rows.add("<button type='button' class='btn btn-sts' onclick='redirectToMapView(\"" + results[3] + "\",\""
					+ fieldStaffDateFormat.format(results[2]) + "\",\"" + results[0] + "\" )'>" + "<i class='fa fa-eye' aria-hidden='true'></i>"
					+ getText("viewmap") + "</button>");

			jsonObject.put("id", results[0]);
			jsonObject.put("cell", rows);
		}
		return jsonObject;
	}

	private Double getDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
		/*
		 * locationHistoryDataList.stream().forEach(loc->{
		 * 
		 * var R = 6371e3; // metres
		 * 
		 * var a = toRad(lat1); var b = toRad(lat2); var c = toRad(lat2-lat1);
		 * var d = toRad(lon2-lon1);
		 * 
		 * var az = Math.sin(c/2) * Math.sin(c/2) + Math.cos(a) * Math.cos(b) *
		 * Math.sin(d/2) * Math.sin(d/2); var cz = 2 * Math.atan2(Math.sqrt(az),
		 * Math.sqrt(1-az));
		 * 
		 * var d = ((R * cz)/1000).toFixed(1);
		 * 
		 * *
		 * 
		 * 
		 * Double R = 6371e3; var a = toRad(lo); });
		 */

		Double dist = new Double(0D);
		Double R = 6371e3;
		Double a = toRad(lat1);
		Double b = toRad(lat2);
		Double c = toRad(lat2 - lat1);
		Double d = toRad(lon2 - lon1);

		Double az = Math.sin(c / 2) * Math.sin(c / 2) + Math.cos(a) * Math.cos(b) * Math.sin(d / 2) * Math.sin(d / 2);
		Double cz = 2 * Math.atan2(Math.sqrt(az), Math.sqrt(1 - az));

		dist = ((R * cz) / 1000);

		return Double.valueOf(df.format(dist));
	}

	/**
	 * Gets the agent list.
	 * 
	 * @return the agent list
	 */
	/*
	 * public List<Agent> getAgentList() {
	 * 
	 * return agentService.listAgent(); }
	 */
	public void populateAgentList() {
		JSONArray fieldStaffArr = new JSONArray();
		List<Agent> agentList = agentService.listAgent();
		if (!ObjectUtil.isEmpty(agentList)) {
			agentList.forEach(obj -> {
				fieldStaffArr.add(getJSONObject(obj.getProfileId(), obj.getPersonalInfo().getAgentName()));
			});
		}
		sendAjaxResponse(fieldStaffArr);
	}

	/**
	 * Sets the agent service.
	 * 
	 * @param agentService
	 *            the new agent service
	 */
	public void setAgentService(IAgentService agentService) {

		this.agentService = agentService;
	}

	/**
	 * Sets the location service.
	 * 
	 * @param locationService
	 *            the new location service
	 */
	public void setLocationService(ILocationService locationService) {

		this.locationService = locationService;
	}

	/**
	 * Gets the filter.
	 * 
	 * @return the filter
	 */
	

	/**
	 * Gets the coordinate values.
	 * 
	 * @return the coordinate values
	 */
	public String getCoordinateValues() {

		return coordinateValues;
	}


	public LocationHistory getFilter() {
		return filter;
	}

	public void setFilter(LocationHistory filter) {
		this.filter = filter;
	}

	/**
	 * Sets the device service.
	 * 
	 * @param deviceService
	 *            the new device service
	 */
	public void setDeviceService(IDeviceService deviceService) {

		this.deviceService = deviceService;
	}

	public String getBranchIdParma() {
		return branchIdParma;
	}

	public void setBranchIdParma(String branchIdParma) {
		this.branchIdParma = branchIdParma;
	}

	public String getDaterange() {
		return daterange;
	}

	public void setDaterange(String daterange) {
		this.daterange = daterange;
	}

	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}

	public Map<String, String> getFields() {
		fields.put("1", getText("date"));
		fields.put("2", getText("agentName"));

		/*
		 * if ((getIsMultiBranch().equalsIgnoreCase("1") &&
		 * (getIsParentBranch().equals("1") ||
		 * StringUtil.isEmpty(branchIdValue)))) { fields.put("4",
		 * getText("app.branch")); } else if (StringUtil.isEmpty(getBranchId()))
		 * { fields.put("3", getText("app.branch")); }
		 */

		if (getIsMultiBranch().equalsIgnoreCase("1")) {
			if (StringUtil.isEmpty(getBranchId())) {
				fields.put("4", getText("app.branch"));
			} else if (getIsParentBranch().equals("1")) {
				fields.put("4", getText("app.subBranch"));
			}

		} else if (StringUtil.isEmpty(getBranchId())) {

			fields.put("3", getText("app.branch"));
		}

		return fields;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}

	public String populateXLS() throws Exception {
		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getText("fieldStaffManagementReport") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(
				FileUtil.createFileInputStreamToZipFile(getText("fieldStaffManagementReport"), fileMap, ".xls"));
		return "xls";
	}

	@SuppressWarnings("unchecked")
	private InputStream getExportDataStream(String exportType) throws IOException {

		Long serialNumber = 0L;

		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		boolean flag = true;
		DateFormat filterDateFormat = new SimpleDateFormat(getGeneralDateFormat());
		DecimalFormat df = new DecimalFormat("0.000");
		DecimalFormat df1 = new DecimalFormat("0.00");

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("exportProcurementTitle"));
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

		HSSFRow row, titleRow, filterRowTitle, filterRow1, filterRow2, filterRow3, filterRow4, filterRow5;
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
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("fieldStaffManagementReportTitle")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);

		if (!StringUtil.isEmpty(branchIdParma)) {
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

		if (!StringUtil.isEmpty(subBranchIdParma) && !subBranchIdParma.equals("0")) {
			filter.setBranchId(subBranchIdParma);
		}

		super.filter = this.filter;

		Map data = isMailExport() ? readData() : readExportData();

		if (isMailExport()) {
			if (!StringUtil.isEmpty(filter.getAgentId()) ||!StringUtil.isEmpty(branchIdParma)){
			rowNum++;
			rowNum++;
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

			if (!ObjectUtil.isEmpty(geteDate())) {
				cell = filterRow2.createCell(2);
				cell.setCellValue(new HSSFRichTextString(filterDateFormat.format(geteDate())));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(filter.getAgentId())) {
				filterRow3 = sheet.createRow(rowNum++);
				cell = filterRow3.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("agentId")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow3.createCell(2);
				String agentName = agentService.findAgentNameByAgentId(String.valueOf(filter.getAgentId()));
				cell.setCellValue(new HSSFRichTextString(agentName));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
				if (StringUtil.isEmpty(branchIdValue)) {
					filterRow4 = sheet.createRow(rowNum++);
					cell = filterRow4.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("mainOrganization")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow4.createCell(2);
					cell.setCellValue(new HSSFRichTextString(
							getBranchesMap().get(getParentBranchMap().get(filter.getBranchId()))));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					filterRow5 = sheet.createRow(rowNum++);
					cell = filterRow5.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("subOrganization")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow5.createCell(2);
					cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(getSubBranchIdParma())));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

				} else {
					if (!StringUtil.isEmpty(getBranchesMap().get(getSubBranchIdParma()))) {
						filterRow4 = sheet.createRow(rowNum++);
						cell = filterRow4.createCell(1);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("subOrganization")));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						cell = filterRow4.createCell(2);
						cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(getSubBranchIdParma())));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);
					}
				}

			} else {
				if (!StringUtil.isEmpty(branchIdParma)) {
					filterRow5 = sheet.createRow(rowNum++);
					cell = filterRow5.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getText("organization")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow5.createCell(2);
					BranchMaster branch = clientService.findBranchMasterByBranchId(branchIdParma);
					cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(branch) ? branch.getName() : "")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);
				}

			}

		}

		rowNum++;

		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		String mainGridColumnHeaders = "";

		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("fieldStaffManagementHeaderBranch");
			} else if (!StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("fieldStaffManagementBranch");
			}
		} else if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {

			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("fieldStaffMobileUserManagementHeaderBranch");
			} else if (getBranchId().equalsIgnoreCase("organic")) {
				mainGridColumnHeaders = getLocaleProperty("OrganicMobileUserManagementExportHeaderagent");
			} else if (getBranchId().equalsIgnoreCase("bci")) {
				mainGridColumnHeaders = getLocaleProperty("BCIMobileUserManagementExportHeaderagent");
			}

		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("fieldStaffMobileUserManagementHeaderBranch");
			} else {
				mainGridColumnHeaders = getLocaleProperty("fieldStaffMobileUserManagementHeader");
			}
		}

		int mainGridIterator = 0;

		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {

			if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
			} else if (cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());
			}

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

		List<Object[]> dfata = (ArrayList) data.get(ROWS);
		if (ObjectUtil.isListEmpty(dfata))
			return null;

		for (Object[] datas : dfata) {
			row = sheet.createRow(rowNum++);
			colNum = 0;
			serialNumber++; 
			
			cell = row.createCell(colNum++);
			  cell.setCellValue( new HSSFRichTextString(String.valueOf(serialNumber) != null ? String.valueOf(serialNumber) : ""));
			  style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cell.setCellStyle(style3);
			
			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

				if (StringUtil.isEmpty(branchIdValue)) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(datas[1])))
									? getBranchesMap().get(getParentBranchMap().get(datas[1]))
									: getBranchesMap().get(datas[1])));
				}
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(datas[1])));
			} else {
				if (StringUtil.isEmpty(branchIdValue)) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							branchesMap.get(ObjectUtil.isEmpty(datas[1]) ? null : datas[1].toString())));
				}
			}
			ESESystem preferences = preferncesService.findPrefernceById("1");
			if (!StringUtil.isEmpty(preferences)) {
				DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(genDate.format(datas[2])));
			}

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(datas[3].toString()));
			String agentName = agentService.findAgentNameByAgentId(String.valueOf(datas[3]));
			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(agentName));

			 if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)){			
			
			if (datas[9] != null && datas[10] != null && !StringUtil.isEmpty(datas[9])
					&& !StringUtil.isEmpty(datas[10])) {
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(datas[9].toString()));
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(datas[10].toString()));

			} else {
				String lat = "";
				String lon = "";
				GeoApiContext context = new GeoApiContext().setEnterpriseCredentials("gme-sourcetrace",
						"UCvsZ3GFpyuwhClHOLePJJT4s0A=");
				try {
					LatLng latlng = new LatLng(Double.valueOf(String.valueOf(datas[5])),
							Double.valueOf(String.valueOf(datas[6])));
					GeocodingResult[] resultz = GeocodingApi.newRequest(context).latlng(latlng).await();
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(resultz[1].formattedAddress));
					lat = resultz[1].formattedAddress;
				} catch (Exception e) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString());

				}
				try {
					LatLng detsLatLng = new LatLng(Double.valueOf(String.valueOf(datas[7])),
							Double.valueOf(String.valueOf(datas[8])));
					GeocodingResult[] dresultz = GeocodingApi.newRequest(context).latlng(detsLatLng).await();
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(dresultz[1].formattedAddress));
					lon = dresultz[1].formattedAddress;
				} catch (Exception e) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString());
				}
				locationService.updateLocHistory(Long.valueOf(datas[0].toString()), lat, lon);
			}
			 }
			/*
			 * 
			 * row = sheet.createRow(rowNum++); colNum = 0;
			 * 
			 * serialNumber++; cell = row.createCell(colNum++);
			 * cell.setCellValue( new
			 * HSSFRichTextString(String.valueOf(serialNumber) != null ?
			 * String.valueOf(serialNumber) : ""));
			 * 
			 * if ((getIsMultiBranch().equalsIgnoreCase("1") &&
			 * (getIsParentBranch().equals("1") ||
			 * StringUtil.isEmpty(branchIdValue)))) { if
			 * (StringUtil.isEmpty(branchIdValue)) { cell =
			 * row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(
			 * !StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get
			 * (datas[1]))) ?
			 * getBranchesMap().get(getParentBranchMap().get(datas[1])) :
			 * getBranchesMap().get(datas[1]))); } cell =
			 * row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(getBranchesMap().get(datas[1])));
			 * 
			 * } else { if (StringUtil.isEmpty(branchIdValue)) { cell =
			 * row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(branchesMap.get(datas[1]))); } }
			 * 
			 * ESESystem preferences = preferncesService.findPrefernceById("1");
			 * if (!StringUtil.isEmpty(preferences)) { DateFormat genDate = new
			 * SimpleDateFormat(getGeneralDateFormat());
			 * 
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(genDate.format(datas[2]))); }
			 * 
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(datas[3].toString()));
			 * 
			 * String agentName =
			 * agentService.findAgentNameByAgentId(String.valueOf(datas[3]));
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(agentName));
			 * 
			 * Date startingDate =
			 * DateUtil.convertStringToDate(fieldStaffDateFormat.format(datas[2]
			 * ), "yyyy-MM-dd"); Date endingDate =
			 * DateUtil.convertStringToDate(fieldStaffDateFormat.format(datas[2]
			 * ), "yyyy-MM-dd"); Date dayStartDateTime =
			 * DateUtil.getDateWithoutTime(startingDate); Date dayEndDateTime =
			 * DateUtil.getDateWithLastMinuteofDay(endingDate);
			 * 
			 * List<LocationHistory> locationHistoryDataList = locationService
			 * .listDeviceLocationHistoryByAgentId(String.valueOf(datas[3]),
			 * dayStartDateTime, dayEndDateTime);
			 * 
			 * if (!ObjectUtil.isListEmpty(locationHistoryDataList)) {
			 * LocationHistory srcLocHistory = locationHistoryDataList.get(0);
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(srcLocHistory.getAddress()));
			 * 
			 * LocationHistory destLocHistory =
			 * locationHistoryDataList.get(locationHistoryDataList.size() - 1);
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(destLocHistory.getAddress()));
			 * 
			 * Double totalDist =
			 * locationHistoryDataList.stream().filter(loc->!ObjectUtil.isEmpty(
			 * loc) && !StringUtil.isEmpty(loc.getDistance())) .mapToDouble(loc
			 * -> Double.valueOf(loc.getDistance())).sum(); cell =
			 * row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(df.format(totalDist).toString() + " Km")); }
			 * else { cell = row.createCell(colNum++); cell.setCellValue("");
			 * 
			 * cell = row.createCell(colNum++); cell.setCellValue("");
			 * 
			 * cell = row.createCell(colNum++); cell.setCellValue("0 KM"); }
			 * 
			 */
			
		}
		 
		for (int i = 0; i <= colNum; i++) {
			sheet.autoSizeColumn(i);
		}

		int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		// picture.resize();
		String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
				: request.getSession().getId();
		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("fieldStaffManagementReport") + fileNameDateFormat.format(new Date())
				+ ".xls";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		wb.write(fileOut);
		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		fileOut.close();

		return stream;

	}

	private int getPicIndex(HSSFWorkbook wb) {

		int index = -1;

		byte[] picData = null;
		picData = clientService.findLogoByCode(Asset.APP_LOGO);

		if (picData != null)
			index = wb.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
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

	public static SimpleDateFormat getFilenamedateformat() {
		return fileNameDateFormat;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getTotalDist() {
		return totalDist;
	}

	public void setTotalDist(String totalDist) {
		this.totalDist = totalDist;
	}

	public String getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}
	
	public String getCoordinateStr() {
		return coordinateStr;
	}

	public void setCoordinateStr(String coordinateStr) {
		this.coordinateStr = coordinateStr;
	}
	

	private Double getGeoCodeDistance(Double lat, Double lon, Double destLat, Double destLon) {

		Double distance = 0.0;
		if (lat == destLat && lon == destLon) {
			return distance;
		} else {
			GeoApiContext context = new GeoApiContext().setEnterpriseCredentials("gme-sourcetrace",
					"UCvsZ3GFpyuwhClHOLePJJT4s0A=");
			DistanceMatrix matrix;
			try {
				matrix = DistanceMatrixApi.newRequest(context).origins(new LatLng(lat, lon))
						.destinations(new LatLng(destLat, destLon)).mode(TravelMode.WALKING).await();
				for (DistanceMatrixRow row : matrix.rows) {
					Double dist = (double) row.elements[0].distance.inMeters;
					if (dist > 0) {
						distance = (double) (dist / 1000);
						return distance;
					}
				}

			} catch (Exception e) {
				return distance;
			}

			return distance;
		}
	}

}
