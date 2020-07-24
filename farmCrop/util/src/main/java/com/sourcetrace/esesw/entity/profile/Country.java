/*
 * Country.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
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
import javax.validation.constraints.Size;

import com.sourcetrace.eses.util.First;
import com.sourcetrace.eses.util.Second;

// TODO: Auto-generated Javadoc

public class Country {

	private long id;
	private String code;
	private String name;
	private Set<State> states;
	private char isActive;
	private long revisionNo;
	private String branchId;
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
	 * Gets the code.
	 * 
	 * @return the code
	 */
	/*
	 * @NotEmpty(message = "empty.code")
	 * 
	 * @NotNull(message = "empty.code")
	 */
	// @NotBlank(message = "empty.code")
	@Size(groups = Second.class, max = 10, message = "length.code")
	@Pattern(groups = First.class, regexp = "[^\\p{Punct}]+$", message = "pattern.code")
	public String getCode() {

		return code;
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
	 * Gets the name.
	 * 
	 * @return the name
	 */
	/*
	 * @NotEmpty(message = "empty.name")
	 * 
	 * @NotNull(message = "empty.name")
	 */
	// @NotBlank(message = "empty.name")
	@Size(groups = Second.class, max = 45, message = "length.name")
	@Pattern(groups = First.class, regexp = "[^\\p{Punct}]+$", message = "pattern.name")
	public String getName() {

		return name;
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
	 * Gets the states.
	 * 
	 * @return the states
	 */
	public Set<State> getStates() {

		return states;
	}

	/**
	 * Sets the states.
	 * 
	 * @param states
	 *            the new states
	 */
	public void setStates(Set<State> states) {

		this.states = states;
	}

	@Override
	public String toString() {

		return name;
	}

	public char getIsActive() {
		return isActive;
	}

	public void setIsActive(char isActive) {
		this.isActive = isActive;
	}

	public long getRevisionNo() {
		return revisionNo;
	}

	public void setRevisionNo(long revisionNo) {
		this.revisionNo = revisionNo;
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
