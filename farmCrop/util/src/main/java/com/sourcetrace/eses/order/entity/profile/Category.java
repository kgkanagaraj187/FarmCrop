/*
 * Category.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.profile;

import java.util.Set;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.sourcetrace.eses.util.First;

// TODO: Auto-generated Javadoc
/**
 * The Class Category.
 */
public class Category {

	public static final int MAX_LENGTH_NAME = 50;

	private long id;
	private String code;
	private String name;
	private Set<SubCategory> subcategory;

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
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {
		return id;
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
	 * Gets the code.
	 * 
	 * @return the code
	 */
	@Pattern(groups = First.class,regexp = "[^\\p{Punct}]+$", message = "pattern.code")
	@NotEmpty(message = "empty.code")
	public String getCode() {
		return code;
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
	 * Sets the subcategory.
	 * 
	 * @param subcategory the new subcategory
	 */
	public void setSubcategory(Set<SubCategory> subcategory) {
		this.subcategory = subcategory;
	}

	/**
	 * Gets the subcategory.
	 * 
	 * @return the subcategory
	 */
	public Set<SubCategory> getSubcategory() {
		return subcategory;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	@Length(max = MAX_LENGTH_NAME, message = "length.name")
	@NotEmpty(message = "empty.name")
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = (name == null || name.equals("")) ? code : name;
		return str;
	}

}
