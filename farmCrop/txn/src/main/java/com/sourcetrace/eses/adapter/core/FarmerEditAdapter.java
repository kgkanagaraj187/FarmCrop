package com.sourcetrace.eses.adapter.core;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.interceptor.ITxnMessageUtil;
import com.sourcetrace.eses.order.entity.txn.AgentAccessLog;
import com.sourcetrace.eses.order.entity.txn.AgentAccessLogDetail;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TransactionTypeProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.AgentService;
import com.sourcetrace.eses.service.DeviceService;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.OfflineTransactionException;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.entity.Image;
import com.sourcetrace.eses.util.entity.ImageInfo;
import com.sourcetrace.esesw.entity.profile.Coordinates;
import com.sourcetrace.esesw.entity.profile.CoordinatesMap;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestData;
import com.sourcetrace.esesw.entity.txn.ESETxn;

@Component
public class FarmerEditAdapter implements ITxnAdapter {
	private static final Logger LOGGER = Logger.getLogger(FarmerEditAdapter.class.getName());
	private static final DateFormat TXN_DATE_FORMAT = new SimpleDateFormat(DateUtil.TXN_DATE_TIME);
	
	private static final String FARMER = "farmer";
	private static final String FARM = "farm";
	private static final String REQUEST_DATA = "requestData";
	private String latitude;
	private String longitude;
	private String mobileNumber;
	private String flatitude;
    private String flongitude;
    
    DecimalFormat formatter = new DecimalFormat("0.000");
    
	@Autowired
	private IFarmerService farmerService;
	
	@Autowired
	private IAgentService agentService;
	
	@Autowired
	private IDeviceService deviceService;

	private int mode = 1;
	Map requestMap = new HashMap();
	
	private void validate(Object object, String errorCode) throws OfflineTransactionException, IOException {

		if (ObjectUtil.isEmpty(object) || ((object instanceof String) && (StringUtil.isEmpty(String.valueOf(object)))))
			throwError(errorCode);
	}
	
	private void throwError(String errorCode) throws OfflineTransactionException, IOException {

		if (mode == ESETxn.ONLINE_MODE)
			throw new SwitchException(errorCode);
		else
			throw new OfflineTransactionException(errorCode);
	}
	
	
	@SuppressWarnings({ "unused", "unchecked" })
	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {
		Head head = (Head) reqData.get(TransactionProperties.HEAD);
		BeanWrapper wrapper = new BeanWrapperImpl(head);
				
		String agentId = head.getAgentId();
		String serialNo = head.getSerialNo();
		String servPointId = head.getServPointId();
		String txnMode = head.getMode();
		String branchId = head.getBranchId();
		String tenantId = head.getTenantId();
        String version=head.getVersionNo();
		LOGGER.info("AGENT ID : " + agentId);
		LOGGER.info("SERIAL NO : " + serialNo);
		LOGGER.info("TXN TYPE: " + TransactionTypeProperties.SENSITIZING_ADAPTER);
		

		
		String farmerId = (String) reqData.get(TxnEnrollmentProperties.FARMER_ID);
		DataHandler farmerPhoto = (DataHandler) reqData.get(TxnEnrollmentProperties.PHOTO);
		String photoCaptureTime = (String) reqData.get(TxnEnrollmentProperties.FARMER_PHOTO_CAPTURE_TIME);
		String latitude = (String) reqData.get(TxnEnrollmentProperties.LATITUDE);
		String longitude = (String) reqData.get(TxnEnrollmentProperties.LONGITUDE);
		String mob = (String) reqData.get(TxnEnrollmentProperties.MOBILE_NUMBER);
		String idProof = (String) reqData.get(TxnEnrollmentProperties.ID_PROOF);
		String proofNo = (String) reqData.get(TxnEnrollmentProperties.ID_PROOF_VALUE);
		String otherIdProof = (String) reqData.get(TxnEnrollmentProperties.ID_PROOF_OTHER);
		String trader = (String) reqData.get(TxnEnrollmentProperties.TRADER_CODE);
		setLatitude(latitude);
		setLongitude(longitude);
		setMobileNumber(mob);
		
		String farmCode = (String) reqData.get(TxnEnrollmentProperties.FARM_CODE);
		DataHandler farmPhotoDataHandler = (DataHandler) reqData.get(TxnEnrollmentProperties.FARM_PHOTO);
		String farmPhotoCaptureTime = (String) reqData.get(TxnEnrollmentProperties.FARM_PHOTO_CAPTURING_TIME);
		Date formatedDate = DateUtil.convertStringToDate(head.getTxnTime(), DateUtil.TXN_DATE_TIME);
		String serialNumber = (String) head.getSerialNo();
		Device device = deviceService.findDeviceBySerialNumber(serialNumber);
		Agent agent = agentService.findAgentByProfileAndBranchId(agentId, device.getBranchId());
		/*AgentAccessLog agentAccessLog = agentService.findAgentAccessLogByAgentId(agent.getProfileId(),
				DateUtil.getDateWithoutTime(formatedDate));		*/	
		String farmLatitude = (String) reqData.get(TxnEnrollmentProperties.FARM_LATITUDE);
		String farmLongitude = (String) reqData.get(TxnEnrollmentProperties.FARM_LONGITUDE);
		String proposePlantArea=(String) reqData.get(TxnEnrollmentProperties.PROPOSED_PLANT_AREA);
		String totalLandHolding=(String) reqData.get(TxnEnrollmentProperties.TOTAL_LAND_HOLDING_AREA);
		
		setFlatitude(farmLatitude);
		setFlongitude(farmLongitude);
		
		if (tenantId.equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)  ){
			if(!StringUtil.isEmpty(farmLatitude) && farmLatitude!=null){
				Farm farm  = farmerService.findFarmByFarmCode(farmCode);
				
				if((farm.getLatitude()==null || !farm.getLatitude().equalsIgnoreCase(farmLatitude) ) && (farm.getLongitude()==null || !farm.getLongitude().equalsIgnoreCase(farmLongitude) 
						)){
					AgentAccessLog agentAccessLog = agentService.findAgentAccessLogByAgentId(agent.getProfileId(),
							DateUtil.getDateWithoutTime(formatedDate));
					if (!ObjectUtil.isEmpty(agentAccessLog)) {
				AgentAccessLogDetail agentAccessLogDetail = agentService
						.findAgentAccessLogDetailByTxn(agentAccessLog.getId(), "3081");
				if (!StringUtil.isEmpty(agentAccessLogDetail)) {
					Long txnCount = agentAccessLogDetail.getTxnCount() + 1L;
					agentAccessLogDetail.setTxnCount(txnCount);
					agentAccessLogDetail.setTxnDate(formatedDate);
					agentService.update(agentAccessLogDetail);
				}
			
				else{
			AgentAccessLogDetail agentAccessLogDetailNew = new AgentAccessLogDetail();
			agentAccessLogDetailNew.setAgentAccessLog(agentAccessLog);
			/*agentAccessLogDetailNew
					.setTxnType((String) wrapper.getPropertyValue(ITxnMessageUtil.TXN_TYPE));*/
			agentAccessLogDetailNew.setTxnType("3081");
			agentAccessLogDetailNew
					.setMessageNumber(head.getMsgNo());
			agentAccessLogDetailNew.setTxnMode(head.getMode());

			agentAccessLogDetailNew.setTxnDate(formatedDate);

			agentAccessLogDetailNew.setTxnCount(1L);

			agentAccessLogDetailNew.setServicePoint(
					head.getServPointId());

			agentService.save(agentAccessLogDetailNew);
			}
					}
					else{	
						AgentAccessLog agentAccessLogNew = new AgentAccessLog();
					agentAccessLogNew.setLogin(formatedDate);
					agentAccessLogNew.setMobileVersion(version);
					agentAccessLogNew.setProfileId(agentId);
					agentAccessLogNew.setSerialNo(serialNo);
					agentAccessLogNew.setBranchId(device.getBranchId());
					agentAccessLogNew.setLastTxnTime(formatedDate);
					agentService.save(agentAccessLogNew);
						
					AgentAccessLogDetail agentAccessLogDetailNew = new AgentAccessLogDetail();
					agentAccessLogDetailNew.setAgentAccessLog(agentAccessLogNew);
					/*agentAccessLogDetailNew
							.setTxnType((String) wrapper.getPropertyValue(ITxnMessageUtil.TXN_TYPE));*/
					agentAccessLogDetailNew.setTxnType("3081");
					agentAccessLogDetailNew
							.setMessageNumber(head.getMsgNo());
					agentAccessLogDetailNew.setTxnMode(head.getMode());

					agentAccessLogDetailNew.setTxnDate(formatedDate);

					agentAccessLogDetailNew.setTxnCount(1L);

					agentAccessLogDetailNew.setServicePoint(
							head.getServPointId());

					agentService.save(agentAccessLogDetailNew);
					}
		 }
	   }
		}
		
		String season = (String) reqData.get(TxnEnrollmentProperties.CURRENT_SEASON_CODE);
		Collection landCollection = (Collection) reqData.get(TxnEnrollmentProperties.FARM_LAND_AREA_GPS);
		ImageInfo imageInfo = null;
		
		byte[] photoContent = null;
		byte[] farmPhotoContent = null;
		
		try{
			// Basic Info Validations
			validate(farmerId,ITxnErrorCodes.EMPTY_FARMER_ID);
			//validate(farmCode, ITxnErrorCodes.EMPTY_FARM_CODE);
			Farmer existingFarmer ;
				existingFarmer = farmerService.findFarmerByFarmerId(farmerId);	
			if (ObjectUtil.isEmpty(existingFarmer)){
				throwError(ITxnErrorCodes.INVALID_FARMER_ID);
			}else{
			
				existingFarmer.setLatitude(latitude);
				existingFarmer.setLongitude(longitude);
				existingFarmer.setMobileNumber(mob);
				if (tenantId.equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)) {
					existingFarmer.setMasterData(trader);
				}
			try {
				if (farmerPhoto != null)
					photoContent = IOUtils.toByteArray(farmerPhoto.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			if (!StringUtil.isEmpty(photoContent) && photoContent.length > 0) {
				if (!ObjectUtil.isEmpty(existingFarmer.getImageInfo())) {
					imageInfo = existingFarmer.getImageInfo();
					Image photo = imageInfo.getPhoto();
					photo.setImage(photoContent);
					imageInfo.setPhoto(photo);
					farmerService.updateImageInfo(imageInfo);
				} else {
					imageInfo = new ImageInfo();
					if (photoContent != null) {
						Image photo = new Image();
						photo.setImage(photoContent);
						photo.setImageId(farmerId + "-FP");
						imageInfo.setPhoto(photo);
						farmerService.addImageInfo(imageInfo);
						existingFarmer.setImageInfo(imageInfo);
					}

				}
			}
			
			
			if(tenantId.equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID) && photoCaptureTime != null && !StringUtil.isEmpty(photoCaptureTime)){
				AgentAccessLog agentAccessLog = agentService.findAgentAccessLogByAgentId(agent.getProfileId(),
						DateUtil.getDateWithoutTime(formatedDate));
				if (!ObjectUtil.isEmpty(agentAccessLog)) {
				
				AgentAccessLogDetail agentAccessLogDetail = agentService
						.findAgentAccessLogDetailByTxn(agentAccessLog.getId(), "3083");
				if (!StringUtil.isEmpty(agentAccessLogDetail)) {
					Long txnCount = agentAccessLogDetail.getTxnCount() + 1L;
					agentAccessLogDetail.setTxnCount(txnCount);
					agentAccessLogDetail.setTxnDate(formatedDate);
					agentService.update(agentAccessLogDetail);
				}
				else{
				AgentAccessLogDetail agentAccessLogDetailNew = new AgentAccessLogDetail();
				agentAccessLogDetailNew.setAgentAccessLog(agentAccessLog);
				/*agentAccessLogDetailNew
						.setTxnType((String) wrapper.getPropertyValue(ITxnMessageUtil.TXN_TYPE));*/
				agentAccessLogDetailNew.setTxnType("3083");
				agentAccessLogDetailNew
						.setMessageNumber(head.getMsgNo());
				agentAccessLogDetailNew.setTxnMode(head.getMode());

				agentAccessLogDetailNew.setTxnDate(formatedDate);

				agentAccessLogDetailNew.setTxnCount(1L);

				agentAccessLogDetailNew.setServicePoint(
						head.getServPointId());

				agentService.save(agentAccessLogDetailNew);
			}
				}
				else{	
					AgentAccessLog agentAccessLogNew = new AgentAccessLog();
				agentAccessLogNew.setLogin(formatedDate);
				agentAccessLogNew.setMobileVersion(version);
				agentAccessLogNew.setProfileId(agentId);
				agentAccessLogNew.setSerialNo(serialNo);
				agentAccessLogNew.setBranchId(device.getBranchId());
				agentAccessLogNew.setLastTxnTime(formatedDate);
				agentService.save(agentAccessLogNew);
					
				AgentAccessLogDetail agentAccessLogDetailNew = new AgentAccessLogDetail();
				agentAccessLogDetailNew.setAgentAccessLog(agentAccessLogNew);
				/*agentAccessLogDetailNew
						.setTxnType((String) wrapper.getPropertyValue(ITxnMessageUtil.TXN_TYPE));*/
				agentAccessLogDetailNew.setTxnType("3083");
				agentAccessLogDetailNew
						.setMessageNumber(head.getMsgNo());
				agentAccessLogDetailNew.setTxnMode(head.getMode());

				agentAccessLogDetailNew.setTxnDate(formatedDate);

				agentAccessLogDetailNew.setTxnCount(1L);

				agentAccessLogDetailNew.setServicePoint(
						head.getServPointId());

				agentService.save(agentAccessLogDetailNew);
				
					
				}
			}
			
			if (!StringUtil.isEmpty(photoCaptureTime)) {
				try {
					Date photoCaptureDate = TXN_DATE_FORMAT.parse(photoCaptureTime);
					existingFarmer.setPhotoCaptureTime(photoCaptureDate);
				} catch (Exception e) {
					LOGGER.info(e.getMessage());
				}
			}
			
			DataHandler fp = (reqData.containsKey(TxnEnrollmentProperties.FARMER_FINGER_PRINT))
					? (DataHandler) reqData.get(TxnEnrollmentProperties.FARMER_FINGER_PRINT) : null;
			if (!ObjectUtil.isEmpty(fp)) {
				byte[] fingerPrint = null;
				try {
					if (fp != null) {
						fingerPrint = IOUtils.toByteArray(fp.getInputStream());
						if (!StringUtil.isEmpty(fingerPrint) && !fingerPrint.equals("[]")) {
							existingFarmer.setFingerPrint(fingerPrint);
						}
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			

			DataHandler idpRoof = (reqData.containsKey(TxnEnrollmentProperties.IDPROOF_PHOTO))
					? (DataHandler) reqData.get(TxnEnrollmentProperties.IDPROOF_PHOTO) : null;
			if (!ObjectUtil.isEmpty(idpRoof)) {
				byte[] idproof = null;
				try {
					if (idpRoof != null) {
						idproof = IOUtils.toByteArray(idpRoof.getInputStream());
						if (!StringUtil.isEmpty(idproof) && !idproof.equals("[]")) {
							existingFarmer.setIdProofImg(idproof);
						}
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			existingFarmer.setIdProof(!StringUtil.isEmpty(idProof) ? idProof : "");
			existingFarmer.setProofNo(!StringUtil.isEmpty(proofNo) ? proofNo : "");

			existingFarmer.setOtherIdProof(!StringUtil.isEmpty(otherIdProof) ? otherIdProof : "");
		
			farmerService.updateContractForFarmerAndHarvestDatas(existingFarmer,new HashSet<HarvestData>(),agentId);
			}
			
			if(farmCode!=null && !StringUtil.isEmpty(farmCode)){
				Farm existingFarm = null;
					 existingFarm = farmerService.findFarmByFarmCode(farmCode);
			if(existingFarm!=null){
			
			try {
				if (farmPhotoDataHandler != null)
					farmPhotoContent = IOUtils.toByteArray(farmPhotoDataHandler.getInputStream());
				if (farmPhotoContent != null) {
					if (farmPhotoContent.length > 0)
						existingFarm.setPhoto(farmPhotoContent);
                       existingFarm.getFarmer().setBasicInfo(1);

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		
			if (!StringUtil.isEmpty(farmPhotoCaptureTime)) {
				try {
					Date farmPhotoCaptureDate = TXN_DATE_FORMAT.parse(farmPhotoCaptureTime);
					existingFarm.setPhotoCaptureTime(farmPhotoCaptureDate);
				} catch (Exception e) {
					LOGGER.info(e.getMessage());
				}
			}
			
			
		}
			
			
			CoordinatesMap coordinatesMap=new CoordinatesMap();
			coordinatesMap.setFarmCoordinates(buildFarmLandArea(landCollection,existingFarm));
			coordinatesMap.setAgentId(agentId);
			coordinatesMap.setArea(existingFarm.getFarmDetailedInfo().getTotalLandHolding());
			coordinatesMap.setDate(	existingFarm.getPlotCapturingTime());
			coordinatesMap.setFarm(existingFarm);
			coordinatesMap.setMidLatitude(existingFarm.getLongitude());
			coordinatesMap.setMidLongitude(existingFarm.getLongitude());
			coordinatesMap.setStatus(CoordinatesMap.Status.ACTIVE.ordinal());
			if(existingFarm.getCoordinatesMap()!=null && !ObjectUtil.isListEmpty(existingFarm.getCoordinatesMap())){
				existingFarm.getCoordinatesMap().stream().forEach(co->{
					co.setStatus(CoordinatesMap.Status.INACTIVE.ordinal());
				});
				existingFarm.getCoordinatesMap().add(coordinatesMap);
				}
				else{
					Set<CoordinatesMap> coMap=new LinkedHashSet<>();
					coMap.add(coordinatesMap);
					existingFarm.setCoordinatesMap(coMap);
					
				}
			existingFarm.setActiveCoordinates(coordinatesMap);
			
			//existingFarm.setCoordinates(buildFarmLandArea(landCollection));
			if (farmPhotoDataHandler != null){
				if(flatitude!= null){
				existingFarm.setLatitude(flatitude);
				}
				if(flongitude!= null){
				existingFarm.setLongitude(flongitude);
				}
				if(totalLandHolding!=null && !StringUtil.isEmpty(totalLandHolding)){
					existingFarm.getFarmDetailedInfo().setTotalLandHolding(totalLandHolding);
					
					if (tenantId.equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID) ){
						AgentAccessLog agentAccessLog = agentService.findAgentAccessLogByAgentId(agent.getProfileId(),
								DateUtil.getDateWithoutTime(formatedDate));
						if (!ObjectUtil.isEmpty(agentAccessLog)) {
						AgentAccessLogDetail agentAccessLogDetail = agentService
								.findAgentAccessLogDetailByTxn(agentAccessLog.getId(), "3082");
						if (!StringUtil.isEmpty(agentAccessLogDetail)) {
							Long txnCount = agentAccessLogDetail.getTxnCount() + 1L;
							agentAccessLogDetail.setTxnCount(txnCount);
							agentAccessLogDetail.setTxnDate(formatedDate);
							agentService.update(agentAccessLogDetail);
						}
						else{
						
						AgentAccessLogDetail agentAccessLogDetailNew = new AgentAccessLogDetail();
						agentAccessLogDetailNew.setAgentAccessLog(agentAccessLog);
						/*agentAccessLogDetailNew
								.setTxnType((String) wrapper.getPropertyValue(ITxnMessageUtil.TXN_TYPE));*/
						agentAccessLogDetailNew.setTxnType("3082");
						agentAccessLogDetailNew
								.setMessageNumber(head.getMsgNo());
						agentAccessLogDetailNew.setTxnMode(head.getMode());

						agentAccessLogDetailNew.setTxnDate(formatedDate);

						agentAccessLogDetailNew.setTxnCount(1L);

						agentAccessLogDetailNew.setServicePoint(
								head.getServPointId());

						agentService.save(agentAccessLogDetailNew);
						}
						}
						else{	
							AgentAccessLog agentAccessLogNew = new AgentAccessLog();
							agentAccessLogNew.setLogin(formatedDate);
							agentAccessLogNew.setMobileVersion(version);
							agentAccessLogNew.setProfileId(agentId);
							agentAccessLogNew.setSerialNo(serialNo);
							agentAccessLogNew.setBranchId(device.getBranchId());
							agentAccessLogNew.setLastTxnTime(formatedDate);
							agentService.save(agentAccessLogNew);
								
							AgentAccessLogDetail agentAccessLogDetailNew = new AgentAccessLogDetail();
							agentAccessLogDetailNew.setAgentAccessLog(agentAccessLogNew);
							/*agentAccessLogDetailNew
									.setTxnType((String) wrapper.getPropertyValue(ITxnMessageUtil.TXN_TYPE));*/
							agentAccessLogDetailNew.setTxnType("3082");
							agentAccessLogDetailNew
									.setMessageNumber(head.getMsgNo());
							agentAccessLogDetailNew.setTxnMode(head.getMode());

							agentAccessLogDetailNew.setTxnDate(formatedDate);

							agentAccessLogDetailNew.setTxnCount(1L);

							agentAccessLogDetailNew.setServicePoint(
									head.getServPointId());

							agentService.save(agentAccessLogDetailNew);
							
								
							
							
						}
						
					}
				}
				if(proposePlantArea!=null && !StringUtil.isEmpty(proposePlantArea)){
					existingFarm.getFarmDetailedInfo().setProposedPlantingArea(proposePlantArea);
				}
				//existingFarm.getFarmDetailedInfo().setTotalLandHolding(totalLandHolding!=null && !StringUtil.isEmpty(totalLandHolding)?formatter.format(Double.valueOf(totalLandHolding)):null);
				existingFarm.setPlotCapturingTime(DateUtil.convertStringToDate(head.getTxnTime(), DateUtil.TXN_DATE_TIME));
				//existingFarm.getFarmDetailedInfo().setProposedPlantingArea(proposePlantArea!=null && !StringUtil.isEmpty(proposePlantArea)?formatter.format(Double.valueOf(proposePlantArea)):null);
			farmerService.editFarm(existingFarm);
			}
			}
		}
		
		
		catch (Exception e) {
			if (mode == ESETxn.ONLINE_MODE) {
				e.printStackTrace();
				throw new SwitchException(
						e instanceof SwitchException ? ((SwitchException) e).getCode() : ITxnErrorCodes.ERROR);
			} 
		}
		
		
		return new HashMap();
		
		
	}
	
	
	private Set<Coordinates> buildFarmLandArea(Collection collection,Farm existingFarm) {

		Set<Coordinates> returnSet = null;
		if (!ObjectUtil.isEmpty(collection)) {
			returnSet = new LinkedHashSet<Coordinates>();
			List<com.sourcetrace.eses.txn.schema.Object> coordinatesList = collection.getObject();
			for (com.sourcetrace.eses.txn.schema.Object coordinatesObj : coordinatesList) {
				Coordinates coordinates = new Coordinates();
				coordinates.setFarm(existingFarm);
				List<Data> coordinatesDataList = coordinatesObj.getData();

				for (Data data : coordinatesDataList) {
					String key = data.getKey();
					String value = data.getValue();
					if (TxnEnrollmentProperties.FARM_LAND_AREA_LATITUDE.equalsIgnoreCase(key)) {
						coordinates.setLatitude(value);
					}
					if (TxnEnrollmentProperties.FARM_LAND_AREA_LONGITUDE.equalsIgnoreCase(key)) {
						coordinates.setLongitude(value);
					}
					if (TxnEnrollmentProperties.FARM_LAND_AREA_ORDER_NO.equalsIgnoreCase(key)) {
						if (!StringUtil.isEmpty(value)) {
							coordinates.setOrderNo(Long.valueOf(value));
						} else {
							coordinates.setOrderNo(0);
						}
					}
				}
				existingFarm.setPlottingStatus(1);
				returnSet.add(coordinates);
			}
		}
		return returnSet;
	}
	
	

		@Override
	public Map<?, ?> processVoid(Map<?, ?> reqData) {
		return null;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getFlatitude() {
		return flatitude;
	}

	public void setFlatitude(String flatitude) {
		this.flatitude = flatitude;
	}

	public String getFlongitude() {
		return flongitude;
	}

	public void setFlongitude(String flongitude) {
		this.flongitude = flongitude;
	}

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public IAgentService getAgentService() {
		return agentService;
	}

	public void setAgentService(IAgentService agentService) {
		this.agentService = agentService;
	}

	public IDeviceService getDeviceService() {
		return deviceService;
	}

	public void setDeviceService(IDeviceService deviceService) {
		this.deviceService = deviceService;
	}
	
	
	
}
