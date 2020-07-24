package com.ese.entity.sms;

import java.util.Date;
import java.util.Set;

import com.sourcetrace.eses.entity.Profile;

public class ForecastSMSHistory  extends Profile{
	public static final int SMS_SINGLE = 1;
    public static final int SMS_BULK = 2;

    public static final int MOBILE_NO_LENGTH = 10;
    public static final String MOBILE_NO_PATTERN = "[0-9]*";
    public static final String BULK_MOBILE_NO_PATTERN = "[0-9,]*";
    public static final int MESSAGE_LENGTH = 160;

    private long id;
    private int smsType;
    private String smsRoute;
    private String senderMobNo;
    private String message;
    private String statusz;
    private String statusMsg;
    private String uuid;
    private Set<ForecastSMSHistoryDetail> forecastSMSHistoryDetails;
    private Date createDT;
    private String createUser;
    private Date lastUpdateDT;
    private String lastUpdateUser;
    private String branchId;
    private Long farmerId;
    private Long farmId;
    private Long advisoryId;
    
	// Transient
    private String receiverMobNo;

    public Long getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(Long farmerId) {
		this.farmerId = farmerId;
	}

	public Long getFarmId() {
		return farmId;
	}

	public void setFarmId(Long farmId) {
		this.farmId = farmId;
	}

	public Long getAdvisoryId() {
		return advisoryId;
	}

	public void setAdvisoryId(Long advisoryId) {
		this.advisoryId = advisoryId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getSmsType() {
		return smsType;
	}

	public void setSmsType(int smsType) {
		this.smsType = smsType;
	}

	public String getSmsRoute() {
		return smsRoute;
	}

	public void setSmsRoute(String smsRoute) {
		this.smsRoute = smsRoute;
	}

	public String getSenderMobNo() {
		return senderMobNo;
	}

	public void setSenderMobNo(String senderMobNo) {
		this.senderMobNo = senderMobNo;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatusz() {
		return statusz;
	}

	public void setStatusz(String statusz) {
		this.statusz = statusz;
	}

	public String getStatusMsg() {
		return statusMsg;
	}

	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	

	public Date getCreateDT() {
		return createDT;
	}

	public void setCreateDT(Date createDT) {
		this.createDT = createDT;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getLastUpdateDT() {
		return lastUpdateDT;
	}

	public void setLastUpdateDT(Date lastUpdateDT) {
		this.lastUpdateDT = lastUpdateDT;
	}

	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getReceiverMobNo() {
		return receiverMobNo;
	}

	public void setReceiverMobNo(String receiverMobNo) {
		this.receiverMobNo = receiverMobNo;
	}

	public static int getSmsSingle() {
		return SMS_SINGLE;
	}

	public static int getSmsBulk() {
		return SMS_BULK;
	}

	public static int getMobileNoLength() {
		return MOBILE_NO_LENGTH;
	}

	public static String getMobileNoPattern() {
		return MOBILE_NO_PATTERN;
	}

	public static String getBulkMobileNoPattern() {
		return BULK_MOBILE_NO_PATTERN;
	}

	public static int getMessageLength() {
		return MESSAGE_LENGTH;
	}

	public Set<ForecastSMSHistoryDetail> getForecastSMSHistoryDetails() {
		return forecastSMSHistoryDetails;
	}

	public void setForecastSMSHistoryDetails(Set<ForecastSMSHistoryDetail> forecastSMSHistoryDetails) {
		this.forecastSMSHistoryDetails = forecastSMSHistoryDetails;
	}
    
    
}
