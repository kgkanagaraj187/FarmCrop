package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.entity.FarmerFeedbackEntity;
import com.sourcetrace.eses.util.StringUtil;

public class FarmerFeedBackReportDAO extends ReportDAO {
	protected void addExampleFiltering(Criteria criteria, Map params) {

		FarmerFeedbackEntity entity = (FarmerFeedbackEntity) params.get(FILTER);
		if (entity != null) {

			if (entity.getFarmerId() != null && !StringUtil.isEmpty(entity.getFarmerId())) {

				criteria.add(Restrictions.eq("farmerId", entity.getFarmerId()));
			}

			if (entity.getVillage() != null && !StringUtil.isEmpty(entity.getVillage().getCode())) {
				criteria.createAlias("village", "v");

				criteria.add(Restrictions.eq("v.code", entity.getVillage().getCode()));
			}

			if (entity.getWarehouse() != null && !StringUtil.isEmpty(entity.getWarehouse().getCode())) {
				criteria.createAlias("warehouse", "v");

				criteria.add(Restrictions.eq("v.code", entity.getWarehouse().getCode()));
			}

			/*
			 * // sorting direction String dir = (String) params.get(DIR); //
			 * sorting column String sort = (String) params.get(SORT_COLUMN); if
			 * (dir.equals(DESCENDING)) { // sort descending
			 * criteria.addOrder(Order.desc(sort)); } else { // sort ascending
			 * criteria.addOrder(Order.asc(sort)); }
			 */

		}
	}

}
