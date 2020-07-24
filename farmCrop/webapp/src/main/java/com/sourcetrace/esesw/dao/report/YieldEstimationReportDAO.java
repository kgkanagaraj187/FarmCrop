package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.FarmCrops;

public class YieldEstimationReportDAO extends ReportDAO{
	protected void addExampleFiltering(Criteria criteria, Map params) {
		FarmCrops entity = (FarmCrops) params.get(FILTER);
		
		if(!ObjectUtil.isEmpty(entity)){
			
			criteria.createAlias("farm", "fa");
			criteria.createAlias("fa.farmer", "fr");
			criteria.createAlias("fr.village","v");
			criteria.createAlias("procurementVariety", "pv");
            criteria.createAlias("pv.procurementProduct", "pp");
            criteria.createAlias("fr.samithi", "s");
            criteria.createAlias("cropSeason", "hs");
            
			
			/*if(!ObjectUtil.isEmpty(entity.getProcurementVariety()) && !StringUtil.isEmpty(entity.getProcurementVariety().getHarvestDays()))
			{
				criteria.add(Restrictions.le("pv.harvestDays",entity.getProcurementVariety().getHarvestDays()));
						
			}
			*/
			if(!ObjectUtil.isListEmpty(entity.getCropIdsList()))
			{
				criteria.add(Restrictions.in("id",entity.getCropIdsList()));
						
			}
		/*	
			if(!ObjectUtil.isEmpty(entity.getProcurementVariety()) && !StringUtil.isEmpty(entity.getProcurementVariety().getCode()))
			{
				
				criteria.add(Restrictions.eq("pv.code",entity.getProcurementVariety().getCode()));
			}*/
			
			/*if(!ObjectUtil.isEmpty(entity.getCropSeason()) && !StringUtil.isEmpty(entity.getCropSeason().getCode())){
	        	criteria.add(Restrictions.eq("hs.code", entity.getCropSeason().getCode()));
	        }    */
			
			 if (entity.getProcurementproductId()>0)
		            criteria.add(Restrictions.eq("pp.id", entity.getProcurementproductId()));
		        if (entity.getFarmerId()>0)
		            criteria.add(Restrictions.eq("fr.id", entity.getFarmerId()));
			
			
			String dir = (String) params.get(DIR);
			String sort = (String) params.get(SORT_COLUMN);

			if (dir != null && sort != null) {
				criteria.addOrder(Order.desc(sort));

			}
		}
	}
}
