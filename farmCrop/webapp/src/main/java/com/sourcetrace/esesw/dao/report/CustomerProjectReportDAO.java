/*
 * CustomerProjectReportDAO.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
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
import com.sourcetrace.esesw.entity.profile.CustomerProject;

/**
 * The Class CustomerProjectReportDAO.
 */
public class CustomerProjectReportDAO extends ReportDAO {

    /*
     * (non-Javadoc)
     * @see com.ese.dao.ReportDAO#addExampleFiltering(org.hibernate.Criteria, java.util.Map)
     */
    protected void addExampleFiltering(Criteria criteria, Map params) {

        // check for filter entity
        CustomerProject entity = (CustomerProject) params.get(FILTER);
        if (entity != null) {

            criteria.createAlias("customer", "c");
            criteria.add(Restrictions.eq("c.customerId", entity.getCustomer().getCustomerId()));

            if (entity != null) {

                if (entity.getCodeOfProject() != null && !"".equals(entity.getCodeOfProject())) {
                    criteria.add(Restrictions.like("codeOfProject", entity.getCodeOfProject(),
                            MatchMode.ANYWHERE));
                }
                if (entity.getNameOfProject() != null && !"".equals(entity.getNameOfProject())) {
                    criteria.add(Restrictions.like("nameOfProject", entity.getNameOfProject(),
                            MatchMode.ANYWHERE));
                }

                if (entity.getUnitNo() != null && !"".equals(entity.getUnitNo())) {
                    criteria.add(Restrictions
                            .like("unitNo", entity.getUnitNo(), MatchMode.ANYWHERE));
                }

                if (entity.getNameOfUnit() != null && !"".equals(entity.getNameOfUnit())) {
                    criteria.add(Restrictions.like("nameOfUnit", entity.getNameOfUnit(),
                            MatchMode.ANYWHERE));
                }

                if (entity.getLocationOfUnit() != null && !"".equals(entity.getLocationOfUnit())) {
                    criteria.add(Restrictions.like("locationOfUnit", entity.getLocationOfUnit(),
                            MatchMode.ANYWHERE));
                }
            }

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
