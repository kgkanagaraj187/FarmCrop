package com.sourcetrace.esesw.view.general;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.ese.entity.DynamicData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sourcetrace.eses.entity.DynamicFieldConfig;
import com.sourcetrace.eses.entity.DynamicSectionConfig;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.FarmCatalogueMaster;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IReportService;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.view.SwitchAction;

public class CreationToolAction extends SwitchAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 804839924142063898L;
	
	private IReportService reportService;
	private IFarmerService farmerService;
	private IPreferencesService preferncesService;
	private DynamicFieldConfig dynamicFieldConfig;
	private ICatalogueService catalogueService;
	private String componentDatas;
	
	

	private DynamicSectionConfig dsc;
	private DynamicFieldConfig dfc;
	private String selectedType;
	private String selectedCatalogueCode;
	private String id;
	private String catalogueCode;
	private String sectionCode;
	private String listComponentsOrder;
	private String sectionName;
	private String existingcatalogueType;
	
	private String selectedFieldNames;
	
	private String selList;
	private  static  Long listId;

	private String referenceIdForListComponents;
	private String parentFieldId;
	private String parentDependencyKey;
	private String parentDependentId;
	public String list() {
		return LIST;
	}
	
	public String grid() {
		return "gridPage";
	}
	
	public String editListOrder(){
		return "editListOrder";
	}
	
	public String addListFields(){
		return "addListFields";
	}
	public String detail() {
		//System.out.println(getId());
		dfc =	farmerService.findDynamicFieldConfigById(Long.valueOf(getId()));
		if(getDfc().getComponentType().equals("8")){
			return "editListOrder";
		}

		return "edit";
	}
	
	public String detailForSectionGrid() {
		//System.out.println(getId());
		dsc =	farmerService.findDynamicSectionConfigById(Long.valueOf(getId()));
		//getDfc().setComponentName(row.getComponentName());
		//System.out.println(dfc.getComponentName());
		return "sectionEdit";
	}
	
	public void saveSection(){
		DynamicSectionConfig dsc = new DynamicSectionConfig();
		dsc.setBranchId(getBranchId());
		dsc.setSectionName(getSectionName());
		farmerService.saveDynamicSection(dsc);
		String sectionCode = "S"+Long.toString(dsc.getId());
		dsc.setSectionCode(StringUtil.getExact(sectionCode, 5));
		farmerService.updateDynamicSection(dsc);
	}
	
	public void updateSection(){
		String updatedSectionName = getDsc().getSectionName();
		String[] id_code = id.split("-");
		dsc =	farmerService.findDynamicSectionConfigById(Long.parseLong(id_code[0]));
		getDsc().setId(Long.parseLong(id_code[0]));
		getDsc().setSectionName(updatedSectionName);
		farmerService.updateDynamicSection(getDsc());
		//return "redirectToList";
	}

	public void deleteSection(){
		
		 String[] id_code = id.split("-");
		 DynamicSectionConfig dsc = new DynamicSectionConfig();
		 dsc.setId(Long.parseLong(id_code[0]));
		 dsc.setSectionCode(id_code[1]);
		farmerService.deleteSection(dsc);
		//return "redirectToList";
	}
	
	public String saveField(){
		
		if (!StringUtil.isEmpty(getSelectedCatalogueCode())) {
		getDfc().setCatalogueType(getSelectedCatalogueCode());
		}
		
		if(!StringUtil.isEmpty(getParentDependentId())){
			getDfc().getParentDepen().setId(Long.valueOf(getParentDependentId()));
			getDfc().setParentDependencyKey(getParentDependencyKey());
		}
		
		farmerService.saveDynamicField(getDfc());
		String Componentcode = "C"+Long.toString(getDfc().getId());
		getDfc().setCode(StringUtil.getExact(Componentcode, 5));
		farmerService.updateDynamicField(getDfc());
		
		
		if (!StringUtil.isEmpty(getParentDependencyKey())){
			DynamicFieldConfig parentField = farmerService.findDynamicFieldConfigById(Long.valueOf(getParentDependentId()));
			if(StringUtil.isEmpty(parentField.getCatDependencyKey())){
				parentField.setCatDependencyKey(getParentDependencyKey());
			}else{
				String catDepKey = parentField.getCatDependencyKey();
				
				String[] parentDependencyKey = getParentDependencyKey().split(",");
				for (String string : parentDependencyKey) {
					if(!parentField.getCatDependencyKey().contains(string.trim())){
						catDepKey = catDepKey+","+string.trim();
					}
				}
				parentField.setCatDependencyKey(catDepKey);
			}
			
			farmerService.updateDynamicField(parentField);
		}
		
		/*if (!StringUtil.isEmpty(getDfc().getParentDepen().getId())){
			DynamicFieldConfig parentField = farmerService.findDynamicFieldConfigById(getDfc().getParentDepen().getId());
			if(StringUtil.isEmpty(parentField.getCatDependencyKey())){
				parentField.setCatDependencyKey(getDfc().getParentDependencyKey());
			}else{
				String catDepKey = parentField.getCatDependencyKey()+","+getDfc().getParentDependencyKey();
				parentField.setCatDependencyKey(catDepKey);
			}
			
			farmerService.updateDynamicField(parentField);
		}
		*/
		
		
		/*String dependent_fields[] = getDfc().getDependencyKey().split(",");
		for (String string : dependent_fields) {
			farmerService.updateDependencyKeyForDependentFieldsOfFormula(string,formula_label_code);
		}*/
		
		
		if(getDfc().getComponentType().equals("8")){
			setListId(getDfc().getId());
			
			if (!StringUtil.isEmpty(getSelectedFieldNames())) {
				//String[] data  = getSelectedFieldNames().split(",");
				String[] data  = getSelectedFieldNames().split(",");
				for(String da : data){
					DynamicFieldConfig selec = farmerService.findDynamicFieldConfigById(Long.valueOf(da.toString().trim()));
					if(!ObjectUtil.isEmpty(selec)){
						selec.setReferenceId(getDfc().getId());
						farmerService.updateDynamicField(selec);
					}
				}
				}
			
			getDfc().setComponentName(getDfc().getComponentName()+" - Add button");
			getDfc().setComponentType("10");
			getDfc().setReferenceId(getDfc().getId());
			
			farmerService.saveDynamicField(getDfc());
			String Componentcode2 = "C"+Long.toString(getDfc().getId());
			getDfc().setCode(StringUtil.getExact(Componentcode2, 5));
			farmerService.updateDynamicField(getDfc());
		}
		
		if(getDfc().getComponentType().equals("7")){
			String formula_label_code = getDfc().getCode();
			String dependent_fields[] = getDfc().getDependencyKey().split(",");
			for (String string : dependent_fields) {
				farmerService.updateDependencyKeyForDependentFieldsOfFormula(string.trim(),formula_label_code.trim());
			}
		}
		
		/*if (!StringUtil.isEmpty(getDfc().getReferenceId()) && getDfc().getComponentType().equals("10")) {
			return "redirectToAddListFields";
			}*/
		
		return REDIRECT;
	}
	
	public String deleteField(){
		DynamicFieldConfig df = farmerService.findDynamicFieldConfigById(getDfc().getId());
		
		if(df.getComponentType().equals("7")){
			String formula_label_code = "";
			String dependent_fields[] = df.getDependencyKey().split(",");
			for (String string : dependent_fields) {
				farmerService.updateDependencyKeyForDependentFieldsOfFormula(string,formula_label_code);
			}
		}
		
		if(df.getComponentType().equals("8")){
			farmerService.deleteListFields(df.getId());
		}
		farmerService.deleteField(df);
		return REDIRECT;
	}
	
	public String updateField(){
		
		if (!StringUtil.isEmpty(getSelectedCatalogueCode())) {
		getDfc().setCatalogueType(getSelectedCatalogueCode());
		}
		
		if(!StringUtil.isEmpty(getParentDependentId())){
			getDfc().getParentDepen().setId(Long.valueOf(getParentDependentId()));
			getDfc().setParentDependencyKey(getParentDependencyKey());
		}
		
		String Componentcode = "C"+Long.toString(getDfc().getId());
		getDfc().setCode(StringUtil.getExact(Componentcode, 5));
		farmerService.updateDynamicField(getDfc());
		
		
		if(getDfc().getComponentType().equals("7")){
			String formula_label_code = getDfc().getCode();
			String dependent_fields[] = getDfc().getDependencyKey().split(",");
			for (String string : dependent_fields) {
				farmerService.updateDependencyKeyForDependentFieldsOfFormula(string,formula_label_code);
			}
		}
		
		
		
		if (!StringUtil.isEmpty(getParentDependencyKey())){
			DynamicFieldConfig parentField = farmerService.findDynamicFieldConfigById(Long.valueOf(getParentDependentId()));
			if(StringUtil.isEmpty(parentField.getCatDependencyKey())){
				parentField.setCatDependencyKey(getParentDependencyKey());
			}else{
				String catDepKey = parentField.getCatDependencyKey();
				
				String[] parentDependencyKey = getParentDependencyKey().split(",");
				for (String string : parentDependencyKey) {
					if(!parentField.getCatDependencyKey().contains(string.trim())){
						catDepKey = catDepKey+","+string.trim();
					}
				}
				parentField.setCatDependencyKey(catDepKey);
			}
			
			farmerService.updateDynamicField(parentField);
		}
		
		return REDIRECT;
	}
	
	public void updateFieldsReferenceId(){
		String fields_id[] = getSelList().split(",");
		ArrayList<Long> fieldsIdList = new ArrayList<Long>();
		for (int i=0;i<fields_id.length;i++) {
			fieldsIdList.add(Long.valueOf(fields_id[i]));
			farmerService.updateListComponentsOrder(Long.valueOf(fields_id[i].trim()));
		}
		farmerService.updateFieldsReferenceId(getListId(),fieldsIdList);
		farmerService.updateListButtonOrder(getListId());
	}
	
  

	/**
	 * @return the selList
	 */
	public String getSelList() {
		return selList;
	}

	/**
	 * @param selList the selList to set
	 */
	public void setSelList(String selList) {
		this.selList = selList;
	}

	public String updateListOrder(){
		//System.out.println(getReferenceIdForListComponents());
		//System.out.println(getListComponentsOrder());
		String[] typesArray = getListComponentsOrder().split(",");
		for (String string : typesArray) {
			//System.out.println(Integer.parseInt(string.trim()));
			farmerService.updateListComponentsOrder(Long.valueOf(string.trim()));
			farmerService.addNewComponentIntoList(Long.valueOf(string.trim()),Long.valueOf(getReferenceIdForListComponents().trim()));
		}
		
		return REDIRECT;
	}
	
	public void getCatalogueTypezFromFarmCatalogueMaster(){
		JSONArray catalogueTypez = new JSONArray();
		catalogueService.listFarmCatalogueMatsters().stream().forEach(ct -> {
			catalogueTypez.add(getJSONObject(ct.getTypez(),ct.getName()));
		});

		sendAjaxResponse(catalogueTypez);
	}
	
	public void populateCatalogueValuesByType() throws Exception {
		List<Integer> typeList = new ArrayList<Integer>();
		String[] typesArray = getSelectedType().split(",");
		for (String string : typesArray) {
			typeList.add(Integer.parseInt(string.trim()));
		}
		JSONArray catalogueValues = new JSONArray();
		catalogueService.listCataloguesByTypeArray(typeList).stream().forEach(fc -> {
			catalogueValues.add(getJSONObject(fc.getCode(),fc.getName()));
		});

		sendAjaxResponse(catalogueValues);
	}
	
	
	public void getLitsComponentsBySectionCodeAndListId() throws Exception {
		
		JSONArray listComponents = new JSONArray();
		farmerService.getLitsComponentsBySectionCodeAndListId(getSectionCode(),getId()).stream().forEach(fc -> {
			listComponents.add(getJSONObject(fc.getId(),fc.getComponentName()));
		});

		sendAjaxResponse(listComponents);
	}
	
	public void findCatalogueTypeByCataloueCode() throws Exception {
		List<String> catalogueCode = new ArrayList<String>();
		JSONArray catalogueTypes = new JSONArray();
		String[] catalogueCodeArray = getCatalogueCode().split(",");
			for (int i = 0; i < catalogueCodeArray.length; i++) {
				catalogueCode.add(catalogueCodeArray[i]);	
			}
		List<FarmCatalogue> catalogueTypezList = catalogueService.findCatalogueTypeByCataloueCode(catalogueCode);
			for (FarmCatalogue object : catalogueTypezList) {
				catalogueTypes.add(getJSONObject(object.getCode(),Integer.toString(object.getTypez()))); 
			}
		sendAjaxResponse(catalogueTypes);
	}
	
	
	
	public void getListComponents(){
		JSONArray list_JSONArray = new JSONArray();
		List<DynamicFieldConfig> branchMasters = farmerService.getListComponent(getSectionCode());
			for (DynamicFieldConfig objects : branchMasters) {
				//System.out.println(objects.getId()+" - "+objects.getComponentName());
				list_JSONArray.add(getJSONObject(Long.toString(objects.getId()),objects.getComponentName())); 
			}
		sendAjaxResponse(list_JSONArray);
	}
	
	public void getFieldsListForFormula(){
		JSONArray textBoxList_JSONArray = new JSONArray();
		List<DynamicFieldConfig> textBoxList = farmerService.getFieldsListForFormula();
			for (DynamicFieldConfig objects : textBoxList) {
				textBoxList_JSONArray.add(getJSONObject(objects.getCode(),objects.getComponentName()+" - "+objects.getCode())); 
			}
		sendAjaxResponse(textBoxList_JSONArray);
	}
	
	public void populateExistingcatalogueType(){
		JSONArray JSONArray = new JSONArray();
		FarmCatalogueMaster fcm = catalogueService.findFarmCatalogueMasterByCatalogueTypez(Integer.valueOf(getExistingcatalogueType()));
			JSONArray.add(getJSONObject(fcm.getId(),fcm.getName())); 
		sendAjaxResponse(JSONArray);
	}
	
	public void availableFieldsForList(){
		JSONArray JSONArray = new JSONArray();
		List<DynamicFieldConfig>  dynamicFieldConfig = new ArrayList<DynamicFieldConfig>();
		dynamicFieldConfig = farmerService.availableFieldsForList();
		for (DynamicFieldConfig dfc : dynamicFieldConfig) {
			JSONArray.add(getJSONObject(dfc.getId(),dfc.getComponentName())); 
		}
		sendAjaxResponse(JSONArray);
	}
	
	@SuppressWarnings("unchecked")
	public String data() throws Exception {
		Map<String, String> searchRecord = getJQGridRequestParam();
		DynamicFieldConfig filter = new DynamicFieldConfig();

		if (!StringUtil.isEmpty(searchRecord.get("dynamicSectionConfig.sectionCode"))) {
			DynamicSectionConfig config = new DynamicSectionConfig();
			config.setSectionCode(searchRecord.get("dynamicSectionConfig.sectionCode"));
			filter.setDynamicSectionConfig(config);
		}
		
		if (!StringUtil.isEmpty(searchRecord.get("componentType"))) {

			filter.setComponentType(searchRecord.get("componentType"));
		}
		
		if (!StringUtil.isEmpty(searchRecord.get("componentName"))) {

			filter.setComponentName(searchRecord.get("componentName"));
		}
		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		return sendJQGridJSONResponse(data);
	}
	
	@SuppressWarnings("unchecked")
	public String dataForSectionGrid() throws Exception {
		Map<String, String> searchRecord = getJQGridRequestParam();
		DynamicSectionConfig filter = new DynamicSectionConfig();
		
		if (!StringUtil.isEmpty(searchRecord.get("sectionName"))) {
			filter.setSectionName(searchRecord.get("sectionName"));
		}
		
		if (!StringUtil.isEmpty(searchRecord.get("sectionCode"))) {
			filter.setSectionCode(searchRecord.get("sectionCode"));
		}
	
		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		return sendJQGridJSONResponseForSectionGrid(data);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected String sendJQGridJSONResponseForSectionGrid(Map data) throws Exception {

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
				rows.add(toJSONForSectionGrid(record));
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
	
	public JSONObject toJSONForSectionGrid(Object record) {
	JSONObject jsonObject = null;
	jsonObject = new JSONObject();

	DynamicSectionConfig dfc = (DynamicSectionConfig) record;
	JSONArray rows = new JSONArray();
	
	rows.add(dfc.getSectionCode());
	rows.add(dfc.getSectionName());
	
	jsonObject.put("id", dfc.getId()+"-"+dfc.getSectionCode());
	jsonObject.put("cell", rows);
	return jsonObject;
}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object record) {
		JSONObject jsonObject = null;
		jsonObject = new JSONObject();

		DynamicFieldConfig dynamicFieldConfig = (DynamicFieldConfig) record;
		JSONArray rows = new JSONArray();
		
		rows.add(dynamicFieldConfig.getDynamicSectionConfig().getSectionCode() + "-"+ dynamicFieldConfig.getDynamicSectionConfig().getSectionName());
		rows.add(getText("componentType" + dynamicFieldConfig.getComponentType()));
		rows.add(dynamicFieldConfig.getComponentName());
		//rows.add(String.valueOf(dynamicFieldConfig.getComponentDataType()));
		//rows.add(String.valueOf(dynamicFieldConfig.getComponentMaxLength()));
		//rows.add(String.valueOf(dynamicFieldConfig.getDefaultValue()));
		//rows.add(String.valueOf(dynamicFieldConfig.getIsRequired()));
		rows.add(dynamicFieldConfig.getIsRequired().equalsIgnoreCase("1") ? getText("yes") : getText("no"));
		//rows.add(String.valueOf(dynamicFieldConfig.getDataFormat()));
		//rows.add(String.valueOf(dynamicFieldConfig.getValidation()));
		if(dynamicFieldConfig.getCatalogueType() != null && !StringUtil.isEmpty(dynamicFieldConfig.getCatalogueType())){
			String catalogue = "";
			String ct = String.valueOf(dynamicFieldConfig.getCatalogueType());
			 String[] items = ct.split(",");
			   int commaCount = items.length;
			if (!StringUtil.isEmpty(ct)) {
				int limit = 1;
				for (String val : ct.split(",")) {
					if ((val.contains("CG"))) {
						FarmCatalogue fc = getCatlogueValueByCode(val.trim());
						//rows.add(fc.getName());
						
						if(limit == commaCount){
							catalogue=catalogue+fc.getName();
							//rows.add(entry.getValue());	
						}else{
							catalogue=catalogue+fc.getName()+",";
							//rows.add(entry.getValue()+",");	
							limit=limit+1;
						}
						
						
					} else {
						Integer type = new Integer(val.trim());
						Map<String, String> farmCatalougeList = getFarmCatalougeMap(type);
						int size = farmCatalougeList.size();
						int i = 1;
						
							
							for (Map.Entry<String, String> entry : farmCatalougeList.entrySet()) {
								if(i==size){
									catalogue = catalogue+entry.getValue();
									//rows.add(entry.getValue());	
								}else{
									catalogue = catalogue+entry.getValue()+",";
									//rows.add(entry.getValue()+",");	
									i=i+1;
								}
								
							}
							
							//rows.add(catalogue);	
						
						/*for (Map.Entry<String, String> entry : farmCatalougeList.entrySet()) {
							rows.add(entry.getValue());
						}*/

					}
				}
				rows.add(catalogue);
			}
			
		
		}else{
			rows.add("");
		}
		//rows.add(String.valueOf(dynamicFieldConfig.getReferenceId()));
		
		rows.add(dynamicFieldConfig.getIsMobileAvail().equalsIgnoreCase("1") ? getText("yes") : getText("no"));
		rows.add(dynamicFieldConfig.getIsReportAvail().equalsIgnoreCase("1") ? getText("yes") : getText("no"));
		//rows.add(String.valueOf(dynamicFieldConfig.getIsMobileAvail()));
		//rows.add(String.valueOf(dynamicFieldConfig.getIsReportAvail()));
		
		jsonObject.put("id", dynamicFieldConfig.getId());
		jsonObject.put("cell", rows);
		return jsonObject;
	}
	
	
	
	
	public String createList() throws IOException {

		String result = null;
		String message = "";
		int maxOrderNo = 0;
		String sectionCode = "";
		if (!StringUtil.isEmpty(componentDatas) && componentDatas.length() > 0) {
			Type listType1 = new TypeToken<List<DynamicData>>() {
			}.getType();
			List<DynamicData> dynamicList = new Gson().fromJson(componentDatas, listType1);
			for (DynamicData dynamicData : dynamicList) {
				if (maxOrderNo == 0) {
					sectionCode = farmerService.findSectionCodeByReferenceId(dynamicData.getListType());

				}
				dynamicFieldConfig = new DynamicFieldConfig();
				maxOrderNo = farmerService.findMaxOrderNo();
				DynamicSectionConfig sectionConfig = new DynamicSectionConfig();
				sectionConfig.setSectionCode(sectionCode);
				dynamicFieldConfig.setDynamicSectionConfig(sectionConfig);
				dynamicFieldConfig.setCode(sectionCode);
				dynamicFieldConfig.setCreatedDate(new Date());
				dynamicFieldConfig.setUpdatedDate(new Date());
				dynamicFieldConfig.setComponentType(dynamicData.getCompoType());
				dynamicFieldConfig.setComponentName(dynamicData.getName());
		//		dynamicFieldConfig.setComponentDataType(dynamicData.getDataType());
				dynamicFieldConfig.setComponentMaxLength(dynamicData.getMaxLength());
				dynamicFieldConfig.setIsRequired(dynamicData.getIsRequired());
				dynamicFieldConfig.setDefaultValue(dynamicData.getDefaultValue());
				dynamicFieldConfig.setCatalogueType(dynamicData.getCatalogue());
				dynamicFieldConfig.setReferenceId(Long.valueOf(dynamicData.getListType()));
				dynamicFieldConfig.setOrderSet(++maxOrderNo);
				farmerService.addDynamicComponents(dynamicFieldConfig);
				message = getText("componentAddedSuccess");
				result = LIST;
			}
			addActionError(message);

		} else {

			result = "createList";
		}

		return result;
	}

	public String create() throws IOException {
		String result = null;
		String message = "";
		if (!StringUtil.isEmpty(componentDatas) && componentDatas.length() > 0) {

			Type listType1 = new TypeToken<List<DynamicData>>() {
			}.getType();
			List<DynamicData> dynamicList = new Gson().fromJson(componentDatas, listType1);
			for (DynamicData dynamicData : dynamicList) {
				String componmentExists = farmerService.findComponentNameByDynamicField(dynamicData.getName());
				int maxOrderNo = farmerService.findMaxOrderNo();
				if (StringUtil.isEmpty(componmentExists)) {
					dynamicFieldConfig = new DynamicFieldConfig();
					dynamicFieldConfig.setCreatedDate(new Date());
					dynamicFieldConfig.setUpdatedDate(new Date());
					dynamicFieldConfig.setComponentType(dynamicData.getCompoType());
					dynamicFieldConfig.setComponentName(dynamicData.getName());
				//	dynamicFieldConfig.setComponentDataType(dynamicData.getDataType());
					dynamicFieldConfig.setComponentMaxLength(dynamicData.getMaxLength());
					dynamicFieldConfig.setIsRequired(dynamicData.getIsRequired());
					dynamicFieldConfig.setDefaultValue(dynamicData.getDefaultValue());
					dynamicFieldConfig.setCatalogueType(dynamicData.getCatalogue());
					dynamicFieldConfig.setOrderSet(++maxOrderNo);
					DynamicSectionConfig dynamicSectionConfig = new DynamicSectionConfig();
					dynamicSectionConfig.setSectionCode(dynamicData.getSectionCode());
					dynamicFieldConfig.setDynamicSectionConfig(dynamicSectionConfig);
					farmerService.addDynamicComponents(dynamicFieldConfig);
					message = getText("componentAddedSuccess");
					result = LIST;
				}

				else {
					addActionError(getText("componentNameExits"));
					result = INPUT;
				}
			}

			addActionError(message);

		}
		return result;

	}

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}

	public Map<Long, String> getCatalogueTypeList() {
		List<FarmCatalogueMaster> list = catalogueService.listFarmCatalogueMatsters();
		Map<Long, String> catalogueMap = new HashMap<>();
		for (FarmCatalogueMaster catalogue : list) {
			catalogueMap.put(catalogue.getId(), catalogue.getName());
		}
		return catalogueMap;

	}

	@SuppressWarnings("unchecked")
	public void populateSection() {
		JSONArray sectionArr = new JSONArray();
		farmerService.listDynamicSections().stream().forEach(sectionConfig -> {
			sectionArr.add(getJSONObject(sectionConfig.getSectionCode(),
					sectionConfig.getSectionCode() + " - " + sectionConfig.getSectionName()));
		});

		sendAjaxResponse(sectionArr);
	}

	@SuppressWarnings("unchecked")
	public void populateCatalogue() {
		JSONArray catArr = new JSONArray();
		catalogueService.listFarmCatalogueMatsters().forEach(fc -> {
			catArr.add(getJSONObject(fc.getId(), fc.getName()));
		});
		sendAjaxResponse(catArr);
	}

	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object id, Object name) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}

	public DynamicFieldConfig getDynamicFieldConfig() {
		return dynamicFieldConfig;
	}

	public void setDynamicFieldConfig(DynamicFieldConfig dynamicFieldConfig) {
		this.dynamicFieldConfig = dynamicFieldConfig;
	}

	public String getComponentDatas() {
		return componentDatas;
	}

	public void setComponentDatas(String componentDatas) {
		this.componentDatas = componentDatas;
	}

	public ICatalogueService getCatalogueService() {
		return catalogueService;
	}

	public void setCatalogueService(ICatalogueService catalogueService) {
		this.catalogueService = catalogueService;
	}

	public Map<Long, String> getComponentList() {
		List<Object[]> componentListValues = farmerService.findComponentListVal(getText("ListType"));
		Map<Long, String> componentListMap = new HashMap<>();
		for (Object[] obj : componentListValues) {
			componentListMap.put(Long.valueOf(String.valueOf(obj[0])), String.valueOf(obj[1]));

		}
		return componentListMap;
	}

	public DynamicSectionConfig getDsc() {
		return dsc;
	}

	public void setDsc(DynamicSectionConfig dsc) {
		this.dsc = dsc;
	}

	public DynamicFieldConfig getDfc() {
		return dfc;
	}

	public void setDfc(DynamicFieldConfig dfc) {
		this.dfc = dfc;
	}

	public String getSelectedType() {
		return selectedType;
	}

	public void setSelectedType(String selectedType) {
		this.selectedType = selectedType;
	}

	public String getSelectedCatalogueCode() {
		return selectedCatalogueCode;
	}

	public void setSelectedCatalogueCode(String selectedCatalogueCode) {
		this.selectedCatalogueCode = selectedCatalogueCode;
	}
	
	public IReportService getReportService() {
		return reportService;
	}

	public void setReportService(IReportService reportService) {
		this.reportService = reportService;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCatalogueCode() {
		return catalogueCode;
	}

	public void setCatalogueCode(String catalogueCode) {
		this.catalogueCode = catalogueCode;
	}

	public String getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(String sectionCode) {
		this.sectionCode = sectionCode;
	}

	public String getListComponentsOrder() {
		return listComponentsOrder;
	}

	public void setListComponentsOrder(String listComponentsOrder) {
		this.listComponentsOrder = listComponentsOrder;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getExistingcatalogueType() {
		return existingcatalogueType;
	}

	public void setExistingcatalogueType(String existingcatalogueType) {
		this.existingcatalogueType = existingcatalogueType;
	}

	public static Long getListId() {
		return listId;
	}

	public static void setListId(Long listId) {
		CreationToolAction.listId = listId;
	}

	public void listDynamicFieldsBySectionCode() {
		JSONArray dfcArray = new JSONArray();
		List<DynamicFieldConfig> dfc = new ArrayList<DynamicFieldConfig>();
		dfc = farmerService.listDynamicFieldsBySectionCode(getSectionCode());
		if(dfc != null){
			dfc.stream().forEach(dynamicField -> {
				dfcArray.add(getJSONObject(dynamicField.getId(),dynamicField.getComponentName()));
			});
		}
	
		
		sendAjaxResponse(dfcArray);
	}

	public String getReferenceIdForListComponents() {
		return referenceIdForListComponents;
	}

	public void setReferenceIdForListComponents(String referenceIdForListComponents) {
		this.referenceIdForListComponents = referenceIdForListComponents;
	}
	
	public Map<String, String> getParentFieldsList() {
		Map<String, String> result = new LinkedHashMap<>();
		List<Object[]> list = new ArrayList<>();
		list = farmerService.listParentFields();
		
			for (Object[] objects : list) {
				result.put(String.valueOf(objects[0]), String.valueOf(objects[1]));
			}
		
		return result;
	}
	
	public void getParentFieldsList_ForEditDropDown() {
		JSONArray result = new JSONArray();
		farmerService.listParentFields().stream().forEach(obj -> {
			result.add(getJSONObject(obj[0],obj[1]));
		});

		sendAjaxResponse(result);
	}
	
	public void populateCatalogueValuesOfSelectedParentField() {
		JSONArray values = new JSONArray();
		DynamicFieldConfig dfc = farmerService.findDynamicFieldConfigById(Long.valueOf(getParentFieldId()));
		List<FarmCatalogue> fc = farmerService.listCatelogueType(dfc.getCatalogueType());
		farmerService.listCatelogueType(dfc.getCatalogueType()).stream().forEach(obj -> {
			values.add(getJSONObject(obj.getCode(), obj.getName()));
		});
		sendAjaxResponse(values);
	}

	public String getParentFieldId() {
		return parentFieldId;
	}

	public void setParentFieldId(String parentFieldId) {
		this.parentFieldId = parentFieldId;
	}

	public String getParentDependencyKey() {
		return parentDependencyKey;
	}

	public void setParentDependencyKey(String parentDependencyKey) {
		this.parentDependencyKey = parentDependencyKey;
	}

	public String getParentDependentId() {
		return parentDependentId;
	}

	public void setParentDependentId(String parentDependentId) {
		this.parentDependentId = parentDependentId;
	}

	public String getSelectedFieldNames() {
		return selectedFieldNames;
	}

	public void setSelectedFieldNames(String selectedFieldNames) {
		this.selectedFieldNames = selectedFieldNames;
	}

	
}
