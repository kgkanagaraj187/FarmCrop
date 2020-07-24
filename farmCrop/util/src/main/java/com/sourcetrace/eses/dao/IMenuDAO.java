/*
 * IMenuDAO.java
 * Copyright (c) 2008-2010, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.List;

import com.sourcetrace.eses.umgmt.entity.Menu;
import com.sourcetrace.eses.umgmt.entity.Role;

/**
 * IMenuDAO defines the interface for Menu DAO.
 * 
 * @author $Author: aravind $
 * @version $Rev: 1268 $, $Date: 2010-11-24 10:52:01 +0530 (Wed, 24 Nov 2010) $
 */
public interface IMenuDAO extends IESEDAO {

	/**
	 * Find menu.
	 * 
	 * @param id
	 *            the id
	 * @return the menu
	 */
	public Menu findMenu(long id);

	/**
	 * Find menu.
	 * 
	 * @param label
	 *            the label
	 * @return the menu
	 */
	public Menu findMenu(String label);

	/**
	 * List all.
	 * 
	 * @return the list< menu>
	 */
	public List<Menu> listMenus();

	/**
	 * List parent menu.
	 * 
	 * @return the list< menu>
	 */
	public List<Menu> listTopParentMenus();

	/**
	 * List all.
	 * 
	 * @param id
	 *            the id
	 * @return the list< menu>
	 */
	public List<Menu> listFlatSubMenusForTopParent(long id);

	/**
	 * List all.
	 * 
	 * @param array
	 * @return the list< menu>
	 */
	public List<Menu> listMenus(Long[] array);

	public Object[] findMenuInfo(long id);

    public Role findRoleExcludeBranch(long selectedRole);

}
