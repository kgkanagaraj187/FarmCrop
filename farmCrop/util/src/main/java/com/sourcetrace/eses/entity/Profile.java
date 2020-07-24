/*
 * Profile.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.entity;

import java.util.Date;

import javax.validation.GroupSequence;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.sourcetrace.eses.umgmt.entity.ContactInfo;
import com.sourcetrace.eses.umgmt.entity.PersonalInfo;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.First;
import com.sourcetrace.eses.util.Second;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.entity.ImageInfo;
import com.sourcetrace.esesw.entity.profile.ServicePoint;

@GroupSequence({ Profile.class, First.class, Second.class })
public class Profile {

    public static final String AGENT = "01";
    public static final String CLIENT = "02";
    public static final String CO_OPEARATIVE_MANAGER = "03";

    private static final int ES_AGENT_LENGTH = 12;
    private static final int ES_ID_LENGTH = 12;

    public final static int ACTIVE = 1;
    public final static int INACTIVE = 0;

    public static final int OFFLINE = 0;
    public static final int ONLINE = 1;

    public static final int MAX_LENGTH_PROFILE_ID = 12;

    private long id;
    private String profileId;
    private String profileType;
    private Double enrolledStationId;
    private String enrolledAgentId;
    private int status;
    private PersonalInfo personalInfo;
    private ContactInfo contactInfo;
    private NomineeInfo nomineeInfo;
    private Date createTime;
    private Date updateTime;
    private ImageInfo imageInfo;
    private long revisionNumber;
    private ServicePoint servicePoint;
    private String branchId;

    private Date createdDate;
    private Date updatedDate;
    private String createdUser;
    private String updatedUser;
    private char isActive;
    private String warehouseId;
    private Warehouse procurementCenter;
    // transient properties for audit log
    private String siteUser;
    private String action;
    private String filterStatus;
    private String mobileno;
    private String fs_status;
    /**
     * Gets the status.
     * @return the status
     */
    public int getStatus() {

        return status;
    }

    /**
     * Sets the status.
     * @param status the new status
     */
    public void setStatus(int status) {

        this.status = status;
    }

    /**
     * Gets the personal info.
     * @return the personal info
     */
    public PersonalInfo getPersonalInfo() {

        return personalInfo;
    }

    /**
     * Sets the personal info.
     * @param personalInfo the new personal info
     */
    public void setPersonalInfo(PersonalInfo personalInfo) {

        this.personalInfo = personalInfo;
    }

    /**
     * Gets the enrolled station id.
     * @return the enrolled station id
     */
   // @Length(max = ES_ID_LENGTH, message = "length.enrolledStationId")
  //  @Pattern(groups = First.class, regexp = "[^\\p{Punct}]+$", message = "pattern.enrolledStationId")
   public Double getEnrolledStationId() {

        return enrolledStationId;
    }

    /**
     * Sets the enrolled station id.
     * @param enrolledStationId the new enrolled station id
     */
    public void setEnrolledStationId(Double enrolledStationId) {

        this.enrolledStationId = enrolledStationId;
    }

    /**
     * Gets the enrolled agent id.
     * @return the enrolled agent id
     */
    @Length(max = ES_AGENT_LENGTH, message = "length.enrolledAgentId")
    @Pattern(groups = First.class, regexp = "[^\\p{Punct}]+$", message = "pattern.enrolledAgentId")
    public String getEnrolledAgentId() {

        return enrolledAgentId;
    }

    /**
     * Sets the enrolled agent id.
     * @param enrolledAgentId the new enrolled agent id
     */
    public void setEnrolledAgentId(String enrolledAgentId) {

        this.enrolledAgentId = enrolledAgentId;
    }

    /**
     * Gets the profile id.
     * @return the profile id
     */

    
    public String getProfileId() {

        return profileId;
    }

    /**
     * Sets the profile id.
     * @param id the new profile id
     */
    public void setProfileId(String id) {

        this.profileId = id;
    }

    /**
     * Gets the id.
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Gets the revision number.
     * @return the revision number
     */
    public long getRevisionNumber() {

        return revisionNumber;
    }

    /**
     * Sets the revision number.
     * @param revisionNumber the new revision number
     */
    public void setRevisionNumber(long revisionNumber) {

        this.revisionNumber = revisionNumber;
    }

    /**
     * Sets the site user.
     * @param userName the new site user
     */
    public void setSiteUser(String userName) {

        this.siteUser = userName;
    }

    /**
     * Gets the site user.
     * @return the site user
     */
    public String getSiteUser() {

        return siteUser;
    }

    /**
     * Gets the action.
     * @return the action
     */
    public String getAction() {

        return action;
    }

    /**
     * Sets the action.
     * @param action the new action
     */
    public void setAction(String action) {

        this.action = action;
    }

    /**
     * Sets the profile type.
     * @param profileType the new profile type
     */
    public void setProfileType(String profileType) {

        this.profileType = profileType;
    }

    /**
     * Gets the profile type.
     * @return the profile type
     */
    public String getProfileType() {

        return profileType;
    }

    /**
     * Sets the contact info.
     * @param contactInfo the new contact info
     */
    public void setContactInfo(ContactInfo contactInfo) {

        this.contactInfo = contactInfo;
    }

    /**
     * Gets the contact info.
     * @return the contact info
     */
    public ContactInfo getContactInfo() {

        return contactInfo;
    }

    /**
     * Sets the nominee info.
     * @param nomineeInfo the new nominee info
     */
    public void setNomineeInfo(NomineeInfo nomineeInfo) {

        this.nomineeInfo = nomineeInfo;
    }

    /**
     * Gets the nominee info.
     * @return the nominee info
     */
    public NomineeInfo getNomineeInfo() {

        return nomineeInfo;
    }

    /**
     * Sets the creates the time.
     * @param createTime the new creates the time
     */
    public void setCreateTime(Date createTime) {

        this.createTime = createTime;
    }

    /**
     * Gets the creates the time.
     * @return the creates the time
     */
    public Date getCreateTime() {

        return createTime;
    }

    /**
     * Sets the update time.
     * @param updateTime the new update time
     */
    public void setUpdateTime(Date updateTime) {

        this.updateTime = updateTime;
    }

    /**
     * Gets the update time.
     * @return the update time
     */
    public Date getUpdateTime() {

        return updateTime;
    }

    /**
     * Sets the image info.
     * @param imageInfo the new image info
     */
    public void setImageInfo(ImageInfo imageInfo) {

        this.imageInfo = imageInfo;
    }

    /**
     * Gets the image info.
     * @return the image info
     */
    public ImageInfo getImageInfo() {

        return imageInfo;
    }

    /**
     * Sets the service point.
     * @param servicePoint the new service point
     */
    public void setServicePoint(ServicePoint servicePoint) {

        this.servicePoint = servicePoint;
    }

    /**
     * Gets the service point.
     * @return the service point
     */
    public ServicePoint getServicePoint() {

        return servicePoint;
    }

    /**
     * Gets the profile id with name.
     * @return the profile id with name
     */
    public String getProfileIdWithName() {

        StringBuffer sb = new StringBuffer();
        if (!StringUtil.isEmpty(personalInfo)) {
            sb.append(personalInfo.getName()).append(" - ");
        }
        sb.append(profileId);
        return sb.toString();
    }

    public String getFilterStatus() {

        return filterStatus;
    }

    public void setFilterStatus(String filterStatus) {

        this.filterStatus = filterStatus;
    }

    public String getBranchId() {

        return branchId;
    }

    public void setBranchId(String branchId) {

        this.branchId = branchId;
    }

    public Date getCreatedDate() {

        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {

        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {

        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {

        this.updatedDate = updatedDate;
    }

    public String getCreatedUser() {

        return createdUser;
    }

    public void setCreatedUser(String createdUser) {

        this.createdUser = createdUser;
    }

    public String getUpdatedUser() {

        return updatedUser;
    }

    public void setUpdatedUser(String updatedUser) {

        this.updatedUser = updatedUser;
    }

    public void setCreationInfo(String userName) {

        this.createdUser = userName;
        this.updatedUser = userName;
        this.createdDate = new Date();
        this.updateTime = new Date();
        this.setRevisionNumber(DateUtil.getRevisionNumber());
    }

    public void setUpdationInfo(String userName) {

        this.createdUser = userName;
        this.updateTime = new Date();
        this.updatedDate = new Date();
        this.setRevisionNumber(DateUtil.getRevisionNumber());
    }

    public char getIsActive() {
    
        return isActive;
    }

    public void setIsActive(char isActive) {
    
        this.isActive = isActive;
    }

	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public Warehouse getProcurementCenter() {
		return procurementCenter;
	}

	public void setProcurementCenter(Warehouse procurementCenter) {
		this.procurementCenter = procurementCenter;
	}

	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

	public String getFs_status() {
		return fs_status;
	}

	public void setFs_status(String fs_status) {
		this.fs_status = fs_status;
	}

	

    
}
