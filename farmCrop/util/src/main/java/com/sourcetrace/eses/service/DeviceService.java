/*
 * DeviceService.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sourcetrace.eses.dao.IDeviceDAO;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.esesw.entity.profile.EnrollmentStation;
import com.sourcetrace.esesw.entity.profile.POS;

@Service
@Transactional
public class DeviceService implements IDeviceService {

    @Autowired
    /**
     * The device dao.
     */
    private IDeviceDAO deviceDAO;
    private String reportEntity;

    /**
     * The filter exclude properties.
     */
    private List<String> filterExcludeProperties;

    /**
     * Sets the filter exclude properties.
     *
     * @param filterExcludeProperties the new filter exclude properties
     */
    public void setFilterExcludeProperties(List<String> filterExcludeProperties) {

        this.filterExcludeProperties = filterExcludeProperties;
    }

    /**
     * Sets the report entity.
     *
     * @param reportEntity the new report entity
     */
    public void setReportEntity(String reportEntity) {

        this.reportEntity = reportEntity;
    }

    /**
     * Sets the device dao.
     *
     * @param deviceDAO the new device dao
     */
    public void setDeviceDAO(IDeviceDAO deviceDAO) {

        this.deviceDAO = deviceDAO;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IDeviceService#addDevice(com.ese.entity.profile .Device)
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void addDevice(Device device) {

        deviceDAO.save(device);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IDeviceService#editDevice(com.ese.entity.profile .Device)
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void editDevice(Device device) {

        if (device instanceof POS) {
            POS pos = (POS) device;
            POS aPOS = deviceDAO.findPOSByDeviceId(pos.getDeviceId());
            aPOS.setEnabled(pos.isEnabled());
            aPOS.setMode(pos.getMode());
            aPOS.setAgents(pos.getAgents());
            deviceDAO.update(aPOS);
        } else if (device instanceof EnrollmentStation) {
            EnrollmentStation es = (EnrollmentStation) device;
            EnrollmentStation aES = deviceDAO.findEnrollmentStationByDeviceId(es.getDeviceId());
            aES.setEnabled(es.isEnabled());
            aES.setAgents(es.getAgents());
            deviceDAO.update(aES);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IDeviceService#findDevice(java.lang.String)
     */
    public EnrollmentStation findEnrollmentStataionByDeviceId(String deviceId) {

        return deviceDAO.findEnrollmentStationByDeviceId(deviceId);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IDeviceService#listPOSDevices()
     */
    public List<POS> listPOSDevices() {

        return deviceDAO.listPOSDevices();
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IDeviceService#removeDevice(com.ese.entity.profile .Device)
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void removeDevice(Device device) {

        deviceDAO.delete(device);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IDeviceService#listEnrollmentDevices()
     */
    public List<EnrollmentStation> listEnrollmentStations() {

        return deviceDAO.listEnrollmentStataions();
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IDeviceService#findDevices(java.util.List)
     */
    public Set<EnrollmentStation> findEnrollmentStations(List<String> deviceIds) {

        Set<EnrollmentStation> devices = new LinkedHashSet<EnrollmentStation>();

        if (deviceIds != null) {
            for (String deviceId : deviceIds) {
                devices.add(deviceDAO.findEnrollmentStationByDeviceId(deviceId));
            }
        }

        return devices;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IDeviceService#listDevices()
     */
    public List<Device> listDevices() {

        return deviceDAO.listDevices();
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IDeviceService#listAllEnrollmentStations()
     */
    public List<EnrollmentStation> listAllEnrollmentStations() {

        return deviceDAO.listAllEnrollmentStataions();
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IDeviceService#listAllPOSDevices()
     */
    public List<POS> listAllPOSDevices() {

        return deviceDAO.listAllPOSDevices();
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IDeviceService#findPOSByPosId(java.lang.String)
     */
    public POS findPOSByDeviceId(String posId) {

        POS pos = deviceDAO.findPOSByDeviceId(posId);

        return pos;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IDeviceService#findPOS(long)
     */
    public POS findPOS(long id) {

        return deviceDAO.findPOS(id);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IDeviceService#findEnrollmentStataion(long)
     */
    public EnrollmentStation findEnrollmentStataion(long id) {

        return deviceDAO.findEnrollmentStation(id);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IDeviceService#findEnrollmentStationTransaction
     * (java.lang.String)
     */
    public boolean findEnrollmentStationTransaction(String deviceId) {

        return deviceDAO.findEnrollmentStationTransaction(deviceId);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IDeviceService#findPOSTransaction(java.lang.String )
     */
    public boolean findPOSTransaction(String deviceId) {

        return deviceDAO.findPOSTransaction(deviceId);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IDeviceService#isBranchHaveDevice(java.lang.String )
     */
    public boolean isBranchHaveDevice(String branchId) {

        return deviceDAO.isBranchHaveDevice(branchId);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IDeviceService#findEnrollmentStationBySerialNumber
     * (java.lang.String)
     */
    public Object findEnrollmentStationBySerialNumber(String serialNo) {

        return deviceDAO.findEnrollmentStationBySerialNumber(serialNo);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IDeviceService#isLocationHaveDevice(long)
     */
    public boolean isLocationHaveDevice(long id) {

        // TODO Auto-generated method stub
        return deviceDAO.isLocationHaveDevice(id);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IDeviceService#isLocationHaveEnrollmentStation (long)
     */
    public boolean isLocationHaveEnrollmentStation(long id) {

        // TODO Auto-generated method stub
        return deviceDAO.isLocationHaveEnrollmentStation(id);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IDeviceService#findDeviceById(java.lang.Long)
     */
    public Device findDeviceById(Long id) {

        return deviceDAO.findDeviceById(id);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IDeviceService#listDevices(java.lang.String)
     */
    public List<Device> listDevices(String deviceType) {

        return deviceDAO.listDevices(deviceType);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IDeviceService#updateDevice(com.ese.entity.profile.Device)
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateDevice(Device device) {

        deviceDAO.update(device);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IDeviceService#findDeviceByCode(java.lang.String)
     */
    public Device findDeviceByCode(String code) {

        return deviceDAO.findDeviceByCode(code);
    }

    /*                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
     * (non-Javadoc)
     * @see com.ese.service.profile.IDeviceService#findDeviceBySerialNumber(java.lang.String)
     */
    public Device findDeviceBySerialNumber(String serialNumber) {

        return deviceDAO.findDeviceBySerialNumber(serialNumber);
    }

    public List<Boolean> listDeviceStatus() {

        return deviceDAO.listDeviceStatus();
    }

    @Override
    public Device findUnRegisterDeviceById(Long id) {
        return deviceDAO.findUnRegisterDeviceById(id);
    }

    @Override
    public List<Boolean> listDeviceStatusBasedOnBranch(String branchIdValue) {
        return deviceDAO.listDeviceStatusBasedOnBranch(branchIdValue);
    }

    @Override
    public Integer findDeviceCount() {
        // TODO Auto-generated method stub
        return deviceDAO.findDeviceCount();
    }

    @Override
    public Integer findDeviceCountByMonth(Date sDate, Date eDate) {
        return deviceDAO.findDeviceCountByMonth(sDate, eDate);
    }
    
    public Device findDeviceBySerialNumber(String serialNumber, String tenantId) {

        return deviceDAO.findDeviceBySerialNumber(serialNumber, tenantId);
    }
}
