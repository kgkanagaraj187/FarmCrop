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
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IFarmerService;
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

@Component
public class FarmCropsDownload implements ITxnAdapter {

	private static final Logger LOGGER = Logger.getLogger(FarmCropsDownload.class.getName());
	private static final String FARMER_DOB = "yyyyMMdd";
	@Autowired
	private IFarmerService farmerService;

	@Autowired
	private IAgentService agentService;
	@Autowired
	private IClientService clientService;
	private String tenantId;


	SimpleDateFormat sdf = new SimpleDateFormat(FARMER_DOB);
	SimpleDateFormat sdf1 = new SimpleDateFormat(DateUtil.TXN_DATE_TIME);
	SimpleDateFormat sdfTxnDate = new SimpleDateFormat(DateUtil.DATABASE_DATE_FORMAT);

	List<java.lang.Object[]> farmICSConversionList = new ArrayList<>();
	Map<Long, List<java.lang.Object[]>> farmObject = new HashMap<>();
	Map<Long, List<java.lang.Object[]>> farmCropsObject = new HashMap<>();

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

		String revisionNo = (String) reqData.get(TxnEnrollmentProperties.FARM_CROPS_DOWNLOAD_REVISION_NO);
		if (StringUtil.isEmpty(revisionNo))
			throw new SwitchException(ITxnErrorCodes.EMPTY_FARMCROP_REVISION_NO);

		LOGGER.info("REVISION NO" + revisionNo);
		List<java.lang.Object[]> farmerList = new ArrayList<java.lang.Object[]>();

		Collection farmerCollection = new Collection();
		 String seasonCode = clientService.findCurrentSeasonCodeByBranchId(head.getBranchId());
		List<Object> farmerObjects = new ArrayList<Object>();
		/** FORMING FARMER OBJECT **/
		if (!ObjectUtil.isListEmpty(agent.getWareHouses())) {
			 farmerList = head.getTenantId().equals("chetna") || head.getTenantId().equals(ESESystem.WELSPUN_TENANT_ID) ? this.farmerService.listFarmCropsFieldsByFarmerIdAgentIdAndSeason(agent.getId(), Long.valueOf(revisionNo), seasonCode) : this.farmerService.listFarmCropsFieldsByFarmerIdByAgentIdAndSeason(agent.getId(), Long.valueOf(revisionNo));
		if (!ObjectUtil.isListEmpty(farmerList)) {
			farmerList.stream().forEach(farmer -> {
				Object farmerObject = new Object();
				farmerObject.setData(CollectionUtil.mapToList(formResponseFarmCrops(farmer)));
			farmerObjects.add(farmerObject);
			});

				
			}

		}

		farmerCollection.setObject(farmerObjects);
		Map resp = new LinkedHashMap();
		resp.put(TxnEnrollmentProperties.FARM_CROPS_LIST, farmerCollection);
		if(!farmerList.isEmpty()){
			resp.put(TxnEnrollmentProperties.FARM_CROPS_DOWNLOAD_REVISION_NO, farmerList.get(0)[farmerList.get(0).length-1]);
		}else{
			resp.put(TxnEnrollmentProperties.FARM_CROPS_DOWNLOAD_REVISION_NO, "0");
		}
		

		return resp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.txn.adapter.ITxnAdapt7er#processVoid(java.util.Map)
	 */
	@Override
	public Map<?, ?> processVoid(Map<?, ?> reqData) {

		return null;
	}

	String icstype = "";

	@SuppressWarnings("unchecked")
	public Map formResponseFarm(List<java.lang.Object[]> farms) {
		Map objectMap = new LinkedHashMap();
		if (!ObjectUtil.isListEmpty(farms)) {
			for (java.lang.Object[] farm : farms) {
				objectMap.put(TxnEnrollmentProperties.FARM_CODE, farm[1]);
				objectMap.put(TxnEnrollmentProperties.FARM_NAME, farm[2]);

				objectMap.put(TxnEnrollmentProperties.FARM_ID, farm[3]);

				objectMap.put(TxnEnrollmentProperties.CHEMICAL_APPLICATION_LAST_DATE, farm[4]);
				// int farmCropsCount = (Integer) farm[7];
				int farmIcs = (Integer) farm[8];
				int cows = (Integer) farm[7];
				objectMap.put(TxnEnrollmentProperties.CONVERSION_STS, "");
				if (farmIcs > 0) {
					if (farmICSConversionList.size() <= 0) {
						farmICSConversionList = farmerService.listFarmIcsByFarmId();
					}
					icstype = "";
					farmICSConversionList.stream()
							.filter(ics -> Long.valueOf(ics[0].toString()) == Long.valueOf(farm[0].toString()))
							.forEach(ics -> {
								if (!ObjectUtil.isEmpty(ics)) {
									icstype = ics[1].toString();
								}
							});
				}
				objectMap.put(TxnEnrollmentProperties.CONVERSION_STS, icstype);

				objectMap.put(TxnEnrollmentProperties.FARM_VERIFIED, farm[5]);

				Collection cowCollection = new Collection();
				List<Object> cowObjects = new ArrayList<Object>();
				if (cows > 0) {
					List<Cow> cowListSet = farmerService.listCowFieldsByFarmId(Long.valueOf(farm[0].toString()));
					if (!ObjectUtil.isListEmpty(cowListSet)) {
						for (Cow cow : cowListSet) {
							Map objectMapForFarm = formResponse(cow);
							Object cowObject = new Object();
							cowObject.setData(CollectionUtil.mapToList(objectMapForFarm));
							cowObjects.add(cowObject);
						}
					}

				}
				cowCollection.setObject(cowObjects);

				objectMap.put(TransactionProperties.COW_LIST, cowCollection);

			}

		}
		return objectMap;
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
				objectMap.put(TxnEnrollmentProperties.FARM_VERIFIED,
					ObjectUtil.isEmpty(farm.getIsVerified()) ? "0" : farm.getIsVerified());

		
			Set<FarmCrops> farmCropListSet = farm.getFarmCrops();
			Collection farmCropCollection = new Collection();
			List<Object> farmCropObjects = new ArrayList<Object>();
			if (!ObjectUtil.isListEmpty(farmCropListSet)) {
				for (FarmCrops farmCrops : farmCropListSet) {
					Map objectMapForFarm = formResponse(farmCrops);
					Object farmCropObject = new Object();
					farmCropObject.setData(CollectionUtil.mapToList(objectMapForFarm));
					farmCropObjects.add(farmCropObject);
				}
			}
			farmCropCollection.setObject(farmCropObjects);
			objectMap.put(TxnEnrollmentProperties.FARM_CROPS_LIST, farmCropCollection);

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

	public Map formResponseFarmCrops(java.lang.Object[] object) {
		Map objectMap = new LinkedHashMap();
		java.lang.Object[] farmCrops = (java.lang.Object[]) object;

		objectMap.put(TxnEnrollmentProperties.FARM_CROPS_CODE, farmCrops[1]);
		objectMap.put(TxnEnrollmentProperties.FARM_CROP_ID, farmCrops[0]);
		objectMap.put(TxnEnrollmentProperties.CROP_ID, farmCrops[2]);
		objectMap.put(TxnEnrollmentProperties.SEED_SOURCE, farmCrops[3]);
		objectMap.put(TxnEnrollmentProperties.SEED_COST_MAIN, farmCrops[4]);
		objectMap.put(TxnEnrollmentProperties.PRODUCTION_YEAR, farmCrops[5]);
		objectMap.put(TxnEnrollmentProperties.CROP_SEASON, farmCrops[6]);
		objectMap.put(TxnEnrollmentProperties.CROP_CATEGORY, farmCrops[7]);
		objectMap.put(TxnEnrollmentProperties.FARM_CODE_REFERENCE, farmCrops[8]);
		objectMap.put(TxnEnrollmentProperties.FARMER_CODE_REFERENCE_IN_FARM_CROPS, farmCrops[9]);
		objectMap.put(TxnEnrollmentProperties.FARMER_ID, farmCrops[9]);
		objectMap.put(TxnEnrollmentProperties.FARM_ID, farmCrops[8]);
		objectMap.put(TxnEnrollmentProperties.RISK_ASSESMENT, farmCrops[10]);
		objectMap.put(TxnEnrollmentProperties.RISK_BUFFER_ZONE_DISTANCE, farmCrops[10]);
		objectMap.put(TxnEnrollmentProperties.SEED_TREATMENT_DETAILS, farmCrops[11]);
		objectMap.put(TxnEnrollmentProperties.OTHER_SEED_TREATMENT_DETAILS, farmCrops[12]);
		objectMap.put(TxnEnrollmentProperties.STAPLE_LENGTH_MAIN, farmCrops[13]);
		objectMap.put(TxnEnrollmentProperties.SEED_USED_MAIN, farmCrops[14]);
		objectMap.put(TxnEnrollmentProperties.FARM_CROP_TYPE, farmCrops[15]);
		objectMap.put(TxnEnrollmentProperties.CULTI_TYPE, farmCrops[16]);
		objectMap.put(TxnEnrollmentProperties.CULTIVATION_AREA, farmCrops[17]);
		objectMap.put(TxnEnrollmentProperties.POST_HARVEST_INTER, farmCrops[20]);
		objectMap.put(TxnEnrollmentProperties.INTER_ACRES, farmCrops[21]);
		objectMap.put(TxnEnrollmentProperties.INTER_CROP, farmCrops[22]);
		objectMap.put(TxnEnrollmentProperties.GROSS_INCOME, farmCrops[23]);
		objectMap.put(TxnEnrollmentProperties.FARM_CROP_STATUS, farmCrops[26]);

		if (!StringUtil.isEmpty(farmCrops[18])) {
			String d = farmCrops[18].toString();
			if (d.length() >= 20) {
				d = d.substring(0, farmCrops[18].toString().length() - 2);
			}
			Date date = DateUtil.convertStringToDate(d, DateUtil.TXN_DATE_TIME);
			objectMap.put(TxnEnrollmentProperties.SOW_DATE, sdfTxnDate.format(date));
		} else {
			objectMap.put(TxnEnrollmentProperties.SOW_DATE, "");
		}

		if (farmCrops[19] != null && StringUtil.isEmpty(farmCrops[19].toString())) {
			// String dat[]=farmCrops.getSowingDate().toString().split(" ");
			// objectMap.put(TxnEnrollmentProperties.SOW_DATE,
			// Date.valueOf(dat[0]));
			objectMap.put(TxnEnrollmentProperties.ESTIMATE_HARVEST_DATE,
					DateUtil.convertStringToDate(farmCrops[19].toString(), DateUtil.DATE));
		} else {
			objectMap.put(TxnEnrollmentProperties.ESTIMATE_HARVEST_DATE, "");
		}
		
		objectMap.put(TxnEnrollmentProperties.CROP_EDIT_STATUS, farmCrops[28]);
		
		return objectMap;
	}

}
