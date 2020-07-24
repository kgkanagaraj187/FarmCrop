/*
 * LicenseGeneratorTest.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.util;

import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.entity.License;
import com.sourcetrace.eses.util.entity.LicenseType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/beans-*.xml" })
public class LicenseGeneratorTest {

	private static final Logger LOGGER = Logger.getLogger(LicenseGeneratorTest.class);

	@Autowired
	private ILicenseKeyGenerator licenseKeyGenerator;

	/**
	 * Test generate.
	 */
	@Test
	public void testGenerate() {

		License license = new License();
		license.setType(LicenseType.DEMO);
		license.setVersion("V1.0.0 R2");
		license.setOwner("SourceTrace");
		license.setClient("gal");
		license.setStart(new Date());
		String from = DateUtil.convertDateToString(license.getStart(), "MMddyyyy");
		String end = DateUtil.plusMonth(from, 3, "MMddyyyy");
		license.setEnd(DateUtil.convertStringToDate(end, "MMddyyyy"));
		license.setKey(licenseKeyGenerator.generate(license.getText()));
		LOGGER.info("----- License Key -----  : " + license.getKey());
		Assert.assertNotNull(license.getKey());

	}

}
