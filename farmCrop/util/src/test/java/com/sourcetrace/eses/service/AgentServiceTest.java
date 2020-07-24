/*
 * AgentServiceTest.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 
package com.sourcetrace.eses.service;

import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.umgmt.entity.ContactInfo;
import com.sourcetrace.eses.umgmt.entity.PersonalInfo;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.crypto.ICryptoUtil;

*//**
 * The Class AgentServiceTest.
 *//*
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/beans-*.xml" })
public class AgentServiceTest {

	@Autowired
	private IAgentService agentService;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private ICryptoUtil cryptoUtil;

	*//**
	 * Test create agent.
	 *//*
	@Test
	public void testCreateAgent() {

		try {
			agentService.createAgent(formAgent());
			System.out.println("Agent Creation Successful");
		} catch (Exception e) {
			System.out.println("Error while Agent Creation");
		}
	}

	*//**
	 * Test find agent by id.
	 *//*
	@Test
	public void testFindAgentById() {

		Agent agent = agentService.findAgentById(agentService.findAgentByAgentId("testAgent").getId());
		Assert.assertNotNull(agent);
	}

	*//**
	 * Test edit agent.
	 *//*
	@Test
	public void testEditAgent() {

		Agent agent = agentService.findAgentByAgentId("testAgent");
		if (!ObjectUtil.isEmpty(agent.getPersonalInfo()))
			agent.getPersonalInfo().setFirstName("TestName");
		try {
			agentService.editAgent(agent);
			System.out.println("Agent Updation Successful For : " + agent.getPersonalInfo().getFirstName());
		} catch (Exception e) {
			System.out.println("Error while Updating Agent");
		}
	}

	*//**
	 * Update agent bod status.
	 *//*
	@Test
	public void updateAgentBODStatus() {

		agentService.updateAgentBODStatus("testAgent", 1);
	}

	*//**
	 * Test delete agent.
	 *//*
	@Test
	public void testDeleteAgent() {

		Agent agent = agentService.findAgentByAgentId("testAgent");
		try {
			agentService.deleteAgent(agent);
			System.out.println("Agent Deletion Successful");
		} catch (Exception e) {
			System.out.println("Error while Deleting Agent");
		}
	}

	*//**
	 * Test list of agents.
	 *//*
	@Test
	public void testListOfAgents() {

		List<Agent> agentList = agentService.listAgentNotMappedwithDevice();
		System.out.println("Number Of Agent Mapped With Device : " + agentList.size());
	}

	*//**
	 * Form agent.
	 * 
	 * @return the agent
	 *//*
	public Agent formAgent() {

		Agent agent = new Agent();
		agent.setProfileId("testAgent");
		agent.setProfileType(Profile.AGENT);
		agent.setAgentType(agentService.findAgentTypeById(1));
		agent.setServicePlace(locationService.findServicePlaceById(1));
		agent.setCreateTime(new Date());

		*//** Form Personal Info **//*
		PersonalInfo personalInfo = new PersonalInfo();
		personalInfo.setFirstName("First Name");
		personalInfo.setLastName("Last Name");
		personalInfo.setIdentityNo("P101");
		personalInfo.setIdentityType(PersonalInfo.Identity.BANK_PASSBOOK.toString());
		personalInfo.setDateOfBirth(new Date());
		personalInfo.setGender(PersonalInfo.Gender.MALE.toString());
		agent.setPersonalInfo(personalInfo);

		*//** Form Contact Info **//*
		ContactInfo contactInfo = new ContactInfo();
		contactInfo.setAddress1("16,Ram Nagar");
		contactInfo.setEmail("test@gmail.com");
		contactInfo.setMobileNo("9876543212");
		contactInfo.setPinCode("642001");
		agent.setCurrentContactInfo(contactInfo);

		return agent;
	}

	*//**
	 * Test get agent encrypt passsword.
	 *//*
	@Test
	public void testGetAgentEncryptPasssword() {

		List<Agent> listOfAgents = agentService.listOfAgents();
		for (Agent agent : listOfAgents) {
			if (!StringUtil.isEmpty(agent.getProfileId()) && !StringUtil.isEmpty(agent.getPassword())
					&& agent.getPassword().length() <= 6)
				System.out.println("UPDATE AGENT_PROF SET PASSWORD='"
						+ cryptoUtil.encrypt(StringUtil.getMulipleOfEight(agent.getProfileId() + agent.getPassword()))
						+ "' WHERE PROF_ID='" + agent.getId() + "'");
		}
	}

}
*/