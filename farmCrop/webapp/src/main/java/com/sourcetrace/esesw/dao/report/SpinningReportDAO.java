package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.ese.entity.traceability.GinningProcess;
import com.ese.entity.traceability.SpinningTransfer;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class SpinningReportDAO extends ReportDAO{
	
	protected void addExampleFiltering(Criteria criteria, Map params) {

		// check for filter entity
		Object obj = (Object) params.get(FILTER);
		if (obj instanceof SpinningTransfer) {
			SpinningTransfer entity = (SpinningTransfer) obj;
				criteria.createAlias("ginning", "g");
				criteria.createAlias("spinning", "s");

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
