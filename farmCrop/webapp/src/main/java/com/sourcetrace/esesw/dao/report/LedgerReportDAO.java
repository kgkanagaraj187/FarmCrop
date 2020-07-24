package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.ese.entity.traceability.GinningProcess;
import com.ese.entity.traceability.LedgerData;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class LedgerReportDAO extends ReportDAO{
	
	protected void addExampleFiltering(Criteria criteria, Map params) {
		// check for filter entity
		Object obj = (Object) params.get(FILTER);
		if (obj instanceof LedgerData) {
			
		}
	}
	}
