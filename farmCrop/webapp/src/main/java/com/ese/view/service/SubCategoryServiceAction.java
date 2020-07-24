/*
 * SubCategoryServiceAction.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.order.entity.profile.Category;
import com.sourcetrace.eses.order.entity.profile.SubCategory;
import com.sourcetrace.eses.order.entity.txn.DistributionDetail;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.ICategoryService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.view.SwitchValidatorAction;


/**
 * The Class SubCategoryServiceAction.
 */
public class SubCategoryServiceAction extends SwitchValidatorAction {

	private static final long serialVersionUID = -1854656479403084244L;

	private SubCategory subCategory;

	private String id;
	private String categoryName;
	private ICategoryService categoryService;
	private String tabIndex = "#tabs-1";
	private String selectedUom = "mm";
	private List<FarmCatalogue> subUomList = new ArrayList<FarmCatalogue>();
	
	
	@Autowired
	private ICatalogueService catalogueService;

	@Autowired
	private IUniqueIDGenerator idGenerator;
	@Autowired
	private IProductDistributionService productDistributionService;

	@Autowired
	private IFarmerService farmerService;
	/**
	 * Gets the sub category.
	 * 
	 * @return the sub category
	 */
	public SubCategory getSubCategory() {

		return subCategory;
	}

	/**
	 * Sets the sub category.
	 * 
	 * @param subCategory
	 *            the new sub category
	 */
	public void setSubCategory(SubCategory subCategory) {

		this.subCategory = subCategory;
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
	 * Data.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 * @see com.sourcetrace.esesw.view.SwitchAction#data()
	 */
	@SuppressWarnings({ "unchecked" })
	public String data() throws Exception {

		Map<String, String> searchRecord = getJQGridRequestParam();

		SubCategory filter = new SubCategory();

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

		if (!StringUtil.isEmpty(searchRecord.get("code"))) {
			filter.setCode(searchRecord.get("code").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("categoryName"))) {
			filter.setName(searchRecord.get("categoryName").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("c.name"))) {
			Category c = new Category();
			c.setName(searchRecord.get("c.name").trim());
			filter.setCategory(c);
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
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		SubCategory subCategory = (SubCategory) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(subCategory.getBranchId())))
						? getBranchesMap().get(getParentBranchMap().get(subCategory.getBranchId()))
						: getBranchesMap().get(subCategory.getBranchId()));
			}
			rows.add(getBranchesMap().get(subCategory.getBranchId()));

		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(branchesMap.get(subCategory.getBranchId()));
			}
		}
		rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">" + subCategory.getCode() + "</font>");
		
		 String subcategory = getLanguagePref(getLoggedInUserLanguage(), subCategory.getCode().trim().toString());
	   		if(!StringUtil.isEmpty(subcategory) && subcategory != null){
	   			rows.add(subcategory);
	   		}else{
	   			rows.add(subCategory.getName());
	   		}
		
		
		jsonObject.put("id", subCategory.getId());
		jsonObject.put("cell", rows);
		return jsonObject;
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

		SubCategory eSubCategory = categoryService.findSubCategoryByName(categoryName);
		if (ObjectUtil.isEmpty(eSubCategory)) {
			subCategory = new SubCategory();
			subCategory.setName(categoryName);

			subCategory.setCode(idGenerator.getSubCategoryIdSeq());
			subCategory.setBranchId(getBranchId());
			categoryService.addSubCategory(subCategory);

			getJsonObject().put("msg", getText("categorymsg.added"));
			getJsonObject().put("title", getText("title.success"));
		} else {
			getJsonObject().put("msg", getLocaleProperty("unique.SubCategoryName"));
			getJsonObject().put("title", getText("title.error"));

		}
		sendAjaxResponse(getJsonObject());
	}

	/**
	 * Update.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public void update() throws Exception {

		if (id != null) {
			JSONObject jsonObject = new JSONObject();
			SubCategory existing = categoryService.findSubCategoryById(Long.valueOf(id));
			Object[] obj = categoryService.findByCategoryNameAndId(categoryName);
			if (ObjectUtil.isEmpty(obj) || Long.valueOf(String.valueOf(obj[0])) == Long.valueOf(id)) {
				// object
				if(!StringUtil.isEmpty(categoryName)){
					existing.setName(categoryName);
					categoryService.editSubCategory(existing);
					jsonObject.put("msg", "Category Updated Successfully");
				}else{
					jsonObject.put("msg", getLocaleProperty("empty.customerName"));
				}
			}

			else {

				if (Long.valueOf(String.valueOf(obj[0])) != Long.valueOf(id)) {
					jsonObject.put("msg", getLocaleProperty("unique.SubCategoryName"));
					jsonObject.put("title", "Error");
				}
			}

			sendAjaxResponse(jsonObject);
		}
	}

	/**
	 * Delete.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public void delete() throws Exception {
		JSONObject jsonObject = new JSONObject();
		if (this.getId() != null && !(this.getId().equals(EMPTY))) {
			SubCategory subcategory = categoryService.findSubCategoryById(Long.parseLong(id));
			if (subCategory == null) {
				addActionError(NO_RECORD);
			}

			List<DistributionDetail> distributionDetailsList = productDistributionService
					.listDistributionDetailBySubCategory(Long.parseLong(id));
			if (!ObjectUtil.isListEmpty(distributionDetailsList)) {

				// addActionError(getLocaleProperty("distribution.exist"));
				jsonObject.put("msg", getLocaleProperty("distribution.exist"));
				jsonObject.put("title", "Error");
			}
			if (!ObjectUtil.isEmpty(subcategory) && !ObjectUtil.isListEmpty(subcategory.getProducts())) {
				addActionError(getLocaleProperty(("delete.warn")));
				jsonObject.put("msg", getLocaleProperty("delete.warn"));
				jsonObject.put("title", "Error");
			} else {
				if (!ObjectUtil.isEmpty(jsonObject)) {
					categoryService.removeSubCategory(subcategory);
					jsonObject.put("msg", getText("categorymsg.deleted"));
					jsonObject.put("title", "Success");
				}
			}
			sendAjaxResponse(jsonObject);
			// sendResponse("Deleted Success");
			//
		}
		request.setAttribute(HEADING, getText(LIST));
	}

	public void populatesubCategory() throws Exception {
		List<SubCategory> subCategoryList = categoryService.listDistributionDetailBySubCategory();
		JSONArray category = new JSONArray();
		if (!ObjectUtil.isEmpty(subCategoryList)) {
			for (SubCategory SubCategory : subCategoryList) {
				category.add(getJSONObject(SubCategory.getId(), SubCategory.getName()));
			}
		}
		sendAjaxResponse(category);
	}

	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
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
	 * Gets the data.
	 * 
	 * @return the data
	 * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
	 */
	public Object getData() {

		return subCategory;
	}

	public String getTabIndex() {

		return tabIndex;
	}

	public void setTabIndex(String tabIndex) {

		this.tabIndex = tabIndex;
	}

	public IUniqueIDGenerator getIdGenerator() {
		return idGenerator;
	}

	public void setIdGenerator(IUniqueIDGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	private List<SubCategory> subCategoryList = new ArrayList<SubCategory>();

	public List<SubCategory> getListSubCategory() {

		subCategoryList = categoryService.listSubCategory();
		return subCategoryList;
	}
	public List<FarmCatalogue> getListUom() {

		if (!StringUtil.isEmpty(selectedUom)) {
			subUomList = catalogueService.listCataloguesByUnit();
		}
		return subUomList;
	}


	}

