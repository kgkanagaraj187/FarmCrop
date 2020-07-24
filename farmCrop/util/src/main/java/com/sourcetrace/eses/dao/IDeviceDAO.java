/*
 * IDeviceDAO.java
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

import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.esesw.entity.profile.EnrollmentStation;
import com.sourcetrace.esesw.entity.profile.POS;

public interface IDeviceDAO extends IESEDAO {

	 public static final String ENTITY = "entity";
	    public static final String DIR = "dir";
	    public static final String SORT_COLUMN = "sort";
	    public static final String START_INDEX = "start";
	    public static final String LIMIT = "limit";
	    public static final String PAGE = "page";
	    public static final String PROJ_GROUP = "groupProperty";
	    public static final String RECORD_COUNT = "records";
	    public static final String RECORDS = "rows";
	    public static final String PAGE_NUMBER = "pagenumber";
	    public static final String DESCENDING = "desc";
	    public static final String DELIMITER = ".";
	    public static final String DATE_COLUMN = "date";
	    public static final String FROM_DATE = "from";
	    public static final String TO_DATE = "to";
	    public static final String FILTER = "example";
	    public static final String EXCLUDE_PROP = "exclude";
	    public static final String PROJ_SUM = "sumProperty";
	    public static final String PROJ_MAX = "maxProperty";
	    public static final String PROJ_MIN = "minProperty";
	    public static final String PROJ_OTHERS = "otherProperties";
	    public static final String SEPARATOR = ",";

	    /**
	     * Find enrollment station by device id.
	     * @param deviceId the device id
	     * @return the enrollment station
	     */
	    public EnrollmentStation findEnrollmentStationByDeviceId(String deviceId);

	    /**
	     * Find pos by serial number.
	     * @param serialNumber the serial number
	     * @return the pOS
	     */
	    public POS findPOSBySerialNumber(String serialNumber);

	    /**
	     * Find enrollment station by serial number.
	     * @param serialNumber the serial number
	     * @return the enrollment station
	     */
	    public EnrollmentStation findEnrollmentStationBySerialNumber(String serialNumber);

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
	     * List pos devices.
	     * @return the list< po s>
	     */
	    public List<POS> listPOSDevices();

	    /**
	     * List enrollment stataions.
	     * @return the list< enrollment station>
	     */
	    public List<EnrollmentStation> listEnrollmentStataions();

	    /**
	     * List all enrollment stataions.
	     * @return the list< enrollment station>
	     */
	    public List<EnrollmentStation> listAllEnrollmentStataions();

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
	     * Find enrollment station.
	     * @param id the id
	     * @return the enrollment station
	     */
	    public EnrollmentStation findEnrollmentStation(long id);

	    /**
	     * Find enrollment station transaction.
	     * @param deviceId the device id
	     * @return true, if successful
	     */
	    public boolean findEnrollmentStationTransaction(String deviceId);

	    /**
	     * Find pos transaction.
	     * @param deviceId the device id
	     * @return true, if successful
	     */
	    public boolean findPOSTransaction(String deviceId);

	    /**
	     * Checks if is branch have device.
	     * @param branchId the branch id
	     * @return true, if is branch have device
	     */
	    public boolean isBranchHaveDevice(String branchId);

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

	    /**
	     * List devices.
	     * @param deviceType the device type
	     * @return the list< device>
	     */
	    public List<Device> listDevices(String deviceType);

	    /**
	     * Find device by id.
	     * @param id the id
	     * @return the device
	     */
	    public Device findDeviceById(Long id);

	    /**
	     * Find device by code.
	     * @param code the code
	     * @return the device
	     */
	    public Device findDeviceByCode(String code);

	    // public void update(Object object);

	    /**
	     * Find device by serial number.
	     * @param serialNumber the serial number
	     * @return the device
	     */
	    public Device findDeviceBySerialNumber(String serialNumber);

	    public void updateMsgNo(String serialNo, String msgNo);

	    public List<Boolean> listDeviceStatus();
	    
	    public Device findUnRegisterDeviceById(Long id);

		public List<Boolean> listDeviceStatusBasedOnBranch(String branchIdValue);
		
		public Integer findDeviceCount();
		
		public Integer findDeviceCountByMonth(Date sDate,Date eDate);
		
		public Device findDeviceBySerialNumber(String serialNumber, String tenantId);
}
