/*
 * BankInformation.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import com.sourcetrace.eses.entity.Warehouse;

public class BankInformation {

	private long id;
	private String accNo;
	private String bankName;
	private String branchName;
	private String sortCode;
	private String swiftCode;
	private Farmer farmer;
	private String accType;
	private String accName;
	private Warehouse warehouse;
	//Transient 
	private String accTypeCode;
	private String bankNameCode;
	private String branchNameCode;

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
	 * Gets the acc no.
	 * 
	 * @return the acc no
	 */
	public String getAccNo() {

		return accNo;
	}

	/**
	 * Sets the acc no.
	 * 
	 * @param accNo
	 *            the new acc no
	 */
	public void setAccNo(String accNo) {

		this.accNo = accNo;
	}

	/**
	 * Gets the bank name.
	 * 
	 * @return the bank name
	 */
	public String getBankName() {

		return bankName;
	}

	/**
	 * Sets the bank name.
	 * 
	 * @param bankName
	 *            the new bank name
	 */
	public void setBankName(String bankName) {

		this.bankName = bankName;
	}

	/**
	 * Gets the branch name.
	 * 
	 * @return the branch name
	 */
	public String getBranchName() {

		return branchName;
	}

	/**
	 * Sets the branch name.
	 * 
	 * @param branchName
	 *            the new branch name
	 */
	public void setBranchName(String branchName) {

		this.branchName = branchName;
	}

	/**
	 * Gets the sort code.
	 * 
	 * @return the sort code
	 */
	public String getSortCode() {

		return sortCode;
	}

	/**
	 * Sets the sort code.
	 * 
	 * @param sortCode
	 *            the new sort code
	 */
	public void setSortCode(String sortCode) {

		this.sortCode = sortCode;
	}

	/**
	 * Gets the swift code.
	 * 
	 * @return the swift code
	 */
	public String getSwiftCode() {

		return swiftCode;
	}

	/**
	 * Sets the swift code.
	 * 
	 * @param swiftCode
	 *            the new swift code
	 */
	public void setSwiftCode(String swiftCode) {

		this.swiftCode = swiftCode;
	}

	/**
	 * Gets the farmer.
	 * 
	 * @return the farmer
	 */
	public Farmer getFarmer() {

		return farmer;
	}

	/**
	 * Sets the farmer.
	 * 
	 * @param farmer
	 *            the new farmer
	 */
	public void setFarmer(Farmer farmer) {

		this.farmer = farmer;
	}

	public String getAccType() {
		return accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
	}

    public String getAccTypeCode() {
    
        return accTypeCode;
    }

    public void setAccTypeCode(String accTypeCode) {
    
        this.accTypeCode = accTypeCode;
    }

	public String getBankNameCode() {
		return bankNameCode;
	}

	public void setBankNameCode(String bankNameCode) {
		this.bankNameCode = bankNameCode;
	}

	public String getBranchNameCode() {
		return branchNameCode;
	}

	public void setBranchNameCode(String branchNameCode) {
		this.branchNameCode = branchNameCode;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}
	
	
}
