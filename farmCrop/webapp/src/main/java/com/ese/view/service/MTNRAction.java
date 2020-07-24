/*
 * MTNRAction.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;

import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.AgentType;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.profile.GradeMaster;
import com.sourcetrace.eses.order.entity.txn.CityWarehouse;
import com.sourcetrace.eses.order.entity.txn.PMT;
import com.sourcetrace.eses.order.entity.txn.PMTDetail;
import com.sourcetrace.eses.order.entity.txn.TransferInfo;
import com.sourcetrace.eses.service.FarmerService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.view.WebTransactionAction;

@SuppressWarnings("serial")
public class MTNRAction extends WebTransactionAction {

    private static final String PROCUREMENT_MTNR = "319";
    private static final DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
    private static final String RECEIPT_DATE_FORMAT = "dd-MM-yyyy";

    private ILocationService locationService;
    private IProductDistributionService productDistributionService;
    @Autowired
    private IProductService productService;
    private IUniqueIDGenerator idGenerator;
    private IFarmerService farmerService;
    private List<GradeMaster> gradeMasterList;
    private Map<String, String> productMap = new LinkedHashMap<String, String>();

    private String gradeInputString;
    private String gradeMasterIdList;
    private String startDate;
    private String receiptNumber;
    private String mtnrDescription;
    private String truckId;
    private String driverId;
    private String selectedProduct;
    private String selectedWarehouseId;
    private String agentId;
    private HashMap<String, Object> mtnrPrintMap = new LinkedHashMap<String, Object>();
    private String listWarehouse;
    private long receiptNo;
    private long warehouseId;
    private Map<Long,String>listOfReceiptNo=new LinkedHashMap<Long,String>();
    private String receiptNoId;
    private long pmtDetailId;
    private long pmtId;
   private String productDetailsStr;
   private String seasonName;
   private String prodId;
   private String varietyId;
   private String gradeId;
   private String farmer;
   private Map<String, String> farmerMap = new LinkedHashMap<String, String>();
   private String selectedCoOperative;
   private String recNum;
   
    public String getRecNum() {
	return recNum;
}

public void setRecNum(String recNum) {
	this.recNum = recNum;
}

	/**
     * @see com.sourcetrace.esesw.view.SwitchAction#list()
     */
    public String list() {
    	
    	
        request.setAttribute(HEADING, getText("create.page"));

        return REDIRECT;
    }

    /**
     * Populate product reception.
     */
    public void populateProductReception() {

        String result = "";
        
      
            Warehouse warehouse = locationService.findWarehouseById(Long
                    .valueOf(selectedCoOperative));
            if (ObjectUtil.isEmpty(warehouse)) {
                result = "warehouse.unavailable";
            }
            PMT proceurementMTNR = new PMT();
            Set<PMTDetail>pmtDetails=new HashSet<PMTDetail>();
            proceurementMTNR.setMtnrTransferInfo(getTransferInfoDetail());
            proceurementMTNR.setMtnrDate(DateUtil.convertStringToDate(startDate, getGeneralDateFormat()));
            proceurementMTNR.setMtnrDate(DateUtil.setTimeToDate(proceurementMTNR.getMtnrDate()));
            
            proceurementMTNR.setCoOperative(warehouse);
            proceurementMTNR.setTruckId(truckId);
            proceurementMTNR.setDriverName(driverId);
            proceurementMTNR.setStatusCode(PMT.Status.MTNR.ordinal());
            proceurementMTNR.setStatusMessage(PMT.Status.MTNR.toString());
            proceurementMTNR.setMtnrReceiptNumber(idGenerator.getMTNRReceiptNoSeq());
            proceurementMTNR.setSeasonCode(clientService.findCurrentSeasonCode());
            proceurementMTNR.setBranchId(getBranchId());
            receiptNumber = proceurementMTNR.getMtnrReceiptNumber();
            proceurementMTNR.setTrnType(PMT.TRN_TYPE_OTEHR);
            
            String[] productArray = productDetailsStr.split("\\|\\|");
			PMT tempPmt=productDistributionService.findPMTByReceiptNumber(recNum);
				for (String gradeStr : productArray) {
					
					PMTDetail pmtDetail=new PMTDetail();
	                String[] productValues = gradeStr.split("##");
	                
	                ProcurementGrade procurementGrade = productDistributionService
                            .findProcurementGradeById(Long.valueOf(productValues[1]));
	                pmtDetail.setPmt(proceurementMTNR);
	                if(!ObjectUtil.isEmpty(procurementGrade)){
	                	pmtDetail.setProcurementProduct(productDistributionService
	                            .findProcurementProductById(Long.valueOf(productValues[0])));
	                }

	                if(!ObjectUtil.isEmpty(procurementGrade)){
	                	pmtDetail.setProcurementGrade(procurementGrade);
	                }
	                pmtDetail.setMtnrGrossWeight(Double.valueOf(productValues[3]));
	                pmtDetail.setMtnrNumberOfBags(Long.valueOf(productValues[2]));
	                pmtDetail.setCoOperative(tempPmt.getCoOperative());
	                pmtDetails.add(pmtDetail);
	                proceurementMTNR.setPmtDetails(pmtDetails);
	                
	                PMTDetail existing=productDistributionService.findpmtdetailById(Long.valueOf(productValues[4]));
	                proceurementMTNR.setMtntReceiptNumber(existing.getPmt().getMtntReceiptNumber());
	                existing.setStatus(3);
	                /*PMTDetail pmtDetailsRece= new PMTDetail();
	              pmtDetailsRece.setPmt(existing.getPmt());
	             pmtDetailsRece.setProcurementProduct(procurementProduct);
	             pmtDetailsRece.setGradeMaster(existing.getGradeMaster());
	             pmtDetailsRece.setMtntNumberOfBags(existing.getMtntNumberOfBags() - Long.valueOf(productValues[1]));
	             pmtDetailsRece.setMtntGrossWeight(existing.getMtntGrossWeight() - Double.valueOf(productValues[2]));
	             pmtDetailsRece.setCoOperative(existing.getCoOperative());
	             pmtDetailsRece.setProcurementGrade(existing.getProcurementGrade());
	             pmtDetailsRece.setStatus(1);*/
	                existing.setMtnrNumberOfBags(existing.getMtnrNumberOfBags() - Long.valueOf(productValues[2]));
	                existing.setMtnrGrossWeight(existing.getMtnrGrossWeight() - Double.valueOf(productValues[3]));
	               productDistributionService.update(existing);

			}
           
            productDistributionService.save(proceurementMTNR);
            productDistributionService.processCityWarehouse(proceurementMTNR);
         //   productDistributionService.update(existingMtntDetails);
          if (StringUtil.isEmpty(result)) {
        	/*  String receiptHtml = "<a href=\"javascript:printReceipt(\\'" + receiptNumber
                      + "\\')\" onclick='printReceipt(\"" + receiptNumber + "\")'>"
                      + getText("printReceipt") + "</button>";
                setMtnrDescription(getText("productReceptionSucess") + "</br>"
                        + getText("receiptNumber") + " : "+ receiptNumber+"<br/>"+ receiptHtml);

                printAjaxResponse(getMtnrDescription(), "text/html");*/
        	  
              String receiptHtml = "<a href=\"javascript:printReceipt(\\'" + receiptNumber + 
                      "\\')\" class ='btn btn-default btnBorderRadius' onclick='printReceipt(\"" + receiptNumber + "\")'>" +
                      getText("printReceipt") + "</button>";
                     
              setMtnrDescription("<h5>"
                            + getText("receiptNumber") + " : " + receiptNumber+ "</h5>" + receiptHtml + "</br>");
              printAjaxResponse(getMtnrDescription(), "text/html");
        	  
            } else {
                setMtnrDescription(getText(result));
                printAjaxResponse(getMtnrDescription(), "text/html");
            }

        }
     

    /**
     * Creates the.
     * @return the string
     */
    public String create() {

        request.setAttribute(HEADING, getText("createmtnr"));
        loadProductList();
        if (!StringUtil.isEmpty(selectedProduct)) {

            // Added for Handling Form ReSubmit - Please See at populateFarmerAccount() Method
            if (ObjectUtil
                    .isEmpty(request.getSession().getAttribute(
                            agentId + "_" + PROCUREMENT_MTNR + "_"
                                    + WebTransactionAction.IS_FORM_RESUBMIT))) {
                reset();
                return INPUT;
            }

            PMT proceurementMTNR = new PMT();
            Agent agent = agentService.findAgentByProfileId(agentId);
            if (!ObjectUtil.isEmpty(agent) && !ObjectUtil.isEmpty(agent.getCooperative())) {
                proceurementMTNR.setMtnrTransferInfo(getTransferInfo(agent));
                proceurementMTNR.setMtnrDate(DateUtil.convertStringToDate(startDate, getGeneralDateFormat()));
                proceurementMTNR.setMtnrDate(DateUtil.convertStringToDate(startDate, getGeneralDateFormat()));
                ProcurementProduct procurementProduct = productDistributionService
                        .findProcurementProductById(Long.valueOf(selectedProduct));
                if (!ObjectUtil.isEmpty(procurementProduct)) {
                    Map<String, String[]> gradeInputMap = formGradeInputMapFromString(gradeInputString);
                    proceurementMTNR.setPmtDetails(new LinkedHashSet<PMTDetail>());

                    if (!ObjectUtil.isEmpty(gradeInputMap)) {
                        if (gradeInputMap.size() > 0) {
                            for (Entry<String, String[]> entry : gradeInputMap.entrySet()) {
                                String id = entry.getKey();
                                String[] gradeValues = entry.getValue();
                                GradeMaster gradeMaster = productDistributionService
                                        .findGradeById(Long.valueOf(id));
                                if (!ObjectUtil.isEmpty(gradeMaster)) {
                                    PMTDetail pmtDetail = new PMTDetail();
                                    pmtDetail.setPmt(proceurementMTNR);
                                    pmtDetail.setProcurementProduct(procurementProduct);
                                    pmtDetail.setGradeMaster(gradeMaster);
                                    pmtDetail.setMtnrNumberOfBags(Long.valueOf(gradeValues[0]));
                                    pmtDetail.setMtnrGrossWeight(Double.valueOf(gradeValues[1]));
                                    proceurementMTNR.getPmtDetails().add(pmtDetail);
                                }
                            }
                        }
                    }
                    proceurementMTNR.setCoOperative(agent.getCooperative());
                    proceurementMTNR.setTruckId(truckId);
                    proceurementMTNR.setDriverName(driverId);
                    proceurementMTNR.setStatusCode(PMT.Status.MTNR.ordinal());
                    proceurementMTNR.setStatusMessage(PMT.Status.MTNR.toString());
                    proceurementMTNR.setMtnrReceiptNumber(idGenerator.getMTNRReceiptNoSeq());
                    proceurementMTNR.setTrnType(PMT.TRN_TYPE_OTEHR);
                    if (!ObjectUtil.isListEmpty(proceurementMTNR.getPmtDetails())) {
                        productDistributionService.editPMTForMTNR(proceurementMTNR);
                        String receiptHtml = "<br/><a href=\"javascript:printReceipt(\\'"
                                + proceurementMTNR.getMtnrReceiptNumber() + "\\')\" >"
                                + getText("printReceipt") + "</a>";
                        setMtnrDescription(getText("receiptNumber") + " : "
                                + proceurementMTNR.getMtnrReceiptNumber() + getText("mtnrSuccess")
                                + receiptHtml);
                    } else {
                        setMtnrDescription(getText("mtnrFailed") + " : "
                                + getText("grades.not.exists"));
                    }
                    resetTimer();
                } else {
                    setMtnrDescription(getText("mtnrFailed") + " : "
                            + getText("product.not.exists"));
                    resetTimer();
                }
            } else {
                setMtnrDescription(getText("mtnrFailed") + " : " + getText("agent.not.exists"));
                resetTimer();
            }
        } else {
            if (!StringUtil.isEmpty(agentId)) {
                request.setAttribute("agentId", agentId);
                Agent agent = agentService.findAgentByProfileId(agentId);
                if (!ObjectUtil.isEmpty(agent) && ESETxn.WEB_BOD == agent.getBodStatus()) {
                    reset();
                    return INPUT;
                }
            }
            return INPUT;
        }
        return INPUT;
    }

    /**
     * Reset.
     */
    private void reset() {

        Calendar currentDate = Calendar.getInstance();
        DateFormat dateFormat=new SimpleDateFormat(getGeneralDateFormat());
        startDate = dateFormat.format(currentDate.getTime());
        request.setAttribute(HEADING, getText("createmtnr"));
        setMtnrDescription(null);
        loadProductList();
    }

    /**
     * Load product list.
     */
    private void loadProductList() {

        productMap = new LinkedHashMap<String, String>();
        List<ProcurementProduct> productList = productDistributionService.listProcurementProduct();
        if (!ObjectUtil.isListEmpty(productList))
            for (ProcurementProduct product : productList)
                productMap.put(String.valueOf(product.getId()), product.getName() + " - "
                        + product.getCode());
    }

    /**
     * Gets the transfer info.
     * @param agent the agent
     * @return the transfer info
     */
    private TransferInfo getTransferInfo(Agent agent) {

        TransferInfo transferInfo = new TransferInfo();
        transferInfo.setAgentId(agent.getProfileId());
        transferInfo.setAgentName((ObjectUtil.isEmpty(agent.getPersonalInfo()) ? "" : agent
                .getPersonalInfo().getAgentName()));
        String nowDate=DateUtil.convertDateToString(new Date(), getGeneralDateFormat());
        transferInfo.setTxnTime(DateUtil.convertStringToDate(nowDate, getGeneralDateFormat()));
        transferInfo.setTxnTime(DateUtil.setTimeToDate(transferInfo.getTxnTime()));
        transferInfo.setDeviceId(getText(NOT_APPLICABLE));
        transferInfo.setDeviceName(getText(NOT_APPLICABLE));
        transferInfo.setServicePointId(getText(NOT_APPLICABLE));
        transferInfo.setServicePointName(getText(NOT_APPLICABLE));
        return transferInfo;
    }

    private TransferInfo getTransferInfoDetail() {

        TransferInfo transferInfo = new TransferInfo();
        String nowDate=DateUtil.convertDateToString(new Date(), getGeneralDateFormat());
        transferInfo.setTxnTime(DateUtil.convertStringToDate(nowDate, getGeneralDateFormat()));
        transferInfo.setTxnTime(DateUtil.setTimeToDate(transferInfo.getTxnTime()));
        transferInfo.setDeviceId(getText(NOT_APPLICABLE));
        transferInfo.setDeviceName(getText(NOT_APPLICABLE));
        transferInfo.setServicePointId(getText(NOT_APPLICABLE));
        transferInfo.setServicePointName(getText(NOT_APPLICABLE));
        return transferInfo;
    }

    /**
     * Form grade input map from string.
     * @param inputString the input string
     * @return the map< string, string[]>
     */
    private Map<String, String[]> formGradeInputMapFromString(String inputString) {

        Map<String, String[]> returnMap = new LinkedHashMap<String, String[]>();
        if (!StringUtil.isEmpty(inputString)) {
            String[] inputArray = inputString.split("\\|\\|");
            for (String gradeStr : inputArray) {
                String[] gradeDetailsArray = gradeStr.split("~~");
                if (!ObjectUtil.isEmpty(gradeDetailsArray) && gradeDetailsArray.length == 3) {
                    if (Double.valueOf(gradeDetailsArray[2].trim()) > 0d)
                        returnMap.put(gradeDetailsArray[0], new String[] { gradeDetailsArray[1],
                                gradeDetailsArray[2] });
                }
            }
        }
        return returnMap;
    }

    /**
     * Populate mtnt details.
     * @throws Exception the exception
     */
    public void populateMTNTDetails() throws Exception {

        String result = "";
        if (!StringUtil.isEmpty(receiptNumber)) {
            PMT procurementMTNT = productDistributionService.findPMTByReceiptNumber(receiptNumber,
                    PMT.Status.MTNT.ordinal());
            if (!ObjectUtil.isEmpty(procurementMTNT)) {
                result = (!ObjectUtil.isEmpty(procurementMTNT.getMtntDate()) ? 
                        DateUtil.convertDateToString(procurementMTNT.getMtntDate(),getGeneralDateFormat()) : "")
                        + "$$"
                        + (!ObjectUtil.isEmpty(procurementMTNT.getCoOperative()) ? procurementMTNT
                                .getCoOperative().getName() : "")
                        + "$$"
                        + procurementMTNT.getTruckId()
                        + "$$"
                        + procurementMTNT.getDriverName()
                        + "@@";
                Set<PMTDetail> pmtDetails = procurementMTNT.getPmtDetails();
                if (!ObjectUtil.isListEmpty(pmtDetails)) {
                    StringBuilder gradeIdSb = new StringBuilder();
                    StringBuilder noOfBagsSb = new StringBuilder();
                    StringBuilder grossWtSb = new StringBuilder();
                    for (PMTDetail pmtDetail : pmtDetails) {
                        gradeIdSb = gradeIdSb.append(pmtDetail.getGradeMaster().getId() + "~~");
                        noOfBagsSb = noOfBagsSb.append(pmtDetail.getMtntNumberOfBags() + "~~");
                        grossWtSb = grossWtSb.append(pmtDetail.getMtntGrossWeight() + "~~");
                    }
                    result = result + gradeIdSb + "||" + noOfBagsSb.toString() + "||"
                            + grossWtSb.toString();
                }
            }
        }
        response.getWriter().print(result);
        response.setContentType("text/html");
    }

    /**
     * Populate submit.
     * @throws Exception the exception
     */
    public void populateSubmit() throws Exception {

        String result = "";
        if (StringUtil.isEmpty(agentId)) {
            result = "agentid.empty";
        } else {
            Agent agent = agentService.findAgentByProfileId(agentId);
            if (ObjectUtil.isEmpty(agent) || ObjectUtil.isEmpty(agent.getCooperative()))
                result = "agent.not.exists";
            else if (Agent.ACTIVE != agent.getStatus())
                result = "agent.inactive";
        }

        if (StringUtil.isEmpty(result)) {
            // Added for handling Form ReSubmit
            request.getSession().setAttribute(
                    agentId + "_" + PROCUREMENT_MTNR + "_" + WebTransactionAction.IS_FORM_RESUBMIT,
                    "No");
        }
        printAjaxResponse(getText(result), "text/html");
    }

    /**
     * Gets the warehouses.
     * @return the warehouses
     */
    public List<Warehouse> getWarehouses() {

        return locationService.listWarehouse();
    }

    /**
     * Gets the receipt number list.
     * @return the receipt number list
     */
    public List<String> getReceiptNumberList() {

        List<String> receiptNumberList = productDistributionService
                .listPMTReceiptNumberByStatus(PMT.Status.MTNT.ordinal());
        return receiptNumberList;
    }

    /**
     * Sets the location service.
     * @param locationService the new location service
     */
    public void setLocationService(ILocationService locationService) {

        this.locationService = locationService;
    }

    /**
     * Sets the product distribution service.
     * @param productDistributionService the new product distribution service
     */
    public void setProductDistributionService(IProductDistributionService productDistributionService) {

        this.productDistributionService = productDistributionService;
    }

    /**
     * Sets the start date.
     * @param startDate the new start date
     */
    public void setStartDate(String startDate) {

        this.startDate = startDate;
    }

    /**
     * Gets the start date.
     * @return the start date
     */
    public String getStartDate() {
    	
		DateFormat df = new SimpleDateFormat(getGeneralDateFormat());
        startDate=df.format(new Date());
        return startDate;
    }

    /**
     * Sets the grade master list.
     * @param gradeMasterList the new grade master list
     */
    public void setGradeMasterList(List<GradeMaster> gradeMasterList) {

        this.gradeMasterList = gradeMasterList;
    }

    /**
     * Gets the grade master list.
     * @return the grade master list
     */
    public List<GradeMaster> getGradeMasterList() {

        if (ObjectUtil.isListEmpty(gradeMasterList)) {
            gradeMasterList = productDistributionService.listGradeMaster();
        }
        return gradeMasterList;
    }

    /**
     * Gets the grade master id list.
     * @return the grade master id list
     */
    public String getGradeMasterIdList() {

        if (ObjectUtil.isEmpty(gradeMasterIdList) && !ObjectUtil.isListEmpty(gradeMasterList)) {
            StringBuilder gradeMasterIdSb = new StringBuilder();
            for (GradeMaster gradeMaster : gradeMasterList)
                gradeMasterIdSb.append(gradeMaster.getId() + "~~");
            gradeMasterIdList = gradeMasterIdSb.toString();
        }
        return gradeMasterIdList;
    }

    /**
     * Gets the procurement product json.
     * @return the procurement product json
     */
    @SuppressWarnings("unchecked")
    public JSONObject getProcurementProductJSON() {

        JSONObject products = new JSONObject();
        JSONArray productArray = new JSONArray();

        Map<String, Map<String, Object>> productsMap = new LinkedHashMap<String, Map<String, Object>>();
        List<ProcurementGrade> procurementGradeList = productDistributionService
                .listProcurementGrade();

        for (ProcurementGrade procurementGrade : procurementGradeList) {
            try {

                if (!ObjectUtil.isEmpty(procurementGrade.getProcurementVariety())
                        && !ObjectUtil.isEmpty(procurementGrade.getProcurementVariety()
                                .getProcurementProduct())) {

                    if (productsMap.containsKey(String.valueOf(procurementGrade
                            .getProcurementVariety().getProcurementProduct().getId()))) {

                        Set<ProcurementGrade> procurementGradeSet = new LinkedHashSet<ProcurementGrade>();

                        if (productsMap.get(
                                String.valueOf(procurementGrade.getProcurementVariety()
                                        .getProcurementProduct().getId())).containsKey(
                                "procurementGrades")) {
                            procurementGradeSet = (Set<ProcurementGrade>) productsMap.get(
                                    String.valueOf(procurementGrade.getProcurementVariety()
                                            .getProcurementProduct().getId())).get(
                                    "procurementGrades");
                        } else {
                            productsMap.get(
                                    String.valueOf(procurementGrade.getProcurementVariety()
                                            .getProcurementProduct())).put("procurementGrades",
                                    procurementGradeSet);
                        }

                        procurementGradeSet.add(procurementGrade);
                    } else {

                        Map<String, Object> productsInfoMap = new LinkedHashMap<String, Object>();
                        Set<ProcurementGrade> procurementGradeSet = new LinkedHashSet<ProcurementGrade>();
                        procurementGradeSet.add(procurementGrade);
                        productsInfoMap.put("product", procurementGrade.getProcurementVariety()
                                .getProcurementProduct());
                        productsInfoMap.put("procurementGrades", procurementGradeSet);
                        productsMap.put(String.valueOf(procurementGrade.getProcurementVariety()
                                .getProcurementProduct().getId()), productsInfoMap);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (Map.Entry entry : productsMap.entrySet()) {
            Map<String, Object> productMap = (Map<String, Object>) entry.getValue();

            ProcurementProduct product = (ProcurementProduct) productMap.get("product");

            Set<ProcurementGrade> procurementGradeSet = (Set<ProcurementGrade>) productMap
                    .get("procurementGrades");

            JSONObject productJSON = new JSONObject();

            productJSON.put("id", product.getId());
            productJSON.put("code", product.getCode());
            productJSON.put("name", product.getName());

            JSONArray procurementGradesJSON = new JSONArray();
            for (ProcurementGrade procurementGrade : procurementGradeSet) {
                JSONObject procurementGradeJSON = new JSONObject();
                procurementGradeJSON.put("id", procurementGrade.getId());
                procurementGradeJSON.put("code", procurementGrade.getCode());
                procurementGradeJSON.put("name", procurementGrade.getName());
                procurementGradeJSON.put("procurementVarietyCode", procurementGrade
                        .getProcurementVariety().getCode());
                procurementGradeJSON.put("procurementVarietyName", procurementGrade
                        .getProcurementVariety().getName());
                procurementGradeJSON.put("procurementVarietyId", procurementGrade
                        .getProcurementVariety().getId());
                procurementGradesJSON.add(procurementGradeJSON);
            }

            productJSON.put("procurementGrades", procurementGradesJSON);

            productArray.add(productJSON);
        }
        products.put("products", productArray);
        return products;
    }

    /**
     * Reset timer.
     */
    private void resetTimer() {

        AgroTimerTask myTask = (AgroTimerTask) request.getSession()
                .getAttribute(agentId + "_timer");
        reStartTxn = true;
        myTask.cancelTimer(false);
        request.setAttribute("agentId", agentId);
        request.getSession().removeAttribute(
                agentId + "_" + PROCUREMENT_MTNR + "_" + WebTransactionAction.IS_FORM_RESUBMIT);
        Calendar currentDate = Calendar.getInstance();
        startDate = DateUtil.convertDateToString(currentDate.getTime(),getGeneralDateFormat());
        request.setAttribute(HEADING, getText("createmtnr"));
        loadProductList();

    }

    /**
     * Gets the current date.
     * @return the current date
     */
    public String getCurrentDate() {

        Calendar currentDate = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat(getGeneralDateFormat());
        return df.format(currentDate.getTime());

    }

    /**
     * Populate print html.
     * @return the string
     */
    public String populatePrintHTML() {

        initializePrintMap();
        if (!StringUtil.isEmpty(receiptNumber)) {
            PMT procurementMTNR = productDistributionService
                    .findPMTByMTNRReceiptNumber(receiptNumber);
            buildTransactionPrintMap(procurementMTNR);
        }
        return "html";
    }
 public void populateReceiptNos(){
	 List<Object[]>receiptNos=productDistributionService.listPMTReceiptNumberByWarehouse(warehouseId);
	 JSONArray jsonArray=new JSONArray();
	 if(!ObjectUtil.isListEmpty(receiptNos)){
	for(Object[] obj:receiptNos){
		 jsonArray.add(convertToJsonObject(obj[0],obj[0]));
	 }
	}
	 sendAjaxResponse(jsonArray);
 }
 public void populateProduct(){
	 List<ProcurementProduct> procurementProducts = new ArrayList<>();
	 if(StringUtil.isEmpty(farmer)){
		 procurementProducts=productDistributionService.listProcurementProductByPMTReceiptNo(receiptNoId);
	 }else{
		 procurementProducts=productDistributionService.listProcurementProductByPMTReceiptNoAndFarmer(receiptNoId,Long.valueOf(farmer));
	 }
	
	 JSONArray jsonArray=new JSONArray();
	 if(!ObjectUtil.isListEmpty(procurementProducts)){
		 for(ProcurementProduct procurementProd:procurementProducts){
			 jsonArray.add(convertToJsonObject(procurementProd.getCode(),procurementProd.getName()));
		 }
	 }
	 sendAjaxResponse(jsonArray);
 }
 
 public void populateFarmers(){
	 List<Object[]> procurementProducts=productDistributionService.listProcurementProductFarmersByPMTReceiptNo(receiptNoId);
	 JSONArray jsonArray=new JSONArray();
	 if(!ObjectUtil.isListEmpty(procurementProducts)){
		 for(Object[] procurementProd:procurementProducts){
			 jsonArray.add(convertToJsonObject(procurementProd[0].toString(),procurementProd[1].toString()+" "+procurementProd[2].toString()));
		 }
	 }
	 sendAjaxResponse(jsonArray);
 }
public String populateStock(){

    JSONObject products = new JSONObject();
    JSONArray productArray = new JSONArray();
    if(!StringUtil.isEmpty(selectedCoOperative)&&selectedCoOperative!=null && !StringUtil.isEmpty(receiptNoId))  {
    	
    	
        List<PMTDetail> pmtDetail = productDistributionService.listPMTDetailByProductIdAndReceiptNo(Long.valueOf(selectedCoOperative),receiptNoId);
            JSONObject productJSON = new JSONObject();
            JSONArray procurementGradesJSON = new JSONArray();
            for (PMTDetail detail : pmtDetail) {
                JSONObject procurementGradeJSON = new JSONObject();
                procurementGradeJSON.put("procurementProdId", detail.getProcurementGrade().getProcurementVariety().getProcurementProduct().getId());
                procurementGradeJSON.put("procurementProdName", detail.getProcurementGrade().getProcurementVariety().getProcurementProduct().getName());
                procurementGradeJSON.put("unit", detail.getProcurementGrade().getProcurementVariety().getProcurementProduct().getUnit());
                procurementGradeJSON.put("procurementVarietyName", detail.getProcurementGrade().getProcurementVariety().getName());
                procurementGradeJSON.put("gradeName", detail.getProcurementGrade().getName());
                procurementGradeJSON.put("gradeId", detail.getProcurementGrade().getId());
                procurementGradeJSON.put("mtntBags", detail.getMtnrNumberOfBags());
                procurementGradeJSON.put("mtntGrssWeight", detail.getMtnrGrossWeight());
                //procurementGradeJSON.put("mtnrWeight", detail.getMtnrGrossWeight());
                procurementGradeJSON.put("id", detail.getId());
                procurementGradesJSON.add(procurementGradeJSON);

            }

            productJSON.put("procurementGrades", procurementGradesJSON);
            productArray.add(productJSON);

       // }
        products.put("products", productArray);
    }
   
    try {
        // sendResponse(products);
        printAjaxResponse(products, "text/html");
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return null;
}
 @SuppressWarnings("unchecked")
 protected JSONObject convertToJsonObject(Object id, Object name) {

     JSONObject jsonObject = new JSONObject();
     jsonObject.put("id", id);
     jsonObject.put("name", name);
     return jsonObject;
 }

	/**
     * Initialize procurement print map.
     */
    private void initializePrintMap() {

        this.mtnrPrintMap = new HashMap<String, Object>();
        List<Map<String, Object>> productMapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> totalMap = new LinkedHashMap<String, Object>();
        this.mtnrPrintMap.put("recNo", "");
        this.mtnrPrintMap.put("date", "");
        this.mtnrPrintMap.put("agentId", "");
        this.mtnrPrintMap.put("agentName", "");
        this.mtnrPrintMap.put("product", "");
        this.mtnrPrintMap.put("productMapList", productMapList);
        this.mtnrPrintMap.put("totalInfo", totalMap);
        this.mtnrPrintMap.put("isAgent", false);
        this.mtnrPrintMap.put("truckId", "");
        this.mtnrPrintMap.put("driverName", "");
        this.mtnrPrintMap.put("warehouseName", "");
    }

    /**
     * Builds the transaction print map.
     * @param procurementMTNR the procurement
     */
    private void buildTransactionPrintMap(PMT procurementMTNR) {

        List<Map<String, Object>> productMapList = new ArrayList<Map<String, Object>>();
        if (!ObjectUtil.isEmpty(procurementMTNR)) {
            long noOfBagsSum = 0l;
            double netWeightSum = 0d;
            DateFormat df = new SimpleDateFormat(getGeneralDateFormat());
            if (!ObjectUtil.isEmpty(procurementMTNR.getMtnrTransferInfo())) {
                if (!StringUtil.isEmpty(procurementMTNR.getMtnrReceiptNumber())) {
                    this.mtnrPrintMap.put("recNo", procurementMTNR.getMtnrReceiptNumber());
                }
                if (!ObjectUtil.isEmpty(procurementMTNR.getMtnrDate())) {
                    this.mtnrPrintMap.put("date", df.format(procurementMTNR.getMtnrDate()));
                }
                if (!ObjectUtil.isEmpty(procurementMTNR.getCoOperative())
                        && !StringUtil.isEmpty(procurementMTNR.getCoOperative().getCode())) {
                    this.mtnrPrintMap.put("agentId", procurementMTNR.getCoOperative().getCode());
                }
                if (!ObjectUtil.isEmpty(procurementMTNR.getCoOperative())
                        && !StringUtil
                                .isEmpty(procurementMTNR.getCoOperative().getName())) {
                    this.mtnrPrintMap.put("agentName", procurementMTNR.getCoOperative().getName());
                }
            }
            this.mtnrPrintMap.put("tructId", procurementMTNR.getTruckId());
            this.mtnrPrintMap.put("driverName", procurementMTNR.getDriverName());
            //this.mtnrPrintMap.put("warehouseName", procurementMTNR.getCoOperative().getWarehouseName());
            this.mtnrPrintMap.put("productMapList", productMapList);
            if (!ObjectUtil.isListEmpty(procurementMTNR.getPmtDetails())) {
                for (PMTDetail pmtDetail : procurementMTNR.getPmtDetails()) {
                   /* if (StringUtil.isEmpty(String.valueOf(mtnrPrintMap.get("product")))
                            && !ObjectUtil.isEmpty(pmtDetail.getProcurementProduct())) {
                        this.mtnrPrintMap.put("product", !StringUtil.isEmpty(pmtDetail
                                .getProcurementProduct().getName()) ? pmtDetail
                                .getProcurementProduct().getName() : "");
                    }*/
                    Map<String, Object> productMap = new LinkedHashMap<String, Object>();
                    productMap.put("product", pmtDetail.getProcurementProduct().getName());
                    productMap.put("variety", pmtDetail.getProcurementGrade().getProcurementVariety().getName());
                    productMap.put("grade", pmtDetail.getProcurementGrade().getName());
                  if(!getCurrentTenantId().equals(ESESystem.PRATIBHA_TENANT_ID)){
                    productMap.put("noOfBags", pmtDetail.getMtnrNumberOfBags());
                  }
                    productMap.put("netWeight", CurrencyUtil.getDecimalFormat(pmtDetail
                            .getMtnrGrossWeight(), "##.000"));
          
                    noOfBagsSum += pmtDetail.getMtnrNumberOfBags();
                    netWeightSum += pmtDetail.getMtnrGrossWeight();

                    productMapList.add(productMap);
                }
            }
            Map<String, Object> totalMap = new LinkedHashMap<String, Object>();
            if(!getCurrentTenantId().equals(ESESystem.PRATIBHA_TENANT_ID)){
            totalMap.put("noOfBags", noOfBagsSum);
            }
            totalMap.put("netWeight", CurrencyUtil.getDecimalFormat(netWeightSum, "##.000"));
            this.mtnrPrintMap.put("totalInfo", totalMap);
        }
    }

    /**
     * Sets the grade input string.
     * @param gradeInputString the new grade input string
     */
    public void setGradeInputString(String gradeInputString) {

        this.gradeInputString = gradeInputString;
    }

    /**
     * Gets the grade input string.
     * @return the grade input string
     */
    public String getGradeInputString() {

        return gradeInputString;
    }

    /**
     * Sets the receipt number.
     * @param receiptNumber the new receipt number
     */
    public void setReceiptNumber(String receiptNumber) {

        this.receiptNumber = receiptNumber;
    }

    /**
     * Gets the receipt number.
     * @return the receipt number
     */
    public String getReceiptNumber() {

        return receiptNumber;
    }

    /**
     * Sets the grade master id list.
     * @param gradeMasterIdList the new grade master id list
     */
    public void setGradeMasterIdList(String gradeMasterIdList) {

        this.gradeMasterIdList = gradeMasterIdList;
    }

    /**
     * Sets the id generator.
     * @param idGenerator the new id generator
     */
    public void setIdGenerator(IUniqueIDGenerator idGenerator) {

        this.idGenerator = idGenerator;
    }

    /**
     * Gets the id generator.
     * @return the id generator
     */
    public IUniqueIDGenerator getIdGenerator() {

        return idGenerator;
    }

    /**
     * Sets the mtnr description.
     * @param mtnrDescription the new mtnr description
     */
    public void setMtnrDescription(String mtnrDescription) {

        this.mtnrDescription = mtnrDescription;
    }

    /**
     * Gets the mtnr description.
     * @return the mtnr description
     */
    public String getMtnrDescription() {

        return mtnrDescription;
    }

    /**
     * Sets the truck id.
     * @param truckId the new truck id
     */
    public void setTruckId(String truckId) {

        this.truckId = truckId;
    }

    /**
     * Gets the truck id.
     * @return the truck id
     */
    public String getTruckId() {

        return truckId;
    }

    /**
     * Sets the driver id.
     * @param driverId the new driver id
     */
    public void setDriverId(String driverId) {

        this.driverId = driverId;
    }

    /**
     * Gets the driver id.
     * @return the driver id
     */
    public String getDriverId() {

        return driverId;
    }

    /**
     * Sets the selected product.
     * @param selectedProduct the new selected product
     */
    public void setSelectedProduct(String selectedProduct) {

        this.selectedProduct = selectedProduct;
    }

    /**
     * Gets the selected product.
     * @return the selected product
     */
    public String getSelectedProduct() {

        return selectedProduct;
    }

    /**
     * Sets the product map.
     * @param productMap the product map
     */
    public void setProductMap(Map<String, String> productMap) {

        this.productMap = productMap;
    }

    /**
     * Gets the product map.
     * @return the product map
     */
    public Map<String, String> getProductMap() {

        return productMap;
    }

    /**
     * Gets the mtnr print map.
     * @return the mtnr print map
     */
    public HashMap<String, Object> getMtnrPrintMap() {

        return mtnrPrintMap;
    }

    /**
     * Sets the mtnr print map.
     * @param mtnrPrintMap the mtnr print map
     */
    public void setMtnrPrintMap(HashMap<String, Object> mtnrPrintMap) {

        this.mtnrPrintMap = mtnrPrintMap;
    }

    /**
     * Gets the agent list.
     * @return the agent list
     */
    public Map<String, String> getAgentList() {

        Map<String, String> returnMap = new LinkedHashMap<String, String>();
        List<Agent> agentLists = (List<Agent>) agentService
                .listAgentByAgentType(AgentType.FIELD_STAFF);
        if (!ObjectUtil.isListEmpty(agentLists)) {
            for (Agent agent : agentLists) {
                returnMap.put(agent.getPersonalInfo().getFirstName() + " "
                        + agent.getPersonalInfo().getLastName() + " - " + agent.getProfileId(),
                        agent.getPersonalInfo().getFirstName() + " "
                                + agent.getPersonalInfo().getLastName() + " - "
                                + agent.getProfileId());
            }
        }

        return returnMap;
    }

    /**
     * Gets the list warehouse.
     * @return the list warehouse
     */
    public Map<Long, String> getListWarehouse() {

        List<Warehouse> warehouseList = locationService.listWarehouse();

        Map<Long, String> warehouseDropDownList = new LinkedHashMap<Long, String>();
        for (Warehouse warehouse : warehouseList) {
            warehouseDropDownList.put(warehouse.getId(), warehouse.getName());
/*            warehouseDropDownList.put(warehouse.getId(), warehouse.getName() + " -  "
                    + warehouse.getCode());*/
        }
        return warehouseDropDownList;
    }
    
    

    /**
     * Gets the selected warehouse id.
     * @return the selected warehouse id
     */
    public String getSelectedWarehouseId() {

        return selectedWarehouseId;
    }

    /**
     * Sets the selected warehouse id.
     * @param selectedWarehouseId the new selected warehouse id
     */
    public void setSelectedWarehouseId(String selectedWarehouseId) {

        this.selectedWarehouseId = selectedWarehouseId;
    }

    /**
     * @see com.sourcetrace.esesw.view.WebTransactionAction#getAgentId()
     */
    public String getAgentId() {

        return agentId;
    }

    /**
     * @see com.sourcetrace.esesw.view.WebTransactionAction#setAgentId(java.lang.String)
     */
    public void setAgentId(String agentId) {

        this.agentId = agentId;
    }


	public void setListWarehouse(String listWarehouse) {
		this.listWarehouse = listWarehouse;
	}

	public long getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(long receiptNo) {
		this.receiptNo = receiptNo;
	}

	public long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public Map<Long, String> getListOfReceiptNo() {
		 Map<Long,String>receiptNosList=new HashMap<Long,String>();
		  if (warehouseId>0) {
			   List<Object[]>receiptNos = productDistributionService.listPMTReceiptNumberByWarehouseI(warehouseId);
			  for(Object[] obj:receiptNos){
				   receiptNosList.put((Long) obj[0],String.valueOf(obj[1]));
			   }
	        }
		return receiptNosList;
	}

	public void setListOfReceiptNo(Map<Long, String> listOfReceiptNo) {
		this.listOfReceiptNo = listOfReceiptNo;
	}

	public String getReceiptNoId() {
		return receiptNoId;
	}

	public void setReceiptNoId(String receiptNoId) {
		this.receiptNoId = receiptNoId;
	}

	public long getPmtDetailId() {
		return pmtDetailId;
	}

	public void setPmtDetailId(long pmtDetailId) {
		this.pmtDetailId = pmtDetailId;
	}

	public long getPmtId() {
		return pmtId;
	}

	public void setPmtId(long pmtId) {
		this.pmtId = pmtId;
	}

	public String getProductDetailsStr() {
		return productDetailsStr;
	}

	public void setProductDetailsStr(String productDetailsStr) {
		this.productDetailsStr = productDetailsStr;
	}

    public String getSeasonName() {
        
        seasonName=super.getCurrentSeasonsCode()+"-"+super.getCurrentSeasonsName();
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
    
        this.seasonName = seasonName;
    }
	
    
    public Map<Long, String> getListProduct() {
		 Map<Long,String>listProductList=new HashMap<Long,String>();
		 
		return listProductList;
	}
    
    
    public Map<Long, String> getListVariety() {
		 Map<Long,String>listVarietyList=new HashMap<Long,String>();
		  
		return listVarietyList;
	}
    
    
    public Map<Long, String> getListGrade() {
		 Map<Long,String>listGradeList=new HashMap<Long,String>();
		  
		return listGradeList;
	}
    
    public void populateGrade(){
   	 List<ProcurementGrade>procurementGrade=productDistributionService.listPMTProcurementGradeByVarietyIdAndProduct(receiptNoId,selectedProduct);
   	
   	 JSONArray jsonArray=new JSONArray();
   	 if(!ObjectUtil.isListEmpty(procurementGrade)){
   		 for(ProcurementGrade procurementGra:procurementGrade){
   			 jsonArray.add(convertToJsonObject(procurementGra.getId(),procurementGra.getName()));
   		 }
   	 }
   	
   	 sendAjaxResponse(jsonArray);
    }
public void populateUnit(){
	if(!StringUtil.isEmpty(selectedProduct)){
	String unt=productService.findProcurementProductUnitByProductCode(selectedProduct);
	
	   	 JSONObject jsonObj=new JSONObject();
	   	 jsonObj.put("unit", StringUtil.isEmpty(unt)?"":unt);
	   	sendAjaxResponse(jsonObj);
	}
}
	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public String getVarietyId() {
		return varietyId;
	}

	public void setVarietyId(String varietyId) {
		this.varietyId = varietyId;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getFarmer() {
		return farmer;
	}

	public void setFarmer(String farmer) {
		this.farmer = farmer;
	}

	public Map<String, String> getFarmerMap() {
		return farmerMap;
	}

	public void setFarmerMap(Map<String, String> farmerMap) {
		this.farmerMap = farmerMap;
	}

    public IFarmerService getFarmerService() {
    
        return farmerService;
    }

    public void setFarmerService(IFarmerService farmerService) {
    
        this.farmerService = farmerService;
    }

	public IProductService getProductService() {
		return productService;
	}

	public void setProductService(IProductService productService) {
		this.productService = productService;
	}

	public String getSelectedCoOperative() {
		return selectedCoOperative;
	}

	public void setSelectedCoOperative(String selectedCoOperative) {
		this.selectedCoOperative = selectedCoOperative;
	}
	
	
	public void populateTruck(){
		if(!StringUtil.isEmpty(receiptNoId)&& receiptNoId!=null){
			PMT pmt = productDistributionService.findPMTByReceiptNumber(receiptNoId);
			 JSONObject obj=new JSONObject();
			 if(!ObjectUtil.isEmpty(pmt)){
				 obj.put("truck", pmt.getTruckId());
				 obj.put("driver", pmt.getDriverName());
			}
			 sendAjaxResponse(obj);
		}
		
	 }
}
