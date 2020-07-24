package com.sourcetrace.esesw.entity.txn;

import java.util.Date;

/**
 * The Class ESETxn.
 */
public class ESETxn {

	public static final int ON_LINE = 1;
	public static final int VOID = 2;
	
	public static final int ONLINE_MODE = 1;
	public static final int OFFLINE_MODE = 2;
	public static final int BOTH = 3;

	public static final int BOD = 1;
	public static final int EOD = 0;
	public static final int WEB_BOD=2;
	
	private long id;
	private ESETxnHeader header;
	private int operType;
	private int resentCount;
	private ESETxnStatus status;
    private String code;
    private String message;
	private Date txnTime;
	private Date updateTime;
	private String txnId;

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
	 * Gets the header.
	 * 
	 * @return the header
	 */
	public ESETxnHeader getHeader() {
		return header;
	}

	/**
	 * Sets the header.
	 * 
	 * @param header
	 *            the new header
	 */
	public void setHeader(ESETxnHeader header) {
		this.header = header;
	}

	/**
	 * Gets the oper type.
	 * 
	 * @return the oper type
	 */
	public int getOperType() {
		return operType;
	}

	/**
	 * Sets the oper type.
	 * 
	 * @param operType
	 *            the new oper type
	 */
	public void setOperType(int operType) {
		this.operType = operType;
	}

	/**
	 * Gets the resent count.
	 * 
	 * @return the resent count
	 */
	public int getResentCount() {
		return resentCount;
	}

	/**
	 * Sets the resent count.
	 * 
	 * @param resentCount
	 *            the new resent count
	 */
	public void setResentCount(int resentCount) {
		this.resentCount = resentCount;
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
	 * Sets the txn time.
	 * 
	 * @param txnTime
	 *            the new txn time
	 */
	public void setTxnTime(Date txnTime) {
		this.txnTime = txnTime;
	}

	/**
	 * Gets the update time.
	 * 
	 * @return the update time
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * Sets the update time.
	 * 
	 * @param updateTime
	 *            the new update time
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * Gets the txn code.
	 * 
	 * @return the txn code
	 */
	public String getTxnId() {
		return txnId;
	}

	/**
	 * Sets the txn Id.
	 * 
	 * @param txnId
	 *            the new txn Id
	 */
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	/**
	 * Gets the status.
	 * 
	 * @return the status
	 */
	public ESETxnStatus getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 * 
	 * @param status
	 *            the new status
	 */
	public void setStatus(ESETxnStatus status) {
		this.status = status;
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
	 * Gets the message.
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 * 
	 * @param message
	 *            the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
