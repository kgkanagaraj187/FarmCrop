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

import java.util.Date;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.order.entity.txn.ProcurementDetail;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class ProcurementReportDAO extends ReportDAO {

	protected Criteria createCriteria(Map params) {

		String entity = (String) params.get(ENTITY);
		Object object = (Object) params.get(FILTER);
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof ProcurementDetail) {
				entity = ProcurementDetail.class.getName();
			}
		}

		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(entity);

		return criteria;
	}

	protected void addExampleFiltering(Criteria criteria, Map params) {
		Object object = (Object) params.get(FILTER);
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof Procurement) {
				Procurement procurement = (Procurement) object;

				criteria.createAlias("village", "v", Criteria.LEFT_JOIN).createAlias("agroTransaction", "agroTxn").createAlias("agroTxn.samithi", "sam",Criteria.LEFT_JOIN);

				criteria.createAlias("farmer", "f",JoinType.LEFT_OUTER_JOIN);
				criteria.createAlias("procurementDetails", "pd");
				
				

				if (!ObjectUtil.isEmpty(procurement.getFilterData()) && procurement.getFilterData().size() > 0) {
					procurement.getFilterData().forEach((key, value) -> {
						if (!StringUtil.isEmpty(key)) {
							criteria.add(Restrictions.like(key, value, MatchMode.ANYWHERE));
						}
					});
				}
				

				if (!ObjectUtil.isEmpty(procurement.getAgroTransaction())) {

					if (!StringUtil.isEmpty(procurement.getAgroTransaction().getFarmerId())) {
						criteria.add(Restrictions.like("agroTxn.farmerId",
								procurement.getAgroTransaction().getFarmerId(), MatchMode.ANYWHERE));
					}

					if (!ObjectUtil.isEmpty(procurement.getVillage())
							&& !StringUtil.isEmpty(procurement.getVillage().getName())) {
						criteria.add(Restrictions.eq("v.name", procurement.getVillage().getName()));
					}

					if (!ObjectUtil.isEmpty(procurement.getSeasonCode())) {
						criteria.add(Restrictions.like("seasonCode", procurement.getSeasonCode(), MatchMode.ANYWHERE));
					}


				}

			} else if (object instanceof ProcurementDetail) {

				ProcurementDetail procurementDetail = (ProcurementDetail) object;

				criteria.createAlias("procurement", "p", Criteria.LEFT_JOIN);
				criteria.createAlias("p.agroTransaction", "agroTxn", Criteria.LEFT_JOIN);
				criteria.createAlias("procurementGrade","pg");
				
				if (!ObjectUtil.isEmpty(procurementDetail.getProcurement()))
					criteria.add(Restrictions.eq("p.id", procurementDetail.getProcurement().getId()));

			}

		}

	}
	
	

}
