package com.sourcetrace.esesw.entity.txn;

/**
 * The Class ESETxnHeader.
 */
public class ESETxnHeader {

	private long id;
	private String serialNo;
	private String deviceId;
	private String deviceName;
	private String deviceType;
	private String agentId;
	private String agentName;
	private String servPointId;
	private String servPointName;
	private String msgNo;
	private String batchNo;
	private String mode;
	private ESETxnType eseTxnType;
	private String branchId;
	private String tenantId;

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
	 * Gets the serial no.
	 * 
	 * @return the serial no
	 */
	public String getSerialNo() {
		return serialNo;
	}

	/**
	 * Sets the serial no.
	 * 
	 * @param serialNo
	 *            the new serial no
	 */
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	/**
	 * Gets the device id.
	 * 
	 * @return the device id
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * Sets the device id.
	 * 
	 * @param deviceId
	 *            the new device id
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * Gets the device name.
	 * 
	 * @return the device name
	 */
	public String getDeviceName() {
		return deviceName;
	}

	/**
	 * Sets the device name.
	 * 
	 * @param deviceName
	 *            the new device name
	 */
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	/**
	 * Gets the device type.
	 * 
	 * @return the device type
	 */
	public String getDeviceType() {
		return deviceType;
	}

	/**
	 * Sets the device type.
	 * 
	 * @param deviceType
	 *            the new device type
	 */
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
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
	 * Gets the serv point id.
	 * 
	 * @return the serv point id
	 */
	public String getServPointId() {
		return servPointId;
	}

	/**
	 * Sets the serv point id.
	 * 
	 * @param servPointId
	 *            the new serv point id
	 */
	public void setServPointId(String servPointId) {
		this.servPointId = servPointId;
	}

	/**
	 * Gets the serv point name.
	 * 
	 * @return the serv point name
	 */
	public String getServPointName() {
		return servPointName;
	}

	/**
	 * Sets the serv point name.
	 * 
	 * @param servPointName
	 *            the new serv point name
	 */
	public void setServPointName(String servPointName) {
		this.servPointName = servPointName;
	}

	/**
	 * Gets the msg no.
	 * 
	 * @return the msg no
	 */
	public String getMsgNo() {
		return msgNo;
	}

	/**
	 * Sets the msg no.
	 * 
	 * @param msgNo
	 *            the new msg no
	 */
	public void setMsgNo(String msgNo) {
		this.msgNo = msgNo;
	}

	/**
	 * Gets the batch no.
	 * 
	 * @return the batch no
	 */
	public String getBatchNo() {
		return batchNo;
	}

	/**
	 * Sets the batch no.
	 * 
	 * @param batchNo
	 *            the new batch no
	 */
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	/**
	 * Gets the mode.
	 * 
	 * @return the mode
	 */
	public String getMode() {
		return mode;
	}

	/**
	 * Sets the mode.
	 * 
	 * @param mode
	 *            the new mode
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}

	/**
	 * Gets the ese txn type.
	 * 
	 * @return the ese txn type
	 */
	public ESETxnType getEseTxnType() {
		return eseTxnType;
	}

	/**
	 * Sets the ese txn type.
	 * 
	 * @param eseTxnType
	 *            the new ese txn type
	 */
	public void setEseTxnType(ESETxnType eseTxnType) {
		this.eseTxnType = eseTxnType;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

}
