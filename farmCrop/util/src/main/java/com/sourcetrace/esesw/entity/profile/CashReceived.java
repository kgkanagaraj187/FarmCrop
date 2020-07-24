package com.sourcetrace.esesw.entity.profile;
import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;

import com.sourcetrace.eses.order.entity.txn.AgroTransaction;





public class CashReceived {
	public static final String Saving = "CR1";
	public static final String Share = "CR2";
	public static final int TXN_TYPE = 361;
	private long id;
	private Date txnDate;
	private String mobileUserName;
	private String farmerName;
	private String cashReceivedMode;
	private double amount;
	private long eseAccountId;
	private String mobileUserId;
	private String farmerId;
	private String txnDescribtion;
	private String serverUpdateTime;
	private double intialBalance;
	private double txnAmount;
	private double balAmount;
	private int txnType;
	private ESEAccount eseAccount;
	private AgroTransaction agroTransaction;

	// Transiant variables

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@NotEmpty(message = "empty.Date")
	public Date getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(Date txnDate) {
		this.txnDate = txnDate;
	}

	@NotEmpty(message = "empty.MobileUser")
	public String getMobileUserName() {
		return mobileUserName;
	}

	public void setMobileUserName(String mobileUserName) {
		this.mobileUserName = mobileUserName;
	}

	@NotEmpty(message = "empty.FarmerName")
	public String getFarmerName() {
		return farmerName;
	}

	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
	}

	@NotEmpty(message = "empty.CashReceivedMode")
	public String getCashReceivedMode() {
		return cashReceivedMode;
	}

	public void setCashReceivedMode(String cashReceivedMode) {
		this.cashReceivedMode = cashReceivedMode;
	}

	@NotEmpty(message = "empty.Amount")
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getMobileUserId() {
		return mobileUserId;
	}

	public void setMobileUserId(String mobileUserId) {
		this.mobileUserId = mobileUserId;
	}

	public String getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}

	public String getTxnDescribtion() {
		return txnDescribtion;
	}

	public void setTxnDescribtion(String txnDescribtion) {
		this.txnDescribtion = txnDescribtion;
	}

	public String getServerUpdateTime() {
		return serverUpdateTime;
	}

	public void setServerUpdateTime(String serverUpdateTime) {
		this.serverUpdateTime = serverUpdateTime;
	}

	public double getBalAmount() {
		return balAmount;
	}

	public void setBalAmount(double balAmount) {
		this.balAmount = balAmount;
	}

	public double getTxnAmount() {
		return txnAmount;
	}

	public void setTxnAmount(double txnAmount) {
		this.txnAmount = txnAmount;
	}

	public int getTxnType() {
		return txnType;
	}

	public void setTxnType(int txnType) {
		this.txnType = txnType;
	}

	public double getIntialBalance() {
		return intialBalance;
	}

	public void setIntialBalance(double intialBalance) {
		this.intialBalance = intialBalance;
	}

	public long getEseAccountId() {
		return eseAccountId;
	}

	public void setEseAccountId(long eseAccountId) {
		this.eseAccountId = eseAccountId;
	}

	public AgroTransaction getAgroTransaction() {
		return agroTransaction;
	}

	public void setAgroTransaction(AgroTransaction agroTransaction) {
		this.agroTransaction = agroTransaction;
	}

	public ESEAccount getEseAccount() {
		return eseAccount;
	}

	public void setEseAccount(ESEAccount eseAccount) {
		this.eseAccount = eseAccount;
	}

}
