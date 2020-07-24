/*
 * ProcurementProductAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.eclipse.jdt.internal.core.index.FileIndexLocation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.ese.entity.util.LoanLedger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sourcetrace.eses.dao.IProductDistributionDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.AgentType;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.entity.WarehouseProduct;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.order.entity.profile.SubCategory;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.CityWarehouse;
import com.sourcetrace.eses.order.entity.txn.DistributionDetail;
import com.sourcetrace.eses.order.entity.txn.LoanApplication;
import com.sourcetrace.eses.order.entity.txn.LoanDistribution;
import com.sourcetrace.eses.order.entity.txn.LoanDistributionDetail;
import com.sourcetrace.eses.order.entity.txn.PMTFarmerDetail;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.order.entity.txn.ProcurementDetail;
import com.sourcetrace.eses.order.entity.txn.SupplierProcurement;
import com.sourcetrace.eses.order.entity.txn.SupplierProcurementDetail;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.txn.exception.OfflineTransactionException;
import com.sourcetrace.eses.umgmt.entity.Menu;
import com.sourcetrace.eses.umgmt.entity.User;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.profile.Product;
import com.sourcetrace.esesw.entity.profile.DistributionBalance;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.profile.Vendor;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.view.WebTransactionAction;

@SuppressWarnings("unchecked")
public class LoanDistributionAction extends WebTransactionAction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(LoanDistributionAction.class);

	public static final String LOAN_DISTRIBUTION = "701";
	private static final String QUANTITY = "quantity";
	private static final String TOTAL_AMT="totalAmt";
	private static final String PRODUCT_CODE="productCode";
	
	private String id;
	protected int page;
	protected int rows;
	protected String sidx;
	protected String sord;
	protected Date sDate = null;
	protected Date eDate = null;
	private String selectedCity;
	private String selectedVillage;
	private String selectedProduct;
	private String selectedLoanTo;
	private String selectedLoanCategory;
	private String selectedWarehouse;
	private String selectedDate;	
	private String selectedFarmer;
	private String productTotalString;
	private double subsidyInterest;
	private double subsidyAmt;
	private double costToFarmer;
	private double currentQty;
	private double currentAmt;
	private double downPaymentQty;
	private double downPaymentAmt;
	private double interestPercentage;
	private double interestAmt;
	private double totalCost;
	private String loanTenure;
	private double monthyRepayment;
	private double loanTenureAmt;
	private String LStatus;
	private String productCode;
	private String selectedCategory;
	private String selectedCProduct;
	private String selectedVendor;
	private String vendorName;
		/** Service Injection */
	@Autowired
	private ILocationService locationService;
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private IProductDistributionService productDistributionService;
	@Autowired
	private IAccountService accountService;
	@Autowired
	private IUniqueIDGenerator idGenerator;
	@Autowired
	private IPreferencesService preferncesService;
	@Autowired
	private IProductDistributionDAO productDistributionDAO;
	@Autowired
	private IProductService productService;	
	private LoanDistribution loanDistribution;	
	List<Warehouse> samithi = new ArrayList<Warehouse>();
	private Map<String, String> productList = new LinkedHashMap<String, String>();
	private Map<String, String> loanStatusList = new LinkedHashMap<String, String>();
	private List<LoanDistributionDetail> distributionDetailList;
	private String distributionDetails;
	private Map<String, String> farmersList = new LinkedHashMap<>();
	@SuppressWarnings("rawtypes")
	public String data() throws Exception {
		getJQGridRequestParam();
		filter = new LoanDistribution();
		if(getVendorsList().size() == 1){
			getVendorsList().entrySet().stream().forEach(i ->{
			Vendor vendor=new Vendor();
				((LoanDistribution) filter).setVendor(vendor);
				((LoanDistribution) filter).getVendor().setVendorId(i.getKey());
			});
		}
		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		return sendJQGridJSONResponse(data);
	}

	public JSONObject toJSON(Object obj) {
		try {
			LoanDistribution loanDistribution = (LoanDistribution) obj;
			JSONObject jsonObject = new JSONObject();
			JSONArray rows = new JSONArray();
			DateFormat genDate = new SimpleDateFormat(
					preferncesService.findPrefernceByName(ESESystem.GENERAL_DATE_FORMAT));
			rows.add(String.valueOf(genDate.format(loanDistribution.getLoanDate())));
			rows.add(!ObjectUtil.isEmpty(loanDistribution.getAgroTransaction()) && !ObjectUtil.isEmpty(loanDistribution.getAgroTransaction().getAccount())
					? (!StringUtil.isEmpty(loanDistribution.getAgroTransaction().getAccount().getLoanAccountNo()) ?loanDistribution.getAgroTransaction().getAccount().getLoanAccountNo() : "") : "");
			rows.add(!ObjectUtil.isEmpty(loanDistribution.getVendor()) ? loanDistribution.getVendor().getVendorName() : "NA");
			if(!StringUtil.isEmpty(loanDistribution.getFarmer().getProofNo())){
			rows.add(!StringUtil.isEmpty(loanDistribution.getFarmer()) ? "<a href='#' onclick='showTaskDetail(\""+loanDistribution.getFarmer().getId()+"\",\"308\")'>" +"<b><u>"+loanDistribution.getFarmer().getName()+" - "+(!StringUtil.isEmpty(loanDistribution.getFarmer().getProofNo())?loanDistribution.getFarmer().getProofNo():"")+"</u></b>"+ "</a>" : "");
			}else{
		    rows.add(!StringUtil.isEmpty(loanDistribution.getFarmer()) ? "<a href='#' onclick='showTaskDetail(\""+loanDistribution.getFarmer().getId()+"\",\"308\")'>" +"<b><u>"+loanDistribution.getFarmer().getName()+"</u></b>"+ "</a>" : "");
			}
		/*	if (!StringUtil.isEmpty(loanDistribution.getVillage())) {
				rows.add(loanDistribution.getVillage().getName());
			} else {
				rows.add("N/A");
			}*/
			if (!StringUtil.isEmpty(loanDistribution.getLoanStatus())) {
				rows.add(getLoanStatusList().get(String.valueOf(loanDistribution.getLoanStatus())));
			} else {
				rows.add("N/A");
			}
			Map<String, Object> details = getTotalLoanDistributionDetails(loanDistribution);
			User us=userService.isValidESEUser(getUsername());
			/*rows.add("<button class=\"fa fa-pencil-square-o\"\"aria-hidden=\"true\"\" onclick=\"popupFarmerData('"
					+ loanDistribution.getId() + "~"+loanDistribution.getLoanStatus()+ "~"+details.get(PRODUCT_CODE)+"~"+us.getTypes()+"')\"></button>");*/
		
			jsonObject.put("id", loanDistribution.getId());
			jsonObject.put("cell", rows);
			return jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String create() {
		
		return INPUT;
	}

	public String detail() throws Exception {
		if (id != null && !id.equals("")) {
			loanDistribution = productDistributionService.findLoanDistributionById(Long.valueOf(id));
			distributionDetailList = new LinkedList<LoanDistributionDetail>();
			for (LoanDistributionDetail distributionDetail : loanDistribution.getLoanDistributionDetail()) {
				LoanDistributionDetail distributionTemp = distributionDetail;
				distributionTemp
						.setQuantity(distributionDetail.getQuantity());

				distributionTemp
						.setAmount(distributionDetail.getAmount());

				distributionDetailList.add(distributionTemp);
			}
		}
		return DETAIL;
	}
	 public String list() throws Exception {

	        if (getCurrentPage() != null) {
	            setCurrentPage(getCurrentPage());
	        }
	        request.setAttribute(HEADING, getText("harvestSeasonlist"));
	       
	        return LIST;
	    }

	public void populateLoanDistribution() {		
		String receiptNumber;		
		/** Form Procurement Detail Object */
		Set<LoanDistributionDetail> loanDistributionDetails = formLoanDistributionDetails();
		LoanDistribution loanDistribution = new LoanDistribution();
		loanDistribution.setLoanDistributionDetail(loanDistributionDetails);
		//loanDistribution.setLoanAccNo(idGenerator.getLoanAccountNoSeq());
		loanDistribution.setLoanDate(DateUtil.convertStringToDate(this.selectedDate, getGeneralDateFormat()));
		//loanDistribution.setLoanTo(Integer.parseInt(selectedLoanTo));
		if(!StringUtil.isEmpty(selectedVillage)){
		Village village = locationService.findVillageById(Long.valueOf(selectedVillage));
		loanDistribution.setVillage(village);
		}else{
			loanDistribution.setVillage(null);
		}
		if(!StringUtil.isEmpty(selectedFarmer)){
			Farmer farmer = farmerService.findFarmerById(Long.valueOf(selectedFarmer));
			loanDistribution.setFarmer(farmer);
			}else{
				
				loanDistribution.setFarmer(null);
			}
		
		if(!StringUtil.isEmpty(selectedVendor)){
			Vendor vendor = clientService.findVendorById(selectedVendor);
			loanDistribution.setVendor(vendor);
			setVendorName(vendor.getVendorName());
			}else{
				
				loanDistribution.setVendor(null);
			}
		
		if(!StringUtil.isEmpty(selectedWarehouse)){
			Farmer farmer = farmerService.findGroupById(Long.valueOf(selectedWarehouse));
		loanDistribution.setGroup(farmer);
		}else{
			loanDistribution.setGroup(null);
		}
		//loanDistribution.setLoanCategory(Integer.parseInt(getSelectedLoanCategory()));
		loanDistribution.setCreatedUser(getUsername());
		loanDistribution.setCreatedDate(DateUtil.convertStringToDate(this.selectedDate, getGeneralDateFormat()));
		//loanDistribution.setCostToFarmer(StringUtil.isDouble(getCostToFarmer()) ? getCostToFarmer() : 0D);
		//loanDistribution.setCurrentQty(StringUtil.isDouble(getCurrentQty()) ? Double.valueOf(getCurrentQty()) : 0D);
		loanDistribution.setTotalCostToFarmer(StringUtil.isDouble(getTotalCost()) ? Double.valueOf(getTotalCost()) : 0D);
		loanDistribution.setDownPaymentAmt(StringUtil.isDouble(getDownPaymentAmt()) ? Double.valueOf(getDownPaymentAmt()) : 0D);
		loanDistribution.setDownPaymentQty(StringUtil.isDouble(getDownPaymentQty()) ? Double.valueOf(getDownPaymentQty()) : 0D);
		loanDistribution.setInterestPercentage(StringUtil.isDouble(getInterestPercentage()) ? Double.valueOf(getInterestPercentage()) : 0D);
		loanDistribution.setInterestAmt(StringUtil.isDouble(getInterestAmt()) ? Double.valueOf(getInterestAmt()) : 0D);
		loanDistribution.setLoanTenure(Integer.parseInt(getLoanTenure()));
		loanDistribution.setLoanTenureAmt(StringUtil.isDouble(getLoanTenureAmt()) ? getLoanTenureAmt() : 0D);
		loanDistribution.setMonthlyRepaymentAmt(StringUtil.isDouble(getMonthyRepayment()) ? Double.valueOf(getMonthyRepayment()) : 0D);		
		loanDistribution.setAgroTransaction(formAgroTxn(loanDistribution));
		loanDistribution.setLoanStatus(0);
		//processLoanDistribution(loanDistribution);
		loanDistribution.setBranchId(getBranchId());
		//productDistributionService.save(loanDistribution);
		productDistributionService.addLoanDistribution(loanDistribution);
		if (!ObjectUtil.isEmpty(loanDistribution) && !ObjectUtil.isEmpty(loanDistribution.getAgroTransaction())
				&& !StringUtil.isEmpty(loanDistribution.getAgroTransaction().getReceiptNo()))
			receiptNumber = loanDistribution.getAgroTransaction().getReceiptNo();
		
		JSONArray respArr = new JSONArray();
		respArr.add(getJSONObject("data", "success"));
		respArr.add(getJSONObject("receiptNumber", loanDistribution.getAgroTransaction().getReceiptNo()));
		sendAjaxResponse(respArr);
	}

	

	private Set<LoanDistributionDetail> formLoanDistributionDetails() {
		Set<LoanDistributionDetail> loanDistributionDetails = new LinkedHashSet<>();
	
		if (!StringUtil.isEmpty(getProductTotalString())) {
			List<String> productsList = Arrays.asList(getProductTotalString().split("@"));

			productsList.stream().filter(obj -> !StringUtil.isEmpty(obj)).forEach(products -> {
				LoanDistributionDetail loanDistributionDetail = new LoanDistributionDetail();
				List<String> list = Arrays.asList(products.split("#"));
			//FarmCatalogue catalogue = getCatlogueValueByCode(list.get(0).toString());
				Product product = productService.findProductById(Long.valueOf(list.get(0)));
				if(!ObjectUtil.isEmpty(product)){
				loanDistributionDetail.setProduct(product);}
				//loanDistributionDetail.setProductCode(list.get(0).toString());
				loanDistributionDetail.setRatePerUnit(!ObjectUtil.isEmpty(list.get(1)) ?Double.valueOf(list.get(1).toString()) : 0.00);
				loanDistributionDetail.setQuantity(!ObjectUtil.isEmpty(list.get(2)) ?Double.valueOf(list.get(2).toString()) : 0.00);
				loanDistributionDetail.setAmount(!ObjectUtil.isEmpty(list.get(3)) ? Double.valueOf(list.get(3).toString()) : 0.00);
				loanDistributionDetail.setTotalAmt(!ObjectUtil.isEmpty(list.get(4)) ? Double.valueOf(list.get(4).toString()) : 0.00);
				//loanDistributionDetail.setSubsidyInterest(!ObjectUtil.isEmpty(list.get(5)) ? Double.valueOf(list.get(5).toString()) :0.00);
				//loanDistributionDetail.setSubsidyAmt(!ObjectUtil.isEmpty(list.get(6)) ? Double.valueOf(list.get(6).toString()) :0.00);
				loanDistributionDetails.add(loanDistributionDetail);
			});

		}

		return loanDistributionDetails;
	}

	private AgroTransaction formAgroTxn(LoanDistribution loanDistribution) {

		AgroTransaction agroTransaction = new AgroTransaction();
		agroTransaction.setDeviceId(NOT_APPLICABLE);
		agroTransaction.setDeviceName(NOT_APPLICABLE);
		agroTransaction.setBranch_id(getBranchId());
		agroTransaction.setVendorId(loanDistribution.getVendor().getVendorId());
		agroTransaction.setVendorName(loanDistribution.getVendor().getVendorName());
		if (!StringUtil.isEmpty(this.selectedDate)) {
			try {
				agroTransaction.setTxnTime(DateUtil.convertStringToDate(this.selectedDate, getGeneralDateFormat()));
				
			} catch (Exception e) {
				String nowDate = DateUtil.convertDateToString(new Date(), getGeneralDateFormat());
				agroTransaction.setTxnTime(DateUtil.convertStringToDate(nowDate, getGeneralDateFormat()));
			}
		}
		DateFormat genDate = new SimpleDateFormat(
				preferncesService.findPrefernceByName(ESESystem.GENERAL_DATE_FORMAT));
		

		String nowDate = DateUtil.convertDateToString(new Date(), getGeneralDateFormat());
	//	agroTransaction.setTxnTime(DateUtil.convertStringToDate(nowDate, getGeneralDateFormat()));
		//FarmCatalogue catalogue = catalogueService.findCatalogueByCode(loanDistribution.getLoanDistributionDetail().iterator().next().getProductCode());
		agroTransaction.setTxnType(LOAN_DISTRIBUTION);
		agroTransaction.setProfType(Profile.CLIENT);
		agroTransaction.setOperType(ESETxn.ON_LINE);
		agroTransaction.setTxnDesc(LoanDistribution.LOAN_DISTRIBUTION_AMOUNT +" | "+loanDistribution.getVendor().getVendorName() +" | "+String.valueOf(genDate.format(loanDistribution.getLoanDate())));
		//agroTransaction.setTxnAmount(loanDistribution.getTotalCostToFarmer());

		//Warehouse warehouse = locationService.findSamithiByCode(selectedWarehouse);		
		//agroTransaction.setSamithi(warehouse);
//if(loanDistribution.getLoanTo()==1){
	/*	Farmer farmer = null;
		if (!StringUtil.isEmpty(loanDistribution.getFarmer())) {
			farmer = loanDistribution.getFarmer();
		}*/
		if (!ObjectUtil.isEmpty(loanDistribution.getFarmer().getFarmerId())) {
			agroTransaction.setFarmerId(loanDistribution.getFarmer().getFarmerId());
			
			agroTransaction.setFarmerName(!ObjectUtil.isEmpty(loanDistribution.getFarmer().getLastName()) ? loanDistribution.getFarmer().getFirstName() + " " + loanDistribution.getFarmer().getLastName() : loanDistribution.getFarmer().getFirstName() );
		} else {
			agroTransaction.setFarmerId(NOT_APPLICABLE);
			agroTransaction.setFarmerName("");
		}
		

		Double bal;
		if (!ObjectUtil.isEmpty(loanDistribution.getFarmer()) && !StringUtil.isEmpty(loanDistribution.getFarmer().getFarmerId())) {
			ESEAccount farmerAccount = productDistributionDAO.findESEAccountByProfileId(loanDistribution.getFarmer().getFarmerId(),ESEAccount.CONTRACT_ACCOUNT);
			if(!ObjectUtil.isEmpty(farmerAccount)) {
				bal = farmerAccount.getOutstandingLoanAmount();
				agroTransaction.setIntBalance(bal);
				agroTransaction.setTxnAmount(StringUtil.isDouble(loanDistribution.getTotalCostToFarmer()) ? Double.valueOf(loanDistribution.getTotalCostToFarmer()) : 0D);
				agroTransaction.setBalAmount(bal + loanDistribution.getTotalCostToFarmer());
				agroTransaction.setAccount(farmerAccount);
			}else {
				agroTransaction.setIntBalance(0.00);
				agroTransaction.setTxnAmount(StringUtil.isDouble(loanDistribution.getTotalCostToFarmer()) ? Double.valueOf(loanDistribution.getTotalCostToFarmer()) : 0D);
				agroTransaction.setBalAmount(0.00);
			}
			
		} else {
			agroTransaction.setIntBalance(0.00);
			agroTransaction.setTxnAmount(StringUtil.isDouble(loanDistribution.getTotalCostToFarmer()) ? Double.valueOf(loanDistribution.getTotalCostToFarmer()) : 0D);
			agroTransaction.setBalAmount(0.00);
			//txn.setAccount(farmerAccount);
		}
		if (!ObjectUtil.isEmpty(loanDistribution.getVendor()) && !StringUtil.isEmpty(loanDistribution.getVendor().getVendorId())) {
			ESEAccount farmerAccount = productDistributionDAO.findESEAccountByProfileId(loanDistribution.getVendor().getVendorId(),ESEAccount.VENDOR_ACCOUNT);
			if(!ObjectUtil.isEmpty(farmerAccount)) {
				bal = farmerAccount.getOutstandingLoanAmount();
				agroTransaction.setVendorIntBalance(bal);
				agroTransaction.setVendorTxnAmount(StringUtil.isDouble(loanDistribution.getTotalCostToFarmer()) ? Double.valueOf(loanDistribution.getTotalCostToFarmer()) : 0D);
				agroTransaction.setVendorBalAmount(bal + loanDistribution.getTotalCostToFarmer());
				agroTransaction.setAccount(farmerAccount);
			}else {
				agroTransaction.setVendorIntBalance(0.00);
				agroTransaction.setVendorTxnAmount(StringUtil.isDouble(loanDistribution.getTotalCostToFarmer()) ? Double.valueOf(loanDistribution.getTotalCostToFarmer()) : 0D);
				agroTransaction.setVendorBalAmount(0.00);
				//txn.setAccount(farmerAccount);
			}
			
		} else {
			agroTransaction.setVendorIntBalance(0.00);
			agroTransaction.setVendorTxnAmount(StringUtil.isDouble(loanDistribution.getTotalCostToFarmer()) ? Double.valueOf(loanDistribution.getTotalCostToFarmer()) : 0D);
			agroTransaction.setVendorBalAmount(0.00);
			//txn.setAccount(farmerAccount);
		}
/*		}
		else{
			if(!StringUtil.isEmpty(selectedWarehouse)){
				Farmer farmer = farmerService.findFarmerById(Long.valueOf(selectedWarehouse));
			
			if (!ObjectUtil.isEmpty(farmer)) {
				agroTransaction.setFarmerId(farmer.getFarmerId());
				
				agroTransaction.setFarmerName(!ObjectUtil.isEmpty(farmer.getSurName()) ? farmer.getFirstName() + " " + farmer.getSurName() : farmer.getFirstName() );
			} else {
				agroTransaction.setFarmerId(NOT_APPLICABLE);
				agroTransaction.setFarmerName("");
			}
			
			}
			Double bal;
			if (!ObjectUtil.isEmpty(loanDistribution.getGroup()) && !StringUtil.isEmpty(loanDistribution.getGroup().getFarmerId())) {
				ESEAccount farmerAccount = productDistributionDAO.findESEAccountByProfileId(loanDistribution.getGroup().getFarmerId(),ESEAccount.CONTRACT_ACCOUNT);
				bal = farmerAccount.getCashBalance();
				agroTransaction.setIntBalance(bal);
				agroTransaction.setTxnAmount(StringUtil.isDouble(loanDistribution.getTotalCostToFarmer()) ? Double.valueOf(loanDistribution.getTotalCostToFarmer()) : 0D);
				agroTransaction.setBalAmount(bal + loanDistribution.getTotalCostToFarmer());
				agroTransaction.setAccount(farmerAccount);
			} else {
				agroTransaction.setIntBalance(0.00);
				agroTransaction.setTxnAmount(StringUtil.isDouble(loanDistribution.getTotalCostToFarmer()) ? Double.valueOf(loanDistribution.getTotalCostToFarmer()) : 0D);
				agroTransaction.setBalAmount(0.00);
				//txn.setAccount(farmerAccount);
			}
			
		}*/
		agroTransaction.setSeasonCode(getCurrentSeasonsCode());
		agroTransaction.setReceiptNo(idGenerator.getLoanDistributionReceiptNoSeq());
	
		return agroTransaction;
	
		
	}
	private AgroTransaction processLoanDistribution(LoanDistribution loanDistribution) {


		AgroTransaction txn = new AgroTransaction();
		txn.setDeviceId(NOT_APPLICABLE);
		txn.setDeviceName(NOT_APPLICABLE);
		txn.setBranch_id(getBranchId());
		if (!StringUtil.isEmpty(this.selectedDate)) {
			try {
				txn.setTxnTime(DateUtil.convertStringToDate(this.selectedDate, getGeneralDateFormat()));
			
			} catch (Exception e) {
				String date = DateUtil.convertDateToString(new Date(), getGeneralDateFormat());
				txn.setTxnTime(DateUtil.convertStringToDate(date, getGeneralDateFormat()));

			}
		}
		//FarmCatalogue catalogue = catalogueService.findCatalogueByCode(loanDistribution.getLoanDistributionDetail().iterator().next().getProductCode());
		txn.setTxnType(LOAN_DISTRIBUTION);
		txn.setProfType(Profile.CLIENT);
		txn.setOperType(ESETxn.ON_LINE);
		txn.setTxnDesc(LoanDistribution.LOAN_DISTRIBUTION_AMOUNT);
		
		Double bal;
		if (!ObjectUtil.isEmpty(loanDistribution.getFarmer()) && !StringUtil.isEmpty(loanDistribution.getFarmer().getFarmerId())) {
			ESEAccount farmerAccount = productDistributionDAO.findESEAccountByProfileId(loanDistribution.getFarmer().getFarmerId(),ESEAccount.CONTRACT_ACCOUNT);
			if(!ObjectUtil.isEmpty(farmerAccount)) {
				bal = farmerAccount.getCashBalance();
				txn.setIntBalance(bal);
				txn.setTxnAmount(StringUtil.isDouble(loanDistribution.getTotalCostToFarmer()) ? Double.valueOf(loanDistribution.getTotalCostToFarmer()) : 0D);
				txn.setBalAmount(bal + loanDistribution.getTotalCostToFarmer());
				txn.setAccount(farmerAccount);
				txn.setFarmerId(loanDistribution.getFarmer().getFarmerId());
				txn.setFarmerName(!ObjectUtil.isEmpty(loanDistribution.getFarmer().getLastName()) ? loanDistribution.getFarmer().getFirstName() + " " + loanDistribution.getFarmer().getLastName() : loanDistribution.getFarmer().getFirstName() );
			}else {
				txn.setIntBalance(0.00);
				txn.setTxnAmount(StringUtil.isDouble(loanDistribution.getTotalCostToFarmer()) ? Double.valueOf(loanDistribution.getTotalCostToFarmer()) : 0D);
				txn.setBalAmount(0.00);
				//txn.setAccount(farmerAccount);
				txn.setFarmerId(NOT_APPLICABLE);
				txn.setFarmerName("");
			}
			
		} else {
			txn.setIntBalance(0.00);
			txn.setTxnAmount(StringUtil.isDouble(loanDistribution.getTotalCostToFarmer()) ? Double.valueOf(loanDistribution.getTotalCostToFarmer()) : 0D);
			txn.setBalAmount(0.00);
			//txn.setAccount(farmerAccount);
			txn.setFarmerId(NOT_APPLICABLE);
			txn.setFarmerName("");
		}
		
		if (!ObjectUtil.isEmpty(loanDistribution.getVendor()) && !StringUtil.isEmpty(loanDistribution.getVendor().getVendorId())) {
			ESEAccount farmerAccount = productDistributionDAO.findESEAccountByProfileId(loanDistribution.getVendor().getVendorId(),ESEAccount.VENDOR_ACCOUNT);
			if(!ObjectUtil.isEmpty(farmerAccount)) {
				bal = farmerAccount.getCashBalance();
				txn.setVendorIntBalance(bal);
				txn.setVendorTxnAmount(StringUtil.isDouble(loanDistribution.getTotalCostToFarmer()) ? Double.valueOf(loanDistribution.getTotalCostToFarmer()) : 0D);
				txn.setVendorBalAmount(bal + loanDistribution.getTotalCostToFarmer());
				txn.setAccount(farmerAccount);
			}else {
				txn.setVendorIntBalance(0.00);
				txn.setVendorTxnAmount(StringUtil.isDouble(loanDistribution.getTotalCostToFarmer()) ? Double.valueOf(loanDistribution.getTotalCostToFarmer()) : 0D);
				txn.setVendorBalAmount(0.00);
			}
			
			
		} else {
			txn.setVendorIntBalance(0.00);
			txn.setVendorTxnAmount(StringUtil.isDouble(loanDistribution.getTotalCostToFarmer()) ? Double.valueOf(loanDistribution.getTotalCostToFarmer()) : 0D);
			txn.setVendorBalAmount(0.00);
			//txn.setAccount(farmerAccount);
		}

		Warehouse warehouse = locationService.findSamithiByCode(selectedWarehouse);
		//txn.setWarehouse(warehouse);
		txn.setSamithi(warehouse);

		
		
		txn.setReceiptNo(loanDistribution.getAgroTransaction().getReceiptNo());		
		productDistributionService.saveAgroTransaction(txn);

		return txn;

	
		
	}
	

	public Map<Long, String> getListWarehouse() {

		List<Warehouse> warehouseList = locationService.listOfSamithi();

		Map<Long, String> warehouseDropDownList = new LinkedHashMap<Long, String>();
		for (Warehouse warehouse : warehouseList) {
			warehouseDropDownList.put(warehouse.getId(), warehouse.getName() + " -  " + warehouse.getCode());
		}
		return warehouseDropDownList;
	}

	public Map<Long, String> getListMunicipality() {

		return locationService.listMunicipality().stream()
				.collect(Collectors.toMap(Municipality::getId, mu -> String.join(" ", mu.getName(), mu.getCode())));
	}
	
	public Map<Long, String> getListProduct() {
		return productDistributionService.listProductsBySubCategoryId(Long.valueOf(selectedCategory)).stream()
				.collect(Collectors.toMap(Product::getId, mu -> String.join(" ", mu.getName(), mu.getCode())));
	}
	
	public Map<String, String> getListVendor() {
		
		if(!ObjectUtil.isListEmpty(getUserVendorMap())){
			return getUserVendorMap().stream()
					.collect(Collectors.toMap(Vendor::getVendorId, mu -> String.join(" ", mu.getVendorName(), mu.getVendorId())));
		}else{
		return productDistributionService.listVendor().stream()
				.collect(Collectors.toMap(Vendor::getVendorId, mu -> String.join(" ", mu.getVendorName(), mu.getVendorId())));
	}

	}

	/** Populate Methods */
	public void populateVillage()throws Exception {
		List<Village> villages = new ArrayList<>();
		if (!selectedCity.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedCity))
				&& !selectedCity.equalsIgnoreCase("0")) {
			villages = locationService.listVillagesByCityID(Long.valueOf(selectedCity));
		}
		JSONArray villageArr = new JSONArray();
		if (!ObjectUtil.isEmpty(villages)) {
			villages.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
				villageArr.add(getJSONObject(obj.getId(), obj.getName() + "-" + obj.getCode()));
			});
		}
		sendAjaxResponse(villageArr);
	}

	public void populateFarmer() throws Exception {
	//	if (!selectedVillage.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedVillage))) {
			
			List<Object[]> listFarmer = farmerService.listFarmerCodeIdNameByFarmerTypezAndLoanApplication(getBranchId());

			JSONArray farmerArr = new JSONArray();		
			
				listFarmer.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
					if(obj[6]!=null){
					farmerArr.add(getJSONObject(obj[4], (obj[2] + " " +(!ObjectUtil.isEmpty(obj[3])?obj[3]:""))+" "+(!ObjectUtil.isEmpty(obj[5])?obj[5]:"") +" - "+(!ObjectUtil.isEmpty(obj[6])?obj[6]:"") ));
					}else{
					farmerArr.add(getJSONObject(obj[4], (obj[2] + " " +(!ObjectUtil.isEmpty(obj[3])?obj[3]:""))+" "+(!ObjectUtil.isEmpty(obj[5])?obj[5]:"")));
					}
				});
			
			sendAjaxResponse(farmerArr);
		//}
	}
	public void populateProduct()throws Exception {
		List<Product> products = new ArrayList<>();
		if (!selectedCategory.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedCategory))) {
			products = productDistributionService.listProductsBySubCategoryId(Long.valueOf(selectedCategory));
		}
		JSONArray productsArr = new JSONArray();
		if (!ObjectUtil.isEmpty(products)) {
			products.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
				productsArr.add(getJSONObject(obj.getId(), obj.getName() + "-" + obj.getCode()));
			});
		}
		sendAjaxResponse(productsArr);
	
	}

	
	public Date getsDate() {

		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		if (!StringUtil.isEmpty(startDate)) {
			try {
				sDate = df.parse(startDate);
			} catch (ParseException e) {
				LOGGER.error("Error parsing start date" + e.getMessage());
			}
		}
		return sDate;
	}

	public Date geteDate() {

		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		if (!StringUtil.isEmpty(endDate)) {
			try {
				eDate = df.parse(endDate);
				eDate.setTime(eDate.getTime() + 86399000);
			} catch (ParseException e) {
				LOGGER.error("Error parsing end date" + e.getMessage());
			}

		}
		return eDate;
	}

	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object code, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", code);
		jsonObject.put("name", name);
		return jsonObject;
	}

	public ILocationService getLocationService() {
		return locationService;
	}

	public void setLocationService(ILocationService locationService) {
		this.locationService = locationService;
	}

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	public IProductDistributionService getProductDistributionService() {
		return productDistributionService;
	}

	public void setProductDistributionService(IProductDistributionService productDistributionService) {
		this.productDistributionService = productDistributionService;
	}

	public IAccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}

	public String getSelectedCity() {
		return selectedCity;
	}

	public void setSelectedCity(String selectedCity) {
		this.selectedCity = selectedCity;
	}

	public String getSelectedVillage() {
		return selectedVillage;
	}

	public void setSelectedVillage(String selectedVillage) {
		this.selectedVillage = selectedVillage;
	}

	public String getSelectedProduct() {
		return selectedProduct;
	}

	public void setSelectedProduct(String selectedProduct) {
		this.selectedProduct = selectedProduct;
	}

	public String getSelectedWarehouse() {
		return selectedWarehouse;
	}

	public void setSelectedWarehouse(String selectedWarehouse) {
		this.selectedWarehouse = selectedWarehouse;
	}

	public String getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(String selectedDate) {
		this.selectedDate = selectedDate;
	}

	public String getSelectedFarmer() {
		return selectedFarmer;
	}

	public void setSelectedFarmer(String selectedFarmer) {
		this.selectedFarmer = selectedFarmer;
	}

	public String getProductTotalString() {
		return productTotalString;
	}

	public void setProductTotalString(String productTotalString) {
		this.productTotalString = productTotalString;
	}

	

	public String update() throws Exception {
		if (id != null && !id.equals("")) {
			loanDistribution = productDistributionService.findLoanDistributionById(Long.valueOf(id));
			/*if (!ObjectUtil.isEmpty(distribution.getDistributionDetails())) {
				Double qty = 0.0;
				{
					for (DistributionDetail distributionDetail : distribution.getDistributionDetails()) {
						qty = qty + distributionDetail.getQuantity();
					}
					setQuantiy(CurrencyUtil.getDecimalFormat((qty), "##.000"));
				}
			}*/
			distributionDetailList = new LinkedList<LoanDistributionDetail>();
			for (LoanDistributionDetail distributionDetail : loanDistribution.getLoanDistributionDetail()) {
				LoanDistributionDetail distributionTemp = distributionDetail;
				distributionTemp
						.setQuantity(distributionDetail.getQuantity());
				distributionTemp
						.setAmount(distributionDetail.getAmount());

			
				distributionDetailList.add(distributionTemp);
			}
			ESESystem preferences1 = preferncesService.findPrefernceById("1");
			/*
			 * approved =
			 * preferncesService.findPrefernceByName(ESESystem.ENABLE_APPROVED);
			 */

			if (loanDistribution == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			setCurrentPage(getCurrentPage());
			id = null;
			command = UPDATE;
			request.setAttribute(HEADING, getText(UPDATE));
			return UPDATE;
		} else {
			
			Set<LoanDistributionDetail> distributionDetails = formDistributionDetailSet();
			
			distributionDetails.stream().forEach(obj -> {
				LoanDistribution existingLoanDistribution = productDistributionService.findLoanDistributionById(obj.getDistributionId());
				Product pd=productDistributionService.findProductByProductId(obj.getProductId());
				obj.setLoanDistribution(existingLoanDistribution);
				obj.setProduct(pd);
				farmerService.update(obj);
				existingLoanDistribution.setTotalCostToFarmer(Double.valueOf(obj.getTotCost()));
				farmerService.update(existingLoanDistribution);
				
			});
			return LIST;
		}
		
	}

	
	public void populateProcurementDetailList() {
		
	}

	public void populateProcurementDetails() {
		
	}

	

	private AgroTransaction processProcurement(SupplierProcurement proc) {
		return null;
		
		
	}

	
	@SuppressWarnings("unchecked")
	public void populateSamithi() throws Exception {

		samithi = locationService.listSamithiesBasedOnType();
		JSONArray warehouseArr = new JSONArray();
		
		samithi.stream().filter(samithi -> !ObjectUtil.isEmpty(samithi)).forEach(samithi -> {
			warehouseArr.add(getJSONObject(samithi.getId(), samithi.getName()));
		});
		sendAjaxResponse(warehouseArr);
	}

	

	public List<Warehouse> getSamithi() {
		return samithi;
	}

	public void setSamithi(List<Warehouse> samithi) {
		this.samithi = samithi;
	}


	public Map<String, String> getLoanCategoryList() {
		Map<String, String> cType = new LinkedHashMap<>();
		cType.put("1", getLocaleProperty("inputs"));
		/*cType.put("0", getLocaleProperty("cash"));*/
		return cType;
	}
	
	public Map<String, String> getLoanToList() {
		Map<String, String> cType = new LinkedHashMap<>();
		cType.put("1", getLocaleProperty("farmer"));
		cType.put("2", getLocaleProperty("group"));
		
		return cType;
	}

	public Map<String, String> getProductList() {

		Map<String, String> productList = new LinkedHashMap<String, String>();
		List<FarmCatalogue> loanProductList = farmerService.listCatelogueType(getText("farmEquipment"));
		loanProductList.stream().collect(Collectors.toMap((FarmCatalogue::getCode), FarmCatalogue::getName)).entrySet().stream()
				.sorted(Map.Entry.comparingByValue(new Comparator<String>() {
					@Override
					public int compare(String o1, String o2) {

						return o1.compareToIgnoreCase(o2);
					}
				})).forEachOrdered(e -> productList.put(e.getKey(), e.getValue()));

		return productList;
	}
	public Map<String, String> getCategoryList() {

		Map<String, String> returnMap = new HashMap<String, String>();

		List<SubCategory> categoryList = productDistributionService.listSubCategory();
		if (!ObjectUtil.isListEmpty(categoryList)) {
			for (SubCategory scategory : categoryList) {
				returnMap.put(String.valueOf(scategory.getId()),scategory.getName());
			}
		}
		return returnMap;
	}

	public void setProductList(Map<String, String> productList) {
		this.productList = productList;
	}

	public Map<String, String> getLoanStatus() {
		Map<String, String> cType = new LinkedHashMap<>();
		
		cType.put("1", getLocaleProperty("loanDistributionStatus1"));
		cType.put("0", getLocaleProperty("loanDistributionStatus0"));
		
		return cType;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getSelectedLoanTo() {
		return selectedLoanTo;
	}

	public void setSelectedLoanTo(String selectedLoanTo) {
		this.selectedLoanTo = selectedLoanTo;
	}

	public String getSelectedLoanCategory() {
		return selectedLoanCategory;
	}

	public void setSelectedLoanCategory(String selectedLoanCategory) {
		this.selectedLoanCategory = selectedLoanCategory;
	}

	public double getSubsidyInterest() {
		return subsidyInterest;
	}

	public void setSubsidyInterest(double subsidyInterest) {
		this.subsidyInterest = subsidyInterest;
	}

	public double getSubsidyAmt() {
		return subsidyAmt;
	}

	public void setSubsidyAmt(double subsidyAmt) {
		this.subsidyAmt = subsidyAmt;
	}

	public double getCostToFarmer() {
		return costToFarmer;
	}

	public void setCostToFarmer(double costToFarmer) {
		this.costToFarmer = costToFarmer;
	}

	public double getCurrentQty() {
		return currentQty;
	}

	public void setCurrentQty(double currentQty) {
		this.currentQty = currentQty;
	}

	public double getCurrentAmt() {
		return currentAmt;
	}

	public void setCurrentAmt(double currentAmt) {
		this.currentAmt = currentAmt;
	}

	public double getDownPaymentQty() {
		return downPaymentQty;
	}

	public void setDownPaymentQty(double downPaymentQty) {
		this.downPaymentQty = downPaymentQty;
	}

	public double getDownPaymentAmt() {
		return downPaymentAmt;
	}

	public void setDownPaymentAmt(double downPaymentAmt) {
		this.downPaymentAmt = downPaymentAmt;
	}

	public double getInterestPercentage() {
		return interestPercentage;
	}

	public void setInterestPercentage(double interestPercentage) {
		this.interestPercentage = interestPercentage;
	}

	public double getInterestAmt() {
		return interestAmt;
	}

	public void setInterestAmt(double interestAmt) {
		this.interestAmt = interestAmt;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	

	public String getLoanTenure() {
		return loanTenure;
	}

	public void setLoanTenure(String loanTenure) {
		this.loanTenure = loanTenure;
	}

	public double getMonthyRepayment() {
		return monthyRepayment;
	}

	public void setMonthyRepayment(double monthyRepayment) {
		this.monthyRepayment = monthyRepayment;
	}

	public LoanDistribution getLoanDistribution() {
		return loanDistribution;
	}

	public void setLoanDistribution(LoanDistribution loanDistribution) {
		this.loanDistribution = loanDistribution;
	}

	public double getLoanTenureAmt() {
		return loanTenureAmt;
	}

	public void setLoanTenureAmt(double loanTenureAmt) {
		this.loanTenureAmt = loanTenureAmt;
	}
	public void populateFarmerLoanYear() throws Exception {
		if (!selectedFarmer.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedFarmer))) {
		LoanDistribution loanDistribution = farmerService.findFarmerLatestLoanYear(Long.valueOf(selectedFarmer));
		if (!ObjectUtil.isEmpty(loanDistribution)) {
			sendResponse(DateUtil.getYearByDateTime(loanDistribution.getLoanDate()));
		}
		}
	}
	public void populateGroupLoanYear() throws Exception {
		if (!selectedFarmer.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedFarmer))) {
				LoanDistribution loanDistribution = farmerService.findGroupLatestLoanYear(Long.valueOf(selectedFarmer));
		if (!ObjectUtil.isEmpty(loanDistribution)) {
			sendResponse(DateUtil.getYearByDateTime(loanDistribution.getLoanDate()));
		}
		}
	}
	
	public void populateFarmerapprovedSeed() throws Exception {
		if (!selectedFarmer.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedFarmer))) {
		LoanApplication loanApplication = farmerService.findFarmerLatestLoanApplication(Long.valueOf(selectedFarmer));
		if (!ObjectUtil.isEmpty(loanApplication)) {
			sendResponse(loanApplication.getSeedlingQty());
		}
		}
	}
	
	public void populateGroupapprovedSeed() throws Exception {
		if (!selectedFarmer.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedFarmer))) {
		LoanApplication loanApplication = farmerService.findGroupLatestLoanApplication(Long.valueOf(selectedFarmer));
		if (!ObjectUtil.isEmpty(loanApplication)) {
			sendResponse(loanApplication.getSeedlingQty());
		}
		}
	}
	
	public Map<Long, String> getListGroup() {

		List<Farmer> groupList = farmerService.listGroup();

		Map<Long, String> groupListDropDownList = new LinkedHashMap<Long, String>();
		for (Farmer farmer : groupList) {
			groupListDropDownList.put(farmer.getId(), farmer.getFirstName() + " -  " + farmer.getLastName());
		}
		return groupListDropDownList;
	}
	/*public void populateGroup() throws Exception {
		if (!selectedVillage.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedVillage))) {
			List<Object[]> listFarmer = farmerService.listGroupCodeIdNameByVillageCodeAndFarmerTypezAndLoanApplication(selectedVillage);

			JSONArray farmerArr = new JSONArray();
			listFarmer.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
				farmerArr.add(getJSONObject(obj[4], obj[2] + "" + (!StringUtil.isEmpty(obj[3]) ? (" " + obj[3]) : " ")
						+ (!StringUtil.isEmpty(obj[1]) ? ("-" + obj[1]) : " ")));
			});
			sendAjaxResponse(farmerArr);
		}
	}*/
	
	@SuppressWarnings("unchecked")
	public String populateFarmerData() {
		try {

			setId(id);
			
			List<LoanDistributionDetail> pmtDatas = productDistributionService.findLoanDistributionDetailById(Long.parseLong(id));
			List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
			pmtDatas.stream().forEach(pmtFarmerData -> {
				JSONObject jsonObject = new JSONObject();
				Product product=productDistributionService.findProductByProductId(pmtFarmerData.getProduct().getId());
				jsonObject.put("category", product.getSubcategory().getName());
				jsonObject.put("product", product.getName());
				jsonObject.put("quantity", pmtFarmerData.getQuantity());
				jsonObject.put("rate", pmtFarmerData.getRatePerUnit());
				jsonObject.put("productCode", pmtFarmerData.getProductCode());
				
				
				jsonObjects.add(jsonObject);

			});

			printAjaxResponse(jsonObjects, "text/html");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public void updateLoanStatus() {
		long farmerId = 0l;
		long vendorId = 0l;
		boolean flag = true;
		try {
			setId(id);
			LoanDistribution loanDistribution= productDistributionService.findLoanDistributionById(Long.parseLong(id));
			Double totAmt = 0.0;
			totAmt = loanDistribution.getTotalCostToFarmer();
			if (getLStatus().equalsIgnoreCase("1") && flag) {
				loanDistribution.setAgroTransaction(formAgroTxn(loanDistribution));
			
				productDistributionService.addLoanDistribution(loanDistribution);
				if (StringUtil
						.isEmpty(loanDistribution.getAgroTransaction().getAccount().getLoanAccountNo())) {
					loanDistribution.getAgroTransaction().getAccount()
							.setLoanAccountNo(idGenerator.getLoanAccountNoSeq());}
					Double loanAmt = loanDistribution.getAgroTransaction().getAccount().getLoanAmount();
					Double outStndAmt = loanDistribution.getAgroTransaction().getAccount().getOutstandingLoanAmount();
					loanDistribution.getAgroTransaction().getAccount().setLoanAmount(loanAmt + totAmt);
					loanDistribution.getAgroTransaction().getAccount().setOutstandingLoanAmount(outStndAmt + totAmt);
					loanDistribution.getAgroTransaction().setSeasonCode(getCurrentSeasonsCode());
					//loanDistribution.getAgroTransaction().getAccount().setCashBalance(outStndAmt + totAmt);
					//ldd.getLoanDistribution().getVendor().get.setLoanAmount(loanAmt + totAmt);
					farmerService.update(loanDistribution.getAgroTransaction().getAccount());
					ESEAccount vendorAccount = productDistributionDAO.findESEAccountByProfileId(loanDistribution.getVendor().getVendorId(),ESEAccount.VENDOR_ACCOUNT);			
					if (StringUtil
							.isEmpty(vendorAccount.getLoanAccountNo())) {
						vendorAccount.setLoanAccountNo(idGenerator.getLoanAccountNoSeq());}
					Double vendorLoanAmt=vendorAccount.getLoanAmount();
					Double vendoroutLoanAmt=vendorAccount.getOutstandingLoanAmount();
					vendorAccount.setLoanAmount(vendorLoanAmt+totAmt);
					vendorAccount.setOutstandingLoanAmount(vendoroutLoanAmt+totAmt);
					//vendorAccount.setCashBalance(vendoroutLoanAmt+totAmt);
					farmerService.update(vendorAccount);
				
					LoanLedger loanLedger = new LoanLedger();
					loanLedger.setTxnTime(loanDistribution.getLoanDate());
					loanLedger.setReceiptNo(loanDistribution.getAgroTransaction().getReceiptNo());
					//if(loanDistribution.getLoanTo()==1){
					loanLedger.setFarmerId(loanDistribution.getFarmer().getFarmerId());
					loanLedger.setVendorId(loanDistribution.getVendor().getVendorId());
					/*}
					else{
						loanLedger.setFarmerId(loanDistribution.getGroup().getFarmerId());
					}*/
					loanLedger.setAccount(loanDistribution.getAgroTransaction().getAccount());
					loanLedger.setAccountNo(loanDistribution.getAgroTransaction().getAccount().getLoanAccountNo());
					//if(loanDistribution.getLoanTo()==1){
						loanLedger.setFarmerId(!ObjectUtil.isEmpty(loanDistribution.getFarmer()) ? loanDistribution.getFarmer().getFarmerId() : "");
					/*}else{
						loanLedger.setFarmerId(!ObjectUtil.isEmpty(loanDistribution.getGroup()) ? loanDistribution.getGroup().getFarmerId() : "");
					}*/
					
					loanLedger.setAccount(!ObjectUtil.isEmpty(loanDistribution.getAgroTransaction()) && !ObjectUtil.isEmpty(loanDistribution.getAgroTransaction().getAccount()) ? loanDistribution.getAgroTransaction().getAccount() : null);
					loanLedger.setAccountNo(!ObjectUtil.isEmpty(loanDistribution.getAgroTransaction().getAccount()) ? loanDistribution.getAgroTransaction().getAccount().getLoanAccountNo() : "");

					loanLedger.setActualAmount(loanDistribution.getTotalCostToFarmer());
					/*loanLedger.setFarmerLoanBal(loanDistribution.getAgroTransaction().getAccount().getLoanAmount());
					loanLedger.setFarmerOutStandingBal(loanDistribution.getAgroTransaction().getAccount().getOutstandingLoanAmount());*/
					loanLedger.setTxnType("701");
					loanLedger.setNewFarmerBal(!ObjectUtil.isEmpty(loanDistribution.getAgroTransaction().getAccount()) ? loanDistribution.getAgroTransaction().getAccount().getLoanAmount() : 0.00);
					loanLedger.setPreFarmerBal(loanDistribution.getTotalCostToFarmer());
					loanLedger.setLoanDesc(LoanDistribution.LOAN_DISTRIBUTION_PAYMENT_AMOUNT);
					loanLedger.setBranchId(loanDistribution.getBranchId());
					loanLedger.setLoanStatus(LoanDistribution.LoanStatus.ACTIVE.ordinal());
					farmerService.save(loanLedger);
					
			}
			List<LoanDistributionDetail> loanDistributionDetail = productDistributionService
					.findLoanDistributionDetailById(Long.parseLong(id));
			Map<Long, Double> result1 = new LinkedHashMap<Long, Double>();
			for (LoanDistributionDetail ldd : loanDistributionDetail) {
				ldd.getLoanDistribution().setLoanStatus(Integer.valueOf(getLStatus()));
				result1.put(ldd.getProduct().getId(), ldd.getQuantity());
				farmerId = ldd.getLoanDistribution().getFarmer().getId();
				vendorId = ldd.getLoanDistribution().getVendor().getId();
				if (getLStatus().equalsIgnoreCase("1") ) {
					List<DistributionBalance> disBal = productDistributionService
							.findDistributionBalanceByFarmerAndProductIdAndVendorId(farmerId, String.valueOf(ldd.getProduct().getId()),vendorId);
					double totalstock = 0.00;
					if (!disBal.isEmpty()) {
						for (DistributionBalance db : disBal) {
							if (result1.containsKey(db.getProduct().getId()))
								totalstock = db.getStock() + result1.get(db.getProduct().getId());
							db.setRevisionNo(DateUtil.getRevisionNoDateTimeMilliSec());
							db.setStock(totalstock);
							farmerService.update(db);
						}
					} else {
						DistributionBalance dbb = new DistributionBalance();
					
							Long pId;
							pId = ldd.getProduct().getId();
							Product product = productService.findProductById(pId);
							dbb.setProduct(product);
							dbb.setStock(result1.get(pId));
							dbb.setRevisionNo(DateUtil.getRevisionNoDateTimeMilliSec());
							//Farmer farmer = farmerService.findFarmerById(farmerId);
							dbb.setFarmer(ldd.getLoanDistribution().getFarmer());
							//Vendor vendor = clientService.findVendorById(farmerId);
							dbb.setVendor(ldd.getLoanDistribution().getVendor());
							farmerService.save(dbb);
						

					}}
				farmerService.update(ldd.getLoanDistribution());
				flag = false;
			}
			List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
			printAjaxResponse(jsonObjects, "text/html");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getLStatus() {
		return LStatus;
	}

	public void setLStatus(String lStatus) {
		LStatus = lStatus;
	}
	public Map<String, String> getLoanStatusList() {
		loanStatusList.put("1", getText("Approved"));
		loanStatusList.put("2", getText("Rejected"));
		loanStatusList.put("0", getText("Not Viewed"));
		return loanStatusList;
	}
	
	private Map<String, Object> getTotalLoanDistributionDetails(LoanDistribution loanDistribution) {

		
		double qty = 0;
		double totalAmt=0;
		String pCode="";
		Map<String, Object> details = new HashMap<String, Object>();
		if (!ObjectUtil.isListEmpty(loanDistribution.getLoanDistributionDetail())) {
			for (LoanDistributionDetail detail : loanDistribution.getLoanDistributionDetail()) {
				qty = qty + detail.getQuantity();
				totalAmt=totalAmt + detail.getTotalAmt();
				pCode=pCode+","+detail.getProduct().getId();
			}
		}
		details.put(QUANTITY, qty);
		details.put(TOTAL_AMT, totalAmt);
		details.put(PRODUCT_CODE, pCode);
		
		return details;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(String selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public String getSelectedCProduct() {
		return selectedCProduct;
	}

	public void setSelectedCProduct(String selectedCProduct) {
		this.selectedCProduct = selectedCProduct;
	}

	public String getSelectedVendor() {
		return selectedVendor;
	}

	public void setSelectedVendor(String selectedVendor) {
		this.selectedVendor = selectedVendor;
	}
	
	  public String populateGroup() throws Exception {

	        String result = "";
	        if (!StringUtil.isEmpty(selectedFarmer)) {
	            Farmer farmer = farmerService.findFarmerById(Long.parseLong(selectedFarmer));

	            result = farmer.getSamithi() !=null ? farmer.getSamithi().getName() : "NA";
	        }
	        PrintWriter out = null;
	        try {
	            response.setCharacterEncoding("UTF-8");
	            response.setContentType("text/html");
	            out = response.getWriter();
	            out.print(result);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }

	public List<LoanDistributionDetail> getDistributionDetailList() {
		return distributionDetailList;
	}

	public void setDistributionDetailList(List<LoanDistributionDetail> distributionDetailList) {
		this.distributionDetailList = distributionDetailList;
	}
	
	public Set<LoanDistributionDetail> formDistributionDetailSet() {

		Set<LoanDistributionDetail> distributionDetailSet = new LinkedHashSet<>();
		if (!StringUtil.isEmpty(distributionDetails)) {
			Type distributionDetailType = new TypeToken<List<LoanDistributionDetail>>() {
			}.getType();
			List<LoanDistributionDetail> distributionList = new Gson().fromJson(distributionDetails,
					distributionDetailType);
			distributionDetailSet.addAll(distributionList);
		}

		return distributionDetailSet;
	}

	public String getDistributionDetails() {
		return distributionDetails;
	}

	public void setDistributionDetails(String distributionDetails) {
		this.distributionDetails = distributionDetails;
	}
	
	
	public String populateCostPrice() throws Exception {

		double result = 0d;

		if (!StringUtil.isEmpty(selectedProduct) && !"0".equalsIgnoreCase(selectedProduct)) {


				Product product = productService.findProductById(Long.valueOf(selectedProduct));
			

			if (!ObjectUtil.isEmpty(product)) {
				result = Double.valueOf(product.getPrice());
			}

		}
		
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			out = response.getWriter();
			out.print(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public Map<String, String> getVendorsList() {

		if(!ObjectUtil.isListEmpty(getUserVendorMap())){
			return getUserVendorMap().stream()
					.collect(Collectors.toMap(Vendor::getVendorId, mu -> String.join(" ", mu.getVendorName(), mu.getVendorId())));
		}else{
		
			Map<String, String> vendorListMap = new LinkedHashMap<String, String>();
			List<Vendor> vendorsList = productDistributionService.listVendor();
			if (!ObjectUtil.isListEmpty(vendorsList)) {
				for (Vendor vendor : vendorsList) {
					vendorListMap.put(vendor.getVendorId(), vendor.getVendorName());
				}
			}
			return vendorListMap;
		}

	}
	
	public Map<String, String> getFarmerList() {

		Map<String, String> returnMap = new HashMap<String, String>();
		List<Object[]> listFarmer = farmerService.listFarmerCodeIdNameByFarmerTypezAndLoanApplication(getBranchId());
			if (!ObjectUtil.isListEmpty(listFarmer)) {
				
				for (Object[] obj : listFarmer) {
				/*	if(obj[6]!=null){
						returnMap.put(obj[4].toString(), (obj[2] + " " +(!ObjectUtil.isEmpty(obj[3])?obj[3]:""))+" "+(!ObjectUtil.isEmpty(obj[5])?obj[5]:"") +" - "+(!ObjectUtil.isEmpty(obj[6])?obj[6]:"") );
						}else{*/
					returnMap.put(obj[4].toString(), (obj[2] + " " +(!ObjectUtil.isEmpty(obj[3])?obj[3]:""))+" "+(!ObjectUtil.isEmpty(obj[5])?obj[5]:"")+" - "+(!ObjectUtil.isEmpty(obj[1])?obj[1]:"") );
						/*}*/
					
					
				}
			
		}
		return returnMap;
	}
}
