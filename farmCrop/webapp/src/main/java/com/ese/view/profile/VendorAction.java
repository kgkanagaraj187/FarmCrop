package com.ese.view.profile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Vendor;
import com.sourcetrace.esesw.entity.profile.WarehousePayment;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

public class VendorAction extends SwitchValidatorAction 
{

    private static final long serialVersionUID = 584542518364324369L;

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(VendorAction.class);
    
    protected static final String CREATE = "create";
    protected static final String DETAIL = "detail";
    protected static final String UPDATE = "update";
    protected static final String MAPPING = "mapping";
    protected static final String DELETE = "delete";
    protected static final String LIST = "list";
    protected static final String TITLE_PREFIX = "title.";
    protected static final String HEADING = "heading";
    private IAgentService agentService;
    private IUniqueIDGenerator idGenerator;
    private IAccountService accountService;
    private Vendor vendor;
    private Vendor filter;
    private String id;
    private IProductService productService;

    
    /**
     * Data.
     * @return the string
     * @throws Exception the exception
     * @see com.sourcetrace.esesw.view.SwitchAction#data()
     */
    @SuppressWarnings("unchecked")
    public String data() throws Exception {

        Map<String, String> searchRecord = getJQGridRequestParam(); // get the search parameter with

        Vendor filter = new Vendor();
        
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
        if (!StringUtil.isEmpty(searchRecord.get("vendorId"))) 
        {
            filter.setVendorId(searchRecord.get("vendorId").trim());
        }

        if (!StringUtil.isEmpty(searchRecord.get("vendorName")))
        {
            filter.setVendorName(searchRecord.get("vendorName").trim());
        }
        if (!StringUtil.isEmpty(searchRecord.get("personName"))) {
            filter.setPersonName(searchRecord.get("personName").trim());
        }
        if (!StringUtil.isEmpty(searchRecord.get("email")))
        {
            filter.setEmail(searchRecord.get("email").trim());
        }
        if (!StringUtil.isEmpty(searchRecord.get("vendorAddress")))
        {
            filter.setVendorAddress(searchRecord.get("vendorAddress").trim());
        }
        
        if (!StringUtil.isEmpty(searchRecord.get("mobileNumber")))
        {
            filter.setMobileNumber(searchRecord.get("mobileNumber").trim());
        }
        
        Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(),
                getResults(), filter, getPage());

        return sendJQGridJSONResponse(data);
    }
    
    
    /**
     * Detail.
     * @return the string
     * @throws Exception the exception
     */
    public String detail() throws Exception 
    {
        String view = "";
        if (id != null && !id.equals(""))
        {
            vendor = agentService.findVendor(Long.valueOf(id));
            if (vendor == null) 
            {
                addActionError(NO_RECORD);
                return REDIRECT;
            }
            setCurrentPage(getCurrentPage());
            command = UPDATE;
            view = DETAIL;
            request.setAttribute(HEADING, getText("vendordetail"));
        }
        else 
        {
            request.setAttribute(HEADING, getText("vendorlist"));
            return LIST;
        }
        return view;
    }
    
    
    /**
     * Creates the.
     * @return the string
     * @throws Exception the exception
     */
    public String create() throws Exception
    {

        if (vendor == null)
        {
            command = "create";
            request.setAttribute(HEADING, getText("vendorcreate"));        
            return INPUT;
        }
        else 
        {            
            String vendorIdSeq = idGenerator.createVendorId();
            vendor.setVendorId(vendorIdSeq);
            vendor.setBranchId(getBranchId());
            agentService.addVendor(vendor);
            String accountNo=idGenerator.createVendorAccountNoSequence(vendorIdSeq);
            accountService.createAccount(vendorIdSeq, accountNo, new Date(), ESEAccount.VENDOR_ACCOUNT,vendor.getBranchId());   
            return REDIRECT;
        }
    }
    
    /**
     * Update.
     * @return the string
     * @throws Exception the exception
     */
    public String update() throws Exception
    {

        if (id != null && !id.equals(""))
        {
            vendor = agentService.findVendor(Long.valueOf(id));
            if (vendor == null)
            {
                addActionError(NO_RECORD);
                return REDIRECT;
            }
            setCurrentPage(getCurrentPage());
            id = null;
            command = UPDATE;
            request.setAttribute(HEADING, getText("vendorupdate"));
        }
        else
        {
            if (vendor != null)
            {
                Vendor tempVendor = agentService.findVendor(Long.valueOf(vendor.getId()));
                if (tempVendor == null) 
                {
                    addActionError(NO_RECORD);
                    return REDIRECT;
                }
                setCurrentPage(getCurrentPage());
                tempVendor.setVendorName(vendor.getVendorName());
                tempVendor.setVendorAddress(vendor.getVendorAddress());        
                tempVendor.setEmail(vendor.getEmail());
                tempVendor.setPersonName(vendor.getPersonName());
                tempVendor.setLandLine(vendor.getLandLine());
                tempVendor.setMobileNumber(vendor.getMobileNumber());
                agentService.editVendor(tempVendor);
            }
            request.setAttribute(HEADING, getText("vendorlist"));
            return LIST;
        }
        
        return super.execute();
    }
    private String deleteErrorMsg;
    
    /**
     * Delete.
     * @return the string
     * @throws Exception the exception
     */
    public String delete() throws Exception 
    {
        String result=null;
        if (this.getId() != null && !(this.getId().equals(EMPTY)))
        {
            vendor = agentService.findVendor(Long.valueOf(getId()));
             if (vendor == null)
            {
                addActionError(NO_RECORD);
                return null;
            }
            if (!ObjectUtil.isEmpty(vendor))
            {
                ESEAccount account=accountService.findAccountByProfileIdAndProfileType(vendor.getVendorId(),ESEAccount.VENDOR_ACCOUNT);
                WarehousePayment warehousePayment=agentService.findVendorId(vendor.getId());
                if(!ObjectUtil.isEmpty(warehousePayment))
                {
                    addActionError(getText("cannotDeleteVendorHasTxn"));
                    request.setAttribute(HEADING, getText("vendordetail"));
                    result=DETAIL;
                    
                }
                else
                {
                    agentService.removeVendor(vendor);
                    if(!ObjectUtil.isEmpty(account))
                    {
                        accountService.removeAccountByProfileIdAndProfileType(account.getProfileId(), account.getType());
                    }
                    result=LIST;
                }
                
            }
            
        }
        
        request.setAttribute(HEADING, getText("vendorlist"));
        return result;

    }
    
    
    /**
     * To json.
     * @param record the record
     * @return the JSON object
     * @see com.sourcetrace.esesw.view.SwitchAction#toJSON(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJSON(Object record) 
    {

        JSONObject jsonObject = null;
        jsonObject = new JSONObject();
        if (record instanceof Vendor)
        {
            Vendor vendor = (Vendor) record;
            JSONArray rows = new JSONArray();
    	  	/*if ((getIsMultiBranch().equalsIgnoreCase("1") && (getIsParentBranch().equals("1")||StringUtil.isEmpty(branchIdValue)))) {

    	          if (StringUtil.isEmpty(branchIdValue)) {
    	              rows.add(!StringUtil.isEmpty(
    	                      getBranchesMap().get(getParentBranchMap().get(vendor.getBranchId())))
    	                              ? getBranchesMap()
    	                                      .get(getParentBranchMap().get(vendor.getBranchId()))
    	                              : getBranchesMap().get(vendor.getBranchId()));
    	          }
    	          rows.add(getBranchesMap().get(vendor.getBranchId()));

    	      } else {
    	          if (StringUtil.isEmpty(branchIdValue)) {
    	              rows.add(branchesMap.get(vendor.getBranchId()));
    	          }
    	      }*/
            rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">"
                    + vendor.getVendorId() + "</font>");

            rows.add(vendor.getVendorName());     
            rows.add(vendor.getPersonName());
            rows.add(vendor.getEmail());
            rows.add(vendor.getVendorAddress());
            rows.add(vendor.getMobileNumber());
            jsonObject.put("id", vendor.getId());
            jsonObject.put("cell", rows);
            return jsonObject;

        }
        return jsonObject;
    }
    
    @Override
    public Object getData() 
    {
        // TODO Auto-generated method stub
        return vendor;
    }

    public void setAgentService(IAgentService agentService) {

        this.agentService = agentService;
    }

    public IAgentService getAgentService() {

        return agentService;
    }

    public void setIdGenerator(IUniqueIDGenerator idGenerator) {

        this.idGenerator = idGenerator;
    }

    public IUniqueIDGenerator getIdGenerator() {

        return idGenerator;
    }

    public void setVendor(Vendor vendor) {

        this.vendor = vendor;
    }

    public Vendor getVendor() {

        return vendor;
    }

    public void setFilter(Vendor filter) {

        this.filter = filter;
    }

    public Vendor getFilter() {

        return filter;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getId() {

        return id;
    }
    public IAccountService getAccountService() {
        
        return accountService;
    }

    public void setAccountService(IAccountService accountService) {
    
        this.accountService = accountService;
    }


    public void setDeleteErrorMsg(String deleteErrorMsg) {

        this.deleteErrorMsg = deleteErrorMsg;
    }


    public String getDeleteErrorMsg() {

        return deleteErrorMsg;
    }

   
}
