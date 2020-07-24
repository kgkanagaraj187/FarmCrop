/*
 * CountryAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.action;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.BreadCrumb;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IReportService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.JsonUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.entity.Location;
import com.sourcetrace.eses.util.entity.LocationLevel;
import com.sourcetrace.eses.validator.LocationValidator;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

@Component
@Scope("prototype")
public class LocationAction extends SwitchValidatorAction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(LocationAction.class);

	@Autowired
	private ILocationService locationService;

	private Location location;

	private String levelCode;
	private LocationLevel level;
	private LinkedList<LocationLevel> parents = new LinkedList<LocationLevel>();
	private String parentCode;

	private Map<String, String> parentMap = new HashMap<String, String>();

	private String action;
	protected Map<Integer, String> statusList = new LinkedHashMap<Integer, String>();
	protected Map<Integer, String> statusYesNoList = new LinkedHashMap<Integer, String>();
	public static final String SAVE = "save";
	protected String statusSearchOptionValues;
	private long id;

	@Autowired
	@Qualifier("locationReportService")
	private IReportService reportService;

	@Autowired
	public LocationAction(LocationValidator locationValidator) {

		super.setValidator(locationValidator);
	}

	/**
	 * Gets the data.
	 * 
	 * @return the data
	 * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
	 */
	@Override
	public Object getData() {

		if (!StringUtil.isEmpty(levelCode) && ObjectUtil.isEmpty(level)) {
			level = locationService.findLevelByCode(levelCode);
			LocationLevel parent = level;
			if (!ObjectUtil.isEmpty(parent)) {
				LinkedList<LocationLevel> parentsRev = new LinkedList<LocationLevel>();
				while (!ObjectUtil.isEmpty(parent.getParent())) {
					parent = parent.getParent();
					parentsRev.add(parent);
				}
				parents = new LinkedList<LocationLevel>();
				for (int i = (parentsRev.size() - 1); i >= 0; i--) {
					parents.add(parentsRev.get(i));
				}
				if (!ObjectUtil.isEmpty(parent) && !StringUtil.isEmpty(parent.getCode())) {
					List topParents = locationService.listLocationsInfoByLevelCode(parent.getCode());
					parentMap = ReflectUtil.buildMap(topParents, new String[] { "0", "1" });
				}
			}
		}
		buildStatusListMap();
		return this.location;
	}

	public String create() {

		LOGGER.info("Create");
		setAction(SAVE);
		return INPUT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.action.IBaseEnrollment#save()
	 */

	public String save() {

		LOGGER.info("Save");
		if (ObjectUtil.isEmpty(location) || ObjectUtil.isEmpty(level)) {
			return REDIRECT;
		}
		if (!ObjectUtil.isEmpty(location.getParent()) && !StringUtil.isEmpty(location.getParent().getCode())) {
			Location parent = locationService.findLocationByCode(location.getParent().getCode());
			location.setParent(parent);
		}
		location.setCreatedUserName(getUsername());
		location.setLastUpdatedUserName(getUsername());
		location.setCreatedDT(new Date());
		location.setLastUpdatedDT(new Date());
		location.setRevisionNumber(DateUtil.getRevisionNumber());
		location.setLevel(level);
		locationService.addLocation(location);
		return REDIRECT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.action.IBaseEnrollment#edit()
	 */
	public String edit() {

		LOGGER.info("Edit");
		location = locationService.findLocationById(getId());
		if (ObjectUtil.isEmpty(location)) {
			return REDIRECT;
		}
		LinkedList<Location> parentsLocRev = new LinkedList<Location>();

		Location parentLoc = location;
		while (!ObjectUtil.isEmpty(parentLoc.getParent())) {
			parentLoc = parentLoc.getParent();
			parentsLocRev.add(parentLoc);
		}

		LinkedList<Location> parentsLocs = new LinkedList<Location>();
		for (int i = (parentsLocRev.size() - 1); i >= 0; i--) {
			parentsLocs.add(parentsLocRev.get(i));
		}

		if (!ObjectUtil.isListEmpty(parents)) {
			for (int i = 0; i < parents.size(); i++) {
				LocationLevel pLL = parents.get(i);
				if (!ObjectUtil.isEmpty(parentsLocs.get(i).getParent())) {
					pLL.setParentCode(parentsLocs.get(i).getParent().getCode());
				}
				pLL.setChildCode(parentsLocs.get(i).getCode());
			}
		}
		setAction(UPDATE);
		return INPUT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.action.IBaseEnrollment#update()
	 */
	public String update() {

		LOGGER.info("Update");
		Location tempLocation = locationService.findLocationById(this.location.getId());
		if (ObjectUtil.isEmpty(tempLocation) || ObjectUtil.isEmpty(level)) {
			return REDIRECT;
		}
		// Do logic for update
		if (!ObjectUtil.isEmpty(location.getParent()) && !StringUtil.isEmpty(location.getParent().getCode())) {
			Location parent = locationService.findLocationByCode(location.getParent().getCode());
			tempLocation.setParent(parent);
		}
		tempLocation.setLevel(level);
		tempLocation.setCode(this.location.getCode());
		tempLocation.setName(this.location.getName());
		tempLocation.setStatus(this.location.getStatus());
		tempLocation.setLastUpdatedUserName(getUsername());
		tempLocation.setLastUpdatedDT(new Date());
		tempLocation.setRevisionNumber(DateUtil.getRevisionNumber());
		locationService.editLocation(tempLocation);
		return REDIRECT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.action.IBaseEnrollment#detail()
	 */
	public String detail() {

		LOGGER.info("Detail");
		location = locationService.findLocationById(getId());
		if (ObjectUtil.isEmpty(location) || ObjectUtil.isEmpty(level)) {
			return REDIRECT;
		}
		LinkedList<Location> parentsLocRev = new LinkedList<Location>();

		Location parentLoc = location;
		while (!ObjectUtil.isEmpty(parentLoc.getParent())) {
			parentLoc = parentLoc.getParent();
			parentsLocRev.add(parentLoc);
		}
		LinkedList<Location> parentsLocs = new LinkedList<Location>();
		for (int i = (parentsLocRev.size() - 1); i >= 0; i--) {
			parentsLocs.add(parentsLocRev.get(i));
		}

		if (!ObjectUtil.isListEmpty(parents)) {
			for (int i = 0; i < parents.size(); i++) {
				LocationLevel pLL = parents.get(i);
				pLL.setLocationName(parentsLocs.get(i).getName());
			}
		}
		return DETAIL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.action.IBaseEnrollment#delete()
	 */
	public String delete() {

		LOGGER.info("Delete");
		location = locationService.findLocationById(getId());
		if (!ObjectUtil.isEmpty(location)) {
			List<Object[]> childs = locationService.listLocationsInfoByParentCode(location.getCode());
			if (!ObjectUtil.isListEmpty(childs)) {
				addActionError(getText("delete.warn"));
				request.setAttribute(HEADING, getText(DETAIL, new String[] { level.getName() }));
				return DETAIL;
			}
			try {
				locationService.removeLocation(location);
			} catch (Exception e) {
				addActionError(getText("delete.warn.association"));
				request.setAttribute(HEADING, getText(DETAIL, new String[] { level.getName() }));
				return DETAIL;
			}
		}
		return REDIRECT;

	}

	public String data() throws Exception {

		Map<String, String> searchRecord = getJQGridRequestParam();

		Location filter = new Location();

		if (!StringUtil.isEmpty(searchRecord.get("code"))) {
			filter.setCode(searchRecord.get("code").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("name"))) {
			filter.setName(searchRecord.get("name").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("p.name"))) {
			Location parent = new Location();
			parent.setName(searchRecord.get("p.name").trim());
			filter.setParent(parent);
		}

		if (!StringUtil.isEmpty(levelCode)) {
			filter.setLevelCode(levelCode);
		}

		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		return sendJQGridJSONResponse(data);

	}

	/**
	 * @see com.sourcetrace.esesw.view.SwitchAction#toJSON(java.lang.Object)
	 */
	public JSONObject toJSON(Object obj) {

		Location location = (Location) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">" + location.getCode() + "</font>");
		rows.add(location.getName());
		rows.add(ObjectUtil.isEmpty(location.getParent()) ? "" : location.getParent().getName());
		jsonObject.put("id", location.getId());
		jsonObject.put("cell", rows);
		return jsonObject;
	}

	public void populateChilds() {

		Map<String, String> map = new LinkedHashMap<String, String>();
		map = getLocationsMap(parentCode);
		sendAjaxResponse(JsonUtil.maptoJSONArrayMap(map));
	}

	/**
	 * Gets the status list default value.
	 * 
	 * @return the status list default value
	 */
	public Integer getStatusListDefaultValue() {

		// return (ObjectUtil.isEmpty(this.location) ? EntityInfo.ACTIVE :
		// this.location.getIsActive());
		return (ObjectUtil.isEmpty(this.location) ? 1 : 1);
	}

	public Map<String, String> getLocationsMap(String code) {

		Map<String, String> map = new LinkedHashMap<String, String>();
		if (!StringUtil.isEmpty(code)) {
			List childs = locationService.listLocationsInfoByParentCode(code);
			map = ReflectUtil.buildMap(childs, new String[] { "0", "1" });
		}
		return map;
	}

	protected void buildStatusListMap() {

		statusList.put(1, getText("active"));
		statusList.put(0, getText("inActive"));

		buildStatusSearchOptionValues();
	}

	/**
	 * Build status search option values.
	 */
	private void buildStatusSearchOptionValues() {

		StringBuffer sb = new StringBuffer();
		sb.append(":").append(getText("search.all"));
		for (Map.Entry<Integer, String> entry : statusList.entrySet()) {
			sb.append(";").append(entry.getKey()).append(":").append(entry.getValue());
		}
		statusSearchOptionValues = sb.toString();
	}

	@Override
	public String getCurrentMenu() {

		return getText("menu.select", new String[] { levelCode });
	}

	public void prepare() throws Exception {

		String actionClassName = this.getClass().getSimpleName();
		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
		levelCode = request.getParameter("levelCode");
		getData();
		String content = getLocaleProperty(actionClassName + "." + BreadCrumb.BREADCRUMB + "." + levelCode);
		if (StringUtil.isEmpty(content) && !ObjectUtil.isEmpty(level) || (content.equalsIgnoreCase(actionClassName + "." + BreadCrumb.BREADCRUMB+ "." + levelCode))) {
			content = super.getText(BreadCrumb.BREADCRUMB, new String[] { level.getCode(), level.getName() });
		}
		request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(content));
	}

	public Location getLocation() {

		return location;
	}

	public void setLocation(Location location) {

		this.location = location;
	}

	public String getLevelCode() {

		return levelCode;
	}

	public void setLevelCode(String levelCode) {

		this.levelCode = levelCode;
	}

	public LocationLevel getLevel() {

		return level;
	}

	public void setLevel(LocationLevel level) {

		this.level = level;
	}

	public Map<String, String> getParentMap() {

		return parentMap;
	}

	public void setParentMap(Map<String, String> parentMap) {

		this.parentMap = parentMap;
	}

	public LinkedList<LocationLevel> getParents() {

		return parents;
	}

	public void setParents(LinkedList<LocationLevel> parents) {

		this.parents = parents;
	}

	public String getParentCode() {

		return parentCode;
	}

	public void setParentCode(String parentCode) {

		this.parentCode = parentCode;
	}

	public String getAction() {

		return action;
	}

	public void setAction(String action) {

		this.action = action;
	}

	public Map<Integer, String> getStatusList() {

		return statusList;
	}

	public void setStatusList(Map<Integer, String> statusList) {

		this.statusList = statusList;
	}

	public Map<Integer, String> getStatusYesNoList() {

		return statusYesNoList;
	}

	public void setStatusYesNoList(Map<Integer, String> statusYesNoList) {

		this.statusYesNoList = statusYesNoList;
	}

	public String getStatusSearchOptionValues() {

		return statusSearchOptionValues;
	}

	public void setStatusSearchOptionValues(String statusSearchOptionValues) {

		this.statusSearchOptionValues = statusSearchOptionValues;
	}

	public long getId() {

		return id;
	}

	public void setId(long id) {

		this.id = id;
	}

}
