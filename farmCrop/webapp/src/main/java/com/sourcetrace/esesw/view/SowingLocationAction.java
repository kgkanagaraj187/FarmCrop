package com.sourcetrace.esesw.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.script.ScriptException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.ese.entity.util.FarmField;
import com.ese.entity.util.FarmerLocationMapField;
import com.ese.util.Base64Util;
import com.ese.view.ESEAction;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Coordinates;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.FarmCropsCoordinates;
import com.sourcetrace.esesw.entity.profile.FarmIcsConversion;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.Locality;
import com.sourcetrace.esesw.entity.profile.LocationDetail;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.State;
import com.sourcetrace.esesw.entity.profile.Village;

@SuppressWarnings("serial")
public class SowingLocationAction extends ESEAction {

	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private ILocationService locationService;
	private String selectedCrop;
	@Autowired
	private IProductService productService;
	private String selectedState;
	private String selectedLocality;
	private String selectedTaluk;
	private String selectedVillage;
	private String branchIdValue;
	private String farmerId;
	private String selectedSeason;
	private String farmCode;
	private String farmId;
	private String selectedOrganicStatus;
	private String selectedFarmer;
	private String farmCropId;
	private String selectedStatus;
	private String selectedFarm;
	@Autowired
	private IPreferencesService preferencesService;
	private List<Municipality> talukList;
	private List<Village> villageList;
	private Map<String, String> statesList = new LinkedHashMap<String, String>();
	private Map<String, String> listLocalities =new LinkedHashMap<String,String>();
	List<Village> listVillage = new ArrayList<Village>();
	List<Municipality> listCity = new ArrayList<Municipality>();
	Map<String, String> organicStatus = new LinkedHashMap<>();
	private String longtitude;
	private String latitude;
	private String plottingType;
	private String fType;
	DecimalFormat formatter = new DecimalFormat("0.00");
	private List<JSONObject> jsonObjectList;
	public String list() throws Exception {
		setLatitude(preferencesService.findPrefernceByName(ESESystem.DEFAULT_LATITUDE));  
		setLongtitude(preferencesService.findPrefernceByName(ESESystem.DEFAULT_LONGTITUDE));
		setPlottingType(preferencesService.findPrefernceByName(ESESystem.PLOTTING_TYPE_MAP));
		return LIST;
	}

	@SuppressWarnings("unchecked")
	public void populateFarmCropMap() throws Exception {

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<Object[]> farmMapObjectList = new ArrayList<Object[]>();
		
		FarmCrops crops = new FarmCrops();
		Farm farm = new Farm();
		Farmer farmer = new Farmer();
	
		if (!StringUtil.isEmpty(selectedCrop)) {
			crops.setCropCode(selectedCrop);
		}

		if (!StringUtil.isEmpty(selectedTaluk)) {
			   farmer.setCity(new Municipality());
			   farmer.getCity().setId(Long.valueOf(selectedTaluk));
			   farm.setFarmer(farmer);
		 }
		
		if (!StringUtil.isEmpty(selectedVillage)) {

			farmer.setVillage(new Village());
			farmer.getVillage().setId(Long.valueOf(selectedVillage));
			farm.setFarmer(farmer);
			crops.setFarm(farm);
		}
		if (!StringUtil.isEmpty(selectedState)){
			Locality l=new Locality();
			State s=locationService.findStateById(Long.parseLong(selectedState));
			l.setState(s);
			Municipality c=new Municipality();
			c.setLocality(l);
			farmer.setCity(c);
			farm.setFarmer(farmer);
		}
		if (!StringUtil.isEmpty(selectedLocality)) {
			Municipality c = new Municipality();
			Locality l =locationService.findLocalityById(Long.parseLong(selectedLocality));
			c.setLocality(l);
			farmer.setCity(c);
			farm.setFarmer(farmer);
		}
		if (!StringUtil.isEmpty(selectedFarmer)) {
			
			Farmer fm = farmerService.findFarmerById(Long.valueOf(selectedFarmer));
			//farmer.setId(Long.valueOf(selectedFarmer));
			farm.setFarmer(fm);
		}else{
			farm.setFarmer(farmer);
		}
		
		farmer.setBranchId(getBranchId());
		crops.setFarm(farm);
		String plottingTyp=preferencesService.findPrefernceByName(ESESystem.PLOTTING_TYPE_MAP);		
		if(!StringUtil.isEmpty(plottingTyp)&&plottingTyp.equalsIgnoreCase("1")){
		farmMapObjectList=farmerService.listFarmerFarmInfoFarmCropsByVillageId(farm,selectedStatus,plottingTyp);
		}
		else if(!StringUtil.isEmpty(plottingTyp)&&plottingTyp.equalsIgnoreCase("2")){
		farmMapObjectList=farmerService.listFarmerFarmInfoFarmCropsByVillageId(crops,selectedStatus,plottingTyp);
		}else if(!StringUtil.isEmpty(plottingTyp)&&plottingTyp.equalsIgnoreCase("3")){
		farmMapObjectList=farmerService.listFarmerFarmInfoFarmCropsByVillageId(farm,selectedStatus,plottingTyp);
		}
		
		farmMapObjectList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id", objArr[0]);
				jsonObject.put("latitude", objArr[1]);
				jsonObject.put("longtitude", objArr[2]);
				jsonObject.put("farmCode", objArr[3]);
				jsonObject.put("farmerName", objArr[4]);
				jsonObject.put("farmName", objArr[5]);
				jsonObject.put("farmId", objArr[6]);
				jsonObject.put("farmerId", objArr[7]);
				jsonObject.put("stateId", objArr[8]);
				jsonObject.put("districtId", objArr[9]);
				jsonObject.put("talukId", objArr[10]);
				jsonObject.put("villageId", objArr[11]);
				if(!StringUtil.isEmpty(plottingTyp)&&plottingTyp.equalsIgnoreCase("1")){
					jsonObject.put("cropId", "");
				}else{
				jsonObject.put("cropId", objArr[12]);
				}
				if(!StringUtil.isEmpty(plottingTyp)&&plottingTyp.equalsIgnoreCase("2")){
				FarmCrops farmCrops=farmerService.findByFarmCropsId(Long.valueOf( objArr[0].toString()));
				if (farmCrops.getActiveCoordinates()!=null && farmCrops.getActiveCoordinates().getFarmCropsCoordinates() != null
						&& !ObjectUtil.isListEmpty(farmCrops.getActiveCoordinates().getFarmCropsCoordinates())) {
					jsonObjectList = getFarmCropJSONObjects(farmCrops.getActiveCoordinates().getFarmCropsCoordinates());
				} else {
					jsonObjectList = new ArrayList();
				}
				jsonObject.put("fTyp", "2");
				}else if(!StringUtil.isEmpty(plottingTyp)&&plottingTyp.equalsIgnoreCase("1")){
				Farm fm=farmerService.findFarmByfarmId(Long.valueOf( objArr[0].toString()));
				if (fm.getActiveCoordinates()!=null && fm.getActiveCoordinates().getFarmCoordinates() != null
						&& !ObjectUtil.isListEmpty(fm.getActiveCoordinates().getFarmCoordinates())) {
					jsonObjectList = getFarmJSONObjects(fm.getActiveCoordinates().getFarmCoordinates());
				} else {
					jsonObjectList = new ArrayList();
				}
				jsonObject.put("fTyp", "1");
				}
				else if(!StringUtil.isEmpty(plottingTyp)&&plottingTyp.equalsIgnoreCase("3")){					
				if(!ObjectUtil.isEmpty(objArr[13])&& objArr[13].toString().equalsIgnoreCase("2")){
				FarmCrops farmCrops=farmerService.findByFarmCropsId(Long.valueOf( objArr[0].toString()));
				if (!ObjectUtil.isEmpty(farmCrops)&&farmCrops.getActiveCoordinates()!=null && farmCrops.getActiveCoordinates().getFarmCropsCoordinates() != null
						&& !ObjectUtil.isListEmpty(farmCrops.getActiveCoordinates().getFarmCropsCoordinates())) {
					jsonObjectList = getFarmCropJSONObjects(farmCrops.getActiveCoordinates().getFarmCropsCoordinates());
				} else {
					jsonObjectList = new ArrayList();
				}
				jsonObject.put("fTyp", "2");
				
				}else{
				Farm fm=farmerService.findFarmByfarmId(Long.valueOf( objArr[0].toString()));
				if (fm.getActiveCoordinates()!=null && fm.getActiveCoordinates().getFarmCoordinates() != null
						&& !ObjectUtil.isListEmpty(fm.getActiveCoordinates().getFarmCoordinates())) {
					jsonObjectList = getFarmJSONObjects(fm.getActiveCoordinates().getFarmCoordinates());
				} else {
					jsonObjectList = new ArrayList();
				}
				jsonObject.put("fTyp", "1");				
				}				
				}				
				jsonObject.put("jsonObjectList", jsonObjectList);
				jsonObjects.add(jsonObject);
		});

		printAjaxResponse(jsonObjects, "text/html");
	}
	
	@SuppressWarnings("unchecked")
	private List<JSONObject> getFarmJSONObjects(Set<Coordinates> coordinates) {

		List<JSONObject> returnObjects = new ArrayList<JSONObject>();
		List<Coordinates> listCoordinates = new ArrayList<Coordinates>(coordinates);
		 Collections.sort(listCoordinates);
		
		if (!ObjectUtil.isListEmpty(listCoordinates)) {
			for (Coordinates coordinateObj : listCoordinates) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("lat",
						!ObjectUtil.isEmpty(coordinateObj.getLatitude()) ? coordinateObj.getLatitude() : "");
				jsonObject.put("lon",
						!ObjectUtil.isEmpty(coordinateObj.getLongitude()) ? coordinateObj.getLongitude() : "");
				jsonObject.put("fType", "1");
				returnObjects.add(jsonObject);
			}
		}
		return returnObjects;		
	}

	@SuppressWarnings("unchecked")
	private List<JSONObject> getFarmCropJSONObjects(Set<FarmCropsCoordinates> coordinates) {
		List<JSONObject> returnObjects = new ArrayList<JSONObject>();
		List<FarmCropsCoordinates> listCoordinates = new ArrayList<FarmCropsCoordinates>(coordinates);
		 Collections.sort(listCoordinates);
		
		if (!ObjectUtil.isListEmpty(listCoordinates)) {
			for (FarmCropsCoordinates coordinateObj : listCoordinates) {
				JSONObject jsonObject = new JSONObject();

				// jsonObject.put("orderNo",!ObjectUtil.isEmpty(coordinateObj.getOrderNo())
				// ? coordinateObj.getOrderNo():"");
				jsonObject.put("lat",
						!ObjectUtil.isEmpty(coordinateObj.getLatitude()) ? coordinateObj.getLatitude() : "");
				jsonObject.put("lon",
						!ObjectUtil.isEmpty(coordinateObj.getLongitude()) ? coordinateObj.getLongitude() : "");
				jsonObject.put("fType", "2");
				returnObjects.add(jsonObject);
			}
		}
		return returnObjects;
	}
	
/*	public void populateFilterMap() {
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
	
		if (!ObjectUtil.isListEmpty(datas)) {
			datas.stream().forEach(obj -> {
				Object[] objArr = (Object[]) obj;
				if (!StringUtil.isEmpty(objArr[2]) && !StringUtil.isEmpty(objArr[3])) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("id", objArr[0]);

					jsonObject.put("latitude", objArr[1]);
					jsonObject.put("longtitude", objArr[2]);
					jsonObject.put("farmCode", objArr[3]);
					jsonObject.put("farmerName", objArr[4]);
					jsonObject.put("farmName", objArr[5]);
					jsonObjects.add(jsonObject);
				}
			});
			
		}
		printAjaxResponse(jsonObjects, "text/html");

	}*/
	Long cropId=0L;

	public void populateImg() {
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		cropId = farmerService.findCropIdByFarmCode(farmCode);
		if (!StringUtil.isEmpty(selectedCrop) && cropId != 0) {
			cropId = Long.valueOf(selectedCrop);
		}
		List<Object[]> farmer = null;
		if (!StringUtil.isEmpty(selectedStatus) && selectedStatus.equalsIgnoreCase("1")) {
			farmer = farmerService.listFarmerFarmCropInfoByVillageIdImg(selectedStatus, Long.valueOf(farmCropId),
					Long.valueOf(farmId));
		} else if (!StringUtil.isEmpty(selectedStatus) && selectedStatus.equalsIgnoreCase("2")) {
			Farm fr=farmerService.findFarmByFarmCode(farmCode);
			farmer = farmerService.listFarmerFarmCropInfoByVillageIdImg(selectedStatus, Long.valueOf(farmCropId),
					Long.valueOf(fr.getId()));
		}

		if (!ObjectUtil.isListEmpty(farmer)) {
			farmer.stream().forEach(obj -> {
				JSONObject jsonObject = null;
				jsonObject = new JSONObject();
				Object[] objArr = (Object[]) obj;
				jsonObject.put("id", objArr[0]);
				jsonObject.put("farmerId", ObjectUtil.isEmpty(objArr[1]) ? "-" : objArr[1].toString());
				jsonObject.put("farmerName", ObjectUtil.isEmpty(objArr[3]) ? "-" : objArr[3].toString());
				jsonObject.put("farmName", objArr[5]);
				jsonObject.put("latitude", objArr[7]);
				jsonObject.put("longtitude", objArr[8]);
				jsonObject.put("landmark", objArr[9]);
				jsonObject.put("village", objArr[12]);
				if (!ObjectUtil.isEmpty(objArr[10]) && objArr[10] != "") {
					jsonObject.put("totalLand", formatter.format(Double.valueOf(String.valueOf(objArr[10]))));
				}
				if (!ObjectUtil.isEmpty(objArr[11]) && objArr[11] != "") {
					jsonObject.put("proposedLand", formatter.format(Double.valueOf(String.valueOf(objArr[11]))));
				}

				if (!ObjectUtil.isEmpty(objArr[13]) && objArr[13] instanceof byte[]) {
					byte[] image = (byte[]) objArr[13];

					if (!StringUtil.isEmpty(image)) {
						jsonObject.put("image", "data:image/png;base64, " + Base64Util.encoder(image));
					} else {
						jsonObject.put("image", "img/no-image.png");
					}
				} else {
					jsonObject.put("image", "img/no-image.png");
				}

				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID) && cropId != 0) {
					jsonObject.put("proposedLand", formatter.format(Double.valueOf(String.valueOf(objArr[18]))));
				}

				if (cropId != 0 && objArr.length>17) {
					jsonObject.put("cropName", !ObjectUtil.isEmpty(objArr[17])?objArr[17]:"");
					if (!ObjectUtil.isEmpty(objArr[19])) {
						String replaceDot = objArr[19].toString().replace(".0", "");
						jsonObject.put("estHavstDate", String.valueOf(
								DateUtil.convertDateFormat(replaceDot, DateUtil.TXN_DATE_TIME, DateUtil.DATE_FORMAT)));
					}
					if (!ObjectUtil.isEmpty(objArr[20])) {
						String replaceDot = objArr[20].toString().replace(".0", "");
						jsonObject.put("sowingDate", String.valueOf(
								DateUtil.convertDateFormat(replaceDot, DateUtil.TXN_DATE_TIME, DateUtil.DATE_FORMAT)));
					}
				}

				if (!ObjectUtil.isEmpty(objArr[14]) && String.valueOf(objArr[14]).length() > 0) {
					HarvestSeason harvestSeason = farmerService.findHarvestSeasonByCode(String.valueOf(objArr[14]));
					jsonObject.put("cropSeason", harvestSeason.getName());
				}

				//
				jsonObject.put("branch",(!ObjectUtil.isEmpty(objArr[15]))?objArr[15]:"");

				if (!ObjectUtil.isEmpty(objArr[16])) {
					String replaceDot = objArr[16].toString().replace(".0", "");
					jsonObject.put("doj", String.valueOf(
							DateUtil.convertDateFormat(replaceDot, DateUtil.TXN_DATE_TIME, DateUtil.DATE_FORMAT)));
				}
				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)) {
					FarmIcsConversion farmIcsConversion = farmerService
							.findFarmIcsConversionByFarmId(Long.valueOf(farmId));

					if (!ObjectUtil.isEmpty(farmIcsConversion)) {
						jsonObject.put("inspDate",
								String.valueOf(DateUtil.convertDateFormat(
										String.valueOf(farmIcsConversion.getInspectionDate()), DateUtil.TXN_DATE_TIME,
										DateUtil.DATE_FORMAT)));
						jsonObject.put("inspectedBy", String.valueOf(farmIcsConversion.getInspectorName()));
						FarmIcsConversion fIcs = farmerService
								.findFarmIcsConversionByFarmIdWithActive(Long.valueOf(farmId));
						if (!ObjectUtil.isEmpty(fIcs) && fIcs != null) {
							jsonObject.put("icsType", fIcs.getOrganicStatus().equalsIgnoreCase("3")
									? getLocaleProperty("alrdyCertified") : getLocaleProperty("inprocess"));
						} else {
							jsonObject.put("icsType", "Conventional");
						}

					} else {
						jsonObject.put("icsType", "Conventional");
					}
				} else {
					Object[] periodObj = farmerService.findPeriodicInsDateByFarmCode(farmCode);
					if (!ObjectUtil.isEmpty(periodObj)) {
						jsonObject.put("inspDate",
								String.valueOf(DateUtil.convertDateFormat(String.valueOf(periodObj[0]),
										DateUtil.TXN_DATE_TIME, DateUtil.DATE_FORMAT)));
						jsonObject.put("inspectedBy", String.valueOf(periodObj[1]));
					}
				}
				jsonObjects.add(jsonObject);
			});
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

	public List<Municipality> getTalukList() {

		branchIdValue = getBranchId();
		if (StringUtil.isEmpty(branchIdValue)) {
			talukList = locationService.listMunicipality();
		} else {
			talukList = locationService.listMunicipalityBasedOnBranch(branchIdValue);
		}
		return talukList;
	}

	public void setTalukList(List<Municipality> talukList) {

		this.talukList = talukList;
	}

	public List<Village> getVillageList() {

		// if(!StringUtil.isEmpty(selectedTaluk)){
		villageList = locationService.listVillage();
		// }
		return villageList;
	}
	public void populateVillageByCity() throws Exception {

		List<Village> villages = new ArrayList<Village>();
		if (!selectedTaluk.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedTaluk))
				&& !selectedTaluk.equalsIgnoreCase("0")) {
			villages = locationService.listVillagesByCityId(Long.valueOf(selectedTaluk));
		}
		JSONArray villageArr = new JSONArray();
		if (!ObjectUtil.isEmpty(villages)) {
			for (Village village : villages) {
				villageArr.add(getJSONObject(village.getId(), village.getName()));
			}
		}
		sendAjaxResponse(villageArr);

	}
     
	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}

	public String getSelectedTaluk() {

		return selectedTaluk;
	}

	public void setSelectedTaluk(String selectedTaluk) {

		this.selectedTaluk = selectedTaluk;
	}

	public String getSelectedVillage() {

		return selectedVillage;
	}

	public void setSelectedVillage(String selectedVillage) {

		this.selectedVillage = selectedVillage;
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

	public String getSelectedCrop() {
		return selectedCrop;
	}

	public void setSelectedCrop(String selectedCrop) {
		this.selectedCrop = selectedCrop;
	}

	public void populateCropList() {
		JSONArray cropArr = new JSONArray();
		String branchId = getBranchId();
		List<Object[]> cropList = productService.listOfFarmCrops(branchId);
/*		List<Object[]> cropList = productService.listOfFarmCrops();
*/		if (!ObjectUtil.isEmpty(cropList)) {
			cropList.forEach(obj -> {
				cropArr.add(getJSONObject(String.valueOf(obj[0]), String.valueOf(obj[1].toString())));
			});
		}
		sendAjaxResponse(cropArr);
	}

	public Map<String, String> getSeasonList() {

		Map<String, String> seasonMap = new LinkedHashMap<String, String>();
		List<Object[]> seasonList = farmerService.listSeasonCodeAndName();
		for (Object[] obj : seasonList) {

			seasonMap.put(String.valueOf(obj[0]), String.valueOf(obj[1]));
		}
		return seasonMap;

	}

	@SuppressWarnings("unchecked")
	public void populateLoadFields() throws IOException {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();	
		Integer farmerCount = farmerService.findFarmersCountSowingLoca(selectedCrop,selectedState,selectedLocality, selectedTaluk, selectedVillage,
				selectedFarmer,selectedStatus);
		Object[] obj = farmerService.findTotalAcreAndEstimatedYieldSwoingLoca(selectedCrop,selectedState,selectedLocality, selectedTaluk, selectedVillage,
				selectedFarmer,selectedStatus,selectedFarm);
		double yield = 0.0;
		if (!ObjectUtil.isEmpty(obj))
		{
				if(!ObjectUtil.isEmpty(obj[2]))
				{
					yield=Double.valueOf(String.valueOf(obj[2]));
				}
			
		}
		jsonObject.put("farmerCount", !StringUtil.isEmpty(farmerCount) ? String.valueOf(farmerCount) : "0");
		jsonObject.put("totalAcres",
				!StringUtil.isEmpty(obj[0]) ? formatter.format(Double.valueOf(String.valueOf(obj[0]))) : "0");
		jsonObject.put("yield", !StringUtil.isEmpty(yield) ? formatter.format(yield) : "0");
		jsonArray.add(jsonObject);

		response.setContentType("text/JSON");
		PrintWriter out = response.getWriter();
		if (jsonArray.size() > 0)
			out.println(jsonArray.toString());
		}
	
	public Map<String, String> getStats() {
		Map<String, String> states = new HashMap<>();
		states.put("tn", "Tamil Nadu");
		states.put("kl", "Kerala");
		states.put("py", "Pondicherry");
		states.put("ap", "Andhra Pradesh");
		states.put("ka", "Karnataka");
		states.put("ga", "Goa");
		states.put("mh", "Maharashtra");
		states.put("or", "Orissa");
		states.put("ct", "Chhattisgarh");
		states.put("mp", "Madhya Pradesh");
		states.put("rj", "Rajasthan");
		states.put("up", "Uttar Pradesh");
		states.put("jh", "Jharkhand");
		states.put("wb", "West Bengal");
		states.put("br", "Bihar");
		states.put("hr", "Haryana");
		states.put("pb", "Punjab");
		states.put("hp", "Himachal Pradesh");
		states.put("jk", "Jammu and Kashmir");
		states.put("ut", "Uttaranchal");
		states.put("sk", "Sikkim");
		states.put("ar", "Arunachal Pradesh");
		states.put("ml", "Meghalaya");
		states.put("as", "Assam");
		states.put("tr", "Tripura");
		states.put("mz", "Mizoram");
		states.put("mn", "Manipur");
		states.put("nl", "Nagaland");

		return states;

	}
	
	@SuppressWarnings("unchecked")
	public void populateFarmerDetailsByStateChart() {
		JSONObject jsonObject = new JSONObject();
		Object[] farmerDatas = farmerService.findFarmerCountByStateName(getStats().get(selectedState));
		if (Integer.valueOf(String.valueOf(farmerDatas[0]))!=0)
		{
			jsonObject.put("Label", getLocaleProperty("farmerProdDetailsChart"));

			jsonObject.put("farmerCount",
					!ObjectUtil.isEmpty(farmerDatas[0]) ? Double.valueOf(String.valueOf(farmerDatas[0])) : 0);
			jsonObject.put("farmCount",
					!ObjectUtil.isEmpty(farmerDatas[1]) ? Double.valueOf(String.valueOf(farmerDatas[1])) : 0);
			jsonObject.put("totalAcres", !StringUtil.isEmpty(farmerDatas[2])
					? Double.valueOf(formatter.format(Double.valueOf(String.valueOf(farmerDatas[2])))) : 0);
		}
		printAjaxResponse(jsonObject, "text/html");

	}

	
	

	public String getFarmCode() {
		return farmCode;
	}

	public void setFarmCode(String farmCode) {
		this.farmCode = farmCode;
	}
	
	public void populateHideFields() throws ScriptException {
	
	List<FarmerLocationMapField> farmerLocFieldList = farmerService.listRemoveFarmerLocMapFields();
	List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
	
	farmerLocFieldList.stream().forEach(farmerLocMapField -> {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("type", farmerLocMapField.getType());
		jsonObject.put("typeName", farmerLocMapField.getTypeName());
		jsonObjects.add(jsonObject);
	});
	
	printAjaxResponse(jsonObjects, "text/html");

	}

	public String getFarmId() {
		return farmId;
	}

	public void setFarmId(String farmId) {
		this.farmId = farmId;
	}
	public Map<String, String> getStatesList() {
		Map<String,String> statemap = new HashMap<>();
		List<Object[]> statelist = locationService.listStates();
		for (Object[] obj : statelist) {
			statemap.put(String.valueOf(obj[2]), String.valueOf(obj[0])+"-"+String.valueOf(obj[1]));
		}
		return statemap;
	}



	public Map<String, String> getListLocalities() {
		Map<String,String> localitymap = new HashMap<>();
		List<Object[]> localitylist = locationService.listLocalities();
		for (Object[] obj: localitylist) {
			localitymap.put(String.valueOf(obj[2]),String.valueOf(obj[0])+ "-"+String.valueOf(obj[1]));
		}
		
		return localitymap;
	}

	public void setListLocalities(Map<String, String> listLocalities) {
		this.listLocalities = listLocalities;
	}

	public List<Municipality> getListCity() {
		return listCity;
	}
	public List<Village> getListVillage() {
		return listVillage;
	}

	public String getSelectedState() {
		return selectedState;
	}

	public void setSelectedState(String selectedState) {
		this.selectedState = selectedState;
	}

	public String getSelectedLocality() {
		return selectedLocality;
	}

	public void setSelectedLocality(String selectedLocality) {
		this.selectedLocality = selectedLocality;
	}

	public void setListCity(List<Municipality> listCity) {
		this.listCity = listCity;
	}

	public void setListVillage(List<Village> listVillage) {
		this.listVillage = listVillage;
	}

	public void setStatesList(Map<String, String> statesList) {
		this.statesList = statesList;
	}
	
	public Map<String, String> getOrganicStatusList() {
		
		if(getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)){
			organicStatus.put("3", getLocaleProperty("alrdyCertified"));
			organicStatus.put("0", getLocaleProperty("inprocess"));
		}else{
			organicStatus.put("3", getLocaleProperty("icsStatus3"));
			organicStatus.put("0", getLocaleProperty("inconversion"));
		}
		organicStatus.put("Conventional", getLocaleProperty("Conventional"));
		return organicStatus;
	}

	public String getSelectedOrganicStatus() {
		return selectedOrganicStatus;
	}

	public void setSelectedOrganicStatus(String selectedOrganicStatus) {
		this.selectedOrganicStatus = selectedOrganicStatus;
	}

	public String getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(String longtitude) {
		this.longtitude = longtitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public Map<String, String> getFarmerList() {
		Map<String, String> farmerListMap = new LinkedHashMap<String, String>();
		List<Object[]> farmersList = farmerService.listOfFarmersByFarmCrops();
		farmerListMap = farmersList.stream()
				.collect(Collectors.toMap(obj -> String.valueOf(obj[0]), obj -> String.valueOf(obj[1]) +" - "+String.valueOf(obj[2])));

		Map<String, String> orderedFarmerMap = farmerListMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

		return orderedFarmerMap;
	}
	
	public void populateStatusList() {
		JSONArray statusArr = new JSONArray();
		Map<Integer, String> statusList = new LinkedHashMap<Integer, String>();
		statusList.put(Farmer.Status.ACTIVE.ordinal(), getText("status" + Farmer.Status.ACTIVE.ordinal()));
		statusList.put(Farmer.Status.INACTIVE.ordinal(), getText("status" + Farmer.Status.INACTIVE.ordinal()));
		statusList.put(2, getText("status" + 2));
		
		statusList.forEach((k,v)->{
			statusArr.add(getJSONObject(String.valueOf( k), v));
		});
	
		sendAjaxResponse(statusArr);
	}
	
	public void populateFarmer() throws Exception {
		if (!selectedVillage.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedVillage))) {
			
			List<Object[]> listFarmer = null;						
			String plottingTyp=preferencesService.findPrefernceByName(ESESystem.PLOTTING_TYPE_MAP);		
			if(!StringUtil.isEmpty(plottingTyp)){
				listFarmer = farmerService.listOfFarmersByVillageAndFarmCrops(Long.valueOf(selectedVillage),plottingTyp);
			}
			JSONArray farmerArr = new JSONArray();
			listFarmer.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
				farmerArr.add(getJSONObject(obj[4], obj[2] + "" + (!StringUtil.isEmpty(obj[3]) ? (" " + obj[3]) : " ")
						+ (!StringUtil.isEmpty(obj[1]) ? ("-" + obj[1]) : " ")));
			});
			sendAjaxResponse(farmerArr);
		}
	}
	
	public void populateFarm() throws Exception {
		if (!selectedFarmer.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedFarmer))) {
			List<Object[]> listFarm = farmerService.listOfFarmsByFarmer(Long.valueOf(selectedFarmer));
			JSONArray farmArr = new JSONArray();
			listFarm.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
				farmArr.add(getJSONObject(obj[3], obj[5]));
			});
			sendAjaxResponse(farmArr);
		}
	}

	public String getSelectedFarmer() {
		return selectedFarmer;
	}

	public void setSelectedFarmer(String selectedFarmer) {
		this.selectedFarmer = selectedFarmer;
	}

	public String getFarmCropId() {
		return farmCropId;
	}

	public void setFarmCropId(String farmCropId) {
		this.farmCropId = farmCropId;
	}
	
	
	public List<JSONObject> getJsonObjectList() {
		return jsonObjectList;
	}

	public void setJsonObjectList(List<JSONObject> jsonObjectList) {
		this.jsonObjectList = jsonObjectList;
	}

	public String getSelectedStatus() {
		return selectedStatus;
	}

	public void setSelectedStatus(String selectedStatus) {
		this.selectedStatus = selectedStatus;
	}

	public String getPlottingType() {
		return plottingType;
	}

	public void setPlottingType(String plottingType) {
		this.plottingType = plottingType;
	}

	public String getfType() {
		return fType;
	}

	public void setfType(String fType) {
		this.fType = fType;
	}

	public String getSelectedFarm() {
		return selectedFarm;
	}

	public void setSelectedFarm(String selectedFarm) {
		this.selectedFarm = selectedFarm;
	}
	
}
