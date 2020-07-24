/*
 * FarmerFamilyReportDAO.java
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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.FarmerFamily;

public class FarmerFamilyReportDAO extends ReportDAO {

    /*
     * (non-Javadoc)
     * @see com.ese.dao.ReportDAO#addExampleFiltering(org.hibernate.Criteria, java.util.Map)
     */
    protected void addExampleFiltering(Criteria criteria, Map params) {

        // check for filter entity
        FarmerFamily entity = (FarmerFamily) params.get(FILTER);
        if (entity != null) {

            criteria.createAlias("farmer", "f");
            criteria.add(Restrictions.eq("f.id", entity.getFarmer().getId()));

            if (entity.getId() != 0L && !"".equals(entity.getId()))
                criteria.add(Restrictions.eq("id", entity.getId())); 
            if (entity.getName() != null && !"".equals(entity.getName()))
                criteria.add(Restrictions.like("name", entity.getName(), MatchMode.ANYWHERE));
            
              if (entity.getAge() != 0 && !"".equals(entity.getAge()))
              criteria.add(Restrictions.eq("age", entity.getAge()));
             
            if (entity.getGender()  != null && !"".equals(entity.getGender())) {
                criteria.add(Restrictions.eq("gender", entity.getGender()));
            }
            
            if (entity.getFilterStatusDis()!=null && !StringUtil.isEmpty(entity.getFilterStatusDis())) {
                criteria.add(Restrictions.eq("disability", entity.getDisability()));
            }
           
            if (entity.getRelation()  != null && !"".equals(entity.getRelation())) {
                criteria.add(Restrictions.eq("relation", entity.getRelation()));
            }
            if (entity.getEducation()  != null && !"".equals(entity.getEducation())) {
                criteria.add(Restrictions.eq("education", entity.getEducation()));
            }
            if (entity.getMaritalStatus()  != null && !"".equals(entity.getMaritalStatus())) {
                criteria.add(Restrictions.eq("maritalStatus", entity.getMaritalStatus()));
            }
            if (entity.getEducationStatus()  != null && !"".equals(entity.getEducationStatus())) {
                criteria.add(Restrictions.eq("educationStatus", entity.getEducationStatus()));
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
