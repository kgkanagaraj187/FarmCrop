/*
 * TrainingMethodAction.java
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
import com.ese.entity.txn.training.TrainingMethod;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.service.ITrainingService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

public class TrainingMethodAction extends SwitchValidatorAction {

    private static final long serialVersionUID = -7330130379881963430L;

    private String id;
    private TrainingMethod trainingMethod;
    private ITrainingService trainingService;
    private String methodName;
    @Autowired
    private IUniqueIDGenerator idGenerator;

    /**
     * Gets the data.
     * @return the data
     * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
     */
    @Override
    public Object getData() {

        return trainingMethod;
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

        TrainingMethod filter = new TrainingMethod();
        
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

        if (!StringUtil.isEmpty(searchRecord.get("methodName"))) {
            filter.setName(searchRecord.get("methodName").trim());
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

        TrainingMethod trainingMethod = (TrainingMethod) obj;
        JSONObject jsonObject = new JSONObject();
        JSONArray rows = new JSONArray();
        if ((getIsMultiBranch().equalsIgnoreCase("1") && (getIsParentBranch().equals("1")||StringUtil.isEmpty(branchIdValue)))) {

            if (StringUtil.isEmpty(branchIdValue)) {
                rows.add(!StringUtil.isEmpty(
                        getBranchesMap().get(getParentBranchMap().get(trainingMethod.getBranchId())))
                                ? getBranchesMap()
                                        .get(getParentBranchMap().get(trainingMethod.getBranchId()))
                                : getBranchesMap().get(trainingMethod.getBranchId()));
            }
            rows.add(getBranchesMap().get(trainingMethod.getBranchId()));

        } else {
            if (StringUtil.isEmpty(branchIdValue)) {
                rows.add(branchesMap.get(trainingMethod.getBranchId()));
            }
        }
        rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">" + trainingMethod.getCode()
                + "</font>");
        rows.add(trainingMethod.getName());
        jsonObject.put("id", trainingMethod.getId());
        jsonObject.put("cell", rows);
        return jsonObject;
    }

    /**
     * Creates the.
     * @return the string
     * @throws Exception the exception
     */
    public void create() throws Exception
    {
    		if(!StringUtil.isEmpty(methodName))
    		{
    			trainingMethod=new TrainingMethod();
    			trainingMethod.setName(methodName);
	        	trainingMethod.setCode(idGenerator.getTrainingMethodIdSeq());
	        	trainingMethod.setBranchId(getBranchId());
	            trainingService.addTrainingMethod(trainingMethod);
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

    	if (!StringUtil.isEmpty(methodName))
        {
                TrainingMethod temp = trainingService
                        .findTrainingMethodById(Long.valueOf(id));
              
                temp.setName(methodName);
                // temp.setCode(country.getCode());
            trainingService.editTrainingMethod(temp);
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
            trainingMethod = trainingService.findTrainingMethodById(Long.valueOf(id));
            if (trainingMethod == null) {
                addActionError(NO_RECORD);
                return REDIRECT;
            }
            setCurrentPage(getCurrentPage());
            command = UPDATE;
            view = DETAIL;
            request.setAttribute(HEADING, getText("trainingmethoddetail"));
        } else {
            request.setAttribute(HEADING, getText("trainingmethodlist"));
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
            trainingMethod = trainingService.findTrainingMethodById(Long.valueOf(id));
            if (!ObjectUtil.isEmpty(trainingMethod)) {
                List<FarmerTraining> farmerTrainingList = trainingService
                        .listFarmerTrainingByTrainingMethod(trainingMethod.getId());
                if (farmerTrainingList.size() > 0) {
                    getJsonObject().put("msg", getText("delete.trainingMethod.warn"));
        	        getJsonObject().put("title", getText("title.error"));
        	      
                } else {
                    trainingService.removeTrainingMethod(trainingMethod);
                    getJsonObject().put("msg", getText("msg.deleted"));
        	        getJsonObject().put("title", getText("title.success"));
                }
            }
            sendAjaxResponse(getJsonObject());
        }
        
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

    /**
     * Gets the training method.
     * @return the training method
     */
    public TrainingMethod getTrainingMethod() {

        return trainingMethod;
    }

    /**
     * Sets the training method.
     * @param trainingMethod the new training method
     */
    public void setTrainingMethod(TrainingMethod trainingMethod) {

        this.trainingMethod = trainingMethod;
    }

    /**
     * Gets the training service.
     * @return the training service
     */
    public ITrainingService getTrainingService() {

        return trainingService;
    }

    /**
     * Sets the training service.
     * @param trainingService the new training service
     */
    public void setTrainingService(ITrainingService trainingService) {

        this.trainingService = trainingService;
    }

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

}
