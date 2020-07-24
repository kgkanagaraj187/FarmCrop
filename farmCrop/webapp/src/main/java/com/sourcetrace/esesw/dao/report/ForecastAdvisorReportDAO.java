package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.order.entity.txn.ForecastAdvisoryDetails;


public class ForecastAdvisorReportDAO extends ReportDAO{
	 protected void addExampleFiltering(Criteria criteria, Map params) {
		 ForecastAdvisoryDetails entity = (ForecastAdvisoryDetails) params.get(FILTER);
		 criteria.createAlias("forecastAdvisory", "f");
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
