/*
 * IFederationDAO.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.List;

import com.sourcetrace.esesw.entity.profile.Federation;

/**
 * The Interface IFederationDAO.
 * @author $Author: aravind $
 * @version $Rev: 554 $ $Date: 2009-12-18 11:24:45 +0530 (Fri, 18 Dec 2009) $
 */
public interface IFederationDAO extends IESEDAO {

    /**
     * List federation.
     * @return the list< federation>
     */
    public List<Federation> list();

    /**
     * Find federation.
     * @param name the name
     * @return the federation
     */
    public Federation findByName(String name);
    
    /**
     * Find.
     * @param id the id
     * @return the federation
     */
    public Federation find(long id);
}
