/*
 * ServiceLocationService.java
 * Copyright (c) 2012-2013, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

package com.sourcetrace.eses.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sourcetrace.eses.dao.IServiceLocationDAO;
import com.sourcetrace.esesw.entity.profile.ServiceLocation;

/**
 * @author tharani
 * 
 */
@Service
@Transactional
public class ServiceLocationService implements IServiceLocationService {
	@Autowired
	private IServiceLocationDAO serviceLocationDAO;

	/* (non-Javadoc)
	 * @see com.sourcetrace.esesw.service.profile.IServiceLocationService#findServiceLocationById(java.lang.Long)
	 */
	public ServiceLocation findServiceLocationById(Long id) {
		return serviceLocationDAO.findServiceLocationById(id);
	}

	/**
	 * Sets the service location dao.
	 * 
	 * @param serviceLocationDAO the new service location dao
	 */
	public void setServiceLocationDAO(IServiceLocationDAO serviceLocationDAO) {
		this.serviceLocationDAO = serviceLocationDAO;
	}

	/**
	 * Gets the service location dao.
	 * 
	 * @return the service location dao
	 */
	public IServiceLocationDAO getServiceLocationDAO() {
		return serviceLocationDAO;
	}

	/* (non-Javadoc)
	 * @see com.sourcetrace.esesw.service.profile.IServiceLocationService#findServiceLocation(long)
	 */
	public ServiceLocation findServiceLocation(long id) {
		return serviceLocationDAO.findServiceLocation(id);
	}

	/* (non-Javadoc)
	 * @see com.sourcetrace.esesw.service.profile.IServiceLocationService#findServiceLocExistForServPoint(long)
	 */
	public boolean findServiceLocExistForServPoint(long servicePointId) {
		return serviceLocationDAO
				.findServiceLocExistForServPoint(servicePointId);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IAgentService#findAgentExistForServicePoint(long)
	 */

	public List<String> findServiceLocationBasedOnServicePointAndAgent(long id) {

		return serviceLocationDAO.findServiceLocationBasedOnServicePointAndAgent(id);
	}

	/* (non-Javadoc)
	 * @see com.ese.service.profile.IAgentService#findServLocBasedOnName(java.lang.String)
	 */
	public ServiceLocation findServiceLocationBasedOnName(String agentSerLocation) {

		return serviceLocationDAO.findServiceLocationBasedOnName(agentSerLocation);
	}

}
