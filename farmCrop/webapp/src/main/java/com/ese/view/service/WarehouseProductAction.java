/*
 * WarehouseProductAction.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.service;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.ese.view.profile.validator.CatalogueValidator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.entity.WarehouseProduct;
import com.sourcetrace.eses.entity.WarehouseProductDetail;
import com.sourcetrace.eses.order.entity.profile.Category;
import com.sourcetrace.eses.order.entity.profile.SubCategory;
import com.sourcetrace.eses.order.entity.txn.DistributionDetail;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.ICategoryService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.profile.Product;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.Vendor;
import com.sourcetrace.esesw.entity.profile.WarehousePayment;
import com.sourcetrace.esesw.entity.profile.WarehousePaymentDetails;
import com.sourcetrace.esesw.view.WebTransactionAction;

// TODO: Auto-generated Javadoc
public class WarehouseProductAction extends WebTransactionAction {

	private static final long serialVersionUID = 1L;
	private String id;
	private String selectedCategory = "";
	private String selectedSubCategory;
	private String selectedWarehouse;
	private String selectedProduct;
	private String selectedVendor;
	private String productTotalString;
	private String batchno;
	private String orderNo;
	private String stock;
	private String productAvailableStock;
	private String securityAction;
	private String trxnDate;
	private String paymentMode;
	private double paymentAmount;
	private String remarks;
	private String productAvailableUnit;
	private double creditAmount;
	private String packetInfo;
	private String parentCode;
	private String variety;
	private WarehouseProduct warehouseProduct;
	private WarehousePayment filter;
	private ICategoryService categoryService;
	private IProductService productService;
	private ILocationService locationService;
	private IProductDistributionService productDistributionService;
	private IUniqueIDGenerator idGenerator;
	@Autowired
	private IFarmerService farmerService;
	private ICatalogueService catalogueService;

	private List<Category> CategoryList = new ArrayList<Category>();
	private List<SubCategory> subCategoryList = new ArrayList<SubCategory>();
	private List<Product> productList = new ArrayList<Product>();
	private List<SubCategory> categoryCategoryList = new ArrayList<SubCategory>();
	Map<String, String> vendorList = new LinkedHashMap<String, String>();
	private String startDate;
	public static final String WAREHOUSE_PRODUCT = "WAREHOUSE_PRODUCT";
	public static final String EMPTY_STOCK = "0";
	private String receiptNumber;
	private String selectedSubCategoryList;
	private String stockDescription;
	Map<Integer, String> cashType = new LinkedHashMap<Integer, String>();
	private String selectedVendorId;
	@Autowired
	private IAccountService accountService;
	private String selectedVendorValue;
	private double taxValue;
	private double finalAmtValue;
	private ESEAccount account;
	private String cashCreditValue;
	private Map<Integer, String> listExpMnth = new LinkedHashMap<Integer, String>();
	private Map<String, Object> warehouseMap = new LinkedHashMap<String, Object>();
	private static final String RECEIPT_DATE_FORMAT = "dd-MM-yyyy";
	private String seasonName;
	Map<String, String> harvestSeasonList = new LinkedHashMap<String, String>();
	private String selectedSeason;
	private String selectedSeedType;
	private String selectedUom;
	private String finalAmount;
	private String totalAmount;

	public String getPacketInfo() {
		return packetInfo;
	}

	public void setPacketInfo(String packetInfo) {
		this.packetInfo = packetInfo;
	}

	public String getVariety() {
		return variety;
	}

	public void setVariety(String variety) {
		this.variety = variety;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getSelectedSeedType() {
		return selectedSeedType;
	}

	public void setSelectedSeedType(String selectedSeedType) {
		this.selectedSeedType = selectedSeedType;
	}

	public String getSelectedUom() {
		return selectedUom;
	}

	public void setSelectedUom(String selectedUom) {
		this.selectedUom = selectedUom;
	}

	private String selectedBatchNo;
	private String enableBatchNo;
	DecimalFormat formatter = new DecimalFormat("0.00");
	private List<WarehousePaymentDetails> payementDetailList;
	private double currentStock;
	private String stockArrJson;

	/**
	 * @see com.sourcetrace.esesw.view.SwitchAction#data()
	 */
	@Override
	public String data() throws Exception {

		Map<String, String> searchRecord = getJQGridRequestParam(); // get the
																	// search
																	// parameter
																	// with
		// value

		filter = new WarehousePayment();
		if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
			filter.setBranchId(searchRecord.get("branchId").trim());
		}
		if (!StringUtil.isEmpty(searchRecord.get("subBranchId"))) {
			filter.setBranchId(searchRecord.get("subBranchId").trim());
		}
		if (!StringUtil.isEmpty(searchRecord.get("orderNo"))) {
			filter.setOrderNo(searchRecord.get("orderNo").trim());
		}
		if (!StringUtil.isEmpty(searchRecord.get("warehouse"))) {
			Warehouse warehouse = new Warehouse();
			warehouse.setName(searchRecord.get("warehouse"));
			filter.setWarehouse(warehouse);
		}
		if (!StringUtil.isEmpty(searchRecord.get("receiptNo"))) {
			filter.setReceiptNo(searchRecord.get("receiptNo").trim());
		}
		if (!StringUtil.isEmpty(searchRecord.get("vendor"))) {
			Vendor vendor = new Vendor();
			vendor.setVendorName(searchRecord.get("vendor"));
			filter.setVendor(vendor);
		}
		if (!StringUtil.isEmpty(searchRecord.get("seasonCode"))) {
			filter.setSeasonCode(searchRecord.get("seasonCode").trim());
		}
		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		return sendJQGridJSONResponse(data);
	}

	/**
	 * @see com.sourcetrace.esesw.view.SwitchAction#toJSON(java.lang.Object)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		WarehousePayment warehousePayment = (WarehousePayment) obj;
		DecimalFormat formatter = new DecimalFormat("#.00");
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(!StringUtil
						.isEmpty(getBranchesMap().get(getParentBranchMap().get(warehousePayment.getBranchId())))
								? getBranchesMap().get(getParentBranchMap().get(warehousePayment.getBranchId()))
								: getBranchesMap().get(warehousePayment.getBranchId()));
			}
			rows.add(getBranchesMap().get(warehousePayment.getBranchId()));
		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(branchesMap.get(warehousePayment.getBranchId()));
			}
		}

		// rows.add(warehousePayment.getTrxnDate().toString());
		rows.add(warehousePayment.getOrderNo());
		rows.add(warehousePayment.getWarehouse().getName());
		rows.add(warehousePayment.getReceiptNo());
		rows.add(warehousePayment.getVendor().getVendorName());
		// rows.add(warehousePayment.getTotalQty().toString());
		// rows.add(warehousePayment.getTotalAmount().toString());
		HarvestSeason harvestSeason = clientService.findSeasonByCode(warehousePayment.getSeasonCode());
		rows.add(!ObjectUtil.isEmpty(harvestSeason) ? harvestSeason.getName() : "");
		jsonObject.put("id", warehousePayment.getId());
		jsonObject.put("cell", rows);
		return jsonObject;
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
			subCategoryList = categoryService.listSubCategoryByCategory(selectedCategory);
		}
		sendResponse(subCategoryList);
		return null;
	}

	/**
	 * Populate product.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String populateProduct() throws Exception {

		List<String> productUnitList = new ArrayList<String>();
		if (!selectedSubCategoryList.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedSubCategoryList))) {
			//productList = productService.findProductListBasedOnSubCategoryCode(selectedSubCategoryList,getBranchId());
			productList=productService.findProductBySubCategoryCode(selectedSubCategoryList);
			/*
			 * for (Product product : productList) {
			 * productUnitList.add(product.getId() + " | " + product.getName());
			 
			 * } }
			 */
			JSONArray productArr = new JSONArray();
			if (!ObjectUtil.isEmpty(productList)) {
				for (Product product : productList) {
					productArr.add(getJSONObject(product.getId(), product.getName()));
					// productArr.add(getJSONObject(product.getId(),
					// product.getName()+" - "+product.getCode()));
				}
			}
			sendAjaxResponse(productArr);
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
	 * Gets the list warehouse.
	 * 
	 * @return the list warehouse
	 */
	public Map<Long, String> getListWarehouse() {

		List<Warehouse> warehouseList = locationService.listWarehouse();

		Map<Long, String> warehouseDropDownList = new LinkedHashMap<Long, String>();
		for (Warehouse warehouse : warehouseList) {
			/*
			 * warehouseDropDownList.put(warehouse.getId(), warehouse.getName()
			 * + " -  " + warehouse.getCode());
			 */
			warehouseDropDownList.put(warehouse.getId(), warehouse.getName());
		}
		return warehouseDropDownList;
	}

	/**
	 * Creates the.
	 * 
	 * @return
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public String create() throws Exception {

		String result = "";
		if (warehouseProduct == null && StringUtil.isEmpty(productTotalString)) {
			command = INPUT;
			request.setAttribute(HEADING, getText("warehouseproductslist"));
			setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));
			return INPUT;
		} else {

			// Added for Handling Form ReSubmit - Please See at
			// populateValidation() Method
			if (ObjectUtil.isEmpty(request.getSession()
					.getAttribute(WAREHOUSE_PRODUCT + "_" + WebTransactionAction.IS_FORM_RESUBMIT))) {
				command = INPUT;
				request.setAttribute(HEADING, getText("warehouseproductslist"));
			}

			Warehouse warehouse = locationService.findWarehouseById(Long.valueOf(selectedWarehouse));
			Vendor vendor = productService.findVendorIdById(Long.valueOf(selectedVendorValue));
			boolean isOrderNoExists = false;
		
			
				if (!StringUtil.isEmpty(orderNo))
					isOrderNoExists = productDistributionService.isOrderNoExists(orderNo);
			

			if (!isOrderNoExists) {

				WarehousePayment warehousePayment = new WarehousePayment();
				List<WarehousePaymentDetails> warehousePaymentList = new ArrayList<WarehousePaymentDetails>();
				double goodQty = 0;
				double damagedQty = 0;
				double totalAmount = 0.0;
				double totalQty = 0;

				String[] productArray = productTotalString.split("\\|");
				for (String productData : productArray) {

					String[] productDataArray = productData.split("\\*");
					Product product = productService.findProductById(Long.valueOf(productDataArray[0].trim()));

					if (!ObjectUtil.isEmpty(warehouse) && !ObjectUtil.isEmpty(product)) {
						WarehouseProduct existing = new WarehouseProduct();
						String userName = (String) request.getSession().getAttribute("user");
						if (userName == null)
							userName = "";

						String receiptnumberSeq = StringUtil.isEmpty(receiptNumber)
								? idGenerator.getWarehouseStockEntryReceiptNumberSeq() : receiptNumber;
						receiptNumber = receiptnumberSeq;
						/*
						 * existing =
						 * productDistributionService.findAvailableStock(
						 * warehouse.getId(), product.getId());
						 */

					/*	if (productDataArray.length == 5) {
							existing = productDistributionService
									.findAvailableStockByWarehouseIdSelectedProductSeasonBatchNo(warehouse.getId(),
											product.getId(), selectedSeason, productDataArray[7]);
						} else {*/
							existing = productDistributionService.findAvailableStockByWarehouseIdSelectedProductSeasonBatchNo(
									warehouse.getId(), product.getId(), selectedSeason,productDataArray[7]);
					/*}*/

						if (ObjectUtil.isEmpty(existing)) {
							warehouseProduct = new WarehouseProduct();
							warehouseProduct.setProduct(product);
							warehouseProduct.setWarehouse(warehouse);
							warehouseProduct.setTxnQty(Double.parseDouble(productDataArray[1]));
							warehouseProduct.setStock(0);
							warehouseProduct.setEdit(true);
							warehouseProduct.setReceiptNumber(receiptNumber);
							warehouseProduct.setOrderNo(orderNo);
							warehouseProduct.setBranchId(getBranchId());
							warehouseProduct.setUserName(userName);
							warehouseProduct.setCostPrice(Double.valueOf(productDataArray[2]));
							warehouseProduct.setVendorId(vendor.getVendorId());
							warehouseProduct.setDamagedQty(!StringUtil.isEmpty(productDataArray[3])
									? Double.valueOf(productDataArray[3]) : 0.00);
							warehouseProduct.setTotalAmt(Double.valueOf(productDataArray[4]));
							warehouseProduct.setSeasonCode(getSelectedSeason());

						
								warehouseProduct.setBatchNo(productDataArray[7]);
							
							productDistributionService.editWarehouseProducts(warehouseProduct);
						} else {
							existing.setTxnQty(Double.parseDouble(productDataArray[1]));
							existing.setEdit(true);
							existing.setReceiptNumber(receiptNumber);
							existing.setOrderNo(orderNo);
							existing.setBranchId(getBranchId());
							existing.setUserName(userName);
							existing.setCostPrice(Double.parseDouble(productDataArray[2]));
							existing.setVendorId(vendor.getVendorId());
							existing.setDamagedQty(!StringUtil.isEmpty(productDataArray[3])
									? Double.parseDouble(productDataArray[3]) : 0.00);
							existing.setSeasonCode(getSelectedSeason());

							existing.setTotalAmt(Double.parseDouble(productDataArray[4]));
							productDistributionService.editWarehouseProducts(existing);
						}
					}

					// To build warehouse payment detail object
					WarehousePaymentDetails whPaymentDetails = new WarehousePaymentDetails();
					whPaymentDetails.setWarehousePayment(warehousePayment);
					whPaymentDetails.setProduct(product);
					whPaymentDetails.setCostPrice(Double.parseDouble(productDataArray[2]));
					whPaymentDetails.setStock(Double.parseDouble(productDataArray[1]));
					whPaymentDetails.setDamagedStock(
							!StringUtil.isEmpty(productDataArray[3]) ? Double.parseDouble(productDataArray[3]) : 0);
					whPaymentDetails.setExpDate(!StringUtil.isEmpty(productDataArray[5]) ? productDataArray[5] : "");
					whPaymentDetails.setAmount(Double.parseDouble(productDataArray[4]));
					whPaymentDetails.setReceiptNo(receiptNumber);
						whPaymentDetails.setBranchId(getBranchId());
				
						whPaymentDetails.setBatchNo(productDataArray[7]);
					
					warehousePaymentList.add(whPaymentDetails);

					goodQty = goodQty + Double.parseDouble(productDataArray[1]);

					if (!StringUtil.isEmpty(productDataArray[3]))
						damagedQty = damagedQty + Double.parseDouble(productDataArray[3]);

					/*
					 * totalAmount = totalAmount +
					 * ((Double.parseDouble(productDataArray[1]) +
					 * (StringUtil.isEmpty(productDataArray[3]) ? 0 :
					 * Double.parseDouble(productDataArray[3])))
					 * Double.parseDouble(productDataArray[2]));
					 */

					totalQty = totalQty + Double.parseDouble(productDataArray[6]);
				}
				// Warehouse Payment
				Set<WarehousePaymentDetails> warehousePaymentDetailsset = new HashSet<WarehousePaymentDetails>();
				for (WarehousePaymentDetails warehousePaymentDetails : warehousePaymentList) {
					warehousePaymentDetailsset.add(warehousePaymentDetails);

				}
				warehousePayment.setTrxnDate(DateUtil.convertStringToDate(getStartDate(), getGeneralDateFormat()));
				warehousePayment.setTrxnDate(DateUtil.setTimeToDate(warehousePayment.getTrxnDate()));
				warehousePayment.setReceiptNo(receiptNumber);
				warehousePayment.setOrderNo(orderNo);
				warehousePayment.setBranchId(getBranchId());
				warehousePayment.setWarehouse(warehouse);
				warehousePayment.setVendor(vendor);
				warehousePayment.setTotalGoodStock(goodQty);
				warehousePayment.setTotalDamagedStock(!StringUtil.isEmpty(damagedQty) ? damagedQty : 0);
				warehousePayment.setTotalAmount(totalAmount);
				warehousePayment.setTax(taxValue);
				warehousePayment.setFinalAmount(creditAmount);
				if ("0".equals(paymentMode)) {
					warehousePayment.setPaymentMode(ESEAccount.PAYMENT_MODE_CASH);
					warehousePayment.setPaymentAmount(paymentAmount);
				} else if ("1".equals(paymentMode)) {
					warehousePayment.setPaymentMode(ESEAccount.PAYMENT_MODE_CREDIT);
					warehousePayment.setPaymentAmount(creditAmount);
				}
				warehousePayment.setRemarks(remarks);
				warehousePayment.setRevisionNo(DateUtil.getRevisionNumber());
				warehousePayment.setWarehousePaymentDetails(warehousePaymentDetailsset);
				warehousePayment.setReceiptNo(receiptNumber);
				warehousePayment.setTotalQty(totalQty);
				warehousePayment.setSeasonCode(getSelectedSeason());

				productDistributionService.processWarehousePaymentStock(warehousePayment);

				if (StringUtil.isEmpty(result)) {

				String receiptHtml = "<a href=\"javascript:printReceipt(\\'" + receiptNumber + 
				                  "\\')\" class ='btn btn-default btnBorderRadius' onclick='printReceipt(\"" + receiptNumber + "\")'>" +
				                  getText("printReceipt") + "</button>";
				                 
			    setStockDescription("<h5>"
				                        + getText("receiptNumber") + " : " + receiptNumber+ "</h5>" + receiptHtml + "</br>");
				printAjaxResponse(getStockDescription(), "text/html");
				response.getWriter().print(result);
				}

			}
			list();
			return INPUT;
		}
	}

	public Map<String, String> getUomList() {
		Map<String, String> uomMap = new LinkedHashMap<String, String>();
		try {
			List<FarmCatalogue> uomList = farmerService.listCatelogueType(getText("uomType"));
			uomList.stream().collect(Collectors.toMap((FarmCatalogue::getCode), FarmCatalogue::getName)).entrySet()
					.stream().sorted(Map.Entry.comparingByValue(new Comparator<String>() {
						@Override
						public int compare(String o1, String o2) {

							return o1.compareToIgnoreCase(o2);
						}
					})).forEachOrdered(e -> uomMap.put(e.getKey(), e.getValue()));
		} catch (Exception e) {
			e.printStackTrace();

		}
		return uomMap;
	}

	public Map<String, String> getSeedList() {
		Map<String, String> seedListMap = new LinkedHashMap<String, String>();
		try {
			List<FarmCatalogue> seedList = farmerService.listCatelogueType(getText("farmCropType"));
			seedList.stream().collect(Collectors.toMap((FarmCatalogue::getCode), FarmCatalogue::getName)).entrySet()
					.stream().sorted(Map.Entry.comparingByValue(new Comparator<String>() {
						@Override
						public int compare(String o1, String o2) {

							return o1.compareToIgnoreCase(o2);
						}
					})).forEachOrdered(e -> seedListMap.put(e.getKey(), e.getValue()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return seedListMap;
	}

	/**
	 * @see com.sourcetrace.esesw.view.SwitchAction#list()
	 */
	public String list() throws Exception {
		ESESystem preferences = preferncesService.findPrefernceById("1");
		setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));
		return LIST;
	}

	public String detail() {

		String view = "";

		ESESystem preferences = preferncesService.findPrefernceById("1");
		DateFormat genDate = new SimpleDateFormat(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));

		if (id != null && !id.equals("")) {
			filter = productDistributionService.findwarehousePaymentById(Long.valueOf(id));

			if (!ObjectUtil.isEmpty(filter)) {
				if (!ObjectUtil.isEmpty(preferences)) {
					Date txnDat = filter.getTrxnDate();
					String txnDatTim = DateUtil.convertDateToString(txnDat, getGeneralDateFormat().concat(""));
					filter.setTransactionDate(!ObjectUtil.isEmpty(filter.getTrxnDate()) ? txnDatTim : "");
					finalAmount = String.valueOf(Double.valueOf(filter.getFinalAmount())
							.longValue()); /*
											 * double to String -->bcoz
											 * exponential issue fixed
											 */
					totalAmount = String.valueOf(Double.valueOf(filter.getTotalAmount()).longValue());
				}
				payementDetailList = new LinkedList<WarehousePaymentDetails>();

				Set<WarehousePaymentDetails> wpDetailSet = new HashSet<WarehousePaymentDetails>();
				for (WarehousePaymentDetails paymentDetails : filter.getWarehousePaymentDetails()) {
					Object[] stockDetail = productDistributionService
							.findAvailableStockAndDamagedStockByWarehouseIdProductIdSeasonBatchNo(
									paymentDetails.getWarehousePayment().getWarehouse().getId(),
									paymentDetails.getProduct().getId(),
									paymentDetails.getWarehousePayment().getSeasonCode(), paymentDetails.getBatchNo());
					if (!ObjectUtil.isEmpty(stockDetail) && stockDetail.length > 0) {
						paymentDetails.setAvlStock(Double.parseDouble(stockDetail[0].toString()));
						paymentDetails.setDamagedAvlStock(Double.parseDouble(stockDetail[1].toString()));
						paymentDetails.setWarehouseProductId(Long.valueOf(stockDetail[2].toString()));
					}
					wpDetailSet.add(paymentDetails);
				}
				payementDetailList.addAll(wpDetailSet);

			}
			if (filter == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}

			setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));
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

	public String update() throws Exception {

		ESESystem preferences = preferncesService.findPrefernceById("1");
		DateFormat genDate = new SimpleDateFormat(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
		if (id != null && !id.equals("")) {
			filter = productDistributionService.findwarehousePaymentById(Long.valueOf(id));
			if (!ObjectUtil.isEmpty(filter)) {
				filter.setTransactionDate(!ObjectUtil.isEmpty(filter.getTrxnDate())
						? DateUtil.convertDateToString(filter.getTrxnDate(), getGeneralDateFormat().concat(""))
						: "");
				payementDetailList = new LinkedList<WarehousePaymentDetails>();
				Set<WarehousePaymentDetails> wpDetailSet = new HashSet<WarehousePaymentDetails>();
				for (WarehousePaymentDetails paymentDetails : filter.getWarehousePaymentDetails()) {
					Object[] stockDetail = productDistributionService
							.findAvailableStockAndDamagedStockByWarehouseIdProductIdSeasonBatchNo(
									paymentDetails.getWarehousePayment().getWarehouse().getId(),
									paymentDetails.getProduct().getId(),
									paymentDetails.getWarehousePayment().getSeasonCode(), paymentDetails.getBatchNo());
					if (!ObjectUtil.isEmpty(stockDetail) && stockDetail.length > 0) {
						paymentDetails.setAvlStock(Double.parseDouble(stockDetail[0].toString()));
						paymentDetails.setDamagedAvlStock(Double.parseDouble(stockDetail[1].toString()));
						paymentDetails.setWarehouseProductId(Long.valueOf(stockDetail[2].toString()));
					}
					wpDetailSet.add(paymentDetails);
				}
				payementDetailList.addAll(wpDetailSet);
			}
			if (filter == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}

			setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));
			id = null;
			setCurrentPage(getCurrentPage());
			command = UPDATE;
			request.setAttribute(HEADING, getText(UPDATE));
		} else {
			Set<WarehousePaymentDetails> paymentDetails = formPayementDetail();
			if (!ObjectUtil.isListEmpty(paymentDetails)) {
				WarehousePayment existingWarehousePayement = null;

				double totalGoodQty = 0;
				double totalDamagedQty = 0;
				double totalAmount = 0;

				for (WarehousePaymentDetails wpDetails : paymentDetails) {
					totalGoodQty += wpDetails.getStock();
					totalDamagedQty += wpDetails.getDamagedStock();
					WarehousePaymentDetails existingWarehousePaymentDetails = productDistributionService
							.findwarehousePaymentDetailById(wpDetails.getId());
					if (!ObjectUtil.isEmpty(existingWarehousePaymentDetails)) {
						double existingTxnGoodStock = existingWarehousePaymentDetails.getStock();
						double existingTxnDamagedStock = existingWarehousePaymentDetails.getDamagedStock();
						existingWarehousePaymentDetails.setStock(wpDetails.getStock());
						existingWarehousePaymentDetails.setDamagedStock(wpDetails.getDamagedStock());
						existingWarehousePaymentDetails.setTotalStock(wpDetails.getTotalStock().longValue());
						existingWarehousePaymentDetails.setAmount((wpDetails.getStock() + wpDetails.getDamagedStock())
								* existingWarehousePaymentDetails.getCostPrice());
						// totalAmount +=
						// existingWarehousePaymentDetails.getAmount();
						productService.updatePayementDetails(existingWarehousePaymentDetails);
						WarehouseProduct existingProduct = productDistributionService
								.findwarehouseProductById(wpDetails.getWarehouseProductId());
						if (!ObjectUtil.isEmpty(existingProduct)) {
							existingProduct.setStock(
									(existingProduct.getStock() - existingTxnGoodStock) + wpDetails.getStock());
							existingProduct.setDamagedQty((existingProduct.getDamagedQty() - existingTxnDamagedStock)
									+ wpDetails.getDamagedStock());
							productDistributionService.update(existingProduct);
						}
						WarehouseProductDetail productDetail = productDistributionService
								.findwarehouseProductDetailByWarehouseProductIdReceiptNo(
										wpDetails.getWarehouseProductId(),
										existingWarehousePaymentDetails.getReceiptNo());
						if (!ObjectUtil.isEmpty(productDetail)) {
							productDetail.setPrevStock(wpDetails.getAvlStock() - existingTxnGoodStock);
							productDetail.setTxnStock(wpDetails.getStock());
							productDetail.setFinalStock(
									(wpDetails.getAvlStock() - existingTxnGoodStock) + wpDetails.getStock());
							productDetail.setDamagedQty((wpDetails.getDamagedAvlStock() - existingTxnDamagedStock)
									+ wpDetails.getDamagedStock());
							productDistributionService.updateWarehouseProductDetail(productDetail);
						}
					}
				}
				for (WarehousePaymentDetails wpDetails : paymentDetails) {
					existingWarehousePayement = productDistributionService
							.findwarehousePaymentById(paymentDetails.iterator().next().getPaymentId());
					String nowDate = DateUtil.convertDateToString(new Date(), getGeneralDateFormat());
					existingWarehousePayement
							.setTrxnDate(DateUtil.convertStringToDate(nowDate, getGeneralDateFormat()));
					existingWarehousePayement
							.setTrxnDate(DateUtil.setTimeToDate(existingWarehousePayement.getTrxnDate()));
					// existingWarehousePayement.setTrxnDate(new Date());
					existingWarehousePayement.setTotalGoodStock(totalGoodQty);
					existingWarehousePayement.setTotalDamagedStock(totalDamagedQty);
					existingWarehousePayement.setTotalQty(totalGoodQty + totalDamagedQty);
					existingWarehousePayement.setTotalAmount(totalAmount);

					existingWarehousePayement
							.setFinalAmount((existingWarehousePayement.getTax() / 100 * totalAmount) + totalAmount);
					productService.updateWarehousePayement(existingWarehousePayement);
					break;
				}

			}
			request.setAttribute(HEADING, getText(LIST));
			return LIST;
		}
		return super.execute();

	}

	private Set<WarehousePaymentDetails> formPayementDetail() {
		// TODO Auto-generated method stub
		Set<WarehousePaymentDetails> paymentDetails = new LinkedHashSet<WarehousePaymentDetails>();
		if (!StringUtil.isEmpty(stockArrJson)) {
			Type distributionDetailType = new TypeToken<List<WarehousePaymentDetails>>() {
			}.getType();
			List<WarehousePaymentDetails> warehousePaymentDetails = new Gson().fromJson(stockArrJson,
					distributionDetailType);
			paymentDetails.addAll(warehousePaymentDetails);
		}
		return paymentDetails;
	}

	/**
	 * Gets the current date.
	 * 
	 * @return the current date
	 */
	public String getCurrentDate() {

		Calendar currentDate = Calendar.getInstance();
		// DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT_1);
		DateFormat df = new SimpleDateFormat(getGeneralDateFormat());
		return df.format(DateUtil.setTimeToDate(new Date()));

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
	 * Gets the selected category.
	 * 
	 * @return the selected category
	 */
	public String getSelectedCategory() {

		return selectedCategory;
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
	 * Gets the selected sub category.
	 * 
	 * @return the selected sub category
	 */
	public String getSelectedSubCategory() {

		return selectedSubCategory;
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
	 * Gets the warehouse product.
	 * 
	 * @return the warehouse product
	 */
	public WarehouseProduct getWarehouseProduct() {

		return warehouseProduct;
	}

	/**
	 * Sets the warehouse product.
	 * 
	 * @param warehouseProduct
	 *            the new warehouse product
	 */
	public void setWarehouseProduct(WarehouseProduct warehouseProduct) {

		this.warehouseProduct = warehouseProduct;
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
	 * Gets the product service.
	 * 
	 * @return the product service
	 */
	public IProductService getProductService() {

		return productService;
	}

	/**
	 * Gets the category service.
	 * 
	 * @return the category service
	 */
	public ICategoryService getCategoryService() {

		return categoryService;
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
	 * Gets the list category.
	 * 
	 * @return the list categorys
	 */
	public List<Category> getListCategory() {

		List<Category> listCategory = categoryService.listCategory();
		return listCategory;
	}

	/**
	 * Gets the sub category list.
	 * 
	 * @return the sub category list
	 */
	public List<SubCategory> getSubCategoryList() {

		if (!StringUtil.isEmpty(selectedCategory)) {
			subCategoryList = categoryService.findSubCategoryByCategoryCode(selectedCategory);
		}
		return subCategoryList;
	}

	/**
	 * Sets the sub category list.
	 * 
	 * @param subCategoryList
	 *            the new sub category list
	 */
	public void setSubCategoryList(List<SubCategory> subCategoryList) {

		this.subCategoryList = subCategoryList;
	}

	/**
	 * Sets the stock description.
	 * 
	 * @param stockDescription
	 *            the new stock description
	 */
	public void setStockDescription(String stockDescription) {

		this.stockDescription = stockDescription;
	}

	/**
	 * Gets the stock description.
	 * 
	 * @return the stock description
	 */
	public String getStockDescription() {

		return stockDescription;
	}

	/**
	 * Gets the product list.
	 * 
	 * @return the product list
	 */
	public List<Product> getProductList() {

		/*return productService.findProductListByBranch(getBranchId()).stream()
				.collect(Collectors.toMap(Product::getId, Product::getName));*/
		return productList;

	}

	public Map<Long, String> getProductList1() {

		Map<Long, String> productsList1 = new LinkedHashMap<Long, String>();
		productList = productService.findProductList();
		for (Product product : productList) {
			productsList1.put(product.getId(), product.getName());
		}
		return productsList1;

	}

	/**
	 * Sets the product list.
	 * 
	 * @param productList
	 *            the new product list
	 */
	public void setProductList(List<Product> productList) {

		this.productList = productList;
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
	 * @see com.sourcetrace.esesw.view.WebTransactionAction#getData()
	 */
	@Override
	public Object getData() {

		setSeasonName(getSelectedSeason() + "-" + getCurrentSeasonsName());
		if (!ObjectUtil.isEmpty(warehouseProduct)) {
			Category category = new Category();
			category.setCode(selectedCategory);

			setOrderNo(orderNo);

			SubCategory subCategory = new SubCategory();
			subCategory.setName(selectedSubCategory);
			subCategory.setCategory(category);

			Product product = new Product();
			if (!StringUtil.isEmpty(selectedProduct))
				// product.setId(Long.valueOf(selectedProduct));
				product.setCode(selectedProduct);
			product.setSubcategory(subCategory);

			warehouseProduct.setWarehouse(locationService.findWarehouseByCode(selectedWarehouse));
			warehouseProduct.setProduct(product);
		}

		cashType.put(0, getText("cashType1"));
		cashType.put(1, getText("cashType2"));

		listExpMnth = expMonthList("expMonth", listExpMnth);

		setSelectedCategory(getText("defaultCategoryCode"));
		return warehouseProduct;
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
	 * Sets the stock.
	 * 
	 * @param stock
	 *            the new stock
	 */
	public void setStock(String stock) {

		this.stock = stock;
	}

	/**
	 * Gets the stock.
	 * 
	 * @return the stock
	 */
	public String getStock() {

		return stock;
	}

	/**
	 * Sets the security action.
	 * 
	 * @param securityAction
	 *            the new security action
	 */
	public void setSecurityAction(String securityAction) {

		this.securityAction = securityAction;
	}

	/**
	 * Gets the security action.
	 * 
	 * @return the security action
	 */
	public String getSecurityAction() {

		return securityAction;
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
	 * Gets the selected vendor.
	 * 
	 * @return the selected vendor
	 */
	public String getSelectedVendor() {

		return selectedVendor;
	}

	/**
	 * Sets the selected vendor.
	 * 
	 * @param selectedVendor
	 *            the new selected vendor
	 */
	public void setSelectedVendor(String selectedVendor) {

		this.selectedVendor = selectedVendor;
	}

	/**
	 * Gets the batchno.
	 * 
	 * @return the batchno
	 */
	public String getBatchno() {

		return batchno;
	}

	/**
	 * Sets the batchno.
	 * 
	 * @param batchno
	 *            the new batchno
	 */
	public void setBatchno(String batchno) {

		this.batchno = batchno;
	}

	/**
	 * Set vendor list.
	 * 
	 * @param vendorList
	 */
	public void setVendorList(Map<String, String> vendorList) {

		this.vendorList = vendorList;
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
	 * Sets the product total string.
	 * 
	 * @param productTotalString
	 *            the new product total string
	 */
	public void setProductTotalString(String productTotalString) {

		this.productTotalString = productTotalString;
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
	 * Sets the receipt number.
	 * 
	 * @param receiptNumber
	 *            the new receipt number
	 */
	public void setReceiptNumber(String receiptNumber) {

		this.receiptNumber = receiptNumber;
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
	 * Sets the id generator.
	 * 
	 * @param idGenerator
	 *            the new id generator
	 */
	public void setIdGenerator(IUniqueIDGenerator idGenerator) {

		this.idGenerator = idGenerator;
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
	 * Sets the start date.
	 * 
	 * @param startDate
	 *            the new start date
	 */
	public void setStartDate(String startDate) {

		this.startDate = startDate;
	}

	/**
	 * Gets the order no.
	 * 
	 * @return the order no
	 */
	public String getOrderNo() {

		return orderNo;
	}

	/**
	 * Sets the order no.
	 * 
	 * @param orderNo
	 *            the new order no
	 */
	public void setOrderNo(String orderNo) {

		this.orderNo = orderNo;
	}

	/**
	 * Gets the product available stock.
	 * 
	 * @return the product available stock
	 */
	public String getProductAvailableStock() {

		return productAvailableStock;
	}

	/**
	 * Sets the product available stock.
	 * 
	 * @param productAvailableStock
	 *            the new product available stock
	 */
	public void setProductAvailableStock(String productAvailableStock) {

		this.productAvailableStock = productAvailableStock;
	}

	public void populateProductAvailableStock() throws Exception {

		String result = EMPTY_STOCK;

		if (!StringUtil.isEmpty(selectedWarehouse) && !StringUtil.isEmpty(selectedProduct)
				&& !StringUtil.isEmpty(selectedSeason)) {
			/*
			 * WarehouseProduct warehouseProduct =
			 * productDistributionService.findAvailableStock(Long
			 * .valueOf(selectedWarehouse), Long.valueOf(selectedProduct));
			 */
			WarehouseProduct warehouseProduct = null;
			if (StringUtil.isEmpty(batchno)) {
				warehouseProduct = productDistributionService.findAvailableStockByWarehouseIdSelectedProductSeason(
						Long.valueOf(selectedWarehouse), Long.valueOf(selectedProduct), selectedSeason);

			} else {
				warehouseProduct = productDistributionService
						.findAvailableStockByWarehouseIdSelectedProductSeasonBatchNo(Long.valueOf(selectedWarehouse),
								Long.valueOf(selectedProduct), selectedSeason, batchno);

			}

			if (!ObjectUtil.isEmpty(warehouseProduct)) {
				productAvailableStock = String.valueOf(warehouseProduct.getStock());

				result = productAvailableStock;
			} else {
				productAvailableStock = EMPTY_STOCK;

			}

			result = productAvailableStock;

		}
		response.getWriter().print(result);
	}

	/**
	 * Gets the list vendor.
	 * 
	 * @return the list vendor
	 */
	public Map<Long, String> getListVendor() {

		List<Vendor> vendorValueList = productDistributionService.listVendor();

		Map<Long, String> vendorValue = new LinkedHashMap<Long, String>();
		for (Vendor vendor : vendorValueList) {
			// vendorValue.put(vendor.getId(), vendor.getVendorId() + " - " +
			// vendor.getVendorName());
			vendorValue.put(vendor.getId(), vendor.getVendorName());
		}
		return vendorValue;
	}

	/**
	 * Gets the category category list.
	 * 
	 * @return the category category list
	 */
	public Map<String, String> getCategoryCategoryList() {

		Map<String, String> subCategoryyyList = new LinkedHashMap<String, String>();
		categoryCategoryList = categoryService.listSubCategoryByDefaultCategory();
		for (SubCategory subCategory : categoryCategoryList) {
			subCategoryyyList.put(String.valueOf(subCategory.getCode()),subCategory.getName());
		}
		return subCategoryyyList;

	}

	/**
	 * Gets the selected sub category list.
	 * 
	 * @return the selected sub category list
	 */
	public String getSelectedSubCategoryList() {

		return selectedSubCategoryList;
	}

	/**
	 * Sets the selected sub category list.
	 * 
	 * @param selectedSubCategoryList
	 *            the new selected sub category list
	 */
	public void setSelectedSubCategoryList(String selectedSubCategoryList) {

		this.selectedSubCategoryList = selectedSubCategoryList;
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
	 * Set cash type.
	 * 
	 * @param cashType
	 */
	public void setCashType(Map<Integer, String> cashType) {

		this.cashType = cashType;
	}

	/**
	 * Gets the selected vendor id.
	 * 
	 * @return the selected vendor id
	 */
	public String getSelectedVendorId() {

		return selectedVendorId;
	}

	/**
	 * Sets the selected vendor id.
	 * 
	 * @param selectedVendorId
	 *            the new selected vendor id
	 */
	public void setSelectedVendorId(String selectedVendorId) {

		this.selectedVendorId = selectedVendorId;
	}

	/**
	 * Gets the selected vendor value.
	 * 
	 * @return the selected vendor value
	 */
	public String getSelectedVendorValue() {

		return selectedVendorValue;
	}

	/**
	 * Sets the selected vendor value.
	 * 
	 * @param selectedVendorValue
	 *            the new selected vendor value
	 */
	public void setSelectedVendorValue(String selectedVendorValue) {

		this.selectedVendorValue = selectedVendorValue;
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
	 * Gets the account service.
	 * 
	 * @return the account service
	 */
	public IAccountService getAccountService() {

		return accountService;
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
	 * Gets the account.
	 * 
	 * @return the account
	 */
	public ESEAccount getAccount() {

		return account;
	}

	/**
	 * Sets the account.
	 * 
	 * @param account
	 *            the new account
	 */
	public void setAccount(ESEAccount account) {

		this.account = account;
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
	 * Gets the trxn date.
	 * 
	 * @return the trxn date
	 */
	public String getTrxnDate() {

		return trxnDate;
	}

	/**
	 * Sets the trxn date.
	 * 
	 * @param trxnDate
	 *            the new trxn date
	 */
	public void setTrxnDate(String trxnDate) {

		this.trxnDate = trxnDate;
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
	 * Gets the payment amount.
	 * 
	 * @return the payment amount
	 */
	public double getPaymentAmount() {

		return paymentAmount;
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
	 * Gets the remarks.
	 * 
	 * @return the remarks
	 */
	public String getRemarks() {

		return remarks;
	}

	/**
	 * Sets the remarks.
	 * 
	 * @param remarks
	 *            the new remarks
	 */
	public void setRemarks(String remarks) {

		this.remarks = remarks;
	}

	/**
	 * Gets the list exp mnth.
	 * 
	 * @return the list exp mnth
	 */
	public Map<Integer, String> getListExpMnth() {

		return listExpMnth;
	}

	/**
	 * Set list exp mnth.
	 * 
	 * @param listExpMnth
	 */
	public void setListExpMnth(Map<Integer, String> listExpMnth) {

		this.listExpMnth = listExpMnth;
	}

	/**
	 * Gets the product available unit.
	 * 
	 * @return the product available unit
	 */
	public String getProductAvailableUnit() {

		return productAvailableUnit;
	}

	/**
	 * Sets the product available unit.
	 * 
	 * @param productAvailableUnit
	 *            the new product available unit
	 */
	public void setProductAvailableUnit(String productAvailableUnit) {

		this.productAvailableUnit = productAvailableUnit;
	}

	/**
	 * Gets the warehouse map.
	 * 
	 * @return the warehouse map
	 */
	public Map<String, Object> getWarehouseMap() {

		return warehouseMap;
	}

	/**
	 * Set warehouse map.
	 * 
	 * @param warehouseMap
	 */
	public void setWarehouseMap(Map<String, Object> warehouseMap) {

		this.warehouseMap = warehouseMap;
	}

	public void populateVendorAccBalance() throws Exception {

		String cashAndCredit = "";
		if (!StringUtil.isEmpty(selectedVendorValue)) {
			Vendor vendor = productService.findVendorIdById(Long.valueOf(selectedVendorValue));
			if (!StringUtil.isEmpty(vendor.getVendorId())) {
				account = accountService.findAccountByVendorIdAndType(vendor.getVendorId(), ESEAccount.VENDOR_ACCOUNT);
				if (!StringUtil.isEmpty(account)) {
					cashCreditValue = (account.getCashBalance() + "," + account.getCreditBalance());
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
	 * Gets the list exp year.
	 * 
	 * @return the list exp year
	 */
	public List<String> getListExpYear() {

		int curYear = DateUtil.getCurrentYear();
		int startYear = curYear;
		int endYear = curYear + 50;
		List<String> list = new ArrayList<String>();
		for (int i = startYear; i < endYear; i++) {
			list.add(String.valueOf(i));
		}
		return list;
	}

	private Map<Integer, String> expMonthList(String keyProperty, Map<Integer, String> enrollmentMap) {

		listExpMnth = new LinkedHashMap();
		String values = getText(keyProperty);
		if (!StringUtil.isEmpty(values)) {
			String[] valuesArray = values.split(",");
			int i = 1;
			for (String value : valuesArray) {
				listExpMnth.put(i++, value);
			}
		}
		return listExpMnth;
	}

	public void populatePopulateProductUnit() throws Exception {

		String result = "";
		String costPrice = "0.0";
		JSONArray productArr = new JSONArray();
		if (!StringUtil.isEmpty(selectedProduct)) {
			Product product = productService.findProductUnitByProductId(Long.valueOf(selectedProduct));

			if (!ObjectUtil.isEmpty(product)) {
				costPrice = (!StringUtil.isEmpty(product.getPrice()) ? product.getPrice() : "0.0");
			}

			if (!ObjectUtil.isEmpty(product) && product.getType() != null) {
				productAvailableUnit = String.valueOf(product.getType().getName());
				result = productAvailableUnit;
			}

			result = productAvailableUnit == null ? "" : productAvailableUnit;
		}
		productArr.add(getJSONObject("unit", result));
		productArr.add(getJSONObject("costPrice", costPrice));
		sendAjaxResponse(productArr);
	}

	public String populatePrintHTML() {

		initializeDistributionPrintMap();
		if (!StringUtil.isEmpty(receiptNumber)) {
			WarehousePayment warehousePayment = productDistributionService.findWarehouseStockByRecNo(receiptNumber);
			buildTransactionPrintMap(warehousePayment);
		}
		return "html";
	}

	private void initializeDistributionPrintMap() {

		this.warehouseMap = new HashMap<String, Object>();
		List<Map<String, Object>> productMapList = new ArrayList<Map<String, Object>>();
		Map<String, Object> totalMap = new LinkedHashMap<String, Object>();

		ESESystem preferences = preferncesService.findPrefernceById("1");
		setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));

		this.warehouseMap.put("recNo", "");
		this.warehouseMap.put("vId", "");
		this.warehouseMap.put("vName", "");
		this.warehouseMap.put("village", "");
		this.warehouseMap.put("date", "");
		this.warehouseMap.put("productMapList", productMapList);
		if (enableBatchNo.equals("1")) {
			this.warehouseMap.put("batchNo", "");
		}
		this.warehouseMap.put("finalAmount", "");
		this.warehouseMap.put("totalAmt", "");
		this.warehouseMap.put("totalTax", "");
		this.warehouseMap.put("totalQty", "");
		this.warehouseMap.put("totalStockVal", "");
		// this.warehouseMap.put("vendorCash", "");
		// this.warehouseMap.put("vendorCredit", "");
		this.warehouseMap.put("payMode", "");
		this.warehouseMap.put("payAmt", "");
		this.warehouseMap.put("credAmt", "");
		this.warehouseMap.put("remarks", "");
	}

	private void buildTransactionPrintMap(WarehousePayment warehousePayment) {

		List<Map<String, Object>> productMapList = new ArrayList<Map<String, Object>>();
		if (!ObjectUtil.isEmpty(warehousePayment)) {
			double totalQuantity = 0d;
			// double totalPricePerUnit = 0d;
			// double totalAmount = 0d;
			double tax = 0d;
			double finalAmount = 0d;
			// double totalQtyStock = 0d;
			double totalStockAmt = 0d;
			// DateFormat df = new SimpleDateFormat(RECEIPT_DATE_FORMAT);
			DateFormat df = new SimpleDateFormat(getGeneralDateFormat());

			totalQuantity += warehousePayment.getTotalAmount();
			tax += warehousePayment.getTax();
			finalAmount += warehousePayment.getFinalAmount();

			// ESEAccount eseAccount =
			// accountService.findAccountByVendorIdAndType(warehousePayment.getVendor().getVendorId(),ESEAccount.VENDOR_ACCOUNT);

			// if (!ObjectUtil.isEmpty(warehousePayment.getAgroTransaction())) {
			if (!StringUtil.isEmpty(warehousePayment.getReceiptNo())) {
				this.warehouseMap.put("recNo", warehousePayment.getReceiptNo());
			}
			if (!ObjectUtil.isEmpty(warehousePayment.getVendor())
					&& !StringUtil.isEmpty(warehousePayment.getVendor().getVendorId())) {
				this.warehouseMap.put("vId", warehousePayment.getVendor().getVendorId());
			}
			if (!ObjectUtil.isEmpty(warehousePayment.getVendor())
					&& !StringUtil.isEmpty(warehousePayment.getVendor().getVendorName())) {
				this.warehouseMap.put("vName", warehousePayment.getVendor().getVendorName());
			}
			if (!ObjectUtil.isEmpty(warehousePayment.getOrderNo())
					&& !StringUtil.isEmpty(warehousePayment.getOrderNo())) {
				this.warehouseMap.put("orderNo", warehousePayment.getOrderNo());
			}
			if (!ObjectUtil.isEmpty(warehousePayment.getTrxnDate())) {
				Date txnPayment = DateUtil.convertStringToDate(df.format(warehousePayment.getTrxnDate()),
						getGeneralDateFormat());
				// this.warehouseMap.put("date",
				// df.format(warehousePayment.getTrxnDate()));
				this.warehouseMap.put("date", DateUtil.setTimeToDate(txnPayment));

			}

			ESESystem preferences = preferncesService.findPrefernceById("1");
			setEnableBatchNo(preferncesService.findPrefernceByName(ESESystem.ENABLE_BATCH_NO));
			/*
			 * if(enableBatchNo.equals("1")){ if
			 * (!ObjectUtil.isEmpty(warehousePayment.getWarehouse()) &&
			 * !StringUtil.isEmpty(warehousePayment.getWarehouse().
			 * getWarehouseProducts().iterator().next().getBatchNo())) {
			 * this.warehouseMap.put("batchNo",
			 * warehousePayment.getWarehouse().getWarehouseProducts().iterator()
			 * .next().getBatchNo()); } }
			 */

			this.warehouseMap.put("warehouseCode", warehousePayment.getWarehouse().getCode());
			this.warehouseMap.put("warehouseName", warehousePayment.getWarehouse().getName());
			this.warehouseMap.put("totalAmt", CurrencyUtil.getDecimalFormat(totalQuantity, "##.00"));
			this.warehouseMap.put("totalTax", CurrencyUtil.getDecimalFormat(Double.valueOf(tax), "##00"));
			this.warehouseMap.put("finalAmount", formatter.format(finalAmount));

			if (warehousePayment.getPaymentMode().equals("CS"))
				this.warehouseMap.put("payMode", "Cash");
			this.warehouseMap.put("payAmt",
					CurrencyUtil.getDecimalFormat(warehousePayment.getPaymentAmount(), "##.00"));
			if (warehousePayment.getPaymentMode().equals("CR"))
				this.warehouseMap.put("payMode", "Credit");
			this.warehouseMap.put("credAmt", CurrencyUtil.getDecimalFormat(warehousePayment.getFinalAmount(), "##.00"));
			this.warehouseMap.put("remarks", warehousePayment.getRemarks());
			// this.warehouseMap.put("vendorCash",
			// CurrencyUtil.getDecimalFormat(eseAccount.getCashBalance(),"##.00"));
			// this.warehouseMap.put("vendorCredit",
			// CurrencyUtil.getDecimalFormat(eseAccount.getCreditBalance(),"##.00"));
			// }

			this.warehouseMap.put("paymentAmout", CurrencyUtil.thousandSeparator(warehousePayment.getPaymentAmount()));
			this.warehouseMap.put("productMapList", productMapList);
			if (!ObjectUtil.isListEmpty(warehousePayment.getWarehousePaymentDetails())) {
				for (WarehousePaymentDetails warehousePaymentDetails : warehousePayment.getWarehousePaymentDetails()) {
					Map<String, Object> productMap = new LinkedHashMap<String, Object>();
					String productName = "";
					String categoryName = "";
					String proUnit = "";
					if (!ObjectUtil.isEmpty(warehousePaymentDetails.getProduct()))
						productName = !StringUtil.isEmpty(warehousePaymentDetails.getProduct().getName())
								? warehousePaymentDetails.getProduct().getName() : "";

					if (!ObjectUtil.isEmpty(warehousePaymentDetails.getProduct().getSubcategory()))
						categoryName = !StringUtil
								.isEmpty(warehousePaymentDetails.getProduct().getSubcategory().getName())
										? warehousePaymentDetails.getProduct().getSubcategory().getName() : "";

					if (!ObjectUtil.isEmpty(warehousePaymentDetails.getProduct()))
						proUnit = !StringUtil.isEmpty(warehousePaymentDetails.getProduct().getUnit())
								? warehousePaymentDetails.getProduct().getUnit() : "";

					productMap.put("category", categoryName);
					productMap.put("product", productName);
					productMap.put("unit", proUnit);
					productMap.put("batchNo", warehousePaymentDetails.getBatchNo());
					productMap.put("expDate", DateUtil.setTimeToDate(DateUtil
							.convertStringToDate(warehousePaymentDetails.getExpDate(), getGeneralDateFormat())));
					productMap.put("costPrice",
							CurrencyUtil.getDecimalFormat(warehousePaymentDetails.getCostPrice(), "##.00"));
					productMap.put("goodQty",
							CurrencyUtil.getDecimalFormat(warehousePaymentDetails.getStock(), "##.000"));
					productMap.put("badQty", !StringUtil.isEmpty(warehousePaymentDetails.getDamagedStock())
							? CurrencyUtil.getDecimalFormat(warehousePaymentDetails.getDamagedStock(), "##.000") : "");

					totalStockAmt = +(((warehousePaymentDetails.getStock())
							+ (warehousePaymentDetails.getDamagedStock())) * warehousePaymentDetails.getCostPrice());

					productMap.put("totalQty",
							(warehousePaymentDetails.getStock()) + (warehousePaymentDetails.getDamagedStock()));
					productMap.put("totalStockVal", CurrencyUtil.getDecimalFormat(totalStockAmt, "##.00"));
					productMap.put("unitType", !ObjectUtil.isEmpty(warehousePaymentDetails.getProduct().getType())
							? warehousePaymentDetails.getProduct().getType().getName() : "");
					productMapList.add(productMap);
				}
			}

		}
	}

	public double getCreditAmount() {

		return creditAmount;
	}

	public void setCreditAmount(double creditAmount) {

		this.creditAmount = creditAmount;
	}

	public String getSeasonName() {
		return seasonName;
	}

	public void setSeasonName(String seasonName) {
		this.seasonName = seasonName;
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

	public void setHarvestSeasonList(Map<String, String> harvestSeasonList) {
		this.harvestSeasonList = harvestSeasonList;
	}

	public String getSelectedSeason() {
		return selectedSeason;
	}

	public void setSelectedSeason(String selectedSeason) {
		this.selectedSeason = selectedSeason;
	}

	public String getEnableBatchNo() {
		return enableBatchNo;
	}

	public void setEnableBatchNo(String enableBatchNo) {
		this.enableBatchNo = enableBatchNo;
	}

	public String getSelectedBatchNo() {
		return selectedBatchNo;
	}

	public void setSelectedBatchNo(String selectedBatchNo) {
		this.selectedBatchNo = selectedBatchNo;
	}

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	public WarehousePayment getFilter() {
		return filter;
	}

	public void setFilter(WarehousePayment filter) {
		this.filter = filter;
	}

	public List<WarehousePaymentDetails> getPayementDetailList() {
		return payementDetailList;
	}

	public void setPayementDetailList(List<WarehousePaymentDetails> payementDetailList) {
		this.payementDetailList = payementDetailList;
	}

	public double getCurrentStock() {
		return currentStock;
	}

	public void setCurrentStock(double currentStock) {
		this.currentStock = currentStock;
	}

	public String getStockArrJson() {
		return stockArrJson;
	}

	public void setStockArrJson(String stockArrJson) {
		this.stockArrJson = stockArrJson;
	}

	public ICatalogueService getCatalogueService() {
		return catalogueService;
	}

	public void setCatalogueService(ICatalogueService catalogueService) {
		this.catalogueService = catalogueService;
	}

	public String getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(String finalAmount) {
		this.finalAmount = finalAmount;
	}

	public JSONObject populateValidationOfOrderNo() throws Exception {

		Boolean isOrderNoExists = productDistributionService.isOrderNoExists(orderNo);
		if (!isOrderNoExists) {

			sendResponse("false");
		} else {
			sendResponse("true");

		}
		return null;
	}

	public JSONObject populateValidationOfVariety() throws Exception {

		Boolean isVarietyExists = productDistributionService.isVarietyExists(variety);
		if (!isVarietyExists) {

			sendResponse("false");
		} else {
			sendResponse("true");

		}
		return null;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

}
