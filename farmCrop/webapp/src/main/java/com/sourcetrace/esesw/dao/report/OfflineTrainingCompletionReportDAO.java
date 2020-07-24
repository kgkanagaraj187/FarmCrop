/*
 * OfflineTrainingCompletionReportDAO.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.dao.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.ese.entity.txn.training.OfflineTrainingStatus;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;

public class OfflineTrainingCompletionReportDAO extends ReportDAO {

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	protected void addExampleFiltering(Criteria criteria, Map params) {

		// check for filter entity
		OfflineTrainingStatus entity = (OfflineTrainingStatus) params.get(FILTER);

		if (entity != null) {
			Date from = (Date) params.get(FROM_DATE);
			// to date
			Date to = (Date) params.get(TO_DATE);

			HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
            HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
    		String tenantId = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID) ? ISecurityFilter.DEFAULT_TENANT_ID
    				: "";
    		if (!ObjectUtil.isEmpty(request)) {
    				tenantId = !StringUtil.isEmpty((String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID))
    					? (String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID) : "";
    		}
    		if (tenantId.equalsIgnoreCase("indev")) {
            Object object = httpSession.getAttribute(ISecurityFilter.MAPPED_BRANCHES);
            /*String currentBranch = ObjectUtil.isEmpty(object) ? "" : object.toString();*/
            if(object!=null){
            String[] branches = object.toString().split(",");

            if (branches!=null&&branches.length>0) {
                criteria.add(Restrictions.in("branchId", branches));
            }
    		}}
            
			
			if (!StringUtil.isEmpty(from.toString()) && !StringUtil.isEmpty(to.toString()))
				criteria.add(Restrictions.between("trainingDate", sdf.format(from), sdf.format(to)));

			criteria.add(Restrictions.in("statusCode",
					new Object[] { ESETxnStatus.ERROR.ordinal(), ESETxnStatus.PENDING.ordinal(), }));

			if (!ObjectUtil.isListEmpty(entity.getBranchesList())) {
				criteria.add(Restrictions.in("branchId", entity.getBranchesList()));
			}

			if (!StringUtil.isEmpty(entity.getBranchId())) {
				criteria.add(Restrictions.eq("branchId", entity.getBranchId()));
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

	protected void addDateRangeFiltering(Criteria criteria, Map params) {

	}

}
