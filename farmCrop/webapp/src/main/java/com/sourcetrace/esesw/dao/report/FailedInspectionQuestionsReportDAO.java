/*
 * FailedInspectionQuestionsReportDAO.java
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.txn.agrocert.FailedQuestions;
import com.sourcetrace.eses.util.StringUtil;

public class FailedInspectionQuestionsReportDAO extends ReportDAO {

    /*
     * (non-Javadoc)
     * 7
     * @see com.ese.dao.ReportDAO#addExampleFiltering(org.hibernate.Criteria,
     * java.util.Map)
     */
    @Override
    protected void addExampleFiltering(Criteria criteria, Map params) {

        // check for filter entity
        FailedQuestions entity = (FailedQuestions) params.get(FILTER);

        if (entity != null) {

        //    criteria.add(Restrictions.eq("userName", entity.getUserName()));

        	if (!StringUtil.isEmpty(entity.getBranchId())){
			    criteria.add(Restrictions.eq("branchId", entity.getBranchId()));
			}
        	
            // sorting direction
            String dir = (String) params.get(DIR);
            // sorting column
            String sort = (String) params.get(SORT_COLUMN);
            // sort a column in the given direction ascending/descending
            if (dir != null && sort != null) {
            if (dir.equals(DESCENDING)) {
                // sort descending
                criteria.addOrder(Order.desc(sort));
            } else {
                // sort ascending
                criteria.addOrder(Order.asc(sort));
            }
            }
        }
    }

}
