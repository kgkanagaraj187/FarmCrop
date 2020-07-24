/*
 * IServicePointService.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import java.util.List;

import com.sourcetrace.esesw.entity.profile.ServicePoint;
import com.sourcetrace.esesw.entity.profile.ServicePointType;

// TODO: Auto-generated Javadoc
/**
 * The Interface IServicePointService.
 * @author $Author: boopalan $
 * @version $Rev: 915 $ $Date: 2010-04-25 12:00:41 +0530 (Sun, 25 Apr 2010) $
 */
public interface IServicePointService {

    /**
     * List service points.
     * @return the list< service point>
     */
    public List<ServicePoint> listServicePoints();

    /**
     * List service point types.
     * @return the list< service point type>
     */
    public List<ServicePointType> listServicePointTypes();

    /**
     * Find service piont type by code.
     * @param code the code
     * @return the service point type
     */
    public ServicePointType findServicePiontTypeByCode(String code);

    /**
     * Adds the service point.
     * @param servicePoint the service point
     */
    public void addServicePoint(ServicePoint servicePoint);

    /**
     * Find service point.
     * @param id the id
     * @return the service point
     */
    public ServicePoint findServicePoint(long id);

    /**
     * Edits the service point.
     * @param servicePoint the service point
     */
    public void editServicePoint(ServicePoint servicePoint);

    /**
     * Removes the service point.
     * @param servicePointId the service point id
     */
    public void removeServicePoint(String servicePointId);

    /**
     * Find service point by serial number.
     * @param serialNumber the serial number
     * @return the service point
     */
    public ServicePoint findServicePointBySerialNumber(String serialNumber);

    /**
     * Find service point by service point id.
     * @param servicePointId the service point id
     * @return the service point
     */
    public ServicePoint findServicePointByServicePointId(String servicePointId);

    /**
     * Find by service point name.
     * @param servicePointName the service point name
     * @return the service point
     */
    public ServicePoint findByServicePointName(String servicePointName);

    /**
     * List service points by store.
     * @param store the store
     * @return the list< service point>
     */
    public List<ServicePoint> listServicePointsByStore(String store);

    /**
     * Find service point by name.
     * @param selectedServicePoint the selected service point
     * @return the service point
     */
    public ServicePoint findServicePointByName(String selectedServicePoint);

    /**
     * Find service point by code.
     * @param code the code
     * @return the service point
     */
    public ServicePoint findServicePointByCode(String code);

    /**
     * List service point code.
     * @return the list< service point>
     */
    public List<ServicePoint> listServicePointCode();

    /**
     * Removes the service point by object.
     * @param servicePoint the service point
     */
    public void removeServicePointByObject(ServicePoint servicePoint);

    /**
     * Find service point by serv point id.
     * @param servPointId the serv point id
     * @return the service point
     */
    public ServicePoint findServicePointByServPointId(String servPointId);

    /**
     * Checks if is agent warehouse mapping exists.
     * @param servicePointId the service point id
     * @return true, if is agent warehouse mapping exists
     */
    public boolean isAgentWarehouseMappingExists(long servicePointId);

    /**
     * Find by service point by id.
     * @param servicePointId the service point id
     * @return the service point
     */
    public ServicePoint findByServicePointById(Long servicePointId);
    
    public ServicePoint findServicePointByServPointId(String servPointId, String tenantId);
    
    public ServicePoint findByServicePointById(Long servicePointId, String tenantId);

    public ServicePoint findServicePointByCode(String servicePointId, String tenantId);
}
