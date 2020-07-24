/*
 * FarmerFailedInspectionReportDAO.java
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
import com.sourcetrace.eses.txn.agrocert.FarmerFailedQuestions;

/**
 * The Class FarmerFailedInspectionReportDAO.
 */
public class FarmerFailedInspectionReportDAO extends ReportDAO {

    /*
     * (non-Javadoc)
     * 
     * @see com.ese.dao.ReportDAO#addExampleFiltering(org.hibernate.Criteria,
     * java.util.Map)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void addExampleFiltering(Criteria criteria, Map params) {

        FarmerFailedQuestions entity = (FarmerFailedQuestions) params
                .get(FILTER);

       /* if (entity != null) {
            if (!StringUtil.isEmpty(entity.getUserName())) {
                criteria.add(Restrictions.like("userName",
                        entity.getUserName(), MatchMode.EXACT));
            }

        }*/
        if (!StringUtil.isEmpty(entity.getBranchId())){
		    criteria.add(Restrictions.eq("branchId", entity.getBranchId()));
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
