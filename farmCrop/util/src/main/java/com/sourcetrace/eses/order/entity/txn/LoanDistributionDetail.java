/*
 * ProcurementDetail.java
 * Copyright (c) 2012-2013, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.io.Serializable;

import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.util.profile.Product;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;

// TODO: Auto-generated Javadoc
public class LoanDistributionDetail implements Serializable {

	private static final long serialVersionUID = -2291644640015331299L;

	
	private long id;	
	private Product product;
	private String productCode;
	private double ratePerUnit;
	private double quantity;
	private double amount;
	private double totalAmt;
	private double subsidyInterest;
	private double subsidyAmt;	
	private LoanDistribution loanDistribution;
	
	
	private long distributionId;
	private long productId;
	private double totCost;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	

	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public double getRatePerUnit() {
		return ratePerUnit;
	}
	public void setRatePerUnit(double ratePerUnit) {
		this.ratePerUnit = ratePerUnit;
	}
	
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public double getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(double totalAmt) {
		this.totalAmt = totalAmt;
	}
	
	public double getSubsidyInterest() {
		return subsidyInterest;
	}
	public void setSubsidyInterest(double subsidyInterest) {
		this.subsidyInterest = subsidyInterest;
	}
	public double getSubsidyAmt() {
		return subsidyAmt;
	}
	public void setSubsidyAmt(double subsidyAmt) {
		this.subsidyAmt = subsidyAmt;
	}	
	public LoanDistribution getLoanDistribution() {
		return loanDistribution;
	}
	public void setLoanDistribution(LoanDistribution loanDistribution) {
		this.loanDistribution = loanDistribution;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public long getDistributionId() {
		return distributionId;
	}
	public void setDistributionId(long distributionId) {
		this.distributionId = distributionId;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public double getTotCost() {
		return totCost;
	}
	public void setTotCost(double totCost) {
		this.totCost = totCost;
	}


	
	
	
	
}
