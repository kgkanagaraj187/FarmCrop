/*
 * AnimalHusbandaryReportDAO.java
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
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.esesw.entity.profile.AnimalHusbandary;
import com.sourcetrace.esesw.entity.profile.DistributionBalance;

public class DistributionBalanceReportDAO extends ReportDAO {

    protected void addExampleFiltering(Criteria criteria, Map params) {

        // check for filter entity
        DistributionBalance entity = (DistributionBalance) params.get(FILTER);

        if (entity != null) {

        	criteria.createAlias("farmer", "f", criteria.LEFT_JOIN);
			criteria.createAlias("product", "p", criteria.LEFT_JOIN);
			
        	if(!ObjectUtil.isEmpty(entity.getFarmer())){
        	if (entity.getFarmer().getFirstName() != null && !"".equals(entity.getFarmer().getFirstName()))
				criteria.add(Restrictions.like("f.firstName", entity.getFarmer().getFirstName(), MatchMode.ANYWHERE));
        	}
        	
        	if(!ObjectUtil.isEmpty(entity.getProduct())){
            	if (entity.getProduct().getName() != null && !"".equals(entity.getProduct().getName()))
    				criteria.add(Restrictions.like("p.name", entity.getProduct().getName(), MatchMode.ANYWHERE));
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
