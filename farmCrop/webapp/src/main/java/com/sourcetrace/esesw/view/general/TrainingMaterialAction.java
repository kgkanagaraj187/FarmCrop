package com.sourcetrace.esesw.view.general;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.txn.training.FarmerTraining;
import com.ese.entity.txn.training.Observations;
import com.ese.entity.txn.training.TrainingMaterial;
import com.sourcetrace.eses.service.ITrainingService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

public class TrainingMaterialAction extends SwitchValidatorAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6374597175972714350L;
	private static final Logger logger=Logger.getLogger(TrainingMaterialAction.class);
	private TrainingMaterial trainingMaterial;
	private ITrainingService trainingService;
	private String id;
	private String materialsName;
	
	@Autowired
    private IUniqueIDGenerator idGenerator;
	
	public String list() throws Exception {
		
		return LIST;
	}
	  public String data() throws Exception {

	        Map<String, String> searchRecord = getJQGridRequestParam(); // get the search parameter with
	        // value

	        TrainingMaterial filter = new TrainingMaterial();
	        
	        if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
	          /*  if (!getIsMultiBranch().equalsIgnoreCase("1")) {
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
	            }*/
	        	filter.setBranchId(searchRecord.get("branchId").trim());
	        }
	        
	        /*if (!StringUtil.isEmpty(searchRecord.get("subBranchId"))) {
	            filter.setBranchId(searchRecord.get("subBranchId").trim());
	        }*/

	        if (!StringUtil.isEmpty(searchRecord.get("code"))) {
	            filter.setCode(searchRecord.get("code").trim());
	        }

	        if (!StringUtil.isEmpty(searchRecord.get("materialsName"))) {
	            filter.setName(searchRecord.get("materialsName").trim());
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

	    	TrainingMaterial trainingMaterial = (TrainingMaterial) obj;
	        JSONObject jsonObject = new JSONObject();
	        JSONArray rows = new JSONArray();
	        /*if ((getIsMultiBranch().equalsIgnoreCase("1") && (getIsParentBranch().equals("1")||StringUtil.isEmpty(branchIdValue)))) {
	        	  if (StringUtil.isEmpty(branchIdValue)) {
						rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(trainingMethod.getBranchId())))
								? getBranchesMap().get(getParentBranchMap().get(trainingMethod.getBranchId()))
								: getBranchesMap().get(trainingMethod.getBranchId()));
					}
					rows.add(getBranchesMap().get(trainingMethod.getBranchId()));
		        
	        } else*/ 
	        if (StringUtil.isEmpty(branchIdValue)) {
	            rows.add(branchesMap.get(trainingMaterial.getBranchId()));
	        }
	    	
	        rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">" + trainingMaterial.getCode()
	                + "</font>");
	        rows.add(trainingMaterial.getName());
	        jsonObject.put("id", trainingMaterial.getId());
	        jsonObject.put("cell", rows);
	        return jsonObject;
	    }

	    /**
	     * Creates the.
	     * @return the string
	     * @throws Exception the exception
	     */
	    public void populateMaterialCreate()  {
	    	if (!StringUtil.isEmpty(materialsName)) {
				trainingMaterial = new TrainingMaterial();
				trainingMaterial.setName(materialsName);
				trainingMaterial.setCode(idGenerator.getTrainingMethodIdSeq());
				trainingMaterial.setBranchId(getBranchId());
				trainingMaterial.setRevisionNo(DateUtil.getRevisionNumber());
				trainingService.add(trainingMaterial);
			}
	    	
	    }
	    public void delete() throws Exception {
	    	 if (!StringUtil.isEmpty(id)) {
	    		 trainingMaterial = trainingService.findTrainingMaterial(Long.valueOf(id));
	    		 List<FarmerTraining> farmerTrainingList=trainingService
	                        .listFarmerTrainingByMaterialId(Long.valueOf(id));
	    		 if (farmerTrainingList.size() > 0) {
		                getJsonObject().put("msg", getText("trainingtopicdelete.warn"));
		                getJsonObject().put("title", getText("title.error"));
		            } else {
		                trainingService.delete(trainingMaterial);
		                getJsonObject().put("msg", getText("msg.deleted"));
		                getJsonObject().put("title", getText("title.success"));
		               
		            }
		        sendAjaxResponse(getJsonObject());
	    	 }
			
			 
			 
		    }

		 public void update() throws Exception {
			 if (id != null && !id.equals("")){
				 trainingMaterial = trainingService.findTrainingMaterial(Long.valueOf(id));
				 TrainingMaterial temp = trainingService.findTrainingMaterial(trainingMaterial.getId());
				 temp.setName(materialsName);
				 trainingService.update(temp);
				 getJsonObject().put("msg", getText("msg.updated"));
			        getJsonObject().put("title", getText("title.success"));
			        sendAjaxResponse(getJsonObject());
			 }
			
			 
		    }


	    /**
	     * Update.
	     * @return the string
	     * @throws Exception the exception
	     */
	  	@Override
	public Object getData() {
		// TODO Auto-generated method stub
		return trainingMaterial;
	}

	public TrainingMaterial getTrainingMaterial() {
		return trainingMaterial;
	}

	public void setTrainingMaterial(TrainingMaterial trainingMaterial) {
		this.trainingMaterial = trainingMaterial;
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
	public String getMaterialsName() {
		return materialsName;
	}
	public void setMaterialsName(String materialsName) {
		this.materialsName = materialsName;
	}


	

	
}
