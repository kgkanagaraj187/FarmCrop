package com.sourcetrace.eses.dao;

import java.util.List;

import com.ese.entity.txn.mfi.InterestRateHistory;
import com.ese.entity.util.ESESystem;
import com.ese.entity.util.LoanInterest;
import com.sourcetrace.eses.txn.schema.Object;

public interface IESESystemDAO extends IESEDAO{
	public ESESystem findESESystem();

	public List<ESESystem> listPrefernces();

	public ESESystem findPrefernceById(String id);
	
	public ESESystem findPrefernceById(String id, String tenantId);

	public String findAgentTimerValue();

	public InterestRateHistory findLastInterestRateHistory();

	public InterestRateHistory findLastInterestRateHistoryForExFarmers();

	public InterestRateHistory findLastInactiveInterestRateHistory();

	public InterestRateHistory findLastInactiveInterestRateHistoryForExFarmers();

	public void addInterestRateHistory(InterestRateHistory interestRateHistory);

	public List<InterestRateHistory> listInterestRateHistories();

	public String findPrefernceByName(String enableMultiProduct);
	public ESESystem findPrefernceByOrganisationId(String organisationId);

	public List<java.lang.Object[]> findAgroPrefernceDetailById(String id);

	public List<java.lang.Object[]> listPreferncesByName(String prefName);

	public LoanInterest findRangeFromLoanInterest(long minRange,long maxRange);

	public List<LoanInterest> listLoanList();

	
}
