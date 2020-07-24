package com.sourcetrace.esesw.view.general;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.txn.training.FarmerTraining;
import com.ese.entity.txn.training.Observations;
import com.ese.entity.txn.training.TrainingTopic;
import com.sourcetrace.eses.service.ITrainingService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

public class ObservationsAction extends SwitchValidatorAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6374597175972714350L;
	private static final Logger logger = Logger.getLogger(TrainingMaterialAction.class);
	private Observations observations;
	private ITrainingService trainingService;
	private String id;
	private String observationName;

	@Autowired
	private IUniqueIDGenerator idGenerator;

	public String list() throws Exception {

		return LIST;
	}

	public String data() throws Exception {

		Map<String, String> searchRecord = getJQGridRequestParam(); // get the
																	// search
																	// parameter
																	// with
		// value

		Observations filter = new Observations();

		if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
			/*
			 * if (!getIsMultiBranch().equalsIgnoreCase("1")) { List<String>
			 * branchList = new ArrayList<>();
			 * branchList.add(searchRecord.get("branchId").trim());
			 * filter.setBranchesList(branchList); } else { List<String>
			 * branchList = new ArrayList<>(); List<BranchMaster> branches =
			 * clientService
			 * .listChildBranchIds(searchRecord.get("branchId").trim());
			 * branchList.add(searchRecord.get("branchId").trim());
			 * branches.stream().filter(branch ->
			 * !StringUtil.isEmpty(branch)).forEach(branch -> {
			 * branchList.add(branch.getBranchId()); });
			 * filter.setBranchesList(branchList); }
			 */
			filter.setBranchId(searchRecord.get("branchId").trim());
		}

		/*
		 * if (!StringUtil.isEmpty(searchRecord.get("subBranchId"))) {
		 * filter.setBranchId(searchRecord.get("subBranchId").trim()); }
		 */

		if (!StringUtil.isEmpty(searchRecord.get("code"))) {
			filter.setCode(searchRecord.get("code").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("observationName"))) {
			filter.setName(searchRecord.get("observationName").trim());
		}

		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		return sendJQGridJSONResponse(data);
	}
	
	 
	/**
	 * To json.
	 * 
	 * @param obj
	 *            the obj
	 * @return the JSON object
	 * @see com.sourcetrace.esesw.view.SwitchAction#toJSON(java.lang.Object)
	 */
	public JSONObject toJSON(Object obj) {

		Observations observations = (Observations) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		/*
		 * if ((getIsMultiBranch().equalsIgnoreCase("1") &&
		 * (getIsParentBranch().equals("1")||StringUtil.isEmpty(branchIdValue)))
		 * ) { if (StringUtil.isEmpty(branchIdValue)) {
		 * rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap(
		 * ).get(trainingMethod.getBranchId()))) ?
		 * getBranchesMap().get(getParentBranchMap().get(trainingMethod.
		 * getBranchId())) :
		 * getBranchesMap().get(trainingMethod.getBranchId())); }
		 * rows.add(getBranchesMap().get(trainingMethod.getBranchId()));
		 * 
		 * } else
		 */
		if (StringUtil.isEmpty(branchIdValue)) {
			rows.add(branchesMap.get(observations.getBranchId()));
		}

		rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">" + observations.getCode() + "</font>");
		rows.add(observations.getName());
		jsonObject.put("id", observations.getId());
		jsonObject.put("cell", rows);
		return jsonObject;
	}

	public void populateObservationCreate() {
		if (!StringUtil.isEmpty(observationName)) {
			observations = new Observations();
			observations.setName(observationName);
			observations.setCode(idGenerator.getObservationsSeqId());
			observations.setBranchId(getBranchId());
			observations.setRevisionNo(DateUtil.getRevisionNumber());
			trainingService.add(observations);
		}
	}
	 public void delete() throws Exception {
		 
		 if (!StringUtil.isEmpty(id)) {
			 observations = trainingService.findObservationsById(Long.valueOf(id));
			 setCurrentPage(getCurrentPage());
			 boolean isTrainingTopicExistForFarmerSelection = trainingService.listFarmerTrainingByObservations(Long.valueOf(id));
			 if (isTrainingTopicExistForFarmerSelection) {
	                getJsonObject().put("msg", getText("trainingtopicdelete.warn"));
	                getJsonObject().put("title", getText("title.error"));
	            } else {
	                trainingService.delete(observations);
	                getJsonObject().put("msg", getText("msg.deleted"));
	                getJsonObject().put("title", getText("title.success"));
	               
	            }
	        sendAjaxResponse(getJsonObject());
		 }
		 
		 
	    }

	 public void update() throws Exception {

		
		 if (id != null && !id.equals("")){
			 observations = trainingService.findObservationsById(Long.valueOf(id));
			 Observations temp = trainingService.findObservationsById(observations.getId());
			 temp.setName(observationName);
			 trainingService.update(temp);
			 getJsonObject().put("msg", getText("msg.updated"));
		        getJsonObject().put("title", getText("title.success"));
		        sendAjaxResponse(getJsonObject());
		 }
	    }

	@Override
	public Object getData() {
		// TODO Auto-generated method stub
		return observations;
	}

	public Observations getObservations() {
		return observations;
	}

	public void setObservations(Observations observations) {
		this.observations = observations;
	}

	public ITrainingService getTrainingService() {
		return trainingService;
	}

	public void setTrainingService(ITrainingService trainingService) {
		this.trainingService = trainingService;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getObservationName() {
		return observationName;
	}

	public void setObservationName(String observationName) {
		this.observationName = observationName;
	}

}
