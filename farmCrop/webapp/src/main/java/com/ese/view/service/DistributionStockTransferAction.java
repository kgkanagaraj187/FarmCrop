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
import com.sourcetrace.eses.order.entity.txn.DistributionStock;
import com.sourcetrace.eses.order.entity.txn.DistributionStockDetail;
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
import com.sourcetrace.esesw.entity.profile.WarehousePaymentDetails;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.view.WebTransactionAction;

// TODO: Auto-generated Javadoc
/**
 * The Class DistributionAction.
 */
public class DistributionStockTransferAction extends WebTransactionAction {
	private String senderWarehouse;
	private String category;
	private String product;
	private String selSenderWarehouse;
	private String receiverWarehouse;
	private String selectedDate;
	private String productTotalString;
	private String receiptNumber;
	private String driverName;
	private String truckId;
	private DistributionStock distributionStock;
	private String distributionDescription;
	private DistributionStockDetail distributionStockDetail;
	private String identityForGrid;
	private String id;
	private String disableBranch;
	private String season;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private IProductService productService;
	@Autowired
	private IUniqueIDGenerator idGenerator;
	@Autowired
	private IProductDistributionService productDistributionService;
	@Autowired
	private IFarmerService farmerService;
	public String list() {
		return LIST;
	}
	public String data() throws Exception {

		Map<String, String> searchRecord = getJQGridRequestParam(); // get the
		DistributionStock filter = new DistributionStock();
		if (getIdentityForGrid() == null){
			
			setIdentityForGrid("distributionStock");
		filter.setTxnType(DistributionStock.DISTRIBUTION_STOCK_TRANSFER);
		filter.setStatus(0);
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
		}
		
		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		return sendJQGridJSONResponse(data);
	}
	public String subGridDetail() throws Exception {
			DistributionStockDetail filter = new DistributionStockDetail();
			DistributionStock ds=new DistributionStock();
			ds.setId(Long.parseLong(id));
			filter.setDistributionStock(ds);
		super.filter = this.filter;
		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());
		
		return sendJQGridJSONResponse(data);
	}
	public JSONObject toJSON(Object obj) {
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		if (identityForGrid == "distributionStock") {
			if (obj instanceof DistributionStock) {
				DistributionStock distributionStock = (DistributionStock) obj;
				if ((getIsMultiBranch().equalsIgnoreCase("1")
						&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

					if (StringUtil.isEmpty(branchIdValue)) {
						rows.add(!StringUtil
								.isEmpty(getBranchesMap().get(getParentBranchMap().get(distributionStock.getBranchId())))
										? getBranchesMap().get(getParentBranchMap().get(distributionStock.getBranchId()))
										: getBranchesMap().get(distributionStock.getBranchId()));
					}
					rows.add(getBranchesMap().get(distributionStock.getBranchId()));
				} else {
					if (StringUtil.isEmpty(branchIdValue)) {
						rows.add(branchesMap.get(distributionStock.getBranchId()));
					}
				}
				ESESystem preferences = preferncesService.findPrefernceById("1");
				if (!StringUtil.isEmpty(preferences)) {
					DateFormat genDate = new SimpleDateFormat(
							preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
					rows.add(!StringUtil.isEmpty(distributionStock.getTxnTime()) ? genDate.format(distributionStock.getTxnTime()) : ""); // Trxn
				}
				rows.add(!ObjectUtil.isEmpty(distributionStock.getSenderWarehouse())&& !StringUtil.isEmpty(distributionStock.getSenderWarehouse().getName())?distributionStock.getSenderWarehouse().getName():"");
				rows.add(!ObjectUtil.isEmpty(distributionStock.getReceiverWarehouse())&& !StringUtil.isEmpty(distributionStock.getReceiverWarehouse().getName())?distributionStock.getReceiverWarehouse().getName():"");
				rows.add(!StringUtil.isEmpty(distributionStock.getTruckId())?distributionStock.getTruckId():"");
				rows.add(!StringUtil.isEmpty(distributionStock.getDriverName())?distributionStock.getDriverName():"");
				Double totQty=0.0;
				totQty=distributionStock.getDistributionStockDetails().stream().mapToDouble(DistributionStockDetail::getDistributionQuantity)
						.sum();
				rows.add(String.valueOf(totQty));
				jsonObject.put("id", distributionStock.getId());
				jsonObject.put("cell", rows);
			}
		}else {
			if (obj instanceof DistributionStockDetail) {
				DistributionStockDetail distributionStockDetail = (DistributionStockDetail) obj;
				if(distributionStockDetail!=null && !ObjectUtil.isEmpty(distributionStockDetail)){
				rows.add(!ObjectUtil.isEmpty(distributionStockDetail.getProduct())&& !ObjectUtil.isEmpty(distributionStockDetail.getProduct().getSubcategory())&& !StringUtil.isEmpty(distributionStockDetail.getProduct().getSubcategory().getName())?distributionStockDetail.getProduct().getSubcategory().getName():"");
				rows.add(!ObjectUtil.isEmpty(distributionStockDetail.getProduct())&& !StringUtil.isEmpty(distributionStockDetail.getProduct().getName())?distributionStockDetail.getProduct().getName():"");
				rows.add(!ObjectUtil.isEmpty(distributionStockDetail.getProduct())&& !StringUtil.isEmpty(distributionStockDetail.getProduct().getUnit())?distributionStockDetail.getProduct().getUnit():"");
				rows.add(distributionStockDetail.getDistributionQuantity());
				jsonObject.put("id", distributionStockDetail.getId());
				jsonObject.put("cell", rows);
				}
			}
		}
			return jsonObject;
	}
	
	public String create() throws Exception {
		return INPUT;
	}
	@SuppressWarnings("unused")
	public String detail() {
		String view = "";
		if (id != null && !id.equals("")) {
			distributionStock = productDistributionService.findDistributionStockById(Long.parseLong(id));
		}
		return DETAIL;
	}
	public Map<String, String> getSeasonList(){
		Map<String, String> seasonMap=new LinkedHashMap<>();
		List<Object[]> seasonList=farmerService.listSeasonCodeAndName();
		if(!ObjectUtil.isListEmpty(seasonList)){
		seasonList.stream().forEach(s -> {
			seasonMap.put(s[0].toString(),s[1].toString());
		});
		}
		return seasonMap;
	}
	public Map<Long, String> getSenderWarehouseList(){
		Map<Long, String> warehouseMap=new LinkedHashMap<>();
		List<Object[]> warehouseList=locationService.listWarehouses();
		if(!ObjectUtil.isListEmpty(warehouseList)){
		warehouseList.stream().forEach(w->{
			String a=String.valueOf((w[2].toString()).concat(w[3].toString()));
			warehouseMap.put(Long.parseLong(w[0].toString()),(w[2].toString())+"-"+(w[3].toString()) ); 
		});
		}
		return warehouseMap;
	}
	
	
	public Map<Long, String> getReceiverWarehouseList(){
		Map<Long, String> warehouseMap=new LinkedHashMap<>();
		setDisableBranch(preferncesService.findPrefernceByName(ESESystem.DISABLE_BRANCH));
		if (!StringUtil.isEmpty(getDisableBranch()) && getDisableBranch().equals("1")) {
			List<Object[]> warehouseList=locationService.listWarehousesWithoutBranch();
			if(!ObjectUtil.isListEmpty(warehouseList)){
				warehouseList.stream().forEach(w->{
					String a=String.valueOf((w[2].toString()).concat(w[3].toString()));
					warehouseMap.put(Long.parseLong(w[0].toString()),(w[2].toString())+"-"+(w[3].toString()) ); 
				});
			}
		}
		else{
			List<Object[]> warehouseList=locationService.listWarehouses();
			if(!ObjectUtil.isListEmpty(warehouseList)){
			warehouseList.stream().forEach(w->{
				warehouseMap.put(Long.parseLong(w[0].toString()), w[2].toString()); 
			});
			}
			
		}
		return warehouseMap;
	}
	
	public void populateProductCategory(){
		JSONArray categoryArr = new JSONArray();
		if(senderWarehouse!=null && season!=null && !StringUtil.isEmpty(senderWarehouse) && !StringUtil.isEmpty(season) ){
		List<Object[]> catList=productService.listCategoriesByWarehouseId(Long.parseLong(senderWarehouse),season);
		if(!ObjectUtil.isListEmpty(catList)){
		catList.stream().distinct().forEach(p->{
			categoryArr.add(getJSONObject(p[0].toString(), p[1].toString()));
		});
		}
		}
		sendAjaxResponse(categoryArr);
	}
	
	public void populateProduct(){
		JSONArray productArr = new JSONArray();
		if(senderWarehouse!=null && category!=null && season!=null && !StringUtil.isEmpty(senderWarehouse) && !StringUtil.isEmpty(category) && !StringUtil.isEmpty(season)){
		List<Product> productList=productService.findProductByWarehouseIdAndSubCategoryCode(Long.parseLong(senderWarehouse),category,season);
		if(!ObjectUtil.isListEmpty(productList)){
		productList.stream().distinct().forEach(p->{
			productArr.add(getJSONObject(p.getId(), p.getName().toString()));
		});
		}
		}
		sendAjaxResponse(productArr);
	}
	public void populateAvailableStock() throws Exception{
		String result = NOT_APPLICABLE;
		Double availableStock=0.0;
		if(senderWarehouse!=null && product!=null && season!=null && !StringUtil.isEmpty(senderWarehouse)&& !StringUtil.isEmpty(product) && !StringUtil.isEmpty(season)){
		availableStock=productService.findAvailableStockByWarehouseIdAndProduct(senderWarehouse,product,season);
		String unit=productService.findProductUnitByProductId(Long.parseLong(product)).getUnit();
		availableStock=!ObjectUtil.isEmpty(availableStock)?availableStock:0.00;
		unit=!StringUtil.isEmpty(unit)?unit:NOT_APPLICABLE;
		result = availableStock + "," + unit;
		}

		response.getWriter().print(result);
	}
	public String getSenderWarehouse() {
		return senderWarehouse;
	}
	public void setSenderWarehouse(String senderWarehouse) {
		this.senderWarehouse = senderWarehouse;
	}
	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public ILocationService getLocationService() {
		return locationService;
	}
	public void setLocationService(ILocationService locationService) {
		this.locationService = locationService;
	}
	public IProductService getProductService() {
		return productService;
	}
	public void setProductService(IProductService productService) {
		this.productService = productService;
	}
	public String getSelSenderWarehouse() {
		return selSenderWarehouse;
	}
	public void setSelSenderWarehouse(String selSenderWarehouse) {
		this.selSenderWarehouse = selSenderWarehouse;
	}
	public void populateDistribution() throws Exception {
		Double avbleStock = 0.0;
		String result = "";
		Warehouse sendWarehouse=null;
		Warehouse receiveWarehouse=null;
		setDisableBranch(preferncesService.findPrefernceByName(ESESystem.DISABLE_BRANCH));
		if (!StringUtil.isEmpty(getDisableBranch()) && getDisableBranch().equals("1")) {
			if (StringUtil.isEmpty(senderWarehouse)) {
				result = "emptySenderWarehouse";
			} else {
				sendWarehouse = locationService.findWarehouseByIdWithoutBranch(Long.parseLong(senderWarehouse.trim()));
				if (sendWarehouse==null || ObjectUtil.isEmpty(sendWarehouse)) {
					result = "senderWarehouseNotExist";
				}
			}
		
			if (StringUtil.isEmpty(receiverWarehouse)) {
				result = "emptyReceiverWarehouse";
			} else {
				receiveWarehouse = locationService.findWarehouseByIdWithoutBranch(Long.parseLong(receiverWarehouse.trim()));
				if (receiveWarehouse==null || ObjectUtil.isEmpty(receiveWarehouse)) {
					result = "receiveWarehouseNotExist";
				}
			}
			if(StringUtil.isEmpty(season)) {
				result="empty.season";
			} 
			
			
		}else{
			if (StringUtil.isEmpty(senderWarehouse)) {
				result = "emptySenderWarehouse";
			} else {
				sendWarehouse = locationService.findWarehouseById(Long.parseLong(senderWarehouse.trim()));
				if (sendWarehouse==null || ObjectUtil.isEmpty(sendWarehouse)) {
					result = "senderWarehouseNotExist";
				}
			}
		
			if (StringUtil.isEmpty(receiverWarehouse)) {
				result = "emptyReceiverWarehouse";
			} else {
				receiveWarehouse = locationService.findWarehouseById(Long.parseLong(receiverWarehouse.trim()));
				if (receiveWarehouse==null || ObjectUtil.isEmpty(receiveWarehouse)) {
					result = "receiveWarehouseNotExist";
				}
			}
			if(StringUtil.isEmpty(season)) {
				result="empty.season";
			}
			
		}
		if (StringUtil.isEmpty(result)) {
			Product prod=null;
			if (!StringUtil.isEmpty(productTotalString)) {
				String[] productTotalArray = productTotalString.trim().split("\\|\\|");
				for (int i = 0; i < productTotalArray.length; i++) {
					String[] productDetail = productTotalArray[i].split("##");
					double quantity = Double.valueOf(productDetail[2]);
						prod = productService.findProductById(Long.valueOf(productDetail[0].trim()));
					if (!ObjectUtil.isEmpty(prod)) {
							avbleStock = productService.findAvailableStockByWarehouseIdAndProduct(senderWarehouse.trim(),String.valueOf(prod.getId()),season);
						 

						if (ObjectUtil.isEmpty(avbleStock) || avbleStock < quantity) {
							result = getText("insufficientstockfor") + " " + prod.getName();
							break;
						}
					}

				}
			}

			if (StringUtil.isEmpty(result)) {
				AgroTransaction agroTransaction = new AgroTransaction();
				agroTransaction.setBranch_id(getBranchId());
				if (StringUtil.isEmpty(receiptNumber))
					distributionStock = new DistributionStock();
				String receiptnumberSeq = idGenerator.getDistributionStockSeq();
				receiptNumber = receiptnumberSeq;
				agroTransaction.setReceiptNo(receiptnumberSeq);
				agroTransaction.setSeasonCode(season);
				agroTransaction.setTxnTime(DateUtil.convertStringToDate(selectedDate, getGeneralDateFormat()));
				agroTransaction.setTxnTime(DateUtil.setTimeToDate(agroTransaction.getTxnTime()));
				distributionStock.setReceiptNo(receiptNumber);
				distributionStock.setSeason(season);
				if (sendWarehouse!=null && !ObjectUtil.isEmpty(sendWarehouse)) {
					agroTransaction.setSamithi(sendWarehouse);
					distributionStock.setSenderWarehouse(sendWarehouse);;
				}
		
					if (!ObjectUtil.isEmpty(receiveWarehouse)) {
						agroTransaction.setServicePointId(receiveWarehouse.getCode());
						agroTransaction.setServicePointName(receiveWarehouse.getName());
						distributionStock.setReceiverWarehouse(receiveWarehouse);
					}
				
					
				
				// Warehouse warehouse = agent.getCooperative();
				agroTransaction.setDistributionStock(distributionStock);
				agroTransaction.setDeviceId(NOT_APPLICABLE);
				agroTransaction.setDeviceName(NOT_APPLICABLE);
				agroTransaction.setOperType(ESETxn.ON_LINE);
				agroTransaction.setProfType(Profile.CLIENT);
				agroTransaction.setTxnType(Distribution.PRODUCT_DISTRIBUTION_TO_FARMER);
				agroTransaction.setTxnDesc(Distribution.PRODUCT_DISTRIBUTION_TO_FARMER_DESCRIPTION);
				agroTransaction.setBranchId(getBranchId());
				ESEAccount fieldStaffaccount = null;
				agroTransaction.setProductStock(WarehouseProduct.StockType.WAREHOUSE_STOCK.name());

				// PROCT DETAILS FOR DISTRIBUTION_DETAIL
				Set<DistributionStockDetail> distributionStockDetailsSet = new HashSet<DistributionStockDetail>();
				double totalAmount = 0;
				DistributionStockDetail distributionStockDetail = new DistributionStockDetail();
				if (!StringUtil.isEmpty(productTotalString)) {
					String[] productTotalArray = productTotalString.trim().split("\\|\\|");
					for (int j = 0; j < productTotalArray.length; j++) {
						String[] productDetail = productTotalArray[j].split("##");
							prod = productService.findProductById(Long.valueOf(productDetail[0].trim()));
						if (prod!=null && !ObjectUtil.isEmpty(prod)) {
							avbleStock = productDistributionService.findAvailableStockByWarehouseIdAndProductId(Long.parseLong(senderWarehouse.trim()),prod.getId());
						}

						double quantity = Double.valueOf(productDetail[2]);
						distributionStockDetail = new DistributionStockDetail();
						distributionStockDetail.setProduct(prod);
						try {
							
							distributionStockDetail.setDistributionQuantity(quantity);
							distributionStockDetail.setDamageQuantity(0.0);
							distributionStockDetail.setTotalQuantity(distributionStockDetail.getDistributionQuantity()+distributionStockDetail.getDamageQuantity());
							distributionStockDetail.setDistributionStock(distributionStock);
							
						} catch (Exception e) {
							e.printStackTrace();
						}
						distributionStockDetailsSet.add(distributionStockDetail);

					}
				}
				distributionStock.setTxnType(DistributionStock.DISTRIBUTION_STOCK_TRANSFER);
				distributionStock.setDriverName(driverName);
				distributionStock.setTruckId(truckId);
				distributionStock.setTxnTime(agroTransaction.getTxnTime());
				distributionStock.setDistributionStockDetails(distributionStockDetailsSet);
				distributionStock.setBranchId(receiveWarehouse.getBranchId());
				distributionStock.setLoggedUser(getUsername());
				distributionStock.setAgrotxn(agroTransaction);
				//distributionStock.setSeason(getCurrentSeasonsCode());
				distributionStock.setStatus(0);
				productDistributionService.saveDistributionStock(distributionStock);

			}

		} else {
			result = getText(result);
		}
		if (StringUtil.isEmpty(result)) {

			String receiptHtml = "<a href=\"javascript:printReceipt(\\'" + receiptNumber
					+ "\\')\" onclick='printReceipt(\"" + receiptNumber + "\")'>" ;
			setDistributionDescription(getText("distributionStockTransferSucess") + "</br>" + getText("receiptNumber") + " : "
					+ receiptNumber + "</br>" + receiptHtml);

			/*
			 * setDistributionDescription(getText("distributionSucess") +
			 * "</br>" + getText("receiptNumber") + " : " + receiptNumber +
			 * "</br>");
			 */
			JSONObject json=new JSONObject();
			json.put("id", distributionStock.getId());
			json.put("des",getDistributionDescription());
			sendAjaxResponse(json);
		}
		response.getWriter().print(result);
	
	}
	public String getReceiverWarehouse() {
		return receiverWarehouse;
	}
	public void setReceiverWarehouse(String receiverWarehouse) {
		this.receiverWarehouse = receiverWarehouse;
	}
	public String getSelectedDate() {
		return selectedDate;
	}
	public void setSelectedDate(String selectedDate) {
		this.selectedDate = selectedDate;
	}
	public String getProductTotalString() {
		return productTotalString;
	}
	public void setProductTotalString(String productTotalString) {
		this.productTotalString = productTotalString;
	}
	public IProductDistributionService getProductDistributionService() {
		return productDistributionService;
	}
	public void setProductDistributionService(IProductDistributionService productDistributionService) {
		this.productDistributionService = productDistributionService;
	}
	public IUniqueIDGenerator getIdGenerator() {
		return idGenerator;
	}
	public void setIdGenerator(IUniqueIDGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}
	public String getDistributionDescription() {
		return distributionDescription;
	}
	public void setDistributionDescription(String distributionDescription) {
		this.distributionDescription = distributionDescription;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getTruckId() {
		return truckId;
	}
	public void setTruckId(String truckId) {
		this.truckId = truckId;
	}
	public String getIdentityForGrid() {
		return identityForGrid;
	}
	public void setIdentityForGrid(String identityForGrid) {
		this.identityForGrid = identityForGrid;
	}
	public String getReceiptNumber() {
		return receiptNumber;
	}
	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}
	public DistributionStock getDistributionStock() {
		return distributionStock;
	}
	public void setDistributionStock(DistributionStock distributionStock) {
		this.distributionStock = distributionStock;
	}
	public DistributionStockDetail getDistributionStockDetail() {
		return distributionStockDetail;
	}
	public void setDistributionStockDetail(DistributionStockDetail distributionStockDetail) {
		this.distributionStockDetail = distributionStockDetail;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getDisableBranch() {
		return disableBranch;
	}

	public void setDisableBranch(String disableBranch) {
		this.disableBranch = disableBranch;
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

	
}
