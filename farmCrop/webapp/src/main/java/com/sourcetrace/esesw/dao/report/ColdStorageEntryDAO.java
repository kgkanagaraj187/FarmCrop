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
import com.sourcetrace.eses.entity.ColdStorage;
import com.sourcetrace.eses.entity.ColdStorageDetail;
import com.sourcetrace.eses.order.entity.txn.Distribution;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.order.entity.txn.ProcurementDetail;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class ColdStorageEntryDAO extends ReportDAO {

	protected Criteria createCriteria(Map params) {

		String entity = (String) params.get(ENTITY);
		Object object = (Object) params.get(FILTER);
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof ColdStorageDetail) {
				entity = ColdStorageDetail.class.getName();
			}
		}

		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(entity);

		return criteria;
	}

	protected void addExampleFiltering(Criteria criteria, Map params) {
		Object object = (Object) params.get(FILTER);
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof ColdStorage) {
				ColdStorage coldStorage = (ColdStorage) object;
				
				
			} else if (object instanceof ColdStorageDetail) {

				ColdStorageDetail coldStorageDetail = (ColdStorageDetail) object;
				criteria.createAlias("procurementGrade","pg");
				criteria.createAlias("coldStorage", "cs", Criteria.LEFT_JOIN);
				if (!ObjectUtil.isEmpty(coldStorageDetail.getColdStorage()))
					criteria.add(Restrictions.eq("cs.id", coldStorageDetail.getColdStorage().getId()));

			}
			 

			
		}

	}

}
