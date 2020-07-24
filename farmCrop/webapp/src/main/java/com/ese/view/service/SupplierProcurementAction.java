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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.sms.SMSHistory;
import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.dao.IProductDistributionDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.AgentType;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.CityWarehouse;
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
import com.sourcetrace.eses.service.ISMSService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.view.WebTransactionAction;

@SuppressWarnings("unchecked")
public class SupplierProcurementAction extends WebTransactionAction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(SupplierProcurementAction.class);

	public static final String SUPPLIER_PROCUREMENT = "378";
	public static final String TRIP_SHEET = "TRIP_SHEET";
	public static final String PROCUREMENT_PRODUCT = "PROCUREMENT_PRODUCT";
	public static final String AGENT = "AGENT";
	public static final String FARMER_ACCOUNTS = "FARMER_ACCOUNTS";
	public static final String PROCUREMENTS = "PROCUREMENTS";
	public static final String REGISTERED_FARMER = "0";
	public static final String un_REGISTERED_FARMER = "1";
	private static final String GRADE_IDS = "gradeId";

	protected int page;
	protected int rows;
	protected String sidx;
	protected String sord;
	protected Date sDate = null;
	protected Date eDate = null;
	private String selectedCity;
	private String selectedVillage;
	private String selectedProduct;
	private String selectedVariety;
	private String selectedGrade;
	private String selectedPro;
	private String selectedVar;
	private Long seasonId;
	private static final String NO_OF_BAGS = "bags";
	private static final String GROSS_WEIGHT = "grossWt";
	private static final String NET_WEIGHT = "netWt";
	private static final Object GRADE_ID = null;

	private String selectedMobileUser;
	private String selectedWarehouse;
	private String selectedDate;
	private String selectedFarmerType;
	private String selectedFarmer;
	private String productTotalString;
	private String selectedSupplier;
	private String selectedMasterType;
	private String mobileNo;
	private String farmerType;
	private String farmerName;
	private String mobileNumber;
	private String paymentAmount;
	private String totalAmount;
	private String enableSupplier;
	private int isSupplier;
	private String supplierName;
	private String totalLabourCost;
	private String transportCost;
	private String invoiceNo;
	/** AWI FIELDS */
	private String roadMapCode;
	private String vehicleNo;
	private String farmerAttnce;
	private String substituteName;
	private String selectedFarm;
	private String supplierMaster;
	private String supplierType;
	private String procurementId;
	private String procurementDetailArray;
	/** Wilmar*/
	private String selectedTrader;
	
	private List<ProcurementVariety> varietyList = new ArrayList<ProcurementVariety>();
	private String enableTraceability;
	private String selectedUom;
	private List<FarmCatalogue> subUomList = new ArrayList<FarmCatalogue>();
	/** Service Injection */
	private ILocationService locationService;
	private IFarmerService farmerService;
	private IProductDistributionService productDistributionService;
	private IAccountService accountService;
	@Autowired
	private IUniqueIDGenerator idGenerator;
	@Autowired
	private IPreferencesService preferncesService;
	@Autowired
	private IProductDistributionDAO productDistributionDAO;
	@Autowired
	private IProductService productService;
	@Autowired
	private ISMSService smsService;
	@Autowired
	private IPreferencesService preferencesService;
	
	
	private String id;
	private SupplierProcurement supplierProcurement;
	private String productAvailableUnit;
	private String productAvailableUnitCode;
	private String selectedCropType;
	List<Warehouse> samithi = new ArrayList<Warehouse>();
	
	String providerResponse = null;
	int smsType = SMSHistory.SMS_SINGLE;
	String status = null;
	String descrption = null;

	public SupplierProcurement getSupplierProcurement() {
		return supplierProcurement;
	}

	public void setSupplierProcurement(SupplierProcurement supplierProcurement) {
		this.supplierProcurement = supplierProcurement;
	}

	CityWarehouse cityWarehouse;

	public CityWarehouse getCityWarehouse() {
		return cityWarehouse;
	}

	public void setCityWarehouse(CityWarehouse cityWarehouse) {
		this.cityWarehouse = cityWarehouse;
	}

	private ProcurementProduct procurementProduct;

	public ProcurementProduct getProcurementProduct() {
		return procurementProduct;
	}

	public void setProcurementProduct(ProcurementProduct procurementProduct) {
		this.procurementProduct = procurementProduct;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String procurementDate;
	private List<MasterData> masterDataList = new LinkedList<>();

	private Map<Integer, String> masterTypeList = new HashMap<Integer, String>();
	private Map<String, String> farmerAttence = new HashMap<>();
	private String taxAmt;
	private String otherCost;

	@SuppressWarnings("rawtypes")
	public String data() throws Exception {
		getJQGridRequestParam();
		filter = new Procurement();
		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		return sendJQGridJSONResponse(data);
	}

	public JSONObject toJSON(Object obj) {
		try {
			Procurement procurement = (Procurement) obj;
			JSONObject jsonObject = new JSONObject();
			JSONArray rows = new JSONArray();

			DateFormat genDate = new SimpleDateFormat(
					preferncesService.findPrefernceByName(ESESystem.GENERAL_DATE_FORMAT));
			rows.add(String.valueOf(genDate.format(procurement.getAgroTransaction().getTxnTime())));

			if (!StringUtil.isEmpty(procurement.getSeasonCode())) {
				HarvestSeason season = farmerService.findSeasonNameByCode(procurement.getSeasonCode().trim());
				rows.add(!StringUtil.isEmpty(season) ? season.getName() : "");
			} else {
				rows.add("N/A");
			}
			/* if (procurement.getFarmer() != null) { */

			rows.add(procurement.getFarmer() != null ? procurement.getFarmer().getFarmerCodeAndName()
					: procurement.getAgroTransaction().getFarmerName() != null
							? procurement.getAgroTransaction().getFarmerName() : "N/A");
			/*
			 * } else { rows.add("NA"); }
			 */

			if (!StringUtil.isEmpty(procurement.getVillage())) {
				rows.add(procurement.getVillage().getName());
			} else {
				rows.add("N/A");
			}

			Map<String, Object> details = getTotalProductDetails(procurement);
			if (details.get(GRADE_IDS) != null) {
				ProcurementGrade grade = productDistributionService
						.findProcurementGradeById((Long) details.get(GRADE_IDS));
				if (!ObjectUtil.isEmpty(grade)) {
					if (ObjectUtil.isEmpty(procurement.getAgroTransaction().getAgentId())
							&& !ObjectUtil.isEmpty(procurement.getFarmer())) {
						CityWarehouse city = productDistributionService
								.findCityWarehouseIdByFarmerAndProductIdAndGradeCode(procurement.getFarmer().getId(),
										grade.getProcurementVariety().getProcurementProduct().getId(), grade.getCode());
						rows.add(!ObjectUtil.isEmpty(city) && !ObjectUtil.isEmpty(city.getCoOperative())
								? city.getCoOperative().getName() : "NA");
					} else {
						rows.add("NA");
					}
				} else {
					rows.add("NA");
				}
			}

			rows.add(details.get(NO_OF_BAGS).toString());
			rows.add(CurrencyUtil.getDecimalFormat((Double) details.get(NET_WEIGHT), "##.000"));

			if (!StringUtil.isEmpty(procurement.getTotalProVal())) {
				rows.add(procurement.getTotalProVal());
			} else {
				rows.add("N/A");
			}

			jsonObject.put("id", procurement.getId());
			jsonObject.put("cell", rows);
			return jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String create() {
		setEnableSupplier(preferncesService.findPrefernceByName(ESESystem.ENABLE_SUPPLIER));
		setEnableTraceability(preferncesService.findPrefernceByName(ESESystem.ENABLE_TRACEABILITY));
		return INPUT;
	}

	public String detail() throws Exception {
		setEnableTraceability(preferncesService.findPrefernceByName(ESESystem.ENABLE_TRACEABILITY));
		supplierProcurement = productDistributionService.findSupplierProcurementById(Long.valueOf(id));
		if (!ObjectUtil.isEmpty(supplierProcurement)) {
			for (SupplierProcurementDetail supplierProcurementDetail : supplierProcurement.getSupplierProcurementDetails()) {
				if (!ObjectUtil.isEmpty(supplierProcurementDetail.getProcurementProduct())
						&& (!ObjectUtil.isEmpty(supplierProcurementDetail.getProcurementGrade()))) {
					cityWarehouse = productDistributionService.findByProdIdAndGradeCode(
							supplierProcurementDetail.getProcurementProduct().getId(),
							supplierProcurementDetail.getProcurementGrade().getCode(), getCurrentTenantId());

				}
			}

			this.setSupplierMaster(this.supplierProcurement.getProcMasterType() != null
					? this.getMasterTypeList().get(Integer.parseInt(this.supplierProcurement.getProcMasterType())) : "");
			
			
			
			
			
			
			if (!StringUtil.isEmpty((String) this.supplierProcurement.getProcMasterTypeId())){
				if("99".equalsIgnoreCase(this.supplierProcurement.getProcMasterType()) || "11".equalsIgnoreCase(this.supplierProcurement.getProcMasterType())) {
					Warehouse warehouse=locationService.findSamithiByCode(this.supplierProcurement.getProcMasterTypeId());
					this.setSupplierType(warehouse.getName());
				}
				else{
				MasterData masterData = this.clientService
						.findMasterDataById(Long.valueOf(Long.parseLong(this.supplierProcurement.getProcMasterTypeId())));
				this.setSupplierType(masterData.getName());
				}
			}
			if (!ObjectUtil.isEmpty(cityWarehouse) && !ObjectUtil.isEmpty(cityWarehouse.getCoOperative())) {

				if (!StringUtil.isEmpty(cityWarehouse.getCoOperative().getName())) {
					Warehouse warehouse = locationService
							.findCoOperativeByCode(cityWarehouse.getCoOperative().getCode());
					if(!ObjectUtil.isEmpty(warehouse))
						supplierProcurement.setWarehouseName(warehouse.getCode() + "-" + warehouse.getName());
				}
			}
		} else {
			return LIST;
		}
		return DETAIL;
	}

	public void populateProcurement() {
		loadCurrentSeason();
		String receiptNumber;
		setEnableSupplier(preferncesService.findPrefernceByName(ESESystem.ENABLE_SUPPLIER));
		setEnableTraceability(preferncesService.findPrefernceByName(ESESystem.ENABLE_TRACEABILITY));
		/** Form Procurement Detail Object */
		Set<SupplierProcurementDetail> supplierProcurementDetails = formSupplierProcurementDetails();

		Season season = productDistributionService.findSeasonById(seasonId);

		SupplierProcurement supplierProcurement = new SupplierProcurement();
		supplierProcurement.setSupplierProcurementDetails(supplierProcurementDetails);
		supplierProcurement.setSeason(season);
		supplierProcurement.setSeasonCode(getCurrentSeasonsCode());
		supplierProcurement.setPaymentAmount(StringUtil.isDouble(getPaymentAmount()) ? Double.valueOf(getPaymentAmount()) : 0D);
		supplierProcurement.setTotalProVal(StringUtil.isDouble(getTotalAmount()) ? Double.valueOf(getTotalAmount()) : 0D);
		supplierProcurement.setIsRegSupplier(!ObjectUtil.isEmpty(isSupplier) ? isSupplier : 0);
		if(supplierProcurement.getIsRegSupplier()==0){		
			supplierProcurement.setProcMasterTypeId(selectedMasterType);	
			supplierProcurement.setMobileNumber(!StringUtil.isEmpty(mobileNo) ? mobileNo : "");
		}else{
			supplierProcurement.setProcMasterTypeId(supplierName);	
			supplierProcurement.setMobileNumber(!StringUtil.isEmpty(mobileNo) ? mobileNo : "");
		}
		if(getCurrentTenantId().equalsIgnoreCase("wilmar")){
			supplierProcurement.setTrader(!StringUtil.isEmpty(selectedTrader) ? selectedTrader : "");
		}
		supplierProcurement.setProcMasterType(getSelectedSupplier());	
			
		supplierProcurement.setCreatedUser(getUsername());
		supplierProcurement.setCreatedDate(DateUtil.convertStringToDate(this.procurementDate, getGeneralDateFormat()));
		supplierProcurement.setTotalLabourCost(!StringUtil.isEmpty(totalLabourCost) ? Double.valueOf(totalLabourCost) : 0.00);
		supplierProcurement.setTransportCost(!StringUtil.isEmpty(transportCost) ? Double.valueOf(transportCost) : 0.00);
		supplierProcurement.setTaxAmt(!StringUtil.isEmpty(taxAmt)? Double.valueOf(taxAmt):0.00);
		supplierProcurement.setOtherCost(!StringUtil.isEmpty(otherCost)?Double.valueOf(otherCost):0.00);
		supplierProcurement.setInvoiceValue(
				calculateInvoiceValue(supplierProcurement.getTotalLabourCost(),supplierProcurement.getTransportCost(), supplierProcurement.getTotalProVal(), supplierProcurement.getTaxAmt(), supplierProcurement.getOtherCost()));
		
		
		supplierProcurement.setInvoiceNo(!StringUtil.isEmpty(invoiceNo) ? invoiceNo : "");
		supplierProcurement.setBranchId(getBranchId());
		Warehouse warehouse = locationService.findWarehouseById(Long.valueOf(selectedWarehouse));
		supplierProcurement.setWarehouseId(String.valueOf(warehouse.getId()));
		supplierProcurement.setAgroTransaction(formAgroTxn(supplierProcurement));
		
		processProcurement(supplierProcurement);

	
			productDistributionService.addSupplierProcurement(supplierProcurement);
	
			
if(getCurrentTenantId().equalsIgnoreCase("gsma")){
	
			ESESystem system = preferencesService.findPrefernceById("1");
			
			supplierProcurement.getSupplierProcurementDetails().stream().forEach(i -> {
				SMSHistory sms = new SMSHistory();
				sms.setCreatedUser(supplierProcurement.getCreatedUser());
				sms.setBranchId(supplierProcurement.getBranchId());
				sms.setSmsRoute(system.getPreferences().get(ESESystem.SMS_ROUTE));
				sms.setReceiverMobNo(i.getFarmer().getMobileNumber());
				sms.setSenderMobNo(i.getFarmer().getMobileNumber());
				
				if (!ObjectUtil.isEmpty(system) && !ObjectUtil.isEmpty(system.getPreferences())) {
					String msg =  system.getPreferences().get(ESESystem.SMS_MESSAGE);
					
					//ProcurementGrade procurementGrade = productService.findProcurementGradeByCode(i.getQuality().trim());
					ProcurementGrade procurementGrade =  i.getProcurementGrade();
					 msg = msg.replaceAll("<grade>", procurementGrade.getName());
					 msg = msg.replaceAll("<ProcurementProduct>", ObjectUtil.isEmpty(procurementGrade.getProcurementVariety()) ? "" : procurementGrade.getProcurementVariety().getProcurementProduct().getName());
					 msg = msg.replaceAll("<ProcurementVariety>", ObjectUtil.isEmpty(procurementGrade.getProcurementVariety()) ? "" : procurementGrade.getProcurementVariety().getName());
					 msg = msg.replaceAll("<RatePerUnit>", String.valueOf(i.getRatePerUnit()));
					 msg = msg.replaceAll("<SubTotal>", String.valueOf(i.getSubTotal()));
					 msg = msg.replaceAll("<procurementDate>", DateUtil.convertDateToString(supplierProcurement.getCreatedDate(), DateUtil.DATE_FORMAT_1));
					 msg = msg.replaceAll("<quantity>", String.valueOf(i.getNetWeight()));
					 msg = msg.replaceAll("<unit>", procurementGrade.getProcurementVariety().getProcurementProduct().getUnit());
					 sms.setMessage(msg);
				}
				
				providerResponse = smsService.sendSMS(smsType, sms.getReceiverMobNo(),sms.getMessage());
				sms.setResponce(providerResponse);
				if(!providerResponse.contains(ISMSService.ERROR)){
					sms.setStatusMsg("Delivered");
					//sms.setUuid(respObj.getString("batch_id"));
					farmerService.save(sms);
				}else{
					sms.setStatusMsg("Failed");
					farmerService.save(sms);
				}
				
				
				
			});
			
}
		
	
			
		
			
			
		if (!ObjectUtil.isEmpty(supplierProcurement) && !ObjectUtil.isEmpty(supplierProcurement.getAgroTransaction())
				&& !StringUtil.isEmpty(supplierProcurement.getAgroTransaction().getReceiptNo()))
			receiptNumber = supplierProcurement.getAgroTransaction().getReceiptNo();

		JSONArray respArr = new JSONArray();
		respArr.add(getJSONObject("data", "success"));
		respArr.add(getJSONObject("receiptNumber", supplierProcurement.getAgroTransaction().getReceiptNo()));
		sendAjaxResponse(respArr);
	}

	private Set<SupplierProcurementDetail> formSupplierProcurementDetails() {
		Set<SupplierProcurementDetail> supplierProcurementDetails = new LinkedHashSet<>();
	
		if (!StringUtil.isEmpty(getProductTotalString())) {
			List<String> productsList = Arrays.asList(getProductTotalString().split("@"));

			productsList.stream().filter(obj -> !StringUtil.isEmpty(obj)).forEach(products -> {
				SupplierProcurementDetail supplierProcurementDetail = new SupplierProcurementDetail();
				List<String> list = Arrays.asList(products.split("#"));
				Double grossWeight = Double.parseDouble(list.get(9));
				supplierProcurementDetail.setIsReg(list.get(0).toString());
				//if(getCurrentTenantId().equalsIgnoreCase("kpf"))
if(getIsKpfBased().equals("1")){
					if ("99".equalsIgnoreCase(!ObjectUtil.isEmpty(list.get(11)) ? list.get(11) : "99") 
					        || "11".equalsIgnoreCase(!ObjectUtil.isEmpty(list.get(11)) ? list.get(11) : "11")) {
						supplierProcurementDetail.setIsReg(!ObjectUtil.isEmpty(list.get(0)) ? list.get(0) : "");
						if(!ObjectUtil.isEmpty(list.get(0))
								&& list.get(0).equalsIgnoreCase("0")){
						
					    Farmer farmerList = farmerService.findFarmerById(Long.valueOf(list.get(1)));
					    supplierProcurementDetail.setFarmer(!ObjectUtil.isEmpty(farmerList) ? farmerList : null);
					    supplierProcurementDetail.setVillageCode(!ObjectUtil.isEmpty(farmerList) && !ObjectUtil.isEmpty(farmerList.getVillage()) && 
		                 		!StringUtil.isEmpty(farmerList.getVillage().getCode()) ? farmerList.getVillage().getCode() : "");
					    supplierProcurementDetail.setFarmerName(!ObjectUtil.isEmpty(farmerList) ? farmerList.getFirstName() + " " + farmerList.getLastName() : "");
					    supplierProcurementDetail.setFarmerMobileNumber(!ObjectUtil.isEmpty(farmerList) ? farmerList.getMobileNumber() : "");	
						}else{
							
								supplierProcurementDetail.setFarmer(null);
								supplierProcurementDetail.setFarmerName(!ObjectUtil.isEmpty(list.get(2)) ? list.get(2) : "");
								supplierProcurementDetail.setFarmerMobileNumber(!ObjectUtil.isEmpty(list.get(3)) ? list.get(3) : "");
								supplierProcurementDetail.setVillageCode("");
							}
						//supplierProcurement.setProcMasterTypeId(!ObjectUtil.isEmpty(list.get(11)) ? list.get(11) : "");
		            } else {
		            	
		            	supplierProcurementDetail.setFarmer(null);
		            	supplierProcurementDetail.setFarmerName("");
		            	supplierProcurementDetail.setFarmerMobileNumber("");
						supplierProcurementDetail.setVillageCode("");	
						//supplierProcurementDetail.getSupplierProcurement().setProcMasterTypeId("");
						//supplierProcurement.setProcMasterTypeId(!ObjectUtil.isEmpty(list.get(11)) ? list.get(11) : "");
		                
		            }
				}else if(getCurrentTenantId().equalsIgnoreCase("wilmar")){

					/*if ("99".equalsIgnoreCase(!ObjectUtil.isEmpty(list.get(11)) ? list.get(11) : "99") 
					        || "11".equalsIgnoreCase(!ObjectUtil.isEmpty(list.get(11)) ? list.get(11) : "11")) {
						*/
						supplierProcurementDetail.setIsReg(!ObjectUtil.isEmpty(list.get(0)) ? list.get(0) : "");
						
						if(!ObjectUtil.isEmpty(list.get(0))
								&& list.get(0).equalsIgnoreCase("0")){
						
					    Farmer farmerList = farmerService.findFarmerById(Long.valueOf(list.get(1)));
					    supplierProcurementDetail.setFarmer(!ObjectUtil.isEmpty(farmerList) ? farmerList : null);
					    supplierProcurementDetail.setVillageCode(!ObjectUtil.isEmpty(farmerList) && !ObjectUtil.isEmpty(farmerList.getVillage()) && 
		                 		!StringUtil.isEmpty(farmerList.getVillage().getCode()) ? farmerList.getVillage().getCode() : "");
					    supplierProcurementDetail.setFarmerName(!ObjectUtil.isEmpty(farmerList) ? farmerList.getFirstName() + " " + farmerList.getLastName() : "");
					    supplierProcurementDetail.setFarmerMobileNumber(!ObjectUtil.isEmpty(farmerList) ? farmerList.getMobileNumber() : "");	
					    
						}/*else{
							
								supplierProcurementDetail.setFarmer(null);
								supplierProcurementDetail.setFarmerName(!ObjectUtil.isEmpty(list.get(2)) ? list.get(2) : "");
								supplierProcurementDetail.setFarmerMobileNumber(!ObjectUtil.isEmpty(list.get(3)) ? list.get(3) : "");
								supplierProcurementDetail.setVillageCode("");
							}*/
						//supplierProcurement.setProcMasterTypeId(!ObjectUtil.isEmpty(list.get(11)) ? list.get(11) : "");
		            //} 
				
				}
				
				
				ProcurementGrade grade = productDistributionService
							.findProcurementGradeById(Long.valueOf(list.get(6)));
				supplierProcurementDetail.setProcurementGrade(grade);
				supplierProcurementDetail.setProcurementProduct(grade.getProcurementVariety().getProcurementProduct());
				supplierProcurementDetail.setRatePerUnit(!ObjectUtil.isEmpty(list.get(13)) ? Double.valueOf(list.get(13)) : 0.00);
			
				supplierProcurementDetail.setNumberOfBags(Long.valueOf(list.get(7)));
				supplierProcurementDetail.setGrossWeight(grossWeight);
				supplierProcurementDetail.setUnit(list.size()>=12?list.get(12):"");
				if (enableTraceability.equalsIgnoreCase("1")) {
					supplierProcurementDetail.setBatchNo(!ObjectUtil.isEmpty(list.get(14)) ? list.get(14) : "");
				}
				supplierProcurementDetail.setCropType(list.get(8));			
				supplierProcurementDetail.setNetWeight(grossWeight);

				supplierProcurementDetail.setSubTotal(
							calculateSubTotal(supplierProcurementDetail.getNetWeight(), supplierProcurementDetail.getRatePerUnit()));
				
				
				supplierProcurementDetails.add(supplierProcurementDetail);
			});

		}

		return supplierProcurementDetails;
	}

	private AgroTransaction formAgroTxn(SupplierProcurement supplierProcurement) {
		AgroTransaction agroTransaction = new AgroTransaction();
		agroTransaction.setDeviceId(NOT_APPLICABLE);
		agroTransaction.setDeviceName(NOT_APPLICABLE);
		agroTransaction.setBranch_id(supplierProcurement.getBranchId());
		agroTransaction.setSeasonCode(supplierProcurement.getSeasonCode());
		if (!StringUtil.isEmpty(this.procurementDate)) {
			try {
				agroTransaction.setTxnTime(DateUtil.convertStringToDate(this.procurementDate, getGeneralDateFormat()));
				//agroTransaction.setTxnTime(DateUtil.setTimeToDate(agroTransaction.getTxnTime()));
			} catch (Exception e) {
				String nowDate = DateUtil.convertDateToString(new Date(), getGeneralDateFormat());
				agroTransaction.setTxnTime(DateUtil.convertStringToDate(nowDate, getGeneralDateFormat()));

				//agroTransaction.setTxnTime(DateUtil.setTimeToDate(agroTransaction.getTxnTime()));

			}
		}
		agroTransaction.setTxnType(SUPPLIER_PROCUREMENT);
		agroTransaction.setProfType(Profile.CLIENT);
		agroTransaction.setOperType(ESETxn.ON_LINE);
		agroTransaction.setTxnDesc(SupplierProcurement.PROCUREMENT_AMOUNT);
		agroTransaction.setTxnAmount(getTransactionAmount(supplierProcurement));

		Warehouse warehouse = locationService.findWarehouseById(Long.valueOf(selectedWarehouse));
		agroTransaction.setWarehouse(warehouse);
		agroTransaction.setSamithi(warehouse);

		if (!StringUtil.isEmpty(selectedMobileUser)) {
			Agent agent = agentService.findAgent(Long.valueOf(selectedMobileUser));
			agroTransaction.setAgentId(agent.getProfileId());
			agroTransaction.setAgentName(agent.getPersonalInfo().getFirstName());
		}
		

		Farmer farmer = null;
		if (!ObjectUtil.isEmpty(supplierProcurement.getSupplierProcurementDetails()) 
				&& !ObjectUtil.isEmpty(supplierProcurement.getSupplierProcurementDetails().iterator().next().getFarmer())) {
			farmer = farmerService.findFarmerByFarmerId(supplierProcurement.getSupplierProcurementDetails().iterator().next().getFarmer().getFarmerId());
		}

		if (!ObjectUtil.isEmpty(farmer)) {
			agroTransaction.setFarmerId(farmer.getFarmerId());
			agroTransaction.setFarmerName(farmer.getFirstName() + " " + farmer.getLastName());
		} else {
			agroTransaction.setFarmerId(NOT_APPLICABLE);
			agroTransaction.setFarmerName(getFarmerName());
		}
		agroTransaction.setReceiptNo(idGenerator.getProcurementReceiptNoSeq());

		return agroTransaction;
	}

	private double calculateNetWeight(double grossWeight, double tareWeight) {

		return grossWeight - tareWeight;
	}

	private double calculateSubTotal(double netWeight, double ratePerUnit) {
		return netWeight * ratePerUnit;
	}
	private double calculateInvoiceValue(double totalLabourCost, double transportCost,double totalProdval,double taxAmt,double otherCost) {
		return totalLabourCost + transportCost + totalProdval+taxAmt+otherCost;
	}
	private double getTransactionAmount(SupplierProcurement supplierProcurement) {
		double txnAmount = 0;
		if (!ObjectUtil.isEmpty(supplierProcurement) && !ObjectUtil.isListEmpty(supplierProcurement.getSupplierProcurementDetails())) {
			for (SupplierProcurementDetail supplierProcurementDetail : supplierProcurement.getSupplierProcurementDetails()) {
				txnAmount = txnAmount + supplierProcurementDetail.getSubTotal();
			}
		}
		return txnAmount;
	}

	private void loadCurrentSeason() {

		ESESystem preference = preferncesService.findPrefernceById(ESESystem.SYSTEM_SWITCH);
		if (!ObjectUtil.isEmpty(preference)) {
			String currentSeasonCode = preference.getPreferences().get(ESESystem.CURRENT_SEASON_CODE);
			if (!StringUtil.isEmpty(currentSeasonCode)) {
				Season currentSeason = productDistributionService.findSeasonBySeasonCode(currentSeasonCode);
				if (!ObjectUtil.isEmpty(currentSeason))
					seasonId = currentSeason.getId();
			}
		}
	}

	/** Default Drop Down values */
	public Map<String, String> getAgentLists() {

		Map<String, String> returnMap = new LinkedHashMap<String, String>();
		List<Agent> agentLists = (List<Agent>) agentService.listAgentByAgentType(AgentType.FIELD_STAFF);
		if (!ObjectUtil.isListEmpty(agentLists)) {
			for (Agent agent : agentLists) {
				returnMap.put(String.valueOf(agent.getId()),
						agent.getProfileId() + "" + agent.getPersonalInfo().getFirstName());
			}
		}

		return returnMap;
	}

	public Map<Long, String> getListWarehouse() {

		List<Warehouse> warehouseList = locationService.listWarehouse();

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

	public List<ProcurementProduct> getProductList() {
		List<ProcurementProduct> listProduct = productDistributionService
				.listProcurementProductByType(Procurement.productType.GOODS.ordinal());
		return listProduct;
	}

	public Map<String, String> getRegType() {
		Map<String, String> regType = new LinkedHashMap<>();
		regType.put("0", getText("RegisterSupplier"));
		regType.put("1", getText("UnRegSupplier"));
		
		return regType;
	}

	public Map<String, String> getFarmerAttence() {

		farmerAttence.put("1", getText("ABSENT"));
		farmerAttence.put("0", getText("PRESENT"));

		return farmerAttence;
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
				villageArr.add(getJSONObject(obj.getCode(), obj.getName() + "-" + obj.getCode()));
			});
		}
		sendAjaxResponse(villageArr);
	}

	public void populateFarmer() throws Exception {
		if (!selectedVillage.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedVillage))) {
			List<Object[]> listFarmer = farmerService.listFarmerCodeIdNameByVillageCode(selectedVillage);

			JSONArray farmerArr = new JSONArray();
			if(getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)){
			listFarmer.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
				farmerArr.add(getJSONObject(obj[4], obj[2] + "" + (!StringUtil.isEmpty(obj[3]) ? (" " + obj[3]) : " ")
						+ (!StringUtil.isEmpty(obj[1]) ? ("-" + obj[1]) : " ")));
			});
			}
			else{
				listFarmer.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
					farmerArr.add(getJSONObject(obj[4], (obj[2] + " " +(!ObjectUtil.isEmpty(obj[3])?obj[3]:""))+" "+(!ObjectUtil.isEmpty(obj[5])?obj[5]:"")));
				});
			}
			sendAjaxResponse(farmerArr);
		}
	}

	public void populateVariety() throws Exception {

		if (!StringUtil.isEmpty(selectedProduct)) {
			List<ProcurementVariety> varietiesList = productDistributionService
					.listProcurementVarietyByProcurementProductId(Long.valueOf(selectedProduct));
			JSONArray varietyArr = new JSONArray();

			varietiesList.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
				varietyArr.add(getJSONObject(obj.getId(), obj.getName()));
			});

			sendAjaxResponse(varietyArr);
		}

	}

	public void populateUnit() throws Exception {

		String result = "";
		String code ="";
		JSONArray productArr = new JSONArray();
		if (!StringUtil.isEmpty(selectedProduct)) {
			ProcurementProduct procurementProduct = productService.findUnitByCropId(Long.valueOf(selectedProduct));

			if (!ObjectUtil.isEmpty(procurementProduct) && procurementProduct.getTypes() != null) {
				productAvailableUnit = String.valueOf(procurementProduct.getTypes().getName());
				productAvailableUnitCode=String.valueOf(procurementProduct.getTypes().getName());
				result = productAvailableUnit;
			}

			result = productAvailableUnit == null ? "" : productAvailableUnit;
		}
		productArr.add(getJSONObject("unit", result));

		sendAjaxResponse(productArr);
	}

	public String populateEditVariety() throws Exception {
		if (!StringUtil.isEmpty(selectedPro)) {
			Map<String, String> varieties = new LinkedHashMap<>();
			List<ProcurementVariety> varietiesList = productDistributionService
					.listProcurementVarietyByProcurementProductId(Long.valueOf(selectedPro));
			if (!ObjectUtil.isEmpty(varietiesList)) {
				for (ProcurementVariety procurementVariety : varietiesList) {
					// varieties.add(procurementVariety.getName() + " - " +
					// procurementVariety.getCode());
					varieties.put(procurementVariety.getName() + " # " + procurementVariety.getCode(),
							procurementVariety.getName());
					/*
					 * varieties.put( procurementVariety.getName() + " # " +
					 * procurementVariety.getCode(),
					 * procurementVariety.getName() + " - " +
					 * procurementVariety.getCode());
					 */
				}
			}
			printAjaxResponse(varieties, "text/html");
		}
		return null;

	}

	public void populateGrade() throws Exception {

		if (!StringUtil.isEmpty(selectedVariety)) {
			ProcurementVariety varierty = productDistributionService
					.findProcurementVariertyById(Long.valueOf(selectedVariety.trim()));

			if (!ObjectUtil.isEmpty(varierty)) {
				List<ProcurementGrade> gradeList = productDistributionService
						.listProcurementGradeByProcurementVarietyId(Long.valueOf(varierty.getId()));

				JSONArray gradeArr = new JSONArray();

				gradeList.stream().forEach(obj -> {
					gradeArr.add(getJSONObject(obj.getId(), obj.getName()));
				});
				sendAjaxResponse(gradeArr);
			}
		}
	}

	public String populateEditGrade() throws Exception {
		if (!StringUtil.isEmpty(selectedVar)) {
			List<String> grades = new ArrayList<String>();
			String[] varArr = selectedVar.split("#");
			String varValue = varArr[1];
			ProcurementVariety varierty = productDistributionService.findProcurementVariertyByCode(varValue.trim());
			List<ProcurementGrade> gradeList = productDistributionService
					.listProcurementGradeByProcurementVarietyId(Long.valueOf(varierty.getId()));
			if (!ObjectUtil.isEmpty(gradeList)) {
				for (ProcurementGrade procurementGrade : gradeList) {
					grades.add(procurementGrade.getName() + " - " + procurementGrade.getCode());
				}
			}
			printAjaxResponse(grades, "text/html");
		}
		return null;
	}

	public void populatePrice() throws Exception {
		if (!StringUtil.isEmpty(selectedGrade)) {
			ProcurementGrade grade = productDistributionService.findProcurementGradeById(Long.valueOf(selectedGrade));
			if (!ObjectUtil.isEmpty(grade)) {
				sendResponse(grade.getPrice());
			}
		}
	}

	public void populateMasterData() {
		JSONArray masterArr = new JSONArray();
		if (!StringUtil.isEmpty(selectedMasterType)) {
			getMasterDataList().stream().filter(masterData -> masterData.getMasterType().equals(selectedMasterType))
					.forEach(masterData -> {
						masterArr.add(getJSONObject(masterData.getCode(), masterData.getName()));
					});
			sendAjaxResponse(masterArr);
		}
	}

	public void populateFarm() {
		JSONArray farmArr = new JSONArray();
		if (!StringUtil.isEmpty(selectedFarmer)) {
			farmerService.findFarmInfoByFarmerId(Long.valueOf(selectedFarmer)).stream().forEach(obj -> {
				farmArr.add(getJSONObject(obj[0].toString(), obj[3].toString()));
			});
		}
		sendAjaxResponse(farmArr);
	}

	public Map<Integer, String> getMasterTypeList() {

		if (masterTypeList.size() == 0) {
			masterTypeList = getPropertyData("masterTypeList");
			return masterTypeList;
		}
		return masterTypeList;

	}

	public void setMasterTypeList(Map<Integer, String> masterTypeList) {

		this.masterTypeList = masterTypeList;
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

	public String getSelectedVariety() {
		return selectedVariety;
	}

	public void setSelectedVariety(String selectedVariety) {
		this.selectedVariety = selectedVariety;
	}

	public String getSelectedGrade() {
		return selectedGrade;
	}

	public void setSelectedGrade(String selectedGrade) {
		this.selectedGrade = selectedGrade;
	}

	public String getSelectedMobileUser() {
		return selectedMobileUser;
	}

	public void setSelectedMobileUser(String selectedMobileUser) {
		this.selectedMobileUser = selectedMobileUser;
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

	public String getSelectedFarmerType() {
		return selectedFarmerType;
	}

	public void setSelectedFarmerType(String selectedFarmerType) {
		this.selectedFarmerType = selectedFarmerType;
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

	public String getFarmerType() {
		return farmerType;
	}

	public void setFarmerType(String farmerType) {
		this.farmerType = farmerType;
	}

	public String getFarmerName() {
		return farmerName;
	}

	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Long getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(Long seasonId) {
		this.seasonId = seasonId;
	}

	public String getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getEnableSupplier() {
		return enableSupplier;
	}

	public void setEnableSupplier(String enableSupplier) {
		this.enableSupplier = enableSupplier;
	}

	public List<MasterData> getMasterDataList() {
		if (masterDataList.size() <= 0) {
			buildMasterDataList();
		}
		return masterDataList;
	}

	private void buildMasterDataList() {
		masterDataList = productDistributionService.listMasterDataByRevisionNo(0L);
	}

	public void setMasterDataList(List<MasterData> masterDataList) {
		this.masterDataList = masterDataList;
	}

	public String getSelectedMasterType() {
		return selectedMasterType;
	}

	public void setSelectedMasterType(String selectedMasterType) {
		this.selectedMasterType = selectedMasterType;
	}

	public String getSelectedSupplier() {
		return selectedSupplier;
	}

	public void setSelectedSupplier(String selectedSupplier) {
		this.selectedSupplier = selectedSupplier;
	}

	public String getProcurementDate() {
		return procurementDate;
	}

	public void setProcurementDate(String procurementDate) {
		this.procurementDate = procurementDate;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getFarmerAttnce() {
		return farmerAttnce;
	}

	public void setFarmerAttnce(String farmerAttnce) {
		this.farmerAttnce = farmerAttnce;
	}

	public String getSubstituteName() {
		return substituteName;
	}

	public String getRoadMapCode() {
		return roadMapCode;
	}

	public void setRoadMapCode(String roadMapCode) {
		this.roadMapCode = roadMapCode;
	}

	public String getSelectedFarm() {
		return selectedFarm;
	}

	public void setSelectedFarm(String selectedFarm) {
		this.selectedFarm = selectedFarm;
	}

	public void setSubstituteName(String substituteName) {
		this.substituteName = substituteName;
	}

	private Map<String, Object> getTotalProductDetails(Procurement procurement) {

		long noOfBags = 0;
		double grossWt = 0;
		/* double tareWt = 0; */
		double netWt = 0;
		long gradeId = 0;
		Map<String, Object> details = new HashMap<String, Object>();
		if (!ObjectUtil.isListEmpty(procurement.getProcurementDetails())) {
			for (ProcurementDetail detail : procurement.getProcurementDetails()) {
				noOfBags = noOfBags + detail.getNumberOfBags();
				grossWt = grossWt + detail.getGrossWeight();
				/* tareWt = tareWt + detail.getTareWeight(); */
				netWt = netWt + detail.getNetWeight();

				if (detail.getProcurementGrade() != null) {
					gradeId = detail.getProcurementGrade().getId();
				}
			}
		}
		details.put(NO_OF_BAGS, noOfBags);
		details.put(GROSS_WEIGHT, grossWt);
		/* details.put(TARE_WEIGHT, tareWt); */
		details.put(NET_WEIGHT, netWt);
		details.put(GRADE_IDS, gradeId);

		return details;
	}

	public String update() throws Exception {
		if (!StringUtil.isEmpty((String) this.id)) {
			this.supplierProcurement = this.productDistributionService.findSupplierProcurementById(Long.valueOf(this.id));
			CityWarehouse cityWarehouse = null;
			if (!ObjectUtil.isEmpty((Object) this.supplierProcurement)) {
				for (SupplierProcurementDetail supplierProcurementDetail : this.supplierProcurement.getSupplierProcurementDetails()) {
					if (ObjectUtil.isEmpty((Object) supplierProcurementDetail.getProcurementProduct())
							|| ObjectUtil.isEmpty((Object) supplierProcurementDetail.getProcurementGrade()))
						continue;
					cityWarehouse = this.productDistributionService.findByProdIdAndGradeCode(
							supplierProcurementDetail.getProcurementProduct().getId(),
							supplierProcurementDetail.getProcurementGrade().getCode(), this.getCurrentTenantId());
				}
				if (!(ObjectUtil.isEmpty((Object) cityWarehouse)
						|| ObjectUtil.isEmpty((Object) cityWarehouse.getCoOperative())
						|| StringUtil.isEmpty((String) cityWarehouse.getCoOperative().getName()))) {
					Warehouse warehouse = this.locationService
							.findCoOperativeByCode(cityWarehouse.getCoOperative().getCode());
					this.supplierProcurement.setWarehouseName(warehouse.getCode() + "-" + warehouse.getName());
				}
			}
			if (!StringUtil.isEmpty((String) this.supplierProcurement.getProcMasterType())) {
				this.setSupplierMaster(
						this.getMasterTypeList().get(Integer.parseInt(this.supplierProcurement.getProcMasterType())));
			}
			if (!StringUtil.isEmpty((String) this.supplierProcurement.getProcMasterTypeId())) {
				MasterData masterData = this.clientService
						.findMasterDataById(Long.valueOf(Long.parseLong(this.supplierProcurement.getProcMasterTypeId())));
				this.setSupplierType(masterData.getName());
			}
		}
		return "update";
	}

	public String getSupplierMaster() {

		return supplierMaster;
	}

	public void setSupplierMaster(String supplierMaster) {

		this.supplierMaster = supplierMaster;
	}

	public String getSupplierType() {

		return supplierType;
	}

	public void setSupplierType(String supplierType) {

		this.supplierType = supplierType;
	}

	public void populateProcurementDetailList() {
		// setEnableTraceability(preferncesService.findPrefernceByName(ESESystem.ENABLE_TRACEABILITY));
		JSONArray array = new JSONArray();
		this.supplierProcurement = this.productDistributionService.findSupplierProcurementById(Long.valueOf(this.procurementId));
		if (!ObjectUtil.isEmpty((Object) this.supplierProcurement)) {
			for (SupplierProcurementDetail procurementDetail : this.supplierProcurement.getSupplierProcurementDetails()) {
				JSONObject obj = new JSONObject();
				obj.put((Object) "variety",
						(Object) procurementDetail.getProcurementGrade().getProcurementVariety().getName());
				obj.put((Object) "grade", (Object) procurementDetail.getProcurementGrade().getName());
				obj.put((Object) "product", (Object) procurementDetail.getProcurementGrade().getProcurementVariety()
						.getProcurementProduct().getName());
				obj.put((Object) "productId", (Object) procurementDetail.getProcurementGrade().getProcurementVariety()
						.getProcurementProduct().getId());
				obj.put((Object) "price", (Object) procurementDetail.getProcurementGrade().getPrice());
				obj.put((Object) "gradeCode", (Object) procurementDetail.getProcurementGrade().getCode());
				obj.put((Object) "varietyCode",
						(Object) procurementDetail.getProcurementGrade().getProcurementVariety().getCode());
				obj.put((Object) "varietyId",
						(Object) procurementDetail.getProcurementGrade().getProcurementVariety().getId());
				obj.put((Object) "productUnit", (Object) procurementDetail.getProcurementGrade().getProcurementVariety()
						.getProcurementProduct().getUnit());
				obj.put((Object) "bags", (Object) (!StringUtil.isEmpty((Object) procurementDetail.getNumberOfBags())
						? Long.valueOf(procurementDetail.getNumberOfBags()) : "0.00"));
				// obj.put((Object)"batchNo",
				// (Object)(!StringUtil.isEmpty((Object)procurementDetail.getBatchNo())
				// ? procurementDetail.getBatchNo() : ""));
				obj.put((Object) "grossWeight",
						(Object) (!StringUtil.isEmpty((Object) procurementDetail.getGrossWeight())
								? Double.valueOf(procurementDetail.getGrossWeight()) : "0.00"));
			
				obj.put((Object) "netWeight", (Object) (!StringUtil.isEmpty((Object) procurementDetail.getNetWeight())
						? Double.valueOf(procurementDetail.getNetWeight()) : "0.00"));
				obj.put((Object) "id", (Object) procurementDetail.getId());
				array.add((Object) obj);
			}
			JSONObject mainObj = new JSONObject();
			mainObj.put((Object) "data", (Object) array);
			this.printAjaxResponse((Object) mainObj, "text/html");
		}
	}

	public String getProcurementId() {
		return this.procurementId;
	}

	public void setProcurementId(String procurementId) {
		this.procurementId = procurementId;
	}

	public void populateProcurementDetails() {
		// setEnableTraceability(preferncesService.findPrefernceByName(ESESystem.ENABLE_TRACEABILITY));
		String updateUser = (String) this.request.getSession().getAttribute("user");
		double subTot = 0.0;
		String[] details = this.procurementDetailArray.split("###");
		ProcurementDetail procurementDetails = this.productDistributionService
				.findByProcurementDetailId(Long.valueOf(details[2].trim()));
		procurementDetails.setNetWeight(Double.valueOf(details[1].trim()).doubleValue());
		procurementDetails.setGrossWeight(Double.valueOf(details[1].trim()).doubleValue());
		procurementDetails.setNumberOfBags(Long.valueOf(details[0].trim()).longValue());
		procurementDetails.getProcurement().setUpdatedDate(new Date());
		procurementDetails.getProcurement().setUpdatedUser(updateUser);
		if (this.getCurrentTenantId().equalsIgnoreCase("lalteer")
				&& !this.getCurrentTenantId().equalsIgnoreCase("awi")) {
			procurementDetails.setDryLoss(Double.valueOf(details[3].trim()));
			procurementDetails.setGradingLoss(Double.valueOf(details[4].trim()));
			double netWeg = procurementDetails.getDryLoss() + procurementDetails.getGradingLoss();
			subTot = procurementDetails.getGrossWeight() - netWeg;
			procurementDetails.setNetWeight(subTot);
			procurementDetails.setSubTotal(subTot);
		} else {
			subTot = procurementDetails.getRatePerUnit() * procurementDetails.getGrossWeight();
			procurementDetails.setSubTotal(subTot);
		}
		procurementDetails.getProcurement().setTotalProVal(subTot);
		procurementDetails.getProcurement().setInvoiceValue(subTot);
		procurementDetails.setTenantId(this.getCurrentTenantId());
		String nowDate = DateUtil.convertDateToString(new Date(), getGeneralDateFormat());
		procurementDetails.getProcurement()
				.setUpdatedDate(DateUtil.convertStringToDate(nowDate, getGeneralDateFormat()));

		procurementDetails.getProcurement().setUpdatedUser(getUsername());
		this.productDistributionService.updateProcurementDetails(procurementDetails);

		Procurement updatePrcurement = procurementDetails.getProcurement();
		Double totVal = updatePrcurement.getProcurementDetails().stream().mapToDouble(obj -> obj.getSubTotal()).sum();

		updatePrcurement.setTotalProVal(totVal);
		updatePrcurement.setInvoiceValue(totVal);
		/*
		 * updatePrcurement.getAgroTransaction().setIntBalance(totVal);
		 * updatePrcurement.getAgroTransaction().setIntBalance(totVal);
		 * updatePrcurement.getAgroTransaction().getAccount().setCashBalance(
		 * totVal);
		 */
		farmerService.update(updatePrcurement);
	}

	public String getProcurementDetailArray() {

		return procurementDetailArray;
	}

	public void setProcurementDetailArray(String procurementDetailArray) {

		this.procurementDetailArray = procurementDetailArray;
	}

	public String getSelectedPro() {

		return selectedPro;
	}

	public void setSelectedPro(String selectedPro) {

		this.selectedPro = selectedPro;
	}

	public String getSelectedVar() {

		return selectedVar;
	}

	public void setSelectedVar(String selectedVar) {

		this.selectedVar = selectedVar;
	}

	public List<ProcurementVariety> getVarietyList() {

		return varietyList;
	}

	public void setVarietyList(List<ProcurementVariety> varietyList) {

		this.varietyList = varietyList;
	}

	public String getEnableTraceability() {

		return enableTraceability;
	}

	public void setEnableTraceability(String enableTraceability) {

		this.enableTraceability = enableTraceability;
	}

	public IProductDistributionDAO getProductDistributionDAO() {
		return productDistributionDAO;
	}

	public void setProductDistributionDAO(IProductDistributionDAO productDistributionDAO) {
		this.productDistributionDAO = productDistributionDAO;
	}

	private AgroTransaction processProcurement(SupplierProcurement proc) {

		AgroTransaction txn = new AgroTransaction();
		txn.setDeviceId(NOT_APPLICABLE);
		txn.setDeviceName(NOT_APPLICABLE);
		txn.setBranch_id(proc.getBranchId());
		if (!StringUtil.isEmpty(this.procurementDate)) {
			try {
				txn.setTxnTime(DateUtil.convertStringToDate(this.procurementDate, getGeneralDateFormat()));
			
			} catch (Exception e) {
				String date = DateUtil.convertDateToString(new Date(), getGeneralDateFormat());
				txn.setTxnTime(DateUtil.convertStringToDate(date, getGeneralDateFormat()));

			}
		}
		txn.setTxnType(SUPPLIER_PROCUREMENT);
		txn.setProfType(Profile.CLIENT);
		txn.setOperType(ESETxn.ON_LINE);
		txn.setTxnDesc(SupplierProcurement.PROCURMEMENT);
		
		Double bal;
		if (!ObjectUtil.isEmpty(proc.getSupplierProcurementDetails())
				&& !ObjectUtil.isEmpty(proc.getSupplierProcurementDetails().iterator().next().getFarmer())) {
			ESEAccount farmerAccount = productDistributionDAO.findESEAccountByProfileId(proc.getSupplierProcurementDetails().iterator().next().getFarmer().getFarmerId(),
					ESEAccount.CONTRACT_ACCOUNT);
			bal = farmerAccount.getCashBalance();
			txn.setIntBalance(bal);
			txn.setTxnAmount(StringUtil.isDouble(getTotalAmount()) ? Double.valueOf(getTotalAmount()) : 0D);
			txn.setBalAmount(bal + proc.getTotalProVal());
			txn.setAccount(farmerAccount);
		} else {
			txn.setIntBalance(0.00);
			txn.setTxnAmount(StringUtil.isDouble(getTotalAmount()) ? Double.valueOf(getTotalAmount()) : 0D);
			txn.setBalAmount(0.00);
			// txn.setAccount(farmerAccount);
		}

		Warehouse warehouse = locationService.findWarehouseById(Long.valueOf(selectedWarehouse));
		txn.setWarehouse(warehouse);
		txn.setSamithi(warehouse);

		if (!StringUtil.isEmpty(selectedMobileUser)) {
			Agent agent = agentService.findAgent(Long.valueOf(selectedMobileUser));
			txn.setAgentId(agent.getProfileId());
			txn.setAgentName(agent.getPersonalInfo().getFirstName());
		}

		Farmer farmer = null;
		if (!ObjectUtil.isEmpty(proc.getSupplierProcurementDetails()) 
				&& !ObjectUtil.isEmpty(proc.getSupplierProcurementDetails().iterator().next().getFarmer())) {
			farmer = farmerService.findFarmerByFarmerId(proc.getSupplierProcurementDetails().iterator().next().getFarmer().getFarmerId());
		}

		if (!ObjectUtil.isEmpty(farmer)) {
			txn.setFarmerId(farmer.getFarmerId());
			txn.setFarmerName(farmer.getFirstName() + " " + farmer.getLastName());
		} else {
			txn.setFarmerId(NOT_APPLICABLE);
			txn.setFarmerName(getFarmerName());
		}
		txn.setReceiptNo(proc.getAgroTransaction().getReceiptNo());
		txn.setSeasonCode(proc.getSeasonCode());
		productDistributionService.saveAgroTransaction(txn);

		return txn;

	}

	public String getProductAvailableUnit() {
		return productAvailableUnit;
	}

	public void setProductAvailableUnit(String productAvailableUnit) {
		this.productAvailableUnit = productAvailableUnit;
	}

	public String getSelectedCropType() {
		return selectedCropType;
	}

	public void setSelectedCropType(String selectedCropType) {
		this.selectedCropType = selectedCropType;
	}

	public Map<String, String> getCTypes() {
		Map<String, String> cType = new LinkedHashMap<>();
		cType.put("1", getLocaleProperty("cpType1"));
		cType.put("0", getLocaleProperty("cpType0"));
		return cType;
	}

	public void populateFarmerByFPO() {
		JSONArray farmerArr = new JSONArray();
		if (!StringUtil.isEmpty(selectedSupplier)) {
			if(getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)){
			farmerService.findFarmerByGroup(Long.valueOf(selectedSupplier)).stream().filter(farmer -> (!ObjectUtil.isEmpty(farmer)))
					.forEach(farmer -> {
						farmerArr
								.add(getJSONObject(farmer[0], (!StringUtil.isEmpty(farmer[3]) ? (" " + farmer[3]) : " ")
										));
					});
			}
			else{
				farmerService.findFarmerByGroup(Long.valueOf(selectedSupplier)).stream().filter(farmer -> (!ObjectUtil.isEmpty(farmer)))
				.forEach(farmer -> {
					farmerArr.add(getJSONObject(farmer[0].toString(), ((!ObjectUtil.isEmpty(farmer[3])?farmer[3]:"")+" "+(!ObjectUtil.isEmpty(farmer[4])?farmer[4]:"")+" "+(!ObjectUtil.isEmpty(farmer[5])?farmer[5]:""))));
				});
			}
		}
		sendAjaxResponse(farmerArr);
	}
	
	@SuppressWarnings("unchecked")
	public void populateSamithi() throws Exception {

		samithi = locationService.listSamithiesBasedOnType();
		JSONArray warehouseArr = new JSONArray();
		;
		samithi.stream().filter(samithi -> !ObjectUtil.isEmpty(samithi)).forEach(samithi -> {
			warehouseArr.add(getJSONObject(samithi.getId(), samithi.getName()));
		});
		sendAjaxResponse(warehouseArr);
	}

	public void populateCatalogueData() {
		JSONArray fpoArr = new JSONArray();
		farmerService.listCatelogueType(getLocaleProperty("fpoType")).stream().forEach(catalogue -> {
			fpoArr.add(getJSONObject(catalogue.getCode(), catalogue.getName()));
		});
		sendAjaxResponse(fpoArr);
	}

	public List<Warehouse> getSamithi() {
		return samithi;
	}

	public void setSamithi(List<Warehouse> samithi) {
		this.samithi = samithi;
	}

	public String getProductAvailableUnitCode() {
		return productAvailableUnitCode;
	}

	public void setProductAvailableUnitCode(String productAvailableUnitCode) {
		this.productAvailableUnitCode = productAvailableUnitCode;
	}
	
	public List<FarmCatalogue> getListUom() {
		List<FarmCatalogue> subUomList = catalogueService.listCataloguesByUnit();

		return subUomList;
	}

	public int getIsSupplier() {
		return isSupplier;
	}

	public void setIsSupplier(int isSupplier) {
		this.isSupplier = isSupplier;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public Map<String, String> getTraderList() {

		Map<String, String> masterTypeMap = new LinkedHashMap<String, String>();
		List<MasterData> masterTypeList = farmerService.listMasterType();
		for (MasterData obj : masterTypeList) {
			masterTypeMap.put(obj.getCode(), obj.getName());
		}
		return masterTypeMap;
	}

	public String getSelectedTrader() {
		return selectedTrader;
	}

	public void setSelectedTrader(String selectedTrader) {
		this.selectedTrader = selectedTrader;
	}

	public String getTotalLabourCost() {
		return totalLabourCost;
	}

	public void setTotalLabourCost(String totalLabourCost) {
		this.totalLabourCost = totalLabourCost;
	}

	public String getTransportCost() {
		return transportCost;
	}

	public void setTransportCost(String transportCost) {
		this.transportCost = transportCost;
	}

	public String getTaxAmt() {
		return taxAmt;
	}

	public void setTaxAmt(String taxAmt) {
		this.taxAmt = taxAmt;
	}

	public String getOtherCost() {
		return otherCost;
	}

	public void setOtherCost(String otherCost) {
		this.otherCost = otherCost;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	
	
}
