/*
 * ProcurementGradeAction.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view.general;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.profile.CropHarvestDetails;
import com.ese.entity.profile.CropSupplyDetails;
import com.sourcetrace.eses.BreadCrumb;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.order.entity.txn.SupplierProcurementDetail;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DoubleUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementGradePricingHistory;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.State;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

public class ProcurementGradeAction extends SwitchValidatorAction {

	private static final long serialVersionUID = 1L;
	private IProductDistributionService productDistributionService;

	private ProcurementGrade procurementGrade;

	private String id;
	private String tabIndexVariety = "#tabs-2";
	private String procurementVarietyId;
	private String procurementVarietyCodeAndName;
	private String procurementProductCodeAndName;
	private String gradePrice;
	private String tabIndex = "#tabs-1";
	private String selectedUom = "mm";
	private List<FarmCatalogue> subUomList = new ArrayList<FarmCatalogue>();
	private String gradeName;
	private List<ProcurementVariety> procurementVarietyList = new ArrayList<>();
	private String selectedUnit;
	private String procurementProductId;

	@Autowired
	private IUniqueIDGenerator idGenerator;
	@Autowired
	private ICatalogueService catalogueService;

	/**
	 * @see com.sourcetrace.esesw.view.SwitchAction#data()
	 */
	public String data() throws Exception {

		Map<String, String> searchRecord = getJQGridRequestParam(); // get the
																	// search
																	// parameter
																	// with
		// value

		ProcurementGrade filter = new ProcurementGrade();
		ProcurementVariety procurementvarietyObject = null;
		ProcurementVariety procurementVariety = new ProcurementVariety();
		ProcurementProduct procurementProduct = new ProcurementProduct();

		if (!StringUtil.isEmpty(searchRecord.get("code"))) {
			filter.setCode(searchRecord.get("code").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("gradeName"))) {
			filter.setName(searchRecord.get("gradeName").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("procurementVarietyId"))) {
			procurementVariety.setName(searchRecord.get("procurementVarietyId"));
			filter.setProcurementVariety(procurementVariety);

		}

		if (!StringUtil.isEmpty(searchRecord.get("gradePrice"))) {
			filter.setPrice(Double.valueOf(searchRecord.get("gradePrice").trim()));
		}

		if (!StringUtil.isEmpty(searchRecord.get("procurementProductId"))) {

			procurementProduct.setName(searchRecord.get("procurementProductId"));

			procurementVariety.setProcurementProduct(procurementProduct);
			filter.setProcurementVariety(procurementVariety);
		}

		if (!StringUtil.isEmpty(this.getProcurementVarietyId())) {
			procurementvarietyObject = productDistributionService
					.findProcurementVariertyById(Long.valueOf(this.getProcurementVarietyId()));
			filter.setProcurementVariety(procurementvarietyObject);
			if (!ObjectUtil.isEmpty(filter.getProcurementVariety())) {
				filter.getProcurementVariety().setId(Long.valueOf(this.getProcurementVarietyId()));
			}

		}

		/*if (!StringUtil.isEmpty(getBranchId())) {

			procurementProduct.setBranchId(getBranchId());
			procurementVariety.setProcurementProduct(procurementProduct);
			filter.setProcurementVariety(procurementVariety);

		}*/

		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		return sendJQGridJSONResponse(data);
	}

	/**
	 * @see com.sourcetrace.esesw.view.SwitchAction#toJSON(java.lang.Object)
	 */
	public JSONObject toJSON(Object obj) {

		ProcurementGrade grade = (ProcurementGrade) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">" + grade.getCode() + "</font>");
		/*rows.add(!ObjectUtil.isEmpty(grade.getProcurementVariety())
				? grade.getProcurementVariety().getProcurementProduct().getName() : "");*/
		
		 if(!StringUtil.isEmpty(grade.getProcurementVariety()) && !StringUtil.isEmpty(grade.getProcurementVariety().getProcurementProduct())){
       	  String productName = getLanguagePref(getLoggedInUserLanguage(), grade.getProcurementVariety().getProcurementProduct().getCode().trim().toString());
     		if(!StringUtil.isEmpty(productName) && productName != null){
     			rows.add(productName);
     		}else{
     			rows.add(!ObjectUtil.isEmpty(grade.getProcurementVariety()) ? grade.getProcurementVariety().getProcurementProduct().getName() : "");
     		}
       }else{
			rows.add(!ObjectUtil.isEmpty(grade.getProcurementVariety()) ? grade.getProcurementVariety().getProcurementProduct().getName() : "");
       }

		//rows.add(!ObjectUtil.isEmpty(grade.getProcurementVariety()) ? grade.getProcurementVariety().getName() : "");
		 
		 if(!StringUtil.isEmpty(grade.getProcurementVariety()) && !StringUtil.isEmpty(grade.getProcurementVariety().getCode())){
			 String varietyname = getLanguagePref(getLoggedInUserLanguage(), grade.getProcurementVariety().getCode().trim().toString());
		   		if(!StringUtil.isEmpty(varietyname) && varietyname != null){
		   			rows.add(varietyname);
		   		}else{
		   			rows.add(!ObjectUtil.isEmpty(grade.getProcurementVariety()) ? grade.getProcurementVariety().getName() : "");
		   		}
		 }else{
			 rows.add(!ObjectUtil.isEmpty(grade.getProcurementVariety()) ? grade.getProcurementVariety().getName() : "");
		 }
		 
		
   		
   		
		//rows.add(grade.getName());
		 
		 String gradeName = getLanguagePref(getLoggedInUserLanguage(), grade.getCode().trim().toString());
	   		if(!StringUtil.isEmpty(gradeName) && gradeName != null){
	   			rows.add(gradeName);
	   		}else{
	   			rows.add(!StringUtil.isEmpty(grade.getName()) ? grade.getName() : "");
	   		}
	   		
	   		

		// rows.add(!ObjectUtil.isEmpty(grade.getType())?grade.getType().getName():"");
		rows.add(CurrencyUtil.getDecimalFormat(grade.getPrice(), "##.00"));
		jsonObject.put("id", grade.getId());
		jsonObject.put("cell", rows);
		return jsonObject;
	}

	/**
	 * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
	 */
	@Override
	public Object getData() {

		return procurementGrade;
	}

	/**
	 * Creates the.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings("unchecked")
	public void create() throws Exception {

		if (!StringUtil.isEmpty(gradeName)) {
			procurementGrade = new ProcurementGrade();
			procurementGrade.setName(gradeName);
			// procurementGrade.processPrice();
			if (!StringUtil.isEmpty(this.getProcurementVarietyId())) {
				procurementGrade.setProcurementVariety(productDistributionService
						.findProcurementVariertyById(Long.valueOf(this.getProcurementVarietyId())));
			}
			ProcurementGrade pg = productDistributionService.findProcurementGradeByNameAndVarietyId(gradeName,Long.valueOf(procurementVarietyId));
			if (!ObjectUtil.isEmpty(pg) && !ObjectUtil.isEmpty(pg.getProcurementVariety())) {
				if (pg.getProcurementVariety().getId() == Long.valueOf(this.getProcurementVarietyId())) {
					getJsonObject().put("msg", getText("msg.error"));
					getJsonObject().put("title", getText("title.duplicate"));
					sendAjaxResponse(getJsonObject());
				}
				
			}else {
				procurementGrade.setPrice(Double.valueOf(gradePrice));
				FarmCatalogue farmCatalogue = catalogueService.findCatalogueByCode(selectedUnit);
				if (!ObjectUtil.isEmpty(farmCatalogue)) {
					this.procurementGrade.setUnit(farmCatalogue.getName());
					this.procurementGrade.setType(farmCatalogue);
				}
				procurementGrade.setCode(idGenerator.getProcurementGradeIdSeq());
				productDistributionService.addProcurementGrade(procurementGrade);
				getJsonObject().put("msg", getText("msg.gradeAdded"));
				getJsonObject().put("title", getText("title.success"));
				sendAjaxResponse(getJsonObject());
			}
		}
	}

	/**
	 * Update.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings("unchecked")
	public void update() throws Exception {

		if (id != null) {
			ProcurementGrade temp = productDistributionService.findProcurementGradeById(Long.valueOf(id));
			
			if(!StringUtil.isEmpty(gradeName) && !StringUtil.isEmpty(gradePrice)){

				ProcurementGradePricingHistory procurementGradePricingHistory = new ProcurementGradePricingHistory();
				procurementGradePricingHistory.setProcurementGrade(temp);
				procurementGradePricingHistory.setPrice(Double.valueOf(gradePrice));
				productDistributionService.addprocurementGradePricingHistory(procurementGradePricingHistory);
				temp.setProcurementVariety(productDistributionService
						.findProcurementVariertyById(Long.valueOf(this.getProcurementVarietyId())));
				temp.setName(gradeName);
				temp.setPrice(Double.valueOf(gradePrice));
				FarmCatalogue farmCatalogue = catalogueService.findCatalogueByCode(selectedUnit);
				if (!ObjectUtil.isEmpty(farmCatalogue)) {
					temp.setType(farmCatalogue);
					temp.setUnit(farmCatalogue.getName());
				}
				productDistributionService.editProcurementGrade(temp);
				getJsonObject().put("msg", getText("msg.gradeUpdated"));
				getJsonObject().put("title", getText("title.success"));
			}else{
				getJsonObject().put("msg", getText("Please Enter Grade Name"));
    			getJsonObject().put("title",getText("Error"));
			}

			
			}

		sendAjaxResponse(getJsonObject());
	}

	/**
	 * Detail.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 *//*
		 * public String detail() throws Exception {
		 * 
		 * String view = ""; if (id != null && !id.equals("")) {
		 * procurementGrade =
		 * productDistributionService.findProcurementGradeById(Long.valueOf(id))
		 * ; if (procurementGrade == null) { addActionError(NO_RECORD); return
		 * REDIRECT; } this.gradePrice =
		 * DoubleUtil.getString(procurementGrade.getPrice());
		 * setCurrentPage(getCurrentPage()); command = UPDATE; view = DETAIL;
		 * request.setAttribute(HEADING, getText(DETAIL)); } else {
		 * request.setAttribute(HEADING, getText(LIST)); return REDIRECT; }
		 * return view; }
		 */

	@SuppressWarnings("unchecked")
	public void populateDelete() throws Exception {

		if (id != null) {
			procurementGrade = productDistributionService.findProcurementGradeById(Long.valueOf(id));

			if (!ObjectUtil.isEmpty(procurementGrade)) {

				List<CropHarvestDetails> cropHarvetsDetailsList = productDistributionService
						.listCropHarvestDetailsByProcurementGradeId(Long.valueOf(id));
				List<CropSupplyDetails> cropSaleDetailsList = productDistributionService
						.listCropSaleDetailsByProcurementGradeId(Long.valueOf(id));
				// List<ProcurementGrade>
				// procurementGradeList=productDistributionService.listProcurementGradeByProcurementVarietyId(Long.parseLong(id));

				List<SupplierProcurementDetail> supplierProcurementList = productDistributionService
						.listSupplierProcurementDetailById(Long.valueOf(id));		
				
				if (!ObjectUtil.isListEmpty(cropHarvetsDetailsList)) {
					addActionError(getText("cropHarvest.exist"));
					getJsonObject().put("msg", getText("cropHarvest.exist"));
					getJsonObject().put("title", getText("title.error"));

				}else if (!ObjectUtil.isListEmpty(supplierProcurementList)) {
					addActionError(getText("supplierProcurement.exist"));
					getJsonObject().put("msg", getText("supplierProcurement.exist"));
					getJsonObject().put("title", getText("title.error"));
				}else if (!ObjectUtil.isListEmpty(cropSaleDetailsList)) {
					addActionError(getText("cropSupply.exist"));
					getJsonObject().put("msg", getText("cropSupply.exist"));
					getJsonObject().put("title", getText("title.error"));
				} else {
					productDistributionService.remove(procurementGrade);
					getJsonObject().put("msg", getText("grademsg.deleted"));
					getJsonObject().put("title", getText("title.success"));
				}

			}
			sendAjaxResponse(getJsonObject());
		}

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
	 * Gets the tab index variety.
	 * 
	 * @return the tab index variety
	 */
	public String getTabIndexVariety() {

		return tabIndexVariety;
	}

	/**
	 * Sets the tab index variety.
	 * 
	 * @param tabIndexVariety
	 *            the new tab index variety
	 */
	public void setTabIndexVariety(String tabIndexVariety) {

		this.tabIndexVariety = tabIndexVariety;
	}

	/**
	 * Gets the procurement variety id.
	 * 
	 * @return the procurement variety id
	 */
	public String getProcurementVarietyId() {

		return procurementVarietyId;
	}

	/**
	 * Sets the procurement variety id.
	 * 
	 * @param procurementVarietyId
	 *            the new procurement variety id
	 */
	public void setProcurementVarietyId(String procurementVarietyId) {

		this.procurementVarietyId = procurementVarietyId;
	}

	/**
	 * Gets the procurement variety code and name.
	 * 
	 * @return the procurement variety code and name
	 */
	public String getProcurementVarietyCodeAndName() {

		return procurementVarietyCodeAndName;
	}

	/**
	 * Sets the procurement variety code and name.
	 * 
	 * @param procurementVarietyCodeAndName
	 *            the new procurement variety code and name
	 */
	public void setProcurementVarietyCodeAndName(String procurementVarietyCodeAndName) {

		this.procurementVarietyCodeAndName = procurementVarietyCodeAndName;
	}

	/**
	 * Gets the procurement grade.
	 * 
	 * @return the procurement grade
	 */
	public ProcurementGrade getProcurementGrade() {

		return procurementGrade;
	}

	/**
	 * Sets the procurement grade.
	 * 
	 * @param procurementGrade
	 *            the new procurement grade
	 */
	public void setProcurementGrade(ProcurementGrade procurementGrade) {

		this.procurementGrade = procurementGrade;
	}

	/**
	 * Gets the procurement detail params.
	 * 
	 * @return the procurement detail params
	 */
	public String getProcurementDetailParams() {

		return "tabIndex=" + URLEncoder.encode(tabIndexVariety) + "&id=" + getProcurementVarietyId() + "&"
				+ tabIndexVariety;
	}

	/**
	 * Gets the grade price.
	 * 
	 * @return the grade price
	 */
	public String getGradePrice() {

		return gradePrice;
	}

	/**
	 * Sets the grade price.
	 * 
	 * @param gradePrice
	 *            the new grade price
	 */
	public void setGradePrice(String gradePrice) {

		this.gradePrice = gradePrice;
	}

	public void prepare() throws Exception {
		/*
		 * String procurementVariety =
		 * (String)request.getParameter("procurementVarietyId"); //below code to
		 * get bread crumb and set variety id value for it. ProcurementVariety
		 * pv =
		 * productDistributionService.findProcurementVariertyById(Long.valueOf(
		 * procurementVariety)); String
		 * breadCrumbValue=getText(BreadCrumb.BREADCRUMB, ""); // store bread
		 * crumb value. //do string manipulation by adding id to the bread
		 * crumb. String varietyBreadCrumbValue="";
		 * if(getCurrentLanguage().equals("en")){
		 * varietyBreadCrumbValue=breadCrumbValue.substring(breadCrumbValue.
		 * indexOf("Variety~procurementProductEnroll_detail.action"),
		 * breadCrumbValue.lastIndexOf(','));
		 * varietyBreadCrumbValue=varietyBreadCrumbValue+"?id="+pv.
		 * getProcurementProduct().getId()+"&tabValue=tabs-2";
		 * breadCrumbValue=breadCrumbValue.replace(
		 * "Variety~procurementProductEnroll_detail.action",
		 * varietyBreadCrumbValue); } else
		 * if(getCurrentLanguage().equals("fr")){
		 * varietyBreadCrumbValue=breadCrumbValue.substring(breadCrumbValue.
		 * indexOf("Variété~procurementProductEnroll_detail.action"),
		 * breadCrumbValue.lastIndexOf(','));
		 * varietyBreadCrumbValue=varietyBreadCrumbValue+"?id="+pv.
		 * getProcurementProduct().getId()+"&tabValue=tabs-2";
		 * breadCrumbValue=breadCrumbValue.replace(
		 * "Variété~procurementProductEnroll_detail.action",
		 * varietyBreadCrumbValue); }
		 * 
		 * request.setAttribute(BreadCrumb.BREADCRUMB,
		 * BreadCrumb.getBreadCrumb(breadCrumbValue+procurementVariety+
		 * "&tabValue=tabs-2"));
		 */
	}

	public IUniqueIDGenerator getIdGenerator() {
		return idGenerator;
	}

	public void setIdGenerator(IUniqueIDGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	public String getProcurementProductCodeAndName() {
		return procurementProductCodeAndName;
	}

	public void setProcurementProductCodeAndName(String procurementProductCodeAndName) {
		this.procurementProductCodeAndName = procurementProductCodeAndName;
	}

	public String getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(String tabIndex) {
		this.tabIndex = tabIndex;
	}

	public List<FarmCatalogue> getListUom() {
		List<FarmCatalogue> subUomList = catalogueService.listCataloguesByUnit();

		return subUomList;
	}

	public String getSelectedUom() {
		return selectedUom;
	}

	public void setSelectedUom(String selectedUom) {
		this.selectedUom = selectedUom;
	}

	public List<FarmCatalogue> getSubUomList() {
		return subUomList;
	}

	public void setSubUomList(List<FarmCatalogue> subUomList) {
		this.subUomList = subUomList;
	}

	public ICatalogueService getCatalogueService() {
		return catalogueService;
	}

	public void setCatalogueService(ICatalogueService catalogueService) {
		this.catalogueService = catalogueService;
	}

	public List<ProcurementVariety> getVarietyList() {
		return productDistributionService.listProcurementVariety();

	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getSelectedUnit() {
		return selectedUnit;
	}

	public void setSelectedUnit(String selectedUnit) {
		this.selectedUnit = selectedUnit;
	}

	public void populateProcurementVariety() {
		List<ProcurementVariety> varietyList = productDistributionService.listProcurementVariety();

		JSONObject jsonObject = new JSONObject();
		varietyList.forEach(procurementVariety -> jsonObject.put(String.valueOf(procurementVariety.getId()),
				procurementVariety.getName()));
		try {
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			out.print(jsonObject.toString().replace("\"", "").replace(",", ";"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void populateUnit() {
		subUomList = catalogueService.listCataloguesByUnit();

		JSONObject jsonObject = new JSONObject();
		subUomList.forEach(unit -> jsonObject.put(String.valueOf(unit.getCode()), unit.getName()));
		try {
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			out.print(jsonObject.toString().replace("\"", "").replace(",", ";"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void populateProcurementProduct() {
		List<ProcurementProduct> procurementProdList = productDistributionService.listProcurementProduct();

		JSONObject jsonObject = new JSONObject();
		procurementProdList.forEach(procurementProduct -> jsonObject.put(String.valueOf(procurementProduct.getId()),
				procurementProduct.getName()));
		try {
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			out.print(jsonObject.toString().replace("\"", "").replace(",", ";"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void populateVariety() {
		JSONArray varietyArr = new JSONArray();
		if (!StringUtil.isEmpty(procurementProductId)) {
			List<Object[]> varietyList = productDistributionService
					.listProcurementProductByVariety(Long.valueOf(procurementProductId));

			if (!ObjectUtil.isEmpty(varietyList)) {
				for (Object[] variety : varietyList) {
					varietyArr.add(getJSONObject(String.valueOf(variety[0]),
							String.valueOf(variety[1]) + " - " + String.valueOf(variety[2])));
				}
			}

		}

		sendAjaxResponse(varietyArr);

	}

	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}

	public List<ProcurementVariety> getProcurementVarietyList() {
		return procurementVarietyList;
	}

	public void setProcurementVarietyList(List<ProcurementVariety> procurementVarietyList) {
		this.procurementVarietyList = procurementVarietyList;
	}

	public String getProcurementProductId() {
		return procurementProductId;
	}

	public void setProcurementProductId(String procurementProductId) {
		this.procurementProductId = procurementProductId;
	}

}
