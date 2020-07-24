/*
 * TargetGroup.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.entity.txn.training;

import java.util.List;

public class TargetGroup {

    public static final int MAX_LENGTH_CODE = 35;
    public static final int MAX_LENGTH_NAME = 255;

    private long id;
    private String code;
    private String name;
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

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        String str = (name == null || name.equals("")) ? code : name;
        return str;
    }

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	@Override
	 public int hashCode() {
	  final int prime = 31;
	  int result = 1;
	  result = prime * result + ((branchId == null) ? 0 : branchId.hashCode());
	  result = prime * result + ((code == null) ? 0 : code.hashCode());
	  result = prime * result + (int) (id ^ (id >>> 32));
	  result = prime * result + ((name == null) ? 0 : name.hashCode());
	  return result;
	 }

	 @Override
	 public boolean equals(Object obj) {
	  if (this == obj)
	   return true;
	  if (obj == null)
	   return false;
	  if (getClass() != obj.getClass())
	   return false;
	  TargetGroup other = (TargetGroup) obj;
	  if (branchId == null) {
	   if (other.branchId != null)
	    return false;
	  } else if (!branchId.equals(other.branchId))
	   return false;
	  if (code == null) {
	   if (other.code != null)
	    return false;
	  } else if (!code.equals(other.code))
	   return false;
	  if (id != other.id)
	   return false;
	  if (name == null) {
	   if (other.name != null)
	    return false;
	  } else if (!name.equals(other.name))
	   return false;
	  return true;
	 }

	public List<String> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}
    
}
