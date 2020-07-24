package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.order.entity.txn.Distribution;
import com.sourcetrace.eses.order.entity.txn.DistributionDetail;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class DistributionToFiledStaffDAO extends ReportDAO {
	
		
	    protected void addExampleFiltering(Criteria criteria, Map params) {
	   
	      
	        DistributionDetail entity = (DistributionDetail) params.get(FILTER);

	        if (entity != null) {
	        	
	        	criteria.createAlias("distribution", "d");
	        	criteria.createAlias("product", "p").createAlias("p.subcategory", "c");
	        
	            if (!StringUtil.isEmpty(entity.getBranchId())) {
					criteria.add(Restrictions.like("d.branchId", entity.getBranchId(), MatchMode.EXACT));
				}
	        	if (!ObjectUtil.isListEmpty(entity.getBranchesList())) {
					criteria.add(Restrictions.in("d.branchId", entity.getBranchesList()));
				}
	            
	            if (entity.getWarehouseName() != null && !"".equals(entity.getWarehouseName()))
	                criteria.add(Restrictions.like("d.servicePointName", entity.getWarehouseName(), MatchMode.ANYWHERE));

	            if (entity.getAgentName() != null && !"".equals(entity.getAgentName()))
	                criteria.add(Restrictions.like("d.agentName", entity.getAgentName(), MatchMode.ANYWHERE));

	         
	            		
	           if (entity.getProductName() != null && entity.getProductName() != null
	                     && !"".equals(entity.getProductName())) 
	             criteria.add(Restrictions.like("p.name",entity.getProductName(),
	                      MatchMode.ANYWHERE)); }
	          
	        if (entity.getCategoryName() != null && !"".equals(entity.getCategoryName()))
                criteria.add(Restrictions.like("c.name", entity.getCategoryName(), MatchMode.ANYWHERE));
	        	criteria.add(Restrictions.eq("d.txnType",Distribution.PRODUCT_DISTRIBUTION_TO_FIELDSTAFF));
	        	
	        if(entity.getEnableApproved().contentEquals("1")){
	        	criteria.add(Restrictions.eq("d.status",Distribution.NOT_APPROVED));
	        }
	        
	        if(entity.getDistribution()!=null && entity.getDistribution().getBranchId()!=null && !StringUtil.isEmpty(entity.getDistribution().getBranchId())){
	            criteria.add(Restrictions.like("d.branchId",entity.getDistribution().getBranchId(), MatchMode.EXACT));
	        }
	  	  criteria.add(Restrictions.ne("d.status",Distribution.DELETE_STATUS));

	        
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
