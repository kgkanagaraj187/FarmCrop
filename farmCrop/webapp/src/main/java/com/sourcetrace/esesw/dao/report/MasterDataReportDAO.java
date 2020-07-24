package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.entity.MasterData;

public class MasterDataReportDAO extends ReportDAO 
{
	protected void addExampleFiltering(Criteria criteria, Map params) 
	{
        // Check for filter entity
        MasterData entity = (MasterData) params.get(FILTER);
      
        if (entity != null) 
        {
            if (entity.getCode() != null && !"".equals(entity.getCode()))
                criteria.add(Restrictions.like("code", entity.getCode(), MatchMode.ANYWHERE));

            if (entity.getName() != null && !"".equals(entity.getName()))
                criteria.add(Restrictions.like("name", entity.getName(), MatchMode.ANYWHERE));
            
            if (entity.getMasterType()!= null && !"".equals(entity.getMasterType()))
                criteria.add(Restrictions.eq("masterType", entity.getMasterType()));

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
