package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;

public class ForecastSoilMoisture {
	private Long id;
	private String lowSoilMoisture;
	private String highSoilMoisture;
	private String reason;
	private String level;
	private Long weatherForecastId;
	private String daysOfSegment;
	private Date dateTime;
	private String timeVal;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLowSoilMoisture() {
		return lowSoilMoisture;
	}

	public void setLowSoilMoisture(String lowSoilMoisture) {
		this.lowSoilMoisture = lowSoilMoisture;
	}

	public String getHighSoilMoisture() {
		return highSoilMoisture;
	}

	public void setHighSoilMoisture(String highSoilMoisture) {
		this.highSoilMoisture = highSoilMoisture;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Long getWeatherForecastId() {
		return weatherForecastId;
	}

	public void setWeatherForecastId(Long weatherForecastId) {
		this.weatherForecastId = weatherForecastId;
	}

	public String getDaysOfSegment() {
		return daysOfSegment;
	}

	public void setDaysOfSegment(String daysOfSegment) {
		this.daysOfSegment = daysOfSegment;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getTimeVal() {
		return timeVal;
	}

	public void setTimeVal(String timeVal) {
		this.timeVal = timeVal;
	}
	
	
}
