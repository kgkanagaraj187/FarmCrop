/*
 * ServicePointService.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sourcetrace.esesw.entity.profile.ServicePoint;
import com.sourcetrace.esesw.entity.profile.ServicePointType;

// TODO: Auto-generated Javadoc
/**
 * The Class ServicePointService.
 * @author $Author: boopalan $
 * @version $Rev: 915 $ $Date: 2010-04-25 12:00:41 +0530 (Sun, 25 Apr 2010) $
 */
@Service
@Transactional
public class ServicePointService implements IServicePointService {

    /** The service point dao. */
	@Autowired
    private IServicePointDAO servicePointDAO;

    /**
     * Sets the service point dao.
     * @param servicePointDAO the new service point dao
     */
    public void setServicePointDAO(IServicePointDAO servicePointDAO) {

        this.servicePointDAO = servicePointDAO;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.profile.IServicePointService#listServicePoints()
     */
    public List<ServicePoint> listServicePoints() {

        return servicePointDAO.list();
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.profile.IServicePointService#listServicePointTypes()
     */
    public List<ServicePointType> listServicePointTypes() {

        return servicePointDAO.listServicePointTypes();
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.profile.IServicePointService#findServicePiontTypeByCode(java
     * .lang.String)
     */
    public ServicePointType findServicePiontTypeByCode(String code) {

        return servicePointDAO.findServicePiontTypeByCode(code);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.profile.IServicePointService#addServicePoint(com.sourcetrace
     * .esesw.entity.profile.ServicePoint)
     */
    public void addServicePoint(ServicePoint servicePoint) {

        servicePointDAO.save(servicePoint);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.profile.IServicePointService#editServicePoint(com.sourcetrace
     * .esesw.entity.profile.ServicePoint)
     */
    public void editServicePoint(ServicePoint servicePoint) {

        servicePointDAO.update(servicePoint);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.profile.IServicePointService#findServicePoint(long)
     */
    public ServicePoint findServicePoint(long id) {

        return servicePointDAO.find(id);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.profile.IServicePointService#findServicePointBySerialNumber
     * (java.lang.String)
     */
    public ServicePoint findServicePointBySerialNumber(String serialNumber) {

        return servicePointDAO.findBySerialNumber(serialNumber);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.profile.IServicePointService#findServicePointByServicePointId
     * (java.lang.String)
     */
    public ServicePoint findServicePointByServicePointId(String servicePointId) {

        return servicePointDAO.findByServicePointId(servicePointId);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.profile.IServicePointService#findByServicePointName(java.lang
     * .String)
     */
    public ServicePoint findByServicePointName(String servicePointName) {

        return servicePointDAO.findByServicePointName(servicePointName);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.profile.IServicePointService#removeServicePoint(java.lang.String
     * )
     */
    public void removeServicePoint(String servicePointId) {

        servicePointDAO.delete(findServicePointByServicePointId(servicePointId));
    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.esesw.service.profile.IServicePointService#
     * listServicePointsByStore(java.lang.String)
     */
    public List<ServicePoint> listServicePointsByStore(String store) {

        return servicePointDAO.listServicePointsByStore(store);
    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.esesw.service.profile.IServicePointService#
     * findServicePointByName(java.lang.String)
     */
    public ServicePoint findServicePointByName(String selectedServicePoint) {

        return servicePointDAO.findServicePointByName(selectedServicePoint);
    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.esesw.service.profile.IServicePointService#
     * findServicePointByCode(java.lang.String)
     */
    public ServicePoint findServicePointByCode(String code) {

        return servicePointDAO.findServicePointByCode(code);
    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.esesw.service.profile.IServicePointService# listServicePointCode()
     */
    public List<ServicePoint> listServicePointCode() {

        return servicePointDAO.listServicePointCode();
    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.esesw.service.profile.IServicePointService# removeServicePointByObject
     * (com.sourcetrace.esesw.entity.profile.ServicePoint)
     */
    public void removeServicePointByObject(ServicePoint servicePoint) {

        servicePointDAO.delete(servicePoint);
    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.esesw.service.profile.IServicePointService#
     * findServicePointByServPointId(java.lang.String)
     */
    public ServicePoint findServicePointByServPointId(String servPointId) {

        return servicePointDAO.findServPointByServPointId(servPointId);

    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.profile.IServicePointService#isAgentWarehouseMappingExists(
     * long)
     */
    public boolean isAgentWarehouseMappingExists(long servicePointId) {

        return servicePointDAO.isAgentWarehouseMappingExists(servicePointId);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.profile.IServicePointService#findByServicePointById(java.lang
     * .Long)
     */
    public ServicePoint findByServicePointById(Long servicePointId) {

        return servicePointDAO.findByServicePointById(servicePointId);
    }
    
    public ServicePoint findServicePointByServPointId(String servPointId, String tenantId) {

        return servicePointDAO.findServPointByServPointId(servPointId, tenantId);

    }
    
    public ServicePoint findByServicePointById(Long servicePointId, String tenantId) {

        return servicePointDAO.findByServicePointById(servicePointId, tenantId);
    }

    @Override
    public ServicePoint findServicePointByCode(String servicePointId, String tenantId) {

        return servicePointDAO.findServicePointByCode(servicePointId, tenantId);
    }
}
