/*
 * SwitchTxnCommission.java
 * Copyright (c) 2010-2011, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

/**
 * The Class TxnCommissionConfig.
 * 
 * @author $Author: aravind $
 * @version $Rev: 510 $ $Date: 2009-12-10 17:35:59 +0530 (Thu, 10 Dec 2009) $
 */
public class SwitchTxnCommission {

    private long id;
    private CommissionType type;
    private double clientCommission;
    private double providerCommission;
    private double switchShare;
    private double affiliateShare;
    private double providerShare;
    private String commissionDetectionType;

    /**
     * Gets the switch share.
     * 
     * @return the switch share
     */
    public double getSwitchShare() {
    
        return switchShare;
    }

    /**
     * Sets the switch share.
     * 
     * @param switchShare the new switch share
     */
    public void setSwitchShare(double switchShare) {
    
        this.switchShare = switchShare;
    }

    /**
     * Gets the affiliate share.
     * 
     * @return the affiliate share
     */
    public double getAffiliateShare() {
    
        return affiliateShare;
    }

    /**
     * Sets the affiliate share.
     * 
     * @param affiliateShare the new affiliate share
     */
    public void setAffiliateShare(double affiliateShare) {
    
        this.affiliateShare = affiliateShare;
    }

    /**
     * Gets the provider share.
     * 
     * @return the provider share
     */
    public double getProviderShare() {
    
        return providerShare;
    }

    /**
     * Sets the provider share.
     * 
     * @param providerShare the new provider share
     */
    public void setProviderShare(double providerShare) {
    
        this.providerShare = providerShare;
    }

    /**
     * Gets the type.
     * 
     * @return the type
     */
    public CommissionType getType() {

        return type;
    }

    /**
     * Sets the type.
     * 
     * @param type the new type
     */
    public void setType(CommissionType type) {

        this.type = type;
    }

    /**
     * Gets the client commission.
     * 
     * @return the client commission
     */
    public double getClientCommission() {

        return clientCommission;
    }

    /**
     * Sets the client commission.
     * 
     * @param clientCommission the new client commission
     */
    public void setClientCommission(double clientCommission) {

        this.clientCommission = clientCommission;
    }

    /**
     * Gets the provider commission.
     * 
     * @return the provider commission
     */
    public double getProviderCommission() {

        return providerCommission;
    }

    /**
     * Sets the provider commission.
     * 
     * @param providerCommission the new provider commission
     */
    public void setProviderCommission(double providerCommission) {

        this.providerCommission = providerCommission;
    }

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Sets the id.
     * 
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Gets the commission detection type.
     * 
     * @return the commission detection type
     */
    public String getCommissionDetectionType() {
    
        return commissionDetectionType;
    }

    /**
     * Sets the commission detection type.
     * 
     * @param commissionDetectionType the new commission detection type
     */
    public void setCommissionDetectionType(String commissionDetectionType) {
    
        this.commissionDetectionType = commissionDetectionType;
    }

  
    
}
