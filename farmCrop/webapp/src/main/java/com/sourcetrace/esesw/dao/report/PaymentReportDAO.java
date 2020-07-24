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

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.PaymentMode;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.order.entity.txn.ProcurementDetail;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class PaymentReportDAO extends ReportDAO {

	protected Criteria createCriteria(Map params) {

		String entity = (String) params.get(ENTITY);
		Object object = (Object) params.get(FILTER);

		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(entity);

		return criteria;
	}

	protected void addExampleFiltering(Criteria criteria, Map params) {

		Object object = (Object) params.get(FILTER);
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof AgroTransaction) {
				AgroTransaction agroTxn = (AgroTransaction) object;
				HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
				String tenantId = "";
				if (!ObjectUtil.isEmpty(request)) {
					tenantId = !StringUtil
							.isEmpty((String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID))
									? (String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID) : "";
				}
				if (!tenantId.equalsIgnoreCase("olivado")) {
					if (!ObjectUtil.isEmpty(agroTxn)) {
						criteria.add(Restrictions.in("txnType", new String[] { PaymentMode.DISTIBUTION_PAYMENT_TXN,
								PaymentMode.PROCURMENT_PAYMENT_TXN,PaymentMode.LOAN_REPAYMENT_TXN }));
						criteria.add(Restrictions.eq("profType", Profile.CO_OPEARATIVE_MANAGER));
					}
				} else {
					if (!ObjectUtil.isEmpty(agroTxn)) {
						criteria.add(Restrictions.in("txnType", new String[] { PaymentMode.DISTIBUTION_PAYMENT_TXN,
								PaymentMode.PROCURMENT_PAYMENT_TXN }));
						criteria.add(Restrictions.eq("profType", Profile.CLIENT));
					}
				}

				if (!ObjectUtil.isEmpty(agroTxn.getFilterData()) && agroTxn.getFilterData().size() > 0) {
					agroTxn.getFilterData().forEach((key, value) -> {
						if (!StringUtil.isEmpty(key)) {
							criteria.add(Restrictions.like(key, value, MatchMode.ANYWHERE));
						}
					});
				}

			}
		}

	}

}
