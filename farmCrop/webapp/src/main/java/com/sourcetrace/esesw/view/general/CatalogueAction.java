package com.sourcetrace.esesw.view.general;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.ese.entity.util.Language;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.FarmCatalogueMaster;
import com.sourcetrace.eses.inspect.agrocert.LanguagePreferences;
import com.sourcetrace.eses.inspect.agrocert.SurveyQuestion;
import com.sourcetrace.eses.inspect.agrocert.SurveySection;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.ICertificationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

@SuppressWarnings("serial")
public class CatalogueAction extends SwitchValidatorAction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(CatalogueAction.class);
	@Autowired
	private ICatalogueService catalogueService;
	@Autowired
	private IUniqueIDGenerator idGenerator;
	@Autowired
	private IPreferencesService preferncesService;

	private Map<Integer, String> typezList = new LinkedHashMap<Integer, String>();
		static final int type = 13;
	private Map<String, String> catTypez = new LinkedHashMap<String, String>();
	private FarmCatalogue farmCatalogue;
	@Autowired
	private ICertificationService certificationService;

	private String id;
	private String catalogueName;
	private Map<String, String> statusMap = new LinkedHashMap<>();
	private String status;
	private String typez;
	   private String dispName;
	   private String survey;
	// transient Variable
	private String cataLogueMasterName;
	private String statusDeafaultVal;

	public void setCatalogueService(ICatalogueService catalogueService) {

		this.catalogueService = catalogueService;
	}

	public void setIdGenerator(IUniqueIDGenerator idGenerator) {

		this.idGenerator = idGenerator;
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
	 * @see com.sourcetrace.esesw.view.SwitchAction#data()
	 */
	public String data() throws Exception {

		Map<String, String> searchRecord = getJQGridRequestParam(); // get the
																	// search
																	// parameter
																	// with
		// value

		FarmCatalogue filter = new FarmCatalogue();

		ESESystem preferences = preferncesService.findPrefernceById("1");
		String isFpoEnabled = "";
		if (!StringUtil.isEmpty(preferences)) {
			isFpoEnabled = (preferences.getPreferences().get(ESESystem.ENABLE_FPOFG));
			if (!(isFpoEnabled.equals("1"))) {
				// filter.setTypeValue(Integer.parseInt(getText("FPO/FG")));
			}
		}
		if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
			if (!getIsMultiBranch().equalsIgnoreCase("1")) {
				List<String> branchList = new ArrayList<>();
				branchList.add(searchRecord.get("branchId").trim());
				filter.setBranchesList(branchList);
			} else {
				List<String> branchList = new ArrayList<>();
				List<BranchMaster> branches = clientService.listChildBranchIds(searchRecord.get("branchId").trim());
				branchList.add(searchRecord.get("branchId").trim());
				branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
					branchList.add(branch.getBranchId());
				});
				filter.setBranchesList(branchList);
			}
		}

		if (!StringUtil.isEmpty(searchRecord.get("subBranchId"))) {
			filter.setBranchId(searchRecord.get("subBranchId").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("code"))) {
			filter.setCode(searchRecord.get("code").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("name"))) {
			filter.setName(searchRecord.get("name").trim());
		}
		
		if (!StringUtil.isEmpty(searchRecord.get("typez"))) {
			filter.setTypez((Integer.valueOf(searchRecord.get("typez"))));
		} else {
			filter.setTypez(0);
		}
		
		if (!StringUtil.isEmpty(searchRecord.get("status"))) {
			filter.setStatus((searchRecord.get("status")));
		}
		
		if (!StringUtil.isEmpty(searchRecord.get("dispName"))) {
			filter.setDispName((searchRecord.get("dispName")));
		}
		
				filter.setMasterStatus(0);
		
		/*
		 * if (!StringUtil.isEmpty(searchRecord.get("typez"))) {
		 * filter.setName(searchRecord.get("typez").trim()); }
		 */

		filter.setIsReserved(0);

		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		return sendJQGridJSONResponse(data);

	}

	/**
	 * @see com.sourcetrace.esesw.view.SwitchAction#toJSON(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		// System.out.println(typezList);
		FarmCatalogue catalogue = (FarmCatalogue) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
/*		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(catalogue.getBranchId())))
						? getBranchesMap().get(getParentBranchMap().get(catalogue.getBranchId()))
						: getBranchesMap().get(catalogue.getBranchId()));
			}
			rows.add(getBranchesMap().get(catalogue.getBranchId()));

		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(branchesMap.get(catalogue.getBranchId()));
			}
		}*/
		rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">" + catalogue.getCode() + "</font>");
		FarmCatalogueMaster catalogueMaster = catalogueService
				.findFarmCatalogueMasterByCatalogueTypez(catalogue.getTypez());
		if (!ObjectUtil.isEmpty(catalogueMaster)) {
			rows.add(catalogueMaster.getName());
		} else {
			rows.add("");
		}
		//rows.add(catalogue.getName());
		
		String name = getLanguagePref(getLoggedInUserLanguage(), catalogue.getCode().trim().toString());
		if(!StringUtil.isEmpty(name) && name != null){
			rows.add(name);
		}else{
			rows.add(catalogue.getName());
		}
		
		if(!StringUtil.isEmpty(catalogue.getStatus())){
			rows.add(catalogue.getStatus().equals("0")?getLocaleProperty("status0"):getLocaleProperty("status1"));
		}else{
			rows.add(getLocaleProperty("status1"));
		}
		jsonObject.put("id", catalogue.getId());
		jsonObject.put("cell", rows);
		return jsonObject;
	}

	/**
	 * Creates the.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String create() throws Exception {
		if (farmCatalogue == null) {
			setStatusDeafaultVal("1");
			List<Language> languageMasters = certificationService.listLanguages();
			// List<Language> languageMasters =
			// certificationService.listLanguages();
			List<LanguagePreferences> languagePreferences = new ArrayList<LanguagePreferences>();
			for (Language languageMaster : languageMasters) {
				LanguagePreferences preferences = new LanguagePreferences();
				preferences.setCode("");
				preferences.setName("");
				preferences.setLang(languageMaster.getCode());
				preferences.setType(LanguagePreferences.Type.CATALOGUE.ordinal());
				languagePreferences.add(preferences);
			}

		
			farmCatalogue = new FarmCatalogue();
			farmCatalogue.setLanguagePreferences(languagePreferences);
			command = "create";
			request.setAttribute(HEADING, getText("cataloguecreate"));
			return INPUT;
		} else if ((farmCatalogue.getTypez() > 0) && (!StringUtil.isEmpty(farmCatalogue.getName()))) {
			// farmCatalogue.setTypez(type);
			farmCatalogue.setCode(idGenerator.getCatalogueIdSeq());
			farmCatalogue.setRevisionNo(DateUtil.getRevisionNumber());
			farmCatalogue.setBranchId(getBranchId());
		/*	if(farmCatalogue.getDispName()==null && StringUtil.isEmpty(farmCatalogue.getDispName())){
				farmCatalogue.setDispName(farmCatalogue.getName());
			}*/
			catalogueService.addCatalogue(farmCatalogue);

		}
		return "redirectSurvey";
	}
	
	public void populateCreate() throws Exception {
		if (farmCatalogue == null) {
			setStatusDeafaultVal("1");
			request.setAttribute(HEADING, getText("cataloguecreate"));
		} else if ((farmCatalogue.getTypez() > 0) && (!StringUtil.isEmpty(farmCatalogue.getName()))) {
			farmCatalogue.setCode(idGenerator.getCatalogueIdSeq());
			farmCatalogue.setRevisionNo(DateUtil.getRevisionNumber());
			farmCatalogue.setBranchId(getBranchId());
			if(StringUtil.isEmpty(farmCatalogue.getDispName()) || farmCatalogue.getDispName()==null){
				farmCatalogue.setDispName(farmCatalogue.getName());
			}
			
			catalogueService.addCatalogue(farmCatalogue);
			
			getJsonObject().put("msg", getText("msg.added"));
			getJsonObject().put("title", getText("title.success"));
			sendAjaxResponse(getJsonObject());
		}
	}
	
	public void populateUpdate(){
		if (!StringUtil.isEmpty(getId())) {
			FarmCatalogue catalogue = catalogueService.findCatalogueById(Long.valueOf(getId()));
			if(!ObjectUtil.isEmpty(catalogue)){
				catalogue.setName(getName());
				catalogue.setRevisionNo(DateUtil.getRevisionNumber());
				catalogue.setTypez(Integer.parseInt(getTypez()));
				if(StringUtil.isEmpty(getDispName()) || getDispName()==null){
					catalogue.setDispName(catalogue.getName());
				}else{
					catalogue.setDispName(getDispName());
				}
				if(getLocaleProperty("status1").equalsIgnoreCase(getStatus())){
					catalogue.setStatus(FarmCatalogue.ACTIVE);
				}else{
					catalogue.setStatus(FarmCatalogue.INACTIVE);
				}
				catalogueService.editCatalogue(catalogue);
				
				getJsonObject().put("msg", getText("msg.updated"));
				getJsonObject().put("title", getText("title.success"));
				sendAjaxResponse(getJsonObject());
			}
		}
	}
	
	
	public void populateDelete(){
		
	}
	

	/**
	 * Update.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String update() throws Exception {

		if (id != null && !id.equals("")) {
			farmCatalogue = catalogueService.findCatalogueById(Long.valueOf(id));
			List<Language> languageMasters = certificationService.findIsSurveyStatusLanguage();
			List<LanguagePreferences> languagePreferences = certificationService.listLangPrefByCode(farmCatalogue.getCode());
			for(Language l : languageMasters){
			
				List<LanguagePreferences> lpList = languagePreferences.stream().filter(obj -> l.getCode().equals(obj.getLang())).collect(Collectors.toList());
				if(lpList==null || lpList.isEmpty()){
					LanguagePreferences preferences = new LanguagePreferences();
					preferences.setCode("");
					preferences.setName("");
					preferences.setLang(l.getCode());
					preferences.setType(LanguagePreferences.Type.CATALOGUE.ordinal());
					languagePreferences.add(preferences);
					
				}
			}
			farmCatalogue.setLanguagePreferences(languagePreferences);
			if (farmCatalogue == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			setCurrentPage(getCurrentPage());
			id = null;
			command = UPDATE;
			request.setAttribute(HEADING, getText("catalogueupdate"));
			setStatusDeafaultVal(farmCatalogue.getStatus());
		} else {
			if (farmCatalogue != null) {
				FarmCatalogue temp = catalogueService.findCatalogueById(farmCatalogue.getId());
				if (temp == null) {
					addActionError(NO_RECORD);
					return REDIRECT;
				}

				setCurrentPage(getCurrentPage());
				temp.setName(farmCatalogue.getName());
				temp.setTypez(farmCatalogue.getTypez());
				temp.setRevisionNo(DateUtil.getRevisionNumber());
				temp.setStatus(farmCatalogue.getStatus());
				
				if(farmCatalogue.getDispName()==null && StringUtil.isEmpty(farmCatalogue.getDispName())){
					temp.setDispName(farmCatalogue.getName());
				}else{
					temp.setDispName(farmCatalogue.getDispName());
				}
				temp.setLanguagePreferences(farmCatalogue.getLanguagePreferences());
				catalogueService.editCatalogue(temp);
			}
			request.setAttribute(HEADING, getText("cataloguelist"));
			return "redirectSurvey";
		}
		return super.execute();
	}

	/**
	 * Detail.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String detail() throws Exception {

		String view = LIST;
		request.setAttribute(HEADING, getText(LIST));
		if (id != null && !id.equals("")) {
			farmCatalogue = catalogueService.findCatalogueById(Long.valueOf(id));
			FarmCatalogueMaster catalogueMaster = catalogueService
					.findFarmCatalogueMasterByCatalogueTypez(farmCatalogue.getTypez());
			if (!ObjectUtil.isEmpty(catalogueMaster)) {
				setCataLogueMasterName(catalogueMaster.getName());
				setStatus(farmCatalogue.getStatus().equals("0")?getLocaleProperty("status0"):getLocaleProperty("status1"));
			}
			
			List<Language> languageMasters = certificationService.findIsSurveyStatusLanguage();
			List<LanguagePreferences> languagePreferences = certificationService.listLangPrefByCode(farmCatalogue.getCode());
			for(Language l : languageMasters){
			
				List<LanguagePreferences> lpList = languagePreferences.stream().filter(obj -> l.getCode().equals(obj.getLang())).collect(Collectors.toList());
				if(lpList==null || lpList.isEmpty()){
					LanguagePreferences preferences = new LanguagePreferences();
					preferences.setCode("");
					preferences.setName("");
					preferences.setLang(l.getCode());
					preferences.setType(LanguagePreferences.Type.CATALOGUE.ordinal());
					languagePreferences.add(preferences);
					
				}
			}
			farmCatalogue.setLanguagePreferences(languagePreferences);
			farmCatalogue.setLanguagePreferences(languagePreferences);
		
			if (farmCatalogue == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}

			command = UPDATE;
			view = DETAIL;
			request.setAttribute(HEADING, getText("cataloguedetail"));
		}
		return view;
	}

	/**
	 * Delete.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String delete() throws Exception {

		if (this.getId() != null && !(this.getId().equals(EMPTY))) {
			farmCatalogue = catalogueService.findCatalogueById(Long.valueOf(id));
			if (!ObjectUtil.isEmpty(farmCatalogue)) {
				FarmCatalogueMaster catalogueMaster = catalogueService
						.findFarmCatalogueMasterById(Long.valueOf(farmCatalogue.getTypez()));
				if (!ObjectUtil.isEmpty(catalogueMaster)) {
					setCataLogueMasterName(catalogueMaster.getName());
				}
			}
			boolean isCatMappedWithFarmInventory = catalogueService.isCatMappedWithFarmInventory(farmCatalogue.getId());
			boolean isCatMappedWithAnimalHusbandary = catalogueService
					.isCatMappedWithAnimalHusbandary(farmCatalogue.getId());
			if (farmCatalogue == null) {
				addActionError(NO_RECORD);
				return list();
			} else if (isCatMappedWithFarmInventory) {
				addActionError(getText("warn.ass"));

				request.setAttribute(HEADING, getText(DETAIL));
				return DETAIL;
			} else if (isCatMappedWithAnimalHusbandary) {
				addActionError(getText("warn.ass.hus"));

				request.setAttribute(HEADING, getText(DETAIL));
				return DETAIL;
			} else {
				setCurrentPage(getCurrentPage());

				catalogueService.removeCatalogue(farmCatalogue.getName());
			}

		}

		request.setAttribute(HEADING, getText(LIST));
		return LIST;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.view.ESEValidatorAction#getData()
	 */
	/**
	 * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getData() {
		typezList = formCatalogueMap("cat", typezList);
		//typezListSurvey = formCatalogueMap("catSurvey", typezListSurvey);
		return farmCatalogue;

	}

	public void setTypezList(Map<Integer, String> typezList) {

		this.typezList = typezList;
	}

	public Map<Integer, String> getTypezList() {

		return typezList;
	}

	private Map<Integer, String> formCatalogueMap(String keyProperty, Map<Integer, String> typezList) {

		typezList = new LinkedHashMap();
		
			List<FarmCatalogueMaster> farmCatalougeList = catalogueService.listFarmCatalogueMatsters();
			/*typezList = farmCatalougeList.stream()
					.collect(Collectors.toMap(FarmCatalogueMaster::getTypez, FarmCatalogueMaster::getName));*/
			for(FarmCatalogueMaster fc: farmCatalougeList){
			typezList.put(fc.getTypez(), fc.getName());}
		return typezList;
	}

	public void populateCatalogueType() {

		if (!catalogueName.equalsIgnoreCase("null") && (!StringUtil.isEmpty(catalogueName))) {

			FarmCatalogueMaster existingCatalogueMaster = catalogueService
					.findFarmCatalogueMasterByName(catalogueName.trim());
			if (existingCatalogueMaster == null) {
				FarmCatalogueMaster farmCatalogue = new FarmCatalogueMaster();
				farmCatalogue.setRevisionNo(DateUtil.getRevisionNumber());
				farmCatalogue.setBranchId(getBranchId());
				farmCatalogue.setName(catalogueName);
				farmCatalogue.setTypez(Integer.valueOf(idGenerator.getCatalougeTypeSeq()));
				catalogueService.addFarmCatalogueMaster(farmCatalogue);
				JSONArray stateArr = new JSONArray();
				List<FarmCatalogueMaster> catalogueMasterLists = catalogueService.listFarmCatalogueMatsters();

				if (!ObjectUtil.isEmpty(catalogueMasterLists)) {

					for (FarmCatalogueMaster obj : catalogueMasterLists) {
						stateArr.add(getJSONObject(obj.getId(), obj.getName()));
					}
				}
				sendAjaxResponse(stateArr);
			} else {
				String result = "0";
				sendAjaxResponse(result);
			}
		}

	}

	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}

	public String getTypezFilterText() {

		StringBuffer sb = new StringBuffer();
		/*
		 * String values = getText("cat"); String[] valuesArray =
		 * values.split(",");
		 * 
		 * ESESystem preferences = preferncesService.findPrefernceById("1");
		 * String isFpoEnabled=""; if(!StringUtil.isEmpty(preferences)){
		 * isFpoEnabled=(preferences.getPreferences().get(ESESystem.ENABLE_FPOFG
		 * )); } if(!isFpoEnabled.equals("1")){ valuesArray= (String[])
		 * ArrayUtils.remove(valuesArray, 10); }
		 * 
		 * sb.append("-1:").append(FILTER_ALL).append(";");
		 * 
		 * for (int count = 0; count < valuesArray.length; count++) {
		 * 
		 * sb.append(count).append(":").append(valuesArray[count]).append(";");
		 * }
		 */
		List<FarmCatalogueMaster> farmCatalougeList =  new ArrayList<>();
	
				 farmCatalougeList = catalogueService.listFarmCatalogueMatsters();
		
		sb.append("-1:").append(FILTER_ALL).append(";");

		for (FarmCatalogueMaster farmCatalougeMaster : farmCatalougeList) {
			sb.append(farmCatalougeMaster.getTypez()).append(":").append(farmCatalougeMaster.getName()).append(";");
		}

		String data = sb.toString();
		return data.substring(0, data.length() - 1);
	}
	
	public void populateCatalougeMaster(){
		JSONObject jsonObject = new JSONObject();
		catalogueService.listFarmCatalogueMatsters().stream().forEach(catalogue->{
			jsonObject.put(catalogue.getId(), catalogue.getName());
		});
		
		try {
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			out.print(jsonObject.toString().replace("\"", "").replace(",", ";"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public String getStatusFilter(){
		String value = getLocaleProperty("status1")+":"+getLocaleProperty("status0");
		return value;
	}
	
	public FarmCatalogue getFarmCatalogue() {
		return farmCatalogue;
	}

	public void setFarmCatalogue(FarmCatalogue farmCatalogue) {
		this.farmCatalogue = farmCatalogue;
	}

	public ICatalogueService getCatalogueService() {
		return catalogueService;
	}

	public Map<Long, String> getCatTypez() {
		Map<Long, String> catalogeList = new HashMap<Long, String>();
		List<FarmCatalogueMaster> farmCatalougeList = catalogueService.listFarmCatalogueMatsters();
		catalogeList = farmCatalougeList.stream()
				.collect(Collectors.toMap(FarmCatalogueMaster::getId, FarmCatalogueMaster::getName));
		return catalogeList;
	}

	public void setCatTypez(Map<String, String> catTypez) {
		this.catTypez = catTypez;
	}

	public String getCatalogueName() {
		return catalogueName;
	}

	public void setCatalogueName(String catalogueName) {
		this.catalogueName = catalogueName;
	}

	public String getCataLogueMasterName() {
		return cataLogueMasterName;
	}

	public void setCataLogueMasterName(String cataLogueMasterName) {
		this.cataLogueMasterName = cataLogueMasterName;
	}

	public Map<String, String> getStatusMap() {
		statusMap.put("1", getLocaleProperty("status1"));
		statusMap.put("0", getLocaleProperty("status0"));
		return statusMap;
	}

	public void setStatusMap(Map<String, String> statusMap) {
		this.statusMap = statusMap;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusDeafaultVal() {
		return statusDeafaultVal;
	}

	public void setStatusDeafaultVal(String statusDeafaultVal) {
		this.statusDeafaultVal = statusDeafaultVal;
	}

	public String getTypez() {
		return typez;
	}

	public void setTypez(String typez) {
		this.typez = typez;
	}
	
	public String getDispName() {
		return dispName;
	}

	public void setDispName(String dispName) {
		this.dispName = dispName;
	}

	


	public String getSurvey() {
		return survey;
	}

	public void setSurvey(String surveyEdit) {
		this.survey = surveyEdit;
	}
	
	
	
}
