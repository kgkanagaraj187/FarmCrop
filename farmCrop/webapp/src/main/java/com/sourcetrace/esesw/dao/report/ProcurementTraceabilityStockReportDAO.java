/*
 * ProcurementStockReportDAO.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.ese.entity.traceability.ProcurementTraceabilityStock;
import com.ese.entity.traceability.ProcurementTraceabilityStockDetails;
import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.order.entity.txn.CityWarehouse;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class ProcurementTraceabilityStockReportDAO extends ReportDAO {

	@SuppressWarnings("unchecked")
	protected void addExampleFiltering(Criteria criteria, Map params) {

		// check for filter entity
		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
		String tenantId = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID) ? ISecurityFilter.DEFAULT_TENANT_ID
				: "";
		if (!ObjectUtil.isEmpty(request)) {
			tenantId = !StringUtil.isEmpty((String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID))
					? (String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID) : "";
		}
		ProcurementTraceabilityStockDetails entity = (ProcurementTraceabilityStockDetails) params.get(FILTER);
		criteria.createAlias("procurementTraceabilityStock", "ps");
		criteria.createAlias("ps.coOperative", "co");
		criteria.createAlias("ps.procurementProduct", "pp");
		criteria.createAlias("farmer", "f");
		criteria.createAlias("ps.village", "v");
		criteria.createAlias("ps.city", "c");
		if (!ObjectUtil.isEmpty(entity)) {
			if (!ObjectUtil.isEmpty(entity.getProcurementTraceabilityStock())) {
				if (!ObjectUtil.isEmpty(entity.getProcurementTraceabilityStock().getCoOperative())){
 						if(entity.getProcurementTraceabilityStock().getCoOperative().getId() == 0) {
					criteria.add(Restrictions.isNotNull("ps.coOperative"));
				} else {
					criteria.add(Restrictions.eq("co.id",
							entity.getProcurementTraceabilityStock().getCoOperative().getId()));
				}
				}
				if (!ObjectUtil.isEmpty(entity.getProcurementTraceabilityStock().getProcurementProduct())
						&& entity.getProcurementTraceabilityStock().getProcurementProduct().getId() != 0) {
					criteria.add(Restrictions.eq("pp.id",
							entity.getProcurementTraceabilityStock().getProcurementProduct().getId()));
				}
				if (!StringUtil.isEmpty(entity.getProcurementTraceabilityStock().getIcs())) {
					criteria.add(Restrictions.eq("ps.ics", entity.getProcurementTraceabilityStock().getIcs()));
				}
				if (!ObjectUtil.isEmpty(entity.getProcurementTraceabilityStock().getVillage())){
					if(entity.getProcurementTraceabilityStock().getVillage().getId() == 0) {
				criteria.add(Restrictions.isNotNull("ps.village"));
			} else {
				criteria.add(Restrictions.eq("v.id",
						entity.getProcurementTraceabilityStock().getVillage().getId()));
			}
			}
				if (!ObjectUtil.isEmpty(entity.getProcurementTraceabilityStock().getCity())){
					if(entity.getProcurementTraceabilityStock().getCity().getId() == 0) {
				criteria.add(Restrictions.isNotNull("ps.city"));
			} else {
				criteria.add(Restrictions.eq("c.id",
						entity.getProcurementTraceabilityStock().getCity().getId()));
			}
			}
				
				if (!ObjectUtil.isEmpty(entity.getProcurementTraceabilityStock().getSeason())){
					criteria.add(Restrictions.like("ps.season", entity.getProcurementTraceabilityStock().getSeason(), MatchMode.EXACT));
			}
				
			}
			if (entity.getFarmer()!=null && !StringUtil.isEmpty(entity.getFarmer().getFarmerId())) {
				criteria.add(Restrictions.eq("f.farmerId", entity.getFarmer().getFarmerId()));
			}
			if (entity.getFarmer()!=null && !StringUtil.isEmpty(entity.getFarmer().getFpo())) {
				criteria.add(Restrictions.eq("f.fpo", entity.getFarmer().getFpo()));
			}
			if (!StringUtil.isEmpty(entity.getBranchId())) {
				criteria.add(Restrictions.like("branchId", entity.getBranchId(), MatchMode.EXACT));
			}
			criteria.add(Restrictions.gt("ps.totalStock", 0d));

			// sort a column in the given direction ascending/descending
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
