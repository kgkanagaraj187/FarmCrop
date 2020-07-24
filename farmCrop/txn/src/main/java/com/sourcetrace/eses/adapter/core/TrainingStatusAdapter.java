/*
 * TrainingStatusAdapter.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.txn.training.FarmerTraining;
import com.ese.entity.txn.training.Observations;
import com.ese.entity.txn.training.OfflineTrainingStatus;
import com.ese.entity.txn.training.Topic;
import com.ese.entity.txn.training.TrainingMaterial;
import com.ese.entity.txn.training.TrainingMethod;
import com.ese.entity.txn.training.TrainingStatus;
import com.ese.entity.txn.training.TrainingStatusLocation;
import com.ese.entity.txn.training.TrainingTopic;
import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.TransferInfo;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IServicePointService;
import com.sourcetrace.eses.service.ITrainingService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.util.CollectionUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.profile.ServicePoint;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;
import com.sourcetrace.esesw.excep.SwitchException;

@Component
public class TrainingStatusAdapter implements ITxnAdapter {

	@Autowired
	private ITrainingService trainingService;
	@Autowired
	private IProductDistributionService productDistributionService;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private IAgentService agentService;
	@Autowired
	private IDeviceService deviceService;
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private IServicePointService servicePointService;
	private Set<Village> villageSet;
	private Set<TrainingMaterial>trainingMatSet;
	private Set<TrainingMethod>trainingMathodSet;
	private Set<Observations>observationSet;
	private Set<Topic>topicSet;
	private TrainingTopic trainingTopic;
	private String topicName;
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {

		/** FETCHING REQUEST DATA **/
		Head head = (Head) reqData.get(TxnEnrollmentProperties.HEAD);
		String agentId = head.getAgentId();
		String serialNo = head.getSerialNo();
		String servicePointCode = head.getServPointId();
		String date = (String) reqData.get(TxnEnrollmentProperties.TRAINING_STATUS_DATE);
		String receiptNo = (String) reqData.get(TxnEnrollmentProperties.RECEIPT_NO);
		/*
		 * String seasonCode = (String)
		 * reqData.get(TxnEnrollmentProperties.SEASON_CODE_DOWNLOAD);
		 */
		String villageCode = (String) reqData.get(TxnEnrollmentProperties.VILLAGE_COOPERATIVE_CODE);
		//String lgCode = (String) reqData.get(TxnEnrollmentProperties.LEARNING_GROUP_CODE);
		String farmerIds = (String) reqData.get(TxnEnrollmentProperties.FARMER_ID);
		String noOfFarmers = (String) reqData.get(TxnEnrollmentProperties.NO_OF_FAMILY_MEMBERS);
		String remarks = (String) reqData.get(TxnEnrollmentProperties.REMARKS);
		String cSeasonCode = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.CURR_SEASON_CODE)))
				? (String) reqData.get(TxnEnrollmentProperties.CURR_SEASON_CODE) : "";
		String trCode=(String) reqData.get(TxnEnrollmentProperties.TRAINING_CODE);
		String trAssistName=(String) reqData.get(TxnEnrollmentProperties.TRAINING_ASSISTANT_NAME);
		String timeTknForTraining=(String) reqData.get(TxnEnrollmentProperties.TIMETAKEN_FOR_TRAINING);
		String crCode=(String) reqData.get(TxnEnrollmentProperties.CRITERIA_CODE);
		String trMaterialCode=(String) reqData.get(TxnEnrollmentProperties.TRAINING_MATERIAL_CODE);
		String trMethodCode=(String) reqData.get(TxnEnrollmentProperties.TRAINING_METHOD_CODE);
		String trObservationCode=(String) reqData.get(TxnEnrollmentProperties.TRAINING_OBSERVATION_CODE);
		
		if (ESETxn.ONLINE_MODE == Integer.parseInt(head.getMode())) {
			/** VALIDATE REQUEST DATA **/
			if (StringUtil.isEmpty(agentId))
				throw new SwitchException(SwitchErrorCodes.AGENT_ID_EMPTY);
			Agent agent = agentService.findAgentByProfileId(agentId);
			if (ObjectUtil.isEmpty(agent))
				throw new SwitchException(SwitchErrorCodes.INVALID_AGENT);
			if (StringUtil.isEmpty(serialNo))
				throw new SwitchException(SwitchErrorCodes.EMPTY_SERIAL_NO);
			Device device = deviceService.findDeviceBySerialNumber(serialNo);
			if (ObjectUtil.isEmpty(device))
				throw new SwitchException(SwitchErrorCodes.INVALID_DEVICE);
			if (StringUtil.isEmpty(servicePointCode))
				throw new SwitchException(SwitchErrorCodes.EMPTY_SERV_POINT_ID);
			ServicePoint servicePoint = servicePointService.findServicePointByCode(servicePointCode);
			if (ObjectUtil.isEmpty(servicePoint))
				throw new SwitchException(SwitchErrorCodes.INVALID_SERVICE_POINT);
			// Training Date
			Date trainingDate = null;
			if (!StringUtil.isEmpty(date)) {
				try {
					trainingDate = DateUtil.convertStringToDate(date, DateUtil.TXN_DATE_TIME);
				} catch (Exception e) {
					throw new SwitchException(SwitchErrorCodes.INVALID_DATE_FORMAT);
				}
			}
			// Receipt No
			if (StringUtil.isEmpty(receiptNo))
				throw new SwitchException(SwitchErrorCodes.EMPTY_RECEIPT_NO);
			TrainingStatus existing = trainingService.findTrainingStatusByReceiptNo(receiptNo);
			if (!ObjectUtil.isEmpty(existing))
				throw new SwitchException(SwitchErrorCodes.TRAINING_STATUS_EXIST);
			// Season
			/*
			 * Season season = null; if (!StringUtil.isEmpty(seasonCode)) {
			 * season =
			 * productDistributionService.findSeasonBySeasonCode(seasonCode); if
			 * (ObjectUtil.isEmpty(season)) throw new
			 * SwitchException(SwitchErrorCodes.SEASON_NOT_EXIST); } else season
			 * = productDistributionService.findCurrentSeason();
			 */
			
			if (StringUtil.isEmpty(villageCode)) {
				throw new SwitchException(SwitchErrorCodes.EMPTY_VILLAGE_CODE);
			} else {
				String[] villageCodeArr = villageCode.split(",");
				for (String vCode : villageCodeArr) {
					Village village = locationService.findVillageByCode(vCode.trim());
					if (ObjectUtil.isEmpty(village)) {
						throw new SwitchException(SwitchErrorCodes.VILLAGE_NOT_EXIST);
					} else {
						villageSet.add(village);
					}
				}
			}

		if(StringUtil.isEmpty(trCode)){
			throw new SwitchException(SwitchErrorCodes.EMPTY_TRAINING_CODE);
		}/*else{
			 trainingTopic =trainingService.findTrainingTopicByCode(trCode.trim());
				if (ObjectUtil.isEmpty(trainingTopic)) {
					throw new SwitchException(SwitchErrorCodes.EMPTY_TRAINING_TOPIC);
				} 
			
			String trainingTopicVal[]=trCode.split(",");
			  for (int i = 0; i < trainingTopicVal.length; i++) {
				
					String val=trainingTopicVal[i].replaceAll("\\s+", "");
				       
			        TrainingTopic tt = trainingService.findTrainingTopicByCode(val);
			        
			        topicName += tt.getName() + ",";
			        
			  }
			
		}*/
		if (StringUtil.isEmpty(crCode)) {
			throw new SwitchException(SwitchErrorCodes.EMPTY_CRITERIA_CODE);
		} else {
			String[] criteriaCodeArr = crCode.split(",");
			for (String criteriaCode : criteriaCodeArr) {
				Topic topic = trainingService.findTopicByCode(criteriaCode.trim());
				if (ObjectUtil.isEmpty(topic)) {
					throw new SwitchException(SwitchErrorCodes.CRITERIA_NOT_EXISTS);
				} else {
					topicSet.add(topic);
				}
			}
		}
		if (StringUtil.isEmpty(trMaterialCode)) {
			throw new SwitchException(SwitchErrorCodes.EMPTY_TRAINING_MATERIAL);
		} else {
			String[] trMaterialCodeArr = trMaterialCode.split(",");
			for (String tmCode : trMaterialCodeArr) {
				TrainingMaterial material = trainingService.findtrainingMaterialByCode(tmCode.trim());
				if (ObjectUtil.isEmpty(material)) {
					throw new SwitchException(SwitchErrorCodes.TRAINING_MATERIAL_NOT_EXISTS);
				} else {
					trainingMatSet.add(material);
				}
			}
		}
		if (StringUtil.isEmpty(trMethodCode)) {
			throw new SwitchException(SwitchErrorCodes.EMPTY_TRAINING_METHOD);
		} else {
			String[] trMethodCodeArr = trMethodCode.split(",");
			for (String trMetCode : trMethodCodeArr) {
				TrainingMethod method = trainingService.findTrainingMethodByCode(trMetCode.trim());
				if (ObjectUtil.isEmpty(method)) {
					throw new SwitchException(SwitchErrorCodes.TRAINING_METHOD_NOT_EXISTS);
				} else {
					trainingMathodSet.add(method);
				}
			}
		}
		if (StringUtil.isEmpty(trObservationCode)) {
			throw new SwitchException(SwitchErrorCodes.EMPTY_TRAINING_METHOD);
		} else {
			String[] trObservationCodeArr = trObservationCode.split(",");
			for (String trObsCode : trObservationCodeArr) {
				Observations obs = trainingService.findtrainingObservationByCode(trObsCode.trim());
				if (ObjectUtil.isEmpty(obs)) {
					throw new SwitchException(SwitchErrorCodes.TRAINING_METHOD_NOT_EXISTS);
				} else {
					observationSet.add(obs);
				}
			}
		}
		// Samithi
		/*if (StringUtil.isEmpty(lgCode))
			throw new SwitchException(SwitchErrorCodes.EMPTY_LEARNING_GROUP_CODE);
		Warehouse learningGroup = locationService.findSamithiByCode(lgCode);
		if (ObjectUtil.isEmpty(learningGroup))
			throw new SwitchException(SwitchErrorCodes.LEARNING_GROUP_NOT_EXIST);*/
		// No of Farmers
		/*int farmerAttended = 0;
		try {
			farmerAttended = Integer.parseInt(noOfFarmers);
		} catch (Exception e) {
			throw new SwitchException(SwitchErrorCodes.FARMERS_ATTENDED_INVALID);
		}*/
		/** PROCESSING TRAINING STATUS **/
		TrainingStatus trainingStatus = new TrainingStatus();
		trainingStatus.setTrainingDate(trainingDate);
		trainingStatus.setReceiptNo(receiptNo);
		trainingStatus.setVillages(villageSet);
		/* trainingStatus.setSeason(season); */
		//trainingStatus.setVillage(village);
		//trainingStatus.setLearningGroup(learningGroup);
		//trainingStatus.setFarmerAttended(farmerAttended);
		trainingStatus.setRemarks(remarks);
		trainingStatus.setBranchId(head.getBranchId());
		// Transfer Info
		TransferInfo transferInfo = new TransferInfo();
		transferInfo.setAgentId(agentId);
		transferInfo.setAgentName(
				(ObjectUtil.isEmpty(agent.getPersonalInfo()) ? "" : agent.getPersonalInfo().getAgentName()));
		transferInfo.setDeviceId(device.getCode());
		transferInfo.setDeviceName(device.getName());
		transferInfo.setServicePointId(servicePoint.getCode());
		transferInfo.setServicePointName(servicePoint.getName());
		transferInfo.setOperationType(ESETxn.ON_LINE);
		transferInfo.setTxnTime(new Date());
		trainingStatus.setFarmerIds(farmerIds);
		trainingStatus.setTransferInfo(transferInfo);
		// Topics
		Collection topicCollection = (Collection) reqData.get(TxnEnrollmentProperties.SHOP_DEALER_CREDIT_LIST);
		List<Object> topicObjectList = topicCollection.getObject();
		Set<Topic> topics = new HashSet<Topic>();
		String criteriaCode = "";
		String trainingCode = "";
		for (Object object : topicObjectList) {
			List<Data> topicDataList = object.getData();
			for (Data data : topicDataList) {
				String key = data.getKey();
				String value = data.getValue();
				if (TxnEnrollmentProperties.WAREHOUSE_CATEGORY_CODE.equalsIgnoreCase(key))
					criteriaCode = value;
				if (TxnEnrollmentProperties.TRAINING_CODE.equalsIgnoreCase(key))
					trainingCode = value;
			}
			// Topic
			if (StringUtil.isEmpty(criteriaCode))
				throw new SwitchException(SwitchErrorCodes.EMPTY_CRITERIA_CODE);
			Topic topic = trainingService.findTopicByCode(criteriaCode);
			if (ObjectUtil.isEmpty(topic))
				throw new SwitchException(SwitchErrorCodes.INVALID_CRITERIA);
			topics.add(topic);
		}
		trainingStatus.setTopics(topics);
		// Farmer Training
		if (StringUtil.isEmpty(trainingCode))
			throw new SwitchException(SwitchErrorCodes.EMPTY_TRAINING_CODE);
		FarmerTraining farmerTraining = trainingService.findFarmerTrainingByCode(trainingCode);
		if (ObjectUtil.isEmpty(farmerTraining))
			throw new SwitchException(SwitchErrorCodes.INVALID_FARMER_TRAINING);
		trainingStatus.setFarmerTraining(farmerTraining);
		// Training Status Location
		trainingStatus.setTrainingStatusLocations(buildTrainingStatusLocation(
				(Collection) reqData.get(TxnEnrollmentProperties.AGENT_MOVEMENT_LOCATIONS), trainingStatus));
		// Saving Training Status
			
			trainingStatus.setTopics(topicSet);
			trainingStatus.setTrainingMaterials(trainingMatSet);
			trainingStatus.setTrainingMethods(trainingMathodSet);
			trainingStatus.setObservations(observationSet);
			trainingStatus.setTrainingCode(trCode);
			//trainingStatus.setTrainingCode(topicName);
			trainingStatus.setTrainingAssistName(trAssistName);
			trainingStatus.setTimeTakenForTraining(timeTknForTraining);
		trainingService.addTrainingStatus(trainingStatus);
	}else

	{
		Agent agent = agentService.findAgentByProfileId(agentId);
		OfflineTrainingStatus offlineTrainingStatus = new OfflineTrainingStatus();
		
		offlineTrainingStatus.setDeviceId(serialNo);
		offlineTrainingStatus.setServicePointId(servicePointCode);
		offlineTrainingStatus.setReceiptNo(receiptNo);
		offlineTrainingStatus.setTrainingDate(date);
		//offlineTrainingStatus.setFarmerAttended(noOfFarmers);
		/* offlineTrainingStatus.setSeasonCode(seasonCode); */
		offlineTrainingStatus.setSeasonCode(cSeasonCode);
		offlineTrainingStatus.setVillageCode(villageCode);
		if(farmerIds!=null && !StringUtil.isEmpty(farmerIds)){
			if(head.getTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
				offlineTrainingStatus.setAgentId(agentId+"~"+agent.getAgentType().getCode());
				if(!ObjectUtil.isEmpty(agent)){
					if(agent.getAgentType().getCode().equals("05")){
						//Warehouse fm = farmerService.findSamithiByFarmerId(farmerIds.contains(",") ? farmerIds.split(",")[0].toString() : farmerIds );
						offlineTrainingStatus.setLearningGroupCode(agent.getProcurementCenter().getCode());
					}
					else if(agent.getAgentType().getCode().equals("06")){
						offlineTrainingStatus.setLearningGroupCode(null);
					}
					else{
						Warehouse fm = farmerService.findSamithiByFarmerId(farmerIds.contains(",") ? farmerIds.split(",")[0].toString() : farmerIds );
						offlineTrainingStatus.setLearningGroupCode(fm.getCode());
					}
				}
			}
			else{
				Warehouse fm = farmerService.findSamithiByFarmerId(farmerIds.contains(",") ? farmerIds.split(",")[0].toString() : farmerIds );
				offlineTrainingStatus.setLearningGroupCode(fm.getCode());
				offlineTrainingStatus.setAgentId(agentId);
			}
			}
		offlineTrainingStatus.setRemarks(remarks);
		offlineTrainingStatus.setBranchId(head.getBranchId());
		offlineTrainingStatus.setFarmerIds(farmerIds);
		// Topics
		Collection topicCollection = (Collection) reqData.get(TxnEnrollmentProperties.SHOP_DEALER_CREDIT_LIST);
		List<Object> topicObjectList = topicCollection.getObject();
		String criteriaCode = "";
		String trainingCode = "";
		for (Object object : topicObjectList) {
			List<Data> topicDataList = object.getData();
			for (Data data : topicDataList) {
				String key = data.getKey();
				String value = data.getValue();
				if (TxnEnrollmentProperties.WAREHOUSE_CATEGORY_CODE.equalsIgnoreCase(key))
					// Crieria code splitted with "|' symbol and saved
					criteriaCode = criteriaCode + value + "|";
				if (TxnEnrollmentProperties.TRAINING_CODE.equalsIgnoreCase(key))
					trainingCode = value;
			}
		}
		offlineTrainingStatus.setCriteriaCodes(crCode);
		//offlineTrainingStatus.setFarmerTrainingCode(trainingCode);
		offlineTrainingStatus.setFarmerTrainingCode(trCode);
		// Training Status Location
		offlineTrainingStatus.setTrainingStatusLocations(buildTrainingStatusLocation(
				(Collection) reqData.get(TxnEnrollmentProperties.AGENT_MOVEMENT_LOCATIONS), offlineTrainingStatus));
		offlineTrainingStatus.setStatusCode(ESETxnStatus.PENDING.ordinal());
		offlineTrainingStatus.setStatusMessage(ESETxnStatus.PENDING.toString());
		offlineTrainingStatus.setTopics(trCode);
		offlineTrainingStatus.setTrainingMaterials(trMaterialCode);
		offlineTrainingStatus.setTrainingMethods(trMethodCode);
		offlineTrainingStatus.setObservations(trObservationCode);
		offlineTrainingStatus.setTrainingCode(trCode);
		offlineTrainingStatus.setTrainingAssistName(trAssistName);
		offlineTrainingStatus.setTimeTakenForTraining(timeTknForTraining);
		
		// Saving Offline Training Status
		trainingService.addOfflineTrainingStatus(offlineTrainingStatus);
	}
	Map resp = new HashMap();return resp;
	}

	/**
     * Builds the training status location.
     * @param locationCollection the location collection
     * @param obj the obj
     * @return the set< training status location>
     */
    private Set<TrainingStatusLocation> buildTrainingStatusLocation(
            Collection locationCollection, java.lang.Object obj) {

        Set<TrainingStatusLocation> locations = new HashSet<TrainingStatusLocation>();
        if (!CollectionUtil.isCollectionEmpty(locationCollection)) {
            List<Object> listOfLocationObject = locationCollection.getObject();
            for (Object locationObject : listOfLocationObject) {
                TrainingStatusLocation statusLocation = new TrainingStatusLocation();
                int type = ESETxn.ONLINE_MODE;
                // Assigning Object and txn mode
                if (obj instanceof TrainingStatus) {
                    TrainingStatus trainingStatus = (TrainingStatus) obj;
                    statusLocation.setTrainingStatus(trainingStatus);
                } else if (obj instanceof OfflineTrainingStatus) {
                    OfflineTrainingStatus offlineTrainingStatus = (OfflineTrainingStatus) obj;
                    statusLocation.setOfflineTrainingStatus(offlineTrainingStatus);
                    type = ESETxn.OFFLINE_MODE;
                }
                List<Data> listOfLocationData = locationObject.getData();
                for (Data locationData : listOfLocationData) {
                    String key = locationData.getKey();
                    // Update Time
                    if (TxnEnrollmentProperties.UPDATE_TIME
                            .equalsIgnoreCase(key)) {
                        try {
                            if (ESETxn.ON_LINE == type)
                                DateUtil.convertStringToDate(locationData.getValue(), DateUtil.TXN_DATE_TIME);
                            statusLocation.setUpdateTime(locationData.getValue());
                        } catch (Exception e) {
                            e.printStackTrace();// Error is thrown only if its online
                            throw new SwitchException(SwitchErrorCodes.INVALID_DATE_FORMAT);
                        }
                    }
                    // Photo
                    if (TxnEnrollmentProperties.PHOTO.equalsIgnoreCase(key)) {
                        DataHandler photoDataHandler = (DataHandler) locationData.getBinaryValue();
                        try {
                            if (photoDataHandler != null) {
                                byte[] photoContent = IOUtils.toByteArray(photoDataHandler.getInputStream());
                                statusLocation.setPhoto(photoContent);
                            }
                        } catch (IOException e) {
                            if (ESETxn.ON_LINE == type)// Error is thrown only if its online
                                throw new SwitchException(SwitchErrorCodes.INVALID_IMAGE);
                            e.printStackTrace();
                        }
                    }
                    // Latitude
                    if (TxnEnrollmentProperties.LATITUDE.equalsIgnoreCase(key))
                        statusLocation.setLatitude(locationData.getValue());
                    // Longitude
                    if (TxnEnrollmentProperties.LONGITUDE.equalsIgnoreCase(key))
                        statusLocation.setLongitude(locationData.getValue());
                }// end of for loop for Data List
                locations.add(statusLocation);
            }// end of for loop for Object List
        }// end of if loop
        return locations;

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

	public Set<Village> getVillageSet() {
		return villageSet;
	}

	public void setVillageSet(Set<Village> villageSet) {
		this.villageSet = villageSet;
	}

	public ITrainingService getTrainingService() {
		return trainingService;
	}

	public void setTrainingService(ITrainingService trainingService) {
		this.trainingService = trainingService;
	}

	

	public Set<TrainingMaterial> getTrainingMatSet() {
		return trainingMatSet;
	}

	public void setTrainingMatSet(Set<TrainingMaterial> trainingMatSet) {
		this.trainingMatSet = trainingMatSet;
	}

	public Set<TrainingMethod> getTrainingMathodSet() {
		return trainingMathodSet;
	}

	public void setTrainingMathodSet(Set<TrainingMethod> trainingMathodSet) {
		this.trainingMathodSet = trainingMathodSet;
	}

	public Set<Observations> getObservationSet() {
		return observationSet;
	}

	public void setObservationSet(Set<Observations> observationSet) {
		this.observationSet = observationSet;
	}

	public TrainingTopic getTrainingTopic() {
		return trainingTopic;
	}

	public void setTrainingTopic(TrainingTopic trainingTopic) {
		this.trainingTopic = trainingTopic;
	}

	public Set<Topic> getTopicSet() {
		return topicSet;
	}

	public void setTopicSet(Set<Topic> topicSet) {
		this.topicSet = topicSet;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	
}
