/**
 * NDVIAction.java
 * Copyright (c) 2019-2020, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

package com.sourcetrace.esesw.view.general;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Coordinates;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.NdviProcess;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

/**
 * The Class CountryAction.
 */
public class NdviProcessAction extends SwitchValidatorAction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(NdviProcessAction.class);
	@Autowired
	private ILocationService locationService;
	@Autowired
	private IUniqueIDGenerator idGenerator;
	@Autowired
	private IPreferencesService preferncesService;
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private IAgentService agentService;

	// Pojo Entity.
	private NdviProcess ndviProcess;

	// Action Methods.
	private String farmerId;
	private Map<Long, String> farmerListMap = new HashMap<Long, String>();
	private Map<Long, String> farmListMap = new HashMap<Long, String>();
	private String farmerIDGCParcel;
	private Map<String, String> farmersWithGCParcelIds = new LinkedHashMap<String, String>();
	private Map<String, String> tmpFarmWithGCParcelIds = new LinkedHashMap<String, String>();
	private String farmId;
	private String selectedFarmType;
	private String selectedFarm;
	private String gcParcelId;
	private String farmCropId;
	
	public static enum typesOFCordinates {
		mainCoordinates,neighbouringDetails_farmcrops,neighbouringDetails_farm
	};

	public void populateFarmerGeoFarms() {
		List<Object[]> farms = new ArrayList<Object[]>();
		JSONArray farmsJsonArray = new JSONArray();
		if (!StringUtil.isEmpty(farmerId)) {
			farms = farmerService.listFarmsOfFarmerId(Long.valueOf(farmerId));
			for (Object[] farm : farms) {
				if (!ObjectUtil.isEmpty(farm[0]) && !ObjectUtil.isEmpty(farm[1])) {
					farmsJsonArray.add(getJSONObject(String.valueOf(farm[0]), String.valueOf(farm[1])));
				}
			}
		}
		sendAjaxResponse(farmsJsonArray);
	}

	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}

	@Override
	public Object getData() {		
		return ndviProcess;
	}

	public ILocationService getLocationService() {
		return locationService;
	}

	public void setLocationService(ILocationService locationService) {
		this.locationService = locationService;
	}

	public IUniqueIDGenerator getIdGenerator() {
		return idGenerator;
	}

	public void setIdGenerator(IUniqueIDGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	public IAgentService getAgentService() {
		return agentService;
	}

	public void setAgentService(IAgentService agentService) {
		this.agentService = agentService;
	}

	public Map<Long, String> getFarmerListMap() {
		List<Object[]> farmerCodeList = farmerService.listFarmersWithIdAndCode();
		for (Object[] farmers : farmerCodeList) {
			farmerListMap.put(Long.valueOf(String.valueOf(farmers[0])), String.valueOf(farmers[1]));
		}
		return farmerListMap;
	}

	public void setFarmerListMap(Map<Long, String> farmerListMap) {
		this.farmerListMap = farmerListMap;
	}

	public Map<Long, String> getFarmListMap() {
		return farmListMap;
	}

	public void setFarmListMap(Map<Long, String> farmListMap) {
		this.farmListMap = farmListMap;
	}

	public NdviProcess getNdviProcess() {
		return ndviProcess;
	}

	public void setNdviProcess(NdviProcess ndviProcess) {
		this.ndviProcess = ndviProcess;
	}

	public String getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}

	public String getFarmerIDGCParcel() {
		return farmerIDGCParcel;
	}

	public void setFarmerIDGCParcel(String farmerIDGCParcel) {
		this.farmerIDGCParcel = farmerIDGCParcel;
	}

	public String getFarmId() {
		return farmId;
	}

	public void setFarmId(String farmId) {
		this.farmId = farmId;
	}

	public void setFarmersWithGCParcelIds(Map<String, String> farmersWithGCParcelIds) {
		this.farmersWithGCParcelIds = farmersWithGCParcelIds;
	}
	
	public Map<String, String> getTmpFarmWithGCParcelIds() {
		return tmpFarmWithGCParcelIds;
	}

	public void setTmpFarmWithGCParcelIds(Map<String, String> tmpFarmWithGCParcelIds) {
		this.tmpFarmWithGCParcelIds = tmpFarmWithGCParcelIds;
	}


	public void populateFarmsWithGcParcelIDs() {
		JSONArray farmWithGcArr = new JSONArray();
		JSONArray farmsWithGcIds = new JSONArray();
		List<Object[]> farmerWithFarmList = new ArrayList<Object[]>();
		if (!StringUtil.isEmpty(farmerIDGCParcel)) {
			List<Object[]> farmerList = farmerService.listFarmParcelIdsByFarmer(farmerIDGCParcel);
			farmerWithFarmList = farmerService.listFarmWithoutCropParcelIdsByFarmer(farmerIDGCParcel);
			farmerList.addAll(farmerWithFarmList);
			if (!ObjectUtil.isListEmpty(farmerList)) {
				farmerList.forEach(obj -> {
					String parcelId = "";
					if (obj.length >= 3) {
						if (!StringUtil.isEmpty(String.valueOf(obj[2])) && !String.valueOf(obj[2]).equals("null")) {
							parcelId = String.valueOf(obj[2]);
						}
						farmersWithGCParcelIds.put(String.valueOf(obj[0]) + "~" + parcelId, String.valueOf(obj[1]));
					}
				});
			}
			for (Map.Entry<String, String> entry : farmersWithGCParcelIds.entrySet()) {
				farmWithGcArr.add(getJSONObject(entry.getKey(), entry.getValue()));
			}
		}
		sendAjaxResponse(farmWithGcArr);

	}

	public void populateFarmersWithGcParcelIDs() {
		List<Object[]> farmerList = farmerService.listFarmersWithFarmParcelIds();
		List<Object[]> farmmerListCrops = farmerService.listFarmersWithFarmCropParcelIds();
		farmerList.addAll(farmmerListCrops);
		JSONArray farmerWithGcArr = new JSONArray();
		JSONArray farmsWithGcIds = new JSONArray();
		if (!ObjectUtil.isListEmpty(farmerList)) {
			farmerList.forEach(obj -> {
				farmerWithGcArr.add(getJSONObject((String.valueOf(obj[0])), String.valueOf(obj[1])));
			});
		}
		sendAjaxResponse(farmerWithGcArr);

	}

	/*
	 * public Map<String, String> getFarmersWithGCParcelIds() { //Commented to
	 * get farmers with existing GC Parcel IDs
	 * 
	 * List<Object[]> farmerList = farmerService.listFarmersWithFarmParcelIds();
	 * List<Object[]> farmmerListCrops =
	 * farmerService.listFarmersWithFarmCropParcelIds();
	 * farmerList.addAll(farmmerListCrops); JSONArray farmerWithGcArr = new
	 * JSONArray(); JSONArray farmsWithGcIds = new JSONArray(); if
	 * (!ObjectUtil.isListEmpty(farmerList)) { farmerList.forEach(obj -> {
	 * farmersWithGCParcelIds.put(String.valueOf(obj[0]),
	 * String.valueOf(obj[1])); }); }
	 * 
	 * return farmersWithGCParcelIds; }
	 */

	public void populateFarmsCropsWithGcParcelIDs() {

		JSONArray farmCropsWithGcArr = new JSONArray();
		if (!StringUtil.isEmpty(farmId)) {
			List<Object[]> farmCropsList = farmerService.listFarmCropsWithGcParcelIdsByFarm(farmId);
			if (!ObjectUtil.isListEmpty(farmCropsList)) {
				farmCropsList.forEach(obj -> {
					if (obj.length >= 3) {
						farmCropsWithGcArr.add(getJSONObject(String.valueOf(obj[2]), String.valueOf(obj[1])));
					}
				});
			}
		}
		sendAjaxResponse(farmCropsWithGcArr);

	}

	public void populateCheckFarmHasGcParcelId() { // Function to check if
													// selected Farm Has 'GC
													// Parcel ID'
		boolean farmHasParcelId = false;
		if (!StringUtil.isEmpty(farmId)) {
			farmHasParcelId = farmerService.findIfFarmHasParcelId(farmId);
		}
		sendAjaxResponse(String.valueOf(farmHasParcelId));

	}

	public void populateFarmsCropsBySelectedFarm() {

		JSONArray farmCropsWithGcArr = new JSONArray();
		if (!StringUtil.isEmpty(farmId)) {
			List<Object[]> farmCropsList = farmerService.listFarmCropsByFarm(farmId);
			if (!ObjectUtil.isListEmpty(farmCropsList)) {
				farmCropsList.forEach(obj -> {
					if (obj.length >= 3) {
						farmCropsWithGcArr.add(getJSONObject(String.valueOf(obj[2]), String.valueOf(obj[1])));
					}
				});
			}
		}
		sendAjaxResponse(farmCropsWithGcArr);

	}

	public Map<String, String> getFarmersWithGCParcelIds() {

		List<Object[]> farmerWithFarmCoordList = new ArrayList<Object[]>();
		farmerWithFarmCoordList = farmerService.listFarmersWithFarmCoordinates();
		if (!ObjectUtil.isListEmpty(farmerWithFarmCoordList)) {
			farmerWithFarmCoordList.forEach(obj -> {
				farmersWithGCParcelIds.put(String.valueOf(obj[0]), String.valueOf(obj[1]));
			});
		}

		return farmersWithGCParcelIds;
	}		
	
	public String getSelectedFarmType() {
		return selectedFarmType;
	}

	public void setSelectedFarmType(String selectedFarmType) {
		this.selectedFarmType = selectedFarmType;
	}

	public void populateFarmsOfSelectedFarmer() {
		JSONArray farmWithGcArr = new JSONArray();
		JSONArray farmsWithGcIds = new JSONArray();
		if (!StringUtil.isEmpty(farmerId)) {
			List<Object[]> farmList = farmerService.listFarmWithCropParcelIdsByFarmer(farmerId);
			if (!ObjectUtil.isListEmpty(farmList)) {
				farmList.forEach(obj -> {
					String parcelId = "";
					if (obj.length >= 3) {
						if (!StringUtil.isEmpty(String.valueOf(obj[2])) && !String.valueOf(obj[2]).equals("null")) {
							parcelId = String.valueOf(obj[2]);
						}
						tmpFarmWithGCParcelIds.put(String.valueOf(obj[0]) + "~" + parcelId, String.valueOf(obj[1]));
					}
				});
			}
			for (Map.Entry<String, String> entry : tmpFarmWithGCParcelIds.entrySet()) {
				farmWithGcArr.add(getJSONObject(entry.getKey(), entry.getValue()));
			}
		}
		sendAjaxResponse(farmWithGcArr);

	}
	
	public void populateFarmsCrops() {

		JSONArray farmCropsWithGcArr = new JSONArray();
		String[] farmIdVal = null;
		if (!StringUtil.isEmpty(farmId)) {
			farmIdVal = farmId.split("~");
			if (farmIdVal.length >= 1) {
				// List<Object[]> farmCropsList =
				// farmerService.listFarmCropsByFarm(farmIdVal[0]);
				List<Object[]> farmCropsList = farmerService.listFarmCropsWithFarmId(Long.valueOf(farmIdVal[0]));
				if (!ObjectUtil.isListEmpty(farmCropsList)) {
					farmCropsList.forEach(obj -> {
						if (obj.length >= 3) {
							farmCropsWithGcArr.add(
									getJSONObject(
											(!StringUtil.isEmpty(obj[2])
													? (String.valueOf(obj[0]) + "~" + String.valueOf(obj[2]))
													: (String.valueOf(obj[0]) + "~")),
											String.valueOf(obj[1])));
						}
					});
				}
			}
		}
		sendAjaxResponse(farmCropsWithGcArr);

	}
	
	public void populateFarmCoordPolygon() {

		Farm selectedFarm;
		String polygonStr1 = "POLYGON ((";
		String polygonStrVal = "";
		String polygonStr2 = "))";
		JSONObject js = new JSONObject();
		Set<Coordinates> farmCoordinates = new HashSet<Coordinates>();
		List<Coordinates> sortedFarmCoordinatesList = new ArrayList<Coordinates>();
		if (!StringUtil.isEmpty(farmId)) {
			selectedFarm = farmerService.findFarmById(Long.valueOf(farmId));
			if (!ObjectUtil.isEmpty(selectedFarm)) {
				if (!ObjectUtil.isEmpty(selectedFarm.getActiveCoordinates())
						&& !ObjectUtil.isEmpty(selectedFarm.getActiveCoordinates().getFarmCoordinates())) {
					farmCoordinates = selectedFarm.getActiveCoordinates().getFarmCoordinates();
					sortedFarmCoordinatesList = new ArrayList<Coordinates>(farmCoordinates);
					sortedFarmCoordinatesList.sort(Comparator.comparing(Coordinates::getOrderNo));
					for (Coordinates farmCoordinate : sortedFarmCoordinatesList) {
						polygonStrVal += farmCoordinate.getLongitude() + " " + farmCoordinate.getLatitude() + ",";
					}
				}
			}
		}
		polygonStrVal = StringUtil.removeLastComma(polygonStrVal);
		polygonStrVal = polygonStr1 + polygonStrVal + polygonStr2;
		js.put("polygon", polygonStrVal);
		sendAjaxResponse(js);
	}
	
	@SuppressWarnings("unchecked")
	public void populateFarmCoordinates() throws Exception {
		JSONObject js = new JSONObject();
		JSONArray panchayathArr = new JSONArray();
		JSONArray neighbouringDetails_farmCrops = new JSONArray();
		JSONArray neighbouringDetails_farm = new JSONArray();
		String selectedFarm = "";
		if (!StringUtil.isEmpty(farmId)) {
			selectedFarm = farmId;
			if (!selectedFarm.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedFarm))
					&& !selectedFarmType.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedFarmType))) {
				if (selectedFarmType.equals("1")) {
					Farm f = farmerService.findFarmById(Long.valueOf(selectedFarm));

					if (!ObjectUtil.isEmpty(f) && !ObjectUtil.isEmpty(f.getActiveCoordinates())
							&& !ObjectUtil.isListEmpty(f.getActiveCoordinates().getFarmCoordinates())) {
						f.getActiveCoordinates().getFarmCoordinates().stream().forEach(coordinateObj -> {
							JSONObject jsonObject = new JSONObject();
							// jsonObject.put("orderNo",!ObjectUtil.isEmpty(coordinateObj.getOrderNo())
							// ? coordinateObj.getOrderNo():"");

							if (coordinateObj.getType() == typesOFCordinates.mainCoordinates.ordinal()) {

								jsonObject.put("lat", !ObjectUtil.isEmpty(coordinateObj.getLatitude())
										? coordinateObj.getLatitude() : "");
								jsonObject.put("lon", !ObjectUtil.isEmpty(coordinateObj.getLongitude())
										? coordinateObj.getLongitude() : "");
								panchayathArr.add(jsonObject);

							} else if (coordinateObj.getType() == typesOFCordinates.neighbouringDetails_farm
									.ordinal()) {
								jsonObject.put("type", coordinateObj.getType());
								jsonObject.put("title", coordinateObj.getTitle());
								jsonObject.put("description", coordinateObj.getDescription());
								jsonObject.put("lat", !ObjectUtil.isEmpty(coordinateObj.getLatitude())
										? coordinateObj.getLatitude() : "");
								jsonObject.put("lng", !ObjectUtil.isEmpty(coordinateObj.getLongitude())
										? coordinateObj.getLongitude() : "");
								neighbouringDetails_farm.add(jsonObject);
							}
						});

					}
					js.put("area", f.getFarmDetailedInfo().getProposedPlantingArea());
					js.put("coord", panchayathArr);
					js.put("neighbouringDetails_farm", neighbouringDetails_farm);
				} else {
					FarmCrops f = farmerService.findByFarmCropsId(Long.valueOf(selectedFarm));

					if (!ObjectUtil.isEmpty(f) && !ObjectUtil.isEmpty(f.getActiveCoordinates())
							&& !ObjectUtil.isListEmpty(f.getActiveCoordinates().getFarmCropsCoordinates())) {
						f.getActiveCoordinates().getFarmCropsCoordinates().stream()
								.sorted((p1, p2) -> (Integer.valueOf(String.valueOf(p1.getOrderNo())))
										.compareTo((Integer.valueOf(String.valueOf(p2.getOrderNo())))))
								.forEach(coordinateObj -> {
									JSONObject jsonObject = new JSONObject();
									// jsonObject.put("orderNo",!ObjectUtil.isEmpty(coordinateObj.getOrderNo())
									// ? coordinateObj.getOrderNo():"");

									if (coordinateObj.getType() == typesOFCordinates.mainCoordinates.ordinal()) {

										jsonObject.put("lat", !ObjectUtil.isEmpty(coordinateObj.getLatitude())
												? coordinateObj.getLatitude() : "");
										jsonObject.put("lon", !ObjectUtil.isEmpty(coordinateObj.getLongitude())
												? coordinateObj.getLongitude() : "");
										panchayathArr.add(jsonObject);
									} else if (coordinateObj
											.getType() == typesOFCordinates.neighbouringDetails_farmcrops.ordinal()) {
										jsonObject.put("type", coordinateObj.getType());
										jsonObject.put("title", coordinateObj.getTitle());
										jsonObject.put("description", coordinateObj.getDescription());
										jsonObject.put("lat", !ObjectUtil.isEmpty(coordinateObj.getLatitude())
												? coordinateObj.getLatitude() : "");
										jsonObject.put("lng", !ObjectUtil.isEmpty(coordinateObj.getLongitude())
												? coordinateObj.getLongitude() : "");
										neighbouringDetails_farmCrops.add(jsonObject);
									}

								});

					}
					js.put("area", f.getCultiArea());
					js.put("coord", panchayathArr);
					js.put("neighbouringDetails_farmCrops", neighbouringDetails_farmCrops);

				}
			}
		}
		sendAjaxResponse(js);
	}
	
	public void populateSetFarmGcParcelId() {
		Farm farmObj = null;
		String gcParcelId = getGcParcelId();
		if (!StringUtil.isEmpty(selectedFarm)) {
			farmObj = farmerService.findFarmById(Long.valueOf(selectedFarm));
			if (!ObjectUtil.isEmpty(gcParcelId)) {
				farmObj.setGcParcelID(gcParcelId);
				farmerService.update(farmObj);
			}
		}
		sendAjaxResponse("GC Parcel ID Updated Successfully");
	}

	public String getSelectedFarm() {
		return selectedFarm;
	}

	public void setSelectedFarm(String selectedFarm) {
		this.selectedFarm = selectedFarm;
	}

	public String getGcParcelId() {
		return gcParcelId;
	}

	public void setGcParcelId(String gcParcelId) {
		this.gcParcelId = gcParcelId;
	}
	
	@SuppressWarnings("unchecked")
	public void populateFarmCropCoordinates() throws Exception {
		JSONObject js = new JSONObject();
		JSONArray farmCropCoordArr = new JSONArray();				
		String selectedFarmCrop = "";
		if (!StringUtil.isEmpty(farmCropId)) {
			selectedFarmCrop = farmCropId;
			if (!selectedFarmCrop.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedFarmCrop))) {				
					FarmCrops farmCropObj = farmerService.findByFarmCropsId(Long.valueOf(selectedFarmCrop));

					if (!ObjectUtil.isEmpty(farmCropObj) && !ObjectUtil.isEmpty(farmCropObj.getActiveCoordinates())
							&& !ObjectUtil.isListEmpty(farmCropObj.getActiveCoordinates().getFarmCropsCoordinates())) {
						farmCropObj.getActiveCoordinates().getFarmCropsCoordinates().stream()
								.sorted((p1, p2) -> (Integer.valueOf(String.valueOf(p1.getOrderNo())))
										.compareTo((Integer.valueOf(String.valueOf(p2.getOrderNo())))))
								.forEach(coordinateObj -> {
									JSONObject jsonObject = new JSONObject();
								
									if (coordinateObj.getType() == typesOFCordinates.mainCoordinates.ordinal()) {
										jsonObject.put("lat", !ObjectUtil.isEmpty(coordinateObj.getLatitude())
												? coordinateObj.getLatitude() : "");
										jsonObject.put("lon", !ObjectUtil.isEmpty(coordinateObj.getLongitude())
												? coordinateObj.getLongitude() : "");
										farmCropCoordArr.add(jsonObject);
									} 

								});

					}					
					js.put("coord", farmCropCoordArr);

			}
		}
		sendAjaxResponse(js);
	}

	public String getFarmCropId() {
		return farmCropId;
	}

	public void setFarmCropId(String farmCropId) {
		this.farmCropId = farmCropId;
	}
	
	public void populateSetFarmCropGcParcelId() {
		FarmCrops farmCropObj = null;
		String gcParcelId = getGcParcelId();
		if (!StringUtil.isEmpty(farmCropId)) {
			farmCropObj = farmerService.findByFarmCropsId(Long.valueOf(farmCropId));
			if (!ObjectUtil.isEmpty(gcParcelId)) {
				farmCropObj.setGcParcelID(gcParcelId);
				farmerService.update(farmCropObj);
			}
		}
		sendAjaxResponse("GC Parcel ID Updated Successfully");
	}
	
}
