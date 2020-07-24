/*
\ * ProcurementReportDAO.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
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
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.order.entity.txn.LoanDistribution;
import com.sourcetrace.eses.order.entity.txn.LoanDistributionDetail;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.order.entity.txn.ProcurementDetail;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class LoanDistributionDAO extends ReportDAO {

	protected Criteria createCriteria(Map params) {

		String entity = (String) params.get(ENTITY);
		Object object = (Object) params.get(FILTER);
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof LoanDistributionDetail) {
				entity = LoanDistributionDetail.class.getName();
			}
		}

		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(entity);

		return criteria;
	}

	protected void addExampleFiltering(Criteria criteria, Map params) {
		Object object = (Object) params.get(FILTER);
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof LoanDistribution) {
				LoanDistribution loanDistribution = (LoanDistribution) object;
				//criteria.createAlias("procurementDetails", "pd");
				criteria.createAlias("farmer", "f",JoinType.LEFT_OUTER_JOIN);
				criteria.createAlias("agroTransaction", "agroTxn",JoinType.LEFT_OUTER_JOIN);
				criteria.createAlias("village", "v", Criteria.LEFT_JOIN);
				criteria.add(Restrictions.eq("loanStatus",0));
				criteria.createAlias("vendor", "vr",JoinType.LEFT_OUTER_JOIN);
				if (!ObjectUtil.isEmpty(loanDistribution)) {

					if (!ObjectUtil.isEmpty(loanDistribution.getFarmer()) && !StringUtil.isEmpty(loanDistribution.getFarmer().getFarmerId())) {
						criteria.add(Restrictions.like("f.farmerId",
								loanDistribution.getFarmer().getFarmerId(), MatchMode.ANYWHERE));
					}

					if (!ObjectUtil.isEmpty(loanDistribution.getVillage())
							&& !StringUtil.isEmpty(loanDistribution.getVillage().getName())) {
						criteria.add(Restrictions.eq("v.name", loanDistribution.getVillage().getName()));
					}

					if (!ObjectUtil.isEmpty(loanDistribution.getVendor())
							&& !StringUtil.isEmpty(loanDistribution.getVendor().getVendorId())) {
						criteria.add(Restrictions.eq("vr.vendorId", loanDistribution.getVendor().getVendorId()));
					}
				}
				
			} else if (object instanceof LoanDistributionDetail) {

				LoanDistributionDetail loanDistributionDetail = (LoanDistributionDetail) object;
				
				criteria.createAlias("loanDistribution", "ld", Criteria.LEFT_JOIN);
				if (!ObjectUtil.isEmpty(loanDistributionDetail.getLoanDistribution()))
					criteria.add(Restrictions.eq("ld.id", loanDistributionDetail.getLoanDistribution().getId()));

			}
		}

	}

}
