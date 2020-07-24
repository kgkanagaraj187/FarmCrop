package com.sourcetrace.esesw.entity.profile;


public class CropCalendarDetail
{
  private long id;
  private CropCalendar cropCalendar;
  private String activityMethod;
  private String noOfDays;
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}

public CropCalendar getCropCalendar() {
	return cropCalendar;
}
public void setCropCalendar(CropCalendar cropCalendar) {
	this.cropCalendar = cropCalendar;
}
public String getActivityMethod() {
	return activityMethod;
}
public void setActivityMethod(String activityMethod) {
	this.activityMethod = activityMethod;
}
public String getNoOfDays() {
	return noOfDays;
}
public void setNoOfDays(String noOfDays) {
	this.noOfDays = noOfDays;
}
  
 
}