package com.sourcetrace.esesw.entity.profile;

import java.util.ArrayList;
import java.util.List;

public class FarmCropsCoordinates implements Comparable<FarmCropsCoordinates> {
	public static enum typesOFCordinates {
		mainCoordinates,neighbouringDetails_farmcrops,neighbouringDetails_farm
	};
	
	private long id;
	private FarmCrops farmCrops;
	private String latitude;
	private String longitude;
	private long orderNo;
	private CoordinatesMap coordinatesMap;
	 private  CoordinatesMap activeCoordinates;

	 private String title;
	 private String description;
	 private int type;
	 private String lat;
	 private String lng;
	 private List<FarmCropsCoordinates> nearByLandDetails = new ArrayList<FarmCropsCoordinates>();
	 
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public FarmCrops getFarmCrops() {
		return farmCrops;
	}

	public void setFarmCrops(FarmCrops farmCrops) {
		this.farmCrops = farmCrops;
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

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}

	public int compareTo(FarmCropsCoordinates coord) {

		long compareQuantity = ((FarmCropsCoordinates) coord).getOrderNo();

		// ascending order
		return (int) (this.orderNo - compareQuantity);

	}

	public CoordinatesMap getCoordinatesMap() {
		return coordinatesMap;
	}

	public void setCoordinatesMap(CoordinatesMap coordinatesMap) {
		this.coordinatesMap = coordinatesMap;
	}

	public CoordinatesMap getActiveCoordinates() {
		return activeCoordinates;
	}

	public void setActiveCoordinates(CoordinatesMap activeCoordinates) {
		this.activeCoordinates = activeCoordinates;
	}



	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<FarmCropsCoordinates> getNearByLandDetails() {
		return nearByLandDetails;
	}

	public void setNearByLandDetails(List<FarmCropsCoordinates> nearByLandDetails) {
		this.nearByLandDetails = nearByLandDetails;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

}
