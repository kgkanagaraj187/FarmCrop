/*
 * MTNRAction.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.traceability.GinningProcess;
import com.ese.entity.traceability.HeapData;
import com.ese.entity.traceability.HeapDataDetail;
import com.ese.entity.util.ESESystem;

import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.AgentType;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.order.entity.profile.GradeMaster;
import com.sourcetrace.eses.order.entity.txn.CityWarehouse;
import com.sourcetrace.eses.order.entity.txn.PMT;
import com.sourcetrace.eses.order.entity.txn.PMTDetail;
import com.sourcetrace.eses.order.entity.txn.TransferInfo;
import com.sourcetrace.eses.service.FarmerService;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.service.ProductService;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.WebTransactionAction;

@SuppressWarnings("serial")
public class GinningProcessAction extends BaseReportAction {
    private ILocationService locationService;
	private IPreferencesService preferncesService;
	@Autowired
	private IProductService productService;
	@Autowired
	private IProductDistributionService productDistributionService;
    private String selectedGinning;
    private String selectedProduct;
    private String selectedIcs;
    private String selectedHeap;
    private String params;
    private String procsQty;
    private String lintQty;
    private String seedQty;
    private String scrupQty;
    private String processDate;
    private String ginningCode;	
    
    
    public String create() {
        request.setAttribute(HEADING, getText("create"));
        return INPUT;
    }

	public String getCurrentDate() {

		Calendar currentDate = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat(getGeneralDateFormat());
		return df.format(DateUtil.setTimeToDate(new Date()));
	}
    
	public void populateGinningList(){
		JSONArray ginningArr = new JSONArray();
		List<Object[]> ginnings =locationService.listOfGinningFromHeap(getCurrentSeasonsCode());
		if (!ObjectUtil.isEmpty(ginnings)) {
			ginnings.forEach(obj -> {
				ginningArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(ginningArr);
	}  
    public void populateProductList(){
    	JSONArray productArr=new JSONArray();
    	if(!StringUtil.isEmpty(selectedGinning)){
    	List<Object[]> products=locationService.listOfProductFromHeapWithHeap(selectedGinning,selectedHeap);
    	if(!ObjectUtil.isEmpty(products)){
    		products.forEach(obj-> {
    			productArr.add(getJSONObject(obj[0].toString(),obj[1].toString()));
    		});
    	}
    	}
    	sendAjaxResponse(productArr);
    	
    }
    public void populateICSList(){
    	JSONArray icsArr=new JSONArray();
    	if(!StringUtil.isEmpty(params)){
    	List<Object[]> ics=locationService.listOfICSFromHeapByGinningCodeAndProdut(params.split(",")[0],params.split(",")[1]);
    	if(!ObjectUtil.isEmpty(ics)){
    		ics.forEach(obj ->{
    			icsArr.add(getJSONObject(obj[0].toString(),obj[1].toString()));
    		});
    	}
    	}
    	sendAjaxResponse(icsArr);
    }
    public void populateHeapList(){
    	JSONArray heapArr=new JSONArray();
    	if(!StringUtil.isEmpty(ginningCode)){
    	List<Object[]> heaps=locationService.listOfHeapsByGinningCodeProdutICS(ginningCode,getCurrentSeasonsCode());
    	if(!ObjectUtil.isEmpty(heaps)){
    		heaps.forEach(obj ->{
    			heapArr.add(getJSONObject(obj[0].toString(),obj[1].toString()));    			
    		});
    	}
    	}
    	sendAjaxResponse(heapArr);
    }
    public void populateDataValue(){
    	JSONObject jsonObj=new JSONObject();
    	if(selectedGinning!=null && selectedHeap!=null && selectedProduct!=null && !StringUtil.isEmpty(selectedHeap) && !StringUtil.isEmpty(selectedGinning) && !StringUtil.isEmpty(selectedProduct)){
    	Object[] vals=locationService.findHeapQtyICSAndProductByHeapAndGinning(selectedHeap,selectedGinning,selectedProduct,getCurrentSeasonsCode());
    	jsonObj.put("icsCode", vals[2]);
    	jsonObj.put("ics", vals[0]);
    	jsonObj.put("qty", vals[1]);
    	}
    	sendAjaxResponse(jsonObj);
    }
    public void populateHeapStock(){
    	JSONObject jsonObj=new JSONObject();
    	if(!StringUtil.isEmpty(selectedHeap)){
    	String qty=locationService.findHeapQtyByHeapDataId(selectedHeap);
    	jsonObj.put("qty", qty);
    	}
    	sendAjaxResponse(jsonObj);
    }
    public void populateGinningProcess(){
    	/*HeapData hd=locationService.findHeapDataById(params.split(",")[1].toString());
    	
    	GinningProcess ginningProcess=new GinningProcess();
    	ginningProcess.setGinning(productService.findWarehouseByCode(params.split(",")[0].toString().trim()));
    	ginningProcess.setProduct(productService.findProcurementProductByCode(params.split(",")[2].toString().trim()));
    	ginningProcess.setIcsCode(params.split(",")[3].toString().trim());
    	ginningProcess.setHeapData(hd);
    	try{
    	 SimpleDateFormat sdf=new SimpleDateFormat(DateUtil.TXN_TIME_FORMAT);
         Calendar cal = Calendar.getInstance();
         cal.setTime(sdf.parse(processDate));
         String currentdate =  sdf.format(cal.getTime());
         System.out.print(currentdate);
    		SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
    		Calendar cal = Calendar.getInstance();
    		String tim=sdf.format(cal.getTime());
    		processDate=processDate+" "+tim;
    		SimpleDateFormat df=new SimpleDateFormat(DateUtil.DATE_FORMAT_4);
         ginningProcess.setProcessDate(df.parse(processDate));
    	}catch(Exception e){}
    	ginningProcess.setTotlintCotton(Double.parseDouble(lintQty));
    	ginningProcess.setTotseedCotton(Double.parseDouble(seedQty));
    	ginningProcess.setTotscrup(Double.parseDouble(scrupQty));
    	locationService.saveGinningProcess(ginningProcess);*/
    	
    	
    	
    	
    	
    	GinningProcess gp=new GinningProcess();
    	gp.setGinning(locationService.findWarehouseById(Long.parseLong(params.split("~")[0].toString().trim()),getCurrentTenantId()));
    	gp.setProduct(productService.findProcurementProductByCode(params.split("~")[2].toString().trim()));
					gp.setHeapCode(params.split("~")[1].toString().trim());
					gp.setProcessDate(DateUtil.convertDateFormat(processDate, DateUtil.DATE_FORMAT_2, DateUtil.DATE));
					gp.setBaleCount(0);
					gp.setProcessQty(Double.parseDouble(procsQty.toString().trim()));
					gp.setTotlintCotton(0.0);
					gp.setTotseedCotton(Double.parseDouble(seedQty.trim()));
					gp.setTotscrap(scrupQty!=null && !StringUtil.isEmpty(scrupQty)?Double.parseDouble(scrupQty.trim()):0);
					//gp.setLintPer((gp.getTotlintCotton()/gp.getProcessQty())*100);
					gp.setSeedPer((gp.getTotseedCotton()/gp.getProcessQty())*100);
					gp.setScrapPer((gp.getTotscrap()/gp.getProcessQty())*100);
					gp.setBranchId(getBranchId());
					gp.setSeason(getCurrentSeasonsCode());
					productDistributionService.saveGinningProcess(gp,getCurrentTenantId());
					
    	/*HeapDataDetail hdd=new HeapDataDetail();
    	hdd.setHeapData(hd);
    	hdd.setDescription(getLocaleProperty("stock.moved"));
    	hdd.setDate(new Date());
    	hdd.setLintCotton(Double.parseDouble(lintQty));
    	hdd.setSeedCotton(Double.parseDouble(seedQty));
    	hdd.setScrup(Double.parseDouble(scrupQty));
    	hdd.setStatus(3);
    	hdd.setPmtDetailId(ginningProcess.getId());
    	hdd.setPreviousStock(hd.getTotalStock());
    	hdd.setTxnStock(Double.parseDouble(procsQty));
    	hdd.setTotalStock(hdd.getPreviousStock()-hdd.getTxnStock());
    	hd.getHeapDataDetail().add(hdd);
    	hd.setTotalStock(hd.getTotalStock()-Double.parseDouble(procsQty));
    	hd.setTotLintCotton(hd.getTotLintCotton()+Double.parseDouble(lintQty));
    	hd.setTotSeedCotton(hd.getTotSeedCotton()+Double.parseDouble(seedQty));
    	hd.setTotScrup(hd.getTotScrup()+Double.parseDouble(scrupQty));
    	locationService.updateHeapData(hd);*/
    	
    	
    }
    
    
    
    
    
	public ILocationService getLocationService() {
		return locationService;
	}

	public void setLocationService(ILocationService locationService) {
		this.locationService = locationService;
	}

	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}

	public String getSelectedGinning() {
		return selectedGinning;
	}

	public void setSelectedGinning(String selectedGinning) {
		this.selectedGinning = selectedGinning;
	}

	public String getSelectedProduct() {
		return selectedProduct;
	}

	public void setSelectedProduct(String selectedProduct) {
		this.selectedProduct = selectedProduct;
	}

	public String getSelectedIcs() {
		return selectedIcs;
	}

	public void setSelectedIcs(String selectedIcs) {
		this.selectedIcs = selectedIcs;
	}

	public String getSelectedHeap() {
		return selectedHeap;
	}

	public void setSelectedHeap(String selectedHeap) {
		this.selectedHeap = selectedHeap;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getProcsQty() {
		return procsQty;
	}

	public void setProcsQty(String procsQty) {
		this.procsQty = procsQty;
	}

	public String getLintQty() {
		return lintQty;
	}

	public void setLintQty(String lintQty) {
		this.lintQty = lintQty;
	}

	public String getSeedQty() {
		return seedQty;
	}

	public void setSeedQty(String seedQty) {
		this.seedQty = seedQty;
	}

	public String getScrupQty() {
		return scrupQty;
	}

	public void setScrupQty(String scrupQty) {
		this.scrupQty = scrupQty;
	}

	public IProductService getProductService() {
		return productService;
	}

	public void setProductService(IProductService productService) {
		this.productService = productService;
	}

	public String getProcessDate() {
		return processDate;
	}

	public void setProcessDate(String processDate) {
		this.processDate = processDate;
	}

	public String getGinningCode() {
		return ginningCode;
	}

	public void setGinningCode(String ginningCode) {
		this.ginningCode = ginningCode;
	}

	public IProductDistributionService getProductDistributionService() {
		return productDistributionService;
	}

	public void setProductDistributionService(IProductDistributionService productDistributionService) {
		this.productDistributionService = productDistributionService;
	}
	
}
