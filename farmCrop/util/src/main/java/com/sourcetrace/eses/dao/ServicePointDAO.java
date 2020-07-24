/*
 * ServicePointDAO.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
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

import com.sourcetrace.eses.service.IServicePointDAO;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.esesw.entity.profile.ServicePoint;
import com.sourcetrace.esesw.entity.profile.ServicePointType;

// TODO: Auto-generated Javadoc
/**
 * The Class ServicePointDAO.
 * @author $Author: boopalan $
 * @version $Rev: 1164 $ $Date: 2010-04-25 12:01:00 +0530 (Sun, 25 Apr 2010) $
 */
@Repository
@Transactional
public class ServicePointDAO extends ESEDAO implements IServicePointDAO
{
	
	@Autowired
	public ServicePointDAO(SessionFactory sessionFactory) {

		this.setSessionFactory(sessionFactory);
	}

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.profile.IServicePointDAO#list()
     */
    public List<ServicePoint> list() {

        return (List<ServicePoint>) list("FROM ServicePoint sp ORDER BY sp.name ASC");
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.profile.IServicePointDAO#find(long)
     */
    public ServicePoint find(long id) {

        ServicePoint servicePoint = (ServicePoint) find("FROM ServicePoint es WHERE es.id = ?", id);
        return servicePoint;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.profile.IServicePointDAO#findBySerialNumber(java.lang.String)
     */
    public ServicePoint findBySerialNumber(String serialNumber) {

        ServicePoint es = (ServicePoint) find(
                "FROM ServicePoint sp LEFT JOIN FETCH sp.store WHERE sp.serialNumber = ?",
                serialNumber);
        return es;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.dao.profile.IServicePointDAO#findByServicePointId(java.lang.String)
     */
    public ServicePoint findByServicePointId(String servicePointId) {

        ServicePoint sp = (ServicePoint) find("FROM ServicePoint sp WHERE sp.id = ?", new Long(
                servicePointId));
        return sp;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.dao.profile.IServicePointDAO#findByServicePointName(java.lang.String)
     */
    public ServicePoint findByServicePointName(String servicePointName) {

        ServicePoint sp = (ServicePoint) find("FROM ServicePoint sp WHERE sp.name = ?",
                servicePointName);
        return sp;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.profile.IServicePointDAO#listServicePointTypes()
     */
    public List<ServicePointType> listServicePointTypes() {

        return list("From ServicePointType");
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.dao.profile.IServicePointDAO#findServicePiontTypeByCode(java.lang.String
     * )
     */
    public ServicePointType findServicePiontTypeByCode(String code) {

        return (ServicePointType) super.find("FROM ServicePointType c WHERE c.code=?", code);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.dao.profile.IServicePointDAO#listServicePointsByStore(java.lang.String)
     */
    public List<ServicePoint> listServicePointsByStore(String store) {

        return list("From ServicePoint sp where sp.store.storeId=? ", store);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.dao.profile.IServicePointDAO#findServicePointByName(java.lang.String)
     */
    public ServicePoint findServicePointByName(String selectedServicePoint) {

        ServicePoint servicePoint = (ServicePoint) find("FROM ServicePoint es WHERE es.name = ?",
                selectedServicePoint);
        return servicePoint;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.dao.profile.IServicePointDAO#findServicePointByCode(java.lang.String)
     */
    public ServicePoint findServicePointByCode(String code) {

        return (ServicePoint) find("FROM ServicePoint sp WHERE sp.code = ?", code);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.profile.IServicePointDAO#listServicePointCode()
     */
    public List<ServicePoint> listServicePointCode() {

        return (List<ServicePoint>) list("select code FROM ServicePoint");
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.profile.IServicePointDAO#isCityMappingexist(long)
     */
    public boolean isCityMappingexist(long id) {

        List<ServicePoint> servicePointList = list("FROM ServicePoint sp WHERE sp.city.id = ?", id);
        if (!ObjectUtil.isListEmpty(servicePointList)) {
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.dao.profile.IServicePointDAO#findServPointByServPointId(java.lang.String
     * )
     */
    public ServicePoint findServPointByServPointId(String servPointId) {

        ServicePoint servicePoint = (ServicePoint) find("FROM ServicePoint es WHERE es.code = ?", servPointId);
        return servicePoint;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.profile.IServicePointDAO#isAgentWarehouseMappingExists(long)
     */
    public boolean isAgentWarehouseMappingExists(long servicePointId) {

        Session session = getSessionFactory().openSession();
        String queryString = "SELECT COUNT(wh.ID) FROM serv_point sp "
                + " LEFT JOIN city c ON c.ID = sp.CITY_ID "
                + " LEFT JOIN warehouse wh ON wh.CITY_ID = c.ID "
                + " INNER JOIN agent_warehouse_map awm ON awm.WAREHOUSE_ID = wh.ID "
                + " WHERE sp.id = " + servicePointId;
        Query query = session.createSQLQuery(queryString);
        List list = query.list();
        session.flush();
        session.close();
        if (list.size() > 0 && (Integer.valueOf(list.get(0).toString())) > 0)
            return true;
        return false;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.dao.profile.IServicePointDAO#findByServicePointById(java.lang.Long)
     */
    public ServicePoint findByServicePointById(Long servicePointId) {

        ServicePoint servicePoint = (ServicePoint) find("FROM ServicePoint sp WHERE sp.id = ?",
                servicePointId);
        return servicePoint;
    }
    
    public ServicePoint findServPointByServPointId(String servPointId, String tenantId) {

        /*ServicePoint servicePoint = (ServicePoint) find("FROM ServicePoint es WHERE es.code = ?", servPointId);
        return servicePoint;*/
    	
    	Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery("FROM ServicePoint es WHERE es.code = :servPointId");
		query.setParameter("servPointId", servPointId);
		
		List<ServicePoint> servicePointList = query.list();
		ServicePoint servicePoint = null;
		if (servicePointList.size() > 0) {
			servicePoint = (ServicePoint) servicePointList.get(0);
		}

		session.flush();
		session.close();
		return servicePoint;
    }
    
    public ServicePoint findByServicePointById(Long servicePointId, String tenantId) {

        /*ServicePoint servicePoint = (ServicePoint) find("FROM ServicePoint sp WHERE sp.id = ?",servicePointId);
        return servicePoint;*/
    	
    	Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery("FROM ServicePoint sp WHERE sp.id = :servicePointId");
		query.setParameter("servicePointId", servicePointId);
		
		List<ServicePoint> servicePointList = query.list();
		ServicePoint servicePoint = null;
		if (servicePointList.size() > 0) {
			servicePoint = (ServicePoint) servicePointList.get(0);
		}

		session.flush();
		session.close();
		return servicePoint;
    }

    @Override
    public ServicePoint findServicePointByCode(String servicePointId, String tenantId) {        
        
        Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
        Query query = session.createQuery("FROM ServicePoint sp WHERE sp.code = :servicePointId");
        query.setParameter("servicePointId", servicePointId);
        
        List<ServicePoint> servicePointList = query.list();
        ServicePoint servicePoint = null;
        if (servicePointList.size() > 0) {
            servicePoint = (ServicePoint) servicePointList.get(0);
        }

        session.flush();
        session.close();
        return servicePoint;
        
        
    }
}
