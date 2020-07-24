/*
 * TopicCriteriaAction.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view.general;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.txn.training.FarmerTraining;
import com.ese.entity.txn.training.Topic;
import com.ese.entity.txn.training.TopicCategory;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.service.ITrainingService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.view.SwitchValidatorAction;
@SuppressWarnings("serial")
public class TopicCriteriaAction extends SwitchValidatorAction {

    private static final Logger LOGGER = Logger.getLogger(TopicCriteriaAction.class);
    @Autowired
    private ITrainingService trainingService;
    @Autowired
    private IUniqueIDGenerator idGenerator;
    private List<TopicCategory> topicCategoryList = new ArrayList<TopicCategory>();
    
    private String topicCategoryFilterComboData;
    private String id;
    private String principle;
    private String topicDes;
    private String topicCategoryId;
    public String getPrinciple() {
		return principle;
	}

	public void setPrinciple(String principle) {
		this.principle = principle;
	}

	public String getTopicDes() {
		return topicDes;
	}

	public void setTopicDes(String topicDes) {
		this.topicDes = topicDes;
	}

	public String getTopicCategoryId() {
		return topicCategoryId;
	}

	public void setTopicCategoryId(String topicCategoryId) {
		this.topicCategoryId = topicCategoryId;
	}

	private Topic topic;
    
    /**
     * List.
     * @return the string
     * @throws Exception the exception
     * @see com.sourcetrace.esesw.view.SwitchAction#list()
     */
    @Override
    public String list() throws Exception {

        buildTopicCategoryFilterComboData();
        return super.list();
    }

    /**
     * Creates the.
     * @return the string
     */
    public void populateCreate() throws Exception
    {
    		topic=new Topic();
	    	topic.setDes(topicDes);
	    	topic.setPrinciple(principle);
	    	TopicCategory topicCategory=trainingService.findTopicCategoryById(Long.valueOf(topicCategoryId));
	    	topic.setTopicCategory(topicCategory);
			topic.setCode(idGenerator.getCriteriaIdSeq());
	    	topic.setBranchId(getBranchId());
	        trainingService.addTopic(topic);
	        getJsonObject().put("msg", getText("msg.added"));
	        getJsonObject().put("title", getText("title.success"));
	        sendAjaxResponse(getJsonObject());
	        
       
    }

    /**
     * Update.
     * @return the string
     */
    public void populateupdate()
    {

     Topic tempTopic = trainingService.findTopicById(Long.valueOf(this.id));
	    if (!ObjectUtil.isEmpty(tempTopic)) {
	    	TopicCategory topicCategory=trainingService.findTopicCategoryById(Long.valueOf(topicCategoryId));
	     //   tempTopic.setCode(this.topic.getCode());
	        tempTopic.setPrinciple(principle);
	        tempTopic.setDes(topicDes);
	        tempTopic.setTopicCategory(topicCategory);
	        trainingService.editTopic(tempTopic);
	        getJsonObject().put("msg", getText("msg.updated"));
	        getJsonObject().put("title", getText("title.success"));
	        sendAjaxResponse(getJsonObject());
	    }
    }

    /**
     * Detail.
     * @return the string
     *//*
    public String detail() {

        if (!StringUtil.isEmpty(this.id)) {
            topic = trainingService.findTopicById(Long.valueOf(this.id));
            if (!ObjectUtil.isEmpty(topic)) {
                request.setAttribute(HEADING, getText(DETAIL));
                return DETAIL;
            }
        }
        return REDIRECT;
    }*/

    /**
     * Delete.
     * @return the string
     */
    public void populatedelete() {

        if (!StringUtil.isEmpty(id)) {
            topic = trainingService.findTopicById(Long.valueOf(this.id));
            if (!ObjectUtil.isEmpty(topic)) {
                List<FarmerTraining> farmerTrainingList = trainingService
                        .listFarmerTrainingByTopic(topic.getId());
                if (farmerTrainingList.size() > 0) {
                	getJsonObject().put("msg", getText("topiccriteriadelete.warn"));
        	        getJsonObject().put("title", getText("title.error"));
        	       
                } else {
                	getJsonObject().put("msg", getText("msg.deleted"));
        	        getJsonObject().put("title", getText("title.success"));
                    trainingService.removeTopic(topic);
                }
                sendAjaxResponse(getJsonObject());
            }
        }
    }

    /**
     * Data.
     * @return the string
     * @throws Exception the exception
     * @see com.sourcetrace.esesw.view.SwitchAction#data()
     */
    @SuppressWarnings("unchecked")
    public String populatedata() throws Exception {

        Map<String, String> searchRecord = getJQGridRequestParam();

        Topic filter = new Topic();
        
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

        if (!StringUtil.isEmpty(searchRecord.get("principle"))) {
            filter.setPrinciple(searchRecord.get("principle").trim());
        }
        
        if (!StringUtil.isEmpty(searchRecord.get("topicDes"))) {
            filter.setDes(searchRecord.get("topicDes").trim());
        }
        
        if (!StringUtil.isEmpty(searchRecord.get("topicCategoryId"))) {
            TopicCategory topicCategory = new TopicCategory();
            topicCategory.setName(searchRecord.get("topicCategoryId").trim());
            filter.setTopicCategory(topicCategory);
        }

        if (!StringUtil.isEmpty(searchRecord.get("topicCategory.name"))) {
            TopicCategory topicCategory = new TopicCategory();
            topicCategory.setName(searchRecord.get("topicCategory.name").trim());
            filter.setTopicCategory(topicCategory);
        }

        Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(),
                getResults(), filter, getPage());

        return sendJQGridJSONResponse(data);
    }

    @Override
    protected JSONObject toJSON(Object record) {

        JSONObject jsonObject = new JSONObject();
        JSONArray rows = new JSONArray();
        /*if (StringUtil.isEmpty(branchIdValue)) {
			rows.add(branchesMap.get(topic.getBranchId()));
		}*/
        if (record instanceof Topic) {
            Topic topic = (Topic) record;
            if (!ObjectUtil.isEmpty(topic)) {
                if ((getIsMultiBranch().equalsIgnoreCase("1") && (getIsParentBranch().equals("1")||StringUtil.isEmpty(branchIdValue)))) {

                    if (StringUtil.isEmpty(branchIdValue)) {
                        rows.add(!StringUtil.isEmpty(
                                getBranchesMap().get(getParentBranchMap().get(topic.getBranchId())))
                                        ? getBranchesMap()
                                                .get(getParentBranchMap().get(topic.getBranchId()))
                                        : getBranchesMap().get(topic.getBranchId()));
                    }
                    rows.add(getBranchesMap().get(topic.getBranchId()));

                } else {
                    if (StringUtil.isEmpty(branchIdValue)) {
                        rows.add(branchesMap.get(topic.getBranchId()));
                    }
                }
                rows.add(topic.getCode());
                rows.add(topic.getPrinciple());
                rows.add(topic.getDes());
                rows.add((!ObjectUtil.isEmpty(topic.getTopicCategory())) ? topic.getTopicCategory()
                        .getName() : "");
               
                jsonObject.put("id", topic.getId());
                jsonObject.put("cell", rows);
            }
        }

        return jsonObject;
    }

    /**
     * Gets the data.
     * @return the data
     * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
     */
    @Override
    public Object getData() {

        if (!ObjectUtil.isEmpty(this.topic)) {
            if (!ObjectUtil.isEmpty(this.topic.getTopicCategory())) {
                TopicCategory topicCategory = trainingService.findTopicCategoryById(this.topic
                        .getTopicCategory().getId());
                this.topic.setTopicCategory(topicCategory);
            }
        }
        return this.topic;
    }

    private void buildTopicCategoryFilterComboData() {

        this.topicCategoryList = getTopicCategoryList();
        StringBuffer sb = new StringBuffer();
        sb.append(":").append(getText("filter.allCategory")).append(";");
        for (TopicCategory topicCategory : topicCategoryList) {
            sb.append(topicCategory.getName()).append(":").append(topicCategory.getName()).append(
                    ";");
        }
        this.topicCategoryFilterComboData = sb.substring(0, sb.length() - 1);
    }

    /**
     * Sets the training service.
     * @param trainingService the new training service
     */
    public void setTrainingService(ITrainingService trainingService) {

        this.trainingService = trainingService;
    }

    /**
     * Gets the topic category list.
     * @return the topic category list
     */
    public List<TopicCategory> getTopicCategoryList() {

        return trainingService.listTopicCategory();
    }

    /**
     * Sets the topic category list.
     * @param topicCategoryList the new topic category list
     */
    public void setTopicCategoryList(List<TopicCategory> topicCategoryList) {

        this.topicCategoryList = topicCategoryList;
    }

    /**
     * Sets the topic category filter combo data.
     * @param topicCategoryFilterComboData the new topic category filter combo data
     */
    public void setTopicCategoryFilterComboData(String topicCategoryFilterComboData) {

        this.topicCategoryFilterComboData = topicCategoryFilterComboData;
    }

    /**
     * Gets the topic category filter combo data.
     * @return the topic category filter combo data
     */
    public String getTopicCategoryFilterComboData() {

        return this.topicCategoryFilterComboData;
    }

    /**
     * Sets the topic.
     * @param topic the topic to set
     */
    public void setTopic(Topic topic) {

        this.topic = topic;
    }

    /**
     * Gets the topic.
     * @return the topic
     */
    public Topic getTopic() {

        return topic;
    }

    /**
     * Gets the id.
     * @return the id
     */
    public String getId() {

        return id;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(String id) {

        this.id = id;
    }
    
    public void populateTopicCategory()
    {
    
	   List<TopicCategory> topicCategroyList =  trainingService.listTopicCategory();
	   JSONObject jsonObject=new JSONObject();
	   topicCategroyList.forEach(topicCategory->jsonObject.put(String.valueOf(topicCategory.getId()), topicCategory.getName()));
		try {
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			out.print(jsonObject.toString().replace("\"", "").replace(",", ";"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }	
    public void populateTopicCategoryList() throws Exception{
      	 
    	List<TopicCategory> topicCategroyList =  trainingService.listTopicCategory();
    	JSONArray topic = new JSONArray();
		if (!ObjectUtil.isEmpty(topicCategroyList)) {
    		 for (TopicCategory topicCategory: topicCategroyList) {
    			 topic.add(getJSONObject(topicCategory.getId(),topicCategory.getName()));
				} 
    	 }
		sendAjaxResponse(topic);
    	 }
    @SuppressWarnings("unchecked")
   	protected JSONObject getJSONObject( Object id, Object name) {

   		JSONObject jsonObject = new JSONObject();
   		jsonObject.put("id", id);
   		jsonObject.put("name", name);
   		return jsonObject;
   	}
}
