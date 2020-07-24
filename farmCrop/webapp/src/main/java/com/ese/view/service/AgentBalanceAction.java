/*
 * AgentBalanceAction.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.AgentCashFlow;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;
import com.sourcetrace.esesw.excep.SwitchException;
import com.sourcetrace.esesw.view.WebTransactionAction;

public class AgentBalanceAction extends WebTransactionAction {

    private static final long serialVersionUID = 1L;

    private AgentCashFlow agentCashFlow;

    private String id;

    private IAccountService accountService;

    private String balance;

    private boolean isDeposit;

    private long warehouseId;

    private int txnType;

    public static final String NOT_APPLICABLE = "N/A";

    public static final String FIELD_STAFF_CASH_FLOW = "FIELD STAFF CASH FLOW";
    
    private static final String AGENT_CASH_FLOW_DISTRIBUTION = "300D";
    
    private static final String AGENT_CASH_FLOW_PROCUREMENT = "300P";
    
    private static final String AGENT_CASH_RETURN="1";
    
    private static final String CASH_DISTRIBUTION="334";
    
    /**
     * Gets the account service.
     * @return the account service
     */
    public IAccountService getAccountService() {

        return accountService;
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
     * Sets the account service.
     * @param accountService the new account service
     */
    public void setAccountService(IAccountService accountService) {

        this.accountService = accountService;
    }

    /**
     * @see com.sourcetrace.esesw.view.WebTransactionAction#getData()
     */
    @Override
    public Object getData() {

        return null;
    }

    /**
     * Sets the balance.
     * @param balance the new balance
     */
    public void setBalance(String balance) {

        this.balance = balance;
    }

    /**
     * Gets the balance.
     * @return the balance
     */
    public String getBalance() {

        return balance;
    }

    /**
     * Sets the deposit.
     * @param isDeposit the new deposit
     */
    public void setDeposit(boolean isDeposit) {

        this.isDeposit = isDeposit;
    }

    /**
     * Checks if is deposit.
     * @return true, if is deposit
     */
    public boolean isDeposit() {

        return isDeposit;
    }

    /**
     * Sets the agent cash flow.
     * @param agentCashFlow the new agent cash flow
     */
    public void setAgentCashFlow(AgentCashFlow agentCashFlow) {

        this.agentCashFlow = agentCashFlow;
    }

    /**
     * Gets the agent cash flow.
     * @return the agent cash flow
     */
    public AgentCashFlow getAgentCashFlow() {

        return agentCashFlow;
    }

    /**
     * @see com.sourcetrace.esesw.view.SwitchAction#toJSON(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJSON(Object obj) {

      /*  AgentCashFlow agentCashFlow = (AgentCashFlow) obj;
        JSONObject jsonObject = new JSONObject();
        JSONArray rows = new JSONArray();
        rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">" + agentCashFlow.getAgentId()
                + "</font>");
        rows.add(agentCashFlow.getAgentName());
        rows.add(CurrencyUtil.currencyFormatter(agentCashFlow.getBalance()));
        rows.add("<button class='faEdit' onclick='enablePopUp(" + agentCashFlow.getId() + ","
                    + agentCashFlow.getBalance() + ")'></button>");
       

        jsonObject.put("id", agentCashFlow.getId());
        jsonObject.put("cell", rows);
        return jsonObject;*/
    	
    	 AgentCashFlow agentCashFlow = (AgentCashFlow) obj;
         JSONObject jsonObject = new JSONObject();
         JSONArray rows = new JSONArray();
         rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">" + agentCashFlow.getAgentId()
                 + "</font>");
         rows.add(agentCashFlow.getAgentName());

         if (txnType == 0) {
             rows.add(CurrencyUtil.currencyFormatter(agentCashFlow.getCashBalance()));
             rows.add("");
             rows.add("<button class='fa fa-pencil-square-o' onclick='enablePopUp(" + agentCashFlow.getId() + ","
                     + CurrencyUtil.currencyFormatter(agentCashFlow.getCashBalance()) + ",\""+agentCashFlow.getAgentId()+"\")'></button>");
         } else {
             rows.add("");

             rows.add(CurrencyUtil.currencyFormatter(agentCashFlow.getDistributionBalance()));
             rows.add("<button class='fa fa-pencil-square-o' onclick='enablePopUp(" + agentCashFlow.getId() + ","
                     + CurrencyUtil.currencyFormatter(agentCashFlow.getDistributionBalance()) + ",\""+agentCashFlow.getAgentId()+"\")'></button>");
         }

         jsonObject.put("id", agentCashFlow.getId());
         jsonObject.put("cell", rows);
         return jsonObject;
    }

    /**
     * @see com.sourcetrace.esesw.view.SwitchAction#data()
     */
    @Override
    public String data() throws Exception {

    	 Map<String, String> searchRecord = getJQGridRequestParam(); // get the search parameter with
         // value

         AgentCashFlow filter = new AgentCashFlow();
         //filter.setWarehouseId(warehouseId);
         setTxnType(txnType);
         if (!StringUtil.isEmpty(searchRecord.get("agentId"))
                 || !StringUtil.isEmpty(searchRecord.get("agentId"))) {
             filter.setAgentId(searchRecord.get("agentId").trim());
         }
         if (!StringUtil.isEmpty(searchRecord.get("agentName"))
                 || !StringUtil.isEmpty(searchRecord.get("agentName"))) {
             filter.setAgentName(searchRecord.get("agentName").trim());
         }
         if(!StringUtil.isEmpty(getBranchId())){
        	 filter.setBranch(getBranchId());
         }

         Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(),
                 getResults(), filter, getPage());

         return sendJQGridJSONResponse(data);
    }

    /**
     * Edits the agent balance.
     * @return the string
     * @throws Exception the exception
     */
    public String editAgentBalance() throws Exception {

      /*  if (id != null && !id.equals("")) {
            Agent loggedAgent = null;
            if(!StringUtil.isEmpty(agentId))
            loggedAgent = agentService.findAgentByProfileId(agentId);
            double totalAmt = 0;
            double existingBalance = 0;
            try {
                // Added for Handling Form ReSubmit - Please See at populateValidation() Method
                if (ObjectUtil.isEmpty(request.getSession().getAttribute(
                        FIELD_STAFF_CASH_FLOW + "_" + WebTransactionAction.IS_FORM_RESUBMIT))) {
                    command = INPUT;
                    request.setAttribute(HEADING, getText(LIST));
                    return list();
                }
                AgentCashFlow cashFlow = accountService.findAgentCashFlowById(Long.parseLong(id));
                if (!ObjectUtil.isEmpty(cashFlow)) {
                    ESEAccount account = accountService.findAccountByProfileIdAndProfileType(
                            cashFlow.getAgentId(), ESEAccount.AGENT_ACCOUNT);
                    request.setAttribute("agentId", cashFlow.getAgentId());
                    if (!ObjectUtil.isEmpty(account)) {
                        *//** FORMING AGRO TRANSACTION OBJECT **//*
                        AgroTransaction agroTransaction = new AgroTransaction();
                        if (txnType == 0){
                            existingBalance = account.getBalance();
                            agroTransaction.setTxnType(AGENT_CASH_FLOW_PROCUREMENT);
                        }
                        else{
                            existingBalance = account.getDistributionBalance();
                            agroTransaction.setTxnType(AGENT_CASH_FLOW_DISTRIBUTION);
                        }
                        if (isDeposit) {
                            totalAmt = existingBalance + Double.parseDouble(balance);
                            agroTransaction.setTxnDesc("CASH DISTRIBUTION"  +(!ObjectUtil.isEmpty(loggedAgent) ? " ( " +loggedAgent.getPersonalInfo().getAgentName()+ " ) " : ""));
                        } else {
                            if (existingBalance < Double.parseDouble(balance)) {
                                throw new SwitchException(SwitchErrorCodes.INSUFFICIENT_BAL);
                            }
                            totalAmt = existingBalance - Double.parseDouble(balance);
                            agroTransaction.setTxnDesc("CASH SETTLEMENT" +(!ObjectUtil.isEmpty(loggedAgent) ? " ( " +loggedAgent.getPersonalInfo().getAgentName()+ " ) " : ""));
                        }

                        agroTransaction.setReceiptNo(NOT_APPLICABLE);
                        agroTransaction.setAgentId(cashFlow.getAgentId());
                        agroTransaction.setAgentName(cashFlow.getAgentName());
                        agroTransaction.setDeviceId(NOT_APPLICABLE);
                        agroTransaction.setDeviceName(NOT_APPLICABLE);
                        Agent agent = agentService.findAgentByProfileId(cashFlow.getAgentId());
                        if (!ObjectUtil.isEmpty(agent)
                                && !ObjectUtil.isEmpty(agent.getServicePoint())) {
                            agroTransaction.setServicePointId(agent.getServicePoint().getCode());
                            agroTransaction.setServicePointName(agent.getServicePoint().getName());
                        }
                        agroTransaction.setProfType(Profile.AGENT);
                        agroTransaction.setOperType(ESETxn.ON_LINE);
                        agroTransaction.setTxnTime(new Date());
                        agroTransaction.setIntBalance(existingBalance);
                        agroTransaction.setTxnAmount(Double.parseDouble(balance));
                        agroTransaction.setBalAmount(totalAmt);

                        *//** CREATING AND UPDATING AGRO TRANSACTION AND ACCOUNT OBJECT **//*
                        if (txnType == 0)
                            account.setBalance(totalAmt);
                        else
                            account.setDistributionBalance(totalAmt);

                        accountService
                                .createAgroTransactionAndEditAccount(agroTransaction, account);
                        setWarehouseId(cashFlow.getWarehouseId());
                        // re initialize the object, becz the entered value should reset after add
                        // or edit the
                        // warehouse
                        request.getSession()
                                .removeAttribute(
                                        FIELD_STAFF_CASH_FLOW + "_"
                                                + WebTransactionAction.IS_FORM_RESUBMIT);

                    }
                }
            } catch (Exception e) {
                if (e instanceof SwitchException)
                    addActionError(getText("insufficientBalanceCash"));
                else
                    addActionError(getText("errorWhileProcess"));
            }

        }
        request.setAttribute("agentId", request.getSession().getAttribute("agentId"));
        return list();
*/
    	
    	if (id != null && !id.equals("")) {
            Agent loggedAgent = null;
            if(!StringUtil.isEmpty(agentId))
            loggedAgent = agentService.findAgentByProfileId(agentId);
            double totalAmt = 0;
            double existingBalance = 0;
            try {
                // Added for Handling Form ReSubmit - Please See at populateValidation() Method
                if (ObjectUtil.isEmpty(request.getSession().getAttribute(
                        FIELD_STAFF_CASH_FLOW + "_" + WebTransactionAction.IS_FORM_RESUBMIT))) {
                    command = INPUT;
                    request.setAttribute(HEADING, getText(LIST));
                    return list();
                }
                AgentCashFlow cashFlow = accountService.findAgentCashFlowById(Long.parseLong(id));
                if (!ObjectUtil.isEmpty(cashFlow)) {
                    ESEAccount account = accountService.findAccountByProfileIdAndProfileType(
                            cashFlow.getAgentId(), ESEAccount.AGENT_ACCOUNT);
                    request.setAttribute("agentId", cashFlow.getAgentId());
                    if (!ObjectUtil.isEmpty(account)) {
                        /** FORMING AGRO TRANSACTION OBJECT **/
                        AgroTransaction agroTransaction = new AgroTransaction();
                        agroTransaction.setBranch_id(getBranchId());
                        if (txnType == 0){
                            existingBalance = account.getCashBalance();
                            agroTransaction.setTxnType(CASH_DISTRIBUTION);
                        }
                        else{
                            existingBalance = account.getDistributionBalance();
                            agroTransaction.setTxnType(CASH_DISTRIBUTION);
                        }
                        if (isDeposit) {
                            totalAmt = existingBalance + Double.parseDouble(balance);
                            agroTransaction.setTxnDesc("CASH DISTRIBUTION"  +(!ObjectUtil.isEmpty(loggedAgent) ? " ( " +loggedAgent.getPersonalInfo().getAgentName()+ " ) " : ""));
                        } else {
                            if (existingBalance < Double.parseDouble(balance)) {
                                throw new SwitchException(SwitchErrorCodes.INSUFFICIENT_BAL);
                            }
                            totalAmt = existingBalance - Double.parseDouble(balance);
                            agroTransaction.setTxnDesc("CASH SETTLEMENT" +(!ObjectUtil.isEmpty(loggedAgent) ? " ( " +loggedAgent.getPersonalInfo().getAgentName()+ " ) " : ""));
                        }
                        HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
            			Object branchObject = httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
            			String currentBranch = ObjectUtil.isEmpty(branchObject) ? ESESystem.SYSTEM_ESE_NAME : branchObject.toString();
            			ESESystem ese = preferncesService.findPrefernceByOrganisationId(currentBranch);
            			String currentSeasonCode = "";
            			if (!ObjectUtil.isEmpty(ese)) {
            				ESESystem preference = preferncesService.findPrefernceById(String.valueOf(ese.getId()));
            				currentSeasonCode = preference.getPreferences().get(ESESystem.CURRENT_SEASON_CODE);

            				if (StringUtil.isEmpty(currentSeasonCode)) {
            					currentSeasonCode = "";
            				}
            			}
            			agroTransaction.setSeasonCode(currentSeasonCode);
                        agroTransaction.setReceiptNo(NOT_APPLICABLE);
                        agroTransaction.setAgentId(cashFlow.getAgentId());
                        agroTransaction.setAgentName(cashFlow.getAgentName());
                        agroTransaction.setDeviceId(NOT_APPLICABLE);
                        agroTransaction.setDeviceName(NOT_APPLICABLE);
                        Agent agent = agentService.findAgentByProfileId(cashFlow.getAgentId());
                        if (!ObjectUtil.isEmpty(agent)
                                && !ObjectUtil.isEmpty(agent.getServicePoint())) {
                            agroTransaction.setServicePointId(agent.getServicePoint().getCode());
                            agroTransaction.setServicePointName(agent.getServicePoint().getName());
                        }
                        agroTransaction.setProfType(Profile.AGENT);
                        agroTransaction.setOperType(ESETxn.ON_LINE);
                        String nowDate=DateUtil.convertDateToString(new Date(), getGeneralDateFormat());
                        agroTransaction.setTxnTime(DateUtil.convertStringToDate(nowDate, getGeneralDateFormat()));
                        agroTransaction.setTxnTime(DateUtil.setTimeToDate(agroTransaction.getTxnTime()));
                      //  agroTransaction.setTxnTime(new Date());
                        agroTransaction.setIntBalance(existingBalance);
                        agroTransaction.setTxnAmount(Double.parseDouble(balance));
                        agroTransaction.setBalAmount(totalAmt);
                        agroTransaction.setAccount(account);
                        
                        /** CREATING AND UPDATING AGRO TRANSACTION AND ACCOUNT OBJECT **/
                        if (txnType == 0)
                            account.setCashBalance(totalAmt);
                        else
                            account.setDistributionBalance(totalAmt);

                        accountService
                                .createAgroTransactionAndEditAccount(agroTransaction, account);
                        //setWarehouseId(cashFlow.getWarehouseId());
                        // re initialize the object, becz the entered value should reset after add
                        // or edit the
                        // warehouse
                        request.getSession()
                                .removeAttribute(
                                        FIELD_STAFF_CASH_FLOW + "_"
                                                + WebTransactionAction.IS_FORM_RESUBMIT);

                    }
                }
            } catch (Exception e) {
                if (e instanceof SwitchException)
                    addActionError(getText("insufficientBalanceCash"));
                else
                    addActionError(getText("errorWhileProcess"));
            }

        }
        request.setAttribute("agentId", request.getSession().getAttribute("agentId"));
        return list();
    }

    /**
     * Edits the resubmit key.
     * @throws Exception the exception
     */
    public void editResubmitKey() throws Exception {

        // Added for handling Form ReSubmit
        request.getSession().setAttribute(
                FIELD_STAFF_CASH_FLOW + "_" + WebTransactionAction.IS_FORM_RESUBMIT, "No");
        response.setContentType("text/html");
        response.getWriter().print(AGENT_CASH_RETURN);
    }

    /**
     * @see com.sourcetrace.esesw.view.SwitchAction#list()
     */
/*public String list() throws Exception {

    if (agentCashFlow == null) {

            if (!StringUtil.isEmpty(agentId)) {
                request.setAttribute("agentId", agentId);
                Agent agent = agentService.findAgentByProfileId(agentId);
                if (!ObjectUtil.isEmpty(agent) && ESETxn.WEB_BOD == agent.getBodStatus()) {
                    for (Warehouse warehouse : agent.getWareHouses()) {
                        setWarehouseId(warehouse.getId());
                        setTxnType(txnType);
                    }
                    request.setAttribute(HEADING, getText(LIST));
                    return LIST;
                }
            }
            return "redirect";

        }
        // re initialize the object, becz the entered value should reset after add or edit the
        // warehouse
        request.getSession().removeAttribute(
                FIELD_STAFF_CASH_FLOW + "_" + WebTransactionAction.IS_FORM_RESUBMIT);
        request.setAttribute(HEADING, getText(LIST));
        return LIST;
    }

    /**
     * Gets the txn type list.
     * @return the txn type list
     */
    public Map<Integer, String> getTxnTypeList() {

        Map<Integer, String> txnTypeList = new HashMap<Integer, String>();
        txnTypeList.put(0, getText("procurement"));
        txnTypeList.put(1, getText("distribution"));
        return txnTypeList;
    }

    /**
     * Sets the warehouse id.
     * @param warehouseId the new warehouse id
     */
    public void setWarehouseId(long warehouseId) {

        this.warehouseId = warehouseId;
    }

    /**
     * Gets the warehouse id.
     * @return the warehouse id
     */
    public long getWarehouseId() {

        return warehouseId;
    }

    /**
     * Gets the txn type.
     * @return the txn type
     */
    public int getTxnType() {

        return txnType;
    }

    /**
     * Sets the txn type.
     * @param txnType the new txn type
     */
    public void setTxnType(int txnType) {

        this.txnType = txnType;
    }

}
