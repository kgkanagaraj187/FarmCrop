/*
 * SamithiAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view.general;

import java.io.PrintWriter;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.ese.entity.util.ESESystem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.FarmCatalogueMaster;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.entity.MasterData.masterTypes;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.BankInformation;
import com.sourcetrace.esesw.entity.profile.Country;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.GramPanchayat;
import com.sourcetrace.esesw.entity.profile.Locality;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.State;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

// TODO: Auto-generated Javadoc
/**
 * The Class CooperativeAction.
 */
public class SamithiAction extends SwitchValidatorAction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(SamithiAction.class);

	public static final String OPTION_CONSTANT = "<option value=\"-1\">--Select--</option>";
	public static final String OPTION_KEY = "<!--optionKey--!>";
	public static final String OPTION_VALUE = "<!--optionValue--!>";
	public static final String PATTERN_OPTION = "<option value='<!--optionKey--!>'><!--optionValue--!></option>";

	private String id;
	private String selectedCooperative;
	private String errorMsg;
	private String farmerMappingExist;
	private String agentMappingExist;
	private String selectedVillageCodes;
	private String selectedCityids;
	private String maleFarmers = "0";
	private String feMaleFarmers = "0";
	private String fieldStaffNames;
	private DateFormat genDate;
	private Warehouse warehouse;

	private List<String> availableVillages = new ArrayList<String>();
	private List<String> selectedVillages = new ArrayList<String>();
	private Map<String, Object> selectedVillageMap = new HashMap<String, Object>();
	private List<Warehouse> cooperatives = new ArrayList<Warehouse>();
	private List<Village> villageCodesList = new ArrayList<Village>();
	private List<String> selectedCitys = new ArrayList<String>();
	private Map<Long, String> municipalityList = new LinkedHashMap<Long, String>();
	private Map<String, Object> selectedCityMap = new HashMap<String, Object>();
	  private Map<Integer, String> icsStatusList = new LinkedHashMap<Integer, String>();
	private ILocationService locationService;
	private IAgentService agentService;
	private IUniqueIDGenerator idGenerator;
	private ICatalogueService catalogueService;
	private IPreferencesService preferncesService;
	private IFarmerService farmerService;

	private String selectedMunipalIds;
	private String selectedMunicipalityId;
	private String municipalityIds;
	private String samithiId;
	private String selectedVillagesValue;
	private Boolean editFlag;
	
	private String village;
	private String sangham;
	private String formationDate;
	SimpleDateFormat formatter = new SimpleDateFormat(DateUtil.TXN_TIME_FORMAT);

	private String groupFormationDate;

	private BankInformation bankInfo;

	private String bankAccType;
	protected Map<Long, String> farmerCount = new LinkedHashMap<Long, String>();
	protected Map<Long, String> cropArea = new LinkedHashMap<Long, String>();
	
	private String coOperativeId;
	private String branchId_F;
	
	
	private String selectedCountry;
	private String selectedState;
	private String selectedLocality;
	private String selectedCity;
	private String selectedPanchayat;
	private String selectedVillage;
	
	List<State> states = new ArrayList<State>();
	List<Locality> localities = new ArrayList<Locality>();
	List<Municipality> cities = new ArrayList<Municipality>();
	List<Locality> listLocalities = new ArrayList<Locality>();
	private String gramPanchayatEnable;
	List<GramPanchayat> panchayat = new ArrayList<GramPanchayat>();
	List<Village> villages = new ArrayList<Village>();
	
	
	
	/**
	 * Gets the data.
	 * 
	 * @return the data
	 * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
	 */
	@Override
	public Object getData() {

		if (!ObjectUtil.isEmpty(warehouse)) {

			/*
			 * Municipality municipality = new Municipality();
			 * municipality.setId(Long.valueOf(getSelectedMunicipalityId()));
			 */

			warehouse.setMultiplecityId(selectedMunicipalityId);
		}
		getCurrentTenantId();
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
		setGramPanchayatEnable(preferences.getPreferences().get(ESESystem.ENABLE_GRAMPANJAYAT));
		}
		return warehouse;
	}

	/**
	 * To json.
	 * 
	 * @param obj
	 *            the obj
	 * @return the JSON object
	 * @see com.sourcetrace.esesw.view.SwitchAction#toJSON(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		Warehouse warehouse = (Warehouse) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(warehouse.getBranchId())))
						? getBranchesMap().get(getParentBranchMap().get(warehouse.getBranchId()))
						: getBranchesMap().get(warehouse.getBranchId()));
			}
			rows.add(getBranchesMap().get(warehouse.getBranchId()));

		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(branchesMap.get(warehouse.getBranchId()));
			}
		}
		if (getCurrentTenantId().equalsIgnoreCase("welspun")) {
			rows.add(
					"<font color=\"#0000FF\" style=\"cursor:pointer;\">" + warehouse.getCapacityInTonnes() + "</font>");
		} else {
			rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">" + warehouse.getCode() + "</font>");
		}
		rows.add(warehouse.getName());

		if (getIsKpfBased().equals("1") && !getCurrentTenantId().equals("gsma")) {
			String mastertype = "-";
			Boolean flag = false;

			for (int i = 0; i <= MasterData.masterTypes.values().length; i++) {
				if (!StringUtil.isEmpty(warehouse.getGroupType())) {
					if (Integer.parseInt(warehouse.getGroupType()) == i) {
						mastertype = getText("masterType" + i);
						rows.add(mastertype);
						flag = true;
					}
				} else {
					rows.add("");
				}
			}

			if (!flag)
				rows.add(mastertype);

		}

		if (getCurrentTenantId().equals("symrise")) {
			formationDate = "";
			if (warehouse.getGroupFormationDate() != null && genDate != null) {
				formationDate = genDate.format(warehouse.getGroupFormationDate());

			}
			rows.add(formationDate);
		}
		if (getCurrentTenantId().equals("susagri")) {
		rows.add(getFarmerCount().get(warehouse.getId()));
		rows.add((StringUtil.isEmpty(getCropArea().get(warehouse.getId())) && getCropArea().get(warehouse.getId())==null)? "0" : getCropArea().get(warehouse.getId()) + " "+ ("Hectare") );
		}
		// rows.add(warehouse.getCitys().toString().replace("[","").replace("]",""));
		jsonObject.put("id", warehouse.getId());
		jsonObject.put("cell", rows);

		return jsonObject;

	}

	/**
	 * Data.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 * @see com.sourcetrace.esesw.view.SwitchAction#data()
	 */
	@SuppressWarnings("unchecked")
	public String data() throws Exception {

		Map<String, String> searchRecord = getJQGridRequestParam();

		Warehouse filter = new Warehouse();

		if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
			if (!getIsMultiBranch().equalsIgnoreCase("1")) {
				List<String> branchList = new ArrayList<>();
				branchList.add(searchRecord.get("branchId").trim());
				filter.setBranchesList(branchList);
				filter.setBranchId(searchRecord.get("branchId").trim());
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
		if (!StringUtil.isEmpty(searchRecord.get("groupType"))) {
			filter.setGroupType(searchRecord.get("groupType").trim());
		}
		/*
		 * if (!StringUtil.isEmpty(searchRecord.get("w.name"))) { Warehouse
		 * warehouse = new Warehouse();
		 * warehouse.setName(searchRecord.get("w.name"));
		 * filter.setRefCooperative(warehouse);
		 * 
		 * }
		 */
		/*
		 * if(!StringUtil.isEmpty(searchRecord.get("c.name"))){ Municipality
		 * city=new Municipality();
		 * city.setName(searchRecord.get("c.name").trim());
		 * filter.setCity(city); }
		 */
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!ObjectUtil.isEmpty(preferences)) {
			genDate = new SimpleDateFormat(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
		} else {
			genDate = new SimpleDateFormat(DateUtil.DATABASE_DATE_TIME);
		}
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

	public String create() throws Exception {

		if (warehouse == null) {
			warehouse = new Warehouse();

			command = "create";
			request.setAttribute(HEADING, getText(CREATE));
			return INPUT;
		} else {

			ESESystem preferences = preferncesService.findPrefernceById("1");
			String codeGenType = preferences.getPreferences().get(ESESystem.CODE_TYPE);
			if (codeGenType.equals("1")) {
				String codeFormat = getVillage();
				BigInteger codeSeq = new BigInteger(idGenerator.getGroupHHIdSeq(codeFormat));
				String maxCode = codeSeq.subtract(new BigInteger("1")).toString();
				String groupMaxCode = maxCode.substring(6, 8);
				if (Integer.valueOf(groupMaxCode) >= 99) {
					addActionError(getText("error.farmerExceed"));
					return INPUT;
				} else {
					warehouse.setCode(idGenerator.getGroupHHIdSeq(codeFormat));
				}
			} else {
				if (getCurrentTenantId().equalsIgnoreCase("welspun")) {
					String branch = getBranchId();
					String codeSeq = idGenerator.getBranchMasterWebSeq(branch);
					// String maxCode = codeSeq.subtract(new
					// BigInteger("1")).toString();
					// String groupMaxCode = maxCode.substring(6, 8);
					warehouse.setCode(branch + String.valueOf(codeSeq));
					warehouse.setCapacityInTonnes(String.valueOf(codeSeq));
				}

				else {
					warehouse.setCode(idGenerator.getGroupIdSeq());
				}
			}
			warehouse.setName(warehouse.getName().trim());
			warehouse.setBranchId(getBranchId());
			warehouse.setCreatedUsername(getUsername());
			warehouse.setUpdatedUsername(getUsername());
			warehouse.setCreatedDate(new Date());
			warehouse.setUpdatedDate(new Date());
			warehouse.setTypez(Warehouse.SAMITHI);
			warehouse.setSanghamType(sangham);
			if (!StringUtil.isEmpty(formationDate)) {
				warehouse.setFormationDate(DateUtil.convertStringToDate(formationDate, DateUtil.PROFILE_DATE_FORMAT));
			}

			if (!StringUtil.isEmpty(groupFormationDate)) {
				warehouse.setGroupFormationDate(
						DateUtil.convertStringToDate(groupFormationDate, getGeneralDateFormat()));
			} else {
				warehouse.setGroupFormationDate(null);
			}
			
			if (getCurrentTenantId().equalsIgnoreCase("kenyafpo")) {
				if(!StringUtil.isEmpty(selectedVillage)){
					Village village =locationService.findVillage(Long.valueOf(selectedVillage));
					
					warehouse.setVillageId(!ObjectUtil.isEmpty(village) ? village.getId() : null) ;
					
					warehouse.setPresidentName(warehouse.getPresidentName());
					warehouse.setPresidentMobileNumber(warehouse.getPresidentMobileNumber());
					warehouse.setEmail(warehouse.getEmail());
				}
				
			}
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)) {
				warehouse.setTotalMembers(warehouse.getTotalMembers());
				warehouse.setNoOfMale(warehouse.getNoOfMale());
				warehouse.setNoOfFemale(warehouse.getNoOfFemale());

				warehouse.setPresidentName(warehouse.getPresidentName());
				warehouse.setPresidentMobileNumber(warehouse.getPresidentMobileNumber());
				warehouse.setSecretaryName(warehouse.getSecretaryName());
				warehouse.setSecretaryMobileNumber(warehouse.getSecretaryMobileNumber());
				warehouse.setTreasurer(warehouse.getTreasurer());
				warehouse.setTreasurerMobileNumber(warehouse.getTreasurerMobileNumber());

				String jsonString = warehouse.getJsonString();
				List<BankInformation> bankInformationList = new ArrayList<BankInformation>();

				if (!StringUtil.isEmpty(jsonString)) {
					GsonBuilder gsonBuilder = new GsonBuilder();
					Gson gson = gsonBuilder.create();
					bankInformationList = Arrays.asList(gson.fromJson(jsonString, BankInformation[].class));
				}

				Set<BankInformation> bankInformationSet = new HashSet<BankInformation>();
				for (BankInformation bankInformation : bankInformationList) {
					if (!StringUtil.isEmpty(bankInformation.getAccNo())) {
						bankInformation.setAccType(bankInformation.getAccTypeCode());
						bankInformation.setAccNo(bankInformation.getAccNo());
						bankInformation.setBankName(bankInformation.getBankName());
						bankInformation.setBranchName(bankInformation.getBranchName());
						bankInformation.setSortCode(bankInformation.getSortCode());
						bankInformation.setSwiftCode(bankInformation.getSwiftCode());

						farmerService.addBankInformation(bankInformation);
						bankInformationSet.add(bankInformation);
					}
				}
				warehouse.setBankInfo(bankInformationSet);

			}
			locationService.addWarehouse(warehouse);
			saveDynamicField("381", String.valueOf(warehouse.getId()), "", "3");
			/* setDynamicFieldValues(warehouse.getId()); */

			return REDIRECT;
		}
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
			warehouse = locationService.findSamithiById(Long.valueOf(id));
			if (warehouse.getFormationDate() != null) {
				String replaceDate = warehouse.getFormationDate().toString().split("\\.", 2)[0];
				Date date = formatter.parse(replaceDate.trim());
				SimpleDateFormat sdfSource = new SimpleDateFormat(DateUtil.PROFILE_DATE_FORMAT);
				formationDate = sdfSource.format(date);

			}

			if (warehouse == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}

			if (getCurrentTenantId().equalsIgnoreCase("atma")) {
				String code = warehouse.getCode();
				if (!StringUtil.isEmpty(code) && code.length() == 8) {
					String villageCode = code.substring(0, 6);
					Village village = locationService.findVillageByCode(villageCode);
					if (!ObjectUtil.isEmpty(village)) {
						setVillage(String.valueOf(village.getCode()));

						setSelectedPanchayat(!ObjectUtil.isEmpty(village.getGramPanchayat())
								? String.valueOf(village.getGramPanchayat().getId()) : "");

						setSelectedCity(!ObjectUtil.isEmpty(village.getGramPanchayat())
								&& !ObjectUtil.isEmpty(village.getGramPanchayat().getCity())
										? String.valueOf(village.getGramPanchayat().getCity().getId()) : "");

						setSelectedLocality(!ObjectUtil.isEmpty(village.getGramPanchayat())
								&& !ObjectUtil.isEmpty(village.getGramPanchayat().getCity())
								&& !ObjectUtil.isEmpty(village.getGramPanchayat().getCity().getLocality())
										? String.valueOf(village.getGramPanchayat().getCity().getLocality().getId())
										: "");
						/*
						 * String sanghamVal = warehouse.getSanghamType();
						 * setSangham(sanghamVal);
						 */

						setSangham(warehouse.getSanghamType());
					}

				} else {
					setSangham(warehouse.getSanghamType());
				}
			}

			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)) {
				ESESystem preferences = preferncesService.findPrefernceById("1");
				if (!ObjectUtil.isEmpty(preferences)) {
					DateFormat genDate = new SimpleDateFormat(
							preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
					if (warehouse.getGroupFormationDate() != null) {
						groupFormationDate = genDate.format(warehouse.getGroupFormationDate());
					}
				}

				Set<BankInformation> bankInfoSet = new HashSet<>();
				List<BankInformation> bankInfo = locationService.findWarehouseBankinfo(warehouse.getId());
				if (!ObjectUtil.isListEmpty(bankInfo)) {
					bankInfoSet.addAll(bankInfo);
				}
				if (!ObjectUtil.isEmpty(bankInfoSet)) {
					warehouse.setJsonString(bankInformationToJson(bankInfoSet));
				}
			}
			
			if (getCurrentTenantId().equalsIgnoreCase("kenyafpo")) {
				Village v = locationService.findVillage(warehouse.getVillageId());
				if (!ObjectUtil.isEmpty(v)) {
					
					if (getGramPanchayatEnable().equalsIgnoreCase("1")) {			
						
						if (!ObjectUtil.isEmpty(v.getGramPanchayat())) {
							setSelectedCountry(v.getGramPanchayat().getCity().getLocality().getState().getCountry().getName());
							setSelectedState(String.valueOf(v.getGramPanchayat().getCity().getLocality().getState().getId()));
							setSelectedLocality(String.valueOf(v.getGramPanchayat().getCity().getLocality().getId()));
							setSelectedCity(String.valueOf(v.getGramPanchayat().getCity().getId()));
							setSelectedPanchayat(String.valueOf(v.getGramPanchayat().getId()));
							setSelectedVillage(String.valueOf(v.getId()));
						}
					} else {
						
							setSelectedCountry(v.getCity().getLocality().getState().getCountry().getName());
							setSelectedState(String.valueOf(v.getCity().getLocality().getState().getId()));
							setSelectedLocality(String.valueOf(v.getCity().getLocality().getId()));
							setSelectedCity(String.valueOf(v.getCity().getId()));
							setSelectedVillage(String.valueOf(v.getId()));
					
					}
				}
				
				
				
			}
			setCurrentPage(getCurrentPage());
			id = null;
			command = UPDATE;
			request.setAttribute(HEADING, getText(UPDATE));
		} else {
			if (warehouse != null) {

				Warehouse temp = locationService.findSamithiById(warehouse.getId());

				if (temp == null) {
					addActionError(NO_RECORD);
					return REDIRECT;
				}
				setCurrentPage(getCurrentPage());
				temp.setName(warehouse.getName());

				ESESystem preferences = preferncesService.findPrefernceById("1");
				String codeGenType = preferences.getPreferences().get(ESESystem.CODE_TYPE);
				if (codeGenType.equals("1")) {
					if (!(temp.getCode().substring(0, 6).equalsIgnoreCase(getVillage()))
							|| !(temp.getSanghamType().equalsIgnoreCase(getSangham()))) {
						Integer farmerCount = locationService.findFarmerCountBySamtihi(warehouse.getId());
						/*
						 * if (farmerCount > 0) {
						 * addActionError(getText("error.update")); return
						 * INPUT; }
						 */
						String codeFormat = getVillage();
						temp.setCode(idGenerator.getGroupHHIdSeq(codeFormat));
					}
				}
				warehouse.setSanghamType(getSangham());
				warehouse.setName(warehouse.getName().trim());

				temp.setSanghamType(getSangham());
				temp.setGroupType(warehouse.getGroupType());
				if (!StringUtil.isEmpty(formationDate)) {

					warehouse.setFormationDate(
							DateUtil.convertStringToDate(formationDate, DateUtil.PROFILE_DATE_FORMAT));
					temp.setFormationDate(warehouse.getFormationDate());
				}
				if (!StringUtil.isEmpty(groupFormationDate)) {
					temp.setGroupFormationDate(
							DateUtil.convertStringToDate(groupFormationDate, getGeneralDateFormat()));
				}
				if (getCurrentTenantId().equalsIgnoreCase("kenyafpo")) {
					if(!StringUtil.isEmpty(selectedVillage)){
						Village village =locationService.findVillage(Long.valueOf(selectedVillage));
						
						temp.setVillageId(!ObjectUtil.isEmpty(village) ? village.getId() : null) ;
					}
					
				}
				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)) {

					temp.setTotalMembers(warehouse.getTotalMembers());

					temp.setNoOfMale(warehouse.getNoOfMale());
					temp.setNoOfFemale(warehouse.getNoOfFemale());
					temp.setPresidentName(warehouse.getPresidentName());
					temp.setPresidentMobileNumber(warehouse.getPresidentMobileNumber());
					temp.setSecretaryName(warehouse.getSecretaryName());
					temp.setSecretaryMobileNumber(warehouse.getSecretaryMobileNumber());
					temp.setTreasurer(warehouse.getTreasurer());
					temp.setTreasurerMobileNumber(warehouse.getTreasurerMobileNumber());

					String jsonString = warehouse.getJsonString();
					List<BankInformation> bankInformationList = new ArrayList<BankInformation>();

					if (!StringUtil.isEmpty(jsonString)) {
						GsonBuilder gsonBuilder = new GsonBuilder();

						Gson gson = gsonBuilder.create();
						bankInformationList = Arrays.asList(gson.fromJson(jsonString, BankInformation[].class));
					} else {
						bankInformationList = new ArrayList<BankInformation>();
					}

					Set<BankInformation> bankInformationSet = new HashSet<BankInformation>();
					if (bankInformationList.isEmpty()) {
						temp.setBankInfo(new HashSet<BankInformation>());
					} else {
						for (BankInformation bankInformation : bankInformationList) {
							if (!StringUtil.isEmpty(bankInformation.getAccNo())) {
								bankInformation.setAccType(bankInformation.getAccType());
								bankInformation.setAccNo(bankInformation.getAccNo());
								bankInformation.setBankName(bankInformation.getBankName());
								bankInformation.setBranchName(bankInformation.getBranchName());
								bankInformation.setSortCode(bankInformation.getSortCode());
								bankInformation.setSwiftCode(bankInformation.getSwiftCode());
								bankInformation.setWarehouse(bankInformation.getWarehouse());
								// bankInformation.setShg(bankInformation.getShg());;
								// bankInformation.setFarmer(bankInformation.getFarmer());
								bankInformationSet.add(bankInformation);
								temp.setBankInfo(bankInformationSet);
							}
						}
					}
				}
				locationService.editWarehouse(temp);
				updateDynamicFields("381", String.valueOf(temp.getId()), "", "3");

				/* setUpdateDynamicFieldValues(temp.getId()); */
			}
			request.setAttribute(HEADING, getText(LIST));
			return LIST;
		}
		return super.execute();
	}

	private String bankInformationToJson(Set<BankInformation> bankInformation) {

		Set<BankInformation> bank = new HashSet<BankInformation>();
		for (BankInformation bankInfo : bankInformation) {

			BankInformation info = new BankInformation();
			FarmCatalogue cat = getCatlogueValueByCode(bankInfo.getAccType());
			info.setAccType(cat != null ? cat.getName() : "");
			info.setAccNo(bankInfo.getAccNo());
			info.setBankName(bankInfo.getBankName());
			info.setBranchName(bankInfo.getBranchName());
			info.setSortCode(bankInfo.getSortCode());
			info.setAccTypeCode(bankInfo.getAccType());
			if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
				FarmCatalogue catalogue = catalogueService.findCatalogueByName(bankInfo.getBankName());
				if (!ObjectUtil.isEmpty(catalogue))
					info.setBankNameCode(catalogue.getCode());
				FarmCatalogue catalogueBranch = catalogueService.findCatalogueByName(bankInfo.getBranchName());
				if (!ObjectUtil.isEmpty(catalogueBranch))
					info.setBranchNameCode(catalogueBranch.getCode());
			}
			bank.add(info);

		}
		Set<BankInformation> bankInformation1 = new HashSet<BankInformation>(bank);
		Gson gson = new Gson();
		String res = gson.toJson(bankInformation1);
		return res;
	}

	/* *//**
			 * Detail.
			 * 
			 * @return the string
			 * @throws Exception
			 *             the exception
			 */
	public String detail() throws Exception {

		String view = "";
		if (id != null && !id.equals("")) {
			warehouse = locationService.findSamithiById(Long.valueOf(id));
			if (warehouse == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}

			if (getCurrentTenantId().equalsIgnoreCase("atma")) {
				String code = warehouse.getCode();
				if (!StringUtil.isEmpty(code) && code.length() == 8) {
					String villageCode = code.substring(0, 6);
					Village village = locationService.findVillageByCode(villageCode);
					if (!ObjectUtil.isEmpty(village)) {
						setVillage(village.getName());
						setSelectedPanchayat(!ObjectUtil.isEmpty(village.getGramPanchayat())
								? village.getGramPanchayat().getName() : "");

						setSelectedCity(!ObjectUtil.isEmpty(village.getGramPanchayat())
								&& !ObjectUtil.isEmpty(village.getGramPanchayat().getCity())
										? village.getGramPanchayat().getCity().getName() : "");

						setSelectedLocality(!ObjectUtil.isEmpty(village.getGramPanchayat())
								&& !ObjectUtil.isEmpty(village.getGramPanchayat().getCity())
								&& !ObjectUtil.isEmpty(village.getGramPanchayat().getCity().getLocality())
										? village.getGramPanchayat().getCity().getLocality().getName() : "");

						Map<String, String> sanghamMap = getSanghamList();
						String sanghamVal = warehouse.getSanghamType();
						setSangham(sanghamMap.get(sanghamVal));
					}

				} else {
					Map<String, String> sanghamMap = getSanghamList();
					String sanghamVal = warehouse.getSanghamType();
					setSangham(sanghamMap.get(sanghamVal));

				}
			}
			// String
			// replaceDate=warehouse.getFormationDate().toString().replace(".",
			// "");
			if (warehouse.getFormationDate() != null) {
				String replaceDate = warehouse.getFormationDate().toString().split("\\.", 2)[0];
				Date date = formatter.parse(replaceDate.trim());
				formationDate = DateUtil.convertDateToString(date, DateUtil.TXN_TIME_FORMAT);
			}
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)) {
				ESESystem preferences = preferncesService.findPrefernceById("1");
				if (!ObjectUtil.isEmpty(preferences)) {
					DateFormat genDate = new SimpleDateFormat(
							preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
					if (!ObjectUtil.isEmpty(warehouse.getGroupFormationDate())) {
						groupFormationDate = genDate.format(warehouse.getGroupFormationDate());
					}
				}

				if (!StringUtil.isEmpty(warehouse.getBankInfo())) {
					for (BankInformation bank : warehouse.getBankInfo()) {
						FarmCatalogue catalogue = getCatlogueValueByCode(bank.getAccType());
						if (!StringUtil.isEmpty(catalogue)) {
							bankAccType = catalogue.getName();
							bank.setAccType(bankAccType);
						}
					}

				}
			}
			
			if (getCurrentTenantId().equalsIgnoreCase("kenyafpo")) {
				Village v = locationService.findVillage(warehouse.getVillageId());
				if (!ObjectUtil.isEmpty(v)) {
					
					if (getGramPanchayatEnable().equalsIgnoreCase("1")) {			
						
						if (!ObjectUtil.isEmpty(v.getGramPanchayat())) {
							setSelectedCountry(v.getGramPanchayat().getCity().getLocality().getState().getCountry().getName());
							setSelectedState(v.getGramPanchayat().getCity().getLocality().getState().getName());
							setSelectedLocality(v.getGramPanchayat().getCity().getLocality().getName());
							setSelectedCity(v.getGramPanchayat().getCity().getName());
							setSelectedPanchayat(v.getGramPanchayat().getName());
							setSelectedVillage(v.getName());
						}
					} else {
						
							setSelectedCountry(v.getCity().getLocality().getState().getCountry().getName());
							setSelectedState(v.getCity().getLocality().getState().getName());
							setSelectedLocality(v.getCity().getLocality().getName());
							setSelectedCity(v.getCity().getName());
							setSelectedVillage(v.getName());
					
					}
				}
				
				
				
			}
			setCurrentPage(getCurrentPage());
			command = UPDATE;
			view = DETAIL;
			request.setAttribute(HEADING, getText(DETAIL));
		} else {
			request.setAttribute(HEADING, getText(LIST));
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

		if (this.getId() != null && !(this.getId().equals(EMPTY))) {

			warehouse = locationService.findSamithiById(Long.valueOf(id));
			if (warehouse == null) {
				addActionError(NO_RECORD);
				return list();
			}
			setCurrentPage(getCurrentPage());
			if (!ObjectUtil.isEmpty(warehouse)) {

				selectedVillageMap = new HashMap<String, Object>();
				for (Village village : warehouse.getVillages()) {
					selectedVillageMap.put(village.getCode(), (Object) (village.getName()));
				}

				boolean isAgentMappingExist = agentService.findAgentMappedWithWarehouse(Long.parseLong(this.getId()));
				if (isAgentMappingExist) {
					addActionError(getLocaleProperty("delete.samithiAgent.warn"));
					request.setAttribute(HEADING, getText(DETAIL));
					return DETAIL;
				}

				boolean isFarmerMappingExist = locationService.findFarmerMappedWithSamithi(warehouse.getId());
				if (isFarmerMappingExist) {
					addActionError(getLocaleProperty("delete.samithiFarmer.warn"));
					request.setAttribute(HEADING, getText(DETAIL));
					return DETAIL;
				}
				locationService.removeWarehouse(warehouse);
			}
		}
		request.setAttribute(HEADING, getText(LIST));
		return LIST;

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
		List<Village> avlvillages = new ArrayList<Village>();
		List<Village> selvillages = new ArrayList<Village>();
		String[] splitSelectedVillages = new String[0];
		Boolean iterationFlag = true;
		List<String> selVillageCodes = new ArrayList<String>();
		if (!ObjectUtil.isEmpty(selectedVillagesValue) && selectedVillagesValue.length() > 0) { // to
																								// check
																								// presence
																								// of
																								// selected
																								// village.
			splitSelectedVillages = selectedVillagesValue.split(",");
		}
		if ((id != null && !id.equals("") && (Long.valueOf(id) > 0))) {
			// Get Samithi Villages
			warehouse = locationService.findSamithiById(Long.valueOf(id));
			if (warehouse == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			if (StringUtil.isEmpty(errorMsg)) {
				selvillages = new ArrayList<Village>(warehouse.getVillages());
			} else {
				selvillages = new ArrayList<Village>();
			}
		} else {
			warehouse = locationService.findSamithiById(Long.valueOf(samithiId));

			if (StringUtil.isEmpty(errorMsg) && !ObjectUtil.isEmpty(warehouse)) {
				selvillages = new ArrayList<Village>(warehouse.getVillages());
				editFlag = true;

				if (!StringUtil.isEmpty(selectedMunipalIds) && !selectedMunipalIds.equalsIgnoreCase("null")) {

					String[] spiltMuniciplity = selectedMunipalIds.split(",");

					List<Village> listVillages = new ArrayList<>();
					for (int i = 0; i < spiltMuniciplity.length; i++) {
						listVillages = locationService.listVillageByCity(Long.valueOf(spiltMuniciplity[i]));

						if (!ObjectUtil.isListEmpty(listVillages)) {
							/*****
							 * if(!ObjectUtil.isListEmpty(selvillages)){ //check
							 * list of selvillages. for(Village selVillage :
							 * selvillages){ //loop through selected villages.
							 * selVillageCodes.add(selVillage.getCode()); } }
							 */

							for (Village village : listVillages) {
								/*****
								 * if(!selVillageCodes.contains(village.getCode())){
								 * avlvillages.add(village); }
								 */
								avlvillages.add(village);
							}
						}
					}

					// avlvillages = new
					// ArrayList<Village>(warehouse.getVillages());
					sendResponse(buildOptionByVillageObj(avlvillages, selvillages));
				}
			} else {

				if (!StringUtil.isEmpty(selectedMunipalIds) && !selectedMunipalIds.equalsIgnoreCase("null")) {

					String[] spiltMuniciplity = selectedMunipalIds.split(",");
					editFlag = false;
					List<Village> listVillages = new ArrayList<>();
					for (int i = 0; i < spiltMuniciplity.length; i++) {
						listVillages = locationService.listVillageByCity(Long.valueOf(spiltMuniciplity[i]));

						if (!ObjectUtil.isListEmpty(listVillages)) {
							if (splitSelectedVillages.length > 0) { // to check
																	// presence
																	// of
																	// selected
																	// villages.

								for (Village village : listVillages) {
									iterationFlag = true;
									for (String selectedVillage : splitSelectedVillages) {
										if (!village.getCode().equals(selectedVillage) && iterationFlag) { // check
																											// if
																											// selected
																											// village
																											// and
																											// village
																											// posted
																											// codes,
																											// !=
																											// then
																											// add.
											iterationFlag = true;
										} else {
											iterationFlag = false;
										}
									}
									if (iterationFlag) {
										avlvillages.add(village); // add
																	// available
																	// village.
									} else {
										selvillages.add(village); // add
																	// selected
																	// village.
									}
								}
							}

							else { // else if selected villages not present.
								for (Village village : listVillages) {
									avlvillages.add(village);
								}
							}
						}
					}

					// avlvillages = new
					// ArrayList<Village>(warehouse.getVillages());
					sendResponse(buildOptionByVillageObj(avlvillages, selvillages));
				}
			}
		}

		/*
		 * if (id != null && !id.equals("") && (Long.valueOf(id) > 0)) { // Get
		 * Samithi Villages warehouse =
		 * locationService.findSamithiById(Long.valueOf(id)); if (warehouse ==
		 * null) { addActionError(NO_RECORD); return REDIRECT; } if
		 * (StringUtil.isEmpty(errorMsg)) { selvillages = new
		 * ArrayList<Village>(warehouse.getVillages()); } else { selvillages =
		 * new ArrayList<Village>(); }
		 * 
		 * }
		 */

		return null;
	}

	/**
	 * Builds the option by village obj.
	 * 
	 * @param avlVillages
	 *            the avl villages
	 * @param selVillages
	 *            the sel villages
	 * @return the string
	 */
	public String buildOptionByVillageObj(List<Village> avlVillages, List<Village> selVillages) {

		StringBuffer villagePickList = new StringBuffer();
		StringBuffer avlVillageOptions = new StringBuffer();
		StringBuffer selVillageOptions = new StringBuffer();
		String listString = null;

		Map<Long, Long> availableVillagesMap = new LinkedHashMap<Long, Long>();
		Map<Long, Long> selectedVillagesMap = new LinkedHashMap<Long, Long>();

		for (Village village : avlVillages) {
			availableVillagesMap.put(village.getId(), village.getId());
		}

		for (Village village : selVillages) {
			selectedVillagesMap.put(village.getId(), village.getId());
		}

		for (Village village : avlVillages) {
			listString = null;
			if (selectedVillagesMap.containsKey(village.getId())) {
				listString = PATTERN_OPTION.replaceAll(OPTION_KEY, village.getCode()).replaceAll(OPTION_VALUE,
						village.getName());
				selVillageOptions.append(listString);
			} else {

				listString = PATTERN_OPTION.replaceAll(OPTION_KEY, village.getCode()).replaceAll(OPTION_VALUE,
						village.getName());
				avlVillageOptions.append(listString);
			}
		}
		if (!editFlag) {
			for (Village village : selVillages) { // to append selected village
													// value.
				listString = null;
				/*
				 * if (selectedVillagesMap.containsKey(village.getId())) {
				 * listString = PATTERN_OPTION.replaceAll(OPTION_KEY,
				 * village.getCode()).replaceAll( OPTION_VALUE,
				 * village.getName()); selVillageOptions.append(listString); }
				 * else {
				 */

				listString = PATTERN_OPTION.replaceAll(OPTION_KEY, village.getCode()).replaceAll(OPTION_VALUE,
						village.getName());
				selVillageOptions.append(listString);

				// }
			}
		}

		villagePickList.append(avlVillageOptions.toString());
		villagePickList.append("~");
		villagePickList.append(selVillageOptions.toString());

		return villagePickList.toString();

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
	 * Gets the selected cooperative.
	 * 
	 * @return the selected cooperative
	 */
	public String getselectedCooperative() {

		return selectedCooperative;
	}

	/**
	 * Sets the selected cooperative.
	 * 
	 * @param selectedCooperative
	 *            the new selected cooperative
	 */
	public void setselectedCooperative(String selectedCooperative) {

		this.selectedCooperative = selectedCooperative;
	}

	/**
	 * Gets the cooperatives.
	 * 
	 * @return the cooperatives
	 */
	public List<Warehouse> getCooperatives() {

		cooperatives = locationService.listOfCooperatives();
		return cooperatives;
	}

	/**
	 * Sets the cooperatives.
	 * 
	 * @param cooperatives
	 *            the new cooperatives
	 */
	public void setCooperatives(List<Warehouse> cooperatives) {

		this.cooperatives = cooperatives;
	}

	/**
	 * Gets the error msg.
	 * 
	 * @return the error msg
	 */
	public String getErrorMsg() {

		return errorMsg;
	}

	/**
	 * Sets the error msg.
	 * 
	 * @param errorMsg
	 *            the new error msg
	 */
	public void setErrorMsg(String errorMsg) {

		this.errorMsg = errorMsg;
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
	 * Gets the farmer mapping exist.
	 * 
	 * @return the farmer mapping exist
	 */
	public String getFarmerMappingExist() {

		return farmerMappingExist;
	}

	/**
	 * Sets the farmer mapping exist.
	 * 
	 * @param farmerMappingExist
	 *            the new farmer mapping exist
	 */
	public void setFarmerMappingExist(String farmerMappingExist) {

		this.farmerMappingExist = farmerMappingExist;
	}

	/**
	 * Gets the agent mapping exist.
	 * 
	 * @return the agent mapping exist
	 */
	public String getAgentMappingExist() {

		return agentMappingExist;
	}

	/**
	 * Sets the agent mapping exist.
	 * 
	 * @param agentMappingExist
	 *            the new agent mapping exist
	 */
	public void setAgentMappingExist(String agentMappingExist) {

		this.agentMappingExist = agentMappingExist;
	}

	/**
	 * Gets the selected village codes.
	 * 
	 * @return the selected village codes
	 */
	public String getSelectedVillageCodes() {

		return selectedVillageCodes;
	}

	/**
	 * Sets the selected village codes.
	 * 
	 * @param selectedVillageCodes
	 *            the new selected village codes
	 */
	public void setSelectedVillageCodes(String selectedVillageCodes) {

		this.selectedVillageCodes = selectedVillageCodes;
	}

	/**
	 * Gets the village codes list.
	 * 
	 * @return the village codes list
	 */
	public List<Village> getVillageCodesList() {

		return villageCodesList;
	}

	/**
	 * Sets the village codes list.
	 * 
	 * @param villageCodesList
	 *            the new village codes list
	 */
	public void setVillageCodesList(List<Village> villageCodesList) {

		this.villageCodesList = villageCodesList;
	}

	/**
	 * Gets the male farmers.
	 * 
	 * @return the male farmers
	 */
	public String getMaleFarmers() {

		return maleFarmers;
	}

	/**
	 * Sets the male farmers.
	 * 
	 * @param maleFarmers
	 *            the new male farmers
	 */
	public void setMaleFarmers(String maleFarmers) {

		this.maleFarmers = maleFarmers;
	}

	/**
	 * Gets the fe male farmers.
	 * 
	 * @return the fe male farmers
	 */
	public String getFeMaleFarmers() {

		return feMaleFarmers;
	}

	/**
	 * Sets the fe male farmers.
	 * 
	 * @param feMaleFarmers
	 *            the new fe male farmers
	 */
	public void setFeMaleFarmers(String feMaleFarmers) {

		this.feMaleFarmers = feMaleFarmers;
	}

	/**
	 * Gets the field staff names.
	 * 
	 * @return the field staff names
	 */
	public String getFieldStaffNames() {

		return fieldStaffNames;
	}

	/**
	 * Sets the field staff names.
	 * 
	 * @param fieldStaffNames
	 *            the new field staff names
	 */
	public void setFieldStaffNames(String fieldStaffNames) {

		this.fieldStaffNames = fieldStaffNames;
	}

	/**
	 * Gets the municipality list.
	 * 
	 * @return the municipality list
	 */
	public Map<Long, String> getMunicipalityList() {
		List<Municipality> listOfMunicipality = locationService.listCity();
		if (!ObjectUtil.isListEmpty(listOfMunicipality)) {
			for (Municipality municipality : listOfMunicipality) {
				municipalityList.put(municipality.getId(), municipality.getName());
			}
		}
		return municipalityList;
	}

	/**
	 * Set municipality list.
	 * 
	 * @param municipalityList
	 */
	public void setMunicipalityList(Map<Long, String> municipalityList) {

		this.municipalityList = municipalityList;
	}

	public Map<String, String> getSanghamList() {
		AtomicInteger i = new AtomicInteger(0);
		Map<String, String> sanghamList = new LinkedHashMap<>();
		FarmCatalogueMaster farmCatalougeMaster = catalogueService.findFarmCatalogueMasterByName(getText("sangham"));
		if (!ObjectUtil.isEmpty(farmCatalougeMaster)) {
			Double d = new Double(farmCatalougeMaster.getId());
			List<FarmCatalogue> farmCatalougeList = catalogueService.findFarmCatalougeByType(d.intValue());
			sanghamList = farmCatalougeList.stream().collect(Collectors.toMap(
					obj -> (StringUtil.getExact((String.valueOf(i.incrementAndGet())), 2)), FarmCatalogue::getName));
		}
		return sanghamList;
	}

	public void setIdGenerator(IUniqueIDGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	public String getSelectedMunipalIds() {
		return selectedMunipalIds;
	}

	public void setSelectedMunipalIds(String selectedMunipalIds) {
		this.selectedMunipalIds = selectedMunipalIds;
	}

	public String getSelectedMunicipalityId() {
		return selectedMunicipalityId;
	}

	public void setSelectedMunicipalityId(String selectedMunicipalityId) {
		this.selectedMunicipalityId = selectedMunicipalityId;
	}

	public List<String> getSelectedCitys() {
		return selectedCitys;
	}

	public void setSelectedCitys(List<String> selectedCitys) {
		this.selectedCitys = selectedCitys;
	}

	public String getMunicipalityIds() {
		return municipalityIds;
	}

	public void setMunicipalityIds(String municipalityIds) {
		this.municipalityIds = municipalityIds;
	}

	public Map<String, Object> getSelectedCityMap() {
		return selectedCityMap;
	}

	public void setSelectedCityMap(Map<String, Object> selectedCityMap) {
		this.selectedCityMap = selectedCityMap;
	}

	public String getSamithiId() {
		return samithiId;
	}

	public void setSamithiId(String samithiId) {
		this.samithiId = samithiId;
	}

	public String getSelectedCityids() {
		return selectedCityids;
	}

	public void setSelectedCityids(String selectedCityids) {
		this.selectedCityids = selectedCityids;
	}

	public String getSelectedVillagesValue() {

		return selectedVillagesValue;
	}

	public void setSelectedVillagesValue(String selectedVillagesValue) {

		this.selectedVillagesValue = selectedVillagesValue;
	}

	public Boolean getEditFlag() {

		return editFlag;
	}

	public void setEditFlag(Boolean editFlag) {

		this.editFlag = editFlag;
	}

	public ICatalogueService getCatalogueService() {
		return catalogueService;
	}

	public void setCatalogueService(ICatalogueService catalogueService) {
		this.catalogueService = catalogueService;
	}

	public Map<String, String> getLocalitiesList() {
		Map<String, String> localitiesMap = new LinkedHashMap<>();
		List<Object[]> localityList = locationService.listLocalityIdCodeAndName();
		if (!ObjectUtil.isListEmpty(localityList)) {
			localitiesMap = localityList.stream()
					.collect(Collectors.toMap(obj -> (String.valueOf(obj[0])), obj -> (String.valueOf(obj[2]))));

			localitiesMap = localitiesMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(
					Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		}
		return localitiesMap;
	}

	public Map<String, String> getCitiesList() {
		Map<String, String> citiesMap = new LinkedHashMap<>();
		if (!StringUtil.isEmpty(selectedLocality)) {
			List<Object[]> cityList = locationService.listCityCodeAndNameByDistrictId(Long.parseLong(selectedLocality));
			citiesMap = cityList.stream()
					.collect(Collectors.toMap(obj -> (String.valueOf(obj[0])), obj -> (String.valueOf(obj[2]))));
		}
		return citiesMap;
	}

	public Map<String, String> getGramPanchayatList() {
		Map<String, String> panchayatMap = new LinkedHashMap<>();
		if (!StringUtil.isEmpty(selectedCity)) {
			List<Object[]> gramPanchayatList = locationService.listGramPanchayatByCityId(Long.parseLong(selectedCity));
			panchayatMap = gramPanchayatList.stream()
					.collect(Collectors.toMap(obj -> (String.valueOf(obj[0])), obj -> (String.valueOf(obj[2]))));
		}
		return panchayatMap;
	}

	public Map<String, String> getVillageList() {
		Map<String, String> villagetMap = new LinkedHashMap<>();
		if (!StringUtil.isEmpty(selectedPanchayat)) {
			List<Object[]> villagetList = locationService.listVillageByPanchayatId(Long.parseLong(selectedPanchayat));
			villagetMap = villagetList.stream()
					.collect(Collectors.toMap(obj -> (String.valueOf(obj[1])), obj -> (String.valueOf(obj[2]))));
		}
		return villagetMap;
	}

	@SuppressWarnings("unchecked")
	public void populateCity() {
		JSONArray cityArr = new JSONArray();
		if (!StringUtil.isEmpty(selectedLocality)) {
			List<Object[]> cityList = locationService.listCityCodeAndNameByDistrictId(Long.parseLong(selectedLocality));
			cityList.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
				cityArr.add(getJSONObject(obj[0].toString(), obj[2].toString()));
			});
		}
		sendAjaxResponse(cityArr);
	}

	@SuppressWarnings("unchecked")
	public void populatePanchayath() {
		JSONArray panchayatArr = new JSONArray();
		if (!StringUtil.isEmpty(selectedCity)) {
			List<Object[]> gramPanchayatList = locationService.listGramPanchayatByCityId(Long.parseLong(selectedCity));
			gramPanchayatList.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
				panchayatArr.add(getJSONObject(obj[0].toString(), obj[2].toString()));
			});
		}
		sendAjaxResponse(panchayatArr);
	}

	@SuppressWarnings("unchecked")
	public void populateVillage() {
		JSONArray villageArr = new JSONArray();
		if (!StringUtil.isEmpty(selectedPanchayat)) {
			List<Object[]> villageList = locationService.listVillageByPanchayatId(Long.parseLong(selectedPanchayat));
			villageList.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
				villageArr.add(getJSONObject(obj[1].toString(), obj[2].toString()));
			});
		}
		sendAjaxResponse(villageArr);
	}

	public String getSelectedCooperative() {
		return selectedCooperative;
	}

	public void setSelectedCooperative(String selectedCooperative) {
		this.selectedCooperative = selectedCooperative;
	}

	public String getSelectedLocality() {
		return selectedLocality;
	}

	public void setSelectedLocality(String selectedLocality) {
		this.selectedLocality = selectedLocality;
	}

	public String getSelectedCity() {
		return selectedCity;
	}

	public void setSelectedCity(String selectedCity) {
		this.selectedCity = selectedCity;
	}

	public String getSelectedPanchayat() {
		return selectedPanchayat;
	}

	public void setSelectedPanchayat(String selectedPanchayat) {
		this.selectedPanchayat = selectedPanchayat;
	}

	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}

	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}

	public String getVillage() {
		return village;
	}

	public void setVillage(String village) {
		this.village = village;
	}

	public String getSangham() {
		return sangham;
	}

	public void setSangham(String sangham) {
		this.sangham = sangham;
	}

	public String getFormationDate() {
		return formationDate;
	}

	public void setFormationDate(String formationDate) {
		this.formationDate = formationDate;
	}

	public Map<String, String> getMTypeList() {
		Map<String, String> mList = new LinkedHashMap<>();
		Arrays.asList(getLocaleProperty("groupMasterTypeList").split(",")).stream().forEach(master -> {
			String[] masterSplit = master.split("~");
			if (!mList.containsKey(masterSplit[0])) {
				mList.put(masterSplit[0], masterSplit[1]);
			}
		});
		return mList;
	}

	public Map<String, String> getAccountTypeList() {

		Map<String, String> bankList = new LinkedHashMap<>();

		bankList = getFarmCatalougeMap(Integer.valueOf(getText("bankInformation")));
		return bankList;
	}

	public Map<String, String> getBankNameList() {

		Map<String, String> branchList = new LinkedHashMap<>();

		branchList = getFarmCatalougeMap(Integer.valueOf(getText("bankName")));
		return branchList;
	}

	public Map<String, String> getBranchNameList() {

		Map<String, String> branchNames = new LinkedHashMap<>();

		branchNames = getFarmCatalougeMap(Integer.valueOf(getText("branchName")));
		return branchNames;
	}

	public String getGroupFormationDate() {
		return groupFormationDate;
	}

	public void setGroupFormationDate(String groupFormationDate) {
		this.groupFormationDate = groupFormationDate;
	}

	public BankInformation getBankInfo() {
		return bankInfo;
	}

	public void setBankInfo(BankInformation bankInfo) {
		this.bankInfo = bankInfo;
	}

	public String getBankAccType() {
		return bankAccType;
	}

	public void setBankAccType(String bankAccType) {
		this.bankAccType = bankAccType;
	}

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}
	
	public Map<Long, String> getFarmerCount() {

		if (farmerCount.size() == 0) {
			getCount();
		}
		return farmerCount;
	}
	
	public Map<Long, String> getCropArea() {

		if (cropArea.size() == 0) {
			getCount();
		}
		return cropArea;
	}
	
	protected void getCount() {

		farmerCount = new LinkedHashMap<Long, String>();
		cropArea = new LinkedHashMap<Long, String>();
		List<Object[]> aa = agentService.findFarmerCountBySamithiId();
		aa.stream().forEach(u ->{
			farmerCount.put(Long.valueOf(u[0].toString()),u[1].toString());
			cropArea.put(Long.valueOf(u[0].toString()),u[2].toString());
		});
		
	}
	
	@SuppressWarnings("unchecked")
	public String farmerData() throws Exception {

		
		Warehouse filter = new Warehouse();

		
		if (!StringUtil.isEmpty(samithiId)) {
			
			filter.setId(Long.valueOf(samithiId));
		}

		
		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		return sendFarmerDataJQGridJSONResponse(data);
	}

	public String getCoOperativeId() {
		return coOperativeId;
	}

	public void setCoOperativeId(String coOperativeId) {
		this.coOperativeId = coOperativeId;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected String sendFarmerDataJQGridJSONResponse(Map data) throws Exception {

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
				rows.add(toFarmerDataJSON(record));
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
	
	@SuppressWarnings("unchecked")
	public JSONObject toFarmerDataJSON(Object obj) {

		Warehouse warehouse = (Warehouse) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(warehouse.getBranchId())))
						? getBranchesMap().get(getParentBranchMap().get(warehouse.getBranchId()))
						: getBranchesMap().get(warehouse.getBranchId()));
			}
			rows.add(getBranchesMap().get(warehouse.getBranchId()));

		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(branchesMap.get(warehouse.getBranchId()));
			}
		}
		if (getCurrentTenantId().equalsIgnoreCase("welspun")) {
			rows.add(
					"<font color=\"#0000FF\" style=\"cursor:pointer;\">" + warehouse.getCapacityInTonnes() + "</font>");
		} else {
			rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">" + warehouse.getCode() + "</font>");
		}
		rows.add(warehouse.getName());

		
		
		jsonObject.put("id", warehouse.getId());
		jsonObject.put("cell", rows);

		return jsonObject;

	}
	public Map<String, String> getLocalities() {
		Map<String, String> locality = new LinkedHashMap<String, String>();
		String bId = StringUtil.isEmpty(getBranchId())
				? ((StringUtil.isEmpty(getBranchId_F()) || getBranchId_F().equals("-1")) ? null : getBranchId_F())
				: getBranchId();
		if(!StringUtil.isEmpty(bId)&& bId!=null && !"-1".equalsIgnoreCase(bId)){
			/*dis  = locationService.findLocalityByBranch(bId);
			if(!ObjectUtil.isEmpty(dis)){
			locality.put(dis.getCode(),dis.getCode()+"-"+dis.getName());
			}*/
			locationService.findLocalityByBranch(bId).stream().forEach(cities -> {
				Object[] cityArr = (Object[]) cities;
				locality.put(String.valueOf(cityArr[1]), String.valueOf(cityArr[1])+"-"+String.valueOf(cityArr[2]));
			});	
			
		}else{
			locationService.listLocalityIdCodeAndName().stream().forEach(cities -> {
				Object[] cityArr = (Object[]) cities;
				locality.put(String.valueOf(cityArr[1]), String.valueOf(cityArr[1])+"-"+String.valueOf(cityArr[2]));
			});	
		}
		
		return locality;
	}
	
	
	public String getBranchId_F() {
		return branchId_F;
	}

	public void setBranchId_F(String branchId_F) {
		this.branchId_F = branchId_F;
	}
	public Map<String, String> getCountries() {

		Map<String, String> countryMap = new LinkedHashMap<String, String>();
		List<Country> countryList = locationService.listCountries();
		for (Country obj : countryList) {
			countryMap.put(obj.getName(), obj.getCode() + "-" + obj.getName());
		}
		return countryMap;
	}
	
	public Map<Long, String> getStates() {

		Map<Long, String> stateMap = new LinkedHashMap<Long, String>();
		if (!StringUtil.isEmpty(selectedCountry)) {
			states = locationService.listStates(selectedCountry);
		}
		if (!ObjectUtil.isEmpty(states)) {
			for (State state : states) {
				stateMap.put(state.getId(), state.getCode() + "-" + state.getName());
			}

		}

		return stateMap;

	}

	public String getSelectedCountry() {
		return selectedCountry;
	}

	public void setSelectedCountry(String selectedCountry) {
		this.selectedCountry = selectedCountry;
	}

	public String getSelectedState() {
		return selectedState;
	}

	public void setSelectedState(String selectedState) {
		this.selectedState = selectedState;
	}

	public void setLocalities(List<Locality> localities) {
		this.localities = localities;
	}

	public void setCities(List<Municipality> cities) {
		this.cities = cities;
	}

	public void setStates(List<State> states) {
		this.states = states;
	}
	
	public Map<Long, String> getListLocalities() {
		Map<Long, String> districtMap = new LinkedHashMap<Long, String>();
		if (!StringUtil.isEmpty(selectedState) && StringUtil.isInteger(selectedState)) {
			listLocalities = locationService.findLocalityByStateId(Long.valueOf(selectedState));
		}
		if (!ObjectUtil.isEmpty(listLocalities)) {
			for (Locality locality : listLocalities) {
				districtMap.put(locality.getId(), locality.getCode() + "-" + locality.getName());
			}
		}

		return districtMap;
	}

	public void setListLocalities(List<Locality> listLocalities) {
		this.listLocalities = listLocalities;
	}
	public Map<Long, String> getVillages() {
		Map<Long, String> villageMap = new LinkedHashMap<Long, String>();
		if (!StringUtil.isEmpty(selectedCity)) {
			villages = locationService.listVillagesByCityId(Long.valueOf(selectedCity));
		}
		if (!ObjectUtil.isEmpty(villages)) {
			for (Village village : villages) {
				villageMap.put(village.getId(), village.getCode() + "-" + village.getName());
			}
		}
		return villageMap;
	}
	public Map<Long, String> getCities() {
		Map<Long, String> cityMap = new LinkedHashMap<Long, String>();
		if (!StringUtil.isEmpty(selectedLocality)) {
			cities = locationService.listMunicipalitiesByLocalityId(Long.parseLong(selectedLocality));
		}
		if (!ObjectUtil.isEmpty(cities)) {
			for (Municipality municipality : cities) {
				cityMap.put(municipality.getId(), municipality.getCode() + "-" + municipality.getName());
			}
		}
		return cityMap;
	}

    public Map<String, String> getSeasonList() {

		Map<String, String> seasonMap = new LinkedHashMap<String, String>();

		List<Object[]> seasonList = farmerService.listSeasonCodeAndName();

		for (Object[] obj : seasonList) {
			seasonMap.put(String.valueOf(obj[0]), String.valueOf(obj[1]));
		}
		return seasonMap;

	}
    
	public Map<Integer, String> getIcsStatusList() {

		String[] icsStatus = getLocaleProperty("icsStatusList").split(",");
		for (int i = 0; i < icsStatus.length; i++)
			icsStatusList.put(i, icsStatus[i]);

		return icsStatusList;
	}

	public Map<Long, String> getPanchayath() {
		Map<Long, String> gramPanchayatMap = new LinkedHashMap<Long, String>();
		if (!StringUtil.isEmpty(selectedCity)) {
			panchayat = locationService.listGramPanchayatsByCityId(Long.valueOf(selectedCity));
		}
		if (!ObjectUtil.isEmpty(panchayat)) {
			for (GramPanchayat gramPanchayat : panchayat) {
				gramPanchayatMap.put(gramPanchayat.getId(), gramPanchayat.getCode() + "-" + gramPanchayat.getName());
			}
		}
		return gramPanchayatMap;
	}
	
	

	public String getGramPanchayatEnable() {
		return gramPanchayatEnable;
	}

	public void setGramPanchayatEnable(String gramPanchayatEnable) {
		this.gramPanchayatEnable = gramPanchayatEnable;
	}

	public List<GramPanchayat> getPanchayat() {
		return panchayat;
	}

	public void setPanchayat(List<GramPanchayat> panchayat) {
		this.panchayat = panchayat;
	}

	public void setVillages(List<Village> villages) {
		this.villages = villages;
	}

	public String getSelectedVillage() {
		return selectedVillage;
	}

	public void setSelectedVillage(String selectedVillage) {
		this.selectedVillage = selectedVillage;
	}

	
}
