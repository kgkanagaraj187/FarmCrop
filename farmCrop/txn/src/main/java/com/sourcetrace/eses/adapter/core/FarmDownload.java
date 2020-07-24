/*
 * FarmerDownload.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.util.CollectionUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Calf;
import com.sourcetrace.esesw.entity.profile.Cow;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.FarmIcsConversion;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicFieldsValue;

@Component
public class FarmDownload implements ITxnAdapter {

	private static final Logger LOGGER = Logger.getLogger(FarmDownload.class.getName());
	private static final String FARMER_DOB = "yyyyMMdd";
	@Autowired
	private IFarmerService farmerService;

	@Autowired
	private IAgentService agentService;
	@Autowired
	private IPreferencesService preferncesService;
	@Autowired
	protected ICatalogueService catalogueService;
	

	private String tenantId;

	SimpleDateFormat sdf = new SimpleDateFormat(FARMER_DOB);
	SimpleDateFormat sdf1 = new SimpleDateFormat(DateUtil.TXN_DATE_TIME);
	SimpleDateFormat sdfTxnDate = new SimpleDateFormat(DateUtil.DATABASE_DATE_FORMAT);

	List<java.lang.Object[]> farmICSConversionList = new ArrayList<>();
	Map<Long, List<java.lang.Object[]>> farmObject = new HashMap<>();
	Map<Long, List<java.lang.Object[]>> farmCropsObject = new HashMap<>();
	private List<FarmCatalogue> farmCatalogueList;
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {

		/** REQUEST DATA **/
		Head head = (Head) reqData.get(TransactionProperties.HEAD);
		String agentId = head.getAgentId();
		setTenantId(head.getTenantId());
		if (StringUtil.isEmpty(agentId))
			throw new SwitchException(ITxnErrorCodes.AGENT_ID_EMPTY);

		Agent agent = agentService.findAgentByProfileId(agentId);
		if (ObjectUtil.isEmpty(agent))
			throw new SwitchException(ITxnErrorCodes.INVALID_AGENT);

		String revisionNo = (String) reqData.get(TxnEnrollmentProperties.FARM_DOWNLOAD_REVISION_NO);
		if (StringUtil.isEmpty(revisionNo))
			throw new SwitchException(ITxnErrorCodes.EMPTY_FARM_REVISION_NO);

		LOGGER.info("REVISION NO" + revisionNo);
		List<java.lang.Object[]> farmerList = new ArrayList<java.lang.Object[]>();

		Collection farmerCollection = new Collection();
		/*
		 * Collection farmCollection = new Collection(); Collection
		 * farmCropsCollection = new Collection();
		 */
		List<Object> farmerObjects = new ArrayList<Object>();
		/** FORMING FARMER OBJECT **/
		if (!ObjectUtil.isListEmpty(agent.getWareHouses())) {
			farmerList = farmerService.listFarmFieldsByFarmerIdByAgentIdAndSeason(agent.getId(),
					Long.valueOf(revisionNo));

			if (!ObjectUtil.isListEmpty(farmerList)) {

				List<Map> objectMapForFarm = formResponseFarm(farmerList);
				Collection farmCollection = new Collection();
				List<Object> farmObjects = new ArrayList<Object>();
				for (Map m : objectMapForFarm) {
					Object farmObject = new Object();
					farmObject.setData(CollectionUtil.mapToList(m));

					farmObjects.add(farmObject);

				}
				farmerCollection.setObject(farmObjects);
			}

		}

		Map resp = new LinkedHashMap();
		resp.put(TxnEnrollmentProperties.FARM_LIST, farmerCollection);
		if (!farmerList.isEmpty()) {
			resp.put(TxnEnrollmentProperties.FARM_DOWNLOAD_REVISION_NO,
					farmerList.get(0)[farmerList.get(0).length - 1]);
		} else {
			resp.put(TxnEnrollmentProperties.FARM_DOWNLOAD_REVISION_NO, "0");
		}

		return resp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.txn.adapter.ITxnAdapter#processVoid(java.util.Map)
	 */
	@Override
	public Map<?, ?> processVoid(Map<?, ?> reqData) {

		return null;
	}

	String icstype = "";
	List<java.lang.Object[]> dynamicValues = new ArrayList<java.lang.Object[]>();
	 Map<Integer,  List<java.lang.Object[]>> dynamicFieldValueMap = new HashMap<Integer,  List<java.lang.Object[]>>();
	 List<java.lang.Object[]> dynamicFieldValueList ;
	 Map<String,String> dynamicFieldCodeAndNamesMap = new HashMap<String, String>();
	List<String> selectedDynamicFieldCodeList = new ArrayList<String>();
	 String selectedDynFields = "";
	@SuppressWarnings("unchecked")
	public List<Map> formResponseFarm(List<java.lang.Object[]> farms) {
		List farmMap = new ArrayList<>();
		
		ESESystem preferences = preferncesService.findPrefernceById("1");
		
		if (!StringUtil.isEmpty(preferences)) {
			selectedDynFields = preferences.getPreferences().get(ESESystem.SELECTED_DYN_FIELDS_WHILE_FARM_DOWNLOAD);
		}
		
		 dynamicFieldValueMap = new HashMap<Integer,  List<java.lang.Object[]>>();
		if(selectedDynFields != null && !StringUtil.isEmpty(selectedDynFields)){
			String str[] = selectedDynFields.split(",");
			selectedDynamicFieldCodeList = Arrays.asList(str);
			
			List<java.lang.Object[]> dynamicFieldNames = farmerService.listDynamicFieldsCodeAndName();
			dynamicFieldNames.parallelStream().forEachOrdered(i -> {
				dynamicFieldCodeAndNamesMap.put(String.valueOf(i[0]), String.valueOf(i[1]));
			});
			
			List<String> farmIdList = new ArrayList<String>();
			farms.parallelStream().forEachOrdered(i -> farmIdList.add(String.valueOf(i[0])));
			
			
			 dynamicValues = farmerService.listFarmerDynamicFieldsValuesByFarmIdList(farmIdList,selectedDynamicFieldCodeList);//selectedDynamicFieldCodeList have more values then this query takes more time to execute
			 
			
			 dynamicValues.stream().forEachOrdered(i -> {
				  dynamicFieldValueList = dynamicFieldValueMap.get(Integer.valueOf(String.valueOf(i[4])));
				 if (dynamicFieldValueList == null || dynamicFieldValueList.isEmpty()) {
					 dynamicFieldValueList = new ArrayList<java.lang.Object[]>();
					}
				 	dynamicFieldValueList.add(i);
				 	dynamicFieldValueMap.put(Integer.valueOf(String.valueOf(i[4])), dynamicFieldValueList);
			 });
			 
			//0 - fdfv.FIELD_NAME
			//1 - fdfv.FIELD_VALUE
			//2 - fdfv.COMPONENT_TYPE
			//3 - fdfv.TYPEZ
			//4 - fdfv.REFERENCE_ID
			//5 - fdfv.TXN_TYPE
			//6 - fdfv.FARMER_DYNAMIC_DATA_ID
			//7 - fdfv.ACCESS_TYPE
			//8 - fdfv.PARENT_ID
			//9 - fic.INSPECTION_DATE
			//10 - fic.INSPECTOR_NAME
			//11 - fic.INSPECTION_TYPE
			//12 - fic.TOTAL_LANDHOLD
			//13 - fic.LAND_ORGANIC
		}
		
		
		
		
		if (!ObjectUtil.isListEmpty(farms)) {
			Map<String,String> insDateMap=new LinkedHashMap<>();
			List<java.lang.Object[]> insDateList=farmerService.listFarmsLastInspectionDate();
			if(insDateList!=null && !ObjectUtil.isListEmpty(insDateList)){
				insDateMap=insDateList.stream().collect(Collectors.toMap(k->k[1].toString(), v->v[0].toString()));
			}
			for (java.lang.Object[] farm : farms) {
				Map objectMap = new LinkedHashMap();
				objectMap.put(TxnEnrollmentProperties.FARM_CODE, farm[1]);
				objectMap.put(TxnEnrollmentProperties.FARM_NAME, farm[2]);
				objectMap.put(TxnEnrollmentProperties.FARMER_ID, farm[6]);
				objectMap.put(TxnEnrollmentProperties.FARM_ID, farm[3]);
				objectMap.put(TxnEnrollmentProperties.FARM_STATUS, farm[9]);
				 objectMap.put(TxnEnrollmentProperties.FARM_LATITUDE,
						 farm[11]);
						 objectMap.put(TxnEnrollmentProperties.FARM_LONGITUDE,
								 farm[12]);
				if(ObjectUtil.isEmpty(farm[11]) && ObjectUtil.isEmpty(farm[12])){
					objectMap.put(TxnEnrollmentProperties.GEO_STATUS, 0);
				}
				else{
					objectMap.put(TxnEnrollmentProperties.GEO_STATUS, 1);
				}
				objectMap.put(TxnEnrollmentProperties.PLOTTING_STATUS, farm[10]);
				objectMap.put(TxnEnrollmentProperties.CHEMICAL_APPLICATION_LAST_DATE, farm[4]);
				if (tenantId.equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)) {
				objectMap.put(TxnEnrollmentProperties.Inspection_Date, insDateMap.containsKey(farm[1])?insDateMap.get(farm[1]):"");
				}
				// int farmCropsCount = (Integer) farm[7];
				int farmIcs = (Integer) farm[7];
				// int cows = (Integer) farm[7];
				// objectMap.put(TxnEnrollmentProperties.CONVERSION_STS, "");
				icstype = "";
				if (farmIcs > 0) {
					if (farmICSConversionList.size() >= 0) {
						farmICSConversionList = farmerService.listFarmIcsByFarmId();
					}
					if (!ObjectUtil.isListEmpty(farmICSConversionList)) {
						farmICSConversionList.stream()
								.filter(ics -> ics!=null && !ObjectUtil.isEmpty(ics) && ics[0].toString().equalsIgnoreCase(farm[0].toString())).forEach(ics -> {
										icstype =ics[1]!=null && !ObjectUtil.isEmpty(ics[1])?ics[1].toString():null;
								});
					}
				}
				objectMap.put(TxnEnrollmentProperties.CONVERSION_STS, icstype);

				objectMap.put(TxnEnrollmentProperties.FARM_VERIFIED, farm[5]);
				objectMap.put(TxnEnrollmentProperties.PROPOSED_PLANTING_AREA, farm[13]);
				objectMap.put(TxnEnrollmentProperties.Total_Land_Holding, farm[14]);
				Collection cowCollection = new Collection();
				List<Object> cowObjects = new ArrayList<Object>();
				/*
				 * if (cows > 0) { List<Cow> cowListSet =
				 * farmerService.listCowFieldsByFarmId(Long.valueOf(farm[0].
				 * toString())); if (!ObjectUtil.isListEmpty(cowListSet)) { for
				 * (Cow cow : cowListSet) { Map objectMapForFarm =
				 * formResponse(cow); Object cowObject = new Object();
				 * cowObject.setData(CollectionUtil.mapToList(objectMapForFarm))
				 * ; cowObjects.add(cowObject); } }
				 * 
				 * }
				 */
				cowCollection.setObject(cowObjects);

				objectMap.put(TransactionProperties.COW_LIST, cowCollection);
				farmMap.add(objectMap);
				
				
				if(selectedDynFields != null && !StringUtil.isEmpty(selectedDynFields)){
					if(dynamicValues != null && !dynamicValues.isEmpty()){
						
						List<List<JSONObject>> collectionlist = new ArrayList<List<JSONObject>>();
							Object langMasterObject = new Object();
							if (dynamicFieldValueMap.get(Integer.valueOf(String.valueOf(farm[0]))) != null) {
								

								Map<Integer, List<JSONObject>> collectionMap = new HashMap<Integer, List<JSONObject>>();
								Map<Integer, List<JSONObject>> nonCollectionMap = new HashMap<Integer, List<JSONObject>>();
								List<List<JSONObject>> nonCollectionlist = new ArrayList<List<JSONObject>>();
								
								
								java.lang.Object[] staticData =  dynamicFieldValueMap.get(Integer.valueOf(String.valueOf(farm[0]))).get(0);
								
								
								if(staticData.length >= 14){
									objectMap.put(TxnEnrollmentProperties.Inspection_Date, DateUtil.convertDateToString((Date) staticData[9],getGeneralDateFormat()));
									objectMap.put(TxnEnrollmentProperties.Inspector_Name,staticData[10]);
									objectMap.put(TxnEnrollmentProperties.Inspection_Type,staticData[11] != null ? getCatlogueValueByCode(staticData[11].toString()).getName() : "");
									objectMap.put(TxnEnrollmentProperties.Total_Land_Holding,staticData[12]);
									objectMap.put(TxnEnrollmentProperties.Land_under_organic_Programme,staticData[13]);
								}
								
								// below functionality for nonCollection (Component type != 8) fields
								dynamicFieldValueMap.get(Integer.valueOf(String.valueOf(farm[0]))).stream().filter(
										obj -> (obj[8] == null || String.valueOf(obj[8]).equalsIgnoreCase("0")) && selectedDynamicFieldCodeList.contains(obj[0].toString())).forEachOrdered(i -> {
											
											List<JSONObject> temp = nonCollectionMap.get(Integer.valueOf(String.valueOf(i[6])));
											if (temp == null || temp.isEmpty()) {
												temp = new ArrayList<JSONObject>();
											}
											JSONObject dynamicFields = new JSONObject();
											if(dynamicFieldCodeAndNamesMap.get(String.valueOf(i[0])) != null){
												dynamicFields.put(dynamicFieldCodeAndNamesMap.get(String.valueOf(i[0])), i[1]);
											}else{
												dynamicFields.put(i[0], i[1]);
											}
											
											temp.add(dynamicFields);

											nonCollectionMap.put(Integer.valueOf(String.valueOf(i[6])), temp);
										});

								nonCollectionMap.entrySet().stream().forEachOrdered(i -> {
									nonCollectionlist.add(i.getValue());
								});

								objectMap.put(TxnEnrollmentProperties.dynfield, nonCollectionlist);
								
								
								// below functionality for Collection (Component type == 8) fields
								
								dynamicFieldValueMap.get(Integer.valueOf(String.valueOf(farm[0]))).stream().filter(
										obj -> (obj[8] != null && !String.valueOf(obj[8]).equalsIgnoreCase("0") && obj[3] != null) && selectedDynamicFieldCodeList.contains(obj[0].toString()))
										.sorted((p1, p2) -> (Integer.valueOf(String.valueOf(p1[3]))).compareTo((Integer.valueOf(String.valueOf(p2[3])))))
										.forEachOrdered(i -> {
											
											List<JSONObject> temp = collectionMap.get(Integer.valueOf(String.valueOf(i[3])));
											if (temp == null || temp.isEmpty()) {
												temp = new ArrayList<JSONObject>();
											}
											JSONObject dynamicFields = new JSONObject();
											if(dynamicFieldCodeAndNamesMap.get(String.valueOf(i[0])) != null){
												dynamicFields.put(dynamicFieldCodeAndNamesMap.get(String.valueOf(i[0])), i[1]);
											}else{
												dynamicFields.put(i[0], i[1]);
											}
											temp.add(dynamicFields);

											collectionMap.put(Integer.valueOf(String.valueOf(i[3])), temp);
										});

								collectionMap.entrySet().stream().forEachOrdered(i -> {
									collectionlist.add(i.getValue());
								});

								objectMap.put(TxnEnrollmentProperties.List_collection, collectionlist);
								
								

							}else{
								List<List<JSONObject>> emptylist = new ArrayList<List<JSONObject>>();
								objectMap.put(TxnEnrollmentProperties.dynfield, emptylist);
								objectMap.put(TxnEnrollmentProperties.List_collection, emptylist);
								
							}

						
					
				}
			}
			}

		}
		return farmMap;
	}

	@SuppressWarnings("unchecked")
	public Map formResponse(java.lang.Object object) {

		Map objectMap = new LinkedHashMap();
		if (object instanceof Farmer) {
			Farmer farmer = (Farmer) object;
			objectMap.put(TxnEnrollmentProperties.FARMER_ID,
					(StringUtil.isEmpty(farmer.getFarmerId()) ? "" : farmer.getFarmerId()));
			if (tenantId.equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
				objectMap.put(TxnEnrollmentProperties.FARMER_CODE,
						(StringUtil.isEmpty(farmer.getTraceId()) ? "" : farmer.getTraceId()));

			} else {
				objectMap.put(TxnEnrollmentProperties.FARMER_CODE,
						(StringUtil.isEmpty(farmer.getFarmerCode()) ? "" : farmer.getFarmerCode()));
			}
			objectMap.put(TxnEnrollmentProperties.FIRST_NAME,
					(StringUtil.isEmpty(farmer.getFirstName()) ? "" : farmer.getFirstName()));
			objectMap.put(TxnEnrollmentProperties.LAST_NAME,
					(StringUtil.isEmpty(farmer.getLastName()) ? "" : farmer.getLastName()));
			objectMap.put(TxnEnrollmentProperties.VILLAGE_CODE, !ObjectUtil.isEmpty(farmer.getVillage())
					? !StringUtil.isEmpty(farmer.getVillage().getCode()) ? farmer.getVillage().getCode() : "" : "");
			objectMap.put(TxnEnrollmentProperties.SAMITHI_CODE, !ObjectUtil.isEmpty(farmer.getSamithi())
					? !StringUtil.isEmpty(farmer.getSamithi().getCode()) ? farmer.getSamithi().getCode() : "" : "");
			objectMap.put(TxnEnrollmentProperties.FARMER_CERTIFIED, farmer.getIsCertifiedFarmer());
			objectMap.put(TxnEnrollmentProperties.FARMER_CERTIFICATION_TYPE, farmer.getCertificationType());
			objectMap.put(TxnEnrollmentProperties.UTZ_STATUS, farmer.getUtzStatus());
			objectMap.put(TxnEnrollmentProperties.HHID,
					!StringUtil.isEmpty(farmer.getFarmerCode()) ? farmer.getFarmerCode() : "");
			objectMap.put(TxnEnrollmentProperties.FARMER_CODE_BYTRACENET,
					!StringUtil.isEmpty(farmer.getFarmersCodeTracenet()) ? farmer.getFarmersCodeTracenet() : "");
			objectMap.put(TxnEnrollmentProperties.CO_OPERATIVE,
					!StringUtil.isEmpty(farmer.getFpo()) ? farmer.getFpo() : "");

			/*
			 * objectMap.put(TxnEnrollmentProperties.ENROLLMENT_PLACE,
			 * (StringUtil.isEmpty(farmer.getEnrollmentPlace()) ? "" :
			 * farmer.getEnrollmentPlace()));
			 * objectMap.put(TxnEnrollmentProperties.ENROLLMENT_PLACE_OTHER,
			 * (StringUtil.isEmpty(farmer.getEnrollmentPlaceOther()) ? "" :
			 * farmer.getEnrollmentPlaceOther()));
			 * objectMap.put(TxnEnrollmentProperties.SEX,(StringUtil.isEmpty(
			 * farmer.getGender()) ? "" : farmer.getGender()));
			 * objectMap.put(TxnEnrollmentProperties.DOB,
			 * (StringUtil.isEmpty(farmer.getDateOfBirth()) ? "" :
			 * sdf.format(farmer.getDateOfBirth())));
			 * objectMap.put(TxnEnrollmentProperties.MARITAL_STATUS,
			 * farmer.getMaritalSatus());
			 * objectMap.put(TxnEnrollmentProperties.EDUCATION,
			 * (StringUtil.isEmpty(farmer.getEducation()) ? "" :
			 * farmer.getEducation()));
			 * objectMap.put(TxnEnrollmentProperties.ADDRESS,
			 * (StringUtil.isEmpty(farmer.getAddress()) ? "" :
			 * farmer.getAddress()));
			 * objectMap.put(TxnEnrollmentProperties.PHONE_NO,
			 * (StringUtil.isEmpty(farmer.getPhoneNumber()) ? "" :
			 * farmer.getPhoneNumber()));
			 * objectMap.put(TxnEnrollmentProperties.MOBILE_NO,
			 * (StringUtil.isEmpty(farmer.getMobileNumber()) ? "" :
			 * farmer.getMobileNumber()));
			 * objectMap.put(TxnEnrollmentProperties.EMAIL,(StringUtil.isEmpty(
			 * farmer.getEmail()) ? "" : farmer.getEmail()));
			 * objectMap.put(TxnEnrollmentProperties.PHOTO,
			 * getBase64EncodedImageFromByteArray(null));
			 * objectMap.put(TxnEnrollmentProperties.FARMER_PHOTO_CAPTURE_TIME,
			 * (StringUtil.isEmpty(farmer.getPhotoCaptureTime()) ? "" :
			 * sdf1.format(farmer.getPhotoCaptureTime())));
			 * objectMap.put(TxnEnrollmentProperties.LATITUDE,
			 * farmer.getLatitude());
			 * objectMap.put(TxnEnrollmentProperties.LONGITUDE,
			 * farmer.getLongitude()); ESECard card =
			 * cardService.findCardByProfileId(farmer.getFarmerId()); if
			 * (!ObjectUtil.isEmpty(card)) {
			 * objectMap.put(TxnEnrollmentProperties.FARMER_CARD_ID,
			 * card.getCardId()); } else {
			 * objectMap.put(TxnEnrollmentProperties.FARMER_CARD_ID, ""); }
			 * ESEAccount account =
			 * accountService.findAccountByProfileId(farmer.getFarmerId()); if
			 * (!ObjectUtil.isEmpty(account)) {
			 * objectMap.put(TxnEnrollmentProperties.FARMER_ACCOUNT_NO,
			 * account.getAccountNo()); } else {
			 * objectMap.put(TxnEnrollmentProperties.FARMER_ACCOUNT_NO, ""); }
			 * // objectMap.put(TxnEnrollmentProperties.FARMER_VERIFIED,
			 * farmer.getIsVerified());
			 */

			Set<Farm> farmListSet = farmer.getFarms();
			Collection farmCollection = new Collection();
			List<Object> farmObjects = new ArrayList<Object>();
			// Set<Farm> farmListSet = null;
			if (!ObjectUtil.isListEmpty(farmListSet)) {
				for (Farm farm : farmListSet) {
					Map objectMapForFarm = formResponse(farm);
					Object farmObject = new Object();
					farmObject.setData(CollectionUtil.mapToList(objectMapForFarm));

					farmObjects.add(farmObject);
				}
			}
			farmCollection.setObject(farmObjects);
			objectMap.put(TxnEnrollmentProperties.FARM_LIST, farmCollection);
		} else if (object instanceof Farm) {
			Farm farm = (Farm) object;
			FarmIcsConversion conversion = new FarmIcsConversion();
			objectMap.put(TxnEnrollmentProperties.FARM_CODE, farm.getFarmCode());
			objectMap.put(TxnEnrollmentProperties.FARM_NAME, farm.getFarmName());

			objectMap.put(TxnEnrollmentProperties.FARM_ID, farm.getFarmId() == null ? "" : farm.getFarmId());

			objectMap.put(TxnEnrollmentProperties.CHEMICAL_APPLICATION_LAST_DATE,
					farm.getFarmDetailedInfo().getLastDateOfChemicalApplication());

			if (!ObjectUtil.isListEmpty(farm.getFarmICSConversion())) {
				objectMap.put(TxnEnrollmentProperties.CONVERSION_STS,
						farm.getFarmICSConversion().iterator().next().getIcsType());
			} else {
				objectMap.put(TxnEnrollmentProperties.CONVERSION_STS, "");
			}
			// FarmDetailedInfo detailInfo = new FarmDetailedInfo();
			// detailInfo.setLastDateOfChemicalApplication(TxnEnrollmentProperties.CHEMICAL_APPLICATION_LAST_DATE,farm.getFarmDetailedInfo().getLastDateOfChemicalApplication());
			// objectMap.put(TxnEnrollmentProperties.FARM_LATITUDE,
			// farm.getLatitude());
			// objectMap.put(TxnEnrollmentProperties.FARM_LONGITUDE,
			// farm.getLongitude());
			objectMap.put(TxnEnrollmentProperties.FARM_VERIFIED,
					ObjectUtil.isEmpty(farm.getIsVerified()) ? "0" : farm.getIsVerified());
			
			objectMap.put(TxnEnrollmentProperties.PLOTTING_STATUS, farm.getPlottingStatus());
			if (tenantId.equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)) {

				objectMap.put(TxnEnrollmentProperties.PRE_BANANA_TREE,
						!StringUtil.isEmpty(farm.getPresenceBananaTrees()) ? farm.getPresenceBananaTrees() : "");
				objectMap.put(TxnEnrollmentProperties.PARALLEL_PROD,
						!StringUtil.isEmpty(farm.getParallelProd()) ? farm.getParallelProd() : "");
				objectMap.put(TxnEnrollmentProperties.INPUT_ORG_UNIT,
						!StringUtil.isEmpty(farm.getInputOrganicUnit()) ? farm.getInputOrganicUnit() : "");
				objectMap.put(TxnEnrollmentProperties.PRE_HIRED_LABOUR,
						!StringUtil.isEmpty(farm.getPresenceHiredLabour()) ? farm.getPresenceHiredLabour() : "");
				objectMap.put(TxnEnrollmentProperties.RISK_CATE,
						!StringUtil.isEmpty(farm.getRiskCategory()) ? farm.getRiskCategory() : "");

			}

			/*
			 * List<java.lang.Object[]> fertPestList = farmerService
			 * .listFertilizerAppliedAndPestAppliedWithQty(farm.getFarmCode());
			 * 
			 * Collection fertCollection = new Collection(); Collection
			 * pestCollection = new Collection(); Collection manureCollection =
			 * new Collection(); List<Object> fertObjects = new
			 * ArrayList<Object>(); List<Object> pestObjects = new
			 * ArrayList<Object>(); List<Object> manureObjects = new
			 * ArrayList<Object>();
			 * 
			 * if (fertPestList.size() > 0) { for (java.lang.Object[] strAry :
			 * fertPestList) { if
			 * (strAry[2].toString().equals(PeriodicInspection.
			 * FERTILIZER_APPLIED) && Double.valueOf(strAry[0].toString()) > 0)
			 * {
			 * 
			 * Map fertMap = new HashMap();
			 * fertMap.put(TxnEnrollmentProperties.FERT, strAry[1].toString());
			 * fertMap.put(TxnEnrollmentProperties.FERT_QTY,
			 * strAry[0].toString());
			 * fertMap.put(TxnEnrollmentProperties.COMPLETED,
			 * strAry[4].toString()); Object farmCropObject = new Object();
			 * farmCropObject.setData(CollectionUtil.mapToList(fertMap));
			 * fertObjects.add(farmCropObject); }
			 * 
			 * if (strAry[2].toString().equals(PeriodicInspection.
			 * PESTICIDE_RECOMMENTED)) { Map pestMap = new LinkedHashMap();
			 * pestMap.put(TxnEnrollmentProperties.PEST_FUNGISIDE,
			 * strAry[1].toString());
			 * pestMap.put(TxnEnrollmentProperties.PEST_FUNGISIDE_QTY,
			 * strAry[0].toString()); Object farmCropObject = new Object();
			 * farmCropObject.setData(CollectionUtil.mapToList(pestMap));
			 * pestObjects.add(farmCropObject); }
			 * if(strAry[2].toString().equals(PeriodicInspection.MANURE_APPLIED)
			 * ) { Map manureMap = new LinkedHashMap();
			 * manureMap.put(TxnEnrollmentProperties.MANURE_FUNGISIDE,
			 * strAry[1].toString());
			 * manureMap.put(TxnEnrollmentProperties.MANURE_FUNGISIDE_QTY,
			 * strAry[0].toString()); Object farmCropObject = new Object();
			 * farmCropObject.setData(CollectionUtil.mapToList(manureMap));
			 * manureObjects.add(farmCropObject); } } }
			 * fertCollection.setObject(fertObjects);
			 * pestCollection.setObject(pestObjects);
			 * manureCollection.setObject(manureObjects);
			 * objectMap.put(TxnEnrollmentProperties.PEST_APP, pestCollection);
			 * objectMap.put(TxnEnrollmentProperties.FERTILIZER_APP,
			 * fertCollection); objectMap.put(TxnEnrollmentProperties.MANUR_APP,
			 * manureCollection);
			 */

			Set<Cow> cowListSet = farm.getCows();
			Collection cowCollection = new Collection();
			List<Object> cowObjects = new ArrayList<Object>();
			if (!ObjectUtil.isListEmpty(cowListSet)) {
				for (Cow cow : cowListSet) {
					Map objectMapForFarm = formResponse(cow);
					Object cowObject = new Object();
					cowObject.setData(CollectionUtil.mapToList(objectMapForFarm));
					cowObjects.add(cowObject);
				}
			}
			cowCollection.setObject(cowObjects);
			objectMap.put(TransactionProperties.COW_LIST, cowCollection);

		}

		else if (object instanceof FarmCrops) {
			FarmCrops farmCrops = (FarmCrops) object;
			// objectMap.put(TxnEnrollmentProperties.FARM_CROPS_CODE,
			// (ObjectUtil.isEmpty(farmCrops.getFarmCropsMaster()) ? "" :
			// farmCrops.getFarmCropsMaster().getCode()));
			// objectMap.put(TxnEnrollmentProperties.FARM_CROPS_NAME,
			// (ObjectUtil.isEmpty(farmCrops.getFarmCropsMaster()) ? "" :
			// farmCrops.getFarmCropsMaster().getName()));
			// objectMap.put(TxnEnrollmentProperties.CROP_AREA,
			// farmCrops.getCropArea());
			objectMap.put(TxnEnrollmentProperties.FARM_CROPS_CODE,
					ObjectUtil.isEmpty(farmCrops.getProcurementVariety()) || farmCrops.getProcurementVariety() == null
							? "" : farmCrops.getProcurementVariety().getCode());
			objectMap.put(TxnEnrollmentProperties.CROP_ID,
					(ObjectUtil.isEmpty(farmCrops.getProcurementVariety()) || farmCrops.getProcurementVariety() == null
							? "" : farmCrops.getProcurementVariety().getProcurementProduct().getCode()));
			objectMap.put(TxnEnrollmentProperties.SEED_SOURCE, String.valueOf(farmCrops.getSeedSource()));
			objectMap.put(TxnEnrollmentProperties.SEED_COST_MAIN, String.valueOf(farmCrops.getSeedQtyCost()));
			objectMap.put(TxnEnrollmentProperties.PRODUCTION_YEAR, farmCrops.getEstimatedYield());
			objectMap.put(TxnEnrollmentProperties.CROP_SEASON, String.valueOf(
					!ObjectUtil.isEmpty(farmCrops.getCropSeason()) ? farmCrops.getCropSeason().getCode() : ""));
			objectMap.put(TxnEnrollmentProperties.CROP_CATEGORY, String.valueOf(farmCrops.getCropCategory()));
			objectMap.put(TxnEnrollmentProperties.FARM_CODE_REFERENCE,
					(ObjectUtil.isEmpty(farmCrops.getFarm()) ? "" : farmCrops.getFarm().getFarmCode()));
			objectMap.put(TxnEnrollmentProperties.FARMER_CODE_REFERENCE_IN_FARM_CROPS,
					(ObjectUtil.isEmpty(farmCrops.getFarm()) && ObjectUtil.isEmpty(farmCrops.getFarm().getFarmer()) ? ""
							: farmCrops.getFarm().getFarmer().getFarmerId()));

			objectMap.put(TxnEnrollmentProperties.RISK_ASSESMENT,
					farmCrops.getRiskAssesment() == null ? "" : farmCrops.getRiskAssesment());
			objectMap.put(TxnEnrollmentProperties.RISK_BUFFER_ZONE_DISTANCE,
					farmCrops.getRiskAssesment() == null ? "" : farmCrops.getRiskAssesment());
			objectMap.put(TxnEnrollmentProperties.SEED_TREATMENT_DETAILS,
					farmCrops.getSeedTreatmentDetails() == null ? "" : farmCrops.getSeedTreatmentDetails());
			objectMap.put(TxnEnrollmentProperties.OTHER_SEED_TREATMENT_DETAILS,
					farmCrops.getOtherSeedTreatmentDetails() == null ? "" : farmCrops.getOtherSeedTreatmentDetails());
			objectMap.put(TxnEnrollmentProperties.STAPLE_LENGTH_MAIN,
					farmCrops.getStapleLength() == null ? "" : farmCrops.getStapleLength());
			objectMap.put(TxnEnrollmentProperties.SEED_USED_MAIN,
					(ObjectUtil.isEmpty(farmCrops.getSeedQtyUsed()) && farmCrops.getSeedQtyUsed() == null) ? ""
							: farmCrops.getSeedQtyUsed());
			objectMap.put(TxnEnrollmentProperties.FARM_CROP_TYPE,
					(StringUtil.isEmpty(farmCrops.getType()) && farmCrops.getType() == null) ? ""
							: farmCrops.getType());
			objectMap.put(TxnEnrollmentProperties.CULTI_TYPE,
					(StringUtil.isEmpty(farmCrops.getCropCategoryList()) && farmCrops.getCropCategoryList() == null)
							? "" : farmCrops.getCropCategoryList());
			objectMap.put(TxnEnrollmentProperties.CULTIVATION_AREA,
					(StringUtil.isEmpty(farmCrops.getCultiArea()) && farmCrops.getCultiArea() == null) ? ""
							: farmCrops.getCultiArea());
			if (!ObjectUtil.isEmpty(farmCrops.getSowingDate())) {
				// String dat[]=farmCrops.getSowingDate().toString().split(" ");
				// objectMap.put(TxnEnrollmentProperties.SOW_DATE,
				// Date.valueOf(dat[0]));
				objectMap.put(TxnEnrollmentProperties.SOW_DATE, (StringUtil.isEmpty(farmCrops.getSowingDate())) ? ""
						: DateUtil.convertDateToString(farmCrops.getSowingDate(), DateUtil.DATE));
			}
			if (!ObjectUtil.isEmpty(farmCrops.getEstimatedHarvestDate())) {
				// String
				// eDate[]=farmCrops.getEstimatedHarvestDate().toString().split("
				// ");

				// objectMap.put(TxnEnrollmentProperties.ESTIMATE_HARVEST_DATE,
				// Date.valueOf(eDate[0]));
				objectMap.put(TxnEnrollmentProperties.ESTIMATE_HARVEST_DATE,
						(StringUtil.isEmpty(farmCrops.getEstimatedHarvestDate())) ? ""
								: DateUtil.convertDateToString(farmCrops.getEstimatedHarvestDate(), DateUtil.DATE));
			}
		}

		else if (object instanceof Cow) {

			Cow cow = (Cow) object;
			objectMap.put(TxnEnrollmentProperties.COW_ID, (ObjectUtil.isEmpty(cow.getCowId()) ? "" : cow.getCowId()));
			objectMap.put(TxnEnrollmentProperties.LACTATION_NUMBER,
					(ObjectUtil.isEmpty(cow.getLactationNo()) ? "" : cow.getLactationNo()));
			Date inspDate = farmerService.findByLastInspDate(cow.getCowId());
			objectMap.put(TxnEnrollmentProperties.INSPECTION_DATE,
					(ObjectUtil.isEmpty(inspDate) ? "" : DateUtil.convertDateToString(inspDate, DateUtil.DATE)));
			Set<Calf> calfListSet = cow.getCalfs();
			Collection calfCollection = new Collection();
			List<Object> calfObjects = new ArrayList<Object>();
			if (!ObjectUtil.isListEmpty(calfListSet)) {
				for (Calf calf : calfListSet) {
					Map objectMapForFarm = formResponse(calf);
					Object calfObject = new Object();
					calfObject.setData(CollectionUtil.mapToList(objectMapForFarm));
					calfObjects.add(calfObject);
				}
			}
			calfCollection.setObject(calfObjects);
			objectMap.put(TransactionProperties.CALF_LIST, calfCollection);

		}

		else if (object instanceof Calf) {

			Calf calf = (Calf) object;
			objectMap.put(TxnEnrollmentProperties.CALF_ID,
					(ObjectUtil.isEmpty(calf.getCalfId()) ? "" : calf.getCalfId()));
			objectMap.put(TxnEnrollmentProperties.SEX, (ObjectUtil.isEmpty(calf.getGender()) ? "" : calf.getGender()));
			objectMap.put(TxnEnrollmentProperties.WEIGTH,
					(ObjectUtil.isEmpty(calf.getBirthWeight()) ? "" : calf.getBirthWeight()));
			objectMap.put(TxnEnrollmentProperties.INTERVAL,
					(ObjectUtil.isEmpty(calf.getCalvingIntvalDays()) ? "" : calf.getCalvingIntvalDays()));
			objectMap.put(TxnEnrollmentProperties.DELIVERY,
					(ObjectUtil.isEmpty(calf.getDeliveryProcess()) ? "" : calf.getDeliveryProcess()));

			objectMap.put(TxnEnrollmentProperties.BULL_ID,
					(ObjectUtil.isEmpty(calf.getBullId()) ? "" : calf.getBullId()));
			objectMap.put(TxnEnrollmentProperties.SERVICE_DATE, (ObjectUtil.isEmpty(calf.getServiceDate()) ? ""
					: DateUtil.convertDateToString(calf.getServiceDate(), DateUtil.TXN_TIME_FORMAT)));
			objectMap.put(TxnEnrollmentProperties.CALVING_DATE, (ObjectUtil.isEmpty(calf.getLastDateCalving()) ? ""
					: DateUtil.convertDateToString(calf.getLastDateCalving(), DateUtil.TXN_TIME_FORMAT)));
		}

		return objectMap;
	}

	/**
	 * Gets the agent service.
	 * 
	 * @return the agent service
	 */
	public IAgentService getAgentService() {

		return agentService;
	}

	/**
	 * Sets the agent service.
	 * 
	 * @param agentService
	 *            the new agent service
	 */
	public void setAgentService(IAgentService agentService) {

		this.agentService = agentService;
	}

	/**
	 * Sets the farmer service.
	 * 
	 * @param farmerService
	 *            the new farmer service
	 */
	public void setFarmerService(IFarmerService farmerService) {

		this.farmerService = farmerService;
	}

	public String getTenantId() {

		return tenantId;
	}

	public void setTenantId(String tenantId) {

		this.tenantId = tenantId;
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
	
	public String getGeneralDateFormat() {
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			return preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT);
		}
		return null;

	}
	
	public ICatalogueService getCatalogueService() {
		return catalogueService;
	}

	public void setCatalogueService(ICatalogueService catalogueService) {
		this.catalogueService = catalogueService;
	}

	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}
	
}
