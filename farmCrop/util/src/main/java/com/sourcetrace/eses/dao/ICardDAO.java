/*
 * ICardDAO.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

package com.sourcetrace.eses.dao;

import java.util.List;

import com.sourcetrace.esesw.entity.profile.Card;
import com.sourcetrace.esesw.entity.profile.ESECard;


/**
 * The Interface ICardDAO.
 * 
 * @author $Author: aravind $
 * @version $Rev: 272 $ $Date: 2009-10-08 07:42:12 +0530 (Thu, 08 Oct 2009) $
 */

public interface ICardDAO extends IESEDAO {

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
	 * Find card by card id.
	 * 
	 * @param cardId
	 *            the card id
	 * 
	 * @return the card
	 */
	public Card findCardByCardId(String cardId);

	/**
	 * Find card.
	 * 
	 * @param id
	 *            the id
	 * 
	 * @return the card
	 */
	public Card findCard(long id);

	/**
	 * List by client id.
	 * 
	 * @param clientId
	 *            the client id
	 * 
	 * @return the list< card>
	 */
	public List<Card> listByClientId(String clientId);

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
	 * Find card by profile.
	 * 
	 * @param profileId
	 *            the profile id
	 * @param cardType
	 *            the card type
	 * 
	 * @return the card
	 */
	public Card findCardByProfile(String profileId, int cardType);

	/**
	 * Find card by enrollment station.
	 * 
	 * @param enrollmentStation
	 *            the enrollment station
	 * 
	 * @return the long
	 */
	public long findCardByEnrollmentStation(String enrollmentStation);

	/**
	 * Find ese card by profile.
	 * 
	 * @param profileId
	 *            the profile id
	 * 
	 * @return the eSE card
	 */
	public ESECard findESECardByProfile(String profileId);

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
	 * Find ese card by card id.
	 * 
	 * @param cardId
	 *            the card id
	 * 
	 * @return the eSE card
	 */
	public ESECard findESECardByCardId(String cardId);

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
