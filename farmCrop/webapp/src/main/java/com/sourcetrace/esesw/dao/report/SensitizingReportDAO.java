package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.order.entity.txn.Sensitizing;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class SensitizingReportDAO extends ReportDAO{
	protected void addExampleFiltering(Criteria criteria, Map params) {
		Sensitizing entity = (Sensitizing) params.get(FILTER);
		
		if(!ObjectUtil.isEmpty(entity)){
			if(!StringUtil.isEmpty(entity.getGroupId())){
				criteria.add(Restrictions.eq("groupId", entity.getGroupId()));
			}
		}
		
		String dir = (String) params.get(DIR);
		String sort = (String) params.get(SORT_COLUMN);

		if (dir != null && sort != null) {
			criteria.addOrder(Order.desc(sort));

		}
	}
}
