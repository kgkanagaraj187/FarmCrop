/*
 * SamithiAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view.general;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.FarmCatalogueMaster;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.entity.MasterData.masterTypes;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IExporter;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.BankInformation;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

// TODO: Auto-generated Javadoc
/**
 * The Class CooperativeAction.
 */
public class SamithiFarmerDetailsAction extends BaseReportAction implements IExporter{

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(SamithiFarmerDetailsAction.class);

	

	private String id;
	
	@Autowired
	private ILocationService locationService;
	@Autowired
	private IFarmerService farmerService;

	
	private String samithiId;
	
	private String coOperativeId;
	private Farmer filter;
	@SuppressWarnings("unchecked")
	public String data() throws Exception {

	

	 filter = new Farmer();
		if (!StringUtil.isEmpty(getSamithiId())) {
			request.getSession().setAttribute("samithiId", getSamithiId());
		} else if (!StringUtil.isEmpty(request.getSession().getAttribute("samithiId"))) {
			request.getSession().setAttribute("samithiId", request.getSession().getAttribute("samithiId"));
			setSamithiId(String.valueOf(request.getSession().getAttribute("samithiId")));
		}
		if (!StringUtil.isEmpty(samithiId)) {			
			
			Warehouse warehouse = locationService.findSamithiById(Long.valueOf(samithiId));
			filter.setSamithi(warehouse);
			
		}
		
		
		super.filter = this.filter;
		Map data = readData();
		return sendJSONResponse(data);
	}

	
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		Farmer farmer = (Farmer) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		rows.add(!ObjectUtil.isEmpty(farmer) && !StringUtil.isEmpty(farmer.getFarmersCodeTracenet())? farmer.getFarmersCodeTracenet():"");
		rows.add(!ObjectUtil.isEmpty(farmer) && !StringUtil.isEmpty(farmer.getLastName())? farmer.getFirstName()+" "+farmer.getLastName():farmer.getFirstName());
		rows.add(!ObjectUtil.isEmpty(farmer) && !ObjectUtil.isEmpty(farmer.getVillage()) && !ObjectUtil.isEmpty(farmer.getVillage().getCity())
				? farmer.getVillage().getCity().getName():"");
		rows.add(!ObjectUtil.isEmpty(farmer) && !ObjectUtil.isEmpty(farmer.getVillage()) ? farmer.getVillage().getName():"");
		rows.add(!ObjectUtil.isEmpty(farmer) && !ObjectUtil.isListEmpty(farmer.getFarms()) ? farmer.getFarms().iterator().next().getFarmName():null);
		
		
		
		if(!ObjectUtil.isEmpty(farmer) && !ObjectUtil.isListEmpty(farmer.getFarms()) 
				&& !ObjectUtil.isListEmpty(farmer.getFarms().iterator().next().getFarmICSConversion()) ){
			FarmCatalogue catalogue = getCatlogueValueByCode(farmer.getFarms().iterator().next().getFarmICSConversion().iterator().next().getInsType());
			rows.add(!ObjectUtil.isEmpty(catalogue) ? catalogue.getName() : "");
		}else{
			rows.add("");
		}
		if(!ObjectUtil.isEmpty(farmer) && !ObjectUtil.isListEmpty(farmer.getFarms()) 
				&& !ObjectUtil.isListEmpty(farmer.getFarms().iterator().next().getFarmICSConversion()) ){
			if (farmer.getFarms().iterator().next().getFarmICSConversion().iterator().next().getIcsType().equalsIgnoreCase("0")) {
				rows.add(getLocaleProperty("farm.ics1"));
			} else if (farmer.getFarms().iterator().next().getFarmICSConversion().iterator().next().getIcsType().equalsIgnoreCase("1")) {
				rows.add(getLocaleProperty("farm.ics2"));
			} else if (farmer.getFarms().iterator().next().getFarmICSConversion().iterator().next().getIcsType().equalsIgnoreCase("2")) {
				rows.add(getLocaleProperty("farm.ics3"));
			} else if (farmer.getFarms().iterator().next().getFarmICSConversion().iterator().next().getIcsType().equalsIgnoreCase("3")) {
				rows.add(getLocaleProperty("farm.organic"));
			}

		}else{
			rows.add("");
		}
		
		jsonObject.put("id", farmer.getId());
		jsonObject.put("cell", rows);

		return jsonObject;

	}

	

	public String create() throws Exception {
		return LIST;
		
	}

	/**
	 * Update.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String update() throws Exception {
		return LIST;
		
	}

	private String bankInformationToJson(Set<BankInformation> bankInformation) {
		return null;
		}

	/* *//**
			 * Detail.
			 * 
			 * @return the string
			 * @throws Exception
			 *             the exception
			 */
	public String detail() throws Exception {
		return LIST;
		
	}

	/**
	 * Delete.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String delete() throws Exception {
		return LIST;

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
	 * Gets the location service.
	 * 
	 * @return the location service
	 */
	public ILocationService getLocationService() {

		return locationService;
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

	

	public String getSamithiId() {
		return samithiId;
	}

	public void setSamithiId(String samithiId) {
		this.samithiId = samithiId;
	}

	
	public ICatalogueService getCatalogueService() {
		return catalogueService;
	}

	public void setCatalogueService(ICatalogueService catalogueService) {
		this.catalogueService = catalogueService;
	}

	
	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}
	
	

	public String getCoOperativeId() {
		return coOperativeId;
	}

	public void setCoOperativeId(String coOperativeId) {
		this.coOperativeId = coOperativeId;
	}
	
	@Override
	public InputStream getExportDataStream(String exportType) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}


	public Farmer getFilter() {
		return filter;
	}


	public void setFilter(Farmer filter) {
		this.filter = filter;
	}


	

}
