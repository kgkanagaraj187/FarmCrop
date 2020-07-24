/*
 * BaseReportAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opensymphony.xwork2.ActionContext;
import com.sourcetrace.eses.BreadCrumb;
import com.sourcetrace.eses.dao.IReportDAO;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.DynamicFeildMenuConfig;
import com.sourcetrace.eses.entity.DynamicFieldConfig;
import com.sourcetrace.eses.entity.DynamicSectionConfig;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfigDetail;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IReportService;
import com.sourcetrace.eses.service.IUserService;
import com.sourcetrace.eses.umgmt.entity.Role;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.DynamicImageData;

/**
 * The Class BaseReportAction.
 * 
 * @author $Author:$
 * @version $Rev:$ $Date:$
 */
public class BaseReportAction extends SwitchAction implements IBaseReportAction {

	private static final Logger logger = Logger.getLogger(BaseReportAction.class);
	private static final long serialVersionUID = 3525047939270233437L;
	protected static final String FILTERDATE = "MM/dd/yyyy";
	protected static final String GRID_RDATE = "MM/dd/yyyy HH:mm:ss";
	protected static final String GRID_TIME_FORMAT = "HH:mm:ss";
	protected static final String TXN_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	protected static final int DATEARRAYSIZE = 2;
	@Autowired
	protected IUserService userService;
	@Autowired
	protected IReportService reportService;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected ServletContext context;
	protected String startDate;
	protected String endDate;
	protected Date sDate = null;
	protected Date eDate = null;
	protected int page;
	protected int rows;
	protected String sidx;
	protected String sord;
	protected Object filter;

	private String name;
	private InputStream inputStream;
	private String description;
	private long size;
	protected String id;
	protected int totalRecords;
	protected DateFormat csvFileDateFormat = new SimpleDateFormat("yyyy MM dd");
	protected DateFormat csvFileTimeFormat = new SimpleDateFormat("HH:mm:ss");
	private boolean mailExport;
	private Properties errors;
	private String branchIdParma;
	protected String subBranchIdParma;
	private String filterSize;
	@Autowired
	private IPreferencesService preferncesService;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private IFarmerService farmerService;

	private Role role;
	private String txnTypez;

	/**
	 * @see org.apache.struts2.interceptor.ServletResponseAware#setServletResponse(javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void setServletResponse(HttpServletResponse res) {

		response = res;
		response.setCharacterEncoding("UTF-8");
	}

	/**
	 * @see org.apache.struts2.interceptor.ServletRequestAware#setServletRequest(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void setServletRequest(HttpServletRequest req) {

		this.request = req;
	}

	/**
	 * @see org.apache.struts2.util.ServletContextAware#setServletContext(javax.servlet.ServletContext)
	 */
	@Override
	public void setServletContext(ServletContext context) {

		this.context = context;
	}

	/**
	 * Sets the user service.
	 * 
	 * @param userService
	 *            the new user service
	 */
	public void setUserService(IUserService userService) {

		this.userService = userService;
	}

	/**
	 * Sets the report service.
	 * 
	 * @param reportService
	 *            the new report service
	 */
	public void setReportService(IReportService reportService) {

		this.reportService = reportService;
	}

	/**
	 * Round two decimals.
	 * 
	 * @param d
	 *            the d
	 * @return the double
	 */
	protected double roundTwoDecimals(double d) {

		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(d));
	}

	/**
	 * Gets the start date.
	 * 
	 * @return the start date
	 */
	public String getStartDate() {

		return startDate;
	}

	/**
	 * Sets the start date.
	 * 
	 * @param startDate
	 *            the new start date
	 */
	public void setStartDate(String startDate) {

		this.startDate = startDate;
	}

	/**
	 * Gets the end date.
	 * 
	 * @return the end date
	 */
	public String getEndDate() {

		return endDate;
	}

	/**
	 * Sets the end date.
	 * 
	 * @param endDate
	 *            the new end date
	 */
	public void setEndDate(String endDate) {

		this.endDate = endDate;
	}

	/**
	 * Gets the page.
	 * 
	 * @return the page
	 */
	public int getPage() {

		return page;
	}

	/**
	 * Sets the page.
	 * 
	 * @param page
	 *            the new page
	 */
	public void setPage(int page) {

		this.page = page;
	}

	/**
	 * Gets the limit.
	 * 
	 * @return the limit
	 */
	public int getLimit() {

		return rows;
	}

	/**
	 * Sets the rows.
	 * 
	 * @param rows
	 *            the new rows
	 */
	public void setRows(int rows) {

		this.rows = rows;
	}

	/**
	 * Gets the sidx.
	 * 
	 * @return the sidx
	 */
	public String getSidx() {

		return sidx;
	}

	/**
	 * Sets the sidx.
	 * 
	 * @param sidx
	 *            the new sidx
	 */
	public void setSidx(String sidx) {

		this.sidx = sidx;
	}

	/**
	 * Gets the sord.
	 * 
	 * @return the sord
	 */
	public String getSord() {

		return sord;
	}

	/**
	 * Sets the sord.
	 * 
	 * @param sord
	 *            the new sord
	 */
	public void setSord(String sord) {

		this.sord = sord;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {

		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {

		this.name = name;
	}

	/**
	 * Gets the input stream.
	 * 
	 * @return the input stream
	 */
	public InputStream getInputStream() {

		return inputStream;
	}

	/**
	 * Sets the input stream.
	 * 
	 * @param inputStream
	 *            the new input stream
	 */
	public void setInputStream(InputStream inputStream) {

		this.inputStream = inputStream;
	}

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {

		return description;
	}

	/**
	 * Sets the description.
	 * 
	 * @param description
	 *            the new description
	 */
	public void setDescription(String description) {

		this.description = description;
	}

	/**
	 * Gets the size.
	 * 
	 * @return the size
	 */
	public long getSize() {

		return size;
	}

	/**
	 * Sets the size.
	 * 
	 * @param size
	 *            the new size
	 */
	public void setSize(long size) {

		this.size = size;
	}

	/**
	 * Gets the start index.
	 * 
	 * @return the start index
	 */
	public int getStartIndex() {

		return (getPage() * getLimit()) - getLimit();
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
	 * Gets the s date.
	 * 
	 * @return the s date
	 */
	public Date getsDate() {

		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		if (!StringUtil.isEmpty(startDate)) {
			try {
				sDate = df.parse(startDate);
			} catch (ParseException e) {
				logger.error("Error parsing start date" + e.getMessage());
			}
		}
		return sDate;
	}

	/**
	 * Sets the s date.
	 * 
	 * @param sDate
	 *            the new s date
	 */
	public void setsDate(Date sDate) {

		this.sDate = sDate;
	}

	/**
	 * Gets the e date.
	 * 
	 * @return the e date
	 */
	public Date geteDate() {

		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		if (!StringUtil.isEmpty(endDate)) {
			try {
				eDate = df.parse(endDate);
				eDate.setTime(eDate.getTime() + 86399000);
			} catch (ParseException e) {
				logger.error("Error parsing end date" + e.getMessage());
			}

		}
		return eDate;
	}

	/**
	 * Sets the e date.
	 * 
	 * @param eDate
	 *            the new e date
	 */
	public void seteDate(Date eDate) {

		this.eDate = eDate;
	}

	/**
	 * List.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String list() throws Exception {

		request.setAttribute(HEADING, getText(LIST));
		return LIST;
	}

	/**
	 * Localize values.
	 * 
	 * @param values
	 *            the values
	 */
	// protected void localizeValues(Collection<? extends ILocalizable> values)
	// {
	//
	// for (ILocalizable value : values) {
	// if (value.getAbbreviation() == null) {
	// String name = value.getName();
	//
	// value.setName(getText(name));
	// value.setAbbreviation(getText(name + ".abbr"));
	// }
	// }
	// }

	/**
	 * Builds the csv header.
	 * 
	 * @param arr
	 *            the arr
	 * @param buf
	 *            the buf
	 */
	public void buildCSVHeader(String[] arr, StringBuffer buf) {

		for (String s : arr) {
			if (s.contains(".")) {
				s = s.substring(s.lastIndexOf(".") + 1, s.length());
				buf.append(getText(s) + CSV_SEPERATOR);
				continue;
			}
			if (s.equals("lastModifiedTime")) {
				buf.append(getText("date") + CSV_SEPERATOR);
				buf.append(getText("time") + CSV_SEPERATOR);
				continue;
			}
			buf.append(getText(s) + CSV_SEPERATOR);
		}
		buf.append("\n");

	}

	/**
	 * Send json response.
	 * 
	 * @param data
	 *            the data
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected String sendJSONResponse(Map data) throws Exception {

		JSONObject gridData = new JSONObject();
		gridData.put(PAGE, getPage());
		totalRecords = (Integer) data.get(RECORDS);
		gridData.put(TOTAL, java.lang.Math.ceil(totalRecords / Double.valueOf(Integer.toString(getLimit()))));
		gridData.put(RECORDS, totalRecords);
		List list = (List) data.get(ROWS);
		JSONArray rows = new JSONArray();
		if (list != null) {
			branchIdValue = getBranchId();
			if (StringUtil.isEmpty(branchIdValue)) {
				buildBranchMap();
			}
			for (Object record : list) {
				rows.add(toJSON(record));
			}
		}
		if (totalRecords > 0) {
			gridData.put("userdata", userDataToJSON());
		} else {
			gridData.put("userdata", userDataToJSON());
		}
		gridData.put(ROWS, rows);
		//
		PrintWriter out = response.getWriter();
		out.println(gridData.toString());

		return null;
	}


	/**
	 * User data to json.
	 * 
	 * @return the jSON object
	 */
	public JSONObject userDataToJSON() {

		return null;
	}

	/**
	 * Gets the user data.
	 * 
	 * @return the user data
	 */
	protected Object getUserData() {

		return null;
	}

	/**
	 * To json.
	 * 
	 * @param record
	 *            the record
	 * @return the jSON object
	 */
	protected JSONObject toJSON(Object record) {

		return null;
	}

	/**
	 * Export csv.
	 * 
	 * @param size
	 *            the size
	 * @param fileName
	 *            the file name
	 * @param byteArray
	 *            the byte array
	 */
	public void exportCSV(long size, String fileName, byte[] byteArray) {

		DateFormat dateFormat = new SimpleDateFormat(CSV_FILE_DATE_FORMAT);
		setDescription("application/csv");
		setSize(size);
		setName(fileName + dateFormat.format(new Date()) + ".csv");
		setInputStream(new ByteArrayInputStream(byteArray));
	}

	/**
	 * Gets the projection properties.
	 * 
	 * @param token
	 *            the token
	 * @return the projection properties
	 */
	@SuppressWarnings("rawtypes")
	public Map getProjectionProperties(String token) {

		Map<String, String> projectionProperties = new HashMap<String, String>();
		projectionProperties.put(IReportDAO.PROJ_GROUP, getLocaleProperty(token + IReportDAO.PROJ_GROUP));
		projectionProperties.put(IReportDAO.PROJ_SUM, getLocaleProperty(token + IReportDAO.PROJ_SUM));
		projectionProperties.put(IReportDAO.PROJ_OTHERS, getLocaleProperty(token + IReportDAO.PROJ_OTHERS));
		return projectionProperties;
	}

	/**
	 * Gets the eSE date format.
	 * 
	 * @return the eSE date format
	 */
	public String getESEDateFormat() {

		return StringUtil.getString(DateUtil.WEB_DATE_FORMAT, DateUtil.DATE_FORMAT);
	}

	/**
	 * Gets the eSE date time format.
	 * 
	 * @return the eSE date time format
	 */
	public String getESEDateTimeFormat() {

		return StringUtil.getString(DateUtil.WEB_DATE_TIME_FORMAT, DateUtil.DATE_TIME_FORMAT);
	}

	/**
	 * Sets the ese date format.
	 */
	public void setESEDateFormat() {

		DateUtil.WEB_DATE_FORMAT = StringUtil.getString(userService.getESEDateFormat(), DateUtil.DATE_FORMAT);
	}

	/**
	 * Sets the ese date time format.
	 */
	public void setESEDateTimeFormat() {

		DateUtil.WEB_DATE_TIME_FORMAT = StringUtil.getString(userService.getESEDateTimeFormat(),
				DateUtil.DATE_TIME_FORMAT);

	}

	/**
	 * Read data.
	 * 
	 * @return the map
	 */
	@SuppressWarnings("rawtypes")
	public Map readData() {

		Map data = reportService.listWithMultipleFiltering(getSord(), getSidx(), getStartIndex(), getLimit(),
				getsDate(), geteDate(), filter, getPage(), null);
		if(!StringUtil.isEmpty(postdata)){
			JsonParser jsonParser = new JsonParser();
			
			JsonObject postdatObjs = jsonParser.parse(postdata).getAsJsonObject();
		  	setPage(postdatObjs.get("page").getAsInt());
		 	setResults(postdatObjs.get("rows").getAsInt());
			setSort(postdatObjs.get("sidx").getAsString());
			setStartIndex((getPage() - 1) * getResults());
			setDir(postdatObjs.get("sord").toString());
			data = reportService.listWithMultipleFiltering(getSord(), getSidx(), getStartIndex(), getLimit(),
					getsDate(), geteDate(), filter, getPage(), null);
			}
		return data;
	}

	/**
	 * Read data.
	 * 
	 * @param projectionToken
	 *            the projection token
	 * @return the map
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map readData(String projectionToken) {

		Map<String, String> projectionProperties = !StringUtil.isEmpty(projectionToken)
				? getProjectionProperties(projectionToken) : null;
		Map data = reportService.listWithMultipleFiltering(getSord(), getSidx(), getStartIndex(), getLimit(),
				getsDate(), geteDate(), filter, getPage(), projectionProperties);
		if(!StringUtil.isEmpty(postdata)){
		JsonParser jsonParser = new JsonParser();
		
		JsonObject postdatObjs = jsonParser.parse(postdata).getAsJsonObject();
	  	setPage(postdatObjs.get("page").getAsInt());
	 	setResults(postdatObjs.get("rows").getAsInt());
		setSort(postdatObjs.get("sidx").getAsString());
		setStartIndex((getPage() - 1) * getResults());
		setDir(postdatObjs.get("sord").toString());
		data = reportService.listWithMultipleFiltering(getSord(), getSidx(), getStartIndex(), getLimit(),
				getsDate(), geteDate(), filter, getPage(), projectionProperties);
		}
		return data;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map readProjectionData(Set<DynamicReportConfigDetail> dynamicReportConfigDetail) {

		Map data = reportService.listWithProjectionFiltering(getSord(), getSidx(), getStartIndex(), getLimit(),
				getsDate(), geteDate(), filter, getPage(), dynamicReportConfigDetail);
		return data;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map readProjectionDataView(Map<String, Map> map) {
		//Set<DynamicReportConfigDetail> dynamicReportConfigDetail,String entity
		String sort="ID";
		Map data = reportService.listWithProjectionFilteringView(getSord(), sort, getStartIndex(), getLimit(),
				getsDate(), geteDate(), filter, getPage(),map);
		return data;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map readTraceabilityData(Map<String,Map> map, String branch){
		Map data = reportService.listWithTraceabilityFilteringData(getSord(), sort, getStartIndex(), getLimit(),
				getsDate(), geteDate(), filter, getPage(),map,branch);
		return data;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map readProjectionDataStatic(Map<String, Map> map) {
		//Set<DynamicReportConfigDetail> dynamicReportConfigDetail,String entity
		String sort="ID";
		Map data = reportService.listWithProjectionFilteringStatic(getSord(), getSidx(), getStartIndex(), getLimit(),
				getsDate(), geteDate(), filter, getPage(),map);
		return data;
	}
	/**
	 * Gets the theme name.
	 * 
	 * @return the theme name
	 */
	public String getThemeName() {

		return StringUtil.isEmpty(request.getSession().getAttribute(ESESystem.SESSION_THEME_ATTRIBUTE_NAME))
				? getText("default.theme")
				: request.getSession().getAttribute(ESESystem.SESSION_THEME_ATTRIBUTE_NAME).toString();
	}

	/**
	 * @see com.opensymphony.xwork2.Preparable#prepare()
	 */
	@Override
	public void prepare() throws Exception {

		String actionClassName = ActionContext.getContext().getActionInvocation().getAction().getClass()
				.getSimpleName();
		String content = getLocaleProperty(actionClassName + "." + BreadCrumb.BREADCRUMB);
		if (StringUtil.isEmpty(content) || (content.equalsIgnoreCase(actionClassName + "." + BreadCrumb.BREADCRUMB))) {
			content = super.getText(BreadCrumb.BREADCRUMB, "");
		} else {

		}
		request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(content));
	}

	/**
	 * Null to empty.
	 * 
	 * @param data
	 *            the data
	 * @return the string
	 */
	public String nullToEmpty(Object data) {

		String resultData = "";
		if (data instanceof Date) {
			resultData = (data == null) ? ""
					: ((Date) data).toString().substring(0, ((Date) data).toString().length() - 2);
		} else {
			resultData = (data == null) ? "" : data.toString();
		}
		return resultData;
	}

	/**
	 * Read export data.
	 * 
	 * @param projectionToken
	 *            the projection token
	 * @return the map
	 */
	@SuppressWarnings("unchecked")
	public Map readExportData(String projectionToken) {

		Map<String, String> projectionProperties = !StringUtil.isEmpty(projectionToken)
				? getProjectionPropertiesForExportData(projectionToken) : null;
		Map data = reportService.listWithMultipleFiltering("desc", "id", getStartIndex(), getLimit(), getsDate(),
				new Date(), filter, getPage(), projectionProperties);
		return data;
	}

	/**
	 * Read export data.
	 * 
	 * @return the map
	 */
	public Map readExportData() {

		Map data = reportService.listWithProjectionFiltering("desc", "id", getStartIndex(), getLimit(), getsDate(),
				new Date(), filter, getPage(), null);
		return data;
	}

	/**
	 * Gets the projection properties for export data.
	 * 
	 * @param token
	 *            the token
	 * @return the projection properties for export data
	 */
	public Map getProjectionPropertiesForExportData(String token) {

		Map<String, String> projectionProperties = new HashMap<String, String>();
		projectionProperties.put(IReportDAO.PROJ_GROUP, getProperty(token + IReportDAO.PROJ_GROUP));
		projectionProperties.put(IReportDAO.PROJ_SUM, getProperty(token + IReportDAO.PROJ_SUM));
		projectionProperties.put(IReportDAO.PROJ_OTHERS, getProperty(token + IReportDAO.PROJ_OTHERS));
		return projectionProperties;
	}

	/**
	 * Gets the property.
	 * 
	 * @param key
	 *            the key
	 * @return the property
	 */
	public String getProperty(String key) {

		if (mailExport) {
			return getText(key);
		}
		// Scheduler
		if (ObjectUtil.isEmpty(errors)) {
			errors = new Properties();
			try {
				errors.load(getClass().getResourceAsStream(getClass().getSimpleName() + "_en.properties"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return errors.getProperty(key);
	}

	public String getPropertyValue(String indexPos, String consValue) {

		return getText(consValue + indexPos);

	}

	/**
	 * Checks if is mail export.
	 * 
	 * @return true, if is mail export
	 */
	public boolean isMailExport() {

		return mailExport;
	}

	/**
	 * Sets the mail export.
	 * 
	 * @param mailExport
	 *            the new mail export
	 */
	public void setMailExport(boolean mailExport) {

		this.mailExport = mailExport;
	}

	public void sendAjaxResponse(JSONArray jsonArray) {

		try {
			response.setContentType("text/JSON");
			PrintWriter out = response.getWriter();
			out.println(jsonArray.toString());

		} catch (IOException e) {
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

	public Map<String, String> getParentBranches() {
		return clientService.findParentBranches().stream().collect(
				Collectors.toMap(branchId -> String.valueOf(branchId[0]), branchName -> String.valueOf(branchName[1])));
	}

	public Map<String, String> getSubBranchesMap() {
		return clientService.listChildBranchIds(getBranchId()).stream().filter(branch -> !ObjectUtil.isEmpty(branch))
				.collect(Collectors.toMap(BranchMaster::getBranchId, BranchMaster::getName));
	}

	public void populateSubBranches() {
		JSONArray branchArr = new JSONArray();
		if (!StringUtil.isEmpty(branchIdParma)) {
			clientService.listChildBranchIds(branchIdParma).stream().filter(branch -> !ObjectUtil.isEmpty(branch))
					.forEach(branch -> {
						branchArr.add(getJSONObject(branch.getBranchId(), branch.getName()));
					});
		} else {

		}
		sendAjaxResponse(branchArr);
	}

	public String getGeneralDateFormat(String inputDate) {
		String generalDate = null;
		String result = null;
		try {
			ESESystem preferences = preferncesService.findPrefernceById("1");
			if (!StringUtil.isEmpty(preferences)) {
				DateFormat df = new SimpleDateFormat(DateUtil.DATABASE_DATE_TIME);
				Date d = df.parse(inputDate);
				result = DateUtil.convertDateToString(d,
						preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}


	
	/**
	 * Page.
	 * 
	 * @return the int
	 */
	public int page() {

		if (getCurrentPage() != null && !getCurrentPage().equals("")) {
			int page;
			if (request.getParameter("page") != null) {
				page = Integer.valueOf(request.getParameter("page"));
			} else {
				page = 1;
			}
			int curPage = Integer.valueOf(getCurrentPage());
			if (page > curPage) {
				return page;
			} else if (page != 1) {
				return page;
			} else {
				if (request.getParameter("qtype") != null
						&& request.getParameter("qtype").equalsIgnoreCase("changePage")) {
					return page;
				} else {
					return curPage;
				}
			}

		} else {
			if (request.getParameter("page") != null) {
				return Integer.valueOf(request.getParameter("page"));
			} else {
				return 1;
			}
		}

	}

	/**
	 * Gets the jQ grid request param.
	 * 
	 * @return the jQ grid request param
	 */
	public Map<String, String> getJQGridRequestParam() {

		setPage(page());
		setResults(Integer.valueOf(request.getParameter("rows")));
		setSort(request.getParameter("sidx"));
		setStartIndex((getPage() - 1) * getResults());
		setDir(request.getParameter("sord"));

		Map<String, String> searchRecord = new HashMap<String, String>();

		if (!StringUtil.isEmpty(request.getParameter("filters"))) {
			searchRecord = getFilters(request.getParameter("filters"));
		}
		return searchRecord;
	}

	public String getBranchIdParma() {
		return branchIdParma;
	}

	public void setBranchIdParma(String branchIdParma) {
		this.branchIdParma = branchIdParma;
	}

	public String getSubBranchIdParma() {
		return subBranchIdParma;
	}

	public void setSubBranchIdParma(String subBranchIdParma) {
		this.subBranchIdParma = subBranchIdParma;
	}

	public String getFilterSize() {
		return filterSize;
	}

	public void setFilterSize(String filterSize) {
		this.filterSize = filterSize;
	}

	public Role getRole() {
		if (ObjectUtil.isEmpty(role)) {
			if (!StringUtil.isEmpty(getUsername()))
				role = userService.findUserByUserName(getUsername()).getRole();
		}
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getIsAdminUser() {
		return getRole().getIsAdminUser();
	}

	

	public void printAjaxResponse(Object value, String contentType) {

		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			if (!StringUtil.isEmpty(contentType))
				response.setContentType(contentType);
			out = response.getWriter();
			out.print(value);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public String populateImage() {
		try {
			setId(id);
			DynamicImageData imageDetail = farmerService.findDynamicImageDataById(Long.valueOf(id));
			byte[] image = imageDetail.getImage();
			response.setContentType("multipart/form-data");
			OutputStream out = response.getOutputStream();
			out.write(image);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public String getText(String prop) {
		return getLocaleProperty(prop);
	}

	public String getTxnTypez() {
		return txnTypez;
	}

	public void setTxnTypez(String txnTypez) {
		this.txnTypez = txnTypez;
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
	
	  public Map readData(Map<String, String> filtersMap) {

	        Map data = reportService.listWithMultipleFiltering(getSord(), getSidx(), getStartIndex(),
	                getLimit(), getsDate(), geteDate(), filter, filtersMap, getPage(), null);
	        return data;
	    }
	  
		@SuppressWarnings("rawtypes")
		public Map readDataExport() {

			Map data = reportService.listWithMultipleFilteringExport(getSord(), getSidx(), getStartIndex(), getLimit(),
					getsDate(), geteDate(), filter, getPage(), null);
			return data;
		}
}
