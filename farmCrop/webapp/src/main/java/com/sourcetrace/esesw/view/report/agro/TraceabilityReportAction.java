package com.sourcetrace.esesw.view.report.agro;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.URL;

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

import com.ese.entity.traceability.FarmerTraceability;
import com.ese.entity.util.ESESystem;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

import net.glxn.qrgen.QRCode;

public class TraceabilityReportAction extends BaseReportAction {
	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private FarmerTraceability filter;
	private String season;
	private String lotNo;
	private String prNo;
	Map<String,String> seasonMap=new HashMap<>();
	Map<Long,String> villageMap=new HashMap<>();
	Map<String,String> icsMap=new HashMap<>();
	Map<Long,String> shgMap=new HashMap<>();
	Map<Long,String> warehouseMap=new HashMap<>();
	private String xlsFileName;
	private InputStream fileInputStream;
	private String exportLimit;
	private String qrArgs;
	private String qrFileName;
	private String params;
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private ICatalogueService catalogueService;
	@Autowired
	private IPreferencesService preferencesService;
	@Autowired
	private IProductDistributionService productDistributionService;
	private String fileName;
	public String list() {
		ESESystem preferences = preferencesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			setExportLimit(preferences.getPreferences().get(ESESystem.EXPORT_RECORD_LIMIT));
		}
		return LIST;
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
	public void populateSeasonList(){
		JSONArray seasonArr=new JSONArray();
		List<HarvestSeason> hs= farmerService.listHarvestSeasons();
		if(hs!=null && !ObjectUtil.isListEmpty(hs)){
			hs.stream().forEach(s->{
				seasonArr.add(getJSONObject(s.getCode(),s.getName()));
			});
		}
		sendAjaxResponse(seasonArr);
	}
	public String data() throws Exception{
		populateICSMap();
		//populateSHGMap();
		populateSeasonMap();
		populateVillageMap();
		populateWarehouseMap();
		if((season!=null && !StringUtil.isEmpty(season))||(lotNo!=null && !StringUtil.isEmpty(lotNo)) ||(prNo!=null && !StringUtil.isEmpty(prNo))){
		filter=new FarmerTraceability();
		int flag=0;
		if(season!=null && !StringUtil.isEmpty(season)){
			filter.setSeason(season);
			flag=1;
		}
		/*else{
			filter.setSeason(getCurrentSeasonsCode());
		}*/
		if(lotNo!=null && !StringUtil.isEmpty(lotNo)){
			filter.setLotNo(lotNo);
			filter.setSeason(season!=null && !StringUtil.isEmpty(season)?season:getCurrentSeasonsCode());
			flag=1;
		}
		if(prNo!=null && !StringUtil.isEmpty(prNo)){
			filter.setPrNo(prNo);
			filter.setSeason(season!=null && !StringUtil.isEmpty(season)?season:getCurrentSeasonsCode());
			flag=1;
		}
		if(flag==1){
			super.filter=this.filter;
		Map data = readData();
		return sendJSONResponse(data);
		}
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {
		FarmerTraceability ft=(FarmerTraceability)obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		
		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(!StringUtil
						.isEmpty(getBranchesMap().get(getParentBranchMap().get(ft.getBranchId())))
								? getBranchesMap().get(getParentBranchMap().get(ft.getBranchId()))
								: getBranchesMap().get(ft.getBranchId()));
			}
			rows.add(getBranchesMap().get(ft.getBranchId()));
		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(branchesMap.get(ft.getBranchId()));
			}
		}
		rows.add(ft.getTracenetCode()!=null && !StringUtil.isEmail(ft.getTracenetCode())?ft.getTracenetCode():"");
		rows.add(ft.getFarmerName()!=null && !StringUtil.isEmpty(ft.getFarmerName())?ft.getFarmerName():"");
		rows.add(ft.getVillage()!=null && !StringUtil.isEmpty(ft.getVillage())?villageMap.get(Long.parseLong(ft.getVillage())):"");
		rows.add(ft.getIcs()!=null && !StringUtil.isEmpty(ft.getIcs())?icsMap.get(ft.getIcs()):"");
		rows.add(ft.getShg()!=null && !StringUtil.isEmpty(ft.getShg())?warehouseMap.get(Long.parseLong(ft.getShg())):"");
		rows.add(ft.getLotNo()!=null && !StringUtil.isEmpty(ft.getLotNo())?ft.getLotNo():"");
		rows.add(ft.getPrNo()!=null && !StringUtil.isEmpty(ft.getPrNo())?ft.getPrNo():"");
		rows.add(ft.getProcurementCenter()!=null && !StringUtil.isEmpty(ft.getProcurementCenter())?warehouseMap.get(Long.parseLong(ft.getProcurementCenter())):"");
		rows.add(ft.getGinning()!=null && !StringUtil.isEmpty(ft.getGinning())?warehouseMap.get(Long.parseLong(ft.getGinning())):"");
		rows.add(ft.getSpinning()!=null && !StringUtil.isEmpty(ft.getSpinning())?warehouseMap.get(Long.parseLong(ft.getSpinning())):"");
		rows.add("<a href=\"#\" class=\"fa fa-globe\"\"aria-hidden=\"true\"\" onclick=\"showMap('"+ft.getId()+ "')\"></a>");
		//rows.add("<button title='" + getText("farm.map.available.title") + "' onclick='showMap(\"" + ft.getId() + "\")'></button>");
		/*rows.add("<a href=\"#\" class=\"fa fa-print\"\"aria-hidden=\"true\"\" onclick=\"makeLabel('"+ft.getId()+ "')\"></a>");*/
		//fa fa-globe
		jsonObject.put("id",ft.getId());
		jsonObject.put("cell", rows);
		return jsonObject;
	}
	public IFarmerService getFarmerService() {
		return farmerService;
	}
	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}
	public FarmerTraceability getFilter() {
		return filter;
	}
	public void setFilter(FarmerTraceability filter) {
		this.filter = filter;
	}
	public String getSeason() {
		return season;
	}
	public void setSeason(String season) {
		this.season = season;
	}
	public Map<String, String> getSeasonMap() {
		
		return seasonMap;
	}
	public Map<Long, String> getVillageMap() {
		return villageMap;
	}
	public Map<String, String> getIcsMap() {
		return icsMap;
	}
	public Map<Long, String> getShgMap() {
		return shgMap;
	}
	public Map<String, String> populateSeasonMap(){
		List<HarvestSeason> hs=farmerService.listHarvestSeasons();
		if(hs!=null && !ObjectUtil.isListEmpty(hs)){
		hs.stream().forEach(s->{
			seasonMap.put(s.getCode(), s.getName());
		});
		}
		return seasonMap;
	}
	public Map<Long,String> populateVillageMap(){
		List<Object[]> villageList=locationService.listVillageIdAndName();
		if(villageList!=null && !ObjectUtil.isListEmpty(villageList)){
			villageList.stream().forEach(v->{
				villageMap.put(Long.parseLong(v[0].toString()), v[2].toString());
			});
		}
		return villageMap;
	}
	public Map<String,String> populateICSMap(){
		List<FarmCatalogue> fcList=catalogueService.listCataloguesByType(getLocaleProperty("icsNameType"));
		if(fcList!=null && !ObjectUtil.isListEmpty(fcList)){
			fcList.stream().forEach(ic->{
				icsMap.put(ic.getCode().toString(), ic.getName().toString());
			});
		}
		return icsMap;
	}
	public Map<Long,String> populateSHGMap(){
		List<Warehouse> shgList=locationService.listSamithiesBasedOnType();
		if(shgList!=null && !ObjectUtil.isEmpty(shgList)){
			shgList.stream().forEach(sh->{
				shgMap.put(sh.getId(), sh.getName().toString());
			});
		}
		return shgMap;
	}
	public Map<Long,String> populateWarehouseMap(){
		List<Warehouse> warehouseList=productDistributionService.listWarehouse();
		warehouseList.stream().filter(f -> f.getTypez() == Warehouse.PROCUREMENT_PLACE
				|| f.getTypez() == Warehouse.GINNER || f.getTypez() == Warehouse.SPINNING || f.getTypez() ==  Warehouse.SAMITHI).forEach(w -> {
					warehouseMap.put(w.getId(), w.getName());
				});
		return warehouseMap;
	}
	/*public String populateQRCode() throws Exception{
		System.out.println(qrArgs);
		setQrFileName(getText("qrCode") + fileNameDateFormat.format(new Date()));
		String reqURL=request.getRequestURL().toString();
		URL url= new URL(reqURL);
		String path=url.getPath();
		String fullPath[]=path.split("/",0);
		String urll=url.getProtocol()+"://192.168.1.190:9003"+"/"+fullPath[1];		 
		String tenant=getCurrentTenantId();
		String message =urll+"/getTraceDetails/farmerTraceDetails.html"+"?traceDetails=%"+qrArgs+"%"+tenant+"%"; 
		setQrFileName(getText("qrCode") + fileNameDateFormat.format(new Date()));
		ByteArrayOutputStream stream = QRCode.from(message).withErrorCorrection(ErrorCorrectionLevel.L).withHint(EncodeHintType.MARGIN, 2).withSize(250, 250).stream();
		setFileInputStream(new ByteArrayInputStream(stream.toByteArray()));
		return "qrCode";
	}*/
	public ILocationService getLocationService() {
		return locationService;
	}
	public void setLocationService(ILocationService locationService) {
		this.locationService = locationService;
	}
	public ICatalogueService getCatalogueService() {
		return catalogueService;
	}
	public void setCatalogueService(ICatalogueService catalogueService) {
		this.catalogueService = catalogueService;
	}
	public String getLotNo() {
		return lotNo;
	}
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	public String getPrNo() {
		return prNo;
	}
	public void setPrNo(String prNo) {
		this.prNo = prNo;
	}
	public void setSeasonMap(Map<String, String> seasonMap) {
		this.seasonMap = seasonMap;
	}
	public void setVillageMap(Map<Long, String> villageMap) {
		this.villageMap = villageMap;
	}
	public void setIcsMap(Map<String, String> icsMap) {
		this.icsMap = icsMap;
	}
	public void setShgMap(Map<Long, String> shgMap) {
		this.shgMap = shgMap;
	}

	public String populateXLS() throws Exception {
		InputStream is;
			is = getTraceabilityFileInputStream(IExporter.EXPORT_MANUAL);
		if (!ObjectUtil.isEmpty(is)) {
			setXlsFileName(getLocaleProperty("TraceabilityReport") + fileNameDateFormat.format(new Date()));
			Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
			fileMap.put(xlsFileName, is);
			setFileInputStream(FileUtil.createFileInputStreamToZipFile(getLocaleProperty("traceabilityReport").trim(), fileMap, ".xls"));
			return "xls";
		} else {
			return LIST;
		}
	}
	private InputStream getTraceabilityFileInputStream(String exportType) throws IOException {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("exportXLSTraceabilityTitle"));
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

		HSSFRow row, titleRow, filterRowTitle, filterRow1, filterRow2, filterRow3;
		HSSFCell cell;
		int rowNum = 2;
		int colNum = 0;
		branchIdValue = getBranchId();
		buildBranchMap();

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(3);
		cell.setCellValue(new HSSFRichTextString(getText("exportXLSTraceabilityTitle")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);
		
		filter=new FarmerTraceability();
		populateICSMap();
		//populateSHGMap();
		populateSeasonMap();
		populateVillageMap();
		populateWarehouseMap();
		filter=new FarmerTraceability();
		int flag=0;
		if(season!=null && !StringUtil.isEmpty(season)){
			filter.setSeason(season);
			flag=1;
		}
		if(lotNo!=null && !StringUtil.isEmpty(lotNo)){
			filter.setLotNo(lotNo);
			filter.setSeason(season!=null && !StringUtil.isEmpty(season)?season:getCurrentSeasonsCode());
			flag=1;
		}
		if(prNo!=null && !StringUtil.isEmpty(prNo)){
			filter.setPrNo(prNo);
			filter.setSeason(season!=null && !StringUtil.isEmpty(season)?season:getCurrentSeasonsCode());
			flag=1;
		}
		Map data=null;
		if(flag==1){
		super.filter=this.filter;
	
		data = readData();
		}
		if(data!=null && !ObjectUtil.isEmpty(data) && data.size()>0){
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
		
		if (!StringUtil.isEmpty(filter.getSeason())) {
			filterRow1 = sheet.createRow(rowNum++);

			cell = filterRow1.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("season")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);

			cell = filterRow1.createCell(2);
			// cell.setCellValue(new
			// HSSFRichTextString(filter1.getFarm().getFarmer().getFirstName()));
			cell.setCellValue(new HSSFRichTextString(seasonMap.get(filter.getSeason())));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);
		}
		if(filter.getLotNo()!=null && !StringUtil.isEmpty(filter.getLotNo())){
			filterRow2 = sheet.createRow(rowNum++);
			cell = filterRow2.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("lotNo")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);
			
			cell=filterRow2.createCell(2);
			cell.setCellValue(new HSSFRichTextString(filter.getLotNo()));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);
		}
		if(filter.getPrNo()!=null && !StringUtil.isEmpty(filter.getPrNo())){
			filterRow3 = sheet.createRow(rowNum++);
			cell = filterRow3.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("prNo")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);
			
			cell=filterRow3.createCell(2);
			cell.setCellValue(new HSSFRichTextString(filter.getPrNo()));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);
		}
		++rowNum;
		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		String columnHeaders = "";
		if (StringUtil.isEmpty(branchIdValue)) {
			columnHeaders = getLocaleProperty("exportTraceabilityReportListColumnHeaderBranch");
		} else  
			columnHeaders = getLocaleProperty("exportTraceabilityReportListtExportHeader");
		int mainGridIterator = 0;
		for (String cellHeader : columnHeaders.split("\\,")) {
			if (StringUtil.isEmpty(branchIdValue)) {
				cell = mainGridRowHead.createCell(mainGridIterator);
				cell.setCellValue(new HSSFRichTextString(cellHeader));
				style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
				style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cell.setCellStyle(style2);
				font2.setBoldweight((short) 12);
				font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				style2.setFont(font2);
				if (mainGridIterator != 7) {
					sheet.setColumnWidth(mainGridIterator, (15 * 450));
				}
				mainGridIterator++;
			} else {
				if (!cellHeader.equalsIgnoreCase(getText("app.branch"))) {
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
		
		List<FarmerTraceability> ftList= (List<FarmerTraceability>) data.get(ROWS);
		if(ftList!=null && !ObjectUtil.isListEmpty(ftList)){
			for(FarmerTraceability ft:ftList){
			row = sheet.createRow(rowNum++);
			colNum = 0;
			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
				if (StringUtil.isEmpty(branchIdValue)) {
					cell.setCellValue(new HSSFRichTextString(!StringUtil
							.isEmpty(getBranchesMap().get(getParentBranchMap().get(ft.getBranchId())))
									? getBranchesMap().get(getParentBranchMap().get(ft.getBranchId()))
									: getBranchesMap().get(ft.getBranchId())));
				}
				cell = row.createCell(colNum++);
				cell.setCellValue(getBranchesMap().get(ft.getBranchId()));
			}

			else {
				if (StringUtil.isEmpty(branchIdValue)) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(ft.getBranchId())
							? (branchesMap.get(ft.getBranchId())) : "N/A"));
				}
			}
			cell=row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(ft.getTracenetCode()!=null && !StringUtil.isEmpty(ft.getTracenetCode())?ft.getTracenetCode():""));
			
			cell=row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(ft.getFarmerName()!=null && !StringUtil.isEmpty(ft.getFarmerName())?ft.getFarmerName():""));
			
			cell=row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(ft.getVillage()!=null && !StringUtil.isEmpty(ft.getVillage())?villageMap.get(Long.parseLong(ft.getVillage())):""));
			
			cell=row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(ft.getIcs()!=null && !StringUtil.isEmpty(ft.getIcs())?icsMap.get(ft.getIcs()):""));
			
			cell=row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(ft.getShg()!=null && !StringUtil.isEmpty(ft.getShg())?warehouseMap.get(Long.parseLong(ft.getShg())):""));
			
			cell=row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(ft.getLotNo()!=null && !StringUtil.isEmpty(ft.getLotNo())?ft.getLotNo():""));
			
			cell=row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(ft.getPrNo()!=null && !StringUtil.isEmpty(ft.getPrNo())?ft.getPrNo():""));
			
			cell=row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(ft.getProcurementCenter()!=null && !StringUtil.isEmpty(ft.getProcurementCenter())?warehouseMap.get(Long.parseLong(ft.getProcurementCenter())):""));
			
			cell=row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(ft.getGinning()!=null && !StringUtil.isEmpty(ft.getGinning())?warehouseMap.get(Long.parseLong(ft.getGinning())):""));
			
			cell=row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(ft.getSpinning()!=null && !StringUtil.isEmpty(ft.getSpinning())?warehouseMap.get(Long.parseLong(ft.getSpinning())):""));
			}
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
			String fileName = getLocaleProperty("farmerList") + fileNameDateFormat.format(new Date()) + ".xls";
			FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
			wb.write(fileOut);
			InputStream stream = new FileInputStream(new File(makeDir + fileName));
			fileOut.close();
			return stream;
		}
		else{
			return null;
		}
		
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
	public IPreferencesService getPreferencesService() {
		return preferencesService;
	}
	public void setPreferencesService(IPreferencesService preferencesService) {
		this.preferencesService = preferencesService;
	}
	public Map<Long, String> getWarehouseMap() {
		return warehouseMap;
	}
	public void setWarehouseMap(Map<Long, String> warehouseMap) {
		this.warehouseMap = warehouseMap;
	}
	public IProductDistributionService getProductDistributionService() {
		return productDistributionService;
	}
	public void setProductDistributionService(IProductDistributionService productDistributionService) {
		this.productDistributionService = productDistributionService;
	}
	public String getQrArgs() {
		return qrArgs;
	}
	public void setQrArgs(String qrArgs) {
		this.qrArgs = qrArgs;
	}
	public String getQrFileName() {
		return qrFileName;
	}
	public void setQrFileName(String qrFileName) {
		this.qrFileName = qrFileName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	@SuppressWarnings("unchecked")
	public void populateCoordinates(){
		JSONObject jsonObject = new JSONObject();
//		System.out.println("params: "+params);
		populateWarehouseMap();
		FarmerTraceability ft=farmerService.findTraceabilityDataById(Long.parseLong(params));
		JSONArray farmArray = new JSONArray();
		StringBuffer fLat=new StringBuffer();
		StringBuffer fLongi=new StringBuffer();
		Farmer farmer = farmerService.findFarmerById(Long.parseLong(ft.getFarmerId()));
			if(farmer!=null && !ObjectUtil.isEmpty(farmer)){
			farmer.getFarms().stream().filter(frm -> frm.getLatitude()!=null && !StringUtil.isEmpty(frm.getLatitude()) && frm.getLongitude()!=null && !ObjectUtil.isEmpty(frm.getLongitude())).forEach(fm->{
				fLat.append(fm.getLatitude());
				fLongi.append(fm.getLongitude());
			});;
				jsonObject.put(0, ft.getFarmerName()+"~"+(fLat!=null && !ObjectUtil.isEmpty(fLat)?fLat:"")+"~"+(fLongi!=null && !ObjectUtil.isEmpty(fLongi)?fLongi:""));
			}
			String idsArr=Long.parseLong(ft.getProcurementCenter())+","+Long.parseLong(ft.getGinning())+","+Long.parseLong(ft.getSpinning());
			List<Warehouse> warehouses=locationService.listWarehouseByWarehouseIds(idsArr);
			if(warehouses!=null && !ObjectUtil.isListEmpty(warehouses)){
				warehouses.stream().distinct().forEach(wa->{
					jsonObject.put(wa.getTypez(), (wa.getName()+"~"+(wa.getLatitude()!=null && !StringUtil.isEmpty(wa.getLatitude())?wa.getLatitude():0.00)+"~"+(wa.getLongitude()!=null && !StringUtil.isEmpty(wa.getLongitude())?wa.getLongitude():0.00)));
				});
			}
		sendAjaxResponse(jsonObject);
	}
	
}
