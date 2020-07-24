/*
 * ClientService.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
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

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.ese.entity.traceability.ProcurementTraceability;
import com.ese.entity.util.ESESystem;
import com.ese.entity.util.Language;
import com.ese.entity.util.LocaleProperty;
import com.sourcetrace.eses.dao.IClientDAO;
import com.sourcetrace.eses.dao.IESESystemDAO;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.order.entity.txn.CustomerRegistration;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfig;
import com.sourcetrace.eses.order.entity.txn.PeriodicInspection;
import com.sourcetrace.eses.order.entity.txn.SensitizingImg;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.entity.Image;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Client;
import com.sourcetrace.esesw.entity.profile.ClientType;
import com.sourcetrace.esesw.entity.profile.Currency;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.CustomerProject;
import com.sourcetrace.esesw.entity.profile.Deposit;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicFieldsValue;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.Loan;
import com.sourcetrace.esesw.entity.profile.Vendor;

/**
 * The Class ClientService.
 * 
 * @author $Author: aravind $
 * @version $Rev: 315 $, $Date: 2009-10-08 10:02:48 +0530 (Thu, 08 Oct 2009) $
 */
@Transactional
@Service
public class ClientService implements IClientService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(ClientService.class);

	@Autowired
	private IClientDAO clientDAO;
	@Autowired
	private IESESystemDAO systemDAO;

	@Autowired
	private IUniqueIDGenerator idGenerator;
	@Autowired
	private IPreferencesService preferncesService;

	// private ICurrencyDAO currencyDAO;

	private VelocityEngine velocityEngine;

	private MailSender mailSender;

	/**
	 * Sets the client dao.
	 * 
	 * @param clientDAO
	 *            the new client dao
	 */
	public void setClientDAO(IClientDAO clientDAO) {

		this.clientDAO = clientDAO;
	}

	public IUniqueIDGenerator getIdGenerator() {
		return idGenerator;
	}

	public void setIdGenerator(IUniqueIDGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#addClient(com.ese.entity.profile
	 * .Client)
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void addClient(Client client) {

		long revisionNo = clientDAO.findMaxRevisionNumber();

		client.setRevisionNumber(++revisionNo);

		clientDAO.save(client);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#convertImageByte(com.ese.entity
	 * .profile.Image)
	 */
	public byte[] convertImageByte(Image image) {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#editBiometric(com.ese.entity.
	 * profile .Client)
	 */
	public void editBiometric(Client client) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#editClient(com.ese.entity.profile
	 * .Client)
	 */
	public void editClient(Client client) {

		clientDAO.update(client);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#editClientTxnCredential(com.ese
	 * .entity.profile.Client)
	 */
	public void editClientTxnCredential(Client client) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#findByPersonalId(java.lang.String,
	 * java.lang.String)
	 */
	public Client findByPersonalId(String identityType, String identityNumber) {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#findClient(java.lang.String)
	 */
	public Client findClient(String clientId) {

		return clientDAO.findByClientId(clientId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#findClient(long)
	 */
	public Client findClient(long id) {

		return clientDAO.findByClientId(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#findClientByEnrollId(java.lang
	 * .String, long)
	 */
	public List<Client> findClientByEnrollId(String enrollId, long revisionNumber) {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#findClientByTempClientId(java.
	 * lang.String)
	 */
	public Client findClientByTempClientId(String clientId) {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#findDeposit(java.lang.String)
	 */
	public Deposit findDeposit(String voucherNumber) {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#findImage(java.lang.String)
	 */
	public Image findImage(String imageId) {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#findLastEnrolledClient(java.lang
	 * .String, int)
	 */
	public Client findLastEnrolledClient(String enrolledStationId, int i) {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#findLoan(java.lang.String)
	 */
	public Loan findLoan(String loanNumber) {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#findLoanById(java.lang.Long)
	 */
	public Loan findLoanById(Long loanId) {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#findLoanByLoanNumAndType(java.
	 * lang.String, java.lang.String)
	 */
	public Loan findLoanByLoanNumAndType(String loanNumber, String type) {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#
	 * findMaxRevisionNumberForClientDownload (java.lang.String, long)
	 */
	public long findMaxRevisionNumberForClientDownload(String enrollId, long revisionNumber) {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#findNonClientByEnrollId(java.lang
	 * .String, long)
	 */
	public List<Client> findNonClientByEnrollId(String enrolledStationId, long revisionNumber) {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#findPinolera(java.lang.String)
	 */
	/*
	 * public Pinolera findPinolera(String pinoleraNumber) {
	 * 
	 * return null; }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#findTransactionType(int)
	 */
	public String findTransactionType(int id) {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#listClientTransactionsByDevice
	 * (java.lang.String)
	 */
	/*
	 * public List<ClientTransaction> listClientTransactionsByDevice(String
	 * deviceId) {
	 * 
	 * return null; }
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#listClients()
	 */
	public List<Client> listClients() {

		return this.clientDAO.listClients();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#listCurrency()
	 */
	public List<Currency> listCurrency() {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#listDeposit(java.lang.String)
	 */
	public Set<Deposit> listDeposit(String clientId) {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#listDeposits()
	 */
	public List<Deposit> listDeposits() {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#listLoan(java.lang.String)
	 */
	public Set<Loan> listLoan(String clientId) {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#listLoans(java.lang.String)
	 */
	public List<Loan> listLoans(String type) {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#listPinolera(java.lang.String)
	 */
	/*
	 * public Set<Pinolera> listPinolera(String clientId) {
	 * 
	 * return null; }
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#listPinoleras()
	 */
	/*
	 * public List<Pinolera> listPinoleras() {
	 * 
	 * return null; }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#removeClient(com.ese.entity.
	 * profile .Client)
	 */
	public void removeClient(Client client) {

		clientDAO.delete(client);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#saveClient(java.util.Map)
	 */
	public void saveClient(Map<String, String[]> parameters) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#saveDeposit(java.util.Map)
	 */
	public void saveDeposit(Map<String, String[]> parameters) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#saveLoan(java.util.Map)
	 */
	public void saveLoan(Map<String, String[]> parameters) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#savePinolera(java.util.Map)
	 */
	public void savePinolera(Map<String, String[]> parameters) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#updatePinolera(java.util.Map)
	 */
	public void updatePinolera(Map<String, String[]> parameters) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#findClientByClientId(java.lang
	 * .String)
	 */
	public Client findClientByClientId(String clientId) {

		return clientDAO.findClientByClientId(clientId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#findClientById(long)
	 */
	public Client findClientById(long id) {

		return clientDAO.findClientById(id);
	}

	/**
	 * Gets the velocity engine.
	 * 
	 * @return the velocity engine
	 */
	public VelocityEngine getVelocityEngine() {

		return velocityEngine;
	}

	/**
	 * Sets the velocity engine.
	 * 
	 * @param velocityEngine
	 *            the new velocity engine
	 */
	public void setVelocityEngine(VelocityEngine velocityEngine) {

		this.velocityEngine = velocityEngine;
	}

	/**
	 * Sets the mail sender.
	 * 
	 * @param mailSender
	 *            the new mail sender
	 */
	public void setMailSender(MailSender mailSender) {

		this.mailSender = mailSender;
	}

	/**
	 * Gets the mail sender.
	 * 
	 * @return the mail sender
	 */
	public MailSender getMailSender() {

		return mailSender;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#getEmailMessage(com.ese.entity
	 * .profile.Client)
	 */
	public String getEmailMessage(Client client) {

		Map<String, String> model = new HashMap<String, String>();
		model.put("clientId", client.getProfileId());
		if (client.getPersonalInfo() != null) {
			model.put("firstName",
					client.getPersonalInfo().getFirstName() != null ? client.getPersonalInfo().getFirstName() : "");
			model.put("middleName",
					client.getPersonalInfo().getMiddleName() != null ? client.getPersonalInfo().getMiddleName() : "");
			model.put("lastName",
					client.getPersonalInfo().getLastName() != null ? client.getPersonalInfo().getLastName() : "");
			model.put("secondLastName", client.getPersonalInfo().getSecondLastName() != null
					? client.getPersonalInfo().getSecondLastName() : "");
		}

		String emailTemplate = null;

		model.put("clientId", client.getProfileId() != null ? client.getProfileId() : "");
		emailTemplate = "client.vm";

		String text = null;
		try {
			// create email text using the template
			text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, emailTemplate, "UTF-8", model);
		} catch (VelocityException e) {
			LOGGER.error("Error in building email text", e);
		}

		return text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#findMaxRevisionNumberForClient
	 * (java.lang.String, long, java.lang.String)
	 */
	public long findMaxRevisionNumberForClient(String profileType, long revisionNumber, String servicePlaceId) {

		return clientDAO.findMaxRevisionNumberForClient(profileType, revisionNumber, servicePlaceId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#findClientTypeById(long)
	 */
	public ClientType findClientTypeById(long id) {

		return clientDAO.findClientTypeById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#findClientExistForServicePoint
	 * (long)
	 */
	public boolean findClientExistForServicePoint(long servicePointId) {

		return clientDAO.findClientExistForServicePoint(servicePointId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#findCustomerByCustomerId(java.
	 * lang.String)
	 */
	public CustomerRegistration findCustomerByCustomerId(String customerId) {

		return clientDAO.findCustomerByCustomerId(customerId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#editCustomer(com.sourcetrace.eses
	 * .order.entity.txn.CustomerRegistration)
	 */
	public void editCustomer(CustomerRegistration customer) {

		clientDAO.update(customer);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#addCustomer(com.sourcetrace.eses
	 * .order.entity.txn.CustomerRegistration)
	 */
	public void addCustomer(CustomerRegistration customer) {

		clientDAO.save(customer);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#listCustomers()
	 */
	public List<CustomerRegistration> listCustomers() {

		return clientDAO.listCustomers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#findCustomerById(java.lang.Long)
	 */
	public CustomerRegistration findCustomerById(Long id) {

		return clientDAO.findCustomerById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#listOfCustomers()
	 */
	public List<Customer> listOfCustomers() {

		return clientDAO.listOfCustomers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#findCustomerById(long)
	 */
	public Customer findCustomer(long id) {

		return clientDAO.findCustomer(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#addCustomer(com.ese.entity.profile
	 * .Customer)
	 */
	public void addCustomer(Customer customer) {

		customer.setRevisionNo(DateUtil.getRevisionNumber());
		clientDAO.save(customer);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#editCustomer(com.ese.entity.
	 * profile.Customer)
	 */
	public void editCustomer(Customer customer) {

		customer.setRevisionNo(DateUtil.getRevisionNumber());
		clientDAO.update(customer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#removeCustomer(com.ese.entity.
	 * profile.Customer)
	 */
	public void removeCustomer(Customer customer) {

		clientDAO.delete(customer);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#findCustomerByCustomerId(java.lang
	 * .String)
	 */
	public Customer findCustomerById(String customerId) {

		return clientDAO.findCustomerById(customerId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#isCustomerWithMappedFarmer(long)
	 */
	public boolean isCustomerWithMappedFarmer(long id) {

		return clientDAO.isCustomerWithMappedFarmer(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#isCustomerWithMappedFieldStaff(
	 * long)
	 */
	public boolean isCustomerWithMappedFieldStaff(long id) {

		return clientDAO.isCustomerWithMappedFieldStaff(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#findCustomerProjectByCodeOfProject
	 * (java.lang.String)
	 */
	public CustomerProject findCustomerProjectByCodeOfProject(String code) {

		return clientDAO.findCustomerProjectByCodeOfProject(code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#listOfCustomerProject(java.lang.
	 * String)
	 */
	public List<CustomerProject> listOfCustomerProject(String customer) {

		return clientDAO.listOfCustomerProject(customer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#addCustomerProject(com.ese.entity.
	 * profile.CustomerProject )
	 */
	public void addCustomerProject(CustomerProject customerProject) {

		long revisionNo = DateUtil.getRevisionNumber();
		customerProject.setRevisionNo(revisionNo);
		if (!ObjectUtil.isEmpty(customerProject.getCustomer()))
			customerProject.getCustomer().setRevisionNo(revisionNo);
		clientDAO.save(customerProject);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#findCustomerProjectById(long)
	 */
	public CustomerProject findCustomerProjectById(long id) {

		return clientDAO.findCustomerProjectById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.ese.service.profile.IClientService#removeCustomerProject(com.ese.
	 * entity.profile. CustomerProject)
	 */
	public void removeCustomerProject(CustomerProject customerProject) {

		long revisionNo = DateUtil.getRevisionNumber();
		if (!ObjectUtil.isEmpty(customerProject.getCustomer()))
			customerProject.getCustomer().setRevisionNo(revisionNo);
		clientDAO.delete(customerProject);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#editCustomerProject(com.ese.entity
	 * .profile.CustomerProject )
	 */
	public void editCustomerProject(CustomerProject customerProject) {

		long revisionNo = DateUtil.getRevisionNumber();
		customerProject.setRevisionNo(revisionNo);
		if (!ObjectUtil.isEmpty(customerProject.getCustomer()))
			customerProject.getCustomer().setRevisionNo(revisionNo);
		clientDAO.update(customerProject);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#isProjectWithMappedFieldStaff(
	 * long)
	 */
	public boolean isProjectWithMappedFieldStaff(long id) {

		return clientDAO.isProjectWithMappedFieldStaff(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#isProjectWithMappedFarmer(long)
	 */
	public boolean isProjectWithMappedFarmer(long id) {

		return clientDAO.isProjectWithMappedFarmer(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#removeCustomerProjectSet(java.util
	 * .Set)
	 */
	public void removeCustomerProjectSet(Set<CustomerProject> customerProjects) {

		clientDAO.delete(customerProjects);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#
	 * removeCustomerProjectwithAgentMapping(long)
	 */
	public void removeCustomerProjectwithAgentMapping(long agentId) {

		clientDAO.removeCustomerProjectwithAgentMapping(agentId);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#listOfCustomerProjectByCustomerId(
	 * java.lang.Long)
	 */
	public List<CustomerProject> listOfCustomerProjectByCustomerId(long id) {

		return clientDAO.listOfCustomerProjectByCustomerId(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#listCustomerByRevNo(java.lang.
	 * Long)
	 */
	public List<Customer> listCustomerByRevNo(Long revNo) {

		return clientDAO.listCustomerByRevNo(revNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.profile.IClientService#
	 * findCustomerProjectLatestRevisionNo()
	 */
	public Long findCustomerProjectLatestRevisionNo() {

		return clientDAO.findCustomerProjectLatestRevisionNo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.profile.IClientService#findCustomerLatestRevisionNo()
	 */
	public Long findCustomerLatestRevisionNo() {

		return clientDAO.findCustomerLatestRevisionNo();
	}

	public BranchMaster findBranchMasterById(Long id) {
		return clientDAO.findBranchMasterById(id);
	}

	public void addBranchMaster(BranchMaster bm) {
		clientDAO.save(bm);
		ESEAccount account = new ESEAccount();
		account.setAccountNo(idGenerator.createOrganizationAccountNoSequence());
		account.setType(ESEAccount.ORGANIZATION_ACCOUNT);
		account.setAccountType(ESEAccount.COMPANY_ACCOUNT);
		account.setStatus(ESEAccount.ACTIVE);
		account.setBranchId(bm.getBranchId());
		account.setBalance(0.0);
		account.setDistributionBalance(0.0);
		account.setSavingAmount(0.0);
		account.setShareAmount(0.0);
		account.setAccountOpenDate(new Date());
		account.setCreateTime(new Date());
		account.setProfileId(bm.getBranchId());
		account.setCashBalance(bm.getAccountBalance());
		clientDAO.save(account);
	}

	public void editBranchMaster(BranchMaster bm) {
		clientDAO.update(bm);
	}

	public void removeBranchMaster(BranchMaster bm) {
		clientDAO.delete(bm);
	}

	public BranchMaster findBranchMasterByBranchId(String branchId) {
		return clientDAO.findBranchMasterByBranchId(branchId);
	}

	public BranchMaster findBranchMasterByName(String name) {
		return clientDAO.findBranchMasterByName(name);
	}

	public byte[] findLogoByCode(String code) {
		return clientDAO.findLogoByCode(code);
	}

	@Override
	public List<BranchMaster> listBranchMasters() {
		// TODO Auto-generated method stub
		return clientDAO.listBranchMasters();
	}

	@Override
	public List<Object[]> listBranchMastersInfo() {
		// TODO Auto-generated method stub
		return clientDAO.listBranchMastersInfo();
	}

	@Override
	public Object[] findBranchInfo(String branchId) {
		// TODO Auto-generated method stub
		return clientDAO.findBranchInfo(branchId);
	}

	public List<BranchMaster> listBranchMastersAndDisableFilter() {
		// TODO Auto-generated method stub
		return clientDAO.listBranchMastersAndDisableFilter();
	}

	public BranchMaster findBranchMasterByBranchIdAndDisableFilter(String branchId) {
		return clientDAO.findBranchMasterByBranchIdAndDisableFilter(branchId);
	}

	public BranchMaster findBranchMasterByIdAndDisableFilter(Long id) {
		return clientDAO.findBranchMasterByIdAndDisableFilter(id);
	}

	public Asset findAssetsByAssetCode(String code) {
		return clientDAO.findAssetsByAssetCode(code);
	}

	public void updateAsset(Asset existing) {
		clientDAO.update(existing);
	}

	public void addMasterData(MasterData masterData) {
		clientDAO.save(masterData);
		// default pref type 3
		masterData.setCode(MasterData.CODE_PREFIX + String.format("%03d", masterData.getId()));
		clientDAO.update(masterData);
	}

	public MasterData findMasterDataById(Long id) {
		return clientDAO.findMasterDataById(id);
	}

	public void editMasterData(MasterData masterData) {
		clientDAO.update(masterData);
	}

	@Override
	public String findLocaleProperty(String code, String languageCode) {
		// TODO Auto-generated method stub
		return clientDAO.findLocaleProperty(code, languageCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.service.IClientService#listLanguages()
	 */
	@Override
	public List<Language> listLanguages() {

		// TODO Auto-generated method stub
		return clientDAO.listLanguages();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.service.IClientService#findLanguageByCode(java.lang.
	 * String)
	 */
	@Override
	public Language findLanguageByCode(String code) {

		// TODO Auto-generated method stub
		return clientDAO.findLanguageByCode(code);
	}

	public String findCurrentSeasonCode() {
		HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
		Object branchObject = httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
		String currentBranch = ObjectUtil.isEmpty(branchObject) ? ESESystem.SYSTEM_ESE_NAME : branchObject.toString();
		ESESystem ese = preferncesService.findPrefernceByOrganisationId(currentBranch);
		String currentSeasonCode = "";
		if (!ObjectUtil.isEmpty(ese)) {
			ESESystem preference = systemDAO.findPrefernceById(String.valueOf(ese.getId()));
			currentSeasonCode = preference.getPreferences().get(ESESystem.CURRENT_SEASON_CODE);

			if (StringUtil.isEmpty(currentSeasonCode)) {
				currentSeasonCode = "";
			}
		}
		return currentSeasonCode;
	}

	@Override
	public String findCurrentSeasonName() {

		HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
		Object branchObject = httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
		String currentBranch = ObjectUtil.isEmpty(branchObject) ? ESESystem.SYSTEM_ESE_NAME : branchObject.toString();
		ESESystem ese = preferncesService.findPrefernceByOrganisationId(currentBranch);
		if (!ObjectUtil.isEmpty(ese)) {
			ESESystem preference = systemDAO.findPrefernceById(String.valueOf(ese.getId()));
			String currentSeasonCode = preference.getPreferences().get(ESESystem.CURRENT_SEASON_CODE);
			if (!StringUtil.isEmpty(currentSeasonCode)) {
				String currentSeason = clientDAO.findCurrentSeasonName(currentSeasonCode);
				return currentSeason;
			}
		}
		return "";

	}

	@Override
	public HarvestSeason findSeasonByCode(String code) {
		return clientDAO.findSeasonByCode(code);
	}

	@Override
	public HarvestSeason findSeasonByCodeByTenant(String code, String tenantId) {
		return clientDAO.findSeasonByCodeByTenant(code, tenantId);
	}

	@Override
	public String findCurrentSeasonCodeByBranchId(String branchId) {
		String currentBranch = ObjectUtil.isEmpty(branchId) ? ESESystem.SYSTEM_ESE_NAME : branchId.toString();
		ESESystem ese = preferncesService.findPrefernceByOrganisationId(currentBranch);
		String currentSeasonCode = "";
		if (!ObjectUtil.isEmpty(ese)) {
			ESESystem preference = systemDAO.findPrefernceById(String.valueOf(ese.getId()));
			currentSeasonCode = preference.getPreferences().get(ESESystem.CURRENT_SEASON_CODE);

			if (StringUtil.isEmpty(currentSeasonCode)) {
				currentSeasonCode = "";
			}
		}
		return currentSeasonCode;

	}

	@Override
	public PeriodicInspection findPeriodicInspectionById(Long id) {

		return clientDAO.findPeriodicInspectionById(id);
	}

	@Override
	public List<Object[]> findParentBranches() {
		return clientDAO.findParentBranches();
	}

	@Override
	public List<Object[]> findChildBranches() {
		return clientDAO.findChildBranches();
	}

	@Override
	public List<BranchMaster> listChildBranchIds(String parentBranch) {
		return clientDAO.listChildBranchIds(parentBranch);
	}

	@Override
	public List<Object[]> listParentBranchMastersInfo() {
		return clientDAO.listParentBranchMastersInfo();
	}

	public Integer isParentBranch(String loggedUserBranch) {
		return clientDAO.isParentBranch(loggedUserBranch);
	}

	@Override
	public SensitizingImg findSensitizingImgById(Long id) {
		return clientDAO.findSensitizingImgById(id);
	}

	@Override
	public Vendor findVendorById(String id) {
		// TODO Auto-generated method stub
		return clientDAO.findVendorById(id);
	}

	@Override
	public DynamicReportConfig findReportByName(String name) {
		return clientDAO.findReportByName(name);
	}

	@Override
	public void addLocaleProperty(String componentName, String defaultValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<LocaleProperty> listLocalePropByLang(String lang) {
		// TODO Auto-generated method stub
		return clientDAO.listLocalePropByLang(lang);
	}

	@Override
	public List<Object> getMappedValues(Map criteriaMap, String refId) {
		return clientDAO.getMappedValues(criteriaMap, refId);
	}


	@Override
	public MasterData findMasterDataByCode(String code) {
		return clientDAO.findMasterDataByCode(code);
	}

	@Override
	public List<PeriodicInspection> findPeriodicInspectionByFarmerId(String farmerId) {
		// TODO Auto-generated method stub
		return clientDAO.findPeriodicInspectionByFarmerId(farmerId);
	}

	@Override
	public List<String> findBranchList() {
		// TODO Auto-generated method stub
		return clientDAO.findBranchList();
	}

	@Override
	public void saveFarmerDynmaicList(List<FarmerDynamicFieldsValue> farmerDynamicFieldValuesList) {
		clientDAO.saveFarmerDynmaicList(farmerDynamicFieldValuesList);
	}

	public void updateFarmerDynmaicList(List<FarmerDynamicFieldsValue> farmerDynamicFieldValuesList, Long id) {
		clientDAO.updateFarmerDynmaicList(farmerDynamicFieldValuesList, id);
	}

	@Override
	public List<Object[]> listMaxTypeByRefId(String refId) {
		return clientDAO.listMaxTypeByRefId(refId);
	}

	@Override
	public List<Object[]> listMaxTypeByFarmerId(Long farmerId) {
		return clientDAO.listMaxTypeByFarmerId(farmerId);
	}

	@Override
	public Object findProcurementTraceabilityImgById(Long id) {
		// TODO Auto-generated method stub
		return clientDAO.findProcurementTraceabilityImgById(id);
	}

	public Long findProductlatestRevisionNo() {
		return clientDAO.findProductlatestRevisionNo();
	}

	public Long findSeasonlatestRevisionNo() {
		return clientDAO.findSeasonlatestRevisionNo();
	}

	public Long findLocationlatestRevisionNo() {

		return clientDAO.findLocationlatestRevisionNo();
	}

	public Long findWarouseStocklatestRevisionNo() {

		return clientDAO.findWarouseStocklatestRevisionNo();
	}

	public Long findVillageWarehouselatestRevisionNo() {

		return clientDAO.findVillageWarehouselatestRevisionNo();
	}

	public Long findBuyerlatestRevisionNo() {

		return clientDAO.findBuyerlatestRevisionNo();
	}

	public Long findCataloguelatestRevisionNo() {

		return clientDAO.findCataloguelatestRevisionNo();
	}

	public Long findCooperativelatestRevisionNo() {
		return clientDAO.findCooperativelatestRevisionNo();
	}

	public Long findSupplierlatestRevisionNo() {

		return clientDAO.findSupplierlatestRevisionNo();
	}

	public Long findFarmerOutstandinglatestRevisionNo() {

		return clientDAO.findFarmerOutstandinglatestRevisionNo();
	}

	public String findProcurementProductlatestRevisionNo() {
		return clientDAO.findProcurementProductlatestRevisionNo();
	}

	public Long findfarmerDistributionBalancelatestRevisionNo() {

		return clientDAO.findfarmerDistributionBalancelatestRevisionNo();
	}

	public Long finddynamicComponentlatestRevisionNo() {

		return clientDAO.finddynamicComponentlatestRevisionNo();
	}
	@Override
	public List<Object[]> getMappedValuesAll(Map criteriaMap, List<Long> isList) {
		return clientDAO.getMappedValuesAll(criteriaMap,isList);
	}
	
	@Override
	public DynamicReportConfig findReportById(String name) {
		return clientDAO.findReportById(name);
	}

	@Override
	public void addDynamicReportConfig(DynamicReportConfig dynamicReportConfig) {
		clientDAO.save(dynamicReportConfig);
		
	}

	@Override
	public List<Object[]> listCustomerIdAndName() {
		// TODO Auto-generated method stub
		return clientDAO.listCustomerIdAndName();
	}

	@Override
	public String findReportByMainGridId(String id) {
		// TODO Auto-generated method stub
		return clientDAO.findReportByMainGridId(id);
	}


	
}
