package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicData;

public class IcsFarmerDairyReportDAO extends ReportDAO {
	protected void addExampleFiltering(Criteria criteria, Map params) {

		FarmerDynamicData entity = (FarmerDynamicData) params.get(FILTER);

		// criteria.createAlias("farmerDynamicFieldsValues", "fdfv");

		/*if (entity != null) {

			if (!StringUtil.isEmpty(entity.getReferenceId())) {
				criteria.add(Restrictions.like("farmerId",
						entity.getFarmerDynamicFieldsValues().iterator().next().getReferenceId(), MatchMode.ANYWHERE));
			}
		}*/

	}

}
