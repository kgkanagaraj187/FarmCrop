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

import com.sourcetrace.eses.order.entity.txn.SupplierProcurement;
import com.sourcetrace.eses.order.entity.txn.SupplierProcurementDetail;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class SupplierProcurementReportDAO extends ReportDAO {

	protected Criteria createCriteria(Map params) {

		String entity = (String) params.get(ENTITY);
		Object object = (Object) params.get(FILTER);
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof SupplierProcurementDetail) {
				entity = SupplierProcurementDetail.class.getName();
			}
		}

		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(entity);

		return criteria;
	}

	@SuppressWarnings("deprecation")
	protected void addExampleFiltering(Criteria criteria, Map params) {
		Object object = (Object) params.get(FILTER);
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof SupplierProcurementDetail) {
				SupplierProcurementDetail supplierProcurementDetail = (SupplierProcurementDetail) object;
				criteria.createAlias("supplierProcurement", "sp");
				
				if (!ObjectUtil.isEmpty(supplierProcurementDetail.getSupplierProcurement())){
					criteria.add(Restrictions.eq("sp.id", supplierProcurementDetail.getSupplierProcurement().getId()));
									
					
				}

		}else if (object instanceof SupplierProcurement) {

			SupplierProcurement supplierProcurement = (SupplierProcurement) object;
			criteria.createAlias("agroTransaction", "agroTxn");
			
			if (!ObjectUtil.isEmpty(supplierProcurement) 
					&& !ObjectUtil.isEmpty(supplierProcurement.getSeasonCode())) {
				criteria.add(Restrictions.like("seasonCode", supplierProcurement.getSeasonCode(), MatchMode.ANYWHERE));
			}
			if (!ObjectUtil.isEmpty(supplierProcurement.getAgroTransaction()) && !StringUtil.isEmpty(supplierProcurement.getAgroTransaction().getAgentId())) {
				criteria.add(Restrictions.like("agroTxn.agentId", supplierProcurement.getAgroTransaction().getAgentId(), MatchMode.ANYWHERE));
			}
			if (!ObjectUtil.isEmpty(supplierProcurement) 
					&& !ObjectUtil.isEmpty(supplierProcurement.getFilterData())
					&& supplierProcurement.getFilterData().size() > 0) {
				supplierProcurement.getFilterData().forEach((key, value) -> {
					if (!StringUtil.isEmpty(key)) {
						criteria.add(Restrictions.like(key, value, MatchMode.ANYWHERE));
					}
				});
			}

		}

	}
	}

	
}
