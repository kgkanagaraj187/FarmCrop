/*
 * IDeviceService.java
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
import java.util.Set;

import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.esesw.entity.profile.EnrollmentStation;
import com.sourcetrace.esesw.entity.profile.POS;

public interface IDeviceService {

	  /**
     * List devices.
     * @return the list< device>
     */
    public List<Device> listDevices();

    /**
     * List all pos devices.
     * @return the list< po s>
     */
    public List<POS> listAllPOSDevices();

    /**
     * List all enrollment stations.
     * @return the list< enrollment station>
     */
    public List<EnrollmentStation> listAllEnrollmentStations();

    /**
     * List POS devices.
     * @return the list< device>
     */
    public List<POS> listPOSDevices();

    /**
     * List POS devices.
     * @return the list< device>
     */
    public List<EnrollmentStation> listEnrollmentStations();

    /**
     * Adds the device.
     * @param Device the device
     */
    public void addDevice(Device Device);

    /**
     * Edits the device.
     * @param Device the device
     */
    public void editDevice(Device Device);

    /**
     * Removes the device.
     * @param device the device
     */
    public void removeDevice(Device device);

    /**
     * Find device.
     * @param deviceId the device id
     * @return the device
     */
    public EnrollmentStation findEnrollmentStataionByDeviceId(String deviceId);

    /**
     * Find pos by device id.
     * @param posId the pos id
     * @return the pOS
     */
    public POS findPOSByDeviceId(String posId);

    /**
     * Find pos.
     * @param id the id
     * @return the pOS
     */
    public POS findPOS(long id);

    /**
     * Find device.
     * @param deviceId the device id
     * @return the device
     */
    public Set<EnrollmentStation> findEnrollmentStations(List<String> deviceId);

    /**
     * Find enrollment stataion.
     * @param id the id
     * @return the enrollment station
     */
    public EnrollmentStation findEnrollmentStataion(long id);

    /**
     * Find enrollment station transaction.
     * @param deviceId the device id
     * @return boolean
     */
    public boolean findEnrollmentStationTransaction(String deviceId);

    /**
     * Find pos transaction.
     * @param deviceId the device id
     * @return boolean
     */
    public boolean findPOSTransaction(String deviceId);

    /**
     * Checks if is branch have device.
     * @param branchId the branch id
     * @return true, if is branch have device
     */
    public boolean isBranchHaveDevice(String branchId);

    /**
     * Find enrollment station by serial number.
     * @param serialNo the serial no
     * @return the object
     */
    public Object findEnrollmentStationBySerialNumber(String serialNo);

    /**
     * Checks if is location have device.
     * @param id the id
     * @return true, if is location have device
     */
    public boolean isLocationHaveDevice(long id);

    /**
     * Checks if is location have enrollment station.
     * @param id the id
     * @return true, if is location have enrollment station
     */
    public boolean isLocationHaveEnrollmentStation(long id);

    public List<Device> listDevices(String deviceType);

    // public void addDevice(Device device);

    public Device findDeviceById(Long id);

    public void updateDevice(Device device);

    public Device findDeviceByCode(String code);

    public Device findDeviceBySerialNumber(String serialNumber);

    public List<Boolean> listDeviceStatus();
    
    public Device findUnRegisterDeviceById(Long id);

	public List<Boolean> listDeviceStatusBasedOnBranch(String branchIdValue);
	
	public Integer findDeviceCount();
	
	public Integer findDeviceCountByMonth(Date sDate,Date eDate);
	
	public Device findDeviceBySerialNumber(String serialNumber, String tenantId);
}
