/*
 * Topic.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.entity.txn.training;

import java.util.List;

public class Topic {
    
    public static final int MAX_LENGTH_CODE=35;
    public static final int MAX_LENGTH_PRINCIPLE=255;
    public static final int MAX_LENGTH_DES=255;

    private long id;
    private String code;
    private String principle;
    private String des;
    private TopicCategory topicCategory;
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
     * Sets the principle.
     * @param principle the new principle
     */
    public void setPrinciple(String principle) {

        this.principle = principle;
    }

    /**
     * Gets the principle.
     * @return the principle
     */
   
    public String getPrinciple() {

        return principle;
    }

    /**
     * Sets the des.
     * @param des the new des
     */
    public void setDes(String des) {

        this.des = des;
    }

    /**
     * Gets the des.
     * @return the des
     */
  
    public String getDes() {

        return des;
    }

    /**
     * Gets the topic category.
     * @return the topic category
     */
    
    public TopicCategory getTopicCategory() {

        return topicCategory;
    }

    /**
     * Sets the topic category.
     * @param topicCategory the new topic category
     */
    public void setTopicCategory(TopicCategory topicCategory) {

        this.topicCategory = topicCategory;
    }

    /**
     * @param revisionNo the revisionNo to set
     */
    public void setRevisionNo(long revisionNo) {

        this.revisionNo = revisionNo;
    }

    /**
     * @return the revisionNo
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
