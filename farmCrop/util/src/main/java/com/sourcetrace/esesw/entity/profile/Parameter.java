/*
 * Parameter.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

// TODO: Auto-generated Javadoc
/**
 * The Class Parameter.
 * 
 * @author $Author: moorthy $
 * @version $Rev: 922 $ $Date: 2010-02-23 21:08:30 +0530 (Tue, 23 Feb 2010) $
 */
public class Parameter implements Comparable<Parameter> {

	/** The id. */
	private long id;

	/** The name. */
	private String name;

	/** The type. */
	private ParameterType type;

	// following variables are transient variable used to dynamic displaying
	// properties

	/** The switch txn id. */
	private String switchTxnId;

	/** The property name. */
	private String propertyName;

	/** The property desc. */
	private String propertyDesc;

	/** The property type. */
	private String propertyType;

	/** The sequence id. */
	private int sequenceId;

	/** The tooltip. */
	private String tooltip;
	
	private String reference;

	

    public String getReference() {
    
        return reference;
    }

    public void setReference(String reference) {
    
        this.reference = reference;
    }

    public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
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
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {

		return name;
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
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(long id) {

		this.id = id;
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
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(ParameterType type) {

		this.type = type;
	}

	/**
	 * Gets the property type.
	 * 
	 * @return the property type
	 */
	public String getPropertyType() {
		return propertyType;
	}

	/**
	 * Sets the property type.
	 * 
	 * @param propertyType
	 *            the new property type
	 */
	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return the sequence id
	 */

	/**
	 * Gets the property desc.
	 * 
	 * @return the property desc
	 */
	public String getPropertyDesc() {

		return propertyDesc;
	}

	public int getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(int sequenceId) {
		this.sequenceId = sequenceId;
	}

	/**
	 * Sets the property desc.
	 * 
	 * @param propertyDesc
	 *            the new property desc
	 */
	public void setPropertyDesc(String propertyDesc) {

		this.propertyDesc = propertyDesc;
	}

	/** The property id. */
	private long propertyId;

	/**
	 * Gets the switch txn id.
	 * 
	 * @return the switch txn id
	 */
	public String getSwitchTxnId() {

		return switchTxnId;
	}

	/**
	 * Sets the switch txn id.
	 * 
	 * @param switchTxnId
	 *            the new switch txn id
	 */
	public void setSwitchTxnId(String switchTxnId) {

		this.switchTxnId = switchTxnId;
	}

	/**
	 * Gets the property id.
	 * 
	 * @return the property id
	 */
	public long getPropertyId() {

		return propertyId;
	}

	/**
	 * Sets the property id.
	 * 
	 * @param propertyId
	 *            the new property id
	 */
	public void setPropertyId(long propertyId) {

		this.propertyId = propertyId;
	}

	/**
	 * Gets the property name.
	 * 
	 * @return the property name
	 */
	public String getPropertyName() {

		return propertyName;
	}

	/**
	 * Sets the property name.
	 * 
	 * @param propertyName
	 *            the new property name
	 */
	public void setPropertyName(String propertyName) {

		this.propertyName = propertyName;
	}

	/**
	 * Equals.
	 * 
	 * @param object
	 *            the object
	 * 
	 * @return true, if equals
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {

		boolean equals = false;
		if (object instanceof Parameter) {
			Parameter another = (Parameter) object;
			equals = name.equals(another.name);
		}

		return equals;
	}

	/**
	 * Hash code.
	 * 
	 * @return the int
	 * 
	 * @see java.lang.Object#hashCode()
	 */

	@Override
	public int hashCode() {

		int hash = 0;
		try {
			hash = name.hashCode();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */

	public int compareTo(Parameter param) {

		int diff = 0;

		if (sequenceId != param.getSequenceId()) {

			if (sequenceId > param.getSequenceId()) {
				diff = 1;
			} else {
				diff = -1;
			}
		}

		return diff;
	}

}
