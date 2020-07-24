package com.sourcetrace.esesw.view.report.agro;

import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.ietf.jgss.GSSException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sourcetrace.eses.BreadCrumb;
import com.sourcetrace.eses.dao.IReportDAO;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfig;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfigDetail;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfigFilter;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.util.Base64Util;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicData;
import com.sourcetrace.esesw.view.BaseReportAction;

public class DynamicViewReportDTAction extends BaseReportAction {
	private String daterange;
	private String mainGridCols;
	private String mainGridColNames;
	private String filterList;
	private String gridIdentity;
	private Object fValue;
	private Object mValue;
	private String expression_result;
	private static DynamicReportConfig dynamicReportConfig;
	private Object filter;
	private List<String> fvalueByParameters = new ArrayList<String>();
	Map<Integer, Long> dynamic_report_config_detail_ID = new HashMap<Integer, Long>();
	private List<DynamicReportConfigFilter> reportConfigFilters = new LinkedList<>();
	Map entityMap = new HashMap<>();
	Map otherMap = new HashMap<>();
	Map<String, Map> mainMap = new HashMap<>();
	private String footerSumCols;
	private String footerTotCol;
	private String id;
	String tothead;
	@Autowired
	private IPreferencesService preferncesService;
	@Autowired
	private IProductService productService;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private IAgentService agentService;
	@Autowired
	private IFarmerService farmerService;
	private List<Object[]> agentList;
	private String fetchType;
	private static DynamicReportConfig subDynamicReportConfig;
	private static Map<String, Map<String, String>> fieldValueMap;
	private String subGridCols;
	private String subGridColNames;
	private List<Object[]> productInfoList;
	private Map<String, String> seasonsList;
	private ESESystem preferences;
	DecimalFormat df = new DecimalFormat("0.00");
	private Long branId = 0l;
	private Long idd = 0l;
	private String branchIdParma;

	public String getSubGridColNames() {
		return subGridColNames;
	}

	public void setSubGridColNames(String subGridColNames) {
		this.subGridColNames = subGridColNames;
	}

	public String list() {
		Calendar currentDate = Calendar.getInstance();
		Calendar cal = (Calendar) currentDate.clone();
		cal.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) - 1);
		DateFormat df = new SimpleDateFormat(DateUtil.DATABASE_DATE_FORMAT);
		super.startDate = df.format(cal.getTime());
		super.endDate = df.format(currentDate.getTime());
		setDaterange(super.startDate + " - " + super.endDate);
		request.setAttribute(HEADING, getText(LIST));
		fieldValueMap = new HashMap<>();
		ESESystem preferences = preferncesService.findPrefernceById("1");

		/*
		 * if (getCurrentTenantId().equalsIgnoreCase(ESESystem.KPF_TENANT_ID))
		 */
		if (!StringUtil.isEmpty(id)) {
			formMainGridCols();
			setFetchType(String.valueOf(dynamicReportConfig.getFetchType()));
			if (!ObjectUtil.isEmpty(dynamicReportConfig)
					&& !ObjectUtil.isListEmpty(dynamicReportConfig.getDynmaicReportConfigFilters())) {
				setFilterSize("");

				dynamicReportConfig.getDynmaicReportConfigFilters().stream().forEach(reportConfigFilter -> {
					if (reportConfigFilter.getMethod() != null && !StringUtil.isEmpty(reportConfigFilter.getMethod())
							&& reportConfigFilter.getType() != null && reportConfigFilter.getType() == 3) {
						Map<String, String> optionMap = (Map<String, String>) getMethodValue(
								reportConfigFilter.getMethod(), null);
						reportConfigFilter.setOptions(optionMap);
					} else if (reportConfigFilter.getMethod() != null
							&& !StringUtil.isEmpty(reportConfigFilter.getMethod())
							&& reportConfigFilter.getType() != null && reportConfigFilter.getType() == 5) {
						Map<String, String> optionMap = (Map<String, String>) getQueryForFilters(
								reportConfigFilter.getMethod(), null);
						reportConfigFilter.setOptions(optionMap);
					}
					setFilterSize(getFilterSize() + "," + reportConfigFilter.getLabel());
					reportConfigFilters.add(reportConfigFilter);
				});

				setFilterSize(getFilterSize().trim().substring(1));
			}
		}
		return LIST;
	}

	private void buildFieldMap(Set<DynamicReportConfigDetail> dynmaicReportConfigDetails) {
		if (fieldValueMap == null || fieldValueMap.isEmpty()) {
			fieldValueMap = new HashMap<>();
			dynmaicReportConfigDetails.stream().filter(u -> Arrays.asList(2l, 7l, 8l).contains(u.getAccessType()))
					.forEach(reportConfigFilter -> {
						if (reportConfigFilter.getAccessType() == 2l) {
							Map<String, String> optionMap = (Map<String, String>) getMethodValue(
									reportConfigFilter.getMethod(), null);
							fieldValueMap.put(reportConfigFilter.getField(), optionMap);
						} else if (reportConfigFilter.getAccessType() == 7l) {
							Map<String, String> optionMap = (Map<String, String>) getQueryForFilters(
									reportConfigFilter.getMethod(), null);
							fieldValueMap.put(reportConfigFilter.getField(), optionMap);
						} else if (reportConfigFilter.getAccessType() == 8l) {
							Map<String, String> optionMap = new Gson().fromJson(reportConfigFilter.getMethod(),
									new TypeToken<HashMap<String, String>>() {
									}.getType());
							fieldValueMap.put(reportConfigFilter.getField(), optionMap);
						}
					});
		}
	}

	int i = 0;

	private void formMainGridCols() {
		dynamicReportConfig = clientService.findReportById(id);
		if (!ObjectUtil.isEmpty(dynamicReportConfig)) {
			mainGridCols = "";
			mainGridColNames = "";
			footerSumCols = "";
			footerTotCol = "";
			dynamicReportConfig.getDynmaicReportConfigDetails()
					.remove(dynamicReportConfig.getDynmaicReportConfigDetails().iterator().next());
			if (!StringUtil.isEmpty(getBranchId())) {
				dynamicReportConfig.getDynmaicReportConfigDetails()
						.remove(dynamicReportConfig.getDynmaicReportConfigDetails().iterator().next());

			}

			dynamicReportConfig.getDynmaicReportConfigDetails().stream()
					.filter(config -> config.getIsGridAvailabiltiy())
					.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
					.forEach(dynamicReportConfigDetail -> {
						if (dynamicReportConfigDetail.getLabelName().contains("@")) {
							String label = dynamicReportConfigDetail.getLabelName();
							mainGridCols += getLocaleProperty(label) + " (" + getCurrencyType() + ")" + "#"
									+ dynamicReportConfigDetail.getWidth() + "#"
									+ dynamicReportConfigDetail.getAlignment() + "#"
									+ dynamicReportConfigDetail.getLabelName() + "#" + dynamicReportConfigDetail.getId()
									+ "%";
							mainGridColNames += dynamicReportConfigDetail.getField() + " (" + getCurrencyType() + ")"
									+ "#" + dynamicReportConfigDetail.getWidth() + "#"
									+ dynamicReportConfigDetail.getAlignment() + "%";
						} else {
							mainGridCols += getLocaleProperty(dynamicReportConfigDetail.getLabelName()) + "#"
									+ dynamicReportConfigDetail.getWidth() + "#"
									+ dynamicReportConfigDetail.getAlignment() + "#"
									+ dynamicReportConfigDetail.getLabelName() + "#" + dynamicReportConfigDetail.getId()
									+ "%";
							mainGridColNames += dynamicReportConfigDetail.getField() + "#"
									+ dynamicReportConfigDetail.getWidth() + "#"
									+ dynamicReportConfigDetail.getAlignment() + "%";
						}

					});
			i = 0;
			dynamicReportConfig.getDynmaicReportConfigDetails().stream()
					.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder())).forEach(dy -> {
						if (dy.getIsGridAvailabiltiy() && dy.getIsFooterSum() != null
								&& (dy.getIsFooterSum().equals("1") || dy.getIsFooterSum().equals("3"))) {
							footerSumCols += i + "#";

						}
						i++;
					});

			if (!footerSumCols.isEmpty())
				footerSumCols = StringUtil.removeLastChar(footerSumCols.trim(), '#');

			dynamicReportConfig.getDynmaicReportConfigDetails().stream().filter(
					f -> f.getIsGridAvailabiltiy() && f.getIsFooterSum() != null && f.getIsFooterSum().equals("2"))
					.forEach(dy -> {
						footerTotCol = dy.getLabelName();
					});
		}
		if (dynamicReportConfig.getSubGrid() != null && dynamicReportConfig.getSubGrid().size() > 0) {
			subDynamicReportConfig = dynamicReportConfig.getSubGrid().iterator().next();

			if (!ObjectUtil.isEmpty(subDynamicReportConfig)) {
				subGridCols = "";
				subDynamicReportConfig.getDynmaicReportConfigDetails().stream()
						.filter(config -> config.getIsGridAvailabiltiy())
						.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
						.forEach(dynamicReportConfigDetail -> {
							if (dynamicReportConfigDetail.getLabelName().contains("@")) {
								String label = dynamicReportConfigDetail.getLabelName();
								subGridCols += getLocaleProperty(label) + " (" + getCurrencyType() + ")" + "#"
										+ dynamicReportConfigDetail.getWidth() + "#"
										+ dynamicReportConfigDetail.getAlignment() + "%";
							} else {
								subGridCols += getLocaleProperty(dynamicReportConfigDetail.getLabelName()) + "#"
										+ dynamicReportConfigDetail.getWidth() + "#"
										+ dynamicReportConfigDetail.getAlignment() + "%";
								subGridColNames += dynamicReportConfigDetail.getField() + "#"
										+ dynamicReportConfigDetail.getWidth() + "#"
										+ dynamicReportConfigDetail.getAlignment() + "%";
							}
						});
			}
		}

	}

	String footersum = "";

	protected String sendJQGridJSONResponse(Map data, boolean isSibGrid) throws Exception {
		JSONArray js = new JSONArray();
		JSONObject gridData = new JSONObject();
		List list = (List) data.get(ROWS);
		Object objj = data.get(RECORDS);
		footersum = "";
		if (objj instanceof JSONObject) {
			JSONObject countart = (JSONObject) objj;
			totalRecords = (Integer) countart.get("count");
			if (countart.containsKey("footers")) {
				JSONObject footers = (JSONObject) countart.get("footers");
				if (!footers.isEmpty()) {
					footers.keySet().stream().forEach(u -> {
						footersum = footersum + getLocaleProperty(u.toString()) + " : " + footers.get(u).toString()
								+ "       ";
					});

					gridData.put("footersum", footersum);
				}
			}
		} else {
			totalRecords = (Integer) data.get(RECORDS);
		}
		JSONArray rows = new JSONArray();
		if (list != null) {
			branchIdValue = getBranchId();
			if (StringUtil.isEmpty(branchIdValue)) {
				buildBranchMap();
			}
			for (Object record : list) {

				if (getGridIdentity().equalsIgnoreCase(IReportDAO.SUB_GRID)) {
					JSONArray newjS = new JSONArray();
					newjS.addAll((JSONArray) toJSON(record).get("cell"));
					js.add(newjS);
				} else {
					js.add(toJSON(record));
				}

			}
			// js.addAll(rows)
		}

		gridData.put("data", js);
		gridData.put("recordsTotal", totalRecords);
		gridData.put("recordsFiltered", totalRecords);
		gridData.put("draw", request.getParameter("draw"));

		PrintWriter out = response.getWriter();
		out.println(gridData.toString());

		return null;
	}

	@SuppressWarnings("unused")
	private Object getMethodValue(String methodName, Object param) {
		Object field = null;
		try {
			@SuppressWarnings("rawtypes")
			Class cls = this.getClass();

			if (param != null) {
				if (param instanceof Object[]) {
					Method setNameMethod = this.getClass().getMethod(methodName, Object[].class);
					field = setNameMethod.invoke(this, param);
				} else {
					Method setNameMethod = this.getClass().getMethod(methodName, String.class);
					field = setNameMethod.invoke(this, param);
				}
			} else {
				Method setNameMethod = this.getClass().getMethod(methodName);
				field = setNameMethod.invoke(this);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return field;
	}

	public String detail() throws Exception {
		// startDate=DateUtil.convertDateFormat(startDate,DateUtil.DATE_FORMAT ,
		// toFarmat)
		String breanchidPa = "BRANCH_ID";
		Type listType1 = new TypeToken<Map<String, String>>() {
		}.getType();
		if (!StringUtil.isEmpty(filterList)) {
			try {
				Map<String, String> filtersList = new Gson().fromJson(filterList, listType1);
				if(dynamicReportConfig.getFetchType() == 2L){
					if (branchIdParma!=null && !StringUtil.isEmpty(branchIdParma)) {
						filtersList.put("branchId", "9~" + branchIdParma + "");
					}else if (getBranchId() != null) {
						filtersList.put("branchId", "9~" + getBranchId() + "");
					}

					if (subBranchIdParma!=null && !StringUtil.isEmpty(subBranchIdParma) && !subBranchIdParma.equals("0")) {
						filtersList.put("branchId", "9~" + subBranchIdParma + "");
					}
					
				}else{
					
					if(!getCurrentTenantId().equalsIgnoreCase(ESESystem.OCP)){
						if (branchIdParma!=null && !StringUtil.isEmpty(branchIdParma)) {
							filtersList.put("BRANCH_ID", " ='" + branchIdParma + "'");
						}else if (getBranchId() != null) {
							filtersList.put("BRANCH_ID", " ='" + getBranchId() + "'");
						}

						if (subBranchIdParma!=null &&  !StringUtil.isEmpty(subBranchIdParma) && !subBranchIdParma.equals("0")) {
							filtersList.put("BRANCH_ID", " ='" + subBranchIdParma + "'");
						}
					}else{
						if (branchIdParma!=null && !StringUtil.isEmpty(branchIdParma)&& (subBranchIdParma==null ||StringUtil.isEmpty(subBranchIdParma)|| !subBranchIdParma.equals("0"))) {
							filtersList.put("PARENT_ID", " ='" + branchIdParma + "'");
						}else if (getBranchId() != null) {
							filtersList.put("BRANCH_ID", " ='" + getBranchId() + "'");
						}

						if (subBranchIdParma!=null &&  !StringUtil.isEmpty(subBranchIdParma) && !subBranchIdParma.equals("0")) {
							filtersList.put("BRANCH_ID", " ='" + subBranchIdParma + "'");
						}
					}
						
				
				}
				
				mainMap.put(DynamicReportConfig.FILTER_FIELDS, filtersList);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		otherMap.put(DynamicReportConfig.DYNAMIC_CONFIG_DETAIL, dynamicReportConfig.getDynmaicReportConfigDetails());
		otherMap.put(DynamicReportConfig.ENTITY, dynamicReportConfig.getEntityName());
		otherMap.put(DynamicReportConfig.ALIAS, dynamicReportConfig.getAlias());
		otherMap.put(DynamicReportConfig.GROUP_PROPERTY, dynamicReportConfig.getGroupProperty());
		mainMap.put(DynamicReportConfig.OTHER_FIELD, otherMap);

		Map data = null;
		if (!ObjectUtil.isEmpty(dynamicReportConfig) && dynamicReportConfig.getFetchType() == 2L) {
			data = readProjectionDataStatic(mainMap);
		} else if (!ObjectUtil.isEmpty(dynamicReportConfig) && dynamicReportConfig.getFetchType() == 3L) {

			data = readProjectionDataView(mainMap);
		} else {
			data = readData();
		}
		setGridIdentity(IReportDAO.MAIN_GRID);
		/* removing ID column config detail fro iterating */
		dynamicReportConfig.getDynmaicReportConfigDetails()
				.remove(dynamicReportConfig.getDynmaicReportConfigDetails().iterator().next());
		/*
		 * removing Branch Dynamic Report COnfig Detail From Iterating, s its
		 * value already added to rows
		 */
		dynamicReportConfig.getDynmaicReportConfigDetails()
				.remove(dynamicReportConfig.getDynmaicReportConfigDetails().iterator().next());

		return sendJQGridJSONResponse(data,false);
	}

	public String subGridDetail() throws Exception {

		/*
		 * if (getCurrentTenantId().equalsIgnoreCase(ESESystem.KPF_TENANT_ID))
		 */
		Map data = null;
		otherMap.put(DynamicReportConfig.DYNAMIC_CONFIG_DETAIL, subDynamicReportConfig.getDynmaicReportConfigDetails());
		otherMap.put(DynamicReportConfig.ENTITY, subDynamicReportConfig.getEntityName().split("~")[0].trim());
		otherMap.put(DynamicReportConfig.ALIAS, subDynamicReportConfig.getAlias());

		// mainMap.put(DynamicReportConfig.FILTER_FIELDS, filtersList);
		if (!ObjectUtil.isEmpty(subDynamicReportConfig) && subDynamicReportConfig.getFetchType() == 2L) {
			Map<String, String> filtersList = new HashMap<>();
			filtersList.put(subDynamicReportConfig.getEntityName().split("~")[1].trim(),
					"7~" + Long.valueOf(getId()) + "");
			mainMap.put(DynamicReportConfig.FILTER_FIELDS, filtersList);
			mainMap.put(DynamicReportConfig.OTHER_FIELD, otherMap);
			data = readProjectionDataStatic(mainMap);
		} else if (!ObjectUtil.isEmpty(subDynamicReportConfig) && subDynamicReportConfig.getFetchType() == 3L) {
			Map<String, String> filtersList = new HashMap<>();
			filtersList.put(subDynamicReportConfig.getEntityName().split("~")[1].trim(),
					getId().contains(",") ? " in (" + getId() + ") " : " = " + getId());
			mainMap.put(DynamicReportConfig.FILTER_FIELDS, filtersList);
			mainMap.put(DynamicReportConfig.OTHER_FIELD, otherMap);

			data = readProjectionDataView(mainMap);
		} else {
			data = readData();
		}
		setGridIdentity(IReportDAO.SUB_GRID);
		return sendJQGridJSONResponse(data, false);

	}

	String expr;
	String branchId="";
	AtomicInteger runCount = new AtomicInteger(1);
	private Map<java.lang.String, java.lang.String> plotting;
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		 runCount = new AtomicInteger(1);
		String id = "";
		StringBuilder exportParams=new StringBuilder("");
		if(obj instanceof Object[]) {
			Object[] arr = (Object[]) obj;
			id = String.valueOf(arr[0]);
			branchId=getBranchId();
			if (getGridIdentity().equalsIgnoreCase(IReportDAO.MAIN_GRID)) {
				if (!StringUtil.isEmpty(dynamicReportConfig)) {
					if (StringUtil.isEmpty(getBranchId())) {
						fValue = ReflectUtil.getObjectFieldValue(arr, String.valueOf(runCount.getAndIncrement()));
						branchId =String.valueOf(fValue);
						jsonObject.put(branId, getBranchesMap().get(fValue));
					} else {
						runCount.getAndIncrement();
					}

					dynamicReportConfig.getDynmaicReportConfigDetails().stream()
							.filter(config -> config.getIsGridAvailabiltiy())
							.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
							.forEach(dynamicReportConfigDetail -> {

								if (dynamicReportConfigDetail.getAccessType() == 1L) {
									fValue = ReflectUtil.getObjectFieldValue(arr,
											String.valueOf(runCount.getAndIncrement()));
									if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
										jsonObject.put(dynamicReportConfigDetail.getId(), fValue.toString());
										exportParams.append("~"+fValue.toString());
									} else {
										jsonObject.put(dynamicReportConfigDetail.getId(), "");
										exportParams.append("~"+"");
									}
								} else if (dynamicReportConfigDetail.getAccessType() == 2L) {
									fValue = ReflectUtil.getObjectFieldValue(arr,
											String.valueOf(runCount.getAndIncrement()));
									if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
										mValue = getMethodValue(dynamicReportConfigDetail.getMethod(),
												fValue.toString());
										if (!ObjectUtil.isEmpty(mValue) && !StringUtil.isEmpty(mValue)) {
											jsonObject.put(dynamicReportConfigDetail.getId(), mValue.toString());
											exportParams.append("~"+mValue.toString());
										} else {
											jsonObject.put(dynamicReportConfigDetail.getId(), "");
											exportParams.append("~"+"");
										}
									} else {
										jsonObject.put(dynamicReportConfigDetail.getId(), "");
										exportParams.append("~"+"");
									}
								} else if (dynamicReportConfigDetail.getAccessType() == 3L) {
									fValue = ReflectUtil.getObjectFieldValue(arr,
											String.valueOf(runCount.getAndIncrement()));

									if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
										mValue = getMethodValue(dynamicReportConfigDetail.getMethod(),
												fValue.toString());
									}

									if (!ObjectUtil.isEmpty(dynamicReportConfigDetail.getExpression())
											&& !StringUtil.isEmpty(dynamicReportConfigDetail.getExpression())) {
										String expression = dynamicReportConfigDetail.getExpression();

										try {
											expression_result = ReflectUtil.getObjectFieldValueByExpression(arr,
													expression, dynamic_report_config_detail_ID);
										} catch (ScriptException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

									}

									if (!ObjectUtil.isEmpty(dynamicReportConfigDetail.getParameters())
											&& !StringUtil.isEmpty(dynamicReportConfigDetail.getParameters())) {
										String parameters = dynamicReportConfigDetail.getParameters();
										String[] parametersArray = parameters.split(",");
										fvalueByParameters = ReflectUtil.getObjectFieldValueByParameters(arr,
												parametersArray, dynamic_report_config_detail_ID);

										Object temp = getMethodValue(dynamicReportConfigDetail.getMethod(),
												fvalueByParameters);

									} else if (!ObjectUtil.isEmpty(expression_result)
											&& !StringUtil.isEmpty(expression_result)
											&& !ObjectUtil.isEmpty(dynamicReportConfigDetail.getExpression())
											&& !StringUtil.isEmpty(dynamicReportConfigDetail.getExpression())) {
										jsonObject.put(dynamicReportConfigDetail.getId(), expression_result);
										exportParams.append("~"+expression_result.toString());
									} else if (!ObjectUtil.isEmpty(mValue) && !StringUtil.isEmpty(mValue)) {
										jsonObject.put(dynamicReportConfigDetail.getId(), mValue.toString());
										exportParams.append("~"+mValue.toString());
									} else {
										jsonObject.put(dynamicReportConfigDetail.getId(), "");
										exportParams.append("~"+"");
									}

								} else if (dynamicReportConfigDetail.getAccessType() == 4L) {
									fValue = ReflectUtil.getObjectFieldValue(arr,
											String.valueOf(runCount.getAndIncrement()));

									if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
										jsonObject.put(dynamicReportConfigDetail.getId(), "<button class='faMap' title='" + getText("farm.map.available.title")
												+ "' onclick='showFarmMap(\"" + fValue + "\")'></button>");
									} else {
										// No Latlon
										jsonObject.put(dynamicReportConfigDetail.getId(), "<button class='no-latLonIcn' title='"
												+ getText("farm.map.unavailable.title") + "'></button>");
									}
								} else if (dynamicReportConfigDetail.getAccessType() == 5L) {
									fValue = ReflectUtil.getObjectFieldValue(arr,
											String.valueOf(runCount.getAndIncrement()));

									if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
										String[] a = String.valueOf(fValue).split(",");
										jsonObject.put(dynamicReportConfigDetail.getId(), "<button class='faMap' title='" + getText("farm.map.available.title")
												+ "' onclick='showMap(\"" + a[0] + "\",\"" + a[1] + "\")'></button>");
									} else {
										// No Latlon
										jsonObject.put(dynamicReportConfigDetail.getId(), "<button class='no-latLonIcn' title='"
												+ getText("farm.map.unavailable.title") + "'></button>");
									}
								} else if (dynamicReportConfigDetail.getAccessType() == 6L) {
									fValue = ReflectUtil.getObjectFieldValue(arr,
											String.valueOf(runCount.getAndIncrement()));

									if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
										String image = (Base64Util.encoder((byte[]) fValue));
										jsonObject.put(dynamicReportConfigDetail.getId(), 
												"<button class=\"fa fa-picture-o\"\"aria-hidden=\"true\"\" onclick='popupWindow(\""
														+ image + "\")'></button>");
									} else {
										// No Latlon
										jsonObject.put(dynamicReportConfigDetail.getId(), "<button class='no-imgIcn'></button>");
									}
								} else if (dynamicReportConfigDetail.getAccessType() == 7L) {
									if(arr[runCount.get()]!=null){
									fValue = getQueryForFiltersJSON(dynamicReportConfigDetail.getMethod(),
											new Object[]{arr[runCount.getAndIncrement()],branchId});
									if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
										jsonObject.put(dynamicReportConfigDetail.getId(), fValue.toString());
										exportParams.append("~"+fValue.toString());
									} else {
										jsonObject.put(dynamicReportConfigDetail.getId(), "");
										exportParams.append("~"+"");
									}
									}else{
										jsonObject.put(dynamicReportConfigDetail.getId(), "");
										exportParams.append("~"+"");
										runCount.getAndIncrement();
									}
								} else if (dynamicReportConfigDetail.getAccessType() == 8L) {
									if(arr[runCount.get()]!=null){
									fValue = convertToJson(dynamicReportConfigDetail.getMethod(),
											arr[runCount.getAndIncrement()]);
									if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
										jsonObject.put(dynamicReportConfigDetail.getId(), fValue.toString());
										exportParams.append("~"+fValue.toString());
									} else {	
										jsonObject.put(dynamicReportConfigDetail.getId(), "");
										exportParams.append("~"+"");
									}
									}else{
										jsonObject.put(dynamicReportConfigDetail.getId(), "");
										exportParams.append("~"+"");
										runCount.getAndIncrement();
									}
								}
								else if (dynamicReportConfigDetail.getAccessType() == 9L) {
									if(arr[runCount.get()]!=null){
									String par =dynamicReportConfigDetail.getParameters().split("~")[0].trim();
									/*mValue = fValue = convertToJson(dynamicReportConfigDetail.getMethod(),
											arr[runCount.getAndIncrement()]);*/
									String title=par.split("#")[1].trim();
									String param=par.split("#")[0].trim();
									mValue = ReflectUtil.getObjectFieldValue(arr,
											String.valueOf(runCount.getAndIncrement()));
									String par1 =dynamicReportConfigDetail.getParameters().split("~")[1].trim();
										fValue = ReflectUtil.getObjectFieldValue(arr,
												String.valueOf(param))+"~"+par1+"~"+dynamicReportConfigDetail.getMethod()+"~"+title;
										System.out.println("fValue: "+fValue);
									//	if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
											/*jsonObject.put(dynamicReportConfigDetail.getId(), mValue +"<button class=\"fa fa-pencil-square-o\"\"aria-hidden=\"true\"\" onclick=\"detailPopup('"
													+fValue + "')\"></button>");*/
											if(!ObjectUtil.isEmpty(mValue) && !StringUtil.isEmpty(mValue)){
											jsonObject.put(dynamicReportConfigDetail.getId(), "<a href onclick=\"detailPopup('"
													+fValue + "');return false\"> "+mValue+"</a>");
											
										} else {
											jsonObject.put(dynamicReportConfigDetail.getId(), "<button class='no-imgIcn'></button>");
											
										}
										}else{
											jsonObject.put(dynamicReportConfigDetail.getId(), "");
											runCount.getAndIncrement();
										}
								}
								else if(dynamicReportConfigDetail.getAccessType() == 10L){
									
									fValue = ReflectUtil.getObjectFieldValue(arr,
											String.valueOf(runCount.getAndIncrement()));
									if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
										
										jsonObject.put(dynamicReportConfigDetail.getId(), "<button class='fa fa-file-pdf-o' style='font-size:18px;color:red' title='" + getText("dynExport") + "' onclick='exportRecPDF(\"" +exportParams+"\")'></button>");

									} else {
										jsonObject.put(dynamicReportConfigDetail.getId(), "<button class='no-imgIcn'></button>");
									}
									
								}else if (dynamicReportConfigDetail.getAccessType() == 12L) {
									if(arr[runCount.get()]!=null){
									String par =dynamicReportConfigDetail.getParameters().split("~")[0].trim();
									
									String title=par.split("#")[1].trim();
									String param=par.split("#")[0].trim();
									mValue = ReflectUtil.getObjectFieldValue(arr,
											String.valueOf(runCount.getAndIncrement()));
									String par1 =dynamicReportConfigDetail.getParameters().split("~")[1].trim();
										fValue = ReflectUtil.getObjectFieldValue(arr,
												String.valueOf(param))+"~"+par1+"~"+dynamicReportConfigDetail.getMethod()+"~"+title;
										System.out.println("fValue: "+fValue);
									
											if(!ObjectUtil.isEmpty(mValue) && !StringUtil.isEmpty(mValue)){
											jsonObject.put(dynamicReportConfigDetail.getId(), "<a href onclick=\"detailPopupDt('"
													+fValue + "');return false\"target=\"_blank\"> "+mValue+"</a>");
											
										} else {
											jsonObject.put(dynamicReportConfigDetail.getId(), "<button class='no-imgIcn'></button>");
											
										}
										}else{
											jsonObject.put(dynamicReportConfigDetail.getId(), "");
											runCount.getAndIncrement();
										}
								} else if (dynamicReportConfigDetail.getAccessType() == 13L) {
									fValue = ReflectUtil.getObjectFieldValue(arr,
											String.valueOf(runCount.getAndIncrement()));

									if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
										String image = (Base64Util.encoder((byte[]) fValue));
										jsonObject.put(dynamicReportConfigDetail.getId(), 
												"<button class='btn btn-info btn-sm' onclick='downloadFile(\""
														+ arr[0].toString() + "\")'><span class='glyphicon glyphicon-cloud-download'></span></button>");
									} else {
										// No Latlon
										jsonObject.put(dynamicReportConfigDetail.getId(), "");
									}
								}
								
								
							});
				}
			} else if (getGridIdentity().equalsIgnoreCase(IReportDAO.SUB_GRID)) {
				subDynamicReportConfig.getDynmaicReportConfigDetails().stream()
						.filter(config -> config.getIsGridAvailabiltiy())
						.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
						.forEach(dynamicReportConfigDetail -> {
							if (dynamicReportConfigDetail.getAccessType() == 1L) {
								fValue = ReflectUtil.getObjectFieldValue(arr,
										String.valueOf(runCount.getAndIncrement()));
								if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
									jsonObject.put(dynamicReportConfigDetail.getId(), fValue.toString());
								} else {
									jsonObject.put(dynamicReportConfigDetail.getId(), "");
								}
							} else if (dynamicReportConfigDetail.getAccessType() == 2L) {
								fValue = ReflectUtil.getObjectFieldValue(arr,
										String.valueOf(runCount.getAndIncrement()));
								if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
									mValue = getMethodValue(dynamicReportConfigDetail.getMethod(), fValue.toString());
									if (!ObjectUtil.isEmpty(mValue) && !StringUtil.isEmpty(mValue)) {
										jsonObject.put(dynamicReportConfigDetail.getId(), mValue.toString());
									} else {
										jsonObject.put(dynamicReportConfigDetail.getId(), "");
									}
								} else {
									jsonObject.put(dynamicReportConfigDetail.getId(), "");
								}
							} else if (dynamicReportConfigDetail.getAccessType() == 3L) {
								fValue = ReflectUtil.getObjectFieldValue(arr,
										dynamicReportConfigDetail.getParameters());
								if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
									mValue = getMethodValue(dynamicReportConfigDetail.getMethod(), fValue);
									if (!ObjectUtil.isEmpty(mValue) && !StringUtil.isEmpty(mValue)) {
										jsonObject.put(dynamicReportConfigDetail.getId(), mValue.toString());
									} else {
										jsonObject.put(dynamicReportConfigDetail.getId(), "");
									}
								} else {
									jsonObject.put(dynamicReportConfigDetail.getId(), "");
								}
							}
							
						});

			}

			jsonObject.put("id", id);
			
			jsonObject.put("cell", rows);

		}
		return jsonObject;

	}

	public String getDaterange() {
		return daterange;
	}

	public void setDaterange(String daterange) {
		this.daterange = daterange;
	}

	public static DynamicReportConfig getDynamicReportConfig() {
		return dynamicReportConfig;
	}

	public static void setDynamicReportConfig(DynamicReportConfig dynamicReportConfig) {
		DynamicViewReportDTAction.dynamicReportConfig = dynamicReportConfig;
	}

	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}

	public List<DynamicReportConfigFilter> getReportConfigFilters() {
		return reportConfigFilters;
	}

	public void setReportConfigFilters(List<DynamicReportConfigFilter> reportConfigFilters) {
		this.reportConfigFilters = reportConfigFilters;
	}

	public String getMainGridCols() {
		return mainGridCols;
	}

	public void setMainGridCols(String mainGridCols) {
		this.mainGridCols = mainGridCols;
	}

	public String getFilterList() {
		return filterList;
	}

	public void setFilterList(String filterList) {
		this.filterList = filterList;
	}

	public String getGridIdentity() {
		return gridIdentity;
	}

	public void setGridIdentity(String gridIdentity) {
		this.gridIdentity = gridIdentity;
	}

	
	public Map<String, String> getAgentList() {
		Map<String, String> agentMap = new LinkedHashMap<>();
		List<Object[]> agentList = agentService.listAgentIdName();
		agentList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			agentMap.put(String.valueOf(objArr[0]), String.valueOf(objArr[1]));
		});
		return agentMap;
	}

	public Map<String, String> getProductList() {
		Map<String, String> prodMap = new HashMap<String, String>();

		List<Object[]> cat = productService.listOfProducts();
		for (Object[] obj : cat) {
			prodMap.put(String.valueOf(obj[0]), String.valueOf(obj[2]));
		}
		return prodMap;
	}

	public Map<String, String> getCategoryList() {
		Map<String, String> cateMap = new HashMap<String, String>();
		List<Object[]> cat = productService.listOfProducts();
		cat.stream().forEach(a -> {
			cateMap.put(a[4].toString(), a[3].toString());
		});
		return cateMap;
	}

	private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	public Object getFilter() {
		return filter;
	}

	public void setFilter(Object filter) {
		this.filter = filter;
	}

	public Map getEntityMap() {
		return entityMap;
	}

	public void setEntityMap(Map entityMap) {
		this.entityMap = entityMap;
	}

	public Map getOtherMap() {
		return otherMap;
	}

	public void setOtherMap(Map otherMap) {
		this.otherMap = otherMap;
	}

	public Map<String, Map> getMainMap() {
		return mainMap;
	}

	public void setMainMap(Map<String, Map> mainMap) {
		this.mainMap = mainMap;
	}

	@Override
	public void prepare() throws Exception {
		dynamicReportConfig = clientService.findReportById(request.getParameter("id"));
		String url = "";
		if (dynamicReportConfig != null) {
			url = getLocaleProperty("report") + "~#," + dynamicReportConfig.getReport()
					+ "~dynamicViewReport_list.action?id=" + dynamicReportConfig.getId();

		}
		request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(url));
	}

	public String getFooterSumCols() {
		return footerSumCols;
	}

	public void setFooterSumCols(String footerSumCols) {
		this.footerSumCols = footerSumCols;
	}

	public String getMainGridColNames() {
		return mainGridColNames;
	}

	public void setMainGridColNames(String mainGridColNames) {
		this.mainGridColNames = mainGridColNames;
	}

	public String getFooterTotCol() {
		return footerTotCol;
	}

	public void setFooterTotCol(String footerTotCol) {
		this.footerTotCol = footerTotCol;
	}

	public Map<String, String> getSeasonsList() {
		if (seasonsList == null || seasonsList.isEmpty()) {
			seasonsList = new HashMap<>();
			List<Object[]> seasonListt = farmerService.listSeasonCodeAndName();

			for (Object[] obj : seasonListt) {
				seasonsList.put(String.valueOf(obj[0]), String.valueOf(obj[1]));
			}
		}
		return seasonsList;

	}

	public String getAgentByProfile(String profileId) {
		String agent = getAgentsList().get(profileId);
		return agent;
	}

	public Map<String, String> getAgentsList() {
		Map<String, String> agentMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(agentList)) {
			agentList = agentService.listAgentIdName();
		}
		agentList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			agentMap.put(String.valueOf(objArr[0]), String.valueOf(objArr[1]));
		});
		return agentMap;
	}

	public String getSeasonByCode(String code) {
		return getSeasonsList().get(code);
	}

	public String getFarmerType(String id) {
		String type = getRegType().get(id);
		return type;
	}

	public Map<String, String> getRegType() {
		Map<String, String> regType = new LinkedHashMap<>();
		regType.put("0", getText("REG"));
		regType.put("1", getText("UNREG"));

		return regType;
	}

	public String getFetchType() {
		return fetchType;
	}

	public void setFetchType(String fetchType) {
		this.fetchType = fetchType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProcurementGradeById(String id) {
		String grade = getProcurementGradeList().get(id);
		return grade;
	}

	public String getProcurementVarietyById(String id) {
		String variety = getProcurementVarietyList().get(id);
		return variety;
	}

	public String getProcurementProductById(String id) {
		String product = getProcurementProductList().get(id);
		return product;
	}

	public String getProcurementProductUnitById(String id) {
		String product = getProcurementProductUnitList().get(id);
		return product;
	}

	public Map<String, String> getProcurementProductList() {
		Map<String, String> productMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(productInfoList)) {
			productInfoList = productService.listProcurementGradeInfo();
		}
		productInfoList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			productMap.put(String.valueOf(objArr[0]), String.valueOf(objArr[8]));
		});
		return productMap;
	}

	public Map<String, String> getProcurementProductUnitList() {
		Map<String, String> productMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(productInfoList)) {
			productInfoList = productService.listProcurementGradeInfo();
		}
		productInfoList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			productMap.put(String.valueOf(objArr[0]), String.valueOf(objArr[9]));
		});
		return productMap;
	}

	public Map<String, String> getProcurementVarietyList() {
		Map<String, String> varietyMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(productInfoList)) {
			productInfoList = productService.listProcurementGradeInfo();
		}
		productInfoList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			varietyMap.put(String.valueOf(objArr[0]), String.valueOf(objArr[5]));
		});
		return varietyMap;
	}

	public Map<String, String> getProcurementGradeList() {
		Map<String, String> gradeMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(productInfoList)) {
			productInfoList = productService.listProcurementGradeInfo();
		}
		productInfoList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			gradeMap.put(String.valueOf(objArr[0]), String.valueOf(objArr[2]));
		});
		return gradeMap;
	}

	public String getSubGridCols() {
		return subGridCols;
	}

	public void setSubGridCols(String subGridCols) {
		this.subGridCols = subGridCols;
	}

	/*public String getGeneralDateFormat(String inputDate) {
		String generalDate = null;
		String result = null;
		try {
			if (preferences == null) {
				preferences = preferncesService.findPrefernceById("1");
			}
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

	}*/

	public static Map<String, Map<String, String>> getFieldValueMap() {
		return fieldValueMap;
	}

	public static void setFieldValueMap(Map<String, Map<String, String>> fieldValueMap) {
		DynamicViewReportDTAction.fieldValueMap = fieldValueMap;
	}

	public Map<String, String> getJQGridRequestParam() {
		
		
		if(!StringUtil.isEmpty(request.getParameter("draw"))){
			setPage(Integer.valueOf(request.getParameter("draw")));
		}
		if(!StringUtil.isEmpty(request.getParameter("length"))){
			setResults(Integer.valueOf(request.getParameter("length")));
			setRows(Integer.valueOf(request.getParameter("length")));
		}
		if(!StringUtil.isEmpty(request.getParameter("start"))){
			setStartIndex(Integer.valueOf(request.getParameter("start")));
		}
		
		
		// setSort(request.getParameter("sidx"));
	
		// setDir(request.getParameter("sord"));

		Map<String, String> searchRecord = new HashMap<String, String>();

		if (!StringUtil.isEmpty(request.getParameter("filters"))) {
			searchRecord = getFilters(request.getParameter("filters"));
		}
		return searchRecord;
	}

	public Long getIdd() {
		return idd;
	}

	public void setIdd(Long idd) {
		this.idd = idd;
		
	}
	
	public String getGeneralDateFormat() {
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			return preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT);
		}
		return null;

	}

	public Map<String, String> getWarehouseList() {
		Map<String, String> warehouseMap = new LinkedHashMap<>();
		warehouseMap = locationService.listWarehouse().stream()
				.collect(Collectors.toMap(warehouse -> String.valueOf(warehouse.getCode()), Warehouse::getName));
		return warehouseMap;
	}
	
	public Map<String,String> getPlottingList(){
		Map<String, String> plot = new LinkedHashMap<>();
		plot.put(getLocaleProperty("plotStatus0"), getLocaleProperty("plotStatus0"));
		plot.put(getLocaleProperty("plotStatus1"), getLocaleProperty("plotStatus1"));
		return plot;
	}
	
	public Map<String,String> getBatchNoList(){
		Map<String, String> batchNoMap = new LinkedHashMap<>();
		batchNoMap=productService.listOfHeapFromBale().stream().filter(f->f!=null && !StringUtil.isEmpty(f)).collect(Collectors.toMap(k->k.toString(), v->v.toString()));
		return batchNoMap;
	}
	
	public Map<String, String> getSamithiList() {
		Map<String, String> warehouseMap = new LinkedHashMap<>();
		warehouseMap = locationService.listOfSamithi().stream()
				.collect(Collectors.toMap(warehouse -> String.valueOf(warehouse.getId()), Warehouse::getName));
		return warehouseMap;
	}
	
	public Map<String, String> getBuyerList() {
		Map<String, String> listOfBuyers = new HashMap<String,String>();
		List<Customer> customers = clientService.listOfCustomers();
		for (Customer customer : customers) {
			listOfBuyers.put(String.valueOf(customer.getId()), customer.getCustomerName());
		}
		return listOfBuyers;
	}
	public Map<String, String> getVillageList() {
		Map<String,String> villageMap=new LinkedHashMap<>();
		villageMap=locationService.listVillageIdAndName().stream().collect(Collectors.toMap(k->String.valueOf(k[0].toString()), v->String.valueOf(v[2].toString())));
		return villageMap;
	}
	public Map<String, String> getFarmersList() {

		Map<String, String> farmersListMap = new LinkedHashMap<String, String>();
		List<Farmer> farmersList = farmerService.listFarmer();
		if (!ObjectUtil.isListEmpty(farmersList)) {
			for (Farmer farmer : farmersList) {
				farmersListMap.put(farmer.getFarmerId(), farmer.getFirstName() + "-" + farmer.getFarmerId());
			}
		}
		return farmersListMap;
	}
	
	public Map<String, String> getFarmsList() {

		Map<String, String> farmsListMap = new LinkedHashMap<String, String>();
		List<Farm> farmsList = farmerService.listFarm();
		if (!ObjectUtil.isListEmpty(farmsList)) {
			for (Farm farm : farmsList) {
				farmsListMap.put(String.valueOf(farm.getId()), farm.getFarmName() );
			}
		}
		return farmsListMap;
	}

	public Long getBranId() {
		return branId;
	}

	public void setBranId(Long branId) {
		this.branId = branId;
	}

	public String getBranchIdParma() {
		return branchIdParma;
	}

	public void setBranchIdParma(String branchIdParma) {
		this.branchIdParma = branchIdParma;
	}

	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}




}
