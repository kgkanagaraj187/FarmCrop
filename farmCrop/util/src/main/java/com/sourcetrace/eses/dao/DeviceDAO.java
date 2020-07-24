/*
 * DeviceDAO.java
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
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.esesw.entity.profile.Client;
import com.sourcetrace.esesw.entity.profile.EnrollmentStation;
import com.sourcetrace.esesw.entity.profile.POS;

@Repository
@Transactional
public class DeviceDAO extends ESEDAO implements IDeviceDAO {

    private Query query;

    @Autowired
    public DeviceDAO(SessionFactory sessionFactory) {

        this.setSessionFactory(sessionFactory);
    }



    /*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IDeviceDAO#listDevices()
     */
    public List<Device> listDevices() {
        return list("FROM Device d where d.isRegistered=1");
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IDeviceDAO#findDevice(java.lang.String)
     */
    public EnrollmentStation findEnrollmentStationByDeviceId(String deviceId) {
        EnrollmentStation device = (EnrollmentStation) find("FROM EnrollmentStation es WHERE es.deviceId = ?",
                deviceId);

        return device;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IDeviceDAO#listEnrollmentStataions()
     */
    public List<EnrollmentStation> listAllEnrollmentStataions() {

        return (List<EnrollmentStation>) list("FROM EnrollmentStation");
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IDeviceDAO#listEnrollmentStataions()
     */
    public List<EnrollmentStation> listEnrollmentStataions() {

        return (List<EnrollmentStation>) list("FROM EnrollmentStation es WHERE  es.enabled = ?", true);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IDeviceDAO#listPOSDevices()
     */
    public List<POS> listAllPOSDevices() {

        return (List<POS>) list("FROM POS");
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IDeviceDAO#listPOSDevices()
     */
    public List<POS> listPOSDevices() {
        return (List<POS>) list("FROM POS p WHERE  p.enabled = ?", true);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IDeviceDAO#findDeviceBySerialNumber(java.lang.String)
     */
    public POS findPOSBySerialNumber(String serialNumber) {
        POS pos = (POS) find("FROM POS p WHERE p.serialNumber = ?", serialNumber);

        return pos;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IDeviceDAO#findPOSByDeviceId(java.lang.String)
     */
    public POS findPOSByDeviceId(String posId) {
        POS pos = (POS) find("FROM POS p WHERE p.deviceId = ?", posId);

        return pos;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IDeviceDAO#findPOS(long)
     */
    public POS findPOS(long id) {
        POS pos = (POS) find("FROM POS p WHERE p.id = ?", id);

        return pos;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IDeviceDAO#findEnrollmentStation(long)
     */
    public EnrollmentStation findEnrollmentStation(long id) {
        EnrollmentStation device = (EnrollmentStation) find("FROM EnrollmentStation es WHERE es.id = ?", id);

        return device;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IDeviceDAO#findDeviceBySerialNumber(java.lang.String)
     */
    public EnrollmentStation findEnrollmentStationBySerialNumber(String serialNumber) {
        EnrollmentStation es = (EnrollmentStation) find("FROM EnrollmentStation es WHERE es.serialNumber = ?",
                serialNumber);

        return es;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IDeviceDAO#findEnrollmentStationTransaction(java.
	 * lang.String)
     */
    public boolean findEnrollmentStationTransaction(String deviceId) {
        Client txn = (Client) find("FROM Client c WHERE c.enrolledStationId = ?", deviceId);

        return txn != null ? true : false;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IDeviceDAO#findPOSTransaction(java.lang.String)
     */
    public boolean findPOSTransaction(String deviceId) {
        Transaction txn = (Transaction) find("FROM Transaction c WHERE c.deviceId = ?", deviceId);

        return txn != null ? true : false;

    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IDeviceDAO#isBranchHaveDevice(java.lang.String)
     */
    public boolean isBranchHaveDevice(String branchId) {
        List<Device> devices = listDevices();

        for (Device device : devices) {
            if (device.getBranch().getBranchId().equalsIgnoreCase(branchId)) {
                return true;
            }
        }
        return false;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IDeviceDAO#isLocationHaveDevice(long)
     */
    public boolean isLocationHaveDevice(long id) {
        List<Device> devices = listDevices();

        for (Device device : devices) {
            if (device.getLocation().getId() == id) {
                return true;
            }
        }
        return false;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IDeviceDAO#isLocationHaveEnrollmentStation(long)
     */
    public boolean isLocationHaveEnrollmentStation(long id) {
        List<EnrollmentStation> esList = listEnrollmentStataions();

        for (EnrollmentStation es : esList) {
            if (es.getLocation().getId() == id) {
                return true;
            }
        }
        return false;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IDeviceDAO#listDevices(java.lang.String)
     */
    public List<Device> listDevices(String deviceType) {
        return (List<Device>) list("FROM Device di WHERE di.deviceType = '" + deviceType + "'");
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IDeviceDAO#findDeviceById(java.lang.Long)
     */
    public Device findDeviceById(Long id) {
        Session session = getSessionFactory().getCurrentSession();
        session.disableFilter(ISecurityFilter.BRANCH_FILTER);
        Query query = session.createQuery("FROM Device di WHERE di.id = :id AND di.deleted = '0'");
        query.setParameter("id", id);
        return (Device) query.uniqueResult();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IDeviceDAO#findDeviceByCode(java.lang.String)
     */
    public Device findDeviceByCode(String code) {
        return (Device) find("FROM Device di WHERE di.code = ? AND di.deleted = '0'", code);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IDeviceDAO#findDeviceBySerialNumber(java.lang.String)
     */
    public Device findDeviceBySerialNumber(String serialNumber) {
        Session session = getSessionFactory().openSession();
        session.disableFilter(ISecurityFilter.BRANCH_FILTER);
        Query query = session.createQuery("From Device where serialNumber=:serialNo and deleted=false");
        query.setParameter("serialNo", serialNumber);
        List list = query.list();
        Device device = null;
        if (list.size() > 0) {
            device = (Device) query.list().get(0);
        }
        session.flush();
        session.close();
        return device;
    }

    public void updateMsgNo(String serialNo, String msgNo) {
        Session session = getSessionFactory().openSession();
        Query query = session.createQuery("update Device set msgNo = :msgNo" + " where serialNumber = :serialNo");
        query.setParameter("msgNo", msgNo);
        query.setParameter("serialNo", serialNo);
        int result = query.executeUpdate();
        session.flush();
        session.close();

    }

    public List<Boolean> listDeviceStatus() {

        Session session = getSessionFactory().openSession();
        String queryString = "SELECT D.ENABLED FROM DEVICE D WHERE D.IS_DELETED = '0'";
        Query query = session.createSQLQuery(queryString);
        List list = query.list();
        session.flush();
        session.close();
        return list;
    }

    @Override
    public Device findUnRegisterDeviceById(Long id) {
        Session session = getSessionFactory().openSession();
        session.disableFilter(ISecurityFilter.BRANCH_FILTER);
        Device device = (Device) session.get(Device.class, id);
        session.flush();
        session.close();
        return device;
    }

    @Override
    public List<Boolean> listDeviceStatusBasedOnBranch(String branchIdValue) {

        Session session = getSessionFactory().openSession();
        String queryString = "SELECT D.ENABLED FROM DEVICE D WHERE D.IS_DELETED = '0' AND D.BRANCH_ID= :BRANCH";
        Query query = session.createSQLQuery(queryString);
        query.setParameter("BRANCH", branchIdValue);
        List list = query.list();
        session.flush();
        session.close();
        return list;
    }

    @Override
    public Integer findDeviceCount() {
        Session session = getSessionFactory().getCurrentSession();
        return ((Long) session.createQuery("select count(*) from Device d where d.isRegistered=1 ").uniqueResult()).intValue();
    }

    @Override
    public Integer findDeviceCountByMonth(Date sDate, Date eDate) {
        Session session = getSessionFactory().openSession();
        Query query = session.createQuery("select count(*) from Device where createdDate BETWEEN :startDate AND :endDate");
        query.setParameter("startDate", sDate).setParameter("endDate", eDate);
        Integer val = ((Long) query.uniqueResult()).intValue();
        session.flush();
        session.close();        
        return val;
    }
    
    public Device findDeviceBySerialNumber(String serialNumber, String tenantId) {
        //Session session = getSessionFactory().openSession();
    	Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
        session.disableFilter(ISecurityFilter.BRANCH_FILTER);
        Query query = session.createQuery("From Device where serialNumber=:serialNo and deleted=false");
        query.setParameter("serialNo", serialNumber);
        List list = query.list();
        Device device = null;
        if (list.size() > 0) {
            device = (Device) query.list().get(0);
        }
        session.flush();
        session.close();
        return device;
    }
}
