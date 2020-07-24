package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.ese.entity.traceability.GinningProcess;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class GinningReportDAO extends ReportDAO{
	
	protected void addExampleFiltering(Criteria criteria, Map params) {
		// check for filter entity
		Object obj = (Object) params.get(FILTER);
		if (obj instanceof GinningProcess) {
			GinningProcess entity = (GinningProcess) obj;
				criteria.createAlias("ginning", "w");
				//criteria.createAlias("heapData", "h");
				criteria.createAlias("product", "p");

				if (!ObjectUtil.isEmpty(entity.getFilterData()) && entity.getFilterData().size() > 0) {
					entity.getFilterData().forEach((key, value) -> {
						if (!StringUtil.isEmpty(key)) {
							criteria.add(Restrictions.like(key, value, MatchMode.ANYWHERE));
						}
					});
				}

	}
	}
	}
