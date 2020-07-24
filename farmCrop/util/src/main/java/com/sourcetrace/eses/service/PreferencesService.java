/*
 * PreferencesService.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ese.entity.txn.fee.TxnCommissionFee;
import com.ese.entity.txn.mfi.InterestRateHistory;
import com.ese.entity.util.ESESystem;
import com.ese.entity.util.LoanInterest;
import com.sourcetrace.eses.dao.ICommissionFeeDAO;
import com.sourcetrace.eses.dao.IESESystemDAO;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.util.DateUtil;


@Service
@Transactional
public class PreferencesService implements IPreferencesService {

	@Autowired
	private IESESystemDAO systemDAO;	
	
	@Autowired
	private ICommissionFeeDAO commissionFeeDAO;

	/**
	 * Sets the commission fee dao.
	 * 
	 * @param commissionFeeDao
	 *            the new commission fee dao
	 */
	public void setCommissionFeeDAO(ICommissionFeeDAO commissionFeeDao) {
		commissionFeeDAO = commissionFeeDao;
	}

	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IPreferencesService#findPrefernceById(java.lang
	 * .String)
	 */
	public ESESystem findPrefernceById(String id) {

		return systemDAO.findPrefernceById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IPreferencesService#listPrefernces()
	 */
	public List<ESESystem> listPrefernces() {

		return systemDAO.listPrefernces();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IPreferencesService#editCommissionSettings(com
	 * .ese.entity .util.ESESystem)
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void editCommissionSettings(Map<String, String> preferences) {
		// finds the existing preference
		ESESystem existingSystem = findPrefernceById("1");
		// reads the target date in existing preference
		Date nStartDateOld = DateUtil.convertStringToDate(existingSystem.getPreferences().get(ESESystem.COMMISSION_NEXT_START_DATE),
				DateUtil.DATE_FORMAT);
		if (nStartDateOld != null) {
			// reads the target date set currently
			Date nStartDateNew = DateUtil.convertStringToDate(preferences.get(ESESystem.COMMISSION_NEXT_START_DATE), DateUtil.DATE_FORMAT);
			// current target date set is less than that of existing
			int diff = nStartDateNew.compareTo(nStartDateOld);
			// disabled commission processing or frequency got shrunk
			if (!"true".equalsIgnoreCase(preferences.get(ESESystem.ENABLE_COMMISSION)) || diff < 0) {
				// reads all calculated commissions in current state
				// current state means the commissions calculated for the
				// previous
				// commission settings
				List<TxnCommissionFee> commFees = commissionFeeDAO.listCommissionFeesCurrentTxn();
				// mark as pending ,which will be processed to CBS in next turn
				for (TxnCommissionFee commFee : commFees) {
					commFee.setStatus(TxnCommissionFee.PENDING);
					commissionFeeDAO.update(commFee);
				}
				// hanshake says there is commissions in pending which can be
				// processed in its next turn itself and not to wait for target
				// date
				preferences.put(ESESystem.COMMISSION_PAYMENT_PROCESS, "1");
			}
		}
		editPreference(preferences);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IPreferencesService#editPreference(java.util.Map)
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void editPreference(Map<String, String> preferences) {
		ESESystem existingSystem = findPrefernceById("1");
		existingSystem.getPreferences().putAll(preferences);
		systemDAO.update(existingSystem);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IPreferencesService#editPreference1(java.util
	 * .Map)
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void editPreference1(Map<String, String> preferences1) {
		ESESystem existingSystem = findPrefernceById("2");
		existingSystem.getPreferences().putAll(preferences1);
		systemDAO.update(existingSystem);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IPreferencesService#editPreferenceByESE(java.
	 * util.Map)
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void editPreferenceByESE(Map<String, String> preferences) {
		ESESystem existingSystem = findPrefernceById(ESESystem.SYSTEM_SWITCH);
		existingSystem.getPreferences().putAll(preferences);
		systemDAO.update(existingSystem);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IPreferencesService#findAgentTimerValue()
	 */
	public String findAgentTimerValue() {
		return systemDAO.findAgentTimerValue();

	}
	

	public void addInterestRateHistory(InterestRateHistory interestRateHistory) {
		// TODO Auto-generated method stub
		systemDAO.addInterestRateHistory(interestRateHistory);
	}

	public List<InterestRateHistory> listInterestRateHistories() {
		// TODO Auto-generated method stub
		return systemDAO.listInterestRateHistories();
	}

	public InterestRateHistory findLastInactiveInterestRateHistory() {
		// TODO Auto-generated method stub
		return systemDAO.findLastInactiveInterestRateHistory();
	}

	public InterestRateHistory findLastInactiveInterestRateHistoryForExFarmers() {
		// TODO Auto-generated method stub
		return systemDAO.findLastInactiveInterestRateHistoryForExFarmers();
	}

	public InterestRateHistory findLastInterestRateHistory() {
		// TODO Auto-generated method stub
		return systemDAO.findLastInterestRateHistory();
	}

	public InterestRateHistory findLastInterestRateHistoryForExFarmers() {
		// TODO Auto-generated method stub
		return systemDAO.findLastInterestRateHistoryForExFarmers();
	}



	@Override
	public String findPrefernceByName(String enableMultiProduct) 
	{
		return systemDAO.findPrefernceByName(enableMultiProduct);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void addOrganisationESE(ESESystem ese) 
	{
		systemDAO.save(ese);
	}



	@Override
	public void editOrganisationPreference(Map<String, String> preferences, ESESystem ese) {
	
		ese.getPreferences().putAll(preferences);
		systemDAO.update(ese);
	}
	
	
		public ESESystem findPrefernceByOrganisationId(String organisationId) {

			return systemDAO.findPrefernceByOrganisationId(organisationId);
		}


		@Override
		public ESESystem findPrefernceById(String id, String tenant) {
			
			return systemDAO.findPrefernceById(id, tenant);
		}



		@Override
		public List<java.lang.Object[]> findAgroPrefernceDetailById(String id) {
			// TODO Auto-generated method stub
			return systemDAO.findAgroPrefernceDetailById(id);
		}



		@Override
		public List<java.lang.Object[]> listPreferncesByName(String prefName) {
			return systemDAO.listPreferncesByName(prefName);
		}

		public void addLoanInfo(LoanInterest loanInterest) {
			// TODO Auto-generated method stub
			systemDAO.save(loanInterest);
		}



		@Override
		public List<LoanInterest> listLoanList() {
			// TODO Auto-generated method stub
			return systemDAO.listLoanList();
		}



		@Override
		public LoanInterest findRangeFromLoanInterest(long minRange,long maxRange) {
			// TODO Auto-generated method stub
			return systemDAO.findRangeFromLoanInterest(minRange,maxRange);
		}
		

}
