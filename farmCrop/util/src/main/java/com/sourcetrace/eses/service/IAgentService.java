/*
 * IAgentService.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.ese.entity.credential.TxnCredential;
import com.ese.entity.txn.fee.CommissionFeeGroup;
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
import com.sourcetrace.esesw.entity.profile.Branch;
import com.sourcetrace.esesw.entity.profile.CommissionAccount;
import com.sourcetrace.esesw.entity.profile.Currency;
import com.sourcetrace.esesw.entity.profile.Vendor;
import com.sourcetrace.esesw.entity.profile.WarehousePayment;

/**
 * The Interface IAgentService.
 * @author $Author: antronivan $
 * @version $Rev: 425 $, $Date: 2009-11-23 08:22:16 +0530 (Mon, 23 Nov 2009) $
 */
public interface IAgentService {

    /**
     * List agents.
     * @return the list< agent>
     */
    public List<Agent> listAgents();

    /**
     * Adds the agent.
     * @param agent the agent
     */
    public void addAgent(Agent agent);

    /**
     * Edits the agent profile.
     * @param agent the agent
     */
    public void editAgentProfile(Agent agent);

    /**
     * Edits the agent credential.
     * @param agent the agent
     */
    public void editAgentCredential(Agent agent);

    /**
     * Edits the agent txn credential.
     * @param agent the agent
     */
    public void editAgentTxnCredential(Agent agent);

    /**
     * Removes the agent.
     * @param agent the agent
     */
    public void removeAgent(Agent agent);

    /**
     * Find agent.
     * @param id the id
     * @return the agent
     */
    public Agent findAgent(long id);

    /**
     * Find agent by agent id.
     * @param agentId the agent id
     * @return the agent
     */
    public Agent findAgentByAgentId(String agentId);

    /**
     * Valid agent.
     * @param enrollmentStationId the enrollment station id
     * @param agentId the agent id
     * @param passwd the passwd
     * @return true, if successful
     */
    public boolean validAgent(String enrollmentStationId, String agentId, String passwd);

    /**
     * List transaction types.
     * @return the list< transaction type>
     */
    public List<TransactionType> listTransactionTypes();

    /**
     * Find transaction types.
     * @param typeIds the type ids
     * @return the set< transaction type>
     */
    public Set<TransactionType> findTransactionTypes(List<Integer> typeIds);

    /**
     * Find agents.
     * @param agentIds the agent ids
     * @return the set< agent>
     */
    public Set<Agent> findAgents(List<String> agentIds);

    /**
     * List client transaction types.
     * @return the list< transaction type>
     */
    List<TransactionType> listClientTransactionTypes();

    /**
     * List agent transaction types.
     * @return the list< transaction type>
     */
    List<TransactionType> listAgentTransactionTypes();

    /**
     * List enrollment agents.
     * @return the list< agent>
     */
    List<Agent> listEnrollmentAgents();

    /**
     * List pos agents.
     * @return the list< agent>
     */
    List<Agent> listPOSAgents();

    /**
     * Valid pos agent.
     * @param agentId the agent id
     * @return true, if successful
     */
    boolean validPOSAgent(String agentId);

    /**
     * Find agent by temp or orginal id.
     * @param agentId the agent id
     * @return the agent
     */
    public Agent findAgentByTempOrOrginalId(String agentId);

    /**
     * Checks if is agent have transaction.
     * @param agentId the agent id
     * @return true, if is agent have transaction
     */
    boolean isAgentHaveTransaction(String agentId);

    /**
     * Find currency by code.
     * @param currency the currency
     * @return the currency
     */

    public Currency findCurrencyByCode(String currency);

    /**
     * List agent type.
     * @return the list< agent type>
     */
    public List<AgentType> listAgentType();

    /**
     * Agent credentials.
     * @param agentId the agent id
     * @return the set< txn credential>
     */
    public Set<TxnCredential> agentCredentials(String agentId);

    /**
     * Checks if is branch have agent.
     * @param branch the branch
     * @return true, if is branch have agent
     */
    public boolean isBranchHaveAgent(Branch branch);

    /**
     * Client transaction types.
     * @return the list< transaction type>
     */
    List<TransactionType> clientTransactionTypes();

    /**
     * Adds the commission accounts.
     * @param commissionAccount the commission account
     */
    public void addCommissionAccounts(CommissionAccount commissionAccount);

    /**
     * Edits the commission accounts.
     * @param commissionAccount the commission account
     */
    public void editCommissionAccounts(CommissionAccount commissionAccount);

    /**
     * List enrollment agents have credential.
     * @return the list< agent>
     */
    List<Agent> listEnrollmentAgentsHaveCredential();

    /**
     * List pos agents have credential.
     * @return the list< agent>
     */
    List<Agent> listPOSAgentsHaveCredential();

    /**
     * List client transaction types by product.
     * @param product the product
     * @return the list< transaction type>
     */
    public List<TransactionType> listClientTransactionTypesByProduct(String product);

    /**
     * Find txn type by id.
     * @param parseLong the parse long
     * @return the transaction type
     */
    public TransactionType findTxnTypeById(int parseLong);

    public boolean isAgentCommissionGroup(CommissionFeeGroup commissionFeeGroup);

    /**
     * Find pos agent.
     * @param agentId the agent id
     * @return the agent
     */
    public Agent findPOSAgent(String agentId);

    /**
     * List ese transaction types.
     * @return the list< transaction type>
     */
    public List<TransactionType> listESETransactionTypes();

    /**
     * List agents by identiy number.
     * @param identityNumber the identity number
     * @return the list< agent>
     */
    public List<Agent> listAgentsByIdentiyNumber(String identityNumber);

    /**
     * List agent by commerce id.
     * @param shopMap the shop map
     * @return the list< agent>
     */
    public List<Agent> listAgentByCommerceId(String[] shopMap);

    /**
     * Creates the agent.
     * @param agent the agent
     */
    public void createAgent(Agent agent);

    /**
     * Find agent.
     * @param profileId the profile id
     * @return the agent
     */
    public Agent findAgent(String profileId);

    /**
     * List agent not mappedwith device.
     * @return the list< agent>
     */
    public List<Agent> listAgentNotMappedwithDevice();

    /**
     * Find agent id from ese txn.
     * @param profileId the profile id
     * @return true, if successful
     */
    public boolean findAgentIdFromEseTxn(String profileId);

    /**
     * Find agent id from device.
     * @param id the id
     * @return true, if successful
     */
    public boolean findAgentIdFromDevice(long id);

    /**
     * Find agent by profile id.
     * @param profileId the profile id
     * @return the agent
     */
    public Agent findAgentByProfileId(String profileId);

    /**
     * Gets the email message.
     * @param agent the agent
     * @return the email message
     */
    public String getEmailMessage(Agent agent);

    /**
     * Removes the agent service location mapping.
     * @param id the id
     */
    public void removeAgentServiceLocationMapping(long id);

    /**
     * Update agent bod status.
     * @param profileId the profile id
     * @param status the status
     */
    public void updateAgentBODStatus(String profileId, int status);

    /**
     * Find agent exist for service point.
     * @param servicePointId the service point id
     * @return true, if successful
     */
    public boolean findAgentExistForServicePoint(long servicePointId);

    /**
     * Edits the agent.
     * @param agent the agent
     */
    public void editAgent(Agent agent);

    /**
     * List agent.
     * @return the list< agent>
     */
    public List<Agent> listAgent();

    /**
     * Find agent mapped with service location.
     * @param serviceLocationId the service location id
     * @return true, if successful
     */
    public boolean findAgentMappedWithServiceLocation(long serviceLocationId);

    /**
     * Edits the transaction agent.
     * @param agent the agent
     */
    public void editTransactionAgent(Agent agent);

    /**
     * List warehouse name by agent id and service point id.
     * @param agentId the agent id
     * @param servicePointId the service point id
     * @return the list< string>
     */
    public List<String> listWarehouseNameByAgentIdAndServicePointId(long agentId,
            long servicePointId);

    /**
     * List warehouse name by service point id.
     * @param id the id
     * @return the list< string>
     */
    public List<String> listWarehouseNameByServicePointId(long id);

    /**
     * Removes the agent warehouse mapping by agent id.
     * @param id the id
     */
    public void removeAgentWarehouseMappingByAgentId(long id);

    /**
     * Find agent mapped with warehouse.
     * @param id the id
     * @return true, if successful
     */
    public boolean findAgentMappedWithWarehouse(long id);

    /**
     * Find agentby service point id.
     * @param id the id
     * @return the list< agent>
     */
    public List<Agent> findAgentbyServicePointId(long id);

    /**
     * List agent by service point.
     * @param exceptThisAgent the except this agent
     * @param servicePointName the service point name
     * @return the list< agent>
     */
    public List<Agent> listAgentByServicePoint(String exceptThisAgent, String servicePointName);

    /**
     * Update agent receipt no sequence.
     * @param agentId the agent id
     * @param offlineReceiptNo the offline receipt no
     */
    public void updateAgentReceiptNoSequence(String agentId, String offlineReceiptNo);

    /**
     * Update agent order no seq.
     * @param agentId the agent id
     * @param inventoryOrderNo the inventory order no
     */
    public void updateAgentOrderNoSeq(String agentId, String inventoryOrderNo);

    /**
     * Update agent delivery no seq.
     * @param agentId the agent id
     * @param inventoryDeliveryNo the inventory delivery no
     */
    public void updateAgentDeliveryNoSeq(String agentId, String inventoryDeliveryNo);

    /**
     * Checks if is valid agent login.
     * @param userName the user name
     * @param password the password
     * @return the string
     */
    public String isValidAgentLogin(String userName, String password);

    /**
     * Creates the ese card.
     * @param farmerId the farmer id
     * @param farmerCardId the farmer card id
     * @param txnTime the txn time
     * @param type the type
     */
    public void createESECard(String farmerId, String farmerCardId, Date txnTime, int type);

    /**
     * Creates the ese account.
     * @param farmerId the farmer id
     * @param farmerAcctNo the farmer acct no
     * @param txnTime the txn time
     * @param type the type
     */
    public void createESEAccount(String farmerId, String farmerAcctNo, Date txnTime, int type);

    /**
     * Update agent shop dealer card id sequence.
     * @param agentId the agent id
     * @param cardId the card id
     */
    public void updateAgentShopDealerCardIdSequence(String agentId, String cardId);

    public void addAgentMovement(AgentMovement agentMovement);

    /**
     * List agent movement images.
     * @param agentMovementId the agent movement id
     * @return the list< agent movement location>
     */
    public List<AgentMovementLocation> listAgentMovementImages(Long agentMovementId);

    /**
     * Load agent movent images.
     * @param id the id
     * @return the agent movement location
     */
    public AgentMovementLocation loadAgentMoventImages(String id);

    /**
     * Find agent name by agent id.
     * @param agentId the agent id
     * @return the string
     */
    public String findAgentNameByAgentId(String agentId);

    /**
     * List agent id name.
     * @return the list< object[]>
     */
    public List<Object[]> listAgentIdName();

    /**
     * Update id seq.
     */
    public void updateIdSeq();

    /**
     * Find agent type by id.
     * @param id the id
     * @return the agent type
     */
    public AgentType findAgentTypeById(long id);

    /**
     * List agent by agent type.
     * @param code the code
     * @return the list< agent>
     */
    public List<Agent> listAgentByAgentType(String code);

    /**
     * List of agent by area.
     * @param servicePointCode the service point code
     * @return the list< agent>
     */
    public List<Agent> listOfAgentByArea(String servicePointCode);

    /**
     * Checks if is valid agent login.
     * @param userName the user name
     * @param password the password
     * @param txn the txn
     * @return the string
     */
    public String isValidAgentLogin(String userName, String password, String txn);

    /**
     * List field staffs by coopetive manager profile id.
     * @param agentId the agent id
     * @return the list< agent>
     */
    public List<Agent> listFieldStaffsByCoopetiveManagerProfileId(String agentId);

    /**
     * List samithi by cooperative id.
     * @param id the id
     * @return the list< string>
     */
    public List<String> listSamithiByCooperativeId(Long id);

    /**
     * List selected samithi by agent id.
     * @param agentId the agent id
     * @param l
     * @return the list< string>
     */
    public List<String> listSelectedSamithiByAgentId(Long agentId, Long cooperativeId);

    /**
     * List available samithi by agent id.
     * @param agentId the agent id
     * @return the list< string>
     */
    public List<String> listAvailableSamithiByAgentId(Long agentId, Long cooperativeId);

    public Agent findAgentByCoOperative(long coOperativeId);

    public PeriodicInspection findPeriodicInspectionById(Long id);

    public byte[] findPeriodicInspectionFarmerVoiceById(Long id);

    public List<Object[]> listFieldStaffByTxnType(String txnType);

    public List<Agent> listOfWareHouseProuctFieldStaff(String code);

    // public List<Agent> findFieldStaffByCooperativeId(Long valueOf);

    public List<String> listSelectedSamithiById(Long id);

    public List<String> listAvailableSamithi();

    public void updateAgentFarmerCardIdSequence(String agentId, String cardId);

    public List<Byte> listAgentsByStatus();

    public List<Agent> findAgentList();

    public Vendor findVendor(Long id);

    public void addVendor(Vendor vendor);

    public void editVendor(Vendor tempVendor);

    public void removeVendor(Vendor vendor);

    public void createAgentESEAccount(String profileId, String accountNo, Date txnTime, int type,
            Agent agent,String branchId);

    public WarehousePayment findVendorId(long id);

    public void addLocationHistory(LocationHistory locationHistory);
    
    public Agent findAgentByProfileAndBranchId(String profileId,String branchId);
    
    public List<Warehouse> listAvailableGroupByAgentId(Long agentId);
    public List<Warehouse> listSelectedGroupByAgentId(Long agentId);
    public List<Warehouse> listSamithies();

	public List<Byte> listAgentsByStatusBasedOnBranch(String branchIdValue);

	public List<PeriodicInspection> listMoileUser();
	
	public Integer findMobileUserCount();
	
	public Integer findMobileUserCountByMonth(Date sDate,Date eDate);
	
	public Agent findAgentByAgentId(String agentId, String tenantId, String branchId);

    public Agent findAgentByProfileId(String agentId, String tenantId, String branchId);
    
    public List<Object[]> listAgentNameProfIdAndIdByBranch(String branchId);
    
    public AgentAccessLog findAgentAccessLogByAgentId(String agentId,Date txnDate);
    
    public AgentAccessLogDetail findAgentAccessLogDetailByTxn(Long accessId,String txnType);
    
    public void save(Object object);
    
    public void update(Object object);

	public List<Object[]> listOfAgentByTraining();

	public List<Object[]> listOfLearningGroupByTraining();

    public void addLocationHistory(LocationHistory locationHistory, String tenantId);

	public List<Agent> listAgentProfileIdByAgentName(String agentName);
   
	public long findAgentCountByWarehouseId(long id);
	
	public AgroTransaction findAgrotxnDetailsByAgentId(String profileId);

    public List<Object[]> listAgrotxnDetailsByAgentId(List<String> profileId, List<String> txnType);
    public List<String> findAgentByWareHouseId(long coOperativeId);

    public List<Object[]> findTxnTypeByTxnTypeAndAgentId(String agentId);
    public long listPeriodicInspectionByAgentId(List<String> profileId);

    public List<String> listTxnTypeByAgentId(List<String> agent);
    
	public List<Object[]> listAgentIdNamebyAgroTxn();

	public AgentAccessLogDetail listAgnetAccessLogDetailsByIdTxnType(long id, String type);
	
	public AgentAccessLogDetail listAgnetAccessLogDetailsByIdTxnType(long id, String type,String msgNo);
	
	public long surveyMasterRevisionByDvId(String agentId);
	List<Object[]> listMobileUser();

	public Agent findAgentByProfileIdWithSurvey(String agentId);
	
	 public Warehouse listSelectedWarehouseById(Long id);

	public List<Agent> listAgentByAgentTypeNotMappedwithDevice();

	public List<Agent> listAgentAndOperator();

	public List<String> listSyncLogins();

	public List<Agent> listAgentByWarehouse(long warehouse);
	
	public List<Object[]> lastLoginAndVersionByAgent(String profileId);
	
	public List<Object[]> lastLoginForTrackingByAgent(String profileId);

	public List<Agent> listAgentByAgentTypeWithStatus(String fieldStaff);

	public boolean findAgentsMappedWithWarehouse(long warehouseId);

	public List<Agent> listAgentByWarehouseAndRevisionNo(long warehouseId, String revisionNo);

	public List<Agent> listAgentByRevisionNo(String branchId, Long revisionNo);
	
	public List<Object[]> listOfTrainingCode();

	public List<Object> listOfFarmer();

	public List<Object[]> findFarmerCountBySamithiId();

	public List<Object[]> listAgrotxnDetailsByAgentIdAndTxnType(List<String> agentId, List<String> txnList);
	
	public List<String> findAgentNameByWareHouseId(long coOperativeId);

}
