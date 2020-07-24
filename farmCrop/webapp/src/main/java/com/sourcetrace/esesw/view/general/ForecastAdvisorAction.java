package com.sourcetrace.esesw.view.general;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.ese.entity.profile.CropHarvest;
import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.dao.IProductDistributionDAO;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.order.entity.profile.Category;
import com.sourcetrace.eses.order.entity.profile.SubCategory;
import com.sourcetrace.eses.order.entity.txn.ForecastAdvisory;
import com.sourcetrace.eses.order.entity.txn.ForecastAdvisoryDetails;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Country;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

public class ForecastAdvisorAction extends SwitchValidatorAction{
	private IFarmerService farmerService;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IProductDistributionService productDistributionService;
	private IProductDistributionDAO productDistributionDAO;
	private ForecastAdvisory forecastAdvisory;
	private ForecastAdvisoryDetails foreCastAdvisoryDetails;
	private ProcurementProduct procurementProduct;
	private String productTotalString;
	private String description;
	private String selectedCrop;
	private String miniHumi;
	private String maxiHumi;
	private String miniWind;
	private String maxiWind;
	private String miniRain;
	private String maxiRain;
	private String miniTemp;
	private String maxiTemp;
	private String id;
	private List<ProcurementProduct> productList = new ArrayList<ProcurementProduct>();

	public String list() {
		if (getCurrentPage() != null) {
            setCurrentPage(getCurrentPage());
        }
        request.setAttribute(HEADING, getText("forecastAdvisorlist"));
		return LIST;
	}
	
	  @SuppressWarnings("unchecked")
	    public String data() throws Exception {


	        Map<String, String> searchRecord = getJQGridRequestParam();

	        ForecastAdvisoryDetails filter = new ForecastAdvisoryDetails();

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
	        
	      /*  if (!StringUtil.isEmpty(searchRecord.get("subBranchId"))) {
	            filter.setBranchId(searchRecord.get("subBranchId").trim());
	        }*/
	        if (!StringUtil.isEmpty(searchRecord.get("crop"))) {
	        	ProcurementProduct procurementProduct=new ProcurementProduct();
	        	procurementProduct.setId(Long.valueOf(searchRecord.get("code")));
	        }

	        if (!StringUtil.isEmpty(searchRecord.get("description"))) {
	        }

	       
	     
	               
	        Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(),
	                getResults(), filter, getPage());

	        return sendJQGridJSONResponse(data);

	    }
	   @SuppressWarnings("unchecked")
	    public JSONObject toJSON(Object obj) {

		   ForecastAdvisoryDetails foreCastAdvisoryDetails = (ForecastAdvisoryDetails) obj;
	      //  SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	        JSONObject jsonObject = new JSONObject();
	        JSONArray rows = new JSONArray();
	  	  	if ((getIsMultiBranch().equalsIgnoreCase("1") && (getIsParentBranch().equals("1")||StringUtil.isEmpty(branchIdValue)))) {

	          if (StringUtil.isEmpty(branchIdValue)) {
	              rows.add(!StringUtil.isEmpty(
	                      getBranchesMap().get(getParentBranchMap().get(foreCastAdvisoryDetails.getBranchId())))
	                              ? getBranchesMap()	
	                                      .get(getParentBranchMap().get(foreCastAdvisoryDetails.getBranchId()))
	                              : getBranchesMap().get(foreCastAdvisoryDetails.getBranchId()));
	          }
	          rows.add(getBranchesMap().get(foreCastAdvisoryDetails.getBranchId()));

	      } else {
	          if (StringUtil.isEmpty(branchIdValue)) {
	              rows.add(branchesMap.get(foreCastAdvisoryDetails.getBranchId()));
	          }
	      }
	        	rows.add(foreCastAdvisoryDetails.getForecastAdvisory().getProcurementProduct().getName());
	        	 rows.add(foreCastAdvisoryDetails.getForecastAdvisory().getDescription());
	        	 rows.add(foreCastAdvisoryDetails.getMinimumRain());
	        	 rows.add(foreCastAdvisoryDetails.getMaximumRain());
	        	 rows.add(foreCastAdvisoryDetails.getMinimumHumi());
	            rows.add(foreCastAdvisoryDetails.getMaximumHumi());
	            rows.add(foreCastAdvisoryDetails.getMinimumWind());
	            rows.add(foreCastAdvisoryDetails.getMaximumWind());
	            
	           
	            rows.add(foreCastAdvisoryDetails.getMinimumTemp()); 
	            rows.add(foreCastAdvisoryDetails.getMaximumTemp());
	        jsonObject.put("id", foreCastAdvisoryDetails.getId());
	        jsonObject.put("cell", rows);
	        return jsonObject;
	    }
	
	
	public String create() throws Exception {
		 if (id == null) {
	            command = "create";
	            request.setAttribute(HEADING, getText(CREATE));        
	            return INPUT;
	        }
		return REDIRECT; 
	}
	
	@SuppressWarnings("unchecked")
	public String populateForeCastCreate() throws IOException
	        {  
		String result="";
		ForecastAdvisory foreCastAdvisory=new ForecastAdvisory();
		ProcurementProduct procurementProduct = productDistributionService.findProcurementProductById(Integer.valueOf(selectedCrop));
		foreCastAdvisory.setProcurementProduct(procurementProduct);
		foreCastAdvisory.setDescription(description);
		farmerService.save(foreCastAdvisory);
		
		ForecastAdvisoryDetails foreCastAdvisoryDetails =new ForecastAdvisoryDetails();
	        	
	        	foreCastAdvisoryDetails.setMaximumHumi(maxiHumi);
	        	foreCastAdvisoryDetails.setMinimumHumi(miniHumi);
	        	foreCastAdvisoryDetails.setMaximumRain(maxiRain);
	        	foreCastAdvisoryDetails.setMinimumRain(miniRain);
	        	foreCastAdvisoryDetails.setMaximumWind(maxiWind);
	        	foreCastAdvisoryDetails.setMinimumWind(miniWind);
	        	foreCastAdvisoryDetails.setMaximumTemp(maxiTemp);
	        	foreCastAdvisoryDetails.setMinimumTemp(miniTemp);
	        	foreCastAdvisoryDetails.setForecastAdvisory(foreCastAdvisory);
	        	farmerService.save(foreCastAdvisoryDetails);
	        	result = "redirect";
	        	return result;
	}
	public String update() throws Exception {
		if (id != null && !id.equals("")) {
			foreCastAdvisoryDetails = farmerService.findforeCastAdvisoryDetailsById(Long.valueOf(id));
            if (foreCastAdvisoryDetails == null) {
                addActionError(NO_RECORD);
                return REDIRECT;
            }
            setCurrentPage(getCurrentPage());
           
            id = null;
            command = UPDATE;
            request.setAttribute(HEADING, getText("forecastAdvisorupdate"));
        }
		else{
			if(foreCastAdvisoryDetails == null){
			
	
				
				
					addActionError(NO_RECORD);
					return list();
					// return REDIRECT;
				}
				else{
					ForecastAdvisoryDetails fore= farmerService.findforeCastAdvisoryDetailsById(foreCastAdvisoryDetails.getId());
					ForecastAdvisory temp=farmerService.findforeCastAdvisoryById(fore.getForecastAdvisory().getId());
					//
					//temp.setProcurementProduct(procurementProduct);
					 setCurrentPage(getCurrentPage());
					 ProcurementProduct procurementProduct = productDistributionService.findProcurementProductById(foreCastAdvisoryDetails.getForecastAdvisory().getProcurementProduct().getId());
					 temp.setProcurementProduct(procurementProduct);
				   temp.setDescription(foreCastAdvisoryDetails.getForecastAdvisory().getDescription());
				   farmerService.update(temp);
				 //  ForecastAdvisoryDetails temp1;
				   
				   fore.setMaximumHumi(foreCastAdvisoryDetails.getMaximumHumi());
				   fore.setMinimumHumi(foreCastAdvisoryDetails.getMinimumHumi());
				   fore.setMaximumWind(foreCastAdvisoryDetails.getMaximumWind());
				   fore.setMinimumWind(foreCastAdvisoryDetails.getMinimumWind());
				   fore.setMaximumTemp(foreCastAdvisoryDetails.getMaximumTemp());
				   fore.setMinimumTemp(foreCastAdvisoryDetails.getMinimumTemp());
				   fore.setMaximumRain(foreCastAdvisoryDetails.getMaximumRain());
				   fore.setMinimumRain(foreCastAdvisoryDetails.getMinimumRain());
				   farmerService.update(fore);
				   return list();
				}
		}
		 return super.execute();
	}
	
	public String detail() throws Exception {

        String view = "";
        if (id != null && !id.equals("")) {
        	foreCastAdvisoryDetails = farmerService.findforeCastAdvisoryDetailsById(Long.valueOf(id));
            if (foreCastAdvisoryDetails == null) {
                addActionError(NO_RECORD);
                return REDIRECT;
            }
            
           
            setCurrentPage(getCurrentPage());
            command = UPDATE;
            view = DETAIL;
            request.setAttribute(HEADING, getText("forecastAdvisorDetail"));
        } else {
            request.setAttribute(HEADING, getText("forecastAdvisorList"));
            return LIST;
        }
        return view;
    
	}
	@Override
	public Object getData() {
		// TODO Auto-generated method stub
		return null;
	}
	public List<ProcurementProduct> getProcurementProductList() {
		List<ProcurementProduct> productList =productDistributionService.listProcurementProduct();
		JSONArray categorysArr = new JSONArray();
		if (!ObjectUtil.isListEmpty(productList)) {
			for (ProcurementProduct procurementProduct : productList) {
				categorysArr.add(getJSONObject(procurementProduct.getId(), procurementProduct.getName()));
			}
		}
		return categorysArr;
	}

	/*public List<ForeCastData> getForecastDataList(){
		 List<ForeCastData> forecast= farmerService.listForeCastData();
		 JSONArray forecastDataArr = new JSONArray();
			if (!ObjectUtil.isListEmpty(forecast)) {
				for (ForeCastData forecastData : forecast) {
					forecastDataArr.add(getJSONObject(forecastData.getId(), forecastData.getName()));
				}
			}
			return forecastDataArr;
	}*/
	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}

	

	public List<ProcurementProduct> getProductList() {
		return productList;
	}


	public void setProductList(List<ProcurementProduct> productList) {
		this.productList = productList;
	}


	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}
	
	public void setProductDistributionService(IProductDistributionService productDistributionService) {

		this.productDistributionService = productDistributionService;
		
	}

	/**
	 * Gets the product distribution service.
	 * 
	 * @return the product distribution service
	 */
	public IProductDistributionService getProductDistributionService() {

		return productDistributionService;
	}
	

	public String getProductTotalString() {
		return productTotalString;
	}


	public void setProductTotalString(String productTotalString) {
		this.productTotalString = productTotalString;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	

	public ForecastAdvisory getForecastAdvisory() {
		return forecastAdvisory;
	}

	public void setForecastAdvisory(ForecastAdvisory forecastAdvisory) {
		this.forecastAdvisory = forecastAdvisory;
	}

	public String getSelectedCrop() {
		return selectedCrop;
	}


	public void setSelectedCrop(String selectedCrop) {
		this.selectedCrop = selectedCrop;
	}


	public ForecastAdvisoryDetails getForeCastAdvisoryDetails() {
		return foreCastAdvisoryDetails;
	}


	public void setForeCastAdvisoryDetails(ForecastAdvisoryDetails foreCastAdvisoryDetails) {
		this.foreCastAdvisoryDetails = foreCastAdvisoryDetails;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMiniHumi() {
		return miniHumi;
	}

	public void setMiniHumi(String miniHumi) {
		this.miniHumi = miniHumi;
	}

	public String getMaxiHumi() {
		return maxiHumi;
	}

	public void setMaxiHumi(String maxiHumi) {
		this.maxiHumi = maxiHumi;
	}

	public String getMiniWind() {
		return miniWind;
	}

	public void setMiniWind(String miniWind) {
		this.miniWind = miniWind;
	}

	public String getMaxiWind() {
		return maxiWind;
	}

	public void setMaxiWind(String maxiWind) {
		this.maxiWind = maxiWind;
	}

	public String getMiniRain() {
		return miniRain;
	}

	public void setMiniRain(String miniRain) {
		this.miniRain = miniRain;
	}

	public String getMaxiRain() {
		return maxiRain;
	}

	public void setMaxiRain(String maxiRain) {
		this.maxiRain = maxiRain;
	}

	public String getMiniTemp() {
		return miniTemp;
	}

	public void setMiniTemp(String miniTemp) {
		this.miniTemp = miniTemp;
	}

	public String getMaxiTemp() {
		return maxiTemp;
	}

	public void setMaxiTemp(String maxiTemp) {
		this.maxiTemp = maxiTemp;
	}

	public ProcurementProduct getProcurementProduct() {
		return procurementProduct;
	}

	public void setProcurementProduct(ProcurementProduct procurementProduct) {
		this.procurementProduct = procurementProduct;
	}


}
