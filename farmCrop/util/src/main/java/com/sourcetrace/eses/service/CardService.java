/*
 * CardService.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sourcetrace.eses.dao.IAgentDAO;
import com.sourcetrace.eses.dao.ICardDAO;
import com.sourcetrace.eses.dao.IClientDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.esesw.entity.profile.Card;
import com.sourcetrace.esesw.entity.profile.Client;
import com.sourcetrace.esesw.entity.profile.ESECard;

/**
 * The Class CardService.
 * 
 * @author $Author: aravind $
 * @version $Rev: 23 $, $Date: 2009-07-13 16:53:19 +0530 (Mon, 13 Jul 2009) $
 */

@Service
@Transactional
public class CardService implements ICardService {
	@Autowired
	private ICardDAO cardDAO;
	@Autowired
	private IAgentDAO agentDAO;
	@Autowired
	private IClientDAO clientDAO;
	@Autowired
	private IUniqueIDGenerator idGenerator;

	/**
	 * Sets the card dao.
	 * 
	 * @param cardDAO
	 *            the new card dao
	 */
	public void setCardDAO(ICardDAO cardDAO) {

		this.cardDAO = cardDAO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.ICardService#addCard(com.ese.entity.profile.Card)
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void addCard(Card card) {

		List<Card> cards = cardDAO.listByClientId(card.getHolder()
				.getProfileId());
		if (cards != null) {
			for (Card aCard : cards) {
				if (aCard.getEnabled() == card.ENABLED) {
					aCard.setEnabled(card.DISABLED);
					cardDAO.update(aCard);
				}
			}
		}

		cardDAO.save(card);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.ICardService#editCard(com.ese.entity.profile.
	 * Card)
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void editCard(Card card) {

		cardDAO.update(card);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.ICardService#reIssueCard(com.ese.entity.profile
	 * .Card, int)
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void reIssueCard(Card card, int status) {

		Card oldCard = findCardById(card.getId());
		switch (status) {
		case 5:

			if (isAgent(oldCard.getHolder().getId())) {
				oldCard.setDelete(true);
				oldCard.setModifiedTime(new Date());
				editCard(oldCard);
				Agent aAgent = new Agent();
				aAgent = agentDAO.findAgent(oldCard.getHolder().getId());
				agentDAO.update(aAgent);
				Card newCard = new Card();
				newCard.setHolder(oldCard.getHolder());
				if (oldCard.getCardType() == card.NON_DATA_CARD) {

					newCard.setCardId(idGenerator.createAgentCardId());
				} else if (oldCard.getCardType() == card.DATA_CARD) {
					newCard.setCardId(idGenerator.createAgentDataCardId());
				}
				newCard.setModifiedTime(new Date());
				newCard.setCardType(oldCard.getCardType());
				addCard(newCard);
			} else {

				oldCard.setModifiedTime(new Date());
				editCard(oldCard);
				Client aClient = new Client();
				aClient = clientDAO.findClient(oldCard.getHolder().getId());
				clientDAO.update(aClient);
			}
			break;
		case 6:
			oldCard.setEnabled(Card.DISABLED);
			oldCard.setModifiedTime(new Date());
			editCard(oldCard);
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.ICardService#findCard(java.lang.String)
	 */
	public Card findCard(String cardId) {

		return cardDAO.findCardByCardId(cardId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.ICardService#findCardById(long)
	 */
	public Card findCardById(long id) {

		return cardDAO.findCard(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.ICardService#listCards()
	 */
	public List<Card> listCards() {

		return cardDAO.listCards();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.ICardService#listCards()
	 */
	public List<Card> listCardsByProfileId(String profileId) {

		return cardDAO.listByClientId(profileId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.ICardService#removeCard(com.ese.entity.profile
	 * .Card)
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void removeCard(Card Card) {

		cardDAO.delete(Card);
	}

	/**
	 * Checks if is agent.
	 * 
	 * @param id
	 *            the id
	 * 
	 * @return true, if is agent
	 */
	public boolean isAgent(long id) {

		Agent aAgent = new Agent();
		aAgent = agentDAO.findAgent(id);
		return (!ObjectUtil.isEmpty(aAgent));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.ICardService#listAgentCards()
	 */
	public List<Card> listAgentCards() {

		return cardDAO.listAgentCards();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.ICardService#listClientCards()
	 */
	public List<Card> listClientCards() {

		return cardDAO.listClientCards();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.ICardService#listLockedCards()
	 */
	public List<Card> listLockedCards() {

		return cardDAO.listLockedCards();
	}

	/**
	 * Sets the id generator.
	 * 
	 * @param idGenerator
	 *            the new id generator
	 */
	public void setIdGenerator(IUniqueIDGenerator idGenerator) {

		this.idGenerator = idGenerator;
	}

	/**
	 * Sets the agent dao.
	 * 
	 * @param agentDAO
	 *            the new agent dao
	 */
	public void setAgentDAO(IAgentDAO agentDAO) {

		this.agentDAO = agentDAO;
	}

	/**
	 * Sets the client dao.
	 * 
	 * @param clientDAO
	 *            the new client dao
	 */
	public void setClientDAO(IClientDAO clientDAO) {

		this.clientDAO = clientDAO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.ICardService#findCardByProfile(java.lang.String,
	 * int)
	 */
	public Card findCardByProfile(String agentId, int cardType) {

		return cardDAO.findCardByProfile(agentId, cardType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.ICardService#createCardForNewAgent(com.ese.entity
	 * .profile.ESECard)
	 */
	/**
	 * Creates the card.
	 * 
	 * @param card
	 *            the card
	 */
	public void createCard(ESECard card) {

		cardDAO.save(card);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.ICardService#findCardByProfileId(java.lang.String
	 * )
	 */
	public ESECard findCardByProfileId(String profileId) {

		return cardDAO.findESECardByProfile(profileId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.ese.service.profile.ICardService#
	 * listOfHomePOSClientByRevisionNumberAndServicePlaceId (long,
	 * java.lang.String)
	 */
	public List<ESECard> listOfHomePOSClientByRevisionNumberAndServicePlaceId(
			long revisionNumber, String servicePlaceId) {

		return cardDAO.listOfHomePOSClientByRevisionNumberAndServicePlaceId(
				revisionNumber, servicePlaceId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.ICardService#update(com.ese.entity.profile.ESECard
	 * )
	 */
	public void update(ESECard tempCard) {

		cardDAO.update(tempCard);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.ICardService#updateCardStatus(java.lang.String,
	 * int)
	 */
	public void updateCardStatus(String profileId, int status,
			int cardRewritable) {
		cardDAO.updateCardStatus(profileId, status, cardRewritable);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.ICardService#findCardIdByProfileId(java.lang.
	 * String)
	 */
	public String findCardIdByProfileId(String agentId) {
		return cardDAO.findCardIdByProfileId(agentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.ICardService#removeCardByProfileId(java.lang.
	 * String)
	 */
	public void removeCardByProfileId(String profileId) {
		cardDAO.removeCardByProfileId(profileId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.ICardService#findESECardByCardId(java.lang.String
	 * )
	 */
	public ESECard findESECardByCardId(String cardId) {
		return cardDAO.findESECardByCardId(cardId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.ICardService#findESECardById(long)
	 */
	public ESECard findESECardById(long id) {
		return cardDAO.findESECardById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.ICardService#findCardByProfileIdAndCardId(java
	 * .lang.String, java.lang.String)
	 */
	public ESECard findCardByProfileIdAndCardId(String profileId, String cardId) {

		return cardDAO.findCardByProfileIdAndCardId(profileId, cardId);
	}
}
