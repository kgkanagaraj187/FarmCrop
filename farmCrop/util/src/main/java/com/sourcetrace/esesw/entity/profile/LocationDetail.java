/**
 * Location.java
 * Copyright (c) 2008, Source Trace Systems
 * ALL RIGHTS RESERVED
 */
package com.sourcetrace.esesw.entity.profile;

import java.util.Set;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;





// TODO: Auto-generated Javadoc
/**
 * The Class Location.
 * @author $Author: antronivan $
 * @version $Rev: 422 $, $Date: 2009-11-23 08:19:13 +0530 (Mon, 23 Nov 2009) $
 */
public class LocationDetail {


	private long id;
	private String name;
	private String latitude;
	private String longitude;
	private Region region;
	

	private Set<Branch> branches;

	/**
	 * Gets the branches.
	 * @return the branches
	 */
	public Set<Branch> getBranches() {
		return branches;
	}

	/**
	 * Sets the branches.
	 * @param branches the new branches
	 */
	public void setBranches(Set<Branch> branches) {
		this.branches = branches;
	}

	/**
	 * Gets the id.
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * @param id the new id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 * @return the name
	 */
	@Pattern(regexp="[^\\p{Punct}]+$", message="pattern.name")
	@Length(max=30, message="length.name")
	@NotEmpty(message="empty.name")
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the latitude.
	 * @return the latitude
	 */
	@Pattern(regexp = "[-]{0,1}+[0-9]+[.]{0,1}+[0-9]+",message="pattern.latitude")
	@Length(max=20, message="latitude.length")
	@NotEmpty(message="empty.latitude")
	public String getLatitude() {
		return latitude;
	}

	/**
	 * Sets the latitude.
	 * @param latitude the new latitude
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * Gets the longitude.
	 * @return the longitude
	 */
	@Pattern(regexp = "[-]{0,1}+[0-9]+[.]{0,1}+[0-9]+",message="pattern.longitude")
	@Length(max=20, message="longitude.length")
	@NotEmpty(message="empty.longitude")
	public String getLongitude() {
		return longitude;
	}

	/**
	 * Sets the longitude.
	 * @param longitude the new longitude
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the region
	 */
	public Region getRegion() {
		return region;
	}

	/**
	 * @param region the region to set
	 */
	public void setRegion(Region region) {
		this.region = region;
	}
}
