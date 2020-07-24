/*
 * FarmerTraining.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.entity.txn.training;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.sourcetrace.eses.util.ObjectUtil;

public class FarmerTraining {

    public final static int FIELD_STAFF = 0;
    public final static int ACTIVE = 1;
    public final static int INACTIVE = 2;

    private long id;
    private String code;
    private TrainingTopic trainingTopic;
    private Set<TargetGroup> targetGroups;
    private Set<Topic> topics;
    private Set<TrainingMethod> trainingMethods;
    private Set<Planner> planners;
    private int status;
    private long revisionNo;
    private String branchId;
    private Set<Observations>observations;
    private Set<TrainingMaterial>materials; 
    private Set<TopicCategory>topicCategories;
    private String selectionType;
    
    // Transient variables
    private String availableTargetId;
    private String selectedTargetId;
    private String selectedTopicId;
    private String availableMethodId;
    private String selectedMethodId;
    private String filterStatus;
	private List<String> branchesList;
	private String availableTrainingMaterialId;
	private String selectedTrainingMaterialId;
	private String availableObservationsId;
	private String selectedObservationsId;
	
	
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
     * Gets the training topic.
     * @return the training topic
     */
    public TrainingTopic getTrainingTopic() {

        return trainingTopic;
    }

    /**
     * Sets the training topic.
     * @param trainingTopic the new training topic
     */
    public void setTrainingTopic(TrainingTopic trainingTopic) {

        this.trainingTopic = trainingTopic;
    }

    /**
     * Sets the target groups.
     * @param targetGroups the targetGroups to set
     */
    public void setTargetGroups(Set<TargetGroup> targetGroups) {

        this.targetGroups = targetGroups;
    }

    /**
     * Gets the target groups.
     * @return the targetGroups
     */
    public Set<TargetGroup> getTargetGroups() {

        return targetGroups;
    }

    /**
     * Gets the topics.
     * @return the topics
     */
    public Set<Topic> getTopics() {

        return topics;
    }

    /**
     * Sets the topics.
     * @param topics the new topics
     */
    public void setTopics(Set<Topic> topics) {

        this.topics = topics;
    }

    /**
     * Sets the training methods.
     * @param trainingMethods the new training methods
     */
    public void setTrainingMethods(Set<TrainingMethod> trainingMethods) {

        this.trainingMethods = trainingMethods;
    }

    /**
     * Gets the training methods.
     * @return the training methods
     */
    public Set<TrainingMethod> getTrainingMethods() {

        return trainingMethods;
    }

    /**
     * Gets the planners.
     * @return the planners
     */
    public Set<Planner> getPlanners() {

        return planners;
    }

    /**
     * Sets the planners.
     * @param planners the new planners
     */
    public void setPlanners(Set<Planner> planners) {

        this.planners = planners;
    }

    /**
     * Gets the target group set.
     * @return the target group set
     */
    public String getTargetGroupSet() {

        StringBuffer targetGroupName = new StringBuffer();
        if (!ObjectUtil.isListEmpty(targetGroups)) {
            Iterator itr = targetGroups.iterator();
            while (itr.hasNext()) {
                TargetGroup targetGroup = (TargetGroup) itr.next();
                targetGroupName.append(targetGroup.getName()).append(", ");
            }
        }

        if (targetGroupName.length() > 0) {
            return targetGroupName.substring(0, targetGroupName.length() - 2);
        }

        return "";
    }
    public String getTrainingMaterialSet() {

        StringBuffer materialName = new StringBuffer();
        if (!ObjectUtil.isListEmpty(materials)) {
            Iterator itr = materials.iterator();
            while (itr.hasNext()) {
                TrainingMaterial trainingMaterial = (TrainingMaterial) itr.next();
                materialName.append(trainingMaterial.getName()).append(", ");
            }
        }

        if (materialName.length() > 0) {
            return materialName.substring(0, materialName.length() - 2);
        }

        return "";
    }

    public String getTrainingObservationsSet() {

        StringBuffer obsName = new StringBuffer();
        if (!ObjectUtil.isListEmpty(observations)) {
            Iterator itr = observations.iterator();
            while (itr.hasNext()) {
                Observations observations = (Observations) itr.next();
                obsName.append(observations.getName()).append(", ");
            }
        }

        if (obsName.length() > 0) {
            return obsName.substring(0, obsName.length() - 2);
        }

        return "";
    }

    /**
     * Gets the training topic activity set.
     * @return the trainingTopicActivitySet
     */
    public String getTrainingTopicActivitySet() {

        StringBuffer trainingTopicCode = new StringBuffer();
        if (!ObjectUtil.isListEmpty(topics)) {
            Iterator itr = topics.iterator();
            while (itr.hasNext()) {
                Topic topic = (Topic) itr.next();
                trainingTopicCode.append(topic.getPrinciple()).append(", ");
            }
        }

        if (trainingTopicCode.length() > 0) {
            return trainingTopicCode.substring(0, trainingTopicCode.length() - 2);
        }

        return "";
    }

    /**
     * Gets the training method set.
     * @return the trainingMethodSet
     */
    public String getTrainingMethodSet() {

        StringBuffer trainingMethodName = new StringBuffer();
        if (!ObjectUtil.isListEmpty(trainingMethods)) {
            Iterator itr = trainingMethods.iterator();
            while (itr.hasNext()) {
                TrainingMethod trainingMethod = (TrainingMethod) itr.next();
                trainingMethodName.append(trainingMethod.getName()).append(", ");
            }
        }

        if (trainingMethodName.length() > 0) {
            return trainingMethodName.substring(0, trainingMethodName.length() - 2);
        }

        return "";
    }

    /**
     * Sets the available target id.
     * @param availableTargetId the availableTargetId to set
     */
    public void setAvailableTargetId(String availableTargetId) {

        this.availableTargetId = availableTargetId;
    }

    /**
     * Gets the available target id.
     * @return the availableTargetId
     */
    public String getAvailableTargetId() {

        return availableTargetId;
    }

    /**
     * Sets the selected target id.
     * @param selectedTargetId the selectedTargetId to set
     */
    public void setSelectedTargetId(String selectedTargetId) {

        this.selectedTargetId = selectedTargetId;
    }

    /**
     * Gets the selected target id.
     * @return the selectedTargetId
     */
    public String getSelectedTargetId() {

        return selectedTargetId;
    }

    /**
     * Sets the selected topic id.
     * @param selectedTopicId the selectedTopicId to set
     */
    public void setSelectedTopicId(String selectedTopicId) {

        this.selectedTopicId = selectedTopicId;
    }

    /**
     * Gets the selected topic id.
     * @return the selectedTopicId
     */
    public String getSelectedTopicId() {

        return selectedTopicId;
    }

    /**
     * Sets the available method id.
     * @param availableMethodId the availableMethodId to set
     */
    public void setAvailableMethodId(String availableMethodId) {

        this.availableMethodId = availableMethodId;
    }

    /**
     * Gets the available method id.
     * @return the availableMethodId
     */
    public String getAvailableMethodId() {

        return availableMethodId;
    }

    /**
     * Sets the selected method id.
     * @param selectedMethodId the selectedMethodId to set
     */
    public void setSelectedMethodId(String selectedMethodId) {

        this.selectedMethodId = selectedMethodId;
    }

    /**
     * Gets the selected method id.
     * @return the selectedMethodId
     */
    public String getSelectedMethodId() {

        return selectedMethodId;
    }

    /**
     * Sets the status.
     * @param status the new status
     */
    public void setStatus(int status) {

        this.status = status;
    }

    /**
     * Gets the status.
     * @return the status
     */
    public int getStatus() {

        return status;
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

    /**
     * Sets the filter status.
     * @param filterStatus the new filter status
     */
    public void setFilterStatus(String filterStatus) {

        this.filterStatus = filterStatus;
    }

    public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	/**
     * Gets the filter status.
     * @return the filter status
     */
    public String getFilterStatus() {

        return filterStatus;
    }

	public void setBranchesList(List<String> branchesList) {
		// TODO Auto-generated method stub
		 this.branchesList = branchesList;
	}

	public Set<Observations> getObservations() {
		return observations;
	}

	public void setObservations(Set<Observations> observations) {
		this.observations = observations;
	}

	public String getAvailableTrainingMaterialId() {
		return availableTrainingMaterialId;
	}

	public void setAvailableTrainingMaterialId(String availableTrainingMaterialId) {
		this.availableTrainingMaterialId = availableTrainingMaterialId;
	}

	public String getSelectedTrainingMaterialId() {
		return selectedTrainingMaterialId;
	}

	public void setSelectedTrainingMaterialId(String selectedTrainingMaterialId) {
		this.selectedTrainingMaterialId = selectedTrainingMaterialId;
	}

	public Set<TrainingMaterial> getMaterials() {
		return materials;
	}

	public void setMaterials(Set<TrainingMaterial> materials) {
		this.materials = materials;
	}

	public String getAvailableObservationsId() {
		return availableObservationsId;
	}

	public void setAvailableObservationsId(String availableObservationsId) {
		this.availableObservationsId = availableObservationsId;
	}

	public String getSelectedObservationsId() {
		return selectedObservationsId;
	}

	public void setSelectedObservationsId(String selectedObservationsId) {
		this.selectedObservationsId = selectedObservationsId;
	}

	public Set<TopicCategory> getTopicCategories() {
		return topicCategories;
	}

	public void setTopicCategories(Set<TopicCategory> topicCategories) {
		this.topicCategories = topicCategories;
	}

	public List<String> getBranchesList() {
		return branchesList;
	}

	public String getSelectionType() {
		return selectionType;
	}

	public void setSelectionType(String selectionType) {
		this.selectionType = selectionType;
	}

}
