package com.sourcetrace.esesw.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.script.ScriptException;

import org.apache.velocity.runtime.log.ServletLogChute;
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
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.FarmIcsConversion;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicData;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.Locality;
import com.sourcetrace.esesw.entity.profile.LocationDetail;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.State;
import com.sourcetrace.esesw.entity.profile.Village;

@SuppressWarnings("serial")
public class FarmerLocationAction extends ESEAction {

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
	private String selectedStatus;
	@Autowired
	private IPreferencesService preferencesService;
	private List<Municipality> talukList;
	private List<Village> villageList;
	private Map<String, String> statesList = new LinkedHashMap<String, String>();
	private Map<String, String> listLocalities =new LinkedHashMap<String,String>();
	List<Village> listVillage = new ArrayList<Village>();
	List<Municipality> listCity = new ArrayList<Municipality>();
	Map<String, String> organicStatus = new LinkedHashMap<>();
	Map<String, String> selectedStatusList = new LinkedHashMap<>();
	private String longtitude;
	private String latitude;
	private String yieldEstimation;
	DecimalFormat formatter = new DecimalFormat("0.00");
	public String list() throws Exception {
		ESESystem preferences = preferencesService.findPrefernceById("1");
		setLatitude(preferencesService.findPrefernceByName(ESESystem.DEFAULT_LATITUDE));  
		setLongtitude(preferencesService.findPrefernceByName(ESESystem.DEFAULT_LONGTITUDE));
		return LIST;
	}

	@SuppressWarnings("unchecked")
	public void populateFarmsMap() throws Exception {

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<Object[]> farmMapObjectList = new ArrayList<Object[]>();
		
		FarmCrops crops = new FarmCrops();
		Farm farm = new Farm();
		Farmer farmer = new Farmer();
		FarmIcsConversion farmics = new FarmIcsConversion();
		Set<FarmIcsConversion> ics = new HashSet<FarmIcsConversion>();
		if (!StringUtil.isEmpty(selectedCrop)) {
			farm.setCropCode(selectedCrop);
		}

		if (!StringUtil.isEmpty(selectedTaluk)) {
			   farmer.setCity(new Municipality());
			   farmer.getCity().setId(Long.valueOf(selectedTaluk));
			   farm.setFarmer(farmer);
			  }

		if (!StringUtil.isEmpty(selectedSeason)) {
		//	HarvestSeason season = new HarvestSeason();
			farmer.setSeasonCode(selectedSeason);
			farm.setFarmer(farmer);
		}

		if (!StringUtil.isEmpty(selectedVillage)) {

			farmer.setVillage(new Village());
			farmer.getVillage().setId(Long.valueOf(selectedVillage));
			farm.setFarmer(farmer);
			//crops.setFarm(farm);
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
		
		farmer.setIsCertifiedFarmer(-1);
		if (!StringUtil.isEmpty(selectedOrganicStatus)) {
			
			if(!selectedOrganicStatus.equalsIgnoreCase("conventional")){
				farmics.setOrganicStatus(selectedOrganicStatus);
				farm.setFarmIcsConv(farmics);
				ics.add(farm.getFarmIcsConv());
				farmer.setIsCertifiedFarmer(1);
				
			}else{
				farmer.setIsCertifiedFarmer(0);
			}
		}
		
		farmer.setBranchId(getBranchId());
	//	farm.setFarmer(farmer);
		
		if (ics.size() > 0){
			farm.setFarmICSConversion(ics);
		}
		//farmMapObjectList=farmerService.listFarmerFarmInfoByVillageId(farm,selectedOrganicStatus,selectedFarmer);
		
		
		List<Long> yieldEstimationDoneFarmers = new ArrayList<>();
		
		if(getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID) && !StringUtil.isEmpty(yieldEstimation) && yieldEstimation.equalsIgnoreCase("yes")){
			List<FarmerDynamicData>  yieldEstimationData = 	farmerService.listFarmerDynamicDataByTxnId("4");
			 yieldEstimationDoneFarmers = yieldEstimationData.stream().filter(f -> f.getTxnType().equalsIgnoreCase("4")).map(m -> Long.valueOf(m.getReferenceId())).collect(Collectors.toList());
		}
		
		farmMapObjectList=farmerService.listFarmerFarmInfoByVillageId(farm,selectedOrganicStatus,selectedFarmer,yieldEstimationDoneFarmers,selectedStatus);
		
	

		farmMapObjectList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			if (!StringUtil.isEmpty(objArr[2]) && !StringUtil.isEmpty(objArr[3])) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id", objArr[0]);

				jsonObject.put("latitude", objArr[1]);
				jsonObject.put("longtitude", objArr[2]);
				jsonObject.put("farmCode", objArr[3]);
				jsonObject.put("farmerName", objArr[4]);
				jsonObject.put("farmName", objArr[5]);
				jsonObject.put("farmId", objArr[6]);
				jsonObject.put("certified", objArr[7]);
				jsonObject.put("organicStatus", objArr[8]);
				jsonObjects.add(jsonObject);
			}
		});

		printAjaxResponse(jsonObjects, "text/html");
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
		 cropId=farmerService.findCropIdByFarmCode(farmCode);
		if(!StringUtil.isEmpty(selectedCrop) && cropId!=0)
		{
			cropId=Long.valueOf(selectedCrop);
		}
		if (!ObjectUtil.isEmpty(farmerId)) {
			List<Object[]> farmer = farmerService.listFarmerFarmInfoByVillageIdImg(cropId,
					Long.valueOf(farmerId),Long.valueOf(farmId));
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
					if(!StringUtil.isEmpty(objArr[10])&& objArr[10]!="")
					{
						jsonObject.put("totalLand", formatter.format(Double.valueOf(String.valueOf(objArr[10]))));
					}
					if(!StringUtil.isEmpty(objArr[11]) && objArr[11]!="")
					{
						jsonObject.put("proposedLand", formatter.format(Double.valueOf(String.valueOf(objArr[11]))));
					}
					

					if (!StringUtil.isEmpty(objArr[13]) && objArr[13] instanceof byte[]) {
						byte[] image = (byte[]) objArr[13];

						if (!StringUtil.isEmpty(image)) {
							jsonObject.put("image", "data:image/png;base64, " + Base64Util.encoder(image));
						} else {
							jsonObject.put("image", "img/no-image.png");
						}
					} else {
						jsonObject.put("image", "img/no-image.png");
					}
					
					if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID) && cropId!=0) {
						jsonObject.put("proposedLand", formatter.format(Double.valueOf(String.valueOf(objArr[18]))));
					}
					
					if(cropId!=0)
					{
						jsonObject.put("cropName", objArr[17]);
						if (!ObjectUtil.isEmpty(objArr[19])) {
							String replaceDot = objArr[19].toString().replace(".0", "");
							jsonObject.put("estHavstDate", String.valueOf(
									DateUtil.convertDateFormat(replaceDot, DateUtil.TXN_DATE_TIME, DateUtil.DATE_FORMAT)));
						}
						jsonObject.put("estYield", formatter.format(Double.valueOf(String.valueOf(objArr[20]))));
					}
					
					if(!ObjectUtil.isEmpty(objArr[14]) && String.valueOf(objArr[14]).length()>0)
					{
						HarvestSeason harvestSeason=farmerService.findHarvestSeasonByCode(String.valueOf(objArr[14]));
						jsonObject.put("cropSeason", harvestSeason.getName());
					}
				
				//	
					jsonObject.put("branch", objArr[15]);

					if (!ObjectUtil.isEmpty(objArr[16])) {
						String replaceDot = objArr[16].toString().replace(".0", "");
						jsonObject.put("doj", String.valueOf(
								DateUtil.convertDateFormat(replaceDot, DateUtil.TXN_DATE_TIME, DateUtil.DATE_FORMAT)));
					}
					if(getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)){
						FarmIcsConversion farmIcsConversion = farmerService.findFarmIcsConversionByFarmId(Long.valueOf(farmId));
						
						if (!ObjectUtil.isEmpty(farmIcsConversion)) {
							jsonObject.put("inspDate",
									String.valueOf(DateUtil.convertDateFormat(String.valueOf(farmIcsConversion.getInspectionDate()),
											DateUtil.TXN_DATE_TIME, DateUtil.DATE_FORMAT)));
							jsonObject.put("inspectedBy", String.valueOf(farmIcsConversion.getInspectorName()));
								FarmIcsConversion fIcs = farmerService.findFarmIcsConversionByFarmIdWithActive(Long.valueOf(farmId));
								if(!ObjectUtil.isEmpty(fIcs)&&fIcs!=null){
									jsonObject.put("icsType",fIcs.getOrganicStatus().equalsIgnoreCase("3") ? getLocaleProperty("alrdyCertified") : getLocaleProperty("inprocess"));
								}else{
									jsonObject.put("icsType","Conventional");
								}
							
							//jsonObject.put("icsType", String.valueOf(farmIcsConversion.getIcsType()));
						}
						else{
							jsonObject.put("icsType","Conventional");
						}
					}
					else{
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
		//String branchId = getBranchId();
		//List<Object[]> cropList = productService.listOfFarmCrops(branchId);
		List<Object[]> cropList = productService.listOfFarmCrops();
		if (!ObjectUtil.isEmpty(cropList)) {
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
	
		Integer farmerCount = farmerService.findFarmersCountFarmerLoca(selectedCrop,selectedState,selectedLocality, selectedTaluk, selectedVillage,selectedFarmer,
				selectedSeason,selectedOrganicStatus,selectedStatus);
		Object[] obj = farmerService.findTotalAcreAndEstimatedYield(selectedCrop,selectedState,selectedLocality, selectedTaluk, selectedVillage,selectedFarmer,
				selectedSeason,selectedOrganicStatus,selectedStatus);
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
	
	public void populateFarmer() throws Exception {
		if (!selectedVillage.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedVillage))) {
			List<Object[]> listFarmer = farmerService.listOfFarmersByVillageAndFarm(Long.valueOf(selectedVillage));

			JSONArray farmerArr = new JSONArray();
			listFarmer.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
				farmerArr.add(getJSONObject(obj[4], obj[2] + "" + (!StringUtil.isEmpty(obj[3]) ? (" " + obj[3]) : " ")
						+ (!StringUtil.isEmpty(obj[1]) ? ("-" + obj[1]) : " ")));
			});
			sendAjaxResponse(farmerArr);
		}
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

	public String getSelectedFarmer() {
		return selectedFarmer;
	}

	public void setSelectedFarmer(String selectedFarmer) {
		this.selectedFarmer = selectedFarmer;
	}

	public String getYieldEstimation() {
		return yieldEstimation;
	}

	public void setYieldEstimation(String yieldEstimation) {
		this.yieldEstimation = yieldEstimation;
	}
	public Map<String, String> getYieldEstimationList() {
        Map<String, String> statemap = new HashMap<String, String>();
        statemap.put("Yes", "Yes");
        statemap.put("No", "No");
        return statemap;
    }

	public String getSelectedStatus() {
		return selectedStatus;
	}

	public void setSelectedStatus(String selectedStatus) {
		this.selectedStatus = selectedStatus;
	}

	public Map<String, String> getSelectedStatusList() {
		return selectedStatusList;
	}

	public void setSelectedStatusList(Map<String, String> selectedStatusList) {
		this.selectedStatusList = selectedStatusList;
	}


}
