/*
 * StoreDAO.java
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

import com.sourcetrace.esesw.entity.profile.Store;

/**
 * The Class StoreDAO.
 * 
 * @author $Author: boopalan $
 * @version $Rev: 1164 $ $Date: 2010-04-25 12:01:00 +0530 (Sun, 25 Apr 2010) $
 */
@Repository
@Transactional
public class StoreDAO extends ESEDAO implements IStoreDAO {
	@Autowired
	public StoreDAO(SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}

	/**
	 * List stores.
	 * 
	 * @return the list< store>
	 * @see com.sourcetrace.esesw.dao.profile.IStoreDAO#listStores()
	 */
	public List<Store> listStores() {

		return list("FROM Store");
	}

	/**
	 * Find store by name.
	 * 
	 * @param name
	 *            the name
	 * @return the store
	 * @see com.sourcetrace.esesw.dao.profile.IStoreDAO#findByName(java.lang.String)
	 */
	public Store findByName(String name) {

		return (Store) find("FROM Store s WHERE s.name=?", name);
	}

	/**
	 * Find store by store id.
	 * 
	 * @param storeId
	 *            the store id
	 * @return the store
	 * @see com.sourcetrace.esesw.dao.profile.IStoreDAO#findByStoreId(java.lang.String)
	 */
	public Store findByStoreId(String storeId) {

		return (Store) find("FROM Store s LEFT JOIN FETCH s.affiliate WHERE s.storeId=?", storeId);
	}

	// for store deleting purpose
	public Store findByStoreIdWithoutAffiliate(String storeId) {

		return (Store) find("FROM Store s WHERE s.storeId=?", storeId);
	}

	/**
	 * Find.
	 * 
	 * @param id
	 *            the id
	 * @return the store
	 * @see com.sourcetrace.esesw.dao.profile.IStoreDAO#find(long)
	 */
	public Store find(long id) {

		return (Store) find("FROM Store s LEFT JOIN FETCH s.servicePoints WHERE s.id=?", id);
	}

	public List<Store> listStoresByAffiliate(String affiliate) {

		return list("FROM Store s where s.affiliate.affiliateId=?", affiliate);
	}
}
