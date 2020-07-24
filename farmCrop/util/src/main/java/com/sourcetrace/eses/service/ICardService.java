/*
 * ICardService.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

package com.sourcetrace.eses.service;

import java.util.List;

import com.sourcetrace.esesw.entity.profile.Card;
import com.sourcetrace.esesw.entity.profile.ESECard;

/**
 * The Interface ICardService.
 * 
 * @author $Author: aravind $
 * @version $Rev: 23 $, $Date: 2009-07-13 16:53:19 +0530 (Mon, 13 Jul 2009) $
 */
public interface ICardService {

	/**
	 * List cards.
	 * 
	 * @return the list< card>
	 */
	public List<Card> listCards();

	/**
	 * List locked cards.
	 * 
	 * @return the list< card>
	 */
	public List<Card> listLockedCards();

	/**
	 * List cards by profile id.
	 * 
	 * @param profileId
	 *            the profile id
	 * 
	 * @return the list< card>
	 */
	public List<Card> listCardsByProfileId(String profileId);

	/**
	 * Adds the card.
	 * 
	 * @param card
	 *            the card
	 */
	public void addCard(Card card);

	/**
	 * Edits the card.
	 * 
	 * @param card
	 *            the card
	 */
	public void editCard(Card card);

	/**
	 * Removes the card.
	 * 
	 * @param card
	 *            the card
	 */
	public void removeCard(Card card);

	/**
	 * Find card.
	 * 
	 * @param cardId
	 *            the card id
	 * 
	 * @return the card
	 */
	public Card findCard(String cardId);

	/**
	 * Find card by id.
	 * 
	 * @param id
	 *            the id
	 * 
	 * @return the card
	 */
	public Card findCardById(long id);

	/**
	 * List agent cards.
	 * 
	 * @return the list< card>
	 */
	public List<Card> listAgentCards();

	/**
	 * List client cards.
	 * 
	 * @return the list< card>
	 */
	public List<Card> listClientCards();

	/**
	 * Re issue card.
	 * 
	 * @param card
	 *            the card
	 * @param status
	 *            the status
	 */
	public void reIssueCard(Card card, int status);

	/**
	 * Find card by profile.
	 * 
	 * @param agentId
	 *            the agent id
	 * @param cardType
	 *            the card type
	 * 
	 * @return the card
	 */
	public Card findCardByProfile(String agentId, int cardType);

	/**
	 * Creates the card.
	 * 
	 * @param card
	 *            the card
	 */
	public void createCard(ESECard card);

	/**
	 * Find card by profile id.
	 * 
	 * @param profileId
	 *            the profile id
	 * 
	 * @return the eSE card
	 */
	public ESECard findCardByProfileId(String profileId);

	/**
	 * List of home pos client by revision number and service place id.
	 * 
	 * @param revisionNumber
	 *            the revision number
	 * @param servicePlaceId
	 *            the service place id
	 * 
	 * @return the list< ese card>
	 */
	public List<ESECard> listOfHomePOSClientByRevisionNumberAndServicePlaceId(
			long revisionNumber, String servicePlaceId);

	/**
	 * Find ese card by id.
	 * 
	 * @param id
	 *            the id
	 * 
	 * @return the eSE card
	 */
	public ESECard findESECardById(long id);

	/**
	 * Find ese card by card id.
	 * 
	 * @param cardId
	 *            the card id
	 * 
	 * @return the eSE card
	 */
	public ESECard findESECardByCardId(String cardId);

	/**
	 * Update.
	 * 
	 * @param tempCard
	 *            the temp card
	 */
	public void update(ESECard tempCard);

	/**
	 * Update card status.
	 * 
	 * @param profileId
	 *            the profile id
	 * @param status
	 *            the status
	 * @param cardRewritable
	 *            the card rewritable
	 */
	public void updateCardStatus(String profileId, int status,
			int cardRewritable);

	/**
	 * Find card id by profile id.
	 * 
	 * @param agentId
	 *            the agent id
	 * 
	 * @return the string
	 */
	public String findCardIdByProfileId(String agentId);

	/**
	 * Removes the card by profile id.
	 * 
	 * @param profileId
	 *            the profile id
	 */
	public void removeCardByProfileId(String profileId);

	/**
	 * Find card by profile id and card id.
	 * 
	 * @param profileId
	 *            the profile id
	 * @param cardId
	 *            the card id
	 * 
	 * @return the eSE card
	 */
	public ESECard findCardByProfileIdAndCardId(String profileId, String cardId);
	
}
