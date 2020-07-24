package com.sourcetrace.esesw.view.general;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.sourcetrace.eses.BreadCrumb;
import com.sourcetrace.eses.entity.DynamicFeildMenuConfig;
import com.sourcetrace.eses.entity.DynamicFieldConfig;
import com.sourcetrace.eses.entity.DynamicMenuFieldMap;
import com.sourcetrace.eses.entity.DynamicMenuSectionMap;
import com.sourcetrace.eses.entity.DynamicSectionConfig;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.FarmCatalogueMaster;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.dynamicUiRelated.DynamicuiStringToJson;
import com.sourcetrace.esesw.view.SwitchAction;


public class DynamicUiAction extends SwitchAction{

	@Autowired
	private IUniqueIDGenerator idGenerator;
	
	@Autowired
	private IFarmerService farmerService;
	
	@Autowired
	private IUniqueIDGenerator uniqueIDGenerator;
	private AtomicInteger order = new AtomicInteger(1);
	private String menudata;
	private String catalogueType;
	private String CatalogueMasterName;
	String sectionCodeList_ForMapping = "";
	String fieldsCodeList_ForMapping = "";
	

	public String list() {
		return LIST;
	}
	
	public void createMenu() {
		System.out.println(" -------------- Dynamic UI Menu Data Start  -------------------------");
		System.out.println(getMenudata());
		System.out.println(" -------------- Dynamic UI Menu Data End ------------------------------");
		DynamicuiStringToJson data = new Gson().fromJson(getMenudata(), DynamicuiStringToJson.class);
		Boolean result = saveSection(data);
		
		if(result){
			String msg = !getLocaleProperty("dynamic.menu.saved.successfully").equalsIgnoreCase("dynamic.menu.saved.successfully") ? getLocaleProperty("dynamic.menu.saved.successfully") : "Dynamic Menu Saved Successfully";
			getJsonObject().put("msg", msg);
			getJsonObject().put("title", getText("title.success"));
			sendAjaxResponse(getJsonObject());
		}else{
			String msg = !getLocaleProperty("dynamic.menu.saved.failed").equalsIgnoreCase("dynamic.menu.saved.failed") ? getLocaleProperty("dynamic.menu.saved.failed") : "Dynamic Menu saving process failed";
			getJsonObject().put("msg", msg);
			getJsonObject().put("title", getText("title.error"));
			sendAjaxResponse(getJsonObject());
		}
	}
	
	public Boolean saveSection(DynamicuiStringToJson data){
		Boolean result = false;
		List<DynamicSectionConfig> dscList = new ArrayList<>();
		Map<String,DynamicSectionConfig> sectionCode =  new HashMap< String,DynamicSectionConfig>(); 
		data.getSectionArray().stream().forEach(sectionArrayContains -> {
			/*System.out.println("-----");
			System.out.println(sectionArrayContains.getSectionData().getId());
			System.out.println(sectionArrayContains.getSectionData().getName());*/
			
			DynamicSectionConfig dsc = new DynamicSectionConfig();
			dsc.setBranchId(getBranchId());
			dsc.setSectionName(sectionArrayContains.getSectionData().getName());
			
			
			long count;
			do {
				 dsc.setSectionCode(idGenerator.getDynamicSectionCodeSeq());
				 count = farmerService.findDynamicSectionCountByCode(dsc.getSectionCode());
			} while (count != 0);
			sectionCodeList_ForMapping = sectionCodeList_ForMapping+dsc.getSectionCode()+",";
			dscList.add(dsc);
			sectionCode.put(sectionArrayContains.getSectionData().getId(), dsc);
			
		});
		
	if(!ObjectUtil.isListEmpty(dscList)){
		Boolean sectionsSaved = false;
		try {
			farmerService.saveSectionBatch(dscList);
			sectionsSaved = true;
			
		} catch (Exception e) {
			System.out.println("<----------------------------------------------------------------------->");
			System.out.println("Sections not saved Properly");
			System.out.println("Error occured while executing farmerService.saveSectionBatch(dscList)");
			System.out.println("<----------------------------------------------------------------------->");
			result = false;
		}
		
		if(sectionsSaved){
			try {
				
				result = saveFields(data,sectionCode);
			} catch (Exception e) {
				System.out.println("<----------------------------------------------------------------------->");
				System.out.println("Dynamic Sections saved SuccessFully");
				System.out.println("Dynamic Fields not saved Properly");
				System.out.println("Error occured while executing -> saveFields(data,sectionCode);");
				System.out.println("<----------------------------------------------------------------------->");
				result = false;
			}
		}
		
		
		
	}
		
		
		return result;
		
	}
	
	public Boolean saveFields(DynamicuiStringToJson data, Map<String,DynamicSectionConfig> dscMap){
		Boolean result = false;
		List<DynamicFieldConfig> dfcList = new ArrayList<>();
		Map<String,DynamicFieldConfig> referenceIdForList =  new HashMap< String,DynamicFieldConfig>(); 
		
		data.getSectionArray().stream().forEach(sectionArrayContains -> {
			sectionArrayContains.getFieldsArray().stream().forEach(field -> {
				
				DynamicFieldConfig dfc = new DynamicFieldConfig();
				dfc.setDynamicSectionConfig(dscMap.get(field.getSectionId()));
				
				long count;
				do {
					 dfc.setCode(idGenerator.getDynamicFieldCodeSeq());
					 count = farmerService.findDynamicFieldsCountByCode(dfc.getCode());
				} while (count != 0);
				
				fieldsCodeList_ForMapping = fieldsCodeList_ForMapping+dfc.getCode()+",";
				
				dfc.setComponentName(field.getComponentName());
				dfc.setComponentType(field.getComponentType());
				dfc.setComponentMaxLength(field.getMaxLength());
				dfc.setDefaultValue(field.getDefaultValue());
				dfc.setValidation(field.getValidationType());
				dfc.setCatalogueType(field.getCatalogueType());
				dfc.setDataFormat(field.getDataFormat());
				dfc.setIsMobileAvail(field.getIsMobileAvail());
				
				if(dfc.getComponentType().equalsIgnoreCase("4") || dfc.getComponentType().equalsIgnoreCase("9") || dfc.getComponentType().equalsIgnoreCase("6") || dfc.getComponentType().equalsIgnoreCase("2")){
					dfc.setAccessType(1);
					//4 - Drop down
					//9 - multi drop down
					//6 - Check box
					//2 - Radio
					//(load catalogue values)
				}else{
					dfc.setAccessType(0);
				}
				
				if(!StringUtil.isEmpty(field.getReferenceId().trim())){
					DynamicFieldConfig list = referenceIdForList.get(field.getReferenceId().trim());
					dfc.setReferenceId(list.getId());
				}
				
				
				if(field.getIsRequired().equalsIgnoreCase("required")){
					dfc.setIsRequired("1");
				}else{
					dfc.setIsRequired("0");
				}
				
				if(field.getComponentType().equalsIgnoreCase("8")){
					farmerService.save(dfc);	//list components saved first bcoz of reference id
					referenceIdForList.put(field.getComponentName(), dfc);
				}else{
					dfcList.add(dfc);
				}
				
				
			});
		});
		
		if(!ObjectUtil.isListEmpty(dfcList)){
			Boolean fieldsSaved = false;
			try {
				farmerService.saveFieldsBatch(dfcList);
				fieldsSaved = true;
				result = true;
			} catch (Exception e) {
				System.out.println("<----------------------------------------------------------------------->");
				System.out.println("Fields not saved Properly");
				System.out.println("Error occured while executing saveFields(data,sectionCode);");
				System.out.println("<----------------------------------------------------------------------->");
				result = false;
			}
			
			if(fieldsSaved){
				try {
					result = saveMenu(data);
					if(!result){
						System.out.println("<----------------------------------------------------------------------->");
						System.out.println("Dynamic sections Saved SuccessFully");
						System.out.println("Dynamic fields Saved SuccessFully");
						System.out.println("Dynamic Menu not saved Properly");
						System.out.println("Error occured while executing -> saveMenu(data); ->  while executing save menu");
						System.out.println("<----------------------------------------------------------------------->");
					}
				} catch (Exception e) {
					System.out.println("<----------------------------------------------------------------------->");
					System.out.println("Dynamic sections Saved SuccessFully");
					System.out.println("Dynamic fields Saved SuccessFully");
					System.out.println("Dynamic Menu not saved Properly");
					System.out.println("Error occured while executing -> saveMenu(data);");
					System.out.println("<----------------------------------------------------------------------->");
					result = false;
				}
			}
			
			
			
			
		}
	
		return result;
		
	}
	
	public Boolean saveMenu(DynamicuiStringToJson data){
		Boolean result = false;
		String selectedFieldName = "";
		String selectedSection = "";
		
		if(!StringUtil.isEmpty(fieldsCodeList_ForMapping)){
			if(fieldsCodeList_ForMapping.contains(",")){
				selectedFieldName = StringUtil.removeLastComma(fieldsCodeList_ForMapping);
			}else{
				selectedFieldName = fieldsCodeList_ForMapping;
			}
			
		}
		if(!StringUtil.isEmpty(sectionCodeList_ForMapping)){
			if(sectionCodeList_ForMapping.contains(",")){
				selectedSection = StringUtil.removeLastComma(sectionCodeList_ForMapping);
			}else{
				selectedSection = sectionCodeList_ForMapping;
			}
			
		}
		 
		DynamicFeildMenuConfig dynamicFeildMenuConfig = new DynamicFeildMenuConfig();
		
		long count;
		do {
			dynamicFeildMenuConfig.setCode(idGenerator.getDynamicMenuCodeSeq());
			 count = farmerService.findDynamicMenuCountByCode(dynamicFeildMenuConfig.getCode());
		} while (count != 0);
		
		dynamicFeildMenuConfig.setName(data.getMenuName());
		dynamicFeildMenuConfig.setIconClass(data.getIconClass());
		dynamicFeildMenuConfig.setEntity(data.getEntity());
		dynamicFeildMenuConfig.setAgentType(data.getAgentType());
		dynamicFeildMenuConfig.setTxnType(data.getTxnType());
		dynamicFeildMenuConfig.setmTxnType(data.getmTxnType());
		dynamicFeildMenuConfig.setIsScore(Integer.valueOf(!StringUtil.isEmpty(data.getIsScore()) ? data.getIsScore() : "0"));
		dynamicFeildMenuConfig.setOrder(farmerService.findDynamicMenuMaxOrderNo()+1);
		dynamicFeildMenuConfig.setRevisionNo(DateUtil.getRevisionNumber());
		List<String> selectedFieldLis = Arrays.asList(selectedFieldName.split(",")).stream().map(x -> x.trim()).collect(Collectors.toList());
		List<DynamicFieldConfig> allFields = farmerService.listDynamicFieldsBySectionCode(selectedSection);
		List<DynamicFieldConfig> fieldList = allFields.stream().filter(u -> selectedFieldLis.contains(u.getCode())).collect(Collectors.toList());
		/*Map<Long, List<DynamicFieldConfig>> parentrList = allFields.stream().filter(u -> u.getReferenceId() != null)
				.collect(Collectors.groupingBy(DynamicFieldConfig::getReferenceId));*/

		List<DynamicSectionConfig> sectionList = farmerService.listDynamicSectionsBySectionId(Arrays.asList(selectedSection.split(",")).stream().map(x -> x.trim()).collect(Collectors.toList()));

		SortedSet<DynamicMenuSectionMap> dsc = new TreeSet<DynamicMenuSectionMap>();
		SortedSet<DynamicMenuFieldMap> dfc = new TreeSet<DynamicMenuFieldMap>();
		order = new AtomicInteger(0);
		fieldList.stream().forEach(field -> {
			DynamicMenuFieldMap fmap = new DynamicMenuFieldMap();
			fmap.setField(field);
			fmap.setMenu(dynamicFeildMenuConfig);
			fmap.setOrder(order.incrementAndGet());
			dfc.add(fmap);
		/*	if (parentrList.containsKey(field.getId())) {
				List<DynamicFieldConfig> chList = parentrList.get(field.getId());
				chList.stream().forEach(f -> {
					DynamicMenuFieldMap ffmap = new DynamicMenuFieldMap();
					ffmap.setField(f);
					ffmap.setMenu(dynamicFeildMenuConfig);
					ffmap.setOrder(order.incrementAndGet());
					dfc.add(ffmap);
				});

			}*/
		});
		order = new AtomicInteger(0);
		sectionList.stream().forEach(section -> {
			DynamicMenuSectionMap smap = new DynamicMenuSectionMap();
			smap.setSection(section);
			smap.setMenu(dynamicFeildMenuConfig);
			smap.setOrder(order.incrementAndGet());
			dsc.add(smap);
		});

		dynamicFeildMenuConfig.setDynamicFieldConfigs(dfc);
		dynamicFeildMenuConfig.setDynamicSectionConfigs(dsc);
		try{
			farmerService.addDynamicFeildMenuConfig(dynamicFeildMenuConfig);
			result = true;
		}catch (Exception e) {
			result = false;
		}
		

		/*if(!StringUtil.isEmpty(serviceM) && serviceM!=null){
		if (serviceM == 1 && !StringUtil.isEmpty(getRole()) && !getRole().equalsIgnoreCase("0")) {
			String url = "dynamicCertification_list.action?txnType=" + dynamicFeildMenuConfig.getTxnType();
			String ent = "service.dynamicCertification" ;
			String action_Id =  "list~1,create~2,update~3,delete~4";
			farmerService.save_subMenu("3", dynamicFeildMenuConfig.getName(), "Dynamic Certification", url, "100", ent, action_Id,getRole());
		} else {
			addActionError(getText("emptyRole"));
			return INPUT;
		}
		}*/
		
		/*if(!StringUtil.isEmpty(reportM) && reportM!=null){
		if (reportM == 1 && !StringUtil.isEmpty(getRole()) && !getRole().equalsIgnoreCase("0")) {
			String url = "dynmaicCertificationReport_list.action?txnType=" + dynamicFeildMenuConfig.getTxnType();
			String ent = "report.dynmaicCertification";
			farmerService.save_subMenu("4", dynamicFeildMenuConfig.getName() , "Dynamic Certification Report", url, "100", ent,	"1", getRole());
		} else {
			addActionError(getText("emptyRole"));
			return INPUT;
		}
		}*/

		return result;
	}
	
	public void getCatalogueTypez(){
		List<FarmCatalogueMaster> catalogueMasterLists = catalogueService.listFarmCatalogueMatsters();
		JSONObject catalogueTypez = new JSONObject();
		catalogueMasterLists.parallelStream().forEach(i -> {
			catalogueTypez.put(String.valueOf(i.getTypez()), i.getName());
		});
		sendAjaxResponse(catalogueTypez);
	}
	
	public void listCataloguesByType(){
		List<FarmCatalogue> fc = catalogueService.listCataloguesByType(getCatalogueType());
		JSONObject catalogueValues = new JSONObject();
		fc.parallelStream().forEach(i -> {
			catalogueValues.put(String.valueOf(i.getCode()), i.getName());
		});
		sendAjaxResponse(catalogueValues);
	}
	
	public void createCatalogueMaster(){
		
			FarmCatalogueMaster fcm = catalogueService.findFarmCatalogueMasterByName(getCatalogueMasterName());
			if(fcm != null && !ObjectUtil.isEmpty(fcm)){
				getJsonObject().put("msg", getText("Already Exists"));
				getJsonObject().put("title", getText("title.error"));
				sendAjaxResponse(getJsonObject());
			}else{
				FarmCatalogueMaster farmCatalogue = new FarmCatalogueMaster();
				farmCatalogue.setRevisionNo(DateUtil.getRevisionNumber());
				farmCatalogue.setBranchId(getBranchId());
				farmCatalogue.setName(getCatalogueMasterName());
				farmCatalogue.setStatus(1);
				do {
					farmCatalogue.setTypez(Integer.valueOf(idGenerator.getCatalougeTypeSeq()));
					fcm = catalogueService.findFarmCatalogueMasterByCatalogueTypez(farmCatalogue.getTypez());
				} while (fcm != null && !ObjectUtil.isEmpty(fcm));
				
				catalogueService.addFarmCatalogueMaster(farmCatalogue);
				
				getJsonObject().put("msg", getText("catalogue.master.added"));
				getJsonObject().put("title", getText("title.success"));
				sendAjaxResponse(getJsonObject());
			}
	}
	
	public String getMenudata() {
		return menudata;
	}

	public void setMenudata(String menudata) {
		this.menudata = menudata;
	}
	
	public IUniqueIDGenerator getIdGenerator() {
		return idGenerator;
	}

	public void setIdGenerator(IUniqueIDGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	public String getCatalogueType() {
		return catalogueType;
	}

	public void setCatalogueType(String catalogueType) {
		this.catalogueType = catalogueType;
	}

	public String getCatalogueMasterName() {
		return CatalogueMasterName;
	}

	public void setCatalogueMasterName(String catalogueMasterName) {
		CatalogueMasterName = catalogueMasterName;
	}
}
