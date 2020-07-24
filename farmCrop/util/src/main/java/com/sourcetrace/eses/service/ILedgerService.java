package com.sourcetrace.eses.service;

import com.ese.entity.util.LoanLedger;
import com.sourcetrace.eses.order.entity.txn.PaymentLedger;

public interface ILedgerService {
    

    public void save(PaymentLedger ledger);

	public void save(LoanLedger loanLedger);

}
