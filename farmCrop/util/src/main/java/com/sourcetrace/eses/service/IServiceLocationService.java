/*
 * IServiceLocationService.java
 * Copyright (c) 2012-2013, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import java.util.List;

import com.sourcetrace.esesw.entity.profile.ServiceLocation;

/**
 * @author tharani
 * 
 */
public interface IServiceLocationService {

	/**
	 * Find service location by id.
	 * 
	 * @param id
	 *            the id
	 * 
	 * @return the service location
	 */
	ServiceLocation findServiceLocationById(Long id);

	/**
	 * Find service location.
	 * 
	 * @param id
	 *            the id
	 * 
	 * @return the service location
	 */
	ServiceLocation findServiceLocation(long id);

	/**
	 * Find service loc exist for serv point.
	 * 
	 * @param servicePointId
	 *            the service point id
	 * 
	 * @return true, if successful
	 */
	boolean findServiceLocExistForServPoint(long servicePointId);
	
	/**
	 * Find serv loc based on name.
	 * 
	 * @param agentSerLocation
	 *            the agent ser location
	 * 
	 * @return the service location
	 */
	public ServiceLocation findServiceLocationBasedOnName(String agentSerLocation);

	/**
	 * Find serv loc based serv point and agent.
	 * 
	 * @param id
	 *            the id
	 * 
	 * @return the list< string>
	 */
	public List<String> findServiceLocationBasedOnServicePointAndAgent(long id);


}
