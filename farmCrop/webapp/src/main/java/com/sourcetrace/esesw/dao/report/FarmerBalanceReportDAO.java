package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farmer;

public class FarmerBalanceReportDAO extends ReportDAO {

	@SuppressWarnings("rawtypes")
	@Override
	protected void addExampleFiltering(Criteria criteria, Map params) {
		AgroTransaction entity = (AgroTransaction) params.get(FILTER);

		if (!ObjectUtil.isEmpty(entity) && entity.getId() == 0) {
			criteria.createAlias("account", "account", JoinType.RIGHT_OUTER_JOIN);
		
				criteria.add(Restrictions.eq("account.type", Integer.valueOf(entity.getAccount().getAccountType())));
				criteria.add(Restrictions.ne("account.status", Integer.valueOf(2)));
				
		}
		
		
		
		if (!ObjectUtil.isEmpty(entity) && !StringUtil.isEmpty(entity.getFarmerId())) {
			criteria.add(Restrictions.like("farmerId", entity.getFarmerId()));
		}
		
		if (!ObjectUtil.isEmpty(entity) && !StringUtil.isEmpty(entity.getAgentId())) {
			criteria.add(Restrictions.like("agentId", entity.getAgentId()));
		}
		
		/*if (!ObjectUtil.isEmpty(entity) && !StringUtil.isEmpty(entity.getSeasonCode())) {
			criteria.add(Restrictions.like("seasonCode", entity.getSeasonCode()));
		}else
			criteria.add(Restrictions.eq("seasonCode", entity.getSeasonCode()));*/
		
		if (!ObjectUtil.isEmpty(entity) && entity.getId() > 0) {
			criteria.add(Restrictions.eq("account.id", entity.getId()));
		}
		if (!ObjectUtil.isEmpty(entity)) {
			criteria.add(Restrictions.in("txnType", new String[] { "314", "316", "334", "334P", "334D" ,"383" }));
		}
		
		if(!StringUtil.isEmpty(entity.getBranch_id())){
			criteria.add(Restrictions.eq("branch_id", entity.getBranch_id()));
		}
	
		
		/**Group Filter Susagri**/
		if (!ObjectUtil.isEmpty(entity.getSamithiId()) && entity.getSamithiId()!=0) {

			DetachedCriteria farmerCriteria = DetachedCriteria.forClass(Farmer.class);
			farmerCriteria.setProjection(Property.forName("farmerId"));
			farmerCriteria.add(Restrictions.eq("samithi.id", entity.getSamithiId()));

			criteria.add(Property.forName("farmerId").in(farmerCriteria));
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

