package com.sourcetrace.eses.adapter.core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.order.entity.txn.Forecast;
import com.sourcetrace.eses.order.entity.txn.ForecastDetails;
import com.sourcetrace.eses.order.entity.txn.WeatherForeCastDetails;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.JsonUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
@Component
public class ForecastSchedulerTask {
	private String cityName;
	public static Logger LOGGER = Logger.getLogger(WeatherForeCastSchedulerTask.class);
	private String FORECAST_URL = null;
	private String SOIL_MOISTURE_URL = null;
	private static final String format = "application/json";
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private IPreferencesService preferncesService;

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public void process() {
		Date localDate = null;
		Date updatedDate = null;
		List<WeatherForeCastDetails> weatheherForecastDetailList = null;
		WeatherForeCastDetails dt = null;
		String dateValue = "";
		String timeValue = "";
		Date tmpDate;
		String dateTimeValue = "";
		// TODO Auto-generated method stub
		try {

			farmerService.removeForecast();
			ESESystem preferences = preferncesService.findPrefernceById("1");
			if (!StringUtil.isEmpty(preferences)) {
				FORECAST_URL = preferences.getPreferences().get(ESESystem.FORECAST_URL);
				}
			List<Object[]> farmsList = farmerService.findLatAndLongByFarm();

			if (!ObjectUtil.isListEmpty(farmsList)) {
				for (Object[] obj : farmsList) {
					String latAndLong = null;

					String farmLat = String.valueOf(obj[1]).trim();
					String farmLon = String.valueOf(obj[2]).trim();
					latAndLong = "&latitude=" + String.valueOf(obj[1]).trim() + "&longitude="
							+ String.valueOf(obj[2]).trim() + "";
					String stringJson = JsonUtil.callURL(FORECAST_URL + latAndLong);
					JSONObject headObj = new JSONObject(stringJson);
					JSONObject dataObj = new JSONObject(String.valueOf(headObj.get("report")));
					Forecast foreCast = new Forecast();
					String cityName = (String) dataObj.get(TxnEnrollmentProperties.CITY_NAME);
					String stateName = (String) dataObj.get(TxnEnrollmentProperties.STATE_NAME);
					String countryName = (String) dataObj.get(TxnEnrollmentProperties.COUNTRY_NAME);
					String wLat = (String) String.valueOf(dataObj.getDouble(TxnEnrollmentProperties.WEATHER_LAT));
					String wLong = (String) String.valueOf(dataObj.getDouble(TxnEnrollmentProperties.WEATHER_LONG));
					String localTime = (String) dataObj.get(TxnEnrollmentProperties.LOCAL_TIME);
					String localUpdateTime = (String) dataObj.get(TxnEnrollmentProperties.LOCAL_UPDATE_TIME);
					/*
					 * String latAndLongExists =
					 * farmerService.findLatAndLong(String.valueOf(wLat),
					 * String.valueOf(wLong)); if
					 * (StringUtil.isEmpty(latAndLongExists)) {
					 */
					if (!StringUtil.isEmpty(localTime))
						localDate = DateUtil.convertStringToDate(DateUtil.convertWeatherDateTime(localTime),
								DateUtil.TXN_DATE_TIME);
					if (!StringUtil.isEmpty(localUpdateTime))
						updatedDate = DateUtil.convertStringToDate(DateUtil.convertWeatherDateTime(localUpdateTime),
								DateUtil.TXN_DATE_TIME);
					foreCast.setFarmId(Long.valueOf(String.valueOf(obj[0])));
					foreCast.setFarmCropId(Long.valueOf(String.valueOf(obj[3])));
					foreCast.setCity(cityName);
					foreCast.setState(stateName);
					foreCast.setCountry(countryName);
					foreCast.setLatitude(farmLat);
					foreCast.setLongitude(farmLon);
					foreCast.setLocalTime(localDate);
					foreCast.setLocalUpdateTime(updatedDate);

					JSONObject foreCastObj = new JSONObject(
							String.valueOf(dataObj.get(TxnEnrollmentProperties.WEATHER_LOCATION)));
					JSONArray locationDataObj = foreCastObj.getJSONArray(TxnEnrollmentProperties.FORE_CAST_LIST);
					Set<ForecastDetails> foreCastSet = new HashSet<>();
					List<WeatherForeCastDetails> foreCastList = new ArrayList<WeatherForeCastDetails>();
					
					for (int i = 0; i < locationDataObj.length(); i++) {
						JSONObject json = locationDataObj.getJSONObject(i);
						Iterator<String> keys = json.keys();
						ForecastDetails foreCastDetails = new ForecastDetails();
						while (keys.hasNext()) {
							String key = keys.next();
							String value = String.valueOf(json.get(key));
							if (key.equalsIgnoreCase(TxnEnrollmentProperties.DAY_SEQ))
								foreCastDetails.setDaySequence(value);
							if (key.equalsIgnoreCase(TxnEnrollmentProperties.DAY_WEEK))
								foreCastDetails.setDayOfWeek(value);
							if (key.equalsIgnoreCase(TxnEnrollmentProperties.WEEK_DAY))
								foreCastDetails.setWeekDay(value);
							if (key.equalsIgnoreCase(TxnEnrollmentProperties.DAY_LIGHT))
								foreCastDetails.setDayLight(value);
							if (key.equalsIgnoreCase(TxnEnrollmentProperties.WEATHER_DATE_KEY)) {
								/*
								 * foreCastDetails
								 * .setDate(DateUtil.convertStringToDate(value,
								 * DateUtil.WEATHER_DATE_FORMAT));
								 */
								dateValue = getDateValueFromDateKey(value);
								timeValue = getTimeValueFromDateKey(value);
								if(!StringUtil.isEmpty(dateValue)&&!StringUtil.isEmpty(timeValue)){
									dateTimeValue = dateValue+" "+timeValue;
								}
								if (!StringUtil.isEmpty(dateTimeValue)) {
									tmpDate = getDateTimeValueFromString(dateTimeValue);
									foreCastDetails.setDate(tmpDate);
								}
								/*if (!StringUtil.isEmpty(timeValue)) {
									foreCastDetails.setTimeVal(timeValue);
								}*/
							}
							if (key.equalsIgnoreCase(TxnEnrollmentProperties.HIGH_TEMP))
								foreCastDetails.setHighTemp(value);
							if (key.equalsIgnoreCase(TxnEnrollmentProperties.LOW_TEMP))
								foreCastDetails.setLowTemp(value);
							if (key.equalsIgnoreCase(TxnEnrollmentProperties.SKY_DESC))
								foreCastDetails.setSkyDesc(value);
							if (key.equalsIgnoreCase(TxnEnrollmentProperties.SKY))
								foreCastDetails.setSky(value);
							if (key.equalsIgnoreCase(TxnEnrollmentProperties.PRECIP_DESC))
								foreCastDetails.setPrecipDesc(value);
							if (key.equalsIgnoreCase(TxnEnrollmentProperties.PRECIP))
								foreCastDetails.setPrecip(value);
									if (key.equalsIgnoreCase(TxnEnrollmentProperties.TEMP_DESC))
								foreCastDetails.setTempDesc(value);
							if (key.equalsIgnoreCase(TxnEnrollmentProperties.AIR_DESC))
								foreCastDetails.setAirDesc(value);
							if (key.equalsIgnoreCase(TxnEnrollmentProperties.AIR))
								foreCastDetails.setAir(value);
							if (key.equalsIgnoreCase(TxnEnrollmentProperties.DESCRIPTION))
								foreCastDetails.setDescripton(value);
							if (key.equalsIgnoreCase(TxnEnrollmentProperties.UV_INDEX))
								foreCastDetails.setUvIndex(value);
							if (key.equalsIgnoreCase(TxnEnrollmentProperties.UV))
								foreCastDetails.setUv(value);
							if (key.equalsIgnoreCase(TxnEnrollmentProperties.WEATHER_WIND_SPEED))
								foreCastDetails.setWindSpeed(value);
							if (key.equalsIgnoreCase(TxnEnrollmentProperties.WIND_DIR))
								foreCastDetails.setWindDir(value);
							if (key.equalsIgnoreCase(TxnEnrollmentProperties.WIND_SHORT))
								foreCastDetails.setWindShort(value);
							if (key.equalsIgnoreCase(TxnEnrollmentProperties.WIND_LONG))
								foreCastDetails.setWindLong(value);
							if (key.equalsIgnoreCase(TxnEnrollmentProperties.WEATHER_HUMIDITY))
								foreCastDetails.setHumidity(value);
							if (key.equalsIgnoreCase(TxnEnrollmentProperties.DEW_POINT))
								foreCastDetails.setDewPoint(value);
							if (key.equalsIgnoreCase(TxnEnrollmentProperties.COMFORT))
								foreCastDetails.setComfort(value);

							if (key.equalsIgnoreCase(TxnEnrollmentProperties.RAIN_FALL))
								foreCastDetails.setRainFall(value);
							if (key.equalsIgnoreCase(TxnEnrollmentProperties.PRECIP_PROB))
								foreCastDetails.setPrecipProb(value);
							if (key.equalsIgnoreCase(TxnEnrollmentProperties.ICON))
								foreCastDetails.setIcon(value);
							if (key.equalsIgnoreCase(TxnEnrollmentProperties.ICON_NAME))
								foreCastDetails.setIconName(value);

							if (key.equalsIgnoreCase(TxnEnrollmentProperties.BEAUFORT))
								foreCastDetails.setBeaufort(value);
							if (key.equalsIgnoreCase(TxnEnrollmentProperties.BEAUFORT_DESC))
								foreCastDetails.setBeaufortDesc(value);
							if (key.equalsIgnoreCase(TxnEnrollmentProperties.BARO_PRESSURE))
								foreCastDetails.setBaroPressure(value);
							
							foreCastSet.add(foreCastDetails);
							System.out.println("Key :" + key + "  Value :" + json.get(key));
						}

					}
					foreCast.setForecastDetails(foreCastSet);
					 farmerService.addForeCast(foreCast);
				}
				
			}
			
		}	
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public Date getDateTimeValueFromString(String dateTimeValue) {
		Date tmpDate = null;
		try {
			tmpDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dateTimeValue);
		} catch (Exception e) {
			LOGGER.info("Date Parse Error: " + e.getMessage());
		}
		return tmpDate;
	}
	
	public String getDateValueFromDateKey(String dateValue) {
		String retValue = "";
		String[] splitValue;
		if (!StringUtil.isEmpty(dateValue)) {
			if (dateValue.contains("T")) {
				splitValue = dateValue.split("T");
				if (splitValue.length > 1) {

					retValue = splitValue[0];
				}
			}
		}
		return retValue;
	}
	
	public String getTimeValueFromDateKey(String dateValue) {
		String retValue = "";
		String[] splitValue;
		String[] splitTimeValue;
		if (!StringUtil.isEmpty(dateValue)) {
			if (dateValue.contains("T")) {
				splitValue = dateValue.split("T");
				if (splitValue.length > 1) {
					splitTimeValue = splitValue[1].split("\\+");
					if (splitTimeValue.length > 1) {
						retValue = splitTimeValue[0];
					}
				}
			}
		}
		return retValue;
	}
	
}
