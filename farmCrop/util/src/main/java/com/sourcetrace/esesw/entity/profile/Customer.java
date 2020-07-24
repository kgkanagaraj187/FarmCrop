/*
 * Customer.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

// TODO: Auto-generated Javadoc
/**
 * The Class Cutomer.
 */
public class Customer {

	private long id;
	private String customerId;
	private String customerName;
	private String customerAddress;
	private String landLine;
	private String personName;
	private String mobileNumber;
	private String email;
	private long revisionNo;
	private Set<CustomerProject> customerProjects;
	private String branchId;
	private String customerType;
	private String customerSegment;
	private Locality city;
	private Municipality municipality;
	
	/**
	 * Transient variable
	 */
	private List<String> branchesList;

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
	 * Gets the customer id.
	 * 
	 * @return the customer id
	 */
	public String getCustomerId() {

		return customerId;
	}

	/**
	 * Sets the customer id.
	 * 
	 * @param customerId
	 *            the new customer id
	 */
	public void setCustomerId(String customerId) {

		this.customerId = customerId;
	}

	/**
	 * Gets the customer name.
	 * 
	 * @return the customer name
	 */
	@Length(max = 90, message = "length.customerName")
	@Pattern(regexp = "[^\\p{Punct}]+$", message = "pattern.customerName")
	@NotEmpty(message = "empty.customerName")
	public String getCustomerName() {

		return customerName;
	}

	/**
	 * Sets the customer name.
	 * 
	 * @param customerName
	 *            the new customer name
	 */
	public void setCustomerName(String customerName) {

		this.customerName = customerName;
	}

	/**
	 * Gets the customer address.
	 * 
	 * @return the customer address
	 */

	public String getCustomerAddress() {

		return customerAddress;
	}

	/**
	 * Sets the customer address.
	 * 
	 * @param customerAddress
	 *            the new customer address
	 */
	public void setCustomerAddress(String customerAddress) {

		this.customerAddress = customerAddress;
	}

	/**
	 * Gets the land line.
	 * 
	 * @return the land line
	 */

	@Length(max = 15, message = "length.landLine")
	@Pattern(regexp = "[0-9]*", message = "pattern.landLine")
	public String getLandLine() {

		return landLine;
	}

	/**
	 * Sets the land line.
	 * 
	 * @param landLine
	 *            the new land line
	 */
	public void setLandLine(String landLine) {

		this.landLine = landLine;
	}

	/**
	 * Gets the person name.
	 * 
	 * @return the person name
	 */
	@Length(max = 90, message = "length.personName")
	public String getPersonName() {

		return personName;
	}

	/**
	 * Sets the person name.
	 * 
	 * @param personName
	 *            the new person name
	 */
	public void setPersonName(String personName) {

		this.personName = personName;
	}

	/**
	 * Gets the mobile number.
	 * 
	 * @return the mobile number
	 */
	@Length(max = 10, message = "length.mobileNumber")
	@Pattern(regexp = "[0-9]*", message = "pattern.mobileNumber")

	public String getMobileNumber() {

		return mobileNumber;
	}

	/**
	 * Sets the mobile number.
	 * 
	 * @param mobileNumber
	 *            the new mobile number
	 */
	public void setMobileNumber(String mobileNumber) {

		this.mobileNumber = mobileNumber;
	}

	/**
	 * Gets the email.
	 * 
	 * @return the email
	 */
	@Length(max = 90, message = "length.email")
	@Email(message = "pattern.email")
	public String getEmail() {

		return email;
	}

	/**
	 * Sets the email.
	 * 
	 * @param email
	 *            the new email
	 */
	public void setEmail(String email) {

		this.email = email;
	}

	/**
	 * Gets the revision no.
	 * 
	 * @return the revision no
	 */
	public long getRevisionNo() {

		return revisionNo;
	}

	/**
	 * Sets the revision no.
	 * 
	 * @param revisionNo
	 *            the new revision no
	 */
	public void setRevisionNo(long revisionNo) {

		this.revisionNo = revisionNo;
	}

	/**
	 * Gets the customer projects.
	 * 
	 * @return the customer projects
	 */
	public Set<CustomerProject> getCustomerProjects() {

		return customerProjects;
	}

	/**
	 * Sets the customer projects.
	 * 
	 * @param customerProjects
	 *            the new customer projects
	 */
	public void setCustomerProjects(Set<CustomerProject> customerProjects) {

		this.customerProjects = customerProjects;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", customerId=" + customerId + ", customerName=" + customerName + ", branchId="
				+ branchId + "]";
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getCustomerSegment() {
		return customerSegment;
	}

	public void setCustomerSegment(String customerSegment) {
		this.customerSegment = customerSegment;
	}

	public Locality getCity() {
		return city;
	}

	public void setCity(Locality city) {
		this.city = city;
	}

	public Municipality getMunicipality() {
		return municipality;
	}

	public void setMunicipality(Municipality municipality) {
		this.municipality = municipality;
	}

    public List<String> getBranchesList() {
    
        return branchesList;
    }

    public void setBranchesList(List<String> branchesList) {
    
        this.branchesList = branchesList;
    }

	
}
