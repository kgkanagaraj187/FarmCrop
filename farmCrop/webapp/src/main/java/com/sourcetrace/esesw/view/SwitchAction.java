/*
 * SwitchAction.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.util.ServletContextAware;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.DynamicData;
import com.ese.entity.ILocalizable;
import com.ese.entity.util.ESESystem;
import com.ese.util.Base64Util;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.sourcetrace.eses.BreadCrumb;
import com.sourcetrace.eses.entity.DynamicFeildMenuConfig;
import com.sourcetrace.eses.entity.DynamicFieldConfig;
import com.sourcetrace.eses.entity.DynamicFieldConfig.LIST_METHOD;
import com.sourcetrace.eses.entity.DynamicFieldScoreMap;
import com.sourcetrace.eses.entity.DynamicMenuFieldMap;
import com.sourcetrace.eses.entity.DynamicSectionConfig;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.inspect.agrocert.LanguagePreferences;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfig;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.ICertificationService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.service.IReportService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.service.IUserService;
import com.sourcetrace.eses.umgmt.entity.Menu;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Country;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.DynamicConstants;
import com.sourcetrace.esesw.entity.profile.DynamicImageData;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicData;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicFieldsValue;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.Vendor;
import com.sourcetrace.translators.MicrosoftTranslatorAPI;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

// TODO: Auto-generated Javadoc
/**
 * The Class SwitchAction.
 */
@SuppressWarnings("serial")
public class SwitchAction extends ActionSupport
		implements ISwitchAction, ServletRequestAware, ServletResponseAware, ServletContextAware, Preparable {

	protected static final String CREATE = "create";
	protected static final String DETAIL = "detail";
	protected static final String UPDATE = "update";
	protected static final String MAPPING = "mapping";
	protected static final String OTHERS = "99";
	protected static final String DELETE = "delete";
	protected static final String LIST = "list";
	protected static final String TITLE_PREFIX = "title.";
	protected static final String HEADING = "heading";
	protected static final String EMPTY = "";
	protected static final String ROWS = "rows";
	protected static final String RECORDS = "records";
	public static final String RECORD_COUNT = "records";
	public static final String PAGE_NUMBER = "pagenumber";
	protected static final String TOTAL = "total";
	public static final String PAGE = "page";
	protected static final String SWITCH = "switch";
	public static final String NO_RECORD = "No_Records_Present";
	protected static final String BY_REGION = "byProdReg";
	protected static final String BY_BUSINESSPOINT = "byProdBPoint";
	protected static final String BY_DAILY = "byProdDaily";
	protected static final String BY_MONTH = "byProdMonth";
	protected static final String CHECK_MONTH = "0";
	protected static final String CHECK_TRIMESTER = "1";
	protected static final String CHECK_SEMESTER = "2";
	protected static final String BY_TRIMESTER = "byProdTrimester";
	protected static final String BY_SEMESTER = "byProdSemester";
	protected static final String BY_LOCATION = "byProdLocation";
	protected static final String BY_BRANCH = "byProdBranch";
	protected static final String EXPORT_BY_REGION = "export_byProdReg";
	protected static final String EXPORT_BY_BUSINESSPOINT = "export_byProdBPoint";
	protected static final String EXPORT_BY_DAILY = "export_byProdDaily";
	protected static final String EXPORT_BY_MONTH = "export_byProdMonth";
	protected static final String EXPORT_BY_TRIMESTER = "export_byProdTrimester";
	protected static final String EXPORT_BY_SEMESTER = "export_byProdSemester";
	protected static final String EXPORT_BY_LOCATION = "export_byProdLocation";
	protected static final String EXPORT_BY_BRANCH = "export_byProdBranch";
	protected static final String REDIRECT = "redirect";
	public static final String NEW_LINE = "\n";
	public static final String DELIMITER = ",";
	public static final String EXPRESSION_TIME = "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";
	public static String DATE_FORMAT;
	public static String DATE_TIME_FORMAT;
	public static final String GRID_DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";
	public static final String GRID_DATE = "MM/dd/yyyy";
	public static final String GRID_TIME = "HH:mm:ss";
	public static final String CONTENT_TYPE_TEXT_HTML = "text/html";
	public static final String REDIRECT_TO_FARMER = "redirectToFarmer";
	public static final String DEFAULT_LANG = "en";
	protected static final String ACTPLAN = "updateActPlan";
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected ServletContext context;
	@Autowired
	protected IUserService userService;
	protected IReportService reportService;
	private List<FarmCatalogue> farmCatalogueList;
	@Autowired
	protected IClientService clientService;
	@Autowired
	protected IProductService productService;
	@Autowired
	protected ICatalogueService catalogueService;
	public int startIndex;
	public int results;
	public int page;
	public String sort;
	public String dir;
	private String queryType;
	private String queryName;
	public final static String EXPORT_CSV = "csv";
	public final static String EXPORT_PDF = "pdf";
	private String name;
	protected List<Object[]> filterObjs;
	private InputStream inputStream;
	private String description;
	private long size;
	private DateFormat eseExportFileFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private String currentPage;
	protected int totalRecords;
	private String branch;
	protected final String FILTER_ALL = getText("filter.allStatus");
	private String imgFileId;

	protected Map<String, String> branchesMap = new LinkedHashMap<String, String>();
	protected Map<String, String> parentBranchMap = new LinkedHashMap<String, String>();
	protected String branchIdValue = "";
	protected String pdfFileName = "";
	@Autowired
	private IPreferencesService preferncesService;
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private ICertificationService certificationService;
	@Autowired
	private IProductDistributionService productDistributionService;

	private String isParentBranch;
	private String parentBranch;
	private static JSONObject jsonObject = null;
	private Map<String, String> localeMap = new LinkedHashMap<String, String>();
	private Properties errors;
	private boolean mailExport;
	protected Map<String, Map<String, String>> questMap = new LinkedHashMap<>();
	protected Map<String, Object[]> menuMap = new LinkedHashMap<>();

	private String selectedObject;
	private String txnTypez;
	private String formulaEquation;
	protected String postdata;
	protected String postdataReport;
	protected String filterMapReport;
	/***/
	protected String dynamicFieldsArray;
	private String dynamicListArray;
	String photoIds = new String();
	private Map<String, HarvestSeason> seasonMap = new LinkedHashMap<>();
	private String version;
	protected LinkedHashMap<String, DynamicFieldConfig> fieldConfigMap = new LinkedHashMap<>();
	@Autowired
	private IUniqueIDGenerator idGenerator;
	private String parentId;
	
	protected String MSA_TRANSLATOR_TEXT_SUBSCRIPTION_KEY;
	protected String MSA_TRANSLATOR_TEXT_ENDPOINT;
	public static final String MS_TRANSLATOR_TEXT_SUBSCRIPTION_KEY_PREF_VAL = "MSA_TRANSLATOR_TEXT_SUBSCRIPTION_KEY"; 
	public static final String MS_TRANSLATOR_TEXT_ENDPOINT_PREF_VAL = "MSA_TRANSLATOR_TEXT_ENDPOINT";
		
	/**
	 * Sets the servlet response.
	 * 
	 * @param res
	 *            the res
	 * @see org.apache.struts2.interceptor.ServletResponseAware#setServletResponse(javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void setServletResponse(HttpServletResponse res) {

		response = res;
		response.setCharacterEncoding("UTF-8");
	}

	/**
	 * Sets the servlet request.
	 * 
	 * @param req
	 *            the req
	 * @see org.apache.struts2.interceptor.ServletRequestAware#setServletRequest(javax.servlet.http.HttpServletRequest)
	 */
	public void setServletRequest(HttpServletRequest req) {

		this.request = req;
	}

	/**
	 * Sets the servlet context.
	 * 
	 * @param context
	 *            the context
	 * @see org.apache.struts2.util.ServletContextAware#setServletContext(javax.servlet.ServletContext)
	 */
	@Override
	public void setServletContext(ServletContext context) {

		this.context = context;
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
	 * Sets the user service.
	 * 
	 * @param userService
	 *            the new user service
	 */
	public void setUserService(IUserService userService) {

		this.userService = userService;
	}

	/**
	 * Gets the query type.
	 * 
	 * @return the query type
	 */
	public String getQueryType() {

		return queryType;
	}

	/**
	 * Sets the query type.
	 * 
	 * @param queryType
	 *            the new query type
	 */
	public void setQueryType(String queryType) {

		this.queryType = queryType;
	}

	/**
	 * Gets the query name.
	 * 
	 * @return the query name
	 */
	public String getQueryName() {

		return queryName;
	}

	/**
	 * Sets the query name.
	 * 
	 * @param queryName
	 *            the new query name
	 */
	public void setQueryName(String queryName) {

		this.queryName = queryName;
	}

	/**
	 * Gets the sort.
	 * 
	 * @return the sort
	 */
	public String getSort() {

		return sort;
	}

	/**
	 * Sets the sort.
	 * 
	 * @param sort
	 *            the new sort
	 */
	public void setSort(String sort) {

		this.sort = sort;
	}

	/**
	 * Gets the dir.
	 * 
	 * @return the dir
	 */
	public String getDir() {

		return dir;
	}

	/**
	 * Sets the dir.
	 * 
	 * @param dir
	 *            the new dir
	 */
	public void setDir(String dir) {

		this.dir = dir;
	}

	/**
	 * Gets the start index.
	 * 
	 * @return the start index
	 */
	public int getStartIndex() {

		return startIndex;
	}

	/**
	 * Sets the start index.
	 * 
	 * @param start
	 *            the new start index
	 */
	public void setStartIndex(int start) {

		this.startIndex = start;
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
	 * Gets the results.
	 * 
	 * @return the results
	 */
	public int getResults() {

		return results;
	}

	/**
	 * Sets the results.
	 * 
	 * @param limit
	 *            the new results
	 */
	public void setResults(int limit) {

		this.results = limit;
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
	 * Gets the current page.
	 * 
	 * @return the current page
	 */
	public String getCurrentPage() {

		return currentPage;
	}

	/**
	 * Sets the current page.
	 * 
	 * @param currentPage
	 *            the new current page
	 */
	public void setCurrentPage(String currentPage) {

		this.currentPage = currentPage;
	}

	/**
	 * Gets the username.
	 * 
	 * @return the username
	 */
	public String getUsername() {

		if (ObjectUtil.isEmpty(request)) {
			request = ReflectUtil.getCurrentHttpRequest();
		}
		Object biObj = ObjectUtil.isEmpty(request) ? null : request.getSession().getAttribute("user");
		return ObjectUtil.isEmpty(biObj) ? null : biObj.toString();

	}

	/**
	 * Gets the ip address.
	 * 
	 * @return the ip address
	 */
	public String getIpAddress() {

		return request.getRemoteAddr();
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
	 * Localize values.
	 * 
	 * @param values
	 *            the values
	 */
	public void localizeValues(Collection<? extends ILocalizable> values) {

		for (ILocalizable value : values) {
			if (value.getAbbreviation() == null) {
				String name = value.getName();

				value.setName(getText(name));
				value.setAbbreviation(getText(name + ".abbr"));
			}
		}
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
	/*
	 * @SuppressWarnings({ "unchecked", "rawtypes" }) protected String
	 * sendJSONResponse(Map data) throws Exception { JSONObject gridData = new
	 * JSONObject(); gridData.put(PAGE, data.get(PAGE_NUMBER));
	 * gridData.put(TOTAL, data.get(RECORDS)); List list = (List)
	 * data.get(ROWS); JSONArray rows = new JSONArray(); if (list != null) {
	 * branchIdValue = getBranchId(); if (StringUtil.isEmpty(branchIdValue)) {
	 * buildBranchMap(); } for (Object record : list) { JSONObject row =
	 * toJSON(record); if (row != null) { rows.add(row); } } }
	 * gridData.put(ROWS, rows); PrintWriter out = response.getWriter();
	 * out.println(gridData.toString()); return null; }
	 */

	/**
	 * Search json response.
	 * 
	 * @param data
	 *            the data
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	/*
	 * @SuppressWarnings({ "unchecked", "rawtypes" }) protected String
	 * searchJSONResponse(Map data) throws Exception { JSONObject gridData = new
	 * JSONObject(); gridData.put(PAGE, data.get(PAGE_NUMBER));
	 * gridData.put(TOTAL, data.get(RECORDS)); List list = (List)
	 * data.get(ROWS); Set set = new HashSet(list); list.clear();
	 * list.addAll(set); JSONArray rows = new JSONArray(); if (list != null) {
	 * branchIdValue = getBranchId(); if (StringUtil.isEmpty(branchIdValue)) {
	 * buildBranchMap(); } for (Object record : list) { JSONObject row =
	 * toJSON(record); if (row != null) { rows.add(row); } } }
	 * gridData.put(ROWS, rows); PrintWriter out = response.getWriter();
	 * out.println(gridData.toString()); return null; }
	 */

	/**
	 * Gets the filters.
	 * 
	 * @param filters
	 *            the filters
	 * @return the filters
	 */
	public Map<String, String> getFilters(String filters) {

		Map<String, String> searchKeyValue = new HashMap<String, String>();

		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(new StringReader(filters));
			JSONArray jsonArray = ((JSONArray) jsonObj.get("rules"));
			for (int i = 0; i < jsonArray.size(); i++) {
				searchKeyValue.put(((JSONObject) jsonArray.get(i)).get("field").toString(),
						((JSONObject) jsonArray.get(i)).get("data").toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return searchKeyValue;
	}

	public Map<String, String> getFiltersFromPostData(String filters) {

		Map<String, String> searchKeyValue = new HashMap<String, String>();

		try {
			// Gson s = new Gson();
			postdata = StringEscapeUtils.unescapeJava(postdata);
			postdata = postdata.replaceAll("^\"|\"$", "");
			Gson gson = new Gson();
			JsonParser jsonParser = new JsonParser();

			JsonObject postdatObjs = jsonParser.parse(postdata).getAsJsonObject();
			setPage(postdatObjs.get("page").getAsInt());
			setResults(postdatObjs.get("rows").getAsInt());
			setSort(postdatObjs.get("sidx").getAsString());
			setStartIndex((getPage() - 1) * getResults());
			String sort = postdatObjs.get("sord").toString();
			setDir(sort.replaceAll("^\"|\"$", ""));
			if (postdatObjs.get("filters") != null) {
				JsonObject jsonObj = postdatObjs.get("filters").getAsJsonObject();

				JsonArray jsonArray = jsonObj.get("rules").getAsJsonArray();
				for (int i = 0; i < jsonArray.size(); i++) {
					searchKeyValue.put((jsonArray.get(i).getAsJsonObject()).get("field").getAsString(),
							(jsonArray.get(i).getAsJsonObject()).get("data").getAsString());
				}
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return searchKeyValue;
	}

	/**
	 * Send jq grid json response.
	 * 
	 * @param data
	 *            the data
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected String sendJQGridJSONResponse(Map data) throws Exception {

		JSONObject gridData = new JSONObject();
		gridData.put(PAGE, getPage());
		totalRecords = (Integer) data.get(RECORDS);
		gridData.put(TOTAL, java.lang.Math.ceil(totalRecords / Double.valueOf(Integer.toString(getResults()))));
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

		if (!StringUtil.isEmpty(postdata)) {
			searchRecord = getFiltersFromPostData(postdata);
		}
		return searchRecord;
	}

	/**
	 * Send response.
	 * 
	 * @param populateResponce
	 *            the populate responce
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	protected String sendResponse(Object populateResponce) throws Exception {

		printAjaxResponse(populateResponce, "");
		return null;
	}

	/**
	 * Prints the ajax response.
	 * 
	 * @param value
	 *            the value
	 * @param contentType
	 *            the content type
	 */
	protected void printAjaxResponse(Object value, String contentType) {

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

		setDescription("application/csv");
		setSize(size);
		setName(fileName + eseExportFileFormat.format(new Date()) + ".csv");
		setInputStream(new ByteArrayInputStream(byteArray));
	}

	/**
	 * Gets the upload type name.
	 * 
	 * @param value
	 *            the value
	 * @return the upload type name
	 */
	public String getUploadTypeName(int value) {

		String typeName;
		if (value == 1) {
			typeName = getText("uploadType.pos");
		} else if (value == 2) {
			typeName = getText("uploadType.datacard");
		} else {
			typeName = getText("uploadType.clientcard");
		}

		return typeName;
	}

	/**
	 * Data.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String data() throws Exception {

		setResults(Integer.valueOf(request.getParameter("rp")));
		setQueryType(request.getParameter("qtype"));
		setQueryName(request.getParameter("query"));
		setSort(request.getParameter("sortname"));
		setStartIndex((getPage() - 1) * getResults());
		setDir(request.getParameter("sortorder"));

		return null;

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
	 * List.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String list() throws Exception {

		if (getCurrentPage() != null) {
			setCurrentPage(getCurrentPage());
		}
		reLocalizeMenu();
		request.setAttribute(HEADING, getText(LIST));
		return LIST;
	}

	/**
	 * Gets the sesssion id.
	 * 
	 * @return the sesssion id
	 */
	public String getSesssionId() {

		String sessionId = (String) request.getSession().getId();
		return sessionId;
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
	 * Prepare.
	 * 
	 * @throws Exception
	 *             the exception
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
	 * Send ajax response.
	 * 
	 * @param jsonObject
	 *            the json object
	 */
	public void sendAjaxResponse(JSONObject jsonObject) {

		try {
			response.setContentType("text/json");
			PrintWriter out = response.getWriter();
			out.println(jsonObject.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send ajax response.
	 * 
	 * @param responseString
	 *            the response string
	 */
	public void sendAjaxResponse(String responseString) {

		try {
			response.setContentType("text/JSON");
			PrintWriter out = response.getWriter();
			out.println(responseString);

		} catch (IOException e) {
			e.printStackTrace();
		}
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
	public Map<Integer, String> getPropertyData(String text) {

		Map<Integer, String> propertyDataMap = null;
		String values = getLocaleProperty(text);
		propertyDataMap = new LinkedHashMap();
		if (!StringUtil.isEmpty(values)) {
			String[] valuesArray = values.split(",");
			for (String value : valuesArray) {
				String[] data = value.trim().split("\\~");
				propertyDataMap.put(Integer.parseInt(data[0].trim()), data[1]);
			}
		}
		return propertyDataMap;

	}

	public String getLocaleProperty(String key, String loggedInUserLanguage, Class clazz) {

		String result = getDBProperty(key, loggedInUserLanguage);
		if (StringUtil.isEmpty(result) || result == null) {
			Properties properties = new Properties();
			try {
				properties.load(
						clazz.getResourceAsStream(clazz.getSimpleName() + "_" + loggedInUserLanguage + ".properties"));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				try {
					clazz = SwitchAction.class;
					properties.load(clazz.getResourceAsStream("SwitchAction_" + loggedInUserLanguage + ".properties"));
				} catch (IOException es) {
					es.printStackTrace();
				}
			}
			result = properties.getProperty(key);
		}
		return result != null && !StringUtil.isEmpty(result) ? result : key;
	}

	public String getBranchId() {

		if (ObjectUtil.isEmpty(request)) {
			request = ReflectUtil.getCurrentHttpRequest();
		}
		Object biObj = ObjectUtil.isEmpty(request) ? null
				: request.getSession().getAttribute(ISecurityFilter.CURRENT_BRANCH);
		return ObjectUtil.isEmpty(biObj) ? null : biObj.toString();
	}

	public String getBranchName() {

		if (ObjectUtil.isEmpty(request)) {
			request = ReflectUtil.getCurrentHttpRequest();
		}
		Object biObj = ObjectUtil.isEmpty(request) ? null
				: request.getSession().getAttribute(ISecurityFilter.CURRENT_BRANCH_NAME);
		return ObjectUtil.isEmpty(biObj) ? null : biObj.toString();
	}

	public String getIsMultiBranch() {

		if (ObjectUtil.isEmpty(request)) {
			request = ReflectUtil.getCurrentHttpRequest();
		}
		Object biObj = ObjectUtil.isEmpty(request) ? null
				: request.getSession().getAttribute(ISecurityFilter.IS_MULTI_BRANCH_APP);
		return ObjectUtil.isEmpty(biObj) ? null : biObj.toString();
	}

	protected List<Object[]> getBranchesInfo() {

		List<Object[]> branchMasters = clientService.listBranchMastersInfo();
		return branchMasters;
	}

	public String getBranchFilterText() {

		StringBuffer sb = new StringBuffer();
		sb.append(":").append(FILTER_ALL).append(";");
		List<Object[]> branchMasters = getBranchesInfo();
		for (Object[] objects : branchMasters) {
			sb.append(objects[0].toString()).append(":").append(objects[1].toString()).append(";");
		}
		String data = sb.toString();
		return data.substring(0, data.length() - 1);
	}

	public String getParentBranchFilterText() {

		StringBuffer sb = new StringBuffer();
		sb.append(":").append(FILTER_ALL).append(";");
		List<Object[]> branchMasters = clientService.findParentBranches();

		for (Object[] objects : branchMasters) {
			sb.append(objects[0].toString()).append(":").append(objects[1].toString()).append(";");
		}

		String data = sb.toString();
		return data.substring(0, data.length() - 1);
	}

	public String getChildBranchFilterText() {

		StringBuffer sb = new StringBuffer();
		sb.append(":").append(FILTER_ALL).append(";");
		List<Object[]> branchMasters = clientService.findChildBranches();

		for (Object[] objects : branchMasters) {
			sb.append(objects[0].toString()).append(":").append(objects[1].toString()).append(";");
		}

		String data = sb.toString();
		return data.substring(0, data.length() - 1);
	}

	protected void buildBranchMap() {

		List<Object[]> branchMasters = getBranchesInfo();
		branchesMap = new LinkedHashMap<String, String>();
		//branchesMap.put(null, getMainBranchName());
		for (Object[] objects : branchMasters) {
			branchesMap.put(objects[0].toString(), objects[1].toString());
		}
	}

	protected Object[] getBranchInfo(String branchId) {

		return clientService.findBranchInfo(branchId);
	}

	protected String getLanguagePref(String lang, String code) {

		if (questMap.size() <= 0) {
			buildQuestMap();
		}

		return questMap != null && !questMap.isEmpty() && !StringUtil.isEmpty(lang)
				? questMap.get(lang) != null && !questMap.get(lang).isEmpty() ? questMap.get(lang).get(code) : "" : "";
	}

	protected void buildParentBranchMap() {

		List<Object[]> branchMasters = getParentBranchesInfo();
		parentBranchMap = new LinkedHashMap<String, String>();
		parentBranchMap.put(null, getMainBranchName());
		for (Object[] objects : branchMasters) {
			parentBranchMap.put(objects[0].toString(), objects[1].toString());
		}
	}

	private List<Object[]> getParentBranchesInfo() {
		List<Object[]> branchMasters = clientService.listParentBranchMastersInfo();
		return branchMasters;
	}

	public String getBranchName(String branchId) {

		Object[] binfo = getBranchInfo(branchId);
		return (ObjectUtil.isEmpty(binfo) || binfo.length < 2) ? getMainBranchName() : binfo[1].toString();
	}

	public Map<String, String> getBranchesMap() {

		if (branchesMap.size() == 0) {
			buildBranchMap();
		}
		return branchesMap;
	}

	public Map<String, String> getParentBranchMap() {
		if (parentBranchMap.size() == 0) {
			buildParentBranchMap();
		}
		return parentBranchMap;
	}

	public String getPdfFileName() {

		return pdfFileName;
	}

	public void setPdfFileName(String pdfFileName) {

		this.pdfFileName = pdfFileName;
	}

	public String[] objectFieldNames(String entity) { // method to get object
														// field names as string
														// array from property
														// file.

		String[] entityFieldNames = null; // String to get entity properties
											// names.
		String entityProperties = null;
		if (!StringUtil.isEmpty(getBranchId())) { // Check if branch User is
													// logged in.
			entityProperties = getText(entity + "ExportPropertiesNoBranch");
		} else {
			entityProperties = getText(entity + "ExportProperties");
		}
		entityFieldNames = entityProperties.split("\\,");

		return entityFieldNames;
	}

	public byte[] exportLogo() {

		byte[] picData = clientService.findLogoByCode(Asset.APP_LOGO);
		if (!ObjectUtil.isEmpty(picData)) {
			// picData = Base64.decode(picData);
		}
		return picData;
	}

	public String columnHeaders(String entityName) {

		String columnHeaders = null; // String to get column headers from
										// property file.

		if (!StringUtil.isEmpty(getBranchId())) { // Check if branch User is
													// logged in.
			columnHeaders = getText(entityName + "ExportHeadingNoBranch");
		} else {
			columnHeaders = getText(entityName + "ExportHeading");
		}
		return columnHeaders;
	}

	public InputStream getPDFExportDataStream(List<Object> obj, String entityName, Map<String, Map> fieldMaps)
			throws IOException, DocumentException, NoSuchFieldException, SecurityException {

		Font cellFont = null; // font for cells.
		Font titleFont = null; // font for title text.
		Paragraph title = null; // to add title for report
		String columnHeaders = null; // to hold column headers from property
										// file.
		String[] objectFieldNames = null; // to hold property (field) names of
											// Object obj, to be got from
											// property file.
		List<Object[]> entityFieldsList = new ArrayList<Object[]>(); // to hold
																		// properties
																		// of
																		// entity
																		// object
																		// passed
																		// as
																		// list.
		Object entityObject = null; // to hold the entity object type.
		Map<String, Map> tempFieldMaps = null; // to hold field Maps in
												// iteration temporarily.
		List<String> fieldNamesList = new ArrayList<String>(); // to iterate
																// properties of
																// map.
		Iterator<String> iterator; // iterator for looping.
		String iteratorValue = null;
		int objectFieldIndex = 0;
		SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Document document = new Document();

		String makeDir = FileUtil.storeXls(request.getSession().getId());
		String fileName = getText(entityName + "ListFile") + fileNameDateFormat.format(new Date()) + ".pdf";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		PdfWriter.getInstance(document, fileOut);

		document.open();

		PdfPTable table = null;

		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap();

		com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance(exportLogo());
		logo.scaleAbsolute(100, 50);
		; // resizing logo image size.

		document.add(logo); // Adding logo in PDF file.

		// below of code for title text
		titleFont = new Font(FontFamily.HELVETICA, 14, Font.BOLD, GrayColor.GRAYBLACK);
		title = new Paragraph(new Phrase(getText(entityName + "ExportPDFTitle"), titleFont));
		title.setAlignment(Element.ALIGN_CENTER);
		document.add(title);
		document.add(
				new Paragraph(new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK)))); // Add
																														// a
																														// blank
																														// line
																														// after
																														// title.

		columnHeaders = columnHeaders(entityName);

		if (!ObjectUtil.isEmpty(columnHeaders)) {
			if (!ObjectUtil.isEmpty(obj)) {
				PdfPCell cell = null; // cell for table.
				cellFont = new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK); // setting
																								// font
																								// for
																								// header
																								// cells
																								// of
																								// table.
				table = new PdfPTable(columnHeaders.split("\\,").length); // Code
																			// for
																			// setting
																			// table
																			// column
																			// Numbers.
				table.setWidthPercentage(100); // Set Table Width.
				table.getDefaultCell().setUseAscender(true);
				table.getDefaultCell().setUseDescender(true);
				for (String cellHeader : columnHeaders.split("\\,")) {
					cell = new PdfPCell(new Phrase(cellHeader, cellFont));
					cell.setBackgroundColor(new BaseColor(255, 255, 224));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setNoWrap(false); // To set wrapping of text in cell.
					// cell.setColspan(3); // To add column span.
					table.addCell(cell);
				}

				entityObject = obj.get(0); // set the class type for this entity
											// object property of this class.
				cellFont = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK); // setting
																								// font
																								// for
																								// cells.
				objectFieldNames = objectFieldNames(entityName);
				entityFieldsList = ReflectUtil.buildPDFData(obj, objectFieldNames);
				if (!ObjectUtil.isEmpty(entityFieldsList)) { // Check if utility
																// returns a
																// null list
																// object.
					for (Object[] entityObj : entityFieldsList) { // iterate
																	// over all
																	// list of
																	// objects.
						tempFieldMaps = new LinkedHashMap<String, Map>(fieldMaps);
						fieldNamesList = new ArrayList<String>(tempFieldMaps.keySet());
						iterator = fieldNamesList.iterator();
						for (Object entityField : entityObj) {

							/*
							 * BEGIN of Code to iterate over maps passed in arg
							 * for fields
							 */
							for (; iterator.hasNext();) { // Iterate over map
															// key.
								iteratorValue = iterator.next();
								String fieldName = iteratorValue.toString();
								fieldName = StringUtil.substringLast(fieldName, ".");
								if (objectFieldNames[objectFieldIndex].equals(fieldName)) { // If
																							// equals
																							// the
																							// change
																							// entity
																							// field
																							// to
																							// the
																							// map
																							// value.

									Map propertyValue = tempFieldMaps.get(iteratorValue);
									entityField = propertyValue.get(entityField);
									iterator.remove();
									break;
								}
							}
							if (ObjectUtil.isEmpty(entityField)) {
								entityField = " ";
							}
							iterator = fieldNamesList.listIterator(); // reset
																		// iterator
																		// to
																		// start
																		// element.
							/*
							 * END of Code to iterate over maps passed in arg
							 * for fields
							 */

							// BEGIN OF CODE FOR A PARTICULAR CELL IN A ROW OF
							// TABLE TO PDF FILE.
							cell = new PdfPCell(new Phrase(
									StringUtil.isEmpty(entityField.toString()) ? getText("NA") : entityField.toString(),
									cellFont));
							table.addCell(cell);
							// END OF CODE FOR A PARTICULAR CELL IN A ROW OF
							// TABLE TO PDF FILE.

							if (objectFieldIndex < (columnHeaders.split("\\,").length - 1)) {
								objectFieldIndex++;
							} else {
								objectFieldIndex = 0;

							}

						}

					}

					document.add(table); // Add table to document.
				}
			}
		}

		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		document.close();
		fileOut.close();
		return stream;
	}

	public String populatePDF() throws Exception {

		return null;
	}

	public String getCurrentMenu() {

		return getText("menu.select");
	}

	public InputStream getPDFExportStream(List<List<Object>> obj, List<String> filters)
			throws IOException, DocumentException, NoSuchFieldException, SecurityException {

		Font cellFont = null; // font for cells.
		Font titleFont = null; // font for title text.
		Paragraph title = null; // to add title for report
		String columnHeaders = null; // to hold column headers from property
										// file.
		SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Document document = new Document();

		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();

		String makeDir = FileUtil.storeXls(request.getSession().getId());
		String fileName = getText("ListFile") + fileNameDateFormat.format(new Date()) + ".pdf";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		PdfWriter.getInstance(document, fileOut);

		document.open();

		PdfPTable table = null;

		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap();

		com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance(exportLogo());
		logo.scaleAbsolute(100, 50);
		// resizing logo image size.

		document.add(logo); // Adding logo in PDF file.

		// below of code for title text
		titleFont = new Font(FontFamily.HELVETICA, 14, Font.BOLD, GrayColor.GRAYBLACK);
		title = new Paragraph(new Phrase(getText("ExportPDFTitle"), titleFont));
		title.setAlignment(Element.ALIGN_CENTER);
		document.add(title);
		document.add(
				new Paragraph(new Phrase(" ", new Font(FontFamily.HELVETICA, 15, Font.NORMAL, GrayColor.GRAYBLACK)))); // Add
																														// a
																														// blank
																														// line
																														// after
																														// title.

		for (String filter : filters) {
			document.add(new Paragraph(
					new Phrase(filter, new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK))));
			document.add(new Paragraph(
					new Phrase(" ", new Font(FontFamily.HELVETICA, 4, Font.NORMAL, GrayColor.GRAYBLACK))));
		}

		columnHeaders = columnHeaders();

		if (!ObjectUtil.isEmpty(columnHeaders)) {
			PdfPCell cell = null; // cell for table.
			cellFont = new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK); // setting
																							// font
																							// for
																							// header
																							// cells
																							// of
																							// table.
			table = new PdfPTable(columnHeaders.split("\\,").length); // Code
																		// for
																		// setting
																		// table
																		// column
																		// Numbers.
			table.setWidthPercentage(100); // Set Table Width.
			table.getDefaultCell().setUseAscender(true);
			table.getDefaultCell().setUseDescender(true);
			for (String cellHeader : columnHeaders.split("\\,")) {
				cell = new PdfPCell(new Phrase(cellHeader, cellFont));
				cell.setBackgroundColor(new BaseColor(255, 255, 224));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setNoWrap(false); // To set wrapping of text in cell.
				// cell.setColspan(3); // To add column span.
				table.addCell(cell);
			}

			cellFont = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK); // setting
																							// font
																							// for
																							// cells.
			for (List<Object> entityObj : obj) { // iterate over all list of
													// objects.
				for (Object entityField : entityObj) {
					// BEGIN OF CODE FOR A PARTICULAR CELL IN A ROW OF TABLE TO
					// PDF FILE.

					if (entityField != null) {
						cell = new PdfPCell(new Phrase(
								StringUtil.isEmpty(entityField.toString()) ? getText("NA") : entityField.toString(),
								cellFont));
						table.addCell(cell);
					} else {
						cell = new PdfPCell(new Phrase("", cellFont));
						table.addCell(cell);
					}
					// END OF CODE FOR A PARTICULAR CELL IN A ROW OF TABLE TO
					// PDF FILE.
				}
			}

			document.add(table); // Add table to document.
		}
		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		document.close();
		fileOut.close();
		return stream;
	}

	public String columnHeaders() {

		String columnHeaders = null; // String to get column headers from
										// property file.

		if (!StringUtil.isEmpty(getBranchId())) { // Check if branch User is
													// logged in.
			columnHeaders = getLocaleProperty("ExportHeadingNoBranch");
		} else {
			columnHeaders = getText("ExportHeading");
		}
		return columnHeaders;
	}

	public String getMainBranchName() {

		if (ObjectUtil.isEmpty(request)) {
			request = ReflectUtil.getCurrentHttpRequest();
		}
		Object biObj = ObjectUtil.isEmpty(request) ? null
				: request.getSession().getAttribute(ESESystem.MAIN_BRANCH_NAME);
		return ObjectUtil.isEmpty(biObj) ? null : biObj.toString();
	}

	public String getLocaleProperty(String prop) {
		String locProp = "";
		if (!ObjectUtil.isEmpty(clientService)) {
			if (localeMap.size() <= 0) {
				clientService.listLocalePropByLang(getCurrentLanguage()).stream().forEach(entry -> {
					if (!localeMap.containsKey(entry.getCode())) {
						localeMap.put(entry.getCode(), entry.getLangValue());
					}
				});
			}
			if (localeMap.containsKey(prop)) {
				locProp = localeMap.get(prop);
			}
		}
		return StringUtil.isEmpty(locProp) ? super.getText(prop) : locProp;
	}

	public String getEnableProductType(String prop) {
		if (!StringUtil.isEmpty(prop) && prop.trim().contains(ESESystem.PRODUCT_COTTON)) {
			prop = prop.replace(ESESystem.PRODUCT_COTTON, ESESystem.PRODUCT_CROP);
		}

		else if (!StringUtil.isEmpty(prop) && prop.trim().contains(ESESystem.PRODUCT_COTTON.toLowerCase())) {
			prop = prop.replace(ESESystem.PRODUCT_COTTON.toLowerCase(), ESESystem.PRODUCT_CROP);
		}

		return prop;

	}

	public String getDBProperty(String prop, String language) {

		String locProp = clientService.findLocaleProperty(prop, language);
		return locProp;
	}

	public String getCurrentLanguage() {

		return (String) ReflectUtil.getCurrentHttpSession().getAttribute(ISecurityFilter.LANGUAGE);
	}

	public String getCurrentTenantId() {

		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
		String tenantId = ISecurityFilter.DEFAULT_TENANT_ID;
		if (!ObjectUtil.isEmpty(request)) {
			tenantId = (String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID);
		}
		if (StringUtil.isEmpty(tenantId)) {
			tenantId = ISecurityFilter.DEFAULT_TENANT_ID;
		}
		return tenantId;
	}

	public String getCurrentSeasonsCode() {

		String currentSessionCode = clientService.findCurrentSeasonCode();
		return currentSessionCode;
	}

	public String getCurrentSeasonsName() {

		String currentSessionName = clientService.findCurrentSeasonName();
		return currentSessionName;
	}

	protected Map<String, String> getSearchAutoText() {

		setJQGridParams();
		Map<String, String> searchRecord = new HashMap<String, String>();
		if (!StringUtil.isEmpty(request.getParameter("filters"))) {
			searchRecord = getFilters(request.getParameter("filters"));
		}
		return searchRecord;
	}

	protected void setJQGridParams() {

		setPage(page());
		setResults(Integer.valueOf(request.getParameter("rows")));
		setSort(request.getParameter("sidx"));
		setStartIndex((getPage() - 1) * getResults());
		setDir(request.getParameter("sord"));
	}

	public String getIsParentBranch() {
		if (StringUtil.isEmpty(parentBranch)) {
			buildParentBranch();
		}
		return parentBranch;
	}

	private String buildParentBranch() {
		parentBranch = String.valueOf(clientService.isParentBranch(getBranchId()));
		return parentBranch;
	}

	public String getGeneralDateFormat(String inputDate) {
		String generalDate = null;
		String result = null;
		try {
			ESESystem preferences = preferncesService.findPrefernceById("1");
			if (!StringUtil.isEmpty(preferences)) {
				DateFormat genDate = new SimpleDateFormat(
						preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
				if (inputDate.toString().contains(".0")) {
					String replaceDate = inputDate.toString().replace(".0", "");
					generalDate = replaceDate;
				} else {
					generalDate = inputDate;
				}

				result = genDate.format(generalDate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	public String getGeneralDateFormat() {
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			return preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT);
		}
		return null;

	}

	public String getCurrencyType() {
		String result = null;

		if (!StringUtil.isEmpty(getBranchId())) {
			ESESystem preferences = preferncesService.findPrefernceByOrganisationId(getBranchId());
			if (!StringUtil.isEmpty(preferences)) {
				result = preferences.getPreferences().get(ESESystem.CURRENCY_TYPE);
				if (result == null) {
					ESESystem preferencess = preferncesService.findPrefernceById("1");
					if (!StringUtil.isEmpty(preferencess)) {
						result = preferencess.getPreferences().get(ESESystem.CURRENCY_TYPE);
					}
				}
			}

		} else {
			ESESystem preferencess = preferncesService.findPrefernceById("1");
			if (!StringUtil.isEmpty(preferencess)) {
				result = preferencess.getPreferences().get(ESESystem.CURRENCY_TYPE);
			}
		}
		return result != null && !StringUtil.isEmpty(result) ? result : ESESystem.CURRENCY_TYPE;

	}

	public String getAreaType() {
		String result = null;
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences) && preferences.getPreferences().get(ESESystem.AREA_TYPE) != null) {
			result = preferences.getPreferences().get(ESESystem.AREA_TYPE);
			result = result.contains("-") ? result.split("-")[1] : result;

		}
		return !StringUtil.isEmpty(result) ? result : ESESystem.AREA_TYPE;

	}

	/**
	 * Singleton Object
	 * 
	 * @return
	 */
	public static JSONObject getJsonObject() {
		if (jsonObject == null) {
			jsonObject = new JSONObject();
		}
		return jsonObject;
	}

	public boolean isCountryUser() {

		boolean isCountryUser = false;
		Object isCountryUserObj = getUserInfo(ISecurityFilter.IS_COUNTRY_USER);
		if (!ObjectUtil.isEmpty(isCountryUserObj)) {
			isCountryUser = (Boolean) isCountryUserObj;
		}
		return isCountryUser;
	}

	@SuppressWarnings("unchecked")
	protected Object getUserInfo(String key) {

		Object value = null;
		Map<String, Object> userInfo = (Map<String, Object>) request.getSession()
				.getAttribute(ISecurityFilter.USER_INFO);
		if (!ObjectUtil.isEmpty(userInfo)) {
			value = userInfo.get(key);
		}
		return value;
	}

	public String getUserCountry() {

		String profileId = null;
		String country = (String) request.getSession().getAttribute(ISecurityFilter.DEFAULT_COUNTRY);

		return country;
	}

	public JSONArray toJsonMaps(List objects, String[] columnNames, String seperater) {

		JSONArray jsonArray = new JSONArray();
		for (Object obj : objects) {
			String key = "", value = "";
			key = String.valueOf(ReflectUtil.getFieldValue(obj, columnNames[0]));
			for (int i = 1; i < columnNames.length; i++) {
				value += String.valueOf(ReflectUtil.getFieldValue(obj, columnNames[i])) + seperater;
			}
			value = StringUtil.removeLastString(value, seperater);
			JSONObject jsonObject = new JSONObject();
			String val = value.trim();
			if (questMap.size() <= 0) {
				buildQuestMap();
			}
			if (questMap.size() > 0) {
				if (questMap.containsKey(getCurrentLanguage())) {
					Map<String, String> lpCodes = questMap.get(getCurrentLanguage());
					val = lpCodes != null && lpCodes.containsKey(key) ? lpCodes.get(key) : val;
				} else if (questMap.containsKey("en")) {
					Map<String, String> lpCodes = questMap.get("en");
					val = lpCodes != null && lpCodes.containsKey(key) ? lpCodes.get(key) : val;
				}
			}
			jsonObject.put(key, val);
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}

	protected Map<String, String> getObjectFieldNames(String entityName) {

		Map<String, String> map = new LinkedHashMap<String, String>();
		ResourceBundle rb = ResourceBundle.getBundle(SwitchAction.class.getName());
		Enumeration<String> keys = rb.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			String value = rb.getString(key);
			if (key.startsWith(entityName + ".")) {
				map.put(key, value);
			}
		}

		return map;
	}

	protected <E extends Enum<E>> Map<Integer, String> getEnumOrdinalNames(Class<E> enumData) {

		Map<Integer, String> map = new LinkedHashMap<Integer, String>();
		for (Enum<E> enumVal : enumData.getEnumConstants()) {
			map.put(enumVal.ordinal(), getText(
					enumData.getName().substring(enumData.getName().lastIndexOf("$") + 1).trim() + enumVal.ordinal()));
		}
		return map;
	}

	public static Field getField(String classAndFieldName) {

		String fieldName = StringUtil.substringLast(classAndFieldName, ".");
		String className = StringUtil.substringBeforeLast(classAndFieldName, ".");
		Field field = null;
		try {
			Class<?> objClass = Class.forName(className);
			field = objClass.getDeclaredField(fieldName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return field;
	}

	protected <E extends Enum<E>> Map<String, String> getEnumStringNames(Class<E> enumData) {

		Map<String, String> map = new LinkedHashMap<String, String>();
		for (Enum<E> enumVal : enumData.getEnumConstants()) {
			map.put(enumVal.toString(), getText(
					enumData.getName().substring(enumData.getName().lastIndexOf("$") + 1).trim() + enumVal.ordinal()));
		}
		return map;
	}

	public boolean isAgentUser() {

		boolean isAgentUser = false;
		Object isAgentUserObj = getUserInfo(ISecurityFilter.IS_AGENT_USER);
		if (!ObjectUtil.isEmpty(isAgentUserObj)) {
			isAgentUser = (Boolean) isAgentUserObj;
		}
		return isAgentUser;
	}

	public String getLoggedInUserLanguage() {

		if (ObjectUtil.isEmpty(request)) {
			request = ReflectUtil.getCurrentHttpRequest();
		}

		Object language = (request != null && request.getSession() != null)
				? request.getSession().getAttribute(ISecurityFilter.LANGUAGE) : DEFAULT_LANG;
		;
		return !ObjectUtil.isEmpty(language) ? String.valueOf(language) : DEFAULT_LANG;
	}

	public String getLocaleProperty(String key, String loggedInUserLanguage) {

		if (mailExport) {
			return getText(key);
		}
		// Scheduler
		if (ObjectUtil.isEmpty(errors)) {
			errors = new Properties();
			try {
				errors.load(getClass()
						.getResourceAsStream(getClass().getSimpleName() + "_" + loggedInUserLanguage + ".properties"));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				try {
					Class clazz = SwitchAction.class;
					errors.load(clazz.getResourceAsStream("SwitchAction_" + loggedInUserLanguage + ".properties"));
				} catch (IOException es) {
					es.printStackTrace();
				}
			}
		}
		return errors.getProperty(key);
	}

	public String getAgentProfileId() {

		String profileId = null;
		Object profileIdObj = getUserInfo(ISecurityFilter.AGENT_PROFILE_ID);
		if (!ObjectUtil.isEmpty(profileIdObj)) {
			profileId = (String) profileIdObj;
		}
		return profileId;
	}

	public String getLoggedInRole() {

		String role = (String) request.getSession().getAttribute(ISecurityFilter.ROLE);
		return role;
	}

	public String getUserFullName() {

		if (ObjectUtil.isEmpty(request)) {
			request = ReflectUtil.getCurrentHttpRequest();
		}
		Object biObj = ObjectUtil.isEmpty(request) ? null : request.getSession().getAttribute("userFullName");
		return ObjectUtil.isEmpty(biObj) ? null : biObj.toString();

	}

	public FarmCatalogue getCatlogueValueByCode(String code) {
		if (ObjectUtil.isEmpty(getFarmCatalogueList()) && ObjectUtil.isListEmpty(getFarmCatalogueList())) {
			setFarmCatalogueList(new ArrayList<>());
			setFarmCatalogueList(catalogueService.listCatalogues());
		}
		FarmCatalogue catValue = getFarmCatalogueList().stream().filter(fc -> fc.getCode().equalsIgnoreCase(code))
				.findAny().orElseGet(() -> {
					FarmCatalogue tmp = new FarmCatalogue();
					tmp.setName("");
					tmp.setDispName("");
					return tmp;
				});
		String name = getLanguagePref(getLoggedInUserLanguage(), catValue.getCode());
		catValue.setName(StringUtil.isEmpty(name) ? catValue.getName() : name);

		return catValue;
	}

	public String getCatlogueValueByCodeArray(String code) {
		if (ObjectUtil.isEmpty(getFarmCatalogueList()) && ObjectUtil.isListEmpty(getFarmCatalogueList())) {
			setFarmCatalogueList(new ArrayList<>());
			setFarmCatalogueList(catalogueService.listCatalogues());
		}
		name = "";
		if (code.contains(",")) {
			Arrays.asList(code.split(",")).stream().forEach(u -> {
				FarmCatalogue catValue = getFarmCatalogueList().stream()
						.filter(fc -> fc.getCode().equalsIgnoreCase(u.trim())).findAny().orElseGet(() -> {
							FarmCatalogue tmp = new FarmCatalogue();
							tmp.setName("");
							tmp.setDispName("");
							return tmp;
						});
				String tmName = getLanguagePref(getLoggedInUserLanguage(), catValue.getCode());
				name += tmName == null || StringUtil.isEmpty(tmName) ? catValue.getName() + "," : tmName;

			});
			name = StringUtil.removeLastComma(name);
		} else {
			FarmCatalogue catValue = getFarmCatalogueList().stream().filter(fc -> fc.getCode().equalsIgnoreCase(code))
					.findAny().orElseGet(() -> {
						FarmCatalogue tmp = new FarmCatalogue();
						tmp.setName("");
						tmp.setDispName("");
						return tmp;
					});

			String tmName = getLanguagePref(getLoggedInUserLanguage(), catValue.getCode());
			name = tmName == null || StringUtil.isEmpty(tmName) ? catValue.getName() : tmName;
			name = StringUtil.removeLastComma(name);
		}

		return name;
	}

	public HarvestSeason getHarvestSeason(String code) {
		HarvestSeason catValue = new HarvestSeason();
		if (seasonMap.size() <= 0) {
			farmerService.listHarvestSeasons().stream().forEach(fc -> {
				if (!seasonMap.containsKey(fc.getCode())) {
					seasonMap.put(fc.getCode(), fc);
				}
			});
		}
		if (seasonMap.containsKey(code)) {
			catValue = seasonMap.get(code);
			if (catValue == null || ObjectUtil.isEmpty(catValue)) {
				catValue = new HarvestSeason();
				catValue.setName("");
			}

		}
		return catValue;
	}

	List<FarmCatalogue> farmCatalougeList = new ArrayList<>();
	private String imgId;

	public Map<String, String> getFarmCatalougeMap(Integer type) {
		List<FarmCatalogue> tempCatalogueList = new ArrayList<>();
		if (ObjectUtil.isListEmpty(farmCatalougeList)) {
			farmCatalougeList = catalogueService.listCatalogues();
		}
		tempCatalogueList = farmCatalougeList.stream().filter(fc -> fc.getTypez() == type)
				.sorted((o1, o2) -> Long.valueOf(o1.getId()).compareTo(o2.getId())).collect(Collectors.toList());
		Map<String, String> catList = new LinkedHashMap<>();
		Class clazz = FarmCatalogue.class;
		Field field = null;
		for (FarmCatalogue fc : tempCatalogueList) {
			if (!catList.containsKey(fc.getCode()))
				try {
					String name = getLanguagePref(getLoggedInUserLanguage(), fc.getCode());
					fc.setName(StringUtil.isEmpty(name) ? fc.getName() : name);

					catList.put(fc.getCode(),
							field != null && field.get(fc) != null
									? field.get(fc) != null && !StringUtil.isEmpty(field.get(fc))
											? field.get(fc).toString() : fc.getName()
									: fc.getName());
				} catch (Exception e) {
					catList.put(fc.getCode(), fc.getName());
				}
		}
		return catList;

	}

	public Map<String, Map<String, String>> buildQuestMap() {
		if (questMap == null || questMap.isEmpty()) {
			List<LanguagePreferences> lpList = certificationService.listLanguagePreferences();
			if (lpList == null || lpList.size() <= 0) {
				questMap.put("en", new HashMap<>());
			}
			for (LanguagePreferences lp : lpList) {
				if (questMap.containsKey(lp.getLang())) {
					Map<String, String> maps = questMap.get(lp.getLang());
					if (!maps.containsKey(lp.getCode())) {
						maps.put(lp.getCode(), lp.getName());
						questMap.put(lp.getLang(), maps);
					}
				} else {
					Map<String, String> maps = new HashMap<>();
					if (!maps.containsKey(lp.getCode())) {
						maps.put(lp.getCode(), lp.getName());
						questMap.put(lp.getLang(), maps);
					}

				}
			}
		}
		return questMap;

	}

	public Map buildMapSurvey(List objects, String[] columnNames) {

		return buildMapSurvey(objects, columnNames, " ");
	}

	public Map buildMapSurvey(List objects, String[] columnNames, String seperater) {
		buildQuestMap();
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (Object obj : objects) {
			String key = "", value = "";
			Object fieldValue = ReflectUtil.getFieldValue(obj, columnNames[0]);
			key = ObjectUtil.isEmpty(fieldValue) ? "" : String.valueOf(fieldValue);
			for (int i = 1; i < columnNames.length; i++) {
				fieldValue = ReflectUtil.getFieldValue(obj, columnNames[i]);
				value += (ObjectUtil.isEmpty(fieldValue) ? "" : String.valueOf(fieldValue)) + seperater;
			}
			value = StringUtil.removeLastString(value, seperater);
			if (!StringUtil.isEmpty(key)) {
				String val = value;
				if (questMap != null) {
					if (questMap.containsKey(getCurrentLanguage())) {
						Map<String, String> lpCodes = questMap.get(getCurrentLanguage());
						val = lpCodes != null && lpCodes.containsKey(key) ? lpCodes.get(key) : val;
					} else if (questMap.containsKey("en")) {
						Map<String, String> lpCodes = questMap.get("en");
						val = lpCodes != null && lpCodes.containsKey(key) ? lpCodes.get(key) : val;
					}
				}

				map.put(key, val);
			}
		}
		return map;
	}

	/*
	 * public FarmCatalogue findfarmcatalogueByCode(String type) { FarmCatalogue
	 * fc = farmerService.findfarmcatalogueByCode(type); if(fc!=null){ return
	 * fc; }
	 */

	public String getCSeasonCodeBasedOnBranch() {

		String val = "";
		return val = clientService.findCurrentSeasonCodeByBranchId(getBranchId());

	}

	List<String> cts = new ArrayList<>();
	String formula = "";
	String isScore = "0";

	@SuppressWarnings("unchecked")
	public void populateDynamicFields() {
		isScore = "0";
		Map<String, List<JSONObject>> jsonMap = new LinkedHashMap<>();
		List<JSONObject> fieldList = new LinkedList<>();
		List<JSONObject> groupList = new LinkedList<>();
		List<JSONObject> sectionList = new LinkedList<>();
		List<DynamicSectionConfig> secList = new ArrayList<>();
		if (branch == null || StringUtil.isEmpty(branch)) {
			branch = getBranchId();
		}
		List<DynamicFeildMenuConfig> dyList = farmerService.findDynamicMenusByType(getTxnTypez(), branch);
		Map<String,DynamicMenuFieldMap> menMap  = (Map<String,DynamicMenuFieldMap>)dyList.stream()
				.flatMap(listContainer -> listContainer.getDynamicFieldConfigs().stream())
				.collect(Collectors.toList()).stream().collect(Collectors.toMap(u -> u.getField().getCode(), obj -> obj));
		List<DynamicConstants> dsc = farmerService.listDynamicConstants();
		cts = dsc.stream().map(u -> u.getCode()).collect(Collectors.toList());

		if (dyList != null && !dyList.isEmpty()) {
			isScore = dyList.get(0).getIsScore().toString();
			List<DynamicFieldConfig> dynamicFieldsConfigList = (List<DynamicFieldConfig>) dyList.stream()
					.flatMap(listContainer -> listContainer.getDynamicFieldConfigs().stream())
					.collect(Collectors.toList()).stream().map(u -> u.getField()).collect(Collectors.toList());
			dyList.stream().forEach(dynamicMenu -> {
				dynamicMenu.getDynamicSectionConfigs().stream().filter(
						u -> (u.getSection().getMobileFieldsSize() > 0 || (queryType != null && queryType.equals("1"))))
						.forEach(dynamicSection -> {
							JSONObject obj = new JSONObject();
							// obj.put("secCode",
							// dynamicSection.getSectionCode());
							// obj.put("secName",
							// dynamicSection.getSectionName());
							obj.put("secCode", dynamicSection.getSection().getSectionCode());
							obj.put("secName",
									StringUtil.isEmpty(getLanguagePref(getLoggedInUserLanguage(),
											dynamicSection.getSection().getSectionCode()))
													? dynamicSection.getSection().getSectionName()
													: getLanguagePref(getLoggedInUserLanguage(),
															dynamicSection.getSection().getSectionCode()));
							obj.put("secOrder", dynamicSection.getSection().getSecorder());
							obj.put("isScore", isScore);
							obj.put("typez", dynamicSection.getSection().getTableId() != null
									? dynamicSection.getSection().getTableId() : "");
							sectionList.add(obj);
							secList.add(dynamicSection.getSection());
						});
			});

			dynamicFieldsConfigList.stream()
					.filter(dynamicFieldConfig -> ObjectUtil.isEmpty(dynamicFieldConfig.getReferenceId())
							|| !StringUtil.isLong(dynamicFieldConfig.getReferenceId()))
					.forEach(dynamicFieldConfig -> {
						if (((Arrays.asList("1", "0","4").contains(dynamicFieldConfig.getIsMobileAvail())
								&& !Arrays.asList(1, 2, 4).contains(dynamicFieldConfig.getFollowUp()))
								|| ((queryType != null && queryType.equals("1"))
										&& !Arrays.asList(1, 2, 4).contains(dynamicFieldConfig.getFollowUp())))) {
							JSONObject obj = new JSONObject();
							/*if (dynamicFieldConfig.getParentActKey() != null
									&& !StringUtil.isEmpty(dynamicFieldConfig.getParentActKey())
									&& (dynamicFieldConfig.getFollowUp() == 1
											|| dynamicFieldConfig.getFollowUp() == 2)) {
								dynamicFieldConfig
										.setParentDependencyKey(dynamicFieldConfig.getParentDependencyKey() != null
												&& !StringUtil.isEmpty(dynamicFieldConfig.getParentDependencyKey())
														? dynamicFieldConfig.getParentDependencyKey() + ","
																+ dynamicFieldConfig.getParentActKey()
														: dynamicFieldConfig.getParentActKey());
							}
							if (dynamicFieldConfig.getParentActKey() != null
									&& !StringUtil.isEmpty(dynamicFieldConfig.getParentActKey())
									&& (dynamicFieldConfig.getFollowUp() == 3)) {
								dynamicFieldConfig.setCatDependencyKey(dynamicFieldConfig.getCatDependencyKey() != null
										&& !StringUtil.isEmpty(dynamicFieldConfig.getCatDependencyKey())
												? dynamicFieldConfig.getCatDependencyKey() + ","
														+ dynamicFieldConfig.getParentActKey()
												: dynamicFieldConfig.getParentActKey());
							}*/
							String depeCode = !ObjectUtil.isEmpty(dynamicFieldConfig.getParentDepen())
									? dynamicFieldConfig.getParentDepen().getCode() : "";
							/*if (dynamicFieldConfig.getParentActField() != null
									&& !StringUtil.isEmpty(dynamicFieldConfig.getParentActField())
									&& (dynamicFieldConfig.getFollowUp() == 1
											|| dynamicFieldConfig.getFollowUp() == 2)) {
								depeCode = dynamicFieldConfig.getParentActField();
							}*/

							obj.put("id", dynamicFieldConfig.getId());
							obj.put("secCode", dynamicFieldConfig.getDynamicSectionConfig().getSectionCode());
							obj.put("compoType", dynamicFieldConfig.getComponentType());
							obj.put("compoName", StringUtil
									.isEmpty(getLanguagePref(getLoggedInUserLanguage(), dynamicFieldConfig.getCode()))
											? dynamicFieldConfig.getComponentName()
											: getLanguagePref(getLoggedInUserLanguage(), dynamicFieldConfig.getCode()));
							obj.put("compoCode", dynamicFieldConfig.getCode());
							obj.put("parentDepen", ObjectUtil.isEmpty(dynamicFieldConfig.getParentDepen()) ? ""
									: dynamicFieldConfig.getParentDepen().getId());
							obj.put("parentDepenCode", depeCode);
							obj.put("parentDepenKey", ObjectUtil.isEmpty(dynamicFieldConfig.getParentDependencyKey())
									? "" : dynamicFieldConfig.getParentDependencyKey());
							obj.put("catDepKey", ObjectUtil.isEmpty(dynamicFieldConfig.getCatDependencyKey()) ? ""
									: dynamicFieldConfig.getCatDependencyKey());
							obj.put("maxLen", dynamicFieldConfig.getComponentMaxLength());
							obj.put("isReq", dynamicFieldConfig.getIsRequired());
							obj.put("sectionId", dynamicFieldConfig.getDynamicSectionConfig().getSectionCode());
							obj.put("sectionId", dynamicFieldConfig.getDynamicSectionConfig().getSectionCode());
							obj.put("isOther", dynamicFieldConfig.getIsOther());
							obj.put("valueDep", dynamicFieldConfig.getValueDependency());
							obj.put("isMobile", dynamicFieldConfig.getIsMobileAvail());
							obj.put("isScore", isScore);
							obj.put("typez", dynamicFieldConfig.getDynamicSectionConfig().getTableId() != null
									? dynamicFieldConfig.getDynamicSectionConfig().getTableId() : "");

							JSONArray cName = new JSONArray();
							JSONArray cType = new JSONArray();

							String ct = dynamicFieldConfig.getCatalogueType();
							if (dynamicFieldConfig.getAccessType() > 0 && dynamicFieldConfig.getAccessType() == 1) {

								Integer type = new Integer(ct);
								Map<String, String> farmCatalougeList = getFarmCatalougeMap(type);
								for (Map.Entry<String, String> entry : farmCatalougeList.entrySet()) {
									cType.add(entry.getKey());
									cName.add(entry.getValue());

								}
							} else if (dynamicFieldConfig.getAccessType() > 0
									&& dynamicFieldConfig.getAccessType() == 2) {
								// String ct =
								// dynamicFieldConfig.getCatalogueType();
								if (!StringUtil.isEmpty(ct)) {
									for (String val : ct.split(",")) {
										if ((val.contains("CG"))) {
											cType.add(val);
											FarmCatalogue fc = getCatlogueValueByCode(val.trim());
											cName.add(fc.getName());
										} else {
											Integer type = new Integer(val);
											Map<String, String> farmCatalougeList = getFarmCatalougeMap(type);
											for (Map.Entry<String, String> entry : farmCatalougeList.entrySet()) {
												cType.add(entry.getKey());
												cName.add(entry.getValue());

											}

										}
									}
								}
							} else if (dynamicFieldConfig.getAccessType() > 0
									&& dynamicFieldConfig.getAccessType() == 3) {
								LIST_METHOD[] methods = DynamicFieldConfig.LIST_METHOD.values();
								try {
									for (Map.Entry<String, String> entry : getOptions(
											methods[Integer.valueOf(ct)].toString()).entrySet()) {
										cType.add(entry.getKey());
										cName.add(entry.getValue());

									}
								} catch (Exception e) {

								}
							}
							obj.put("catalogueType", cType);
							obj.put("catalogueName", cName);
							obj.put("catType", dynamicFieldConfig.getCatalogueType());
							obj.put("accessType", dynamicFieldConfig.getAccessType());
							obj.put("dataFormat", dynamicFieldConfig.getDataFormat());

							obj.put("beforeInsert", menMap.get(dynamicFieldConfig.getCode()).getBeforeInsert());
							obj.put("afterInsert", menMap.get(dynamicFieldConfig.getCode()).getAfterInsert());
							obj.put("validation", dynamicFieldConfig.getValidation());
							obj.put("defaultVal", dynamicFieldConfig.getDefaultValue());
							formula = "";
							if (dynamicFieldConfig.getDependencyKey() != null
									&& !StringUtil.isEmpty(dynamicFieldConfig.getDependencyKey())) {
								List<DynamicFieldConfig> formulaList = dynamicFieldsConfigList.stream()
										.filter(forumula -> Arrays
												.asList(dynamicFieldConfig.getDependencyKey().contains(",")
														? dynamicFieldConfig.getDependencyKey().split(",")
														: dynamicFieldConfig.getDependencyKey())
												.contains(forumula.getCode()))
										.collect(Collectors.toList());
								if (formulaList != null && !formulaList.isEmpty()) {
									formula = formulaList.stream().map(DynamicFieldConfig::getFormula)
											.collect(Collectors.joining(","));
								}
							}
							obj.put("constExist", "0");
							if (StringUtil.isEmpty(formula) || formula.equals("null") || formula.equals("")) {

								obj.put("formula", dynamicFieldConfig.getFormula());
							} else {

								if (cts != null && !cts.stream().filter(u -> formula.contains(u))
										.collect(Collectors.toList()).isEmpty()) {
									obj.put("constExist", "1");
								}

								obj.put("formula", formula);
							}

							obj.put("dependencyKey", dynamicFieldConfig.getDependencyKey());
							obj.put("orderSet", dynamicFieldConfig.getOrderSet());
							fieldList.add(obj);
						}
					});

			dynamicFieldsConfigList.stream()
					.filter(dynamicFieldConfig -> !ObjectUtil.isEmpty(dynamicFieldConfig.getReferenceId())
							&& StringUtil.isLong(dynamicFieldConfig.getReferenceId()))
					.forEach(dynamicFieldConfig -> {
						if (((Arrays.asList("1", "0").contains(dynamicFieldConfig.getIsMobileAvail())
								&& !Arrays.asList(1, 2, 4).contains(dynamicFieldConfig.getFollowUp()))
								|| ((queryType != null && queryType.equals("1"))
										&& !Arrays.asList(1, 2, 4).contains(dynamicFieldConfig.getFollowUp())))) {

							JSONObject obj = new JSONObject();
							if (dynamicFieldConfig.getParentActKey() != null
									&& !StringUtil.isEmpty(dynamicFieldConfig.getParentActKey())
									&& (dynamicFieldConfig.getFollowUp() == 1
											|| dynamicFieldConfig.getFollowUp() == 2)) {
								dynamicFieldConfig
										.setParentDependencyKey(dynamicFieldConfig.getParentDependencyKey() != null
												&& !StringUtil.isEmpty(dynamicFieldConfig.getParentDependencyKey())
														? dynamicFieldConfig.getParentDependencyKey() + ","
																+ dynamicFieldConfig.getParentActKey()
														: dynamicFieldConfig.getParentActKey());
							}
							if (dynamicFieldConfig.getParentActKey() != null
									&& !StringUtil.isEmpty(dynamicFieldConfig.getParentActKey())
									&& (dynamicFieldConfig.getFollowUp() == 3)) {
								dynamicFieldConfig.setCatDependencyKey(dynamicFieldConfig.getCatDependencyKey() != null
										&& !StringUtil.isEmpty(dynamicFieldConfig.getCatDependencyKey())
												? dynamicFieldConfig.getCatDependencyKey() + ","
														+ dynamicFieldConfig.getParentActKey()
												: dynamicFieldConfig.getParentActKey());
							}
							String depeCode = !ObjectUtil.isEmpty(dynamicFieldConfig.getParentDepen())
									? dynamicFieldConfig.getParentDepen().getCode() : "";
							if (dynamicFieldConfig.getParentActField() != null
									&& !StringUtil.isEmpty(dynamicFieldConfig.getParentActField())
									&& (dynamicFieldConfig.getFollowUp() == 1
											|| dynamicFieldConfig.getFollowUp() == 2)) {
								depeCode = dynamicFieldConfig.getParentActField();
							}

							obj.put("id", dynamicFieldConfig.getId());
							obj.put("secCode", dynamicFieldConfig.getDynamicSectionConfig().getSectionCode());
							obj.put("catType", dynamicFieldConfig.getCatalogueType());
							obj.put("valueDep", dynamicFieldConfig.getValueDependency());
							obj.put("compoType", dynamicFieldConfig.getComponentType());
							obj.put("compoName", StringUtil
									.isEmpty(getLanguagePref(getLoggedInUserLanguage(), dynamicFieldConfig.getCode()))
											? dynamicFieldConfig.getComponentName()
											: getLanguagePref(getLoggedInUserLanguage(), dynamicFieldConfig.getCode()));
							obj.put("compoCode", dynamicFieldConfig.getCode());
							obj.put("parentDepen", ObjectUtil.isEmpty(dynamicFieldConfig.getParentDepen()) ? ""
									: dynamicFieldConfig.getParentDepen().getId());
							obj.put("parentDepenCode", depeCode);
							obj.put("parentDepenKey", ObjectUtil.isEmpty(dynamicFieldConfig.getParentDependencyKey())
									? "" : dynamicFieldConfig.getParentDependencyKey());
							obj.put("catDepKey", ObjectUtil.isEmpty(dynamicFieldConfig.getCatDependencyKey()) ? ""
									: dynamicFieldConfig.getCatDependencyKey());
							obj.put("isOther", dynamicFieldConfig.getIsOther());
							obj.put("maxLen", dynamicFieldConfig.getComponentMaxLength());
							obj.put("isReq", dynamicFieldConfig.getIsRequired());
							obj.put("sectionId", dynamicFieldConfig.getDynamicSectionConfig().getSectionCode());
							obj.put("isScore", isScore);
							obj.put("typez", dynamicFieldConfig.getDynamicSectionConfig().getTableId() != null
									? dynamicFieldConfig.getDynamicSectionConfig().getTableId() : "");

							obj.put("isMobile", dynamicFieldConfig.getIsMobileAvail());
							JSONArray cName = new JSONArray();
							JSONArray cType = new JSONArray();

							String ct = dynamicFieldConfig.getCatalogueType();
							if (dynamicFieldConfig.getAccessType() > 0 && dynamicFieldConfig.getAccessType() == 1) {

								Integer type = new Integer(ct);
								Map<String, String> farmCatalougeList = getFarmCatalougeMap(type);
								for (Map.Entry<String, String> entry : farmCatalougeList.entrySet()) {
									cType.add(entry.getKey());
									cName.add(entry.getValue());

								}
							} else if (dynamicFieldConfig.getAccessType() > 0
									&& dynamicFieldConfig.getAccessType() == 1) {
								// String ct =
								// dynamicFieldConfig.getCatalogueType();
								if (!StringUtil.isEmpty(ct)) {
									for (String val : ct.split(",")) {
										if ((val.contains("CG"))) {
											cType.add(val);
											FarmCatalogue fc = getCatlogueValueByCode(val.trim());
											cName.add(fc.getName());
										} else {
											Integer type = new Integer(val);
											Map<String, String> farmCatalougeList = getFarmCatalougeMap(type);
											for (Map.Entry<String, String> entry : farmCatalougeList.entrySet()) {
												cType.add(entry.getKey());
												cName.add(entry.getValue());

											}

										}
									}
								}
							} else if (dynamicFieldConfig.getAccessType() > 0
									&& dynamicFieldConfig.getAccessType() == 3 ) {
								if(dynamicFieldConfig.getValueDependency()!=1){
								LIST_METHOD[] methods = DynamicFieldConfig.LIST_METHOD.values();
								try {
									for (Map.Entry<String, String> entry : getOptions(
											methods[Integer.valueOf(ct)].toString()).entrySet()) {
										cType.add(entry.getKey());
										cName.add(entry.getValue());

									}
								} catch (Exception e) {

								}
								}
							}
							obj.put("catalogueType", cType);
							obj.put("catalogueName", cName);
							obj.put("accessType", dynamicFieldConfig.getAccessType());
							obj.put("dataFormat", dynamicFieldConfig.getDataFormat());
							obj.put("beforeInsert", menMap.get(dynamicFieldConfig.getCode()).getBeforeInsert());
							obj.put("afterInsert", menMap.get(dynamicFieldConfig.getCode()).getAfterInsert());
							obj.put("validation", dynamicFieldConfig.getValidation());
							obj.put("defaultVal", dynamicFieldConfig.getDefaultValue());
							formula = "";
							if (dynamicFieldConfig.getDependencyKey() != null
									&& !StringUtil.isEmpty(dynamicFieldConfig.getDependencyKey())) {
								List<DynamicFieldConfig> formulaList = dynamicFieldsConfigList.stream()
										.filter(forumula -> Arrays
												.asList(dynamicFieldConfig.getDependencyKey().contains(",")
														? dynamicFieldConfig.getDependencyKey().split(",")
														: dynamicFieldConfig.getDependencyKey())
												.contains(forumula.getCode()))
										.collect(Collectors.toList());
								if (formulaList != null && !formulaList.isEmpty()) {
									formula = formulaList.stream().map(DynamicFieldConfig::getFormula)
											.collect(Collectors.joining(","));
								}
							}
							obj.put("constExist", "0");
							if (StringUtil.isEmpty(formula) || formula.equals("null") || formula.equals("")) {
								obj.put("formula", dynamicFieldConfig.getFormula());
							} else {
								if (cts != null && !cts.stream().filter(u -> formula.contains(u))
										.collect(Collectors.toList()).isEmpty()) {
									obj.put("constExist", "1");
								}

								obj.put("formula", formula);
							}
							obj.put("dependencyKey", dynamicFieldConfig.getDependencyKey());
							obj.put("refId", dynamicFieldConfig.getReferenceId());
							groupList.add(obj);
						}
					});
		}

		jsonMap.put("sections", sectionList);
		jsonMap.put("fields", fieldList);
		jsonMap.put("groups", groupList);

		JSONObject objects = new JSONObject();
		objects.putAll(jsonMap);

		printAjaxResponse(objects, "text/json");

	}

	@SuppressWarnings("unchecked")
	public void populateDynmaicFieldValuesByFarmerId() {

		if (!StringUtil.isEmpty(getSelectedObject()) && !StringUtil.isEmpty(getTxnTypez())) {
			List<DynamicSectionConfig> dynamicSectionConfigList = farmerService
					.findDynamicFieldsBySectionId(Farmer.TABLE_ID);

			dynamicSectionConfigList = dynamicSectionConfigList.stream()
					.filter(section -> (section.getType() != null && section.getType().contains(getTxnTypez())))
					.collect(Collectors.toList());

			Map<String, String> listFields = new LinkedHashMap<>();
			dynamicSectionConfigList.stream().forEach(dynmaicSection -> {
				dynmaicSection.getDynamicFieldConfigs().stream()
						.filter(dynamicFieldConfig -> !ObjectUtil.isEmpty(dynamicFieldConfig.getReferenceId())
								&& StringUtil.isLong(dynamicFieldConfig.getReferenceId()))
						.forEach(dynamicFieldConfig -> {
							listFields.put(dynamicFieldConfig.getCode(),
									String.valueOf(dynamicFieldConfig.getReferenceId()));
						});
			});
			Map<String, List<JSONObject>> jsonMap = new LinkedHashMap<>();
			List<JSONObject> groupList = new LinkedList<>();
			List<JSONObject> fieldList = new LinkedList<>();

			farmerService.listFarmerDynmaicFieldsByFarmerId(Long.valueOf(getSelectedObject()), getTxnTypez()).stream()
					.forEach(dynmaicFieldConfig -> {
						if (ObjectUtil.isEmpty(dynmaicFieldConfig.getParentId())
								|| dynmaicFieldConfig.getParentId() == 0) {
							JSONObject obj = new JSONObject();
							obj.put("code", dynmaicFieldConfig.getFieldName());
							obj.put("name", dynmaicFieldConfig.getFieldValue() == null ? ""
									: dynmaicFieldConfig.getFieldValue());
							obj.put("componentType", dynmaicFieldConfig.getComponentType());

							if (!StringUtil.isEmpty(dynmaicFieldConfig.getComponentType())
									&& Arrays
											.asList(String
													.valueOf(DynamicFieldConfig.COMPONENT_TYPES.DROP_DOWN.ordinal()),
													String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.RADIO.ordinal()),
													String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT
															.ordinal()),
													String.valueOf(
															DynamicFieldConfig.COMPONENT_TYPES.CHECKBOX.ordinal()))
											.contains(dynmaicFieldConfig.getComponentType())
									&& dynmaicFieldConfig.getAccessType() != null
									&& dynmaicFieldConfig.getAccessType().equals(1)) {
								obj.put("dispName",
										getCatlogueValueByCode(dynmaicFieldConfig.getFieldValue()).getName());
							} else if (!StringUtil.isEmpty(dynmaicFieldConfig.getComponentType())
									&& Arrays
											.asList(String
													.valueOf(DynamicFieldConfig.COMPONENT_TYPES.DROP_DOWN.ordinal()),
													String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.RADIO.ordinal()),
													String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT
															.ordinal()),
													String.valueOf(
															DynamicFieldConfig.COMPONENT_TYPES.CHECKBOX.ordinal()))
											.contains(dynmaicFieldConfig.getComponentType())
									&& dynmaicFieldConfig.getAccessType() != null
									&& dynmaicFieldConfig.getAccessType().equals(3)) {
								try {
									LIST_METHOD[] methods = DynamicFieldConfig.LIST_METHOD.values();
									Map<String, String> listValeus = getOptions(
											methods[Integer.valueOf(dynmaicFieldConfig.getListMethod())].toString());

									obj.put("dispName", listValeus.containsKey(dynmaicFieldConfig.getFieldValue())
											? listValeus.get(dynmaicFieldConfig.getFieldValue()) : "");
								} catch (Exception e) {
									obj.put("dispName", "");
								}
							} else {
								obj.put("dispName", dynmaicFieldConfig.getFieldValue());
							}
							if (dynmaicFieldConfig.getImageIds() != null
									&& !StringUtil.isEmpty(dynmaicFieldConfig.getImageIds())) {
								obj.put("photoCompoAvailable", "1");
								if (dynmaicFieldConfig.getComponentType().equals("13")
										|| dynmaicFieldConfig.getComponentType().equals("11")) {
									obj.put("photoCompoAvailable", "2");
								}
								obj.put("photoIds", dynmaicFieldConfig.getImageIds());
							} else {
								obj.put("photoCompoAvailable", "0");
								obj.put("photoByteStr", "");
							}
							fieldList.add(obj);
						} else {
							JSONObject obj = new JSONObject();
							obj.put("code", dynmaicFieldConfig.getFieldName());
							obj.put("name", dynmaicFieldConfig.getFieldValue() == null ? ""
									: dynmaicFieldConfig.getFieldValue());
							obj.put("refId", dynmaicFieldConfig.getParentId());
							obj.put("typez",
									dynmaicFieldConfig.getTypez() + (dynmaicFieldConfig.getFarmerDynamicData() == null
											? "" : "_" + dynmaicFieldConfig.getFarmerDynamicData().getId()));
							obj.put("componentType", dynmaicFieldConfig.getComponentType());

							if (!StringUtil.isEmpty(dynmaicFieldConfig.getComponentType())
									&& Arrays
											.asList(String
													.valueOf(DynamicFieldConfig.COMPONENT_TYPES.DROP_DOWN.ordinal()),
													String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.RADIO.ordinal()),
													String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT
															.ordinal()),
													String.valueOf(
															DynamicFieldConfig.COMPONENT_TYPES.CHECKBOX.ordinal()))
											.contains(dynmaicFieldConfig.getComponentType())
									&& dynmaicFieldConfig.getAccessType() == 1) {
								obj.put("dispName",
										getCatlogueValueByCode(dynmaicFieldConfig.getFieldValue()).getName());
							} else if (!StringUtil.isEmpty(dynmaicFieldConfig.getComponentType())
									&& Arrays
											.asList(String
													.valueOf(DynamicFieldConfig.COMPONENT_TYPES.DROP_DOWN.ordinal()),
													String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.RADIO.ordinal()),
													String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT
															.ordinal()),
													String.valueOf(
															DynamicFieldConfig.COMPONENT_TYPES.CHECKBOX.ordinal()))
											.contains(dynmaicFieldConfig.getComponentType())
									&& dynmaicFieldConfig.getAccessType() == 3) {

								try {
									LIST_METHOD[] methods = DynamicFieldConfig.LIST_METHOD.values();
									Map<String, String> listValeus = getOptions(
											methods[Integer.valueOf(dynmaicFieldConfig.getListMethod())].toString());

									obj.put("dispName", listValeus.containsKey(dynmaicFieldConfig.getFieldValue())
											? listValeus.get(dynmaicFieldConfig.getFieldValue()) : "");
								} catch (Exception e) {
									obj.put("dispName", "");
								}

							} else {
								obj.put("dispName", dynmaicFieldConfig.getFieldValue());
							}
							if (dynmaicFieldConfig.getImageIds() != null
									&& !StringUtil.isEmpty(dynmaicFieldConfig.getImageIds())) {
								obj.put("photoCompoAvailable", "1");
								if (dynmaicFieldConfig.getComponentType().equals("13")
										|| dynmaicFieldConfig.getComponentType().equals("11")) {
									obj.put("photoCompoAvailable", "2");
								}
								obj.put("photoIds", dynmaicFieldConfig.getImageIds());
							} else {
								obj.put("photoCompoAvailable", "0");
								obj.put("photoByteStr", "");
							}
							groupList.add(obj);
						}
					});// Date of Illness

			jsonMap.put("fields", fieldList);
			jsonMap.put("groups", groupList);

			JSONObject objects = new JSONObject();
			objects.putAll(jsonMap);

			printAjaxResponse(objects, "text/json");

		}

	}

	String cons = "";

	public void populateExtractFormula() {
		// String formulaEqnString = getFormulaEquation();
		Pattern p = Pattern.compile("\\{([^}]*)\\}");
		Matcher m = p.matcher(formulaEquation);
		StringBuilder ids = new StringBuilder();
		while (m.find()) {
			ids.append(m.group(1) + ",");
		}
		cons = "";
		List<DynamicConstants> consts = farmerService.listDynamicConstants();
		Map<String, String> cts = consts.stream().collect(Collectors.toMap(u -> u.getCode(), u -> u.getFieldName()));
		if (selectedObject != null && !StringUtil.isEmpty(selectedObject) && name != null
				&& !StringUtil.isEmpty(name)) {
			cts.entrySet().forEach(consta -> {
				if (formulaEquation.contains(consta.getKey())) {
					Pattern cp = Pattern.compile("\\##([^##]*)\\##");
					Matcher cm = cp.matcher(formulaEquation);
					while (cm.find()) {
						if (cts.containsKey(cm.group(1))) {
							String vlue = farmerService.getFieldValueByContant(selectedObject, name,
									cts.get(cm.group(1)));
							vlue = vlue == null || StringUtil.isEmpty(vlue) ? "0" : vlue;
							cons = cm.group(1) + "-" + vlue + ",";
						}

					}
				}
			});

			cons = StringUtil.removeLastComma(cons.toString());
		}
		// ids = StringUtil.removeLastComma(ids.toString());
		if (!StringUtil.isEmpty(cons)) {
			ids.append(StringUtil.removeLastComma(ids.toString()) + "~" + cons);
		}
		printAjaxResponse(StringUtil.removeLastComma(ids.toString()), "text");
	}

	@SuppressWarnings("unchecked")
	public void populateDynmaicFieldValuesByRefId() {
		if (!StringUtil.isEmpty(getSelectedObject()) && !StringUtil.isEmpty(getTxnTypez())) {
			List<DynamicFeildMenuConfig> dyList = farmerService.findDynamicMenusByType(getTxnTypez());
			if (dyList != null && !dyList.isEmpty()) {
				List<DynamicFieldConfig> dynamicFieldsConfigList = (List<DynamicFieldConfig>) dyList.stream()
						.flatMap(listContainer -> listContainer.getDynamicFieldConfigs().stream())
						.collect(Collectors.toList()).stream().map(u -> u.getField()).collect(Collectors.toList());
				Map<String, String> listFields = new LinkedHashMap<>();
				dynamicFieldsConfigList.stream()
						.filter(dynamicFieldConfig -> !ObjectUtil.isEmpty(dynamicFieldConfig.getReferenceId())
								&& StringUtil.isLong(dynamicFieldConfig.getReferenceId()))
						.forEach(dynamicFieldConfig -> {
							listFields.put(dynamicFieldConfig.getCode(),
									String.valueOf(dynamicFieldConfig.getReferenceId()));
						});

				Map<String, List<JSONObject>> jsonMap = new LinkedHashMap<>();
				List<JSONObject> groupList = new LinkedList<>();
				List<JSONObject> fieldList = new LinkedList<>();

				farmerService.listFarmerDynmaicFieldsByRefId(getSelectedObject(), getTxnTypez()).stream()
						.forEach(dynmaicFieldConfig -> {

							if (ObjectUtil.isEmpty(dynmaicFieldConfig.getParentId())
									|| dynmaicFieldConfig.getParentId() == 0) {
								JSONObject obj = new JSONObject();
								obj.put("code", dynmaicFieldConfig.getFieldName());
								obj.put("name", dynmaicFieldConfig.getFieldValue() == null ? ""
										: dynmaicFieldConfig.getFieldValue());
								obj.put("componentType", dynmaicFieldConfig.getComponentType());
								if (dynmaicFieldConfig.getFieldValue() != null) {
									if (!StringUtil.isEmpty(dynmaicFieldConfig.getComponentType())
											&& Arrays.asList(
													String.valueOf(
															DynamicFieldConfig.COMPONENT_TYPES.DROP_DOWN.ordinal()),
													String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.RADIO.ordinal()),
													String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT
															.ordinal()),
													String.valueOf(
															DynamicFieldConfig.COMPONENT_TYPES.CHECKBOX.ordinal()))
													.contains(dynmaicFieldConfig.getComponentType())
											&& dynmaicFieldConfig.getAccessType() != null
											&& (dynmaicFieldConfig.getAccessType().equals(1)
													|| dynmaicFieldConfig.getAccessType().equals(2))) {
										if (DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal() == Integer
												.valueOf(dynmaicFieldConfig.getComponentType())
												|| DynamicFieldConfig.COMPONENT_TYPES.CHECKBOX.ordinal() == Integer
														.valueOf(dynmaicFieldConfig.getComponentType())) {
											obj.put("dispName",
													getCatlogueValueByCodeArray(dynmaicFieldConfig.getFieldValue()));
										} else {
											obj.put("dispName",
													getCatlogueValueByCode(dynmaicFieldConfig.getFieldValue())
															.getName());
										}
									} else if (!StringUtil.isEmpty(dynmaicFieldConfig.getComponentType())
											&& Arrays.asList(
													String.valueOf(
															DynamicFieldConfig.COMPONENT_TYPES.DROP_DOWN.ordinal()),
													String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.RADIO.ordinal()),
													String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT
															.ordinal()),
													String.valueOf(
															DynamicFieldConfig.COMPONENT_TYPES.CHECKBOX.ordinal()))
													.contains(dynmaicFieldConfig.getComponentType())
											&& dynmaicFieldConfig.getAccessType() != null
											&& dynmaicFieldConfig.getAccessType().equals(3)) {
										try {
											LIST_METHOD[] methods = DynamicFieldConfig.LIST_METHOD.values();
											Map<String, String> listValeus = getOptions(
													methods[Integer.valueOf(dynmaicFieldConfig.getListMethod())]
															.toString());

											if (DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal() == Integer
													.valueOf(dynmaicFieldConfig.getComponentType())
													&& dynmaicFieldConfig.getFieldValue().contains(",")) {

												obj.put("dispName", listValeus.entrySet().stream()
														.filter(u -> Arrays
																.asList(dynmaicFieldConfig.getFieldValue().split(","))
																.contains(u.getKey()))
														.map(u -> u.getValue()).collect(Collectors.joining(",")));
											} else {
												obj.put("dispName",
														listValeus.containsKey(dynmaicFieldConfig.getFieldValue())
																? listValeus.get(dynmaicFieldConfig.getFieldValue())
																: "");
											}

										} catch (Exception e) {
											obj.put("dispName", "");
										}
									} else if (Integer.parseInt(dynmaicFieldConfig
											.getComponentType()) == DynamicFieldConfig.COMPONENT_TYPES.WEATHER_INFO
													.ordinal()) {
										watherInfo = "";
										if (dynmaicFieldConfig.getFieldValue() != null
												&& !StringUtil.isEmpty(dynmaicFieldConfig.getFieldValue())) {
											String[] arr = dynmaicFieldConfig.getFieldValue().split("\\|");
											AtomicInteger i = new AtomicInteger(0);
											Arrays.asList(getLocaleProperty("temp"), getLocaleProperty("rain"),
													getLocaleProperty("humidity"), getLocaleProperty("windSpeed"))
													.stream().forEach(u -> {
														try {
															watherInfo += u + " : "
																	+ arr[i.getAndIncrement()].toString() + " \n ";
														} catch (ArrayIndexOutOfBoundsException e) {
															watherInfo += u + " : " + " \n ";
														}
													});
										}

										obj.put("dispName", watherInfo);
									} else {
										obj.put("dispName", dynmaicFieldConfig.getFieldValue());
									}
								}
								if (dynmaicFieldConfig.getImageIds() != null
										&& !StringUtil.isEmpty(dynmaicFieldConfig.getImageIds())) {
									obj.put("photoCompoAvailable", "1");
									if (dynmaicFieldConfig.getComponentType().equals("13")
											|| dynmaicFieldConfig.getComponentType().equals("11")) {
										obj.put("photoCompoAvailable", "2");
									}
									obj.put("photoIds", dynmaicFieldConfig.getImageIds());
								} else {
									obj.put("photoCompoAvailable", "0");
									obj.put("photoByteStr", "");
								}
								fieldList.add(obj);
							} else {
								JSONObject obj = new JSONObject();
								obj.put("code", dynmaicFieldConfig.getFieldName());
								obj.put("name", dynmaicFieldConfig.getFieldValue() == null ? ""
										: dynmaicFieldConfig.getFieldValue());
								obj.put("refId", dynmaicFieldConfig.getParentId());
								obj.put("typez",
										dynmaicFieldConfig.getTypez()
												+ (dynmaicFieldConfig.getFarmerDynamicData() == null ? ""
														: "_" + dynmaicFieldConfig.getFarmerDynamicData().getId()));
								obj.put("componentType", dynmaicFieldConfig.getComponentType());
								if (dynmaicFieldConfig.getFieldValue() != null) {
									if (!StringUtil.isEmpty(dynmaicFieldConfig.getComponentType())
											&& Arrays.asList(
													String.valueOf(
															DynamicFieldConfig.COMPONENT_TYPES.DROP_DOWN.ordinal()),
													String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.RADIO.ordinal()),
													String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT
															.ordinal()),
													String.valueOf(
															DynamicFieldConfig.COMPONENT_TYPES.CHECKBOX.ordinal()))
													.contains(dynmaicFieldConfig.getComponentType())
											&& dynmaicFieldConfig.getAccessType() != null
											&& (dynmaicFieldConfig.getAccessType().equals(1)
													|| dynmaicFieldConfig.getAccessType().equals(2))) {
										if (DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal() == Integer
												.valueOf(dynmaicFieldConfig.getComponentType())) {
											obj.put("dispName",
													getCatlogueValueByCodeArray(dynmaicFieldConfig.getFieldValue()));
										} else {
											obj.put("dispName",
													getCatlogueValueByCode(dynmaicFieldConfig.getFieldValue())
															.getName());
										}
									} else if (!StringUtil.isEmpty(dynmaicFieldConfig.getComponentType())
											&& Arrays.asList(
													String.valueOf(
															DynamicFieldConfig.COMPONENT_TYPES.DROP_DOWN.ordinal()),
													String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.RADIO.ordinal()),
													String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT
															.ordinal()),
													String.valueOf(
															DynamicFieldConfig.COMPONENT_TYPES.CHECKBOX.ordinal()))
													.contains(dynmaicFieldConfig.getComponentType())
											&& dynmaicFieldConfig.getAccessType() != null
											&& dynmaicFieldConfig.getAccessType().equals(3)) {
										try {
											LIST_METHOD[] methods = DynamicFieldConfig.LIST_METHOD.values();
											Map<String, String> listValeus = getOptions(
													methods[Integer.valueOf(dynmaicFieldConfig.getListMethod())]
															.toString());

											if (DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal() == Integer
													.valueOf(dynmaicFieldConfig.getComponentType())
													&& dynmaicFieldConfig.getFieldValue().contains(",")) {

												obj.put("dispName", listValeus.entrySet().stream()
														.filter(u -> Arrays
																.asList(dynmaicFieldConfig.getFieldValue().split(","))
																.contains(u.getKey()))
														.map(u -> u.getValue()).collect(Collectors.joining(",")));
											} else {
												obj.put("dispName",
														listValeus.containsKey(dynmaicFieldConfig.getFieldValue())
																? listValeus.get(dynmaicFieldConfig.getFieldValue())
																: "");
											}

										} catch (Exception e) {
											obj.put("dispName", "");
										}
									} else {
										obj.put("dispName", dynmaicFieldConfig.getFieldValue());
									}
								}
								if (dynmaicFieldConfig.getImageIds() != null
										&& !StringUtil.isEmpty(dynmaicFieldConfig.getImageIds())) {
									obj.put("photoCompoAvailable", "1");
									if (dynmaicFieldConfig.getComponentType().equals("13")
											|| dynmaicFieldConfig.getComponentType().equals("11")) {
										obj.put("photoCompoAvailable", "2");
									}
									obj.put("photoIds", dynmaicFieldConfig.getImageIds());
								} else {
									obj.put("photoCompoAvailable", "0");
									obj.put("photoByteStr", "");
								}
								groupList.add(obj);
							}

						});// Date of Illness

				jsonMap.put("fields", fieldList);
				jsonMap.put("groups", groupList);

				JSONObject objects = new JSONObject();
				objects.putAll(jsonMap);

				printAjaxResponse(objects, "text/json");
			}
		}

	}

	String watherInfo = "";

	public String getFieldValue(String componentType, String accessType, String FieldVal, String listMethod) {
		String dispName = "";

		if (!StringUtil.isEmpty(componentType)
				&& Arrays
						.asList(String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.DROP_DOWN.ordinal()),
								String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.RADIO.ordinal()),
								String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal()),
								String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.CHECKBOX.ordinal()))
						.contains(componentType)
				&& accessType != null && (Arrays.asList(1, 2).contains(Integer.valueOf(accessType)))) {
			if (DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal() == Integer.valueOf(componentType)) {
				dispName = getCatlogueValueByCodeArray(FieldVal);
			} else {
				dispName = getCatlogueValueByCode(FieldVal).getName();
			}
		} else if (!StringUtil.isEmpty(componentType)
				&& Arrays
						.asList(String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.DROP_DOWN.ordinal()),
								String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.RADIO.ordinal()),
								String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal()),
								String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.CHECKBOX.ordinal()))
						.contains(componentType)
				&& accessType != null && Arrays.asList(3).contains(Integer.valueOf(accessType))) {
			try {
				LIST_METHOD[] methods = DynamicFieldConfig.LIST_METHOD.values();
				Map<String, String> listValeus = getOptions(methods[Integer.valueOf(listMethod)].toString());

				if (DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal() == Integer.valueOf(componentType)
						&& FieldVal.contains(",")) {

					dispName = listValeus.entrySet().stream()
							.filter(u -> Arrays.asList(FieldVal.split(",")).contains(u.getKey())).map(u -> u.getValue())
							.collect(Collectors.joining(","));
				} else {
					dispName = listValeus.containsKey(FieldVal) ? listValeus.get(FieldVal) : "";
				}

			} catch (Exception e) {
				dispName = "";
			}
		} else if (Integer.parseInt(componentType) == DynamicFieldConfig.COMPONENT_TYPES.WEATHER_INFO.ordinal()) {
			watherInfo = "";
			if (FieldVal != null && !StringUtil.isEmpty(FieldVal)) {
				String[] arr = FieldVal.split("\\|");
				AtomicInteger i = new AtomicInteger(0);
				Arrays.asList(getLocaleProperty("temp"), getLocaleProperty("rain"), getLocaleProperty("humidity"),
						getLocaleProperty("windSpeed")).stream().forEach(u -> {
							try {
								watherInfo += u + " : " + arr[i.getAndIncrement()].toString() + " \n ";
							} catch (ArrayIndexOutOfBoundsException e) {
								watherInfo += u + " : " + " \n ";
							}
						});
			}

			dispName = watherInfo;
		} else {
			dispName = FieldVal;
		}
		return dispName;
	}

	public void populateDynmaicFieldValuesByTxnId() {

		if (!StringUtil.isEmpty(getSelectedObject()) && !StringUtil.isEmpty(getTxnTypez())) {
			Map<String, List<JSONObject>> jsonMap = new LinkedHashMap<>();
			List<JSONObject> groupList = new LinkedList<>();
			List<JSONObject> fieldList = new LinkedList<>();
			FarmerDynamicData fyd = farmerService.findFarmerDynamicData(getSelectedObject());
			ESESystem preferences = preferncesService.findPrefernceById("1");
			if (fyd.getDymamicImageData() != null && fyd.getDymamicImageData().size() > 0) {
				fyd.setDigSignByteString(Base64Util.encoder(fyd.getDymamicImageData().iterator().next().getImage()));
			}
			List<DynamicFeildMenuConfig> dyList = farmerService.findDynamicMenusByType(fyd.getTxnType());
			List<DynamicFieldConfig> dynamicFieldsConfigList = (List<DynamicFieldConfig>) dyList.stream()
					.flatMap(listContainer -> listContainer.getDynamicFieldConfigs().stream())
					.collect(Collectors.toList()).stream().map(u -> u.getField()).collect(Collectors.toList());
			Map<String, DynamicFieldConfig> dynMap = dynamicFieldsConfigList.stream()
					.collect(Collectors.toMap(u -> u.getCode(), u -> u));
			fyd.getFarmerDynamicFieldsValues().stream().forEach(dynmaicFieldConfig -> {
				if (ObjectUtil.isEmpty(dynmaicFieldConfig.getParentId()) || dynmaicFieldConfig.getParentId() == 0) {
					JSONObject obj = new JSONObject();
					obj.put("code", dynmaicFieldConfig.getFieldName());
					obj.put("name",
							dynmaicFieldConfig.getFieldValue() == null ? "" : dynmaicFieldConfig.getFieldValue());
					obj.put("componentType", dynmaicFieldConfig.getComponentType());
					obj.put("score", dynmaicFieldConfig.getScore() != null ? dynmaicFieldConfig.getScore() : "");
					obj.put("percentage",
							dynmaicFieldConfig.getPercentage() != null ? dynmaicFieldConfig.getPercentage() : "");
					obj.put("grade", dynmaicFieldConfig.getGrade() != null ? dynmaicFieldConfig.getGrade() : "");
					obj.put("isAct", dynmaicFieldConfig.getFollowUp());
					if (dynmaicFieldConfig.getFollowUp() == 3) {
						if (dynmaicFieldConfig.getFollowUps() != null
								&& !ObjectUtil.isEmpty(dynmaicFieldConfig.getFollowUps())) {
							JSONArray jsonArray = new JSONArray();
							dynmaicFieldConfig.getFollowUps().stream().forEach(dyField -> {
								JSONObject jsonObject = new JSONObject();
								String dataVal = getFieldValue(dyField.getComponentType(),
										String.valueOf(dyField.getAccessType()), dyField.getFieldValue(),
										dyField.getListMethod());
								jsonObject.put("qusCode", dyField.getFieldName());
								jsonObject.put("fieldName", dynMap.get(dyField.getFieldName()).getComponentName());
								jsonObject.put("fieldVal", dataVal);
								if (dyField.getDymamicImageData() != null
										&& !ObjectUtil.isListEmpty(dyField.getDymamicImageData())) {
									dyField.getDymamicImageData().stream().forEach(im -> {
										if (im != null && !ObjectUtil.isEmpty(im)) {
											jsonObject.put("img", Base64Util.encoder(im.getImage()));
										}
									});
								} else {
									jsonObject.put("img", null);
								}
								jsonArray.add(jsonObject);
							});
							String json = jsonArray.toString();
							obj.put("subAns", json);
						}

					}
					if (dynmaicFieldConfig.getActionPlan() != null) {
						obj.put("actPlan", dynmaicFieldConfig.getActionPlan().getFieldValue());

					}
					obj.put("actStatus", dynmaicFieldConfig.getActStatus() != null
							? getLocaleProperty("actStatus" + dynmaicFieldConfig.getActStatus()) : "");
					if (dynmaicFieldConfig.getDeadline() != null) {
						obj.put("deadline", dynmaicFieldConfig.getDeadline().getFieldValue());

					}
					if (dynmaicFieldConfig.getFieldValue() != null) {
						if (!StringUtil.isEmpty(dynmaicFieldConfig.getComponentType())
								&& Arrays
										.asList(String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.DROP_DOWN.ordinal()),
												String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.RADIO.ordinal()),
												String.valueOf(
														DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal()),
												String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.CHECKBOX.ordinal()))
										.contains(dynmaicFieldConfig.getComponentType())
								&& dynmaicFieldConfig.getAccessType() != null
								&& (dynmaicFieldConfig.getAccessType().equals(1)
										|| dynmaicFieldConfig.getAccessType().equals(2))) {
							if (DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal() == Integer
									.valueOf(dynmaicFieldConfig.getComponentType())) {
								obj.put("dispName", getCatlogueValueByCodeArray(dynmaicFieldConfig.getFieldValue()));
							} else {
								obj.put("dispName",
										getCatlogueValueByCode(dynmaicFieldConfig.getFieldValue()).getName());
							}
						} else if (!StringUtil.isEmpty(dynmaicFieldConfig.getComponentType())
								&& Arrays
										.asList(String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.DROP_DOWN.ordinal()),
												String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.RADIO.ordinal()),
												String.valueOf(
														DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal()),
												String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.CHECKBOX.ordinal()))
										.contains(dynmaicFieldConfig.getComponentType())
								&& dynmaicFieldConfig.getAccessType() != null
								&& dynmaicFieldConfig.getAccessType().equals(3)) {
							try {
								LIST_METHOD[] methods = DynamicFieldConfig.LIST_METHOD.values();
								Map<String, String> listValeus = getOptions(
										methods[Integer.valueOf(dynmaicFieldConfig.getListMethod())].toString());

								if (DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal() == Integer
										.valueOf(dynmaicFieldConfig.getComponentType())
										&& dynmaicFieldConfig.getFieldValue().contains(",")) {

									obj.put("dispName", listValeus.entrySet().stream()
											.filter(u -> Arrays.asList(dynmaicFieldConfig.getFieldValue().split(","))
													.contains(u.getKey()))
											.map(u -> u.getValue()).collect(Collectors.joining(",")));
								} else {
									obj.put("dispName", listValeus.containsKey(dynmaicFieldConfig.getFieldValue())
											? listValeus.get(dynmaicFieldConfig.getFieldValue()) : "");
								}

							} catch (Exception e) {
								obj.put("dispName", "");
							}
						} else if (Integer.parseInt(dynmaicFieldConfig
								.getComponentType()) == DynamicFieldConfig.COMPONENT_TYPES.WEATHER_INFO.ordinal()) {
							watherInfo = "";
							if (dynmaicFieldConfig.getFieldValue() != null
									&& !StringUtil.isEmpty(dynmaicFieldConfig.getFieldValue())) {
								String[] arr = dynmaicFieldConfig.getFieldValue().split("\\|");
								AtomicInteger i = new AtomicInteger(0);
								Arrays.asList(getLocaleProperty("temp"), getLocaleProperty("rain"),
										getLocaleProperty("humidity"), getLocaleProperty("windSpeed")).stream()
										.forEach(u -> {
											try {
												watherInfo += u + " : " + arr[i.getAndIncrement()].toString() + " \n ";
											} catch (ArrayIndexOutOfBoundsException e) {
												watherInfo += u + " : " + " \n ";
											}
										});
							}

							obj.put("dispName", watherInfo);
						} else {
							obj.put("dispName", dynmaicFieldConfig.getFieldValue());
						}
					}
					if (dynmaicFieldConfig.getImageIds() != null
							&& !StringUtil.isEmpty(dynmaicFieldConfig.getImageIds())) {
						obj.put("photoCompoAvailable", "1");
						if (dynmaicFieldConfig.getComponentType().equals("13")
								|| dynmaicFieldConfig.getComponentType().equals("11")) {
							obj.put("photoCompoAvailable", "2");
						}
						obj.put("photoIds", dynmaicFieldConfig.getImageIds());
					} else {
						obj.put("photoCompoAvailable", "0");
						obj.put("photoByteStr", "");
					}
					fieldList.add(obj);
				} else {
					JSONObject obj = new JSONObject();
					obj.put("code", dynmaicFieldConfig.getFieldName());
					obj.put("name",
							dynmaicFieldConfig.getFieldValue() == null ? "" : dynmaicFieldConfig.getFieldValue());
					obj.put("refId", dynmaicFieldConfig.getParentId());
					obj.put("typez", dynmaicFieldConfig.getTypez() + (dynmaicFieldConfig.getFarmerDynamicData() == null
							? "" : "_" + dynmaicFieldConfig.getFarmerDynamicData().getId()));
					obj.put("componentType", dynmaicFieldConfig.getComponentType());
					obj.put("score", dynmaicFieldConfig.getScore() != null ? dynmaicFieldConfig.getScore() : "");
					obj.put("percentage",
							dynmaicFieldConfig.getPercentage() != null ? dynmaicFieldConfig.getPercentage() : "");
					obj.put("grade", dynmaicFieldConfig.getGrade() != null ? dynmaicFieldConfig.getGrade() : "");
					obj.put("isAct", dynmaicFieldConfig.getFollowUp());
					if (dynmaicFieldConfig.getActionPlan() != null) {
						obj.put("actPlan", dynmaicFieldConfig.getActionPlan().getFieldValue());

					}

					obj.put("actStatus", dynmaicFieldConfig.getActStatus() != null
							? getLocaleProperty("actStatus" + dynmaicFieldConfig.getActStatus()) : "");
					if (dynmaicFieldConfig.getDeadline() != null) {
						obj.put("deadline", dynmaicFieldConfig.getDeadline().getFieldValue());

					}

					if (dynmaicFieldConfig.getFieldValue() != null) {
						if (!StringUtil.isEmpty(dynmaicFieldConfig.getComponentType())
								&& Arrays
										.asList(String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.DROP_DOWN.ordinal()),
												String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.RADIO.ordinal()),
												String.valueOf(
														DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal()),
												String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.CHECKBOX.ordinal()))
										.contains(dynmaicFieldConfig.getComponentType())
								&& dynmaicFieldConfig.getAccessType() != null
								&& (dynmaicFieldConfig.getAccessType().equals(1)
										|| dynmaicFieldConfig.getAccessType().equals(2))) {
							if (DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal() == Integer
									.valueOf(dynmaicFieldConfig.getComponentType())) {
								obj.put("dispName", getCatlogueValueByCodeArray(dynmaicFieldConfig.getFieldValue()));
							} else {
								obj.put("dispName",
										getCatlogueValueByCode(dynmaicFieldConfig.getFieldValue()).getName());
							}
						} else if (!StringUtil.isEmpty(dynmaicFieldConfig.getComponentType())
								&& Arrays
										.asList(String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.DROP_DOWN.ordinal()),
												String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.RADIO.ordinal()),
												String.valueOf(
														DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal()),
												String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.CHECKBOX.ordinal()))
										.contains(dynmaicFieldConfig.getComponentType())
								&& dynmaicFieldConfig.getAccessType() != null
								&& dynmaicFieldConfig.getAccessType().equals(3)) {
							try {
								LIST_METHOD[] methods = DynamicFieldConfig.LIST_METHOD.values();
								Map<String, String> listValeus = getOptions(
										methods[Integer.valueOf(dynmaicFieldConfig.getListMethod())].toString());

								if (DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal() == Integer
										.valueOf(dynmaicFieldConfig.getComponentType())
										&& dynmaicFieldConfig.getFieldValue().contains(",")) {

									obj.put("dispName", listValeus.entrySet().stream()
											.filter(u -> Arrays.asList(dynmaicFieldConfig.getFieldValue().split(","))
													.contains(u.getKey()))
											.map(u -> u.getValue()).collect(Collectors.joining(",")));
								} else {
									obj.put("dispName", listValeus.containsKey(dynmaicFieldConfig.getFieldValue())
											? listValeus.get(dynmaicFieldConfig.getFieldValue()) : "");
								}

							} catch (Exception e) {
								obj.put("dispName", "");
							}
						} else {
							obj.put("dispName", dynmaicFieldConfig.getFieldValue());
						}
					}
					if (dynmaicFieldConfig.getImageIds() != null
							&& !StringUtil.isEmpty(dynmaicFieldConfig.getImageIds())) {
						obj.put("photoCompoAvailable", "1");
						if (dynmaicFieldConfig.getComponentType().equals("13")
								|| dynmaicFieldConfig.getComponentType().equals("11")) {
							obj.put("photoCompoAvailable", "2");
						}
						obj.put("photoIds", dynmaicFieldConfig.getImageIds());
					} else {
						obj.put("photoCompoAvailable", "0");
						obj.put("photoByteStr", "");
					}
					groupList.add(obj);
				}
			});// Date of Illnessbse

			jsonMap.put("fields", fieldList);
			jsonMap.put("groups", groupList);

			JSONObject objects = new JSONObject();
			objects.putAll(jsonMap);

			printAjaxResponse(objects, "text/json");

		}

	}

	public String getSelectedObject() {
		return selectedObject;
	}

	public void setSelectedObject(String selectedObject) {
		this.selectedObject = selectedObject;
	}

	public String getTxnTypez() {
		return txnTypez;
	}

	public void setTxnTypez(String txnTypez) {
		this.txnTypez = txnTypez;
	}

	public String getDynamicFieldsArray() {
		return dynamicFieldsArray;
	}

	public void setDynamicFieldsArray(String dynamicFieldsArray) {
		this.dynamicFieldsArray = dynamicFieldsArray;
	}

	public String getDynamicListArray() {
		return dynamicListArray;
	}

	public void setDynamicListArray(String dynamicListArray) {
		this.dynamicListArray = dynamicListArray;
	}

	protected void setDynamicFieldValues(Long id) {
		if (!StringUtil.isEmpty(dynamicFieldsArray)) {
			try {
				List<FarmerDynamicFieldsValue> farmerDynamicFieldValuesList = new ArrayList<>();
				Type listType1 = new TypeToken<List<DynamicData>>() {
				}.getType();
				List<DynamicData> dynamicList = new Gson().fromJson(dynamicFieldsArray, listType1);
				for (DynamicData dynamicData : dynamicList) {

					if (!StringUtil.isEmpty(dynamicData.getValue())) {
						FarmerDynamicFieldsValue dynamicFieldsValue = new FarmerDynamicFieldsValue();
						dynamicFieldsValue.setFieldName(dynamicData.getName());
						dynamicFieldsValue.setFieldValue(dynamicData.getValue());
						dynamicFieldsValue.setTypez(Integer.parseInt(dynamicData.getTypez()));
						dynamicFieldsValue.setReferenceId(String.valueOf(id));
						dynamicFieldsValue.setTxnType(dynamicData.getTxnTypez());
						dynamicFieldsValue.setCreatedDate(new Date());
						dynamicFieldsValue.setCreatedUser(getUsername());
						farmerDynamicFieldValuesList.add(dynamicFieldsValue);
					}
				}
				clientService.saveFarmerDynmaicList(farmerDynamicFieldValuesList);
				farmerService.updateDynamicFarmerFieldComponentType();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void setUpdateDynamicFieldValues(Long id) {
		if (!StringUtil.isEmpty(dynamicFieldsArray)) {
			try {
				List<FarmerDynamicFieldsValue> fdfvList = farmerService
						.listFarmerDynamicFieldsValuePhotoByRefTxnType(String.valueOf(id), getTxnTypez());
				List<FarmerDynamicFieldsValue> farmerDynamicFieldValuesList = new ArrayList<>();
				Type listType1 = new TypeToken<List<DynamicData>>() {
				}.getType();
				List<DynamicData> dynamicList = new Gson().fromJson(dynamicFieldsArray, listType1);
				for (DynamicData dynamicData : dynamicList) {

					if (!StringUtil.isEmpty(dynamicData.getValue())) {
						FarmerDynamicFieldsValue dynamicFieldsValue = new FarmerDynamicFieldsValue();
						dynamicFieldsValue.setFieldName(dynamicData.getName());
						dynamicFieldsValue.setFieldValue(dynamicData.getValue());
						dynamicFieldsValue.setTypez(Integer.parseInt(dynamicData.getTypez()));
						dynamicFieldsValue.setReferenceId(String.valueOf(id));
						dynamicFieldsValue.setTxnType(dynamicData.getTxnTypez());

						dynamicFieldsValue.setCreatedDate(new Date());
						dynamicFieldsValue.setCreatedUser(getUsername());
						farmerDynamicFieldValuesList.add(dynamicFieldsValue);
					}
				}
				clientService.updateFarmerDynmaicList(farmerDynamicFieldValuesList, id);
				farmerService.updateDynamicFarmerFieldComponentType();
				if (!ObjectUtil.isListEmpty(fdfvList)) {
					fdfvList.stream().forEach(fdfv -> {
						fdfv.setId(0L);
						Set<DynamicImageData> imageDataSet = fdfv.getDymamicImageData();
						fdfv.setDymamicImageData(null);
						farmerService.save(fdfv);
						imageDataSet.stream().forEach(dynamicImage -> {
							dynamicImage.setId(0L);
							dynamicImage.setFarmerDynamicFieldsValue(fdfv);
							farmerService.save(dynamicImage);
							imageDataSet.add(dynamicImage);
						});
						fdfv.setDymamicImageData(imageDataSet);
						farmerService.update(fdfv);
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String getMenuLabel(Object[] ssmenuInfo) {
		String label = "";
		if (menuMap.size() <= 0) {
			buildDynamicMenus();
		}
		if (ssmenuInfo[2].toString().contains("txnType") && ssmenuInfo[2].toString().split("txnType=").length > 1) {
			String txnType = ssmenuInfo[2].toString().split("txnType=")[1].toString();
			if (menuMap.containsKey(txnType) && menuMap.get(txnType).length > 1) {
				// 0-MenuName,1-Entity,2 - isSeason,3 - isSingleRecord,4 -
				// mTxnType,5 - iconClass,6 - agentType
				Object[] menuDet = menuMap.get(txnType);
				label = menuDet[0].toString();
			} else {
				label = getLocaleProperty(ssmenuInfo[1] + "", getLoggedInUserLanguage(), SwitchAction.class);
			}
		} else {
			label = getLocaleProperty(ssmenuInfo[1] + "", getLoggedInUserLanguage(), SwitchAction.class);
		}
		return label;
	}

	public String getDynamicViewReportLabel(Object[] ssmenuInfo) {
		String label = "";

		if (ssmenuInfo[2].toString().contains("id") && ssmenuInfo[2].toString().split("id=").length > 1) {
			String txnType = ssmenuInfo[2].toString().split("id=")[1].toString();
			if (txnType != null && !StringUtil.isEmpty(txnType)) {
				DynamicReportConfig dynamicReportConfig = clientService.findReportById(txnType);

				label = getLocaleProperty(dynamicReportConfig.getReport());
			} else {
				label = getLocaleProperty(ssmenuInfo[1] + "", getLoggedInUserLanguage(), SwitchAction.class);
			}
		} else {
			label = getLocaleProperty(ssmenuInfo[1] + "", getLoggedInUserLanguage(), SwitchAction.class);
		}
		return label;
	}

	public void reLocalizeMenu() {

		if (!ObjectUtil.isEmpty(request) && !ObjectUtil.isEmpty(request.getSession())
				&& (ObjectUtil.isEmpty(request.getSession().getAttribute("menuLocalize"))
						|| !request.getSession().getAttribute("menuLocalize").equals("done"))) {
			List<Menu> userMenus = (List<Menu>) request.getSession().getAttribute(ISecurityFilter.MENU);
			if (userMenus != null) {
				String language = (String) request.getSession().getAttribute(ISecurityFilter.LANGUAGE);
				for (Menu userMenu : userMenus) {
					Object[] menuInfo = userService.findMenuInfo(userMenu.getId());
					userMenu.setMenuClassName(menuInfo[1] + "");
					if (menuInfo[1].toString().contains("service.dynamicCertification")
							|| menuInfo[1].toString().contains("report.dynmaicCertification")) {
						userMenu.setLabel(getMenuLabel(menuInfo));
					} else if (menuInfo[2].toString().contains("dynamicViewReport")) {
						userMenu.setLabel(getDynamicViewReportLabel(menuInfo));
					} else {
						userMenu.setLabel(getLocaleProperty(menuInfo[1].toString()));
					}
					// userMenu.setLabel(getLocaleProperty(menuInfo[1]));
					Set<Menu> subMenus = userMenu.getSubMenus();

					for (Menu smenu : subMenus) {
						Object[] smenuInfo = userService.findMenuInfo(smenu.getId());
						smenu.setMenuClassName(smenuInfo[1] + "");
						if (smenuInfo[1].toString().contains("service.dynamicCertification")
								|| smenuInfo[1].toString().contains("report.dynmaicCertification")) {
							smenu.setLabel(getMenuLabel(smenuInfo));
						} else if (smenuInfo[2].toString().contains("dynamicViewReport")) {
							smenu.setLabel(getDynamicViewReportLabel(smenuInfo));
						} else {
							smenu.setLabel(getLocaleProperty(smenuInfo[1].toString()));
						}
						// smenu.setLabel(getLocaleProperty(smenuInfo[1]));
						Set<Menu> subSubMenus = smenu.getSubMenus();

						for (Menu ssmenu : subSubMenus) {
							Object[] ssmenuInfo = userService.findMenuInfo(ssmenu.getId());
							System.out.println(ssmenu.getId());
							try {
								ssmenu.setMenuClassName(ssmenuInfo[1] + "");
							} catch (NullPointerException e) {
								System.out.println("dd");
							}
							if (ssmenuInfo[1].toString().contains("service.dynamicCertification")
									|| ssmenuInfo[1].toString().contains("report.dynmaicCertification")) {
								ssmenu.setLabel(getMenuLabel(ssmenuInfo));
							} else if (ssmenuInfo[2].toString().contains("dynamicViewReport")) {
								ssmenu.setLabel(getDynamicViewReportLabel(ssmenuInfo));
							} else {
								ssmenu.setLabel(getLocaleProperty(ssmenuInfo[1].toString()));
							}
						}
					}
				}
			}

			request.getSession().setAttribute("menuLocalize", "done");
			request.getSession().removeAttribute("localize");
		}
	}

	public String getFormulaEquation() {
		return formulaEquation;
	}

	public void setFormulaEquation(String formulaEquation) {
		this.formulaEquation = formulaEquation;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getImgId() {
		return imgId;
	}

	public void setImgId(String imgId) {
		this.imgId = imgId;
	}

	public String populateVideoPlay() {

		try {

			DynamicImageData pmtImg = null;
			if (!StringUtil.isEmpty(imgId) && StringUtil.isLong(imgId))
				pmtImg = farmerService.findDynamicImageDataById(Long.valueOf(imgId));

			if (ObjectUtil.isEmpty(pmtImg)) {
				return REDIRECT;
			}
			String fileNameStr = "";
			if (pmtImg.getFarmerDynamicFieldsValue().getFarmerDynamicData().getEntityId().equals("1")) {
				Object[] farmer = farmerService.findFarmerInfoById(
						Long.valueOf(pmtImg.getFarmerDynamicFieldsValue().getFarmerDynamicData().getReferenceId()));
				fileNameStr = farmer != null && farmer.length > 0 ? farmer[1].toString() : "";
			} else if (pmtImg.getFarmerDynamicFieldsValue().getFarmerDynamicData().getEntityId().equals("2")
					|| pmtImg.getFarmerDynamicFieldsValue().getFarmerDynamicData().getEntityId().equals("4")) {
				Object[] farme = farmerService.findFarmInfoById(
						Long.valueOf(pmtImg.getFarmerDynamicFieldsValue().getFarmerDynamicData().getReferenceId()));
				fileNameStr = farme != null && farme.length > 0 ? farme[1].toString() : "";
			} else if (pmtImg.getFarmerDynamicFieldsValue().getFarmerDynamicData().getEntityId().equals("3")) {
				Warehouse w = locationService.findSamithiById(
						Long.valueOf(pmtImg.getFarmerDynamicFieldsValue().getFarmerDynamicData().getReferenceId()));
				fileNameStr = w != null ? w.getName() : "";
			}
			String fileExt = ".mp4";
			if (pmtImg.getFileExt() != null && !StringUtil.isEmpty(pmtImg.getFileExt())) {
				fileExt = "." + pmtImg.getFileExt();
			}
			response.setContentType("video/mp4");
			response.setContentLength(pmtImg.getImage().length);
			response.setHeader("Content-Disposition", "attachment;filename=" + fileNameStr + "_"
					+ eseExportFileFormat.format(pmtImg.getFarmerDynamicFieldsValue().getFarmerDynamicData().getDate())
					+ fileExt);
			response.getOutputStream().write(pmtImg.getImage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String populateImageDynamic() {

		try {
			// setImgId(imgId);
			DynamicImageData pmtImg = null;
			if (!StringUtil.isEmpty(imgId) && StringUtil.isLong(imgId))
				pmtImg = farmerService.findDynamicImageDataById(Long.valueOf(imgId));
			byte[] image = null;
			if (!ObjectUtil.isEmpty(pmtImg) && pmtImg.getImage() != null) {
				image = pmtImg.getImage();
			}
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

	public void populateDynamicImage() {

		try {
			// setImgId(imgId);
			DynamicImageData pmtImg = null;
			if (!StringUtil.isEmpty(imgId) && StringUtil.isLong(imgId))
				pmtImg = farmerService.findDynamicImageDataById(Long.valueOf(imgId));
			byte[] image = null;
			JSONObject json = new JSONObject();
			if (!ObjectUtil.isEmpty(pmtImg) && pmtImg.getImage() != null) {
				image = pmtImg.getImage();
				json.put("img", "data:image/png;base64,"+Base64Util.encoder(image));
			}else{
				json.put("img", "");
			}
			
			
			printAjaxResponse(json, "text/html");
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

	
	public Map<String, Object[]> buildDynamicMenus() {
		if (menuMap == null || menuMap.isEmpty()) {
			List<DynamicFeildMenuConfig> lpList = farmerService.listDynamicMenus();
			for (DynamicFeildMenuConfig lp : lpList) {
				menuMap.put(lp.getTxnType(),
						new Object[] { lp.getLangName(getLoggedInUserLanguage()), lp.getEntity(), lp.getIsSeason(),
								lp.getIsSingleRecord(), lp.getmTxnType(), lp.getIconClass(), lp.getAgentType() });
			}
		}
		return menuMap;

	}

	public Map<String, Object[]> getMenuMap() {
		return menuMap;
	}

	public void setMenuMap(Map<String, Object[]> menuMap) {
		this.menuMap = menuMap;
	}

	public String getIsKpfBased() {

		String result = null;
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			result = preferences.getPreferences().get(ESESystem.IS_KPF_BASED);
		}

		return !StringUtil.isEmpty(result) ? result : ESESystem.IS_KPF_BASED;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected String sendDataTableJSONResponse(Map data) throws Exception {

		JSONObject gridData = new JSONObject();

		gridData.put("draw", "1");
		gridData.put("recordsTotal", totalRecords);
		gridData.put("recordsFiltered", totalRecords);
		List list = (List) data.get(ROWS);
		JSONArray rows = new JSONArray();
		if (list != null) {
			branchIdValue = getBranchId();
			if (StringUtil.isEmpty(branchIdValue)) {
				buildBranchMap();
			}
			for (Object record : list) {
				rows.add(toDataTableJSON(record));
			}
		}
		gridData.put("data", rows);
		printAjaxResponse(gridData, "text/html");

		return null;
	}

	protected JSONArray toDataTableJSON(Object record) {

		return null;
	}

	public Map<String, String> getStatesDynamic() {
		return locationService.listStates().stream().filter(u -> u[0] != null)
				.collect(Collectors.toMap(u -> u[0].toString(), u -> u[1].toString()));

	}

	public Map<String, String> getDistrictsDynamic() {
		return locationService.listLocalities().stream().filter(u -> u[0] != null)
				.collect(Collectors.toMap(u -> u[0].toString(), u -> u[1].toString()));

	}

	public Map<String, String> getTaluksDynamic() {
		return locationService.listMunicipality().stream().filter(u -> u != null && u.getCode() != null)
				.collect(Collectors.toMap(u -> u.getCode().toString(), u -> u.getName()));

	}

	public Map<String, String> getGpsDynamic() {
		return locationService.listGramPanchayatIdCodeName().stream().filter(u -> u[1] != null)
				.collect(Collectors.toMap(u -> u[1].toString(), u -> u[2].toString()));

	}

	public Map<String, String> getVillagesDynamic() {
		return locationService.listVillageIdAndName().stream().filter(u -> u[1] != null)
				.collect(Collectors.toMap(u -> u[1].toString(), u -> u[2].toString()));

	}

	public Map<String, String> getWarehouseDynamic() {
		return locationService.listWarehouseIdAndName().stream().filter(u -> u[1] != null)
				.collect(Collectors.toMap(u -> u[0].toString(), u -> u[2].toString()));

	}

	public Map<String, String> getColdStorageNameDynamic() {
		return farmerService.listColdStorageNameDynamic().stream().filter(u -> u[1] != null)
				.collect(Collectors.toMap(u -> u[0].toString(), u -> u[1].toString()));

	}

	public Map<String, String> getGroupsDynamic() {
		return farmerService.listSamithiName().stream().filter(u -> u != null && u.getCode() != null)
				.collect(Collectors.toMap(u -> u.getCode().toString(), u -> u.getName()));

	}

	public Map<String, String> getFarmersDynamic() {
		return farmerService.listFarmerIDAndName().stream().filter(u -> u[1] != null).collect(
				Collectors.toMap(u -> u[1].toString(), u -> (u[2].toString() + (u[3] == null ? "" : u[3].toString()))));

	}

	public Map<String, String> getFarmsDynamic() {
		return farmerService.listFarmIDAndName().stream().filter(u -> u[1] != null)
				.collect(Collectors.toMap(u -> u[1].toString(), u -> u[2].toString()));

	}

	public Map<String, String> getCropsDynamic() {
		return productDistributionService.listProcurementProduct().stream()
				.filter(u -> u != null && u.getCode() != null)
				.collect(Collectors.toMap(u -> u.getCode().toString(), u -> u.getName()));
	}

	public Map<String, String> getManufacturerDynamic() {
		return productService.listProducts().stream().filter(u -> u[3] != null && u[4] != null)
				.collect(Collectors.toMap(u -> String.valueOf(u[3]), u -> String.valueOf(u[4])));

	}

	public Map<String, String> getFarmProductDynamic() {
		return productService.listProducts().stream().filter(u -> u[3] != null && u[0] != null)
				.collect(Collectors.toMap(u -> String.valueOf(u[3]), u -> String.valueOf(u[0])));

	}

	public Map<String, String> getBuyerDynamic() {
		return clientService.listCustomerIdAndName().stream().filter(u -> u[1] != null && u[0] != null)
				.collect(Collectors.toMap(u -> String.valueOf(u[0]), u -> String.valueOf(u[1])));

	}

	public Map<String, String> getBuyerList() {
		Map<String, String> listOfBuyers = new HashMap<String, String>();
		List<Customer> customers = clientService.listOfCustomers();
		for (Customer customer : customers) {
			listOfBuyers.put(customer.getCustomerId(), customer.getCustomerName());
		}
		return listOfBuyers;
	}

	public Map<Long, String> getBuyersList() {
		Map<Long, String> listOfBuyers = new HashMap<Long, String>();
		List<Customer> customers = clientService.listOfCustomers();
		for (Customer customer : customers) {
			listOfBuyers.put(customer.getId(), customer.getCustomerName());
		}
		return listOfBuyers;
	}

	public Map<String, String> getBuyersNameList() {
		Map<String, String> listOfBuyers = new HashMap<String, String>();
		List<Customer> customers = clientService.listOfCustomers();
		for (Customer customer : customers) {
			listOfBuyers.put(customer.getCustomerName(), customer.getCustomerName());
		}
		return listOfBuyers;
	}
	@SuppressWarnings("unchecked")
	public Map<String, String> getOptions(String methodName) {

		Map<String, String> returnMap = new LinkedHashMap<String, String>();
		try {
			Method method = this.getClass().getMethod(methodName);
			Object returnObj = method.invoke(this);
			if (!ObjectUtil.isEmpty(returnObj)) {
				returnMap = (Map<String, String>) returnObj;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnMap;
	}

	public void localizeMenu() {
		if (!ObjectUtil.isEmpty(request) && !ObjectUtil.isEmpty(request.getSession())
				&& (ObjectUtil.isEmpty(request.getSession().getAttribute("menuLocalize"))
						|| !request.getSession().getAttribute("menuLocalize").equals("done"))) {
			List<Menu> userMenus = (List<Menu>) request.getSession().getAttribute(ISecurityFilter.MENU);
			if (userMenus != null && request.getSession().getAttribute("localize") != null) {
				String language = (String) request.getSession().getAttribute(ISecurityFilter.LANGUAGE);
				for (Menu userMenu : userMenus) {
					Object[] menuInfo = userService.findMenuInfo(userMenu.getId());
					userMenu.setMenuClassName(menuInfo[1] + "");
					if (menuInfo[1].toString().contains("service.dynamicCertification")
							|| menuInfo[1].toString().contains("report.dynmaicCertification")) {
						userMenu.setLabel(getMenuLabel(menuInfo));
					} else if (menuInfo[2].toString().contains("dynamicViewReport")) {
						userMenu.setLabel(getDynamicViewReportLabel(menuInfo));
					} else {
						userMenu.setLabel(getLocaleProperty(menuInfo[1].toString()));
					}
					// userMenu.setLabel(getLocaleProperty(menuInfo[1]));
					Set<Menu> subMenus = userMenu.getSubMenus();

					for (Menu smenu : subMenus) {
						Object[] smenuInfo = userService.findMenuInfo(smenu.getId());
						smenu.setMenuClassName(smenuInfo[1] + "");
						if (smenuInfo[1].toString().contains("service.dynamicCertification")
								|| smenuInfo[1].toString().contains("report.dynmaicCertification")) {
							smenu.setLabel(getMenuLabel(smenuInfo));
						} else if (smenuInfo[2].toString().contains("dynamicViewReport")) {
							smenu.setLabel(getDynamicViewReportLabel(smenuInfo));
						} else {
							smenu.setLabel(getLocaleProperty(smenuInfo[1].toString()));
						}
						// smenu.setLabel(getLocaleProperty(smenuInfo[1]));
						Set<Menu> subSubMenus = smenu.getSubMenus();

						for (Menu ssmenu : subSubMenus) {
							Object[] ssmenuInfo = userService.findMenuInfo(ssmenu.getId());
							System.out.println(ssmenu.getId());
							try {
								ssmenu.setMenuClassName(ssmenuInfo[1] + "");
							} catch (NullPointerException e) {
								System.out.println("dd");
							}
							if (ssmenuInfo[1].toString().contains("service.dynamicCertification")
									|| ssmenuInfo[1].toString().contains("report.dynmaicCertification")) {
								ssmenu.setLabel(getMenuLabel(ssmenuInfo));
							} else if (ssmenuInfo[2].toString().contains("dynamicViewReport")) {
								ssmenu.setLabel(getDynamicViewReportLabel(ssmenuInfo));
							} else {
								ssmenu.setLabel(getLocaleProperty(ssmenuInfo[1].toString()));
							}
						}
					}
				}

				request.getSession().setAttribute("menuLocalize", "done");
				request.getSession().removeAttribute("localize");
			}
		}
	}

	public String getPostdata() {
		return postdata;
	}

	public void setPostdata(String postdata) {
		this.postdata = postdata;
	}

	public List<FarmCatalogue> getFarmCatalogueList() {
		return farmCatalogueList;
	}

	public void setFarmCatalogueList(List<FarmCatalogue> farmCatalogueList) {
		this.farmCatalogueList = farmCatalogueList;
	}

	public String getPostdataReport() {
		return postdataReport;
	}

	public void setPostdataReport(String postdataReport) {
		this.postdataReport = postdataReport;
	}

	public String getFilterMapReport() {
		return filterMapReport;
	}

	public void setFilterMapReport(String filterMapReport) {
		this.filterMapReport = filterMapReport;
	}

	public void saveDynamicField(String txnType, String referenceId, String seasonCode, String entityType) {
		if (!StringUtil.isEmpty(dynamicFieldsArray) && dynamicFieldsArray.length() > 0) {
			List<DynamicFeildMenuConfig> dm = farmerService.findDynamicMenusByType(txnType);
			DynamicFeildMenuConfig dmm = farmerService.findDynamicMenusByMType(txnType).get(0);
			FarmerDynamicData farmerDynamicData = new FarmerDynamicData();
			if (dmm.getIsScore() != null && (dmm.getIsScore() == 1 || dmm.getIsScore() == 2 || dmm.getIsScore() == 3)) {
				farmerDynamicData.setIsScore(dmm.getIsScore());
				farmerDynamicData.setScoreValue(new HashMap<>());
				farmerDynamicData.setActStatus(0);
			}
			dm.stream().flatMap(listContainer -> listContainer.getDynamicFieldConfigs().stream())
					.collect(Collectors.toList()).stream().forEach(section -> {
						section.getField().setfOrder(section.getOrder());
						fieldConfigMap.put(section.getField().getCode(), section.getField());
						if (section.getDynamicFieldScoreMap() != null && !section.getDynamicFieldScoreMap().isEmpty()) {
							farmerDynamicData.getScoreValue().put(section.getField().getCode(),
									section.getDynamicFieldScoreMap().stream()
											.collect(Collectors.toMap(DynamicFieldScoreMap::getCatalogueCode,
													u -> String.valueOf(String.valueOf(u.getScore()) + "~"
															+ String.valueOf(u.getPercentage())))));
						}

					});
			farmerDynamicData.setFarmerDynamicFieldsValues(addFarmerDynamicFieldsSet(referenceId));
			// farmerDynamicData.setTxnUniqueId(Long.valueOf(head.getMsgNo()));
			farmerDynamicData.setReferenceId(String.valueOf(referenceId));
			farmerDynamicData.setTxnType(txnType);
			farmerDynamicData.setDate(new Date(0));
			farmerDynamicData.setCreatedDate(new Date(0));
			farmerDynamicData.setCreatedUser(getUsername());
			farmerDynamicData.setStatus("0");
			farmerDynamicData.setBranch(getBranchId());
			farmerDynamicData.setEntityId(entityType);
			farmerDynamicData.setSeason(seasonCode);
			
			/* farmerService.saveOrUpdate(farmerDynamicData); */
			farmerService.saveOrUpdate(farmerDynamicData, new HashMap<>(), fieldConfigMap);
			farmerService.deleteChildObjects(farmerDynamicData.getTxnType());

		}
	}

	protected Set<FarmerDynamicFieldsValue> addFarmerDynamicFieldsSet(String id) {
		Set<FarmerDynamicFieldsValue> dynamicFieldsValuesSet = new HashSet<>();

		if (!StringUtil.isEmpty(dynamicFieldsArray)) {
			try {
				Type listType1 = new TypeToken<List<DynamicData>>() {
				}.getType();
				List<DynamicData> dynamicList = new Gson().fromJson(dynamicFieldsArray, listType1);
				for (DynamicData dynamicData : dynamicList) {
					if (!StringUtil.isEmpty(dynamicData.getValue())
							|| !StringUtil.isEmpty(dynamicData.getImageFile())) {
						FarmerDynamicFieldsValue dynamicFieldsValue = new FarmerDynamicFieldsValue();
						dynamicFieldsValue.setFieldName(dynamicData.getName());
						dynamicFieldsValue.setFieldValue(dynamicData.getValue());
						if (!StringUtil.isEmpty(dynamicData.getTypez())) {
							dynamicFieldsValue.setTypez(Integer.parseInt(dynamicData.getTypez()));
						}
						dynamicFieldsValue.setTxnType(dynamicData.getTxnTypez());
						dynamicFieldsValue.setComponentType(dynamicData.getCompoType());
						if (dynamicFieldsValue.getComponentType() == null) {
							dynamicFieldsValue
									.setComponentType(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null
											? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getComponentType()
											: null);

						}
						dynamicFieldsValue.setCreatedDate(new Date());
						dynamicFieldsValue.setCreatedUser(getUsername());
						dynamicFieldsValue.setTxnUniqueId(DateUtil.getRevisionNumber());
						dynamicFieldsValue.setReferenceId(String.valueOf(id));
						dynamicFieldsValue
								.setIsMobileAvail(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null
										? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getIsMobileAvail()
										: null);

						dynamicFieldsValue.setAccessType(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null
								? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getAccessType() : 0);

						dynamicFieldsValue.setListMethod(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null
								&& fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getCatalogueType() != null
										? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getCatalogueType()
										: "");
						dynamicFieldsValue
						.setFollowUp(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null

								? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getFollowUp() : 0);
						dynamicFieldsValue.setParentActField(fieldConfigMap
								.get(dynamicFieldsValue.getFieldName()) != null
								&& fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getParentActField() != null
										? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getParentActField()
										: null);
								dynamicFieldsValue.setParentActKey(fieldConfigMap
								.get(dynamicFieldsValue.getFieldName()) != null
								&& fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getParentActKey() != null
										? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getParentActKey()
										: null);
						dynamicFieldsValue
						.setGrade(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null
								? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getGrade() : null);
						dynamicFieldsValue.setParentId(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null
								&& fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getReferenceId() != null
										? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getReferenceId() : 0);
						if (fieldConfigMap.get(dynamicFieldsValue.getFieldName())!=null&&fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getIsOther() == 1) {
							if (Integer.valueOf(
									dynamicFieldsValue.getAccessType()) == DynamicFieldConfig.ACCESS_TYPE.CATALOGUE_TYPE
											.ordinal()) {
								if (dynamicFieldsValue.getFieldValue().contains(",")) {
									Arrays.asList(dynamicFieldsValue.getFieldValue().split(",")).stream().forEach(u -> {
										FarmCatalogue fm = catalogueService.findCatalogueByCode(u);
										if (fm == null) {
											fm = new FarmCatalogue();
											fm.setCode(idGenerator.getCatalogueIdSeq());
											fm.setName(u);
											fm.setRevisionNo(DateUtil.getRevisionNumber());
											fm.setStatus("1");
											fm.setTypez(Integer.valueOf(fieldConfigMap
													.get(dynamicFieldsValue.getFieldName()).getCatalogueType()));
											catalogueService.addCatalogue(fm);
											dynamicFieldsValue.setFieldValue(
													dynamicFieldsValue.getFieldValue().replaceAll(u, fm.getCode()));
										}

									});
								} else {
									FarmCatalogue fm = catalogueService
											.findCatalogueByCode(dynamicFieldsValue.getFieldValue());
									if (fm == null) {
										fm = new FarmCatalogue();
										fm.setCode(idGenerator.getCatalogueIdSeq());
										fm.setName(dynamicFieldsValue.getFieldValue());
										fm.setRevisionNo(DateUtil.getRevisionNumber());
										fm.setStatus("1");
										fm.setTypez(Integer.valueOf(fieldConfigMap
												.get(dynamicFieldsValue.getFieldName()).getCatalogueType()));
										catalogueService.addCatalogue(fm);
									}
									dynamicFieldsValue.setFieldValue(fm.getCode());
								}
							}
						}

						if (dynamicData.getCompoType() != null && dynamicData.getCompoType().equals("12")
								&& dynamicData.getImageFile() != null
								&& !StringUtil.isEmpty(dynamicData.getImageFile())) {
							Set<DynamicImageData> imageDataSet = new HashSet<>();
							DynamicImageData dy = new DynamicImageData();
							dy.setImage(Base64.decode(dynamicData.getImageFile().split("base64,")[1]));
							dy.setFarmerDynamicFieldsValue(dynamicFieldsValue);
							dy.setOrder("1");
							imageDataSet.add(dy);
							dynamicFieldsValue.setDymamicImageData(imageDataSet);

						}

						dynamicFieldsValuesSet.add(dynamicFieldsValue);
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		

		return dynamicFieldsValuesSet;

	}

	FarmerDynamicData farmerDynamicData = new FarmerDynamicData();

	protected void updateDynamicFields(String txnType, String referenceId, String seasonCode, String entityType) {
		// if (!StringUtil.isEmpty(dynamicFieldsArray) &&
		// dynamicFieldsArray.length() > 0) {
		Map<String, List<FarmerDynamicFieldsValue>> fdMap = new HashMap<>();
		farmerDynamicData = new FarmerDynamicData();
		List<DynamicFeildMenuConfig> dm = farmerService.findDynamicMenusByType(txnType);

		farmerDynamicData = farmerService.findFarmerDynamicData(txnType, String.valueOf(referenceId));
		if (farmerDynamicData != null) {
		farmerDynamicData.setScoreValue(new HashMap<>());
		dm.stream().flatMap(listContainer -> listContainer.getDynamicFieldConfigs().stream())
				.collect(Collectors.toList()).stream().forEach(section -> {
					section.getField().setfOrder(section.getOrder());
					fieldConfigMap.put(section.getField().getCode(), section.getField());
					if (section.getDynamicFieldScoreMap() != null && !section.getDynamicFieldScoreMap().isEmpty()) {
						farmerDynamicData.getScoreValue().put(section.getField().getCode(), section
								.getDynamicFieldScoreMap().stream()
								.collect(Collectors.toMap(DynamicFieldScoreMap::getCatalogueCode, u -> String.valueOf(
										String.valueOf(u.getScore()) + "~" + String.valueOf(u.getPercentage())))));
					}

				});
			fdMap = farmerDynamicData.getFarmerDynamicFieldsValues().stream()
					.collect(Collectors.groupingBy(FarmerDynamicFieldsValue::getFieldName));
			farmerDynamicData.getFarmerDynamicFieldsValues().clear();
			farmerDynamicData
					.setFarmerDynamicFieldsValues(editFarmerDynamicFieldsSet(fdMap, farmerDynamicData, referenceId));
		} else {
			farmerDynamicData = new FarmerDynamicData();
			dm.stream().flatMap(listContainer -> listContainer.getDynamicFieldConfigs().stream())
			.collect(Collectors.toList()).stream().forEach(section -> {
				section.getField().setfOrder(section.getOrder());
				fieldConfigMap.put(section.getField().getCode(), section.getField());
			});
			farmerDynamicData.setFarmerDynamicFieldsValues(addFarmerDynamicFieldsSet(referenceId));
			// farmerDynamicData.setTxnUniqueId(Long.valueOf(head.getMsgNo()));
			farmerDynamicData.setReferenceId(String.valueOf(referenceId));
			farmerDynamicData.setTxnType(txnType);
			farmerDynamicData.setDate(new Date(0));
			farmerDynamicData.setCreatedDate(new Date(0));
			farmerDynamicData.setCreatedUser(getUsername());
			farmerDynamicData.setStatus("0");
			farmerDynamicData.setBranch(getBranchId());
			farmerDynamicData.setEntityId("1");
			farmerDynamicData.setSeason(seasonCode);

		}
		fdMap = farmerDynamicData.getFarmerDynamicFieldsValues().stream()
				.collect(Collectors.groupingBy(FarmerDynamicFieldsValue::getFieldName));
		farmerService.saveOrUpdate(farmerDynamicData, fdMap, fieldConfigMap);
		if (fieldConfigMap.values().stream().anyMatch(p -> Arrays.asList("4").contains(p.getIsMobileAvail())
				&& p.getFormula() != null && !StringUtil.isEmpty(p.getFormula()))) {
			farmerService.processCustomisedFormula(farmerDynamicData, fieldConfigMap,new HashMap<>());
		}
		/* farmerService.saveOrUpdate(farmerDynamicData); */
		farmerService.deleteChildObjects(farmerDynamicData.getTxnType());

		// }
	}

	protected Set<FarmerDynamicFieldsValue> editFarmerDynamicFieldsSet(
			Map<String, List<FarmerDynamicFieldsValue>> fdMap, FarmerDynamicData farmerDynamicData,
			String referenceID) {
		Set<FarmerDynamicFieldsValue> dynamicFieldsValuesSet = new HashSet<>();
		
		
		
		if (!StringUtil.isEmpty(dynamicFieldsArray)) {
			try {
				List<FarmerDynamicFieldsValue> farmerDynamicFieldValuesList = new ArrayList<>();
				Type listType1 = new TypeToken<List<DynamicData>>() {
				}.getType();
				List<DynamicData> dynamicList = new Gson().fromJson(dynamicFieldsArray, listType1);
				for (DynamicData dynamicData : dynamicList) {
					if (!StringUtil.isEmpty(dynamicData.getValue())
							|| !StringUtil.isEmpty(dynamicData.getImageFile())) {
						FarmerDynamicFieldsValue dynamicFieldsValue = new FarmerDynamicFieldsValue();
						//farmerDynamicData.getFarmerDynamicFieldsValues().removeIf(u ->u.getFieldName().equals(dynamicData.getName()));
						dynamicFieldsValue.setFieldName(dynamicData.getName());
						dynamicFieldsValue.setFieldValue(dynamicData.getValue());
						if (!StringUtil.isEmpty(dynamicData.getTypez())) {
							dynamicFieldsValue.setTypez(Integer.parseInt(dynamicData.getTypez()));
						}
						dynamicFieldsValue.setTxnType(dynamicData.getTxnTypez());
						dynamicFieldsValue.setComponentType(dynamicData.getCompoType());
						dynamicFieldsValue.setCreatedDate(new Date());
						dynamicFieldsValue.setCreatedUser(getUsername());
						dynamicFieldsValue.setTxnUniqueId(DateUtil.getRevisionNumber());
						dynamicFieldsValue.setReferenceId(String.valueOf(referenceID));
						if (dynamicFieldsValue.getComponentType() == null) {
							dynamicFieldsValue
									.setComponentType(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null
											? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getComponentType()
											: null);

						}
						dynamicFieldsValue.setCreatedDate(new Date());
						dynamicFieldsValue.setCreatedUser(getUsername());
						dynamicFieldsValue.setTxnUniqueId(DateUtil.getRevisionNumber());
						dynamicFieldsValue.setReferenceId(String.valueOf(referenceID));
						dynamicFieldsValue
								.setIsMobileAvail(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null
										? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getIsMobileAvail()
										: null);
						dynamicFieldsValue
						.setFollowUp(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null

								? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getFollowUp() : 0);
						dynamicFieldsValue
						.setGrade(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null
								? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getGrade() : null);
						dynamicFieldsValue.setParentActField(fieldConfigMap
						.get(dynamicFieldsValue.getFieldName()) != null
						&& fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getParentActField() != null
								? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getParentActField()
								: null);
						dynamicFieldsValue.setParentActKey(fieldConfigMap
						.get(dynamicFieldsValue.getFieldName()) != null
						&& fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getParentActKey() != null
								? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getParentActKey()
								: null);

						dynamicFieldsValue.setAccessType(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null
								? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getAccessType() : 0);

						dynamicFieldsValue.setListMethod(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null
								&& fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getCatalogueType() != null
										? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getCatalogueType()
										: "");

						dynamicFieldsValue.setParentId(fieldConfigMap.get(dynamicFieldsValue.getFieldName()) != null
								&& fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getReferenceId() != null
										? fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getReferenceId() : 0);
						if (fieldConfigMap.get(dynamicFieldsValue.getFieldName()).getIsOther() == 1) {
							if (Integer.valueOf(
									dynamicFieldsValue.getAccessType()) == DynamicFieldConfig.ACCESS_TYPE.CATALOGUE_TYPE
											.ordinal()) {
								if (dynamicFieldsValue.getFieldValue().contains(",")) {
									Arrays.asList(dynamicFieldsValue.getFieldValue().split(",")).stream().forEach(u -> {
										FarmCatalogue fm = catalogueService.findCatalogueByCode(u);
										if (fm == null) {
											fm = new FarmCatalogue();
											fm.setCode(idGenerator.getCatalogueIdSeq());
											fm.setName(u);
											fm.setRevisionNo(DateUtil.getRevisionNumber());
											fm.setStatus("1");
											fm.setTypez(Integer.valueOf(fieldConfigMap
													.get(dynamicFieldsValue.getFieldName()).getCatalogueType()));
											catalogueService.addCatalogue(fm);
											dynamicFieldsValue.setFieldValue(
													dynamicFieldsValue.getFieldValue().replaceAll(u, fm.getCode()));
										}

									});
								} else {
									FarmCatalogue fm = catalogueService
											.findCatalogueByCode(dynamicFieldsValue.getFieldValue());
									if (fm == null) {
										fm = new FarmCatalogue();
										fm.setCode(idGenerator.getCatalogueIdSeq());
										fm.setName(dynamicFieldsValue.getFieldValue());
										fm.setRevisionNo(DateUtil.getRevisionNumber());
										fm.setStatus("1");
										fm.setTypez(Integer.valueOf(fieldConfigMap
												.get(dynamicFieldsValue.getFieldName()).getCatalogueType()));
										catalogueService.addCatalogue(fm);
									}
									dynamicFieldsValue.setFieldValue(fm.getCode());
								}
							}
						}

						if (dynamicData.getCompoType() != null
								&& (dynamicData.getCompoType().equals("12") || dynamicData.getCompoType().equals("13"))
								&& dynamicData.getImageFile() != null
								&& !StringUtil.isEmpty(dynamicData.getImageFile())) {
							Set<DynamicImageData> imageDataSet = new HashSet<>();
							DynamicImageData dy = new DynamicImageData();
							dy.setFileExt(dynamicData.getFileExt());

							try {
								Long ids = Long.parseLong(dynamicData.getImageFile());
								DynamicImageData dt = farmerService.findDynamicImageDataById(ids);
								if (dt != null) {
									dy.setImage(dt.getImage());
									dy.setFileExt(dt.getFileExt());
								}

							} catch (Exception e) {

								dy.setImage(Base64.decode(dynamicData.getImageFile().split("base64,")[1]));

							}

							dy.setFarmerDynamicFieldsValue(dynamicFieldsValue);
							dy.setOrder("1");
							imageDataSet.add(dy);
							dynamicFieldsValue.setDymamicImageData(imageDataSet);

						}

						farmerDynamicFieldValuesList.add(dynamicFieldsValue);
					}
				}
				
				dynamicFieldsValuesSet.addAll(farmerDynamicFieldValuesList);
				
				farmerDynamicFieldValuesList.stream()
				.filter(farmerDynamicFieldsValue -> farmerDynamicFieldsValue.getFollowUp() == 1
						|| farmerDynamicFieldsValue.getFollowUp() == 2)
				.forEach(farmerDynamicFieldsValue -> {
					if (farmerDynamicFieldsValue.getFollowUp() == 1 && dynamicFieldsValuesSet.stream()
							.anyMatch(u -> u.getFieldName().equals(farmerDynamicFieldsValue.getParentActField()))) {
						FarmerDynamicFieldsValue ParentACt = dynamicFieldsValuesSet.stream()
								.filter(u -> u.getFieldName().equals(farmerDynamicFieldsValue.getParentActField()))
								.findFirst().get();
					
						dynamicFieldsValuesSet.remove(ParentACt);
						ParentACt.setActionPlan(farmerDynamicFieldsValue);
						dynamicFieldsValuesSet.add( ParentACt);

					} else if (farmerDynamicFieldsValue.getFollowUp() == 2 && dynamicFieldsValuesSet.stream()
							.anyMatch(u -> u.getFieldName().equals(farmerDynamicFieldsValue.getParentActField()))) {
						FarmerDynamicFieldsValue ParentACt = dynamicFieldsValuesSet.stream()
								.filter(u -> u.getFieldName().equals(farmerDynamicFieldsValue.getParentActField()))
								.findFirst().get();
						dynamicFieldsValuesSet.remove(ParentACt);
						ParentACt.setDeadline(farmerDynamicFieldsValue);
						dynamicFieldsValuesSet.add( ParentACt);

					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return dynamicFieldsValuesSet;

	}

	public Map<String, String> getVarietyDynamic() {
		return productDistributionService.listProcurementVariety().stream()
				.filter(u -> u != null && u.getCode() != null)
				.collect(Collectors.toMap(u -> u.getCode().toString(), u -> u.getName()));
	}

	public Map<String, String> getGradeDynamic() {
		return productDistributionService.listProcurementGrade().stream().filter(u -> u != null && u.getCode() != null)
				.collect(Collectors.toMap(u -> u.getCode().toString(), u -> u.getName()));
	}

	protected String convertToJson(String method, Object param) {
		if (param != null) {
			Gson gs = new Gson();
			JsonObject js = gs.fromJson(method, JsonElement.class).getAsJsonObject();
			return js.get(param.toString()).getAsString();
		} else {
			return "";
		}
	}

	protected Map<String, String> getQueryForFilters(String methodName, Object[] param) {

		Map<String, String> valuesMap = new LinkedHashMap<String, String>();
		if (!StringUtil.isEmpty(methodName)) {
			filterObjs = farmerService.listValuesbyQuery(methodName, param, getBranchId());
		}
		filterObjs.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			valuesMap.put(String.valueOf(objArr[0]), String.valueOf(objArr[1]));
		});
		return valuesMap;
	}

	protected String getQueryForFiltersJSON(String methodName, Object[] arr) {
		if (arr != null) {
			return farmerService.getValueByQuery(methodName, arr, getBranchId());
		} else {
			return "";
		}
	}
	
	protected List<byte[]> getQueryForImagesJSON(String methodName, Object[] arr) {
		List<byte[]> a=new ArrayList<byte[]>();
		if (arr != null) {
			return farmerService.getImageByQuery(methodName, arr, getBranchId());
		} else {
			return a;
		}
	}

	public Map<String, String> getHarvestseasonsLang() {

		Map<String, String> seasonMap = new HashMap<>();
		List<Object[]> listTemp = farmerService.listfarmingseasonlist();
		for (Object[] obj : listTemp) {
			String name = getLanguagePref(getLoggedInUserLanguage(), obj[0].toString());
			if (!StringUtil.isEmpty(name) && name != null) {
				seasonMap.put(obj[0].toString(), getLanguagePref(getLoggedInUserLanguage(), obj[0].toString()));
			} else {
				seasonMap.put(obj[0].toString(), obj[1].toString());
			}

		}
		return seasonMap;
	}

	public Map<String, String> getListProcurementProductLang() {
		List<ProcurementProduct> cropsList = new ArrayList<ProcurementProduct>();
		Map<String, String> farmCropsMap = new LinkedHashMap<String, String>();
		cropsList = productDistributionService.listProcurementProduct();
		for (ProcurementProduct farmCrop : cropsList) {

			String name = getLanguagePref(getLoggedInUserLanguage(), farmCrop.getCode().trim().toString());
			if (!StringUtil.isEmpty(name) && name != null) {
				farmCropsMap.put(farmCrop.getCode().toString(),
						getLanguagePref(getLoggedInUserLanguage(), farmCrop.getCode().toString()));
			} else {
				farmCropsMap.put(farmCrop.getCode().toString(), farmCrop.getName().toString());
			}
		}
		return farmCropsMap;
	}

	public Map<String, String> getCountriesLang() {

		Map<String, String> countryMap = new LinkedHashMap<String, String>();
		List<Country> countryList = locationService.listCountries();
		for (Country obj : countryList) {
			// countryMap.put(obj.getName(), obj.getCode() + "-" +
			// obj.getName());

			String name = getLanguagePref(getLoggedInUserLanguage(), obj.getCode().trim().toString());
			if (!StringUtil.isEmpty(name) && name != null) {
				countryMap.put(obj.getCode().toString(), obj.getCode().toString() + "-"
						+ getLanguagePref(getLoggedInUserLanguage(), obj.getCode().toString()));
			} else {
				countryMap.put(obj.getName(), obj.getCode() + "-" + obj.getName());
			}

		}
		return countryMap;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@SuppressWarnings("unchecked")
	public void populateDynamicFieldsForOCP() {
		isScore = "0";
		Map<String, List<JSONObject>> jsonMap = new LinkedHashMap<>();
		List<JSONObject> fieldList = new LinkedList<>();
		List<JSONObject> groupList = new LinkedList<>();
		List<JSONObject> sectionList = new LinkedList<>();
		List<DynamicSectionConfig> secList = new ArrayList<>();
		if (branch == null || branch == "") {
			branch = getBranchId();
		}
		List<DynamicFeildMenuConfig> dyList = farmerService.findDynamicMenusByTypeForOCP(getTxnTypez(), branch);
		List<DynamicConstants> dsc = farmerService.listDynamicConstants();
		cts = dsc.stream().map(u -> u.getCode()).collect(Collectors.toList());
		Map<String,DynamicMenuFieldMap> menMap  = (Map<String,DynamicMenuFieldMap>)dyList.stream()
				.flatMap(listContainer -> listContainer.getDynamicFieldConfigs().stream())
				.collect(Collectors.toList()).stream().collect(Collectors.toMap(u -> u.getField().getCode(), obj -> obj));
		if (dyList != null && !dyList.isEmpty()) {
			isScore = dyList.get(0).getIsScore().toString();
			List<DynamicFieldConfig> dynamicFieldsConfigList = (List<DynamicFieldConfig>) dyList.stream()
					.flatMap(listContainer -> listContainer.getDynamicFieldConfigs().stream())
					.collect(Collectors.toList()).stream().map(u -> u.getField()).collect(Collectors.toList());
			dyList.stream().forEach(dynamicMenu -> {
				dynamicMenu.getDynamicSectionConfigs().stream().filter(
						u -> (u.getSection().getMobileFieldsSize() > 0 || (queryType != null && queryType.equals("1"))))
						.forEach(dynamicSection -> {
							JSONObject obj = new JSONObject();
							// obj.put("secCode",
							// dynamicSection.getSectionCode());
							// obj.put("secName",
							// dynamicSection.getSectionName());
							obj.put("secCode", dynamicSection.getSection().getSectionCode());
							obj.put("secName",
									StringUtil.isEmpty(getLanguagePref(getLoggedInUserLanguage(),
											dynamicSection.getSection().getSectionCode()))
													? dynamicSection.getSection().getSectionName()
													: getLanguagePref(getLoggedInUserLanguage(),
															dynamicSection.getSection().getSectionCode()));
							obj.put("secOrder", dynamicSection.getSection().getSecorder());
							obj.put("isScore", isScore);
							obj.put("typez", dynamicSection.getSection().getTableId() != null
									? dynamicSection.getSection().getTableId() : "");
							sectionList.add(obj);
							secList.add(dynamicSection.getSection());
						});
			});

			dynamicFieldsConfigList.stream()
					.filter(dynamicFieldConfig -> ObjectUtil.isEmpty(dynamicFieldConfig.getReferenceId())
							|| !StringUtil.isLong(dynamicFieldConfig.getReferenceId()))
					.forEach(dynamicFieldConfig -> {
						if (((Arrays.asList("1", "0").contains(dynamicFieldConfig.getIsMobileAvail())
								&& !Arrays.asList(4).contains(dynamicFieldConfig.getFollowUp()))
								|| ((queryType != null && queryType.equals("1"))
										&& !Arrays.asList(4).contains(dynamicFieldConfig.getFollowUp())))) {
							JSONObject obj = new JSONObject();
							if (dynamicFieldConfig.getParentActKey() != null
									&& !StringUtil.isEmpty(dynamicFieldConfig.getParentActKey())
									&& (dynamicFieldConfig.getFollowUp() == 1
											|| dynamicFieldConfig.getFollowUp() == 2)) {
								dynamicFieldConfig
										.setParentDependencyKey(dynamicFieldConfig.getParentDependencyKey() != null
												&& !StringUtil.isEmpty(dynamicFieldConfig.getParentDependencyKey())
														? dynamicFieldConfig.getParentDependencyKey() + ","
																+ dynamicFieldConfig.getParentActKey()
														: dynamicFieldConfig.getParentActKey());
							}
							if (dynamicFieldConfig.getParentActKey() != null
									&& !StringUtil.isEmpty(dynamicFieldConfig.getParentActKey())
									&& (dynamicFieldConfig.getFollowUp() == 3)) {
								dynamicFieldConfig.setCatDependencyKey(dynamicFieldConfig.getCatDependencyKey() != null
										&& !StringUtil.isEmpty(dynamicFieldConfig.getCatDependencyKey())
												? dynamicFieldConfig.getCatDependencyKey() + ","
														+ dynamicFieldConfig.getParentActKey()
												: dynamicFieldConfig.getParentActKey());
							}
							String depeCode = !ObjectUtil.isEmpty(dynamicFieldConfig.getParentDepen())
									? dynamicFieldConfig.getParentDepen().getCode() : "";
							if (dynamicFieldConfig.getParentActField() != null
									&& !StringUtil.isEmpty(dynamicFieldConfig.getParentActField())
									&& (dynamicFieldConfig.getFollowUp() == 1
											|| dynamicFieldConfig.getFollowUp() == 2)) {
								depeCode = dynamicFieldConfig.getParentActField();
							}

							obj.put("id", dynamicFieldConfig.getId());
							obj.put("secCode", dynamicFieldConfig.getDynamicSectionConfig().getSectionCode());
							obj.put("compoType", dynamicFieldConfig.getComponentType());
							obj.put("compoName", StringUtil
									.isEmpty(getLanguagePref(getLoggedInUserLanguage(), dynamicFieldConfig.getCode()))
											? dynamicFieldConfig.getComponentName()
											: getLanguagePref(getLoggedInUserLanguage(), dynamicFieldConfig.getCode()));
							obj.put("compoCode", dynamicFieldConfig.getCode());
							obj.put("parentDepen", ObjectUtil.isEmpty(dynamicFieldConfig.getParentDepen()) ? ""
									: dynamicFieldConfig.getParentDepen().getId());
							obj.put("parentDepenCode", depeCode);
							obj.put("parentDepenKey", ObjectUtil.isEmpty(dynamicFieldConfig.getParentDependencyKey())
									? "" : dynamicFieldConfig.getParentDependencyKey());
							obj.put("catDepKey", ObjectUtil.isEmpty(dynamicFieldConfig.getCatDependencyKey()) ? ""
									: dynamicFieldConfig.getCatDependencyKey());
							obj.put("maxLen", dynamicFieldConfig.getComponentMaxLength());
							obj.put("isReq", dynamicFieldConfig.getIsRequired());
							obj.put("sectionId", dynamicFieldConfig.getDynamicSectionConfig().getSectionCode());
							obj.put("sectionId", dynamicFieldConfig.getDynamicSectionConfig().getSectionCode());
							obj.put("isOther", dynamicFieldConfig.getIsOther());
							obj.put("isMobile", dynamicFieldConfig.getIsMobileAvail());
							obj.put("isScore", isScore);
							obj.put("typez", dynamicFieldConfig.getDynamicSectionConfig().getTableId() != null
									? dynamicFieldConfig.getDynamicSectionConfig().getTableId() : "");

							JSONArray cName = new JSONArray();
							JSONArray cType = new JSONArray();

							String ct = dynamicFieldConfig.getCatalogueType();
							if (dynamicFieldConfig.getAccessType() > 0 && dynamicFieldConfig.getAccessType() == 1) {

								Integer type = new Integer(ct);
								Map<String, String> farmCatalougeList = getFarmCatalougeMap(type);
								for (Map.Entry<String, String> entry : farmCatalougeList.entrySet()) {
									cType.add(entry.getKey());
									cName.add(entry.getValue());

								}
							} else if (dynamicFieldConfig.getAccessType() > 0
									&& dynamicFieldConfig.getAccessType() == 2) {
								// String ct =
								// dynamicFieldConfig.getCatalogueType();
								if (!StringUtil.isEmpty(ct)) {
									for (String val : ct.split(",")) {
										if ((val.contains("CG"))) {
											cType.add(val);
											FarmCatalogue fc = getCatlogueValueByCode(val.trim());
											cName.add(fc.getName());
										} else {
											Integer type = new Integer(val);
											Map<String, String> farmCatalougeList = getFarmCatalougeMap(type);
											for (Map.Entry<String, String> entry : farmCatalougeList.entrySet()) {
												cType.add(entry.getKey());
												cName.add(entry.getValue());

											}

										}
									}
								}
							} else if (dynamicFieldConfig.getAccessType() > 0
									&& dynamicFieldConfig.getAccessType() == 3) {
								LIST_METHOD[] methods = DynamicFieldConfig.LIST_METHOD.values();
								try {
									for (Map.Entry<String, String> entry : getOptions(
											methods[Integer.valueOf(ct)].toString()).entrySet()) {
										cType.add(entry.getKey());
										cName.add(entry.getValue());

									}
								} catch (Exception e) {

								}
							}
							obj.put("catalogueType", cType);
							obj.put("catalogueName", cName);
							obj.put("accessType", dynamicFieldConfig.getAccessType());
							obj.put("dataFormat", dynamicFieldConfig.getDataFormat());

							obj.put("beforeInsert", menMap.get(dynamicFieldConfig.getCode()).getBeforeInsert());
							obj.put("afterInsert", menMap.get(dynamicFieldConfig.getCode()).getAfterInsert());
							obj.put("validation", dynamicFieldConfig.getValidation());
							obj.put("defaultVal", dynamicFieldConfig.getDefaultValue());
							formula = "";
							if (dynamicFieldConfig.getDependencyKey() != null
									&& !StringUtil.isEmpty(dynamicFieldConfig.getDependencyKey())) {
								List<DynamicFieldConfig> formulaList = dynamicFieldsConfigList.stream()
										.filter(forumula -> Arrays
												.asList(dynamicFieldConfig.getDependencyKey().contains(",")
														? dynamicFieldConfig.getDependencyKey().split(",")
														: dynamicFieldConfig.getDependencyKey())
												.contains(forumula.getCode()))
										.collect(Collectors.toList());
								if (formulaList != null && !formulaList.isEmpty()) {
									formula = formulaList.stream().map(DynamicFieldConfig::getFormula)
											.collect(Collectors.joining(","));
								}
							}
							obj.put("constExist", "0");
							if (StringUtil.isEmpty(formula) || formula.equals("null") || formula.equals("")) {

								obj.put("formula", dynamicFieldConfig.getFormula());
							} else {

								if (cts != null && !cts.stream().filter(u -> formula.contains(u))
										.collect(Collectors.toList()).isEmpty()) {
									obj.put("constExist", "1");
								}

								obj.put("formula", formula);
							}

							obj.put("dependencyKey", dynamicFieldConfig.getDependencyKey());
							obj.put("orderSet", dynamicFieldConfig.getOrderSet());
							fieldList.add(obj);
						}
					});

			dynamicFieldsConfigList.stream()
					.filter(dynamicFieldConfig -> !ObjectUtil.isEmpty(dynamicFieldConfig.getReferenceId())
							&& StringUtil.isLong(dynamicFieldConfig.getReferenceId()))
					.forEach(dynamicFieldConfig -> {
						if (((Arrays.asList("1", "0").contains(dynamicFieldConfig.getIsMobileAvail())
								&& !Arrays.asList(4).contains(dynamicFieldConfig.getFollowUp()))
								|| ((queryType != null && queryType.equals("1"))
										&& !Arrays.asList(4).contains(dynamicFieldConfig.getFollowUp())))) {

							JSONObject obj = new JSONObject();
							if (dynamicFieldConfig.getParentActKey() != null
									&& !StringUtil.isEmpty(dynamicFieldConfig.getParentActKey())
									&& (dynamicFieldConfig.getFollowUp() == 1
											|| dynamicFieldConfig.getFollowUp() == 2)) {
								dynamicFieldConfig
										.setParentDependencyKey(dynamicFieldConfig.getParentDependencyKey() != null
												&& !StringUtil.isEmpty(dynamicFieldConfig.getParentDependencyKey())
														? dynamicFieldConfig.getParentDependencyKey() + ","
																+ dynamicFieldConfig.getParentActKey()
														: dynamicFieldConfig.getParentActKey());
							}
							if (dynamicFieldConfig.getParentActKey() != null
									&& !StringUtil.isEmpty(dynamicFieldConfig.getParentActKey())
									&& (dynamicFieldConfig.getFollowUp() == 3)) {
								dynamicFieldConfig.setCatDependencyKey(dynamicFieldConfig.getCatDependencyKey() != null
										&& !StringUtil.isEmpty(dynamicFieldConfig.getCatDependencyKey())
												? dynamicFieldConfig.getCatDependencyKey() + ","
														+ dynamicFieldConfig.getParentActKey()
												: dynamicFieldConfig.getParentActKey());
							}
							String depeCode = !ObjectUtil.isEmpty(dynamicFieldConfig.getParentDepen())
									? dynamicFieldConfig.getParentDepen().getCode() : "";
							if (dynamicFieldConfig.getParentActField() != null
									&& !StringUtil.isEmpty(dynamicFieldConfig.getParentActField())
									&& (dynamicFieldConfig.getFollowUp() == 1
											|| dynamicFieldConfig.getFollowUp() == 2)) {
								depeCode = dynamicFieldConfig.getParentActField();
							}

							obj.put("id", dynamicFieldConfig.getId());
							obj.put("secCode", dynamicFieldConfig.getDynamicSectionConfig().getSectionCode());

							obj.put("compoType", dynamicFieldConfig.getComponentType());
							obj.put("compoName", StringUtil
									.isEmpty(getLanguagePref(getLoggedInUserLanguage(), dynamicFieldConfig.getCode()))
											? dynamicFieldConfig.getComponentName()
											: getLanguagePref(getLoggedInUserLanguage(), dynamicFieldConfig.getCode()));
							obj.put("compoCode", dynamicFieldConfig.getCode());
							obj.put("parentDepen", ObjectUtil.isEmpty(dynamicFieldConfig.getParentDepen()) ? ""
									: dynamicFieldConfig.getParentDepen().getId());
							obj.put("parentDepenCode", depeCode);
							obj.put("parentDepenKey", ObjectUtil.isEmpty(dynamicFieldConfig.getParentDependencyKey())
									? "" : dynamicFieldConfig.getParentDependencyKey());
							obj.put("catDepKey", ObjectUtil.isEmpty(dynamicFieldConfig.getCatDependencyKey()) ? ""
									: dynamicFieldConfig.getCatDependencyKey());
							obj.put("isOther", dynamicFieldConfig.getIsOther());
							obj.put("maxLen", dynamicFieldConfig.getComponentMaxLength());
							obj.put("isReq", dynamicFieldConfig.getIsRequired());
							obj.put("sectionId", dynamicFieldConfig.getDynamicSectionConfig().getSectionCode());
							obj.put("isScore", isScore);
							obj.put("typez", dynamicFieldConfig.getDynamicSectionConfig().getTableId() != null
									? dynamicFieldConfig.getDynamicSectionConfig().getTableId() : "");

							obj.put("isMobile", dynamicFieldConfig.getIsMobileAvail());
							JSONArray cName = new JSONArray();
							JSONArray cType = new JSONArray();

							String ct = dynamicFieldConfig.getCatalogueType();
							if (dynamicFieldConfig.getAccessType() > 0 && dynamicFieldConfig.getAccessType() == 1) {

								Integer type = new Integer(ct);
								Map<String, String> farmCatalougeList = getFarmCatalougeMap(type);
								for (Map.Entry<String, String> entry : farmCatalougeList.entrySet()) {
									cType.add(entry.getKey());
									cName.add(entry.getValue());

								}
							} else if (dynamicFieldConfig.getAccessType() > 0
									&& dynamicFieldConfig.getAccessType() == 1) {
								// String ct =
								// dynamicFieldConfig.getCatalogueType();
								if (!StringUtil.isEmpty(ct)) {
									for (String val : ct.split(",")) {
										if ((val.contains("CG"))) {
											cType.add(val);
											FarmCatalogue fc = getCatlogueValueByCode(val.trim());
											cName.add(fc.getName());
										} else {
											Integer type = new Integer(val);
											Map<String, String> farmCatalougeList = getFarmCatalougeMap(type);
											for (Map.Entry<String, String> entry : farmCatalougeList.entrySet()) {
												cType.add(entry.getKey());
												cName.add(entry.getValue());

											}

										}
									}
								}
							} else if (dynamicFieldConfig.getAccessType() > 0
									&& dynamicFieldConfig.getAccessType() == 3) {
								LIST_METHOD[] methods = DynamicFieldConfig.LIST_METHOD.values();
								try {
									for (Map.Entry<String, String> entry : getOptions(
											methods[Integer.valueOf(ct)].toString()).entrySet()) {
										cType.add(entry.getKey());
										cName.add(entry.getValue());

									}
								} catch (Exception e) {

								}
							}
							obj.put("catalogueType", cType);
							obj.put("catalogueName", cName);
							obj.put("accessType", dynamicFieldConfig.getAccessType());
							obj.put("dataFormat", dynamicFieldConfig.getDataFormat());
							obj.put("beforeInsert", menMap.get(dynamicFieldConfig.getCode()).getBeforeInsert());
							obj.put("afterInsert", menMap.get(dynamicFieldConfig.getCode()).getAfterInsert());
							obj.put("validation", dynamicFieldConfig.getValidation());
							obj.put("defaultVal", dynamicFieldConfig.getDefaultValue());
							formula = "";
							if (dynamicFieldConfig.getDependencyKey() != null
									&& !StringUtil.isEmpty(dynamicFieldConfig.getDependencyKey())) {
								List<DynamicFieldConfig> formulaList = dynamicFieldsConfigList.stream()
										.filter(forumula -> Arrays
												.asList(dynamicFieldConfig.getDependencyKey().contains(",")
														? dynamicFieldConfig.getDependencyKey().split(",")
														: dynamicFieldConfig.getDependencyKey())
												.contains(forumula.getCode()))
										.collect(Collectors.toList());
								if (formulaList != null && !formulaList.isEmpty()) {
									formula = formulaList.stream().map(DynamicFieldConfig::getFormula)
											.collect(Collectors.joining(","));
								}
							}
							obj.put("constExist", "0");
							if (StringUtil.isEmpty(formula) || formula.equals("null") || formula.equals("")) {
								obj.put("formula", dynamicFieldConfig.getFormula());
							} else {
								if (cts != null && !cts.stream().filter(u -> formula.contains(u))
										.collect(Collectors.toList()).isEmpty()) {
									obj.put("constExist", "1");
								}

								obj.put("formula", formula);
							}
							obj.put("dependencyKey", dynamicFieldConfig.getDependencyKey());
							obj.put("refId", dynamicFieldConfig.getReferenceId());
							groupList.add(obj);
						}
					});
		}

		jsonMap.put("sections", sectionList);
		jsonMap.put("fields", fieldList);
		jsonMap.put("groups", groupList);

		JSONObject objects = new JSONObject();
		objects.putAll(jsonMap);

		printAjaxResponse(objects, "text/json");

	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public List<String> getBranches(String branchId) {
		if (ObjectUtil.isEmpty(request)) {
			request = ReflectUtil.getCurrentHttpRequest();
		}
		List<String> branches = new ArrayList<String>();
		Object isMultiBranch =  (Integer) request.getSession().getAttribute(ISecurityFilter.IS_MULTI_BRANCH_APP);
		//Object isMultiBranch = request.getAttribute(ISecurityFilter.IS_MULTI_BRANCH_APP);
		String multiBranch = ObjectUtil.isEmpty(isMultiBranch) ? "" : isMultiBranch.toString();
		if (multiBranch.equals("1")) {
			//Object object = request.getAttribute(ISecurityFilter.MAPPED_BRANCHES);
			Object object=(String) request.getSession().getAttribute(ISecurityFilter.MAPPED_BRANCHES);
			String currentBranch = ObjectUtil.isEmpty(object) ? "" : object.toString();
			if (!StringUtil.isEmpty(currentBranch)) {

				Arrays.asList(currentBranch.split(",")).stream().filter(branch -> !StringUtil.isEmpty(branch))
						.forEach(branch -> {
							branches.add(branch);
						});

			}
		} else {
			//Object object = request.getAttribute(ISecurityFilter.CURRENT_BRANCH);
			Object object=(String) request.getSession().getAttribute(ISecurityFilter.CURRENT_BRANCH);
			String currentBranch = ObjectUtil.isEmpty(object) ? "" : object.toString();
			if (!StringUtil.isEmpty(currentBranch)) {

				branches.add(currentBranch);

			}

		}

		return branches;
	}
	
	@SuppressWarnings("unchecked")
	public void populateDynamicFieldsWithActionPlan() {
	
		if (!StringUtil.isEmpty(getSelectedObject()) && !StringUtil.isEmpty(getTxnTypez())) {
			Map<String, List<JSONObject>> jsonMap = new LinkedHashMap<>();
			List<JSONObject> groupList = new LinkedList<>();
			List<JSONObject> fieldList = new LinkedList<>();
			ArrayList<String> actCt = new ArrayList<>();
			FarmerDynamicData fyd = null;
			if(getTxnTypez().equalsIgnoreCase("359")){
				fyd = farmerService.findFarmerDynamicData(getTxnTypez(),getSelectedObject());
			}else{
			fyd = farmerService.findFarmerDynamicData(getSelectedObject());
			}
			ESESystem preferences = preferncesService.findPrefernceById("1");
			if (fyd.getDymamicImageData() != null && fyd.getDymamicImageData().size() > 0) {
				fyd.setDigSignByteString(Base64Util.encoder(fyd.getDymamicImageData().iterator().next().getImage()));
			}
			List<DynamicFeildMenuConfig> dyList = farmerService.findDynamicMenusByType(fyd.getTxnType());
			List<DynamicFieldConfig> dynamicFieldsConfigList = (List<DynamicFieldConfig>) dyList.stream()
					.flatMap(listContainer -> listContainer.getDynamicFieldConfigs().stream())
					.collect(Collectors.toList()).stream().map(u -> u.getField()).collect(Collectors.toList());
			Map<String, DynamicFieldConfig> dynMap = dynamicFieldsConfigList.stream().filter(u -> Arrays.asList(1, 2, 3).contains(u.getFollowUp()))
					.collect(Collectors.toMap(u -> u.getCode(), u -> u));

			
			fyd.getFarmerDynamicFieldsValues().stream().filter(dynmaicFieldConfig -> (dynMap).containsKey(dynmaicFieldConfig.getFieldName()) && dynMap.get(dynmaicFieldConfig.getFieldName()).getParentActKey().equalsIgnoreCase(dynmaicFieldConfig.getFieldValue())).forEach(dynmaicFieldConfig -> {
				if (ObjectUtil.isEmpty(dynmaicFieldConfig.getParentId()) || dynmaicFieldConfig.getParentId() == 0) {
					JSONObject obj = new JSONObject();
					obj.put("code", dynmaicFieldConfig.getFieldName());
					obj.put("name",
							dynmaicFieldConfig.getFieldValue() == null ? "" : dynmaicFieldConfig.getFieldValue());
					obj.put("componentType", dynmaicFieldConfig.getComponentType());
					obj.put("score", dynmaicFieldConfig.getScore() != null ? dynmaicFieldConfig.getScore() : "");
					obj.put("percentage",
							dynmaicFieldConfig.getPercentage() != null ? dynmaicFieldConfig.getPercentage() : "");
					obj.put("grade", dynmaicFieldConfig.getGrade() != null ? dynmaicFieldConfig.getGrade() : "");
					obj.put("isAct", dynmaicFieldConfig.getFollowUp());
					if (dynmaicFieldConfig.getFollowUp() == 3) {
						if (dynmaicFieldConfig.getFollowUps() != null
								&& !ObjectUtil.isEmpty(dynmaicFieldConfig.getFollowUps())) {
							JSONArray jsonArray = new JSONArray();
							dynmaicFieldConfig.getFollowUps().stream().forEach(dyField -> {
								JSONObject jsonObject = new JSONObject();
								String dataVal = getFieldValue(dyField.getComponentType(),
										String.valueOf(dyField.getAccessType()), dyField.getFieldValue(),
										dyField.getListMethod());
								jsonObject.put("qusCode", dyField.getFieldName());
								jsonObject.put("fieldName", dynMap.get(dyField.getFieldName()).getComponentName());
								jsonObject.put("fieldVal", dataVal);
								if (dyField.getDymamicImageData() != null
										&& !ObjectUtil.isListEmpty(dyField.getDymamicImageData())) {
									dyField.getDymamicImageData().stream().forEach(im -> {
										if (im != null && !ObjectUtil.isEmpty(im)) {
											jsonObject.put("img", Base64Util.encoder(im.getImage()));
										}
									});
								} else {
									jsonObject.put("img", null);
								}
								jsonArray.add(jsonObject);
							});
							String json = jsonArray.toString();
							obj.put("subAns", json);
						}

					}
					if (dynmaicFieldConfig.getActionPlan() != null) {
						obj.put("actPlan", dynmaicFieldConfig.getActionPlan().getFieldValue());

					}
					obj.put("actStatus", dynmaicFieldConfig.getActStatus() != null
							? getLocaleProperty("actStatus" + dynmaicFieldConfig.getActStatus()) : "");
					if (dynmaicFieldConfig.getDeadline() != null) {
						obj.put("deadline", dynmaicFieldConfig.getDeadline().getFieldValue());

					}
					if (dynmaicFieldConfig.getFieldValue() != null) {
						if (!StringUtil.isEmpty(dynmaicFieldConfig.getComponentType())
								&& Arrays
										.asList(String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.DROP_DOWN.ordinal()),
												String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.RADIO.ordinal()),
												String.valueOf(
														DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal()),
												String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.CHECKBOX.ordinal()))
										.contains(dynmaicFieldConfig.getComponentType())
								&& dynmaicFieldConfig.getAccessType() != null
								&& (dynmaicFieldConfig.getAccessType().equals(1)
										|| dynmaicFieldConfig.getAccessType().equals(2))) {
							if (DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal() == Integer
									.valueOf(dynmaicFieldConfig.getComponentType())) {
								obj.put("dispName", getCatlogueValueByCodeArray(dynmaicFieldConfig.getFieldValue()));
							} else {
								obj.put("dispName",
										getCatlogueValueByCode(dynmaicFieldConfig.getFieldValue()).getName());
							}
						} else if (!StringUtil.isEmpty(dynmaicFieldConfig.getComponentType())
								&& Arrays
										.asList(String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.DROP_DOWN.ordinal()),
												String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.RADIO.ordinal()),
												String.valueOf(
														DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal()),
												String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.CHECKBOX.ordinal()))
										.contains(dynmaicFieldConfig.getComponentType())
								&& dynmaicFieldConfig.getAccessType() != null
								&& dynmaicFieldConfig.getAccessType().equals(3)) {
							try {
								LIST_METHOD[] methods = DynamicFieldConfig.LIST_METHOD.values();
								Map<String, String> listValeus = getOptions(
										methods[Integer.valueOf(dynmaicFieldConfig.getListMethod())].toString());

								if (DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal() == Integer
										.valueOf(dynmaicFieldConfig.getComponentType())
										&& dynmaicFieldConfig.getFieldValue().contains(",")) {

									obj.put("dispName", listValeus.entrySet().stream()
											.filter(u -> Arrays.asList(dynmaicFieldConfig.getFieldValue().split(","))
													.contains(u.getKey()))
											.map(u -> u.getValue()).collect(Collectors.joining(",")));
								} else {
									obj.put("dispName", listValeus.containsKey(dynmaicFieldConfig.getFieldValue())
											? listValeus.get(dynmaicFieldConfig.getFieldValue()) : "");
								}

							} catch (Exception e) {
								obj.put("dispName", "");
							}
						} else if (Integer.parseInt(dynmaicFieldConfig
								.getComponentType()) == DynamicFieldConfig.COMPONENT_TYPES.WEATHER_INFO.ordinal()) {
							watherInfo = "";
							if (dynmaicFieldConfig.getFieldValue() != null
									&& !StringUtil.isEmpty(dynmaicFieldConfig.getFieldValue())) {
								String[] arr = dynmaicFieldConfig.getFieldValue().split("\\|");
								AtomicInteger i = new AtomicInteger(0);
								Arrays.asList(getLocaleProperty("temp"), getLocaleProperty("rain"),
										getLocaleProperty("humidity"), getLocaleProperty("windSpeed")).stream()
										.forEach(u -> {
											try {
												watherInfo += u + " : " + arr[i.getAndIncrement()].toString() + " \n ";
											} catch (ArrayIndexOutOfBoundsException e) {
												watherInfo += u + " : " + " \n ";
											}
										});
							}

							obj.put("dispName", watherInfo);
						} else {
							obj.put("dispName", dynmaicFieldConfig.getFieldValue());
						}
					}
					if (dynmaicFieldConfig.getImageIds() != null
							&& !StringUtil.isEmpty(dynmaicFieldConfig.getImageIds())) {
						obj.put("photoCompoAvailable", "1");
						if (dynmaicFieldConfig.getComponentType().equals("13")
								|| dynmaicFieldConfig.getComponentType().equals("11")) {
							obj.put("photoCompoAvailable", "2");
						}
						obj.put("photoIds", dynmaicFieldConfig.getImageIds());
					} else {
						obj.put("photoCompoAvailable", "0");
						obj.put("photoByteStr", "");
					}
					actCt.add(dynmaicFieldConfig.getFieldName());
					String[] pField= dynMap.get(dynmaicFieldConfig.getFieldName()).getParentActField() !=null ? dynMap.get(dynmaicFieldConfig.getFieldName()).getParentActField().split(",") : null;
					if(pField!=null){
					actCt.add(pField[0]);
					actCt.add(pField[1]);
					}
					fieldList.add(obj);
				} else {
					JSONObject obj = new JSONObject();
					obj.put("code", dynmaicFieldConfig.getFieldName());
					obj.put("name",
							dynmaicFieldConfig.getFieldValue() == null ? "" : dynmaicFieldConfig.getFieldValue());
					obj.put("refId", dynmaicFieldConfig.getParentId());
					obj.put("typez", dynmaicFieldConfig.getTypez() + (dynmaicFieldConfig.getFarmerDynamicData() == null
							? "" : "_" + dynmaicFieldConfig.getFarmerDynamicData().getId()));
					obj.put("componentType", dynmaicFieldConfig.getComponentType());
					obj.put("score", dynmaicFieldConfig.getScore() != null ? dynmaicFieldConfig.getScore() : "");
					obj.put("percentage",
							dynmaicFieldConfig.getPercentage() != null ? dynmaicFieldConfig.getPercentage() : "");
					obj.put("grade", dynmaicFieldConfig.getGrade() != null ? dynmaicFieldConfig.getGrade() : "");
					obj.put("isAct", dynmaicFieldConfig.getFollowUp());
					if (dynmaicFieldConfig.getActionPlan() != null) {
						obj.put("actPlan", dynmaicFieldConfig.getActionPlan().getFieldValue());

					}

					obj.put("actStatus", dynmaicFieldConfig.getActStatus() != null
							? getLocaleProperty("actStatus" + dynmaicFieldConfig.getActStatus()) : "");
					if (dynmaicFieldConfig.getDeadline() != null) {
						obj.put("deadline", dynmaicFieldConfig.getDeadline().getFieldValue());

					}

					if (dynmaicFieldConfig.getFieldValue() != null) {
						if (!StringUtil.isEmpty(dynmaicFieldConfig.getComponentType())
								&& Arrays
										.asList(String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.DROP_DOWN.ordinal()),
												String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.RADIO.ordinal()),
												String.valueOf(
														DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal()),
												String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.CHECKBOX.ordinal()))
										.contains(dynmaicFieldConfig.getComponentType())
								&& dynmaicFieldConfig.getAccessType() != null
								&& (dynmaicFieldConfig.getAccessType().equals(1)
										|| dynmaicFieldConfig.getAccessType().equals(2))) {
							if (DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal() == Integer
									.valueOf(dynmaicFieldConfig.getComponentType())) {
								obj.put("dispName", getCatlogueValueByCodeArray(dynmaicFieldConfig.getFieldValue()));
							} else {
								obj.put("dispName",
										getCatlogueValueByCode(dynmaicFieldConfig.getFieldValue()).getName());
							}
						} else if (!StringUtil.isEmpty(dynmaicFieldConfig.getComponentType())
								&& Arrays
										.asList(String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.DROP_DOWN.ordinal()),
												String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.RADIO.ordinal()),
												String.valueOf(
														DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal()),
												String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.CHECKBOX.ordinal()))
										.contains(dynmaicFieldConfig.getComponentType())
								&& dynmaicFieldConfig.getAccessType() != null
								&& dynmaicFieldConfig.getAccessType().equals(3)) {
							try {
								LIST_METHOD[] methods = DynamicFieldConfig.LIST_METHOD.values();
								Map<String, String> listValeus = getOptions(
										methods[Integer.valueOf(dynmaicFieldConfig.getListMethod())].toString());

								if (DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal() == Integer
										.valueOf(dynmaicFieldConfig.getComponentType())
										&& dynmaicFieldConfig.getFieldValue().contains(",")) {

									obj.put("dispName", listValeus.entrySet().stream()
											.filter(u -> Arrays.asList(dynmaicFieldConfig.getFieldValue().split(","))
													.contains(u.getKey()))
											.map(u -> u.getValue()).collect(Collectors.joining(",")));
								} else {
									obj.put("dispName", listValeus.containsKey(dynmaicFieldConfig.getFieldValue())
											? listValeus.get(dynmaicFieldConfig.getFieldValue()) : "");
								}

							} catch (Exception e) {
								obj.put("dispName", "");
							}
						} else {
							obj.put("dispName", dynmaicFieldConfig.getFieldValue());
						}
					}
					if (dynmaicFieldConfig.getImageIds() != null
							&& !StringUtil.isEmpty(dynmaicFieldConfig.getImageIds())) {
						obj.put("photoCompoAvailable", "1");
						if (dynmaicFieldConfig.getComponentType().equals("13")
								|| dynmaicFieldConfig.getComponentType().equals("11")) {
							obj.put("photoCompoAvailable", "2");
						}
						obj.put("photoIds", dynmaicFieldConfig.getImageIds());
					} else {
						obj.put("photoCompoAvailable", "0");
						obj.put("photoByteStr", "");
					}
					groupList.add(obj);
				}
			});// Date of Illnessbse

			jsonMap.put("fields", fieldList);
			jsonMap.put("groups", groupList);

			
			//objects.putAll(jsonMap);

			//printAjaxResponse(objects, "text/json");

		

		isScore="0";
		List<JSONObject> fieldListRender = new LinkedList<>();
		List<JSONObject> groupListRender = new LinkedList<>();
		List<JSONObject> sectionList = new LinkedList<>();
		List<DynamicSectionConfig> secList = new ArrayList<>();
		//List<DynamicFeildMenuConfig> dyList = farmerService.findDynamicMenusByType(getTxnTypez(),getBranchId());
		List<DynamicConstants> dsc = farmerService.listDynamicConstants();
		cts = dsc.stream().map(u -> u.getCode()).collect(Collectors.toList());
		Map<String,DynamicMenuFieldMap> menMap  = (Map<String,DynamicMenuFieldMap>)dyList.stream()
				.flatMap(listContainer -> listContainer.getDynamicFieldConfigs().stream())
				.collect(Collectors.toList()).stream().collect(Collectors.toMap(u -> u.getField().getCode(), obj -> obj));
		if (dyList != null && !dyList.isEmpty()) {
			isScore = dyList.get(0).getIsScore().toString();
			/*List<DynamicFieldConfig> dynamicFieldsConfigList = (List<DynamicFieldConfig>) dyList.stream()
					.flatMap(listContainer -> listContainer.getDynamicFieldConfigs().stream())
					.collect(Collectors.toList()).stream().map(u -> u.getField()).collect(Collectors.toList());*/
			dyList.stream().forEach(dynamicMenu -> {
				dynamicMenu.getDynamicSectionConfigs().stream().filter(
						u -> (u.getSection().getDynamicFieldConfigs().stream().anyMatch(ui -> actCt.contains(ui.getCode()))))
						.forEach(dynamicSection -> {
							JSONObject obj = new JSONObject();

							obj.put("secCode", dynamicSection.getSection().getSectionCode());
							obj.put("secName",
									StringUtil.isEmpty(getLanguagePref(getLoggedInUserLanguage(),
											dynamicSection.getSection().getSectionCode()))
													? dynamicSection.getSection().getSectionName()
													: getLanguagePref(getLoggedInUserLanguage(),
															dynamicSection.getSection().getSectionCode()));
							obj.put("secOrder", dynamicSection.getSection().getSecorder());
							obj.put("isScore", isScore);
							obj.put("typez", dynamicSection.getSection().getTableId()!=null ?  dynamicSection.getSection().getTableId() : "" );
							sectionList.add(obj);
							secList.add(dynamicSection.getSection());
						});
			});
			
			dynamicFieldsConfigList.stream()
			.filter(dynamicFieldConfig -> actCt.contains(dynamicFieldConfig.getCode())).forEach(dynamicFieldConfig -> {
						if (((Arrays.asList("1", "0").contains(dynamicFieldConfig.getIsMobileAvail()) && !Arrays.asList(4).contains(dynamicFieldConfig.getFollowUp())))) {
							JSONObject obj = new JSONObject();
							if (dynamicFieldConfig.getParentActKey() != null && !StringUtil.isEmpty(dynamicFieldConfig.getParentActKey())
									&& (dynamicFieldConfig.getFollowUp() == 1 || dynamicFieldConfig.getFollowUp() == 2)) {
								dynamicFieldConfig.setParentDependencyKey(dynamicFieldConfig.getParentDependencyKey() != null
										&& !StringUtil.isEmpty(dynamicFieldConfig.getParentDependencyKey())
												? dynamicFieldConfig.getParentDependencyKey() + "," + dynamicFieldConfig.getParentActKey()
												: dynamicFieldConfig.getParentActKey());
							}
							if (dynamicFieldConfig.getParentActKey() != null && !StringUtil.isEmpty(dynamicFieldConfig.getParentActKey())
									&& (dynamicFieldConfig.getFollowUp() == 3)) {
								dynamicFieldConfig.setCatDependencyKey(
										dynamicFieldConfig.getCatDependencyKey() != null && !StringUtil.isEmpty(dynamicFieldConfig.getCatDependencyKey())
												? dynamicFieldConfig.getCatDependencyKey() + "," + dynamicFieldConfig.getParentActKey()
												: dynamicFieldConfig.getParentActKey());
							}
							String depeCode = 
									!ObjectUtil.isEmpty(dynamicFieldConfig.getParentDepen()) ? dynamicFieldConfig.getParentDepen().getCode() : "";
							if (dynamicFieldConfig.getParentActField() != null && !StringUtil.isEmpty(dynamicFieldConfig.getParentActField())
									&& (dynamicFieldConfig.getFollowUp() == 1 || dynamicFieldConfig.getFollowUp() == 2)) {
								depeCode = dynamicFieldConfig.getParentActField();
							}
							
							obj.put("id", dynamicFieldConfig.getId());
							obj.put("secCode", dynamicFieldConfig.getDynamicSectionConfig().getSectionCode());
							obj.put("compoType", dynamicFieldConfig.getComponentType());
							obj.put("compoName", StringUtil
									.isEmpty(getLanguagePref(getLoggedInUserLanguage(), dynamicFieldConfig.getCode()))
											? dynamicFieldConfig.getComponentName()
											: getLanguagePref(getLoggedInUserLanguage(), dynamicFieldConfig.getCode()));
							obj.put("compoCode", dynamicFieldConfig.getCode());
							obj.put("parentDepen", ObjectUtil.isEmpty(dynamicFieldConfig.getParentDepen()) ? ""
									: dynamicFieldConfig.getParentDepen().getId());
							obj.put("parentDepenCode",depeCode);
							obj.put("parentDepenKey", ObjectUtil.isEmpty(dynamicFieldConfig.getParentDependencyKey())
									? "" : dynamicFieldConfig.getParentDependencyKey());
							obj.put("catDepKey", ObjectUtil.isEmpty(dynamicFieldConfig.getCatDependencyKey()) ? ""
									: dynamicFieldConfig.getCatDependencyKey());
							obj.put("maxLen", dynamicFieldConfig.getComponentMaxLength());
							obj.put("isReq", dynamicFieldConfig.getIsRequired());
							obj.put("sectionId", dynamicFieldConfig.getDynamicSectionConfig().getSectionCode());
							obj.put("sectionId", dynamicFieldConfig.getDynamicSectionConfig().getSectionCode());
							obj.put("isOther", dynamicFieldConfig.getIsOther());
							obj.put("isMobile", dynamicFieldConfig.getIsMobileAvail());
							obj.put("isScore", isScore);
							obj.put("typez", dynamicFieldConfig.getDynamicSectionConfig().getTableId()!=null ?  dynamicFieldConfig.getDynamicSectionConfig().getTableId() : "" );
							
							JSONArray cName = new JSONArray();
							JSONArray cType = new JSONArray();

							String ct = dynamicFieldConfig.getCatalogueType();
							if (dynamicFieldConfig.getAccessType() > 0 && dynamicFieldConfig.getAccessType() == 1) {

								Integer type = new Integer(ct);
								Map<String, String> farmCatalougeList = getFarmCatalougeMap(type);
								for (Map.Entry<String, String> entry : farmCatalougeList.entrySet()) {
									cType.add(entry.getKey());
									cName.add(entry.getValue());

								}
							} else if (dynamicFieldConfig.getAccessType() > 0
									&& dynamicFieldConfig.getAccessType() == 2) {
								// String ct =
								// dynamicFieldConfig.getCatalogueType();
								if (!StringUtil.isEmpty(ct)) {
									for (String val : ct.split(",")) {
										if ((val.contains("CG"))) {
											cType.add(val);
											FarmCatalogue fc = getCatlogueValueByCode(val.trim());
											cName.add(fc.getName());
										} else {
											Integer type = new Integer(val);
											Map<String, String> farmCatalougeList = getFarmCatalougeMap(type);
											for (Map.Entry<String, String> entry : farmCatalougeList.entrySet()) {
												cType.add(entry.getKey());
												cName.add(entry.getValue());

											}

										}
									}
								}
							} else if (dynamicFieldConfig.getAccessType() > 0
									&& dynamicFieldConfig.getAccessType() == 3) {
								LIST_METHOD[] methods = DynamicFieldConfig.LIST_METHOD.values();
								try {
									for (Map.Entry<String, String> entry : getOptions(
											methods[Integer.valueOf(ct)].toString()).entrySet()) {
										cType.add(entry.getKey());
										cName.add(entry.getValue());

									}
								} catch (Exception e) {

								}
							}
							obj.put("catalogueType", cType);
							obj.put("catalogueName", cName);
							obj.put("accessType", dynamicFieldConfig.getAccessType());
							obj.put("dataFormat", dynamicFieldConfig.getDataFormat());

							obj.put("beforeInsert", menMap.get(dynamicFieldConfig.getCode()).getBeforeInsert());
							obj.put("afterInsert", menMap.get(dynamicFieldConfig.getCode()).getAfterInsert());
							obj.put("validation", dynamicFieldConfig.getValidation());
							obj.put("defaultVal", dynamicFieldConfig.getDefaultValue());
							formula = "";
							if (dynamicFieldConfig.getDependencyKey() != null
									&& !StringUtil.isEmpty(dynamicFieldConfig.getDependencyKey())) {
								List<DynamicFieldConfig> formulaList = dynamicFieldsConfigList.stream()
										.filter(forumula -> Arrays
												.asList(dynamicFieldConfig.getDependencyKey().contains(",")
														? dynamicFieldConfig.getDependencyKey().split(",")
														: dynamicFieldConfig.getDependencyKey())
												.contains(forumula.getCode()))
										.collect(Collectors.toList());
								if (formulaList != null && !formulaList.isEmpty()) {
									formula = formulaList.stream().map(DynamicFieldConfig::getFormula)
											.collect(Collectors.joining(","));
								}
							}
							obj.put("constExist", "0");
							if (StringUtil.isEmpty(formula) || formula.equals("null") || formula.equals("")) {

								obj.put("formula", dynamicFieldConfig.getFormula());
							} else {

								if (cts != null && !cts.stream().filter(u -> formula.contains(u))
										.collect(Collectors.toList()).isEmpty()) {
									obj.put("constExist", "1");
								}

								obj.put("formula", formula);
							}

							obj.put("dependencyKey", dynamicFieldConfig.getDependencyKey());
							obj.put("orderSet", dynamicFieldConfig.getOrderSet());
							fieldListRender.add(obj);
						}
					});

			dynamicFieldsConfigList.stream().filter(dynamicFieldConfig -> !ObjectUtil
					.isEmpty(dynamicFieldConfig.getReferenceId())
					&& StringUtil.isLong(dynamicFieldConfig.getReferenceId())
			&& Arrays.asList(actCt).contains(dynamicFieldConfig.getCode()))
					.forEach(dynamicFieldConfig -> {
						if (((Arrays.asList("1", "0").contains(dynamicFieldConfig.getIsMobileAvail()) && !Arrays.asList(4).contains(dynamicFieldConfig.getFollowUp()))
								|| ((queryType != null && queryType.equals("1")) && !Arrays.asList(1,2,4).contains(dynamicFieldConfig.getFollowUp())))) {
					
						JSONObject obj = new JSONObject();
						if (dynamicFieldConfig.getParentActKey() != null && !StringUtil.isEmpty(dynamicFieldConfig.getParentActKey())
								&& (dynamicFieldConfig.getFollowUp() == 1 || dynamicFieldConfig.getFollowUp() == 2)) {
							dynamicFieldConfig.setParentDependencyKey(dynamicFieldConfig.getParentDependencyKey() != null
									&& !StringUtil.isEmpty(dynamicFieldConfig.getParentDependencyKey())
											? dynamicFieldConfig.getParentDependencyKey() + "," + dynamicFieldConfig.getParentActKey()
											: dynamicFieldConfig.getParentActKey());
						}
						if (dynamicFieldConfig.getParentActKey() != null && !StringUtil.isEmpty(dynamicFieldConfig.getParentActKey())
								&& (dynamicFieldConfig.getFollowUp() == 3)) {
							dynamicFieldConfig.setCatDependencyKey(
									dynamicFieldConfig.getCatDependencyKey() != null && !StringUtil.isEmpty(dynamicFieldConfig.getCatDependencyKey())
											? dynamicFieldConfig.getCatDependencyKey() + "," + dynamicFieldConfig.getParentActKey()
											: dynamicFieldConfig.getParentActKey());
						}
						String depeCode = 
								!ObjectUtil.isEmpty(dynamicFieldConfig.getParentDepen()) ? dynamicFieldConfig.getParentDepen().getCode() : "";
						if (dynamicFieldConfig.getParentActField() != null && !StringUtil.isEmpty(dynamicFieldConfig.getParentActField())
								&& (dynamicFieldConfig.getFollowUp() == 1 || dynamicFieldConfig.getFollowUp() == 2)) {
							depeCode = dynamicFieldConfig.getParentActField();
						}
						
						
						obj.put("id", dynamicFieldConfig.getId());
						obj.put("secCode", dynamicFieldConfig.getDynamicSectionConfig().getSectionCode());
						
						obj.put("compoType", dynamicFieldConfig.getComponentType());
						obj.put("compoName", StringUtil
								.isEmpty(getLanguagePref(getLoggedInUserLanguage(), dynamicFieldConfig.getCode()))
										? dynamicFieldConfig.getComponentName()
										: getLanguagePref(getLoggedInUserLanguage(), dynamicFieldConfig.getCode()));
						obj.put("compoCode", dynamicFieldConfig.getCode());
						obj.put("parentDepen", ObjectUtil.isEmpty(dynamicFieldConfig.getParentDepen()) ? ""
								: dynamicFieldConfig.getParentDepen().getId());
						obj.put("parentDepenCode",depeCode);
						obj.put("parentDepenKey", ObjectUtil.isEmpty(dynamicFieldConfig.getParentDependencyKey()) ? ""
								: dynamicFieldConfig.getParentDependencyKey());
						obj.put("catDepKey", ObjectUtil.isEmpty(dynamicFieldConfig.getCatDependencyKey()) ? ""
								: dynamicFieldConfig.getCatDependencyKey());
						obj.put("isOther", dynamicFieldConfig.getIsOther());
						obj.put("maxLen", dynamicFieldConfig.getComponentMaxLength());
						obj.put("isReq", dynamicFieldConfig.getIsRequired());
						obj.put("sectionId", dynamicFieldConfig.getDynamicSectionConfig().getSectionCode());
						obj.put("isScore", isScore);
						obj.put("typez", dynamicFieldConfig.getDynamicSectionConfig().getTableId()!=null ?  dynamicFieldConfig.getDynamicSectionConfig().getTableId() : "" );
						
						obj.put("isMobile", dynamicFieldConfig.getIsMobileAvail());
						JSONArray cName = new JSONArray();
						JSONArray cType = new JSONArray();

						String ct = dynamicFieldConfig.getCatalogueType();
						if (dynamicFieldConfig.getAccessType() > 0 && dynamicFieldConfig.getAccessType() == 1) {

							Integer type = new Integer(ct);
							Map<String, String> farmCatalougeList = getFarmCatalougeMap(type);
							for (Map.Entry<String, String> entry : farmCatalougeList.entrySet()) {
								cType.add(entry.getKey());
								cName.add(entry.getValue());

							}
						} else if (dynamicFieldConfig.getAccessType() > 0 && dynamicFieldConfig.getAccessType() == 1) {
							// String ct =
							// dynamicFieldConfig.getCatalogueType();
							if (!StringUtil.isEmpty(ct)) {
								for (String val : ct.split(",")) {
									if ((val.contains("CG"))) {
										cType.add(val);
										FarmCatalogue fc = getCatlogueValueByCode(val.trim());
										cName.add(fc.getName());
									} else {
										Integer type = new Integer(val);
										Map<String, String> farmCatalougeList = getFarmCatalougeMap(type);
										for (Map.Entry<String, String> entry : farmCatalougeList.entrySet()) {
											cType.add(entry.getKey());
											cName.add(entry.getValue());

										}

									}
								}
							}
						} else if (dynamicFieldConfig.getAccessType() > 0 && dynamicFieldConfig.getAccessType() == 3) {
							LIST_METHOD[] methods = DynamicFieldConfig.LIST_METHOD.values();
							try {
								for (Map.Entry<String, String> entry : getOptions(
										methods[Integer.valueOf(ct)].toString()).entrySet()) {
									cType.add(entry.getKey());
									cName.add(entry.getValue());

								}
							} catch (Exception e) {

							}
						}
						obj.put("catalogueType", cType);
						obj.put("catalogueName", cName);
						obj.put("accessType", dynamicFieldConfig.getAccessType());
						obj.put("dataFormat", dynamicFieldConfig.getDataFormat());
						obj.put("beforeInsert", menMap.get(dynamicFieldConfig.getCode()).getBeforeInsert());
						obj.put("afterInsert", menMap.get(dynamicFieldConfig.getCode()).getAfterInsert());
						obj.put("validation", dynamicFieldConfig.getValidation());
						obj.put("defaultVal", dynamicFieldConfig.getDefaultValue());
						formula = "";
						if (dynamicFieldConfig.getDependencyKey() != null
								&& !StringUtil.isEmpty(dynamicFieldConfig.getDependencyKey())) {
							List<DynamicFieldConfig> formulaList = dynamicFieldsConfigList.stream()
									.filter(forumula -> Arrays
											.asList(dynamicFieldConfig.getDependencyKey().contains(",")
													? dynamicFieldConfig.getDependencyKey().split(",")
													: dynamicFieldConfig.getDependencyKey())
											.contains(forumula.getCode()))
									.collect(Collectors.toList());
							if (formulaList != null && !formulaList.isEmpty()) {
								formula = formulaList.stream().map(DynamicFieldConfig::getFormula)
										.collect(Collectors.joining(","));
							}
						}
						obj.put("constExist", "0");
						if (StringUtil.isEmpty(formula) || formula.equals("null") || formula.equals("")) {
							obj.put("formula", dynamicFieldConfig.getFormula());
						} else {
							if (cts != null && !cts.stream().filter(u -> formula.contains(u))
									.collect(Collectors.toList()).isEmpty()) {
								obj.put("constExist", "1");
							}

							obj.put("formula", formula);
						}
						obj.put("dependencyKey", dynamicFieldConfig.getDependencyKey());
						obj.put("refId", dynamicFieldConfig.getReferenceId());
						groupListRender.add(obj);
						}
					});
		}

		jsonMap.put("sections", sectionList);
		jsonMap.put("fieldsRender", fieldListRender);
		jsonMap.put("groupsRender", groupListRender);

		JSONObject objects = new JSONObject();
		objects.putAll(jsonMap);

		printAjaxResponse(objects, "text/json");

	}
	}
	

	protected void updateDynamicFieldsWithActPlan(String txnType, String referenceId, String seasonCode, String entityType) {
		// if (!StringUtil.isEmpty(dynamicFieldsArray) &&
		// dynamicFieldsArray.length() > 0) {
		Map<String, List<FarmerDynamicFieldsValue>> fdMap = new HashMap<>();
		farmerDynamicData = new FarmerDynamicData();
		List<DynamicFeildMenuConfig> dm = farmerService.findDynamicMenusByType(txnType);
/*dm.stream().filter(u-> u.getIsScore()==1||u.getIsScore()==2||u.getIsScore()==3).forEach( u->{
	farmerDynamicData.setIsScore(u.getIsScore());
	farmerDynamicData.setScoreValue(new HashMap<>());
});*/
		farmerDynamicData = farmerService.findFarmerDynamicData(txnType, String.valueOf(referenceId));
		dm.stream().flatMap(listContainer -> listContainer.getDynamicFieldConfigs().stream())
				.collect(Collectors.toList()).stream().forEach(section -> {
					section.getField().setfOrder(section.getOrder());
					fieldConfigMap.put(section.getField().getCode(), section.getField());
					/*if (section.getDynamicFieldScoreMap() != null && !section.getDynamicFieldScoreMap().isEmpty()) {
						farmerDynamicData.getScoreValue().put(section.getField().getCode(), section
								.getDynamicFieldScoreMap().stream()
								.collect(Collectors.toMap(DynamicFieldScoreMap::getCatalogueCode, u -> String.valueOf(
										String.valueOf(u.getScore()) + "~" + String.valueOf(u.getPercentage())))));
					}*/

				});
		if (farmerDynamicData != null) {
			fdMap = farmerDynamicData.getFarmerDynamicFieldsValues().stream()
					.collect(Collectors.groupingBy(FarmerDynamicFieldsValue::getFieldName));
			//farmerDynamicData.getFarmerDynamicFieldsValues().clear();
			Set<FarmerDynamicFieldsValue> fdvLi = editFarmerDynamicFieldsSet(fdMap, farmerDynamicData, referenceId);
			farmerDynamicData.getFarmerDynamicFieldsValues().removeIf(u -> fdvLi.stream().map(ff -> ff.getFieldName()).collect(Collectors.toList()).contains(u.getFieldName()));
			farmerDynamicData
					.getFarmerDynamicFieldsValues().addAll(fdvLi);
			farmerDynamicData.setActStatus(1);
		} else {
			farmerDynamicData = new FarmerDynamicData();
			farmerDynamicData.setFarmerDynamicFieldsValues(addFarmerDynamicFieldsSet(referenceId));
			// farmerDynamicData.setTxnUniqueId(Long.valueOf(head.getMsgNo()));
			farmerDynamicData.setReferenceId(String.valueOf(referenceId));
			farmerDynamicData.setTxnType(txnType);
			farmerDynamicData.setDate(new Date(0));
			farmerDynamicData.setCreatedDate(new Date(0));
			farmerDynamicData.setCreatedUser(getUsername());
			farmerDynamicData.setStatus("0");
			farmerDynamicData.setBranch(getBranchId());
			farmerDynamicData.setEntityId("1");
			farmerDynamicData.setSeason(seasonCode);

		}
		fdMap = farmerDynamicData.getFarmerDynamicFieldsValues().stream()
				.collect(Collectors.groupingBy(FarmerDynamicFieldsValue::getFieldName));
		farmerService.saveOrUpdate(farmerDynamicData, fdMap, fieldConfigMap);
		if (fieldConfigMap.values().stream().anyMatch(p -> Arrays.asList("4").contains(p.getIsMobileAvail())
				&& p.getFormula() != null && !StringUtil.isEmpty(p.getFormula()))) {
			farmerService.processCustomisedFormula(farmerDynamicData, fieldConfigMap,new HashMap<>());
		}
		/* farmerService.saveOrUpdate(farmerDynamicData); */
		farmerService.deleteChildObjects(farmerDynamicData.getTxnType());

		// }
	}
	
	private static Set<Vendor> vendorSet = new HashSet<>();
	@SuppressWarnings("unchecked")
	protected Set<Vendor> getUserVendorMap() {
		if (ObjectUtil.isEmpty(request)) {
			request = ReflectUtil.getCurrentHttpRequest();
		}
		
	/*	if(vendorSet.size() == 0){*/
			vendorSet = (Set<Vendor>) request.getSession().getAttribute(ISecurityFilter.USER_VENDOR_MAP); 
			if(!ObjectUtil.isListEmpty(vendorSet)){
				return vendorSet;
			}else{
				vendorSet = new HashSet<>();
			}
	/*	}*/
		
		return vendorSet;
	}
	
	public String populateFileDownload() {
	DynamicImageData pmtImg = null;
	if (!StringUtil.isEmpty(imgFileId) && StringUtil.isLong(imgFileId))
		pmtImg = farmerService.findDynamicImageDataById(Long.valueOf(imgFileId));
	byte[] image = null;
	String[] documentNameInfo = null;
	String extension;
	String fName="";
		image =  pmtImg.getImage();
		
		//documentNameInfo = pmtImg.getIdProofImgName().split("\\.");
		extension = pmtImg.getFileExt();
		fName="img"+"."+extension;
	//response.setContentType("multipart/form-data");
	response.setContentType("multipart/form-data");
	response.setHeader("Content-Disposition",
			"attachment;filename="+fName.replace(' ', '_'));
	try {
		java.io.OutputStream out=response.getOutputStream();
		out.write(image);
		out.flush();
		out.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return FILTER_ALL;
	}

	public String getImgFileId() {
		return imgFileId;
	}

	public void setImgFileId(String imgFileId) {
		this.imgFileId = imgFileId;
	}
	public Map<String, String> getListLotCode() {
		return farmerService.listValueByFieldName(getLocaleProperty("lotCode.fieldName"),getBranchId()).parallelStream()
				.filter(fil -> fil != null && !ObjectUtil.isEmpty(fil) && fil[2] != null && fil[1] != null)
				.collect(Collectors.toMap(id -> String.valueOf(id[1]), val -> String.valueOf(val[1])));
	}
	
	public void populateDepListMethos(){
		JSONArray objects = new JSONArray();
		
		if(postdata!=null && !StringUtil.isEmpty(postdata) && !postdata.equalsIgnoreCase("undefined") && postdata.contains("~") ){
			
		
			String listMet =postdata.split("~")[0].toString();
			String valuee =postdata.split("~")[1].toString();
			if(listMet.equals("9")){
				List<Object[]> pvlIst = productService.listProcurementVarietyByCode(valuee);
				pvlIst.stream().forEach(uu ->{
					JSONObject jss = new JSONObject();
					jss.put("id",uu[0].toString());
					jss.put("name",uu[1].toString());
				
					objects.add(jss);
				});
				
			}else if(listMet.equals("10")){
				List<Object[]> pvlIst = productService.listProcurmentGradeByVarityCode(valuee);
				pvlIst.stream().forEach(uu ->{
					JSONObject jss = new JSONObject();
					jss.put("id",uu[0].toString());
					jss.put("name",uu[1].toString());
					objects.add(jss);
				});
				
			}
			
		}
		
		

		printAjaxResponse(objects, "text/json");
	}
	
	public void setMicrosoftTranslatorParams(){
		MSA_TRANSLATOR_TEXT_SUBSCRIPTION_KEY = preferncesService.findPrefernceByName(MS_TRANSLATOR_TEXT_SUBSCRIPTION_KEY_PREF_VAL);
		MSA_TRANSLATOR_TEXT_ENDPOINT = preferncesService.findPrefernceByName(MS_TRANSLATOR_TEXT_ENDPOINT_PREF_VAL);
	}
	
	
	public String msTextTranslator(String srcText, String fromLang, String toLang) {

		String translatedText = "";
		String msTranslatedText = "";
		try {
			setMicrosoftTranslatorParams();
			translatedText = MicrosoftTranslatorAPI.translateText(MSA_TRANSLATOR_TEXT_SUBSCRIPTION_KEY,
					MSA_TRANSLATOR_TEXT_ENDPOINT, fromLang, toLang, srcText);
			if (!StringUtil.isEmpty(translatedText)) {
				msTranslatedText = MicrosoftTranslatorAPI.msTranslatedWord(translatedText);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.print(msTranslatedText);
		return msTranslatedText;
	}
}
