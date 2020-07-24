/*
 * DistributionAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.velocity.anakia.Escape;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.profile.CropSupply;
import com.ese.entity.util.ESESystem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sourcetrace.eses.dao.IFarmerDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.AgentType;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.entity.WarehouseProduct;
import com.sourcetrace.eses.entity.WarehouseProductDetail;
import com.sourcetrace.eses.order.entity.profile.Category;
import com.sourcetrace.eses.order.entity.profile.SubCategory;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.CropSupplyImages;
import com.sourcetrace.eses.order.entity.txn.Distribution;
import com.sourcetrace.eses.order.entity.txn.DistributionDetail;
import com.sourcetrace.eses.order.entity.txn.PMTImageDetails;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.ICategoryService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.entity.Image;
import com.sourcetrace.eses.util.entity.ImageInfo;
import com.sourcetrace.eses.util.profile.Product;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.view.WebTransactionAction;

// TODO: Auto-generated Javadoc
/**
 * The Class DistributionAction.
 */
public class DistributionAction extends WebTransactionAction {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7392928098354296987L;

	/** The product distribution service. */
	private IProductDistributionService productDistributionService;

	/** The farmer service. */
	private IFarmerService farmerService;

	/** The location service. */
	private ILocationService locationService;

	/** The category service. */
	private ICategoryService categoryService;

	/** The product service. */
	private IProductService productService;

	/** The account service. */
	private IAccountService accountService;

	/** The id generator. */
	private IUniqueIDGenerator idGenerator;

	/** The agent. */
	private Agent agent;

	/** The farmer. */
	private Farmer farmer;

	/** The product. */
	private Product product;

	/** The distribution. */
	private Distribution distribution;

	/** The distributiondetail. */
	private DistributionDetail distributiondetail;

	/** The id. */
	private String id;

	/** The selected warehouse. */
	private String selectedWarehouse;

	/** The selected village. */
	private String selectedVillage;

	/** The selected farmer. */
	private String selectedFarmer;

	/** The farmer name. */
	private String farmerName = "";

	/** The farmer id. */
	private String farmerId = "";

	/** The farmer address. */
	private String farmerAddress = "";

	/** The selected category. */
	private String selectedCategory;

	/** The selected sub category. */
	private String selectedSubCategory;

	/** The selected product. */
	private String selectedProduct;

	/** The selected season. */
	private String selectedSeason;

	/** The selected unit. */
	private String selectedUnit;

	/** The stock. */
	private double stock;

	/** The distribution product. */
	private String distributionProduct;

	/** The product price. */
	private String productPrice;

	/** The units. */
	private String units;

	/** The distribution product list. */
	private String distributionProductList;

	/** The start date. */
	private String startDate;

	/** The receipt number. */
	private String receiptNumber;

	/** The distribution description. */
	private String distributionDescription;

	/** The product total string. */
	private String productTotalString;

	/** The reference no. */
	private String referenceNo;

	/** The serial no. */
	private String serialNo;

	/** The id. */
	private String fId;

	/** The payment rupee. */
	private String paymentRupee;

	/** The payment paise. */
	private String paymentPaise;

	/** The un register farmer name. */
	private String unRegisterFarmerName;

	/** The mobile no. */
	private String mobileNo;

	/** The selected agent. */
	private String selectedAgent;

	/** The registered farmer. */
	private boolean registeredFarmer;

	/** The warehouse list. */
	private List<String> warehouseList = new ArrayList<String>();

	/** The village list. */
	private List<String> villageList = new ArrayList<String>();

	/** The farmers list. */
	private Map<String, String> farmersList = new LinkedHashMap<>();

	/** The category list. */
	private List<String> categoryList = new ArrayList<String>();

	/** The sub categorys list. */
	private List<String> subCategorysList = new ArrayList<String>();

	/** The products name unit map. */
	private Map<String, String> productsNameUnitMap = new LinkedHashMap<String, String>();

	/** The distribution map. */
	private Map<String, Object> distributionMap = new LinkedHashMap<String, Object>();

	/** The coopearative list. */
	Map<String, String> coopearativeList = new LinkedHashMap<String, String>();

	/** The agent lists. */
	Map<String, String> agentLists = new LinkedHashMap<String, String>();

	/** The selected cooperative. */
	private String selectedCooperative;

	/** The tax value. */
	private double taxValue;

	/** The final amt value. */
	private double finalAmtValue;

	/** The cash type. */
	Map<Integer, String> cashType = new LinkedHashMap<Integer, String>();

	/** The payment mode. */
	private String paymentMode;

	/** The payment amount. */
	private double paymentAmount;

	/** The check boxs. */
	private Map<Character, String> checkBoxs = new LinkedHashMap<Character, String>();
	private Map<Long, String> batchNoList = new LinkedHashMap<Long, String>();

	private String productDistributionTypes;
	private List<DistributionDetail> distributionDetailList;
	private String quantiy;
	private String distributionDetails;
	/** The selected farmer value. */
	private String selectedFarmerValue;
	private String enableBatchNo;
	private String isFreeDistribution;
	private String productAvailableUnit;
	/** The farmer dao. */
	@Autowired
	private IFarmerDAO farmerDAO;

	/** The cash credit value. */
	private String cashCreditValue;
	// private boolean stockType;
	/** The product stock. */
	private boolean productStock;

	/** The is free distribution. */
	private String freeDistribution;

	private String seasonName;

	private String selectedCity;

	private String harvestSeasonEnabled;
	private String batchNo;
	// private List<String> agentLists = new ArrayList<String>();
	Map<String, String> harvestSeasonList = new LinkedHashMap<String, String>();

	/**
	 * Creates the.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */

	private String identityForGrid;
	private String type;
	private String farmerType;
	private String qty;
	private String cash;
	private String credit;
	private String seasonCodeAndName;
	private double taxPercent;
	private double finalAmount;
	private double payemnt;
	DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
	// private String approved;
	// private List<String> agentLists = new ArrayList<String>();
	private String seasonCode;
	private String distTxnType;
	private String selectedDate;
	private String updatedDate;
	private String changeStockUpdate = "0";

	// SagiSeed's Distribution to Farmer
	private Map<Long, String> listWarehouse = new LinkedHashMap<Long, String>();
	private Map<String, String> listSeason = new LinkedHashMap<String, String>();
	private List<String> listReceiptNo = new ArrayList<String>();
	private List<String> listProduct = new ArrayList<String>();
	private Map<String, String> listVillage = new LinkedHashMap<String, String>();
	private List<String> listFarmer = new ArrayList<String>();
	private String warehouseId;
	private String productId;

	private String receiptNum;
	private String lotNumber;
	private String villageId;
	private String selectedWarehouseId;
	private String selectedReceiptNo;
	private String recDate;
	private String distImgAvil;
	private File distImg1;
	private File distImg2;
	private String distId;
	private String distributionimage;
	private OutputStream imageStream;
	private String imageArr;
	private String stockTypeText;
	Map<String,String> warehouseMap=new HashMap<>();
	/**
	 * Creates the.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String list() {
		type = Distribution.FARMER;
		return LIST;
	}

	public String seedList() {
		return "seedList";
	}

	public String data() throws Exception {
		List<Warehouse> warehouseList=productDistributionService.listWarehouse();
		warehouseMap = warehouseList.stream().collect(
                Collectors.toMap(Warehouse::getCode, Warehouse::getName));
		Map<String, String> searchRecord = getJQGridRequestParam(); // get the
		Distribution filter = new Distribution();

		filter.setTxnType(Distribution.PRODUCT_DISTRIBUTION_TO_FARMER);
		if (getIdentityForGrid() == null)
			setIdentityForGrid("distribution");

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

		if (!StringUtil.isEmpty(searchRecord.get("subBranchId"))) {
			filter.setBranchId(searchRecord.get("subBranchId").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("farmerType"))) {
			filter.setFarmerType(searchRecord.get("farmerType").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("warehouseName"))) {
			filter.setServicePointName(searchRecord.get("warehouseName").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("agentName"))) {
			filter.setAgentName(searchRecord.get("agentName").trim());
		}
		
		if (!StringUtil.isEmpty(searchRecord.get("lastName"))) {
			
			filter.setFatherName(searchRecord.get("lastName").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("farmer"))) {
			filter.setFarmerName(searchRecord.get("farmer").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("village"))) {
			Village village = new Village();
			village.setName(searchRecord.get("village").trim());
			filter.setVillage(village);
		}

		/*
		 * if (!StringUtil.isEmpty(searchRecord.get("stockType"))) {
		 * filter.setStockType(searchRecord.get("stockType")); }
		 */

		if (!StringUtil.isEmpty(searchRecord.get("stockType"))) {
			if ("1".equals(searchRecord.get("stockType"))) {
				filter.setStockType("1");
				// filter.setStockType(Distribution.FARMER);
			} else if("2".equals(searchRecord.get("stockType"))) {
				filter.setStockType("2");
				// filter.setStockType(Distribution.AGENT);
			}
		}

		if (!StringUtil.isEmpty(searchRecord.get("seasonCode"))) {
			filter.setSeasonCode(searchRecord.get("seasonCode").trim());
		}
		String approved = preferncesService.findPrefernceByName(ESESystem.ENABLE_APPROVED);
		if (approved.equalsIgnoreCase("1")) {
			filter.setEnableApproved("1");
		} else {
			filter.setEnableApproved("0");
		}

		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		return sendJQGridJSONResponse(data);
	}

	/**
	 * To json.
	 * 
	 * @param obj
	 *            the obj
	 * @return the JSON object
	 * @see com.sourcetrace.esesw.view.SwitchAction#toJSON(java.lang.Object)
	 */
	public JSONObject toJSON(Object obj) {
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();

		if (identityForGrid == "distribution") {
			if (obj instanceof Distribution) {
				Distribution distribution = (Distribution) obj;
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

				/*
				 * rows.add(!ObjectUtil.isEmpty(distribution) ?
				 * (!StringUtil.isEmpty(dateFormat.format(distribution.
				 * getTxnTime())) ? dateFormat.format(distribution.getTxnTime())
				 * : "") : "");
				 */

				// end

				if (!StringUtil.isEmpty(distribution.getServicePointName())) {
					rows.add(getLocaleProperty("warehouseStock"));
					rows.add(warehouseMap.get(distribution.getServicePointId()));
					//rows.add(distribution.getServicePointName());
					rows.add("NA");

				} else {
					// rows.add("MobileUserStock");
					rows.add(getLocaleProperty("fieldstaff.stock"));
					if(!getCurrentTenantId().equals(ESESystem.PRATIBHA_TENANT_ID))
						rows.add("NA");
					else
						//rows.add(!StringUtil.isEmpty(distribution.getWarehouseName())?distribution.getWarehouseName():"");
						rows.add(warehouseMap.get(distribution.getWarehouseCode()));
					rows.add(distribution.getAgentName());

				}
				/*
				 * Farmer farmer =
				 * farmerService.findFarmerByFarmerId(distribution.getFarmerId()
				 * ); if (!ObjectUtil.isEmpty(farmer)) { rows.add("Registered");
				 * } else { rows.add("Unregistered"); }
				 */

				if (distribution.getSamithiName() != null) {
					rows.add(distribution.getFarmerName());
					
				/*	  if (!StringUtil.isEmpty(farmer.getLastName()) ||
					  farmer.getLastName() != null) {
					  rows.add(farmer.getLastName()); } else { rows.add(""); }*/
					 
					if (!ObjectUtil.isEmpty(distribution.getVillage())) {
						rows.add(distribution.getVillage().getName());
						// rows.add(distribution.getVillage().getCode() + "-" +
						// distribution.getVillage().getName());

					} else {
						rows.add("");
					}
					/* rows.add("NA"); */
				} else {
					rows.add(distribution.getFarmerName());
					if (getCurrentTenantId().equalsIgnoreCase("pratibha")){
					rows.add(distribution.getFatherName());
					}
					/* rows.add(""); */
					// rows.add(distribution.getVillage().getCode() + "-" +
					// distribution.getVillage().getName());
					rows.add(distribution.getVillage().getName());
					/* rows.add(distribution.getVillage().getVillageName()); */
					/* rows.add(distribution.getMobileNumber()); */
				}

				/*
				 * rows.add(getText("productDistributionType" +
				 * distribution.getFreeDistribution())); if
				 * (!ObjectUtil.isEmpty(distribution.getDistributionDetails()))
				 * { Double qty = 0.0; { for (DistributionDetail
				 * distributionDetail : distribution.getDistributionDetails()) {
				 * qty = qty + distributionDetail.getQuantity(); }
				 * rows.add(CurrencyUtil.getDecimalFormat((qty), "##.0000")); }
				 * }
				 */

				/*
				 * if (distribution.getDistributionDetails().iterator().next().
				 * getSellingPrice() == 0.00) {
				 * 
				 * rows.add("NA"); rows.add("NA"); rows.add("NA");
				 * rows.add("NA"); rows.add("NA"); } else {
				 * rows.add(CurrencyUtil.getDecimalFormat(distribution.
				 * getTotalAmount(), "##.000"));
				 * rows.add(CurrencyUtil.getDecimalFormat(distribution.getTax(),
				 * "##.000"));
				 * rows.add(CurrencyUtil.getDecimalFormat(distribution.
				 * getFinalAmount(), "##.000"));
				 * 
				 * if ("CS".equals(distribution.getPaymentMode())) {
				 * 
				 * rows.add(CurrencyUtil.getDecimalFormat(distribution.
				 * getPaymentAmount(), "##.000"));
				 * rows.add(CurrencyUtil.getDecimalFormat(
				 * distribution.getFinalAmount() -
				 * distribution.getPaymentAmount(), "##.000"));
				 * 
				 * } else if ("CR".equals(distribution.getPaymentMode())) {
				 * rows.add("NA");
				 * rows.add(CurrencyUtil.getDecimalFormat(distribution.
				 * getFinalAmount(), "##.000")); } else { rows.add("NA");
				 * rows.add("NA"); } }
				 */
				HarvestSeason season = farmerService.findSeasonNameByCode(distribution.getSeasonCode());
				rows.add(!ObjectUtil.isEmpty(season) ? season.getName() : "");

				jsonObject.put("id", distribution.getId());
				jsonObject.put("cell", rows);
			}

		} else {
			if (obj instanceof DistributionDetail) {
				DecimalFormat df = new DecimalFormat("0.0000");
				DistributionDetail distributionDetail = (DistributionDetail) obj;
				rows.add(distributionDetail.getProduct().getSubcategory() == null ? ""
						: distributionDetail.getProduct().getSubcategory().getName());
				rows.add(distributionDetail.getProduct() == null ? "" : distributionDetail.getProduct().getName());
				// rows.add(distributionDetail.getExistingQuantity());
				rows.add(distributionDetail.getExistingQuantity());
				rows.add(CurrencyUtil.getDecimalFormat(distributionDetail.getCostPrice(), "##.000"));
				if (distributionDetail.getSellingPrice() == 0.00) {
					rows.add("NA");
					rows.add("NA");
				} else {
					rows.add(CurrencyUtil.getDecimalFormat(distributionDetail.getSellingPrice(), "##.000"));
					rows.add(CurrencyUtil.getDecimalFormat(Double.valueOf(distributionDetail.getSellingPrice())
							* Double.valueOf(distributionDetail.getQuantity()), "##.0000"));
				}

				rows.add(CurrencyUtil.getDecimalFormat(distributionDetail.getQuantity(), "##.0000"));
				Double diffQty = 0.00;
				if (!StringUtil.isEmpty(distributionDetail.getExistingQuantity())) {
					diffQty = Double.valueOf(distributionDetail.getExistingQuantity())
							- (distributionDetail.getQuantity());
				}
				rows.add(CurrencyUtil.getDecimalFormat(Double.valueOf(distributionDetail.getCurrentQuantity()),
						"##.0000"));
				jsonObject.put("id", distributionDetail.getId());
				jsonObject.put("cell", rows);
			}
		}
		return jsonObject;
	}

	public String create() throws Exception {

		if (id == null) {
			command = CREATE;
			if (!StringUtil.isEmpty(agentId)) {
				request.setAttribute("agentId", agentId);
				agent = agentService.findAgentByProfileId(agentId);
				if (!ObjectUtil.isEmpty(agent) && ESETxn.WEB_BOD == agent.getBodStatus()) {
					Warehouse cooperative = agent.getCooperative();
					if (!ObjectUtil.isEmpty(cooperative))
						setSelectedWarehouse(cooperative.getName() + " - " + cooperative.getCode());
					Calendar currentDate = Calendar.getInstance();
					DateFormat df = new SimpleDateFormat(getGeneralDateFormat());
					startDate = df.format(currentDate.getTime());
					request.setAttribute(HEADING, getText("distcreate.page"));
					setReceiptNumber(null);
					setDistributionDescription(null);
					setCurrentSeason();
					loadWarehouseAndProductData();
					if (!StringUtil.isEmpty(fId) && !fId.equalsIgnoreCase(null)) {
						setFarmerAndVillageData();
					}
					ESESystem preferences = preferncesService.findPrefernceById("1");
					setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));
					return INPUT;
				}
			}
			ESESystem preferences = preferncesService.findPrefernceById("1");
			setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));
			return INPUT;
		} else {

			/*
			 * agentId = (String) request.getAttribute("agentId"); Agent agent =
			 * agentService.findAgentByProfileId(agentId); if
			 * (ObjectUtil.isEmpty(agent) || ESETxn.EOD == agent.getBodStatus()
			 * || Agent.ACTIVE != agent.getStatus()) { return REDIRECT; }
			 */
			// Added for Handling Form ReSubmit - Please See at
			// populateFarmerAccount() Method
			if (ObjectUtil.isEmpty(request.getSession().getAttribute(agentId + "_"
					+ Distribution.PRODUCT_DISTRIBUTION_TO_FARMER + "_" + WebTransactionAction.IS_FORM_RESUBMIT))) {
				setAgent(agent);
				setCurrentSeason();
				return INPUT;
			}

			AgroTransaction agroTransaction = new AgroTransaction();

			// FORMING DISTRIBUTION OBJECT
			if (StringUtil.isEmpty(receiptNumber))
				distribution = new Distribution();
			if (registeredFarmer) {
				Farmer farmer = farmerService.findFarmerByFarmerId(farmerId);
				if (!ObjectUtil.isEmpty(farmer) && !ObjectUtil.isEmpty(farmer.getVillage())) {
					Village village = locationService.findVillageById(farmer.getVillage().getId());
					if (!ObjectUtil.isEmpty(village))
						distribution.setVillage(village);
				}
				agroTransaction.setFarmerId(farmerId);
				agroTransaction.setFarmerName(farmerName);
			} else {
				if (!StringUtil.isEmpty(selectedVillage)) {
					Village village = locationService.findVillageByCode(selectedVillage.split("-")[1].trim());
					distribution.setVillage(village);
				}
				distribution.setMobileNumber(mobileNo);
				agroTransaction.setFarmerId(NOT_APPLICABLE);
				agroTransaction.setFarmerName(unRegisterFarmerName);
			}

			// CREATING RECEIPT NUMBER
			String receiptnumberSeq = idGenerator.getDistributionSeq();
			// FORMING AGRO TRANSACTION OBJECT
			receiptNumber = receiptnumberSeq;
			agroTransaction.setReceiptNo(receiptnumberSeq);
			// agroTransaction.setTxnTime(DateUtil.convertStringToDate(startDate,
			// "MM/dd/yyyy"));
			agroTransaction.setTxnTime(DateUtil.convertStringToDate(startDate, getGeneralDateFormat()));
			agroTransaction.setTxnTime(DateUtil.setTimeToDate(agroTransaction.getTxnTime()));
			if (registeredFarmer && !StringUtil.isEmpty(selectedWarehouse) && selectedWarehouse.contains("-")) {
				Warehouse samithi = locationService.findWarehouseByCode(selectedWarehouse.trim().split("-")[1].trim());
				agroTransaction.setSamithi(samithi);
			}
			if (!StringUtil.isEmpty(selectedAgent)) {
				Agent agent = agentService.findAgent(Long.valueOf(selectedAgent));
				// String[] agentArr = selectedAgent.split("-");
				ESEAccount fieldStaffaccount = accountService.findAccountByProfileIdAndProfileType(agent.getProfileId(),
						ESEAccount.AGENT_ACCOUNT);
				if (ObjectUtil.isEmpty(fieldStaffaccount)) {
					return REDIRECT;
				}

				agroTransaction.setAgentId(agent.getProfileId());
				// agent =
				// agentService.findAgentByProfileId(agentArr[1].trim());
				agroTransaction.setAgentName(agent.getPersonalInfo().getAgentName());
				// Warehouse warehouse =
				// locationDAO.findCoOperativeByCode(distribution.getAgroTransaction().getServicePointId());
			}
			/*
			 * ESEAccount fieldStaffaccount =
			 * accountService.findAccountByProfileIdAndProfileType(
			 * selectedAgent, ESEAccount.AGENT_ACCOUNT); if
			 * (!agent.isCoOperativeManager() &&
			 * ObjectUtil.isEmpty(fieldStaffaccount)) { return REDIRECT; }
			 * agroTransaction.setAgentId(selectedAgent.split("-")[1].trim());
			 * agent =
			 * agentService.findAgentByProfileId(selectedAgent.split("-")[1].
			 * trim());
			 * agroTransaction.setAgentName(agent.getPersonalInfo().getAgentName
			 * ());
			 */

			if (!StringUtil.isEmpty(selectedCooperative)) {
				String[] cooperativeAry = selectedCooperative.split("#");
				Warehouse warehouse = locationService.findCoOperativeByCode(cooperativeAry[1].trim());
				if (!ObjectUtil.isEmpty(warehouse)) {
					agroTransaction.setServicePointId(warehouse.getCode());
					agroTransaction.setServicePointName(warehouse.getName());
				}
			}
			// Warehouse warehouse = agent.getCooperative();

			agroTransaction.setDeviceId(NOT_APPLICABLE);
			agroTransaction.setDeviceName(NOT_APPLICABLE);
			agroTransaction.setOperType(ESETxn.ON_LINE);
			/*
			 * agroTransaction .setProfType(agent.isCoOperativeManager() ?
			 * Profile.CO_OPEARATIVE_MANAGER : Profile.AGENT);
			 */
			agroTransaction.setProfType(Profile.CLIENT);
			agroTransaction.setTxnType(Distribution.PRODUCT_DISTRIBUTION_TO_FARMER);
			agroTransaction.setTxnDesc(Distribution.PRODUCT_DISTRIBUTION_TO_FARMER_DESCRIPTION);
			agroTransaction.setDistribution(distribution);

			if (productStock) {
				agroTransaction.setProductStock(WarehouseProduct.StockType.WAREHOUSE_STOCK.name());
				agroTransaction.setWarehouseCode(selectedCooperative.split("#")[1].trim());
				distribution.setProductStock(agroTransaction.getProductStock());
			} else {
				agroTransaction.setProductStock(WarehouseProduct.StockType.AGENT_STOCK.name());
				distribution.setProductStock(agroTransaction.getProductStock());

			}

			// PROCT DETAILS FOR DISTRIBUTION_DETAIL
			Set<DistributionDetail> distributionDetailsSet = new HashSet<DistributionDetail>();
			double totalAmount = 0;
			DistributionDetail distributionDetail = new DistributionDetail();
			if (distributionProductList != null && distributionProductList.length() > 0) {
				String[] products = distributionProductList.split(",");
				for (int i = 0; i < products.length; i++) {
					String[] product = products[i].split("\\|");
					distributionDetail = new DistributionDetail();
					distributionDetail.setDistribution(distribution);
					Product productDetails = productService.findProductByCode(product[0].trim());
					distributionDetail.setProduct(productDetails);
					try {
						distributionDetail.setQuantity(Double.valueOf(product[1]));
						distributionDetail.setUnit(product[2].trim());
						distributionDetail.setPricePerUnit(Double.valueOf(product[3]));
						distributionDetail.setSubTotal(Double.valueOf(product[4]));
						totalAmount = totalAmount + distributionDetail.getSubTotal();
					} catch (Exception e) {
						e.printStackTrace();
					}
					distributionDetailsSet.add(distributionDetail);

				}
			}

			/*
			 * if (!ObjectUtil.isEmpty(fieldStaffaccount)) { //
			 * agroTransaction.setIntBalance(fieldStaffaccount.
			 * getDistributionBalance());
			 * agroTransaction.setTxnAmount(totalAmount); //
			 * agroTransaction.setBalAmount(agroTransaction.getIntBalance() // +
			 * agroTransaction.getTxnAmount()); //
			 * fieldStaffaccount.setDistributionBalance(agroTransaction.
			 * getBalAmount()); //
			 * agroTransaction.setAccount(fieldStaffaccount); }
			 */

			// Season season =
			// farmerService.findSeasonById(Long.valueOf(selectedSeason));

			distribution.setDistributionDetails(distributionDetailsSet);

			distribution.setAgroTransaction(agroTransaction);
			// distribution.setSeason(season);

			distribution.setPaymentAmount(getPaymentAmount());
			
			productDistributionService.saveDistributionAndDistributionDetail(distribution);
			String receiptHtml = "<br/><a href=\"javascript:printReceipt(\\'" + receiptnumberSeq + "\\')\" >"
					+ getText("printReceipt") + "</a>";
			setDistributionDescription(getText("receiptNumber") + " : " + receiptnumberSeq + " "
					+ getText("distributionSucess") + receiptHtml);
			paymentRupee = null;
			paymentPaise = null;
			loadWarehouseAndProductData();
			// reset();
			return INPUT;
		}

	}

	@SuppressWarnings("unused")
	public String detail() {

		String view = "";
		if (id != null && !id.equals("")) {
			distribution = productDistributionService.findDistributionById(Long.parseLong(id));
			if (StringUtil.isEmpty(distribution.getServicePointName())) {
				distribution.setServicePointName("agentStock");
			}
			HarvestSeason season = farmerService.findSeasonNameByCode(distribution.getSeasonCode());
			setSeasonCodeAndName(!ObjectUtil.isEmpty(season) ? season.getName() : "");
			Farmer farmer = farmerService.findFarmerByFarmerId(distribution.getFarmerId());
			distribution.setFreeDistribution(getText("distributionType" + distribution.getFreeDistribution()));
			distribution.setTotalAmount(
					Double.valueOf(CurrencyUtil.getDecimalFormat(distribution.getTotalAmount(), "##.00")));
			distribution.setFinalAmount(
					Double.valueOf(CurrencyUtil.getDecimalFormat(distribution.getFinalAmount(), "##.00")));
			distribution.setTxnAmount(Double.valueOf(distribution.getTxnAmount()));
			distribution.setPaymentAmount(
					Double.valueOf(CurrencyUtil.getDecimalFormat(distribution.getPaymentAmount(), "##.00")));
			
			if (!ObjectUtil.isEmpty(distribution.getDistributionDetails())) {
				Double qty = 0.0;
				{
					for (DistributionDetail distributionDetail : distribution.getDistributionDetails()) {
						qty = qty + distributionDetail.getQuantity();
					}
					setQuantiy(CurrencyUtil.getDecimalFormat((qty), "##.0000"));
				}
			}
			ESESystem preferences = preferncesService.findPrefernceById("1");
			DateFormat genDate = new SimpleDateFormat(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
			// DateFormat genDateTime = new
			// SimpleDateFormat(DateUtil.DATE_TIME);
			if (!ObjectUtil.isEmpty(preferences)) {
				distribution.setTransactionTime(!ObjectUtil.isEmpty(distribution)
						? (!ObjectUtil.isEmpty(genDate.format(distribution.getTxnTime()))
								? DateUtil.convertDateToString(distribution.getTxnTime(),
										getGeneralDateFormat().concat(" HH:mm:ss"))
								: "")
						: "");
				updatedDate = (!ObjectUtil.isEmpty(distribution)
						? (!ObjectUtil.isEmpty(distribution.getUpdateTime()) ? DateUtil.convertDateToString(
								distribution.getUpdateTime(), getGeneralDateFormat().concat(" HH:mm:ss")) : "")
						: "");
			}

			if (!ObjectUtil.isEmpty(farmer)) {
				setFarmerType("Registered");
			} else {
				setFarmerType("Unregistered");
			}
			if (distribution.getDistributionDetails().iterator().next().getSellingPrice() == 0.00) {

			} else {
				distribution.setTotalAmount(
						Double.valueOf((CurrencyUtil.getDecimalFormat(distribution.getTotalAmount(), "##.00"))));
				distribution.setTax(Double.valueOf(CurrencyUtil.getDecimalFormat(distribution.getTax(), "##.00")));
				distribution.setFinalAmount(
						Double.valueOf(CurrencyUtil.getDecimalFormat(distribution.getFinalAmount(), "##.00")));
				distribution.setPaymentAmount(
						Double.valueOf(CurrencyUtil.getDecimalFormat(distribution.getPaymentAmount(), "##.00")));

				/*
				 * if ("CS".equals(distribution.getPaymentMode())) {
				 * 
				 * 
				 * setCredit((CurrencyUtil.getDecimalFormat(
				 * distribution.getFinalAmount() -
				 * distribution.getPaymentAmount(), "##.00")));
				 * 
				 * } else if ("CR".equals(distribution.getPaymentMode())) {
				 * distribution.setPaymentAmount(0.00);
				 * setCredit(CurrencyUtil.getDecimalFormat(distribution.
				 * getFinalAmount(), "##.00")); } else {
				 * distribution.setPaymentAmount(0.00); setCredit("NA"); }
				 */
			}

			// setCash(view);
			distributionDetailList = new LinkedList<DistributionDetail>();
			for (DistributionDetail distributionDetail : distribution.getDistributionDetails()) {
				DistributionDetail distributionTemp = distributionDetail;
				distributionTemp
						.setDisQuantity(CurrencyUtil.getDecimalFormat(distributionDetail.getQuantity(), "##.0000"));
				/*
				 * distributionTemp
				 * .setAmount(CurrencyUtil.getDecimalFormat(Double.valueOf(
				 * distributionDetail.getSellingPrice())
				 * Double.valueOf(distributionDetail.getQuantity()), "##.00"));
				 */
				distributionTemp
						.setAmount(CurrencyUtil.getDecimalFormat(Double.valueOf(distributionDetail.getCostPrice())
								* Double.valueOf(distributionDetail.getQuantity()), "##.00"));
				WarehouseProduct warehouseProduct;
				if (StringUtil.isEmpty(distributionDetail.getDistribution().getAgentId())) {
					Warehouse warehouse = locationService
							.findCoOperativeByCode(distributionDetail.getDistribution().getServicePointId());
					/*
					 * if(!getCurrentTenantId().equalsIgnoreCase("lalteer")){
					 * warehouseProduct = productDistributionService.
					 * findAvailableStockByWarehouseIdSelectedProductSeasonBatchNo
					 * (warehouse.getId(),
					 * distributionDetail.getProduct().getId(),
					 * distributionDetail.getDistribution().getSeasonCode(),
					 * distributionDetail.getBatchNo()); }else{ warehouseProduct
					 * = productDistributionService.
					 * findAvailableStockByWarehouseIdSelectedProductBatchNo(
					 * warehouse.getId(),
					 * distributionDetail.getProduct().getId(),
					 * distributionDetail.getBatchNo()); }
					 */

					warehouseProduct = productDistributionService.findAvailableStockByWarehouseIdSelectedProductBatchNo(
							warehouse.getId(), distributionDetail.getProduct().getId(),
							distributionDetail.getBatchNo());

				} else {

					warehouseProduct = productDistributionService.findAvailableStockByAgentIdProductIdBatchNo(
							distributionDetail.getDistribution().getAgentId(), distributionDetail.getProduct().getId(),
							distributionDetail.getBatchNo());

					/*
					 * if(!getCurrentTenantId().equalsIgnoreCase("lalteer")){
					 * warehouseProduct = productDistributionService.
					 * findAvailableStockByAgentIdProductIdBatchNoSeason(
					 * distributionDetail.getDistribution().getAgentId(),
					 * distributionDetail.getProduct().getId(),
					 * distributionDetail.getDistribution().getSeasonCode(),
					 * distributionDetail.getBatchNo());
					 * 
					 * }else{ warehouseProduct = productDistributionService.
					 * findAvailableStockByAgentIdProductIdBatchNo(
					 * distributionDetail.getDistribution().getAgentId(),
					 * distributionDetail.getProduct().getId(),
					 * distributionDetail.getBatchNo()); }
					 */
				}
				Double diffQty = 0.00;
				Double avlQty = 0.00;
				if (!ObjectUtil.isEmpty(warehouseProduct)) {
					diffQty = warehouseProduct.getStock() - (distributionDetail.getQuantity());
					avlQty = diffQty - distributionDetail.getQuantity();
					distributionTemp.setExistingQuantity(CurrencyUtil.getDecimalFormat((diffQty), "##.0000"));
				}
				distributionTemp.setTax(distributionDetail.getDistribution().getTax());
				distributionTemp.setAvlQty(CurrencyUtil.getDecimalFormat((warehouseProduct.getStock()), "##.0000"));
				distributionTemp.setBatchNo(warehouseProduct.getBatchNo());
				distributionDetailList.add(distributionTemp);
			}
			setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));
			setDistImgAvil(preferncesService.findPrefernceByName(ESESystem.ENABLE_DISTRIBUTION_IMAGE));
			if (distribution == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			setCurrentPage(getCurrentPage());
			command = UPDATE;
			view = DETAIL;
			request.setAttribute(HEADING, getText(DETAIL));
		} else {
			request.setAttribute(HEADING, getText(LIST));
			return LIST;
		}
		return view;
	}

	public String update() {

		String view = "";
		if (id != null && !id.equals("")) {
			distribution = productDistributionService.findDistributionById(Long.parseLong(id));
			HarvestSeason season = farmerService.findSeasonNameByCode(distribution.getSeasonCode());
			setSeasonCodeAndName(!ObjectUtil.isEmpty(season) ? season.getName() : "");
			Farmer farmer = farmerService.findFarmerByFarmerId(distribution.getFarmerId());
			distribution.setFreeDistribution((getText("distributionType" + distribution.getFreeDistribution())));
			if (!ObjectUtil.isEmpty(distribution.getDistributionDetails())) {
				Double qty = 0.0;
				{
					for (DistributionDetail distributionDetail : distribution.getDistributionDetails()) {
						qty = qty + distributionDetail.getQuantity();
					}
					setQuantiy(CurrencyUtil.getDecimalFormat((qty), "##.000"));
				}
			}

			/*
			 * ESESystem preferences = preferncesService.findPrefernceById("1");
			 * DateFormat genDate = new
			 * SimpleDateFormat(preferences.getPreferences().get(ESESystem.
			 * GENERAL_DATE_FORMAT)); if (!ObjectUtil.isEmpty(preferences)) {
			 */
			distribution
					.setTransactionTime(
							!ObjectUtil.isEmpty(distribution)
									? (!ObjectUtil.isEmpty(distribution.getTxnTime()) ? DateUtil.convertDateToString(
											distribution.getTxnTime(), getGeneralDateFormat().concat(" HH:mm:ss")) : "")
									: "");
			// }
			if (!ObjectUtil.isEmpty(farmer)) {
				setFarmerType("Registered");
			} else {
				setFarmerType("Unregistered");
			}
			setTaxPercent(distribution.getTax());
			setPayemnt(distribution.getPaymentAmount());
			setFinalAmount(distribution.getFinalAmount());
			if (distribution.getDistributionDetails().iterator().next().getSellingPrice() == 0.00) {

				/*
				 * rows.add("NA"); rows.add("NA"); rows.add("NA");
				 * rows.add("NA"); rows.add("NA");
				 */
			} else {
				distribution.setTotalAmount(
						Double.valueOf((CurrencyUtil.getDecimalFormat(distribution.getTotalAmount(), "##.00"))));
				distribution.setTax(Double.valueOf(CurrencyUtil.getDecimalFormat(distribution.getTax(), "##.00")));
				distribution.setFinalAmount(
						Double.valueOf(CurrencyUtil.getDecimalFormat(distribution.getFinalAmount(), "##.00")));
				distribution.setPaymentAmount(
						Double.valueOf(CurrencyUtil.getDecimalFormat(distribution.getPaymentAmount(), "##.00")));

				/*
				 * if ("CS".equals(distribution.getPaymentMode())) {
				 * 
				 * 
				 * setCredit((CurrencyUtil.getDecimalFormat(
				 * distribution.getFinalAmount() -
				 * distribution.getPaymentAmount(), "##.00")));
				 * 
				 * } else if ("CR".equals(distribution.getPaymentMode())) {
				 * distribution.setPaymentAmount(0.00);
				 * setCredit(CurrencyUtil.getDecimalFormat(distribution.
				 * getFinalAmount(), "##.00")); } else {
				 * distribution.setPaymentAmount(0.00); setCredit("NA"); }
				 */
			}

			// setCash(view);
			distributionDetailList = new LinkedList<DistributionDetail>();
			for (DistributionDetail distributionDetail : distribution.getDistributionDetails()) {
				DistributionDetail distributionTemp = distributionDetail;
				distributionTemp
						.setDisQuantity(CurrencyUtil.getDecimalFormat(distributionDetail.getQuantity(), "##.0000"));
				/*
				 * distributionTemp
				 * .setAmount(CurrencyUtil.getDecimalFormat(Double.valueOf(
				 * distributionDetail.getSellingPrice())
				 * Double.valueOf(distributionDetail.getQuantity()), "##.00"));
				 */

				distributionTemp
						.setAmount(CurrencyUtil.getDecimalFormat(Double.valueOf(distributionDetail.getCostPrice())
								* Double.valueOf(distributionDetail.getQuantity()), "##.00"));

				WarehouseProduct warehouseProduct;
				if (StringUtil.isEmpty(distributionDetail.getDistribution().getAgentId())) {
					Warehouse warehouse = locationService
							.findCoOperativeByCode(distributionDetail.getDistribution().getServicePointId());
					if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {
						warehouseProduct = productDistributionService
								.findAvailableStockByWarehouseIdSelectedProductSeasonBatchNo(warehouse.getId(),
										distributionDetail.getProduct().getId(),
										distributionDetail.getDistribution().getSeasonCode(),
										distributionDetail.getBatchNo());
					} else {
						warehouseProduct = productDistributionService
								.findAvailableStockByWarehouseIdSelectedProductBatchNo(warehouse.getId(),
										distributionDetail.getProduct().getId(), distributionDetail.getBatchNo());

					}
				} else {
					if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {
						warehouseProduct = productDistributionService.findAvailableStockByAgentIdProductIdBatchNoSeason(
								distributionDetail.getDistribution().getAgentId(),
								distributionDetail.getProduct().getId(),
								distributionDetail.getDistribution().getSeasonCode(), distributionDetail.getBatchNo());

					} else {
						warehouseProduct = productDistributionService.findAvailableStockByAgentIdProductIdBatchNo(
								distributionDetail.getDistribution().getAgentId(),
								distributionDetail.getProduct().getId(), distributionDetail.getBatchNo());
					}
				}

				Double diffQty = 0.00;
				Double avlQty = 0.00;
				if (!ObjectUtil.isEmpty(warehouseProduct)) {
					diffQty = warehouseProduct.getStock() - (distributionDetail.getQuantity());
					avlQty = diffQty - distributionDetail.getQuantity();
					distributionTemp.setExistingQuantity(CurrencyUtil.getDecimalFormat((diffQty), "##.0000"));
				}
				distributionTemp.setTax(distributionDetail.getDistribution().getTax());
				distributionTemp.setAvlQty(CurrencyUtil.getDecimalFormat(warehouseProduct.getStock(), "##.0000"));
				/*
				 * Double diffQty = 0.00; if
				 * (!StringUtil.isEmpty(distributionDetail.getExistingQuantity()
				 * )) { diffQty =
				 * Double.valueOf(distributionDetail.getExistingQuantity()) -
				 * (distributionDetail.getQuantity()); }
				 * distributionTemp.setAvlQty(CurrencyUtil.getDecimalFormat((
				 * diffQty), "##.0000"));
				 */
				distributionDetailList.add(distributionTemp);
			}
			ESESystem preferences1 = preferncesService.findPrefernceById("1");
			/*
			 * approved =
			 * preferncesService.findPrefernceByName(ESESystem.ENABLE_APPROVED);
			 */

			if (distribution == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));
			setCurrentPage(getCurrentPage());
			id = null;
			command = UPDATE;
			request.setAttribute(HEADING, getText(UPDATE));
			return UPDATE;
		} else {
			/*
			 * approved =
			 * preferncesService.findPrefernceByName(ESESystem.ENABLE_APPROVED);
			 */
			Set<DistributionDetail> distributionDetails = formDistributionDetailSet();
			Set<DistributionDetail> tempDistributionDetails = null;
			Distribution existingDistribution = null;
			if (!changeStockUpdate.equalsIgnoreCase("1")) {
				for (DistributionDetail detail : distributionDetails) {
					if (detail.getAmountFlag() == 1) {
						AgroTransaction eseAgrotransaction = null;
						AgroTransaction eseFarmerTransaction = null;
						List<AgroTransaction> farmerAgroTranactionList = null;
						AgroTransaction farmerAgrotxnInitBal = null;
						AgroTransaction farmerAgrotxnFinalBal = null;
						double eseAccbal;
						existingDistribution = productDistributionService
								.findDistributionById(detail.getDistributionId());
						eseAccbal = existingDistribution.getPaymentAmount();
						ESEAccount farmerEseAccount = accountService
								.findAccountByProfileId(existingDistribution.getFarmerId());
						ESEAccount eseAccount = null;
						if (StringUtil.isEmpty(existingDistribution.getAgentId())) {
							eseAccount = accountService.findAccountByProfileId(ESEAccount.BASIX_ACCOUNT);

						} else {
							eseAccount = accountService.findAccountByProfileId(existingDistribution.getAgentId());
							eseAgrotransaction = productDistributionService.findAgrotxnByReceiptNoAndProfType(
									existingDistribution.getReceiptNumber(), ESEAccount.AGENT_ACC);
							eseAgrotransaction.setIntBalance(
									eseAccount.getCashBalance() - existingDistribution.getPaymentAmount());
							if (!StringUtil.isEmpty(detail.getAmount())) {
								eseAgrotransaction.setTxnAmount(Double.valueOf(detail.getAmount()));
							} else {
								detail.setAmount("0.0");
							}
							eseAgrotransaction.setBalAmount(
									eseAgrotransaction.getIntBalance() + eseAgrotransaction.getTxnAmount());
							eseAgrotransaction.setDeviceId(NOT_APPLICABLE);
							eseAgrotransaction.setDeviceName(NOT_APPLICABLE);
							eseAgrotransaction.setModeOfPayment(ESEAccount.PAYMENT_MODE_CASH);
							eseAgrotransaction.setOperType(ESETxn.ON_LINE);
							Calendar currentDate = Calendar.getInstance();
							// DateFormat df = new
							// SimpleDateFormat(DateUtil.DATE_FORMAT);
							DateFormat df = new SimpleDateFormat(getGeneralDateFormat());
							startDate = df.format(currentDate.getTime());
							eseAgrotransaction
									.setTxnTime(DateUtil.convertStringToDate(startDate, getGeneralDateFormat()));
							eseAgrotransaction.setTxnTime(DateUtil.setTimeToDate(eseAgrotransaction.getTxnTime()));
						}
						if (!ObjectUtil.isEmpty(farmerEseAccount)) {

							farmerAgroTranactionList = productDistributionService
									.listAgroTransactionByReceiptNoAndProfType(existingDistribution.getReceiptNumber(),
											ESEAccount.FARMER_ACC);

							for (AgroTransaction agroTransaction : farmerAgroTranactionList) {
								if (agroTransaction.getTxnDesc()
										.equalsIgnoreCase(Distribution.PRODUCT_DISTRIBUTION_TO_FARMER_DESCRIPTION)) {
									farmerAgrotxnInitBal = agroTransaction;
								} else {
									farmerAgrotxnFinalBal = agroTransaction;
								}
							}

							eseFarmerTransaction = new AgroTransaction();
							farmerAgrotxnInitBal.setIntBalance(
									(existingDistribution.getFinalAmount() - existingDistribution.getPaymentAmount())
											+ farmerEseAccount.getCashBalance());
							farmerAgrotxnInitBal.setTxnAmount(Double.valueOf(detail.getFinalAmount()));
							farmerAgrotxnInitBal.setBalAmount(
									farmerAgrotxnInitBal.getIntBalance() - farmerAgrotxnInitBal.getTxnAmount());
							if (StringUtil.isEmpty(detail.getAmount())) {
								detail.setAmount("0.0");
							}
							farmerAgrotxnInitBal.setPaidAmount(Double.valueOf(detail.getAmount()));
							farmerAgrotxnInitBal.setDebitAmt(
									Double.valueOf(detail.getFinalAmount()) - Double.valueOf(detail.getAmount()));
							farmerAgrotxnInitBal.setDeviceId(NOT_APPLICABLE);
							farmerAgrotxnInitBal.setDeviceName(NOT_APPLICABLE);
						/*	farmerAgrotxnInitBal
									.setTxnTime(DateUtil.convertStringToDate(startDate, getGeneralDateFormat()));
					*/		

							farmerAgrotxnFinalBal.setIntBalance(farmerAgrotxnInitBal.getBalAmount());
							farmerAgrotxnFinalBal.setTxnAmount(Double.valueOf(detail.getAmount()));
							farmerAgrotxnFinalBal.setBalAmount(
									farmerAgrotxnFinalBal.getIntBalance() + farmerAgrotxnFinalBal.getTxnAmount());
							farmerAgrotxnFinalBal.setPaidAmount(Double.valueOf(detail.getAmount()));
							farmerAgrotxnFinalBal.setDebitAmt(
									Double.valueOf(detail.getFinalAmount()) - Double.valueOf(detail.getAmount()));
							farmerAgrotxnFinalBal.setDeviceId(NOT_APPLICABLE);
							farmerAgrotxnFinalBal.setDeviceName(NOT_APPLICABLE);
									farmerEseAccount.setCashBalance(farmerAgrotxnFinalBal.getBalAmount());
							farmerEseAccount.setCreditBalance(
									farmerEseAccount.getCreditBalance() + existingDistribution.getFinalAmount());
						}
						if (!ObjectUtil.isEmpty(eseAgrotransaction)) {
							eseAccount.setCashBalance(eseAgrotransaction.getBalAmount());
							productDistributionService.update(eseAgrotransaction);
						}
						if(!ObjectUtil.isEmpty(eseAccount))
						{
							accountService.update(eseAccount);
						}
						if (!ObjectUtil.isEmpty(farmerAgrotxnInitBal)) {
							productDistributionService.update(farmerAgrotxnInitBal);
						}
						if (!ObjectUtil.isEmpty(farmerAgrotxnFinalBal)) {
							productDistributionService.update(farmerAgrotxnFinalBal);
							farmerEseAccount.setCashBalance(farmerAgrotxnFinalBal.getBalAmount());
							accountService.update(farmerEseAccount);
						}

						existingDistribution.setTax(StringUtil.isEmpty(detail.getTax()) ? 0.00 : detail.getTax());
						if (StringUtil.isEmpty(detail.getAmount())) {
							detail.setAmount("0.0");
						}
						existingDistribution.setTotalAmount(Double.valueOf(detail.getAmount()));
						existingDistribution.setTxnAmount(Double.valueOf(detail.getAmount()));
						existingDistribution.setBalAmount(Double.valueOf(detail.getAmount()));
						existingDistribution.setPaymentAmount(Double.valueOf(detail.getAmount()));
						existingDistribution.setFinalAmount(Double.valueOf(detail.getFinalAmount()));
						existingDistribution.setPaymentMode("CS");
						existingDistribution.setUpdatedUserName(getUsername());
						Calendar currentDate = Calendar.getInstance();
						// DateFormat df = new
						// SimpleDateFormat(DateUtil.DATE_FORMAT_4);
						DateFormat df = new SimpleDateFormat(getGeneralDateFormat());
						startDate = df.format(currentDate.getTime());
						existingDistribution
								.setUpdateTime(DateUtil.convertStringToDate(startDate, getGeneralDateFormat()));
						existingDistribution
								.setUpdateTime(DateUtil.setTimeToDate(existingDistribution.getUpdateTime()));
						if (detail.getStatus() == 1) {
							existingDistribution.setStatus(1);
						}
						productDistributionService.updateDistribution(existingDistribution);
						break;
					}
				}
			}
			for (DistributionDetail detail : distributionDetails) {
				if (detail.getQtyflag() == 1) {
					DistributionDetail existingDistributionDetail = productDistributionService
							.findDistributionDetailById(Long.valueOf(detail.getId()));

					if (existingDistributionDetail.getId() == detail.getId()) {
						Double stock = Double.valueOf(detail.getExistingQuantity())
								- Double.valueOf(detail.getDisQuantity());
						if (existingDistributionDetail.getQuantity() != Double.valueOf(detail.getDisQuantity())) {
							existingDistributionDetail.setCurrentQuantity(detail.getCurrentQuantity());
						}

						if (detail.getExistingQuantity().equalsIgnoreCase(detail.getCurrentQuantity())) {
							existingDistributionDetail.setExistingQuantity(
									String.valueOf(Double.valueOf(existingDistributionDetail.getCurrentQuantity())
											- Double.valueOf(detail.getDisQuantity())));
						} else {
							existingDistributionDetail.setExistingQuantity(detail.getExistingQuantity());
						}

						existingDistributionDetail.setSellingPrice(detail.getSellingPrice());
						existingDistributionDetail.setQuantity(Double.valueOf(detail.getDisQuantity()));
						existingDistributionDetail.setExistingQuantity(detail.getExistingQuantity());
						existingDistributionDetail.setProduct(existingDistributionDetail.getProduct());
						existingDistributionDetail.setDistribution(existingDistributionDetail.getDistribution());
						existingDistributionDetail.setUnit(existingDistributionDetail.getUnit());
						existingDistributionDetail.setSubTotal(detail.getSubTotal());
						double updateStock = Double.valueOf(detail.getCurrentQuantity())
								- Double.valueOf(detail.getDisQuantity());
						WarehouseProduct warehouseProduct;
						if (StringUtil.isEmpty(detail.getAgentId())) {
							Warehouse warehouse = locationService.findCoOperativeByCode(detail.getWarehouseCode());
							warehouseProduct = productDistributionService
									.findAvailableStockByWarehouseIdSelectedProductSeasonBatchNo(warehouse.getId(),
											detail.getProductId(),
											existingDistributionDetail.getDistribution().getSeasonCode(),
											existingDistributionDetail.getBatchNo());
							warehouseProduct.setStock(
									warehouseProduct.getStock() - Double.valueOf(detail.getDisExistQuantity()));
							Set<WarehouseProductDetail> productDetails = new LinkedHashSet<WarehouseProductDetail>();
							WarehouseProductDetail warehouseProductDetail = new WarehouseProductDetail();
							warehouseProductDetail = formWarehouseProductDetail(warehouseProductDetail,
									warehouseProduct, detail);
							productDetails.add(warehouseProductDetail);
							warehouseProduct.setWarehouseDetails(productDetails);
							productDistributionService.updateWarehouseProduct(warehouseProduct);
							/*
							 * if (approved.equals("1")) { if
							 * (detail.getStatus() == 1) { Warehouse warehouse =
							 * locationService.findCoOperativeByCode(detail.
							 * getWarehouseCode());
							 * if(!getCurrentTenantId().equalsIgnoreCase(
							 * "lalteer")){ warehouseProduct
							 * =productDistributionService.
							 * findAvailableStockByWarehouseIdSelectedProductSeasonBatchNo
							 * (warehouse.getId(),
							 * detail.getProductId(),existingDistributionDetail.
							 * getDistribution().getSeasonCode(),
							 * existingDistributionDetail.getBatchNo()); }else{
							 * warehouseProduct =productDistributionService.
							 * findAvailableStockByWarehouseIdSelectedProductBatchNo
							 * (warehouse.getId(),
							 * detail.getProductId(),existingDistributionDetail.
							 * getBatchNo()); }
							 * warehouseProduct.setStock(warehouseProduct.
							 * getStock()
							 * -Double.valueOf(detail.getDisExistQuantity()));
							 * Set<WarehouseProductDetail> productDetails = new
							 * LinkedHashSet<WarehouseProductDetail>();
							 * WarehouseProductDetail warehouseProductDetail =
							 * new WarehouseProductDetail();
							 * warehouseProductDetail =
							 * formWarehouseProductDetail(
							 * warehouseProductDetail, warehouseProduct,
							 * detail);
							 * productDetails.add(warehouseProductDetail);
							 * warehouseProduct.setWarehouseDetails(
							 * productDetails);
							 * productDistributionService.updateWarehouseProduct
							 * (warehouseProduct); } } else { Warehouse
							 * warehouse =
							 * locationService.findCoOperativeByCode(detail.
							 * getWarehouseCode()); warehouseProduct =
							 * productDistributionService.
							 * findAvailableStockByWarehouseIdSelectedProductSeasonBatchNo
							 * (warehouse.getId(),
							 * detail.getProductId(),existingDistributionDetail.
							 * getDistribution().getSeasonCode(),
							 * existingDistributionDetail.getBatchNo());
							 * warehouseProduct.setStock(warehouseProduct.
							 * getStock()
							 * -Double.valueOf(detail.getDisExistQuantity()));
							 * Set<WarehouseProductDetail> productDetails = new
							 * LinkedHashSet<WarehouseProductDetail>();
							 * WarehouseProductDetail warehouseProductDetail =
							 * new WarehouseProductDetail();
							 * warehouseProductDetail =
							 * formWarehouseProductDetail(
							 * warehouseProductDetail, warehouseProduct,
							 * detail);
							 * productDetails.add(warehouseProductDetail);
							 * warehouseProduct.setWarehouseDetails(
							 * productDetails);
							 * productDistributionService.updateWarehouseProduct
							 * (warehouseProduct); }
							 */
						} else {
							warehouseProduct = productDistributionService
									.findAvailableStockByAgentIdProductIdBatchNoSeason(detail.getAgentId(),
											detail.getProductId(),
											existingDistributionDetail.getDistribution().getSeasonCode(),
											existingDistributionDetail.getBatchNo());
							warehouseProduct.setStock(
									warehouseProduct.getStock() - Double.valueOf(detail.getDisExistQuantity()));
							warehouseProduct.setRevisionNo(DateUtil.getRevisionNumber());
							Set<WarehouseProductDetail> productDetails = new LinkedHashSet<WarehouseProductDetail>();
							// List<WarehouseProductDetail> productDetail=
							// productDistributionService.findWarehouseproductDetailByWarehouseproductIdAndReceiptNo(warehouseProduct.getId(),existingDistributionDetail.getDistribution().getReceiptNumber());
							WarehouseProductDetail warehouseProductDetail = new WarehouseProductDetail();
							warehouseProductDetail = formWarehouseProductDetail(warehouseProductDetail,
									warehouseProduct, detail);
							productDetails.add(warehouseProductDetail);
							warehouseProduct.setWarehouseDetails(productDetails);
							productDistributionService.updateWarehouseProduct(warehouseProduct);
							/*
							 * if (approved.equals("1")) { if
							 * (detail.getStatus() == 1) { warehouseProduct =
							 * productDistributionService
							 * .findAvailableStockByAgentIdProductIdBatchNoSeason
							 * (detail.getAgentId(),
							 * detail.getProductId(),existingDistributionDetail.
							 * getDistribution().getSeasonCode(),
							 * existingDistributionDetail.getBatchNo());
							 * warehouseProduct.setStock(warehouseProduct.
							 * getStock()
							 * -Double.valueOf(detail.getDisExistQuantity()));
							 * Set<WarehouseProductDetail> productDetails = new
							 * LinkedHashSet<WarehouseProductDetail>(); //
							 * List<WarehouseProductDetail> productDetail= //
							 * productDistributionService.
							 * findWarehouseproductDetailByWarehouseproductIdAndReceiptNo
							 * (warehouseProduct.getId(),
							 * existingDistributionDetail.getDistribution().
							 * getReceiptNumber()); WarehouseProductDetail
							 * warehouseProductDetail = new
							 * WarehouseProductDetail(); warehouseProductDetail
							 * = formWarehouseProductDetail(
							 * warehouseProductDetail, warehouseProduct,
							 * detail);
							 * productDetails.add(warehouseProductDetail);
							 * warehouseProduct.setWarehouseDetails(
							 * productDetails);
							 * productDistributionService.updateWarehouseProduct
							 * (warehouseProduct); } } else { warehouseProduct =
							 * productDistributionService
							 * .findAvailableStockByAgentIdProductIdBatchNoSeason
							 * (detail.getAgentId(),
							 * detail.getProductId(),existingDistributionDetail.
							 * getDistribution().getSeasonCode(),
							 * existingDistributionDetail.getBatchNo());
							 * warehouseProduct.setStock(warehouseProduct.
							 * getStock()
							 * -Double.valueOf(detail.getDisExistQuantity()));
							 * Set<WarehouseProductDetail> productDetails = new
							 * LinkedHashSet<WarehouseProductDetail>(); //
							 * List<WarehouseProductDetail> productDetail= //
							 * productDistributionService.
							 * findWarehouseproductDetailByWarehouseproductIdAndReceiptNo
							 * (warehouseProduct.getId(),
							 * existingDistributionDetail.getDistribution().
							 * getReceiptNumber()); WarehouseProductDetail
							 * warehouseProductDetail = new
							 * WarehouseProductDetail(); warehouseProductDetail
							 * = formWarehouseProductDetail(
							 * warehouseProductDetail, warehouseProduct,
							 * detail);
							 * productDetails.add(warehouseProductDetail);
							 * warehouseProduct.setWarehouseDetails(
							 * productDetails);
							 * productDistributionService.updateWarehouseProduct
							 * (warehouseProduct); }
							 */
						}

						productDistributionService.updateDistributionDetail(existingDistributionDetail);
					}

				}
			}

		}

		request.setAttribute(HEADING, getText(LIST));
		return LIST;
	}

	public String delete() {
		if (id != null && !id.equals("")) {
			distribution = productDistributionService.findDistributionById(Long.parseLong(id));
			if (!ObjectUtil.isEmpty(distribution)) {
				if (!StringUtil.isEmpty(distribution.getServicePointId())) {
					Warehouse warehouse = locationService.findWarehouseByCode(distribution.getServicePointId());
					if (!ObjectUtil.isEmpty(warehouse)) {
						for (DistributionDetail distributionDetailz : distribution.getDistributionDetails()) {
							WarehouseProduct warehouseProduct = productService.findWarehouseProductbyIdAndSeasonCode(
									warehouse.getId(), distributionDetailz.getProduct().getId(),
									distribution.getSeasonCode());
							if (!ObjectUtil.isEmpty(warehouseProduct)) {
								Double updatedStock = warehouseProduct.getStock() + distributionDetailz.getQuantity();
								warehouseProduct.setStock(updatedStock);
								warehouseProduct.setRevisionNo(DateUtil.getRevisionNumber());
								farmerService.update(warehouseProduct);
							}
						}
					}
				}
				else if(distribution.getAgentId()!=null && !StringUtil.isEmpty(distribution.getAgentId()) && distribution.getTxnType().equalsIgnoreCase(Distribution.PRODUCT_DISTRIBUTION_TO_FARMER)){
					Agent agnt=agentService.findAgentByAgentId(distribution.getAgentId());
				if(agnt!=null && !ObjectUtil.isEmpty(agnt)){
					distribution.getDistributionDetails().stream().forEach(d->{
						WarehouseProduct warehouseProduct=productService.findWarehouseProductByAgentAndProductAndSeason(agnt.getId(),d.getProduct().getId() , distribution.getSeasonCode());
						if(warehouseProduct!=null && !ObjectUtil.isEmpty(warehouseProduct)){
							WarehouseProductDetail wpDetail=new WarehouseProductDetail();
							wpDetail.setDate(new Date());
							wpDetail.setDesc("DISTRIBUTION TO FARMER (FROM MOBILE USER) DELETED");
							wpDetail.setPrevStock(warehouseProduct.getStock());
							wpDetail.setTxnStock(d.getQuantity());
							wpDetail.setFinalStock(warehouseProduct.getStock()+d.getQuantity());
							wpDetail.setReceiptNo(distribution.getReceiptNumber());
							warehouseProduct.setStock(d.getQuantity()+warehouseProduct.getStock());
							warehouseProduct.setRevisionNo(DateUtil.getRevisionNumber());
							wpDetail.setWarehouseProduct(warehouseProduct);
							warehouseProduct.getWarehouseDetails().add(wpDetail);
							farmerService.update(warehouseProduct);
						}
					}); 
				}
				}
			}
			AgroTransaction agroTxn1 =null;
			List<AgroTransaction> agroTxnList=productService.listAgroTransactionByDistributionId(distribution.getId());
			HarvestSeason season = farmerService.findHarvestSeasonByCode(distribution.getSeasonCode());
			Farmer farmer = farmerService.findFarmerByFarmerId(distribution.getFarmerId());
			if(farmer!=null && !ObjectUtil.isEmpty(farmer)){
			ESEAccount farmerAccount = farmerDAO.findAccountBySeassonProcurmentProductFarmer(season.getId(), 0L,
					farmer.getId());
			
			if(agroTxnList.size()>=2){
				double txnAmt = agroTxnList.get(0).getTxnAmount();
				double balanceAmt = agroTxnList.get(1).getTxnAmount();
				double balance;
				if(txnAmt>balanceAmt){
					balance=txnAmt-(balanceAmt);
					double finalCashBalance=farmerAccount.getCashBalance()+balance;
					farmerAccount.setCashBalance(finalCashBalance);
					farmerService.update(farmerAccount);
				}
				agroTxn1 = agroTxnList.get(1);
				//farmerService.deleteObject(agroTxnList.get(1));
			}
			}
			farmerService.updateDistributionStatus(Distribution.DELETE_STATUS,distribution.getId());
			//farmerService.deleteObject(distribution);
			if(distribution.getFarmerId()!=null && !StringUtil.isEmpty(distribution.getFarmerId())){
				AgroTransaction agroTxn=new AgroTransaction();
				ESEAccount eseAccount= accountService.findAccountByProfileId(distribution.getFarmerId());
			
				
				distribution.setStatus(Distribution.DELETE_STATUS);
				agroTxn.setFarmerId(distribution.getFarmerId());
				agroTxn.setServicePointName(distribution.getServicePointName());
				agroTxn.setServicePointId(distribution.getServicePointId());
				agroTxn.setFarmerName(distribution.getFarmerName());
				agroTxn.setDistribution(distribution);
				if (eseAccount!=null && !ObjectUtil.isEmpty(eseAccount))
					agroTxn.setEseAccountId(eseAccount.getId());
				agroTxn.setTxnType(distribution.getTxnType()+"D");
				agroTxn.setTxnDesc(Distribution.DISTRIBUTION_DELETE_FARMER);
				farmerService.addAgroTxn(agroTxn);
			}
		
		}
		return LIST;
	}

	private WarehouseProductDetail formWarehouseProductDetail(WarehouseProductDetail warehouseProductDetail,
			WarehouseProduct warehouseProduct, DistributionDetail detail) {
		WarehouseProductDetail productDetail = new WarehouseProductDetail();
		// TODO Auto-generated method stub
		productDetail.setReceiptNo(detail.getReceiptNo());
		Calendar currentDate = Calendar.getInstance();
		// DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		DateFormat df = new SimpleDateFormat(getGeneralDateFormat());
		startDate = df.format(currentDate.getTime());
		// productDetail.setDate(DateUtil.convertStringToDate(startDate,
		// "MM/dd/yyyy"));
		productDetail.setDate(DateUtil.convertStringToDate(startDate, getGeneralDateFormat()));
		productDetail.setDate(DateUtil.setTimeToDate(productDetail.getDate()));
		productDetail.setDesc(Distribution.PRODUCT_DISTRIBUTION_TO_FARMER_DESCRIPTION);
		productDetail.setCostPrice(detail.getCostPrice());
		productDetail.setPrevStock(Double.valueOf(detail.getExistingQuantity()));
		productDetail.setTxnStock(Double.valueOf(detail.getDisQuantity()));
		productDetail
				.setFinalStock(Double.valueOf(detail.getExistingQuantity()) - Double.valueOf(detail.getDisQuantity()));
		productDetail.setWarehouseProduct(warehouseProduct);
		return productDetail;
	}

	public Set<DistributionDetail> formDistributionDetailSet() {

		Set<DistributionDetail> distributionDetailSet = new LinkedHashSet<>();
		if (!StringUtil.isEmpty(distributionDetails)) {
			Type distributionDetailType = new TypeToken<List<DistributionDetail>>() {
			}.getType();
			List<DistributionDetail> distributionList = new Gson().fromJson(distributionDetails,
					distributionDetailType);

			distributionDetailSet.addAll(distributionList);
		}

		return distributionDetailSet;
	}

	/**
	 * Gets the data.
	 * 
	 * @return the data
	 * @see com.sourcetrace.esesw.view.WebTransactionAction#getData()
	 */
	public Object getData() {
		ESESystem preferences = preferncesService.findPrefernceById("1");
		setHarvestSeasonEnabled(preferences.getPreferences().get(ESESystem.ENABLE_HARVEST_SEASON_INFO));
		setDistImgAvil(preferences.getPreferences().get(ESESystem.ENABLE_DISTRIBUTION_IMAGE));
		setSeasonName(getCurrentSeasonsCode() + "-" + getCurrentSeasonsName());
		cashType.put(0, getText("cashType1"));
		cashType.put(1, getText("cashType2"));
		checkBoxs.put('0', getText("distribution.freeDispatch"));
		stockTypeText=getLocaleProperty("stockTypes");
		return distribution;
	}

	/**
	 * Reset.
	 */
	private void reset() {

		AgroTimerTask distributionTask = (AgroTimerTask) request.getSession().getAttribute(agentId + "_timer");
		setReStartTxn(true);
		distributionTask.cancelTimer(false);
		request.setAttribute("agentId", agentId);
		request.getSession().removeAttribute(agentId + "_" + Distribution.PRODUCT_DISTRIBUTION_TO_FARMER + "_"
				+ WebTransactionAction.IS_FORM_RESUBMIT);
		setCurrentSeason();
		loadWarehouseAndProductData();
	}

	/**
	 * Load warehouse and product data.
	 */
	private void loadWarehouseAndProductData() {

		loadWarehouseData();
		loadProductData();

	}

	/**
	 * Load warehouse data.
	 */
	private void loadWarehouseData() {

		List<Warehouse> returnValue = new LinkedList<Warehouse>();
		if (!StringUtil.isEmpty(agentId) && ObjectUtil.isEmpty(agent)) {
			agent = agentService.findAgentByAgentId(agentId);
			setAgent(agent);
		}
		if (!ObjectUtil.isEmpty(agent) && !ObjectUtil.isEmpty(agent.getCooperative())) {
			if (agent.isCoOperativeManager())
				returnValue = locationService.listActiveFarmersSamithiByCoOperativeId(agent.getCooperative().getId());
			else
				returnValue = locationService.listActiveFarmersSamithiByAgentId(agent.getId());
		}
		if (!ObjectUtil.isListEmpty(returnValue))
			for (Warehouse warehouse : returnValue) {
				warehouseList.add(warehouse.getName() + " - " + warehouse.getCode());
			}
	}

	/**
	 * Load product data.
	 */
	private void loadProductData() {

		productsNameUnitMap = new LinkedHashMap<String, String>();
		List<Object[]> productList = new LinkedList<Object[]>();
		if (ObjectUtil.isEmpty(agent))
			agent = agentService.findAgentByProfileId(agentId);
		if (!ObjectUtil.isEmpty(agent)) {
			Warehouse warehouse = agent.getCooperative();
			if (!ObjectUtil.isEmpty(warehouse)) {
				if (agent.isCoOperativeManager())
					productList = productService.listProductNameUnitByCooperativeManagerProfileId(agentId);
				else
					productList = productService.listProductNameUnitByWarehouseAgentId(warehouse.getCode(),
							agent.getId());

				if (!ObjectUtil.isListEmpty(productList)) {
					for (Object[] productObj : productList) {
						productsNameUnitMap.put(productObj[0] + "=" + productObj[1].toString(),
								productObj[0].toString());
					}
				}
			}
		}
	}

	/**
	 * Sets the farmer and village data.
	 */
	private void setFarmerAndVillageData() {

		Farmer farmer = farmerService.findFarmerById(Long.valueOf(fId));
		setSelectedVillage(farmer.getVillage().getName() + " - " + farmer.getVillage().getCode());
		setSelectedFarmer(farmer.getFirstName() + " " + farmer.getLastName() + " - " + farmer.getFarmerId());

	}

	/**
	 * Load farmer detail.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void loadFarmerDetail() throws Exception {

		String result = "";
		String[] farmerDetail = selectedFarmer.split("-");
		farmer = farmerService.findFarmerByFarmerId(farmerDetail[0].trim());
		if (!ObjectUtil.isEmpty(farmer)) {
			farmerName = farmer.getFirstName() + " " + farmer.getLastName();
			farmerId = String.valueOf(farmer.getFarmerId());
			farmerAddress = farmer.getAddress();
			result = farmerName + "," + farmerId + "," + farmerAddress;
		}

		response.getWriter().print(result);
	}

	/**
	 * Load available stock.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void loadAvailableStock() throws Exception {

		WarehouseProduct warehouseProduct = null;
		Warehouse warehouse = new Warehouse();
		Double avbleStock = 0.0;
		String result = NOT_APPLICABLE;
		/*
		 * if (!StringUtil.isEmpty(selectedAgent)) { String[] fieldstaffArr =
		 * selectedAgent.split("-"); //
		 * warehouse=locationService.findCoOperativeByCode(fieldstaffArr[1].trim
		 * ()); }
		 */

		if (!StringUtil.isEmpty(selectedCooperative)) {
			String[] warehouseArr = selectedCooperative.split("#");
			warehouse = locationService.findCoOperativeByCode(warehouseArr[1].trim());
		}
		List<String> productUnitList = new ArrayList<String>();
		SubCategory subCategory = productService.findSubCategoryByCode(selectedCategory);
		String[] productDetail = selectedProduct.split("=");
		Product product = productService.findProductByProductCodeAndSubCategoryId(selectedProduct, subCategory.getId());
		if (!StringUtil.isEmpty(selectedCooperative) && StringUtil.isEmpty(selectedAgent)
				&& !StringUtil.isEmpty(product)) {

			// warehouseProduct=productDistributionService.findAvailableStockByWarehouseIdSelectedProductSeasonBatchNo(warehouse.getId(),product.getId(),selectedSeason,batchNo);
			avbleStock = productDistributionService.findAvailableStockByWarehouseIdProductIdSeasonBatchNo(
					warehouse.getId(), product.getId(), selectedSeason, batchNo);

			/*
			 * warehouseProduct =
			 * productDistributionService.findAvailableStock(warehouse.getId(),
			 * product.getId());
			 */
		} else if (StringUtil.isEmpty(selectedCooperative) && !StringUtil.isEmpty(selectedAgent)
				&& !StringUtil.isEmpty(product)) {
			/*
			 * warehouseProduct = productDistributionService.
			 * findAvailableStockByAgentIdProductIdBatchNoSeason(
			 * selectedAgent.split("-")[1].trim(),
			 * product.getId(),selectedSeason,batchNo);
			 */
			Agent agent = agentService.findAgent(Long.valueOf(selectedAgent));
			if (agent != null) {
				avbleStock = productDistributionService.findAvailableStockByAgentIdProductIdSeasonBatchNo(
						agent.getProfileId(), product.getId(), selectedSeason, batchNo);
			}
		}

		if (!ObjectUtil.isEmpty(avbleStock)) {
			// stock = warehouseProduct.getStock();
			stock = avbleStock;
			units = product.getUnit();
			productPrice = product.getPrice();
			result = stock + "," + productPrice + "," + units;
		} else {
			stock = 0.000;
			productPrice = "0";
			units = NOT_APPLICABLE;
		}
		result = stock + "," + productPrice + "," + units;

		response.getWriter().print(result);
	}

	public void loadAvailableStockLalteer() throws Exception {

		WarehouseProduct warehouseProduct = null;
		Warehouse warehouse = new Warehouse();
		Double avbleStock = 0.0;
		String result = NOT_APPLICABLE;
		/*
		 * if (!StringUtil.isEmpty(selectedAgent)) { String[] fieldstaffArr =
		 * selectedAgent.split("-"); //
		 * warehouse=locationService.findCoOperativeByCode(fieldstaffArr[1].trim
		 * ()); }
		 */

		if (!StringUtil.isEmpty(selectedCooperative)) {
			String[] warehouseArr = selectedCooperative.split("#");
			warehouse = locationService.findCoOperativeByCode(warehouseArr[1].trim());
		}
		List<String> productUnitList = new ArrayList<String>();
		SubCategory subCategory = productService.findSubCategoryByCode(selectedCategory);
		String[] productDetail = selectedProduct.split("=");
		if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {
			product = productService.findProductByProductCodeAndSubCategoryId(selectedProduct, subCategory.getId());
		} else {
			product = productService.findProductByProductIdAndSubCategoryId(Long.valueOf(selectedProduct),
					subCategory.getId());
		}

		if (!StringUtil.isEmpty(selectedCooperative) && StringUtil.isEmpty(selectedAgent)
				&& !StringUtil.isEmpty(product)) {

			// warehouseProduct=productDistributionService.findAvailableStockByWarehouseIdSelectedProductSeasonBatchNo(warehouse.getId(),product.getId(),selectedSeason,batchNo);
			avbleStock = productDistributionService.findAvailableStockByWarehouseIdProductIdBatchNum(warehouse.getId(),
					product.getId(), batchNo);

			/*
			 * warehouseProduct =
			 * productDistributionService.findAvailableStock(warehouse.getId(),
			 * product.getId());
			 */
		} else if (StringUtil.isEmpty(selectedCooperative) && !StringUtil.isEmpty(selectedAgent)
				&& !StringUtil.isEmpty(product)) {
			/*
			 * warehouseProduct = productDistributionService.
			 * findAvailableStockByAgentIdProductIdBatchNoSeason(
			 * selectedAgent.split("-")[1].trim(),
			 * product.getId(),selectedSeason,batchNo);
			 */
			Agent agent = agentService.findAgent(Long.valueOf(selectedAgent));
			if (agent != null) {

				avbleStock = productDistributionService
						.findAvailableStockByAgentIdProductIdBatchNum(agent.getProfileId(), product.getId(), batchNo);
			}
		}

		if (!ObjectUtil.isEmpty(avbleStock)) {
			// stock = warehouseProduct.getStock();
			stock = avbleStock;
			units = product.getUnit();
			productPrice = product.getPrice();
			result = stock + "," + productPrice + "," + units;
		} else {
			stock = 0.000;
			productPrice = "0";
			units = NOT_APPLICABLE;
		}
		result = stock + "," + productPrice + "," + units;

		response.getWriter().print(result);
	}

	public void populateBatchNo() {

		WarehouseProduct warehouseProduct = null;
		Warehouse warehouse = new Warehouse();
		List<Object[]> batchNoList = null;
		/*
		 * if (!StringUtil.isEmpty(selectedAgent)) { String[] fieldstaffArr =
		 * selectedAgent.split("-"); //
		 * warehouse=locationService.findCoOperativeByCode(fieldstaffArr[1].trim
		 * ()); }
		 */
		if (!StringUtil.isEmpty(selectedCooperative)) {
			String[] warehouseArr = selectedCooperative.split("#");
			warehouse = locationService.findCoOperativeByCode(warehouseArr[1].trim());
		}

		List<String> productUnitList = new ArrayList<String>();
		SubCategory subCategory = productService.findSubCategoryByCode(selectedCategory);
		String[] productDetail = selectedProduct.split("=");
		Product product = productService.findProductByProductCodeAndSubCategoryId(selectedProduct, subCategory.getId());
		// WarehouseProduct
		// warehouseProducts=productDistributionService.findAvailableStockByWarehouseIdSelectedProductSeason(warehouse.getId(),product.getId(),selectedSeason);
		if (!StringUtil.isEmpty(selectedCooperative) && StringUtil.isEmpty(selectedAgent)
				&& !StringUtil.isEmpty(product)) {
			/* if(Math.abs(warehouseProducts.getStock())>0.0){ */
			batchNoList = productDistributionService.findBatchNoListByWarehouseIdProductIdSeason(warehouse.getId(),
					product.getId(), selectedSeason);

			/*
			 * warehouseProduct =
			 * productDistributionService.findAvailableStock(warehouse.getId(),
			 * product.getId());
			 */
		} else if (StringUtil.isEmpty(selectedCooperative) && !StringUtil.isEmpty(selectedAgent)
				&& !StringUtil.isEmpty(product)) {
			/*
			 * warehouseProduct =
			 * productDistributionService.findFieldStaffAvailableStock(
			 * selectedAgent.split("-")[1].trim(), product.getId());
			 * 
			 * if(Math.abs(warehouseProducts.getStock())>0.0){
			 */
			Agent agent = agentService.findAgent(Long.valueOf(selectedAgent));
			if (agent != null) {
				batchNoList = productDistributionService.findBatchNoListByAgentIdProductIdSeason(agent.getProfileId(),
						product.getId(), selectedSeason);
			}

		}
		JSONArray jsonArray = new JSONArray();
		if (!ObjectUtil.isListEmpty(batchNoList)) {
			for (Object[] productObj : batchNoList) {
				if (!productObj[1].toString().equalsIgnoreCase("NA"))
					jsonArray.add(getJSONObject(productObj[1].toString(), productObj[1].toString()));
			}
		}
		sendAjaxResponse(jsonArray);
	}

	/**
	 * Gets the warehouse list.
	 * 
	 * @return the warehouse list
	 */
	public List<String> getWarehouseList() {

		List<Warehouse> returnValue = locationService.listOfSamithi();
		for (Warehouse warehouse : returnValue) {
			warehouseList.add(warehouse.getName() + " - " + warehouse.getCode());
		}
		return warehouseList;
	}

	/**
	 * Gets the village list.
	 * 
	 * @return the village list
	 */
	public List<String> getVillageList() {

		if (!StringUtil.isEmpty(selectedWarehouse)) {
			String[] warehouse = selectedWarehouse.split("-");
			List<Village> returnValue = locationService.listVillagesByCityCode(warehouse[1].trim());
			for (Village village : returnValue) {
				villageList.add(village.getName() + " - " + village.getCode());

			}
		}
		return villageList;

	}

	/**
	 * Gets the farmer list.
	 * 
	 * @return the farmer list
	 */
	public Map<String, String> getFarmerList() {

		String currentSeasonCode = getCurrentSeasonsCode();
		if (!StringUtil.isEmpty(selectedVillage) && !StringUtil.isEmpty(currentSeasonCode)) {
			String[] villageId = selectedVillage.split("-");
			List<Farmer> listFarmer = farmerService
					.listActiveContractFarmersByVillageCodeSeasonCode(villageId[1].trim(), currentSeasonCode);
			if (!ObjectUtil.isListEmpty(listFarmer)) {
				/*
				 * for (Farmer farmer : listFarmer) {
				 * farmersList.add(farmer.getFirstName() + " " +
				 * farmer.getLastName() + " - " + farmer.getFarmerId()); }
				 */

				for (Farmer farmer : listFarmer) {
					farmersList.put((farmer.getFirstName() + " " + farmer.getLastName() + " - " + farmer.getFarmerId()),
							farmer.getFirstName() + " " + farmer.getLastName() + " - " + farmer.getFarmerCode());
				}
			}
		}
		return farmersList;
	}

	/**
	 * Gets the season list.
	 * 
	 * @return the season list
	 */
	public Map<String, String> getSeasonList() {

		List<Season> listSeason = farmerService.listSeasons();
		Map<String, String> returnMap = new LinkedHashMap<String, String>();
		if (!ObjectUtil.isListEmpty(listSeason)) {
			for (Season season : listSeason)
				returnMap.put(String.valueOf(season.getId()), season.getName() + "-" + season.getYear());
		}
		return returnMap;
	}

	/**
	 * Populate farmer.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String populateFarmer() throws Exception {

		/*
		 * String currentSeasonCode = getCurrentSeasonCode(); if
		 * (!selectedVillage.equalsIgnoreCase("null") &&
		 * (!StringUtil.isEmpty(selectedVillage) &&
		 * !StringUtil.isEmpty(selectedWarehouse)) &&
		 * !StringUtil.isEmpty(currentSeasonCode)) { String[] villageId =
		 * selectedVillage.split("-"); String[] samithiCode =
		 * selectedWarehouse.split("-"); List<Farmer> listFarmer = farmerService
		 * .listActiveContractFarmersByVillageCodeSeasonCodeSamithiCode(
		 * villageId[1].trim(), currentSeasonCode, samithiCode[1].trim()); if
		 * (!ObjectUtil.isListEmpty(listFarmer)) { for (Farmer farmer :
		 * listFarmer) { farmersList.add(farmer.getFirstName() + " " +
		 * farmer.getLastName() + " - " + farmer.getFarmerId());
		 * if(!StringUtil.isEmpty(farmer.getFarmerCode()))
		 * farmersList.put((farmer.getFirstName() + " " + farmer.getLastName() +
		 * " - " + farmer.getFarmerId()), farmer.getFirstName() + " " +
		 * farmer.getLastName() + " - " + farmer.getFarmerCode()); else
		 * if(!StringUtil.isEmpty(farmer.getLastName())){
		 * farmersList.put((farmer.getFirstName() + " " + farmer.getLastName() +
		 * " - " + farmer.getFarmerId()), farmer.getFirstName() + " " +
		 * farmer.getLastName()); } else farmersList.put((farmer.getFirstName()
		 * + " "+ " - "+ farmer.getFarmerId()), farmer.getFirstName()); } } }
		 * sendResponse(farmersList);
		 */

		if (!selectedVillage.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedVillage))) {
			List<Object[]> listFarmer = farmerService.listFarmerCodeIdNameByVillageCode(selectedVillage);

			listFarmer.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
				Object[] objArr = (Object[]) obj;
				farmersList.put((objArr[2] + "-" + objArr[0]),
						(objArr[2] + "" + (!StringUtil.isEmpty(objArr[3]) ? (" " + objArr[3]) : " ")
								+ (!StringUtil.isEmpty(objArr[1]) ? ("-" + objArr[1]) : " ")));
			});

			sendResponse(farmersList);
		}
		return null;
	}

	/**
	 * Gets the list category.
	 * 
	 * @return the list category
	 */
	public List<String> getListCategory() {

		List<Category> returnValue = categoryService.listCategory();
		for (Category category : returnValue) {
			categoryList.add(category.getCode() + " - " + category.getName());
		}
		return categoryList;
	}

	/**
	 * Gets the list sub category.
	 * 
	 * @return the list sub category
	 */
	public List<String> getListSubCategory() {

		if (!StringUtil.isEmpty(selectedCategory)) {
			List<SubCategory> subCategoryList = categoryService.listSubCategoryByCategory(selectedCategory);
			if (!ObjectUtil.isListEmpty(subCategoryList)) {
				for (SubCategory subCategory : subCategoryList) {
					subCategorysList.add(subCategory.getCode() + " - " + subCategory.getName());
				}
			}
		}
		return subCategorysList;
	}

	/**
	 * Populate subcategory.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String populateSubcategory() throws Exception {

		if (!selectedCategory.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedCategory))) {
			String[] subcategory = selectedCategory.split("-");
			List<SubCategory> listSubCategory = categoryService.findSubCategoryByCategoryCode(subcategory[0].trim());
			if (!ObjectUtil.isListEmpty(listSubCategory)) {
				for (SubCategory innerCategory : listSubCategory) {
					subCategorysList.add(innerCategory.getCode() + " - " + innerCategory.getName());
				}
			}
		}
		sendResponse(subCategorysList);
		return null;
	}

	/**
	 * Gets the products name unit map.
	 * 
	 * @return the products name unit map
	 */
	public Map<String, String> getProductsNameUnitMap() {

		return productsNameUnitMap;
	}

	/**
	 * Populate product.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	/*
	 * public String populateProduct() throws Exception { Map<String, String>
	 * returnMap = new LinkedHashMap<String, String>(); if
	 * (!StringUtil.isEmpty(selectedWarehouse)) { String[] warehouse =
	 * selectedWarehouse.split("-"); List<Object[]> productList =
	 * productService.listProductNameUnitByWarehouse(warehouse[1] .trim()); if
	 * (!ObjectUtil.isListEmpty(productList)) { for (Object[] productObj :
	 * productList) { returnMap.put(productObj[0].toString(),
	 * productObj[1].toString()); } } } PrintWriter out = null; try {
	 * response.setCharacterEncoding("UTF-8");
	 * response.setContentType("text/html"); out = response.getWriter();
	 * out.print(returnMap); } catch (IOException e) { e.printStackTrace(); }
	 * return null; }
	 */

	/**
	 * Populate village.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String populateVillage() throws Exception {

		if (!StringUtil.isEmpty(selectedWarehouse)) {
			String[] warehouse = selectedWarehouse.split("-");
			List<Village> returnValue = locationService.listActiveFarmersVillageBySamithiCode(warehouse[1].trim());
			if (!ObjectUtil.isListEmpty(returnValue)) {
				for (Village village : returnValue) {
					villageList.add(village.getName() + " - " + village.getCode());

				}
			}
		}
		sendResponse(villageList);
		return null;
	}

	/**
	 * Populate village for un registered farmer.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void populateVillageForUnRegisteredFarmer() throws Exception {

		List<Village> villageList = new ArrayList<Village>();
		villageList = locationService.listVillage();
		JSONArray villageArr = new JSONArray();

		if (!ObjectUtil.isEmpty(villageList)) {
			for (Village village : villageList) {
				villageArr.add(getJSONObject(village.getName() + " - " + village.getCode(), village.getName()));
			}
		}
		sendAjaxResponse(villageArr);

	}

	/**
	 * Form error.
	 * 
	 * @param error
	 *            the error
	 * @return the string
	 */
	public String formError(String error) {

		addActionError(getText(error));
		request.setAttribute(HEADING, getText(INPUT));
		return INPUT;
	}

	/**
	 * Populate validations.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void populateDistribution() throws Exception {
		Double avbleStock = 0.0;
		boolean isFreeDispatch = true;
		String result = "";
		Agent agent = null;
		if (!StringUtil.isEmpty(selectedAgent)) {
			// String[] agentArry = selectedAgent.split("-");
			agent = agentService.findAgent(Long.valueOf(selectedAgent));
			if (ObjectUtil.isEmpty(agent)) {
				result = "comOrFs.invalidAgent";
			} else if (Agent.ACTIVE != agent.getStatus()) {
				result = agent.getAgentType().getCode() + "inactiveAgent";
			} else if (!agent.isCoOperativeManager()) {
				ESEAccount fieldStaffAcct = accountService.findAccountByProfileIdAndProfileType(agent.getProfileId(),
						ESEAccount.AGENT_ACCOUNT);
				if (ObjectUtil.isEmpty(fieldStaffAcct))
					result = "disaccountUnavailable";
				else if (ESEAccount.ACTIVE != fieldStaffAcct.getStatus())
					result = "disaccountInactive";
			}
		}

		if (StringUtil.isEmpty(result)) {
			if (registeredFarmer) {
				Farmer farmer = farmerService.findFarmerByFarmerId(farmerId);
				if (ObjectUtil.isEmpty(farmer))
					result = "farmerUnavailable";
				else if (Farmer.Status.ACTIVE.ordinal() != farmer.getStatus())
					result = "farmerInactive";
			}
		}

		if (productStock) {
			if (StringUtil.isEmpty(selectedCooperative)) {
				result = "emptyCooperative";
			} else {
				String[] warehouseArry = selectedCooperative.split("#");
				Warehouse warehouse = locationService.findCoOperativeByCode(warehouseArry[1].trim());
				if (ObjectUtil.isEmpty(warehouse)) {
					result = "cooperativeNotExist";
				}
			}
		}

		if (StringUtil.isEmpty(result)) {
			WarehouseProduct warehouseProduct = null;
			if (!StringUtil.isEmpty(productTotalString)) {
				String[] productTotalArray = productTotalString.trim().split("\\|\\|");
				for (int i = 0; i < productTotalArray.length; i++) {
					String[] productDetail = productTotalArray[i].split("##");
					double quantity = Double.valueOf(productDetail[1]);
					String[] productInfo = productDetail[0].split("=");
					if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {
						product = productService.findProductByCode(productInfo[0].trim());
					} else {
						product = productService.findProductById(Long.valueOf(productInfo[0].trim()));
					}

					if (!ObjectUtil.isEmpty(product)) {
						if (ObjectUtil.isEmpty(agent)) {
							String[] warehouseArry = selectedCooperative.split("#");
							Warehouse warehouse = locationService.findCoOperativeByCode(warehouseArry[1].trim());
							/*
							 * warehouseProduct =
							 * productDistributionService.findAvailableStock(
							 * warehouse.getId(), product.getId());
							 */
							// warehouseProduct=productDistributionService.findAvailableStockByWarehouseIdSelectedProductSeasonBatchNo(
							// warehouse.getId(), product.getId(),
							// selectedSeason, productDetail[5]);
							if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {
								avbleStock = productDistributionService
										.findAvailableStockByWarehouseIdProductIdSeasonBatchNo(warehouse.getId(),
												product.getId(), selectedSeason, productDetail[5]);

							} else {
								avbleStock = productDistributionService
										.findAvailableStockByWarehouseIdProductIdBatchNum(warehouse.getId(),
												product.getId(), productDetail[5]);
							}
						} else
						/*
						 * warehouseProduct = productDistributionService
						 * .findFieldStaffAvailableStock(agent.getProfileId( ),
						 * product .getId());
						 */
						// warehouseProduct =
						// productDistributionService.findAvailableStockByAgentIdProductIdBatchNoSeason(agent.getProfileId(),product.getId(),
						// selectedSeason, productDetail[5]);
						if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {

							avbleStock = productDistributionService.findAvailableStockByAgentIdProductIdSeasonBatchNo(
									agent.getProfileId(), product.getId(), selectedSeason, productDetail[5]);
						} else {
							avbleStock = productDistributionService.findAvailableStockByAgentIdProductIdBatchNum(
									agent.getProfileId(), product.getId(), productDetail[5]);

						}

						if (ObjectUtil.isEmpty(avbleStock) || avbleStock < quantity) {
							result = getText("insufficientstockfor") + " " + product.getName() + " "
									+ product.getUnit();
							break;
						}
					}

				}
			}

			if (StringUtil.isEmpty(result)) {
				AgroTransaction agroTransaction = new AgroTransaction();
				agroTransaction.setBranch_id(getBranchId());
				if (StringUtil.isEmpty(receiptNumber))
					distribution = new Distribution();
				Farmer farmer = null;
				if (registeredFarmer) {
					farmer = farmerService.findFarmerByFarmerId(farmerId);
					if (!ObjectUtil.isEmpty(farmer) && !ObjectUtil.isEmpty(farmer.getVillage())) {
						Village village = locationService.findVillageById(farmer.getVillage().getId());
						if (!ObjectUtil.isEmpty(village))
							distribution.setVillage(village);
					}
					agroTransaction.setFarmerId(farmer.getFarmerId());
					agroTransaction.setFarmerName(farmer.getFirstName());
					distribution.setFarmerId(farmer.getFarmerId());
					distribution.setFarmerName(farmer.getFirstName());

				} else {
					if (!StringUtil.isEmpty(selectedVillage)) {
						Village village = locationService.findVillageByCode(selectedVillage.split("-")[1].trim());
						distribution.setVillage(village);
					}
					distribution.setMobileNumber(mobileNo);
					distribution.setFarmerId(NOT_APPLICABLE);
					distribution.setFarmerName(unRegisterFarmerName);
					agroTransaction.setFarmerId(NOT_APPLICABLE);
					agroTransaction.setFarmerName(unRegisterFarmerName);
				}

				String receiptnumberSeq = idGenerator.getDistributionSeq();
				receiptNumber = receiptnumberSeq;
				agroTransaction.setReceiptNo(receiptnumberSeq);
				agroTransaction.setSeasonCode(getCurrentSeasonsCode());
				distribution.setReceiptNumber(receiptNumber);
				// Calendar currentDate = Calendar.getInstance();
				agroTransaction.setTxnTime(DateUtil.convertStringToDate(selectedDate, getGeneralDateFormat()));
				agroTransaction.setTxnTime(DateUtil.setTimeToDate(agroTransaction.getTxnTime()));
				if (registeredFarmer && !StringUtil.isEmpty(selectedWarehouse) && selectedWarehouse.contains("-")) {
					Warehouse samithi = locationService
							.findWarehouseByCode(selectedWarehouse.trim().split("-")[1].trim());
					agroTransaction.setSamithi(samithi);
					distribution.setSamithiName(samithi.getName());
				}
				if (!StringUtil.isEmpty(selectedAgent)) {
					agent = agentService.findAgent(Long.valueOf(selectedAgent));
					ESEAccount fieldStaffaccount = accountService
							.findAccountByProfileIdAndProfileType(agent.getProfileId(), ESEAccount.AGENT_ACCOUNT);
					if (ObjectUtil.isEmpty(fieldStaffaccount)) {
						result = "agentInactive";
					}

					agroTransaction.setAgentId(agent.getProfileId());
					agent = agentService.findAgentByProfileId(agent.getProfileId());
					agroTransaction.setAgentName(agent.getPersonalInfo().getAgentName());
					distribution.setAgentId(agroTransaction.getAgentId());
					distribution.setAgentName(agroTransaction.getAgentName());
					if(getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
						distribution.setWarehouseCode(!ObjectUtil.isEmpty(agent.getProcurementCenter())?agent.getProcurementCenter().getCode():"");
						distribution.setWarehouseName(!ObjectUtil.isEmpty(agent.getProcurementCenter())?agent.getProcurementCenter().getName():"");
						distribution.setSamithiId(!ObjectUtil.isEmpty(farmer.getSamithi())?String.valueOf(farmer.getSamithi().getId()):"");
						}
					// Warehouse warehouse =
					// locationDAO.findCoOperativeByCode(distribution.getAgroTransaction().getServicePointId());
				}
				/*
				 * ESEAccount fieldStaffaccount =
				 * accountService.findAccountByProfileIdAndProfileType(
				 * selectedAgent, ESEAccount.AGENT_ACCOUNT); if
				 * (!agent.isCoOperativeManager() &&
				 * ObjectUtil.isEmpty(fieldStaffaccount)) { return REDIRECT; }
				 * agroTransaction.setAgentId(selectedAgent.split("-")[1].trim()
				 * ); agent =
				 * agentService.findAgentByProfileId(selectedAgent.split("-")[1]
				 * .trim());
				 * agroTransaction.setAgentName(agent.getPersonalInfo().
				 * getAgentName());
				 */

				if (!StringUtil.isEmpty(selectedCooperative)) {
					// String[] cooperativeAry = selectedCooperative.split("#");
					Warehouse warehouseTemp = locationService
							.findCoOperativeByCode(selectedCooperative.split("#")[1].trim());
					if (!ObjectUtil.isEmpty(warehouseTemp)) {
						agroTransaction.setServicePointId(warehouseTemp.getCode());
						agroTransaction.setServicePointName(warehouseTemp.getName());
						distribution.setServicePointId(agroTransaction.getServicePointId());
					}
				}
				
					
				
				// Warehouse warehouse = agent.getCooperative();
				agroTransaction.setDistribution(distribution);
				agroTransaction.setDeviceId(NOT_APPLICABLE);
				agroTransaction.setDeviceName(NOT_APPLICABLE);
				agroTransaction.setOperType(ESETxn.ON_LINE);
				distribution.setServicePointId(agroTransaction.getServicePointId());
				distribution.setServicePointName(agroTransaction.getServicePointName());
				distribution.setTxnTime(agroTransaction.getTxnTime());
				agroTransaction.setProfType(Profile.CLIENT);
				agroTransaction.setTxnType(Distribution.PRODUCT_DISTRIBUTION_TO_FARMER);
				agroTransaction.setTxnDesc(Distribution.PRODUCT_DISTRIBUTION_TO_FARMER_DESCRIPTION);
				ESEAccount fieldStaffaccount = null;
				if (productStock) {
					agroTransaction.setProductStock(WarehouseProduct.StockType.WAREHOUSE_STOCK.name());
					agroTransaction.setWarehouseCode(selectedCooperative.split("#")[1].trim());
					distribution.setProductStock(agroTransaction.getProductStock());
				} else {
					agroTransaction.setProductStock(WarehouseProduct.StockType.AGENT_STOCK.name());
					distribution.setProductStock(agroTransaction.getProductStock());
					agent = agentService.findAgent(Long.valueOf(selectedAgent));
					fieldStaffaccount = accountService.findAccountByProfileIdAndProfileType(agent.getProfileId(),
							ESEAccount.AGENT_ACCOUNT);

				}

				// PROCT DETAILS FOR DISTRIBUTION_DETAIL
				Set<DistributionDetail> distributionDetailsSet = new HashSet<DistributionDetail>();
				double totalAmount = 0;
				DistributionDetail distributionDetail = new DistributionDetail();
				if (!StringUtil.isEmpty(productTotalString)) {
					String[] productTotalArray = productTotalString.trim().split("\\|\\|");
					for (int j = 0; j < productTotalArray.length; j++) {
						String[] productDetail = productTotalArray[j].split("##");
						String[] productInfo = productDetail[0].split("=");
						String[] warehouseArr = selectedCooperative.split("#");
						if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {
							product = productService.findProductByCode(productInfo[0].trim());
						} else {
							product = productService.findProductById(Long.valueOf(productInfo[0].trim()));
						}
						// Product product =
						// productService.findProductByCode(productInfo[0].trim());

						if (!ObjectUtil.isEmpty(product)) {
							if (ObjectUtil.isEmpty(agent)) {
								String[] warehouseArry = selectedCooperative.split("#");
								Warehouse warehouse = locationService.findCoOperativeByCode(warehouseArry[1].trim());
								/*
								 * warehouseProduct =
								 * productDistributionService.
								 * findAvailableStock( warehouse.getId(),
								 * product.getId());
								 */
								// warehouseProduct=productDistributionService.findAvailableStockByWarehouseIdSelectedProductSeasonBatchNo(
								// warehouse.getId(), product.getId(),
								// selectedSeason, productDetail[5]);

								if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {
									avbleStock = productDistributionService
											.findAvailableStockByWarehouseIdProductIdSeasonBatchNo(warehouse.getId(),
													product.getId(), selectedSeason, productDetail[5]);

								} else {
									avbleStock = productDistributionService
											.findAvailableStockByWarehouseIdProductIdBatchNum(warehouse.getId(),
													product.getId(), productDetail[5]);
								}

							}

							else
							/*
							 * warehouseProduct = productDistributionService
							 * .findFieldStaffAvailableStock(agent.
							 * getProfileId(), product .getId());
							 */
							// warehouseProduct =
							// productDistributionService.findAvailableStockByAgentIdProductIdBatchNoSeason(agent.getProfileId(),product.getId(),
							// selectedSeason, productDetail[5]);
							if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {

								avbleStock = productDistributionService
										.findAvailableStockByAgentIdProductIdSeasonBatchNo(agent.getProfileId(),
												product.getId(), selectedSeason, productDetail[5]);
							} else {
								avbleStock = productDistributionService.findAvailableStockByAgentIdProductIdBatchNum(
										agent.getProfileId(), product.getId(), productDetail[5]);

							}

						}

						Product productDetails = productService.findProductByCode(productInfo[0].trim());
						double quantity = Double.valueOf(productDetail[1]);
						// double pricePerUnit =
						// Double.valueOf(productDetail[2]);
						// double sellingPrice =
						// Double.valueOf(productDetail[2]);
						double sellingPrice = Double.valueOf(productDetail[2].replaceAll(",", ""));
						double subTotal = Double.valueOf(productDetail[3]);
						distributionDetail = new DistributionDetail();
						distributionDetail.setProduct(product);
						try {
							distributionDetail.setDistribution(distribution);
							distributionDetail.setQuantity(quantity);
							distributionDetail.setUnit(productInfo[1].trim());
							// distributionDetail.setPricePerUnit(pricePerUnit);
							distributionDetail.setExistingQuantity(String.valueOf(avbleStock));
							distributionDetail.setCurrentQuantity(
									String.valueOf(Double.valueOf(distributionDetail.getExistingQuantity())
											- distributionDetail.getQuantity()));
							distributionDetail.setSellingPrice(sellingPrice);
							distributionDetail.setCostPrice(Double.valueOf(productDetail[4].trim()));
							distributionDetail.setTax(taxValue);
							distributionDetail.setBatchNo(productDetail[5]);
							distributionDetail.setSeasonCode(selectedSeason);
							distribution.setTax(taxValue);
							distribution.setFinalAmount(finalAmtValue);
							distributionDetail.setSubTotal(subTotal);
							totalAmount = totalAmount + (Double.valueOf(productDetail[3]));
							distribution.setTotalAmount(totalAmount);
						} catch (Exception e) {
							e.printStackTrace();
						}
						distributionDetailsSet.add(distributionDetail);

					}
				}
				agroTransaction.setTxnAmount(totalAmount);

				if (!ObjectUtil.isEmpty(fieldStaffaccount)) {
					agroTransaction.setIntBalance(fieldStaffaccount.getDistributionBalance());
					agroTransaction.setBalAmount(agroTransaction.getIntBalance() + agroTransaction.getTxnAmount());
					fieldStaffaccount.setDistributionBalance(agroTransaction.getBalAmount());
					agroTransaction.setAccount(fieldStaffaccount);

				}

				ESEAccount farmerAccount = null;
				Season season = productDistributionService.findCurrentSeason();
				if (registeredFarmer) {
					farmer = farmerService.findFarmerByFarmerId(farmerId);
					if (!StringUtil.isEmpty(farmer.getId()) && !ObjectUtil.isEmpty(season)) {
						farmerAccount = farmerDAO.findAccountBySeassonProcurmentProductFarmer(season.getId(), 0L,
								farmer.getId());
						if (!ObjectUtil.isEmpty(farmerAccount)) {
							/*
							 * if ("0".equals(paymentMode)) { {
							 */
							// distribution.setPaymentMode(ESEAccount.PAYMENT_MODE_CASH);
							agroTransaction.setIntBalance(farmerAccount.getCashBalance());
							agroTransaction.setTxnAmount(finalAmtValue);
							/*
							 * if(farmerAccount.getCashBalance()>0.00){
							 * agroTransaction.setBalAmount(getCashBalance(
							 * farmerAccount .getCashBalance(),
							 * agroTransaction.getTxnAmount(), false)); }else{
							 * agroTransaction.setBalAmount(farmerAccount
							 * .getCashBalance());
							 */
							agroTransaction
									.setBalAmount(agroTransaction.getIntBalance() - agroTransaction.getTxnAmount());
							// }

							agroTransaction.setAccount(farmerAccount);
							// }
						} /*
							 * else if ("1".equals(paymentMode)) {
							 * distribution.setPaymentMode(ESEAccount.
							 * PAYMENT_MODE_CREDIT);
							 * agroTransaction.setIntBalance(farmerAccount.
							 * getCreditBalance());
							 * agroTransaction.setTxnAmount(agroTransaction.
							 * getTxnAmount());
							 * agroTransaction.setBalAmount(getCreditBalance(
							 * farmerAccount .getCreditBalance(),
							 * agroTransaction.getTxnAmount(), true));
							 * agroTransaction.setAccount(farmerAccount); }
							 */
						// }
					}
				}

				// Season season =
				// farmerService.findSeasonById(Long.valueOf(selectedSeason));
				distribution.setDistributionDetails(distributionDetailsSet);
				distribution.setFreeDistribution(String.valueOf(freeDistribution));
				distribution.setTenantId(getCurrentTenantId());
				// distribution.setSeason(season);saveDistributionAndDistributionDetail
				// if ("0".equals(paymentMode)) {
				// distribution.setPaymentMode(ESEAccount.PAYMENT_MODE_CASH);
				// distribution.setPaymentAmount(paymentAmount);
				// distribution.setPaymentAmount(getPaymentAmount());
				// } else
				if ("1".equals(paymentMode)) {
					/*
					 * distribution.setPaymentMode(ESEAccount.
					 * PAYMENT_MODE_CREDIT);
					 * distribution.setPaymentAmount(finalAmtValue);
					 */
					distribution.setPaymentMode(ESEAccount.PAYMENT_MODE_CASH);
					distribution.setPaymentAmount(getPaymentAmount());

				} /*
					 * else { distribution.setPaymentMode(paymentMode); }
					 */
				if (freeDistribution.equalsIgnoreCase("0")) {
					if (StringUtil.isEmpty(paymentRupee)) {
						paymentRupee = "0";
					}
					if (StringUtil.isEmpty(paymentPaise)) {
						paymentPaise = "0";
					}
					distribution.setTxnAmount(Double.valueOf(paymentRupee) + Double.valueOf(paymentPaise));
				}
				distribution.setIntBalance(agroTransaction.getIntBalance());
				distribution.setBalAmount(agroTransaction.getIntBalance() - finalAmtValue + getPaymentAmount());
				distribution.setAgroTransaction(agroTransaction);
				distribution.setTxnType(agroTransaction.getTxnType());

				// productDistributionService.buildAgroTransactionDistributionObject(distribution);
				distribution.setBranchId(getBranchId());
				distribution.setTenantId(getCurrentTenantId());
				distribution.setUserName(getUsername());
				if (harvestSeasonEnabled.equals("1")) {
					HarvestSeason harvestSeason = productDistributionService
							.findHarvestSeasonBySeasonCode(selectedSeason);
					distribution.setHarvestSeason(harvestSeason);
					distribution.setSeasonCode(distribution.getHarvestSeason().getCode());
				} else {
					distribution.setSeasonCode(getCurrentSeasonsCode());
				}
				
				productDistributionService.saveDistributionAndDistributionDetail(distribution);
				/*
				 * String receiptHtml =
				 * "<br/><a href=\"javascript:printReceipt(\\'" +
				 * receiptnumberSeq + "\\')\" >" + getText("printReceipt") +
				 * "</a>";
				 * 
				 * 
				 * setDistributionDescription(getText("receiptNumber") + " : " +
				 * receiptNumber + " " + getText("distributionSucess") +
				 * receiptHtml);
				 */

				paymentRupee = null;
				paymentPaise = null;
				// loadWarehouseAndProductData();
				// reset();
				// return INPUT;
			}

		} else {
			result = getText(result);
		}
		if (StringUtil.isEmpty(result)) {
			// Added for handling Form ReSubmit
			/*
			 * request.getSession().setAttribute( agentId + "_" +
			 * Distribution.PRODUCT_DISTRIBUTION_TO_FARMER + "_" +
			 * WebTransactionAction.IS_FORM_RESUBMIT, "No");
			 */

			String receiptHtml = "<a href=\"javascript:printReceipt(\\'" + receiptNumber + 
	                  "\\')\" class ='btn btn-default btnBorderRadius'onclick='printReceipt(\"" + receiptNumber + "\")'>" +
	                  getText("printReceipt") + "</button>";
	                 
	                setDistributionDescription("<h5>"
	                        + getText("receiptNumber") + " : " + receiptNumber+ "</h5>" + receiptHtml + "</br>");
			
			/*String receiptHtml = "<a href=\"javascript:printReceipt(\\'" + receiptNumber + 
	                  "\\')\" class ='btn btn-default btnBorderRadius'onclick='printReceipt(\"" + receiptNumber + "\")'>" +
	                  getText("printReceipt") + "</button>";
	                 
	                setDistributionDescription("<h5>"
	                        + getText("receiptNumber") + " : " + receiptNumber+ "</h5>" + receiptHtml + "</br>");*/

			/*
			 * setDistributionDescription(getText("distributionSucess") +
			 * "</br>" + getText("receiptNumber") + " : " + receiptNumber +
			 * "</br>");
			 */
			JSONObject json=new JSONObject();
			json.put("id", distribution.getId());
			json.put("des",getDistributionDescription());
			sendAjaxResponse(json);
		}
		response.getWriter().print(result);
	}

	public String createImage() throws Exception {
		String result = "redirect";
		ESESystem preferences = preferncesService.findPrefernceById("1");
		setDistImgAvil(preferences.getPreferences().get(ESESystem.ENABLE_DISTRIBUTION_IMAGE));
		if (distImgAvil.equals("1") && distId!=null && !StringUtil.isEmpty(distId)) {
			Distribution distribution = productDistributionService.findDistributionById(Long.parseLong(distId));
			Set<PMTImageDetails> distImageSet = new HashSet<PMTImageDetails>();
			File[] dynamicData = { getDistImg1(), getDistImg2() };
			for (File productData : dynamicData) {
				File value = productData;
				if (value != null) {
					PMTImageDetails distImage = new PMTImageDetails();
					byte[] farmPhotoData = FileUtil.getBinaryFileContent(value);
					if (farmPhotoData != null) {
						distImage.setPhoto(farmPhotoData);
					}
					distImageSet.add(distImage);
				}
			}
			distribution.setPmtImageDetail(distImageSet);
			productDistributionService.updateDistribution(distribution);
		}

		return result;

	}
	/**
	 * Sets the current season.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String populateProduct() throws Exception {

		String[] warehouseCode = selectedCooperative.split("#");
		WarehouseProduct warehouseProduct = null;

		Warehouse warehouse = productService.findWarehouseByCode(warehouseCode[1].trim());

		if (!StringUtil.isEmpty(selectedCategory)) {
			// String[] product = selectedCategory.split("-");
			List<Product> productList = productService.findProductBySubCategoryCode(selectedCategory.trim());
			JSONArray productArr = new JSONArray();

			if (!ObjectUtil.isListEmpty(productList)) {
				for (Product productObj : productList) {
					warehouseProduct = productDistributionService.findAvailableStock(warehouse.getId(),
							productObj.getId());
					if (!ObjectUtil.isEmpty(warehouseProduct)) {

						productArr.add(getJSONObject(productObj.getCode(), productObj.getName()));

					}
				}
			}
			sendAjaxResponse(productArr);
		}

		return null;

	}

	public String populateLalteerProduct() throws Exception {

		String[] warehouseCode = selectedCooperative.split("#");
		WarehouseProduct warehouseProduct = null;

		Warehouse warehouse = productService.findWarehouseByCode(warehouseCode[1].trim());

		if (!StringUtil.isEmpty(selectedCategory)) {
			// String[] product = selectedCategory.split("-");
			List<Product> productList = productService.findProductBySubCategoryCode(selectedCategory.trim());
			JSONArray productArr = new JSONArray();

			if (!ObjectUtil.isListEmpty(productList)) {
				for (Product productObj : productList) {
					warehouseProduct = productDistributionService.findAvailableStock(warehouse.getId(),
							productObj.getId());
					if (!ObjectUtil.isEmpty(warehouseProduct)) {

						productArr.add(getJSONObject(productObj.getId(), productObj.getName()));

					}
				}
			}
			sendAjaxResponse(productArr);
		}

		return null;

	}

	/**
	 * Sets the current season.
	 */
	private void setCurrentSeason() {

		ESESystem preference = preferncesService.findPrefernceById("2");
		String currentSeasonCode = preference.getPreferences().get(ESESystem.CURRENT_SEASON_CODE);
		if (!StringUtil.isEmpty(currentSeasonCode)) {
			Season currentSeason = productDistributionService.findSeasonBySeasonCode(currentSeasonCode);
			setSelectedSeason(String.valueOf(currentSeason.getId()));
		}

	}

	/**
	 * Populate product cooperative.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String populateProductCooperative() throws Exception {

		if (!StringUtil.isEmpty(selectedCooperative)) {
			String[] warehouse = selectedCooperative.split("#");
			List<Object[]> productList = productService.listProductNameUnitByWarehouse(warehouse[1].trim());
			JSONArray productArr = new JSONArray();
			if (!ObjectUtil.isListEmpty(productList)) {
				for (Object[] productObj : productList) {
					productArr.add(getJSONObject(productObj[0].toString(), productObj[1].toString()));
				}
			}
			sendAjaxResponse(productArr);
		}

		return null;
	}

	/**
	 * Populate warehouse category.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String populateWarehouseCategory() throws Exception {

		if (!StringUtil.isEmpty(selectedCooperative) && !StringUtil.isEmpty(seasonCode)) {
			String[] warehouse = selectedCooperative.split("#");

			/*
			 * List<SubCategory> categoryList = productService
			 * .findSubCategorybyWarehouseId(warehouse[1].trim());
			 */
			List<SubCategory> categoryList = productService.lisCategoryByWarehouseIdSeasonCode(warehouse[1].trim(),
					seasonCode);

			JSONArray categorysArr = new JSONArray();
			if (!ObjectUtil.isListEmpty(categoryList)) {
				for (SubCategory subCategory : categoryList) {
					categorysArr.add(getJSONObject(subCategory.getCode(), subCategory.getName()));
				}
			}
			sendAjaxResponse(categorysArr);
		}

		return null;
	}

	public String populateLalteerWarehouseCategory() throws Exception {

		if (!StringUtil.isEmpty(selectedCooperative)) {
			String[] warehouse = selectedCooperative.split("#");

			/*
			 * List<SubCategory> categoryList = productService
			 * .findSubCategorybyWarehouseId(warehouse[1].trim());
			 */
			List<SubCategory> categoryList = productService.lisCategoryByWarehouseId(warehouse[1].trim());

			JSONArray categorysArr = new JSONArray();
			if (!ObjectUtil.isListEmpty(categoryList)) {
				for (SubCategory subCategory : categoryList) {
					categorysArr.add(getJSONObject(subCategory.getCode(), subCategory.getName()));
				}
			}
			sendAjaxResponse(categorysArr);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}

	/**
	 * Populate product field staff.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String populateProductFieldStaff() throws Exception {

		WarehouseProduct warehouseProduct = null;
		if (!StringUtil.isEmpty(selectedAgent)) {
			// String[] agents = selectedAgent.split("-");
			// Agent agent =
			// agentService.findAgent(Long.valueOf(selectedAgent));
			List<Product> productList = productService.findProductBySubCategoryCode(selectedCategory.trim());
			Agent agent = agentService.findAgent(Long.valueOf(Long.valueOf(selectedAgent)));
			JSONArray productArr = new JSONArray();
			if (!ObjectUtil.isListEmpty(productList)) {
				for (Product productObj : productList) {
					warehouseProduct = productDistributionService.findAgentAvailableStock(Long.valueOf(selectedAgent),
							productObj.getId());
					if (!ObjectUtil.isEmpty(warehouseProduct)) {
						if (warehouseProduct != null && warehouseProduct.getStock() > 0) {
							productArr.add(getJSONObject(productObj.getCode(), productObj.getName()));
						}
					}
				}
			}
			sendAjaxResponse(productArr);
		}

		return null;

	}

	public String populateLalteerProductFieldStaff() throws Exception {

		WarehouseProduct warehouseProduct = null;
		if (!StringUtil.isEmpty(selectedAgent)) {
			// String[] agents = selectedAgent.split("-");
			// Agent agent =
			// agentService.findAgent(Long.valueOf(selectedAgent));
			List<Product> productList = productService.findProductBySubCategoryCode(selectedCategory.trim());
			Agent agent = agentService.findAgent(Long.valueOf(Long.valueOf(selectedAgent)));
			JSONArray productArr = new JSONArray();
			if (!ObjectUtil.isListEmpty(productList)) {
				for (Product productObj : productList) {
					warehouseProduct = productDistributionService.findAgentAvailableStock(Long.valueOf(selectedAgent),
							productObj.getId());
					if (!ObjectUtil.isEmpty(warehouseProduct)) {
						if (warehouseProduct != null && warehouseProduct.getStock() > 0) {
							productArr.add(getJSONObject(productObj.getId(), productObj.getName()));
						}
					}
				}
			}
			sendAjaxResponse(productArr);
		}

		return null;

	}

	/**
	 * Populate agent category.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String populateAgentCategory() throws Exception {

		if (!StringUtil.isEmpty(selectedAgent)) {
			// String[] warehouse = selectedAgent.split("-");
			// Agent agent =
			// agentService.findAgent(Long.valueOf(selectedAgent));
			List<SubCategory> categoryList = productService
					.findSubCategorybyAgentIdAndSeason(Long.valueOf(selectedAgent), selectedSeason);
			JSONArray categoryArr = new JSONArray();
			if (!ObjectUtil.isListEmpty(categoryList)) {
				for (SubCategory subCategory : categoryList) {
					categoryArr.add(getJSONObject(subCategory.getCode(), subCategory.getName()));
				}
			}

			sendAjaxResponse(categoryArr);
		}

		return null;

	}

	public String populateLalteerAgentCategory() throws Exception {

		if (!StringUtil.isEmpty(selectedAgent)) {
			// String[] warehouse = selectedAgent.split("-");
			// Agent agent =
			// agentService.findAgent(Long.valueOf(selectedAgent));
			List<SubCategory> categoryList = productService.findSubCategorybyAgentId(Long.valueOf(selectedAgent));
			JSONArray categoryArr = new JSONArray();
			if (!ObjectUtil.isListEmpty(categoryList)) {
				for (SubCategory subCategory : categoryList) {
					categoryArr.add(getJSONObject(subCategory.getCode(), subCategory.getName()));
				}
			}

			sendAjaxResponse(categoryArr);
		}

		return null;

	}

	/**
	 * Populate cost price.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String populateCostPrice() throws Exception {

		double result = 0d;

		if (!StringUtil.isEmpty(selectedProduct) && !StringUtil.isEmpty(selectedCooperative)
				&& !"0".equalsIgnoreCase(selectedProduct)) {
			String[] warehouseCode = selectedCooperative.split("#");

			Warehouse warehouse = productService.findWarehouseByCode(warehouseCode[1].trim());
			if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {
				product = productService.findProductByProductCode(selectedProduct);
			} else {
				product = productService.findProductById(Long.valueOf(selectedProduct));
			}
			/*
			 * if (!ObjectUtil.isEmpty(warehouse) &&
			 * !ObjectUtil.isEmpty(product)) { WarehouseProduct warehouseProduct
			 * = productService.findCostPriceForProduct(product.getId(),
			 * warehouse.getId());
			 */

			if (!ObjectUtil.isEmpty(product)) {
				result = Double.valueOf(product.getPrice());
			}
			// }

		}

		else if (!StringUtil.isEmpty(selectedProduct) && !StringUtil.isEmpty(selectedAgent)
				&& !"0".equalsIgnoreCase(selectedProduct)) {
			// String[] productCode = selectedProduct.split("=");
			// String[] agentCode = selectedAgent.split("-");
			// Agent profile =
			// productService.findProfileByProfileid(agentCode[1].trim());
			if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {
				product = productService.findProductByProductCode(selectedProduct);
			} else {
				product = productService.findProductById(Long.valueOf(selectedProduct));
			}
			WarehouseProduct warehouseProduct = productService.findCostPriceForAgent(product.getId(),
					Long.valueOf(selectedAgent));
			if (!ObjectUtil.isEmpty(warehouseProduct)) {
				result =Double.valueOf(product.getPrice());
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

	public String populateLalteerCostPrice() throws Exception {

		double result = 0d;

		if (!StringUtil.isEmpty(selectedProduct) && !StringUtil.isEmpty(selectedCooperative)
				&& !"0".equalsIgnoreCase(selectedProduct)) {
			String[] warehouseCode = selectedCooperative.split("#");

			Warehouse warehouse = productService.findWarehouseByCode(warehouseCode[1].trim());
			if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {
				product = productService.findProductByProductCode(selectedProduct);
			} else {
				product = productService.findProductById(Long.valueOf(selectedProduct));
			}
			if (!ObjectUtil.isEmpty(warehouse) && !ObjectUtil.isEmpty(product)) {
				WarehouseProduct warehouseProduct = productService.findCostPriceForProduct(product.getId(),
						warehouse.getId());

				if (!ObjectUtil.isEmpty(warehouseProduct)) {
					result = warehouseProduct.getCostPrice();
				}
			}

		}

		else if (!StringUtil.isEmpty(selectedProduct) && !StringUtil.isEmpty(selectedAgent)
				&& !"0".equalsIgnoreCase(selectedProduct)) {
			// String[] productCode = selectedProduct.split("=");
			// String[] agentCode = selectedAgent.split("-");
			// Agent profile =
			// productService.findProfileByProfileid(agentCode[1].trim());
			if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {
				product = productService.findProductByProductCode(selectedProduct);
			} else {
				product = productService.findProductById(Long.valueOf(selectedProduct));
			}
			WarehouseProduct warehouseProduct = productService.findCostPriceForAgent(product.getId(),
					Long.valueOf(selectedAgent));
			if (!ObjectUtil.isEmpty(warehouseProduct)) {
				result = warehouseProduct.getCostPrice();
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

	/**
	 * Gets the current season code.
	 * 
	 * @return the current season code
	 *//*
		 * private String getCurrentSeasonCode() {
		 * 
		 * ESESystem preference = preferncesService.findPrefernceById("2");
		 * return
		 * preference.getPreferences().get(ESESystem.CURRENT_SEASON_CODE); }
		 */

	/**
	 * Populate print html.
	 * 
	 * @return the string
	 */
	public String populatePrintHTML() {

		initializeDistributionPrintMap();
		if (!StringUtil.isEmpty(receiptNumber)) {
			Distribution distribution = productDistributionService
					.findDistributionFarmerByRecNoAndTxnType(receiptNumber, distTxnType);
			if (!StringUtil.isEmpty(distribution)) {
				buildTransactionPrintMap(distribution);
			}

			/*
			 * Distribution distribution = productDistributionService
			 * .findDistributionByRecNoAndTxnType(receiptNumber,
			 * Distribution.PRODUCT_DISTRIBUTION_TO_FARMER);
			 */
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
		this.distributionMap.put("freeDist", "0");
		this.distributionMap.put("oBal", 0.00);
		this.distributionMap.put("fBal", 0.00);
		this.distributionMap.put("isAgent", false);
		this.distributionMap.put("payMode", "");
		this.distributionMap.put("payAmt", "");
		this.distributionMap.put("credAmt", "");
		this.distributionMap.put("finPayBal", 0.00);
		this.distributionMap.put("amntTax", 0.00);
		this.distributionMap.put("enableBatchNo", "");
		this.distributionMap.put("warehouseCode", "");
		this.distributionMap.put("warehouseName", "");
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
			double distributionAmount = 0d;
			// DateFormat df = new SimpleDateFormat(RECEIPT_DATE_FORMAT);
			DateFormat df = new SimpleDateFormat(getGeneralDateFormat());
			// if (!ObjectUtil.isEmpty(distribution.getAgroTransaction())) {
			if (!StringUtil.isEmpty(distribution.getReceiptNumber())) {
				this.distributionMap.put("recNo", distribution.getReceiptNumber());
			}
			if (!StringUtil.isEmpty(distribution.getFarmerId())) {
				this.distributionMap.put("fId", distribution.getFarmerId());
			}
			if (!ObjectUtil.isEmpty(distribution.getAgroTransaction()) 
					&& !StringUtil.isEmpty(distribution.getAgroTransaction().getFarmerName())) {
				this.distributionMap.put("fName", distribution.getAgroTransaction().getFarmerName());
			}
			if (!ObjectUtil.isEmpty(distribution.getVillage())
					&& !StringUtil.isEmpty(distribution.getVillage().getName())) {
				this.distributionMap.put("village", distribution.getVillage().getName());
			}
			if (!ObjectUtil.isEmpty(distribution.getTxnTime())) {
				this.distributionMap.put("date", df.format(distribution.getTxnTime()));
			}
			if (!StringUtil.isEmpty(distribution.getAgentId())) {
				this.distributionMap.put("agentId", distribution.getAgentId());
				Agent agent = agentService.findAgentByProfileId(distribution.getAgentId());
				if (!ObjectUtil.isEmpty(agent))
					this.distributionMap.put("isAgent", true);
			}
			if (!StringUtil.isEmpty(distribution.getAgentName())) {
				this.distributionMap.put("agentName", distribution.getAgentName());
			}
			if (!StringUtil.isEmpty(distribution.getSamithiName())) {
				this.distributionMap.put("samithi", distribution.getSamithiName());
			}

			if (!StringUtil.isEmpty(distribution.getFreeDistribution())) {
				this.distributionMap.put("freeDist", distribution.getFreeDistribution());
			}
			/*
			 * if
			 * (!ObjectUtil.isEmpty(distribution.getAgroTransaction().getSamithi
			 * ()) &&
			 * !StringUtil.isEmpty(distribution.getAgroTransaction().getSamithi(
			 * ) .getName())) { this.distributionMap.put("samithi",
			 * distribution.getAgroTransaction() .getSamithi().getName()); }
			 */
			/*
			 * if (!StringUtil.isEmpty(distribution.getTax())) {
			 * distributionAmount = distribution.getTxnAmount() +
			 * ((distribution.getTxnAmount() * distribution.getTax()) / 100);
			 * this.distributionMap.put("distributionAmt",
			 * CurrencyUtil.thousandSeparator(distributionAmount)); // Added //
			 * the // tax // percentage // value // with // the // distribution
			 * // Amount.
			 * 
			 * } else { distributionAmount = distribution.getTxnAmount();
			 * this.distributionMap.put("distributionAmt",
			 * CurrencyUtil.thousandSeparator(distributionAmount)); }
			 */
			if (!StringUtil.isEmpty(distribution.getTotalAmount())) {
				this.distributionMap.put("distributionAmt",
						CurrencyUtil.thousandSeparator(distribution.getTotalAmount()));
			}

			if (!StringUtil.isEmpty(distribution.getFinalAmount())) {
				this.distributionMap.put("distributionFinAmt",
						CurrencyUtil.thousandSeparator(distribution.getFinalAmount()));
			}

			this.distributionMap.put("openingBal",
					CurrencyUtil.thousandSeparator(Math.abs(distribution.getIntBalance())));
			this.distributionMap.put("oBal", distribution.getIntBalance());
			// this.distributionMap.put("finalBal",
			// CurrencyUtil.thousandSeparator(Math
			// .abs(distribution.getFinalAmount())));
			if (distribution.isPaymentAvailable()) {
				/*
				 * AgroTransaction agroTransactionPaymentRef =
				 * productDistributionService
				 * .findAgroTransactionByRecNoProfTypeTxnDescAndDate(
				 * distribution .getAgroTransaction().getReceiptNo(),
				 * distribution .getAgroTransaction().getProfType(),
				 * Distribution.DISTRIBUTION_PAYMENT_AMOUNT, distribution
				 * .getAgroTransaction().getTxnTime()); if
				 * (!ObjectUtil.isEmpty(agroTransactionPaymentRef)) {
				 */
				if (!ObjectUtil.isEmpty(distribution.getPaymentMode())) {
					if (distribution.getPaymentMode().equals("CR")) {
						this.distributionMap.put("finalBal", CurrencyUtil
								.thousandSeparator(Math.abs((distributionAmount) - distribution.getIntBalance())));
					} else {
						this.distributionMap.put("finalBal", CurrencyUtil
								.thousandSeparator(Math.abs((distributionAmount) - distribution.getIntBalance())));
					}
					this.distributionMap.put("fBal", distribution.getBalAmount());
					// }
				}
			} else {
				this.distributionMap.put("finalBal",
						CurrencyUtil.thousandSeparator(Math.abs(distribution.getBalAmount())));
				this.distributionMap.put("fBal", distribution.getBalAmount());
			}
			if (!ObjectUtil.isEmpty(distribution.getPaymentMode())) {
				if (distribution.getPaymentMode().equals("CS")) {
					if ((distribution.getTxnAmount()
							+ ((distribution.getTxnAmount() * distribution.getTax()) / 100)) > distribution
									.getPaymentAmount()) {
						this.distributionMap.put("finPayBal",
								CurrencyUtil.getDecimalFormat(distribution.getTxnAmount()
										+ ((distribution.getTxnAmount() * distribution.getTax()) / 100)
										- distribution.getPaymentAmount(), "##.00"));
					} else if ((distribution.getTxnAmount()
							+ ((distribution.getTxnAmount() * distribution.getTax()) / 100)) < distribution
									.getPaymentAmount()) {
						this.distributionMap.put("finPayBal",
								CurrencyUtil.getDecimalFormat(distribution.getTxnAmount()
										+ ((distribution.getTxnAmount() * distribution.getTax()) / 100)
										- distribution.getPaymentAmount(), "##.00"));
					}

				}
				if (distribution.getPaymentMode().equals("CS")) {
					this.distributionMap.put("payMode", "Cash");
					Double outstandingBal = distribution.getFinalAmount() - distribution.getPaymentAmount();
					this.distributionMap.put("outstandingBal", CurrencyUtil.getDecimalFormat(outstandingBal, "##.000"));
				}
			}
			this.distributionMap.put("payAmt", CurrencyUtil.getDecimalFormat(distribution.getPaymentAmount(), "##.00"));
			if (!ObjectUtil.isEmpty(distribution.getPaymentMode())) {
				if (distribution.getPaymentMode().equals("CR")) {
					this.distributionMap.put("payMode", "Credit");
					Double outstandingBal = distribution.getFinalAmount() + (Math.abs(distribution.getIntBalance()));
					this.distributionMap.put("outstandingBal", outstandingBal);
				}
			}
			if (!ObjectUtil.isEmpty(distribution.getFinalAmount())) {
				this.distributionMap.put("credAmt",
						CurrencyUtil.getDecimalFormat(distribution.getFinalAmount(), "##.00"));
			}
			if (!ObjectUtil.isEmpty(distribution.getTax())) {
				this.distributionMap.put("amntTax", CurrencyUtil.getDecimalFormat(distribution.getTax(), "##.00"));
			}
			if (!StringUtil.isEmpty(distribution.getServicePointId())) {
				this.distributionMap.put("warehouseCode", distribution.getServicePointId());
			}
			if (!StringUtil.isEmpty(distribution.getServicePointName())) {
				this.distributionMap.put("warehouseName", distribution.getServicePointName());
			}
			String batchaNo = preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO);
			this.distributionMap.put("enableBatchNo", batchaNo);
			// }
			if (!ObjectUtil.isEmpty(distribution.getTxnType())) {
				if ("314".equalsIgnoreCase(distribution.getTxnType())) {
					this.distributionMap.put("isAgent", false);
				} else {
					this.distributionMap.put("isAgent", true);
				}
			}

			this.distributionMap.put("paymentAmout", CurrencyUtil.thousandSeparator(distribution.getPaymentAmount()));
			this.distributionMap.put("productMapList", productMapList);
			if (!ObjectUtil.isListEmpty(distribution.getDistributionDetails())) {
				for (DistributionDetail distributionDetail : distribution.getDistributionDetails()) {
					Map<String, Object> productMap = new LinkedHashMap<String, Object>();
					String productName = "";
					String categoryName = "";

					if (!ObjectUtil.isEmpty(distributionDetail.getProduct().getSubcategory()))
						categoryName = !StringUtil.isEmpty(distributionDetail.getProduct().getSubcategory().getName())
								? distributionDetail.getProduct().getSubcategory().getName() : "";
					if (!ObjectUtil.isEmpty(distributionDetail.getProduct()))
						productName = !StringUtil.isEmpty(distributionDetail.getProduct().getName())
								? distributionDetail.getProduct().getName() : "";

					productMap.put("category", categoryName);
					productMap.put("product", productName);
					productMap.put("batchNo", distributionDetail.getBatchNo());
					productMap.put("sellingPrice",
							CurrencyUtil.getDecimalFormat(distributionDetail.getCostPrice(), "##.0000"));
					productMap.put("quantity",
							CurrencyUtil.getDecimalFormat(distributionDetail.getQuantity(), "##.000"));
					// productMap.put("pricePerUnit", CurrencyUtil
					// .thousandSeparator(distributionDetail.getPricePerUnit()));
					productMap.put("amount", CurrencyUtil.thousandSeparator(distributionDetail.getSubTotal()));

					totalQuantity += distributionDetail.getQuantity();
					totalPricePerUnit += distributionDetail.getPricePerUnit();
					totalAmount += distributionDetail.getSubTotal();

					productMapList.add(productMap);
				}
			}
			Map<String, Object> totalMap = new LinkedHashMap<String, Object>();
			totalMap.put("totalQuantity", CurrencyUtil.getDecimalFormat(totalQuantity, "##.000"));
			totalMap.put("totalPricePerUnit", CurrencyUtil.thousandSeparator(totalPricePerUnit));
			totalMap.put("totalAmount", CurrencyUtil.thousandSeparator(totalAmount));
			this.distributionMap.put("totalInfo", totalMap);
		}
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
	 * Sets the selected warehouse.
	 * 
	 * @param selectedWarehouse
	 *            the new selected warehouse
	 */
	public void setSelectedWarehouse(String selectedWarehouse) {

		this.selectedWarehouse = selectedWarehouse;
	}

	/**
	 * Gets the selected warehouse.
	 * 
	 * @return the selected warehouse
	 */
	public String getSelectedWarehouse() {

		return selectedWarehouse;
	}

	/**
	 * Sets the selected village.
	 * 
	 * @param selectedVillage
	 *            the new selected village
	 */
	public void setSelectedVillage(String selectedVillage) {

		this.selectedVillage = selectedVillage;
	}

	/**
	 * Gets the selected village.
	 * 
	 * @return the selected village
	 */
	public String getSelectedVillage() {

		return selectedVillage;
	}

	/**
	 * Sets the account service.
	 * 
	 * @param accountService
	 *            the new account service
	 */
	public void setAccountService(IAccountService accountService) {

		this.accountService = accountService;
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
	 * Sets the selected farmer.
	 * 
	 * @param selectedFarmer
	 *            the new selected farmer
	 */
	public void setSelectedFarmer(String selectedFarmer) {

		this.selectedFarmer = selectedFarmer;
	}

	/**
	 * Gets the selected farmer.
	 * 
	 * @return the selected farmer
	 */
	public String getSelectedFarmer() {

		return selectedFarmer;
	}

	/**
	 * Sets the farmer name.
	 * 
	 * @param farmerName
	 *            the new farmer name
	 */
	public void setFarmerName(String farmerName) {

		this.farmerName = farmerName;
	}

	/**
	 * Gets the farmer name.
	 * 
	 * @return the farmer name
	 */
	public String getFarmerName() {

		return farmerName;
	}

	/**
	 * Sets the farmer id.
	 * 
	 * @param farmerId
	 *            the new farmer id
	 */
	public void setFarmerId(String farmerId) {

		this.farmerId = farmerId;
	}

	/**
	 * Gets the farmer id.
	 * 
	 * @return the farmer id
	 */
	public String getFarmerId() {

		return farmerId;
	}

	/**
	 * Sets the farmer address.
	 * 
	 * @param farmerAddress
	 *            the new farmer address
	 */
	public void setFarmerAddress(String farmerAddress) {

		this.farmerAddress = farmerAddress;
	}

	/**
	 * Gets the farmer address.
	 * 
	 * @return the farmer address
	 */
	public String getFarmerAddress() {

		return farmerAddress;
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
	 * Sets the selected category.
	 * 
	 * @param selectedCategory
	 *            the new selected category
	 */
	public void setSelectedCategory(String selectedCategory) {

		this.selectedCategory = selectedCategory;
	}

	/**
	 * Gets the selected category.
	 * 
	 * @return the selected category
	 */
	public String getSelectedCategory() {

		return selectedCategory;
	}

	/**
	 * Sets the category service.
	 * 
	 * @param categoryService
	 *            the new category service
	 */
	public void setCategoryService(ICategoryService categoryService) {

		this.categoryService = categoryService;
	}

	/**
	 * Sets the selected sub category.
	 * 
	 * @param selectedSubCategory
	 *            the new selected sub category
	 */
	public void setSelectedSubCategory(String selectedSubCategory) {

		this.selectedSubCategory = selectedSubCategory;
	}

	/**
	 * Gets the selected sub category.
	 * 
	 * @return the selected sub category
	 */
	public String getSelectedSubCategory() {

		return selectedSubCategory;
	}

	/**
	 * Sets the selected product.
	 * 
	 * @param selectedProduct
	 *            the new selected product
	 */
	public void setSelectedProduct(String selectedProduct) {

		this.selectedProduct = selectedProduct;
	}

	/**
	 * Gets the selected product.
	 * 
	 * @return the selected product
	 */
	public String getSelectedProduct() {

		return selectedProduct;
	}

	/**
	 * Sets the product service.
	 * 
	 * @param productService
	 *            the new product service
	 */
	public void setProductService(IProductService productService) {

		this.productService = productService;
	}

	/**
	 * Gets the stock.
	 * 
	 * @return the stock
	 */

	/**
	 * Sets the product price.
	 * 
	 * @param productPrice
	 *            the new product price
	 */
	public void setProductPrice(String productPrice) {

		this.productPrice = productPrice;
	}

	public double getStock() {

		return stock;
	}

	public void setStock(double stock) {

		this.stock = stock;
	}

	/**
	 * Gets the product price.
	 * 
	 * @return the product price
	 */
	public String getProductPrice() {

		return productPrice;
	}

	/**
	 * Sets the distribution product.
	 * 
	 * @param distributionProduct
	 *            the new distribution product
	 */
	public void setDistributionProduct(String distributionProduct) {

		this.distributionProduct = distributionProduct;
	}

	/**
	 * Gets the distribution product.
	 * 
	 * @return the distribution product
	 */
	public String getDistributionProduct() {

		return distributionProduct;
	}

	/**
	 * Sets the distribution product list.
	 * 
	 * @param distributionProductList
	 *            the new distribution product list
	 */
	public void setDistributionProductList(String distributionProductList) {

		this.distributionProductList = distributionProductList;
	}

	/**
	 * Gets the distribution product list.
	 * 
	 * @return the distribution product list
	 */
	public String getDistributionProductList() {

		return distributionProductList;
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
	 * Sets the village list.
	 * 
	 * @param villageList
	 *            the new village list
	 */
	public void setVillageList(List<String> villageList) {

		this.villageList = villageList;
	}

	/**
	 * Sets the category list.
	 * 
	 * @param categoryList
	 *            the new category list
	 */
	public void setCategoryList(List<String> categoryList) {

		this.categoryList = categoryList;
	}

	/**
	 * Gets the category list.
	 * 
	 * @return the category list
	 */
	public List<String> getCategoryList() {

		return categoryList;
	}

	/**
	 * Sets the sub categorys list.
	 * 
	 * @param subCategorysList
	 *            the new sub categorys list
	 */
	public void setSubCategorysList(List<String> subCategorysList) {

		this.subCategorysList = subCategorysList;
	}

	/**
	 * Gets the sub categorys list.
	 * 
	 * @return the sub categorys list
	 */
	public List<String> getSubCategorysList() {

		return subCategorysList;
	}

	/**
	 * Sets the distributiondetail.
	 * 
	 * @param distributiondetail
	 *            the new distributiondetail
	 */
	public void setDistributiondetail(DistributionDetail distributiondetail) {

		this.distributiondetail = distributiondetail;
	}

	/**
	 * Gets the distributiondetail.
	 * 
	 * @return the distributiondetail
	 */
	public DistributionDetail getDistributiondetail() {

		return distributiondetail;
	}

	/**
	 * Gets the agent.
	 * 
	 * @return the agent
	 */
	public Agent getAgent() {

		return agent;
	}

	/**
	 * Sets the agent.
	 * 
	 * @param agent
	 *            the new agent
	 */
	public void setAgent(Agent agent) {

		this.agent = agent;
	}

	/**
	 * Gets the farmer.
	 * 
	 * @return the farmer
	 */
	public Farmer getFarmer() {

		return farmer;
	}

	/**
	 * Sets the farmer.
	 * 
	 * @param farmer
	 *            the new farmer
	 */
	public void setFarmer(Farmer farmer) {

		this.farmer = farmer;
	}

	/**
	 * Gets the product.
	 * 
	 * @return the product
	 */
	public Product getProduct() {

		return product;
	}

	/**
	 * Sets the product.
	 * 
	 * @param product
	 *            the new product
	 */
	public void setProduct(Product product) {

		this.product = product;
	}

	/**
	 * Gets the distribution.
	 * 
	 * @return the distribution
	 */
	public Distribution getDistribution() {

		return distribution;
	}

	/**
	 * Sets the distribution.
	 * 
	 * @param distribution
	 *            the new distribution
	 */
	public void setDistribution(Distribution distribution) {

		this.distribution = distribution;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {

		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {

		this.id = id;
	}

	/**
	 * Sets the selected season.
	 * 
	 * @param selectedSeason
	 *            the new selected season
	 */
	public void setSelectedSeason(String selectedSeason) {

		this.selectedSeason = selectedSeason;
	}

	/**
	 * Gets the selected season.
	 * 
	 * @return the selected season
	 */
	public String getSelectedSeason() {

		return selectedSeason;
	}

	/**
	 * Sets the units.
	 * 
	 * @param units
	 *            the new units
	 */
	public void setUnits(String units) {

		this.units = units;
	}

	/**
	 * Gets the units.
	 * 
	 * @return the units
	 */
	public String getUnits() {

		return units;
	}

	/**
	 * Sets the start date.
	 * 
	 * @param startDate
	 *            the new start date
	 */
	public void setStartDate(String startDate) {

		this.startDate = startDate;
	}

	/**
	 * Gets the start date.
	 * 
	 * @return the start date
	 */
	public String getStartDate() {

		return startDate;
	}

	/**
	 * Sets the id generator.
	 * 
	 * @param idGenerator
	 *            the new id generator
	 */
	public void setIdGenerator(IUniqueIDGenerator idGenerator) {

		this.idGenerator = idGenerator;
	}

	/**
	 * Gets the id generator.
	 * 
	 * @return the id generator
	 */
	public IUniqueIDGenerator getIdGenerator() {

		return idGenerator;
	}

	/**
	 * Gets the current date.
	 * 
	 * @return the current date
	 */
	public String getCurrentDate() {

		Calendar currentDate = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat(getGeneralDateFormat().concat(" HH:mm:ss"));
		return df.format(currentDate.getTime());

	}

	/*
	 * public Map<String, String> getCoopearativeList() { List<WarehouseProduct>
	 * returnValue = locationService.listOfWareHouseProductCooperatives(); for
	 * (WarehouseProduct warehouseProduct : returnValue) {
	 * warehouseList.add(warehouseProduct.getWarehouse().getName() + " - " +
	 * warehouseProduct.getWarehouse().getCode()); } return warehouseList; }
	 */

	/**
	 * Gets the coopearative list.
	 * 
	 * @return the coopearative list
	 */
	public Map<String, String> getCoopearativeList() {

		Map<String, String> returnMap = new LinkedHashMap<String, String>();
		List<Warehouse> coopearativeList = locationService.listOfCooperatives();
		if (!ObjectUtil.isListEmpty(coopearativeList)) {

			for (Warehouse warehouseProduct : coopearativeList) {
				/*
				 * returnMap.put(warehouseProduct.getName() + " - " +
				 * warehouseProduct.getCode(), warehouseProduct.getName() +
				 * " - " + warehouseProduct.getCode());
				 */
				returnMap.put(warehouseProduct.getName() + " # " + warehouseProduct.getCode(),
						warehouseProduct.getName());
			}
		}
		return returnMap;
	}

	public Map<String, String> getHarvestSeasonList() {

		Map<String, String> returnMap = new LinkedHashMap<String, String>();
		List<HarvestSeason> harvestSeasonList = productDistributionService.listHarvestSeason();
		if (!ObjectUtil.isListEmpty(harvestSeasonList)) {

			for (HarvestSeason harvestSeason : harvestSeasonList) {
				/*
				 * returnMap.put(harvestSeason.getCode(),
				 * harvestSeason.getName() + " - " + harvestSeason.getCode());
				 */
				returnMap.put(harvestSeason.getCode(), harvestSeason.getName());
			}
		}
		return returnMap;
	}

	/**
	 * Gets the agent lists.
	 * 
	 * @return the agent lists
	 */
	public Map<Long, String> getAgentLists() {

		Map<Long, String> returnMap = new LinkedHashMap<Long, String>();
		List<Agent> agentLists = (List<Agent>) agentService.listAgentByAgentType(AgentType.FIELD_STAFF);
		if (!ObjectUtil.isListEmpty(agentLists)) {
			returnMap = agentLists.stream().collect(Collectors.toMap(Agent::getId, ag -> String.join(" ",
					ag.getPersonalInfo().getFirstName(), ag.getPersonalInfo().getLastName(), ag.getProfileId())));
		}
		return returnMap;
	}

	/**
	 * Sets the selected unit.
	 * 
	 * @param selectedUnit
	 *            the new selected unit
	 */
	public void setSelectedUnit(String selectedUnit) {

		this.selectedUnit = selectedUnit;
	}

	/**
	 * Gets the selected unit.
	 * 
	 * @return the selected unit
	 */
	public String getSelectedUnit() {

		return selectedUnit;
	}

	/**
	 * Sets the receipt number.
	 * 
	 * @param receiptNumber
	 *            the new receipt number
	 */
	public void setReceiptNumber(String receiptNumber) {

		this.receiptNumber = receiptNumber;
	}

	/**
	 * Gets the receipt number.
	 * 
	 * @return the receipt number
	 */
	public String getReceiptNumber() {

		return receiptNumber;
	}

	/**
	 * Sets the product total string.
	 * 
	 * @param productTotalString
	 *            the new product total string
	 */
	public void setProductTotalString(String productTotalString) {

		this.productTotalString = productTotalString;
	}

	/**
	 * Gets the product total string.
	 * 
	 * @return the product total string
	 */
	public String getProductTotalString() {

		return productTotalString;
	}

	/**
	 * Sets the distribution description.
	 * 
	 * @param distributionDescription
	 *            the new distribution description
	 */
	public void setDistributionDescription(String distributionDescription) {

		this.distributionDescription = distributionDescription;
	}

	/**
	 * Gets the distribution description.
	 * 
	 * @return the distribution description
	 */
	public String getDistributionDescription() {

		return distributionDescription;
	}

	/**
	 * Sets the reference no.
	 * 
	 * @param referenceNo
	 *            the new reference no
	 */
	public void setReferenceNo(String referenceNo) {

		this.referenceNo = referenceNo;
	}

	/**
	 * Gets the reference no.
	 * 
	 * @return the reference no
	 */
	public String getReferenceNo() {

		return referenceNo;
	}

	/**
	 * Sets the serial no.
	 * 
	 * @param serialNo
	 *            the new serial no
	 */
	public void setSerialNo(String serialNo) {

		this.serialNo = serialNo;
	}

	/**
	 * Gets the serial no.
	 * 
	 * @return the serial no
	 */
	public String getSerialNo() {

		return serialNo;
	}

	/**
	 * Sets the f id.
	 * 
	 * @param fId
	 *            the new f id
	 */
	public void setfId(String fId) {

		this.fId = fId;
	}

	/**
	 * Gets the f id.
	 * 
	 * @return the f id
	 */
	public String getfId() {

		return fId;
	}

	/**
	 * Sets the products name unit map.
	 * 
	 * @param productsNameUnitMap
	 *            the products name unit map
	 */
	public void setProductsNameUnitMap(Map<String, String> productsNameUnitMap) {

		this.productsNameUnitMap = productsNameUnitMap;
	}

	/**
	 * Gets the payment rupee.
	 * 
	 * @return the payment rupee
	 */
	public String getPaymentRupee() {

		return paymentRupee;
	}

	/**
	 * Sets the payment rupee.
	 * 
	 * @param paymentRupee
	 *            the new payment rupee
	 */
	public void setPaymentRupee(String paymentRupee) {

		this.paymentRupee = paymentRupee;
	}

	/**
	 * Gets the payment paise.
	 * 
	 * @return the payment paise
	 */
	public String getPaymentPaise() {

		return paymentPaise;
	}

	/**
	 * Sets the payment paise.
	 * 
	 * @param paymentPaise
	 *            the new payment paise
	 */
	public void setPaymentPaise(String paymentPaise) {

		this.paymentPaise = paymentPaise;
	}

	/**
	 * Gets the payment amount.
	 * 
	 * @return the payment amount
	 */
	private double getPaymentAmount() {

		double paymentAmount = 0d;
		if (!StringUtil.isEmpty(getPaymentRupee()) || !StringUtil.isEmpty(getPaymentPaise())) {
			try {
				paymentAmount = Double.valueOf(getPaymentRupee() + "." + getPaymentPaise());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return paymentAmount;
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
	 * Sets the registered farmer.
	 * 
	 * @param registeredFarmer
	 *            the new registered farmer
	 */
	public void setRegisteredFarmer(boolean registeredFarmer) {

		this.registeredFarmer = registeredFarmer;
	}

	/**
	 * Checks if is registered farmer.
	 * 
	 * @return true, if is registered farmer
	 */
	public boolean isRegisteredFarmer() {

		return registeredFarmer;
	}

	/**
	 * Sets the un register farmer name.
	 * 
	 * @param unRegisterFarmerName
	 *            the new un register farmer name
	 */
	public void setUnRegisterFarmerName(String unRegisterFarmerName) {

		this.unRegisterFarmerName = unRegisterFarmerName;
	}

	/**
	 * Gets the un register farmer name.
	 * 
	 * @return the un register farmer name
	 */
	public String getUnRegisterFarmerName() {

		return unRegisterFarmerName;
	}

	/**
	 * Sets the mobile no.
	 * 
	 * @param mobileNo
	 *            the new mobile no
	 */
	public void setMobileNo(String mobileNo) {

		this.mobileNo = mobileNo;
	}

	/**
	 * Gets the mobile no.
	 * 
	 * @return the mobile no
	 */
	public String getMobileNo() {

		return mobileNo;
	}

	/**
	 * Sets the coopearative list.
	 * 
	 * @param coopearativeList
	 *            the coopearative list
	 */
	public void setCoopearativeList(Map<String, String> coopearativeList) {

		this.coopearativeList = coopearativeList;
	}

	/**
	 * Sets the agent lists.
	 * 
	 * @param agentLists
	 *            the agent lists
	 */
	public void setAgentLists(Map<String, String> agentLists) {

		this.agentLists = agentLists;
	}

	/**
	 * Gets the selected agent.
	 * 
	 * @return the selected agent
	 */
	public String getSelectedAgent() {

		return selectedAgent;
	}

	/**
	 * Sets the selected agent.
	 * 
	 * @param selectedAgent
	 *            the new selected agent
	 */
	public void setSelectedAgent(String selectedAgent) {

		this.selectedAgent = selectedAgent;
	}

	/**
	 * Gets the selected cooperative.
	 * 
	 * @return the selected cooperative
	 */
	public String getSelectedCooperative() {

		return selectedCooperative;
	}

	/**
	 * Sets the selected cooperative.
	 * 
	 * @param selectedCooperative
	 *            the new selected cooperative
	 */
	public void setSelectedCooperative(String selectedCooperative) {

		this.selectedCooperative = selectedCooperative;
	}

	/*
	 * public boolean isStockType() { return stockType; } public void
	 * setStockType(boolean stockType) { this.stockType = stockType; }
	 */

	/**
	 * Checks if is product stock.
	 * 
	 * @return true, if is product stock
	 */
	public boolean isProductStock() {

		return productStock;
	}

	/**
	 * Sets the product stock.
	 * 
	 * @param productStock
	 *            the new product stock
	 */
	public void setProductStock(boolean productStock) {

		this.productStock = productStock;
	}

	/**
	 * Gets the tax value.
	 * 
	 * @return the tax value
	 */
	public double getTaxValue() {

		return taxValue;
	}

	/**
	 * Sets the tax value.
	 * 
	 * @param taxValue
	 *            the new tax value
	 */
	public void setTaxValue(double taxValue) {

		this.taxValue = taxValue;
	}

	/**
	 * Gets the cash type.
	 * 
	 * @return the cash type
	 */
	public Map<Integer, String> getCashType() {

		return cashType;
	}

	/**
	 * Sets the cash type.
	 * 
	 * @param cashType
	 *            the cash type
	 */
	public void setCashType(Map<Integer, String> cashType) {

		this.cashType = cashType;
	}

	/**
	 * Gets the final amt value.
	 * 
	 * @return the final amt value
	 */
	public double getFinalAmtValue() {

		return finalAmtValue;
	}

	/**
	 * Sets the final amt value.
	 * 
	 * @param finalAmtValue
	 *            the new final amt value
	 */
	public void setFinalAmtValue(double finalAmtValue) {

		this.finalAmtValue = finalAmtValue;
	}

	/**
	 * Populate farmer acc balance.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void populateFarmerAccBalance() throws Exception {

		AgroTransaction agroTransaction = new AgroTransaction();
		String cashAndCredit = "";
		ESEAccount farmerAccount = null;
		if (!StringUtil.isEmpty(selectedFarmerValue)) {
			Farmer farmer = farmerDAO.findFarmerByFarmerId(selectedFarmerValue.split("-")[1].trim());
			Season season = productDistributionService.findCurrentSeason();
			if (!ObjectUtil.isEmpty(farmer) && !ObjectUtil.isEmpty(season)) {
				farmerAccount = farmerDAO.findAccountBySeassonProcurmentProductFarmer(season.getId(), 0L,
						farmer.getId());
				if (!StringUtil.isEmpty(farmerAccount)) {
					cashCreditValue = (farmerAccount.getCashBalance() + "," + farmerAccount.getCreditBalance());
					cashAndCredit = cashCreditValue;
				} else {
					cashCreditValue = "";
				}
				cashAndCredit = cashCreditValue;
			}
		}
		response.getWriter().print(cashAndCredit);

	}

	/**
	 * Gets the selected farmer value.
	 * 
	 * @return the selected farmer value
	 */
	public String getSelectedFarmerValue() {

		return selectedFarmerValue;
	}

	/**
	 * Sets the selected farmer value.
	 * 
	 * @param selectedFarmerValue
	 *            the new selected farmer value
	 */
	public void setSelectedFarmerValue(String selectedFarmerValue) {

		this.selectedFarmerValue = selectedFarmerValue;
	}

	/**
	 * Gets the farmer dao.
	 * 
	 * @return the farmer dao
	 */
	public IFarmerDAO getFarmerDAO() {

		return farmerDAO;
	}

	/**
	 * Sets the farmer dao.
	 * 
	 * @param farmerDAO
	 *            the new farmer dao
	 */
	public void setFarmerDAO(IFarmerDAO farmerDAO) {

		this.farmerDAO = farmerDAO;
	}

	/**
	 * Gets the cash credit value.
	 * 
	 * @return the cash credit value
	 */
	public String getCashCreditValue() {

		return cashCreditValue;
	}

	/**
	 * Sets the cash credit value.
	 * 
	 * @param cashCreditValue
	 *            the new cash credit value
	 */
	public void setCashCreditValue(String cashCreditValue) {

		this.cashCreditValue = cashCreditValue;
	}

	/**
	 * Gets the check boxs.
	 * 
	 * @return the check boxs
	 */
	public Map<Character, String> getCheckBoxs() {

		return checkBoxs;
	}

	/**
	 * Sets the check boxs.
	 * 
	 * @param checkBoxs
	 *            the check boxs
	 */
	public void setCheckBoxs(Map<Character, String> checkBoxs) {

		this.checkBoxs = checkBoxs;
	}

	/**
	 * Sets the payment amount.
	 * 
	 * @param paymentAmount
	 *            the new payment amount
	 */
	public void setPaymentAmount(double paymentAmount) {

		this.paymentAmount = paymentAmount;
	}

	/**
	 * Gets the payment mode.
	 * 
	 * @return the payment mode
	 */
	public String getPaymentMode() {

		return paymentMode;
	}

	/**
	 * Sets the payment mode.
	 * 
	 * @param paymentMode
	 *            the new payment mode
	 */
	public void setPaymentMode(String paymentMode) {

		this.paymentMode = paymentMode;
	}

	/**
	 * Gets the credit balance.
	 * 
	 * @param accountBalance
	 *            the account balance
	 * @param trxnBalance
	 *            the trxn balance
	 * @param isTxnType
	 *            the is txn type
	 * @return the credit balance
	 */
	public double getCreditBalance(double accountBalance, double trxnBalance, boolean isTxnType) {

		if (isTxnType) {
			return (accountBalance + trxnBalance);
		} else {
			return (accountBalance - trxnBalance);
		}
	}

	/**
	 * Gets the cash balance.
	 * 
	 * @param accountBalance
	 *            the account balance
	 * @param trxnBalance
	 *            the trxn balance
	 * @param isTxnType
	 *            the is txn type
	 * @return the cash balance
	 */
	public double getCashBalance(double accountBalance, double trxnBalance, boolean isTxnType) {

		if (isTxnType) {
			return (accountBalance + trxnBalance);
		} else {
			return (accountBalance - trxnBalance);
		}
	}

	/**
	 * Gets the free distribution.
	 * 
	 * @return the free distribution
	 */
	public String getFreeDistribution() {

		return freeDistribution;
	}

	/**
	 * Sets the free distribution.
	 * 
	 * @param freeDistribution
	 *            the new free distribution
	 */
	public void setFreeDistribution(String freeDistribution) {

		this.freeDistribution = freeDistribution;
	}

	public Map<String, String> getFarmersList() {
		return farmersList;
	}

	public void setFarmersList(Map<String, String> farmersList) {
		this.farmersList = farmersList;
	}

	public String getSeasonName() {
		return seasonName;
	}

	public void setSeasonName(String seasonName) {
		this.seasonName = seasonName;
	}

	public Map<String, String> getCities() {
		Map<String, String> citiesMap = new LinkedHashMap<>();
		List<Object[]> cityCodeAndName = locationService.listCityCodeAndName();
		cityCodeAndName.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			citiesMap.put(objArr[0].toString(), objArr[0].toString() + "-" + objArr[1].toString());
			// citiesMap.put(objArr[0].toString(),objArr[1].toString());
		});
		return citiesMap;
	}

	public void populateVillageByCity() throws Exception {
		List<Object[]> villageList = new LinkedList<>();
		if (!selectedCity.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedCity))
				&& !selectedCity.equalsIgnoreCase("0")) {
			villageList = locationService.listVillageCodeAndNameByCityCode(selectedCity);
		}
		JSONArray villageArr = new JSONArray();
		if (!ObjectUtil.isEmpty(villageList)) {
			/*
			 * for (Village village : villages) {
			 * villageArr.add(getJSONObject(village.getId(),
			 * village.getName())); }
			 */
			villageList.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
				Object[] objArr = (Object[]) obj;
				villageArr.add(getJSONObject(objArr[0].toString(), objArr[0].toString() + "-" + objArr[1].toString()));
				// villageArr.add(getJSONObject(objArr[0].toString(),objArr[1].toString()));
			});
		}
		sendAjaxResponse(villageArr);

	}

	public String getSelectedCity() {
		return selectedCity;
	}

	public void setSelectedCity(String selectedCity) {
		this.selectedCity = selectedCity;
	}

	public void setHarvestSeasonList(Map<String, String> harvestSeasonList) {

		this.harvestSeasonList = harvestSeasonList;
	}

	public String getHarvestSeasonEnabled() {

		return harvestSeasonEnabled;
	}

	public void setHarvestSeasonEnabled(String harvestSeasonEnabled) {

		this.harvestSeasonEnabled = harvestSeasonEnabled;
	}

	public String getIdentityForGrid() {
		return identityForGrid;
	}

	public void setIdentityForGrid(String identityForGrid) {
		this.identityForGrid = identityForGrid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFarmerType() {
		return farmerType;
	}

	public void setFarmerType(String farmerType) {
		this.farmerType = farmerType;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getCash() {
		return cash;
	}

	public void setCash(String cash) {
		this.cash = cash;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getSeasonCodeAndName() {
		return seasonCodeAndName;
	}

	public void setSeasonCodeAndName(String seasonCodeAndName) {
		this.seasonCodeAndName = seasonCodeAndName;
	}

	public double getTaxPercent() {
		return taxPercent;
	}

	public void setTaxPercent(double taxPercent) {
		this.taxPercent = taxPercent;
	}

	public double getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(double finalAmount) {
		this.finalAmount = finalAmount;
	}

	public double getPayemnt() {
		return payemnt;
	}

	public void setPayemnt(double payemnt) {
		this.payemnt = payemnt;
	}

	public ILocationService getLocationService() {
		return locationService;
	}

	public IProductService getProductService() {
		return productService;
	}

	public String getProductDistributionTypes() {
		return productDistributionTypes;
	}

	public void setProductDistributionTypes(String productDistributionTypes) {
		this.productDistributionTypes = productDistributionTypes;
	}

	public List<DistributionDetail> getDistributionDetailList() {
		return distributionDetailList;
	}

	public void setDistributionDetailList(List<DistributionDetail> distributionDetailList) {
		this.distributionDetailList = distributionDetailList;
	}

	public String getQuantiy() {
		return quantiy;
	}

	public void setQuantiy(String quantiy) {
		this.quantiy = quantiy;
	}

	public String getDistributionDetails() {
		return distributionDetails;
	}

	public void setDistributionDetails(String distributionDetails) {
		this.distributionDetails = distributionDetails;
	}

	public DateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	/*
	 * public String getApproved() { return approved; }
	 * 
	 * public void setApproved(String approved) { this.approved = approved; }
	 */
	public String getSeasonCode() {
		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
	}

	public String populateUnit() throws Exception {

		String result = "";
		StringBuffer sb = new StringBuffer();
		if (!StringUtil.isEmpty(selectedCategory) && !selectedCategory.equalsIgnoreCase("0")) {
			SubCategory subCategory = categoryService.findSubCategoryByCode(selectedCategory);
			Product product = productDistributionService.findProductBySubCategoryId(subCategory.getId());
			// List<Product> product =
			// productDistributionService.findProductBySubCategoryId(subCategory.);

			// Agent agent =
			// agentService.findAgent(Long.parseLong(selectedAgent));
			// for (Product productObj : product) {
			if (!ObjectUtil.isEmpty(product)) {
				sb.append(product.getUnit());
			}
			// }

			result = sb.length() > 0 ? sb.substring(0, sb.length() - 0) : "Unit";
		}
		PrintWriter out = null;
		// result=getText("distributionStock");
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

	public void populatePopulateProductUnit() throws Exception {

		String result = "";
		String costPrice = "0.0";
		Product product;
		JSONArray productArr = new JSONArray();
		Product prod = new Product();
		if (!StringUtil.isEmpty(selectedProduct)) {
			if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {
				prod = productService.findProductUnitByProductCode(selectedProduct);
			} else {
				prod = productService.findProductUnitByProductId(Long.valueOf(selectedProduct));
			}

			if (!ObjectUtil.isEmpty(prod)) {
				costPrice = (!StringUtil.isEmpty(prod.getPrice()) ? prod.getPrice() : "0.0");
				result = prod.getUnit();
			}

			if (!ObjectUtil.isEmpty(prod) && prod.getType() != null) {
				productAvailableUnit = String.valueOf(prod.getType().getName());
				// result = productAvailableUnit;
			}

		}
		productArr.add(getJSONObject("unit", result));
		productArr.add(getJSONObject("costPrice", costPrice));
		sendAjaxResponse(productArr);
	}

	public String getEnableBatchNo() {
		return enableBatchNo;
	}

	public void setEnableBatchNo(String enableBatchNo) {
		this.enableBatchNo = enableBatchNo;
	}

	public Map<Long, String> getBatchNoList() {
		return batchNoList;
	}

	public void setBatchNoList(Map<Long, String> batchNoList) {
		this.batchNoList = batchNoList;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getProductAvailableUnit() {
		return productAvailableUnit;
	}

	public void setProductAvailableUnit(String productAvailableUnit) {
		this.productAvailableUnit = productAvailableUnit;
	}

	public String getDistTxnType() {
		return distTxnType;
	}

	public void setDistTxnType(String distTxnType) {
		this.distTxnType = distTxnType;
	}

	public String getIsFreeDistribution() {
		return isFreeDistribution;
	}

	public void setIsFreeDistribution(String isFreeDistribution) {
		this.isFreeDistribution = isFreeDistribution;
	}

	public String getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(String selectedDate) {
		this.selectedDate = selectedDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getChangeStockUpdate() {
		return changeStockUpdate;
	}

	public void setChangeStockUpdate(String changeStock) {
		this.changeStockUpdate = changeStock;
	}

	public void setListWarehouse(Map<Long, String> listWarehouse) {
		this.listWarehouse = listWarehouse;
	}

	public Map<String, String> getListSeason() {
		List<Object[]> seasonList = farmerService.listSeasonCodeAndName();
		if (!ObjectUtil.isListEmpty(seasonList)) {
			for (Object[] obj : seasonList) {
				listSeason.put(String.valueOf(obj[0]), obj[1] + " - " + obj[0]);
			}
		}
		return listSeason;
	}

	public void setListSeason(Map<String, String> listSeason) {
		this.listSeason = listSeason;
	}

	public List<String> getListReceiptNo() {
		return listReceiptNo;
	}

	public void setListReceiptNo(List<String> listReceiptNo) {
		this.listReceiptNo = listReceiptNo;
	}

	public List<String> getListProduct() {
		return listProduct;
	}

	public void setListProduct(List<String> listProduct) {
		this.listProduct = listProduct;
	}

	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public String getReceiptNum() {
		return receiptNum;
	}

	public void setReceiptNum(String receiptNum) {
		this.receiptNum = receiptNum;
	}

	public Map<String, String> getListVillage() {
		List<Village> villages = locationService.listVillage();
		if (!ObjectUtil.isEmpty(villages)) {
			for (Village v : villages) {
				listVillage.put(v.getCode(), v.getName());
			}
		}
		return listVillage;
	}

	public void setListVillage(Map<String, String> listVillage) {
		this.listVillage = listVillage;
	}

	public List<String> getListFarmer() {
		return listFarmer;
	}

	public void setListFarmer(List<String> listFarmer) {
		this.listFarmer = listFarmer;
	}

	public void populateSagiFarmer() {
		JSONArray farmerArr = new JSONArray();
		List<Object[]> farmers = farmerService.listFarmerCodeIdNameByVillageCode(villageId);
		if (!ObjectUtil.isEmpty(farmers)) {
			for (Object[] obj : farmers) {
				farmerArr.add(getJSONObject(obj[0].toString(), obj[2].toString() + " " + obj[3].toString()));
			}
		}
		sendAjaxResponse(farmerArr);
	}

	public String getVillageId() {
		return villageId;
	}

	public void setVillageId(String villageId) {
		this.villageId = villageId;
	}
	/*
	 * warehouseProduct = productDistributionDAO
	 * .findAvailableStockByWarehouseIdSelectedProductSeasonBatchNo(warehouse.
	 * getId(), ptrDetails.getLotNo());
	 */

	public String getSelectedWarehouseId() {
		return selectedWarehouseId;
	}

	public void setSelectedWarehouseId(String selectedWarehouseId) {
		this.selectedWarehouseId = selectedWarehouseId;
	}

	public String getSelectedReceiptNo() {
		return selectedReceiptNo;
	}

	public void setSelectedReceiptNo(String selectedReceiptNo) {
		this.selectedReceiptNo = selectedReceiptNo;
	}

	public String getRecDate() {
		return recDate;
	}

	public void setRecDate(String recDate) {
		this.recDate = recDate;
	}

	public String getDistImgAvil() {
		return distImgAvil;
	}

	public void setDistImgAvil(String distImgAvil) {
		this.distImgAvil = distImgAvil;
	}

	public File getDistImg1() {
		return distImg1;
	}

	public void setDistImg1(File distImg1) {
		this.distImg1 = distImg1;
	}

	public File getDistImg2() {
		return distImg2;
	}

	public void setDistImg2(File distImg2) {
		this.distImg2 = distImg2;
	}

	public String getDistId() {
		return distId;
	}

	public void setDistId(String distId) {
		this.distId = distId;
	}

	public String getDistributionimage() {
		return distributionimage;
	}

	public void setDistributionimage(String distributionimage) {
		this.distributionimage = distributionimage;
	}

	public OutputStream getImageStream() {
		return imageStream;
	}

	public void setImageStream(OutputStream imageStream) {
		this.imageStream = imageStream;
	}
	
	public void populateImageId()  {
		String distImgId = "";
		String tempVal="";
		List<PMTImageDetails> distImageList = productDistributionService.findPMTImageDetailByDistributionId(Long.valueOf(id));
		if (!ObjectUtil.isListEmpty(distImageList)) {
			for (PMTImageDetails distImage : distImageList) {
				if (distImage.getPhoto() != null && distImage.getPhoto().length > 0) {
					distImgId += String.valueOf(distImage.getId())  + ",";
				}
				
			}
			 tempVal = distImgId.substring(0, distImgId.length() - 1);
	}
		sendAjaxResponse(String.valueOf(tempVal));
	}
	
	public String populatedetailImage() {
		try {
			setId(id);
			PMTImageDetails imageDetail = productDistributionService.findPMTImageDetailById(Long.valueOf(id));
			byte[] image = imageDetail.getPhoto();
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
	
	public String getImageArr() {
		return imageArr;
	}

	public void setImageArr(String imageArr) {
		this.imageArr = imageArr;
	}

	public String getStockTypeText() {
		return stockTypeText;
	}

	public void setStockTypeText(String stockTypeText) {
		this.stockTypeText = stockTypeText;
	}

	public Map<String, String> getWarehouseMap() {
		return warehouseMap;
	}

	public void setWarehouseMap(Map<String, String> warehouseMap) {
		this.warehouseMap = warehouseMap;
	}
	
}
