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
import com.sourcetrace.eses.order.entity.txn.PMT;
import com.sourcetrace.eses.order.entity.txn.PMTDetail;
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
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.entity.profile.WarehousePaymentDetails;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.view.WebTransactionAction;

// TODO: Auto-generated Javadoc
/**
 * The Class DistributionAction.
 */
public class DistributionStockReceptionAction extends WebTransactionAction {
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
	private String receiptNo;
	private String distDetailsStr;
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
		filter.setTxnType(DistributionStock.DISTRIBUTION_STOCK_RECEPTION);
		filter.setStatus(3);
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
		//super.filter = filter;
			Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(),
	                getResults(), filter, getPage());
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
				rows.add(String.valueOf(distributionStockDetail.getDistributionQuantity()));
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
	public Map<Long, String> getReceiverWarehouseList(){
		Map<Long, String> warehouseMap=new LinkedHashMap<>();
		List<Object[]> warehouseList=locationService.listWarehouses();
		if(!ObjectUtil.isListEmpty(warehouseList)){
		warehouseList.stream().forEach(w->{
			String a=String.valueOf((w[2].toString()).concat(w[3].toString()));
			warehouseMap.put(Long.parseLong(w[0].toString()),w[2].toString() ); 
		});
		}
		return warehouseMap;
	}
	
	public void populateReceiptNo(){
		List<Object> receiptList=productService.listOfDistributionStockReceiptNo(Long.parseLong(receiverWarehouse));
		JSONArray receiptArr = new JSONArray();
		if(!ObjectUtil.isListEmpty(receiptList)){
		receiptList.stream().distinct().forEach(p->{
			receiptArr.add(getJSONObject(p.toString(), p.toString()));
		});
		}
		sendAjaxResponse(receiptArr);
	}
	
	public void populateProduct(){
		List<Product> productList=productService.findProductBySubCategoryCode(category);
		JSONArray productArr = new JSONArray();
		if(!ObjectUtil.isListEmpty(productList)){
		productList.stream().distinct().forEach(p->{
			productArr.add(getJSONObject(p.getId(), p.getName().toString()));
		});
		}
		sendAjaxResponse(productArr);
	}
	public void populateAvailableStock() throws Exception{
		String result = NOT_APPLICABLE;
		Double availableStock=0.0;
		availableStock=productService.findAvailableStockByWarehouseIdAndProduct(senderWarehouse,product,season);
		String unit=productService.findProductUnitByProductId(Long.parseLong(product)).getUnit();
		availableStock=!ObjectUtil.isEmpty(availableStock)?availableStock:0.00;
		unit=!StringUtil.isEmpty(unit)?unit:NOT_APPLICABLE;
		result = availableStock + "," + unit;

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
	public String populateStock(){

	    JSONObject products = new JSONObject();
	    JSONArray productArray = new JSONArray();
	    if(receiverWarehouse!=null && receiptNo!=null && !StringUtil.isEmpty(receiverWarehouse)&& !StringUtil.isEmpty(receiptNo))  {
	        List<DistributionStockDetail> distStockDetail = productDistributionService.listDistributionStockDetailByReceiveWarehouseIdAndReceiptNo(Long.valueOf(receiverWarehouse),receiptNo);
	            JSONObject productJSON = new JSONObject();
	            JSONArray distJSON = new JSONArray();
	            for (DistributionStockDetail detail : distStockDetail) {
	                JSONObject detailJSON = new JSONObject();
	                detailJSON.put("categoryId", detail.getProduct().getSubcategory().getId());
	                detailJSON.put("categoryName", detail.getProduct().getSubcategory().getName());
	                detailJSON.put("productId", detail.getProduct().getId());
	                detailJSON.put("productName", detail.getProduct().getName());
	                detailJSON.put("unit", detail.getProduct().getUnit());
	                detailJSON.put("qty", detail.getDistributionQuantity());
	                detailJSON.put("id", detail.getId());
	                distJSON.add(detailJSON);

	            }

	            productJSON.put("distStockReceiption", distJSON);
	            productArray.add(productJSON);

	       // }
	        products.put("products", productArray);
	    }
	   
	    try {
	        // sendResponse(products);
	        printAjaxResponse(products, "text/html");
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    return null;
	}
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	public void populateTruck(){
		if(!StringUtil.isEmpty(receiptNo)&& receiptNo!=null){
			DistributionStock ds= productDistributionService.findTransferDistributionStockByReceiptNumber(receiptNo);
			 JSONObject obj=new JSONObject();
			 if(!ObjectUtil.isEmpty(ds)){
				 obj.put("truck", ds.getTruckId());
				 obj.put("driver", ds.getDriverName());
			}
			 sendAjaxResponse(obj);
		}
		
	 }
	public void populateReception(){
        String result = "";
        Warehouse warehouse=null;
        setDisableBranch(preferncesService.findPrefernceByName(ESESystem.DISABLE_BRANCH));
		if (!StringUtil.isEmpty(getDisableBranch()) && getDisableBranch().equals("1")) {
             warehouse = locationService.findWarehouseByIdWithoutBranch(Long.valueOf(receiverWarehouse));
		}
		else{
			warehouse = locationService.findWarehouseById(Long.valueOf(receiverWarehouse));
		}
            if (ObjectUtil.isEmpty(warehouse)) {
                result = "warehouse.unavailable";
            }
            if(StringUtil.isEmpty(receiptNo)){
            	result="empty.receiptNo";
            }
            if(StringUtil.isEmpty(season)){
            	result="empty.season";
            }
            if (StringUtil.isEmpty(result)) {
            DistributionStock distributionStock = new DistributionStock();
            Set<DistributionStockDetail>distStkDetail=new HashSet<DistributionStockDetail>();
            distributionStock.setTxnTime(DateUtil.convertStringToDate(startDate, getGeneralDateFormat()));
            distributionStock.setReceiverWarehouse(warehouse);
            distributionStock.setTruckId(truckId);
            distributionStock.setDriverName(driverName);
            distributionStock.setSeason(season);
            distributionStock.setBranchId(getBranchId());
            distributionStock.setReceiptNo(receiptNo);
            distributionStock.setTxnType(DistributionStock.DISTRIBUTION_STOCK_RECEPTION);
            DistributionStock tempds=productDistributionService.findTransferDistributionStockByReceiptNumber(distributionStock.getReceiptNo());
            distributionStock.setSenderWarehouse(tempds.getSenderWarehouse());
            distributionStock.setLoggedUser(getUsername());
            distributionStock.setStatus(1);
            String[] productArray=distDetailsStr.split("\\|\\|");
			for (String gradeStr : productArray) {
					DistributionStockDetail distDetail=new DistributionStockDetail();
	                String[] productValues = gradeStr.split("##");
	                distDetail.setDistributionStock(distributionStock);
	                Product product=productService.findProductById(Long.valueOf(productValues[0]));		
	                distDetail.setProduct(product);
	                distDetail.setDistributionQuantity(Double.parseDouble(productValues[1]));
	                DistributionStockDetail existing=productDistributionService.findDistributionStockDetailById(Long.valueOf(productValues[2]));
	                distDetail.setDamageQuantity(existing.getDistributionQuantity()-distDetail.getDistributionQuantity());
	                distDetail.setTotalQuantity(existing.getDistributionQuantity());
	                distStkDetail.add(distDetail);
	                distributionStock.setDistributionStockDetails(distStkDetail);
			}
			tempds.setStatus(2);
			productDistributionService.update(tempds);
           
			productDistributionService.saveDistributionStock(distributionStock);
            }else {
    			result = getText(result);
    		}
            
	          if (StringUtil.isEmpty(result)) {
	        	  String receiptHtml = "<a href=\"javascript:printReceipt(\\'" + receiptNo
	                      + "\\')\" onclick='printReceipt(\"" + receiptNo + "\")'>";
	                setDistributionDescription(getText("distributionStockReceptionSucess") + "</br>"
	                        + getText("receiptNumber") + " : "+ receiptNo+"<br/>"+ receiptHtml);

	                printAjaxResponse(getDistributionDescription(), "text/html");
	            } else {
	                setDistributionDescription(getText(result));
	                printAjaxResponse(getDistributionDescription(), "text/html");
	            }

        
	}
	public String getDistDetailsStr() {
		return distDetailsStr;
	}
	public void setDistDetailsStr(String distDetailsStr) {
		this.distDetailsStr = distDetailsStr;
	}
	
	public String getDisableBranch() {
		return disableBranch;
	}

	public void setDisableBranch(String disableBranch) {
		this.disableBranch = disableBranch;
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
	public String getSeason() {
		return season;
	}
	public void setSeason(String season) {
		this.season = season;
	}
	public IFarmerService getFarmerService() {
		return farmerService;
	}
	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}
	
}
