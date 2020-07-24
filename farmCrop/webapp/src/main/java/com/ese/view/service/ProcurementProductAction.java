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
import java.text.DateFormat;
import java.text.DecimalFormat;
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

import com.ese.entity.util.ESESystem;
import com.ese.entity.util.LoanInterest;
import com.lowagie.text.Row;
import com.sourcetrace.eses.dao.IProductDistributionDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.AgentType;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.CityWarehouse;
import com.sourcetrace.eses.order.entity.txn.PaymentLedger;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.order.entity.txn.ProcurementDetail;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Farm;
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
public class ProcurementProductAction extends WebTransactionAction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ProcurementProductAction.class);

	public static final String PROCUREMENT_PRODUCT_ENROLLMENT = "316";
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
	private static final String NO_OF_FRUIT_BAGS= "fruitBags";
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
	private String selectedBuyer;

	public String getSelectedBuyer() {
		return selectedBuyer;
	}

	public void setSelectedBuyer(String selectedBuyer) {
		this.selectedBuyer = selectedBuyer;
	}

	private String farmerType;
	private String farmerName;
	private String mobileNumber;
	private String paymentAmount;
	private String totalAmount;
	private String enableSupplier;
	private String enableBuyer;
	private String selectedFarmerValue;
	private ESEAccount account;
	private String cashCreditValue;
	private String noOfCrates;

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
	private List<ProcurementVariety> varietyList = new ArrayList<ProcurementVariety>();
	private String enableTraceability;
	private String enableLoanModule;
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
	private String id;
	private Procurement procurement;
	private String productAvailableUnit;
	private String productAvailableUnitCode;
	private String selectedCropType;
	private Double transport_cost;
	private String vehicleType;
	private String buyerName;
	private Procurement filter;
	private double actualAmt;
	private String selectedLoanInt;
	private String selectedFinalAmt;
	private double loanAmt;
	List<Warehouse> samithi = new ArrayList<Warehouse>();
	private double repaymentAmt;
	public Procurement getProcurement() {
		return procurement;
	}

	public void setProcurement(Procurement procurement) {
		this.procurement = procurement;
	}
	DecimalFormat df1 = new DecimalFormat("0.00");		
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

	@SuppressWarnings("rawtypes")
	public String data() throws Exception {
		getJQGridRequestParam();
		filter = new Procurement();
		if(getCurrentTenantId().equalsIgnoreCase(ESESystem.AWI_TENANT_ID)){
			filter.setStatus(0);
		}
		
		
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
			if(!StringUtil.isEmpty(procurement.getWarehouseId())){
				Warehouse warehouse =locationService.findWarehouseById(Long.valueOf(procurement.getWarehouseId()));
				rows.add(!ObjectUtil.isEmpty(warehouse) ? warehouse.getName() : "N/A");
			}else{
				rows.add("N/A");
			}
			
			Map<String, Object> details = getTotalProductDetails(procurement);
	/*		if (details.get(GRADE_IDS) != null) {
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
			}*/
			if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)
					&& !getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
				rows.add(details.get(NO_OF_BAGS).toString());
			}
			if(getCurrentTenantId().equalsIgnoreCase(ESESystem.AWI_TENANT_ID)){
				rows.add(details.get(NO_OF_FRUIT_BAGS).toString());
			}
			rows.add(CurrencyUtil.getDecimalFormat((Double) details.get(NET_WEIGHT), "##.00"));
			
			if (!StringUtil.isEmpty(procurement.getTotalProVal())) {
				rows.add(df1.format(procurement.getTotalProVal()));
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
	
		ESESystem preferences = preferncesService.findPrefernceById("1");	
		setEnableSupplier(preferences.getPreferences().get(ESESystem.ENABLE_SUPPLIER));
		setEnableTraceability(preferences.getPreferences().get(ESESystem.ENABLE_TRACEABILITY));
		setEnableBuyer(preferences.getPreferences().get(ESESystem.ENABLE_BUYER));
		setNoOfCrates(preferences.getPreferences().get(ESESystem.NO_OF_CRATES));
		setEnableLoanModule(preferences.getPreferences().get(ESESystem.ENABLE_LOAN_MODULE));
		return INPUT;
	}

	public String detail() throws Exception {
		ESESystem preferences = preferncesService.findPrefernceById("1");			
		setEnableTraceability(preferences.getPreferences().get(ESESystem.ENABLE_TRACEABILITY));		
		setEnableSupplier(preferences.getPreferences().get(ESESystem.ENABLE_SUPPLIER));
		procurement = productDistributionService.findProcurementById(Long.valueOf(id));

		if (!ObjectUtil.isEmpty(procurement)) {
			for (ProcurementDetail procurementDetail : procurement.getProcurementDetails()) {
				if (!ObjectUtil.isEmpty(procurementDetail.getProcurementProduct())
						&& (!ObjectUtil.isEmpty(procurementDetail.getProcurementGrade()))) {
					if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)) {
						cityWarehouse = productDistributionService.findByProdIdAndGradeCode(
								procurementDetail.getProcurementProduct().getId(),
								procurementDetail.getProcurementGrade().getCode(), getCurrentTenantId());
					} else {
						cityWarehouse = productDistributionService
								.findCityWarehouseIdByFarmerAndProductIdAndGradeCode(procurement.getFarmer().getId(),
										procurementDetail.getProcurementGrade().getProcurementVariety()
												.getProcurementProduct().getId(),
										procurementDetail.getProcurementGrade().getCode());
					}
				}
			}

			this.setSupplierMaster(this.procurement.getProcMasterType() != null
					? this.getMasterTypeList().get(Integer.parseInt(this.procurement.getProcMasterType())) : "");

			if (!StringUtil.isEmpty((String) this.procurement.getProcMasterTypeId())) {
				if ("99".equalsIgnoreCase(this.procurement.getProcMasterType())
						|| "11".equalsIgnoreCase(this.procurement.getProcMasterType())) {
					Warehouse warehouse = locationService.findSamithiByCode(this.procurement.getProcMasterTypeId());
					this.setSupplierType(warehouse.getName());
				} else {
					MasterData masterData = this.clientService
							.findMasterDataById(Long.valueOf(Long.parseLong(this.procurement.getProcMasterTypeId())));
					this.setSupplierType(masterData.getName());
				}
			}
			/*if (!ObjectUtil.isEmpty(cityWarehouse) && !ObjectUtil.isEmpty(cityWarehouse.getCoOperative())) {

				if (!StringUtil.isEmpty(cityWarehouse.getCoOperative().getName())) {
					Warehouse warehouse = locationService
							.findCoOperativeByCode(cityWarehouse.getCoOperative().getCode());
					if (!ObjectUtil.isEmpty(warehouse))
						procurement.setWarehouseName(warehouse.getCode() + "-" + warehouse.getName());
				}
			}
*/
			
			if (!StringUtil.isEmpty(procurement.getWarehouseId())) {
				Warehouse warehouse = locationService.findWarehouseById(Long.valueOf(procurement.getWarehouseId()));
				if (!ObjectUtil.isEmpty(warehouse))
					procurement.setWarehouseName(warehouse.getCode() + "-" + warehouse.getName());
			}
				
			if (!StringUtil.isEmpty(procurement.getVehicleType())) {
				String catalogueCode[] = procurement.getVehicleType().split(",");
				String vehicleType = "";
				for (int i = 0; i < catalogueCode.length; i++) {
					FarmCatalogue farmCatalogue = getCatlogueValueByCode(catalogueCode[i].trim());
					if (!ObjectUtil.isEmpty(farmCatalogue)) {
						if (catalogueCode.length == i + 1) {
							vehicleType = vehicleType + farmCatalogue.getName();
						} else {
							vehicleType = vehicleType + farmCatalogue.getName() + ",";
						}

					}
				}

				setVehicleType(vehicleType);
			}
			if (!ObjectUtil.isEmpty(procurement.getBuyer())
					&& !StringUtil.isEmpty(procurement.getBuyer().getCustomerName())) {
				setBuyerName(procurement.getBuyer().getCustomerName());
			}

		} else {
			return LIST;
		}
		return DETAIL;
	}

	public void populateProcurement() {
		loadCurrentSeason();
		String receiptNumber;
		Farmer farmer =null;
		ESESystem preferences = preferncesService.findPrefernceById("1");	
		setEnableSupplier(preferences.getPreferences().get(ESESystem.ENABLE_SUPPLIER));
		setEnableTraceability(preferences.getPreferences().get(ESESystem.ENABLE_TRACEABILITY));
		setEnableBuyer(preferences.getPreferences().get(ESESystem.ENABLE_BUYER));
		setEnableLoanModule(preferences.getPreferences().get(ESESystem.ENABLE_LOAN_MODULE));
		/** Form Procurement Detail Object */
		Set<ProcurementDetail> procurementDetails = formProcurementDetails();

		Season season = productDistributionService.findSeasonById(seasonId);

		Procurement procurement = new Procurement();
		procurement.setProcurementDetails(procurementDetails);

		Village village = locationService.findVillageByCode(selectedVillage);
		procurement.setType(Integer.valueOf(getFarmerType()));
		procurement.setVillage(village);
		procurement.setSeason(season);
		procurement.setSeasonCode(getCurrentSeasonsCode());
		procurement.setPaymentAmount(StringUtil.isDouble(getPaymentAmount()) ? Double.valueOf(getPaymentAmount()) : 0D);
		procurement.setTotalProVal(StringUtil.isDouble(getTotalAmount()) ? Double.valueOf(getTotalAmount()) : 0D);
		procurement.setInvoiceValue(StringUtil.isDouble(getTotalAmount()) ? Double.valueOf(getTotalAmount()) : 0D);
		// String nowDate = DateUtil.convertDateToString(new Date(),
		// getGeneralDateFormat());
		if(!StringUtil.isEmpty(getEnableLoanModule()) && getEnableLoanModule().equalsIgnoreCase("1")){
			procurement.setActualAmount(StringUtil.isDouble(getTotalAmount()) ? Double.valueOf(getTotalAmount()) : 0D);
			procurement.setLoanInterest(StringUtil.isDouble(getSelectedLoanInt()) ? Double.valueOf(getSelectedLoanInt()) : 0D);
			procurement.setLoanAmount(StringUtil.isDouble(getRepaymentAmt()) ? getRepaymentAmt() : 0D);
			procurement.setFinalPayAmt(StringUtil.isDouble(getSelectedFinalAmt()) ? Double.valueOf(getSelectedFinalAmt()) : 0D);
		}
	

		procurement.setCreatedUser(getUsername());
		procurement.setCreatedDate(DateUtil.convertStringToDate(this.procurementDate, getGeneralDateFormat()));
		// procurement.setCreatedDate(DateUtil.setTimeToDate(procurement.getCreatedDate()));
		procurement.setCropType(getSelectedCropType());
		procurement.setTransportCost(getTransport_cost());
		procurement.setVehicleType(getVehicleType());
		procurement.setStatus(0);
		/*
		 * procurement.setUpdatedDate(DateUtil.convertStringToDate(nowDate,
		 * getGeneralDateFormat()));
		 * procurement.setUpdatedDate(DateUtil.setTimeToDate(procurement.
		 * getUpdatedDate())); procurement.setUpdatedUser(getUsername());
		 */
		procurement.setBranchId(getBranchId());
		Warehouse warehouse = locationService.findWarehouseById(Long.valueOf(selectedWarehouse));
		procurement.setWarehouseId(String.valueOf(warehouse.getId()));
		
		procurement.setPaymentMode(ESEAccount.PAYMENT_MODE_CREDIT);
		if(!StringUtil.isEmpty(getSelectedFarmer())) {
			farmer = farmerService.findFarmerById(Long.valueOf(getSelectedFarmer()));
		}
		
		if (!StringUtil.isEmpty(getEnableSupplier()) && getEnableSupplier().equals("1")) {
			procurement.setProcMasterType(getSelectedSupplier());

			if (getSelectedSupplier().equals("99") || getSelectedSupplier().equals("11")) {

				if (getSelectedSupplier().equals("99")) {
					if (getFarmerType().equalsIgnoreCase(REGISTERED_FARMER)) {
						//Farmer farmer = farmerService.findFarmerById(Long.valueOf(getSelectedFarmer()));
						procurement.setFarmer(!ObjectUtil.isEmpty(farmer) ? farmer : null);
						procurement.setProcMasterTypeId(
								!ObjectUtil.isEmpty(farmer) && !ObjectUtil.isEmpty(farmer.getSamithi())
										&& !StringUtil.isEmpty(farmer.getSamithi().getGroupType())
												? farmer.getSamithi().getGroupType() : "");
					} else {
						procurement.setMobileNumber(getMobileNumber());
					}
				} else {
					//Farmer farmer = farmerService.findFarmerById(Long.valueOf(getSelectedFarmer()));
					procurement.setFarmer(!ObjectUtil.isEmpty(farmer) ? farmer : null);
					procurement
							.setProcMasterTypeId(!ObjectUtil.isEmpty(farmer) && !ObjectUtil.isEmpty(farmer.getSamithi())
									&& !StringUtil.isEmpty(farmer.getSamithi().getCode())
											? farmer.getSamithi().getCode() : "");
				}
			} else {
				MasterData master = farmerService.findMasterDataIdByCode(getSelectedMasterType());
				procurement.setProcMasterTypeId(!ObjectUtil.isEmpty(master) ? String.valueOf(master.getId()) : "");
			}
		} else {
			if (getFarmerType().equalsIgnoreCase(REGISTERED_FARMER)) {
				//Farmer farmer = farmerService.findFarmerById(Long.valueOf(getSelectedFarmer()));
				procurement.setFarmer(farmer);
				procurement.setProcMasterType("-1");
			} else {
				procurement.setMobileNumber(getMobileNumber());
			}
		}
		if (!StringUtil.isEmpty(getEnableBuyer()) && getEnableBuyer().equals("1")) {
			Customer buyer = clientService.findCustomer(Long.valueOf(selectedBuyer));
			procurement.setBuyer(buyer);
		}

		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.AWI_TENANT_ID)) {
			procurement.setRoadMapCode(getRoadMapCode());
			procurement.setVehicleNum(getVehicleNo());
			procurement.setFarmerAttnce(getFarmerAttnce());
			procurement.setSubstituteName(getSubstituteName());
			if (!StringUtil.isEmpty(selectedFarm)) {
				Farm farm = farmerService.findFarmByfarmId(Long.valueOf(getSelectedFarm()));
			
					procurement.setFarmId(farm.getFarmId());
			
			}
		}
		procurement.setAgroTransaction(formAgroTxn(procurement,warehouse));
		processProcurement(procurement,warehouse);

		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.LALTEER_TENANT_ID)) {
			productDistributionService.addProcurement1(procurement);
		} 
		if(getCurrentTenantId().equalsIgnoreCase(ESESystem.AWI_TENANT_ID)){
			productDistributionService.addUnapprovedProcurement(procurement,getCurrentTenantId());
		}
		else {
			
			productDistributionService.addProcurement(procurement);
		}
		if (!ObjectUtil.isEmpty(procurement) && !ObjectUtil.isEmpty(procurement.getAgroTransaction())
				&& !StringUtil.isEmpty(procurement.getAgroTransaction().getReceiptNo()))
			receiptNumber = procurement.getAgroTransaction().getReceiptNo();

		JSONArray respArr = new JSONArray();
		respArr.add(getJSONObject("data", "success"));
		respArr.add(getJSONObject("receiptNumber", procurement.getAgroTransaction().getReceiptNo()));
		sendAjaxResponse(respArr);
	}

	public void populateFarmerAccBalance() throws Exception {
		ESESystem preferences = preferncesService.findPrefernceById("1");	
		
		setEnableLoanModule(preferences.getPreferences().get(ESESystem.ENABLE_LOAN_MODULE));
		String cash = "";
		if (!StringUtil.isEmpty(selectedFarmerValue)) {
			Farmer farmer = farmerService.findFarmerById(Long.valueOf(selectedFarmerValue));
			account = accountService.findAccountByProfileIdAndProfileType(farmer.getFarmerId(),
					ESEAccount.CONTRACT_ACCOUNT);
			
			if (!StringUtil.isEmpty(account)) {
				if(getEnableLoanModule().equalsIgnoreCase("1")){
					cashCreditValue = (account.getLoanAmount() + "," + account.getOutstandingLoanAmount()+ "," +account.getCashBalance());
				}else{
					cashCreditValue = (account.getCashBalance() + "," + account.getCreditBalance());
				}
				cash = cashCreditValue;
			} else {
				cashCreditValue = "";
			}
			cash = cashCreditValue;

		}
		response.getWriter().print(cash);

	}

	private Set<ProcurementDetail> formProcurementDetails() {
		Set<ProcurementDetail> procurementDetails = new LinkedHashSet<>();

		if (!StringUtil.isEmpty(getProductTotalString())) {
			List<String> productsList = Arrays.asList(getProductTotalString().split("@"));

			productsList.stream().filter(obj -> !StringUtil.isEmpty(obj)).forEach(products -> {
				ProcurementDetail procurementDetail = new ProcurementDetail();
				List<String> list = Arrays.asList(products.split("#"));
				Double grossWeight = Double.parseDouble(list.get(4));

				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.LALTEER_TENANT_ID)) {
					ProcurementVariety variety = productDistributionService
							.findProcurementVariertyById((Long.valueOf(list.get(1))));
					ProcurementGrade grade = productDistributionService
							.findProcurementGradeByVarityId((Long.valueOf(list.get(1))));
					procurementDetail.setProcurementProduct(variety.getProcurementProduct());
					procurementDetail.setProcurementGrade(grade);

				} else {
					ProcurementGrade grade = productDistributionService
							.findProcurementGradeById(Long.valueOf(list.get(2)));
					procurementDetail.setProcurementGrade(grade);
					procurementDetail.setProcurementProduct(grade.getProcurementVariety().getProcurementProduct());
					procurementDetail.setRatePerUnit(grade.getPrice());
					// if(getCurrentTenantId().equalsIgnoreCase("kpf"))
					/*
					 * if (getIsKpfBased().equals("1"))
					 * procurementDetail.setUnit(list.size() >= 9 ? list.get(9)
					 * : ""); else
					 */
					procurementDetail.setUnit(grade.getProcurementVariety().getProcurementProduct().getUnit());

				}
				if (!getCurrentTenantId().equals(ESESystem.PRATIBHA_TENANT_ID)) {
					procurementDetail.setNumberOfBags(Long.valueOf(list.get(3)));
				}
				if(getCurrentTenantId().equalsIgnoreCase(ESESystem.AWI_TENANT_ID)){
					procurementDetail.setNumberOfFruitBags(String.valueOf(list.get(10)));
				}
				procurementDetail.setGrossWeight(grossWeight);
				// procurementDetail.setUnit(list.size()>=9?list.get(9):"");
				if (enableTraceability.equalsIgnoreCase("1")) {
					procurementDetail.setBatchNo(!ObjectUtil.isEmpty(list.get(8)) ? list.get(8) : "");
				}
				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.LALTEER_TENANT_ID)) {
					Double subTotal = 0.00;
					procurementDetail.setDryLoss(Double.valueOf(list.get(6)));
					procurementDetail.setGradingLoss(Double.valueOf(list.get(7)));
					Double totalLoss = Double.parseDouble(list.get(6)) + Double.parseDouble(list.get(7));
					Double netWeight = grossWeight - totalLoss;
					procurementDetail.setNetWeight(netWeight);
					procurementDetail.setSubTotal(subTotal);

				}
				procurementDetail
						.setTareWeight(StringUtil.isDouble(preferncesService.findPrefernceByName(ESESystem.TARE_WEIGHT))
								? Double.valueOf(preferncesService.findPrefernceByName(ESESystem.TARE_WEIGHT)) : 0D);

				if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.LALTEER_TENANT_ID)) {
					procurementDetail.setNetWeight(
							calculateNetWeight(procurementDetail.getGrossWeight(), procurementDetail.getTareWeight()));

					procurementDetail.setSubTotal(
							calculateSubTotal(procurementDetail.getNetWeight(), procurementDetail.getRatePerUnit()));
				}
				procurementDetails.add(procurementDetail);
			});

		}

		return procurementDetails;
	}

	private AgroTransaction formAgroTxn(Procurement procurement, Warehouse warehouse) {
		AgroTransaction agroTransaction = new AgroTransaction();
		agroTransaction.setDeviceId(NOT_APPLICABLE);
		agroTransaction.setDeviceName(NOT_APPLICABLE);
		agroTransaction.setBranch_id(procurement.getBranchId());
		if (!StringUtil.isEmpty(this.procurementDate)) {
			try {
				agroTransaction.setTxnTime(DateUtil.convertStringToDate(this.procurementDate, getGeneralDateFormat()));
				// agroTransaction.setTxnTime(DateUtil.setTimeToDate(agroTransaction.getTxnTime()));
			} catch (Exception e) {
				String nowDate = DateUtil.convertDateToString(new Date(), getGeneralDateFormat());
				agroTransaction.setTxnTime(DateUtil.convertStringToDate(nowDate, getGeneralDateFormat()));

				// agroTransaction.setTxnTime(DateUtil.setTimeToDate(agroTransaction.getTxnTime()));

			}
		}
		agroTransaction.setTxnType(PROCUREMENT_PRODUCT_ENROLLMENT);
		agroTransaction.setProfType(Profile.CLIENT);
		agroTransaction.setOperType(ESETxn.ON_LINE);
		agroTransaction.setTxnDesc(Procurement.PROCUREMENT_AMOUNT);
		agroTransaction.setTxnAmount(getTransactionAmount(procurement));
		agroTransaction.setProcurement(procurement);

		//Warehouse warehouse = locationService.findWarehouseById(Long.valueOf(selectedWarehouse));
		agroTransaction.setWarehouse(warehouse);
		agroTransaction.setSamithi(warehouse);

		if (!StringUtil.isEmpty(selectedMobileUser)) {
			Agent agent = agentService.findAgent(Long.valueOf(selectedMobileUser));
			agroTransaction.setAgentId(agent.getProfileId());
			agroTransaction.setAgentName(agent.getPersonalInfo().getFirstName());
		}

		/*Farmer farmer = null;
		if (!StringUtil.isEmpty(selectedFarmer)) {
			farmer = farmerService.findFarmerById(Long.valueOf(selectedFarmer));
		}*/

		if (!ObjectUtil.isEmpty(procurement.getFarmer())) {
			agroTransaction.setFarmerId(procurement.getFarmer().getFarmerId());
			agroTransaction.setFarmerName(!StringUtil.isEmpty(procurement.getFarmer().getLastName()) ? procurement.getFarmer().getFirstName() + " " + procurement.getFarmer().getLastName() :  procurement.getFarmer().getFirstName());
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

	private double getTransactionAmount(Procurement procurement) {
		double txnAmount = 0;
		if (!ObjectUtil.isEmpty(procurement) && !ObjectUtil.isListEmpty(procurement.getProcurementDetails())) {
			for (ProcurementDetail procurementDetail : procurement.getProcurementDetails()) {
				txnAmount = txnAmount + procurementDetail.getSubTotal();
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
		regType.put("0", getText("REG"));
		regType.put("1", getText("UNREG"));

		return regType;
	}

	public Map<String, String> getFarmerAttence() {

		farmerAttence.put("1", getText("ABSENT"));
		farmerAttence.put("0", getText("PRESENT"));

		return farmerAttence;
	}

	/** Populate Methods */
	public void populateVillage() {
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
			listFarmer.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
				farmerArr.add(getJSONObject(obj[4], obj[2] + "" + (!StringUtil.isEmpty(obj[3]) ? (" " + obj[3]) : " ")
						+ (!StringUtil.isEmpty(obj[1]) ? ("-" + obj[1]) : " ")));
			});
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
		String code = "";
		JSONArray productArr = new JSONArray();
		if (!StringUtil.isEmpty(selectedProduct)) {
			ProcurementProduct procurementProduct = productService.findUnitByCropId(Long.valueOf(selectedProduct));

			if (!ObjectUtil.isEmpty(procurementProduct) && procurementProduct.getTypes() != null) {
				productAvailableUnit = String.valueOf(procurementProduct.getTypes().getName());
				productAvailableUnitCode = String.valueOf(procurementProduct.getTypes().getName());
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
		long noOfFruitBags=0;
		/* double tareWt = 0; */
		double netWt = 0;
		long gradeId = 0;
		Map<String, Object> details = new HashMap<String, Object>();
		if (!ObjectUtil.isListEmpty(procurement.getProcurementDetails())) {
			for (ProcurementDetail detail : procurement.getProcurementDetails()) {
				noOfBags = noOfBags + detail.getNumberOfBags();
				noOfFruitBags = noOfFruitBags + (detail.getNumberOfFruitBags()!=null && !StringUtil.isEmpty(detail.getNumberOfFruitBags())?Long.parseLong(detail.getNumberOfFruitBags()):0);
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
		if(getCurrentTenantId().equalsIgnoreCase(ESESystem.AWI_TENANT_ID))
			details.put(NO_OF_FRUIT_BAGS, noOfFruitBags);
		/* details.put(TARE_WEIGHT, tareWt); */
		details.put(NET_WEIGHT, netWt);
		details.put(GRADE_IDS, gradeId);

		return details;
	}

	public String update() throws Exception {
		
		if (!StringUtil.isEmpty((String) this.id)) {
			this.procurement = this.productDistributionService.findProcurementById(Long.valueOf(this.id));
			CityWarehouse cityWarehouse = null;
			if (!ObjectUtil.isEmpty((Object) this.procurement)) {
				for (ProcurementDetail procurementDetail : this.procurement.getProcurementDetails()) {
					if (ObjectUtil.isEmpty((Object) procurementDetail.getProcurementProduct())
							|| ObjectUtil.isEmpty((Object) procurementDetail.getProcurementGrade()))
						continue;
					cityWarehouse = this.productDistributionService.findByProdIdAndGradeCode(
							procurementDetail.getProcurementProduct().getId(),
							procurementDetail.getProcurementGrade().getCode(), this.getCurrentTenantId());
				}
				/*if (!(ObjectUtil.isEmpty((Object) cityWarehouse)
						|| ObjectUtil.isEmpty((Object) cityWarehouse.getCoOperative())
						|| StringUtil.isEmpty((String) cityWarehouse.getCoOperative().getName()))) {
					Warehouse warehouse = this.locationService
							.findCoOperativeByCode(cityWarehouse.getCoOperative().getCode());
					this.procurement.setWarehouseName(warehouse.getCode() + "-" + warehouse.getName());
				}*/
				
				if(!StringUtil.isEmpty(procurement.getWarehouseId())){
					Warehouse warehouse =locationService.findWarehouseById(Long.valueOf(procurement.getWarehouseId()));
					if (!ObjectUtil.isEmpty(warehouse))
						procurement.setWarehouseName(warehouse.getCode() + "-" + warehouse.getName());
				}
			}
			if (!StringUtil.isEmpty((String) this.procurement.getProcMasterType())) {
				this.setSupplierMaster(
						this.getMasterTypeList().get(Integer.parseInt(this.procurement.getProcMasterType())));
			}

			if (!StringUtil.isEmpty((String) this.procurement.getProcMasterTypeId())) {
				if ("99".equalsIgnoreCase(this.procurement.getProcMasterType())
						|| "11".equalsIgnoreCase(this.procurement.getProcMasterType())) {
					Warehouse warehouse = locationService.findSamithiByCode(this.procurement.getProcMasterTypeId());
					this.setSupplierType(warehouse.getName());
				} else {
					MasterData masterData = this.clientService
							.findMasterDataById(Long.valueOf(Long.parseLong(this.procurement.getProcMasterTypeId())));
					this.setSupplierType(masterData.getName());
				}
			}
			/*
			 * if (!StringUtil.isEmpty((String)
			 * this.procurement.getProcMasterTypeId())) { MasterData masterData
			 * = this.clientService
			 * .findMasterDataById(Long.valueOf(Long.parseLong(this.procurement.
			 * getProcMasterTypeId())));
			 * this.setSupplierType(masterData.getName()); }
			 */
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
		this.procurement = this.productDistributionService.findProcurementById(Long.valueOf(this.procurementId));
		if (!ObjectUtil.isEmpty((Object) this.procurement)) {
			for (ProcurementDetail procurementDetail : this.procurement.getProcurementDetails()) {
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
				obj.put((Object) "subTotal", (Object) procurementDetail.getSubTotal());
				obj.put((Object) "ratePerUnit", (Object) procurementDetail.getRatePerUnit());
				obj.put((Object) "bags", (Object) (!StringUtil.isEmpty((Object) procurementDetail.getNumberOfBags())
						? Long.valueOf(procurementDetail.getNumberOfBags()) : "0.00"));
				obj.put((Object) "fruitBags", (Object) (!StringUtil.isEmpty((Object) procurementDetail.getNumberOfFruitBags())
						? Long.valueOf(procurementDetail.getNumberOfFruitBags()) : "0.00"));
				/*obj.put((Object) "frutiBags", (Object) (!StringUtil.isEmpty((Object) procurementDetail.getNumberOfFruitBags())
						? Long.valueOf(procurementDetail.getNumberOfFruitBags()) : "0.00"));*/
				obj.put((Object)"batchNo",
				 (Object)(!StringUtil.isEmpty((Object)procurementDetail.getBatchNo())
				 ? procurementDetail.getBatchNo() : ""));
				obj.put((Object) "grossWeight",
						(Object) (!StringUtil.isEmpty((Object) procurementDetail.getGrossWeight())
								? Double.valueOf(procurementDetail.getGrossWeight()) : "0.00"));
				obj.put((Object) "dryLoss", (Object) (!StringUtil.isEmpty((Object) procurementDetail.getDryLoss())
						? Double.valueOf(procurementDetail.getDryLoss()) : "0.00"));
				obj.put((Object) "gradingLoss",
						(Object) (!StringUtil.isEmpty((Object) procurementDetail.getGradingLoss())
								? Double.valueOf(procurementDetail.getGradingLoss()) : "0.00"));
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
		if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
			procurementDetails.setNumberOfBags(Long.valueOf(details[0].trim()).longValue());
		}
		if(getCurrentTenantId().equalsIgnoreCase(ESESystem.AWI_TENANT_ID)){
			procurementDetails.setNumberOfFruitBags(String.valueOf(details[5].trim()));
		}
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
		procurementDetails.setBatchNo(details[6].trim());
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
		

		/*
		 * AgroTransaction txn
		 * =productDistributionDAO.findAgroTransactionByProcurementId(
		 * procurementDetails.getProcurement().getId(),procurementDetails.
		 * getProcurement().getAgroTransaction().getId()); AgroTransaction
		 * agrotxn
		 * =productDistributionDAO.findAgroTransactionByRecAndTxnDesc(txn.
		 * getReceiptNo(),Procurement.PROCURMEMENT);
		 * agrotxn.setTxnAmount(procurementDetails.getProcurement().
		 * getTotalProVal());
		 * agrotxn.setBalAmount(agrotxn.getIntBalance()+procurementDetails.
		 * getProcurement().getTotalProVal()); farmerService.update(agrotxn);
		 * 
		 * txn.setIntBalance(agrotxn.getBalAmount());
		 * txn.setBalAmount(agrotxn.getBalAmount() -
		 * procurementDetails.getProcurement().getPaymentAmount());
		 * txn.setCreditAmt(agrotxn.getBalAmount() -
		 * procurementDetails.getProcurement().getPaymentAmount());
		 * farmerService.update(txn); AgroTransaction txn1
		 * =productDistributionDAO.findAgroTransactionByProcurementId(
		 * procurementDetails.getProcurement().getId(),procurementDetails.
		 * getProcurement().getAgroTransaction().getId());
		 * 
		 * PaymentLedger ledger =
		 * productDistributionDAO.findPaymenyLedgerByProcurementId(String.
		 * valueOf(procurementDetails.getProcurement().getId()));
		 * ledger.setPrevValue(txn1.getBalAmount());
		 * ledger.setTxnValue(procurementDetails.getProcurement().
		 * getAgroTransaction().getTxnAmount());
		 * ledger.setNewValue(txn1.getBalAmount()-procurementDetails.
		 * getProcurement().getAgroTransaction().getTxnAmount());
		 * farmerService.update(ledger);
		 * 
		 * 
		 * ESEAccount farmerAccount = productDistributionDAO
		 * .findESEAccountByProfileId(procurementDetails.getProcurement().
		 * getAgroTransaction().getFarmerId(), ESEAccount.CONTRACT_ACCOUNT); if
		 * (!ObjectUtil.isEmpty(farmerAccount)) {
		 * 
		 * farmerAccount.setCashBalance( txn1.getBalAmount());
		 * farmerService.update(farmerAccount); }
		 */
	}

	public void populatePaymentAmt() {

		if (!StringUtil.isEmpty((String) this.id))
			this.procurement = this.productDistributionService.findProcurementById(Long.valueOf(this.id));

		if (!StringUtil.isEmpty(paymentAmount) && paymentAmount != null) {
			procurement.setPaymentAmount(Double.valueOf(paymentAmount));
		} else {
			procurement.setPaymentAmount(0D);
		}

		procurement.setVehicleType(getVehicleType());
		procurement.setTransportCost(getTransport_cost());
		if(getCurrentTenantId().equalsIgnoreCase(ESESystem.AWI_TENANT_ID)){
			procurement.setStatus(1);
			productDistributionService.approveProcurement(procurement);
			
		}else{
		productDistributionService.editProcurement(procurement);

		AgroTransaction txn = productDistributionDAO.findAgroTransactionByProcurementId(procurement.getId(),
				procurement.getAgroTransaction().getId());
		AgroTransaction agrotxn = productDistributionDAO.findAgroTransactionByRecAndTxnDesc(txn.getReceiptNo(),
				Procurement.PROCURMEMENT);
		agrotxn.setTxnAmount(procurement.getTotalProVal());
		agrotxn.setBalAmount(agrotxn.getIntBalance() + procurement.getTotalProVal());
		farmerService.update(agrotxn);

		txn.setIntBalance(agrotxn.getBalAmount());
		txn.setBalAmount(agrotxn.getBalAmount() - procurement.getPaymentAmount());
		txn.setCreditAmt(agrotxn.getBalAmount() - procurement.getPaymentAmount());
		farmerService.update(txn);
		AgroTransaction txn1 = productDistributionDAO.findAgroTransactionByProcurementId(procurement.getId(),
				procurement.getAgroTransaction().getId());

		PaymentLedger ledger = productDistributionDAO
				.findPaymenyLedgerByProcurementId(String.valueOf(procurement.getId()));
		ledger.setPrevValue(txn1.getBalAmount());
		ledger.setTxnValue(procurement.getAgroTransaction().getTxnAmount());
		ledger.setNewValue(txn1.getBalAmount() - procurement.getAgroTransaction().getTxnAmount());
		farmerService.update(ledger);

		ESEAccount farmerAccount = productDistributionDAO
				.findESEAccountByProfileId(procurement.getAgroTransaction().getFarmerId(), ESEAccount.CONTRACT_ACCOUNT);
		if (!ObjectUtil.isEmpty(farmerAccount)) {

			farmerAccount.setCashBalance(txn1.getBalAmount());
			farmerService.update(farmerAccount);
		}
		}

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

	private AgroTransaction processProcurement(Procurement proc,Warehouse warehouse) {
	
		AgroTransaction txn = new AgroTransaction();
		txn.setDeviceId(NOT_APPLICABLE);
		txn.setDeviceName(NOT_APPLICABLE);
		txn.setBranch_id(proc.getBranchId());
		if (!StringUtil.isEmpty(this.procurementDate)) {
			try {
				txn.setTxnTime(DateUtil.convertStringToDate(this.procurementDate, getGeneralDateFormat()));
				// txn.setTxnTime(DateUtil.setTimeToDate(txn.getTxnTime()));
			} catch (Exception e) {
				String date = DateUtil.convertDateToString(new Date(), getGeneralDateFormat());
				txn.setTxnTime(DateUtil.convertStringToDate(date, getGeneralDateFormat()));

				// txn.setTxnTime(DateUtil.setTimeToDate(txn.getTxnTime()));

			}
		}
		txn.setTxnType(PROCUREMENT_PRODUCT_ENROLLMENT);
		txn.setProfType(Profile.CLIENT);
		txn.setOperType(ESETxn.ON_LINE);
		txn.setTxnDesc(Procurement.PROCURMEMENT);

		Double bal;
		if (!ObjectUtil.isEmpty(proc.getFarmer()) && !StringUtil.isEmpty(proc.getFarmer().getFarmerId())) {
			ESEAccount farmerAccount = productDistributionDAO.findESEAccountByProfileId(proc.getFarmer().getFarmerId(),
					ESEAccount.CONTRACT_ACCOUNT);
			if (farmerAccount != null && !ObjectUtil.isEmpty(farmerAccount)) {
				bal = farmerAccount.getCashBalance();
				txn.setIntBalance(bal);
				/*if(!StringUtil.isEmpty(getEnableLoanModule()) && getEnableLoanModule().equalsIgnoreCase("1")){
					txn.setTxnAmount(StringUtil.isDouble(getSelectedFinalAmt()) ? Double.valueOf(getSelectedFinalAmt()) : 0D);
					txn.setBalAmount(bal + Double.valueOf(getSelectedFinalAmt()));
				}else{*/
				txn.setTxnAmount(StringUtil.isDouble(getTotalAmount()) ? Double.valueOf(getTotalAmount()) : 0D);
				txn.setBalAmount(bal + proc.getTotalProVal());
				/*}*/
				txn.setAccount(farmerAccount);
				txn.setFarmerId(proc.getFarmer().getFarmerId());
				txn.setFarmerName(!StringUtil.isEmpty(proc.getFarmer().getLastName()) ? proc.getFarmer().getFirstName() + " " + proc.getFarmer().getLastName() :proc.getFarmer().getFirstName() );
			} else {
				txn.setIntBalance(0.00);
				/*if(!StringUtil.isEmpty(getEnableLoanModule()) && getEnableLoanModule().equalsIgnoreCase("1")){
					txn.setTxnAmount(StringUtil.isDouble(getSelectedFinalAmt()) ? Double.valueOf(getSelectedFinalAmt()) : 0D);
				}else{*/
					txn.setTxnAmount(StringUtil.isDouble(getTotalAmount()) ? Double.valueOf(getTotalAmount()) : 0D);
			/*	}*/
				
				txn.setBalAmount(0.00);
				txn.setFarmerId(NOT_APPLICABLE);
				txn.setFarmerName(getFarmerName());
			}
		} else {
			txn.setIntBalance(0.00);
			if(!StringUtil.isEmpty(getEnableLoanModule()) && getEnableLoanModule().equalsIgnoreCase("1")){
				txn.setTxnAmount(StringUtil.isDouble(getSelectedFinalAmt()) ? Double.valueOf(getSelectedFinalAmt()) : 0D);
			}else{
				txn.setTxnAmount(StringUtil.isDouble(getTotalAmount()) ? Double.valueOf(getTotalAmount()) : 0D);
			}
			txn.setBalAmount(0.00);
			txn.setFarmerId(NOT_APPLICABLE);
			txn.setFarmerName(getFarmerName());
			// txn.setAccount(farmerAccount);
		}

		//Warehouse warehouse = locationService.findWarehouseById(Long.valueOf(selectedWarehouse));
		txn.setWarehouse(warehouse);
		txn.setSamithi(warehouse);

		if (!StringUtil.isEmpty(selectedMobileUser)) {
			Agent agent = agentService.findAgent(Long.valueOf(selectedMobileUser));
			txn.setAgentId(agent.getProfileId());
			txn.setAgentName(agent.getPersonalInfo().getFirstName());
		}

		/*Farmer farmer = null;
		if (!StringUtil.isEmpty(selectedFarmer)) {
			farmer = farmerService.findFarmerById(Long.valueOf(selectedFarmer));
		}

		if (!ObjectUtil.isEmpty(farmer)) {
			txn.setFarmerId(farmer.getFarmerId());
			txn.setFarmerName(!StringUtil.isEmpty(farmer.getLastName()) ? farmer.getFirstName() + " " + farmer.getLastName() :farmer.getFirstName() );
		} else {
			txn.setFarmerId(NOT_APPLICABLE);
			txn.setFarmerName(getFarmerName());
		}*/
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
			farmerService.findFarmerByGroup(Long.valueOf(selectedSupplier)).stream()
					.filter(farmer -> (!ObjectUtil.isEmpty(farmer))).forEach(farmer -> {
						farmerArr
								.add(getJSONObject(farmer[0], (!StringUtil.isEmpty(farmer[3]) ? (" " + farmer[3]) : " ")
										+ (!StringUtil.isEmpty(farmer[1]) ? ("-" + farmer[1]) : " ")));
					});
		}
		sendAjaxResponse(farmerArr);
	}

	public String delete() {
		if (!StringUtil.isEmpty(id)) {
			procurement = productDistributionService.findProcurementById(Long.valueOf(id));
			productService.removeProcurment(id, Procurement.DELETE_STATUS);
			AgroTransaction agrotxn = new AgroTransaction();
			agrotxn.setTxnDesc(Procurement.PROCURMEMENT_DELETE);
			agrotxn.setTxnType(procurement.getAgroTransaction().getTxnType() + "D");
			agrotxn.setFarmerId(procurement.getFarmer().getFarmerId());
			agrotxn.setFarmerName(procurement.getFarmer().getFirstName());
			agrotxn.setServicePointId(procurement.getWarehouseId());
			agrotxn.setServicePointName(procurement.getWarehouseName());
			farmerService.addAgroTxn(agrotxn);
		}

		return LIST;

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

	public Map<Long, String> getBuyersList() {
		Map<Long, String> listOfBuyers = new HashMap<Long, String>();
		List<Customer> customers = clientService.listOfCustomers();
		for (Customer customer : customers) {
			listOfBuyers.put(customer.getId(), customer.getCustomerName());
		}
		return listOfBuyers;
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

	public String getSelectedFarmerValue() {
		return selectedFarmerValue;
	}

	public void setSelectedFarmerValue(String selectedFarmerValue) {
		this.selectedFarmerValue = selectedFarmerValue;
	}

	public ESEAccount getAccount() {
		return account;
	}

	public void setAccount(ESEAccount account) {
		this.account = account;
	}

	public String getCashCreditValue() {
		return cashCreditValue;
	}

	public void setCashCreditValue(String cashCreditValue) {
		this.cashCreditValue = cashCreditValue;
	}

	public String getEnableBuyer() {
		return enableBuyer;
	}

	public void setEnableBuyer(String enableBuyer) {
		this.enableBuyer = enableBuyer;
	}

	public Double getTransport_cost() {
		return transport_cost;
	}

	public void setTransport_cost(Double transport_cost) {
		this.transport_cost = transport_cost;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public Map<String, String> getVehicleList() {
		Map<String, String> waterSourceList = new LinkedHashMap<>();
		waterSourceList = getFarmCatalougeMap(Integer.valueOf("124"));
		return waterSourceList;
	}

	public String getNoOfCrates() {
		return noOfCrates;
	}

	public void setNoOfCrates(String noOfCrates) {
		this.noOfCrates = noOfCrates;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public Procurement getFilter() {
		return filter;
	}

	public void setFilter(Procurement filter) {
		this.filter = filter;
	}

	public String getEnableLoanModule() {
		return enableLoanModule;
	}

	public void setEnableLoanModule(String enableLoanModule) {
		this.enableLoanModule = enableLoanModule;
	}
	public void populateLoanInt() throws Exception {
		String loanIntPer ="";
		if (!StringUtil.isEmpty(selectedFarmerValue)) {
			Farmer farmer=farmerService.findFarmerById(Long.valueOf(selectedFarmerValue));
			account = accountService.findAccountByProfileIdAndProfileType(farmer.getFarmerId(),
				ESEAccount.CONTRACT_ACCOUNT);
		if (!ObjectUtil.isEmpty(account) && account.getLoanAmount()>0.00 && account.getOutstandingLoanAmount() >0.00) {
			if (!StringUtil.isEmpty(actualAmt)) {
				LoanInterest loanVal = farmerService.findLoanPercent((long)actualAmt);
				loanIntPer = ObjectUtil.isEmpty(loanVal) ? "0.0" : loanVal.getInterest()+ "," + account.getOutstandingLoanAmount();
			}
		}
		}
		
		response.getWriter().print(loanIntPer);
	}

	public double getActualAmt() {
		return actualAmt;
	}

	public void setActualAmt(double actualAmt) {
		this.actualAmt = actualAmt;
	}

	public String getSelectedLoanInt() {
		return selectedLoanInt;
	}

	public void setSelectedLoanInt(String selectedLoanInt) {
		this.selectedLoanInt = selectedLoanInt;
	}

	public String getSelectedFinalAmt() {
		return selectedFinalAmt;
	}

	public void setSelectedFinalAmt(String selectedFinalAmt) {
		this.selectedFinalAmt = selectedFinalAmt;
	}

	public double getLoanAmt() {
		return loanAmt;
	}

	public void setLoanAmt(double loanAmt) {
		this.loanAmt = loanAmt;
	}

	public double getRepaymentAmt() {
		return repaymentAmt;
	}

	public void setRepaymentAmt(double repaymentAmt) {
		this.repaymentAmt = repaymentAmt;
	}
	
}
