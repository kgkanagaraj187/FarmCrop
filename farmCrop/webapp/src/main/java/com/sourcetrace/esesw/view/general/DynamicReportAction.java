package com.sourcetrace.esesw.view.general;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.ss.formula.functions.T;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.sourcetrace.eses.entity.DynamicFeildMenuConfig;
import com.sourcetrace.eses.entity.DynamicMenuFieldMap;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.DynamicReportFieldsConfig;
import com.sourcetrace.esesw.entity.profile.DynamicReportTableConfig;
import com.sourcetrace.esesw.view.SwitchAction;

public class DynamicReportAction extends SwitchAction {

	@Autowired
	private IFarmerService farmerService;
	private String entity;
	private String groupByField;
	private String selectedFields;
	private String selectedlabels;
	private String filter_data;
	private String reportTitle;
	private String saved_dynamic_report_id;
	private String reportDescription;
	private String header_fields;
	private String id;
	private String filFields;

	// Bussiness logic

	public String list() {
		return LIST;
	}

	public void populateAvailableColumns() {
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		List<DynamicReportFieldsConfig> availabeColumns = farmerService.populateDynamicReportFields();
		availabeColumns.stream().forEach(obj -> {

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("id", obj.getId());
			jsonObj.put("field", obj.getField());
			jsonObj.put("label", getLocaleProperty(obj.getLabel()));
			jsonObj.put("filter", obj.getFilterAlias());
			jsonObj.put("dataType", obj.getDataType());
			if (obj.getIsGroupByField() != null && obj.getIsGroupByField().equals("1")) {
				jsonObj.put("isGroupBy", "1");
			} else {
				jsonObj.put("isGroupBy", "0");
			}

			jsonObj.put("table", obj.getDynamicReportTable().getId());
			jsonObj.put("tableName", obj.getDynamicReportTable().getEntityName());
			jsonList.add(jsonObj);
		});
		List<DynamicFeildMenuConfig> dyList = farmerService.findDynamicMenus();
		dyList.stream()
				.flatMap(listContainer -> listContainer.getDynamicFieldConfigs().stream())
				.collect(Collectors.toList()).stream().
				filter(u->u.getMenu().getTxnType().equalsIgnoreCase("308")||u.getMenu().getTxnType().equalsIgnoreCase("359")||
						u.getMenu().getTxnType().equalsIgnoreCase("357")).forEach(u->{
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("id", u.getField().getId());
					
					jsonObj.put("label",u.getField().getComponentName());
					jsonObj.put("filter","t."+u.getField().getCode().replaceAll("\\s+","")+"");
					jsonObj.put("dataType", u.getField().getComponentType());
	/*				if (obj.getIsGroupByField() != null && obj.getIsGroupByField().equals("1")) {
						jsonObj.put("isGroupBy", "1");
					} else {
						jsonObj.put("isGroupBy", "0");
					}*/
					if (u.getMenu().getTxnType().equalsIgnoreCase("308")) {
						jsonObj.put("table", 1);
						jsonObj.put("tableName", "Farmer");
						jsonObj.put("field", "max((CASE WHEN ( fdv.FIELD_NAME=\""+u.getField().getCode()+"\") THEN fdv.FIELD_VALUE END )) AS "+u.getField().getCode().replaceAll("\\s+","")+"");
					}else if (u.getMenu().getTxnType().equalsIgnoreCase("359")) {
						jsonObj.put("table", 2);
						jsonObj.put("tableName", "Farm");
						jsonObj.put("field", "max((CASE WHEN ( fmdv.FIELD_NAME=\""+u.getField().getCode()+"\") THEN fmdv.FIELD_VALUE END )) AS "+u.getField().getCode().replaceAll("\\s+","")+"");
					}else if (u.getMenu().getTxnType().equalsIgnoreCase("357")) {
						jsonObj.put("table", 3);
						jsonObj.put("tableName", "Farm_Crops");
						jsonObj.put("field", "max((CASE WHEN ( fcdv.FIELD_NAME=\""+u.getField().getCode()+"\") THEN fcdv.FIELD_VALUE END )) AS "+u.getField().getCode().replaceAll("\\s+","")+"");
					}
					jsonList.add(jsonObj);
				});
		
		printAjaxResponse(jsonList, "text/html");
	}

	// getters and setters

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	String query = "";
	Long enity = 0l;

	public void executeQueryAndgetValues() {
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		JSONObject jss = new JSONObject();
		enity = 0l;
		setEntity(StringUtil.removeLastComma(getEntity()));
		if (getEntity().contains(",")) {
			enity = Arrays.asList(getEntity().split(",")).stream().mapToLong(u -> Long.valueOf(u)).min()
					.orElseThrow(NoSuchElementException::new);
			;
		} else {
			enity = Long.valueOf(getEntity());
		}
		String parentQuery = "";
		query = "select * from (";

		LinkedHashMap<String, String> joinMaps = new LinkedHashMap<>();
		LinkedHashMap<String, String> otherJoins = new LinkedHashMap<>();
		Arrays.asList(getEntity().split(",")).stream().sorted().forEach(u -> {

			DynamicReportTableConfig dtb = farmerService.findDyamicReportTableConfigById(Long.valueOf(u));
			if (Long.valueOf(u) == enity) {
				query += "Select " + getSelectedFields() + " from  " + dtb.getTable() + " " + dtb.getAlias();
			}
			if (dtb.getJoinString() != null) {
				query += " "+ dtb.getJoinString();
			}

			if (dtb.getEntityQuery() != null) {
				joinMaps.put(dtb.getTable() + " " + dtb.getAlias(), dtb.getEntityQuery());
			}

		});
		if (!joinMaps.isEmpty()) {
			joinMaps.entrySet().stream().forEach(u -> {
				query += " " + u.getValue();

			});
		}

		if (!otherJoins.isEmpty()) {
			otherJoins.entrySet().stream().forEach(u -> {
				query += " join " + u.getKey() + " on " + u.getValue();

			});
		}
		query = query + ")t  ";
		if (!StringUtil.isEmpty(filter_data) && !filter_data.equals("[]")) {
			Type listType1 = new TypeToken<JsonArray>() {
			}.getType();
			JsonArray jsonArray = new Gson().fromJson(filter_data, listType1);

			query = query + " where  ";
			jsonArray.forEach(u -> {
				String field = u.getAsJsonObject().get("field").toString().replaceAll("^\"|\"$", "");
				String dataType = u.getAsJsonObject().get("fieldDataType").toString().replaceAll("^\"|\"$", "");
				String expression = u.getAsJsonObject().get("expression").toString().replaceAll("^\"|\"$", "");
				String andOr = u.getAsJsonObject().get("andOr") == null ? ""
						: (u.getAsJsonObject().get("andOr").toString().replaceAll("^\"|\"$", "") + " ");
				if (u.getAsJsonObject().get("value") != null && !StringUtil.isEmpty(u.getAsJsonObject().get("value"))) {
					String value = u.getAsJsonObject().get("value").toString().replaceAll("^\"|\"$", "");

					if (expression.contains("between")) {
						value = "'" + value.split("and")[0].trim() + "' and '" + value.split("and")[1].trim() + "'";
					} else {
						value = "'" + value.toString() + "'";
					}

					if (expression.equals("like")) {
						value = value.replaceAll("'", "");
						value = "'%" + value + "%'";

					}
					query = query + field + " " + expression + " " + value + " " + andOr;
				} else {
					query = query + field + " " + expression + " " + andOr;
				}
			});

		}

		List<Object[]> gridData = farmerService.getGridData(query);
		String[] selectedFieldsForHeader = getSelectedlabels().split(",");

		if (selectedFieldsForHeader.length > 1) {
			gridData.stream().forEach(obj -> {
				Object[] objArr = (Object[]) obj;
				JSONObject jsonObj = new JSONObject();
				for (int i = 0; i < selectedFieldsForHeader.length; i++) {
					jsonObj.put(selectedFieldsForHeader[i],
							!ObjectUtil.isEmpty(objArr[i]) ? String.valueOf(objArr[i]) : " ");
				}
				jsonList.add(jsonObj);
			});
		} else {
			for (int i = 0; i < gridData.size(); i++) {
				JSONObject jsonObj = new JSONObject();

				if (!ObjectUtil.isEmpty(gridData.get(i))) {
					jsonObj.put(selectedFieldsForHeader[0], String.valueOf(gridData.get(i)));
				} else {
					jsonObj.put(selectedFieldsForHeader[0], gridData.get(i));
				}
				jsonList.add(jsonObj);
			}

		}
		jss.put("list", jsonList);
		jss.put("query", query);
		printAjaxResponse(jss, "text/html");
	}

	public String formQuery() {
		enity = 0l;
		setEntity(StringUtil.removeLastComma(getEntity()));
		if (getEntity().contains(",")) {
			enity = Arrays.asList(getEntity().split(",")).stream().mapToLong(u -> Long.valueOf(u)).max()
					.orElseThrow(NoSuchElementException::new);
			;
		} else {
			enity = Long.valueOf(getEntity());
		}
		String parentQuery = "";
		query = "select distinct  "+getFilFields()+" from (";	

		LinkedHashMap<String, String> joinMaps = new LinkedHashMap<>();
		LinkedHashMap<String, String> otherJoins = new LinkedHashMap<>();
		
		DynamicReportTableConfig dtb = farmerService.findDyamicReportTableConfigById(enity);
		
		   query += "Select " + getSelectedFields() + " from  " + dtb.getTable() + " " + dtb.getAlias();
			
		
		query =  createQuery(dtb,query);
		
		/*Arrays.asList(getEntity().split(",")).stream().sorted(Comparator.reverseOrder()).forEach(u -> {

			DynamicReportTableConfig dtb = farmerService.findDyamicReportTableConfigById(Long.valueOf(u));
			if (Long.valueOf(u) == enity) {
				query += "Select " + getSelectedFields() + " from  " + dtb.getTable() + " " + dtb.getAlias();
			}
			if (dtb.getJoinString() != null && query.contains(dtb.getJoinString())) {
				query += " "+ dtb.getJoinString();
			}

			if (dtb.getEntityQuery() != null) {
				joinMaps.put(dtb.getTable() + " " + dtb.getAlias(), dtb.getEntityQuery());
			}

		});
		if (!joinMaps.isEmpty()) {
			joinMaps.entrySet().stream().forEach(u -> {
				query += " "+ u.getValue();

			});
		}
*/
	/*	if (!otherJoins.isEmpty()) {
			otherJoins.entrySet().stream().forEach(u -> {
				if (u.getKey().contains("|")) {
					query += " left join " + u.getKey().split("\\|")[0] + " on " + u.getValue();
				} else {
					query += " join " + u.getKey() + " on " + u.getValue();
				}

			});
		}*/
		query = query + ")t  ";
		if (!StringUtil.isEmpty(filter_data) && !filter_data.equals("[]")) {
			Type listType1 = new TypeToken<JsonArray>() {
			}.getType();
			JsonArray jsonArray = new Gson().fromJson(filter_data, listType1);

			query = query + " where  ";
			jsonArray.forEach(u -> {
				String field = u.getAsJsonObject().get("field").toString().replaceAll("^\"|\"$", "");
				String dataType = u.getAsJsonObject().get("fieldDataType").toString().replaceAll("^\"|\"$", "");
				String expression = u.getAsJsonObject().get("expression").toString().replaceAll("^\"|\"$", "");
				String andOr = u.getAsJsonObject().get("andOr") == null ? ""
						: (u.getAsJsonObject().get("andOr").toString().replaceAll("^\"|\"$", "") + " ");
				if (u.getAsJsonObject().get("value") != null && !StringUtil.isEmpty(u.getAsJsonObject().get("value"))) {
					String value = u.getAsJsonObject().get("value").toString().replaceAll("^\"|\"$", "");

					if (expression.contains("between") && !StringUtil.isLong(value.split("and")[0].trim())
							&& !StringUtil.isDouble(value.split("and")[0].trim())
							&& !StringUtil.isInteger(value.split("and")[0].trim())
							&& !StringUtil.isLong(value.split("and")[1].trim())
							&& !StringUtil.isDouble(value.split("and")[1].trim())
							&& !StringUtil.isInteger(value.split("and")[1].trim())) {
						value = "\"" + value.split("and")[0].trim() + "\" and \"" + value.split("and")[1].trim() + "\"";
					} else if (expression.contains("between")) {
						value = value.split("and")[0].trim() + " and " + value.split("and")[1].trim();
					}

					

					if (expression.equals("like")) {
						value = value.replaceAll("\"", "").trim();
						value = "\"%" + value + "%\"";

					}else if (expression.equals("between")) {
						value=value;

					} else if (!StringUtil.isLong(value) && !StringUtil.isDouble(value) && !StringUtil.isInteger(value) ) {
						value = "\"" + value.toString() + "\"";
					}
					query = query + field + " " + expression + " " + value + " " + andOr;
				} else {
					query = query + field + " " + expression + " " + andOr;
				}
			});

		}
		if (getGroupByField()!=null && !StringUtil.isEmpty(getGroupByField())) {
			
			query = query+" group by "+getGroupByField();
		}
		return query;
	}

	private String createQuery(DynamicReportTableConfig dtb, String query2) {
	 	
		if (dtb.getJoinString() != null && !query.contains(dtb.getJoinString())) {
			query += " "+ dtb.getJoinString();
		}

		if (dtb.getEntityQuery() != null) {
			query += " "+ dtb.getEntityQuery();
		}
		if(dtb.getParent()!=null){
			query = createQuery(dtb.getParent(),query);
		}
		return query;
	}

	public void executeQueryAndgetValuesDT() {

		JSONArray finalA = new JSONArray();
		JSONObject jss = new JSONObject();
		JSONArray jsA = new JSONArray();
		if (!StringUtil.isEmpty(id)) {
			query = id;
		} else {
			formQuery();

		}

		List<Object[]> gridData = farmerService.getGridData(query);
		/*
		 * String[] selectedFieldsForHeader = getSelectedlabels().split(",");
		 */
		gridData.stream().forEach(obj -> {
			JSONArray rows = new JSONArray();
			Object[] objArr = (Object[]) obj;
			for (int i = 0; i < objArr.length; i++) {
				rows.add(!ObjectUtil.isEmpty(objArr[i]) ? String.valueOf(objArr[i]) : " ");

			}
			finalA.add(rows);
		});

		/*
		 * if (selectedFieldsForHeader.length > 1) {
		 * gridData.stream().forEach(obj -> { JSONArray rows = new JSONArray();
		 * Object[] objArr = (Object[]) obj; for (int i = 0; i <
		 * selectedFieldsForHeader.length; i++) {
		 * rows.add(!ObjectUtil.isEmpty(objArr[i]) ? String.valueOf(objArr[i]) :
		 * " ");
		 * 
		 * } finalA.add(rows); });
		 * 
		 * } else { JSONArray rows = new JSONArray(); for (int i = 0; i <
		 * gridData.size(); i++) { if (!ObjectUtil.isEmpty(gridData.get(i))) {
		 * rows.add(String.valueOf(gridData.get(i)));
		 * 
		 * } else { rows.add(gridData.get(i));
		 * 
		 * }
		 * 
		 * } finalA.add(rows); }
		 */
		jss.put("data", finalA);
		jss.put("query", query);
		printAjaxResponse(jss, "text/html");
	}

	public void populateSavedReports() {
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		List<Object[]> gridData = farmerService.getGridData(getQuery());
		String[] selectedFieldsForHeader = getSelectedFields().split(",");

		if (selectedFieldsForHeader.length > 1) {
			gridData.stream().forEach(obj -> {
				Object[] objArr = (Object[]) obj;
				JSONObject jsonObj = new JSONObject();
				for (int i = 0; i < selectedFieldsForHeader.length; i++) {
					jsonObj.put(selectedFieldsForHeader[i],
							!ObjectUtil.isEmpty(objArr[i]) ? String.valueOf(objArr[i]) : " ");
				}
				jsonList.add(jsonObj);
			});
		} else {
			for (int i = 0; i < gridData.size(); i++) {
				JSONObject jsonObj = new JSONObject();

				if (!ObjectUtil.isEmpty(gridData.get(i))) {
					jsonObj.put(selectedFieldsForHeader[0], String.valueOf(gridData.get(i)));
				} else {
					jsonObj.put(selectedFieldsForHeader[0], gridData.get(i));
				}
				jsonList.add(jsonObj);
			}

		}

		printAjaxResponse(jsonList, "text/html");
	}

	public void executeSavedReport() {
		JSONObject jss = new JSONObject();
		JSONArray jsA = new JSONArray();
		JSONArray finalA = new JSONArray();

		String query = "select t.id,t.title,t.des,t.`query`,t.header_fields,t.filter_data,t.GROUP_BY_FIELD,t.FIELDS_SELECTED,t.ENTITY from saved_dynamic_report t where t.id = "
				+ id;
		List<Object[]> repData = farmerService.getGridData(query);
		List<Object[]> gridData = farmerService.getGridData(repData.get(0)[3].toString());
		/*
		 * String[] selectedFieldsForHeader = getSelectedlabels().split(",");
		 */
		gridData.stream().forEach(obj -> {
			JSONArray rows = new JSONArray();
			Object[] objArr = (Object[]) obj;
			for (int i = 0; i < objArr.length; i++) {
				rows.add(!ObjectUtil.isEmpty(objArr[i]) ? String.valueOf(objArr[i]) : " ");

			}
			finalA.add(rows);
		});

		query = repData.get(0)[3].toString();

		String enities = repData.get(0)[7].toString();
		List<DynamicReportFieldsConfig> availabeColumns = farmerService.listDynamicColumnsByEntity(enities);
		availabeColumns.stream().forEach(obj -> {

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("id", obj.getId());
			jsonObj.put("field", obj.getField());
			jsonObj.put("label", getLocaleProperty(obj.getLabel()));
			jsonObj.put("filter", obj.getFilterAlias());
			jsonObj.put("dataType", obj.getDataType());
			if (obj.getIsGroupByField() != null && obj.getIsGroupByField().equals("1")) {
				jsonObj.put("isGroupBy", "1");
			} else {
				jsonObj.put("isGroupBy", "0");
			}

			jsonObj.put("table", obj.getDynamicReportTable().getId());
			jsonObj.put("tableName", obj.getDynamicReportTable().getEntityName());
			jsA.add(jsonObj);

		});
		jss.put("fieldList", jsA);
		jss.put("filterData", repData.get(0)[5].toString());
		jss.put("groupBy", repData.get(0)[6].toString());

		jss.put("query", repData.get(0)[3].toString());
		jss.put("colName", repData.get(0)[4].toString());
		jss.put("aaData", finalA);
		printAjaxResponse(jss, "text/html");
	}

	public void saveDynamicReport() {
		String filterData = StringEscapeUtils.escapeJava(getFilter_data());
		formQuery();

		farmerService.saveDynamicReport(getReportTitle(), getReportDescription(), query, selectedlabels, filterData,
				entity, header_fields, groupByField, id);
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getSelectedFields() {
		return selectedFields;
	}

	public void setSelectedFields(String selectedFields) {
		this.selectedFields = selectedFields;
	}

	public String getGroupByField() {
		return groupByField;
	}

	public void setGroupByField(String groupByField) {
		this.groupByField = groupByField;
	}

	public String getSelectedlabels() {
		return selectedlabels;
	}

	public void setSelectedlabels(String selectedlabels) {
		this.selectedlabels = selectedlabels;
	}

	public String getFilter_data() {
		return filter_data;
	}

	public void setFilter_data(String filter_data) {
		this.filter_data = filter_data;
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public String getReportDescription() {
		return reportDescription;
	}

	public void setReportDescription(String reportDescription) {
		this.reportDescription = reportDescription;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getHeader_fields() {
		return header_fields;
	}

	public void setHeader_fields(String header_fields) {
		this.header_fields = header_fields;
	}

	public void deleteDynamicReportById() {
		farmerService.deleteDynamicReportById(getSaved_dynamic_report_id());
	}

	public void updateDynamicReportById() {
		String filterData = StringEscapeUtils.escapeJava(getFilter_data());
		farmerService.updateDynamicReportById(getSaved_dynamic_report_id(), getReportTitle(), getReportDescription(),
				query, getHeader_fields(), filterData, entity, selectedFields, groupByField);
	}

	public String getSaved_dynamic_report_id() {
		return saved_dynamic_report_id;
	}

	public void setSaved_dynamic_report_id(String saved_dynamic_report_id) {
		this.saved_dynamic_report_id = saved_dynamic_report_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFilFields() {
		return filFields;
	}

	public void setFilFields(String filFields) {
		this.filFields = filFields;
	}

	public void validateJoins(){
		enity = 0l;
		setEntity(StringUtil.removeLastComma(getEntity()));
		List<Object[]> chartData = farmerService.findDyamicReportTableConfigParentIdsById(getEntity());
		Set<Object[]> set = new HashSet<Object[]>(chartData);
		JSONObject jsonObj = new JSONObject();
		if(set.size() < chartData.size()){
			jsonObj.put("stat", 1);
		}else{
			jsonObj.put("stat", 0);
		}
		    
		printAjaxResponse(jsonObj, "text/html");
	}
}
