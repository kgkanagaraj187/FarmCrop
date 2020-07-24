/*
 * MenuDAO.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.umgmt.entity.Menu;
import com.sourcetrace.eses.umgmt.entity.Role;

/**
 * MenuDAO is the implementation of IMenuDAO interface.
 * 
 * @author $Author: aravind $
 * @version $Rev: 1268 $, $Date: 2010-11-24 10:52:01 +0530 (Wed, 24 Nov 2010) $
 */
@Repository
@Transactional
public class MenuDAO extends ESEDAO implements IMenuDAO {

	/**
	 * Instantiates a new menu dao.
	 * 
	 * @param sessionFactory
	 *            the session factory
	 */
	@Autowired
	public MenuDAO(SessionFactory sessionFactory) {

		this.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.umgmt.dao.user.IMenuDAO#findMenu(long)
	 */
	public Menu findMenu(long id) {

		Menu menu = (Menu) find("FROM Menu m WHERE m.id = ?", id);
		return menu;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.umgmt.dao.user.IMenuDAO#findMenu(java.lang.String)
	 */
	public Menu findMenu(String label) {

		Menu menu = (Menu) find("FROM Menu m WHERE m.label = ?", label);
		return menu;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.umgmt.dao.user.IMenuDAO#listMenus()
	 */
	public List<Menu> listMenus() {

		List<Menu> menus = (List<Menu>) list("FROM Menu m WHERE m.parentId IS NULL ORDER BY m.order");
		return menus;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.umgmt.dao.user.IMenuDAO#listFlatSubMenusForTopParent
	 * (long)
	 */
	public List<Menu> listFlatSubMenusForTopParent(long id) {

		Object[] values = { id, id };
		List<Menu> menus = (List<Menu>) list("FROM Menu m WHERE m.parentId IN "
				+ "(SELECT cm.id FROM Menu cm WHERE cm.parentId = ?) OR m.parentId = ? "
				+ "AND m.url !='javascript:void(0)'", values);
		return menus;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.umgmt.dao.user.IMenuDAO#listTopParentMenus()
	 */
	public List<Menu> listTopParentMenus() {

		List<Menu> menus = (List<Menu>) list("FROM Menu m where m.parentId = null");
		return menus;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.umgmt.dao.user.IMenuDAO#listMenus(java.lang.Long[])
	 */
	@Override
	public List<Menu> listMenus(Long[] array) {

		return (List<Menu>) getHibernateTemplate().findByNamedParam("FROM Menu m where m.id in (:ids)", "ids", array);
	}

	@Override
	public Object[] findMenuInfo(long id) {
		// TODO Auto-generated method stub
		return (Object[]) find("SELECT m.id,m.label,m.url FROM Menu m WHERE m.id=?", id);
	}

    @Override
    public Role findRoleExcludeBranch(long selectedRole) {
        Role returnVal=new Role();
        Session session = getSessionFactory().openSession();
        session.disableFilter(ISecurityFilter.BRANCH_FILTER);
        Query query = session.createQuery("FROM Role where id = :selectedRole");
        query.setParameter("selectedRole",selectedRole);
        List<Role> list = query.list();
        if(list.size()>0){
            returnVal = list.get(0);
        }
        session.flush();
        session.clear();
        session.close();
        return returnVal;
    }
}
