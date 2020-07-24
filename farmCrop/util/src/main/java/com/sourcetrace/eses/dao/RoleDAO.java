/*
 * RoleDAO.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.umgmt.entity.Action;
import com.sourcetrace.eses.umgmt.entity.Entitlement;
import com.sourcetrace.eses.umgmt.entity.Filter;
import com.sourcetrace.eses.umgmt.entity.Role;
import com.sourcetrace.eses.umgmt.entity.User;
import com.sourcetrace.eses.util.StringUtil;

/**
 * RoleDAO implements the IRoleDAO interface.
 * 
 * @author $Author: aravind $
 * @version $Rev: 1384 $ $Date: 2010-11-26 14:56:59 +0530 (Fri, 26 Nov 2010) $
 */
@Repository
@Transactional
public class RoleDAO extends ESEDAO implements IRoleDAO {

	/**
	 * Instantiates a new role dao.
	 * 
	 * @param sessionFactory
	 *            the session factory
	 */
	@Autowired
	public RoleDAO(SessionFactory sessionFactory) {

		this.setSessionFactory(sessionFactory);
	}

	/**
	 * List roles.
	 * 
	 * @return the list< role>
	 * @see com.ese.dao.user.IRoleDAO#listRoles()
	 */
	public List<Role> listRoles() {

		return list("FROM Role ORDER BY name ASC");
	}

	/**
	 * Find role.
	 * 
	 * @param id
	 *            the id
	 * @return the role
	 * @see com.ese.dao.user.IRoleDAO#findRole(long)
	 */
	public Role findRole(long id) {

		Role role = (Role) find("FROM Role r WHERE r.id = ?", id);
		return role;
	}

	/**
	 * Load role by name.
	 * 
	 * @param id
	 *            the id
	 * @return the role
	 * @see com.ese.dao.user.IRoleDAO#loadRoleByName(java.lang.String)
	 */
	public Role loadRole(long id) {

		Role role = (Role) find(
				"From Role r INNER JOIN FETCH r.entitlements " + "INNER JOIN FETCH r.menus WHERE r.id = ?", id);
		return role;
	}

	/**
	 * Load role menus.
	 * 
	 * @param id
	 *            the id
	 * @return the role
	 * @see com.ese.dao.user.IRoleDAO#loadRoleMenus(java.lang.String)
	 */
	public Role loadRoleMenus(long id) {

		Role role = (Role) find("From Role r INNER JOIN FETCH r.menus WHERE r.id = ?", id);
		return role;
	}

	/**
	 * Find role by name.
	 * 
	 * @param name
	 *            the name
	 * @return the role
	 * @see com.ese.dao.user.IRoleDAO#findRoleByName(java.lang.String)
	 */
	public Role findRoleByName(String name) {

		Role role = (Role) find("FROM Role r WHERE r.name = ?", name);
		return role;
	}

	/**
	 * Find entitlement.
	 * 
	 * @param name
	 *            the name
	 * @return the entitlement
	 * @see com.ese.dao.user.IRoleDAO#findEntitlement(java.lang.String)
	 */
	public Entitlement findEntitlement(String name) {

		Entitlement ent = (Entitlement) find("FROM Entitlement e WHERE e.authority = ?", name);
		return ent;
	}

	/**
	 * List roles by type.
	 * 
	 * @param type
	 *            the type
	 * @return the set< role>
	 * @see com.ese.dao.user.IRoleDAO#listRolesByType(int)
	 */
	public List<Role> listRolesByType(int type) {

		List<Role> availableRoles = list("FROM Role r WHERE r.filter.id = ?", type);
		return availableRoles;
	}

	/**
	 * List filters.
	 * 
	 * @return the list< filter>
	 * @see com.ese.dao.user.IRoleDAO#listFilters()
	 */
	public List<Filter> listFilters() {

		List<Filter> filters = list("FROM Filter f");
		return filters;
	}

	/**
	 * Find filter.
	 * 
	 * @param id
	 *            the id
	 * @return the filter
	 * @see com.sourcetrace.eses.umgmt.dao.user.IRoleDAO#findFilter(int)
	 */
	public Filter findFilter(int id) {

		Filter filter = (Filter) find("FROM Filter f WHERE f.id = ?", id);
		return filter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.umgmt.dao.user.IRoleDAO#listAction()
	 */
	public List<Action> listAction() {

		return (List<Action>) list("FROM Action a ORDER BY a.id ASC");
	}

	@Override
	public List<Role> listRolesByTypeAndBranchId(int type, String branchId) {
		// TODO Auto-generated method stub
		if (StringUtil.isEmpty(branchId)) {
			List<Role> availableRoles = list("FROM Role r WHERE r.filter.id = ? AND r.branchId IS NULL ORDER BY r.name ASC",
					 type);
			return availableRoles;
		} else {
			List<Role> availableRoles = list("FROM Role r WHERE r.filter.id = ? AND r.branchId = ? ORDER BY r.name ASC",
					new Object[] { type, branchId });
			return availableRoles;
		}
		
	}

	@Override
	public Object[] findRoleInfo(long id) {
		// TODO Auto-generated method stub
		Object[] roleInfo = (Object[]) find("SELECT r.id,r.name,r.branchId FROM Role r WHERE r.id = ?", id);
		return roleInfo;
	}

    @Override
    public List<Role> listRolesByTypeAndBranchIdExcludeBranch(int id, String branchId) {
        Session session = getSessionFactory().openSession();
        session.disableFilter(ISecurityFilter.BRANCH_FILTER);
        String queryStr="";
        List list; 
        Query query;
        List<Role> user  = new ArrayList<>();
        if (StringUtil.isEmpty(branchId)) {
            queryStr = "FROM Role r WHERE r.filter.id = :type AND r.branchId IS NULL ORDER BY r.name ASC";
             query = session.createQuery(queryStr);
            query.setParameter("type", id);
            user = query.list();
        } else {
            queryStr = "FROM Role r WHERE r.filter.id = :type AND r.branchId = :branch ORDER BY r.name ASC";
             query = session.createQuery(queryStr);
            query.setParameter("type", id);
            query.setParameter("branch", branchId);
            user = query.list();
                  
        }

     
      
        session.flush();
        session.close();
        return user;
    }

}
