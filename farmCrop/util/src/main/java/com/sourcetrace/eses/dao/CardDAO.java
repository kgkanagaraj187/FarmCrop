/*
 * CardDAO.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

package com.sourcetrace.eses.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sourcetrace.esesw.entity.profile.Card;
import com.sourcetrace.esesw.entity.profile.Client;
import com.sourcetrace.esesw.entity.profile.ESECard;


/**
 * The Class CardDAO.
 * 
 * @author $Author: aravind $
 * @version $Rev: 272 $ $Date: 2009-10-08 07:42:12 +0530 (Thu, 08 Oct 2009) $
 */
@Repository
@Transactional
public class CardDAO extends ESEDAO implements ICardDAO
{
	
	@Autowired
	public CardDAO(SessionFactory sessionFactory) {

		this.setSessionFactory(sessionFactory);
	}

	public List<Card> listCards() {

		return list("FROM Card c WHERE c.delete='N'");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.ICardDAO#findCardByEnrollmentStation(java.lang.String
	 * )
	 */
	public long findCardByEnrollmentStation(String enrollmentStation) {

		Long id = (Long) find(
				"select max(c.id) FROM Card c WHERE c.delete='N' and SUBSTRING(c.cardId,3,4) = ?",
				enrollmentStation);
		return id != null ? id : 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.ICardDAO#listLockedCards()
	 */
	public List<Card> listLockedCards() {

		return list("FROM Card c WHERE c.enabled='2' and c.delete='N'");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.ICardDAO#findCard(java.lang.String)
	 */
	public Card findCardByCardId(String cardId) {

		Card card = (Card) find(
				"FROM Card c WHERE c.delete='N' and c.cardId = ?", cardId);
		return card;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.ICardDAO#findCardByClientId(java.lang.String)
	 */
	public List<Card> listByClientId(String clientId) {

		List<Card> cards = list(
				"FROM Card c WHERE c.delete='N' and c.holder.profileId = ?",
				clientId);
		return cards;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.ICardDAO#findCard(long)
	 */
	public Card findCardByProfile(String profileId, int cardType) {

		Object[] bindValues = { profileId, cardType };
		Card card = (Card) find(
				"FROM Card c WHERE c.delete='N' and c.holder.profileId = ? and c.cardType= ?",
				bindValues);
		return card;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.ICardDAO#listAgentCards()
	 */
	public List<Card> listAgentCards() {

		return list("FROM Card c WHERE  c.delete='N' and c.holder.id in(select id from Agent)");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.ICardDAO#listClientCards()
	 */
	public List<Card> listClientCards() {

		return list("FROM Card c WHERE c.delete='N' and c.holder.id in(select id from Client)");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.ESEDAO#save(java.lang.Object)
	 */
	public void save(Object object) {

		if (object instanceof Card) {
			Card card = (Card) object;
			card.setCreatedTime(new Date());
			card.setModifiedTime(new Date());
		}

		super.save(object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.ICardDAO#findCard(long)
	 */
	public Card findCard(long id) {

		Card card = (Card) find("FROM Card c WHERE c.id = ?", id);
		return card;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.ESEDAO#update(java.lang.Object)
	 */
	/*
	 * public void update(Object object) {
	 * 
	 * if (object instanceof Card) { Card card = (Card) object;
	 * card.setModifiedTime(new Date()); }
	 * 
	 * super.update(object); }
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.ICardDAO#findESECardByProfile(java.lang.String)
	 */
	public ESECard findESECardByProfile(String profileId) {

		return (ESECard) find("FROM ESECard c WHERE c.profileId = ?", profileId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.ese.dao.profile.ICardDAO#
	 * listOfHomePOSClientByRevisionNumberAndServicePlaceId(long,
	 * java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<ESECard> listOfHomePOSClientByRevisionNumberAndServicePlaceId(
			long revisionNumber, String servicePlaceId) {

		Object[] bind = { Client.CLIENT, revisionNumber, servicePlaceId };
		List<ESECard> clientCardList = list(
				"select eseCard FROM Client client, ESECard eseCard WHERE client.profileId = eseCard.profileId AND client.profileType=? AND client.revisionNumber>? AND client.servicePoint.code = ? ORDER BY client.revisionNumber",
				bind);

		return clientCardList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.ICardDAO#updateCardStatus(java.lang.String, int)
	 */
	public void updateCardStatus(String profileId, int status,
			int cardRewritable) {

		Session session = getSessionFactory().openSession();
		Query query = session
				.createQuery("update ESECard set status = :status,cardRewritable = :cardRewritable"
						+ " where profileId = :profileId");
		query.setParameter("status", status);
		query.setParameter("profileId", profileId);
		query.setParameter("cardRewritable", cardRewritable);
		int result = query.executeUpdate();
		session.flush();
		session.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.ICardDAO#findCardIdByProfileId(java.lang.String)
	 */
	public String findCardIdByProfileId(String agentId) {
		return (String) find(
				"SELECT c.cardId FROM ESECard c WHERE c.profileId = ?", agentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.ICardDAO#removeCardByProfileId(java.lang.String)
	 */
	public void removeCardByProfileId(String profileId) {
		Session session = getSessionFactory().openSession();
		Query query = session
				.createQuery("delete ESECard where profileId = :profileId");
		query.setParameter("profileId", profileId);
		int result = query.executeUpdate();
		session.flush();
		session.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.ICardDAO#findESECardByCardId(java.lang.String)
	 */
	public ESECard findESECardByCardId(String cardId) {
		return (ESECard) find("FROM ESECard c WHERE c.cardId = ?", cardId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.ICardDAO#findESECardById(long)
	 */
	public ESECard findESECardById(long id) {
		return (ESECard) find("FROM ESECard c WHERE c.id = ?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.ICardDAO#findCardByProfileIdAndCardId(java.lang.String
	 * , java.lang.String)
	 */
	public ESECard findCardByProfileIdAndCardId(String profileId, String cardId) {

		Object[] values = { profileId, cardId };
		return (ESECard) find(
				"FROM ESECard esec WHERE esec.profileId = ? AND esec.cardId = ?",
				values);
	}
	
}
