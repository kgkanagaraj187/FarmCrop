package com.sourcetrace.esesw.view.report.agro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.profile.CropHarvestDetails;
import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.entity.WarehouseProduct;
import com.sourcetrace.eses.service.CatalogueService;
import com.sourcetrace.eses.service.ICatalogueService;
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
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.Vendor;
import com.sourcetrace.esesw.entity.profile.WarehousePayment;
import com.sourcetrace.esesw.entity.profile.WarehousePaymentDetails;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

// TODO: Auto-generated Javadoc
@SuppressWarnings({ "unchecked", "serial" })
public class WarehouseStockEntryReportAction extends BaseReportAction {

	private static final Logger LOGGER = Logger.getLogger(WarehouseStockEntryReportAction.class);
	private Map<String, String> fields = new HashMap<>();
	private WarehousePaymentDetails filter;
	SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
	private String warehouse;
	private String order;
	private ILocationService locationService;
	private ICatalogueService catalogueService;
	@Autowired
	private IProductDistributionService productDistributionService;
	private String vendor;
	private String receipt;
	private String branchIdParma;
	private String daterange;
	private String seasonCode;
	private IFarmerService farmerService;
	private String enableBatchNo;
	private IPreferencesService preferncesService;
	private String xlsFileName;
	private InputStream fileInputStream;
	private WarehouseProduct warehouseProduct;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private String selectedProduct;
	private String totalQty;

	public String getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(String totalQty) {
		this.totalQty = totalQty;
	}

	@Autowired
	private IProductService productService;

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

		ESESystem preferences = preferncesService.findPrefernceById("1");
		setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));
		request.setAttribute(HEADING, getText(LIST));
		return LIST;
	}

	public String detail() throws Exception {

		if (ObjectUtil.isEmpty(this.filter))
			this.filter = new WarehousePaymentDetails();
		if (!StringUtil.isEmpty(warehouse)) {
			WarehousePayment wpa = new WarehousePayment();
			Warehouse w = new Warehouse();
			w.setCode(warehouse);
			wpa.setWarehouse(w);
			filter.setWarehousePayment(wpa);
			// filter.getWarehousePayment().getWarehouse().setCode(warehouse);
		}

		if (!StringUtil.isEmpty(order)) {

			if (filter.getWarehousePayment() != null) {
				filter.getWarehousePayment().setOrderNo(order);
			} else {
				WarehousePayment wpa = new WarehousePayment();
				wpa.setOrderNo(order);
				filter.setWarehousePayment(wpa);
			}
		}

		if (!StringUtil.isEmpty(vendor)) {
			if (filter.getWarehousePayment() != null) {
				Vendor vr = new Vendor();
				vr.setVendorId(vendor);
				filter.getWarehousePayment().setVendor(vr);
			} else {
				WarehousePayment wpa = new WarehousePayment();
				Vendor vr = new Vendor();
				vr.setVendorId(vendor);
				wpa.setVendor(vr);
				filter.setWarehousePayment(wpa);
				// filter.getWarehousePayment().getVendor().setVendorId(vendor);
			}
		}
		if (!StringUtil.isEmpty(receipt)) {

			if (filter.getWarehousePayment() != null) {
				filter.getWarehousePayment().setReceiptNo(receipt);
			} else {
				WarehousePayment wpa = new WarehousePayment();
				wpa.setReceiptNo(receipt);
				filter.setWarehousePayment(wpa);
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

		if (ObjectUtil.isEmpty(filter.getWarehousePayment())) {
			filter.setWarehousePayment(new WarehousePayment());
		}
		/*
		 * if(!getCurrentTenantId().equalsIgnoreCase("lalteer")){
		 * filter.getWarehousePayment().setSeasonCode(getCurrentSeasonsCode());
		 * }
		 */
		if (!StringUtil.isEmpty(seasonCode)) {
			filter.getWarehousePayment().setSeasonCode(seasonCode);
		}

		if (!StringUtil.isEmpty(selectedProduct)) {
			Product product = new Product();
			product.setId(Long.valueOf(selectedProduct));
			filter.setProduct(product);
		}

		// this.filter.setDesc("STOCK ADDED");
		super.filter = this.filter;
		Map data = readData("warehouseStockEntry");
		return sendJSONResponse(data);
	}

	public String subGridDetail() throws Exception {

		if (ObjectUtil.isEmpty(this.filter))
			this.filter = new WarehousePaymentDetails();

		this.filter.setReceiptNo(this.id);
		// this.filter.setDesc("STOCK ADDED");
		if (!StringUtil.isEmpty(warehouse)) {
			WarehousePayment wpa = new WarehousePayment();
			Warehouse w = new Warehouse();
			w.setCode(warehouse);
			wpa.setWarehouse(w);
			filter.setWarehousePayment(wpa);
			// filter.getWarehousePayment().getWarehouse().setCode(warehouse);
		}
		if (!StringUtil.isEmpty(selectedProduct)) {
			Product product = new Product();
			product.setId(Long.valueOf(selectedProduct));
			filter.setProduct(product);
		}
		if (!StringUtil.isEmpty(order)) {

			if (filter.getWarehousePayment() != null) {
				filter.getWarehousePayment().setOrderNo(order);
			} else {
				WarehousePayment wpa = new WarehousePayment();
				wpa.setOrderNo(order);
				filter.setWarehousePayment(wpa);
			}
		}

		if (!StringUtil.isEmpty(vendor)) {
			if (filter.getWarehousePayment() != null) {
				Vendor vr = new Vendor();
				vr.setVendorId(vendor);
				filter.getWarehousePayment().setVendor(vr);
			} else {
				WarehousePayment wpa = new WarehousePayment();
				Vendor vr = new Vendor();
				vr.setVendorId(vendor);
				wpa.setVendor(vr);
				filter.setWarehousePayment(wpa);
				// filter.getWarehousePayment().getVendor().setVendorId(vendor);
			}
		}
		if (!StringUtil.isEmpty(receipt)) {

			if (filter.getWarehousePayment() != null) {
				filter.getWarehousePayment().setReceiptNo(receipt);
			} else {
				WarehousePayment wpa = new WarehousePayment();
				wpa.setReceiptNo(receipt);
				filter.setWarehousePayment(wpa);
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

		if (ObjectUtil.isEmpty(filter.getWarehousePayment())) {
			filter.setWarehousePayment(new WarehousePayment());
		}
		/*
		 * if(!getCurrentTenantId().equalsIgnoreCase("lalteer")){
		 * filter.getWarehousePayment().setSeasonCode(getCurrentSeasonsCode());
		 * }
		 */
		if (!StringUtil.isEmpty(seasonCode)) {
			filter.getWarehousePayment().setSeasonCode(seasonCode);
		}
		super.filter = this.filter;
		Map data = readData();
		ESESystem preferences = preferncesService.findPrefernceById("1");
		setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));
		return sendJSONResponse(data);

	}

	public JSONObject toJSON(Object obj) {

		JSONObject jsonObject = new JSONObject();
		if (obj instanceof WarehousePaymentDetails) {
			WarehousePaymentDetails warehousePaymentDetails = (WarehousePaymentDetails) obj;
			JSONArray rows = new JSONArray();

			double goodStock = warehousePaymentDetails.getStock();
			double damStock = warehousePaymentDetails.getDamagedStock();
			double totalStock = goodStock + damStock;

			/*
			 * if (StringUtil.isEmpty(branchIdValue)) {
			 * rows.add(branchesMap.get(warehousePaymentDetails.getBranchId()));
			 * }
			 */
			/*
			 * if ((getIsMultiBranch().equalsIgnoreCase("1") &&
			 * (getIsParentBranch().equals("1")||StringUtil.isEmpty(
			 * branchIdValue)))) {
			 * 
			 * if (StringUtil.isEmpty(branchIdValue)) {
			 * rows.add(!StringUtil.isEmpty(getBranchesMap().get(
			 * getParentBranchMap().get(warehousePaymentDetails.getBranchId())))
			 * ? getBranchesMap().get(getParentBranchMap().get(
			 * warehousePaymentDetails.getBranchId())) :
			 * getBranchesMap().get(warehousePaymentDetails.getBranchId())); }
			 * rows.add(getBranchesMap().get(warehousePaymentDetails.getBranchId
			 * ()));
			 * 
			 * 
			 * } else { if (StringUtil.isEmpty(branchIdValue)) {
			 * rows.add(branchesMap.get(warehousePaymentDetails.getBranchId()));
			 * } }
			 */

			/*
			 * rows.add(warehousePaymentDetails.getProduct().getSubcategory().
			 * getCode() + "-" +
			 * warehousePaymentDetails.getProduct().getSubcategory().getName());
			 * 
			 * rows.add(warehousePaymentDetails.getProduct().getCode() + "-" +
			 * warehousePaymentDetails.getProduct().getName());
			 */

			rows.add(warehousePaymentDetails.getProduct().getSubcategory().getName());

			rows.add(warehousePaymentDetails.getProduct().getName());

			rows.add(warehousePaymentDetails.getProduct().getUnit());

			if (getEnableBatchNo().equalsIgnoreCase("1")) {

				rows.add(warehousePaymentDetails.getBatchNo());
			}

			ESESystem preferences = preferncesService.findPrefernceById("1");
			if (!StringUtil.isEmpty(preferences)) {
				// DateFormat genDate=new
				// SimpleDateFormat(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
				// rows.add(String.valueOf(genDate.format(warehousePaymentDetails.getExpDate())));
				// rows.add(String.valueOf(warehousePaymentDetails.getExpDate()));
			}

			// rows.add(warehousePaymentDetails.getCostPrice());

			rows.add(CurrencyUtil.getDecimalFormat(warehousePaymentDetails.getStock(), "##.00"));

			rows.add(CurrencyUtil.getDecimalFormat(warehousePaymentDetails.getDamagedStock(), "##.00"));

			// rows.add(totalStock);
			rows.add(CurrencyUtil.getDecimalFormat(totalStock, "##.00"));

			// rows.add(warehousePaymentDetails.getAmount());

			// rows.add(warehousePaymentDetails.getWarehousePayment().getSeasonCode());
			HarvestSeason season = farmerService
					.findSeasonNameByCode(warehousePaymentDetails.getWarehousePayment().getSeasonCode());
			/*
			 * rows.add(!ObjectUtil.isEmpty(season)?season.getCode()+"-"+season.
			 * getName():"");
			 */ // rows.add(!ObjectUtil.isEmpty(season) ? season.getName() : "");

			jsonObject.put("id", warehousePaymentDetails.getId());
			jsonObject.put("cell", rows);
		} else {

			Object[] datas = (Object[]) obj;
			JSONArray rows = new JSONArray();
			/*
			 * if (StringUtil.isEmpty(branchIdValue)) {
			 * rows.add(branchesMap.get(datas[12])); // BranchId }
			 */

			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(datas[13])))
							? getBranchesMap().get(getParentBranchMap().get(datas[13]))
							: getBranchesMap().get(datas[13]));
				}
				rows.add(getBranchesMap().get(datas[13]));
			} else {
				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(branchesMap.get(datas[13]));
				}
			}

			ESESystem preferences = preferncesService.findPrefernceById("1");
			if (!StringUtil.isEmpty(preferences)) {
				DateFormat genDate = new SimpleDateFormat(
						preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
				rows.add(!StringUtil.isEmpty(genDate.format(datas[1])) ? genDate.format(datas[1]) : ""); // Trxn
			} // Date

			// rows.add(datas[12]); // season code
			HarvestSeason season = farmerService.findSeasonNameByCode(String.valueOf(datas[12]));
			rows.add(!ObjectUtil.isEmpty(season) ? season.getName() : "");

			rows.add(datas[3]); // Order No
			rows.add(datas[5]); // Warehouse Code And Name
			rows.add(datas[0]); // Receipt No
			rows.add(datas[7]); // Vendor Id-Name
			/*
			 * if(!getCurrentTenantId().equalsIgnoreCase(ESESystem.
			 * SAGI_TENANT_ID) &&
			 * !getCurrentTenantId().equalsIgnoreCase(ESESystem.MOVCD_TENANT_ID)
			 * ){ if (datas[10].toString().equalsIgnoreCase("CS")) {
			 * rows.add(getText("cash")); } else if
			 * (datas[10].toString().equalsIgnoreCase("CR")) {
			 * rows.add(getText("credit")); }
			 * 
			 * rows.add(CurrencyUtil.getDecimalFormat((Double) datas[11],
			 * "##.000")); // amount }
			 */
			rows.add(CurrencyUtil.getDecimalFormat((Double) datas[8], "##.00"));// Total
																					// Qty

			/*
			 * rows.add(CurrencyUtil.getDecimalFormat((Double) datas[16],
			 * "##.00")); if (!StringUtil.isEmpty(datas[14].toString())) {
			 * rows.add(CurrencyUtil.getDecimalFormat((Double) datas[14],
			 * "##.00")); }
			 * 
			 * rows.add(CurrencyUtil.getDecimalFormat((Double) datas[9],
			 * "##.000"));// Final
			 */
			jsonObject.put("id", datas[0]);
			jsonObject.put("cell", rows);

		}
		return jsonObject;

	}

	/*
	 * public Map<String, String> getWarehouseList() {
	 * 
	 * Map<String, String> warehouseListMap = new LinkedHashMap<String,
	 * String>(); List<Warehouse> warehouseList =
	 * locationService.listWarehouse(); for (Warehouse obj : warehouseList) {
	 * warehouseListMap.put(obj.getCode(), obj.getName() + " - " +
	 * obj.getCode());
	 * 
	 * } return warehouseListMap; }
	 */

	/*
	 * public Map<String, String> getWarehouseList() { Map<String, String>
	 * warehouseMap = new LinkedHashMap<>(); List<Object[]> warehouseList =
	 * locationService.listOfWarehouseByStockEntry(); if
	 * (!ObjectUtil.isEmpty(warehouseList)) {
	 * 
	 * warehouseMap = warehouseList.stream() .collect(Collectors.toMap(obj ->
	 * (String.valueOf(obj[0])), obj -> String.valueOf(obj[1])));
	 * 
	 * } return warehouseMap; }
	 */

	public void populateWarehouseList() {
		JSONArray warehouseArr = new JSONArray();
		List<Object[]> warehouseList = locationService.listOfWarehouseByStockEntry();
		if (!ObjectUtil.isEmpty(warehouseList)) {
			warehouseList.forEach(obj -> {
				warehouseArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(warehouseArr);
	}

	public String populateXLS() throws Exception {
		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getLocaleProperty("Warehouse-StockEntryReport") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getLocaleProperty("warehouseStockReport"), fileMap, ".xls"));
		return "xls";
	}

	private InputStream getExportDataStream(String exportType) throws IOException {
		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		boolean flag = true;
		DateFormat filterDateFormat = new SimpleDateFormat(getGeneralDateFormat());
		DecimalFormat df = new DecimalFormat("0.000");
		DecimalFormat df1 = new DecimalFormat("0.00");

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getText("warehouseStockReportTitle"));
		HSSFPatriarch drawing = sheet.createDrawingPatriarch();

		HSSFCellStyle style1 = wb.createCellStyle();
		HSSFCellStyle style2 = wb.createCellStyle();
		HSSFCellStyle style3 = wb.createCellStyle();
		HSSFCellStyle style4 = wb.createCellStyle();
		HSSFCellStyle style5 = wb.createCellStyle();
		//HSSFCellStyle cellStyle = wb.createCellStyle();
		HSSFCellStyle filterStyle = wb.createCellStyle();

		HSSFFont font1 = wb.createFont();
		font1.setFontHeightInPoints((short) 22);

		HSSFFont font2 = wb.createFont();
		font2.setFontHeightInPoints((short) 12);

		HSSFFont font3 = wb.createFont();
		font3.setFontHeightInPoints((short) 10);

		HSSFFont filterFont = wb.createFont();
		filterFont.setFontHeightInPoints((short) 12);

		HSSFRow row, titleRow, filterRowTitle, filterRow1, filterRow2, filterRow3, filterRow4, filterRow5, filterRow6;
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
		 style1.setAlignment(CellStyle.ALIGN_CENTER);
		cell.setCellValue(new HSSFRichTextString(getText("warehouseStockReportTitle")));	
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);

		if (ObjectUtil.isEmpty(this.filter))
			this.filter = new WarehousePaymentDetails();

		super.filter = this.filter;
		WarehousePayment wpa = new WarehousePayment();
	//	WarehousePaymentDetails wpad=new WarehousePaymentDetails();
		if (!StringUtil.isEmpty(warehouse)) {
			//WarehousePayment wpa = new WarehousePayment();
			Warehouse w = new Warehouse();
			w.setCode(warehouse);
			wpa.setWarehouse(w);
			filter.setWarehousePayment(wpa);
		}

		if (ObjectUtil.isEmpty(filter.getWarehousePayment())) {
			filter.setWarehousePayment(new WarehousePayment());
		}

		if (!StringUtil.isEmpty(seasonCode)) {
			filter.getWarehousePayment().setSeasonCode(seasonCode);
		}

		if (!StringUtil.isEmpty(order)) {

			if (filter.getWarehousePayment() != null) {
				filter.getWarehousePayment().setOrderNo(order);
			} else {
				//WarehousePayment wpa = new WarehousePayment();
				wpa.setOrderNo(order);
				filter.setWarehousePayment(wpa);
			}
		}

		if (!StringUtil.isEmpty(receipt)) {

			if (filter.getWarehousePayment() != null) {
				filter.getWarehousePayment().setReceiptNo(receipt);
			} else {
				//WarehousePayment wpa = new WarehousePayment();
				wpa.setReceiptNo(receipt);
				filter.setWarehousePayment(wpa);
			}
		}

		if (!StringUtil.isEmpty(vendor)) {
			//WarehousePayment wpa = new WarehousePayment();
			Vendor vr = new Vendor();
			vr.setVendorId(vendor);
			wpa.setVendor(vr);
			filter.setWarehousePayment(wpa);
		}

		if (!StringUtil.isEmpty(selectedProduct)) {
			Product product = new Product();
			product.setId(Long.valueOf(selectedProduct));
			filter.setProduct(product);
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

		if (isMailExport()) {
			rowNum++;
			rowNum++;

			/*if (!StringUtil.isEmpty(totalQty)) {
				filterRow4 = sheet.createRow(rowNum++);
				cell = filterRow4.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getText("totalQty")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow4.createCell(2);
				cell.setCellValue(new HSSFRichTextString(totalQty));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}*/
          if(!ObjectUtil.isEmpty(geteDate()) || !StringUtil.isEmpty(seasonCode) || !StringUtil.isEmpty(warehouse) || 
        		  !StringUtil.isEmpty(order) || !StringUtil.isEmpty(receipt) || !StringUtil.isEmpty(vendor) || !StringUtil.isEmpty(selectedProduct)
        		   || !StringUtil.isEmpty(branchIdValue) || !StringUtil.isEmpty(branchIdParma) || !StringUtil.isEmpty(totalQty) )
          {
			filterRowTitle = sheet.createRow(rowNum++);
			cell = filterRowTitle.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getText("filter")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);
          }
			filterRow1 = sheet.createRow(rowNum++);

			cell = filterRow1.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getText("StartingDate")));
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
			cell.setCellValue(new HSSFRichTextString(getText("EndingDate")));
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

			if (!StringUtil.isEmpty(seasonCode)) {
				filterRow3 = sheet.createRow(rowNum++);
				cell = filterRow3.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getText("cSeasonCode")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				HarvestSeason season = farmerService.findHarvestSeasonByCode(seasonCode);
				cell = filterRow3.createCell(2);
				cell.setCellValue(new HSSFRichTextString(season.getName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(warehouse)) {
				filterRow4 = sheet.createRow(rowNum++);
				cell = filterRow4.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getText("warehouse")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow4.createCell(2);
				Warehouse wp = locationService.findWarehouseByCode(warehouse);
				cell.setCellValue(new HSSFRichTextString(wp.getName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(order)) {
				filterRow4 = sheet.createRow(rowNum++);
				cell = filterRow4.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getText("orderNo")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow4.createCell(2);
				cell.setCellValue(new HSSFRichTextString(order));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(receipt)) {
				filterRow3 = sheet.createRow(rowNum++);
				cell = filterRow3.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getText("receiptNo")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow3.createCell(2);
				cell.setCellValue(new HSSFRichTextString(receipt));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(vendor)) {
				filterRow5 = sheet.createRow(rowNum++);
				cell = filterRow5.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getText("vendor")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow5.createCell(2);
				Vendor vendor = clientService.findVendorById(filter.getWarehousePayment().getVendor().getVendorId());
				cell.setCellValue(new HSSFRichTextString(vendor.getVendorId() + "-" + vendor.getVendorName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(selectedProduct)) {
				filterRow6 = sheet.createRow(rowNum++);
				cell = filterRow6.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getText("product")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow6.createCell(2);
				Product prod = productService.findProductById(Long.valueOf(selectedProduct));
				cell.setCellValue(new HSSFRichTextString(prod.getName()));
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
					cell.setCellValue(new HSSFRichTextString(getText("mainOrganization")));
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
					cell.setCellValue(new HSSFRichTextString(getText("subOrganization")));
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
						cell.setCellValue(new HSSFRichTextString(getText("subOrganization")));
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
		rowNum++;
		
		if (!StringUtil.isEmpty(totalQty)) {
			filterRow4 = sheet.createRow(rowNum++);
			cell = filterRow4.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getText("totalQty")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);

			cell = filterRow4.createCell(2);
			 filterStyle.setAlignment(CellStyle.ALIGN_RIGHT);
				cell.setCellStyle(filterStyle);
			cell.setCellValue(new HSSFRichTextString(totalQty));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);
			
		}
				
		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
						
		String mainGridColumnHeaders = "";

		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("warehouseStockEntryHeaderBranch");
			} else if (!StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("warehouseStockEntryBranch");
			}
		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("warehouseStockEntryReportHeaderBranch");
			} else {
				mainGridColumnHeaders = getLocaleProperty("warehouseStockEntryReportHeader");
			}
		}

		if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("warehouseStockEntryReporHeader");
			} 
			else if (getBranchId().equalsIgnoreCase("organic")) {
				mainGridColumnHeaders = getLocaleProperty("OrganiwarehouseStockEntryReporHeader");
			} else if (getBranchId().equalsIgnoreCase("bci")) {
				mainGridColumnHeaders = getLocaleProperty("BCIwarehouseStockEntryReporHeader");
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
			style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cell.setCellStyle(style2);
			style2.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
			style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cell.setCellStyle(style2);
			font2.setBoldweight((short) 12);
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			style2.setFont(font2);
			sheet.setColumnWidth(mainGridIterator, (15 * 550));
			mainGridIterator++;
		}

		Map data = readData("warehouseStockEntry");

		int serialNo = 1;

		List<Object[]> dfata = (ArrayList) data.get(ROWS);
		if (!ObjectUtil.isEmpty(dfata)) {
			for (Object[] datas : dfata) {
				row = sheet.createRow(rowNum++);
				colNum = 0;

				cell = row.createCell(colNum++);
				style5.setAlignment(CellStyle.ALIGN_CENTER);
				cell.setCellStyle(style5);
				cell.setCellValue(serialNo);
				serialNo = serialNo + 1;

				if ((getIsMultiBranch().equalsIgnoreCase("1")
						&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
					if (StringUtil.isEmpty(branchIdValue)) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(
								!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(datas[13])))
										? getBranchesMap().get(getParentBranchMap().get(datas[13]))
										: getBranchesMap().get(datas[13])));
					}
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(datas[13])));

				} else {
					if (StringUtil.isEmpty(branchIdValue)) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(branchesMap.get(datas[13])));
					}
				}

				ESESystem preferences = preferncesService.findPrefernceById("1");
				if (!StringUtil.isEmpty(preferences)) {
					DateFormat genDate = new SimpleDateFormat(
							preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							!StringUtil.isEmpty(genDate.format(datas[1])) ? genDate.format(datas[1]) : "")); // Trxn

				}

				HarvestSeason season = farmerService.findSeasonNameByCode(String.valueOf(datas[12]));

				cell = row.createCell(colNum++);
				/*
				 * cell.setCellValue(new HSSFRichTextString(
				 * !ObjectUtil.isEmpty(season) ? season.getCode() + "-" +
				 * season.getName() : ""));
				 */
				cell.setCellValue(new HSSFRichTextString(season.getName()));

				cell = row.createCell(colNum++);
				style4.setAlignment(CellStyle.ALIGN_RIGHT);
				cell.setCellStyle(style4);
				cell.setCellValue(new HSSFRichTextString(datas[3].toString()));// Order
																				// No

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(datas[5].toString()));
				/*
				 * cell.setCellValue(new HSSFRichTextString(datas[4].toString()
				 * + " - " + datas[5].toString()));
				 */ // Warehouse
					// code
					// -Name

				cell = row.createCell(colNum++);
				style4.setAlignment(CellStyle.ALIGN_RIGHT);
				cell.setCellStyle(style4);
				cell.setCellValue(new HSSFRichTextString(datas[0].toString()));

				cell = row.createCell(colNum++);
				/*
				 * cell.setCellValue(new HSSFRichTextString(datas[6].toString())
				 * + "-" + datas[7].toString());
				 */
				cell.setCellValue(new HSSFRichTextString(datas[7].toString()));

				/*
				 * if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.
				 * MOVCD_TENANT_ID)) { if
				 * (datas[10].toString().equalsIgnoreCase("CS")) { cell =
				 * row.createCell(colNum++); cell.setCellValue(new
				 * HSSFRichTextString(getText("cash"))); } else if
				 * (datas[10].toString().equalsIgnoreCase("CR")) { cell =
				 * row.createCell(colNum++); cell.setCellValue(new
				 * HSSFRichTextString(getText("credit"))); }
				 * 
				 * cell = row.createCell(colNum++); cell.setCellValue( new
				 * HSSFRichTextString(CurrencyUtil.getDecimalFormat((Double)
				 * datas[11], "##.000")));
				 * 
				 * }
				 */

				cell = row.createCell(colNum++);
 				if(datas[8]==null || StringUtil.isEmpty(datas[8])){
 					cell.setCellValue("0.00");
 				}else{
 					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
 					cell.setCellValue(df1.format(Double.valueOf(datas[8].toString())));
 				}
 				
				 
				/*String cellVal = datas[8].toString();
				cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("###0.00"));
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new Double(cellVal));
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);*/

				/*
				 * if (!StringUtil.isEmpty(datas[14].toString())) { cell =
				 * row.createCell(colNum++); cell.setCellValue( new
				 * HSSFRichTextString(CurrencyUtil.getDecimalFormat((Double)
				 * datas[14], "##.00")));
				 * 
				 * }
				 * 
				 * cell = row.createCell(colNum++); cell.setCellValue(new
				 * HSSFRichTextString(CurrencyUtil.getDecimalFormat((Double)
				 * datas[11], "##.000")));
				 */

				HSSFRow subGridRowHead = sheet.createRow(rowNum++);
				String subGridColumnHeaders = " ";

				subGridColumnHeaders = getLocaleProperty("warehouseStockEntryReportSub");

				int subGridIterator = 1;

				for (String cellHeader : subGridColumnHeaders.split("\\,")) {
					cell = subGridRowHead.createCell(subGridIterator);
					cell.setCellValue(new HSSFRichTextString(cellHeader));

					/*
					 * style2.setFillForegroundColor(HSSFColor.
					 * LIGHT_CORNFLOWER_BLUE.index);
					 * style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					 * style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					 * cell.setCellStyle(style2);
					 */

					style3.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
					style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				//	style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					cell.setCellStyle(style3);
					
					style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					cell.setCellStyle(style3);

					font2.setBoldweight((short) 12);
					font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					style3.setFont(font2);
					sheet.setColumnWidth(subGridIterator, (15 * 550));
					subGridIterator++;
				}

				List<WarehousePaymentDetails> warehousePaymentDetails = productDistributionService
						.listWarehousePaymentDetailsByWarehousePaymentId(Long.parseLong(datas[15].toString()));
				ESESystem preference = preferncesService.findPrefernceById("1");
				setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));
				for (WarehousePaymentDetails wpd : warehousePaymentDetails) {
					double goodStock = wpd.getStock();
					double damStock = wpd.getDamagedStock();
					double totalStock = goodStock + damStock;
					row = sheet.createRow(rowNum++);
					colNum = 1;

					cell = row.createCell(colNum++);
					/*
					 * cell.setCellValue(new
					 * HSSFRichTextString(wpd.getProduct().getSubcategory().
					 * getCode() + "-" +
					 * wpd.getProduct().getSubcategory().getName()));
					 */

					cell.setCellValue(new HSSFRichTextString(wpd.getProduct().getSubcategory().getName()));

					cell = row.createCell(colNum++);
					/*
					 * cell.setCellValue( new
					 * HSSFRichTextString(wpd.getProduct().getCode() + "-" +
					 * wpd.getProduct().getName()));
					 */
					cell.setCellValue(new HSSFRichTextString(wpd.getProduct().getName()));

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							!ObjectUtil.isEmpty(wpd.getProduct().getUnit()) ? wpd.getProduct().getUnit() : ""));

					if (getEnableBatchNo().equalsIgnoreCase("1")) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(wpd.getBatchNo()));
					}

					/*
					 * cell = row.createCell(colNum++); cell.setCellValue(new
					 * HSSFRichTextString(wpd.getCostPrice().toString()));
					 */

					cell = row.createCell(colNum++);
					cell.setCellValue(Double.valueOf(CurrencyUtil.getDecimalFormat(wpd.getStock(), "##.00")));

					cell = row.createCell(colNum++);
					cell.setCellValue(
							Double.valueOf(CurrencyUtil.getDecimalFormat(wpd.getDamagedStock(), "##.00")));

					cell = row.createCell(colNum++);
					cell.setCellValue(Double.valueOf(
							CurrencyUtil.getDecimalFormat(totalStock, "##.00")));

					/*
					 * cell = row.createCell(colNum++); cell.setCellValue(new
					 * HSSFRichTextString(
					 * CurrencyUtil.getDecimalFormat(wpd.getWarehousePayment().
					 * getTotalAmount(), "##.000")));
					 */

				}
				row = sheet.createRow(rowNum++);
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
		String fileName = getText("warehouseStockReportTitle") + fileNameDateFormat.format(new Date()) + ".xls";
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

	/**
	 * Gets the filter.
	 * 
	 * @return the filter
	 */
	public String getWarehouse() {

		return warehouse;
	}

	public void setWarehouse(String warehouse) {

		this.warehouse = warehouse;
	}

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

	public String getOrder() {

		return order;
	}

	public void setOrder(String order) {

		this.order = order;
	}

	public WarehousePaymentDetails getFilter() {

		return filter;
	}

	public void setFilter(WarehousePaymentDetails filter) {

		this.filter = filter;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public String getVendor() {

		return vendor;
	}

	public void setVendor(String vendor) {

		this.vendor = vendor;
	}

	public String getBranchIdParma() {
		return branchIdParma;
	}

	public void setBranchIdParma(String branchIdParma) {
		this.branchIdParma = branchIdParma;
	}

	public IProductDistributionService getProductDistributionService() {

		return productDistributionService;
	}

	public void setProductDistributionService(IProductDistributionService productDistributionService) {

		this.productDistributionService = productDistributionService;
	}

	/*
	 * public Map<String, String> getVendorList() {
	 * 
	 * Map<String, String> vendorListMap = new LinkedHashMap<String, String>();
	 * List<Vendor> vendorList = productDistributionService.listVendor(); for
	 * (Vendor obj : vendorList) { vendorListMap.put(obj.getVendorId(),
	 * obj.getVendorName() + " - " + obj.getVendorId());
	 * 
	 * }
	 * 
	 * return vendorListMap; }
	 */

	/*
	 * public Map<String, String> getVendorList() { Map<String, String>
	 * vendorMap = new LinkedHashMap<>(); List<Object[]> vendorList =
	 * productDistributionService.listOfWarehouseByStockEntry(); if
	 * (!ObjectUtil.isEmpty(vendorList)) {
	 * 
	 * vendorMap = vendorList.stream() .collect(Collectors.toMap(obj ->
	 * (String.valueOf(obj[0])), obj -> String.valueOf(obj[1])));
	 * 
	 * } return vendorMap; }
	 */

	public void populateVendorList() {
		JSONArray vendorArr = new JSONArray();
		List<Object[]> vendorList = productDistributionService.listOfWarehouseByStockEntry();
		if (!ObjectUtil.isEmpty(vendorList)) {
			vendorList.forEach(obj -> {
				vendorArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(vendorArr);
	}

	public String getDaterange() {
		return daterange;
	}

	public void setDaterange(String daterange) {
		this.daterange = daterange;
	}

	/*
	 * public Map<String, String> getSeasonList() {
	 * 
	 * Map<String, String> seasonMap = new LinkedHashMap<String, String>();
	 * List<Object[]> seasonList = farmerService.listSeasonCodeAndName();
	 * seasonMap = seasonList.stream() .collect(Collectors.toMap(obj ->
	 * (String.valueOf(obj[0])), obj -> String.valueOf(obj[1])));
	 * 
	 * seasonMap = seasonList.stream().collect( Collectors.toMap(obj ->
	 * (String.valueOf(obj[0])), obj -> String.valueOf(obj[1] + " - " +
	 * obj[0])));
	 * 
	 * return seasonMap; }
	 */

	public void populateSeasonList() {
		JSONArray seasonArr = new JSONArray();
		List<Object[]> seasonList = farmerService.listSeasonCodeAndName();
		if (!ObjectUtil.isEmpty(seasonList)) {
			seasonList.forEach(obj -> {
				seasonArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(seasonArr);
	}

	/*
	 * public Map<String, String> getProductList() {
	 * 
	 * Map<String, String> productMap = new LinkedHashMap<>(); List<Object[]>
	 * productList = productService.listOfProductByStock(); if
	 * (!ObjectUtil.isEmpty(productList)) {
	 * 
	 * productMap = productList.stream() .collect(Collectors.toMap(obj ->
	 * (String.valueOf(obj[3])), obj -> (String.valueOf(obj[1]))));
	 * 
	 * } return productMap; }
	 */

	public void populateProductList() {
		JSONArray productArr = new JSONArray();
		List<Object[]> productList = productService.listOfProductByStock();
		if (!ObjectUtil.isEmpty(productList)) {
			productList.forEach(obj -> {
				productArr.add(getJSONObject(obj[3].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(productArr);
	}

	public String getSeasonCode() {
		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
	}

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}

	public String getEnableBatchNo() {
		return enableBatchNo;
	}

	public void setEnableBatchNo(String enableBatchNo) {
		this.enableBatchNo = enableBatchNo;
	}

	public Map<String, String> getFields() {
		fields.put("1", getText("date"));
		fields.put("2", getLocaleProperty("warehouse"));
		fields.put("3", getText("orderNo"));
		fields.put("4", getText("vendor"));
		fields.put("5", getText("receiptNo"));
		fields.put("6", getText("season"));

		/*
		 * if ((getIsMultiBranch().equalsIgnoreCase("1") &&
		 * (getIsParentBranch().equals("1") ||
		 * StringUtil.isEmpty(branchIdValue)))) { fields.put("8",
		 * getText("app.branch")); } else if (StringUtil.isEmpty(getBranchId()))
		 * { fields.put("7", getText("app.branch")); }
		 */

		if (getIsMultiBranch().equalsIgnoreCase("1")) {
			if (StringUtil.isEmpty(getBranchId())) {
				fields.put("8", getText("app.branch"));
			} else if (getIsParentBranch().equals("1")) {
				fields.put("8", getText("app.subBranch"));
			}

		} else if (StringUtil.isEmpty(getBranchId())) {

			fields.put("7", getText("app.branch"));
		}

		return fields;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
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

	public String getSelectedProduct() {
		return selectedProduct;
	}

	public void setSelectedProduct(String selectedProduct) {
		this.selectedProduct = selectedProduct;
	}

	public void populateLoadFields() throws IOException {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();

		Object[] datas = farmerService.findTotalQty(warehouse, order, selectedProduct, vendor,
				DateUtil.convertStringToDate(startDate, DateUtil.DATE_FORMAT),
				DateUtil.convertStringToDate(endDate, DateUtil.DATE_FORMAT), receipt, seasonCode, branchIdParma);
		jsonObject.put("totalQty", !StringUtil.isEmpty(datas[0])&& !StringUtil.isEmpty(datas[2]) ? NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(datas[0].toString())+Double.parseDouble(datas[2].toString())) : "0.0");
		jsonArray.add(jsonObject);

		response.setContentType("text/JSON");
		PrintWriter out = response.getWriter();
		if (jsonArray.size() > 0)
			out.println(jsonArray.toString());

	}

}