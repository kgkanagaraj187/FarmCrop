/*
 * ProcurementStockReportAction.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
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
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

import com.ese.entity.util.ESESystem;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.profile.GradeMaster;
import com.sourcetrace.eses.order.entity.txn.CityWarehouse;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

public class ProcurementStockReportAction extends BaseReportAction implements IExporter {

	private static final long serialVersionUID = 1L;
	private Map<String, String> fields = new HashMap<>();
	private CityWarehouse filter;
	private String areaCode;
	private String gridIdentity;
	private ILocationService locationService;
	private IFarmerService farmerService;
	private IPreferencesService preferncesService;
	private IProductDistributionService productDistributionService;
	private IProductService productService;
	private Map<String, GradeMaster> gradeMasterMap;
	private Map<String, ProcurementGrade> procurememtGradeMap;
	private String xlsFileName;
	private InputStream fileInputStream;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private IClientService clientService;
	private String branchIdParma;
	private String branch;
	private String exportLimit;
	private String farmerId;
	private String productId;
	private Map<String, CityWarehouse> cityWarehouseGradeMap;
	private String selectedCoOperative;
	private String headerFields;

	/**
	 * @see com.sourcetrace.esesw.view.BaseReportAction#list()
	 */
	public String list() throws Exception {

		request.setAttribute(HEADING, getText(LIST));

		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			setExportLimit(preferences.getPreferences().get(ESESystem.EXPORT_RECORD_LIMIT));
		}
		filter = new CityWarehouse();
		loadProcurementGrades();
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
	public String detail() throws Exception {

		this.gridIdentity = "detail";
		if (!ObjectUtil.isEmpty(this.filter)) {
			if (!ObjectUtil.isEmpty(this.filter.getProcurementProduct())) {
				this.filter.getProcurementProduct().setId(this.filter.getProcurementProduct().getId());
			}
			if (!ObjectUtil.isEmpty(this.filter.getProcurementProduct())) {
				this.filter.getProcurementProduct().setUnit(this.filter.getProcurementProduct().getUnit());
			}
			
		} else {
			this.filter = new CityWarehouse();
			this.filter.setCoOperative(new Warehouse());
		}
		if (!StringUtil.isEmpty(branchIdParma)) {
			filter.setBranchId(branchIdParma);
		}
	
		super.filter = this.filter;
		// Map data = readData("coOperativeAndProcurmentProduct");
		if (getCurrentTenantId().equalsIgnoreCase("lalteer")) {
			Map data = readData("coOperativeAndProcurmentProduct");
			return sendJSONResponse(data);
		} else {
			Map data = readData("wareHouseAndProcurmentProduct");

			return sendJSONResponse(data);
		}
		// return sendJSONResponse(data);
	}

	/**
	 * Sub grid detail.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings("unchecked")
	public String populateSubGridDetails() throws Exception {

		loadProcurementGrades();
		this.gridIdentity = "subDetail";
		if (!ObjectUtil.isEmpty(this.filter.getProcurementProduct())
				&& this.filter.getProcurementProduct().getId() == 0) {
			this.filter.setProcurementProduct(null);
		}
		if (!ObjectUtil.isEmpty(this.filter.getFarmer()) && this.filter.getFarmer().getId() == 0) {
			this.filter.setFarmer(null);
		}
		if (!ObjectUtil.isEmpty(this.filter.getQuality())) {
			this.filter.setQuality(null);
		}
		super.filter = this.filter;
		// Transient value assigned for sub grid
		this.filter.setAreaId(this.gridIdentity);
		if (getCurrentTenantId().equalsIgnoreCase("lalteer")) {
			Map data = readData("coOperativeProcurmentProductAndGrade");
			return sendJSONResponse(data);
		} else {
			Map data = readData("wareHouseProcurmentProductAndGrade");

			return sendJSONResponse(data);
		}

	}

	/**
	 * @see com.sourcetrace.esesw.view.BaseReportAction#toJSON(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		JSONObject jsonObject = new JSONObject();
		DecimalFormat formatter = new DecimalFormat("###.00");
		// ESESystem system =
		// preferncesService.findPrefernceById(ESESystem.SYSTEM_SWITCH);
		// String tareWeight =
		// system.getPreferences().get(ESESystem.TARE_WEIGHT);
		Double totalTareWt = 0.0;
		if ("detail".equals(this.gridIdentity)) {
			Object[] datas = (Object[]) obj;
			JSONArray rows = new JSONArray();
			/*
			 * if (StringUtil.isEmpty(branchIdValue)) {
			 * rows.add(branchesMap.get(datas[7])); // BranchId }
			 */
			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(datas[6])))
							? getBranchesMap().get(getParentBranchMap().get(datas[6]))
							: getBranchesMap().get(datas[6]));
				}
				rows.add(getBranchesMap().get(datas[6]));

			} else {
				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(branchesMap.get(datas[6]));
				}
			}
			if (getCurrentTenantId().equalsIgnoreCase("lalteer")) {
				// rows.add(datas[3]); // Co Operative Code
				rows.add(datas[6]); // Co Operative Name
				// rows.add(datas[4]); // Product Code
				rows.add(datas[7]); // Product Name
				// rows.add(datas[5]==null ? datas[8] : datas[8] + "-" +
				// datas[5]); // farmer name and code
				rows.add(datas[8]);
				Object totalBag = productDistributionService.findNoOfBagByByWarehouseIdProductIdAndFarmerId(datas[0],
						datas[1], datas[2]);
				rows.add(totalBag); // No of Bags
				Object totalNetWeight = productDistributionService
						.findNetWeightByWarehouseIdProductIdAndFarmerId(datas[0], datas[1], datas[2]);
				rows.add(formatter.format(
						Double.parseDouble(formatter.format(Double.parseDouble(String.valueOf(totalNetWeight)))))); // Net
																													// Weight
				jsonObject.put("id", datas[0] + "_" + datas[1] + "_" + datas[2]);
				jsonObject.put("cell", rows);
			} else {
				/*
				 * rows.add(datas[2]); // Co Operative Code
				 */ rows.add(datas[3]);
				// Co Operative Name
				/*
				 * rows.add(datas[4]); // Product Code
				 */ rows.add(datas[5]); // Product Name
				 rows.add(datas[7]);
				
				if(!getCurrentTenantId().equals(ESESystem.PRATIBHA_TENANT_ID)){
				 if (datas[0] != "" || datas[0] != "0" && datas[1] != "" || datas[1] != "0") {
					Object totalBag = productDistributionService.findNoOfBagByByWarehouseIdProductId(datas[0],
							datas[1]);
					rows.add(totalBag); // No of Bags}
				} else {
					rows.add(0);
				}
				}
				if (datas[0] != "" || datas[0] != "0" && datas[1] != "" || datas[1] != "0") {
					Object totalNetWeight = productDistributionService.findNetWeightByWarehouseIdProductId(datas[0],
							datas[1]);
					/*
					 * rows.add(formatter
					 * .format(Double.parseDouble(formatter.format(Double.
					 * parseDouble(String.valueOf(datas[8])))) - totalTareWt));
					 * // Net Weight
					 */ rows.add(formatter.format(
							Double.parseDouble(formatter.format(Double.parseDouble(String.valueOf(totalNetWeight)))))); // Net
																														// Weight
				} else {
					rows.add(0.00);
				}
				jsonObject.put("id", datas[0] + "_" + datas[1]);
				jsonObject.put("cell", rows);
			}

		} else {

			if (getCurrentTenantId().equalsIgnoreCase("lalteer")) {
				Object[] datas = (Object[]) obj;
				JSONArray rows = new JSONArray();

				// rows.add(datas[2]); // Quantity Code
				String procurementProductGrade = String.valueOf(datas[3]);

				if (procurememtGradeMap.containsKey(procurementProductGrade)) {

					ProcurementGrade procurementGrade = procurememtGradeMap.get(procurementProductGrade);
					if (!ObjectUtil.isEmpty(procurementGrade)) {
						rows.add(!ObjectUtil.isEmpty(procurementGrade.getProcurementVariety())
								? !StringUtil.isEmpty(procurementGrade.getProcurementVariety().getName())
										? procurementGrade.getProcurementVariety().getName() : ""
								: "");
						if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {
							rows.add(!ObjectUtil.isEmpty(procurementGrade)
									? !StringUtil.isEmpty(procurementGrade.getName()) ? procurementGrade.getName() : ""
									: ""); // Grade Name
						}
					}
				} else {
					rows.add("");
					rows.add("");
				}
				rows.add(datas[6]);
				rows.add(formatter
						.format(Double.parseDouble(formatter.format(Double.parseDouble(String.valueOf(datas[5]))))
								- totalTareWt)); // Net Weight
				jsonObject.put("id", datas[4] + "_" + datas[2]);
				jsonObject.put("cell", rows);
			} else {
				Object[] datas = (Object[]) obj;
				JSONArray rows = new JSONArray();

				// rows.add(datas[5]);// Variety
				// rows.add(datas[6]);// Grade
				// rows.add(datas[7]);// Number of bags
				// rows.add(datas[9]);// Weight
				String procurementProductGrade = String.valueOf(datas[2]);

				if (procurememtGradeMap.containsKey(procurementProductGrade)) {

					ProcurementGrade procurementGrade = procurememtGradeMap.get(procurementProductGrade);
					if (!ObjectUtil.isEmpty(procurementGrade)) {
						rows.add(!ObjectUtil.isEmpty(procurementGrade.getProcurementVariety())
								? !StringUtil.isEmpty(procurementGrade.getProcurementVariety().getName())
										? procurementGrade.getProcurementVariety().getName() : ""
								: "");
						if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {
							rows.add(!ObjectUtil.isEmpty(procurementGrade)
									? !StringUtil.isEmpty(procurementGrade.getName()) ? procurementGrade.getName() : ""
									: ""); // Grade Name
						}
					}
				} else {
					rows.add("");
					rows.add("");
				}
				if(!getCurrentTenantId().equals(ESESystem.PRATIBHA_TENANT_ID)){
				if (datas[0] != "" || datas[0] != "0" && datas[1] != "" || datas[1] != "0" && datas[2] != ""
						|| datas[2] != "0") {
					Object totalBag = productDistributionService
							.findNoOfBagByByWarehouseIdProductIdAndGradeCode(datas[0], datas[1], datas[2]);
					rows.add(totalBag); // No of Bags}
				} else {
					rows.add(0);
				}
				}
				if (datas[0] != "" || datas[0] != "0" && datas[1] != "" || datas[1] != "0" && datas[2] != ""
						|| datas[2] != "0") {
					Object totalNetWeight = productDistributionService
							.findNetWeightByWarehouseIdProductIdAndGradeCode(datas[0], datas[1], datas[2]);
					/*
					 * rows.add(formatter
					 * .format(Double.parseDouble(formatter.format(Double.
					 * parseDouble(String.valueOf(datas[8])))) - totalTareWt));
					 * // Net Weight
					 */ rows.add(formatter.format(
							Double.parseDouble(formatter.format(Double.parseDouble(String.valueOf(totalNetWeight)))))); // Net
																														// Weight
				} else {
					rows.add(0.00);
				}

				jsonObject.put("id", datas[0] + "_" + datas[1] + "_" + datas[2]);
				jsonObject.put("cell", rows);
			}

		}

		return jsonObject;
	}

	/**
	 * Gets the city list.
	 * 
	 * @return the city list
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getCityList() {

		Map<String, String> cityList = new LinkedHashMap<String, String>();

		List<Municipality> cities = locationService.listCity();
		if (!ObjectUtil.isEmpty(cities)) {
			for (Municipality city : cities) {
				cityList.put(city.getCode(), city.getName() + "-" + city.getCode());
			}
		}

		return cityList;
	}

	/**
	 * Gets the co operative list.
	 * 
	 * @return the co operative list
	 */
	/*
	 * public List<Warehouse> getCoOperativeList() {
	 * 
	 * return locationService.listOfCooperatives(); }
	 */

	/*
	 * public Map<String, String> getCoOperativeList() { Map<String, String>
	 * farmerMap = new LinkedHashMap<>(); List<Object[]> farmerList =
	 * locationService.listOfCooperativeByProcurement(); if
	 * (!ObjectUtil.isEmpty(farmerList)) {
	 * 
	 * farmerMap = farmerList.stream().collect(Collectors.toMap(obj ->
	 * (String.valueOf(obj[0])), obj -> (String.valueOf(obj[2]) + " - " +
	 * String.valueOf(obj[1]))));
	 * 
	 * } return farmerMap; }
	 * 
	 * public Map<String, String> getFarmersList() {
	 * 
	 * Map<String, String> farmerMap = new LinkedHashMap<>(); List<Object[]>
	 * farmerList = farmerService.listFarmerInfoByProcurement(); if
	 * (!ObjectUtil.isEmpty(farmerList)) {
	 * 
	 * farmerMap = farmerList.stream() .collect(Collectors.toMap(obj ->
	 * (String.valueOf(obj[0])), obj -> (String.valueOf(obj[1]))));
	 * 
	 * } return farmerMap;
	 * 
	 * 
	 * }
	 */

	/**
	 * Gets the grade master map.
	 * 
	 * @return the grade master map
	 */
	public Map<String, GradeMaster> getGradeMasterMap() {

		if (ObjectUtil.isEmpty(gradeMasterMap)) {
			gradeMasterMap = new LinkedHashMap<String, GradeMaster>();
			List<GradeMaster> gradeMasterList = productDistributionService.listGradeMaster();
			for (GradeMaster gradeMaster : gradeMasterList)
				gradeMasterMap.put(gradeMaster.getCode(), gradeMaster);
		}
		return gradeMasterMap;
	}

	/**
	 * Populate central warehouse.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void populateCentralWarehouse() throws Exception {

		StringBuffer sb = new StringBuffer();
		NumberFormat formatter = new DecimalFormat();
		List<Object[]> listCityWarehouse = productDistributionService.listCentralWarehouse();
		if (!ObjectUtil.isListEmpty(listCityWarehouse)) {
			ESESystem system = preferncesService.findPrefernceById(ESESystem.SYSTEM_SWITCH);
			String tareWeight = system.getPreferences().get(ESESystem.TARE_WEIGHT);
			Double totalNetWeight = 0.0;
			for (Object[] object : listCityWarehouse) {
				Double totalTareWeight = Double.parseDouble(object[1].toString()) * Double.parseDouble(tareWeight);
				totalNetWeight += Double.parseDouble(object[2].toString()) - totalTareWeight;
				GradeMaster grade = productDistributionService.findGradeByCode(object[0].toString());
				sb.append("<tr><td>" + grade.getName() + "</td><td>" + object[1] + "</td><td>"
						+ formatter.format(object[2]) + "</td></tr>");
			}
			sb.append("<tr></tr>");
			sb.append("<tr bgcolor='#E8FAE6'><td><b>" + getText("netWeight") + "</td><td><b>-</td><td><b>"
					+ formatter.format(totalNetWeight) + "</td></tr>");
		}
		response.getWriter().print(sb);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");

	}

	/**
	 * Sets the filter.
	 * 
	 * @param filter
	 *            the new filter
	 */
	public void setFilter(CityWarehouse filter) {

		this.filter = filter;
	}

	/**
	 * Gets the filter.
	 * 
	 * @return the filter
	 */
	public CityWarehouse getFilter() {

		return filter;
	}

	/**
	 * Sets the area code.
	 * 
	 * @param areaCode
	 *            the new area code
	 */
	public void setAreaCode(String areaCode) {

		this.areaCode = areaCode;
	}

	/**
	 * Gets the area code.
	 * 
	 * @return the area code
	 */
	public String getAreaCode() {

		return areaCode;
	}

	/**
	 * Gets the grid identity.
	 * 
	 * @return the grid identity
	 */
	public String getGridIdentity() {

		return gridIdentity;
	}

	/**
	 * Sets the grid identity.
	 * 
	 * @param gridIdentity
	 *            the new grid identity
	 */
	public void setGridIdentity(String gridIdentity) {

		this.gridIdentity = gridIdentity;
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
	 * Gets the location service.
	 * 
	 * @return the location service
	 */
	public ILocationService getLocationService() {

		return locationService;
	}

	/**
	 * Gets the prefernces service.
	 * 
	 * @return the prefernces service
	 */
	public IPreferencesService getPreferncesService() {

		return preferncesService;
	}

	/**
	 * Sets the prefernces service.
	 * 
	 * @param preferncesService
	 *            the new prefernces service
	 */
	public void setPreferncesService(IPreferencesService preferncesService) {

		this.preferncesService = preferncesService;
	}

	/**
	 * Gets the product distribution service.
	 * 
	 * @return the product distribution service
	 */
	public IProductDistributionService getProductDistributionService() {

		return productDistributionService;
	}

	/**
	 * Sets the product distribution service.
	 * 
	 * @param productDistributionService
	 *            the new product distribution service
	 */
	public void setProductDistributionService(IProductDistributionService productDistributionService) {

		this.productDistributionService = productDistributionService;
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

	/**
	 * Gets the file input stream.
	 * 
	 * @return the file input stream
	 */
	public InputStream getFileInputStream() {

		return fileInputStream;
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

	public String getBranchIdParma() {

		return branchIdParma;
	}

	public void setBranchIdParma(String branchIdParma) {

		this.branchIdParma = branchIdParma;
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
		setXlsFileName(getText("ProcurementStockReportList") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(
				FileUtil.createFileInputStreamToZipFile(getText("procurementStockReportList"), fileMap, ".xls"));
		return "xls";
	}

	/**
	 * @see com.sourcetrace.esesw.view.IExporter#getExportDataStream(java.lang.String)
	 */

	@Override
	public String populatePDF() throws Exception {
		List<String> filters = new ArrayList<String>();
		InputStream is = getPDFExportDataStream(filters);
		setPdfFileName(getText("procurementStockReportList") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(getPdfFileName(), is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("distributionReportList"), fileMap, ".pdf"));
		return "pdf";
	}

	private InputStream getPDFExportDataStream(List<String> filters)
			throws IOException, DocumentException, NoSuchFieldException, SecurityException {

		Long serialNo = 0L;

		Font cellFont = null; // font for cells.
		Font titleFont = null; // font for title text.
		Paragraph title = null; // to add title for report
		String columnHeaders = null; // to hold column headers from property
		DecimalFormat formatter = new DecimalFormat("###.00");
		Double totalTareWt = 0.0;
		List<Object[]> entityFieldsList = new ArrayList<Object[]>();
		SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Document document = new Document();
		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap();
		setMailExport(true);
		String makeDir = FileUtil.storeXls(request.getSession().getId());
		String fileName = getText("procurementStockReportList") + fileNameDateFormat.format(new Date()) + ".pdf";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		PdfWriter.getInstance(document, fileOut);
		document.open();
		PdfPTable table = null;
		com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance(exportLogo());
		logo.scaleAbsolute(100, 50);
		document.add(logo); // Adding logo in PDF file.
		// below of code for title text
		titleFont = new Font(FontFamily.HELVETICA, 14, Font.BOLD, GrayColor.GRAYBLACK);
		title = new Paragraph(new Phrase(getLocaleProperty("exportDistributionTitle"), titleFont));
		title.setAlignment(Element.ALIGN_CENTER);
		document.add(title);
		document.add(
				new Paragraph(new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK)))); // Add

		PdfPCell cell = null; // cell for table.
		setsDate(IExporter.EXPORT_DAILY.equalsIgnoreCase(IExporter.EXPORT_MANUAL)
				? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));
		boolean flag = true;
		DecimalFormat df = new DecimalFormat("0.000");
		DateFormat filterDateFormat = new SimpleDateFormat(FILTERDATE);

		/*
		 * if (isMailExport()) { document.add(new Paragraph(new
		 * Phrase(getLocaleProperty("filter"),new Font(FontFamily.HELVETICA, 10,
		 * Font.BOLD, GrayColor.GRAYBLACK)))); document.add(new Paragraph(new
		 * Phrase(getLocaleProperty("StartingDate")+" : "+filterDateFormat.format(
		 * getsDate()),new Font(FontFamily.HELVETICA, 10, Font.BOLD,
		 * GrayColor.GRAYBLACK)))); document.add(new Paragraph(new
		 * Phrase(getLocaleProperty("EndingDate")+" : "+filterDateFormat.format(
		 * geteDate()),new Font(FontFamily.HELVETICA, 10, Font.BOLD,
		 * GrayColor.GRAYBLACK)))); document.add(new Paragraph(new
		 * Phrase(" ",new Font(FontFamily.HELVETICA, 10, Font.NORMAL,
		 * GrayColor.GRAYBLACK)))); // Add a blank line. }
		 */

		// HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		String mainGridColumnHeaders = "";
		int mainGridIterator = 0;
		if (StringUtil.isEmpty(branchIdValue)) {
			mainGridColumnHeaders = getLocaleProperty("ExportMainGridHeadingProcurmentStockBranch");
			table = new PdfPTable(mainGridColumnHeaders.split("\\,").length);
		} else {
			mainGridColumnHeaders = getLocaleProperty("ExportMainGridHeadingProcurmentStock");
			table = new PdfPTable(mainGridColumnHeaders.split("\\,").length);
		}

		Integer mainGridColSize = mainGridColumnHeaders.split("\\,").length;

		cellFont = new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK);

		String headerLabel = null;

		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {

			if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
				headerLabel = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
			} else {
				headerLabel = cellHeader.trim();
			}

			cell = new PdfPCell(new Phrase(headerLabel, cellFont));
			cell.setBackgroundColor(new BaseColor(144,238,144));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setNoWrap(false); // To set wrapping of text in cell.
			table.addCell(cell);

		}
		cellFont = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK);
		if (!ObjectUtil.isEmpty(this.filter)) {
			if (!ObjectUtil.isEmpty(this.filter.getProcurementProduct())) {
				this.filter.getProcurementProduct().setId(this.filter.getProcurementProduct().getId());
			}
			
		} else {
			this.filter = new CityWarehouse();
			this.filter.setCoOperative(new Warehouse());

		}

		if (!StringUtil.isEmpty(branchIdParma)) {
			filter.setBranchId(branchIdParma);
		}
		super.filter = this.filter;
		String proj = "wareHouseAndProcurmentProduct";

		if (getCurrentTenantId().equalsIgnoreCase("lalteer")) {
			proj = "coOperativeAndProcurmentProduct";

		}

		Map data = isMailExport() ? readData(proj) : readExportData(proj);

	

		List<Object[]> mainGridRows = (List<Object[]>) data.get(ROWS);
		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;
		
		
		if (filter.getCoOperative().getId() > 0) {

			  Warehouse coOperative = locationService.findWarehouseById(filter.getCoOperative().getId());
			  
			filters.add(getLocaleProperty("cooperative") + " : " + String.valueOf(coOperative.getName()).trim() + " - " + String.valueOf(coOperative.getCode()).trim());
			
			
		}if (!ObjectUtil.isEmpty(this.filter.getProcurementProduct())) {
		if(filter.getProcurementProduct().getId() > 0){
			ProcurementProduct product =productService.findProcurementProductById(filter.getProcurementProduct().getId());
			filters.add(getLocaleProperty("product") + " : " + String.valueOf(product.getName()).trim() + " - " + String.valueOf(product.getCode()).trim());
		
		}
		}
		flag = false;
		
		for (String filter : filters) {
			document.add(new Paragraph(
					new Phrase(filter, new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK))));
			document.add(new Paragraph(
					new Phrase(" ", new Font(FontFamily.HELVETICA, 4, Font.NORMAL, GrayColor.GRAYBLACK))));
		}

		if (!StringUtil.isEmpty(headerFields)) {
			String[] headerFieldsArr=headerFields.split("###");
			 
				  document.add(new Paragraph(
							new Phrase(headerFieldsArr[0] + " : " + headerFieldsArr[1] +"  "+ headerFieldsArr[2] + " : " + headerFieldsArr[3] ,
									new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));
				  document.add(new Paragraph(new Phrase(" ",new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));
			}
		
		for (Object[] datas : mainGridRows) {
		
			cellFont = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK);

			serialNo++;

			cell = new PdfPCell(new Paragraph(new Phrase(String.valueOf(serialNo), cellFont)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			if (StringUtil.isEmpty(branchIdValue)) {
				cell = new PdfPCell(new Phrase(branchesMap.get(datas[6]), cellFont)); // BranchId
				table.addCell(cell);
			}
			if (getCurrentTenantId().equalsIgnoreCase("lalteer")) {

				/*
				 * cell = new PdfPCell(new
				 * Phrase(datas[3].toString(),cellFont)); // Co
				 * table.addCell(cell);
				 */ // Operative
				// Code

				cell = new PdfPCell(new Phrase(datas[6].toString(), cellFont)); // Co
				table.addCell(cell); // Operative
				// Name

				cell = new PdfPCell(new Phrase(datas[7].toString(), cellFont)); // Product
				table.addCell(cell); // Name

				cell = new PdfPCell(new Phrase(datas[5] == null ? datas[8].toString() : datas[8].toString(), cellFont)); // farmer
				table.addCell(cell); // name
				// and
				// code
				if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
				Object totalBag = productDistributionService.findNoOfBagByByWarehouseIdProductIdAndFarmerId(datas[0],
						datas[1], datas[2]);

				cell = new PdfPCell(new Phrase(totalBag.toString(), cellFont)); // No
				table.addCell(cell); // of
				// Bags
				}
				Object totalNetWeight = productDistributionService
						.findNetWeightByWarehouseIdProductIdAndFarmerId(datas[0], datas[1], datas[2]);

				cell = new PdfPCell(new Phrase(
						formatter.format(Double
								.parseDouble(formatter.format(Double.parseDouble(String.valueOf(totalNetWeight))))),
						cellFont));
				table.addCell(cell);
			} else {
				/*
				 * cell = new PdfPCell(new Phrase(datas[2].toString())); // Co
				 * table.addCell(cell);
				 */
				// Warehouse code // Operative
				// Code

				cell = new PdfPCell(new Phrase(datas[3].toString(), cellFont)); // Co
				table.addCell(cell); // Operative
				// Name

				/*
				 * cell = new PdfPCell(new Phrase(datas[4].toString())); //
				 * Product table.addCell(cell);
				 */ // Code

				cell = new PdfPCell(new Phrase(datas[5].toString(), cellFont)); // Product
				table.addCell(cell); // Name
				
				cell = new PdfPCell(new Phrase(datas[7].toString(), cellFont)); // Product
				table.addCell(cell);
				
				if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
				if (datas[0] != "" || datas[0] != "0" && datas[1] != "" || datas[1] != "0") {
					Object totalBag = productDistributionService.findNoOfBagByByWarehouseIdProductId(datas[0],
							datas[1]);
					cell = new PdfPCell(new Phrase(totalBag.toString(), cellFont)); // No of
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell); // of
					// Bags
				} else {
					cell = new PdfPCell(new Phrase("0"));
					table.addCell(cell);
				}
				}
				
				if (datas[0] != "" || datas[0] != "0" && datas[1] != "" || datas[1] != "0") {
					Object totalNetWeight = productDistributionService.findNetWeightByWarehouseIdProductId(datas[0],
							datas[1]);

					cell = new PdfPCell(new Phrase(
							formatter.format(Double
									.parseDouble(formatter.format(Double.parseDouble(String.valueOf(totalNetWeight))))),
							cellFont)); // Net
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell); // Weight
				} else {
					cell = new PdfPCell(new Phrase("0.00"));
					table.addCell(cell);
				}
			}
			String sunGridcolumnHeaders = "";
			sunGridcolumnHeaders = getText("ExportSubGridHeadingProcurmentStock");
			int subGridSize = sunGridcolumnHeaders.split(",").length;
			int emptyCols = 0;
			if (mainGridColSize > (subGridSize + 1)) {
				emptyCols = mainGridColSize - (subGridSize + 1);
			}
			if (serialNo >= 1) {
				cell = new PdfPCell();
				table.addCell(cell);

			}
			int subGridIterator = 1;
			cellFont = new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK);
			for (String cellHeader : sunGridcolumnHeaders.split("\\,")) {

				if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
					headerLabel = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
				} else {
					headerLabel = cellHeader.trim();
				}

				cell = new PdfPCell(new Phrase(headerLabel, cellFont));
				cell.setBackgroundColor(GrayColor.LIGHT_GRAY);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setNoWrap(false); // To set wrapping of text in cell.
				// cell.setColspan(2); // To add column span.
				table.addCell(cell);
			}
			/*
			 * if(subGridSize!=0){ for(int i=0;i<subGridSize;i++){ cell = new
			 * PdfPCell(new Phrase("")); table.addCell(cell); } }
			 */
			loadProcurementGrades();
			CityWarehouse subFilter = new CityWarehouse();
			Warehouse coOp = new Warehouse();
			coOp.setId(Long.parseLong(datas[0].toString()));
			subFilter.setCoOperative(coOp);
			ProcurementProduct pp = new ProcurementProduct();
			pp.setId(Long.parseLong(datas[1].toString()));
			subFilter.setProcurementProduct(pp);

			Map subData = new HashMap();
			if (getCurrentTenantId().equalsIgnoreCase("lalteer")) {
				Farmer ff = new Farmer();
				ff.setId(Long.parseLong(datas[2].toString()));
				subFilter.setFarmer(ff);
				super.filter = subFilter;
				subData = readData("coOperativeProcurmentProductAndGrade");
			} else {
				super.filter = subFilter;
				subData = readData("wareHouseProcurmentProductAndGrade");
			}

			for (int i = 0; i < emptyCols; i++) {
				cell = new PdfPCell(new Phrase(""));
				table.addCell(cell);
			}
			List<Object[]> subGridRows = (List<Object[]>) subData.get(ROWS);

			for (Object[] subDatas : subGridRows) {

				cellFont = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK);

				if (serialNo >= 1) {
					cell = new PdfPCell();
					table.addCell(cell);

				}

				if (getCurrentTenantId().equalsIgnoreCase("lalteer")) {
					String procurementProductGrade = String.valueOf(subDatas[3]);

					if (procurememtGradeMap.containsKey(procurementProductGrade)) {

						ProcurementGrade procurementGrade = procurememtGradeMap.get(procurementProductGrade);
						if (!ObjectUtil.isEmpty(procurementGrade)) {

							cell = new PdfPCell(new Phrase(
									!ObjectUtil.isEmpty(procurementGrade.getProcurementVariety())
											? !StringUtil.isEmpty(procurementGrade.getProcurementVariety().getName())
													? procurementGrade.getProcurementVariety().getName() : "NA"
											: "NA",
									cellFont));
							table.addCell(cell);
							if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {

								cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(procurementGrade)
										? !StringUtil.isEmpty(procurementGrade.getName()) ? procurementGrade.getName()
												: "NA"
										: "NA")); // Grade Name
								table.addCell(cell);
							}
						}
					} else {

						cell = new PdfPCell(new Phrase("NA"));
						table.addCell(cell);
						cell = new PdfPCell(new Phrase("NA"));
						table.addCell(cell);
					}

					cell = new PdfPCell(new Phrase(subDatas[6].toString(), cellFont));
					table.addCell(cell);
					cell = new PdfPCell(new Phrase(
							formatter.format(Double.parseDouble(
									formatter.format(Double.parseDouble(String.valueOf(subDatas[5])))) - totalTareWt),
							cellFont)); // Net Weight
					table.addCell(cell);
				} else {
					String procurementProductGrade = String.valueOf(subDatas[2]);

					if (procurememtGradeMap.containsKey(procurementProductGrade)) {

						ProcurementGrade procurementGrade = procurememtGradeMap.get(procurementProductGrade);
						if (!ObjectUtil.isEmpty(procurementGrade)) {

							cell = new PdfPCell(new Phrase(
									!ObjectUtil.isEmpty(procurementGrade.getProcurementVariety())
											? !StringUtil.isEmpty(procurementGrade.getProcurementVariety().getName())
													? procurementGrade.getProcurementVariety().getName() : "NA"
											: "NA",
									cellFont));
							table.addCell(cell);
							if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {

								cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(procurementGrade)
										? !StringUtil.isEmpty(procurementGrade.getName()) ? procurementGrade.getName()
												: "NA"
										: "NA", cellFont)); // Grade Name
								table.addCell(cell);
							}
						}
					} else {

						cell = new PdfPCell(new Phrase("NA"));
						table.addCell(cell);
						if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {

							cell = new PdfPCell(new Phrase("NA"));
							table.addCell(cell);
						}
					}

					if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
					if (subDatas[0] != "" || subDatas[0] != "0" && subDatas[1] != ""
							|| subDatas[1] != "0" && subDatas[2] != "" || subDatas[2] != "0") {
						Object totalBag = productDistributionService
								.findNoOfBagByByWarehouseIdProductIdAndGradeCode(subDatas[0], subDatas[1], subDatas[2]);
						if (!ObjectUtil.isEmpty(totalBag) && !StringUtil.isEmpty(totalBag) && totalBag != null) {
							cell = new PdfPCell(new Phrase(totalBag.toString(), cellFont));
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							table.addCell(cell);

						} else {
							cell = new PdfPCell(new Phrase("0"));
							table.addCell(cell);

						}
						// of
						// Bags}
					} else {

						cell = new PdfPCell(new Phrase("0"));
						table.addCell(cell);
					}
					}
					if (subDatas[0] != "" || subDatas[0] != "0" && subDatas[1] != ""
							|| subDatas[1] != "0" && subDatas[2] != "" || subDatas[2] != "0") {
						Object totalNetWeight = productDistributionService
								.findNetWeightByWarehouseIdProductIdAndGradeCode(subDatas[0], subDatas[1], subDatas[2]);
						if (!ObjectUtil.isEmpty(totalNetWeight) && !StringUtil.isEmpty(totalNetWeight)
								&& totalNetWeight != null) {
							cell = new PdfPCell(new Phrase(totalNetWeight.toString(), cellFont));
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							table.addCell(cell);
						} else {
							cell = new PdfPCell(new Phrase("0.00"));
							table.addCell(cell);
						}
					} else {

						cell = new PdfPCell(new Phrase("0.00"));
						table.addCell(cell);
					}

				}
				for (int i = 0; i < emptyCols; i++) {
					cell = new PdfPCell(new Phrase(" ", cellFont));
					table.addCell(cell);
				}
			}

			/*
			 * for (int i = 0; i < 3; i++) { cell = new PdfPCell(new Phrase("",
			 * cellFont)); table.addCell(cell); }
			 * 
			 * /* cell = new PdfPCell(new Phrase("", cellFont)); // add empty
			 * cell to sub grid. table.addCell(cell);
			 * 
			 * 
			 * cellFont = new Font(FontFamily.HELVETICA, 8, Font.NORMAL,
			 * GrayColor.GRAYBLACK);
			 * 
			 * loadProcurementGrades(); CityWarehouse subFilter = new
			 * CityWarehouse(); Warehouse coOp = new Warehouse();
			 * coOp.setName(String.valueOf(datas[6]));
			 * subFilter.setCoOperative(coOp); ProcurementProduct pp = new
			 * ProcurementProduct(); pp.setName(String.valueOf(datas[7]));
			 * subFilter.setProcurementProduct(pp); Farmer ff = new Farmer();
			 * ff.setName(String.valueOf(datas[8])); subFilter.setFarmer(ff);
			 * super.filter = subFilter; Map subData =
			 * readData("coOperativeProcurmentProductAndGrade"); List<Object[]>
			 * subGridRows = (List<Object[]>) subData.get(ROWS);
			 * 
			 * /*for (Object[] subDatas : subGridRows) { String quality =
			 * (String) subDatas[1]; if
			 * (getGradeMasterMap().containsKey(quality)) quality =
			 * getGradeMasterMap().get(quality).getName();
			 * 
			 * String procurementProductGrade = (String) subDatas[1];
			 * 
			 * if (procurememtGradeMap.containsKey(procurementProductGrade)) {
			 * ProcurementGrade procurementGrade =
			 * procurememtGradeMap.get(procurementProductGrade); if
			 * (!ObjectUtil.isEmpty(procurementGrade)) { procurementProductGrade
			 * = procurementGrade.getProcurementVariety().getName(); } else {
			 * procurementProductGrade = ""; } }
			 * 
			 * 
			 * cell = new PdfPCell(new Phrase(
			 * StringUtil.isEmpty(procurementProductGrade .trim()) ? "" :
			 * procurementProductGrade.trim())); table.addCell(cell);
			 * 
			 * 
			 * String procurementProductGradeName = (String) subDatas[1];
			 * 
			 * if (procurememtGradeMap.containsKey(procurementProductGradeName))
			 * { ProcurementGrade procurementGrade =
			 * procurememtGradeMap.get(procurementProductGradeName); if
			 * (!ObjectUtil.isEmpty(procurementGrade)) {
			 * procurementProductGradeName = procurementGrade.getName(); } else
			 * { procurementProductGradeName = ""; } }
			 * 
			 * cell = new PdfPCell(new
			 * Phrase(StringUtil.isEmpty(procurementProductGradeName.trim()) ?
			 * "" : procurementProductGradeName.trim())); table.addCell(cell);
			 * 
			 * cell = new PdfPCell(new
			 * Phrase(StringUtil.isEmpty(String.valueOf(subDatas[5]).trim()) ?
			 * "" : String.valueOf(subDatas[5]).trim())); table.addCell(cell);
			 * 
			 * cell = new PdfPCell(new
			 * Phrase(StringUtil.isEmpty(String.valueOf(subDatas[6]).trim()) ?
			 * "" : String.valueOf(subDatas[6]).trim())); table.addCell(cell);
			 * 
			 * 
			 * }
			 */

		}

		document.add(table); // Add table to document.

		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		document.close();
		fileOut.close();
		return stream;

	}

	@SuppressWarnings("unchecked")
	public InputStream getExportDataStream(String exportType) throws IOException {

		Long serialNumber = 0L;

		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		setsDate(IExporter.EXPORT_DAILY.equalsIgnoreCase(exportType) ? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));
		DecimalFormat formatter = new DecimalFormat("###.00");
		Double totalTareWt = 0.0;
		boolean flag = true;

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("exportXLSProcurmentStockTitle"));
		HSSFPatriarch drawing = sheet.createDrawingPatriarch();

		HSSFCellStyle style1 = wb.createCellStyle();
		HSSFCellStyle style2 = wb.createCellStyle();
		HSSFCellStyle style3 = wb.createCellStyle();
		HSSFCellStyle style4 = wb.createCellStyle();
		HSSFCellStyle style5 = wb.createCellStyle();
		HSSFCellStyle style6 = wb.createCellStyle();

		HSSFFont font1 = wb.createFont();
		font1.setFontHeightInPoints((short) 22);

		HSSFFont font2 = wb.createFont();
		font2.setFontHeightInPoints((short) 12);

		HSSFFont font3 = wb.createFont();
		font3.setFontHeightInPoints((short) 10);

		HSSFRow row, titleRow, filterRowTitle = null, filterRow = null;
		HSSFCell cell;

		int imgRow1 = 0;
		int imgRow2 = 4;
		int imgCol1 = 0;
		int imgCol2 = 0;
		int titleRow1 = 2;
		int titleRow2 = 5;
		int count=0;
		int rowNum = 2;
		int colNum = 0;
		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap(); // build branch map to get branch name form branch id.

		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, titleRow1, titleRow2));

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(2);
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("exportXLSProcurmentStockTitle")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);
		if (isMailExport()) {
			rowNum++;
			rowNum++;
			filterRowTitle = sheet.createRow(rowNum++);
			filterRow = sheet.createRow(rowNum++);
		}
		rowNum++;
		 HSSFRow totalGridRowHead = sheet.createRow(rowNum++);
			String[] headerFieldsArr=headerFields.split("###");
			  for(int i=0;i<headerFieldsArr.length;i++)
			  {
				  cell = totalGridRowHead.createCell(count);
					cell.setCellValue(new HSSFRichTextString(String.valueOf(headerFieldsArr[i])));
			//		style4.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
				//	style4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					style4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					cell.setCellStyle(style4);
					font2.setBoldweight((short) 12);
					font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					style4.setFont(font2);
					sheet.setColumnWidth(count, (15 * 550));
					count++;
				  
			  }
			  row = sheet.createRow(rowNum++);
		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		String mainGridColumnHeaders;
		if (StringUtil.isEmpty(branchIdValue)) {
			mainGridColumnHeaders = getLocaleProperty("ExportMainGridHeadingProcurmentStockBranch");

		} else {
			mainGridColumnHeaders = getLocaleProperty("ExportMainGridHeadingProcurmentStock");

		}
		int mainGridIterator = 0;

		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {
			if (StringUtil.isEmpty(branchIdValue)) { // Add branch header to
														// export sheet if main
														// branch logged in.
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
			} else {
				if (!cellHeader.equalsIgnoreCase("Organization")) {
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
			}

		}
		if (!ObjectUtil.isEmpty(this.filter)) {
			if (!ObjectUtil.isEmpty(this.filter.getProcurementProduct())) {
				this.filter.getProcurementProduct().setId(this.filter.getProcurementProduct().getId());
			}
			
		} else {
			this.filter = new CityWarehouse();
			this.filter.setCoOperative(new Warehouse());
		}
		if (!StringUtil.isEmpty(branchIdParma)) {
			filter.setBranchId(branchIdParma);
		}
		
		super.filter = this.filter;
		String proj = "wareHouseAndProcurmentProduct";

		if (getCurrentTenantId().equalsIgnoreCase("lalteer")) {
			proj = "coOperativeAndProcurmentProduct";

		}

		Map data = readData(proj);

		List<Object[]> mainGridRows = (List<Object[]>) data.get(ROWS);
		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;

		for (Object[] datas : mainGridRows) {

			if (filter.getCoOperative().getId() > 0 && flag) {

				style2 = wb.createCellStyle();

				cell = filterRowTitle.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("filter")));
				cell.setCellStyle(style2);
				font2.setBoldweight((short) 12);
				font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				style2.setFont(font2);

				cell = filterRow.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("cooperative")));
				cell.setCellStyle(style2);
				font2.setBoldweight((short) 12);
				font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				style2.setFont(font2);

				cell = filterRow.createCell(2);
				cell.setCellValue(new HSSFRichTextString(
						String.valueOf(datas[3]).trim() + " - " + String.valueOf(datas[2]).trim()));
				cell.setCellStyle(style2);
				font2.setBoldweight((short) 12);
				font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				style2.setFont(font2);
				
			}
			if (!ObjectUtil.isEmpty(this.filter.getProcurementProduct())) {
			if(filter.getProcurementProduct().getId() > 0 ){
				rowNum++;
				cell = filterRow.createCell(3);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("product")));
				cell.setCellStyle(style2);
				font2.setBoldweight((short) 12);
				font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				style2.setFont(font2);
				
				
				cell = filterRow.createCell(4);
				cell.setCellValue(new HSSFRichTextString(
						String.valueOf(datas[5]).trim() + " - " + String.valueOf(datas[4]).trim()));
				cell.setCellStyle(style2);
				font2.setBoldweight((short) 12);
				font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				style2.setFont(font2);
				
				flag = false;
			}
			}
			
			
			row = sheet.createRow(rowNum++);
			colNum = 0;
	
			serialNumber++;
			cell = row.createCell(colNum++);
			style6.setAlignment(CellStyle.ALIGN_CENTER);
			cell.setCellStyle(style6);
			cell.setCellValue(new HSSFRichTextString(String.valueOf(serialNumber)!=null ? String.valueOf(serialNumber) : ""));

			if (StringUtil.isEmpty(branchIdValue)) {
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(branchesMap.get(datas[6]))); // BranchId
			}
			if (getCurrentTenantId().equalsIgnoreCase("lalteer")) {
				/*
				 * cell = row.createCell(colNum++); cell.setCellValue(new
				 * HSSFRichTextString(datas[3].toString()));
				 */ // Co
					// Operative
					// Code
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(datas[6].toString())); // Co
																				// Operative
																				// Name
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(datas[7].toString())); // Product
																				// Name
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(datas[5] == null ? datas[8].toString() : datas[8].toString())); // farmer
																															// name
																															// and
				if(!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){																											// code
				Object totalBag = productDistributionService.findNoOfBagByByWarehouseIdProductIdAndFarmerId(datas[0],
						datas[1], datas[2]);
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(totalBag.toString())); // No
				}																// of
																				// Bags
				Object totalNetWeight = productDistributionService
						.findNetWeightByWarehouseIdProductIdAndFarmerId(datas[0], datas[1], datas[2]);
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(formatter.format(
						Double.parseDouble(formatter.format(Double.parseDouble(String.valueOf(totalNetWeight))))))); // Net
																														// Weight

			} else {
				/*
				 * cell = row.createCell(colNum++); cell.setCellValue(new
				 * HSSFRichTextString(datas[2].toString()));
				 */// Co
					// Operative
					// Code
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(datas[3].toString())); // Co
																				// Operative
																				// Name
				/*
				 * cell = row.createCell(colNum++); cell.setCellValue(new
				 * HSSFRichTextString(datas[4].toString()));
				 */ // Product
					// Code
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(datas[5].toString())); // Product
																				// Name
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(datas[7].toString()));
				
				if(!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
				cell = row.createCell(colNum++);
				if (datas[0] != "" || datas[0] != "0" && datas[1] != "" || datas[1] != "0") {
					Object totalBag = productDistributionService.findNoOfBagByByWarehouseIdProductId(datas[0],
							datas[1]);
					//cell.setCellValue(new HSSFRichTextString(totalBag.toString())); // No
					 cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		 				cell.setCellValue(Double.valueOf(String.valueOf(totalBag.toString())));
					style5.setAlignment(CellStyle.ALIGN_RIGHT);			     	// of
					cell.setCellStyle(style5);																// Bags}
				} else {
					cell.setCellValue(new HSSFRichTextString("0"));
				}
				}
				cell = row.createCell(colNum++);
				if (datas[0] != "" || datas[0] != "0" && datas[1] != "" || datas[1] != "0") {
					Object totalNetWeight = productDistributionService.findNetWeightByWarehouseIdProductId(datas[0],
							datas[1]);
					/*
					 * rows.add(formatter
					 * .format(Double.parseDouble(formatter.format(Double.
					 * parseDouble(String.valueOf(datas[8])))) - totalTareWt));
					 * // Net Weight
					 */
					//cell.setCellValue(new HSSFRichTextString(formatter.format(
						//	Double.parseDouble(formatter.format(Double.parseDouble(String.valueOf(totalNetWeight))))))); // Net
					 style5.setAlignment(CellStyle.ALIGN_RIGHT);			     	// of
					 cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
	 				cell.setCellValue(Double.valueOf(String.valueOf(totalNetWeight)));
						cell.setCellStyle(style5);																								// Weight
				} else {
					cell.setCellValue(new HSSFRichTextString("0.00"));
				}
			}
			String sunGridcolumnHeaders = "";
			HSSFRow subGridRowHead = sheet.createRow(rowNum++);
			sunGridcolumnHeaders = getLocaleProperty("ExportSubGridHeadingProcurmentStock");
			int subGridIterator = 1;

			for (String cellHeader : sunGridcolumnHeaders.split("\\,")) {

				cell = subGridRowHead.createCell(subGridIterator);
				cell.setCellValue(new HSSFRichTextString(cellHeader));
				style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cell.setCellStyle(style3);
				style3.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
				style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				cell.setCellStyle(style3);
				font3.setBoldweight((short) 10);
				font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				style3.setFont(font3);
				subGridIterator++;
			}
			loadProcurementGrades();
			CityWarehouse subFilter = new CityWarehouse();
			Warehouse coOp = new Warehouse();
			coOp.setId(Long.parseLong(datas[0].toString()));
			subFilter.setCoOperative(coOp);
			ProcurementProduct pp = new ProcurementProduct();
			pp.setId(Long.parseLong(datas[1].toString()));
			subFilter.setProcurementProduct(pp);

			Map subData = new HashMap();
			if (getCurrentTenantId().equalsIgnoreCase("lalteer")) {
				Farmer ff = new Farmer();
				ff.setId(Long.parseLong(datas[2].toString()));
				subFilter.setFarmer(ff);
				super.filter = subFilter;

				subData = readData("coOperativeProcurmentProductAndGrade");

			} else {
				super.filter = subFilter;
				subData = readData("wareHouseProcurmentProductAndGrade");
			}
			List<Object[]> subGridRows = (List<Object[]>) subData.get(ROWS);
			for (Object[] subDatas : subGridRows) {
				row = sheet.createRow(rowNum++);
				colNum = 1;
				if (getCurrentTenantId().equalsIgnoreCase("lalteer")) {
					String procurementProductGrade = String.valueOf(subDatas[3]);

					if (procurememtGradeMap.containsKey(procurementProductGrade)) {

						ProcurementGrade procurementGrade = procurememtGradeMap.get(procurementProductGrade);
						if (!ObjectUtil.isEmpty(procurementGrade)) {

							cell = row.createCell(colNum++);
							cell.setCellValue(
									new HSSFRichTextString(!ObjectUtil.isEmpty(procurementGrade.getProcurementVariety())
											? !StringUtil.isEmpty(procurementGrade.getProcurementVariety().getName())
													? procurementGrade.getProcurementVariety().getName() : ""
											: ""));
							if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {
								cell = row.createCell(colNum++);
								cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(procurementGrade)
										? !StringUtil.isEmpty(procurementGrade.getName()) ? procurementGrade.getName()
												: ""
										: "")); // Grade Name
							}
						}
					} else {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(""));
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(""));
					}
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(subDatas[6].toString()));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(formatter.format(
							Double.parseDouble(formatter.format(Double.parseDouble(String.valueOf(subDatas[5]))))
									- totalTareWt))); // Net Weight
				} else {
					String procurementProductGrade = String.valueOf(subDatas[2]);

					if (procurememtGradeMap.containsKey(procurementProductGrade)) {

						ProcurementGrade procurementGrade = procurememtGradeMap.get(procurementProductGrade);
						if (!ObjectUtil.isEmpty(procurementGrade)) {
							cell = row.createCell(colNum++);
							cell.setCellValue(
									new HSSFRichTextString(!ObjectUtil.isEmpty(procurementGrade.getProcurementVariety())
											? !StringUtil.isEmpty(procurementGrade.getProcurementVariety().getName())
													? procurementGrade.getProcurementVariety().getName() : ""
											: ""));
							if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {
								cell = row.createCell(colNum++);
								cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(procurementGrade)
										? !StringUtil.isEmpty(procurementGrade.getName()) ? procurementGrade.getName()
												: ""
										: "")); // Grade Name
							}
						}
					} else {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(""));
						if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(""));
						}
					}
                  
					if(!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
					if (subDatas[0] != "" || subDatas[0] != "0" && subDatas[1] != ""
							|| subDatas[1] != "0" && subDatas[2] != "" || subDatas[2] != "0") {
						Object totalBag = productDistributionService
								.findNoOfBagByByWarehouseIdProductIdAndGradeCode(subDatas[0], subDatas[1], subDatas[2]);
						cell = row.createCell(colNum++);
						//cell.setCellValue(
						//		new HSSFRichTextString(!ObjectUtil.isEmpty(totalBag) ? totalBag.toString() : "")); // No
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		 				cell.setCellValue(Double.valueOf(String.valueOf(totalBag)));
						style5.setAlignment(CellStyle.ALIGN_RIGHT);			     	// of
						cell.setCellStyle(style5);																					// of
																													// Bags}
					} else {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString("0"));
					}
					}
					if (subDatas[0] != "" || subDatas[0] != "0" && subDatas[1] != ""
							|| subDatas[1] != "0" && subDatas[2] != "" || subDatas[2] != "0") {
						Object totalNetWeight = productDistributionService
								.findNetWeightByWarehouseIdProductIdAndGradeCode(subDatas[0], subDatas[1], subDatas[2]);
						/*
						 * rows.add(formatter
						 * .format(Double.parseDouble(formatter.format(
						 * Double.parseDouble(String.valueOf(datas[8])))) -
						 * totalTareWt)); // Net Weight
						 */
						cell = row.createCell(colNum++);
						/*cell.setCellValue(
								new HSSFRichTextString(!ObjectUtil.isEmpty(totalNetWeight)
										? formatter.format(Double.parseDouble(
												formatter.format(Double.parseDouble(String.valueOf(totalNetWeight)))))
										: ""));*/ // Net Weight
						
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		 				cell.setCellValue(Double.valueOf(String.valueOf(totalNetWeight)));
						style5.setAlignment(CellStyle.ALIGN_RIGHT);			     	// of
						cell.setCellStyle(style5);
					} else {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString("0.00"));
					}

				}
			}
			rowNum++;
		}

		/*
		 * loadProcurementGrades(); CityWarehouse subFilter = new
		 * CityWarehouse(); Warehouse coOp = new Warehouse();
		 * coOp.setName(String.valueOf(datas[6]));
		 * subFilter.setCoOperative(coOp); ProcurementProduct pp = new
		 * ProcurementProduct(); pp.setName(String.valueOf(datas[7]));
		 * subFilter.setProcurementProduct(pp); Farmer ff = new Farmer();
		 * ff.setName(String.valueOf(datas[5])); subFilter.setFarmer(ff);
		 * 
		 * 
		 * super.filter = subFilter; Map subData =
		 * readData("coOperativeProcurmentProductAndGrade"); List<Object[]>
		 * subGridRows = (List<Object[]>) subData.get(ROWS);
		 * 
		 * 
		 * 
		 * for (Object[] subDatas : subGridRows) {
		 * 
		 * row = sheet.createRow(rowNum++); colNum = 1;
		 * 
		 * String quality = (String) subDatas[2]; if
		 * (getGradeMasterMap().containsKey(quality)) quality =
		 * getGradeMasterMap().get(quality).getName();
		 * 
		 * String procurementProductGrade = (String) subDatas[3];
		 * 
		 * if (procurememtGradeMap.containsKey(procurementProductGrade)) {
		 * ProcurementGrade procurementGrade =
		 * procurememtGradeMap.get(procurementProductGrade); if
		 * (!ObjectUtil.isEmpty(procurementGrade)) { procurementProductGrade =
		 * procurementGrade.getProcurementVariety().getName(); } else {
		 * procurementProductGrade = ""; } }
		 * 
		 * 
		 * cell = row.createCell(colNum++); cell.setCellValue(new
		 * HSSFRichTextString(StringUtil.isEmpty(procurementProductGrade
		 * .trim()) ? getLocaleProperty("NA") : procurementProductGrade.trim()));
		 * 
		 * 
		 * String procurementProductGradeName = (String) subDatas[3];
		 * 
		 * if (procurememtGradeMap.containsKey(procurementProductGradeName)) {
		 * ProcurementGrade procurementGrade =
		 * procurememtGradeMap.get(procurementProductGradeName); if
		 * (!ObjectUtil.isEmpty(procurementGrade)) { procurementProductGradeName
		 * = procurementGrade.getName(); } else { procurementProductGradeName =
		 * ""; } }
		 * 
		 * cell = row.createCell(colNum++); cell.setCellValue(new
		 * HSSFRichTextString(StringUtil.isEmpty(procurementProductGradeName
		 * .trim()) ? getLocaleProperty("NA") : procurementProductGradeName.trim()));
		 * 
		 * cell = row.createCell(colNum++); cell.setCellValue(new
		 * HSSFRichTextString(StringUtil.isEmpty(String.valueOf(subDatas[5])
		 * .trim()) ? getLocaleProperty("NA") : String.valueOf(subDatas[5]).trim()));
		 * 
		 * cell = row.createCell(colNum++); cell.setCellValue(new
		 * HSSFRichTextString(StringUtil.isEmpty(String.valueOf(subDatas[6])
		 * .trim()) ? getLocaleProperty("NA") : String.valueOf(subDatas[6]).trim()));
		 * 
		 * 
		 * cell = row.createCell(colNum++); cell.setCellValue(new
		 * HSSFRichTextString(formatter.format(Double
		 * .parseDouble(formatter.format(Double.parseDouble(String
		 * .valueOf(subDatas[6])))) - totalTareWt)));
		 * 
		 * }
		 */
		/*for (int i = 0; i <= colNum; i++) {
			sheet.autoSizeColumn(i);
		}*/

		int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		// picture.resize();
		InputStream stream = null;
		String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
				: request.getSession().getId();
		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("procurementStockReportList") + fileNameDateFormat.format(new Date()) + ".xls";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		wb.write(fileOut);
		stream = new FileInputStream(new File(makeDir + fileName));
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
		if (!ObjectUtil.isEmpty(picData)) {
			// picData=Base64.decode(picData);
		}
		// if (ObjectUtil.isEmpty(request)) {
		// picData = productDistributionService.findLogoImageById(1L);
		// } else {
		// String client = "basix-logo.png";
		// String logoPath =
		// request.getSession().getServletContext().getRealPath(
		// "/assets/client/demo/images/" + client);
		// File pic = new File(logoPath);
		// long length = pic.length();
		// picData = new byte[(int) length];
		//
		// FileInputStream picIn = new FileInputStream(pic);
		// picIn.read(picData);
		// }
		if (picData != null)
			index = wb.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
	}

	/**
	 * Load procurement grades.
	 */
	public void loadProcurementGrades() {

		if (ObjectUtil.isEmpty(procurememtGradeMap)) {
			procurememtGradeMap = new LinkedHashMap<String, ProcurementGrade>();
			List<ProcurementGrade> procurementGradeList = productDistributionService.listProcurementGrade();
			for (ProcurementGrade procurementGradeObj : procurementGradeList)
				procurememtGradeMap.put(procurementGradeObj.getCode(), procurementGradeObj);
		}

	}

	/*
	 * @Override public String populatePDF() throws Exception { List <String>
	 * filters = new ArrayList<String>(); List <Object> fields = new
	 * ArrayList<Object>(); List <List <Object>> entityObject = new
	 * ArrayList<List <Object>>(); DecimalFormat format = new
	 * DecimalFormat("0.00"); branchIdValue = getBranchId(); //set value for
	 * branch id. buildBranchMap(); // build branch map to get branch name form
	 * branch id. setMailExport(true);
	 * setsDate(IExporter.EXPORT_MANUAL.equalsIgnoreCase(IExporter.
	 * EXPORT_MANUAL) ? DateUtil .getDateWithoutTime(new Date()) :
	 * DateUtil.convertStringToDate(DateUtil.minusWeek( df.format(new Date()),
	 * 1, DateUtil.DATABASE_DATE_FORMAT), DateUtil.DATABASE_DATE_FORMAT));
	 * filters.add(getLocaleProperty("filter")); if(!StringUtil.isEmpty(filter)) {
	 * filters.add((getLocaleProperty("filter")+" : "+filter)); } String coOperative;
	 * if(!StringUtil.isEmpty(coOperative)) {
	 * filters.add((getLocaleProperty("coOperative")+" : "+coOperative)); }
	 * if(!StringUtil.isEmpty(village)) {
	 * filters.add(getLocaleProperty("village")+" : "+village); }
	 * if(!StringUtil.isEmpty(branchIdParma)) {
	 * filters.add(getLocaleProperty("branchIdParma")+" : "+branchIdParma); }
	 * //Beginning of setting filter values. if
	 * (!ObjectUtil.isEmpty(this.filter)){
	 * if(!ObjectUtil.isEmpty(this.filter.getProcurementProduct()) &&
	 * this.filter.getProcurementProduct().getId() == 0) {
	 * this.filter.setProcurementProduct(null); } } else { this.filter = new
	 * CityWarehouse(); this.filter.setCoOperative(new Warehouse()); }
	 * if(!StringUtil.isEmpty(branchIdParma)){
	 * filter.setBranchId(branchIdParma); } super.filter = this.filter; //End of
	 * setting filter values. Map data = isMailExport() ?
	 * readData("coOperativeAndProcurmentProduct") :
	 * readExportData("coOperativeAndProcurmentProduct"); //Map data =
	 * isMailExport() ? readData() : readExportData(); List<Object[]>
	 * mainGridRows = (List<Object[]>) data.get(ROWS); if
	 * (ObjectUtil.isListEmpty(mainGridRows)) return null; for (Object[] datas :
	 * mainGridRows) { fields = new ArrayList<Object>();
	 * if(StringUtil.isEmpty(branchIdValue)){ //Check if non-main branch has
	 * logged in as for farmer.
	 * fields.add(!StringUtil.isEmpty(branchesMap.get(datas[8])) ?
	 * (branchesMap.get(datas[8])) : ""); }
	 * fields.add(StringUtil.isEmpty(String.valueOf(datas[0]) .trim()) ?
	 * String.valueOf(datas[0]).trim(): "") ;
	 * fields.add(StringUtil.isEmpty(String.valueOf(datas[2]) .trim()) ?
	 * String.valueOf(datas[2]).trim(): "") ;
	 * fields.add(StringUtil.isEmpty(String.valueOf(datas[3]) .trim()) ?
	 * String.valueOf(datas[1]).trim(): "") ;
	 * fields.add(StringUtil.isEmpty(String.valueOf(datas[1]) .trim()) ?
	 * String.valueOf(datas[3]).trim(): "") ;
	 * fields.add(StringUtil.isEmpty(String.valueOf(datas[4]) .trim()) ?
	 * String.valueOf(datas[6]).trim(): "") ;
	 * fields.add(StringUtil.isEmpty(String.valueOf(datas[7]) .trim()) ?
	 * String.valueOf(datas[7]).trim(): "") ;
	 * fields.add(StringUtil.isEmpty(String.valueOf(datas[7]) .trim()) ?
	 * String.valueOf(datas[7]).trim(): "") ; CityWarehouse subFilter = new
	 * CityWarehouse(); Warehouse coOp = new Warehouse();
	 * coOp.setId(Long.valueOf(String.valueOf(datas[5])));
	 * subFilter.setCoOperative(coOp); ProcurementProduct pp = new
	 * ProcurementProduct(); pp.setId(Long.valueOf(String.valueOf(datas[6])));
	 * subFilter.setProcurementProduct(pp); super.filter = subFilter;
	 * loadProcurementGrades(); Map subData = isMailExport() ?
	 * readData("coOperativeProcurmentProductAndGrade") :
	 * readExportData("coOperativeProcurmentProductAndGrade"); List<Object[]>
	 * subGridRows = (List<Object[]>) subData.get(ROWS); for (Object[] subDatas
	 * : subGridRows) { DecimalFormat formatter = new DecimalFormat("###.000");
	 * Double totalTareWt = 0.0; String quality = (String) subDatas[2]; if
	 * (getGradeMasterMap().containsKey(quality)) quality =
	 * getGradeMasterMap().get(quality).getName(); String
	 * procurementProductGrade = (String) subDatas[2]; if
	 * (procurememtGradeMap.containsKey(procurementProductGrade)) {
	 * ProcurementGrade procurementGrade = procurememtGradeMap
	 * .get(procurementProductGrade); if (!ObjectUtil.isEmpty(procurementGrade))
	 * { procurementProductGrade = procurementGrade
	 * .getProcurementVariety().getName(); } else { procurementProductGrade =
	 * ""; } } fields.add(StringUtil.isEmpty(procurementProductGrade .trim()) ?
	 * procurementProductGrade.trim(): "") ; String procurementProductGradeName
	 * = (String) subDatas[2]; if
	 * (procurememtGradeMap.containsKey(procurementProductGradeName)) {
	 * ProcurementGrade procurementGrade = procurememtGradeMap
	 * .get(procurementProductGradeName); if
	 * (!ObjectUtil.isEmpty(procurementGrade)) { procurementProductGradeName =
	 * procurementGrade.getName(); } else { procurementProductGradeName = ""; }
	 * } fields.add(StringUtil.isEmpty(procurementProductGradeName .trim()) ?
	 * procurementProductGradeName.trim() : "") ;
	 * fields.add(StringUtil.isEmpty(String.valueOf( subDatas[5]).trim()) ?
	 * String.valueOf(subDatas[5]) .trim(): "") ;
	 * fields.add(formatter.format(Double
	 * .parseDouble(formatter.format(Double.parseDouble(String
	 * .valueOf(subDatas[6])))) - totalTareWt)); } entityObject.add(fields); }
	 * InputStream is = getPDFExportStream(entityObject,filters);
	 * setPdfFileName(getText("ListFile") + fileNameDateFormat.format(new
	 * Date())); Map<String, InputStream> fileMap = new HashMap<String,
	 * InputStream>(); fileMap.put(getPdfFileName(), is);
	 * setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText(
	 * "ListFile"), fileMap, ".pdf")); return "pdf"; }
	 */
	public IClientService getClientService() {

		return clientService;
	}

	public void setClientService(IClientService clientService) {

		this.clientService = clientService;
	}

	public String getExportLimit() {

		return exportLimit;
	}

	public void setExportLimit(String exportLimit) {

		this.exportLimit = exportLimit;
	}

	public String getFarmerId() {

		return farmerId;
	}

	public void setFarmerId(String farmerId) {

		this.farmerId = farmerId;
	}

	public String getProductId() {

		return productId;
	}

	public void setProductId(String productId) {

		this.productId = productId;
	}

	public Map<String, CityWarehouse> getCityWarehouseGradeMap() {

		return cityWarehouseGradeMap;
	}

	public void setCityWarehouseGradeMap(Map<String, CityWarehouse> cityWarehouseGradeMap) {

		this.cityWarehouseGradeMap = cityWarehouseGradeMap;
	}

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	public Map<String, String> getFields() {

		fields.put("1", getText("coOperative"));
		fields.put("2", getLocaleProperty("farmer"));

		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			fields.put("4", getText("app.branch"));
		} else if (StringUtil.isEmpty(getBranchId())) {
			fields.put("3", getText("app.branch"));
		}

		return fields;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}

	public Map<String, ProcurementGrade> getProcurememtGradeMap() {

		return procurememtGradeMap;
	}

	public void setProcurememtGradeMap(Map<String, ProcurementGrade> procurememtGradeMap) {

		this.procurememtGradeMap = procurememtGradeMap;
	}

	public void populateCooperativeList() {
		JSONArray coopArr = new JSONArray();
		List<Object[]> coopList = locationService.listOfCooperativeByProcurement();
		if (!ObjectUtil.isEmpty(coopList)) {
			coopList.forEach(obj -> {
				coopArr.add(getJSONObject(obj[0].toString(), obj[2].toString() + "-" + obj[1].toString()));
			});
		}
		sendAjaxResponse(coopArr);
	}

	public void populateFarmerList() {
		JSONArray farmerArr = new JSONArray();
		List<Object[]> farmerList = farmerService.listFarmerInfoByProcurement();
		if (!ObjectUtil.isEmpty(farmerList)) {
			farmerList.forEach(obj -> {
				farmerArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(farmerArr);
	}
	
	public void populateProductList(){
		JSONArray productArr = new JSONArray();
		List<Object[]> productList = productService.listOfProcurementProductByStock();
		if (!ObjectUtil.isEmpty(productList)) {
			productList.forEach(obj -> {
				productArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(productArr);
	}

	public IProductService getProductService() {
		return productService;
	}

	public void setProductService(IProductService productService) {
		this.productService = productService;
	}
	
    public void populateHeaderFields() throws IOException {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		
		Object[] datas = farmerService.findTotalNoBagsAndNetWeg(farmerId,selectedCoOperative,branchIdParma,branch,productId);
		if(!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
		jsonObject.put("totalNoBags", !StringUtil.isEmpty(datas[0]) ? datas[0] : "0.0");
		}
		jsonObject.put("totalNetWegt", !StringUtil.isEmpty(datas[1]) ? datas[1] : "0.0");
		jsonArray.add(jsonObject);

		response.setContentType("text/JSON");
		PrintWriter out = response.getWriter();
		if (jsonArray.size() > 0)
			out.println(jsonArray.toString());

	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getSelectedCoOperative() {
		return selectedCoOperative;
	}

	public void setSelectedCoOperative(String selectedCoOperative) {
		this.selectedCoOperative = selectedCoOperative;
	}
	public String getHeaderFields() {
			return headerFields;
		}

		public void setHeaderFields(String headerFields) {
			this.headerFields = headerFields;
		}
    
}
