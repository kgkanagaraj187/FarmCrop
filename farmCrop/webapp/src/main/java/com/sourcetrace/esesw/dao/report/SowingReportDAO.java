package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.FarmCrops;

public class SowingReportDAO extends ReportDAO{

	
	
    protected void addExampleFiltering(Criteria criteria, Map params) {
    	 
      
    	FarmCrops entity = (FarmCrops) params.get(FILTER);
    	
        if (entity != null) {
        criteria.createAlias("farm", "f",Criteria.LEFT_JOIN).createAlias("f.farmDetailedInfo", "fdi").createAlias("f.farmer", "fr",Criteria.LEFT_JOIN);
        criteria.createAlias("procurementVariety", "pv",Criteria.LEFT_JOIN).createAlias("pv.procurementProduct", "pp");
        criteria.createAlias("fr.village", "v");
        criteria.createAlias("cropSeason", "hs");
        criteria.createAlias("fr.samithi", "s");
        
        HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
        Object object = httpSession.getAttribute(ISecurityFilter.IS_MULTI_BRANCH_APP);
		String multibranch = ObjectUtil.isEmpty(object) ? "" : object.toString();
		
		if (multibranch.equals("1")) {
			Object object1 = httpSession.getAttribute(ISecurityFilter.MAPPED_BRANCHES);
			if (object1 != null) {
				String[] branches = object1.toString().split(",");

				if (branches != null && branches.length > 0) {
					criteria.add(Restrictions.in("branchId", branches));
				}
			}
		} else {
			Object object2= httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
			String currentBranch1 = ObjectUtil.isEmpty(object2) ? "" : object2.toString();
			if (!StringUtil.isEmpty(currentBranch1)) {
				criteria.add(Restrictions.like("branchId", currentBranch1, MatchMode.EXACT));
			}
		}
		
    	if (!ObjectUtil.isListEmpty(entity.getBranchesList())) {
			criteria.add(Restrictions.in("branchId", entity.getBranchesList()));
		}
		
		if (!StringUtil.isEmpty(entity.getBranchId())){
		    criteria.add(Restrictions.eq("branchId", entity.getBranchId()));
		}
		
        if (entity.getProcurementproductId()>0)
            criteria.add(Restrictions.eq("pp.id", entity.getProcurementproductId()));
        if (entity.getFarmerId()>0)
            criteria.add(Restrictions.eq("fr.id", entity.getFarmerId()));
       if(!StringUtil.isEmpty(entity.getFarmerCode()))
        	 criteria.add(Restrictions.like("fr.farmerId", entity.getFarmerCode(), MatchMode.ANYWHERE));
        
        /*if(!StringUtil.isEmpty(entity.getFarmerCode()))
       	 criteria.add(Restrictions.like("fr.farmerId", entity.getFarmerCode()));*/
        
        if(entity.getCropId()>0){
        	criteria.add(Restrictions.eq("pp.id", entity.getCropId()));
        }
        if(entity.getSamithiId()>0){
        	criteria.add(Restrictions.eq("s.id", entity.getSamithiId()));
        }
        if(entity.getSeason()>0){
        	criteria.add(Restrictions.eq("hs.id", entity.getSeason()));
        }      
         if (!StringUtil.isEmpty(entity.getIcsName())){
            criteria.add(Restrictions.eq("fr.icsName", entity.getIcsName()));
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