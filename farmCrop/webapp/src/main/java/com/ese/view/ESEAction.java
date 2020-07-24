/*
 * ESEAction.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.ILocalizable;
import com.ese.entity.util.ESESystem;
import com.opensymphony.xwork2.ActionContext;
import com.sourcetrace.eses.BreadCrumb;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IReportService;
import com.sourcetrace.eses.service.IUserService;
import com.sourcetrace.eses.umgmt.entity.Menu;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.view.SwitchAction;

public class ESEAction extends SwitchAction implements IESEAction {
	protected static final String CREATE = "create";
	protected static final String DETAIL = "detail";
	protected static final String UPDATE = "update";
	protected static final String DELETE = "delete";
	protected static final String LIST = "list";
	protected static final String TITLE_PREFIX = "title.";
	protected static final String HEADING = "heading";
	protected static final String EMPTY = "";
	protected static final String ROWS = "rows";
	protected static final String RECORDS = "records";

	/** The Constant PAGE_NUMBER. */
	public static final String PAGE_NUMBER = "pagenumber";
	protected static final String TOTAL = "total";

	/** The Constant PAGE. */
	public static final String PAGE = "page";

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

	/** The Constant NEW_LINE. */
	public static final String NEW_LINE = "\n";

	/** The Constant DELIMITER. */
	public static final String DELIMITER = ",";

	/** The DAT e_ format. */
	public static String DATE_FORMAT;

	/** The DAT e_ tim e_ format. */
	public static String DATE_TIME_FORMAT;

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected ServletContext context;
	protected IReportService reportService;
	private int startIndex;
	private int results;
	private int page;

	private String sort;
	private String dir;

	private String queryType;
	private String queryName;

	/** The Constant EXPORT_CSV. */
	public final static String EXPORT_CSV = "csv";

	/** The Constant EXPORT_PDF. */
	public final static String EXPORT_PDF = "pdf";
	private String name;
	private InputStream inputStream;
	private String description;
	private long size;
	private DateFormat eseExportFileFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private String parentBranch;

	protected Map<String, String> branchesMap = new LinkedHashMap<String, String>();
	@Autowired
	private IPreferencesService preferncesService;
	@Autowired
	protected ICatalogueService catalogueService;
	private Map<String, FarmCatalogue> catalogueMap = new LinkedHashMap<>();

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
	 * Gets the username.
	 * 
	 * @return the username
	 */
	public String getUsername() {

		String username = (String) request.getSession().getAttribute("user");

		if (username == null) {
			username = "";
		}
		return username;
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

	protected String sendJSONResponse(Map data) throws Exception {

		JSONObject gridData = new JSONObject();
		gridData.put(RECORDS, data.get(RECORDS));

		List list = (List) data.get(ROWS);
		JSONArray rows = new JSONArray();
		if (list != null) {
			for (Object record : list) {
				rows.add(toJSON(record));
			}
		}

		gridData.put(ROWS, rows);

		PrintWriter out = response.getWriter();
		out.println(gridData.toString());
		return null;
	}

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

	protected String sendResponse(List<?> populateResponce) throws Exception {

		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			out.print(populateResponce);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String getBranchId() {
		if (ObjectUtil.isEmpty(request)) {
			request = ReflectUtil.getCurrentHttpRequest();
		}
		Object biObj = ObjectUtil.isEmpty(request) ? null
				: request.getSession().getAttribute(ISecurityFilter.CURRENT_BRANCH);
		return ObjectUtil.isEmpty(biObj) ? null : biObj.toString();
	}

	public void sendAjaxResponse(JSONObject jsonObject) {

		try {
			response.setContentType("text/JSON");
			PrintWriter out = response.getWriter();
			out.println(jsonObject.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void buildBranchMap() {
		List<Object[]> branchMasters = getBranchesInfo();
		branchesMap = new LinkedHashMap<String, String>();
		branchesMap.put(null, getMainBranchName());
		for (Object[] objects : branchMasters) {
			branchesMap.put(objects[0].toString(), objects[1].toString());
		}
	}

	protected Object[] getBranchInfo(String branchId) {
		return clientService.findBranchInfo(branchId);
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

	protected List<Object[]> getBranchesInfo() {
		List<Object[]> branchMasters = clientService.listBranchMastersInfo();
		return branchMasters;
	}

	public String getCurrentMenu() {

		return getText("menu.select");
	}

	public String getMainBranchName() {
		if (ObjectUtil.isEmpty(request)) {
			request = ReflectUtil.getCurrentHttpRequest();
		}
		Object biObj = ObjectUtil.isEmpty(request) ? null
				: request.getSession().getAttribute(ESESystem.MAIN_BRANCH_NAME);
		return ObjectUtil.isEmpty(biObj) ? "" : biObj.toString();
	}

	public String getLocaleProperty(String prop) {

		String result = getText(prop);
		String language = getCurrentLanguage();
		String locProp = getDBProperty(prop, language);

		return StringUtil.isEmpty(locProp) ? result : locProp;
	}

	/*
	 * public String convertCurrency(String currency) { String prefCurrency =
	 * preferncesService.findPrefernceByName(ESESystem.CURRENCY_TYPE); if
	 * (!StringUtil.isEmpty(prefCurrency) && prefCurrency.equals("1")) {
	 * currency = currency.trim().replace(ESESystem.INDIA_CURRENCY,
	 * ESESystem.DOLLAR_CURRENCY); }
	 * 
	 * return currency;
	 * 
	 * }
	 * 
	 * public String getCurrency() { String prefCurrency =
	 * preferncesService.findPrefernceByName(ESESystem.CURRENCY_TYPE); if
	 * (!StringUtil.isEmpty(prefCurrency) && prefCurrency.equals("1")) {
	 * prefCurrency = ESESystem.DOLLAR_CURRENCY; } else { prefCurrency =
	 * ESESystem.INDIA_CURRENCY; } return prefCurrency;
	 * 
	 * }
	 */

	public String getDBProperty(String prop, String language) {
		String locProp = clientService.findLocaleProperty(prop, language);
		return locProp;
	}

	public String getCurrentLanguage() {
		return (String) request.getSession().getAttribute(ISecurityFilter.LANGUAGE);
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

	public FarmCatalogue getCatlogueValueByCode(String code) {
		FarmCatalogue catValue = new FarmCatalogue();
		if (catalogueMap.size() <= 0) {
			catalogueService.listCatalogues().stream().forEach(fc -> {
				if (!catalogueMap.containsKey(fc.getCode())) {
					catalogueMap.put(fc.getCode(), fc);
				}
			});

		}
		if (catalogueMap.containsKey(code)) {
			catValue = catalogueMap.get(code);
			if (catValue == null || ObjectUtil.isEmpty(catValue)) {
				catValue = new FarmCatalogue();
				catValue.setName("");
			}

		}
		return catValue;
	}

	public Map<String, String> getFarmCatalougeMap(Integer type) {
		List<FarmCatalogue> farmCatalougeList = catalogueService.findFarmCatalougeByType(type);
		Map<String, String> catList = new LinkedHashMap<>();
		Class clazz = FarmCatalogue.class;
		Field field = null;
		try {
			field = clazz.getDeclaredField(FarmCatalogue.NAME);

		} catch (NoSuchFieldException e1) {

		} catch (SecurityException e1) {

		}
		for (FarmCatalogue fc : farmCatalougeList) {
			if (!catList.containsKey(fc.getCode()))
				try {
					catList.put(fc.getCode(),
							field != null && field.get(fc) != null
									? field.get(fc) != null && !StringUtil.isEmpty(field.get(fc))
											? field.get(fc).toString() : fc.getName()
									: fc.getName());
				} catch (Exception e) {
					catList.put(fc.getCode(), "");
				}
		}
		return catList;

	}

}
