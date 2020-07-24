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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.order.entity.txn.CowFeedType;
import com.sourcetrace.eses.order.entity.txn.CowInspection;
import com.sourcetrace.eses.order.entity.txn.CowInspectionImages;
import com.sourcetrace.eses.order.entity.txn.CowVaccination;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.util.Base64Util;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Cow;
import com.sourcetrace.esesw.entity.profile.Currency;
import com.sourcetrace.esesw.entity.profile.ResearchStation;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

public class CowInspectionReportAction extends BaseReportAction implements IExporter
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3199882732784874187L;
	
	private CowInspection filter;
	private InputStream fileInputStream;
	private String xlsFileName;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private String researchStationId;
	private IFarmerService farmerService;
	private CowInspection cowInspection;
	SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	private static final SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
	private IPreferencesService preferncesService;
	private String exportLimit;
	@Autowired
	private ICatalogueService catalogueService;
	private String cowIdName;
	private String catalougeValues;
	private Set<CowFeedType> feedTypes;
	private Set<CowInspectionImages> CowInspectionImages  = new HashSet<>(); 
	private String feedTotalAmt;
	private Set<CowVaccination> cowVaccinations;
	private Map<String, String> fields = new LinkedHashMap<String, String>();
	double  feedTot=0.0;
	public String list()
	{
		 Calendar currentDate = Calendar.getInstance();
	     Calendar cal = (Calendar) currentDate.clone();
	     cal.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) - 1);
	     DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
	     super.startDate = df.format(cal.getTime());
	     super.endDate = df.format(currentDate.getTime());
	     ESESystem preferences = preferncesService.findPrefernceById("1");
	     if (!StringUtil.isEmpty(preferences)) {
	     	setExportLimit(preferences.getPreferences().get(ESESystem.EXPORT_RECORD_LIMIT));
	      }
		
	    request.setAttribute(HEADING, getText(LIST));
	    setFilter(cowInspection);
		return LIST;
	}
	
	public String data() throws Exception
	{
		super.filter = this.filter;
        return sendJSONResponse(readData());
	}
	
	
	public String getResearchStationId() {
		return researchStationId;
	}

	public void setResearchStationId(String researchStationId) {
		this.researchStationId = researchStationId;
	}

	@SuppressWarnings("unchecked")
	 public JSONObject toJSON(Object record) 
	{
		CowInspection cowInspection=(CowInspection)record;
		JSONObject jsonObject = new JSONObject();
		if (record instanceof CowInspection)
        {
	    JSONArray rows = new JSONArray();
	    ESESystem preferences = preferncesService.findPrefernceById("1");
		DateFormat genDate = new SimpleDateFormat(!ObjectUtil.isEmpty(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT))?preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT):DateUtil.DATE_FORMAT);
		rows.add(!ObjectUtil.isEmpty(cowInspection.getCurrentInspDate())?String.valueOf(genDate.format(DateUtil.removeDateDotZero(cowInspection.getCurrentInspDate()))):"");
		rows.add(!ObjectUtil.isEmpty(cowInspection.getLastInspDate())?String.valueOf(genDate.format(DateUtil.removeDateDotZero(cowInspection.getLastInspDate()))):"");
		rows.add(!StringUtil.isEmpty(cowInspection.getCowId())?cowInspection.getCowId():"");
	  	rows.add(!ObjectUtil.isEmpty(cowInspection.getResearchStation())?cowInspection.getResearchStation().getName():"");
	  	rows.add(!StringUtil.isEmpty(cowInspection.getFarmerId())?cowInspection.getFarmerId()+"-"+cowInspection.getFarm().getFarmer().getName():"");
	   	rows.add(!ObjectUtil.isEmpty(cowInspection.getFarm())?cowInspection.getFarm().getFarmName():"");
	    rows.add(!StringUtil.isEmpty(cowInspection.getInspectionNo())?cowInspection.getInspectionNo():"");
	    rows.add(!StringUtil.isEmpty(cowInspection.getIntervalDays())?cowInspection.getIntervalDays():"");
	    rows.add(!StringUtil.isEmpty(cowInspection.getIsMilkingCow())?getText(cowInspection.getIsMilkingCow()):"");
	    if (!ObjectUtil.isEmpty(cowInspection)) {

			if ((!StringUtil.isEmpty(cowInspection.getLatitude())
					&& !StringUtil.isEmpty(cowInspection.getLongitude())))
					 {
				rows.add("<button class='faMap' title='" + getText("cow.map.available.title")
						+ "' onclick='showCowMap(\"" + cowInspection.getCowId()
						+ "\",\""
						+ (!ObjectUtil.isEmpty(cowInspection.getLatitude()) ? cowInspection.getLatitude() : "0")
						+ "\",\""
						+ (!ObjectUtil.isEmpty(cowInspection.getLongitude()) ? cowInspection.getLongitude() : "0")
						+"\")'></button>");

			} else {
				// No Latlon
				rows.add("<button class='no-latLonIcn' title='" + getText("cow.map.available.title")
						+ "'></button>");
			}
		} else {
			// No Latlon
			rows.add("<button class='no-latLonIcn' title='" + getText("cow.map.available.title")
					+ "'></button>");
		}
	    
	    byte[] voice = (byte[]) cowInspection.getAudio();
        if (!ObjectUtil.isEmpty(voice)) {
            rows.add(
                    voice.length != 0
                            ? rows.add("<button type=\"button\" title='" + getText("audio.download")
                                    + "' class=\"fa fa-download\" \" aria-hidden=\"true\" align=\"center\" onclick=\"downloadAudioFile("
                                    + cowInspection.getId() + ")\">" + "</button>" + "&nbsp;&nbsp;&nbsp;"
                                    + "<button type=\"button\" title='" + getText("audio.play")
                                    + "' class=\"fa fa-play-circle-o\" \" aria-hidden=\"true\"\" align=\"center\" onclick=\"playAudioFiles("
                                    + cowInspection.getId() + ",'"
                                    + sdf.format(cowInspection.getCurrentInspDate()) + "','"
                                    + cowInspection.getCowId()
                                    + "','"
                                    + getFarmerIdName(cowInspection.getFarmerId(),
                                    		cowInspection.getFarm().getFarmer().getFirstName())
                                    
                                    + "')\">" + "</button>")
                            : getText("NA"));
        } else {
            rows.add("<button type=\"button\" align=\"center\" title='" + getText("audio.noVoice")
                    + "' class=\"fa fa-stop-circle\">" + "</button>");

        }
        
	    jsonObject.put("id", cowInspection.getId());
        jsonObject.put("cell", rows);
        return jsonObject;
        } 
		return jsonObject;
		
		
	}
	 public String populateDownload() {

	        try {
	            filter = farmerService.findCowInspectionById(Long.valueOf(id));
	            if (ObjectUtil.isEmpty(filter)) {
	                return REDIRECT;
	            }
	            response.setContentType("audio/mpeg");
	            response.setHeader("Content-Disposition",
	                    "attachment;filename=" + filter.getFarm().getFarmName() + "_"
	                            + fileNameDateFormat.format(new Date()) + ".mp3");
	            response.getOutputStream().write(filter.getAudio());
	        } catch (Exception e) {
	            e.printStackTrace();

	        }
	        return null;
	    }
	
	 public String populateAudioPlay() {

	        try {
	            byte[] audioContent = farmerService
	                    .findCowInspectionCowVoiceById(Long.valueOf(id));
	            if (ObjectUtil.isEmpty(audioContent)) {
	                return REDIRECT;
	            }
	            response.setContentType("audio/mpeg");
	            response.setContentLength(audioContent.length);
	            response.setHeader("Content-Disposition",
	                    "attachment;filename=" + String.valueOf(this.getId()) + "_"
	                            + fileNameDateFormat.format(new Date()) + ".mp3");
	            response.getOutputStream().write(audioContent);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	
	public String detail() {

		if (!StringUtil.isEmpty(getId())) {
			cowInspection = farmerService.findCowInspectionById(Long.valueOf(id));
			if (!ObjectUtil.isEmpty(cowInspection)) {
				Cow cow = farmerService.findByCowId(cowInspection.getCowId());
				if (!ObjectUtil.isEmpty(cow)) {
					setCowIdName(cow.getId() + "");
				}
				
				//cowInspection.setMedicineName(getCatalouge(cowInspection.getMedicineName()));
				cowInspection.setDiseaseName(getCatalouge(cowInspection.getDiseaseName()));
				cowInspection.setDiseaseServices(getCatalouge(cowInspection.getDiseaseServices()));
				cowInspection.setHealthServices(getCatalouge(cowInspection.getHealthServices()));
				cowInspection.setInfestationPara(getCatalouge(cowInspection.getInfestationPara()));
				cowInspection.setDeworwingPlace(getText(cowInspection.getDeworwingPlace()));
				cowInspection.setDiseaseNoticed(getText(cowInspection.getDiseaseNoticed()));
				cowInspection.setHealthProblem(getText(cowInspection.getHealthProblem()));
				cowInspection.setVaccinationPlace(getText(cowInspection.getVaccinationPlace()));
				cowInspection.setIsMilkingCow(getText(cowInspection.getIsMilkingCow()));
				feedTypes = new HashSet<>();
				
				cowInspection.getCowFeedTypes().stream().filter(feedType -> !ObjectUtil.isEmpty(feedType))
						.forEach(feedType -> {
							
							CowFeedType feedTypez = new CowFeedType();
							feedTypez.setFeedType(getCatalouge(feedType.getFeedType()));
							feedTypez.setAmount(feedType.getAmount());
							 feedTot+=feedType.getAmount();
							
							feedTypes.add(feedTypez);
							//setFeedTotalAmt(CurrencyUtil.getDecimalFormat(feedTot, "#.##"));
						});
				
				 feedTotalAmt=CurrencyUtil.getDecimalFormat(feedTot, "0.00");
				 cowVaccinations=new HashSet<>();
				 
				 cowInspection.getCowVaccinations().stream().filter(cowVacc -> !ObjectUtil.isEmpty(cowVacc))
					.forEach(cowVacc -> {
						
						CowVaccination cowVaccination = new CowVaccination();
						cowVaccination.setName(getCatalouge(cowVacc.getName()));
						String removeDot=String.valueOf(cowVacc.getDate()).replace(".0", "");
						cowVaccination.setDate(DateUtil.convertStringToDate(removeDot, DateUtil.TXN_DATE_TIME));
						cowVaccinations.add(cowVaccination);
						//setFeedTotalAmt(CurrencyUtil.getDecimalFormat(feedTot, "#.##"));
					});
			
				if(!ObjectUtil.isListEmpty(cowInspection.getInspectionImages())){
					cowInspection.getInspectionImages().stream().filter(img->!ObjectUtil.isEmpty(img)).forEach(img->{
						CowInspectionImages images = new CowInspectionImages();
						 if (!StringUtil.isEmpty(img.getPhoto())) {
							 img.setImageByteString(Base64Util.encoder(img.getPhoto()));
						 }
						 CowInspectionImages.add(img);
					});
				}

			}
		} else {
			return LIST;
		}

		return DETAIL;
	}
	
	
	public Set<CowFeedType> getFeedTypes() {
		return feedTypes;
	}

	public void setFeedTypes(Set<CowFeedType> feedTypes) {
		this.feedTypes = feedTypes;
	}

	public String getCatalouge(String code) {
		setCatalougeValues("");
		if (!StringUtil.isEmpty(code)) {
			String[] codes = code.split(",");
			Arrays.asList(codes).stream().filter(codez -> !StringUtil.isEmpty(codez)).forEach(codez -> {
				FarmCatalogue catalogue = catalogueService.findCatalogueByCode(codez);
				if (!ObjectUtil.isEmpty(catalogue)) {
					if (!StringUtil.isEmpty(getCatalougeValues())) {
						setCatalougeValues(getCatalougeValues() + catalogue.getName() + ",");
					} else {
						setCatalougeValues(catalogue.getName() + ",");
					}
				}
			});
		}

		return !StringUtil.isEmpty(catalougeValues) ? StringUtil.removeLastComma(catalougeValues) : "";
	}

	
	public String populateXLS() throws Exception {

        InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
        setXlsFileName(getText("cowInspectionReport") + fileNameDateFormat.format(new Date()));
        Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
        fileMap.put(xlsFileName, is);
        setFileInputStream(FileUtil.createFileInputStreamToZipFile(
                getText("cowInspectionReport"), fileMap, ".xls"));
        return "xls";
    }





	public Map<String,String> getElitTypeList()
	{
		Map<String,String> elitTypMap=new HashMap<>();
		elitTypMap.put(Cow.ELITE_FARMER, getText("elitTypeFarmer"));
		elitTypMap.put(Cow.ELITE_RESEARCH, getText("elitTypeRs"));
		return elitTypMap;
	}
	
	public Map<String,String> getFarmerList()
	{
		Map<String,String> farmerMap=new HashMap<>();
		List<Object[]> farmerList=farmerService.findByCowInspFarmer();
		for(Object[] object:farmerList)
		{
			farmerMap.put(String.valueOf(object[0]), String.valueOf(object[1]));
		}
		return farmerMap;
	}
	
	
	
	public Map<String,String> getResearchStationList()
	{
		Map<String,String> researchStatMap=new HashMap<>();
		List<Object[]> researchStatList=farmerService.findByResearchStation();
		for(Object[] object:researchStatList)
		{
			researchStatMap.put(String.valueOf(object[0]), String.valueOf(object[1]));
		}
		return researchStatMap;
	}
	
	
	public Map<String,String> getCowList()
	{
		try{
		Map<String,String> cowMap=new HashMap<>();
		List<String> cowListTemp=farmerService.findByCowList();
		for(String object:cowListTemp)
		{
			cowMap.put(object, object);
		}
		return cowMap;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}

	@Override
	public InputStream getExportDataStream(String exportType) throws IOException {
		// TODO Auto-generated method stub
		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		setsDate(IExporter.EXPORT_DAILY.equalsIgnoreCase(exportType) ? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));
		boolean flag = true;
		DateFormat filterDateFormat = new SimpleDateFormat(FILTERDATE);
		DecimalFormat df = new DecimalFormat("0.000");
		DecimalFormat df1 = new DecimalFormat("0.00");

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("exportCowInspeTitle"));
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

		HSSFRow row, titleRow, filterRowTitle, filterRow1, filterRow2, filterRow3, filterRow4 = null, filterRow5 = null;
		HSSFCell cell;

		int imgRow1 = 0;
		int imgRow2 = 4;
		int imgCol1 = 0;
		int imgCol2 = 1;
		int titleRow1 = 2;
		int titleRow2 = 3;

		int rowNum = 3;
		int colNum = 0;

		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap(); // build branch map to get branch name form branch id.

		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, titleRow1, titleRow2));

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(2);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("exportCowInspeTitle")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);

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

			if (!StringUtil.isEmpty(filter.getCowId())) {
				filterRow1 = sheet.createRow(rowNum++);

				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("cow.cowId")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow1.createCell(2);
				cell.setCellValue(new HSSFRichTextString(filter.getCowId()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			
			if (!StringUtil.isEmpty(filter.getFarmerId())) {
				filterRow1 = sheet.createRow(rowNum++);

				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmerId")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow1.createCell(2);
				cell.setCellValue(new HSSFRichTextString(filter.getFarmerId()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			
			if (!ObjectUtil.isEmpty(filter.getResearchStation())) {
				filterRow1 = sheet.createRow(rowNum++);

				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("cow.researchStationName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow1.createCell(2);
				cell.setCellValue(new HSSFRichTextString(filter.getResearchStation().getName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			
			
			if (!ObjectUtil.isEmpty(filter.getElitType())) {
				filterRow1 = sheet.createRow(rowNum++);

				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("elitType")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow1.createCell(2);
				cell.setCellValue(new HSSFRichTextString(filter.getElitType()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			filterRow4 = sheet.createRow(rowNum++);
			filterRow5 = sheet.createRow(rowNum++);
		}
		rowNum++;

		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		String mainGridColumnHeaders = "";
		if (StringUtil.isEmpty(branchIdValue)) {
			mainGridColumnHeaders = getText("exportColumnHeaderBranch");
		} else {
			mainGridColumnHeaders = getText("exportColumnHeader");
		}
		int mainGridIterator = 0;

		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {
			if (StringUtil.isEmpty(branchIdValue)) { // Add branch header to
														// export sheet if main
														// branch logged in.
				cell = mainGridRowHead.createCell(mainGridIterator);
				cell.setCellValue(new HSSFRichTextString(cellHeader));
				style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
				style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cell.setCellStyle(style2);
				font2.setBoldweight((short) 12);
				font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				style2.setFont(font2);
				sheet.setColumnWidth(mainGridIterator, (15 * 550));
				mainGridIterator++;
			} else {
				if (!cellHeader.equalsIgnoreCase("Organization")) {
					cell = mainGridRowHead.createCell(mainGridIterator);
					cell.setCellValue(new HSSFRichTextString(cellHeader));
					style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
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
		
	      if (!StringUtil.isEmpty(researchStationId)) {
	           ResearchStation rs=new ResearchStation();
	           rs.setResearchStationId(researchStationId);
	           filter.setResearchStation(rs);
	        }
	        

		super.filter = this.filter;

		Map data = isMailExport() ? readData() : readExportData();

		List<CowInspection> mainGridRows = (List<CowInspection>) data.get(ROWS);

		if (!ObjectUtil.isEmpty(mainGridRows)) 
		{
			
			for (CowInspection cowInspection : mainGridRows)
			{
				
				row = sheet.createRow(rowNum++);
				colNum = 0;

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(cowInspection.getCurrentInspDate())
						? (!StringUtil.isEmpty(sdf.format(cowInspection.getCurrentInspDate()))
								? sdf.format(cowInspection.getCurrentInspDate()) : "")
						: ""));
				
				
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(cowInspection.getLastInspDate())
						? (!StringUtil.isEmpty(sdf.format(cowInspection.getLastInspDate()))
								? sdf.format(cowInspection.getLastInspDate()) : "")
						: ""));

				
				
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(
						(!StringUtil.isEmpty(cowInspection.getCowId()) ? cowInspection.getCowId() : "")));

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(
						(!ObjectUtil.isEmpty(cowInspection.getResearchStation()) ? cowInspection.getResearchStation().getName() : "")));
				
				
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(
						(!StringUtil.isEmpty(cowInspection.getFarmerId()) ? cowInspection.getFarmerId() : "")));
				
				
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(
						(!ObjectUtil.isEmpty(cowInspection.getFarm()) ? cowInspection.getFarm().getFarmName() : "")));
				
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(
						(!StringUtil.isEmpty(cowInspection.getInspectionNo()) ? cowInspection.getInspectionNo() : "")));
				
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(
						(!StringUtil.isEmpty(cowInspection.getInspectionNo()) ? cowInspection.getInspectionNo() : "")));
				
				
			}
			
			
			int pictureIdx = getPicIndex(wb);
			HSSFClientAnchor anchor = new HSSFClientAnchor(400, 10, 655, 200, (short) 0, 0, (short) 0, 0);
			anchor.setAnchorType(1);
			HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
			picture.resize();
			String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
					: request.getSession().getId();
			String makeDir = FileUtil.storeXls(id);
			String fileName = getLocaleProperty("procurementReportList") + fileNameDateFormat.format(new Date()) + ".xls";
			FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
			wb.write(fileOut);
			InputStream stream = new FileInputStream(new File(makeDir + fileName));
			fileOut.close();

			return stream;

			
		}
		return fileInputStream;
	}
	
	
	public int getPicIndex(HSSFWorkbook wb) throws IOException {

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

	public CowInspection getFilter() {
		return filter;
	}

	public void setFilter(CowInspection filter) {
		this.filter = filter;
	}


	public void setCowInspection(CowInspection cowInspection) {
		this.cowInspection = cowInspection;
	}

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}
	
	public CowInspection getCowInspection() {
		return cowInspection;
	}

	

	public String getExportLimit() {
		return exportLimit;
	}

	public void setExportLimit(String exportLimit) {
		this.exportLimit = exportLimit;
	}

	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}

	public String getCowIdName() {
		return cowIdName;
	}

	public void setCowIdName(String cowIdName) {
		this.cowIdName = cowIdName;
	}

	public String getCatalougeValues() {
		return catalougeValues;
	}

	public void setCatalougeValues(String catalougeValues) {
		this.catalougeValues = catalougeValues;
	}



	 private String getFarmerIdName(String farmerId, String name) {

	        return (farmerId) + "-" + (name);
	    }

	public String getFeedTotalAmt() {
		return feedTotalAmt;
	}

	public void setFeedTotalAmt(String feedTotalAmt) {
		this.feedTotalAmt = feedTotalAmt;
	}
	
	 public Set<CowVaccination> getCowVaccinations() {
			return cowVaccinations;
		}

		public void setCowVaccinations(Set<CowVaccination> cowVaccinations) {
			this.cowVaccinations = cowVaccinations;
		}

		public Map<String, String> getFields() {
			
			
			fields.put("1",getText("inspDate"));
			fields.put("2",getText("cow.cowId"));
			fields.put("3",getText("farmerId"));
			fields.put("4",getText("cow.researchStationName"));
			fields.put("5",getText("elitType"));
			if (ObjectUtil.isEmpty(getBranchId())) {
				fields.put("6",getText("app.branch"));
			}
			return fields;
		}

}
