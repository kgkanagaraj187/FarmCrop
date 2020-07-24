/*
 * FieldStaffManagementReportDAO.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.dao.report;

import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.order.entity.txn.PeriodicInspection;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.FarmIcsConversion;

@SuppressWarnings("unchecked")
public class PeriodicInspectionReportDAO extends ReportDAO {

	private SimpleDateFormat DataBaseFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	protected void addExampleFiltering(Criteria criteria, Map params) {

		PeriodicInspection entity = (PeriodicInspection) params.get(FILTER);

		/*criteria.createAlias("farm", "f").createAlias("f.farmIcsConv", "fi").createAlias("f.farmer", "fm").createAlias("fm.samithi", "fs")
				.createAlias("fm.village", "fv");*/
		criteria.createAlias("farm", "f").createAlias("f.farmICSConversion", "fi",Criteria.LEFT_JOIN).createAlias("f.farmer", "fm").createAlias("fm.samithi", "fs")
		.createAlias("fm.village", "fv");
		if (entity != null) {

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
    		
    		if (multibranch.equals("1")) {
    			Object object = httpSession.getAttribute(ISecurityFilter.MAPPED_BRANCHES);
    			if (object != null) {
    				String[] branches = object.toString().split(",");

    				if (branches != null && branches.length > 0) {
    					criteria.add(Restrictions.in("branchId", branches));
    				}
    			}
    		} else {
    			Object object3= httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
    			String currentBranch1 = ObjectUtil.isEmpty(object3) ? "" : object3.toString();
    			if (!StringUtil.isEmpty(currentBranch1)) {
    				criteria.add(Restrictions.like("branchId", currentBranch1, MatchMode.EXACT));
    			}
    		}
    		
			
			if (!ObjectUtil.isListEmpty(entity.getBranchesList())) {
				criteria.add(Restrictions.in("branchId", entity.getBranchesList()));
			}

			if (!StringUtil.isEmpty(entity.getBranchId())) {
				criteria.add(Restrictions.eq("branchId", entity.getBranchId()));
			}

			if (entity.getInspectionDate() != null && !"".equals(entity.getInspectionDate())) {
				criteria.add(Restrictions.like("inspectionDate", DataBaseFormat.format(entity.getInspectionDate()),
						MatchMode.ANYWHERE));
			}

			if (!ObjectUtil.isEmpty(entity.getInspectionType()) && !StringUtil.isEmpty(entity.getInspectionType())) {
				criteria.add(Restrictions.eq("inspectionType", entity.getInspectionType()));
			}

			if (!ObjectUtil.isEmpty(entity.getFarmId()) && !StringUtil.isEmpty(entity.getFarmId())) {
				criteria.add(Restrictions.like("farmId", entity.getFarmId().trim(), MatchMode.EXACT));
			}

			if (!ObjectUtil.isEmpty(entity.getFarm()) && !StringUtil.isEmpty(entity.getFarm().getFarmer())
					&& !StringUtil.isEmpty(entity.getFarm().getFarmer().getFarmerId())) {
				criteria.add(Restrictions.like("fm.farmerId", entity.getFarm().getFarmer().getFarmerId().trim(),
						MatchMode.EXACT));
			}
			if (!ObjectUtil.isEmpty(entity.getFarm()) && !ObjectUtil.isEmpty(entity.getFarm().getFarmer())
					&& !ObjectUtil.isEmpty(entity.getFarm().getFarmer().getSamithi())
					&& !StringUtil.isEmpty(entity.getFarm().getFarmer().getSamithi().getCode())) {
				criteria.add(Restrictions.like("fs.code", entity.getFarm().getFarmer().getSamithi().getCode()));
			}
			if (!ObjectUtil.isEmpty(entity.getFarm()) && !ObjectUtil.isEmpty(entity.getFarm().getFarmer())
					&& !ObjectUtil.isEmpty(entity.getFarm().getFarmer().getVillage())
					&& !StringUtil.isEmpty(entity.getFarm().getFarmer().getVillage().getCode())) {
				criteria.add(Restrictions.like("fv.code", entity.getFarm().getFarmer().getVillage().getCode()));
			}

			if (!ObjectUtil.isEmpty(entity.getFarm()) && !StringUtil.isEmpty(entity.getFarm().getFarmer())
					&& !StringUtil.isEmpty(entity.getFarm().getFarmer().getLastName())) {
				criteria.add(Restrictions.like("fm.lastName", entity.getFarm().getFarmer().getLastName().trim(),
						MatchMode.EXACT));
			}

			if (!ObjectUtil.isEmpty(entity.getCreatedUserName()) && !StringUtil.isEmpty(entity.getCreatedUserName())) {
				criteria.add(Restrictions.like("createdUserName", entity.getCreatedUserName().trim(), MatchMode.EXACT));
			}
			if (!ObjectUtil.isEmpty(entity.getSeasonCode()) && !StringUtil.isEmpty(entity.getSeasonCode())) {
				criteria.add(Restrictions.like("seasonCode", entity.getSeasonCode(), MatchMode.EXACT));
			}

			if (!ObjectUtil.isEmpty(entity.getIsDeleted()) && !StringUtil.isEmpty(entity.getIsDeleted())) {
				criteria.add(Restrictions.like("isDeleted", entity.getIsDeleted(), MatchMode.EXACT));
			}
			if (!ObjectUtil.isEmpty(entity.getIcsName()) && !StringUtil.isEmpty(entity.getIcsName())) {
				//FarmIcsConversion icsType = farmerService.findFarmIcsConversionByFarmId(Long.valueOf(entity.getFarm().getId()));
				criteria.add(Restrictions.like("fi.icsType", entity.getIcsName().trim(), MatchMode.EXACT));
			}
		}

		// sorting direction

		String dir = (String) params.get(DIR);
		// sorting column
		String sort = (String) params.get(SORT_COLUMN);
		// sort a column in the given direction ascending/descending
		if (dir != null && sort != null) {
			Criteria sortCriteria = null;
			if (sort.contains(DELIMITER)) {
				sort = sort.substring(sort.lastIndexOf(DELIMITER) + 1);
				if (dir.equals(DESCENDING)) {
					// sort descending
					sortCriteria.addOrder(Order.desc(sort));
				} else {
					// sort ascending
					sortCriteria.addOrder(Order.asc(sort));
				}
			}
		}

	}

}
