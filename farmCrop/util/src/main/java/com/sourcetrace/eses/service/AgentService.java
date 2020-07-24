/*
 * AgentService.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.ese.entity.credential.TxnCredential;
import com.ese.entity.txn.fee.CommissionFeeGroup;
import com.sourcetrace.eses.dao.IAccountDAO;
import com.sourcetrace.eses.dao.IAgentDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.AgentType;
import com.sourcetrace.eses.entity.LocationHistory;
import com.sourcetrace.eses.entity.TransactionType;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.AgentAccessLog;
import com.sourcetrace.eses.order.entity.txn.AgentAccessLogDetail;
import com.sourcetrace.eses.order.entity.txn.AgentMovement;
import com.sourcetrace.eses.order.entity.txn.AgentMovementLocation;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.PeriodicInspection;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.crypto.ICryptoUtil;
import com.sourcetrace.esesw.entity.profile.Branch;
import com.sourcetrace.esesw.entity.profile.CommissionAccount;
import com.sourcetrace.esesw.entity.profile.Currency;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.ESECard;
import com.sourcetrace.esesw.entity.profile.Vendor;
import com.sourcetrace.esesw.entity.profile.WarehousePayment;
import com.sourcetrace.esesw.entity.txn.ESETxn;

// TODO: Auto-generated Javadoc
/**
 * The Class AgentService.
 * @author $Author: antronivan $
 * @version $Rev: 425 $, $Date: 2009-11-23 08:22:16 +0530 (Mon, 23 Nov 2009) $
 */
@Transactional
@Service
public class AgentService implements IAgentService {

    /** The Constant PROCUREMENT. */
    public static final String PROCUREMENT = "procurement";

    public static final String DISTRIBUTION = "distribution";

    public static final String PAYMENT = "payment";

    private static final Logger LOGGER = Logger.getLogger(AgentService.class);

    @Autowired
    private IUniqueIDGenerator idGenerator;

    @Autowired
    private IAgentDAO agentDAO;
    /*
     * @Autowired private ITransactionDAO transactionDAO;
     */
    @Autowired
    private ICardService cardService;

    /** The currency dao. */
    // private ICurrencyDAO currencyDAO;

    /** The commission fee dao. */

    // private ICommissionFeeDAO commissionFeeDAO;

    /** The account dao. */
    @Autowired
    private IAccountDAO accountDAO;

    /** The crypto util. */
    @Autowired
    private ICryptoUtil cryptoUtil;

    /** The report entity. */
    private String reportEntity;

    /** The filter exclude properties. */
    private List<String> filterExcludeProperties;

    /** The velocity engine. */
    private VelocityEngine velocityEngine;

    /** The mail sender. */
    private MailSender mailSender;

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#addAgent(com.ese.entity.profile .Agent)
     */
    public void addAgent(Agent agent) {

    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#addCommissionAccounts(com.ese.entity
     * .profile.CommissionAccount)
     */
    public void addCommissionAccounts(CommissionAccount commissionAccount) {

    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#agentCredentials(java.lang.String)
     */
    public Set<TxnCredential> agentCredentials(String agentId) {

        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#clientTransactionTypes()
     */
    public List<TransactionType> clientTransactionTypes() {

        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#editAgentCredential(com.ese.entity .profile.Agent)
     */
    public void editAgentCredential(Agent agent) {

    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#editAgentProfile(com.ese.entity .profile.Agent)
     */
    public void editAgentProfile(Agent agent) {
    	agent.setRevisionNumber(DateUtil.getRevisionNumber());
        agentDAO.update(agent);

    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#editAgentTxnCredential(com.ese.
     * entity.profile.Agent)
     */
    public void editAgentTxnCredential(Agent agent) {

    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#editCommissionAccounts(com.ese.
     * entity.profile.CommissionAccount)
     */
    public void editCommissionAccounts(CommissionAccount commissionAccount) {

    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#findAgent(long)
     */
    public Agent findAgent(long id) {

        return agentDAO.findAgent(id);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#findAgentByAgentId(java.lang.String )
     */
    public Agent findAgentByAgentId(String agentId) {

        return agentDAO.findAgentByAgentId(agentId);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#findAgentByTempOrOrginalId(java .lang.String)
     */
    public Agent findAgentByTempOrOrginalId(String agentId) {

        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#findAgents(java.util.List)
     */

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#findCurrencyByCode(java.lang.String )
     */
    public Currency findCurrencyByCode(String currency) {

        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#findPOSAgent(java.lang.String)
     */
    public Agent findPOSAgent(String agentId) {

        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#findTransactionTypes(java.util. List)
     */

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#findTxnTypeById(int)
     */
    public TransactionType findTxnTypeById(int parseLong) {

        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#isAgentCommissionGroup(com.ese.
     * entity.txn.fee.CommissionFeeGroup)
     */
    public boolean isAgentCommissionGroup(CommissionFeeGroup commissionFeeGroup) {

        return false;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#isAgentHaveTransaction(java.lang .String)
     */
    public boolean isAgentHaveTransaction(String agentId) {

        return false;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#isBranchHaveAgent(com.ese.entity .profile.Branch)
     */
    public boolean isBranchHaveAgent(Branch branch) {

        return false;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#listAgentByCommerceId(java.lang .String[])
     */
    public List<Agent> listAgentByCommerceId(String[] shopMap) {

        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#listAgentTransactionTypes()
     */
    public List<TransactionType> listAgentTransactionTypes() {

        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#listAgentType()
     */
    public List<AgentType> listAgentType() {

        return agentDAO.listAgentType();
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#listAgents()
     */
    public List<Agent> listAgents() {

        return agentDAO.listAgents();
    }

    /**
     * Gets the agent dao.
     * @return the agent dao
     */
    public IAgentDAO getAgentDAO() {

        return agentDAO;
    }

    /**
     * Sets the agent dao.
     * @param agentDAO the new agent dao
     */
    public void setAgentDAO(IAgentDAO agentDAO) {

        this.agentDAO = agentDAO;
    }

    /**
     * Gets the account dao.
     * @return the account dao
     */
    public IAccountDAO getAccountDAO() {

        return accountDAO;
    }

    /**
     * Sets the account dao.
     * @param accountDAO the new account dao
     */
    public void setAccountDAO(IAccountDAO accountDAO) {

        this.accountDAO = accountDAO;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#listAgentsByIdentiyNumber(java. lang.String)
     */
    public List<Agent> listAgentsByIdentiyNumber(String identityNumber) {

        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#listClientTransactionTypes()
     */
    public List<TransactionType> listClientTransactionTypes() {

        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#listClientTransactionTypesByProduct
     * (java.lang.String)
     */
    public List<TransactionType> listClientTransactionTypesByProduct(String product) {

        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#listESETransactionTypes()
     */
    public List<TransactionType> listESETransactionTypes() {

        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#listEnrollmentAgents()
     */
    public List<Agent> listEnrollmentAgents() {

        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#listEnrollmentAgentsHaveCredential ()
     */
    public List<Agent> listEnrollmentAgentsHaveCredential() {

        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#listPOSAgents()
     */
    public List<Agent> listPOSAgents() {

        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#listPOSAgentsHaveCredential()
     */
    public List<Agent> listPOSAgentsHaveCredential() {

        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#listTransactionTypes()
     */
    public List<TransactionType> listTransactionTypes() {

        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#removeAgent(com.ese.entity.profile .Agent)
     */
    public void removeAgent(Agent agent) {

        agentDAO.delete(agent);

    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#validAgent(java.lang.String, java.lang.String,
     * java.lang.String)
     */
    public boolean validAgent(String enrollmentStationId, String agentId, String passwd) {

        return false;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#validPOSAgent(java.lang.String)
     */
    public boolean validPOSAgent(String agentId) {

        return false;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#createAgent(com.ese.entity.profile .Agent)
     */
    public void createAgent(Agent agent) {
    	agent.setRevisionNumber(DateUtil.getRevisionNumber());
        agentDAO.save(agent);

    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#findAgent(java.lang.String)
     */
    public Agent findAgent(String profileId) {

        return agentDAO.findAgent(profileId);
    }

    /**
     * Sets the report entity.
     * @param reportEntity the new report entity
     */
    public void setReportEntity(String reportEntity) {

        this.reportEntity = reportEntity;
    }

    /**
     * Gets the report entity.
     * @return the report entity
     */
    public String getReportEntity() {

        return reportEntity;
    }

    /**
     * Sets the filter exclude properties.
     * @param filterExcludeProperties the new filter exclude properties
     */
    public void setFilterExcludeProperties(List<String> filterExcludeProperties) {

        this.filterExcludeProperties = filterExcludeProperties;
    }

    /**
     * Gets the filter exclude properties.
     * @return the filter exclude properties
     */
    public List<String> getFilterExcludeProperties() {

        return filterExcludeProperties;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#listAgentNotMappedwithDevice()
     */
    public List<Agent> listAgentNotMappedwithDevice() {

        return agentDAO.listAgentNotMappedwithDevice();
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#findAgentIdFromEseTxn(java.lang .String)
     */
    public boolean findAgentIdFromEseTxn(String profileId) {

        return agentDAO.findAgentIdFromEseTxn(profileId);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#findAgentIdFromDevice(long)
     */
    public boolean findAgentIdFromDevice(long id) {

        return agentDAO.findAgentIdFromDevice(id);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#findAgentByProfileId(java.lang. String)
     */
    public Agent findAgentByProfileId(String profileId) {

        return agentDAO.findAgentByProfileId(profileId);
    }

    /**
     * Sets the mail sender.
     * @param mailSender the new mail sender
     */
    public void setMailSender(MailSender mailSender) {

        this.mailSender = mailSender;
    }

    /**
     * Gets the mail sender.
     * @return the mail sender
     */
    public MailSender getMailSender() {

        return mailSender;
    }

    /**
     * Gets the velocity engine.
     * @return the velocity engine
     */
    public VelocityEngine getVelocityEngine() {

        return velocityEngine;
    }

    /**
     * Sets the velocity engine.
     * @param velocityEngine the new velocity engine
     */
    public void setVelocityEngine(VelocityEngine velocityEngine) {

        this.velocityEngine = velocityEngine;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#getEmailMessage(com.ese.entity. profile.Agent)
     */
    public String getEmailMessage(Agent agent) {

        Map<String, String> model = new HashMap<String, String>();
        model.put("agentId", agent.getProfileId());
        if (agent.getPersonalInfo() != null) {
            model.put("firstName", agent.getPersonalInfo().getFirstName() != null
                    ? agent.getPersonalInfo().getFirstName() : "");
            model.put("middleName", agent.getPersonalInfo().getMiddleName() != null
                    ? agent.getPersonalInfo().getMiddleName() : "");
            model.put("lastName", agent.getPersonalInfo().getLastName() != null
                    ? agent.getPersonalInfo().getLastName() : "");
            model.put("secondLastName", agent.getPersonalInfo().getSecondLastName() != null
                    ? agent.getPersonalInfo().getSecondLastName() : "");
        }

        String emailTemplate = null;

        model.put("agentId", agent.getProfileId() != null ? agent.getProfileId() : "");
        emailTemplate = "agent.vm";

        String text = null;
        try {
            // create email text using the template
            text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, emailTemplate,
                    "UTF-8", model);
        } catch (VelocityException e) {
            LOGGER.error("Error in building email text", e);
        }

        return text;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#removeAgentServiceLocationMapping (long)
     */
    public void removeAgentServiceLocationMapping(long id) {

        agentDAO.removeAgentServiceLocationMapping(id);

    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#updateAgentBODStatus(String, int)
     */
    public void updateAgentBODStatus(String profileId, int status) {

        agentDAO.updateAgentBODStatus(profileId, status);

    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#findAgentExistForServicePoint(long)
     */
    public boolean findAgentExistForServicePoint(long servicePointId) {

        return agentDAO.findAgentExistForServicePoint(servicePointId);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#findAgents(java.util.List)
     */
    public Set<Agent> findAgents(List<String> agentIds) {

        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#findTransactionTypes(java.util. List)
     */
    public Set<TransactionType> findTransactionTypes(List<Integer> typeIds) {

        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#update(com.ese.entity.profile.ESECard )
     */
    /**
     * Update.
     * @param tempCard the temp card
     */
    public void update(ESECard tempCard) {

        agentDAO.update(tempCard);

    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#editAgent(com.ese.entity.profile .Agent)
     */
    public void editAgent(Agent agent) {

        agentDAO.update(agent);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#listAgent()
     */
    public List<Agent> listAgent() {

        return agentDAO.listAgent();
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#findAgentMappedWithServiceLocation (long)
     */
    public boolean findAgentMappedWithServiceLocation(long serviceLocationId) {

        return agentDAO.findAgentMappedWithServiceLocation(serviceLocationId);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#editTransactionAgent(com.ese.entity
     * .profile.Agent)
     */
    public void editTransactionAgent(Agent agent) {

        agentDAO.merge(agent);
    }

    /*
     * (non-Javadoc)
     * @seecom.ese.service.profile.IAgentService# listWarehouseNameByAgentIdAndServicePointId(long,
     * long)
     */
    public List<String> listWarehouseNameByAgentIdAndServicePointId(long agentId,
            long servicePointId) {

        return agentDAO.listWarehouseNameByAgentIdAndServicePointId(agentId, servicePointId);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#listWarehouseNameByServicePointId (long)
     */
    public List<String> listWarehouseNameByServicePointId(long id) {

        return agentDAO.listWarehouseNameByServicePointId(id);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#removeAgentWarehouseMappingByAgentId (long)
     */
    public void removeAgentWarehouseMappingByAgentId(long id) {

        agentDAO.removeAgentWarehouseMappingByAgentId(id);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#findAgentMappedWithWarehouse(long)
     */
    public boolean findAgentMappedWithWarehouse(long id) {

        return agentDAO.findAgentMappedWithWarehouse(id);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#findAgentbyServicePointId(long)
     */
    public List<Agent> findAgentbyServicePointId(long id) {

        return agentDAO.findAgentbyServicePointId(id);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#listAgentByServicePoint(java.lang .String,
     * java.lang.String)
     */
    public List<Agent> listAgentByServicePoint(String exceptThisAgent, String servicePointName) {

        return agentDAO.listAgentByServicePoint(exceptThisAgent, servicePointName);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#updateAgentReceiptNoSequence(java .lang.String,
     * java.lang.String)
     */
    public void updateAgentReceiptNoSequence(String agentId, String offlineReceiptNo) {

        agentDAO.updateAgentReceiptNoSequence(agentId, offlineReceiptNo);

    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#updateAgentOrderNoSeq(java.lang .String,
     * java.lang.String)
     */
    public void updateAgentOrderNoSeq(String agentId, String inventoryOrderNo) {

        agentDAO.updateAgentOrderNoSeq(agentId, inventoryOrderNo);

    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#updateAgentDeliveryNoSeq(java.lang .String,
     * java.lang.String)
     */
    public void updateAgentDeliveryNoSeq(String agentId, String inventoryDeliveryNo) {

        agentDAO.updateAgentDeliveryNoSeq(agentId, inventoryDeliveryNo);

    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#isValidAgentLogin(java.lang.String,
     * java.lang.String)
     */
    public String isValidAgentLogin(String userName, String password) {

        Agent agent = agentDAO.findAgentByProfileId(userName);
        if (ObjectUtil.isEmpty(agent))
            return "invalid.userName";
        if (Agent.ACTIVE != agent.getStatus())
            return "agent.inactive";
        if (!password.equals(agent.getPassword()))
            return "invalid.combination";
        if (ESETxn.BOD == agent.getBodStatus())
            return "bod.exist.device";
        if (ESETxn.WEB_BOD == agent.getBodStatus())
            return "bod.exist.web";
        return null;

    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#createESECard(java.lang.String, java.lang.String,
     * java.util.Date, int)
     */
    public void createESECard(String profileId, String cardId, Date txnTime, int type) {

        ESECard card = new ESECard();
        card.setCardId(cardId);
        card.setType(type);
        card.setCreateTime(txnTime);
        card.setIssueDate(txnTime);
        card.setProfileId(profileId);
        card.setStatus(ESECard.INACTIVE);
        card.setCardRewritable(ESECard.IS_REWRITABLE_NO);
        agentDAO.save(card);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#createESEAccount(java.lang.String,
     * java.lang.String, java.util.Date, int)
     */
    public void createESEAccount(String profileId, String accountNo, Date txnTime, int type) {

        ESEAccount account = new ESEAccount();
        account.setAccountNo(accountNo);
        account.setType(type);
        if (ESEAccount.AGENT_ACCOUNT == type) {
            account.setAccountType(ESEAccount.OPERATOR_ACCOUNT);
            account.setStatus(ESEAccount.ACTIVE);
        } else if (ESEAccount.FARMER_ACCOUNT == type) {
            account.setAccountType(ESEAccount.SAVING_BANK_ACCOUNT);
            account.setStatus(ESEAccount.ACTIVE);
        }
        account.setBalance(0.0);
        account.setAccountOpenDate(txnTime);
        account.setCreateTime(txnTime);
        account.setProfileId(profileId);
        agentDAO.save(account);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#updateAgentShopDealerCardIdSequence
     * (java.lang.String, java.lang.String)
     */
    public void updateAgentShopDealerCardIdSequence(String agentId, String cardId) {

        agentDAO.updateAgentShopDealerCardIdSequence(agentId, cardId);

    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#addAgentMovement(com.sourcetrace
     * .eses.order.entity. txn.AgentMovement)
     */
    public void addAgentMovement(AgentMovement agentMovement) {

        agentDAO.save(agentMovement.getAgroTransaction());
        agentDAO.save(agentMovement);

    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#listAgentMovementImages(java.lang .Long)
     */
    public List<AgentMovementLocation> listAgentMovementImages(Long agentMovementId) {

        return agentDAO.listAgentMovementImages(agentMovementId);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#loadAgentMoventImages(java.lang .String)
     */
    public AgentMovementLocation loadAgentMoventImages(String id) {

        return agentDAO.loadAgentMoventImages(id);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#findAgentNameByAgentId(java.lang .String)
     */
    public String findAgentNameByAgentId(String agentId) {

        return agentDAO.findAgentNameByAgentId(agentId);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#listAgentIdName()
     */
    public List<Object[]> listAgentIdName() {

        return agentDAO.listAgentIdName();
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#updateIdSeq()
     */
    public void updateIdSeq() {

        agentDAO.updateFarmerIdSequence();
        agentDAO.updateShopDealerIdSequence();

    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#findAgentTypeById(long)
     */
    public AgentType findAgentTypeById(long id) {

        return agentDAO.findAgentTypeById(id);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#listAgentByAgentType(java.lang. String)
     */
    public List<Agent> listAgentByAgentType(String code) {

        return agentDAO.listAgentByAgentType(code);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#listOfAgentByArea(java.lang.String)
     */
    public List<Agent> listOfAgentByArea(String servicePointCode) {

        return agentDAO.listOfAgentByArea(servicePointCode);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#isValidAgentLogin(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public String isValidAgentLogin(String userName, String password, String txn) {

        Agent agent = agentDAO.findAgentByProfileId(userName);
        if (ObjectUtil.isEmpty(agent))
            return "invalid.userName";
        if (!(txn.equals(PROCUREMENT) || txn.equals(DISTRIBUTION) || txn.equals(PAYMENT))) {
            if (!AgentType.COOPERATIVE_MANAGER.equals(agent.getAgentType().getCode()))
                return "agent.notAllowed";
        }
        if (Agent.ACTIVE != agent.getStatus())
            return "agent.inactive";
        String tempPassword = cryptoUtil
                .encrypt(StringUtil.getMulipleOfEight(agent.getProfileId() + password));

        if (!tempPassword.equals(agent.getPassword()))
            return "invalid.combination";
        if (AgentType.COOPERATIVE_MANAGER.equals(agent.getAgentType().getCode())
                && ObjectUtil.isListEmpty(agent.getWareHouses()))
            return "coopeartive.unavailable";
        if (txn.equals(PROCUREMENT) || txn.equals(DISTRIBUTION) || txn.equals(PAYMENT)) {
            if (AgentType.FIELD_STAFF.equals(agent.getAgentType().getCode())) {
                ESEAccount agentAccount = accountDAO.findAccountByProfileIdAndProfileType(
                        agent.getProfileId(), ESEAccount.AGENT_ACCOUNT);
                if (ObjectUtil.isEmpty(agentAccount))
                    return "acct.unavailable";
                if (ESEAccount.ACTIVE != agentAccount.getStatus())
                    return "acct.inActive";
                if (ObjectUtil.isListEmpty(agent.getWareHouses()))
                    return "samithi.unavailable";
            }
        }
        if (ESETxn.BOD == agent.getBodStatus())
            return "bod.exist.device";
        if (ESETxn.WEB_BOD == agent.getBodStatus())
            return "bod.exist.web";
        return null;
    }

    /*
     * (non-Javadoc)
     * @seecom.ese.service.profile.IAgentService#
     * listFieldStaffsByCoopetiveManagerProfileId(java.lang .String)
     */
    public List<Agent> listFieldStaffsByCoopetiveManagerProfileId(String agentId) {

        return agentDAO.listFieldStaffsByCoopetiveManagerProfileId(agentId);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#listAvailableSamithiByAgentId(java .lang.Long)
     */
    public List<String> listAvailableSamithiByAgentId(Long agentId, Long cooperativeId) {

        return agentDAO.listAvailableSamithiByAgentId(agentId, cooperativeId);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#listSamithiByCooperativeId(java .lang.Long)
     */
    public List<String> listSamithiByCooperativeId(Long id) {

        return agentDAO.listSamithiByCooperativeId(id);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#listSelectedSamithiByAgentId(java .lang.Long)
     */
    public List<String> listSelectedSamithiByAgentId(Long agentId, Long cooperativeId) {

        return agentDAO.listSelectedSamithiByAgentId(agentId, cooperativeId);
    }

    public List<String> listSelectedSamithiById(Long id) {

        return agentDAO.listSelectedSamithiById(id);
    }

    public List<String> listAvailableSamithi() {

        return agentDAO.listAvailableSamithi();
    }

    /*
     * (non-Javadoc)
     * @see com.ese.service.profile.IAgentService#findAgentByCoOperative(long)
     */
    public Agent findAgentByCoOperative(long coOperativeId) {

        // TODO Auto-generated method stub
        return agentDAO.findAgentByCoOperative(coOperativeId);
    }

    /**
     * Gets the crypto util.
     * @return the crypto util
     */
    public ICryptoUtil getCryptoUtil() {

        return cryptoUtil;
    }

    /**
     * Sets the crypto util.
     * @param cryptoUtil the new crypto util
     */
    public void setCryptoUtil(ICryptoUtil cryptoUtil) {

        this.cryptoUtil = cryptoUtil;
    }

    public PeriodicInspection findPeriodicInspectionById(Long id) {

        return agentDAO.findPeriodicInspectionById(id);
    }

    public byte[] findPeriodicInspectionFarmerVoiceById(Long id) {

        return agentDAO.findPeriodicInspectionFarmerVoiceById(id);
    }

    public List<Object[]> listFieldStaffByTxnType(String txnType) {

        return agentDAO.listFieldStaffByTxnType(txnType);
    }

    /*
     * public List<Agent> findFieldStaffByCooperativeId(Long id){ return
     * agentDAO.findFieldStaffByCooperativeId(id); }
     */
    public List<Agent> listOfWareHouseProuctFieldStaff(String code) {

        return agentDAO.listOfWareHouseProuctFieldStaff(code);
    }

    public void updateAgentFarmerCardIdSequence(String agentId, String cardId) {

        agentDAO.updateAgentFarmerCardIdSequence(agentId, cardId);

    }

    public List<Byte> listAgentsByStatus() {

        return agentDAO.listAgentsByStatus();
    }

    public List<Agent> findAgentList() {

        return agentDAO.findAgentList();
    }

    public void addVendor(Vendor vendor) {

        // TODO Auto-generated method stub
        this.agentDAO.save(vendor);
    }

    public void editVendor(Vendor tempVendor) {

        // TODO Auto-generated method stub
        this.agentDAO.update(tempVendor);
    }

    public Vendor findVendor(Long id) {

        // TODO Auto-generated method stub
        return this.agentDAO.findVendor(id);
    }

    public void removeVendor(Vendor vendor) {

        // TODO Auto-generated method stub
        this.agentDAO.delete(vendor);
    }

    public void createAgentESEAccount(String profileId, String accountNo, Date txnTime, int type,
            Agent agent,String branchId) {

        ESEAccount account = new ESEAccount();
        account.setAccountNo(accountNo);
        account.setType(type);
        if (ESEAccount.AGENT_ACCOUNT == type) {
            account.setAccountType(ESEAccount.OPERATOR_ACCOUNT);
            account.setStatus(ESEAccount.ACTIVE);
        } else if (ESEAccount.FARMER_ACCOUNT == type) {
            account.setAccountType(ESEAccount.SAVING_BANK_ACCOUNT);
            account.setStatus(ESEAccount.ACTIVE);
        }
        account.setBranchId(branchId);
        account.setBalance(0.0);
        account.setAccountOpenDate(txnTime);
        account.setCreateTime(txnTime);
        account.setProfileId(profileId);
        account.setCashBalance(0.00);
        agentDAO.save(account);
    }

    public WarehousePayment findVendorId(long vendorId) {

        // TODO Auto-generated method stub
        return agentDAO.findVendorId(vendorId);
    }

    @Override
    public void addLocationHistory(LocationHistory locationHistory) {

        agentDAO.saveOrUpdate(locationHistory);

    }

	@Override
	public Agent findAgentByProfileAndBranchId(String profileId,String branchId) {
		 return agentDAO.findAgentByProfileAndBranchId(profileId,branchId);
	}
	
	public List<Warehouse> listAvailableGroupByAgentId(Long agentId){
		return agentDAO.listAvailableGroupByAgentId(agentId);
	}
	
	public List<Warehouse> listSelectedGroupByAgentId(Long agentId){
		return agentDAO.listSelectedGroupByAgentId(agentId);
	}
	
	public List<Warehouse> listSamithies(){
		return agentDAO.listSamithies();
	}

	@Override
	public List<Byte> listAgentsByStatusBasedOnBranch(String branchIdValue) {
		return agentDAO.listAgentsByStatusBasedOnBranch(branchIdValue);
	}

	@Override
	public List<PeriodicInspection> listMoileUser() {
		// TODO Auto-generated method stub
		return agentDAO.listMoileUser();
	}
	
	public Integer findMobileUserCount(){
		return agentDAO.findMobileUserCount();
	}

	@Override
	public Integer findMobileUserCountByMonth(Date sDate, Date eDate) {
		
		return agentDAO.findMobileUserCountByMonth(sDate,eDate);
	}
	
	public Agent findAgentByAgentId(String agentId, String tenantId,String branchId) {

        return agentDAO.findAgentByAgentId(agentId,tenantId,branchId);
    }

    @Override
    public Agent findAgentByProfileId(String agentId, String tenantId, String branchId) {

        return agentDAO.findAgentByProfileId(agentId,tenantId,branchId);
    }

	@Override
	public List<Object[]> listAgentNameProfIdAndIdByBranch(String branchId) {
		return agentDAO.listAgentNameProfIdAndIdByBranch(branchId);
	}

    @Override
    public AgentAccessLog findAgentAccessLogByAgentId(String agentId,Date txnDate) {

        return agentDAO.findAgentAccessLogByAgentId(agentId,txnDate);
    }

    @Override
    public AgentAccessLogDetail findAgentAccessLogDetailByTxn(Long accessId, String txnType) {

        return agentDAO.findAgentAccessLogDetailByTxn(accessId,txnType);
    }
    
    @Override
    public void save(Object object) {
        agentDAO.save(object);
    }
    
    @Override
    public void update(Object object) {
        agentDAO.update(object);
    }

	@Override
	public List<Object[]> listOfAgentByTraining() {
		// TODO Auto-generated method stub
		return agentDAO.listOfAgentByTraining();
	}

	@Override
	public List<Object[]> listOfLearningGroupByTraining() {
		// TODO Auto-generated method stub
		return agentDAO.listOfLearningGroupByTraining();
	}

    @Override
    public void addLocationHistory(LocationHistory locationHistory, String tenantId) {
        
        agentDAO.addLocationHistory(locationHistory,tenantId);

        
    }

	@Override
	public List<Agent> listAgentProfileIdByAgentName(String agentName) {
		return agentDAO.listAgentProfileIdByAgentName(agentName);
	}

    @Override
    public long findAgentCountByWarehouseId(long id) {

       
        return agentDAO.findAgentCountByWarehouseId(id);
    }

    @Override
    public AgroTransaction findAgrotxnDetailsByAgentId(String profileId) {

       
        return agentDAO.findAgrotxnDetailsByAgentId(profileId);
    }
    @Override
    public List<Object[]> listAgrotxnDetailsByAgentId(List<String> profileId, List<String> txnType) {

       
        return agentDAO.listAgrotxnDetailsByAgentId(profileId,txnType);
    }

    @Override
    public List<String> findAgentByWareHouseId(long coOperativeId) {

        // TODO Auto-generated method stub
        return agentDAO.findAgentByWareHouseId(coOperativeId);
    }

    @Override
    public List<Object[]> findTxnTypeByTxnTypeAndAgentId(String agentId) {

        // TODO Auto-generated method stub
        return agentDAO.findTxnTypeByTxnTypeAndAgentId(agentId);
    }

    @Override
    public long listPeriodicInspectionByAgentId(List<String> profileId) {

        
        return agentDAO.listPeriodicInspectionByAgentId(profileId);
    }

    @Override
    public List<String> listTxnTypeByAgentId(List<String> agent) {

        
        return agentDAO.listTxnTypeByAgentId(agent);
    }
    
	@Override
	public List<Object[]> listAgentIdNamebyAgroTxn() {
		// TODO Auto-generated method stub
		return agentDAO.listAgentIdNamebyAgroTxn();
	}
	@Override
	public AgentAccessLogDetail listAgnetAccessLogDetailsByIdTxnType(long id, String type) {
		// TODO Auto-generated method stub
		return agentDAO.listAgnetAccessLogDetailsByIdTxnType(id,type);
	}
	
	@Override
	public AgentAccessLogDetail listAgnetAccessLogDetailsByIdTxnType(long id, String type, String msgNo) {
		return agentDAO.listAgnetAccessLogDetailsByIdTxnType(id,type,msgNo);
	}
	
	public long surveyMasterRevisionByDvId(String agentId) {

		return agentDAO.surveyMasterRevisionByDvId(agentId);
	}

	@Override
	public List<Object[]> listMobileUser() {
		return agentDAO.listMobileUser();
	}

	@Override
	public Agent findAgentByProfileIdWithSurvey(String agentId) {
		return agentDAO.findAgentByProfileIdWithSurvey(agentId);
	}
	 public Warehouse listSelectedWarehouseById(Long id) {

	        return agentDAO.listSelectedWarehouseById(id);
	    }
	 public List<Agent> listAgentByAgentTypeNotMappedwithDevice(){
		 return agentDAO.listAgentByAgentTypeNotMappedwithDevice();
	 }

	@Override
	public List<Agent> listAgentAndOperator() {
		 return agentDAO.listAgentAndOperator();
	}

	@Override
	public List<String> listSyncLogins() {
		return agentDAO.listSyncLogins();
	}

	@Override
	public List<Agent> listAgentByWarehouse(long warehouse) {
		return agentDAO.listAgentByWarehouse(warehouse);
	}
	
	public List<Object[]> lastLoginAndVersionByAgent(String profileId){
		return agentDAO.lastLoginAndVersionByAgent(profileId);
	}
	
	public List<Object[]> lastLoginForTrackingByAgent(String profileId){
		return agentDAO.lastLoginForTrackingByAgent(profileId);
	}

	@Override
	public List<Agent> listAgentByAgentTypeWithStatus(String fieldStaff) {
		// TODO Auto-generated method stub
		return agentDAO.listAgentByAgentTypeWithStatus(fieldStaff);
	}

	@Override
	public boolean findAgentsMappedWithWarehouse(long warehouseId) {
		return agentDAO.findAgentsMappedWithWarehouse(warehouseId);
	}

	@Override
	public List<Agent> listAgentByWarehouseAndRevisionNo(long warehouseId, String revisionNo) {
		return agentDAO.listAgentByWarehouseAndRevisionNo(warehouseId, revisionNo);
	}

	@Override
	public List<Agent> listAgentByRevisionNo(String branchId, Long revisionNo) {
		return agentDAO.listAgentByRevisionNo(branchId, revisionNo);
	}
	
	public List<Object[]> listOfTrainingCode(){
		return agentDAO.listOfTrainingCode();
	}
    
	public List<Object> listOfFarmer(){
		return agentDAO.listOfFarmer();
	}

	@Override
	public List<Object[]> findFarmerCountBySamithiId() {
		// TODO Auto-generated method stub
		return agentDAO.findFarmerCountBySamithiId();
	}

	@Override
	public List<Object[]> listAgrotxnDetailsByAgentIdAndTxnType(List<String> agentId, List<String> txnList) {
		// TODO Auto-generated method stub
		return agentDAO.listAgrotxnDetailsByAgentIdAndTxnType(agentId, txnList);
	}

    @Override
    public List<String> findAgentNameByWareHouseId(long coOperativeId) {

        // TODO Auto-generated method stub
        return agentDAO.findAgentNameByWareHouseId(coOperativeId);
    }

}
