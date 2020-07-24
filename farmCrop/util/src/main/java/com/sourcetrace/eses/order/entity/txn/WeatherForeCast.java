package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;
import java.util.Set;

public class WeatherForeCast
{
	private Long id;
	private String city;
	private String state;
	private String country;
	private String latitude;
	private String longitude;
	private Date localTime;
	private Date localUpdateTime;
	private String date;
	private Set<WeatherForeCastDetails> weatherForeCastDetails;
	private Set<ForecastSoilMoisture> forecastSoilMoistures;
	private Long farmId;
	private Long farmCropId;
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
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

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Set<WeatherForeCastDetails> getWeatherForeCastDetails() {
		return weatherForeCastDetails;
	}
	public void setWeatherForeCastDetails(Set<WeatherForeCastDetails> weatherForeCastDetails) {
		this.weatherForeCastDetails = weatherForeCastDetails;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getLocalTime() {
		return localTime;
	}
	public void setLocalTime(Date localTime) {
		this.localTime = localTime;
	}
	public Date getLocalUpdateTime() {
		return localUpdateTime;
	}
	public void setLocalUpdateTime(Date localUpdateTime) {
		this.localUpdateTime = localUpdateTime;
	}
	public Long getFarmId() {
		return farmId;
	}
	public void setFarmId(Long farmId) {
		this.farmId = farmId;
	}
	public Long getFarmCropId() {
		return farmCropId;
	}
	public void setFarmCropId(Long farmCropId) {
		this.farmCropId = farmCropId;
	}
	public Set<ForecastSoilMoisture> getForecastSoilMoistures() {
		return forecastSoilMoistures;
	}
	public void setForecastSoilMoistures(Set<ForecastSoilMoisture> forecastSoilMoistures) {
		this.forecastSoilMoistures = forecastSoilMoistures;
	}

}
