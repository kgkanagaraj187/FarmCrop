package com.sourcetrace.esesw.entity.profile;

import java.util.Date;

/**
 * The Class ESECard.
 */
public class ESECard implements Comparable<ESECard> {

	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final int BLOCKED = 2;

	public static final int AGENT_CARD = 0;
	public static final int CLIENT_CARD = 1;
	public static final int SHOP_DEALER_CARD=1;
	public static final int FARMER_CARD=2;

	public static final String OPERATOR_CARD = "OC";
	public static final String CUSTOMER_CARD = "CC";

	public static final int IS_REWRITABLE_NO = 0;
	public static final int IS_REWRITABLE_YES = 1;

	private long id;
	private String cardId;
	private String cardType;
	private int type;
	private Date issueDate;
	private int status;
	private Date createTime;
	private Date updateTime;
	private String profileId;
	private int cardRewritable;

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
	 * @param cardId the new card id
	 */
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	/**
	 * Gets the card type.
	 * 
	 * @return the card type
	 */
	public String getCardType() {
		return cardType;
	}

	/**
	 * Sets the card type.
	 * 
	 * @param cardType the new card type
	 */
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type the new type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public int getType() {
		return type;
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
	 * @param issueDate the new issue date
	 */
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	/**
	 * Gets the status.
	 * 
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 * 
	 * @param status the new status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Gets the creates the time.
	 * 
	 * @return the creates the time
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * Sets the creates the time.
	 * 
	 * @param createTime the new creates the time
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	 * @param updateTime the new update time
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * Gets the profile id.
	 * 
	 * @return the profile id
	 */
	public String getProfileId() {
		return profileId;
	}

	/**
	 * Sets the profile id.
	 * 
	 * @param profileId the new profile id
	 */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(ESECard card) {

		int value = -1;
		if (card instanceof ESECard) {
			ESECard temp = (ESECard) card;
			value = this.getCardId().compareTo(temp.getCardId());
		}
		return value;

	}

	/**
	 * Sets the card rewritable.
	 * 
	 * @param cardRewritable the new card rewritable
	 */
	public void setCardRewritable(int cardRewritable) {
		this.cardRewritable = cardRewritable;
	}

	/**
	 * Gets the card rewritable.
	 * 
	 * @return the card rewritable
	 */
	public int getCardRewritable() {
		return cardRewritable;
	}

}
