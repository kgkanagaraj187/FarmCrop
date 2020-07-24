package com.ese.view.service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sourcetrace.eses.dao.IFarmerDAO;
import com.sourcetrace.eses.dao.IReportDAO;
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
import com.sourcetrace.eses.order.entity.txn.Distribution;
import com.sourcetrace.eses.order.entity.txn.DistributionDetail;
import com.sourcetrace.eses.order.entity.txn.PMTImageDetails;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.order.entity.txn.ProcurementDetail;
import com.sourcetrace.eses.order.entity.txn.ProductReturn;
import com.sourcetrace.eses.order.entity.txn.ProductReturnDetail;
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
import com.sourcetrace.eses.util.profile.Product;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.view.WebTransactionAction;

public class ProductReturnFromFarmerAction extends WebTransactionAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4177884321611083524L;

	private IProductDistributionService productDistributionService;
	private IFarmerService farmerService;
	private ILocationService locationService;
	@Autowired
	private ICategoryService categoryService;
	private IProductService productService;
	private IAccountService accountService;
	private IUniqueIDGenerator idGenerator;
	private Agent agent;
	private Farmer farmer;
	Object[] distributionObj = null;
	private Product product;

	private ProductReturn productReturn;

	private ProductReturnDetail productReturnDetail;

	private String id;

	private String selectedWarehouse;

	private String selectedVillage;

	private String selectedFarmer;

	private String farmerName = "";

	private String farmerId = "";

	private String farmerAddress = "";

	private String selectedCategory;

	private String selectedSubCategory;

	private String selectedProduct;

	private String selectedSeason;

	private String selectedUnit;

	private double stock;

	private String distributionProduct;

	private String productPrice;

	private String units;

	private String distributionProductList;

	private String startDate;

	private String receiptNumber;

	private String distributionDescription;

	private String productTotalString;

	private String referenceNo;

	private String serialNo;

	private String fId;

	private String paymentRupee;
	private String paymentPaise;

	private String unRegisterFarmerName;

	private String mobileNo;

	private String selectedAgent;

	private boolean registeredFarmer;

	private List<String> warehouseList = new ArrayList<String>();

	private List<String> villageList = new ArrayList<String>();

	private Map<String, String> farmersList = new LinkedHashMap<>();

	private List<String> categoryList = new ArrayList<String>();

	private List<String> subCategorysList = new ArrayList<String>();

	private Map<String, String> productsNameUnitMap = new LinkedHashMap<String, String>();

	private Map<String, Object> productReturnMap = new LinkedHashMap<String, Object>();

	Map<String, String> coopearativeList = new LinkedHashMap<String, String>();

	Map<String, String> agentLists = new LinkedHashMap<String, String>();

	private String selectedCooperative;

	private double taxValue;

	private double finalAmtValue;

	Map<Integer, String> cashType = new LinkedHashMap<Integer, String>();

	private String paymentMode;

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

	private String distId;
	
	private File prodReturnImg1;
	private File prodReturnImg2;
	private String prodReturnImgAvil;
	
	/**
	 * Creates the.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String list() {
		return LIST;
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
					request.setAttribute(HEADING, getText("create.page"));
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

			return SUCCESS;
	}
		

	}
	
	public String createImage() throws Exception {
		String result = "redirect";
		ESESystem preferences = preferncesService.findPrefernceById("1");
		setProdReturnImgAvil(preferences.getPreferences().get(ESESystem.ENABLE_PRODUCT_RETURN_IMAGE));
		if (prodReturnImgAvil.equals("1") && distId!=null && !StringUtil.isEmpty(distId)) {
			ProductReturn prodReturn = productDistributionService.findProductReturnById(Long.parseLong(distId));
			Set<PMTImageDetails> distImageSet = new HashSet<PMTImageDetails>();
			File[] dynamicData = {getProdReturnImg1(), getProdReturnImg2() };
			for (File productData : dynamicData) {
				File value = productData;
				if (value != null) {
					PMTImageDetails distImage = new PMTImageDetails();
					byte[] farmPhotoData = FileUtil.getBinaryFileContent(value);
					if (farmPhotoData != null) {
						distImage.setPhoto(farmPhotoData);
						distImage.setType(4);
					}
					distImageSet.add(distImage);
				}
			}
			prodReturn.setPmtImageDetail(distImageSet);
			productDistributionService.updateProductReturn(prodReturn);
		}

		return result;

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
		setProdReturnImgAvil(preferences.getPreferences().get(ESESystem.ENABLE_PRODUCT_RETURN_IMAGE));
		setSeasonName(getCurrentSeasonsCode() + "-" + getCurrentSeasonsName());
		cashType.put(0, getText("cashType1"));
		cashType.put(1, getText("cashType2"));
		checkBoxs.put('0', getText("distribution.freeDispatch"));
		return productReturn;
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

		double avliableStock=0.0;
		
		String result = NOT_APPLICABLE;
		/*populateDistribution
		 * if (!StringUtil.isEmpty(selectedAgent)) { String[] fieldstaffArr =
		 * selectedAgent.split("-"); //
		 * warehouse=locationService.findCoOperativeByCode(fieldstaffArr[1].trim
		 * ()); }
		 */
		SubCategory subCategory = productService.findSubCategoryByCode(selectedCategory);
		 product = productService.findProductByProductCodeAndSubCategoryId(selectedProduct, subCategory.getId());
		if (!StringUtil.isEmpty(selectedFarmer) && StringUtil.isEmpty(selectedAgent) && !StringUtil.isEmpty(selectedProduct)) {
			String[] WarehouseArray = selectedCooperative.split("-");
		
		
		if (!StringUtil.isEmpty(selectedFarmer) && !StringUtil.isEmpty(product)) 
		{

			//distributionObj = productDistributionService.findAvailableStockByFarmer(farmerArray[1],product.getId(),selectedSeason,batchNo);
			avliableStock = productDistributionService.findWarehouseAvaliableStock(WarehouseArray[1],product.getId(),selectedSeason,batchNo);
			//avliableStock=Double.valueOf(String.valueOf(distributionObj[0]));
			productPrice =product.getPrice();
			
		}
		}else if (StringUtil.isEmpty(selectedCooperative) && !StringUtil.isEmpty(selectedAgent)
				&& !StringUtil.isEmpty(product)) {
			/*
			 * warehouseProduct = productDistributionService.
			 * findAvailableStockByAgentIdProductIdBatchNoSeason(
			 * selectedAgent.split("-")[1].trim(),
			 * product.getId(),selectedSeason,batchNo);
			 */
			Agent agent = agentService.findAgent(Long.valueOf(selectedAgent));
			if (agent != null) {
				avliableStock = productDistributionService.findAvailableStockByAgentIdProductIdSeasonBatchNo(
						agent.getProfileId(), product.getId(), selectedSeason, batchNo);
				productPrice =product.getPrice();
			}
		}else if (!StringUtil.isEmpty(selectedCooperative)&& !StringUtil.isEmpty(product)){
			String[] WarehouseArray = selectedCooperative.split("-");
			avliableStock = productDistributionService.findWarehouseAvaliableStock(WarehouseArray[1],product.getId(),selectedSeason,batchNo);
		}
		
		productPrice=StringUtil.isEmpty(productPrice)?"0.0":productPrice;

		if (!ObjectUtil.isEmpty(avliableStock)) {
			// stock = warehouseProduct.getStock();
			stock = avliableStock;
			units = product.getUnit();
			
			
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
			String[] warehouseArr = selectedCooperative.split("-");
			warehouse = locationService.findCoOperativeByCode(warehouseArr[1].trim());
		}
		SubCategory subCategory = productService.findSubCategoryByCode(selectedCategory);
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

		Warehouse warehouse = new Warehouse();
		List<Object[]> batchNoList = null;
		/*
		 * if (!StringUtil.isEmpty(selectedAgent)) { String[] fieldstaffArr =
		 * selectedAgent.split("-"); //
		 * warehouse=locationService.findCoOperativeByCode(fieldstaffArr[1].trim
		 * ()); }
		 */
		if (!StringUtil.isEmpty(selectedCooperative)) {
			String[] warehouseArr = selectedCooperative.split("-");
			warehouse = locationService.findCoOperativeByCode(warehouseArr[1].trim());
		}

		SubCategory subCategory = productService.findSubCategoryByCode(selectedCategory);
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
	public void populateProductReturn() throws Exception {
		Double avbleStock = 0.0;
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
					result = "accountUnavailable";
				else if (ESEAccount.ACTIVE != fieldStaffAcct.getStatus())
					result = "accountInactive";
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

	/*	if (productStock) {
			if (StringUtil.isEmpty(selectedCooperative)) {
				result = "emptyCooperative";
			} else {
				String[] warehouseArry = selectedCooperative.split("-");
				Warehouse warehouse = locationService.findCoOperativeByCode(warehouseArry[1].trim());
				if (ObjectUtil.isEmpty(warehouse)) {
					result = "cooperativeNotExist";
				}
			}
		}*/

		if (StringUtil.isEmpty(result)) {
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
							String[] warehouseArry = selectedCooperative.split("-");
							Warehouse warehouse = locationService.findCoOperativeByCode(warehouseArry[1].trim());
							
							if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {
								/*avbleStock = productDistributionService
										.findAvailableStockByWarehouseIdProductIdSeasonBatchNo(warehouse.getId(),
												product.getId(), selectedSeason, productDetail[5]);*/
								avbleStock = productDistributionService
										.findAvailableStockByWarehouseIdProductIdSeasonBatchNo(warehouse.getId(),
												product.getId(), selectedSeason, productDetail[3]);

							} else {
								avbleStock = productDistributionService
										.findAvailableStockByWarehouseIdProductIdBatchNum(warehouse.getId(),
												product.getId(), productDetail[5]);
							}
						} else						
						if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {

							/*avbleStock = productDistributionService.findAvailableStockByAgentIdProductIdSeasonBatchNo(
									agent.getProfileId(), product.getId(), selectedSeason, productDetail[5]);*/
							avbleStock = productDistributionService.findAvailableStockByAgentIdProductIdSeasonBatchNo(
									agent.getProfileId(), product.getId(), selectedSeason, productDetail[3]);
						} else {
							/*avbleStock = productDistributionService.findAvailableStockByAgentIdProductIdBatchNum(
									agent.getProfileId(), product.getId(), productDetail[5]);*/
							avbleStock = productDistributionService.findAvailableStockByAgentIdProductIdBatchNum(
									agent.getProfileId(), product.getId(), productDetail[3]);

						}

						if (ObjectUtil.isEmpty(avbleStock)) {
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
					productReturn = new ProductReturn();

				if (registeredFarmer) {
					Farmer farmer = farmerService.findFarmerByFarmerId(farmerId);
					if (!ObjectUtil.isEmpty(farmer) && !ObjectUtil.isEmpty(farmer.getVillage())) {
						Village village = locationService.findVillageById(farmer.getVillage().getId());
						if (!ObjectUtil.isEmpty(village))
							productReturn.setVillage(village);
					}
					agroTransaction.setFarmerId(farmer.getFarmerId());
					agroTransaction.setFarmerName(farmer.getFirstName());
					productReturn.setFarmerId(agroTransaction.getFarmerId());
					productReturn.setFarmerName(agroTransaction.getFarmerName());

				} else {
					if (!StringUtil.isEmpty(selectedVillage)) {
						Village village = locationService.findVillageByCode(selectedVillage.split("-")[1].trim());
						productReturn.setVillage(village);
					}
					productReturn.setMobileNumber(mobileNo);
					productReturn.setFarmerId(NOT_APPLICABLE);
					productReturn.setFarmerName(unRegisterFarmerName);
					agroTransaction.setFarmerId(NOT_APPLICABLE);
					agroTransaction.setFarmerName(unRegisterFarmerName);
				}

				String receiptnumberSeq = idGenerator.getDistributionSeq();
				receiptNumber = receiptnumberSeq;
				agroTransaction.setReceiptNo(receiptnumberSeq);
				productReturn.setReceiptNumber(receiptNumber);
				// Calendar currentDate = Calendar.getInstance();
				agroTransaction.setTxnTime(DateUtil.convertStringToDate(selectedDate, getGeneralDateFormat()));
				agroTransaction.setTxnTime(DateUtil.setTimeToDate(agroTransaction.getTxnTime()));
				if (registeredFarmer && !StringUtil.isEmpty(selectedWarehouse) && selectedWarehouse.contains("-")) {
					Warehouse samithi = locationService
							.findWarehouseByCode(selectedWarehouse.trim().split("-")[1].trim());
					agroTransaction.setSamithi(samithi);
					productReturn.setSamithiName(agroTransaction.getSamithi().getName());
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
					agroTransaction.setTxnType(ProductReturn.PRODUCT_RETURN_FROM_FARMER);
					agroTransaction.setTxnDesc(ProductReturn.PRODUCT_RETURN_FROM_FARMER_DESCRIPTION);
					productReturn.setAgentId(agroTransaction.getAgentId());
					productReturn.setAgentName(agroTransaction.getAgentName());
					productReturn.setAgent(agent);

					
				}
				

				if (!StringUtil.isEmpty(selectedCooperative)) {
					// String[] cooperativeAry = selectedCooperative.split("-");
					Warehouse warehouseTemp = locationService
							.findCoOperativeByCode(selectedCooperative.split("-")[1].trim());
					if (!ObjectUtil.isEmpty(warehouseTemp)) {
						agroTransaction.setServicePointId(warehouseTemp.getCode());
						agroTransaction.setServicePointName(warehouseTemp.getName());
						agroTransaction.setTxnType(ProductReturn.PRODUCT_RETURN_FROM_FARMER);
						agroTransaction.setTxnDesc(ProductReturn.PRODUCT_RETURN_FROM_FARMER_DESCRIPTION);
						productReturn.setServicePointId(agroTransaction.getServicePointId());
						productReturn.setWarehouse(warehouseTemp);
					}
				}
				else{
					agroTransaction.setServicePointId("");
					agroTransaction.setServicePointName("");
				}
				// Warehouse warehouse = agent.getCooperative();
				agroTransaction.setProductReturn(productReturn);
				agroTransaction.setDeviceId(NOT_APPLICABLE);
				agroTransaction.setDeviceName(NOT_APPLICABLE);
				agroTransaction.setOperType(ESETxn.ON_LINE);
				productReturn.setServicePointId(agroTransaction.getServicePointId());
				productReturn.setServicePointName(agroTransaction.getServicePointName());
				productReturn.setTxnTime(agroTransaction.getTxnTime());
				agroTransaction.setProfType(Profile.CLIENT);
				//agroTransaction.setTxnType(ProductReturn.PRODUCT_RETURN_FROM_FARMER);
				//agroTransaction.setTxnDesc(ProductReturn.PRODUCT_RETURN_FROM_FARMER_DESCRIPTION);
				ESEAccount fieldStaffaccount = null;
				if (productStock) {
					agroTransaction.setProductStock(WarehouseProduct.StockType.WAREHOUSE_STOCK.name());
					agroTransaction.setWarehouseCode(selectedCooperative.split("-")[1].trim());
					productReturn.setProductStock(agroTransaction.getProductStock());
					productReturn.setStockType(ProductReturn.WAREHOUSE_STOCK);
				} else {
					agroTransaction.setProductStock(WarehouseProduct.StockType.AGENT_STOCK.name());
					productReturn.setProductStock(agroTransaction.getProductStock());
					agent = agentService.findAgent(Long.valueOf(selectedAgent));
					fieldStaffaccount = accountService.findAccountByProfileIdAndProfileType(agent.getProfileId(),
							ESEAccount.AGENT_ACCOUNT);
					productReturn.setStockType(ProductReturn.MOBILE_STOCK);

				}

				// PROCT DETAILS FOR DISTRIBUTION_DETAIL
				Set<ProductReturnDetail> productReturnDetailsSet = new HashSet<ProductReturnDetail>();
				double totalAmount = 0;
				if (!StringUtil.isEmpty(productTotalString)) {
					String[] productTotalArray = productTotalString.trim().split("\\|\\|");
					for (int j = 0; j < productTotalArray.length; j++) {
						String[] productDetail = productTotalArray[j].split("##");
						String[] productInfo = productDetail[0].split("=");
						String[] farmerId = selectedFarmerValue.split("-");
						if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {
							product = productService.findProductByCode(productInfo[0].trim());
						} else {
							product = productService.findProductById(Long.valueOf(productInfo[0].trim()));
						}
						// Product product =
						// productService.findProductByCode(productInfo[0].trim());

						if (!ObjectUtil.isEmpty(product)) {
							if (ObjectUtil.isEmpty(agent)) {
						
								
								if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {
/*									distributionObj = productDistributionService
											.findAvailableStockByFarmer(farmerId[1],
													product.getId(), selectedSeason, productDetail[5]);*/
									distributionObj = productDistributionService
											.findAvailableStockByFarmer(farmerId[1],
													product.getId(), selectedSeason, productDetail[3]);
									avbleStock=Double.valueOf(String.valueOf(distributionObj[0]));

								} else {
									/*distributionObj = productDistributionService
											.findAvailableStockByFarmer(farmerId[1],
													product.getId(), selectedSeason, productDetail[5]);*/
									distributionObj = productDistributionService
											.findAvailableStockByFarmer(farmerId[1],
													product.getId(), selectedSeason, productDetail[3]);
									avbleStock=Double.valueOf(String.valueOf(distributionObj[0]));
								}

							}

							else
								if (!getCurrentTenantId().equalsIgnoreCase("lalteer")) {

								/*avbleStock = productDistributionService
										.findAvailableStockByAgentIdProductIdSeasonBatchNo(agent.getProfileId(),
												product.getId(), selectedSeason, productDetail[5]);*/
									avbleStock = productDistributionService
											.findAvailableStockByAgentIdProductIdSeasonBatchNo(agent.getProfileId(),
													product.getId(), selectedSeason, productDetail[3]);
							} else {
								/*avbleStock = productDistributionService.findAvailableStockByAgentIdProductIdBatchNum(
										agent.getProfileId(), product.getId(), productDetail[5]);*/
								avbleStock = productDistributionService.findAvailableStockByAgentIdProductIdBatchNum(
										agent.getProfileId(), product.getId(), productDetail[3]);

							}

						}

						Product productDetails = productService.findProductByCode(productInfo[0].trim());
						double quantity = Double.valueOf(productDetail[1]);
						double sellingPrice = Double.valueOf(productDetail[2]);
					//	double subTotal = Double.valueOf(productDetail[3]);
						productReturnDetail = new ProductReturnDetail();
						productReturnDetail.setProduct(product);
						try {
							productReturnDetail.setProductReturn(productReturn);
							productReturnDetail.setQuantity(quantity);
							productReturnDetail.setUnit(product.getUnit());
							// distributionDetail.setPricePerUnit(pricePerUnit);
							productReturnDetail.setExistingQuantity(String.valueOf(avbleStock));
							productReturnDetail.setCurrentQuantity(
									String.valueOf(Double.valueOf(productReturnDetail.getExistingQuantity())
											+ productReturnDetail.getQuantity()));
							productReturnDetail.setSellingPrice(sellingPrice);
							//productReturnDetail.setCostPrice(Double.valueOf(productDetail[4].trim()));
							productReturnDetail.setCostPrice(Double.valueOf("0.00"));
							productReturnDetail.setTax(taxValue);
							//productReturnDetail.setBatchNo(productDetail[5]);
							productReturnDetail.setBatchNo(productDetail[3]);
							productReturnDetail.setSeasonCode(selectedSeason);
							productReturnDetail.setTax(taxValue);
							productReturnDetail.setFinalAmount(finalAmtValue);
							//productReturnDetail.setSubTotal(subTotal);
							productReturnDetail.setSubTotal(Double.valueOf("0.00"));
							totalAmount = totalAmount
									+ (Double.valueOf(productDetail[2]) * Double.valueOf(productDetail[1]));
							productReturn.setTotalAmount(totalAmount);
						/*	productReturn.setTax(Double.valueOf(String.valueOf(distributionObj[2])));
							productReturn.setPaymentAmount(Double.valueOf(String.valueOf(distributionObj[3])));
							productReturn.setPaymentAmount(Double.valueOf(String.valueOf(distributionObj[4])));*/
						} catch (Exception e) {
							e.printStackTrace();
						}
						productReturnDetailsSet.add(productReturnDetail);

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
					Farmer farmer = farmerService.findFarmerByFarmerId(farmerId);
					if (!StringUtil.isEmpty(farmer.getId()) && !ObjectUtil.isEmpty(season)) {
						farmerAccount = farmerDAO.findAccountBySeassonProcurmentProductFarmer(season.getId(), 0L,
								farmer.getId());
						if (!ObjectUtil.isEmpty(farmerAccount)) {
							
							agroTransaction.setIntBalance(farmerAccount.getCashBalance());
							agroTransaction.setTxnAmount(getFinalAmtValue());
							/*
							 * if(farmerAccount.getCashBalance()>0.00){
							 * agroTransaction.setBalAmount(getCashBalance(
							 * farmerAccount .getCashBalance(),
							 * agroTransaction.getTxnAmount(), false)); }else{
							 * agroTransaction.setBalAmount(farmerAccount
							 * .getCashBalance());
							 */
							agroTransaction
									.setBalAmount(farmerAccount.getCashBalance());
							// }

							agroTransaction.setAccount(farmerAccount);
						}
					}
				}

				// Season season =
				// farmerService.findSeasonById(Long.valueOf(selectedSeason));
				productReturn.setProductReturnDetail(productReturnDetailsSet);
				productReturn.setTenantId(getCurrentTenantId());
				// distribution.setSeason(season);saveDistributionAndDistributionDetail
				// if ("0".equals(paymentMode)) {
				// distribution.setPaymentMode(ESEAccount.PAYMENT_MODE_CASH);
				// distribution.setPaymentAmount(paymentAmount);
				// distribution.setPaymentAmount(getPaymentAmount());
				// } else
				
				productReturn.setIntBalance(agroTransaction.getIntBalance());
				productReturn.setBalAmount(agroTransaction.getBalAmount());
				productReturn.setAgroTransaction(agroTransaction);
				productReturn.setTxnType(agroTransaction.getTxnType());
				productReturn.setBranchId(getBranchId());
				// productDistributionService.buildAgroTransactionDistributionObject(distribution);
				
				productReturn.setTenantId(getCurrentTenantId());
				productReturn.setUserName(getUsername());
				if (harvestSeasonEnabled.equals("1")) {
					HarvestSeason harvestSeason = productDistributionService
							.findHarvestSeasonBySeasonCode(selectedSeason);
					productReturn.setHarvestSeason(harvestSeason);
					productReturn.setSeasonCode(productReturn.getHarvestSeason().getCode());
				} else {
					productReturn.setSeasonCode(getCurrentSeasonsCode());
				}
				productDistributionService.saveProductReturnAndProductReturnDetail(productReturn);
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

			/*String receiptHtml = "<a href=\"javascript:printReceipt(\\'" + receiptNumber
					+ "\\')\" onclick='printReceipt(\"" + receiptNumber + "\")'>" + getText("printReceipt")
					+ "</button>";

			setDistributionDescription(getText("productreturnSucess") + "</br>" + getText("receiptNumber") + " : "
					+ receiptNumber + "</br>" + receiptHtml);
			*/
			String receiptHtml = "<a href=\"javascript:printReceipt(\\'" + receiptNumber + 
	                  "\\')\" class ='btn btn-default btnBorderRadius' onclick='printReceipt(\"" + receiptNumber + "\")'>" +
	                  getText("printReceipt") + "</button>";
	                 
	                setDistributionDescription("<h5>"
	                        + getText("receiptNumber") + " : " + receiptNumber+ "</h5>" + receiptHtml + "</br>");

			//printAjaxResponse(getDistributionDescription(), "text/html");
			JSONObject json=new JSONObject();
			json.put("id", productReturn.getId());
			json.put("des",getDistributionDescription());
			sendAjaxResponse(json);
		}
		response.getWriter().print(result);
	}
	
	/**
	 * Sets the current season.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String populateProduct() throws Exception {

		//String[] warehouseCode = selectedCooperative.split("-");
		WarehouseProduct warehouseProduct = null;

		//Warehouse warehouse = productService.findWarehouseByCode(warehouseCode[1].trim());

		if (!StringUtil.isEmpty(selectedCategory)) {
			// String[] product = selectedCategory.split("-");
			List<Product> productList = productService.findProductBySubCategoryCode(selectedCategory.split("-")[0].trim());
			JSONArray productArr = new JSONArray();

			if (!ObjectUtil.isListEmpty(productList)) {
				for (Product productObj : productList) {
					/*warehouseProduct = productDistributionService.findAvailableStock(warehouse.getId(),
							productObj.getId());*/
					//if (!ObjectUtil.isEmpty(warehouseProduct)) {

						productArr.add(getJSONObject(productObj.getCode(), productObj.getName()));

					//}
				}
			}
			sendAjaxResponse(productArr);
		}

		return null;

	}

	public String populateLalteerProduct() throws Exception {

		String[] warehouseCode = selectedCooperative.split("-");
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
			String[] warehouse = selectedCooperative.split("-");
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
			String[] warehouse = selectedCooperative.split("-");

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
			String[] warehouse = selectedCooperative.split("-");

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
						if (warehouseProduct != null) {
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
			String[] warehouseCode = selectedCooperative.split("-");

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

	public String populateLalteerCostPrice() throws Exception {

		double result = 0d;

		if (!StringUtil.isEmpty(selectedProduct) && !StringUtil.isEmpty(selectedCooperative)
				&& !"0".equalsIgnoreCase(selectedProduct)) {
			String[] warehouseCode = selectedCooperative.split("-");

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
			ProductReturn productReturn = productDistributionService.findProductReturnFarmerByRecNoAndTxnType(receiptNumber, ProductReturn.PRODUCT_RETURN_FROM_FARMER);
			if (!StringUtil.isEmpty(productReturn)) {
				buildTransactionPrintMap(productReturn);
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

		this.productReturnMap = new HashMap<String, Object>();
		List<Map<String, Object>> productMapList = new ArrayList<Map<String, Object>>();
		Map<String, Object> totalMap = new LinkedHashMap<String, Object>();
		this.productReturnMap.put("recNo", "");
		this.productReturnMap.put("fId", "");
		this.productReturnMap.put("fName", "");
		this.productReturnMap.put("village", "");
		this.productReturnMap.put("date", "");
		this.productReturnMap.put("distributionAmt", "");
		this.productReturnMap.put("paymentAmout", "");
		this.productReturnMap.put("productMapList", productMapList);
		this.productReturnMap.put("totalInfo", totalMap);
		this.productReturnMap.put("agentId", "");
		this.productReturnMap.put("agentName", "");
		this.productReturnMap.put("openingBal", "0.00");
		this.productReturnMap.put("finalBal", "0.00");
		this.productReturnMap.put("samithi", "N/A");
		this.productReturnMap.put("freeDist", "0");
		this.productReturnMap.put("oBal", 0.00);
		this.productReturnMap.put("fBal", 0.00);
		this.productReturnMap.put("isAgent", false);
		this.productReturnMap.put("payMode", "");
		this.productReturnMap.put("payAmt", "");
		this.productReturnMap.put("credAmt", "");
		this.productReturnMap.put("finPayBal", 0.00);
		this.productReturnMap.put("amntTax", 0.00);
		this.productReturnMap.put("enableBatchNo", "");
		this.productReturnMap.put("warehouseCode", "");
		this.productReturnMap.put("warehouseName", "");
	}

	/**
	 * Builds the transaction print map.
	 * 
	 * @param distribution
	 *            the distribution
	 */
	private void buildTransactionPrintMap(ProductReturn productReturn) {

		List<Map<String, Object>> productMapList = new ArrayList<Map<String, Object>>();
		if (!ObjectUtil.isEmpty(productReturn)) {
			double totalQuantity = 0d;
			double totalPricePerUnit = 0d;
			double totalAmount = 0d;
			DateFormat df = new SimpleDateFormat(getGeneralDateFormat());
			// if (!ObjectUtil.isEmpty(distribution.getAgroTransaction())) {
			if (!StringUtil.isEmpty(productReturn.getReceiptNumber())) {
				this.productReturnMap.put("recNo", productReturn.getReceiptNumber());
			}
			if (!StringUtil.isEmpty(productReturn.getFarmerId())) {
				this.productReturnMap.put("fId", productReturn.getFarmerId());
			}
			if (!StringUtil.isEmpty(productReturn.getFarmerName())) {
				this.productReturnMap.put("fName", productReturn.getFarmerName());
			}
			if (!ObjectUtil.isEmpty(productReturn.getVillage())
					&& !StringUtil.isEmpty(productReturn.getVillage().getName())) {
				this.productReturnMap.put("village", productReturn.getVillage().getName());
			}
			if (!ObjectUtil.isEmpty(productReturn.getTxnTime())) {
				this.productReturnMap.put("date", df.format(productReturn.getTxnTime()));
			}
			if (!StringUtil.isEmpty(productReturn.getAgentId())) {
				this.productReturnMap.put("agentId", productReturn.getAgentId());
				Agent agent = agentService.findAgentByProfileId(productReturn.getAgentId());
				if (!ObjectUtil.isEmpty(agent))
					this.productReturnMap.put("isAgent", true);
			}
			if (!StringUtil.isEmpty(productReturn.getAgentName())) {
				this.productReturnMap.put("agentName", productReturn.getAgentName());
			}
			if (!StringUtil.isEmpty(productReturn.getSamithiName())) {
				this.productReturnMap.put("samithi", productReturn.getSamithiName());
			}

			if (!StringUtil.isEmpty(productReturn.getFreeDistribution())) {
				this.productReturnMap.put("freeDist", productReturn.getFreeDistribution());
			}
			
			if (!StringUtil.isEmpty(productReturn.getTotalAmount())) {
				this.productReturnMap.put("distributionAmt",
						CurrencyUtil.thousandSeparator(productReturn.getTotalAmount()));
			}

			if (!StringUtil.isEmpty(productReturn.getFinalAmount())) {
				this.productReturnMap.put("distributionFinAmt",
						CurrencyUtil.thousandSeparator(productReturn.getFinalAmount()));
			}

			this.productReturnMap.put("openingBal",
					CurrencyUtil.thousandSeparator(Math.abs(productReturn.getIntBalance())));
			this.productReturnMap.put("oBal", productReturn.getIntBalance());
			
				this.productReturnMap.put("finalBal",
						CurrencyUtil.thousandSeparator(Math.abs(productReturn.getBalAmount())));
				this.productReturnMap.put("fBal", productReturn.getBalAmount());
	
			if (!ObjectUtil.isEmpty(productReturn.getPaymentMode())) {
				if (productReturn.getPaymentMode().equals("CR")) {
					this.productReturnMap.put("payMode", "Credit");
					Double outstandingBal = productReturn.getFinalAmount() + (Math.abs(productReturn.getIntBalance()));
					this.productReturnMap.put("outstandingBal", outstandingBal);
				}
			}
			if (!ObjectUtil.isEmpty(productReturn.getFinalAmount())) {
				this.productReturnMap.put("credAmt",
						CurrencyUtil.getDecimalFormat(productReturn.getFinalAmount(), "##.00"));
			}
			if (!ObjectUtil.isEmpty(productReturn.getTax())) {
				this.productReturnMap.put("amntTax", CurrencyUtil.getDecimalFormat(productReturn.getTax(), "##.00"));
			}
			if (!StringUtil.isEmpty(productReturn.getServicePointId())) {
				this.productReturnMap.put("warehouseCode", productReturn.getServicePointId());
			}
			if (!StringUtil.isEmpty(productReturn.getServicePointName())) {
				this.productReturnMap.put("warehouseName", productReturn.getServicePointName());
			}
			String batchaNo = preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO);
			this.productReturnMap.put("enableBatchNo", batchaNo);
			// }
			if (!ObjectUtil.isEmpty(productReturn.getTxnType())) {
				if ("344".equalsIgnoreCase(productReturn.getTxnType())) {
					this.productReturnMap.put("isAgent", false);
				} else {
					this.productReturnMap.put("isAgent", true);
				}
			}

			this.productReturnMap.put("paymentAmout", CurrencyUtil.thousandSeparator(productReturn.getPaymentAmount()));
			this.productReturnMap.put("productMapList", productMapList);
			if (!ObjectUtil.isListEmpty(productReturn.getProductReturnDetail())) {
				for (ProductReturnDetail productReturnDetail : productReturn.getProductReturnDetail()) {
					Map<String, Object> productMap = new LinkedHashMap<String, Object>();
					String productName = "";
					String categoryName = "";

					if (!ObjectUtil.isEmpty(productReturnDetail.getProduct().getSubcategory()))
						categoryName = !StringUtil.isEmpty(productReturnDetail.getProduct().getSubcategory().getName())
								? productReturnDetail.getProduct().getSubcategory().getName() : "";
					if (!ObjectUtil.isEmpty(productReturnDetail.getProduct()))
						productName = !StringUtil.isEmpty(productReturnDetail.getProduct().getName())
								? productReturnDetail.getProduct().getName() : "";

					productMap.put("category", categoryName);
					productMap.put("product", productName);
					productMap.put("batchNo", productReturnDetail.getBatchNo());
					productMap.put("sellingPrice",
							CurrencyUtil.getDecimalFormat(productReturnDetail.getSellingPrice(), "##.0000"));
					productMap.put("quantity",
							CurrencyUtil.getDecimalFormat(productReturnDetail.getQuantity(), "##.000"));
					// productMap.put("pricePerUnit", CurrencyUtil
					// .thousandSeparator(distributionDetail.getPricePerUnit()));
					productMap.put("amount", CurrencyUtil.thousandSeparator(productReturnDetail.getSubTotal()));

					totalQuantity += productReturnDetail.getQuantity();
					totalPricePerUnit += productReturnDetail.getPricePerUnit();
					totalAmount += productReturnDetail.getSubTotal();

					productMapList.add(productMap);
				}
			}
			Map<String, Object> totalMap = new LinkedHashMap<String, Object>();
			totalMap.put("totalQuantity", CurrencyUtil.getDecimalFormat(totalQuantity, "##.000"));
			totalMap.put("totalPricePerUnit", CurrencyUtil.thousandSeparator(totalPricePerUnit));
			totalMap.put("totalAmount", CurrencyUtil.thousandSeparator(totalAmount));
			this.productReturnMap.put("totalInfo", totalMap);
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

			List<SubCategory> listSubCategory = categoryService.listSubCategory();;
			if (!ObjectUtil.isListEmpty(listSubCategory)) {
				for (SubCategory innerCategory : listSubCategory) {
					categoryList.add(innerCategory.getCode() + " - " + innerCategory.getName());
				}
			}
		
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
				returnMap.put(warehouseProduct.getName() + " -" +warehouseProduct.getCode(),
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



	public ProductReturn getProductReturn() {
		return productReturn;
	}



	public void setProductReturn(ProductReturn productReturn) {
		this.productReturn = productReturn;
	}



	public ProductReturnDetail getProductReturnDetail() {
		return productReturnDetail;
	}



	public void setProductReturnDetail(ProductReturnDetail productReturnDetail) {
		this.productReturnDetail = productReturnDetail;
	}



	public Map<String, Object> getProductReturnMap() {
		return productReturnMap;
	}



	public void setProductReturnMap(Map<String, Object> productReturnMap) {
		this.productReturnMap = productReturnMap;
	}



	public String getDistId() {
		return distId;
	}



	public void setDistId(String distId) {
		this.distId = distId;
	}



	public File getProdReturnImg1() {
		return prodReturnImg1;
	}



	public void setProdReturnImg1(File prodReturnImg1) {
		this.prodReturnImg1 = prodReturnImg1;
	}



	public File getProdReturnImg2() {
		return prodReturnImg2;
	}



	public void setProdReturnImg2(File prodReturnImg2) {
		this.prodReturnImg2 = prodReturnImg2;
	}



	public String getProdReturnImgAvil() {
		return prodReturnImgAvil;
	}



	public void setProdReturnImgAvil(String prodReturnImgAvil) {
		this.prodReturnImgAvil = prodReturnImgAvil;
	}


	
}
