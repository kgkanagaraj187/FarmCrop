/*
 * PaymentAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.PaymentMode;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.profile.Vendor;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.view.WebTransactionAction;



public class PaymentAction extends WebTransactionAction {

    private static final long serialVersionUID = 1L;
    public static final String PROCUREMENT_BALANCE = "PROCUREMENT_BALANCE";
    public static final String DISTRIBUTION_BALANCE = "DISTRIBUTION_BALANCE";
    public static final String CASH_ADVANCE = "CASH_ADVANCE";
    private static final String RECEIPT_DATE_FORMAT = "dd-MM-yyyy";

    private ILocationService locationService;
    private IFarmerService farmerService;
    private IProductDistributionService productDistributionService;
    private IAccountService accountService;
    private IUniqueIDGenerator idGenerator;

    private String id;
    private String selectedCity;
    private String selectedVillage;
    private String selectedFarmer;
    private String selectedSeason;
    private String pageNo;
    private String selectedPayment;
    private String paymentAmount;
    private String receiptNo;
    private String agentName;
    private String agentBalance;
    private String serialNumber;
    private String startDate;
    private String remarks;
    private String seleAgent;
    private Double cashCreditValue;

    List<String> villageList = new ArrayList<String>();
    List<String> farmerList = new ArrayList<String>();
    List<String> cityList = new ArrayList<String>();

    DecimalFormat formatter = new DecimalFormat("#.##");

    private Map<String, Object> agentInfoMap = new LinkedHashMap<String, Object>();
    private Map<String, Object> paymentMap = new LinkedHashMap<String, Object>();
    private Map<String,String> agentList = new HashMap<String,String>();
    private String selectedAgent;
	private String enableLoanModule;
	private String selectedId;
	private Double loanAmount;
	private Double loanBalance;
	private Double cashBalance;
	 private String farmerId;

    /**
     * Creates the.
     * @return the string
     */
    public String create() {
   
        Agent agent = null;
        ESEAccount agentAccount = null;
        ESESystem preferences = preferncesService.findPrefernceById("1");
    	setEnableLoanModule(preferences.getPreferences().get(ESESystem.ENABLE_LOAN_MODULE));

        if (id == null) {
            if (!StringUtil.isEmpty(agentId)) {
                request.setAttribute("agentId", agentId);
                agent = agentService.findAgentByProfileId(agentId);
                agentAccount = accountService.findAccountByProfileIdAndProfileType(agentId,
                        ESEAccount.AGENT_ACCOUNT);
                if (!ObjectUtil.isEmpty(agent) && ESETxn.WEB_BOD == agent.getBodStatus()) {
                    request.setAttribute(HEADING, getText("create.page"));
                    setAgentName(agent.getPersonalInfo().getAgentName());
                    /*
                     * setAgentBalance(agentAccount.getBalance() > 0 ? formatter.format(agentAccount
                     * .getBalance()) + " DR" :
                     * formatter.format(Math.abs(agentAccount.getBalance())) + " CR");
                     */
                    setAgentInfo(agentAccount);
                    setCurrentSeason();
                    setSelectedVillage(null);
                	
                	
                    return INPUT;
                }
            }
            return INPUT;
        } else {
            request.setAttribute(HEADING, getText("create.page"));
            if(!StringUtil.isEmpty(agentId)){
            agent = agentService.findAgentByProfileId(agentId);
            /** FETCHING AGENT ACCOUNT **/
            agentAccount = accountService.findAccountByProfileIdAndProfileType(agentId,
                    ESEAccount.AGENT_ACCOUNT);
            // Added for Handling Form ReSubmit - Please See at populateAccount() Method
            if (ObjectUtil.isEmpty(request.getSession().getAttribute(
                    agentId + "_" + PaymentMode.PROCURMENT_PAYMENT_TXN + "_"
                            + WebTransactionAction.IS_FORM_RESUBMIT))) {
                setAgentName(agent.getPersonalInfo().getAgentName());
                /*
                 * setAgentBalance(agentAccount.getBalance() > 0 ? formatter.format(agentAccount
                 * .getBalance()) + " DR" : formatter.format(Math.abs(agentAccount.getBalance())) +
                 * " CR");
                 */
                setAgentInfo(agentAccount);
                setCurrentSeason();
                setSelectedVillage(null);
                return INPUT;
            }
            }
            AgroTransaction agentPaymentTxn = new AgroTransaction();
            AgroTransaction farmerPaymentTxn = new AgroTransaction();

            String farmer[] = selectedFarmer.split("-");
            Season season = null;
            if(!ObjectUtil.isEmpty(selectedSeason)){
            season = productDistributionService.findSeasonById(Long.valueOf(selectedSeason));
            }

            if (StringUtil.isEmpty(pageNo))
                pageNo = AgroTransaction.EMPTY_PAGE_NO;

            receiptNo = idGenerator.getPaymentSeqId();
            /** FORMING AGRO TRANSACTION FOR AGENT **/
            agentPaymentTxn.setAgentId(agentId);
            agentPaymentTxn.setAgentName(agent.getPersonalInfo().getAgentName());
            agentPaymentTxn.setDeviceId(NOT_APPLICABLE);
            agentPaymentTxn.setDeviceName(NOT_APPLICABLE);
            agentPaymentTxn.setServicePointId(agent.getServicePoint().getCode());
            agentPaymentTxn.setServicePointName(agent.getServicePoint().getName());
            agentPaymentTxn.setFarmerId(agent.getProfileId());
            agentPaymentTxn.setFarmerName(agent.getPersonalInfo().getFirstName());
            agentPaymentTxn.setReceiptNo(receiptNo);
            agentPaymentTxn.setProfType(Profile.CO_OPEARATIVE_MANAGER);
            agentPaymentTxn.setOperType(ESETxn.ON_LINE);
            agentPaymentTxn.setTxnTime(DateUtil.convertStringToDate(startDate, "MM/dd/yyyy"));
            agentPaymentTxn.setTxnAmount(Double.parseDouble(paymentAmount));
            /** Transaction Type, Payment Mode, SeasonName, SeasonYear, PageNo, SerialNo **/
            agentPaymentTxn
                    .setTxnDesc(selectedPayment.toUpperCase() /*+ "|"
                            + season.getName() + "|" + season.getYear() + "|" + pageNo + "|"
                            + serialNumber*/);
            agentPaymentTxn.setSeasonCode(getCurrentSeasonCodeData());
            agentPaymentTxn.setBranch_id(getBranchId());

            /** FORMING AGRO TRANSACTION FOR FARMER **/
            farmerPaymentTxn.setAgentId(agentId);
            farmerPaymentTxn.setAgentName(agent.getPersonalInfo().getAgentName());
            farmerPaymentTxn.setDeviceId(NOT_APPLICABLE);
            farmerPaymentTxn.setDeviceName(NOT_APPLICABLE);
            farmerPaymentTxn.setServicePointId(agent.getServicePoint().getCode());
            farmerPaymentTxn.setServicePointName(agent.getServicePoint().getName());
            farmerPaymentTxn.setFarmerId(farmer[1]);
            farmerPaymentTxn.setFarmerName(farmer[0]);
            farmerPaymentTxn.setReceiptNo(receiptNo);
            farmerPaymentTxn.setProfType(Profile.CLIENT);
            farmerPaymentTxn.setOperType(ESETxn.ON_LINE);
            farmerPaymentTxn.setTxnTime(DateUtil.convertStringToDate(startDate, "MM/dd/yyyy"));
            farmerPaymentTxn.setTxnAmount(Double.parseDouble(paymentAmount));
            Farmer farmerEntity = farmerService.findFarmerByFarmerId(farmer[1]);
            ESEAccount farmerAccount = farmerService.findAccountBySeassonProcurmentProductFarmer(
                    0L, farmerEntity.getId());
            /** Transaction Type, Payment Mode, SeasonName, SeasonYear, PageNo, SerialNo **/
            farmerPaymentTxn
                    .setTxnDesc(selectedPayment.toUpperCase() /*+ "|"
                            + season.getName() + "|" + season.getYear() + "|" + pageNo + "|"
                            + serialNumber*/);
            farmerPaymentTxn.setSeasonCode(getCurrentSeasonCodeData());
            farmerPaymentTxn.setBranch_id(getBranchId());

            // Adding remarks to the Transaction description
           if (!StringUtil.isEmpty(remarks)) {
                agentPaymentTxn.setTxnDesc(agentPaymentTxn.getTxnDesc() + "|" + remarks);
                farmerPaymentTxn.setTxnDesc(farmerPaymentTxn.getTxnDesc() + "|" + remarks);
            }
            // Added txn insufficient balance check
            if (!PaymentMode.DISTRIBUTION_ADVANCE_PAYMENT_MODE_NAME
                    .equalsIgnoreCase(selectedPayment)) {
                agentPaymentTxn.setTxnType(PaymentMode.PROCURMENT_PAYMENT_TXN);
                farmerPaymentTxn.setTxnType(PaymentMode.PROCURMENT_PAYMENT_TXN);
            } else {
                agentPaymentTxn.setTxnType(PaymentMode.DISTIBUTION_PAYMENT_TXN);
                farmerPaymentTxn.setTxnType(PaymentMode.DISTIBUTION_PAYMENT_TXN);
            }
            // Setting Balance for Agent and Farmer
            farmerPaymentTxn.setAccount(farmerAccount);
            agentPaymentTxn.setAccount(agentAccount);
            // Save and Update Payment Txn and Account
            productDistributionService.saveAgroTransactionForPayment(farmerPaymentTxn,
                    agentPaymentTxn);

           /* AgroTimerTask myTask = (AgroTimerTask) request.getSession().getAttribute(
                    agentId + "_timer");
            setReStartTxn(true);
            setReceiptNo(farmerPaymentTxn.getReceiptNo());
            myTask.cancelTimer(false);*/
            request.setAttribute("agentId", agentId);
            request.getSession().removeAttribute(
                    agentId + "_" + PaymentMode.PROCURMENT_PAYMENT_TXN + "_"
                            + WebTransactionAction.IS_FORM_RESUBMIT);
            setAgentName(agent.getPersonalInfo().getAgentName());
            /*
             * setAgentBalance(agentAccount.getBalance() > 0 ? formatter.format(agentAccount
             * .getBalance()) + " DR" : formatter.format(Math.abs(agentAccount.getBalance())) +
             * " CR");
             */
            setAgentInfo(agentAccount);
            setCurrentSeason();
            setSelectedVillage(null);
            return INPUT;
        }

    }

    /**
     * Gets the city list.
     * @return the city list
     */
    @SuppressWarnings("unchecked")
    public List<String> getCityList() {

        List<Municipality> cities = locationService.listCity();
        if (!ObjectUtil.isEmpty(cities)) {
            for (Municipality city : cities) {
                cityList.add(city.getName() + "-" + city.getCode());
            }
        }
        return cityList;

    }

    /**
     * Gets the village list.
     * @return the village list
     */
    public List<String> getVillageList() {

        if (!StringUtil.isEmpty(selectedCity)) {
            String cityCode[] = selectedCity.split("-");
            List<Village> listVillage = locationService.listVillages(cityCode[1]);
            if (!ObjectUtil.isListEmpty(listVillage)) {
                for (Village village : listVillage) {
                    villageList.add(village.getName() + "-" + village.getCode());
                }
            }
        }
        return villageList;
    }

    /**
     * Populate village.
     * @return the string
     * @throws Exception the exception
     */
    public String populateVillage() throws Exception {
        JSONArray villageArr = new JSONArray();

        if (!ObjectUtil.isEmpty(selectedCity) && (!StringUtil.isEmpty(selectedCity))) {
            String cityCode[] = selectedCity.split("-");
            List<Village> listVillage = locationService.listVillagesByCityCode(cityCode[1]);            
            if (!ObjectUtil.isListEmpty(listVillage)) {
                for (Village village : listVillage) {
                    villageArr.add(getJSONObject(village.getName() + "-" + village.getCode(), village.getName() + "-" + village.getCode()));
                }
            }
        }
        sendAjaxResponse(villageArr);
        return null;
    }

    /**
     * Populate farmer.
     * @return the string
     * @throws Exception the exception
     */
    public String populateFarmer() throws Exception {
        JSONArray farmerArr = new JSONArray();
        String currentSeasonCode = getCurrentSeasonCode();
        if (!ObjectUtil.isEmpty(selectedVillage) && (!StringUtil.isEmpty(selectedVillage) && (!selectedVillage.equals("0")))
                && !StringUtil.isEmpty(currentSeasonCode)) {
            String[] villageCode = selectedVillage.split("-");
           /* List<Farmer> listFarmer = farmerService
                    .listActiveContractFarmersByVillageCodeSeasonCode(villageCode[1],
                            currentSeasonCode);*/
            List<Farmer> listFarmer = farmerService.findFarmerByVillageCode(villageCode[1]);
            if (!ObjectUtil.isListEmpty(listFarmer)) {
                for (Farmer farmer : listFarmer) {                   
                    farmerArr.add(getJSONObject(farmer.getFirstName() + " " + farmer.getLastName()+"-"+farmer.getFarmerId(),farmer.getFirstName() + " " 
                + farmer.getLastName()+"-"+farmer.getFarmerId()));
                }
            }
        }
        sendAjaxResponse(farmerArr);
        return null;
    }

    /**
     * Gets the current season code.
     * @return the current season code
     */
    private String getCurrentSeasonCode() {

        ESESystem preference = preferncesService.findPrefernceById("1");
        return preference.getPreferences().get(ESESystem.CURRENT_SEASON_CODE);
    }

    private String getCurrentSeasonCodeData() {

		String currentSessionCode = clientService.findCurrentSeasonCode();
		return currentSessionCode;
	
    }
    /**
     * Gets the farmer list.
     * @return the farmer list
     */
    public List<String> getFarmerList() {

        String currentSeasonCode = getCurrentSeasonCode();
        if (!StringUtil.isEmpty(selectedVillage) && !StringUtil.isEmpty(currentSeasonCode)) {
            String[] villageCode = selectedVillage.split("-");
            List<Farmer> listFarmer = farmerService
                    .listActiveContractFarmersByVillageCodeSeasonCode(villageCode[1],
                            currentSeasonCode);
            if (!ObjectUtil.isListEmpty(listFarmer)) {
                for (Farmer farmer : listFarmer) {
                    farmerList.add(farmer.getFirstName() + " " + farmer.getLastName() + "-"
                            + farmer.getFarmerId());
                }
            }
        }
        return farmerList;
    }

    /**
     * Gets the season list.
     * @return the season list
     */
    public Map<String, String> getSeasonList() {

        List<Season> seasons = productDistributionService.listSeasons();
        Map<String, String> returnMap = new LinkedHashMap<String, String>();
        if (!ObjectUtil.isListEmpty(seasons)) {
            for (Season season : seasons) {
                returnMap.put(String.valueOf(season.getId()), season.getName() + "-"
                        + season.getYear());
            }
        }
        return returnMap;
    }

    /**
     * Gets the payment list.
     * @return the payment list
     */
   /* public List<String> getPaymentList() {

        List<PaymentMode> modes = productDistributionService.listPaymentMode();
        List<String> paymentList = new ArrayList<String>();
        if (!ObjectUtil.isListEmpty(modes)) {
            for (PaymentMode mode : modes) {
                paymentList.add(mode.getName());
            }
        }
        return paymentList;
    }*/
    public Map<String, String> getPaymentList() {
    	  ESESystem preferences = preferncesService.findPrefernceById("1");
      	setEnableLoanModule(preferences.getPreferences().get(ESESystem.ENABLE_LOAN_MODULE));
        List<PaymentMode> modes = productDistributionService.listPaymentMode();
       
        Map<String, String> paymentListMap = new LinkedHashMap<String, String>();
        if (!ObjectUtil.isListEmpty(modes)) {
            for (PaymentMode mode : modes) {
            	if(!StringUtil.isEmpty(getEnableLoanModule()) && !getEnableLoanModule().equalsIgnoreCase("1")) {
            		if(mode.getName().equalsIgnoreCase("Loan Repayment")) {
                		paymentListMap.remove(mode);
                	}else {
                		paymentListMap.put(mode.getName(),mode.getName());
                	}
            	}else {
            		paymentListMap.put(mode.getName(),mode.getName());
            	}
            	
            	
            }
        }
        return paymentListMap;
    }

    /**
     * Populate account.
     * @throws Exception the exception
     */
    public void populateAccount() throws Exception {

        String result = "";
        if (!StringUtil.isEmpty(paymentAmount)) {
            ESEAccount agentAccount = accountService.findAccountByProfileIdAndProfileType(agentId,
                    ESEAccount.AGENT_ACCOUNT);
            if (ObjectUtil.isEmpty(agentAccount))
                result = "agentAccountUnavailable";
            else if (ESEAccount.INACTIVE == agentAccount.getStatus())
                result = "agentAccountInactive";
            /*
             * else if (Double.valueOf(paymentAmount) > agentAccount.getBalance()) result =
             * "insufficientBalance";
             */

        }
        if (StringUtil.isEmpty(result) || result == "" || result == null) {

            String farmer[] = selectedFarmer.split("-");
            Farmer farmerEntity = farmerService.findFarmerByFarmerId(farmer[1]);
            ESEAccount farmerAccount = farmerService.findAccountBySeassonProcurmentProductFarmer(
                    0L, farmerEntity.getId());
            if (ObjectUtil.isEmpty(farmerAccount))
                result = "farmerAccountUnavailable";
            else if (ESEAccount.INACTIVE == farmerAccount.getStatus())
                result = "farmeraccountInactive";
        }
        if (StringUtil.isEmpty(result)) {
            // Added for handling Form ReSubmit
            request.getSession().setAttribute(
                    agentId + "_" + PaymentMode.PROCURMENT_PAYMENT_TXN + "_"
                            + WebTransactionAction.IS_FORM_RESUBMIT, "No");
        }
        response.getWriter().print(getText(result));
    }

    /**
     * Populate farmer balance.
     * @throws Exception the exception
     */
    public void populateFarmerBalance() throws Exception {

        String result = "";
        if (!StringUtil.isEmpty(selectedFarmer) && !StringUtil.isEmpty(selectedPayment) && (!selectedFarmer.equals("0"))) {
            String farmer[] = selectedFarmer.split("-");
            Farmer farmerEntity = farmerService.findFarmerByFarmerId(farmer[1]);
            ESEAccount farmerAccount = farmerService.findAccountBySeassonProcurmentProductFarmer(
                    0L, farmerEntity.getId());
            if (!ObjectUtil.isEmpty(farmerAccount)) {
                /*
                 * if (farmerAccount.getBalance() > 0) result =
                 * formatter.format(farmerAccount.getBalance()) + "," + "DR"; else result =
                 * formatter.format(Math.abs(farmerAccount.getBalance())) + "," + "CR";
                 */
                if (PaymentMode.DISTRIBUTION_ADVANCE_PAYMENT_MODE_NAME
                        .equalsIgnoreCase(selectedPayment)) {
                    result = getDistributionBalance(farmerAccount);
                } else {
                    result = getProcurementBalance(farmerAccount);
                }
            }
        }
        response.getWriter().print(getText(result));
    }

    private void setCurrentSeason() {

        ESESystem preference = preferncesService.findPrefernceById("2");
        String currentSeasonCode = preference.getPreferences().get(ESESystem.CURRENT_SEASON_CODE);
        if (!StringUtil.isEmpty(currentSeasonCode)) {
            Season currentSeason = productDistributionService
                    .findSeasonBySeasonCode(currentSeasonCode);
            setSelectedSeason(String.valueOf(currentSeason.getId()));
        }

    }

    /**
     * Populate print html.
     * @return the string
     */
    public String populatePrintHTML() {

        initializePrintMap();
        if (!StringUtil.isEmpty(receiptNo)) {
            AgroTransaction paymentTxn = productDistributionService
                    .findPaymentAgroTransactionByRecNoProfType(receiptNo, Profile.CLIENT);
            buildTransactionPrintMap(paymentTxn);
        }
        return "html";
    }

    private void initializePrintMap() {

        this.paymentMap = new HashMap<String, Object>();
        this.paymentMap.put("recNo", "");
        this.paymentMap.put("fId", "");
        this.paymentMap.put("fName", "");
        this.paymentMap.put("village", "");
        this.paymentMap.put("samithi", "");
        this.paymentMap.put("date", "");
        this.paymentMap.put("paymentAmout", "");
        this.paymentMap.put("agentId", "");
        this.paymentMap.put("agentName", "");
        this.paymentMap.put("openingBal", "0.00");
        this.paymentMap.put("finalBal", "0.00");
        this.paymentMap.put("oBal", 0.00);
        this.paymentMap.put("fBal", 0.00);
        this.paymentMap.put("isAgent", true);
        this.paymentMap.put("pMode", "");
    }

    private void buildTransactionPrintMap(AgroTransaction paymentTxn) {

        if (!ObjectUtil.isEmpty(paymentTxn)) {
            DateFormat df = new SimpleDateFormat(RECEIPT_DATE_FORMAT);

            if (!StringUtil.isEmpty(paymentTxn.getReceiptNo())) {
                this.paymentMap.put("recNo", paymentTxn.getReceiptNo());
            }
            if (!StringUtil.isEmpty(paymentTxn.getFarmerId())) {
                this.paymentMap.put("fId", paymentTxn.getFarmerId());
                // Loading village and samithi from farmer
                Farmer farmer = farmerService.findFarmerByFarmerId(paymentTxn.getFarmerId());
                if (!ObjectUtil.isEmpty(farmer)) {
                    if (!ObjectUtil.isEmpty(farmer.getVillage()))
                        this.paymentMap.put("village", farmer.getVillage().getName());
                    if (!ObjectUtil.isEmpty(farmer.getSamithi()))
                        this.paymentMap.put("samithi", farmer.getSamithi().getName());
                }
            }
            if (!StringUtil.isEmpty(paymentTxn.getFarmerName())) {
                this.paymentMap.put("fName", paymentTxn.getFarmerName());
            }

            if (!ObjectUtil.isEmpty(paymentTxn.getTxnTime())) {
                this.paymentMap.put("date", df.format(paymentTxn.getTxnTime()));
            }
            if (!StringUtil.isEmpty(paymentTxn.getAgentId())) {
                this.paymentMap.put("agentId", paymentTxn.getAgentId());
                Agent agent = agentService.findAgentByProfileId(paymentTxn.getAgentId());
                if (!ObjectUtil.isEmpty(agent))
                    this.paymentMap.put("isAgent", !agent.isCoOperativeManager());
            }
            if (!StringUtil.isEmpty(paymentTxn.getAgentName())) {
                this.paymentMap.put("agentName", paymentTxn.getAgentName());
            }
            this.paymentMap.put("openingBal", CurrencyUtil.thousandSeparator(Math.abs(paymentTxn
                    .getIntBalance())));
            this.paymentMap.put("oBal", paymentTxn.getIntBalance());
            this.paymentMap.put("finalBal", CurrencyUtil.thousandSeparator(Math.abs(paymentTxn
                    .getBalAmount())));
            this.paymentMap.put("fBal", paymentTxn.getBalAmount());
            this.paymentMap.put("paymentAmout", CurrencyUtil.thousandSeparator(paymentTxn
                    .getTxnAmount()));
            if (!StringUtil.isEmpty(paymentTxn.getTxnDesc())) {
                String[] txnDescStrArray = paymentTxn.getTxnDesc().split("\\|");
                if (txnDescStrArray.length > 2) {
                    this.paymentMap.put("pMode", txnDescStrArray[1]);
                }
            }
        }
    }

    /**
     * Sets the selected city.
     * @param selectedCity the new selected city
     */
    public void setSelectedCity(String selectedCity) {

        this.selectedCity = selectedCity;
    }

    /**
     * Gets the selected city.
     * @return the selected city
     */
    public String getSelectedCity() {

        return selectedCity;
    }

    /**
     * Gets the location service.
     * @return the location service
     */
    public ILocationService getLocationService() {

        return locationService;
    }

    /**
     * Sets the location service.
     * @param locationService the new location service
     */
    public void setLocationService(ILocationService locationService) {

        this.locationService = locationService;
    }

    /**
     * Sets the selected village.
     * @param selectedVillage the new selected village
     */
    public void setSelectedVillage(String selectedVillage) {

        this.selectedVillage = selectedVillage;
    }

    /**
     * Gets the selected village.
     * @return the selected village
     */
    public String getSelectedVillage() {

        return selectedVillage;
    }

    /**
     * Sets the selected farmer.
     * @param selectedFarmer the new selected farmer
     */
    public void setSelectedFarmer(String selectedFarmer) {

        this.selectedFarmer = selectedFarmer;
    }

    /**
     * Gets the selected farmer.
     * @return the selected farmer
     */
    public String getSelectedFarmer() {

        return selectedFarmer;
    }

    /**
     * Gets the farmer service.
     * @return the farmer service
     */
    public IFarmerService getFarmerService() {

        return farmerService;
    }

    /**
     * Sets the farmer service.
     * @param farmerService the new farmer service
     */
    public void setFarmerService(IFarmerService farmerService) {

        this.farmerService = farmerService;
    }

    /**
     * Sets the farmer list.
     * @param farmerList the new farmer list
     */
    public void setFarmerList(List<String> farmerList) {

        this.farmerList = farmerList;
    }

    /**
     * Sets the city list.
     * @param cityList the new city list
     */
    public void setCityList(List<String> cityList) {

        this.cityList = cityList;
    }

    /**
     * Sets the village list.
     * @param villageList the new village list
     */
    public void setVillageList(List<String> villageList) {

        this.villageList = villageList;
    }

    /**
     * Sets the page no.
     * @param pageNo the new page no
     */
    public void setPageNo(String pageNo) {

        this.pageNo = pageNo;
    }

    /**
     * Gets the remarks.
     * @return the remarks
     */
    public String getRemarks() {

        return remarks;
    }

    /**
     * Sets the remarks.
     * @param remarks the new remarks
     */
    public void setRemarks(String remarks) {

        this.remarks = remarks;
    }

    /**
     * Gets the page no.
     * @return the page no
     */
    public String getPageNo() {

        return pageNo;
    }

    /**
     * Sets the selected season.
     * @param selectedSeason the new selected season
     */
    public void setSelectedSeason(String selectedSeason) {

        this.selectedSeason = selectedSeason;
    }

    /**
     * Gets the selected season.
     * @return the selected season
     */
    public String getSelectedSeason() {

        return selectedSeason;
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
     * Gets the product distribution service.
     * @return the product distribution service
     */
    public IProductDistributionService getProductDistributionService() {

        return productDistributionService;
    }

    /**
     * Sets the product distribution service.
     * @param productDistributionService the new product distribution service
     */
    public void setProductDistributionService(IProductDistributionService productDistributionService) {

        this.productDistributionService = productDistributionService;
    }

    /**
     * Sets the selected payment.
     * @param selectedPayment the new selected payment
     */
    public void setSelectedPayment(String selectedPayment) {

        this.selectedPayment = selectedPayment;
    }

    /**
     * Gets the selected payment.
     * @return the selected payment
     */
    public String getSelectedPayment() {

        return selectedPayment;
    }

    /**
     * Sets the payment amount.
     * @param paymentAmount the new payment amount
     */
    public void setPaymentAmount(String paymentAmount) {

        this.paymentAmount = paymentAmount;
    }

    /**
     * Gets the payment amount.
     * @return the payment amount
     */
    public String getPaymentAmount() {

        return paymentAmount;
    }

    /**
     * Gets the account service.
     * @return the account service
     */
    public IAccountService getAccountService() {

        return accountService;
    }

    /**
     * Sets the account service.
     * @param accountService the new account service
     */
    public void setAccountService(IAccountService accountService) {

        this.accountService = accountService;
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
     * Sets the receipt no.
     * @param receiptNo the new receipt no
     */
    public void setReceiptNo(String receiptNo) {

        this.receiptNo = receiptNo;
    }

    /**
     * Gets the receipt no.
     * @return the receipt no
     */
    public String getReceiptNo() {

        return receiptNo;
    }

    /**
     * Sets the agent name.
     * @param agentName the new agent name
     */
    public void setAgentName(String agentName) {

        this.agentName = agentName;
    }

    /**
     * Gets the agent name.
     * @return the agent name
     */
    public String getAgentName() {

        return agentName;
    }

    /**
     * Sets the agent balance.
     * @param agentBalance the new agent balance
     */
    public void setAgentBalance(String agentBalance) {

        this.agentBalance = agentBalance;
    }

    /**
     * Gets the agent balance.
     * @return the agent balance
     */
    public String getAgentBalance() {

        return agentBalance;
    }

    /**
     * Sets the serial number.
     * @param serialNumber the new serial number
     */
    public void setSerialNumber(String serialNumber) {

        this.serialNumber = serialNumber;
    }

    /**
     * Gets the serial number.
     * @return the serial number
     */
    public String getSerialNumber() {

        return serialNumber;
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

    private String getProcurementBalance(ESEAccount account) {

        if (!ObjectUtil.isEmpty(account))
            return (account.getBalance() > 0 ? formatter.format(account.getBalance()) + " CR"
                    : formatter.format(Math.abs(account.getBalance())) + " DR");
        return null;
    }

    private String getDistributionBalance(ESEAccount account) {

        if (!ObjectUtil.isEmpty(account))
            return (account.getDistributionBalance() > 0 ? formatter.format(account
                    .getDistributionBalance())
                    + " CR" : formatter.format(Math.abs(account.getDistributionBalance())) + " DR");
        return null;
    }

    private void setAgentInfo(ESEAccount account) {

        if (!ObjectUtil.isEmpty(account)) {
            agentInfoMap.put(PROCUREMENT_BALANCE, getProcurementBalance(account));
            agentInfoMap.put(DISTRIBUTION_BALANCE, getDistributionBalance(account));
        }
    }

    /**
     * Gets the agent info map.
     * @return the agent info map
     */
    public Map<String, Object> getAgentInfoMap() {

        return agentInfoMap;
    }

    /**
     * Sets the agent info map.
     * @param agentInfoMap the agent info map
     */
    public void setAgentInfoMap(Map<String, Object> agentInfoMap) {

        this.agentInfoMap = agentInfoMap;
    }

    /**
     * Gets the payment map.
     * @return the payment map
     */
    public Map<String, Object> getPaymentMap() {

        return paymentMap;
    }

    /**
     * Sets the payment map.
     * @param paymentMap the payment map
     */
    public void setPaymentMap(Map<String, Object> paymentMap) {

        this.paymentMap = paymentMap;
    }
    
    @SuppressWarnings("unchecked")
    protected JSONObject getJSONObject(Object id, Object name) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("name", name);
        return jsonObject;
    }

    public Map<String,String> getAgentList() {
        List<Agent> listAgents = agentService.listAgent();
        if (!ObjectUtil.isListEmpty(listAgents)) {
            for (Agent agent : listAgents) {
                agentList.put(agent.getProfileId(),agent.getPersonalInfo().getAgentName()+ "-" + agent.getProfileId());
            }
        }    
        return agentList;
    }

    public void setAgentList(Map<String,String> agentList) {
    
        this.agentList = agentList;
    }

    public String getSelectedAgent() {
    
        return selectedAgent;
    }

    public void setSelectedAgent(String selectedAgent) {
    
        this.selectedAgent = selectedAgent;
    }
    
    public String getSeleAgent() {
		return seleAgent;
	}

	public void setSeleAgent(String seleAgent) {
		this.seleAgent = seleAgent;
	}
	
	

	public Double getCashCreditValue() {
		return cashCreditValue;
	}

	public void setCashCreditValue(Double cashCreditValue) {
		this.cashCreditValue = cashCreditValue;
	}

	public void populateAgentAccBalance() throws Exception {

		String cashAndCredit = "";
		ESEAccount acc = new ESEAccount();
			if (!StringUtil.isEmpty(seleAgent)) {
				acc = accountService.findAccountByVendorIdAndType(seleAgent, ESEAccount.AGENT_ACCOUNT);
				if (!StringUtil.isEmpty(acc)) {
					cashCreditValue = (acc.getCashBalance());
					cashAndCredit = cashCreditValue.toString();
				} else {
					cashCreditValue = 0.00;
				}
				cashAndCredit = cashCreditValue.toString();
			}
			Double bal = Double.valueOf(cashAndCredit);
			if (bal >= 0) {
				cashAndCredit =CurrencyUtil.currencyFormatter(Math.abs(bal)) + "-" + "CR";
			} else {
				cashAndCredit = CurrencyUtil.currencyFormatter(Math.abs(bal)) + "-" + "DR";
			}
		response.getWriter().print(cashAndCredit);

	}
	
	
	public void populateFarmerAccBalance() throws Exception {

		String cashAndCredit = "";
		ESEAccount acc = new ESEAccount();
			if (!StringUtil.isEmpty(selectedFarmer)) {
				acc = accountService.findAccountByVendorIdAndType(selectedFarmer.split("-")[1], ESEAccount.CONTRACT_ACCOUNT);
				if (!StringUtil.isEmpty(acc)) {
					cashCreditValue = (acc.getCashBalance());
					cashAndCredit = cashCreditValue.toString();
				} else {
					cashCreditValue = 0.00;
				}
				cashAndCredit = cashCreditValue.toString();
			}
			Double bal = Double.valueOf(cashAndCredit);
			if (bal >= 0) {
				cashAndCredit =CurrencyUtil.currencyFormatter(Math.abs(bal)) + "-" + "CR";
			} else {
				cashAndCredit = CurrencyUtil.currencyFormatter(Math.abs(bal)) + "-" + "DR";
			}
		
		response.getWriter().print(cashAndCredit);

	}
 
	public String getEnableLoanModule() {
		return enableLoanModule;
	}

	public void setEnableLoanModule(String enableLoanModule) {
		this.enableLoanModule = enableLoanModule;
	}
	public void populateFarmerLoanBalance() throws Exception {

		String loanAmt = "";
		String loanBal="";
		String cashBal="";
		String result="";
		ESEAccount acc = new ESEAccount();
			if (!StringUtil.isEmpty(selectedId) && selectedId!="") {
				acc = accountService.findAccountByVendorIdAndType(selectedId.split("-")[1], ESEAccount.CONTRACT_ACCOUNT);
				if (!StringUtil.isEmpty(acc)) {
					loanAmount = acc.getLoanAmount();
					loanBalance = acc.getOutstandingLoanAmount();
					cashBalance=acc.getCashBalance();
					loanAmt=loanAmount.toString();
					loanBal=loanBalance.toString();
					cashBal=cashBalance.toString();
					
				} else {
					loanAmount = 0.00;
					loanBalance=0.00;
					cashBalance=0.00;
				}
				
			
			Double loanAmunt = Double.valueOf(loanAmt);
			if (loanAmunt >= 0) {
				loanAmt =CurrencyUtil.currencyFormatter(Math.abs(loanAmunt));
			} 
			Double loanBalan = Double.valueOf(loanBal);
			if (loanBalan >= 0) {
				loanBal =CurrencyUtil.currencyFormatter(Math.abs(loanBalan));
			} 
			
			Double cashBalan = Double.valueOf(cashBal);
			if (cashBalan >= 0) {
				cashBal =CurrencyUtil.currencyFormatter(Math.abs(cashBalan));
			} 
			result = loanAmt+","+loanBal+","+cashBal;
			}
		response.getWriter().printf(result);

	}

	public String getSelectedId() {
		return selectedId;
	}

	public void setSelectedId(String selectedId) {
		this.selectedId = selectedId;
	}

	public Double getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(Double loanAmount) {
		this.loanAmount = loanAmount;
	}

	public Double getLoanBalance() {
		return loanBalance;
	}

	public void setLoanBalance(Double loanBalance) {
		this.loanBalance = loanBalance;
	}

	public Double getCashBalance() {
		return cashBalance;
	}

	public void setCashBalance(Double cashBalance) {
		this.cashBalance = cashBalance;
	}

	 public void populateLoanRepayment() {

	     
	      String farmers[] = selectedFarmer.split("-");
	        ESEAccount farmerAccount = null;
	        Farmer farmerEntity= null;
	            request.setAttribute(HEADING, getText("create.page"));
	            if(!StringUtil.isEmpty(selectedFarmer)){
	            	   farmerEntity = farmerService.findFarmerByFarmerId(farmers[1]);
	  	             farmerAccount = farmerService.findAccountByFarmerLoanProduct(farmerEntity.getId());
	          
	            }	         
	            AgroTransaction farmerPaymentTxn = new AgroTransaction();

	           
	            Season season = null;
	            if(!ObjectUtil.isEmpty(selectedSeason)){
	            season = productDistributionService.findSeasonById(Long.valueOf(selectedSeason));
	            }

	            if (StringUtil.isEmpty(pageNo))
	                pageNo = AgroTransaction.EMPTY_PAGE_NO;

	            receiptNo = idGenerator.getLoanRepaymentReceiptNoSeq();
	           

	            /** FORMING AGRO TRANSACTION FOR FARMER **/
	            farmerPaymentTxn.setAgentId("");
	            farmerPaymentTxn.setAgentName("");
	            farmerPaymentTxn.setDeviceId(NOT_APPLICABLE);
	            farmerPaymentTxn.setDeviceName(NOT_APPLICABLE);
	            farmerPaymentTxn.setServicePointId("");
	            farmerPaymentTxn.setServicePointName("");
	            farmerPaymentTxn.setFarmerId(farmers[1]);
	            farmerPaymentTxn.setFarmerName(!ObjectUtil.isEmpty(farmerEntity) && !StringUtil.isEmpty(farmerEntity.getSurName()) ? farmerEntity.getFirstName()+" "+farmerEntity.getSurName() : farmerEntity.getFirstName());
	            farmerPaymentTxn.setReceiptNo(receiptNo);
	            farmerPaymentTxn.setProfType(Profile.CO_OPEARATIVE_MANAGER);
	            farmerPaymentTxn.setOperType(ESETxn.ON_LINE);
	            farmerPaymentTxn.setTxnTime(DateUtil.convertStringToDate(startDate, "MM/dd/yyyy"));
	            farmerPaymentTxn.setIntBalance(farmerAccount.getOutstandingLoanAmount());
	            farmerPaymentTxn.setTxnAmount(Double.parseDouble(paymentAmount));
	            if(farmerAccount.getOutstandingLoanAmount()>=Double.parseDouble(paymentAmount)){
	            farmerPaymentTxn.setBalAmount(farmerAccount.getOutstandingLoanAmount() - Double.parseDouble(paymentAmount));	
	            }else{
	            	 farmerPaymentTxn.setBalAmount(0.0);
	            }
	            farmerPaymentTxn.setTxnDesc(PaymentMode.LOAN_REPAYMENT_MODE_NAME);
	            farmerPaymentTxn.setSeasonCode(getCurrentSeasonCodeData());
	            farmerPaymentTxn.setBranch_id(getBranchId());
	            farmerPaymentTxn.setTxnType(PaymentMode.LOAN_REPAYMENT_TXN);

	            // Adding remarks to the Transaction description
	            if (!StringUtil.isEmpty(remarks)) {
	            farmerPaymentTxn.setTxnDesc(farmerPaymentTxn.getTxnDesc() + "|" + remarks);          
	            }
	            farmerPaymentTxn.setAccount(farmerAccount);
	          
	            // Save and Update Payment Txn and Account
	            productDistributionService.saveAgroTransactionForLoanRePayment(farmerPaymentTxn);	           
	           
	            JSONArray respArr = new JSONArray();
	    		respArr.add(getJSONObject("data", "success"));
	    		
	    		sendAjaxResponse(respArr);
	       

	    }
	 
	 public void populateFarmerLoanAccount() throws Exception {

	        String result = "";
	        String farmer[] = selectedFarmer.split("-");
	        if (StringUtil.isEmpty(result) || result == "" || result == null) {      
	            
	            if(!StringUtil.isEmpty(farmer[1]) && !ObjectUtil.isEmpty(farmer[1])){
	            	 Farmer farmerEntity = farmerService.findFarmerByFarmerId(farmer[1]);
	            	 	if(!ObjectUtil.isEmpty(farmerEntity)){
	            	 		 ESEAccount farmerAccount = farmerService.findAccountByFarmerLoanProduct(farmerEntity.getId());
	     	 	            if (ObjectUtil.isEmpty(farmerAccount))
	     	 	                result = "farmerAccountUnavailable";
	     	 	            else if (ESEAccount.INACTIVE == farmerAccount.getStatus())
	     	 	                result = "farmeraccountInactive";
	            	 	}
	            	
	           
	           
	        }
	      
	       
	        }
	        response.getWriter().print(getText(result));
	    }

	public String getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}
	
	

	
	 public String populateFarmerWithLoan() throws Exception {
	        JSONArray farmerArr = new JSONArray();
	        String currentSeasonCode = getCurrentSeasonCode();
	        if (!ObjectUtil.isEmpty(selectedVillage) && (!StringUtil.isEmpty(selectedVillage) && (!selectedVillage.equals("0")))
	                && !StringUtil.isEmpty(currentSeasonCode)) {
	            String[] villageCode = selectedVillage.split("-");
	           /* List<Farmer> listFarmer = farmerService
	                    .listActiveContractFarmersByVillageCodeSeasonCode(villageCode[1],
	                            currentSeasonCode);*/
	           
	            List<Farmer> listFarmer = farmerService.listFarmerWithOutstandLoanBal(villageCode[1]);
	            if (!ObjectUtil.isListEmpty(listFarmer)) {
	                for (Farmer farmer : listFarmer) {                   
	                    farmerArr.add(getJSONObject(farmer.getFirstName() + " " + farmer.getLastName()+"-"+farmer.getFarmerId(),farmer.getFirstName() + " " 
	                + farmer.getLastName()+"-"+farmer.getFarmerId()));
	                }
	            }
	        }
	        sendAjaxResponse(farmerArr);
	        return null;
	    }
	 
}
