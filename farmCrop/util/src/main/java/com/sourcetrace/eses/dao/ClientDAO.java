/*
 * ClientDAO.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ese.entity.util.Language;
import com.ese.entity.util.LocaleProperty;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.multitenancy.ConcatProjection;
import com.sourcetrace.eses.order.entity.txn.CustomerRegistration;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfig;
import com.sourcetrace.eses.order.entity.txn.PeriodicInspection;
import com.sourcetrace.eses.order.entity.txn.SensitizingImg;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.entity.Image;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Client;
import com.sourcetrace.esesw.entity.profile.ClientType;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.CustomerProject;
import com.sourcetrace.esesw.entity.profile.Deposit;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicFieldsValue;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.Loan;
import com.sourcetrace.esesw.entity.profile.Vendor;

/**
 * The Class ClientDAO.
 * 
 * @author $Author: aravind $
 * @version $Rev: 272 $, $Date: 2009-10-08 07:42:12 +0530 (Thu, 08 Oct 2009) $
 */
@Repository
@Transactional
public class ClientDAO extends ESEDAO implements IClientDAO {
	@Autowired
	public ClientDAO(SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}

	public List<Client> listClients() {

		return list("FROM Client");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#listClients()
	 */
	public List<Client> listClients(int clientType) {

		return list("FROM Client c where c.clientType=? ", clientType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#findByClientId(java.lang.String)
	 */
	public Client findByClientId(String clientId) {

		Client client = (Client) find("FROM Client c WHERE c.profileId = ?", clientId);
		return client;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#findByClientId(long)
	 */
	public Client findByClientId(long clientId) {

		Client client = (Client) find("FROM Client c WHERE c.id = ?", clientId);
		return client;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#findByTempClientId(java.lang.String)
	 */
	public Client findByTempClientId(String tempId) {

		Client client = (Client) find("FROM Client c WHERE c.temporaryId = ?", tempId);

		return client;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#findByImageId(java.lang.String)
	 */
	public Image findByImageId(String imageId) {

		Image image = (Image) find("FROM Image i WHERE i.imageId = ?", imageId);

		return image;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#findDepositByVoucherNumber()
	 */
	public Deposit findDepositByVoucherNumber(String number) {

		Deposit deposit = (Deposit) find("FROM Deposit d WHERE d.voucherNumber = ?", number);

		return deposit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#findLoanByLoanNumber()
	 */
	public Loan findLoanByLoanNumber(String number) {

		Loan loan = (Loan) find("FROM Loan l WHERE l.loanNumber = ?", number);

		return loan;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IClientDAO#findPinoleraByPinoleraNumber(java.lang
	 * .String)
	 */
	/*
	 * public Pinolera findPinoleraByPinoleraNumber(String number) { Pinolera
	 * pinolera = (Pinolera) find("FROM Pinolera l WHERE l.pinoleraNumber = ?",
	 * number); return pinolera; }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#findClient(long)
	 */
	public Client findClient(long id) {

		Client client = (Client) find("FROM Client c WHERE c.id = ?", id);
		return client;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#listLoans()
	 */
	public List<Loan> listLoans(String type) {

		int changetype = Integer.parseInt(type);

		return list("FROM Loan l where l.type=?", changetype);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#listDeposits()
	 */
	public List<Deposit> listDeposits() {

		return list("FROM Deposit");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#listPinoleras()
	 */
	/*
	 * public List<Pinolera> listPinoleras() { return list("FROM Pinolera"); }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#listNewClients()
	 */
	public List<Client> listNewClients() {

		return list("FROM Client c where c.status=0");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#listNewDeposits()
	 */
	public List<Deposit> listNewDeposits() {

		return list("FROM Deposit d where d.status=0");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#listNewLoans()
	 */
	public List<Loan> listNewLoans() {

		return list("FROM Loan l where l.status=0");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#listNewPinoleras()
	 */
	/*
	 * public List<Pinolera> listNewPinoleras() { return
	 * list("FROM Pinolera p where p.status=0"); }
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IClientDAO#findClientByEnrollId(java.lang.String,
	 * long)
	 */
	public List<Client> findClientByEnrollId(String enrollId, long revisionNumber) {

		Object[] bind = { enrollId, revisionNumber };

		return list("FROM Client c WHERE c.enrolledStationId = ? and c.revisionNumber >? order by c.revisionNumber",
				bind);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#clientListByRevision(int, long)
	 */
	public List<Client> clientListByRevision(int clientType, long revisionNumber) {

		Object[] bind = { clientType, revisionNumber };
		return list("FROM Client c where c.clientType=? and c.revisionNumber >? order by c.revisionNumber", bind);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IClientDAO#findClientByEnrollmentStationId(java.lang
	 * .String)
	 */
	public List<Client> findClientByEnrollmentStationId(String enrollId, int type) {

		Object[] bind = { enrollId, type };

		return list("FROM Client c WHERE c.enrolledStationId = ? and c.clientType=?", bind);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IClientDAO#findNonClientByEnrollId(java.lang.String,
	 * long)
	 */
	public List<Client> findNonClientByEnrollId(String enrollId, long revisionNumber) {

		Object[] bind = { enrollId, revisionNumber };

		return list("FROM Client c WHERE c.enrolledStationId != ? and c.revisionNumber >? order by c.revisionNumber",
				bind);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IClientDAO#findMaxRevisionNumberForClientDownload
	 * (java.lang.String, long)
	 */
	@SuppressWarnings("unchecked")
	public long findMaxRevisionNumberForClientDownload(String enrollId, long revisionNumber) {

		Object[] bind = { enrollId, revisionNumber };
		List<Long> maxRevisionNumber = (List<Long>) getHibernateTemplate().find(
				"select max(c.revisionNumber)FROM Client c WHERE c.enrolledStationId != ? and c.revisionNumber >? order by c.revisionNumber",
				bind);

		if (!(maxRevisionNumber.get(0) == null)) {
			return maxRevisionNumber.get(0);
		} else {
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#findMaxRevisionNumberForClient(int,
	 * long)
	 */
	@SuppressWarnings("unchecked")
	public long findMaxRevisionNumberForClient(int clientType, long revisionNumber) {

		Object[] bind = { clientType, revisionNumber };
		List<Long> maxRevisionNumber = (List<Long>) getHibernateTemplate().find(
				"select max(c.revisionNumber)FROM Client c WHERE c.clientType = ? and c.revisionNumber >? order by c.revisionNumber",
				bind);

		if (!(maxRevisionNumber.get(0) == null)) {
			return maxRevisionNumber.get(0);
		} else {
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IClientDAO#findMaxRevisionNumberForClient(java.lang
	 * .String, long, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public long findMaxRevisionNumberForClient(String profileType, long revisionNumber, String servicePlaceId) {

		Object[] bind = { profileType, revisionNumber, servicePlaceId };
		List<Long> maxRevisionNumber = (List<Long>) getHibernateTemplate().find(
				"select max(c.revisionNumber) FROM Client c WHERE c.profileType = ? and c.revisionNumber > ? AND c.servicePoint.code = ? ORDER BY c.revisionNumber ",
				bind);
		if (!(maxRevisionNumber.get(0) == null)) {
			return maxRevisionNumber.get(0);
		} else {
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#findMaxRevisionNumber()
	 */
	@SuppressWarnings("unchecked")
	public long findMaxRevisionNumber() {

		List<Long> revisionNumber = (List<Long>) getHibernateTemplate()
				.find("select max(a.revisionNumber)FROM Client a ");

		if (!(revisionNumber.get(0) == null)) {
			return revisionNumber.get(0);
		} else {
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IClientDAO#findLoanByLoanNumberAndType(java.lang.
	 * String, java.lang.String)
	 */
	public Loan findLoanByLoanNumberAndType(String loanNumber, String type) {

		Object[] bindValues = { loanNumber, type };
		Loan loan = (Loan) find("FROM Loan l WHERE l.loanNumber = ? and l.type=?", bindValues);

		return loan;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#findLoanByLoanId(java.lang.Long)
	 */
	public Loan findLoanByLoanId(Long loanId) {

		Loan loan = (Loan) find("FROM Loan l WHERE l.id = ?", loanId);

		return loan;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IClientDAO#listClientTransactionsByDevice(java.lang.
	 * String)
	 */
	/*
	 * public List<ClientTransaction> listClientTransactionsByDevice(String
	 * deviceId) { Session session = getSessionFactory().openSession(); Criteria
	 * criteria = session//
	 * getHibernateTemplate().getSessionFactory().getCurrentSession()
	 * .createCriteria(ClientTransaction.class); Criterion transType1 =
	 * Restrictions.between("transType", 31, 37); Criterion transType2 =
	 * Restrictions.between("transType", 53, 57); LogicalExpression orExp =
	 * Restrictions.or(transType1, transType2); criteria.add(orExp);
	 * criteria.add(Restrictions.eq("statusCode", Transaction.SUCCESS));
	 * criteria.add(Restrictions.eq("deviceId", deviceId));
	 * criteria.addOrder(Order.desc("deviceTime")); criteria.setMaxResults(15);
	 * List<ClientTransaction> list = criteria.list(); session.flush();
	 * session.close(); return list; }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#findByPersonalId(java.lang.String,
	 * java.lang.String)
	 */
	public Client findByPersonalId(String idType, String idNumber) {

		String[] values = { idType, idNumber };
		Client client = (Client) find(
				"FROM Client c WHERE c.personalInfo.identityType = ? AND c.personalInfo.identityNumber = ?", values);
		return client;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IClientDAO#findLastEnrolledClient(java.lang.String,
	 * int)
	 */
	public long findLastEnrolledClient(String enrolledStationId, int i) {

		Object[] values = { enrolledStationId, i };
		Long id = (Long) find(
				"select max(c.id) from Client c WHERE c.enrolledStationId = ? AND c.profileId not like '1199%' and c.clientType = ?",
				values);
		return id != null ? id : 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IClientDAO#findClientByClientId(java.lang.String)
	 */
	public Client findClientByClientId(String clientId) {

		Client client = (Client) find("FROM Client c WHERE c.profileId = ?", clientId);
		return client;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#findClientById(long)
	 */
	public Client findClientById(long id) {

		Client client = (Client) find("FROM Client c WHERE c.id = ?", id);
		return client;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#findClientTypeById(long)
	 */
	public ClientType findClientTypeById(long id) {

		return (ClientType) find("FROM ClientType ct WHERE ct.id = ?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#findClientExistForServicePoint(long)
	 */
	public boolean findClientExistForServicePoint(long servicePointId) {

		List<Profile> servicePointList = list("From Profile p where p.profileType=02 AND p.servicePoint.id = ?",
				servicePointId);
		boolean isClientMapExist = false;
		if (servicePointList.size() > 0) {
			isClientMapExist = true;
		}
		return isClientMapExist;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#isCityMappingexist(long)
	 */
	public boolean isCityMappingexist(long id) {

		List<Client> clientList = list("FROM Client c WHERE c.contactInfo.city.id = ?", id);
		if (!ObjectUtil.isListEmpty(clientList)) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IClientDAO#findCustomerByCustomerId(java.lang.String)
	 */
	public CustomerRegistration findCustomerByCustomerId(String customerId) {

		CustomerRegistration customer = (CustomerRegistration) find(
				"FROM CustomerRegistration cr WHERE cr.customerId = ?", customerId);
		return customer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#listCustomers()
	 */
	public List<CustomerRegistration> listCustomers() {

		return list("From CustomerRegistration c");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#findCustomerById(java.lang.Long)
	 */
	public CustomerRegistration findCustomerById(Long id) {

		CustomerRegistration customer = (CustomerRegistration) find("FROM CustomerRegistration cr WHERE cr.id = ?", id);
		return customer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#listOfCustomers()
	 */
	
	public List<Customer> listOfCustomers() {

		return list("FROM Customer c ORDER BY c.customerName");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#findCustomerById(long)
	 */
	public Customer findCustomer(long id) {

		Customer customer = (Customer) find("FROM Customer c WHERE c.id = ?", id);

		return customer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IClientDAO#findCustomerByCustomerId(java.lang.String)
	 */
	public Customer findCustomerById(String customerId) {

		Customer customer = (Customer) find("FROM Customer c WHERE c.customerId = ?", customerId);
		return customer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#isCustomerWithMappedFarmer(long)
	 */
	public boolean isCustomerWithMappedFarmer(long id) {

		boolean isCustomerMapped = false;

		Farmer farmer = (Farmer) find("FROM Farmer f WHERE f.customer.id=?", id);
		if (!ObjectUtil.isEmpty(farmer)) {
			isCustomerMapped = true;
		}
		return isCustomerMapped;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#isCustomerWithMappedFieldStaff(long)
	 */
	public boolean isCustomerWithMappedFieldStaff(long id) {

		boolean isCustomerMappedWithFS = false;

		Agent agent = (Agent) find("FROM Agent ag WHERE ag.customer.id=?", id);
		if (!ObjectUtil.isEmpty(agent)) {
			isCustomerMappedWithFS = true;
		}
		return isCustomerMappedWithFS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IClientDAO#findCustomerProjectByCodeOfProject(java.
	 * lang.String)
	 */
	public CustomerProject findCustomerProjectByCodeOfProject(String code) {

		CustomerProject customerProject = (CustomerProject) find("FROM CustomerProject c WHERE c.codeOfProject = ?",
				code);
		return customerProject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IClientDAO#listOfCustomerProject(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<CustomerProject> listOfCustomerProject(String customer) {

		return list("FROM CustomerProject c WHERE c.customer.customerId=?", customer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#findCustomerProjectById(long)
	 */
	public CustomerProject findCustomerProjectById(long id) {

		CustomerProject customerProject = (CustomerProject) find("FROM CustomerProject cp WHERE cp.id=?", id);
		return customerProject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#isProjectWithMappedFieldStaff(long)
	 */
	public boolean isProjectWithMappedFieldStaff(long id) {

		boolean isProjectMappedWithFS = false;

		String query = "SELECT COUNT(*) as count FROM AGENT_CUSTOMER_PROJECT_MAPPING WHERE " + "CUSTOMER_PROJECT_ID = '"
				+ id + "'";
		int count = Integer
				.valueOf(getSessionFactory().getCurrentSession().createSQLQuery(query).list().get(0).toString());
		if (count > 0) {
			isProjectMappedWithFS = true;
		}

		return isProjectMappedWithFS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#isProjectWithMappedFarmer(long)
	 */
	public boolean isProjectWithMappedFarmer(long id) {

		boolean isProjectMappedWithFarmer = false;

		Farmer farmerObj = (Farmer) find("FROM Farmer f WHERE f.customerProject.id=?", id);
		if (!ObjectUtil.isEmpty(farmerObj)) {
			isProjectMappedWithFarmer = true;
		}
		return isProjectMappedWithFarmer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IClientDAO#removeCustomerProjectwithAgentMapping(
	 * long)
	 */
	public void removeCustomerProjectwithAgentMapping(long agentId) {

		String queryString = "DELETE FROM AGENT_CUSTOMER_PROJECT_MAPPING WHERE AGENT_ID=" + agentId;
		Session sessions = getSessionFactory().openSession();
		Query querys = sessions.createSQLQuery(queryString);
		querys.executeUpdate();
		sessions.flush();
		sessions.close();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.profile.IClientDAO#listOfCustomerProjectByCustomerId(java.
	 * lang.Long)
	 */
	/**
	 * List of customer project by customer id.
	 * 
	 * @param id
	 *            the id
	 * @return the list< customer project>
	 */
	public List<CustomerProject> listOfCustomerProjectByCustomerId(long id) {

		return list("FROM CustomerProject c WHERE c.customer.id=?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#listCustomerByRevNo(java.lang.Long)
	 */
	public List<Customer> listCustomerByRevNo(Long revNo) {

		return list("FROM Customer cust WHERE cust.revisionNo > ? ORDER BY cust.revisionNo DESC", revNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#findCustomerProjectLatestRevisionNo()
	 */
	public Long findCustomerProjectLatestRevisionNo() {

		List list = list("Select Max(c.revisionNo) FROM CustomerProject c");
		if (!ObjectUtil.isListEmpty(list) && !ObjectUtil.isEmpty(list.get(0)))
			return (Long) list.get(0);
		return 0l;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.profile.IClientDAO#findCustomerLatestRevisionNo()
	 */
	public Long findCustomerLatestRevisionNo() {

		List list = list("Select Max(c.revisionNo) FROM Customer c");
		if (!ObjectUtil.isListEmpty(list) && !ObjectUtil.isEmpty(list.get(0)))
			return (Long) list.get(0);
		return 0l;
	}

	public BranchMaster findBranchMasterById(Long id) {

		BranchMaster branchMaster = (BranchMaster) find("FROM BranchMaster bm WHERE bm.id = ?", id);
		return branchMaster;
	}

	public BranchMaster findBranchMasterByBranchId(String branchId) {

		BranchMaster branchMaster = (BranchMaster) find("FROM BranchMaster bm WHERE bm.branchId = ?", branchId);
		return branchMaster;
	}

	public BranchMaster findBranchMasterByName(String name) {

		BranchMaster branchMaster = (BranchMaster) find("FROM BranchMaster bm WHERE bm.name = ?", name);
		return branchMaster;
	}

	@Override
	public List<BranchMaster> listBranchMasters() {

		// TODO Auto-generated method stub
		Object[] value = { FarmCatalogue.ACTIVE };
		return list("FROM BranchMaster br where br.status=? ORDER BY br.name ASC", value);
	}

	public byte[] findLogoByCode(String code) {

		byte[] logo = (byte[]) find("SELECT a.file FROM Asset a WHERE a.code = ?", code);
		return logo;
	}

	@Override
	public List<Object[]> listBranchMastersInfo() {

		// TODO Auto-generated method stub
		return list("SELECT br.branchId,br.name FROM BranchMaster br ORDER BY br.name ASC");
	}

	@Override
	public Object[] findBranchInfo(String branchId) {

		// TODO Auto-generated method stub
		return (Object[]) find("SELECT br.branchId,br.name FROM BranchMaster br WHERE br.branchId=?", branchId);
	}

	public List<BranchMaster> listBranchMastersAndDisableFilter() {

		// TODO Auto-generated method stub
		Session session = getSessionFactory().openSession();
		session.disableFilter(ISecurityFilter.BRANCH_FILTER);
		Query query = session.createQuery("FROM BranchMaster br ORDER BY br.name ASC");
		List<BranchMaster> list = query.list();

		session.flush();
		session.close();

		return list;
	}

	public BranchMaster findBranchMasterByBranchIdAndDisableFilter(String branchId) {

		Session session = getSessionFactory().openSession();
		session.disableFilter(ISecurityFilter.BRANCH_FILTER);
		Query query = session.createQuery("FROM BranchMaster bm WHERE bm.branchId = :branchId");
		query.setParameter("branchId", branchId);
		BranchMaster branchMaster = (BranchMaster) query.list().get(0);
		session.flush();
		session.close();
		return branchMaster;
	}

	public BranchMaster findBranchMasterByIdAndDisableFilter(Long id) {

		Session session = getSessionFactory().openSession();
		session.disableFilter(ISecurityFilter.BRANCH_FILTER);
		Query query = session.createQuery("FROM BranchMaster bm WHERE bm.id = :id");
		query.setParameter("id", id);
		BranchMaster branchMaster = (BranchMaster) query.list().get(0);
		session.flush();
		session.close();
		return branchMaster;
	}

	public Asset findAssetsByAssetCode(String code) {

		Asset asset = (Asset) find("FROM Asset ast WHERE ast.code = ?", code);
		return asset;
	}

	public MasterData findMasterDataById(Long id) {

		MasterData masterData = (MasterData) find("FROM MasterData md WHERE md.id = ?", id);
		return masterData;
	}

	@Override
	public String findLocaleProperty(String code, String languageCode) {

		// TODO Auto-generated method stub
		Session session = currentSession();
		Query query = session.createQuery(
				"SELECT l.langValue FROM LocaleProperty l WHERE l.code=:code AND l.langCode=:languageCode");
		query.setParameter("code", code);
		query.setParameter("languageCode", languageCode);
		List<Object> list = query.list();
		Object object = ObjectUtil.isListEmpty(list) ? null : list.get(0);
		return ObjectUtil.isEmpty(object) ? "" : object.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.dao.IClientDAO#listLanguages()
	 */
	@Override
	public List<Language> listLanguages() {
		// TODO Auto-generated method stub
		return list("FROM Language l WHERE l.webStatus=1 ORDER by l.code ASC");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.dao.IClientDAO#findLanguageByCode(java.lang.String)
	 */
	@Override
	public Language findLanguageByCode(String code) {

		// TODO Auto-generated method stub
		return (Language) find("FROM Language l WHERE l.code=?", code);
	}

	/*
	 * @Override public String findCurrentSeasonCode() { Session session =
	 * currentSession(); Query query = session.
	 * createQuery("SELECT hs.code from HarvestSeason hs where currentSeason=:seasonValue"
	 * );
	 * query.setParameter("seasonValue",HarvestSeason.SeasonTypes.CURRENT_SEASON
	 * .ordinal()); List<Object> list = query.list(); Object object =
	 * ObjectUtil.isListEmpty(list) ? null : list.get(0); return
	 * ObjectUtil.isEmpty(object) ? "" : object.toString(); }
	 */

	@Override
	public String findCurrentSeasonName(String seasonCode) {
		Session session = currentSession();
		Query query = session.createQuery("SELECT hs.name from HarvestSeason hs where code=:seasonValue");
		query.setParameter("seasonValue", seasonCode);
		List<Object> list = query.list();
		Object object = ObjectUtil.isListEmpty(list) ? null : list.get(0);
		return ObjectUtil.isEmpty(object) ? "" : object.toString();
	}

	@Override
	public HarvestSeason findSeasonByCode(String code) {
		HarvestSeason harvestSeason = (HarvestSeason) find("FROM HarvestSeason hs WHERE hs.code=?", code);
		return harvestSeason;
	}

	@Override
	public HarvestSeason findSeasonByCodeByTenant(String code, String tenantId) {
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery("FROM HarvestSeason hs WHERE hs.code=:code");
		query.setParameter("code", code);
		@SuppressWarnings("unchecked")
		List<HarvestSeason> harvestSeasonList = query.list();
		HarvestSeason harvestSeason = null;
		if (harvestSeasonList.size() > 0) {
			harvestSeason = harvestSeasonList.get(0);
		}
		session.flush();
		session.close();
		return harvestSeason;
	}

	@Override
	public PeriodicInspection findPeriodicInspectionById(Long id) {

		PeriodicInspection periodicInspection = (PeriodicInspection) find("FROM PeriodicInspection pi WHERE pi.id=?",
				id);
		return periodicInspection;
	}

	@Override
	public List<BranchMaster> listChildBranchIds(String parentBranch) {
		Session session = getSessionFactory().openSession();
		session.disableFilter(ISecurityFilter.BRANCH_FILTER);
		Query query = session.createQuery("FROM BranchMaster hs WHERE hs.parentBranch=:parentBranch");
		query.setParameter("parentBranch", parentBranch);
		List<BranchMaster> list = query.list();
		session.flush();
		session.clear();
		session.close();
		return list;
	}

	@Override
	public List<Object[]> findParentBranches() {
		Object[] bindValues = { "" };
		return list(
				"SELECT br.branchId,br.name FROM BranchMaster br WHERE br.parentBranch IS NULL OR br.parentBranch=? ORDER BY br.name ASC",
				bindValues);
	}

	@Override
	public List<Object[]> findChildBranches() {
		Object[] bindValues = { "" };
		return list(
				"SELECT br.branchId,br.name FROM BranchMaster br WHERE br.parentBranch IS NOT NULL OR br.parentBranch!=? ORDER BY br.name ASC",
				bindValues);
	}

	@Override
	public List<Object[]> listParentBranchMastersInfo() {
		return list(
				"SELECT br.branchId,br.parentBranch FROM BranchMaster br WHERE br.parentBranch IS NOT NULL ORDER BY br.name ASC");
	}

	public Integer isParentBranch(String loggedUserBranch) {
		Integer returnVal = 0;
		Session session = getSessionFactory().openSession();
		session.disableFilter(ISecurityFilter.BRANCH_FILTER);
		Query query = session.createQuery("FROM BranchMaster hs WHERE hs.parentBranch=:parentBranch");
		query.setParameter("parentBranch", loggedUserBranch);
		List<BranchMaster> list = query.list();
		if (list.size() > 0) {
			returnVal = 1;
		}
		session.flush();
		session.clear();
		session.close();
		return returnVal;
	}

	@Override
	public SensitizingImg findSensitizingImgById(Long id) {
		SensitizingImg sensitizingImg = (SensitizingImg) find("FROM SensitizingImg s WHERE s.id = ?", id);
		return sensitizingImg;

	}

	@Override
	public Vendor findVendorById(String id) {
		Vendor vendor = (Vendor) find("FROM Vendor v WHERE v.vendorId = ?", id);
		return vendor;
	}

	@Override
	public DynamicReportConfig findReportByName(String name) {
		return (DynamicReportConfig) find(
				"FROM DynamicReportConfig drc LEFT JOIN FETCH drc.dynmaicReportConfigFilters drcf LEFT JOIN FETCH drc.dynmaicReportConfigDetails drcd WHERE drc.report=? ORDER BY drcd.order ASC",
				name);
	}
	
	@Override
	public DynamicReportConfig findReportById(String id) {
		return (DynamicReportConfig) find(
				"FROM DynamicReportConfig drc LEFT JOIN FETCH drc.dynmaicReportConfigFilters drcf LEFT JOIN FETCH drc.dynmaicReportConfigDetails drcd WHERE drc.id=? ORDER BY drcd.order ASC",
				Long.valueOf(id));
	}

	@Override
	public List<LocaleProperty> listLocalePropByLang(String lang) {
		return list("FROM LocaleProperty lp where lp.langCode=?", lang);
	}
int i=0;
	@SuppressWarnings("rawtypes")
	public List<Object> getMappedValues(Map criteriaMap, String refId) {

		Session session = getSessionFactory().openSession();
		List<Object> finaObj = new ArrayList<>();
		Object[] strRy  =null;
		Object entity = criteriaMap.get(IClientService.ENTITY);
		String criteriaAlias = (String) criteriaMap.get(IClientService.CRITERIA_ALIAS);
		String projetcionsList = (String) criteriaMap.get(IClientService.PROJECTIONS_LIST);
		String projetcionsGroup = (String) criteriaMap.get(IClientService.PROJECTIONS_GROUP);
		String condtion = (String) criteriaMap.get(IClientService.PROJECTIONS_CONDITION);

		Criteria criteria = session.createCriteria((Class) entity);

		if (!StringUtil.isEmpty(criteriaAlias)) {
			/*
			 * String[] aliasSplit = criteriaAlias.split(",");
			 * criteria.createAlias(aliasSplit[0],aliasSplit[1]);
			 */
			Arrays.asList(criteriaAlias.split(",")).stream().forEach(restriction -> {
				String[] aliasSplit = restriction.split("=");
				criteria.createAlias(aliasSplit[1].trim(), aliasSplit[0].trim());
			});
		}

		ProjectionList pList = Projections.projectionList();
		if (!StringUtil.isEmpty(projetcionsList)) {
			Arrays.asList(projetcionsList.split(",")).stream().forEach(property -> {
				if(property.contains("|")){
					pList.add( new ConcatProjection(" ",property.split("\\|")));			
				}else{
					pList.add(Projections.property(property));
				}
				
			});
		}

		if (!StringUtil.isEmpty(projetcionsGroup)) {
			Arrays.asList(projetcionsGroup.split(",")).stream().forEach(property -> {
				pList.add(Projections.groupProperty(property));
			});
		}

		if (!StringUtil.isEmpty(projetcionsList) || !StringUtil.isEmpty(projetcionsGroup)) {
			criteria.setProjection(pList);
		}
		List<String> refIds = new ArrayList<>();
		if (refId.contains(",")) {
			refIds = Arrays.asList(refId.split(","));
		} else {
			refIds.add(refId);
		}
		criteria.add(Restrictions.in("id", ObjectUtil.stringListToLongList(refIds)));
		if(condtion!=null && !StringUtil.isEmpty(condtion)){
		Arrays.asList(condtion.split(",")).stream().forEach(restriction -> {
			String[] aliasSplit = restriction.split("=");
			criteria.add(Restrictions.eq(aliasSplit[0].trim(), Long.valueOf(aliasSplit[1].trim())));
		});
		}
		List<Object> list = criteria.list();
		strRy = new Object[pList.getLength()];
		if(pList.getLength()>0){
		if(list.get(0) instanceof Object[]){
		for( i=0;i < strRy.length;i++){
			strRy[i] = list.stream().map(e -> ((Object[] )e)[i].toString()).distinct().collect(Collectors.joining(","));
		}
		}else{
			for( i=0;i < strRy.length;i++){
				strRy[i] = list.stream().map(e ->  ((String )e)).distinct().collect(Collectors.joining(","));
			}
		}
		}
		finaObj.add(strRy);
		session.flush();
		session.close();
		return finaObj;
	
	}
	List<Object[]> list1 = new ArrayList<>();
	int q=0;
	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getMappedValuesAll(Map criteriaMap, List<Long> issList) {
		 List<Long> isList = issList.stream() 
                 .distinct() 
                 .collect(Collectors.toList()); 
		Session session = getSessionFactory().openSession();
		Object entity = criteriaMap.get(IClientService.ENTITY);
		String criteriaAlias = (String) criteriaMap.get(IClientService.CRITERIA_ALIAS);
		String projetcionsList = (String) criteriaMap.get(IClientService.PROJECTIONS_LIST);
		String projetcionsGroup = (String) criteriaMap.get(IClientService.PROJECTIONS_GROUP);
		String condtion = (String) criteriaMap.get(IClientService.PROJECTIONS_CONDITION);
		list1 = new ArrayList<>();

		final AtomicInteger counter = new AtomicInteger();
		Collection<List<Long>> result = isList.stream()
			    .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / 2000))
			    .values();
		result.stream().forEach(u->{
			Criteria criteria = session.createCriteria((Class) entity);

			if (!StringUtil.isEmpty(criteriaAlias)) {
				/*
				 * String[] aliasSplit = criteriaAlias.split(",");
				 * criteria.createAlias(aliasSplit[0],aliasSplit[1]);
				 */
				Arrays.asList(criteriaAlias.split(",")).stream().forEach(restriction -> {
					String[] aliasSplit = restriction.split("=");
					criteria.createAlias(aliasSplit[1].trim(), aliasSplit[0].trim());
				});
			}

			ProjectionList pList = Projections.projectionList();
			if (!StringUtil.isEmpty(projetcionsList)) {
				Arrays.asList(projetcionsList.split(",")).stream().forEach(property -> {
					if(property.contains("|")){
						pList.add( new ConcatProjection(" ",property.split("\\|")));			
					}else{
						pList.add(Projections.property(property));
					}
					
				});
			}

			if (!StringUtil.isEmpty(projetcionsGroup)) {
				Arrays.asList(projetcionsGroup.split(",")).stream().forEach(property -> {
					pList.add(Projections.groupProperty(property));
				});
			}

			if (!StringUtil.isEmpty(projetcionsList) || !StringUtil.isEmpty(projetcionsGroup)) {
				criteria.setProjection(pList);
			}

			if (condtion != null && condtion.contains(",")) {
				Arrays.asList(condtion.split(",")).stream().forEach(restriction -> {
					String[] aliasSplit = restriction.split("=");
					criteria.add(Restrictions.eq(aliasSplit[0].trim(), Long.valueOf(aliasSplit[1].trim())));
				});
			} else if (condtion != null) {
				String[] aliasSplit = condtion.split("=");
				criteria.add(Restrictions.eq(aliasSplit[0].trim(), Long.valueOf(aliasSplit[1].trim())));
			}
				criteria.add(Restrictions.in("id", u));
				List<Object[]> list = criteria.list();
				list1.addAll(list);

		});
		session.flush();
		session.close();
		
		
		return list1;
	
	}

	@Override
	public MasterData findMasterDataByCode(String code) {
		MasterData masterData = (MasterData) find("FROM MasterData md WHERE md.code = ?", code);
		return masterData;
	}

	@Override
	public List<PeriodicInspection> findPeriodicInspectionByFarmerId(String farmerId) {
		// TODO Auto-generated method stub
		// return list("FROM PeriodicInspection p INNER JOIN p.farm fa INNER
		// JOIN fa.farmer f where f.farmerId=?",farmerId);
		return list("SELECT p FROM PeriodicInspection p INNER JOIN p.farm fa INNER JOIN fa.farmer f where f.farmerId=?",
				farmerId);
	}

	@Override
	public List<String> findBranchList() {
		// TODO Auto-generated method stub
		return list("Select b.name from BranchMaster b");
	}

	@Override
	public void saveFarmerDynmaicList(List<FarmerDynamicFieldsValue> farmerDynamicFieldValuesList) {
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		AtomicInteger index = new AtomicInteger();

		// Batch Insert Principle used here

		farmerDynamicFieldValuesList.forEach(empDetail -> {
			session.saveOrUpdate(empDetail);
			if (index.incrementAndGet() % 20 == 0) { // 20, same as the JDBC
														// batch size
				// flush a batch of inserts and release memory:
				session.flush();
				session.clear();
			}
		});
		tx.commit();
		session.close();
	}

	public void updateFarmerDynmaicList(List<FarmerDynamicFieldsValue> farmerDynamicFieldValuesList, Long id) {
		Session deleteSession = getSessionFactory().openSession();
		Query query = deleteSession
				.createSQLQuery("DELETE FROM farmer_dynamic_field_value where REFERENCE_ID=" + id + "");
		int result = query.executeUpdate();
		deleteSession.flush();
		deleteSession.close();

		saveFarmerDynmaicList(farmerDynamicFieldValuesList);

	}

	@Override
	public List<Object[]> listMaxTypeByRefId(String refId) {
		Object[] values = { refId };
		return list(
				"SELECT fdfv.parentId,MAX(fdfv.typez) FROM FarmerDynamicFieldsValue fdfv WHERE fdfv.referenceId=? AND fdfv.typez IS NOT NULL  and  fdfv.parentId is not null GROUP BY fdfv.parentId",
				values);
	}

	@Override
	public List<Object[]> listMaxTypeByFarmerId(Long farmerId) {
		Object[] values = { farmerId };
		return list(
				"SELECT fdfv.fieldName,fdfv.typez FROM FarmerDynamicFieldsValue fdfv WHERE fdfv.farmer.id=? AND fdfv.typez IS NOT NULL GROUP BY fdfv.fieldName",
				values);
	}

	@Override
	public Object findProcurementTraceabilityImgById(Long id) {
		// TODO Auto-generated method stub
		/*
		 * ProcurementTraceability pt = (ProcurementTraceability)
		 * find("FROM ProcurementTraceability p left join fetch p.farmer f WHERE p.id = ?"
		 * , id);
		 */
		Object pt = (Object) find("SELECT p.stripImage FROM ProcurementTraceability p WHERE p.id = ?", id);
		return pt;
	}

	public Long findProductlatestRevisionNo() {

		List list = list("Select Max(p.revisionNo) FROM Product p");
		if (!ObjectUtil.isListEmpty(list) && !ObjectUtil.isEmpty(list.get(0)))
			return (Long) list.get(0);
		return 0l;
	}

	public Long findSeasonlatestRevisionNo() {

		List list = list("Select Max(s.revisionNo) FROM Season s");
		if (!ObjectUtil.isListEmpty(list) && !ObjectUtil.isEmpty(list.get(0)))
			return (Long) list.get(0);
		return 0l;
	}

	public Long findLocationlatestRevisionNo() {

		List list = list("Select Max(l.revisionNo) FROM Locality l");
		if (!ObjectUtil.isListEmpty(list) && !ObjectUtil.isEmpty(list.get(0)))
			return (Long) list.get(0);
		return 0l;
	}

	public Long findWarouseStocklatestRevisionNo() {

		List list = list("Select Max(p.revisionNo) FROM ProcurementTraceabilityStock p");
		if (!ObjectUtil.isListEmpty(list) && !ObjectUtil.isEmpty(list.get(0)))
			return (Long) list.get(0);
		return 0l;
	}

	@Override
	public Long findVillageWarehouselatestRevisionNo() {

		List list = list("Select Max(w.revisionNo) FROM WarehouseProduct w");
		if (!ObjectUtil.isListEmpty(list) && !ObjectUtil.isEmpty(list.get(0)))
			return (Long) list.get(0);
		return 0l;
	}

	public Long findBuyerlatestRevisionNo() {

		List list = list("Select Max(c.revisionNo) FROM Customer c");
		if (!ObjectUtil.isListEmpty(list) && !ObjectUtil.isEmpty(list.get(0)))
			return (Long) list.get(0);
		return 0l;
	}

	public Long findCataloguelatestRevisionNo() {

		List list = list("Select Max(fc.revisionNo) FROM FarmCatalogue fc");
		if (!ObjectUtil.isListEmpty(list) && !ObjectUtil.isEmpty(list.get(0)))
			return (Long) list.get(0);
		return 0l;
	}

	public Long findCooperativelatestRevisionNo() {

		List list = list("Select Max(w.revisionNo) FROM Warehouse w WHERE w.typez=1");
		if (!ObjectUtil.isListEmpty(list) && !ObjectUtil.isEmpty(list.get(0)))
			return (Long) list.get(0);
		return 0l;
	}

	public Long findSupplierlatestRevisionNo() {

		List list = list("Select Max(m.revisionNo) FROM MasterData m");
		if (!ObjectUtil.isListEmpty(list) && !ObjectUtil.isEmpty(list.get(0)))
			return (Long) list.get(0);
		return 0l;
	}

	public Long findFarmerOutstandinglatestRevisionNo() {

		List list = list("Select Max(c.account.updateTime) FROM Contract c");
		if (!ObjectUtil.isListEmpty(list) && !ObjectUtil.isEmpty(list.get(0)))
			return (Long) list.get(0);
		return 0l;
	}

	public String findProcurementProductlatestRevisionNo() {

		List list = list("Select Max(pp.revisionNo) FROM ProcurementProduct pp");
		List list1 = list("Select Max(pv.revisionNo) FROM ProcurementVariety pv");
		List list2 = list("Select Max(pg.revisionNo) FROM ProcurementGrade pg");
		if (!ObjectUtil.isListEmpty(list) && !ObjectUtil.isEmpty(list.get(0)) && !ObjectUtil.isListEmpty(list1)
				&& !ObjectUtil.isEmpty(list1.get(0)) && !ObjectUtil.isListEmpty(list2)
				&& !ObjectUtil.isEmpty(list2.get(0)))
			return (String) (list.get(0) + "|" + list1.get(0) + "|" + list2.get(0));
		return null;
	}

	public Long findfarmerDistributionBalancelatestRevisionNo() {

		List list = list("Select Max(d.revisionNo) FROM DistributionBalance d");
		if (!ObjectUtil.isListEmpty(list) && !ObjectUtil.isEmpty(list.get(0)))
			return (Long) list.get(0);
		return 0l;
	}

	public Long finddynamicComponentlatestRevisionNo() {

		List list = list("Select Max(d.revisionNo) FROM DynamicFeildMenuConfig d");
		if (!ObjectUtil.isListEmpty(list) && !ObjectUtil.isEmpty(list.get(0)))
			return (Long) list.get(0);
		return 0l;
	}
	@Override
	public List<Object[]> listCustomerIdAndName() {
		return list("SELECT DISTINCT c.id,c.customerName from Customer c");
	}

	@Override
	public String findReportByMainGridId(String id) {

		return (String) find("SELECT d.report From DynamicReportConfig d where d.parentId = ? ", id);
	
	}

	

}
