/*
 * ShopDealerDAO.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sourcetrace.eses.order.entity.profile.ShopDealerAccount;
import com.sourcetrace.eses.order.entity.txn.Inventory;
import com.sourcetrace.eses.order.entity.txn.InventoryDetail;
import com.sourcetrace.eses.service.IShopDealerDAO;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.esesw.entity.profile.OfflineShopDealerEnrollment;
import com.sourcetrace.esesw.entity.profile.ShopDealer;

@Repository
@Transactional
public class ShopDealerDAO extends ESEDAO implements IShopDealerDAO
{
	
	@Autowired
	 public ShopDealerDAO(SessionFactory sessionFactory) {

		  this.setSessionFactory(sessionFactory);
		 }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.profile.IShopDealerDAO#findShopDealerByShopDealerId
     * (java.lang.String)
     */
    public ShopDealer findShopDealerByShopDealerId(String shopDealerId) {

        ShopDealer shopDealer = (ShopDealer) find("FROM ShopDealer sd WHERE  sd.shopDealerId = ?",
                shopDealerId);

        return shopDealer;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.profile.IShopDealerDAO#findShopDealerById(java .lang.Long)
     */
    public ShopDealer findShopDealerById(Long id) {

        return (ShopDealer) find("FROM ShopDealer sd where sd.id=?", id);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.profile.IShopDealerDAO#findShopDealerList()
     */
    public List<ShopDealerAccount> findShopDealerList() {

        return list("FROM ShopDealerAccount");
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.profile.IShopDealerDAO#findShopDealerAccountById (long)
     */
    public ShopDealerAccount findShopDealerAccountById(long id) {

        return (ShopDealerAccount) find("FROM ShopDealerAccount sda WHERE sda.id = ?", id);
    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.esesw.dao.profile.IShopDealerDAO#
     * findShopDealerAccountByShopDealerId(java.lang.String)
     */
    public ShopDealerAccount findShopDealerAccountByShopDealerId(String shopDealerId) {

        return (ShopDealerAccount) find(
                "From ShopDealerAccount sda WHERE sda.shopDealer.shopDealerId=?", shopDealerId);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.profile.IShopDealerDAO#listOrderDetail(java .lang.Long)
     */
    public List<InventoryDetail> listOrderDetail(Long id) {

        return list("From InventoryDetail order Where order.inventory.id = ?", id);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.profile.IShopDealerDAO#listOrderDetail(java .lang.Long, int,
     * int)
     */
    public List<InventoryDetail> listOrderDetail(Long id, int startIndex, int limit) {

        Session session = getSessionFactory().openSession();
        String queryString = "select * FROM inventory_detail WHERE INVENTORY_ID = '" + id
                + "' LIMIT " + startIndex + "," + limit;
        Query query = session.createSQLQuery(queryString).addEntity(InventoryDetail.class);
        List<InventoryDetail> list = query.list();
        session.flush();
        session.close();
        return list;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.profile.IShopDealerDAO#listShopDealer()
     */
    public List<ShopDealer> listShopDealer() {

        return list("From ShopDealer s ORDER BY s.firstName ASC");
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.profile.IShopDealerDAO#updateAccount(long, double)
     */
    public void updateAccount(long id, double outstandingBalance) {

        Session session = getSessionFactory().openSession();
        Query query = session
                .createQuery("update ShopDealerAccount set outstandingBalance = :outstandingBalance"
                        + " where id = :id");
        query.setParameter("id", id);
        query.setParameter("outstandingBalance", outstandingBalance);
        int result = query.executeUpdate();
        session.flush();
        session.close();
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.profile.IShopDealerDAO#isCityMappingexist(long)
     */
    public boolean isCityMappingexist(long id) {

        List<ShopDealer> shopDealerList = list("FROM ShopDealer sd WHERE sd.city.id = ?", id);
        if (!ObjectUtil.isListEmpty(shopDealerList)) {
            return true;
        }
        return false;

    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.profile.IShopDealerDAO#listShopDealerId(java .lang.String)
     */
    public List<Inventory> listShopDealerId(String shopDealerId) {

        return list("FROM Inventory iv WHERE iv.shopDealerId = ?", shopDealerId);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.profile.IShopDealerDAO#removeShopDealerAccount (long)
     */
    public void removeShopDealerAccount(long id) {

        Session session = getSessionFactory().openSession();
        Query query = session
                .createQuery("DELETE FROM ShopDealerAccount sda where sda.shopDealer.id = :id");
        query.setParameter("id", id);
        int result = query.executeUpdate();
        session.flush();
        session.close();

    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.profile.IShopDealerDAO#listOfShopDealer(java .lang.Long)
     */
    public List<ShopDealer> listOfShopDealer(Long revisionNo, Long id) {

        Object[] values = { revisionNo, id };
        return list(
                "FROM ShopDealer sd WHERE sd.revisionNo > ? AND sd.city.id = ? order by sd.revisionNo DESC",
                values);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.profile.IShopDealerDAO#listShopDealerEnrollmentByStatus(int)
     */
    @SuppressWarnings("unchecked")
    public List<OfflineShopDealerEnrollment> listShopDealerEnrollmentByStatus(int txnStatus) {

        return list("FROM OfflineShopDealerEnrollment osd WHERE statusCode=?", txnStatus);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.dao.profile.IShopDealerDAO#listShopDealerEnrollmentByStatusEnrollmentType
     * (int, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<OfflineShopDealerEnrollment> listShopDealerEnrollmentByStatusEnrollmentType(
            int txnStatus, String txnType) {

        Object[] values = { txnStatus, txnType };
        return list(
                "FROM OfflineShopDealerEnrollment osd WHERE osd.statusCode=? AND osd.transactionType=?",
                values);
    }

}
