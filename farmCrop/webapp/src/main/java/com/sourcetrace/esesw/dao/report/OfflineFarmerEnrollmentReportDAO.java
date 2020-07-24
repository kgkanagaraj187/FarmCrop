/*
 * OfflineFarmerEnrollmentReportDAO.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.dao.report;

import java.util.Date;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.OfflineFarmEnrollment;

public class OfflineFarmerEnrollmentReportDAO extends ReportDAO {

    /*
     * (non-Javadoc)
     * @see com.ese.dao.ReportDAO#createCriteria(java.util.Map)
     */
    protected Criteria createCriteria(Map params) {

        String entity = (String) params.get(ENTITY);
        Object object = (Object) params.get(FILTER);
        if (!ObjectUtil.isEmpty(object)) {
            if (object instanceof OfflineFarmEnrollment) {
                entity = OfflineFarmEnrollment.class.getName();
            }
        }

        Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createCriteria(entity);

        return criteria;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.dao.ReportDAO#addExampleFiltering(org.hibernate.Criteria, java.util.Map)
     */
    protected void addExampleFiltering(Criteria criteria, Map params) {

        Object object = (Object) params.get(FILTER);
        if (!ObjectUtil.isEmpty(object)) {
            if (object instanceof Farmer) {
            	Farmer farmer = (Farmer) object;
                criteria.add(Restrictions.ne("statusCode", 0));
                criteria.createAlias("village", "v");
               
                if (!StringUtil.isEmpty(farmer.getVillage().getName())) {
                    criteria.add(Restrictions.like("v.name", farmer.getVillage().getName().trim(),
                            MatchMode.ANYWHERE));
                }
                if (!StringUtil.isEmpty(farmer.getLastName())) {
                    criteria.add(Restrictions.like("lastName", farmer.getLastName().trim(),
                            MatchMode.ANYWHERE));
                }
                if (!StringUtil.isEmpty(farmer.getBranchId())) {
					criteria.add(Restrictions.like("branchId", farmer.getBranchId(), MatchMode.EXACT));
				}
                if (!StringUtil.isEmpty(farmer.getStartDate()) && !StringUtil.isEmpty(farmer.getEndDate())) {
    				Date startDate = DateUtil.convertStringToDate(farmer.getStartDate(), DateUtil.DATE_FORMAT);
    				Date endDate = DateUtil.convertStringToDate(farmer.getEndDate(), DateUtil.DATE_FORMAT);
    				if (!ObjectUtil.isEmpty(startDate) && !ObjectUtil.isEmpty(endDate)) {
    					criteria.add(Restrictions.between("createdDate", DateUtil.getDateWithoutTime(startDate), DateUtil.getDateWithoutTime(endDate)));
    				}
                }
            }

        }
    }

}
