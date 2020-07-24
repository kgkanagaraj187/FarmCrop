/*
 * LocationServiceTest.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.util.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.esesw.entity.profile.Country;

/**
 * @author PANNEER
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/beans-*.xml" })
public class LocationServiceTest {

	@Autowired
	private ILocationService locationService;

	/**
	 * Test list of countrries.
	 */
	@Test
	public void testListOfCountrries() {

		List<Country> countryList = locationService.listCountries();
		Assert.assertNotNull(countryList);
	}

}
