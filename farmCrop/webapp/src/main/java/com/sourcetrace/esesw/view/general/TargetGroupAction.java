/*
 * TargetGroupAction.java
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

import com.ese.entity.txn.training.FarmerTraining;
import com.ese.entity.txn.training.TargetGroup;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.service.ITrainingService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

public class TargetGroupAction extends SwitchValidatorAction {

    private static final long serialVersionUID = -7330130379881963430L;

    private String id;
    private TargetGroup targetGroup;
    private ITrainingService trainingService;
    
    @Autowired
    private IUniqueIDGenerator idGenerator;
    private String targetGroupName;

    /**
     * Gets the data.
     * @return the data
     * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
     */
    @Override
    public Object getData() {

        return targetGroup;
    }

    /**
     * Data.
     * @return the string
     * @throws Exception the exception
     * @see com.sourcetrace.esesw.view.SwitchAction#data()
     */
    public String data() throws Exception {

        Map<String, String> searchRecord = getJQGridRequestParam(); // get the search parameter with
        // value

        TargetGroup filter = new TargetGroup();
        
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
     * To json.
     * @param obj the obj
     * @return the JSON object
     * @see com.sourcetrace.esesw.view.SwitchAction#toJSON(java.lang.Object)
     */
    public JSONObject toJSON(Object obj) {

        TargetGroup targetGroup = (TargetGroup) obj;
        JSONObject jsonObject = new JSONObject();
        JSONArray rows = new JSONArray();
        if ((getIsMultiBranch().equalsIgnoreCase("1") && (getIsParentBranch().equals("1")||StringUtil.isEmpty(branchIdValue)))) {

            if (StringUtil.isEmpty(branchIdValue)) {
                rows.add(!StringUtil.isEmpty(
                        getBranchesMap().get(getParentBranchMap().get(targetGroup.getBranchId())))
                                ? getBranchesMap()
                                        .get(getParentBranchMap().get(targetGroup.getBranchId()))
                                : getBranchesMap().get(targetGroup.getBranchId()));
            }
            rows.add(getBranchesMap().get(targetGroup.getBranchId()));

        } else {
            if (StringUtil.isEmpty(branchIdValue)) {
                rows.add(branchesMap.get(targetGroup.getBranchId()));
            }
        }
        rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">" + targetGroup.getCode()
                + "</font>");
        rows.add(targetGroup.getName());
        jsonObject.put("id", targetGroup.getId());
        jsonObject.put("cell", rows);
        return jsonObject;
    }

    /**
     * Creates the.
     * @return the string
     * @throws Exception the exception
     */
    @SuppressWarnings("unchecked")
	public void create() throws Exception
    {
    	if(!StringUtil.isEmpty(targetGroupName))
    	{
	    	targetGroup=new TargetGroup();
	    	targetGroup.setName(targetGroupName);
	    	targetGroup.setCode(idGenerator.getTargetGroupIdSeq());
	        targetGroup.setBranchId(getBranchId());
	        trainingService.addTargetGroup(targetGroup);
	        getJsonObject().put("msg", getText("msg.added"));
	        getJsonObject().put("title", getText("title.success"));
	        sendAjaxResponse(getJsonObject());
	        
    	}
        
    }

    /**
     * Update.
     * @return the string
     * @throws Exception the exception
     */
    public void update() throws Exception
    {

        if (id != null && !id.equals(""))
        {
           TargetGroup temp = trainingService.findTargetGroupById(Long.valueOf(id));
            temp.setName(targetGroupName);
            trainingService.editTargetGroup(temp);
            getJsonObject().put("msg", getText("msg.updated"));
	        getJsonObject().put("title", getText("title.success"));
	        sendAjaxResponse(getJsonObject());
         }
      }

    /**
     * Detail.
     * @return the string
     * @throws Exception the exception
     *//*
    public String detail() throws Exception {

        String view = "";
        if (id != null && !id.equals("")) {
            targetGroup = trainingService.findTargetGroupById(Long.valueOf(id));
            if (targetGroup == null) {
                addActionError(NO_RECORD);
                return REDIRECT;
            }
            setCurrentPage(getCurrentPage());
            command = UPDATE;
            view = DETAIL;
            request.setAttribute(HEADING, getText("targetgroupdetail"));
        } else {
            request.setAttribute(HEADING, getText("targetgrouplist"));
            return LIST;
        }
        return view;
    }
*/
    /**
     * Delete.
     * @return the string
     * @throws Exception the exception
     */
    public void delete() throws Exception {

        if (!StringUtil.isEmpty(id)) {
            targetGroup = trainingService.findTargetGroupById(Long.valueOf(id));
            if (!ObjectUtil.isEmpty(targetGroup)) {
                List<FarmerTraining> farmerTrainingList = trainingService
                        .listFarmerTrainingByTargetGroup(targetGroup.getId());
                if (farmerTrainingList.size() > 0) {
                    getJsonObject().put("msg", getText("delete.targetGroup.warn"));
        	        getJsonObject().put("title", getText("title.error"));
                } else {
                    trainingService.removeTargetGroup(targetGroup);
                    getJsonObject().put("msg", getText("msg.deleted"));
        	        getJsonObject().put("title", getText("title.success"));
        	      
                }
            }
            sendAjaxResponse(getJsonObject());
        }

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
     * Sets the target group.
     * @param targetGroup the new target group
     */
    public void setTargetGroup(TargetGroup targetGroup) {

        this.targetGroup = targetGroup;
    }

    /**
     * Gets the target group.
     * @return the target group
     */
    public TargetGroup getTargetGroup() {

        return targetGroup;
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

	public String getTargetGroupName() {
		return targetGroupName;
	}

	public void setTargetGroupName(String targetGroupName) {
		this.targetGroupName = targetGroupName;
	}

}
