package com.sourcetrace.esesw.view.general;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sourcetrace.eses.order.entity.txn.WeatherForeCast;
import com.sourcetrace.eses.order.entity.txn.WeatherForeCastDetails;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

public class WeatherForeCastAction extends SwitchValidatorAction {

	/**
	 * 
	 */
	private IFarmerService farmerService;
	private ILocationService locationService;
	private static final long serialVersionUID = 688003550680625533L;
	private String selectedCity;

	public String List() {

		return LIST;
	}

	public void populateCityList() {

		List<Object[]> citys = locationService.findCityNames();

		JSONArray citysArr = new JSONArray();
		if (!ObjectUtil.isListEmpty(citys)) {
			citys.forEach(obj -> {
				citysArr.add(getJSONObject(String.valueOf(obj[0]), String.valueOf(obj[0])));

			});
			sendAjaxResponse(citysArr);
		}

	}

	@SuppressWarnings("unchecked")
	public void populateForeCast() {
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<WeatherForeCast> forecastDatasList = farmerService.findForecastByCity(selectedCity,getDasyOfSeq());
		WeatherForeCast weatherForeCast = forecastDatasList.get(0);
		JSONObject weatherJsonObject = new JSONObject();
		if (!ObjectUtil.isEmpty(weatherForeCast.getLocalUpdateTime())) {
			weatherJsonObject.put("updatedDate", String.valueOf(weatherForeCast.getLocalUpdateTime()));
		}
		weatherJsonObject.put("city", selectedCity);
		jsonObjects.add(weatherJsonObject);

		for (WeatherForeCast forecastDatas : forecastDatasList) {

			for (WeatherForeCastDetails foreCastDetails : forecastDatas.getWeatherForeCastDetails()) {

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("date", String.valueOf(foreCastDetails.getDate()));
				jsonObject.put("temp",
						!StringUtil.isEmpty(foreCastDetails.getTemp()) ? foreCastDetails.getTemp() : "0.0");
				/*
				 * jsonObject.put("lowTemp",
				 * !StringUtil.isEmpty(foreCastDetails.getTemp()) ?
				 * foreCastDetails.getTemp() : "0.0");
				 */ jsonObject.put("humidity",
						!StringUtil.isEmpty(foreCastDetails.getHumidity()) ? foreCastDetails.getHumidity() : "0.0");
				jsonObject.put("windSpeed",
						!StringUtil.isEmpty(foreCastDetails.getWindSpeed()) ? foreCastDetails.getWindSpeed() : "0.0");
				jsonObject.put("rainFall",
						!StringUtil.isEmpty(foreCastDetails.getRainFall()) ? foreCastDetails.getRainFall() : "0.0");
				setForecastFields(jsonObject, foreCastDetails);
				jsonObjects.add(jsonObject);
			}
		}
		printAjaxResponse(jsonObjects, "text/html");
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
		// TODO Auto-generated method stub
		return null;
	}

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	public ILocationService getLocationService() {
		return locationService;
	}

	public void setLocationService(ILocationService locationService) {
		this.locationService = locationService;
	}

	public String getSelectedCity() {
		return selectedCity;
	}

	public void setSelectedCity(String selectedCity) {
		this.selectedCity = selectedCity;
	}

	public void setForecastFields(JSONObject jsonObject, WeatherForeCastDetails foreCastDetails) {
		jsonObject.put("lowSoilMoisture", !StringUtil.isEmpty(foreCastDetails.getLowSoilMoisture())
				? foreCastDetails.getLowSoilMoisture() : "0.0");
		jsonObject.put("highSoilMoisture", !StringUtil.isEmpty(foreCastDetails.getHighSoilMoisture())
				? foreCastDetails.getHighSoilMoisture() : "0.0");
	}
	
	public List<String> getDasyOfSeq()
	{
		List<String> list=new ArrayList<String>();
		list.add("M");
		list.add("E");
		return list;
		
	}
}
