package com.sourcetrace.eses.adapter.core;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.activation.DataHandler;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.entity.DynamicFeildMenuConfig;
import com.sourcetrace.eses.entity.DynamicFieldConfig;
import com.sourcetrace.eses.entity.DynamicFieldScoreMap;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.DynamicImageData;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicData;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicFieldsValue;

@Component
public class DynamicFollowUpAdapter implements ITxnAdapter {

	@Autowired
	private IFarmerService farmerService;

	private FarmerDynamicData farmerDynamicData = new FarmerDynamicData();

	boolean hit = false;
	@Autowired
	public IUniqueIDGenerator idServoce;

	@Autowired
	public ICatalogueService catService;
	FarmerDynamicFieldsValue farmerDynamicFieldsValue = new FarmerDynamicFieldsValue();
	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {

		String txmId = (String) reqData.get(TxnEnrollmentProperties.TXN_UNIQUE_ID);
		LinkedHashMap<String, DynamicFieldConfig> fieldConfigMap = new LinkedHashMap<>();
		Map<String, Integer> orderMap = new HashMap<>();
		Head head = (Head) reqData.get(TransactionProperties.HEAD);
		Collection dynamicFields = (reqData.containsKey(TransactionProperties.DYNAMIC_FIELD))
				? (Collection) reqData.get(TransactionProperties.DYNAMIC_FIELD) : null;

		Collection photoList = (reqData.containsKey(TransactionProperties.DYNAMIC_IMAGE_LIST))
				? (Collection) reqData.get(TransactionProperties.DYNAMIC_IMAGE_LIST) : null;
		String farmerId = (reqData.containsKey(TxnEnrollmentProperties.FARMER_ID))
				? (String) reqData.get(TxnEnrollmentProperties.FARMER_ID) : "";
		String txnType = (reqData.containsKey(TxnEnrollmentProperties.DYNAMIC_TXN_ID))
				? (String) reqData.get(TxnEnrollmentProperties.DYNAMIC_TXN_ID) : "";

		farmerDynamicData = farmerService.findFarmerDynamicData(txnType.split("-")[1].toString());
		Map<String, String> ActMap = new HashMap<>();
		Map<String, List<FarmerDynamicFieldsValue>> fdMap = new HashMap<>();
		if (!ObjectUtil.isEmpty(farmerDynamicData)) {
			fdMap = farmerDynamicData.getFarmerDynamicFieldsValues().stream().filter(u -> u.getTypez() == null)
					.collect(Collectors.groupingBy(FarmerDynamicFieldsValue::getFieldName));

			DynamicFeildMenuConfig dm = farmerService.findDynamicMenusByMType(txnType.split("-")[0].toString()).get(0);

			if (dm.getIsScore() != null && (dm.getIsScore() == 1 || dm.getIsScore() == 2 || dm.getIsScore() == 3 )) {
				farmerDynamicData.setIsScore(dm.getIsScore());
				farmerDynamicData.setScoreValue(new HashMap<>());
			}
			dm.getDynamicFieldConfigs().stream().forEach(section -> {
				section.getField().setfOrder(section.getOrder());
				fieldConfigMap.put(section.getField().getCode(), section.getField());
				orderMap.put(section.getField().getCode(), section.getOrder());
				if (section.getDynamicFieldScoreMap() != null && !section.getDynamicFieldScoreMap().isEmpty()) {
					farmerDynamicData.getScoreValue().put(section.getField().getCode(), section
							.getDynamicFieldScoreMap().stream()
							.collect(Collectors.toMap(DynamicFieldScoreMap::getCatalogueCode, u -> String
									.valueOf(String.valueOf(u.getScore()) + "~" + String.valueOf(u.getPercentage())))));
				}
				if (section.getField().getFollowUp() == 1 || section.getField().getFollowUp() == 2) {
					ActMap.put(section.getField().getCode(), section.getField().getParentActField());
				}
			});
			if (!ObjectUtil.isEmpty(dynamicFields)) {
				List<com.sourcetrace.eses.txn.schema.Object> fieldObjects = dynamicFields.getObject();
				for (com.sourcetrace.eses.txn.schema.Object fieldObject : fieldObjects) {
					List<Data> dynamicData = fieldObject.getData();
					 farmerDynamicFieldsValue = new FarmerDynamicFieldsValue();

					for (Data data : dynamicData) {
						String key = data.getKey();
						String value = data.getValue();
						if (TxnEnrollmentProperties.FIELD_ID.equalsIgnoreCase(key)) {
							if (fdMap.containsKey(fieldConfigMap.get(value).getParentActField())
									&& !ObjectUtil.isEmpty(farmerDynamicData)) {
								FarmerDynamicFieldsValue fd = fdMap.get(fieldConfigMap.get(value).getParentActField()).get(0);
								if(fd.getFollowUps()!=null && !fd.getFollowUps().isEmpty()){
								farmerDynamicFieldsValue = 	fd.getFollowUps().stream().anyMatch(u -> u.getFieldName().equals(value)) ? fd.getFollowUps().stream().filter(u -> u.getFieldName().equals(value)).findFirst().get() : new FarmerDynamicFieldsValue(); 
								fd.getFollowUps().removeIf(u -> u.getFieldName().equals(value));
								}
							} 
							farmerDynamicFieldsValue.setFieldName(value);
							
						} else if (TxnEnrollmentProperties.FIELD_VAL.equalsIgnoreCase(key)) {
							if (fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getIsOther() == 1) {
								if (Integer.valueOf(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName())
										.getAccessType()) == DynamicFieldConfig.ACCESS_TYPE.CATALOGUE_TYPE.ordinal()) {
									if (farmerDynamicFieldsValue.getFieldValue().contains(",")) {
										Arrays.asList(farmerDynamicFieldsValue.getFieldValue().split(",")).stream()
												.forEach(u -> {
													FarmCatalogue fm = catService.findCatalogueByCode(u);
													if (fm == null) {
														fm = new FarmCatalogue();
														fm.setCode(idServoce.getCatalogueIdSeq());
														fm.setName(u);
														fm.setRevisionNo(DateUtil.getRevisionNumber());
														fm.setStatus("1");
														fm.setTypez(Integer.valueOf(fieldConfigMap
																.get(farmerDynamicFieldsValue.getFieldName())
																.getCatalogueType()));
														catService.addCatalogue(fm);
														farmerDynamicFieldsValue.setFieldValue(farmerDynamicFieldsValue
																.getFieldValue().replaceAll(u, fm.getCode()));
													}

												});
									} else {
										FarmCatalogue fm = catService
												.findCatalogueByCode(farmerDynamicFieldsValue.getFieldValue());
										if (fm == null) {
											fm = new FarmCatalogue();
											fm.setCode(idServoce.getCatalogueIdSeq());
											fm.setName(farmerDynamicFieldsValue.getFieldValue());
											fm.setRevisionNo(DateUtil.getRevisionNumber());
											fm.setStatus("1");
											fm.setTypez(Integer.valueOf(fieldConfigMap
													.get(farmerDynamicFieldsValue.getFieldName()).getCatalogueType()));
											catService.addCatalogue(fm);
										}
										farmerDynamicFieldsValue.setFieldValue(fm.getCode());
									}
								}
							}

							farmerDynamicFieldsValue.setFieldValue(value);
						} else if (TxnEnrollmentProperties.COMPONENT_TYPE.equalsIgnoreCase(key)) {
							farmerDynamicFieldsValue.setComponentType(value);
						}
					}

					if (fdMap.containsKey(farmerDynamicFieldsValue.getFieldName())
							&& !ObjectUtil.isEmpty(farmerDynamicData)) {
						FarmerDynamicFieldsValue fd = fdMap.get(farmerDynamicFieldsValue.getFieldName()).get(0);
						farmerDynamicData.getFarmerDynamicFieldsValues()
								.remove(fdMap.get(farmerDynamicFieldsValue.getFieldName()));
						fd.setFieldValue(farmerDynamicFieldsValue.getFieldValue());
						farmerDynamicData.getFarmerDynamicFieldsValues().add(fd);
						fdMap.put(farmerDynamicFieldsValue.getFieldName(), Arrays.asList(fd));

					} else {
						
						farmerDynamicFieldsValue.setTxnType(dm.getTxnType());
						farmerDynamicFieldsValue.setReferenceId(farmerDynamicData.getReferenceId());
						farmerDynamicFieldsValue.setCreatedDate(new Date());
						farmerDynamicFieldsValue.setCreatedUser(head.getAgentId());
						farmerDynamicFieldsValue.setTxnUniqueId(Long.valueOf(farmerDynamicData.getTxnUniqueId()));
						farmerDynamicFieldsValue
								.setAccessType(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
										? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getAccessType()
										: 0);
						farmerDynamicFieldsValue
								.setGrade(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
										? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getGrade()
										: null);
						farmerDynamicFieldsValue.setListMethod(
								fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null && fieldConfigMap
										.get(farmerDynamicFieldsValue.getFieldName()).getCatalogueType() != null
												? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName())
														.getCatalogueType()
												: "");
						farmerDynamicFieldsValue.setParentId(fieldConfigMap
								.get(farmerDynamicFieldsValue.getFieldName()) != null
								&& fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getReferenceId() != null
										? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getReferenceId()
										: 0);

						farmerDynamicFieldsValue
								.setValidationType(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
										? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getValidation()
										: "0");
						// farmerDynamicFieldsValue.setFarmerDynamicData(farmerDynamicData);
						farmerDynamicFieldsValue
								.setIsMobileAvail(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
										? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getIsMobileAvail()
										: "0");

						farmerDynamicFieldsValue
								.setIsMobileAvail(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
										? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getIsMobileAvail()
										: "0");

						farmerDynamicFieldsValue
								.setAccessType(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
										? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getAccessType()
										: 0);

						farmerDynamicFieldsValue.setListMethod(
								fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null && fieldConfigMap
										.get(farmerDynamicFieldsValue.getFieldName()).getCatalogueType() != null
												? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName())
														.getCatalogueType()
												: "");
						farmerDynamicFieldsValue.setParentId(fieldConfigMap
								.get(farmerDynamicFieldsValue.getFieldName()) != null
								&& fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getReferenceId() != null
										? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getReferenceId()
										: 0);
						farmerDynamicFieldsValue.setOrder(orderMap.get(farmerDynamicFieldsValue.getFieldName()));
						farmerDynamicFieldsValue
								.setFollowUp(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null

										? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getFollowUp()
										: 0);
						farmerDynamicFieldsValue
								.setGrade(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
										? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getGrade()
										: null);
						farmerDynamicFieldsValue.setParentActField(
								fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null && fieldConfigMap
										.get(farmerDynamicFieldsValue.getFieldName()).getParentActField() != null
												? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName())
														.getParentActField()
												: null);
						farmerDynamicFieldsValue.setParentActKey(fieldConfigMap
								.get(farmerDynamicFieldsValue.getFieldName()) != null
								&& fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getParentActKey() != null
										? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getParentActKey()
										: null);
						fdMap.put(farmerDynamicFieldsValue.getFieldName(), Arrays.asList(farmerDynamicFieldsValue));
						if (farmerDynamicFieldsValue.getParentActField() != null) {
							if (farmerDynamicFieldsValue.getFollowUp() == 4
									&& fdMap.containsKey(farmerDynamicFieldsValue.getParentActField())) {
								FarmerDynamicFieldsValue ParentACt = fdMap
										.get(farmerDynamicFieldsValue.getParentActField()).get(0);
								SortedSet<FarmerDynamicFieldsValue> foolowUp = new TreeSet<>();
								if (!ParentACt.getFollowUps().isEmpty()) {
									foolowUp = ParentACt.getFollowUps();
								}
								farmerDynamicFieldsValue.setFollowUpParent(ParentACt);
								foolowUp.add(farmerDynamicFieldsValue);
								ParentACt.setFollowUps(foolowUp);
								fdMap.put(farmerDynamicFieldsValue.getParentActField(), Arrays.asList(ParentACt));

							}

						}

					}

				}
			}

			if (!ObjectUtil.isEmpty(photoList)) {

				List<com.sourcetrace.eses.txn.schema.Object> fieldObjects = photoList.getObject();
				for (com.sourcetrace.eses.txn.schema.Object fieldObject : fieldObjects) {
					List<Data> dynamicData = fieldObject.getData();
					DynamicImageData dynamicImageData = new DynamicImageData();
					 farmerDynamicFieldsValue = new FarmerDynamicFieldsValue();
					boolean photoAvail = true;
					for (Data data : dynamicData) {
						String key = data.getKey();
						String value = data.getValue();
						if (TxnEnrollmentProperties.FIELD_ID.equalsIgnoreCase(key)) {
							if (fieldConfigMap.containsKey(value)) {
							
								farmerDynamicFieldsValue = new FarmerDynamicFieldsValue();
								if (fdMap.containsKey(fieldConfigMap.get(value).getParentActField())
										&& !ObjectUtil.isEmpty(farmerDynamicData)) {
									FarmerDynamicFieldsValue fd = fdMap.get(fieldConfigMap.get(value).getParentActField()).get(0);
									if(fd.getFollowUps()!=null && !fd.getFollowUps().isEmpty()){
									farmerDynamicFieldsValue = 	fd.getFollowUps().stream().anyMatch(u -> u.getFieldName().equals(value)) ? fd.getFollowUps().stream().filter(u -> u.getFieldName().equals(value)).findFirst().get() : new FarmerDynamicFieldsValue(); 
									fd.getFollowUps().removeIf(u -> u.getFieldName().equals(value));
									}
								} 
								farmerDynamicFieldsValue.setTxnType(dm.getTxnType());
								farmerDynamicFieldsValue.setReferenceId(farmerDynamicData.getReferenceId());
								farmerDynamicFieldsValue.setCreatedDate(new Date());
								farmerDynamicFieldsValue.setCreatedUser(head.getAgentId());
								farmerDynamicFieldsValue
										.setTxnUniqueId(Long.valueOf(farmerDynamicData.getTxnUniqueId()));
								farmerDynamicFieldsValue.setFieldName(value);
								farmerDynamicFieldsValue.setComponentType(
										fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getComponentType());
								farmerDynamicFieldsValue.setFieldValue("");
								farmerDynamicFieldsValue.setTxnType(dm.getTxnType());

								farmerDynamicFieldsValue.setIsMobileAvail(
										fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
												? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName())
														.getIsMobileAvail()
												: "0");
								farmerDynamicFieldsValue.setValidationType(
										fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
												? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName())
														.getValidation()
												: "0");
								farmerDynamicFieldsValue
										.setParentId(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
												&& fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName())
														.getReferenceId() != null
																? fieldConfigMap
																		.get(farmerDynamicFieldsValue.getFieldName())
																		.getReferenceId()
																: 0);
								dynamicImageData.setFarmerDynamicFieldsValue(farmerDynamicFieldsValue);
								/* } */
							} else {
								break;
							}
						} else if (TxnEnrollmentProperties.AUDIO_URL.equalsIgnoreCase(key)) {
							byte[] voiceDataContent = null;
							try {
								if (data.getValue() != null && data.getValue().length() > 0) {
									voiceDataContent = Base64.decodeBase64(data.getValue());
									dynamicImageData.setImage(voiceDataContent);
								}
							} catch (Exception e) {

							}
						} else if (TxnEnrollmentProperties.F_PHOTO.equalsIgnoreCase(key)) {

							byte[] imageContent = null;
							DataHandler photo = data.getBinaryValue();

							try {
								if (photo != null && photo.getInputStream().available() > 0) {
									imageContent = IOUtils.toByteArray(photo.getInputStream());
									dynamicImageData.setImage(imageContent);
								}
							} catch (Exception e) {

							}

						} else if (TxnEnrollmentProperties.FARMER_PHOTO_CAPTURE_TIME.equalsIgnoreCase(key)) {
							if (!StringUtil.isEmpty(value) && !value.equals("0")) {
								try {
									Date photoCaptureDate = DateUtil.convertStringToDate(value,
											DateUtil.TXN_TIME_FORMAT);
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
					farmerDynamicFieldsValue
							.setAccessType(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
									? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getAccessType() : 0);
					farmerDynamicFieldsValue
							.setGrade(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
									? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getGrade() : null);
					farmerDynamicFieldsValue.setListMethod(fieldConfigMap
							.get(farmerDynamicFieldsValue.getFieldName()) != null
							&& fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getCatalogueType() != null
									? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getCatalogueType()
									: "");
					farmerDynamicFieldsValue.setParentId(fieldConfigMap
							.get(farmerDynamicFieldsValue.getFieldName()) != null
							&& fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getReferenceId() != null
									? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getReferenceId() : 0);

					farmerDynamicFieldsValue
							.setValidationType(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
									? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getValidation()
									: "0");
					// farmerDynamicFieldsValue.setFarmerDynamicData(farmerDynamicData);
					farmerDynamicFieldsValue
							.setIsMobileAvail(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
									? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getIsMobileAvail()
									: "0");

					farmerDynamicFieldsValue
							.setIsMobileAvail(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
									? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getIsMobileAvail()
									: "0");

					farmerDynamicFieldsValue
							.setAccessType(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
									? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getAccessType() : 0);

					farmerDynamicFieldsValue.setListMethod(fieldConfigMap
							.get(farmerDynamicFieldsValue.getFieldName()) != null
							&& fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getCatalogueType() != null
									? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getCatalogueType()
									: "");
					farmerDynamicFieldsValue.setParentId(fieldConfigMap
							.get(farmerDynamicFieldsValue.getFieldName()) != null
							&& fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getReferenceId() != null
									? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getReferenceId() : 0);
					farmerDynamicFieldsValue.setOrder(orderMap.get(farmerDynamicFieldsValue.getFieldName()));
					farmerDynamicFieldsValue
							.setFollowUp(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null

									? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getFollowUp() : 0);
					farmerDynamicFieldsValue
							.setGrade(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
									? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getGrade() : null);
					farmerDynamicFieldsValue.setParentActField(fieldConfigMap
							.get(farmerDynamicFieldsValue.getFieldName()) != null
							&& fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getParentActField() != null
									? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getParentActField()
									: null);
					farmerDynamicFieldsValue.setParentActKey(fieldConfigMap
							.get(farmerDynamicFieldsValue.getFieldName()) != null
							&& fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getParentActKey() != null
									? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getParentActKey()
									: null);
					
					if (farmerDynamicFieldsValue.getParentActField() != null) {
						if (farmerDynamicFieldsValue.getFollowUp() == 4
								&& fdMap.containsKey(farmerDynamicFieldsValue.getParentActField())) {
							FarmerDynamicFieldsValue ParentACt = fdMap.get(farmerDynamicFieldsValue.getParentActField())
									.get(0);
							SortedSet<FarmerDynamicFieldsValue> foolowUp = new TreeSet<>();
							if (!ParentACt.getFollowUps().isEmpty()) {
								foolowUp = ParentACt.getFollowUps();
							}
							farmerDynamicFieldsValue.setFollowUpParent(ParentACt);
							foolowUp.add(farmerDynamicFieldsValue);
							ParentACt.setFollowUps(foolowUp);
							fdMap.put(farmerDynamicFieldsValue.getParentActField(), Arrays.asList(ParentACt));

						}

					}

				}

			}
			farmerDynamicData.getFarmerDynamicFieldsValues()
					.addAll(fdMap.values().stream().flatMap(u -> u.stream()).collect(Collectors.toList()));
		}
		farmerDynamicData.setFollowUpDate(DateUtil.convertStringToDate(head.getTxnTime(), DateUtil.TXN_DATE_TIME));
		farmerDynamicData.setFollowUpUser(head.getAgentId());
		farmerDynamicData.setActStatus(2);
		farmerService.updateFarmerDynamicData(farmerDynamicData, fieldConfigMap, fdMap);

		return new HashMap();
	}

	@Override
	public Map<?, ?> processVoid(Map<?, ?> reqData) {
		return null;
	}

}
