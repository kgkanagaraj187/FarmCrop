/*
 * IStoreDAO.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.List;

import com.sourcetrace.esesw.entity.profile.Store;

/**
 * The Interface IStoreDAO.
 * @author $Author: boopalan $
 * @version $Rev: 1164 $ $Date: 2010-04-25 12:01:00 +0530 (Sun, 25 Apr 2010) $
 */
public interface IStoreDAO extends IESEDAO {

    /**
     * List store.
     * @return the list< store>
     */
    public List<Store> listStores();

    /**
     * Find.
     * @param id the id
     * @return the store
     */
    public Store find(long id);

    /**
     * Find store.
     * @param name the name
     * @return the store
     */
    public Store findByName(String name);

    /**
     * Find store by store id.
     * @param storeId the store id
     * @return the store
     */
    public Store findByStoreId(String storeId);

    /**
     * List stores by affiliate.
     * @param affiliate the affiliate
     * @return the list< store>
     */
    public List<Store> listStoresByAffiliate(String affiliate);

    /**
     * Find by store id without affiliate.
     * @param storeId the store id
     * @return the store
     */
    public Store findByStoreIdWithoutAffiliate(String storeId);
}
