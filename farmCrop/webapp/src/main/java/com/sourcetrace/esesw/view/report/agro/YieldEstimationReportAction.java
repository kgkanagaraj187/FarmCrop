package com.sourcetrace.esesw.view.report.agro;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.view.BaseReportAction;

public class YieldEstimationReportAction extends BaseReportAction {
	private static final long serialVersionUID = 1L;
	private FarmCrops filter;
	@Autowired
	private IFarmerService farmerService;
	private String gridIdentiy;
	private String farmerCount;
	private String areaCount;
	private String yieldCount;
	private String varietyCode;
	private String seasonCode;
	private static String totalYield;
	private static int totalFarmer;
	private static String totalArea;
	public List<String> farmerCountList=new ArrayList<>();
	long defaulVal=0;
	@Autowired
	private IPreferencesService preferncesService;
	private List<Long> cropList;
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	public String data() throws Exception {
		cropList=new ArrayList<>();
		if (!ObjectUtil.isEmpty(filter.getCropSeason())
				&& !StringUtil.isEmpty(filter.getCropSeason().getCode())) 
		{
			List<Object[]>  seasonObj=farmerService.findCropProductBySeason(filter.getCropSeason().getCode(),varietyCode);
			
			if (!ObjectUtil.isListEmpty(seasonObj))
			{
				
				for(Object[] obj:seasonObj)
				{
					if(!StringUtil.isEmpty(obj[1]) && String.valueOf(obj[2]).length()>0)
					{
						String showingDate = obj[1].toString().replace(".0", "");
						int intervalDays = DateUtil.daysBetween2Date(
							DateUtil.convertStringToDate(showingDate, DateUtil.TXN_DATE_TIME),
							DateUtil.convertStringToDate(startDate, getGeneralDateFormat()));
					
						int noGrowIntervalDays=DateUtil.daysBetween2Date(
							DateUtil.convertStringToDate(showingDate, DateUtil.TXN_DATE_TIME),
							new Date());
					if(noGrowIntervalDays<Integer.valueOf(!StringUtil.isEmpty(obj[2])?obj[2].toString():"0"))
					{
						
						if(Integer.valueOf(!StringUtil.isEmpty(obj[0])?obj[0].toString():"0")<=intervalDays)
						{
								cropList.add(Long.valueOf(obj[3].toString()));
								filter.setCropIdsList(cropList);
						}
						
						
					}
					
					else {
						cropList.add(defaulVal);
						filter.setCropIdsList(cropList);
					}
				}	
					
					else {
						cropList.add(defaulVal);
						filter.setCropIdsList(cropList);
					}
				}
				
			}
			
			else {
				cropList.add(defaulVal);
				filter.setCropIdsList(cropList);
			}

		
		
		} 
		else if(!StringUtil.isEmpty(varietyCode))
		{
			
			
			List<Object[]> varietyObj = farmerService.findSowingDateAndInterval(varietyCode);
			if (!ObjectUtil.isEmpty(varietyObj)) {
				for(Object[] obj:varietyObj)
				{
					
					if(!StringUtil.isEmpty(obj[1]) &&  String.valueOf(obj[2]).length()>0)
					{
					
						String showingDate = obj[1].toString().replace(".0", "");
						int intervalDays = DateUtil.daysBetween2Date(
								DateUtil.convertStringToDate(showingDate, DateUtil.TXN_DATE_TIME),
								DateUtil.convertStringToDate(startDate, getGeneralDateFormat()));
						
						int noGrowIntervalDays=DateUtil.daysBetween2Date(
								DateUtil.convertStringToDate(showingDate, DateUtil.TXN_DATE_TIME),
								new Date());
						
						if(noGrowIntervalDays<=Integer.valueOf(!StringUtil.isEmpty(obj[2])?obj[2].toString():"0"))
						{
							
							if(Integer.valueOf(!StringUtil.isEmpty(obj[0])?obj[0].toString():"0")<=intervalDays)
							{
						
							cropList.add(Long.valueOf(obj[3].toString()));
							filter.setCropIdsList(cropList);
							}
						}
						else {
							cropList.add(defaulVal);
							filter.setCropIdsList(cropList);
						}
					}	
						
					}	
				}	
				
				else {
					cropList.add(defaulVal);
					filter.setCropIdsList(cropList);
				}
					
				/*
				 * Date nowdate =
				 * DateUtil.convertStringToDate(endDate,DateUtil.DATE_FORMAT);
				 * long nowms = nowdate.getTime(); long differencems =
				 * intervalDays * 24L * 60 * 60 * 1000; long thenms = nowms -
				 * differencems; Date thendate = new Date(thenms); String
				 * showDate=df.format(thendate);
				 * this.filter.setSowingDate(DateUtil.convertStringToDate(
				 * showDate, DateUtil.DATE));
				 */
			}
		else {
			cropList.add(defaulVal);
			filter.setCropIdsList(cropList);
		}

		super.filter = this.filter;
		return sendJSONResponse(readData("yieldReport"));
	}

	
	
	public String subGridDetail() throws Exception {

		if (ObjectUtil.isEmpty(this.filter))
			this.filter = new FarmCrops();
		String[] ids = id.split("-");
		filter.setProcurementproductId(Long.valueOf(ids[0]));
		filter.setFarmerId(Long.valueOf(ids[1]));

		gridIdentiy = "SUBGRID";
		super.filter = this.filter;
		Map data = readData("yieldReportSubgrid");
		return sendJSONResponse(data);

	}
	double totalYie = 0.0;
	double totArea = 0.0;
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object record) {
		
		Object[] data = (Object[]) record;
		JSONObject jsonObject = new JSONObject();
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (gridIdentiy != "SUBGRID") {
			if (data.length > 1) {
			
				JSONArray rows = new JSONArray();
				rows.add(!StringUtil.isEmpty(data[3]) ? data[3].toString() : "");
				rows.add(!StringUtil.isEmpty(data[4]) ? data[4].toString() : "");
				rows.add(!StringUtil.isEmpty(data[5]) ? data[5].toString() : "");
				rows.add(!StringUtil.isEmpty(data[6]) ? data[6].toString() : "");
				rows.add(!StringUtil.isEmpty(data[12]) ? data[12].toString() : "");

				double daysGrow = Double.valueOf(String.valueOf(!ObjectUtil.isEmpty(data[8])?data[8].toString():"0.0")) - Double.valueOf(!ObjectUtil.isEmpty(data[9])?data[9].toString():"0.0");
				if (!ObjectUtil.isEmpty(data[11]) && !StringUtil.isEmpty(endDate)) {
					
					String sowingDate=data[11].toString().replace(".0", "");
					Integer intervalDays = DateUtil.daysBetween2Date(
							DateUtil.convertStringToDate(sowingDate, DateUtil.TXN_DATE_TIME),
							DateUtil.convertStringToDate(endDate, getGeneralDateFormat()));
					intervalDays=intervalDays-Integer.valueOf(!StringUtil.isEmpty(data[9])?String.valueOf(data[9]):"0");
					double totYield = (Double.valueOf(!ObjectUtil.isEmpty(data[10])?data[10].toString():"0.0") / daysGrow * (intervalDays + 1))
							* Double.valueOf(!ObjectUtil.isEmpty(data[12])?data[12].toString():"1");
					rows.add(CurrencyUtil.getDecimalFormat(totYield, "##.00"));
					
					if(!StringUtil.isEmpty(totYield))
					{
						
						 totalYie+=totYield;
						totalYield=CurrencyUtil.getDecimalFormat(totalYie, "##.00");
					}
					farmerCountList.add(!ObjectUtil.isEmpty(data[1])?data[1].toString():"");
					totalFarmer=(!ObjectUtil.isListEmpty(farmerCountList)?farmerCountList.size():0);
					
					totArea+=Double.valueOf(!ObjectUtil.isEmpty(data[12])?data[12].toString():"0");
					totalArea=CurrencyUtil.getDecimalFormat(totArea, "##.00");;
				}
				rows.add(!ObjectUtil.isEmpty(data[7]) ? data[7].toString() : "");
				String ids = data[0] + "-" + data[1];
				jsonObject.put("id", ids);
				jsonObject.put("cell", rows);

			}
		} else {
			if (data.length > 0) {
				JSONArray rows = new JSONArray();
				rows.add(!ObjectUtil.isEmpty(data[0]) ? String.valueOf(data[0]) : "");
				rows.add(!ObjectUtil.isEmpty(data[1]) ? String.valueOf(data[1]) : "");
				rows.add(!ObjectUtil.isEmpty(data[2]) ? String.valueOf(data[2]) : "");
				//rows.add(!ObjectUtil.isEmpty(data[3]) ? String.valueOf(data[3]) : "");
				if (!ObjectUtil.isEmpty(preferences)){
					DateFormat genDate = new SimpleDateFormat(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
				rows.add(!ObjectUtil.isEmpty(data[3]) ? String.valueOf(genDate.format(data[3])) : "");
				}
				
	    		  
				/* rows.add(crops[6]); */
				/*
				 * rows.add(warehousePaymentDetails.getProduct().
				 * getSubcategory().getCode() + "-" +
				 * warehousePaymentDetails.getProduct().getSubcategory().
				 * getName());
				 */

				jsonObject.put("id", data[0]);
				jsonObject.put("cell", rows);
			}

		}

		return jsonObject;
	}

	public FarmCrops getFilter() {
		return filter;
	}

	public void setFilter(FarmCrops filter) {
		this.filter = filter;
	}

	/**
	 * Gets the current date.
	 * 
	 * @return the current date
	 */
	public String getCurrentDate() {
		 ESESystem preferences = preferncesService.findPrefernceById("1");
			DateFormat genDate = new SimpleDateFormat(!ObjectUtil.isEmpty(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT))?preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT):DateUtil.DATE_FORMAT);
		
		return genDate.format(new Date());
	}

	public Map<String, String> getCropProductList() {
		List<Object[]> productsList = farmerService.findFarmCropProductList();

		return productsList.stream().filter(obj -> !ObjectUtil.isEmpty(obj))
				.collect(Collectors.toMap(objKey -> String.valueOf(objKey[0]), objVal -> String.valueOf(objVal[1])+"-"+String.valueOf(objVal[2])));
	}

	public String getGridIdentiy() {
		return gridIdentiy;
	}

	public void setGridIdentiy(String gridIdentiy) {
		this.gridIdentiy = gridIdentiy;
	}

	public Map<String, String> getSeasonList() {

		Map<String, String> seasonMap = new LinkedHashMap<String, String>();
		List<Object[]> seasonList = farmerService.listSeasonCodeAndName();
		seasonMap = seasonList.stream()
				.collect(Collectors.toMap(obj -> (String.valueOf(obj[0])), obj -> String.valueOf(obj[1])));
		Map<String, String> orderedMap = seasonMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
		return orderedMap;
	}

	public String getFarmerCount() {
		return farmerCount;
	}

	public void setFarmerCount(String farmerCount) {
		this.farmerCount = farmerCount;
	}

	public String getAreaCount() {
		return areaCount;
	}

	public void setAreaCount(String areaCount) {
		this.areaCount = areaCount;
	}

	public String getYieldCount() {
		return yieldCount;
	}

	public void setYieldCount(String yieldCount) {
		this.yieldCount = yieldCount;
	}

	public String getVarietyCode() {
		return varietyCode;
	}

	public void setVarietyCode(String varietyCode) {
		this.varietyCode = varietyCode;
	}

	public void populateTotalValues() {
		
		JSONObject jsonObject = new JSONObject();
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();

		jsonObject.put("fCount", String.valueOf(!ObjectUtil.isEmpty(totalFarmer)?totalFarmer:""));
		jsonObject.put("tArea", String.valueOf(!ObjectUtil.isEmpty(totalArea)?totalArea:""));
		jsonObject.put("tYield", String.valueOf(!ObjectUtil.isEmpty(totalYield)?totalYield:""));

		jsonObjects.add(jsonObject);
		printAjaxResponse(jsonObjects, "text/html");
	}
	
	public String getSeasonCode() {
		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
	}


	
}
