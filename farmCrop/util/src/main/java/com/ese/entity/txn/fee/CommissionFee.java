/**
 * CommissionFee.java
 * Copyright (c) 2008, Source Trace Systems
 * ALL RIGHTS RESERVED
 */
package com.ese.entity.txn.fee;

import java.text.DecimalFormat;
import java.util.Currency;

import javax.validation.constraints.Min;

import com.sourcetrace.eses.entity.TransactionType;

// TODO: Auto-generated Javadoc
/**
 * The Class CommissionFee.
 * @author $Author: antronivan $
 * @version $Rev: 422 $, $Date: 2009-11-23 08:19:13 +0530 (Mon, 23 Nov 2009) $
 */
public class CommissionFee implements Comparable<CommissionFee> {

    public static final int FIXED_AMOUNT = 1;
    public static final int PERCENT_AMOUNT = 2;
    public static final int PERCENT_AMOUNT_MINMAX = 3;
    public static final int TXN_AMOUNT = 4;
    public static final int TXN_VOLUME = 5;

    public static final int CLIENT = 1;
    public static final int AGENT = 2;
    public static final int BANK = 3;
    public static final int AGENT_BANK = 4;
    public static final int CLIENT_BANK = 5;
    public static final int CLIENT_AGENT = 6;

    public static final String UPTO = "1";
    public static final String ABOVE = "2";
    public static final String ABOVE_FROM = "3";
    public static final String BELOW = "4";

    private long id;
    private int type;
    private double amount;
    private long startRange;
    private long endRange;
    private int debit;
    private int credit;
    private double percentage;
    private double minimumAmount;
    private double maximumAmount;
    private Currency currency;
    private TransactionType txnType;
    private int position;
    // transient properties for currency
    private double amountInCdollar;
    private double percentageInCdollar;
    private double minimumAmountInCdollar;
    private double maximumAmountInCdollar;
    private String condition;// Simple||Complex
    private long limit;
    DecimalFormat decimal = new DecimalFormat("0.00");

    /**
     * Gets the position.
     * @return the position
     */
    public int getPosition() {

        return position;
    }

    /**
     * Sets the position.
     * @param position the position to set
     */
    public void setPosition(int position) {

        this.position = position;
    }

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
     * Gets the type.
     * @return the type
     */
    public int getType() {

        return type;
    }

    /**
     * Sets the type.
     * @param type the new type
     */
    public void setType(int type) {

        this.type = type;
    }

    /**
     * Gets the amount.
     * @return the amount
     */
    @Min(value = 0, message = "min.amount")
    public double getAmount() {

        return amount;
    }

    /**
     * Sets the amount.
     * @param amount the new amount
     */
    public void setAmount(double amount) {

        this.amount = amount;

    }

    /**
     * Gets the start range.
     * @return the start range
     */

    /**
     * Gets the debit.
     * @return the debit
     */
    public int getDebit() {

        return debit;
    }

    /**
     * Sets the debit.
     * @param debit the new debit
     */
    public void setDebit(int debit) {

        this.debit = debit;
    }

    /**
     * Gets the start range.
     * @return the start range
     */
    public long getStartRange() {

        return startRange;
    }

    /**
     * Sets the start range.
     * @param startRange the new start range
     */
    public void setStartRange(long startRange) {

        this.startRange = startRange;
    }

    /**
     * Gets the end range.
     * @return the end range
     */
    @Min(value = 0, message = "pattern.endRange")
    public long getEndRange() {

        return endRange;
    }

    /**
     * Sets the end range.
     * @param endRange the new end range
     */
    public void setEndRange(long endRange) {

        this.endRange = endRange;
    }

    /**
     * Gets the credit.
     * @return the credit
     */
    public int getCredit() {

        return credit;
    }

    /**
     * Sets the credit.
     * @param credit the new credit
     */
    public void setCredit(int credit) {

        this.credit = credit;
    }

    /**
     * Gets the percentage.
     * @return the percentage
     */
    public double getPercentage() {

        return percentage;
    }

    /**
     * Sets the percentage.
     * @param percentage the new percentage
     */
    public void setPercentage(double percentage) {

        this.percentage = percentage;
    }

    /**
     * Gets the minimum amount.
     * @return the minimum amount
     */
    public double getMinimumAmount() {

        return minimumAmount;
    }

    /**
     * Sets the minimum amount.
     * @param minimumAmount the new minimum amount
     */
    public void setMinimumAmount(double minimumAmount) {

        this.minimumAmount = minimumAmount;
    }

    /**
     * Gets the maximum amount.
     * @return the maximum amount
     */
    public double getMaximumAmount() {

        return maximumAmount;
    }

    /**
     * Sets the maximum amount.
     * @param maximumAmount the new maximum amount
     */
    public void setMaximumAmount(double maximumAmount) {

        this.maximumAmount = maximumAmount;
    }

    /**
     * Gets the currency.
     * @return the currency
     */
    public Currency getCurrency() {

        return currency;
    }

    /**
     * Sets the currency.
     * @param currency the new currency
     */

    public void setCurrency(Currency currency) {

        this.currency = currency;
    }

    /**
     * Gets the txn type.
     * @return the txn type
     */
    public TransactionType getTxnType() {

        return txnType;
    }

    /**
     * Sets the txn type.
     * @param txnType the new txn type
     */
    public void setTxnType(TransactionType txnType) {

        this.txnType = txnType;
    }

    /**
     * Gets the amount in cdollar.
     * @return the amount in cdollar
     */
    public double getAmountInCdollar() {

        return amountInCdollar;
    }

    /**
     * Sets the amount in cdollar.
     * @param amountInCdollar the new amount in cdollar
     */
    public void setAmountInCdollar(double amountInCdollar) {

        this.amountInCdollar = Double.valueOf(decimal.format(amountInCdollar));
    }

    /**
     * Gets the percentage in cdollar.
     * @return the percentage in cdollar
     */
    public double getPercentageInCdollar() {

        return percentageInCdollar;
    }

    /**
     * Sets the percentage in cdollar.
     * @param percentageInCdollar the new percentage in cdollar
     */
    public void setPercentageInCdollar(double percentageInCdollar) {

        this.percentageInCdollar = percentageInCdollar;
    }

    /**
     * Gets the minimum amount in cdollar.
     * @return the minimum amount in cdollar
     */
    public double getMinimumAmountInCdollar() {

        return minimumAmountInCdollar;
    }

    /**
     * Sets the minimum amount in cdollar.
     * @param minimumAmountInCdollar the new minimum amount in cdollar
     */
    public void setMinimumAmountInCdollar(double minimumAmountInCdollar) {

        this.minimumAmountInCdollar = Double.valueOf(decimal.format(minimumAmountInCdollar));
    }

    /**
     * Gets the maximum amount in cdollar.
     * @return the maximum amount in cdollar
     */
    public double getMaximumAmountInCdollar() {

        return maximumAmountInCdollar;
    }

    /**
     * Sets the maximum amount in cdollar.
     * @param maximumAmountInCdollar the new maximum amount in cdollar
     */
    public void setMaximumAmountInCdollar(double maximumAmountInCdollar) {

        this.maximumAmountInCdollar = Double.valueOf(decimal.format(maximumAmountInCdollar));
    }

    /**
     * @return the condition
     */
    public String getCondition() {

        return condition;
    }

    /**
     * @param condition the condition to set
     */
    public void setCondition(String condition) {

        this.condition = condition;
    }

    /**
     * @return the limit
     */
    public long getLimit() {

        return limit;
    }

    /**
     * @param limit the limit to set
     */
    public void setLimit(long limit) {

        this.limit = limit;
    }

    public int compareTo(CommissionFee arg0) {

        long diff = 0;
        try {
            diff = this.getStartRange() - arg0.getStartRange();
        } catch (Exception e) {

        }
        String s = String.valueOf(diff);
        return Integer.parseInt(s);
    }
}
