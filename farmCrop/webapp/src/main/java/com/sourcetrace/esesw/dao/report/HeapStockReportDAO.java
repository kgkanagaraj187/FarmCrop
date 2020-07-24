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
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.ese.entity.traceability.HeapData;
import com.ese.entity.traceability.HeapDataDetail;
import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.order.entity.txn.CityWarehouse;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class HeapStockReportDAO extends ReportDAO {

	@SuppressWarnings("unchecked")
	protected void addExampleFiltering(Criteria criteria, Map params) {

		// check for filter entity
		HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
		String tenantId = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID) ? ISecurityFilter.DEFAULT_TENANT_ID
				: "";
		Object object2 = httpSession.getAttribute(ISecurityFilter.IS_MULTI_BRANCH_APP);
		String multibranch = ObjectUtil.isEmpty(object2) ? "" : object2.toString();
		
		if (!ObjectUtil.isEmpty(request)) {
			tenantId = !StringUtil.isEmpty((String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID))
					? (String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID) : "";
		}
		
		HeapData entity = (HeapData) params.get(FILTER);
		criteria.createAlias("heapDataDetail", "hd");
		criteria.createAlias("ginning", "g");
		criteria.createAlias("procurementProduct", "pd");

		criteria.add(Restrictions.eq("hd.stockType", HeapData.stock.HEAP.ordinal()));
		
		if (entity != null) {

			if (!ObjectUtil.isEmpty(entity.getGinning()) && !StringUtil.isEmpty(entity.getGinning().getId())) {
				criteria.add(Restrictions.like("g.id", entity.getGinning().getId()));
			}

			if (!ObjectUtil.isEmpty(entity.getProcurementProduct()) && !StringUtil.isEmpty(entity.getProcurementProduct().getId())) {
				criteria.add(Restrictions.like("pd.id", entity.getProcurementProduct().getId()));
			}
			
			if (!StringUtil.isEmpty(entity.getIcs())) {
				criteria.add(Restrictions.like("ics", entity.getIcs(),MatchMode.ANYWHERE));
			}
			
			if (!StringUtil.isEmpty(entity.getName())) {
				criteria.add(Restrictions.like("name", entity.getName()));
			}
			if (multibranch.equals("1")) {
				Object object1 = httpSession.getAttribute(ISecurityFilter.MAPPED_BRANCHES);
				if (object1 != null) {
					String[] branches = object1.toString().split(",");

					if (branches != null && branches.length > 0) {
						criteria.add(Restrictions.in("branchId", branches));
					}
				}
			} else {
				Object object3 = httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
				String currentBranch1 = ObjectUtil.isEmpty(object3) ? "" : object3.toString();
				if (!StringUtil.isEmpty(currentBranch1)) {
					criteria.add(Restrictions.like("branchId", currentBranch1, MatchMode.EXACT));
				}
			}

			
			if (!StringUtil.isEmpty(entity.getBranchId())) {
				criteria.add(Restrictions.like("branchId", entity.getBranchId(), MatchMode.EXACT));
			}
			criteria.add(Restrictions.like("season", entity.getSeason(),MatchMode.EXACT));
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
