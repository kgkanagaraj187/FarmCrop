/*
 * SurveyMasterReportDAO.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.inspect.agrocert.SurveyMaster;
import com.sourcetrace.eses.inspect.agrocert.SurveyMasterTarget;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class SurveyMasterReportDAO extends ReportDAO {

	@SuppressWarnings("unchecked")
	protected void addPropertyProjections(Criteria criteria, Map params) {

		// check for filter entity
		SurveyMaster entity = (SurveyMaster) params.get(FILTER);

		if (entity != null) {
			criteria.createAlias("sections", "s", Criteria.LEFT_JOIN)
					.createAlias("s.languagePreferences", "slp", Criteria.LEFT_JOIN)
					.createAlias("dataLevel", "d", Criteria.LEFT_JOIN)
					.createAlias("d.languagePreferences", "dlp", Criteria.LEFT_JOIN)
					/*.createAlias("surveyMasterTarget", "smt", Criteria.LEFT_JOIN)*/
					.createAlias("surveyType", "sT", Criteria.LEFT_JOIN);

			criteria.createAlias("languagePreferences", "lp", Criteria.LEFT_JOIN);

			/*if (entity.getCountry() != null) { 
				criteria.createAlias("country", "county");
				criteria.add(Restrictions.like("country", entity.getCountry()));
			}*/
			// criteria.add(Restrictions.eq("status", SurveyMaster.ACTIVE));
			/*criteria.add(Restrictions.eq("smt.status", SurveyMasterTarget.STATUS.ACTIVE.ordinal()));*/
			if (StringUtil.isEmpty(entity.getLanguage()))
				entity.setLanguage("en");
			criteria.add(Restrictions.eq("slp.lang", entity.getLanguage()));
			criteria.add(Restrictions.eq("dlp.lang", entity.getLanguage()));
			criteria.add(Restrictions.eq("lp.lang", entity.getLanguage()));

			if (!StringUtil.isEmpty(entity.getCode()))
				criteria.add(Restrictions.like("code", entity.getCode(), MatchMode.ANYWHERE));

			if (!StringUtil.isEmpty(entity.getName()))
				criteria.add(Restrictions.like("lp.name", entity.getName(), MatchMode.ANYWHERE));

			if (!StringUtil.isEmpty(entity.getDescription()))
				criteria.add(Restrictions.like("description", entity.getDescription(), MatchMode.ANYWHERE));

			if (!ObjectUtil.isEmpty(entity.getSurveyType()) && !StringUtil.isEmpty(entity.getSurveyType().getName()))
				criteria.add(Restrictions.like("sT.name", entity.getSurveyType().getName(), MatchMode.ANYWHERE));

			if (!ObjectUtil.isEmpty(entity.getSection()) && !StringUtil.isEmpty(entity.getSection().getName()))
				criteria.add(Restrictions.like("slp.name", entity.getSection().getName(), MatchMode.ANYWHERE));

			

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
		addProjections(criteria, params);
	}

	@Override
	protected void addGrouping(Map params, ProjectionList list) {

		list.add(Projections.groupProperty("id"));
	}

	@Override
	protected void addProjections(Criteria criteria, Map params) {

		ProjectionList list = Projections.projectionList();
		addGrouping(params, list);
		list.add(Projections.property("code"));
		list.add(Projections.property("name"));
		list.add(Projections.property("description"));
		list.add(Projections.property("sT.name"));
		list.add(Projections.sqlProjection("GROUP_CONCAT(slp2_.name) as sectionName", new String[] { "sectionName" },
				new Type[] { StringType.INSTANCE }));
		list.add(Projections.property("dlp.name"));
		//list.add(Projections.property("smt.targetValue"));
		criteria.setProjection(list);
	}

}
