package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.FarmerLandDetails;

public class FarmHistoryListReportDAO extends ReportDAO {
	

	protected void addExampleFiltering(Criteria criteria, Map params) {

	// check for filter entity
		FarmerLandDetails entity = (FarmerLandDetails) params.get(FILTER);

	if (entity != null) {
		
		HttpSession httpSession = ReflectUtil.getCurrentHttpSession();

		Object object = httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
		String currentBranch = ObjectUtil.isEmpty(object) ? "" : object.toString();

		if (!StringUtil.isEmpty(currentBranch)) {
			criteria.add(Restrictions.like("branchId", currentBranch));
		}

			criteria.createAlias("farmId", "f", Criteria.LEFT_JOIN).createAlias("f.farmer","fa",Criteria.LEFT_JOIN);
		
	    if(!ObjectUtil.isEmpty(entity.getFarmId()) && !ObjectUtil.isEmpty(entity.getFarmId().getFarmer())){
		    criteria.add(Restrictions.eq("fa.id",entity.getFarmId().getFarmer()
				.getId()));
		}
	    
	    if (!StringUtil.isEmpty(entity.getYear()))
            criteria.add(Restrictions.like("year", entity.getYear(), MatchMode.ANYWHERE));

	    if (!StringUtil.isEmpty(entity.getCrops()))
            criteria.add(Restrictions.like("crops", entity.getCrops(), MatchMode.ANYWHERE));
		
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
