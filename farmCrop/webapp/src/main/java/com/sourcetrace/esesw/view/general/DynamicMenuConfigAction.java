package com.sourcetrace.esesw.view.general;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.sourcetrace.eses.entity.DynamicFeildMenuConfig;
import com.sourcetrace.eses.entity.DynamicFieldConfig;
import com.sourcetrace.eses.entity.DynamicMenuFieldMap;
import com.sourcetrace.eses.entity.DynamicMenuSectionMap;
import com.sourcetrace.eses.entity.DynamicSectionConfig;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

public class DynamicMenuConfigAction extends SwitchValidatorAction {

	private static final long serialVersionUID = 804839924142063898L;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(DynamicMenuConfigAction.class);

	private DynamicFeildMenuConfig dynamicFeildMenuConfig;

	Map<Integer, String> entityType = new LinkedHashMap<Integer, String>();
	// private List<String> selectedName = new ArrayList<String>();
	private List<String> availableName = new ArrayList<String>();

	private List<String> availableSectionName = new ArrayList<String>();

	private List<String> availableDynamicFieldName = new ArrayList<String>();

	private Map<String, String> availableSection = new LinkedHashMap<String, String>();

	private Map<String, String> selectedSections = new HashMap<>();

	// List<String> selectedFieldName = new ArrayList<String>();
	private Map<String, String> availableFieldName = new HashMap<>();

	// private List<String> availableField = new ArrayList<String>();

	private Map<String, String> availableField = new LinkedHashMap<String, String>();

	private Map<String, String> selectedField = new LinkedHashMap<String, String>();
	private String id;
	private String entType;
	private String selVals;
	private String selectedName;

	private String selectedFieldName;

	private String sectionNames;
	private String fieldNames;
	@Autowired
	private IUniqueIDGenerator uniqueIDGenerator;

	@Autowired
	private IFarmerService farmerService;

	@Autowired
	private IAgentService agentService;
	
	private AtomicInteger order = new AtomicInteger(1);
	private Integer serviceM;
	private Integer reportM;
	private String parentMenu;
	private String role;
	private String isSeason;
	private String mobTxnType;
	Map<String, String> isSeasonList = new LinkedHashMap<String, String>();
	
	Map<String, String> txnTypeList = new LinkedHashMap<String, String>();
	
	@Override
	public Object getData() {

		entityType.put(DynamicFeildMenuConfig.EntityTypes.FARMER.ordinal(),
				getLocaleProperty("entityType" + DynamicFeildMenuConfig.EntityTypes.FARMER.ordinal()));
		entityType.put(DynamicFeildMenuConfig.EntityTypes.FARM.ordinal(),
				getLocaleProperty("entityType" + DynamicFeildMenuConfig.EntityTypes.FARM.ordinal()));
		entityType.put(DynamicFeildMenuConfig.EntityTypes.GROUP.ordinal(),
				getLocaleProperty("entityType" + DynamicFeildMenuConfig.EntityTypes.GROUP.ordinal()));
		entityType.put(DynamicFeildMenuConfig.EntityTypes.CERTIFICATION.ordinal(),
				getLocaleProperty("entityType" + DynamicFeildMenuConfig.EntityTypes.CERTIFICATION.ordinal()));

		return dynamicFeildMenuConfig;
	}

	@SuppressWarnings("unchecked")
	public String data() throws Exception {

		Map<String, String> searchRecord = getJQGridRequestParam(); // get the
																	// search
																	// parameter
																	// with

		DynamicFeildMenuConfig filter = new DynamicFeildMenuConfig();

		if (!StringUtil.isEmpty(searchRecord.get("name"))) {
			filter.setName(searchRecord.get("name").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("entity"))) {
			filter.setEntity(searchRecord.get("entity"));
		}
				
		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		return sendJQGridJSONResponse(data);
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object record) {

		JSONObject jsonObject = null;
		jsonObject = new JSONObject();
		if (record instanceof DynamicFeildMenuConfig) {
			DynamicFeildMenuConfig dfmc = (DynamicFeildMenuConfig) record;
			JSONArray rows = new JSONArray();

			rows.add(dfmc.getName());
			rows.add(getText("entityType" + dfmc.getEntity()));
			jsonObject.put("id", dfmc.getId());
			jsonObject.put("cell", rows);
			return jsonObject;

		}
		return jsonObject;
	}

	public String list() throws Exception {
		return LIST;
	}

	public String grid() {
		return "gridPage";
	}

	public String create() throws Exception {
		if (dynamicFeildMenuConfig == null) {
			command = "create";
			request.setAttribute(HEADING, getText("dynamicFeildMenuConfigCreate"));
			return INPUT;
		} else {
			// String code = "M" +
			// Long.toString(dynamicFeildMenuConfig.getId());
			// dynamicFeildMenuConfig.setCode(code);
			dynamicFeildMenuConfig.setName(dynamicFeildMenuConfig.getName());
			dynamicFeildMenuConfig.setIconClass(dynamicFeildMenuConfig.getIconClass());
			dynamicFeildMenuConfig.setEntity(dynamicFeildMenuConfig.getEntity());
			dynamicFeildMenuConfig.setAgentType(dynamicFeildMenuConfig.getAgentType());
			String txnType =uniqueIDGenerator.getDynmaicTxnType();
			if(dynamicFeildMenuConfig.getTxnType().equalsIgnoreCase("99")){
				dynamicFeildMenuConfig.setTxnType(txnType);
			}else{
			dynamicFeildMenuConfig.setTxnType(dynamicFeildMenuConfig.getTxnType());
			}
			
			if(mobTxnType.equalsIgnoreCase("99")){
				dynamicFeildMenuConfig.setmTxnType(txnType);
			}else{
			dynamicFeildMenuConfig.setmTxnType(mobTxnType);
			}
			dynamicFeildMenuConfig.setOrder(farmerService.findDynamicMenuMaxOrderNo()+1);
			dynamicFeildMenuConfig.setRevisionNo(DateUtil.getRevisionNumber());
			//dynamicFeildMenuConfig.setTxnType(uniqueIDGenerator.getDynmaicTxnType());
			List<String> selectedFieldLis = Arrays.asList(selectedFieldName.split(",")).stream().map(x -> x.trim())
					.collect(Collectors.toList());
			List<DynamicFieldConfig> allFields = farmerService.listDynamicFieldsBySectionCode(selectedName);
			List<DynamicFieldConfig> fieldList = allFields.stream().filter(u -> selectedFieldLis.contains(u.getCode()))
					.collect(Collectors.toList());
			Map<Long, List<DynamicFieldConfig>> parentrList = allFields.stream().filter(u -> u.getReferenceId() != null)
					.collect(Collectors.groupingBy(DynamicFieldConfig::getReferenceId));

			List<DynamicSectionConfig> sectionList = farmerService.listDynamicSectionsBySectionId(
					Arrays.asList(selectedName.split(",")).stream().map(x -> x.trim()).collect(Collectors.toList()));

			SortedSet<DynamicMenuSectionMap> dsc = new TreeSet<DynamicMenuSectionMap>();
			SortedSet<DynamicMenuFieldMap> dfc = new TreeSet<DynamicMenuFieldMap>();
			order = new AtomicInteger(0);
			fieldList.stream().forEach(field -> {
				DynamicMenuFieldMap fmap = new DynamicMenuFieldMap();
				fmap.setField(field);
				fmap.setMenu(dynamicFeildMenuConfig);
				fmap.setOrder(order.incrementAndGet());
				dfc.add(fmap);
				if (parentrList.containsKey(field.getId())) {
					List<DynamicFieldConfig> chList = parentrList.get(field.getId());
					chList.stream().forEach(f -> {
						DynamicMenuFieldMap ffmap = new DynamicMenuFieldMap();
						ffmap.setField(f);
						ffmap.setMenu(dynamicFeildMenuConfig);
						ffmap.setOrder(order.incrementAndGet());
						dfc.add(ffmap);
					});

				}
			});
			order = new AtomicInteger(0);
			sectionList.stream().forEach(field -> {
				DynamicMenuSectionMap fmap = new DynamicMenuSectionMap();
				fmap.setSection(field);
				fmap.setMenu(dynamicFeildMenuConfig);
				fmap.setOrder(order.incrementAndGet());

				dsc.add(fmap);
			});

			dynamicFeildMenuConfig.setDynamicFieldConfigs(dfc);
			dynamicFeildMenuConfig.setDynamicSectionConfigs(dsc);

			farmerService.addDynamicFeildMenuConfig(dynamicFeildMenuConfig);

			if(!StringUtil.isEmpty(serviceM) && serviceM!=null){
			if (serviceM == 1 && !StringUtil.isEmpty(getRole()) && !getRole().equalsIgnoreCase("0")) {
				String url = "dynamicCertification_list.action?txnType=" + dynamicFeildMenuConfig.getTxnType();
				String ent = "service.dynamicCertification" ;
				String action_Id =  "list~1,create~2,update~3,delete~4";
				farmerService.save_subMenu("3", dynamicFeildMenuConfig.getName(), "Dynamic Certification", url, "100", ent, action_Id,getRole());
			} else {
				addActionError(getText("emptyRole"));
				return INPUT;
			}
			}
			
			if(!StringUtil.isEmpty(reportM) && reportM!=null){
			if (reportM == 1 && !StringUtil.isEmpty(getRole()) && !getRole().equalsIgnoreCase("0")) {
				String url = "dynmaicCertificationReport_list.action?txnType=" + dynamicFeildMenuConfig.getTxnType();
				String ent = "report.dynmaicCertification";
				farmerService.save_subMenu("4", dynamicFeildMenuConfig.getName() , "Dynamic Certification Report", url, "100", ent,	"1", getRole());
			} else {
				addActionError(getText("emptyRole"));
				return INPUT;
			}
			}

		}
		return LIST;

	}

	public String detail() throws Exception {
		String view = "";
		if (id != null && !id.equals("")) {
			dynamicFeildMenuConfig = farmerService.findDynamicMenuConfigById(Long.valueOf(id));

			if (!ObjectUtil.isListEmpty(dynamicFeildMenuConfig.getDynamicSectionConfigs())) {
				String sl = "";

				for (DynamicMenuSectionMap temp : dynamicFeildMenuConfig.getDynamicSectionConfigs()) {
					sl = sl + temp.getSection().getSectionName() + ", ";

				}
				if (sl != null && sl.length() > 2) {
					sectionNames = sl.substring(0, sl.length() - 2);
				}

			}

			if (!ObjectUtil.isEmpty(dynamicFeildMenuConfig.getDynamicFieldConfigs())) {
				String s2 = "";

				for (DynamicMenuFieldMap dfc : dynamicFeildMenuConfig.getDynamicFieldConfigs()) {
					s2 = s2 + dfc.getField().getComponentName() + ", ";
				}
				if (s2 != null && s2.length() > 2) {
					fieldNames = s2.substring(0, s2.length() - 2);
				}
			}

			if (dynamicFeildMenuConfig == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			setCurrentPage(getCurrentPage());
			command = UPDATE;
			view = DETAIL;
			request.setAttribute(HEADING, getText("dynamicFeildMenuConfigdetail"));
		} else {
			request.setAttribute(HEADING, getText("dynamicFeildMenuConfiglist"));
			return LIST;
		}
		return view;
	}

	public String update() throws Exception {

		if (id != null && !id.equals("")) {
			dynamicFeildMenuConfig = farmerService.findDynamicMenuConfigById(Long.valueOf(id));
			/*
			 * for(DynamicSectionConfig dsf :
			 * dynamicFeildMenuConfig.getDynamicSectionConfigs()){
			 * setSelectedName(dsf.getSectionName());
			 * populateSelectedSection(id);
			 * 
			 * } for(DynamicFieldConfig dfc :
			 * dynamicFeildMenuConfig.getDynamicFieldConfigs()){
			 * setSelectedFieldName(dfc.getComponentName());
			 * populateSelectedField(id);
			 * 
			 * }
			 */

			if (dynamicFeildMenuConfig == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			
			setCurrentPage(getCurrentPage());
				
			dynamicFeildMenuConfig.getDynamicSectionConfigs().stream().forEach(menu -> {
				selectedSections.put(menu.getSection().getSectionCode(), menu.getSection().getSectionName());
			});
			dynamicFeildMenuConfig.getDynamicFieldConfigs().stream().forEach(menu -> {
				selectedField.put(menu.getField().getCode(), menu.getField().getComponentName());
			});
			
			id = null;
			command = UPDATE;
			request.setAttribute(HEADING, getText("dynamicFeildMenuConfigupdate"));
		} else {
			if (dynamicFeildMenuConfig != null) {
				DynamicFeildMenuConfig tempDynamicMenuConfig = farmerService
						.findDynamicMenuConfigById(Long.valueOf(dynamicFeildMenuConfig.getId()));
				if (tempDynamicMenuConfig == null) {
					addActionError(NO_RECORD);
					return REDIRECT;
				}
				setCurrentPage(getCurrentPage());
				tempDynamicMenuConfig.setName(dynamicFeildMenuConfig.getName());
				tempDynamicMenuConfig.setIconClass(dynamicFeildMenuConfig.getIconClass());
				tempDynamicMenuConfig.setTxnType(dynamicFeildMenuConfig.getTxnType());
				tempDynamicMenuConfig.setIsSeason(dynamicFeildMenuConfig.getIsSeason());
				tempDynamicMenuConfig.setIsSingleRecord(dynamicFeildMenuConfig.getIsSingleRecord());
				tempDynamicMenuConfig.setAgentType(dynamicFeildMenuConfig.getAgentType());
				if(dynamicFeildMenuConfig.getTxnType().equalsIgnoreCase("99")){
					tempDynamicMenuConfig.setTxnType(uniqueIDGenerator.getDynmaicTxnType());
				}else{
					tempDynamicMenuConfig.setTxnType(dynamicFeildMenuConfig.getTxnType());
				}
				if(mobTxnType.equalsIgnoreCase("99")){
					tempDynamicMenuConfig.setmTxnType(uniqueIDGenerator.getDynmaicTxnType());
				}else{
					tempDynamicMenuConfig.setmTxnType(mobTxnType);
				}
				List<String> selectedFieldLis = Arrays.asList(selectedFieldName.split(",")).stream().map(x -> x.trim())
						.collect(Collectors.toList());
				List<DynamicFieldConfig> allFields = farmerService.listDynamicFieldsBySectionCode(selectedName);
				List<DynamicFieldConfig> fieldList = allFields.stream()
						.filter(u -> selectedFieldLis.contains(u.getCode())).collect(Collectors.toList());
				Map<Long, List<DynamicFieldConfig>> parentrList = allFields.stream()
						.filter(u -> u.getReferenceId() != null)
						.collect(Collectors.groupingBy(DynamicFieldConfig::getReferenceId));

				List<DynamicSectionConfig> sectionList = farmerService.listDynamicSectionsBySectionId(Arrays
						.asList(selectedName.split(",")).stream().map(x -> x.trim()).collect(Collectors.toList()));

				SortedSet<DynamicMenuSectionMap> dsc = new TreeSet<DynamicMenuSectionMap>();
				SortedSet<DynamicMenuFieldMap> dfc = new TreeSet<DynamicMenuFieldMap>();
				order = new AtomicInteger(0);
				fieldList.stream().forEach(field -> {
					DynamicMenuFieldMap fmap = new DynamicMenuFieldMap();
					fmap.setField(field);
					fmap.setMenu(dynamicFeildMenuConfig);
					fmap.setOrder(order.incrementAndGet());
					dfc.add(fmap);
					if (parentrList.containsKey(field.getId())) {
						List<DynamicFieldConfig> chList = parentrList.get(field.getId());
						chList.stream().forEach(f -> {
							DynamicMenuFieldMap ffmap = new DynamicMenuFieldMap();
							ffmap.setField(f);
							ffmap.setMenu(dynamicFeildMenuConfig);
							ffmap.setOrder(order.incrementAndGet());
							dfc.add(ffmap);
						});

					}
				});
				order = new AtomicInteger(0);
				sectionList.stream().forEach(field -> {
					DynamicMenuSectionMap fmap = new DynamicMenuSectionMap();
					fmap.setSection(field);
					fmap.setMenu(dynamicFeildMenuConfig);
					fmap.setOrder(order.incrementAndGet());

					dsc.add(fmap);
				});

				tempDynamicMenuConfig.setDynamicFieldConfigs(dfc);
				tempDynamicMenuConfig.setDynamicSectionConfigs(dsc);
				farmerService.editDynamicFeildMenuConfig(tempDynamicMenuConfig);
			}
			request.setAttribute(HEADING, getText("dynamicFeildMenuConfiglist"));
			return LIST;
		}
		return super.execute();
	}

	private void populateSelectedField(String menuId) {
		if (!StringUtil.isEmpty(menuId)) {

			selectedField = farmerService.findDynamicFieldsByMenuId(Long.valueOf(menuId)).stream()
					.collect(Collectors.toMap(obj -> String.valueOf(obj[1]), obj -> String.valueOf(obj[2])));
			availableFieldName = farmerService
					.listOfDynamicFields(
							selectedSections.entrySet().stream().map(x -> x.getKey()).collect(Collectors.toList()))
					.stream().filter(u -> (u[3] == null))
					.collect(Collectors.toMap(kv -> String.valueOf(kv[1]), kv -> String.valueOf(kv[2])));

			selectedField.forEach((k, v) -> {
				availableFieldName.remove(k.trim());
			});

		}
	}

	private void populateSelectedSection(String menuId) {

		if (!StringUtil.isEmpty(menuId)) {
			selectedSections = farmerService.findDynamicSectionByMenuId(Long.valueOf(menuId)).stream()
					.collect(Collectors.toMap(obj -> String.valueOf(obj[1]), obj -> String.valueOf(obj[2])));
			availableName = farmerService.listOfDynamicSections();

			System.out.println("selectedSections:" + selectedSections);

			selectedSections.forEach((k, v) -> {
				availableName.remove(k.trim());
			});

			setAvailableSectionName(availableName);
		}
	}

	public Map<String, String> getAvailableSection() {

		List<DynamicSectionConfig> dynamicSectionsList = farmerService.listDynamicSections();

		if (!ObjectUtil.isListEmpty(dynamicSectionsList)) {

			for (DynamicSectionConfig ds : dynamicSectionsList) {
				// availableSection.add(ds.getSectionCode());
				availableSection.put(ds.getSectionCode(), ds.getSectionName());
			}

		}

		selectedSections.forEach((k, v) -> {
			availableSection.remove(k.trim());
		});

		return availableSection;
	}

	public void setAvailableSection(Map<String, String> availableSection) {
		this.availableSection = availableSection;
	}

	@SuppressWarnings("unchecked")
	public void populateFields() {

		JSONObject jsonObject = new JSONObject();

		Map<String, JSONArray> jsonArray = new LinkedHashMap<>();
		JSONArray dynamicFieldArray = new JSONArray();
		List<DynamicFieldConfig> dfc = new ArrayList<DynamicFieldConfig>();

		if (selectedName != null && !StringUtil.isEmpty(selectedName)) {
			selectedName = StringUtil.removeLastComma(selectedName);
			String[] array1 = selectedName.split(",");
			dfc = farmerService.listDynamicFieldsBySectionId(Arrays.asList(array1));
			dfc.stream().forEach(dynamicField -> {
				String[] array = new String[] {};
				if (selVals != null && !StringUtil.isEmpty(selVals)) {
					selVals = StringUtil.removeLastComma(selVals);
					array = selVals.split(",");
				}
				if (!Arrays.asList(array).contains(dynamicField.getCode()) && dynamicField.getReferenceId() == null) {
					dynamicFieldArray.add(getJSONObject(dynamicField.getCode(), dynamicField.getComponentName()));
				}
			});
		}

		jsonArray.put("dynamicField", dynamicFieldArray);
		JSONObject jsonobject = new JSONObject();
		jsonobject.putAll(jsonArray);
		printAjaxResponse(jsonobject, "text/html");
	}

	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}

	public String delete() throws Exception {

		if (this.getId() != null && !(this.getId().equals(EMPTY))) {
			dynamicFeildMenuConfig = farmerService.findDynamicMenuConfigById(Long.valueOf(id));

			if (dynamicFeildMenuConfig == null) {
				addActionError(NO_RECORD);
				return null;
			}
			if (!ObjectUtil.isEmpty(dynamicFeildMenuConfig)) {
				farmerService.removedynamicFeildMenuConfig(dynamicFeildMenuConfig);
			}

		}

		request.setAttribute(HEADING, getText(" "));
		return LIST;

	}

	public DynamicFeildMenuConfig getDynamicFeildMenuConfig() {
		return dynamicFeildMenuConfig;
	}

	public void setDynamicFeildMenuConfig(DynamicFeildMenuConfig dynamicFeildMenuConfig) {
		this.dynamicFeildMenuConfig = dynamicFeildMenuConfig;
	}

	public Map<Integer, String> getEntityType() {
		return entityType;
	}

	public void setEntityType(Map<Integer, String> entityType) {
		this.entityType = entityType;
	}

	public List<String> getAvailableName() {
		return availableName;
	}

	public void setAvailableName(List<String> availableName) {
		this.availableName = availableName;
	}

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	public Map<String, String> getSelectedSections() {
		return selectedSections;
	}

	public void setSelectedSections(Map<String, String> selectedSections) {
		this.selectedSections = selectedSections;
	}

	public String getEntType() {
		return entType;
	}

	public void setEntType(String entType) {
		this.entType = entType;
	}

	public String getSelVals() {
		return selVals;
	}

	public void setSelVals(String selVals) {
		this.selVals = selVals;
	}

	public Map<String, String> getAvailableField() {
		return availableField;
	}

	public void setAvailableField(Map<String, String> availableField) {
		this.availableField = availableField;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSelectedName() {
		return selectedName;
	}

	public void setSelectedName(String selectedName) {
		this.selectedName = selectedName;
	}

	public String getSelectedFieldName() {
		return selectedFieldName;
	}

	public void setSelectedFieldName(String selectedFieldName) {
		this.selectedFieldName = selectedFieldName;
	}

	public String getSectionNames() {
		return sectionNames;
	}

	public void setSectionNames(String sectionNames) {
		this.sectionNames = sectionNames;
	}

	public String getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(String fieldNames) {
		this.fieldNames = fieldNames;
	}

	public List<String> getAvailableSectionName() {
		return availableSectionName;
	}

	public void setAvailableSectionName(List<String> availableSectionName) {
		this.availableSectionName = availableSectionName;
	}

	public Map<String, String> getSelectedField() {
		return selectedField;
	}

	public void setSelectedField(Map<String, String> selectedField) {
		this.selectedField = selectedField;
	}

	public List<String> getAvailableDynamicFieldName() {
		return availableDynamicFieldName;
	}

	public void setAvailableDynamicFieldName(List<String> availableDynamicFieldName) {
		this.availableDynamicFieldName = availableDynamicFieldName;
	}

	public Map<String, String> getAvailableFieldName() {
		return availableFieldName;
	}

	public void setAvailableFieldName(Map<String, String> availableFieldName) {
		this.availableFieldName = availableFieldName;
	}

	public Integer getServiceM() {
		return serviceM;
	}

	public void setServiceM(Integer serviceM) {
		this.serviceM = serviceM;
	}

	public Integer getReportM() {
		return reportM;
	}

	public void setReportM(Integer reportM) {
		this.reportM = reportM;
	}

	public String getParentMenu() {
		return parentMenu;
	}

	public void setParentMenu(String parentMenu) {
		this.parentMenu = parentMenu;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Map<String, String> getRoles() {
		List<Object[]> role = new ArrayList<Object[]>();
		Map<String, String> RoleMap = new LinkedHashMap<String, String>();
		role = farmerService.getRole();
		for (Object[] object : role) {
			RoleMap.put(String.valueOf(object[0]), object[1].toString());
		}
		return RoleMap;
	}

	public Map<String, String> getParentMenus() {
		List<Object[]> parentMenus = new ArrayList<Object[]>();
		Map<String, String> parentMenuMap = new LinkedHashMap<String, String>();
		parentMenus = farmerService.getParentMenus();
		for (Object[] object : parentMenus) {
			parentMenuMap.put(String.valueOf(object[0]), object[1].toString());
		}
		return parentMenuMap;
	}

	public Map<String, String> getYesNoList() {

		Map<String, String> parentMenuMap = new LinkedHashMap<String, String>();
		parentMenuMap.put("1", "Yes");
		parentMenuMap.put("0", "No");
		return parentMenuMap;
	}

	public String getIsSeason() {
		return isSeason;
	}

	public void setIsSeason(String isSeason) {
		this.isSeason = isSeason;
	}

	public Map<String, String> getIsSeasonList() {
		
		isSeasonList.put("0", getText("no"));
		isSeasonList.put("1", getText("yes"));
		
		return isSeasonList;
	}

	public void setIsSeasonList(Map<String, String> isSeasonList) {
		this.isSeasonList = isSeasonList;
	}

	public Map<String, String> getTxnTypeList() {
		
		txnTypeList.put("308",getText("farmer"));
		txnTypeList.put("359",getText("farm"));
		txnTypeList.put("357",getText("sowing"));
		txnTypeList.put("99", getText("separateMenu"));
		
		return txnTypeList;
	}

	public Map<String, String> getMTxnTypeList(){
		return getTxnTypeList();
	}
	public void setTxnTypeList(Map<String, String> txnTypeList) {
		this.txnTypeList = txnTypeList;
	}
	public Map<String, String> getAgentType(){
		return agentService.listAgentType().stream().collect(Collectors.toMap(u-> u.getCode(),u -> u.getName()));
	}
	public IAgentService getAgentService() {
		return agentService;
	}

	public void setAgentService(IAgentService agentService) {
		this.agentService = agentService;
	}

	public String getMobTxnType() {
		return mobTxnType;
	}

	public void setMobTxnType(String mobTxnType) {
		this.mobTxnType = mobTxnType;
	}


	
}
