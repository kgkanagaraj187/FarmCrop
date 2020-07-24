package com.sourcetrace.esesw.view.general.validator;

import java.util.LinkedHashMap;
import java.util.Map;

import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.IDeviceDAO;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.ValidationUtil;

public class ProfileDeviceValidator implements IValidator {

	private IDeviceDAO deviceDAO;

	/**
	 * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
	 */
	@Override
	public Map<String, String> validate(Object object) {

		ClassValidator<Device> deviceValidator = new ClassValidator<Device>(Device.class);
		Device device = (Device) object;

		Map<String, String> errorCodes = new LinkedHashMap<String, String>();

		InvalidValue[] values = deviceValidator.getInvalidValues(device, "code");
		for (InvalidValue value : values) {
			errorCodes.put(value.getPropertyName(), value.getMessage());
		}
		
		/*if (values == null || values.length == 0) {
			Device eDevice = deviceDAO.findDeviceByCode(device.getCode());
			if (eDevice != null && device.getId() != eDevice.getId()) {
				errorCodes.put("deviceCode", "exist.deviceCode");
			}
		}*/

		// InvalidValue[] values = deviceValidator.getInvalidValues(device,
		// "serialNumber");
		for (InvalidValue value : values) {
			errorCodes.put(value.getPropertyName(), value.getMessage());
		}

		values = deviceValidator.getInvalidValues(device, "name");
		for (InvalidValue value : values) {
			errorCodes.put(value.getPropertyName(), value.getMessage());
		}

		if (values == null || values.length == 0) {
			Device eDevice = deviceDAO.findDeviceBySerialNumber(device.getSerialNumber());
			if (eDevice != null && device.getId() != eDevice.getId()) {
				errorCodes.put("serialNumber", "exist.deviceSerialNumber");
			}
		}

		values = deviceValidator.getInvalidValues(device, "deviceType");
		for (InvalidValue value : values) {
			errorCodes.put(value.getPropertyName(), value.getMessage());
		}

		if (!StringUtil.isEmpty(device.getName())) {
			if (!ValidationUtil.isPatternMaches(device.getName(), ValidationUtil.ALPHANUMERIC_PATTERN)) {
				errorCodes.put("pattern.deviceName", "pattern.name");
			}
		} else {
			errorCodes.put("empty.deviceName", "empty.deviceName");
		}

		if (!StringUtil.isEmpty(device.getSerialNumber())) {
			if (!ValidationUtil.isPatternMaches(device.getSerialNumber(), ValidationUtil.ALPHANUMERIC_PATTERN)) {
				errorCodes.put("pattern.serialNumber", "pattern.serialNumber");
			}
		} else {
			if(!StringUtil.isEmpty(device.getDeviceAddress()) && device.getDeviceAddress().equalsIgnoreCase("0") )
				errorCodes.put("empty.serialNumber", "empty.serialNumber");
		}
		
		if (!StringUtil.isEmpty(device.getMacAddress())) {
			if (!ValidationUtil.isPatternMaches(device.getMacAddress(), ValidationUtil.MAC_ADDRESS_REGX)) {
				errorCodes.put("pattern.macAddress", "pattern.macAddress");
			}
		} else {
			if(!StringUtil.isEmpty(device.getDeviceAddress()) && device.getDeviceAddress().equalsIgnoreCase("1") )
			errorCodes.put("empty.macAddress", "empty.macAddress");
		}
		return errorCodes;
	}

	/**
	 * Sets the device dao.
	 * 
	 * @param deviceDAO
	 *            the new device dao
	 */
	public void setDeviceDAO(IDeviceDAO deviceDAO) {

		this.deviceDAO = deviceDAO;
	}

	/**
	 * Gets the device dao.
	 * 
	 * @return the device dao
	 */
	public IDeviceDAO getDeviceDAO() {

		return deviceDAO;
	}

}
