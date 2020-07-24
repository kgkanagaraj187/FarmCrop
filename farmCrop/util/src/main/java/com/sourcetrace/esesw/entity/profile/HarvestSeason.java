/*
 * HarvestSeason.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

package com.sourcetrace.esesw.entity.profile;

import java.util.Date;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class HarvestSeason.
 * @author admin
 */
public class HarvestSeason {

    private String code;
    private long id;
    private String name;
    private Date fromPeriod;
    private Date toPeriod;
    private long revisionNo;
    private int currentSeason;
    private String branchId;
    
    //transient variable
    private String isCurrentSeason;
	private List<String> branchesList;
    public enum SeasonTypes{
    	NA,CURRENT_SEASON
    }

    /**
     * Gets the code.
     * @return the code
     */
   // @Pattern(regex = "[^\\p{Punct}]+$", message = "pattern.code")
    //@NotEmpty(message = "empty.code")
    public String getCode() {

        return code;
    }

    /**
     * Sets the code.
     * @param code the new code
     */
    public void setCode(String code) {

        this.code = code;
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
    //@Pattern(regex = "[^\\p{Punct}]+$", message = "pattern.name")
    //@NotEmpty(message = "empty.name")
    public String getName() {

        return name;
    }

    /**
     * Sets the name.
     * @param name the new name
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Gets the from period.
     * @return the from period
     */
    //@NotNull(message = "empty.fromPeriod")
    public Date getFromPeriod() {

        return fromPeriod;
    }

    /**
     * Sets the from period.
     * @param fromPeriod the new from period
     */
    public void setFromPeriod(Date fromPeriod) {

        this.fromPeriod = fromPeriod;
    }

    /**
     * Gets the to period.
     * @return the to period
     */
    //@NotNull(message = "empty.toPeriod")
    public Date getToPeriod() {

        return toPeriod;
    }

    /**
     * Sets the to period.
     * @param toPeriod the new to period
     */
    public void setToPeriod(Date toPeriod) {

        this.toPeriod = toPeriod;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HarvestSeason other = (HarvestSeason) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return name;
    }

    public long getRevisionNo() {

        return revisionNo;
    }

    public void setRevisionNo(long revisionNo) {

        this.revisionNo = revisionNo;
    }

    public int getCurrentSeason() {

        return currentSeason;
    }

    public void setCurrentSeason(int currentSeason) {

        this.currentSeason = currentSeason;
    }

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getIsCurrentSeason() {
		return isCurrentSeason;
	}

	public void setIsCurrentSeason(String isCurrentSeason) {
		this.isCurrentSeason = isCurrentSeason;
	}

	public List<String> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}
    
    

}
