/*
 * LicenseKeyGeneratorTest.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.util;

import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sourcetrace.eses.util.crypto.ICryptoUtil;
import com.sourcetrace.eses.util.entity.License;
import com.sourcetrace.eses.util.entity.LicenseType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/beans-*.xml" })
public class LicenseKeyGeneratorTest {

    private static final Logger LOGGER = Logger.getLogger(LicenseKeyGeneratorTest.class);

    @Autowired
    private ICryptoUtil tripleDES;

    /**
     * Test generate.
     */
    @Test
    public void testGenerate() {

        License license = new License();
        license.setType(LicenseType.DEMO);
        license.setVersion("1.5.0");
        license.setOwner("SourceTrace");
        license.setClient("canda");
        license.setStart(new Date());
        String from = DateUtil.convertDateToString(license.getStart(), "MMddyyyy");
        String end = DateUtil.plusMonth(from, 12, "MMddyyyy");
        license.setEnd(DateUtil.convertStringToDate(end, "MMddyyyy"));
        license.setKey(tripleDES.encrypt(StringUtil.getMulipleOfEight(license.getText())));
        LOGGER.info("----- License Key -----  : " + license.getKey());
        Assert.assertNotNull(license.getKey());
    }

}
