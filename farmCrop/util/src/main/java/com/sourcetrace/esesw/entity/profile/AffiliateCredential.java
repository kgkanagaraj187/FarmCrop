/*
 * AffiliateCredential.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import java.util.Map;

import org.hibernate.validator.constraints.Range;



/**
 * The Class AffiliateCredential.
 * @author $Author: ganesh $
 * @version $Rev: 690 $ $Date: 2010-01-13 12:13:16 +0530 (Wed, 13 Jan 2010) $
 */
public class AffiliateCredential {
            
	private static final int NAME_MAX_LENGTH = 10;
	private long id;
    private long maxTxnCount;
    private long accumulatedTxnCount;
    private Map<SwitchTxn, SwitchTxnCommission> txnsCommissions;

    /**
     * Gets the accumulated txn count.
     * @return the accumulated txn count
     */
    public long getAccumulatedTxnCount() {

        return accumulatedTxnCount;
    }

    /**
     * Gets the txns commissions.
     * @return the txns commissions
     */
    public Map<SwitchTxn, SwitchTxnCommission> getTxnsCommissions() {
    
        return txnsCommissions;
    }

    /**
     * Gets the id.
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Gets the max txn count.
     * @return the max txn count
     */
   @Range(max = 100000000, min = 1, message = "length.maxtxncount")
   public long getMaxTxnCount() {

        return maxTxnCount;
    }

    /**
     * Sets the accumulated txn count.
     * @param accumulatedTxnCount the new accumulated txn count
     */
    public void setAccumulatedTxnCount(long accumulatedTxnCount) {

        this.accumulatedTxnCount = accumulatedTxnCount;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Sets the max txn count.
     * @param maxTxnCount the new max txn count
     */
    public void setMaxTxnCount(long maxTxnCount) {

        this.maxTxnCount = maxTxnCount;
    }

    /**
     * Sets the txns commissions.
     * @param txnsCommissions the txns commissions
     */
    public void setTxnsCommissions(Map<SwitchTxn, SwitchTxnCommission> txnsCommissions) {

        this.txnsCommissions = txnsCommissions;
    }
}
