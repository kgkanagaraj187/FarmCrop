/*
 * ProcurementProductEnrollAction.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view.general;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.profile.CropHarvestDetails;
import com.ese.entity.profile.CropSupplyDetails;
import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.dao.IProductDistributionDAO;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

public class ProcurementProductEnrollAction extends SwitchValidatorAction {

	private static final long serialVersionUID = 1L;

	private IProductDistributionService productDistributionService;
	private IProductDistributionDAO productDistributionDAO;

	private ProcurementProduct procurementProduct;

	private String id;
	private String cropName;
	private String unit;
	private String cropCategory;
	private String mspRate;
	private String mspPercentage;
    private String selectedVariety;
    
	public String getMspPercentage() {
		return mspPercentage;
	}

	public void setMspPercentage(String mspPercentage) {
		this.mspPercentage = mspPercentage;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Autowired
	private IUniqueIDGenerator idGenerator;
	private Map<Integer, String> cropCategoryList;
	private Map<Integer, String> cropTypeList;
	@Autowired
	private ICatalogueService catalogueService;
	private List<ProcurementVariety> procurementVarietyList = new ArrayList<>();
	private List<FarmCatalogue> subUomList = new ArrayList<FarmCatalogue>();
	private Map<String, String> cropCatList = new HashMap<String, String>();
	
	/**
	 * @see com.sourcetrace.esesw.view.SwitchAction#data()
	 */
	@SuppressWarnings("unchecked")
	public String data() throws Exception {

		Map<String, String> searchRecord = getJQGridRequestParam(); // get the
																	// search
																	// parameter
																	// with
																	// value

		ProcurementProduct filter = new ProcurementProduct();

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

		if (!StringUtil.isEmpty(searchRecord.get("cropName"))) {
			filter.setName(searchRecord.get("cropName").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("unit"))) {
			filter.setUnit(searchRecord.get("unit").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("cropCategory"))) {
			filter.setCropCategory(searchRecord.get("cropCategory").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("mspRate"))) {
			filter.setMspRate(Double.valueOf(searchRecord.get("mspRate").trim()));
		}
		
		if (!StringUtil.isEmpty(searchRecord.get("mspPercentage"))) {
			filter.setMspPercentage(Double.valueOf(searchRecord.get("mspPercentage").trim()));
		}
		
		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		return sendJQGridJSONResponse(data);
	}

	/**
	 * @see com.sourcetrace.esesw.view.SwitchAction#toJSON(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		ProcurementProduct product = (ProcurementProduct) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(product.getBranchId())))
						? getBranchesMap().get(getParentBranchMap().get(product.getBranchId()))
						: getBranchesMap().get(product.getBranchId()));
			}
			rows.add(getBranchesMap().get(product.getBranchId()));

		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(branchesMap.get(product.getBranchId()));
			}
		}
		rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">" + product.getCode() + "</font>");
		
		String name = getLanguagePref(getLoggedInUserLanguage(), product.getCode().trim().toString());
		if(!StringUtil.isEmpty(name) && name != null){
			rows.add(name);
		}else{
			rows.add(product.getName());
		}
		
		
		rows.add(product.getUnit());
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.KPF_TENANT_ID)||getCurrentTenantId().equalsIgnoreCase(ESESystem.SIMFED_TENANT_ID)) {
		rows.add(!StringUtil.isEmpty(product.getCropCategory()) ? getText("cs" + product.getCropCategory()) : "");
		}
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)) {
			rows.add(String.valueOf(product.getMspRate()));
			rows.add(String.valueOf(product.getMspPercentage()));
		}
		jsonObject.put("id", product.getId());
		jsonObject.put("cell", rows);
		return jsonObject;
	}

	/*
	 * public String getCropTypeText() {
	 * 
	 * 
	 * StringBuffer sb = new StringBuffer();
	 * 
	 * 
	 * List<ProcurementProduct> dataList =
	 * productDistributionService.listcropCategory();
	 * sb.append("-1:").append(FILTER_ALL).append(";");
	 * 
	 * for (ProcurementProduct pp : dataList) {
	 * sb.append(pp.getCropCategory()).append(":").append(pp.getCropCategory()).
	 * append(";"); }
	 * 
	 * String data = sb.toString(); return data.substring(0, data.length() - 1);
	 * 
	 * }
	 */
	/**
	 * Creates the.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings("unchecked")
	public void create() throws Exception {
		/*
		 * ProcurementProduct aProcurementProduct = (ProcurementProduct) object;
		 */

		if (!StringUtil.isEmpty(cropName) && !StringUtil.isEmpty(unit)) {
			procurementProduct = new ProcurementProduct();
			if (!getCurrentTenantId().equalsIgnoreCase("indong")) {
				procurementProduct.setCode(idGenerator.getProductEnrollIdSeq());
			}
			ProcurementProduct eProcurementProduct = productDistributionService.findProcurementProductByName(cropName);
			if (eProcurementProduct == null) {
				procurementProduct.setName(cropName);
				FarmCatalogue farmCatalogue = catalogueService.findCatalogueByCode(unit);
				if (!ObjectUtil.isEmpty(farmCatalogue)) {
					procurementProduct.setUnit(farmCatalogue.getName());
					procurementProduct.setTypes(farmCatalogue);
				}

				procurementProduct.setCropCategory(StringUtil.isEmpty(cropCategory) ? "" : cropCategory);
				procurementProduct.setBranchId(getBranchId());
				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)) {

					procurementProduct.setMspRate(Double.valueOf(mspRate));
					procurementProduct.setMspPercentage(Double.valueOf(mspPercentage));
				}
				productDistributionService.addProcurementProduct(procurementProduct);
				getJsonObject().put("msg", getText("msg.cropAdded"));
				getJsonObject().put("title", getText("title.success"));
				sendAjaxResponse(getJsonObject());
			} else {
				getJsonObject().put("msg", getText("crop.exist"));
				getJsonObject().put("title", getText("title.error"));
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
		if (!StringUtil.isEmpty(id)) {
			ProcurementProduct temp = productDistributionService.findProcurementProductById(Long.valueOf(id));
			if(!StringUtil.isEmpty(cropName)){
				
				temp.setName(cropName);
				FarmCatalogue farmCatalogue = catalogueService.findCatalogueByCode(unit);
				if (!ObjectUtil.isEmpty(farmCatalogue)) {
					temp.setUnit(farmCatalogue.getName());
					temp.setTypes(farmCatalogue);
				}
				// temp.setCropCategory(cropCategory);
				temp.setCropCategory(!StringUtil.isEmpty(temp.getCropCategory()) ? temp.getCropCategory() : "");
				// temp.setCropType(procurementProduct.getCropType());
				// temp.setCropCategory(procurementProduct.getCropCategory());
				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)) {

					temp.setMspRate(Double.valueOf(mspRate));
					temp.setMspPercentage(Double.valueOf(mspPercentage));
				}
				productDistributionService.editProcurementProduct(temp);
				getJsonObject().put("msg", getText("msg.cropUpdated"));
				getJsonObject().put("title", getText("title.success"));	
			}else{
				getJsonObject().put("msg", getText("Please Enter Crop Name"));
    			getJsonObject().put("title",getText("Error"));
			}
			
			sendAjaxResponse(getJsonObject());

		}

	}

	/* *//**
			 * Detail.
			 * 
			 * @return the string
			 * @throws Exception
			 *             the exception
			 *//*
			 * public String detail() throws Exception {
			 * 
			 * String view = ""; if (id != null && !id.equals("") &&
			 * !id.equals("null")) { procurementProduct =
			 * productDistributionService.findProcurementProductById(Long.
			 * valueOf(id)); if (procurementProduct == null) {
			 * addActionError(NO_RECORD); return REDIRECT; }
			 * setCurrentPage(getCurrentPage());
			 * 
			 * if(!StringUtil.isEmpty(procurementProduct.getCropCategory())&&
			 * Integer.valueOf(procurementProduct.getCropCategory())>-1){ String
			 * cropCategories=getText("cropCategories"); String[]
			 * cropCategory=cropCategories.split(",");
			 * procurementProduct.setCropCategory((cropCategory[Integer.valueOf(
			 * procurementProduct.getCropCategory())])); }else{
			 * procurementProduct.setCropCategory(""); }
			 * if(!StringUtil.isEmpty(procurementProduct.getCropType())&&
			 * Integer.valueOf(procurementProduct.getCropType())>-1){ String
			 * cropTypes=getText("cropTypes"); String[]
			 * cropTyp=cropTypes.split(",");
			 * procurementProduct.setCropType((cropTyp[Integer.valueOf(
			 * procurementProduct.getCropType())])); }else{
			 * procurementProduct.setCropType(""); } command = UPDATE; view =
			 * DETAIL; request.setAttribute(HEADING, getText(DETAIL)); } else {
			 * request.setAttribute(HEADING, getText(LIST)); return LIST; }
			 * return view; }
			 */

	@SuppressWarnings("unchecked")
	public void populateDelete() throws Exception {
		if (id != null) {
			procurementProduct = productDistributionService.findProcurementProductById(Long.parseLong(id));

			if (!ObjectUtil.isEmpty(procurementProduct)) {

				List<CropHarvestDetails> cropHarvetsDetailsList = productDistributionService
						.listCropHarvestDetailsByProcurementProductId(Long.valueOf(id));
				List<CropSupplyDetails> cropSaleDetailsList = productDistributionService
						.listCropSaleDetailsByProcurementProductId(Long.valueOf(id));
				List<ProcurementVariety> procurementVarietyList = productDistributionService
						.listProcurementVarietyByProcurementProductId(Long.valueOf(id));

				if (!ObjectUtil.isListEmpty(cropHarvetsDetailsList)) {
					getJsonObject().put("msg", getText("cropHarvestCrop.exist"));
					getJsonObject().put("title", getText("title.error"));

				}

				if (!ObjectUtil.isListEmpty(cropSaleDetailsList)) {
					getJsonObject().put("msg", getText("cropSupplyCrop.exist"));
					getJsonObject().put("title", getText("title.error"));
				}

				if (!ObjectUtil.isListEmpty(procurementVarietyList)) {
					getJsonObject().put("msg", getText("procurementdeleteCrop.warn"));
					getJsonObject().put("title", getText("title.error"));
				} else {
					productDistributionService.remove(procurementProduct);
					getJsonObject().put("msg", getText("cropmsg.deleted"));
					getJsonObject().put("title", getText("title.success"));
				}
			}

			sendAjaxResponse(getJsonObject());
		}
	}

	/**
	 * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
	 */
	@Override
	public Object getData() {

		return procurementProduct;
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
	 * Gets the product distribution service.
	 * 
	 * @return the product distribution service
	 */
	public IProductDistributionService getProductDistributionService() {

		return productDistributionService;
	}

	/**
	 * Sets the procurement product.
	 * 
	 * @param procurementProduct
	 *            the new procurement product
	 */
	public void setProcurementProduct(ProcurementProduct procurementProduct) {

		this.procurementProduct = procurementProduct;
	}

	/**
	 * Gets the procurement product.
	 * 
	 * @return the procurement product
	 */
	public ProcurementProduct getProcurementProduct() {

		return procurementProduct;
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

	public IUniqueIDGenerator getIdGenerator() {
		return idGenerator;
	}

	public void setIdGenerator(IUniqueIDGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	public Map<Integer, String> getCropCategoryList() {
		Map<Integer, String> cropCategories = new LinkedHashMap<Integer, String>();
		String categories = getText("cropCategories");
		String[] cropCategory = categories.split(",");
		Arrays.sort(cropCategory);
		int i = 0;
		for (String cropCat : cropCategory) {
			cropCategories.put(Integer.valueOf(i++), cropCat);
		}
		return cropCategories;
	}

	public Map<String, String> getCropCatList() {
		cropCatList.put("0", getText("main"));
		cropCatList.put("1", getText("inter"));

		return cropCatList;
	}
	
	public void setCropCategoryList(Map<Integer, String> cropCategoryList) {
		this.cropCategoryList = cropCategoryList;
	}

	public Map<Integer, String> getCropTypeList() {
		Map<Integer, String> cropTypeMap = new LinkedHashMap<Integer, String>();
		String cropType = getText("cropTypes");
		String[] crops = cropType.split(",");
		Arrays.sort(crops);
		int i = 0;
		for (String cropTypes : crops) {
			cropTypeMap.put(Integer.valueOf(i++), cropTypes);
		}
		return cropTypeMap;
	}

	public void setCropTypeList(Map<Integer, String> cropTypeList) {

		this.cropTypeList = cropTypeList;
	}

	public String getCropName() {
		return cropName;
	}

	public void setCropName(String cropName) {
		this.cropName = cropName;
	}

	public List<ProcurementProduct> getProcurementProductList() {

		return productDistributionService.listProcurementProduct();
	}

	/*
	 * public List<ProcurementVariety> getProcurementVarietyList() { return
	 * productDistributionService.listProcurementVariety();
	 * 
	 * }
	 */

	public List<FarmCatalogue> getListUom() {
		List<FarmCatalogue> subUomList = catalogueService.listCataloguesByUnit();

		return subUomList;
	}

	public List<ProcurementVariety> getProcurementVarietyList() {
		return procurementVarietyList;
	}

	public void setProcurementVarietyList(List<ProcurementVariety> procurementVarietyList) {
		this.procurementVarietyList = procurementVarietyList;
	}

	public void populateUnit() throws Exception {

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

	public void populateCropCategory() throws Exception {
		JSONObject jsonObject = new JSONObject();
		if (cropCatList != null) {
			/*
			 * cropCatList.stream().filter(procurementProduct ->
			 * !StringUtil.isEmpty(procurementProduct.getCropCategory()))
			 * .forEach(procurementProduct -> jsonObject.p
			 * ut(String.valueOf(procurementProduct.getId()),
			 * procurementProduct.getCropCategory()));
			 */
			getCropCatList().forEach((key, value) -> {
				jsonObject.put(key, value);
			});
			try {
				PrintWriter out = response.getWriter();
				response.setContentType("application/json");
				out.print(jsonObject.toString().replace("\"", "").replace(",", ";"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public List<FarmCatalogue> getSubUomList() {
		return subUomList;
	}

	public void setSubUomList(List<FarmCatalogue> subUomList) {
		this.subUomList = subUomList;
	}

	public void setCropCatList(Map<String, String> cropCatList) {
		this.cropCatList = cropCatList;
	}

	public String getCropCategory() {
		return cropCategory;
	}

	public void setCropCategory(String cropCategory) {
		this.cropCategory = cropCategory;
	}

	public String getMspRate() {
		return mspRate;
	}

	public void setMspRate(String mspRate) {
		this.mspRate = mspRate;
	}

	public String getSelectedVariety() {
		return selectedVariety;
	}

	public void setSelectedVariety(String selectedVariety) {
		this.selectedVariety = selectedVariety;
	}
   
	

}
