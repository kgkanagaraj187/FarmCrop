package com.sourcetrace.eses.service;

import java.util.List;

import com.sourcetrace.eses.dao.IESEDAO;
import com.sourcetrace.esesw.entity.profile.ServicePoint;
import com.sourcetrace.esesw.entity.profile.ServicePointType;

public interface IServicePointDAO extends IESEDAO
{
	 /**
     * Find.
     * @param id the id
     * @return the service point
     */
    public ServicePoint find(long id);

    /**
     * Find by serial number.
     * @param serialNumber the serial number
     * @return the service point
     */
    public ServicePoint findBySerialNumber(String serialNumber);

    /**
     * Find by service point id.
     * @param servicePointId the service point id
     * @return the service point
     */
    public ServicePoint findByServicePointId(String servicePointId);

    /**
     * Find by service point name.
     * @param servicePointName the service point name
     * @return the service point
     */
    public ServicePoint findByServicePointName(String servicePointName);

    /**
     * List.
     * @return the list< service point>
     */
    public List<ServicePoint> list();

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
     * Checks if is city mappingexist.
     * @param id the id
     * @return true, if is city mappingexist
     */
    public boolean isCityMappingexist(long id);

    /**
     * Find serv point by serv point id.
     * @param servPointId the serv point id
     * @return the service point
     */
    public ServicePoint findServPointByServPointId(String servPointId);

    /**
     * Checks if is agent warehouse mapping exists.
     * @param servicePointId the service point id
     * @return true, if is agent warehouse mapping exists
     */
    public boolean isAgentWarehouseMappingExists(long servicePointId);

    public ServicePoint findByServicePointById(Long servicePointId);
    
    public ServicePoint findServPointByServPointId(String servPointId, String tenantId);
    
    public ServicePoint findByServicePointById(Long servicePointId, String tenantId);
    public ServicePoint findServicePointByCode(String servicePointId, String tenantId);
}
