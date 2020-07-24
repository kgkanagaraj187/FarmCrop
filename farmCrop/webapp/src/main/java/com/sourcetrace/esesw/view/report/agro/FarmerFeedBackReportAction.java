package com.sourcetrace.esesw.view.report.agro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import org.apache.poi.hssf.util.HSSFColor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.FarmerFeedbackEntity;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.service.ICertificationService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.view.BaseReportAction;

public class FarmerFeedBackReportAction extends BaseReportAction {
	private static final long serialVersionUID = 1L;
	
	DateFormat df = new SimpleDateFormat(getESEDateFormat());
	SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	private IPreferencesService preferncesService;
	private ICertificationService certificationService;
	private IFarmerService farmerService;
	private FarmerFeedbackEntity filter;
	private String farmerId;
	
	@Autowired
	private ILocationService locationService;
	private Map<String, String> farmerNameList = new HashMap<String, String>();
	private String xlsFileName;
	private InputStream fileInputStream;


	
	public String list() throws Exception {

		Calendar currentDate = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		request.setAttribute(HEADING, getText(LIST));
		ESESystem preferences = preferncesService.findPrefernceById("1");
		
		setFilter(new FarmerFeedbackEntity());
		return LIST;
	}
		@SuppressWarnings("unchecked")
		public String data() throws Exception {
	    Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
					getPage());
			return sendJSONResponse(data);
		}

	 
	 
	 @SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		JSONObject jsonObject = new JSONObject();
		FarmerFeedbackEntity farmerFeedbackEntity = (FarmerFeedbackEntity) obj;
		ESESystem preferences = preferncesService.findPrefernceById("1");
		DateFormat genDate = new SimpleDateFormat(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
		JSONArray rows = new JSONArray();

		rows.add(genDate.format(farmerFeedbackEntity.getAnsweredDate()));
		if (!ObjectUtil.isEmpty(farmerFeedbackEntity.getVillage())) {
			Village village = locationService.findVillageById(Long.valueOf(farmerFeedbackEntity.getVillage().getId()));
			rows.add(!StringUtil.isEmpty(village) ? village.getName() : getText("NA"));
		} else {
			rows.add(getText("NA"));
		}
		if (!ObjectUtil.isEmpty(farmerFeedbackEntity.getWarehouse())) {
			
			rows.add(!StringUtil.isEmpty(farmerFeedbackEntity.getWarehouse()) ? farmerFeedbackEntity.getWarehouse().getName() : getText("NA"));
		} else {
			rows.add(getText("NA"));
		}
		rows.add(!StringUtil.isEmpty(farmerFeedbackEntity.getFarmerName()) ? farmerFeedbackEntity.getFarmerName() : getText("NA"));

		rows.add(farmerFeedbackEntity.getQuestion1());
		rows.add(farmerFeedbackEntity.getQuestion2());
		rows.add(farmerFeedbackEntity.getQuestion3());
		rows.add(farmerFeedbackEntity.getQuestion4());

		jsonObject.put("id", farmerFeedbackEntity.getId());

		jsonObject.put("cell", rows);
		return jsonObject;
	}
	public DateFormat getDf() {
		return df;
	}
	public void setDf(DateFormat df) {
		this.df = df;
	}
	public SimpleDateFormat getSdf() {
		return sdf;
	}
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}
	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}
	public ICertificationService getCertificationService() {
		return certificationService;
	}
	public void setCertificationService(ICertificationService certificationService) {
		this.certificationService = certificationService;
	}
	public IFarmerService getFarmerService() {
		return farmerService;
	}
	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public static SimpleDateFormat getFilenamedateformat() {
		return fileNameDateFormat;
	}
	public FarmerFeedbackEntity getFilter() {
		return filter;
	}
	public void setFilter(FarmerFeedbackEntity filter) {
		this.filter = filter;
	}
	public String getFarmerId() {
		return farmerId;
	}
	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}
	public ILocationService getLocationService() {
		return locationService;
	}
	public void setLocationService(ILocationService locationService) {
		this.locationService = locationService;
	}
	public Map<String, String> getFarmerNameList() {
		List<FarmerFeedbackEntity> farmerList = farmerService.findFamerFromFarmerFeedback();
		farmerList.stream().forEach(entry->{
			if(!farmerNameList.containsKey(entry.getFarmerId())){
				farmerNameList.put(entry.getFarmerId(),entry.getFarmerName());
			}
		});
		
		return farmerNameList;
	}
	public void setFarmerNameList(Map<String, String> farmerNameList) {
		this.farmerNameList = farmerNameList;
	}
	
	public List<Warehouse> getWarehouseList() {
		List<FarmerFeedbackEntity> farmerList = farmerService.findFamerFromFarmerFeedback();
		List<Warehouse> vilList  = farmerList.stream()
                .map(FarmerFeedbackEntity::getWarehouse)
                .collect(Collectors.toList());
		
		return vilList;
	}
	
	public List<Village> getVillageList() {
		List<FarmerFeedbackEntity> farmerList = farmerService.findFamerFromFarmerFeedback();
		List<Village> vilList  = farmerList.stream()
                .map(FarmerFeedbackEntity::getVillage)
                .collect(Collectors.toList());
		
		return vilList;
	}
	
	 
	 public String populateXLS() throws Exception {
			InputStream is = getExportDataStream();
			setXlsFileName(getLocaleProperty("exportXLSFarmerFeedbackTitle")+ fileNameDateFormat.format(new Date()));
			Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
			fileMap.put(xlsFileName, is);
			setFileInputStream(FileUtil.createFileInputStreamToZipFile(getLocaleProperty("exportXLSFarmerFeedbackTitle"), fileMap, ".xls"));
			return "xls";
		}
	 private InputStream getExportDataStream() throws IOException {

			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet(getLocaleProperty("exportXLSFarmerFeedbackTitle"));
			HSSFPatriarch drawing = sheet.createDrawingPatriarch();

			HSSFCellStyle style1 = wb.createCellStyle();
			HSSFCellStyle style2 = wb.createCellStyle();

			HSSFFont font1 = wb.createFont();
			font1.setFontHeightInPoints((short) 22);

			HSSFFont font2 = wb.createFont();
			font2.setFontHeightInPoints((short) 12);

			HSSFFont font3 = wb.createFont();
			font3.setFontHeightInPoints((short) 10);

			HSSFRow row, titleRow;
			HSSFCell cell;

			int rowNum = 3;
			int colNum = 0;

			branchIdValue = getBranchId(); // set value for branch id.
			buildBranchMap();

			titleRow = sheet.createRow(rowNum++);
			cell = titleRow.createCell(3);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("exportXLSFarmerFeedbackTitle")));
			cell.setCellStyle(style1);
			font1.setBoldweight((short) 22);
			font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			style1.setFont(font1);

			++rowNum;

			String	columnHeaders1 = getLocaleProperty("exportXLSFarmerFeedbackHeadingSubOrg");
			HSSFRow rowHead1 = sheet.createRow(rowNum++);

			int iterator1 = 0;
			for (String cellHeader : columnHeaders1.split("\\,")) {
				if (StringUtil.isEmpty(branchIdValue)) {
					cell = rowHead1.createCell(iterator1);
					cell.setCellValue(new HSSFRichTextString(cellHeader));
					style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
					style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					cell.setCellStyle(style2);
					font2.setBoldweight((short) 12);
					font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					style2.setFont(font2);
					if (iterator1 != 7) {
						sheet.setColumnWidth(iterator1, (15 * 450));
					}
					iterator1++;

				} else {
					if (!cellHeader.equalsIgnoreCase(getText("app.branch"))) {
						cell = rowHead1.createCell(iterator1);
						cell.setCellValue(new HSSFRichTextString(cellHeader));
						// style2.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
						// style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
						cell.setCellStyle(style2);
						font2.setBoldweight((short) 12);
						font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						style2.setFont(font2);
						sheet.setColumnWidth(iterator1, (15 * 550));
						iterator1++;
					}
				}
			}
			

			filter = new FarmerFeedbackEntity();
			
			
			if (!StringUtil.isEmpty(farmerId)) {
				filter.setFarmerId(farmerId.trim());
			}
			
		
			

			// super.filter = this.filter;
			super.filter = this.filter;
			Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
					getPage());
			List<FarmerFeedbackEntity> fList = (List<FarmerFeedbackEntity>) data.get(ROWS);
			ESESystem preferences = preferncesService.findPrefernceById("1");
			DateFormat genDate = new SimpleDateFormat(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
	
			for (FarmerFeedbackEntity farmerFeedbackEntity : fList) {
				row = sheet.createRow(rowNum++);
				colNum = 0;
				
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(genDate.format(farmerFeedbackEntity.getAnsweredDate())));
				
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(farmerFeedbackEntity.getVillage()) ? farmerFeedbackEntity.getVillage().getName() : getText("NA")));
				
				
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(farmerFeedbackEntity.getWarehouse()) ? farmerFeedbackEntity.getWarehouse().getName() : getText("NA")));
				
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(farmerFeedbackEntity.getFarmerName()) ? farmerFeedbackEntity.getFarmerName() : getText("NA")));
				
				
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(farmerFeedbackEntity.getQuestion1()));
				
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(farmerFeedbackEntity.getQuestion2()));
				
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(farmerFeedbackEntity.getQuestion3()));
				
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(farmerFeedbackEntity.getQuestion4()));
				
				
			
			
			}
			// Add a picture
			int pictureIdx = getPicIndex(wb);
			HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
			anchor.setAnchorType(1);
			HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
			// picture.resize();

			String makeDir = FileUtil.storeXls(request.getSession().getId());
			String fileName = getText("farmerList") + fileNameDateFormat.format(new Date()) + ".xls";
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
			String roleName = (String) request.getSession().getAttribute("role");

			byte[] picData = null;

			picData = clientService.findLogoByCode(Asset.APP_LOGO);

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
	 
	 
}
