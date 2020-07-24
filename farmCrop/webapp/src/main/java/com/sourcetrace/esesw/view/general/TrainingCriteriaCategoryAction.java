/*
 * TrainingCriteriaCategoryAction.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view.general;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.txn.training.TopicCategory;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.service.ITrainingService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

/**
 * The Class TrainingCriteriaCategoryAction.
 */
public class TrainingCriteriaCategoryAction extends SwitchValidatorAction {

    private static final long serialVersionUID = 1L;
    private ITrainingService trainingService;
    private TopicCategory trainingCriteriaCategory;
    private String id;
    private String topicCategoryName;
    public String getTopicCategoryName() {
		return topicCategoryName;
	}

	public void setTopicCategoryName(String topicCategoryName) {
		this.topicCategoryName = topicCategoryName;
	}

	@Autowired
    private IUniqueIDGenerator idGenerator;
    
    
    /**
     * @see com.sourcetrace.esesw.view.SwitchAction#data()
     */
    @SuppressWarnings("unchecked")
    public String populatedata() throws Exception {

        Map<String, String> searchRecord = getJQGridRequestParam(); 
        
        TopicCategory filter = new TopicCategory();
        
	    if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
            if (!getIsMultiBranch().equalsIgnoreCase("1")) {
                List<String> branchList = new ArrayList<>();
                branchList.add(searchRecord.get("branchId").trim());
                filter.setBranchesList(branchList);
            } else {
                List<String> branchList = new ArrayList<>();
                List<BranchMaster> branches = clientService
                        .listChildBranchIds(searchRecord.get("branchId").trim());
                branchList.add(searchRecord.get("branchId").trim());
                branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
                    branchList.add(branch.getBranchId());
                });
                filter.setBranchesList(branchList);
            }
        }
        
        if (!StringUtil.isEmpty(searchRecord.get("subBranchId"))) {
            filter.setBranchId(searchRecord.get("subBranchId").trim());
        }
        
        if (!StringUtil.isEmpty(searchRecord.get("code"))) {
            filter.setCode(searchRecord.get("code").trim());
        }

        if (!StringUtil.isEmpty(searchRecord.get("topicCategoryName"))) {
            filter.setName(searchRecord.get("topicCategoryName").trim());
        }

        Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(),
                getResults(), filter, getPage());

        return sendJQGridJSONResponse(data);
    }

    /**
     * @see com.sourcetrace.esesw.view.SwitchAction#toJSON(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJSON(Object obj) {

        TopicCategory category = (TopicCategory) obj;
        JSONObject jsonObject = new JSONObject();
        JSONArray rows = new JSONArray();
	  	if ((getIsMultiBranch().equalsIgnoreCase("1") && (getIsParentBranch().equals("1")||StringUtil.isEmpty(branchIdValue)))) {

	          if (StringUtil.isEmpty(branchIdValue)) {
	              rows.add(!StringUtil.isEmpty(
	                      getBranchesMap().get(getParentBranchMap().get(category.getBranchId())))
	                              ? getBranchesMap()
	                                      .get(getParentBranchMap().get(category.getBranchId()))
	                              : getBranchesMap().get(category.getBranchId()));
	          }
	          rows.add(getBranchesMap().get(category.getBranchId()));

	      } else {
	          if (StringUtil.isEmpty(branchIdValue)) {
	              rows.add(branchesMap.get(category.getBranchId()));
	          }
	      }
        rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">" + category.getCode()
                + "</font>");
        rows.add(category.getName());
        jsonObject.put("id", category.getId());
        jsonObject.put("cell", rows);
        return jsonObject;
    }

    /**
     * Creates the.
     * @return the string
     * @throws Exception the exception
     */
    public void populatecreate() throws Exception
    {
    	trainingCriteriaCategory=new TopicCategory();
    	trainingCriteriaCategory.setName(topicCategoryName);
		trainingCriteriaCategory.setCode(idGenerator.getTrainingCriteriaCategoryIdSeq());
    	trainingCriteriaCategory.setBranchId(getBranchId());
        trainingService.addTopicCategory(trainingCriteriaCategory);
        getJsonObject().put("msg", getText("msg.added"));
        getJsonObject().put("title", getText("title.success"));
        sendAjaxResponse(getJsonObject());
           
    }

    /**
     * Update.
     * @return the string
     * @throws Exception the exception
     */
    public void update() throws Exception {

        if (id != null && !id.equals(""))
        {
            TopicCategory temp = trainingService.findTopicCategoryById(Long.valueOf(this.id));
            temp.setName(topicCategoryName);
            trainingService.editTopicCategory(temp);
            getJsonObject().put("msg", getText("msg.updated"));
            getJsonObject().put("title", getText("title.success"));
            sendAjaxResponse(getJsonObject());
       }
    }

   /* *//**
     * Detail.
     * @return the string
     * @throws Exception the exception
     *//*
    public String detail() throws Exception {

        String view = "";
        if (id != null && !id.equals("")) {
            trainingCriteriaCategory = trainingService.findTopicCategoryById(Long.valueOf(id));
            if (trainingCriteriaCategory == null) {
                addActionError(NO_RECORD);
                return REDIRECT;
            }
            setCurrentPage(getCurrentPage());
            command = UPDATE;
            view = DETAIL;
            request.setAttribute(HEADING, getText("trainingcriteriadetail"));
        } else {
            request.setAttribute(HEADING, getText("trainingcriterialist"));
            return LIST;
        }
        return view;
    }*/

    /**
     * Delete.
     * @return the string
     * @throws Exception the exception
     */
    public void populatedelete() throws Exception {

        if (!StringUtil.isEmpty(id)) {
            trainingCriteriaCategory = trainingService.findTopicCategoryById(Long.valueOf(id));
           if (trainingCriteriaCategory != null
                    && !ObjectUtil.isListEmpty(trainingCriteriaCategory.getTopics())) {
                getJsonObject().put("msg", getText("trainingcriteriadelete.warn"));
                getJsonObject().put("title", getText("title.error"));
            } else {
                trainingService.removeTopicCategory(trainingCriteriaCategory);
                getJsonObject().put("msg", getText("msg.deleted"));
                getJsonObject().put("title", getText("title.success"));
               
            }

        }

        sendAjaxResponse(getJsonObject());

    }

    /**
     * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
     */
    @Override
    public Object getData() {

        return trainingCriteriaCategory;

    }

    /**
     * Sets the training service.
     * @param trainingService the new training service
     */
    public void setTrainingService(ITrainingService trainingService) {

        this.trainingService = trainingService;
    }

    /**
     * Gets the training service.
     * @return the training service
     */
    public ITrainingService getTrainingService() {

        return trainingService;
    }

    /**
     * Sets the training criteria category.
     * @param trainingCriteriaCategory the new training criteria category
     */
    public void setTrainingCriteriaCategory(TopicCategory trainingCriteriaCategory) {

        this.trainingCriteriaCategory = trainingCriteriaCategory;
    }

    /**
     * Gets the training criteria category.
     * @return the training criteria category
     */
    public TopicCategory getTrainingCriteriaCategory() {

        return trainingCriteriaCategory;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(String id) {

        this.id = id;
    }

    /**
     * Gets the id.
     * @return the id
     */
    public String getId() {

        return id;
    }

	public IUniqueIDGenerator getIdGenerator() {
		return idGenerator;
	}

	public void setIdGenerator(IUniqueIDGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}
    
    

}
