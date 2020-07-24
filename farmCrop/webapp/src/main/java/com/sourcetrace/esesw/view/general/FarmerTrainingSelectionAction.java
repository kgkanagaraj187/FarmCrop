/*
 * FarmerTrainingSelectionAction.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view.general;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.ese.entity.txn.training.FarmerTraining;
import com.ese.entity.txn.training.Observations;
import com.ese.entity.txn.training.TargetGroup;
import com.ese.entity.txn.training.Topic;
import com.ese.entity.txn.training.TopicCategory;
import com.ese.entity.txn.training.TrainingMaterial;
import com.ese.entity.txn.training.TrainingMethod;
import com.ese.entity.txn.training.TrainingTopic;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.ITrainingService;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

public class FarmerTrainingSelectionAction extends SwitchValidatorAction {

    private static final long serialVersionUID = -7330130379881963430L;
    private FarmerTraining farmerTraining;
    private ITrainingService trainingService;
   // private String selectedFarmerTrainingType;
    List<TrainingTopic> trainingTopics = new ArrayList<TrainingTopic>();
    List<TargetGroup> targetGroups = new ArrayList<TargetGroup>();
    List<Topic> topicList = new ArrayList<Topic>();
    List<TrainingMethod> trainingMethodList = new ArrayList<TrainingMethod>();

    private List<TargetGroup> availableTargetGroups = new ArrayList<TargetGroup>();
    private List<TargetGroup> selectedTargetGroups = new ArrayList<TargetGroup>();
    private List<TrainingMethod> availableTrainingMethods = new ArrayList<TrainingMethod>();
    private List<TrainingMethod> selectedTrainingMethods = new ArrayList<TrainingMethod>();

    Map<Integer, String> farmerTrainingStatus = new LinkedHashMap<Integer, String>();
    Map<Integer, String> farmerTrainingType = new LinkedHashMap<Integer, String>();
    
    private List<TrainingMaterial>selectedTrainingMaterials=new ArrayList<TrainingMaterial>();		
    private List<TrainingMaterial>availableTrainingMaterials=new ArrayList<TrainingMaterial>();		
    private List<Observations>selectedTrainingObservations=new ArrayList<Observations>();		
    private List<Observations>availableTrainingObservations=new ArrayList<Observations>();	
    private Map<Long,TrainingMaterial> tempSelectTrainingMaterial=new TreeMap<Long,TrainingMaterial>();
    private Set<TopicCategory>topicCategories=new HashSet<>();

    private String id;
    private ICatalogueService catalogueService;

  

	/**
     * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
     */
    @Override
    public Object getData() {

        if (!ObjectUtil.isEmpty(farmerTraining)) {
            // Resetting available Target
            if (!StringUtil.isEmpty(farmerTraining.getAvailableTargetId())) {
                String[] targetIds = farmerTraining.getAvailableTargetId().split("\\,");
                for (String targetId : targetIds) {
                    TargetGroup targetGroup = trainingService.findTargetGroupById(Long
                            .parseLong(targetId.trim()));
                    if (!ObjectUtil.isEmpty(targetGroup)) {
                        availableTargetGroups.add(targetGroup);
                    }
                }
            }
            // Resetting selected Target
            if (!StringUtil.isEmpty(farmerTraining.getSelectedTargetId())) {
                String[] targetIds = farmerTraining.getSelectedTargetId().split("\\,");
                for (String targetId : targetIds) {
                    TargetGroup targetGroup = trainingService.findTargetGroupById(Long
                            .parseLong(targetId.trim()));
                    if (!ObjectUtil.isEmpty(targetGroup)) {
                        selectedTargetGroups.add(targetGroup);
                    }
                }
            }

            // Resetting available Method
            if (!StringUtil.isEmpty(farmerTraining.getAvailableMethodId())) {
                String[] methodIds = farmerTraining.getAvailableMethodId().split("\\,");
                for (String methodId : methodIds) {
                    TrainingMethod trainingMethod = trainingService.findTrainingMethodById(Long
                            .parseLong(methodId.trim()));
                    if (!ObjectUtil.isEmpty(trainingMethod)) {
                        availableTrainingMethods.add(trainingMethod);
                    }
                }
            }
            // Resetting selected Method
            if (!StringUtil.isEmpty(farmerTraining.getSelectedMethodId())) {
                String[] methodIds = farmerTraining.getSelectedMethodId().split("\\,");
                for (String methodId : methodIds) {
                    TrainingMethod trainingMethod = trainingService.findTrainingMethodById(Long
                            .parseLong(methodId.trim()));
                    if (!ObjectUtil.isEmpty(trainingMethod)) {
                        selectedTrainingMethods.add(trainingMethod);
                    }
                }
            }
            // Resetting selected material		
            if (!StringUtil.isEmpty(farmerTraining.getAvailableTrainingMaterialId())) {		
                String[] materialsId = farmerTraining.getAvailableTrainingMaterialId().split("\\,");		
                for (String materialId : materialsId) {		
                    TrainingMaterial trainingMaterial = trainingService.findTrainingMaterial(Long		
                            .parseLong(materialId.trim()));		
                    if (!ObjectUtil.isEmpty(trainingMaterial)) {		
                        availableTrainingMaterials.add(trainingMaterial);		
                    }		
                }		
            }		
            // Resetting selected material		
            if (!StringUtil.isEmpty(farmerTraining.getSelectedTrainingMaterialId())) {		
                String[] materialsId = farmerTraining.getSelectedTrainingMaterialId().split("\\,");		
                for (String materialId : materialsId) {		
                	TrainingMaterial trainingMaterial = trainingService.findTrainingMaterial(Long		
                            .parseLong(materialId.trim()));		
                    if (!ObjectUtil.isEmpty(trainingMaterial)) {		
                        selectedTrainingMaterials.add(trainingMaterial);		
                    }		
                }		
            }		
            		
            // Resetting selected observations		
            if (!StringUtil.isEmpty(farmerTraining.getAvailableObservationsId())) {		
                String[] obsId = farmerTraining.getAvailableObservationsId().split("\\,");		
                for (String observationsId : obsId) {		
                    Observations observations = trainingService.findObservationsById(Long		
                            .parseLong(observationsId.trim()));		
                    if (!ObjectUtil.isEmpty(observations)) {		
                        availableTrainingObservations.add(observations);		
                    }		
                }		
            }		
            // Resetting selected observations		
            if (!StringUtil.isEmpty(farmerTraining.getSelectedObservationsId())) {		
                String[] obsId = farmerTraining.getSelectedObservationsId().split("\\,");		
                for (String observationId : obsId) {		
                	Observations observations = trainingService.findObservationsById(Long		
                            .parseLong(observationId.trim()));		
                    if (!ObjectUtil.isEmpty(observations)) {		
                        selectedTrainingObservations.add(observations);		
                    }		
                }		
            }		
            
        }

        farmerTrainingStatus.put(FarmerTraining.ACTIVE, getLocaleProperty("statusValue1"));
        farmerTrainingStatus.put(FarmerTraining.INACTIVE, getLocaleProperty("statusValue2"));
     //   farmerTrainingType.put(FarmerTraining.FIELD_STAFF, getText("status0"));
      //  farmerTrainingType.put(FarmerTraining.ACTIVE, getText("status1"));

        return farmerTraining;
    }

    /**
     * Data.
     * @return the string
     * @throws Exception the exception
     * @see com.sourcetrace.esesw.view.SwitchAction#data()
     */
    @SuppressWarnings("unchecked")
    public String data() throws Exception {

        Map<String, String> searchRecord = getJQGridRequestParam(); // get the search parameter with
        // value

        FarmerTraining filter = new FarmerTraining();

        if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {		
			if (!getIsMultiBranch().equalsIgnoreCase("1")) {		
				List<String> branchList = new ArrayList<>();		
				branchList.add(searchRecord.get("branchId").trim());		
				filter.setBranchesList(branchList);		
			} else {		
				List<String> branchList = new ArrayList<>();		
				List<BranchMaster> branches = clientService.listChildBranchIds(searchRecord.get("branchId").trim());		
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

        if (!StringUtil.isEmpty(searchRecord.get("tt.name"))) {
            TrainingTopic trainingTopic = new TrainingTopic();
            trainingTopic.setName(searchRecord.get("tt.name").trim());
            filter.setTrainingTopic(trainingTopic);
        }

      /*if (!StringUtil.isEmpty(searchRecord.get("status"))) {
            if ("1".equals(searchRecord.get("status"))) {
                filter.setFilterStatus("status");
                filter.setStatus(FarmerTraining.ACTIVE);

            } else if ("0".equals(searchRecord.get("status"))) {
                filter.setFilterStatus("status");
                filter.setStatus(FarmerTraining.FIELD_STAFF);
            }
        }
        */
        if (!StringUtil.isEmpty(searchRecord.get("status"))) {

			filter.setStatus(Integer.valueOf(searchRecord.get("status")));
		}
        
        if (!StringUtil.isEmpty(searchRecord.get("selectionType"))) {

			filter.setSelectionType(searchRecord.get("selectionType"));
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
    @SuppressWarnings("unchecked")
    public JSONObject toJSON(Object obj) {

        FarmerTraining farmerTraining = (FarmerTraining) obj;
        JSONObject jsonObject = new JSONObject();
        JSONArray rows = new JSONArray();
        if ((getIsMultiBranch().equalsIgnoreCase("1") && (getIsParentBranch().equals("1")||StringUtil.isEmpty(branchIdValue)))) {		        if (StringUtil.isEmpty(branchIdValue)) {
  			rows.add(branchesMap.get(farmerTraining.getBranchId()));
      if (StringUtil.isEmpty(branchIdValue)) {		  		}
			rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(farmerTraining.getBranchId())))		
					? getBranchesMap().get(getParentBranchMap().get(farmerTraining.getBranchId()))		
					: getBranchesMap().get(farmerTraining.getBranchId()));		
		}		
		rows.add(getBranchesMap().get(farmerTraining.getBranchId()));		
 } else {		
     if (StringUtil.isEmpty(branchIdValue)) {		
         rows.add(branchesMap.get(farmerTraining.getBranchId()));		
     }		
 }
        rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">" + farmerTraining.getCode()
                + "</font>");
        rows.add(farmerTraining.getTrainingTopic() == null ? "  " : farmerTraining
                .getTrainingTopic().getName());
       /* rows.add(!StringUtil.isEmpty(farmerTraining.getSelectionType())
				? getCatlogueValueByCode(farmerTraining.getSelectionType()) : "");*/
	/*	if (farmerTraining.getSelectionType() != null && farmerTraining.getSelectionType().length() > 1) {
			FarmCatalogue catalogue = catalogueService
					.findCatalogueByCode(String.valueOf(farmerTraining.getSelectionType()));
			if (!ObjectUtil.isEmpty(catalogue)) {
				String name = catalogue != null ? catalogue.getName() : "";
				 rows.add(name);
			} else {
				 rows.add("");
			}
		}else{
			rows.add("");
		}*/
        rows.add(getText("statusValue" + String.valueOf(farmerTraining.getStatus())));
        jsonObject.put("id", farmerTraining.getId());
        jsonObject.put("cell", rows);
        return jsonObject;
    }

    /**
     * Creates the.
     * @return the string
     * @throws Exception the exception
     */
    public String create() throws Exception {

        if (farmerTraining == null) {
            command = CREATE;
            request.setAttribute(HEADING, getText(CREATE));
            availableTargetGroups = trainingService.listTargetGroup();
            availableTrainingMethods = trainingService.listTrainingMethod();
            availableTrainingMaterials=trainingService.listTrainingMaterials();		
            availableTrainingObservations=trainingService.listObservations();
            return INPUT;
        } else {
            // Training Topic
            TrainingTopic trainingTopic = trainingService.findTrainingTopicByName(farmerTraining
                    .getTrainingTopic().getName());
            farmerTraining.setTrainingTopic(trainingTopic);
            // Setting new Topic,Target Group and Method
            farmerTraining.setTopics(buildTopicSet(farmerTraining.getSelectedTopicId()));
            Set<Long>topicCategoryId=new HashSet<Long>();		
    		
            farmerTraining.getTopics().forEach(a->topicCategoryId.add(a.getTopicCategory().getId()));		
            topicCategoryId.forEach(tId->topicCategories.add(trainingService.findTopicCategoryById(tId)));		
          //  farmerTraining.getTopics().forEach(topic->topicCategories.add(topic.getTopicCategory()));		
            //topicCategories = farmerTraining.getTopics().stream().filter(topic->topic.getTopicCategory()).collect(Collectors.toSet());		
           // topicCategories=farmerTraining.getTopics().stream().filter(topic->!topicCategories.contains(topic.getTopicCategory().getId())).collect(Collectors.toSet());		
            System.out.println(topicCategoryId);		
            farmerTraining.setTopicCategories(topicCategories);		
         //   farmerTraining.setTopicCategories(farmerTraining.getTopics().);
            farmerTraining.setTargetGroups(new HashSet<TargetGroup>(selectedTargetGroups));
            farmerTraining.setTrainingMethods(new HashSet<TrainingMethod>(selectedTrainingMethods));
            farmerTraining.setBranchId(getBranchId());
            farmerTraining.setMaterials(new HashSet<TrainingMaterial>(selectedTrainingMaterials));		
            farmerTraining.setObservations(new HashSet<Observations>(selectedTrainingObservations));
            // Saving Farmer Training
            trainingService.addFarmerTraining(farmerTraining);
            return REDIRECT;
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
            farmerTraining = trainingService.findFarmerTrainingById(Long.valueOf(id));
            FarmCatalogue catalogue = catalogueService
					.findCatalogueByCode(String.valueOf(farmerTraining.getSelectionType()));
			if (!ObjectUtil.isEmpty(catalogue)) {
				String name = catalogue != null ? catalogue.getName() : "";
          /*  farmerTraining.setSelectionType(!StringUtil.isEmpty(farmerTraining.getSelectionType())
					? getCatlogueValueByCode(farmerTraining.getSelectionType()) : "");*/
			farmerTraining.setSelectionType(!StringUtil.isEmpty(name)?name:"NA");
			}
            if (farmerTraining == null) {
                addActionError(NO_RECORD);
                return REDIRECT;
            }
            command = UPDATE;
            view = DETAIL;
            request.setAttribute(HEADING, getText(DETAIL));
        } else {
            request.setAttribute(HEADING, getText(LIST));
            return LIST;
        }
        return view;
    }

    /**
     * Delete.
     * @return the string
     * @throws Exception the exception
     */
    public String delete() throws Exception {

        if (this.getId() != null && !(this.getId().equals(EMPTY))) {
            farmerTraining = trainingService.findFarmerTrainingById(Long.valueOf(id));
            if (farmerTraining == null) {
                addActionError(NO_RECORD);
                return list();
            }
            setCurrentPage(getCurrentPage());
            if (farmerTraining != null && !ObjectUtil.isListEmpty(farmerTraining.getPlanners())) {
                addActionError(getText("delete.trainingSelection.warn"));
                return detail();
            } else
                trainingService.removeFarmerTraining(farmerTraining);
        }

        request.setAttribute(HEADING, getText(LIST));
        return LIST;

    }

    /**
     * Update.
     * @return the string
     * @throws Exception the exception
     */
    public String update() throws Exception {

        if (id != null && !id.equals("")) {
            farmerTraining = trainingService.findFarmerTrainingById(Long.valueOf(id));
           /* if (farmerTraining.getStatus() == FarmerTraining.FIELD_STAFF)
                setSelectedFarmerTrainingType(String.valueOf(FarmerTraining.FIELD_STAFF));
            else
                setSelectedFarmerTrainingType(String.valueOf(FarmerTraining.ACTIVE));*/
            if (farmerTraining == null) {
                addActionError(NO_RECORD);
                return REDIRECT;
            }
            farmerTraining.setSelectedTopicId(farmerTraining.getTopics().stream().map(topic->String.valueOf(topic.getId())).collect(Collectors.joining(",")));
            // Setting the values for Farmer Training
            formTransientValuesForFarmerTraining(farmerTraining);
            setCurrentPage(getCurrentPage());
            id = null;
            command = UPDATE;
            request.setAttribute(HEADING, getText(UPDATE));
        } else {
            if (farmerTraining != null) {
                FarmerTraining temp = trainingService
                        .findFarmerTrainingById(farmerTraining.getId());
                // Setting TrainingTopic
                TrainingTopic trainingTopic = trainingService
                        .findTrainingTopicByName(farmerTraining.getTrainingTopic().getName());
                temp.setTrainingTopic(trainingTopic);
                // Remove already existing and add new Topic,Target Group and Method
                temp.setTopics(buildTopicSet(farmerTraining.getSelectedTopicId()));
                temp.setTargetGroups(new HashSet<TargetGroup>(selectedTargetGroups));
                temp.setTrainingMethods(new HashSet<TrainingMethod>(selectedTrainingMethods));
                temp.setMaterials(new HashSet<TrainingMaterial>(selectedTrainingMaterials));		
                temp.setObservations(new HashSet<Observations>(selectedTrainingObservations));
                temp.setStatus(farmerTraining.getStatus());
                temp.setSelectionType(farmerTraining.getSelectionType());
                Set<Long>topicCategoryId=new HashSet<Long>();
                topicCategories=new HashSet<>();
                temp.getTopics().forEach(a->topicCategoryId.add(a.getTopicCategory().getId()));	
                topicCategoryId.forEach(tId->topicCategories.add(trainingService.findTopicCategoryById(tId)));	
                temp.setTopicCategories(topicCategories);	
                trainingService.editFarmerTraining(temp);
                
            }
            request.setAttribute(HEADING, getText(LIST));
            return LIST;
        }
        return super.execute();

    }

    /**
     * Form topic set.
     * @param selectedTopic the selected topic
     * @return the set< topic>
     */
    public Set<Topic> buildTopicSet(String selectedTopic) {

        Set<Topic> topics = new HashSet<Topic>();
        String[] topicIds = selectedTopic.split("\\,");
        for (String topicId : topicIds) {
            if (StringUtil.isLong(topicId.trim())) {
                Topic topic = trainingService.findTopicById(Long.parseLong(topicId.trim()));
                if (!ObjectUtil.isEmpty(topic)) {
                    topics.add(topic);
                }
            }
        }
        return topics;
    }

    /**
     * Form transient values for farmer training.
     * @param farmerTraining the farmer training
     * @return the farmer training
     */
    public FarmerTraining formTransientValuesForFarmerTraining(FarmerTraining farmerTraining) {
        List<String> selectedTrainingObjList=new ArrayList();

        // Forming Topic values
        String topicValue = "";
        for (Topic topic : farmerTraining.getTopics()) {
            topicValue = topicValue + String.valueOf(topic.getId()) + ",";
        }
        farmerTraining.setSelectedTopicId(topicValue);
        // Forming Selected and Availabe Target Groups
        for (TargetGroup selectedGroup : farmerTraining.getTargetGroups()) {
            selectedTargetGroups.add(selectedGroup);
        }
        for (TargetGroup availableGroup : trainingService.listTargetGroup()) {
            if (!selectedTargetGroups.contains(availableGroup))
                availableTargetGroups.add(availableGroup);
        }
        // Forming Selected and Available Training Methods
        for (TrainingMethod selectedMethod : farmerTraining.getTrainingMethods()) {
            selectedTrainingMethods.add(selectedMethod);
        }
        for (TrainingMethod availableMethod : trainingService.listTrainingMethod()) {
            if (!selectedTrainingMethods.contains(availableMethod))
                availableTrainingMethods.add(availableMethod);
        }
        // Forming Selected and Available Training Materials		
        for (TrainingMaterial selectedMaterial : farmerTraining.getMaterials()) {		
            selectedTrainingMaterials.add(selectedMaterial);
            tempSelectTrainingMaterial.put(selectedMaterial.getId(), selectedMaterial);
        }		
        for (TrainingMaterial availbleMaterial : trainingService.listTrainingMaterials()) {		
            if (ObjectUtil.isEmpty(tempSelectTrainingMaterial.get(availbleMaterial.getId())))		
                availableTrainingMaterials.add(availbleMaterial);		
        }		
       		
        // Forming Selected and Available Training Observations		
        for (Observations selectedObs : farmerTraining.getObservations()) {		
        	selectedTrainingObjList.add(selectedObs.getName());	
        	selectedTrainingObservations.add(selectedObs);
        }		
        for (Observations selectedObs : trainingService.listObservations()) {		
            if (!selectedTrainingObjList.contains(selectedObs.getName()))		
                availableTrainingObservations.add(selectedObs);		
        }
        return farmerTraining;
    }

    /**
     * Sets the farmer training.
     * @param farmerTraining the new farmer training
     */
    public void setFarmerTraining(FarmerTraining farmerTraining) {

        this.farmerTraining = farmerTraining;
    }

    /**
     * Gets the farmer training.
     * @return the farmer training
     */
    public FarmerTraining getFarmerTraining() {

        return farmerTraining;
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
     * Gets the training topics.
     * @return the training topics
     */
    public List<TrainingTopic> getTrainingTopics() {

        return trainingService.listTrainingTopic();
    }

    /**
     * Sets the training topics.
     * @param trainingTopics the new training topics
     */
    public void setTrainingTopics(List<TrainingTopic> trainingTopics) {

        this.trainingTopics = trainingTopics;
    }

    /**
     * Gets the target groups.
     * @return the target groups
     */
    public List<TargetGroup> getTargetGroups() {

        return trainingService.listTargetGroup();
    }

    /**
     * Sets the target groups.
     * @param targetGroups the new target groups
     */
    public void setTargetGroups(List<TargetGroup> targetGroups) {

        this.targetGroups = targetGroups;
    }

    /**
     * Gets the topic list.
     * @return the topic list
     */
    public List<Topic> getTopicList() {

        return trainingService.listTopic();
    }

    /**
     * Sets the topic list.
     * @param topicList the new topic list
     */
    public void setTopicList(List<Topic> topicList) {

        this.topicList = topicList;
    }

    /**
     * Gets the training method list.
     * @return the training method list
     */
    public List<TrainingMethod> getTrainingMethodList() {

        return trainingService.listTrainingMethod();
    }

    /**
     * Sets the training method list.
     * @param trainingMethodList the new training method list
     */
    public void setTrainingMethodList(List<TrainingMethod> trainingMethodList) {

        this.trainingMethodList = trainingMethodList;
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
     * Gets the available target groups.
     * @return the available target groups
     */
    public List<TargetGroup> getAvailableTargetGroups() {

        return availableTargetGroups;
    }

    /**
     * Sets the available target groups.
     * @param availableTargetGroups the new available target groups
     */
    public void setAvailableTargetGroups(List<TargetGroup> availableTargetGroups) {

        this.availableTargetGroups = availableTargetGroups;
    }

    /**
     * Gets the selected target groups.
     * @return the selected target groups
     */
    public List<TargetGroup> getSelectedTargetGroups() {

        return selectedTargetGroups;
    }

    /**
     * Sets the selected target groups.
     * @param selectedTargetGroups the new selected target groups
     */
    public void setSelectedTargetGroups(List<TargetGroup> selectedTargetGroups) {

        this.selectedTargetGroups = selectedTargetGroups;
    }

    /**
     * Gets the available training methods.
     * @return the available training methods
     */
    public List<TrainingMethod> getAvailableTrainingMethods() {

        return availableTrainingMethods;
    }

    /**
     * Sets the available training methods.
     * @param availableTrainingMethods the new available training methods
     */
    public void setAvailableTrainingMethods(List<TrainingMethod> availableTrainingMethods) {

        this.availableTrainingMethods = availableTrainingMethods;
    }

    /**
     * Gets the selected training methods.
     * @return the selected training methods
     */
    public List<TrainingMethod> getSelectedTrainingMethods() {

        return selectedTrainingMethods;
    }

    /**
     * Sets the selected training methods.
     * @param selectedTrainingMethods the new selected training methods
     */
    public void setSelectedTrainingMethods(List<TrainingMethod> selectedTrainingMethods) {

        this.selectedTrainingMethods = selectedTrainingMethods;
    }

    /**
     * Gets the farmer training status.
     * @return the farmer training status
     */
    public Map<Integer, String> getFarmerTrainingStatus() {

        return farmerTrainingStatus;
    }

    /**
     * Sets the farmer training status.
     * @param farmerTrainingStatus the farmer training status
     */
    public void setFarmerTrainingStatus(Map<Integer, String> farmerTrainingStatus) {

        this.farmerTrainingStatus = farmerTrainingStatus;
    }

    /**
     * Gets the farmer training type.
     * @return the farmer training type
     */
    public Map<String, String> getFarmerTrainingType()
    {
    	List<FarmCatalogue> farmCatalougeList = catalogueService.findFarmCatalougeByType(Integer.valueOf(getText("trainingSelection").trim()));
		return farmCatalougeList.stream().collect(Collectors.toMap(FarmCatalogue::getCode, FarmCatalogue::getName));
    }

    /**
     * Sets the farmer training type.
     * @param farmerTrainingType the farmer training type
     */
    public void setFarmerTrainingType(Map<Integer, String> farmerTrainingType) {

        this.farmerTrainingType = farmerTrainingType;
    }

    /**
     * Sets the selected farmer training type.
     * @param selectedFarmerTrainingType the new selected farmer training type
     */
/*    public void setSelectedFarmerTrainingType(String selectedFarmerTrainingType) {

        this.selectedFarmerTrainingType = selectedFarmerTrainingType;
    }

    *//**
     * Gets the selected farmer training type.
     * @return the selected farmer training type
     *//*
    public String getSelectedFarmerTrainingType() {

        return selectedFarmerTrainingType;
    }*/
    public List<TrainingMaterial> getSelectedTrainingMaterials() {		
		return selectedTrainingMaterials;		
	}		
		
	public void setSelectedTrainingMaterials(List<TrainingMaterial> selectedTrainingMaterials) {		
		this.selectedTrainingMaterials = selectedTrainingMaterials;		
	}		
		
	public List<TrainingMaterial> getAvailableTrainingMaterials() {		
		return availableTrainingMaterials;		
	}		
		
	public void setAvailableTrainingMaterials(List<TrainingMaterial> availableTrainingMaterials) {		
		this.availableTrainingMaterials = availableTrainingMaterials;		
	}		
		
	public List<Observations> getSelectedTrainingObservations() {		
		return selectedTrainingObservations;		
	}		
		
	public void setSelectedTrainingObservations(List<Observations> selectedTrainingObservations) {		
		this.selectedTrainingObservations = selectedTrainingObservations;		
	}		
		
	public List<Observations> getAvailableTrainingObservations() {		
		return availableTrainingObservations;		
	}		
		
	public Set<TopicCategory> getTopicCategories() {		
		return topicCategories;		
	}		
		
	public void setTopicCategories(Set<TopicCategory> topicCategories) {		
		this.topicCategories = topicCategories;		
	}		
	
	
	public String getTrainingSelectionFilter() {

		StringBuffer season = new StringBuffer();
		season.append(":").append(FILTER_ALL).append(";");
		List<Object[]> farmCatalougeList = catalogueService.findCatalogueCodeAndNameByType(Integer.valueOf(getText("trainingSelection").trim()));
		for (Object[] obj : farmCatalougeList) {
			season.append(obj[0].toString()).append(":").append(obj[1].toString()).append(";");

		}
		String data = season.toString();
		return data.substring(0, data.length() - 1);

	}
	

	/*public String getCatlogueValueByCode(String code) {
		String catValue = "";
		Map<String, String> catalogueMap = new LinkedHashMap<>();
		if (catalogueMap.size() <= 0) {
			catalogueMap = catalogueService.listCatalogues().stream()
					.collect(Collectors.toMap(FarmCatalogue::getCode, FarmCatalogue::getName));
		}
		if (catalogueMap.containsKey(code)) {
			catValue = catalogueMap.get(code);
		}
		return catValue;
	}*/

	
	  public ICatalogueService getCatalogueService() {
			return catalogueService;
		}

		public void setCatalogueService(ICatalogueService catalogueService) {
			this.catalogueService = catalogueService;
		}

}
