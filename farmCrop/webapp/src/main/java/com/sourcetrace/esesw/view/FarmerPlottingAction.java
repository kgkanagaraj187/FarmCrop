package com.sourcetrace.esesw.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.view.ESEAction;
import com.google.gson.Gson;
import com.sourcetrace.eses.entity.Warehouse;
//import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.service.IFarmCropsService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Coordinates;
import com.sourcetrace.esesw.entity.profile.CoordinatesMap;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.FarmCropsCoordinates;
import com.sourcetrace.esesw.entity.profile.FarmCropsCoordinates.typesOFCordinates;
import com.sourcetrace.esesw.entity.profile.Farmer;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

@SuppressWarnings("serial")
public class FarmerPlottingAction extends ESEAction {

	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private IFarmCropsService farmCropsService;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private IProductService productService;
	private Farm farm;

	private String farmerId;
	private String farmId;
	private String selectedSeason;
	private String farmCode;

	List<Warehouse> samithi = new ArrayList<Warehouse>();
	List<Farmer> farmer = new ArrayList<Farmer>();
	private String selectedGroup;

	private String selectedFarmer;

	private String selectedFarm;
	private String selectedFarmCrop;
	private String selectedFarmType;
	private String latLon;
	private Map<String, String> farmTypeList = new LinkedHashMap<String, String>();
	private String area;
	private String farmLatLon;

	public String list() throws Exception {

		return LIST;
	}

	/*
	 * public void create() throws Exception { if
	 * (!StringUtil.isEmpty(getLatLon())) { Type coordsType = new
	 * TypeToken<List<Coordinates>>() { }.getType();
	 * 
	 * List<Coordinates> coordList = new Gson().fromJson(getLatLon(),
	 * coordsType); if(!ObjectUtil.isListEmpty(coordList)){ Farm farm =
	 * farmerService.findFarmById(Long.valueOf(selectedFarm));
	 * farmerService.removeFarmCoordinates(Long.valueOf(farm.getId())); for
	 * (Coordinates coordinates : coordList) {
	 * 
	 * coordinates.setFarm(farm);
	 * coordinates.setLatitude(coordinates.getLatitude());
	 * coordinates.setLongitude(coordinates.getLongitude());
	 * coordinates.setOrderNo(coordinates.getOrderNo());
	 * farmerService.save(coordinates); }
	 * 
	 * } } }
	 */

	public void create() throws Exception {
		if (!StringUtil.isEmpty(getLatLon())) {
			Type coordsType = new TypeToken<List<Coordinates>>() {
			}.getType();

			Type farmcropCoordsType = new TypeToken<List<FarmCropsCoordinates>>() {
			}.getType();

			List<Coordinates> coordList = new Gson().fromJson(getLatLon(), coordsType);
			List<FarmCropsCoordinates> farmCropCoordList = new Gson().fromJson(getLatLon(), farmcropCoordsType);
			if (!ObjectUtil.isListEmpty(coordList) && farmCode != null) {

				if (farmCode.equals("2")) {

					FarmCrops farmCrops = farmerService.findByFarmCropsId(Long.valueOf(selectedFarm));
					farmCrops.setCultiArea(area);
					Set<FarmCropsCoordinates> returnSet = new LinkedHashSet<FarmCropsCoordinates>();
					CoordinatesMap coordinatesMap=new CoordinatesMap();
					for (FarmCropsCoordinates farmcropCoordinates : farmCropCoordList) {
						
						if(farmcropCoordinates.getLatitude() == null && farmcropCoordinates.getLongitude() == null && farmcropCoordinates.getOrderNo() == 0){
							
							farmCropCoordList.stream().filter(i -> i.getOrderNo() == 1).forEach(j->{
								FarmCropsCoordinates firstPlotting = j;
								farmcropCoordinates.setFarmCrops(farmCrops);
								farmcropCoordinates.setLatitude(firstPlotting.getLatitude());
								farmcropCoordinates.setLongitude(firstPlotting.getLongitude());
								farmcropCoordinates.setOrderNo(farmcropCoordinates.getOrderNo());
								farmcropCoordinates.setType(typesOFCordinates.mainCoordinates.ordinal());
							});
							returnSet.add(farmcropCoordinates);
						}else{
							farmcropCoordinates.setFarmCrops(farmCrops);
							farmcropCoordinates.setLatitude(farmcropCoordinates.getLatitude());
							farmcropCoordinates.setLongitude(farmcropCoordinates.getLongitude());
							farmcropCoordinates.setOrderNo(farmcropCoordinates.getOrderNo());
							farmcropCoordinates.setType(typesOFCordinates.mainCoordinates.ordinal());
							returnSet.add(farmcropCoordinates);
						}
						
						//farmerService.save(farmcropCoordinates);
						
					}
					
					farmCropCoordList.stream().forEach(j -> {
						j.getNearByLandDetails().stream().forEach(i -> {
							FarmCropsCoordinates farmcropCoordinates = new FarmCropsCoordinates();
							farmcropCoordinates.setFarmCrops(farmCrops);
							farmcropCoordinates.setLatitude(i.getLat());
							farmcropCoordinates.setLongitude(i.getLng());
							farmcropCoordinates.setTitle(i.getTitle());
							farmcropCoordinates.setDescription(i.getDescription());
							farmcropCoordinates.setType(typesOFCordinates.neighbouringDetails_farmcrops.ordinal());
							returnSet.add(farmcropCoordinates);
						});
					});
					
					
					
					coordinatesMap.setFarmCropsCoordinates(returnSet);
					coordinatesMap.setAgentId(getUsername());
					coordinatesMap.setArea(farmCrops.getFarm().getFarmDetailedInfo().getTotalLandHolding());
					coordinatesMap.setDate(new Date());
					coordinatesMap.setFarmCrops(farmCrops);
					coordinatesMap.setMidLatitude(farmCrops.getLongitude());
					coordinatesMap.setMidLongitude(farmCrops.getLongitude());
					coordinatesMap.setStatus(CoordinatesMap.Status.ACTIVE.ordinal());
					if(farmCrops.getCoordinatesMap()!=null && !ObjectUtil.isListEmpty(farmCrops.getCoordinatesMap())){
						farmCrops.getCoordinatesMap().stream().forEach(co->{
							co.setStatus(CoordinatesMap.Status.INACTIVE.ordinal());
						});
						farmCrops.getCoordinatesMap().add(coordinatesMap);
						}
						else{
							Set<CoordinatesMap> coMap=new LinkedHashSet<>();
							coMap.add(coordinatesMap);
							farmCrops.setCoordinatesMap(coMap);
							
						}
					farmCrops.setActiveCoordinates(coordinatesMap);
					
					
					
					farmCropsService.editFarmCrops(farmCrops);
					//farmerService.removeFarmCropCoordinates(Long.valueOf(farmCrops.getId()));
					

				} else {

					Farm farm = farmerService.findFarmById(Long.valueOf(selectedFarm));

					/*
					 * if (farmCode.equals("1")) {
					 * farm.getFarmDetailedInfo().setTotalLandHolding(area);
					 * farmerService.editFarm(farm);
					 * 
					 * } else
					 */
					if (farmCode.equals("1")) {
						farm.getFarmDetailedInfo().setProposedPlantingArea(area);

						if(!StringUtil.isEmpty(farmLatLon)){
							String[] tempArray = farmLatLon.substring(farmLatLon.indexOf("(") + 1, farmLatLon.lastIndexOf(")")).split(",");
							farm.setLatitude(tempArray[0]);
							farm.setLongitude(tempArray[1]);
						}
						
						
					}
					CoordinatesMap coordinatesMap=new CoordinatesMap();
					//farmerService.removeFarmCoordinates(Long.valueOf(farm.getId()));
					Set<Coordinates> returnSet = new LinkedHashSet<Coordinates>();
					/*for (Coordinates coordinates : coordList) {
						coordinates.setFarm(farm);
						coordinates.setLatitude(coordinates.getLatitude());
						coordinates.setLongitude(coordinates.getLongitude());
						coordinates.setOrderNo(coordinates.getOrderNo());
						returnSet.add(coordinates);
						//farmerService.save(coordinates);
					}*/
					
					coordList.stream().forEachOrdered(i -> {
						if(i.getLatitude() == null && i.getLongitude() == null && i.getOrderNo() == 0){
							coordList.stream().filter(j -> j.getOrderNo() == 1).forEach(j->{
								Coordinates firstPlotting = j;
								i.setFarm(farm);
								i.setLatitude(firstPlotting.getLatitude());
								i.setLongitude(firstPlotting.getLongitude());
								i.setOrderNo(i.getOrderNo());
								i.setType(typesOFCordinates.mainCoordinates.ordinal());
								returnSet.add(i);
							});
						}else{
							i.setFarm(farm);
							i.setLatitude(i.getLatitude());
							i.setLongitude(i.getLongitude());
							i.setOrderNo(i.getOrderNo());
							i.setType(typesOFCordinates.mainCoordinates.ordinal());
							returnSet.add(i);
						}
					});
					
					coordList.stream().forEach(j -> {
						j.getNearByLandDetails().stream().forEach(i -> {
							Coordinates neighbouringPoints = new Coordinates();
							neighbouringPoints.setFarm(farm);
							neighbouringPoints.setLatitude(i.getLat());
							neighbouringPoints.setLongitude(i.getLng());
							neighbouringPoints.setTitle(i.getTitle());
							neighbouringPoints.setDescription(i.getDescription());
							neighbouringPoints.setType(typesOFCordinates.neighbouringDetails_farm.ordinal());
							returnSet.add(neighbouringPoints);
						});
					});
					
					
					coordinatesMap.setFarmCoordinates(returnSet);
					coordinatesMap.setAgentId(getUsername());
					coordinatesMap.setArea(farm.getFarmDetailedInfo().getTotalLandHolding());
					coordinatesMap.setDate(	new Date());
					coordinatesMap.setFarm(farm);
					coordinatesMap.setMidLatitude(farm.getLongitude());
					coordinatesMap.setMidLongitude(farm.getLongitude());
					coordinatesMap.setStatus(CoordinatesMap.Status.ACTIVE.ordinal());
					if(farm.getCoordinatesMap()!=null && !ObjectUtil.isEmpty(farm.getCoordinatesMap())){
					farm.getCoordinatesMap().stream().forEach(co->{
						co.setStatus(CoordinatesMap.Status.INACTIVE.ordinal());
					});
					farm.getCoordinatesMap().add(coordinatesMap);
					}else{
						Set<CoordinatesMap> coMap=new LinkedHashSet<>();
						coMap.add(coordinatesMap);
						farm.setCoordinatesMap(coMap);
					}
					
					
					farm.setActiveCoordinates(coordinatesMap);
					farmerService.editFarm(farm);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void populateFarmsMap() throws Exception {

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();

		Farm farm = new Farm();

		if (!StringUtil.isEmpty(selectedFarm)) {
			farm.setFarmId(selectedFarm);
		}

		farm = farmerService.listOfFarmCoordinateByFarmId(Long.valueOf(selectedFarm));

		if (!ObjectUtil.isEmpty(farm)) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", farm.getId());
			jsonObject.put("farmName", farm.getFarmName());
			jsonObject.put("latitude", farm.getLatitude());
			jsonObject.put("longtitude", farm.getLongitude());
			jsonObjects.add(jsonObject);
		}
		printAjaxResponse(jsonObjects, "text/html");
	}

	protected void printAjaxResponse(Object value, String contentType) {

		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			if (!StringUtil.isEmpty(contentType)) {
				response.setContentType(contentType);
			}
			out = response.getWriter();
			out.print(value);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}

	public String getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}

	public String plotting() {
		return "plotting";
	}

	public String getSelectedSeason() {
		return selectedSeason;
	}

	public void setSelectedSeason(String selectedSeason) {
		this.selectedSeason = selectedSeason;
	}

	public Map<Long, String> getGroupList() {

		samithi = locationService.listSamithiesBasedOnType();
		Map<Long, String> samithiMap = new LinkedHashMap<>();
		samithiMap = samithi.stream().collect(Collectors.toMap(Warehouse::getId, Warehouse::getName));
		return samithiMap;
	}

	public Map<String, String> getFarmersList() {

		Map<String, String> farmersListMap = new LinkedHashMap<String, String>();
		List<Farmer> farmersList = farmerService.listFarmer();
		if (!ObjectUtil.isListEmpty(farmersList)) {
			for (Farmer farmer : farmersList) {
				farmersListMap.put(farmer.getFarmerId(), farmer.getFirstName() + "-" + farmer.getFarmerId());
			}
		}
		return farmersListMap;
	}

	public void populateFarmer() throws Exception {
		if (!selectedGroup.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedGroup))) {
			List<Object[]> listFarmer = farmerService.listFarmerBySamithiId(Long.valueOf(selectedGroup));
			JSONArray farmerArr = new JSONArray();
			if (!ObjectUtil.isEmpty(listFarmer)) {
				for (Object[] farmer : listFarmer) {
					farmerArr.add(getJSONObject(String.valueOf(farmer[3]),
							String.valueOf(farmer[1]) + " " + String.valueOf(farmer[2])));
				}
			}
			sendAjaxResponse(farmerArr);
		}
	}

	public void populateFarm() throws Exception {
		if (!selectedFarmer.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedFarmer))) {
			List<Object[]> listFarm = farmerService.listFarmFieldsByFarmerId(Long.valueOf(selectedFarmer));
			JSONArray farmArr = new JSONArray();
			if (!ObjectUtil.isEmpty(listFarm)) {
				for (Object[] farm : listFarm) {
					// if(farm.getLatitude()!=null &&
					// farm.getLatitude().equals("0")){
					if (farm[4] != null && farm[5]!= null) {
						farmArr.add(getJSONObject(
								String.valueOf(farm[0].toString()) + "~" + farm[4].toString() + "~" + farm[5].toString(),
								String.valueOf(farm[2].toString())));
					} else {
						farmArr.add(
								getJSONObject(String.valueOf(farm[0].toString()) + "~ ", String.valueOf(farm[2].toString())));
					}
				}
			}
			sendAjaxResponse(farmArr);
		}
	}

	public void populateFarmCrop() throws Exception {
		if (!selectedFarm.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedFarm))) {

			List<FarmCrops> listFarmCrop = farmerService.listFarmCropsByFarmId(Long.valueOf(selectedFarm));
			JSONArray farmCropArr = new JSONArray();
			if (!ObjectUtil.isEmpty(listFarmCrop)) {
				for (FarmCrops farmCrop : listFarmCrop) {
					farmCropArr.add(getJSONObject(String.valueOf(farmCrop.getId()),
							String.valueOf(farmCrop.getProcurementVariety().getProcurementProduct().getName())));
				}
			}

			sendAjaxResponse(farmCropArr);
		}
	}

	public String getFarmCode() {
		return farmCode;
	}

	public void setFarmCode(String farmCode) {
		this.farmCode = farmCode;
	}

	public String getSelectedGroup() {
		return selectedGroup;
	}

	public void setSelectedGroup(String selectedGroup) {
		this.selectedGroup = selectedGroup;
	}

	public String getSelectedFarmer() {
		return selectedFarmer;
	}

	public void setSelectedFarmer(String selectedFarmer) {
		this.selectedFarmer = selectedFarmer;
	}

	public String getSelectedFarm() {
		return selectedFarm;
	}

	public void setSelectedFarm(String selectedFarm) {
		this.selectedFarm = selectedFarm;
	}

	public String getLatLon() {
		return latLon;
	}

	public void setLatLon(String latLon) {
		this.latLon = latLon;
	}

	public Farm getFarm() {
		return farm;
	}

	public void setFarm(Farm farm) {
		this.farm = farm;
	}

	public String getFarmId() {
		return farmId;
	}

	public void setFarmId(String farmId) {
		this.farmId = farmId;
	}

	public Map<String, String> getFarmTypeList() {

		/*
		 * farmTypeList.put("1", "Total Land Holding Area");
		 * farmTypeList.put("2", "Proposed planting Area");
		 * farmTypeList.put("3", "Cultivation Area");
		 */
		// farmTypeList.put("1", "Total Land Holding Area");
		
		if(getCurrentTenantId().equalsIgnoreCase("wilmar")){
			farmTypeList.put("1", "Total Farm Coconut Area");
			farmTypeList.put("2", "Total Crop Coconut Area");
		}else{
			farmTypeList.put("1", "Proposed planting Area");
		farmTypeList.put("2", "Cultivation Area");
		}

		return farmTypeList;
	}

	public void setFarmTypeList(Map<String, String> farmTypeList) {
		this.farmTypeList = farmTypeList;
	}

	public String getSelectedFarmCrop() {
		return selectedFarmCrop;
	}

	public void setSelectedFarmCrop(String selectedFarmCrop) {
		this.selectedFarmCrop = selectedFarmCrop;
	}

	public String getSelectedFarmType() {
		return selectedFarmType;
	}

	public void setSelectedFarmType(String selectedFarmType) {
		this.selectedFarmType = selectedFarmType;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public IFarmCropsService getFarmCropsService() {
		return farmCropsService;
	}

	public void setFarmCropsService(IFarmCropsService farmCropsService) {
		this.farmCropsService = farmCropsService;
	}

	public String getFarmLatLon() {
		return farmLatLon;
	}

	public void setFarmLatLon(String farmLatLon) {
		this.farmLatLon = farmLatLon;
	}
	@SuppressWarnings("unchecked")
	public void populateFarmCoordinates() throws Exception {
		JSONObject js = new JSONObject()	;
		JSONArray panchayathArr = new JSONArray();
		JSONArray neighbouringDetails_farmCrops = new JSONArray();
		JSONArray neighbouringDetails_farm = new JSONArray();
		
		if (!selectedFarm.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedFarm)) && !selectedFarmType.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedFarmType))) {
			if(selectedFarmType.equals("1")){
		Farm f = farmerService.findFarmById(Long.valueOf(selectedFarm));	
		
		if (!ObjectUtil.isEmpty(f.getActiveCoordinates()) && !ObjectUtil.isListEmpty(f.getActiveCoordinates().getFarmCoordinates())) {
			f.getActiveCoordinates().getFarmCoordinates().stream().forEach(coordinateObj ->{
				JSONObject jsonObject = new JSONObject();
				// jsonObject.put("orderNo",!ObjectUtil.isEmpty(coordinateObj.getOrderNo())
				// ? coordinateObj.getOrderNo():"");
				
				if(coordinateObj.getType() == typesOFCordinates.mainCoordinates.ordinal()){
					
					jsonObject.put("lat",!ObjectUtil.isEmpty(coordinateObj.getLatitude()) ? coordinateObj.getLatitude() : "");
					jsonObject.put("lon",!ObjectUtil.isEmpty(coordinateObj.getLongitude()) ? coordinateObj.getLongitude() : "");
					panchayathArr.add(jsonObject);
					
				}else if(coordinateObj.getType() == typesOFCordinates.neighbouringDetails_farm.ordinal()){
					jsonObject.put("type",coordinateObj.getType());
					jsonObject.put("title",coordinateObj.getTitle());
					jsonObject.put("description",coordinateObj.getDescription());
					jsonObject.put("lat",!ObjectUtil.isEmpty(coordinateObj.getLatitude()) ? coordinateObj.getLatitude() : "");
					jsonObject.put("lng",!ObjectUtil.isEmpty(coordinateObj.getLongitude()) ? coordinateObj.getLongitude() : "");
					neighbouringDetails_farm.add(jsonObject);
				}
			});
		
		}
		js.put("area",f.getFarmDetailedInfo().getProposedPlantingArea());
		js.put("coord",panchayathArr);
		js.put("neighbouringDetails_farm", neighbouringDetails_farm);
			}else{
				FarmCrops f = farmerService.findByFarmCropsId(Long.valueOf(selectedFarm));
				
				if (!ObjectUtil.isEmpty(f.getActiveCoordinates())
						&& !ObjectUtil.isListEmpty(f.getActiveCoordinates().getFarmCropsCoordinates())) {
					f.getActiveCoordinates().getFarmCropsCoordinates().stream().sorted((p1, p2) -> (Integer.valueOf(String.valueOf(p1.getOrderNo()))).compareTo((Integer.valueOf(String.valueOf(p2.getOrderNo()))))).forEach(coordinateObj ->{
						JSONObject jsonObject = new JSONObject();
						// jsonObject.put("orderNo",!ObjectUtil.isEmpty(coordinateObj.getOrderNo())
						// ? coordinateObj.getOrderNo():"");
						
						if(coordinateObj.getType() == typesOFCordinates.mainCoordinates.ordinal()){
							
							jsonObject.put("lat",
									!ObjectUtil.isEmpty(coordinateObj.getLatitude()) ? coordinateObj.getLatitude() : "");
							jsonObject.put("lon",
									!ObjectUtil.isEmpty(coordinateObj.getLongitude()) ? coordinateObj.getLongitude() : "");
							panchayathArr.add(jsonObject);
						}else if(coordinateObj.getType() == typesOFCordinates.neighbouringDetails_farmcrops.ordinal()){
							jsonObject.put("type",coordinateObj.getType());
							jsonObject.put("title",coordinateObj.getTitle());
							jsonObject.put("description",coordinateObj.getDescription());
							jsonObject.put("lat",!ObjectUtil.isEmpty(coordinateObj.getLatitude()) ? coordinateObj.getLatitude() : "");
							jsonObject.put("lng",!ObjectUtil.isEmpty(coordinateObj.getLongitude()) ? coordinateObj.getLongitude() : "");
							neighbouringDetails_farmCrops.add(jsonObject);
						}
						
					});
				
				}
				js.put("area",f.getCultiArea());
				js.put("coord",panchayathArr);
				js.put("neighbouringDetails_farmCrops",neighbouringDetails_farmCrops);
				
			}
		}
		
		sendAjaxResponse(js);
	}

}
