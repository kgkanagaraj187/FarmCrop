package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class AgentAccessLog {
    private long id;
    private String profileId;
    private Date login;
    private Date lastTxnTime;
    private String serialNo;
    private String mobileVersion;
    private String branchId;
    private Set<AgentAccessLogDetail> agentAccessLogDetails;
     
    /**Transient Variable*/
    private String agentName;
	private List<String> branchesList;
    public long getId() {
    
        return id;
    }

    public void setId(long id) {
    
        this.id = id;
    }

    public String getProfileId() {
    
        return profileId;
    }

    public void setProfileId(String profileId) {
    
        this.profileId = profileId;
    }

    public Date getLogin() {
    
        return login;
    }

    public void setLogin(Date login) {
    
        this.login = login;
    }

    public Date getLastTxnTime() {
    
        return lastTxnTime;
    }

    public void setLastTxnTime(Date lastTxnTime) {
    
        this.lastTxnTime = lastTxnTime;
    }

    public String getSerialNo() {
    
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
    
        this.serialNo = serialNo;
    }

    public String getMobileVersion() {
    
        return mobileVersion;
    }

    public void setMobileVersion(String mobileVersion) {
    
        this.mobileVersion = mobileVersion;
    }

    public String getBranchId() {
    
        return branchId;
    }

    public void setBranchId(String branchId) {
    
        this.branchId = branchId;
    }

    public Set<AgentAccessLogDetail> getAgentAccessLogDetails() {
    
        return agentAccessLogDetails;
    }

    public void setAgentAccessLogDetails(Set<AgentAccessLogDetail> agentAccessLogDetails) {
    
        this.agentAccessLogDetails = agentAccessLogDetails;
    }

    public String getAgentName() {
    
        return agentName;
    }

    public void setAgentName(String agentName) {
    
        this.agentName = agentName;
    }

	public List<String> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	} 
    
  
}
