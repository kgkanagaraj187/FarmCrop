/*
 * IAgentDAO.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.Date;
import java.util.List;

import com.ese.entity.credential.MccCode;
import com.ese.entity.txn.fee.CommissionFeeGroup;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.AgentType;
import com.sourcetrace.eses.entity.LocationHistory;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.AgentAccessLog;
import com.sourcetrace.eses.order.entity.txn.AgentAccessLogDetail;
import com.sourcetrace.eses.order.entity.txn.AgentMovementLocation;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.PeriodicInspection;
import com.sourcetrace.esesw.entity.profile.Branch;
import com.sourcetrace.esesw.entity.profile.ServicePoint;
import com.sourcetrace.esesw.entity.profile.Vendor;
import com.sourcetrace.esesw.entity.profile.WarehousePayment;

public interface IAgentDAO extends IESEDAO {

    /**
     * List agents.
     * @return the list< agent>
     */
    public List<Agent> listAgents();

    /**
     * List enrollment agents.
     * @return the list< agent>
     */
    public List<Agent> listEnrollmentAgents();

    /**
     * List pos agents.
     * @return the list< agent>
     */
    public List<Agent> listPOSAgents();

    /**
     * Find agent by agent id.
     * @param agentId the agent id
     * @return the agent
     */
    public Agent findAgentByAgentId(String agentId);

    /**
     * Find agent by temporary id.
     * @param tempId the temp id
     * @return the agent
     */
    public Agent findAgentByTemporaryId(String tempId);

    /**
     * Find enroll agent by agent id.
     * @param agentId the agent id
     * @return the agent
     */
    public Agent findEnrollAgentByAgentId(String agentId);

    /**
     * Find agent.
     * @param id the id
     * @return the agent
     */
    public Agent findAgent(long id);

    /**
     * Delete.
     * @param agent the agent
     */
    public void delete(Agent agent);

    /**
     * List new agents.
     * @return the list< agent>
     */
    public List<Agent> listNewAgents();

    /**
     * List agents by type.
     * @param type the type
     * @return the list< agent>
     */
    public List<Agent> listAgentsByType(int type);

    /**
     * Find max revision number.
     * @return the long
     */
    public long findMaxRevisionNumber();

    /**
     * Checks if is agent have transaction.
     * @param agentId the agent id
     * @return true, if is agent have transaction
     */
    public boolean isAgentHaveTransaction(String agentId);

    /**
     * List mcc code.
     * @return the list< mcc code>
     */
    public List<MccCode> listMccCode();

    /**
     * List agent type.
     * @return the list< agent type>
     */
    public List<AgentType> listAgentType();

    /**
     * Checks if is branch have agent.
     * @param branch the branch
     * @return true, if is branch have agent
     */
    public boolean isBranchHaveAgent(Branch branch);

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
     * Find agent by identity number.
     * @param identityNumber the identity number
     * @return the agent
     */
    public Agent findAgentByIdentityNumber(String identityNumber);

    /**
     * Find agent by agent id or temp id.
     * @param agentId the agent id
     * @return the agent
     */
    public Agent findAgentByAgentIdOrTempId(String agentId);

    /**
     * Find agent fees by type.
     * @param agentId the agent id
     * @param transactionType the transaction type
     * @param accountType the account type
     * @param currency the currency
     * @return the string
     */
    public String findAgentFeesByType(String agentId, int transactionType, String accountType,
            String currency);

    /**
     * Checks if is agent commission group.
     * @param commissionFeeGroup the commission fee group
     * @return true, if is agent commission group
     */
    public boolean isAgentCommissionGroup(CommissionFeeGroup commissionFeeGroup);

    /**
     * Find pos agent.
     * @param agentId the agent id
     * @return the agent
     */
    public Agent findPOSAgent(String agentId);

    /**
     * Find ocx agent.
     * @return the agent
     */
    public Agent findOCXAgent();

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
     * Find agents service point by agent id.
     * @param agentId the agent id
     * @return the service point
     */
    public ServicePoint findAgentsServicePointByAgentId(String agentId);

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
     * Checks if is city mappingexist.
     * @param id the id
     * @return true, if is city mappingexist
     */
    public boolean isCityMappingexist(long id);

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
     * Find agent login user name exist.
     * @param userName the user name
     * @return true, if successful
     */
    public boolean findAgentLoginUserNameExist(String userName);

    /**
     * Find agent login password exist.
     * @param password the password
     * @return true, if successful
     */
    public boolean findAgentLoginPasswordExist(String password);

    /**
     * Find agent valid login by user name and password.
     * @param userName the user name
     * @param password the password
     * @return the agent
     */
    public Agent findAgentValidLoginByUserNameAndPassword(String userName, String password);

    /**
     * Update agent shop dealer card id sequence.
     * @param agentId the agent id
     * @param cardId the card id
     */
    public void updateAgentShopDealerCardIdSequence(String agentId, String cardId);

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
     * Update farmer id sequence.
     */
    public void updateFarmerIdSequence();

    /**
     * Update shop dealer id sequence.
     */
    public void updateShopDealerIdSequence();

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
     * Checks if is agent mapped for service point.
     * @param code the code
     * @return true, if is agent mapped for service point
     */
    public boolean isAgentMappedForServicePoint(String code);

    /**
     * List of agent by area.
     * @param servicePointCode the service point code
     * @return the list< agent>
     */
    public List<Agent> listOfAgentByArea(String servicePointCode);

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
    public List<String> listSamithiByCooperativeId(long id);

    /**
     * List selected samithi by agent id.
     * @param agentId the agent id
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

    /*
     * public List<Agent> findFieldStaffByCooperativeId(Long id);
     */

    public List<String> listSelectedSamithiById(Long id);

    public List<String> listAvailableSamithi();

    public Object updateAgentFarmerCardIdSequence(String agentId, String cardId);

    public List<Byte> listAgentsByStatus();

    public List<Agent> findAgentList();

    public Vendor findVendor(Long id);

    public WarehousePayment findVendorId(long vendorId);

    public void merge(Agent agent);
    
    public Agent findAgentByProfileAndBranchId(String profileId,String branchId);
    
    public List<Warehouse> listAvailableGroupByAgentId(Long agentId);
    
    public List<Warehouse> listSelectedGroupByAgentId(Long agentId);
    
    public List<Warehouse> listSamithies();

	public List<Byte> listAgentsByStatusBasedOnBranch(String branchIdValue);

	public List<PeriodicInspection> listMoileUser();
	
	public Integer findMobileUserCount();
	
	public Integer findMobileUserCountByMonth(Date sDate,Date eDate);
	
	public Agent findAgentByProfileId(String profileId, String tenantId);
	
	public Agent findAgentByAgentId(String agentId, String tenantId,String branchId);
	
	public Agent findAgentByProfileId(String agentId, String tenantId, String branchId);
	
	public List<Object[]> listAgentNameProfIdAndIdByBranch(String branchId);
	
	public AgentAccessLog findAgentAccessLogByAgentId(String agentId, Date txnDate);
	
	public AgentAccessLogDetail findAgentAccessLogDetailByTxn(Long accessId,String txnType);

	public List<Object[]> listOfAgentByTraining();

	public List<Object[]> listOfLearningGroupByTraining();

	public List<Agent> listAgentProfileIdByAgentName(String agentName);

    public void addLocationHistory(LocationHistory locationHistory, String tenantId);

    public long findAgentCountByWarehouseId(long id);
    
    public AgroTransaction findAgrotxnDetailsByAgentId(String profileId);
    
    public List<Object[]>listAgrotxnDetailsByAgentId(List<String> profileId, List<String> txnType);
    
    public List<String> findAgentByWareHouseId(long coOperativeId);
    
    public List<Object[]> findTxnTypeByTxnTypeAndAgentId(String agentId);
    public long listPeriodicInspectionByAgentId(List<String> profileId);
    
    public List<String> listTxnTypeByAgentId(List<String> agent);


	public List<Object[]> listAgentIdNamebyAgroTxn();

	public AgentAccessLogDetail listAgnetAccessLogDetailsByIdTxnType(long id, String type);
	
	public AgentAccessLogDetail listAgnetAccessLogDetailsByIdTxnType(long id, String type,String msgNo);
	
	public long surveyMasterRevisionByDvId(String agentId);

	List<Object[]> listMobileUser();

	Agent findAgentByProfileIdWithSurvey(String id);

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

	public Agent findAgentByAgentIdd(String profileId);

	public List<Object[]> findFarmerCountBySamithiId();

	public List<Object[]> listAgrotxnDetailsByAgentIdAndTxnType(List<String> agentId, List<String> txnList);
	
	 public List<String> findAgentNameByWareHouseId(long coOperativeId);
	
}
