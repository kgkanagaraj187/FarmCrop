package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.order.entity.txn.Distribution;
import com.sourcetrace.eses.order.entity.txn.ProductReturn;
import com.sourcetrace.eses.util.StringUtil;

public class ProductReturnFromFarmerDAO  extends ReportDAO{

	
	
    protected void addExampleFiltering(Criteria criteria, Map params) {
   
      
    	ProductReturn entity = (ProductReturn) params.get(FILTER);
    	
        if (entity != null) {
        	criteria.createAlias("village", "v");
        
           
            if (!StringUtil.isEmpty(entity.getBranchId())) {
				criteria.add(Restrictions.like("branchId", entity.getBranchId(), MatchMode.EXACT));
			}
            
            if (entity.getFarmerType() != null && !"".equals(entity.getFarmerType())){
            	Criterion name=Restrictions.like("farmerName", entity.getFarmerType(), MatchMode.ANYWHERE);
            	Criterion farmerId=Restrictions.eq("farmerId", "NA");
            	LogicalExpression and=Restrictions.and(name, farmerId);
                criteria.add(and);
                
            }
            if (entity.getFarmerName() != null && !"".equals(entity.getFarmerName()))
                criteria.add(Restrictions.like("farmerName", entity.getFarmerName(), MatchMode.ANYWHERE));
            

            if (entity.getAgentName() != null && !"".equals(entity.getAgentName()))
                criteria.add(Restrictions.like("agentName", entity.getAgentName(), MatchMode.ANYWHERE));
            		
           if (entity.getVillage() != null && entity.getVillage().getName() != null
                     && !"".equals(entity.getVillage().getName())) 
             criteria.add(Restrictions.like("v.name",entity.getVillage().getName(),
                      MatchMode.ANYWHERE)); }
          
        if (entity.getSeasonCode() != null && !"".equals(entity.getSeasonCode()))
            criteria.add(Restrictions.like("seasonCode", entity.getSeasonCode(), MatchMode.ANYWHERE));
        
        if (entity.getServicePointName() != null && !"".equals(entity.getServicePointName()))
            criteria.add(Restrictions.like("servicePointName", entity.getServicePointName(), MatchMode.ANYWHERE));
        
        	
        	criteria.add(Restrictions.eq("txnType",ProductReturn.PRODUCT_DISTRIBUTION_TO_FARMER));
        	//criteria.add(Restrictions.eq("freeDistribution","0"));
        	
        if(entity.getEnableApproved().contentEquals("1")){
        	criteria.add(Restrictions.eq("status",ProductReturn.NOT_APPROVED));
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
