/*
 * FarmerProductTransferAction.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.service;

import java.text.DateFormat;
import java.text.DecimalFormat;
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

import com.sourcetrace.eses.dao.FarmerDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.AgentType;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.profile.GradeMaster;
import com.sourcetrace.eses.order.entity.txn.CityWarehouse;
import com.sourcetrace.eses.order.entity.txn.CityWarehouseDetail;
import com.sourcetrace.eses.order.entity.txn.PMT;
import com.sourcetrace.eses.order.entity.txn.PMTAgentDetail;
import com.sourcetrace.eses.order.entity.txn.PMTDetail;
import com.sourcetrace.eses.order.entity.txn.TransferInfo;
import com.sourcetrace.eses.order.entity.txn.TripSheet;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IProductDistributionService;
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
public class FarmerProductTransferAction extends WebTransactionAction {

    private static final String PROCUREMENT_MTNR = "319";
    private static final DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
    private static final String RECEIPT_DATE_FORMAT = "dd-MM-yyyy";

    private ILocationService locationService;
    private IProductDistributionService productDistributionService;
    private IUniqueIDGenerator idGenerator;
    private IClientService clientService;
    private IFarmerService farmerService;

    private List<GradeMaster> gradeMasterList;
    private Map<String, String> productMap = new LinkedHashMap<String, String>();
    private Map<String, String> coOperativeMap = new LinkedHashMap<String, String>();
    private Map<String, String> recCoOperativeMap = new LinkedHashMap<String, String>();
    private Map<String, String> farmerMap = new LinkedHashMap<String, String>();
    private Map<String, String[]> returnMap = new LinkedHashMap<String, String[]>();
    private String gradeInputString;
    private String gradeMasterIdList;
    private String startDate;
    private String receiptNumber;
    private String mtnrDescription;
    private String truckId;
    private String driverId;
    private String selectedProduct;
    private String selectedCoOperative;
    private String selectedRecCoOperative;
    private HashMap<String, Object> mtnrPrintMap = new LinkedHashMap<String, Object>();
    private String seasonName;
    private String selectedFarmer;
    private String selectedVariety;
    private String selectedGrade;
    private List<Farmer> farmerList = new ArrayList<Farmer>();
    private List<ProcurementProduct> productList = new ArrayList<ProcurementProduct>();
    private List<ProcurementVariety> varietyList = new ArrayList<ProcurementVariety>();
    

    /**
     * Creates the.
     * @return the string
     */
    public String create() {

        request.setAttribute(HEADING, getText("create"));
        System.out.println("create");
        if (!StringUtil.isEmpty(selectedProduct)) {

            // Added for Handling Form ReSubmit - Please See at populateFarmerAccount() Method
            if (ObjectUtil
                    .isEmpty(request.getSession().getAttribute(
                            agentId + "_" + PROCUREMENT_MTNR + "_"
                                    + WebTransactionAction.IS_FORM_RESUBMIT))) {
                reset();
                return INPUT;
            }
            if (!ObjectUtil.isLong(selectedRecCoOperative)) {
                reset();
                return INPUT;
            }
            PMT proceurementMTNR = new PMT();
            PMT pmt = new PMT();

            Agent senderAgent = agentService.findAgentByProfileId(agentId);
            Agent recAgent = agentService.findAgentByCoOperative(Long
                    .parseLong(selectedRecCoOperative));
            // Warehouse recCoOperative = locationService.findWarehouseById(Long
            // .parseLong(selectedRecCoOperative));
            if (!ObjectUtil.isEmpty(recAgent) && !ObjectUtil.isEmpty(recAgent.getCooperative())) {
                proceurementMTNR.setMtnrTransferInfo(getTransferInfo(recAgent));
                proceurementMTNR.setMtnrDate(DateUtil.convertStringToDate(startDate, "MM/dd/yyyy"));
                proceurementMTNR.setAgentRef(recAgent);

                pmt.setMtntDate(DateUtil.convertStringToDate(startDate, "MM/dd/yyyy"));
                pmt.setMtntTransferInfo(getTransferInfo(senderAgent));
                pmt.setStatusCode(PMT.Status.MTNT.ordinal());
                pmt.setAgentRef(senderAgent);

                ProcurementProduct procurementProduct = productDistributionService
                        .findProcurementProductById(Long.valueOf(selectedProduct));
                if (!ObjectUtil.isEmpty(procurementProduct)) {
                    Map<String, String[]> gradeInputMap = formGradeInputMapFromString(gradeInputString);
                    proceurementMTNR.setPmtDetails(new LinkedHashSet<PMTDetail>());

                    if (!ObjectUtil.isEmpty(gradeInputMap)) {
                        if (gradeInputMap.size() > 0) {
                            Set<PMTDetail> pmtDetails = new HashSet<PMTDetail>();
                            pmt.setPmtDetails(pmtDetails);
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
                                    pmtDetail.setCoOperative(senderAgent.getCooperative());
                                    proceurementMTNR.getPmtDetails().add(pmtDetail);

                                    PMTDetail pmtDetail1 = new PMTDetail();
                                    pmtDetail1.setGradeMaster(gradeMaster);
                                    pmtDetail1.setProcurementProduct(procurementProduct);
                                    pmtDetail1.setMtntNumberOfBags(Long.valueOf(gradeValues[0]));
                                    pmtDetail1.setMtntGrossWeight(Double.valueOf(gradeValues[1]));
                                    pmtDetail1.setPmt(pmt);
                                    pmtDetail1.setCoOperative(recAgent.getCooperative());

                                    pmt.getPmtDetails().add(pmtDetail1);
                                }
                            }
                        }
                    }
                    pmt.setCoOperative(senderAgent.getCooperative());
                    pmt.setTruckId(truckId);
                    pmt.setDriverName(driverId);
                    pmt.setTrnType(PMT.TRN_TYPE_STOCK_TRNASFER);

                    proceurementMTNR.setCoOperative(recAgent.getCooperative());
                    proceurementMTNR.setTruckId(truckId);
                    proceurementMTNR.setDriverName(driverId);
                    proceurementMTNR.setStatusCode(PMT.Status.MTNR.ordinal());
                    proceurementMTNR.setStatusMessage(PMT.Status.MTNR.toString());
                    proceurementMTNR.setMtnrReceiptNumber(idGenerator.getMTNRReceiptNoSeq());
                    proceurementMTNR.setTrnType(PMT.TRN_TYPE_STOCK_TRNASFER);

                    if (!ObjectUtil.isListEmpty(proceurementMTNR.getPmtDetails())) {
                        if (isDetailsExists(pmt)) {
                            String mtntReceiptNumber = productDistributionService
                                    .addProcurementMTNT(pmt);
                            if (ObjectUtil.isEmpty(mtntReceiptNumber)) {
                                addActionError(getText("receiptno.empty"));
                            }
                        }

                        productDistributionService.editPMTForMTNR(proceurementMTNR);
                        // String receiptHtml = "<br/><a href=\"javascript:printReceipt(\\'"
                        // + proceurementMTNR.getMtnrReceiptNumber() + "\\')\" >"
                        // + getText("printReceipt") + "</a>";
                        // setMtnrDescription(getText("receiptNumber") + " : "
                        // + proceurementMTNR.getMtnrReceiptNumber() + getText("mtnrSuccess")
                        // + receiptHtml);

                        // buildTripSheetsAndPMTDetails(pmt, procurementProduct);

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
        startDate = df.format(currentDate.getTime());
        request.setAttribute(HEADING, getText("create"));
        setMtnrDescription(null);
        loadCoOperativeList();
    }

    //
    // private void buildTripSheetsAndPMTDetails(PMT pmt, ProcurementProduct product) {
    //
    // Set<TripSheet> tripSheets = getTripSheetsByNotInMTNTStatus(null);
    // if (getActionErrors().size() == 0) {
    // for (TripSheet tripSheet : tripSheets) {
    // tripSheet.setTransitStatus(TripSheet.TRANSIT_STATUS.MTNT.ordinal());
    // buildPMTDetailsAndCityWarehouseUpdateDetails(tripSheet, product, pmt);
    // }
    // pmt.setTripSheets(tripSheets);
    // }
    // }

    private void buildPMTDetailsAndCityWarehouseUpdateDetails(TripSheet tripSheet,
            ProcurementProduct product, PMT pmt) {

        if (!ObjectUtil.isEmpty(tripSheet) && !ObjectUtil.isEmpty(product)) {
            List<Object[]> result = productDistributionService
                    .listGradeInformationByTripSheetIdProductId(tripSheet.getId(), product.getId());
            if (result.size() > 0) {
                for (Object[] gradeInfoArray : result) {
                    if (gradeInfoArray.length >= 5) {

                        if (ObjectUtil.isListEmpty(pmt.getPmtDetails())) {
                            pmt.setPmtDetails(new LinkedHashSet<PMTDetail>());
                        }

                        boolean flag = false;

                        GradeMaster gradeMaster = findGradeMasterById(Long.valueOf(String
                                .valueOf(gradeInfoArray[0])));
                        long noOfBags = Long.parseLong(String.valueOf(gradeInfoArray[1]));
                        double grossWeight = Double.parseDouble(String.valueOf(gradeInfoArray[2]));

                        for (PMTDetail pmtDetail : pmt.getPmtDetails()) {
                            if (pmtDetail.getGradeMaster().getId() == gradeMaster.getId()
                                    && pmtDetail.getProcurementProduct().getId() == product.getId()) {
                                pmtDetail.setMtntNumberOfBags(pmtDetail.getMtntNumberOfBags()
                                        + noOfBags);
                                pmtDetail.setMtntGrossWeight(pmtDetail.getMtntGrossWeight()
                                        + grossWeight);
                                flag = true;
                                break;
                            }
                        }

                        if (!flag) {
                            PMTDetail pmtDetail = new PMTDetail();
                            pmtDetail.setGradeMaster(gradeMaster);
                            pmtDetail.setProcurementProduct(product);
                            pmtDetail.setMtntNumberOfBags(noOfBags);
                            pmtDetail.setMtntGrossWeight(grossWeight);
                            pmtDetail.setPmt(pmt);

                            pmt.getPmtDetails().add(pmtDetail);
                        }

                        String[] buyerAgentInfo = tripSheet.getBuyerName().split("-");
                        if (!ObjectUtil.isEmpty(buyerAgentInfo) && buyerAgentInfo.length > 0) {
                            if (pmt.getPmtAgentDetails() == null) {
                                pmt.setPmtAgentDetails(new ArrayList<PMTAgentDetail>());
                            }

                            PMTAgentDetail pmtAgentDetail = new PMTAgentDetail();
                            pmtAgentDetail.setCity(tripSheet.getCity());
                            pmtAgentDetail.setProduct(product);
                            pmtAgentDetail.setAgentId(buyerAgentInfo[0]);
                            pmtAgentDetail.setGradeMaster(gradeMaster);
                            pmtAgentDetail.setNoOfBags(noOfBags);
                            pmtAgentDetail.setGrossWeight(grossWeight);
                            pmt.getPmtAgentDetails().add(pmtAgentDetail);
                        }
                    }
                }
            }
        }
    }

    private GradeMaster findGradeMasterById(long id) {

        GradeMaster gradeMaster = null;
        if (!ObjectUtil.isListEmpty(getGradeMasterList())) {
            for (GradeMaster gm : getGradeMasterList()) {
                if (id == gm.getId()) {
                    gradeMaster = gm;
                    break;
                }
            }
        }
        return gradeMaster;
    }

    private boolean isDetailsExists(PMT pmt) {

        if (getActionErrors().size() == 0) {
            if (!ObjectUtil.isListEmpty(pmt.getPmtDetails())) {
                return true;
            } else if (ObjectUtil.isListEmpty(pmt.getPmtDetails())) {
                // LOGGER.info("METERIAL TRANSFER DETAIL SIZE IS EMPTY");
                addActionError(getText("empty.mtnt.details"));
                return false;
            }
            // } else if (ObjectUtil.isEmpty(pmt.getPmtAgentDetails())) {
            // // LOGGER.info("CITY WAREHOUSE DETAILS EMPTY");
            // addActionError(getText("empty.citywarehouse.details"));
            // return false;
            // }
        }
        return false;
    }

    /**
     * Load product list.
     */
    private void loadCoOperativeList() {

        // productMap = new LinkedHashMap<String, String>();
        // List<ProcurementProduct> productList =
        // productDistributionService.listProcurementProduct();
        // if (!ObjectUtil.isListEmpty(productList))
        // for (ProcurementProduct product : productList)
        // productMap.put(String.valueOf(product.getId()), product.getName() + " - "
        // + product.getCode());

        coOperativeMap = new LinkedHashMap<String, String>();
        List<Warehouse> coOperatives = locationService.listCoOperativeByAgent(agentId);
        if (!ObjectUtil.isListEmpty(coOperatives)) {
            for (Warehouse coOperative : coOperatives) {
                coOperativeMap.put(String.valueOf(coOperative.getId()), coOperative.getName()
                        + " - " + coOperative.getCode());
            }
        }
        recCoOperativeMap = new LinkedHashMap<String, String>();
        // coOperatives = locationService.listOfCooperatives();
        coOperatives = locationService.listCoOperativeWithManagers();
        if (!ObjectUtil.isListEmpty(coOperatives)) {
            for (Warehouse coOperative : coOperatives) {
                if (!coOperativeMap.containsKey(String.valueOf(coOperative.getId()))) {
                    recCoOperativeMap.put(String.valueOf(coOperative.getId()), coOperative
                            .getName()
                            + " - " + coOperative.getCode());
                }
            }
        }
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
        transferInfo.setTxnTime(new Date());
        transferInfo.setDeviceId(getText(NOT_APPLICABLE));
        transferInfo.setDeviceName(getText(NOT_APPLICABLE));
        transferInfo.setServicePointId(getText(NOT_APPLICABLE));
        transferInfo.setServicePointName(getText(NOT_APPLICABLE));
        return transferInfo;
    }

    private TransferInfo getTransferInfoDetail() {

        TransferInfo transferInfo = new TransferInfo();

        transferInfo.setTxnTime(new Date());
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
        
        if (!StringUtil.isEmpty(inputString)) {
            String[] inputArray = inputString.split("\\|");
            for (String gradeStr : inputArray) {
                String[] gradeDetailsArray = gradeStr.split("-");
                if (gradeDetailsArray.length == 5) {                 
                        returnMap.put(gradeDetailsArray[0], new String[] {gradeDetailsArray[1],
                                gradeDetailsArray[2], gradeDetailsArray[3], gradeDetailsArray[4] });
                }
            }
        }
        return returnMap;
    }
    
 private List<String> formGradeInputMapFromString1(String inputString) {
     
     if (!StringUtil.isEmpty(inputString)) {
         String[] inputArray = inputString.split("\\|");
         for (String gradeStr : inputArray) {
             String[] gradeDetailsArray = gradeStr.split("-");
             if (gradeDetailsArray.length == 5) { 
                 
                 
             }
                 
             }
        
    }
    return null;
 }
    /**
     * Populate products.
     * @return the string
     */
    public String populateProducts() {

        List<CityWarehouse> cityWarehouses = new ArrayList<CityWarehouse>();
        List<String> prods = new ArrayList<String>();
        StringBuffer sb = new StringBuffer();
        if (!StringUtil.isEmpty(selectedCoOperative) && ObjectUtil.isLong(selectedCoOperative)) {
            cityWarehouses = productDistributionService.listProductsByCoOperative(Long
                    .parseLong(selectedCoOperative));          
           
            for (int i = 0; i < cityWarehouses.size(); i++) {
                CityWarehouse cityWarehouse = cityWarehouses.get(i);
                String val = cityWarehouse.getProcurementProduct().getName() + " ("
                        + cityWarehouse.getProcurementProduct().getCode() + ")-"
                        + cityWarehouse.getProcurementProduct().getId();
                if (!prods.contains(val)) {
                    // double exQty = productDistributionService.findPrecurementProductStock(
                    // cityWarehouse.getAgentId(), cityWarehouse.getCoOperative().getId(),
                    // cityWarehouse.getProcurementProduct().getId());
                    /*
                     * double exNetQty =
                     * productDistributionService.findPrecurementProductStockNetWht(
                     * cityWarehouse.getAgentId(), cityWarehouse.getCoOperative().getId(),
                     * cityWarehouse.getProcurementProduct().getId());
                     */

                    double exNetQty = productDistributionService
                            .findPrecurementProductStockNetWhtByWarehouseIdAndProductId(
                                    cityWarehouse.getCoOperative().getId(), cityWarehouse
                                            .getProcurementProduct().getId());

                    if (exNetQty > 0) {
                        prods.add(val);
                        sb.append(val);
                        if ((i + 1) != cityWarehouses.size()) {
                            sb.append(",");
                        }
                    }
                }
            }
        }
        try {
            // sendResponse(prods);
            printAjaxResponse(sb.toString(), "text/html");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    public String populateProducts1() {

        List<CityWarehouse> cityWarehouses = new ArrayList<CityWarehouse>();
        List<String> prods = new ArrayList<String>();
        Farmer farmer = new Farmer();
        StringBuffer sb = new StringBuffer();
        if (!StringUtil.isEmpty(selectedFarmer)&& ObjectUtil.isLong(selectedFarmer) && 
                !StringUtil.isEmpty(selectedCoOperative)&& ObjectUtil.isLong(selectedCoOperative)) {
            //farmer = farmerService.findFarmerByFarmerCode(selectedFarmer);
            cityWarehouses = productDistributionService.listProductsByFarmerIdAndCooperativeId(Long.valueOf(selectedFarmer),Long.valueOf(selectedCoOperative));           
           
            for (int i = 0; i < cityWarehouses.size(); i++) {
                CityWarehouse cityWarehouse = cityWarehouses.get(i);
                String val = cityWarehouse.getProcurementProduct().getName() + " - "
                        + cityWarehouse.getProcurementProduct().getCode() + " - "
                        + cityWarehouse.getProcurementProduct().getId();
                if (!prods.contains(val)) {
                    // double exQty = productDistributionService.findPrecurementProductStock(
                    // cityWarehouse.getAgentId(), cityWarehouse.getCoOperative().getId(),
                    // cityWarehouse.getProcurementProduct().getId());
                    /*
                     * double exNetQty =
                     * productDistributionService.findPrecurementProductStockNetWht(
                     * cityWarehouse.getAgentId(), cityWarehouse.getCoOperative().getId(),
                     * cityWarehouse.getProcurementProduct().getId());
                     */

                    double exNetQty = productDistributionService
                            .findFarmerStockNetWgtByWarehouseIdAndFarmerId(
                                    cityWarehouse.getCoOperative().getId(), cityWarehouse
                                            .getFarmer().getFarmerId());

                    if (exNetQty > 0) {
                        prods.add(val);
                        sb.append(val);
                        if ((i + 1) != cityWarehouses.size()) {
                            sb.append(",");
                        }
                    }
                }
            }
        }
        try {
            // sendResponse(prods);
            printAjaxResponse(sb.toString(), "text/html");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public String populateVariety() {
        //ProcurementVariety procurementVariety = new ProcurementVariety();
        if (!StringUtil.isEmpty(selectedCoOperative)&& ObjectUtil.isLong(selectedCoOperative)&&
                !StringUtil.isEmpty(selectedFarmer)&& ObjectUtil.isLong(selectedFarmer)&&                 
                !StringUtil.isEmpty(selectedProduct)&& ObjectUtil.isLong(selectedProduct)) {
           // procurementVariety=productDistributionService.findProcurementVariertyByProductCode(selectedProduct);
            List<CityWarehouse>  cityWarehouses = productDistributionService.listProductsByProductId(Long.valueOf(selectedCoOperative),Long.valueOf(selectedFarmer),Long.valueOf(selectedProduct));    
        Map<String,String> varieties = new LinkedHashMap<>();
        for (int i = 0; i < cityWarehouses.size(); i++) {
            CityWarehouse cityWarehouse = cityWarehouses.get(i);
        List<ProcurementGrade> varietiesList = productDistributionService
                .listProcurementVarietyByGradeCode(cityWarehouse.getQuality());
        if (!ObjectUtil.isEmpty(varietiesList)) {
            for (ProcurementGrade procurementGrade : varietiesList) {
                //varieties.add(procurementVariety.getName() + " - " + procurementVariety.getCode());
                varieties.put(procurementGrade.getProcurementVariety().getId().toString(),
                        procurementGrade.getProcurementVariety().getName() + " - " + procurementGrade.getProcurementVariety().getCode());
            }
        }
        }
        printAjaxResponse(varieties, "text/html");
    }
    return null;
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
                result = (!ObjectUtil.isEmpty(procurementMTNT.getMtntDate()) ? df
                        .format(procurementMTNT.getMtntDate()) : "")
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
        List<GradeMaster> gradeMasterList = productDistributionService.listGradeMaster();
        for (GradeMaster gradeMaster : gradeMasterList) {
            if (!ObjectUtil.isEmpty(gradeMaster.getProduct())) {
                if (productsMap.containsKey(String.valueOf(gradeMaster.getProduct().getId()))) {
                    Set<GradeMaster> gradeMasters = new LinkedHashSet<GradeMaster>();
                    if (productsMap.get(String.valueOf(gradeMaster.getProduct().getId()))
                            .containsKey("grades")) {
                        gradeMasters = (Set<GradeMaster>) productsMap.get(
                                String.valueOf(gradeMaster.getProduct().getId())).get("grades");
                    } else {
                        productsMap.get(String.valueOf(gradeMaster.getProduct().getId())).put(
                                "grades", gradeMasters);
                    }
                    gradeMasters.add(gradeMaster);
                } else {
                    Map<String, Object> productsInfoMap = new LinkedHashMap<String, Object>();
                    Set<GradeMaster> gradeMasters = new LinkedHashSet<GradeMaster>();
                    gradeMasters.add(gradeMaster);
                    productsInfoMap.put("product", gradeMaster.getProduct());
                    productsInfoMap.put("grades", gradeMasters);
                    productsMap.put(String.valueOf(gradeMaster.getProduct().getId()),
                            productsInfoMap);
                }
            }
        }

        for (Map.Entry entry : productsMap.entrySet()) {
            Map<String, Object> productMap = (Map<String, Object>) entry.getValue();
            ProcurementProduct product = (ProcurementProduct) productMap.get("product");

            Set<GradeMaster> gradeMasters = (Set<GradeMaster>) productMap.get("grades");

            JSONObject productJSON = new JSONObject();

            productJSON.put("id", product.getId());
            productJSON.put("code", product.getCode());
            productJSON.put("name", product.getName());

            JSONArray gradesJSON = new JSONArray();
            for (GradeMaster gradeMaster : gradeMasters) {
                JSONObject gradeMasterJSON = new JSONObject();
                gradeMasterJSON.put("id", gradeMaster.getId());
                gradeMasterJSON.put("code", gradeMaster.getCode());
                gradeMasterJSON.put("name", gradeMaster.getName());
                gradesJSON.add(gradeMasterJSON);
            }
            productJSON.put("grades", gradesJSON);
            productArray.add(productJSON);
        }

        products.put("products", productArray);
        return products;
    }

    /**
     * Populate stock.
     * @return the string
     */
    @SuppressWarnings("unchecked")
    public String populateStock() {

        JSONObject products = new JSONObject();
        JSONArray productArray = new JSONArray();
        Map<String, Map<String, Object>> productsMap = new LinkedHashMap<String, Map<String, Object>>();

        if (ObjectUtil.isLong(selectedProduct)) {

            List<ProcurementGrade> procurementGradeList = productDistributionService
                    .listProcurementGradeByProcurementProductId(Long.parseLong(selectedProduct));

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
                    procurementGradeJSON.put("numberOfBags", "0");
                    procurementGradeJSON.put("grossWeight", "0");
                    procurementGradeJSON.put("procurementVarietyCode", procurementGrade
                            .getProcurementVariety().getCode());
                    procurementGradeJSON.put("procurementVarietyName", procurementGrade
                            .getProcurementVariety().getName());
                    procurementGradeJSON.put("procurementVarietyId", procurementGrade
                            .getProcurementVariety().getId());

                    if (ObjectUtil.isLong(selectedCoOperative)) {
                        CityWarehouse cityWarehouse = productDistributionService
                                .findCityWareHouseByWarehouseIdProcrmentGradeCodeAndProcurementProductId(
                                        Long.parseLong(selectedCoOperative), procurementGrade
                                                .getCode(), Long.valueOf(getSelectedProduct()));
                        if (!ObjectUtil.isEmpty(cityWarehouse)) {
                            if (cityWarehouse.getGrossWeight() > 0) {
                                procurementGradeJSON.put("numberOfBags", cityWarehouse
                                        .getNumberOfBags());
                                procurementGradeJSON.put("grossWeight", cityWarehouse
                                        .getGrossWeight());
                                procurementGradesJSON.add(procurementGradeJSON);
                            }
                        }
                    }

                }

                productJSON.put("procurementGrades", procurementGradesJSON);
                productArray.add(productJSON);

            }
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
        startDate = df.format(currentDate.getTime());
        request.setAttribute(HEADING, getText("create"));
        loadCoOperativeList();

    }

    /**
     * Gets the current date.
     * @return the current date
     */
    public String getCurrentDate() {

        Calendar currentDate = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
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
            DateFormat df = new SimpleDateFormat(RECEIPT_DATE_FORMAT);
            if (!ObjectUtil.isEmpty(procurementMTNR.getMtnrTransferInfo())) {
                if (!StringUtil.isEmpty(procurementMTNR.getMtnrReceiptNumber())) {
                    this.mtnrPrintMap.put("recNo", procurementMTNR.getMtnrReceiptNumber());
                }
                if (!ObjectUtil.isEmpty(procurementMTNR.getMtnrDate())) {
                    this.mtnrPrintMap.put("date", df.format(procurementMTNR.getMtnrDate()));
                }
                if (!ObjectUtil.isEmpty(procurementMTNR.getMtnrTransferInfo())
                        && !StringUtil.isEmpty(procurementMTNR.getMtnrTransferInfo().getAgentId())) {
                    this.mtnrPrintMap.put("agentId", procurementMTNR.getMtnrTransferInfo()
                            .getAgentId());
                }
                if (!ObjectUtil.isEmpty(procurementMTNR.getMtnrTransferInfo())
                        && !StringUtil
                                .isEmpty(procurementMTNR.getMtnrTransferInfo().getAgentName())) {
                    this.mtnrPrintMap.put("agentName", procurementMTNR.getMtnrTransferInfo()
                            .getAgentName());
                }
            }
            this.mtnrPrintMap.put("tructId", procurementMTNR.getTruckId());
            this.mtnrPrintMap.put("driverName", procurementMTNR.getDriverName());
            this.mtnrPrintMap.put("productMapList", productMapList);
            if (!ObjectUtil.isListEmpty(procurementMTNR.getPmtDetails())) {
                for (PMTDetail pmtDetail : procurementMTNR.getPmtDetails()) {
                    if (StringUtil.isEmpty(String.valueOf(mtnrPrintMap.get("product")))
                            && !ObjectUtil.isEmpty(pmtDetail.getProcurementProduct())) {
                        this.mtnrPrintMap.put("product", !StringUtil.isEmpty(pmtDetail
                                .getProcurementProduct().getName()) ? pmtDetail
                                .getProcurementProduct().getName() : "");
                    }
                    Map<String, Object> productMap = new LinkedHashMap<String, Object>();
                    productMap.put("grade", pmtDetail.getGradeMaster().getName());
                    productMap.put("noOfBags", pmtDetail.getMtnrNumberOfBags());
                    productMap.put("netWeight", CurrencyUtil.getDecimalFormat(pmtDetail
                            .getMtnrGrossWeight(), "##.000"));

                    noOfBagsSum += pmtDetail.getMtnrNumberOfBags();
                    netWeightSum += pmtDetail.getMtnrGrossWeight();

                    productMapList.add(productMap);
                }
            }
            Map<String, Object> totalMap = new LinkedHashMap<String, Object>();
            totalMap.put("noOfBags", noOfBagsSum);
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
     * Gets the co operative map.
     * @return the co operative map
     */
    public Map<String, String> getCoOperativeMap() {

        return coOperativeMap;
    }

    /**
     * Sets the co operative map.
     * @param coOperativeMap the co operative map
     */
    public void setCoOperativeMap(Map<String, String> coOperativeMap) {

        this.coOperativeMap = coOperativeMap;
    }

    /**
     * Gets the rec co operative map.
     * @return the rec co operative map
     */
    public Map<String, String> getRecCoOperativeMap() {

        return recCoOperativeMap;
    }

    /**
     * Sets the rec co operative map.
     * @param recCoOperativeMap the rec co operative map
     */
    public void setRecCoOperativeMap(Map<String, String> recCoOperativeMap) {

        this.recCoOperativeMap = recCoOperativeMap;
    }

    /**
     * Gets the selected co operative.
     * @return the selected co operative
     */
    public String getSelectedCoOperative() {

        return selectedCoOperative;
    }

    /**
     * Sets the selected co operative.
     * @param selectedCoOperative the new selected co operative
     */
    public void setSelectedCoOperative(String selectedCoOperative) {

        this.selectedCoOperative = selectedCoOperative;
    }

    /**
     * Gets the selected rec co operative.
     * @return the selected rec co operative
     */
    public String getSelectedRecCoOperative() {

        return selectedRecCoOperative;
    }

    /**
     * Sets the selected rec co operative.
     * @param selectedRecCoOperative the new selected rec co operative
     */
    public void setSelectedRecCoOperative(String selectedRecCoOperative) {

        this.selectedRecCoOperative = selectedRecCoOperative;
    }

    /**
     * Gets the list warehouse.
     * @return the list warehouse
     */
    public Map<Long, String> getListWarehouse() {

        List<Warehouse> warehouseList = locationService.listWarehouse();

        Map<Long, String> warehouseDropDownList = new LinkedHashMap<Long, String>();
        for (Warehouse warehouse : warehouseList) {
            warehouseDropDownList.put(warehouse.getId(), warehouse.getName() + " -  "
                    + warehouse.getCode());
        }
        return warehouseDropDownList;
    }

    /**
     * Populate product transfer.
     */
    public void populateProductTransfer() {

        System.out.println("populateProductTransfer");
        String result = "";
        if (StringUtil.isEmpty(selectedCoOperative)) {

        } else if (!StringUtil.isEmpty(selectedCoOperative)) {

            Warehouse senderWarehouse = locationService.findWarehouseById(Long
                    .valueOf(getSelectedCoOperative()));
            if (ObjectUtil.isEmpty(senderWarehouse)) {
                result = "senderWarehouse.unavailable";
            }
            Warehouse receiverWarehouse = locationService.findWarehouseById(Long
                    .valueOf(getSelectedRecCoOperative()));
            if (ObjectUtil.isEmpty(receiverWarehouse)) {
                result = "receiverWarehouse.unavailable";
            }

            PMT proceurementMTNR = new PMT();
            PMT pmt = new PMT();

            // Agent senderAgent = agentService.findAgentByProfileId(agentId);
            // Agent recAgent =
            // agentService.findAgentByCoOperative(Long.parseLong(selectedRecCoOperative));
            // Warehouse recCoOperative = locationService.findWarehouseById(Long
            // .parseLong(selectedRecCoOperative));
            // if (!ObjectUtil.isEmpty(recAgent) && !ObjectUtil.isEmpty(recAgent.getCooperative()))
            // {
            proceurementMTNR.setMtnrTransferInfo(getTransferInfoDetail());
            proceurementMTNR.setMtnrDate(DateUtil.convertStringToDate(startDate, "MM/dd/yyyy"));
            // proceurementMTNR.setAgentRef(recAgent);

            pmt.setMtntDate(DateUtil.convertStringToDate(startDate, "MM/dd/yyyy"));
            // pmt.setMtntTransferInfo(getTransferInfo(senderAgent));
            pmt.setStatusCode(PMT.Status.MTNT.ordinal());
            // pmt.setAgentRef(senderAgent);
            Warehouse warehouse =locationService.findWarehouseById(Long.valueOf(selectedCoOperative));
           /* Farmer farmer=farmerService.findFarmerByFarmerId(selectedFarmer);
            ProcurementProduct procurementProduct = productDistributionService
                    .findProcurementProductById(Long.valueOf(getSelectedProduct()));*/
            if (!ObjectUtil.isEmpty(warehouse)) {
               // Map<String, String[]> gradeInputMap = formGradeInputMapFromString(gradeInputString);
                proceurementMTNR.setPmtDetails(new LinkedHashSet<PMTDetail>());

                if (!ObjectUtil.isEmpty(gradeInputString)) {
                    if (!StringUtil.isEmpty(gradeInputString)) {
                        Set<PMTDetail> pmtDetails = new HashSet<PMTDetail>();
                        pmt.setPmtDetails(pmtDetails);
                        
                        String[] inputArray = gradeInputString.split("\\|");
                        for (String gradeStr : inputArray) {
                            String[] gradeDetailsArray = gradeStr.split("-");
                            if (gradeDetailsArray.length == 5) { 
                                
                                
                       
                        
                       /* for (Entry<String, String[]> entry : gradeInputMap.entrySet()) {
                            String id = entry.getKey();
                            String[] gradeValues = entry.getValue();*/
                            Farmer farmer=farmerService.findFarmerById(Long.valueOf(gradeDetailsArray[0]));
                            ProcurementProduct procurementProduct = productDistributionService
                                    .findProcurementProductById(Long.valueOf(gradeDetailsArray[1]));
                            
                         ProcurementGrade procurementGrade = productDistributionService
                                    .findProcurementGradeById(Long.valueOf(gradeDetailsArray[2]));
                         
                         
                         
                           // if (!ObjectUtil.isEmpty(id)) {
                                PMTDetail pmtDetail = new PMTDetail();
                                pmtDetail.setPmt(proceurementMTNR);
                                pmtDetail.setFarmer(farmer);
                                pmtDetail.setProcurementProduct(procurementProduct);
                                pmtDetail.setProcurementGrade(procurementGrade);
                                pmtDetail.setMtnrNumberOfBags(Long.valueOf(gradeDetailsArray[3]));
                                pmtDetail.setMtnrGrossWeight(Double.valueOf(gradeDetailsArray[4]));
                                pmtDetail.setCoOperative(senderWarehouse);
                                proceurementMTNR.getPmtDetails().add(pmtDetail);

                                PMTDetail pmtDetail1 = new PMTDetail();
                                pmtDetail1.setFarmer(farmer);
                                pmtDetail1.setProcurementGrade(procurementGrade);
                                pmtDetail1.setProcurementProduct(procurementProduct);
                                pmtDetail1.setMtntNumberOfBags(Long.valueOf(gradeDetailsArray[3]));
                                pmtDetail1.setMtntGrossWeight(Double.valueOf(gradeDetailsArray[4]));
                                pmtDetail1.setPmt(pmt);
                                pmtDetail1.setCoOperative(receiverWarehouse);

                                pmt.getPmtDetails().add(pmtDetail1);
                            }
                        }
                    }
                }
                pmt.setCoOperative(senderWarehouse);
                pmt.setTruckId(truckId);
                pmt.setDriverName(driverId);
                pmt.setTrnType(PMT.TRN_TYPE_STOCK_TRNASFER);
                pmt.setSeasonCode(clientService.findCurrentSeasonCode());

                proceurementMTNR.setCoOperative(receiverWarehouse);
                proceurementMTNR.setTruckId(truckId);
                proceurementMTNR.setDriverName(driverId);
                proceurementMTNR.setStatusCode(PMT.Status.MTNR.ordinal());
                proceurementMTNR.setStatusMessage(PMT.Status.MTNR.toString());
                proceurementMTNR.setMtnrReceiptNumber(idGenerator.getMTNRReceiptNoSeq());
                proceurementMTNR.setTrnType(PMT.TRN_TYPE_STOCK_TRNASFER);
                proceurementMTNR.setSeasonCode(clientService.findCurrentSeasonCode());

                if (!ObjectUtil.isListEmpty(proceurementMTNR.getPmtDetails())) {
                    if (isDetailsExists(pmt)) {
                        String mtntReceiptNumber = productDistributionService
                                .addProcurementTransfer(pmt);
                        receiptNumber = mtntReceiptNumber;

                        if (ObjectUtil.isEmpty(mtntReceiptNumber)) {
                            result = getText("receiptno.empty");
                        }
                    }

                    //productDistributionService.editPMTForMTNR(proceurementMTNR);

                } else {
                    setMtnrDescription(getText("mtnrFailed") + " : " + getText("grades.not.exists"));
                }

            } else {

            }
            if (StringUtil.isEmpty(result)) {
                setMtnrDescription(getText("productTransferSucess") + "</br>"
                        + getText("receiptNumber") + " : " + receiptNumber);

                printAjaxResponse(getMtnrDescription(), "text/html");
            } else {
                setMtnrDescription(getText(result));
                printAjaxResponse(getMtnrDescription(), "text/html");
            }
        }
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
     * @see com.sourcetrace.esesw.view.SwitchAction#list()
     */
    public String list() {

        request.setAttribute(HEADING, getText("create.page"));

        return REDIRECT;
    }

    public IClientService getClientService() {
    
        return clientService;
    }

    public void setClientService(IClientService clientService) {
    
        this.clientService = clientService;
    }

    public String getSeasonName() {
        seasonName=super.getCurrentSeasonsCode()+"-"+super.getCurrentSeasonsName();
    
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
    
        this.seasonName = seasonName;
    }

   public Map<String, String> getFarmerMap() {
    
        return farmerMap;
    }

    public void setFarmerMap(Map<String, String> farmerMap) {
    
        this.farmerMap = farmerMap;
    }
    
    public String populateFarmers() {

        List<CityWarehouse> cityWarehouses = new ArrayList<CityWarehouse>();
        List<String> prods = new ArrayList<String>();
        StringBuffer sb = new StringBuffer();
        if (!StringUtil.isEmpty(selectedCoOperative) && ObjectUtil.isLong(selectedCoOperative)) {
            cityWarehouses = productDistributionService.listFarmersByCoOperative(Long
                    .parseLong(selectedCoOperative));
            for (int i = 0; i < cityWarehouses.size(); i++) {
                CityWarehouse cityWarehouse = cityWarehouses.get(i);
                String val = "";
                if(!ObjectUtil.isEmpty(cityWarehouse.getFarmer())){
                val = cityWarehouse.getFarmer().getName() + " - "+ cityWarehouse.getFarmer().getFarmerCode() + " - "+ cityWarehouse.getFarmer().getId();
                }
                if (!prods.contains(val)) {
                    
                    prods.add(val);
                    sb.append(val);
                    if ((i + 1) != cityWarehouses.size()) {
                        sb.append(",");
                    }
                }
            }
        }
        try {
            // sendResponse(prods);
            printAjaxResponse(sb.toString(), "text/html");
        } catch (Exception e) {            
            e.printStackTrace();
        }
        return null;
    }

    public String getSelectedFarmer() {
    
        return selectedFarmer;
    }

    public void setSelectedFarmer(String selectedFarmer) {
    
        this.selectedFarmer = selectedFarmer;
    }
    
    public void populateStocks() throws Exception { 
        DecimalFormat formatter = new DecimalFormat("###.000");
        String stock="";
       // Farmer farmer=new Farmer();
        if (!StringUtil.isEmpty(selectedCoOperative) && !StringUtil.isEmpty(selectedFarmer) && !StringUtil.isEmpty(selectedProduct)
                && !StringUtil.isEmpty(selectedGrade) ) {
           // farmer = farmerService.findFarmerByFarmerCode(selectedFarmer);
            
           // ProcurementProduct procurementProduct = productDistributionService.findProcurementProductById(Long.valueOf(selectedProduct));
            ProcurementGrade procurementGrade = productDistributionService.findProcurementGradeById(Long.valueOf(selectedGrade));
          CityWarehouse cityWareHouse = productDistributionService.listStockByFarmerIdProductIdGradeCodeAndCooperativeId(Long.valueOf(selectedCoOperative),Long.valueOf(selectedFarmer),
                  Long.valueOf(selectedProduct),procurementGrade.getCode());
            if (!ObjectUtil.isEmpty(cityWareHouse)) {
                 stock = String.valueOf(cityWareHouse.getNumberOfBags()) +" Bags - "+ formatter.format(Double.parseDouble(formatter.format(Double.parseDouble(String
                         .valueOf((cityWareHouse.getGrossWeight()))))));
            }
        }
        sendResponse(stock);
    }

    public IFarmerService getFarmerService() {
    
        return farmerService;
    }

    public void setFarmerService(IFarmerService farmerService) {
    
        this.farmerService = farmerService;
    }

   @SuppressWarnings("unchecked")
public String populateGrade() {   

       if (!StringUtil.isEmpty(selectedCoOperative) && !StringUtil.isEmpty(selectedFarmer) && !StringUtil.isEmpty(selectedProduct) && !StringUtil.isEmpty(selectedVariety)){ 
           Map<String,String> grades = new LinkedHashMap<>(); 
           
           List<ProcurementGrade> gradeListt = productDistributionService
                   .listProcurementGradeByProcurementVarietyId(Long.valueOf(selectedVariety));
           if (!ObjectUtil.isEmpty(gradeListt)) {
               for (ProcurementGrade grade : gradeListt) {
           
           List<CityWarehouse> cityWareHouses= productDistributionService.listGradeByWarehouseIdFarmerIdProductIdAndVarietyCode(Long.valueOf(selectedCoOperative),Long.valueOf(selectedFarmer),
                   Long.valueOf(selectedProduct),grade.getCode());
           for (int i = 0; i < cityWareHouses.size(); i++) {
               CityWarehouse cityWarehouse = cityWareHouses.get(i);
               List<ProcurementGrade> gradeList = productDistributionService
                       .listProcurementVarietyByGradeCode(cityWarehouse.getQuality());
               if (!ObjectUtil.isEmpty(gradeList)) {
                   for (ProcurementGrade procurementGrade : gradeList) {
                       grades.put(procurementGrade.getId().toString(),
                               procurementGrade.getName() + " - " + procurementGrade.getCode());
                   }
               }
           }
               }
           printAjaxResponse(grades, "text/html");
   
           }
   
       }  
       return null;
}
   public void populateEditFarmer() throws Exception {

       if (!selectedFarmer.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedFarmer))) {
           farmerList = farmerService.listFarmerByFarmerId(Long.valueOf(selectedFarmer));
       }

       JSONArray varArr = new JSONArray();
       if (!ObjectUtil.isEmpty(farmerList)) {
           for (Farmer farmer : farmerList) {
               varArr.add(getJSONObject(farmer.getId(), farmer.getName()+"-"+farmer.getFarmerCode()));
           }
       }
       sendAjaxResponse(varArr);
   
   
}
   public void populateEditProduct() throws Exception {

       if (!selectedProduct.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedProduct))) {
           productList = productDistributionService.listProcurementProductByProductId(Long.valueOf(selectedProduct));
       }

       JSONArray varArr = new JSONArray();
       if (!ObjectUtil.isEmpty(productList)) {
           for (ProcurementProduct product : productList) {
               varArr.add(getJSONObject(product.getId(), product.getName()+"-"+product.getCode()));
           }
       }
       sendAjaxResponse(varArr);
   
   
}
   public void populateEditVariety() throws Exception {

       if (!selectedVariety.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedVariety))) {
           varietyList = productDistributionService.listProcurementProductVarietyByVarietyId(Long.valueOf(selectedVariety));
       }

       JSONArray varArr = new JSONArray();
       if (!ObjectUtil.isEmpty(varietyList)) {
           for (ProcurementVariety varierty : varietyList) {
               varArr.add(getJSONObject(varierty.getId(), varierty.getName()+"-"+varierty.getCode()));
           }
       }
       sendAjaxResponse(varArr);
   
   
}
   @SuppressWarnings("unchecked")
   protected JSONObject getJSONObject(Object id, Object name) {

       JSONObject jsonObject = new JSONObject();
       jsonObject.put("id", id);
       jsonObject.put("name", name);
       return jsonObject;
   }
public List<Farmer> getFarmerList() {

    return farmerList;
}

public void setFarmerList(List<Farmer> farmerList) {

    this.farmerList = farmerList;
}

public List<ProcurementProduct> getProductList() {

    return productList;
}

public void setProductList(List<ProcurementProduct> productList) {

    this.productList = productList;
}



public String getSelectedVariety() {

    return selectedVariety;
}

public void setSelectedVariety(String selectedVariety) {

    this.selectedVariety = selectedVariety;
}

public List<ProcurementVariety> getVarietyList() {

    return varietyList;
}

public void setVarietyList(List<ProcurementVariety> varietyList) {

    this.varietyList = varietyList;
}

public String getSelectedGrade() {

    return selectedGrade;
}

public void setSelectedGrade(String selectedGrade) {

    this.selectedGrade = selectedGrade;
}  


   
}
