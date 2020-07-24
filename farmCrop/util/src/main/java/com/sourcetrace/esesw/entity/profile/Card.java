/**
 * Card.java
 * Copyright (c) 2008, Source Trace Systems
 * ALL RIGHTS RESERVED
 */
package com.sourcetrace.esesw.entity.profile;

import java.util.Date;

import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.util.entity.AuditLog;

// TODO: Auto-generated Javadoc
/**
 * The Class Card.
 * 
 * @author $Author: aravind $
 * @version $Rev: 59 $, $Date: 2009-08-04 17:10:51 +0530 (Tue, 04 Aug 2009) $
 */
public class Card {
	public static final int DISABLED = 0;
	public static final int ENABLED = 1;
	public static final int LOCKED = 2;

	public static final int APPROVED = 5;
	public static final int REJECTED = 6;

	public static final int NON_DATA_CARD = 1;
	public static final int DATA_CARD = 2;

	private long id;
	private String cardId;
	private int enabled;

	private Profile holder;
	private boolean pinReset;

	private String issuedEnrollmentStation;
	private String issuedAgent;
	private long updateSecond;

	private Date issueDate;
	private Date createdTime;
	private Date modifiedTime;
	private int cardType;

	private boolean delete;

	// transient properties for audit log
	private String siteUser;
	private String action;

	/**
	 * Gets the card id.
	 * 
	 * @return the card id
	 */
	public String getCardId() {
		return cardId;
	}

	/**
	 * Sets the card id.
	 * 
	 * @param cardId
	 *            the new card id
	 */
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	/**
	 * Gets the enabled.
	 * 
	 * @return the enabled
	 */
	public int getEnabled() {
		return enabled;
	}

	/**
	 * Sets the enabled.
	 * 
	 * @param enabled
	 *            the new enabled
	 */
	public void setEnabled(int enabled) {

		if (enabled != this.enabled) {

			switch (enabled) {
			case 0:
				action = AuditLog.DISABLE;
				break;

			case 1:
				action = AuditLog.ENABLE;
				break;
			case 2:
				action = AuditLog.LOCKED;
				break;

			}

		}

		this.enabled = enabled;
	}

	/**
	 * Gets the holder.
	 * 
	 * @return the holder
	 */
	public Profile getHolder() {
		return holder;
	}

	/**
	 * Sets the holder.
	 * 
	 * @param holder
	 *            the new holder
	 */
	public void setHolder(Profile holder) {
		this.holder = holder;
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
	 * Sets the created time.
	 * 
	 * @param createdTime
	 *            the new created time
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * Gets the modified time.
	 * 
	 * @return the modified time
	 */
	public Date getModifiedTime() {
		return modifiedTime;
	}

	/**
	 * Sets the modified time.
	 * 
	 * @param modifiedTime
	 *            the new modified time
	 */
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
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
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the issue date.
	 * 
	 * @return the issue date
	 */
	public Date getIssueDate() {
		return issueDate;
	}

	/**
	 * Sets the issue date.
	 * 
	 * @param issueDate
	 *            the new issue date
	 */
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	/**
	 * Sets the site user.
	 * 
	 * @param userName
	 *            the new site user
	 */
	public void setSiteUser(String userName) {
		this.siteUser = userName;
	}

	/**
	 * Gets the site user.
	 * 
	 * @return the site user
	 */
	public String getSiteUser() {
		return siteUser;
	}

	/**
	 * Gets the action.
	 * 
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Sets the action.
	 * 
	 * @param action
	 *            the new action
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * Checks if is pin reset.
	 * 
	 * @return true, if is pin reset
	 */
	public boolean isPinReset() {
		return pinReset;
	}

	/**
	 * Sets the pin reset.
	 * 
	 * @param pinReset
	 *            the new pin reset
	 */
	public void setPinReset(boolean pinReset) {
		this.pinReset = pinReset;
	}

	/**
	 * Gets the issued enrollment station.
	 * 
	 * @return the issued enrollment station
	 */
	public String getIssuedEnrollmentStation() {
		return issuedEnrollmentStation;
	}

	/**
	 * Sets the issued enrollment station.
	 * 
	 * @param issuedEnrollmentStation
	 *            the new issued enrollment station
	 */
	public void setIssuedEnrollmentStation(String issuedEnrollmentStation) {
		this.issuedEnrollmentStation = issuedEnrollmentStation;
	}

	/**
	 * Gets the issued agent.
	 * 
	 * @return the issued agent
	 */
	public String getIssuedAgent() {
		return issuedAgent;
	}

	/**
	 * Sets the issued agent.
	 * 
	 * @param issuedAgent
	 *            the new issued agent
	 */
	public void setIssuedAgent(String issuedAgent) {
		this.issuedAgent = issuedAgent;
	}

	/**
	 * Gets the update second.
	 * 
	 * @return the update second
	 */
	public long getUpdateSecond() {
		return updateSecond;
	}

	/**
	 * Sets the update second.
	 * 
	 * @param updateSecond
	 *            the new update second
	 */
	public void setUpdateSecond(long updateSecond) {
		this.updateSecond = updateSecond;
	}

	/**
	 * Checks if is delete.
	 * 
	 * @return true, if is delete
	 */
	public boolean isDelete() {
		return delete;
	}

	/**
	 * Sets the delete.
	 * 
	 * @param delete
	 *            the new delete
	 */
	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	/**
	 * Gets the card type.
	 * 
	 * @return the card type
	 */
	public int getCardType() {
		return cardType;
	}

	/**
	 * Sets the card type.
	 * 
	 * @param cardType
	 *            the new card type
	 */
	public void setCardType(int cardType) {
		this.cardType = cardType;
	}

}
