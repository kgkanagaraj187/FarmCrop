package com.sourcetrace.esesw.view;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.view.ESEAction;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.service.IFarmCropsService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.Farmer;

@SuppressWarnings("serial")
public class GeoPlottingAction extends ESEAction {

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
	  private static final String POST_LOCALHOST = "https://kpuaj417rk.execute-api.eu-west-2.amazonaws.com/betajune/jobs";
	  private static final String GET_LOCALHOST = "https://1abdn4twk0.execute-api.eu-west-2.amazonaws.com/var/task/lambda_function.py";
	    private static final String format = "application/json";
	 
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
		if (!StringUtil.isEmpty(selectedFarm)) {
			Long farm = Long.valueOf(selectedFarm.split("_")[0].toString());
			Farm fm  = farmerService.findFarmById(farm);
			String s = fm.getActiveCoordinates().getFarmCoordinates().stream().map(x -> x.getLongitude()+","+x.getLatitude())
			.collect(Collectors.joining("],[", "[", "]"));
	        String req = "{\"UserId\": \""+selectedFarm+"\",\"polygonID\": \""+selectedFarm+"\", \"satellitetype\": \"sentinel-s2-l1c\", \"geojson\": {\"type\": \"Feature\",\"properties\": {\"AGOSLAG\": 1},\"year\": 2018,\"month\": 5,\"geometry\": {\"type\": \"Polygon\",\"coordinates\": [["+s+"]]}}}";
	        String  responseCode =  getJSON(POST_LOCALHOST,"POST",req);
		     printAjaxResponse(responseCode, "text/html");
		}
	}
	
	public void populateImage() throws Exception {
		if (!StringUtil.isEmpty(selectedFarm)) {
			     String req = "https://1abdn4twk0.execute-api.eu-west-2.amazonaws.com/betajune/results?userId="+selectedFarm+"&polygonid="+selectedFarm+"&yearmonth=y2018m7";
			   String  responseCode =  getJSON(req,"GET",null);
			     printAjaxResponse(responseCode, "text/html");
		}
	}
	
	public String getJSON(String url, String method,String postData) {
	    HttpURLConnection c = null;
	    try {
	        URL u = new URL(url);
	        c = (HttpURLConnection) u.openConnection();
	        c.setRequestMethod(method);
	        c.setRequestProperty("Content-length", "0");
	        c.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
	        c.setRequestProperty ("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VyTmFtZSI6ImRvZXJhbmRkb250ZXJAbWFpbC5jb20iLCJwYXNzd29yZCI6InRoaXNpc215bG9naW5wYXNzd29yZCJ9.2daRX0DQukFyeJZW7jraowwRWwNv4zHa5LjocwBWimg");
	        c.setDoOutput(true);
	        c.setRequestProperty("Content-Type", format);
	        if(postData!=null){
	        DataOutputStream wr = new DataOutputStream(c.getOutputStream());
	        wr.writeBytes(postData);
		        wr.flush();
		        wr.close();
	        }

	       // c.setAllowUserInteraction(false);
	          c.connect();
	        int status = c.getResponseCode();

	        BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
            br.close();
            return sb.toString();

	    } catch (MalformedURLException ex) {
	        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
	    } catch (IOException ex) {
	        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
	    } finally {
	       if (c != null) {
	          try {
	              c.disconnect();
	          } catch (Exception ex) {
	             Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
	          }
	       }
	    }
	    return null;
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
		List<Object[]> farmersList = farmerService.listFarmerHavingPlots();
		farmersListMap = farmersList.stream().collect(Collectors.toMap(u -> u[0].toString(), u -> u[1].toString()+(u[2]==null ? " " : " "+u[2].toString())));
		
		return farmersListMap;
	}

	public void populateFarm() throws Exception {
		if (!selectedFarmer.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedFarmer))) {
			List<Object[]> listFarm = farmerService.listFarmByFarmerIds(Long.valueOf(selectedFarmer));
			JSONArray farmArr = new JSONArray();
			listFarm.stream().forEach(u ->{
				farmArr.add(getJSONObject(u[0].toString()+"_"+u[2].toString(),u[1].toString()));
			});
		
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
		if (!selectedFarm.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedFarm)) && !selectedFarmType.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedFarmType))) {
			if(selectedFarmType.equals("1")){
		Farm f = farmerService.findFarmById(Long.valueOf(selectedFarm));	
		
		if (!ObjectUtil.isListEmpty(f.getActiveCoordinates().getFarmCoordinates())) {
			f.getActiveCoordinates().getFarmCoordinates().stream().forEach(coordinateObj ->{
				JSONObject jsonObject = new JSONObject();
				// jsonObject.put("orderNo",!ObjectUtil.isEmpty(coordinateObj.getOrderNo())
				// ? coordinateObj.getOrderNo():"");
				jsonObject.put("lat",
						!ObjectUtil.isEmpty(coordinateObj.getLatitude()) ? coordinateObj.getLatitude() : "");
				jsonObject.put("lon",
						!ObjectUtil.isEmpty(coordinateObj.getLongitude()) ? coordinateObj.getLongitude() : "");
				
				panchayathArr.add(jsonObject);
			});
		
		}
		js.put("area",f.getFarmDetailedInfo().getProposedPlantingArea());
		js.put("coord",panchayathArr);
			}else{
				FarmCrops f = farmerService.findByFarmCropsId(Long.valueOf(selectedFarm));
				
				if (!ObjectUtil.isListEmpty(f.getActiveCoordinates().getFarmCropsCoordinates())) {
					f.getActiveCoordinates().getFarmCropsCoordinates().stream().forEach(coordinateObj ->{
						JSONObject jsonObject = new JSONObject();
						// jsonObject.put("orderNo",!ObjectUtil.isEmpty(coordinateObj.getOrderNo())
						// ? coordinateObj.getOrderNo():"");
						jsonObject.put("lat",
								!ObjectUtil.isEmpty(coordinateObj.getLatitude()) ? coordinateObj.getLatitude() : "");
						jsonObject.put("lon",
								!ObjectUtil.isEmpty(coordinateObj.getLongitude()) ? coordinateObj.getLongitude() : "");
						
						panchayathArr.add(jsonObject);
					});
				
				}
				js.put("area",f.getCultiArea());
				js.put("coord",panchayathArr);
			}
		}
		
		sendAjaxResponse(js);
	}

}
