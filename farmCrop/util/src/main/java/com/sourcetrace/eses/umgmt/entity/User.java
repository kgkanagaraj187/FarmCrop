/*
 * User.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.umgmt.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.GroupSequence;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.sourcetrace.eses.util.First;
import com.sourcetrace.eses.util.Second;
import com.sourcetrace.eses.util.log.Auditable;

// TODO: Auto-generated Javadoc
@GroupSequence({ User.class, First.class, Second.class })
public class User implements Serializable, Auditable {

    public static final int PROVIDER = 1;
    public static final int FEDERATION = 2;
    public static final int AFFILIATE = 3;
    public static final int STORE = 4;
    public static final int SERVICE_POINT = 5;
    public static final int SWITCH = 6;
    public static final int BRANCH = 7;

    public static final int NOT_MDB_USER = 5;
    public static final int USER_NOT_ENABLED = 0;
    public static final int INVALID_LOGIN = 1;
    public static final int ACCOUNT_BLOCKED = 2;
    public static final int SUCCESS_LOGIN = 3;
    public static final int NO_USER_PRESENT = 4;
    private static final int NAME_LENGTH = 50;
    public static final int PASSWD_MIN_LENGTH = 8;
    public static final int PASSWD_MAX_LENGTH = 16;

    public static final String ADMIN_USER_NAME = "exec";

    private long id;
    private boolean enabled;
    private String username;
    private String password;
    private String language;
    private PersonalInfo personalInfo;
    private ContactInfo contactInfo;
    private long milliSecond = 0;
    private Filter filter;
    private long dataId;
    private int status;
    private Role role;
    private Set<Entitlement> entitlements;
    private Map<Integer, String> preferences;
    private boolean reset;
    private Integer loginStatus;
    private String branchId;

    private Date createdDate;
    private Date updatedDate;
    private String createdUser;
    private String updatedUser;

    // transient audit log properties
    private String siteUser;
    private String action;
   
    // transient password change properties
    private boolean changePassword;
    private String confirmPassword;
    private String filterStatus;
    private boolean isAdmin;
    private String searchPage;
    private String oldPassword;
    private String isMultiBranch;
    private String parentBranchId;
    private List<String> branchesList;
    private String branchFiletr;
    // Transient SMS Module
    private String tempStatus;
    private String mobileno;
    private String wu_status;
    private String selectedUsers = "";
    
    
    public List<String> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}

	/**
     * Gets the role.
     * @return the role
     */
    public Role getRole() {

        return role;
    }

    /**
     * Sets the role.
     * @param role the new role
     */
    public void setRole(Role role) {

        this.role = role;
    }

    /**
     * Gets the filter.
     * @return the filter
     */
    public Filter getFilter() {

        return filter;
    }

    /**
     * Sets the filter.
     * @param filter the new filter
     */
    public void setFilter(Filter filter) {

        this.filter = filter;
    }

    /**
     * Gets the data id.
     * @return the data id
     */

    public long getDataId() {

        return dataId;
    }

    /**
     * Sets the data id.
     * @param dataId the new data id
     */
    public void setDataId(long dataId) {

        this.dataId = dataId;
    }

    /**
     * Checks if is enabled.
     * @return true, if is enabled
     */
    public boolean isEnabled() {

        return enabled;
    }

    /**
     * Sets the enabled.
     * @param enabled the new enabled
     */
    public void setEnabled(boolean enabled) {

        this.enabled = enabled;
    }

    /**
     * Checks if is change password.
     * @return true, if is change password
     */
    public boolean isChangePassword() {

        return changePassword;
    }

    /**
     * Sets the change password.
     * @param changePassword the new change password
     */
    public void setChangePassword(boolean changePassword) {

        this.changePassword = changePassword;
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
     * Gets the preferences.
     * @return the preferences
     */
    public Map<Integer, String> getPreferences() {

        return preferences;
    }

    /**
     * Sets the preferences.
     * @param preferences the preferences
     */
    public void setPreferences(Map<Integer, String> preferences) {

        this.preferences = preferences;
    }

    /**
     * Gets the username.
     * @return the username
     */
    @Length(max = NAME_LENGTH, message = "length.userName")
   // @Pattern(groups = First.class, regexp = "^[a-zA-Z0-9 ]+$", message = "pattern.userName")
    @NotEmpty(message = "empty.userName")
    public String getUsername() {

        return username;
    }

    /**
     * Sets the username.
     * @param username the new username
     */
    public void setUsername(String username) {

        this.username = username;
    }

    /**
     * Gets the password.
     * @return the password
     */

    @NotEmpty(message = "empty.password")
    public String getPassword() {

        return password;
    }

    /**
     * Sets the password.
     * @param password the new password
     */
    public void setPassword(String password) {

        this.password = password;
    }

    /**
     * Gets the confirm password.
     * @return the confirm password
     */
    public String getConfirmPassword() {

        return confirmPassword;
    }

    /**
     * Sets the confirm password.
     * @param confirmPassword the new confirm password
     */
    public void setConfirmPassword(String confirmPassword) {

        this.confirmPassword = confirmPassword;
    }

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
     * Gets the site user.
     * @return the site user
     */

    /**
     * Gets the site user.
     * @return the site user
     */
    public String getSiteUser() {

        return siteUser;
    }

    /**
     * Sets the site user.
     * @param userName the new site user
     */
    public void setSiteUser(String userName) {

        this.siteUser = userName;
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
     * Gets the milli second.
     * @return the milli second
     */
    public long getMilliSecond() {

        return milliSecond;
    }

    /**
     * Sets the milli second.
     * @param milliSecond the new milli second
     */
    public void setMilliSecond(long milliSecond) {

        this.milliSecond = milliSecond;
    }

    /**
     * Gets the language.
     * @return the language
     */
    @NotEmpty(message = "empty.language")
    public String getLanguage() {

        return language;
    }

    /**
     * Sets the language.
     * @param language the new language
     */
    public void setLanguage(String language) {

        this.language = language;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return username;
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
     * Gets the contact info.
     * @return the contact info
     */
    public ContactInfo getContactInfo() {

        return contactInfo;
    }

    /**
     * Sets the contact info.
     * @param contactInfo the new contact info
     */
    public void setContactInfo(ContactInfo contactInfo) {

        this.contactInfo = contactInfo;
    }

    /**
     * Gets the entitlements.
     * @return the entitlements
     */
    public Set<Entitlement> getEntitlements() {

        return entitlements;
    }

    /**
     * Sets the entitlements.
     * @param entitlements the new entitlements
     */
    public void setEntitlements(Set<Entitlement> entitlements) {

        this.entitlements = entitlements;
    }

    /**
     * Gets the filter status.
     * @return the filter status
     */
    public String getFilterStatus() {

        return filterStatus;
    }

    /**
     * Sets the filter status.
     * @param filterStatus the new filter status
     */
    public void setFilterStatus(String filterStatus) {

        this.filterStatus = filterStatus;
    }

    /**
     * Sets the admin.
     * @param isAdmin the new admin
     */
    public void setAdmin(boolean isAdmin) {

        this.isAdmin = isAdmin;
    }

    /**
     * Checks if is admin.
     * @return true, if is admin
     */
    public boolean isAdmin() {

        return isAdmin;
    }

    /**
     * Sets the reset.
     * @param reset the new reset
     */
    public void setReset(boolean reset) {

        this.reset = reset;
    }

    /**
     * Checks if is reset.
     * @return true, if is reset
     */
    public boolean isReset() {

        return reset;
    }

    public String getSearchPage() {

        return searchPage;
    }

    public void setSearchPage(String searchPage) {

        this.searchPage = searchPage;
    }

    public Integer getLoginStatus() {

        return loginStatus;
    }

    public void setLoginStatus(Integer loginStatus) {

        this.loginStatus = loginStatus;
    }

    public String getOldPassword() {

        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {

        this.oldPassword = oldPassword;
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

    public String getIsMultiBranch() {

        return isMultiBranch;
    }

    public void setIsMultiBranch(String isMultiBranch) {

        this.isMultiBranch = isMultiBranch;
    }

    public String getParentBranchId() {
    
        return parentBranchId;
    }

    public void setParentBranchId(String parentBranchId) {
    
        this.parentBranchId = parentBranchId;
    }

	public String getBranchFiletr() {
		return branchFiletr;
	}

	public void setBranchFiletr(String branchFiletr) {
		this.branchFiletr = branchFiletr;
	}

	public String getTempStatus() {
		return tempStatus;
	}

	public void setTempStatus(String tempStatus) {
		this.tempStatus = tempStatus;
	}

	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

	public String getWu_status() {
		return wu_status;
	}

	public void setWu_status(String wu_status) {
		this.wu_status = wu_status;
	}

	public String getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(String selectedUsers) {
		this.selectedUsers = selectedUsers;
	}
    
    

}
