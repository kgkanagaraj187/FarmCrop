/*
 * FederationDAO.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sourcetrace.esesw.entity.profile.Federation;

/**
 * The Class FederationDAO.
 * 
 * @author $Author: moorthy $
 * @version $Rev: 1413 $ $Date: 2010-09-08 18:51:40 +0530 (Wed, 08 Sep 2010) $
 */
@Repository@Transactional
public class FederationDAO extends ESEDAO implements IFederationDAO {

	@Autowired
	public FederationDAO(SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}

	/**
	 * List.
	 * 
	 * @return the list< federation>
	 * @see com.sourcetrace.esesw.dao.profile.IFederationDAO#list()
	 */
	@SuppressWarnings("unchecked")
	public List<Federation> list() {

		return list("FROM Federation");
	}

	/**
	 * Find by name.
	 * 
	 * @param name
	 *            the name
	 * @return the federation
	 * @see com.sourcetrace.esesw.dao.profile.IFederationDAO#findByName(java.lang.String)
	 */
	public Federation findByName(String name) {

		Federation federation = (Federation) find("FROM Federation f WHERE f.name = ?", name);
		return federation;
	}

	/**
	 * Find.
	 * 
	 * @param id
	 *            the id
	 * @return the federation
	 * @see com.sourcetrace.esesw.dao.profile.IFederationDAO#find(long)
	 */
	public Federation find(long id) {

		Federation federation = (Federation) find("FROM Federation f WHERE f.id = ?", id);
		return federation;
	}
}
