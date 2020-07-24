/*
 * State.java
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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.sourcetrace.eses.util.First;

/**
 * The Class State.
 * 
 * @author $Author: ganesh $
 * @version $Rev: 595 $ $Date: 2009-12-24 15:06:39 +0530 (Thu, 24 Dec 2009) $
 */
public class State {
	public static final int MAX_LENGTH_CODE = 8;
	public static final int MAX_LENGTH_NAME = 35;
	private String code;
	private long id;
	private Country country;
	private Set<Locality> localities;
	private String name;
	private long revisionNo;
	private String branchId;
	/**
	 * Transient variable
	 */
	private List<String> branchesList;

	/**
	 * Gets the country.
	 * 
	 * @return the country
	 */
	
	public Country getCountry() {

		return country;
	}

	/**
	 * Sets the country.
	 * 
	 * @param country
	 *            the new country
	 */
	public void setCountry(Country country) {

		this.country = country;
	}

	/**
	 * Gets the code.
	 * 
	 * @return the code
	 */
	
	public String getCode() {

		return code;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {

		return id;
	}

	/**
	 * Gets the localities.
	 * 
	 * @return the localities
	 */
	public Set<Locality> getLocalities() {

		return localities;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	@Length(max = MAX_LENGTH_NAME, message = "length.name")
	@Pattern(groups = First.class, regexp = "[^\\p{Punct}]+$", message = "pattern.name")
	@NotEmpty(message = "empty.name")
	public String getName() {

		return name;
	}

	/**
	 * Sets the code.
	 * 
	 * @param code
	 *            the new code
	 */
	public void setCode(String code) {

		this.code = code;
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
	 * Sets the localities.
	 * 
	 * @param localities
	 *            the new localities
	 */
	public void setLocalities(Set<Locality> localities) {

		this.localities = localities;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {

		this.name = name;
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
	 * Gets the revision no.
	 * 
	 * @return the revision no
	 */
	public long getRevisionNo() {

		return revisionNo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return name;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public List<String> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}

}
