package com.sourcetrace.eses.adapter.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.activation.DataHandler;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.entity.DynamicFieldConfig;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.OfflineTransactionException;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.DynamicImageData;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicData;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicFieldsValue;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;

@Component
public class ShgroupRecord implements ITxnAdapter {
	private int mode = 0;
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private IClientService clientService;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private IAgentService agentService;
	private String id;
	private String txnType;

	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {
		Collection dynamicFields = (reqData.containsKey(TransactionProperties.DYNAMIC_FIELD))
				? (Collection) reqData.get(TransactionProperties.DYNAMIC_FIELD) : null;

		Collection dynamicList = (reqData.containsKey(TransactionProperties.DYNAMIC_FIELD_LIST))
				? (Collection) reqData.get(TransactionProperties.DYNAMIC_FIELD_LIST) : null;

		String farmerId = (reqData.containsKey(TxnEnrollmentProperties.FARMER_ID))
				? (String) reqData.get(TxnEnrollmentProperties.FARMER_ID) : "";

		String txnUniqueId = (reqData.containsKey(TxnEnrollmentProperties.TXN_UNIQUE_ID))
				? (String) reqData.get(TxnEnrollmentProperties.TXN_UNIQUE_ID) : "";

		String txnTypeIdMaster = (reqData.containsKey(TxnEnrollmentProperties.TXN_TYPE_ID_MASTER))
				? (String) reqData.get(TxnEnrollmentProperties.TXN_TYPE_ID_MASTER) : "";

		String latitude = (String) reqData.get(TxnEnrollmentProperties.LATITUDE);
		String longitude = (String) reqData.get(TxnEnrollmentProperties.LONGITUDE);
		String txnDate = (String) reqData.get(TxnEnrollmentProperties.TXN_DATE);

		Collection photoList = (reqData.containsKey(TransactionProperties.DYNAMIC_IMAGE_LIST))
				? (Collection) reqData.get(TransactionProperties.DYNAMIC_IMAGE_LIST) : null;

		Head head = (Head) reqData.get(TransactionProperties.HEAD);

		FarmerDynamicData farmerDynamicData = new FarmerDynamicData();

		List<DynamicFieldConfig> dynmaicFieldsConfigList = new ArrayList<>();

		farmerService.findDynamicFieldsBySectionId(Farmer.TABLE_ID).stream().forEach(section -> {
			dynmaicFieldsConfigList.addAll(section.getDynamicFieldConfigs());
		});

		Map<String, DynamicFieldConfig> fieldConfigMap = dynmaicFieldsConfigList.stream()
				.collect(Collectors.toMap(DynamicFieldConfig::getCode, obj -> obj));

		try {
			validate(farmerId, SwitchErrorCodes.EMPTY_FARMER_ID);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// String agentId = null;

		if (head.getTxnType().equalsIgnoreCase("381")) {
			Warehouse group = locationService.findSamithiByCode(farmerId);
			setId(String.valueOf(group.getId()));
		} else if (head.getTxnType().equalsIgnoreCase("384") || head.getTxnType().equalsIgnoreCase("391")) {
			Farm farm = farmerService.findFarmByCode(farmerId);
			setId(String.valueOf(farm.getId()));
		}

		List<Object[]> fdfvList = clientService.listMaxTypeByRefId(farmerId);
		Map<String, String> fieldTypeMap = fdfvList.stream()
				.collect(Collectors.toMap(obj -> String.valueOf(obj[0]), obj -> String.valueOf(obj[1])));

		List<FarmerDynamicFieldsValue> farmerDynamicFieldsValueList = new LinkedList<>();

		if (!ObjectUtil.isEmpty(dynamicFields)) {
			List<com.sourcetrace.eses.txn.schema.Object> fieldObjects = dynamicFields.getObject();
			for (com.sourcetrace.eses.txn.schema.Object fieldObject : fieldObjects) {
				List<Data> dynamicData = fieldObject.getData();
				FarmerDynamicFieldsValue farmerDynamicFieldsValue = new FarmerDynamicFieldsValue();
				for (Data data : dynamicData) {
					String key = data.getKey();
					String value = data.getValue();
					if (TxnEnrollmentProperties.FIELD_ID.equalsIgnoreCase(key)) {
						farmerDynamicFieldsValue.setFieldName(value);
						setTxnType(fieldConfigMap.get(value).getDynamicSectionConfig().getmTxnType());
					} else if (TxnEnrollmentProperties.FIELD_VAL.equalsIgnoreCase(key)) {
						farmerDynamicFieldsValue.setFieldValue(value);
					} else if (TxnEnrollmentProperties.COMPONENT_TYPE.equalsIgnoreCase(key)) {
						farmerDynamicFieldsValue.setComponentType(value);
					}
				}
				farmerDynamicFieldsValue.setTxnType(getTxnType());
				farmerDynamicFieldsValue.setReferenceId(getId());
				farmerDynamicFieldsValueList.add(farmerDynamicFieldsValue);
			}
		}

		if (!ObjectUtil.isEmpty(dynamicList)) {
			List<com.sourcetrace.eses.txn.schema.Object> fieldObjects = dynamicList.getObject();
			for (com.sourcetrace.eses.txn.schema.Object fieldObject : fieldObjects) {
				List<Data> dynamicData = fieldObject.getData();
				FarmerDynamicFieldsValue farmerDynamicFieldsValue = new FarmerDynamicFieldsValue();
				for (Data data : dynamicData) {
					String key = data.getKey();
					String value = data.getValue();
					if (TxnEnrollmentProperties.FIELD_ID.equalsIgnoreCase(key)) {
						farmerDynamicFieldsValue.setFieldName(value);
					} else if (TxnEnrollmentProperties.FIELD_VAL.equalsIgnoreCase(key)) {
						farmerDynamicFieldsValue.setFieldValue(value);
					} else if (TxnEnrollmentProperties.COMPONENT_TYPE.equalsIgnoreCase(key)) {
						farmerDynamicFieldsValue.setComponentType(value);
					} else if (TxnEnrollmentProperties.TXN_TYPE_ID.equalsIgnoreCase(key)) {
						farmerDynamicFieldsValue.setTxnType(txnTypeIdMaster);
					} else if (TxnEnrollmentProperties.LIST_ITRATION.equalsIgnoreCase(key)) {
						String iterateCount = fieldTypeMap.get(farmerDynamicFieldsValue.getFieldName());
						Integer type = null;
						if (!StringUtil.isEmpty(iterateCount) && StringUtil.isInteger(iterateCount)) {
							type = Integer.valueOf(iterateCount) + Integer.valueOf(value);
						} else {
							type = Integer.valueOf(value);
						}
						farmerDynamicFieldsValue.setTypez(type);
					}
				}
				farmerDynamicFieldsValue.setReferenceId(getId());
				// farmerDynamicFieldsValue.setTxnUniqueId(Long.valueOf(txnUniqueId));
				farmerDynamicFieldsValue.setCreatedDate(new Date());
				farmerDynamicFieldsValue.setCreatedUser(head.getAgentId());
				farmerDynamicFieldsValueList.add(farmerDynamicFieldsValue);
			}
		}

		if (head.getTxnType().equalsIgnoreCase("384") || head.getTxnType().equalsIgnoreCase("391")) {

			Set<FarmerDynamicFieldsValue> fdfv = new HashSet<FarmerDynamicFieldsValue>();
			farmerDynamicData.setTxnUniqueId(Long.valueOf(txnUniqueId));
			farmerDynamicData.setReferenceId(getId());
			farmerDynamicData.setTxnType(head.getTxnType());

			Date txnDateVal = null;
			if (!StringUtil.isEmpty(txnDate)) {
				txnDateVal = DateUtil.convertStringToDate(txnDate, DateUtil.TXN_DATE_TIME);
				farmerDynamicData.setDate(txnDateVal);
			}
			farmerDynamicData.setLatitude(StringUtil.isEmpty(latitude) ? null : latitude);
			farmerDynamicData.setLongitude(StringUtil.isEmpty(longitude) ? null : longitude);
			farmerDynamicData.setCreatedDate(DateUtil.convertStringToDate(head.getTxnTime(), DateUtil.TXN_DATE_TIME));
			// Agent tmpAgent = agentService.findAgentByAgentId(agentId);
			farmerDynamicData.setCreatedUser(head.getAgentId());
			// farmerDynamicData.setFarmerDynamicFieldsValues(new
			// HashSet<FarmerDynamicData>());
			fdfv.addAll(farmerDynamicFieldsValueList);
			// farmerDynamicData.getFarmerDynamicFieldsValues().addAll(fdfv);

			farmerDynamicData.setFarmerDynamicFieldsValues(fdfv);

			farmerService.save(farmerDynamicData);

		} else {
			clientService.saveFarmerDynmaicList(farmerDynamicFieldsValueList);
		}

		farmerService.updateDynamicFarmerFieldComponentType();

		Map<String, FarmerDynamicFieldsValue> farmerDynmaicFieldValuesList = new HashMap<>();
		if (!ObjectUtil.isListEmpty(farmerDynamicFieldsValueList)) {
			farmerDynamicFieldsValueList.stream().forEach(obj -> {
				if (!farmerDynmaicFieldValuesList.containsKey(obj.getFieldName())) {
					farmerDynmaicFieldValuesList.put(obj.getFieldName(), obj);
				}
			});
		}

		if (!ObjectUtil.isEmpty(photoList)) {
			List<com.sourcetrace.eses.txn.schema.Object> fieldObjects = photoList.getObject();
			for (com.sourcetrace.eses.txn.schema.Object fieldObject : fieldObjects) {
				List<Data> dynamicData = fieldObject.getData();
				DynamicImageData dynamicImageData = new DynamicImageData();
				FarmerDynamicFieldsValue farmerDynamicFieldsValue = null;
				for (Data data : dynamicData) {
					String key = data.getKey();
					String value = data.getValue();
					if (TxnEnrollmentProperties.FIELD_ID.equalsIgnoreCase(key)) {
						if (fieldConfigMap.containsKey(value)) {
							if (!ObjectUtil.isEmpty(fieldConfigMap.get(value).getParentDepen())) {
								String parentKey = fieldConfigMap.get(value).getParentDepen().getCode();
								farmerDynamicFieldsValue = farmerDynmaicFieldValuesList.get(parentKey);
								dynamicImageData.setFarmerDynamicFieldsValue(farmerDynamicFieldsValue);
							} else {
								farmerDynamicFieldsValue = new FarmerDynamicFieldsValue();
								farmerDynamicFieldsValue.setComponentType(String
										.valueOf(DynamicFieldConfig.COMPONENT_TYPES.PHOTO_CERTIFICATION.ordinal()));
								farmerDynamicFieldsValue.setCreatedDate(new Date());
								farmerDynamicFieldsValue.setReferenceId(getId());
								farmerDynamicFieldsValue.setCreatedUser(head.getAgentId());
								farmerDynamicFieldsValue.setFieldName(value);
								farmerDynamicFieldsValue.setFieldValue("");
								farmerDynamicFieldsValue.setTxnType(fieldConfigMap.get(value).getDynamicSectionConfig().getmTxnType());
								farmerService.save(farmerDynamicFieldsValue);
								dynamicImageData.setFarmerDynamicFieldsValue(farmerDynamicFieldsValue);
							}
						} else {
							break;
						}
					} else if (TxnEnrollmentProperties.F_PHOTO.equalsIgnoreCase(key)) {
						DataHandler photo = data.getBinaryValue();
						byte[] imageContent = null;
						try {
							if (photo != null && photo.getInputStream().available() > 0) {
								imageContent = IOUtils.toByteArray(photo.getInputStream());
								dynamicImageData.setImage(imageContent);
							}
						} catch (Exception e) {
							e.printStackTrace();
							throw new SwitchException(ITxnErrorCodes.ERR0R_WHILE_PROCESSING);
						}

					} else if (TxnEnrollmentProperties.FARMER_PHOTO_CAPTURE_TIME.equalsIgnoreCase(key)) {
						if (!StringUtil.isEmpty(value) && !value.equals("0")) {
							try {
								Date photoCaptureDate = DateUtil.convertStringToDate(value, DateUtil.TXN_TIME_FORMAT);
								dynamicImageData.setPhotoCaptureTime(photoCaptureDate);
							} catch (Exception e) {
								e.printStackTrace();
								throw new SwitchException(ITxnErrorCodes.DATE_CONVERSION_ERROR);
							}
						}
					} else if (TxnEnrollmentProperties.PHOTO_LATITUDE.equalsIgnoreCase(key))
						dynamicImageData.setLatitude(value);

					else if (TxnEnrollmentProperties.PHOTO_LONGITUDE.equalsIgnoreCase(key))
						dynamicImageData.setLongitude(value);

				}
				Set<DynamicImageData> imageDataSet = new HashSet<>();
				imageDataSet.add(dynamicImageData);
				farmerDynamicFieldsValue.setDymamicImageData(imageDataSet);
				farmerService.update(farmerDynamicFieldsValue);
				farmerService.updateDynmaicImage(farmerDynamicFieldsValue, String.valueOf(farmerDynamicData.getId()));
			}
		}

		return new HashMap();
	}

	@Override
	public Map<?, ?> processVoid(Map<?, ?> reqData) {
		// TODO Auto-generated method stub
		return null;
	}

	private void validate(Object object, String errorCode) throws OfflineTransactionException, IOException {

		if (ObjectUtil.isEmpty(object) || ((object instanceof String) && (StringUtil.isEmpty(String.valueOf(object)))))
			throwError(errorCode);
	}

	/**
	 * Throw error.
	 * 
	 * @param errorCode
	 *            the error code
	 * @throws OfflineTransactionException
	 *             the offline transaction exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void throwError(String errorCode) throws OfflineTransactionException, IOException {

		if (mode == ESETxn.ONLINE_MODE)
			throw new SwitchException(errorCode);
		else
			throw new OfflineTransactionException(errorCode);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

}
