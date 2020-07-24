/**
 * The Class RoleAction.
 */
package com.ese.view.profile;

import java.util.ArrayList;

/**
 * RoleAction.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IRoleService;
import com.sourcetrace.eses.umgmt.entity.Filter;
import com.sourcetrace.eses.umgmt.entity.Role;
import com.sourcetrace.eses.umgmt.entity.User;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

/**
 * The Class RoleAction.
 */
public class RoleAction extends SwitchValidatorAction {
	private static final long serialVersionUID = 1L;

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(RoleAction.class);

	/** The role service. */
	private IRoleService roleService;

	private List<Filter> filters;

	private String selectedfilter;
	/** The role. */
	private Role role;

	/** The id. */
	private String id;

	/** The status. */
	private boolean status;

	private String branchId_F;
	private String subBranchId_F;
	
	@Autowired
	private IClientService clientService;

	/**
	 * Gets the role.
	 * 
	 * @return the role
	 */
	public Role getRole() {

		return role;
	}

	/**
	 * Sets the role.
	 * 
	 * @param role
	 *            the new role
	 */
	public void setRole(Role role) {

		this.role = role;
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
	 * Gets the status.
	 * 
	 * @return the status
	 */
	public boolean getStatus() {

		return status;
	}

	/**
	 * Sets the status.
	 * 
	 * @param status
	 *            the new status
	 */
	public void setStatus(boolean status) {

		this.status = status;
	}

	public String getSelectedfilter() {

		return selectedfilter;
	}

	public void setSelectedfilter(String selectedfilter) {

		this.selectedfilter = selectedfilter;
	}

	/**
	 * @see com.sourcetrace.esesw.view.SwitchAction#list()
	 */
	public String list() throws Exception {

		request.setAttribute(HEADING, getText(LIST));
		return LIST;
	}

	/**
	 * @see com.sourcetrace.esesw.view.SwitchAction#data()
	 */
	public String data() throws Exception {

		Map<String, String> searchRecord = getJQGridRequestParam();

		Role filter = new Role();

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

		if (!StringUtil.isEmpty(searchRecord.get("name"))) {
			filter.setName(searchRecord.get("name").trim());
		}

		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		return sendJQGridJSONResponse(data);

	}

	/**
	 * @see com.sourcetrace.esesw.view.SwitchAction#toJSON(java.lang.Object)
	 */
	public JSONObject toJSON(Object obj) {

		Role role = (Role) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		if ((getIsMultiBranch().equalsIgnoreCase("1") && (getIsParentBranch().equals("1")||StringUtil.isEmpty(branchIdValue)))) {

	      if(StringUtil.isEmpty(branchIdValue)){  
           rows.add(!StringUtil.isEmpty(
                    getBranchesMap().get(getParentBranchMap().get(role.getBranchId())))
                            ? getBranchesMap()
                                    .get(getParentBranchMap().get(role.getBranchId()))
                            : getBranchesMap().get(role.getBranchId()));
	      }
        rows.add(!StringUtil.isEmpty(
                getBranchesMap().get(getParentBranchMap().get(role.getBranchId())))
                        ? getBranchesMap().get(role.getBranchId())
                        : "");

		} else {
        if (StringUtil.isEmpty(branchIdValue)) {
            rows.add(branchesMap.get(role.getBranchId()));
        }
    	}
		rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">" + role.getName() + "</font>");
		// rows.add(role.getFilter().getName());
		// rows.add(role.getFilter());
		jsonObject.put("id", role.getId());
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

		filters = roleService.listFilters();
		// localizeValues(filters);
		if (role == null) {
			command = CREATE;
			request.setAttribute(HEADING, getText(CREATE));
			return INPUT;
		} else {
			String bId = StringUtil.isEmpty(getBranchId())
					? ((StringUtil.isEmpty(getBranchId_F())) ? null : getBranchId_F()) : getBranchId();
			role.setBranchId(bId);

			if (getIsMultiBranch().equals("1") && !StringUtil.isEmpty(getSubBranchId_F())) {
				role.setBranchId(subBranchId_F);
			}

			roleService.addRole(role);
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
	//
	public String update() throws Exception {

		filters = roleService.listFilters();
		// localizeValues(filters);

		String view = "";
		if (id != null && !id.equals("")) {
			role = roleService.findRole(Long.parseLong(id));
			id = null;
			if (role == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			
			if( role.getBranchId()!=null && !StringUtil.isEmpty( role.getBranchId())){
		        BranchMaster fm  = clientService.findBranchMasterByBranchId( role.getBranchId());
		        if(!ObjectUtil.isEmpty(fm))
		        {
		            if(fm.getParentBranch()==null || StringUtil.isEmpty(fm.getParentBranch())){
		            	 role.setParentBranchId(role.getBranchId());
		            	 role.setBranchId("");
		             //   branchId_F = role.getBranchId();
		            }else{
		                BranchMaster parent  = clientService.findBranchMasterByBranchIdAndDisableFilter( fm.getParentBranch());
		                branchId_F = parent.getBranchId();
		                subBranchId_F = role.getBranchId();
		                role.setParentBranchId(branchId_F);
		            }
		            }
			}
			command = UPDATE;
			view = INPUT;
			request.setAttribute(HEADING, getText("updatetitle"));
		} else {

			view = INPUT;
			if (role != null) {

				Role temp = roleService.findRole(role.getId());

				temp.setName(role.getName());
				temp.setIsAdminUser(role.getIsAdminUser());
				temp.setFilter(role.getFilter());
				if (temp == null) {
					addActionError(NO_RECORD);
					return REDIRECT;
				}

				roleService.editRole(temp);
			}
			view = REDIRECT;
			return view;
		}
		return view;
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
			role = roleService.findRole(Long.parseLong(id));
			// setSelectedfilter(role.getFilter().getName());
			if (role == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
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

		logger.info("RoleId " + id);
		if (id != null && !id.equals("")) {
			role = roleService.findRole((Long.parseLong(id)));
			Set<User> users = role.getUsers();
			if (users != null && !users.isEmpty()) {
				addActionError(getText("delete.roleWithUsers"));
				request.setAttribute(HEADING, getText(DETAIL));
				// setSelectedfilter(role.getFilter().getName());
				return DETAIL;
			} else {
				roleService.removeRole(role);
			}
		}
		return REDIRECT;
	}

	/**
	 * Gets the list by type.
	 * 
	 * @return the list by type
	 */
	public List<Filter> getListByType() {

		filters = roleService.listFilters();
		return filters;
	}

	/**
	 * Gets the data.
	 * 
	 * @return the data
	 * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
	 */
	@Override
	public Object getData() {

		if (branchId_F != null) {
			role.setBranchId(branchId_F);
		}
		
		if(role!=null){
		    if( role.getBranchId()!=null && !StringUtil.isEmpty( role.getBranchId())){
                BranchMaster fm  = clientService.findBranchMasterByBranchId( role.getBranchId());
                if(!ObjectUtil.isEmpty(fm))
		        {
                   if(fm.getParentBranch()==null || StringUtil.isEmpty(fm.getParentBranch())){
                       branchId_F = role.getBranchId();
                   }else{
                       BranchMaster parent  = clientService.findBranchMasterByBranchIdAndDisableFilter( fm.getParentBranch());
                       branchId_F = parent.getBranchId();
                       subBranchId_F = role.getBranchId();
                       role.setParentBranchId(branchId_F);
                   }
           }  
		    }
		}

		return role;

	}

	public IRoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(IRoleService roleService) {
		this.roleService = roleService;
	}

	public String getBranchId_F() {
		return branchId_F;
	}

	public void setBranchId_F(String branchId_F) {
		this.branchId_F = branchId_F;
	}

	public String getSubBranchId_F() {
		return subBranchId_F;
	}

	public void setSubBranchId_F(String subBranchId_F) {
		this.subBranchId_F = subBranchId_F;
	}

	public void populateSubBranches() {
		JSONArray branchArr = new JSONArray();
		if (!StringUtil.isEmpty(getBranchId_F())) {
			clientService.listChildBranchIds(branchId_F).stream().filter(branch -> !ObjectUtil.isEmpty(branch))
					.forEach(branch -> {
						branchArr.add(getJSONObject(branch.getBranchId(), branch.getName()));
					});
		} else {

		}
		sendAjaxResponse(branchArr);
	}

	public Map<String, String> getParentBranches() {
		return clientService.findParentBranches().stream().collect(
				Collectors.toMap(branchId -> String.valueOf(branchId[0]), branchName -> String.valueOf(branchName[1])));
	}

	public Map<String, String> getSubBranchesMap() {
	    String branchId = getBranchId() == null ? getBranchId_F() : StringUtil.isEmpty(getBranchId()) ? getBranchId_F() : getBranchId() ;
		return clientService.listChildBranchIds(branchId).stream().filter(branch -> !ObjectUtil.isEmpty(branch))
				.collect(Collectors.toMap(BranchMaster::getBranchId, BranchMaster::getName));
	}

	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}
}
