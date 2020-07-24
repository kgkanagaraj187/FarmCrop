package com.ese.view.profile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmDetailedInfo;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerLandDetails;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

public class FarmHistoryAction extends SwitchValidatorAction{
	
	private String id;
	private String farmerId;
	private Farm farm;
	private String farmId;
	public static final String FARMER_DETAIL = "farmerDetail";
	public static final String FARM_DETAIL = "farmDetail";
	@Autowired
	 private IFarmerService farmerService;
	//private List<FarmerLandDetails> farmerLandDetailsList = new ArrayList<FarmerLandDetails>();
	private String date;
	private FarmerLandDetails farmerLandDetails;
	/*public String create() throws Exception {
		
		Set<FarmerLandDetails> farmerLandDetails = new LinkedHashSet<FarmerLandDetails>();
		if (!ObjectUtil.isListEmpty(farmerLandDetailsList)) {
			for (FarmerLandDetails farmerLandDetail : farmerLandDetailsList) {
				if (!ObjectUtil.isEmpty(farmerLandDetail)) {
					FarmerLandDetails LandDetails = new FarmerLandDetails();
					LandDetails.setYear(farmerLandDetail.getYear());
					LandDetails.setCrops(farmerLandDetail.getCrops());
					LandDetails.setEstimatedAcreage(!StringUtil.isEmpty(farmerLandDetail.getEstimatedAcreage())
							? (farmerLandDetail.getEstimatedAcreage()) : 0.00);
					LandDetails.setNoOfTrees(farmerLandDetail.getNoOfTrees());
					LandDetails.setPestdiseases(farmerLandDetail.getPestdiseases());
					LandDetails.setPestdiseasesControl(farmerLandDetail.getPestdiseasesControl());
					LandDetails.setFertilizationMethod(farmerLandDetail.getFertilizationMethod());
					LandDetails.setInputs(farmerLandDetail.getInputs());
					LandDetails.setWithBuffer(farmerLandDetail.getWithBuffer());
					LandDetails.setWithoutBuffer(farmerLandDetail.getWithoutBuffer());
					SimpleDateFormat sf=new SimpleDateFormat("MM/dd/yyyy");
					date=date!=null && !StringUtil.isEmpty(date)?date:sf.format(new Date());
					LandDetails.setDate(DateUtil.convertStringToDate(date, "MM/dd/yyyy"));
					farmerLandDetails.add(LandDetails);
				}
			}
			farm.setFarmerLandDetails(farmerLandDetails);
		}
		return "FARMER_DETAIL";
}
	
	*/
	
	public String data() throws Exception {

		Map<String, String> searchRecord = getJQGridRequestParam(); 

		
		FarmerLandDetails filter = new FarmerLandDetails();
		
		Farmer farmer = new Farmer();
		farmer.setId(Long.valueOf(this.id));
		Farm farm = new Farm();
		farm.setFarmer(farmer);
		filter.setFarmId(farm);
		
		if (!StringUtil.isEmpty(searchRecord.get("year")))
			filter.setYear(searchRecord.get("year").trim());
		
		if (!StringUtil.isEmpty(searchRecord.get("crops")))
			filter.setCrops(searchRecord.get("crops").trim());

		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());
		
		return sendJQGridJSONResponse(data);
	}
	
	public JSONObject toJSON(Object obj) {

		FarmerLandDetails fld = (FarmerLandDetails) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		
		rows.add(String.valueOf(fld.getYear()));
		if (!StringUtil.isEmpty(fld.getCrops())) {
			ProcurementProduct crops = farmerService.findCropByCropCode(fld.getCrops().trim());
			rows.add(!StringUtil.isEmpty(crops) ? crops.getName() : "");
		} else {
			rows.add("N/A");
		}
		rows.add(fld.getSeedlings());
		rows.add(String.valueOf(fld.getEstimatedAcreage()));
		rows.add(fld.getNoOfTrees());
		rows.add(fld.getPestdiseases());
		rows.add(fld.getPestdiseasesControl());
		
		jsonObject.put("id", fld.getId());
		jsonObject.put("cell", rows);
		return jsonObject;
	}

	public String detail() throws Exception {
		 String view = "";
		request.setAttribute(HEADING, getText(LIST));
		if (id != null && !id.equals("") && !id.equals("null")) {
			farmerLandDetails = farmerService.findFarmerLandDetailsById(Long.valueOf(id));
			ProcurementProduct crops = farmerService.findCropByCropCode(farmerLandDetails.getCrops().trim());
			if(!StringUtil.isEmpty(crops)) {
				farmerLandDetails.setCrops(crops.getName());
			}
		}
		if (farmerLandDetails == null) {
			addActionError(NO_RECORD);
			view =  REDIRECT;
		} else {
		 setCurrentPage(getCurrentPage());
         command = UPDATE;
         view = DETAIL;
         request.setAttribute(HEADING, getText("detail"));
		}
		return view;
	}
	
	@Override
	public Object getData() {
		// TODO Auto-generated method stub
		return null;
	}

	 public Map<String, String> getDistance() {
		  Map<String, String> distance = new LinkedHashMap<>();
		  distance.put("1", getText("yes"));
		  distance.put("2", getText("no"));
		  return distance;

		 }
	 
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}

	public Farm getFarm() {
		return farm;
	}

	public void setFarm(Farm farm) {
		this.farm = farm;
	}

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	public String getFarmId() {
		return farmId;
	}

	public void setFarmId(String farmId) {
		this.farmId = farmId;
	}

	public FarmerLandDetails getFarmerLandDetails() {
		return farmerLandDetails;
	}

	public void setFarmerLandDetails(FarmerLandDetails farmerLandDetails) {
		this.farmerLandDetails = farmerLandDetails;
	}
	
	

}
