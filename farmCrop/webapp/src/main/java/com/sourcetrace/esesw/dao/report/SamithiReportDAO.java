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
import com.sourcetrace.esesw.entity.profile.Farmer;

public class SamithiReportDAO extends ReportDAO {

    /**
     * Adds the example filtering.
     * @param criteria the criteria
     * @param params the params
     */
    protected void addExampleFiltering(Criteria criteria, Map params) {
    	
    	Object obj = (Object) params.get(FILTER);

		if (obj != null) {
			if (obj instanceof Warehouse) {
				  Warehouse entity = (Warehouse) params.get(FILTER);
				if (entity != null) {

		        	
		        	if (!ObjectUtil.isListEmpty(entity.getBranchesList())) {
						criteria.add(Restrictions.in("branchId", entity.getBranchesList()));
					}
		        	
		        	if (!StringUtil.isEmpty(entity.getBranchId())) {
						criteria.add(Restrictions.like("branchId", entity.getBranchId(), MatchMode.EXACT));
					}
		            //
		           // criteria.createAlias("refCooperative", "w");
		            if (entity.getName() != null && !"".equals(entity.getName()))
		                criteria.add(Restrictions.like("name", entity.getName(), MatchMode.ANYWHERE));
		            if (entity.getCode() != null && !"".equals(entity.getCode()))
		                criteria.add(Restrictions.like("code", entity.getCode(), MatchMode.ANYWHERE));
		            criteria.add(Restrictions.eq("typez",Warehouse.SAMITHI));
		            
		            
		            if (entity.getGroupType()!= null && !"".equals(entity.getGroupType()))
		                criteria.add(Restrictions.like("groupType", entity.getGroupType(), MatchMode.ANYWHERE));
		            
		           /* if (entity.getCity() != null && entity.getCity().getName() != null
		                    && !"".equals(entity.getCity().getName()))
		                criteria.add(Restrictions.like("c.name", entity.getCity().getName(),
		                        MatchMode.ANYWHERE));*/
		            
		            if (!ObjectUtil.isEmpty(entity) && !ObjectUtil.isEmpty(entity.getId()) && entity.getId()>0) {

						DetachedCriteria farmerCriteria = DetachedCriteria.forClass(Farmer.class);
						farmerCriteria.setProjection(Property.forName("id"));
						farmerCriteria.add(Restrictions.eq("samithi.id", entity.getId()));

						criteria.add(Property.forName("id").in(farmerCriteria));
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

       

       
    }
}
