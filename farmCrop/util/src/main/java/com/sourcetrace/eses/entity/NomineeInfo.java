package com.sourcetrace.eses.entity;

import java.util.Date;

public class NomineeInfo {

	private long id;
    private String name;
    private String relationShip;
    private Date dateOfBirth;
    private String address;

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
	 * @param id the new id
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
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the relation ship.
	 * 
	 * @return the relation ship
	 */
	public String getRelationShip() {
		return relationShip;
	}
	
	/**
	 * Sets the relation ship.
	 * 
	 * @param relationShip the new relation ship
	 */
	public void setRelationShip(String relationShip) {
		this.relationShip = relationShip;
	}
	
	/**
	 * Gets the date of birth.
	 * 
	 * @return the date of birth
	 */
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	
	/**
	 * Sets the date of birth.
	 * 
	 * @param dateOfBirth the new date of birth
	 */
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	/**
	 * Gets the address.
	 * 
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Sets the address.
	 * 
	 * @param address the new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

}
