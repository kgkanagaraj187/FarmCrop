/*
 * DistributionBalanceAction.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.profile;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.profile.Product;
import com.sourcetrace.esesw.entity.profile.DistributionBalance;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.Locality;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

// TODO: Auto-generated Javadoc
/**
 * The Class DistributionBalanceAction.
 */
public class DistributionBalanceAction extends SwitchValidatorAction {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(DistributionBalanceAction.class);

	/** The id. */
	private String id;

	private DistributionBalance distributionBalance;

	/** The farmer service. */
	@Autowired
	private IFarmerService farmerService;
	
	@Autowired
	private ILocationService locationService;

	/** The farmer id. */
	private String farmerId;

	/** The farmer name. */
	private String farmerName;

	/** The json object. */
	JSONObject jsonObject = new JSONObject();

	@Autowired
	private IProductService productService;

	private String farmerIdId;

	private String product;

	private String stock;

	private String selectedState;
	
	List<Locality> localities = new ArrayList<Locality>();
	
	private String selectedDistrict;
	
	List<Municipality> cities = new ArrayList<Municipality>();
	List<Farmer> farmerLists = new ArrayList<Farmer>();
	
	private String selectedCity;	
	
	private String farmer;
	/**
	 * Gets the data.
	 * 
	 * @return the data
	 * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
	 */
	@Override
	public Object getData() {

		return distributionBalance;
	}

	/**
	 * @see com.sourcetrace.esesw.view.SwitchAction#list()
	 */
	public String list() throws Exception {

		if (getCurrentPage() != null) {
			setCurrentPage(getCurrentPage());
		}
		request.setAttribute(HEADING, getText(LIST));

		return LIST;
	}

	public String data() throws Exception {

		Map<String, String> searchRecord = getJQGridRequestParam();

		DistributionBalance filter = new DistributionBalance();
		
		if (!StringUtil.isEmpty(searchRecord.get("farmer"))) {
			Farmer farmer = new Farmer();
			farmer.setFirstName(searchRecord.get("farmer").trim());
			filter.setFarmer(farmer);
		}
		
		if (!StringUtil.isEmpty(searchRecord.get("product"))) {
			Product prod = new Product();
			prod.setName(searchRecord.get("product").trim());
			filter.setProduct(prod);
		}
		
		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		return sendJQGridJSONResponse(data);
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
	 * Sets the farmer service.
	 * 
	 * @param farmerService
	 *            the new farmer service
	 */
	public void setFarmerService(IFarmerService farmerService) {

		this.farmerService = farmerService;
	}

	/**
	 * Gets the farmer service.
	 * 
	 * @return the farmer service
	 */
	public IFarmerService getFarmerService() {

		return farmerService;
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
	 * Sets the farmer id.
	 * 
	 * @param farmerId
	 *            the new farmer id
	 */
	public void setFarmerId(String farmerId) {

		this.farmerId = farmerId;
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
	 * Sets the farmer name.
	 * 
	 * @param farmerName
	 *            the new farmer name
	 */
	public void setFarmerName(String farmerName) {

		this.farmerName = farmerName;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		DistributionBalance db = (DistributionBalance) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();

		if(!ObjectUtil.isEmpty(db)){
		rows.add(db.getFarmer().getName());
		rows.add(db.getProduct().getName());
		rows.add(db.getStock());
		}
		jsonObject.put("id", db.getId());
		jsonObject.put("cell", rows);
		return jsonObject;

	}

	public void populateDistributionBalance() throws Exception {

		distributionBalance = new DistributionBalance();

		Farmer farmer = farmerService.findFarmerById(Long.valueOf(farmerIdId));
		Product pro = productService.findProductByProductCode(product);

		if (!ObjectUtil.isEmpty(farmer) && !ObjectUtil.isEmpty(pro)) {

			distributionBalance.setFarmer(farmer);
			distributionBalance.setProduct(pro);
			distributionBalance.setStock(Double.valueOf(stock));
			distributionBalance.setRevisionNo(DateUtil.getRevisionNumber());
			farmerService.addDistributionBalance(distributionBalance);
			getJsonObject().put("msg", getText("msg.added"));
			getJsonObject().put("title", getText("title.success"));
			sendAjaxResponse(getJsonObject());
		}

	}

	public void sendAjaxResponse(JSONObject jsonObject) {

		try {
			response.setContentType("text/json");
			PrintWriter out = response.getWriter();
			out.println(jsonObject.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void populateUpdate() {
		if (!StringUtil.isEmpty(getId())) {
			DistributionBalance distBal = farmerService.findDistributionBalanceById(Long.valueOf(getId()));
			if (!ObjectUtil.isEmpty(distBal)) {
				distBal.setStock(Double.valueOf(getStock()));
			}

			farmerService.editDistributionBalance(distBal);
			getJsonObject().put("msg", getText("msg.updated"));
			getJsonObject().put("title", getText("title.success"));
			sendAjaxResponse(getJsonObject());
		}
	}

	public ICatalogueService getCatalogueService() {

		return catalogueService;
	}

	public void setCatalogueService(ICatalogueService catalogueService) {

		this.catalogueService = catalogueService;
	}

	public String getFarmerIdId() {
		return farmerIdId;
	}

	public void setFarmerIdId(String farmerIdId) {
		this.farmerIdId = farmerIdId;
	}

	public DistributionBalance getDistributionBalance() {
		return distributionBalance;
	}

	public void setDistributionBalance(DistributionBalance distributionBalance) {
		this.distributionBalance = distributionBalance;
	}

	public IProductService getProductService() {
		return productService;
	}

	public void setProductService(IProductService productService) {
		this.productService = productService;
	}

	public Map<String, String> getProductList() {

		Map<String, String> prodMap = new HashMap<String, String>();

		List<Object[]> cat = productService.listOfProducts();
		for (Object[] obj : cat) {
			prodMap.put(String.valueOf(obj[1]), String.valueOf(obj[2]));
		}
		return prodMap;
	}

	public Map<String, String> getFarmerList() {

		Map<String, String> returnMap = new HashMap<String, String>();

		List<Object[]> farmerList = new ArrayList<Object[]>();
		if(!StringUtil.isEmpty(selectedCity)){
		farmerList = farmerService.listFarmerIDAndName();
		if (!ObjectUtil.isListEmpty(farmerList)) {
			for (Object[] farmerObj : farmerList) {
				returnMap.put(String.valueOf(farmerObj[1]),
						String.valueOf(farmerObj[2]) + " " + String.valueOf(farmerObj[3]));
			}
		}
		}
		return returnMap;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}
	
	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}

	
	public ILocationService getLocationService() {
		return locationService;
	}

	public void setLocationService(ILocationService locationService) {
		this.locationService = locationService;
	}

	public String getSelectedState() {
		return selectedState;
	}

	public void setSelectedState(String selectedState) {
		this.selectedState = selectedState;
	}
	
	public Map<String, String> getStatesList() {
		Map<String,String> statemap = new HashMap<>();
		List<Object[]> statelist = locationService.listStates();
		for (Object[] obj : statelist) {
			statemap.put(String.valueOf(obj[2]), String.valueOf(obj[0])+"-"+String.valueOf(obj[1]));
		}
		return statemap;
	}
	
	public void populateLocality() throws Exception {

		if (!selectedState.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedState))) {
			localities = locationService.listLocalitiesByStateID(Long.valueOf(selectedState.trim()));
		}
		JSONArray localtiesArr = new JSONArray();
		if (!ObjectUtil.isEmpty(localities)) {
			for (Locality locality : localities) {
				localtiesArr.add(getJSONObject(locality.getCode(), locality.getCode() + " - " + locality.getName()));
			}
		}
		sendAjaxResponse(localtiesArr);
	}
	
	
	public Map<String, String> getListLocalities() {
		Map<String, String> districtMap = new LinkedHashMap<String, String>();
	
		if (!StringUtil.isEmpty(selectedState)) {
		localities = locationService.listOfLocalities();
		
		if (!ObjectUtil.isEmpty(localities)) {
			for (Locality locality : localities) {
				districtMap.put(locality.getCode(), locality.getCode() + "-" + locality.getName());
			}
		}
		}
		return districtMap;
		
	}
	
	public void populateCity() throws Exception {

		if ((!StringUtil.isEmpty(selectedDistrict) && !selectedDistrict.equalsIgnoreCase("null"))) {
			cities = locationService.listMunicipalitiesByCode(selectedDistrict);
		}
		JSONArray cityArray = new JSONArray();
		if (!ObjectUtil.isEmpty(cities)) {
			for (Municipality municipality : cities) {
				cityArray.add(
						getJSONObject(municipality.getId(), municipality.getCode() + " - " + municipality.getName()));
			}
		}
		sendAjaxResponse(cityArray);
	}

	public String getSelectedDistrict() {
		return selectedDistrict;
	}

	public void setSelectedDistrict(String selectedDistrict) {
		this.selectedDistrict = selectedDistrict;
	}

	public Map<Long, String> getCities() {

		Map<Long, String> city = new LinkedHashMap<Long, String>();
		if (!StringUtil.isEmpty(selectedDistrict)) {
			cities = locationService.listMunicipalitiesByCode(selectedDistrict);
			for (Municipality muncipality : cities) {
				city.put(muncipality.getId(), muncipality.getCode() + " - " + muncipality.getName());
			}
		}
		return city;
	
	}

	public void setCities(List<Municipality> cities) {
		this.cities = cities;
	}
	
	public void populateFarmer() throws Exception { 
		
		if ((!StringUtil.isEmpty(selectedCity) && !selectedCity.equalsIgnoreCase("null"))) {
			farmerLists = farmerService.listFarmerByCityId(Long.valueOf(selectedCity));
		}
		JSONArray cityArray = new JSONArray();
		if (!ObjectUtil.isEmpty(farmerLists)) {
			for (Farmer fm : farmerLists) {
				cityArray.add(
						getJSONObject(fm.getId(), fm.getFirstName()));
			}
		}
		sendAjaxResponse(cityArray);
	}

	public String getSelectedCity() {
		return selectedCity;
	}

	public void setSelectedCity(String selectedCity) {
		this.selectedCity = selectedCity;
	}

	public String getFarmer() {
		return farmer;
	}

	public void setFarmer(String farmer) {
		this.farmer = farmer;
	}

	
}
