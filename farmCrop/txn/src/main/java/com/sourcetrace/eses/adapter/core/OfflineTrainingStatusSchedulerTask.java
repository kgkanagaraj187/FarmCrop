/*
 * OfflineTrainingStatusSchedulerTask.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
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
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.TransferInfo;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IServicePointService;
import com.sourcetrace.eses.service.ITrainingService;
import com.sourcetrace.eses.txn.exception.OfflineTransactionException;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.ServicePoint;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;
import com.sourcetrace.esesw.excep.SwitchException;

@Component
public class OfflineTrainingStatusSchedulerTask {

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
	private IServicePointService servicePointService;
	@Resource(name = "datasources")
	private Map<String, DataSource> datasources;
	private Set<Village> villageSet;
	private Set<TrainingMaterial> trainingMatSet ;
	private Set<TrainingMethod> trainingMathodSet;
	private Set<Observations> observationSet;
	private Set<Topic> topicSet;
	private TrainingTopic trainingTopic;
	private FarmerTraining farmerTraining;
	
	
	private static Logger LOGGER = Logger.getLogger(OfflineTrainingStatusSchedulerTask.class);
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.TimerTask#run()
	 */
	public void process() {
		Vector<String> tenantIds = new Vector<>(datasources.keySet());

		for (String tenantId : tenantIds) {
			List<OfflineTrainingStatus> offlineTrainingStatusList = trainingService.listOfflineTrainingStatus(tenantId);
			for (OfflineTrainingStatus offlineTrainingStatus : offlineTrainingStatusList) {
				int statusCode = ESETxnStatus.SUCCESS.ordinal();
				String statusMsg = ESETxnStatus.SUCCESS.toString();
				try {
					if (StringUtil.isEmpty(offlineTrainingStatus.getAgentId()))
						throw new OfflineTransactionException(SwitchErrorCodes.AGENT_ID_EMPTY);
					Agent agent;
					if(offlineTrainingStatus.getAgentId().contains("~")){
						agent = agentService.findAgentByProfileId(offlineTrainingStatus.getAgentId().split("~")[0], tenantId,
							offlineTrainingStatus.getBranchId());
					}
					else{
						agent = agentService.findAgentByProfileId(offlineTrainingStatus.getAgentId(), tenantId,
								offlineTrainingStatus.getBranchId());
						if (StringUtil.isEmpty(offlineTrainingStatus.getVillageCode())) {
							throw new SwitchException(SwitchErrorCodes.EMPTY_VILLAGE_CODE);
						} else {
							 villageSet = new HashSet<Village>();
							String[] villageCodeArr = offlineTrainingStatus.getVillageCode().split(",");
							for (String vCode : villageCodeArr) {
								Village vill = locationService.findVillageByCode(vCode.trim(),tenantId);
								if (ObjectUtil.isEmpty(vill)) {
									throw new SwitchException(SwitchErrorCodes.VILLAGE_NOT_EXIST);
								} else {
									villageSet.add(vill);
								}
							}
						}
					}
					if (ObjectUtil.isEmpty(agent))
						throw new OfflineTransactionException(SwitchErrorCodes.INVALID_AGENT);
					if (StringUtil.isEmpty(offlineTrainingStatus.getDeviceId()))
						throw new OfflineTransactionException(SwitchErrorCodes.EMPTY_SERIAL_NO);
					Device device = deviceService.findDeviceBySerialNumber(offlineTrainingStatus.getDeviceId(),
							tenantId);
					if (ObjectUtil.isEmpty(device))
						throw new OfflineTransactionException(SwitchErrorCodes.INVALID_DEVICE);
					if (StringUtil.isEmpty(offlineTrainingStatus.getServicePointId()))
						throw new OfflineTransactionException(SwitchErrorCodes.EMPTY_SERV_POINT_ID);
					ServicePoint servicePoint = servicePointService
							.findServicePointByCode(offlineTrainingStatus.getServicePointId(), tenantId);
					if (ObjectUtil.isEmpty(servicePoint))
						throw new OfflineTransactionException(SwitchErrorCodes.INVALID_SERVICE_POINT);
					// Training Date
					Date trainingDate = null;
					if (!StringUtil.isEmpty(offlineTrainingStatus.getTrainingDate())) {
						try {
							trainingDate = DateUtil.convertStringToDate(offlineTrainingStatus.getTrainingDate(),
									DateUtil.TXN_DATE_TIME);
						} catch (Exception e) {
							throw new OfflineTransactionException(SwitchErrorCodes.INVALID_DATE_FORMAT);
						}
					}
					
					/*int farmerAttended = 0;
					try {
						farmerAttended = Integer.parseInt(offlineTrainingStatus.getFarmerAttended());
					} catch (Exception e) {
						throw new OfflineTransactionException(SwitchErrorCodes.FARMERS_ATTENDED_INVALID);
					}*/
					
					

					if (StringUtil.isEmpty(offlineTrainingStatus.getTrainingCode())) {
						throw new SwitchException(SwitchErrorCodes.EMPTY_TRAINING_CODE);
					} /*else {
						farmerTraining = trainingService
								.findFarmerTrainingByCode(offlineTrainingStatus.getTrainingCode(), tenantId);
						if (ObjectUtil.isEmpty(farmerTraining)) {
							throw new SwitchException(SwitchErrorCodes.EMPTY_TRAINING_TOPIC);
						}

					}*/
					if (StringUtil.isEmpty(offlineTrainingStatus.getCriteriaCodes())) {
						throw new SwitchException(SwitchErrorCodes.EMPTY_CRITERIA_CODE);
					} else {
						topicSet=new HashSet<Topic>();
						String[] criteriaCodeArr = offlineTrainingStatus.getCriteriaCodes().split(",");
						for (String criteriaCode : criteriaCodeArr) {
							Topic topic = trainingService.findTopicByCode(criteriaCode.trim(),tenantId);
							if (ObjectUtil.isEmpty(topic)) {
								throw new SwitchException(SwitchErrorCodes.CRITERIA_NOT_EXISTS);
							} else {
								
								topicSet.add(topic);
							}
						}
					}

					if (!StringUtil.isEmpty(offlineTrainingStatus.getTrainingMaterials())) {
					
						trainingMatSet=new HashSet<TrainingMaterial>();
						String[] trMaterialCodeArr = (offlineTrainingStatus.getTrainingMaterials().split(","));
						for (String tmCode : trMaterialCodeArr) {
							TrainingMaterial material = trainingService.findtrainingMaterialByCode(tmCode.trim(),tenantId);
							if (!ObjectUtil.isEmpty(material)) {
								trainingMatSet.add(material);
							} 
						}
					}
					if (!StringUtil.isEmpty(offlineTrainingStatus.getTrainingMethods())) {
					
						trainingMathodSet=new HashSet<TrainingMethod>();
						String[] trMethodCodeArr = offlineTrainingStatus.getTrainingMethods().split(",");
						for (String trMetCode : trMethodCodeArr) {
							TrainingMethod method = trainingService.findTrainingMethodByCode(trMetCode.trim(),tenantId);
							if (!ObjectUtil.isEmpty(method)) {
								
								trainingMathodSet.add(method);
							}
						}
					}
					if (!StringUtil.isEmpty(offlineTrainingStatus.getObservations())) {
				
						observationSet=new HashSet<Observations>();
						String[] trObservationCodeArr = offlineTrainingStatus.getObservations().split(",");
						for (String trObsCode : trObservationCodeArr) {
							Observations obs = trainingService.findtrainingObservationByCode(trObsCode.trim(),tenantId);
							if (!ObjectUtil.isEmpty(obs)) {
							observationSet.add(obs);
							}
						}
					}

					/** PROCESSING TRAINING STATUS **/
					TrainingStatus trainingStatus = new TrainingStatus();
					trainingStatus.setTrainingDate(trainingDate);
					trainingStatus.setReceiptNo(offlineTrainingStatus.getReceiptNo());
					trainingStatus.setVillages(villageSet);
					
					//trainingStatus.setFarmerAttended(farmerAttended);
					trainingStatus.setRemarks(offlineTrainingStatus.getRemarks());
					trainingStatus.setBranchId(offlineTrainingStatus.getBranchId());
					trainingStatus.setFarmerIds(offlineTrainingStatus.getFarmerIds());
					// Transfer Info
					TransferInfo transferInfo = new TransferInfo();
					if(offlineTrainingStatus.getAgentId().contains("~")){
						transferInfo.setAgentId(offlineTrainingStatus.getAgentId().split("~")[0]);
						transferInfo.setAgentType(offlineTrainingStatus.getAgentId().split("~")[1]);
					}
					else{
						transferInfo.setAgentId(offlineTrainingStatus.getAgentId());
						transferInfo.setAgentType("02");
					}
					transferInfo.setAgentName((ObjectUtil.isEmpty(agent.getPersonalInfo()) ? ""
							: agent.getPersonalInfo().getAgentName()));
					transferInfo.setDeviceId(device.getCode());
					transferInfo.setDeviceName(device.getName());
					transferInfo.setServicePointId(servicePoint.getCode());
					transferInfo.setServicePointName(servicePoint.getName());
					transferInfo.setOperationType(ESETxn.ON_LINE);
					transferInfo.setTxnTime(new Date());

					trainingStatus.setTransferInfo(transferInfo);
				
					// Training Status Location
					Set<TrainingStatusLocation> locations = offlineTrainingStatus.getTrainingStatusLocations();
					for (TrainingStatusLocation location : locations) {
						location.setTrainingStatus(trainingStatus);
					}

					trainingStatus.setTopics(topicSet);
					trainingStatus.setTrainingMaterials(trainingMatSet);
					trainingStatus.setTrainingMethods(trainingMathodSet);
					trainingStatus.setObservations(observationSet);
					trainingStatus.setTrainingCode(offlineTrainingStatus.getTrainingCode());
					Warehouse wh = locationService.findSamithiByCode(offlineTrainingStatus.getLearningGroupCode(),tenantId);
					trainingStatus.setLearningGroup(wh);
					trainingStatus.setTrainingAssistName(offlineTrainingStatus.getTrainingAssistName());
					trainingStatus.setTimeTakenForTraining(offlineTrainingStatus.getTimeTakenForTraining());
					
					// Saving Training Status
					trainingService.saveTrainingStatus(trainingStatus, tenantId);
					
				} // end try
				catch (OfflineTransactionException ote) {
					statusCode = ESETxnStatus.ERROR.ordinal();
					statusMsg = ote.getError();
				} catch (Exception e) { // Catches all type of exception except
										// OfflineTransactionException
					statusCode = ESETxnStatus.ERROR.ordinal();
					statusMsg = !ObjectUtil.isEmpty(e)?e.getMessage():"MSG NOT FOUND";
				}
				// Update the error msg in Offline Training Status
				offlineTrainingStatus.setStatusCode(statusCode);
				offlineTrainingStatus.setStatusMessage(statusMsg);
				trainingService.updateOfflineTrainingStatus(offlineTrainingStatus, tenantId);

			}

		}

	}

	private Set<TrainingStatusLocation> buildTrainingStatusLoc(Set<TrainingStatusLocation> trainingStatusLocations)
			throws OfflineTransactionException, IOException {
		Set<TrainingStatusLocation> trainingStatusLocationSet = new HashSet<TrainingStatusLocation>();
		for (TrainingStatusLocation locations : trainingStatusLocationSet) {
			TrainingStatusLocation trainingStatus = new TrainingStatusLocation();
			if (StringUtil.isEmpty(locations.getUpdateTime())) {
				throw new OfflineTransactionException(SwitchErrorCodes.EMPTY_TRAINING_TIME);
			} else {
				trainingStatus.setUpdateTime(locations.getUpdateTime());
			}
			if (locations.getPhoto().length > 0) {
				throw new OfflineTransactionException(SwitchErrorCodes.EMPTY_TRAINING_PHOTO);
			} else {
				trainingStatus.setPhoto(locations.getPhoto());
			}
			if (StringUtil.isEmpty(locations.getLatitude())) {
				throw new OfflineTransactionException(SwitchErrorCodes.EMPTY_LATITUDE);
			} else {
				trainingStatus.setLatitude(locations.getLatitude());
			}
			if (StringUtil.isEmpty(locations.getLongitude())) {
				throw new OfflineTransactionException(SwitchErrorCodes.EMPTY_LATITUDE);
			} else {
				trainingStatus.setLongitude(locations.getLongitude());
			}
			trainingStatusLocationSet.add(trainingStatus);
		}
		// TODO Auto-generated method stub
		return trainingStatusLocationSet;
	}

	public ITrainingService getTrainingService() {
		return trainingService;
	}

	public void setTrainingService(ITrainingService trainingService) {
		this.trainingService = trainingService;
	}

	public Set<Village> getVillageSet() {
		return villageSet;
	}

	public void setVillageSet(Set<Village> villageSet) {
		this.villageSet = villageSet;
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

	public Set<Topic> getTopicSet() {
		return topicSet;
	}

	public void setTopicSet(Set<Topic> topicSet) {
		this.topicSet = topicSet;
	}

	public TrainingTopic getTrainingTopic() {
		return trainingTopic;
	}

	public void setTrainingTopic(TrainingTopic trainingTopic) {
		this.trainingTopic = trainingTopic;
	}

	public FarmerTraining getFarmerTraining() {
		return farmerTraining;
	}

	public void setFarmerTraining(FarmerTraining farmerTraining) {
		this.farmerTraining = farmerTraining;
	}

}
