package com.sourcetrace.esesw.view.general;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.ese.view.profile.CustomerAction;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

@SuppressWarnings("serial")
public class BranchMasterAction extends SwitchValidatorAction {

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
	private ESEAccount account;

	private String id;
	private String branchId;
	private String name;
	private String contactPerson;
	private String phoneNo;
	private String address;
	private String profileId;
	private BranchMaster branchMaster;
	private BranchMaster filter;
	private Map<String, String> statusMap = new LinkedHashMap<>();
	private String status;
	private String statusDeafaultVal;

	private IClientService clientService;
	private IAccountService accountService;
	private IUniqueIDGenerator idGenerator;
	@Autowired
	private IPreferencesService preferncesService;

	@Override
	public Object getData() {
		if (ObjectUtil.isEmpty(branchMaster)) {
			return null;
		} else {
			return branchMaster;
		}
	}

	@SuppressWarnings("unchecked")
	public String data() throws Exception {

		Map<String, String> searchRecord = getJQGridRequestParam(); // get the
																	// search
																	// parameter
																	// with

		BranchMaster filter = new BranchMaster();

		if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
			filter.setBranchId(searchRecord.get("branchId").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("name"))) {
			filter.setName(searchRecord.get("name").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("contactPerson"))) {
			filter.setContactPerson(searchRecord.get("contactPerson").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("phoneNo"))) {
			filter.setPhoneNo(searchRecord.get("phoneNo").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("address"))) {
			filter.setAddress(searchRecord.get("address").trim());
		}
		
		if (!StringUtil.isEmpty(searchRecord.get("status"))) {
			filter.setStatus(searchRecord.get("status").trim());
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
			branchMaster = clientService.findBranchMasterById(Long.valueOf(id));
			account = accountService.findAccountByProfileIdAndProfileType(branchMaster.getBranchId(),
					ESEAccount.ORGANIZATION_ACCOUNT);
			if (branchMaster == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			account = accountService.findAccountByProfileIdAndProfileType(branchMaster.getBranchId(),
					ESEAccount.ORGANIZATION_ACCOUNT);
			
			if (!ObjectUtil.isEmpty(account)) {
				String[] agentAcc = String.valueOf(account.getCashBalance()).split("\\.");
				branchMaster.setAccountRupee(agentAcc[0]);
				branchMaster.setAccountPaise(agentAcc[1]);
			}
			setStatus(!ObjectUtil.isEmpty(branchMaster.getStatus()) && branchMaster.getStatus().equals("0")?getText("status0"):getText("status1"));
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
	 * Creates the.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String create() throws Exception {

		if (branchMaster == null) {
			command = "create";
			request.setAttribute(HEADING, getText(CREATE));
			setStatusDeafaultVal("1");
			return INPUT;
		} else {
			if (!StringUtil.isEmpty(branchMaster.getAccountBalance())) {
				branchMaster.setAccountBalance(branchMaster.getAccountBalance());
			} else
				branchMaster.setAccountBalance(0.00);

			if (!StringUtil.isEmpty(super.getBranchId())) {
				BranchMaster branch = clientService.findBranchMasterByBranchIdAndDisableFilter(super.getBranchId());
				branchMaster.setParentBranch(branch.getBranchId());
			}
			branchMaster.setTenant(getCurrentTenantId());
			clientService.addBranchMaster(branchMaster);
			ESESystem ese = new ESESystem();
			Map<String, String> preferences = new HashMap<String, String>();
			ESESystem mainese = preferncesService.findPrefernceById("1");
			preferences.put(ESESystem.INVALID_ATTEMPTS_COUNT,
					mainese.getPreferences().get(ESESystem.INVALID_ATTEMPTS_COUNT));
			preferences.put(ESESystem.TIME_TO_AUTO_RELEASE,
					mainese.getPreferences().get(ESESystem.TIME_TO_AUTO_RELEASE));
			preferences.put(ESESystem.AREA_CAPTURE_MODE, mainese.getPreferences().get(ESESystem.AREA_CAPTURE_MODE));
			preferences.put(ESESystem.GEO_FENCING_FLAG, mainese.getPreferences().get(ESESystem.GEO_FENCING_FLAG));
			preferences.put(ESESystem.GEO_FENCING_RADIUS_MT,
					mainese.getPreferences().get(ESESystem.GEO_FENCING_RADIUS_MT));
			preferences.put(ESESystem.CURRENT_SEASON_CODE, "");
			ese.setName(branchMaster.getBranchId());
			ese.setPreferences(preferences);
			preferncesService.addOrganisationESE(ese);
			request = ReflectUtil.getCurrentHttpRequest();
			Object isMultiBranch = request.getSession().getAttribute(ISecurityFilter.IS_MULTI_BRANCH_APP);
			String multiBranch = ObjectUtil.isEmpty(isMultiBranch) ? "" : isMultiBranch.toString();
			if (multiBranch.equals("1")) {
				Object object = request.getSession().getAttribute(ISecurityFilter.MAPPED_BRANCHES);
				String currentBranch = ObjectUtil.isEmpty(object) ? "" : object.toString();
				currentBranch = currentBranch + "," + branchMaster.getBranchId();
				request.getSession().setAttribute(ISecurityFilter.MAPPED_BRANCHES, currentBranch);
			}
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
			branchMaster = clientService.findBranchMasterById(Long.valueOf(id));
			if (branchMaster == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			setStatusDeafaultVal(branchMaster.getStatus());
			account = accountService.findAccountByProfileIdAndProfileType(branchMaster.getBranchId(),
					ESEAccount.ORGANIZATION_ACCOUNT);
			if (!StringUtil.isEmpty(account)) {
				String[] accBalance = String.valueOf(account.getCashBalance()).split("\\.");
				branchMaster.setAccountRupee(String.valueOf(accBalance[0]));
				branchMaster.setAccountPaise(String.valueOf(accBalance[1]));
			} else {
				branchMaster.setAccountRupee("0");
				branchMaster.setAccountPaise("00");
			}

			setCurrentPage(getCurrentPage());

			id = null;
			command = UPDATE;
			request.setAttribute(HEADING, getText(UPDATE));
		} else {
			if (branchMaster != null) {
				BranchMaster tempBranchMaster = clientService.findBranchMasterById(Long.valueOf(branchMaster.getId()));
				if (tempBranchMaster == null) {
					addActionError(NO_RECORD);
					return REDIRECT;
				}
				setCurrentPage(getCurrentPage());
				// tempBranchMaster.setBranchId(branchMaster.getBranchId());
				tempBranchMaster.setName(branchMaster.getName());
				tempBranchMaster.setContactPerson(branchMaster.getContactPerson());
				tempBranchMaster.setPhoneNo(branchMaster.getPhoneNo());
				tempBranchMaster.setAddress(branchMaster.getAddress());
				tempBranchMaster.setAccountBalance(branchMaster.getAccountBalance());
				tempBranchMaster.setStatus(branchMaster.getStatus());
				clientService.editBranchMaster(tempBranchMaster);

				account = accountService.findAccountByProfileIdAndProfileType(tempBranchMaster.getBranchId(),
						ESEAccount.ORGANIZATION_ACCOUNT);
				if (account != null) {
					account.setCashBalance(branchMaster.getAccountBalance());
					accountService.update(account);
				}
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
			branchMaster = clientService.findBranchMasterById(Long.valueOf(getId()));
			if (branchMaster == null) {
				addActionError(NO_RECORD);
				return null;
			}
			if (!ObjectUtil.isEmpty(branchMaster)) {
				// clientService.removeBranchMaster(branchMaster); // this line
				// has been commented to disable deleting branch master.

			}

		}

		request.setAttribute(HEADING, getText(LIST));
		return LIST;

	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object record) {

		JSONObject jsonObject = null;
		jsonObject = new JSONObject();
		if (record instanceof BranchMaster) {
			BranchMaster branchMaster = (BranchMaster) record;
			JSONArray rows = new JSONArray();
			rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">" + branchMaster.getBranchId() + "</font>");

			rows.add(branchMaster.getName());
			rows.add(branchMaster.getContactPerson());
			rows.add(branchMaster.getPhoneNo());
			rows.add(branchMaster.getAddress());
			if(!StringUtil.isEmpty(branchMaster.getStatus())){
				rows.add(branchMaster.getStatus().equals("0")?getText("status0"):getText("status1"));
			}else{
				rows.add(getText("status1"));
			}
			jsonObject.put("id", String.valueOf(branchMaster.getId()));
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

	public String getId() {

		return id;
	}

	public void setId(String id) {

		this.id = id;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BranchMaster getBranchMaster() {
		return branchMaster;
	}

	public void setBranchMaster(BranchMaster branchMaster) {
		this.branchMaster = branchMaster;
	}

	public BranchMaster getFilter() {
		return filter;
	}

	public void setFilter(BranchMaster filter) {
		this.filter = filter;
	}

	public IClientService getClientService() {
		return clientService;
	}

	public void setClientService(IClientService clientService) {
		this.clientService = clientService;
	}

	public IAccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}

	public ESEAccount getAccount() {
		return account;
	}

	public void setAccount(ESEAccount account) {
		this.account = account;
	}

	public IUniqueIDGenerator getIdGenerator() {
		return idGenerator;
	}

	public void setIdGenerator(IUniqueIDGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public Map<String, String> getStatusMap() {
		statusMap.put("1", getText("status1"));
		statusMap.put("0", getText("status0"));
		return statusMap;
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
	
}
