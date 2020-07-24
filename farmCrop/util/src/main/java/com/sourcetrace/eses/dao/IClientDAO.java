/*
 * IClientDAO.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.List;
import java.util.Map;

import com.ese.entity.traceability.ProcurementTraceability;
import com.ese.entity.util.Language;
import com.ese.entity.util.LocaleProperty;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.order.entity.txn.CustomerRegistration;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfig;
import com.sourcetrace.eses.order.entity.txn.PeriodicInspection;
import com.sourcetrace.eses.order.entity.txn.SensitizingImg;
import com.sourcetrace.eses.util.entity.Image;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Client;
import com.sourcetrace.esesw.entity.profile.ClientType;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.CustomerProject;
import com.sourcetrace.esesw.entity.profile.Deposit;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicFieldsValue;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.Loan;
import com.sourcetrace.esesw.entity.profile.Vendor;

/**
 * The Interface IClientDAO.
 */
public interface IClientDAO extends IESEDAO {
	/**
	 * Find client by client id.
	 * 
	 * @param clientId
	 *            the client id
	 * @return the client
	 */
	public Client findClientByClientId(String clientId);

	/**
	 * Find client by id.
	 * 
	 * @param id
	 *            the id
	 * @return the client
	 */
	public Client findClientById(long id);

	/**
	 * Find by client id.
	 * 
	 * @param client
	 *            the client
	 * @return the client
	 */
	public Client findByClientId(String client);

	/**
	 * Find by client id.
	 * 
	 * @param client
	 *            the client
	 * @return the client
	 */
	public Client findByClientId(long client);

	/**
	 * Find by personal id.
	 * 
	 * @param idType
	 *            the id type
	 * @param idNumber
	 *            the id number
	 * @return the client
	 */
	public Client findByPersonalId(String idType, String idNumber);

	/**
	 * Find by temp client id.
	 * 
	 * @param client
	 *            the client
	 * @return the client
	 */
	public Client findByTempClientId(String client);

	/**
	 * Find client.
	 * 
	 * @param client
	 *            the client
	 * @return the client
	 */
	public Client findClient(long client);

	/**
	 * List clients.
	 * 
	 * @return the list< client>
	 */
	public List<Client> listClients();

	/**
	 * Find by image id.
	 * 
	 * @param imageId
	 *            the image id
	 * @return the image
	 */
	public Image findByImageId(String imageId);

	/**
	 * Find loan by loan number.
	 * 
	 * @param number
	 *            the number
	 * @return the loan
	 */

	public Loan findLoanByLoanNumber(String number);

	/**
	 * Find deposit by voucher number.
	 * 
	 * @param number
	 *            the number
	 * @return the deposit
	 */
	public Deposit findDepositByVoucherNumber(String number);

	/**
	 * Find pinolera by pinolera number.
	 * 
	 * @param number
	 *            the number
	 * @return the pinolera
	 */

	// public Pinolera findPinoleraByPinoleraNumber(String number);

	/**
	 * List loans.
	 * 
	 * @param type
	 *            the type
	 * @return the list< loan>
	 */
	public List<Loan> listLoans(String type);

	/**
	 * List deposits.
	 * 
	 * @return the list< deposit>
	 */
	public List<Deposit> listDeposits();

	/**
	 * List pinoleras.
	 * 
	 * @return the list< pinolera>
	 */
	// public List<Pinolera> listPinoleras();

	/**
	 * List new loans.
	 * 
	 * @return the list< loan>
	 */
	public List<Loan> listNewLoans();

	/**
	 * Find client by enroll id.
	 * 
	 * @param enrollId
	 *            the enroll id
	 * @param revisionNumber
	 *            the revision number
	 * @return the list< client>
	 */
	public List<Client> findClientByEnrollId(String enrollId, long revisionNumber);

	/**
	 * List new deposits.
	 * 
	 * @return the list< deposit>
	 */
	public List<Deposit> listNewDeposits();

	/**
	 * List new pinoleras.
	 * 
	 * @return the list< pinolera>
	 */
	// public List<Pinolera> listNewPinoleras();

	/**
	 * List new clients.
	 * 
	 * @return the list< client>
	 */
	public List<Client> listNewClients();

	/**
	 * Find non client by enroll id.
	 * 
	 * @param enrollId
	 *            the enroll id
	 * @param revisionNumber
	 *            the revision number
	 * @return the list< client>
	 */
	public List<Client> findNonClientByEnrollId(String enrollId, long revisionNumber);

	/**
	 * Find max revision number.
	 * 
	 * @return the long
	 */
	public long findMaxRevisionNumber();

	/**
	 * Find loan by loan number and type.
	 * 
	 * @param loanNumber
	 *            the loan number
	 * @param type
	 *            the type
	 * @return the loan
	 */
	public Loan findLoanByLoanNumberAndType(String loanNumber, String type);

	/**
	 * Find loan by loan id.
	 * 
	 * @param loanId
	 *            the loan id
	 * @return the loan
	 */
	public Loan findLoanByLoanId(Long loanId);

	/**
	 * List client transactions by device.
	 * 
	 * @param deviceId
	 *            the device id
	 * @return the list< client transaction>
	 */
	// public List<ClientTransaction> listClientTransactionsByDevice(String
	// deviceId);

	/**
	 * Find max revision number for client download.
	 * 
	 * @param enrollId
	 *            the enroll id
	 * @param revisionNumber
	 *            the revision number
	 * @return the long
	 */
	public long findMaxRevisionNumberForClientDownload(String enrollId, long revisionNumber);

	/**
	 * Find client by enrollment station id.
	 * 
	 * @param enrollId
	 *            the enroll id
	 * @param type
	 *            the type
	 * @return the list< client>
	 */
	public List<Client> findClientByEnrollmentStationId(String enrollId, int type);

	/**
	 * Find last enrolled client.
	 * 
	 * @param enrolledStationId
	 *            the enrolled station id
	 * @param i
	 *            the i
	 * @return the long
	 */
	public long findLastEnrolledClient(String enrolledStationId, int i);

	/**
	 * List clients.
	 * 
	 * @param clientType
	 *            the client type
	 * @return the list< client>
	 */
	public List<Client> listClients(int clientType);

	/**
	 * Client list by revision.
	 * 
	 * @param clientType
	 *            the client type
	 * @param revisionNumber
	 *            the revision number
	 * @return the list< client>
	 */
	public List<Client> clientListByRevision(int clientType, long revisionNumber);

	/**
	 * Find max revision number for client.
	 * 
	 * @param clientType
	 *            the client type
	 * @param revisionNumber
	 *            the revision number
	 * @return the long
	 */
	public long findMaxRevisionNumberForClient(int clientType, long revisionNumber);

	/**
	 * Find max revision number for client.
	 * 
	 * @param profileType
	 *            the profile type
	 * @param revisionNumber
	 *            the revision number
	 * @param servicePlaceId
	 *            the service place id
	 * @return the long
	 */
	public long findMaxRevisionNumberForClient(String profileType, long revisionNumber, String servicePlaceId);

	/**
	 * Find client type by id.
	 * 
	 * @param id
	 *            the id
	 * @return the client type
	 */
	public ClientType findClientTypeById(long id);

	/**
	 * Find client exist for service point.
	 * 
	 * @param id
	 *            the id
	 * @return true, if successful
	 */
	public boolean findClientExistForServicePoint(long id);

	/**
	 * Checks if is city mappingexist.
	 * 
	 * @param id
	 *            the id
	 * @return true, if is city mappingexist
	 */
	public boolean isCityMappingexist(long id);

	/**
	 * Find customer by customer id.
	 * 
	 * @param customerId
	 *            the customer id
	 * @return the customer registration
	 */
	public CustomerRegistration findCustomerByCustomerId(String customerId);

	/**
	 * List customers.
	 * 
	 * @return the list< customer registration>
	 */
	public List<CustomerRegistration> listCustomers();

	/**
	 * Find customer by id.
	 * 
	 * @param id
	 *            the id
	 * @return the customer registration
	 */
	public CustomerRegistration findCustomerById(Long id);

	/**
	 * List of customers.
	 * 
	 * @return the list< customer>
	 */
	public List<Customer> listOfCustomers();

	/**
	 * Find customer.
	 * 
	 * @param id
	 *            the id
	 * @return the customer
	 */
	public Customer findCustomer(long id);

	/**
	 * Find customer by id.
	 * 
	 * @param customerId
	 *            the customer id
	 * @return the customer
	 */
	public Customer findCustomerById(String customerId);

	/**
	 * Checks if is customer with mapped farmer.
	 * 
	 * @param id
	 *            the id
	 * @return true, if is customer with mapped farmer
	 */
	public boolean isCustomerWithMappedFarmer(long id);

	/**
	 * Checks if is customer with mapped field staff.
	 * 
	 * @param id
	 *            the id
	 * @return true, if is customer with mapped field staff
	 */
	public boolean isCustomerWithMappedFieldStaff(long id);

	/**
	 * Find customer project by code of project.
	 * 
	 * @param code
	 *            the code
	 * @return the customer project
	 */
	public CustomerProject findCustomerProjectByCodeOfProject(String code);

	/**
	 * List of customer project.
	 * 
	 * @param customer
	 *            the customer
	 * @return the list< customer project>
	 */
	public List<CustomerProject> listOfCustomerProject(String customer);

	/**
	 * Find customer project by id.
	 * 
	 * @param id
	 *            the id
	 * @return the customer project
	 */
	public CustomerProject findCustomerProjectById(long id);

	/**
	 * Checks if is project with mapped field staff.
	 * 
	 * @param id
	 *            the id
	 * @return true, if is project with mapped field staff
	 */
	public boolean isProjectWithMappedFieldStaff(long id);

	/**
	 * Checks if is project with mapped farmer.
	 * 
	 * @param id
	 *            the id
	 * @return true, if is project with mapped farmer
	 */
	public boolean isProjectWithMappedFarmer(long id);

	/**
	 * Removes the customer projectwith agent mapping.
	 * 
	 * @param agentId
	 *            the agent id
	 */
	public void removeCustomerProjectwithAgentMapping(long agentId);

	/**
	 * List of customer project by customer id.
	 * 
	 * @param id
	 *            the id
	 * @return the list< customer project>
	 */
	public List<CustomerProject> listOfCustomerProjectByCustomerId(long id);

	/**
	 * List customer by rev no.
	 * 
	 * @param revNo
	 *            the rev no
	 * @return the list< customer>
	 */
	public List<Customer> listCustomerByRevNo(Long revNo);

	/**
	 * Find customer project latest revision no.
	 * 
	 * @return the long
	 */
	public Long findCustomerProjectLatestRevisionNo();

	public Long findCustomerLatestRevisionNo();

	public BranchMaster findBranchMasterById(Long id);

	public BranchMaster findBranchMasterByBranchId(String branchId);

	public BranchMaster findBranchMasterByName(String name);

	public byte[] findLogoByCode(String code);

	public List<BranchMaster> listBranchMasters();

	public List<Object[]> listBranchMastersInfo();

	public Object[] findBranchInfo(String branchId);

	public List<BranchMaster> listBranchMastersAndDisableFilter();

	public BranchMaster findBranchMasterByBranchIdAndDisableFilter(String branchId);

	public BranchMaster findBranchMasterByIdAndDisableFilter(Long id);

	public Asset findAssetsByAssetCode(String code);

	public MasterData findMasterDataById(Long id);

	public String findLocaleProperty(String code, String languageCode);

	public List<Language> listLanguages();

	public Language findLanguageByCode(String code);

	/* public String findCurrentSeasonCode(); */

	public String findCurrentSeasonName(String seasonCode);

	public HarvestSeason findSeasonByCode(String code);

	public HarvestSeason findSeasonByCodeByTenant(String code, String tenantId);

	public PeriodicInspection findPeriodicInspectionById(Long id);

	public List<BranchMaster> listChildBranchIds(String parentBranch);

	public List<Object[]> findParentBranches();

	public List<Object[]> findChildBranches();

	public List<Object[]> listParentBranchMastersInfo();

	public Integer isParentBranch(String loggedUserBranch);

	public SensitizingImg findSensitizingImgById(Long id);

	public Vendor findVendorById(String id);

	public DynamicReportConfig findReportByName(String name);

	public List<LocaleProperty> listLocalePropByLang(String lang);

	public List<Object> getMappedValues(Map criteriaMap, String refId);

	public MasterData findMasterDataByCode(String code);

	public List<PeriodicInspection> findPeriodicInspectionByFarmerId(String farmerId);

	public List<String> findBranchList();

	public void saveFarmerDynmaicList(List<FarmerDynamicFieldsValue> farmerDynamicFieldValuesList);

	public void updateFarmerDynmaicList(List<FarmerDynamicFieldsValue> farmerDynamicFieldValuesList, Long id);

	public List<Object[]> listMaxTypeByRefId(String refId);

	public List<Object[]> listMaxTypeByFarmerId(Long farmerId);
	
	public Object findProcurementTraceabilityImgById(Long id);
	
	public Long findProductlatestRevisionNo();

	public Long findSeasonlatestRevisionNo();

	public Long findLocationlatestRevisionNo();

	public Long findWarouseStocklatestRevisionNo();

	public Long findVillageWarehouselatestRevisionNo();

	public Long findFarmerOutstandinglatestRevisionNo();

	public String findProcurementProductlatestRevisionNo();

	public Long findBuyerlatestRevisionNo();

	public Long findCataloguelatestRevisionNo();

	public Long findCooperativelatestRevisionNo();

	public Long findSupplierlatestRevisionNo();

	public Long finddynamicComponentlatestRevisionNo();

	public Long findfarmerDistributionBalancelatestRevisionNo();

	public List<Object[]> getMappedValuesAll(Map criteriaMap, List<Long> isList);

	public DynamicReportConfig findReportById(String id);

	public List<Object[]> listCustomerIdAndName();

	public String findReportByMainGridId(String id);

	

}
