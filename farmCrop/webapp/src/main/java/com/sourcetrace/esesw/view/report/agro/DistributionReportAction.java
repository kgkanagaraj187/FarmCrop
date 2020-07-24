/*
\ * DistributionReportAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view.report.agro;

import java.awt.FontFormatException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
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
import org.springframework.context.ApplicationContext;

import com.ese.entity.util.ESESystem;
import com.google.appengine.repackaged.com.google.common.flogger.parser.ParseException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.opensymphony.xwork2.ActionContext;
import com.sourcetrace.eses.BreadCrumb;
import com.sourcetrace.eses.dao.FarmerDAO;
import com.sourcetrace.eses.dao.IReportDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.FarmCatalogueMaster;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.Distribution;
import com.sourcetrace.eses.order.entity.txn.DistributionDetail;
import com.sourcetrace.eses.order.entity.txn.PMTImageDetails;
import com.sourcetrace.eses.service.AccountService;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IClientService;
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
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.profile.State;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

/**
 * The Class DistributionReportAction.
 */
public class DistributionReportAction extends BaseReportAction implements IExporter {

	private static final long serialVersionUID = 4897676200025482185L;

	private static final String FARMER = "farmer";
	private static final String AGENT = "agent";
	DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
	private static final String RECEIPT_DATE_FORMAT = "dd-MM-yyyy";
	private Distribution filter;
	private IProductDistributionService productDistributionService;
	private IPreferencesService preferncesService;
	private AccountService accser;
	private ILocationService locationService;
	private IFarmerService farmerService;
	private IAgentService agentService;
	private Map<String, String> fields = new LinkedHashMap<String, String>();
	private Map<String, String> filterFields = new LinkedHashMap<String, String>();
	private String distributorId; // farmer id or agent Id
	private String distributorName; // farmer Name or agent Name
	private String fAgentId; // Farmer Report - Agent id filter
	private String fAgentName; // Farmer Report - Agent name filter
	private String selectedFieldStaff; // FieldStaff Report - FS name filter
	private String servicePointName;
	private String type;

	private String disableColumns;
	private String identityForGrid;
	private String receiptNo;
	private String operationType;
	private String season;
	private String id;
	@SuppressWarnings("unchecked")
	private List locationList = new ArrayList(); // city / village list
	private Map<String, String> prodCenterList = new LinkedHashMap<String, String>();
	private List<Season> seasonYearList = new ArrayList<Season>();
	private String location; // city name / village name
	Map<String, String> operationTypeList = new LinkedHashMap<String, String>();
	private Map<String, Object> distributionMap = new LinkedHashMap<String, Object>();
	private String txnTime;
	private String xlsFileName;
	private InputStream fileInputStream;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private boolean registeredFarmer;
	private String farmerId;
	private String farmerName;
	private String enableBatchNo;
	private String product;
	@Autowired
	private IProductService productService;
	private IClientService clientService;
	@Autowired
	private ICatalogueService catalogueService;
	private String branchIdParma;
	private String daterange;
	private String fatherName;
	private String seasonCode;
	private String exportLimit;
	private String stateName;
	private String icsName;
	private String fpo;
	private String headerFields;
	private String agentId;
	private String productId;
	private String samithi;
	private String branch;

	private static List<JSONObject> jsonList_chart = new ArrayList<JSONObject>();
	private String warehouse_chart;
	private String distImgAvil;
	private String chartType;
	Map<String,String> warehouseMap=new HashMap<>();
	
	public DistributionReportAction() {

		System.out.println("ll");
	}

	/**
	 * @see com.sourcetrace.esesw.view.BaseReportAction#list()
	 */
	public String list() throws Exception {

		Calendar currentDate = Calendar.getInstance();
		Calendar cal = (Calendar) currentDate.clone();
		cal.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) - 1);
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		super.startDate = df.format(cal.getTime());
		super.endDate = df.format(currentDate.getTime());

		daterange = super.startDate + " - " + super.endDate;

		type = request.getParameter("type");

		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			setExportLimit(preferences.getPreferences().get(ESESystem.EXPORT_RECORD_LIMIT));
			setDistImgAvil(preferences.getPreferences().get(ESESystem.ENABLE_DISTRIBUTION_IMAGE));
		}
		if (FARMER.equals(type)) {
			fields.put("1", getText("distributionDate"));
			fields.put("2", getText("fs" + type));
			fields.put("3", getText("location" + type));
			fields.put("4", getText("farmer"));
			fields.put("5", getLocaleProperty("fatherName"));
			fields.put("6", getText("product"));

			/*
			 * if ((getIsMultiBranch().equalsIgnoreCase("1") &&
			 * (getIsParentBranch().equals("1") ||
			 * StringUtil.isEmpty(branchIdValue)))) { fields.put("10",
			 * getText("app.branch")); } else if
			 * (StringUtil.isEmpty(getBranchId())) { fields.put("7",
			 * getText("app.branch")); }
			 */

			if (getIsMultiBranch().equalsIgnoreCase("1")) {
				if (StringUtil.isEmpty(getBranchId())) {
					fields.put("10", getText("app.branch"));
				} else if (getIsParentBranch().equals("1")) {
					fields.put("10", getText("app.subBranch"));
				}

			} else if (StringUtil.isEmpty(getBranchId())) {

				fields.put("7", getText("app.branch"));
			}

			fields.put("8", getText("season"));
			if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
				fields.put("11", getLocaleProperty("state.name"));

				fields.put("12", getLocaleProperty("cooperative"));

				fields.put("13", getLocaleProperty("icsName"));
			}

		} else {
			fields.put("1", getText("distributionDate"));
			fields.put("2", getText("fs" + type));
			fields.put("3", getText("location" + type));
			/*
			 * if ((getIsMultiBranch().equalsIgnoreCase("1") &&
			 * (getIsParentBranch().equals("1") ||
			 * StringUtil.isEmpty(branchIdValue)))) { fields.put("10",
			 * getText("app.branch")); } else if
			 * (StringUtil.isEmpty(getBranchId())) { fields.put("7",
			 * getText("app.branch")); }
			 */

			if (getIsMultiBranch().equalsIgnoreCase("1")) {
				if (StringUtil.isEmpty(getBranchId())) {
					fields.put("10", getText("app.branch"));
				} else if (getIsParentBranch().equals("1")) {
					fields.put("10", getText("app.subBranch"));
				}

			} else if (StringUtil.isEmpty(getBranchId())) {

				fields.put("7", getText("app.branch"));
			}

			fields.put("9", getText("season"));
		}

		operationTypeList.put(String.valueOf(ESETxn.ON_LINE), getText("operationType1"));
		operationTypeList.put(String.valueOf(ESETxn.VOID), getText("operationType2"));
		request.setAttribute("fields", fields);
		distributorId = "";
		distributorName = "";
		location = "";
		operationType = "";
		season = "";
		/*
		 * if (FARMER.equals(type)) { locationList =
		 * locationService.listOfCooperatives(); } else { locationList =
		 * locationService.listOfCooperatives(); }
		 */
		setFilter(new Distribution());
		setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));
		request.setAttribute(HEADING, getText(LIST + type));
		return LIST;
	}

	/*
	 * @SuppressWarnings({ "unchecked", "rawtypes" }) protected String
	 * sendJSONResponse(Map data) throws Exception {
	 * 
	 * JSONObject gridData = new JSONObject(); gridData.put(PAGE, getPage());
	 * totalRecords = (Integer) data.get(RECORDS); gridData.put(TOTAL,
	 * java.lang.Math.ceil(totalRecords /
	 * Double.valueOf(Integer.toString(getLimit())))); gridData.put(RECORDS,
	 * totalRecords); List list = (List) data.get(ROWS); JSONArray rows = new
	 * JSONArray(); if (list != null) { branchIdValue = getBranchId(); if
	 * (StringUtil.isEmpty(branchIdValue)) { buildBranchMap(); } for (Object
	 * record : list) { if (FARMER.equals(type)) { rows.add(toJSON(record)); }
	 * else { Distribution dis = (Distribution) record; if
	 * (!StringUtil.isEmpty(product)) {
	 * dis.getDistributionDetails().stream().filter(disDetail->String.valueOf(
	 * disDetail.getProduct().getId()).equals(product)).forEach(disDetail -> {
	 * rows.add(toJSON(disDetail)); }); }else{
	 * dis.getDistributionDetails().stream().forEach(disDetail -> {
	 * rows.add(toJSON(disDetail)); }); }
	 * 
	 * for (DistributionDetail distDetail : dis.getDistributionDetails()) {
	 * rows.add(toJSON(distDetail)); }
	 * 
	 * }
	 * 
	 * } } if (totalRecords > 0) { gridData.put("userdata", userDataToJSON()); }
	 * else { gridData.put("userdata", userDataToJSON()); } gridData.put(ROWS,
	 * rows); // PrintWriter out = response.getWriter();
	 * out.println(gridData.toString());
	 * 
	 * return null; }
	 */
	/**
	 * Load column limit.
	 */
	public void loadColumnLimit() {

		// disable unnecessary columns
		if (FARMER.equalsIgnoreCase(type)) {
			disableColumns = "agroTransaction.comId,agroTransaction.comName,agroTransaction.agentId,agroTransaction.agentName,location,agroTransaction.txnType";
		} else if (AGENT.equalsIgnoreCase(type)) {
			disableColumns = "agroTransaction.comId,agroTransaction.comName,agroTransaction.farmerId,agroTransaction.farmerName,agroTransaction.txnType,agentType";
		}
		try {
			response.setContentType("text/html");
			response.getWriter().write(disableColumns);
		} catch (Exception e) {

		}
	}

	/**
	 * @see com.sourcetrace.esesw.view.BaseReportAction#toJSON(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		type = request.getParameter("type");
		if (identityForGrid == "distribution") {
			if (obj instanceof Distribution) {
				Distribution distribution = (Distribution) obj;
				ESESystem preferences = preferncesService.findPrefernceById("1");
				setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));
				setDistImgAvil(preferncesService.findPrefernceByName(ESESystem.ENABLE_DISTRIBUTION_IMAGE));
				Farmer farmer = farmerService.findFarmerByFarmerId(distribution.getFarmerId());
				// default columns for farmer and agent -start

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
					DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
					rows.add(!ObjectUtil.isEmpty(distribution)
							? (!StringUtil.isEmpty(genDate.format(distribution.getTxnTime()))
									? genDate.format(distribution.getTxnTime()) : "")
							: "");
				}

				// end

				if (!StringUtil.isEmpty(distribution.getServicePointName())) {
					if (getCurrentTenantId().equalsIgnoreCase("gar")) {
						rows.add("Produce Fulfilment Centre Stock");
						//rows.add(distribution.getServicePointName());
						rows.add(warehouseMap.get(distribution.getServicePointId()));
						rows.add("NA");
					}

					rows.add(getLocaleProperty("warehouseStock"));
					//rows.add(distribution.getServicePointName());
					rows.add(warehouseMap.get(distribution.getServicePointId()));
					// rows.add(distribution.getServicePointId() + "-" +
					// distribution.getServicePointName());
					rows.add("NA");

				} else {
					if (getCurrentTenantId().equalsIgnoreCase("gar")) {
						rows.add("PFC Incharge Stock");
						rows.add("NA");
						rows.add(distribution.getAgentName());
					}
					rows.add(getLocaleProperty("fieldstaff.stock"));
					if (!getCurrentTenantId().equals(ESESystem.PRATIBHA_TENANT_ID))
						rows.add("NA");
					else{
						rows.add(warehouseMap.get(distribution.getWarehouseCode()));
						//rows.add(!StringUtil.isEmpty(distribution.getWarehouseName()) ? distribution.getWarehouseName(): "");
					
					}
					rows.add(distribution.getAgentName());
					/*
					 * rows.add(distribution.getAgentName() + "-" +
					 * distribution.getAgentId());
					 */ }

				if (!ObjectUtil.isEmpty(farmer)) {
					rows.add("Registered");
				} else {
					rows.add("Unregistered");
				}

				
				if (distribution.getSamithiName() != null) {
					rows.add(distribution.getFarmerName());
					
					/*
					 * if (!StringUtil.isEmpty(farmer.getLastName()) ||
					 * farmer.getLastName() != null) {
					 * rows.add(farmer.getLastName()); } else { rows.add(""); }
					 */
					if (!ObjectUtil.isEmpty(distribution.getVillage())) {
						rows.add(distribution.getVillage().getVillageName());
					} else {
						rows.add("");
					}
					rows.add("NA");
				} else {
				/*	rows.add(!ObjectUtil.isEmpty(distribution.getAgroTransaction())
							&& !StringUtil.isEmpty(distribution.getAgroTransaction().getFarmerName())
									? distribution.getAgroTransaction().getFarmerName() : "NA");*/
					
					String firstName = distribution.getFarmerName();
					String farmerId = distribution.getFarmerId();
					Farmer f=farmerService.findFarmerByFarmerId(farmerId);
					if(!ObjectUtil.isEmpty(f) && f!=null){
					String linkField = "<a href=farmer_detail.action?id="+f.getId()+" target=_blank>"+firstName+"</a>";
					rows.add(!ObjectUtil.isEmpty(linkField) ? linkField : "");
					}else{
						rows.add(firstName);
					}

					
					/*
					 * rows.add((!ObjectUtil.isEmpty(farmer) &&
					 * !StringUtil.isEmpty(farmer.getLastName()) &&
					 * farmer.getLastName() != null) ? farmer.getLastName() :
					 * " ");
					 */
					// rows.add(distribution.getVillage().getVillageName());
					/*
					 * if (!ObjectUtil.isEmpty(farmer) &&
					 * !StringUtil.isEmpty(farmer.getMobileNumber())) {
					 * rows.add(farmer.getMobileNumber()); } else {
					 * rows.add(distribution.getMobileNumber()); }
					 */
					rows.add(!StringUtil.isEmpty(distribution.getMobileNumber()) ? distribution.getMobileNumber()
							: "NA");
					rows.add(distribution.getVillage().getName());

				}
				if (getCurrentTenantId().equalsIgnoreCase("susagri")) {
					rows.add(!ObjectUtil.isEmpty(farmer) && !ObjectUtil.isEmpty(farmer.getSamithi()) ? farmer.getSamithi().getName() : "");
				}
				rows.add(getText("productDistributionType" + distribution.getFreeDistribution()));
				if (!ObjectUtil.isEmpty(distribution.getDistributionDetails())) {
					Double qty = 0.0;
					{
						for (DistributionDetail distributionDetail : distribution.getDistributionDetails()) {
							qty = qty + distributionDetail.getQuantity();
						}
						rows.add(CurrencyUtil.getDecimalFormat((qty), "##.00"));
					}
				}

				// if (!distribution.getDistributionDetails().isEmpty()) {
				/*
				 * if (distribution.getDistributionDetails().iterator().next().
				 * getSellingPrice() == 0.00) {
				 * 
				 * rows.add("NA"); rows.add("NA"); rows.add("NA");
				 * rows.add("NA"); } else {
				 */
				rows.add(CurrencyUtil.getDecimalFormat(distribution.getTotalAmount(), "##.00"));
				rows.add(CurrencyUtil.getDecimalFormat(distribution.getTax(), "##.00"));
				rows.add(CurrencyUtil.getDecimalFormat(distribution.getFinalAmount(), "##.00"));

				rows.add(CurrencyUtil.getDecimalFormat(distribution.getPaymentAmount(), "##.00"));
				/*
				 * if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.
				 * CHETNA_TENANT_ID)) {
				 * rows.add(CurrencyUtil.getDecimalFormat(distribution.
				 * getPaymentAmount(), "##.000")); }else{
				 * rows.add("CS".equals(distribution.getPaymentMode()) ?
				 * CurrencyUtil.getDecimalFormat(distribution.getPaymentAmount()
				 * , "##.000") : "0.000"); }
				 */
				/*
				 * if ("CS".equals(distribution.getPaymentMode())) {
				 * 
				 * rows.add(CurrencyUtil.getDecimalFormat( distribution.
				 * getPaymentAmount(), "##.000"));
				 * rows.add(CurrencyUtil.getDecimalFormat(
				 * distribution.getFinalAmount() -
				 * distribution.getPaymentAmount(), "##.000"));
				 * 
				 * } else if ("CR".equals(distribution.getPaymentMode())) {
				 * rows.add("NA"); rows.add(CurrencyUtil.getDecimalFormat(
				 * distribution. getFinalAmount(), "##.000")); } else {
				 * rows.add("NA"); rows.add("NA"); }
				 */
				// }
				/*
				 * rows .add(!ObjectUtil.isEmpty(distribution) ?
				 * (!StringUtil.isEmpty(distribution .getTxnType())) ?
				 * ((distribution.getTxnType()
				 * .equals(Distribution.PRODUCT_DISTRIBUTION_TO_FARMER)) ?
				 * "PRODUCT DISTRIBUTION" : "PRODUCT RETURN") : "" : ""); //
				 * when loading Distribution to Farmer no need to load
				 * Cooperative Manager info, so // make those column // detail
				 * as empty rows.add(""); rows.add("");
				 * rows.add(!ObjectUtil.isEmpty(distribution) ?
				 * (!StringUtil.isEmpty(distribution .getFarmerId()) ?
				 * distribution.getFarmerId() : "") : "");
				 * rows.add(!ObjectUtil.isEmpty(distribution) ?
				 * (!StringUtil.isEmpty(distribution .getFarmerName()) ?
				 * distribution.getFarmerName() : "") : ""); rows.add("");
				 * rows.add(""); rows.add(""); String agentType = "",
				 * populatePrintAction =
				 * "farmerDistributionReport_populatePrintHTML.action?type=farmer";
				 * if (!ObjectUtil.isEmpty(distribution) &&
				 * !StringUtil.isEmpty(distribution.getAgentId())) { Agent agent
				 * = agentService.findAgentByAgentId(distribution.
				 * getAgentId()); agentType = ""; if (!ObjectUtil.isEmpty(agent)
				 * && AgentType.COOPERATIVE_MANAGER.equalsIgnoreCase(agent.
				 * getAgentType() .getCode())) agentType = "*"; else agentType =
				 * ""; rows.add(distribution.getAgentId() + "-" +
				 * distribution.getAgentName()); } else {
				 * rows.add(distribution.getServicePointId() + "-" +
				 * distribution.getServicePointName()); } rows.add("<a href ='"
				 * + populatePrintAction + distribution.getReceiptNumber() +
				 * "' target='_blank'><img src='" + getImagePath(
				 * "/assets/client/demo/images/pdfgreen.jpg") + "'/></a>");
				 */
				// }

				HarvestSeason season = farmerService.findSeasonNameByCode(distribution.getSeasonCode());
				rows.add(!ObjectUtil.isEmpty(season) ? season.getName() : "");
				
				if (type.equalsIgnoreCase("farmer")) {
					if (getDistImgAvil().equals("1")) {
						if (distribution.getPmtImageDetail() != null
								&& !ObjectUtil.isListEmpty(distribution.getPmtImageDetail())) {
							String imgIds = "";
							for (PMTImageDetails pmtImg : distribution.getPmtImageDetail()) {
								if (pmtImg.getPhoto() != null && pmtImg.getPhoto().length > 0)
									imgIds = pmtImg.getId() + "," + imgIds;
							}
							if (imgIds != null && imgIds != "" && !StringUtil.isEmpty(imgIds))
								rows.add(
										"<button class=\"fa fa-picture-o\"\"aria-hidden=\"true\"\" onclick=\"popupWindow('"
												+ imgIds.substring(0, imgIds.length() - 1) + "')\"></button>");
							else
								rows.add("<button class='no-imgIcn'></button>");
						} else {
							rows.add("<button class='no-imgIcn'></button>");
						}
					}

				}
				/*
				 * rows.add(!ObjectUtil.isEmpty(season) ? season.getCode() + "-"
				 * + season.getName() : "");
				 */

				jsonObject.put("id", distribution.getId());
				jsonObject.put("cell", rows);
			} else {

				DistributionDetail distributionDetail = (DistributionDetail) obj;
				if ((getIsMultiBranch().equalsIgnoreCase("1")
						&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

					if (StringUtil.isEmpty(branchIdValue)) {
						rows.add(!StringUtil
								.isEmpty(getBranchesMap().get(distributionDetail.getDistribution().getBranchId()))
										? getBranchesMap().get(getParentBranchMap()
												.get(distributionDetail.getDistribution().getBranchId()))
										: getBranchesMap().get(distributionDetail.getDistribution().getBranchId()));
					}
					rows.add(getBranchesMap().get(distributionDetail.getDistribution().getBranchId()));
				} else {

					if (StringUtil.isEmpty(branchIdValue)) {
						rows.add(branchesMap.get(distributionDetail.getDistribution().getBranchId()));
					}
				}
				ESESystem preferences = preferncesService.findPrefernceById("1");
				setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));

				if (!StringUtil.isEmpty(preferences)) {
					DateFormat genDate = new SimpleDateFormat(
							preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
					rows.add(!ObjectUtil.isEmpty(distributionDetail)
							? (!StringUtil.isEmpty(genDate.format(distributionDetail.getDistribution().getTxnTime()))
									? genDate.format(distributionDetail.getDistribution().getTxnTime()) : "")
							: "");
				}

				/*
				 * rows.add(!ObjectUtil.isEmpty(distributionDetail) ?
				 * distributionDetail.getDistribution().getServicePointName() +
				 * "-" +
				 * distributionDetail.getDistribution().getServicePointId() :
				 * "");
				 */
				/*rows.add(!ObjectUtil.isEmpty(distributionDetail)
						? distributionDetail.getDistribution().getServicePointName() : "");*/
				rows.add(!ObjectUtil.isEmpty(distributionDetail)?warehouseMap.get(distributionDetail.getDistribution().getServicePointId()):"");
				rows.add(!ObjectUtil.isEmpty(distributionDetail) ? distributionDetail.getDistribution().getAgentName()
						: "");
				/*
				 * rows.add(!ObjectUtil.isEmpty(distributionDetail) ?
				 * distributionDetail.getDistribution().getAgentName() + "-" +
				 * distributionDetail.getDistribution().getAgentId() : "");
				 */

				rows.add(!ObjectUtil.isEmpty(distributionDetail.getProduct())
						&& !ObjectUtil.isEmpty(distributionDetail.getProduct().getSubcategory())
						&& !StringUtil.isEmpty(distributionDetail.getProduct().getSubcategory().getName())
								? distributionDetail.getProduct().getSubcategory().getName() : "");
				rows.add(!ObjectUtil.isEmpty(distributionDetail.getProduct())
						&& !StringUtil.isEmpty(distributionDetail.getProduct().getName())
								? distributionDetail.getProduct().getName() : "");
				rows.add(!ObjectUtil.isEmpty(distributionDetail.getProduct())
						&& !StringUtil.isEmpty(distributionDetail.getProduct().getUnit())
								? distributionDetail.getProduct().getUnit() : "");
				if (enableBatchNo.equals("1")) {
					rows.add(distributionDetail.getBatchNo());
				}
				//rows.add(distributionDetail.getExistingQuantity());
				rows.add(CurrencyUtil.getDecimalFormat(Double.valueOf(distributionDetail.getQuantity()), "##.00"));
				//rows.add(distributionDetail.getCurrentQuantity());

				HarvestSeason season = farmerService
						.findSeasonNameByCode(distributionDetail.getDistribution().getSeasonCode());
				rows.add(!ObjectUtil.isEmpty(season) ? season.getName() : "");
				/*
				 * rows.add(!ObjectUtil.isEmpty(season) ? season.getCode() + "-"
				 * + season.getName() : "");
				 */ jsonObject.put("id", distributionDetail.getId());
				jsonObject.put("cell", rows);

				JSONObject jsonObject_chart = new JSONObject();
				jsonObject_chart.put("id", distributionDetail.getDistribution().getServicePointId());
				jsonObject_chart.put("warehouse", !ObjectUtil.isEmpty(distributionDetail)
						? distributionDetail.getDistribution().getServicePointName() : "");
				jsonObject_chart.put("existingQuantity",  NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(distributionDetail.getExistingQuantity())));
				jsonObject_chart.put("distributionQuantity",  NumberFormat.getNumberInstance(Locale.US).format(distributionDetail.getQuantity()));
				jsonObject_chart.put("currentQuantity", NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(distributionDetail.getCurrentQuantity())));
				jsonList_chart.add(jsonObject_chart);

			}

		} else {
			if (obj instanceof DistributionDetail) {
				DecimalFormat df = new DecimalFormat("0.00");
				DistributionDetail distributionDetail = (DistributionDetail) obj;

				ESESystem preferences = preferncesService.findPrefernceById("1");
				setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));

				rows.add(distributionDetail.getProduct().getSubcategory() == null ? ""
						: distributionDetail.getProduct().getSubcategory().getName());
				rows.add(distributionDetail.getProduct() == null ? "" : distributionDetail.getProduct().getName());
				rows.add(distributionDetail.getProduct() == null ? "" : distributionDetail.getProduct().getUnit());
				if (enableBatchNo.equals("1")) {
					rows.add(distributionDetail.getBatchNo());
				}
				// rows.add(distributionDetail.getExistingQuantity());
				rows.add(CurrencyUtil.getDecimalFormat(Double.valueOf(distributionDetail.getExistingQuantity()), "##.00"));
				rows.add(CurrencyUtil.getDecimalFormat(distributionDetail.getCostPrice(), "##.00"));
				/*
				 * if (distributionDetail.getSellingPrice() == 0.00) {
				 * rows.add("NA"); rows.add("NA"); } else {
				 */
				// rows.add(CurrencyUtil.getDecimalFormat(distributionDetail.getSellingPrice(),
				// "##.000"));
				rows.add(CurrencyUtil.getDecimalFormat(Double.valueOf(distributionDetail.getCostPrice())
						* Double.valueOf(distributionDetail.getQuantity()), "##.00"));
				// }

				rows.add(CurrencyUtil.getDecimalFormat(distributionDetail.getQuantity(), "##.00"));
				Double diffQty = 0.00;
				if (!StringUtil.isEmpty(distributionDetail.getExistingQuantity())) {
					diffQty = Double.valueOf(distributionDetail.getExistingQuantity())
							- (distributionDetail.getQuantity());
				}

				rows.add(CurrencyUtil.getDecimalFormat((diffQty), "##.00"));

				jsonObject.put("id", distributionDetail.getId());
				jsonObject.put("cell", rows);

			}
		}
		return jsonObject;
	}

	public String sendJSONResponse(Map data) throws Exception {

		JSONObject gridData = new JSONObject();
		gridData.put(PAGE, getPage());
		Object[] counts = (Object[]) data.get(RECORDS);
		totalRecords = Integer.valueOf(counts[0].toString());
		gridData.put(TOTAL, java.lang.Math.ceil(totalRecords / Double.valueOf(Integer.toString(getLimit()))));
		gridData.put(RECORDS, totalRecords);
		List<Distribution> list = (List<Distribution>) data.get(ROWS);
		JSONArray rows = new JSONArray();
		if (list != null) {
			branchIdValue = getBranchId();
			if (StringUtil.isEmpty(branchIdValue)) {
				buildBranchMap();
			}
			for (Object record : list) {
				rows.add(toJSON(record));
			}
		}
		if (totalRecords > 0) {
			gridData.put("userdata", userDataToJSON());
		} else {
			gridData.put("userdata", userDataToJSON());
		}
		gridData.put(ROWS, rows);
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		gridData.put("finalAmt",
				counts[1] == null ? "0" : CurrencyUtil.getDecimalFormat(Double.valueOf(counts[1].toString()), "##.00"));
		gridData.put("payMent",
				counts[2] == null ? "0" : CurrencyUtil.getDecimalFormat(Double.valueOf(counts[2].toString()), "##.00"));
		gridData.put("qty",
				counts[3] == null ? "0" : CurrencyUtil.getDecimalFormat(Double.valueOf(counts[3].toString()), "##.00"));
		//select sum(dd.quantity),sum(dd.existingQuantity),sum(dd.currentQuantity)
		gridData.put("agentQty",counts[1] == null ? "0" : CurrencyUtil.getDecimalFormat(Double.valueOf(counts[1].toString()), "##.00"));
		gridData.put("agentExistQty", counts[2]==null?"0":CurrencyUtil.getDecimalFormat(Double.valueOf(counts[2].toString()), "##.00"));
		gridData.put("agentCurQty", counts[3]==null?"0":CurrencyUtil.getDecimalFormat(Double.valueOf(counts[3].toString()), "##.00"));
		//
		PrintWriter out = response.getWriter();
		out.println(gridData.toString());

		return null;
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
		List<Warehouse> warehouseList=productDistributionService.listWarehouse();
		warehouseMap = warehouseList.stream().collect(
                Collectors.toMap(Warehouse::getCode, Warehouse::getName));
		setIdentityForGrid("distribution");
		// setReceiptNo(filter.getReceiptNumber());
		// setServicePointName(filter.getAgroTransaction().getServicePointName());

		setFilter(new Distribution());
		type = request.getParameter("type");
		filter.setAgroTransaction(new AgroTransaction());

		if (!StringUtil.isEmpty(type)) {
			if (FARMER.equals(type)) {
				filter.setTxnType(FARMER);
			} else {
				filter.setTxnType(AGENT);
				if(getBranchId()!=null && !StringUtil.isEmpty(getBranchId())){
					filter.setBranchId(getBranchId());
				}
			}
		}

		if (!StringUtil.isEmpty(distributorId)) {
			if (FARMER.equals(type)) {
				filter.getAgroTransaction().setFarmerId(distributorId);
			} else {
				filter.getAgroTransaction().setAgentId(distributorId);
			}
		}
		if (!StringUtil.isEmpty(selectedFieldStaff)) {
			// filter.getAgroTransaction().setRefAgroTransaction(new
			// AgroTransaction());
			String agentIds[] = selectedFieldStaff.split("-");
			String agentId = agentIds[0].trim();
			filter.setAgentId(agentId);
		}

		/*
		 * if(!StringUtil.isEmpty(selectedFieldStaff)) {
		 * filter.setAgentId(selectedFieldStaff); }
		 */

		/*
		 * if (!StringUtil.isEmpty(receiptNo)) {
		 * filter.setReceiptNumber(receiptNo); }
		 */
		if (!StringUtil.isEmpty(operationType)) {
			filter.getAgroTransaction().setOperType(Integer.valueOf(operationType));
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

		if (!StringUtil.isEmpty(servicePointName)) {
			filter.setServicePointName(servicePointName);
		}

		if (!StringUtil.isEmpty(farmerId)) {
			filter.setFarmerName(farmerId);
		}

		if (!StringUtil.isEmpty(product)) {
			filter.setProductId(Long.valueOf(product));
		}

		if (!StringUtil.isEmpty(fatherName)) {
			filter.setFatherName(fatherName);
		}

		if (!StringUtil.isEmpty(location)) {
			if (FARMER.equals(type)) {
				filter.setServicePointId(location);
				if (!StringUtil.isEmpty(farmerId)) {
					filter.setFarmerId(farmerId);
				}

				if (!StringUtil.isEmpty(product)) {
					filter.setProductId(Long.valueOf(product));
				}

			} else {
				filter.setServicePointName(location);
			}
		}

		// filter.setSeasonCode(getCurrentSeasonsCode());
		if (!StringUtil.isEmpty(seasonCode)) {

			/*
			 * filter.setSeasonCode(getCurrentSeasonsCode()); } else {
			 */
			filter.setSeasonCode(seasonCode);
		}
		if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
			if (!StringUtil.isEmpty(stateName)) {
				// State s = locationService.findStateByCode(stateName);
				filter.setStateName(stateName);
			}

			if (!StringUtil.isEmpty(fpo)) {
				filter.setFpo(fpo);
			}

			if (!StringUtil.isEmpty(icsName)) {

				filter.setIcsName(icsName);
			}
		}
		if (getCurrentTenantId().equalsIgnoreCase("susagri")) {
			if (!StringUtil.isEmpty(samithi)) {

				filter.setGroupId(Long.valueOf(samithi));
			}
		}
		Map data = readData();

		setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));
		return sendJSONResponse(data);
	}

	/**
	 * Sub list.
	 * 
	 * @return the jSON object
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings("unchecked")
	public JSONObject subList() throws Exception {

		setId(id);
		setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));
		// To List OrderDetails Based on Id
		List<DistributionDetail> distributionDetailList = productDistributionService
				.listDistributionDetail(Long.parseLong(id));

		List<DistributionDetail> distributionDetailLimitList = productDistributionService
				.listDistributionDetail(Long.parseLong(id), getStartIndex(), getLimit());

		JSONObject gridData = new JSONObject();
		JSONArray rows = new JSONArray();
		// Send to grid to display records
		if (distributionDetailLimitList != null) {
			for (DistributionDetail record : distributionDetailLimitList) {
				rows.add(toJSON(record));
			}
		}
		gridData.put(PAGE, getPage());
		totalRecords = distributionDetailList.size();
		gridData.put(TOTAL, java.lang.Math.ceil(totalRecords / Double.valueOf(Integer.toString(getLimit()))));
		gridData.put(IReportDAO.START_INDEX, getStartIndex());
		gridData.put(IReportDAO.LIMIT, distributionDetailList.size());
		gridData.put(IReportDAO.RECORD_COUNT, distributionDetailLimitList.size());
		gridData.put(ROWS, rows);
		PrintWriter out = response.getWriter();
		out.println(gridData.toString());

		return null;
	}

	/**
	 * @see com.sourcetrace.esesw.view.BaseReportAction#prepare()
	 */
	public void prepare() throws Exception {

		type = request.getParameter("type");
		if (!StringUtil.isEmpty(type)) {
			String actionClassName = ActionContext.getContext().getActionInvocation().getAction().getClass()
					.getSimpleName();
			String content = getLocaleProperty(actionClassName + "." + BreadCrumb.BREADCRUMB + type);
			if (StringUtil.isEmpty(content)
					|| (content.equalsIgnoreCase(actionClassName + "." + BreadCrumb.BREADCRUMB + type))) {
				content = super.getText(BreadCrumb.BREADCRUMB + type, "");
			}
			request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(content));

		} else {
			String actionClassName = ActionContext.getContext().getActionInvocation().getAction().getClass()
					.getSimpleName();
			String content = getLocaleProperty(actionClassName + "." + BreadCrumb.BREADCRUMB);
			if (StringUtil.isEmpty(content)
					|| (content.equalsIgnoreCase(actionClassName + "." + BreadCrumb.BREADCRUMB))) {
				content = super.getText(BreadCrumb.BREADCRUMB, "");
			}
			request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(content));
		}
	}

	/**
	 * Detail transaction.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String detailTransaction() throws Exception {

		if (id != null && !id.equals("")) {
			filter = productDistributionService.findDistributionById(Long.valueOf(id));
			txnTime = dateFormat.format(filter.getAgroTransaction().getTxnTime());
			if (!ObjectUtil.isEmpty(filter)) {
				request.setAttribute(HEADING, getText("distdetail"));
				return "detail";
			}
		}
		request.setAttribute(HEADING, getText(LIST + type));
		return REDIRECT;

	}

	/**
	 * Gets the distributor id.
	 * 
	 * @return the distributor id
	 */
	public String getDistributorId() {

		return distributorId;
	}

	/**
	 * Sets the distributor id.
	 * 
	 * @param distributorId
	 *            the new distributor id
	 */
	public void setDistributorId(String distributorId) {

		this.distributorId = distributorId;
	}

	/**
	 * Gets the distributor name.
	 * 
	 * @return the distributor name
	 */
	public String getDistributorName() {

		return distributorName;
	}

	/**
	 * Sets the distributor name.
	 * 
	 * @param distributorName
	 *            the new distributor name
	 */
	public void setDistributorName(String distributorName) {

		this.distributorName = distributorName;
	}

	/**
	 * Gets the filter.
	 * 
	 * @return the filter
	 */
	public Distribution getFilter() {

		return filter;
	}

	/**
	 * Sets the filter.
	 * 
	 * @param filter
	 *            the new filter
	 */
	public void setFilter(Distribution filter) {

		super.filter = filter;
		this.filter = filter;
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
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {

		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(String type) {

		this.type = type;
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
	 * Sets the disable columns.
	 * 
	 * @param disableColumns
	 *            the new disable columns
	 */
	public void setDisableColumns(String disableColumns) {

		this.disableColumns = disableColumns;
	}

	/**
	 * Gets the disable columns.
	 * 
	 * @return the disable columns
	 */
	public String getDisableColumns() {

		return disableColumns;
	}

	/**
	 * @see com.sourcetrace.esesw.view.BaseReportAction#getId()
	 */
	public String getId() {

		return id;
	}

	/**
	 * @see com.sourcetrace.esesw.view.BaseReportAction#setId(java.lang.String)
	 */
	public void setId(String id) {

		this.id = id;
	}

	/**
	 * Sets the receipt no.
	 * 
	 * @param receiptNo
	 *            the new receipt no
	 */
	public void setReceiptNo(String receiptNo) {

		this.receiptNo = receiptNo;
	}

	/**
	 * Gets the receipt no.
	 * 
	 * @return the receipt no
	 */
	public String getReceiptNo() {

		return receiptNo;
	}

	/**
	 * Sets the location list.
	 * 
	 * @param locationList
	 *            the new location list
	 */
	@SuppressWarnings("unchecked")
	public void setLocationList(List locationList) {

		this.locationList = locationList;
	}

	/**
	 * Gets the location list.
	 * 
	 * @return the location list
	 */
	@SuppressWarnings("unchecked")
	public List getLocationList() {

		return locationList;
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
	 * Sets the location.
	 * 
	 * @param location
	 *            the new location
	 */
	public void setLocation(String location) {

		this.location = location;
	}

	/**
	 * Gets the location.
	 * 
	 * @return the location
	 */
	public String getLocation() {

		return location;
	}

	/**
	 * Gets the operation type.
	 * 
	 * @return the operation type
	 */
	public String getOperationType() {

		return operationType;
	}

	/**
	 * Sets the operation type.
	 * 
	 * @param operationType
	 *            the new operation type
	 */
	public void setOperationType(String operationType) {

		this.operationType = operationType;
	}

	/**
	 * Gets the operation type list.
	 * 
	 * @return the operation type list
	 */
	public Map<String, String> getOperationTypeList() {

		return operationTypeList;
	}

	/**
	 * Sets the operation type list.
	 * 
	 * @param operationTypeList
	 *            the operation type list
	 */
	public void setOperationTypeList(Map<String, String> operationTypeList) {

		this.operationTypeList = operationTypeList;
	}

	/**
	 * Gets the fields.
	 * 
	 * @return the fields
	 */
	public Map<String, String> getFields() {

		return fields;
	}

	/**
	 * Sets the fields.
	 * 
	 * @param fields
	 *            the fields
	 */
	public void setFields(Map<String, String> fields) {

		this.fields = fields;
	}

	/**
	 * Sets the filter fields.
	 * 
	 * @param filterFields
	 *            the filter fields
	 */
	public void setFilterFields(Map<String, String> filterFields) {

		this.filterFields = filterFields;
	}

	/**
	 * Gets the filter fields.
	 * 
	 * @return the filter fields
	 */
	public Map<String, String> getFilterFields() {

		return filterFields;
	}

	/**
	 * Sets the season.
	 * 
	 * @param season
	 *            the new season
	 */
	public void setSeason(String season) {

		this.season = season;
	}

	/**
	 * Gets the season.
	 * 
	 * @return the season
	 */
	public String getSeason() {

		return season;
	}

	/**
	 * Gets the season list.
	 * 
	 * @return the season list
	 */
	/*
	 * public Map<String, String> getSeasonList() {
	 * 
	 * List<Season> seasonList = productDistributionService.listSeasons();
	 * 
	 * Map<String, String> seasonYearList = new LinkedHashMap<String, String>();
	 * for (Season season : seasonList) { seasonYearList.put(season.getCode(),
	 * season.getName());
	 * 
	 * seasonYearList.put(season.getCode(), season.getName() + " - " +
	 * season.getYear());
	 * 
	 * }
	 * 
	 * return seasonYearList; }
	 */
	/**
	 * Sets the season year list.
	 * 
	 * @param seasonYearList
	 *            the new season year list
	 */
	public void setSeasonYearList(List<Season> seasonYearList) {

		this.seasonYearList = seasonYearList;
	}

	/**
	 * Gets the season year list.
	 * 
	 * @return the season year list
	 */
	public List<Season> getSeasonYearList() {

		return seasonYearList;
	}

	/**
	 * Gets the f agent id.
	 * 
	 * @return the f agent id
	 */
	public String getfAgentId() {

		return fAgentId;
	}

	/**
	 * Sets the f agent id.
	 * 
	 * @param fAgentId
	 *            the new f agent id
	 */
	public void setfAgentId(String fAgentId) {

		this.fAgentId = fAgentId;
	}

	/**
	 * Gets the f agent name.
	 * 
	 * @return the f agent name
	 */
	public String getfAgentName() {

		return fAgentName;
	}

	/**
	 * Sets the f agent name.
	 * 
	 * @param fAgentName
	 *            the new f agent name
	 */
	public void setfAgentName(String fAgentName) {

		this.fAgentName = fAgentName;
	}

	/**
	 * Gets the service point name.
	 * 
	 * @return the service point name
	 */
	public String getServicePointName() {

		return servicePointName;
	}

	/**
	 * Sets the service point name.
	 * 
	 * @param servicePointName
	 *            the new service point name
	 */
	public void setServicePointName(String servicePointName) {

		this.servicePointName = servicePointName;
	}

	/**
	 * Sets the txn time.
	 * 
	 * @param txnTime
	 *            the new txn time
	 */
	public void setTxnTime(String txnTime) {

		this.txnTime = txnTime;
	}

	/**
	 * Gets the txn time.
	 * 
	 * @return the txn time
	 */
	public String getTxnTime() {

		return txnTime;
	}

	/**
	 * Gets the farmer service.
	 * 
	 * @return the farmer service
	 */
	public IFarmerService getFarmerService() {

		return farmerService;
	}

	/**
	 * Sets the farmer service.
	 * 
	 * @param farmerService
	 *            the new farmer service
	 */
	public void setFarmerService(IFarmerService farmerService) {

		this.farmerService = farmerService;
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
	 * Gets the farmers list.
	 * 
	 * @return the farmers list
	 */

	/*
	 * public Map<String, String> getFarmersList() { Map<String, String>
	 * farmerMap = new LinkedHashMap<>(); List<Object[]> farmerList =
	 * farmerService.listFarmerInfoByDistribution(); if
	 * (!ObjectUtil.isEmpty(farmerList)) { farmerList.stream().forEach(obj->{
	 * if(!farmerMap.containsKey(obj[1])){
	 * farmerMap.put(String.valueOf(obj[1]),String.valueOf(obj[1])); } });
	 * farmerMap = farmerList.stream() .collect(Collectors.toMap(obj ->
	 * (String.valueOf(obj[0])), obj -> (String.valueOf(obj[1])))); } return
	 * farmerMap; }
	 */

	/*
	 * public List<String[]> getFatherNameList() {
	 * 
	 * List<String[]> fatherNameList = farmerService.listFatherName();
	 * 
	 * return fatherNameList;
	 * 
	 * }
	 */

	public Map<String, String> getFatherNameList() {
		Map<String, String> farmerMap = new LinkedHashMap<>();
		List<Object[]> farmerList = farmerService.listFarmerInfoByDistribution();
		List<String> testList = new ArrayList<>();
		if (!ObjectUtil.isEmpty(farmerList)) {
			testList = farmerList.stream().filter(obj -> (obj[2] != null && !StringUtil.isEmpty(obj[2].toString())))
					.map(obj -> String.valueOf(obj[2])).distinct().collect(Collectors.toList());
			farmerMap = testList.stream().collect(Collectors.toMap(String::toString, String::toString));

		}
		return farmerMap;
	}

	/**
	 * Gets the cooperative managet list.
	 * 
	 * @return the cooperative managet list
	 */
	/*
	 * public Map<String, String> getCooperativeManagetList() {
	 * 
	 * Map<String, String> agentList = new LinkedHashMap<String, String>();
	 * 
	 * List<Agent> agents =
	 * agentService.listAgentByAgentType(AgentType.COOPERATIVE_MANAGER); if
	 * (!ObjectUtil.isEmpty(agents)) { for (Agent agent : agents) {
	 * agentList.put(agent.getProfileId(),
	 * agent.getPersonalInfo().getAgentName() + " - " + agent.getProfileId()); }
	 * }
	 * 
	 * return agentList; }
	 */

	/**
	 * Gets the field staff list.
	 * 
	 * @return the field staff list
	 */
	/*
	 * public List<String> getFieldStaffList() {
	 * 
	 * List<String> agentList = new ArrayList<String>();
	 * 
	 * List<Agent> agents =
	 * agentService.listAgentByAgentType(AgentType.FIELD_STAFF); if
	 * (!ObjectUtil.isEmpty(agents)) { for (Agent agent : agents) {
	 * agentList.add(agent.getPersonalInfo().getAgentName() + " - " +
	 * agent.getProfileId()); } }
	 * 
	 * return agentList; }
	 */

	/*
	 * public Map<String,String> getFieldStaffList(){ Map<String,String>
	 * farmerMap = new LinkedHashMap<>(); List<Object[]> farmerList =
	 * locationService.listOfCooperativesByDistribution();
	 * if(!ObjectUtil.isEmpty(farmerList)){
	 * 
	 * farmerMap=farmerList.stream().collect(Collectors.toMap(obj->(String.
	 * valueOf(obj[2])), obj->(String.valueOf(obj[3]))));
	 * 
	 * } return farmerMap; }
	 */

	/*
	 * public Map<String, String> getFieldStaffList() { Map<String, String>
	 * farmerMap = new LinkedHashMap<>(); List<Object[]> farmerList =
	 * locationService.listOfCooperativesByDistribution(); List<String> testList
	 * = new ArrayList<>(); if (!ObjectUtil.isListEmpty(farmerList)) { testList
	 * = farmerList.stream().filter(obj -> (obj[3]) != null &&
	 * !StringUtil.isEmpty(obj[3].toString())) .map(obj ->
	 * String.valueOf(obj[3])).distinct().collect(Collectors.toList());
	 * farmerMap = testList.stream().collect(Collectors.toMap(String::toString,
	 * String::toString));
	 * 
	 * } return farmerMap; }
	 */
	/**
	 * Sets the selected field staff.
	 * 
	 * @param selectedFieldStaff
	 *            the new selected field staff
	 */
	public void setSelectedFieldStaff(String selectedFieldStaff) {

		this.selectedFieldStaff = selectedFieldStaff;
	}

	/**
	 * Populate print html.
	 * 
	 * @return the string
	 */
	public String populatePrintHTML() {

		String url = request.getQueryString();
		String txnType = "";
		if (url.contains("farmer")) {
			String v = url.replace("type=farmer", "");
			receiptNo = v;
			txnType = Distribution.PRODUCT_DISTRIBUTION_TO_FARMER;
		}
		if (url.contains("agent")) {
			String s = url.replace("type=agent", "");
			receiptNo = s;
			txnType = Distribution.PRODUCT_DISTRIBUTION_TO_FIELDSTAFF;
		}
		initializeDistributionPrintMap();
		if (!StringUtil.isEmpty(receiptNo)) {
			filter = productDistributionService.findDistributionByRecNoAndTxnType(receiptNo, txnType);
			buildTransactionPrintMap(filter);
		}
		return "html";
	}

	/**
	 * Initialize distribution print map.
	 */
	private void initializeDistributionPrintMap() {

		this.distributionMap = new HashMap<String, Object>();
		List<Map<String, Object>> productMapList = new ArrayList<Map<String, Object>>();
		Map<String, Object> totalMap = new LinkedHashMap<String, Object>();
		this.distributionMap.put("recNo", "");
		this.distributionMap.put("fId", "");
		this.distributionMap.put("fName", "");
		this.distributionMap.put("village", "");
		this.distributionMap.put("date", "");
		this.distributionMap.put("distributionAmt", "");
		this.distributionMap.put("paymentAmout", "");
		this.distributionMap.put("productMapList", productMapList);
		this.distributionMap.put("totalInfo", totalMap);
		this.distributionMap.put("agentId", "");
		this.distributionMap.put("agentName", "");
		this.distributionMap.put("openingBal", "0.00");
		this.distributionMap.put("finalBal", "0.00");
		this.distributionMap.put("samithi", "N/A");
		this.distributionMap.put("oBal", 0.00);
		this.distributionMap.put("fBal", 0.00);
		this.distributionMap.put("isAgent", true);
	}

	/**
	 * Builds the transaction print map.
	 * 
	 * @param distribution
	 *            the distribution
	 */
	private void buildTransactionPrintMap(Distribution distribution) {

		List<Map<String, Object>> productMapList = new ArrayList<Map<String, Object>>();
		if (!ObjectUtil.isEmpty(distribution)) {
			double totalQuantity = 0d;
			double totalPricePerUnit = 0d;
			double totalAmount = 0d;
			DateFormat df = new SimpleDateFormat(RECEIPT_DATE_FORMAT);
			if (!ObjectUtil.isEmpty(distribution.getAgroTransaction())) {
				if (!StringUtil.isEmpty(distribution.getAgroTransaction().getReceiptNo())) {
					this.distributionMap.put("recNo", distribution.getAgroTransaction().getReceiptNo());
				}
				if (distribution.getAgroTransaction().getRefAgroTransaction() == null) {
					if (!StringUtil.isEmpty(distribution.getAgroTransaction().getFarmerId())) {
						this.distributionMap.put("fId", distribution.getAgroTransaction().getFarmerId());
					}
					if (!StringUtil.isEmpty(distribution.getAgroTransaction().getFarmerName())) {
						this.distributionMap.put("fName", distribution.getAgroTransaction().getFarmerName());
					}
					if (!ObjectUtil.isEmpty(distribution.getVillage())
							&& !StringUtil.isEmpty(distribution.getVillage().getName())) {
						this.distributionMap.put("village", distribution.getVillage().getName());
					}

				} else {
					this.distributionMap.put("fId",
							distribution.getAgroTransaction().getRefAgroTransaction().getAgentId());

					this.distributionMap.put("fName",
							distribution.getAgroTransaction().getRefAgroTransaction().getAgentName());

					this.distributionMap.put("finalBal",
							CurrencyUtil.thousandSeparator(Math.abs(distribution.getAgroTransaction().getBalAmount())));
					this.distributionMap.put("fBal", distribution.getAgroTransaction().getBalAmount());

				}

				if (!ObjectUtil.isEmpty(distribution.getAgroTransaction().getTxnTime())) {
					this.distributionMap.put("date", df.format(distribution.getAgroTransaction().getTxnTime()));
				}

				if (!StringUtil.isEmpty(distribution.getAgroTransaction().getAgentName())) {
					this.distributionMap.put("agentName", distribution.getAgroTransaction().getAgentName());
				}
				if (!ObjectUtil.isEmpty(distribution.getAgroTransaction().getSamithi())
						&& !StringUtil.isEmpty(distribution.getAgroTransaction().getSamithi().getName())) {
					this.distributionMap.put("samithi", distribution.getAgroTransaction().getSamithi().getName());
				}
				if (!StringUtil.isEmpty(distribution.getAgroTransaction().getAgentId())) {
					this.distributionMap.put("agentId", distribution.getAgroTransaction().getAgentId());
					Agent agent = agentService.findAgentByProfileId(distribution.getAgroTransaction().getAgentId());
					if (!ObjectUtil.isEmpty(agent)) {
						this.distributionMap.put("isAgent", !agent.isCoOperativeManager());
					}
				}

				this.distributionMap.put("distributionAmt",
						CurrencyUtil.thousandSeparator(distribution.getAgroTransaction().getTxnAmount()));
				this.distributionMap.put("openingBal",
						CurrencyUtil.thousandSeparator(Math.abs(distribution.getAgroTransaction().getIntBalance())));
				this.distributionMap.put("oBal", distribution.getAgroTransaction().getIntBalance());

				if (distribution.isPaymentAvailable()) {
					AgroTransaction agroTransactionPaymentRef = productDistributionService
							.findAgroTransactionByRecNoProfTypeTxnDescAndDate(
									distribution.getAgroTransaction().getReceiptNo(),
									distribution.getAgroTransaction().getProfType(),
									Distribution.DISTRIBUTION_PAYMENT_AMOUNT,
									distribution.getAgroTransaction().getTxnTime());
					if (!ObjectUtil.isEmpty(agroTransactionPaymentRef)) {
						this.distributionMap.put("finalBal",
								CurrencyUtil.thousandSeparator(Math.abs(agroTransactionPaymentRef.getBalAmount())));
						this.distributionMap.put("fBal", agroTransactionPaymentRef.getBalAmount());
					}
				} else {
					this.distributionMap.put("finalBal",
							CurrencyUtil.thousandSeparator(Math.abs(distribution.getAgroTransaction().getBalAmount())));
					this.distributionMap.put("fBal", distribution.getAgroTransaction().getBalAmount());
				}
			}

			this.distributionMap.put("paymentAmout", CurrencyUtil.thousandSeparator(distribution.getPaymentAmount()));
			this.distributionMap.put("productMapList", productMapList);
			if (!ObjectUtil.isListEmpty(distribution.getDistributionDetails())) {
				for (DistributionDetail distributionDetail : distribution.getDistributionDetails()) {
					Map<String, Object> productMap = new LinkedHashMap<String, Object>();
					String productName = "";
					if (!ObjectUtil.isEmpty(distributionDetail.getProduct()))
						productName = !StringUtil.isEmpty(distributionDetail.getProduct().getName())
								? distributionDetail.getProduct().getName() : "";
					productMap.put("product", productName);
					productMap.put("quantity",
							CurrencyUtil.getDecimalFormat(distributionDetail.getQuantity(), "##.00"));
					productMap.put("pricePerUnit",
							CurrencyUtil.thousandSeparator(distributionDetail.getPricePerUnit()));
					productMap.put("amount", CurrencyUtil.thousandSeparator(distributionDetail.getSubTotal()));

					totalQuantity += distributionDetail.getQuantity();
					totalPricePerUnit += distributionDetail.getPricePerUnit();
					totalAmount += distributionDetail.getSubTotal();

					productMapList.add(productMap);
				}
			}
			Map<String, Object> totalMap = new LinkedHashMap<String, Object>();
			totalMap.put("totalQuantity", CurrencyUtil.getDecimalFormat(totalQuantity, "##.00"));
			totalMap.put("totalPricePerUnit", CurrencyUtil.thousandSeparator(totalPricePerUnit));
			totalMap.put("totalAmount", CurrencyUtil.thousandSeparator(totalAmount));
			this.distributionMap.put("totalInfo", totalMap);
		}
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
		setXlsFileName(
				getText("DistributionReportList" + type).replace(" ", "_") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(
				FileUtil.createFileInputStreamToZipFile(getText("distributionReportList" + type), fileMap, ".xls"));
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
		HSSFCellStyle style5 = wb.createCellStyle();
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
				filterRow6 = null, filterRow7 = null, filterRow8 = null, filterRow9 = null, filterRow10 = null,
				filterRow11 = null, filterRow14 = null, filterRow12 = null,filterRow13 = null;
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

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(2);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("exportDistributionTitle" + type)));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);
		int count = 0;
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
			//filterRow7 = sheet.createRow(rowNum++);
			/*
			filterRow2 = sheet.createRow(rowNum++);
			filterRow2 = sheet.createRow(rowNum++);
			filterRow2 = sheet.createRow(rowNum++);
			filterRow4 = sheet.createRow(rowNum++);*/
			/*filterRow5 = sheet.createRow(rowNum++);
			filterRow6 = sheet.createRow(rowNum++);
			filterRow7 = sheet.createRow(rowNum++);
			filterRow8 = sheet.createRow(rowNum++);
			filterRow9 = sheet.createRow(rowNum++);
			filterRow10 = sheet.createRow(rowNum++);
			filterRow11 = sheet.createRow(rowNum++);
			filterRow12 = sheet.createRow(rowNum++);*/
			
			if ((!StringUtil.isEmpty(distributorId) || !StringUtil.isEmpty(selectedFieldStaff)
					|| !StringUtil.isEmpty(farmerId) || !StringUtil.isEmpty(seasonCode)
					|| !StringUtil.isEmpty(location) || (!StringUtil.isEmpty(branchIdParma)) || (!StringUtil.isEmpty(product)) && flag)) {

				/*if (!StringUtil.isEmpty(distributorId)) {

					String distributorType = getLocaleProperty("distributor" + type);
					String distributor;

					if (FARMER.equals(type)) {

						distributor = distribution.getAgroTransaction().getFarmerName() + " - "
								+ distribution.getAgroTransaction().getFarmerId();
					} else {
						distributor = distribution.getAgroTransaction().getAgentName();
						
						 * distributor =
						 * distribution.getAgroTransaction().getAgentName()
						 * + " - " +
						 * distribution.getAgroTransaction().getAgentId();
						 
					}

					cell = filterRow4.createCell(1);
					cell.setCellValue(new HSSFRichTextString(distributorType));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow4.createCell(2);
					cell.setCellValue(new HSSFRichTextString(distributor));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

				}*/

				if (!StringUtil.isEmpty(selectedFieldStaff)) {
					filterRow5 = sheet.createRow(rowNum++);
					cell = filterRow5.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("distributiorIdagent")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow5.createCell(2);
					Agent agent = agentService.findAgentByAgentId(selectedFieldStaff);
					cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(agent)?agent.getPersonalInfo().getFirstName() +" "+(!StringUtil.isEmpty(agent.getPersonalInfo().getLastName())?agent.getPersonalInfo().getLastName():" "):""));					/*
					 * cell.setCellValue(new HSSFRichTextString(
					 * distribution.getAgentName() + " - " +
					 * distribution.getAgentId()));
					 */
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					/*if (StringUtil.isEmpty(distributorId)) {
						sheet.shiftRows(filterRow4.getRowNum() + 1, filterRow5.getRowNum() + 1, -1);
					}*/
				}

				if (!StringUtil.isEmpty(location)) {
					filterRow6 = sheet.createRow(rowNum++);
					cell = filterRow6.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("locationagent")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow6.createCell(2);
					Warehouse warehouse = locationService.findWarehouseByCode(location);
					cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(warehouse)?warehouse.getName():""));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

				}

				if (!StringUtil.isEmpty(fatherName)) {
					filterRow6 = sheet.createRow(rowNum++);
					cell = filterRow6.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("fatherName")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow6.createCell(2);
					cell.setCellValue(new HSSFRichTextString(fatherName));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					if (StringUtil.isEmpty(distributorId)) {
						sheet.shiftRows(filterRow4.getRowNum() + 1, filterRow5.getRowNum() + 1, -1);
					}
				}

				if (!StringUtil.isEmpty(farmerId)) {
					filterRow7 = sheet.createRow(rowNum++);
					cell = filterRow7.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmer")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow7.createCell(2);
					Farmer farmer = farmerService.findFarmerByFarmerName(farmerId);
					cell.setCellValue(new HSSFRichTextString(farmer.getFirstName()));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);
				}

				if (!StringUtil.isEmpty(seasonCode)) {
					filterRow8 = sheet.createRow(rowNum++);
					cell = filterRow8.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("season")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow8.createCell(2);
					HarvestSeason season = farmerService.findSeasonNameByCode(seasonCode);
					cell.setCellValue(new HSSFRichTextString(season.getName()));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);
				}

				if (!StringUtil.isEmpty(stateName)) {
					filterRow9 = sheet.createRow(rowNum++);
					cell = filterRow9.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("stateName")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow9.createCell(2);
					State s = locationService.findStateByCode(stateName);
					cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(s) ? s.getName() : "")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

				}

				if (!StringUtil.isEmpty(icsName)) {
					filterRow10 = sheet.createRow(rowNum++);
					cell = filterRow10.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("icsName")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow10.createCell(2);
					FarmCatalogue fc = farmerService.findfarmcatalogueByCode(icsName);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(fc) ? fc.getName() : ""));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

				}

				if (!StringUtil.isEmpty(fpo)) {
					filterRow11 = sheet.createRow(rowNum++);
					cell = filterRow11.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("cooperative")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow11.createCell(2);
					FarmCatalogue fc = farmerService.findfarmcatalogueByCode(fpo);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(fc) ? fc.getName() : ""));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

				}

				if (!StringUtil.isEmpty(branchIdParma)) {
					filterRow12 = sheet.createRow(rowNum++);
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
				
				if (!StringUtil.isEmpty(product)) {
					filterRow13 = sheet.createRow(rowNum++);
					cell = filterRow13.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("product")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow13.createCell(2);
					Product prod = productService.findProductById(Long.valueOf(product));
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(product) ? prod.getName() : ""));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

				}
				if (!StringUtil.isEmpty(samithi)) {
					filterRow14 = sheet.createRow(rowNum++);
					cell = filterRow14.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("samithi")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow14.createCell(2);
					Warehouse group = locationService.findSamithiById(Long.valueOf(samithi));
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(group) ? group.getName() : ""));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

				}

				flag = false;

			}
			rowNum++;
			HSSFRow headerGridRowHead = sheet.createRow(rowNum++);
			if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
				String[] headerFieldsArr = headerFields.split("###");
				for (int i = 0; i < headerFieldsArr.length; i++) {
					cell = headerGridRowHead.createCell(count);
					cell.setCellValue(new HSSFRichTextString(String.valueOf(headerFieldsArr[i])));
				//	style4.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
				//	style4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					style4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					cell.setCellStyle(style4);
					font2.setBoldweight((short) 12);
					font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					style4.setFont(font2);
					sheet.setColumnWidth(count, (15 * 550));
					count++;

				}
			}
		}

		

		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		// String mainGridColumnHeaders = getLocaleProperty("exportColumnHeader"
		// +
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
		} else if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {

			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("exportColumnHeaderDistributionBranchPratibha" + type);
			} else if (getBranchId().equalsIgnoreCase("organic")) {
				mainGridColumnHeaders = getLocaleProperty("OrganiDistributionExportHeader" + type);
			} else if (getBranchId().equalsIgnoreCase("bci")) {
				mainGridColumnHeaders = getLocaleProperty("BCIDistributionExportHeader" + type);
			}

		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("exportColumnHeaderDistributionBranch" + type);
			} else {
				mainGridColumnHeaders = getLocaleProperty("exportColumnHeaderDistribution" + type);
			}
		}

		int mainGridIterator = 0;

		setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));
		if (!StringUtil.isEmpty(enableBatchNo) && !enableBatchNo.equals("1")) {
			mainGridColumnHeaders = mainGridColumnHeaders.replace(",Batch No", "");
		}

		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {
			if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
			} else if (cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());
			}
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
				if (!cellHeader.equalsIgnoreCase(getText("app.branch"))) {
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
		if (ObjectUtil.isEmpty(this.filter)) {
			this.filter = new Distribution();
			this.filter.setAgroTransaction(new AgroTransaction());
		}

		type = request.getParameter("type");
		filter.setAgroTransaction(new AgroTransaction());

		if (!StringUtil.isEmpty(type)) {
			if (FARMER.equals(type)) {
				filter.setTxnType(FARMER);
			} else {
				filter.setTxnType(AGENT);
				if(getBranchId()!=null && !StringUtil.isEmpty(getBranchId())){
					filter.setBranchId(getBranchId());
				}
			}
		}

		if (!StringUtil.isEmpty(distributorId)) {
			if (FARMER.equals(type)) {
				filter.getAgroTransaction().setFarmerId(distributorId);
			} else {
				filter.getAgroTransaction().setAgentId(distributorId);
			}
		}
		if (!StringUtil.isEmpty(selectedFieldStaff)) {
			// filter.getAgroTransaction().setRefAgroTransaction(new
			// AgroTransaction());
			String agentIds[] = selectedFieldStaff.split("-");
			String agentId = agentIds[0].trim();
			filter.setAgentId(agentId);
		}

		/*
		 * if (!StringUtil.isEmpty(receiptNo)) {
		 * filter.setReceiptNumber(receiptNo); }
		 */
		if (!StringUtil.isEmpty(operationType)) {
			filter.getAgroTransaction().setOperType(Integer.valueOf(operationType));
		}

		if (!StringUtil.isEmpty(servicePointName)) {
			filter.setServicePointName(servicePointName);
		}

		if (!StringUtil.isEmpty(farmerId)) {
			filter.setFarmerName(farmerId);
		}

		if (!StringUtil.isEmpty(fatherName)) {
			filter.setFatherName(fatherName);
		}

		if (!StringUtil.isEmpty(product)) {
			filter.setProductId(Long.valueOf(product));
		}

		if (!StringUtil.isEmpty(location)) {
			if (FARMER.equals(type)) {
				filter.setServicePointId(location);
				if (!StringUtil.isEmpty(farmerId)) {
					filter.setFarmerId(farmerId);
				}

				if (!StringUtil.isEmpty(product)) {
					filter.setProductId(Long.valueOf(product));
				}

			} else {
				filter.setServicePointName(location);
			}
		}

		filter.setSeason(new Season());
		if (!StringUtil.isEmpty(seasonCode)) {
			/*
			 * filter.setSeasonCode(getCurrentSeasonsCode()); } else {
			 */
			filter.setSeasonCode(seasonCode);
		}

		if (!StringUtil.isEmpty(branchIdParma)) { // set filter of branch id and
													// check it.
			filter.setBranchId(branchIdParma);
		}
		if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
			if (!StringUtil.isEmpty(stateName)) {
				// State s = locationService.findStateByCode(stateName);
				filter.setStateName(stateName);
			}

			if (!StringUtil.isEmpty(fpo)) {
				filter.setFpo(fpo);
			}

			if (!StringUtil.isEmpty(icsName)) {

				filter.setIcsName(icsName);
			}
		}
		if (getCurrentTenantId().equalsIgnoreCase("susagri")) {
			if (!StringUtil.isEmpty(samithi)) {

				filter.setGroupId(Long.valueOf(samithi));
			}
		}
		super.filter = this.filter;

		Map data = isMailExport() ? readData() : readExportData();
		List<Warehouse> warehouseList=productDistributionService.listWarehouse();
		warehouseMap = warehouseList.stream().collect(
                Collectors.toMap(Warehouse::getCode, Warehouse::getName));
		if (FARMER.equalsIgnoreCase(type)) {
			List<Distribution> mainGridRows = (List<Distribution>) data.get(ROWS);
			if (ObjectUtil.isListEmpty(mainGridRows))
				return null;
			for (Distribution distribution : mainGridRows) {
				

				row = sheet.createRow(rowNum++);

				colNum = 0;

				serialNumber1++;
				cell = row.createCell(colNum++);
				style5.setAlignment(CellStyle.ALIGN_CENTER);
				cell.setCellStyle(style5);
				cell.setCellValue(serialNumber1);

				if (getCurrentTenantId().equalsIgnoreCase("sagi")) {

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							!ObjectUtil.isEmpty(distribution) ? (!ObjectUtil.isEmpty(distribution.getTxnTime())
									? DateUtil.convertDateToString(distribution.getTxnTime(), getGeneralDateFormat())
									: "") : ""));

					cell = row.createCell(colNum++);
					/*cell.setCellValue(new HSSFRichTextString(
							!ObjectUtil.isEmpty(distribution) ? (!StringUtil.isEmpty(distribution.getServicePointName())
									? distribution.getServicePointName() : "") : ""));*/
					cell.setCellValue(new HSSFRichTextString(
							!ObjectUtil.isEmpty(distribution) ? (!StringUtil.isEmpty(distribution.getServicePointId())
									? warehouseMap.get(distribution.getServicePointId()) : "") : ""));

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(distribution.getFarmerName()));

					cell = row.createCell(colNum++);
					if (!ObjectUtil.isEmpty(distribution.getVillage())) {
						cell.setCellValue(new HSSFRichTextString(distribution.getVillage().getVillageName()));
					} else {
						cell.setCellValue(new HSSFRichTextString(""));
					}

					Double qty = 0.0;

					if (!ObjectUtil.isListEmpty(distribution.getDistributionDetails())) {
						qty = distribution.getDistributionDetails().stream().mapToDouble(obj -> obj.getQuantity())
								.sum();
					}
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(qty.toString()));
				} else {

					if ((getIsMultiBranch().equalsIgnoreCase("1")
							&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
						if (StringUtil.isEmpty(branchIdValue)) {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(!StringUtil
									.isEmpty(getBranchesMap().get(getParentBranchMap().get(distribution.getBranchId())))
											? getBranchesMap().get(getParentBranchMap().get(distribution.getBranchId()))
											: getBranchesMap().get(distribution.getBranchId())));
						}
						cell = row.createCell(colNum++);
						cell.setCellValue(getBranchesMap().get(distribution.getBranchId()));
					}

					else {
						if (StringUtil.isEmpty(branchIdValue)) {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(distribution.getBranchId())
									? (branchesMap.get(distribution.getBranchId())) : "N/A"));
						}
					}

					/*
					 * if (StringUtil.isEmpty(branchIdValue)) { // Check if
					 * non-main // branch has logged // in as for farmer. cell =
					 * row.createCell(colNum++); cell.setCellValue(new
					 * HSSFRichTextString(!StringUtil.isEmpty(distribution.
					 * getBranchId()) ?
					 * (branchesMap.get(distribution.getBranchId())) : "")); }
					 */

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							!ObjectUtil.isEmpty(distribution) ? (!ObjectUtil.isEmpty(distribution.getTxnTime())
									? DateUtil.convertDateToString(distribution.getTxnTime(), getGeneralDateFormat())
									: "") : ""));

					if (!StringUtil.isEmpty(distribution.getServicePointName())) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("warehouseStock")));

						cell = row.createCell(colNum++);
						/*cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(distribution)
								? (!StringUtil.isEmpty(distribution.getServicePointName())
										? distribution.getServicePointName() : "")
								: ""));*/
						
						cell.setCellValue(new HSSFRichTextString(
								!ObjectUtil.isEmpty(distribution) ? (!StringUtil.isEmpty(distribution.getServicePointId())
										? warehouseMap.get(distribution.getServicePointId()) : "") : ""));
						/*
						 * cell.setCellValue(new
						 * HSSFRichTextString(!ObjectUtil.isEmpty(distribution)
						 * ?
						 * (!StringUtil.isEmpty(distribution.getServicePointName
						 * ()) ? distribution.getServicePointName() + "-" +
						 * distribution.getServicePointId() : "") : ""));
						 */
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString("NA"));
					} else {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("fieldstaff.stock")));
						cell = row.createCell(colNum++);
						if (!getCurrentTenantId().equals(ESESystem.PRATIBHA_TENANT_ID))
							cell.setCellValue(new HSSFRichTextString("NA"));
						else{
							/*cell.setCellValue(
									new HSSFRichTextString(!StringUtil.isEmpty(distribution.getWarehouseName())
											? distribution.getWarehouseName() : ""));*/
							cell.setCellValue(
									new HSSFRichTextString(!StringUtil.isEmpty(distribution.getWarehouseCode())
											? warehouseMap.get(distribution.getWarehouseCode()) : ""));
							
						}
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(distribution)
								? (!StringUtil.isEmpty(distribution.getAgentName()) ? distribution.getAgentName() : "")
								: ""));
						/*
						 * cell.setCellValue( new
						 * HSSFRichTextString(!ObjectUtil.isEmpty(distribution)
						 * ? (!StringUtil.isEmpty(distribution.getAgentName()) ?
						 * distribution.getAgentName() + "-" +
						 * distribution.getAgentId() : "") : ""));
						 */
					}

					Farmer farmer = farmerService.findFarmerByFarmerId(distribution.getFarmerId());
					if (!ObjectUtil.isEmpty(farmer)) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString("Registered"));

					} else {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString("UnRegistered"));

					}

					if (distribution.getSamithiName() != null) {

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(distribution.getFarmerName()));

						/*
						 * cell = row.createCell(colNum++); if
						 * (!StringUtil.isEmpty(distribution.getFatherName())) {
						 * cell.setCellValue(new
						 * HSSFRichTextString(distribution.getFatherName())); }
						 * else { cell.setCellValue(new
						 * HSSFRichTextString("NA")); }
						 */
						cell = row.createCell(colNum++);
						if (!ObjectUtil.isEmpty(distribution.getVillage())) {
							cell.setCellValue(new HSSFRichTextString(distribution.getVillage().getVillageName()));
						} else {
							cell.setCellValue(new HSSFRichTextString(""));
						}

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString("NA"));

					} else {

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(distribution.getAgroTransaction())
								&& !StringUtil.isEmpty(distribution.getAgroTransaction().getFarmerName())
										? distribution.getAgroTransaction().getFarmerName() : "NA"));

						/*
						 * cell = row.createCell(colNum++); if
						 * (!StringUtil.isEmpty(distribution.getFatherName())) {
						 * cell.setCellValue(new
						 * HSSFRichTextString(distribution.getFatherName())); }
						 * else { cell.setCellValue(new
						 * HSSFRichTextString("NA")); }
						 */
						cell = row.createCell(colNum++);

						if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
							cell.setCellValue(
									new HSSFRichTextString((!StringUtil.isEmpty(distribution.getMobileNumber()))
											? distribution.getMobileNumber() : "NA"));
						} else {
							if (!ObjectUtil.isEmpty(farmer)) {
								cell.setCellValue(new HSSFRichTextString((!StringUtil.isEmpty(farmer.getMobileNumber()))
										? farmer.getMobileNumber() : "NA"));
							} else {
								cell.setCellValue(
										new HSSFRichTextString((!StringUtil.isEmpty(distribution.getMobileNumber()))
												? distribution.getMobileNumber() : "NA"));
							}
						}

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(distribution.getVillage().getVillageName()));

						/*
						 * cell = row.createCell(colNum++);
						 * cell.setCellValue(new
						 * HSSFRichTextString(!StringUtil.isEmpty(farmer.
						 * getMobileNumber()) ? farmer.getMobileNumber() : ""));
						 */
					}
					if (getCurrentTenantId().equalsIgnoreCase(ESESystem.SUSAGRI)) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(farmer) && !ObjectUtil.isEmpty(farmer.getSamithi() )? farmer.getSamithi().getName() : "NA"));

					}
					if (!ObjectUtil.isEmpty(distribution.getFreeDistribution())) {
						if ((distribution.getFreeDistribution().equals("1"))) {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString("Free Product Distribution"));
						} else {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString("Normal Product Distribution"));
						}
					} else {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString("NA"));
					}
					Double qty = 0.0;
					if (!ObjectUtil.isEmpty(distribution.getDistributionDetails())) {
						for (DistributionDetail distributionDetail : distribution.getDistributionDetails()) {

							{
								qty = qty + distributionDetail.getQuantity();

							}
						}
						cell = row.createCell(colNum++);
						cell.setCellValue(Double.valueOf(qty));
					}

					if (distribution.getDistributionDetails().size() > 0
							&& distribution.getDistributionDetails().iterator().next().getSellingPrice() == 0.00) {
						cell = row.createCell(colNum++);
						cell.setCellValue(
								Double.valueOf(CurrencyUtil.getDecimalFormat(distribution.getTotalAmount(), "##.00")));

						cell = row.createCell(colNum++);
						cell.setCellValue(
								Double.valueOf(CurrencyUtil.getDecimalFormat(distribution.getTax(), "##.00")));

						cell = row.createCell(colNum++);
						cell.setCellValue(
								Double.valueOf(CurrencyUtil.getDecimalFormat(distribution.getFinalAmount(), "##.00")));

						cell = row.createCell(colNum++);
						cell.setCellValue(Double
								.valueOf(CurrencyUtil.getDecimalFormat(distribution.getPaymentAmount(), "##.00")));

						/*
						 * cell = row.createCell(colNum++);
						 * cell.setCellValue(new HSSFRichTextString("NA")); cell
						 * = row.createCell(colNum++); cell.setCellValue(new
						 * HSSFRichTextString("NA")); cell =
						 * row.createCell(colNum++); cell.setCellValue(new
						 * HSSFRichTextString("NA"));
						 */
						/*
						 * cell = row.createCell(colNum++);
						 * cell.setCellValue(new HSSFRichTextString("NA"));
						 */
					} else {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(distribution.getTotalAmount().toString()));

						/*
						 * cell = row.createCell(colNum++);
						 * cell.setCellValue(new
						 * HSSFRichTextString(distribution.getTax().toString()))
						 * ;
						 */

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(distribution.getFinalAmount().toString()));

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(
								CurrencyUtil.getDecimalFormat(distribution.getPaymentAmount(), "##.00")));

						/*
						 * if (!getCurrentTenantId().equalsIgnoreCase("chetna"))
						 * { cell = row.createCell(colNum++);
						 * cell.setCellValue(new HSSFRichTextString(
						 * CurrencyUtil.getDecimalFormat(distribution.
						 * getPaymentAmount(), "##.000")));
						 * 
						 * }else{ cell = row.createCell(colNum++);
						 * cell.setCellValue( new
						 * HSSFRichTextString("CS".equals(distribution.
						 * getPaymentMode()) ?
						 * CurrencyUtil.getDecimalFormat(distribution.
						 * getPaymentAmount(), "##.000") : "0.000")); }
						 */
						/*
						 * if ("CS".equals(distribution.getPaymentMode())) {
						 * cell = row.createCell(colNum++);
						 * cell.setCellValue(new
						 * HSSFRichTextString(String.valueOf(distribution.
						 * getPaymentAmount()))); cell =
						 * row.createCell(colNum++); cell.setCellValue(new
						 * HSSFRichTextString("NA")); } else if
						 * ("CR".equals(distribution.getPaymentMode())) { cell =
						 * row.createCell(colNum++); cell.setCellValue(new
						 * HSSFRichTextString("NA")); cell =
						 * row.createCell(colNum++); cell.setCellValue(new
						 * HSSFRichTextString(String.valueOf(distribution.
						 * getFinalAmount()))); }
						 */

					}
				}
				cell = row.createCell(colNum++);
				if (!StringUtil.isEmpty(distribution.getSeasonCode())) {
					HarvestSeason season = farmerService.findSeasonNameByCode(distribution.getSeasonCode());
					/*
					 * cell.setCellValue(new
					 * HSSFRichTextString(!ObjectUtil.isEmpty(season) ?
					 * season.getCode() + "-" + season.getName() : ""));
					 */ cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(season) ? season.getName() : ""));

				}

				HSSFRow subGridRowHead = sheet.createRow(rowNum++);
				String sunGridcolumnHeaders = getLocaleProperty("exportSubColumnHeaderDistribution" + type);

				int subGridIterator = 1;

				setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));
				if (!StringUtil.isEmpty(enableBatchNo) && !enableBatchNo.equals("1")) {
					sunGridcolumnHeaders = sunGridcolumnHeaders.replace(",Batch No", "");
				}

				for (String cellHeader : sunGridcolumnHeaders.split("\\,")) {
					if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
						cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
					} else {
						cellHeader = cellHeader.trim();
					}
					cell = subGridRowHead.createCell(subGridIterator);
					cell.setCellValue(new HSSFRichTextString(cellHeader));
					style3.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
					style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					cell.setCellStyle(style3);
					style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					cell.setCellStyle(style3);
					font3.setBoldweight((short) 10);
					font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					style3.setFont(font3);
					subGridIterator++;
				}

				for (DistributionDetail distributionDetail : distribution.getDistributionDetails()) {
					if (getCurrentTenantId().equalsIgnoreCase("sagi")) {
						row = sheet.createRow(rowNum++);
						colNum = 1;

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(distributionDetail.getProduct() == null ? ""
								: distributionDetail.getProduct().getName()));
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(
								distributionDetail.getSagiCode() == null ? "" : distributionDetail.getSagiCode()));
						if (getEnableBatchNo().equals("1")) {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(
									distributionDetail.getBatchNo() == null ? "" : distributionDetail.getBatchNo()));
						}

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(String.valueOf(distributionDetail.getQuantity())));
					} else {
						row = sheet.createRow(rowNum++);
						colNum = 1;

						cell = row.createCell(colNum++);
						cell.setCellValue(
								new HSSFRichTextString(distributionDetail.getProduct().getSubcategory() == null ? ""
										: distributionDetail.getProduct().getSubcategory().getName()));

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(distributionDetail.getProduct() == null ? ""
								: distributionDetail.getProduct().getName()));

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(distributionDetail.getProduct() == null ? ""
								: distributionDetail.getProduct().getUnit()));

						if (getEnableBatchNo().equals("1")) {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(
									distributionDetail.getBatchNo() == null ? "" : distributionDetail.getBatchNo()));
						}

						cell = row.createCell(colNum++);
						cell.setCellValue(Double.valueOf(distributionDetail.getExistingQuantity()));

						cell = row.createCell(colNum++);
						cell.setCellValue(Double.valueOf(distributionDetail.getCostPrice()));

						if (distributionDetail.getSellingPrice() == 0.00) {
							/*
							 * cell = row.createCell(colNum++);
							 * cell.setCellValue(new HSSFRichTextString("NA"));
							 * 
							 * cell = row.createCell(colNum++);
							 * cell.setCellValue(new HSSFRichTextString("NA"));
							 */
						} else {

							if (!getCurrentTenantId().equalsIgnoreCase("agro")) {
								cell = row.createCell(colNum++);
								cell.setCellValue(Double.valueOf(distributionDetail.getSellingPrice()));

								cell = row.createCell(colNum++);
								cell.setCellValue(new HSSFRichTextString(
										String.valueOf(Double.valueOf(distributionDetail.getSellingPrice())
												* Double.valueOf(distributionDetail.getQuantity()))));
							}
						}

						cell = row.createCell(colNum++);
						cell.setCellValue(
								Double.valueOf(
										CurrencyUtil.getDecimalFormat(
												Double.valueOf(distributionDetail.getCostPrice())
														* Double.valueOf(distributionDetail.getQuantity()),
												"##.00")));

						cell = row.createCell(colNum++);
						cell.setCellValue(Double.valueOf(distributionDetail.getQuantity()));

						cell = row.createCell(colNum++);
						if (!StringUtil.isEmpty(distributionDetail.getExistingQuantity())) {
							Double diffQty = 0.00;
							diffQty = Double.valueOf(distributionDetail.getExistingQuantity())
									- (distributionDetail.getQuantity());
							cell.setCellValue(Double.valueOf(CurrencyUtil.getDecimalFormat((diffQty), "##.00")));
						}
					}
				}
				row = sheet.createRow(rowNum++);
			}

		} else {

			List<DistributionDetail> mainGridRows = (List<DistributionDetail>) data.get(ROWS);
			if (ObjectUtil.isListEmpty(mainGridRows))
				return null;
			/*
			 * for (Distribution distribution : mainGridRows) {
			 * if(!StringUtil.isEmpty(getType())&&!getType().equalsIgnoreCase(
			 * FARMER)){ if(!StringUtil.isEmpty(product)){
			 * Set<DistributionDetail> detailsSet =
			 * distribution.getDistributionDetails().stream().filter(disDetail->
			 * String.valueOf(disDetail.getProduct().getId()).equals(product)).
			 * collect(Collectors.toSet());
			 * distribution.setDistributionDetails(detailsSet); } }
			 */
			for (DistributionDetail distributionDetail : mainGridRows) {
				if ((!StringUtil.isEmpty(distributorId) || !StringUtil.isEmpty(selectedFieldStaff)
						|| !StringUtil.isEmpty(seasonCode) || !StringUtil.isEmpty(product)
						|| !StringUtil.isEmpty(location)) && flag) {

					if (!StringUtil.isEmpty(distributorId)) {

						String distributorType = getLocaleProperty("distributor" + type);
						String distributor;

						if (FARMER.equals(type)) {

							distributor = distributionDetail.getDistribution().getAgroTransaction().getFarmerName()
									+ " - " + distributionDetail.getDistribution().getAgroTransaction().getFarmerId();
						} else {
							distributor = distributionDetail.getDistribution().getAgroTransaction().getAgentName();
							/*
							 * distributor = distribution.getAgroTransaction().
							 * getAgentName() + " - " +
							 * distribution.getAgroTransaction().getAgentId( );
							 */
						}

						cell = filterRow4.createCell(1);
						cell.setCellValue(new HSSFRichTextString(distributorType));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						cell = filterRow4.createCell(2);
						cell.setCellValue(new HSSFRichTextString(distributor));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

					}

					if (!StringUtil.isEmpty(selectedFieldStaff)) {

						cell = filterRow5.createCell(1);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("distributiorId" + type)));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						cell = filterRow5.createCell(2);
						cell.setCellValue(new HSSFRichTextString(distributionDetail.getDistribution().getAgentName()));
						/*
						 * cell.setCellValue(new HSSFRichTextString(
						 * distribution.getAgentName() + " - " +
						 * distribution.getAgentId()));
						 */
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						if (StringUtil.isEmpty(distributorId)) {
							sheet.shiftRows(filterRow4.getRowNum() + 1, filterRow5.getRowNum() + 1, -1);
						}
					}

					/*if (!StringUtil.isEmpty(seasonCode)) {

						cell = filterRow6.createCell(1);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("seasonCode")));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						
						 * cell = filterRow6.createCell(2);
						 * cell.setCellValue(new
						 * HSSFRichTextString(distribution.getServicePointName()
						 * )); filterFont.setBoldweight((short) 12);
						 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						 * filterStyle.setFont(filterFont);
						 * cell.setCellStyle(filterStyle);
						 
						
						 * if (StringUtil.isEmpty(selectedFieldStaff) &&
						 * StringUtil.isEmpty(distributorId)) {
						 * sheet.shiftRows(filterRow5.getRowNum() + 1,
						 * filterRow6.getRowNum() + 1, -2); } else if
						 * (StringUtil.isEmpty(selectedFieldStaff) ||
						 * StringUtil.isEmpty(distributorId)) {
						 * sheet.shiftRows(filterRow5.getRowNum() + 1,
						 * filterRow6.getRowNum() + 1, -1); }
						 
					}*/

					if (!StringUtil.isEmpty(location)) {

						cell = filterRow6.createCell(1);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("locationagent")));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						cell = filterRow6.createCell(2);
						/*cell.setCellValue(
								new HSSFRichTextString(distributionDetail.getDistribution().getServicePointName()));*/
						cell.setCellValue(
								new HSSFRichTextString(warehouseMap.get(distributionDetail.getDistribution().getServicePointId())));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						if (StringUtil.isEmpty(selectedFieldStaff) && StringUtil.isEmpty(distributorId)) {
							sheet.shiftRows(filterRow5.getRowNum() + 1, filterRow6.getRowNum() + 1, -2);
						} else if (StringUtil.isEmpty(selectedFieldStaff) || StringUtil.isEmpty(distributorId)) {
							sheet.shiftRows(filterRow5.getRowNum() + 1, filterRow6.getRowNum() + 1, -1);
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
						Product prod = productService.findProductById(Long.valueOf(product));
						cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(product) ? prod.getName() : ""));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

					}

					if (!StringUtil.isEmpty(seasonCode)) {

						cell = filterRow8.createCell(1);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("season")));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						cell = filterRow8.createCell(2);
						HarvestSeason season = farmerService
								.findSeasonNameByCode(distributionDetail.getDistribution().getSeasonCode());
						cell.setCellValue(new HSSFRichTextString(season.getName()));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);
					}

					flag = false;

				}

				row = sheet.createRow(rowNum++);
				colNum = 0;

				serialNumber++;
					cell = row.createCell(colNum++);
					style5.setAlignment(CellStyle.ALIGN_CENTER);
					cell.setCellStyle(style5);
					cell.setCellValue(serialNumber);

				if ((getIsMultiBranch().equalsIgnoreCase("1")
						&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
					if (StringUtil.isEmpty(branchIdValue)) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(getBranchesMap()
								.get(getParentBranchMap().get(distributionDetail.getDistribution().getBranchId())))
										? getBranchesMap().get(getParentBranchMap()
												.get(distributionDetail.getDistribution().getBranchId()))
										: getBranchesMap().get(distributionDetail.getDistribution().getBranchId())));
					}
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!StringUtil
							.isEmpty(getBranchesMap().get(distributionDetail.getDistribution().getBranchId()))
									? getBranchesMap().get(distributionDetail.getDistribution().getBranchId()) : ""));
					// fields.add(getBranchesMap().get(farmCrop.getBranchId()));

				} else {

					if (StringUtil.isEmpty(branchIdValue)) { // Check if
																// non-main
																// branch
																// has
																// logged
																// in.
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(
								!StringUtil.isEmpty(distributionDetail.getDistribution().getBranchId())
										? (branchesMap.get(distributionDetail.getDistribution().getBranchId())) : ""));
					}
				}
				cell = row.createCell(colNum++);
				DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(distributionDetail.getDistribution())
						? (!StringUtil.isEmpty(genDate.format(distributionDetail.getDistribution().getTxnTime()))
								? genDate.format(distributionDetail.getDistribution().getTxnTime()) : "")
						: ""));

				if (FARMER.equalsIgnoreCase(type)) {

					if (!StringUtil.isEmpty(distributionDetail.getDistribution().getServicePointId())) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString("WarehouseStock"));

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(
								!ObjectUtil.isEmpty(distributionDetail.getDistribution()) ? (!StringUtil
										.isEmpty(distributionDetail.getDistribution().getServicePointId())
												? warehouseMap.get(distributionDetail.getDistribution().getServicePointId()) : "")
										: ""));
						/*cell.setCellValue(new HSSFRichTextString(
								!ObjectUtil.isEmpty(distributionDetail.getDistribution()) ? (!StringUtil
										.isEmpty(distributionDetail.getDistribution().getServicePointName())
												? distributionDetail.getDistribution().getServicePointName() : "")
										: ""));*/
						/*
						 * cell.setCellValue(new
						 * HSSFRichTextString(!ObjectUtil.isEmpty( distribution)
						 * ? (!StringUtil.isEmpty(distribution.
						 * getServicePointName()) ?
						 * distribution.getServicePointName() + "-" +
						 * distribution.getServicePointId() : "") : ""));
						 */
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString("NA"));
					} else {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString("MobileUserStock"));
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString("NA"));
						cell = row.createCell(colNum++);
						cell.setCellValue(
								new HSSFRichTextString(!ObjectUtil.isEmpty(distributionDetail.getDistribution())
										? (!StringUtil.isEmpty(distributionDetail.getDistribution().getAgentName())
												? distributionDetail.getDistribution().getAgentName() : "")
										: ""));
						/*
						 * cell.setCellValue(new
						 * HSSFRichTextString(!ObjectUtil.isEmpty( distribution)
						 * ? (!StringUtil.isEmpty(distribution.getAgentName()) ?
						 * distribution.getAgentName() + "-" +
						 * distribution.getAgentId() : "") : ""));
						 */
					}

					if (distributionDetail.getDistribution().getSamithiName() != null) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString("Registered"));

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(distributionDetail.getDistribution().getFarmerName()));

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(
								distributionDetail.getDistribution().getVillage().getVillageName()));

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString("NA"));

					} else {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString("UnRegistered"));

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(distributionDetail.getDistribution().getFarmerName()));

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(
								distributionDetail.getDistribution().getVillage().getVillageName()));
						Farmer farmer = farmerService
								.findFarmerByFarmerId(distributionDetail.getDistribution().getFarmerId());
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(farmer.getMobileNumber()));
					}

					if ((distributionDetail.getDistribution().getDistributionDetails().iterator().next()
							.getSellingPrice() == 0.00)
							&& (distributionDetail.getDistribution().getDistributionDetails().size() > 0)) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString("Yes"));
					} else {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString("No"));
					}

					if (!ObjectUtil.isEmpty(distributionDetail.getDistribution().getDistributionDetails())) {
						Double qty = 0.0;
						{
							qty = qty + distributionDetail.getQuantity();
							cell = row.createCell(colNum++);
							cell.setCellValue(Double.valueOf(qty));
						}
					}
					if (distributionDetail.getDistribution().getDistributionDetails().iterator().next()
							.getSellingPrice() == 0.00) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString("NA"));
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString("NA"));
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString("NA"));
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString("NA"));
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString("NA"));
					} else {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(
								distributionDetail.getDistribution().getTotalAmount().toString()));

						/*
						 * cell = row.createCell(colNum++);
						 * cell.setCellValue(new
						 * HSSFRichTextString(distribution.getTax().toString()))
						 * ;
						 */

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(
								distributionDetail.getDistribution().getFinalAmount().toString()));

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(CurrencyUtil
								.getDecimalFormat(distributionDetail.getDistribution().getPaymentAmount(), "##.00")));

						/*
						 * if ("CS".equals(distribution.getPaymentMode())) {
						 * cell = row.createCell(colNum++); cell.setCellValue(
						 * new HSSFRichTextString(String.valueOf(distribution.
						 * getPaymentAmount()))); cell =
						 * row.createCell(colNum++); cell.setCellValue(new
						 * HSSFRichTextString("NA")); } else if
						 * ("CR".equals(distribution.getPaymentMode())) { cell =
						 * row.createCell(colNum++); cell.setCellValue(new
						 * HSSFRichTextString("NA")); cell =
						 * row.createCell(colNum++); cell.setCellValue( new
						 * HSSFRichTextString(String.valueOf(distribution.
						 * getFinalAmount()))); }
						 */
					}
				} else {

					cell = row.createCell(colNum++);
					/*
					 * cell.setCellValue(new
					 * HSSFRichTextString(!ObjectUtil.isEmpty(distribution.
					 * getAgroTransaction()) &&
					 * !ObjectUtil.isEmpty(distribution) ?
					 * (!StringUtil.isEmpty(distribution.getAgentId()) ?
					 * distribution.getAgentName() + "-" +
					 * distribution.getAgentId() : "") : ""));
					 */

					/*cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(distributionDetail.getDistribution())
							? (!StringUtil.isEmpty(distributionDetail.getDistribution().getServicePointName())
									? distributionDetail.getDistribution().getServicePointName() : "")
							: ""));*/

					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(distributionDetail.getDistribution())
							? (!StringUtil.isEmpty(distributionDetail.getDistribution().getServicePointId())
									? warehouseMap.get(distributionDetail.getDistribution().getServicePointId()) : "")
							: ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							!ObjectUtil.isEmpty(distributionDetail.getDistribution().getAgroTransaction())
									&& !ObjectUtil.isEmpty(distributionDetail.getDistribution())
											? (!StringUtil.isEmpty(distributionDetail.getDistribution().getAgentId())
													? distributionDetail.getDistribution().getAgentName() : "")
											: ""));

					/*
					 * cell.setCellValue(new
					 * HSSFRichTextString(!ObjectUtil.isEmpty(distribution) ?
					 * (!StringUtil.isEmpty(distribution.getServicePointName ())
					 * ? distribution.getServicePointName() + "-" +
					 * distribution.getServicePointId() : "") : ""));
					 */
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(distributionDetail.getProduct() != null
							&& distributionDetail.getProduct().getSubcategory() != null
									? distributionDetail.getProduct().getSubcategory().getName() : ""));

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(distributionDetail)
							? (!ObjectUtil.isEmpty(distributionDetail.getProduct())
									? distributionDetail.getProduct().getName() : "")
							: ""));

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							distributionDetail.getProduct() == null ? "" : distributionDetail.getProduct().getUnit()));

					if (getEnableBatchNo().equals("1")) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(
								distributionDetail.getBatchNo() == null ? "" : distributionDetail.getBatchNo()));
					}

					/*cell = row.createCell(colNum++);
					cell.setCellValue(Double.valueOf(!ObjectUtil.isEmpty(distributionDetail)
							? (!StringUtil.isEmpty(distributionDetail.getExistingQuantity())
									? distributionDetail.getExistingQuantity() : "")
							: ""));*/

					cell = row.createCell(colNum++);
					cell.setCellValue(Double.valueOf(df.format(distributionDetail.getQuantity())));

					/*cell = row.createCell(colNum++);
					cell.setCellValue(Double.valueOf(!ObjectUtil.isEmpty(distributionDetail)
							? (!StringUtil.isEmpty(distributionDetail.getCurrentQuantity())
									? distributionDetail.getCurrentQuantity() : "")
							: ""));
*/
					cell = row.createCell(colNum++);
					if (!StringUtil.isEmpty(distributionDetail.getDistribution().getSeasonCode())) {
						HarvestSeason season = farmerService
								.findSeasonNameByCode(distributionDetail.getDistribution().getSeasonCode());
						cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(season) ? season.getName() : ""));

					}
				}

				if (FARMER.equals(type)) {
					HSSFRow subGridRowHead = sheet.createRow(rowNum++);
					String sunGridcolumnHeaders = getLocaleProperty("exportSubColumnHeader" + type);
					int subGridIterator = 1;

					for (String cellHeader : sunGridcolumnHeaders.split("\\,")) {

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

					row = sheet.createRow(rowNum++);
					colNum = 1;

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(distributionDetail.getProduct().getSubcategory() == null
							? "" : distributionDetail.getProduct().getSubcategory().getName()));

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							distributionDetail.getProduct() == null ? "" : distributionDetail.getProduct().getName()));

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							distributionDetail.getProduct() == null ? "" : distributionDetail.getProduct().getUnit()));

					ESESystem preferences1 = preferncesService.findPrefernceById("1");
					setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));

					if (getEnableBatchNo().equals("1")) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(
								distributionDetail.getBatchNo() == null ? "" : distributionDetail.getBatchNo()));
					}

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(String.valueOf(distributionDetail.getExistingQuantity())));

					/*
					 * cell = row.createCell(colNum++); cell.setCellValue(new
					 * HSSFRichTextString(String.valueOf(distributionDetail.
					 * getCostPrice())));
					 */

					if (distributionDetail.getSellingPrice() == 0.00) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString("NA"));

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString("NA"));
					} else {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(String.valueOf(distributionDetail.getSellingPrice())));

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(
								String.valueOf(Double.valueOf(distributionDetail.getSellingPrice())
										* Double.valueOf(distributionDetail.getQuantity()))));
					}
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(String.valueOf(distributionDetail.getQuantity())));
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
		String fileName = getLocaleProperty("distributionReportList" + type) + fileNameDateFormat.format(new Date())
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

	/*
	 * public Map<String, String> getProductList() {
	 * 
	 * Map<String, String> returnMap = new LinkedHashMap<String, String>();
	 * 
	 * // List<Object[]> productList = //
	 * productService.listWarehouseStockProducts(); List<Object[]> productList =
	 * productService.listFarmProducts(); if
	 * (!ObjectUtil.isListEmpty(productList)) { for (Object[] productObj :
	 * productList) { // returnMap.put(productObj[0].toString(), //
	 * productObj[1].toString() + " - " + productObj[0].toString());
	 * returnMap.put(productObj[1].toString(), productObj[0].toString()); } }
	 * return returnMap; }
	 */
	/**
	 * Gets the image path.
	 * 
	 * @param imagePath
	 * @return the image path
	 */
	private String getImagePath(String imagePath) {

		StringBuilder pdfIconBuffer = new StringBuilder();
		pdfIconBuffer.append(request.getContextPath());
		pdfIconBuffer.append(imagePath);
		return pdfIconBuffer.toString();
	}

	/**
	 * Gets the selected field staff.
	 * 
	 * @return the selected field staff
	 */
	public String getSelectedFieldStaff() {

		return selectedFieldStaff;
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
	 * Gets the distribution map.
	 * 
	 * @return the distribution map
	 */
	public Map<String, Object> getDistributionMap() {

		return distributionMap;
	}

	/**
	 * Sets the distribution map.
	 * 
	 * @param distributionMap
	 *            the distribution map
	 */
	public void setDistributionMap(Map<String, Object> distributionMap) {

		this.distributionMap = distributionMap;
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

	public boolean isRegisteredFarmer() {

		return registeredFarmer;
	}

	public void setRegisteredFarmer(boolean registeredFarmer) {

		this.registeredFarmer = registeredFarmer;
	}

	public String getFarmerId() {

		return farmerId;
	}

	public void setFarmerId(String farmerId) {

		this.farmerId = farmerId;
	}

	public String getProduct() {

		return product;
	}

	public void setProduct(String product) {

		this.product = product;
	}

	public IProductService getProductService() {

		return productService;
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

	@Override
	public String populatePDF() throws Exception {
		InputStream is = getPDFExportDataStream();
		setPdfFileName(
				getText("distributionReportList" + type).replace(" ", "_") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(getPdfFileName(), is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(
				getText("distributionReportList" + type).replace(" ", "_"), fileMap, ".pdf"));
		return "pdf";
	}

	public InputStream getPDFExportDataStream()
			throws IOException, DocumentException, NoSuchFieldException, SecurityException, FontFormatException {

		Font cellFont = null; // font for cells.
		Font titleFont = null; // font for title text.
		Paragraph title = null; // to add title for report
		String columnHeaders = null; // to hold column headers from property
										// file.
		
		String serverFilePath = request.getSession().getServletContext().getRealPath("/");
		serverFilePath = serverFilePath.replace('\\', '/');		
		String arialFontFileLocation = serverFilePath+"/fonts/ARIAL.TTF";
		arialFontFileLocation = arialFontFileLocation.replace("//","/");
		BaseFont bf =BaseFont.createFont(arialFontFileLocation,BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		
		int mainGridColWidth = 0;
		int subGridColWidth = 0;

		List<Object[]> entityFieldsList = new ArrayList<Object[]>(); // to hold
																		// properties
																		// of
																		// entity
																		// object
																		// passed
																		// as
																		// list.
		SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Document document = new Document();
		setMailExport(true);
		int cols;

		String makeDir = FileUtil.storeXls(request.getSession().getId());
		String fileName = getText("distributionReportList" + type).replace(" ", "_")
				+ fileNameDateFormat.format(new Date()) + ".pdf";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		PdfWriter.getInstance(document, fileOut);

		document.open();

		PdfPTable table = null;
		PdfPCell cell = null;
		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap();
		
		

		com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance(exportLogo());
		logo.scaleAbsolute(100, 50);
		; // resizing logo image size.

		document.add(logo); // Adding logo in PDF file.

		// below of code for title text
		titleFont = new Font(FontFamily.HELVETICA, 14, Font.BOLD, GrayColor.GRAYBLACK);
		title = new Paragraph(new Phrase(getLocaleProperty("exportDistributionTitle" + type), titleFont));
		title.setAlignment(Element.ALIGN_CENTER);
		document.add(title);
		document.add(
				new Paragraph(new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK)))); // Add
																														// a
																														// blank
																														// line
																														// after
																														// title.
	
		// cell for table.

		/* Beginning */
		// setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(IExporter.EXPORT_MANUAL)
		// ? true : false);
		setsDate(IExporter.EXPORT_DAILY.equalsIgnoreCase(IExporter.EXPORT_MANUAL)
				? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));
		boolean flag = true;
		DecimalFormat df = new DecimalFormat("0.000");
		DateFormat filterDateFormat = new SimpleDateFormat(getGeneralDateFormat());

		if (isMailExport()) {
			document.add(new Paragraph(new Phrase(getLocaleProperty("filter"),
					new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
			document.add(new Paragraph(
					new Phrase(getLocaleProperty("StartingDate") + " : " + filterDateFormat.format(getsDate()),
							new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
			document.add(new Paragraph(
					new Phrase(getLocaleProperty("EndingDate") + " : " + filterDateFormat.format(geteDate()),
							new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
			/*
			 * document.add(new Paragraph( new Phrase(" ", new
			 * Font(FontFamily.HELVETICA, 10, Font.NORMAL,
			 * GrayColor.GRAYBLACK))));
			 */ // Add
				// a
				// blank
				// line.
		}

		ESESystem preferences = preferncesService.findPrefernceById("1");
		setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));

		String mainGridColumnHeaders = "";

		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("ExportColumnHeadingBranch" + type);
			} else if (!StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("ExportColumnHeadingBranch" + type);
			}
		} else if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {

			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("exportColumnHeaderDistributionBranchPratibha" + type);
			} else if (getBranchId().equalsIgnoreCase("organic")) {
				mainGridColumnHeaders = getLocaleProperty("OrganiDistributionExportHeader" + type);
			} else if (getBranchId().equalsIgnoreCase("bci")) {
				mainGridColumnHeaders = getLocaleProperty("BCIDistributionExportHeader" + type);
			}

		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("exportColumnHeaderDistributionBranch" + type);
			} else {
				mainGridColumnHeaders = getLocaleProperty("exportColumnHeaderDistribution" + type);
			}
		}

		int mainGridIterator = 0;

		setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));
		if (!StringUtil.isEmpty(enableBatchNo) && !enableBatchNo.equals("1")) {
			mainGridColumnHeaders = mainGridColumnHeaders.replace(",Batch No", "");
		}

		table = new PdfPTable(mainGridColumnHeaders.split("\\,").length); // Code
																			// for
																			// setting
																			// table
																			// column
																			// Numbers.
		cellFont = new Font(bf, 10, Font.BOLD, GrayColor.GRAYBLACK); // setting
																						// font
																						// for
																						// header
																						// cells
																						// of
																						// table.

		mainGridColWidth = mainGridColumnHeaders.split("\\,").length;

		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {
			if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
			} else {
				cellHeader = cellHeader.trim();
			}
			if (StringUtil.isEmpty(branchIdValue)) { // Add branch header to
														// export sheet if main
														// branch logged in.
				cell = new PdfPCell(new Phrase(cellHeader, cellFont));
				cell.setBackgroundColor(new BaseColor(144,238,144));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setNoWrap(false); // To set wrapping of text in cell.
				// cell.setColspan(3); // To add column span.
				table.addCell(cell);
			} else {
				if (!cellHeader.equalsIgnoreCase(getText("app.branch"))) {
					cell = new PdfPCell(new Phrase(cellHeader, cellFont));
					cell.setBackgroundColor(new BaseColor(144,238,144));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setNoWrap(false); // To set wrapping of text in cell.
					// cell.setColspan(3); // To add column span.
					table.addCell(cell);
				}
			}

		}

		cellFont = new Font(bf, 8, Font.NORMAL, GrayColor.GRAYBLACK); // setting
																						// font
																						// for
																						// cells.

		if (ObjectUtil.isEmpty(this.filter)) {
			this.filter = new Distribution();
			this.filter.setAgroTransaction(new AgroTransaction());
		}

		type = request.getParameter("type");
		filter.setAgroTransaction(new AgroTransaction());

		if (!StringUtil.isEmpty(type)) {
			if (FARMER.equals(type)) {
				filter.setTxnType(FARMER);
			} else {
				filter.setTxnType(AGENT);
				if(getBranchId()!=null && !StringUtil.isEmpty(getBranchId())){
					filter.setBranchId(getBranchId());
				}
			}
		}

		if (!StringUtil.isEmpty(distributorId)) {
			if (FARMER.equals(type)) {
				filter.getAgroTransaction().setFarmerId(distributorId);
			} else {
				filter.getAgroTransaction().setAgentId(distributorId);
			}
		}
		if (!StringUtil.isEmpty(selectedFieldStaff)) {
			// filter.getAgroTransaction().setRefAgroTransaction(new
			// AgroTransaction());
			String agentIds[] = selectedFieldStaff.split("-");
			String agentId = agentIds[0].trim();
			filter.setAgentId(agentId);
		}

		/*
		 * if (!StringUtil.isEmpty(receiptNo)) {
		 * filter.setReceiptNumber(receiptNo); }
		 */
		if (!StringUtil.isEmpty(operationType)) {
			filter.getAgroTransaction().setOperType(Integer.valueOf(operationType));
		}

		if (!StringUtil.isEmpty(servicePointName)) {
			filter.setServicePointName(servicePointName);
		}

		if (!StringUtil.isEmpty(farmerId)) {
			filter.setFarmerName(farmerId);
		}

		if (!StringUtil.isEmpty(product)) {
			filter.setProductId(Long.valueOf(product));
		}
		if (!StringUtil.isEmpty(location)) {
			if (FARMER.equals(type)) {
				filter.setServicePointId(location);
				if (!StringUtil.isEmpty(farmerId)) {
					filter.setFarmerId(farmerId);
				}

				if (!StringUtil.isEmpty(product)) {
					filter.setProductId(Long.valueOf(product));
				}

			} else {
				filter.setServicePointName(location);
			}
		}
		filter.setSeason(new Season());
		/*
		 * if (!StringUtil.isEmpty(season)) {
		 * filter.getSeason().setCode(season); }
		 */
		if (!StringUtil.isEmpty(seasonCode)) {
			/*
			 * filter.setSeasonCode(getCurrentSeasonsCode()); } else {
			 */
			filter.setSeasonCode(seasonCode);
		}

		if (!StringUtil.isEmpty(branchIdParma)) { // set filter of branch id and
													// check it.
			filter.setBranchId(branchIdParma);
		}
		if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
			if (!StringUtil.isEmpty(stateName)) {
				// State s = locationService.findStateByCode(stateName);
				filter.setStateName(stateName);
			}

			if (!StringUtil.isEmpty(fpo)) {
				filter.setFpo(fpo);
			}

			if (!StringUtil.isEmpty(icsName)) {

				filter.setIcsName(icsName);
			}

		}
		if (getCurrentTenantId().equalsIgnoreCase("susagri")) {
			if (!StringUtil.isEmpty(samithi)) {

				filter.setGroupId(Long.valueOf(samithi));
			}
		}
		super.filter = this.filter;

		Map data = isMailExport() ? readData() : readExportData();
		List<Warehouse> warehouseList=productDistributionService.listWarehouse();
		warehouseMap = warehouseList.stream().collect(
                Collectors.toMap(Warehouse::getCode, Warehouse::getName));
		List<Distribution> mainGridRows = (List<Distribution>) data.get(ROWS);
		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;

		Long serialNo = 0L;

		if (FARMER.equalsIgnoreCase(type)) {
			for (Distribution distribution : mainGridRows) {

				serialNo++;

				cell = new PdfPCell(new Paragraph(new Phrase(String.valueOf(serialNo), cellFont)));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				
				if (flag) {

					if (!StringUtil.isEmpty(distributorId)) {

						String distributorType = getLocaleProperty("distributor" + type);
						String distributor;

						if (FARMER.equals(type)) {

							distributor = distribution.getAgroTransaction().getFarmerName() + " - "
									+ distribution.getAgroTransaction().getFarmerId();
						} else {
							distributor = distribution.getAgroTransaction().getAgentName();

						}

						document.add(new Paragraph(new Phrase(distributorType + " : " + distributor,
								new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));

					}
					if (!StringUtil.isEmpty(selectedFieldStaff)) {

						document.add(new Paragraph(new Phrase(
								getLocaleProperty("distributiorIdagent") + " : " + distribution.getAgentName(),
								new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));
					}

					if (!StringUtil.isEmpty(location)) {

						/*document.add(new Paragraph(
								new Phrase(getLocaleProperty("locationagent") + " : " +distribution.getServicePointName(),
										new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));*/
						document.add(new Paragraph(
								new Phrase(getLocaleProperty("locationagent") + " : " +warehouseMap.get(distribution.getServicePointId()),
										new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));
					}

					if (!StringUtil.isEmpty(farmerId)) {
						Farmer farmer = farmerService.findFarmerByFarmerName(farmerId);
						document.add(
								new Paragraph(new Phrase(getLocaleProperty("farmer") + " : " + farmer.getFirstName(),
										new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));
					}

					if (!StringUtil.isEmpty(seasonCode)) {
						HarvestSeason season = farmerService.findSeasonNameByCode(distribution.getSeasonCode());
						document.add(new Paragraph(new Phrase(getLocaleProperty("season") + " : " + season.getName(),
								new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));
					}

					if (!StringUtil.isEmpty(stateName)) {
						State state = locationService.findStateByCode(stateName);
						document.add(new Paragraph(new Phrase(getLocaleProperty("stateName") + " : " + state.getName(),
								new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));
					}

					if (!StringUtil.isEmpty(icsName)) {
						FarmCatalogue catalogue = catalogueService.findCatalogueByCode(icsName);
						if (!StringUtil.isEmpty(catalogue)) {
							document.add(
									new Paragraph(new Phrase(getLocaleProperty("icsName") + " : " + catalogue.getName(),
											new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));
						}
					}
					if (!StringUtil.isEmpty(samithi)) {
						Warehouse group = locationService.findSamithiById(Long.valueOf(samithi));
						if (!ObjectUtil.isEmpty(group)) {
							document.add(
									new Paragraph(new Phrase(getLocaleProperty("samithi") + " : " + group.getName(),
											new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));
						}
					}
					if (!StringUtil.isEmpty(branchIdParma)) {
						if (!getIsMultiBranch().equalsIgnoreCase("1")) {
							List<String> branchList = new ArrayList<>();
							branchList.add(branchIdParma);
							filter.setBranchesList(branchList);
							document.add(new Paragraph(new Phrase(getLocaleProperty("BranchId") + " : " + branchIdParma,
									new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));
						} else {
							List<String> branchList = new ArrayList<>();
							List<BranchMaster> branches = clientService.listChildBranchIds(branchIdParma);
							branchList.add(branchIdParma);
							branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
								branchList.add(branch.getBranchId());
							});
							filter.setBranchesList(branchList);
							document.add(new Paragraph(new Phrase(getLocaleProperty("BranchId") + " : " + branchIdParma,
									new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));

						}
					}

					if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
						if (!StringUtil.isEmpty(headerFields)) {
							String[] headerFieldsArr = headerFields.split("###");
							document.add(new Paragraph(new Phrase(
									headerFieldsArr[0] + " : " + headerFieldsArr[1] + "  " + headerFieldsArr[2] + " : "
											+ headerFieldsArr[3] + "  " + headerFieldsArr[4] + " : "
											+ headerFieldsArr[5],
									new Font(bf, 10, Font.NORMAL, GrayColor.GRAYBLACK))));
							
							
								/*document.add(new Paragraph(new Phrase(chunkRupee
										)));*/
								
								
						}
					}
					String empty = "       ";
					document.add(new Paragraph(
							new Phrase(empty, new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));
					flag = false;
					// table.addCell(cell);
				}

				ESESystem preference = preferncesService.findPrefernceById("1");

				if ((getIsMultiBranch().equalsIgnoreCase("1")
						&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
					if (StringUtil.isEmpty(branchIdValue)) {
						cell = new PdfPCell(new Phrase(!StringUtil
								.isEmpty(getBranchesMap().get(getParentBranchMap().get(distribution.getBranchId())))
										? getBranchesMap().get(getParentBranchMap().get(distribution.getBranchId()))
										: getBranchesMap().get(distribution.getBranchId()),
								cellFont));
						table.addCell(cell);
					}
					cell = new PdfPCell(new Phrase(!StringUtil.isEmpty(getBranchesMap().get(distribution.getBranchId()))
							? getBranchesMap().get(distribution.getBranchId()) : "", cellFont));

					table.addCell(cell);
				} else {
					if (StringUtil.isEmpty(branchIdValue)) {
						cell = new PdfPCell(new Phrase(!StringUtil.isEmpty(branchesMap.get(distribution.getBranchId()))
								? (branchesMap.get(distribution.getBranchId())) : "", cellFont));
						table.addCell(cell);
					}
				}

				DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
				/*
				 * cell = new PdfPCell(new
				 * Phrase(!ObjectUtil.isEmpty(distribution) ?
				 * (!StringUtil.isEmpty(dateFormat.format(distribution.
				 * getTxnTime())) ? genDate.format(distribution.getTxnTime()) :
				 * "") : "", cellFont));
				 */ // with time

				cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(distribution)
						? (!StringUtil.isEmpty(genDate.format(distribution.getTxnTime()))
								? genDate.format(distribution.getTxnTime()) : "")
						: "", cellFont));
				table.addCell(cell);

				if (!StringUtil.isEmpty(distribution.getServicePointName())) {
					cell = new PdfPCell(new Phrase(getLocaleProperty("warehouseStock"), cellFont));
					table.addCell(cell);

					/*cell = new PdfPCell(new Phrase(
							!ObjectUtil.isEmpty(distribution) ? (!StringUtil.isEmpty(distribution.getServicePointName())
									? distribution.getServicePointName() : "") : "",
							cellFont));*/
					cell = new PdfPCell(new Phrase(
							!ObjectUtil.isEmpty(distribution) ? (!StringUtil.isEmpty(distribution.getServicePointId())
									? warehouseMap.get(distribution.getServicePointId()): "") : "",
							cellFont));
					
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("NA", cellFont));
					table.addCell(cell);
				} else {

					cell = new PdfPCell(new Phrase(getLocaleProperty("fieldstaff.stock"), cellFont));
					table.addCell(cell);
					if (!getCurrentTenantId().equals(ESESystem.PRATIBHA_TENANT_ID))
						cell = new PdfPCell(new Phrase("NA", cellFont));
					else{
						//cell = new PdfPCell(new Phrase(distribution.getWarehouseName(), cellFont));
						cell = new PdfPCell(new Phrase(warehouseMap.get(distribution.getWarehouseCode()), cellFont));
					}
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(distribution)
							? (!StringUtil.isEmpty(distribution.getAgentName()) ? distribution.getAgentName() : "")
							: "", cellFont));

					table.addCell(cell);

				}

				Farmer farmer = farmerService.findFarmerByFarmerId(distribution.getFarmerId());
				if (!ObjectUtil.isEmpty(farmer)) {
					cell = new PdfPCell(new Phrase("Registered", cellFont));
					table.addCell(cell);
				} else {
					cell = new PdfPCell(new Phrase("Unregistered", cellFont));
					table.addCell(cell);
				}

				if (distribution.getSamithiName() != null) {

					cell = new PdfPCell(new Phrase(distribution.getFarmerName(), cellFont));
					table.addCell(cell);

					/*
					 * cell = new PdfPCell(new
					 * Phrase(distribution.getFatherName(), cellFont));
					 * table.addCell(cell);
					 */

					if (!ObjectUtil.isEmpty(distribution.getVillage())) {
						cell = new PdfPCell(new Phrase(distribution.getVillage().getVillageName(), cellFont));
						table.addCell(cell);
					} else {
						cell = new PdfPCell(new Phrase("", cellFont));
						table.addCell(cell);
					}

					cell = new PdfPCell(new Phrase("NA", cellFont));
					table.addCell(cell);

				} else {

					cell = new PdfPCell(
							new Phrase(
									!ObjectUtil.isEmpty(distribution.getAgroTransaction())
											&& !StringUtil.isEmpty(distribution.getAgroTransaction().getFarmerName())
													? distribution.getAgroTransaction().getFarmerName() : "NA",
									cellFont));
					table.addCell(cell);

					if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
						cell = new PdfPCell(new Phrase((!StringUtil.isEmpty(distribution.getMobileNumber()))
								? distribution.getMobileNumber() : "NA", cellFont));
					} else {
						if (!ObjectUtil.isEmpty(farmer)) {
							cell = new PdfPCell(new Phrase(
									(!StringUtil.isEmpty(farmer.getMobileNumber())) ? farmer.getMobileNumber() : "NA",
									cellFont));
						} else {
							cell = new PdfPCell(new Phrase((!StringUtil.isEmpty(distribution.getMobileNumber()))
									? distribution.getMobileNumber() : "NA", cellFont));
						}
					}
					table.addCell(cell);

					cell = new PdfPCell(
							new Phrase(!StringUtil.isEmpty(distribution.getVillage().getVillageName())
									? ((distribution.getVillage().getVillageName().split("-").length > 0)
											? distribution.getVillage().getVillageName().split("-")[1].trim() : "NA")
									: "NA", cellFont));
					table.addCell(cell);

				}
				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.SUSAGRI)) {
					cell = new PdfPCell(new Phrase(
							(!ObjectUtil.isEmpty(farmer) && !ObjectUtil.isEmpty(farmer.getSamithi())) ? farmer.getSamithi().getName() : "NA",
							cellFont));
					table.addCell(cell);
				}
				if (!ObjectUtil.isEmpty(distribution.getFreeDistribution())) {
					if ((distribution.getFreeDistribution().equals("1"))) {
						cell = new PdfPCell(new Phrase("Free Product Distribution", cellFont));
						table.addCell(cell);
					} else {
						cell = new PdfPCell(new Phrase("Normal Product Distribution", cellFont));
						table.addCell(cell);
					}
				} else {
					cell = new PdfPCell(new Phrase("NA", cellFont));
					table.addCell(cell);
				}

				Double qty = 0.0;
				if (!ObjectUtil.isEmpty(distribution.getDistributionDetails())) {
					for (DistributionDetail distributionDetail : distribution.getDistributionDetails()) {

						{
							qty = qty + distributionDetail.getQuantity();

						}
					}
					cell = new PdfPCell(new Phrase(CurrencyUtil.getDecimalFormat((qty), "##.00"), cellFont));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);
				}

				if (!distribution.getDistributionDetails().isEmpty()
						&& (distribution.getDistributionDetails().iterator().next().getSellingPrice() == 0.0)
						&& (distribution.getDistributionDetails().size() > 0)) {

					cell = new PdfPCell(new Phrase(
							CurrencyUtil.getDecimalFormat(distribution.getTotalAmount(), "##.00"), cellFont));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);

					cell = new PdfPCell(
							new Phrase(CurrencyUtil.getDecimalFormat(distribution.getTax(), "##.00"), cellFont));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(
							CurrencyUtil.getDecimalFormat(distribution.getFinalAmount(), "##.00"), cellFont));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(
							CurrencyUtil.getDecimalFormat(distribution.getPaymentAmount(), "##.00"), cellFont));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);

				} else {

					cell = new PdfPCell(new Phrase(distribution.getTotalAmount().toString(), cellFont));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);

					/*
					 * cell = new PdfPCell(new
					 * Phrase(distribution.getTax().toString(), cellFont));
					 * table.addCell(cell);
					 */

					cell = new PdfPCell(new Phrase(distribution.getFinalAmount().toString(), cellFont));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(
							CurrencyUtil.getDecimalFormat((distribution.getPaymentAmount()), "##.00"), cellFont));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);

				}

				if (!StringUtil.isEmpty(distribution.getSeasonCode())) {
					HarvestSeason season = farmerService.findSeasonNameByCode(distribution.getSeasonCode());
					cell = new PdfPCell(
							new Phrase(String.valueOf(!ObjectUtil.isEmpty(season) ? season.getName() : ""), cellFont));

					table.addCell(cell);
				}

				String sunGridcolumnHeaders = getLocaleProperty("exportSubColumnHeaderDistribution" + type);
				int subGridIterator = 1;

				cellFont = new Font(bf, 10, Font.BOLD, GrayColor.GRAYBLACK);

				setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));
				if (!StringUtil.isEmpty(enableBatchNo) && !enableBatchNo.equals("1")) {
					sunGridcolumnHeaders = sunGridcolumnHeaders.replace(",Batch No", "");
				}

				subGridColWidth = sunGridcolumnHeaders.split("\\,").length;

				cell = new PdfPCell(new Phrase("", cellFont));
				table.addCell(cell);

				for (String cellHeader : sunGridcolumnHeaders.split("\\,")) {
					if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
						cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
						//cellHeader = new String(cellHeader.getBytes("UTF-8"));
					} else {
						cellHeader = cellHeader.trim();
					}
					cell = new PdfPCell(new Phrase(cellHeader, cellFont));
					cell.setBackgroundColor(GrayColor.LIGHT_GRAY);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setNoWrap(false); // To set wrapping of text in cell.
					// cell.setColspan(3); // To add column span.
					table.addCell(cell);
				}

				/*
				 * if (StringUtil.isEmpty(branchIdValue)) { cols = 7; } else {
				 * cols = 6; }
				 */

				System.out.println(table.getNumberOfColumns());

				for (int i = (subGridColWidth + 1); i < table.getNumberOfColumns(); i++) {
					cell = new PdfPCell(new Phrase("", cellFont));
					table.addCell(cell);
				}

				cellFont = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK);

				for (DistributionDetail distributionDetail : distribution.getDistributionDetails()) {

					cell = new PdfPCell(new Phrase("", cellFont));
					table.addCell(cell);

					if (getCurrentTenantId().equalsIgnoreCase("sagi")) {
						cell = new PdfPCell(new Phrase(distributionDetail.getProduct() == null ? ""
								: distributionDetail.getProduct().getName(), cellFont));
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(
								distributionDetail.getSagiCode() == null ? "" : distributionDetail.getSagiCode(),
								cellFont));
						table.addCell(cell);
						cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(distributionDetail)
								? (!StringUtil.isEmpty(distributionDetail.getBatchNo())
										? distributionDetail.getBatchNo() : "NA")
								: "", cellFont));
						table.addCell(cell);
						cell = new PdfPCell(new Phrase(String.valueOf(distributionDetail.getQuantity()), cellFont));
						table.addCell(cell);

					} else {

						cell = new PdfPCell(new Phrase(distributionDetail.getProduct().getSubcategory() == null ? ""
								: distributionDetail.getProduct().getSubcategory().getName(), cellFont));
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(distributionDetail.getProduct() == null ? ""
								: distributionDetail.getProduct().getName(), cellFont));
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(distributionDetail.getProduct().getUnit() == null ? ""
								: distributionDetail.getProduct().getUnit(), cellFont));
						table.addCell(cell);

						if (!StringUtil.isEmpty(enableBatchNo) && enableBatchNo.equals("1")) {
							cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(distributionDetail)
									? (!StringUtil.isEmpty(distributionDetail.getBatchNo())
											? distributionDetail.getBatchNo() : "NA")
									: "", cellFont));
							table.addCell(cell);
						}
						cell = new PdfPCell(new Phrase(distributionDetail.getProduct() == null ? ""
								: String.valueOf(distributionDetail.getExistingQuantity()), cellFont));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(String.valueOf(distributionDetail.getCostPrice()), cellFont));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						table.addCell(cell);

						if (distributionDetail.getSellingPrice() == 0.00) {

							/*
							 * cell = new PdfPCell(new Phrase("NA", cellFont));
							 * table.addCell(cell);
							 * 
							 * cell = new PdfPCell(new Phrase("NA", cellFont));
							 * table.addCell(cell);
							 */
						} else {

							if (!getCurrentTenantId().equalsIgnoreCase("agro")) {

								cell = new PdfPCell(
										new Phrase((String.valueOf(distributionDetail.getSellingPrice())), cellFont));
								cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								table.addCell(cell);

								cell = new PdfPCell(
										new Phrase(String.valueOf(Double.valueOf(distributionDetail.getSellingPrice())
												* Double.valueOf(distributionDetail.getQuantity())), cellFont));
								cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								table.addCell(cell);

							}
						}

						cell = new PdfPCell(
								new Phrase(
										CurrencyUtil.getDecimalFormat(Double.valueOf(distributionDetail.getCostPrice())
												* Double.valueOf(distributionDetail.getQuantity()), "##.00"),
										cellFont));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(String.valueOf(distributionDetail.getQuantity()), cellFont));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						table.addCell(cell);

						Double diffQty = 0.00;
						if (!StringUtil.isEmpty(distributionDetail.getExistingQuantity())) {
							diffQty = Double.valueOf(distributionDetail.getExistingQuantity())
									- (distributionDetail.getQuantity());
							cell = new PdfPCell(
									new Phrase(CurrencyUtil.getDecimalFormat((diffQty), "##.00"), cellFont));
						}
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						table.addCell(cell);

						/*
						 * if (StringUtil.isEmpty(branchIdValue)) { cols = 8; }
						 * else { cols = 8; }
						 */
					}
					for (int i = (subGridColWidth + 1); i < table.getNumberOfColumns(); i++) {
						cell = new PdfPCell(new Phrase("", cellFont));
						table.addCell(cell);
					}

				}

			}

		} else {
			if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
				if (!StringUtil.isEmpty(headerFields)) {
					String[] headerFieldsArr = headerFields.split("###");
					if (getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)) {     //For chetna 
						document.add(new Paragraph(new Phrase(
								headerFieldsArr[0] + " : " + headerFieldsArr[1],
								new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));
					}
					else{
					document.add(new Paragraph(new Phrase(
							headerFieldsArr[0] + " : " + headerFieldsArr[1] + "  " + headerFieldsArr[2] + " : "
									+ headerFieldsArr[3] + "  " + headerFieldsArr[4] + " : " + headerFieldsArr[5],
							new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));
				}
					}
			}

			List<DistributionDetail> mainGridRowsDatas = (List<DistributionDetail>) data.get(ROWS);
			if (ObjectUtil.isListEmpty(mainGridRowsDatas))
				return null;

			for (DistributionDetail distributionDetail : mainGridRowsDatas) {

				if ((!StringUtil.isEmpty(distributorId) || !StringUtil.isEmpty(selectedFieldStaff)
						|| !StringUtil.isEmpty(seasonCode) || !StringUtil.isEmpty(product)|| !StringUtil.isEmpty(branchIdParma)
						|| !StringUtil.isEmpty(location)) && flag) {

					if (!StringUtil.isEmpty(distributorId)) {

						String distributorType = getLocaleProperty("distributor" + type);
						String distributor;

						if (FARMER.equals(type)) {

							distributor = distributionDetail.getDistribution().getAgroTransaction().getFarmerName()
									+ " - " + distributionDetail.getDistribution().getAgroTransaction().getFarmerId();
						} else {
							distributor = distributionDetail.getDistribution().getAgroTransaction().getAgentName();

						}

						document.add(new Paragraph(new Phrase(distributorType + " : " + distributor,
								new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));

					}

					if (!StringUtil.isEmpty(branchIdParma)) {
						if (!getIsMultiBranch().equalsIgnoreCase("1")) {
							List<String> branchList = new ArrayList<>();
							branchList.add(branchIdParma);
							filter.setBranchesList(branchList);
							document.add(new Paragraph(new Phrase(getLocaleProperty("app.branch") + " : " + branchIdParma,
									new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));
						} else {
							List<String> branchList = new ArrayList<>();
							List<BranchMaster> branches = clientService.listChildBranchIds(branchIdParma);
							branchList.add(branchIdParma);
							branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
								branchList.add(branch.getBranchId());
							});
							filter.setBranchesList(branchList);
							document.add(new Paragraph(new Phrase(getLocaleProperty("BranchId") + " : " + branchIdParma,
									new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));

						}
					}
					
					
					if (!StringUtil.isEmpty(selectedFieldStaff)) {

						document.add(new Paragraph(new Phrase(
								getLocaleProperty("distributiorIdagent") + " : "
										+ distributionDetail.getDistribution().getAgentName(),
								new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));

					}

					if (!StringUtil.isEmpty(location)) {
						document.add(new Paragraph(new Phrase(
								getLocaleProperty("locationagent") + " : "
										+ warehouseMap.get(location),
								new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));
					}

					if (!StringUtil.isEmpty(product)) {
						Product prod = productService.findProductById(Long.valueOf(product));
						document.add(new Paragraph(new Phrase(getLocaleProperty("product") + " : " + prod.getName(),
								new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));
					}

					if (!StringUtil.isEmpty(seasonCode)) {

						HarvestSeason season = farmerService
								.findSeasonNameByCode(distributionDetail.getDistribution().getSeasonCode());
						document.add(new Paragraph(new Phrase(getLocaleProperty("season") + " : " + season.getName(),
								new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));
					}

					flag = false;

				}

				serialNo++;

				cell = new PdfPCell(new Paragraph(new Phrase(String.valueOf(serialNo), cellFont)));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				if ((getIsMultiBranch().equalsIgnoreCase("1")
						&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
					if (StringUtil.isEmpty(branchIdValue)) {

						cell = new PdfPCell(new Paragraph(new Phrase(!StringUtil.isEmpty(getBranchesMap()
								.get(getParentBranchMap().get(distributionDetail.getDistribution().getBranchId())))
										? getBranchesMap().get(getParentBranchMap()
												.get(distributionDetail.getDistribution().getBranchId()))
										: getBranchesMap().get(distributionDetail.getDistribution().getBranchId()))));
						table.addCell(cell);

					}

					cell = new PdfPCell(new Phrase(!StringUtil
							.isEmpty(getBranchesMap().get(distributionDetail.getDistribution().getBranchId()))
									? getBranchesMap().get(distributionDetail.getDistribution().getBranchId()) : ""));
					table.addCell(cell);

				} else {

					if (StringUtil.isEmpty(branchIdValue)) {

						cell = new PdfPCell(
								new Phrase(!StringUtil.isEmpty(distributionDetail.getDistribution().getBranchId())
										? (branchesMap.get(distributionDetail.getDistribution().getBranchId())) : ""));
						table.addCell(cell);

					}
				}

				DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());

				cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(distributionDetail.getDistribution())
						? (!StringUtil.isEmpty(genDate.format(distributionDetail.getDistribution().getTxnTime()))
								? genDate.format(distributionDetail.getDistribution().getTxnTime()) : "")
						: ""));
				table.addCell(cell);

				if (FARMER.equalsIgnoreCase(type)) {

					if (!StringUtil.isEmpty(distributionDetail.getDistribution().getServicePointName())) {

						cell = new PdfPCell(new Phrase("WarehouseStock"));
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(distributionDetail.getDistribution())
								? (!StringUtil.isEmpty(distributionDetail.getDistribution().getServicePointId())
										? warehouseMap.get(distributionDetail.getDistribution().getServicePointId()) : "")
								: ""));
						table.addCell(cell);

						cell = new PdfPCell(new Phrase("NA"));
						table.addCell(cell);

					} else {

						cell = new PdfPCell(new Phrase("MobileUserStock"));
						table.addCell(cell);

						cell = new PdfPCell(new Phrase("NA"));
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(distributionDetail.getDistribution())
								? (!StringUtil.isEmpty(distributionDetail.getDistribution().getAgentName())
										? distributionDetail.getDistribution().getAgentName() : "")
								: ""));
						table.addCell(cell);

					}

					if (distributionDetail.getDistribution().getSamithiName() != null) {

						cell = new PdfPCell(new Phrase("Registered"));
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(distributionDetail.getDistribution().getFarmerName()));
						table.addCell(cell);

						cell = new PdfPCell(
								new Phrase(distributionDetail.getDistribution().getVillage().getVillageName()));
						table.addCell(cell);

						cell = new PdfPCell(new Phrase("NA"));
						table.addCell(cell);

					} else {

						cell = new PdfPCell(new Phrase("UnRegistered"));
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(distributionDetail.getDistribution().getFarmerName()));
						table.addCell(cell);

						cell = new PdfPCell(
								new Phrase(distributionDetail.getDistribution().getVillage().getVillageName()));
						table.addCell(cell);

						Farmer farmer = farmerService
								.findFarmerByFarmerId(distributionDetail.getDistribution().getFarmerId());
						cell = new PdfPCell(new Phrase(farmer.getMobileNumber()));
						table.addCell(cell);

					}

					if ((distributionDetail.getDistribution().getDistributionDetails().iterator().next()
							.getSellingPrice() == 0.00)
							&& (distributionDetail.getDistribution().getDistributionDetails().size() > 0)) {

						cell = new PdfPCell(new Phrase("Yes"));
						table.addCell(cell);

					} else {

						cell = new PdfPCell(new Phrase("No"));
						table.addCell(cell);

					}

					if (!ObjectUtil.isEmpty(distributionDetail.getDistribution().getDistributionDetails())) {
						Double qty = 0.0;
						{

							qty = qty + distributionDetail.getQuantity();
							/*
							 * cell = new PdfPCell(CurrencyUtil
							 * .getDecimalFormat(Double.valueOf(qty)));
							 */
							cell = new PdfPCell(new Phrase(CurrencyUtil.getDecimalFormat(qty, "##.00")));

							table.addCell(cell);

						}
					}
					if (distributionDetail.getDistribution().getDistributionDetails().iterator().next()
							.getSellingPrice() == 0.00) {

						cell = new PdfPCell(new Phrase("NA"));
						table.addCell(cell);

						cell = new PdfPCell(new Phrase("NA"));
						table.addCell(cell);

						cell = new PdfPCell(new Phrase("NA"));
						table.addCell(cell);

						cell = new PdfPCell(new Phrase("NA"));
						table.addCell(cell);

						cell = new PdfPCell(new Phrase("NA"));
						table.addCell(cell);

					} else {

						cell = new PdfPCell(
								new Phrase(distributionDetail.getDistribution().getTotalAmount().toString()));
						table.addCell(cell);

						cell = new PdfPCell(
								new Phrase(distributionDetail.getDistribution().getFinalAmount().toString()));
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(CurrencyUtil
								.getDecimalFormat(distributionDetail.getDistribution().getPaymentAmount(), "##.00")));
						table.addCell(cell);

					}
				} else {

					cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(distributionDetail.getDistribution())
							? (!StringUtil.isEmpty(distributionDetail.getDistribution().getServicePointId())
									?warehouseMap.get(distributionDetail.getDistribution().getServicePointId()) : "")
							: ""));
					table.addCell(cell);

					cell = new PdfPCell(
							new Phrase(!ObjectUtil.isEmpty(distributionDetail.getDistribution().getAgroTransaction())
									&& !ObjectUtil.isEmpty(distributionDetail.getDistribution())
											? (!StringUtil.isEmpty(distributionDetail.getDistribution().getAgentId())
													? distributionDetail.getDistribution().getAgentName() : "")
											: ""));
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(distributionDetail.getProduct() != null
							&& distributionDetail.getProduct().getSubcategory() != null
									? distributionDetail.getProduct().getSubcategory().getName() : ""));
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(distributionDetail)
							? (!ObjectUtil.isEmpty(distributionDetail.getProduct())
									? distributionDetail.getProduct().getName() : "")
							: ""));
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(
							distributionDetail.getProduct() == null ? "" : distributionDetail.getProduct().getUnit()));
					table.addCell(cell);

					if (getEnableBatchNo().equals("1")) {

						cell = new PdfPCell(new Phrase(
								distributionDetail.getBatchNo() == null ? "" : distributionDetail.getBatchNo()));
						table.addCell(cell);

					}

				/*	cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(distributionDetail)
							? (!StringUtil.isEmpty(distributionDetail.getExistingQuantity())
									? distributionDetail.getExistingQuantity() : "")
							: ""));

					table.addCell(cell);
*/
					cell = new PdfPCell(new Phrase(df.format(distributionDetail.getQuantity())));
					table.addCell(cell);

					/*cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(distributionDetail)
							? (!StringUtil.isEmpty(distributionDetail.getCurrentQuantity())
									? distributionDetail.getCurrentQuantity() : "")
							: ""));
					table.addCell(cell);*/

					if (!StringUtil.isEmpty(distributionDetail.getDistribution().getSeasonCode())) {
						HarvestSeason season = farmerService
								.findSeasonNameByCode(distributionDetail.getDistribution().getSeasonCode());

						cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(season) ? season.getName() : ""));
						table.addCell(cell);

					}

				}

				if (FARMER.equals(type)) {

					String sunGridcolumnHeaders = getLocaleProperty("exportSubColumnHeader" + type);

					cellFont = new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK); // setting

					cell = new PdfPCell(new Phrase("", cellFont)); // add

					table.addCell(cell);

					for (String cellHeader : sunGridcolumnHeaders.split("\\,")) {

						cell = new PdfPCell(new Phrase(cellHeader, cellFont));
						cell.setBackgroundColor(GrayColor.LIGHT_GRAY);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell.setNoWrap(false); // To set wrapping of text in
												// cell.
						// cell.setColspan(3); // To add column span.
						table.addCell(cell);
					}

					cell = new PdfPCell(new Phrase("", cellFont));
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(distributionDetail.getProduct().getSubcategory() == null ? ""
							: distributionDetail.getProduct().getSubcategory().getName()));
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(
							distributionDetail.getProduct() == null ? "" : distributionDetail.getProduct().getName()));
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(
							distributionDetail.getProduct() == null ? "" : distributionDetail.getProduct().getUnit()));
					table.addCell(cell);

					ESESystem preferences1 = preferncesService.findPrefernceById("1");
					setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));

					if (getEnableBatchNo().equals("1")) {

						cell = new PdfPCell(new Phrase(
								distributionDetail.getBatchNo() == null ? "" : distributionDetail.getBatchNo()));
						table.addCell(cell);

					}

					cell = new PdfPCell(new Phrase(String.valueOf(distributionDetail.getExistingQuantity())));
					table.addCell(cell);

					if (distributionDetail.getSellingPrice() == 0.00) {

						cell = new PdfPCell(new Phrase("NA"));
						table.addCell(cell);

						cell = new PdfPCell(new Phrase("NA"));
						table.addCell(cell);

					} else {

						cell = new PdfPCell(new Phrase(String.valueOf(distributionDetail.getSellingPrice())));
						table.addCell(cell);

						cell = new PdfPCell(
								new Phrase(String.valueOf(Double.valueOf(distributionDetail.getSellingPrice())
										* Double.valueOf(distributionDetail.getQuantity()))));
						table.addCell(cell);

					}

					cell = new PdfPCell(new Phrase(String.valueOf(distributionDetail.getQuantity())));
					table.addCell(cell);

				}

			}

		}
		/* End */
		table.addCell(cell);
		document.add(table); // Add table to document.

		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		document.close();
		fileOut.close();
		return stream;
	}

	@Override
	public String getCurrentMenu() {
		String type = request.getParameter("type");
		if (!StringUtil.isEmpty(type)) {
			if (type.equalsIgnoreCase("farmer")) {
				return getText("menu.select");
			} else if (type.equalsIgnoreCase("agent")) {
				return getText("menu1.select");
			}
		}
		return getText("menu.select");

	}

	public String getDaterange() {
		return daterange;
	}

	public void setDaterange(String daterange) {
		this.daterange = daterange;
	}

	public String getFatherName() {

		return fatherName;
	}

	public void setFatherName(String fatherName) {

		this.fatherName = fatherName;
	}

	public Map<String, String> getSeasonsList() {

		Map<String, String> seasonMap = new LinkedHashMap<String, String>();
		List<Object[]> seasonList = farmerService.listSeasonCodeAndName();
		/*
		 * seasonMap = seasonList.stream().collect( Collectors.toMap(obj ->
		 * (String.valueOf(obj[0])), obj -> String.valueOf(obj[1] + " - " +
		 * obj[0])));
		 */
		seasonMap = seasonList.stream()
				.collect(Collectors.toMap(obj -> (String.valueOf(obj[0])), obj -> String.valueOf(obj[1])));
		return seasonMap;
	}

	public String getSeasonCode() {
		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
	}

	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}

	public String getExportLimit() {
		return exportLimit;
	}

	public void setExportLimit(String exportLimit) {
		this.exportLimit = exportLimit;
	}

	public IClientService getClientService() {
		return clientService;
	}

	public void setClientService(IClientService clientService) {
		this.clientService = clientService;
	}

	public String getFarmerName() {
		return farmerName;
	}

	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
	}

	public String getEnableBatchNo() {
		return enableBatchNo;
	}

	public void setEnableBatchNo(String enableBatchNo) {
		this.enableBatchNo = enableBatchNo;
	}

	public void setProdCenterList(Map<String, String> prodCenterList) {
		this.prodCenterList = prodCenterList;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getIcsName() {
		return icsName;
	}

	public void setIcsName(String icsName) {
		this.icsName = icsName;
	}

	public String getFpo() {
		return fpo;
	}

	public void setFpo(String fpo) {
		this.fpo = fpo;
	}

	/*
	 * public Map<String, String> getStateList() {
	 * 
	 * Map<String, String> stateMap = new LinkedHashMap<String, String>();
	 * 
	 * List<State> stateList = locationService.listOfStates(); for (State obj :
	 * stateList) { stateMap.put(obj.getCode(),obj.getName());
	 * 
	 * } return stateMap;
	 * 
	 * }
	 */
	/*
	 * public Map<String, String> getWarehouseList() { AtomicInteger i = new
	 * AtomicInteger(0); Map<String, String> warehouseMap = new
	 * LinkedHashMap<>(); FarmCatalogueMaster farmCatalougeMaster =
	 * catalogueService
	 * .findFarmCatalogueMasterByName(getLocaleProperty("cooperative")); if
	 * (!ObjectUtil.isEmpty(farmCatalougeMaster)) { Double d = new
	 * Double(farmCatalougeMaster.getId()); List<FarmCatalogue>
	 * farmCatalougeList =
	 * catalogueService.findFarmCatalougeByAlpha(d.intValue());
	 * 
	 * for (FarmCatalogue catalogue : farmCatalougeList) { if
	 * (!catalogue.getName().trim().contentEquals(ESESystem.OTHERS)) {
	 * warehouseMap.put(catalogue.getCode(), catalogue.getName()); } }
	 * 
	 * } warehouseMap.put("99", "Others");
	 * 
	 * return warehouseMap;
	 * 
	 * }
	 * 
	 * public Map<String, String> getIcsNameList() {
	 * 
	 * AtomicInteger i = new AtomicInteger(0); Map<String, String> icsMap = new
	 * LinkedHashMap<>(); FarmCatalogueMaster farmCatalougeMaster =
	 * catalogueService.findFarmCatalogueMasterByName(getLocaleProperty(
	 * "icsName")); if (!ObjectUtil.isEmpty(farmCatalougeMaster)) { Double d =
	 * new Double(farmCatalougeMaster.getId()); List<FarmCatalogue>
	 * farmCatalougeList =
	 * catalogueService.findFarmCatalougeByAlpha(d.intValue());
	 * 
	 * for (FarmCatalogue catalogue : farmCatalougeList) { if
	 * (!catalogue.getName().trim().contentEquals(ESESystem.OTHERS)) {
	 * icsMap.put(catalogue.getCode(), catalogue.getName()); } }
	 * 
	 * } icsMap.put("99", "Others");
	 * 
	 * return icsMap;
	 * 
	 * }
	 */
	public void populateFieldStaffList() {
		JSONArray fieldStaffArr = new JSONArray();
		List<String> testList = new ArrayList<>();
		List<Object[]> fieldStaffList = locationService.listOfMobileUserByDistribution();
		if (!ObjectUtil.isListEmpty(fieldStaffList)) {
			fieldStaffList.stream().filter(obj -> (obj[0]) != null && !StringUtil.isEmpty(obj[0].toString()))
					.forEach(obj -> {
						fieldStaffArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
					});
		}
		sendAjaxResponse(fieldStaffArr);
	}

	public void populateFarmerList() {
		List<String> flagList = new ArrayList<>();
		JSONArray farmerfArr = new JSONArray();
		List<Object[]> farmerList = farmerService.listFarmerInfoByDistribution();
		if (!ObjectUtil.isListEmpty(farmerList)) {
			flagList = farmerList.stream().filter(obj -> (obj[1]) != null && !ObjectUtil.isEmpty(obj[1]))
					.map(obj -> String.valueOf(obj[1])).distinct().collect(Collectors.toList());
			flagList.forEach(obj -> {
				farmerfArr.add(getJSONObject(obj.toString(), obj.toString()));
			});
		}
		sendAjaxResponse(farmerfArr);
	}

	public void populateLocationList() {
		JSONArray warehousefArr = new JSONArray();
		List<Warehouse> warehouseList = locationService.listOfCooperatives();
		if (!ObjectUtil.isEmpty(warehouseList)) {
			warehouseList.forEach(obj -> {
				warehousefArr.add(getJSONObject(obj.getCode(), obj.getName()));
			});
		}
		sendAjaxResponse(warehousefArr);
	}

	public void populateProductList() {
		List<Object[]> productList = productService.listFarmProducts();
		JSONArray productArr = new JSONArray();
		if (!ObjectUtil.isListEmpty(productList)) {
			productList.forEach(obj -> {
				productArr.add(getJSONObject(obj[1].toString(), obj[0].toString()));
			});
		}
		sendAjaxResponse(productArr);
	}

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

	public void populateStateList() {
		List<State> stateList = locationService.listOfStates();
		JSONArray stateArr = new JSONArray();
		if (!ObjectUtil.isListEmpty(stateList)) {
			stateList.forEach(obj -> {
				stateArr.add(getJSONObject(obj.getCode(), obj.getName()));
			});
		}
		sendAjaxResponse(stateArr);
	}

	public void populateWarehouseList() {
		JSONArray warehouseArr = new JSONArray();
		FarmCatalogueMaster farmCatalougeMaster = catalogueService
				.findFarmCatalogueMasterByName(getLocaleProperty("cooperative"));
		if (!ObjectUtil.isEmpty(farmCatalougeMaster)) {
			Double d = new Double(farmCatalougeMaster.getId());
			List<FarmCatalogue> farmCatalougeList = catalogueService.findFarmCatalougeByAlpha(d.intValue());
			farmCatalougeList.stream().filter(obj -> (!obj.getName().trim().contentEquals(ESESystem.OTHER)))
					.forEach(obj -> {
						warehouseArr.add(getJSONObject(obj.getCode(), obj.getName()));

					});
			/*
			 * for (FarmCatalogue catalogue : farmCatalougeList) { if
			 * (!catalogue.getName().trim().contentEquals(ESESystem.OTHERS)) {
			 * cata warehouseMap.put(catalogue.getCode(), catalogue.getName());
			 * } }
			 */

		}
		warehouseArr.add(getJSONObject("99", "Others"));
		sendAjaxResponse(warehouseArr);
	}

	public void populateIcsList() {
		JSONArray icsArr = new JSONArray();
		Map<String, String> icsMap = new LinkedHashMap<>();
		FarmCatalogueMaster farmCatalougeMaster = catalogueService
				.findFarmCatalogueMasterByName(getLocaleProperty("icsName"));
		if (!ObjectUtil.isEmpty(farmCatalougeMaster)) {
			Double d = new Double(farmCatalougeMaster.getId());
			List<FarmCatalogue> farmCatalougeList = catalogueService.findFarmCatalougeByAlpha(d.intValue());

			farmCatalougeList.stream().filter(obj -> (!obj.getName().trim().contentEquals(ESESystem.OTHER)))
					.forEach(obj -> {
						icsArr.add(getJSONObject(obj.getCode(), obj.getName()));

					});

			/*
			 * for (FarmCatalogue catalogue : farmCatalougeList) { if
			 * (!catalogue.getName().trim().contentEquals(ESESystem.OTHERS)) {
			 * icsMap.put(catalogue.getCode(), catalogue.getName()); } }
			 */

		}
		icsArr.add(getJSONObject("99", "Others"));
		sendAjaxResponse(icsArr);
	}

	public void populateFarmerMethod() throws IOException {
		String agentId = null;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		if (!StringUtil.isEmpty(selectedFieldStaff)) {
			// filter.getAgroTransaction().setRefAgroTransaction(new
			// AgroTransaction());
			String agentIds[] = selectedFieldStaff.split("-");
			agentId = agentIds[0].trim();

		}
		Object[] datas = farmerService.findTotalQtyAndAmt(location, farmerId, product, seasonCode, stateName, fpo,
				icsName, branchIdParma, subBranchIdParma, agentId,
				DateUtil.convertStringToDate(startDate, DateUtil.DATE_FORMAT),
				DateUtil.convertStringToDate(endDate, DateUtil.DATE_FORMAT), branch);
		jsonObject.put("totalQty", !StringUtil.isEmpty(datas[0]) ? datas[0] : "0.0");
		jsonObject.put("totalAmt", !StringUtil.isEmpty(datas[1]) ? datas[1] : "0.0");
		jsonObject.put("totalPayAmt", !StringUtil.isEmpty(datas[2]) ? datas[2] : "0.0");

		jsonArray.add(jsonObject);

		response.setContentType("text/JSON");
		PrintWriter out = response.getWriter();
		if (jsonArray.size() > 0)
			out.println(jsonArray.toString());

	}

	public String getHeaderFields() {
		return headerFields;
	}

	public void setHeaderFields(String headerFields) {
		this.headerFields = headerFields;
	}
	public void populateLatAndLon() throws IOException{
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		PMTImageDetails imageDetail = productDistributionService.findPMTImageDetailById(Long.valueOf(id));
		jsonObject.put("lat", !StringUtil.isEmpty(imageDetail.getLatitude()) ?imageDetail.getLatitude() : "");
		jsonObject.put("lon", !StringUtil.isEmpty(imageDetail.getLongitude()) ? imageDetail.getLongitude() : "");
		jsonArray.add(jsonObject);

		response.setContentType("text/JSON");
		PrintWriter out = response.getWriter();
		if (jsonArray.size() > 0)
			out.println(jsonArray.toString());
	}

	public void populateAgentMethod() throws IOException, ParseException {
		String farmerId = null;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		if (!StringUtil.isEmpty(selectedFieldStaff)) {
			// filter.getAgroTransaction().setRefAgroTransaction(new
			// AgroTransaction());
			String agentIds[] = selectedFieldStaff.split("-");
			agentId = agentIds[0].trim();

		}

		Object[] datas = farmerService.findTotalQtyInAgent(location, agentId, productId, seasonCode, stateName, fpo,
				icsName, branchIdParma, subBranchIdParma, agentId,
				DateUtil.convertStringToDate(startDate, DateUtil.DATE_FORMAT),
				DateUtil.convertStringToDate(endDate, DateUtil.DATE_FORMAT), branch);
		jsonObject.put("existQty", !StringUtil.isEmpty(datas[1]) ? datas[1] : "0.0");
		jsonObject.put("distQty", !StringUtil.isEmpty(datas[0]) ? datas[0] : "0.0");
		jsonObject.put("currentQty", !StringUtil.isEmpty(datas[2]) ? datas[2] : "0.0");
		jsonArray.add(jsonObject);

		response.setContentType("text/JSON");
		PrintWriter out = response.getWriter();
		if (jsonArray.size() > 0)
			out.println(jsonArray.toString());

	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public List<JSONObject> getJsonList_chart() {
		return jsonList_chart;
	}

	public void setJsonList_chart(List<JSONObject> jsonList_chart) {
		this.jsonList_chart = jsonList_chart;
	}

	public String getWarehouse_chart() {
		return warehouse_chart;
	}

	public void setWarehouse_chart(String warehouse_chart) {
		this.warehouse_chart = warehouse_chart;
	}

	public void getChartData() {

		if (!StringUtil.isEmpty(getWarehouse_chart())) {
			getJsonList_chart().clear();
			List<Object[]> warehouseAndProductsDetails = farmerService.findProductsByWarehouse(getWarehouse_chart(),
					getChartType());
			for (Iterator iterator = warehouseAndProductsDetails.iterator(); iterator.hasNext();) {
				Object[] objects = (Object[]) iterator.next();

				JSONObject jsonObject_chart = new JSONObject();
				jsonObject_chart.put("id", objects[0]);
				jsonObject_chart.put("warehouse", objects[1]);
				jsonObject_chart.put("product", objects[2]);
				jsonObject_chart.put("existingQuantity", objects[3]);
				jsonObject_chart.put("distributionQuantity", objects[4]);
				jsonObject_chart.put("currentQuantity", objects[5]);
				jsonList_chart.add(jsonObject_chart);

			}
		} else {

			getJsonList_chart().clear();
			List<Object[]> DistributionWarehouseDetails = farmerService.listDistributionWarehouse(getBranch(),
					getSeasonCode(), getChartType());
			for (Iterator iterator = DistributionWarehouseDetails.iterator(); iterator.hasNext();) {
				Object[] objects = (Object[]) iterator.next();

				JSONObject jsonObject_chart = new JSONObject();
				jsonObject_chart.put("id", objects[0]);
				jsonObject_chart.put("warehouse", objects[1]);
				jsonObject_chart.put("distributionQuantity", objects[2]);
				jsonList_chart.add(jsonObject_chart);
			}

		}
		printAjaxResponse(getJsonList_chart(), "text/html");
		getJsonList_chart().clear();

	}

	public String getDistImgAvil() {
		return distImgAvil;
	}

	public void setDistImgAvil(String distImgAvil) {
		this.distImgAvil = distImgAvil;
	}

	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	public String populateDistributionImage() {

		try {
			setId(id);
			OutputStream out = response.getOutputStream();
			PMTImageDetails imageDetail = productDistributionService.findPMTImageDetailById(Long.valueOf(id));
			byte[] image = imageDetail.getPhoto();
			response.setContentType("multipart/form-data");
			out.write(image);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public Map<String, String> getWarehouseMap() {
		return warehouseMap;
	}

	public void setWarehouseMap(Map<String, String> warehouseMap) {
		this.warehouseMap = warehouseMap;
	}
	public Map<String, String> getSamithiList() {
		Map<String, String> warehouseMap = new LinkedHashMap<>();
		warehouseMap = locationService.listOfSamithi().stream()
				.collect(Collectors.toMap(warehouse -> String.valueOf(warehouse.getId()), Warehouse::getName));
		return warehouseMap;
	}

	public String getSamithi() {
		return samithi;
	}

	public void setSamithi(String samithi) {
		this.samithi = samithi;
	}
	
	
}
