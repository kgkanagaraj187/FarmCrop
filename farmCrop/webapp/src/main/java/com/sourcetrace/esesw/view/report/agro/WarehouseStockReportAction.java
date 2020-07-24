/*
tabIndex * ProcurementReportAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
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
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import org.apache.poi.ss.usermodel.CellStyle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.dao.IReportDAO;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.entity.WarehouseProduct;
import com.sourcetrace.eses.order.entity.profile.SubCategory;
import com.sourcetrace.eses.service.ICategoryService;
import com.sourcetrace.eses.service.IDeviceService;
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
import com.sourcetrace.esesw.entity.profile.WarehousePaymentDetails;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

// TODO: Auto-generated Javadoc
/**
 * The Class PeriodicInspectionReportAction.
 */
@SuppressWarnings("unchecked")
public class WarehouseStockReportAction extends BaseReportAction implements IExporter {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(WarehouseStockReportAction.class);
	private Map<String, String> fields = new HashMap<>();
	private ILocationService locationService;
	private IDeviceService deviceService;
	private WarehouseProduct filter;
	private WarehouseProduct warehouseProduct;
	private IProductService productService;
	private String warehouse;
	private String product;
	private String category;
	private String branchIdParma;
	private IFarmerService farmerService;
	private String seasonCode;
	private String enableBatchNo;
	private IPreferencesService preferncesService;
	private String gridIdentity;
	private ICategoryService categoryService;
	private List<SubCategory> categoryCategoryList = new ArrayList<SubCategory>();
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private String xlsFileName;
	private InputStream fileInputStream;
	private String stock;
	private JSONObject jsonObject;
	@Autowired
	private IProductDistributionService productDistributionService;

	/**
	 * @see com.sourcetrace.esesw.view.BaseReportAction#list()
	 */
	public String list() {
		setFilter(new WarehouseProduct());
		ESESystem preferences = preferncesService.findPrefernceById("1");
		setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));
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
	/**
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */

	public String detail() throws Exception {
		Map<String, String> searchRecord = getJQGridRequestParam();
		setFilter(new WarehouseProduct());
		filter.setWarehouse(new Warehouse());
		filter.getWarehouse().setTypez(Warehouse.COOPERATIVE);
		/*
		 * if(!getCurrentTenantId().equalsIgnoreCase("lalteer")){
		 * filter.setSeasonCode(getCurrentSeasonsCode()); }
		 */
		if (!StringUtil.isEmpty(warehouse)) {
			filter.getWarehouse().setCode(warehouse);
		}
		filter.setProduct(new Product());
		if (!StringUtil.isEmpty(product)) {
			filter.getProduct().setCode(product);
		}

		if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
			if (!getIsMultiBranch().equalsIgnoreCase("1")) {
				List<String> branchList = new ArrayList<>();
				branchList.add(searchRecord.get("branchId").trim());
				filter.setBranchesList(branchList);
			} else {
				List<String> branchList = new ArrayList<>();
				List<BranchMaster> branches = clientService.listChildBranchIds(searchRecord.get("branchId").trim());
				branchList.add(searchRecord.get("branchId").trim());
				branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
					branchList.add(branch.getBranchId());
				});
				filter.setBranchesList(branchList);
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

		filter.getProduct().setSubcategory(new SubCategory());
		if (!StringUtil.isEmpty(category)) {
			filter.getProduct().getSubcategory().setName(category);
		}

		if (!StringUtil.isEmpty(subBranchIdParma) && !subBranchIdParma.equals("0")) {
			filter.setBranchId(subBranchIdParma);
		}
		if (!StringUtil.isEmpty(seasonCode)) {
			filter.setSeasonCode(seasonCode);
		}
		if (!StringUtil.isEmpty(searchRecord.get("subBranchId"))) {
			filter.setBranchId(searchRecord.get("subBranchId").trim());
		}
		if (!StringUtil.isEmpty(searchRecord.get("w.name"))) {
			Warehouse w = new Warehouse();
			w.setName(searchRecord.get("w.name").trim());
			filter.setWarehouse(w);
		}
		if (!StringUtil.isEmpty(searchRecord.get("subCategory"))) {
			Product p = new Product();
			SubCategory s = new SubCategory();
			s.setName(searchRecord.get("subCategory").trim());
			p.setSubcategory(s);
			filter.setProduct(p);
		}
		if (!StringUtil.isEmpty(searchRecord.get("productName"))) {
			Product p = new Product();
			p.setName(searchRecord.get("productName").trim());
			filter.setProduct(p);
		}

		super.filter = this.filter;

		Map data = readData("warehouseStock");
		ESESystem preferences = preferncesService.findPrefernceById("1");
		setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));

		setGridIdentity(IReportDAO.MAIN_GRID);
		return sendJSONResponse(data);

	}

	public JSONObject toJSON(Object obj) {

		DecimalFormat format = new DecimalFormat("0.000");

		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		Object[] datas = (Object[]) obj;
		if (IReportDAO.MAIN_GRID.equalsIgnoreCase(getGridIdentity())) {
			/**
			 * 4=>Branch ID,6=>Wh name,8=>Sub cat name,10=>Product
			 * Name,12=>Season,13=>Total Stock
			 */
			/*
			 * if (StringUtil.isEmpty(branchIdValue)) {
			 * rows.add(branchesMap.get(datas[4])); // BranchId }
			 */if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.LALTEER_TENANT_ID)) {
				if ((getIsMultiBranch().equalsIgnoreCase("1")
						&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

					if (StringUtil.isEmpty(branchIdValue)) {
						rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(datas[4])))
								? getBranchesMap().get(getParentBranchMap().get(datas[4]))
								: getBranchesMap().get(datas[4]));
					}
					rows.add(getBranchesMap().get(datas[4]));

				} else {
					if (StringUtil.isEmpty(branchIdValue)) {
						rows.add(branchesMap.get(datas[4]));
					}
				}
			}
			rows.add(datas[6].toString());
			if (!getCurrentTenantId().equalsIgnoreCase("sagi")) {
				rows.add(datas[8].toString());
			}
			if (getCurrentTenantId().equalsIgnoreCase("sagi"))
				rows.add(datas[8].toString() + "-" + datas[10].toString());
			else
				rows.add(datas[10].toString());
			rows.add(datas[13].toString());
			/*
			 * if(getCurrentTenantId().equalsIgnoreCase("chetna")){
			 * if(!ObjectUtil.isEmpty(datas[13])){
			 * rows.add(datas[13].toString()); }else{ rows.add(""); } }
			 */

			if (getEnableBatchNo().equals("1")) {
				rows.add(!ObjectUtil.isEmpty(datas[14]) ? datas[14].toString() : "");
			}
			rows.add(CurrencyUtil.getDecimalFormat(Double.valueOf(datas[15].toString()), "##.00"));
			if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.LALTEER_TENANT_ID)) {
				if (!ObjectUtil.isEmpty(datas[12])) {
					HarvestSeason seasonName = productService.findHarvestSeasonNamebySeasonCode(datas[12].toString());
					rows.add(!ObjectUtil.isEmpty(seasonName) ? seasonName.getName() : "NA");
				} else {
					rows.add("");
				}
			}
			jsonObject.put("id", datas[3].toString());

			jsonObject.put("cell", rows);

		} else if (IReportDAO.SUB_GRID.equalsIgnoreCase(getGridIdentity())) {

			rows.add(datas[3].toString());
			rows.add(datas[1].toString());
			rows.add(datas[4].toString());
			jsonObject.put("id", datas[3].toString());
			jsonObject.put("cell", rows);
		}
		return jsonObject;
	}

	public String subGridDetail() throws Exception {

		setGridIdentity(IReportDAO.SUB_GRID);
		if (!StringUtil.isEmpty(id)) {
			this.filter = productService.findByWarehouseProductId(Long.valueOf(id));
		}
		if (!ObjectUtil.isEmpty(filter)) {
			super.filter = this.filter;
			String projectionProp = "warehouseSubStockSagi";

			Map data = readData(projectionProp);
			return sendJSONResponse(data);
		}
		return null;
	}

	/**
	 * Gets the warehouse list.
	 * 
	 * @return the warehouse list
	 */
	/*
	 * public Map<String, String> getWarehouseList() { Map<String, String>
	 * warehouseListMap = new LinkedHashMap<String, String>(); List<Warehouse>
	 * warehouseList = locationService.listWarehouse(); for (Warehouse obj :
	 * warehouseList) { warehouseListMap.put(obj.getCode(), obj.getName() +
	 * " - " + obj.getCode()); } return warehouseListMap; }
	 */

	/*public Map<String, String> getWarehouseList() {

		Map<String, String> farmerMap = new LinkedHashMap<>();

		List<Object[]> farmerList = locationService.listOfWarehouseByStock();
		if (!ObjectUtil.isEmpty(farmerList)) {

			farmerMap = farmerList.stream()
					.collect(Collectors.toMap(obj -> (String.valueOf(obj[0])), obj -> (String.valueOf(obj[1]))));

		}

		return farmerMap;
	}*/

	/**
	 * Gets the product list.
	 * 
	 * @return the product list
	 */
	/*
	 * public Map<String, String> getProductList() { Map<String, String>
	 * returnMap = new LinkedHashMap<String, String>(); List<Object[]>
	 * productList = productService.listWarehouseStockProducts(); if
	 * (!ObjectUtil.isListEmpty(productList)) { for (Object[] productObj :
	 * productList) { returnMap.put(productObj[0].toString(),
	 * productObj[1].toString() + " - " + productObj[0].toString()); } } return
	 * returnMap; }
	 */

	/*public Map<String, String> getProductList() {

		Map<String, String> productMap = new LinkedHashMap<>();
		List<Object[]> productList = productService.listOfProductByStock();
		if (!ObjectUtil.isEmpty(productList)) {

			productMap = productList.stream()
					.collect(Collectors.toMap(obj -> (String.valueOf(obj[0])), obj -> (String.valueOf(obj[1]))));

			
			 * productMap = productList.stream() .collect(Collectors.toMap(obj
			 * -> (String.valueOf(obj[0])), obj -> (String.valueOf(obj[1]))));
			 

		}
		return productMap;
	}
*/
	public String populateXLS() throws Exception {
		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getLocaleProperty("Warehouse-StockReport") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getLocaleProperty("warehouseStockReport"), fileMap, ".xls"));
		return "xls";
	}

	public InputStream getExportDataStream(String exportType) throws IOException {
		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		boolean flag = true;
		DateFormat filterDateFormat = new SimpleDateFormat(getGeneralDateFormat());
		DecimalFormat df = new DecimalFormat("0.000");
		DecimalFormat df1 = new DecimalFormat("0.00");

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("whwarehouseStockReportTitle"));
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
		style1.setAlignment(CellStyle.ALIGN_CENTER);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("whwarehouseStockReportTitle")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);

		if (ObjectUtil.isEmpty(this.filter))
			this.filter = new WarehouseProduct();
		filter.setWarehouse(new Warehouse());

		if (!StringUtil.isEmpty(warehouse)) {
			filter.getWarehouse().setCode(warehouse);
		}

		filter.setProduct(new Product());
		if (!StringUtil.isEmpty(product)) {
			filter.getProduct().setCode(product);
		}

		if (!StringUtil.isEmpty(seasonCode)) {
			filter.setSeasonCode(seasonCode);
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

		super.filter = this.filter;
		ESESystem preferences = preferncesService.findPrefernceById("1");
		setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));

		Map data = readData("warehouseStock");

		if (isMailExport()) {
			rowNum++;
			rowNum++;
			if (!StringUtil.isEmpty(seasonCode) || !StringUtil.isEmpty(warehouse) 
					|| !StringUtil.isEmpty(product) || !StringUtil.isEmpty(branchIdValue) || !StringUtil.isEmpty(branchIdParma)) 
			{	
			filterRowTitle = sheet.createRow(rowNum++);
			cell = filterRowTitle.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("filter")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(seasonCode)) {
				filterRow1 = sheet.createRow(rowNum++);
				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("cSeasonCode")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				HarvestSeason season = farmerService.findHarvestSeasonByCode(seasonCode);
				cell = filterRow1.createCell(2);
				cell.setCellValue(new HSSFRichTextString(season.getName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(warehouse)) {
				filterRow2 = sheet.createRow(rowNum++);
				cell = filterRow2.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("warehouse")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow2.createCell(2);
				Warehouse wp = locationService.findWarehouseByCode(filter.getWarehouse().getCode());
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(wp) ? wp.getName() : ""));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(product)) {
				filterRow3 = sheet.createRow(rowNum++);
				cell = filterRow3.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("product")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow3.createCell(2);
				Product prod = productService.findProductByCode(product);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(prod) ? prod.getName() : ""));
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
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("organization")));
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
		String headings = "";
		if (getEnableBatchNo().equals("1")) {
			headings = getLocaleProperty("warehouseStockExportWithBatch");
		} else {
			headings = getLocaleProperty("warehouseStockExport");
		}
		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("branch") + "," + getLocaleProperty("subOrganization") + ","
						+ headings;
			} else if (!StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("subOrganization") + "," + headings;
			}
		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("warehouseStockReportExportBranch") ;
			} else {
				mainGridColumnHeaders = getLocaleProperty("warehouseStockReportExport");
			}
		}
		
		if(getCurrentTenantId().equalsIgnoreCase("pratibha")){
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("warehouseStockExportHeaderagent");
			} 
			else if(getBranchId().equalsIgnoreCase("organic")){
				mainGridColumnHeaders = getLocaleProperty("OrganiwarehouseStockExportHeaderagent");
			}else if(getBranchId().equalsIgnoreCase("bci")){
				mainGridColumnHeaders = getLocaleProperty("BCIwarehouseStockExportHeaderagent");
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
			
			
			cell = mainGridRowHead.createCell(mainGridIterator);
			cell.setCellValue(new HSSFRichTextString(cellHeader));
			style2.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
			style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cell.setCellStyle(style2);
			style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cell.setCellStyle(style2);
			font2.setBoldweight((short) 12);
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			style2.setFont(font2);
			sheet.setColumnWidth(mainGridIterator, (15 * 550));
			mainGridIterator++;
		}

		
		Long serialNumber = 0L;

		
		List<Object[]> dfata = (ArrayList) data.get(ROWS);
		if (!ObjectUtil.isEmpty(dfata)) {
			for (Object[] datas : dfata) {
				row = sheet.createRow(rowNum++);
				colNum = 0;
				
				serialNumber++;
				cell = row.createCell(colNum++);
				style4.setAlignment(CellStyle.ALIGN_CENTER);
				cell.setCellStyle(style4);
				cell.setCellValue(new HSSFRichTextString(String.valueOf(serialNumber)!=null ? String.valueOf(serialNumber) : ""));
				
				
				if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.LALTEER_TENANT_ID)) {
					if ((getIsMultiBranch().equalsIgnoreCase("1")
							&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
						if (StringUtil.isEmpty(branchIdValue)) {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(
									!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(datas[4])))
											? getBranchesMap().get(getParentBranchMap().get(datas[4]))
											: getBranchesMap().get(datas[4])));
						}
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(datas[4])));

					} else {
						if (StringUtil.isEmpty(branchIdValue)) {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(branchesMap.get(datas[4])));
						}
					}
				}
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(datas[6]) ? datas[6].toString() : ""));

				if (!getCurrentTenantId().equalsIgnoreCase("sagi")) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(datas[8]) ? datas[8].toString() : ""));

				}

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(datas[10]) ? datas[10].toString() : ""));
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(datas[13]) ? datas[13].toString() : ""));
				if (getEnableBatchNo().equals("1")) {
					cell = row.createCell(colNum++);
					cell.setCellValue(
							new HSSFRichTextString(!ObjectUtil.isEmpty(datas[14]) ? datas[14].toString() : ""));
				}
				cell = row.createCell(colNum++);
				cell.setCellValue(df1.format(datas[15].toString()));
				if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.LALTEER_TENANT_ID)) {
					if (!ObjectUtil.isEmpty(datas[12])) {
						HarvestSeason seasonName = productService
								.findHarvestSeasonNamebySeasonCode(datas[12].toString());
						cell = row.createCell(colNum++);
						cell.setCellValue(
								new HSSFRichTextString(!ObjectUtil.isEmpty(seasonName) ? seasonName.getName() : "NA"));
					} else {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(""));
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
		String fileName = getLocaleProperty("whwarehouseStockReportTitle") + fileNameDateFormat.format(new Date()) + ".xls";
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
	 * Gets the location service.
	 * 
	 * @return the location service
	 */
	public ILocationService getLocationService() {

		return locationService;
	}

	public WarehouseProduct getFilter() {

		return filter;
	}

	/**
	 * Sets the filter.
	 * 
	 * @param warehouseProduct2
	 *            the new filter
	 */
	public void setFilter(WarehouseProduct warehouseProduct2) {

		this.filter = warehouseProduct2;
	}

	/**
	 * Gets the warehouse product.
	 * 
	 * @return the warehouse product
	 */
	public WarehouseProduct getWarehouseProduct() {

		return warehouseProduct;
	}

	/**
	 * Sets the warehouse product.
	 * 
	 * @param warehouseProduct
	 *            the new warehouse product
	 */
	public void setWarehouseProduct(WarehouseProduct warehouseProduct) {

		this.warehouseProduct = warehouseProduct;
	}

	/**
	 * Gets the product service.
	 * 
	 * @return the product service
	 */
	public IProductService getProductService() {

		return productService;
	}

	/**
	 * Gets the warehouse.
	 * 
	 * @return the warehouse
	 */
	public String getWarehouse() {

		return warehouse;
	}

	/**
	 * Gets the product.
	 * 
	 * @return the product
	 */
	public String getProduct() {

		return product;
	}

	/**
	 * Sets the warehouse.
	 * 
	 * @param warehouse
	 *            the new warehouse
	 */
	public void setWarehouse(String warehouse) {

		this.warehouse = warehouse;
	}

	/**
	 * Sets the product.
	 * 
	 * @param product
	 *            the new product
	 */
	public void setProduct(String product) {

		this.product = product;
	}

	public void setProductService(IProductService productService) {

		this.productService = productService;
	}

	public String getBranchIdParma() {

		return branchIdParma;
	}

	public void setBranchIdParma(String branchIdParma) {

		this.branchIdParma = branchIdParma;
	}

	public IFarmerService getFarmerService() {

		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {

		this.farmerService = farmerService;
	}

	/*public Map<String, String> getSeasonList() {

		Map<String, String> seasonMap = new LinkedHashMap<String, String>();
		List<Object[]> seasonList = farmerService.listSeasonCodeAndName();
		
		 * seasonMap = seasonList.stream().collect( Collectors.toMap(obj ->
		 * (String.valueOf(obj[0])), obj -> String.valueOf(obj[1] + " - " +
		 * obj[0])));
		 
		seasonMap = seasonList.stream()
				.collect(Collectors.toMap(obj -> (String.valueOf(obj[0])), obj -> String.valueOf(obj[1])));
		return seasonMap;
	}
*/
	public String getSeasonCode() {

		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {

		this.seasonCode = seasonCode;
	}

	public String getEnableBatchNo() {

		return enableBatchNo;
	}

	public void setEnableBatchNo(String enableBatchNo) {

		this.enableBatchNo = enableBatchNo;
	}

	public IPreferencesService getPreferncesService() {

		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {

		this.preferncesService = preferncesService;
	}

	public IDeviceService getDeviceService() {

		return deviceService;
	}

	public void setDeviceService(IDeviceService deviceService) {

		this.deviceService = deviceService;
	}

	public String getGridIdentity() {

		return gridIdentity;
	}

	public void setGridIdentity(String gridIdentity) {

		this.gridIdentity = gridIdentity;
	}

	public Map<String, String> getFields() {
		fields.put("1", getText("warehouse"));
		fields.put("2", getText("product"));
		if (!getCurrentTenantId().equals("lalteer")) {
			fields.put("3", getText("name.season"));
		}

		/*
		 * if ((getIsMultiBranch().equalsIgnoreCase("1") &&
		 * (getIsParentBranch().equals("1") ||
		 * StringUtil.isEmpty(branchIdValue)))) { fields.put("5",
		 * getText("app.branch")); } else if (StringUtil.isEmpty(getBranchId()))
		 * { fields.put("4", getText("app.branch")); }
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

		fields.put("6", getText("category"));

		return fields;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}

	public Map<String, String> getCategoryCategoryList() {

		Map<String, String> subCategoryyyList = new LinkedHashMap<String, String>();
		categoryCategoryList = categoryService.listSubCategoryByDefaultCategory();
		for (SubCategory subCategory : categoryCategoryList) {
			subCategoryyyList.put(subCategory.getName(), subCategory.getName());
		}
		return subCategoryyyList;

	}

	public ICategoryService getCategoryService() {

		return categoryService;
	}

	public void setCategoryService(ICategoryService categoryService) {

		this.categoryService = categoryService;
	}

	public void setCategoryCategoryList(List<SubCategory> categoryCategoryList) {

		this.categoryCategoryList = categoryCategoryList;
	}

	public String getCategory() {

		return category;
	}

	public void setCategory(String category) {

		this.category = category;
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

	public IProductDistributionService getProductDistributionService() {
		return productDistributionService;
	}

	public void setProductDistributionService(IProductDistributionService productDistributionService) {
		this.productDistributionService = productDistributionService;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	@SuppressWarnings("unchecked")
	public void populateWarehouseStockUpdate() {
		if (!StringUtil.isEmpty(getId())) {
			warehouseProduct = productDistributionService.findwarehouseProductById(Long.valueOf(getId()));
			warehouseProduct.setStock(Double.valueOf(getStock()));

			productDistributionService.editWarehouseProducts(warehouseProduct);
			getJsonObjects().put("msg", getText("msg.updated"));
			getJsonObjects().put("title", getText("title.success"));
			sendAjaxResponse(getJsonObjects());
			// printAjaxResponse(getJsonObject(), "text/json");
		}
	}

	public JSONObject getJsonObjects() {
		if (jsonObject == null) {
			jsonObject = new JSONObject();
		}
		return jsonObject;
	}

	public void sendAjaxResponse(JSONObject jsonObject) {

		try {
			response.setContentType("text/JSON");
			PrintWriter out = response.getWriter();
			out.println(jsonObject.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
public void populateWarehouseList(){
	JSONArray warehouseArr = new JSONArray();
	List<Object[]> warehouse =locationService.listOfWarehouseByStock();
	if (!ObjectUtil.isEmpty(warehouse)) {
		warehouse.forEach(obj -> {
			warehouseArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
		});
	}
	sendAjaxResponse(warehouseArr);
}
public void populateProductList(){
	JSONArray productArr = new JSONArray();
	List<Object[]> productList = productService.listOfProductByStock();
	if (!ObjectUtil.isEmpty(productList)) {
		productList.forEach(obj -> {
			productArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
		});
	}
	sendAjaxResponse(productArr);
}
public void populateSeasonList(){
	  JSONArray seasonArr = new JSONArray();
	  List<Object[]> seasonList = farmerService.listSeasonCodeAndName();
	  if (!ObjectUtil.isEmpty(seasonList)) {
	   seasonList.forEach(obj -> {
	    seasonArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
	   });
	  }
	  sendAjaxResponse(seasonArr);
	 }
}