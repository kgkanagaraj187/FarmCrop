package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.entity.DynamicFieldConfig;
import com.sourcetrace.eses.util.ObjectUtil;

public class CreationFarmerReportDAO extends ReportDAO {
	protected void addExampleFiltering(Criteria criteria, Map params) {
		
		  DynamicFieldConfig entity = (DynamicFieldConfig) params.get(FILTER);
		  criteria.createAlias("dynamicSectionConfig", "section");
	        if (entity != null)
	        {
	        	

	        	if (!ObjectUtil.isEmpty(entity.getDynamicSectionConfig())) {
					criteria.add(Restrictions.eq("section.sectionCode", entity.getDynamicSectionConfig().getSectionCode()));
				}
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
