/*
 * ResponseParameter.java
 * Copyright (c) 2008-2010, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import com.sourcetrace.eses.util.StringUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class ResponseParameter.
 * 
 * @author $Author:$
 * @version $Rev:$ $Date:$
 */
public class ResponseParameter implements Comparable<ResponseParameter> {

	/** The id. */
	private long id;

	/** The name. */
	private String name;

	/** The type. */
	private ParameterType type;

	// transient entity

	/** The response property. */
	SwitchTxnResponseProperty responseProperty;

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
	 * Gets the name.
	 * 
	 * @return the name
	 */
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
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public ParameterType getType() {

		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(ParameterType type) {

		this.type = type;
	}

	/**
	 * Gets the response property.
	 * 
	 * @return the response property
	 */
	public SwitchTxnResponseProperty getResponseProperty() {
		return responseProperty;
	}

	/**
	 * Sets the response property.
	 * 
	 * @param responseProperty
	 *            the new response property
	 */
	public void setResponseProperty(SwitchTxnResponseProperty responseProperty) {
		this.responseProperty = responseProperty;
	}

	public int compareTo(ResponseParameter param) {

		int diff = 0;
		if (!StringUtil.isEmpty(param.getResponseProperty())) {
			if (this.responseProperty.getOrder() != param.getResponseProperty()
					.getOrder()) {
				if (this.responseProperty.getOrder() > param
						.getResponseProperty().getOrder()) {
					diff = 1;
				} else {
					diff = -1;
				}
			}
		}

		return diff;
	}

}
