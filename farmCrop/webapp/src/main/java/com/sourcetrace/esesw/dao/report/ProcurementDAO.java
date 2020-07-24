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
import com.sourcetrace.eses.order.entity.txn.Distribution;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.order.entity.txn.ProcurementDetail;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class ProcurementDAO extends ReportDAO {

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
				//criteria.createAlias("procurementDetails", "pd");
				criteria.createAlias("farmer", "f",JoinType.LEFT_OUTER_JOIN);
				criteria.createAlias("agroTransaction", "agroTxn");
				if(!ObjectUtil.isEmpty(procurement.getFilterData())&&procurement.getFilterData().size()>0){
					procurement.getFilterData().forEach((key,value)->{
						if(!StringUtil.isEmpty(key)){
							criteria.add(Restrictions.like(key, value,MatchMode.ANYWHERE));
						}
					});
				}
				
			} else if (object instanceof ProcurementDetail) {

				ProcurementDetail procurementDetail = (ProcurementDetail) object;
				criteria.createAlias("procurementGrade","pg");
				criteria.createAlias("procurement", "p", Criteria.LEFT_JOIN);
				if (!ObjectUtil.isEmpty(procurementDetail.getProcurement()))
					criteria.add(Restrictions.eq("p.id", procurementDetail.getProcurement().getId()));

			}
			  criteria.add(Restrictions.ne("status",Integer.valueOf(Procurement.DELETE_STATUS)));

			
		}

	}

}
