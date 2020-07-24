/*
 * ProcurementProductReportDAO.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;

public class ProcurementProductReportDAO extends ReportDAO {

    @SuppressWarnings("unchecked")
    protected void addExampleFiltering(Criteria criteria, Map params) {
    	
        // check for filter entity
        ProcurementProduct entity = (ProcurementProduct) params.get(FILTER);

        if (entity != null) {
        	
        	if (!ObjectUtil.isEmpty(entity) && entity != null) {
            	if(!StringUtil.isEmpty(entity.getBranchId())){
            		
            		criteria.add(Restrictions.like("branchId", entity.getBranchId().trim(), MatchMode.ANYWHERE));
            	}
            	
              	if (!ObjectUtil.isListEmpty(entity.getBranchesList())) {
					criteria.add(Restrictions.in("branchId", entity.getBranchesList()));
				}            	
            	if(!StringUtil.isEmpty(entity.getCode())){
            		criteria.add(Restrictions.like("code", entity.getCode().trim(), MatchMode.ANYWHERE));
            	}
            	if(!StringUtil.isEmpty(entity.getName())){
            		criteria.add(Restrictions.like("name", entity.getName().trim(), MatchMode.ANYWHERE));
            	}
            	if(!StringUtil.isEmpty(entity.getUnit())){
            		criteria.add(Restrictions.like("unit", entity.getUnit().trim(), MatchMode.ANYWHERE));
            	}
            	if(!StringUtil.isEmpty(entity.getCropCategory())){
            		criteria.add(Restrictions.like("cropCategory", entity.getCropCategory().trim(),MatchMode.ANYWHERE));
            	}
             	if(!StringUtil.isEmpty(entity.getMspRate())){
             	      criteria.add(Restrictions.eq("mspRate", entity.getMspRate()));
                 }
             	if(!StringUtil.isEmpty(entity.getMspPercentage())){
            		criteria.add(Restrictions.eq("mspPercentage", entity.getMspPercentage()));
            	}
        	
        	// sorting direction
        				String dir = (String) params.get(DIR);
        				// sorting column
        				String sort = (String) params.get(SORT_COLUMN);
        				// sort a column in the given direction ascending/descending
        				if (dir != null && sort != null) {
        					Criteria sortCriteria = null;
        					if (sort.contains(DELIMITER)) {
        						sort = sort.substring(sort.lastIndexOf(DELIMITER) + 1);
        						if (dir.equals(DESCENDING)) {
        							// sort descending
        							sortCriteria.addOrder(Order.desc(sort));
        						} else {
        							// sort ascending
        							sortCriteria.addOrder(Order.asc(sort));
        						}
        					}		
			}
        }
    }
    }
}
