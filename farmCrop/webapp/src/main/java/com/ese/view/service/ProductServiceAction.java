/*
 * ProductServiceAction.java
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.BreadCrumb;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.order.entity.profile.Category;
import com.sourcetrace.eses.order.entity.profile.SubCategory;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.ICategoryService;
import com.sourcetrace.eses.service.IESETxnService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.profile.Product;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

/**
 * The Class ProductServiceAction.
 */
public class ProductServiceAction extends SwitchValidatorAction {

	private static final long serialVersionUID = -3684050656083312784L;
	private String id;
	private Product product;
	private ICategoryService categoryService;
	private IProductService productService;
	private String selectedCategory = "Vegetables";
	private List<SubCategory> subCategoryList = new ArrayList<SubCategory>();
	private List<String> unitMeasurements;
	private String selectedMeasurement;
	private IESETxnService txnService;
	private IProductDistributionService productDistributionService;

	private String tabIndex = "#tabs-1";
	private String tabIndexsubCategoryService = "#tabs-2";
	private String subCategoryId;
	private String selectedProductName;
	private String selectedUnit;
	private String selectedPrice;
	private List<FarmCatalogue> listManufacture=new ArrayList<FarmCatalogue>();
	private List<FarmCatalogue> listIngredient=new ArrayList<FarmCatalogue>();
	private String manufacture;
	private String ingredient;
	public String getSelectedUnit() {
		return selectedUnit;
	}

	public void setSelectedUnit(String selectedUnit) {
		this.selectedUnit = selectedUnit;
	}

	public String getSelectedPrice() {
		return selectedPrice;
	}

	public void setSelectedPrice(String selectedPrice) {
		this.selectedPrice = selectedPrice;
	}

	private String subCategoryCode;
	private String subCategoryName;
	private String prodPrice;

	@Autowired
	private ICatalogueService catalogueService;

	private String selectedUom = "mm";
	private List<FarmCatalogue> subUomList = new ArrayList<FarmCatalogue>();

	@Autowired
	private IUniqueIDGenerator idGenerator;

	/**
	 * Data.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 * @see com.sourcetrace.esesw.view.SwitchAction#data()
	 */
	@SuppressWarnings("unchecked")
	public String data() throws Exception {

		Map<String, String> searchRecord = getJQGridRequestParam(); // get the
																	// search
																	// parameter
																	// with
		// value

		Product filter = new Product();
		SubCategory subCategory = new SubCategory();

		if (!StringUtil.isEmpty(searchRecord.get("code")))
			filter.setCode(searchRecord.get("code").trim());

		if (!StringUtil.isEmpty(searchRecord.get("selectedProductName")))
			filter.setName(searchRecord.get("selectedProductName").trim());

		if (!StringUtil.isEmpty(searchRecord.get("selectedUnit")))
			filter.setUnit(searchRecord.get("selectedUnit").trim());

		if (!StringUtil.isEmpty(searchRecord.get("selectedPrice")))
			filter.setPrice(searchRecord.get("selectedPrice").trim());

		if (!StringUtil.isEmpty(searchRecord.get("subCategoryId"))) {

			subCategory.setName(searchRecord.get("subCategoryId"));
			filter.setSubcategory(subCategory);

		}
		if(!StringUtil.isEmpty(searchRecord.get("manufacture")))
			filter.setManufacture(searchRecord.get("manufacture").trim());
		if(!StringUtil.isEmpty(searchRecord.get("ingredient")))
			filter.setIngredient(searchRecord.get("ingredient").trim());

		/*if (!StringUtil.isEmpty(getBranchId())) {
			subCategory.setBranchId(getBranchId());
			filter.setSubcategory(subCategory);
		}
*/
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
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		Product product = (Product) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">" + product.getCode() + "</font>");

		

		 String subcategory = getLanguagePref(getLoggedInUserLanguage(), product.getSubcategory().getCode().trim().toString());
	   		if(!StringUtil.isEmpty(subcategory) && subcategory != null){
	   			rows.add(subcategory);
	   		}else{
	   			rows.add(product.getSubcategory().getName());
	   		}

		
		 String productName = getLanguagePref(getLoggedInUserLanguage(), product.getCode().trim().toString());
	   		if(!StringUtil.isEmpty(productName) && productName != null){
	   			rows.add(productName);
	   		}else{
	   			rows.add(product.getName());
	   		}
		
		rows.add(product.getUnit());
		if(getCurrentTenantId().equals(ESESystem.GRIFFITH_TENANT_ID)){
		rows.add(product.getPrice());
		rows.add(catalogueService.findCatalogueByCode(product.getManufacture())!=null && !ObjectUtil.isEmpty(catalogueService.findCatalogueByCode(product.getManufacture()))?catalogueService.findCatalogueByCode(product.getManufacture()).getName():"");
		rows.add(catalogueService.findCatalogueByCode(product.getIngredient())!=null && !ObjectUtil.isEmpty(catalogueService.findCatalogueByCode(product.getIngredient()))?catalogueService.findCatalogueByCode(product.getIngredient()).getName():"");
		}
		rows.add(product.getPrice());

		jsonObject.put("id", product.getId());
		jsonObject.put("cell", rows);
		return jsonObject;
	}

	/**
	 * Creates the.
	 * 
	 * @return the string
	 */
	@SuppressWarnings("unchecked")
	public void create() {
		if (subCategoryId != null && selectedProductName != null) {
			try {
				this.product = new Product();
				
				if(getCurrentTenantId().equals(ESESystem.GRIFFITH_TENANT_ID)){
					Product prod=productService.findProuductByProudctNameSubCategoryIdAndManufacture(selectedProductName,Long.valueOf(subCategoryId),catalogueService.findCatalogueByCode(manufacture).getName());
					if(prod ==null || ObjectUtil.isEmpty(prod)){
						SubCategory subcategory = categoryService.findSubCategoryById(Long.valueOf(subCategoryId));
						this.product.setSubcategory(subcategory);
						FarmCatalogue farmCatalogue = catalogueService.findCatalogueByCode(selectedUnit);
						if (!ObjectUtil.isEmpty(farmCatalogue)) {
							this.product.setUnit(farmCatalogue.getName());
							this.product.setType(farmCatalogue);
						}
							this.product.setManufacture(manufacture);
							this.product.setIngredient(ingredient);
							product.setName(selectedProductName);
							this.product.setPrice(selectedPrice);

							product.setCode(idGenerator.getProductIdSeq());
							productService.addProduct(product);
							getJsonObject().put("msg", getText("msg.productAdded"));
							getJsonObject().put("title", getText("title.success"));
						}
					else {
						getJsonObject().put("msg", getLocaleProperty("unique.ProductName"));
						getJsonObject().put("title", getText("title.error"));

					}
					}
				
				else{
				Product eProduct = productService.findProductByProductNameAndSubCategoryId(selectedProductName,
						Long.valueOf(subCategoryId));
				if (ObjectUtil.isEmpty(eProduct)) {
					SubCategory subcategory = categoryService.findSubCategoryById(Long.valueOf(subCategoryId));

					this.product.setSubcategory(subcategory);

					FarmCatalogue farmCatalogue = catalogueService.findCatalogueByCode(selectedUnit);
					if (!ObjectUtil.isEmpty(farmCatalogue)) {
						this.product.setUnit(farmCatalogue.getName());
						this.product.setType(farmCatalogue);
					}
					
					product.setName(selectedProductName);

					if (!StringUtil.isEmpty(selectedPrice))
						this.product.setPrice(CurrencyUtil.currencyFormatter(Double.valueOf(selectedPrice)));

					product.setCode(idGenerator.getProductIdSeq());
					productService.addProduct(product);
					getJsonObject().put("msg", getText("prdmsg.added"));
					getJsonObject().put("title", getText("title.success"));
				} else {
					getJsonObject().put("msg", getLocaleProperty("unique.ProductName"));
					getJsonObject().put("title", getText("title.error"));

				}
				}
				sendAjaxResponse(getJsonObject());
				
				
			} catch (Exception e) {
				e.printStackTrace();
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
	public String update() throws Exception {

		if (id != null) {
			JSONObject jsonObject = new JSONObject();
			Product eProduct = productService.findProductByProductNameAndSubCategoryId(selectedProductName,
					Long.valueOf(subCategoryId));
			Product existing = productService.findProductById(Long.valueOf(id));
			Object[] obj = productService.findByProdNameAndId(selectedProductName);

			if (!ObjectUtil.isEmpty(eProduct) && eProduct.getId() != Long.valueOf(id)) {
				jsonObject.put("msg", getLocaleProperty("unique.ProductName"));
				jsonObject.put("title", "Error");
				sendAjaxResponse(jsonObject);
				return null;
			}

			if (ObjectUtil.isEmpty(obj) || obj[0].toString().equalsIgnoreCase(id) ) {
				if (existing == null) {
					addActionError(NO_RECORD);
				}
				SubCategory subcategory = categoryService.findSubCategoryById(Long.valueOf(subCategoryId));
				FarmCatalogue farmCatalogue = catalogueService.findCatalogueByCode(selectedUnit);
				if (!ObjectUtil.isEmpty(farmCatalogue)) {
					existing.setType(farmCatalogue);
					existing.setUnit(farmCatalogue.getName());
				}
				if(getCurrentTenantId().equalsIgnoreCase(ESESystem.GRIFFITH_TENANT_ID)){
					existing.setManufacture(catalogueService.findCatalogueByCode(manufacture).getCode());
					existing.setIngredient(catalogueService.findCatalogueByCode(ingredient).getCode());
				}
				existing.setSubcategory(subcategory);
				if (!StringUtil.isEmpty(selectedProductName)) {
					existing.setName(selectedProductName);

					// existing.setUnit(product.getUnit()+ "-"
					// +selectedMeasurement);
					// Default Unit is added as "1-Unit"

					if (!StringUtil.isEmpty(selectedPrice))
						existing.setPrice(CurrencyUtil.currencyFormatter(Double.valueOf(selectedPrice)));
					else
						existing.setPrice("");
					jsonObject.put("msg", getText("msg.productUpdate"));
					jsonObject.put("title", "Success");
					// addActionError("Update Successfully!!!");
					productService.editProduct(existing);
					request.setAttribute(HEADING, getText(LIST));
					jsonObject.put("msg", getText("msg.productUpdate"));
				} else {
					jsonObject.put("msg", getLocaleProperty("empty.ProductName"));
					jsonObject.put("title", "Error");
				}
			} else if (eProduct == null) {
				existing.setName(selectedProductName);
				productService.editProduct(existing);
				jsonObject.put("msg", getText("msg.productUpdate"));
				jsonObject.put("title", "Success");
			} else {
				if (!ObjectUtil.isEmpty(eProduct) && Long.valueOf(String.valueOf(obj[0])) != Long.valueOf(id)) {
					jsonObject.put("msg", getText("msg.productUpdate"));
					jsonObject.put("title", "Success");
				}
			}
			sendAjaxResponse(jsonObject);
		}
		return null;
	}

	/**
	 * Delete.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public void delete() throws Exception {

		if (id != null && !id.equals("")) {
			JSONObject jsonObject = new JSONObject();
			product = productService.findProductById(Long.parseLong(id));
			if (product == null) {
				addActionError(NO_RECORD);
			} else {
				boolean isTxnExist = txnService.findTxnExistForProductByProductId(product.getId());
				boolean isWarehouseMapExist = productService.findWarehouseProductMappingExist(product.getId());
				if (!isTxnExist && !isWarehouseMapExist) {
					productDistributionService.removeWarehouseProductByProductId(product.getId());
					productService.removeProduct(product);
					jsonObject.put("msg", getText("productmsg.deleted"));
				}
				if (isTxnExist) {
					// addActionError(getText("txnExist.product"));
					jsonObject.put("msg", getText("txnExist.product"));
				} else if (isWarehouseMapExist) {
					// addActionError(getText("mappingExist.product"));
					jsonObject.put("msg", getText("mappingExist.product"));
				}

			}
			sendAjaxResponse(jsonObject);
		}
	}

	/**
	 * Gets the data.
	 * 
	 * @return the data
	 * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
	 */
	@Override
	public Object getData() {

		/*
		 * unitMeasurements = new ArrayList<String>();
		 * unitMeasurements.add("gm"); unitMeasurements.add("kg");
		 * unitMeasurements.add("lt"); unitMeasurements.add("ml");
		 * unitMeasurements.add("pc"); unitMeasurements.add("pk");
		 * 
		 * return product;
		 */
		return product;
	}

	public List<FarmCatalogue> getListUom() {

		if (!StringUtil.isEmpty(selectedUom)) {
			subUomList = catalogueService.listCataloguesByUnit();
		}
		return subUomList;
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
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {

		this.id = id;
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
	 * Sets the product.
	 * 
	 * @param product
	 *            the new product
	 */
	public void setProduct(Product product) {

		this.product = product;
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
	 * Sets the category service.
	 * 
	 * @param categoryService
	 *            the new category service
	 */
	public void setCategoryService(ICategoryService categoryService) {

		this.categoryService = categoryService;
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
	 * Gets the list sub category.
	 * 
	 * @return the list sub category
	 */
	public List<SubCategory> getListSubCategory() {

		subCategoryList = categoryService.listSubCategory();
		return subCategoryList;
	}

	/**
	 * Gets the list category.
	 * 
	 * @return the list category
	 */
	public List<Category> getListCategory() {

		List<Category> listCategory = categoryService.listCategory();
		return listCategory;

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
	 * Sets the txn service.
	 * 
	 * @param txnService
	 *            the new txn service
	 */
	public void setTxnService(IESETxnService txnService) {

		this.txnService = txnService;
	}

	/**
	 * Gets the txn service.
	 * 
	 * @return the txn service
	 */
	public IESETxnService getTxnService() {

		return txnService;
	}

	/**
	 * Sets the product distribution service.
	 * 
	 * @param productDistributionService
	 *            the productDistributionService to set
	 */
	public void setProductDistributionService(IProductDistributionService productDistributionService) {

		this.productDistributionService = productDistributionService;
	}

	/**
	 * Gets the product distribution service.
	 * 
	 * @return the productDistributionService
	 */
	public IProductDistributionService getProductDistributionService() {

		return productDistributionService;
	}

	/**
	 * Sets the unit measurements.
	 * 
	 * @param unitMeasurements
	 *            the new unit measurements
	 */
	public void setUnitMeasurements(List<String> unitMeasurements) {

		this.unitMeasurements = unitMeasurements;
	}

	/**
	 * Gets the unit measurements.
	 * 
	 * @return the unit measurements
	 */
	public List<String> getUnitMeasurements() {

		return unitMeasurements;
	}

	/**
	 * Sets the selected measurement.
	 * 
	 * @param selectedMeasurement
	 *            the new selected measurement
	 */
	public void setSelectedMeasurement(String selectedMeasurement) {

		this.selectedMeasurement = selectedMeasurement;
	}

	/**
	 * Gets the selected measurement.
	 * 
	 * @return the selected measurement
	 */
	public String getSelectedMeasurement() {

		return selectedMeasurement;
	}

	/**
	 * Gets the customer detail params.
	 * 
	 * @return the customer detail params
	 */
	public String getSubCategoryDetailParams() {

		return "tabIndex=" + URLEncoder.encode(tabIndex) + "&id=" + getSubCategoryId() + "&" + tabIndex;
	}

	@SuppressWarnings("deprecation")
	public String getSubCategoryDetailzParams() {

		return "tabIndex=" + URLEncoder.encode(tabIndexsubCategoryService) + "&id=" + getSubCategoryId() + "&"
				+ tabIndexsubCategoryService;
	}

	public String getSubCategoryId() {

		return subCategoryId;
	}

	public void setSubCategoryId(String subCategoryId) {

		this.subCategoryId = subCategoryId;
	}

	public String getTabIndex() {

		return tabIndex;
	}

	public void setTabIndex(String tabIndex) {

		this.tabIndex = tabIndex;
	}

	public String getTabIndexsubCategoryService() {

		return tabIndexsubCategoryService;
	}

	public void setTabIndexsubCategoryService(String tabIndexsubCategoryService) {

		this.tabIndexsubCategoryService = tabIndexsubCategoryService;
	}

	public String getSubCategoryCode() {

		return subCategoryCode;
	}

	public void setSubCategoryCode(String subCategoryCode) {

		this.subCategoryCode = subCategoryCode;
	}

	public String getSubCategoryName() {

		return subCategoryName;
	}

	public void setSubCategoryName(String subCategoryName) {

		this.subCategoryName = subCategoryName;
	}

	/*
	 * public void prepare() throws Exception { String subCategoryId = (String)
	 * request.getParameter("subCategoryId");
	 * request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(
	 * getText(BreadCrumb.BREADCRUMB, "") + subCategoryId + "&" +
	 * getTabIndexsubCategoryService())); }
	 */
	public String getProdPrice() {

		return prodPrice;
	}

	public void setProdPrice(String prodPrice) {

		this.prodPrice = prodPrice;
	}

	public IUniqueIDGenerator getIdGenerator() {
		return idGenerator;
	}

	public void setIdGenerator(IUniqueIDGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	public String getSelectedUom() {
		return selectedUom;
	}

	public void setSelectedUom(String selectedUom) {
		this.selectedUom = selectedUom;
	}

	public void populateSubCategory() {
		subCategoryList = categoryService.listSubCategory();

		JSONObject jsonObject = new JSONObject();
		subCategoryList.forEach(category -> jsonObject.put(String.valueOf(category.getId()), category.getName()));
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
	public void populateManufacture(){
		listManufacture=catalogueService.listCataloguesByType(getText("icsNameType"));
		JSONObject jsonObj = new JSONObject();
		listManufacture.forEach(m-> jsonObj.put(String.valueOf(m.getCode()), m.getName()) );
		try{
			PrintWriter out=response.getWriter();
			response.setContentType("application/json");
			out.println(jsonObj.toString().replace("\"", "").replace(",",";"));
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	public void populateIngredient() {
		listIngredient=catalogueService.listCataloguesByType(getText("icsUnitType"));
		JSONObject json=new JSONObject();
		listIngredient.forEach(ingr -> json.put(ingr.getCode(), ingr.getName() ));
		try{
			PrintWriter out=response.getWriter();
			response.setContentType("application/json");
			out.println(json.toString().replace("\"", "").replace(",", ";"));
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
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

	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}

	public String getSelectedProductName() {
		return selectedProductName;
	}

	public void setSelectedProductName(String selectedProductName) {
		this.selectedProductName = selectedProductName;
	}

	public void populateProductSave() {
		if (subCategoryId != null && selectedProductName != null) {
			try {
				this.product = new Product();

				Product eProduct = productService.findProductByProductNameAndSubCategoryId(selectedProductName,
						Long.valueOf(subCategoryId));
				if (ObjectUtil.isEmpty(eProduct)) {
					SubCategory subcategory = categoryService.findSubCategoryById(Long.valueOf(subCategoryId));

					this.product.setSubcategory(subcategory);

					FarmCatalogue farmCatalogue = catalogueService.findCatalogueByCode(selectedUnit);
					if (!ObjectUtil.isEmpty(farmCatalogue)) {
						this.product.setUnit(farmCatalogue.getName());
						this.product.setType(farmCatalogue);
					}

					product.setName(selectedProductName);

					if (!StringUtil.isEmpty(selectedPrice))
						this.product.setPrice(CurrencyUtil.currencyFormatter(Double.valueOf(selectedPrice)));

					product.setCode(idGenerator.getProductIdSeq());
					productService.addProduct(product);
					getJsonObject().put("msg", getText("msg.added"));
					getJsonObject().put("title", getText("title.success"));
				} else {
					getJsonObject().put("msg", getLocaleProperty("unique.ProductName"));
					getJsonObject().put("title", getText("title.error"));

				}
				sendAjaxResponse(getJsonObject());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String populateProductList() throws Exception {
		Map<String, String> searchRecord = getJQGridRequestParam(); // get the
		// search
		// parameter
		// with
		// value

		Product filter = new Product();
		SubCategory subCategory = new SubCategory();

		if (!StringUtil.isEmpty(searchRecord.get("code")))
			filter.setCode(searchRecord.get("code").trim());

		if (!StringUtil.isEmpty(searchRecord.get("selectedProductName")))
			filter.setName(searchRecord.get("selectedProductName").trim());
		if (!StringUtil.isEmpty(getBranchId())) {
			subCategory.setBranchId(getBranchId());
		}
		if (!StringUtil.isEmpty(searchRecord.get("selectedUnit")))
			filter.setUnit(searchRecord.get("selectedUnit").trim());

		if (!StringUtil.isEmpty(searchRecord.get("selectedPrice")))
			filter.setPrice(searchRecord.get("selectedPrice").trim());

		if (!StringUtil.isEmpty(searchRecord.get("subCategoryId"))) {

			subCategory.setName(searchRecord.get("subCategoryId"));
			filter.setSubcategory(subCategory);

		}

		if (!StringUtil.isEmpty(getBranchId())) {
			subCategory.setBranchId(getBranchId());
			filter.setSubcategory(subCategory);
		}

		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		return sendJQGridJSONResponse(data);
	}
	public List<FarmCatalogue> getListManufacture(){
		listManufacture=catalogueService.listCataloguesByType(getText("icsNameType"));
		return listManufacture;
	}
	public List<FarmCatalogue> getListIngredient(){
		listIngredient=catalogueService.listCataloguesByType(getText("icsUnitType"));
		return listIngredient;
		
	}

	public String getManufacture() {
		return manufacture;
	}

	public void setManufacture(String manufacture) {
		this.manufacture = manufacture;
	}

	public String getIngredient() {
		return ingredient;
	}

	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}
	
}
