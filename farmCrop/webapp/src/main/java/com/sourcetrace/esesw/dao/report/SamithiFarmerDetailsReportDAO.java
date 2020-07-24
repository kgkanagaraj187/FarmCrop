/*
 * SamithiReportDAO.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.Distribution;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmIcsConversion;
import com.sourcetrace.esesw.entity.profile.Farmer;

public class SamithiFarmerDetailsReportDAO extends ReportDAO {

    /**
     * Adds the example filtering.
     * @param criteria the criteria
     * @param params the params
     */
    protected void addExampleFiltering(Criteria criteria, Map params) {

        // check for filter entity
        Farmer entity = (Farmer) params.get(FILTER);
        if (entity != null) {
        	 
            criteria.createAlias("samithi", "s");   
           
            if (!ObjectUtil.isEmpty(entity) && !ObjectUtil.isEmpty(entity.getSamithi()) && entity.getSamithi().getId()!=0)
                criteria.add(Restrictions.eq("s.id", entity.getSamithi().getId()));
          
        }
    }
}
