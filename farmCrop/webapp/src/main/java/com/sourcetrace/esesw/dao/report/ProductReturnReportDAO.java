/*
 * DistributionReportDAO.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.order.entity.txn.ProductReturn;
import com.sourcetrace.eses.order.entity.txn.ProductReturnDetail;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class ProductReturnReportDAO extends ReportDAO {

	protected Criteria createCriteria(Map params) {

		String entity = (String) params.get(ENTITY);
		Object object = (Object) params.get(FILTER);
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof ProductReturnDetail) {
				entity = ProductReturnDetail.class.getName();
			}
		}

		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(entity);

		return criteria;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.ReportDAO#addExampleFiltering(org.hibernate.Criteria,
	 * java.util.Map)
	 */
	protected void addExampleFiltering(Criteria criteria, Map params) {

		// check for filter entity
		Object obj = (Object) params.get(FILTER);
		if (obj instanceof ProductReturn) {
			ProductReturn entity = (ProductReturn) obj;
			
				criteria.createAlias("productReturnDetail", "prd").createAlias("prd.product", "p");
				criteria.createAlias("village", "v");

				if (!ObjectUtil.isEmpty(entity.getFilterData()) && entity.getFilterData().size() > 0) {
					entity.getFilterData().forEach((key, value) -> {
						if (!StringUtil.isEmpty(key)) {
							criteria.add(Restrictions.like(key, value, MatchMode.ANYWHERE));
						}
					});
				}

		} else if (obj instanceof ProductReturnDetail) {
			ProductReturnDetail detail = (ProductReturnDetail)obj;
			
				criteria.createAlias("productReturn", "pr");
				criteria.createAlias("product", "p").createAlias("p.subcategory", "subcategory");

				if (!ObjectUtil.isEmpty(detail.getProductReturn())) {

					if (detail.getProductReturn().getTxnType() != null
							&& detail.getProductReturn().getTxnType().equals(ProductReturn.AGENT)) {
						criteria.add(Restrictions.isNull("pr.farmerId"));
					}

					if (!StringUtil.isEmpty(detail.getProductReturn().getId())
							&& detail.getProductReturn().getId() > 0L) {
						criteria.add(Restrictions.eq("pr.id", detail.getProductReturn().getId()));
					}
					
					if (!StringUtil.isEmpty(detail.getProductReturn().getBranchId())) {
						criteria.add(Restrictions.eq("pr.branchId", detail.getProductReturn().getBranchId()));
					}
					
				}

				if (!ObjectUtil.isEmpty(detail.getFilterData()) && detail.getFilterData().size() > 0) {
					detail.getFilterData().forEach((key, value) -> {
						if (!StringUtil.isEmpty(key)) {
							criteria.add(Restrictions.like(key, value, MatchMode.ANYWHERE));
						}
					});
				}
			}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.ReportDAO#addDateRangeFiltering(org.hibernate.Criteria,
	 * java.util.Map)
	 */
	

}
