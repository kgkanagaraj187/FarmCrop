/*
 * AgentLoginAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.service;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;
import com.sourcetrace.eses.BreadCrumb;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.view.SwitchAction;

public class AgentLoginAction extends SwitchAction {

    private static final long serialVersionUID = 4059897302696092838L;
    private static final Logger LOGGER = Logger.getLogger(AgentLoginAction.class.getName());
    private static final String BOD_EXIST = "bod.exist.web";

    public static final String PROCUREMENT = "procurement";
    public static final String DISTRIBUTION = "distribution";
    public static final String DISTRIBUTION_TO_FIELDSTAFF = "distributionToFieldStaff";
    public static final String MTNT = "mtnt";
    public static final String MTNR = "mtnr";
    public static final String PAYMENT = "payment";
    public static final String DISTRIBUTION_MTNT = "dmtnt";
    public static final String DISTRIBUTION_MTNR = "dmtnr";
    public static final String FIELDSTAFF_PRODUCT_RETURN = "fsProductReturn";
    public static final String WAREHOUSE_PRODUCT_STOCK = "warehouseProduct";
    public static final String CASH_DISTRIBUTION = "agentBalance";
    private IAgentService agentService;
    private IFarmerService farmerService;
    private String userName;
    private String password;
    private String type;
    private String farmerId = "";

    /**
     * Sets the agent service.
     * @param agentService the new agent service
     */
    public void setAgentService(IAgentService agentService) {

        this.agentService = agentService;
    }

    /**
     * Gets the agent service.
     * @return the agent service
     */
    public IAgentService getAgentService() {

        return agentService;
    }

    /**
     * Sets the user name.
     * @param userName the new user name
     */
    public void setUserName(String userName) {

        this.userName = userName;
    }

    /**
     * Gets the user name.
     * @return the user name
     */
    public String getUserName() {

        return userName;
    }

    /**
     * Sets the password.
     * @param password the new password
     */
    public void setPassword(String password) {

        this.password = password;
    }

    /**
     * Gets the password.
     * @return the password
     */
    public String getPassword() {

        return password;
    }

    /**
     * Sets the type.
     * @param type the new type
     */
    public void setType(String type) {

        this.type = type;
    }

    /**
     * Gets the type.
     * @return the type
     */
    public String getType() {

        return type;
    }

    /**
     * Login.
     * @return the string
     */
    public String login() {

        type = request.getParameter("type");
        farmerId = request.getParameter("fId");
        if (type.equalsIgnoreCase(DISTRIBUTION) && !farmerId.equalsIgnoreCase("null")) {
            Farmer farmer = farmerService.findFarmerById(Long.valueOf(farmerId));
            Agent agent = agentService.findAgentByProfileId(userName);
            if (!ObjectUtil.isEmpty(agent) && !ObjectUtil.isEmpty(agent.getServicePoint())
                    && !ObjectUtil.isEmpty(agent.getServicePoint().getCity())
                    && farmer.getCity().getId() != agent.getServicePoint().getCity().getId()) {
                addActionError(getText("distributionUnavailable"));
                request.setAttribute(HEADING, getText(LIST));
                return SUCCESS;
            }

        }
        String loginText = agentService.isValidAgentLogin(userName, password, type);
        if (StringUtil.isEmpty(loginText)) {
            agentService.updateAgentBODStatus(userName, ESETxn.WEB_BOD);
            return redirection(type);
        } else {
            if (BOD_EXIST.equals(loginText)) {
                AgroTimerTask timerTask = (AgroTimerTask) request.getSession().getAttribute(
                        userName + "_timer");
                if (!ObjectUtil.isEmpty(timerTask) && !timerTask.isCancelled()) {
                    timerTask.cancelTimer(false);
                    return redirection(type);
                }
            }
            if (type.equals(PROCUREMENT) || type.equals(DISTRIBUTION) || type.equals(PAYMENT)) {
                addActionError(getText("comOrAgent." + loginText));
                request.setAttribute(HEADING, getText("comOrAgent." + LIST));
            } else {
                addActionError(getText(loginText));
                request.setAttribute(HEADING, getText(LIST));
            }
            return SUCCESS;
        }
    }

    /**
     * Redirection.
     * @param type the type
     * @return the string
     */
    private String redirection(String type) {

        if (PROCUREMENT.equals(type))
            return PROCUREMENT;
        else if (DISTRIBUTION.equals(type))
            return DISTRIBUTION;
        else if (DISTRIBUTION_TO_FIELDSTAFF.equals(type))
            return DISTRIBUTION_TO_FIELDSTAFF;
        else if (MTNT.equals(type))
            return MTNT;
        else if (MTNR.equals(type))
            return MTNR;
        else if (PAYMENT.equals(type))
            return PAYMENT;
        else if (DISTRIBUTION_MTNR.equals(type))
            return DISTRIBUTION_MTNR;
        else if (FIELDSTAFF_PRODUCT_RETURN.equals(type))
            return FIELDSTAFF_PRODUCT_RETURN;
        else if (WAREHOUSE_PRODUCT_STOCK.equals(type))
            return WAREHOUSE_PRODUCT_STOCK;
        else if (CASH_DISTRIBUTION.equals(type))
            return CASH_DISTRIBUTION;
        else
            return DISTRIBUTION_MTNT;
    }

    /**
     * @see com.opensymphony.xwork2.ActionSupport#execute()
     */
    public String execute() {

        type = request.getParameter("type");
        if (type.equals(PROCUREMENT) || type.equals(DISTRIBUTION) || type.equals(PAYMENT)) {
            request.setAttribute(HEADING, getText("comOrAgent." + LIST));
        } else {
            request.setAttribute(HEADING, getText(LIST));
        }
        return SUCCESS;
    }

    /**
     * Gets the agent id.
     * @return the agent id
     */
    public String getAgentId() {

        String tempId = "agentId=" + userName;
        if (type.equalsIgnoreCase(DISTRIBUTION) && !"null".equalsIgnoreCase(farmerId))
            tempId += getFarmerId();
        return tempId;
    }

    /**
     * Gets the farmer id.
     * @return the farmer id
     */
    public String getFarmerId() {

        return "&fId=" + farmerId;

    }

    /**
     * @see com.sourcetrace.esesw.view.SwitchAction#prepare()
     */
    public void prepare() throws Exception {

    	if (!StringUtil.isEmpty(type)) {
			String actionClassName = ActionContext.getContext().getActionInvocation().getAction().getClass()
					.getSimpleName();
			String content = getLocaleProperty(actionClassName + "." + BreadCrumb.BREADCRUMB+type);
			if (StringUtil.isEmpty(content) || (content.equalsIgnoreCase(actionClassName + "." + BreadCrumb.BREADCRUMB+type))) {
				content = super.getText(BreadCrumb.BREADCRUMB+type, "");
			}
			request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(content));
			
		}else{
			String actionClassName = ActionContext.getContext().getActionInvocation().getAction().getClass()
					.getSimpleName();
			String content = getLocaleProperty(actionClassName + "." + BreadCrumb.BREADCRUMB);
			if (StringUtil.isEmpty(content) || (content.equalsIgnoreCase(actionClassName + "." + BreadCrumb.BREADCRUMB))) {
				content = super.getText(BreadCrumb.BREADCRUMB, "");
			}else{
				
			}
			request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(content));
		}
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
     * Sets the farmer id.
     * @param farmerId the new farmer id
     */
    public void setFarmerId(String farmerId) {

        this.farmerId = farmerId;
    }

}
