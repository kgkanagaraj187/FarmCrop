/*
 * IRoleService.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import java.util.List;
import java.util.Map;

import com.sourcetrace.eses.umgmt.entity.Action;
import com.sourcetrace.eses.umgmt.entity.Filter;
import com.sourcetrace.eses.umgmt.entity.Menu;
import com.sourcetrace.eses.umgmt.entity.Role;

/**
 * IRoleService is an interface for user role related services.
 * 
 * @author $Author: ganesh $
 * @version $Rev: 682 $ $Date: 2010-02-03 10:26:45 +0530 (Wed, 03 Feb 2010) $
 */
public interface IRoleService {

	/** The Constant SELECTED. */
	public static final String SELECTED = "selected";

	/** The Constant AVAILABLE. */
	public static final String AVAILABLE = "available";

	/**
	 * List roles.
	 * 
	 * @return the list< role>
	 */
	public List<Role> listRoles();

	/**
	 * List roles by type.
	 * 
	 * @param type
	 *            the type
	 * @return the set< role>
	 */
	public List<Role> listRolesByType(int type);

	/**
	 * Find role.
	 * 
	 * @param id
	 *            the id
	 * @return the role
	 */
	public Role findRole(long id);

	/**
	 * Find role.
	 * 
	 * @param name
	 *            the name
	 * @return the role
	 */
	public Role findRole(String name);

	/**
	 * Creates the role.
	 * 
	 * @param aRole
	 *            the a role
	 */
	public void addRole(Role aRole);

	/**
	 * Update role.
	 * 
	 * @param aRole
	 *            the a role
	 */
	public void editRole(Role aRole);

	/**
	 * Delete role.
	 * 
	 * @param aRole
	 *            the a role
	 */
	public void removeRole(Role aRole);

	/**
	 * List parent menus.
	 * 
	 * @return the list< menu>
	 */
	public List<Menu> listParentMenus();

	/**
	 * List flat selected sub menus for the given role and parent menu. The
	 * available sub menus for a role and parent menu are built by excluding
	 * already selected menus from the total sub menu list.
	 * 
	 * @param roleId
	 *            the given role id
	 * @param parentMenuId
	 *            the given parent menu id
	 * @return List<Menu> the selected sub menus
	 */
	public List<Menu> getSelectedSubMenus(long roleId, long parentMenuId);

	/**
	 * Returns a map containing available and selected sub menus for the given
	 * role and parent menu. The available sub menus for a role and parent menu
	 * are built by excluding already selected menus from the total sub menu
	 * list.
	 * 
	 * @param roleId
	 *            the given role id
	 * @param parentMenuId
	 *            the given parent menu id
	 * @return Map<String, List<Menu>> the available selected sub menus
	 */
	public Map<String, List<Menu>> getAvailableSelectedSubMenus(long roleId, long parentMenuId);

	/**
	 * Returns a map containing selected sub menus as key and value is a map
	 * containing actions as key and value is a boolean true if entitlement is
	 * given for the given role.
	 * 
	 * @param roleId
	 *            the role id
	 * @param parentMenuId
	 *            the parent menu id
	 * @return the selected sub menus actions
	 */
	public Map<String, Map<String, String>> getSelectedSubMenusActions(long roleId, long parentMenuId);

	/**
	 * Gets the entitlements.
	 * 
	 * @param roleId
	 *            the role id
	 * @param parentMenuId
	 *            the parent menu id
	 * @return the entitlements
	 */
	public List<String> getEntitlements(long roleId, long parentMenuId);

	/**
	 * Update sub menus for role.
	 * 
	 * @param roleId
	 *            the role id
	 * @param selectedSubMenus
	 *            the selected sub menus
	 * @param parentMenuId
	 *            the parent menu id
	 */
	public void editSubMenusForRole(long roleId, long parentMenuId, List<Long> selectedSubMenus);

	/**
	 * Update entitlements for the given role.
	 * 
	 * @param entitlements
	 *            the entitlements
	 * @param roleId
	 *            the role id
	 * @param parentId
	 *            the parent id
	 */
	public Role editEntitlements(long roleId, long parentId, List<String> entitlements);

	/**
	 * List filters.
	 * 
	 * @return the list< filter>
	 */
	public List<Filter> listFilters();

	/**
	 * Find filter.
	 * 
	 * @param id
	 *            the id
	 * @return the filter
	 */
	public Filter findFilter(int id);

	/**
	 * List action.
	 * 
	 * @return the list< action>
	 */
	public List<Action> listAction();

	public List<Role> listRolesByTypeAndBranchId(int type, String branchId);

	public Object[] findRoleInfo(long id);

    public List<Role> listRolesByTypeAndBranchIdExcludeBranch(int id, String branchId_F);
}
