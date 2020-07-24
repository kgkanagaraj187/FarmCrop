/*
 * Role.java
 * Copyright (c) 2010-2011, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.umgmt.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.validation.GroupSequence;

import com.sourcetrace.eses.util.First;
import com.sourcetrace.eses.util.Second;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.log.Auditable;

@GroupSequence({ Role.class, First.class, Second.class })
public class Role implements Serializable, Auditable {

    private static final long serialVersionUID = 5068238356258295644L;
    public static final int MAX_LENGTH_DESCRIPTION = 45;
    private long id;
    private String name;
    private String description;
    private boolean dataFiltered;
    private Filter filter;
    private Set<User> users;
    private Set<Entitlement> entitlements;
    private Set<Menu> menus;
    private String branchId;
    private String isAdminUser;
    
    private String parentBranchId;
    
	/**
	 * Transient variable
	 */
	private List<String> branchesList;

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
     * Checks if is data filtered.
     * @return true, if is data filtered
     */
    public boolean isDataFiltered() {

        return dataFiltered;
    }

    /**
     * Sets the data filtered.
     * @param dataFiltered the new data filtered
     */
    public void setDataFiltered(boolean dataFiltered) {

        this.dataFiltered = dataFiltered;
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
     * Gets the name.
     * @return the name
     */
    /*
     * @NotEmpty(message = "empty.name")
     * @NotNull(message = "empty.name")
     */
    // @NotBlank(message = "empty.name")
    // @Pattern(groups = First.class, regexp = "[^\\p{Punct}]+$", message = "pattern.name")
    // @Size(groups = Second.class, max = 35, message = "length.name")
    public String getName() {

        return name;
    }

    /**
     * Sets the name.
     * @param name the new name
     */
    public void setName(String name) {

        this.name = StringUtil.trim(name);
    }

    /**
     * Gets the description.
     * @return the description
     */
    // @Length(groups = Second.class, max = MAX_LENGTH_DESCRIPTION, message =
    // "length.description")
    public String getDescription() {

        return description;
    }

    /**
     * Sets the description.
     * @param description the new description
     */
    public void setDescription(String description) {

        this.description = StringUtil.trim(description);
    }

    /**
     * Gets the users.
     * @return the users
     */
    public Set<User> getUsers() {

        return users;
    }

    /**
     * Sets the users.
     * @param users the new users
     */
    public void setUsers(Set<User> users) {

        this.users = users;
    }

    /**
     * Gets the menus.
     * @return the menus
     */
    public Set<Entitlement> getEntitlements() {

        return entitlements;
    }

    /**
     * Sets the menus.
     * @param entitlements the entitlements
     */
    public void setEntitlements(Set<Entitlement> entitlements) {

        this.entitlements = entitlements;
    }

    /**
     * Gets the menus.
     * @return the menus
     */
    public Set<Menu> getMenus() {

        return menus;
    }

    /**
     * Sets the menus.
     * @param menus the new menus
     */
    public void setMenus(Set<Menu> menus) {

        this.menus = menus;
    }

    /**
     * Gets the abbreviation.
     * @return the abbreviation
     * @see com.ese.entity.ILocalizable#getAbbreviation()
     */
    public String getAbbreviation() {

        return null;
    }

    /**
     * Sets the abbreviation.
     * @param abbreviation the abbreviation
     * @see com.ese.entity.ILocalizable#setAbbreviation(java.lang.String)
     */
    public void setAbbreviation(String abbreviation) {

    }

    /**
     * Equals.
     * @param obj the obj
     * @return true, if successful
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {

        boolean equals = false;

        if (obj instanceof Role) {
            Role role = (Role) obj;
            equals = role.id == this.id;
        }

        return equals;
    }

    /**
     * Hash code.
     * @return the int
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        Object obj = new Long(id);
        return obj.hashCode();
    }

    /**
     * To string.
     * @return the string
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return name;
    }

    public String getBranchId() {

        return branchId;
    }

    public void setBranchId(String branchId) {

        this.branchId = branchId;
    }

    public String getIsAdminUser() {

        return isAdminUser;
    }

    public void setIsAdminUser(String isAdminUser) {

        this.isAdminUser = isAdminUser;
    }

	public List<String> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}

    public String getParentBranchId() {
    
        return parentBranchId;
    }

    public void setParentBranchId(String parentBranchId) {
    
        this.parentBranchId = parentBranchId;
    }
    
	
	
}
