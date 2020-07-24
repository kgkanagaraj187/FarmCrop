package com.sourcetrace.eses.adapter.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.activation.DataHandler;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.sms.SMSHistory;
import com.ese.entity.sms.SMSHistoryDetail;
import com.ese.entity.sms.SmsGroupDetail;
import com.ese.entity.util.ESESystem;
import com.ese.util.Base64Util;
import com.sourcetrace.eses.dao.IESESystemDAO;
import com.sourcetrace.eses.entity.DynamicFeildMenuConfig;
import com.sourcetrace.eses.entity.DynamicFieldConfig;
import com.sourcetrace.eses.entity.DynamicFieldScoreMap;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
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
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.FarmIcsConversion;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicData;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicFieldsValue;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.DynamicImageData.TYPES;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.service.ISMSService;
@Component
public class DynamicTxnAdapter implements ITxnAdapter {
	public static enum ENTITES {
		NA, FARMER, FARM, GROUP, CERTIFICATION, TRAINING, FARM_CROPS
	}

	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private IClientService clientService;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private IPreferencesService preferncesService;
	@Autowired
	public ICatalogueService catService;
	@Autowired
	public IUniqueIDGenerator idServoce;
	@Autowired
	public IProductDistributionService productService;
	@Autowired
	protected ICatalogueService catalogueService;
	@Autowired
	private IESESystemDAO systemDAO;
	
	private List<FarmCatalogue> farmCatalogueList;
	private String id;
	private String branch;
	private String tenantId;
	private boolean isVideo;
	private FarmerDynamicData farmerDynamicDatas;
	private String VIDEO_FILE_PATH = "/home/deployer/ftp/Pictures/";
	private FarmerDynamicData farmerDynamicData = new FarmerDynamicData();
	String phone;
	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {
		isVideo = true;
		ESESystem eseSystem = preferncesService.findPrefernceById("1");
		if (eseSystem.getPreferences().get(ESESystem.FTP_VIDEO_PATH) != null
				&& !StringUtil.isEmpty(eseSystem.getPreferences().get(ESESystem.FTP_VIDEO_PATH))) {
			VIDEO_FILE_PATH = eseSystem.getPreferences().get(ESESystem.FTP_VIDEO_PATH);
		}
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
		String entity = (String) reqData.get(TxnEnrollmentProperties.ENTITY);
		String inspectionStatus = (String) reqData.get(TxnEnrollmentProperties.INSPECTION_STATUS);
		String conversionStatus = (String) reqData.get(TxnEnrollmentProperties.CONVERSION_STATUS);
		if (StringUtil.isEmpty(conversionStatus)) {
			conversionStatus = "0";
		}
		String correctiveActionPlan = (String) reqData.get(TxnEnrollmentProperties.CORECTIVE_PLAN);
		String txnType = (reqData.containsKey(TxnEnrollmentProperties.DYNAMIC_TXN_ID))
				? (String) reqData.get(TxnEnrollmentProperties.DYNAMIC_TXN_ID) : "";

		Collection photoList = (reqData.containsKey(TransactionProperties.DYNAMIC_IMAGE_LIST))
				? (Collection) reqData.get(TransactionProperties.DYNAMIC_IMAGE_LIST) : null;

		String season = (reqData.containsKey(TxnEnrollmentProperties.SEASON))
				? (String) reqData.get(TxnEnrollmentProperties.SEASON) : "";

		String insDate = (String) reqData.get(TxnEnrollmentProperties.INSPECTION_DATE);
		String insName = (String) reqData.get(TxnEnrollmentProperties.INSPECTOR_NAME);
		String mobileNo = (String) reqData.get(TxnEnrollmentProperties.MOBILE_NO);
		String insType = (String) reqData.get(TxnEnrollmentProperties.INSPECTIONS_TYPE);
		String scope = (String) reqData.get(TxnEnrollmentProperties.SCOPE);
		String totLand = (String) reqData.get(TxnEnrollmentProperties.TOTAL_LAND_HOLDING_AREA);
		String orgLand = (String) reqData.get(TxnEnrollmentProperties.ORGANIC_LAND);
		String totSite = (String) reqData.get(TxnEnrollmentProperties.TOTAL_SITE);

		
		
		
		Head head = (Head) reqData.get(TransactionProperties.HEAD);
		branch = head.getBranchId();
		tenantId = head.getTenantId();
		farmerDynamicData = new FarmerDynamicData();
		File videoFileObject = null;
		FarmerDynamicData fd = farmerService.findFarmerDynamicDataByTxnUniquId(txnUniqueId);
		if (ObjectUtil.isEmpty(fd)) {
			// List<DynamicFieldConfig> dynmaicFieldsConfigList = new
			// ArrayList<>();

			/*
			 * farmerService.listDynamicSections().stream().forEach(section -> {
			 * dynmaicFieldsConfigList.addAll(section.getDynamicFieldConfigs());
			 * });
			 */
	
			if (!StringUtil.isEmpty(entity) && StringUtil.isInteger(entity)) {
				if (Integer.parseInt(entity) == ENTITES.FARMER.ordinal()) {
					Farmer farmer = farmerService.findFarmerByFarmerId(farmerId);
					setId(String.valueOf(farmer.getId()));
				} else if (Integer.parseInt(entity) == ENTITES.FARM.ordinal()
						|| Integer.parseInt(entity) == ENTITES.CERTIFICATION.ordinal()) {
					Object[] farm = farmerService.listFarmInfoByCode(farmerId);
					setId(String.valueOf(farm[0]));
				} else if (Integer.parseInt(entity) == ENTITES.GROUP.ordinal()) {
					Warehouse group = locationService.findSamithiByCode(farmerId);
					setId(String.valueOf(group.getId()));
				} else if (Integer.parseInt(entity) == ENTITES.FARM_CROPS.ordinal()) {
					String[] str = farmerId.split("_");
					Farm fm = farmerService.findFarmByCode(str[0].trim().toString());
					FarmCrops fc = fm.getFarmCrops().stream()
							.filter(u -> u.getProcurementVariety().getCode().equals(str[2].trim().toString()))
							.findFirst().get();
					setId(String.valueOf(fc.getId()));

				} else {
					if (farmerId.contains(",")) {
						List<Farmer> f = farmerService.listFarmerByFarmerIdByIdList(Arrays.asList(farmerId.split(",")));
						setId(f.stream().map(u -> String.valueOf(u.getId())).collect(Collectors.joining(",")));
					} else {
						Farmer farmer = farmerService.findFarmerByFarmerId(farmerId.trim());
						setId(String.valueOf(farmer.getId()));
					}
				}
			} else {
				if (head.getTxnType().equalsIgnoreCase("381")) {
					Warehouse group = locationService.findSamithiByCode(farmerId);
					setId(String.valueOf(group.getId()));
				} else if (head.getTxnType().equalsIgnoreCase("384") || head.getTxnType().equalsIgnoreCase("391")) {
					Farm farm = farmerService.findFarmByCode(farmerId);
					setId(String.valueOf(farm.getId()));
				}
			}

			Map<String, List<FarmerDynamicFieldsValue>> fdMap = new HashMap<>();
			DynamicFeildMenuConfig dm = farmerService.findDynamicMenusByMType(txnType).get(0);
			LinkedHashMap<String, DynamicFieldConfig> fieldConfigMap = new LinkedHashMap<>();

			Map<Long, Integer> fieldTypeMap = new HashMap<>();
			Map<String, String> ActMap = new HashMap<>();
			if (dm.getIsSingleRecord() == 1) {
				if (dm.getIsSeason() != null && dm.getIsSeason() == 1) {
					farmerDynamicData = farmerService.findFarmerDynamicDataBySeason(dm.getTxnType(), getId(), season);
				} else {
					farmerDynamicData = farmerService.findFarmerDynamicData(dm.getTxnType(), getId());
				}
				if (farmerDynamicData != null) {
					fdMap = farmerDynamicData.getFarmerDynamicFieldsValues().stream()
							.collect(Collectors.groupingBy(FarmerDynamicFieldsValue::getFieldName));
					farmerDynamicData.getFarmerDynamicFieldsValues()
							.removeAll(farmerDynamicData.getFarmerDynamicFieldsValues().stream().filter(
									map -> (map.getIsMobileAvail() != null && Arrays.asList("0","2","5","4").contains(map.getIsMobileAvail())))
									.collect(Collectors.toList()));

					fieldTypeMap = farmerDynamicData.getFarmerDynamicFieldsValues().stream()
							.filter(u -> (u.getTypez() != null && u.getParentId() != null))
							.collect(Collectors.toMap(FarmerDynamicFieldsValue::getParentId,
									FarmerDynamicFieldsValue::getTypez, Integer::max));

				} else {

					farmerDynamicData = new FarmerDynamicData();

				}
			}
			if (dm.getIsScore() != null && (dm.getIsScore() == 1 || dm.getIsScore() == 2)) {
				farmerDynamicData.setIsScore(dm.getIsScore());
				farmerDynamicData.setScoreValue(new HashMap<>());
			}
			dm.getDynamicFieldConfigs().stream().forEach(section -> {
				section.getField().setfOrder(section.getOrder());
				fieldConfigMap.put(section.getField().getCode(), section.getField());
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
			/*	
			*/ List<FarmerDynamicFieldsValue> farmerDynamicFieldsValueList = new LinkedList<>();
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
						} else if (TxnEnrollmentProperties.FIELD_VAL.equalsIgnoreCase(key)) {
							farmerDynamicFieldsValue.setFieldValue(value);
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
									}else {
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
								}else if ( fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName())
										.getCatalogueType()!=null && Integer.valueOf(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName())
								.getAccessType()) == DynamicFieldConfig.ACCESS_TYPE.LIST_METHOD.ordinal() && Integer.valueOf(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName())
										.getCatalogueType()) ==10  && head.getTenantId().equals("griffith")) {
							
							 ProcurementVariety pv= productService.findProcurementVariertyByCode(farmerDynamicFieldsValue.getFieldValue());
							 if (pv == null) {
							    pv=productService.findProcurementVariertyByNameAndCropId(farmerDynamicFieldsValue.getFieldValue(),Long.valueOf(1l));
							    if (pv == null) {
								pv = new ProcurementVariety();
								pv.setCode(idServoce.getProcurementVarietyIdSeq());
								pv.setName(farmerDynamicFieldsValue.getFieldValue());
								pv.setRevisionNo(DateUtil.getRevisionNumber());
								pv.setProcurementProduct(productService.findProcurementProductById(1l));
								productService.addProcurementVariety(pv);
							    }
							}
							farmerDynamicFieldsValue.setFieldValue(pv.getCode());
						}
							}

							
						} else if (TxnEnrollmentProperties.COMPONENT_TYPE.equalsIgnoreCase(key)) {
							farmerDynamicFieldsValue.setComponentType(value);
						}
					}

					if (dm.getIsSingleRecord() == 1 && fdMap.containsKey(farmerDynamicFieldsValue.getFieldName())
							&& !ObjectUtil.isEmpty(farmerDynamicData)) {
						farmerDynamicData.getFarmerDynamicFieldsValues()
								.removeAll(fdMap.get(farmerDynamicFieldsValue.getFieldName()));
					}
					farmerDynamicFieldsValue.setTxnType(dm.getTxnType());
					farmerDynamicFieldsValue.setReferenceId(getId());
					farmerDynamicFieldsValue.setCreatedDate(new Date());
					farmerDynamicFieldsValue.setCreatedUser(head.getAgentId());
					farmerDynamicFieldsValue.setTxnUniqueId(Long.valueOf(txnUniqueId));
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
					farmerDynamicFieldsValue.setFarmerDynamicData(farmerDynamicData);
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
				
					if (fieldConfigMap.containsKey(farmerDynamicFieldsValue.getFieldName())
							&& fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
							&& !farmerDynamicFieldsValue.getIsMobileAvail().equals("2")) {
						farmerDynamicFieldsValueList.add(farmerDynamicFieldsValue);
						// To get the values for formula calculation

						fdMap.put(farmerDynamicFieldsValue.getFieldName(), new ArrayList<FarmerDynamicFieldsValue>() {
							{
								add(farmerDynamicFieldsValue);

							}
						});
					}

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
							farmerDynamicFieldsValue
									.setParentId(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
											&& fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName())
													.getReferenceId() != null
															? fieldConfigMap
																	.get(farmerDynamicFieldsValue.getFieldName())
																	.getReferenceId()
															: 0);

						} else if (TxnEnrollmentProperties.FIELD_VAL.equalsIgnoreCase(key)) {
							farmerDynamicFieldsValue.setFieldValue(value);
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
						} else if (TxnEnrollmentProperties.COMPONENT_TYPE.equalsIgnoreCase(key)) {
							farmerDynamicFieldsValue.setComponentType(value);
						} else if (TxnEnrollmentProperties.TXN_TYPE_ID.equalsIgnoreCase(key)) {
							farmerDynamicFieldsValue.setTxnType(txnTypeIdMaster);
						} else if (TxnEnrollmentProperties.LIST_ITRATION.equalsIgnoreCase(key)) {

							Integer type = null;
							if (fieldTypeMap != null
									&& fieldTypeMap.containsKey(farmerDynamicFieldsValue.getParentId())) {

								Integer iterateCount = fieldTypeMap.get(farmerDynamicFieldsValue.getParentId());

								if (!StringUtil.isEmpty(iterateCount) && StringUtil.isInteger(iterateCount)) {
									type = Integer.valueOf(iterateCount) + Integer.valueOf(value);
								} else {
									type = Integer.valueOf(value);
								}
							} else {
								type = Integer.valueOf(value);
							}

							farmerDynamicFieldsValue.setTypez(type);
						}
					}
					// To add the values in the list
					if (fdMap.containsKey(farmerDynamicFieldsValue.getFieldName())) {
						List<FarmerDynamicFieldsValue> dy = fdMap.get(farmerDynamicFieldsValue.getFieldName());
						dy.add(farmerDynamicFieldsValue);
						fdMap.put(farmerDynamicFieldsValue.getFieldName(), dy);
					} else {
						fdMap.put(farmerDynamicFieldsValue.getFieldName(), new ArrayList<FarmerDynamicFieldsValue>() {
							{
								add(farmerDynamicFieldsValue);

							}
						});
					}
					farmerDynamicFieldsValue
							.setValidationType(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
									? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getValidation()
									: "0");

					farmerDynamicFieldsValue.setReferenceId(getId());
					farmerDynamicFieldsValue.setCreatedDate(new Date());
					farmerDynamicFieldsValue.setCreatedUser(head.getAgentId());
					farmerDynamicFieldsValue.setTxnType(dm.getTxnType());
					farmerDynamicFieldsValue.setFarmerDynamicData(farmerDynamicData);
					farmerDynamicFieldsValue.setTxnUniqueId(Long.valueOf(txnUniqueId));
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
					

					farmerDynamicFieldsValueList.add(farmerDynamicFieldsValue);
				}
			}
			Set<FarmerDynamicFieldsValue> fdfv = new HashSet<FarmerDynamicFieldsValue>();

			farmerDynamicData.setTxnUniqueId(Long.valueOf(txnUniqueId));
			farmerDynamicData.setReferenceId(getId());
			farmerDynamicData.setTxnType(dm.getTxnType());
			Date txnDateVal = null;
			if (!StringUtil.isEmpty(txnDate)) {
				txnDateVal = DateUtil.convertStringToDate(txnDate, DateUtil.TXN_DATE_TIME);
				farmerDynamicData.setDate(txnDateVal);
			}
			farmerDynamicData.setLatitude(StringUtil.isEmpty(latitude) ? null : latitude);
			farmerDynamicData.setLongitude(StringUtil.isEmpty(longitude) ? null : longitude);
			farmerDynamicData.setCreatedDate(DateUtil.convertStringToDate(head.getTxnTime(), DateUtil.TXN_DATE_TIME));
			farmerDynamicData.setCreatedUser(head.getAgentId());
			farmerDynamicData.setTxnType(dm.getTxnType());
			farmerDynamicData.setTxnUniqueId(Long.valueOf(txnUniqueId));
			farmerDynamicData.setStatus("0");
			farmerDynamicData.setBranch(branch);
			farmerDynamicData.setEntityId(entity);
			farmerDynamicData.setSeason(season);

			if (Integer.parseInt(entity) == ENTITES.CERTIFICATION.ordinal()) {
				farmerDynamicData.setConversionStatus(inspectionStatus);
				farmerDynamicData.setIcsName(conversionStatus);
				farmerDynamicData.setCorrectiveActionPlan(correctiveActionPlan);
				FarmIcsConversion existing = new FarmIcsConversion();
				Date df = null;
				if (!StringUtil.isEmpty(insDate)) {
					// df = DateUtil.convertStringToDate(insDate,
					// DateUtil.TXN_DATE_TIME);
					df = DateUtil.convertStringToDate(insDate, DateUtil.FARMER_DOB);
					if (!tenantId.equals(ESESystem.SUSAGRI)) {
						 existing = farmerService
								.findFarmIcsConversionByFarmIdAndInspectionTypeAndScopeOperationWithInspectionDate(
										Long.valueOf(farmerDynamicData.getReferenceId()), insType, scope,
										Integer.parseInt(DateUtil.getYearByDateTime(df)));
					}else{
						 existing = farmerService
									.findFarmIcsConversionByFarmSeasonScopeAndInspectionType(
											Long.valueOf(farmerDynamicData.getReferenceId()), season, scope,insType);
					}
					
					Farm frm = farmerService.findFarmByID(Long.parseLong(farmerDynamicData.getReferenceId()));
					if (ObjectUtil.isEmpty(existing)) {
						FarmIcsConversion farmIcsconversion = new FarmIcsConversion();
						farmIcsconversion.setFarm(frm);
						farmIcsconversion.setIcsType(conversionStatus);
						farmIcsconversion.setInspectionDate(df);
						farmIcsconversion.setInspectorMobile(mobileNo);
						farmIcsconversion.setInspectorName(insName);
						farmIcsconversion.setOrganicLand(orgLand);
						farmIcsconversion.setScope(scope);
						farmIcsconversion.setSeason(season);
						farmIcsconversion.setStatus(Integer.parseInt(inspectionStatus));
						farmIcsconversion.setTotalLand(totLand);
						farmIcsconversion.setInsType(insType);
						farmIcsconversion.setTotalSite(totSite);
						farmIcsconversion.setQualified(Integer.parseInt(inspectionStatus));
						farmIcsconversion.setIsActive(1);
						farmIcsconversion.setFarmer(frm.getFarmer());
						if (tenantId.equals(ESESystem.WILMAR_TENANT_ID)) {
							Farmer farmer = farmerService.findFarmerById(Long.valueOf(frm.getFarmer().getId()));
							farmer.setIsCertifiedFarmer(1);
							farmer.setCertificationType(1);
							farmerService.update(farmer);
						}
						/*if (tenantId.equals(ESESystem.WILMAR_TENANT_ID)) {*/
							if (!ObjectUtil.isEmpty(frm) && !ObjectUtil.isEmpty(frm.getFarmer().getIsCertifiedFarmer())
									&& frm.getFarmer().getIsCertifiedFarmer() == 1
									&& !StringUtil.isEmpty(farmIcsconversion.getIcsType())
									&& farmIcsconversion.getIcsType().equalsIgnoreCase("3")) {
								farmIcsconversion.setOrganicStatus("3");
							} else {
								farmIcsconversion.setOrganicStatus("0");
							}
						/*}*/
						farmerService.saveFarmIcsConversionByFarmId(Long.parseLong(farmerDynamicData.getReferenceId()),
								farmIcsconversion);
						FarmIcsConversion fIcs = farmerService.findFarmIcsConversionById(farmIcsconversion.getId());
						farmerDynamicData.setFarmIcs(fIcs);

					} else {
						existing.setFarm(frm);
						existing.setIcsType(conversionStatus);
						existing.setInspectionDate(DateUtil.convertStringToDate(insDate, DateUtil.FARMER_DOB));
						existing.setInspectorMobile(mobileNo);
						existing.setInspectorName(insName);
						existing.setOrganicLand(orgLand);
						existing.setScope(scope);
						existing.setSeason(season);
						existing.setStatus(Integer.parseInt(inspectionStatus));
						existing.setTotalLand(totLand);
						existing.setInsType(insType);
						existing.setTotalSite(totSite);
						existing.setQualified(Integer.parseInt(inspectionStatus));
						existing.setIsActive(1);
						existing.setFarmer(frm.getFarmer());
					/*	if (tenantId.equals(ESESystem.WILMAR_TENANT_ID)) {*/
							if (!ObjectUtil.isEmpty(frm) && !ObjectUtil.isEmpty(frm.getFarmer().getIsCertifiedFarmer())
									&& frm.getFarmer().getIsCertifiedFarmer() == 1
									&& !StringUtil.isEmpty(existing.getIcsType())
									&& existing.getIcsType().equalsIgnoreCase("3")) {
								existing.setOrganicStatus("3");
							} else {
								existing.setOrganicStatus("0");
							}
						/*}*/
						farmerService.update(existing);
						FarmIcsConversion fIcs = farmerService.findFarmIcsConversionById(existing.getId());
						farmerDynamicData.setFarmIcs(fIcs);

					}
				}
				/*
				 * if (!StringUtil.isEmpty(conversionStatus)) {
				 * farmerService.updateFarmICSStatusByFarmId(Long.valueOf(getId(
				 * )), conversionStatus); }
				 */

			}
			farmerDynamicData.setIsSingleRecord(dm == null ? 0 : dm.getIsSingleRecord());

			/*
			 * if(!StringUtil.isEmpty(conversionStatus)){
			 * farmerService.updateFarmICSStatusByFarmId(Long.valueOf(getId()),
			 * conversionStatus); }
			 */
			/*
			 * } else {
			 * clientService.saveFarmerDynmaicList(farmerDynamicFieldsValueList)
			 * ; }
			 */
			/* farmerService.updateDynamicFarmerFieldComponentType(); */

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
					FarmerDynamicFieldsValue farmerDynamicFieldsValue = new FarmerDynamicFieldsValue();
					boolean photoAvail = true;
					for (Data data : dynamicData) {
						String key = data.getKey();
						String value = data.getValue();
						if (TxnEnrollmentProperties.FIELD_ID.equalsIgnoreCase(key)) {
							if (fieldConfigMap.containsKey(value)) {
								/*
								 * if
								 * (!ObjectUtil.isEmpty(fieldConfigMap.get(value
								 * ).getParentDepen())) { String parentKey =
								 * fieldConfigMap.get(value).getParentDepen().
								 * getCode(); farmerDynamicFieldsValue =
								 * farmerDynmaicFieldValuesList.get(parentKey);
								 * farmerDynamicFieldsValueList.remove(
								 * farmerDynamicFieldsValue);
								 * dynamicImageData.setFarmerDynamicFieldsValue(
								 * farmerDynamicFieldsValue);
								 * 
								 * } else {
								 */
								farmerDynamicFieldsValue = new FarmerDynamicFieldsValue();

								farmerDynamicFieldsValue.setCreatedDate(new Date());
								farmerDynamicFieldsValue.setReferenceId(getId());
								farmerDynamicFieldsValue.setCreatedUser(head.getAgentId());
								farmerDynamicFieldsValue.setCreatedDate(new Date());
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
								farmerDynamicFieldsValueList.add(farmerDynamicFieldsValue);

								// farmerService.save(farmerDynamicFieldsValue);
								dynamicImageData.setFarmerDynamicFieldsValue(farmerDynamicFieldsValue);
								/* } */
							} else {
								break;
							}
						} else if (TxnEnrollmentProperties.VIDEO.equalsIgnoreCase(key)) {
							byte[] imageContent = null;
							String fileName = data.getValue();
							if (farmerDynamicFieldsValue.getComponentType()
									.equals(String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.VIDEO.ordinal()))) {

								if (!StringUtil.isEmpty(fileName)) {
									try {
										videoFileObject = createFileObject(fileName);
										if (!ObjectUtil.isEmpty(videoFileObject)) {
											// if(Files.exists(Paths.get(videoFilePath+fileName)))
											// {
											if (videoFileObject.exists()) {
												byte[] videoFileObj = IOUtils
														.toByteArray(new FileInputStream(videoFileObject));
												dynamicImageData.setImage(videoFileObj);
												System.out.println("upload" + fileName);
												try {
													String fileExt = fileName.split(".")[1].toString();
													dynamicImageData.setFileExt(fileExt);
												} catch (Exception e) {
													dynamicImageData.setFileExt("mp4");
												}
											} else {
												isVideo = false;
											}
										}
									} catch (Exception e) {
										isVideo = false;
									}
								}
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
						} else if (TxnEnrollmentProperties.LIST_ITRATION.equalsIgnoreCase(key) && value != null
								&& Integer.valueOf(value) > 0) {
							Integer type = null;
							if (fieldTypeMap != null
									&& fieldTypeMap.containsKey(farmerDynamicFieldsValue.getParentId())) {

								Integer iterateCount = fieldTypeMap.get(farmerDynamicFieldsValue.getParentId());

								if (!StringUtil.isEmpty(iterateCount) && StringUtil.isInteger(iterateCount)) {
									type = Integer.valueOf(iterateCount) + Integer.valueOf(value);
								} else {
									type = Integer.valueOf(value);
								}
							} else {
								type = Integer.valueOf(value);
							}

							farmerDynamicFieldsValue.setTypez(type);
						} else if (TxnEnrollmentProperties.PHOTO_LATITUDE.equalsIgnoreCase(key))
							dynamicImageData.setLatitude(value);

						else if (TxnEnrollmentProperties.PHOTO_LONGITUDE.equalsIgnoreCase(key))
							dynamicImageData.setLongitude(value);

					}

					DynamicFieldConfig df = fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName());
					if (df.getReferenceId() != null && Long.valueOf(df.getReferenceId()) > 0) {
						if (fdMap.containsKey(farmerDynamicFieldsValue.getFieldName())) {
							List<FarmerDynamicFieldsValue> dy = fdMap.get(farmerDynamicFieldsValue.getFieldName());
							dy.add(farmerDynamicFieldsValue);
							fdMap.put(farmerDynamicFieldsValue.getFieldName(), dy);
						} else {
							List<FarmerDynamicFieldsValue> fdList = new ArrayList<FarmerDynamicFieldsValue>();
							fdList.add(farmerDynamicFieldsValue);
							fdMap.put(farmerDynamicFieldsValue.getFieldName(), fdList);
						}
					} else {
						if (dm.getIsSingleRecord() == 1 && fdMap.containsKey(farmerDynamicFieldsValue.getFieldName())
								&& !ObjectUtil.isEmpty(farmerDynamicData)) {
							farmerDynamicData.getFarmerDynamicFieldsValues()
									.removeAll(fdMap.get(farmerDynamicFieldsValue.getFieldName()));
						}
					}

					Set<DynamicImageData> imageDataSet = new HashSet<>();
					imageDataSet.add(dynamicImageData);
					farmerDynamicFieldsValue.setDymamicImageData(imageDataSet);
					farmerDynamicFieldsValue.setFarmerDynamicData(farmerDynamicData);
					farmerDynamicFieldsValue
							.setIsMobileAvail(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
									? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getIsMobileAvail()
									: "0");

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
							.setFollowUp(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null

									? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getFollowUp() : 0);

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

					farmerDynamicFieldsValueList.add(farmerDynamicFieldsValue);

					// farmerService.update(farmerDynamicFieldsValue);
					// farmerService.updateDynmaicImage(farmerDynamicFieldsValue,
					// String.valueOf(farmerDynamicData.getId()));
				}
			}

			Set<DynamicImageData> imgDataSet = new HashSet<>();
			
			String ds = (String) reqData.get(TxnEnrollmentProperties.DIGITAL_SIGNATURE);
			byte[] digitalSignature = Base64.decodeBase64(ds);
			
			DynamicImageData dynamicImagData = new DynamicImageData();
			if (digitalSignature != null && !digitalSignature.equals("[]")) {
				dynamicImagData.setImage(digitalSignature);
				dynamicImagData.setFarmerDynamicFieldsValue(null);
				dynamicImagData.setFarmerDynamicData(farmerDynamicData);
				dynamicImagData.setTypez(TYPES.digitalSignature.ordinal());
				imgDataSet.add(dynamicImagData);
				farmerDynamicData.setDymamicImageData(imgDataSet);
			}
			
			
			String as = (String) reqData.get(TxnEnrollmentProperties.agentSign);
			byte[] agentSign = Base64.decodeBase64(as);
			
			DynamicImageData dynamicImagData2 = new DynamicImageData();
			if (agentSign != null && !agentSign.equals("[]")) {
				dynamicImagData2.setImage(agentSign);
				dynamicImagData2.setFarmerDynamicFieldsValue(null);
				dynamicImagData2.setFarmerDynamicData(farmerDynamicData);
				dynamicImagData2.setTypez(TYPES.agentSignature.ordinal());
				imgDataSet.add(dynamicImagData2);
				farmerDynamicData.setDymamicImageData(imgDataSet);
			}
			
		/*	DataHandler as = (reqData.containsKey(TxnEnrollmentProperties.agentSign))
					? (DataHandler) reqData.get(TxnEnrollmentProperties.agentSign) : null;
			if (!ObjectUtil.isEmpty(as)) {
				byte[] agentSign = null;
				try {
					if (as != null) {
						agentSign = IOUtils.toByteArray(as.getInputStream());
						if (!StringUtil.isEmpty(agentSign) && !agentSign.equals("[]")) {
							farmer.setFingerPrint(agentSign);
						}
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}*/
			
			fdfv.addAll(farmerDynamicFieldsValueList);
			farmerDynamicFieldsValueList.stream()
					.filter(farmerDynamicFieldsValue -> farmerDynamicFieldsValue.getFollowUp() == 1
							|| farmerDynamicFieldsValue.getFollowUp() == 2)
					.forEach(farmerDynamicFieldsValue -> {
						if (farmerDynamicFieldsValue.getFollowUp() == 1 && farmerDynamicFieldsValueList.stream()
								.anyMatch(u -> u.getFieldName().equals(farmerDynamicFieldsValue.getParentActField()))) {
							FarmerDynamicFieldsValue ParentACt = farmerDynamicFieldsValueList.stream()
									.filter(u -> u.getFieldName().equals(farmerDynamicFieldsValue.getParentActField()))
									.findFirst().get();
						
							fdfv.remove(ParentACt);
							ParentACt.setActionPlan(farmerDynamicFieldsValue);
							fdfv.add( ParentACt);

						} else if (farmerDynamicFieldsValue.getFollowUp() == 2 && farmerDynamicFieldsValueList.stream()
								.anyMatch(u -> u.getFieldName().equals(farmerDynamicFieldsValue.getParentActField()))) {
							FarmerDynamicFieldsValue ParentACt = farmerDynamicFieldsValueList.stream()
									.filter(u -> u.getFieldName().equals(farmerDynamicFieldsValue.getParentActField()))
									.findFirst().get();
							fdfv.remove(ParentACt);
							ParentACt.setDeadline(farmerDynamicFieldsValue);
							fdfv.add( ParentACt);

						}
					});

		

			if (farmerDynamicData != null && farmerDynamicData.getId() != null && farmerDynamicData.getId() > 0) {
				farmerDynamicData.getFarmerDynamicFieldsValues().addAll(fdfv);
			} else {
				farmerDynamicData.setFarmerDynamicFieldsValues(fdfv);
			}
			if (isVideo) {
				try {

					farmerService.saveOrUpdate(farmerDynamicData, fdMap, fieldConfigMap);
					if (fieldConfigMap.values().stream().anyMatch(p -> Arrays.asList("4").contains(p.getIsMobileAvail())
							&& p.getFormula() != null && !StringUtil.isEmpty(p.getFormula()))) {
						farmerDynamicData=farmerService.processCustomisedFormula(farmerDynamicData, fieldConfigMap,fdMap);
					}

					farmerService.deleteChildObjects(farmerDynamicData.getTxnType());

				} catch (Exception e) {
					farmerService.deleteObject(farmerDynamicData);
				}

			} else {

				throw new SwitchException(ITxnErrorCodes.VIDEO_NOT_UPLOADED);

			}
			if(dm.getMethod()!= null && dm.getParameter()!= null){
				
				Map<String, String> valuesMap = new LinkedHashMap<>();
				Map<String, String> fieldMap = new LinkedHashMap<>();
				farmerDynamicData = farmerService.findFarmerDynamicData(String.valueOf(farmerDynamicData.getId()));
				
				dm.getDynamicFieldConfigs().stream().forEach(section -> {
					section.getField().setfOrder(section.getOrder());
					fieldMap.put(section.getField().getCode(), section.getField().getComponentName());
				});
				List<String> param=Arrays.asList(dm.getParameter().split(","));
		
				Map<String,String> paramMap=param.stream().collect(Collectors.toMap(k->k, v->v));
				farmerDynamicData.getFarmerDynamicFieldsValues().stream().filter(fc->paramMap.containsKey(fc.getFieldName())).forEach(dynamicFieldValues -> {
			
					if (!StringUtil.isEmpty(dynamicFieldValues.getComponentType())
							&& Arrays
									.asList(String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.DROP_DOWN.ordinal()),
											String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.RADIO.ordinal()),
											String.valueOf(
													DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal()),
											String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.CHECKBOX.ordinal()))
									.contains(dynamicFieldValues.getComponentType())) {
						valuesMap.put(fieldMap.get(dynamicFieldValues.getFieldName()),
								getCatlogueValueByCode(dynamicFieldValues.getFieldValue()).getName());
						
						if(param.get(param.size()-1)!=null && param.get(param.size()-1).equalsIgnoreCase(dynamicFieldValues.getFieldName())){
							Farmer farmer = farmerService.findFarmerById(Long.valueOf(farmerDynamicData.getReferenceId()));
							FarmCatalogue catalogue =	catalogueService.findCatalogueByCode(dynamicFieldValues.getFieldValue());
							 //phone=catalogue.getDispName().toString();
							valuesMap.put("phone",catalogue.getDispName()+","+ (farmer.getMobileNumber() == null ? "" : farmer.getMobileNumber()));
						}
					} else {
						valuesMap.put(fieldMap.get(dynamicFieldValues.getFieldName()), dynamicFieldValues.getFieldValue());
					}

				});
				
			
				
				 getMethodValue(dm.getMethod(), valuesMap);
			}

		}
		return new HashMap();
	}

	private void getMethodValue(String methodName, Map<String, String> valuesMap) {
		// TODO Auto-generated method stub

		Object field = null;
		try {
			@SuppressWarnings("rawtypes")
			Class cls = this.getClass();

			if (valuesMap != null) {
		
					Method setNameMethod = this.getClass().getMethod(methodName, Map.class);
					field = setNameMethod.invoke(this, valuesMap);
				
			} 

		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	

	

	@Override
	public Map<?, ?> processVoid(Map<?, ?> reqData) {
		return null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public File createFileObject(String fileName) {
		File tmpFileObject = null;
		String filePath = VIDEO_FILE_PATH;
		tmpFileObject = new File(filePath + fileName);
		System.out.println("The File Path To Create is : " + filePath + fileName);
		return tmpFileObject;
	}
	
	public FarmCatalogue getCatlogueValueByCode(String code) {
		if (ObjectUtil.isEmpty(getFarmCatalogueList()) && ObjectUtil.isListEmpty(getFarmCatalogueList())) {
			setFarmCatalogueList(new ArrayList<>());
			setFarmCatalogueList(catalogueService.listCatalogues());
		}
		FarmCatalogue catValue = getFarmCatalogueList().stream().filter(fc -> fc.getCode().equalsIgnoreCase(code))
				.findAny().orElseGet(() -> {
					FarmCatalogue tmp = new FarmCatalogue();
					tmp.setName("");
					tmp.setDispName("");
					return tmp;
				});
		

		return catValue;
	}
	
	public List<FarmCatalogue> getFarmCatalogueList() {
		return farmCatalogueList;
	}

	public void setFarmCatalogueList(List<FarmCatalogue> farmCatalogueList) {
		this.farmCatalogueList = farmCatalogueList;
	}
	

}
