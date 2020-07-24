package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.AgentCashFlow;

public class AgentCashFlowListReportDAO extends ReportDAO{
	
	 @SuppressWarnings("unchecked")
	protected void addExampleFiltering(Criteria criteria, Map params) {

		// check for filter entity
		AgentCashFlow entity = (AgentCashFlow) params.get(FILTER);
		

		if (!ObjectUtil.isEmpty(entity)) {
			
				if (!StringUtil.isEmpty(entity.getAgentId())) {
					criteria.add(Restrictions.like("agentId", entity
							.getAgentId(), MatchMode.ANYWHERE));
				}
				if (!StringUtil.isEmpty(entity.getAgentName())) {
					criteria.add(Restrictions.like("agentName", entity
							.getAgentName(), MatchMode.ANYWHERE));
				}
				if(!StringUtil.isEmpty(entity.getBranch())){
					criteria.add(Restrictions.like("branch", entity.getBranch(),MatchMode.ANYWHERE));
				}
				//criteria.add(Restrictions.eq("warehouseId", entity.getWarehouseId()));
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
