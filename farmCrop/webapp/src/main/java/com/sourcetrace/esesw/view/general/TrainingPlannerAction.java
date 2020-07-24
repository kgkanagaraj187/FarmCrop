/*
 * TrainingPlannerAction.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view.general;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import org.springframework.security.access.method.P;

import com.ese.entity.txn.training.FarmerTraining;
import com.ese.entity.txn.training.Planner;
import com.ese.entity.txn.training.TargetGroup;
import com.ese.entity.txn.training.Topic;
import com.ese.entity.txn.training.TrainingMethod;
import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.service.ITrainingService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

public class TrainingPlannerAction extends SwitchValidatorAction {

	private static final long serialVersionUID = 8164016166008237876L;
	private static final Logger LOGGER = Logger.getLogger(TrainingPlannerAction.class);
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private int templateYear = DateUtil.getCurrentYear();
	private Integer[] templateYears = { 2016, 2017, 2018, 2019, 2020, 2021, 2022, 2023, 2024, 2015 };
	/*
	 * private String[] monthKey = { "Jan", "Feb", "Mar", "Apr", "May", "Jun",
	 * "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	 */
	private String[] monthKey = { (getText("mon.jan")), (getText("mon.feb")), (getText("mon.mar")),
			(getText("mon.apr")), (getText("mon.may")), (getText("mon.jun")), (getText("mon.jul")),
			(getText("mon.aug")), (getText("mon.sep")), (getText("mon.oct")), (getText("mon.nov")),
			(getText("mon.dec")) };

	private Map<String, Integer> monthMap;
	Map<Integer, String> monthNames = new LinkedHashMap<Integer, String>();
	private List<FarmerTraining> farmerTrainingList = new ArrayList<FarmerTraining>();
	private List<String> hideYearList = new ArrayList<String>();
	private List<String> hideYearMonthList = new ArrayList<String>();
	private List<String> hideYearMonthWeekList = new ArrayList<String>();
	private String selectedWeeks;
	private ITrainingService trainingService;
	private String selectedTrainingRefId;
	private FarmerTraining farmerTraining;
	private String deletedWeeks;

	

	public String getDeletedWeeks() {
		return deletedWeeks;
	}

	public void setDeletedWeeks(String deletedWeeks) {
		this.deletedWeeks = deletedWeeks;
	}

	/**
	 * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
	 */
	@Override
	public Object getData() {

		monthNames.put(1, "Jan");
		monthNames.put(2, "Feb");
		monthNames.put(3, "Mar");
		monthNames.put(4, "Apr");
		monthNames.put(5, "May");
		monthNames.put(6, "Jun");
		monthNames.put(7, "Jul");
		monthNames.put(8, "Aug");
		monthNames.put(9, "Sep");
		monthNames.put(10, "Oct");
		monthNames.put(11, "Nov");
		monthNames.put(12, "Dec");

		return null;
	}

	/**
	 * List.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 * @see com.sourcetrace.esesw.view.SwitchAction#list()
	 */
	public String list() throws Exception {

		request.setAttribute(HEADING, getText(LIST));
		processTrainingSchedule();
		return LIST;

	}
	
	

	/**
     * Update.
     * @return the string
     * @throws Exception the exception
     */
    public String update() throws Exception {

        if (!ObjectUtil.isEmpty(selectedWeeks) && selectedWeeks.length()> 0) {
            	String[] spiltComma=selectedWeeks.split("\\s*,\\s*");
            	for(int i=0;i<spiltComma.length;i++)
            	{
            		String[] weekInfoArray = spiltComma[i].split("_");
                if (!ObjectUtil.isEmpty(weekInfoArray) && weekInfoArray.length == 4) {
                    long topicId = Long.valueOf(weekInfoArray[0]); // index 0 for topic Id
                    int year = Integer.valueOf(weekInfoArray[1]); // index 1 for selected year
                    int month = Integer.valueOf(weekInfoArray[2]); // inded 2 from selected month
                    int week = Integer.valueOf(weekInfoArray[3]); // inded 2 from selected week
                    FarmerTraining farmerTraining = trainingService.findFarmerTrainingById(topicId);
                    
	                    if (!ObjectUtil.isEmpty(farmerTraining)) {
	                        Planner planner = new Planner();
	                        planner.setMonth(month);
	                        planner.setWeek(week);
	                        planner.setYear(year);
	                        planner.setFarmerTraining(farmerTraining);
	                        trainingService.add(planner);
	                    }
                    
                }
            }
        }
        
        if(!StringUtil.isEmpty(deletedWeeks) && deletedWeeks.length()>0)
        {
        	
        	String[] spiltComma=deletedWeeks.split("\\s*,\\s*");
        	for(int i=0;i<spiltComma.length;i++)
        	{
        		String[] weekInfoArray = spiltComma[i].split("_");
            if (!ObjectUtil.isEmpty(weekInfoArray) && weekInfoArray.length == 4) {
                long topicId = Long.valueOf(weekInfoArray[0]); // index 0 for topic Id
                int year = Integer.valueOf(weekInfoArray[1]); // index 1 for selected year
                int month = Integer.valueOf(weekInfoArray[2]); // inded 2 from selected month
                int week = Integer.valueOf(weekInfoArray[3]); // inded 2 from selected week
                FarmerTraining farmerTraining = trainingService.findFarmerTrainingById(topicId);
                if (!ObjectUtil.isEmpty(farmerTraining)) {
                	 Planner planner=trainingService.findPlannerByMntYrWk(year,month,week,farmerTraining.getId());
                	 if(!ObjectUtil.isEmpty(planner))
                	 {
                		 trainingService.deletePlanner(planner.getId());
                	 }
                }
            }
        }
        	
          

        }
        return REDIRECT;
    }

	public String getSelectedWeeks() {
		return selectedWeeks;
	}

	public void setSelectedWeeks(String selectedWeeks) {
		this.selectedWeeks = selectedWeeks;
	}

	/**
	 * Process training schedule.
	 */
	private void processTrainingSchedule() {

		if (!StringUtil.isEmpty((selectedTrainingRefId))) {
			try {
				long farmerTrainingId = Long.valueOf(selectedTrainingRefId);
				FarmerTraining farmerTraining = trainingService.findActiveFarmerTrainingById(farmerTrainingId);
				if (!ObjectUtil.isEmpty(farmerTraining)) {
					farmerTrainingList.add(farmerTraining);
				}
			} catch (Exception e) {
				LOGGER.info("Exception : " + e.getMessage());
				e.printStackTrace();
			}
		}

		if (ObjectUtil.isListEmpty(farmerTrainingList)) {
			setFarmerTrainingList(getFarmerTrainings());
		}

		setMonthMap();
		buildHideList();
	}

	/**
	 * Gets the farmer trainings.
	 * 
	 * @return the farmer trainings
	 */
	public List<FarmerTraining> getFarmerTrainings() {
		return trainingService.listActiveFarmerTraining();
	}

	/**
	 * Builds the hide list.
	 */
	private void buildHideList() {

		int currentYear = DateUtil.getCurrent(1);
		int currentMonth = DateUtil.getCurrent(2);
		int currentWeek = DateUtil.getCurrent(4);
		// Hide years fully
		for (int yearCount = 0; yearCount < templateYears.length; yearCount++) {
			if (templateYears[yearCount] < currentYear) {
				hideYearList.add(String.valueOf(templateYears[yearCount]));
			}
		}
		// Hide month & weeks in currnt year
		for (int monthCount = 0; monthCount < monthKey.length; monthCount++) {
			if (currentMonth > monthCount) {
				hideYearMonthList.add(String.valueOf(currentYear) + "_" + (monthCount + 1));
			}
			if (currentMonth == monthCount) {
				int noOfWeeks;
				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.SYMRISE)) {
				 noOfWeeks = 2;}else{
				 noOfWeeks = getNoOfWeeks(currentYear, currentMonth);
				}
				for (int weekCount = 1; weekCount <= noOfWeeks; weekCount++) {
					if (currentWeek > weekCount) {
						hideYearMonthWeekList
								.add(String.valueOf(currentYear) + "_" + (monthCount + 1) + "_" + weekCount);
					}
				}
			}
		}
	}

	/**
	 * Gets the no of weeks.
	 * 
	 * @param year
	 *            the year
	 * @param month
	 *            the month
	 * @return the no of weeks
	 */
	private int getNoOfWeeks(int year, int month) {

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
	}

	/**
	 * Sets the month map.
	 */
	public void setMonthMap() {

		monthMap = new LinkedHashMap<String, Integer>();
		String mnths = getLocaleProperty("months");
		String[] arr = mnths.split("\\,");
		// Calendar cal = Calendar.getInstance();
		for (int i = 0; i < arr.length; i++) {
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.SYMRISE)) {
			monthMap.put(arr[i], 2);}else{
			monthMap.put(arr[i], getNoOfWeeks(templateYear, i));
			}
		}
	}

	/**
	 * Gets the month map.
	 * 
	 * @return the month map
	 */
	public Map<String, Integer> getMonthMap() {

		return monthMap;
	}

	/**
	 * Gets the years.
	 * 
	 * @return the years
	 */
	public List<Integer> getYears() {

		List<Integer> yearList = new LinkedList<>();
		  for (int i = DateUtil.getCurrentYear(); i >= 2010; i--) {
		   yearList.add(i);
		  }
		return yearList;
	}

	/**
	 * Export.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String export() throws Exception {

		Date date = new Date();
		InputStream inputStream = null;

		String fileName = "TrainingPlanner-";
		inputStream = getTrainingPlannerExport();
		response.setContentType("application/xls");
		response.setHeader("Content-Disposition",
				"attachment; filename=" + fileName + fileNameDateFormat.format(date) + ".xls");

		byte[] buffer = new byte[1024 * 1024];
		while (true) {
			int bytesRead = inputStream.read(buffer);
			if (bytesRead < 0)
				break;
			response.getOutputStream().write(buffer, 0, bytesRead);
		}
		inputStream.close();
		response.getOutputStream().flush();
		response.getOutputStream().close();

		return null;
	}

	/**
	 * Gets the training planner export.
	 * 
	 * @return the training planner export
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private InputStream getTrainingPlannerExport() throws IOException {

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("Training-Planner");
		HSSFPatriarch drawing = sheet.createDrawingPatriarch();
		wb.createCellStyle();
		wb.createCellStyle();
		HSSFCellStyle titleStyle = wb.createCellStyle();

		HSSFFont titleFont = wb.createFont();
		titleFont.setFontHeightInPoints((short) 22);
		HSSFFont contentFont = wb.createFont();
		contentFont.setFontHeightInPoints((short) 8);
		HSSFFont headerFont = wb.createFont();
		headerFont.setFontHeightInPoints((short) 12);
		headerFont.setBoldweight((short) 12);

		HSSFCellStyle style = wb.createCellStyle();
		style = wb.createCellStyle();
		HSSFCellStyle cellRightAlignStyle;
		cellRightAlignStyle = wb.createCellStyle();
		cellRightAlignStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

		HSSFCellStyle greenStyle;
		greenStyle = wb.createCellStyle();
		greenStyle.setFillForegroundColor(HSSFColor.AQUA.index);
		greenStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		greenStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		greenStyle.setBorderBottom((short) 1);
		greenStyle.setBorderLeft((short) 1);
		greenStyle.setBorderRight((short) 1);
		greenStyle.setBorderTop((short) 1);
		greenStyle.setTopBorderColor(HSSFColor.BLACK.index);
		greenStyle.setRightBorderColor(HSSFColor.BLACK.index);
		greenStyle.setLeftBorderColor(HSSFColor.BLACK.index);
		greenStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		greenStyle.setFont(headerFont);

		HSSFCellStyle orangeStyle;
		orangeStyle = wb.createCellStyle();
		orangeStyle.setFillForegroundColor(HSSFColor.ORANGE.index);
		orangeStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		orangeStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		orangeStyle.setBorderBottom((short) 1);
		orangeStyle.setBorderLeft((short) 1);
		orangeStyle.setBorderRight((short) 1);
		orangeStyle.setBorderTop((short) 1);
		orangeStyle.setTopBorderColor(HSSFColor.BLACK.index);
		orangeStyle.setRightBorderColor(HSSFColor.BLACK.index);
		orangeStyle.setLeftBorderColor(HSSFColor.BLACK.index);
		orangeStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		orangeStyle.setFont(headerFont);

		HSSFCellStyle headerStyle;
		headerStyle = wb.createCellStyle();
		headerStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		headerStyle.setBorderBottom((short) 1);
		headerStyle.setBorderLeft((short) 1);
		headerStyle.setBorderRight((short) 1);
		headerStyle.setBorderTop((short) 1);
		headerStyle.setTopBorderColor(HSSFColor.BLACK.index);
		headerStyle.setRightBorderColor(HSSFColor.BLACK.index);
		headerStyle.setLeftBorderColor(HSSFColor.BLACK.index);
		headerStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		headerStyle.setFont(headerFont);

		HSSFCellStyle weekStyle = wb.createCellStyle();
		weekStyle.setBorderBottom((short) 1);
		weekStyle.setBorderLeft((short) 1);
		weekStyle.setBorderRight((short) 1);
		weekStyle.setBorderTop((short) 1);
		weekStyle.setTopBorderColor(HSSFColor.BLACK.index);
		weekStyle.setRightBorderColor(HSSFColor.BLACK.index);
		weekStyle.setLeftBorderColor(HSSFColor.BLACK.index);
		weekStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		weekStyle.setFillBackgroundColor(HSSFColor.YELLOW.index);
		weekStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
		weekStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		HSSFCellStyle contentStyle = wb.createCellStyle();
		contentStyle.setWrapText(true);
		contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		contentStyle.setFont(contentFont);

		HSSFRow row, titleRow;
		HSSFCell cell;
		int rowNum = 1;
		int colNum = 0;

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(3);
		cell.setCellValue(new HSSFRichTextString(getText("exportXLSTrainingPlannerTitle")));
		cell.setCellStyle(titleStyle);
		titleStyle.setFont(titleFont);
		style.setFont(headerFont);
		byte borderSize = 1;
		style.setBorderBottom(borderSize);
		style.setBorderLeft(borderSize);
		style.setBorderRight(borderSize);
		style.setBorderTop(borderSize);
		short borderColor = HSSFColor.GREY_80_PERCENT.index;
		style.setBottomBorderColor(borderColor);
		style.setTopBorderColor(borderColor);
		style.setRightBorderColor(borderColor);
		style.setLeftBorderColor(borderColor);

		row = sheet.createRow(rowNum++);

		// Filtering
		if (templateYear != 0) {
			row = sheet.createRow(rowNum++);
			cell = row.createCell(0);
			cell.setCellValue(new HSSFRichTextString(getText("filter")));
			cell.setCellStyle(headerStyle);
			headerFont.setBoldweight((short) 22);
			style.setFont(contentFont);
			row = sheet.createRow(rowNum++);
			cell = row.createCell(0);
			cell.setCellValue(new HSSFRichTextString(getText("year")));
			cell = row.createCell(1);
			String yearVal = String.valueOf(templateYear);
			cell.setCellValue(new HSSFRichTextString(yearVal));

			if (selectedTrainingRefId != null && !"".equalsIgnoreCase(selectedTrainingRefId)) {
				FarmerTraining farmerTraining = trainingService
						.findActiveFarmerTrainingById(Long.valueOf(selectedTrainingRefId));
				row = sheet.createRow(rowNum++);
				cell = row.createCell(0);
				cell.setCellValue(new HSSFRichTextString(getText("trainingRef")));
				cell = row.createCell(1);
				cell.setCellValue(new HSSFRichTextString(farmerTraining.getCode()));
			}
		}
		++rowNum;

		Map<String, Integer> weekPositions = new LinkedHashMap<String, Integer>();
		HSSFRow colRowHead = sheet.createRow(rowNum);
		String columnHeaders1 = getText("exportXLSTrainingPlannerHeading");
		setMonthMap();
		processTrainingSchedule();
		int weeklimit;
		String weekHeader = "";
		String monthHeader = "";
		int weekColumnNum = 4, weekColumnNum1 = 4;
		for (Map.Entry<String, Integer> entry : monthMap.entrySet()) {
			weeklimit = entry.getValue();
			monthHeader = entry.getKey();
			for (int i = 1; i <= weeklimit; i++) {
				weekHeader = String.valueOf(i);
				columnHeaders1 += weekHeader + ",";
				weekColumnNum++;
				weekPositions.put(templateYear + "_" + monthHeader + "_" + weekHeader, weekColumnNum);
			}
			weekColumnNum1 += 1;

			HSSFCell cell_Header = colRowHead.createCell(weekColumnNum1);
			cell_Header.setCellValue(new HSSFRichTextString(monthHeader));
			headerFont.setBoldweight((short) 12);
			headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			headerStyle.setFont(headerFont);
			headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			weekColumnNum1 += (weeklimit - 1);
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, cell_Header.getColumnIndex(), weekColumnNum1));
			cell_Header.setCellStyle(headerStyle);
		}
		rowNum++;

		HSSFRow rowHead1 = sheet.createRow(rowNum++);
		int iterator1 = 0;
		for (String cellHeader : columnHeaders1.split("\\,")) {
			cell = rowHead1.createCell(iterator1);
			cell.setCellValue(new HSSFRichTextString(cellHeader));
			if (iterator1 == 0 || iterator1 == 2)
				cell.setCellStyle(greenStyle);
			else if (iterator1 == 1 || iterator1 == 3 || iterator1 == 4)
				cell.setCellStyle(orangeStyle);
			else
				cell.setCellStyle(headerStyle);
			headerFont.setBoldweight((short) 12);
			headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			headerStyle.setFont(headerFont);
			if (iterator1 > 4) {
				sheet.setColumnWidth(iterator1, (15 * 80));
			} else {
				sheet.setColumnWidth(iterator1, (15 * 300));
			}
			iterator1++;
		}
		sheet.createFreezePane(5, rowNum);

		for (FarmerTraining farmerTraining : getFarmerTrainingList()) {
			row = sheet.createRow(rowNum++);
			colNum = 0;
			// Farmer Training code
			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(
					ObjectUtil.isEmpty(farmerTraining.getCode()) ? "" : farmerTraining.getCode()));
			cell.setCellStyle(contentStyle);
			contentStyle.setFont(contentFont);

			// Farmer Training Topic
			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(ObjectUtil.isEmpty(farmerTraining.getTrainingTopic()) ? ""
					: (StringUtil.isEmpty(farmerTraining.getTrainingTopic().getName()) ? ""
							: farmerTraining.getTrainingTopic().getName())));
			cell.setCellStyle(contentStyle);

			// Farmer Training Topic Criteria
			String topicValues = "";
			if (!ObjectUtil.isListEmpty(farmerTraining.getTopics())) {
				for (Topic topicList : farmerTraining.getTopics()) {
					topicValues += topicList.getCode() + ",";
				}
			}
			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(topicValues.substring(0, topicValues.length() - 1)));
			cell.setCellStyle(contentStyle);

			// Farmer Training Target Group
			String targetGroupValue = "";
			if (!ObjectUtil.isListEmpty(farmerTraining.getTargetGroups())) {
				for (TargetGroup targetGroupList : farmerTraining.getTargetGroups()) {
					targetGroupValue += targetGroupList.getName() + ",";
				}
			}
			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(targetGroupValue.substring(0, targetGroupValue.length() - 1)));
			cell.setCellStyle(contentStyle);

			// Farmer Training Methods
			String trainingMethodValues = "";
			if (!ObjectUtil.isListEmpty(farmerTraining.getTrainingMethods())) {
				for (TrainingMethod trainingMethodList : farmerTraining.getTrainingMethods()) {
					trainingMethodValues += trainingMethodList.getName() + ",";
				}
			}
			cell = row.createCell(colNum++);
			cell.setCellValue(
					new HSSFRichTextString(trainingMethodValues.substring(0, trainingMethodValues.length() - 1)));
			cell.setCellStyle(contentStyle);

			// Farmer Training Planner Values
			int colValu = 0;
			if (!ObjectUtil.isListEmpty(farmerTraining.getPlanners())) {
				for (Planner planner : farmerTraining.getPlanners()) {
					if (monthNames.containsKey(planner.getMonth())) {
						String monName = monthNames.get(planner.getMonth());
						if (weekPositions.containsKey(planner.getYear() + "_" + monName + "_" + planner.getWeek())) {
							colValu = weekPositions.get(planner.getYear() + "_" + monName + "_" + planner.getWeek());
							cell = row.createCell(colValu);
							cell.setCellStyle(weekStyle);
							cell.setCellValue(new HSSFRichTextString(getText("")));
						}
					}
				}
			}
		}

		// Add a picture
		int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(400, 10, 655, 200, (short) 0, 0, (short) 0, 0);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		picture.resize();

		String makeDir = FileUtil.storeXls(request.getSession().getId());
		Date date = new Date();
		String fileName = "TrainingPlanner-" + fileNameDateFormat.format(date) + ".xls";
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
		index = wb.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
	}

	/**
	 * Gets the hide year list.
	 * 
	 * @return the hide year list
	 */
	public List<String> getHideYearList() {

		return hideYearList;
	}

	/**
	 * Sets the hide year list.
	 * 
	 * @param hideYearList
	 *            the new hide year list
	 */
	public void setHideYearList(List<String> hideYearList) {

		this.hideYearList = hideYearList;
	}

	/**
	 * Gets the hide year month list.
	 * 
	 * @return the hide year month list
	 */
	public List<String> getHideYearMonthList() {

		return hideYearMonthList;
	}

	/**
	 * Sets the hide year month list.
	 * 
	 * @param hideYearMonthList
	 *            the new hide year month list
	 */
	public void setHideYearMonthList(List<String> hideYearMonthList) {

		this.hideYearMonthList = hideYearMonthList;
	}

	/**
	 * Gets the hide year month week list.
	 * 
	 * @return the hide year month week list
	 */
	public List<String> getHideYearMonthWeekList() {

		return hideYearMonthWeekList;
	}

	/**
	 * Sets the hide year month week list.
	 * 
	 * @param hideYearMonthWeekList
	 *            the new hide year month week list
	 */
	public void setHideYearMonthWeekList(List<String> hideYearMonthWeekList) {

		this.hideYearMonthWeekList = hideYearMonthWeekList;
	}

	/**
	 * Gets the template year.
	 * 
	 * @return the template year
	 */
	public int getTemplateYear() {

		return templateYear;
	}

	/**
	 * Sets the template year.
	 * 
	 * @param templateYear
	 *            the new template year
	 */
	public void setTemplateYear(int templateYear) {

		this.templateYear = templateYear;
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
	 * Gets the training service.
	 * 
	 * @return the training service
	 */
	public ITrainingService getTrainingService() {

		return trainingService;
	}

	/**
	 * Sets the farmer training list.
	 * 
	 * @param farmerTrainingList
	 *            the new farmer training list
	 */
	public void setFarmerTrainingList(List<FarmerTraining> farmerTrainingList) {

		this.farmerTrainingList = farmerTrainingList;
	}

	/**
	 * Gets the farmer training list.
	 * 
	 * @return the farmer training list
	 */
	public List<FarmerTraining> getFarmerTrainingList() {

		return farmerTrainingList;
	}

	/**
	 * Gets the selected training ref id.
	 * 
	 * @return the selected training ref id
	 */
	public String getSelectedTrainingRefId() {

		return selectedTrainingRefId;
	}

	/**
	 * Sets the selected training ref id.
	 * 
	 * @param selectedTrainingRefId
	 *            the new selected training ref id
	 */
	public void setSelectedTrainingRefId(String selectedTrainingRefId) {

		this.selectedTrainingRefId = selectedTrainingRefId;
	}

	/**
	 * Gets the month names.
	 * 
	 * @return the month names
	 */
	public Map<Integer, String> getMonthNames() {

		return monthNames;
	}

	/**
	 * Sets the month names.
	 * 
	 * @param monthNames
	 *            the month names
	 */
	public void setMonthNames(Map<Integer, String> monthNames) {

		this.monthNames = monthNames;
	}

	/**
	 * Sets the farmer training.
	 * 
	 * @param farmerTraining
	 *            the new farmer training
	 */
	public void setFarmerTraining(FarmerTraining farmerTraining) {

		this.farmerTraining = farmerTraining;
	}

	/**
	 * Gets the farmer training.
	 * 
	 * @return the farmer training
	 */
	public FarmerTraining getFarmerTraining() {

		return farmerTraining;
	}

}
