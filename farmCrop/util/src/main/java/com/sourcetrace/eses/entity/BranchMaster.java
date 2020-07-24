package com.sourcetrace.eses.entity;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.sourcetrace.eses.util.StringUtil;

public class BranchMaster {
	private Long Id;
	private String branchId;
	private String name;
	private String contactPerson;
	private String phoneNo;
	private String address;
	private String parentBranch;
	private String status;
	// transient variable
	private double accountBalance;
	private String accountRupee;
	private String accountPaise;
	private long webSeq;
	private long remoteSeq;
	private long limit;
	private long webLimit;
	private String tenant;

	public static final String ACTIVE="1";
	public static final String INACTIVE="0";
	
	public static final String WEB_SEQ_APPEND = "00000";
	public static final String WEB_SEQ_LIMIT_APPEND = "99999";
	public static final String REMOTE_SEQ_APPEND = "10000";
	

	public Long getId() {

		return Id;
	}

	public void setId(Long id) {

		Id = id;
	}

	public String getBranchId() {

		return branchId;
	}

	public void setBranchId(String branchId) {

		this.branchId = branchId;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getContactPerson() {

		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {

		this.contactPerson = contactPerson;
	}

	@Length(max = 10, message = "length.phoneNo")
	@Pattern(regexp = "[0-9]*", message = "pattern.phoneNo")

	public String getPhoneNo() {

		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {

		this.phoneNo = phoneNo;
	}

	public String getAddress() {

		return address;
	}

	public void setAddress(String address) {

		this.address = address;
	}

	public double getAccountBalance() {

		if (this.getAccountRupee() == null || StringUtil.isEmpty(this.getAccountRupee())) {
			setAccountRupee("0");
		}
		if (this.getAccountPaise() == null || StringUtil.isEmpty(this.getAccountPaise())) {
			setAccountPaise("0");
		}
		return Double.valueOf(this.getAccountRupee() + "." + this.getAccountPaise());
	}

	public void setAccountBalance(double accountBalance) {

		this.accountBalance = accountBalance;
	}

	public String getAccountRupee() {

		return accountRupee;
	}

	public void setAccountRupee(String accountRupee) {

		this.accountRupee = accountRupee;
	}

	public String getAccountPaise() {

		return accountPaise;
	}

	public void setAccountPaise(String accountPaise) {

		this.accountPaise = accountPaise;
	}

	public String getParentBranch() {

		return parentBranch;
	}

	public void setParentBranch(String parentBranch) {

		this.parentBranch = parentBranch;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getWebSeq() {
		return webSeq;
	}

	public void setWebSeq(long webSeq) {
		this.webSeq = webSeq;
	}

	public long getRemoteSeq() {
		return remoteSeq;
	}

	public void setRemoteSeq(long remoteSeq) {
		this.remoteSeq = remoteSeq;
	}

	public long getLimit() {
		return limit;
	}

	public void setLimit(long limit) {
		this.limit = limit;
	}

	public long getWebLimit() {
		return webLimit;
	}

	public void setWebLimit(long webLimit) {
		this.webLimit = webLimit;
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}
	
	

}
