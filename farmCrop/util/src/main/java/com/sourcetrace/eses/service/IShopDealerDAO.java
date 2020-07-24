package com.sourcetrace.eses.service;

import java.util.List;

import com.sourcetrace.eses.dao.IESEDAO;
import com.sourcetrace.eses.order.entity.profile.ShopDealerAccount;
import com.sourcetrace.eses.order.entity.txn.Inventory;
import com.sourcetrace.eses.order.entity.txn.InventoryDetail;
import com.sourcetrace.esesw.entity.profile.OfflineShopDealerEnrollment;
import com.sourcetrace.esesw.entity.profile.ShopDealer;

public interface IShopDealerDAO extends IESEDAO {

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
     * Find shop dealer account by shop dealer id.
     * @param shopDealerId the shop dealer id
     * @return the shop dealer account
     */
    public ShopDealerAccount findShopDealerAccountByShopDealerId(String shopDealerId);

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
     * Checks if is city mappingexist.
     * @param id the id
     * @return true, if is city mappingexist
     */
    public boolean isCityMappingexist(long id);

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
     * List shop dealer enrollment by status.
     * @param txnStatus the txn status
     * @return the list< offline shop dealer enrollment>
     */
    public List<OfflineShopDealerEnrollment> listShopDealerEnrollmentByStatus(int txnStatus);

    /**
     * List shop dealer enrollment by status enrollment type.
     * @param txnStatus the txn status
     * @param txnType the txn type
     * @return the list< offline shop dealer enrollment>
     */
    public List<OfflineShopDealerEnrollment> listShopDealerEnrollmentByStatusEnrollmentType(
            int txnStatus, String txnType);

}