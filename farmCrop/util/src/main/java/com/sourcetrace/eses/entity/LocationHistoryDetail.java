package com.sourcetrace.eses.entity;

import java.util.Date;
import java.util.List;

import com.sourcetrace.esesw.entity.profile.Farmer;

public class LocationHistoryDetail {
	
	private long id;
	private Date txnTime;
	private String serialNumber;
	private String longitude;
	private String latitude;
	private String agentId;
	private Date createdTime;
	private String branchId;
	private String distance;
	private String address;
	private long accuracy;
	private String netStatus;
	private String gpsStatus;
	private LocationHistory locHistory;
	// Transient Variable
	private String agentName;
	private List<String> branchesList;

	// Read Only Properties
	private Profile profile;
	private Device device;

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
	 * Sets the txn time.
	 * 
	 * @param txnTime
	 *            the new txn time
	 */
	public void setTxnTime(Date txnTime) {

		this.txnTime = txnTime;
	}

	/**
	 * Gets the txn time.
	 * 
	 * @return the txn time
	 */
	public Date getTxnTime() {

		return txnTime;
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
	 * Gets the longitude.
	 * 
	 * @return the longitude
	 */
	public String getLongitude() {

		return longitude;
	}

	/**
	 * Sets the serial number.
	 * 
	 * @param serialNumber
	 *            the new serial number
	 */
	public void setSerialNumber(String serialNumber) {

		this.serialNumber = serialNumber;
	}

	/**
	 * Gets the serial number.
	 * 
	 * @return the serial number
	 */
	public String getSerialNumber() {

		return serialNumber;
	}

	/**
	 * Sets the latitude.
	 * 
	 * @param latitude
	 *            the new latitude
	 */
	public void setLatitude(String latitude) {

		this.latitude = latitude;
	}

	/**
	 * Gets the latitude.
	 * 
	 * @return the latitude
	 */
	public String getLatitude() {

		return latitude;
	}

	/**
	 * Gets the agent id.
	 * 
	 * @return the agent id
	 */
	public String getAgentId() {

		return agentId;
	}

	/**
	 * Sets the agent id.
	 * 
	 * @param agentId
	 *            the new agent id
	 */
	public void setAgentId(String agentId) {

		this.agentId = agentId;
	}

	/**
	 * Gets the agent name.
	 * 
	 * @return the agent name
	 */
	public String getAgentName() {

		return agentName;
	}

	/**
	 * Sets the agent name.
	 * 
	 * @param agentName
	 *            the new agent name
	 */
	public void setAgentName(String agentName) {

		this.agentName = agentName;
	}

	/**
	 * Sets the created time.
	 * 
	 * @param createdTime
	 *            the new created time
	 */
	public void setCreatedTime(Date createdTime) {

		this.createdTime = createdTime;
	}

	/**
	 * Gets the created time.
	 * 
	 * @return the created time
	 */
	public Date getCreatedTime() {

		return createdTime;
	}

	/**
	 * Sets the profile.
	 * 
	 * @param profile
	 *            the new profile
	 */
	public void setProfile(Profile profile) {

		this.profile = profile;
	}

	/**
	 * Gets the profile.
	 * 
	 * @return the profile
	 */
	public Profile getProfile() {

		return profile;
	}

	/**
	 * Sets the device.
	 * 
	 * @param device
	 *            the new device
	 */
	public void setDevice(Device device) {

		this.device = device;
	}

	/**
	 * Gets the device.
	 * 
	 * @return the device
	 */
	public Device getDevice() {

		return device;
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

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(long accuracy) {
		this.accuracy = accuracy;
	}

	public String getNetStatus() {
		return netStatus;
	}

	public void setNetStatus(String netStatus) {
		this.netStatus = netStatus;
	}

	public String getGpsStatus() {
		return gpsStatus;
	}

	public void setGpsStatus(String gpsStatus) {
		this.gpsStatus = gpsStatus;
	}

	public LocationHistory getLocHistory() {
		return locHistory;
	}

	public void setLocHistory(LocationHistory locHistory) {
		this.locHistory = locHistory;
	}
	
	

}
