/*
 * UniqueIDGeneratorTest.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/beans-*.xml" })
public class UniqueIDGeneratorTest {

    @Autowired
    private IUniqueIDGenerator idGenerator;

    @Test
    public void testCreateFarmerIdSeq() {

        String farmerIdSeq = idGenerator.getFarmerWebIdSeq();
        assertNotNull(farmerIdSeq);
    }

    @Test
    public void testCreateAgentInternalIdSeq() {

        String agentInternalIdSeq = idGenerator.createAgentInternalIdSequence();
        assertNotNull(agentInternalIdSeq);
    }

}
