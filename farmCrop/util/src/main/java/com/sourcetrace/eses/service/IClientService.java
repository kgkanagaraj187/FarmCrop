/*
 * IClientService.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.sourcetrace.esesw.entity.profile.Currency;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.CustomerProject;
import com.sourcetrace.esesw.entity.profile.Deposit;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicFieldsValue;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.Loan;
import com.sourcetrace.esesw.entity.profile.Vendor;

/**
 * The Interface IClientService.
 * 
 * @author $Author: aravind $
 * @version $Rev: 315 $, $Date: 2009-10-08 10:02:48 +0530 (Thu, 08 Oct 2009) $
 */
public interface IClientService {
	public static final String ENTITY ="ENTITY";
	public static final String CRITERIA_ALIAS ="CRITERIA_ALIAS";
	public static final String PROJECTIONS_LIST ="PROJECTIONS_LIST";
	public static final String PROJECTIONS_GROUP = "PROJECTIONS_GROUP";
	public static final String PROJECTIONS_CONDITION = "PROJECTIONS_CONDITION";
	
	public List<Client> listClients();

	public void addClient(Client Client);

	public void editClient(Client Client);

	public void editClientTxnCredential(Client client);

	public void removeClient(Client Client);

	public Client findClient(String clientId);

	public Client findClientByTempClientId(String clientId);

	public Image findImage(String imageId);

	public void saveLoan(Map<String, String[]> parameters);

	public Loan findLoan(String loanNumber);

	public void saveDeposit(Map<String, String[]> parameters);

	public Deposit findDeposit(String voucherNumber);

	public void savePinolera(Map<String, String[]> parameters);

	// public Pinolera findPinolera(String pinoleraNumber);

	public Set<Loan> listLoan(String clientId);

	public Set<Deposit> listDeposit(String clientId);

	public List<Loan> listLoans(String type);

	public List<Deposit> listDeposits();

	// public List<Pinolera> listPinoleras();

	// public Set<Pinolera> listPinolera(String clientId);

	public List<Client> findClientByEnrollId(String enrollId, long revisionNumber);

	public byte[] convertImageByte(Image image);

	public List<Client> findNonClientByEnrollId(String enrolledStationId, long revisionNumber);

	public Client findClient(long id);

	public Loan findLoanByLoanNumAndType(String loanNumber, String type);

	public Loan findLoanById(Long loanId);

	void updatePinolera(Map<String, String[]> parameters);

	public String findTransactionType(int id);

	// public List<ClientTransaction> listClientTransactionsByDevice(String
	// deviceId);

	public List<Currency> listCurrency();

	public long findMaxRevisionNumberForClientDownload(String enrollId, long revisionNumber);

	public void saveClient(Map<String, String[]> parameters);

	public void editBiometric(Client client);

	public Client findByPersonalId(String identityType, String identityNumber);

	public Client findLastEnrolledClient(String enrolledStationId, int i);

	public Client findClientByClientId(String clientId);

	public Client findClientById(long id);

	public String getEmailMessage(Client client);

	public long findMaxRevisionNumberForClient(String profileType, long revisionNumber, String servicePlaceId);

	public ClientType findClientTypeById(long id);

	public boolean findClientExistForServicePoint(long id);

	public CustomerRegistration findCustomerByCustomerId(String customerId);

	public void editCustomer(CustomerRegistration customer);

	public void addCustomer(CustomerRegistration customer);

	public List<CustomerRegistration> listCustomers();

	public CustomerRegistration findCustomerById(Long id);

	public List<Customer> listOfCustomers();

	public Customer findCustomerById(String customerId);

	public Customer findCustomer(long id);

	public void addCustomer(Customer customer);

	public void editCustomer(Customer customer);

	public void removeCustomer(Customer customer);

	public boolean isCustomerWithMappedFarmer(long id);

	public boolean isCustomerWithMappedFieldStaff(long id);

	public CustomerProject findCustomerProjectByCodeOfProject(String code);

	public List<CustomerProject> listOfCustomerProject(String customer);

	public void addCustomerProject(CustomerProject customerProject);

	public CustomerProject findCustomerProjectById(long id);

	public void removeCustomerProject(CustomerProject customerProject);

	public void editCustomerProject(CustomerProject customerProject);

	public boolean isProjectWithMappedFieldStaff(long id);

	public boolean isProjectWithMappedFarmer(long id);

	public void removeCustomerProjectSet(Set<CustomerProject> customerProjects);

	public void removeCustomerProjectwithAgentMapping(long agentId);

	public List<CustomerProject> listOfCustomerProjectByCustomerId(long id);

	public List<Customer> listCustomerByRevNo(Long revNo);

	public Long findCustomerProjectLatestRevisionNo();

	public Long findCustomerLatestRevisionNo();

	public BranchMaster findBranchMasterById(Long id);

	public void addBranchMaster(BranchMaster bm);

	public void editBranchMaster(BranchMaster bm);

	public void removeBranchMaster(BranchMaster bm);

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

	public void updateAsset(Asset existing);

	public void addMasterData(MasterData masterData);

	public MasterData findMasterDataById(Long id);

	public void editMasterData(MasterData masterData);
	
	public String findLocaleProperty(String code, String languageCode);
	
	public List<Language> listLanguages();
	
	public Language findLanguageByCode(String code);
	
	public String findCurrentSeasonCode();
	
	public String findCurrentSeasonName();
	
	public HarvestSeason findSeasonByCode(String code);
	
	public HarvestSeason findSeasonByCodeByTenant(String code,String tenantId);

	public String findCurrentSeasonCodeByBranchId(String branchId);

    public PeriodicInspection findPeriodicInspectionById(Long id);
    
    public List<Object[]> findParentBranches();
    
    public List<Object[]> findChildBranches();
    
    public List<BranchMaster> listChildBranchIds(String parentBranch);

    public List<Object[]> listParentBranchMastersInfo();
    
    public Integer isParentBranch(String loggedUserBranch);
    
    public SensitizingImg findSensitizingImgById(Long id);

	public Vendor findVendorById(String id);
	
	 public DynamicReportConfig findReportByName(String name);

	public void addLocaleProperty(String componentName, String defaultValue);
	
	public List<LocaleProperty> listLocalePropByLang(String lang);
	
	public List<Object> getMappedValues(Map criteriaMap,String refId);
	
	public MasterData findMasterDataByCode(String code);

	public List<PeriodicInspection> findPeriodicInspectionByFarmerId(String farmerId);

	public List<String> findBranchList();
	
	public void saveFarmerDynmaicList(List<FarmerDynamicFieldsValue> farmerDynamicFieldValuesList);

	public void updateFarmerDynmaicList(List<FarmerDynamicFieldsValue> farmerDynamicFieldValuesList, Long id);
	
	public List<Object[]> listMaxTypeByRefId(String refId);
	
	public List<Object[]> listMaxTypeByFarmerId(Long farmerId);
	
	public Object findProcurementTraceabilityImgById(Long id);

	public java.lang.Object findProductlatestRevisionNo();

	public java.lang.Object findSeasonlatestRevisionNo();

	public java.lang.Object findLocationlatestRevisionNo();

	public java.lang.Object findWarouseStocklatestRevisionNo();

	public java.lang.Object findVillageWarehouselatestRevisionNo();

	public java.lang.Object findFarmerOutstandinglatestRevisionNo();

	public java.lang.Object findProcurementProductlatestRevisionNo();

	public java.lang.Object findBuyerlatestRevisionNo();

	public java.lang.Object findCataloguelatestRevisionNo();

	public java.lang.Object findCooperativelatestRevisionNo();

	public java.lang.Object findSupplierlatestRevisionNo();

	public java.lang.Object finddynamicComponentlatestRevisionNo();

	public java.lang.Object findfarmerDistributionBalancelatestRevisionNo();
	
	public List<Object[]> getMappedValuesAll(Map criteriaMap, List<Long> isList);
	public DynamicReportConfig findReportById(String id);

	public void addDynamicReportConfig(DynamicReportConfig dynamicReportConfig);
	
	public List<Object[]> listCustomerIdAndName();

	public String findReportByMainGridId(String id);


}
