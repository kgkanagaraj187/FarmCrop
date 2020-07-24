/*
 * ServicePlace.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.util.entity;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sourcetrace.eses.util.First;
import com.sourcetrace.eses.util.Second;
@GroupSequence({ServicePlace.class, First.class, Second.class})
public class ServicePlace implements Comparable<ServicePlace> {

	private long id;
	private String code;
	private String name;
	private ServicePlaceType servPlaceType;

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
	
//	@NotEmpty(message = "empty.servicePlace.code")
	@NotNull(message = "empty.servicePlace.code")
	@Size(groups = Second.class, max = 25, message = "length.servicePlacecode")
	@Pattern(groups = First.class,  regexp = "[[^@#$%&*();:,{}^<>?+|^'!^/=\"\\_-]]+$", message = "pattern.servicePlacecode")
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
//	@NotEmpty(message = "empty.servicePlace.name")
    @NotNull(message = "empty.servicePlace.name")
	@Size(groups = Second.class, max = 25, message = "length.servicePlacename")
	@Pattern(groups = First.class,  regexp = "[[^@#$%&*();:,{}^<>?+|^'!^/=\"\\_-]]+$", message = "pattern.servicePlacename")
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
	 * Gets the serv place type.
	 * 
	 * @return the serv place type
	 */
	public ServicePlaceType getServPlaceType() {
		return servPlaceType;
	}

	/**
	 * Sets the serv place type.
	 * 
	 * @param servPlaceType
	 *            the new serv place type
	 */
	public void setServPlaceType(ServicePlaceType servPlaceType) {
		this.servPlaceType = servPlaceType;
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ServicePlace object) {

		int value = 0;

		ServicePlace servicePlace = (ServicePlace) object;

		if (this.id > servicePlace.id)
			value = 1;
		else if (this.id < servicePlace.id)
			value = -1;
		else if (this.id == servicePlace.id)
			value = 0;

		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {

		if (other == null) {
			return false;
		}
		if (other == this) {
			return true;
		}
		if (other.getClass() != getClass()) {
			return false;
		}

		ServicePlace rhs = (ServicePlace) other;
		return new EqualsBuilder().append(id, rhs.id).append(name, rhs.name)
				.isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {

		return new HashCodeBuilder(43, 11).append(id).append(name).toHashCode();
	}

}
