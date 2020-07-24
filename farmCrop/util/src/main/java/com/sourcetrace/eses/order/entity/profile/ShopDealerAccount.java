/*
 * 
 */
package com.sourcetrace.eses.order.entity.profile;

import java.io.Serializable;
import java.util.Set;

import com.sourcetrace.esesw.entity.profile.ShopDealer;

@SuppressWarnings("serial")
public class ShopDealerAccount implements Serializable {
	private long id;
	private ShopDealer shopDealer;
	private Double creditLimit;
	private Double outstandingBalance;
	private Set<ShopDealerAccountDetail> shopDealerAccountDetails;

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
	 * @param id
	 *            the new id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the shop dealer.
	 * 
	 * @return the shop dealer
	 */
	public ShopDealer getShopDealer() {
		return shopDealer;
	}

	/**
	 * Sets the shop dealer.
	 * 
	 * @param shopDealer
	 *            the new shop dealer
	 */
	public void setShopDealer(ShopDealer shopDealer) {
		this.shopDealer = shopDealer;
	}

	/**
	 * Gets the credit limit.
	 * 
	 * @return the credit limit
	 */
	public Double getCreditLimit() {
		return creditLimit;
	}

	/**
	 * Sets the credit limit.
	 * 
	 * @param creditLimit
	 *            the new credit limit
	 */
	public void setCreditLimit(Double creditLimit) {
		this.creditLimit = creditLimit;
	}

	/**
	 * Gets the outstanding balance.
	 * 
	 * @return the outstanding balance
	 */
	public Double getOutstandingBalance() {
		return outstandingBalance;
	}

	/**
	 * Sets the outstanding balance.
	 * 
	 * @param outstandingBalance
	 *            the new outstanding balance
	 */
	public void setOutstandingBalance(Double outstandingBalance) {
		this.outstandingBalance = outstandingBalance;
	}

	/**
	 * Gets the shop dealer account details.
	 * 
	 * @return the shop dealer account details
	 */
	public Set<ShopDealerAccountDetail> getShopDealerAccountDetails() {
		return shopDealerAccountDetails;
	}

	/**
	 * Sets the shop dealer account details.
	 * 
	 * @param shopDealerAccountDetails
	 *            the new shop dealer account details
	 */
	public void setShopDealerAccountDetails(
			Set<ShopDealerAccountDetail> shopDealerAccountDetails) {
		this.shopDealerAccountDetails = shopDealerAccountDetails;
	}

	/**
	 * Gets the limit.
	 * 
	 * @return the limit
	 */
	public String getLimit() {
		return Double.toString(creditLimit);
	}

	/**
	 * Gets the outstanding.
	 * 
	 * @return the outstanding
	 */
	public String getOutstanding() {
		return Double.toString(outstandingBalance);
	}
}
