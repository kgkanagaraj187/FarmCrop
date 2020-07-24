/*
 * CryptoUtilTest.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.util.crypto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.sourcetrace.eses.util.StringUtil;

/**
 * @author PANNEER
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/beans-*.xml" })
public class CryptoUtilTest {

    @Autowired
    private ICryptoUtil cryptoUtil;

    /**
     * Test encrypt password.
     */
    @Test
    public void testEncryptPassword() {
        /*
         * username : sourcetrace
         * password : 12345678
         */
        String encryptPassword = cryptoUtil.encrypt(StringUtil.getMulipleOfEight("username+password"));
        Assert.notNull(encryptPassword);
    }

    /**
     * Test decrypt password.
     */
    @Test
    public void testDecryptPassword() {

        String decryptPassword = cryptoUtil.decrypt("4139F1732A5C2E81F66D1F2C9E38FFE4");
        Assert.notNull(decryptPassword);

    }

}
