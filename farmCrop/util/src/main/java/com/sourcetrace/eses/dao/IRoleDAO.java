/*
 * IRoleDAO.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.List;

import com.sourcetrace.eses.umgmt.entity.Action;
import com.sourcetrace.eses.umgmt.entity.Entitlement;
import com.sourcetrace.eses.umgmt.entity.Filter;
import com.sourcetrace.eses.umgmt.entity.Role;

// TODO: Auto-generated Javadoc
/**
 * IRoleDAO defines the interface for RoleDAO.
 */
public interface IRoleDAO extends IESEDAO {

	/**
	 * Find role.
	 * 
	 * @param id
	 *            the id
	 * 
	 * @return the role
	 */
	public Role findRole(long id);

	/**
	 * Find entitlement.
	 * 
	 * @param name
	 *            the name
	 * 
	 * @return the entitlement
	 */
	public Entitlement findEntitlement(String name);

	/**
	 * Find role by name.
	 * 
	 * @param name
	 *            the name
	 * 
	 * @return the role
	 */
	public Role findRoleByName(String name);

	/**
	 * Load role by name.
	 * 
	 * @param id
	 *            the id
	 * 
	 * @return the role
	 */
	public Role loadRoleMenus(long id);

	/**
	 * Load role.
	 * 
	 * @param id
	 *            the id
	 * 
	 * @return the role
	 */
	public Role loadRole(long id);

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
	 * 
	 * @return the set< role>
	 */
	public List<Role> listRolesByType(int type);

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
	 * 
	 * @return the filter
	 */
	public Filter findFilter(int id);

	/**
	 * List action.
	 * 
	 * @return the list
	 */
	public List<Action> listAction();

	public List<Role> listRolesByTypeAndBranchId(int type, String branchId);
	
	public Object[] findRoleInfo(long id);

    public List<Role> listRolesByTypeAndBranchIdExcludeBranch(int id, String branchId_F);

}
