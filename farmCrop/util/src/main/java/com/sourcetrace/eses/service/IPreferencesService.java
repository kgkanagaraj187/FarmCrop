/*
 * IPreferencesService.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;


import java.util.List;
import java.util.Map;

import com.ese.entity.txn.mfi.InterestRateHistory;
import com.ese.entity.util.ESESystem;
import com.ese.entity.util.LoanInterest;
import com.sourcetrace.eses.txn.schema.Object;


public interface IPreferencesService {


	/**
	 * Find prefernce by id.
	 * 
	 * @param id
	 *            the id
	 * @return the eSE system
	 */
	public com.ese.entity.util.ESESystem findPrefernceById(String id);

	/**
	 * List prefernces.
	 * 
	 * @return the list< ese system>
	 */
	public List<ESESystem> listPrefernces();

	/**
	 * Edits the preference.
	 * 
	 * @param prefernces
	 *            the prefernces
	 */
	public void editPreference(Map<String, String> prefernces);

	/**
	 * Edits the commission settings.
	 * 
	 * @param prefernces
	 *            the prefernces
	 */
	public void editCommissionSettings(Map<String, String> prefernces);

	/**
	 * Edits the preference by ese.
	 * 
	 * @param preferences
	 *            the preferences
	 */
	public void editPreferenceByESE(Map<String, String> preferences);

	/**
	 * Edits the preference1.
	 * 
	 * @param preferences1
	 *            the preferences1
	 */
	public void editPreference1(Map<String, String> preferences1);

	public String findAgentTimerValue();

	public void addInterestRateHistory(InterestRateHistory interestRateHistory);

	public List<InterestRateHistory> listInterestRateHistories();

	public InterestRateHistory findLastInterestRateHistory();

	public InterestRateHistory findLastInactiveInterestRateHistory();

	public InterestRateHistory findLastInterestRateHistoryForExFarmers();

	public InterestRateHistory findLastInactiveInterestRateHistoryForExFarmers();

	public String findPrefernceByName(String enableMultiProduct);
	public void addOrganisationESE(ESESystem ese);

	public void editOrganisationPreference(Map<String, String> preferences, ESESystem ese);

	public ESESystem findPrefernceByOrganisationId(String branchId);

	public com.ese.entity.util.ESESystem findPrefernceById(String id,String tenant);

	public List<java.lang.Object[]> findAgroPrefernceDetailById(String id);
	public List<java.lang.Object[]> listPreferncesByName(String prefName);
	
	public void addLoanInfo(LoanInterest loanInterest);

	public List<LoanInterest> listLoanList();

	public LoanInterest findRangeFromLoanInterest(long minRange,long maxRange);
	
}
