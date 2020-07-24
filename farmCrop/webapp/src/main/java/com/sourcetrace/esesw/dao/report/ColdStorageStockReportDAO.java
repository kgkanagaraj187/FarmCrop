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

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.order.entity.txn.CityWarehouse;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class ColdStorageStockReportDAO extends ReportDAO {

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
		CityWarehouse entity = (CityWarehouse) params.get(FILTER);
		criteria.createAlias("coOperative", "co");
		criteria.createAlias("procurementProduct", "pp");
		criteria.createAlias("pp.procurementVarieties", "pv");
		criteria.createAlias("pv.procurementGrades", "pg");

		if (tenantId.equalsIgnoreCase("lalteer")) {
			criteria.createAlias("farmer", "f",criteria.LEFT_JOIN);
		}
		
		if (entity != null) {

			if (entity.getCoOperative().getId() == 0) {
				criteria.add(Restrictions.isNotNull("coOperative"));
			} else {
				criteria.add(Restrictions.eq("co.id", entity.getCoOperative().getId()));
			}

			/*
			 * if(!ObjectUtil.isEmpty(entity.getCoOperative())) {
			 * criteria.add(Restrictions.eq("co.code",
			 * entity.getCoOperative().getCode())); }
			 */

			if (!ObjectUtil.isEmpty(entity.getProcurementProduct()) && !ObjectUtil.isEmpty(entity.getProcurementProduct().getId())
					&& entity.getProcurementProduct().getId()!=0) {
				criteria.add(Restrictions.eq("pp.id", entity.getProcurementProduct().getId()));
			}

			if (!StringUtil.isEmpty(entity.getBranchId())) {
				criteria.add(Restrictions.like("branchId", entity.getBranchId(), MatchMode.EXACT));
			}

			if (!ObjectUtil.isEmpty(entity.getFarmer()) && !StringUtil.isEmpty(entity.getFarmer().getFarmerId())) {
				criteria.add(Restrictions.like("f.farmerId", entity.getFarmer().getFarmerId()));
			}

			if (!StringUtil.isEmpty(entity.getQuality())) {
				criteria.add(Restrictions.eq("quality", entity.getQuality()));
			}

			criteria.add(Restrictions.eq("isDelete", 0));
			criteria.add(Restrictions.gt("grossWeight", 0d));

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
