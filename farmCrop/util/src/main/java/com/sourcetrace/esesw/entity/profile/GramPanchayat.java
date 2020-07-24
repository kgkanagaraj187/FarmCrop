/*
 * GramPanchayat.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import java.util.List;
import java.util.Set;

import com.sourcetrace.eses.util.StringUtil;

public class GramPanchayat {

    private long id;
    private String code;
    private String name;
    private Municipality city;
    private Set<Village> villages;
    private long revisionNo;
    private String branchId;
    
    /**
	 * Transient variable
	 */
	private List<String> branchesList;

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
     * Gets the code.
     * @return the code
     */
//    @NotEmpty(message = "empty.code")
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
     * Gets the name.
     * @return the name
     */
//    @NotEmpty(message = "empty.name")
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
     * Gets the city.
     * @return the city
     */
    public Municipality getCity() {

        return city;
    }

    /**
     * Sets the city.
     * @param city the new city
     */
    public void setCity(Municipality city) {

        this.city = city;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return name;
    }

    /**
     * Gets the gram panchayat name.
     * @return the gram panchayat name
     */
    public String getGramPanchayatName() {

        StringBuffer gramPanchayatName = new StringBuffer();
        if (!StringUtil.isEmpty(code)) {
            gramPanchayatName.append(code);
            gramPanchayatName.append(" - ");
        }
        if (!StringUtil.isEmpty(name)) {
            gramPanchayatName.append(name);
        }
        return gramPanchayatName.toString();
    }

    /**
     * Gets the villages.
     * @return the villages
     */
    public Set<Village> getVillages() {

        return villages;
    }

    /**
     * Sets the villages.
     * @param villages the new villages
     */
    public void setVillages(Set<Village> villages) {

        this.villages = villages;
    }

    /**
     * Sets the revision no.
     * @param revisionNo the new revision no
     */
    public void setRevisionNo(long revisionNo) {

        this.revisionNo = revisionNo;
    }

    /**
     * Gets the revision no.
     * @return the revision no
     */
    public long getRevisionNo() {

        return revisionNo;
    }

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public List<String> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}
    
	
}
