/*
 * CoOperativeReportDAO.java
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

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class CooperativeReportDAO extends ReportDAO {

    /**
     * Adds the example filtering.
     * @param criteria the criteria
     * @param params the params
     */
    protected void addExampleFiltering(Criteria criteria, Map params) {

        // check for filter entity
        Warehouse entity = (Warehouse) params.get(FILTER);
		HttpServletRequest httpRequest = ReflectUtil.getCurrentHttpRequest();
		String tenantId = (String) httpRequest.getSession().getAttribute(ISecurityFilter.TENANT_ID);
		
        if (entity != null) {
        	
        	if (!ObjectUtil.isListEmpty(entity.getBranchesList())) {
				criteria.add(Restrictions.in("branchId", entity.getBranchesList()));
			}
			
			if (!StringUtil.isEmpty(entity.getBranchId())){
			    criteria.add(Restrictions.eq("branchId", entity.getBranchId()));
			}
        	

            if (entity.getName() != null && !"".equals(entity.getName()))
                criteria.add(Restrictions.like("name", entity.getName(), MatchMode.ANYWHERE));
            if (entity.getCode() != null && !"".equals(entity.getCode()))
                criteria.add(Restrictions.like("code", entity.getCode(), MatchMode.ANYWHERE));
            if(entity.getRefCooperative()==null)
                criteria.add(Restrictions.isNull("refCooperative"));
            

            
            if(!StringUtil.isEmpty(tenantId) && (tenantId.equalsIgnoreCase("chetna") || tenantId.equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID))){ 
            	criteria.add(Restrictions.ne("typez",Warehouse.SAMITHI));
            }else{
            	criteria.add(Restrictions.eq("typez",Warehouse.COOPERATIVE));
           }
            
            if (entity.getFilterStatus() != null && !"".equals(entity.getFilterStatus())) {
				if (entity.getFilterStatus().equals("WarehouseType")) {
					criteria.add(Restrictions.like("typez", entity.getTypez()));
				}
			}
            
            if (entity.getLocation()!= null && !"".equals(entity.getLocation()))
                criteria.add(Restrictions.like("location", entity.getLocation(), MatchMode.ANYWHERE));
            
            if (entity.getWarehouseInCharge() != null && !"".equals(entity.getWarehouseInCharge()))
                criteria.add(Restrictions.like("warehouseInCharge", entity.getWarehouseInCharge(), MatchMode.ANYWHERE));
            
            if (entity.getCapacityInTonnes()!= null && !"".equals(entity.getCapacityInTonnes()))
                criteria.add(Restrictions.like("capacityInTonnes", entity.getCapacityInTonnes(), MatchMode.ANYWHERE));
            
            if(entity.getWarehouse_type() != null && !"".equals(entity.getWarehouse_type())){
            	criteria.add(Restrictions.eq("warehouse_type",entity.getWarehouse_type()));
            	
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
