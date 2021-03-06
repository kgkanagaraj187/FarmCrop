/*
\\tabIndex * ProcurementReportAction.java
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
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

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
import com.ese.view.service.PaymentAction;
import com.opensymphony.xwork2.ActionContext;
import com.sourcetrace.eses.BreadCrumb;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.Distribution;
import com.sourcetrace.eses.order.entity.txn.DistributionDetail;
import com.sourcetrace.eses.order.entity.txn.FarmerBalanceReport;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.order.entity.txn.ProcurementDetail;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ExportUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

// TODO: Auto-generated Javadoc
/**
 * The Class PeriodicInspectionReportAction.
 */
public class FarmerBalanceReportAction extends BaseReportAction implements IExporter {

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final long serialVersionUID = 1L;

	private String accountId;
	private String gridIdentity;

	private AgroTransaction filter;
	@Autowired
	private ILocationService locationService;
	private IFarmerService farmerService;
	private IAgentService agentService;
	private IProductDistributionService productDistributionService;
	private IPreferencesService preferncesService;
	private String agentId;

	// protected List<String> fields = new ArrayList<String>();
	private Map<String, String> fields = new LinkedHashMap<String, String>();
	Map<Integer, String> balanceTypeList = new HashMap<Integer, String>();

	private String xlsFileName;
	private InputStream fileInputStream;
	private String exportLimit;
	private String farmerId;
	private String txnType;
	private String type;
	private String daterange;
	private String farmeridSeasonCode;
	Map<String, String> agentLists = new LinkedHashMap<String, String>();
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	List<AgroTransaction> agroTransactions;
	private String seasonCode;
    public static final String PRODUCT_DISTRIBUTION = "314";
    public static final String PROCUREMENT_PRODUCT_ENROLLMENT = "316";
    public static final String PRODUCT_RETURN_FROM_FARMER = "334";
    public static final String PAYMENT_ADAPTER = "334P";
    public static final String DISTIBUTION_PAYMENT_TXN = "334D";
    public static final String LOAN_DISBURSEMENT= "701";
    public static final String LOAN_REPAYMENT= "702";
	private String farmerName;
	protected String command;
	private String samithi;
	private String enableLoanModule;
	/**
	 * Gets the fields.
	 * 
	 * @return the fields
	 */
	/*
	 * public List<String> getFields() {
	 * 
	 * return fields; }
	 * 
	 *//**
		 * Sets the fields.
		 * 
		 * @param fields
		 *            the new fields
		 *//*
		 * public void setFields(List<String> fields) {
		 * 
		 * this.fields = fields; }
		 */

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
	 * Gets the grid identity.
	 * 
	 * @return the grid identity
	 */
	public String getGridIdentity() {

		return gridIdentity;
	}

	/**
	 * @see com.sourcetrace.esesw.view.BaseReportAction#list()
	 */
	@Override
	public String list() throws Exception {
		type = request.getParameter("type");
	
		ESESystem preferences = preferncesService.findPrefernceById("1");
		setEnableLoanModule(preferences.getPreferences().get(ESESystem.ENABLE_LOAN_MODULE));
		if (!StringUtil.isEmpty(preferences)) {
			setExportLimit(preferences.getPreferences().get(ESESystem.EXPORT_RECORD_LIMIT));
		}
		balanceTypeList.put(316, getText("procurement"));
		// if(getType().equalsIgnoreCase("farmer")){
		balanceTypeList.put(314, getText("distribution"));
		/*
		 * } else { balanceTypeList.put(514, getText("distribution")); }
		 */
		request.setAttribute(HEADING, getText(LIST));
		fields.put("1", getText("season"));
		if (getType().equalsIgnoreCase("farmer")) {
			fields.put("2", getText("farmer"));
		} else {
			fields.put("2", getText("agent"));
		}

		filter = new AgroTransaction();
		return LIST;
	}

	/**
	 * Gets the balance type list.
	 * 
	 * @return the balance type list
	 */

	/**
	 * Sets the balance type list.
	 * 
	 * @param balanceTypeList
	 *            the balance type list
	 */
	public void setBalanceTypeList(Map<Integer, String> balanceTypeList) {

		this.balanceTypeList = balanceTypeList;
	}

	public Map<Integer, String> getBalanceTypeList() {
		return balanceTypeList;
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
		// this.filter.setType(3);

		ESEAccount account = new ESEAccount();
		filter = new AgroTransaction();

		if (!StringUtil.isEmpty(getFarmerId())) {
			filter.setFarmerId(getFarmerId());
		}
		if (!StringUtil.isEmpty(getAgentId())) {
			filter.setAgentId(getAgentId());
		}

		if (!StringUtil.isEmpty(getSeasonCode())) {
			filter.setSeasonCode(getSeasonCode());
		} else {
			HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
			Object branchObject = httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
			String currentBranch = ObjectUtil.isEmpty(branchObject) ? ESESystem.SYSTEM_ESE_NAME
					: branchObject.toString();
			ESESystem ese = preferncesService.findPrefernceByOrganisationId(currentBranch);
			String currentSeasonCode = "";
			if (!ObjectUtil.isEmpty(ese)) {
				ESESystem preference = preferncesService.findPrefernceById(String.valueOf(ese.getId()));
				currentSeasonCode = preference.getPreferences().get(ESESystem.CURRENT_SEASON_CODE);

				if (StringUtil.isEmpty(currentSeasonCode)) {
					currentSeasonCode = "";
				}
			}

			filter.setSeasonCode(currentSeasonCode);
		}
		
		
		if (getType().equalsIgnoreCase("farmer")) {
			account.setAccountType("3");
			if(getCurrentTenantId().equalsIgnoreCase("susagri")){
				if(!StringUtil.isEmpty(samithi)){
					filter.setSamithiId(Long.valueOf(samithi));	
				}
				
			}
		} else {
			account.setAccountType("1");
		}

		filter.setAccount(account);
		filter.setBranch_id(getBranchId());
		super.filter = this.filter;
		Map data = readData("farmerBalance");
		return sendJSONResponse(data);
	}

	public String data() throws Exception {
		this.gridIdentity = "subGrid";
		filter = new AgroTransaction();
		String[] idTxnType = id.split("-");
		filter.setId(Long.valueOf(idTxnType[2]));
		filter.setTxnType(idTxnType[3]);
		filter.setBranchId(getBranchId());
		if (!StringUtil.isEmpty(getSeasonCode())) {
			filter.setSeasonCode(getSeasonCode());
		} else {
			HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
			Object branchObject = httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
			String currentBranch = ObjectUtil.isEmpty(branchObject) ? ESESystem.SYSTEM_ESE_NAME
					: branchObject.toString();
			ESESystem ese = preferncesService.findPrefernceByOrganisationId(currentBranch);
			String currentSeasonCode = "";
			if (!ObjectUtil.isEmpty(ese)) {
				ESESystem preference = preferncesService.findPrefernceById(String.valueOf(ese.getId()));
				currentSeasonCode = preference.getPreferences().get(ESESystem.CURRENT_SEASON_CODE);

				if (StringUtil.isEmpty(currentSeasonCode)) {
					currentSeasonCode = "";
				}
			}

			filter.setSeasonCode(currentSeasonCode);
		}
		super.filter = this.filter;
		Map data = readData();
		return sendJSONResponse(data);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected String sendJSONResponse(Map data) throws Exception {

		JSONObject gridData = new JSONObject();
		gridData.put(PAGE, getPage());
		totalRecords = (Integer) data.get(RECORDS);
		gridData.put(TOTAL, java.lang.Math.ceil(totalRecords / Double.valueOf(Integer.toString(getLimit()))));
		gridData.put(RECORDS, totalRecords);
		List list = (List) data.get(ROWS);
		JSONArray rows = new JSONArray();
		if (list != null) {
			branchIdValue = getBranchId();
			if (StringUtil.isEmpty(branchIdValue)) {
				buildBranchMap();
			}
			for (Object record : list) {
				if (!toJSON(record).isEmpty()) {
					rows.add(toJSON(record));
				}
			}
		}
		if (totalRecords > 0) {
			gridData.put("userdata", userDataToJSON());
		} else {
			gridData.put("userdata", userDataToJSON());
		}
		gridData.put(ROWS, rows);
		//
		PrintWriter out = response.getWriter();
		out.println(gridData.toString());

		return null;
	}

	/**
	 * Sub grid detail.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	// @SuppressWarnings("unchecked")
	// public String subGridDetail() throws Exception {
	//
	// this.gridIdentity = "subDetail";
	// AgroTransaction agroDetail = new AgroTransaction();
	// ESEAccount account = new ESEAccount();
	// account.setId(Long.valueOf(accountId));
	// agroDetail.setAccount(account);
	// agroDetail.setBalanceType(filter.getBalanceType());
	// super.filter = agroDetail;
	// Map data = readData();
	// return sendJSONResponse(data);
	// }

	/**
	 * @see com.sourcetrace.esesw.view.BaseReportAction#toJSON(java.lang.Object)
	 */
	@Override
	@SuppressWarnings({ "unchecked" })
	public JSONObject toJSON(Object obj) {
		ESESystem preferences = preferncesService.findPrefernceById("1");	
		setEnableLoanModule(preferences.getPreferences().get(ESESystem.ENABLE_LOAN_MODULE));
		JSONObject jsonObject = new JSONObject();
		if (gridIdentity.equalsIgnoreCase("detail")) {
			Object[] datas = (Object[]) obj;
			String agentname= datas[2].toString().replaceAll(" ", "");
			Farmer f = null;
			if (datas.length > 0) {

				/*
				 * f =
				 * farmerService.findFarmerByFarmerId(String.valueOf(datas[2]));
				 */
				if (!StringUtil.isEmpty(datas[2])) {
					/*if (StringUtil.isLong(datas[2])) {
						f = farmerService.findFarmerById(Long.valueOf(datas[2].toString()));
					} else {
						f = farmerService.findFarmerByFarmerId(String.valueOf(datas[2].toString()));
					}*/
					f = farmerService.findFarmerByFarmerId(String.valueOf(datas[2].toString()));
				}
				JSONArray rows = new JSONArray();
				if (f != null) {
					rows.add(f.getFarmerId());
					
					if (f.getLastName() != null){
					
					//rows.add(f.getFirstName() + " " + f.getLastName());
						String firstName =  String.valueOf(f.getFirstName() + " " + f.getLastName());
						String farmerId = String.valueOf(f.getId());
						String linkField = "<a href=farmer_detail.action?id="+farmerId+" target=_blank>"+firstName+"</a>";
						rows.add(!ObjectUtil.isEmpty(linkField) ? linkField : "");// Farmer
					
					}else{
						//rows.add(f.getFirstName());
						String firstName =  String.valueOf(f.getFirstName());
						String farmerId = String.valueOf(f.getId());
						String linkField = "<a href=farmer_detail.action?id="+farmerId+" target=_blank>"+firstName+"</a>";
						rows.add(!ObjectUtil.isEmpty(linkField) ? linkField : "");// Farmer
					}
					
				} else {
					rows.add(String.valueOf(datas[7]));
					rows.add(String.valueOf(datas[6]));
				}
				if(getCurrentTenantId().equalsIgnoreCase("susagri")){
					rows.add(!ObjectUtil.isEmpty(f) && !ObjectUtil.isEmpty(f.getSamithi())?f.getSamithi().getName() : "NA");
				}
				/* * rows.add(String.valueOf(datas[2]));
				 * 
				 * rows.add(String.valueOf(datas[3]));
				 */
				
				
			
					Double bal = Double.valueOf(datas[3].toString());
					if (bal >= 0) {
						rows.add(CurrencyUtil.currencyFormatter(Math.abs(bal)) + "-" + "CR");
					} else {
						rows.add(CurrencyUtil.currencyFormatter(Math.abs(bal)) + "-" + "DR");
					}
				
				if (ObjectUtil.isEmpty(f)) {
					jsonObject.put("id", String.valueOf(agentname) + "-" + String.valueOf(datas[1]) + "-"
							+ String.valueOf(datas[0]) + "-" + String.valueOf(datas[5]));
				} else {
					jsonObject.put("id", f.getFarmerId() + "-" + String.valueOf(datas[1]) + "-"
							+ String.valueOf(datas[0]) + "-" + String.valueOf(datas[5]));
				}
				jsonObject.put("cell", rows);
			}
		} else {
			AgroTransaction agroTransaction = (AgroTransaction) obj;
			JSONArray rows = new JSONArray();
			if (!ObjectUtil.isEmpty(agroTransaction)) {
				// rows.add(String.valueOf(agroTransaction.getTxnTime()));
				rows.add(DateUtil.convertDateToString(agroTransaction.getTxnTime(),
						getGeneralDateFormat().concat(" HH:mm:ss")));
				rows.add(agroTransaction.getReceiptNo());
				rows.add(agroTransaction.getTxnDesc());
				rows.add(CurrencyUtil.currencyFormatter(agroTransaction.getIntBalance()));
				rows.add(CurrencyUtil.currencyFormatter(agroTransaction.getTxnAmount()));
				if (agroTransaction.getBalAmount() >= 0) {
					rows.add(CurrencyUtil.currencyFormatter(Math.abs(agroTransaction.getBalAmount())) + "-" + "CR");
				} else {
					rows.add(CurrencyUtil.currencyFormatter(Math.abs(agroTransaction.getBalAmount())) + "-" + "DR");
				}

				jsonObject.put("id", String.valueOf(agroTransaction.getId()) + "-" + agroTransaction.getTxnType());
				jsonObject.put("cell", rows);
			}
		}
		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	public String farmerDetail() throws Exception {

		String view = "";
		if (!StringUtil.isEmpty(farmeridSeasonCode)) {
			String farmerIdSeason[] = farmeridSeasonCode.split("-");
			List<AgroTransaction> agroTransactionList = productDistributionService
					.findAgroTxnByFarmerIdTXnTypesSeasonCode(farmerIdSeason[0], farmerIdSeason[1], "314,316,334");
			Set<ProcurementDetail> procurementDetailSet;
			List<DistributionDetail> distributionList;
			List<DistributionDetail> distributionDetails = new LinkedList<DistributionDetail>();
			List<ProcurementDetail> procuremrementList;
			List<ProcurementDetail> procurmentDetails = new LinkedList<ProcurementDetail>();
			filter = new AgroTransaction();

			agroTransactions = new LinkedList<AgroTransaction>();
			for (AgroTransaction agroTransaction : agroTransactionList) {
				Double finalBal = agroTransaction.getBalAmount();
				if (!ObjectUtil.isEmpty(agroTransaction.getDistribution())) {
					Double qty = 0.0;
					String item = "";
					distributionList = new LinkedList<DistributionDetail>(
							agroTransaction.getDistribution().getDistributionDetails());

					for (DistributionDetail detail : distributionList) {
						if (finalBal < 0) {
							detail.setIndividulalFinalBal(Math.abs(finalBal) + "-Dr");
						} else {
							detail.setIndividulalFinalBal(Math.abs(finalBal) + "-Cr");
						}

						finalBal = finalBal + detail.getSubTotal();
						detail.setItem(detail.getProduct().getName());

						distributionDetails.add(detail);
					}
					agroTransaction.setTxnTimeStr(DateUtil.convertDateToString(agroTransaction.getTxnTime(),
							getGeneralDateFormat().concat(" HH:mm:ss")));
					agroTransaction.setDistributionDetailList(distributionDetails);
					if (agroTransaction.getTxnDesc().equalsIgnoreCase(Distribution.DISTRIBUTION_PAYMENT_AMOUNT))
						agroTransaction.setBalAmount(agroTransaction.getIntBalance() + agroTransaction.getTxnAmount());
					else
						agroTransaction.setBalAmount(agroTransaction.getIntBalance() - agroTransaction.getTxnAmount());
					if (agroTransaction.getBalAmount() < 0) {
						agroTransaction.setFinalBalance(Math.abs(agroTransaction.getBalAmount()) + "-Dr");
					} else {
						agroTransaction.setFinalBalance(Math.abs(agroTransaction.getBalAmount()) + "-Cr");
					}
					agroTransaction.setDistributionDetailList(distributionList);
					agroTransaction.setQty(qty);
					agroTransaction.setItem(StringUtil.removeLastComma(item));
					if (agroTransaction.getTxnAmount() == 0) {
						agroTransaction = null;
					}
					agroTransactions.add(agroTransaction);
				}
				// Procurement codes

				if (!ObjectUtil.isEmpty(agroTransaction) && !ObjectUtil.isEmpty(agroTransaction.getProcurement())) {
					Double qty = 0.0;
					String item = "";
					procuremrementList = new LinkedList<ProcurementDetail>(
							agroTransaction.getProcurement().getProcurementDetails());

					for (ProcurementDetail detail : procuremrementList) {
						if (finalBal < 0) {
							detail.setIndividulalFinalBal(Math.abs(finalBal) + "-Dr");
						} else {
							detail.setIndividulalFinalBal(Math.abs(finalBal) + "-Cr");
						}
						finalBal = finalBal - detail.getSubTotal();
						detail.setItem(detail.getProcurementProduct().getName());

						procurmentDetails.add(detail);
					}
					agroTransaction.setProcurementDetailList(procurmentDetails);
					agroTransaction.setTxnTimeStr(DateUtil.convertDateToString(agroTransaction.getTxnTime(),
							getGeneralDateFormat().concat(" HH:mm:ss")));
					if (agroTransaction.getTxnDesc().equalsIgnoreCase(Procurement.PROCUREMENT_PAYMENT))
						agroTransaction.setBalAmount(agroTransaction.getIntBalance() - agroTransaction.getTxnAmount());
					if (agroTransaction.getBalAmount() < 0) {
						agroTransaction.setFinalBalance(Math.abs(agroTransaction.getBalAmount()) + "-Dr");
					} else {
						agroTransaction.setFinalBalance(Math.abs(agroTransaction.getBalAmount()) + "-Cr");
					}
					agroTransaction.setProcurementDetailList(procuremrementList);
					agroTransaction.setQty(qty);
					if (agroTransaction.getTxnAmount() == 0) {
						agroTransaction = null;
					}
					agroTransactions.add(agroTransaction);
				}
				if (!ObjectUtil.isEmpty(agroTransaction)
						&& agroTransaction.getTxnDesc().equalsIgnoreCase(AgroTransaction.CASH_PAYMENT)) {
					agroTransactions.add(agroTransaction);
				}
			}
			view = DETAIL;
			request.setAttribute(HEADING, getText(DETAIL));
		} else {
			request.setAttribute(HEADING, getText(LIST));
			return LIST;
		}
		return view;
	}

	/**
	 * Gets the filter.
	 * 
	 * @return the filter
	 */
	
	
	public String detailList() throws Exception {
		
		DecimalFormat formatter = new DecimalFormat("0.00");
        String view = "";
          Date sdate =new Date();
        Date edate =new Date();
        Farmer farmer = new Farmer();
        if(StringUtil.isEmpty(super.startDate) && StringUtil.isEmpty(super.endDate)){
      		DateFormat df = new SimpleDateFormat(DateUtil.BALNACEFORMAT);
		sdate = DateUtil.getStartDateWithTime();
		edate = DateUtil.getCurrentDateWithLastTime();
		super.startDate = df.format(sdate);
		super.endDate = df.format(edate);
		daterange = super.startDate + " - " + super.endDate;
		
        }else{
        	DateFormat df = new SimpleDateFormat(DateUtil.BALNACEFORMAT);
    		if (!StringUtil.isEmpty(startDate)) {
    			try {
    				sdate = df.parse( super.startDate);
    				edate = df.parse(super.endDate);
    			} catch (ParseException e) {
    				
    			}
    		}
        	
        	daterange = super.startDate + " - " + super.endDate;
        }
         if (!StringUtil.isEmpty(farmeridSeasonCode)) {
			String farmerIdSeason[] = farmeridSeasonCode.split("-");
			String[] transactionArray = new String[] {PRODUCT_DISTRIBUTION,PROCUREMENT_PRODUCT_ENROLLMENT, PRODUCT_RETURN_FROM_FARMER, PAYMENT_ADAPTER,DISTIBUTION_PAYMENT_TXN };
			
			
			List<AgroTransaction> agroTransactionList = productDistributionService
					.findAgroTxnByFarmerIdTXnTypesSeasonCode(farmerIdSeason[0], farmerIdSeason[1], transactionArray ,sdate,edate);
			
			farmer = farmerService.findFarmerByFarmerId(farmerIdSeason[0]);
			
			farmerName = farmer.getFirstName() +" "+ farmer.getLastName();
			
			Set<ProcurementDetail> procurementDetailSet;
			List<DistributionDetail> distributionList;
			List<DistributionDetail> distributionDetails = new LinkedList<DistributionDetail>();
			List<ProcurementDetail> procuremrementList;
			List<ProcurementDetail> procurmentDetails = new LinkedList<ProcurementDetail>();
			filter = new AgroTransaction();
			String tempBalAmt ="";
			String tempTxnAmt ="";
			String tempIntBalance ="";
			agroTransactions = new LinkedList<AgroTransaction>();
			for (AgroTransaction agroTransaction : agroTransactionList) {
				Double finalBal = agroTransaction.getBalAmount();
				//if (!ObjectUtil.isEmpty(agroTransaction.getDistribution())) {
				if (!ObjectUtil.isEmpty(agroTransaction)&&agroTransaction.getTxnType().equalsIgnoreCase(PRODUCT_DISTRIBUTION)) {
					Double qty = 0.0;
					String item = "";
					
					/*distributionList = new LinkedList<DistributionDetail>(
							agroTransaction.getDistribution().getDistributionDetails());*/

			/*		for (DistributionDetail detail : distributionList) {
						if (finalBal < 0) {
							detail.setIndividulalFinalBal(CurrencyUtil.currencyFormatter(Math.abs(finalBal)) + "-Dr");
						} else {
							detail.setIndividulalFinalBal(CurrencyUtil.currencyFormatter(Math.abs(finalBal)) + "-Cr");
						}

						finalBal = finalBal + detail.getSubTotal();
						detail.setItem(detail.getProduct().getName());

						distributionDetails.add(detail);
					}*/
					agroTransaction.setTxnTimeStr(DateUtil.convertDateToString(agroTransaction.getTxnTime(),
							getGeneralDateFormat().concat(" HH:mm:ss")));
					agroTransaction.setDistributionDetailList(distributionDetails);
					 tempIntBalance =new Double(Math.abs(agroTransaction.getIntBalance())).toString(); 
					 
					agroTransaction.setTempIntBalance(tempIntBalance);
			/*		 if (agroTransaction.getTxnDesc().equalsIgnoreCase(Distribution.DISTRIBUTION_PAYMENT_AMOUNT)){
						
						  tempBalAmt =new Double(formatter.format(Math.abs(agroTransaction.getIntBalance()) - Math.abs(agroTransaction.getTxnAmount()))).toString(); 
							 agroTransaction.setTempBalAmt(tempBalAmt);
						  
					}
					else
						 tempBalAmt =new Double(Math.abs(agroTransaction.getIntBalance()) + Math.abs(agroTransaction.getTxnAmount())).toString(); 
					 
					  agroTransaction.setTempBalAmt(tempBalAmt.replace("-", ""));*/
					tempTxnAmt=new Double(Math.abs(agroTransaction.getTxnAmount())).toString();
					agroTransaction.setTemptxnAmount(tempTxnAmt);
					if (agroTransaction.getBalAmount() < 0) {
						
						agroTransaction.setTempBalAmt(CurrencyUtil.currencyFormatter(Math.abs(agroTransaction.getBalAmount())) + "-Dr");
					} else {
						agroTransaction.setTempBalAmt(CurrencyUtil.currencyFormatter(Math.abs(agroTransaction.getBalAmount())) + "-Cr");
					}
				//	agroTransaction.setDistributionDetailList(distributionList);
					agroTransaction.setQty(qty);
					agroTransaction.setItem(StringUtil.removeLastComma(item));
				/*	if (agroTransaction.getTxnAmount() == 0) {
						agroTransaction = null;
					}*/
					agroTransactions.add(agroTransaction);
				}
				// Procurement codes

				//if (!ObjectUtil.isEmpty(agroTransaction) && !ObjectUtil.isEmpty(agroTransaction.getProcurement())) {
				if (!ObjectUtil.isEmpty(agroTransaction)&&agroTransaction.getTxnType().equalsIgnoreCase(PROCUREMENT_PRODUCT_ENROLLMENT)) {
					Double qty = 0.0;
					String item = "";
					/*procuremrementList = new LinkedList<ProcurementDetail>(
							agroTransaction.getProcurement().getProcurementDetails());

					for (ProcurementDetail detail : procuremrementList) {
						if (finalBal < 0) {
							detail.setIndividulalFinalBal(CurrencyUtil.currencyFormatter(Math.abs(finalBal)) + "-Dr");
						} else {
							detail.setIndividulalFinalBal(CurrencyUtil.currencyFormatter(Math.abs(finalBal)) + "-Cr");
						}
						finalBal = finalBal - detail.getSubTotal();
						detail.setItem(detail.getProcurementProduct().getName());

						procurmentDetails.add(detail);
					}*/
				//	agroTransaction.setProcurementDetailList(procurmentDetails);
					agroTransaction.setTxnTimeStr(DateUtil.convertDateToString(agroTransaction.getTxnTime(),
							getGeneralDateFormat().concat(" HH:mm:ss")));
					 tempIntBalance =new Double(Math.abs(agroTransaction.getIntBalance())).toString(); 
					 
						agroTransaction.setTempIntBalance(tempIntBalance);
					/*if (agroTransaction.getTxnDesc().equalsIgnoreCase(Procurement.PROCUREMENT_AMOUNT)){
						
					 tempBalAmt =new Double(Math.abs(agroTransaction.getIntBalance() - agroTransaction.getTxnAmount())).toString(); 
					 agroTransaction.setTempBalAmt(tempBalAmt);
					}
					else{
						tempBalAmt =new Double(Math.abs(agroTransaction.getIntBalance() + agroTransaction.getTxnAmount())).toString(); 
						 agroTransaction.setTempBalAmt(tempBalAmt);	
					}*/
						tempTxnAmt=new Double(Math.abs(agroTransaction.getTxnAmount())).toString();
						agroTransaction.setTemptxnAmount(tempTxnAmt);
					if (agroTransaction.getBalAmount() < 0) {
						agroTransaction.setTempBalAmt(Math.abs(agroTransaction.getBalAmount()) + "-Dr");
					} else {
						agroTransaction.setTempBalAmt(Math.abs(agroTransaction.getBalAmount()) + "-Cr");
					}
				//	agroTransaction.setProcurementDetailList(procuremrementList);
					agroTransaction.setQty(qty);
				/*	if (agroTransaction.getTxnAmount() == 0) {
						agroTransaction = null;
					}*/
					agroTransactions.add(agroTransaction);
				}
				/*if (!ObjectUtil.isEmpty(agroTransaction)
						&& agroTransaction.getTxnDesc().equalsIgnoreCase(AgroTransaction.CASH_PAYMENT)) {
					agroTransactions.add(agroTransaction);
				}*/
				if(!ObjectUtil.isEmpty(agroTransaction) && (agroTransaction.getTxnType().equalsIgnoreCase(PAYMENT_ADAPTER) || agroTransaction.getTxnType().equalsIgnoreCase(DISTIBUTION_PAYMENT_TXN))){
					agroTransaction.setTxnTimeStr(DateUtil.convertDateToString(agroTransaction.getTxnTime(),
							getGeneralDateFormat().concat(" HH:mm:ss")));
					 agroTransaction.setTxnDesc(agroTransaction.getTxnDesc());
					 tempIntBalance =new Double(Math.abs(agroTransaction.getIntBalance())).toString(); 
						agroTransaction.setTempIntBalance(tempIntBalance);
						tempTxnAmt=new Double(Math.abs(agroTransaction.getTxnAmount())).toString();
						agroTransaction.setTemptxnAmount(tempTxnAmt);
						//tempBalAmt=new Double(Math.abs(agroTransaction.getBalAmount())).toString();
						//agroTransaction.setFinalBalance(tempBalAmt);
						if (agroTransaction.getBalAmount() < 0) {
							agroTransaction.setTempBalAmt(Math.abs(agroTransaction.getBalAmount()) + "-Dr");
						} else {
							agroTransaction.setTempBalAmt(Math.abs(agroTransaction.getBalAmount()) + "-Cr");
						}
						
						agroTransactions.add(agroTransaction);
			}/*if(!ObjectUtil.isEmpty(agroTransaction) && (agroTransaction.getTxnType().equalsIgnoreCase(LOAN_DISBURSEMENT) )){
				agroTransaction.setTxnTimeStr(DateUtil.convertDateToString(agroTransaction.getTxnTime(),
						getGeneralDateFormat().concat(" HH:mm:ss")));
				 agroTransaction.setTxnDesc(agroTransaction.getTxnDesc());
				 tempIntBalance =new Double(Math.abs(agroTransaction.getIntBalance())).toString(); 
					agroTransaction.setTempIntBalance(tempIntBalance);
					tempTxnAmt=new Double(Math.abs(agroTransaction.getTxnAmount())).toString();
					agroTransaction.setTemptxnAmount(tempTxnAmt);
					//tempBalAmt=new Double(Math.abs(agroTransaction.getBalAmount())).toString();
					//agroTransaction.setFinalBalance(tempBalAmt);
					if (agroTransaction.getBalAmount() < 0) {
						agroTransaction.setTempBalAmt(Math.abs(agroTransaction.getBalAmount()) + "-Dr");
					} else {
						agroTransaction.setTempBalAmt(Math.abs(agroTransaction.getBalAmount()) + "-Cr");
					}
					
					agroTransactions.add(agroTransaction);
		}if(!ObjectUtil.isEmpty(agroTransaction) && (agroTransaction.getTxnType().equalsIgnoreCase(LOAN_REPAYMENT) )){
					agroTransaction.setTxnTimeStr(DateUtil.convertDateToString(agroTransaction.getTxnTime(),
							getGeneralDateFormat().concat(" HH:mm:ss")));
					 agroTransaction.setTxnDesc(agroTransaction.getTxnDesc());
					 tempIntBalance =new Double(Math.abs(agroTransaction.getIntBalance())).toString(); 
						agroTransaction.setTempIntBalance(tempIntBalance);
						tempTxnAmt=new Double(Math.abs(agroTransaction.getTxnAmount())).toString();
						agroTransaction.setTemptxnAmount(tempTxnAmt);
						//tempBalAmt=new Double(Math.abs(agroTransaction.getBalAmount())).toString();
						//agroTransaction.setFinalBalance(tempBalAmt);
						if (agroTransaction.getBalAmount() < 0) {
							agroTransaction.setTempBalAmt(Math.abs(agroTransaction.getBalAmount()) + "-Dr");
						} else {
							agroTransaction.setTempBalAmt(Math.abs(agroTransaction.getBalAmount()) + "-Cr");
						}
						
						agroTransactions.add(agroTransaction);
			}*/
			}
			 setCurrentPage(getCurrentPage());
	            command = DETAIL;
	            view = DETAIL;
	            request.setAttribute(HEADING, getText("farmerBalancedetail"));
		} else {
			  request.setAttribute(HEADING, getText("farmerBalancelist"));
	            return LIST;
		}
        
        return view;
    
	}

	/**
	 * Gets the account id.
	 * 
	 * @return the account id
	 */
	public String getAccountId() {

		return accountId;
	}

	public AgroTransaction getFilter() {
		return filter;
	}

	public void setFilter(AgroTransaction filter) {
		this.filter = filter;
	}

	/**
	 * Sets the account id.
	 * 
	 * @param accountId
	 *            the new account id
	 */
	public void setAccountId(String accountId) {

		this.accountId = accountId;
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
	 * Gets the farmers list.
	 * 
	 * @return the farmers list
	 */
	public Map<String, String> getFarmersList() {
		Map<String, String> farmerMap = new LinkedHashMap<String, String>();
		
		farmerService.listFarmerInfo().stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			farmerMap.put(String.valueOf(objArr[1]), String.valueOf(objArr[3]));
		});

		return farmerMap;
	}

/*	public Map<String, String> getAgentLists() {

		Map<String, String> returnMap = new LinkedHashMap<String, String>();
		List<Object[]> agentListArr = agentService.listAgentIdNamebyAgroTxn();
		returnMap = agentListArr.stream()
				.collect(Collectors.toMap(obj -> String.valueOf(obj[0]), obj -> String.valueOf(obj[1])));

		return returnMap;

	}*/
	public void populateAgentList(){
		JSONArray agentArr = new JSONArray();
		List<Object[]> agentListArr = agentService.listAgentIdNamebyAgroTxn();
		if (!ObjectUtil.isEmpty(agentListArr)) {
			agentListArr.forEach(obj -> {
				agentArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(agentArr);
	}

	/**
	 * Populate xls.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	/*
	 * public String populateXLS() throws Exception {
	 * 
	 * InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
	 * setXlsFileName(getText("farmerBalanceReportList") +
	 * fileNameDateFormat.format(new Date())); Map<String, InputStream> fileMap
	 * = new HashMap<String, InputStream>(); fileMap.put(xlsFileName, is);
	 * setFileInputStream(FileUtil.createFileInputStreamToZipFile(
	 * getText("farmerBalanceReportList"), fileMap, ".xls")); return "xls"; }
	 * 
	 */

	public String populateXLS() throws Exception {

		InputStream is = getFarmerBalanceDataStream();

		String temp = getType();
		if (!StringUtil.isEmpty(getType()) && getType().equalsIgnoreCase("farmer")) {
			setXlsFileName(getLocaleProperty("FarmerBalanceReportList") + fileNameDateFormat.format(new Date()));
			Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
			fileMap.put(getLocaleProperty("farmerBalanceReportList") + DateUtil.getRevisionNoDateTimeMilliSec(), is);
			setFileInputStream(FileUtil.createFileInputStreamToZipFile(
					getLocaleProperty("farmerBalanceReportList") + DateUtil.getRevisionNoDateTimeMilliSec(), fileMap, ".xls"));
		} else {
			setXlsFileName(getLocaleProperty("agentBalanceReportList") + fileNameDateFormat.format(new Date()));

			Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
			fileMap.put(getLocaleProperty("agentBalanceReportList") + DateUtil.getRevisionNoDateTimeMilliSec(), is);
			setFileInputStream(FileUtil.createFileInputStreamToZipFile(
					getLocaleProperty("agentBalanceReportList") + DateUtil.getRevisionNoDateTimeMilliSec(), fileMap, ".xls"));
		}
		return "xls";
	}

	private InputStream getFarmerBalanceDataStream() throws IOException, ParseException {
		LinkedHashMap<String, String> filters = new LinkedHashMap<String, String>();
		LinkedList<String> headersList = new LinkedList<String>();
		LinkedList<String> subGridHeadersList = new LinkedList<String>();
		List<List<String>> dataList = new ArrayList<List<String>>();
		List<List<String>> subGridDataList = new ArrayList<List<String>>();
		Map<Long, List<List<String>>> subListMap = new LinkedHashMap<Long, List<List<String>>>();

		// this.gridIdentity = "detail";
		// this.filter.setType(3);
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!ObjectUtil.isEmpty(preferences)) {
			/*
			 * DateFormat genDate=new
			 * SimpleDateFormat(preferences.getPreferences().get(ESESystem.
			 * GENERAL_DATE_FORMAT)); Date sDate=df.parse(getStartDate()); Date
			 * eDate=df.parse(getEndDate()); filters.put(getText("startDate"),
			 * genDate.format(sDate)); filters.put(getText("EndingDate"),
			 * genDate.format(eDate));
			 */
		}
		ESEAccount account = new ESEAccount();
		branchIdValue = getBranchId();
		filter = new AgroTransaction();
		
		if (!StringUtil.isEmpty(getBranchId())) {
		branchIdValue = getBranchId(); // set value for branch id.
		  buildBranchMap(); // build branch map to get branch name form branch id.
		  filter.setBranch_id(branchIdValue);
		}
		
		if (!StringUtil.isEmpty(getSeasonCode())) {
			filter.setSeasonCode(getSeasonCode());
			HarvestSeason season = farmerService.findHarvestSeasonByCode(getSeasonCode());
			filters.put(getText("season"), season.getName());
		} else {
			HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
			Object branchObject = httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
			String currentBranch = ObjectUtil.isEmpty(branchObject) ? ESESystem.SYSTEM_ESE_NAME
					: branchObject.toString();
			ESESystem ese = preferncesService.findPrefernceByOrganisationId(currentBranch);
			String currentSeasonCode = "";
			if (!ObjectUtil.isEmpty(ese)) {
				ESESystem preference = preferncesService.findPrefernceById(String.valueOf(ese.getId()));
				currentSeasonCode = preference.getPreferences().get(ESESystem.CURRENT_SEASON_CODE);

				if (StringUtil.isEmpty(currentSeasonCode)) {
					currentSeasonCode = "";
				}
			}
			filter.setSeasonCode(currentSeasonCode);
			HarvestSeason season = farmerService.findHarvestSeasonByCode(currentSeasonCode);
			filters.put(getText("season"), season.getName());
		}
		if (!StringUtil.isEmpty(getFarmerId())) {
			filter.setFarmerId(getFarmerId());
			Farmer frmr = farmerService.findFarmerByFarmerId(filter.getFarmerId());
			filters.put(getLocaleProperty("farmerName"), frmr.getFirstName() + " " + frmr.getLastName());
		}
		if (!StringUtil.isEmpty(getAgentId())) {
			filter.setAgentId(getAgentId());
			AgroTransaction agroTxn = productDistributionService.findAgentByAgentId(getAgentId());
			if (!ObjectUtil.isEmpty(agroTxn)) {
				filters.put(getText("agent"), agroTxn.getAgentName());
			}
		}
		if (!StringUtil.isEmpty(getTxnType())) {
			filter.setTxnType(getTxnType());
		}
		String headers;
		setTxnType(getTxnType());

		if (!StringUtil.isEmpty(getType()) && getType().equalsIgnoreCase("farmer")) {
			account.setAccountType("3");
			headers = getLocaleProperty("exportFarmerBalanceReportColumnHeader");
		} else {
			account.setAccountType("1");
			headers = getLocaleProperty("exportAgentMobileUserBalanceReportColumnHeader");
		}
		
		if(getCurrentTenantId().equalsIgnoreCase("pratibha")){
			
			 if(getBranchId().equalsIgnoreCase("organic")){
				 if (!StringUtil.isEmpty(getType()) && getType().equalsIgnoreCase("farmer")) {
						account.setAccountType("3");
						headers = getLocaleProperty("OrganicexportFarmerBalanceReportColumnHeader");
					} else {
						account.setAccountType("1");
						headers = getLocaleProperty("OrganicexportAgentMobileUserBalanceReportColumnHeader");
					}
			}else if(getBranchId().equalsIgnoreCase("bci")){
				if (!StringUtil.isEmpty(getType()) && getType().equalsIgnoreCase("farmer")) {
					account.setAccountType("3");
					headers = getLocaleProperty("BCIexportFarmerBalanceReportColumnHeader");
				} else {
					account.setAccountType("1");
					headers = getLocaleProperty("BCIexportAgentMobileUserBalanceReportColumnHeader");
				}
			}
			
		}
		 if (!StringUtil.isEmpty(getType()) && getType().equalsIgnoreCase("farmer")) {
		if(getCurrentTenantId().equalsIgnoreCase("susagri")){
			if(!StringUtil.isEmpty(samithi)){
				filter.setSamithiId(Long.valueOf(samithi));	
			}
			
		}
		 }
		filter.setAccount(account);
		super.filter = this.filter;
		Map datas = readData("farmerBalance");

		String subGridHeaders = getLocaleProperty("exportFarmerBalanceReportSubColumnHeader");
		if (headers != null) {

			for (String name : headers.split(",")) {
				
				if(name.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT))				
				{				
					name=name.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());				
				}else if(name.trim().contains(ESESystem.AREA_TYPE_EXPORT))						
				{						
					name=name.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());						
				}			
				
				
				headersList.add(getLocaleProperty(name));
			}
		}

		if (subGridHeaders != null) {
			for (String name : subGridHeaders.split(",")) {
				
				if(name.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT))				
				{				
					name=name.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());				
				}else if(name.trim().contains(ESESystem.AREA_TYPE_EXPORT))						
				{						
					name=name.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());						
				}			
				
				
				subGridHeadersList.add(getLocaleProperty(name));
			}
		}
		for (Object record : (List) datas.get(ROWS)) {
			List<String> list = getDataForReport(record);

			filter = new AgroTransaction();

			filter.setId(Long.valueOf(list.get(0)));
			// filter.setTxnType(list.get(5));

			HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
			Object branchObject = httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
			String currentBranch = ObjectUtil.isEmpty(branchObject) ? ESESystem.SYSTEM_ESE_NAME
					: branchObject.toString();
			ESESystem ese = preferncesService.findPrefernceByOrganisationId(currentBranch);
			String currentSeasonCode = "";
			if (!ObjectUtil.isEmpty(ese)) {
				ESESystem preference = preferncesService.findPrefernceById(String.valueOf(ese.getId()));
				currentSeasonCode = preference.getPreferences().get(ESESystem.CURRENT_SEASON_CODE);

				if (StringUtil.isEmpty(currentSeasonCode)) {
					currentSeasonCode = "";
				}

				filter.setSeasonCode(currentSeasonCode);
			}
			super.filter = this.filter;
			Map data = readData();

			for (Object record1 : (List) data.get(ROWS)) {
				List<String> subList = getSubGridDataForReport(record1);
				if (subListMap.containsKey(Long.valueOf(list.get(0)))) {
					subListMap.get(Long.valueOf(list.get(0))).add(subList);
				} else {
					List<List<String>> newList = new ArrayList<List<String>>();
					newList.add(subList);
					subListMap.put(Long.parseLong(list.get(0)), newList);
				}
			}

			list.remove(list.size() - 1);
			dataList.add(list);

		}

		Asset existingAssetLogin = clientService.findAssetsByAssetCode(Asset.APP_LOGO);
		String title = getText("farmerBalanceReportList");
		String reportName = getText("exportFarmerBalanceTitle");
		if (!StringUtil.isEmpty(getType()) && getType().equalsIgnoreCase("agent")) {
			title = getLocaleProperty("agentBalanceReportList");
			reportName = getLocaleProperty("exportAgentBalanceTitle");
		}

		InputStream stream = ExportUtil.exportXLSWithSubGrid(dataList, headersList, filters, title, getText("filter"),
				reportName, existingAssetLogin.getFile(), subGridHeadersList, subListMap);
	

		return stream;

	}

	
	Long serialNumber = 0L;
	
	public List<String> getDataForReport(Object obj) {
		List<String> dataList = new ArrayList<>();
		Object[] datas = (Object[]) obj;
		
		Farmer f = null;
		
		dataList.add(datas[0].toString());
	
		
		if (datas.length > 0) {
			
			serialNumber++;		
			dataList.add(String.valueOf(serialNumber));
			
			if (!StringUtil.isEmpty(datas[2])) {
				/*if (StringUtil.isLong(datas[2])) {
					f = farmerService.findFarmerById(Long.valueOf(datas[2].toString()));
				} else {
					f = farmerService.findFarmerByFarmerId(String.valueOf(datas[2].toString()));
				}*/
				f = farmerService.findFarmerByFarmerId(String.valueOf(datas[2].toString()));
			}
			
			
			/*if (!StringUtil.isEmpty(getType()) && getType().equalsIgnoreCase("farmer")) {
				f = farmerService.findFarmerByFarmerId(String.valueOf(datas[2]));
				f = farmerService.findFarmerByFarmerId(String.valueOf(datas[2].toString()));
			}*/

			/*if (f != null) {
				dataList.add(String.valueOf(datas[8]));
				dataList.add(String.valueOf(datas[9]));
			} else {
				dataList.add(String.valueOf(datas[7]));
				dataList.add(String.valueOf(datas[6]));
			}*/
			
			if (f != null) {		
				dataList.add(f.getFarmerId());		
				if (f.getLastName() != null){
					
					dataList.add(f.getFirstName() + " " + f.getLastName());
					
					}else{
						dataList.add(f.getFirstName());
					}
										
			} else {		
				dataList.add(String.valueOf(datas[7]));		
				dataList.add(String.valueOf(datas[6]));		
			}
			if(getCurrentTenantId().equalsIgnoreCase("susagri")){
				dataList.add(!ObjectUtil.isEmpty(f) && !ObjectUtil.isEmpty(f.getSamithi())?f.getSamithi().getName() : "NA");
			}
			
			/*
			 * rows.add(String.valueOf(datas[2]));
			 * 
			 * rows.add(String.valueOf(datas[3]));
			 */
			Double bal = Double.valueOf(datas[3].toString());
			if (bal >= 0) {
				dataList.add(String.valueOf(Math.abs(bal)) + "-" + "CR");
			} else {
				dataList.add(String.valueOf(Math.abs(bal)) + "-" + "DR");
			}
		}
		dataList.add(String.valueOf(datas[4].toString()));
		// dataList.add(String.valueOf(datas[5].toString()));
		return dataList;
	}

	public List<String> getSubGridDataForReport(Object obj) {

		List<String> subGridDataList = new ArrayList<>();

		AgroTransaction agroTransaction = (AgroTransaction) obj;
		// JSONArray rows = new JSONArray();
		if (!ObjectUtil.isEmpty(agroTransaction)) {
			// subGridDataList.add(String.valueOf(agroTransaction.getTxnTime()));
			subGridDataList.add(DateUtil.convertDateToString(agroTransaction.getTxnTime(),
					getGeneralDateFormat().concat(" HH:mm:ss")));
			subGridDataList.add(agroTransaction.getReceiptNo());
			subGridDataList.add(agroTransaction.getTxnDesc());
			subGridDataList.add(String.valueOf(agroTransaction.getIntBalance()));
			subGridDataList.add(String.valueOf(agroTransaction.getTxnAmount()));
			if (agroTransaction.getBalAmount() >= 0) {
				subGridDataList.add(String.valueOf(Math.abs(agroTransaction.getBalAmount())) + "-" + "CR");
			} else {
				subGridDataList.add(String.valueOf(Math.abs(agroTransaction.getBalAmount())) + "-" + "DR");
			}
		}

		return subGridDataList;
	}

	public Map<String, String> getSeasonList() {

		Map<String, String> seasonListListMap = new LinkedHashMap<String, String>();

		List<HarvestSeason> seasonList = farmerService.listHarvestSeasons();

		for (HarvestSeason obj : seasonList) {
			seasonListListMap.put(obj.getCode(), obj.getName());
		}
		return seasonListListMap;

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

		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		setsDate(IExporter.EXPORT_DAILY.equalsIgnoreCase(exportType) ? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));
		boolean flag = true;
		DateFormat filterDateFormat = new SimpleDateFormat(getGeneralDateFormat());
		int balType;

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("exportFarmerBalanceTitle"));
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

		HSSFRow row, titleRow, filterRowTitle, filterRow1, filterRow2, filterRow3, filterRow4 = null;
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
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("exportFarmerBalanceTitle")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);

		rowNum++;

		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		String mainGridColumnHeaders = getLocaleProperty("exportColumnHeader");
		int mainGridIterator = 0;

		filterRow4 = sheet.createRow(rowNum++);

		/* if (!ObjectUtil.isEmpty(filter.getProfileId()) */
		// && flag)
		{
			Farmer f = new Farmer();
			// Farmer f =
			// farmerService.findFarmerByFarmerId(filter.getProfileId());
			cell = filterRow4.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmer")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);
			if (f != null) {

				cell = filterRow4.createCell(2);
				cell.setCellValue(new HSSFRichTextString(String.valueOf(f.getFarmerId()) + " - "
						+ String.valueOf(f.getFirstName() + " " + f.getLastName())));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			} else {
				cell = filterRow4.createCell(2);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("NA")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			flag = false;
		}

		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {

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

		if (ObjectUtil.isEmpty(this.filter))
			this.gridIdentity = "detail";
		// this.filter.setType(3);
		super.filter = this.filter;

		setSord("desc");
		setSidx("id");

		super.filter = this.filter;

		Map data = isMailExport() ? readData() : readExportData();

		List<FarmerBalanceReport> mainGridRows = (List<FarmerBalanceReport>) data.get(ROWS);
		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;

		for (Object obj : mainGridRows) {

			ESEAccount object = (ESEAccount) obj;
			Farmer f = farmerService.findFarmerByFarmerId(object.getProfileId());

			if (f != null) {
				row = sheet.createRow(rowNum++);
				colNum = 0;
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(f.getFarmerId()));
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(f.getFirstName() + " " + f.getLastName()));

				if (object.getCashBalance() < 0) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							CurrencyUtil.getDecimalFormat((object.getCashBalance()), "##.000").substring(1) + "Dr"));
				} else {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							CurrencyUtil.getDecimalFormat((object.getCashBalance()), "##.000") + "Cr"));
				}

				if (object.getCreditBalance() < 0) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							CurrencyUtil.getDecimalFormat((object.getCreditBalance()), "##.000").substring(1) + "Dr"));
				} else {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							CurrencyUtil.getDecimalFormat((object.getCreditBalance()), "##.000") + "Cr"));
				}
			}

		}
		for (int i = 0; i <= colNum; i++) {
			sheet.autoSizeColumn(i);
		}
		int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(400, 10, 655, 200, (short) 0, 0, (short) 0, 0);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		picture.resize();
		String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
				: request.getSession().getId();
		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("agentBalanceReportList") + fileNameDateFormat.format(new Date()) + ".xls";
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

	public IProductDistributionService getProductDistributionService() {

		return productDistributionService;
	}

	public void setProductDistributionService(IProductDistributionService productDistributionService) {

		this.productDistributionService = productDistributionService;
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

	public String getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public IAgentService getAgentService() {
		return agentService;
	}

	public void setAgentService(IAgentService agentService) {
		this.agentService = agentService;
	}

	public void setAgentLists(Map<String, String> agentLists) {
		this.agentLists = agentLists;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getDaterange() {
		return daterange;
	}

	public void setDaterange(String daterange) {
		this.daterange = daterange;
	}

	public Map<String, String> getFields() {
		return fields;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}

	public String getFarmeridSeasonCode() {
		return farmeridSeasonCode;
	}

	public void setFarmeridSeasonCode(String farmeridSeasonCode) {
		this.farmeridSeasonCode = farmeridSeasonCode;
	}

	public List<AgroTransaction> getAgroTransactions() {
		return agroTransactions;
	}

	public void setAgroTransactions(List<AgroTransaction> agroTransactions) {
		this.agroTransactions = agroTransactions;
	}

	public String getSeasonCode() {
		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getFarmerName() {
		return farmerName;
	}

	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
	}
	public Map<String, String> getSamithiList() {
		Map<String, String> warehouseMap = new LinkedHashMap<>();
		warehouseMap = locationService.listOfSamithi().stream()
				.collect(Collectors.toMap(warehouse -> String.valueOf(warehouse.getId()), Warehouse::getName));
		return warehouseMap;
	}

	public ILocationService getLocationService() {
		return locationService;
	}

	public void setLocationService(ILocationService locationService) {
		this.locationService = locationService;
	}

	public String getSamithi() {
		return samithi;
	}

	public void setSamithi(String samithi) {
		this.samithi = samithi;
	}

	public String getEnableLoanModule() {
		return enableLoanModule;
	}

	public void setEnableLoanModule(String enableLoanModule) {
		this.enableLoanModule = enableLoanModule;
	}
	
}