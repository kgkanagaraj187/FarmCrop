/*
 * PMTReportAction.java
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
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

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
import org.apache.poi.ss.usermodel.CellStyle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.opensymphony.xwork2.ActionContext;
import com.sourcetrace.eses.BreadCrumb;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.PMT;
import com.sourcetrace.eses.order.entity.txn.PMTDetail;
import com.sourcetrace.eses.order.entity.txn.PMTFarmerDetail;
import com.sourcetrace.eses.order.entity.txn.PMTImageDetails;
import com.sourcetrace.eses.order.entity.txn.TransferInfo;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.umgmt.entity.User;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

public class PMTReportAction extends BaseReportAction implements IExporter {

	private static final long serialVersionUID = 5753871231037958377L;

	private static final String PMTNT = "pmtnt";
	private static final String PMTNR = "pmtnr";
	private static final String PROCUREMENT_CENTER = "procTrans";
	private static final String PRODUCT_RECEIVE = "procRecp";
	private static final String NO_OF_BAGS = "bags";
	private static final String GROSS_WEIGHT = "grossWt";
	private static final String SUB_TOTAL = "subTotal";

	/** The date format. */
	DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
	private Map<String, String> fields = new LinkedHashMap<String, String>();
	private Map<String, String> filterFields = new LinkedHashMap<String, String>();
	private ILocationService locationService;
	private IProductDistributionService productDistributionService;
	private IFarmerService farmerService;
	private IPreferencesService preferncesService;
	@Autowired
	private IAgentService agentService;
	private List<String> warehouseList = new ArrayList<String>();
	private PMT filter;
	private String type;
	private String disableColumns;
	private String identityForGrid;
	private String receiptNo;
	private String operationType;
	private String senderArea;
	private String senderCity;
	private String senderCityMTNR;
	private String truck;
	private String driver;
	private String mtntReceiptNumber;
	private String mtnrReceiptNumber;
	private String cooperative;
	private String transType;
	private String season;
	private String branchIdParma;
	int serialNo = 1;
	private String xlsFileName;
	private InputStream fileInputStream;
	private String proCenter;
	private String ginning;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private String imagesId;
	private String imgId;

	private String icsName;
	private String pmtFarmerData;
	private String grade;
	private String product;
	private String pmtId;
	private String selectedFieldStaff;
	String productName;
	/**
	 * @see com.sourcetrace.esesw.view.BaseReportAction#list()
	 */

	public String list() {

		Calendar currentDate = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		super.startDate = df.format(DateUtil.getdateBeforeOneMonth(currentDate.get(Calendar.YEAR),
				currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)));
		super.endDate = df.format(currentDate.getTime());
		type = request.getParameter("type");
		/*
		 * fields.put("1", getText("date")); fields.put("2",
		 * getText("receiptNumber")); fields.put("3", getText("cooperative"));
		 * fields.put("4", getText("truckId")); fields.put("5",
		 * getText("driverName")); fields.put("6", getText("transType"));
		 * fields.put("7", getText("branchIdParma"));
		 */

		/*
		 * filterFields.put("1", "date"); if (PMTNT.equals(type))
		 * filterFields.put("2", "mtntReceiptNumber"); else
		 * filterFields.put("2", "mtnrReceiptNumber"); filterFields.put("3",
		 * "cooperative"); filterFields.put("4", "truckId");
		 * filterFields.put("5", "driverName"); filterFields.put("6",
		 * "transType"); filterFields.put("7", "season"); filterFields.put("8",
		 * "branchIdParma");
		 */

		request.setAttribute("fields", fields);
		setFilter(new PMT());
		request.setAttribute(HEADING, getText(LIST + type));
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

		setFilter(new PMT());
		type = request.getParameter("type");
		Map data= new HashMap<>();
		if (PMTNT.equals(type)) {
			filter.setStatusCode(PMT.Status.MTNT.ordinal());

			if (!StringUtil.isEmpty(mtntReceiptNumber))
				filter.setMtntReceiptNumber(mtntReceiptNumber);

			if (!StringUtil.isEmpty(season)) {
				filter.setSeasonCode(season);
			} 
			if (!StringUtil.isEmpty(selectedFieldStaff)) {
				filter.setMtntTransferInfo(new TransferInfo());
				filter.getMtntTransferInfo().setAgentId(selectedFieldStaff);
			}
			if (!StringUtil.isEmpty(cooperative)) {
				filter.setTrnType(PMT.TRN_TYPE_STOCK_TRNASFER);
				filter.setCoOperative(new Warehouse());
				filter.getCoOperative().setCode(cooperative);
			}
			if (!StringUtil.isEmpty(truck))
				filter.setTruckId(truck);

			if (!StringUtil.isEmpty(driver))
				filter.setDriverName(driver);
			data = readData();
		}else if (PMTNR.equals(type)) {
			filter.setStatusCode(PMT.Status.MTNR.ordinal());

			if (!StringUtil.isEmpty(mtnrReceiptNumber))
				filter.setMtnrReceiptNumber(mtnrReceiptNumber);

			if (!StringUtil.isEmpty(season)) {
				filter.setSeasonCode(season);
			} 
			if (!StringUtil.isEmpty(cooperative)) {
				filter.setCoOperative(new Warehouse());
				filter.getCoOperative().setCode(cooperative);
			}
			if (!StringUtil.isEmpty(truck))
				filter.setTruckId(truck);

			if (!StringUtil.isEmpty(driver))
				filter.setDriverName(driver);
			data = readData();
		}else if (PROCUREMENT_CENTER.equalsIgnoreCase(type)) {
			//PMTDetail pmtD = new PMTDetail();
			filter.setStatusCode(PMT.Status.MTNT.ordinal());
			if(getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)){
			season=season!=null && !StringUtil.isEmpty(season)?season:getCurrentSeasonsCode();
			filter.setSeasonCode(season);
			}
			if (!StringUtil.isEmpty(selectedFieldStaff)) {
				filter.setMtntTransferInfo(new TransferInfo());
				filter.getMtntTransferInfo().setAgentId(selectedFieldStaff);
			}
			if (!StringUtil.isEmpty(cooperative)) {
				filter.setCoOperative(new Warehouse());
				filter.getCoOperative().setCode(cooperative);
			}
			if (!StringUtil.isEmpty(proCenter)) {
				filter.setCoOperative(new Warehouse());
				filter.getCoOperative().setCode(proCenter);
			}
			if (!StringUtil.isEmpty(ginning)) {
				filter.setGinningCode(ginning);
			}

			if (!StringUtil.isEmpty(truck))
				filter.setTruckId(truck);

			if (!StringUtil.isEmpty(driver))
				filter.setDriverName(driver);

			if (!StringUtil.isEmpty(transType)) {
				filter.setTrnType(transType);
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
		
			//pmtD.setPmt(filter);
			data = readData("productCenterList",filter);
		}
		if (PRODUCT_RECEIVE.equalsIgnoreCase(type)) {
			//PMTDetail pmtD = new PMTDetail();
			filter.setStatusCode(PMT.Status.MTNR.ordinal());
			season=season!=null && !StringUtil.isEmpty(season)?season:getCurrentSeasonsCode();
			filter.setSeasonCode(season);
			if (!StringUtil.isEmpty(selectedFieldStaff)) {
				filter.setMtnrTransferInfo(new TransferInfo());
				filter.getMtnrTransferInfo().setAgentId(selectedFieldStaff);
			}
			if (!StringUtil.isEmpty(cooperative)) {
				filter.setCoOperative(new Warehouse());
				filter.getCoOperative().setCode(cooperative);
			}
			if (!StringUtil.isEmpty(proCenter)) {
				filter.setCoOperative(new Warehouse());
				filter.getCoOperative().setCode(proCenter);
			}
			if (!StringUtil.isEmpty(ginning)) {
				filter.setGinningCode(ginning);
			}

			if (!StringUtil.isEmpty(truck))
				filter.setTruckId(truck);

			if (!StringUtil.isEmpty(driver))
				filter.setDriverName(driver);

			if (!StringUtil.isEmpty(transType)) {
				filter.setTrnType(transType);
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
		
			//pmtD.setPmt(filter);
			data = readData("productCenterList",filter);
		
			
			
		}
		/*
		 * if (!StringUtil.isEmpty(cooperative)) { filter.setCoOperative(new
		 * Warehouse());
		 * filter.getCoOperative().setId(Long.valueOf(cooperative)); }
		 */


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
	
		
		return sendJSONResponse(data);

	}

	/**
	 * @see com.sourcetrace.esesw.view.BaseReportAction#toJSON(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		type = request.getParameter("type");

		if (obj instanceof PMT) {
			PMT pmt = (PMT) obj;
			//JSONArray rows = new JSONArray();

			if (PMTNT.equals(type) || PROCUREMENT_CENTER.equalsIgnoreCase(type)) {
				/*if (getCurrentTenantId().equalsIgnoreCase("kpf") 
						|| getCurrentTenantId().equalsIgnoreCase(ESESystem.CABI_TENANT_ID)) */
			//	if (!getCurrentTenantId().equalsIgnoreCase("pratibha")){
				if(getIsKpfBased().equals("1")){
					rows.add(!ObjectUtil.isEmpty(pmt.getMtntDate()) ? (!StringUtil.isEmpty(pmt.getMtntDate())
							? DateUtil.getDateByDateTime(pmt.getMtntDate()) : "") : "");

					rows.add(!ObjectUtil.isEmpty(pmt.getMtntDate()) ? (!StringUtil.isEmpty(pmt.getMtntDate())
							? DateUtil.getMonthByDateTime(pmt.getMtntDate()) : "") : "");

					rows.add(!ObjectUtil.isEmpty(pmt.getMtntDate()) ? (!StringUtil.isEmpty(pmt.getMtntDate())
							? DateUtil.getYearByDateTime(pmt.getMtntDate()) : "") : "");

					rows.add(!StringUtil.isEmpty(pmt.getInvoiceNo()) ? pmt.getInvoiceNo() : "");
				}

			/*	if (!getCurrentTenantId().equalsIgnoreCase("kpf")
						&& !getCurrentTenantId().equalsIgnoreCase(ESESystem.CABI_TENANT_ID)) */
					if(!getIsKpfBased().equals("1")){
					if ((getIsMultiBranch().equalsIgnoreCase("1")
							&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

						if (StringUtil.isEmpty(branchIdValue)) {
							rows.add(!StringUtil
									.isEmpty(getBranchesMap().get(getParentBranchMap().get(pmt.getBranchId())))
											? getBranchesMap().get(getParentBranchMap().get(pmt.getBranchId()))
											: getBranchesMap().get(pmt.getBranchId()));
						}
						rows.add(getBranchesMap().get(pmt.getBranchId()));
					} else {
						if (StringUtil.isEmpty(branchIdValue)) {
							rows.add(branchesMap.get(pmt.getBranchId()));
						}
					}
					ESESystem preferences = preferncesService.findPrefernceById("1");
					if (!getCurrentTenantId().equalsIgnoreCase("pratibha")){
					if (!StringUtil.isEmpty(preferences)) {
						DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
						rows.add(StringUtil.isEmpty(pmt.getMtntDate()) ? "" : genDate.format(pmt.getMtntDate()));
					}

					if (!StringUtil.isEmpty(pmt.getSeasonCode())) {
						HarvestSeason season = farmerService.findSeasonNameByCode(pmt.getSeasonCode());
						rows.add((!ObjectUtil.isEmpty(season) ? season.getName() : " "));
					} else {
						rows.add("NA");
					}
					
					rows.add((!ObjectUtil.isEmpty(pmt.getMtntReceiptNumber()) ? pmt.getMtntReceiptNumber() : ""));			
					}
					if (!ObjectUtil.isEmpty(pmt.getMtntTransferInfo())) {
						//Agent agent=agentService.findAgentByAgentId(pmt.getMtntTransferInfo().getAgentId());
						if(!StringUtil.isEmpty(pmt.getMtntTransferInfo().getDeviceId()) && !StringUtil.isEmpty(pmt.getMtntTransferInfo().getDeviceName())){
						rows.add(!StringUtil.isEmpty(pmt.getMtntTransferInfo().getAgentName()) 
								? pmt.getMtntTransferInfo().getAgentName()+" - "+pmt.getMtntTransferInfo().getAgentId() : "NA");
						}
						else{
							rows.add(!StringUtil.isEmpty(pmt.getMtntTransferInfo().getAgentName()) 
									? pmt.getMtntTransferInfo().getAgentName()+" * ": "NA");
						}
					} else {
						rows.add("NA");
					}
					
					
				}
				if (!pmt.getTrnType().equalsIgnoreCase(PMT.TRN_TYPE_STOCK_TRNASFER)) {
					if (getCurrentTenantId().equalsIgnoreCase("lalteer")
							|| getCurrentTenantId().equalsIgnoreCase("kpf") || getCurrentTenantId().equalsIgnoreCase("iffco")|| getCurrentTenantId().equalsIgnoreCase("wub")
							|| getCurrentTenantId().equalsIgnoreCase(ESESystem.CABI_TENANT_ID)|| getCurrentTenantId().equalsIgnoreCase("gar") || getCurrentTenantId().equalsIgnoreCase("efk")|| getCurrentTenantId().equalsIgnoreCase("simfed") ) {
						rows.add(pmt.getCoOperative().getName());						
			
					} else {
						Vector<PMTDetail> pmtDetls = new Vector<PMTDetail>(pmt.getPmtDetails());
						if (pmtDetls.size() > 0 && !ObjectUtil.isEmpty(pmtDetls.get(0)) && !StringUtil.isEmpty(pmtDetls.get(0).getVillage()) ) {
							rows.add(pmtDetls.get(0).getVillage().getName());
						} else {
							rows.add("");
						}
						rows.add(pmt.getCoOperative().getName());
					}
					
					
				} else {
					rows.add(pmt.getCoOperative().getName() + "*");
					Vector<PMTDetail> pmtDetls = new Vector<PMTDetail>(pmt.getPmtDetails());
					if (pmtDetls.size() > 0 && !ObjectUtil.isEmpty(pmtDetls.get(0)) && !StringUtil.isEmpty(pmtDetls.get(0).getCoOperative()) ) {
						rows.add(pmtDetls.get(0).getCoOperative().getName());
					} else {
						rows.add("");
					}
				}
								
				// rows.add(pmt.getMtntReceiptNumber());
				rows.add(pmt.getTruckId());
				rows.add(pmt.getDriverName());

			/*	if (getCurrentTenantId().equalsIgnoreCase("kpf")
						|| getCurrentTenantId().equalsIgnoreCase(ESESystem.CABI_TENANT_ID)) */
					if(getIsKpfBased().equals("1")){
					if (!StringUtil.isEmpty(pmt.getClient())) {
						MasterData masterData = farmerService.findMasterDataIdByCode(pmt.getClient());

						rows.add(!ObjectUtil.isEmpty(masterData) ? masterData.getName() : "");
					} else {
						rows.add("");
					}

				}

				Map<String, Object> mtntDetails =getTotalMTNTProductDetails(pmt);
				if(!getCurrentTenantId().equals(ESESystem.PRATIBHA_TENANT_ID)){
				rows.add(mtntDetails.get(NO_OF_BAGS));
				}
				rows.add(CurrencyUtil.getDecimalFormat((Double) mtntDetails.get(GROSS_WEIGHT), "##.00"));
				/*if (getCurrentTenantId().equalsIgnoreCase("kpf") 
						|| getCurrentTenantId().equalsIgnoreCase(ESESystem.CABI_TENANT_ID))*/ 
					if(getIsKpfBased().equals("1")){
					rows.add(!StringUtil.isEmpty(mtntDetails.get(SUB_TOTAL)) ? CurrencyUtil.getDecimalFormat(Double.parseDouble(mtntDetails.get(SUB_TOTAL).toString()), "##.00") : "0.00");
					rows.add(!ObjectUtil.isEmpty(pmt.getTotalLabourCost())
							? CurrencyUtil.getDecimalFormat(Double.parseDouble(pmt.getTotalLabourCost().toString()), "##.00"): "0.00");
					rows.add(!ObjectUtil.isEmpty(pmt.getTransportCost())
							? CurrencyUtil.thousandSeparator(pmt.getTransportCost()) : "0.00");
					rows.add(!ObjectUtil.isEmpty(pmt.getTotalAmt()) ? CurrencyUtil.thousandSeparator(pmt.getTotalAmt())
							: "0.00");

				}
				/*
				 * if (pmt.getTrnType() == null) {
				 * rows.add(getText("transType.other")); } else {
				 * rows.add(pmt.getTrnType().equalsIgnoreCase(PMT.
				 * TRN_TYPE_STOCK_TRNASFER) ?
				 * getText("transType.productTransfer") :
				 * getText("transType.other")); }
				 */

			/*}else{
				
				if ((getIsMultiBranch().equalsIgnoreCase("1")
						&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

					if (StringUtil.isEmpty(branchIdValue)) {
						rows.add(!StringUtil
								.isEmpty(getBranchesMap().get(getParentBranchMap().get(pmt.getBranchId())))
										? getBranchesMap().get(getParentBranchMap().get(pmt.getBranchId()))
										: getBranchesMap().get(pmt.getBranchId()));
					}
					rows.add(getBranchesMap().get(pmt.getBranchId()));
				} else {
					if (StringUtil.isEmpty(branchIdValue)) {
						rows.add(branchesMap.get(pmt.getBranchId()));
					}
				}
				
				if (!pmt.getTrnType().equalsIgnoreCase(PMT.TRN_TYPE_STOCK_TRNASFER)) {
					if (getCurrentTenantId().equalsIgnoreCase("lalteer")
							|| getCurrentTenantId().equalsIgnoreCase("kpf")
							|| getCurrentTenantId().equalsIgnoreCase(ESESystem.CABI_TENANT_ID)|| getCurrentTenantId().equalsIgnoreCase("gar") || getCurrentTenantId().equalsIgnoreCase("efk")) {
						rows.add(pmt.getCoOperative().getName());
					} else {
						Vector<PMTDetail> pmtDetls = new Vector<PMTDetail>(pmt.getPmtDetails());
						if (pmtDetls.size() > 0 && !ObjectUtil.isEmpty(pmtDetls.get(0)) && !StringUtil.isEmpty(pmtDetls.get(0).getVillage()) ) {
							rows.add(pmtDetls.get(0).getVillage().getName());
						} else {
							rows.add("");
						}
					}
				} else {
					rows.add(pmt.getCoOperative().getName() + "*");
				}
				// rows.add(pmt.getMtntReceiptNumber());
				rows.add(pmt.getTruckId());
				rows.add(pmt.getDriverName());
				Map<String, Object> mtntDetails = getTotalMTNTProductDetails(pmt);
				rows.add(mtntDetails.get(NO_OF_BAGS));
				rows.add(CurrencyUtil.getDecimalFormat((Double) mtntDetails.get(GROSS_WEIGHT), "##.000"));
			   }*/
			}
			if (PMTNR.equals(type)) {
				if(getIsKpfBased().equals("1")){
					rows.add(!ObjectUtil.isEmpty(pmt.getMtnrDate()) ? (!StringUtil.isEmpty(pmt.getMtnrDate())
							? DateUtil.getDateByDateTime(pmt.getMtnrDate()) : "") : "");

					rows.add(!ObjectUtil.isEmpty(pmt.getMtnrDate()) ? (!StringUtil.isEmpty(pmt.getMtnrDate())
							? DateUtil.getMonthByDateTime(pmt.getMtnrDate()) : "") : "");

					rows.add(!ObjectUtil.isEmpty(pmt.getMtnrDate()) ? (!StringUtil.isEmpty(pmt.getMtnrDate())
							? DateUtil.getYearByDateTime(pmt.getMtnrDate()) : "") : "");
					rows.add(pmt.getInvoiceNo());
					
					}
				
				if(!getIsKpfBased().equals("1")){
				if ((getIsMultiBranch().equalsIgnoreCase("1")
						&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

					if (StringUtil.isEmpty(branchIdValue)) {
						rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(pmt.getBranchId())))
								? getBranchesMap().get(getParentBranchMap().get(pmt.getBranchId()))
								: getBranchesMap().get(pmt.getBranchId()));
					}
					rows.add(getBranchesMap().get(pmt.getBranchId()));
				} else {
					if (StringUtil.isEmpty(branchIdValue)) {
						rows.add(branchesMap.get(pmt.getBranchId()));
					}
				}
				ESESystem preferences = preferncesService.findPrefernceById("1");
				if (!getCurrentTenantId().equalsIgnoreCase("pratibha")){
				if (!StringUtil.isEmpty(preferences)) {
					DateFormat genDate = new SimpleDateFormat(
							preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
					rows.add(genDate.format(pmt.getMtnrDate()));
				}
				
				if (!StringUtil.isEmpty(pmt.getSeasonCode())) {
					HarvestSeason season = farmerService.findSeasonNameByCode(pmt.getSeasonCode());
					/*
					 * rows.add((!ObjectUtil.isEmpty(season) ? season.getName()
					 * + "-" + season.getCode() : " "));
					 */
					rows.add((season.getName()));
				} else {
					rows.add("NA");
				}
				rows.add((!ObjectUtil.isEmpty(pmt.getMtntReceiptNumber()) ? pmt.getMtntReceiptNumber() : ""));			
				rows.add((!ObjectUtil.isEmpty(pmt.getMtnrReceiptNumber()) ? pmt.getMtnrReceiptNumber() : ""));			
				
				}}
				if (!getCurrentTenantId().equalsIgnoreCase("pratibha")){
				if (pmt.getTrnType().equalsIgnoreCase(PMT.TRN_TYPE_STOCK_TRNASFER)) {
					Vector<PMTDetail> pmtDetls = new Vector<PMTDetail>(pmt.getPmtDetails());
					if (pmtDetls.size() > 0) {
						// rows.add(pmtDetls.get(0).getCoOperative().getName());
						rows.add(pmt.getCoOperative().getName());
					} else {
						if(!getCurrentTenantId().equalsIgnoreCase("iffco")){
						rows.add("");
						}
					}
				} 
				else {
					if(!getCurrentTenantId().equalsIgnoreCase("iffco") && !getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
					rows.add(pmt.getCoOperative().getName());
					}
				}
				
				Vector<PMTDetail> pmtDetls = new Vector<PMTDetail>(pmt.getPmtDetails());
				if (pmtDetls.size() > 0) {
				rows.add(pmtDetls.get(0).getCoOperative().getName());
				}
				}
		
				//rows.add(pmt.getCoOperative().getName());
				/* rows.add(pmt.getMtnrReceiptNumber()); */
				rows.add(pmt.getTruckId());
				rows.add(pmt.getDriverName());
				
				if(getIsKpfBased().equals("1")){
				rows.add("");	
				}	
				
				if (getCurrentTenantId().equalsIgnoreCase("wub")) {
				if (!StringUtil.isEmpty(pmt.getClient())) {
					MasterData masterData = farmerService.findMasterDataIdByCode(pmt.getClient());

					rows.add(!ObjectUtil.isEmpty(masterData) ? masterData.getName() : "");
				} else {
					rows.add("");
				}

			}
				Map<String, Object> mtntDetails = getTotalMTNTDetail(pmt.getMtntReceiptNumber());
				if (!getCurrentTenantId().equalsIgnoreCase("pratibha")){
				rows.add(CurrencyUtil.getDecimalFormat((Double) mtntDetails.get(GROSS_WEIGHT), "##.00"));}
				
				Map<String, Object> mtnrDetails = getTotalMTNRProductDetails(pmt);
				rows.add(CurrencyUtil.getDecimalFormat((Double) mtnrDetails.get(GROSS_WEIGHT), "##.00"));
				
				if (!getCurrentTenantId().equalsIgnoreCase("pratibha")){
				Double transporationLoss=0.0;
				transporationLoss= (Double)mtntDetails.get(GROSS_WEIGHT)-(Double)mtnrDetails.get(GROSS_WEIGHT);
				rows.add(CurrencyUtil.getDecimalFormat(transporationLoss, "##.00"));
				}
				/*
				 * if (pmt.getTrnType() == null) {
				 * rows.add(getText("transType.other")); } else { rows
				 * .add(pmt.getTrnType().equalsIgnoreCase(PMT.
				 * TRN_TYPE_STOCK_TRNASFER) ?
				 * getText("transType.productTransfer") :
				 * getText("transType.other")); }
				 */
			}
			/*
			 * if(PROCUREMENT_CENTER.equalsIgnoreCase(type)){
			 * 
			 * rows.add("Date"); rows.add("Season"); rows.add("TruckId");
			 * rows.add("Driver"); rows.add("Tot Bags");
			 * rows.add("Gross Weight"); }
			 */
			jsonObject.put("id", pmt.getId());
			jsonObject.put("cell", rows);

		} else if (obj instanceof PMTDetail) {
			PMTDetail pmtDetail = (PMTDetail) obj;
			//JSONArray rows = new JSONArray();
			/*if (getCurrentTenantId().equalsIgnoreCase("kpf") 
					|| getCurrentTenantId().equalsIgnoreCase(ESESystem.CABI_TENANT_ID))*/
				/*if(getIsKpfBased().equals("1")){
				rows.add(serialNo);
			}*/
			if (getCurrentTenantId().equalsIgnoreCase("lalteer")) {
				if (pmtDetail.getFarmer() != null) {
					rows.add(pmtDetail.getFarmer().getName() + " - " + pmtDetail.getFarmer().getFarmerCode());
				}
			}
			rows.add(pmtDetail.getProcurementProduct().getName());
			rows.add(pmtDetail.getProcurementProduct().getUnit());
			rows.add(pmtDetail.getProcurementGrade().getProcurementVariety().getName());
			if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {
				rows.add(pmtDetail.getProcurementGrade().getName());
			}

			/*if (getCurrentTenantId().equalsIgnoreCase("kpf")
					|| getCurrentTenantId().equalsIgnoreCase(ESESystem.CABI_TENANT_ID) )*/ 
			if (!getCurrentTenantId().equalsIgnoreCase("wub")){
				if(getIsKpfBased().equals("1")){
				if (!StringUtil.isEmpty(pmtDetail.getUom())) {
					FarmCatalogue cat = catalogueService.findCatalogueByCode(pmtDetail.getUom());
					rows.add(!ObjectUtil.isEmpty(cat) ? cat.getName() : "");

				} else {
					rows.add("");
				}
				

			}}else{
				rows.add(!ObjectUtil.isEmpty(pmtDetail.getProcurementProduct()) ? pmtDetail.getProcurementProduct().getUnit() : "");
			}
			/*
			 * if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {
			 * rows.add(pmtDetail.getProcurementProduct().getUnit()); }
			 */

				/*if(!getIsKpfBased().equals("1")&& !getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
				if (pmtDetail.getPmt().getTrnType() != null
						&& pmtDetail.getPmt().getTrnType().equalsIgnoreCase(PMT.TRN_TYPE_STOCK_TRNASFER)) {
					rows.add(ObjectUtil.isEmpty(pmtDetail.getCoOperative()) ? ""
							: (StringUtil.isEmpty(pmtDetail.getCoOperative().getName()) ? ""
									: pmtDetail.getCoOperative().getName() + " *"));
				} else {
					
					 * if(pmtDetail.getVillage()==null){
					 * rows.add(ObjectUtil.isEmpty(pmtDetail.getCoOperative()) ?
					 * "" : (StringUtil
					 * .isEmpty(pmtDetail.getCoOperative().getName()) ? "" :
					 * pmtDetail.getCoOperative() .getName()+" *")); }else{
					 * rows.add(ObjectUtil.isEmpty(pmtDetail.getVillage()) ? ""
					 * : (StringUtil .isEmpty(pmtDetail.getVillage().getName())
					 * ? "" : pmtDetail.getVillage() .getName())); }
					 
					if (!ObjectUtil.isEmpty(pmtDetail.getPmt())) {
						rows.add(pmtDetail.getCoOperative().getName() + " *");
					}
				}
			}*/
			if (PMTNT.equals(type)) {			
				if(!getCurrentTenantId().equals(ESESystem.PRATIBHA_TENANT_ID  )){
				rows.add(pmtDetail.getMtntNumberOfBags());
				}
				rows.add(CurrencyUtil.getDecimalFormat(pmtDetail.getMtntGrossWeight(), "##.00"));
				/*if (getCurrentTenantId().equalsIgnoreCase("kpf")
						|| getCurrentTenantId().equalsIgnoreCase(ESESystem.CABI_TENANT_ID))*/
					if(getIsKpfBased().equals("1")){
					rows.add(!StringUtil.isEmpty(pmtDetail.getPricePerUnit()) ? pmtDetail.getPricePerUnit() : "0.00");
					rows.add(!StringUtil.isEmpty(pmtDetail.getSubTotal()) ? pmtDetail.getSubTotal() : "0.00");

				}
			}
			if (PMTNR.equals(type)) {
				Map<String, Object> mtntDetails = getTotalMTNTDetails(pmtDetail.getPmt().getMtntReceiptNumber(),pmtDetail.getProcurementGrade().getId());
				if(!getCurrentTenantId().equals(ESESystem.PRATIBHA_TENANT_ID)){
				rows.add(mtntDetails.get(NO_OF_BAGS));				
				rows.add(CurrencyUtil.getDecimalFormat((Double) mtntDetails.get(GROSS_WEIGHT), "##.00"));
				}
				if(!getCurrentTenantId().equals(ESESystem.PRATIBHA_TENANT_ID)){
				rows.add(pmtDetail.getMtnrNumberOfBags());
				}
				rows.add(CurrencyUtil.getDecimalFormat(pmtDetail.getMtnrGrossWeight(), "##.00"));
			}
			if (pmtDetail.getPmt().getTrnType() == null) {
				rows.add(getText("transType.other"));
			} else {
				rows.add(pmtDetail.getPmt().getTrnType().equalsIgnoreCase(PMT.TRN_TYPE_STOCK_TRNASFER)
						? getText("transType.productTransfer") : getText("transType.other"));
			}

			jsonObject.put("id", pmtDetail.getId());
			jsonObject.put("cell", rows);
			serialNo++;
		} else {
			Object[] data = (Object[]) obj;
			
			String date = "";
			if (PROCUREMENT_CENTER.equals(type)) {
				if(getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)){
				if ((getIsMultiBranch().equalsIgnoreCase("1")
						&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

					if (StringUtil.isEmpty(branchIdValue)) {
						rows.add(!ObjectUtil.isEmpty(data[3]) && !StringUtil
								.isEmpty(getBranchesMap().get(getParentBranchMap().get(data[3].toString())))
										? getBranchesMap().get(getParentBranchMap().get(data[3].toString()))
										: getBranchesMap().get(data[3].toString()));
					}
					rows.add(!ObjectUtil.isEmpty(data[3]) ? getBranchesMap().get(data[3].toString()) : "");
				} else {
					if (StringUtil.isEmpty(branchIdValue)) {
						rows.add(!ObjectUtil.isEmpty(data[3]) ? branchesMap.get(data[3].toString()) : "");
					}
				}
				//if (PROCUREMENT_CENTER.equalsIgnoreCase(type))
					if (!ObjectUtil.isEmpty(data[4])) {
						ESESystem preferences = preferncesService.findPrefernceById("1");
						if (!StringUtil.isEmpty(preferences)) {
							DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
							rows.add(genDate.format(data[4]));
						}
					}
				rows.add(!ObjectUtil.isEmpty(data[18]) ? farmerService.findSeasonNameByCode(data[18].toString()).getName():"");
				rows.add(!ObjectUtil.isEmpty(data[13]) ? data[13].toString() : "");
				rows.add(!ObjectUtil.isEmpty(data[16]) ? data[16].toString():"");
				rows.add(!ObjectUtil.isEmpty(data[5]) ? data[5].toString() : "");
				rows.add(!ObjectUtil.isEmpty(data[6]) ? data[6].toString() : "");
			
				rows.add(!ObjectUtil.isEmpty(data[11]) ? data[11].toString() : "");
				rows.add(!ObjectUtil.isEmpty(data[10]) ? data[10].toString() : "");
				//rows.add(!ObjectUtil.isEmpty(data[1]) ? getCatlogueValueByCode(data[1].toString()).getName() : "");
				List<PMTFarmerDetail> pmtfmr=productDistributionService.findPmtFarmerDetailByPmtId(Long.parseLong(data[0].toString()));
				String prods= pmtfmr!=null ? pmtfmr.stream().map(e -> e.getProcurementProduct().getName()).distinct().reduce("",
						(a, b) -> a +","+ b) : null;
				rows.add(!StringUtil.isEmpty(prods) ? prods.substring(1): "");
				/*rows.add(!ObjectUtil.isEmpty(data[10]) ? data[10].toString() : "");
				rows.add(!ObjectUtil.isEmpty(data[8]) ? data[8].toString() : "");*/
				rows.add(!ObjectUtil.isEmpty(data[8]) ? data[8].toString() : "");
				rows.add(!ObjectUtil.isEmpty(data[19]) ? data[19].toString() : "");
				rows.add(!ObjectUtil.isEmpty(data[23]) ? data[23].toString() : "");

				/*
				 * String ids
				 * =String.valueOf(data[0])+"#"+String.valueOf(data[14]); rows.
				 * add("<button class=\"fa fa-pencil-square-o\"\"aria-hidden=\"true\"\" onclick=\"popupFarmerData('"
				 * + ids + "')\"></button>");
				 */

				String ids = String.valueOf(data[0]);
				rows.add("<button class=\"fa fa-pencil-square-o\"\"aria-hidden=\"true\"\" onclick=\"popupFarmerData('"
						+ ids + "','"+data[12]+"','"+data[1].toString()+"')\"></button>");
			}else{
				if ((getIsMultiBranch().equalsIgnoreCase("1")
						&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

					if (StringUtil.isEmpty(branchIdValue)) {
						rows.add(!ObjectUtil.isEmpty(data[3]) && !StringUtil
								.isEmpty(getBranchesMap().get(getParentBranchMap().get(data[3].toString())))
										? getBranchesMap().get(getParentBranchMap().get(data[3].toString()))
										: getBranchesMap().get(data[3].toString()));
					}
					rows.add(!ObjectUtil.isEmpty(data[3]) ? getBranchesMap().get(data[3].toString()) : "");
				} else {
					if (StringUtil.isEmpty(branchIdValue)) {
						rows.add(!ObjectUtil.isEmpty(data[3]) ? branchesMap.get(data[3].toString()) : "");
					}
				}
				if (!ObjectUtil.isEmpty(data[4])) {
					ESESystem preferences = preferncesService.findPrefernceById("1");
					if (!StringUtil.isEmpty(preferences)) {
						DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
						rows.add(genDate.format(data[4]));
					}
				}
				rows.add(!ObjectUtil.isEmpty(data[13]) ? data[13].toString() : "");
				rows.add(!ObjectUtil.isEmpty(data[8]) ? data[8].toString() : "");
				rows.add(!ObjectUtil.isEmpty(data[11]) ? data[11].toString() : "");
				rows.add(!ObjectUtil.isEmpty(data[5]) ? data[5].toString() : "");
				rows.add(!ObjectUtil.isEmpty(data[6]) ? data[6].toString() : "");
				rows.add(!ObjectUtil.isEmpty(data[10]) ? data[10].toString() : "");
				List<PMTFarmerDetail> pmtfmr=productDistributionService.findPmtFarmerDetailByPmtId(Long.parseLong(data[0].toString()));
				String prods= pmtfmr!=null ? pmtfmr.stream().map(e -> e.getProcurementProduct().getName()).distinct().reduce("",
						(a, b) -> a +","+ b) : null;
				rows.add(!StringUtil.isEmpty(prods) ? prods.substring(1): "");
				String variety=pmtfmr!=null ?pmtfmr.stream().map(e-> e.getProcurementGrade().getProcurementVariety().getName()).distinct().reduce("", (a,b)->a+","+b):null;
				rows.add(variety!=null && !StringUtil.isEmpty(variety)?variety.substring(1):"");
				String grade=pmtfmr!=null ?pmtfmr.stream().map(e-> e.getProcurementGrade().getName()).distinct().reduce("", (a,b)->a+","+b):null;
				rows.add(grade!=null && !StringUtil.isEmpty(grade)?grade.substring(1):"");
				rows.add(!ObjectUtil.isEmpty(data[20]) ? data[20].toString() : "");
				String ids = String.valueOf(data[0]);
				if(getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)){
				rows.add("<button class=\"fa fa-pencil-square-o\"\"aria-hidden=\"true\"\" onclick=\"popupFarmerData('"
						+ ids + "','"+data[12]+"','"+data[1].toString()+"')\"></button>");
				}else{
					rows.add("<button class=\"fa fa-pencil-square-o\"\"aria-hidden=\"true\"\" onclick=\"popupFarmerData('"
							+ ids + "','"+data[12]+"')\"></button>");
				}
			}
			}

			else if (PRODUCT_RECEIVE.equals(type)) {
				if(getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)){
				if ((getIsMultiBranch().equalsIgnoreCase("1")
						&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

					if (StringUtil.isEmpty(branchIdValue)) {
						rows.add(!ObjectUtil.isEmpty(data[3]) && !StringUtil
								.isEmpty(getBranchesMap().get(getParentBranchMap().get(data[3].toString())))
										? getBranchesMap().get(getParentBranchMap().get(data[3].toString()))
										: getBranchesMap().get(data[3].toString()));
					}
					rows.add(!ObjectUtil.isEmpty(data[3]) ? getBranchesMap().get(data[3].toString()) : "");
				} else {
					if (StringUtil.isEmpty(branchIdValue)) {
						rows.add(!ObjectUtil.isEmpty(data[3]) ? branchesMap.get(data[3].toString()) : "");
					}
				}
				//rows.add(!ObjectUtil.isEmpty(data[4]) ? data[4].toString() : "");
				if (!ObjectUtil.isEmpty(data[9])) {
					ESESystem preferences = preferncesService.findPrefernceById("1");
					if (!StringUtil.isEmpty(preferences)) {
						DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
						rows.add(genDate.format(data[9]));
					}
				}
				rows.add(!ObjectUtil.isEmpty(data[18]) ? farmerService.findSeasonNameByCode(data[18].toString()).getName():"");
				rows.add(!ObjectUtil.isEmpty(data[13]) ? data[13].toString() : "");
				rows.add(!ObjectUtil.isEmpty(data[14]) ? data[14].toString() : "");
				rows.add(!ObjectUtil.isEmpty(data[17]) ? data[17].toString():"");
				rows.add(!ObjectUtil.isEmpty(data[6]) ? data[6].toString() : "");
				rows.add(!ObjectUtil.isEmpty(data[5]) ? data[5].toString() : "");
				rows.add(!ObjectUtil.isEmpty(data[11]) ? data[11].toString() : "");
				rows.add(!ObjectUtil.isEmpty(data[10]) ? data[10].toString() : "");
				//rows.add(!ObjectUtil.isEmpty(data[1]) ? getCatlogueValueByCode(data[1].toString()).getName() : "");
				//rows.add(!ObjectUtil.isEmpty(data[7]) ? data[7].toString() : "");
				List<PMTDetail> pmtfmr=productDistributionService.findpmtdetailByPmtId(Long.parseLong(data[0].toString()));
				productName=null;
				if(pmtfmr!=null && !ObjectUtil.isListEmpty(pmtfmr)){
					pmtfmr.stream().filter(pm-> pm.getProcurementProduct()!=null && !ObjectUtil.isEmpty(pm.getProcurementProduct())).forEach(p->{
						productName=productName!=null && !StringUtil.isEmpty(productName)?!productName.contains(p.getProcurementProduct().getName())?productName+p.getProcurementProduct().getName()+",":productName:p.getProcurementProduct().getName()+",";
					});
				}
				rows.add(productName!=null && !StringUtil.isEmpty(productName) ? StringUtil.removeLastComma(productName.trim()): "");
				/*rows.add(!ObjectUtil.isEmpty(data[10]) ? data[10].toString() : "");
				rows.add(!ObjectUtil.isEmpty(data[8]) ? data[8].toString() : "");*/
				rows.add(!ObjectUtil.isEmpty(data[8]) ? data[8].toString() : "");
				//rows.add(!ObjectUtil.isEmpty(data[15]) ? getCatlogueValueByCode(data[15].toString()).getName() : "");
				rows.add(!ObjectUtil.isEmpty(data[19]) ? data[19].toString() : "");
				rows.add(!ObjectUtil.isEmpty(data[23]) ? data[23].toString() : "");
				rows.add(!ObjectUtil.isEmpty(data[21]) ? data[21].toString() : "");
				rows.add(!ObjectUtil.isEmpty(data[22]) ? data[22].toString() : "");
				Double transWeit = !ObjectUtil.isEmpty(data[23]) ? Double.parseDouble(data[23].toString()) : 0.0;
				Double recvWeit = !ObjectUtil.isEmpty(data[22]) ? Double.parseDouble(data[22].toString()) : 0.0;
				
				rows.add(String.valueOf((transWeit - recvWeit) >= 0 ?  CurrencyUtil.getDecimalFormat((transWeit - recvWeit), "#.00") : 0));
				if (!ObjectUtil.isEmpty(data[0])) {
					imagesId = "";
					List<Object> imgDetId = productDistributionService
							.listPMTImageDetailsIdByPmtId(Long.parseLong(data[0].toString()));
					if (!ObjectUtil.isListEmpty(imgDetId)) {
						imgDetId.stream().forEach(imgsId -> {
							imagesId += String.valueOf(imgsId) + ",";
						});
						/*
						 * sensitizing.getSentizingImages().stream().forEach(
						 * sensitizingImg -> { imagesId +=
						 * String.valueOf(sensitizingImg.getId()) + ","; });
						 */
						rows.add("<button class=\"fa fa-picture-o\"\"aria-hidden=\"true\"\" onclick=\"popupWindow('"
								+ StringUtil.removeLastComma(imagesId) + "')\"></button>");
						
					}
					 else {
							rows.add("<button class='no-imgIcn'></button>");
						}
					rows.add("<button class=\"fa fa-pencil-square-o\"\"aria-hidden=\"true\"\" onclick=\"detailPopup('"
							+ data[0] + "')\"></button>");
				
				}
			}
			else{
				if ((getIsMultiBranch().equalsIgnoreCase("1")
						&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

					if (StringUtil.isEmpty(branchIdValue)) {
						rows.add(!ObjectUtil.isEmpty(data[3]) && !StringUtil
								.isEmpty(getBranchesMap().get(getParentBranchMap().get(data[3].toString())))
										? getBranchesMap().get(getParentBranchMap().get(data[3].toString()))
										: getBranchesMap().get(data[3].toString()));
					}
					rows.add(!ObjectUtil.isEmpty(data[3]) ? getBranchesMap().get(data[3].toString()) : "");
				} else {
					if (StringUtil.isEmpty(branchIdValue)) {
						rows.add(!ObjectUtil.isEmpty(data[3]) ? branchesMap.get(data[3].toString()) : "");
					}
				}
				rows.add(!ObjectUtil.isEmpty(data[5]) ? data[5].toString() : "");
				rows.add(!ObjectUtil.isEmpty(data[14]) ? data[14].toString() : "");
				if (!ObjectUtil.isEmpty(data[9])) {
					ESESystem preferences = preferncesService.findPrefernceById("1");
					if (!StringUtil.isEmpty(preferences)) {
						DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
						rows.add(genDate.format(data[9]));
					}
				}
				rows.add(!ObjectUtil.isEmpty(data[8]) ? data[8].toString() : "");
				rows.add(!ObjectUtil.isEmpty(data[6]) ? data[6].toString() : "");
				//rows.add(!ObjectUtil.isEmpty(data[7]) ? data[7].toString() : "");
				rows.add(!ObjectUtil.isEmpty(data[20]) ? data[20].toString() : "");
				rows.add(!ObjectUtil.isEmpty(data[22]) ? data[22].toString() : "");
				double transQty=data[20]!=null && !ObjectUtil.isEmpty(data[20])?Double.parseDouble(data[20].toString()):0.0;
				double recevQty=data[22]!=null && !ObjectUtil.isEmpty(data[22])?Double.parseDouble(data[22].toString()):0.0;
				rows.add(transQty-recevQty);
				rows.add("<button class=\"fa fa-pencil-square-o\"\"aria-hidden=\"true\"\" onclick=\"FarmerDetailsPopup('"
						+ data[13] + "')\"></button>");
			}
			}

			jsonObject.put("id", data[0]);
			jsonObject.put("cell", rows);
		}
		return jsonObject;
	}

	private Map<String, Object> getTotalMTNTDetail(String receiptNo) {
		// TODO Auto-generated method stub
		PMT pm = productDistributionService.findPMTByReceiptNumber(receiptNo);
		Map<String, Object> mtntDetails = new HashMap<String, Object>();		
		if (!ObjectUtil.isEmpty(pm) && !ObjectUtil.isEmpty(pm.getPmtDetails()) && pm.getPmtDetails()!=null) {
			for (PMTDetail detail : pm.getPmtDetails()) {
				if (detail.getStatus() == 3) {
					grossWt = grossWt + detail.getMtntGrossWeight();
				}
			}
		}
		mtntDetails.put(GROSS_WEIGHT, grossWt);
		return mtntDetails;
	}

	long noOfBags = 0;
	double grossWt = 0;
	private Map<String, Object> getTotalMTNTDetails(String receiptNo,Long gradeId) {
		
		PMT pmt = productDistributionService.findPMTByReceiptNumber(receiptNo,PMT.Status.MTNT.ordinal());
		Map<String, Object> mtntDetails = new HashMap<String, Object>();
		if(!ObjectUtil.isEmpty(pmt)){
		pmt.getPmtDetails().stream().filter(u->u.getStatus()==3&&u.getProcurementGrade().getId()==Long.valueOf(gradeId)).forEach(u->{
			noOfBags = u.getMtntNumberOfBags();
			grossWt =  u.getMtntGrossWeight();	
		});}
		mtntDetails.put(NO_OF_BAGS, noOfBags);
		mtntDetails.put(GROSS_WEIGHT, grossWt);
		return mtntDetails;
	}

	@SuppressWarnings("unchecked")
	public String populateDetailData(){
		try {
			setPmtId(pmtId);
			List<PMTDetail> pmtDatas=productDistributionService.findpmtdetailByPmtId(Long.parseLong(pmtId));
			List<JSONObject> jsonObjects=new ArrayList<JSONObject>();
			pmtDatas.stream().forEach(pmtdet -> {
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("Product", pmtdet.getProcurementProduct().getName());
				jsonObject.put("ICS", getCatlogueValueByCode(String.valueOf(pmtdet.getIcs())).getName());
				jsonObject.put("TransQty", pmtdet.getMtntQuintalWeight());
				jsonObject.put("RecQty", pmtdet.getMtnrGrossWeight());
				jsonObject.put("TransBag",pmtdet.getMtntNumberOfBags());
				jsonObject.put("RecBag",pmtdet.getMtnrNumberOfBags());
				jsonObject.put("Ginning",pmtdet.getPmt().getCoOperative().getName() );
				jsonObject.put("Warehouse",pmtdet.getCoOperative().getName() );
				jsonObject.put("Heap", getCatlogueValueByCode(String.valueOf(pmtdet.getHeap())).getName());
				jsonObjects.add(jsonObject);
			});
			printAjaxResponse(jsonObjects, "text/html");
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public String populateFarmerDetailData(){
		try {
			setPmtId(pmtId);
			PMT pmtDatas=productDistributionService.findPMTByReceiptNumber(mtntReceiptNumber);
			List<JSONObject> jsonObjects=new ArrayList<JSONObject>();
			pmtDatas.getPmtFarmerDetais().stream().forEach(pmtFarmerData -> {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("product", pmtFarmerData.getProcurementProduct().getName());
				jsonObject.put("grade", pmtFarmerData.getProcurementGrade().getName());
				jsonObject.put("variety", pmtFarmerData.getProcurementGrade().getProcurementVariety().getName());
				jsonObject.put("goodQty", pmtFarmerData.getMtntGrossWeight());
				
				farmerService.findFarmerDatasByFarmerID(Long.valueOf((String) pmtFarmerData.getFarmer() )).stream()
				.forEach(dataArr -> {
					jsonObject.put("Name", dataArr[3]+" "+dataArr[4]);
					jsonObject.put("farmerCode",dataArr[2]);
					jsonObject.put("Village_Name", dataArr[6]);
					jsonObject.put("City_Name", dataArr[5]);
					jsonObject.put("Group_Name", dataArr[7]);
				});
		jsonObjects.add(jsonObject);
			});
			printAjaxResponse(jsonObjects, "text/html");
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public String populateFarmerData() {
		try {

			setId(id);
			// String[] splitId = id.split("#");
			// List<Object[]> pmtDatas =
			// productDistributionService.findpmtdetailByPmtId(Long.valueOf(splitId[1]),Long.valueOf(splitId[0]));

			List<PMTFarmerDetail> pmtDatas = productDistributionService.findPmtFarmerDetailByPmtId(Long.parseLong(id));
			List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
			pmtDatas.stream().forEach(pmtFarmerData -> {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("noOfBags", pmtFarmerData.getMtntNumberOfBags());
				jsonObject.put("grossBags", pmtFarmerData.getMtntGrossWeight());
				jsonObject.put("product", pmtFarmerData.getProcurementProduct().getName());
				jsonObject.put("grade", pmtFarmerData.getProcurementGrade().getName());
				jsonObject.put("variety", pmtFarmerData.getProcurementGrade().getProcurementVariety().getName());
				farmerService.findFarmerDatasByFarmerID(Long.valueOf((String) pmtFarmerData.getFarmer() )).stream()
						.forEach(dataArr -> {
							jsonObject.put("First_Name", dataArr[3]);
							jsonObject.put("Last_Name", dataArr[4]);
							jsonObject.put("Village_Name", dataArr[6]);
							jsonObject.put("City_Name", dataArr[5]);
							jsonObject.put("Group_Name", dataArr[7]);
							String icsName = "";
							icsName += getCatlogueValueByCode(String.valueOf(dataArr[8])).getName();
							jsonObject.put("Ics_Name", icsName);
							String fpo = "";
							fpo += getCatlogueValueByCode(String.valueOf(dataArr[9])).getName();
						    jsonObject.put("fpo", fpo);
						    jsonObject.put("Farmer_Code", dataArr[2]);
						});
				jsonObjects.add(jsonObject);

			});

			printAjaxResponse(jsonObjects, "text/html");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param pmt
	 *            the pmt
	 * @return the total mtnt product details
	 */
	private Map<String, Object> getTotalMTNTProductDetails(PMT pmt) {

		long noOfBags = 0;
		double grossWt = 0;
		double subTotal = 0;
		Map<String, Object> mtntDetails = new HashMap<String, Object>();
		if (!ObjectUtil.isListEmpty(pmt.getPmtDetails())) {
			for (PMTDetail detail : pmt.getPmtDetails()) {
					noOfBags = noOfBags + detail.getMtntNumberOfBags();
					grossWt = grossWt + detail.getMtntGrossWeight();
					subTotal = subTotal + Double
							.parseDouble(!StringUtil.isEmpty(detail.getSubTotal()) ? detail.getSubTotal() : "0.00");
				
			}
		}
		mtntDetails.put(NO_OF_BAGS, noOfBags);
		mtntDetails.put(GROSS_WEIGHT, grossWt);
		mtntDetails.put(SUB_TOTAL, subTotal);
		return mtntDetails;
	}

	/**
	 * Gets the total mtnr product details.
	 * 
	 * @param pmt
	 *            the pmt
	 * @return the total mtnr product details
	 */
	private Map<String, Object> getTotalMTNRProductDetails(PMT pmt) {

		long noOfBags = 0;
		double grossWt = 0;
		Map<String, Object> mtnrDetails = new HashMap<String, Object>();
		if (!ObjectUtil.isListEmpty(pmt.getPmtDetails())) {
			for (PMTDetail detail : pmt.getPmtDetails()) {
				noOfBags = noOfBags + detail.getMtnrNumberOfBags();
				grossWt = grossWt + detail.getMtnrGrossWeight();
			}
		}
		mtnrDetails.put(NO_OF_BAGS, noOfBags);
		mtnrDetails.put(GROSS_WEIGHT, grossWt);
		return mtnrDetails;
	}

	private Map<String, Object> getTotalMTNTDetails(PMT pmt) {
		double grossWt = 0;
		Map<String, Object> mtntDetails = new HashMap<String, Object>();
		if (!ObjectUtil.isListEmpty(pmt.getPmtDetails())) {
			for (PMTDetail detail : pmt.getPmtDetails()) {
				if (detail.getStatus() == 3) {
					grossWt = grossWt + detail.getMtntGrossWeight();
				}
			}
		}
		mtntDetails.put(GROSS_WEIGHT, grossWt);
		return mtntDetails;
	}
	
	/**
	 * Sub list.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings("unchecked")
	public String subList() throws Exception {

		PMTDetail pmtDetail = new PMTDetail();
		PMT pmt = new PMT();
		pmt.setId(Long.valueOf(id));
		pmtDetail.setPmt(pmt);
		super.filter = pmtDetail;
		Map data = readData();
		if (getPage() > 1) {
			serialNo = (getPage() - 1) * 10 + 1;
		}
		return sendJSONResponse(data);

	}

	/**
	 * Load column limit.
	 */
	public void loadColumnLimit() {

		// disable unnecessary columns
		if ("pmtnr".equalsIgnoreCase(type)) {
			disableColumns = "villageName";
		} else if ("pmtnt".equalsIgnoreCase(type)) {
			// disableColumns = "mtnrTransferInfo.agentName";
		}
		try {
			response.setContentType("text/html");
			response.getWriter().write(disableColumns);
		} catch (Exception e) {

		}
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
	 * Gets the disable columns.
	 * 
	 * @return the disable columns
	 */
	public String getDisableColumns() {

		return disableColumns;
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
	 * Gets the receipt no.
	 * 
	 * @return the receipt no
	 */
	public String getReceiptNo() {

		return receiptNo;
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
	 * Gets the filter fields.
	 * 
	 * @return the filter fields
	 */
	public Map<String, String> getFilterFields() {

		return filterFields;
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
	 * Gets the filter.
	 * 
	 * @return the filter
	 */
	public PMT getFilter() {

		return filter;
	}

	/**
	 * Sets the filter.
	 * 
	 * @param filter
	 *            the new filter
	 */
	public void setFilter(PMT filter) {

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
	 * Gets the warehouse list.
	 * 
	 * @return the warehouse list
	 */
	public List<String> getWarehouseList() {

		List<Warehouse> returnValue = locationService.listWarehouse();
		for (Warehouse warehouse : returnValue) {
			warehouseList.add(warehouse.getName() + " - " + warehouse.getCode());
		}

		return warehouseList;
	}

	/**
	 * Sets the warehouse list.
	 * 
	 * @param warehouseList
	 *            the new warehouse list
	 */
	public void setWarehouseList(List<String> warehouseList) {

		this.warehouseList = warehouseList;
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
	 * Gets the sender area.
	 * 
	 * @return the sender area
	 */
	public String getSenderArea() {

		return senderArea;
	}

	/**
	 * Sets the sender area.
	 * 
	 * @param senderArea
	 *            the new sender area
	 */
	public void setSenderArea(String senderArea) {

		this.senderArea = senderArea;
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
	 * Gets the sender area list.
	 * 
	 * @return the sender area list
	 */
	public Map<String, String> getSenderAreaList() {

		List<Municipality> cities = productDistributionService.listPMTSenderCity();
		Map<String, String> cityList = new LinkedHashMap<String, String>();
		if (!ObjectUtil.isListEmpty(cities)) {
			for (Municipality city : cities) {
				cityList.put(String.valueOf(city.getId()), city.getName() + "-" + city.getCode());
			}
		}
		return cityList;
	}

	/**
	 * Gets the cooperative list.
	 * 
	 * @return the cooperative list
	 */
	/*
	 * public Map<String, String> getCooperativeList() {
	 * 
	 * List<Warehouse> cooperative = locationService.listOfCooperatives();
	 * Map<String, String> cooperativeList = new LinkedHashMap<String,
	 * String>(); if (!ObjectUtil.isListEmpty(cooperative)) { for (Warehouse
	 * warehouse : cooperative) {
	 * cooperativeList.put(String.valueOf(warehouse.getCode()),
	 * String.valueOf(warehouse.getName())); } } return cooperativeList; }
	 */
	public void populateCooperativeList() {
		JSONArray cooperativeArr = new JSONArray();
		List<Warehouse> cooperative = locationService.listOfCooperatives();
		if (!ObjectUtil.isEmpty(cooperative)) {
			cooperative.forEach(obj -> {
				cooperativeArr.add(getJSONObject(obj.getCode(), obj.getName()));
			});
		}
		sendAjaxResponse(cooperativeArr);
	}

	/*
	 * public Map<String,String> getCooperativeList(){ Map<String,String>
	 * warehouseMap = new LinkedHashMap<>(); List<Object[]> warehouseList =
	 * locationService.listOfWarehousePMT();
	 * if(!ObjectUtil.isEmpty(warehouseList)){
	 * 
	 * warehouseMap=warehouseList.stream().collect(Collectors.toMap(obj->(String
	 * .valueOf(obj[0])), obj->(String.valueOf(obj[0]) + " -" +
	 * String.valueOf(obj[1]))));
	 * 
	 * } return warehouseMap; }
	 */

	public Map<String, String> getTransTypeList() {

		Map<String, String> transTypeList = new LinkedHashMap<String, String>();
		transTypeList.put(PMT.TRN_TYPE_STOCK_TRNASFER, getText("transType.stockTransfer"));
		transTypeList.put(PMT.TRN_TYPE_OTEHR, getText("transType.other"));
		return transTypeList;
	}

	/**
	 * Gets the receiver city list.
	 * 
	 * @return the receiver city list
	 */
	public Map<String, String> getReceiverCityList() {

		List<Municipality> cities = productDistributionService.listPMTReceiverCities();
		Map<String, String> cityList = new LinkedHashMap<String, String>();
		if (!ObjectUtil.isListEmpty(cities)) {
			for (Municipality city : cities) {
				cityList.put(String.valueOf(city.getId()), city.getName() + "-" + city.getCode());
			}
		}
		return cityList;
	}

	/**
	 * Sets the sender city.
	 * 
	 * @param senderCity
	 *            the new sender city
	 */
	public void setSenderCity(String senderCity) {

		this.senderCity = senderCity;
	}

	/**
	 * Gets the sender city.
	 * 
	 * @return the sender city
	 */
	public String getSenderCity() {

		return senderCity;
	}

	/**
	 * Gets the truck.
	 * 
	 * @return the truck
	 */
	public String getTruck() {

		return truck;
	}

	/**
	 * Sets the truck.
	 * 
	 * @param truck
	 *            the new truck
	 */
	public void setTruck(String truck) {

		this.truck = truck;
	}

	/**
	 * Gets the driver.
	 * 
	 * @return the driver
	 */
	public String getDriver() {

		return driver;
	}

	/**
	 * Sets the driver.
	 * 
	 * @param driver
	 *            the new driver
	 */
	public void setDriver(String driver) {

		this.driver = driver;
	}

	/**
	 * Sets the sender city mtnr.
	 * 
	 * @param senderCityMTNR
	 *            the new sender city mtnr
	 */
	public void setSenderCityMTNR(String senderCityMTNR) {

		this.senderCityMTNR = senderCityMTNR;
	}

	/**
	 * Gets the sender city mtnr.
	 * 
	 * @return the sender city mtnr
	 */
	public String getSenderCityMTNR() {

		return senderCityMTNR;
	}

	/**
	 * Sets the mtnt receipt number.
	 * 
	 * @param mtntReceiptNumber
	 *            the new mtnt receipt number
	 */
	public void setMtntReceiptNumber(String mtntReceiptNumber) {

		this.mtntReceiptNumber = mtntReceiptNumber;
	}

	/**
	 * Gets the mtnt receipt number.
	 * 
	 * @return the mtnt receipt number
	 */
	public String getMtntReceiptNumber() {

		return mtntReceiptNumber;
	}

	/**
	 * Sets the cooperative.
	 * 
	 * @param cooperative
	 *            the new cooperative
	 */
	public void setCooperative(String cooperative) {

		this.cooperative = cooperative;
	}

	/**
	 * Gets the cooperative.
	 * 
	 * @return the cooperative
	 */
	public String getCooperative() {

		return cooperative;
	}

	/**
	 * Gets the mtnr receipt number.
	 * 
	 * @return the mtnr receipt number
	 */
	public String getMtnrReceiptNumber() {

		return mtnrReceiptNumber;
	}

	/**
	 * Sets the mtnr receipt number.
	 * 
	 * @param mtnrReceiptNumber
	 *            the new mtnr receipt number
	 */
	public void setMtnrReceiptNumber(String mtnrReceiptNumber) {

		this.mtnrReceiptNumber = mtnrReceiptNumber;
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
		setXlsFileName(getText("pmtReportList" + type) + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("pmtReportList" + type), fileMap, ".xls"));
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

		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		setsDate(IExporter.EXPORT_DAILY.equalsIgnoreCase(exportType) ? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));
		boolean flag = true;
		DateFormat filterDateFormat = new SimpleDateFormat(getGeneralDateFormat());

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("exportTitle" + type));
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
				filterRow6 = null, filterRow7 = null,filterRow8=null;
		HSSFCell cell;
		String receiptNumber = "";
		String fieldStaff="";
		int imgRow1 = 0;
		int imgRow2 = 4;
		int imgCol1 = 0;
		int imgCol2 = 0;
		int titleRow1 = 2;
		int titleRow2 = 5;

		int rowNum = 2;
		int colNum = 0;

		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, titleRow1, titleRow2));

		sheet.setDefaultColumnWidth(13);
		
		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(2);
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("exportTitle" + type)));
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

			//if (PMTNT.equals(type) && !StringUtil.isEmpty(mtntReceiptNumber)) {
			if (PMTNT.equals(type)) {
				//receiptNumber = mtntReceiptNumber;
				fieldStaff=selectedFieldStaff;
				
			} else if (PMTNR.equals(type) && !StringUtil.isEmpty(mtnrReceiptNumber)) {
				receiptNumber = mtnrReceiptNumber;
			}
			if (!StringUtil.isEmpty(receiptNumber)) {

				filterRow3 = sheet.createRow(rowNum++);

				cell = filterRow3.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("receiptNumber")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow3.createCell(2);
				cell.setCellValue(new HSSFRichTextString(receiptNumber));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			if (!StringUtil.isEmpty(fieldStaff)) {

				filterRow4 = sheet.createRow(rowNum++);

				cell = filterRow4.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("agentId")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow4.createCell(2);
				 Agent agent = agentService.findAgentByAgentId(selectedFieldStaff);
				 if(!ObjectUtil.isEmpty(agent))
					 cell.setCellValue(new HSSFRichTextString(agent.getProfileIdWithName()));
				 else
					 cell.setCellValue(new HSSFRichTextString(selectedFieldStaff));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			

			filterRow4 = sheet.createRow(rowNum++);
			filterRow5 = sheet.createRow(rowNum++);
			filterRow8 = sheet.createRow(rowNum);
			filterRow6 = sheet.createRow(rowNum++);
			filterRow7 = sheet.createRow(rowNum++);
			
		}
		rowNum++;
		branchIdValue = getBranchId();
		buildBranchMap();
		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		String mainGridColumnHeaders;
		if (StringUtil.isEmpty(branchIdValue)) {
			mainGridColumnHeaders = getLocaleProperty("exportColumnHeaderBranch" + type);
		} else {
			mainGridColumnHeaders = getLocaleProperty("exportColumnHeader" + type);
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

		setFilter(new PMT());
		Map data= new HashMap<>();
		/*if (PMTNT.equals(type)) {
			filter.setStatusCode(PMT.Status.MTNT.ordinal());

			if (!StringUtil.isEmpty(mtntReceiptNumber))
				filter.setMtntReceiptNumber(mtntReceiptNumber);
		}

		if (PMTNR.equals(type)) {
			filter.setStatusCode(PMT.Status.MTNR.ordinal());

			if (!StringUtil.isEmpty(mtnrReceiptNumber))
				filter.setMtnrReceiptNumber(mtnrReceiptNumber);
		}

		
		 * if (!StringUtil.isEmpty(cooperative)) { filter.setCoOperative(new
		 * Warehouse());
		 * filter.getCoOperative().setId(Long.valueOf(cooperative)); }
		 

		if (!StringUtil.isEmpty(cooperative)) {
			filter.setCoOperative(new Warehouse());
			filter.getCoOperative().setCode(cooperative);
		}
		if (!StringUtil.isEmpty(proCenter)) {
			filter.setCoOperative(new Warehouse());
			filter.getCoOperative().setCode(proCenter);
		}
		if (!StringUtil.isEmpty(ginning)) {
			filter.setGinningCode(ginning);
		}
		if (!StringUtil.isEmpty(truck))
			filter.setTruckId(truck);

		if (!StringUtil.isEmpty(driver))
			filter.setDriverName(driver);

		if (!StringUtil.isEmpty(transType))
			filter.setTrnType(transType);
		if (PROCUREMENT_CENTER.equalsIgnoreCase(type)) {
			filter.setStatusCode(PMT.Status.MTNT.ordinal());
		}
		if (PRODUCT_RECEIVE.equalsIgnoreCase(type)) {
			filter.setStatusCode(PMT.Status.MTNR.ordinal());
		}
		super.filter = this.filter;*/

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
	
		if (PMTNT.equals(type)) {
			filter.setStatusCode(PMT.Status.MTNT.ordinal());

			if (!StringUtil.isEmpty(mtntReceiptNumber))
				filter.setMtntReceiptNumber(mtntReceiptNumber);

			if (!StringUtil.isEmpty(season)) {
				filter.setSeasonCode(season);
			} 
			if (!StringUtil.isEmpty(selectedFieldStaff)) {
				filter.setMtntTransferInfo(new TransferInfo());
				filter.getMtntTransferInfo().setAgentId(selectedFieldStaff);
			}
			
			if (!StringUtil.isEmpty(cooperative)) {
				filter.setTrnType(PMT.TRN_TYPE_STOCK_TRNASFER);
				filter.setCoOperative(new Warehouse());
				filter.getCoOperative().setCode(cooperative);
			}
			if (!StringUtil.isEmpty(truck))
				filter.setTruckId(truck);

			if (!StringUtil.isEmpty(driver))
				filter.setDriverName(driver);
			
			
			data = readData();
		}else if (PMTNR.equals(type)) {
			filter.setStatusCode(PMT.Status.MTNR.ordinal());

			if (!StringUtil.isEmpty(mtnrReceiptNumber))
				filter.setMtnrReceiptNumber(mtnrReceiptNumber);

			if (!StringUtil.isEmpty(season)) {
				filter.setSeasonCode(season);
			} 
			if (!StringUtil.isEmpty(cooperative)) {
				filter.setCoOperative(new Warehouse());
				filter.getCoOperative().setCode(cooperative);
			}
			if (!StringUtil.isEmpty(truck))
				filter.setTruckId(truck);

			if (!StringUtil.isEmpty(driver))
				filter.setDriverName(driver);
			data = readData();
		}else if (PROCUREMENT_CENTER.equalsIgnoreCase(type)) {
			PMTDetail pmtD = new PMTDetail();
			filter.setStatusCode(PMT.Status.MTNT.ordinal());
			season=season!=null && !StringUtil.isEmpty(season)?season:getCurrentSeasonsCode();
			filter.setSeasonCode(season);

			if (!StringUtil.isEmpty(cooperative)) {
				filter.setCoOperative(new Warehouse());
				filter.getCoOperative().setCode(cooperative);
			}
			if (!StringUtil.isEmpty(proCenter)) {
				filter.setCoOperative(new Warehouse());
				filter.getCoOperative().setCode(proCenter);
			}
			if (!StringUtil.isEmpty(ginning)) {
				filter.setGinningCode(ginning);
			}

			if (!StringUtil.isEmpty(truck))
				filter.setTruckId(truck);

			if (!StringUtil.isEmpty(driver))
				filter.setDriverName(driver);

			if (!StringUtil.isEmpty(transType)) {
				filter.setTrnType(transType);
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
		
			//pmtD.setPmt(filter);
			data = readData("productCenterList",filter);
		}
		if (PRODUCT_RECEIVE.equalsIgnoreCase(type)) {
			PMTDetail pmtD = new PMTDetail();
			filter.setStatusCode(PMT.Status.MTNR.ordinal());
			season=season!=null && !StringUtil.isEmpty(season)?season:getCurrentSeasonsCode();
			filter.setSeasonCode(season);

			if (!StringUtil.isEmpty(cooperative)) {
				filter.setCoOperative(new Warehouse());
				filter.getCoOperative().setCode(cooperative);
			}
			if (!StringUtil.isEmpty(proCenter)) {
				filter.setCoOperative(new Warehouse());
				filter.getCoOperative().setCode(proCenter);
			}
			if (!StringUtil.isEmpty(ginning)) {
				filter.setGinningCode(ginning);
			}

			if (!StringUtil.isEmpty(truck))
				filter.setTruckId(truck);

			if (!StringUtil.isEmpty(driver))
				filter.setDriverName(driver);

			if (!StringUtil.isEmpty(transType)) {
				filter.setTrnType(transType);
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
		
			pmtD.setPmt(filter);
			data = readData("productCenterList",filter);
		
			
			
		}
		
				if (!StringUtil.isEmpty(truck)) {

					cell = filterRow6.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("truckId")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow6.createCell(2);
					cell.setCellValue(new HSSFRichTextString(truck));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

				}
				if (!StringUtil.isEmpty(driver)) {

					cell = filterRow7.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("driverName")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow7.createCell(2);
					cell.setCellValue(new HSSFRichTextString(driver));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

				}
				if (PROCUREMENT_CENTER.equalsIgnoreCase(type) || PRODUCT_RECEIVE.equalsIgnoreCase(type)) {
					if (!StringUtil.isEmpty(proCenter) || !StringUtil.isEmpty(truck) || !StringUtil.isEmpty(driver)
							|| !StringUtil.isEmpty(ginning) || !StringUtil.isEmpty(season)) {

						if (!StringUtil.isEmpty(proCenter)) {

							cell = filterRow4.createCell(1);
							cell.setCellValue(new HSSFRichTextString(getLocaleProperty("cooperative")));
							filterFont.setBoldweight((short) 12);
							filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
							filterStyle.setFont(filterFont);
							cell.setCellStyle(filterStyle);

							cell = filterRow4.createCell(2);
							cell.setCellValue(new HSSFRichTextString(locationService.findWarehouseByCode(proCenter).getName()));
							filterFont.setBoldweight((short) 12);
							filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
							filterStyle.setFont(filterFont);
							cell.setCellStyle(filterStyle);

						}

						if (!StringUtil.isEmpty(ginning)) {

							cell = filterRow5.createCell(1);
							cell.setCellValue(new HSSFRichTextString(getLocaleProperty("ginning")));
							filterFont.setBoldweight((short) 12);
							filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
							filterStyle.setFont(filterFont);
							cell.setCellStyle(filterStyle);

							cell = filterRow5.createCell(2);
							cell.setCellValue(new HSSFRichTextString(locationService.findWarehouseByCode(ginning).getName()));
							filterFont.setBoldweight((short) 12);
							filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
							filterStyle.setFont(filterFont);
							cell.setCellStyle(filterStyle);

						}
						if(!getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)){
						if (!StringUtil.isEmpty(season)) {
							cell = filterRow8.createCell(1);
							cell.setCellValue(new HSSFRichTextString(getLocaleProperty("season")));
							filterFont.setBoldweight((short) 12);
							filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
							filterStyle.setFont(filterFont);
							cell.setCellStyle(filterStyle);

								cell = filterRow8.createCell(2);
								 cell.setCellValue(new HSSFRichTextString(farmerService.findSeasonNameByCode(season).getName()));
								filterFont.setBoldweight((short) 12);
								filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
								filterStyle.setFont(filterFont);
								cell.setCellStyle(filterStyle);
						}
						}
							
			}
			ESESystem preferences = preferncesService.findPrefernceById("1");
			DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
			List<Object[]> dfata = (ArrayList) data.get(ROWS);
			for (Object obj[] : dfata) {
				row = sheet.createRow(rowNum++);
				colNum = 0;
				if (PROCUREMENT_CENTER.equals(type)) {
					if ((getIsMultiBranch().equalsIgnoreCase("1")
							&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

						if (StringUtil.isEmpty(branchIdValue)) {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[3]) && !StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(obj[3].toString())))
									? getBranchesMap().get(getParentBranchMap().get(obj[3].toString()))
									: getBranchesMap().get(obj[3].toString())));
						}
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[3]) ? getBranchesMap().get(obj[3].toString()) : ""));
					} else {
						if (StringUtil.isEmpty(branchIdValue)) {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[3]) ? branchesMap.get(obj[3].toString()) : ""));
						}
					}
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[4]) ? obj[4].toString() : ""));
					if(getCurrentTenantId().equalsIgnoreCase("chetna")){
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[18]) ? farmerService.findSeasonNameByCode(obj[18].toString()).getName() : ""));
					}
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[13]) ? obj[13].toString() : ""));
					if(getCurrentTenantId().equalsIgnoreCase("chetna")){
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[16]) ? obj[16].toString() : ""));
					}
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[5]) ? obj[5].toString() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[6]) ? obj[6].toString() : ""));
				
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[11]) ? obj[11].toString() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[10]) ? obj[10].toString() : ""));
					if(getCurrentTenantId().equalsIgnoreCase("chetna")){
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[1]) ? getCatlogueValueByCode(obj[1].toString()).getName() : ""));
					}
					cell = row.createCell(colNum++);
					/*cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[7]) ? obj[7].toString() : ""));*/
					List<PMTFarmerDetail> pmtfmr=productDistributionService.findPmtFarmerDetailByPmtId(Long.parseLong(obj[0].toString()));
					String prods= pmtfmr!=null ? pmtfmr.stream().map(e -> e.getProcurementProduct().getName()).distinct().reduce("",
							(a, b) -> a +","+ b) : null;
							cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(prods) ? prods.substring(1): ""));
					/*cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[10]) ? obj[10].toString() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[8]) ? obj[8].toString() : ""));*/
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[8]) ? obj[8].toString() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[20]) ? obj[20].toString() : ""));
					if(getCurrentTenantId().equalsIgnoreCase("chetna")){
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[22]) ? obj[22].toString() : ""));
					HSSFCellStyle style6 = null;
					style6.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
					cell.setCellStyle(style6);
					
				}
				}
			
			else if (PRODUCT_RECEIVE.equals(type)) {
				if ((getIsMultiBranch().equalsIgnoreCase("1")
						&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

					if (StringUtil.isEmpty(branchIdValue)) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[3]) && !StringUtil
								.isEmpty(getBranchesMap().get(getParentBranchMap().get(obj[3].toString())))
										? getBranchesMap().get(getParentBranchMap().get(obj[3].toString()))
										: getBranchesMap().get(obj[3].toString())));
					}
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[3]) ? getBranchesMap().get(obj[3].toString()) : ""));
				} else {
					if (StringUtil.isEmpty(branchIdValue)) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[3]) ? branchesMap.get(obj[3].toString()) : ""));
					}
				}
				if(getCurrentTenantId().equalsIgnoreCase("chetna")){
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[9]) ? (!StringUtil.isEmpty(preferences) ?genDate.format(obj[9]):null):null));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[18]) ? farmerService.findSeasonNameByCode(obj[18].toString()).getName() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[13]) ? obj[13].toString() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[14]) ? obj[14].toString() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[17]) ? obj[17].toString() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[6]) ? obj[6].toString() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[5]) ? obj[5].toString() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[11]) ? obj[11].toString() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[10]) ? obj[10].toString() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[7]) ? obj[7].toString() : ""));
					/*cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[10]) ? obj[10].toString() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[8]) ? obj[8].toString() : ""));*/
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[8]) ? obj[8].toString() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[19]) ? obj[19].toString() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[23]) ? obj[23].toString() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[21]) ? obj[21].toString() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[22]) ? obj[22].toString() : ""));
					/*cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[8]) ? obj[8].toString() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[15]) ? getCatlogueValueByCode(obj[15].toString()).getName() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[16]) ? obj[16].toString() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[20]) ? obj[20].toString() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[18]) ? obj[18].toString() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[19]) ? obj[19].toString() : ""));*/
					double transWeit1 = !ObjectUtil.isEmpty(obj[23]) ? Double.parseDouble(obj[23].toString()) : 0.0;
					double recvWeit1 = !ObjectUtil.isEmpty(obj[22]) ? Double.parseDouble(obj[22].toString()) : 0.0;
					
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(String.valueOf((transWeit1 - recvWeit1) >= 0 ? CurrencyUtil.getDecimalFormat((transWeit1 - recvWeit1), "#.00") : 0)));
				}else if(getCurrentTenantId().equalsIgnoreCase("livelihood")){
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[5]) ? obj[5].toString() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[14]) ? obj[14].toString() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[9]) ? obj[9].toString() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[8]) ? obj[8].toString() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[6]) ? obj[6].toString() : ""));
					/*cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[7]) ? obj[7].toString() : ""));*/
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[23]) ? obj[23].toString() : ""));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[22]) ? obj[22].toString() : ""));
					double transQty=obj[23]!=null && !ObjectUtil.isEmpty(obj[23])?Double.parseDouble(obj[23].toString()):0.0;
					double recetQty=obj[22]!=null && !ObjectUtil.isEmpty(obj[22])?Double.parseDouble(obj[22].toString()):0.0;
					double badQty=transQty-recetQty;
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(String.valueOf(badQty)));
				}else{
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[4]) ? obj[4].toString() : ""));
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[13]) ? obj[13].toString() : ""));
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[14]) ? obj[14].toString() : ""));
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[6]) ? obj[6].toString() : ""));
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[5]) ? obj[5].toString() : ""));
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[11]) ? obj[11].toString() : ""));
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[10]) ? obj[10].toString() : ""));
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[1]) ? getCatlogueValueByCode(obj[1].toString()).getName() : ""));
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[7]) ? obj[7].toString() : ""));
				/*cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[10]) ? obj[10].toString() : ""));
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[8]) ? obj[8].toString() : ""));*/
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[8]) ? obj[8].toString() : ""));
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[19]) ? obj[19].toString() : ""));
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[23]) ? obj[23].toString() : ""));
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[21]) ? obj[21].toString() : ""));
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[22]) ? obj[22].toString() : ""));
				/*cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[8]) ? obj[8].toString() : ""));
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[15]) ? getCatlogueValueByCode(obj[15].toString()).getName() : ""));
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[16]) ? obj[16].toString() : ""));
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[20]) ? obj[20].toString() : ""));
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[18]) ? obj[18].toString() : ""));
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(obj[19]) ? obj[19].toString() : ""));*/
				double transWeit = !ObjectUtil.isEmpty(obj[23]) ? Double.parseDouble(obj[23].toString()) : 0.0;
				double recvWeit = !ObjectUtil.isEmpty(obj[22]) ? Double.parseDouble(obj[22].toString()) : 0.0;
				
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(String.valueOf((transWeit - recvWeit) >= 0 ? CurrencyUtil.getDecimalFormat((transWeit - recvWeit), "#.00") : 0)));
			   }
			}
		} 
		
		}
		else {
			data = isMailExport() ? readData() : readExportData();
			List<PMT> mainGridRows = (List<PMT>) data.get(ROWS);
			if (ObjectUtil.isListEmpty(mainGridRows))
				return null;
			for (PMT pmt : mainGridRows) {

				if ((!StringUtil.isEmpty(cooperative) || !StringUtil.isEmpty(truck) || !StringUtil.isEmpty(driver)
						|| !StringUtil.isEmpty(season) || !StringUtil.isEmpty(transType)) && flag) {
					
					if (!StringUtil.isEmpty(cooperative)) {

						cell = filterRow4.createCell(1);
						if (PMTNT.equals(type)) {
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("cooperative.sender")));
						}
						if (PMTNR.equals(type)) {
							cell.setCellValue(new HSSFRichTextString(getLocaleProperty("villageNames")));
						}
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						cell = filterRow4.createCell(2);
						cell.setCellValue(new HSSFRichTextString(
								pmt.getCoOperative().getName() + " - " + pmt.getCoOperative().getCode()));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

					}

			/*		if (!StringUtil.isEmpty(truck)) {

						cell = filterRow5.createCell(1);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("truckId")));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						cell = filterRow5.createCell(2);
						cell.setCellValue(new HSSFRichTextString(pmt.getTruckId()));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						if (StringUtil.isEmpty(cooperative)) {
							sheet.shiftRows(filterRow4.getRowNum() + 1, filterRow5.getRowNum() + 1, -1);
						}
					}

					if (!StringUtil.isEmpty(driver)) {

						cell = filterRow6.createCell(1);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("driverName")));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						cell = filterRow6.createCell(2);
						cell.setCellValue(new HSSFRichTextString(pmt.getDriverName()));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						if (StringUtil.isEmpty(cooperative) && StringUtil.isEmpty(truck)) {
							sheet.shiftRows(filterRow5.getRowNum() + 1, filterRow6.getRowNum() + 1, -2);
						} else if (StringUtil.isEmpty(cooperative) || StringUtil.isEmpty(truck)) {
							sheet.shiftRows(filterRow5.getRowNum() + 1, filterRow6.getRowNum() + 1, -1);
						}
					}*/

					if (!StringUtil.isEmpty(transType)) {

						cell = filterRow6.createCell(1);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("transType")));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						cell = filterRow6.createCell(2);
						cell.setCellValue(
								new HSSFRichTextString(pmt.getTrnType().equalsIgnoreCase(PMT.TRN_TYPE_STOCK_TRNASFER)
										? getText("transType.productTransfer") : getText("transType.other")));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);
					}

					if (!StringUtil.isEmpty(season)) {
						cell = filterRow5.createCell(1);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("season")));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						cell = filterRow5.createCell(2);
						HarvestSeason seasonObj = farmerService.findHarvestSeasonByCode(pmt.getSeasonCode());
						cell.setCellValue(new HSSFRichTextString(seasonObj.getName() + " - " + seasonObj.getCode()));
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
				cell.setCellValue(new HSSFRichTextString(
						String.valueOf(serialNumber) != null ? String.valueOf(serialNumber) : ""));

				if (PMTNT.equals(type)) {

				/*	if (getCurrentTenantId().equalsIgnoreCase("kpf") 
							|| getCurrentTenantId().equalsIgnoreCase(ESESystem.CABI_TENANT_ID))*/ 
					if(getIsKpfBased().equals("1")){

						cell = row.createCell(colNum++);
						cell.setCellValue(
								!ObjectUtil.isEmpty(pmt.getMtntDate()) ? (!StringUtil.isEmpty(pmt.getMtntDate())
										? DateUtil.getDateByDateTime(pmt.getMtntDate()) : "") : "");

						cell = row.createCell(colNum++);
						cell.setCellValue(
								!ObjectUtil.isEmpty(pmt.getMtntDate()) ? (!StringUtil.isEmpty(pmt.getMtntDate())
										? DateUtil.getMonthByDateTime(pmt.getMtntDate()) : "") : "");

						cell = row.createCell(colNum++);
						cell.setCellValue(
								!ObjectUtil.isEmpty(pmt.getMtntDate()) ? (!StringUtil.isEmpty(pmt.getMtntDate())
										? DateUtil.getYearByDateTime(pmt.getMtntDate()) : "") : "");

						cell = row.createCell(colNum++);
						cell.setCellValue(!StringUtil.isEmpty(pmt.getInvoiceNo()) ? pmt.getInvoiceNo() : "");

					}

					if ((getIsMultiBranch().equalsIgnoreCase("1")
							&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
						if (StringUtil.isEmpty(branchIdValue)) {
							cell = row.createCell(colNum++);
							cell.setCellValue(branchesMap.get(pmt.getBranchId()));
						}
						cell = row.createCell(colNum++);
						cell.setCellValue(getBranchesMap().get(pmt.getBranchId()));
					}

					else {
						if (StringUtil.isEmpty(branchIdValue)) {

							cell = row.createCell(colNum++);
							cell.setCellValue(getBranchesMap().get(pmt.getBranchId()));

						}

					}

					/*if (!getCurrentTenantId().equalsIgnoreCase("kpf") 
							&& !getCurrentTenantId().equalsIgnoreCase(ESESystem.CABI_TENANT_ID))*/
						if(!getIsKpfBased().equals("1")){
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(
								DateUtil.convertDateToString(pmt.getMtntDate(), getGeneralDateFormat())));

						HarvestSeason season = farmerService.findSeasonNameByCode(pmt.getSeasonCode());

						cell = row.createCell(colNum++);
						cell.setCellValue(
								new HSSFRichTextString(!ObjectUtil.isEmpty(season) ? season.getName() : "N/A"));
						
						cell = row.createCell(colNum++);
						cell.setCellValue(
								new HSSFRichTextString(!ObjectUtil.isEmpty(pmt.getMtntReceiptNumber()) ? pmt.getMtntReceiptNumber() : "N/A"));
						style4.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
						cell.setCellStyle(style4);
					}
						if ((!getCurrentTenantId().equalsIgnoreCase("gar"))&& (!getCurrentTenantId().equalsIgnoreCase("kpf")&& (!getCurrentTenantId().equalsIgnoreCase("wub"))&&(!getCurrentTenantId().equalsIgnoreCase("iffco")))) {	
						cell = row.createCell(colNum++);
					if (!ObjectUtil.isEmpty(pmt.getMtntTransferInfo())) {
						Agent agent=agentService.findAgentByAgentId(pmt.getMtntTransferInfo().getAgentId());
						if(!ObjectUtil.isEmpty(agent)){
						cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(pmt.getMtntTransferInfo().getAgentName()) ? pmt.getMtntTransferInfo().getAgentName() : "NA"));
						}
						else{
							cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(pmt.getMtntTransferInfo().getAgentName())	? pmt.getMtntTransferInfo().getAgentName()+" * ": "NA"));
						}
					} 
					else {
						cell.setCellValue(new HSSFRichTextString("NA"));
					}
						}
					if (!pmt.getTrnType().equalsIgnoreCase(PMT.TRN_TYPE_STOCK_TRNASFER)) {
						if (getCurrentTenantId().equalsIgnoreCase("lalteer")
								|| getCurrentTenantId().equalsIgnoreCase("kpf")
								|| getCurrentTenantId().equalsIgnoreCase("iffco")
								|| getCurrentTenantId().equalsIgnoreCase("gar")
								|| getCurrentTenantId().equalsIgnoreCase(ESESystem.CABI_TENANT_ID)) {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(pmt.getCoOperative().getName() + "*"));
						} else {
							Vector<PMTDetail> pmtDetls = new Vector<PMTDetail>(pmt.getPmtDetails());
							if (pmtDetls.size() > 0 && !ObjectUtil.isEmpty(pmtDetls.get(0)) && !ObjectUtil.isEmpty(pmtDetls.get(0).getVillage())) {
								cell = row.createCell(colNum++);
								cell.setCellValue(new HSSFRichTextString(pmtDetls.get(0).getVillage().getName()));
							} else {
								cell = row.createCell(colNum++);
								cell.setCellValue(new HSSFRichTextString(""));
							}
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(pmtDetls.get(0).getCoOperative().getName()));
						}
					} else {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(pmt.getCoOperative().getName() + "*"));
						
						Vector<PMTDetail> pmtDetls = new Vector<PMTDetail>(pmt.getPmtDetails());
						if (pmtDetls.size() > 0 && !ObjectUtil.isEmpty(pmtDetls.get(0)) && !StringUtil.isEmpty(pmtDetls.get(0).getCoOperative()) ) {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(pmtDetls.get(0).getCoOperative().getName()));
						} else {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(""));
						}
						
					}

					/*
					 * if (!getCurrentTenantId().equalsIgnoreCase("agro")) {
					 * cell = row.createCell(colNum++); cell.setCellValue(new
					 * HSSFRichTextString(pmt.getMtntReceiptNumber())); }
					 */
					
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(pmt.getTruckId()));

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(pmt.getDriverName()));

					/*
					 * cell = row.createCell(colNum++); cell.setCellValue(new
					 * HSSFRichTextString(pmt.getTrnType()));
					 */

					/*if (getCurrentTenantId().equalsIgnoreCase("kpf") 
							|| getCurrentTenantId().equalsIgnoreCase(ESESystem.CABI_TENANT_ID))*/ 
					if(getIsKpfBased().equals("1")){
						if (!StringUtil.isEmpty(pmt.getClient())) {
							MasterData masterData = farmerService.findMasterDataIdByCode(pmt.getClient());

							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(
									!ObjectUtil.isEmpty(masterData) ? masterData.getName() : ""));

						} else {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(""));
						}

					}

					Map<String, Object> mtntDetails = getTotalMTNTProductDetails(pmt);

					if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
					cell = row.createCell(colNum++);
					//cell.setCellValue(new HSSFRichTextString(String.valueOf(mtntDetails.get(NO_OF_BAGS))));
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
 					cell.setCellValue(Double.valueOf(mtntDetails.get(NO_OF_BAGS).toString()));
					style4.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					cell.setCellStyle(style4);
					}
					cell = row.createCell(colNum++);
					//cell.setCellValue(new HSSFRichTextString(
					//		CurrencyUtil.getDecimalFormat((Double) mtntDetails.get(GROSS_WEIGHT), "##.00")));
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
 					cell.setCellValue(Double.valueOf(mtntDetails.get(GROSS_WEIGHT).toString()));
					style4.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					cell.setCellStyle(style4);

				/*	if (getCurrentTenantId().equalsIgnoreCase("kpf")
							|| getCurrentTenantId().equalsIgnoreCase(ESESystem.CABI_TENANT_ID)) */
						if(getIsKpfBased().equals("1")){

						cell = row.createCell(colNum++);
						cell.setCellValue(
								new HSSFRichTextString(!StringUtil.isEmpty(mtntDetails.get(SUB_TOTAL).toString())
										? mtntDetails.get(SUB_TOTAL).toString() : "0.00"));

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(pmt.getTotalLabourCost())
								? CurrencyUtil.thousandSeparator(pmt.getTotalLabourCost()) : "0.00"));

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(pmt.getTransportCost())
								? CurrencyUtil.thousandSeparator(pmt.getTransportCost()) : "0.00"));

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(pmt.getTotalAmt())
								? CurrencyUtil.thousandSeparator(pmt.getTotalAmt()) : "0.00"));

					}

				} else {

					/*
					 * if (StringUtil.isEmpty(branchIdValue)) {
					 * 
					 * cell = row.createCell(colNum++);
					 * cell.setCellValue(getBranchesMap().get(pmt.getBranchId())
					 * ); }
					 */
					if(getIsKpfBased().equals("1")){
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(pmt.getMtnrDate()) ? (!StringUtil.isEmpty(pmt.getMtnrDate())
								? DateUtil.getDateByDateTime(pmt.getMtnrDate()) : "") : ""));

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(pmt.getMtnrDate()) ? (!StringUtil.isEmpty(pmt.getMtnrDate())
								? DateUtil.getMonthByDateTime(pmt.getMtnrDate()) : "") : ""));

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(pmt.getMtnrDate()) ? (!StringUtil.isEmpty(pmt.getMtnrDate())
								? DateUtil.getYearByDateTime(pmt.getMtnrDate()) : "") : ""));
					}
					if(!getIsKpfBased().equals("1")){
					if ((getIsMultiBranch().equalsIgnoreCase("1")
							&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

						if (StringUtil.isEmpty(branchIdValue)) {

							cell = row.createCell(colNum++);
							cell.setCellValue(!StringUtil
									.isEmpty(getBranchesMap().get(getParentBranchMap().get(pmt.getBranchId())))
											? getBranchesMap().get(getParentBranchMap().get(pmt.getBranchId()))
											: getBranchesMap().get(pmt.getBranchId()));

						}

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(pmt.getBranchId())));

					} else {
						if (StringUtil.isEmpty(branchIdValue)) {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(pmt.getBranchId())));
						}
					}
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							DateUtil.convertDateToString(pmt.getMtnrDate(), getGeneralDateFormat())));

					HarvestSeason season = farmerService.findSeasonNameByCode(pmt.getSeasonCode());

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(season) ? season.getName() : "N/A"));
					
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(pmt.getMtntReceiptNumber()) ? pmt.getMtntReceiptNumber(): "N/A"));
					style4.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					cell.setCellStyle(style4);
					
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(pmt.getMtnrReceiptNumber()) ? pmt.getMtnrReceiptNumber() : "N/A"));
					style4.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					cell.setCellStyle(style4);
					}
					/*
					 * cell = row.createCell(colNum++); cell.setCellValue(new
					 * HSSFRichTextString(pmt.getCoOperative().getName()));
					 */
					/*if (!getCurrentTenantId().equalsIgnoreCase("kpf")
							&& !getCurrentTenantId().equalsIgnoreCase(ESESystem.CABI_TENANT_ID)) */
					if(!getIsKpfBased().equals("1")){
						if (pmt.getTrnType().equalsIgnoreCase(PMT.TRN_TYPE_STOCK_TRNASFER)) {
							Vector<PMTDetail> pmtDetls = new Vector<PMTDetail>(pmt.getPmtDetails());
							if (pmtDetls.size() > 0) {
								cell = row.createCell(colNum++);
								cell.setCellValue(new HSSFRichTextString(pmt.getCoOperative().getName()));

							} else {
								cell = row.createCell(colNum++);
								cell.setCellValue(new HSSFRichTextString(""));

							}
						} else {
							
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(pmt.getCoOperative().getName()));
							
						}
					}
					
					Vector<PMTDetail> pmtDetls = new Vector<PMTDetail>(pmt.getPmtDetails());		
					if (pmtDetls.size() > 0) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(pmtDetls.get(0).getCoOperative().getName()));
					} else {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(""));
					}					
					
					/*
					 * if (!getCurrentTenantId().equalsIgnoreCase("agro")) {
					 * cell = row.createCell(colNum++); cell.setCellValue(new
					 * HSSFRichTextString(pmt.getMtnrReceiptNumber())); }
					 */
					if(getIsKpfBased().equals("1")){
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(pmt.getInvoiceNo())?pmt.getInvoiceNo():""));
						
					}
					/*cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(pmt.getCoOperative().getName())?pmt.getCoOperative().getName():""));*/
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(pmt.getTruckId()));

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(pmt.getDriverName()));

					/*
					 * cell = row.createCell(colNum++); cell.setCellValue(new
					 * HSSFRichTextString(pmt.getTrnType()));
					 */
					
					if(getCurrentTenantId().equalsIgnoreCase("wub")){
						if (!StringUtil.isEmpty(pmt.getClient())) {
							MasterData masterData = farmerService.findMasterDataIdByCode(pmt.getClient());

							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(
									!ObjectUtil.isEmpty(masterData) ? masterData.getName() : ""));

						} else {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(""));
						}

					}
					
					Map<String, Object> mtntDetails = getTotalMTNTDetail(pmt.getMtntReceiptNumber());
					if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){						
						cell = row.createCell(colNum++);
						//cell.setCellValue(new HSSFRichTextString(CurrencyUtil.getDecimalFormat((Double) mtntDetails.get(GROSS_WEIGHT), "##.00")));
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellValue(Double.valueOf(mtntDetails.get(GROSS_WEIGHT).toString()));
						style4.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
						cell.setCellStyle(style4);
					}
										
					Map<String, Object> mtnrDetails = getTotalMTNRProductDetails(pmt);

					cell = row.createCell(colNum++);
					//cell.setCellValue(new HSSFRichTextString(CurrencyUtil.getDecimalFormat((Double) mtnrDetails.get(GROSS_WEIGHT), "##.00")));
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(Double.valueOf(mtnrDetails.get(GROSS_WEIGHT).toString()));
					style4.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					cell.setCellStyle(style4);
					if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
					Double transporationLoss=0.0;
					transporationLoss= (Double)mtntDetails.get(GROSS_WEIGHT)-(Double)mtnrDetails.get(GROSS_WEIGHT);
					cell = row.createCell(colNum++);
					style4.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					cell.setCellStyle(style4);
					//cell.setCellValue(new HSSFRichTextString(CurrencyUtil.getDecimalFormat((Double) transporationLoss, "##.00")));
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(Double.valueOf(transporationLoss));
					}
				}

				HSSFRow subGridRowHead = sheet.createRow(rowNum++);
				String sunGridcolumnHeaders = getLocaleProperty("exportSubColumnHeader" + type);
				int subGridIterator = 1;

				for (String cellHeader : sunGridcolumnHeaders.split("\\,")) {

					if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
						cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
					} else if (cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT)) {
						cellHeader = cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());
					}

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

				for (PMTDetail pmtDetail : pmt.getPmtDetails()) {

					row = sheet.createRow(rowNum++);
					colNum = 1;

				/*	if (getCurrentTenantId().equalsIgnoreCase("kpf") 
							|| getCurrentTenantId().equalsIgnoreCase(ESESystem.CABI_TENANT_ID))*/
						if(getIsKpfBased().equals("1")){

						cell = row.createCell(colNum++);
						cell.setCellValue(serialNo);
					}

					if (getCurrentTenantId().equalsIgnoreCase("lalteer")) {
						if (pmtDetail.getFarmer() != null) {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(
									pmtDetail.getFarmer().getName() + " - " + pmtDetail.getFarmer().getFarmerCode()));
						}
					}

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(pmtDetail.getProcurementProduct().getName()));
					
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(pmtDetail.getProcurementProduct().getUnit()));

					/*
					 * cell = row.createCell(colNum++); cell.setCellValue(new
					 * HSSFRichTextString((!StringUtil.isEmpty(pmtDetail.
					 * getGradeMaster()) && pmtDetail.getGradeMaster()!=null) ?
					 * pmtDetail.getGradeMaster().getName() : ""));
					 */

					cell = row.createCell(colNum++);
					cell.setCellValue(
							new HSSFRichTextString(pmtDetail.getProcurementGrade().getProcurementVariety().getName()));

					if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(pmtDetail.getProcurementGrade().getName()));
					}

					/*if (getCurrentTenantId().equalsIgnoreCase("kpf")
							|| getCurrentTenantId().equalsIgnoreCase(ESESystem.CABI_TENANT_ID))*/ 
					if (!getCurrentTenantId().equalsIgnoreCase("wub")){
					if(getIsKpfBased().equals("1")){
					
						if (!StringUtil.isEmpty(pmtDetail.getUom())) {
							FarmCatalogue cat = catalogueService.findCatalogueByCode(pmtDetail.getUom());
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(cat) ? cat.getName() : ""));

						} else {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(""));
						}
						
					  }
					}else{
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(pmtDetail.getProcurementProduct()) ? pmtDetail.getProcurementProduct().getUnit() : ""));
					}

					/*
					 * if (pmtDetail.getPmt().getTrnType() != null &&
					 * pmtDetail.getPmt().getTrnType().equalsIgnoreCase(PMT.
					 * TRN_TYPE_STOCK_TRNASFER)) {
					 * 
					 * cell = row.createCell(colNum++); cell.setCellValue(new
					 * HSSFRichTextString(ObjectUtil.isEmpty(pmtDetail.
					 * getCoOperative()) ? "" :
					 * (StringUtil.isEmpty(pmtDetail.getCoOperative().getName())
					 * ? "" : pmtDetail.getCoOperative().getName() + " *")));
					 * 
					 * } else {
					 * 
					 * if (!ObjectUtil.isEmpty(pmtDetail.getPmt())) { cell =
					 * row.createCell(colNum++); cell.setCellValue(new
					 * HSSFRichTextString(pmtDetail.getPmt().getCoOperative().
					 * getName() + " *"));
					 * 
					 * } }
					 */

					if (PMTNT.equals(type)) {
				/*		if ((!getCurrentTenantId().equalsIgnoreCase("gar"))&&(!getCurrentTenantId().equalsIgnoreCase("kpf")&&(!getCurrentTenantId().equalsIgnoreCase("iffco")))) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(pmtDetail.getCoOperative().getName()));
						}*/
						
						if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){ 
						cell = row.createCell(colNum++);
						//cell.setCellValue(new HSSFRichTextString(String.valueOf(pmtDetail.getMtntNumberOfBags())));
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						if(ObjectUtil.isEmpty(pmtDetail.getMtntNumberOfBags()) || StringUtil.isEmpty(pmtDetail.getMtntNumberOfBags())){
		 					cell.setCellValue("0.000");
		 				}else{
	 					cell.setCellValue(Double.valueOf(pmtDetail.getMtntNumberOfBags()));
		 				}
						}
						cell = row.createCell(colNum++);
						//cell.setCellValue(new HSSFRichTextString(
						//		CurrencyUtil.getDecimalFormat(pmtDetail.getMtntGrossWeight(), "##.00")));
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						if(ObjectUtil.isEmpty(pmtDetail) || StringUtil.isEmpty(pmtDetail.getMtntGrossWeight())){
		 					cell.setCellValue("0.000");
		 				}else{
	 					cell.setCellValue(Double.valueOf(pmtDetail.getMtntGrossWeight()));
		 				}

						/*if (getCurrentTenantId().equalsIgnoreCase("kpf") 
								|| getCurrentTenantId().equalsIgnoreCase(ESESystem.CABI_TENANT_ID)) */
							if(getIsKpfBased().equals("1")){
							if ((!getCurrentTenantId().equalsIgnoreCase("gar"))&& (!getCurrentTenantId().equalsIgnoreCase("kpf")&&(!getCurrentTenantId().equalsIgnoreCase("iffco")))){

							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(pmtDetail.getPricePerUnit())
									? pmtDetail.getPricePerUnit() : "0.00"));

							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(
									!StringUtil.isEmpty(pmtDetail.getSubTotal()) ? pmtDetail.getSubTotal() : "0.00"));
							}
						}

					}
					if (PMTNR.equals(type)) {

						/*
						 * cell = row.createCell(colNum++);
						 * cell.setCellValue(new
						 * HSSFRichTextString(pmt.getCoOperative().getName()));
						 */
		/*				if(!getCurrentTenantId().equalsIgnoreCase("iffco")){
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(pmtDetail.getCoOperative().getName()));
						}
						*/
						Map<String, Object> mtntDetails = getTotalMTNTDetails(pmtDetail.getPmt().getMtntReceiptNumber(),pmtDetail.getProcurementGrade().getId());
						if(!getCurrentTenantId().equals(ESESystem.PRATIBHA_TENANT_ID)){
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(String.valueOf(mtntDetails.get(NO_OF_BAGS))));
						style4.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
						cell.setCellStyle(style4);
					    cell = row.createCell(colNum++);
					    cell.setCellValue(new HSSFRichTextString(
								CurrencyUtil.getDecimalFormat((Double)mtntDetails.get(GROSS_WEIGHT), "##.00")));
					    style4.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
						cell.setCellStyle(style4);
						}
						
						if(!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
						cell = row.createCell(colNum++);
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellValue(Double.valueOf(pmtDetail.getMtnrNumberOfBags()));
						//cell.setCellValue(new HSSFRichTextString(String.valueOf(pmtDetail.getMtnrNumberOfBags())));
						 style4.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
							cell.setCellStyle(style4);
						}
						
						cell = row.createCell(colNum++);
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellValue(Double.valueOf(pmtDetail.getMtnrGrossWeight()));
						//cell.setCellValue(new HSSFRichTextString(CurrencyUtil.getDecimalFormat(pmtDetail.getMtnrGrossWeight(), "##.00")));
						 style4.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
							cell.setCellStyle(style4);
					}

					/*
					 * if (pmtDetail.getPmt().getTrnType() == null) {
					 * 
					 * cell = row.createCell(colNum++); cell.setCellValue(new
					 * HSSFRichTextString(getText("transType.other")));
					 * 
					 * } else {
					 * 
					 * cell = row.createCell(colNum++); cell.setCellValue(new
					 * HSSFRichTextString(
					 * pmtDetail.getPmt().getTrnType().equalsIgnoreCase(PMT.
					 * TRN_TYPE_STOCK_TRNASFER) ?
					 * getText("transType.productTransfer") :
					 * getText("transType.other")));
					 * 
					 * }
					 */

				}

				row = sheet.createRow(rowNum++);
			}
		}
		row = sheet.createRow(rowNum++);
		/*for (

				int i = 0; i <= colNum; i++) {
			sheet.autoSizeColumn(i);
		}*/

		int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
	//	picture.resize();
		String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
				: request.getSession().getId();
		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("pmtReportList" + type) + fileNameDateFormat.format(new Date()) + ".xls";
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

	public String getTransType() {

		return transType;
	}

	public void setTransType(String transType) {

		this.transType = transType;
	}

	@Override
	public String getCurrentMenu() {
		String type = request.getParameter("type");
		if (!StringUtil.isEmpty(type)) {
			if (type.equalsIgnoreCase("pmtnr")) {
				return getText("menu.select");
			} else if (type.equalsIgnoreCase("pmtnt")) {
				return getText("menu1.select");
			} else if (type.equalsIgnoreCase("pmtnt")) {
				return getText("menu1.select");
			} else if (type.equalsIgnoreCase("pmtnt")) {
				return getText("menu1.select");
			}
		}
		return getText("menu.select");

	}

	/*
	 * public Map<String, String> getSeasonList() {
	 * 
	 * Map<String, String> seasonMap = new LinkedHashMap<String, String>();
	 * 
	 * List<Object[]> seasonList = farmerService.listSeasonCodeAndName();
	 * 
	 * for (Object[] obj : seasonList) {
	 * 
	 * seasonMap.put(String.valueOf(obj[0]), String.valueOf(obj[1]));
	 * 
	 * } return seasonMap;
	 * 
	 * }
	 */

	public Map<String, String> getReceiptNumberList() {

		Map<String, String> receiptMap = new LinkedHashMap<String, String>();

		List<PMT> receiptList = farmerService.lisReceiptNumberList();

		for (PMT obj : receiptList) {
			if (obj.getMtnrReceiptNumber() != null) {
				receiptMap.put(String.valueOf(obj.getMtnrReceiptNumber()), obj.getMtnrReceiptNumber());
			}
			if (obj.getMtntReceiptNumber() != null) {
				receiptMap.put(String.valueOf(obj.getMtntReceiptNumber()), obj.getMtntReceiptNumber());
			}
		}
		return receiptMap;

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

	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}

	public String getBranchIdParma() {
		return branchIdParma;
	}

	public void setBranchIdParma(String branchIdParma) {
		this.branchIdParma = branchIdParma;
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

	public void populateProCenterList() {
		JSONArray proCenterArr = new JSONArray();
		List<Object[]> proCenterList = productDistributionService.listTransferProCenters();
		if (!ObjectUtil.isEmpty(proCenterList)) {
			proCenterList.forEach(obj -> {
				proCenterArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(proCenterArr);
	}

	public void populateGinningList() {
		JSONArray ginningrArr = new JSONArray();
		List<Object[]> ginningList = productDistributionService.listReceiverGinning();
		if (!ObjectUtil.isEmpty(ginningList)) {
			ginningList.forEach(obj -> {
				ginningrArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(ginningrArr);
	}

	public String getProCenter() {
		return proCenter;
	}

	public void setProCenter(String proCenter) {
		this.proCenter = proCenter;
	}

	public String getGinning() {
		return ginning;
	}

	public void setGinning(String ginning) {
		this.ginning = ginning;
	}

	public String getImgId() {
		return imgId;
	}

	public void setImgId(String imgId) {
		this.imgId = imgId;
	}

	public void populateFarmer() {

	}

	public String populateImage() {

		try {
			setImgId(imgId);
			PMTImageDetails pmtImg = null;
			if (!StringUtil.isEmpty(imgId))
				pmtImg = productDistributionService.findPMTImageDetailById(Long.valueOf(imgId));
			byte[] image = null;
			if (!ObjectUtil.isEmpty(pmtImg) && pmtImg.getPhoto() != null) {
				image = pmtImg.getPhoto();
			}
			response.setContentType("multipart/form-data");
			OutputStream out = response.getOutputStream();
			out.write(image);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public String getPmtFarmerData() {
		return pmtFarmerData;
	}

	public void setPmtFarmerData(String pmtFarmerData) {
		this.pmtFarmerData = pmtFarmerData;
	}

	public String getIcsName() {
		return icsName;
	}

	public void setIcsName(String icsName) {
		this.icsName = icsName;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map readData(String projectionToken,Object filter) {

		Map<String, String> projectionProperties = !StringUtil.isEmpty(projectionToken)
				? getProjectionProperties(projectionToken) : null;
		Map data = reportService.listWithMultipleFiltering(getSord(), getSidx(), getStartIndex(), getLimit(),
				getsDate(), geteDate(), filter, getPage(), projectionProperties);
		return data;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getPmtId() {
		return pmtId;
	}

	public void setPmtId(String pmtId) {
		this.pmtId = pmtId;
	}
	
	public String getSelectedFieldStaff() {
		return selectedFieldStaff;
	}

	public void setSelectedFieldStaff(String selectedFieldStaff) {
		this.selectedFieldStaff = selectedFieldStaff;
	}
	public void populateAgentList() {
		JSONArray agentArr = new JSONArray();
		JSONArray userArr = new JSONArray();
		List<Object[]> agentList = agentService.listAgentIdName();
		if (!ObjectUtil.isEmpty(agentList)) {
			agentList.forEach(obj -> {
				agentArr.add(getJSONObject(obj[0].toString(), obj[1].toString()+" - "+obj[0].toString()));
			});
		}
		List<User> userList = userService.listUsers();
		if (!ObjectUtil.isEmpty(userList)) {
			userList.forEach(obj -> {
				userArr.add(getJSONObject(obj.getUsername(),obj.getUsername()+"*"));
			});
		}
		agentArr.addAll(userArr);
		
		sendAjaxResponse(agentArr);
		
		}

}
