/*
 * IShopDealerService.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import java.util.List;

import com.sourcetrace.eses.order.entity.profile.ShopDealerAccount;
import com.sourcetrace.eses.order.entity.profile.ShopDealerAccountDetail;
import com.sourcetrace.eses.order.entity.txn.Inventory;
import com.sourcetrace.eses.order.entity.txn.InventoryDetail;
import com.sourcetrace.esesw.entity.profile.OfflineShopDealerEnrollment;
import com.sourcetrace.esesw.entity.profile.ShopDealer;

public interface IShopDealerService {

    /**
     * Adds the shop dealer.
     * @param shopDealer the shop dealer
     */
    public void addShopDealer(ShopDealer shopDealer);

    /**
     * Find shop dealer by shop dealer id.
     * @param shopDealerId the shop dealer id
     * @return the shop dealer
     */
    public ShopDealer findShopDealerByShopDealerId(String shopDealerId);

    /**
     * Find shop dealer by id.
     * @param id the id
     * @return the shop dealer
     */
    ShopDealer findShopDealerById(Long id);

    /**
     * Edits the shop dealer.
     * @param existing the existing
     */
    void editShopDealer(ShopDealer existing);

    /**
     * Removes the shop dealer.
     * @param shopDealer the shop dealer
     */
    void removeShopDealer(ShopDealer shopDealer);

    /**
     * Adds the shop dealer and shop dealer account.
     * @param shopDealer the shop dealer
     * @param shopDealerAccount the shop dealer account
     */
    void addShopDealerAndShopDealerAccount(ShopDealer shopDealer,
            ShopDealerAccount shopDealerAccount);

    /**
     * Adds the shop dealer account.
     * @param shopDealerAccount the shop dealer account
     */
    public void addShopDealerAccount(ShopDealerAccount shopDealerAccount);

    /**
     * Find shop dealer list.
     * @return the list< shop dealer account>
     */
    public List<ShopDealerAccount> findShopDealerList();

    /**
     * Find shop dealer account by id.
     * @param id the id
     * @return the shop dealer account
     */
    public ShopDealerAccount findShopDealerAccountById(long id);

    /**
     * Edits the shop dealer account.
     * @param shopDealerAccount the shop dealer account
     */
    public void editShopDealerAccount(ShopDealerAccount shopDealerAccount);

    /**
     * Find shop dealer account by shop dealer id.
     * @param shopDealerId the shop dealer id
     * @return the shop dealer account
     */
    public ShopDealerAccount findShopDealerAccountByShopDealerId(String shopDealerId);

    /**
     * Adds the shop dealer account detail.
     * @param shopDealerAccountDetail the shop dealer account detail
     */
    public void addShopDealerAccountDetail(ShopDealerAccountDetail shopDealerAccountDetail);

    /**
     * List order detail.
     * @param id the id
     * @return the list< inventory detail>
     */
    public List<InventoryDetail> listOrderDetail(Long id);

    /**
     * List order detail.
     * @param id the id
     * @param startIndex the start index
     * @param limit the limit
     * @return the list< inventory detail>
     */
    public List<InventoryDetail> listOrderDetail(Long id, int startIndex, int limit);

    /**
     * List shop dealer.
     * @return the list< shop dealer>
     */
    public List<ShopDealer> listShopDealer();

    /**
     * Update account.
     * @param id the id
     * @param outstandingBalance the outstanding balance
     */
    public void updateAccount(long id, double outstandingBalance);

    /**
     * List shop dealer id.
     * @param shopDealerId the shop dealer id
     * @return the list< inventory>
     */
    public List<Inventory> listShopDealerId(String shopDealerId);

    /**
     * Removes the shop dealer account.
     * @param id the id
     */
    public void removeShopDealerAccount(long id);

    /**
     * List of shop dealer.
     * @param revisionNo the revision no
     * @param id the id
     * @return the list< shop dealer>
     */
    public List<ShopDealer> listOfShopDealer(Long revisionNo, Long id);

    /**
     * Adds the offline shop dealer enrollment.
     * @param offlineEnrollment the offline enrollment
     */
    public void addOfflineShopDealerEnrollment(OfflineShopDealerEnrollment offlineEnrollment);

    /**
     * Process offline shop dealer enrollment.
     */
    public void processOfflineShopDealerEnrollment();

    /**
     * Process offline shop dealer biometric upload.
     */
    public void processOfflineShopDealerBiometricUpload();

}
