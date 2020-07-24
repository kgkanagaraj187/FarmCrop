/*
 * VillageListReportDAO.java
 * Copyright (c) 2012-2013, SourceTrace Systems, All Rights Reserved.
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
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Village;

public class VillageListReportDAO extends ReportDAO {
	@Autowired
	private IPreferencesService preferncesService;

	protected void addExampleFiltering(Criteria criteria, Map params) {
		String enableGrampan = null;
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			enableGrampan = preferences.getPreferences().get(ESESystem.ENABLE_GRAMPANJAYAT);
		}

		// check for filter entity
		Village entity = (Village) params.get(FILTER);

		if (entity != null) {

			if (enableGrampan.equalsIgnoreCase("1")) {
				criteria.createAlias("gramPanchayat", "gp").createAlias("city", "c").createAlias("c.locality", "l")
						.createAlias("l.state", "s").createAlias("s.country", "country");
			} else {
				criteria.createAlias("city", "c");
				criteria.createAlias("c.locality", "l");
				criteria.createAlias("l.state", "s");
				criteria.createAlias("s.country", "country");
			}

			if (!ObjectUtil.isListEmpty(entity.getBranchesList())) {
				criteria.add(Restrictions.in("branchId", entity.getBranchesList()));
			}

			if (!StringUtil.isEmpty(entity.getBranchId())) {
				criteria.add(Restrictions.eq("branchId", entity.getBranchId()));
			}

			if (entity.getCode() != null && !"".equals(entity.getCode()))
				criteria.add(Restrictions.like("code", entity.getCode(), MatchMode.ANYWHERE));

			if (entity.getName() != null && !"".equals(entity.getName()))
				criteria.add(Restrictions.like("name", entity.getName(), MatchMode.ANYWHERE));

			if (entity.getGramPanchayat() != null && entity.getGramPanchayat().getName() != null
					&& !"".equals(entity.getGramPanchayat().getName()))
				criteria.add(Restrictions.like("gp.name", entity.getGramPanchayat().getName(), MatchMode.ANYWHERE));
		}

		if (entity.getCity() != null && entity.getCity().getName() != null && !"".equals(entity.getCity().getName()))
			criteria.add(Restrictions.like("c.name", entity.getCity().getName(), MatchMode.ANYWHERE));

		if (entity.getCity() != null && entity.getCity().getLocality() != null
				&& entity.getCity().getLocality().getName() != null
				&& !"".equals(entity.getCity().getLocality().getName()))
			criteria.add(Restrictions.like("l.name", entity.getCity().getLocality().getName(), MatchMode.ANYWHERE));

		if (entity.getCity() != null && entity.getCity().getLocality() != null
				&& entity.getCity().getLocality().getState() != null
				&& entity.getCity().getLocality().getState().getName() != null
				&& !"".equals(entity.getCity().getLocality().getState().getName()))
			criteria.add(Restrictions.like("s.name", entity.getCity().getLocality().getState().getName(),
					MatchMode.ANYWHERE));
		
		if (entity.getCity() != null && entity.getCity().getLocality() != null
				&& entity.getCity().getLocality().getState()!= null
				&& entity.getCity().getLocality().getState().getCountry() != null
				&& entity.getCity().getLocality().getState().getCountry().getName() != null
				&& !"".equals(entity.getCity().getLocality().getState().getCountry().getName()))
			criteria.add(Restrictions.like("country.name", entity.getCity().getLocality().getState().getCountry().getName(),
					MatchMode.ANYWHERE));
		

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
