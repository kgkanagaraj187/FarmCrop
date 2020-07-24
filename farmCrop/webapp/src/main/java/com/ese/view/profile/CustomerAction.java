/*
 * ClientAction.java
 * Copyright (c) 2012-2013, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

package com.ese.view.profile;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.profile.CropSupply;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.ColdStorage;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.ColdStorageStockTransfer;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Country;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Locality;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.State;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

/**
 * The Class CustomerAction.
 */
@SuppressWarnings("serial")
public class CustomerAction extends SwitchValidatorAction {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CustomerAction.class);

	protected static final String CREATE = "create";
	protected static final String DETAIL = "detail";
	protected static final String UPDATE = "update";
	protected static final String MAPPING = "mapping";
	protected static final String DELETE = "delete";
	protected static final String LIST = "list";
	protected static final String TITLE_PREFIX = "title.";
	protected static final String HEADING = "heading";

	public static final String COUNTRY_ID_SEQ = "COUNTRY_ID_SEQ";
	public static final String STATE_ID_SEQ = "STATE_ID_SEQ";
	public static final String DISTRICT_ID_SEQ = "DISTRICT_ID_SEQ";
	public static final String MANDAL_ID_SEQ = "MANDAL_ID_SEQ";
	public static final String VILLAGE_ID_SEQ = "VILLAGE_ID_SEQ";

	public static final String DEVICE_ID_SEQ = "DEVICE_ID_SEQ";
	public static final String GROUP_ID_SEQ = "GROUP_ID_SEQ";
	public static final String WAREHOUSE_ID_SEQ = "WAREHOUSE_ID_SEQ";

	public static final String SUBCATEGORY_ID_SEQ = "SUBCATEGORY_ID_SEQ";

	public static final String PRODUCT_ID_SEQ = "PRODUCT_ID_SEQ";

	private IClientService clientService;
	private IUniqueIDGenerator idGenerator;

	private Customer customer;
	private Customer filter;
	private String id;
	private String selectedHoldingType;

	private Map<String, String> holdingTypeList = new LinkedHashMap<String, String>();
	private String tabIndex = "#tabs-1";
	private IAccountService accountService;
	@Autowired
	private IProductService productService;
	@Autowired
	private ILocationService locationService;
	private Map<String, String> listOfCustomerTypes;
	private Map<String, String> listOfCustomerSegment;
	List<Locality> localities = new ArrayList<Locality>();
	List<Municipality> municipalities = new ArrayList<Municipality>();
	List<State> states = new ArrayList<State>();
	List<Country> countries=new ArrayList<Country>();
	private String selectedCountry;
	private String selectedState;
	private Municipality municipality;
	private String selectedDistrict;
    private String selectedCountryCode;
	private String selectedBranch;
	private String selectedStateCode;

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

		Map<String, String> searchRecord = getJQGridRequestParam(); // get the
																	// search
																	// parameter
																	// with

		Customer filter = new Customer();

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

		if (!StringUtil.isEmpty(searchRecord.get("customerId"))) {
			filter.setCustomerId(searchRecord.get("customerId").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("customerName"))) {
			filter.setCustomerName(searchRecord.get("customerName").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("personName"))) {
			filter.setPersonName(searchRecord.get("personName").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("email"))) {
			filter.setEmail(searchRecord.get("email").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("mobileNumber"))) {
			filter.setMobileNumber(searchRecord.get("mobileNumber").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("customerAddress"))) {
			filter.setCustomerAddress(searchRecord.get("customerAddress").trim());
		}

		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		return sendJQGridJSONResponse(data);
	}

	/**
	 * Detail.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String detail() throws Exception {

		String view = "";
		if (id != null && !id.equals("")) {
			customer = clientService.findCustomer(Long.valueOf(id));
			if (customer == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			setCurrentPage(getCurrentPage());
			// String customerTypes=getText("listOfCustomerTypes");
			if (!StringUtil.isEmpty(customer.getCustomerType()) && Integer.valueOf(customer.getCustomerType()) > -1) {
				// String[] customers=customerTypes.split(",");
				customer.setCustomerType((getListOfCustomerTypes().get(customer.getCustomerType())));
			} else {
				customer.setCustomerType("");
			}
			if (!StringUtil.isEmpty(customer.getCustomerSegment())
					&& Integer.valueOf(customer.getCustomerSegment()) > -1) {
				String customerSegments = getText("customerSegments");

				String[] customerSegment = customerSegments.split(",");
				customer.setCustomerSegment((getListOfCustomerSegment().get(customer.getCustomerSegment())));
			} else {
				customer.setCustomerSegment("");
			}
			if (customer.getMunicipality() != null) {
				customer.getMunicipality().setName(customer.getMunicipality().getName());
			}
			command = UPDATE;
			if (customer.getCity() != null) {
				setSelectedCountry(customer.getCity().getState().getCountry().getName());
				setSelectedState(customer.getCity().getState().getName());
			}
			view = DETAIL;
			request.setAttribute(HEADING, getText("buyerdetail"));
		} else {
			request.setAttribute(HEADING, getText("buyerlist"));
			return LIST;
		}
		return view;
	}

	/**
	 * Creates the.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String create() throws Exception {

		if (customer == null) {
			command = "create";
			request.setAttribute(HEADING, getText("buyercreate"));
			return INPUT;
		} else {
			if (customer.getCity() != null) {
				Locality locality = locationService.findLocalityByCode(customer.getCity().getName());
				customer.setCity(locality);
			}
			if (customer.getMunicipality() != null) {
				Municipality municipality = locationService
						.findMunicipalityByCode(customer.getMunicipality().getName());
				customer.setMunicipality(municipality);
			}
			String customerIdSeq = idGenerator.createCustomerId();
			customer.setCustomerId(customerIdSeq);
			customer.setBranchId(getBranchId());
			clientService.addCustomer(customer);
			String accountNo = idGenerator.createCustomerAccountNoSequence(customerIdSeq);
			accountService.createAccount(customerIdSeq, accountNo, new Date(), ESEAccount.CLIENT_ACCOUNT,customer.getBranchId());
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
			customer = clientService.findCustomer(Long.valueOf(id));
			if (customer == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			setCurrentPage(getCurrentPage());
			if (customer.getCity() != null) {
				setSelectedCountry(customer.getCity().getState().getCountry().getCode());
				setSelectedState(customer.getCity().getState().getCode());
			}
			if (customer.getCity() != null) {
				Locality locality = locationService.findLocalityByCode(customer.getCity().getCode());
				customer.getCity().setName(locality.getCode());
			}
					
			if (customer.getMunicipality() != null) {
				Municipality municipality = locationService
						.findMunicipalityByCode(customer.getMunicipality().getCode());
				customer.getMunicipality().setName(municipality.getCode());
			}
			id = null;
			command = UPDATE;
			request.setAttribute(HEADING, getText("buyerupdate"));
		} else {
			if (customer != null) {
				Customer tempCustomer = clientService.findCustomer(Long.valueOf(customer.getId()));
				if (tempCustomer == null) {
					addActionError(NO_RECORD);
					return REDIRECT;
				}
				setCurrentPage(getCurrentPage());
				if (customer.getCity() != null) {
					Locality locality = locationService.findLocalityByCode(customer.getCity().getName());
					tempCustomer.setCity(locality);
				}
				if (customer.getMunicipality() != null) {
					Municipality municipality = locationService
							.findMunicipalityByCode(customer.getMunicipality().getName());
					tempCustomer.setMunicipality(municipality);
				}
				tempCustomer.setCustomerName(customer.getCustomerName());
				tempCustomer.setCustomerAddress(customer.getCustomerAddress());
				tempCustomer.setEmail(customer.getEmail());
				tempCustomer.setPersonName(customer.getPersonName());
				tempCustomer.setLandLine(customer.getLandLine());
				tempCustomer.setMobileNumber(customer.getMobileNumber());
				tempCustomer.setCustomerType(customer.getCustomerType());
				tempCustomer.setCustomerSegment(customer.getCustomerSegment());
				clientService.editCustomer(tempCustomer);
			}
			request.setAttribute(HEADING, getText(LIST));
			return LIST;
		}
		return super.execute();
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
			customer = clientService.findCustomer(Long.valueOf(getId()));
			if (customer == null) {
				addActionError(NO_RECORD);
				return null;
			}
			if (!ObjectUtil.isEmpty(customer)) {

				boolean isCustomerMappedWithFS = false;/*
														 * clientService
														 * .isCustomerWithMappedFieldStaff
														 * (customer.getId());
														 */
				List<ColdStorageStockTransfer> cst = productService.findColdStorageStockTransferByCustomerId(customer.getId());
				if (cst.size() > 0) {
					addActionError(getText("buyercannotDeleteCustomerMappedColdStorageStockTransfer"));
					request.setAttribute(HEADING, getText(DETAIL));
					detail();
					return DETAIL;
				}
				
				List<CropSupply> cs = productService.findCropSupplyByCustomerId(customer.getId());
				if (cs.size() > 0) {
					addActionError(getText("buyercannotDeleteCustomerMappedCropSupply"));
					request.setAttribute(HEADING, getText(DETAIL));
					detail();
					return DETAIL;
				}

				if (customer.getCustomerProjects().size() > 0) {
					addActionError(getText("buyercannotDeleteCustomerMappedProject"));
					request.setAttribute(HEADING, getText(DETAIL));
					
					return DETAIL;
				} else if (isCustomerMappedWithFS) {
					addActionError(getText("buyercannotDeleteCustomerFS"));
					request.setAttribute(HEADING, getText(DETAIL));
				
					return DETAIL;
				} else {
					ESEAccount account = accountService.findAccountByProfileIdAndProfileType(customer.getCustomerId(),ESEAccount.CLIENT_ACCOUNT);
					if (!StringUtil.isEmpty(account)) {
			           AgroTransaction accountTransaction = accountService.findEseAccountByTransaction(account.getId());
				   if (accountTransaction != null) {
    						addActionError(getText("buyercannotDeleteCustomerMappedTransaction"));
    						request.setAttribute(HEADING, getText(DETAIL));
    						
    						return DETAIL;
    					} else {
    						if (account != null && !(ObjectUtil.isEmpty(account))) {
    							accountService.removeAccountByProfileIdAndProfileType(account.getProfileId(), account.getType());
    						}
    						clientService.removeCustomer(customer);
    					}
					}
					else{
					    clientService.removeCustomer(customer);
					}
				}
			}
		}
		request.setAttribute(HEADING, getText(LIST));

		return LIST;

	}

	/**
	 * To json.
	 * 
	 * @param record
	 *            the record
	 * @return the JSON object
	 * @see com.sourcetrace.esesw.view.SwitchAction#toJSON(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object record) {

		JSONObject jsonObject = null;
		jsonObject = new JSONObject();
		if (record instanceof Customer) {
			Customer customer = (Customer) record;
			JSONArray rows = new JSONArray();

	/*		if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(customer.getBranchId())))
							? getBranchesMap().get(getParentBranchMap().get(customer.getBranchId()))
							: getBranchesMap().get(customer.getBranchId()));
				}
				rows.add(getBranchesMap().get(customer.getBranchId()));

			} else {
				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(branchesMap.get(customer.getBranchId()));
				}
			}*/

			rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">" + customer.getCustomerId() + "</font>");
			rows.add(customer.getCustomerName());
			rows.add(customer.getPersonName());
			rows.add(customer.getMobileNumber());
			rows.add(customer.getEmail());
			jsonObject.put("id", customer.getId());
			jsonObject.put("cell", rows);
			return jsonObject;

		}
		return jsonObject;
	}

	/**
	 * Populate state.
	 * 
	 * @param populateResponce
	 *            the populate responce
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public void populateLocality() throws Exception {
		if (!selectedState.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedState))) {
			localities = locationService.listLocalities(selectedState);
		}
		JSONArray localtiesArr = new JSONArray();
		if (!ObjectUtil.isEmpty(localities)) {
			for (Locality locality : localities) {
				localtiesArr.add(getJSONObject(locality.getCode(), locality.getName()+"-"+locality.getCode()));
			}
		}
		sendAjaxResponse(localtiesArr);
	}

	public void populateState() throws Exception {
		if (!selectedCountry.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedCountry))) {
			states = locationService.listStatesByCode(selectedCountry);
		}
		JSONArray stateArr = new JSONArray();
		if (!ObjectUtil.isEmpty(states)) {
			for (State state : states) {
				stateArr.add(getJSONObject(state.getCode(), state.getName()+""+state.getCode() ));
			}
		}
		sendAjaxResponse(stateArr);
	}

	public void populateTaluks() {
		if (!selectedDistrict.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedDistrict))) {
			municipalities = locationService.listMunicipalities(selectedDistrict);
		}
		JSONArray stateArr = new JSONArray();
		if (!ObjectUtil.isEmpty(municipalities)) {
			for (Municipality municipality : municipalities) {
				stateArr.add(getJSONObject(municipality.getCode(), municipality.getName()+"-"+municipality.getCode()));
			}
		}
		sendAjaxResponse(stateArr);
	}

	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
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

	/**
	 * Gets the data.
	 * 
	 * @return the data
	 * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
	 */
	@Override
	public Object getData() {
		if (ObjectUtil.isEmpty(customer)) {
			return null;
		} else {
			Country country = new Country();
			country.setCode(this.selectedCountry);

			State state = new State();
			state.setCode(this.selectedState);
			state.setCountry(country);

			Locality locality = new Locality();
			locality.setCode(this.selectedDistrict);
			locality.setState(state);

		/*	Municipality city = new Municipality();
			city.setCode(this.selectedCity);
			city.setLocality(locality);*/
			
			return customer;
		}
	}

	/**
	 * Gets the client service.
	 * 
	 * @return the client service
	 */
	public IClientService getClientService() {

		return clientService;
	}

	/**
	 * Sets the client service.
	 * 
	 * @param clientService
	 *            the new client service
	 */
	public void setClientService(IClientService clientService) {

		this.clientService = clientService;
	}

	/**
	 * Gets the id generator.
	 * 
	 * @return the id generator
	 */
	public IUniqueIDGenerator getIdGenerator() {

		return idGenerator;
	}

	/**
	 * Sets the id generator.
	 * 
	 * @param idGenerator
	 *            the new id generator
	 */
	public void setIdGenerator(IUniqueIDGenerator idGenerator) {

		this.idGenerator = idGenerator;
	}

	/**
	 * Sets the filter.
	 * 
	 * @param filter
	 *            the new filter
	 */
	public void setFilter(Customer filter) {

		this.filter = filter;
	}

	/**
	 * Gets the customer.
	 * 
	 * @return the customer
	 */
	public Customer getCustomer() {

		return customer;
	}

	/**
	 * Sets the customer.
	 * 
	 * @param customer
	 *            the new customer
	 */
	public void setCustomer(Customer customer) {

		this.customer = customer;
	}

	/**
	 * Gets the filter.
	 * 
	 * @return the filter
	 */
	public Customer getFilter() {

		return filter;
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
	 * Gets the holding type list.
	 * 
	 * @return the holding type list
	 */
	public Map<String, String> getHoldingTypeList() {

		return holdingTypeList;
	}

	/**
	 * Sets the holding type list.
	 * 
	 * @param holdingTypeList
	 *            the holding type list
	 */
	public void setHoldingTypeList(Map<String, String> holdingTypeList) {

		this.holdingTypeList = holdingTypeList;
	}

	/**
	 * Gets the selected holding type.
	 * 
	 * @return the selected holding type
	 */
	public String getSelectedHoldingType() {

		return selectedHoldingType;
	}

	/**
	 * Sets the selected holding type.
	 * 
	 * @param selectedHoldingType
	 *            the new selected holding type
	 */
	public void setSelectedHoldingType(String selectedHoldingType) {

		this.selectedHoldingType = selectedHoldingType;
	}

	public String getTabIndex() {

		return URLDecoder.decode(tabIndex);
	}

	public void setTabIndex(String tabIndex) {

		this.tabIndex = tabIndex;
	}

	public IAccountService getAccountService() {

		return accountService;
	}

	public void setAccountService(IAccountService accountService) {

		this.accountService = accountService;
	}

	public Map<String, String> getListOfCustomerTypes() {
		String customerTypes = getText("listOfCustomerTypes");
		Map<String, String> customersMap = new LinkedHashMap<String, String>();
		String[] customers = customerTypes.split(",");
		Arrays.sort(customers);
		int i = 0;
		for (String customer : customers) {
			customersMap.put(String.valueOf(i++), customer);
		}
		return customersMap;
	}

	public void setListOfCustomerTypes(Map<String, String> listOfCustomerTypes) {
		this.listOfCustomerTypes = listOfCustomerTypes;
	}

	public Map<String, String> getListOfCustomerSegment() {
		String customerSegments = getText("customerSegments");
		Map<String, String> customerSegmentMap = new LinkedHashMap<String, String>();
		String[] customerSegment = customerSegments.split(",");
		Arrays.sort(customerSegment);
		int i = 0;
		for (String customer : customerSegment) {
			customerSegmentMap.put(String.valueOf(i++), customer);
		}
		return customerSegmentMap;
	}

	public void setListOfCustomerSegment(Map<String, String> listOfCustomerSegment) {
		this.listOfCustomerSegment = listOfCustomerSegment;
	}

	public Map<String,String> getLocalities() {
		Map<String, String> locality=new LinkedHashMap<String, String>();
		if (!StringUtil.isEmpty(selectedState)) {
			localities = locationService.listLocalities(selectedState);
			for(Locality loc:localities){
				locality.put(loc.getCode(), loc.getCode()+" - "+loc.getName());
			}
		}
		return locality;
	}

	public void setLocalities(List<Locality> localities) {
		this.localities = localities;
	}

	public Map<String,String> getStates() {
		Map<String, String> state=new LinkedHashMap<String, String>();
		if (!StringUtil.isEmpty(selectedCountry)) {
			states = locationService.listStatesByCode(selectedCountry);
			for(State s:states){
				state.put(s.getCode(), s.getCode()+" - "+s.getName());
			}
		}
		return state;
	}

	public void setStates(List<State> states) {
		this.states = states;
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

	public Map<String, String> getCountries() {
		Map<String, String> countryMap=new LinkedHashMap<String, String>();
		List<Country> returnValue = locationService.listCountries();
		for(Country c:returnValue){
			countryMap.put(c.getCode(), c.getCode()+" - "+c.getName());
		}
		return countryMap;
	}

	public Municipality getMunicipality() {
		return municipality;
	}

	public void setMunicipality(Municipality municipality) {
		this.municipality = municipality;
	}

	public Map<String,String> getMunicipalities() {
		Map<String, String> city=new LinkedHashMap<String, String>();
		if (!ObjectUtil.isEmpty(customer)&&!ObjectUtil.isEmpty(customer.getCity())&&!StringUtil.isEmpty(customer.getCity().getName())) {
			List<Municipality> cities = locationService.listMunicipalitiesByCode(customer.getCity().getName());
			for(Municipality muncipality:cities){
				city.put(muncipality.getCode(), muncipality.getCode()+" - "+muncipality.getName());
			}
		}
		return city;
	}

	public void setMunicipalities(List<Municipality> municipalities) {
		this.municipalities = municipalities;
	}

	public String getSelectedDistrict() {
		return selectedDistrict;
	}

	public void setSelectedDistrict(String selectedDistrict) {
		this.selectedDistrict = selectedDistrict;
	}

	@SuppressWarnings("unchecked")
	public void populateChildBranch() {

		JSONArray branchArr = new JSONArray();

		List<BranchMaster> branchMaster = new ArrayList<>();
		if (!StringUtil.isEmpty(selectedBranch)) {
			branchMaster = clientService.listChildBranchIds(selectedBranch);

			branchMaster.stream().filter(branch -> !ObjectUtil.isEmpty(branch)).forEach(branch -> {
				branchArr.add(getJSONObject(branch.getBranchId(), branch.getName()));
			});
		} else {
			List<Object[]> branches = clientService.findChildBranches();
			branches.forEach(obj -> {
				branchArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}

		sendAjaxResponse(branchArr);
	}

	public String getSelectedCountryCode() {
		Country country = locationService.findCountryByName(selectedCountry);
		String countryCode=country.getCode();
		return countryCode;
	}

	public void setSelectedCountryCode(String selectedCountryCode) {
		this.selectedCountryCode = selectedCountryCode;
	}

	public String getSelectedStateCode() {
		State state=locationService.findStateByName(selectedState);
		String stateCode=state.getCode();
		return stateCode;
	}

	public void setSelectedStateCode(String selectedStateCode) {
		this.selectedStateCode = selectedStateCode;
	}

	public String getSelectedBranch() {
		return selectedBranch;
	}

	public void setSelectedBranch(String selectedBranch) {
		this.selectedBranch = selectedBranch;
	}
	
	

}
