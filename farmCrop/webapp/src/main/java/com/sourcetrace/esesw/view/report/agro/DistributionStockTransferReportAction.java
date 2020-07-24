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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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

import com.ese.entity.profile.CropHarvest;
import com.ese.entity.profile.CropSupply;
import com.ese.entity.profile.CropSupplyDetails;
import com.ese.entity.util.ESESystem;
import com.opensymphony.xwork2.ActionContext;
import com.sourcetrace.eses.BreadCrumb;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.entity.WarehouseProduct;
import com.sourcetrace.eses.entity.WarehouseProductDetail;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.Distribution;
import com.sourcetrace.eses.order.entity.txn.DistributionDetail;
import com.sourcetrace.eses.order.entity.txn.DistributionStock;
import com.sourcetrace.eses.order.entity.txn.DistributionStockDetail;
import com.sourcetrace.eses.order.entity.txn.PMT;
import com.sourcetrace.eses.order.entity.txn.PMTDetail;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.profile.Product;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmIcsConversion;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.State;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;


@SuppressWarnings("serial")
public class DistributionStockTransferReportAction extends BaseReportAction implements IExporter{

	@Autowired
	private IPreferencesService preferncesService;
	private static final String TRANSFER = "transfer";
	private static final String RECEPTION = "reception";
	
	public static final String DISTRIBUTION_STOCK_TRANSFER = "314DT";
	public static final String DISTRIBUTION_STOCK_RECEPTION = "314DR";
	 
	private String exportLimit;
	private String daterange;
	private String receiptNo;
	private String truckId;
	private String product;
	private String season;
	private DistributionStock distributionStock;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private IProductService productService;
	@Autowired
	private IFarmerService farmerService;
	private String type;
	private DistributionStock filter;
	private DistributionStockDetail filters;
	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private String xlsFileName;
	private InputStream fileInputStream;
	private String receiverWarehouse;
	private String senderWarehouse;
	private String branchIdParma;
	private String distId;
	@Autowired
	private IProductDistributionService productDistributionService;
	
	
	
	public String list() throws Exception{
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
		//daterange = super.startDate + " - " + super.endDate;

		type = request.getParameter("type");
		filter = new DistributionStock();
		request.setAttribute(HEADING, getText(LIST + type));
		
		return LIST;
	}
	
	
	@SuppressWarnings("unchecked")
	public String detail() throws Exception {

		Map<String, String> searchRecord = getJQGridRequestParam();
		setFilter(new DistributionStock());
		
		type = request.getParameter("type");
		
		if (!StringUtil.isEmpty(type)) {
			if (TRANSFER.equals(type)) {
				filter.setTxnType(DISTRIBUTION_STOCK_TRANSFER);
				filter.setSeason(season);
			} else {
				filter.setTxnType(DISTRIBUTION_STOCK_RECEPTION);
				filter.setSeason(season);
			}
		}
		if (!StringUtil.isEmpty(senderWarehouse)) {
			Warehouse s = new Warehouse();
			s.setId(Long.valueOf(senderWarehouse));
			filter.setSenderWarehouse(s);
		}
		if (!StringUtil.isEmpty(receiverWarehouse)) {
			Warehouse s = new Warehouse();
			s.setId(Long.valueOf(receiverWarehouse));
			filter.setReceiverWarehouse(s);
		}
		if (!StringUtil.isEmpty(truckId))
			filter.setTruckId(truckId);
		
		if(!StringUtil.isEmpty(receiptNo))
			filter.setReceiptNo(receiptNo);
		
		if(!StringUtil.isEmpty(product))
			filter.setProductCode(Long.valueOf(product));
		
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
		Map data = readData();
		return sendJSONResponse(data);
		
	
	}
	
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		type = request.getParameter("type");
		int sno=0;
		
		if (obj instanceof DistributionStock){
	
		DistributionStock distribution = (DistributionStock) obj;
		
		
		if (TRANSFER.equalsIgnoreCase(type)){
		ESESystem preferences = preferncesService.findPrefernceById("1");
		
		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(!StringUtil
						.isEmpty(getBranchesMap().get(getParentBranchMap().get(distribution.getBranchId())))
								? getBranchesMap().get(getParentBranchMap().get(distribution.getBranchId()))
								: getBranchesMap().get(distribution.getBranchId()));
			}
			rows.add(getBranchesMap().get(distribution.getBranchId()));
		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(branchesMap.get(distribution.getBranchId()));
			}
		}	
		if (!StringUtil.isEmpty(preferences)) {
			DateFormat genDate=new SimpleDateFormat(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
			rows.add(!ObjectUtil.isEmpty(genDate.format(distribution.getTxnTime()))
					? genDate.format(distribution.getTxnTime()).toString() : "");
			
			}
		
		rows.add(distribution.getSeason()!=null && !StringUtil.isEmpty(distribution.getSeason()) ? farmerService.findBySeasonCode(distribution.getSeason()):"" );
		rows.add(!StringUtil.isEmpty(distribution.getReceiptNo())?distribution.getReceiptNo():"NA");
		rows.add(!StringUtil.isEmpty(distribution.getSenderWarehouse().getName())
				? distribution.getSenderWarehouse().getName()+" - "+distribution.getSenderWarehouse().getBranchId(): "NA");
		
		rows.add(!StringUtil.isEmpty(distribution.getReceiverWarehouse().getName())
				? distribution.getReceiverWarehouse().getName()+" - "+distribution.getReceiverWarehouse().getBranchId(): "NA");
		
		rows.add(!StringUtil.isEmpty(distribution.getTruckId())?distribution.getTruckId():"NA");
		rows.add(!StringUtil.isEmpty(distribution.getDriverName())?distribution.getDriverName():"NA");
		Double totQty=0.0;
	    totQty=distribution.getDistributionStockDetails().stream().mapToDouble(DistributionStockDetail::getDistributionQuantity)
	      .sum();
	    rows.add(totQty);
	    if(!StringUtil.isEmpty(distribution.getStatus())&& distribution.getStatus()!=2){
	    rows.add("<button class='fa fa-trash' onclick=\"deleteDistribution('"
				+ distribution.getId() + "')\"></button>");
	    }
	    else{
	    	rows.add("<button class='fa fa-check' onclick=\"noDeleteDistribution('"
					+ distribution.getId() + "')\"></button>");
	    }
		jsonObject.put("id", distribution.getId());
		jsonObject.put("cell", rows);
		}else{

			ESESystem preferences = preferncesService.findPrefernceById("1");
			
			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(!StringUtil
							.isEmpty(getBranchesMap().get(getParentBranchMap().get(distribution.getBranchId())))
									? getBranchesMap().get(getParentBranchMap().get(distribution.getBranchId()))
									: getBranchesMap().get(distribution.getBranchId()));
				}
				rows.add(getBranchesMap().get(distribution.getBranchId()));
			} else {
				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(branchesMap.get(distribution.getBranchId()));
				}
			}	
			if (!StringUtil.isEmpty(preferences)) {
				DateFormat genDate=new SimpleDateFormat(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
				rows.add(!ObjectUtil.isEmpty(genDate.format(distribution.getTxnTime()))
						? genDate.format(distribution.getTxnTime()).toString() : "");
				
				}
		
			rows.add(distribution.getSeason()!=null && !StringUtil.isEmpty(distribution.getSeason()) ? farmerService.findSeasonNameByCode(distribution.getSeason()).getName():"" );
			
			rows.add(!StringUtil.isEmpty(distribution.getReceiptNo())?distribution.getReceiptNo():"NA");
			rows.add(!StringUtil.isEmpty(distribution.getSenderWarehouse().getName())
					? distribution.getSenderWarehouse().getName()+" - "+distribution.getSenderWarehouse().getBranchId(): "NA");
			
			rows.add(!StringUtil.isEmpty(distribution.getReceiverWarehouse().getName())
					? distribution.getReceiverWarehouse().getName()+" - "+distribution.getReceiverWarehouse().getBranchId(): "NA");
			
			rows.add(!StringUtil.isEmpty(distribution.getTruckId())?distribution.getTruckId():"NA");
			rows.add(!StringUtil.isEmpty(distribution.getDriverName())?distribution.getDriverName():"NA");
			Double totQty=0.0;
		    totQty=distribution.getDistributionStockDetails().stream().mapToDouble(DistributionStockDetail::getDistributionQuantity)
		      .sum();
		    rows.add(totQty);
			jsonObject.put("id", distribution.getId());
			jsonObject.put("cell", rows);
			
		}
		}
		else if(obj instanceof DistributionStockDetail){
			DistributionStockDetail distriStockDetail = (DistributionStockDetail) obj;
			DecimalFormat df = new DecimalFormat("0.0000");
			if (TRANSFER.equalsIgnoreCase(type)){
			rows.add(!StringUtil.isEmpty(distriStockDetail.getProduct().getSubcategory())?distriStockDetail.getProduct().getSubcategory().getName():"NA");
			rows.add(!StringUtil.isEmpty(distriStockDetail.getProduct().getName())? distriStockDetail.getProduct().getName(): "NA");
			rows.add(!StringUtil.isEmpty(distriStockDetail.getProduct().getUnit())? distriStockDetail.getProduct().getUnit(): "NA");
			rows.add(CurrencyUtil.getDecimalFormat(distriStockDetail.getDistributionQuantity(),"##.000"));
			
			jsonObject.put("id", distriStockDetail.getId());
			jsonObject.put("cell", rows);
			}else{

				rows.add(!StringUtil.isEmpty(distriStockDetail.getProduct().getSubcategory())?distriStockDetail.getProduct().getSubcategory().getName():"NA");
				rows.add(!StringUtil.isEmpty(distriStockDetail.getProduct().getName())? distriStockDetail.getProduct().getName(): "NA");
				rows.add(!StringUtil.isEmpty(distriStockDetail.getProduct().getUnit())? distriStockDetail.getProduct().getUnit(): "NA");
				rows.add(CurrencyUtil.getDecimalFormat(distriStockDetail.getDistributionQuantity(),"##.000"));
				rows.add(CurrencyUtil.getDecimalFormat(distriStockDetail.getDamageQuantity(),"##.000"));

				jsonObject.put("id", distriStockDetail.getId());
				jsonObject.put("cell", rows);
				
			}
		}
		
		return jsonObject;
	}
	
	public String subGridDetail() throws Exception {

		DistributionStockDetail distriStockDetail = new DistributionStockDetail();
		if (ObjectUtil.isEmpty(this.filter))
	    distriStockDetail.setDistributionStock(new DistributionStock());
		distriStockDetail.getDistributionStock().setId(Long.valueOf(id));
		
		if(!StringUtil.isEmpty(product)){
			distriStockDetail.setProduct(new Product());
			distriStockDetail.getProduct().setId(Long.valueOf(product));}

		super.filter = distriStockDetail;
		Map data = readData();
		return sendJSONResponse(data);

	}
	
	public String populateDelete(){
		if (distId != null && !distId.equals("")) {
			
			distributionStock = productDistributionService.findDistributionStockById(Long.valueOf(distId));
		
			if(!ObjectUtil.isEmpty(distributionStock)){
				
				if(!StringUtil.isEmpty(distributionStock.getStatus())&& distributionStock.getStatus()!=2){
				
			for (DistributionStockDetail dsd : distributionStock.getDistributionStockDetails()) {
				
				WarehouseProduct wp = productService.findWarehouseProductbyIdAndSeasonCode(Long.valueOf(distributionStock.getSenderWarehouse().getId()),
						dsd.getProduct().getId(),distributionStock.getSeason());
					if(!ObjectUtil.isEmpty(wp)){
						Double updatedStock = wp.getStock()+dsd.getDistributionQuantity();
						wp.setStock(updatedStock);
						wp.setRevisionNo(DateUtil.getRevisionNumber());
						farmerService.update(wp);
					}
			}
			
			List<AgroTransaction> agroTxnList=productService.listAgroTransactionByDistributionStockId(distributionStock.getId(),distributionStock.getReceiptNo(),distributionStock.getTxnType(),distributionStock.getSeason());
			if(agroTxnList.size()>0){
				for(AgroTransaction ax : agroTxnList){
				productDistributionService.deleteAgroTxnById(ax.getId());
				}
			}
						
				if(!StringUtil.isEmpty(distributionStock.getReceiptNo())){
					DistributionStock ds= productDistributionService.findTransferDistributionStockByReceiptNumber(distributionStock.getReceiptNo());
					List<DistributionStockDetail> dsd = productDistributionService.findDistributionStOckDetailByDistributionId(Long.valueOf(ds.getId()));
					
			
					productDistributionService.delete(ds);
				}
				
				
				
				}else{
					addActionError(getText("cantDelete"));
					return LIST;
				}
			}
			
		}
		return LIST;
	}
	
	public void populateSeasonList(){
		JSONArray seasonArr=new JSONArray();
		List<Object[]> seasonList=farmerService.listSeasonCodeAndName();
		if(!ObjectUtil.isListEmpty(seasonList)){
			seasonList.stream().forEach(obj->{
				seasonArr.add(getJSONObject(obj[0],obj[1]));
			});
		}
		 sendAjaxResponse(seasonArr);
	}
	
    public void populateWarehouseList (){
    	JSONArray warehousefArr = new JSONArray();
    	List<Warehouse> warehouseList = locationService.listOfCooperatives();
  	  if (!ObjectUtil.isEmpty(warehouseList)) {
  		warehouseList.forEach(obj -> {
  			warehousefArr.add(getJSONObject(obj.getId(), obj.getName()));
  	   });
  	  }
  	  sendAjaxResponse(warehousefArr);
    }
    
    public void populateProductList(){
    	String pType="";
    	JSONArray productArr = new JSONArray();
    	if (TRANSFER.equalsIgnoreCase(type)){
    		pType=DistributionStock.DISTRIBUTION_STOCK_TRANSFER;
    	}
    	else{
    		pType=DistributionStock.DISTRIBUTION_STOCK_RECEPTION;
    	}
    	List<Object[]> productList = productService.listOfDistributionProductsByType(pType);
    	if (!ObjectUtil.isEmpty(productList)) {
    		productList.forEach(obj -> {
    			productArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
    		});
    	}
    	sendAjaxResponse(productArr);
    }
    
	public String populateXLS() throws Exception {

		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(
				getText("DistributionStockTransferReportList" + type).replace(" ", "_") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(
				FileUtil.createFileInputStreamToZipFile(getText("distributionStockTransferReportList" + type), fileMap, ".xls"));
		return "xls";
	}


	public String getXlsFileName() {
		return xlsFileName;
	}

	public void setXlsFileName(String xlsFileName) {
		this.xlsFileName = xlsFileName;
	}

	public void setFileInputStream(InputStream fileInputStream) {

		this.fileInputStream = fileInputStream;
	}
	public InputStream getFileInputStream() {

		return fileInputStream;
	}
	
	@SuppressWarnings("unchecked")
	public InputStream getExportDataStream(String exportType) throws IOException {
		Long serialNumber = 0L;

		Long serialNumber1 = 0L;
		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		setsDate(IExporter.EXPORT_DAILY.equalsIgnoreCase(exportType) ? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));
		boolean flag = true;
		DecimalFormat df = new DecimalFormat("0.000");
		DateFormat filterDateFormat = new SimpleDateFormat(getGeneralDateFormat());

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("exportDistributionTitle" + type));
		HSSFPatriarch drawing = sheet.createDrawingPatriarch();

		HSSFCellStyle style1 = wb.createCellStyle();
		HSSFCellStyle style2 = wb.createCellStyle();
		HSSFCellStyle style3 = wb.createCellStyle();
		HSSFCellStyle style4 = wb.createCellStyle();
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
				filterRow6 = null,	filterRow7 = null,	filterRow8 = null,	filterRow9 = null,filterRow10 = null,filterRow11 = null,filterRow15 = null,filterRow12 = null;
		HSSFCell cell;

		int imgRow1 = 0;
		int imgRow2 = 4;
		int imgCol1 = 0;
		int imgCol2 = 1;
		int titleRow1 = 2;
		int titleRow2 = 5;

		int rowNum = 3;
		int colNum = 0;
		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap(); // build branch map to get branch name form branch id.

		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, titleRow1, titleRow2));

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(2);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("exportDistributionTitle" + type)));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);
		int count=0;
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
			
		}
		filterRow15=sheet.createRow(rowNum++);
		if (season!=null && !StringUtil.isEmpty(season)) {
			cell = filterRow15.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("season")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);

			cell = filterRow15.createCell(2);
			cell.setCellValue(new HSSFRichTextString(farmerService.findBySeasonCode(season)));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);
		}
		
			filterRow4 = sheet.createRow(rowNum++);
			filterRow5 = sheet.createRow(rowNum++);
			filterRow6 = sheet.createRow(rowNum++);
			filterRow7 = sheet.createRow(rowNum++);
			filterRow8 = sheet.createRow(rowNum++);
			filterRow9 = sheet.createRow(rowNum++);
			filterRow10 = sheet.createRow(rowNum++);
			filterRow11 = sheet.createRow(rowNum++);
			filterRow12 = sheet.createRow(rowNum++);
			
			
			HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
			// String mainGridColumnHeaders = getLocaleProperty("exportColumnHeader" +
			// type);
			type = request.getParameter("type");
			String mainGridColumnHeaders = "";
			
			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
				if (StringUtil.isEmpty(branchIdValue)) {
					mainGridColumnHeaders = getLocaleProperty("ExportColumnHeadingBranch" + type);
				} else if (!StringUtil.isEmpty(branchIdValue)) {
					mainGridColumnHeaders = getLocaleProperty("ExportColumnHeadingBranch" + type);
				}
			} else if(getCurrentTenantId().equalsIgnoreCase("pratibha")){
				
				if (StringUtil.isEmpty(branchIdValue)) {
					mainGridColumnHeaders = getLocaleProperty("exportColumnHeaderDistributionStockBranchPratibha"+ type);
				}else if(getBranchId().equalsIgnoreCase("organic")){
					mainGridColumnHeaders = getLocaleProperty("OrganiDistributionStockExportHeader"+ type);
				}else if(getBranchId().equalsIgnoreCase("bci")){
					mainGridColumnHeaders = getLocaleProperty("BCIDistributionStockExportHeader"+ type);
				}
				
			}else{
				if (StringUtil.isEmpty(branchIdValue)) {
					mainGridColumnHeaders = getLocaleProperty("exportColumnHeaderDistributionStockBranch"+ type);
				} else {
					mainGridColumnHeaders = getLocaleProperty("exportColumnHeaderDistributionStock"+ type);
				}	
			}

			int mainGridIterator = 0;
			for (String cellHeader : mainGridColumnHeaders.split("\\,")) {
				if(cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT))		
				{		
					cellHeader=cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());		
				}else if(cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT))				
				{				
					cellHeader=cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());				
				}	
				if (StringUtil.isEmpty(branchIdValue)) { // Add branch header to
															// export sheet if main
															// branch logged in.
					cell = mainGridRowHead.createCell(mainGridIterator);
					cell.setCellValue(new HSSFRichTextString(cellHeader));
					style2.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
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
						style2.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
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
			
			setFilter(new DistributionStock());
			Map data= new HashMap<>();
			
			if (!StringUtil.isEmpty(type)) {
				if (TRANSFER.equals(type)) {
					filter.setTxnType(DISTRIBUTION_STOCK_TRANSFER);
					filter.setSeason(season);
				} else {
					filter.setTxnType(DISTRIBUTION_STOCK_RECEPTION);
					filter.setSeason(season);
				}
			}
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
		
			if (!StringUtil.isEmpty(senderWarehouse)) {
				Warehouse s = new Warehouse();
				s.setId(Long.valueOf(senderWarehouse));
				filter.setSenderWarehouse(s);
			}
			if (!StringUtil.isEmpty(receiverWarehouse)) {
				Warehouse s = new Warehouse();
				s.setId(Long.valueOf(receiverWarehouse));
				filter.setReceiverWarehouse(s);
			}
			if (!StringUtil.isEmpty(truckId))
				filter.setTruckId(truckId);
			
			if(!StringUtil.isEmpty(receiptNo))
				filter.setReceiptNo(receiptNo);
			
			if(!StringUtil.isEmpty(product))
				filter.setProductCode(Long.valueOf(product));
            
            if (!StringUtil.isEmpty(branchIdParma)) { // set filter of branch id and
													// check it.
			filter.setBranchId(branchIdParma);
		    }
            super.filter = this.filter;
        	data = readData();
		
		
			List<DistributionStock> mainGridRows = (List<DistributionStock>) data.get(ROWS);
			if (ObjectUtil.isListEmpty(mainGridRows))
				return null;
		if (TRANSFER.equalsIgnoreCase(type)){
			for (DistributionStock stockDistribution : mainGridRows) {
				if ((!StringUtil.isEmpty(receiptNo) || !StringUtil.isEmpty(senderWarehouse) || !StringUtil.isEmpty(receiverWarehouse)
						|| !StringUtil.isEmpty(truckId) || !StringUtil.isEmpty(truckId) || (!StringUtil.isEmpty(product)) || (!StringUtil.isEmpty(branchIdParma))&& flag)) {     
              

					if (!StringUtil.isEmpty(senderWarehouse)) {

						cell = filterRow5.createCell(1);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("senderWarehouse")));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						cell = filterRow5.createCell(2);
						cell.setCellValue(new HSSFRichTextString(stockDistribution.getSenderWarehouse().getName()));
						
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						if (StringUtil.isEmpty(senderWarehouse)) {
							sheet.shiftRows(filterRow4.getRowNum() + 1, filterRow5.getRowNum() + 1, -1);
						}
					}

					if (!StringUtil.isEmpty(receiverWarehouse)) {
						//filterRow6 = sheet.createRow(rowNum++);
						cell = filterRow6.createCell(1);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("receiverWarehouse")));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						cell = filterRow6.createCell(2);
						cell.setCellValue(new HSSFRichTextString(stockDistribution.getReceiverWarehouse().getName()));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

					}

					if (!StringUtil.isEmpty(receiptNo)) {

						cell = filterRow9.createCell(1);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("receiptNo")));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						cell = filterRow9.createCell(2);
						cell.setCellValue(new HSSFRichTextString(stockDistribution.getReceiptNo()));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						if (StringUtil.isEmpty(receiptNo)) {
							sheet.shiftRows(filterRow4.getRowNum() + 1, filterRow5.getRowNum() + 1, -1);
						}
					}
					
					
					if (!StringUtil.isEmpty(product)) {
						cell = filterRow7.createCell(1);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("product")));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						cell = filterRow7.createCell(2);
						cell.setCellValue(new HSSFRichTextString(productService.findProductById(filter.getProductCode()).getName()));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);
					}
					
					
					if (!StringUtil.isEmpty(truckId)) {
						cell = filterRow8.createCell(1);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("truckId")));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						cell = filterRow8.createCell(2);
						cell.setCellValue(new HSSFRichTextString(stockDistribution.getTruckId()));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);
					}
					
					
					
					  if (!StringUtil.isEmpty(branchIdParma)) {
	        				cell = filterRow12.createCell(1);
	        				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("BranchId")));
	        				filterFont.setBoldweight((short) 12);
	        				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	        				filterStyle.setFont(filterFont);
	        				cell.setCellStyle(filterStyle);

	        				cell = filterRow12.createCell(2);
	        				cell.setCellValue(new HSSFRichTextString(branchIdParma));
	        				filterFont.setBoldweight((short) 12);
	        				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	        				filterStyle.setFont(filterFont);
	        				cell.setCellStyle(filterStyle);

	        			}
                    
					flag = false;

				}
				
				row = sheet.createRow(rowNum++);

				colNum = 0;
				
				
				serialNumber1++;
				cell = row.createCell(colNum++);
				cell.setCellValue(serialNumber1);
				

					if ((getIsMultiBranch().equalsIgnoreCase("1")
							&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
						if (StringUtil.isEmpty(branchIdValue)) {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(!StringUtil
									.isEmpty(getBranchesMap().get(getParentBranchMap().get(stockDistribution.getBranchId())))
											? getBranchesMap().get(getParentBranchMap().get(stockDistribution.getBranchId()))
											: getBranchesMap().get(stockDistribution.getBranchId())));
						}
						cell = row.createCell(colNum++);
						cell.setCellValue(getBranchesMap().get(stockDistribution.getBranchId()));
					}

					else {
						if (StringUtil.isEmpty(branchIdValue)) {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(stockDistribution.getBranchId())
									? (branchesMap.get(stockDistribution.getBranchId())) : "N/A"));
						}
					}

		

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							!ObjectUtil.isEmpty(stockDistribution) ? (!ObjectUtil.isEmpty(stockDistribution.getTxnTime())
									? DateUtil.convertDateToString(stockDistribution.getTxnTime(), getGeneralDateFormat())
									: "") : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(stockDistribution.getSeason()!=null ?farmerService.findBySeasonCode(stockDistribution.getSeason()):""));
				      
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(stockDistribution.getReceiptNo()));
				       
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(stockDistribution.getSenderWarehouse().getName()+" - "+stockDistribution.getSenderWarehouse().getBranchId()));

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(stockDistribution.getReceiverWarehouse().getName()+" - "+stockDistribution.getReceiverWarehouse().getBranchId()));
						
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(stockDistribution.getTruckId()));
						
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(stockDistribution.getDriverName()));
				
						Double totQty =0.0;
					    totQty=stockDistribution.getDistributionStockDetails().stream().mapToDouble(DistributionStockDetail::getDistributionQuantity)
					      .sum();
                        cell=row.createCell(colNum++);
                        cell.setCellValue(new HSSFRichTextString(totQty.toString()));
                        
				HSSFRow subGridRowHead = sheet.createRow(rowNum++);
				String sunGridcolumnHeaders = getLocaleProperty("exportSubColumnHeaderDistribution" + type);

				int subGridIterator = 1;

				
				for (String cellHeader : sunGridcolumnHeaders.split("\\,")) {
					if(cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT))
					{
						cellHeader=cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
					}
					else
					{
						cellHeader=cellHeader.trim();
					}
					cell = subGridRowHead.createCell(subGridIterator);
					cell.setCellValue(new HSSFRichTextString(cellHeader));
					style3.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
					style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					cell.setCellStyle(style3);
					font3.setBoldweight((short) 10);
					font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					style3.setFont(font3);
					subGridIterator++;
				}
				
				for (DistributionStockDetail distributionStockDetail : stockDistribution.getDistributionStockDetails()) {
				 
						row = sheet.createRow(rowNum++);
						colNum = 1;
     
						cell = row.createCell(colNum++);
						cell.setCellValue(
								new HSSFRichTextString(distributionStockDetail.getProduct().getSubcategory() == null ? ""
										: distributionStockDetail.getProduct().getSubcategory().getName()));

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(distributionStockDetail.getProduct() == null ? ""
								: distributionStockDetail.getProduct().getName()));

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(distributionStockDetail.getProduct() == null ? ""
								: distributionStockDetail.getProduct().getUnit()));

			
						cell = row.createCell(colNum++);
						cell.setCellValue(
								Double.valueOf(distributionStockDetail.getDistributionQuantity()));

				
				}
				row = sheet.createRow(rowNum++);
			}	
		}else{
			for (DistributionStock stockDistribution : mainGridRows) {
				if ((!StringUtil.isEmpty(receiptNo) || !StringUtil.isEmpty(senderWarehouse) || !StringUtil.isEmpty(receiverWarehouse)
						|| !StringUtil.isEmpty(truckId) || !StringUtil.isEmpty(truckId) || (!StringUtil.isEmpty(product)) || (!StringUtil.isEmpty(branchIdParma))&& flag)) {     
              
    			
					if (!StringUtil.isEmpty(senderWarehouse)) {

						cell = filterRow5.createCell(1);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("senderWarehouse")));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						cell = filterRow5.createCell(2);
						cell.setCellValue(new HSSFRichTextString(stockDistribution.getSenderWarehouse().getName()));
						
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						if (StringUtil.isEmpty(senderWarehouse)) {
							sheet.shiftRows(filterRow4.getRowNum() + 1, filterRow5.getRowNum() + 1, -1);
						}
					}

					if (!StringUtil.isEmpty(receiverWarehouse)) {
						//filterRow6 = sheet.createRow(rowNum++);
						cell = filterRow6.createCell(1);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("receiverWarehouse")));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						cell = filterRow6.createCell(2);
						cell.setCellValue(new HSSFRichTextString(stockDistribution.getReceiverWarehouse().getName()));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

					}

					if (!StringUtil.isEmpty(receiptNo)) {

						cell = filterRow6.createCell(1);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("receiptNo")));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						cell = filterRow6.createCell(2);
						cell.setCellValue(new HSSFRichTextString(stockDistribution.getReceiptNo()));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						if (StringUtil.isEmpty(receiptNo)) {
							sheet.shiftRows(filterRow4.getRowNum() + 1, filterRow5.getRowNum() + 1, -1);
						}
					}
					
					
					if (!StringUtil.isEmpty(product)) {
						cell = filterRow7.createCell(1);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("product")));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						cell = filterRow7.createCell(2);
						cell.setCellValue(new HSSFRichTextString(productService.findProductById(filter.getProductCode()).getName()));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);
					}
					
					
					if (!StringUtil.isEmpty(truckId)) {
						cell = filterRow8.createCell(1);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("truckId")));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						cell = filterRow8.createCell(2);
						cell.setCellValue(new HSSFRichTextString(stockDistribution.getTruckId()));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);
					}
                    
					flag = false;

				}
				
				row = sheet.createRow(rowNum++);

				colNum = 0;
				
				
				serialNumber1++;
				cell = row.createCell(colNum++);
				cell.setCellValue(serialNumber1);
				

					if ((getIsMultiBranch().equalsIgnoreCase("1")
							&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
						if (StringUtil.isEmpty(branchIdValue)) {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(!StringUtil
									.isEmpty(getBranchesMap().get(getParentBranchMap().get(stockDistribution.getBranchId())))
											? getBranchesMap().get(getParentBranchMap().get(stockDistribution.getBranchId()))
											: getBranchesMap().get(stockDistribution.getBranchId())));
						}
						cell = row.createCell(colNum++);
						cell.setCellValue(getBranchesMap().get(stockDistribution.getBranchId()));
					}

					else {
						if (StringUtil.isEmpty(branchIdValue)) {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(stockDistribution.getBranchId())
									? (branchesMap.get(stockDistribution.getBranchId())) : "N/A"));
						}
					}

		

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							!ObjectUtil.isEmpty(stockDistribution) ? (!ObjectUtil.isEmpty(stockDistribution.getTxnTime())
									? DateUtil.convertDateToString(stockDistribution.getTxnTime(), getGeneralDateFormat())
									: "") : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(stockDistribution.getSeason()!=null ?farmerService.findBySeasonCode(stockDistribution.getSeason()):""));
				    
				      
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(stockDistribution.getReceiptNo()));
				       
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(stockDistribution.getSenderWarehouse().getName()+" - "+stockDistribution.getSenderWarehouse().getBranchId()));

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(stockDistribution.getReceiverWarehouse().getName()+" - "+stockDistribution.getReceiverWarehouse().getBranchId()));
						
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(stockDistribution.getTruckId()));
						
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(stockDistribution.getDriverName()));
				
						Double totQty =0.0;
					    totQty=stockDistribution.getDistributionStockDetails().stream().mapToDouble(DistributionStockDetail::getDistributionQuantity)
					      .sum();
                        cell=row.createCell(colNum++);
                        cell.setCellValue(new HSSFRichTextString(totQty.toString()));
                        
				HSSFRow subGridRowHead = sheet.createRow(rowNum++);
				String sunGridcolumnHeaders = getLocaleProperty("exportSubColumnHeaderDistribution" + type);

				int subGridIterator = 1;

				
				for (String cellHeader : sunGridcolumnHeaders.split("\\,")) {
					if(cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT))
					{
						cellHeader=cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
					}
					else
					{
						cellHeader=cellHeader.trim();
					}
					cell = subGridRowHead.createCell(subGridIterator);
					cell.setCellValue(new HSSFRichTextString(cellHeader));
					style3.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
					style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					cell.setCellStyle(style3);
					font3.setBoldweight((short) 10);
					font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					style3.setFont(font3);
					subGridIterator++;
				}
				
				for (DistributionStockDetail distributionStockDetail : stockDistribution.getDistributionStockDetails()) {
				 
						row = sheet.createRow(rowNum++);
						colNum = 1;
     
						cell = row.createCell(colNum++);
						cell.setCellValue(
								new HSSFRichTextString(distributionStockDetail.getProduct().getSubcategory() == null ? ""
										: distributionStockDetail.getProduct().getSubcategory().getName()));

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(distributionStockDetail.getProduct() == null ? ""
								: distributionStockDetail.getProduct().getName()));

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(distributionStockDetail.getProduct() == null ? ""
								: distributionStockDetail.getProduct().getUnit()));

			
						cell = row.createCell(colNum++);
						cell.setCellValue(
								Double.valueOf(distributionStockDetail.getDistributionQuantity()));

						cell = row.createCell(colNum++);
						cell.setCellValue(
								Double.valueOf(distributionStockDetail.getDamageQuantity()));
				}
				row = sheet.createRow(rowNum++);
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
			String fileName = getLocaleProperty("distributionReportList" + type) + fileNameDateFormat.format(new Date()) + ".xls";
			FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
			wb.write(fileOut);
			InputStream stream = new FileInputStream(new File(makeDir + fileName));
			fileOut.close();

		return stream;
     
	}
	public String getExportLimit() {
		return exportLimit;
	}

	public void setExportLimit(String exportLimit) {
		this.exportLimit = exportLimit;
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

	public DistributionStock getDistributionStock() {
		return distributionStock;
	}

	public void setDistributionStock(DistributionStock distributionStock) {
		this.distributionStock = distributionStock;
	}

	public DistributionStock getFilter() {
		return filter;
	}

	public void setFilter(DistributionStock filter) {
		this.filter = filter;
	}

	public ILocationService getLocationService() {
		return locationService;
	}

	public void setLocationService(ILocationService locationService) {
		this.locationService = locationService;
	}

	public IProductService getProductService() {
		return productService;
	}

	public void setProductService(IProductService productService) {
		this.productService = productService;
	}

	public static String getTransfer() {
		return TRANSFER;
	}

	public static String getReception() {
		return RECEPTION;
	}


	public String getReceiverWarehouse() {
		return receiverWarehouse;
	}


	public void setReceiverWarehouse(String receiverWarehouse) {
		this.receiverWarehouse = receiverWarehouse;
	}


	public String getSenderWarehouse() {
		return senderWarehouse;
	}


	public void setSenderWarehouse(String senderWarehouse) {
		this.senderWarehouse = senderWarehouse;
	}


	public String getReceiptNo() {
		return receiptNo;
	}


	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}


	public String getTruckId() {
		return truckId;
	}


	public void setTruckId(String truckId) {
		this.truckId = truckId;
	}


	public String getProduct() {
		return product;
	}


	public void setProduct(String product) {
		this.product = product;
	}
	
	
	public String getBranchIdParma() {
		return branchIdParma;
	}


	public void setBranchIdParma(String branchIdParma) {
		this.branchIdParma = branchIdParma;
	}


	public int getPicIndex(HSSFWorkbook wb) throws IOException {

		int index = -1;

		byte[] picData = null;
		picData = clientService.findLogoByCode(Asset.APP_LOGO);

		if (picData != null)
			index = wb.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}
	
	public void prepare() throws Exception {

		type = request.getParameter("type");
		if (!StringUtil.isEmpty(type)) {
			String actionClassName = ActionContext.getContext().getActionInvocation().getAction().getClass()
					.getSimpleName();
			String content = getLocaleProperty(actionClassName + "." + BreadCrumb.BREADCRUMB+type);
			if (StringUtil.isEmpty(content) || (content.equalsIgnoreCase(actionClassName + "." + BreadCrumb.BREADCRUMB+type))) {
				content = super.getText(BreadCrumb.BREADCRUMB+type, "");
			}
			request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(content));
			
		}else{
			String actionClassName = ActionContext.getContext().getActionInvocation().getAction().getClass()
					.getSimpleName();
			String content = getLocaleProperty(actionClassName + "." + BreadCrumb.BREADCRUMB);
			if (StringUtil.isEmpty(content) || (content.equalsIgnoreCase(actionClassName + "." + BreadCrumb.BREADCRUMB))) {
				content = super.getText(BreadCrumb.BREADCRUMB, "");
			}
			request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(content));
		}
	}


	public IFarmerService getFarmerService() {
		return farmerService;
	}


	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}


	public String getSeason() {
		return season;
	}


	public void setSeason(String season) {
		this.season = season;
	}


	public String getDistId() {
		return distId;
	}


	public void setDistId(String distId) {
		this.distId = distId;
	}


	public IProductDistributionService getProductDistributionService() {
		return productDistributionService;
	}


	public void setProductDistributionService(IProductDistributionService productDistributionService) {
		this.productDistributionService = productDistributionService;
	}
	
	
}
