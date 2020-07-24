package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;

import com.sourcetrace.esesw.entity.profile.ESEAccount;

/**
 * @author admin
 *
 */
public class FarmerBalanceReport {

	private long id;
	private String receiptNo;
	private String agentId;
	private String agentName;
	private String farmerId;
	private String farmerName;
	private String servicePointName;
	private String txnType;
	private double initialBalance;
	private double txnAmount;
	private double balanceAmount;
	private Date txnTime;
	private String txnDesc;
	private String accountNumber;
	private String accountType;
	private ESEAccount account;

	// transient variable
	private String profileId;
	private double finalBalance;
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param receiptNo the receiptNo to set
	 */
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	/**
	 * @return the receiptNo
	 */
	public String getReceiptNo() {
		return receiptNo;
	}
	/**
	 * @param agentId the agentId to set
	 */
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	/**
	 * @return the agentId
	 */
	public String getAgentId() {
		return agentId;
	}
	/**
	 * @param agentName the agentName to set
	 */
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	/**
	 * @return the agentName
	 */
	public String getAgentName() {
		return agentName;
	}
	/**
	 * @param farmerId the farmerId to set
	 */
	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}
	/**
	 * @return the farmerId
	 */
	public String getFarmerId() {
		return farmerId;
	}
	/**
	 * @param farmerName the farmerName to set
	 */
	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
	}
	/**
	 * @return the farmerName
	 */
	public String getFarmerName() {
		return farmerName;
	}
	/**
	 * @param servicePointName the servicePointName to set
	 */
	public void setServicePointName(String servicePointName) {
		this.servicePointName = servicePointName;
	}
	/**
	 * @return the servicePointName
	 */
	public String getServicePointName() {
		return servicePointName;
	}
	/**
	 * @param txnType the txnType to set
	 */
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	/**
	 * @return the txnType
	 */
	public String getTxnType() {
		return txnType;
	}
	/**
	 * @param initialBalance the initialBalance to set
	 */
	public void setInitialBalance(double initialBalance) {
		this.initialBalance = initialBalance;
	}
	/**
	 * @return the initialBalance
	 */
	public double getInitialBalance() {
		return initialBalance;
	}
	/**
	 * @param txnAmount the txnAmount to set
	 */
	public void setTxnAmount(double txnAmount) {
		this.txnAmount = txnAmount;
	}
	/**
	 * @return the txnAmount
	 */
	public double getTxnAmount() {
		return txnAmount;
	}
	/**
	 * @param balanceAmount the balanceAmount to set
	 */
	public void setBalanceAmount(double balanceAmount) {
		this.balanceAmount = balanceAmount;
	}
	/**
	 * @return the balanceAmount
	 */
	public double getBalanceAmount() {
		return balanceAmount;
	}
	/**
	 * @param txnTime the txnTime to set
	 */
	public void setTxnTime(Date txnTime) {
		this.txnTime = txnTime;
	}
	/**
	 * @return the txnTime
	 */
	public Date getTxnTime() {
		return txnTime;
	}
	/**
	 * @param txnDesc the txnDesc to set
	 */
	public void setTxnDesc(String txnDesc) {
		this.txnDesc = txnDesc;
	}
	/**
	 * @return the txnDesc
	 */
	public String getTxnDesc() {
		return txnDesc;
	}
	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}
	/**
	 * @param accountType the accountType to set
	 */
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	/**
	 * @return the accountType
	 */
	public String getAccountType() {
		return accountType;
	}
	/**
	 * @param profileId the profileId to set
	 */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}
	/**
	 * @return the profileId
	 */
	public String getProfileId() {
		return profileId;
	}
	/**
	 * @param finalBalance the finalBalance to set
	 */
	public void setFinalBalance(double finalBalance) {
		this.finalBalance = finalBalance;
	}
	/**
	 * @return the finalBalance
	 */
	public double getFinalBalance() {
		return finalBalance;
	}
	public ESEAccount getAccount() {
		return account;
	}
	public void setAccount(ESEAccount account) {
		this.account = account;
	}
	
	 
}
