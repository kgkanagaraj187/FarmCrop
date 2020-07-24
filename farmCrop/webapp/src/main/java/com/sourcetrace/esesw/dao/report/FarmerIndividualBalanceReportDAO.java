package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class FarmerIndividualBalanceReportDAO extends ReportDAO {

	@SuppressWarnings("rawtypes")
	@Override
	protected void addExampleFiltering(Criteria criteria, Map params) {
		AgroTransaction entity = (AgroTransaction) params.get(FILTER);

		if (!ObjectUtil.isEmpty(entity) && entity.getId() == 0) {
			criteria.createAlias("account", "account", JoinType.RIGHT_OUTER_JOIN);
		
				criteria.add(Restrictions.eq("account.type", 3));


		}
		if (!ObjectUtil.isEmpty(entity) && !StringUtil.isEmpty(entity.getFarmerId())) {
			criteria.add(Restrictions.like("farmerId", entity.getFarmerId()));
		}

		if (!ObjectUtil.isEmpty(entity) && !StringUtil.isEmpty(entity.getSeasonCode())) {
			criteria.add(Restrictions.like("seasonCode", entity.getSeasonCode()));
		}else
			criteria.add(Restrictions.eq("seasonCode", entity.getSeasonCode()));
		if (!ObjectUtil.isEmpty(entity) && entity.getId() > 0) {
			criteria.add(Restrictions.eq("account.id", entity.getId()));
		}
		if (!ObjectUtil.isEmpty(entity)) {
			criteria.add(Restrictions.in("txnType", new String[] { "314", "316", "334" }));
		}
		
		if(!StringUtil.isEmpty(entity.getBranchId())){
			criteria.add(Restrictions.eq("branchId", entity.getBranchId()));
		}
		
		if (ObjectUtil.isEmpty(entity)) {
			// From Date

			String dir = (String) params.get(DIR);
			String sort = "farmerId";
			if (dir != null && sort != null) {
				if (dir.equals(DESCENDING)) {
					criteria.addOrder(Order.desc(sort));
				} else {
					criteria.addOrder(Order.asc(sort));
				}
			}

		} else {
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

