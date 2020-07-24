/*
 * CooperativeAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view.general;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.opensymphony.xwork2.ActionContext;
import com.sourcetrace.eses.BreadCrumb;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.FarmCatalogueMaster;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.entity.WarehouseStorageMap;
import com.sourcetrace.eses.entity.ColdStorage;
import com.sourcetrace.eses.order.entity.txn.ProcurementDetail;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Calf;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

/**
 * The Class CooperativeAction.
 */
public class CooperativeAction extends SwitchValidatorAction implements SessionAware {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(CooperativeAction.class);
	public static final String SELECT_MULTI = "-1";
	public static final String OPTION_CONSTANT = "<option value=\"-1\">--Select--</option>";
	public static final String OPTION_KEY = "<!--optionKey--!>";
	public static final String OPTION_VALUE = "<rep!--optionValue--!>";
	public static final String PATTERN_OPTION = "<option value='<!--optionKey--!>'><!--optionValue--!></option>";

	private String id;
	private String selectedBlock;
	private Warehouse warehouse;

	private List<Municipality> blockes = new ArrayList<Municipality>();
	private List<String> availableVillages = new ArrayList<String>();
	private List<String> selectedVillages = new ArrayList<String>();
	private Map<String, Object> selectedVillageMap = new LinkedHashMap<String, Object>();
	private Map<String, Object> sessionScopMap = new HashMap<String, Object>();
	private Map<Integer, String> commodityList = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> ownershipList = new LinkedHashMap<Integer, String>();
	private List<Object[]> availableVillageObjs = new ArrayList<Object[]>();

	private ILocationService locationService;
	private IAgentService agentService;
	private ICatalogueService catalogueService;
	private IFarmerService farmerService;
	@Autowired
	private IPreferencesService preferncesService;
	private IUniqueIDGenerator idGenerator;
	private String commodityName;
	private String ownershipName;
	private String type;
	private String selectedCommodity;
	private String storageArrayToString;
	private String enableStorage;
	Map<Integer, String> warehouseType = new LinkedHashMap<Integer, String>();
	private String selectedColdStorageName;
	private Double maxBayWeight;
	private List<WarehouseStorageMap> warehouseStorageMapList;
	/**
	 * Gets the data.
	 * 
	 * @return the data
	 * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
	 */

	public String list() throws Exception {
		setType(request.getParameter("type"));
		if (getType() == null) {
			setType(getType());
		}
		if (getCurrentPage() != null) {
			setCurrentPage(getCurrentPage());
		}
		request.setAttribute(HEADING, getText(LIST));
		return LIST;
	}

	@Override
	public Object getData() {
		if(getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)){
		warehouseType.put(Warehouse.WarehouseTypes.COOPERATIVE.ordinal(), getLocaleProperty("WarehouseType" + Warehouse.WarehouseTypes.COOPERATIVE.ordinal()));
		}
		warehouseType.put(Warehouse.WarehouseTypes.PROCUREMENT_PLACE.ordinal(),  getLocaleProperty("WarehouseType" + Warehouse.WarehouseTypes.PROCUREMENT_PLACE.ordinal()));
		warehouseType.put(Warehouse.WarehouseTypes.GINNER.ordinal(),  getLocaleProperty("WarehouseType" +Warehouse.WarehouseTypes.GINNER.ordinal()));
		if(getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)){
		warehouseType.put(Warehouse.WarehouseTypes.SPINNER.ordinal(),  getLocaleProperty("WarehouseType" +Warehouse.WarehouseTypes.SPINNER.ordinal()));
		}
		return warehouse;
	}

	/**
	 * @see com.sourcetrace.esesw.view.SwitchAction#toJSON(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		Warehouse warehouse = (Warehouse) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
	  	if ((getIsMultiBranch().equalsIgnoreCase("1") && (getIsParentBranch().equals("1")||StringUtil.isEmpty(branchIdValue)))) {

	          if (StringUtil.isEmpty(branchIdValue)) {
	              rows.add(!StringUtil.isEmpty(
	                      getBranchesMap().get(getParentBranchMap().get(warehouse.getBranchId())))
	                              ? getBranchesMap()
	                                      .get(getParentBranchMap().get(warehouse.getBranchId()))
	                              : getBranchesMap().get(warehouse.getBranchId()));
	          }
	          rows.add(getBranchesMap().get(warehouse.getBranchId()));

	      } else {
	          if (StringUtil.isEmpty(branchIdValue)) {
	              rows.add(branchesMap.get(warehouse.getBranchId()));
	          }
	      }
		rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">" + warehouse.getCode() + "</font>");
		rows.add(warehouse.getName());
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID) || getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)) {
		rows.add(getText("WarehouseType"+warehouse.getTypez()));
		}
		rows.add(!StringUtil.isEmpty(warehouse.getLocation()) ? warehouse.getLocation() : "NA");
		if(!getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)){
		rows.add(!StringUtil.isEmpty(warehouse.getWarehouseInCharge()) ? warehouse.getWarehouseInCharge() : "NA");
		rows.add(!StringUtil.isEmpty(warehouse.getCapacityInTonnes()) ? warehouse.getCapacityInTonnes() : "NA");
		}
		jsonObject.put("id", warehouse.getId());
		jsonObject.put("cell", rows);

		return jsonObject;

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

		Warehouse filter = new Warehouse();

	    if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
            if (!getIsMultiBranch().equalsIgnoreCase("1")) {
                List<String> branchList = new ArrayList<>();
                branchList.add(searchRecord.get("branchId").trim());
                filter.setBranchesList(branchList);
            } else {
                List<String> branchList = new ArrayList<>();
                List<BranchMaster> branches = clientService
                        .listChildBranchIds(searchRecord.get("branchId").trim());
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
			if ("0".equals(searchRecord.get("typez"))) {
				filter.setFilterStatus("WarehouseType");
				filter.setTypez(Warehouse.WarehouseTypes.COOPERATIVE.ordinal());
			} else if ("2".equals(searchRecord.get("typez"))) {
				filter.setFilterStatus("WarehouseType");
				filter.setTypez(Warehouse.WarehouseTypes.PROCUREMENT_PLACE.ordinal());
			}
			else if ("3".equals(searchRecord.get("typez"))) {
				filter.setFilterStatus("WarehouseType");
				filter.setTypez(Warehouse.WarehouseTypes.GINNER.ordinal());
			}
			else if ("4".equals(searchRecord.get("typez"))) {
				filter.setFilterStatus("WarehouseType");
				filter.setTypez(Warehouse.WarehouseTypes.SPINNER.ordinal());
			}
		}
		
		
		/*if (!StringUtil.isEmpty(searchRecord.get("typez"))) {
			filter.setTypez(searchRecord.get("typez").trim());
		}*/
		if (!StringUtil.isEmpty(searchRecord.get("location"))) {
			filter.setLocation(searchRecord.get("location").trim());
		}
		if (!StringUtil.isEmpty(searchRecord.get("warehouseInCharge"))) {
			filter.setWarehouseInCharge(searchRecord.get("warehouseInCharge").trim());
		}
		if (!StringUtil.isEmpty(searchRecord.get("capacityInTonnes"))) {
			filter.setCapacityInTonnes(searchRecord.get("capacityInTonnes").trim());
		}
		String type=request.getParameter("type");
		if(type==null||type.equalsIgnoreCase("cooperative")){
			filter.setWarehouse_type(Warehouse.PRODUCT_DISTRIBUTION_CENTER);
		}else if(type.equalsIgnoreCase("collectionCenter")){
			filter.setWarehouse_type(Warehouse.FIELD_COLLECTION_CENTER);
		}
		
		filter.setRefCooperative(null);
		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		return sendJQGridJSONResponse(data);
	}

	/**
	 * Creates the.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings("unchecked")
	public String create() throws Exception {
		setType(getType());
		setEnableStorage(preferncesService.findPrefernceByName(ESESystem.ENABLE_STORAGE));
		
		if (warehouse == null) {
			command = "create";
			sessionScopMap.put("selVillageMap", null);
			request.setAttribute(HEADING, getText("cooperativecreate"));
			setEnableStorage(preferncesService.findPrefernceByName(ESESystem.ENABLE_BUYER));
			setEnableStorage(preferncesService.findPrefernceByName(ESESystem.ENABLE_STORAGE));
			return INPUT;
		} else {

			/*
			 * Map map = (Map) sessionScopMap.get("selVillageMap"); if
			 * (ObjectUtil.isEmpty(map) || map.size() <= 0) {
			 * addActionError(getText("empty.villageSelection")); return INPUT;
			 * } List<String> villageCodeList = new
			 * ArrayList<String>(map.keySet()); List<Village> villages =
			 * locationService.listOfVillagesByCodes(villageCodeList); if
			 * (!ObjectUtil.isEmpty(villages)) { warehouse.setVillages(new
			 * HashSet<Village>(villages)); }
			 */
			warehouse.setCode(idGenerator.getWarehouseIdSeq());
			warehouse.setBranchId(getBranchId());
			warehouse.setCreatedUsername(getUsername());
			warehouse.setUpdatedUsername(getUsername());
			warehouse.setCreatedDate(new Date());
			warehouse.setUpdatedDate(new Date());
			if (!getCurrentTenantId().equalsIgnoreCase("chetna") && !getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)) {
			if (ObjectUtil.isEmpty(warehouse.getRefCooperative())) {
				warehouse.setTypez(Warehouse.COOPERATIVE);
			}}else{
				warehouse.setTypez(warehouse.getTypez());
				warehouse.setLatitude(warehouse.getLatitude());
				warehouse.setLongitude(warehouse.getLatitude());
			}
		
			if(getType().equalsIgnoreCase("collectionCenter")){
				warehouse.setWarehouse_type(Warehouse.FIELD_COLLECTION_CENTER);
			}else if(getType().equalsIgnoreCase("cooperative")){
				warehouse.setWarehouse_type(Warehouse.PRODUCT_DISTRIBUTION_CENTER);
			}
			
			warehouse.setWarehouseOwnerShip(warehouse.getWarehouseOwnerShip());
			
			warehouse.setStorageCommodity((selectedCommodity != SELECT_MULTI) ? selectedCommodity : SELECT_MULTI);
		
			if(!StringUtil.isEmpty(getEnableStorage()) && getEnableStorage().equalsIgnoreCase("1")){
			
				Set<WarehouseStorageMap> warehouseStorageMapDetails = new LinkedHashSet<>();
				if (!StringUtil.isEmpty(getStorageArrayToString())&& getStorageArrayToString()!= null) {
					List<String> productsList = Arrays.asList(getStorageArrayToString().split("@"));
					productsList.stream().filter(obj -> !StringUtil.isEmpty(obj)).forEach(products -> {
						WarehouseStorageMap warehouseStorageMap = new WarehouseStorageMap();
						List<String> list = Arrays.asList(products.split("#"));
						warehouseStorageMap.setColdStorageName(String.valueOf(list.get(0)));
						warehouseStorageMap.setMaxBayHold(Double.valueOf(list.get(1)));
						warehouseStorageMap.setBranchId(getBranchId());
						warehouseStorageMapDetails.add(warehouseStorageMap);
					});
					warehouse.setWarehouseStorageMap(warehouseStorageMapDetails);
				}else{
					addActionError("ColdStorage is not Empty");
					return INPUT;
				}
				
			}
			
			
			locationService.addWarehouse(warehouse);
			sessionScopMap.put("selVillageMap", null);
			return list();
			// return REDIRECT;
		}
	}

	/**
	 * Update.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings("unchecked")
	public String update() throws Exception {
		setEnableStorage(preferncesService.findPrefernceByName(ESESystem.ENABLE_STORAGE));
		setType(getType());
		if (id != null && !id.equals("")) {
			if (getCurrentTenantId().equalsIgnoreCase("chetna") || getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)) {
			warehouse = locationService.findWarehouseWithoutTypeById(Long.valueOf(id));
			}else{
			warehouse = locationService.findWarehouseById(Long.valueOf(id));}
			if (warehouse == null) {
				addActionError(NO_RECORD);
				return list();
			}
			sessionScopMap.put("selVillageMap", null);
			selectedVillageMap = new HashMap<String, Object>();
			for (Village village : warehouse.getVillages()) {
				selectedVillageMap.put(village.getCode(),
						(Object) (village.getName() + " / " + village.getGramPanchayat().getCity().getName()));
			}
			sessionScopMap.put("selVillageMap", selectedVillageMap);
			selectedCommodity = warehouse.getStorageCommodity()!=null && !StringUtil.isEmpty(warehouse.getStorageCommodity())?warehouse.getStorageCommodity().trim():SELECT_MULTI;
			//selectedCommodity=selectedCommodity.trim();
			//selectedColdStorageName=warehouse.getWarehouseStorageMap().iterator().next().getColdStorageName();
			//maxBayWeight=warehouse.getWarehouseStorageMap().iterator().next().getMaxBayHold();
			if(!StringUtil.isEmpty(getEnableStorage()) && getEnableStorage().equalsIgnoreCase("1")){
			warehouseStorageMapList = new ArrayList<>();
			List<WarehouseStorageMap> calfListTemp = farmerService.listOfwarehouseStorageMap(warehouse.getId());
			calfListTemp.stream().filter(warehouseStorageMap -> !ObjectUtil.isEmpty(warehouseStorageMap)).forEach(warehouseStorageMap -> {
				//warehouseStorageMap.setColdStorageName(String.valueOf(getCatlogueValueByCode(warehouseStorageMap.getColdStorageName()).getName()));
				warehouseStorageMap.setColdStorageName(String.valueOf(warehouseStorageMap.getColdStorageName()));
				warehouseStorageMap.setMaxBayHold(warehouseStorageMap.getMaxBayHold());
				warehouseStorageMapList.add(warehouseStorageMap);
				
			});
			}
			setCurrentPage(getCurrentPage());
			
			id = null;
			command = UPDATE;
			request.setAttribute(HEADING, getText("cooperativeupdate"));
		} else {
			setType(getType());
			if (warehouse != null) {
				Warehouse temp;
				if (getCurrentTenantId().equalsIgnoreCase("chetna") || getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)) {
					temp = locationService.findWarehouseWithoutTypeById(warehouse.getId());
					temp.setTypez(warehouse.getTypez());
					temp.setLatitude(warehouse.getLatitude());
					temp.setLongitude(warehouse.getLongitude());
					}else{
						temp = locationService.findWarehouseById(warehouse.getId());}
				if (temp == null) {
					addActionError(NO_RECORD);
					return list();
					// return REDIRECT;
				}
				setCurrentPage(getCurrentPage());
				temp.setName(warehouse.getName());
				// temp.setCode(warehouse.getCode());
				temp.setLocation(warehouse.getLocation());
				temp.setAddress(warehouse.getAddress());
				temp.setPhoneNo(warehouse.getPhoneNo());
				temp.setUpdatedUsername(getUsername());
				temp.setUpdatedDate(warehouse.getUpdatedDate());
				temp.setWarehouseInCharge(warehouse.getWarehouseInCharge());
				temp.setCapacityInTonnes(warehouse.getCapacityInTonnes());
				temp.setStorageCommodity((selectedCommodity != SELECT_MULTI) ? selectedCommodity : SELECT_MULTI);
				temp.setCommodityOthers(warehouse.getCommodityOthers());
				temp.setWarehouseOwnerShip(warehouse.getWarehouseOwnerShip());

				Map map = (Map) sessionScopMap.get("selVillageMap");
				/*
				 * if (ObjectUtil.isEmpty(map) || map.size() <= 0) {
				 * addActionError(getText("empty.villageSelection")); return
				 * INPUT; } List<String> villageCodeList = new
				 * ArrayList<String>(map.keySet()); List<Village> villages =
				 * locationService.listOfVillagesByCodes(villageCodeList); if
				 * (!ObjectUtil.isEmpty(villages)) { temp.getVillages().clear();
				 * temp.setVillages(new HashSet<Village>(villages)); }
				 */
				locationService.editWarehouse(temp);
				sessionScopMap.put("selVillageMap", null);
			}
			request.setAttribute(HEADING, getText("cooperativelist"));
			return LIST;
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
		setType(getType());
		setEnableStorage(preferncesService.findPrefernceByName(ESESystem.ENABLE_STORAGE));
		String view = "";
		if (id != null && !id.equals("")) {
			if (getCurrentTenantId().equalsIgnoreCase("chetna") || getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)) {
			warehouse = locationService.findWarehouseWithoutTypeById(Long.valueOf(id));
			}else{
			warehouse = locationService.findWarehouseById(Long.valueOf(id));}
			if (warehouse.getCapacityInTonnes()!=null && !StringUtil.isEmpty(warehouse.getCapacityInTonnes())) {
				String capacityDeci = CurrencyUtil.getDecimalFormat(Double.valueOf(warehouse.getCapacityInTonnes()),
						"##.000");
				warehouse.setCapacityInTonnes(capacityDeci);
				
			}
			if(!ObjectUtil.isEmpty(warehouse)&& warehouse.getStorageCommodity()!=null && !StringUtil.isEmpty(warehouse.getStorageCommodity()) && warehouse.getStorageCommodity().length()>1){
				String[] codeArr=warehouse.getStorageCommodity().split(",");
				String commodityStr ="";
				
				for(String code:codeArr){
					
				FarmCatalogue farmCatlogue=catalogueService.findCatalogueByCode(code.trim());
				if(!ObjectUtil.isEmpty(farmCatlogue))
				commodityStr+=farmCatlogue.getName().concat(",");
				
				}
				commodityStr=StringUtil.removeLastComma(commodityStr);
				warehouse.setStorageCommodity(commodityStr);
			}

			
			if (warehouse == null) {
				addActionError(NO_RECORD);
				return list();
				// return REDIRECT;
			}
		
			if(!StringUtil.isEmpty(getEnableStorage()) && getEnableStorage().equalsIgnoreCase("1")){
			warehouseStorageMapList = new ArrayList<>();
			List<WarehouseStorageMap> calfListTemp = farmerService.listOfwarehouseStorageMap(warehouse.getId());
			calfListTemp.stream().filter(warehouseStorageMap -> !ObjectUtil.isEmpty(warehouseStorageMap)).forEach(warehouseStorageMap -> {
			//	warehouseStorageMap.setColdStorageName(String.valueOf(getCatlogueValueByCode(warehouseStorageMap.getColdStorageName()).getName()));
				warehouseStorageMap.setColdStorageName(String.valueOf(getCatlogueValueByCode(warehouseStorageMap.getColdStorageName()).getName()));
				
				warehouseStorageMap.setMaxBayHold(warehouseStorageMap.getMaxBayHold());
				warehouseStorageMapList.add(warehouseStorageMap);
				
			});
			}
			setCooperativeInSession(warehouse);
			setCurrentPage(getCurrentPage());
			command = UPDATE;
			view = DETAIL;
			request.setAttribute(HEADING, getText("cooperativedetail"));
		} else {
			request.setAttribute(HEADING, getText("cooperativelist"));
			return LIST;
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
		setType(getType());
		if (this.getId() != null && !(this.getId().equals(EMPTY))) {
			if (getCurrentTenantId().equalsIgnoreCase("chetna")|| getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)) {
			warehouse = locationService.findWarehouseWithoutTypeById(Long.valueOf(id));
			}else{
			warehouse = locationService.findWarehouseById(Long.valueOf(id));}
			if (warehouse == null) {
				addActionError(NO_RECORD);
				return list();
			}
			setCurrentPage(getCurrentPage());
			if (!ObjectUtil.isEmpty(warehouse)) {
				// Check Cooperative and Samithi Mapping
				boolean isAgentExist = agentService.findAgentMappedWithWarehouse(Long.parseLong(this.getId()));
				if (isAgentExist) {
					setCooperativeInSession(warehouse);
					addActionError(getText("delete.cooperativeMan.warn"));
					request.setAttribute(HEADING, getText("cooperativedetail"));
					return DETAIL;
				}
				// Check Cooperative and Cooperative Manager
				boolean isSamithiExist = locationService.findCooperativeMappedWithSamithi(warehouse.getCode());
				if (isSamithiExist) {
					setCooperativeInSession(warehouse);
					addActionError(getText("delete.samithi.warn"));
					request.setAttribute(HEADING, getText("cooperativedetail"));
					return DETAIL;
				}

				boolean isWarehousePayment = locationService
						.findCooperativeMappedWithWarhousePayment(warehouse.getId());
				if (isWarehousePayment) {
					setCooperativeInSession(warehouse);
					addActionError(getText("delete.payment.warn"));
					request.setAttribute(HEADING, getText("cooperativedetail"));
					return DETAIL;
				}
				if(getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
				boolean isAgentMapped = agentService.findAgentsMappedWithWarehouse(Long.parseLong(this.getId()));
					if(isAgentMapped){
						setCooperativeInSession(warehouse);
						addActionError(getText("delete.mobileUser.warn"));
						request.setAttribute(HEADING, getText("cooperativedetail"));
						return DETAIL;
					}
				}
				// Cooperative Record delete logic
				locationService.removeWarehouse(warehouse);
			}
		}
		request.setAttribute(HEADING, getText("cooperativelist"));
		return LIST;

	}

	/**
	 * Sets the cooperative in session.
	 * 
	 * @param warehouse
	 *            the new cooperative in session
	 */
	private void setCooperativeInSession(Warehouse warehouse) {

		sessionScopMap.put("selVillageMap", null);
		selectedVillageMap = new HashMap<String, Object>();
		for (Village village : warehouse.getVillages()) {
			selectedVillageMap.put(village.getCode(),
					(Object) (village.getName() + " / " + village.getGramPanchayat().getCity().getName()));
		}
		sessionScopMap.put("selVillageMap", selectedVillageMap);
	}

	/**
	 * Grid check.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String gridCheck() throws Exception {

		List<String> mappedVillages = new ArrayList<String>();
		if (!ObjectUtil.isEmpty(getAvailableVillages()) && !StringUtil.isEmpty(getId())) {
			for (String avlVillage : getAvailableVillages()) {
				String villageName = locationService
						.findCooperativeVillageMappedWtihSamithiVillage(Long.parseLong(getId()), avlVillage);
				if (!StringUtil.isEmpty(villageName)) {
					mappedVillages.add(villageName);
				}
			}
		}
		if (!ObjectUtil.isListEmpty(mappedVillages)) {
			sendResponse(getText("delete.samithi.village.warn"));
			return null;
		}
		return null;
	}

	/**
	 * Grid process.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings("unchecked")
	public String gridProcess() throws Exception {

		Map selVillageMap = (Map) sessionScopMap.get("selVillageMap");
		if (!ObjectUtil.isEmpty(selVillageMap)) {
			selectedVillageMap.putAll(selVillageMap);
		}
		if (!ObjectUtil.isEmpty(getAvailableVillages())) {
			for (String avlVillage : getAvailableVillages()) {
				if (selectedVillageMap.containsKey(avlVillage)) {
					selectedVillageMap.remove(avlVillage);
				}
			}
		}
		if (!ObjectUtil.isEmpty(getSelectedVillages())) {
			String blockName = null;
			if (!StringUtil.isEmpty(selectedBlock))
				blockName = locationService.findCityNameByCode(selectedBlock);
			for (String selVillage : getSelectedVillages()) {
				selectedVillageMap.put(selVillage,
						locationService.findVillageNameByCode(selVillage) + " / " + blockName);
			}
		}
		sessionScopMap.put("selVillageMap", selectedVillageMap);
		sendResponse(selectedVillageMap);
		return null;
	}

	/**
	 * Gets the blockes.
	 * 
	 * @return the blockes
	 */
	@SuppressWarnings("unchecked")
	public List<Municipality> getBlockes() {

		blockes = locationService.listCity();
		return blockes;
	}

	/**
	 * Populate villages.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String populateVillages() throws Exception {

		// Load Villages details
		if (!ObjectUtil.isEmpty(selectedBlock)) {
			availableVillageObjs = locationService.listOfVillageCodeNameByCityCode(selectedBlock);
			sendResponse(buildOptionByObjArray(availableVillageObjs));
		}
		return null;
	}

	/**
	 * Builds the option by obj array.
	 * 
	 * @param villageList
	 *            the village list
	 * @return the string
	 */
	@SuppressWarnings("unchecked")
	public String buildOptionByObjArray(List<Object[]> villageList) {

		Map selVillageMap = (Map) sessionScopMap.get("selVillageMap");
		StringBuffer villagePickList = new StringBuffer();
		StringBuffer avlVillageOptions = new StringBuffer();
		StringBuffer selVillageOptions = new StringBuffer();
		String listString = null;
		for (Object[] objArray : villageList) {
			listString = null;
			if (ObjectUtil.isEmpty(selVillageMap) || !selVillageMap.containsKey((String) objArray[0])) {
				listString = PATTERN_OPTION.replaceAll(OPTION_KEY, (String) objArray[0]).replaceAll(OPTION_VALUE,
						(String) objArray[1]);
				avlVillageOptions.append(listString);
			} else {
				listString = PATTERN_OPTION.replaceAll(OPTION_KEY, (String) objArray[0]).replaceAll(OPTION_VALUE,
						(String) objArray[1]);
				selVillageOptions.append(listString);

			}
		}
		villagePickList.append(avlVillageOptions.toString());
		villagePickList.append("~");
		villagePickList.append(selVillageOptions.toString());

		return villagePickList.toString();
	}

	public void prepare() {
		String actionClassName = ActionContext.getContext().getActionInvocation().getAction().getClass()
				.getSimpleName();
		String content = getLocaleProperty(actionClassName + "." + BreadCrumb.BREADCRUMB);
		String tenant = getCurrentTenantId();
		if (tenant.equalsIgnoreCase("kpf")|| tenant.equalsIgnoreCase("simfed")|| tenant.equalsIgnoreCase("wub")) {
			setType(request.getParameter("type"));
			if (getType() == null) {
				setType(getType());
			}

			if (StringUtil.isEmpty(content)&& getType()!=null) {
				content = getText(BreadCrumb.BREADCRUMB.concat(getType()), "");
			}
		} else {
			if (StringUtil.isEmpty(content)|| (content.equalsIgnoreCase(actionClassName + "." + BreadCrumb.BREADCRUMB))) {
				content = getText(BreadCrumb.BREADCRUMB, "");
			}
		}
		if(!StringUtil.isEmpty(content)){
		request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(content));}
	}

	@Override
	public String getCurrentMenu() {
		String type = request.getParameter("type");
		String tenant = getCurrentTenantId();
		if (tenant.equalsIgnoreCase("kpf")|| tenant.equalsIgnoreCase("simfed")|| tenant.equalsIgnoreCase("wub")) {
			if (!StringUtil.isEmpty(type)) {
				if (type.equalsIgnoreCase("cooperative")) {
					return getText("menu.select");
				} else if (type.equalsIgnoreCase("collectionCenter")) {
					return getText("menu1.select");
				}
			}
			return getText("menu.select");
		} else {
			return getText("menu.select");
		}
	}

	/**
	 * Gets the warehouse.
	 * 
	 * @return the warehouse
	 */
	public Warehouse getWarehouse() {

		return warehouse;
	}

	/**
	 * Sets the warehouse.
	 * 
	 * @param warehouse
	 *            the new warehouse
	 */
	public void setWarehouse(Warehouse warehouse) {

		this.warehouse = warehouse;
	}

	/**
	 * Sets the location service.
	 * 
	 * @param locationService
	 *            the new location service
	 */
	public void setLocationService(ILocationService locationService) {

		this.locationService = locationService;
	}

	/**
	 * Gets the location service.
	 * 
	 * @return the location service
	 */
	public ILocationService getLocationService() {

		return locationService;
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
	 * Gets the selected block.
	 * 
	 * @return the selected block
	 */
	public String getSelectedBlock() {

		return selectedBlock;
	}

	/**
	 * Sets the selected block.
	 * 
	 * @param selectedBlock
	 *            the new selected block
	 */
	public void setSelectedBlock(String selectedBlock) {

		this.selectedBlock = selectedBlock;
	}

	/**
	 * Gets the available villages.
	 * 
	 * @return the available villages
	 */
	public List<String> getAvailableVillages() {

		return availableVillages;
	}

	/**
	 * Sets the available villages.
	 * 
	 * @param availableVillages
	 *            the new available villages
	 */
	public void setAvailableVillages(List<String> availableVillages) {

		this.availableVillages = availableVillages;
	}

	/**
	 * Gets the selected villages.
	 * 
	 * @return the selected villages
	 */
	public List<String> getSelectedVillages() {

		return selectedVillages;
	}

	/**
	 * Sets the selected villages.
	 * 
	 * @param selectedVillages
	 *            the new selected villages
	 */
	public void setSelectedVillages(List<String> selectedVillages) {

		this.selectedVillages = selectedVillages;
	}

	/**
	 * Gets the session scop map.
	 * 
	 * @return the session scop map
	 */
	public Map<String, Object> getSessionScopMap() {

		return sessionScopMap;
	}

	/**
	 * Sets the session scop map.
	 * 
	 * @param sessionScopMap
	 *            the session scop map
	 */
	public void setSessionScopMap(Map<String, Object> sessionScopMap) {

		this.sessionScopMap = sessionScopMap;
	}

	/**
	 * Gets the selected village map.
	 * 
	 * @return the selected village map
	 */
	public Map<String, Object> getSelectedVillageMap() {

		return selectedVillageMap;
	}

	/**
	 * Sets the selected village map.
	 * 
	 * @param selectedVillageMap
	 *            the selected village map
	 */
	public void setSelectedVillageMap(Map<String, Object> selectedVillageMap) {

		this.selectedVillageMap = selectedVillageMap;
	}

	/**
	 * Gets the agent service.
	 * 
	 * @return the agent service
	 */
	public IAgentService getAgentService() {

		return agentService;
	}

	/**
	 * Sets the agent service.
	 * 
	 * @param agentService
	 *            the new agent service
	 */
	public void setAgentService(IAgentService agentService) {

		this.agentService = agentService;
	}

	/**
	 * Sets the session.
	 * 
	 * @param session
	 *            the session
	 * @see org.apache.struts2.interceptor.SessionAware#setSession(java.util.Map)
	 */
	@Override
	public void setSession(Map<String, Object> session) {

		this.sessionScopMap = session;

	}

	/*
	 * public Map<String, String> getCommodityList() {
	 * 
	 * Map<String, String> commodityMap = new LinkedHashMap<String, String>();
	 * 
	 * List<FarmCatalogue> educationList =
	 * farmerService.listCatelogueType(getText("commodityType")); for
	 * (FarmCatalogue obj : educationList) { commodityMap.put(obj.getCode(),
	 * obj.getName()); } return commodityMap; }
	 */

	public Map<String, String> getCommodityList() {
		Map<String, String> commodityList = new LinkedHashMap<>();
		FarmCatalogueMaster farmCatalougeMaster = catalogueService
				.findFarmCatalogueMasterByName(getText("Storage Commodity"));
		if (!ObjectUtil.isEmpty(farmCatalougeMaster)) {
			Double d = new Double(farmCatalougeMaster.getId());
			List<FarmCatalogue> farmCatalougeList = catalogueService.findFarmCatalougeByType(d.intValue());
			commodityList = farmCatalougeList.stream()
					.collect(Collectors.toMap(obj -> (String.valueOf(obj.getCode())), FarmCatalogue::getName));
		}
		return commodityList;
	}

	/*
	 * public Map<String, String> getOwnershipList() {
	 * 
	 * Map<String, String> commodityMap = new LinkedHashMap<String, String>();
	 * 
	 * List<FarmCatalogue> educationList =
	 * farmerService.listCatelogueType(getText("ownershipType")); for
	 * (FarmCatalogue obj : educationList) { commodityMap.put(obj.getCode(),
	 * obj.getName()); } return commodityMap; }
	 */

	public Map<String, String> getOwnershipList() {
		Map<String, String> ownwershipList = new LinkedHashMap<>();
		FarmCatalogueMaster farmCatalougeMaster = catalogueService
				.findFarmCatalogueMasterByName(getText("Warehouse Ownership"));
		if (!ObjectUtil.isEmpty(farmCatalougeMaster)) {
			Double d = new Double(farmCatalougeMaster.getId());
			List<FarmCatalogue> farmCatalougeList = catalogueService.findFarmCatalougeByType(d.intValue());
			ownwershipList = farmCatalougeList.stream()
					.collect(Collectors.toMap(obj -> (String.valueOf(obj.getCode())), FarmCatalogue::getName));
		}
		return ownwershipList;
	}

	public void setIdGenerator(IUniqueIDGenerator idGenerator) {

		this.idGenerator = idGenerator;
	}

	public void populateCommodity() {

		if (!commodityName.equalsIgnoreCase("null") && (!StringUtil.isEmpty(commodityName))) {

			FarmCatalogueMaster farmCatalougeMaster = catalogueService
					.findFarmCatalogueMasterByName(getText("Storage Commodity"));
			if (!ObjectUtil.isEmpty(farmCatalougeMaster)) {
				int catalogueTypez = farmCatalougeMaster.getTypez();

				FarmCatalogue catalogue = catalogueService.findByNameAndType(commodityName, catalogueTypez);
				if (catalogue == null) {
					FarmCatalogue farmCatalogue = new FarmCatalogue();
					farmCatalogue.setCode(idGenerator.getCatalogueIdSeq());
					farmCatalogue.setRevisionNo(DateUtil.getRevisionNumber());
					farmCatalogue.setBranchId(getBranchId());
					farmCatalogue.setName(commodityName);
					farmCatalogue.setTypez(catalogueTypez);
					farmCatalogue.setStatus(FarmCatalogue.ACTIVE);
					catalogueService.addCatalogue(farmCatalogue);
					JSONArray stateArr = new JSONArray();
					List<FarmCatalogue> educationList = farmerService.listCatelogueType(String.valueOf(catalogueTypez));
					if (!ObjectUtil.isEmpty(educationList)) {

						for (FarmCatalogue obj : educationList) {
							stateArr.add(getJSONObject(obj.getCode(), obj.getName()));
						}
					}
					sendAjaxResponse(stateArr);
				} else {
					String result = "0";
					sendAjaxResponse(result);
				}
			}
		} else {
			String result = "0";
			sendAjaxResponse(result);
		}

	}

	public void populateOwnership() {

		if (!ownershipName.equalsIgnoreCase("null") && (!StringUtil.isEmpty(ownershipName))) {

			FarmCatalogueMaster farmCatalougeMaster = catalogueService
					.findFarmCatalogueMasterByName(getText("Warehouse Ownership"));
			if (!ObjectUtil.isEmpty(farmCatalougeMaster)) {
				int catalogueTypez = farmCatalougeMaster.getTypez();

				FarmCatalogue catalogue = catalogueService.findByNameAndType(ownershipName, catalogueTypez);
				if (catalogue == null) {
					FarmCatalogue farmCatalogue = new FarmCatalogue();
					farmCatalogue.setCode(idGenerator.getCatalogueIdSeq());
					farmCatalogue.setRevisionNo(DateUtil.getRevisionNumber());
					farmCatalogue.setBranchId(getBranchId());
					farmCatalogue.setName(ownershipName);
					farmCatalogue.setTypez(catalogueTypez);
					farmCatalogue.setStatus(FarmCatalogue.ACTIVE);
					catalogueService.addCatalogue(farmCatalogue);
					JSONArray stateArr = new JSONArray();
					List<FarmCatalogue> educationList = farmerService.listCatelogueType(String.valueOf(catalogueTypez));
					if (!ObjectUtil.isEmpty(educationList)) {

						for (FarmCatalogue obj : educationList) {
							stateArr.add(getJSONObject(obj.getCode(), obj.getName()));
						}
					}
					sendAjaxResponse(stateArr);
				} else {
					String result = "0";
					sendAjaxResponse(result);
				}
			}
		} else {
			String result = "0";
			sendAjaxResponse(result);
		}

	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}

	public void setCommodityList(Map<Integer, String> commodityList) {
		this.commodityList = commodityList;
	}

	public void setOwnershipList(Map<Integer, String> ownershipList) {
		this.ownershipList = ownershipList;
	}

	public ICatalogueService getCatalogueService() {
		return catalogueService;
	}

	public void setCatalogueService(ICatalogueService catalogueService) {
		this.catalogueService = catalogueService;
	}

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	public String getOwnershipName() {
		return ownershipName;
	}

	public void setOwnershipName(String ownershipName) {
		this.ownershipName = ownershipName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSelectedCommodity() {
		return selectedCommodity;
	}

	public void setSelectedCommodity(String selectedCommodity) {
		this.selectedCommodity = selectedCommodity;
	}

	public Map<Integer, String> getWarehouseType() {
		return warehouseType;
	}

	public void setWarehouseType(Map<Integer, String> warehouseType) {
		this.warehouseType = warehouseType;
	}
	public Map<String, String> getColdStorageNameList() {

		Map<String, String> farmCatalougeList = new LinkedHashMap<>();
		farmCatalougeList = getFarmCatalougeMap(Integer.valueOf(getLocaleProperty("coldStorageCatalog").trim()));
		return farmCatalougeList;

	}
	
	public Map<String, String> getBlockNameList() {

		Map<String, String> farmCatalougeList = new LinkedHashMap<>();
		farmCatalougeList = getFarmCatalougeMap(Integer.valueOf(getLocaleProperty("blockNameCatalog").trim()));
		return farmCatalougeList;

	}
	
	public Map<String, String> getFloorNameList() {

		Map<String, String> farmCatalougeList = new LinkedHashMap<>();
		farmCatalougeList = getFarmCatalougeMap(Integer.valueOf(getLocaleProperty("floorNameCatalog").trim()));
		return farmCatalougeList;

	}
	
	public Map<String, String> getBayNumList() {

		Map<String, String> farmCatalougeList = new LinkedHashMap<>();
		farmCatalougeList = getFarmCatalougeMap(Integer.valueOf(getLocaleProperty("bayNumCatalog").trim()));
		return farmCatalougeList;

	}

	public String getStorageArrayToString() {
		return storageArrayToString;
	}

	public void setStorageArrayToString(String storageArrayToString) {
		this.storageArrayToString = storageArrayToString;
	}

	public String getEnableStorage() {
		return enableStorage;
	}

	public void setEnableStorage(String enableStorage) {
		this.enableStorage = enableStorage;
	}

	public String getSelectedColdStorageName() {
		return selectedColdStorageName;
	}

	public void setSelectedColdStorageName(String selectedColdStorageName) {
		this.selectedColdStorageName = selectedColdStorageName;
	}

	public Double getMaxBayWeight() {
		return maxBayWeight;
	}

	public void setMaxBayWeight(Double maxBayWeight) {
		this.maxBayWeight = maxBayWeight;
	}

	public List<WarehouseStorageMap> getWarehouseStorageMapList() {
		return warehouseStorageMapList;
	}

	public void setWarehouseStorageMapList(List<WarehouseStorageMap> warehouseStorageMapList) {
		this.warehouseStorageMapList = warehouseStorageMapList;
	}



	
	
}
