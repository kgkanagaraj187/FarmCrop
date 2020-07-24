/*
 * TrainingCompletionReportAction.java
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
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

import com.ese.entity.txn.training.FarmerTraining;
import com.ese.entity.txn.training.Observations;
import com.ese.entity.txn.training.Topic;
import com.ese.entity.txn.training.TopicCategory;
import com.ese.entity.txn.training.TrainingMaterial;
import com.ese.entity.txn.training.TrainingMethod;
import com.ese.entity.txn.training.TrainingStatus;
import com.ese.entity.txn.training.TrainingStatusLocation;
import com.ese.entity.txn.training.TrainingTopic;
import com.ese.entity.util.ESESystem;
import com.ese.service.report.FarmerQueryMigration;
import com.sourcetrace.eses.dao.IReportDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.TransferInfo;
import com.sourcetrace.eses.service.IAgentService;
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
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

public class TrainingCompletionReportAction extends BaseReportAction implements IExporter {

	private static final long serialVersionUID = 5753871231037958377L;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

	private String agent;
	private String learningGroup;
	private String identityForGrid;
	private String imageindex;
	private String xlsFileName;
	private Map<String, String> fields = new HashMap<>();
	boolean completedTopic = false;
	private TrainingStatus filter;
	@Autowired
	private ITrainingService trainingService;
	@Autowired
	private IFarmerService farmerService;
	private IAgentService agentService;
	private ILocationService locationService;
	private IPreferencesService preferncesService;
	private InputStream fileInputStream;
	private IProductDistributionService productDistributionService;
	private String branchIdParma;
	private String daterange;
	private Set<Topic> topicList;
	private List<Map<String, String>> farmersList;
	private String remarks;
	private FarmerTraining farmerTraining;
	private String methodStr;
	private String materialStr;
	private String obsStr;
	private Set<TopicCategory> topicCategoryList;
	private String farmerCount;
	private String trainingTopicName;
	private String agentType;
	private String trainingCode;
	private String farmerFilter;
	private int farmerCnt;
	/**
	 * @see com.sourcetrace.esesw.view.BaseReportAction#list()
	 */
	public String list() {

		Calendar currentDate = Calendar.getInstance();
		Calendar cal = (Calendar) currentDate.clone();
		cal.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) - 1);
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		super.startDate = df.format(cal.getTime());
		super.endDate = df.format(currentDate.getTime());

		daterange = super.startDate + " - " + super.endDate;

		setFilter(new TrainingStatus());
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
	@SuppressWarnings("unchecked")
	public String data() throws Exception {

		setIdentityForGrid("trainingStatus");
		setFilter(new TrainingStatus());

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

		if (!StringUtil.isEmpty(agent)) {
			filter.setTransferInfo(new TransferInfo());
			filter.getTransferInfo().setAgentId(agent);
		}
		if (!StringUtil.isEmpty(learningGroup)) {
			filter.setLearningGroup(new Warehouse());
			filter.getLearningGroup().setCode(learningGroup);
		}
		if (!StringUtil.isEmpty(trainingCode)) {
			filter.setTrainingCode(trainingCode);
		}
		if (!StringUtil.isEmpty(farmerFilter)) {
			filter.setFarmerIds(farmerFilter);
		}
		
		super.filter = this.filter;
		Map data = readData();
		return sendJSONResponse(data);
	}

	public String detail() {
		filter = trainingService.findTrainingStatusById(Long.valueOf(id));
		String[] farmerIdArr;
		farmersList = new LinkedList<>();
		if (!ObjectUtil.isEmpty(filter)) {
			if (!StringUtil.isEmpty(filter.getFarmerIds())) {
				farmerIdArr = filter.getFarmerIds().split(",");
				if (farmerIdArr.length > 0) {
					for (String farmerId : farmerIdArr) {
						if(getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
							agentType=filter.getTransferInfo().getAgentType()!=null && !StringUtil.isEmpty(filter.getTransferInfo().getAgentType())?filter.getTransferInfo().getAgentType().trim():"02";
								if(agentType.equals("05") || agentType.equals("06")){
									//Agent agent=agentService.findAgentByProfileAndBranchId(filter.getTransferInfo().getAgentId(), getBranchId());
							        Agent ag;
							         if(!StringUtil.isEmpty(getBranchId())){
							         ag=agentService.findAgentByProfileAndBranchId(farmerId, getBranchId());
							         }
							         else{
							         ag=agentService.findAgentByProfileId(farmerId);
							         }
								//	ag=agentService.findAgentByProfileAndBranchId(farmerId, getBranchId());		
									Map<String, String> objMap = new HashMap<>();
									if (!ObjectUtil.isEmpty(ag)) {
										objMap.put("farmerCode",ag.getProfileId());
										objMap.put("farmerName", ag.getPersonalInfo().getFirstName()+" "+ag.getPersonalInfo().getLastName());
										objMap.put("villageName","");
										objMap.put("samithiName", agentType.equals("06")?"":ag.getProcurementCenter().getName());
										farmersList.add(objMap);
										
									}
								}else {
									Object[] values = farmerService.findFarmerCodeNameVillageSamithibyFarmerId(farmerId.trim());
									Map<String, String> objMap = new HashMap<>();
									if (values.length > 0) {
										objMap.put("farmerCode",!ObjectUtil.isEmpty(values[0])?values[0].toString():"");
										objMap.put("farmerName", !ObjectUtil.isEmpty(values[1])?values[1].toString():"");
										objMap.put("villageName",!ObjectUtil.isEmpty(values[2])? values[2].toString():"");
										objMap.put("samithiName", !ObjectUtil.isEmpty(values[3])?values[3].toString():"");
										farmersList.add(objMap);
									}
								}
							}
							else{
								agentType="02";
								Object[] values = farmerService.findFarmerCodeNameVillageSamithibyFarmerId(farmerId.trim());
								Map<String, String> objMap = new HashMap<>();
								if (values.length > 0) {
									objMap.put("farmerCode",!ObjectUtil.isEmpty(values[0])?values[0].toString():"");
									objMap.put("farmerName", !ObjectUtil.isEmpty(values[1])?values[1].toString():"");
									objMap.put("villageName",!ObjectUtil.isEmpty(values[2])? values[2].toString():"");
									objMap.put("samithiName", !ObjectUtil.isEmpty(values[3])?values[3].toString():"");
									farmersList.add(objMap);
								}
							}
					}
				}
			}
			
			String topicName="";
			String trainingTopicVal[]=filter.getTrainingCode().split(",");
			
			 for (int i = 0; i < trainingTopicVal.length; i++) {
		        	String val=trainingTopicVal[i].replaceAll("\\s+", "");
		       
		        FarmerTraining ft = trainingService.findFarmerTrainingByCode(val);
		        if(!ObjectUtil.isEmpty(ft)&&ft!=null){
		        topicName += ft.getTrainingTopic().getName() + ",";
		        }
		        }
			 
			  if(topicName.endsWith(",")){
				  topicName = topicName.substring(0, topicName.length() - 1);
			  }
			  
			  trainingTopicName = topicName;
			farmerCount=!ObjectUtil.isListEmpty(farmersList)?String.valueOf(farmersList.size()):"0";
			materialStr=filter.getTrainingMaterials().stream().map(TrainingMaterial::getName).collect(Collectors.joining(","));
			methodStr=filter.getTrainingMethods().stream().map(TrainingMethod::getName).collect(Collectors.joining(","));
			obsStr=filter.getObservations().stream().map(Observations::getName).collect(Collectors.joining(","));
			topicList = filter.getTopics();
			topicCategoryList = filter.getTopicCategory();

			farmerTraining=trainingService.findFarmerTrainingByCode(filter.getTrainingCode());
		}

		return DETAIL;
	}

	/**
	 * @see com.sourcetrace.esesw.view.BaseReportAction#toJSON(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		if (identityForGrid == "trainingStatus") {
			TrainingStatus trainingStatus = (TrainingStatus) obj;
			/*
			 * if (StringUtil.isEmpty(branchIdValue)) {
			 * rows.add(branchesMap.get(trainingStatus.getBranchId())); }
			 */

			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(!StringUtil
							.isEmpty(getBranchesMap().get(getParentBranchMap().get(trainingStatus.getBranchId())))
									? getBranchesMap().get(getParentBranchMap().get(trainingStatus.getBranchId()))
									: getBranchesMap().get(trainingStatus.getBranchId()));
				}
				rows.add(getBranchesMap().get(trainingStatus.getBranchId()));
			} else {
				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(branchesMap.get(trainingStatus.getBranchId()));
				}
			}

			ESESystem preferences = preferncesService.findPrefernceById("1");
			if (!StringUtil.isEmpty(preferences)) {
				DateFormat genDate = new SimpleDateFormat(
						preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
				rows.add(genDate.format(trainingStatus.getTrainingDate()));
			}
			
			rows.add(trainingStatus.getTrainingCode());
			
			FarmerTraining ft = trainingService.findFarmerTrainingByCode(trainingStatus.getTrainingCode());
			
			rows.add(ft.getTrainingTopic().getName());
		/*	rows.add((!ObjectUtil.isEmpty(trainingStatus.getFarmerTraining())
					&& !StringUtil.isEmpty(trainingStatus.getFarmerTraining().getCode()))
							? trainingStatus.getFarmerTraining().getCode() : "");*/
			rows.add(!ObjectUtil.isEmpty(trainingStatus.getTransferInfo())
					? (!StringUtil.isEmpty(trainingStatus.getTransferInfo().getAgentName())
							? trainingStatus.getTransferInfo().getAgentName() : "")
					: "");
			String[] farmerIdArr;
			if (!StringUtil.isEmpty(trainingStatus.getFarmerIds())) {
			farmerIdArr = trainingStatus.getFarmerIds().split(",");
			farmerCnt= farmerIdArr.length;
			rows.add(farmerCnt);
			}
			else{rows.add("0");}
			//Agent agent=agentService.findAgentByAgentId(trainingStatus.getTransferInfo().getAgentId());
			if(getCurrentTenantId().equals(ESESystem.PRATIBHA_TENANT_ID)){	
				if(trainingStatus.getTransferInfo().getAgentType()!=null && !StringUtil.isEmpty(trainingStatus.getTransferInfo().getAgentType()) && trainingStatus.getTransferInfo().getAgentType().equals("06")){
					rows.add(getLocaleProperty("agent01")+","+getLocaleProperty("agent02"));
				}
				else if(trainingStatus.getTransferInfo().getAgentType()!=null && !StringUtil.isEmpty(trainingStatus.getTransferInfo().getAgentType()) && trainingStatus.getTransferInfo().getAgentType().equals("05")){
					rows.add(getLocaleProperty("agent02"));
				}
				else{
					rows.add(getLocaleProperty("farmer"));
				}
			}
			rows.add(!ObjectUtil.isEmpty(trainingStatus.getLearningGroup())
					? (!StringUtil.isEmpty(trainingStatus.getLearningGroup().getName())
							? trainingStatus.getLearningGroup().getName() : "")
					: "");
			/*
			 * rows.add(!StringUtil.isEmpty(trainingStatus.getReceiptNo()) ?
			 * trainingStatus .getReceiptNo() : "");
			 */
			//rows.add(trainingStatus.getFarmerAttended());
			rows.add(!StringUtil.isEmpty(trainingStatus.getRemarks()) ? trainingStatus.getRemarks() : "");
			String[] locVal = new String[0];
			String trainingStatusImgId = "";
			List<TrainingStatusLocation> trainingStatusLocationList = trainingService
					.listTrainingStatusLoctaion(trainingStatus.getId());
			if (!ObjectUtil.isListEmpty(trainingStatusLocationList)) {
				for (TrainingStatusLocation trainingStatusLocation : trainingStatusLocationList) {
					if (trainingStatusLocation.getPhoto() != null && trainingStatusLocation.getPhoto().length > 0) {
						trainingStatusImgId += String.valueOf(trainingStatusLocation.getId() + "/"
								+ trainingStatusLocation.getUpdateTime() + "/" + trainingStatusLocation.getLatitude()
								+ "/" + trainingStatusLocation.getLongitude() + ",");
					}

				}

				if (!StringUtil.isEmpty(trainingStatusImgId)) {
					String tempVal = trainingStatusImgId.substring(0, trainingStatusImgId.length() - 1);
					rows.add("<button class=\"fa fa-picture-o\"\"aria-hidden=\"true\"\" onclick=\"popupWindow("
							+ trainingStatus.getId() + ",'" + tempVal + "')\"></button>");
				} else {
					rows.add("<button class='no-imgIcn'></button>");
				}
				if (!StringUtil.isEmpty(trainingStatusImgId)) {
					String tempVal = trainingStatusImgId.substring(0, trainingStatusImgId.length() - 1);
					String[] locArray = tempVal.split(",");
					String loc = locArray[0];
					locVal = loc.split("/");

					if ((!StringUtil.isEmpty(locVal[2]) && !StringUtil.isEmpty(locVal[3]))) {
						rows.add("<button class='faMap' title='" + getText("farm.map.available.title")
								+ "' onclick='showFarmMap(\"" + (!StringUtil.isEmpty(locVal[2]) ? locVal[2] : "0")
								+ "\",\"" + (!StringUtil.isEmpty(locVal[3]) ? locVal[3] : "0") + "\")'></button>");
					} else {
						// No Latlon
						rows.add("<button class='no-latLonIcn' title='" + getText("farm.map.unavailable.title")
								+ "'></button>");
					}

				}

			} else
				rows.add("<button class='no-imgIcn'></button>");

			jsonObject.put("id", trainingStatus.getId());
			jsonObject.put("cell", rows);

		} else {

			if (obj instanceof Topic) {
				Topic topic = (Topic) obj;
				rows.add(topic.getCode());
				rows.add(topic.getPrinciple());
				rows.add(topic.getDes());
				/*
				 * if (completedTopic) rows.add(
				 * "<button type='button' class='tickIcon'"); else rows.add(
				 * "<button type='button' class='crossIcon'");
				 */
				jsonObject.put("id", topic.getId());
				jsonObject.put("cell", rows);
			}

		}
		return jsonObject;
	}

	/**
	 * Sub grid detail.
	 * 
	 * @return the jSON object
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings("unchecked")
	public JSONObject subGridDetail() throws Exception {

		setId(id);
		TrainingStatus trainingStatus = trainingService.findTrainingStatusById(Long.valueOf(id));
		// List<Topic> farmerTrainingTopicList =
		// trainingService.listTopicByFarmerSelection(trainingStatus.getFarmerTraining().getId())
		Set<Topic> farmerTrainingTopicList = trainingStatus.getFarmerTraining().getTopics();
		Set<Topic> topicList = trainingStatus.getTopics();
		// To List OrderDetails Based on Id
		JSONObject gridData = new JSONObject();
		JSONArray rows = new JSONArray();
		// Send to grid to display records
		/*
		 * if (farmerTrainingTopicList != null) { for (Topic record :
		 * farmerTrainingTopicList) { completedTopic = false; if
		 * (topicList.contains(record)) completedTopic = true;
		 * rows.add(toJSON(record)); } }
		 */
		if (!ObjectUtil.isListEmpty(topicList)) {
			for (Topic record : topicList) {
				rows.add(toJSON(record));
			}
		}
		gridData.put(PAGE, getPage());
		totalRecords = farmerTrainingTopicList.size();
		gridData.put(IReportDAO.START_INDEX, getStartIndex());
		gridData.put(IReportDAO.LIMIT, farmerTrainingTopicList.size());
		gridData.put(ROWS, rows);
		PrintWriter out = response.getWriter();
		out.println(gridData.toString());
		return null;

	}

	/**
	 * Gets the agent list.
	 * 
	 * @return the agent list
	 */
	/*
	 * public Map<String, String> getAgentList() {
	 * 
	 * Map<String, String> agentList = new LinkedHashMap<String, String>();
	 * List<Agent> agents =
	 * agentService.listAgentByAgentType(AgentType.FIELD_STAFF); if
	 * (!ObjectUtil.isEmpty(agents)) { for (Agent agent : agents) {
	 * agentList.put(agent.getProfileId(),
	 * agent.getPersonalInfo().getAgentName()); } }
	 * 
	 * return agentList; }
	 */

	public Map<String, String> getAgentList() {
		Map<String, String> agentMap = new LinkedHashMap<>();
		List<Object[]> agentList = agentService.listOfAgentByTraining();
		if (!ObjectUtil.isEmpty(agentList)) {

			agentMap = agentList.stream()
					.collect(Collectors.toMap(obj -> (String.valueOf(obj[1])), obj -> (String.valueOf(obj[0]))));

		}
		return agentMap;
	}

	/**
	 * Gets the learning group list.
	 * 
	 * @return the learning group list
	 */
	/*
	 * public Map<String, String> getLearningGroupList() {
	 * 
	 * Map<String, String> learningGroupList = new LinkedHashMap<String,
	 * String>(); List<Warehouse> learningGroup =
	 * locationService.listSamithiesBasedOnType(); if
	 * (!ObjectUtil.isEmpty(learningGroup)) { for (Warehouse warehouse :
	 * learningGroup) { learningGroupList.put(warehouse.getCode(),
	 * warehouse.getName());
	 * 
	 * } } return learningGroupList; }
	 */

	public Map<String, String> getLearningGroupList() {
		Map<String, String> groupMap = new LinkedHashMap<>();
		List<Object[]> groupList = agentService.listOfLearningGroupByTraining();
		if (!ObjectUtil.isEmpty(groupList)) {

			groupMap = groupList.stream()
					.collect(Collectors.toMap(obj -> (String.valueOf(obj[0])), obj -> (String.valueOf(obj[1]))));

		}
		return groupMap;
	}

	/**
	 * Detail image session.
	 * 
	 * @return the string
	 */
	public String detailImageSession() {

		try {
			response.setContentType("text/html");
			response.getWriter().write("1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Detail image.
	 * 
	 * @return the string
	 */
	public String detailImage() {

		try {
			setId(id);
			TrainingStatusLocation imageDetail = trainingService.loadTrainingStatusLocationImage(Long.valueOf(id));
			byte[] image = imageDetail.getPhoto();
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

	public String detailImageData() {
		return "imageData";
	}

	/**
	 * Populate xls.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String populateXLS() throws Exception {

		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getText("TrainingCompletionReport") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(
				FileUtil.createFileInputStreamToZipFile(getText("trainingCompletionReportFileName"), fileMap, ".xls"));
		return "xls";
	}

	/**
	 * Gets the excel export input stream.
	 * 
	 * @param manual
	 *            the manual
	 * @return the excel export input stream
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("unchecked")
	public InputStream getExportDataStream(String exportType) throws IOException {

		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		setsDate(IExporter.EXPORT_DAILY.equalsIgnoreCase(exportType) ? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("trainingCompletionReportTitle"));
		HSSFPatriarch drawing = sheet.createDrawingPatriarch();
		
		
		HSSFCellStyle titleStyle = wb.createCellStyle();
		HSSFCellStyle headerStyle = wb.createCellStyle();
		HSSFCellStyle dataStyle = wb.createCellStyle();
		HSSFCellStyle subGridStyle = wb.createCellStyle();
		subGridStyle.setWrapText(true);

		HSSFFont titleFont = wb.createFont();
		titleFont.setFontHeightInPoints((short) 22);
		titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		HSSFFont headerFont = wb.createFont();
		headerFont.setFontHeightInPoints((short) 12);
		headerFont.setBoldweight((short) 12);
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		HSSFFont dataFont = wb.createFont();
		dataFont.setFontHeightInPoints((short) 10);

		HSSFFont subGridFont = wb.createFont();
		subGridFont.setFontHeightInPoints((short) 10);
		
		HSSFCellStyle style1 = wb.createCellStyle();

		HSSFRow row, titleRow, filterRowTitle, filterRow;
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

		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2)); // Logo
																							// Cells
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, titleRow1, titleRow2)); // Title
																							// Cells
		
		HSSFFont filterFont = wb.createFont();
		filterFont.setFontHeightInPoints((short) 12);
		
		HSSFCellStyle filterStyle = wb.createCellStyle();

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(2);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("trainingCompletionReportTitle")));
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cell.setCellStyle(titleStyle);
		titleStyle.setFont(titleFont);

		rowNum++;
		rowNum++;

		setFilter(new TrainingStatus());
		if (!StringUtil.isEmpty(agent)) {
			filter.setTransferInfo(new TransferInfo());
			filter.getTransferInfo().setAgentId(agent);
		}
		if (!StringUtil.isEmpty(learningGroup)) {
			filter.setLearningGroup(new Warehouse());
			filter.getLearningGroup().setCode(learningGroup);
		}
		if (!StringUtil.isEmpty(trainingCode)) {
			filter.setTrainingCode(trainingCode);
		}
		if (!StringUtil.isEmpty(farmerFilter)) {
			filter.setFarmerIds(farmerFilter);
		}
		super.filter = this.filter;

		if (isMailExport() && super.filter != null) {
			//if (!ObjectUtil.isEmpty(filter.getTransferInfo()) || !ObjectUtil.isEmpty(filter.getTrainingCode()) ||!ObjectUtil.isEmpty(filter.getFarmerIds()) || !StringUtil.isEmpty(branchIdParma)){
			filterRowTitle = sheet.createRow(rowNum++);
			cell = filterRowTitle.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("exportFilter")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);
			//cell.setCellStyle(headerStyle);
			//headerStyle.setFont(headerFont);
			//}
			filterRow = sheet.createRow(rowNum++);
			cell = filterRow.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("StartingDate")));
			cell.setCellStyle(dataStyle);
			dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			dataStyle.setFont(dataFont);

			cell = filterRow.createCell(2);
			String startDateVal = String.valueOf(super.startDate);
			cell.setCellValue(new HSSFRichTextString(startDateVal));
			cell.setCellStyle(dataStyle);
			dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			dataStyle.setFont(dataFont);

			filterRow = sheet.createRow(rowNum++);
			cell = filterRow.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("EndingDate")));
			cell.setCellStyle(dataStyle);
			dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			dataStyle.setFont(dataFont);

			cell = filterRow.createCell(2);
			String endDateVal = String.valueOf(super.endDate);
			cell.setCellValue(new HSSFRichTextString(endDateVal));
			cell.setCellStyle(dataStyle);
			dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			dataStyle.setFont(dataFont);

			if (!ObjectUtil.isEmpty(filter.getTransferInfo())
					&& !StringUtil.isEmpty(filter.getTransferInfo().getAgentId())) {
				filterRow = sheet.createRow(rowNum++);
				cell = filterRow.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("agentLabel")));
				cell.setCellStyle(dataStyle);
				dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				dataStyle.setFont(dataFont);
				cell = filterRow.createCell(2);
				Agent agent = agentService.findAgentByProfileId(filter.getTransferInfo().getAgentId());
				cell.setCellValue(
						new HSSFRichTextString(agent.getPersonalInfo().getAgentName() + "-" + agent.getProfileId()));
				cell.setCellStyle(dataStyle);
				dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				dataStyle.setFont(dataFont);
			}

			if (!ObjectUtil.isEmpty(filter.getLearningGroup())
					&& !StringUtil.isEmpty(filter.getLearningGroup().getCode())) {
				filterRow = sheet.createRow(rowNum++);
				cell = filterRow.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("warehouseLabel")));
				cell.setCellStyle(dataStyle);
				dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				dataStyle.setFont(dataFont);
				cell = filterRow.createCell(2);
				filter.setLearningGroup(locationService.findWarehouseByCode(filter.getLearningGroup().getCode()));
				cell.setCellValue(new HSSFRichTextString(
						filter.getLearningGroup().getName() + "-" + filter.getLearningGroup().getCode()));
				cell.setCellStyle(dataStyle);
				dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				dataStyle.setFont(dataFont);
			}
			if (!ObjectUtil.isEmpty(filter.getTrainingCode())) {
				filterRow = sheet.createRow(rowNum++);
				cell = filterRow.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("trainingCode")));
				cell.setCellStyle(dataStyle);
				dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				dataStyle.setFont(dataFont);
				cell = filterRow.createCell(2);
				filter.setTrainingCode(filter.getTrainingCode());
				cell.setCellValue(new HSSFRichTextString(
						filter.getTrainingCode()));
				cell.setCellStyle(dataStyle);
				dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				dataStyle.setFont(dataFont);
			}
			if (!ObjectUtil.isEmpty(filter.getFarmerIds())) {
				filterRow = sheet.createRow(rowNum++);
				cell = filterRow.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmerId")));
				cell.setCellStyle(dataStyle);
				dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				dataStyle.setFont(dataFont);
				cell = filterRow.createCell(2);
				Farmer farmer =farmerService.findFarmerByFarmerId(filter.getFarmerIds());
				cell.setCellValue(new HSSFRichTextString(
						farmer.getFirstName()));
				cell.setCellStyle(dataStyle);
				dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				dataStyle.setFont(dataFont);
			}
			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
				   if (StringUtil.isEmpty(branchIdValue)) {
					   filterRow = sheet.createRow(rowNum++);
				    cell = filterRow.createCell(1);
				    cell.setCellValue(new HSSFRichTextString(getLocaleProperty("mainOrganization")));
				    dataFont.setBoldweight((short) 12);
				    dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				    dataStyle.setFont(dataFont);
				    cell.setCellStyle(dataStyle);

				    cell = filterRow.createCell(2);
				    //cell.setCellValue(new HSSFRichTextString((branchesMap.get(filter.getBranchId()))));
				    cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(getParentBranchMap().get(filter.getBranchId()))));
				    dataFont.setBoldweight((short) 12);
				    dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				    dataStyle.setFont(dataFont);
				    cell.setCellStyle(dataStyle);
				  
				    filterRow = sheet.createRow(rowNum++);
				    cell = filterRow.createCell(1);
				    cell.setCellValue(new HSSFRichTextString(getLocaleProperty("subOrganization")));
				    dataFont.setBoldweight((short) 12);
				    dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				    dataStyle.setFont(dataFont);
				    cell.setCellStyle(dataStyle);

				    cell = filterRow.createCell(2);
				    cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(getSubBranchIdParma())));
				    dataFont.setBoldweight((short) 12);
				    dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				    dataStyle.setFont(dataFont);
				    cell.setCellStyle(dataStyle);
				    
				   } else{
					   if(!StringUtil.isEmpty(getBranchesMap().get(getSubBranchIdParma()))){
						   filterRow = sheet.createRow(rowNum++);
					    cell = filterRow.createCell(1);
					    cell.setCellValue(new HSSFRichTextString(getLocaleProperty("subOrganization")));
					    dataFont.setBoldweight((short) 12);
					    dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					    dataStyle.setFont(dataFont);
					    cell.setCellStyle(dataStyle);

					    cell = filterRow.createCell(2);
					    cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(getSubBranchIdParma())));
					    dataFont.setBoldweight((short) 12);
					    dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					    dataStyle.setFont(dataFont);
					    cell.setCellStyle(dataStyle);
					   }
				   }

				   }else{
					   if(!StringUtil.isEmpty(branchIdParma)){
						   filterRow = sheet.createRow(rowNum++);
					    cell = filterRow.createCell(1);
					    cell.setCellValue(new HSSFRichTextString(getText("organization")));
					    dataFont.setBoldweight((short) 12);
					    dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					    dataStyle.setFont(dataFont);
					    cell.setCellStyle(dataStyle);

					    cell = filterRow.createCell(2);
					    BranchMaster branch = clientService.findBranchMasterByBranchId(branchIdParma);
					    cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(branch) ? branch.getName() : "" )));
					    //cell.setCellValue(new HSSFRichTextString(branchIdParma));
					    dataFont.setBoldweight((short) 12);
					    dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					    dataStyle.setFont(dataFont);
					    cell.setCellStyle(dataStyle);
					   }
				   
				   }

			++rowNum;
		}

		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		String mainGridColumnHeaders = "";
		
		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("trainingcompletionHeaderBranch");
			} else if (!StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("trainingcompletionBranch");
			}
		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("trainingcompletionStatusReportColumnHeadersBranch");
			} else {
				mainGridColumnHeaders = getLocaleProperty("trainingcompletionStatusReportColumnHeaders");
			}
		}
		
		if(getCurrentTenantId().equalsIgnoreCase("pratibha")){
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("StatusReportExportHeader");
			} 
			else if(getBranchId().equalsIgnoreCase("organic")){
				mainGridColumnHeaders = getLocaleProperty("OrganiStatusReportExportHeader");
			}else if(getBranchId().equalsIgnoreCase("bci")){
				mainGridColumnHeaders = getLocaleProperty("BCIStatusReportExportHeader");
			}
			
		}
		
		int mainGridIterator = 0;

		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {
			if (StringUtil.isEmpty(branchIdValue)) {
				cell = mainGridRowHead.createCell(mainGridIterator);
				cell.setCellValue(new HSSFRichTextString(cellHeader));
				headerStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
				headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cell.setCellStyle(headerStyle);
				headerStyle.setFont(headerFont);
				sheet.setColumnWidth(mainGridIterator, (15 * 450));
				mainGridIterator++;
			} else {
				if (!cellHeader.equalsIgnoreCase(getText("app.branch"))) {
					cell = mainGridRowHead.createCell(mainGridIterator);
					cell.setCellValue(new HSSFRichTextString(cellHeader));
					headerStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
					headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					cell.setCellStyle(headerStyle);
					headerStyle.setFont(headerFont);
					sheet.setColumnWidth(mainGridIterator, (15 * 450));
					mainGridIterator++;
				}
			}
		}

		Map data = isMailExport() ? readData() : readExportData();
		List<TrainingStatus> mainGridRows = (List<TrainingStatus>) data.get(ROWS);
		
		Long serialNumber = 0L;
		
		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;
		for (TrainingStatus trainingStatus : mainGridRows) {

			ESESystem preferences = preferncesService.findPrefernceById("1");

			DateFormat genDate = new SimpleDateFormat(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));

			row = sheet.createRow(rowNum++);
			colNum = 0;
			
			
			serialNumber++;		
			cell = row.createCell(colNum++);		
			//cell.setCellValue(new HSSFRichTextString(String.valueOf(serialNumber)!=null ? String.valueOf(serialNumber) : ""));		

					style1.setAlignment(CellStyle.ALIGN_CENTER);
					cell.setCellStyle(style1);
					cell.setCellValue(serialNumber);		
					

			/*if (StringUtil.isEmpty(branchIdValue)) {
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString((!StringUtil.isEmpty(trainingStatus.getBranchId())
						? branchesMap.get(trainingStatus.getBranchId()) : getText("NA"))));
			}*/

			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
				if (StringUtil.isEmpty(branchIdValue)) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(trainingStatus.getBranchId())))
							? getBranchesMap().get(getParentBranchMap().get(trainingStatus.getBranchId()))
							: getBranchesMap().get(trainingStatus.getBranchId())));
				}
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(trainingStatus.getBranchId())));

			} else {
				if (StringUtil.isEmpty(branchIdValue)) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(branchesMap.get(trainingStatus.getBranchId())));
				}
			}
			
			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(genDate.format(trainingStatus.getTrainingDate())));

			cell = row.createCell(colNum++);
			cell.setCellValue(
					new HSSFRichTextString(!StringUtil.isEmpty(trainingStatus.getTrainingCode())
									? trainingStatus.getTrainingCode() : getLocaleProperty("N/A")));
			
			FarmerTraining ft = trainingService.findFarmerTrainingByCode(trainingStatus.getTrainingCode());
			cell = row.createCell(colNum++);
			cell.setCellValue(
					new HSSFRichTextString(!StringUtil.isEmpty(ft.getTrainingTopic().getName())
									? ft.getTrainingTopic().getName() : getLocaleProperty("N/A")));

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(trainingStatus.getTransferInfo())
					? (!StringUtil.isEmpty(trainingStatus.getTransferInfo().getAgentName())
							? trainingStatus.getTransferInfo().getAgentName() : getLocaleProperty("N/A"))
					: getLocaleProperty("N/A")));

			
			
			
			String[] farmerIdArr;
			if (!StringUtil.isEmpty(trainingStatus.getFarmerIds())) {
			farmerIdArr = trainingStatus.getFarmerIds().split(",");
			farmerCnt= farmerIdArr.length;
			//rows.add(farmerCnt);
			cell = row.createCell(colNum++);
			cell.setCellValue(
					new HSSFRichTextString(!StringUtil.isEmpty(farmerCnt)
									? String.valueOf(farmerCnt) : getLocaleProperty("N/A")));
			
			}
			cell = row.createCell(colNum++);
			cell.setCellValue(
					new HSSFRichTextString(!ObjectUtil.isEmpty(trainingStatus.getLearningGroup())
							? (!StringUtil.isEmpty(trainingStatus.getLearningGroup().getName())
									? trainingStatus.getLearningGroup().getName() : getLocaleProperty("N/A"))
							: getLocaleProperty("N/A")));

			/*cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(String.valueOf(trainingStatus.getFarmerAttended())));*/
			if(getCurrentTenantId().equals(ESESystem.PRATIBHA_TENANT_ID)){	
				if(trainingStatus.getTransferInfo().getAgentType()!=null && !StringUtil.isEmpty(trainingStatus.getTransferInfo().getAgentType()) && trainingStatus.getTransferInfo().getAgentType().equals("06")){
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("agent01")+","+getLocaleProperty("agent02")));
				}
				else if(trainingStatus.getTransferInfo().getAgentType()!=null && !StringUtil.isEmpty(trainingStatus.getTransferInfo().getAgentType()) && trainingStatus.getTransferInfo().getAgentType().equals("05")){
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("agent02")));
				}
				else{
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmer")));
				}
			}
			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(trainingStatus.getRemarks())
					? trainingStatus.getRemarks() : getLocaleProperty("N/A")));

			/*if (!ObjectUtil.isListEmpty(trainingStatus.getTopics())) {
				HSSFRow subGridHeaderRow = sheet.createRow(rowNum++);
				String subGridColumnHeader = getLocaleProperty("trainingStatusReportSubColumnHeaders");
				int subGridIterator = 1;

				for (String cellHeader : subGridColumnHeader.split("\\,")) {
					cell = subGridHeaderRow.createCell(subGridIterator);
					cell.setCellValue(new HSSFRichTextString(cellHeader));
					subGridStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
					subGridStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					cell.setCellStyle(subGridStyle);
					subGridFont.setBoldweight((short) 10);
					subGridFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					subGridStyle.setFont(subGridFont);
					subGridIterator++;
				}

				for (Topic topic : trainingStatus.getTopics()) {
					row = sheet.createRow(rowNum++);
					colNum = 1;

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(topic.getCode()));

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(topic.getPrinciple()));

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(topic.getDes()));

				}
			}*/
		}

		/*for (int i = 0; i <= colNum; i++) {		
			sheet.autoSizeColumn(i);		
		}*/
		
		int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		/*picture.resize();*/
		String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
				: request.getSession().getId();
		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("trainingCompletionReportFileName") + fileNameDateFormat.format(new Date())
				+ ".xls";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		wb.write(fileOut);
		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		fileOut.close();

		return stream;
	}

	/**
	 * Gets the pic index.
	 * 
	 * @param wb
	 *            the wb
	 * @return the pic index
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public int getPicIndex(HSSFWorkbook wb) throws IOException {

		int index = -1;

		byte[] picData = null;
		picData = clientService.findLogoByCode(Asset.APP_LOGO);

		if (picData != null)
			index = wb.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
	}

	/**
	 * Sets the filter.
	 * 
	 * @param filter
	 *            the new filter
	 */
	public void setFilter(TrainingStatus filter) {

		super.filter = filter;
		this.filter = filter;
	}

	/**
	 * Gets the filter.
	 * 
	 * @return the filter
	 */
	public TrainingStatus getFilter() {

		return filter;
	}

	/**
	 * Gets the training service.
	 * 
	 * @return the training service
	 */
	public ITrainingService getTrainingService() {

		return trainingService;
	}

	/**
	 * Sets the training service.
	 * 
	 * @param trainingService
	 *            the new training service
	 */
	public void setTrainingService(ITrainingService trainingService) {

		this.trainingService = trainingService;
	}

	/**
	 * Gets the identity for grid.
	 * 
	 * @return the identity for grid
	 */
	public String getIdentityForGrid() {

		return identityForGrid;
	}

	/**
	 * Sets the identity for grid.
	 * 
	 * @param identityForGrid
	 *            the new identity for grid
	 */
	public void setIdentityForGrid(String identityForGrid) {

		this.identityForGrid = identityForGrid;
	}

	/**
	 * Gets the agent service.
	 * 
	 * @return the agent service
	 */
	public IAgentService getAgentService() {

		return agentService;
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
	 * Gets the location service.
	 * 
	 * @return the location service
	 */
	public ILocationService getLocationService() {

		return locationService;
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
	 * Gets the agent.
	 * 
	 * @return the agent
	 */
	public String getAgent() {

		return agent;
	}

	/**
	 * Sets the agent.
	 * 
	 * @param agent
	 *            the new agent
	 */
	public void setAgent(String agent) {

		this.agent = agent;
	}

	/**
	 * Gets the learning group.
	 * 
	 * @return the learning group
	 */
	public String getLearningGroup() {

		return learningGroup;
	}

	/**
	 * Sets the learning group.
	 * 
	 * @param learningGroup
	 *            the new learning group
	 */
	public void setLearningGroup(String learningGroup) {

		this.learningGroup = learningGroup;
	}

	public IProductDistributionService getProductDistributionService() {

		return productDistributionService;
	}

	public void setProductDistributionService(IProductDistributionService productDistributionService) {

		this.productDistributionService = productDistributionService;
	}

	/**
	 * Gets the imageindex.
	 * 
	 * @return the imageindex
	 */
	public String getImageindex() {

		return imageindex;
	}

	/**
	 * Sets the imageindex.
	 * 
	 * @param imageindex
	 *            the new imageindex
	 */
	public void setImageindex(String imageindex) {

		this.imageindex = imageindex;
	}

	/**
	 * Checks if is completed topic.
	 * 
	 * @return true, if is completed topic
	 */
	public boolean isCompletedTopic() {

		return completedTopic;
	}

	/**
	 * Sets the completed topic.
	 * 
	 * @param completedTopic
	 *            the new completed topic
	 */
	public void setCompletedTopic(boolean completedTopic) {

		this.completedTopic = completedTopic;
	}

	/**
	 * Sets the file input stream.
	 * 
	 * @param fileInputStream
	 *            the new file input stream
	 */
	public void setFileInputStream(InputStream fileInputStream) {

		this.fileInputStream = fileInputStream;
	}

	/**
	 * Gets the file input stream.
	 * 
	 * @return the file input stream
	 */
	public InputStream getFileInputStream() {

		return fileInputStream;
	}

	/**
	 * Gets the xls file name.
	 * 
	 * @return the xls file name
	 */
	public String getXlsFileName() {

		return xlsFileName;
	}

	/**
	 * Sets the xls file name.
	 * 
	 * @param xlsFileName
	 *            the new xls file name
	 */
	public void setXlsFileName(String xlsFileName) {

		this.xlsFileName = xlsFileName;
	}

	public String getBranchIdParma() {
		return branchIdParma;
	}

	public void setBranchIdParma(String branchIdParma) {
		this.branchIdParma = branchIdParma;
	}

	public String getCustomContentType() {
		return "image/jpeg";
	}

	public String getCustomContentDisposition() {
		return "anyname.jpg";
	}

	public byte[] getCustomImageInBytes() {
		setId(id);
		TrainingStatusLocation imageDetail = trainingService.loadTrainingStatusLocationImage(Long.valueOf(id));
		byte[] image = null;
		if (!ObjectUtil.isEmpty(imageDetail)) {
			image = imageDetail.getPhoto();
		}
		return image;
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
		/**
		 * fields.add(getText("trainingDate"));
		 * fields.add(getText("agentName"));
		 * fields.add(getText("learningGroup")); if
		 * (ObjectUtil.isEmpty(getBranchId())) {
		 * fields.add(getText("app.branch")); }
		 */

		fields.put("1", getText("trainingDate"));
		fields.put("2", getLocaleProperty("agentName"));
		fields.put("3", getLocaleProperty("samithiName"));
		/*
		 * if ((getIsMultiBranch().equalsIgnoreCase("1") &&
		 * (getIsParentBranch().equals("1")||StringUtil.isEmpty(branchIdValue)))
		 * ) { fields.put("5", getText("app.branch")); } else if
		 * (StringUtil.isEmpty(getBranchId())) { fields.put("4",
		 * getText("app.branch")); }
		 */

		if (getIsMultiBranch().equalsIgnoreCase("1")) {
			if (StringUtil.isEmpty(getBranchId())) {
				fields.put("5", getText("app.branch"));
			} else if (getIsParentBranch().equals("1")) {
				fields.put("5", getText("app.subBranch"));
			}

		} else if (StringUtil.isEmpty(getBranchId())) {

			fields.put("4", getText("app.branch"));
		}

		return fields;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}

	public Set<Topic> getTopicList() {
		return topicList;
	}

	public void setTopicList(Set<Topic> topicList) {
		this.topicList = topicList;
	}

	public List<Map<String, String>> getFarmersList() {
		return farmersList;
	}

	public void setFarmersList(List<Map<String, String>> farmersList) {
		this.farmersList = farmersList;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public FarmerTraining getFarmerTraining() {
		return farmerTraining;
	}

	public void setFarmerTraining(FarmerTraining farmerTraining) {
		this.farmerTraining = farmerTraining;
	}

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	public String getMethodStr() {
		return methodStr;
	}

	public void setMethodStr(String methodStr) {
		this.methodStr = methodStr;
	}

	public String getMaterialStr() {
		return materialStr;
	}

	public void setMaterialStr(String materialStr) {
		this.materialStr = materialStr;
	}

	public String getObsStr() {
		return obsStr;
	}

	public void setObsStr(String obsStr) {
		this.obsStr = obsStr;
	}

	public Set<TopicCategory> getTopicCategoryList() {
		return topicCategoryList;
	}

	public void setTopicCategoryList(Set<TopicCategory> topicCategoryList) {
		this.topicCategoryList = topicCategoryList;
	}
	public void populateAgentList(){
		  JSONArray agentArr = new JSONArray();
		  List<Object[]> agentList = agentService.listOfAgentByTraining();
		  if (!ObjectUtil.isEmpty(agentList)) {
			  agentList.forEach(obj -> {
			   agentArr.add(getJSONObject(obj[1].toString(), obj[0].toString()));
		   });
		  }
		  sendAjaxResponse(agentArr);
		 }
	
	public void populateTrainingCodeList(){
		  JSONArray agentArr = new JSONArray();
		  List<Object[]> trainingCodeList = agentService.listOfTrainingCode();
	//	  List<Object[]> agentList = agentService.listOfAgentByTraining();
		  if (!ObjectUtil.isEmpty(trainingCodeList)) {
			  trainingCodeList.forEach(obj -> {
			   agentArr.add(getJSONObject( obj[0].toString(),obj[0].toString()));
		   });
		  }
		  sendAjaxResponse(agentArr);
		 }
	public void populateLearningGroupList(){
		  JSONArray groupArr = new JSONArray();
		  List<Object[]> groupList = agentService.listOfLearningGroupByTraining();
		  if (!ObjectUtil.isEmpty(groupList)) {
			  groupList.forEach(obj -> {
				  groupArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
		   });
		  }
		  sendAjaxResponse(groupArr);
		 }
	
	public void populateFarmerList(){
		  JSONArray farmerArr = new JSONArray();
		  List<String> farmerFilterList= new LinkedList<>();
		  List<Object> farmerList = agentService.listOfFarmer();
		  farmerList.stream().forEach(obj->{
			  if(obj!=null && !ObjectUtil.isEmpty(obj)){
				  for(String s:String.valueOf(obj).split(",")){
					  if(!farmerFilterList.contains(s))
						  farmerFilterList.add(s);
				  }
			  }
		  });
		  if(farmerFilterList!=null && !ObjectUtil.isListEmpty(farmerFilterList)){
			  List<Object[]> frmrDataList=farmerService.findFarmerByListOfFarmerId(farmerFilterList);
			  if(frmrDataList!=null && !ObjectUtil.isEmpty(frmrDataList)){
				  frmrDataList.stream().distinct().forEach(f->{
					  if(f!=null && !ObjectUtil.isEmpty(f) && f[0]!=null)
					  farmerArr.add(getJSONObject(f[0].toString(),f[1]!=null?f[1].toString():""+" "+f[2]!=null?f[2].toString():""));
				  });
			  }
		  }
		  sendAjaxResponse(farmerArr);
		 }

	public String getFarmerCount() {
		return farmerCount;
	}

	public void setFarmerCount(String farmerCount) {
		this.farmerCount = farmerCount;
	}

	public String getTrainingTopicName() {
		return trainingTopicName;
	}

	public void setTrainingTopicName(String trainingTopicName) {
		this.trainingTopicName = trainingTopicName;
	}

	public String getAgentType() {
		return agentType;
	}

	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}

	public String getTrainingCode() {
		return trainingCode;
	}

	public void setTrainingCode(String trainingCode) {
		this.trainingCode = trainingCode;
	}

	public String getFarmerFilter() {
		return farmerFilter;
	}

	public void setFarmerFilter(String farmerFilter) {
		this.farmerFilter = farmerFilter;
	}
	
}
