/*
 * CustomerReportDAO.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.ResearchStation;

// TODO: Auto-generated Javadoc
/**
 * The Class CustomerReportDAO.
 */
public class ResearchStationReportDAO extends ReportDAO {

    /**
     * Adds the example filtering.
     * 
     * @param criteria
     *            the criteria
     * @param params
     *            the params
     */
    @SuppressWarnings("unchecked")
    protected void addExampleFiltering(Criteria criteria, Map params) {

        // check for filter entity
        ResearchStation entity = (ResearchStation) params.get(FILTER);
        if (entity != null) {

            if (!StringUtil.isEmpty(entity.getBranchId())) {
                criteria.add(Restrictions.like("branchId", entity.getBranchId(), MatchMode.EXACT));
            }

            if (entity.getResearchStationId() != null && !"".equals(entity.getResearchStationId()))
                criteria.add(Restrictions.like("researchStationId", entity.getResearchStationId(), MatchMode.ANYWHERE));
            
            if (entity.getName() != null && !"".equals(entity.getName()))
                criteria.add(Restrictions.like("name", entity.getName(), MatchMode.ANYWHERE));
            
            if (entity.getPointPerson() != null && !"".equals(entity.getPointPerson()))
                criteria.add(Restrictions.like("pointPerson", entity.getPointPerson(), MatchMode.ANYWHERE));
            
            if (entity.getDivision() != null && !"".equals(entity.getDivision()))
                criteria.add(Restrictions.like("division", entity.getDivision(), MatchMode.ANYWHERE));
            
            if (entity.getDesignation() != null && !"".equals(entity.getDesignation()))
                criteria.add(Restrictions.like("designation", entity.getDesignation(), MatchMode.ANYWHERE));


            if (entity.getResearchStationAddress() != null && !"".equals(entity.getResearchStationAddress()))
                criteria.add(Restrictions.like("researchStationAddress", entity.getResearchStationAddress(), MatchMode.ANYWHERE));
            
         
            String dir = (String) params.get(DIR);
            String sort = (String) params.get(SORT_COLUMN);

            if (dir != null && sort != null) {
                if (dir.equals(DESCENDING)) {
                    criteria.addOrder(Order.desc(sort));
                } else {
                    criteria.addOrder(Order.asc(sort));
                }
            }
        }
    }

}
