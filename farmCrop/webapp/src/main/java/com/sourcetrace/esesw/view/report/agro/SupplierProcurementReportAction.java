/*
 * ProcurementReportAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view.report.agro;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

import org.apache.commons.io.IOUtils;
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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.profile.GradeMaster;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfig;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfigFilter;
import com.sourcetrace.eses.order.entity.txn.SupplierProcurement;
import com.sourcetrace.eses.order.entity.txn.SupplierProcurementDetail;
import com.sourcetrace.eses.service.IAgentService;
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
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

@SuppressWarnings("unchecked")
public class SupplierProcurementReportAction extends BaseReportAction implements IExporter {

	private static final long serialVersionUID = 1L;
	private static final String NO_OF_BAGS = "bags";
	private static final String GROSS_WEIGHT = "grossWt";
	private static final String NET_WEIGHT = "netWt";
	private static final String DRY_LOSS = "dryLoss";
	private static final String GRADING_LOSS = "gradingLoss";
	private static final String PRODUCT_ID = "productId";
	private static final String GRADE_ID = "gradeId";
	private String mainGridCols;
	private static String subGridCols;
	private static DynamicReportConfig dynamicReportConfig;
	private static DynamicReportConfig subDynamicReportConfig;
	private Object fValue;
	private Object mValue;
	private List<Object[]> seasonList;
	private List<Object[]> farmList;
	private List<Object[]> agentList;
	private List<Object[]> farmerList;
	private List<Object[]> productInfoList;
	private String gridIdentity;
	private Map<String, String> warehouseMap = new HashMap<>();
	private String filterList;
	private String seasonCode;
	private String xlsFileName;
	private InputStream fileInputStream;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private SupplierProcurement filter;
	private Map<String, String> fpoMap = new HashMap<>();
	private Map<String, String> farmerMap = new HashMap<>();
	private List<DynamicReportConfigFilter> reportConfigFilters = new LinkedList<>();
	protected List<String> fields = new ArrayList<String>();
	private String supplierEnabled;
	int serialNo = 1;
	private String daterange;
	private String tenantId;
	private String enableTraceability;
	private String villageName;
	private String farmerId;
	private String selectedProcId;
	private Map<String, GradeMaster> gradeMasterMap;
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private IAgentService agentService;
	@Autowired
	private IProductService productService;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private IPreferencesService preferncesService;
	@Autowired
	private IProductDistributionService productDistributionService;

	private static final String QR_CODE_SPLITER = "#QR_CODE#";

	private String selectedPrinter;
	private String selectedFieldStaff;

	public String list() {
		Calendar currentDate = Calendar.getInstance();
		Calendar cal = (Calendar) currentDate.clone();
		cal.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) - 1);
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		super.startDate = df.format(cal.getTime());
		super.endDate = df.format(currentDate.getTime());
		setEnableTraceability(preferncesService.findPrefernceByName(ESESystem.ENABLE_TRACEABILITY));
		daterange = super.startDate + " - " + super.endDate;
		request.setAttribute(HEADING, getText(LIST));
		filter = new SupplierProcurement();
		setTenantId(getCurrentTenantId());
		setSupplierEnabled(preferncesService.findPrefernceByName(ESESystem.ENABLE_SUPPLIER));
		return LIST;

	}

	public String subGridDetail() throws Exception {
		setEnableTraceability(preferncesService.findPrefernceByName(ESESystem.ENABLE_TRACEABILITY));
		SupplierProcurementDetail supplierProcurementDetail = new SupplierProcurementDetail();
		SupplierProcurement supplierProcurement = new SupplierProcurement();
		supplierProcurement.setId(Long.valueOf(id));
		supplierProcurementDetail.setSupplierProcurement(supplierProcurement);
		if (getPage() > 1) {
			serialNo = (getPage() - 1) * 10 + 1;
		}
		super.filter = supplierProcurementDetail;
		Map data = readData();
		return sendJSONResponse(data);

	}

	public String detail() throws Exception {

		setFilter(new SupplierProcurement());
		filter.setAgroTransaction(new AgroTransaction());
		if (!StringUtil.isEmpty(seasonCode)) {

			filter.setSeasonCode(seasonCode);
		}
		if (!StringUtil.isEmpty(selectedFieldStaff)) {
			// filter.getAgroTransaction().setRefAgroTransaction(new
			// AgroTransaction());
			String agentIds[] = selectedFieldStaff.split("-");
			String agentId = agentIds[0].trim();
			filter.getAgroTransaction().setAgentId(agentId);
		}
		/*
		 * if (!StringUtil.isEmpty(farmerId)) {
		 * 
		 * filter.getAgroTransaction().setFarmerId(farmerId); } if
		 * (!StringUtil.isEmpty(villageName)) {
		 * filter.setVillage(locationService.findVillageByName(villageName.trim(
		 * ))); }
		 */
		super.filter = this.filter;
		Map data = readData();
		return sendJSONResponse(data);

	}

	public JSONObject toJSON(Object obj) {
		JSONObject jsonObject = new JSONObject();

		if (obj instanceof SupplierProcurement) {
			SupplierProcurement supplierProcurement = (SupplierProcurement) obj;
			JSONArray rows = new JSONArray();

			rows.add(!ObjectUtil.isEmpty(supplierProcurement.getAgroTransaction())
					? (!StringUtil.isEmpty(supplierProcurement.getAgroTransaction().getTxnTime())
							? DateUtil.getDateByDateTime(supplierProcurement.getAgroTransaction().getTxnTime()) : "")
					: "");

			rows.add(!ObjectUtil.isEmpty(supplierProcurement.getAgroTransaction())
					? (!StringUtil.isEmpty(supplierProcurement.getAgroTransaction().getTxnTime())
							? DateUtil.getMonthByDateTime(supplierProcurement.getAgroTransaction().getTxnTime()) : "")
					: "");

			rows.add(!ObjectUtil.isEmpty(supplierProcurement.getAgroTransaction())
					? (!StringUtil.isEmpty(supplierProcurement.getAgroTransaction().getTxnTime())
							? DateUtil.getYearByDateTime(supplierProcurement.getAgroTransaction().getTxnTime()) : "")
					: "");
			if(!getCurrentTenantId().equalsIgnoreCase("gsma")){
			rows.add(!ObjectUtil.isEmpty(supplierProcurement.getInvoiceNo())
					? (!StringUtil.isEmpty(supplierProcurement.getInvoiceNo()) ? supplierProcurement.getInvoiceNo()
							: "")
					: "");
			}

			HarvestSeason season = farmerService.findSeasonNameByCode(supplierProcurement.getSeasonCode());
			rows.add(!ObjectUtil.isEmpty(season) ? season.getName() : "");

			if (!StringUtil.isEmpty(supplierProcurement.getWarehouseId())
					&& StringUtil.isLong(supplierProcurement.getWarehouseId())) {
				rows.add(getWarehouseMap().containsKey(supplierProcurement.getWarehouseId())
						? getWarehouseMap().get(supplierProcurement.getWarehouseId()) : "");

			} else {
				rows.add("");
			}

			if (!ObjectUtil.isEmpty(supplierProcurement.getAgroTransaction())
					&& !StringUtil.isEmpty(supplierProcurement.getAgroTransaction().getAgentId())) {
				rows.add(supplierProcurement.getAgroTransaction().getAgentName());
			} else {
				rows.add("");
			}

			if (preferncesService.findPrefernceByName(ESESystem.ENABLE_SUPPLIER).equals("1")) {
				//if(getCurrentTenantId().equalsIgnoreCase("kpf"))
					if(getIsKpfBased().equals("1")){
					rows.add(getText("isReg" + supplierProcurement.getIsRegSupplier()));
					rows.add(getText("sup" + supplierProcurement.getProcMasterType()));
				}else{
					rows.add(getText("Master" + supplierProcurement.getProcMasterType()));
				}
				
				
				
				// if
				// (!StringUtil.isEmpty(supplierProcurement.getProcMasterType()))
				// {
				if (!supplierProcurement.getProcMasterType().equalsIgnoreCase("99")
						&& !supplierProcurement.getProcMasterType().equalsIgnoreCase("11")) {

					if (supplierProcurement.getIsRegSupplier() == 0) {

						if (!StringUtil.isEmpty(supplierProcurement.getProcMasterTypeId())) {
							MasterData mData = farmerService
									.findMasterDataIdByCode(supplierProcurement.getProcMasterTypeId());
							//if(getCurrentTenantId().equalsIgnoreCase("kpf"))
								if(getIsKpfBased().equals("1")){
							rows.add(!ObjectUtil.isEmpty(mData) ? mData.getName() : "");
							}else{
								if(supplierProcurement.getProcMasterType().equalsIgnoreCase("1")){
									rows.add(!ObjectUtil.isEmpty(mData) ? mData.getName() : "");
								}else{
						
								MasterData mtrader = farmerService
										.findMasterDataIdByCode(supplierProcurement.getTrader());
								rows.add(!ObjectUtil.isEmpty(mData) ? mData.getName()+" - "+(!ObjectUtil.isEmpty(mtrader)? mtrader.getName() :"") : "");
								}
							}
						} else {
							rows.add("NA");
						}
					} else {
						rows.add(!StringUtil.isEmpty(supplierProcurement.getProcMasterTypeId())
								? supplierProcurement.getProcMasterTypeId() : "");
					}
				} else if (supplierProcurement.getProcMasterType().equalsIgnoreCase("11")) {
					if (!StringUtil.isEmpty(supplierProcurement.getProcMasterTypeId())) {
						Warehouse samithi = locationService
								.findSamithiById(Long.valueOf(supplierProcurement.getProcMasterTypeId()));
						rows.add(!ObjectUtil.isEmpty(samithi) ? samithi.getName() : "");
					} else {
						rows.add("NA");
					}
				}

				else {
					// rows.add("NA");
				if(!getCurrentTenantId().equalsIgnoreCase("gsma")){
					rows.add("NA");
				}

				}
			}

			// rows.add(!ObjectUtil.isEmpty(supplierProcurement.getVillage()) ?
			// supplierProcurement.getVillage().getName() : "");

			Map<String, Object> details = getTotalProductDetails(supplierProcurement);

			rows.add(CurrencyUtil.getDecimalFormat((Double) details.get(NET_WEIGHT), "##.00"));

			rows.add(!ObjectUtil.isEmpty(supplierProcurement.getTotalProVal())
					? CurrencyUtil.thousandSeparator(supplierProcurement.getTotalProVal()) : "0.00");
			//if(getCurrentTenantId().equalsIgnoreCase("kpf"))
				if(getIsKpfBased().equals("1")){
				rows.add(!ObjectUtil.isEmpty(supplierProcurement.getTotalLabourCost())
						? CurrencyUtil.thousandSeparator(supplierProcurement.getTotalLabourCost()) : "0.00");
				
				rows.add(!ObjectUtil.isEmpty(supplierProcurement.getTransportCost())
						? CurrencyUtil.thousandSeparator(supplierProcurement.getTransportCost()) : "0.00");
				rows.add(!ObjectUtil.isEmpty(supplierProcurement.getTaxAmt())? CurrencyUtil.thousandSeparator(supplierProcurement.getTaxAmt()) : "0.00");
				rows.add(!ObjectUtil.isEmpty(supplierProcurement.getOtherCost())? CurrencyUtil.thousandSeparator(supplierProcurement.getOtherCost()) : "0.00");
				rows.add(!ObjectUtil.isEmpty(supplierProcurement.getInvoiceValue())
						? CurrencyUtil.thousandSeparator(supplierProcurement.getInvoiceValue()) : "0.00");
			}

			/*
			 * if ((!StringUtil.isEmpty(supplierProcurement.getLatitude()) &&
			 * !StringUtil.isEmpty(supplierProcurement.getLongitude()))) {
			 * rows.add("<button class='faMap' title='" +
			 * getText("farm.map.available.title") + "' onclick='showFarmMap(\""
			 * + (!StringUtil.isEmpty(supplierProcurement.getLatitude()) ?
			 * supplierProcurement.getLatitude() : "0") + "\",\"" +
			 * (!StringUtil.isEmpty(supplierProcurement.getLongitude()) ?
			 * supplierProcurement.getLongitude() : "0") + "\")'></button>"); }
			 * else { // No Latlon
			 * rows.add("<button class='no-latLonIcn' title='" +
			 * getText("farm.map.unavailable.title") + "'></button>"); }
			 */

			jsonObject.put("id", supplierProcurement.getId());
			jsonObject.put("cell", rows);
		} else if (obj instanceof SupplierProcurementDetail) {

			SupplierProcurementDetail supplierProcurementDetail = (SupplierProcurementDetail) obj;

			JSONArray rows = new JSONArray();

			rows.add(serialNo);
			//if(getCurrentTenantId().equalsIgnoreCase("kpf"))
				if(getIsKpfBased().equalsIgnoreCase("1")){
				if (supplierProcurementDetail.getSupplierProcurement().getProcMasterType().equals("99")
						|| supplierProcurementDetail.getSupplierProcurement().getProcMasterType().equals("11")) {
					if (supplierProcurementDetail.getIsReg().equalsIgnoreCase("0")) {

						if (!ObjectUtil.isEmpty(supplierProcurementDetail.getFarmer())) {
							/*
							 * rows.add(!StringUtil.isEmpty(
							 * supplierProcurementDetail.getFarmer().getFarmerId())
							 * ? supplierProcurementDetail.getFarmer().getFarmerId()
							 * :"");
							 */
							rows.add(!StringUtil.isEmpty(supplierProcurementDetail.getFarmer().getFirstName())
									? supplierProcurementDetail.getFarmer().getFirstName() + " "
											+ supplierProcurementDetail.getFarmer().getLastName()
									: "NA");
						}
					} else {

						rows.add(!StringUtil.isEmpty(supplierProcurementDetail.getFarmerName())
								? supplierProcurementDetail.getFarmerName() : "NA");

					}

				} else {
					// rows.add("NA");
					rows.add("NA");
				}
			}else{
				rows.add(!ObjectUtil.isEmpty(supplierProcurementDetail.getFarmer()) && !StringUtil.isEmpty(supplierProcurementDetail.getFarmer().getFirstName())
						? supplierProcurementDetail.getFarmer().getFirstName() + " "
								+ supplierProcurementDetail.getFarmer().getLastName()
						: "NA");
			}
			
			if (!ObjectUtil.isEmpty(supplierProcurementDetail.getFarmer())
					&& !ObjectUtil.isEmpty(supplierProcurementDetail.getFarmer().getSamithi())) {
				rows.add(!StringUtil.isEmpty(supplierProcurementDetail.getFarmer().getSamithi().getName())
						? supplierProcurementDetail.getFarmer().getSamithi().getName() : "");
			} else {
				rows.add("NA");
			}
			rows.add(!ObjectUtil.isEmpty(
					supplierProcurementDetail.getProcurementGrade().getProcurementVariety().getProcurementProduct())
							? supplierProcurementDetail.getProcurementGrade().getProcurementVariety()
									.getProcurementProduct().getName()
							: "");

			String quality = supplierProcurementDetail.getProcurementGrade().getName();
			if (getGradeMasterMap().containsKey(quality)) {
				quality = getGradeMasterMap().get(quality).getName();
			}

			rows.add(quality);
			if (!StringUtil.isEmpty(supplierProcurementDetail.getUnit())) {
				FarmCatalogue cat = catalogueService.findCatalogueByCode(supplierProcurementDetail.getUnit());
				rows.add(!ObjectUtil.isEmpty(cat) ? cat.getName() : "");
			} else {
				rows.add("");
			}
			Double price = !ObjectUtil.isEmpty(supplierProcurementDetail.getRatePerUnit())
					? supplierProcurementDetail.getRatePerUnit() : 0D;

			rows.add(supplierProcurementDetail.getNumberOfBags());
			/*if (preferncesService.findPrefernceByName(ESESystem.ENABLE_TRACEABILITY).equals("1")) {
				rows.add(supplierProcurementDetail.getBatchNo());
			}else{
				rows.add("");
			}
			*/
			rows.add(CurrencyUtil.getDecimalFormat(price, "##.00"));

			rows.add(CurrencyUtil.getDecimalFormat(supplierProcurementDetail.getGrossWeight(), "##.00"));

			Double total = price * supplierProcurementDetail.getGrossWeight();
			rows.add(CurrencyUtil.getDecimalFormat(total, "##.00"));

			rows.add(!StringUtil.isEmpty(supplierProcurementDetail.getCropType())
					? getLocaleProperty("cpType" + supplierProcurementDetail.getCropType()) : "NA");
			if(getIsKpfBased().equalsIgnoreCase("1"))
			{
			rows.add("<button class='btn btn-sts' onclick='enablePrinter(" + supplierProcurementDetail.getId()
					+ ")'><i class='fa fa-print' aria-hidden='true'></i></button>");
			}
			jsonObject.put("id", supplierProcurementDetail.getId());
			jsonObject.put("cell", rows);
			serialNo++;

		}

		return jsonObject;
	}

	@SuppressWarnings("unused")
	private Object getMethodValue(String methodName, Object param) {
		Object field = null;
		try {
			@SuppressWarnings("rawtypes")
			Class cls = this.getClass();

			if (param != null) {
				if (param instanceof Object[]) {
					Method setNameMethod = this.getClass().getMethod(methodName, Object[].class);
					field = setNameMethod.invoke(this, param);
				} else {
					Method setNameMethod = this.getClass().getMethod(methodName, String.class);
					field = setNameMethod.invoke(this, param);
				}
			} else {
				Method setNameMethod = this.getClass().getMethod(methodName);
				field = setNameMethod.invoke(this);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return field;
	}

	public Map<String, String> getSeasonsList() {
		Map<String, String> seasonMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(seasonList)) {
			seasonList = farmerService.listSeasonCodeAndName();
		}
		for (Object[] obj : seasonList) {
			seasonMap.put(String.valueOf(obj[0]), String.valueOf(obj[1]));
		}
		return seasonMap;
	}

	public Map<String, String> getUserList() {

		Map<String, String> userMap = new LinkedHashMap<String, String>();
		List<Object[]> agentList = agentService.listMobileUser();
		for (Object[] inspection : agentList) {
			if (!ObjectUtil.isEmpty(inspection)) {
				userMap.put(inspection[0].toString(), inspection[1].toString());
			}

		}

		return userMap;

	}

	public Map<String, String> getFarmersList() {

		Map<String, String> farmerMap = new LinkedHashMap<>();
		List<Object[]> farmerList = farmerService.listFarmerInfoByProcurement();
		List<String> testList = new ArrayList<>();
		if (!ObjectUtil.isEmpty(farmerList)) {

			farmerMap = farmerList.stream()
					.collect(Collectors.toMap(obj -> (String.valueOf(obj[0])), obj -> (String.valueOf(obj[1]))));

		}
		return farmerMap;
	}

	public Map<String, String> getFarmerFirstNameList() {
		Map<String, String> farmerMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(farmerList)) {
			farmerList = farmerService.listFarmerInfo();
		}
		farmerList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			farmerMap.put(String.valueOf(objArr[3]), String.valueOf(objArr[3]));
		});

		return farmerMap;
	}

	public Map<String, String> getFarmsList() {
		Map<String, String> farmMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(farmList)) {
			farmList = farmerService.listFarmInfo();
		}
		farmList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			farmMap.put(String.valueOf(objArr[1]), String.valueOf(objArr[2]));
		});

		return farmMap;
	}

	public Map<String, String> getProcurementProductList() {
		Map<String, String> productMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(productInfoList)) {
			productInfoList = productService.listProcurementGradeInfo();
		}
		productInfoList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			productMap.put(String.valueOf(objArr[0]), String.valueOf(objArr[8]));
		});
		return productMap;
	}

	public Map<String, String> getProcurementProductUnitList() {
		Map<String, String> productMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(productInfoList)) {
			productInfoList = productService.listProcurementGradeInfo();
		}
		productInfoList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			productMap.put(String.valueOf(objArr[0]), String.valueOf(objArr[9]));
		});
		return productMap;
	}

	public Map<String, String> getProcurementVarietyList() {
		Map<String, String> varietyMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(productInfoList)) {
			productInfoList = productService.listProcurementGradeInfo();
		}
		productInfoList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			varietyMap.put(String.valueOf(objArr[0]), String.valueOf(objArr[5]));
		});
		return varietyMap;
	}

	public Map<String, String> getProcurementGradeList() {
		Map<String, String> gradeMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(productInfoList)) {
			productInfoList = productService.listProcurementGradeInfo();
		}
		productInfoList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			gradeMap.put(String.valueOf(objArr[0]), String.valueOf(objArr[2]));
		});
		return gradeMap;
	}

	public Map<String, String> getAgentsList() {
		Map<String, String> agentMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(agentList)) {
			agentList = agentService.listAgentIdName();
		}
		agentList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			agentMap.put(String.valueOf(objArr[0]), String.valueOf(objArr[1]));
		});
		return agentMap;
	}

	public String getSeasonByCode(String code) {
		String season = getSeasonsList().get(code);
		return season;
	}

	public String getFarmerRegUnReg(Object[] code) {

		for (Object obj : code) {
			if (obj != null) {
				return obj.toString();
			}

		}
		return "";
	}

	public String getFarmByCode(String id) {
		String farm = getFarmsList().get(id);
		return farm;
	}

	public String getFarmerById(String id) {
		String farmer = getFarmersList().get(id);
		return farmer;
	}

	public String getAgentByProfile(String profileId) {
		String agent = getAgentsList().get(profileId);
		return agent;
	}

	public String getProcurementGradeById(String id) {
		String grade = getProcurementGradeList().get(id);
		return grade;
	}

	public String getProcurementVarietyById(String id) {
		String variety = getProcurementVarietyList().get(id);
		return variety;
	}

	public String getProcurementProductById(String id) {
		String product = getProcurementProductList().get(id);
		return product;
	}

	public String getProcurementProductUnitById(String id) {
		String product = getProcurementProductUnitList().get(id);
		return product;
	}

	public String populateXLS() throws Exception {

		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getLocaleProperty("supplierProcurementReportList") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(
				FileUtil.createFileInputStreamToZipFile(getText("supplierProcurementReportList"), fileMap, ".xls"));

		return "xls";
	}

	XSSFRow row, filterRowTitle, filterRow1, filterRow2, filterRow3;
	XSSFRow titleRow;
	int colCount, rowCount, titleRow1 = 4, titleRow2 = 6;
	Cell cell;
	Integer cellIndex;

	@Override
	public InputStream getExportDataStream(String exportType) throws IOException {
		InputStream stream;
		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		setsDate(IExporter.EXPORT_DAILY.equalsIgnoreCase(exportType) ? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));
		boolean flag = true;
		DateFormat filterDateFormat = new SimpleDateFormat(FILTERDATE);

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("exportSupplierProcurementTitle"));
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

		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, titleRow1, titleRow2));

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(2);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("exportSupplierProcurementTitle")));
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
			if (geteDate() != null) {
				cell.setCellValue(new HSSFRichTextString(filterDateFormat.format(geteDate())));
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
			if (!StringUtil.isEmpty(selectedFieldStaff)) {
				filterRow1 = sheet.createRow(rowNum++);
				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("agentName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				Agent agent = agentService.findAgentByAgentId(selectedFieldStaff);

				cell = filterRow1.createCell(2);
				cell.setCellValue(new HSSFRichTextString(selectedFieldStaff + "-"
						+ ((!ObjectUtil.isEmpty(agent)) ? agent.getPersonalInfo().getFirstName() : " ")));
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
		String mainGridColumnHeaders = getLocaleProperty("exportColumnHeader");
		int mainGridIterator = 0;

		String enableSupplier = preferncesService.findPrefernceByName(ESESystem.ENABLE_SUPPLIER);
		if (enableSupplier.equals("1")) {
			mainGridColumnHeaders = getLocaleProperty("exportColumnSupplierProcurementHeader");
		}
		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {

			if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
			} else if (cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());
			}

			cell = mainGridRowHead.createCell(mainGridIterator);
			cell.setCellValue(new HSSFRichTextString(cellHeader));
			style2.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
			style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cell.setCellStyle(style2);
			font2.setBoldweight((short) 12);
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			style2.setFont(font2);
			sheet.setColumnWidth(mainGridIterator, (15 * 550));
			mainGridIterator++;
		}

		setFilter(new SupplierProcurement());
		filter.setAgroTransaction(new AgroTransaction());
		if (!StringUtil.isEmpty(seasonCode)) {

			filter.setSeasonCode(seasonCode);
		}
		if (!StringUtil.isEmpty(selectedFieldStaff)) {
			String agentIds[] = selectedFieldStaff.split("-");
			String agentId = agentIds[0].trim();
			filter.getAgroTransaction().setAgentId(agentId);
		}

		super.filter = this.filter;
		Map data = readData();

		List<SupplierProcurement> mainGridRows = (List<SupplierProcurement>) data.get(ROWS);

		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;

		ESESystem preferences = preferncesService.findPrefernceById("1");
		DateFormat genDate = new SimpleDateFormat(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
		enableSupplier = preferncesService.findPrefernceByName(ESESystem.ENABLE_SUPPLIER);

		for (SupplierProcurement supplierProcurement : mainGridRows) {

			// if
			// (getCurrentTenantId().equalsIgnoreCase(ESESystem.KPF_TENANT_ID))
			// {
			row = sheet.createRow(rowNum++);
			colNum = 0;

			cell = row.createCell(colNum++);
			cell.setCellValue(!ObjectUtil.isEmpty(supplierProcurement.getAgroTransaction())
					? (!StringUtil.isEmpty(supplierProcurement.getAgroTransaction().getTxnTime())
							? DateUtil.getDateByDateTime(supplierProcurement.getAgroTransaction().getTxnTime()) : "")
					: "");

			cell = row.createCell(colNum++);
			cell.setCellValue(!ObjectUtil.isEmpty(supplierProcurement.getAgroTransaction())
					? (!StringUtil.isEmpty(supplierProcurement.getAgroTransaction().getTxnTime())
							? DateUtil.getMonthByDateTime(supplierProcurement.getAgroTransaction().getTxnTime()) : "")
					: "");

			cell = row.createCell(colNum++);
			cell.setCellValue(!ObjectUtil.isEmpty(supplierProcurement.getAgroTransaction())
					? (!StringUtil.isEmpty(supplierProcurement.getAgroTransaction().getTxnTime())
							? DateUtil.getYearByDateTime(supplierProcurement.getAgroTransaction().getTxnTime()) : "")
					: "");

			if(!getCurrentTenantId().equalsIgnoreCase("gsma")){
			cell = row.createCell(colNum++);
			cell.setCellValue(!ObjectUtil.isEmpty(supplierProcurement.getInvoiceNo())
					? (!StringUtil.isEmpty(supplierProcurement.getInvoiceNo()) ? supplierProcurement.getInvoiceNo()
							: "")
					: "");
			}
			cell = row.createCell(colNum++);
			if (!StringUtil.isEmpty(supplierProcurement.getSeasonCode())) {
				HarvestSeason season = farmerService.findSeasonNameByCode(supplierProcurement.getSeasonCode());
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(season) ? season.getName() : ""));

			}

			cell = row.createCell(colNum++);
			if (!StringUtil.isEmpty(supplierProcurement.getWarehouseId())
					&& StringUtil.isLong(supplierProcurement.getWarehouseId())) {
				cell.setCellValue(getWarehouseMap().containsKey(supplierProcurement.getWarehouseId())
						? getWarehouseMap().get(supplierProcurement.getWarehouseId()) : "");
			} else {
				cell.setCellValue("");
			}

			cell = row.createCell(colNum++);
			if (!ObjectUtil.isEmpty(supplierProcurement.getAgroTransaction())
					&& !StringUtil.isEmpty(supplierProcurement.getAgroTransaction().getAgentId())) {
				cell.setCellValue(supplierProcurement.getAgroTransaction().getAgentName());
			} else {
				cell.setCellValue("");
			}
			if (preferncesService.findPrefernceByName(ESESystem.ENABLE_SUPPLIER).equals("1")) {
				//if(getCurrentTenantId().equalsIgnoreCase("kpf"))
					if(getIsKpfBased().equals("1")){
				cell = row.createCell(colNum++);
				cell.setCellValue(getText("isReg" + supplierProcurement.getIsRegSupplier()));
				
				cell = row.createCell(colNum++);
				cell.setCellValue(getText("sup" + supplierProcurement.getProcMasterType()));
				}else{
					cell = row.createCell(colNum++);
					cell.setCellValue(getText("Master" + supplierProcurement.getProcMasterType()));
				}
				if (!supplierProcurement.getProcMasterType().equals("99")
						&& !supplierProcurement.getProcMasterType().equals("11")) {					

					if (supplierProcurement.getIsRegSupplier() == 0) {
						if (!StringUtil.isEmpty(supplierProcurement.getProcMasterTypeId())) {
							MasterData mData = farmerService
									.findMasterDataIdByCode(supplierProcurement.getProcMasterTypeId());
							//if(getCurrentTenantId().equalsIgnoreCase("kpf"))
								if(getIsKpfBased().equals("1")){
								cell = row.createCell(colNum++);
								cell.setCellValue(!ObjectUtil.isEmpty(mData) ? mData.getName() : "");
							}else{
								if(supplierProcurement.getProcMasterType().equalsIgnoreCase("1")){
									cell = row.createCell(colNum++);
									cell.setCellValue(!ObjectUtil.isEmpty(mData) ? mData.getName() : "");
									
								}else{
						
								MasterData mtrader = farmerService
										.findMasterDataIdByCode(supplierProcurement.getTrader());
								cell = row.createCell(colNum++);
								cell.setCellValue(!ObjectUtil.isEmpty(mData) ? mData.getName()+" - "+(!ObjectUtil.isEmpty(mtrader)? mtrader.getName() :"") : "");
								
							}
						}

						} else {
							cell = row.createCell(colNum++);
							cell.setCellValue("NA");

						}
					} else {
						cell = row.createCell(colNum++);
						cell.setCellValue(!StringUtil.isEmpty(supplierProcurement.getProcMasterTypeId())
								? supplierProcurement.getProcMasterTypeId() : "");

					}
				} else if (supplierProcurement.getProcMasterType().equalsIgnoreCase("11")) {
					if (!StringUtil.isEmpty(supplierProcurement.getProcMasterTypeId())) {
						Warehouse samithi = locationService
								.findSamithiById(Long.valueOf(supplierProcurement.getProcMasterTypeId()));
						cell = row.createCell(colNum++);
						cell.setCellValue(!ObjectUtil.isEmpty(samithi) ? samithi.getName() : "");
						
					} else {
						cell = row.createCell(colNum++);
						cell.setCellValue("NA");
					}
				}else {
					if(!getCurrentTenantId().equalsIgnoreCase("gsma")){
					cell = row.createCell(colNum++);
					cell.setCellValue("NA");
					}
					/*cell = row.createCell(colNum++);
					cell.setCellValue("NA");*/

				}
			}

			Map<String, Object> details = getTotalProductDetails(supplierProcurement);

			cell = row.createCell(colNum++);
			cell.setCellValue(CurrencyUtil.getDecimalFormat((Double) details.get(NET_WEIGHT), "##.00"));

			cell = row.createCell(colNum++);
			cell.setCellValue(!ObjectUtil.isEmpty(supplierProcurement.getTotalProVal())
					? CurrencyUtil.thousandSeparator(supplierProcurement.getTotalProVal()) : "0.00");
			//if(getCurrentTenantId().equalsIgnoreCase("kpf"))
				if(getIsKpfBased().equals("1")){
				cell = row.createCell(colNum++);
				cell.setCellValue(!ObjectUtil.isEmpty(supplierProcurement.getTotalLabourCost())
						? CurrencyUtil.thousandSeparator(supplierProcurement.getTotalLabourCost()) : "0.00");
				
				cell = row.createCell(colNum++);
				cell.setCellValue(!ObjectUtil.isEmpty(supplierProcurement.getTransportCost())
						? CurrencyUtil.thousandSeparator(supplierProcurement.getTransportCost()) : "0.00");
				
				cell = row.createCell(colNum++);
				cell.setCellValue(!ObjectUtil.isEmpty(supplierProcurement.getTaxAmt())
						? CurrencyUtil.thousandSeparator(supplierProcurement.getTaxAmt()) : "0.00");
				
				
				cell = row.createCell(colNum++);
				cell.setCellValue(!ObjectUtil.isEmpty(supplierProcurement.getOtherCost())
						? CurrencyUtil.thousandSeparator(supplierProcurement.getOtherCost()) : "0.00");
				
				cell = row.createCell(colNum++);
				cell.setCellValue(!ObjectUtil.isEmpty(supplierProcurement.getInvoiceValue())
						? CurrencyUtil.thousandSeparator(supplierProcurement.getInvoiceValue()) : "0.00");
				
			}

			HSSFRow subGridRowHead = sheet.createRow(rowNum++);
			String sunGridcolumnHeaders = getLocaleProperty("exportColumnSupplierProcurementSubHeader");
			int subGridIterator = 1;

			for (String cellHeader : sunGridcolumnHeaders.split("\\,")) {

				if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
					cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
				} else if (cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT)) {
					cellHeader = cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());
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

			for (SupplierProcurementDetail supplierProcurementDetail : supplierProcurement
					.getSupplierProcurementDetails()) {
				row = sheet.createRow(rowNum++);
				colNum = 1;

				//if(getCurrentTenantId().equalsIgnoreCase("kpf"))
					if(getIsKpfBased().equals("1")){
					if (supplierProcurementDetail.getSupplierProcurement().getProcMasterType().equals("99")
							|| supplierProcurementDetail.getSupplierProcurement().getProcMasterType().equals("11")) {
						if (supplierProcurementDetail.getIsReg().equalsIgnoreCase("0")) {

							if (!ObjectUtil.isEmpty(supplierProcurementDetail.getFarmer())) {
								cell = row.createCell(colNum++);
								cell.setCellValue(!StringUtil.isEmpty(supplierProcurementDetail.getFarmer().getFirstName())
										? supplierProcurementDetail.getFarmer().getFirstName() + " "
										+ supplierProcurementDetail.getFarmer().getLastName()
								: "NA");
								
							}
						} else {
							cell = row.createCell(colNum++);
							cell.setCellValue(!StringUtil.isEmpty(supplierProcurementDetail.getFarmerName())
									? supplierProcurementDetail.getFarmerName() : "NA");
							

						}

					} else {
						cell = row.createCell(colNum++);
						cell.setCellValue("NA");
					}
				}else{
					cell = row.createCell(colNum++);
					cell.setCellValue(!ObjectUtil.isEmpty(supplierProcurementDetail.getFarmer())
							&& !StringUtil.isEmpty(supplierProcurementDetail.getFarmer().getFirstName())
							? supplierProcurementDetail.getFarmer().getFirstName() + " "
							+ supplierProcurementDetail.getFarmer().getLastName()
					: "NA");
					
					
				}
				

				if (!ObjectUtil.isEmpty(supplierProcurementDetail.getFarmer())
						&& !ObjectUtil.isEmpty(supplierProcurementDetail.getFarmer().getSamithi())) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							!StringUtil.isEmpty(supplierProcurementDetail.getFarmer().getSamithi().getName())
									? supplierProcurementDetail.getFarmer().getSamithi().getName() : ""));

				} else {
					cell = row.createCell(colNum++);
					cell.setCellValue("NA");
				}
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(
						!ObjectUtil.isEmpty(supplierProcurementDetail.getProcurementGrade()) ? supplierProcurementDetail
								.getProcurementGrade().getProcurementVariety().getProcurementProduct().getName() : ""));

				cell = row.createCell(colNum++);
				cell.setCellValue(
						new HSSFRichTextString(!ObjectUtil.isEmpty(supplierProcurementDetail.getProcurementGrade())
								? supplierProcurementDetail.getProcurementGrade().getName() : ""));

				if (!StringUtil.isEmpty(supplierProcurementDetail.getUnit())) {
					FarmCatalogue cat = catalogueService.findCatalogueByCode(supplierProcurementDetail.getUnit());
					cell = row.createCell(colNum++);
					cell.setCellValue(!ObjectUtil.isEmpty(cat) ? cat.getName() : "");
				} else {
					cell.setCellValue("NA");
				}

				Double price = !ObjectUtil.isEmpty(supplierProcurementDetail.getProcurementGrade())
						? supplierProcurementDetail.getProcurementGrade().getPrice() : 0D;

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(String.valueOf(supplierProcurementDetail.getNumberOfBags())));
				
				
				if (preferncesService.findPrefernceByName(ESESystem.ENABLE_TRACEABILITY).equals("1")) {
					
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(String.valueOf(supplierProcurementDetail.getBatchNo())));
					
				}else{
					cell.setCellValue("NA");
				}

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(CurrencyUtil.getDecimalFormat(price, "##.00")));

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(
						CurrencyUtil.getDecimalFormat(supplierProcurementDetail.getGrossWeight(), "##.00")));

				Double total = price * supplierProcurementDetail.getGrossWeight();
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(CurrencyUtil.getDecimalFormat(total, "##.00")));

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(supplierProcurementDetail.getCropType())
						? getLocaleProperty("cpType" + supplierProcurementDetail.getCropType()) : ""));

			}

			// }

		}

		int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		// picture.resize();
		String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
				: request.getSession().getId();
		File.createTempFile("tmpDir" + id, null);
		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("supplierProcurementReportList") + fileNameDateFormat.format(new Date()) + ".xls";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		wb.write(fileOut);
		File file = new File(makeDir + fileName);
		stream = new FileInputStream(file);
		fileOut.close();
		/*File file = new File(makeDir + fileName);
		stream = new FileInputStream(file);
		fileOut.close();*/

		return stream;

	}

	public void alternateGreenAndWhiteRows(XSSFSheet sheet) {

		SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();
		ConditionalFormattingRule rule1 = sheetCF.createConditionalFormattingRule("MOD(ROW(),2)");
		PatternFormatting fill1 = rule1.createPatternFormatting();
		fill1.setFillBackgroundColor(IndexedColors.TURQUOISE.index);
		fill1.setFillPattern(PatternFormatting.SOLID_FOREGROUND);

		CellRangeAddress[] regions = { CellRangeAddress.valueOf("A7:Z108") };

		sheetCF.addConditionalFormatting(regions, rule1);

	}

	public int getPicIndex(HSSFWorkbook wb) throws IOException {

		int index = -1;

		byte[] picData = null;
		picData = clientService.findLogoByCode(Asset.APP_LOGO);

		if (picData != null)
			index = wb.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
	}

	public int getPicIndex(XSSFWorkbook workbook) throws IOException {

		int index = -1;

		byte[] picData = null;
		picData = clientService.findLogoByCode(Asset.APP_LOGO);

		if (picData != null)
			index = workbook.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
	}

	public String getMainGridCols() {
		return mainGridCols;
	}

	public void setMainGridCols(String mainGridCols) {
		this.mainGridCols = mainGridCols;
	}

	public String getSubGridCols() {
		return subGridCols;
	}

	public void setSubGridCols(String subGridCols) {
		this.subGridCols = subGridCols;
	}

	public static DynamicReportConfig getDynamicReportConfig() {
		return dynamicReportConfig;
	}

	public static void setDynamicReportConfig(DynamicReportConfig dynamicReportConfig) {
		SupplierProcurementReportAction.dynamicReportConfig = dynamicReportConfig;
	}

	public String getGridIdentity() {
		return gridIdentity;
	}

	public void setGridIdentity(String gridIdentity) {
		this.gridIdentity = gridIdentity;
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

	public List<DynamicReportConfigFilter> getReportConfigFilters() {
		return reportConfigFilters;
	}

	public void setReportConfigFilters(List<DynamicReportConfigFilter> reportConfigFilters) {
		this.reportConfigFilters = reportConfigFilters;
	}

	public String getFilterList() {
		return filterList;
	}

	public void setFilterList(String filterList) {
		this.filterList = filterList;
	}

	public static String getDateByDateTime(String date) {
		if (!StringUtil.isEmpty(date)) {
			Date startDate;
			Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
			DateFormat df = new SimpleDateFormat(TXN_TIME_FORMAT);
			try {
				String d = date.substring(0, date.length() - 2);
				startDate = df.parse(d);
				calendar.setTime(startDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			int day = calendar.get(Calendar.DAY_OF_MONTH);
			return String.valueOf(day);
		}
		return null;

	}

	public static String getMonthByDateTime(String date) {
		if (!ObjectUtil.isEmpty(date)) {
			Date startDate;
			Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
			DateFormat df = new SimpleDateFormat(TXN_TIME_FORMAT);
			try {
				String d = date.substring(0, date.length() - 2);
				startDate = df.parse(d);
				calendar.setTime(startDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String month = new SimpleDateFormat("MMM").format(calendar.getTime());
			return String.valueOf(month);
		}
		return null;

	}

	public static String getYearByDateTime(String date) {
		if (!ObjectUtil.isEmpty(date)) {
			Date startDate;
			Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
			DateFormat df = new SimpleDateFormat(TXN_TIME_FORMAT);
			try {
				String d = date.substring(0, date.length() - 2);
				startDate = df.parse(d);
				calendar.setTime(startDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int year = calendar.get(Calendar.YEAR);
			return String.valueOf(year);
		}
		return null;

	}

	public Map<String, String> getWarehouseMap(String id) {
		if (warehouseMap.size() <= 0) {
			warehouseMap = locationService.listWarehouse().stream()
					.collect(Collectors.toMap(warehouse -> id, Warehouse::getName));
		}
		return warehouseMap;
	}

	public String getCropType(String prop) {

		return !StringUtil.isEmpty(prop) ? getLocaleProperty("cpType" + prop) : "";
	}

	public String getSupplier(String supplier) {
		if (preferncesService.findPrefernceByName(ESESystem.ENABLE_SUPPLIER).equals("1")) {
			if (!StringUtil.isEmpty(supplier)) {
				if (supplier.equals("99") || supplier.equals("11") || supplier.equals("4")) {
					if (supplier.equals("99")) {
						return getText("sup99");
					} else if (supplier.equals("4")) {
						return getText("sup4");
					} else {
						return getText("sup11");
					}

				} else {
					return getText("sup" + supplier);
				}
			}

		}
		return null;
	}

	public String getSupplierName(String fpo) {
		if (!StringUtil.isEmpty(fpo)) {
			FarmCatalogue cat = getCatlogueValueByCode(fpo);
			return !ObjectUtil.isEmpty(cat) ? cat.getName() : "";
		}
		return null;
	}

	public String getSeasonCode() {
		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
	}

	public Map<String, String> getWarehouseMap() {
		if (warehouseMap.size() <= 0) {
			warehouseMap = locationService.listWarehouse().stream()
					.collect(Collectors.toMap(warehouse -> String.valueOf(warehouse.getId()), Warehouse::getName));
		}
		return warehouseMap;
	}

	public Map<String, String> getFpoMap() {
		if (fpoMap.size() <= 0) {
			fpoMap = farmerService.listCatelogueType(getText("fpoType")).stream()
					.collect(Collectors.toMap(FarmCatalogue::getCode, FarmCatalogue::getName));
		}
		return fpoMap;
	}

	public Map<String, String> getFarmerMap() {
		if (farmerMap.size() <= 0) {
			farmerService.listFarmerInfo().stream().forEach(farmer -> {
				farmerMap.put(String.valueOf(farmer[1]), String.valueOf(farmer[6]));
			});
		}
		return farmerMap;
	}

	private Map<String, Object> getTotalProductDetails(SupplierProcurement supplierProcurement) {

		long noOfBags = 0;
		double grossWt = 0;
		double netWt = 0;
		long productId = 0;
		long gradeId = 0;
		Map<String, Object> details = new HashMap<String, Object>();
		if (!ObjectUtil.isListEmpty(supplierProcurement.getSupplierProcurementDetails())) {
			for (SupplierProcurementDetail detail : supplierProcurement.getSupplierProcurementDetails()) {
				noOfBags = noOfBags + detail.getNumberOfBags();
				grossWt = grossWt + detail.getGrossWeight();
				netWt = netWt + detail.getNetWeight();
				if (detail.getProcurementProduct() != null) {
					productId = detail.getProcurementProduct().getId();
				}
				if (detail.getProcurementGrade() != null) {
					gradeId = detail.getProcurementGrade().getId();
				}

			}
		}
		details.put(NO_OF_BAGS, noOfBags);
		details.put(GROSS_WEIGHT, grossWt);
		details.put(NET_WEIGHT, netWt);
		details.put(PRODUCT_ID, productId);
		details.put(GRADE_ID, gradeId);
		return details;
	}

	public String getDaterange() {
		return daterange;
	}

	public void setDaterange(String daterange) {
		this.daterange = daterange;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	

	public String getEnableTraceability() {
		ESESystem preference = preferncesService.findPrefernceById(ESESystem.SYSTEM_ESE);
		if (!ObjectUtil.isEmpty(preference)) {
			enableTraceability = (preferncesService.findPrefernceByName(ESESystem.ENABLE_TRACEABILITY));

		}
		return enableTraceability;
	}

	public void setEnableTraceability(String enableTraceability) {
		this.enableTraceability = enableTraceability;
	}

	public void setWarehouseMap(Map<String, String> warehouseMap) {
		this.warehouseMap = warehouseMap;
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public void setFpoMap(Map<String, String> fpoMap) {
		this.fpoMap = fpoMap;
	}

	public void setFarmerMap(Map<String, String> farmerMap) {
		this.farmerMap = farmerMap;
	}

	public String getSupplierEnabled() {

		ESESystem preference = preferncesService.findPrefernceById(ESESystem.SYSTEM_ESE);
		if (!ObjectUtil.isEmpty(preference)) {
			supplierEnabled = (preference.getPreferences().get(ESESystem.ENABLE_SUPPLIER));

		}
		return supplierEnabled;
	}

	public void setSupplierEnabled(String supplierEnabled) {

		this.supplierEnabled = supplierEnabled;
	}

	public Map<String, String> getVillageList() {

		Map<String, String> farmerMap = new LinkedHashMap<>();
		List<Object[]> farmerList = locationService.listOfVillageInfoByProcurement();
		List<String> villagesList = new ArrayList<>();
		if (!ObjectUtil.isEmpty(farmerList)) {
			villagesList = farmerList.stream().map(obj -> String.valueOf(obj[1])).distinct()
					.collect(Collectors.toList());
			farmerMap = villagesList.stream().collect(Collectors.toMap(String::toString, String::toString));

		}
		return farmerMap;
	}

	public Map<String, GradeMaster> getGradeMasterMap() {

		if (ObjectUtil.isEmpty(gradeMasterMap)) {
			gradeMasterMap = new LinkedHashMap<String, GradeMaster>();
			List<GradeMaster> gradeMasterList = productDistributionService.listGradeMaster();
			for (GradeMaster gradeMaster : gradeMasterList)
				gradeMasterMap.put(gradeMaster.getCode(), gradeMaster);
		}
		return gradeMasterMap;

	}

	public String getVillageName() {
		return villageName;
	}

	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}

	public String getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}

	public SupplierProcurement getFilter() {
		return filter;
	}

	public void setFilter(SupplierProcurement filter) {
		this.filter = filter;
	}

	public String getSelectedPrinter() {
		return selectedPrinter;
	}

	public void setSelectedPrinter(String selectedPrinter) {
		this.selectedPrinter = selectedPrinter;
	}

	public void populatePrintz() {
		try {
			String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
					: request.getSession().getId();
			String makeDir = FileUtil.storeXls(request.getSession().getId());
			String fileName = getCurrentTenantId() + "-" + DateUtil.getRevisionNoDateTimeMilliSec() + ".pdf";

			File file = new File(makeDir + fileName);
			FileWriter outputFileReader = new FileWriter(file);
			PrintWriter outputStream = new PrintWriter(outputFileReader);
			outputStream.println();

			String inLine = null;
			SupplierProcurementDetail supplierProcurementDetail = productDistributionService
					.findSupplierProcurementDetailById(Long.valueOf(getSelectedProcId()));

			StringBuilder sb = new StringBuilder();

			sb = new StringBuilder();
			
			if (supplierProcurementDetail.getSupplierProcurement().getProcMasterType().equals("99")
					|| supplierProcurementDetail.getSupplierProcurement().getProcMasterType().equalsIgnoreCase("11")) {
				if (!ObjectUtil.isEmpty(supplierProcurementDetail.getFarmer())) {
				
					Farm farm = farmerService.findFarmByFarmerId(supplierProcurementDetail.getFarmer().getId());
					String url = request.getRequestURL().toString();
					 URL aURL = new URL(url);
					 String path=aURL.getPath();
					 String fullPath[]= path.split("/", 0);			
					 String urll=aURL.getProtocol()+"://"+aURL.getAuthority()+"/"+fullPath[1];		 
					 String tenant=getCurrentTenantId();					
					String message =urll+"/getTraceDetails/traceDetails.html"+"?traceDetails=%"+farm.getFarmCode();
					sb.append(message);
					/*sb.append(System.getProperty("line.separator"));
					sb.append("Trace your Produce");*/
				}
			} else {
				sb.append(supplierProcurementDetail.getProcurementGrade().getName() + " in ");
				sb.append(System.getProperty("line.separator"));
				sb.append(supplierProcurementDetail.getProcurementGrade().getProcurementVariety().getName());
				sb.append(System.getProperty("line.separator"));
				sb.append("is procured from ");
				sb.append(System.getProperty("line.separator"));
				if (supplierProcurementDetail.getSupplierProcurement().getIsRegSupplier() == 0) {

					if (!StringUtil.isEmpty(supplierProcurementDetail.getSupplierProcurement().getProcMasterTypeId())) {
						MasterData mData = farmerService.findMasterDataIdByCode(
								supplierProcurementDetail.getSupplierProcurement().getProcMasterTypeId());
						if (!ObjectUtil.isEmpty(mData)) {
							sb.append(getLocaleProperty(
									"sup" + supplierProcurementDetail.getSupplierProcurement().getProcMasterType()));
							sb.append(System.getProperty("line.separator"));
							sb.append(mData.getName());
							sb.append(System.getProperty("line.separator"));
							sb.append("Krishi Pragati Foundation Centre");
							sb.append(System.getProperty("line.separator"));
							sb.append("Otur, Pune, Maharashtra");
							sb.append(System.getProperty("line.separator"));
							sb.append("Standard as per NPOP");
							sb.append(System.getProperty("line.separator"));
							sb.append("Visit us : www.go4fresh.in");
						}

					}
				}

			}

			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));

			document.open();
			PdfContentByte cb = writer.getDirectContent();

			
			com.itextpdf.text.Image logo =com.itextpdf.text.Image.getInstance(exportQRLogo());
			logo.scaleAbsolute(150,40); logo.setAlignment(Image.LEFT);
			document.add(logo);
			
			ESESystem preferences = null;
			if (!StringUtil.isEmpty(getBranchId())) {
				preferences = preferncesService.findPrefernceByOrganisationId(getBranchId());
			} else {
				preferences = preferncesService.findPrefernceById("1");
			}

			Font bold = new Font(FontFamily.TIMES_ROMAN, 18, Font.BOLD);

			Paragraph addressLine1 = new Paragraph(preferences.getPreferences().get(ESESystem.ADDRESS_LINE1), bold);
			addressLine1.setAlignment(Element.ALIGN_LEFT);
			
			document.add(addressLine1);

			Paragraph addressLine2 = new Paragraph(preferences.getPreferences().get(ESESystem.ADDRESS_LINE2), bold);
			addressLine2.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(addressLine2);

			Paragraph addressLine3 = new Paragraph(preferences.getPreferences().get(ESESystem.ADDRESS_LINE3), bold);
			addressLine3.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(addressLine3);

			Paragraph addressLine4 = new Paragraph(preferences.getPreferences().get(ESESystem.ADDRESS_LINE4), bold);
			addressLine4.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(addressLine4);

			Paragraph addressLine5 = new Paragraph(preferences.getPreferences().get(ESESystem.ADDRESS_LINE5), bold);
			addressLine5.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(addressLine5);

			Paragraph addressLine6 = new Paragraph(preferences.getPreferences().get(ESESystem.ADDRESS_LINE6), bold);
			addressLine6.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(addressLine6);

			Paragraph addressLine7 = new Paragraph(preferences.getPreferences().get(ESESystem.ADDRESS_LINE7), bold);
			addressLine7.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(addressLine7);

			Paragraph addressLine8 = new Paragraph(preferences.getPreferences().get(ESESystem.ADDRESS_LINE8), bold);
			addressLine8.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(addressLine8);

			Paragraph addressLine9 = new Paragraph(preferences.getPreferences().get(ESESystem.ADDRESS_LINE9), bold);
			addressLine9.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(addressLine9);

			Paragraph addressLine10 = new Paragraph(preferences.getPreferences().get(ESESystem.ADDRESS_LINE10), bold);
			addressLine10.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(addressLine10);

			Paragraph addressLine11 = new Paragraph(preferences.getPreferences().get(ESESystem.ADDRESS_LINE11), bold);
			addressLine11.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(addressLine11);

			Paragraph addressLine12 = new Paragraph(preferences.getPreferences().get(ESESystem.ADDRESS_LINE12), bold);
			addressLine12.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(addressLine12);

			Paragraph addressLine13 = new Paragraph(preferences.getPreferences().get(ESESystem.ADDRESS_LINE13), bold);
			addressLine13.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(addressLine13);

			BarcodeQRCode barcodeQRCode = new BarcodeQRCode(sb.toString(), 1000, 1000, null);
			Image codeQrImage = barcodeQRCode.getImage();
			codeQrImage.setAlignment(Image.LEFT);
			codeQrImage.scaleAbsolute(150, 150);
			document.add(codeQrImage);
			document.add(addressLine1);
			document.close();
			

			response.setContentType("application/pdf");
			OutputStream out = response.getOutputStream();
			out.write(IOUtils.toByteArray(new FileInputStream(file)));
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void populatePrint() {
		try {
			if (!StringUtil.isEmpty(getSelectedPrinter())) {
				StringBuilder sb = new StringBuilder();
				byte[] imageData = new byte[] {};
				imageData = clientService.findLogoByCode(Asset.PRINTER_PRN);
				InputStream is = null;
				BufferedReader inputStream = null;
				if (!ObjectUtil.isEmpty(imageData) || imageData.length != 0) {
					is = new ByteArrayInputStream(imageData);
					inputStream = new BufferedReader(new InputStreamReader(is));
					String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
							: request.getSession().getId();
					String makeDir = FileUtil.storeXls(id);
					String fileName = "print" + DateUtil.getRevisionNumber() + ".prn";
					FileWriter outputFileReader = new FileWriter(makeDir + fileName);
					PrintWriter outputStream = new PrintWriter(outputFileReader);
					outputStream.println();

					String inLine = null;
					SupplierProcurementDetail supplierProcurementDetail = productDistributionService
							.findSupplierProcurementDetailById(Long.valueOf(getSelectedProcId()));
					while ((inLine = inputStream.readLine()) != null) {
						if (inLine.contains(QR_CODE_SPLITER)) {
							sb = new StringBuilder();
							sb.append(supplierProcurementDetail.getProcurementGrade().getName() + " in ");
							sb.append(
									supplierProcurementDetail.getProcurementGrade().getProcurementVariety().getName());
							if (supplierProcurementDetail.getSupplierProcurement().getProcMasterType().equals("99")
									|| supplierProcurementDetail.getSupplierProcurement().getProcMasterType()
											.equalsIgnoreCase("11")) {
								if (!ObjectUtil.isEmpty(supplierProcurementDetail.getFarmer())) {
									sb.append("is grown and harvested as per NPOP from ");
									sb.append(
											supplierProcurementDetail.getFarmer().getVillage().getName() + " Village ");
									sb.append(supplierProcurementDetail.getFarmer().getVillage().getCity().getLocality()
											.getName() + " District ");
									sb.append(supplierProcurementDetail.getFarmer().getVillage().getCity().getLocality()
											.getState().getName() + " State by");
									sb.append(supplierProcurementDetail.getFarmer().getSamithi().getName() + " on "
											+ supplierProcurementDetail.getSupplierProcurement().getAgroTransaction()
													.getTxnTime());
								}
							} else {
								sb.append("is procured from ");

								if (supplierProcurementDetail.getSupplierProcurement().getIsRegSupplier() == 0) {

									if (!StringUtil.isEmpty(
											supplierProcurementDetail.getSupplierProcurement().getProcMasterTypeId())) {
										MasterData mData = farmerService
												.findMasterDataIdByCode(supplierProcurementDetail
														.getSupplierProcurement().getProcMasterTypeId());
										if (!ObjectUtil.isEmpty(mData)) {
											sb.append(getLocaleProperty("sup" + supplierProcurementDetail
													.getSupplierProcurement().getProcMasterType()));
											sb.append(" " + mData.getName());
										}

									}
								}

							}

							inLine = inLine.replace(QR_CODE_SPLITER, sb.toString());
						}
						outputStream.println(inLine);
					}

					outputStream.close();
					inputStream.close();
					is = new BufferedInputStream(new FileInputStream(makeDir + fileName));

					DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
					PrintService service = null;
					PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
					for (PrintService printer : printServices) {
						if (printer.getName().equalsIgnoreCase(getSelectedPrinter())) {
							service = printer;
						}
					}

					if (!ObjectUtil.isEmpty(service)) {
						DocPrintJob job = service.createPrintJob();
						Doc nic = new SimpleDoc(is, flavor, null);
						job.print(nic, null);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<String> getPrintersList() {
		List<String> printersList = new ArrayList<>();
		PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
		Arrays.asList(printServices).stream().forEach(printer -> {
			printersList.add(printer.getName());
		});
		return printersList;
	}

	public String getSelectedFieldStaff() {
		return selectedFieldStaff;
	}

	public void setSelectedFieldStaff(String selectedFieldStaff) {
		this.selectedFieldStaff = selectedFieldStaff;
	}

	public void populateAgentList() {
		JSONArray agentArr = new JSONArray();
		List<Object[]> agentList = agentService.listAgentIdName();
		if (!ObjectUtil.isEmpty(agentList)) {
			agentList.forEach(obj -> {
				agentArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(agentArr);
	}

	public String getSelectedProcId() {
		return selectedProcId;
	}

	public void setSelectedProcId(String selectedProcId) {
		this.selectedProcId = selectedProcId;
	}
	public byte[] exportQRLogo() {

		byte[] picData = clientService.findLogoByCode(Asset.QR_LOGO);
		if (!ObjectUtil.isEmpty(picData)) {
			// picData = Base64.decode(picData);
		}
		return picData;
	}

}