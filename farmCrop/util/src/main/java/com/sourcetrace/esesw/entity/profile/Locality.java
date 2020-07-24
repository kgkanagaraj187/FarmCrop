/*
 * Locality.java
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
 * The Class Locality.
 * 
 * @author $Author: moorthy $
 * @version $Rev: 1166 $ $Date: 2010-04-26 10:33:59 +0530 (Mon, 26 Apr 2010) $
 */
public class Locality {
	public static final int MAX_LENGTH_NAME = 35;
	private long id;
	private String name;
	private String code;
	private State state;
	private Set<Municipality> municipalities;
	private long revisionNo;
	private String branchId;
	private List<String> branchesList;

	/**
	 * Gets the state.
	 * 
	 * @return the state
	 */
	@NotNull(message = "empty.state")
	public State getState() {

		return state;
	}

	/**
	 * Sets the state.
	 * 
	 * @param state
	 *            the new state
	 */
	public void setState(State state) {

		this.state = state;
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
	 * Gets the municipalities.
	 * 
	 * @return the municipalities
	 */
	public Set<Municipality> getMunicipalities() {

		return municipalities;
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
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(long id) {

		this.id = id;
	}

	/**
	 * Sets the municipalities.
	 * 
	 * @param municipalities
	 *            the new municipalities
	 */
	public void setMunicipalities(Set<Municipality> municipalities) {

		this.municipalities = municipalities;
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
	 * Gets the code.
	 * 
	 * @return the code
	 */
	@Length(max = 8, message = "length.code")
	@Pattern(groups = First.class, regexp = "[^\\p{Punct}]+$", message = "pattern.code")
	@NotEmpty(message = "empty.code")
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
