/*
 * Contract.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.io.Serializable;
import java.util.Set;

import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.Season;

public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Integer WRITE_OFF = 1;
    public static final Integer CARRY_FORWARD = 2;

    public static final String CONTRACT_TXN = "500";
    public static final String CONTRACT_TXN_DESCRIPTION = "CONTRACT CARRY FORWARDED";

    public static enum Status {
        INACTIVE, ACTIVE
    }

    private long id;
    private String contractNo;
    private Farmer farmer;
    private ProcurementProduct procurementProduct;
    private Season season;
    private Set<PricePattern> pricePatterns;
    private ESEAccount account;
    private double acre;
    private double price;
    private int status;

    // Transient Variable
    private PricePattern pricePattern;
    private String accountId;
    private double finalBalance;
    private int existingContractDecision;
    private int balanceType;

    /**
     * Gets the id.
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Gets the contract no.
     * @return the contract no
     */
    public String getContractNo() {

        return contractNo;
    }

    /**
     * Sets the contract no.
     * @param contractNo the new contract no
     */
    public void setContractNo(String contractNo) {

        this.contractNo = contractNo;
    }

    /**
     * Gets the farmer.
     * @return the farmer
     */
    public Farmer getFarmer() {

        return farmer;
    }

    /**
     * Sets the farmer.
     * @param farmer the new farmer
     */
    public void setFarmer(Farmer farmer) {

        this.farmer = farmer;
    }

    /**
     * Gets the procurement product.
     * @return the procurement product
     */
    public ProcurementProduct getProcurementProduct() {

        return procurementProduct;
    }

    /**
     * Sets the procurement product.
     * @param procurementProduct the new procurement product
     */
    public void setProcurementProduct(ProcurementProduct procurementProduct) {

        this.procurementProduct = procurementProduct;
    }

    /**
     * Gets the season.
     * @return the season
     */
    public Season getSeason() {

        return season;
    }

    /**
     * Sets the season.
     * @param season the new season
     */
    public void setSeason(Season season) {

        this.season = season;
    }

    /**
     * Gets the price pattern.
     * @return the price pattern
     */
    public PricePattern getPricePattern() {

        return pricePattern;
    }

    /**
     * Sets the price pattern.
     * @param pricePattern the new price pattern
     */
    public void setPricePattern(PricePattern pricePattern) {

        this.pricePattern = pricePattern;
    }

    /**
     * Gets the account.
     * @return the account
     */
    public ESEAccount getAccount() {

        return account;
    }

    /**
     * Sets the account.
     * @param account the new account
     */
    public void setAccount(ESEAccount account) {

        this.account = account;
    }

    /**
     * Gets the acre.
     * @return the acre
     */
    public double getAcre() {

        return acre;
    }

    /**
     * Sets the acre.
     * @param acre the new acre
     */
    public void setAcre(double acre) {

        this.acre = acre;
    }

    /**
     * Gets the price.
     * @return the price
     */
    public double getPrice() {

        return price;
    }

    /**
     * Sets the price.
     * @param price the new price
     */
    public void setPrice(double price) {

        this.price = price;
    }

    /**
     * Gets the status.
     * @return the status
     */
    public int getStatus() {

        return status;
    }

    /**
     * Sets the status.
     * @param status the new status
     */
    public void setStatus(int status) {

        this.status = status;
    }

    /**
     * Sets the final balance.
     * @param finalBalance the new final balance
     */
    public void setFinalBalance(double finalBalance) {

        this.finalBalance = finalBalance;
    }

    /**
     * Gets the final balance.
     * @return the final balance
     */
    public double getFinalBalance() {

        return finalBalance;
    }

    /**
     * Sets the account id.
     * @param accountId the new account id
     */
    public void setAccountId(String accountId) {

        this.accountId = accountId;
    }

    /**
     * Gets the account id.
     * @return the account id
     */
    public String getAccountId() {

        return accountId;
    }

    /**
     * Sets the existing contract decision.
     * @param existingContractDecision the new existing contract decision
     */
    public void setExistingContractDecision(int existingContractDecision) {

        this.existingContractDecision = existingContractDecision;
    }

    /**
     * Gets the existing contract decision.
     * @return the existing contract decision
     */
    public int getExistingContractDecision() {

        return existingContractDecision;
    }

    /**
     * Sets the price patterns.
     * @param pricePatterns the new price patterns
     */
    public void setPricePatterns(Set<PricePattern> pricePatterns) {

        this.pricePatterns = pricePatterns;
    }

    /**
     * Gets the price patterns.
     * @return the price patterns
     */
    public Set<PricePattern> getPricePatterns() {

        return pricePatterns;
    }

    /**
     * Sets the balance type.
     * @param balanceType the new balance type
     */
    public void setBalanceType(int balanceType) {

        this.balanceType = balanceType;
    }

    /**
     * Gets the balance type.
     * @return the balance type
     */
    public int getBalanceType() {

        return balanceType;
    }

}
