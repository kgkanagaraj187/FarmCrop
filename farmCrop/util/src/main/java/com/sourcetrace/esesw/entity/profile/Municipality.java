/*
 * Municipality.java
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

/**
 * The Class Municipality.
 * 
 * @author $Author: moorthy $
 * @version $Rev: 1166 $ $Date: 2010-04-26 10:33:59 +0530 (Mon, 26 Apr 2010) $
 */
public class Municipality {
	public static final int MAX_LENGTH_NAME = 35;
	public static final int MAX_LENGTH_LANANDLOG = 15;
	public static final int MAX_LENGTH_POSTALCOED = 10;
	public static final String EXPRESSION_LATITUDE = "(-?[0-8]?[0-9](\\.\\d*)?)|-?90(\\.[0]*)?";
	public static final String EXPRESSION_LONGITUDE = "(-?([1]?[0-7][1-9]|[1-9]?[0-9])?(\\.\\d*)?)|-?180(\\.[0]*)?";
	private long id;
	private String name;
	private String code;
	private String latitude;
	private String longitude;
	private String postalCode;
	private Locality locality;
	private Set<GramPanchayat> gramPanchayats;
	private Set<Village> villages;
	private long revisionNo;
	private String branchId;
	/**
	 * Transient variable
	 */
	private List<String> branchesList;
	
	private String stateName;
	private String countryName;
	/**
	 * Gets the locality.
	 * 
	 * @return the locality
	 */
	/*
	 * @NotNull(message = "empty.locality") public Locality getLocality() {
	 * 
	 * return locality; }
	 */
	/**
	 * Sets the locality.
	 * 
	 * @param locality
	 *            the new locality
	 */
	/*
	 * public void setLocality(Locality locality) {
	 * 
	 * this.locality = locality; }
	 */


	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {

		return id;
	}

	/**
	 * Gets the latitude.
	 * 
	 * @return the latitude
	 */

	/* @NotEmpty(message = "empty.latitude") */
	public String getLatitude() {

		return latitude;
	}

	/**
	 * Gets the longitude.
	 * 
	 * @return the longitude
	 */

	/* @NotEmpty(message = "empty.longitude") */
	public String getLongitude() {

		return longitude;
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
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(long id) {

		this.id = id;
	}

	/**
	 * Sets the latitude.
	 * 
	 * @param lattitude
	 *            the new latitude
	 */
	public void setLatitude(String lattitude) {

		this.latitude = lattitude;
	}

	/**
	 * Sets the longitude.
	 * 
	 * @param longitude
	 *            the new longitude
	 */
	public void setLongitude(String longitude) {

		this.longitude = longitude;
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
	 * Gets the postal code.
	 * 
	 * @return the postal code
	 */

	public String getPostalCode() {

		return postalCode;
	}

	/**
	 * Sets the postal code.
	 * 
	 * @param postalCode
	 *            the new postal code
	 */
	public void setPostalCode(String postalCode) {

		this.postalCode = postalCode;
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
	 * Sets the code.
	 * 
	 * @param code
	 *            the new code
	 */
	public void setCode(String code) {

		this.code = code;
	}

	/**
	 * Sets the villages.
	 * 
	 * @param villages
	 *            the new villages
	 */
	public void setVillages(Set<Village> villages) {

		this.villages = villages;
	}

	/**
	 * Gets the villages.
	 * 
	 * @return the villages
	 */
	public Set<Village> getVillages() {

		return villages;
	}

	/**
	 * Gets the code with name.
	 * 
	 * @return the code with name
	 */
	public String getCodeWithName() {

		return name + " - " + code;
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

	/**
	 * Gets the gram panchayats.
	 * 
	 * @return the gram panchayats
	 */
	public Set<GramPanchayat> getGramPanchayats() {

		return gramPanchayats;
	}

	/**
	 * Sets the gram panchayats.
	 * 
	 * @param gramPanchayats
	 *            the new gram panchayats
	 */
	public void setGramPanchayats(Set<GramPanchayat> gramPanchayats) {

		this.gramPanchayats = gramPanchayats;
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

	public Locality getLocality() {
		return locality;
	}

	public void setLocality(Locality locality) {
		this.locality = locality;
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

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
}
