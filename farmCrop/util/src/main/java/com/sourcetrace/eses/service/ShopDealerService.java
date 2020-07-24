/*
 * ShopDealerService.java
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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sourcetrace.eses.dao.IAgentDAO;
import com.sourcetrace.eses.dao.ICardDAO;
import com.sourcetrace.eses.dao.ILocationDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.order.entity.profile.ShopDealerAccount;
import com.sourcetrace.eses.order.entity.profile.ShopDealerAccountDetail;
import com.sourcetrace.eses.order.entity.txn.Inventory;
import com.sourcetrace.eses.order.entity.txn.InventoryDetail;
import com.sourcetrace.eses.txn.exception.OfflineTransactionException;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.entity.Image;
import com.sourcetrace.eses.util.entity.ImageInfo;
import com.sourcetrace.esesw.entity.profile.ESECard;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.OfflineShopDealerEnrollment;
import com.sourcetrace.esesw.entity.profile.ShopDealer;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;

/**
 * The Class ShopDealerService.
 */
@Service
@Transactional
public class ShopDealerService implements IShopDealerService {

    private static final Logger LOGGER = Logger.getLogger(ShopDealerService.class.getName());
    private static final String SHOP_DEALER_DOB = "yyyyMMdd";
    @Autowired
    private IShopDealerDAO shopDealerDAO;
    @Autowired
    private IUniqueIDGenerator idGenerator;
    @Autowired
    private ILocationDAO locationDAO;
    @Autowired
    private ICardDAO cardDAO;
    @Autowired
    private IAgentDAO agentDAO;

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.profile.IShopDealerService#addShopDealer
     * (com.ese.entity.profile.ShopDealer)
     */
    public void addShopDealer(ShopDealer shopDealer) {

        shopDealer.setRevisionNo(DateUtil.getRevisionNumber());
        shopDealerDAO.save(shopDealer);
    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.esesw.service.profile.IShopDealerService#
     * findShopDealerByShopDealerId(java.lang.String)
     */
    public ShopDealer findShopDealerByShopDealerId(String shopDealerId) {

        return shopDealerDAO.findShopDealerByShopDealerId(shopDealerId);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.profile.IShopDealerService#findShopDealerById
     * (java.lang.Long)
     */
    public ShopDealer findShopDealerById(Long id) {

        return shopDealerDAO.findShopDealerById(id);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.profile.IShopDealerService#editShopDealer
     * (com.ese.entity.profile.ShopDealer)
     */
    public void editShopDealer(ShopDealer existing) {

        existing.setRevisionNo(DateUtil.getRevisionNumber());
        shopDealerDAO.update(existing);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.profile.IShopDealerService#removeShopDealer
     * (com.ese.entity.profile.ShopDealer)
     */
    public void removeShopDealer(ShopDealer shopDealer) {

        shopDealerDAO.delete(shopDealer);
    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.esesw.service.profile.IShopDealerService#
     * addShopDealerAndShopDealerAccount(com.ese.entity.profile.ShopDealer,
     * com.sourcetrace.eses.order.entity.profile.ShopDealerAccount)
     */
    public void addShopDealerAndShopDealerAccount(ShopDealer shopDealer,
            ShopDealerAccount shopDealerAccount) {

        shopDealer.setRevisionNo(DateUtil.getRevisionNumber());
        shopDealerDAO.save(shopDealer);
        shopDealerDAO.save(shopDealerAccount);

        ESECard card = new ESECard();
        card.setCardId(idGenerator.createShopDealerCardIdSequence(IUniqueIDGenerator.WEB_REQUEST,
                IUniqueIDGenerator.ESE_AGENT_INTERNAL_ID));
        card.setType(ESECard.SHOP_DEALER_CARD);
        card.setCreateTime(new Date());
        card.setIssueDate(new Date());
        card.setStatus(ESECard.INACTIVE);
        card.setCardRewritable(ESECard.IS_REWRITABLE_NO);
        card.setProfileId(shopDealer.getShopDealerId());
        shopDealerDAO.save(card);

    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.profile.IShopDealerService#addShopDealerAccount
     * (com.sourcetrace.eses.order.entity.profile.ShopDealerAccount)
     */
    public void addShopDealerAccount(ShopDealerAccount shopDealerAccount) {

        shopDealerDAO.save(shopDealerAccount);

    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.profile.IShopDealerService#findShopDealerList ()
     */
    public List<ShopDealerAccount> findShopDealerList() {

        return shopDealerDAO.findShopDealerList();
    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.esesw.service.profile.IShopDealerService# findShopDealerAccountById(long)
     */
    public ShopDealerAccount findShopDealerAccountById(long id) {

        return shopDealerDAO.findShopDealerAccountById(id);
    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.esesw.service.profile.IShopDealerService# editShopDealerAccount
     * (com.sourcetrace.eses.order.entity.profile.ShopDealerAccount)
     */
    public void editShopDealerAccount(ShopDealerAccount shopDealerAccount) {

        shopDealerDAO.update(shopDealerAccount);

    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.esesw.service.profile.IShopDealerService#
     * findShopDealerAccountByShopDealerId(java.lang.String)
     */
    public ShopDealerAccount findShopDealerAccountByShopDealerId(String shopDealerId) {

        return shopDealerDAO.findShopDealerAccountByShopDealerId(shopDealerId);
    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.esesw.service.profile.IShopDealerService# addShopDealerAccountDetail
     * (com.sourcetrace.eses.order.entity.profile.ShopDealerAccountDetail)
     */
    public void addShopDealerAccountDetail(ShopDealerAccountDetail shopDealerAccountDetail) {

        shopDealerDAO.save(shopDealerAccountDetail);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.profile.IShopDealerService#listOrderDetail
     * (java.lang.Long)
     */
    public List<InventoryDetail> listOrderDetail(Long id) {

        return shopDealerDAO.listOrderDetail(id);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.profile.IShopDealerService#listOrderDetail
     * (java.lang.Long, int, int)
     */
    public List<InventoryDetail> listOrderDetail(Long id, int startIndex, int limit) {

        return shopDealerDAO.listOrderDetail(id, startIndex, limit);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.profile.IShopDealerService#listShopDealer()
     */
    public List<ShopDealer> listShopDealer() {

        return shopDealerDAO.listShopDealer();
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.profile.IShopDealerService#updateAccount (long, double)
     */
    public void updateAccount(long id, double outstandingBalance) {

        shopDealerDAO.updateAccount(id, outstandingBalance);

    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.profile.IShopDealerService#listShopDealerId
     * (java.lang.String)
     */
    public List<Inventory> listShopDealerId(String shopDealerId) {

        return shopDealerDAO.listShopDealerId(shopDealerId);
    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.esesw.service.profile.IShopDealerService# removeShopDealerAccount(long)
     */
    public void removeShopDealerAccount(long id) {

        shopDealerDAO.removeShopDealerAccount(id);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.profile.IShopDealerService#listOfShopDealer
     * (java.lang.Long)
     */
    public List<ShopDealer> listOfShopDealer(Long revisionNo, Long id) {

        return shopDealerDAO.listOfShopDealer(revisionNo, id);
    }

    /**
     * Gets the id generator.
     * @return the id generator
     */
    public IUniqueIDGenerator getIdGenerator() {

        return idGenerator;
    }

    /**
     * Sets the id generator.
     * @param idGenerator the new id generator
     */
    public void setIdGenerator(IUniqueIDGenerator idGenerator) {

        this.idGenerator = idGenerator;
    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.esesw.service.profile.IShopDealerService#
     * addOfflineShopDealerEnrollment(com .ese.entity.profile.OfflineShopDealerEnrollment)
     */
    public void addOfflineShopDealerEnrollment(OfflineShopDealerEnrollment offlineEnrollment) {

        shopDealerDAO.save(offlineEnrollment);
    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.esesw.service.profile.IShopDealerService#
     * processOfflineShopDealerEnrollment()
     */
    public void processOfflineShopDealerEnrollment() {

        List<OfflineShopDealerEnrollment> shopDealerEnrollmentList = shopDealerDAO
                .listShopDealerEnrollmentByStatusEnrollmentType(ESETxnStatus.PENDING.ordinal(),
                        OfflineShopDealerEnrollment.SHOP_DEALER_ENROLLMENT);
        if (!ObjectUtil.isListEmpty(shopDealerEnrollmentList)) {

            int count = 1;
            for (OfflineShopDealerEnrollment offlineShopDealerEnrollment : shopDealerEnrollmentList) {
                try {

                    LOGGER.info("Processing Offline Shop Dealer Enrollment " + count++
                            + " Of size " + shopDealerEnrollmentList.size());

                    /** VALIDATING REQUEST PARAMETER **/
                    if (StringUtil.isEmpty(offlineShopDealerEnrollment.getAgentId())) {
                        throw new OfflineTransactionException(SwitchErrorCodes.AGENT_ID_EMPTY);
                    }
                    Agent agent = agentDAO.findAgentByAgentId(offlineShopDealerEnrollment
                            .getAgentId());
                    if (ObjectUtil.isEmpty(agent)) {
                        throw new OfflineTransactionException(SwitchErrorCodes.INVALID_AGENT);
                    }

                    if (StringUtil.isEmpty(offlineShopDealerEnrollment.getShopDealerId())) {
                        throw new OfflineTransactionException(SwitchErrorCodes.EMPTY_SHOP_DEALER_ID);
                    }
                    ShopDealer shopDealer = findShopDealerByShopDealerId(offlineShopDealerEnrollment
                            .getShopDealerId());
                    if (!ObjectUtil.isEmpty(shopDealer)) {
                        throw new OfflineTransactionException(SwitchErrorCodes.SHOP_DEALER_EXIST);
                    }
                    if (StringUtil.isEmpty(offlineShopDealerEnrollment.getFirstName())) {
                        throw new OfflineTransactionException(
                                SwitchErrorCodes.EMPTY_SHOP_DEALER_FIRST_NAME);
                    }
                    if (StringUtil.isEmpty(offlineShopDealerEnrollment.getLastName())) {
                        throw new OfflineTransactionException(
                                SwitchErrorCodes.EMPTY_SHOP_DEALER_LAST_NAME);
                    }
                    if (StringUtil.isEmpty(offlineShopDealerEnrollment.getGender())) {
                        throw new OfflineTransactionException(
                                SwitchErrorCodes.EMPTY_SHOP_DEALER_GENDER);
                    }
                    Municipality city;
                    if (StringUtil.isEmpty(offlineShopDealerEnrollment.getCityCode())) {
                        throw new OfflineTransactionException(
                                SwitchErrorCodes.EMPTY_SHOP_DEALER_CITY_CODE);
                    } else {
                        city = locationDAO.findMunicipalityByCode(offlineShopDealerEnrollment
                                .getCityCode());
                        if (ObjectUtil.isEmpty(city)) {
                            throw new OfflineTransactionException(
                                    SwitchErrorCodes.INVALID_SHOP_DEALER_CITY_CODE);
                        }
                    }

                    if (StringUtil.isEmpty(offlineShopDealerEnrollment.getAddress())) {
                        throw new OfflineTransactionException(
                                SwitchErrorCodes.EMPTY_SHOP_DEALER_ADDRESS);
                    }
                    if (StringUtil.isEmpty(offlineShopDealerEnrollment.getMobileNumber())) {
                        throw new OfflineTransactionException(
                                SwitchErrorCodes.EMPTY_SHOP_DEALER_MOBILE_NUMBER);
                    }
                    if (StringUtil.isEmpty(offlineShopDealerEnrollment.getPinCode())) {
                        throw new OfflineTransactionException(
                                SwitchErrorCodes.EMPTY_SHOP_DEALER_PINCODE);
                    }
                    if (StringUtil.isEmpty(offlineShopDealerEnrollment.getShopName())) {
                        throw new OfflineTransactionException(
                                SwitchErrorCodes.EMPTY_SHOP_DEALER_SHOP_NAME);
                    }
                    if (!ShopDealer.SAME_AS_SHOP_DEALER
                            .equalsIgnoreCase(offlineShopDealerEnrollment.getContactPerson())) {
                        if (StringUtil.isEmpty(offlineShopDealerEnrollment
                                .getContactPersonFirstName())) {
                            throw new OfflineTransactionException(
                                    SwitchErrorCodes.EMPTY_SHOP_DEALER_CONTACT_PERSON_FIRST_NAME);
                        }
                        if (StringUtil.isEmpty(offlineShopDealerEnrollment
                                .getContactPersonLastName())) {
                            throw new OfflineTransactionException(
                                    SwitchErrorCodes.EMPTY_SHOP_DEALER_CONTACT_PERSON_LAST_NAME);
                        }
                    }

                    /** FORMING SHOP DEALER OBJECT **/
                    shopDealer = new ShopDealer();
                    shopDealer.setShopDealerId(offlineShopDealerEnrollment.getShopDealerId());
                    if (!ObjectUtil.isEmpty(offlineShopDealerEnrollment.getDateOfBirth())) {
                        try {
                            shopDealer.setDateOfBirth(DateUtil.convertStringToDate(
                                    offlineShopDealerEnrollment.getDateOfBirth(), SHOP_DEALER_DOB));
                        } catch (Exception e) {
                            throw new OfflineTransactionException(
                                    SwitchErrorCodes.INVALID_SHOP_DEALER_DOB_DATE_FORMAT);
                        }
                    }
                    shopDealer.setFirstName(offlineShopDealerEnrollment.getFirstName());
                    shopDealer.setLastName(offlineShopDealerEnrollment.getLastName());
                    shopDealer.setGender(offlineShopDealerEnrollment.getGender().toUpperCase());
                    shopDealer.setCity(city);
                    shopDealer.setAddress(offlineShopDealerEnrollment.getAddress());
                    shopDealer.setEmail(offlineShopDealerEnrollment.getEmail());
                    shopDealer.setMobileNumber(offlineShopDealerEnrollment.getMobileNumber());
                    shopDealer.setPhoneNumber(offlineShopDealerEnrollment.getPhoneNumber());
                    shopDealer.setPinCode(offlineShopDealerEnrollment.getPinCode());
                    shopDealer.setShopName(offlineShopDealerEnrollment.getShopName());

                    if (ShopDealer.SAME_AS_SHOP_DEALER.equalsIgnoreCase(offlineShopDealerEnrollment
                            .getContactPerson())) {
                        shopDealer.setContactPerson(true);
                        shopDealer.setContactPersonFirstName(offlineShopDealerEnrollment
                                .getFirstName());
                        shopDealer.setContactPersonLastName(offlineShopDealerEnrollment
                                .getLastName());
                    } else {
                        shopDealer.setContactPerson(false);
                        shopDealer.setContactPersonFirstName(offlineShopDealerEnrollment
                                .getContactPersonFirstName());
                        shopDealer.setContactPersonLastName(offlineShopDealerEnrollment
                                .getContactPersonLastName());
                    }

                    /** GENERATING CARD ID **/
                    if (StringUtil.isEmpty(offlineShopDealerEnrollment.getCardId())) {
                        offlineShopDealerEnrollment.setCardId(idGenerator
                                .createShopDealerCardIdSequence(IUniqueIDGenerator.WEB_REQUEST,
                                        IUniqueIDGenerator.ESE_AGENT_INTERNAL_ID));
                    }
                    ESECard existingCard = cardDAO.findESECardByCardId(offlineShopDealerEnrollment
                            .getCardId());
                    if (!ObjectUtil.isEmpty(existingCard)) {
                        throw new OfflineTransactionException(
                                SwitchErrorCodes.SHOP_DEALER_CARD_EXIST);
                    }

                    /** FORMING IMAGE INFO **/
                    if (offlineShopDealerEnrollment.getPhoto() != null
                            || offlineShopDealerEnrollment.getFingerPrint() != null) {
                        ImageInfo imageInfo = new ImageInfo();
                        if (offlineShopDealerEnrollment.getPhoto() != null) {
                            Image photo = new Image();
                            photo.setImage(offlineShopDealerEnrollment.getPhoto());
                            photo.setImageId(offlineShopDealerEnrollment.getShopDealerId() + "-SP");
                            imageInfo.setPhoto(photo);
                        }
                        if (offlineShopDealerEnrollment.getFingerPrint() != null) {
                            Image fp = new Image();
                            fp.setImage(offlineShopDealerEnrollment.getFingerPrint());
                            fp.setImageId(offlineShopDealerEnrollment.getShopDealerId() + "-SF");
                            imageInfo.setBiometric(fp);
                        }
                        shopDealer.setImageInfo(imageInfo);
                    }

                    /** SAVING SHOP DEALER OBJECT **/
                    addShopDealer(shopDealer);

                    /** SAVING SHOP DEALER ACCOUNT OBJECT **/
                    ShopDealerAccount shopDealerAccount = new ShopDealerAccount();
                    shopDealerAccount.setShopDealer(shopDealer);
                    shopDealerAccount.setCreditLimit(5000.00);
                    shopDealerAccount.setOutstandingBalance(0.0);
                    addShopDealerAccount(shopDealerAccount);

                    /** SAVING CARD INFORMATION **/
                    ESECard card = new ESECard();
                    card.setCardId(offlineShopDealerEnrollment.getCardId());
                    card.setType(ESECard.SHOP_DEALER_CARD);
                    card.setCreateTime(new Date());
                    card.setIssueDate(new Date());
                    card.setProfileId(offlineShopDealerEnrollment.getShopDealerId());
                    card.setStatus(ESECard.INACTIVE);
                    card.setCardRewritable(ESECard.IS_REWRITABLE_NO);
                    shopDealerDAO.save(card);

                    offlineShopDealerEnrollment.setStatusCode(ESETxnStatus.SUCCESS.ordinal());
                    offlineShopDealerEnrollment.setStatusMsg(ESETxnStatus.SUCCESS.toString());
                    shopDealerDAO.update(offlineShopDealerEnrollment);
                } catch (OfflineTransactionException e) {
                    offlineShopDealerEnrollment.setStatusCode(ESETxnStatus.ERROR.ordinal());
                    offlineShopDealerEnrollment.setStatusMsg(e.getError());
                    shopDealerDAO.update(offlineShopDealerEnrollment);
                } catch (Exception e) {
                    offlineShopDealerEnrollment.setStatusCode(ESETxnStatus.ERROR.ordinal());
                    offlineShopDealerEnrollment.setStatusMsg("SERVER ERROR");
                    shopDealerDAO.update(offlineShopDealerEnrollment);
                }
            }

        }
    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.esesw.service.profile.IShopDealerService#
     * processOfflineShopDealerBiometricUpload()
     */
    public void processOfflineShopDealerBiometricUpload() {

        List<OfflineShopDealerEnrollment> shopDealerBiometricList = shopDealerDAO
                .listShopDealerEnrollmentByStatusEnrollmentType(ESETxnStatus.PENDING.ordinal(),
                        OfflineShopDealerEnrollment.SHOP_DEALER_BIOMETRIC_UPLOAD);
        if (!ObjectUtil.isListEmpty(shopDealerBiometricList)) {

            int count = 1;
            for (OfflineShopDealerEnrollment offlineShopDealerEnrollment : shopDealerBiometricList) {
                try {

                    LOGGER.info("Processing Offline Shop Dealer Biometric Upload " + count++
                            + " Of size " + shopDealerBiometricList.size());

                    /** VALIDATING REQUEST PARAMETER **/
                    if (StringUtil.isEmpty(offlineShopDealerEnrollment.getAgentId())) {
                        throw new OfflineTransactionException(SwitchErrorCodes.AGENT_ID_EMPTY);
                    }
                    Agent agent = agentDAO.findAgentByAgentId(offlineShopDealerEnrollment
                            .getAgentId());
                    if (ObjectUtil.isEmpty(agent)) {
                        throw new OfflineTransactionException(SwitchErrorCodes.INVALID_AGENT);
                    }

                    if (StringUtil.isEmpty(offlineShopDealerEnrollment.getShopDealerId())) {
                        throw new OfflineTransactionException(SwitchErrorCodes.EMPTY_SHOP_DEALER_ID);
                    }
                    ShopDealer shopDealer = findShopDealerByShopDealerId(offlineShopDealerEnrollment
                            .getShopDealerId());
                    if (ObjectUtil.isEmpty(shopDealer)) {
                        throw new OfflineTransactionException(
                                SwitchErrorCodes.SHOP_DEALER_NOT_EXIST);
                    }
                    if (StringUtil.isEmpty(offlineShopDealerEnrollment.getCardId())) {
                        throw new OfflineTransactionException(
                                SwitchErrorCodes.EMPTY_SHOP_DEALER_CARD_ID);
                    }
                    ESECard card = cardDAO.findCardByProfileIdAndCardId(shopDealer
                            .getShopDealerId(), offlineShopDealerEnrollment.getCardId());
                    if (ObjectUtil.isEmpty(card)) {
                        throw new OfflineTransactionException(
                                SwitchErrorCodes.SHOP_DEALER_CARD_UNAVAILABLE);
                    }

                    /** DATA PROCESSING **/
                    if (ObjectUtil.isEmpty(shopDealer.getImageInfo())) {
                        if (offlineShopDealerEnrollment.getPhoto() != null
                                && offlineShopDealerEnrollment.getFingerPrint() != null) {
                            ImageInfo imageInfo = new ImageInfo();
                            if (offlineShopDealerEnrollment.getPhoto() != null) {
                                Image photo = new Image();
                                photo.setImage(offlineShopDealerEnrollment.getPhoto());
                                photo.setImageId(offlineShopDealerEnrollment.getShopDealerId()
                                        + "-SP");
                                imageInfo.setPhoto(photo);
                            }
                            if (offlineShopDealerEnrollment.getFingerPrint() != null) {
                                Image fp = new Image();
                                fp.setImage(offlineShopDealerEnrollment.getFingerPrint());
                                fp
                                        .setImageId(offlineShopDealerEnrollment.getShopDealerId()
                                                + "-SF");
                                imageInfo.setBiometric(fp);
                            }
                            shopDealer.setImageInfo(imageInfo);
                        }
                    } else {
                        if (offlineShopDealerEnrollment.getFingerPrint() != null) {
                            if (ObjectUtil.isEmpty(shopDealer.getImageInfo().getBiometric())) {
                                Image fp = new Image();
                                fp.setImage(offlineShopDealerEnrollment.getFingerPrint());
                                fp
                                        .setImageId(offlineShopDealerEnrollment.getShopDealerId()
                                                + "-SF");
                                shopDealer.getImageInfo().setBiometric(fp);
                            } else {
                                shopDealer.getImageInfo().getBiometric().setImage(
                                        offlineShopDealerEnrollment.getFingerPrint());
                            }
                        }
                        if (offlineShopDealerEnrollment.getPhoto() != null) {
                            if (ObjectUtil.isEmpty(shopDealer.getImageInfo().getPhoto())) {
                                Image photo = new Image();
                                photo.setImage(offlineShopDealerEnrollment.getPhoto());
                                photo.setImageId(offlineShopDealerEnrollment.getShopDealerId()
                                        + "-SP");
                                shopDealer.getImageInfo().setPhoto(photo);
                            } else {
                                shopDealer.getImageInfo().getPhoto().setImage(
                                        offlineShopDealerEnrollment.getPhoto());
                            }
                        }
                    }

                    shopDealerDAO.update(shopDealer);
                    offlineShopDealerEnrollment.setStatusCode(ESETxnStatus.SUCCESS.ordinal());
                    offlineShopDealerEnrollment.setStatusMsg(ESETxnStatus.SUCCESS.toString());
                    shopDealerDAO.update(offlineShopDealerEnrollment);

                } catch (OfflineTransactionException e) {
                    offlineShopDealerEnrollment.setStatusCode(ESETxnStatus.ERROR.ordinal());
                    offlineShopDealerEnrollment.setStatusMsg(e.getError());
                    shopDealerDAO.update(offlineShopDealerEnrollment);
                } catch (Exception e) {
                    offlineShopDealerEnrollment.setStatusCode(ESETxnStatus.ERROR.ordinal());
                    offlineShopDealerEnrollment.setStatusMsg("SERVER ERROR");
                    shopDealerDAO.update(offlineShopDealerEnrollment);
                }
            }
        }

    }

    /**
     * Sets the shop dealer dao.
     * @param shopDealerDAO the new shop dealer dao
     */
    public void setShopDealerDAO(IShopDealerDAO shopDealerDAO) {

        this.shopDealerDAO = shopDealerDAO;
    }

    /**
     * Gets the shop dealer dao.
     * @return the shop dealer dao
     */
    public IShopDealerDAO getShopDealerDAO() {

        return shopDealerDAO;
    }

    /**
     * Gets the location dao.
     * @return the location dao
     */
    public ILocationDAO getLocationDAO() {

        return locationDAO;
    }

    /**
     * Sets the location dao.
     * @param locationDAO the new location dao
     */
    public void setLocationDAO(ILocationDAO locationDAO) {

        this.locationDAO = locationDAO;
    }

    /**
     * Sets the card dao.
     * @param cardDAO the new card dao
     */
    public void setCardDAO(ICardDAO cardDAO) {

        this.cardDAO = cardDAO;
    }

    /**
     * Gets the card dao.
     * @return the card dao
     */
    public ICardDAO getCardDAO() {

        return cardDAO;
    }

    /**
     * Gets the agent dao.
     * @return the agent dao
     */
    public IAgentDAO getAgentDAO() {

        return agentDAO;
    }

    /**
     * Sets the agent dao.
     * @param agentDAO the new agent dao
     */
    public void setAgentDAO(IAgentDAO agentDAO) {

        this.agentDAO = agentDAO;
    }

}
