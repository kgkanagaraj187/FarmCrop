package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.order.entity.txn.Distribution;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class DistributionToFarmerDAO extends ReportDAO {

	@Override
	protected void addExampleFiltering(Criteria criteria, Map params) {

		Distribution entity = (Distribution) params.get(FILTER);
		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
		String tenantId = ISecurityFilter.DEFAULT_TENANT_ID;
		if (!ObjectUtil.isEmpty(request)) {
			tenantId = (String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID);
		}
		if (StringUtil.isEmpty(tenantId)) {
			tenantId = ISecurityFilter.DEFAULT_TENANT_ID;
		}
		if (entity != null) {
			criteria.createAlias("village", "v");

			if (!StringUtil.isEmpty(entity.getBranchId())) {
				criteria.add(Restrictions.like("branchId", entity.getBranchId(), MatchMode.EXACT));
			}
        	if (!ObjectUtil.isListEmpty(entity.getBranchesList())) {
				criteria.add(Restrictions.in("branchId", entity.getBranchesList()));
			}
			if (entity.getFarmerType() != null && !"".equals(entity.getFarmerType())) {
				Criterion name = Restrictions.like("farmerName", entity.getFarmerType(), MatchMode.ANYWHERE);
				Criterion farmerId = Restrictions.eq("farmerId", "NA");
				LogicalExpression and = Restrictions.and(name, farmerId);
				criteria.add(and);

			}
			if (entity.getFarmerName() != null && !"".equals(entity.getFarmerName()))
				criteria.add(Restrictions.like("farmerName", entity.getFarmerName(), MatchMode.ANYWHERE));

			if (entity.getFatherName() != null && !"".equals(entity.getFatherName()))
				criteria.add(Restrictions.like("fatherName", entity.getFatherName(), MatchMode.ANYWHERE));

			if (entity.getAgentName() != null && !"".equals(entity.getAgentName()))
				criteria.add(Restrictions.like("agentName", entity.getAgentName(), MatchMode.ANYWHERE));

			if (entity.getVillage() != null && entity.getVillage().getName() != null
					&& !"".equals(entity.getVillage().getName()))
				criteria.add(Restrictions.like("v.name", entity.getVillage().getName(), MatchMode.ANYWHERE));
		}

		if (entity.getSeasonCode() != null && !"".equals(entity.getSeasonCode()))
			criteria.add(Restrictions.like("seasonCode", entity.getSeasonCode(), MatchMode.ANYWHERE));

		if (entity.getServicePointName() != null && !"".equals(entity.getServicePointName())) {
			if (tenantId.equals(ESESystem.PRATIBHA_TENANT_ID)) {
				criteria.add(Restrictions.like("warehouseName", entity.getServicePointName(), MatchMode.ANYWHERE));
			} else {
				criteria.add(Restrictions.like("servicePointName", entity.getServicePointName(), MatchMode.ANYWHERE));
			}
		}
		if (entity.getStockType() != null && !"".equals(entity.getStockType())) {
			if (entity.getStockType().equals("1"))
				criteria.add(Restrictions.isNotNull("servicePointName")).add(Restrictions.isNull("agentName"));
			else
				criteria.add(Restrictions.isNotNull("agentName")).add(Restrictions.isNull("servicePointName"));
		}

		/*
		 * criteria.add(Restrictions.eq("txnType",Distribution.
		 * PRODUCT_DISTRIBUTION_TO_FARMER));
		 */
		criteria.add(Restrictions.in("txnType", new Object[] { Distribution.PRODUCT_DISTRIBUTION_TO_FARMER,
				Distribution.PRODUCT_RETURN_FROM_FARMER, Distribution.PRODUCT_DISTRIBUTION_FARMER_BALANCE }));
		// criteria.add(Restrictions.eq("freeDistribution","0"));

		if (entity.getEnableApproved().contentEquals("1")) {
			criteria.add(Restrictions.eq("status", Distribution.NOT_APPROVED));
		}

		criteria.add(Restrictions.ne("status", Distribution.DELETE_STATUS));

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
