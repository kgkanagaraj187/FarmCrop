/*
 * TrainingTopicAction.java
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
import com.ese.entity.txn.training.TrainingTopic;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.service.ITrainingService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

public class TrainingTopicAction extends SwitchValidatorAction {

    private static final long serialVersionUID = 1L;

    private ITrainingService trainingService;

    private TrainingTopic trainingTopic;

    private String id;
    private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Autowired
    private IUniqueIDGenerator idGenerator;

    /**
     * @see com.sourcetrace.esesw.view.SwitchAction#data()
     */
    @SuppressWarnings("unchecked")
    public String data() throws Exception {

        Map<String, String> searchRecord = getJQGridRequestParam(); // get the search parameter with
                                                                    // value

        TrainingTopic filter = new TrainingTopic();
        
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
        
      
        if (!StringUtil.isEmpty(searchRecord.get("name"))) {
            filter.setName(searchRecord.get("name").trim());
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

        TrainingTopic trainingTopic = (TrainingTopic) obj;
        JSONObject jsonObject = new JSONObject();
        JSONArray rows = new JSONArray();
        if ((getIsMultiBranch().equalsIgnoreCase("1") && (getIsParentBranch().equals("1")||StringUtil.isEmpty(branchIdValue)))) {
        	  if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(trainingTopic.getBranchId())))
							? getBranchesMap().get(getParentBranchMap().get(trainingTopic.getBranchId()))
							: getBranchesMap().get(trainingTopic.getBranchId()));
				}
				rows.add(getBranchesMap().get(trainingTopic.getBranchId()));
         
        } else {
        if (StringUtil.isEmpty(branchIdValue)) {
            rows.add(branchesMap.get(trainingTopic.getBranchId()));
        }
        }
        rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">" + trainingTopic.getCode()
                + "</font>");
        rows.add(trainingTopic.getName());
        jsonObject.put("id", trainingTopic.getId());
        jsonObject.put("cell", rows);
        return jsonObject;
    }

    /**
     * Creates the.
     * @throws Exception the exception
     */
    public void create() throws Exception {
    	trainingTopic=new TrainingTopic();
    	trainingTopic.setName(name);
    	trainingTopic.setCode(idGenerator.getTrainingTopicIdSeq());
    	trainingTopic.setBranchId(getBranchId());
    	TrainingTopic training = trainingService.findTrainingTopicByNameBasedOnBranch(trainingTopic.getName(),getBranchId());
    	if(StringUtil.isEmpty(training)&& training==null){
    		trainingService.addTrainingTopic(trainingTopic);
            getJsonObject().put("msg", getText("msg.added"));
            getJsonObject().put("title", getText("title.success"));
    	}else{
    		getJsonObject().put("msg", getText("msg.unique"));
            getJsonObject().put("title", getText("title.success"));
    	}
    	
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
    	 trainingTopic = trainingService.findTrainingTopicById(Long.valueOf(id));
    	TrainingTopic temp = trainingService.findTrainingTopicById(trainingTopic.getId());
    	 temp.setName(name);
         trainingService.editTrainingTopic(temp);
        getJsonObject().put("msg", getText("msg.updated"));
        getJsonObject().put("title", getText("title.success"));
        sendAjaxResponse(getJsonObject());
   }
}

    /**
     * Detail.
     * @return the string
     * @throws Exception the exception
     */
    public String detail() throws Exception {

        String view = "";
        if (id != null && !id.equals("")) {
            trainingTopic = trainingService.findTrainingTopicById(Long.valueOf(id));
            if (trainingTopic == null) {
                addActionError(NO_RECORD);
                return REDIRECT;
            }
            setCurrentPage(getCurrentPage());
            command = UPDATE;
            view = DETAIL;
            request.setAttribute(HEADING, getText("trainingtopicdetail"));
        } else {
            request.setAttribute(HEADING, getText("trainingtopiclist"));
            return LIST;
        }
        return view;
    }

    /**
     * Delete.
     * @return the string
     * @throws Exception the exception
     */
 
    public void delete() throws Exception {

        if (!StringUtil.isEmpty(id)) {
        	trainingTopic = trainingService.findTrainingTopicById(Long.valueOf(id));
        	setCurrentPage(getCurrentPage());
            boolean isTrainingTopicExistForFarmerSelection = trainingService.findFarmerTrainingSelectionWithTrainingTopic(Long.valueOf(id));
            if (isTrainingTopicExistForFarmerSelection) {
                getJsonObject().put("msg", getText("trainingtopicdelete.warn"));
                getJsonObject().put("title", getText("title.error"));
            } else {
                trainingService.removeTrainingTopic(trainingTopic);
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

        return trainingTopic;

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
     * Sets the training topic.
     * @param trainingTopic the new training topic
     */
    public void setTrainingTopic(TrainingTopic trainingTopic) {

        this.trainingTopic = trainingTopic;
    }

    /**
     * Gets the training topic.
     * @return the training topic
     */
    public TrainingTopic getTrainingTopic() {

        return trainingTopic;
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
    
	public List<TopicCategory> getTopicCategoryList() {

        return trainingService.listTopicCategory();
    }

}
