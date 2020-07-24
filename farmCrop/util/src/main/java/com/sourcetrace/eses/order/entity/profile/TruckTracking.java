package com.sourcetrace.eses.order.entity.profile;

public class TruckTracking  {

	private long id;
	private String longitude;
	private String latitude;
	private String truckDate;
	private String txnId;

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setTruckDate(String truckDate) {
		this.truckDate = truckDate;
	}

	public String getTruckDate() {
		return truckDate;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

}
