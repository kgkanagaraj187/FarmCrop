package com.ese.entity.util;

import java.util.Date;

import com.sourcetrace.esesw.entity.profile.ESEAccount;

public class LoanLedger {
	
	public static enum loanStatus {
		CLOSED, PARTIAL, ACTIVE,PENTING
	}
	
	private long id;
	private Date txnTime;
	private String farmerId;
	private double actualAmount;
	private double loanInterest;
	private double finalPayAmt;	
	private double preFarmerBal;
	private double newFarmerBal;
	private String accountNo;
	private String loanDesc;
	private String txnType;
	private String branchId;
	private ESEAccount account;
	private String receiptNo;
	private String vendorId;
	private int loanStatus;
	
	
	
	public int getLoanStatus() {
		return loanStatus;
	}
	public void setLoanStatus(int loanStatus) {
		this.loanStatus = loanStatus;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getTxnTime() {
		return txnTime;
	}
	public void setTxnTime(Date txnTime) {
		this.txnTime = txnTime;
	}
	public String getFarmerId() {
		return farmerId;
	}
	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}
	
	public double getActualAmount() {
		return actualAmount;
	}
	public void setActualAmount(double actualAmount) {
		this.actualAmount = actualAmount;
	}
	public double getLoanInterest() {
		return loanInterest;
	}
	public void setLoanInterest(double loanInterest) {
		this.loanInterest = loanInterest;
	}
	public double getFinalPayAmt() {
		return finalPayAmt;
	}
	public void setFinalPayAmt(double finalPayAmt) {
		this.finalPayAmt = finalPayAmt;
	}
	public String getLoanDesc() {
		return loanDesc;
	}
	public void setLoanDesc(String loanDesc) {
		this.loanDesc = loanDesc;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public ESEAccount getAccount() {
		return account;
	}
	public void setAccount(ESEAccount account) {
		this.account = account;
	}
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public double getPreFarmerBal() {
		return preFarmerBal;
	}
	public void setPreFarmerBal(double preFarmerBal) {
		this.preFarmerBal = preFarmerBal;
	}
	public double getNewFarmerBal() {
		return newFarmerBal;
	}
	public void setNewFarmerBal(double newFarmerBal) {
		this.newFarmerBal = newFarmerBal;
	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	
	

}
