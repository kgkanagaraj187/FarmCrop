package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.entity.SamithiIcs;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.esesw.entity.profile.Farmer;

public class SamithiIcsReportDAO extends ReportDAO {
	
	protected void addExampleFiltering(Criteria criteria, Map params) {
	 SamithiIcs entity = (SamithiIcs) params.get(FILTER);
     if (entity != null) {
     	 
         criteria.createAlias("warehouse", "s");   
        
         if (!ObjectUtil.isEmpty(entity) && !ObjectUtil.isEmpty(entity.getWarehouse()) && entity.getWarehouse().getId()!=0)
             criteria.add(Restrictions.eq("s.id", entity.getWarehouse().getId()));
       
     }
 }
}
