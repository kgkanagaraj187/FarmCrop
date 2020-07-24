/*
 * Procurement.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.profile.Vendor;
import com.sourcetrace.esesw.entity.profile.Village;

public class LoanDistribution implements Serializable {

	private static final long serialVersionUID = -691522973746677689L;
	
	public static enum LoanStatus {
		CLOSED, PARTIAL, ACTIVE,PENTING
	}
	

	public static final String LOAN_DISTRIBUTION_AMOUNT = "LOAN DISBURSEMENT AMOUNT";	
	public static final String TXN_TYPE = "701";	
	public static final String LOAN_DISTRIBUTION = "LOAN DISBURSEMENT";
	public static final String LOAN_DISTRIBUTION_PAYMENT_AMOUNT="LOAN DISBURSEMENT";
	private long id;
	private Date loanDate;
	private int loanTo;	
	private Village village;
	private AgroTransaction agroTransaction;
	private Farmer farmer;
	private Farmer group;
	private int loanCategory;
	private Set<LoanDistributionDetail> loanDistributionDetail;	
	private String createdUser;
	private String updatedUser;
	private Date createdDate;
	private Date updatedDate;	
	private String branchId;
	private String loanAccNo;
	private double costToFarmer;
	private double currentQty;
	private double downPaymentQty;
	private double downPaymentAmt;
	private double interestPercentage;
	private double interestAmt;
	private double totalCostToFarmer;
	private int loanTenure;
	private double loanTenureAmt;
	private double monthlyRepaymentAmt;
	private int loanStatus;
	private Map<String, String> filterData;
	private Vendor vendor;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getLoanDate() {
		return loanDate;
	}
	public void setLoanDate(Date loanDate) {
		this.loanDate = loanDate;
	}
	public int getLoanTo() {
		return loanTo;
	}
	public void setLoanTo(int loanTo) {
		this.loanTo = loanTo;
	}
	public Village getVillage() {
		return village;
	}
	public void setVillage(Village village) {
		this.village = village;
	}
	public AgroTransaction getAgroTransaction() {
		return agroTransaction;
	}
	public void setAgroTransaction(AgroTransaction agroTransaction) {
		this.agroTransaction = agroTransaction;
	}
	public Farmer getFarmer() {
		return farmer;
	}
	public void setFarmer(Farmer farmer) {
		this.farmer = farmer;
	}

	public Farmer getGroup() {
		return group;
	}
	public void setGroup(Farmer group) {
		this.group = group;
	}
	public int getLoanCategory() {
		return loanCategory;
	}
	public void setLoanCategory(int loanCategory) {
		this.loanCategory = loanCategory;
	}
	public Set<LoanDistributionDetail> getLoanDistributionDetail() {
		return loanDistributionDetail;
	}
	public void setLoanDistributionDetail(Set<LoanDistributionDetail> loanDistributionDetail) {
		this.loanDistributionDetail = loanDistributionDetail;
	}
	public String getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}
	public String getUpdatedUser() {
		return updatedUser;
	}
	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getLoanAccNo() {
		return loanAccNo;
	}
	public void setLoanAccNo(String loanAccNo) {
		this.loanAccNo = loanAccNo;
	}
	public double getCostToFarmer() {
		return costToFarmer;
	}
	public void setCostToFarmer(double costToFarmer) {
		this.costToFarmer = costToFarmer;
	}
	public double getCurrentQty() {
		return currentQty;
	}
	public void setCurrentQty(double currentQty) {
		this.currentQty = currentQty;
	}
	public double getDownPaymentAmt() {
		return downPaymentAmt;
	}
	public void setDownPaymentAmt(double downPaymentAmt) {
		this.downPaymentAmt = downPaymentAmt;
	}
	
	public double getDownPaymentQty() {
		return downPaymentQty;
	}
	public void setDownPaymentQty(double downPaymentQty) {
		this.downPaymentQty = downPaymentQty;
	}
	public double getInterestPercentage() {
		return interestPercentage;
	}
	public void setInterestPercentage(double interestPercentage) {
		this.interestPercentage = interestPercentage;
	}
	public int getLoanTenure() {
		return loanTenure;
	}
	public void setLoanTenure(int loanTenure) {
		this.loanTenure = loanTenure;
	}
	public double getMonthlyRepaymentAmt() {
		return monthlyRepaymentAmt;
	}
	public void setMonthlyRepaymentAmt(double monthlyRepaymentAmt) {
		this.monthlyRepaymentAmt = monthlyRepaymentAmt;
	}
	public int getLoanStatus() {
		return loanStatus;
	}
	public void setLoanStatus(int loanStatus) {
		this.loanStatus = loanStatus;
	}
	public double getTotalCostToFarmer() {
		return totalCostToFarmer;
	}
	public void setTotalCostToFarmer(double totalCostToFarmer) {
		this.totalCostToFarmer = totalCostToFarmer;
	}
	public double getInterestAmt() {
		return interestAmt;
	}
	public void setInterestAmt(double interestAmt) {
		this.interestAmt = interestAmt;
	}
	public double getLoanTenureAmt() {
		return loanTenureAmt;
	}
	public void setLoanTenureAmt(double loanTenureAmt) {
		this.loanTenureAmt = loanTenureAmt;
	}
	public Map<String, String> getFilterData() {
		return filterData;
	}
	public void setFilterData(Map<String, String> filterData) {
		this.filterData = filterData;
	}
	public Vendor getVendor() {
		return vendor;
	}
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
	

	


}
