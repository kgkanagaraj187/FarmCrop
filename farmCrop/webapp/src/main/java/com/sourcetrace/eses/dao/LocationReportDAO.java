/*
 * CountryReportDAO.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.entity.Location;

@Repository
public class LocationReportDAO extends ReportDAO {

	/**
	 * Instantiates a new country report dao.
	 * 
	 * @param sessionFactory
	 *            the session factory
	 */
	@Autowired
	public LocationReportDAO(SessionFactory sessionFactory) {

		this.setSessionFactory(sessionFactory);
	}

	@SuppressWarnings("rawtypes")
	protected void addExampleFiltering(Criteria criteria, Map params) {

		Location entity = (Location) params.get(FILTER);
		if (entity != null) {
			criteria.createAlias("level", "lvl");
			criteria.createAlias("parent", "pt", Criteria.LEFT_JOIN);

			if (!StringUtil.isEmpty(entity.getLevelCode()))
				criteria.add(Restrictions.eq("lvl.code", entity.getLevelCode()));

			if (!StringUtil.isEmpty(entity.getCode()))
				criteria.add(Restrictions.like("code", entity.getCode(), MatchMode.ANYWHERE));

			if (!StringUtil.isEmpty(entity.getName()))
				criteria.add(Restrictions.like("name", entity.getName(), MatchMode.ANYWHERE));

			if (!ObjectUtil.isEmpty(entity.getParent()) && !StringUtil.isEmpty(entity.getParent().getName()))
				criteria.add(Restrictions.like("pt.name", entity.getParent().getName(), MatchMode.ANYWHERE));

//			if (entity.getIsActive() != EntityInfo.NULL_CHAR) {
//				criteria.add(Restrictions.eq("isActive", entity.getIsActive()));
//			}

//			criteria.add(Restrictions.ne("isActive", EntityInfo.DELETE));
		}
	}

}
