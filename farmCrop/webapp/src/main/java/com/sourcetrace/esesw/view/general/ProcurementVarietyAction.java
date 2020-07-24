package com.sourcetrace.esesw.view.general;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.profile.CropHarvestDetails;
import com.ese.entity.profile.CropSupplyDetails;
import com.ese.entity.util.ESESystem;
import com.opensymphony.xwork2.ActionContext;
import com.sourcetrace.eses.BreadCrumb;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

public class ProcurementVarietyAction extends SwitchValidatorAction {

    private static final long serialVersionUID = 1L;
    private IProductDistributionService productDistributionService;

    private ProcurementVariety procurementVariety;
    
    private ProcurementGrade procurementGrade; 

    private String id;

    private String tabIndexVariety = "#tabs-2";

    private String tabIndex = "#tabs-1";

    private String procurementProductId;

    private String procurementProductCodeAndName;
    
    private String addingGradeEnabled;
    
    private String varietyYield;
    private String noDaysToGrow;
    private String harvestDays;
    private String varietyName;
    private String  crop;


	@Autowired
    private IUniqueIDGenerator idGenerator;

    public String data() throws Exception {

        Map<String, String> searchRecord = getJQGridRequestParam(); // get the search parameter with
        // value

        ProcurementVariety filter = new ProcurementVariety();
        ProcurementProduct procurementProduct=new ProcurementProduct();
        ProcurementProduct ppObj = null;

        if (!StringUtil.isEmpty(searchRecord.get("code"))) {
            filter.setCode(searchRecord.get("code").trim());
        }

        if (!StringUtil.isEmpty(searchRecord.get("varietyName"))) {
            filter.setName(searchRecord.get("varietyName").trim());
        }
        
        if (!StringUtil.isEmpty(searchRecord.get("procurementProductId"))) {
          	procurementProduct.setName(searchRecord.get("procurementProductId"));
          	filter.setProcurementProduct(procurementProduct);
        }
        
        if (!StringUtil.isEmpty(searchRecord.get("noDaysToGrow"))) {
            filter.setNoDaysToGrow(searchRecord.get("noDaysToGrow").trim());
        }  
        
        if (!StringUtil.isEmpty(searchRecord.get("varietyYield"))) {
            filter.setYield(searchRecord.get("varietyYield").trim());
        }
        
        if (!StringUtil.isEmpty(searchRecord.get("harvestDays"))) {
            filter.setHarvestDays(searchRecord.get("harvestDays").trim());
        }      
        
       /* if(!StringUtil.isEmpty(getBranchId()))
        {
        	procurementProduct.setBranchId(getBranchId());
        	filter.setProcurementProduct(procurementProduct);
        	
        }*/

        if (!StringUtil.isEmpty(this.getProcurementProductId())) {
            ppObj = productDistributionService
                    .findProcurementProductById(Long.valueOf(this.getProcurementProductId()));
            filter.setProcurementProduct(ppObj);
            if (!ObjectUtil.isEmpty(filter.getProcurementProduct())) {
                filter.getProcurementProduct().setId(Long.valueOf(this.getProcurementProductId()));
            }

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

        ProcurementVariety variety = (ProcurementVariety) obj;
        JSONObject jsonObject = new JSONObject();
        JSONArray rows = new JSONArray();
        rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">" + variety.getCode()
                + "</font>");
       // rows.add(!ObjectUtil.isEmpty(variety.getProcurementProduct())?variety.getProcurementProduct().getName():"");
        
        if(!ObjectUtil.isEmpty(variety.getProcurementProduct())){
        	  String productName = getLanguagePref(getLoggedInUserLanguage(), variety.getProcurementProduct().getCode().trim().toString());
      		if(!StringUtil.isEmpty(productName) && productName != null){
      			rows.add(productName);
      		}else{
      			rows.add(!ObjectUtil.isEmpty(variety.getProcurementProduct())?variety.getProcurementProduct().getName():"");
      		}
        }else{
        	rows.add(!ObjectUtil.isEmpty(variety.getProcurementProduct())?variety.getProcurementProduct().getName():"");
        }
      
        
        
        
        
        //rows.add(variety.getName());
       
        String varietyname = getLanguagePref(getLoggedInUserLanguage(), variety.getCode().trim().toString());
      		if(!StringUtil.isEmpty(varietyname) && varietyname != null){
      			rows.add(varietyname);
      		}else{
      			rows.add(variety.getName());
      		}
      		
       if(!getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)) 
       {
	        rows.add(!StringUtil.isEmpty(variety.getNoDaysToGrow())?variety.getNoDaysToGrow():"");
	        rows.add(!StringUtil.isEmpty(variety.getYield())?CurrencyUtil.getDecimalFormat(Double.valueOf(variety.getYield()),"##.00"):"");
	        rows.add(!StringUtil.isEmpty(variety.getHarvestDays())?variety.getHarvestDays():"");
       }  
        jsonObject.put("id", variety.getId());
        jsonObject.put("cell", rows);
        return jsonObject;
    }

    @SuppressWarnings("unchecked")
	public void create() throws Exception {

    	if(!StringUtil.isEmpty(varietyName))
    	{
    		procurementVariety=new ProcurementVariety();
	            if (!StringUtil.isEmpty(this.getProcurementProductId())) {
	                procurementVariety.setProcurementProduct(productDistributionService
	                        .findProcurementProductById(Long.valueOf(this.getProcurementProductId())));
	            }
	            ProcurementVariety pv=productDistributionService.findProcurementVariertyByNameAndCropId(varietyName,Long.valueOf(procurementProductId));
	            if(!ObjectUtil.isEmpty(pv) && !ObjectUtil.isEmpty(procurementVariety.getProcurementProduct())){
	            	if(pv.getProcurementProduct().getId()==procurementVariety.getProcurementProduct().getId()){
	            		getJsonObject().put("msg", getText("msg.alreadyExists"));  
	        	        getJsonObject().put("title", getText("title.error"));
	        	        sendAjaxResponse(getJsonObject());
	            	}
	            }
	            else{
	            if (!StringUtil.isEmpty(varietyYield))
	                this.procurementVariety.setYield(CurrencyUtil.currencyFormatter(Double.valueOf(varietyYield)));
	            else
	            procurementVariety.setYield("0");
	            procurementVariety.setCode(idGenerator.getProcurementVarietyIdSeq()); 
	            procurementVariety.setHarvestDays(harvestDays);
	            procurementVariety.setName(varietyName);
	            procurementVariety.setNoDaysToGrow(noDaysToGrow);
	            
	            productDistributionService.addProcurementVariety(procurementVariety);
	            if((getCurrentTenantId().equalsIgnoreCase("lalteer")) || (getCurrentTenantId().equalsIgnoreCase("awi")) ){
	            procurementGrade = new ProcurementGrade();
	            if (!StringUtil.isEmpty(procurementVariety.getId())) {
	             procurementGrade.setProcurementVariety(procurementVariety);
	            }
	           
	                procurementGrade.setName(procurementVariety.getName());
	                procurementGrade.setCode(idGenerator.getProcurementGradeIdSeq());
	                procurementGrade.setPrice(new Double(0));
	        
	                productDistributionService.addProcurementGrade(procurementGrade);
	          
	        }
	        getJsonObject().put("msg", getText("msg.varietyAdded"));  
	        getJsonObject().put("title", getText("title.success"));
	        sendAjaxResponse(getJsonObject());
	            }
	            
    	}
    }

    @SuppressWarnings("unchecked")
	public void update() throws Exception {

     
            if (!StringUtil.isEmpty(id)) {
                ProcurementVariety temp = productDistributionService
                        .findProcurementVariertyById(Long.valueOf(id));
                temp.setName(varietyName);
            
                Pattern p = Pattern.compile("[0-9]+");
            	Matcher m = p.matcher(noDaysToGrow);
            	boolean b = m.matches();
            	
            	//if(!StringUtil.isEmpty(noDaysToGrow)&& noDaysToGrow.length()<4 && !noDaysToGrow.equals(0)&& b!=false && !StringUtil.isEmpty(varietyName)){
            	if(!StringUtil.isEmpty(varietyName)){
            		temp.setNoDaysToGrow(noDaysToGrow);
            		
            		   temp.setHarvestDays(harvestDays);
                       
                       if (!StringUtil.isEmpty(varietyYield)){
                       	temp.setYield(CurrencyUtil.currencyFormatter(Double.valueOf(varietyYield)));
                       }else{
                       	temp.setYield("0"); 
                       }
                       productDistributionService.editProcurementVariety(temp);
                       
                    getJsonObject().put("msg", getText("msg.varityUpdated"));  
           	        getJsonObject().put("title", getText("title.success"));
           	      
            	}else if(getCurrentTenantId().equalsIgnoreCase(ESESystem.KPF_TENANT_ID)&&getCurrentTenantId().equalsIgnoreCase(ESESystem.SIMFED_TENANT_ID)&&getCurrentTenantId().equalsIgnoreCase(ESESystem.WUB_TENANT_ID)){
            		if(!StringUtil.isEmpty(noDaysToGrow)&& noDaysToGrow.length()<4 && !noDaysToGrow.equals(0)&& b!=false && !StringUtil.isEmpty(varietyName)){
            			getJsonObject().put("msg", getText("Please Enter Valid Crop Cycle(Days)"));
            			getJsonObject().put("title",getText("Error"));
            		}
            	}else{
            			getJsonObject().put("msg", getText("Please Enter Variety Name"));
            			getJsonObject().put("title",getText("Error"));
            			
            	}
            	
                //temp.setNoDaysToGrow(noDaysToGrow);
                //temp.setYield(procurementVariety.getYield());
            	
            	sendAjaxResponse(getJsonObject());
            }
    }

    /*public String detail() throws Exception {

        String view = "";
        if (id != null && !id.equals("")) {
            procurementVariety = productDistributionService
                    .findProcurementVariertyById(Long.valueOf(id));
            if (procurementVariety == null) {
                addActionError(NO_RECORD);
                return REDIRECT;
            }
            setCurrentPage(getCurrentPage());
            command = UPDATE;
            view = DETAIL;
            request.setAttribute(HEADING, getText(DETAIL));
        } else {
            request.setAttribute(HEADING, getText(DETAIL));
            view = DETAIL;
        }
        return view;
    }*/

    @SuppressWarnings("unchecked")
	public void populateDelete() throws Exception {

        if (id != null) {
            procurementVariety = productDistributionService
                    .findProcurementVariertyById(Long.valueOf(id));
            
            if (!ObjectUtil.isEmpty(procurementVariety)) {

                List<CropHarvestDetails> cropHarvetsDetailsList = productDistributionService
                        .listCropHarvestDetailsByProcurementVarietyId(Long.valueOf(id));
                List<CropSupplyDetails> cropSaleDetailsList = productDistributionService
                        .listCropSaleDetailsByProcurementVarietyId(Long.valueOf(id));
                List<ProcurementGrade> procurementGradeList = productDistributionService
                        .listProcurementGradeByProcurementVarietyId(Long.parseLong(id));
                List<FarmCrops> farmCropsList = productDistributionService
                        .listfarmCropsByProcurementVarietyId(Long.parseLong(id));
                if (!ObjectUtil.isListEmpty(cropHarvetsDetailsList)) {
                    getJsonObject().put("msg", getText("cropHarvest.exist"));  
        	        getJsonObject().put("title", getText("title.error"));
        	       
                }

                if (!ObjectUtil.isListEmpty(cropSaleDetailsList)) {
                    getJsonObject().put("msg", getText("cropSupply.exist"));  
        	        getJsonObject().put("title", getText("title.error"));
                }

                if (!ObjectUtil.isListEmpty(procurementGradeList)) {
                    getJsonObject().put("msg", getText("delete.warn"));  
        	        getJsonObject().put("title", getText("title.error"));
                }
                if (!ObjectUtil.isListEmpty(farmCropsList)) {
                    getJsonObject().put("msg", getText("farmCrop.exist"));  
        	        getJsonObject().put("title", getText("title.error"));
                }
                else
                {
                	productDistributionService.remove(procurementVariety);
                	getJsonObject().put("msg", getText("varietymsg.deleted"));  
        	        getJsonObject().put("title", getText("title.success"));
                }
                
            }
            sendAjaxResponse(getJsonObject());
        }
    }
    public void populateCrop() throws Exception{
    	 
    	List<ProcurementProduct>procurementProdList=productDistributionService.listProcurementProduct();
    	JSONArray crop = new JSONArray();
		if (!ObjectUtil.isEmpty(procurementProdList)) {
    	// JSONObject crop=new JSONObject();
    	 //if (!ObjectUtil.isEmpty(procurementProdList)) {
    		 for (ProcurementProduct ProcurementProduct: procurementProdList) {
    			 crop.add(getJSONObject(ProcurementProduct.getId(),ProcurementProduct.getName()));
				} 
    	 }
		sendAjaxResponse(crop);
    	 }
    @SuppressWarnings("unchecked")
	protected JSONObject getJSONObject( Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}
	public void populateProcurementProduct()
	{
		  List<ProcurementProduct>procurementProdList = productDistributionService.listProcurementProduct();
		  
		  JSONObject jsonObject=new JSONObject();
		  procurementProdList.forEach(procurementProduct->jsonObject.put(String.valueOf(procurementProduct.getId()), procurementProduct.getName()));
			try {
				PrintWriter out = response.getWriter();
				response.setContentType("application/json");
				out.print(jsonObject.toString().replace("\"", "").replace(",", ";"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
    
    

    public IProductDistributionService getProductDistributionService() {

        return productDistributionService;
    }

    public void setProductDistributionService(
            IProductDistributionService productDistributionService) {

        this.productDistributionService = productDistributionService;
    }

    public ProcurementVariety getProcurementVariety() {

        return procurementVariety;
    }

    public void setProcurementVariety(ProcurementVariety procurementVariety) {

        this.procurementVariety = procurementVariety;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getTabIndexVariety() {

        return tabIndexVariety;
    }

    public void setTabIndexVariety(String tabIndexVariety) {

        this.tabIndexVariety = tabIndexVariety;
    }

    public String getProcurementProductId() {

        return procurementProductId;
    }

    public void setProcurementProductId(String procurementProductId) {

        this.procurementProductId = procurementProductId;
    }

    public String getProcurementProductCodeAndName() {

        return procurementProductCodeAndName;
    }

    public void setProcurementProductCodeAndName(String procurementProductCodeAndName) {

        this.procurementProductCodeAndName = procurementProductCodeAndName;
    }

    public String getProcurementDetailParams() {

        return "tabIndex=" + URLEncoder.encode(tabIndexVariety) + "&id=" + getProcurementProductId()
                + "&" + tabIndexVariety;
    }

    public IUniqueIDGenerator getIdGenerator() {

        return idGenerator;
    }

    public void setIdGenerator(IUniqueIDGenerator idGenerator) {

        this.idGenerator = idGenerator;
    }

    @Override
    public Object getData() {

        return procurementVariety;
    }

    public void prepare() throws Exception {

        String procurementProductId = (String) request.getParameter("procurementProductEnrollId");

        if (StringUtil.isEmpty(procurementProductId) && !StringUtil
                .isEmpty(request.getSession().getAttribute("uniqueProcurementProductId"))) {
            procurementProductId = request.getSession().getAttribute("uniqueProcurementProductId")
                    .toString();
        }
        String actionClassName = ActionContext.getContext().getActionInvocation().getAction().getClass()
				.getSimpleName();
		String content = getLocaleProperty(actionClassName + "." + BreadCrumb.BREADCRUMB);
		if (StringUtil.isEmpty(content) || (content.equalsIgnoreCase(actionClassName + "." + BreadCrumb.BREADCRUMB))) {
			content = super.getText(BreadCrumb.BREADCRUMB, "");
		}
		request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(content+ procurementProductId + "&tabValue=tabs-2"));
       /* request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(
                getText(BreadCrumb.BREADCRUMB, "") + procurementProductId + "&tabValue=tabs-2"));*/
    }

    public String getTabIndex() {

        return tabIndex;
    }

    public void setTabIndex(String tabIndex) {

        this.tabIndex = tabIndex;
    }
       
   
    private double getDoubleValueFromString(String str1, String str2) {

        double toDoubleValue = 0d;
        try {

            StringBuffer sb = new StringBuffer();
            if (!StringUtil.isEmpty(str1)) {
                sb.append(str1);
            }
            if (!StringUtil.isEmpty(str2)) {
                sb.append(".");
                sb.append(str2);
            }
            if (!StringUtil.isEmpty(sb.toString())) {
                toDoubleValue = Double.valueOf(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toDoubleValue;
    }

    public ProcurementGrade getProcurementGrade() {
    
        return procurementGrade;
    }

    public void setProcurementGrade(ProcurementGrade procurementGrade) {
    
        this.procurementGrade = procurementGrade;
    }

    public String getAddingGradeEnabled() {
    
        return addingGradeEnabled;
    }

    public void setAddingGradeEnabled(String addingGradeEnabled) {
    
        this.addingGradeEnabled = addingGradeEnabled;
    }

	public String getVarietyYield() {
		return varietyYield;
	}

	public void setVarietyYield(String varietyYield) {
		this.varietyYield = varietyYield;
	}  

	 public String getNoDaysToGrow() {
			return noDaysToGrow;
		}

		public void setNoDaysToGrow(String noDaysToGrow) {
			this.noDaysToGrow = noDaysToGrow;
		}

		public String getHarvestDays() {
			return harvestDays;
		}

		public void setHarvestDays(String harvestDays) {
			this.harvestDays = harvestDays;
		}

	    public String getVarietyName() {
			return varietyName;
		}

		public void setVarietyName(String varietyName) {
			this.varietyName = varietyName;
		}
		
		
		
    
}
