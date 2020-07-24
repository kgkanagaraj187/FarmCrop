/*
 * AgentDAO.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transaction;
import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ese.entity.credential.Fees;
import com.ese.entity.credential.MccCode;
import com.ese.entity.txn.fee.CommissionFeeGroup;
import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.AgentType;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.LocationHistory;
import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.order.entity.txn.AgentAccessLog;
import com.sourcetrace.eses.order.entity.txn.AgentAccessLogDetail;
import com.sourcetrace.eses.order.entity.txn.AgentMovementLocation;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.PeriodicInspection;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Branch;
import com.sourcetrace.esesw.entity.profile.ServicePoint;
import com.sourcetrace.esesw.entity.profile.Vendor;
import com.sourcetrace.esesw.entity.profile.WarehousePayment;
import com.sourcetrace.esesw.entity.txn.ESETxnHeader;

@Repository
@Transactional
public class AgentDAO extends ESEDAO implements IAgentDAO {
	@Autowired
	public AgentDAO(SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#listAgents()
	 */
	public List<Agent> listAgents() {

		return list("select profileId FROM Agent");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#delete(com.ese.entity.profile.Agent)
	 */
	public void delete(Agent agent) {

		super.delete(agent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#findAgent(long)
	 */
	public Agent findAgent(long id) {

		//Agent agent = (Agent) find("FROM Agent a left join fetch a.surveys WHERE a.id = ?", id);
		Agent agent = (Agent) find("FROM Agent a WHERE a.id = ?", id);
		
		return agent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#findAgentByAgentId(java.lang.String)
	 */
	public Agent findAgentByAgentId(String agentId) {

		Agent agent = (Agent) find("FROM Agent a WHERE  a.profileId = ?", agentId);

		return agent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#findAgentByAgentIdOrTempId(java.lang.String
	 * )
	 */
	public Agent findAgentByAgentIdOrTempId(String agentId) {

		Object[] bind = { agentId, agentId };
		Agent agent = (Agent) find("FROM Agent a WHERE a.profileId = ? or a.temporaryId=?", bind);

		return agent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#listNewAgents()
	 */
	public List<Agent> listNewAgents() {

		return list("FROM Agent a WHERE  a.status = 0 AND (a.type=1 or a.type=3)");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#listAgentsByType(int)
	 */
	public List<Agent> listAgentsByType(int type) {

		return list("FROM Agent a WHERE  a.type = ?", type);
	}

	/**
	 * Find agentby mcc code.
	 * 
	 * @param mccCode
	 *            the mcc code
	 * @return the list< agent>
	 */
	public List<Agent> findAgentbyMccCode(String mccCode) {

		return list("FROM Agent a WHERE  a.mccode = ?", mccCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#listEnrollmentAgents()
	 */
	public List<Agent> listEnrollmentAgents() {

		return list("FROM Agent a WHERE  (a.type=2 or a.type=3)");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#listPOSAgents()
	 */
	public List<Agent> listPOSAgents() {

		return list("FROM Agent a WHERE  (a.type=1 or a.type=3  )");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#findMaxRevisionNumber()
	 */
	/*
	 * public long findMaxRevisionNumber() { List<Long> revisionNumber =
	 * getHibernateTemplate().find("select max(a.revisionNumber)FROM Agent a ");
	 * if (!(revisionNumber.get(0) == null)) { return revisionNumber.get(0); }
	 * else { return 0; } }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#findEnrollAgentByAgentId(java.lang.String)
	 */
	public Agent findEnrollAgentByAgentId(String agentId) {

		Object[] bind = { agentId, agentId };
		Agent agent = (Agent) find("FROM Agent a WHERE a.profileId = ? or a.temporaryId=?", bind);

		return agent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#findAgentByTemporaryId(java.lang.String)
	 */
	public Agent findAgentByTemporaryId(String tempId) {

		Agent agent = (Agent) find("FROM Agent a WHERE  a.temporaryId = ?", tempId);

		return agent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#isAgentHaveTransaction(java.lang.String)
	 */
	public boolean isAgentHaveTransaction(String agentId) {

		Transaction txn = (Transaction) find("FROM Transaction c WHERE c.agentId = ?", agentId);

		return txn != null ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#findAgentByIdentityNumber(java.lang.String)
	 */
	public Agent findAgentByIdentityNumber(String identityNumber) {

		Agent agent = (Agent) find("FROM Agent a WHERE a.personalInfo.identityNumber = ? ", identityNumber);
		return agent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#listMccCode()
	 */
	public List<MccCode> listMccCode() {

		return list("FROM MccCode");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#listAgentType()
	 */
	public List<AgentType> listAgentType() {

		return list("FROM AgentType");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#isBranchHaveAgent(com.ese.entity.profile
	 * .Branch)
	 */
	public boolean isBranchHaveAgent(Branch branch) {

		Agent agent = (Agent) find("FROM Agent a WHERE a.branch=?", branch);
		return !ObjectUtil.isEmpty(agent);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#listEnrollmentAgentsHaveCredential()
	 */
	public List<Agent> listEnrollmentAgentsHaveCredential() {

		List<Agent> agents = list("FROM Agent a WHERE  a.type=2 and a.password <>'')");
		return agents;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#listPOSAgentsHaveCredential()
	 */
	public List<Agent> listPOSAgentsHaveCredential() {

		@SuppressWarnings("unchecked")
		List<Agent> agents = list(
				"FROM Agent a WHERE  a.type=1 and (a.credentialGroup is not null or a.mcc is not null))");
		return agents;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#findAgentFeesByType(java.lang.String,
	 * int, java.lang.String, java.lang.String)
	 */
	public String findAgentFeesByType(String agentId, int transactionType, String accountType, String currency) {

		Object[] bind = { agentId, transactionType, accountType, currency };
		Fees fees = (Fees) find(
				"FROM Fees a WHERE a.profile.profileId=? and a.transactionType.id=? and a.accountType=? and a.currency.currencyCode=?",
				bind);
		if (!ObjectUtil.isEmpty(fees)) {
			return Double.toString(fees.getAmount());

		} else {
			return "";
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#isAgentCommissionGroup(com.ese.entity.txn
	 * .fee.CommissionFeeGroup )
	 */
	public boolean isAgentCommissionGroup(CommissionFeeGroup commissionFeeGroup) {

		Object[] bind = { commissionFeeGroup, commissionFeeGroup };
		Agent agent = (Agent) find("FROM Agent a WHERE  a.commissionFeeGroup=? or a.mcc.commissionFeeGroup=?", bind);
		return !ObjectUtil.isEmpty(agent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#findPOSAgent(java.lang.String)
	 */
	public Agent findPOSAgent(String agentId) {

		Agent agent = (Agent) find("FROM Agent a WHERE (a.type=1 or a.type=3  ) and a.profileId = ?", agentId);
		return agent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#findOCXAgent()
	 */
	public Agent findOCXAgent() {

		return (Agent) find("FROM Agent a WHERE  a.profileId=?", "AG9999999999");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#listAgentsByIdentiyNumber(java.lang.String)
	 */
	public List<Agent> listAgentsByIdentiyNumber(String identityNumber) {

		@SuppressWarnings("unchecked")
		List<Agent> agents = list("FROM Agent a WHERE a.personalInfo.identityNumber=?)", identityNumber);
		return agents;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#listAgentByCommerceId(java.lang.String[])
	 */
	public List<Agent> listAgentByCommerceId(String[] shopMap) {

		Map<String, Object[]> bindValues = new HashMap<String, Object[]>();
		bindValues.put("id", shopMap);
		return null;

		/*
		 * List<Agent> agents = (List<Agent>) getHibernateTemplate()
		 * .execute(new ExecuteFind( "from Agent p where p.commerceId in (:id)",
		 * bindValues)); return agents;
		 */

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#findAgent(java.lang.String)
	 */
	public Agent findAgent(String profileId) {

		Agent agent = (Agent) find("from Agent agent where agent.id = ?", profileId);
		return agent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#listAgentNotMappedwithDevice()
	 */
	public List<Agent> listAgentNotMappedwithDevice() {

		// List<Agent> agent =
		// list("FROM Agent a WHERE a.servicePoint.id IN (SELECT sp.id FROM
		// ServicePoint sp) and a.id NOT IN(SELECT d.agent.id FROM Device d
		// WHERE d.agent.id IS NOT NULL)");
		List<Agent> agent = list(
				"FROM Agent a WHERE a.id NOT IN(SELECT d.agent.id FROM Device d WHERE d.agent.id IS NOT NULL) AND a.agentType.id='2'");
		return agent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#findAgentIdFromEseTxn(java.lang.String)
	 */
	public boolean findAgentIdFromEseTxn(String profileId) {

		List<ESETxnHeader> txnHeaderList = list("From ESETxnHeader eth where eth.agentId=?", profileId);
		boolean isTxnHeaderExist = false;
		if (txnHeaderList.size() > 0) {
			isTxnHeaderExist = true;
		}
		return isTxnHeaderExist;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#findAgentIdFromDevice(long)
	 */
	public boolean findAgentIdFromDevice(long id) {

		List<Device> device = list("From Device dev where dev.agent.id=?", id);
		boolean isDeviceExist = false;
		if (device.size() > 0) {
			isDeviceExist = true;
		}
		return isDeviceExist;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#findAgentByProfileId(java.lang.String)
	 */
	public Agent findAgentByProfileId(String profileId) {

		Agent agent = (Agent) find("from Agent agent where agent.profileId = ?", profileId);
		return agent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#removeAgentServiceLocationMapping(long)
	 */
	public void removeAgentServiceLocationMapping(long id) {

		Session sessions = getSessionFactory().openSession();
		String queryStrings = "DELETE FROM AGENT_SERV_LOC_MAP WHERE AGENT_ID = " + id;
		Query querys = sessions.createSQLQuery(queryStrings);

		int results = querys.executeUpdate();
		sessions.flush();
		sessions.close();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#updateAgentBODStatus(long, int)
	 */
	public void updateAgentBODStatus(String profileId, int status) {

		String queryString = "UPDATE agent_prof Inner Join prof ON agent_prof.PROF_ID = prof.ID SET agent_prof.BOD_STATUS = '"
				+ status + "' WHERE prof.PROF_ID =  '" + profileId + "'";
		Session sessions = getSessionFactory().openSession();
		Query querys = sessions.createSQLQuery(queryString);
		int results = querys.executeUpdate();
		sessions.flush();
		sessions.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#findAgentExistForServicePoint(long)
	 */
	@SuppressWarnings("unchecked")
	public boolean findAgentExistForServicePoint(long servicePointId) {

		List<Profile> servicePointList = list("From Profile p where p.profileType=01 AND p.servicePoint.id = ?",
				servicePointId);
		boolean isAgentMapExist = false;
		if (servicePointList.size() > 0) {
			isAgentMapExist = true;
		}
		return isAgentMapExist;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#findAgentsServicePointByAgentId(java.lang
	 * .String)
	 */
	public ServicePoint findAgentsServicePointByAgentId(String agentId) {

		ServicePoint servicePoint = (ServicePoint) find("SELECT a.servicePoint FROM Agent a where a.profileId = ?",
				agentId);
		return servicePoint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#listAgent()
	 */
	public List<Agent> listAgent() {

		return list("FROM Agent a ORDER BY a.personalInfo.firstName ASC");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#findAgentMappedWithServiceLocation(long)
	 */
	public boolean findAgentMappedWithServiceLocation(long serviceLocationId) {

		boolean isAgentMappedWithServiceLocation = false;
		Session sessions = getSessionFactory().openSession();
		String queryStrings = "SELECT AGENT_ID FROM AGENT_SERV_LOC_MAP WHERE SERV_LOC_ID = " + serviceLocationId;
		Query querys = sessions.createSQLQuery(queryStrings);

		List results = querys.list();
		if (results.size() > 0) {
			isAgentMappedWithServiceLocation = true;
		}
		sessions.flush();
		sessions.close();
		return isAgentMappedWithServiceLocation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#isCityMappingexist(long)
	 */
	public boolean isCityMappingexist(long id) {

		List<Agent> agentList = list("FROM Agent a WHERE a.contactInfo.city.id = ?", id);
		if (!ObjectUtil.isListEmpty(agentList)) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#listWarehouseNameByAgentIdAndServicePointId
	 * (long, long)
	 */
	public List<String> listWarehouseNameByAgentIdAndServicePointId(long agentId, long servicePointId) {

		Session session = getSessionFactory().openSession();
		String queryString = "SELECT" + " w.name" + " FROM" + " warehouse w"
				+ " LEFT JOIN agent_warehouse_map awm ON awm.warehouse_id = w.id"
				+ " left join serv_point sp on sp.city_id=w.city_id" + " WHERE awm.agent_id = " + agentId
				+ " and sp.id=" + servicePointId;
		Query query = session.createSQLQuery(queryString);
		List list = query.list();
		session.flush();
		session.close();
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#listWarehouseNameByServicePointId(long)
	 */
	public List<String> listWarehouseNameByServicePointId(long id) {

		Session session = getSessionFactory().openSession();
		String queryString = "SELECT" + " w.name " + " FROM" + " warehouse w" + " LEFT JOIN city c ON c.id = w.CITY_ID"
				+ " LEFT JOIN serv_point sp ON sp.CITY_ID = c.id"
				+ " WHERE w.id NOT IN (SELECT awm.warehouse_id FROM agent_warehouse_map awm)" + " AND sp.id=" + id;
		Query query = session.createSQLQuery(queryString);
		List list = query.list();
		session.flush();
		session.close();
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#removeAgentWarehouseMappingByAgentId(long)
	 */
	public void removeAgentWarehouseMappingByAgentId(long id) {

		Session sessions = getSessionFactory().openSession();
		String queryStrings = "DELETE FROM AGENT_WAREHOUSE_MAP WHERE AGENT_ID = " + id;
		Query querys = sessions.createSQLQuery(queryStrings);

		int results = querys.executeUpdate();
		sessions.flush();
		sessions.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#findAgentMappedWithWarehouse(long)
	 */
	public boolean findAgentMappedWithWarehouse(long id) {

		boolean isAgentMappedWithWarehouse = false;
		Session sessions = getSessionFactory().openSession();
		String queryStrings = "SELECT AGENT_ID FROM AGENT_WAREHOUSE_MAP WHERE WAREHOUSE_ID = " + id;
		Query querys = sessions.createSQLQuery(queryStrings);
		List results = querys.list();
		if (results.size() > 0) {
			isAgentMappedWithWarehouse = true;
		}
		sessions.flush();
		sessions.close();
		return isAgentMappedWithWarehouse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#findAgentbyServicePointId(long)
	 */
	public List<Agent> findAgentbyServicePointId(long id) {

		return (List<Agent>) list("From Agent a WHERE a.servicePoint.id = ?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#listAgentByServicePoint(java.lang.String,
	 * java.lang.String)
	 */
	public List<Agent> listAgentByServicePoint(String exceptThisAgent, String servicePointName) {

		return (List<Agent>) list("FROM Agent a WHERE a.servicePoint.name = ? AND a.profileId <> ? ",
				new Object[] { servicePointName, exceptThisAgent });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#updateAgentReceiptNoSequence(java.lang.
	 * String, java.lang.String)
	 */
	public void updateAgentReceiptNoSequence(String agentId, String offlineReceiptNo) {

		String queryString = "UPDATE agent_prof INNER JOIN prof ON prof.ID = agent_prof.PROF_ID SET agent_prof.RECEIPT_NUMBER = ' "
				+ offlineReceiptNo + "' WHERE prof.PROF_ID = '" + agentId + "'";
		Session sessions = getSessionFactory().openSession();
		Query querys = sessions.createSQLQuery(queryString);
		int results = querys.executeUpdate();
		sessions.flush();
		sessions.close();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#updateAgentOrderNoSeq(java.lang.String,
	 * java.lang.String)
	 */
	public void updateAgentOrderNoSeq(String agentId, String inventoryOrderNo) {

		String queryString = "UPDATE agent_prof INNER JOIN prof ON prof.ID = agent_prof.PROF_ID SET agent_prof.ORDER_NO_SEQ = ' "
				+ inventoryOrderNo + "' WHERE prof.PROF_ID = '" + agentId + "'";
		Session sessions = getSessionFactory().openSession();
		Query querys = sessions.createSQLQuery(queryString);
		int results = querys.executeUpdate();
		sessions.flush();
		sessions.close();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#updateAgentDeliveryNoSeq(java.lang.String,
	 * java.lang.String)
	 */
	public void updateAgentDeliveryNoSeq(String agentId, String inventoryDeliveryNo) {

		String queryString = "UPDATE agent_prof INNER JOIN prof ON prof.ID = agent_prof.PROF_ID SET agent_prof.DELIVERY_NO_SEQ = ' "
				+ inventoryDeliveryNo + "' WHERE prof.PROF_ID = '" + agentId + "'";
		Session sessions = getSessionFactory().openSession();
		Query querys = sessions.createSQLQuery(queryString);
		int results = querys.executeUpdate();
		sessions.flush();
		sessions.close();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#findAgentLoginPasswordExist(java.lang.
	 * String )
	 */
	public boolean findAgentLoginPasswordExist(String password) {

		Agent agent = (Agent) find("FROM Agent agent WHERE agent.password=?", password);
		if (ObjectUtil.isEmpty(agent))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#findAgentLoginUserNameExist(java.lang.
	 * String )
	 */
	public boolean findAgentLoginUserNameExist(String userName) {

		Agent agent = (Agent) find("FROM Agent agent WHERE agent.profileId=?", userName);
		if (ObjectUtil.isEmpty(agent))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#findAgentValidLoginByUserNameAndPassword
	 * (java.lang.String, java.lang.String)
	 */
	public Agent findAgentValidLoginByUserNameAndPassword(String userName, String password) {

		Object[] credential = { userName, password };
		Agent agent = (Agent) find("FROM Agent agent WHERE agent.profileId=? AND agent.password=?", credential);
		return agent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#updateAgentShopDealerCardIdSequence
	 * (java.lang.String, java.lang.String)
	 */
	public void updateAgentShopDealerCardIdSequence(String agentId, String cardId) {

		String queryString = "UPDATE agent_prof INNER JOIN prof ON prof.ID = agent_prof.PROF_ID SET agent_prof.SHOP_DEALER_CARD_ID_SEQ = ' "
				+ cardId + "' WHERE prof.PROF_ID = '" + agentId + "'";
		Session sessions = getSessionFactory().openSession();
		Query querys = sessions.createSQLQuery(queryString);
		@SuppressWarnings("unused")
		int results = querys.executeUpdate();
		sessions.flush();
		sessions.close();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#listAgentMovementImages(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<AgentMovementLocation> listAgentMovementImages(Long agentMovementId) {

		List<AgentMovementLocation> imagesList = list("FROM AgentMovementLocation aml WHERE aml.agentMovement.id = ? ",
				agentMovementId);
		return imagesList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#loadAgentMoventImages(java.lang.String)
	 */
	public AgentMovementLocation loadAgentMoventImages(String id) {

		return (AgentMovementLocation) find("FROM AgentMovementLocation m where m.id=?", Long.parseLong(id));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#findAgentNameByAgentId(java.lang.String)
	 */
	public String findAgentNameByAgentId(String agentId) {

		return (String) find(
				"SELECT concat(p.firstName,' ',p.lastName) from Agent a LEFT JOIN a.personalInfo p where a.profileId =?",
				agentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#listAgentIdName()
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> listAgentIdName() {

		return list(
				"Select a.profileId,CONCAT(a.personalInfo.firstName,' ',a.personalInfo.lastName)  FROM Agent a order by a.personalInfo.firstName");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#updateFarmerIdSequence()
	 */
	public void updateFarmerIdSequence() {

		String qs1 = "select FARMER_ID from farmer";

		Session sessions = getSessionFactory().openSession();

		Query query = sessions.createSQLQuery(qs1);
		List list = query.list();
		for (int i = 0; i < list.size(); i++) {

			String oldID = list.get(i).toString();
			String newID = StringUtil.appendZeroPrefix(String.valueOf(i + 1), 6);

			String qs2 = "UPDATE trxn_agro SET farmer_id = '" + newID + "' where farmer_id = '" + oldID + "'";
			Query querys = sessions.createSQLQuery(qs2);
			int results = querys.executeUpdate();

			String qs3 = "UPDATE ese_account SET profile_id = '" + newID + "' where profile_id = '" + oldID + "'";
			querys = sessions.createSQLQuery(qs3);
			results = querys.executeUpdate();

			String qs4 = "UPDATE farmer SET farmer_id = '" + newID + "' where farmer_id = '" + oldID + "'";
			querys = sessions.createSQLQuery(qs4);
			results = querys.executeUpdate();

			String qs5 = "UPDATE farmer SET farmer_id = '" + newID + "' where farmer_id = '" + oldID + "'";
			querys = sessions.createSQLQuery(qs5);
			results = querys.executeUpdate();
		}

		sessions.flush();
		sessions.close();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#updateShopDealerIdSequence()
	 */
	public void updateShopDealerIdSequence() {

		String qs1 = "select SHOP_DEALER_ID from shop_dealer";

		Session sessions = getSessionFactory().openSession();

		Query query = sessions.createSQLQuery(qs1);
		List list = query.list();
		for (int i = 0; i < list.size(); i++) {

			String oldID = list.get(i).toString();
			String newID = StringUtil.appendZeroPrefix(String.valueOf(i + 1), 6);

			String qs2 = "update inventory set SHOP_DEALER_ID = 'S" + newID + "' where SHOP_DEALER_ID = '" + oldID
					+ "'";
			Query querys = sessions.createSQLQuery(qs2);
			int results = querys.executeUpdate();

			String qs3 = "update shop_dealer set SHOP_DEALER_ID = 'S" + newID + "' where SHOP_DEALER_ID = '" + oldID
					+ "'";
			querys = sessions.createSQLQuery(qs3);
			results = querys.executeUpdate();
		}

		sessions.flush();
		sessions.close();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#findAgentTypeById(long)
	 */
	public AgentType findAgentTypeById(long id) {

		return (AgentType) find("From AgentType WHERE id=?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#listAgentByAgentType(java.lang.String)
	 */
	public List<Agent> listAgentByAgentType(String code) {

		return (List<Agent>) list("FROM Agent a WHERE a.agentType.code=? AND a.status=1", code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#isAgentMappedForServicePoint(java.lang.
	 * String)
	 */
	public boolean isAgentMappedForServicePoint(String code) {

		List<Profile> servicePointList = list("From Profile p where p.profileType=01 AND p.servicePoint.code = ?",
				code);
		boolean isAgentMapExist = false;
		if (servicePointList.size() > 0) {
			isAgentMapExist = true;
		}
		return isAgentMapExist;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#listOfAgentByArea(long)
	 */
	public List<Agent> listOfAgentByArea(String servicePointCode) {

		Object[] bindValues = { servicePointCode, Profile.ACTIVE };
		List<Agent> agent = list("FROM Agent a WHERE a.servicePoint.code=? AND a.status=?", bindValues);
		return agent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IAgentDAO#listSamithiByCooperativeId(long)
	 */
	public List<String> listSamithiByCooperativeId(long id) {

		Session session = getSessionFactory().openSession();
		String queryString = "SELECT w.name FROM warehouse w where w.REF_WAREHOUSE_ID=" + id;
		Query query = session.createSQLQuery(queryString);
		List list = query.list();
		session.flush();
		session.close();
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#listSelectedSamithiByAgentId(java.lang.
	 * Long)
	 */
	public List<String> listSelectedSamithiByAgentId(Long agentId, Long cooperativeId) {

		Session session = getSessionFactory().openSession();
		String queryString = "SELECT w.name from warehouse w LEFT JOIN agent_warehouse_map awm ON awm.WAREHOUSE_ID = w.id WHERE awm.AGENT_ID="
				+ agentId + " AND w.REF_WAREHOUSE_ID=" + cooperativeId;
		Query query = session.createSQLQuery(queryString);
		List list = query.list();
		session.flush();
		session.close();
		return list;
	}

	public List<String> listSelectedSamithiById(Long agentId) {

		Session session = getSessionFactory().openSession();
		String queryString = "SELECT w.name from warehouse w LEFT JOIN agent_warehouse_map awm ON awm.WAREHOUSE_ID = w.id WHERE w.typez=1 AND awm.AGENT_ID="
				+ agentId;
		Query query = session.createSQLQuery(queryString);
		List list = query.list();
		session.flush();
		session.close();
		return list;
	}

	public List<String> listAvailableSamithi() {

		Session session = getSessionFactory().openSession();
		String queryString = "SELECT NAME FROM Warehouse samithi  WHERE samithi.typez=1  ORDER BY samithi.name ASC";
		Query query = session.createSQLQuery(queryString);
		List list = query.list();
		session.flush();
		session.close();
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#listAvailableSamithiByAgentId(java.lang.
	 * Long)
	 */
	public List<String> listAvailableSamithiByAgentId(Long agentId, Long cooperativeId) {

		Session session = getSessionFactory().openSession();
		String queryString = "SELECT w.name from warehouse w WHERE w.REF_WAREHOUSE_ID is not NULL AND w.ID NOT IN (SELECT awm.WAREHOUSE_ID FROM agent_warehouse_map awm WHERE awm.AGENT_ID="
				+ agentId + ") AND w.REF_WAREHOUSE_ID=" + cooperativeId + " ";
		Query query = session.createSQLQuery(queryString);
		List list = query.list();
		session.flush();
		session.close();
		return list;
	}
	
	
	public List<Warehouse> listAvailableGroupByAgentId(Long agentId){
		Session session = getSessionFactory().openSession();
		List<Warehouse> warehouseList = new ArrayList<>();
		
		String queryString="FROM Warehouse w WHERE w.refCooperative is NULL AND w.id NOT IN(SELECT wh.id FROM Agent a INNER JOIN a.wareHouses wh where a.id=:agentID)";
		Query query = session.createQuery(queryString);
		query.setParameter("agentID", agentId);
		warehouseList = query.list();
		
		session.flush();
		session.close();
		return warehouseList;
	}
	
	public List<Warehouse> listSelectedGroupByAgentId(Long agentId){
		Session session = getSessionFactory().openSession();
		List<Warehouse> warehouseList = new ArrayList<>();
		
		String queryString="FROM Warehouse w WHERE w.refCooperative is NULL AND w.id IN(SELECT wh.id FROM Agent a INNER JOIN a.wareHouses wh where a.id=:agentID)";
		Query query = session.createQuery(queryString);
		query.setParameter("agentID", agentId);
		warehouseList = query.list();
		
		session.flush();
		session.close();
		return warehouseList;
	}
	
	public List<Warehouse> listSamithies(){
		return (List<Warehouse>)list("FROM Warehouse WHERE refCooperative IS NULL");
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IAgentDAO#listFieldStaffsByCoopetiveManagerProfileId(
	 * java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Agent> listFieldStaffsByCoopetiveManagerProfileId(String agentId) {

		Object[] bindValues = { AgentType.FIELD_STAFF, agentId, AgentType.COOPERATIVE_MANAGER, Agent.ACTIVE };
		List<Agent> agentList = list(
				"Select DISTINCT a From Agent a INNER JOIN a.wareHouses s WHERE a.agentType.code=? AND s.refCooperative.id=(SELECT  co.id FROM Agent com INNER JOIN com.wareHouses co WHERE com.profileId=? AND com.agentType.code=?) AND a.status=?",
				bindValues);
		return agentList;
	}

	public Agent findAgentByCoOperative(long coOperativeId) {

		// TODO Auto-generated method stub
		Session session = getSessionFactory().openSession();
		Criteria crit = session.createCriteria(Agent.class, "agent");
		crit.createAlias("wareHouses", "w");
		crit.add(Restrictions.eq("w.id", coOperativeId));
		List<Agent> agents = crit.list();
		session.flush();
		session.close();
		return agents.size() > 0 ? agents.get(0) : null;
	}

	public PeriodicInspection findPeriodicInspectionById(Long id) {

		PeriodicInspection periodicInspection = (PeriodicInspection) find(
				"FROM PeriodicInspection p LEFT JOIN FETCH p.inspectionImageInfo WHERE p.id=?", id);

		return periodicInspection;
	}

	public byte[] findPeriodicInspectionFarmerVoiceById(Long id) {

		return (byte[]) find("SELECT pr.farmerVoice FROM PeriodicInspection pr WHERE pr.id=?", id);
	}

	public List<ESETxnHeader> listFieldStaff() {

		List<ESETxnHeader> list = (List) list("Select pi.eSETxn.header  From PeriodicInspection pi");
		return list;

	}

	public List<Object[]> listFieldStaffByTxnType(String txnType) {

		return list("SELECT th.agentId, th.agentName FROM ESETxnHeader th WHERE th.eseTxnType.code=?", txnType);
	}

	/*
	 * public List<Agent> findFieldStaffByCooperativeId(Long id) { Object[]
	 * bindValues = { AgentType.FIELD_STAFF, id, Agent.ACTIVE }; List<Agent>
	 * agentList = list(
	 * "Select DISTINCT a From Agent a INNER JOIN a.wareHouses s WHERE a.agentType.code=? AND s.refCooperative.id=? AND a.status=?"
	 * , bindValues); return agentList; }
	 */

	public List<Agent> listOfWareHouseProuctFieldStaff(String code) {

		return (List<Agent>) list(
				"select wp.agent from WarehouseProduct wp where wp.agent.agentType.code=? AND wp.agent.status=1", code);
	}

	public Object updateAgentFarmerCardIdSequence(String agentId, String cardId) {

		String queryString = "UPDATE agent_prof INNER JOIN prof ON prof.ID = agent_prof.PROF_ID SET agent_prof.FARMER_CARD_ID_SEQ = ' "
				+ cardId + "' WHERE prof.PROF_ID = '" + agentId + "'";
		Session sessions = getSessionFactory().openSession();
		Query querys = sessions.createSQLQuery(queryString);
		@SuppressWarnings("unused")
		int results = querys.executeUpdate();
		sessions.flush();
		sessions.close();
		return results;

	}

	public List<Byte> listAgentsByStatus() {

		Session session = getSessionFactory().openSession();
		String queryString = "SELECT AP.BOD_STATUS FROM PROF P INNER JOIN AGENT_PROF AP ON P.ID= AP.PROF_ID WHERE AP.AGENT_TYPE_ID="
				+ AgentType.AgentTypes.FIELD_STAFF.ordinal();
		Query query = session.createSQLQuery(queryString);
		List list = query.list();
		session.flush();
		session.close();
		return list;
	}

	public List<Agent> findAgentList() {

		return list("FROM Agent");
	}

	public Vendor findVendor(Long id) {

		// TODO Auto-generated method stub
		Vendor vendor = (Vendor) find("FROM Vendor v WHERE v.id = ?", id);
		return vendor;

	}

	public WarehousePayment findVendorId(long vendorId) {
		// TODO Auto-generated method stub

		return (WarehousePayment) find("FROM WarehousePayment wp WHERE wp.vendor.id = ?", vendorId);
	}

	@Override
	public void merge(Agent agent) {
		// TODO Auto-generated method stub

	}

	@Override
	public long findMaxRevisionNumber() {

		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Agent findAgentByProfileAndBranchId(String agentID, String branchID) {
		Session session = getSessionFactory().openSession();
		org.hibernate.Filter branchFilter = session.getEnabledFilter(ISecurityFilter.BRANCH_FILTER);
		if (!ObjectUtil.isEmpty(branchFilter)) {
			session.disableFilter(ISecurityFilter.BRANCH_FILTER);
		}
		Query query = session
				.createQuery("From Agent agent where agent.profileId=:agentId and agent.branchId=:branchId");
		query.setParameter("agentId", agentID).setParameter("branchId", branchID);
		Agent agent = (Agent) query.list().get(0);
		session.flush();
		session.close();
		return agent;
	}

	@Override
	public List<Byte> listAgentsByStatusBasedOnBranch(String branchIdValue) {
		Session session = getSessionFactory().openSession();
	String queryString = "SELECT AP.BOD_STATUS FROM PROF P INNER JOIN AGENT_PROF AP ON P.ID= AP.PROF_ID WHERE P.BRANCH_ID= :BRANCH AND AP.AGENT_TYPE_ID="
			+ AgentType.AgentTypes.FIELD_STAFF.ordinal();
	Query query = session.createSQLQuery(queryString);
	query.setParameter("BRANCH", branchIdValue);
	List list = query.list();
	session.flush();
	session.close();
	return list;
	}

	@Override
	public List<PeriodicInspection> listMoileUser() {
		// TODO Auto-generated method stub
		return list(" FROM PeriodicInspection pi GROUP BY pi.createdUserName ");
	}
	
	public Integer findMobileUserCount(){
		Session session = getSessionFactory().getCurrentSession();
		return ((Long)session.createQuery("select count(*) from Agent").uniqueResult()).intValue();
	}

	@Override
	public Integer findMobileUserCountByMonth(Date sDate, Date eDate) {
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("select count(*) from Agent where createdDate BETWEEN :startDate AND :endDate");
		query.setParameter("startDate", sDate).setParameter("endDate", eDate);
		
		Integer val = ((Long)query.uniqueResult()).intValue();
		session.flush();
		session.close();
		return val;
	}
	
	public Agent findAgentByProfileId(String profileId, String tenantId) {

		/*Agent agent = (Agent) find("from Agent agent where agent.profileId = ?", profileId);
		return agent;*/
		
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery("from Agent agent where agent.profileId = :profileId");
		query.setParameter("profileId", profileId);
		
		List<Agent> agentList = query.list();
		Agent agent = null;
		
		if (agentList.size() > 0) {
			agent = (Agent) agentList.get(0);
		}

		session.flush();
		session.close();
		return agent;
	}
	
	public Agent findAgentByAgentId(String agentId, String tenantId,String branchId) {

		/*Agent agent = (Agent) find("FROM Agent a WHERE  a.profileId = ?", agentId);
		return agent;*/
		
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery("select distinct a FROM Agent a  inner join fetch a.personalInfo pi inner join fetch a.contactInfo pc left join fetch a.procurementCenter pc inner join fetch a.agentType at WHERE  a.profileId = :agentId and a.branchId=:branchId");
		query.setParameter("agentId", agentId);
		query.setParameter("branchId",branchId);
		
		List<Agent> agentList = query.list();
		Agent agent = null;
		
		if (agentList.size() > 0) {
			agent = (Agent) agentList.get(0);
		}

		session.flush();
		session.close();
		return agent;
	}
	
	public Agent findAgentByProfileId(String agentId, String tenantId,String branchId) {
        
        Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();        
        Query query = session.createQuery("FROM Agent a inner join fetch a.personalInfo pi inner join fetch a.contactInfo pc WHERE  a.profileId = :agentId and a.branchId=:branchId");
        query.setParameter("agentId", agentId);
        query.setParameter("branchId",branchId);
        
        List<Agent> agentList = query.list();
        Agent agent = null;
        
        if (agentList.size() > 0) {
            agent = (Agent) agentList.get(0);
        }

        session.flush();
        session.close();
        return agent;
    }

	@Override
	public List<Object[]> listAgentNameProfIdAndIdByBranch(String branchId) {
		return list("Select a.profileId,a.id,a.personalInfo.firstName FROM Agent a GROUP BY a.profileId ORDER BY a.personalInfo.firstName");
	}

    @Override
    public AgentAccessLog findAgentAccessLogByAgentId(String agentId,Date txnDate) {
        Object[] bind = { agentId,txnDate };
        AgentAccessLog agentAccessLog = (AgentAccessLog) find("FROM AgentAccessLog a WHERE a.profileId = ? and a.login = ?", bind);
        return agentAccessLog;
    }

    @Override
    public AgentAccessLogDetail findAgentAccessLogDetailByTxn(Long accessId, String txnType) {
        Object[] bind = { accessId,txnType };
        AgentAccessLogDetail agentAccessLogDetail = (AgentAccessLogDetail) find("FROM AgentAccessLogDetail a WHERE a.agentAccessLog.id = ? AND a.txnType=?", bind);
        return agentAccessLogDetail;
    }

	@Override
	public List<Object[]> listOfAgentByTraining() {
		// TODO Auto-generated method stub
		List<Object[]> result = list("SELECT DISTINCT ts.transferInfo.agentName,ts.transferInfo.agentId FROM TrainingStatus ts");
		return result;
	}

	@Override
	public List<Object[]> listOfLearningGroupByTraining() {
		// TODO Auto-generated method stub
		List<Object[]> result = list("SELECT DISTINCT ts.learningGroup.code,ts.learningGroup.name FROM TrainingStatus ts");
		return result;
	}

	@Override
	public List<Agent> listAgentProfileIdByAgentName(String agentName) {
		List<Agent> result=list("SELECT a.profileId FROM Agent a WHERE a.name=?",agentName);
		return result;
	}

    @Override
    public void addLocationHistory(LocationHistory locationHistory, String tenantId) {
    	if(StringUtil.isEmpty(tenantId)){
    		tenantId = ESESystem.AGRO_TENANT;
    	}
        Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
        sessions.saveOrUpdate(locationHistory);
        sessions.flush();
        sessions.close();
        
    }
    
    @Override
    public long findAgentCountByWarehouseId(long id) {
        long sequence = 0;
        Session session = getSessionFactory().openSession();
        String queryString = "SELECT count(DISTINCT awm.AGENT_ID) from agent_warehouse_map awm where awm.WAREHOUSE_ID ="+ id;
        Query query = session.createSQLQuery(queryString);
        sequence = ((BigInteger) query.list().get(0)).longValue();
      
        session.flush();
        session.close();        
        return sequence;
    }

    @Override
    public AgroTransaction findAgrotxnDetailsByAgentId(String profileId) {

        AgroTransaction agrotxn = (AgroTransaction) find("FROM AgroTransaction at WHERE  at.agentId = ?", profileId);

        return agrotxn;
    }
    
    @SuppressWarnings("unchecked")
    public List<Object[]> listAgrotxnDetailsByAgentId(List<String> profileId, List<String>  txnType) {
     
        Session sessions = getSessionFactory().openSession();
      //  String queryString ="SELECT at.txnType,COUNT(*) FROM  AgroTransaction at WHERE at.agentId IN(:profileId) AND at.txnType =:txnType AND at.profType='01' GROUP BY at.txnType UNION (SELECT pp.txnType,COUNT(*) FROM PeriodicInspection pp WHERE pp.createdUserName IN(:profileId))";
      /*  String queryString ="SELECT ta.txn_type,COUNT(*) FROM trxn_agro ta "
                        + "WHERE ta.AGENT_ID IN(:profileId) AND ta.txn_type IN(:txnType) AND ta.PROF_TYPE='01' "  
                        + "GROUP BY ta.txn_type "
                        + "UNION "
                        + "SELECT pp.TXN_TYPE,COUNT(*) FROM periodic_inspection pp "
                        + "WHERE pp.CREATE_USER_NAME IN (:profileId) "
                        + "GROUP BY pp.txn_type ";*/
        String queryString = "SELECT pp.TXN_TYPE,COUNT(*) FROM periodic_inspection pp "
                + "WHERE pp.CREATE_USER_NAME IN (:profileId) AND pp.txn_type IN(:txnType)" 
               // + "GROUP BY pp.txn_type "
                + "UNION "
                + "SELECT ta.txn_type,COUNT(DISTINCT ta.RECEIPT_NO) FROM trxn_agro ta "
                + "WHERE ta.AGENT_ID IN(:profileId) AND ta.txn_type IN(:txnType) " ; 
                //+ "GROUP BY ta.txn_type";
             
        Query query = sessions.createSQLQuery(queryString);
        query.setParameterList("profileId", profileId);      
        query.setParameterList("txnType", txnType);  
        List<Object[]> array = query.list();
        sessions.flush();
        sessions.close();
        return array;
    }
    
    public List<String> findAgentByWareHouseId(long coOperativeId) {

        Session session = getSessionFactory().openSession();    
        String queryString = "SELECT p.PROF_ID from agent_warehouse_map awm LEFT JOIN  prof p on awm.agent_id = p.id WHERE awm.warehouse_id ='"+coOperativeId+"'";
        Query query = session.createSQLQuery(queryString);
        List list = query.list();
        session.flush();
        session.close();
        return list;
    }
    
    @Override
    public List<Object[]> findTxnTypeByTxnTypeAndAgentId(String agentId){          
        Object[] obj=null;
        Session session = getSessionFactory().openSession();
        String queryString = "SELECT DISTINCT ta.TXN_TYPE,COUNT(*) from trxn_agro ta where ta.AGENT_ID ='"+agentId+"' GROUP BY ta.TXN_TYPE";
        Query query = session.createSQLQuery(queryString);
        List list = query.list();
        session.flush();
        session.close();
        return list;
       
    }
    
    @Override
    public long listPeriodicInspectionByAgentId(List<String> profileId) {      
        
        long sequence = 0;
        Session session = getSessionFactory().openSession();
        String queryString ="SELECT COUNT(*) FROM  periodic_inspection pp WHERE pp.CREATE_USER_NAME IN(:profileId)";
        Query query = session.createSQLQuery(queryString);
        query.setParameterList("profileId", profileId);    
        int count = Integer.valueOf(query.list().get(0).toString());

        if (count > 0) {
            sequence = count;
        }
        session.flush();
        session.close();
        return sequence;
    }
    
    @SuppressWarnings("unchecked")
    public List<String> listTxnTypeByAgentId(List<String> profileId) {
     
        Session sessions = getSessionFactory().openSession();
      //  String queryString ="SELECT at.txnType,COUNT(*) FROM  AgroTransaction at WHERE at.agentId IN(:profileId) AND at.txnType =:txnType AND at.profType='01' GROUP BY at.txnType UNION (SELECT pp.txnType,COUNT(*) FROM PeriodicInspection pp WHERE pp.createdUserName IN(:profileId))";
        String queryString ="SELECT ta.txn_type FROM trxn_agro ta "
                        + "WHERE ta.AGENT_ID IN(:profileId) AND ta.PROF_TYPE='01' "                       
                        + "UNION "
                        + "SELECT pp.TXN_TYPE FROM periodic_inspection pp "
                        + "WHERE pp.CREATE_USER_NAME IN (:profileId) ";
        Query query = sessions.createSQLQuery(queryString);
        query.setParameterList("profileId", profileId);
        List list = query.list();
        sessions.flush();
        sessions.close();
        return list;
    }
    
	@Override
	public List<Object[]> listAgentIdNamebyAgroTxn() {
		// TODO Auto-generated method stub
		return list("SELECT a.agentId ,a.agentName FROM AgroTransaction a where a.txnType in(314,316,334) AND a.agentId is not null and a.agentName is not null and a.agentId not like 'N/A' GROUP BY a.agentId order by a.agentName ASC ");
	}
	
	@Override
	public AgentAccessLogDetail listAgnetAccessLogDetailsByIdTxnType(long id, String type) {
		// TODO Auto-generated method stub
		Object[] obj={id,type};
		return (AgentAccessLogDetail) find("FROM AgentAccessLogDetail aad WHERE aad.agentAccessLog.id=? AND aad.txnType=? ORDER BY aad.id DESC",obj);
	}
	
	@Override
	public AgentAccessLogDetail listAgnetAccessLogDetailsByIdTxnType(long id, String type, String msgNo) {
		Object[] obj={id,type,msgNo};
		return (AgentAccessLogDetail) find("FROM AgentAccessLogDetail aad WHERE aad.agentAccessLog.id=? AND aad.txnType=? AND aad.messageNumber=? ORDER BY aad.id DESC",obj);
	}
	
	public long surveyMasterRevisionByDvId(String agentId) {

		Object obj = (Object) find(
				"Select MIN(sm.revisionNumber) From VerifierScope ss inner join ss.surveyMasters sm where ss.agent.profileId=?",
				agentId);

		return ObjectUtil.isLong(obj) ? Long.valueOf(String.valueOf(obj)) : 0;
	}
	
	@Override
	public List<Object[]> listMobileUser() {
	    Session sessions = getSessionFactory().openSession();
	      //  String queryString ="SELECT at.txnType,COUNT(*) FROM  AgroTransaction at WHERE at.agentId IN(:profileId) AND at.txnType =:txnType AND at.profType='01' GROUP BY at.txnType UNION (SELECT pp.txnType,COUNT(*) FROM PeriodicInspection pp WHERE pp.createdUserName IN(:profileId))";
	        String queryString ="select pi.CREATE_USER_NAME, CONCAT(p.FIRST_NAME,' ',p.LAST_NAME) from periodic_inspection pi join prof a on a.PROF_ID = pi.CREATE_USER_NAME join pers_info p on p.ID = a.PERS_INFO_ID GROUP BY pi.CREATE_USER_NAME";
	        Query query = sessions.createSQLQuery(queryString);
	        List list = query.list();
	        sessions.flush();
	        sessions.close();
	        return list;
	}
	@Override
	public Agent findAgentByProfileIdWithSurvey(String id) {

		Agent agent = (Agent) find("FROM Agent a left join fetch a.surveys WHERE a.profileId = ?", id);

		
		return agent;
	}
	public Warehouse listSelectedWarehouseById(Long agentId) {		
		
	List list = null;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		String queryString = "SELECT w.name from warehouse w LEFT JOIN agent_warehouse_map awm ON awm.WAREHOUSE_ID = w.id WHERE w.typez=2 AND awm.AGENT_ID="
				+ agentId;
		Query query = session.createSQLQuery(queryString);
		List<Warehouse> warehouseList = query.list();
		Warehouse warehouse = null;
		if (warehouseList.size() > 0) {
			warehouse = (Warehouse) warehouseList.get(0);
		}

		session.flush();
		session.close();
		return warehouse;
		
	}
	public List<Agent> listAgentByAgentTypeNotMappedwithDevice() {

			List<Agent> agent = list(
				"FROM Agent a WHERE a.id NOT IN(SELECT d.agent.id FROM Device d WHERE d.agent.id IS NOT NULL)");
		return agent;
	}

	@Override
	public List<Agent> listAgentAndOperator() {
		// TODO Auto-generated method stub
		return (List<Agent>) list("FROM Agent a WHERE a.agentType.code IN(02,03) AND a.status=1");
	}
	
	@Override
	public List<String> listSyncLogins() {
		Session sessions = getSessionFactory().openSession();
		String queryStrings = "select TXN_KEY from Login_Txns where STATUS=1";
		Query querys = sessions.createSQLQuery(queryStrings);

		List results = querys.list();
		sessions.flush();
		sessions.close();
		return results;
	}

	@Override
	public List<Agent> listAgentByWarehouse(long warehouse) {
		
		return (List<Agent>) list("FROM Agent a WHERE a.procurementCenter.id=?",warehouse);
		
	}
	
	public List<Object[]> lastLoginAndVersionByAgent(String profileId){
		  Object[] obj=null;
	        Session session = getSessionFactory().openSession();
	        String queryString = "select MAX(tl.TXN_TIME) from txn_log tl where tl.AGENT_ID='" + profileId
				+ "' AND tl.TXN_TYPE='301'";
	        Query query = session.createSQLQuery(queryString);
	        List list = query.list();
	        session.flush();
	        session.close();
	        return list;
	}
	
	public List<Object[]> lastLoginForTrackingByAgent(String profileId){
		  Object[] obj=null;
	        Session session = getSessionFactory().openSession();
	        String queryString ="select MAX(l.TXN_TIME) from loc_history l where l.AGENT_ID='" + profileId
				+ "'";
	        Query query = session.createSQLQuery(queryString);
	        List list = query.list();
	        session.flush();
	        session.close();
	        return list;
	}

	@Override
	public List<Agent> listAgentByAgentTypeWithStatus(String fieldStaff) {
		// TODO Auto-generated method stub
		return (List<Agent>) list("FROM Agent a WHERE a.agentType.code=?", fieldStaff);

	}

	@Override
	public boolean findAgentsMappedWithWarehouse(long warehouseId) {
		boolean isAgentMappedWithWarehouse = false;
		Session sessions = getSessionFactory().openSession();
		String queryStrings = "SELECT PROF_ID FROM PROF WHERE WAREHOUSE_ID = " + warehouseId;
		Query querys = sessions.createSQLQuery(queryStrings);
		List results = querys.list();
		if (results.size() > 0) {
			isAgentMappedWithWarehouse = true;
		}
		sessions.flush();
		sessions.close();
		return isAgentMappedWithWarehouse;
	}

	@Override
	public List<Agent> listAgentByWarehouseAndRevisionNo(long warehouseId, String revisionNo) {
		return (List<Agent>) list("FROM Agent a WHERE a.procurementCenter.id=? and a.revisionNumber>? ORDER BY a.id ASC",new Object[]{warehouseId,Long.parseLong(revisionNo)});
	}

	@Override
	public List<Agent> listAgentByRevisionNo(String branchId, Long revisionNo) {
		Session session = getSessionFactory().openSession();
		org.hibernate.Filter branchFilter = session.getEnabledFilter(ISecurityFilter.BRANCH_FILTER);
		if (!ObjectUtil.isEmpty(branchFilter)) {
			session.disableFilter(ISecurityFilter.BRANCH_FILTER);
		}
		List<Agent> agents = new ArrayList<>();
		Query query = session
				.createQuery("FROM Agent a where a.procurementCenter is not null ORDER BY a.id ASC");
		agents = query.list();
		
		session.flush();
		session.close();
		return agents;
		
		
	}
	
	
	@Override
	public List<Object[]> listOfTrainingCode() {
		// TODO Auto-generated method stub
		List<Object[]> result = list("SELECT DISTINCT ts.code,ts.id FROM FarmerTraining ts");
		return result;
	}
	
	@Override
	public List<Object> listOfFarmer() {
		// TODO Auto-generated method stub
		List<Object> result = list("SELECT ts.farmerIds FROM TrainingStatus ts where ts.learningGroup is not null");
		return result;
	}

	@Override
	public Agent findAgentByAgentIdd(String profileId) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().openSession();
		session.disableFilter(ISecurityFilter.BRANCH_FILTER);
		Query query = session
				.createQuery("From Agent agent where agent.profileId=:agentId");
		query.setParameter("agentId", profileId);
		Agent agent = null;
		if (query.list().size() > 0) {
			return (Agent) query.list().get(0);
		}
		session.flush();
		session.close();
		return agent;
	}

	@Override
	public List<Object[]> findFarmerCountBySamithiId() {
		// TODO Auto-generated method stub
		 Session sessions = getSessionFactory().openSession();
	        String queryString = "select f.SAMITHI_ID,count(distinct(f.id)),ROUND(sum(fdi.PROPOSED_PLANTING_AREA),2) from farmer f left join farm fa on fa.FARMER_ID =f.ID left join farm_detailed_info fdi on fdi.ID=fa.FARM_DETAILED_INFO_ID where  f.`STATUS`=1 and fa.`STATUS`=1  GROUP BY SAMITHI_ID" ; 
	        Query query = sessions.createSQLQuery(queryString);
	        List<Object[]> array = query.list();
	        sessions.flush();
	        sessions.close();
	        return array;
	}

	@Override
	public List<Object[]> listAgrotxnDetailsByAgentIdAndTxnType(List<String> agentId, List<String> txnList) {
		Session sessions = getSessionFactory().openSession();
		String queryString = "";
		if (txnList.contains("360")) {
			queryString = "SELECT pp.TXN_TYPE,COUNT(*) FROM periodic_inspection pp "
					+ "WHERE pp.CREATE_USER_NAME IN (:profileId) AND pp.txn_type IN(:txnType)";
		} else {
			queryString = "SELECT ta.txn_type,COUNT(DISTINCT ta.RECEIPT_NO) FROM trxn_agro ta "
					+ "WHERE ta.AGENT_ID IN(:profileId) AND ta.txn_type IN(:txnType) ";
		}

		Query query = sessions.createSQLQuery(queryString);
		query.setParameterList("profileId", agentId);
		query.setParameterList("txnType", txnList);
		List<Object[]> array = query.list();
		sessions.flush();
		sessions.close();
		return array;
	}
	
    public List<String> findAgentNameByWareHouseId(long coOperativeId) {

        Session session = getSessionFactory().openSession();    
        String queryString = "SELECT pi.FIRST_NAME from agent_warehouse_map awm LEFT JOIN  prof p on awm.agent_id = p.id LEFT JOIN pers_info pi on p.PERS_INFO_ID=pi.ID WHERE awm.warehouse_id ='"+coOperativeId+"'";
        Query query = session.createSQLQuery(queryString);
        List list = query.list();
        session.flush();
        session.close();
        return list;
    }

}
