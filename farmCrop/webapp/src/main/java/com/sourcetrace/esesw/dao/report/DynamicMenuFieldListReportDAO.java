package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.entity.DynamicFeildMenuConfig;
import com.sourcetrace.eses.util.StringUtil;

public class DynamicMenuFieldListReportDAO extends ReportDAO  {



	
	protected void addExampleFiltering(Criteria criteria, Map params) {

		// check for filter entity
		DynamicFeildMenuConfig entity = (DynamicFeildMenuConfig) params.get(FILTER);
		if (entity != null) {
						
			if (entity.getName() != null && !"".equals(entity.getName()))
                criteria.add(Restrictions.like("name", entity.getName(),
                        MatchMode.ANYWHERE));
			
			
			if (!StringUtil.isEmpty(entity.getEntity())) {
				criteria.add(Restrictions.eq("entity", entity.getEntity()));
			}
			
			// sorting direction
			String dir = (String) params.get(DIR);
			// sorting column
			String sort = (String) params.get(SORT_COLUMN);
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
