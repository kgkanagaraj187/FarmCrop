/*
 * CustomerReportDAO.java
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Customer;

// TODO: Auto-generated Javadoc
/**
 * The Class CustomerReportDAO.
 */
public class CustomerReportDAO extends ReportDAO {
    
	@SuppressWarnings("unchecked")
	protected void addExampleFiltering(Criteria criteria, Map params) {

		// check for filter entity
		Customer entity = (Customer) params.get(FILTER);
		if (entity != null) {

			if (!ObjectUtil.isListEmpty(entity.getBranchesList())) {
				criteria.add(Restrictions.in("branchId", entity.getBranchesList()));
			}
			
			if (!StringUtil.isEmpty(entity.getBranchId())){
			    criteria.add(Restrictions.eq("branchId", entity.getBranchId()));
			}
			
			if (entity.getCustomerId() != null && !"".equals(entity.getCustomerId()))
				criteria.add(Restrictions.like("customerId", entity.getCustomerId(), MatchMode.ANYWHERE));
			if (entity.getCustomerName() != null && !"".equals(entity.getCustomerName()))
				criteria.add(Restrictions.like("customerName", entity.getCustomerName(), MatchMode.ANYWHERE));
			if (entity.getPersonName() != null && !"".equals(entity.getPersonName()))
				criteria.add(Restrictions.like("personName", entity.getPersonName(), MatchMode.ANYWHERE));
			if (entity.getEmail() != null && !"".equals(entity.getEmail()))
				criteria.add(Restrictions.like("email", entity.getEmail(), MatchMode.ANYWHERE));

			if (entity.getCustomerAddress() != null && !"".equals(entity.getCustomerAddress()))
				criteria.add(Restrictions.like("customerAddress", entity.getCustomerAddress(), MatchMode.ANYWHERE));
			if (entity.getMobileNumber() != null && !"".equals(entity.getMobileNumber()))
				criteria.add(Restrictions.like("mobileNumber", entity.getMobileNumber(), MatchMode.ANYWHERE));

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
