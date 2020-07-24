/*
 * ServiceLocationDAO.java
 * Copyright (c) 2012-2013, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sourcetrace.esesw.entity.profile.ServiceLocation;

/**
 * @author tharani
 * 
 */
@Repository
@Transactional
public class ServiceLocationDAO extends ESEDAO implements IServiceLocationDAO {
	
	@Autowired
	  public ServiceLocationDAO(SessionFactory sessionFactory) {

	    this.setSessionFactory(sessionFactory);
	   }

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.esesw.dao.profile.IServiceLocationDAO#
	 * findServiceLocationByCode(java.lang.String)
	 */
	public ServiceLocation findServiceLocationByCode(String code) {
		return (ServiceLocation) find(
				"FROM ServiceLocation sl WHERE sl.code = ?", code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.esesw.dao.profile.IServiceLocationDAO#
	 * findServiceLocationByName(java.lang.String)
	 */
	public ServiceLocation findServiceLocationByName(String name) {
		return (ServiceLocation) find(
				"FROM ServiceLocation sl WHERE sl.name = ?", name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.IServiceLocationDAO#findServiceLocationById
	 * (java.lang.Long)
	 */
	public ServiceLocation findServiceLocationById(Long id) {
		return (ServiceLocation) find(
				"FROM ServiceLocation sl WHERE sl.id = ?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.IServiceLocationDAO#findServiceLocation
	 * (long)
	 */
	public ServiceLocation findServiceLocation(long id) {
		return (ServiceLocation) find(
				"FROM ServiceLocation sl WHERE sl.id = ?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.esesw.dao.profile.IServiceLocationDAO#
	 * findServiceLocExistForServPoint(long)
	 */
	@SuppressWarnings("unchecked")
	public boolean findServiceLocExistForServPoint(long servicePointId) {
		List<ServiceLocation> servicePointList = list(
				"From ServiceLocation sl where sl.servicePoint.id = ?",
				servicePointId);
		boolean isServLocMapExist = false;
		if (servicePointList.size() > 0) {
			isServLocMapExist = true;
		}
		return isServLocMapExist;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#findServiceLocationBasedOnServicePointAndAgent(long)
	 */
	public List<String> findServiceLocationBasedOnServicePointAndAgent(long id) {
		Session session = getSessionFactory().openSession();
		String queryString = "SELECT SERV_LOC.NAME from SERV_LOC WHERE SERV_LOC.ID NOT IN (SELECT AGENT_SERV_LOC_MAP.SERV_LOC_ID FROM AGENT_SERV_LOC_MAP)AND SERV_LOC.SERV_POINT_ID='"
				+ id + "'";
		Query query = session.createSQLQuery(queryString);
		List list = query.list();
		session.flush();
		session.close();
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#findServiceLocationBasedOnName(java.lang.String)
	 */
	public ServiceLocation findServiceLocationBasedOnName(
			String agentSerLocation) {
		ServiceLocation serviceLocation = (ServiceLocation) find(
				"from ServiceLocation serviceLocation where serviceLocation.name = ?",
				agentSerLocation);
		return serviceLocation;
	}

}
