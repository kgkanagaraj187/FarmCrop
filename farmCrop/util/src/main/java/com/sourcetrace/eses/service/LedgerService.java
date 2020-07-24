package com.sourcetrace.eses.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ese.entity.util.LoanLedger;
import com.sourcetrace.eses.dao.ILedgerDAO;
import com.sourcetrace.eses.order.entity.txn.PaymentLedger;

@Service
@Transactional
public class LedgerService implements ILedgerService {
	
	 @Autowired
	    private ILedgerDAO ledgerDAO;

	public ILedgerDAO getLedgerDAO() {
		return ledgerDAO;
	}

	public void setLedgerDAO(ILedgerDAO ledgerDAO) {
		this.ledgerDAO = ledgerDAO;
	}

    @Override
    public void save(PaymentLedger ledger) {

        ledgerDAO.save(ledger);
        
    }

    @Override
    public void save(LoanLedger loanLedger) {

        ledgerDAO.save(loanLedger);
        
    }
	 

}
