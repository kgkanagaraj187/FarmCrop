
package com.sourcetrace.eses.service;

import java.math.BigInteger;
import java.net.URI;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ese.entity.profile.CropHarvestDetails;
import com.ese.entity.profile.CropSupply;
import com.ese.entity.profile.CropSupplyDetails;
import com.ese.entity.traceability.BaleGeneration;
import com.ese.entity.traceability.GinningProcess;
import com.ese.entity.traceability.HeapData;
import com.ese.entity.traceability.HeapDataDetail;
import com.ese.entity.traceability.LedgerData;
import com.ese.entity.traceability.ProcurementTraceability;
import com.ese.entity.traceability.ProcurementTraceabilityDetails;
import com.ese.entity.traceability.ProcurementTraceabilityStock;
import com.ese.entity.traceability.ProcurementTraceabilityStockDetails;
import com.ese.entity.traceability.SpinningTransfer;
import com.ese.entity.txn.mfi.InterestCalcConsolidated;
import com.ese.entity.util.ESESystem;
import com.ese.entity.util.LoanLedger;
import com.sourcetrace.eses.dao.IAccountDAO;
import com.sourcetrace.eses.dao.IAgentDAO;
import com.sourcetrace.eses.dao.IESESystemDAO;
import com.sourcetrace.eses.dao.IFarmerDAO;
import com.sourcetrace.eses.dao.ILocationDAO;
import com.sourcetrace.eses.dao.IProductDistributionDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.ColdStorage;
import com.sourcetrace.eses.entity.ColdStorageDetail;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.entity.WarehouseProduct;
import com.sourcetrace.eses.entity.WarehouseProductDetail;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.order.entity.profile.GradeMaster;
import com.sourcetrace.eses.order.entity.profile.GradeMasterPricing;
import com.sourcetrace.eses.order.entity.profile.SubCategory;
import com.sourcetrace.eses.order.entity.txn.AgentMovement;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.CityWarehouse;
import com.sourcetrace.eses.order.entity.txn.CityWarehouseDetail;
import com.sourcetrace.eses.order.entity.txn.ColdStorageStockTransfer;
import com.sourcetrace.eses.order.entity.txn.ColdStorageStockTransferDetail;
import com.sourcetrace.eses.order.entity.txn.DMT;
import com.sourcetrace.eses.order.entity.txn.DMTDetail;
import com.sourcetrace.eses.order.entity.txn.Distribution;
import com.sourcetrace.eses.order.entity.txn.DistributionDetail;
import com.sourcetrace.eses.order.entity.txn.DistributionStock;
import com.sourcetrace.eses.order.entity.txn.DistributionStockDetail;
import com.sourcetrace.eses.order.entity.txn.FarmCropCalendar;
import com.sourcetrace.eses.order.entity.txn.LoanDistribution;
import com.sourcetrace.eses.order.entity.txn.LoanDistributionDetail;
import com.sourcetrace.eses.order.entity.txn.MTNT;
import com.sourcetrace.eses.order.entity.txn.MTNTDetail;
import com.sourcetrace.eses.order.entity.txn.OfflineBaleGeneration;
import com.sourcetrace.eses.order.entity.txn.OfflineDistribution;
import com.sourcetrace.eses.order.entity.txn.OfflineFarmCropCalendar;
import com.sourcetrace.eses.order.entity.txn.OfflineGinningProcess;
import com.sourcetrace.eses.order.entity.txn.OfflineMTNT;
import com.sourcetrace.eses.order.entity.txn.OfflinePMTNR;
import com.sourcetrace.eses.order.entity.txn.OfflinePayment;
import com.sourcetrace.eses.order.entity.txn.OfflineProcurement;
import com.sourcetrace.eses.order.entity.txn.OfflineProcurementTraceability;
import com.sourcetrace.eses.order.entity.txn.OfflineProductReturn;
import com.sourcetrace.eses.order.entity.txn.OfflineSpinningTransfer;
import com.sourcetrace.eses.order.entity.txn.OfflineSupplierProcurement;
import com.sourcetrace.eses.order.entity.txn.PMT;
import com.sourcetrace.eses.order.entity.txn.PMTDetail;
import com.sourcetrace.eses.order.entity.txn.PMTFarmerDetail;
import com.sourcetrace.eses.order.entity.txn.PMTImageDetails;
import com.sourcetrace.eses.order.entity.txn.PaymentLedger;
import com.sourcetrace.eses.order.entity.txn.PaymentMode;
import com.sourcetrace.eses.order.entity.txn.PricePattern;
import com.sourcetrace.eses.order.entity.txn.PricePatternDetail;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.order.entity.txn.ProcurementDetail;
import com.sourcetrace.eses.order.entity.txn.ProductReturn;
import com.sourcetrace.eses.order.entity.txn.ProductReturnDetail;
import com.sourcetrace.eses.order.entity.txn.SupplierProcurement;
import com.sourcetrace.eses.order.entity.txn.SupplierProcurementDetail;
import com.sourcetrace.eses.order.entity.txn.SupplierWarehouse;
import com.sourcetrace.eses.order.entity.txn.TripSheet;
import com.sourcetrace.eses.order.entity.txn.TruckStock;
import com.sourcetrace.eses.order.entity.txn.TruckStockDetail;
import com.sourcetrace.eses.order.entity.txn.VillageWarehouse;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.profile.Product;
import com.sourcetrace.esesw.entity.profile.CropCalendar;
import com.sourcetrace.esesw.entity.profile.CropCalendarDetail;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.DistributionBalance;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.GradeMasterPricingHistory;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementGradePricingHistory;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.profile.Vendor;
import com.sourcetrace.esesw.entity.profile.WarehousePayment;
import com.sourcetrace.esesw.entity.profile.WarehousePaymentDetails;
import com.sourcetrace.esesw.entity.profile.WarehouseStockReturn;
import com.sourcetrace.esesw.entity.profile.WarehouseStockReturnDetails;
import com.sourcetrace.esesw.entity.txn.ESETxn;


// TODO: Auto-generated Javadoc
/**
 * The Class ProductDistributionService.
 */
@Service
@Transactional
public class ProductDistributionService implements IProductDistributionService {

	private static final Logger LOGGER = Logger.getLogger(ProductDistributionService.class);
	@Autowired
	private IProductDistributionDAO productDistributionDAO;
	@Autowired
	private IAgentDAO agentDAO;
	@Autowired
	private IAccountDAO accountDAO;
	@Autowired
	private IUniqueIDGenerator idGenerator;
	@Autowired
	private IFarmerDAO farmerDAO;
	@Autowired
	private ILocationDAO locationDAO;
	@Autowired
	private IESESystemDAO systemDAO;
	@Autowired
	private ILedgerService ledgerService;

	@Autowired
	private ICatalogueService catalogueService;

	/**
	 * Sets the product distribution dao.
	 * 
	 * @param productDistributionDAO
	 *            the new product distribution dao
	 */
	public void setProductDistributionDAO(IProductDistributionDAO productDistributionDAO) {

		this.productDistributionDAO = productDistributionDAO;
	}

	/**
	 * Gets the product distribution dao.
	 * 
	 * @return the product distribution dao
	 */
	public IProductDistributionDAO getProductDistributionDAO() {

		return productDistributionDAO;
	}

	/**
	 * Sets the id generator.
	 * 
	 * @param idGenerator
	 *            the new id generator
	 */
	public void setIdGenerator(IUniqueIDGenerator idGenerator) {

		this.idGenerator = idGenerator;
	}

	/**
	 * Gets the id generator.
	 * 
	 * @return the id generator
	 */
	public IUniqueIDGenerator getIdGenerator() {

		return idGenerator;
	}

	/**
	 * Sets the agent dao.
	 * 
	 * @param agentDAO
	 *            the new agent dao
	 */
	public void setAgentDAO(IAgentDAO agentDAO) {

		this.agentDAO = agentDAO;
	}

	/**
	 * Gets the agent dao.
	 * 
	 * @return the agent dao
	 */
	public IAgentDAO getAgentDAO() {

		return agentDAO;
	}

	/**
	 * Gets the account dao.
	 * 
	 * @return the account dao
	 */
	public IAccountDAO getAccountDAO() {

		return accountDAO;
	}

	/**
	 * Sets the account dao.
	 * 
	 * @param accountDAO
	 *            the new account dao
	 */
	public void setAccountDAO(IAccountDAO accountDAO) {

		this.accountDAO = accountDAO;
	}

	/**
	 * Gets the farmer dao.
	 * 
	 * @return the farmer dao
	 */
	public IFarmerDAO getFarmerDAO() {

		return farmerDAO;
	}

	/**
	 * Sets the farmer dao.
	 * 
	 * @param farmerDAO
	 *            the new farmer dao
	 */
	public void setFarmerDAO(IFarmerDAO farmerDAO) {

		this.farmerDAO = farmerDAO;
	}

	/**
	 * Gets the location dao.
	 * 
	 * @return the location dao
	 */
	public ILocationDAO getLocationDAO() {

		return locationDAO;
	}

	/**
	 * Sets the location dao.
	 * 
	 * @param locationDAO
	 *            the new location dao
	 */
	public void setLocationDAO(ILocationDAO locationDAO) {

		this.locationDAO = locationDAO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findDistributionByRecNo(java.lang.String)
	 */
	public Distribution findDistributionByRecNo(String receiptNo) {

		return productDistributionDAO.findDistributionByRecNo(receiptNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #addDistribution(com.sourcetrace.eses.order.entity.txn.Distribution)
	 */
	public void addDistribution(Distribution distribution) {

		productDistributionDAO.save(distribution);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #addDistributionDetail
	 * (com.sourcetrace.eses.order.entity.txn.DistributionDetail)
	 */
	public void addDistributionDetail(DistributionDetail detail) {

		productDistributionDAO.save(detail);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #listDistributionDetail(long)
	 */
	public List<DistributionDetail> listDistributionDetail(long id) {

		return productDistributionDAO.listDistributionDetail(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #listDistributionDetail(long, int, int)
	 */
	public List<DistributionDetail> listDistributionDetail(long id, int startIndex, int limit) {

		return productDistributionDAO.listDistributionDetail(id, startIndex, limit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findProcurementByRecNo(java.lang.String)
	 */
	public Procurement findProcurementByRecNo(String receiptNo) {

		return productDistributionDAO.findProcurementByRecNo(receiptNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findProcurementProductByCode(java.lang.String)
	 */
	public ProcurementProduct findProcurementProductByCode(String productCode) {

		return productDistributionDAO.findProcurementProductByCode(productCode);
	}

	public ILedgerService getLedgerService() {

		return ledgerService;
	}

	public void setLedgerService(ILedgerService ledgerService) {

		this.ledgerService = ledgerService;
	}

	public void addProcurement(Procurement procurement) {

		// processAgentTransaction(procurement.getAgroTransaction());
		// productDistributionDAO.save(procurement.getAgroTransaction().getRefAgroTransaction());
		ESEAccount intialBal = productDistributionDAO
				.findESEAccountByProfileId(procurement.getAgroTransaction().getFarmerId(), ESEAccount.CONTRACT_ACCOUNT);
		Double initBal = 0.0;
		if (!ObjectUtil.isEmpty(intialBal)) {
			initBal = intialBal.getCashBalance();
		}
		ESESystem eseSystem = systemDAO.findPrefernceById("1");
		if (eseSystem.getPreferences().get(ESESystem.ENABLE_LOAN_MODULE).equals("1")) {
			LoanLedger loanLedgerPro = new LoanLedger();
			loanLedgerPro.setTxnTime(procurement.getAgroTransaction().getTxnTime());
			loanLedgerPro.setFarmerId(!ObjectUtil.isEmpty(procurement.getFarmer()) ? procurement.getFarmer().getFarmerId() : "");
			loanLedgerPro.setActualAmount(procurement.getTotalProVal()); 
			loanLedgerPro.setLoanInterest(procurement.getLoanInterest());
			loanLedgerPro.setFinalPayAmt(procurement.getFinalPayAmt());
			loanLedgerPro.setLoanDesc(procurement.PROCURMEMENT);
			loanLedgerPro.setTxnType(procurement.TXN_TYPE);
			loanLedgerPro.setAccountNo(ObjectUtil.isEmpty(intialBal)?"":intialBal.getLoanAccountNo());
			loanLedgerPro.setPreFarmerBal(ObjectUtil.isEmpty(intialBal)?0.00:intialBal.getOutstandingLoanAmount());
			loanLedgerPro.setNewFarmerBal(ObjectUtil.isEmpty(intialBal)?0.00:intialBal.getLoanAmount());		
			loanLedgerPro.setBranchId(procurement.getBranchId());
			loanLedgerPro.setAccount(!ObjectUtil.isEmpty(intialBal) ? intialBal : null);
			loanLedgerPro.setReceiptNo(!ObjectUtil.isEmpty(procurement.getAgroTransaction()) ? procurement.getAgroTransaction().getReceiptNo() : "");
			ledgerService.save(loanLedgerPro);
		}

		AgroTransaction existingAgroTxn = procurement.getAgroTransaction();
		processTransaction(procurement);
		// productDistributionDAO.save(procurement.getAgroTransaction());
		procurement.setAgroTransaction(existingAgroTxn);
		procurement.getAgroTransaction().setTxnDesc(Procurement.PROCUREMENT_AMOUNT);
		Double bal;
		/*ESEAccount farmerAccount = productDistributionDAO
				.findESEAccountByProfileId(procurement.getAgroTransaction().getFarmerId(), ESEAccount.CONTRACT_ACCOUNT);*/
		if (!ObjectUtil.isEmpty(intialBal)) {
			bal = intialBal.getCashBalance();
			if (eseSystem.getPreferences().get(ESESystem.ENABLE_LOAN_MODULE).equals("1")) {				
				
				procurement.getAgroTransaction().setTxnDesc(Procurement.REPAYMENT_AMOUNT);
				procurement.getAgroTransaction().setIntBalance(bal + procurement.getTotalProVal());
				procurement.getAgroTransaction().setTxnAmount(procurement.getLoanAmount() + procurement.getPaymentAmount());
				procurement.getAgroTransaction().setBalAmount(bal + procurement.getFinalPayAmt() - procurement.getPaymentAmount());
				procurement.getAgroTransaction().setAccount(intialBal);
				procurement.getAgroTransaction().setCreditAmt(procurement.getFinalPayAmt() - procurement.getPaymentAmount());
				procurement.getAgroTransaction().setProcurement(procurement);
				procurement.getAgroTransaction().setIntialBalance(initBal);
				procurement.getAgroTransaction().setSeasonCode(procurement.getSeasonCode());
				intialBal.setCashBalance(bal + procurement.getFinalPayAmt() - procurement.getPaymentAmount());
				if(intialBal.getOutstandingLoanAmount()>=procurement.getLoanAmount()){
					intialBal.setOutstandingLoanAmount(intialBal.getOutstandingLoanAmount() - procurement.getLoanAmount());
				}
				
			}else{
				
				procurement.getAgroTransaction()
						.setBalAmount(bal + procurement.getTotalProVal() - procurement.getPaymentAmount());
				procurement.getAgroTransaction().setIntBalance(bal + procurement.getTotalProVal());
				procurement.getAgroTransaction().setTxnAmount(procurement.getPaymentAmount());
				procurement.getAgroTransaction().setAccount(intialBal);
				procurement.getAgroTransaction()
						.setCreditAmt(procurement.getTotalProVal() - procurement.getPaymentAmount());
				procurement.getAgroTransaction().setProcurement(procurement);
				procurement.getAgroTransaction().setIntialBalance(initBal);
				procurement.getAgroTransaction().setSeasonCode(procurement.getSeasonCode());
				intialBal.setCashBalance(bal + procurement.getTotalProVal() - procurement.getPaymentAmount());
			}			
			
			productDistributionDAO.update(intialBal);
		}

		productDistributionDAO.save(procurement);
		PaymentLedger ledger = new PaymentLedger();
		ledger.setCreatedDate(procurement.getAgroTransaction().getTxnTime());
		ledger.setCreatedUser(procurement.getAgroTransaction().getAgentName());
		ledger.setType(Procurement.TXN_TYPE);
		ledger.setRefId(String.valueOf(procurement.getId()));
		ledger.setPrevValue(procurement.getAgroTransaction().getIntBalance());
		ledger.setTxnValue(procurement.getAgroTransaction().getTxnAmount());
		ledger.setNewValue(procurement.getAgroTransaction().getBalAmount());
		ledgerService.save(ledger);
		
		if (eseSystem.getPreferences().get(ESESystem.ENABLE_LOAN_MODULE).equals("1")) {
			LoanLedger loanLedger = new LoanLedger();
			loanLedger.setTxnTime(procurement.getAgroTransaction().getTxnTime());
			loanLedger.setFarmerId(!ObjectUtil.isEmpty(procurement.getFarmer()) ? procurement.getFarmer().getFarmerId() : "");
			loanLedger.setActualAmount(procurement.getLoanAmount()); 
			loanLedger.setLoanInterest(procurement.getLoanInterest());
			loanLedger.setFinalPayAmt(procurement.getActualAmount());
			loanLedger.setLoanDesc(procurement.REPAYMENT_AMOUNT);
			loanLedger.setTxnType(procurement.REPAYMENT_TXN_TYPE);
			loanLedger.setAccountNo(ObjectUtil.isEmpty(intialBal)?"":intialBal.getLoanAccountNo());
			loanLedger.setPreFarmerBal(ObjectUtil.isEmpty(intialBal)?0.00:intialBal.getOutstandingLoanAmount());
			loanLedger.setNewFarmerBal(ObjectUtil.isEmpty(intialBal)?0.00:intialBal.getLoanAmount());		
			loanLedger.setBranchId(procurement.getBranchId());
			loanLedger.setAccount(!ObjectUtil.isEmpty(procurement.getAgroTransaction()) && !ObjectUtil.isEmpty(procurement.getAgroTransaction().getAccount()) ? procurement.getAgroTransaction().getAccount() : null);
			loanLedger.setReceiptNo(!ObjectUtil.isEmpty(procurement.getAgroTransaction()) ? procurement.getAgroTransaction().getReceiptNo() : "");
			ledgerService.save(loanLedger);
		}
		
		processCityWarehouse(procurement);
	}

	/**
	 * Process agent transaction.
	 * 
	 * @param agentTxn
	 *            the agent txn
	 */
	@SuppressWarnings("unused")
	private void processAgentTransaction(AgroTransaction agentTxn) {

		AgroTransaction farmerTxn = agentTxn.getRefAgroTransaction();
		if (!ObjectUtil.isEmpty(farmerTxn)) {
			ESEAccount agentAccount = accountDAO.findAccountByProfileIdAndProfileType(farmerTxn.getAgentId(),
					ESEAccount.AGENT_ACCOUNT);
			if (!ObjectUtil.isEmpty(agentAccount)) {
				agentTxn.setReceiptNo(farmerTxn.getReceiptNo());
				agentTxn.setAgentId(farmerTxn.getAgentId());
				agentTxn.setAgentName(farmerTxn.getAgentName());
				agentTxn.setDeviceId(farmerTxn.getDeviceId());
				agentTxn.setDeviceName(farmerTxn.getDeviceName());
				agentTxn.setServicePointId(farmerTxn.getServicePointId());
				agentTxn.setServicePointName(farmerTxn.getServicePointName());
				agentTxn.setFarmerId(farmerTxn.getFarmerId());
				agentTxn.setFarmerName(farmerTxn.getFarmerName());

				Agent agent = agentDAO.findAgentByProfileId(farmerTxn.getAgentId());
				agentTxn.setProfType(Profile.AGENT);
				// Setting profile type based on Agent type
				if (!ObjectUtil.isEmpty(agent) && agent.isCoOperativeManager()) {
					agentTxn.setProfType(Profile.CO_OPEARATIVE_MANAGER);
				}

				agentTxn.setTxnType(farmerTxn.getTxnType());
				agentTxn.setOperType(farmerTxn.getOperType());
				agentTxn.setTxnTime(farmerTxn.getTxnTime());
				agentTxn.setTxnDesc(farmerTxn.getTxnDesc());
				agentTxn.setSamithi(farmerTxn.getSamithi());
				// Transaction related Stuff
				agentTxn.setIntBalance(agentAccount.getBalance());
				agentTxn.setTxnAmount(farmerTxn.getTxnAmount());
				agentTxn.setBalAmount(agentTxn.getIntBalance() - agentTxn.getTxnAmount());
				// Updating Balance of Agent Account
				agentAccount.setBalance(agentTxn.getBalAmount());
				// Mapping Agent Account with Agro Transaction
				agentTxn.setAccount(agentAccount);
				// Updating Agent account
				// accountDAO.update(agentAccount);
				// accountDAO.save(agentTxn);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #addProcurementDetail
	 * (com.sourcetrace.eses.order.entity.txn.ProcurementDetail)
	 */
	public void addProcurementDetail(ProcurementDetail procurementDetail) {

		productDistributionDAO.save(procurementDetail);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findMTNTByReceiptNo(java.lang.String)
	 */
	public MTNT findMTNTByReceiptNo(String receiptNo) {

		return productDistributionDAO.findMTNTByReceiptNo(receiptNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #addMTNT(com.sourcetrace.eses.order.entity.txn.MTNT)
	 */
	public void addMTNT(MTNT mtnt) {

		productDistributionDAO.save(mtnt);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #addMtntDetail(com.sourcetrace.eses.order.entity.txn.MTNTDetail)
	 */
	public void addMtntDetail(MTNTDetail mtntDetail) {

		productDistributionDAO.save(mtntDetail);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #removeMTNT(com.sourcetrace.eses.order.entity.txn.MTNT)
	 */
	public void removeMTNT(MTNT mtnt) {

		productDistributionDAO.delete(mtnt);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #listMTNTDetailList(long)
	 */
	public List<MTNTDetail> listMTNTDetailList(long mtntId) {

		return productDistributionDAO.listMTNTDetailList(mtntId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #listMTNTDetailList(long, int, int)
	 */
	public List<MTNTDetail> listMTNTDetailList(long mtntId, int startIndex, int limit) {

		return productDistributionDAO.listMTNTDetailList(mtntId, startIndex, limit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #removeProcurement(com.sourcetrace.eses.order.entity.txn.Procurement)
	 */
	public void removeProcurement(Procurement procurement) {

		productDistributionDAO.delete(procurement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #removeDistribution(com.sourcetrace.eses.order.entity.txn.Distribution)
	 */
	public void removeDistribution(Distribution distribution) {

		productDistributionDAO.delete(distribution);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findVillageWarehouseByVillageCode(long)
	 */
	public List<VillageWarehouse> findVillageWarehouseByVillageCode(long id) {

		return productDistributionDAO.findVillageWarehouseByVillageCode(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findVillageWarehouseByVillageAndProduct(long, long)
	 */
	public VillageWarehouse findVillageWarehouseByVillageAndProduct(long villageId, long productId) {

		return productDistributionDAO.findVillageWarehouseByVillageAndProduct(villageId, productId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #removeMTNTDetail(long)
	 */
	public void removeMTNTDetail(long id) {

		productDistributionDAO.removeMTNTDetail(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #removeDistributionDetail(long)
	 */
	public void removeDistributionDetail(long id) {

		productDistributionDAO.removeDistributionDetail(id);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #removeProcurementDetail(long)
	 */
	public void removeProcurementDetail(long procurementId) {

		productDistributionDAO.removeProcurementDetail(procurementId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #addProcurementAndProcurementDetail
	 * (com.sourcetrace.eses.order.entity.txn.Procurement, java.util.List)
	 */
	public void addProcurementAndProcurementDetail(Procurement procurement, AgroTransaction agentPaymentTxn,
			AgroTransaction farmerPaymentTxn, boolean isPayment) {

		productDistributionDAO.save(procurement.getAgroTransaction());
		productDistributionDAO.save(procurement);
		if (isPayment) {
			productDistributionDAO.save(farmerPaymentTxn);
			productDistributionDAO.save(agentPaymentTxn);
		}
		updateVillageWarehouse(procurement, ESETxn.ON_LINE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #saveMTNTAndMTNTDetail(com.sourcetrace.eses.order.entity.txn.MTNT)
	 */
	public void saveMTNTAndMTNTDetail(MTNT mtnt) {

		/** SAVING MTNT OBJECT **/
		mtnt.setOperationType(ESETxn.ON_LINE);
		productDistributionDAO.save(mtnt);
		if (!StringUtil.isEmpty(mtnt.getTruckId()))
			/** CALLING METHOD TO SAVE MTNT AND MTNR IN TRANSIT OBJECT **/
			saveMTNTAndMTNRInTransit(mtnt);
	}

	/**
	 * Save mtnt and mtnr in transit.
	 * 
	 * @param mtnt
	 *            the mtnt
	 */
	private void saveMTNTAndMTNRInTransit(MTNT mtnt) {

		// Check whether Truck Stock exists or not
		TruckStock existingTruck = productDistributionDAO.findTruckStockByTruckId(mtnt.getTruckId());

		if (!ObjectUtil.isEmpty(existingTruck)) {
			if (MTNT.Type.MTNT.ordinal() == mtnt.getType()) {
				// Updating for MTNT
				existingTruck.setSendStock(
						String.valueOf(Double.valueOf(existingTruck.getSendStock()) + mtnt.getTotalGrossWeight()));
				existingTruck.setCreateTime(mtnt.getMtntDate());
			} else {
				// Updating for MTNR
				if (!StringUtil.isEmpty(existingTruck.getReceivedStock()))
					existingTruck.setReceivedStock(String.valueOf(
							Double.parseDouble(existingTruck.getReceivedStock()) + mtnt.getTotalGrossWeight()));
				else
					existingTruck.setReceivedStock(String.valueOf(mtnt.getTotalGrossWeight()));
			}
			// To set Reason
			if (!StringUtil.isEmpty(existingTruck.getReceivedStock())
					&& existingTruck.getRemainingStock().equalsIgnoreCase("0.0")) {
				existingTruck.setReason(TruckStock.COMPLETED);
			}
			// Updating Truck Stock
			productDistributionDAO.update(existingTruck);

		} else {
			if (MTNT.Type.MTNT.ordinal() == mtnt.getType()) {
				// Forming Truck Stock
				existingTruck = new TruckStock();
				existingTruck.setTruckId(mtnt.getTruckId());
				existingTruck.setCreateTime(mtnt.getMtntDate());
				existingTruck.setSendStock(String.valueOf(mtnt.getTotalGrossWeight()));
				// Saving Truck Stock
				productDistributionDAO.save(existingTruck);
			}
		}
		// Saving Truck Stock Details
		if (!ObjectUtil.isEmpty(existingTruck))
			formTruckStockDetails(mtnt, existingTruck);
	}

	/**
	 * Form truck stock details.
	 * 
	 * @param mtnt
	 *            the mtnt
	 * @param truckStock
	 *            the truck stock
	 */
	private void formTruckStockDetails(MTNT mtnt, TruckStock truckStock) {

		// Forming Truck Stock Details
		TruckStockDetail truckStockDetail = new TruckStockDetail();
		if (!StringUtil.isEmpty(mtnt.getDriverId()))
			truckStockDetail.setDriverId(mtnt.getDriverId());
		else
			truckStockDetail.setDriverId(AgroTransaction.NOT_APPLICABLE);
		truckStockDetail.setUniqueSeqId(idGenerator.createTruckSequence());
		truckStockDetail.setCreateTime(mtnt.getMtntDate());
		truckStockDetail.setStock(String.valueOf(mtnt.getTotalGrossWeight()));
		truckStockDetail.setType(mtnt.getType());
		truckStockDetail.setTruckStock(truckStock);
		// Save Truck Stock Details
		productDistributionDAO.save(truckStockDetail);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #saveDistributionAndDistributionDetail
	 * (com.sourcetrace.eses.order.entity.txn.Distribution)
	 */
	public void saveDistributionAndDistributionDetail(Distribution distribution) {

		/** SAVING DISTRIBUTION OBJECT **/
		/*
		 * Saving Agro Transaction on sidtribution is must,then only balance
		 * calculations will be correct
		 */
		if (!Distribution.PRODUCT_DISTRIBUTION_TO_FARMER
				.equalsIgnoreCase(distribution.getAgroTransaction().getTxnType())) {
			if (ObjectUtil.isEmpty(distribution.getAgroTransaction().getRefAgroTransaction()))
				distribution.getAgroTransaction()
						.setRefAgroTransaction(processAgroTransaction(distribution.getAgroTransaction()));
			distribution.getAgroTransaction().setSeasonCode(distribution.getSeasonCode());
			distribution.getAgroTransaction().setSeasonCode(distribution.getSeasonCode());
			productDistributionDAO.save(distribution.getAgroTransaction().getRefAgroTransaction());
			productDistributionDAO.save(distribution.getAgroTransaction());
			// accountDAO.updateCashBal(distribution.getAgroTransaction().getAccount().getId(),distribution.getAgroTransaction().getAccount().getCashBalance(),distribution.getAgroTransaction().getAccount().getCreditBalance());
		} else {
			List<AgroTransaction> agroDistList = buildDistributionAgroTransactionObject(distribution);
			// List<AgroTransaction> agroDistList
			// =buildAgroTransactionDistributionObject(distribution);
			for (AgroTransaction agro : agroDistList) {
				agro.setSeasonCode(distribution.getSeasonCode());
				agro.setDistribution(distribution);
				productDistributionDAO.save(agro);
				accountDAO.updateESEAccountCashBalById(agro.getEseAccountId(), agro.getBalAmount());

			}
			// distribution.setAgroTransaction(null);
		}

		productDistributionDAO.save(distribution);
		processWarehouseProducts(distribution);
	}

	private List<AgroTransaction> buildDistributionAgroTransactionObject(Distribution distribution) {
		List<AgroTransaction> agroTransactionList = null;
		if (!ObjectUtil.isEmpty(distribution)) {
			agroTransactionList = new ArrayList<AgroTransaction>();
			if ("0".equalsIgnoreCase(distribution.getFreeDistribution())) {
				if (!StringUtil.isEmpty(distribution.getFarmerId())) {// Checking
																		// Reg
																		// Former
																		// or
																		// Not
					AgroTransaction finalBalAgroObject = new AgroTransaction();
					finalBalAgroObject = formNewAgroTransaction(finalBalAgroObject, distribution.getAgroTransaction());
					// distribution.getAgroTransaction();
					finalBalAgroObject.setIntBalance(distribution.getAgroTransaction().getBalAmount());
					finalBalAgroObject.setTxnAmount(distribution.getTxnAmount());
					finalBalAgroObject.setBalAmount(
							distribution.getAgroTransaction().getBalAmount() + distribution.getTxnAmount());
					finalBalAgroObject.setProfType("02");
					ESEAccount farmerAccount = productDistributionDAO
							.findESEAccountByProfileId(distribution.getFarmerId(), ESEAccount.CONTRACT_ACCOUNT);
					if (!ObjectUtil.isEmpty(farmerAccount)) {
						farmerAccount.setCashBalance(finalBalAgroObject.getBalAmount());
						productDistributionDAO.update(farmerAccount);
					}
					finalBalAgroObject.setTxnDesc(Distribution.DISTRIBUTION_PAYMENT_AMOUNT);
					finalBalAgroObject.setTxnType(distribution.getAgroTransaction().getTxnType());
					finalBalAgroObject.setBranch_id(distribution.getBranchId());
					agroTransactionList.add(distribution.getAgroTransaction());
					agroTransactionList.add(finalBalAgroObject);
					// AgentORWarehouseAccount forming
					if (!StringUtil.isEmpty(distribution.getAgentId())) {
						ESEAccount agentAccount = productDistributionDAO
								.findESEAccountByProfileId(distribution.getAgentId(), ESEAccount.AGENT_ACCOUNT);
						if (!ObjectUtil.isEmpty(agentAccount)) {
							AgroTransaction agentAgroTransaction = new AgroTransaction();
							agentAgroTransaction = formNewAgroTransaction(agentAgroTransaction,
									distribution.getAgroTransaction());
							agentAgroTransaction.setIntBalance(agentAccount.getCashBalance());
							agentAgroTransaction.setTxnAmount(distribution.getTxnAmount());
							agentAgroTransaction
									.setBalAmount(agentAccount.getCashBalance() + distribution.getTxnAmount());
							agentAgroTransaction.setTxnType(Distribution.PRODUCT_DISTRIBUTION_TO_FARMER);
							agentAccount.setCashBalance(agentAgroTransaction.getBalAmount());
							productDistributionDAO.update(agentAccount);
							agentAgroTransaction.setAccount(agentAccount);
							agentAgroTransaction.setProfType("01");// AGENT_ACCOUNT
							agentAgroTransaction.setTxnDesc(Distribution.DISTRIBUTION_PAYMENT_AMOUNT);
							agentAgroTransaction.setBranch_id(distribution.getBranchId());
							agroTransactionList.add(agentAgroTransaction);
						}

					} else if (!StringUtil.isEmpty(distribution.getServicePointId())) {
						ESEAccount servicePointAccount = productDistributionDAO
								.findESEAccountByProfileId(distribution.getAgentId(), ESEAccount.COMPANY_ACCOUNT_TYPE);
						if (!ObjectUtil.isEmpty(servicePointAccount)) {
							AgroTransaction warehouseAgroTransactiont = new AgroTransaction();
							warehouseAgroTransactiont = formNewAgroTransaction(warehouseAgroTransactiont,
									distribution.getAgroTransaction());
							warehouseAgroTransactiont.setIntBalance(servicePointAccount.getCashBalance());
							warehouseAgroTransactiont.setTxnAmount(distribution.getTxnAmount());
							warehouseAgroTransactiont
									.setBalAmount(servicePointAccount.getCashBalance() + distribution.getTxnAmount());
							warehouseAgroTransactiont.setAccount(servicePointAccount);
							warehouseAgroTransactiont.setProfType("03");// Warehouse
																		// Account
							warehouseAgroTransactiont.setBranch_id(distribution.getBranchId());
							agroTransactionList.add(warehouseAgroTransactiont);
						}
					}

				} else if (!StringUtil.isEmpty(distribution.getAgentId())) {// single
																			// Rec
																			// for
																			// Unreg
																			// Former
					ESEAccount agentAccount = productDistributionDAO
							.findESEAccountByProfileId(distribution.getAgentId(), ESEAccount.AGENT_ACCOUNT);
					if (!ObjectUtil.isEmpty(agentAccount)) {
						distribution.getAgroTransaction().setIntBalance(agentAccount.getCashBalance());
						distribution.getAgroTransaction()
								.setBalAmount(agentAccount.getCashBalance() + distribution.getTxnAmount());
						distribution.getAgroTransaction().setAccount(agentAccount);
						distribution.getAgroTransaction().setTxnDesc(Distribution.DISTRIBUTION_PAYMENT_AMOUNT);
						distribution.getAgroTransaction().setProfType("01");// AGENT_ACCOUNT
						agroTransactionList.add(distribution.getAgroTransaction());
					}

				} else if (!StringUtil.isEmpty(distribution.getServicePointId())) {
					ESEAccount servicePointAccount = productDistributionDAO
							.findESEAccountByProfileId(distribution.getAgentId(), ESEAccount.COMPANY_ACCOUNT_TYPE);
					if (!ObjectUtil.isEmpty(servicePointAccount)) {
						distribution.getAgroTransaction().setIntBalance(servicePointAccount.getCashBalance());
						distribution.getAgroTransaction()
								.setBalAmount(servicePointAccount.getCashBalance() + distribution.getTxnAmount());
						distribution.getAgroTransaction().setAccount(servicePointAccount);
						distribution.getAgroTransaction().setProfType("03");// Warehouse
																			// Account
						distribution.getAgroTransaction().setTxnDesc(Distribution.DISTRIBUTION_PAYMENT_AMOUNT);
						agroTransactionList.add(distribution.getAgroTransaction());
					}
				}
			}
		} else if ("1".equalsIgnoreCase(distribution.getFreeDistribution())) {

			AgroTransaction freeAgroTxn = new AgroTransaction();
			freeAgroTxn.setReceiptNo(distribution.getAgroTransaction().getReceiptNo());
			freeAgroTxn.setAgentId(StringUtil.isEmpty(distribution.getAgroTransaction().getAgentId()) ? "N/A"
					: distribution.getAgroTransaction().getAgentId());
			freeAgroTxn.setAgentName(StringUtil.isEmpty(distribution.getAgroTransaction().getAgentName()) ? "N/A"
					: distribution.getAgroTransaction().getAgentName());
			freeAgroTxn.setDeviceId(StringUtil.isEmpty(distribution.getAgroTransaction().getDeviceId()) ? "N/A"
					: distribution.getAgroTransaction().getDeviceId());
			freeAgroTxn.setDeviceName(StringUtil.isEmpty(distribution.getAgroTransaction().getDeviceName()) ? "N/A"
					: distribution.getAgroTransaction().getDeviceName());
			freeAgroTxn.setServicePointId(StringUtil.isEmpty(distribution.getAgroTransaction().getServicePointId())
					? "N/A" : distribution.getAgroTransaction().getServicePointId());
			freeAgroTxn.setServicePointName(StringUtil.isEmpty(distribution.getAgroTransaction().getServicePointName())
					? "N/A" : distribution.getAgroTransaction().getServicePointName());
			freeAgroTxn.setFarmerId(distribution.getAgroTransaction().getFarmerId());
			freeAgroTxn.setFarmerName(distribution.getAgroTransaction().getFarmerName());
			freeAgroTxn.setVendorId(null);
			freeAgroTxn.setVendorName(null);
			freeAgroTxn.setProfType(null);
			freeAgroTxn.setOperType(1);
			freeAgroTxn.setTxnTime(distribution.getAgroTransaction().getTxnTime());
			freeAgroTxn.setTxnDesc("FREE PRODUCT DISTRIBUTION");
			freeAgroTxn.setModeOfPayment("N/A");
			freeAgroTxn.setIntBalance(0.00);
			freeAgroTxn.setTxnAmount(0.00);
			freeAgroTxn.setBalAmount(0.00);
			freeAgroTxn.setAccount(null);
			freeAgroTxn.setTxnType(distribution.getAgroTransaction().getTxnType());
			freeAgroTxn.setBranch_id(distribution.getBranchId());
			agroTransactionList.add(freeAgroTxn);
		}
		// TODO Auto-generated method stub
		return agroTransactionList;
	}

	private List<AgroTransaction> buildDistributionAgroTransactionObject(Distribution distribution, String tenantId) {
		List<AgroTransaction> agroTransactionList = null;
		if (!ObjectUtil.isEmpty(distribution)) {
			agroTransactionList = new ArrayList<AgroTransaction>();
			if ("0".equalsIgnoreCase(distribution.getFreeDistribution())) {
				if (!StringUtil.isEmpty(distribution.getFarmerId())) {// Checking
																		// Reg
																		// Former
																		// or
																		// Not
					AgroTransaction finalBalAgroObject = new AgroTransaction();
					finalBalAgroObject = formNewAgroTransaction(finalBalAgroObject, distribution.getAgroTransaction());
					// distribution.getAgroTransaction();
					finalBalAgroObject.setBranch_id(distribution.getBranchId());
					finalBalAgroObject.setIntBalance(distribution.getAgroTransaction().getBalAmount());
					finalBalAgroObject.setTxnAmount(distribution.getPaymentAmount());
					finalBalAgroObject.setBalAmount(
							distribution.getAgroTransaction().getBalAmount() + distribution.getPaymentAmount());
					finalBalAgroObject.setProfType("02");
					ESEAccount farmerAccount = accountDAO.findAccountByProfileId(distribution.getFarmerId(), tenantId);
					if (!ObjectUtil.isEmpty(farmerAccount)) {
						farmerAccount.setCashBalance(finalBalAgroObject.getBalAmount());

						productDistributionDAO.updateESEAccount(farmerAccount, tenantId);
					}
					finalBalAgroObject.setTxnDesc(Distribution.DISTRIBUTION_PAYMENT_AMOUNT);
					finalBalAgroObject.setTxnType(distribution.getAgroTransaction().getTxnType());
					distribution.getAgroTransaction().setDistribution(null);
					agroTransactionList.add(distribution.getAgroTransaction());
					agroTransactionList.add(finalBalAgroObject);
					// AgentORWarehouseAccount forming
					if (!StringUtil.isEmpty(distribution.getAgentId())) {
						ESEAccount agentAccount = accountDAO.findAccountByProfileId(distribution.getAgentId(),
								tenantId);
						if (!ObjectUtil.isEmpty(agentAccount)) {
							AgroTransaction agentAgroTransaction = new AgroTransaction();
							agentAgroTransaction = formNewAgroTransaction(agentAgroTransaction,
									distribution.getAgroTransaction());
							agentAgroTransaction.setIntBalance(agentAccount.getCashBalance());
							agentAgroTransaction.setTxnAmount(distribution.getPaymentAmount());
							agentAgroTransaction
									.setBalAmount(agentAccount.getCashBalance() + distribution.getPaymentAmount());
							agentAgroTransaction.setTxnType(Distribution.PRODUCT_DISTRIBUTION_TO_FARMER);
							agentAccount.setCashBalance(agentAgroTransaction.getBalAmount());
							productDistributionDAO.updateESEAccount(agentAccount, tenantId);
							agentAgroTransaction.setAccount(agentAccount);
							agentAgroTransaction.setProfType("01");// AGENT_ACCOUNT
							agentAgroTransaction.setTxnDesc(Distribution.DISTRIBUTION_PAYMENT_AMOUNT);
							agentAgroTransaction.setBranch_id(distribution.getBranchId());
							agroTransactionList.add(agentAgroTransaction);
						}

					} else if (!StringUtil.isEmpty(distribution.getServicePointId())) {
						ESEAccount servicePointAccount = accountDAO.findAccountByProfileId(distribution.getAgentId(),
								tenantId);
						if (!ObjectUtil.isEmpty(servicePointAccount)) {
							AgroTransaction warehouseAgroTransactiont = new AgroTransaction();
							warehouseAgroTransactiont = formNewAgroTransaction(warehouseAgroTransactiont,
									distribution.getAgroTransaction());
							warehouseAgroTransactiont.setIntBalance(servicePointAccount.getCashBalance());
							warehouseAgroTransactiont.setTxnAmount(distribution.getTxnAmount());
							warehouseAgroTransactiont
									.setBalAmount(servicePointAccount.getCashBalance() + distribution.getTxnAmount());
							warehouseAgroTransactiont.setAccount(servicePointAccount);
							warehouseAgroTransactiont.setProfType("03");// Warehouse
																		// Account
							warehouseAgroTransactiont.setBranch_id(distribution.getBranchId());
							agroTransactionList.add(warehouseAgroTransactiont);
						}
					}

				} else if (!StringUtil.isEmpty(distribution.getAgentId())) {// single
																			// Rec
																			// for
																			// Unreg
																			// Former
					ESEAccount agentAccount = accountDAO.findAccountByProfileId(distribution.getAgentId(), tenantId);
					if (!ObjectUtil.isEmpty(agentAccount)) {
						distribution.getAgroTransaction().setIntBalance(agentAccount.getCashBalance());
						distribution.getAgroTransaction()
								.setBalAmount(agentAccount.getCashBalance() + distribution.getTxnAmount());
						distribution.getAgroTransaction().setAccount(agentAccount);
						distribution.getAgroTransaction().setTxnDesc(Distribution.DISTRIBUTION_PAYMENT_AMOUNT);
						distribution.getAgroTransaction().setProfType("01");// AGENT_ACCOUNT
						agroTransactionList.add(distribution.getAgroTransaction());
					}

				} else if (!StringUtil.isEmpty(distribution.getServicePointId())) {
					ESEAccount servicePointAccount = accountDAO.findAccountByProfileId(distribution.getAgentId(),
							tenantId);
					if (!ObjectUtil.isEmpty(servicePointAccount)) {
						distribution.getAgroTransaction().setIntBalance(servicePointAccount.getCashBalance());
						distribution.getAgroTransaction()
								.setBalAmount(servicePointAccount.getCashBalance() + distribution.getTxnAmount());
						distribution.getAgroTransaction().setAccount(servicePointAccount);
						distribution.getAgroTransaction().setProfType("03");// Warehouse
																			// Account
						distribution.getAgroTransaction().setTxnDesc(Distribution.DISTRIBUTION_PAYMENT_AMOUNT);
						agroTransactionList.add(distribution.getAgroTransaction());
					}
				}
			}
		} else if ("1".equalsIgnoreCase(distribution.getFreeDistribution())) {

			AgroTransaction freeAgroTxn = new AgroTransaction();
			freeAgroTxn.setReceiptNo(distribution.getAgroTransaction().getReceiptNo());
			freeAgroTxn.setAgentId(StringUtil.isEmpty(distribution.getAgroTransaction().getAgentId()) ? "N/A"
					: distribution.getAgroTransaction().getAgentId());
			freeAgroTxn.setAgentName(StringUtil.isEmpty(distribution.getAgroTransaction().getAgentName()) ? "N/A"
					: distribution.getAgroTransaction().getAgentName());
			freeAgroTxn.setDeviceId(StringUtil.isEmpty(distribution.getAgroTransaction().getDeviceId()) ? "N/A"
					: distribution.getAgroTransaction().getDeviceId());
			freeAgroTxn.setDeviceName(StringUtil.isEmpty(distribution.getAgroTransaction().getDeviceName()) ? "N/A"
					: distribution.getAgroTransaction().getDeviceName());
			freeAgroTxn.setServicePointId(StringUtil.isEmpty(distribution.getAgroTransaction().getServicePointId())
					? "N/A" : distribution.getAgroTransaction().getServicePointId());
			freeAgroTxn.setServicePointName(StringUtil.isEmpty(distribution.getAgroTransaction().getServicePointName())
					? "N/A" : distribution.getAgroTransaction().getServicePointName());
			freeAgroTxn.setFarmerId(distribution.getAgroTransaction().getFarmerId());
			freeAgroTxn.setFarmerName(distribution.getAgroTransaction().getFarmerName());
			freeAgroTxn.setVendorId(null);
			freeAgroTxn.setVendorName(null);
			freeAgroTxn.setProfType(null);
			freeAgroTxn.setOperType(1);
			freeAgroTxn.setTxnTime(distribution.getAgroTransaction().getTxnTime());
			freeAgroTxn.setTxnDesc("FREE PRODUCT DISTRIBUTION");
			freeAgroTxn.setModeOfPayment("N/A");
			freeAgroTxn.setIntBalance(0.00);
			freeAgroTxn.setTxnAmount(0.00);
			freeAgroTxn.setBalAmount(0.00);
			freeAgroTxn.setAccount(null);
			freeAgroTxn.setTxnType(distribution.getAgroTransaction().getTxnType());
			freeAgroTxn.setBranch_id(distribution.getBranchId());
			agroTransactionList.add(freeAgroTxn);
		}
		// TODO Auto-generated method stub
		return agroTransactionList;
	}

	private AgroTransaction formNewAgroTransaction(AgroTransaction finalBalAgroObject,
			AgroTransaction agroTransaction) {
		finalBalAgroObject.setReceiptNo(agroTransaction.getReceiptNo());
		finalBalAgroObject.setAgentId(agroTransaction.getAgentId());
		finalBalAgroObject.setAgentName(agroTransaction.getAgentName());
		finalBalAgroObject.setServicePointId(agroTransaction.getServicePointId());
		finalBalAgroObject.setServicePointName(agroTransaction.getServicePointName());
		finalBalAgroObject.setFarmerId(agroTransaction.getFarmerId());
		finalBalAgroObject.setFarmerName(agroTransaction.getFarmerName());
		finalBalAgroObject.setOperType(agroTransaction.getOperType());
		finalBalAgroObject.setTxnTime(agroTransaction.getTxnTime());
		finalBalAgroObject.setAccount(agroTransaction.getAccount());

		// TODO Auto-generated method stub
		return finalBalAgroObject;
	}

	/**
	 * Process agro transaction.
	 * 
	 * @param agroTransaction
	 *            the agro transaction
	 * @return the agro transaction
	 */
	private AgroTransaction processAgroTransactionTenant(AgroTransaction agroTransaction,String tenantId) {

		AgroTransaction refAgroTransaction = new AgroTransaction();
		refAgroTransaction.setReceiptNo(agroTransaction.getReceiptNo());
		refAgroTransaction.setTxnTime(agroTransaction.getTxnTime());
		refAgroTransaction.setFarmerId(agroTransaction.getFarmerId());
		refAgroTransaction.setFarmerName(agroTransaction.getFarmerName());
		refAgroTransaction.setSamithi(agroTransaction.getSamithi());
		refAgroTransaction.setAgentId(agroTransaction.getAgentId());
		refAgroTransaction.setAgentName(agroTransaction.getAgentName());
		refAgroTransaction.setServicePointId(agroTransaction.getServicePointId());
		refAgroTransaction.setServicePointName(agroTransaction.getServicePointName());
		refAgroTransaction.setDeviceId(agroTransaction.getDeviceId());
		refAgroTransaction.setDeviceName(agroTransaction.getDeviceName());
		refAgroTransaction.setOperType(agroTransaction.getOperType());
		refAgroTransaction.setProfType(Profile.CLIENT);
		refAgroTransaction.setTxnType(Distribution.PRODUCT_DISTRIBUTION_TO_FARMER);
		refAgroTransaction.setTxnDesc(Distribution.PRODUCT_DISTRIBUTION_TO_FARMER_DESCRIPTION);
		Farmer farmer = farmerDAO.findFarmerByFarmerId(agroTransaction.getFarmerId(),tenantId);
		Season season = findCurrentSeason();
		ESEAccount farmerAccount = null;
		if (!ObjectUtil.isEmpty(farmer) && !ObjectUtil.isEmpty(season)) {
			farmerAccount = farmerDAO.findAccountBySeassonProcurmentProductFarmer(season.getId(), 0L, farmer.getId(),tenantId);
			if (!ObjectUtil.isEmpty(farmerAccount)) {
				refAgroTransaction.setIntBalance(farmerAccount.getDistributionBalance());
				refAgroTransaction.setTxnAmount(agroTransaction.getTxnAmount());
				refAgroTransaction.setBalAmount(refAgroTransaction.getIntBalance() - refAgroTransaction.getTxnAmount());
				refAgroTransaction.setAccount(farmerAccount);
				farmerAccount.setDistributionBalance(refAgroTransaction.getBalAmount());
			}
		}
		return refAgroTransaction;
	}
	
	private AgroTransaction processAgroTransaction(AgroTransaction agroTransaction) {

		AgroTransaction refAgroTransaction = new AgroTransaction();
		refAgroTransaction.setReceiptNo(agroTransaction.getReceiptNo());
		refAgroTransaction.setTxnTime(agroTransaction.getTxnTime());
		refAgroTransaction.setFarmerId(agroTransaction.getFarmerId());
		refAgroTransaction.setFarmerName(agroTransaction.getFarmerName());
		refAgroTransaction.setSamithi(agroTransaction.getSamithi());
		refAgroTransaction.setAgentId(agroTransaction.getAgentId());
		refAgroTransaction.setAgentName(agroTransaction.getAgentName());
		refAgroTransaction.setServicePointId(agroTransaction.getServicePointId());
		refAgroTransaction.setServicePointName(agroTransaction.getServicePointName());
		refAgroTransaction.setDeviceId(agroTransaction.getDeviceId());
		refAgroTransaction.setDeviceName(agroTransaction.getDeviceName());
		refAgroTransaction.setOperType(agroTransaction.getOperType());
		refAgroTransaction.setProfType(Profile.CLIENT);
		refAgroTransaction.setTxnType(Distribution.PRODUCT_DISTRIBUTION_TO_FARMER);
		refAgroTransaction.setTxnDesc(Distribution.PRODUCT_DISTRIBUTION_TO_FARMER_DESCRIPTION);
		Farmer farmer = farmerDAO.findFarmerByFarmerId(agroTransaction.getFarmerId());
		Season season = findCurrentSeason();
		ESEAccount farmerAccount = null;
		if (!ObjectUtil.isEmpty(farmer) && !ObjectUtil.isEmpty(season)) {
			farmerAccount = farmerDAO.findAccountBySeassonProcurmentProductFarmer(season.getId(), 0L, farmer.getId());
			if (!ObjectUtil.isEmpty(farmerAccount)) {
				refAgroTransaction.setIntBalance(farmerAccount.getDistributionBalance());
				refAgroTransaction.setTxnAmount(agroTransaction.getTxnAmount());
				refAgroTransaction.setBalAmount(refAgroTransaction.getIntBalance() - refAgroTransaction.getTxnAmount());
				refAgroTransaction.setAccount(farmerAccount);
				farmerAccount.setDistributionBalance(refAgroTransaction.getBalAmount());
			}
		}
		return refAgroTransaction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findVillageWarehouseByVillageCodeAndAgentId(long, java.lang.String)
	 */
	public List<VillageWarehouse> findVillageWarehouseByVillageCodeAndAgentId(long id, String agentId) {

		return productDistributionDAO.findVillageWarehouseByVillageCodeAndAgentId(id, agentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findVillageWarehouse(long, long, java.lang.String)
	 */
	public VillageWarehouse findVillageWarehouse(long villageId, long procurementProductId, String agentId) {

		return productDistributionDAO.findVillageWarehouse(villageId, procurementProductId, agentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #isPendingMTNTExistsForAgent(java.lang.String)
	 */
	public boolean isPendingMTNTExistsForAgent(String agentId) {

		return productDistributionDAO.isPendingMTNTExistsForAgent(agentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #listPendingMTNTExistsByAgentId(java.lang.String)
	 */
	public List<VillageWarehouse> listPendingMTNTExistsByAgentId(String agentId) {

		return productDistributionDAO.listPendingMTNTExistsByAgentId(agentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #listVillageWarehouseForAgent(java.lang.String)
	 */
	public List<VillageWarehouse> listVillageWarehouseForAgent(String agentId) {

		return productDistributionDAO.listVillageWarehouseForAgent(agentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #removePendingMTNTStockByAgentId(java.lang.String)
	 */
	public void removePendingMTNTStockByAgentId(String agentId) {

		productDistributionDAO.removePendingMTNTStockByAgentId(agentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findVillageWarehouseStock(long, long, java.lang.String, long, double)
	 */
	public VillageWarehouse findVillageWarehouseStock(long villageId, long procurementProductId, String agentId,
			long numberOfBags, double grossWeight) {

		return productDistributionDAO.findVillageWarehouseStock(villageId, procurementProductId, agentId, numberOfBags,
				grossWeight);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #addOfflineProcurementAndOfflineProcurementDetail
	 * (com.sourcetrace.eses.order.entity.txn.OfflineProcurement)
	 */
	public void addOfflineProcurementAndOfflineProcurementDetail(OfflineProcurement offlineProcurement) {

		productDistributionDAO.save(offlineProcurement);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #listPendingOfflineProcurementList(java.lang.String)
	 */
	public List<OfflineProcurement> listPendingOfflineProcurementList(String tenantId) {

		return productDistributionDAO.listPendingOfflineProcurementList(tenantId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #update(com.sourcetrace.eses.order.entity.txn.OfflineProcurement)
	 */
	public void update(OfflineProcurement offlineProcurement) {

		productDistributionDAO.update(offlineProcurement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #addProcurementAndProcurementDetailAndUpdateOfflineProcurementAndUpdateAgent
	 * (com.sourcetrace.eses.order.entity.txn.Procurement,
	 * com.sourcetrace.eses.order.entity.txn.OfflineProcurement,
	 * com.ese.entity.profile.Agent)
	 */
	public void addProcurementAndProcurementDetailAndUpdateAgent(Procurement procurement, Agent agent,
			AgroTransaction agentPaymentTxn, AgroTransaction farmerPaymentTxn, boolean isPayment) {

		addProcurementAndProcurementDetail(procurement, agentPaymentTxn, farmerPaymentTxn, isPayment);
		productDistributionDAO.update(agent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #saveOfflineDistribution
	 * (com.sourcetrace.eses.order.entity.txn.OfflineDistribution)
	 */
	public void saveOfflineDistribution(OfflineDistribution offlineDistribution) {

		productDistributionDAO.save(offlineDistribution);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #listOfflineDistribution()
	 */
	public List<OfflineDistribution> listOfflineDistribution(String tenantId) {

		return productDistributionDAO.listOfflineDistribution(tenantId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #updateOfflineDistribution
	 * (com.sourcetrace.eses.order.entity.txn.OfflineDistribution)
	 */
	public void updateOfflineDistribution(OfflineDistribution offlineDistribution) {

		productDistributionDAO.update(offlineDistribution);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #addOfflineMTNT(com.sourcetrace.eses.order.entity.txn.OfflineMTNT)
	 */
	public void addOfflineMTNT(OfflineMTNT offlineMTNT) {

		this.productDistributionDAO.save(offlineMTNT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #listPendingOfflineMTNTAndMTNR()
	 */
	public List<OfflineMTNT> listPendingOfflineMTNTAndMTNR() {

		return this.productDistributionDAO.listPendingOfflineMTNTAndMTNR();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #editOfflineMTNT(com.sourcetrace.eses.order.entity.txn.OfflineMTNT)
	 */
	public void editOfflineMTNT(OfflineMTNT offlineMTNT) {

		this.productDistributionDAO.update(offlineMTNT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findMTNByReceiptNoAndType(java.lang.String, int)
	 */
	public MTNT findMTNByReceiptNoAndType(String receiptNo, int type) {

		return productDistributionDAO.findMTNByReceiptNoAndType(receiptNo, type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #updateMTN(com.sourcetrace.eses.order.entity.txn.MTNT)
	 */
	public void updateMTN(MTNT existing) {

		productDistributionDAO.update(existing);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #updateVillageWarehouseStock
	 * (com.sourcetrace.eses.order.entity.txn.Procurement)
	 */
	public void updateVillageWarehouseStock(Procurement procurement) {

		productDistributionDAO.update(procurement);
		updateVillageWarehouse(procurement, ESETxn.VOID);
	}

	/**
	 * Update village warehouse.
	 * 
	 * @param procurement
	 *            the procurement
	 * @param operationType
	 *            the operation type
	 */
	private void updateVillageWarehouse(Procurement procurement, int operationType) {

		for (ProcurementDetail proDetail : procurement.getProcurementDetails()) {

			VillageWarehouse existingVillageWarehouse = productDistributionDAO.findVillageWarehouse(
					procurement.getVillage().getId(), proDetail.getProcurementProduct().getId(),
					procurement.getAgroTransaction().getAgentId(), proDetail.getQuality());

			if (ObjectUtil.isEmpty(existingVillageWarehouse)) {
				if (ESETxn.VOID != operationType) {
					VillageWarehouse villageWarehouse = new VillageWarehouse();
					villageWarehouse.setVillage(procurement.getVillage());
					villageWarehouse.setProcurementProduct(proDetail.getProcurementProduct());
					villageWarehouse.setGrossWeight(proDetail.getGrossWeight());
					villageWarehouse.setNumberOfBags(proDetail.getNumberOfBags());
					villageWarehouse.setAgentId(procurement.getAgroTransaction().getAgentId());
					villageWarehouse.setIsDelete(VillageWarehouse.NOT_DELETED);
					villageWarehouse.setQuality(proDetail.getQuality());
					productDistributionDAO.save(villageWarehouse);
				}
			} else {
				long totalNumberOfBags;
				double totalGrossWeight;
				if (ESETxn.VOID == operationType) {
					totalNumberOfBags = existingVillageWarehouse.getNumberOfBags() - proDetail.getNumberOfBags();
					totalGrossWeight = existingVillageWarehouse.getGrossWeight() - proDetail.getGrossWeight();
				} else {
					totalNumberOfBags = existingVillageWarehouse.getNumberOfBags() + proDetail.getNumberOfBags();
					totalGrossWeight = existingVillageWarehouse.getGrossWeight() + proDetail.getGrossWeight();
				}
				existingVillageWarehouse.setNumberOfBags(totalNumberOfBags);
				existingVillageWarehouse.setGrossWeight(totalGrossWeight);
				productDistributionDAO.update(existingVillageWarehouse);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findDistributionByRecNoAndOperType(java.lang.String, int)
	 */
	public Distribution findDistributionByRecNoAndOperType(String receiptNo, int operationType) {

		return productDistributionDAO.findDistributionByRecNoAndOperType(receiptNo, operationType);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findProcurementByRecNoAndOperType(java.lang.String, int,
	 * java.lang.String)
	 */
	public Procurement findProcurementByRecNoAndOperType(String receiptNo, int operationType, String tenantId) {

		return productDistributionDAO.findProcurementByRecNoAndOperType(receiptNo, operationType, tenantId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findMTNByReceiptNoTypeAndOperType(java.lang.String, int)
	 */
	public MTNT findMTNByReceiptNoTypeAndOperType(String receiptNo, int type) {

		return productDistributionDAO.findMTNByReceiptNoTypeAndOperType(receiptNo, type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #editOfflineDistribution
	 * (com.sourcetrace.eses.order.entity.txn.OfflineDistribution)
	 */
	public void editOfflineDistribution(OfflineDistribution existing) {

		productDistributionDAO.update(existing);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findOfflineDistributionById(long)
	 */
	public OfflineDistribution findOfflineDistributionById(long id) {

		return productDistributionDAO.findOfflineDistributionById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findOfflineProcurementById(long)
	 */
	public OfflineProcurement findOfflineProcurementById(long id) {

		return productDistributionDAO.findOfflineProcurementById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findOfflineMTNTById(long)
	 */
	public OfflineMTNT findOfflineMTNTById(long id) {

		return productDistributionDAO.findOfflineMTNTById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #editOfflineProcurement
	 * (com.sourcetrace.eses.order.entity.txn.OfflineProcurement)
	 */
	public void editOfflineProcurement(OfflineProcurement existing) {

		productDistributionDAO.update(existing);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #listProcurementProductByType(int)
	 */
	public List<ProcurementProduct> listProcurementProductByType(int type) {

		return productDistributionDAO.listProcurementProductByType(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findAvailableStock(long, long)
	 */
	public WarehouseProduct findAvailableStock(long warehouseId, long productId) {

		return productDistributionDAO.findAvailableStock(warehouseId, productId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #addDistributionAndDistributionDetail
	 * (com.sourcetrace.eses.order.entity.txn.Distribution)
	 */
	public void addDistributionAndDistributionDetail(Distribution distribution) {

		productDistributionDAO.save(distribution);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #update(com.ese.entity.profile.WarehouseProduct)
	 */
	public void update(WarehouseProduct warehouseProduct) {

		productDistributionDAO.update(warehouseProduct);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findAgroTxnByReceiptNo(java.lang.String)
	 */
	public List<AgroTransaction> findAgroTxnByReceiptNo(String receiptNo) {

		return productDistributionDAO.findAgroTxnByReceiptNo(receiptNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #updateAgroTxnVillageWarehouseAndAccount(java.util.List)
	 */
	public void updateAgroTxnVillageWarehouseAndAccount(List<AgroTransaction> existingList) {

		if (!ObjectUtil.isListEmpty(existingList)) {
			Procurement procurement = productDistributionDAO.findProcurementByRecNo(existingList.get(0).getReceiptNo());
			ESEAccount agentAccount = null;
			ESEAccount farmerAccount = accountDAO
					.findAccountByProfileIdAndProfileType(existingList.get(0).getFarmerId(), ESEAccount.FARMER_ACCOUNT);
			for (AgroTransaction agroTxn : existingList) {
				agroTxn.setOperType(ESETxn.VOID);
				productDistributionDAO.update(agroTxn);
				if (Profile.AGENT.equalsIgnoreCase(agroTxn.getProfType())) {
					agentAccount = accountDAO.findAccountByProfileIdAndProfileType(agroTxn.getAgentId(),
							ESEAccount.AGENT_ACCOUNT);
					agentAccount.setBalance(agentAccount.getBalance() + agroTxn.getTxnAmount());

				} else {
					if (Procurement.PROCUREMENT_AMOUNT.equalsIgnoreCase(agroTxn.getTxnDesc()))
						farmerAccount.setBalance(farmerAccount.getBalance() + agroTxn.getTxnAmount());
					else if (Procurement.PROCUREMENT_PAYMENT.equalsIgnoreCase(agroTxn.getTxnDesc()))
						farmerAccount.setBalance(farmerAccount.getBalance() - agroTxn.getTxnAmount());
				}

			}
			if (!ObjectUtil.isEmpty(agentAccount))
				accountDAO.update(agentAccount);
			accountDAO.update(farmerAccount);
			updateVillageWarehouse(procurement, ESETxn.VOID);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findProcurementProductById(long)
	 */
	public ProcurementProduct findProcurementProductById(long id) {

		return productDistributionDAO.findProcurementProductById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #addMTNTAndDetailWithVillageWarehouse
	 * (com.sourcetrace.eses.order.entity.txn.MTNT)
	 */
	public void addMTNTAndDetailWithVillageWarehouse(MTNT mtnt) {

		if (!ObjectUtil.isEmpty(mtnt) && !ObjectUtil.isListEmpty(mtnt.getMtntDetails())) {
			for (MTNTDetail detail : mtnt.getMtntDetails()) {
				VillageWarehouse existingWarehouse = productDistributionDAO.findVillageWarehouse(
						detail.getVillage().getId(), detail.getProcurementProduct().getId(), mtnt.getAgentId());
				if (!ObjectUtil.isEmpty(existingWarehouse)) {
					existingWarehouse.setNumberOfBags(existingWarehouse.getNumberOfBags() - detail.getNumberOfBags());
					existingWarehouse.setGrossWeight(existingWarehouse.getGrossWeight() - detail.getGrossWeight());
					/** UPDATING VILLAGE WAREHOUSE **/
					productDistributionDAO.update(existingWarehouse);

				}

			}
			// Saving MTNT,MTNT Detail,Truck Stock and Truck Stock Detail
			saveMTNTAndMTNTDetail(mtnt);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findTruckStockByTruckId(java.lang.String)
	 */
	public TruckStock findTruckStockByTruckId(String truckId) {

		return productDistributionDAO.findTruckStockByTruckId(truckId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService #
	 * listFarmerTransactionHistory(java.lang.String, java.lang.String,
	 * java.lang.String, int)
	 */
	public List<AgroTransaction> listFarmerTransactionHistory(String farmerId, String[] transactionArray, int limit) {

		return productDistributionDAO.listFarmerTransactionHistory(farmerId, transactionArray, limit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findTruckStockById (java.lang.Long)
	 */
	public TruckStock findTruckStockById(Long id) {

		return productDistributionDAO.findTruckStockById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #editTruckStock(com .sourcetrace.eses.order.entity.txn.TruckStock)
	 */
	public void editTruckStock(TruckStock exiting) {

		productDistributionDAO.update(exiting);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #listSeasons()
	 */
	public List<Season> listSeasons() {

		return productDistributionDAO.listSeasons();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #listPaymentMode()
	 */
	public List<PaymentMode> listPaymentMode() {

		return productDistributionDAO.listPaymentMode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #saveAgroTransaction
	 * (com.sourcetrace.eses.order.entity.txn.AgroTransaction)
	 */
	/**
	 * Save agro transaction.
	 * 
	 * @param agroTransaction
	 *            the agro transaction
	 */
	public void saveAgroTransaction(AgroTransaction agroTransaction) {

		productDistributionDAO.save(agroTransaction);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findGradeById(long)
	 */
	public GradeMaster findGradeById(long id) {

		return productDistributionDAO.findGradeById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #editGradePrice(com.sourcetrace.eses.order.entity.profile.GradeMaster)
	 */
	public void editGradePrice(GradeMaster gradeMaster) {

		productDistributionDAO.update(gradeMaster);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #listGradeMaster()
	 */
	public List<GradeMaster> listGradeMaster() {

		return productDistributionDAO.listGradeMaster();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService
	 * #saveAgroTransactionForPayment(com.sourcetrace.eses.order.entity.txn.
	 * AgroTransaction, com.sourcetrace.eses.order.entity.txn.AgroTransaction)
	 */
	public void saveAgroTransactionForPayment(AgroTransaction farmerPaymentTxn, AgroTransaction agentPaymentTxn) {

		String description = farmerPaymentTxn.getTxnDesc();
		if (!StringUtil.isEmpty(description) ) {
			String[] descriptionDetail = description.split("\\|");
			// if (!ObjectUtil.isEmpty(descriptionDetail) &&
			// descriptionDetail.length >= 2) {
			// if (!ObjectUtil.isEmpty(descriptionDetail)) {
			if (PaymentMode.DISTRIBUTION_ADVANCE_PAYMENT_MODE_NAME.equalsIgnoreCase(descriptionDetail[0])
					|| PaymentMode.DISTRIBUTION_PAYMENT_MODE_NAME.equalsIgnoreCase(descriptionDetail[0])) {
				// DISTRIBUTION ADVANCE PAYMENT
				farmerPaymentTxn.calculateBalance(farmerPaymentTxn.getAccount(), farmerPaymentTxn.getTxnAmount(), false,
						true);
				agentPaymentTxn.calculateBalance(agentPaymentTxn.getAccount(), agentPaymentTxn.getTxnAmount(), false,
						true);

				ESESystem eseSystem = systemDAO.findPrefernceById("1");
				if (eseSystem.getPreferences().get(ESESystem.INTEREST_MODULE).equals("1")) {

					InterestCalcConsolidated intCalConsolidated = farmerDAO
							.findInterestCalcConsolidatedByfarmerProfileId(farmerPaymentTxn.getFarmerId());
					double paymentAmt = farmerPaymentTxn.getTxnAmount();
					if (!ObjectUtil.isEmpty(intCalConsolidated) && intCalConsolidated.getAccumulatedIntAmount() > 0) {
						double remainAmt = intCalConsolidated.getAccumulatedIntAmount() - paymentAmt;
						if (remainAmt >= 0) {
							intCalConsolidated.setAccumulatedIntAmount(remainAmt);
							intCalConsolidated.setUpdateUserName(farmerPaymentTxn.getAgentId());
							intCalConsolidated.setLastUpdateDt(new Date());
							intCalConsolidated.setRevisionNo(DateUtil.getRevisionNumber());
							farmerDAO.update(intCalConsolidated);
						} else if (remainAmt < 0) {
							intCalConsolidated.setAccumulatedIntAmount(0);
							intCalConsolidated.setAccumulatedPrincipalAmount(
									intCalConsolidated.getAccumulatedPrincipalAmount() - Math.abs(remainAmt));
							intCalConsolidated.setUpdateUserName(farmerPaymentTxn.getAgentId());
							intCalConsolidated.setLastUpdateDt(new Date());
							intCalConsolidated.setRevisionNo(DateUtil.getRevisionNumber());
							farmerDAO.update(intCalConsolidated);
						}
					} 
				}
			} else  if (PaymentMode.DISTRIBUTION_ADVANCE_PAYMENT_MODE_NAME.equalsIgnoreCase(description)
					|| PaymentMode.DISTRIBUTION_PAYMENT_MODE_NAME.equalsIgnoreCase(description)) {
				// DISTRIBUTION ADVANCE PAYMENT
				farmerPaymentTxn.calculateBalance(farmerPaymentTxn.getAccount(), farmerPaymentTxn.getTxnAmount(), false,
						true);
				agentPaymentTxn.calculateBalance(agentPaymentTxn.getAccount(), agentPaymentTxn.getTxnAmount(), false,
						true);

				ESESystem eseSystem = systemDAO.findPrefernceById("1");
				if (eseSystem.getPreferences().get(ESESystem.INTEREST_MODULE).equals("1")) {

					InterestCalcConsolidated intCalConsolidated = farmerDAO
							.findInterestCalcConsolidatedByfarmerProfileId(farmerPaymentTxn.getFarmerId());
					double paymentAmt = farmerPaymentTxn.getTxnAmount();
					if (!ObjectUtil.isEmpty(intCalConsolidated) && intCalConsolidated.getAccumulatedIntAmount() > 0) {
						double remainAmt = intCalConsolidated.getAccumulatedIntAmount() - paymentAmt;
						if (remainAmt >= 0) {
							intCalConsolidated.setAccumulatedIntAmount(remainAmt);
							intCalConsolidated.setUpdateUserName(farmerPaymentTxn.getAgentId());
							intCalConsolidated.setLastUpdateDt(new Date());
							intCalConsolidated.setRevisionNo(DateUtil.getRevisionNumber());
							farmerDAO.update(intCalConsolidated);
						} else if (remainAmt < 0) {
							intCalConsolidated.setAccumulatedIntAmount(0);
							intCalConsolidated.setAccumulatedPrincipalAmount(
									intCalConsolidated.getAccumulatedPrincipalAmount() - Math.abs(remainAmt));
							intCalConsolidated.setUpdateUserName(farmerPaymentTxn.getAgentId());
							intCalConsolidated.setLastUpdateDt(new Date());
							intCalConsolidated.setRevisionNo(DateUtil.getRevisionNumber());
							farmerDAO.update(intCalConsolidated);
						}
					} 
				}
			}else if(PaymentMode.LOAN_REPAYMENT_MODE_NAME.equalsIgnoreCase(description)){
				farmerPaymentTxn.calculateFarmerLoanBalance(farmerPaymentTxn.getAccount(), farmerPaymentTxn.getTxnAmount());
				productDistributionDAO.save(farmerPaymentTxn);
				LoanLedger loanLedger = new LoanLedger();
				loanLedger.setTxnTime(farmerPaymentTxn.getTxnTime());
				loanLedger.setReceiptNo(farmerPaymentTxn.getReceiptNo());
				loanLedger.setFarmerId(farmerPaymentTxn.getFarmerId());
				loanLedger.setAccount(farmerPaymentTxn.getAccount());
				loanLedger.setAccountNo(farmerPaymentTxn.getAccount().getLoanAccountNo());
				loanLedger.setActualAmount(farmerPaymentTxn.getTxnAmount());
				loanLedger.setNewFarmerBal(farmerPaymentTxn.getAccount().getLoanAmount());
				loanLedger.setPreFarmerBal(farmerPaymentTxn.getBalAmount());
				loanLedger.setTxnType("702");
				loanLedger.setLoanDesc(PaymentMode.LOAN_REPAYMENT_AMOUNT);
				loanLedger.setBranchId(farmerPaymentTxn.getBranchId());
				ledgerService.save(loanLedger);
				if (!StringUtil.isEmpty(farmerPaymentTxn.getBalAmount())) {
					accountDAO.updateESEAccountOutStandingBalById(farmerPaymentTxn.getAccount().getId(),
							farmerPaymentTxn.getBalAmount());
				}
			
			else {// OTHER PAYMENTS
				farmerPaymentTxn.calculateBalance(farmerPaymentTxn.getAccount(), farmerPaymentTxn.getTxnAmount(), true,
						false);
				agentPaymentTxn.calculateBalance(agentPaymentTxn.getAccount(), agentPaymentTxn.getTxnAmount(), true,
						false);
			} 
			} else {// OTHER PAYMENTS
				farmerPaymentTxn.calculateBalance(farmerPaymentTxn.getAccount(), farmerPaymentTxn.getTxnAmount(), true,
						false);
				agentPaymentTxn.calculateBalance(agentPaymentTxn.getAccount(), agentPaymentTxn.getTxnAmount(), true,
						false);
			}
			

		
		
		productDistributionDAO.save(farmerPaymentTxn);
		if(!PaymentMode.LOAN_REPAYMENT_MODE_NAME.equalsIgnoreCase(descriptionDetail[0])){
			productDistributionDAO.save(agentPaymentTxn);
			}

		if (!StringUtil.isEmpty(farmerPaymentTxn.getBalAmount())) {
			accountDAO.updateESEAccountCashBalById(farmerPaymentTxn.getAccount().getId(),
					farmerPaymentTxn.getBalAmount());
		}
		
		if(!PaymentMode.LOAN_REPAYMENT_MODE_NAME.equalsIgnoreCase(descriptionDetail[0])){
			if (!StringUtil.isEmpty(agentPaymentTxn.getBalAmount())) {
				accountDAO.updateESEAccountCashBalById(agentPaymentTxn.getAccount().getId(),
						agentPaymentTxn.getBalAmount());
			}
			
		}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findPaymentModeByCode (java.lang.String)
	 */
	public PaymentMode findPaymentModeByCode(String paymentType) {

		return productDistributionDAO.findPaymentModeByCode(paymentType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findSeasonBySeasonCode (java.lang.String)
	 */
	public Season findSeasonBySeasonCode(String seasonCode) {

		return productDistributionDAO.findSeasonBySeasonCode(seasonCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService # findTripSheetByCityDateChartNo(long,
	 * java.util.Date, java.lang.String)
	 */
	public TripSheet findTripSheetByCityDateChartNo(long cityId, Date date, String chartNo) {

		return productDistributionDAO.findTripSheetByCityDateChartNo(cityId, date, chartNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findGradeByCode(java .lang.String)
	 */
	public GradeMaster findGradeByCode(String code) {

		return productDistributionDAO.findGradeByCode(code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #listProcurementProduct ()
	 */
	public List<ProcurementProduct> listProcurementProduct() {

		return productDistributionDAO.listProcurementProduct();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findGradePricingExist (long, long, long, long)
	 */
	public GradeMasterPricing findGradePricingExist(long cityId, long seasonId, long productId, long gradeMasterId) {

		return productDistributionDAO.findGradePricingExist(cityId, seasonId, productId, gradeMasterId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #addGradeMasterPricing
	 * (com.sourcetrace.eses.order.entity.profile.GradeMasterPricing)
	 */
	public void addGradeMasterPricing(GradeMasterPricing pricing) {

		productDistributionDAO.save(pricing);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #editGradeMasterPricing
	 * (com.sourcetrace.eses.order.entity.profile.GradeMasterPricing)
	 */
	public void editGradeMasterPricing(GradeMasterPricing pricing) {

		productDistributionDAO.update(pricing);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService # listPMTReceiptNumberByStatus(int)
	 */
	public List<String> listPMTReceiptNumberByStatus(int status) {

		return productDistributionDAO.listPMTReceiptNumberByStatus(status);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findPMTByReceiptNumber (java.lang.String, int)
	 */
	public PMT findPMTByReceiptNumber(String receiptNumber, int status) {

		return productDistributionDAO.findPMTByReceiptNumber(receiptNumber, status);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #editPMTForMTNR(com .sourcetrace.eses.order.entity.txn.PMT)
	 */
	public void editPMTForMTNR(PMT proceurementMTNR) {

		if (!ObjectUtil.isListEmpty(proceurementMTNR.getPmtDetails())) {
			productDistributionDAO.saveOrUpdate(proceurementMTNR);
			processCityWarehouse(proceurementMTNR);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findPMTByReceiptNumber (java.lang.String)
	 */
	public PMT findPMTByReceiptNumber(String receiptNumber) {

		return productDistributionDAO.findPMTByReceiptNumber(receiptNumber);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findSeasonById(long)
	 */
	public Season findSeasonById(long id) {

		return productDistributionDAO.findSeasonById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService #
	 * listGradeMasterPricingByCityIdSeasonIdProductId(long, long, long)
	 */
	public List<GradeMasterPricing> listGradeMasterPricingByCityIdSeasonIdProductId(long cityId, long seasonId,
			long productId) {

		return productDistributionDAO.listGradeMasterPricingByCityIdSeasonIdProductId(cityId, seasonId, productId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService # listMunicipalityFromCityWarehouse()
	 */
	public List<Municipality> listMunicipalityFromCityWarehouse() {

		return productDistributionDAO.listMunicipalityFromCityWarehouse();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService # listTripSheetByCityIdTransitStatus(long,
	 * int)
	 */
	public List<TripSheet> listTripSheetByCityIdTransitStatus(long cityId, int transitStatus) {

		return productDistributionDAO.listTripSheetByCityIdTransitStatus(cityId, transitStatus);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService #
	 * listGradeInformationByTripSheetIdProductId(long, long)
	 */
	public List<Object[]> listGradeInformationByTripSheetIdProductId(long tripSheetId, long procurementProductId) {

		return productDistributionDAO.listGradeInformationByTripSheetIdProductId(tripSheetId, procurementProductId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findTripSheetById( long)
	 */
	public TripSheet findTripSheetById(long id) {

		return productDistributionDAO.findTripSheetById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #addPMTWebTransaction (com.sourcetrace.eses.order.entity.txn.PMT,
	 * java.util.List, com.ese.entity.profile.ProcurementProduct)
	 */
	/**
	 * Adds the pmt web transaction.
	 * 
	 * @param pmt
	 *            the pmt
	 * @param tripSheetIds
	 *            the trip sheet ids
	 * @param product
	 *            the product
	 * @return the string
	 */
	public String addPMTWebTransaction(PMT pmt, List<Long> tripSheetIds, ProcurementProduct product) {

		if (!ObjectUtil.isEmpty(pmt)) {
			Set<PMTDetail> pmtDetails = new LinkedHashSet<PMTDetail>();
			Set<TripSheet> tripSheets = new LinkedHashSet<TripSheet>();

			pmt.setPmtDetails(pmtDetails);
			pmt.setTripSheets(tripSheets);

			for (Long tripSheetId : tripSheetIds) {
				TripSheet tripSheet = productDistributionDAO.findTripSheetById(tripSheetId);
				if (!ObjectUtil.isEmpty(tripSheet)
						&& tripSheet.getTransitStatus() == TripSheet.TRANSIT_STATUS.NONE.ordinal()) {
					tripSheet.setTransitStatus(TripSheet.TRANSIT_STATUS.MTNT.ordinal());
					tripSheets.add(tripSheet);
					List<Object[]> result = productDistributionDAO
							.listGradeInformationByTripSheetIdProductId(Long.valueOf(tripSheetId), product.getId());
					if (result.size() > 0) {
						for (Object[] gradeInfoArray : result) {
							if (gradeInfoArray.length >= 5) {
								PMTDetail pmtDetail = new PMTDetail();
								pmtDetail.setGradeMaster(productDistributionDAO
										.findGradeById(Long.valueOf(String.valueOf(gradeInfoArray[0]))));
								pmtDetail.setProcurementProduct(product);
								pmtDetail.setMtntGrossWeight(Double.parseDouble(String.valueOf(gradeInfoArray[2])));
								pmtDetail.setMtntNumberOfBags(Long.parseLong(String.valueOf(gradeInfoArray[1])));
								pmtDetail.setPmt(pmt);
								pmtDetails.add(pmtDetail);

								String[] buyerAgentInfo = tripSheet.getBuyerName().split("-");
								if (!ObjectUtil.isEmpty(buyerAgentInfo) && buyerAgentInfo.length > 0) {
									CityWarehouse existingWarehouse = productDistributionDAO.findCityWarehouse(
											tripSheet.getCity().getId(), product.getId(), buyerAgentInfo[0],
											pmtDetail.getGradeMaster().getCode());
									if (!ObjectUtil.isEmpty(existingWarehouse)) {
										existingWarehouse.setNumberOfBags(
												existingWarehouse.getNumberOfBags() - pmtDetail.getMtntNumberOfBags());
										existingWarehouse.setGrossWeight(
												existingWarehouse.getGrossWeight() - pmtDetail.getMtntGrossWeight());
										productDistributionDAO.update(existingWarehouse);
									}
								}
							}
						}
					}
				} else if (!ObjectUtil.isEmpty(tripSheet)) {
					LOGGER.info("MTNT ALREADY DONE FOR THE CHART NO : " + tripSheet.getChartNo());
				} else {
					LOGGER.info("TRIP SHEET OBJECT NULL FOR ID : " + tripSheetId);
				}
			}
			if (!ObjectUtil.isListEmpty(pmt.getPmtDetails())) {
				pmt.setMtntReceiptNumber(idGenerator.getMTNTReceiptNoSeq());
				productDistributionDAO.save(pmt);
			} else {
				LOGGER.info("METERIAL TRANSFER DETAIL SIZE IS EMPTY");
			}
			return pmt.getMtntReceiptNumber();
		} else {
			LOGGER.info("PROCUREMENT METERIAL TRANSFER (PMT) OBJECT NULL");
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService #
	 * saveDistributionOfflineDistributionToFieldStaff
	 * (com.sourcetrace.eses.order.entity.txn.Distribution)
	 */
	public void saveDistributionOfflineDistributionToFieldStaff(Distribution distribution) {

		saveDistributionAndDistributionDetail(distribution);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService # findAvailableStockByAgentId(long, long,
	 * long)
	 */
	public WarehouseProduct findAvailableStockByAgentId(long warehouseId, long productId, long agentId) {

		return productDistributionDAO.findAvailableStockByAgentId(warehouseId, productId, agentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService #
	 * editWarehouseProductByAgent(com.ese.entity.profile.WarehouseProduct)
	 */
	public void editWarehouseProductByAgent(WarehouseProduct warehouseProductAgent) {

		productDistributionDAO.saveOrUpdate(warehouseProductAgent);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #updateWarehouseProduct (com.ese.entity.profile.WarehouseProduct)
	 */
	public void updateWarehouseProduct(WarehouseProduct warehouseProduct) {

		productDistributionDAO.update(warehouseProduct);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService #
	 * addDistributionMTNTAndDetails(com.sourcetrace.eses.order.entity.txn.DMT)
	 */
	public void addDistributionMTNTAndDetails(DMT dmt) {

		/** SAVING DMT,DMTDETAILS AND TRANSFERINFO **/
		productDistributionDAO.save(dmt.getSenderTransferInfo());
		productDistributionDAO.save(dmt);
		/** UPDATING AND SAVING WAREHOUSE PRODUCT AND DETAIL **/
		processWarehouseProducts(dmt);
	}

	/**
	 * Process warehouse products.
	 * 
	 * @param object
	 *            the object
	 */
	public void processWarehouseProducts(Object object) {
		DecimalFormat df = new DecimalFormat("0.00");
		int i = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		WarehouseProduct warehouseProduct;
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof DMT) {
				DMT dmt = (DMT) object;
				if (dmt.getStatus() == DMT.Status.MTNT.ordinal()) { // Distribution
					// MTNT
					map.put(DATE, dmt.getMtntDate());
					map.put(RECEIPT_NO, dmt.getMtntReceiptNo());
					map.put(DESC, "DISTRIBUTION MTNT");
					for (DMTDetail dmtDetail : dmt.getDmtDetails()) {
						warehouseProduct = productDistributionDAO.findAvailableStock(dmt.getSenderWarehouse().getId(),
								dmtDetail.getProduct().getId());
						map.put(QTY, dmtDetail.getTransferedQty());
						updateWarehouseProducts(warehouseProduct, map, false);
					}
				} else if (dmt.getStatus() == DMT.Status.MTNR.ordinal()) { // Distribution
					// MTNR
					map.put(DATE, dmt.getMtnrDate());
					map.put(RECEIPT_NO, dmt.getMtnrReceiptNo());
					map.put(DESC, "DISTRIBUTION MTNR");
					for (DMTDetail dmtDetail : dmt.getDmtDetails()) {
						warehouseProduct = productDistributionDAO.findAvailableStock(dmt.getReceiverWarehouse().getId(),
								dmtDetail.getProduct().getId());
						if (ObjectUtil.isEmpty(warehouseProduct)) {
							warehouseProduct = new WarehouseProduct();
							warehouseProduct.setWarehouse(dmt.getReceiverWarehouse());
							warehouseProduct.setProduct(dmtDetail.getProduct());
						}
						map.put(QTY, dmtDetail.getReceivedQty());
						updateWarehouseProducts(warehouseProduct, map, true);
					}
				}
			} // End of DMT
			if (object instanceof Distribution) { // Distribution
				Double avbleStock = 0.00;
				Distribution distribution = (Distribution) object;
				String agentId = distribution.getAgentId();
				String txnType = distribution.getTxnType();
				map.put(DATE, distribution.getTxnTime());
				map.put(RECEIPT_NO, distribution.getReceiptNumber());
				map.put(SEASON_CODE, distribution.getSeasonCode());
				// Product Distribution to Farmer
				if (Distribution.PRODUCT_DISTRIBUTION_TO_FARMER.equals(txnType)) {
					map.put(DESC, Distribution.PRODUCT_DISTRIBUTION_TO_FARMER_DESCRIPTION);
					for (DistributionDetail distributionDetail : distribution.getDistributionDetails()) {

						if (distribution.getProductStock()
								.equalsIgnoreCase(WarehouseProduct.StockType.WAREHOUSE_STOCK.name())) {
							/*
							 * warehouseProduct =
							 * productDistributionDAO.findAvailableStocks(
							 * distribution.getServicePointId(),
							 * distributionDetail.getProduct().getId());
							 */
							Warehouse warehouse = locationDAO.findCoOperativeByCode(distribution.getServicePointId());
							warehouseProduct = productDistributionDAO
									.findAvailableStockByWarehouseIdSelectedProductSeasonBatchNo(warehouse.getId(),
											distributionDetail.getProduct().getId(), distributionDetail.getSeasonCode(),
											distributionDetail.getBatchNo());
							// avbleStock
							// =productDistributionDAO.findAvailableStockByWarehouseIdProductIdSeasonBatchNo(warehouse.getId(),distributionDetail.getProduct().getId(),
							// distributionDetail.getSeasonCode(),distributionDetail.getBatchNo());
						} else {
							warehouseProduct = productDistributionDAO.findAvailableStockByAgentIdProductIdBatchNoSeason(
									agentId, distributionDetail.getProduct().getId(),
									distributionDetail.getSeasonCode(), distributionDetail.getBatchNo());

							/*
							 * warehouseProduct = productDistributionDAO.
							 * findFieldStaffAvailableStock(agentId,
							 * distributionDetail.getProduct().getId());
							 */
						}
						// warehouseProduct.setStock(Double.valueOf(distribution.getProductStock()));
						warehouseProduct.setBranchId(distribution.getBranchId());
						warehouseProduct.setSeasonCode(distribution.getSeasonCode());
						warehouseProduct.setBatchNo(distributionDetail.getBatchNo());

						map.put(QTY, distributionDetail.getQuantity());
						String approved = productDistributionDAO.findPrefernceByName(ESESystem.ENABLE_APPROVED);
						if (Distribution.PRODUCT_DISTRIBUTION_TO_FIELDSTAFF
								.equalsIgnoreCase(distribution.getTxnType())) {
							if (approved.equalsIgnoreCase("0")) {
								updateWarehouseProducts(warehouseProduct, map, false);
							}
						} else if (distribution.getFreeDistribution().equalsIgnoreCase("0")) {
							if (approved.equalsIgnoreCase("0")) {
								updateWarehouseProducts(warehouseProduct, map, false);
							}
						} else {
							updateWarehouseProducts(warehouseProduct, map, false);
						}

					}

					PaymentLedger paymentLedger = new PaymentLedger();
					paymentLedger.setCreatedDate(distribution.getTxnTime());
					paymentLedger.setCreatedUser(distribution.getUserName());
					paymentLedger.setType(Distribution.PRODUCT_DISTRIBUTION_TO_FARMER);
					paymentLedger.setPrevValue(distribution.getIntBalance());
					paymentLedger.setTxnValue(distribution.getTxnAmount());
					paymentLedger.setNewValue(distribution.getBalAmount());

					productDistributionDAO.save(paymentLedger);
				}
				// Product Return from Farmer
				if (Distribution.PRODUCT_RETURN_FROM_FARMER.equals(txnType)) {
					map.put(DESC, Distribution.PRODUCT_RETURN_FROM_FARMER_DESCRIPTION);
					for (DistributionDetail distributionDetail : distribution.getDistributionDetails()) {
						processFieldStaffWarehouseProduct(agentId, distributionDetail, null, map);
					}
				}
				// Product Distribution to Field Staff
				if (Distribution.PRODUCT_DISTRIBUTION_TO_FIELDSTAFF.equals(txnType)) {
					map.put(DESC, Distribution.PRODUCT_DISTRIBUTION_TO_FIELDSTAFF_DESCRIPTION);

					for (DistributionDetail distributionDetail : distribution.getDistributionDetails()) {

						map.put(QTY, distributionDetail.getQuantity());

						Warehouse warehouse = locationDAO
								.findCoOperativeByCode(distribution.getAgroTransaction().getServicePointId());
						/*
						 * WarehouseProduct warehouseStock =
						 * productDistributionDAO
						 * .findCooperativeAvailableStockByCooperative(warehouse
						 * .getId(), distributionDetail.getProduct().getId());
						 */
						WarehouseProduct warehouseStock = productDistributionDAO
								.findAvailableStockByWarehouseIdProductIdBatchNoAndSeason(warehouse.getId(),
										distributionDetail.getProduct().getId(), distributionDetail.getBatchNo(),
										distribution.getSeasonCode());
						String costPrice[] = distributionDetail.getCostPriceArray().split(",");
						if (costPrice[0].length() > 1) {

							map.put(COSTPRICE, costPrice[i]);
						}
						i++;
						distributionDetail.setSeasonCode(distribution.getSeasonCode());
						// distributionDetail.setExistingQuantity(warehouseProd)
						warehouseStock.setBranchId(distribution.getBranchId());
						warehouseStock.setSeasonCode(distribution.getSeasonCode());
						warehouseStock.setBatchNo(distributionDetail.getBatchNo());
						String approved = productDistributionDAO.findPrefernceByName(ESESystem.ENABLE_APPROVED);

						if (Distribution.PRODUCT_DISTRIBUTION_TO_FIELDSTAFF
								.equalsIgnoreCase(distribution.getTxnType())) {
							if (approved.equalsIgnoreCase("0")) {
								updateWarehouseProducts(warehouseStock, map, false);
							}
						} else {
							if (distribution.getFreeDistribution().equalsIgnoreCase("0")) {
								if (approved.equalsIgnoreCase("0")) {
									updateWarehouseProducts(warehouseStock, map, false);
								}
							} else {
								updateWarehouseProducts(warehouseStock, map, false);
							}
						}
						// Add FS stock
						processFieldStaffWarehouseProduct(
								distribution.getAgroTransaction().getRefAgroTransaction().getAgentId(),
								distributionDetail, warehouseStock.getWarehouse(), map);
					}
				}
				// Product Return from Field Staff
				if (Distribution.PRODUCT_RETURN_FROM_FIELDSTAFF.equals(txnType)) {
					if (ObjectUtil.isEmpty(map.get(DATE)))
						map.put(DATE, new Date());
					map.put(DESC, Distribution.PRODUCT_RETURN_FROM_FIELDSTAFF_DESCRIPTION);
					for (DistributionDetail distributionDetail : distribution.getDistributionDetails()) {
						// Deduct FS stock
						WarehouseProduct fieldStaffStock = productDistributionDAO.findAgentAvailableStock(agentId,
								distributionDetail.getProduct().getId());
						map.put(QTY, distributionDetail.getQuantity());

						// distributionDetail.set
						updateWarehouseProducts(fieldStaffStock, map, false);
						// Add WH stock
						WarehouseProduct warehouseStock = productDistributionDAO.findAvailableStock(
								fieldStaffStock.getWarehouse().getId(), distributionDetail.getProduct().getId());
						if (ObjectUtil.isEmpty(warehouseStock)) {
							warehouseStock = new WarehouseProduct();
							warehouseStock.setWarehouse(fieldStaffStock.getWarehouse());
							warehouseStock.setProduct(distributionDetail.getProduct());
						}
						updateWarehouseProducts(warehouseStock, map, true);
					}
				}

			} // End of Distribution
			if (object instanceof WarehouseProduct) { // WarehouseProduct
				warehouseProduct = (WarehouseProduct) object;
				map.put(DATE, new Date());
				map.put(RECEIPT_NO, !StringUtil.isEmpty(warehouseProduct.getReceiptNumber())
						? warehouseProduct.getReceiptNumber() : "");
				map.put(QTY, warehouseProduct.getTxnQty());
				if (warehouseProduct.isEdit()) {
					map.put(DESC, "STOCK ADDED");
					updateWarehouseProducts(warehouseProduct, map, true);
				} else {
					map.put(DESC, "STOCK DEDUCTED");
					updateWarehouseProducts(warehouseProduct, map, false);
				}
			} // End of WarehouseProduct
			
			if(object instanceof DistributionStock){
				DistributionStock distributionStock = (DistributionStock) object;
				String txnType=distributionStock.getTxnType();
				map.put(DATE, distributionStock.getTxnTime());
				map.put(RECEIPT_NO, distributionStock.getReceiptNo());
				if (DistributionStock.DISTRIBUTION_STOCK_TRANSFER.equals(txnType)) {
					map.put(DESC, DistributionStock.DISTRIBUTION_TRANSACTION);
					for (DistributionStockDetail distributionStockDetail : distributionStock.getDistributionStockDetails()) {
						map.put(QTY, distributionStockDetail.getDistributionQuantity());
						WarehouseProduct warehouseStock = productDistributionDAO.findWarehouseStockByWarehouseIdAndProductId(distributionStock.getSenderWarehouse().getId(),
										distributionStockDetail.getProduct().getId(),distributionStock.getSeason());
						if(warehouseStock!=null && !ObjectUtil.isEmpty(warehouseStock)){
					//	warehouseStock.setBranchId(distributionStock.getBranchId());
						warehouseStock.setSeasonCode(distributionStock.getSeason());
						warehouseStock.setWarehouse(distributionStock.getSenderWarehouse());
						warehouseStock.setProduct(distributionStockDetail.getProduct());
						updateWarehouseProducts(warehouseStock, map, false);
						}
					}
				}
				else if(DistributionStock.DISTRIBUTION_STOCK_RECEPTION.equals(txnType)){
					map.put(DESC, DistributionStock.DISTRIBUTION_RECEPTION);
					for (DistributionStockDetail distributionStockDetail : distributionStock.getDistributionStockDetails()) {
						map.put(QTY, distributionStockDetail.getDistributionQuantity());
						WarehouseProduct warehouseStock = productDistributionDAO.findWarehouseStockByWarehouseIdAndProductId(distributionStock.getReceiverWarehouse().getId(),
										distributionStockDetail.getProduct().getId(),distributionStock.getSeason());
						if(warehouseStock!=null && !ObjectUtil.isEmpty(warehouseStock)){
						warehouseStock.setBranchId(distributionStock.getBranchId());
						warehouseStock.setSeasonCode(distributionStock.getSeason());
						warehouseStock.setWarehouse(distributionStock.getReceiverWarehouse());
						warehouseStock.setProduct(distributionStockDetail.getProduct());
						warehouseStock.setDamagedQty(distributionStockDetail.getDamageQuantity());
						updateWarehouseProducts(warehouseStock, map, true);
						}
						else{
							warehouseStock=new WarehouseProduct();
							warehouseStock.setBatchNo("NA");
							warehouseStock.setBranchId(distributionStock.getBranchId());
							warehouseStock.setSeasonCode(distributionStock.getSeason());
							warehouseStock.setWarehouse(distributionStock.getReceiverWarehouse());
							warehouseStock.setProduct(distributionStockDetail.getProduct());
							warehouseStock.setDamagedQty(distributionStockDetail.getDamageQuantity());
							updateWarehouseProducts(warehouseStock, map, true);
						}
					}
				
				}
			}

		}
	}

	/**
	 * Process field staff warehouse product.
	 * 
	 * @param agentId
	 *            the agent id
	 * @param distributionDetail
	 *            the distribution detail
	 * @param warehouse
	 *            the warehouse
	 * @param map
	 *            the map
	 */
	private void processFieldStaffWarehouseProduct(String agentId, DistributionDetail distributionDetail,
			Warehouse warehouse, Map<String, Object> map) {

		/*
		 * WarehouseProduct fieldStaffStock =
		 * productDistributionDAO.findFieldStaffAvailableStock(agentId,
		 * distributionDetail.getProduct().getId());
		 */
		WarehouseProduct fieldStaffStock = productDistributionDAO.findAvailableStockByAgentIdProductIdBatchNoSeason(
				agentId, distributionDetail.getProduct().getId(), distributionDetail.getSeasonCode(),
				distributionDetail.getBatchNo());
		if (ObjectUtil.isEmpty(fieldStaffStock)) {
			fieldStaffStock = new WarehouseProduct();
			Agent agent = agentDAO.findAgentByProfileId(agentId);
			/*
			 * if (ObjectUtil.isEmpty(warehouse)) { warehouse =
			 * agent.getCooperative(); }
			 * fieldStaffStock.setWarehouse(warehouse);
			 */
			fieldStaffStock.setProduct(distributionDetail.getProduct());
			fieldStaffStock.setBranchId(warehouse.getBranchId());
			fieldStaffStock.setBatchNo(distributionDetail.getBatchNo());
			// fieldStaffStock.setSeasonCode(distributionDetail.getDistribution().getSeasonCode());
			if (!ObjectUtil.isEmpty(agent))
				fieldStaffStock.setAgent(agent);
		}
		String approved = productDistributionDAO.findPrefernceByName(ESESystem.ENABLE_APPROVED);
		if (approved.equalsIgnoreCase("1")) {
			map.put(QTY, distributionDetail.getQuantity());
			/*
			 * if (distributionDetail.getStatus() == 1) {
			 * updateWarehouseProducts(fieldStaffStock, map, true); }
			 */
			fieldStaffStock.setBatchNo(distributionDetail.getBatchNo());
			updateWarehouseProducts(fieldStaffStock, map, true);
		} else {
			fieldStaffStock.setBatchNo(distributionDetail.getBatchNo());
			updateWarehouseProducts(fieldStaffStock, map, true);
		}
	}

	/**
	 * Update warehouse products.
	 * 
	 * @param warehouseProduct
	 *            the warehouse product
	 * @param map
	 *            the map
	 * @param flag
	 *            the flag
	 */
	private void updateWarehouseProducts(WarehouseProduct warehouseProduct, Map<String, Object> map, boolean flag) {
		DecimalFormat df = new DecimalFormat("0.0000");

		if (!ObjectUtil.isEmpty(warehouseProduct)) {
			WarehouseProductDetail warehouseProductDetail = new WarehouseProductDetail();
			warehouseProductDetail.setDate((Date) map.get(DATE));
			warehouseProductDetail.setWarehouseProduct(warehouseProduct);
			warehouseProductDetail.setDesc((String) map.get(DESC));
			warehouseProductDetail.setPrevStock(Double.valueOf(df.format(warehouseProduct.getStock())));
			warehouseProductDetail.setTxnStock((Double) map.get(QTY));
			warehouseProductDetail.setOrderNo(
					!StringUtil.isEmpty(warehouseProduct.getOrderNo()) ? warehouseProduct.getOrderNo() : "");
			warehouseProductDetail.setUserName(
					!StringUtil.isEmpty(warehouseProduct.getUserName()) ? warehouseProduct.getUserName() : "");
			warehouseProductDetail.setVendorId(
					!StringUtil.isEmpty(warehouseProduct.getVendorId()) ? warehouseProduct.getVendorId() : "");
			warehouseProductDetail.setDamagedQty(Double.valueOf(warehouseProduct.getDamagedQty()));
			warehouseProductDetail.setCostPrice(Double.valueOf(warehouseProduct.getCostPrice()));
			// warehouseProductDetail.setVariety(warehouseProduct.getVariety());

			if (flag) {
				warehouseProductDetail
						.setFinalStock(warehouseProductDetail.getPrevStock() + warehouseProductDetail.getTxnStock());
			} else {

				double finalstock = warehouseProductDetail.getPrevStock() - warehouseProductDetail.getTxnStock();
				warehouseProductDetail.setFinalStock(Double.valueOf(df.format(finalstock)));

			}
			warehouseProductDetail.setReceiptNo((String) map.get(RECEIPT_NO));
			/** SAVING WAREHOUSE PRODUCT DETAIL **/
			productDistributionDAO.save(warehouseProductDetail);
			/** UPDATING WAREHOUSE PRODUCT **/

			if (map.get(DESC).equals(Distribution.PRODUCT_DISTRIBUTION_TO_FIELDSTAFF_DESCRIPTION)) {
				warehouseProduct.setCostPrice(Double.valueOf(map.get(COSTPRICE).toString()));
				warehouseProduct.setSeasonCode(map.get(SEASON_CODE).toString());
			}
			warehouseProduct.setStock(warehouseProductDetail.getFinalStock());
			warehouseProduct.setRevisionNo(DateUtil.getRevisionNumber());

			productDistributionDAO.saveOrUpdate(warehouseProduct);
		}

	}

	private void updateWarehouseStocks(WarehouseProduct warehouseProduct, Map<String, Object> map, boolean flag) {
		DecimalFormat df = new DecimalFormat("0.0000");

		if (!ObjectUtil.isEmpty(warehouseProduct)) {
			WarehouseProductDetail warehouseProductDetail = new WarehouseProductDetail();
			warehouseProductDetail.setDate((Date) map.get(DATE));
			warehouseProductDetail.setWarehouseProduct(warehouseProduct);
			warehouseProductDetail.setDesc((String) map.get(DESC));
			warehouseProductDetail.setPrevStock(Double.valueOf(df.format(warehouseProduct.getStock())));
			warehouseProductDetail.setTxnStock((Double) map.get(QTY));
			warehouseProductDetail.setOrderNo(
					!StringUtil.isEmpty(warehouseProduct.getOrderNo()) ? warehouseProduct.getOrderNo() : "");
			warehouseProductDetail.setUserName(
					!StringUtil.isEmpty(warehouseProduct.getUserName()) ? warehouseProduct.getUserName() : "");
			warehouseProductDetail.setVendorId(
					!StringUtil.isEmpty(warehouseProduct.getVendorId()) ? warehouseProduct.getVendorId() : "");
			warehouseProductDetail.setDamagedQty(Double.valueOf(warehouseProduct.getDamagedQty()));
			warehouseProductDetail.setCostPrice(Double.valueOf(warehouseProduct.getCostPrice()));

			if (flag) {
				warehouseProductDetail
						.setFinalStock(warehouseProductDetail.getPrevStock() + warehouseProductDetail.getTxnStock());
			} else {

				double finalstock = warehouseProductDetail.getPrevStock() - warehouseProductDetail.getTxnStock();
				warehouseProductDetail.setFinalStock(Double.valueOf(df.format(finalstock)));

			}
			warehouseProductDetail.setReceiptNo((String) map.get(RECEIPT_NO));
			/** SAVING WAREHOUSE PRODUCT DETAIL **/
			// productDistributionDAO.save(warehouseProductDetail);
			/** UPDATING WAREHOUSE PRODUCT **/

			if (map.get(DESC).equals(Distribution.PRODUCT_DISTRIBUTION_TO_FIELDSTAFF_DESCRIPTION)) {
				warehouseProduct.setCostPrice(Double.valueOf(map.get(COSTPRICE).toString()));
				warehouseProduct.setSeasonCode(map.get(SEASON_CODE).toString());
			}
			warehouseProduct.setStock(warehouseProductDetail.getFinalStock());
			warehouseProduct.setRevisionNo(DateUtil.getRevisionNumber());

			productDistributionDAO.saveOrUpdate(warehouseProduct);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findAgentAvailableStock (java.lang.String, long)
	 */
	public WarehouseProduct findAgentAvailableStock(String agentId, long productId) {

		return productDistributionDAO.findAgentAvailableStock(agentId, productId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService #
	 * findWarehouseProductAvailableStock(java.lang.String, long)
	 */
	public WarehouseProduct findWarehouseProductAvailableStock(String agentId, long productId) {

		return productDistributionDAO.findWarehouseProductAvailableStock(agentId, productId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #addProcurementMTNT (com.sourcetrace.eses.order.entity.txn.PMT)
	 */
	public String addProcurementMTNT(PMT pmt) {

		if (!ObjectUtil.isEmpty(pmt) && !ObjectUtil.isListEmpty(pmt.getPmtDetails())) {
			if (StringUtil.isEmpty(pmt.getMtntReceiptNumber())) {
				pmt.setMtntReceiptNumber(idGenerator.getMTNTReceiptNoSeq());
				productDistributionDAO.save(pmt);
				processCityWarehouse(pmt);
				return pmt.getMtntReceiptNumber();
			}
			productDistributionDAO.saveOrUpdate(pmt);
			processCityWarehouse(pmt);

		}
		return null;
	}

	public String addProcurementMTNT(PMT pmt, String tenantId) {

		if (!ObjectUtil.isEmpty(pmt) && !ObjectUtil.isListEmpty(pmt.getPmtDetails())) {
			if (StringUtil.isEmpty(pmt.getMtntReceiptNumber())) {
				pmt.setMtntReceiptNumber(idGenerator.getMTNTReceiptNoSeq());
				productDistributionDAO.save(pmt, tenantId);
				processCityWarehouse(pmt, tenantId);
				return pmt.getMtntReceiptNumber();
			}
			productDistributionDAO.saveOrUpdate(pmt, tenantId);
			processCityWarehouse(pmt, tenantId);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #editWarehouseProducts (com.ese.entity.profile.WarehouseProduct)
	 */
	public void editWarehouseProducts(WarehouseProduct warehouseProduct) {

		processWarehouseProducts(warehouseProduct);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #processCityWarehouse (java.lang.Object)
	 */

	public void processCityWarehouseTraceabilty(ProcurementTraceability object, AgroTransaction existingAgroTxn) {
		HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
		String tenant = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID) ? ISecurityFilter.DEFAULT_TENANT_ID : "";
		if (!ObjectUtil.isEmpty(request)) {
			tenant = !StringUtil.isEmpty((String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID))
					? (String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID) : "";
		}
		CityWarehouse cityWarehouse = null;
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if (object instanceof ProcurementTraceability) {
			ProcurementTraceability procurement = (ProcurementTraceability) object;
			map.put("DATE", new Date());
			map.put("TYPE", CityWarehouseDetail.TYPE.PROCUREMENT.ordinal());
			map.put("REFERENCE_ID", procurement.getId());
			map.put("DESCRIPTION", CityWarehouseDetail.TYPE.PROCUREMENT.toString());

			{
				for (ProcurementTraceabilityDetails procurementDetail : procurement
						.getProcurmentTraceabilityDetails()) {
					map.put("TXN_NO_OF_BAGS", procurementDetail.getNumberOfBags());
					map.put("TXN_GROSS_WEIGHT", procurementDetail.getNetWeight());
					if (existingAgroTxn.getDeviceId().equalsIgnoreCase(AgroTransaction.NOT_APPLICABLE)) {

						if (!ObjectUtil.isEmpty(procurementDetail.getProcuremntGrade())) {
							cityWarehouse = productDistributionDAO.findCityWarehouseByCoOperative(
									existingAgroTxn.getWarehouse().getId(),
									procurementDetail.getProcurementProduct().getId(), existingAgroTxn.getAgentId(),
									procurementDetail.getProcuremntGrade().getCode());
						} else {
							cityWarehouse = productDistributionDAO.findCityWarehouseByCoOperative(
									existingAgroTxn.getWarehouse().getId(),
									procurementDetail.getProcurementProduct().getId(), existingAgroTxn.getAgentId(),
									procurementDetail.getProcurementProduct().getCode());
						}
					} else {
						cityWarehouse = productDistributionDAO.findCityWarehouseByVillage(
								procurement.getFarmer().getVillage().getId(),
								procurementDetail.getProcurementProduct().getId(), existingAgroTxn.getAgentId(),
								procurementDetail.getProcuremntGrade().getCode());
					}

					if (ObjectUtil.isEmpty(cityWarehouse)) {
						cityWarehouse = new CityWarehouse();
						cityWarehouse.setProcurementProduct(procurementDetail.getProcurementProduct());
						cityWarehouse.setAgentId(existingAgroTxn.getAgentId());

						if (!ObjectUtil.isEmpty(procurementDetail.getProcuremntGrade())) {
							cityWarehouse.setQuality(procurementDetail.getProcuremntGrade().getCode());
						} else {
							cityWarehouse.setQuality(procurementDetail.getProcurementProduct().getCode());
						}
						cityWarehouse.setIsDelete(CityWarehouse.NOT_DELETED);

						if (existingAgroTxn.getDeviceId().equalsIgnoreCase(AgroTransaction.NOT_APPLICABLE)) {
							cityWarehouse.setCoOperative(existingAgroTxn.getWarehouse());
						} else {
							cityWarehouse.setVillage(procurement.getFarmer().getVillage());
						}
					}
					cityWarehouse.setBranchId(procurement.getBranchId());
					cityWarehouse.setBatchNo(null);
					cityWarehouse
							.setFarmer(!ObjectUtil.isEmpty(procurement.getFarmer()) ? procurement.getFarmer() : null);
					processCityWarehouseDetail(cityWarehouse, map, true);
				}
			}
		}
	}

	public void processCityWarehouse(Object object) {
		HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
		String tenant = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID) ? ISecurityFilter.DEFAULT_TENANT_ID : "";
		if (!ObjectUtil.isEmpty(request)) {
			tenant = !StringUtil.isEmpty((String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID))
					? (String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID) : "";
		}
		CityWarehouse cityWarehouse = null;
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof Procurement) { // to process Procurement
				Procurement procurement = (Procurement) object;
				map.put("DATE", new Date());
				map.put("TYPE", CityWarehouseDetail.TYPE.PROCUREMENT.ordinal());
				map.put("REFERENCE_ID", procurement.getId());
				map.put("DESCRIPTION", CityWarehouseDetail.TYPE.PROCUREMENT.toString());

				/*
				 * Agent agent =
				 * agentDAO.findAgentByProfileId(procurement.getAgroTransaction(
				 * ).getAgentId()); if (!ObjectUtil.isEmpty(agent))
				 */
				{
					for (ProcurementDetail procurementDetail : procurement.getProcurementDetails()) {
						map.put("TXN_NO_OF_BAGS", procurementDetail.getNumberOfBags());
						if(!tenant.equalsIgnoreCase("olivado")){
							map.put("TXN_GROSS_WEIGHT", procurementDetail.getGrossWeight());
						}else{
							map.put("TXN_GROSS_WEIGHT", procurementDetail.getNetWeight());
						}
						

						if (procurement.getAgroTransaction().getDeviceId()
								.equalsIgnoreCase(AgroTransaction.NOT_APPLICABLE)) {

							if (!ObjectUtil.isEmpty(procurementDetail.getProcurementGrade())) {
								cityWarehouse = productDistributionDAO.findCityWarehouseByCoOperative(
										procurement.getAgroTransaction().getWarehouse().getId(),
										procurementDetail.getProcurementProduct().getId(),
										procurement.getAgroTransaction().getAgentId(),
										procurementDetail.getProcurementGrade().getCode());
							} else {
								cityWarehouse = productDistributionDAO.findCityWarehouseByCoOperative(
										procurement.getAgroTransaction().getWarehouse().getId(),
										procurementDetail.getProcurementProduct().getId(),
										procurement.getAgroTransaction().getAgentId(),
										procurementDetail.getProcurementProduct().getCode());
							}
						} else {
							cityWarehouse = productDistributionDAO.findCityWarehouseByVillage(
									procurement.getVillage().getId(), procurementDetail.getProcurementProduct().getId(),
									procurement.getAgroTransaction().getAgentId(),
									procurementDetail.getProcurementGrade().getCode());
						}

						if (ObjectUtil.isEmpty(cityWarehouse)) {
							cityWarehouse = new CityWarehouse();
							/*
							 * cityWarehouse.setCity(procurement.getVillage()
							 * .getCity());
							 */
							cityWarehouse.setProcurementProduct(procurementDetail.getProcurementProduct());
							cityWarehouse.setAgentId(procurement.getAgroTransaction().getAgentId());

							if (!ObjectUtil.isEmpty(procurementDetail.getProcurementGrade())) {
								cityWarehouse.setQuality(procurementDetail.getProcurementGrade().getCode());
							} else {
								cityWarehouse.setQuality(procurementDetail.getProcurementProduct().getCode());
							}
							cityWarehouse.setIsDelete(CityWarehouse.NOT_DELETED);

							if (procurement.getAgroTransaction().getDeviceId()
									.equalsIgnoreCase(AgroTransaction.NOT_APPLICABLE)) {
								cityWarehouse.setCoOperative(procurement.getAgroTransaction().getWarehouse());
							} else {
								cityWarehouse.setVillage(procurement.getVillage());
							}
						}
						cityWarehouse.setBranchId(procurement.getBranchId());
						cityWarehouse.setBatchNo(!ObjectUtil.isEmpty(procurementDetail.getBatchNo())
								? procurementDetail.getBatchNo() : "");
						cityWarehouse.setFarmer(
								!ObjectUtil.isEmpty(procurement.getFarmer()) ? procurement.getFarmer() : null);
						processCityWarehouseDetail(cityWarehouse, map, true);
					}
				}
			}
			if (object instanceof ProcurementTraceability) {
				ProcurementTraceability procurement = (ProcurementTraceability) object;
				map.put("DATE", new Date());
				map.put("TYPE", CityWarehouseDetail.TYPE.PROCUREMENT.ordinal());
				map.put("REFERENCE_ID", procurement.getId());
				map.put("DESCRIPTION", CityWarehouseDetail.TYPE.PROCUREMENT.toString());

				{
					for (ProcurementTraceabilityDetails procurementDetail : procurement
							.getProcurmentTraceabilityDetails()) {
						map.put("TXN_NO_OF_BAGS", procurementDetail.getNumberOfBags());
						map.put("TXN_GROSS_WEIGHT", procurementDetail.getNetWeight());
						if (procurement.getAgroTransaction().getDeviceId()
								.equalsIgnoreCase(AgroTransaction.NOT_APPLICABLE)) {

							if (!ObjectUtil.isEmpty(procurementDetail.getProcuremntGrade())) {
								cityWarehouse = productDistributionDAO.findCityWarehouseByCoOperative(
										procurement.getAgroTransaction().getWarehouse().getId(),
										procurementDetail.getProcurementProduct().getId(),
										procurement.getAgroTransaction().getAgentId(),
										procurementDetail.getProcuremntGrade().getCode());
							} else {
								cityWarehouse = productDistributionDAO.findCityWarehouseByCoOperative(
										procurement.getAgroTransaction().getWarehouse().getId(),
										procurementDetail.getProcurementProduct().getId(),
										procurement.getAgroTransaction().getAgentId(),
										procurementDetail.getProcurementProduct().getCode());
							}
						} else {
							cityWarehouse = productDistributionDAO.findCityWarehouseByVillage(
									procurement.getFarmer().getVillage().getId(),
									procurementDetail.getProcurementProduct().getId(),
									procurement.getAgroTransaction().getAgentId(),
									procurementDetail.getProcuremntGrade().getCode());
						}

						if (ObjectUtil.isEmpty(cityWarehouse)) {
							cityWarehouse = new CityWarehouse();
							cityWarehouse.setProcurementProduct(procurementDetail.getProcurementProduct());
							cityWarehouse.setAgentId(procurement.getAgroTransaction().getAgentId());

							if (!ObjectUtil.isEmpty(procurementDetail.getProcuremntGrade())) {
								cityWarehouse.setQuality(procurementDetail.getProcuremntGrade().getCode());
							} else {
								cityWarehouse.setQuality(procurementDetail.getProcurementProduct().getCode());
							}
							cityWarehouse.setIsDelete(CityWarehouse.NOT_DELETED);

							if (procurement.getAgroTransaction().getDeviceId()
									.equalsIgnoreCase(AgroTransaction.NOT_APPLICABLE)) {
								cityWarehouse.setCoOperative(procurement.getAgroTransaction().getWarehouse());
							} else {
								cityWarehouse.setVillage(procurement.getFarmer().getVillage());
							}
						}
						cityWarehouse.setBranchId(procurement.getBranchId());
						cityWarehouse.setBatchNo(null);
						cityWarehouse.setFarmer(
								!ObjectUtil.isEmpty(procurement.getFarmer()) ? procurement.getFarmer() : null);
						processCityWarehouseDetail(cityWarehouse, map, true);
					}
				}
			}
			if (object instanceof PMT) {
				PMT proceurementMT = (PMT) object;
				map.put("REFERENCE_ID", proceurementMT.getId());
				if (proceurementMT.getStatusCode() == PMT.Status.MTNT.ordinal()) {// for
					// MTNT
					map.put("DATE", new Date());
					map.put("TYPE", CityWarehouseDetail.TYPE.PROCUREMENT_MTNT.ordinal());
					map.put("DESCRIPTION", CityWarehouseDetail.TYPE.PROCUREMENT_MTNT.toString());
					Agent agent = proceurementMT.getAgentRef();
					if (!ObjectUtil.isEmpty(agent)) {
						for (PMTDetail pmtDetail : proceurementMT.getPmtDetails()) {
							map.put("TXN_NO_OF_BAGS", pmtDetail.getMtntNumberOfBags());
							map.put("TXN_GROSS_WEIGHT", pmtDetail.getMtntGrossWeight());
							if (pmtDetail.getVillage() == null) {
								if (!tenant.equalsIgnoreCase("kpf")&&!tenant.equalsIgnoreCase("wub")&&!tenant.equalsIgnoreCase("gar")&&!tenant.equalsIgnoreCase("simfed")) {
									cityWarehouse = productDistributionDAO.findCityWarehouseByCoOperative(
											proceurementMT.getCoOperative().getId(),
											pmtDetail.getProcurementProduct().getId(), agent.getProfileId(),
											pmtDetail.getProcurementGrade().getCode());
								} else {
									cityWarehouse = productDistributionDAO.findSupplierWarehouseByCoOperative(
											proceurementMT.getCoOperative().getId(),
											pmtDetail.getProcurementProduct().getId(),
											pmtDetail.getProcurementGrade().getCode(), agent.getProfileId());
								}
							} else {
								cityWarehouse = productDistributionDAO.findCityWarehouseByVillage(
										pmtDetail.getVillage().getId(), pmtDetail.getProcurementProduct().getId(),
										agent.getProfileId(), pmtDetail.getProcurementGrade().getCode());
							}

							if (!ObjectUtil.isEmpty(cityWarehouse)) {
								processCityWarehouseDetail(cityWarehouse, map, false);
							}

						}
					} else if (ObjectUtil.isEmpty(agent)) {
						for (PMTDetail pmtDetail : proceurementMT.getPmtDetails()) {
							map.put("TXN_NO_OF_BAGS", pmtDetail.getMtntNumberOfBags());
							map.put("TXN_GROSS_WEIGHT", pmtDetail.getMtntGrossWeight());
							if (!tenant.equalsIgnoreCase("kpf")&&!tenant.equalsIgnoreCase("wub")&&!tenant.equalsIgnoreCase("gar")&&!tenant.equalsIgnoreCase("simfed")) {
								cityWarehouse = productDistributionDAO
										.findCityWareHouseByWarehouseIdProcrmentGradeCodeAndProcurementProductId(
												proceurementMT.getCoOperative().getId(),
												pmtDetail.getProcurementGrade().getCode(),
												pmtDetail.getProcurementProduct().getId());
							} else {
								cityWarehouse = productDistributionDAO
										.findSupplierWarehouseByCoOperativeProductAndGrade(
												proceurementMT.getCoOperative().getId(),
												pmtDetail.getProcurementProduct().getId(),
												pmtDetail.getProcurementGrade().getCode());
							}
							if (!ObjectUtil.isEmpty(cityWarehouse)) {
								processCityWarehouseDetail(cityWarehouse, map, false);
							}

						}

					}
				} else if (proceurementMT.getStatusCode() == PMT.Status.MTNR.ordinal()
						|| proceurementMT.getStatusCode() == PMT.Status.COMPLETE.ordinal()) {// for

					map.put("DATE", new Date());
					map.put("TYPE", CityWarehouseDetail.TYPE.PROCUREMENT_MTNR.ordinal());
					map.put("DESCRIPTION", CityWarehouseDetail.TYPE.PROCUREMENT_MTNR.toString());

					if (!ObjectUtil.isEmpty(proceurementMT.getMtnrTransferInfo())
							&& !StringUtil.isEmpty(proceurementMT.getMtnrTransferInfo().getAgentId())) {
						String agentId = proceurementMT.getMtnrTransferInfo().getAgentId();
						for (PMTDetail pmtDetail : proceurementMT.getPmtDetails()) {
							map.put("TXN_NO_OF_BAGS", pmtDetail.getMtnrNumberOfBags());
							map.put("TXN_GROSS_WEIGHT", pmtDetail.getMtnrGrossWeight());
							cityWarehouse = productDistributionDAO.findCityWarehouseByCoOperative(
									proceurementMT.getCoOperative().getId(), pmtDetail.getProcurementProduct().getId(),
									agentId, pmtDetail.getProcurementGrade().getCode());
							if (ObjectUtil.isEmpty(cityWarehouse)) {
								cityWarehouse = new CityWarehouse();
								cityWarehouse.setCity(null);
								cityWarehouse.setCoOperative(proceurementMT.getCoOperative());
								cityWarehouse.setVillage(null);
								cityWarehouse.setProcurementProduct(pmtDetail.getProcurementProduct());
								cityWarehouse.setAgentId(agentId);
								cityWarehouse.setBranchId(proceurementMT.getBranchId());
								cityWarehouse.setQuality(pmtDetail.getProcurementGrade().getCode());
								cityWarehouse.setIsDelete(CityWarehouse.NOT_DELETED);
							}
							processCityWarehouseDetail(cityWarehouse, map, true);
						}
					} else if (!ObjectUtil.isEmpty(proceurementMT.getMtnrTransferInfo())
							&& StringUtil.isEmpty(proceurementMT.getMtnrTransferInfo().getAgentId())) {
						for (PMTDetail pmtDetail : proceurementMT.getPmtDetails()) {

							map.put("TXN_NO_OF_BAGS", pmtDetail.getMtnrNumberOfBags());
							map.put("TXN_GROSS_WEIGHT", pmtDetail.getMtnrGrossWeight());

							cityWarehouse = productDistributionDAO
									.findCityWareHouseByWarehouseIdProcrmentGradeCodeAndProcurementProductId(
											proceurementMT.getCoOperative().getId(),
											pmtDetail.getProcurementGrade().getCode(),
											pmtDetail.getProcurementProduct().getId());

							if (ObjectUtil.isEmpty(cityWarehouse)) {
								cityWarehouse = new CityWarehouse();
								cityWarehouse.setCity(null);
								cityWarehouse.setCoOperative(proceurementMT.getCoOperative());
								cityWarehouse.setVillage(null);
								cityWarehouse.setProcurementProduct(pmtDetail.getProcurementProduct());
								cityWarehouse.setBranchId(proceurementMT.getBranchId());
								cityWarehouse.setQuality(pmtDetail.getProcurementGrade().getCode());
								cityWarehouse.setIsDelete(CityWarehouse.NOT_DELETED);
							}
							processCityWarehouseDetail(cityWarehouse, map, true);
						}
					}

				}
			}
			if (object instanceof SupplierProcurement) { // to process
															// Procurement
				SupplierProcurement supplierProcurement = (SupplierProcurement) object;
				map.put("DATE", new Date());
				map.put("TYPE", CityWarehouseDetail.TYPE.SUPPLIER_PROCUREMENT.ordinal());
				map.put("REFERENCE_ID", supplierProcurement.getId());
				map.put("DESCRIPTION", CityWarehouseDetail.TYPE.SUPPLIER_PROCUREMENT.toString());

				/*
				 * Agent agent =
				 * agentDAO.findAgentByProfileId(procurement.getAgroTransaction(
				 * ).getAgentId()); if (!ObjectUtil.isEmpty(agent))
				 */
				{
					for (SupplierProcurementDetail supplierProcurementDetail : supplierProcurement
							.getSupplierProcurementDetails()) {
						map.put("TXN_NO_OF_BAGS", supplierProcurementDetail.getNumberOfBags());
						map.put("TXN_GROSS_WEIGHT", supplierProcurementDetail.getGrossWeight());

						/*
						 * if
						 * (supplierProcurement.getAgroTransaction().getDeviceId
						 * () .equalsIgnoreCase(AgroTransaction.NOT_APPLICABLE))
						 * {
						 */
						if (!ObjectUtil.isEmpty(supplierProcurementDetail.getProcurementGrade())) {
							cityWarehouse = productDistributionDAO.findSupplierWarehouseByCoOperativeProductAndGrade(
									supplierProcurement.getAgroTransaction().getWarehouse().getId(),
									supplierProcurementDetail.getProcurementProduct().getId(),
									supplierProcurementDetail.getProcurementGrade().getCode());
						} /*
							 * else { cityWarehouse = productDistributionDAO.
							 * findSupplierWarehouseByCoOperative(
							 * supplierProcurement.getAgroTransaction().
							 * getWarehouse().getId(),
							 * supplierProcurementDetail.getProcurementProduct()
							 * .getId(),
							 * supplierProcurementDetail.getProcurementProduct()
							 * .getCode()); }
							 */

						if (ObjectUtil.isEmpty(cityWarehouse)) {
							cityWarehouse = new CityWarehouse();
							/*
							 * cityWarehouse.setCity(procurement.getVillage()
							 * .getCity());
							 */
							cityWarehouse.setProcurementProduct(supplierProcurementDetail.getProcurementProduct());
							cityWarehouse.setAgentId(supplierProcurement.getAgroTransaction().getAgentId());

							if (!ObjectUtil.isEmpty(supplierProcurementDetail.getProcurementGrade())) {
								cityWarehouse.setQuality(supplierProcurementDetail.getProcurementGrade().getCode());
							} else {
								cityWarehouse.setQuality(supplierProcurementDetail.getProcurementProduct().getCode());
							}
							cityWarehouse.setIsDelete(CityWarehouse.NOT_DELETED);
							Warehouse warehouse = locationDAO
									.findWarehouseById(Long.valueOf(supplierProcurement.getWarehouseId()));
							cityWarehouse.setCoOperative(warehouse);
							/*
							 * if (supplierProcurement.getAgroTransaction().
							 * getDeviceId()
							 * .equalsIgnoreCase(AgroTransaction.NOT_APPLICABLE)
							 * ) {
							 */
							// cityWarehouse.setCoOperative(supplierProcurement.getWarehouseId());
							/*
							 * } else {
							 * cityWarehouse.setVillage(supplierProcurement.
							 * getVillage()); }
							 */
						}
						cityWarehouse.setVillage(null);
						cityWarehouse.setBranchId(supplierProcurement.getBranchId());
						cityWarehouse.setBatchNo(!ObjectUtil.isEmpty(supplierProcurementDetail.getBatchNo())
								? supplierProcurementDetail.getBatchNo() : "");
						cityWarehouse.setFarmer(null);
						processCityWarehouseDetail(cityWarehouse, map, true);
					}
				}
			}if (object instanceof ColdStorage) { // to process Procurement
				ColdStorage coldStorage = (ColdStorage) object;
				map.put("DATE", new Date());
				map.put("TYPE", CityWarehouseDetail.TYPE.COLD_STORAGE.ordinal());
				map.put("REFERENCE_ID", coldStorage.getId());
				map.put("DESCRIPTION", CityWarehouseDetail.TYPE.COLD_STORAGE.toString());
				map.put("BATCH_NO", coldStorage.getBatchNo());
				
					for (ColdStorageDetail coldStorageDetail : coldStorage.getColdStorageDetail()) {
						map.put("TXN_NO_OF_BAGS", coldStorageDetail.getNoOfBags());
						map.put("TXN_GROSS_WEIGHT", coldStorageDetail.getTxnQty());
						map.put("BLOCK_NAME", coldStorageDetail.getBlockName());
						map.put("FLOOR_NAME", coldStorageDetail.getFloorName());
						map.put("BAY_NUMBER", coldStorageDetail.getBayNumber());
							if (!ObjectUtil.isEmpty(coldStorageDetail.getProcurementVariety())) {
								cityWarehouse = productDistributionDAO.findCityWarehouseByWarehouseProductBatchNoGradeFarmer(
										coldStorage.getWarehouse().getId(),
										coldStorageDetail.getProcurementVariety().getProcurementProduct().getId(),	
										coldStorage.getBatchNo(),
										coldStorageDetail.getProcurementVariety().getCode(),
										coldStorage.getColdStorageName(),
										coldStorageDetail.getBlockName(),
										coldStorageDetail.getFloorName(),
										coldStorageDetail.getBayNumber(),
										coldStorage.getFarmer().getId()
										);
							}

						if (ObjectUtil.isEmpty(cityWarehouse)) {
							cityWarehouse = new CityWarehouse();
							cityWarehouse.setCoOperative(coldStorage.getWarehouse());
							cityWarehouse.setProcurementProduct(coldStorageDetail.getProcurementVariety().getProcurementProduct());
							if (!ObjectUtil.isEmpty(coldStorageDetail.getProcurementVariety())) {
								cityWarehouse.setQuality(coldStorageDetail.getProcurementVariety().getCode());
							}
							cityWarehouse.setIsDelete(CityWarehouse.NOT_DELETED);
							cityWarehouse.setColdStorageName(coldStorage.getColdStorageName());
							cityWarehouse.setFarmer(coldStorage.getFarmer());
							//cityWarehouse.setGrossWeight(coldStorage.getTotalQty());
							
						}
						cityWarehouse.setBranchId(coldStorage.getBranchId());
						cityWarehouse.setRevisionNo(Long.valueOf(coldStorage.getRevisionNo()));
						
						processCityWarehouseDetail(cityWarehouse, map, true);
					}
				
			}if (object instanceof ColdStorageStockTransfer) { 
				ColdStorageStockTransfer coldStorageStockTransfer = (ColdStorageStockTransfer) object;
				map.put("DATE", new Date());
				map.put("TYPE", CityWarehouseDetail.TYPE.COLD_STORAGE_STOCK_TRANSFER.ordinal());
				map.put("REFERENCE_ID", coldStorageStockTransfer.getId());
				map.put("DESCRIPTION", CityWarehouseDetail.TYPE.COLD_STORAGE_STOCK_TRANSFER.toString());
			//	map.put("BATCH_NO", coldStorageStockTransfer.getBatchNo());		

					for (ColdStorageStockTransferDetail coldStorageStockTransferDetail : coldStorageStockTransfer.getColdStorageStockTransferDetail()) {
						map.put("TXN_NO_OF_BAGS", coldStorageStockTransferDetail.getNoOfBags());
						map.put("TXN_GROSS_WEIGHT", coldStorageStockTransferDetail.getQty());
						map.put("BLOCK_NAME", coldStorageStockTransferDetail.getBlockName());
						map.put("FLOOR_NAME", coldStorageStockTransferDetail.getFloorName());
						map.put("BAY_NUMBER", coldStorageStockTransferDetail.getBayNumber());
						map.put("BATCH_NO", coldStorageStockTransferDetail.getBatchNo());
						cityWarehouse = productDistributionDAO.findCityWarehouseByWarehouseProductBatchNoGradeFarmer(
								coldStorageStockTransfer.getWarehouse().getId(),
								coldStorageStockTransferDetail.getProcurementVariety().getProcurementProduct().getId(),	
								coldStorageStockTransferDetail.getBatchNo(),
								coldStorageStockTransferDetail.getProcurementVariety().getCode(),
								coldStorageStockTransfer.getColdStorageName(),
								coldStorageStockTransferDetail.getBlockName(),
								coldStorageStockTransferDetail.getFloorName(),
								coldStorageStockTransferDetail.getBayNumber(),
								coldStorageStockTransferDetail.getFarmer().getId()
								
								);
						

						if (!ObjectUtil.isEmpty(cityWarehouse)) {
							processCityWarehouseDetail(cityWarehouse, map, false);
						}

					
				
				}
			}

		}
	}
	
	public void processCityWarehouse(Object object, String tenantId) {

		CityWarehouse cityWarehouse = null;
		HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
		String tenant = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID) ? ISecurityFilter.DEFAULT_TENANT_ID : "";
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof Procurement) { // to process Procurement
				Procurement procurement = (Procurement) object;
				map.put("DATE", new Date());
				map.put("TYPE", CityWarehouseDetail.TYPE.PROCUREMENT.ordinal());
				map.put("REFERENCE_ID", procurement.getId());
				map.put("DESCRIPTION", CityWarehouseDetail.TYPE.PROCUREMENT.toString());

				Agent agent = agentDAO.findAgentByProfileId(procurement.getAgroTransaction().getAgentId(), tenantId);
				if (!ObjectUtil.isEmpty(agent)) {
					for (ProcurementDetail procurementDetail : procurement.getProcurementDetails()) {
						map.put("TXN_NO_OF_BAGS", procurementDetail.getNumberOfBags());
						map.put("TXN_GROSS_WEIGHT", procurementDetail.getGrossWeight());

						// Finding City warehouse by Agent type
						/*
						 * if (agent.isCoOperativeManager()) { if
						 * (!ObjectUtil.isEmpty(agent.getCooperative()))
						 * cityWarehouse = productDistributionDAO
						 * .findCityWarehouseByCoOperative(agent.getCooperative(
						 * ) .getId(), procurementDetail.getProcurementProduct()
						 * .getId(), procurement.getAgroTransaction()
						 * .getAgentId(), procurementDetail.getQuality()); }
						 */
						if (procurement.getAgroTransaction().getDeviceId()
								.equalsIgnoreCase(AgroTransaction.NOT_APPLICABLE)) {
							cityWarehouse = productDistributionDAO.findCityWarehouseByCoOperative(
									procurement.getAgroTransaction().getWarehouse().getId(),
									procurementDetail.getProcurementProduct().getId(),
									procurement.getAgroTransaction().getAgentId(),
									procurementDetail.getProcurementGrade().getCode(), tenantId);
						} else {
							cityWarehouse = productDistributionDAO.findCityWarehouseByVillage(
									procurement.getVillage().getId(), procurementDetail.getProcurementProduct().getId(),
									procurement.getAgroTransaction().getAgentId(),
									procurementDetail.getProcurementGrade().getCode(), tenantId);
						}

						if (ObjectUtil.isEmpty(cityWarehouse)) {
							cityWarehouse = new CityWarehouse();
							/*
							 * cityWarehouse.setCity(procurement.getVillage()
							 * .getCity());
							 */
							cityWarehouse.setProcurementProduct(procurementDetail.getProcurementProduct());
							cityWarehouse.setAgentId(procurement.getAgroTransaction().getAgentId());
							cityWarehouse.setQuality(procurementDetail.getProcurementGrade().getCode());
							cityWarehouse.setIsDelete(CityWarehouse.NOT_DELETED);
							cityWarehouse.setQuality(procurementDetail.getProcurementGrade().getCode());
							cityWarehouse.setBranchId(procurement.getBranchId());
							if (procurement.getAgroTransaction().getDeviceId()
									.equalsIgnoreCase(AgroTransaction.NOT_APPLICABLE)) {
								cityWarehouse.setCoOperative(procurement.getAgroTransaction().getWarehouse());
							} else {
								cityWarehouse.setVillage(procurement.getVillage());
							}
						}

						processCityWarehouseDetail(cityWarehouse, map, true, tenantId);
					}
				}
			}
			if (object instanceof PMT) {
				PMT proceurementMT = (PMT) object;
				map.put("REFERENCE_ID", proceurementMT.getId());
				if (proceurementMT.getStatusCode() == PMT.Status.MTNT.ordinal()) {// for
					// MTNT
					map.put("DATE", new Date());
					map.put("TYPE", CityWarehouseDetail.TYPE.PROCUREMENT_MTNT.ordinal());
					map.put("DESCRIPTION", CityWarehouseDetail.TYPE.PROCUREMENT_MTNT.toString());
					Agent agent = proceurementMT.getAgentRef();
					if (!ObjectUtil.isEmpty(agent)) {
						for (PMTDetail pmtDetail : proceurementMT.getPmtDetails()) {
							map.put("TXN_NO_OF_BAGS", pmtDetail.getMtntNumberOfBags());
							map.put("TXN_GROSS_WEIGHT", pmtDetail.getMtntGrossWeight());
							if (pmtDetail.getVillage() == null) {
								if (!tenant.equalsIgnoreCase("kpf") &&!tenant.equalsIgnoreCase("wub") && !tenant.equalsIgnoreCase("gar")&&!tenant.equalsIgnoreCase("simfed")) {
									cityWarehouse = productDistributionDAO.findCityWarehouseByCoOperative(
											proceurementMT.getCoOperative().getId(),
											pmtDetail.getProcurementProduct().getId(), agent.getProfileId(),
											pmtDetail.getProcurementGrade().getCode(), tenantId);
								} else {
									cityWarehouse = productDistributionDAO.findSupplierWarehouseByCoOperative(
											proceurementMT.getCoOperative().getId(),
											pmtDetail.getProcurementProduct().getId(),
											pmtDetail.getProcurementGrade().getCode(), agent.getProfileId(), tenantId);
								}
							} else {
								cityWarehouse = productDistributionDAO.findCityWarehouseByVillage(
										pmtDetail.getVillage().getId(), pmtDetail.getProcurementProduct().getId(),
										agent.getProfileId(), pmtDetail.getProcurementGrade().getCode(), tenantId);
							}

							if (!ObjectUtil.isEmpty(cityWarehouse)) {
								processCityWarehouseDetail(cityWarehouse, map, false, tenantId);
							}

						}
					} else if (ObjectUtil.isEmpty(agent)) {
						for (PMTDetail pmtDetail : proceurementMT.getPmtDetails()) {
							map.put("TXN_NO_OF_BAGS", pmtDetail.getMtntNumberOfBags());
							map.put("TXN_GROSS_WEIGHT", pmtDetail.getMtntGrossWeight());
							if (!tenant.equalsIgnoreCase("kpf")&&!tenant.equalsIgnoreCase("wub") &&!tenant.equalsIgnoreCase("gar")&&!tenant.equalsIgnoreCase("simfed")) {
								cityWarehouse = productDistributionDAO
										.findCityWareHouseByWarehouseIdProcrmentGradeCodeAndProcurementProductId(
												proceurementMT.getCoOperative().getId(),
												pmtDetail.getProcurementGrade().getCode(),
												pmtDetail.getProcurementProduct().getId(), tenantId);
							} else {
								cityWarehouse = productDistributionDAO
										.findSupplierWarehouseByCoOperativeProductAndGrade(
												proceurementMT.getCoOperative().getId(),
												pmtDetail.getProcurementProduct().getId(),
												pmtDetail.getProcurementGrade().getCode(), tenantId);
							}
							if (!ObjectUtil.isEmpty(cityWarehouse)) {
								processCityWarehouseDetail(cityWarehouse, map, false, tenantId);
							}

						}

					}
				} else if (proceurementMT.getStatusCode() == PMT.Status.MTNR.ordinal()
						|| proceurementMT.getStatusCode() == PMT.Status.COMPLETE.ordinal()) {// for

					map.put("DATE", new Date());
					map.put("TYPE", CityWarehouseDetail.TYPE.PROCUREMENT_MTNR.ordinal());
					map.put("DESCRIPTION", CityWarehouseDetail.TYPE.PROCUREMENT_MTNR.toString());

					if (!ObjectUtil.isEmpty(proceurementMT.getMtnrTransferInfo())
							&& !StringUtil.isEmpty(proceurementMT.getMtnrTransferInfo().getAgentId())) {
						String agentId = proceurementMT.getMtnrTransferInfo().getAgentId();
						for (PMTDetail pmtDetail : proceurementMT.getPmtDetails()) {
							map.put("TXN_NO_OF_BAGS", pmtDetail.getMtnrNumberOfBags());
							map.put("TXN_GROSS_WEIGHT", pmtDetail.getMtnrGrossWeight());
							cityWarehouse = productDistributionDAO.findCityWarehouseByCoOperative(
									proceurementMT.getCoOperative().getId(), pmtDetail.getProcurementProduct().getId(),
									agentId, pmtDetail.getProcurementGrade().getCode(), tenantId);
							if (ObjectUtil.isEmpty(cityWarehouse)) {
								cityWarehouse = new CityWarehouse();
								cityWarehouse.setCity(null);
								cityWarehouse.setCoOperative(proceurementMT.getCoOperative());
								cityWarehouse.setVillage(null);
								cityWarehouse.setProcurementProduct(pmtDetail.getProcurementProduct());
								cityWarehouse.setAgentId(agentId);
								cityWarehouse.setBranchId(proceurementMT.getBranchId());
								cityWarehouse.setQuality(pmtDetail.getProcurementGrade().getCode());
								cityWarehouse.setIsDelete(CityWarehouse.NOT_DELETED);
							}
							processCityWarehouseDetail(cityWarehouse, map, true, tenantId);
						}
					} else if (!ObjectUtil.isEmpty(proceurementMT.getMtnrTransferInfo())
							&& StringUtil.isEmpty(proceurementMT.getMtnrTransferInfo().getAgentId())) {
						for (PMTDetail pmtDetail : proceurementMT.getPmtDetails()) {

							map.put("TXN_NO_OF_BAGS", pmtDetail.getMtnrNumberOfBags());
							map.put("TXN_GROSS_WEIGHT", pmtDetail.getMtnrGrossWeight());

							cityWarehouse = productDistributionDAO
									.findCityWareHouseByWarehouseIdProcrmentGradeCodeAndProcurementProductId(
											proceurementMT.getCoOperative().getId(),
											pmtDetail.getProcurementGrade().getCode(),
											pmtDetail.getProcurementProduct().getId(), tenantId);

							if (ObjectUtil.isEmpty(cityWarehouse)) {
								cityWarehouse = new CityWarehouse();
								cityWarehouse.setCity(null);
								cityWarehouse.setCoOperative(proceurementMT.getCoOperative());
								cityWarehouse.setVillage(null);
								cityWarehouse.setProcurementProduct(pmtDetail.getProcurementProduct());
								cityWarehouse.setBranchId(proceurementMT.getBranchId());
								cityWarehouse.setQuality(pmtDetail.getProcurementGrade().getCode());
								cityWarehouse.setIsDelete(CityWarehouse.NOT_DELETED);
							}
							processCityWarehouseDetail(cityWarehouse, map, true, tenantId);
						}
					}

				}
			}
			if (object instanceof SupplierProcurement) { // to process
															// Procurement
				SupplierProcurement supplierProcurement = (SupplierProcurement) object;
				map.put("DATE", new Date());
				map.put("TYPE", CityWarehouseDetail.TYPE.SUPPLIER_PROCUREMENT.ordinal());
				map.put("REFERENCE_ID", supplierProcurement.getId());
				map.put("DESCRIPTION", CityWarehouseDetail.TYPE.SUPPLIER_PROCUREMENT.toString());

				Agent agent = agentDAO.findAgentByProfileId(supplierProcurement.getAgroTransaction().getAgentId(),
						tenantId);
				if (!ObjectUtil.isEmpty(agent)) {
					for (SupplierProcurementDetail supplierProcurementDetail : supplierProcurement
							.getSupplierProcurementDetails()) {
						map.put("TXN_NO_OF_BAGS", supplierProcurementDetail.getNumberOfBags());
						map.put("TXN_GROSS_WEIGHT", supplierProcurementDetail.getGrossWeight());

						cityWarehouse = productDistributionDAO.findSupplierWarehouseByCoOperative(
								Long.valueOf(supplierProcurement.getWarehouseId()),
								supplierProcurementDetail.getProcurementProduct().getId(),
								supplierProcurementDetail.getProcurementGrade().getCode(),
								supplierProcurement.getAgroTransaction().getAgentId(), tenantId);

						if (ObjectUtil.isEmpty(cityWarehouse)) {
							cityWarehouse = new CityWarehouse();
							cityWarehouse.setBranchId(supplierProcurement.getBranchId());
							cityWarehouse.setProcurementProduct(supplierProcurementDetail.getProcurementProduct());
							cityWarehouse.setAgentId(supplierProcurement.getAgroTransaction().getAgentId());
							cityWarehouse.setIsDelete(SupplierWarehouse.NOT_DELETED);
							cityWarehouse.setQuality(supplierProcurementDetail.getProcurementGrade().getCode());
							Warehouse warehouse = locationDAO
									.findWarehouseById(Long.valueOf(supplierProcurement.getWarehouseId()), tenantId);
							cityWarehouse.setCoOperative(warehouse);

						}

						processCityWarehouseDetail(cityWarehouse, map, true, tenantId);
					}
				}
			}
		}
	}

	/**
	 * Process city warehouse detail.
	 * 
	 * @param cityWarehouse
	 *            the city warehouse
	 * @param map
	 *            the map
	 * @param flag
	 *            the flag
	 */
	private void processCityWarehouseDetail(CityWarehouse cityWarehouse, Map<String, Object> map, boolean flag) {
		HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
		String tenant = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID) ? ISecurityFilter.DEFAULT_TENANT_ID : "";
		if (!ObjectUtil.isEmpty(request)) {
			tenant = !StringUtil.isEmpty((String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID))
					? (String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID) : "";
		}
		CityWarehouseDetail cityWarehouseDetail = new CityWarehouseDetail();
		cityWarehouseDetail.setCityWarehouse(cityWarehouse);
		cityWarehouseDetail.setDate((Date) map.get("DATE"));
		cityWarehouseDetail.setType((Integer) map.get("TYPE"));
		cityWarehouseDetail.setReferenceId((Long) map.get("REFERENCE_ID"));
		cityWarehouseDetail.setPreviousNumberOfBags(cityWarehouse.getNumberOfBags());
		cityWarehouseDetail.setPreviousGrossWeight(cityWarehouse.getGrossWeight());
		cityWarehouseDetail.setTxnNumberOfBags((Long) map.get("TXN_NO_OF_BAGS"));
		cityWarehouseDetail.setTxnGrossWeight((Double) map.get("TXN_GROSS_WEIGHT"));
		cityWarehouseDetail.setDescription((String) map.get("DESCRIPTION"));
		if(!tenant.equalsIgnoreCase("griffith")){
			cityWarehouseDetail.setBatchNo(!ObjectUtil.isEmpty(cityWarehouse.getBatchNo()) ? cityWarehouse.getBatchNo() : "");
		}else{
			cityWarehouseDetail.setBatchNo((String) map.get("BATCH_NO"));
		}
		
		if (flag) { // Adding of procurement stock
			cityWarehouseDetail.setTotalNumberOfBags(
					cityWarehouseDetail.getPreviousNumberOfBags() + cityWarehouseDetail.getTxnNumberOfBags());
			cityWarehouseDetail.setTotalGrossWeight(
					cityWarehouseDetail.getPreviousGrossWeight() + cityWarehouseDetail.getTxnGrossWeight());
		} else { // Detecting of procurement stock
			cityWarehouseDetail.setTotalNumberOfBags(
					cityWarehouseDetail.getPreviousNumberOfBags() - cityWarehouseDetail.getTxnNumberOfBags());
			cityWarehouseDetail.setTotalGrossWeight(
					cityWarehouseDetail.getPreviousGrossWeight() - cityWarehouseDetail.getTxnGrossWeight());
		}
		if(tenant.equalsIgnoreCase("griffith")){
		cityWarehouseDetail.setBlockName((String) map.get("BLOCK_NAME"));
		cityWarehouseDetail.setFloorName((String) map.get("FLOOR_NAME"));
		cityWarehouseDetail.setBayNumber((String) map.get("BAY_NUMBER"));
		}
		productDistributionDAO.save(cityWarehouseDetail);
		cityWarehouse.setNumberOfBags(cityWarehouseDetail.getTotalNumberOfBags());
		cityWarehouse.setGrossWeight(cityWarehouseDetail.getTotalGrossWeight());
		cityWarehouse.setRevisionNo(DateUtil.getRevisionNumber());
		productDistributionDAO.saveOrUpdate(cityWarehouse);
	}

	private void processCityWarehouseDetail(CityWarehouse cityWarehouse, Map<String, Object> map, boolean flag,
			String tenantId) {

		CityWarehouseDetail cityWarehouseDetail = new CityWarehouseDetail();
		cityWarehouseDetail.setCityWarehouse(cityWarehouse);
		cityWarehouseDetail.setDate((Date) map.get("DATE"));
		cityWarehouseDetail.setType((Integer) map.get("TYPE"));
		cityWarehouseDetail.setReferenceId((Long) map.get("REFERENCE_ID"));
		cityWarehouseDetail.setPreviousNumberOfBags(cityWarehouse.getNumberOfBags());
		cityWarehouseDetail.setPreviousGrossWeight(cityWarehouse.getGrossWeight());
		cityWarehouseDetail.setTxnNumberOfBags((Long) map.get("TXN_NO_OF_BAGS"));
		cityWarehouseDetail.setTxnGrossWeight((Double) map.get("TXN_GROSS_WEIGHT"));
		cityWarehouseDetail.setDescription((String) map.get("DESCRIPTION"));
		if (flag) { // Adding of procurement stock
			cityWarehouseDetail.setTotalNumberOfBags(
					cityWarehouseDetail.getPreviousNumberOfBags() + cityWarehouseDetail.getTxnNumberOfBags());
			cityWarehouseDetail.setTotalGrossWeight(
					cityWarehouseDetail.getPreviousGrossWeight() + cityWarehouseDetail.getTxnGrossWeight());
		} else { // Detecting of procurement stock
			cityWarehouseDetail.setTotalNumberOfBags(
					cityWarehouseDetail.getPreviousNumberOfBags() - cityWarehouseDetail.getTxnNumberOfBags());
			cityWarehouseDetail.setTotalGrossWeight(
					cityWarehouseDetail.getPreviousGrossWeight() - cityWarehouseDetail.getTxnGrossWeight());
		}
		cityWarehouse.setNumberOfBags(cityWarehouseDetail.getTotalNumberOfBags());
		cityWarehouse.setGrossWeight(cityWarehouseDetail.getTotalGrossWeight());
		cityWarehouse.setRevisionNo(DateUtil.getRevisionNumber());

		productDistributionDAO.saveOrUpdateCityWarehouse(cityWarehouse, tenantId);
		productDistributionDAO.saveCityWarehouseDetail(cityWarehouseDetail, tenantId);
	}

	public void processCityWarehouse1(Object object) {

		CityWarehouse cityWarehouse = null;
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof Procurement) { // to process Procurement
				Procurement procurement = (Procurement) object;
				map.put("DATE", new Date());
				map.put("TYPE", CityWarehouseDetail.TYPE.PROCUREMENT.ordinal());
				map.put("REFERENCE_ID", procurement.getId());
				map.put("DESCRIPTION", CityWarehouseDetail.TYPE.PROCUREMENT.toString());

				/*
				 * Agent agent =
				 * agentDAO.findAgentByProfileId(procurement.getAgroTransaction(
				 * ).getAgentId()); if (!ObjectUtil.isEmpty(agent)) {
				 */
				for (ProcurementDetail procurementDetail : procurement.getProcurementDetails()) {
					map.put("TXN_NO_OF_BAGS", procurementDetail.getNumberOfBags());
					map.put("TXN_GROSS_WEIGHT", procurementDetail.getNetWeight());

					if (procurement.getAgroTransaction().getDeviceId()
							.equalsIgnoreCase(AgroTransaction.NOT_APPLICABLE)) {
						cityWarehouse = productDistributionDAO.findCityWarehouseByCoOperativeAndFarmer(
								procurement.getAgroTransaction().getWarehouse().getId(),
								procurementDetail.getProcurementProduct().getId(), procurement.getFarmer().getId(),
								procurementDetail.getProcurementGrade().getCode());
					} else {
						cityWarehouse = productDistributionDAO.findCityWarehouseByVillageAndFarmer(
								procurement.getVillage().getId(), procurementDetail.getProcurementProduct().getId(),
								procurement.getFarmer().getId(), procurementDetail.getProcurementGrade().getCode());
					}

					if (ObjectUtil.isEmpty(cityWarehouse)) {
						cityWarehouse = new CityWarehouse();
						cityWarehouse.setProcurementProduct(procurementDetail.getProcurementProduct());
						// CityWarehouse.setAgentId(procurement.getAgroTransaction().getAgentId());
						cityWarehouse.setQuality(procurementDetail.getProcurementGrade().getCode());
						cityWarehouse.setIsDelete(CityWarehouse.NOT_DELETED);
						cityWarehouse.setFarmer(
								!ObjectUtil.isEmpty(procurement.getFarmer()) ? procurement.getFarmer() : null);

						if (procurement.getAgroTransaction().getDeviceId()
								.equalsIgnoreCase(AgroTransaction.NOT_APPLICABLE)) {
							cityWarehouse.setCoOperative(procurement.getAgroTransaction().getWarehouse());
						} else {
							cityWarehouse.setVillage(procurement.getVillage());
						}
					}
					cityWarehouse.setBranchId(procurement.getBranchId());
					cityWarehouse.setBatchNo(
							!ObjectUtil.isEmpty(procurementDetail.getBatchNo()) ? procurementDetail.getBatchNo() : "");
					processCityWarehouseDetail1(cityWarehouse, map, true);
				}
				// }
			}
			if (object instanceof PMT) {
				PMT proceurementMT = (PMT) object;
				map.put("REFERENCE_ID", proceurementMT.getId());
				if (proceurementMT.getStatusCode() == PMT.Status.MTNT.ordinal()) {// for
					// MTNT
					map.put("DATE", new Date());
					map.put("TYPE", CityWarehouseDetail.TYPE.PROCUREMENT_MTNT.ordinal());
					map.put("DESCRIPTION", CityWarehouseDetail.TYPE.PROCUREMENT_MTNT.toString());
					Agent agent = proceurementMT.getAgentRef();
					if (!ObjectUtil.isEmpty(agent)) {
						for (PMTDetail pmtDetail : proceurementMT.getPmtDetails()) {
							map.put("TXN_NO_OF_BAGS", pmtDetail.getMtntNumberOfBags());
							map.put("TXN_GROSS_WEIGHT", pmtDetail.getMtntGrossWeight());
							if (pmtDetail.getVillage() == null) {
								cityWarehouse = productDistributionDAO.findCityWarehouseByCoOperative(
										proceurementMT.getCoOperative().getId(),
										pmtDetail.getProcurementProduct().getId(), agent.getProfileId(),
										pmtDetail.getProcurementGrade().getCode());
							} else {
								cityWarehouse = productDistributionDAO.findCityWarehouseByVillage(
										pmtDetail.getVillage().getId(), pmtDetail.getProcurementProduct().getId(),
										agent.getProfileId(), pmtDetail.getProcurementGrade().getCode());
							}

							if (!ObjectUtil.isEmpty(cityWarehouse)) {
								processCityWarehouseDetail(cityWarehouse, map, false);
							}

						}
					} else if (ObjectUtil.isEmpty(agent)) {
						for (PMTDetail pmtDetail : proceurementMT.getPmtDetails()) {
							map.put("TXN_NO_OF_BAGS", pmtDetail.getMtntNumberOfBags());
							map.put("TXN_GROSS_WEIGHT", pmtDetail.getMtntGrossWeight());

							cityWarehouse = productDistributionDAO
									.findCityWareHouseByWarehouseIdProcrmentGradeCodeAndProcurementProductIdAndFarmerId(
											pmtDetail.getFarmer().getId(), proceurementMT.getCoOperative().getId(),
											pmtDetail.getProcurementGrade().getCode(),
											pmtDetail.getProcurementProduct().getId());
							if (!ObjectUtil.isEmpty(cityWarehouse)) {
								processCityWarehouseDetail(cityWarehouse, map, false);
							}

						}

					}
				} else if (proceurementMT.getStatusCode() == PMT.Status.MTNR.ordinal()
						|| proceurementMT.getStatusCode() == PMT.Status.COMPLETE.ordinal()) {// for

					map.put("DATE", new Date());
					map.put("TYPE", CityWarehouseDetail.TYPE.PROCUREMENT_MTNR.ordinal());
					map.put("DESCRIPTION", CityWarehouseDetail.TYPE.PROCUREMENT_MTNR.toString());

					if (!ObjectUtil.isEmpty(proceurementMT.getMtnrTransferInfo())
							&& !StringUtil.isEmpty(proceurementMT.getMtnrTransferInfo().getAgentId())) {
						String agentId = proceurementMT.getMtnrTransferInfo().getAgentId();
						for (PMTDetail pmtDetail : proceurementMT.getPmtDetails()) {
							map.put("TXN_NO_OF_BAGS", pmtDetail.getMtnrNumberOfBags());
							map.put("TXN_GROSS_WEIGHT", pmtDetail.getMtnrGrossWeight());
							cityWarehouse = productDistributionDAO.findCityWarehouseByCoOperative(
									proceurementMT.getCoOperative().getId(), pmtDetail.getProcurementProduct().getId(),
									agentId, pmtDetail.getProcurementGrade().getCode());
							if (ObjectUtil.isEmpty(cityWarehouse)) {
								cityWarehouse = new CityWarehouse();
								cityWarehouse.setCity(null);
								cityWarehouse.setCoOperative(proceurementMT.getCoOperative());
								cityWarehouse.setVillage(null);
								cityWarehouse.setProcurementProduct(pmtDetail.getProcurementProduct());
								cityWarehouse.setAgentId(agentId);
								cityWarehouse.setBranchId(proceurementMT.getBranchId());
								cityWarehouse.setQuality(pmtDetail.getProcurementGrade().getCode());
								cityWarehouse.setIsDelete(CityWarehouse.NOT_DELETED);
							}
							processCityWarehouseDetail(cityWarehouse, map, true);
						}
					} else if (!ObjectUtil.isEmpty(proceurementMT.getMtnrTransferInfo())
							&& StringUtil.isEmpty(proceurementMT.getMtnrTransferInfo().getAgentId())) {
						for (PMTDetail pmtDetail : proceurementMT.getPmtDetails()) {

							map.put("TXN_NO_OF_BAGS", pmtDetail.getMtnrNumberOfBags());
							map.put("TXN_GROSS_WEIGHT", pmtDetail.getMtnrGrossWeight());

							cityWarehouse = productDistributionDAO
									.findCityWareHouseByWarehouseIdProcrmentGradeCodeAndProcurementProductId(
											proceurementMT.getCoOperative().getId(),
											pmtDetail.getProcurementGrade().getCode(),
											pmtDetail.getProcurementProduct().getId());

							if (ObjectUtil.isEmpty(cityWarehouse)) {
								cityWarehouse = new CityWarehouse();
								cityWarehouse.setCity(null);
								cityWarehouse.setCoOperative(proceurementMT.getCoOperative());
								cityWarehouse.setVillage(null);
								cityWarehouse.setProcurementProduct(pmtDetail.getProcurementProduct());
								cityWarehouse.setBranchId(proceurementMT.getBranchId());
								cityWarehouse.setQuality(pmtDetail.getProcurementGrade().getCode());
								cityWarehouse.setIsDelete(CityWarehouse.NOT_DELETED);
							}
							processCityWarehouseDetail(cityWarehouse, map, true);
						}
					}

				}
			}
		}
	}

	/**
	 * Process city warehouse detail.
	 * 
	 * @param cityWarehouse
	 *            the city warehouse
	 * @param map
	 *            the map
	 * @param flag
	 *            the flag
	 */
	private void processCityWarehouseDetail1(CityWarehouse cityWarehouse, Map<String, Object> map, boolean flag) {

		CityWarehouseDetail cityWarehouseDetail = new CityWarehouseDetail();

		cityWarehouseDetail.setCityWarehouse(cityWarehouse);
		cityWarehouseDetail.setDate((Date) map.get("DATE"));
		cityWarehouseDetail.setType((Integer) map.get("TYPE"));
		cityWarehouseDetail.setReferenceId((Long) map.get("REFERENCE_ID"));
		cityWarehouseDetail.setPreviousNumberOfBags(cityWarehouse.getNumberOfBags());
		cityWarehouseDetail.setPreviousGrossWeight(cityWarehouse.getGrossWeight());
		cityWarehouseDetail.setTxnNumberOfBags((Long) map.get("TXN_NO_OF_BAGS"));
		cityWarehouseDetail.setTxnGrossWeight((Double) map.get("TXN_GROSS_WEIGHT"));
		cityWarehouseDetail.setDescription((String) map.get("DESCRIPTION"));
		if (flag) { // Adding of procurement stock
			cityWarehouseDetail.setTotalNumberOfBags(
					cityWarehouseDetail.getPreviousNumberOfBags() + cityWarehouseDetail.getTxnNumberOfBags());
			cityWarehouseDetail.setTotalGrossWeight(
					cityWarehouseDetail.getPreviousGrossWeight() + cityWarehouseDetail.getTxnGrossWeight());
		} else { // Detecting of procurement stock
			cityWarehouseDetail.setTotalNumberOfBags(
					cityWarehouseDetail.getPreviousNumberOfBags() - cityWarehouseDetail.getTxnNumberOfBags());
			cityWarehouseDetail.setTotalGrossWeight(
					cityWarehouseDetail.getPreviousGrossWeight() - cityWarehouseDetail.getTxnGrossWeight());
		}
		// cityWarehouseDetail.setProcurementProduct(map.get("PROCUREMENT_PRODUCT_ID"));

		// cityWarehouseDetail.setProcurementProduct(procurementDetail.getProcurementProduct());
		// cityWarehouseDetail.setProcurementGrade(procurementDetail.getProcurementGrade());

		/*
		 * Procurement procurementObj =
		 * productDistributionDAO.findProcurementByFarmerId(cityWarehouse.
		 * getFarmer().getId()); ProcurementDetail procurementDetailObj =
		 * productDistributionDAO.findProcurementDetailByProcurementId(
		 * procurementObj.getId()); ProcurementGrade procurementGradeObj =
		 * productDistributionDAO
		 * .findProcurementGradeByProcurementGradeId(procurementDetailObj.
		 * getProcurementGrade().getId());
		 */

		productDistributionDAO.save(cityWarehouseDetail);
		cityWarehouse.setNumberOfBags(cityWarehouseDetail.getTotalNumberOfBags());
		cityWarehouse.setGrossWeight(cityWarehouseDetail.getTotalGrossWeight());
		cityWarehouse.setRevisionNo(DateUtil.getRevisionNumber());

		productDistributionDAO.saveOrUpdate(cityWarehouse);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService #
	 * findMTNTReceiptNoByReceiverWarehouse(java.lang.String)
	 */
	public List<DMT> findMTNTReceiptNoByReceiverWarehouse(String selectedWarehouse) {

		return productDistributionDAO.findMTNTReceiptNoByReceiverWarehouse(selectedWarehouse);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findMTNTDetails(long, java.lang.String)
	 */
	public DMT findMTNTDetails(String receiverWarehouseId, String selectedMTNTReceiptNo) {

		return productDistributionDAO.findMTNTDetails(receiverWarehouseId, selectedMTNTReceiptNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findMTNTProductDetails (long)
	 */
	public List<DMTDetail> findMTNTProductDetails(long dmtId) {

		return productDistributionDAO.findMTNTProductDetails(dmtId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findDMTByMTNTReceiptNo (java.lang.String)
	 */
	public DMT findDMTByMTNTReceiptNo(String selectedMTNTReceiptNo) {

		return productDistributionDAO.findDMTByMTNTReceiptNo(selectedMTNTReceiptNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findDMTProductByProductId (long, long)
	 */
	public DMTDetail findDMTProductByProductId(long productId, long dmtId) {

		return productDistributionDAO.findDMTProductByProductId(productId, dmtId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #editDMT(com.sourcetrace .eses.order.entity.txn.DMT)
	 */
	public void editDMT(DMT dmt) {

		productDistributionDAO.save(dmt.getReceiverTransferInfo());
		productDistributionDAO.update(dmt);
		processWarehouseProducts(dmt);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService #
	 * isAgentWarehouseProductStockExist(java.lang.String)
	 */
	public boolean isAgentWarehouseProductStockExist(String profileId) {

		return productDistributionDAO.isAgentWarehouseProductStockExist(profileId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #listCentralWarehouse()
	 */
	public List<Object[]> listCentralWarehouse() {

		return productDistributionDAO.listCentralWarehouse();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #listDMTDetailProductList (long, long)
	 */
	public List<DMTDetail> listDMTDetailProductList(long productId, long dmtId) {

		return productDistributionDAO.listDMTDetailProductList(productId, dmtId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService # removeAgentWarehouseProduct(long)
	 */
	public void removeAgentWarehouseProduct(long agentId) {

		List<WarehouseProduct> products = productDistributionDAO.listWarehouseProductByAgentId(agentId);
		for (WarehouseProduct product : products) {
			productDistributionDAO.delete(product);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #removeCityWarehouseProduct (java.lang.String)
	 */
	public void removeCityWarehouseProduct(String cityCode) {

		List<WarehouseProduct> products = productDistributionDAO.listWarehouseProductByCityCode(cityCode);
		for (WarehouseProduct product : products) {
			productDistributionDAO.delete(product);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService # removeWarehouseProductByProductId(long)
	 */
	public void removeWarehouseProductByProductId(long productId) {

		List<WarehouseProduct> products = productDistributionDAO.listWarehouseProductByProductId(productId);
		for (WarehouseProduct product : products) {
			productDistributionDAO.delete(product);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #saveOfflinePayment
	 * (com.sourcetrace.eses.order.entity.txn.OfflinePayment)
	 */
	public void saveOfflinePayment(OfflinePayment payment) {

		productDistributionDAO.save(payment);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #listPendingPaymentTxn ()
	 */
	public List<OfflinePayment> listPendingPaymentTxn() {

		return productDistributionDAO.listPendingPaymentTxn();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #updateOfflinePayment
	 * (com.sourcetrace.eses.order.entity.txn.OfflinePayment)
	 */
	public void updateOfflinePayment(OfflinePayment payment) {

		productDistributionDAO.update(payment);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #listDMTDetail(long)
	 */
	public List<DMTDetail> listDMTDetail(long id) {

		return productDistributionDAO.listDMTDetail(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #listSenderWarehouse()
	 */
	public List<Warehouse> listSenderWarehouse() {

		return productDistributionDAO.listSenderWarehouse();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #listMTNTReceiverWarehouse ()
	 */
	public List<Warehouse> listMTNTReceiverWarehouse() {

		return productDistributionDAO.listMTNTReceiverWarehouse();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #listMTNRReceiverWarehouse ()
	 */
	public List<Warehouse> listMTNRReceiverWarehouse() {

		return productDistributionDAO.listMTNRReceiverWarehouse();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService # listWarehouseProductForAgentWarehouse(long)
	 */
	public List<WarehouseProduct> listWarehouseProductForAgentWarehouse(long cityId) {

		return productDistributionDAO.listWarehouseProductForAgentWarehouse(cityId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findMTNTByAgentWarehouse (long)
	 */
	public List<DMT> findMTNTByAgentWarehouse(long cityId) {

		return productDistributionDAO.findMTNTByAgentWarehouse(cityId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findDMTByMTNRReceiptNo (java.lang.String)
	 */
	public DMT findDMTByMTNRReceiptNo(String mtnrReceiptNo) {

		return productDistributionDAO.findDMTByMTNRReceiptNo(mtnrReceiptNo);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #editDMTForMTNR(com .sourcetrace.eses.order.entity.txn.DMT)
	 */
	public void editDMTForMTNR(DMT mtnr) {

		productDistributionDAO.save(mtnr.getReceiverTransferInfo());
		// productDistributionDAO.update(dmt);
		processWarehouseProducts(mtnr);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #listOfPMTNTReportbyPMTId (long, java.lang.String)
	 */
	public List<Object[]> listOfPMTNTReportbyPMTId(long id, String tripsheetIds) {

		return productDistributionDAO.listOfPMTNTReportbyPMTId(id, tripsheetIds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #listTripSheetsIdsByPMTId (long, int, int)
	 */
	public List<BigInteger> listTripSheetsIdsByPMTId(long id, int startIndex, int endIndex) {

		return productDistributionDAO.listTripSheetsIdsByPMTId(id, startIndex, endIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findPMTById(java.lang .Long)
	 */
	public PMT findPMTById(Long id) {

		return productDistributionDAO.findPMTById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #listProcurementMTNT()
	 */
	public List<PMT> listProcurementMTNT() {

		return productDistributionDAO.listProcurementMTNT();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService #addTripSheet(com.
	 * sourcetrace.eses.order.entity.txn.TripSheet)
	 */
	public void addTripSheet(TripSheet tripSheet) {

		productDistributionDAO.save(tripSheet);
		for (Procurement procurement : tripSheet.getProcurements()) {
			processCityWarehouse(procurement);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #addProcurementList (java.util.Set)
	 */
	public void addProcurementList(Set<Procurement> procurements) {

		if (!ObjectUtil.isListEmpty(procurements)) {
			for (Procurement procurement : procurements) {
				// Receipt no generation only for web transaction
				/*
				 * procurement.getAgroTransaction().getRefAgroTransaction().
				 * setReceiptNo ( idGenerator.getProcurementReceiptNoSeq());
				 */
				procurement.getAgroTransaction().setReceiptNo(idGenerator.getProcurementReceiptNoSeq());
				addProcurement(procurement);
			}
		}
	}

	public void addProcurementList1(Set<Procurement> procurements) {

		if (!ObjectUtil.isListEmpty(procurements)) {
			for (Procurement procurement : procurements) {
				// Receipt no generation only for web transaction
				/*
				 * procurement.getAgroTransaction().getRefAgroTransaction().
				 * setReceiptNo ( idGenerator.getProcurementReceiptNoSeq());
				 */
				procurement.getAgroTransaction().setReceiptNo(idGenerator.getProcurementReceiptNoSeq());
				addProcurement1(procurement);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #listDMTDetail(long, int, int)
	 */
	public List<DMTDetail> listDMTDetail(long id, String type, int startIndex, int limit) {

		return productDistributionDAO.listDMTDetail(id, type, startIndex, limit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findPMTByMTNRReceiptNumber (java.lang.String)
	 */
	public PMT findPMTByMTNRReceiptNumber(String mtnrReceiptNo) {

		return productDistributionDAO.findPMTByMTNRReceiptNumber(mtnrReceiptNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService #
	 * listPricePatternByRevNoAndSeason(java.lang.Long, java.lang.String)
	 */
	public List<PricePattern> listPricePatternByRevNoAndSeason(Long revisionNo, String seasonCode) {

		return productDistributionDAO.listPricePatternByRevNoAndSeason(revisionNo, seasonCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findAgentProductDetails(long)
	 */
	public List<WarehouseProduct> findAgentProductDetails(long id) {

		return productDistributionDAO.findAgentProductDetails(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findCurrentSeason()
	 */
	public Season findCurrentSeason() {

		ESESystem preference = systemDAO.findPrefernceById(ESESystem.SYSTEM_SWITCH);
		String currentSeasonCode = preference.getPreferences().get(ESESystem.CURRENT_SEASON_CODE);
		if (!StringUtil.isEmpty(currentSeasonCode)) {
			Season currentSeason = productDistributionDAO.findSeasonBySeasonCode(currentSeasonCode);
			return currentSeason;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findCurrentSeasonCode ()
	 */
	public String findCurrentSeasonCode() {

		ESESystem preference = systemDAO.findPrefernceById(ESESystem.SYSTEM_SWITCH);
		String currentSeasonCode = preference.getPreferences().get(ESESystem.CURRENT_SEASON_CODE);
		if (StringUtil.isEmpty(currentSeasonCode)) {
			currentSeasonCode = "";
		}
		return currentSeasonCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #addPricePattern(com .sourcetrace.eses.order.entity.txn.PricePattern)
	 */
	public void addPricePattern(PricePattern pricePattern) {

		pricePattern.setCode(idGenerator.createPricePatternCodeSequence());
		pricePattern.setRevisionNo(DateUtil.getRevisionNumber());
		productDistributionDAO.save(pricePattern);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #editPricePattern(com .sourcetrace.eses.order.entity.txn.PricePattern)
	 */
	public void editPricePattern(PricePattern pricePattern) {

		pricePattern.setRevisionNo(DateUtil.getRevisionNumber());
		productDistributionDAO.removePricePatternDetail(pricePattern.getId());
		productDistributionDAO.update(pricePattern);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findPricePatternById (long)
	 */
	public PricePattern findPricePatternById(long id) {

		return productDistributionDAO.findPricePatternById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService #
	 * listPricePatternBySeasonProcurementProduct(long, long)
	 */
	public List<PricePattern> listPricePatternBySeasonProcurementProduct(long seasonId, long procurementProductId) {

		return productDistributionDAO.listPricePatternBySeasonProcurementProduct(seasonId, procurementProductId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService # listPricePatternDetailByPricePattern(long)
	 */
	public List<PricePatternDetail> listPricePatternDetailByPricePattern(long pricePatternId) {

		return productDistributionDAO.listPricePatternDetailByPricePattern(pricePatternId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findDistributionById (java.lang.Long)
	 */
	public Distribution findDistributionById(Long id) {

		return productDistributionDAO.findDistributionById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #listPMTSenderCity()
	 */
	public List<Municipality> listPMTSenderCity() {

		return productDistributionDAO.listPMTSenderCity();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #listPMTReceiverCities()
	 */
	public List<Municipality> listPMTReceiverCities() {

		return productDistributionDAO.listPMTReceiverCities();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService #
	 * findDistributionByReceiptNoTxnType(java.lang.String, java.lang.String)
	 */
	public Distribution findDistributionByReceiptNoTxnType(String receiptNo, String txnType, String tenantId) {

		return productDistributionDAO.findDistributionByReceiptNoTxnType(receiptNo, txnType, tenantId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findCityWarehouseByAgentId (java.lang.String)
	 */
	public List<CityWarehouse> findCityWarehouseByAgentId(String agentId) {

		return productDistributionDAO.findCityWarehouseByAgentId(agentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService #
	 * listProcurementStocksForAgent(java.lang.String)
	 */
	public List<CityWarehouse> listProcurementStocksForAgent(String agentId) {

		return productDistributionDAO.listProcurementStocksForAgent(agentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService #
	 * findPricePatternDetailByProductFarmerSeasonGrade(long, long,
	 * java.lang.String)
	 */
	public PricePatternDetail findPricePatternDetailByProductFarmerSeasonGrade(long farmerId, long procurementProductId,
			String gradeCode) {

		Season season = findCurrentSeason();
		if (!ObjectUtil.isEmpty(season)) {
			return productDistributionDAO.findPricePatternDetailByProductFarmerSeasonGrade(farmerId, season.getId(),
					procurementProductId, gradeCode);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService # listPricePatternDetailByProductSeason(long)
	 */
	public List<PricePatternDetail> listPricePatternDetailByProductSeason(long productId) {

		Season season = findCurrentSeason();
		if (!ObjectUtil.isEmpty(season)) {
			return productDistributionDAO.listPricePatternDetailByProductSeason(productId, season.getId());
		}
		return null;
	}

	/**
	 * Process transaction.
	 * 
	 * @param object
	 *            the object
	 */
	private void processTransaction(Object object) {

		Farmer farmer = null;
		ESEAccount farmerAccount = null, agentAccount = null;
		ProcurementProduct procurementProduct = null;
		Season season = findCurrentSeason();
		boolean isRegisteredFarmer = false;
		WarehouseProduct warehouseProduct;
		ESESystem eseSystem = systemDAO.findPrefernceById("1");
		
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof Procurement) {
				Procurement procurement = (Procurement) object;
				// Procurement may happen with Un registered farmer also
				// if
				// (!"N/A".equalsIgnoreCase(procurement.getAgroTransaction().getFarmerId())
				// || procurement.getAgroTransaction().getFarmerId()!=null) {
				if (procurement.getAgroTransaction().getFarmerId() != null) {
					isRegisteredFarmer = true;
				}
				// Finds registered farmer
				if (isRegisteredFarmer)
					farmer = farmerDAO.findFarmerByFarmerId(procurement.getAgroTransaction().getFarmerId());
				// Finds the Procurement product
				if (!ObjectUtil.isListEmpty(procurement.getProcurementDetails())) {
					procurementProduct = procurement.getProcurementDetails().iterator().next().getProcurementProduct();
				}
				// Finds Farmer Account
				if (!ObjectUtil.isEmpty(season) && isRegisteredFarmer && !ObjectUtil.isEmpty(farmer)
						&& !ObjectUtil.isEmpty(procurementProduct)) {
					farmerAccount = farmerDAO.findAccountBySeassonProcurmentProductFarmer(season.getId(),
							procurementProduct.getId(), farmer.getId());
				}
				// Checks whether payment available
				// if (procurement.isPaymentAvailable()) {
				Agent agent = agentDAO.findAgentByProfileId(procurement.getAgroTransaction().getAgentId());
				if (!ObjectUtil.isEmpty(agent)) {

					agentAccount = accountDAO.findAccountByProfileIdAndProfileType(agent.getProfileId(),
							ESEAccount.AGENT_ACCOUNT);
					if (!ObjectUtil.isEmpty(agentAccount)) {
						procurement.getAgroTransaction().setTxnDesc(Procurement.PROCUREMENT_PAYMENT_AMOUNT);
						AgroTransaction agentPaymentTransaction = buildAgroTransaction(
								procurement.getAgroTransaction());
						agentPaymentTransaction.setProfType(Profile.AGENT);
						if (!ObjectUtil.isEmpty(agent) && agent.isCoOperativeManager()) {
							agentPaymentTransaction.setProfType(Profile.CO_OPEARATIVE_MANAGER);
						}
						if (!ObjectUtil.isEmpty(agentAccount)) {
							agentPaymentTransaction.setTxnDesc(Procurement.PROCUREMENT_AMOUNT);
							agentPaymentTransaction.setIntBalance(agentAccount.getCashBalance());
							
							if (eseSystem.getPreferences().get(ESESystem.ENABLE_LOAN_MODULE).equals("1")) {
								agentPaymentTransaction.setTxnAmount(procurement.getFinalPayAmt());
								agentPaymentTransaction
										.setBalAmount(agentAccount.getCashBalance() - procurement.getFinalPayAmt());
								
								agentAccount.setCashBalance(agentAccount.getCashBalance() - procurement.getFinalPayAmt());
							}else{
								agentPaymentTransaction.setTxnAmount(procurement.getPaymentAmount());
								agentPaymentTransaction
										.setBalAmount(agentAccount.getCashBalance() - procurement.getPaymentAmount());
								
								agentAccount.setCashBalance(agentAccount.getCashBalance() - procurement.getPaymentAmount());
							}
							
							agentPaymentTransaction.setAccount(agentAccount);
							productDistributionDAO.update(agentAccount);
						}
						productDistributionDAO.save(agentPaymentTransaction);

						AgroTransaction farmerPaymentTransaction = buildAgroTransaction(
								procurement.getAgroTransaction());
						farmerPaymentTransaction.setSeasonCode(procurement.getSeasonCode());
						if (!ObjectUtil.isEmpty(farmerAccount)) {
							farmerPaymentTransaction.setTxnDesc(Procurement.PROCURMEMENT);
							farmerPaymentTransaction.setIntBalance(farmerAccount.getCashBalance());
							farmerPaymentTransaction.setTxnAmount(procurement.getTotalProVal());
							farmerPaymentTransaction
									.setBalAmount(farmerAccount.getCashBalance() + procurement.getTotalProVal());
							farmerAccount.setCashBalance(farmerAccount.getCashBalance() + procurement.getTotalProVal());
							farmerPaymentTransaction.setAccount(farmerAccount);
							productDistributionDAO.update(farmerAccount);
						}
						productDistributionDAO.save(farmerPaymentTransaction);
						// Agent account will be detected with payment
						// amount
						/*
						 * if
						 * (!StringUtil.isEmpty(procurement.getPaymentAmount())
						 * && procurement.getPaymentAmount() > 0.00) { if
						 * (procurement.getPaymentMode().equals(ESEAccount.
						 * PAYMENT_MODE_CASH)) { if
						 * (procurement.getTotalProVal() ==
						 * procurement.getPaymentAmount()) {
						 * 
						 * agentPaymentTransaction.calculateBalance(
						 * agentAccount, procurement.getPaymentAmount(), true,
						 * false);
						 * productDistributionDAO.save(agentPaymentTransaction);
						 * 
						 * // Farmer account will be added with transaction //
						 * amount procurement.getAgroTransaction().
						 * calculateBalance(farmerAccount,
						 * procurement.getAgroTransaction(). getTxnAmount(),
						 * true, true); productDistributionDAO.save(procurement.
						 * getAgroTransaction());
						 * 
						 * 
						 * // Farmer account will be detected wtih // payment //
						 * amount AgroTransaction farmerPaymentTransaction =
						 * buildAgroTransaction(
						 * procurement.getAgroTransaction());
						 * farmerPaymentTransaction.calculateBalance(
						 * farmerAccount, procurement.getPaymentAmount(), true,
						 * true);
						 * productDistributionDAO.save(farmerPaymentTransaction)
						 * ;
						 * 
						 * }
						 */ /*
							 * else if (procurement.getTotalProVal() >
							 * procurement.getPaymentAmount()) {
							 * 
							 * agentPaymentTransaction.calculateBalance(
							 * agentAccount, procurement.getPaymentAmount(),
							 * true, false);
							 * agentPaymentTransaction.calculateCreditBalance(
							 * agentAccount, procurement.getTotalProVal() -
							 * procurement.getPaymentAmount(), false, false);
							 * productDistributionDAO.save(
							 * agentPaymentTransaction);
							 * 
							 * // Farmer account will be added with transaction
							 * // amount procurement.getAgroTransaction().
							 * calculateBalance(farmerAccount,
							 * procurement.getAgroTransaction(). getTxnAmount(),
							 * true, true);
							 * productDistributionDAO.save(procurement.
							 * getAgroTransaction());
							 * 
							 * 
							 * // Farmer account will be detected wtih //
							 * payment // amount AgroTransaction
							 * farmerPaymentTransaction = buildAgroTransaction(
							 * procurement.getAgroTransaction());
							 * farmerPaymentTransaction.calculateBalance(
							 * farmerAccount, procurement.getPaymentAmount(),
							 * true, true);
							 * farmerPaymentTransaction.calculateCreditBalance(
							 * farmerAccount, procurement.getTotalProVal() -
							 * procurement.getPaymentAmount(), false, true);
							 * productDistributionDAO.save(
							 * farmerPaymentTransaction);
							 * 
							 * }
							 * 
							 * }
							 * 
							 * }
							 */ /*
								 * else if (procurement.getPaymentMode().equals(
								 * ESEAccount.PAYMENT_MODE_CREDIT)) {
								 * 
								 * agentPaymentTransaction.
								 * calculateCreditBalance(agentAccount,
								 * procurement.getTotalProVal(), false, false);
								 * productDistributionDAO.save(
								 * agentPaymentTransaction);
								 * 
								 * // Farmer account will be added with
								 * transaction // amount
								 * procurement.getAgroTransaction().
								 * calculateCreditBalance(farmerAccount,
								 * procurement.getAgroTransaction().getTxnAmount
								 * (), false, true);
								 * productDistributionDAO.save(procurement.
								 * getAgroTransaction());
								 * 
								 * 
								 * // Farmer account will be detected wtih
								 * payment // amount AgroTransaction
								 * farmerPaymentTransaction =
								 * buildAgroTransaction(
								 * procurement.getAgroTransaction());
								 * farmerPaymentTransaction.
								 * calculateCreditBalance(farmerAccount,
								 * procurement.getTotalProVal(), false, true);
								 * productDistributionDAO.save(
								 * farmerPaymentTransaction);
								 * 
								 * }
								 */

					}
				}
				// }

				/*
				 * else { // Farmer account will be added with transaction
				 * amount procurement.getAgroTransaction().calculateBalance(
				 * farmerAccount,
				 * procurement.getAgroTransaction().getTxnAmount(), true, true);
				 * productDistributionDAO.save(procurement.getAgroTransaction())
				 * ; }
				 */
			} else if (object instanceof Distribution) {
				Distribution distribution = (Distribution) object;

				// Distribution may happen with Un registered farmer also
				if (!"N/A".equalsIgnoreCase(distribution.getAgroTransaction().getFarmerId())) {
					isRegisteredFarmer = true;
				}
				// Finds registered farmer
				if (isRegisteredFarmer)
					farmer = farmerDAO.findFarmerByFarmerId(distribution.getAgroTransaction().getFarmerId());
				// Finds Farmer Account
				if (!ObjectUtil.isEmpty(season) && isRegisteredFarmer && !ObjectUtil.isEmpty(farmer)) {
					farmerAccount = farmerDAO.findAccountBySeassonProcurmentProductFarmer(season.getId(), 0L,
							farmer.getId());
				}

				updateInterestConsolidated(distribution);

				// Checks whether is payment available
				if (distribution.isPaymentAvailable()) {

					if (distribution.getAgroTransaction().getProductStock()
							.equalsIgnoreCase(WarehouseProduct.StockType.AGENT_STOCK.name())) {

						Agent agent = agentDAO.findAgentByProfileId(distribution.getAgroTransaction().getAgentId());
						if (!ObjectUtil.isEmpty(agent)) {
							agentAccount = accountDAO.findAccountByProfileIdAndProfileType(agent.getProfileId(),
									ESEAccount.AGENT_ACCOUNT);
							if (!ObjectUtil.isEmpty(agentAccount)) {
								distribution.getAgroTransaction().setTxnDesc(Distribution.DISTRIBUTION_PAYMENT_AMOUNT);
								// Agent account will be added with payment
								// amount
								AgroTransaction agentPaymentTransaction = buildAgroTransaction(
										distribution.getAgroTransaction());
								agentPaymentTransaction.setProfType(Profile.AGENT);
								if (!ObjectUtil.isEmpty(agent) && agent.isCoOperativeManager()) {
									agentPaymentTransaction.setProfType(Profile.CO_OPEARATIVE_MANAGER);
								}
								agentPaymentTransaction.calculateBalance(agentAccount, distribution.getPaymentAmount(),
										false, true);
								productDistributionDAO.save(agentPaymentTransaction);
								// Farmer account will be detected with
								// transaction
								// amount
								distribution.getAgroTransaction().calculateBalance(farmerAccount,
										distribution.getAgroTransaction().getTxnAmount(), false, false);

								productDistributionDAO.save(distribution.getAgroTransaction());

								// Farmer account will be added with payment
								// amount
								AgroTransaction farmerPaymentTransaction = buildAgroTransaction(
										distribution.getAgroTransaction());
								farmerPaymentTransaction.calculateBalance(farmerAccount,
										distribution.getPaymentAmount(), false, true);
								productDistributionDAO.save(farmerPaymentTransaction);

								ESESystem preferences = systemDAO.findPrefernceById("1");
								String isInterestModule = preferences.getPreferences().get(ESESystem.INTEREST_MODULE);
								if (isInterestModule.equalsIgnoreCase("1")) {
									InterestCalcConsolidated intCalConsolidated = farmerDAO
											.findInterestCalcConsolidatedByfarmerProfileId(
													distribution.getAgroTransaction().getFarmerId());
									double paymentAmt = distribution.getPaymentAmount();
									if (!ObjectUtil.isEmpty(intCalConsolidated)) {
										if (intCalConsolidated.getAccumulatedIntAmount() > 0) {
											double remainAmt = intCalConsolidated.getAccumulatedIntAmount()
													- paymentAmt;
											if (remainAmt >= 0) {
												intCalConsolidated.setAccumulatedIntAmount(remainAmt);
												intCalConsolidated.setUpdateUserName(
														distribution.getAgroTransaction().getAgentId());
												intCalConsolidated.setLastUpdateDt(new Date());
												intCalConsolidated.setRevisionNo(DateUtil.getRevisionNumber());
												farmerDAO.update(intCalConsolidated);
											} else if (remainAmt < 0) {
												intCalConsolidated.setAccumulatedIntAmount(0);
												intCalConsolidated.setAccumulatedPrincipalAmount(
														intCalConsolidated.getAccumulatedPrincipalAmount()
																- Math.abs(remainAmt));
												intCalConsolidated.setUpdateUserName(
														distribution.getAgroTransaction().getAgentId());
												intCalConsolidated.setLastUpdateDt(new Date());
												intCalConsolidated.setRevisionNo(DateUtil.getRevisionNumber());
												farmerDAO.update(intCalConsolidated);
											}
										} else {
											intCalConsolidated.setAccumulatedPrincipalAmount(
													intCalConsolidated.getAccumulatedPrincipalAmount() - paymentAmt);
											intCalConsolidated
													.setUpdateUserName(distribution.getAgroTransaction().getAgentId());
											intCalConsolidated.setLastUpdateDt(new Date());
											intCalConsolidated.setRevisionNo(DateUtil.getRevisionNumber());
											farmerDAO.update(intCalConsolidated);
										}
									}
								}
							}
						}
					} else if (distribution.getAgroTransaction().getProductStock().equalsIgnoreCase(
							WarehouseProduct.StockType.WAREHOUSE_STOCK.name()) && !ObjectUtil.isEmpty(farmerAccount)) {
						// Farmer account will be detected with transaction
						// amount
						distribution.getAgroTransaction().calculateBalance(farmerAccount,
								distribution.getAgroTransaction().getTxnAmount(), false, false);
						productDistributionDAO.save(distribution.getAgroTransaction());
						distribution.getAgroTransaction().setTxnDesc(Distribution.DISTRIBUTION_PAYMENT_AMOUNT);
						// Farmer account will be added with payment amount
						AgroTransaction farmerPaymentTransaction = buildAgroTransaction(
								distribution.getAgroTransaction());
						farmerPaymentTransaction.calculateBalance(farmerAccount, distribution.getPaymentAmount(), false,
								true);
						productDistributionDAO.save(farmerPaymentTransaction);

						ESESystem preferences = systemDAO.findPrefernceById("1");
						String isInterestModule = preferences.getPreferences().get(ESESystem.INTEREST_MODULE);
						if (isInterestModule.equalsIgnoreCase("1")) {
							InterestCalcConsolidated intCalConsolidated = farmerDAO
									.findInterestCalcConsolidatedByfarmerProfileId(
											distribution.getAgroTransaction().getFarmerId());
							double paymentAmt = distribution.getPaymentAmount();
							if (!ObjectUtil.isEmpty(intCalConsolidated)) {
								if (intCalConsolidated.getAccumulatedIntAmount() > 0) {
									double remainAmt = intCalConsolidated.getAccumulatedIntAmount() - paymentAmt;
									if (remainAmt >= 0) {
										intCalConsolidated.setAccumulatedIntAmount(remainAmt);
										intCalConsolidated
												.setUpdateUserName(distribution.getAgroTransaction().getAgentId());
										intCalConsolidated.setLastUpdateDt(new Date());
										intCalConsolidated.setRevisionNo(DateUtil.getRevisionNumber());
										farmerDAO.update(intCalConsolidated);
									} else if (remainAmt < 0) {
										intCalConsolidated.setAccumulatedIntAmount(0);
										intCalConsolidated.setAccumulatedPrincipalAmount(
												intCalConsolidated.getAccumulatedPrincipalAmount()
														- Math.abs(remainAmt));
										intCalConsolidated
												.setUpdateUserName(distribution.getAgroTransaction().getAgentId());
										intCalConsolidated.setLastUpdateDt(new Date());
										intCalConsolidated.setRevisionNo(DateUtil.getRevisionNumber());
										farmerDAO.update(intCalConsolidated);
									}
								} else {
									intCalConsolidated.setAccumulatedPrincipalAmount(
											intCalConsolidated.getAccumulatedPrincipalAmount() - paymentAmt);
									intCalConsolidated
											.setUpdateUserName(distribution.getAgroTransaction().getAgentId());
									intCalConsolidated.setLastUpdateDt(new Date());
									intCalConsolidated.setRevisionNo(DateUtil.getRevisionNumber());
									farmerDAO.update(intCalConsolidated);
								}
							}
						}
					} else if (distribution.getAgroTransaction().getProductStock().equalsIgnoreCase(
							WarehouseProduct.StockType.WAREHOUSE_STOCK.name()) && ObjectUtil.isEmpty(farmerAccount)) {
						productDistributionDAO.save(distribution.getAgroTransaction());
					} else if (distribution.getAgroTransaction().getProductStock().equalsIgnoreCase(
							WarehouseProduct.StockType.AGENT_STOCK.name()) && ObjectUtil.isEmpty(farmerAccount)) {

						Agent agent = agentDAO.findAgentByProfileId(distribution.getAgroTransaction().getAgentId());
						if (!ObjectUtil.isEmpty(agent)) {
							agentAccount = accountDAO.findAccountByProfileIdAndProfileType(agent.getProfileId(),
									ESEAccount.AGENT_ACCOUNT);
							if (!ObjectUtil.isEmpty(agentAccount)) {
								// Agent account will be added with payment
								// amount
								AgroTransaction agentPaymentTransaction = buildAgroTransaction(
										distribution.getAgroTransaction());
								agentPaymentTransaction.setTxnDesc(Distribution.DISTRIBUTION_PAYMENT_AMOUNT);

								agentPaymentTransaction.setProfType(Profile.AGENT);
								if (!ObjectUtil.isEmpty(agent) && agent.isCoOperativeManager()) {
									agentPaymentTransaction.setProfType(Profile.CO_OPEARATIVE_MANAGER);
								}
								agentPaymentTransaction.calculateBalance(agentAccount, distribution.getPaymentAmount(),
										false, true);
								productDistributionDAO.save(agentPaymentTransaction);
							}
						}
						productDistributionDAO.save(distribution.getAgroTransaction());
					}
				} else {
					// Farmer account will be detected with transaction amount
					distribution.getAgroTransaction().calculateBalance(farmerAccount,
							distribution.getAgroTransaction().getTxnAmount(), false, false);
					productDistributionDAO.save(distribution.getAgroTransaction());
				}
			}

			else if (object instanceof ProcurementTraceability) {
				ProcurementTraceability procurement = (ProcurementTraceability) object;
				if (procurement.getAgroTransaction().getFarmerId() != null) {
					isRegisteredFarmer = true;
				}
				// Finds registered farmer
				if (isRegisteredFarmer)
					farmer = farmerDAO.findFarmerByFarmerId(procurement.getAgroTransaction().getFarmerId());
				// Finds the Procurement product
				if (!ObjectUtil.isListEmpty(procurement.getProcurmentTraceabilityDetails())) {
					procurementProduct = procurement.getProcurmentTraceabilityDetails().iterator().next()
							.getProcurementProduct();
				}
				// Finds Farmer Account
				if (!ObjectUtil.isEmpty(season) && isRegisteredFarmer && !ObjectUtil.isEmpty(farmer)
						&& !ObjectUtil.isEmpty(procurementProduct)) {
					farmerAccount = farmerDAO.findAccountBySeassonProcurmentProductFarmer(season.getId(),
							procurementProduct.getId(), farmer.getId());
				}
				Agent agent = agentDAO.findAgentByProfileId(procurement.getAgroTransaction().getAgentId());
				if (!ObjectUtil.isEmpty(agent)) {

					agentAccount = accountDAO.findAccountByProfileIdAndProfileType(agent.getProfileId(),
							ESEAccount.AGENT_ACCOUNT);
					if (!ObjectUtil.isEmpty(agentAccount)) {
						procurement.getAgroTransaction().setTxnDesc(Procurement.PROCUREMENT_PAYMENT_AMOUNT);
						AgroTransaction agentPaymentTransaction = buildAgroTransaction(
								procurement.getAgroTransaction());
						agentPaymentTransaction.setProfType(Profile.AGENT);
						if (!ObjectUtil.isEmpty(agent) && agent.isCoOperativeManager()) {
							agentPaymentTransaction.setProfType(Profile.CO_OPEARATIVE_MANAGER);
						}
						if (!ObjectUtil.isEmpty(agentAccount)) {
							agentPaymentTransaction.setTxnDesc(Procurement.PROCUREMENT_AMOUNT);
							agentPaymentTransaction.setIntBalance(agentAccount.getCashBalance());
							agentPaymentTransaction.setTxnAmount(procurement.getPaymentAmount());
							agentPaymentTransaction
									.setBalAmount(agentAccount.getCashBalance() - procurement.getPaymentAmount());
							agentPaymentTransaction.setAccount(agentAccount);
							agentAccount.setCashBalance(agentAccount.getCashBalance() - procurement.getPaymentAmount());
							productDistributionDAO.update(agentAccount);
						}
						productDistributionDAO.save(agentPaymentTransaction);

						AgroTransaction farmerPaymentTransaction = buildAgroTransaction(
								procurement.getAgroTransaction());
						farmerPaymentTransaction.setSeasonCode(procurement.getSeason());
						if (!ObjectUtil.isEmpty(farmerAccount)) {
							farmerPaymentTransaction.setTxnDesc(Procurement.PROCURMEMENT);
							farmerPaymentTransaction.setIntBalance(farmerAccount.getCashBalance());
							farmerPaymentTransaction.setTxnAmount(procurement.getTotalProVal());
							farmerPaymentTransaction
									.setBalAmount(farmerAccount.getCashBalance() + procurement.getTotalProVal());
							farmerAccount.setCashBalance(farmerAccount.getCashBalance() + procurement.getTotalProVal());
							farmerPaymentTransaction.setAccount(farmerAccount);
							productDistributionDAO.update(farmerAccount);
						}
						productDistributionDAO.save(farmerPaymentTransaction);
					}
				}
			}else if (object instanceof LoanDistribution) {
				LoanDistribution loanDistribution = (LoanDistribution) object;
				
				if (loanDistribution.getAgroTransaction().getFarmerId() != null) {
					isRegisteredFarmer = true;
				}
				// Finds registered farmer
				if (isRegisteredFarmer)
					farmer = farmerDAO.findFarmerByFarmerId(loanDistribution.getAgroTransaction().getFarmerId());
				
				// Finds Farmer Account
				if (isRegisteredFarmer && !ObjectUtil.isEmpty(farmer)) {
					farmerAccount = farmerDAO.findAccountByFarmerLoanProduct(farmer.getId());
				}
				if (!ObjectUtil.isEmpty(farmerAccount)) {
							
							farmerAccount.setLoanAmount(farmerAccount.getLoanAmount() + loanDistribution.getTotalCostToFarmer());
							farmerAccount.setOutstandingLoanAmount(farmerAccount.getOutstandingLoanAmount() + loanDistribution.getTotalCostToFarmer());
						
							productDistributionDAO.update(farmerAccount);
						}
						
					}
		}
	}

	private void processTransaction(Object object, String tenantId) {

		Farmer farmer = null;
		ESEAccount farmerAccount = null, agentAccount = null;
		ProcurementProduct procurementProduct = null;
		Season season = findCurrentSeason(tenantId);
		boolean isRegisteredFarmer = false;
		WarehouseProduct warehouseProduct;
		ESESystem eseSystem = systemDAO.findPrefernceById("1");
		
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof Procurement) {
				Procurement procurement = (Procurement) object;
				// Procurement may happen with Un registered farmer also
				// if
				// (!"N/A".equalsIgnoreCase(procurement.getAgroTransaction().getFarmerId())
				// || procurement.getAgroTransaction().getFarmerId()!=null) {
				if (procurement.getAgroTransaction().getFarmerId() != null) {
					isRegisteredFarmer = true;
				}
				// Finds registered farmer
				if (isRegisteredFarmer)
					farmer = farmerDAO.findFarmerByFarmerId(procurement.getAgroTransaction().getFarmerId(), tenantId);
				// Finds the Procurement product
				if (!ObjectUtil.isListEmpty(procurement.getProcurementDetails())) {
					procurementProduct = procurement.getProcurementDetails().iterator().next().getProcurementProduct();
				}
				// Finds Farmer Account
				if (!ObjectUtil.isEmpty(season) && isRegisteredFarmer && !ObjectUtil.isEmpty(farmer)) {
					farmerAccount = farmerDAO.findAccountBySeassonProcurmentProductFarmer(season.getId(),
							procurementProduct.getId(), farmer.getId(), tenantId);
				}
				// Checks whether payment available
				// if (procurement.isPaymentAvailable()) {
				Agent agent = agentDAO.findAgentByProfileId(procurement.getAgroTransaction().getAgentId(), tenantId);
				if (!ObjectUtil.isEmpty(agent)) {
					agentAccount = accountDAO.findAccountByProfileIdAndProfileType(agent.getProfileId(),
							ESEAccount.AGENT_ACCOUNT, tenantId);
					if (!ObjectUtil.isEmpty(agentAccount)) {
						

						AgroTransaction farmerPaymentTransaction = buildAgroTransaction(
								procurement.getAgroTransaction());
						if (!ObjectUtil.isEmpty(farmerAccount)) {
							farmerPaymentTransaction.setTxnDesc(Procurement.PROCURMEMENT);
							farmerPaymentTransaction.setIntBalance(farmerAccount.getCashBalance());
							farmerPaymentTransaction.setTxnAmount(procurement.getTotalProVal());
							farmerPaymentTransaction
									.setBalAmount(farmerAccount.getCashBalance() + procurement.getTotalProVal());
							farmerPaymentTransaction.setBranch_id(procurement.getBranchId());							
							
							farmerPaymentTransaction.setAccount(farmerAccount);
							productDistributionDAO.updateESEAccount(farmerAccount, tenantId);
						}
						farmerPaymentTransaction.setSeasonCode(procurement.getSeasonCode());
						productDistributionDAO.saveAgroTransaction(farmerPaymentTransaction, tenantId);		
						
						
						procurement.getAgroTransaction().setTxnDesc(Procurement.PROCUREMENT_PAYMENT_AMOUNT);
						AgroTransaction agentPaymentTransaction = buildAgroTransaction(
								procurement.getAgroTransaction());
						agentPaymentTransaction.setProfType(Profile.AGENT);
						if (!ObjectUtil.isEmpty(agent) && agent.isCoOperativeManager()) {
							agentPaymentTransaction.setProfType(Profile.CO_OPEARATIVE_MANAGER);
						}

						if (!ObjectUtil.isEmpty(agentAccount)) {
							agentPaymentTransaction.setTxnDesc(Procurement.PROCUREMENT_AMOUNT);
							agentPaymentTransaction.setIntBalance(agentAccount.getCashBalance());
							if (eseSystem.getPreferences().get(ESESystem.ENABLE_LOAN_MODULE).equals("1")) {
								agentPaymentTransaction.setTxnAmount(procurement.getFinalPayAmt());
								agentPaymentTransaction
										.setBalAmount(agentAccount.getCashBalance() - procurement.getFinalPayAmt());
								
								agentAccount.setCashBalance(agentAccount.getCashBalance() - procurement.getFinalPayAmt());
							}else{
								agentPaymentTransaction.setTxnAmount(procurement.getPaymentAmount());
								agentPaymentTransaction
										.setBalAmount(agentAccount.getCashBalance() - procurement.getPaymentAmount());
								
								agentAccount.setCashBalance(agentAccount.getCashBalance() - procurement.getPaymentAmount());
							}
							productDistributionDAO.updateESEAccount(agentAccount, tenantId);
						}
						agentPaymentTransaction.setSeasonCode(procurement.getSeasonCode());
						productDistributionDAO.saveAgroTransaction(agentPaymentTransaction, tenantId);

						// Agent account will be detected with payment
						// amount

						/*
						 * if
						 * (!StringUtil.isEmpty(procurement.getPaymentAmount())
						 * && procurement.getPaymentAmount() > 0.00) { if
						 * (procurement.getPaymentMode().equals(ESEAccount.
						 * PAYMENT_MODE_CASH)) { if
						 * (procurement.getTotalProVal() ==
						 * procurement.getPaymentAmount()) {
						 * 
						 * agentPaymentTransaction.calculateBalance(
						 * agentAccount, procurement.getPaymentAmount(), true,
						 * false); productDistributionDAO.saveAgroTransaction(
						 * agentPaymentTransaction, tenantId); // Farmer account
						 * will be added with // transaction // amount
						 * 
						 * procurement.getAgroTransaction().
						 * calculateBalance(farmerAccount,
						 * procurement.getAgroTransaction(). getTxnAmount(),
						 * true, true); productDistributionDAO.
						 * saveAgroTransaction(procurement.
						 * getAgroTransaction(), tenantId);
						 * 
						 * // Farmer account will be detected wtih // payment //
						 * amount AgroTransaction farmerPaymentTransaction =
						 * buildAgroTransaction(
						 * procurement.getAgroTransaction());
						 * farmerPaymentTransaction.calculateBalance(
						 * farmerAccount, procurement.getPaymentAmount(), true,
						 * false); productDistributionDAO.saveAgroTransaction(
						 * farmerPaymentTransaction, tenantId);
						 * 
						 * } else if (procurement.getTotalProVal() >
						 * procurement.getPaymentAmount()) {
						 * 
						 * agentPaymentTransaction.calculateBalance(
						 * agentAccount, procurement.getPaymentAmount(), true,
						 * false);
						 * agentPaymentTransaction.calculateCreditBalance(
						 * agentAccount, procurement.getTotalProVal(), false,
						 * false); productDistributionDAO.saveAgroTransaction(
						 * agentPaymentTransaction, tenantId);
						 * 
						 * // Farmer account will be added with transaction //
						 * amount procurement.getAgroTransaction().
						 * calculateBalance(farmerAccount,
						 * procurement.getAgroTransaction(). getTxnAmount(),
						 * true, true); productDistributionDAO.save(procurement.
						 * getAgroTransaction());
						 * 
						 * 
						 * // Farmer account will be detected wtih // payment //
						 * amount AgroTransaction farmerPaymentTransaction =
						 * buildAgroTransaction(
						 * procurement.getAgroTransaction());
						 * farmerPaymentTransaction.calculateBalance(
						 * farmerAccount, procurement.getPaymentAmount(), true,
						 * true);
						 * farmerPaymentTransaction.calculateCreditBalance(
						 * farmerAccount, procurement.getTotalProVal() -
						 * procurement.getPaymentAmount(), false, true);
						 * productDistributionDAO.saveAgroTransaction(
						 * farmerPaymentTransaction, tenantId);
						 * 
						 * } } }
						 */ /*
							 * else if
							 * (procurement.getPaymentMode().equals(ESEAccount.
							 * PAYMENT_MODE_CREDIT)) {
							 * 
							 * agentPaymentTransaction.calculateCreditBalance(
							 * agentAccount, procurement.getTotalProVal(),
							 * false, false);
							 * productDistributionDAO.saveAgroTransaction(
							 * agentPaymentTransaction, tenantId); // Farmer
							 * account will be added with transaction // amount
							 * 
							 * procurement.getAgroTransaction().calculateBalance
							 * (farmerAccount,
							 * procurement.getAgroTransaction().getTxnAmount(),
							 * true, true);
							 * productDistributionDAO.saveAgroTransaction(
							 * procurement.getAgroTransaction(), tenantId);
							 * 
							 * // Farmer account will be detected wtih payment
							 * // amount AgroTransaction
							 * farmerPaymentTransaction = buildAgroTransaction(
							 * procurement.getAgroTransaction());
							 * farmerPaymentTransaction.calculateCreditBalance(
							 * farmerAccount, procurement.getTotalProVal(),
							 * false, true);
							 * productDistributionDAO.saveAgroTransaction(
							 * farmerPaymentTransaction, tenantId);
							 * 
							 * }
							 */

					}
				}
				// }
				/*
				 * else { // Farmer account will be added with transaction
				 * amount procurement.getAgroTransaction().calculateBalance(
				 * farmerAccount,
				 * procurement.getAgroTransaction().getTxnAmount(), true, true);
				 * productDistributionDAO.saveAgroTransaction(procurement.
				 * getAgroTransaction(), tenantId); }
				 */
			} else if (object instanceof Distribution) {
				Distribution distribution = (Distribution) object;

				// Distribution may happen with Un registered farmer also
				if (!"N/A".equalsIgnoreCase(distribution.getAgroTransaction().getFarmerId())) {
					isRegisteredFarmer = true;
				}
				// Finds registered farmer
				if (isRegisteredFarmer)
					farmer = farmerDAO.findFarmerByFarmerId(distribution.getAgroTransaction().getFarmerId(), tenantId);
				// Finds Farmer Account
				if (!ObjectUtil.isEmpty(season) && isRegisteredFarmer && !ObjectUtil.isEmpty(farmer)) {
					farmerAccount = farmerDAO.findAccountBySeassonProcurmentProductFarmer(season.getId(), 0L,
							farmer.getId(), tenantId);
				}

				updateInterestConsolidated(distribution, tenantId);

				// Checks whether is payment available
				if (distribution.isPaymentAvailable()) {

					if (distribution.getAgroTransaction().getProductStock()
							.equalsIgnoreCase(WarehouseProduct.StockType.AGENT_STOCK.name())) {

						Agent agent = agentDAO.findAgentByProfileId(distribution.getAgroTransaction().getAgentId(),
								tenantId);
						if (!ObjectUtil.isEmpty(agent)) {
							agentAccount = accountDAO.findAccountByProfileIdAndProfileType(agent.getProfileId(),
									ESEAccount.AGENT_ACCOUNT, tenantId);
							if (!ObjectUtil.isEmpty(agentAccount)) {
								distribution.getAgroTransaction().setTxnDesc(Distribution.DISTRIBUTION_PAYMENT_AMOUNT);
								// Agent account will be added with payment
								// amount
								AgroTransaction agentPaymentTransaction = buildAgroTransaction(
										distribution.getAgroTransaction());
								agentPaymentTransaction.setProfType(Profile.AGENT);
								if (!ObjectUtil.isEmpty(agent) && agent.isCoOperativeManager()) {
									agentPaymentTransaction.setProfType(Profile.CO_OPEARATIVE_MANAGER);
								}
								agentPaymentTransaction.calculateBalance(agentAccount, distribution.getPaymentAmount(),
										false, true);
								productDistributionDAO.saveAgroTransaction(agentPaymentTransaction, tenantId);
								// Farmer account will be detected with
								// transaction
								// amount
								distribution.getAgroTransaction().calculateBalance(farmerAccount,
										distribution.getAgroTransaction().getTxnAmount(), false, false);
								productDistributionDAO.saveAgroTransaction(distribution.getAgroTransaction(), tenantId);

								// Farmer account will be added with payment
								// amount
								AgroTransaction farmerPaymentTransaction = buildAgroTransaction(
										distribution.getAgroTransaction());
								farmerPaymentTransaction.calculateBalance(farmerAccount,
										distribution.getPaymentAmount(), false, true);
								productDistributionDAO.saveAgroTransaction(farmerPaymentTransaction, tenantId);

								ESESystem preferences = systemDAO.findPrefernceById("1", tenantId);
								String isInterestModule = preferences.getPreferences().get(ESESystem.INTEREST_MODULE);
								if (isInterestModule.equalsIgnoreCase("1")) {
									InterestCalcConsolidated intCalConsolidated = farmerDAO
											.findInterestCalcConsolidatedByfarmerProfileId(
													distribution.getAgroTransaction().getFarmerId(), tenantId);
									double paymentAmt = distribution.getPaymentAmount();
									if (!ObjectUtil.isEmpty(intCalConsolidated)) {
										if (intCalConsolidated.getAccumulatedIntAmount() > 0) {
											double remainAmt = intCalConsolidated.getAccumulatedIntAmount()
													- paymentAmt;
											if (remainAmt >= 0) {
												intCalConsolidated.setAccumulatedIntAmount(remainAmt);
												intCalConsolidated.setUpdateUserName(
														distribution.getAgroTransaction().getAgentId());
												intCalConsolidated.setLastUpdateDt(new Date());
												intCalConsolidated.setRevisionNo(DateUtil.getRevisionNumber());
												farmerDAO.updateInterestCalcConsolidated(intCalConsolidated, tenantId);
											} else if (remainAmt < 0) {
												intCalConsolidated.setAccumulatedIntAmount(0);
												intCalConsolidated.setAccumulatedPrincipalAmount(
														intCalConsolidated.getAccumulatedPrincipalAmount()
																- Math.abs(remainAmt));
												intCalConsolidated.setUpdateUserName(
														distribution.getAgroTransaction().getAgentId());
												intCalConsolidated.setLastUpdateDt(new Date());
												intCalConsolidated.setRevisionNo(DateUtil.getRevisionNumber());
												farmerDAO.updateInterestCalcConsolidated(intCalConsolidated, tenantId);
											}
										} else {
											intCalConsolidated.setAccumulatedPrincipalAmount(
													intCalConsolidated.getAccumulatedPrincipalAmount() - paymentAmt);
											intCalConsolidated
													.setUpdateUserName(distribution.getAgroTransaction().getAgentId());
											intCalConsolidated.setLastUpdateDt(new Date());
											intCalConsolidated.setRevisionNo(DateUtil.getRevisionNumber());
											farmerDAO.updateInterestCalcConsolidated(intCalConsolidated, tenantId);
										}
									}
								}
							}
						}
					} else if (distribution.getAgroTransaction().getProductStock().equalsIgnoreCase(
							WarehouseProduct.StockType.WAREHOUSE_STOCK.name()) && !ObjectUtil.isEmpty(farmerAccount)) {
						// Farmer account will be detected with transaction
						// amount
						distribution.getAgroTransaction().calculateBalance(farmerAccount,
								distribution.getAgroTransaction().getTxnAmount(), false, false);
						productDistributionDAO.saveAgroTransaction(distribution.getAgroTransaction(), tenantId);
						distribution.getAgroTransaction().setTxnDesc(Distribution.DISTRIBUTION_PAYMENT_AMOUNT);
						// Farmer account will be added with payment amount
						AgroTransaction farmerPaymentTransaction = buildAgroTransaction(
								distribution.getAgroTransaction());
						farmerPaymentTransaction.calculateBalance(farmerAccount, distribution.getPaymentAmount(), false,
								true);
						productDistributionDAO.saveAgroTransaction(farmerPaymentTransaction, tenantId);

						ESESystem preferences = systemDAO.findPrefernceById("1", tenantId);
						String isInterestModule = preferences.getPreferences().get(ESESystem.INTEREST_MODULE);
						if (isInterestModule.equalsIgnoreCase("1")) {
							InterestCalcConsolidated intCalConsolidated = farmerDAO
									.findInterestCalcConsolidatedByfarmerProfileId(
											distribution.getAgroTransaction().getFarmerId(), tenantId);
							double paymentAmt = distribution.getPaymentAmount();
							if (!ObjectUtil.isEmpty(intCalConsolidated)) {
								if (intCalConsolidated.getAccumulatedIntAmount() > 0) {
									double remainAmt = intCalConsolidated.getAccumulatedIntAmount() - paymentAmt;
									if (remainAmt >= 0) {
										intCalConsolidated.setAccumulatedIntAmount(remainAmt);
										intCalConsolidated
												.setUpdateUserName(distribution.getAgroTransaction().getAgentId());
										intCalConsolidated.setLastUpdateDt(new Date());
										intCalConsolidated.setRevisionNo(DateUtil.getRevisionNumber());
										farmerDAO.updateInterestCalcConsolidated(intCalConsolidated, tenantId);
									} else if (remainAmt < 0) {
										intCalConsolidated.setAccumulatedIntAmount(0);
										intCalConsolidated.setAccumulatedPrincipalAmount(
												intCalConsolidated.getAccumulatedPrincipalAmount()
														- Math.abs(remainAmt));
										intCalConsolidated
												.setUpdateUserName(distribution.getAgroTransaction().getAgentId());
										intCalConsolidated.setLastUpdateDt(new Date());
										intCalConsolidated.setRevisionNo(DateUtil.getRevisionNumber());
										farmerDAO.updateInterestCalcConsolidated(intCalConsolidated, tenantId);
									}
								} else {
									intCalConsolidated.setAccumulatedPrincipalAmount(
											intCalConsolidated.getAccumulatedPrincipalAmount() - paymentAmt);
									intCalConsolidated
											.setUpdateUserName(distribution.getAgroTransaction().getAgentId());
									intCalConsolidated.setLastUpdateDt(new Date());
									intCalConsolidated.setRevisionNo(DateUtil.getRevisionNumber());
									farmerDAO.updateInterestCalcConsolidated(intCalConsolidated, tenantId);
								}
							}
						}
					} else if (distribution.getAgroTransaction().getProductStock().equalsIgnoreCase(
							WarehouseProduct.StockType.WAREHOUSE_STOCK.name()) && ObjectUtil.isEmpty(farmerAccount)) {
						productDistributionDAO.saveAgroTransaction(distribution.getAgroTransaction(), tenantId);
					} else if (distribution.getAgroTransaction().getProductStock().equalsIgnoreCase(
							WarehouseProduct.StockType.AGENT_STOCK.name()) && ObjectUtil.isEmpty(farmerAccount)) {

						Agent agent = agentDAO.findAgentByProfileId(distribution.getAgroTransaction().getAgentId(),
								tenantId);
						if (!ObjectUtil.isEmpty(agent)) {
							agentAccount = accountDAO.findAccountByProfileIdAndProfileType(agent.getProfileId(),
									ESEAccount.AGENT_ACCOUNT, tenantId);
							if (!ObjectUtil.isEmpty(agentAccount)) {
								// Agent account will be added with payment
								// amount
								AgroTransaction agentPaymentTransaction = buildAgroTransaction(
										distribution.getAgroTransaction());
								agentPaymentTransaction.setTxnDesc(Distribution.DISTRIBUTION_PAYMENT_AMOUNT);

								agentPaymentTransaction.setProfType(Profile.AGENT);
								if (!ObjectUtil.isEmpty(agent) && agent.isCoOperativeManager()) {
									agentPaymentTransaction.setProfType(Profile.CO_OPEARATIVE_MANAGER);
								}
								agentPaymentTransaction.calculateBalance(agentAccount, distribution.getPaymentAmount(),
										false, true);
								productDistributionDAO.saveAgroTransaction(agentPaymentTransaction, tenantId);
							}
						}
						productDistributionDAO.saveAgroTransaction(distribution.getAgroTransaction(), tenantId);
					}
				} else {
					// Farmer account will be detected with transaction amount
					distribution.getAgroTransaction().calculateBalance(farmerAccount,
							distribution.getAgroTransaction().getTxnAmount(), false, false);
					productDistributionDAO.saveAgroTransaction(distribution.getAgroTransaction(), tenantId);
				}
			}
			if (object instanceof SupplierProcurement) {
				SupplierProcurement supplierProcurement = (SupplierProcurement) object;
				if (supplierProcurement.getAgroTransaction().getFarmerId() != null) {
					isRegisteredFarmer = true;
				}
				// Finds registered farmer
				if (!ObjectUtil
						.isEmpty(supplierProcurement.getSupplierProcurementDetails().iterator().next().getFarmer()))
					farmer = farmerDAO.findFarmerByFarmerId(supplierProcurement.getSupplierProcurementDetails()
							.iterator().next().getFarmer().getFarmerId(), tenantId);
				// Finds the Procurement product
				if (!ObjectUtil.isListEmpty(supplierProcurement.getSupplierProcurementDetails())) {
					procurementProduct = supplierProcurement.getSupplierProcurementDetails().iterator().next()
							.getProcurementProduct();
				}
				// Finds Farmer Account
				if (!ObjectUtil.isEmpty(season) && isRegisteredFarmer && !ObjectUtil.isEmpty(farmer)) {
					farmerAccount = farmerDAO.findAccountBySeassonProcurmentProductFarmer(season.getId(),
							procurementProduct.getId(), farmer.getId(), tenantId);
				}
				// Checks whether payment available
				// if (procurement.isPaymentAvailable()) {
				Agent agent = agentDAO.findAgentByProfileId(supplierProcurement.getAgroTransaction().getAgentId(),
						tenantId);
				if (!ObjectUtil.isEmpty(agent)) {
					agentAccount = accountDAO.findAccountByProfileIdAndProfileType(agent.getProfileId(),
							ESEAccount.AGENT_ACCOUNT, tenantId);
					if (!ObjectUtil.isEmpty(agentAccount)) {
						supplierProcurement.getAgroTransaction().setTxnDesc(Procurement.PROCUREMENT_PAYMENT_AMOUNT);
						AgroTransaction agentPaymentTransaction = buildAgroTransaction(
								supplierProcurement.getAgroTransaction());
						agentPaymentTransaction.setProfType(Profile.AGENT);
						if (!ObjectUtil.isEmpty(agent) && agent.isCoOperativeManager()) {
							agentPaymentTransaction.setProfType(Profile.CO_OPEARATIVE_MANAGER);
						}

						if (!ObjectUtil.isEmpty(agentAccount)) {
							agentPaymentTransaction.setTxnDesc(Procurement.PROCUREMENT_AMOUNT);
							agentPaymentTransaction.setIntBalance(agentAccount.getCashBalance());
							agentPaymentTransaction.setTxnAmount(supplierProcurement.getPaymentAmount());
							agentPaymentTransaction.setBalAmount(
									agentAccount.getCashBalance() - supplierProcurement.getPaymentAmount());
							agentPaymentTransaction.setAccount(agentAccount);
							agentAccount.setCashBalance(
									agentAccount.getCashBalance() - supplierProcurement.getPaymentAmount());
							productDistributionDAO.updateESEAccount(agentAccount, tenantId);
						}
						agentPaymentTransaction.setSeasonCode(supplierProcurement.getSeasonCode());
						productDistributionDAO.saveAgroTransaction(agentPaymentTransaction, tenantId);

						AgroTransaction farmerPaymentTransaction = buildAgroTransaction(
								supplierProcurement.getAgroTransaction());
						if (!ObjectUtil.isEmpty(farmerAccount)) {
							farmerPaymentTransaction.setTxnDesc(SupplierProcurement.PROCURMEMENT);
							farmerPaymentTransaction.setIntBalance(farmerAccount.getCashBalance());
							farmerPaymentTransaction.setTxnAmount(supplierProcurement.getTotalProVal());
							farmerPaymentTransaction.setBalAmount(
									farmerAccount.getCashBalance() + supplierProcurement.getTotalProVal());
							farmerAccount.setCashBalance(
									farmerAccount.getCashBalance() + supplierProcurement.getTotalProVal());
							farmerPaymentTransaction.setAccount(farmerAccount);
							productDistributionDAO.updateESEAccount(farmerAccount, tenantId);
						}
						farmerPaymentTransaction.setSeasonCode(supplierProcurement.getSeasonCode());
						productDistributionDAO.saveAgroTransaction(farmerPaymentTransaction, tenantId);

					}
				}

			}
		}
	}

	private void processTransaction1(Object object) {

		Farmer farmer = null;
		ESEAccount farmerAccount = null, agentAccount = null;
		ProcurementProduct procurementProduct = null;
		Season season = findCurrentSeason();
		// boolean isRegisteredFarmer = false;
		WarehouseProduct warehouseProduct;
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof Procurement) {
				Procurement procurement = (Procurement) object;
				farmer = farmerDAO.findFarmerByFarmerId(procurement.getAgroTransaction().getFarmerId());

				if (!ObjectUtil.isListEmpty(procurement.getProcurementDetails())) {
					procurementProduct = procurement.getProcurementDetails().iterator().next().getProcurementProduct();
				}
				// Finds Farmer Account
				if (!ObjectUtil.isEmpty(season) && !ObjectUtil.isEmpty(farmer)
						&& !ObjectUtil.isEmpty(procurementProduct)) {
					farmerAccount = farmerDAO.findAccountBySeassonProcurmentProductFarmer(season.getId(),
							procurementProduct.getId(), farmer.getId());
				}
				// Checks whether payment available
				// if (procurement.isPaymentAvailable()) {
				Agent agent = agentDAO.findAgentByProfileId(procurement.getAgroTransaction().getAgentId());
				if (!ObjectUtil.isEmpty(agent)) {
					agentAccount = accountDAO.findAccountByProfileIdAndProfileType(agent.getProfileId(),
							ESEAccount.AGENT_ACCOUNT);
					if (!ObjectUtil.isEmpty(agentAccount)) {
						procurement.getAgroTransaction().setTxnDesc(Procurement.PROCUREMENT_PAYMENT_AMOUNT);
						AgroTransaction agentPaymentTransaction = buildAgroTransaction(
								procurement.getAgroTransaction());
						agentPaymentTransaction.setProfType(Profile.AGENT);
						/*
						 * if (!ObjectUtil.isEmpty(agent) &&
						 * agent.isCoOperativeManager()) {
						 * agentPaymentTransaction.setProfType(Profile.
						 * CO_OPEARATIVE_MANAGER); }
						 */

						/*
						 * if
						 * (!StringUtil.isEmpty(procurement.getPaymentAmount())
						 * && procurement.getPaymentAmount() > 0.00) { if
						 * (procurement.getPaymentMode().equals(ESEAccount.
						 * PAYMENT_MODE_CASH)) { if
						 * (procurement.getTotalProVal() ==
						 * procurement.getPaymentAmount()) {
						 * 
						 * agentPaymentTransaction.calculateBalance(
						 * agentAccount, procurement.getPaymentAmount(), true,
						 * false);
						 * productDistributionDAO.save(agentPaymentTransaction);
						 * 
						 * AgroTransaction farmerPaymentTransaction =
						 * buildAgroTransaction(
						 * procurement.getAgroTransaction());
						 * farmerPaymentTransaction.calculateBalance(
						 * farmerAccount, procurement.getPaymentAmount(), true,
						 * true);
						 * productDistributionDAO.save(farmerPaymentTransaction)
						 * ;
						 * 
						 * } else if (procurement.getTotalProVal() >
						 * procurement.getPaymentAmount()) {
						 * 
						 * agentPaymentTransaction.calculateBalance(
						 * agentAccount, procurement.getPaymentAmount(), true,
						 * false);
						 * agentPaymentTransaction.calculateCreditBalance(
						 * agentAccount, procurement.getTotalProVal() -
						 * procurement.getPaymentAmount(), false, false);
						 * productDistributionDAO.save(agentPaymentTransaction);
						 * 
						 * AgroTransaction farmerPaymentTransaction =
						 * buildAgroTransaction(
						 * procurement.getAgroTransaction());
						 * farmerPaymentTransaction.calculateBalance(
						 * farmerAccount, procurement.getPaymentAmount(), true,
						 * true);
						 * farmerPaymentTransaction.calculateCreditBalance(
						 * farmerAccount, procurement.getTotalProVal() -
						 * procurement.getPaymentAmount(), false, true);
						 * productDistributionDAO.save(farmerPaymentTransaction)
						 * ;
						 * 
						 * }
						 * 
						 * }
						 * 
						 * } else
						 */ if (procurement.getPaymentMode().equals(ESEAccount.PAYMENT_MODE_CREDIT)) {

							agentPaymentTransaction.calculateCreditBalance(agentAccount, procurement.getTotalProVal(),
									false, false);
							productDistributionDAO.save(agentPaymentTransaction);

							AgroTransaction farmerPaymentTransaction = buildAgroTransaction(
									procurement.getAgroTransaction());
							farmerPaymentTransaction.calculateCreditBalance(farmerAccount, procurement.getTotalProVal(),
									false, true);
							productDistributionDAO.save(farmerPaymentTransaction);

						}

					}
				}

			} else if (object instanceof Distribution) {
				Distribution distribution = (Distribution) object;

				farmer = farmerDAO.findFarmerByFarmerId(distribution.getAgroTransaction().getFarmerId());

				if (!ObjectUtil.isEmpty(season) && !ObjectUtil.isEmpty(farmer)) {
					farmerAccount = farmerDAO.findAccountBySeassonProcurmentProductFarmer(season.getId(), 0L,
							farmer.getId());
				}

				updateInterestConsolidated(distribution);

				// Checks whether is payment available
				if (distribution.isPaymentAvailable()) {

					if (distribution.getAgroTransaction().getProductStock()
							.equalsIgnoreCase(WarehouseProduct.StockType.AGENT_STOCK.name())) {

						Agent agent = agentDAO.findAgentByProfileId(distribution.getAgroTransaction().getAgentId());
						if (!ObjectUtil.isEmpty(agent)) {
							agentAccount = accountDAO.findAccountByProfileIdAndProfileType(agent.getProfileId(),
									ESEAccount.AGENT_ACCOUNT);
							if (!ObjectUtil.isEmpty(agentAccount)) {
								distribution.getAgroTransaction().setTxnDesc(Distribution.DISTRIBUTION_PAYMENT_AMOUNT);
								// Agent account will be added with payment
								// amount
								AgroTransaction agentPaymentTransaction = buildAgroTransaction(
										distribution.getAgroTransaction());
								agentPaymentTransaction.setProfType(Profile.AGENT);
								if (!ObjectUtil.isEmpty(agent) && agent.isCoOperativeManager()) {
									agentPaymentTransaction.setProfType(Profile.CO_OPEARATIVE_MANAGER);
								}
								agentPaymentTransaction.calculateBalance(agentAccount, distribution.getPaymentAmount(),
										false, true);
								productDistributionDAO.save(agentPaymentTransaction);
								// Farmer account will be detected with
								// transaction
								// amount
								distribution.getAgroTransaction().calculateBalance(farmerAccount,
										distribution.getAgroTransaction().getTxnAmount(), false, false);
								productDistributionDAO.save(distribution.getAgroTransaction());

								// Farmer account will be added with payment
								// amount
								AgroTransaction farmerPaymentTransaction = buildAgroTransaction(
										distribution.getAgroTransaction());
								farmerPaymentTransaction.calculateBalance(farmerAccount,
										distribution.getPaymentAmount(), false, true);
								productDistributionDAO.save(farmerPaymentTransaction);

								ESESystem preferences = systemDAO.findPrefernceById("1");
								String isInterestModule = preferences.getPreferences().get(ESESystem.INTEREST_MODULE);
								if (isInterestModule.equalsIgnoreCase("1")) {
									InterestCalcConsolidated intCalConsolidated = farmerDAO
											.findInterestCalcConsolidatedByfarmerProfileId(
													distribution.getAgroTransaction().getFarmerId());
									double paymentAmt = distribution.getPaymentAmount();
									if (!ObjectUtil.isEmpty(intCalConsolidated)) {
										if (intCalConsolidated.getAccumulatedIntAmount() > 0) {
											double remainAmt = intCalConsolidated.getAccumulatedIntAmount()
													- paymentAmt;
											if (remainAmt >= 0) {
												intCalConsolidated.setAccumulatedIntAmount(remainAmt);
												intCalConsolidated.setUpdateUserName(
														distribution.getAgroTransaction().getAgentId());
												intCalConsolidated.setLastUpdateDt(new Date());
												intCalConsolidated.setRevisionNo(DateUtil.getRevisionNumber());
												farmerDAO.update(intCalConsolidated);
											} else if (remainAmt < 0) {
												intCalConsolidated.setAccumulatedIntAmount(0);
												intCalConsolidated.setAccumulatedPrincipalAmount(
														intCalConsolidated.getAccumulatedPrincipalAmount()
																- Math.abs(remainAmt));
												intCalConsolidated.setUpdateUserName(
														distribution.getAgroTransaction().getAgentId());
												intCalConsolidated.setLastUpdateDt(new Date());
												intCalConsolidated.setRevisionNo(DateUtil.getRevisionNumber());
												farmerDAO.update(intCalConsolidated);
											}
										} else {
											intCalConsolidated.setAccumulatedPrincipalAmount(
													intCalConsolidated.getAccumulatedPrincipalAmount() - paymentAmt);
											intCalConsolidated
													.setUpdateUserName(distribution.getAgroTransaction().getAgentId());
											intCalConsolidated.setLastUpdateDt(new Date());
											intCalConsolidated.setRevisionNo(DateUtil.getRevisionNumber());
											farmerDAO.update(intCalConsolidated);
										}
									}
								}
							}
						}
					} else if (distribution.getAgroTransaction().getProductStock().equalsIgnoreCase(
							WarehouseProduct.StockType.WAREHOUSE_STOCK.name()) && !ObjectUtil.isEmpty(farmerAccount)) {
						// Farmer account will be detected with transaction
						// amount
						distribution.getAgroTransaction().calculateBalance(farmerAccount,
								distribution.getAgroTransaction().getTxnAmount(), false, false);
						productDistributionDAO.save(distribution.getAgroTransaction());
						distribution.getAgroTransaction().setTxnDesc(Distribution.DISTRIBUTION_PAYMENT_AMOUNT);
						// Farmer account will be added with payment amount
						AgroTransaction farmerPaymentTransaction = buildAgroTransaction(
								distribution.getAgroTransaction());
						farmerPaymentTransaction.calculateBalance(farmerAccount, distribution.getPaymentAmount(), false,
								true);
						productDistributionDAO.save(farmerPaymentTransaction);

						ESESystem preferences = systemDAO.findPrefernceById("1");
						String isInterestModule = preferences.getPreferences().get(ESESystem.INTEREST_MODULE);
						if (isInterestModule.equalsIgnoreCase("1")) {
							InterestCalcConsolidated intCalConsolidated = farmerDAO
									.findInterestCalcConsolidatedByfarmerProfileId(
											distribution.getAgroTransaction().getFarmerId());
							double paymentAmt = distribution.getPaymentAmount();
							if (!ObjectUtil.isEmpty(intCalConsolidated)) {
								if (intCalConsolidated.getAccumulatedIntAmount() > 0) {
									double remainAmt = intCalConsolidated.getAccumulatedIntAmount() - paymentAmt;
									if (remainAmt >= 0) {
										intCalConsolidated.setAccumulatedIntAmount(remainAmt);
										intCalConsolidated
												.setUpdateUserName(distribution.getAgroTransaction().getAgentId());
										intCalConsolidated.setLastUpdateDt(new Date());
										intCalConsolidated.setRevisionNo(DateUtil.getRevisionNumber());
										farmerDAO.update(intCalConsolidated);
									} else if (remainAmt < 0) {
										intCalConsolidated.setAccumulatedIntAmount(0);
										intCalConsolidated.setAccumulatedPrincipalAmount(
												intCalConsolidated.getAccumulatedPrincipalAmount()
														- Math.abs(remainAmt));
										intCalConsolidated
												.setUpdateUserName(distribution.getAgroTransaction().getAgentId());
										intCalConsolidated.setLastUpdateDt(new Date());
										intCalConsolidated.setRevisionNo(DateUtil.getRevisionNumber());
										farmerDAO.update(intCalConsolidated);
									}
								} else {
									intCalConsolidated.setAccumulatedPrincipalAmount(
											intCalConsolidated.getAccumulatedPrincipalAmount() - paymentAmt);
									intCalConsolidated
											.setUpdateUserName(distribution.getAgroTransaction().getAgentId());
									intCalConsolidated.setLastUpdateDt(new Date());
									intCalConsolidated.setRevisionNo(DateUtil.getRevisionNumber());
									farmerDAO.update(intCalConsolidated);
								}
							}
						}
					} else if (distribution.getAgroTransaction().getProductStock().equalsIgnoreCase(
							WarehouseProduct.StockType.WAREHOUSE_STOCK.name()) && ObjectUtil.isEmpty(farmerAccount)) {
						productDistributionDAO.save(distribution.getAgroTransaction());
					} else if (distribution.getAgroTransaction().getProductStock().equalsIgnoreCase(
							WarehouseProduct.StockType.AGENT_STOCK.name()) && ObjectUtil.isEmpty(farmerAccount)) {

						Agent agent = agentDAO.findAgentByProfileId(distribution.getAgroTransaction().getAgentId());
						if (!ObjectUtil.isEmpty(agent)) {
							agentAccount = accountDAO.findAccountByProfileIdAndProfileType(agent.getProfileId(),
									ESEAccount.AGENT_ACCOUNT);
							if (!ObjectUtil.isEmpty(agentAccount)) {
								// Agent account will be added with payment
								// amount
								AgroTransaction agentPaymentTransaction = buildAgroTransaction(
										distribution.getAgroTransaction());
								agentPaymentTransaction.setTxnDesc(Distribution.DISTRIBUTION_PAYMENT_AMOUNT);

								agentPaymentTransaction.setProfType(Profile.AGENT);
								if (!ObjectUtil.isEmpty(agent) && agent.isCoOperativeManager()) {
									agentPaymentTransaction.setProfType(Profile.CO_OPEARATIVE_MANAGER);
								}
								agentPaymentTransaction.calculateBalance(agentAccount, distribution.getPaymentAmount(),
										false, true);
								productDistributionDAO.save(agentPaymentTransaction);
							}
						}
						productDistributionDAO.save(distribution.getAgroTransaction());
					}
				} else {
					// Farmer account will be detected with transaction amount
					distribution.getAgroTransaction().calculateBalance(farmerAccount,
							distribution.getAgroTransaction().getTxnAmount(), false, false);
					productDistributionDAO.save(distribution.getAgroTransaction());
				}
			}
		}
	}

	/**
	 * Update interest consolidated.
	 * 
	 * @param distribution
	 *            the distribution
	 */
	private void updateInterestConsolidated(Distribution distribution) {

		ESESystem eseSystem = systemDAO.findPrefernceById("1");
		if (eseSystem.getPreferences().get(ESESystem.INTEREST_MODULE).equals("1")) {
			// Farmer Interest Amount reduce logic
			InterestCalcConsolidated intCalConsolidated = farmerDAO
					.findInterestCalcConsolidatedByfarmerProfileId(distribution.getAgroTransaction().getFarmerId());
			if (!ObjectUtil.isEmpty(intCalConsolidated)) {
				intCalConsolidated.setAccumulatedPrincipalAmount(intCalConsolidated.getAccumulatedPrincipalAmount()
						+ distribution.getAgroTransaction().getTxnAmount());
				intCalConsolidated.setLastCalcDate(new Date());

				intCalConsolidated.setUpdateUserName(distribution.getAgroTransaction().getAgentId());
				intCalConsolidated.setLastUpdateDt(new Date());
				intCalConsolidated.setRevisionNo(DateUtil.getRevisionNumber());
				farmerDAO.update(intCalConsolidated);
			}
		}
	}

	private void updateInterestConsolidated(Distribution distribution, String tenantId) {

		ESESystem eseSystem = systemDAO.findPrefernceById("1", tenantId);
		if (eseSystem.getPreferences().get(ESESystem.INTEREST_MODULE).equals("1")) {
			// Farmer Interest Amount reduce logic
			InterestCalcConsolidated intCalConsolidated = farmerDAO.findInterestCalcConsolidatedByfarmerProfileId(
					distribution.getAgroTransaction().getFarmerId(), tenantId);
			if (!ObjectUtil.isEmpty(intCalConsolidated)) {
				intCalConsolidated.setAccumulatedPrincipalAmount(intCalConsolidated.getAccumulatedPrincipalAmount()
						+ distribution.getAgroTransaction().getTxnAmount());
				intCalConsolidated.setLastCalcDate(new Date());

				intCalConsolidated.setUpdateUserName(distribution.getAgroTransaction().getAgentId());
				intCalConsolidated.setLastUpdateDt(new Date());
				intCalConsolidated.setRevisionNo(DateUtil.getRevisionNumber());
				farmerDAO.updateInterestCalcConsolidated(intCalConsolidated, tenantId);
			}
		}
	}

	/**
	 * Builds the agro transaction.
	 * 
	 * @param eAgroTransaction
	 *            the e agro transaction
	 * @return the agro transaction
	 */
	private AgroTransaction buildAgroTransaction(AgroTransaction eAgroTransaction) {

		AgroTransaction nAgroTransaction = new AgroTransaction();
		nAgroTransaction.setReceiptNo(eAgroTransaction.getReceiptNo());
		nAgroTransaction.setAgentId(eAgroTransaction.getAgentId());
		nAgroTransaction.setAgentName(eAgroTransaction.getAgentName());
		nAgroTransaction.setDeviceId(eAgroTransaction.getDeviceId());
		nAgroTransaction.setDeviceName(eAgroTransaction.getDeviceName());
		nAgroTransaction.setServicePointId(eAgroTransaction.getServicePointId());
		nAgroTransaction.setServicePointName(eAgroTransaction.getServicePointName());
		nAgroTransaction.setFarmerId(eAgroTransaction.getFarmerId());
		nAgroTransaction.setFarmerName(eAgroTransaction.getFarmerName());
		nAgroTransaction.setProfType(eAgroTransaction.getProfType());
		nAgroTransaction.setTxnType(eAgroTransaction.getTxnType());
		nAgroTransaction.setOperType(eAgroTransaction.getOperType());
		nAgroTransaction.setTxnTime(eAgroTransaction.getTxnTime());
		nAgroTransaction.setTxnDesc(eAgroTransaction.getTxnDesc());
		nAgroTransaction.setSamithi(eAgroTransaction.getSamithi());
		return nAgroTransaction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #editPricePatternDetail
	 * (com.sourcetrace.eses.order.entity.txn.PricePatternDetail, double)
	 */
	public void editPricePatternDetail(PricePatternDetail existingPricePattern, double price) {

		// Procurement Product Pricing
		GradeMasterPricingHistory pricing = new GradeMasterPricingHistory();
		pricing.setDate(new Date());
		pricing.setProduct(existingPricePattern.getPricePattern().getProcurementProduct());
		pricing.setGradeMaster(existingPricePattern.getGradeMaster());
		pricing.setPrice(existingPricePattern.getPrice());
		productDistributionDAO.save(pricing);

		existingPricePattern.setPrice(price);
		existingPricePattern.getPricePattern().setRevisionNo(DateUtil.getRevisionNumber());
		existingPricePattern.getPricePattern().setUpdatedDate(new Date());
		// Updating both PricePattern and PricePatternDetail
		productDistributionDAO.update(existingPricePattern.getPricePattern());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findPricePatternDetailById (long)
	 */
	public PricePatternDetail findPricePatternDetailById(long id) {

		return productDistributionDAO.findPricePatternDetailById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService #
	 * listGradeMasterByProcurementProductId(java.lang.Long)
	 */
	public List<GradeMaster> listGradeMasterByProcurementProductId(Long productId) {

		return productDistributionDAO.listGradeMasterByProcurementProductId(productId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #addProcurementProduct (com.ese.entity.profile.ProcurementProduct)
	 */
	public void addProcurementProduct(ProcurementProduct procurementProduct) {

		procurementProduct.setRevisionNo(DateUtil.getRevisionNumber());
		productDistributionDAO.save(procurementProduct);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #editProcurementProduct (com.ese.entity.profile.ProcurementProduct)
	 */
	public void editProcurementProduct(ProcurementProduct procurementProduct) {

		procurementProduct.setRevisionNo(DateUtil.getRevisionNumber());
		productDistributionDAO.update(procurementProduct);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #removeProcrementProduct (com.ese.entity.profile.ProcurementProduct)
	 */
	public void removeProcrementProduct(ProcurementProduct procurementProduct) {

		productDistributionDAO.delete(procurementProduct);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService #
	 * addGradeMasterWithPricePattern(com.sourcetrace.eses.order.entity.profile
	 * .GradeMaster)
	 */
	public void addGradeMasterWithPricePattern(GradeMaster gradeMaster) {

		gradeMaster.setRevisionNo(DateUtil.getRevisionNumber());
		// Price Pattern
		PricePattern pattern = new PricePattern();
		pattern.setCode(idGenerator.createPricePatternCodeSequence());
		pattern.setName(pattern.getCode());
		pattern.setCreatedDate(new Date());
		pattern.setUpdatedDate(new Date());
		pattern.setProcurementProduct(gradeMaster.getProduct());
		pattern.setRevisionNo(DateUtil.getRevisionNumber());
		pattern.setSeason(findCurrentSeason());
		// Price Pattern Detail
		PricePatternDetail detail = new PricePatternDetail();
		detail.setPricePattern(pattern);
		detail.setGradeMaster(gradeMaster);
		detail.setPrice(0d);
		pattern.setPricePatternDetails(new HashSet<PricePatternDetail>(Arrays.asList(detail)));
		productDistributionDAO.save(gradeMaster);
		productDistributionDAO.save(pattern);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #editGradeMaster(com .sourcetrace.eses.order.entity.profile.GradeMaster)
	 */
	public void editGradeMaster(GradeMaster gradeMaster) {

		gradeMaster.setRevisionNo(DateUtil.getRevisionNumber());
		productDistributionDAO.update(gradeMaster);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService #
	 * findAgroTransactionByRecNoProfTypeTxnDescAndDate(java.lang.String,
	 * java.lang.String, java.lang.String, java.util.Date)
	 */
	public AgroTransaction findAgroTransactionByRecNoProfTypeTxnDescAndDate(String recNo, String profType,
			String txnDesc, Date date) {

		return productDistributionDAO.findAgroTransactionByRecNoProfTypeTxnDescAndDate(recNo, profType, txnDesc, date);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService #
	 * findPaymentAgroTransactionByRecNoProfType(java.lang.String,
	 * java.lang.String)
	 */
	public AgroTransaction findPaymentAgroTransactionByRecNoProfType(String recNo, String profType) {

		return productDistributionDAO.findPaymentAgroTransactionByRecNoProfType(recNo, profType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService # listProcurementProductByRevisionNo(long)
	 */
	public List<ProcurementProduct> listProcurementProductByRevisionNo(long revisionNo) {

		return productDistributionDAO.listProcurementProductByRevisionNo(revisionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService #
	 * listCityWarehouseByAgentIdRevisionNo(java.lang.String, long)
	 */
	public List<CityWarehouse> listCityWarehouseByAgentIdRevisionNo(String agentId, long revisionNo) {

		return productDistributionDAO.listCityWarehouseByAgentIdRevisionNo(agentId, revisionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService # listGradeMasterByRevisionNo(long)
	 */
	public List<GradeMaster> listGradeMasterByRevisionNo(long revisionNo) {

		return productDistributionDAO.listGradeMasterByRevisionNo(revisionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #findLogoImageById( java.lang.Long)
	 */
	public byte[] findLogoImageById(Long id) {

		return productDistributionDAO.findLogoImageById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService
	 * #listSeasonByRevisionNo (long)
	 */
	public List<Season> listSeasonByRevisionNo(long revisionNo) {

		return productDistributionDAO.listSeasonByRevisionNo(revisionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService#
	 * listProductsByCoOperative (long)
	 */
	public List<CityWarehouse> listProductsByCoOperative(long coOperativeId) {

		// TODO Auto-generated method stub
		return productDistributionDAO.listProductsByCoOperative(coOperativeId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService#
	 * findCityWareHouseByGrade (java.lang.String, long, long, java.lang.String)
	 */
	public CityWarehouse findCityWareHouseByGrade(String agentId, long coOperativeId, long productId,
			String gradeCode) {

		// TODO Auto-generated method stub
		return productDistributionDAO.findCityWareHouseByGrade(agentId, coOperativeId, productId, gradeCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService#
	 * findPrecurementProductStock(java.lang.String, long, long)
	 */
	public double findPrecurementProductStock(String agentId, long coOperativeId, long productId) {

		// TODO Auto-generated method stub
		return productDistributionDAO.findPrecurementProductStock(agentId, coOperativeId, productId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService#
	 * findPrecurementProductStockNetWht(java.lang.String, long, long)
	 */
	public double findPrecurementProductStockNetWht(String agentId, long coOperativeId, long productId) {

		// TODO Auto-generated method stub
		return productDistributionDAO.findPrecurementProductStockNetWht(agentId, coOperativeId, productId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService#
	 * findDistributionByRecNoAndTxnType(java.lang.String, java.lang.String)
	 */
	public Distribution findDistributionByRecNoAndTxnType(String receiptNumber, String txnType) {

		return productDistributionDAO.findDistributionByRecNoAndTxnType(receiptNumber, txnType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService# findTxnAgroAudioFileByTxnAgroId(long)
	 */
	public byte[] findTxnAgroAudioFileByTxnAgroId(long id) {

		return productDistributionDAO.findTxnAgroAudioFileByTxnAgroId(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService#
	 * findAgentMovementById (long)
	 */
	public AgentMovement findAgentMovementById(long id) {

		return productDistributionDAO.findAgentMovementById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService#
	 * findFieldStaffAvailableStock(java.lang.String, long)
	 */
	public WarehouseProduct findFieldStaffAvailableStock(String agentId, long productId) {

		return productDistributionDAO.findFieldStaffAvailableStock(agentId, productId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService#
	 * addProcurementGrade (com.ese.entity.profile.ProcurementGrade)
	 */
	public void addProcurementGrade(ProcurementGrade procurementGrade) {

		procurementGrade.setRevisionNo(DateUtil.getRevisionNumber());
		productDistributionDAO.save(procurementGrade);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService#
	 * addProcurementVariety (com.ese.entity.profile.ProcurementVariety)
	 */
	public void addProcurementVariety(ProcurementVariety procurementVariety) {

		procurementVariety.setRevisionNo(DateUtil.getRevisionNumber());
		productDistributionDAO.save(procurementVariety);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService#
	 * addprocurementGradePricingHistory(com.ese.entity.profile.
	 * ProcurementGradePricingHistory)
	 */
	public void addprocurementGradePricingHistory(ProcurementGradePricingHistory procurementGradePricingHistory) {

		procurementGradePricingHistory.setRevisionNo(DateUtil.getRevisionNumber());
		productDistributionDAO.save(procurementGradePricingHistory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService#
	 * editProcurementGrade (com.ese.entity.profile.ProcurementGrade)
	 */
	public void editProcurementGrade(ProcurementGrade procurementGrade) {

		procurementGrade.setRevisionNo(DateUtil.getRevisionNumber());
		productDistributionDAO.update(procurementGrade);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService#
	 * editProcurementVariety (com.ese.entity.profile.ProcurementVariety)
	 */
	public void editProcurementVariety(ProcurementVariety procurementVariety) {

		procurementVariety.setRevisionNo(DateUtil.getRevisionNumber());
		productDistributionDAO.update(procurementVariety);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService#
	 * findProcurementGradeByCode (java.lang.String, java.lang.String)
	 */
	public ProcurementGrade findProcurementGradeByCode(String productCode) {

		// TODO Auto-generated method stub
		return productDistributionDAO.findProcurementGradeByCode(productCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService#
	 * findProcurementGradeById (java.lang.Long)
	 */
	public ProcurementGrade findProcurementGradeById(Long id) {

		// TODO Auto-generated method stub
		return productDistributionDAO.findProcurementGradeById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService#
	 * findProcurementGradeByName (java.lang.String)
	 */
	public ProcurementGrade findProcurementGradeByName(String name) {

		// TODO Auto-generated method stub
		return productDistributionDAO.findProcurementGradeByName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService#
	 * findProcurementGradeByPrice(java.lang.Double)
	 */
	public ProcurementGrade findProcurementGradeByPrice(Double rate) {

		return productDistributionDAO.findProcurementGradeByPrice(rate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService#
	 * findProcurementVariertyByCode(java.lang.String)
	 */
	public ProcurementVariety findProcurementVariertyByCode(String productCode) {

		return productDistributionDAO.findProcurementVariertyByCode(productCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService# findProcurementVariertyById(java.lang.Long)
	 */
	public ProcurementVariety findProcurementVariertyById(Long id) {

		return productDistributionDAO.findProcurementVariertyById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService#
	 * findProcurementVariertyByName(java.lang.String)
	 */
	public ProcurementVariety findProcurementVariertyByName(String name) {

		return productDistributionDAO.findProcurementVariertyByName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService#
	 * listProcurementProductGradeByRevisionNo(java.lang.Long)
	 */
	public List<ProcurementGrade> listProcurementProductGradeByRevisionNo(Long revisionNo) {

		return productDistributionDAO.listProcurementProductGradeByRevisionNo(revisionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService#
	 * listProcurementProductVarietyByRevisionNo(java.lang.Long)
	 */
	public List<ProcurementVariety> listProcurementProductVarietyByRevisionNo(Long revisionNo) {

		return productDistributionDAO.listProcurementProductVarietyByRevisionNo(revisionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService#
	 * listProcurementProductGradeByProcurementProductVarietyId(java.lang.Long,
	 * java.lang.Long)
	 */
	public List<ProcurementGrade> listProcurementProductGradeByProcurementProductVarietyId(Long id, Long revisionNo) {

		return productDistributionDAO.listProcurementProductGradeByProcurementProductVarietyId(id, revisionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService#
	 * listProcurementProductVarietyByProcurementProductId(long, java.lang.Long)
	 */
	public List<ProcurementVariety> listProcurementProductVarietyByProcurementProductId(long id, Long revisionNo) {

		return productDistributionDAO.listProcurementProductVarietyByProcurementProductId(id, revisionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService#
	 * findProcurementVariertyByCropCode(java.lang.String)
	 */
	public List<ProcurementVariety> findProcurementVariertyByCropCode(String selectedCrop) {

		return productDistributionDAO.findProcurementVariertyByCropCode(selectedCrop);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService#
	 * listProcurementVarietyByProcurementProductId(java.lang.Long)
	 */
	public List<ProcurementVariety> listProcurementVarietyByProcurementProductId(Long id) {

		return productDistributionDAO.listProcurementVarietyByProcurementProductId(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService#
	 * listProcurementGradeByProcurementVarietyId(java.lang.Long)
	 */
	public List<ProcurementGrade> listProcurementGradeByProcurementVarietyId(Long id) {

		return productDistributionDAO.listProcurementGradeByProcurementVarietyId(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService#
	 * listPriceByVarAndGrade (long, long, long)
	 */
	public ProcurementGrade listPriceByVarAndGrade(long procurementProductId, long procurementVarId,
			long procurementGradeId) {

		return productDistributionDAO.listPriceByVarAndGrade(procurementProductId, procurementVarId,
				procurementGradeId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService#
	 * listProcurementVariety ()
	 */
	public List<ProcurementVariety> listProcurementVariety() {

		return productDistributionDAO.listProcurementVariety();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService#
	 * listProcurementGrade()
	 */
	public List<ProcurementGrade> listProcurementGrade() {

		return productDistributionDAO.listProcurementGrade();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService#
	 * listProcurementGradeByProcurementProductId(long)
	 */
	public List<ProcurementGrade> listProcurementGradeByProcurementProductId(long productId) {

		return productDistributionDAO.listProcurementGradeByProcurementProductId(productId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService#
	 * findCityWareHouseByWarehouseIdProcrmentGradeCodeAndProcurementProductId(
	 * long, java.lang.String, long)
	 */
	public CityWarehouse findCityWareHouseByWarehouseIdProcrmentGradeCodeAndProcurementProductId(long warehouseId,
			String gradeCode, long productId) {

		return productDistributionDAO.findCityWareHouseByWarehouseIdProcrmentGradeCodeAndProcurementProductId(
				warehouseId, gradeCode, productId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.
	 * IProductDistributionService#
	 * findPrecurementProductStockNetWhtByWarehouseIdAndProductId(long, long)
	 */
	public double findPrecurementProductStockNetWhtByWarehouseIdAndProductId(long warehouseId, long productId) {

		return productDistributionDAO.findPrecurementProductStockNetWhtByWarehouseIdAndProductId(warehouseId,
				productId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductDistributionService#
	 * processWarehousePaymentStock(com.ese.entity.profile.WarehousePayment)
	 */
	public void processWarehousePaymentStock(WarehousePayment warehousePayment) {
		List<AgroTransaction> agroTransactionList = buildAgroTransactionObject(warehousePayment);
		for (AgroTransaction agroTransaction : agroTransactionList) {
			productDistributionDAO.save(agroTransaction);
			// productDistributionDAO.save(agroTransaction.getRefAgroTransaction());
			if (agroTransaction.getAccount() != null) {
				accountDAO.updateCashBal(agroTransaction.getAccount().getId(),
						agroTransaction.getAccount().getCashBalance(), agroTransaction.getAccount().getCreditBalance());
				// productDistributionDAO.saveOrUpdate(agroTransaction.getRefAgroTransaction().getAccount());
				// warehousePayment.setAgroTransaction(agroTransaction);
			}
		}
		productDistributionDAO.save(warehousePayment);
	}

	/**
	 * Build agro transaction object.
	 * 
	 * @param warehousePayment
	 * @return the list< agro transaction>
	 */
	public List<AgroTransaction> buildAgroTransactionObject(WarehousePayment warehousePayment) {

		List<AgroTransaction> agroTransactionList = new ArrayList<AgroTransaction>();
		// AgroTransaction agroTransactionMaster = null;
		// AgroTransaction agroTransactionChild = null;
		double finalAmount = (!StringUtil.isEmpty(warehousePayment.getFinalAmount()) ? warehousePayment.getFinalAmount()
				: 0D);
		double paymentAmount = (!StringUtil.isEmpty(warehousePayment.getPaymentAmount())
				? warehousePayment.getPaymentAmount() : 0D);

		if (warehousePayment.getPaymentMode().equals(ESEAccount.PAYMENT_MODE_CASH)) {
			if (finalAmount == paymentAmount) {
				// agroTransactionMaster =
				// buildCashAgroTransactionObject(warehousePayment,
				// warehousePayment.getPaymentAmount());
				agroTransactionList
						.addAll(buildCashAgroTransactionObject(warehousePayment, warehousePayment.getPaymentAmount()));
			} else if (warehousePayment.getFinalAmount() > warehousePayment.getPaymentAmount()) {
				// agroTransactionMaster =
				// buildCashAgroTransactionObject(warehousePayment,
				// warehousePayment.getPaymentAmount());
				// agroTransactionChild =
				// buildCreditAgroTransactionObject(warehousePayment,
				// -(warehousePayment.getFinalAmount()-warehousePayment.getPaymentAmount()));
				agroTransactionList
						.addAll(buildCashAgroTransactionObject(warehousePayment, warehousePayment.getPaymentAmount()));
				agroTransactionList.addAll(buildCreditAgroTransactionObject(warehousePayment,
						-(warehousePayment.getFinalAmount() - warehousePayment.getPaymentAmount())));
			} else if (warehousePayment.getFinalAmount() < warehousePayment.getPaymentAmount()) {
				// agroTransactionMaster =
				// buildCashAgroTransactionObject(warehousePayment,
				// warehousePayment.getPaymentAmount());
				// agroTransactionChild=
				// buildCreditAgroTransactionObject(warehousePayment,
				// (warehousePayment.getPaymentAmount()-warehousePayment.getFinalAmount()));

				agroTransactionList
						.addAll(buildCashAgroTransactionObject(warehousePayment, warehousePayment.getPaymentAmount()));
				agroTransactionList.addAll(buildCreditAgroTransactionObject(warehousePayment,
						(warehousePayment.getPaymentAmount() - warehousePayment.getFinalAmount())));
			}
		} else if (warehousePayment.getPaymentMode().equals(ESEAccount.PAYMENT_MODE_CREDIT)) {
			// agroTransactionMaster =
			// buildCreditAgroTransactionObject(warehousePayment,
			// warehousePayment.getPaymentAmount());
			agroTransactionList
					.addAll(buildCreditAgroTransactionObject(warehousePayment, -(warehousePayment.getPaymentAmount())));
		}
		return agroTransactionList;
	}

	/**
	 * Build cash agro transaction object.
	 * 
	 * @param warehousePayment
	 * @param trxnAmount
	 * @return the agro transaction
	 */
	public List<AgroTransaction> buildCashAgroTransactionObject(WarehousePayment warehousePayment, double trxnAmount) {

		List<AgroTransaction> agroTransactionList = new ArrayList<AgroTransaction>();
		try {
			// Company Account Process
			String cpProfId = "BASIX";
			ESEAccount companyAccount = accountDAO.findAccountByProfileId(1L);
			if (ObjectUtil.isEmpty(companyAccount)) {

			}
			AgroTransaction agroTransactionOrg = new AgroTransaction();
			agroTransactionOrg.setReceiptNo(warehousePayment.getReceiptNo());
			agroTransactionOrg.setAgentId("BASIX");
			agroTransactionOrg.setAgentName("BASIX");
			agroTransactionOrg.setDeviceId("N/A");
			agroTransactionOrg.setDeviceName("N/A");
			agroTransactionOrg.setServicePointId(null);
			agroTransactionOrg.setServicePointName(null);
			agroTransactionOrg.setFarmerId(null);
			agroTransactionOrg.setFarmerName(null);
			agroTransactionOrg.setVendorId(warehousePayment.getVendor().getVendorId());
			agroTransactionOrg.setVendorName(warehousePayment.getVendor().getVendorName());
			agroTransactionOrg.setProfType(null);
			agroTransactionOrg.setOperType(1);
			agroTransactionOrg.setTxnTime(warehousePayment.getTrxnDate());
			agroTransactionOrg.setTxnDesc("WAREHOUSE STOCK ENTRY : CASH PAYMENT");
			agroTransactionOrg.setModeOfPayment(ESEAccount.PAYMENT_MODE_CASH);
			agroTransactionOrg.setIntBalance(companyAccount.getCashBalance());
			agroTransactionOrg.setTxnAmount(trxnAmount);
			agroTransactionOrg.setBalAmount(getCashBalance(companyAccount.getCashBalance(), trxnAmount, false));
			agroTransactionOrg.setAccount(companyAccount);
			agroTransactionOrg.setTxnType("363ORG");

			companyAccount.setCashBalance(agroTransactionOrg.getBalAmount());

			// Vendor Account Process
			ESEAccount vendorAccount = accountDAO.findAccountByProfileIdAndProfileType(
					warehousePayment.getVendor().getVendorId(), ESEAccount.VENDOR_ACCOUNT);
			if (ObjectUtil.isEmpty(vendorAccount)) {

			}
			AgroTransaction agroTransactionVendor = new AgroTransaction();
			agroTransactionVendor.setReceiptNo(warehousePayment.getReceiptNo());
			agroTransactionVendor.setAgentId("BASIX");
			agroTransactionVendor.setAgentName("BASIX");
			agroTransactionVendor.setDeviceId("N/A");
			agroTransactionVendor.setDeviceName("N/A");
			agroTransactionVendor.setServicePointId(null);
			agroTransactionVendor.setServicePointName(null);
			agroTransactionVendor.setFarmerId(null);
			agroTransactionVendor.setFarmerName(null);
			agroTransactionVendor.setVendorId(warehousePayment.getVendor().getVendorId());
			agroTransactionVendor.setVendorName(warehousePayment.getVendor().getVendorName());
			agroTransactionVendor.setProfType(null);
			agroTransactionVendor.setOperType(1);
			agroTransactionVendor.setTxnTime(warehousePayment.getTrxnDate());
			agroTransactionVendor.setTxnDesc("WAREHOUSE STOCK ENTRY : CASH PAYMENT");
			agroTransactionVendor.setModeOfPayment(ESEAccount.PAYMENT_MODE_CASH);
			if (!ObjectUtil.isEmpty(vendorAccount) && vendorAccount.getCashBalance() != 0.0) {
				agroTransactionVendor.setIntBalance(vendorAccount.getCashBalance());
				agroTransactionVendor.setBalAmount(getCashBalance(vendorAccount.getCashBalance(), trxnAmount, true));
			}
			agroTransactionVendor.setTxnAmount(trxnAmount);
			agroTransactionVendor.setAccount(vendorAccount);
			agroTransactionVendor.setTxnType("363V");
			if (agroTransactionVendor.getBalAmount() != 0.0) {
				vendorAccount.setCashBalance(agroTransactionVendor.getBalAmount());
			}
			// agroTransactionOrg.setRefAgroTransaction(agroTransactionVendor);

			agroTransactionList.add(agroTransactionOrg);
			agroTransactionList.add(agroTransactionVendor);

			return agroTransactionList;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Build credit agro transaction object.
	 * 
	 * @param warehousePayment
	 * @param trxnAmount
	 * @return the agro transaction
	 */
	public List<AgroTransaction> buildCreditAgroTransactionObject(WarehousePayment warehousePayment,
			double trxnAmount) {

		List<AgroTransaction> agroTransactionList = new ArrayList<AgroTransaction>();
		try {
			// Company Account Process
			String cpProfId = "BASIX";
			ESEAccount companyAccount = accountDAO.findAccountByProfileId(cpProfId);
			if (ObjectUtil.isEmpty(companyAccount)) {

			}
			AgroTransaction agroTransactionOrg = new AgroTransaction();
			agroTransactionOrg.setReceiptNo(warehousePayment.getReceiptNo());
			agroTransactionOrg.setAgentId("BASIX");
			agroTransactionOrg.setAgentName("BASIX");
			agroTransactionOrg.setDeviceId("N/A");
			agroTransactionOrg.setDeviceName("N/A");
			agroTransactionOrg.setServicePointId(null);
			agroTransactionOrg.setServicePointName(null);
			agroTransactionOrg.setFarmerId(null);
			agroTransactionOrg.setFarmerName(null);
			agroTransactionOrg.setVendorId(warehousePayment.getVendor().getVendorId());
			agroTransactionOrg.setVendorName(warehousePayment.getVendor().getVendorName());
			agroTransactionOrg.setProfType(null);
			agroTransactionOrg.setOperType(1);
			agroTransactionOrg.setTxnTime(warehousePayment.getTrxnDate());
			agroTransactionOrg.setTxnDesc("WAREHOUSE STOCK ENTRY : CREDIT PAYMENT");
			agroTransactionOrg.setModeOfPayment(ESEAccount.PAYMENT_MODE_CREDIT);

			if (!ObjectUtil.isEmpty(companyAccount)) {
				agroTransactionOrg.setIntBalance(companyAccount.getCreditBalance());

				agroTransactionOrg.setTxnAmount(trxnAmount);
				// if(trxnAmount<0){
				// agroTransactionOrg.setBalAmount(getCreditBalance(companyAccount.getCreditBalance(),
				// -trxnAmount, false));
				// }
				agroTransactionOrg.setBalAmount(getCreditBalance(companyAccount.getCreditBalance(), trxnAmount, true));
				agroTransactionOrg.setAccount(companyAccount);
				agroTransactionOrg.setTxnType("363ORG");

				companyAccount.setCreditBalance(agroTransactionOrg.getBalAmount());
			} // Vendor Account Process
			ESEAccount vendorAccount = accountDAO.findAccountByProfileIdAndProfileType(
					warehousePayment.getVendor().getVendorId(), ESEAccount.VENDOR_ACCOUNT);
			if (ObjectUtil.isEmpty(vendorAccount)) {

			}
			AgroTransaction agroTransactionVendor = new AgroTransaction();
			agroTransactionVendor.setReceiptNo(warehousePayment.getReceiptNo());
			agroTransactionVendor.setAgentId("BASIX");
			agroTransactionVendor.setAgentName("BASIX");
			agroTransactionVendor.setDeviceId("N/A");
			agroTransactionVendor.setDeviceName("N/A");
			agroTransactionVendor.setServicePointId(null);
			agroTransactionVendor.setServicePointName(null);
			agroTransactionVendor.setFarmerId(null);
			agroTransactionVendor.setFarmerName(null);
			agroTransactionVendor.setVendorId(warehousePayment.getVendor().getVendorId());
			agroTransactionVendor.setVendorName(warehousePayment.getVendor().getVendorName());
			agroTransactionVendor.setProfType(null);
			agroTransactionVendor.setOperType(1);
			agroTransactionVendor.setTxnTime(warehousePayment.getTrxnDate());
			agroTransactionVendor.setTxnDesc("WAREHOUSE STOCK ENTRY : CREDIT PAYMENT");
			agroTransactionVendor.setModeOfPayment(ESEAccount.PAYMENT_MODE_CREDIT);
			agroTransactionVendor.setTxnAmount(trxnAmount);
			// if(trxnAmount<0){
			// agroTransactionVendor.setBalAmount(getCreditBalance(vendorAccount.getCreditBalance(),
			// -trxnAmount,true));
			// }
			if (!ObjectUtil.isEmpty(vendorAccount) && vendorAccount.getCashBalance() != 0.0) {
				agroTransactionVendor.setIntBalance(vendorAccount.getCreditBalance());
				agroTransactionVendor
						.setBalAmount(getCreditBalance(vendorAccount.getCreditBalance(), trxnAmount, false));
				agroTransactionVendor.setAccount(vendorAccount);

				agroTransactionVendor.setTxnType("363V");
				if (agroTransactionVendor.getBalAmount() != 0.0) {
					vendorAccount.setCreditBalance(agroTransactionVendor.getBalAmount());
				}
			}
			// agroTransactionOrg.setRefAgroTransaction(agroTransactionVendor);

			agroTransactionList.add(agroTransactionOrg);
			agroTransactionList.add(agroTransactionVendor);

			return agroTransactionList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Credit Deposit - true | Credit Withdraw - false
	/**
	 * Gets the credit balance.
	 * 
	 * @param accountBalance
	 * @param trxnBalance
	 * @param isTxnType
	 * @return the credit balance
	 */
	public double getCreditBalance(double accountBalance, double trxnBalance, boolean isTxnType) {

		if (isTxnType) {
			return (accountBalance + trxnBalance);
		} else {
			return (accountBalance - trxnBalance);
		}
	}

	// Cash Deposit - true | Cash Withdraw - false
	/**
	 * Gets the cash balance.
	 * 
	 * @param accountBalance
	 * @param trxnBalance
	 * @param isTxnType
	 * @return the cash balance
	 */
	public double getCashBalance(double accountBalance, double trxnBalance, boolean isTxnType) {

		if (isTxnType) {
			return (accountBalance + trxnBalance);
		} else {
			return (accountBalance - trxnBalance);
		}
	}

	public List<Vendor> listVendor() {

		return productDistributionDAO.listVendor();
	}

	public WarehousePayment findWarehouseStockByRecNo(String receiptNumber) {

		return productDistributionDAO.findWarehouseStockByRecNo(receiptNumber);
	}

	public List<AgroTransaction> buildAgroTransactionDistributionObject(Distribution distribution) {

		List<AgroTransaction> agroTransactionList = new ArrayList<AgroTransaction>();
		if ("0".equalsIgnoreCase(distribution.getFreeDistribution())) {
			// AgroTransaction agroTransactionMaster = null;
			// AgroTransaction agroTransactionChild = null;
			double finalAmount = distribution.getFinalAmount();
			double paymentAmount = distribution.getPaymentAmount();

			if (distribution.getPaymentMode().equals(ESEAccount.PAYMENT_MODE_CASH)) {
				if (finalAmount == paymentAmount) {
					agroTransactionList.addAll(
							buildCashAgroTransactionDistributionObject(distribution, distribution.getPaymentAmount()));
				} else if (distribution.getFinalAmount() > distribution.getPaymentAmount()) {
					agroTransactionList.addAll(
							buildCashAgroTransactionDistributionObject(distribution, distribution.getPaymentAmount()));
					agroTransactionList.addAll(buildCreditAgroTransactionDistributionObject(distribution,
							(distribution.getFinalAmount() - distribution.getPaymentAmount())));
				} else if (distribution.getFinalAmount() < distribution.getPaymentAmount()) {

					agroTransactionList.addAll(
							buildCashAgroTransactionDistributionObject(distribution, distribution.getFinalAmount()));
					agroTransactionList.addAll(buildCreditAgroTransactionDistributionObjectForGreaterAmount1(
							distribution, (distribution.getPaymentAmount() - distribution.getFinalAmount())));
				}
			} else if (distribution.getPaymentMode().equals(ESEAccount.PAYMENT_MODE_CREDIT)) {
				agroTransactionList.addAll(
						buildCreditAgroTransactionDistributionObject(distribution, (distribution.getPaymentAmount())));
			}
		} else if ("1".equalsIgnoreCase(distribution.getFreeDistribution())) {

			AgroTransaction freeAgroTxn = new AgroTransaction();
			freeAgroTxn.setReceiptNo(distribution.getAgroTransaction().getReceiptNo());
			freeAgroTxn.setAgentId(StringUtil.isEmpty(distribution.getAgroTransaction().getAgentId()) ? "N/A"
					: distribution.getAgroTransaction().getAgentId());
			freeAgroTxn.setAgentName(StringUtil.isEmpty(distribution.getAgroTransaction().getAgentName()) ? "N/A"
					: distribution.getAgroTransaction().getAgentName());
			freeAgroTxn.setDeviceId(StringUtil.isEmpty(distribution.getAgroTransaction().getDeviceId()) ? "N/A"
					: distribution.getAgroTransaction().getDeviceId());
			freeAgroTxn.setDeviceName(StringUtil.isEmpty(distribution.getAgroTransaction().getDeviceName()) ? "N/A"
					: distribution.getAgroTransaction().getDeviceName());
			freeAgroTxn.setServicePointId(StringUtil.isEmpty(distribution.getAgroTransaction().getServicePointId())
					? "N/A" : distribution.getAgroTransaction().getServicePointId());
			freeAgroTxn.setServicePointName(StringUtil.isEmpty(distribution.getAgroTransaction().getServicePointName())
					? "N/A" : distribution.getAgroTransaction().getServicePointName());
			freeAgroTxn.setFarmerId(distribution.getAgroTransaction().getFarmerId());
			freeAgroTxn.setFarmerName(distribution.getAgroTransaction().getFarmerName());
			freeAgroTxn.setVendorId(null);
			freeAgroTxn.setVendorName(null);
			freeAgroTxn.setProfType(null);
			freeAgroTxn.setOperType(1);
			freeAgroTxn.setTxnTime(distribution.getAgroTransaction().getTxnTime());
			freeAgroTxn.setTxnDesc("FREE PRODUCT DISTRIBUTION");
			freeAgroTxn.setModeOfPayment("N/A");
			freeAgroTxn.setIntBalance(0.00);
			freeAgroTxn.setTxnAmount(0.00);
			freeAgroTxn.setBalAmount(0.00);
			freeAgroTxn.setAccount(null);
			freeAgroTxn.setTxnType(distribution.getAgroTransaction().getTxnType());
			agroTransactionList.add(freeAgroTxn);

		}
		return agroTransactionList;

	}

	private Collection<AgroTransaction> buildCreditAgroTransactionDistributionObject(Distribution distribution,
			double trxnAmount) {
		List<AgroTransaction> agroTransactionList = new ArrayList<AgroTransaction>();
		try {
			if (distribution.getAgroTransaction().getServicePointId() != null
					&& (!StringUtil.isEmpty(distribution.getAgroTransaction().getServicePointId()))) {
				// Company Account Process
				String cpProfId = "BASIX";
				ESEAccount companyAccount = accountDAO.findAccountByProfileId(cpProfId);
				if (ObjectUtil.isEmpty(companyAccount)) {

				}
				AgroTransaction agroTransactionOrg = new AgroTransaction();
				agroTransactionOrg.setReceiptNo(distribution.getAgroTransaction().getReceiptNo());
				agroTransactionOrg.setAgentId("N/A");
				agroTransactionOrg.setAgentName("N/A");
				agroTransactionOrg.setDeviceId("N/A");
				agroTransactionOrg.setDeviceName("N/A");
				agroTransactionOrg.setServicePointId(distribution.getAgroTransaction().getServicePointId());
				agroTransactionOrg.setServicePointName(distribution.getAgroTransaction().getServicePointName());
				agroTransactionOrg.setFarmerId(distribution.getAgroTransaction().getFarmerId());
				agroTransactionOrg.setFarmerName(distribution.getAgroTransaction().getFarmerName());
				agroTransactionOrg.setVendorId(null);
				agroTransactionOrg.setVendorName(null);
				agroTransactionOrg.setProfType(null);
				agroTransactionOrg.setOperType(1);
				agroTransactionOrg.setTxnTime(distribution.getAgroTransaction().getTxnTime());
				agroTransactionOrg.setTxnDesc("PRODUCT DISTRIBUTION : CREDIT PAYMENT");
				agroTransactionOrg.setModeOfPayment(ESEAccount.PAYMENT_MODE_CREDIT);
				agroTransactionOrg.setIntBalance(companyAccount.getCreditBalance());
				agroTransactionOrg.setTxnAmount(trxnAmount);
				// if((distribution.getAgroTransaction().getTxnAmount())<0){
				// agroTransactionOrg.setBalAmount(getCreditBalance(companyAccount.getCreditBalance(),
				// trxnAmount, true));
				// }
				agroTransactionOrg.setBalAmount(getCreditBalance(companyAccount.getCreditBalance(), trxnAmount, false));
				agroTransactionOrg.setAccount(companyAccount);
				agroTransactionOrg.setTxnType(distribution.getAgroTransaction().getTxnType());
				String approved = productDistributionDAO.findPrefernceByName(ESESystem.ENABLE_APPROVED);
				if (approved.equalsIgnoreCase("1")) {
					// companyAccount.setCreditBalance(agroTransactionOrg.getBalAmount());
				} else {
					companyAccount.setCreditBalance(agroTransactionOrg.getBalAmount());
				}
				agroTransactionList.add(agroTransactionOrg);

			}

			// Farmer Account Process
			Farmer farmer = farmerDAO.findFarmerByFarmerId(distribution.getAgroTransaction().getFarmerId());
			Season season = findCurrentSeason();
			if (!ObjectUtil.isEmpty(farmer) && !ObjectUtil.isEmpty(season)) {
				ESEAccount farmerAccount = farmerDAO.findAccountBySeassonProcurmentProductFarmer(season.getId(), 0L,
						farmer.getId());
				if (!ObjectUtil.isEmpty(farmerAccount)) {

				}
				AgroTransaction agroTransactionFarmer = new AgroTransaction();

				agroTransactionFarmer.setReceiptNo(distribution.getAgroTransaction().getReceiptNo());
				agroTransactionFarmer.setAgentId("N/A");
				agroTransactionFarmer.setAgentName("N/A");
				agroTransactionFarmer.setDeviceId("N/A");
				agroTransactionFarmer.setDeviceName("N/A");
				agroTransactionFarmer.setServicePointId(null);
				agroTransactionFarmer.setServicePointName(null);
				agroTransactionFarmer.setFarmerId(distribution.getAgroTransaction().getFarmerId());
				agroTransactionFarmer.setFarmerName(distribution.getAgroTransaction().getFarmerName());
				agroTransactionFarmer.setVendorId(null);
				agroTransactionFarmer.setVendorName(null);
				agroTransactionFarmer.setProfType(null);
				agroTransactionFarmer.setOperType(1);
				agroTransactionFarmer.setTxnTime(distribution.getAgroTransaction().getTxnTime());
				agroTransactionFarmer.setTxnDesc("FARMER PRODUCT DISTRIBUTION : CREDIT PAYMENT");
				agroTransactionFarmer.setModeOfPayment(ESEAccount.PAYMENT_MODE_CREDIT);
				agroTransactionFarmer.setIntBalance(farmerAccount.getCreditBalance());
				agroTransactionFarmer.setTxnAmount(trxnAmount);
				if (farmerAccount.getCreditBalance() >= trxnAmount) {
					agroTransactionFarmer
							.setBalAmount(getCreditBalance(farmerAccount.getCreditBalance(), -(trxnAmount), true));
				} else {
					agroTransactionFarmer
							.setBalAmount(getCreditBalance(farmerAccount.getCreditBalance(), trxnAmount, false));
				}
				agroTransactionFarmer.setAccount(farmerAccount);
				agroTransactionFarmer.setTxnType(Distribution.PRODUCT_DISTRIBUTION_TO_FARMER);
				String approved = productDistributionDAO.findPrefernceByName(ESESystem.ENABLE_APPROVED);
				if (approved.equalsIgnoreCase("1")) {
					// farmerAccount.setCreditBalance(agroTransactionFarmer.getBalAmount());
				} else {
					farmerAccount.setCreditBalance(agroTransactionFarmer.getBalAmount());
				}

				agroTransactionList.add(agroTransactionFarmer);

				if (distribution.getAgroTransaction().getAgentId() != null) {

					ESEAccount agentAccount = accountDAO.findAccountByProfileIdAndProfileType(
							distribution.getAgroTransaction().getAgentId(), ESEAccount.AGENT_ACCOUNT);
					if (!ObjectUtil.isEmpty(agentAccount)) {
						AgroTransaction agroTransactAgent = new AgroTransaction();
						agroTransactAgent.setReceiptNo(distribution.getAgroTransaction().getReceiptNo());
						agroTransactAgent.setAgentId(distribution.getAgroTransaction().getAgentId());
						agroTransactAgent.setAgentName(distribution.getAgroTransaction().getAgentName());
						agroTransactAgent.setDeviceId(distribution.getAgroTransaction().getDeviceId());
						agroTransactAgent.setDeviceName(distribution.getAgroTransaction().getDeviceName());
						agroTransactAgent.setServicePointId(null);
						agroTransactAgent.setServicePointName(null);
						agroTransactAgent.setFarmerId(distribution.getAgroTransaction().getFarmerId());
						agroTransactAgent.setFarmerName(distribution.getAgroTransaction().getFarmerName());
						agroTransactAgent.setVendorId(null);
						agroTransactAgent.setVendorName(null);
						agroTransactAgent.setProfType(null);
						agroTransactAgent.setOperType(1);
						agroTransactAgent.setTxnTime(distribution.getAgroTransaction().getTxnTime());
						agroTransactAgent.setTxnDesc("FIELDSTAFF PRODUCT DISTRIBUTION : CREDIT PAYMENT");
						agroTransactAgent.setModeOfPayment(ESEAccount.PAYMENT_MODE_CREDIT);
						agroTransactAgent.setIntBalance(agentAccount.getCreditBalance());
						agroTransactAgent.setTxnAmount(trxnAmount);
						agroTransactAgent
								.setBalAmount(getCreditBalance(agentAccount.getCreditBalance(), trxnAmount, false));
						agroTransactAgent.setAccount(agentAccount);
						agroTransactAgent.setTxnType(Distribution.PRODUCT_DISTRIBUTION_TO_FIELDSTAFF);
						// String
						// approved=productDistributionDAO.findPrefernceByName(ESESystem.ENABLE_APPROVED);
						if (approved.equalsIgnoreCase("1")) {
							// agentAccount.setCreditBalance(agroTransactAgent.getBalAmount());
						} else {
							agentAccount.setCreditBalance(agroTransactAgent.getBalAmount());
						}
						agroTransactionList.add(agroTransactAgent);

					}
				}
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return agroTransactionList;
	}

	private Collection<AgroTransaction> buildCashAgroTransactionDistributionObject(Distribution distribution,
			double trxnAmount) {
		AgroTransaction refAgroTransaction = new AgroTransaction();
		List<AgroTransaction> agroTransactionList = new ArrayList<AgroTransaction>();
		try {
			if (distribution.getAgroTransaction().getServicePointId() != null
					&& (!StringUtil.isEmpty(distribution.getAgroTransaction().getServicePointId()))) {
				String cmpProfId = "BASIX";
				ESEAccount companyAccount = accountDAO.findAccountByProfileId(cmpProfId);
				if (ObjectUtil.isEmpty(companyAccount)) {

				}
				refAgroTransaction.setReceiptNo(distribution.getAgroTransaction().getReceiptNo());
				refAgroTransaction.setAgentId(null);
				refAgroTransaction.setAgentName(null);
				refAgroTransaction.setDeviceId("N/A");
				refAgroTransaction.setDeviceName("N/A");
				refAgroTransaction.setServicePointId(distribution.getAgroTransaction().getServicePointId());
				refAgroTransaction.setServicePointName(distribution.getAgroTransaction().getServicePointName());
				refAgroTransaction.setFarmerId(distribution.getAgroTransaction().getFarmerId());
				refAgroTransaction.setFarmerName(distribution.getAgroTransaction().getFarmerName());
				refAgroTransaction.setVendorId(null);
				refAgroTransaction.setVendorName(null);
				refAgroTransaction.setProfType(null);
				refAgroTransaction.setOperType(1);
				refAgroTransaction.setTxnTime(distribution.getAgroTransaction().getTxnTime());
				refAgroTransaction.setTxnDesc("PRODUCT DISTRIBUTION  : CASH PAYMENT");
				refAgroTransaction.setModeOfPayment(ESEAccount.PAYMENT_MODE_CASH);
				refAgroTransaction.setIntBalance(companyAccount.getCashBalance());
				refAgroTransaction.setTxnAmount(trxnAmount);
				refAgroTransaction.setBalAmount(getCashBalance(companyAccount.getCashBalance(), trxnAmount, true));
				refAgroTransaction.setAccount(companyAccount);
				refAgroTransaction.setTxnType(distribution.getAgroTransaction().getTxnType());
				String approved = productDistributionDAO.findPrefernceByName(ESESystem.ENABLE_APPROVED);
				if (approved.equalsIgnoreCase("1")) {
					// companyAccount.setCashBalance(refAgroTransaction.getBalAmount());
				} else {
					companyAccount.setCashBalance(refAgroTransaction.getBalAmount());
				}
				agroTransactionList.add(refAgroTransaction);

			}
			// Farmer Account Process
			// AgroTransaction agroTransactionFarmer = new AgroTransaction();

			// Agent Account Process

			if (distribution.getAgroTransaction().getAgentId() != null) {

				ESEAccount agentAccount = accountDAO.findAccountByProfileIdAndProfileType(
						distribution.getAgroTransaction().getAgentId(), ESEAccount.AGENT_ACCOUNT);
				if (!ObjectUtil.isEmpty(agentAccount)) {
					AgroTransaction agroTransactAgent = new AgroTransaction();
					agroTransactAgent.setReceiptNo(distribution.getAgroTransaction().getReceiptNo());
					agroTransactAgent.setAgentId(distribution.getAgroTransaction().getAgentId());
					agroTransactAgent.setAgentName(distribution.getAgroTransaction().getAgentName());
					agroTransactAgent.setDeviceId(distribution.getAgroTransaction().getDeviceId());
					agroTransactAgent.setDeviceName(distribution.getAgroTransaction().getDeviceName());
					agroTransactAgent.setServicePointId(null);
					agroTransactAgent.setServicePointName(null);
					agroTransactAgent.setFarmerId(distribution.getAgroTransaction().getFarmerId());
					agroTransactAgent.setFarmerName(distribution.getAgroTransaction().getFarmerName());
					agroTransactAgent.setVendorId(null);
					agroTransactAgent.setVendorName(null);
					agroTransactAgent.setProfType(null);
					agroTransactAgent.setOperType(1);
					agroTransactAgent.setTxnTime(distribution.getAgroTransaction().getTxnTime());
					agroTransactAgent.setTxnDesc("FARMER PRODUCT DISTRIBUTION : CASH PAYMENT");
					agroTransactAgent.setModeOfPayment(ESEAccount.PAYMENT_MODE_CASH);
					agroTransactAgent.setIntBalance(agentAccount.getCashBalance());
					agroTransactAgent.setTxnAmount(trxnAmount);
					agroTransactAgent.setBalAmount(getCashBalance(agentAccount.getCashBalance(), trxnAmount, true));
					agroTransactAgent.setAccount(agentAccount);
					agroTransactAgent.setTxnType(Distribution.PRODUCT_DISTRIBUTION_TO_FIELDSTAFF);
					String approved = productDistributionDAO.findPrefernceByName(ESESystem.ENABLE_APPROVED);
					if (approved.equalsIgnoreCase("1")) {
						// agentAccount.setCashBalance(agroTransactAgent.getBalAmount());
					} else {
						agentAccount.setCashBalance(agroTransactAgent.getBalAmount());
					}
					agroTransactionList.add(agroTransactAgent);

				}
			}
			if (distribution.getAgroTransaction().getFarmerId() != null) {
				Farmer farmer = farmerDAO.findFarmerByFarmerId(distribution.getAgroTransaction().getFarmerId());
				Season season = findCurrentSeason();
				if (!ObjectUtil.isEmpty(farmer) && !ObjectUtil.isEmpty(season)) {
					ESEAccount farmerAccount = farmerDAO.findAccountBySeassonProcurmentProductFarmer(season.getId(), 0L,
							farmer.getId());
					if (!ObjectUtil.isEmpty(farmerAccount)) {

					}
					AgroTransaction agroTransactFarmer = new AgroTransaction();
					agroTransactFarmer.setReceiptNo(distribution.getAgroTransaction().getReceiptNo());
					agroTransactFarmer.setAgentId(null);
					agroTransactFarmer.setAgentName(null);
					agroTransactFarmer.setDeviceId("N/A");
					agroTransactFarmer.setDeviceName("N/A");
					agroTransactFarmer.setServicePointId(null);
					agroTransactFarmer.setServicePointName(null);
					agroTransactFarmer.setFarmerId(distribution.getAgroTransaction().getFarmerId());
					agroTransactFarmer.setFarmerName(distribution.getAgroTransaction().getFarmerName());
					agroTransactFarmer.setVendorId(null);
					agroTransactFarmer.setVendorName(null);
					agroTransactFarmer.setProfType(null);
					agroTransactFarmer.setOperType(1);
					agroTransactFarmer.setTxnTime(distribution.getAgroTransaction().getTxnTime());
					agroTransactFarmer.setTxnDesc("FARMER PRODUCT DISTRIBUTION : CASH PAYMENT");
					agroTransactFarmer.setModeOfPayment(ESEAccount.PAYMENT_MODE_CASH);
					agroTransactFarmer.setIntBalance(farmerAccount.getCashBalance());
					agroTransactFarmer.setTxnAmount(trxnAmount);
					if (farmerAccount.getCashBalance() > 0.00) {
						agroTransactFarmer
								.setBalAmount(getCashBalance(farmerAccount.getCashBalance(), trxnAmount, false) < 0
										? 0.00 : getCashBalance(farmerAccount.getCashBalance(), trxnAmount, false));
					} else {
						agroTransactFarmer.setBalAmount(farmerAccount.getCashBalance());
					}

					agroTransactFarmer.setAccount(farmerAccount);
					agroTransactFarmer.setTxnType(Distribution.PRODUCT_DISTRIBUTION_TO_FARMER);
					String approved = productDistributionDAO.findPrefernceByName(ESESystem.ENABLE_APPROVED);
					if (approved.equalsIgnoreCase("1")) {
						// farmerAccount.setCashBalance(agroTransactFarmer.getBalAmount());
					} else {
						farmerAccount.setCashBalance(agroTransactFarmer.getBalAmount());
					}

					agroTransactionList.add(agroTransactFarmer);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return agroTransactionList;
	}

	public Distribution findDistributionFarmerByRecNo(String receiptNumber) {

		return productDistributionDAO.findDistributionFarmerByRecNo(receiptNumber);
	}

	/**
	 * List by all vendor names for warehouse payment
	 * 
	 * @return List
	 */

	public List<WarehousePayment> selectVendorList() {
		return this.productDistributionDAO.selectVendorList();

	}

	/**
	 * List by all order No for warehousepayment
	 * 
	 * @return List
	 */

	public List<WarehousePayment> selectOrderNoList() {
		// TODO Auto-generated method stub
		return this.productDistributionDAO.selectOrderNoList();
	}

	/**
	 * Find by vendorId and orderno for warehousepayment
	 * 
	 * @param selected
	 *            vendor Id
	 * @param selected
	 *            OrderNo warehousepayment
	 */

	public WarehousePayment findVendorAndOrderNo(Long selectedVendor, String selectedOrderNo) {
		System.out.println("selectedVendor" + selectedVendor + "selectedOrderNo" + selectedOrderNo);
		return this.productDistributionDAO.findVendorAndOrderNo(selectedVendor, selectedOrderNo);
		// TODO Auto-generated method stub

	}

	/**
	 * Find by warehouse paymentId for warehouse payment details
	 * 
	 * @param payementId
	 */

	public List<WarehousePaymentDetails> listWarehousePaymentDetails(long warehousePaymentId) {
		// TODO Auto-generated method stub
		return this.productDistributionDAO.listWarehousePaymentDetails(warehousePaymentId);
	}

	/**
	 * List by all warehouse names
	 * 
	 * @return
	 */
	public List<Warehouse> listWarehouse() {
		// TODO Auto-generated method stub
		return this.productDistributionDAO.listWarehouse();
	}

	/**
	 * Add WarehouseStockReturnDetails Records
	 */

	public void addStockReturnDeatils(WarehouseStockReturnDetails warehouseStockReturnDetails) {
		// TODO Auto-generated method stub
		this.productDistributionDAO.save(warehouseStockReturnDetails);
	}

	/**
	 * Edit Warehouse Damaged Stock Quantity for Warehouse Payment
	 */

	public void editWarehouseDamagedStock(String selectedOrderNo, String selectedVendor, long updateDamagedStock) {
		// TODO Auto-generated method stub
		this.productDistributionDAO.editWarehouseDamagedStock(selectedOrderNo, selectedVendor, updateDamagedStock);

	}

	/**
	 * Find Warehouse Id and ProductId for Warehouse Payment Deatils
	 * 
	 * @param Warehouseid
	 * @param damagedProducts
	 * @return
	 */
	public WarehousePaymentDetails findWarehousePaymentIdAndProduct(long id, String damagedProducts) {
		// TODO Auto-generated method stub
		return this.productDistributionDAO.findWarehousePaymentIdAndProduct(id, damagedProducts);
	}

	/**
	 * Edit Warehouse Damaged Stock Quantity for Warehouse Payment Details
	 */

	public void editWarehousePaymentDetails(long id, String damagedProducts, long damagedStockQty) {
		// TODO Auto-generated method stub
		this.productDistributionDAO.editWarehousePaymentDetails(id, damagedProducts, damagedStockQty);
	}

	public void editWarehouseDamagedStock(WarehousePayment warehousePayment) {
		this.productDistributionDAO.update(warehousePayment);
		// TODO Auto-generated method stub

	}

	public void editWarehousePaymentDetails(WarehousePaymentDetails warehousePaymentDetails) {
		this.productDistributionDAO.update(warehousePaymentDetails);
		// TODO Auto-generated method stub

	}

	/**
	 * Add Warehouse Stock Returns Records
	 */

	public void addStockReturnsProduct(WarehouseStockReturn warehouseStockReturn) {
		// TODO Auto-generated method stub
		this.productDistributionDAO.save(warehouseStockReturn);
	}

	public List<WarehousePayment> selectOrderNoList(long selectedVendor) {

		return this.productDistributionDAO.selectOrderNoList(selectedVendor);
	}

	public List<WarehousePayment> warehouseByvendorAndOrderNo(long selectedVendor, String selectedOrderNo) {

		return this.productDistributionDAO.warehouseByvendorAndOrderNo(selectedVendor, selectedOrderNo);
	}

	public void processWarehouseStockReturns(WarehouseStockReturn warehouseStockReturn) {
		List<AgroTransaction> agroTransactionList = buildAgroTransactionObject(warehouseStockReturn);
		for (AgroTransaction agroTransaction : agroTransactionList) {
			productDistributionDAO.save(agroTransaction);

			accountDAO.updateCashBal(agroTransaction.getAccount().getId(),
					agroTransaction.getAccount().getCashBalance(), agroTransaction.getAccount().getCreditBalance());

		}
		// productDistributionDAO.save(warehouseStockReturn);
	}

	public List<AgroTransaction> buildAgroTransactionObject(WarehouseStockReturn warehouseStockReturn) {

		List<AgroTransaction> agroTransactionList = new ArrayList<AgroTransaction>();
		double finalAmount = warehouseStockReturn.getTotalAmount();
		double paymentAmount = warehouseStockReturn.getPaymentAmount();
		String cpProfId = "BASIX";
		ESEAccount companyAccount = accountDAO.findAccountByProfileId(cpProfId);
		ESEAccount vendorAccount = accountDAO.findAccountByProfileIdAndProfileType(
				warehouseStockReturn.getVendor().getVendorId(), ESEAccount.VENDOR_ACCOUNT);
		if (warehouseStockReturn.getPaymentMode().equals(ESEAccount.PAYMENT_MODE_CASH)) {

			if (finalAmount == paymentAmount) {
				// agroTransactionMaster =
				// buildCashAgroTransactionObject(warehousePayment,
				// warehousePayment.getPaymentAmount());
				agroTransactionList.addAll(buildCashAgroTransactionObject(warehouseStockReturn,
						warehouseStockReturn.getPaymentAmount(), companyAccount, vendorAccount));
			} else if (warehouseStockReturn.getTotalAmount() > warehouseStockReturn.getPaymentAmount()) {
				// agroTransactionMaster =
				// buildCashAgroTransactionObject(warehousePayment,
				// warehousePayment.getPaymentAmount());
				// agroTransactionChild =
				// buildCreditAgroTransactionObject(warehousePayment,
				// -(warehousePayment.getFinalAmount()-warehousePayment.getPaymentAmount()));
				agroTransactionList.addAll(buildCashAgroTransactionObject(warehouseStockReturn,
						warehouseStockReturn.getPaymentAmount(), companyAccount, vendorAccount));
				agroTransactionList.addAll(buildCreditAgroTransactionObject(warehouseStockReturn,
						-(warehouseStockReturn.getTotalAmount() - warehouseStockReturn.getPaymentAmount()),
						companyAccount, vendorAccount));
			} else if (warehouseStockReturn.getTotalAmount() < warehouseStockReturn.getPaymentAmount()) {
				// agroTransactionMaster =
				// buildCashAgroTransactionObject(warehousePayment,
				// warehousePayment.getPaymentAmount());
				// agroTransactionChild=
				// buildCreditAgroTransactionObject(warehousePayment,
				// (warehousePayment.getPaymentAmount()-warehousePayment.getFinalAmount()));

				agroTransactionList.addAll(buildCashAgroTransactionObject(warehouseStockReturn,
						warehouseStockReturn.getPaymentAmount(), companyAccount, vendorAccount));
				agroTransactionList.addAll(buildCreditAgroTransactionObject(warehouseStockReturn,
						(warehouseStockReturn.getPaymentAmount() - warehouseStockReturn.getTotalAmount()),
						companyAccount, vendorAccount));
			}
		} else if (warehouseStockReturn.getPaymentMode().equals(ESEAccount.PAYMENT_MODE_CREDIT)) {
			// agroTransactionMaster =
			// buildCreditAgroTransactionObject(warehousePayment,
			// warehousePayment.getPaymentAmount());

			if (warehouseStockReturn.getCreditBalance() != null) {
				agroTransactionList.addAll(buildCreditAgroTransactionObject(warehouseStockReturn,
						-(warehouseStockReturn.getCreditBalance()), companyAccount, vendorAccount));
			}
		}

		return agroTransactionList;
	}

	public List<AgroTransaction> buildCashAgroTransactionObject(WarehouseStockReturn warehouseStockReturn,
			double trxnAmount, ESEAccount companyAccount, ESEAccount vendorAccount) {

		List<AgroTransaction> agroTransactionList = new ArrayList<AgroTransaction>();
		try {
			// Company Account Process

			if (ObjectUtil.isEmpty(companyAccount)) {

			}
			AgroTransaction agroTransactionOrg = new AgroTransaction();
			agroTransactionOrg.setReceiptNo(warehouseStockReturn.getReceiptNo());
			agroTransactionOrg.setAgentId("BASIX");
			agroTransactionOrg.setAgentName("BASIX");
			agroTransactionOrg.setDeviceId("N/A");
			agroTransactionOrg.setDeviceName("N/A");
			agroTransactionOrg.setServicePointId(null);
			agroTransactionOrg.setServicePointName(null);
			agroTransactionOrg.setFarmerId(null);
			agroTransactionOrg.setFarmerName(null);
			agroTransactionOrg.setVendorId(warehouseStockReturn.getVendor().getVendorId());
			agroTransactionOrg.setVendorName(warehouseStockReturn.getVendor().getVendorName());
			agroTransactionOrg.setProfType(null);
			agroTransactionOrg.setOperType(1);
			agroTransactionOrg.setTxnTime(warehouseStockReturn.getTrxnDate());
			agroTransactionOrg.setTxnDesc("WAREHOUSE STOCK RETURNS : CASH PAYMENT");
			agroTransactionOrg.setModeOfPayment(ESEAccount.PAYMENT_MODE_CASH);
			agroTransactionOrg.setIntBalance(companyAccount.getCashBalance());
			agroTransactionOrg.setTxnAmount(trxnAmount);
			agroTransactionOrg.setBalAmount(getCashBalance(companyAccount.getCashBalance(), trxnAmount, true));
			companyAccount.setCashBalance(agroTransactionOrg.getBalAmount());
			agroTransactionOrg.setAccount(companyAccount);
			agroTransactionOrg.setTxnType("363ORG");

			// Vendor Account Process

			if (ObjectUtil.isEmpty(vendorAccount)) {

			}
			AgroTransaction agroTransactionVendor = new AgroTransaction();
			agroTransactionVendor.setReceiptNo(warehouseStockReturn.getReceiptNo());
			agroTransactionVendor.setAgentId("BASIX");
			agroTransactionVendor.setAgentName("BASIX");
			agroTransactionVendor.setDeviceId("N/A");
			agroTransactionVendor.setDeviceName("N/A");
			agroTransactionVendor.setServicePointId(null);
			agroTransactionVendor.setServicePointName(null);
			agroTransactionVendor.setFarmerId(null);
			agroTransactionVendor.setFarmerName(null);
			agroTransactionVendor.setVendorId(warehouseStockReturn.getVendor().getVendorId());
			agroTransactionVendor.setVendorName(warehouseStockReturn.getVendor().getVendorName());
			agroTransactionVendor.setProfType(null);
			agroTransactionVendor.setOperType(1);
			agroTransactionVendor.setTxnTime(warehouseStockReturn.getTrxnDate());
			agroTransactionVendor.setTxnDesc("WAREHOUSE STOCK RETURNS: CASH PAYMENT");
			agroTransactionVendor.setModeOfPayment(ESEAccount.PAYMENT_MODE_CASH);
			agroTransactionVendor.setIntBalance(vendorAccount.getCashBalance());
			agroTransactionVendor.setTxnAmount(trxnAmount);
			agroTransactionVendor.setBalAmount(getCashBalance(vendorAccount.getCashBalance(), trxnAmount, false));
			vendorAccount.setCashBalance(agroTransactionVendor.getBalAmount());
			agroTransactionVendor.setAccount(vendorAccount);
			agroTransactionVendor.setTxnType("363V");

			// agroTransactionOrg.setRefAgroTransaction(agroTransactionVendor);

			agroTransactionList.add(agroTransactionOrg);
			agroTransactionList.add(agroTransactionVendor);

			return agroTransactionList;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Build credit agro transaction object.
	 * 
	 * @param warehousePayment
	 * @param trxnAmount
	 * @return the agro transaction
	 */
	public List<AgroTransaction> buildCreditAgroTransactionObject(WarehouseStockReturn warehouseStockReturn,
			double trxnAmount, ESEAccount companyAccount, ESEAccount vendorAccount) {

		List<AgroTransaction> agroTransactionList = new ArrayList<AgroTransaction>();
		try {
			// Company Account Process

			if (ObjectUtil.isEmpty(companyAccount)) {

			}
			AgroTransaction agroTransactionOrg = new AgroTransaction();
			agroTransactionOrg.setReceiptNo(warehouseStockReturn.getReceiptNo());
			agroTransactionOrg.setAgentId("BASIX");
			agroTransactionOrg.setAgentName("BASIX");
			agroTransactionOrg.setDeviceId("N/A");
			agroTransactionOrg.setDeviceName("N/A");
			agroTransactionOrg.setServicePointId(null);
			agroTransactionOrg.setServicePointName(null);
			agroTransactionOrg.setFarmerId(null);
			agroTransactionOrg.setFarmerName(null);
			agroTransactionOrg.setVendorId(warehouseStockReturn.getVendor().getVendorId());
			agroTransactionOrg.setVendorName(warehouseStockReturn.getVendor().getVendorName());
			agroTransactionOrg.setProfType(null);
			agroTransactionOrg.setOperType(1);
			agroTransactionOrg.setTxnTime(warehouseStockReturn.getTrxnDate());
			agroTransactionOrg.setTxnDesc("WAREHOUSE STOCK RETURNS : CREDIT PAYMENT");
			agroTransactionOrg.setModeOfPayment(ESEAccount.PAYMENT_MODE_CREDIT);
			if (!ObjectUtil.isEmpty(companyAccount)) {
				agroTransactionOrg.setIntBalance(companyAccount.getCreditBalance());
			}
			agroTransactionOrg.setTxnAmount(trxnAmount);
			agroTransactionOrg.setBalAmount(getCreditBalance(companyAccount.getCreditBalance(), trxnAmount, false));
			companyAccount.setCreditBalance(agroTransactionOrg.getBalAmount());
			agroTransactionOrg.setAccount(companyAccount);
			agroTransactionOrg.setTxnType("363ORG");

			// Vendor Account Process

			if (ObjectUtil.isEmpty(vendorAccount)) {

			}
			AgroTransaction agroTransactionVendor = new AgroTransaction();
			agroTransactionVendor.setReceiptNo(warehouseStockReturn.getReceiptNo());
			agroTransactionVendor.setAgentId("BASIX");
			agroTransactionVendor.setAgentName("BASIX");
			agroTransactionVendor.setDeviceId("N/A");
			agroTransactionVendor.setDeviceName("N/A");
			agroTransactionVendor.setServicePointId(null);
			agroTransactionVendor.setServicePointName(null);
			agroTransactionVendor.setFarmerId(null);
			agroTransactionVendor.setFarmerName(null);
			agroTransactionVendor.setVendorId(warehouseStockReturn.getVendor().getVendorId());
			agroTransactionVendor.setVendorName(warehouseStockReturn.getVendor().getVendorName());
			agroTransactionVendor.setProfType(null);
			agroTransactionVendor.setOperType(1);
			agroTransactionVendor.setTxnTime(warehouseStockReturn.getTrxnDate());
			agroTransactionVendor.setTxnDesc("WAREHOUSE STOCK RETURNS : CREDIT PAYMENT");
			agroTransactionVendor.setModeOfPayment(ESEAccount.PAYMENT_MODE_CREDIT);
			agroTransactionVendor.setIntBalance(vendorAccount.getCreditBalance());
			agroTransactionVendor.setTxnAmount(trxnAmount);
			agroTransactionVendor.setBalAmount(getCreditBalance(vendorAccount.getCreditBalance(), trxnAmount, true));
			vendorAccount.setCreditBalance(agroTransactionVendor.getBalAmount());
			agroTransactionVendor.setAccount(vendorAccount);
			agroTransactionVendor.setTxnType("363V");

			// agroTransactionOrg.setRefAgroTransaction(agroTransactionVendor);

			agroTransactionList.add(agroTransactionOrg);
			agroTransactionList.add(agroTransactionVendor);

			return agroTransactionList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<WarehousePayment> listAllWarehouse() {

		return productDistributionDAO.listAllWarehouse();
	}

	public List<WarehousePayment> loadOrderNobasedOnVendorAndQty(long selectedVendor) {

		return productDistributionDAO.loadOrderNobasedOnVendorAndQty(selectedVendor);
	}

	public WarehousePayment warehousePaymentByVendorAndOrderNo(long selectedVendor, String selectedOrderNo) {

		return productDistributionDAO.warehousePaymentByVendorAndOrderNo(selectedVendor, selectedOrderNo);
	}

	public WarehousePaymentDetails findWarehousePaymentDetail(long id, String damagedProducts) {

		return productDistributionDAO.findWarehousePaymentDetail(id, damagedProducts);
	}

	public void editWarehouseStock(WarehousePayment warehousePayment) {

		this.productDistributionDAO.update(warehousePayment);

	}

	public void editWarehousePaymentDetailsStock(WarehousePaymentDetails warehousePaymentDetails) {

		this.productDistributionDAO.update(warehousePaymentDetails);

	}

	public List<String> listStockReturnType() {
		// TODO Auto-generated method stub
		return this.productDistributionDAO.listStockReturnType();
	}

	public List<String> listOfOrderNo() {
		// TODO Auto-generated method stub

		return this.productDistributionDAO.listOfOrderNo();
	}

	public List<WarehouseProduct> listwarehouseProductByWarehouseId(long selectedWarehouseId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listwarehouseProductByWarehouseId(selectedWarehouseId);
	}

	public WarehousePaymentDetails WarehousePaymentDetailsByWarehouseProductIds(long productId,
			long warehousePaymentId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.WarehousePaymentDetailsByWarehouseProductIds(productId, warehousePaymentId);
	}

	public boolean isOrderNoExists(String orderNo) {

		WarehousePayment warehousePayment = productDistributionDAO.isOrderNoExists(orderNo);
		boolean flag = false;
		if (!ObjectUtil.isEmpty(warehousePayment))
			flag = true;

		return flag;
	}

	public List<HarvestSeason> listHarvestSeason() {

		return productDistributionDAO.listHarvestSeason();
	}

	public void saveCropSupplyAndCropSupplyDetails(CropSupply cropSupply) {
		productDistributionDAO.save(cropSupply);
	}

	public List<FarmCatalogue> listCatalogueByRevisionNo(Long revNo) {

		return productDistributionDAO.listCatalogueByRevisionNo(revNo);
	}

	public byte[] findLogoCultivateImageById(Long id) {

		return productDistributionDAO.findLogoCultivateImageById(id);
	}

	public List<HarvestSeason> listHarvestSeasonByRevisionNo(Long revNo) {

		return productDistributionDAO.listHarvestSeasonByRevisionNo(revNo);
	}

	public List<Customer> listCustomerByRevisionNo(Long revNo) {

		return productDistributionDAO.listCustomerByRevisionNo(revNo);
	}

	@Override
	public Object findTotalYieldPriceByHarvestId(long id) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findTotalYieldPriceByHarvestId(id);
	}

	@Override
	public WarehouseProduct findAgentAvailableStock(long agentId, long prodId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findAgentAvailableStock(agentId, prodId);
	}

	public Boolean isProductDistributionExist(Long id) {
		return productDistributionDAO.isProductDistributionExist(id);
	}

	public void remove(Object object) {
		productDistributionDAO.delete(object);
	}

	@Override
	public List<CropHarvestDetails> listCropHarvestDetailsByProcurementProductId(Long id) {
		return productDistributionDAO.listCropHarvestDetailsByProcurementProductId(id);
	}

	@Override
	public List<CropSupplyDetails> listCropSaleDetailsByProcurementProductId(Long id) {
		return productDistributionDAO.listCropSaleDetailsByProcurementProductId(id);
	}

	@Override
	public List<CropHarvestDetails> listCropHarvestDetailsByProcurementVarietyId(Long id) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listCropHarvestDetailsByProcurementVarietyId(id);
	}

	@Override
	public List<CropSupplyDetails> listCropSaleDetailsByProcurementVarietyId(Long id) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listCropSaleDetailsByProcurementVarietyId(id);
	}

	@Override
	public List<CropHarvestDetails> listCropHarvestDetailsByProcurementGradeId(Long id) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listCropHarvestDetailsByProcurementGradeId(id);
	}

	@Override
	public List<CropSupplyDetails> listCropSaleDetailsByProcurementGradeId(Long id) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listCropSaleDetailsByProcurementGradeId(id);
	}

	@Override
	public List<DistributionDetail> listDistributionDetailBySubCategory(Long id) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listDistributionDetailBySubCategory(id);
	}

	@Override
	public void save(Object obj) {
		productDistributionDAO.save(obj);
	}

	public List<MasterData> listMasterDataByRevisionNo(Long revNo) {

		return productDistributionDAO.listMasterDataByRevisionNo(revNo);
	}

	public List<MasterData> listMasterDataByMasterTypeAndRevisionNo(Long masterType, Long revNo) {
		return productDistributionDAO.listMasterDataByMasterTypeAndRevisionNo(masterType, revNo);
	}

	@Override
	public List<MasterData> listMandiTraderSupplier() {
		// TODO Auto-generated method stub
		return productDistributionDAO.listMandiTraderSupplier();
	}

	@Override
	public List<MasterData> listMandiAggregatorSupplier() {
		// TODO Auto-generated method stub
		return productDistributionDAO.listMandiAggregatorSupplier();
	}

	@Override
	public List<MasterData> listFarmAggregator() {
		// TODO Auto-generated method stub
		return productDistributionDAO.listFarmAggregator();
	}

	@Override
	public List<MasterData> listSupplierFpo() {
		// TODO Auto-generated method stub
		return productDistributionDAO.listSupplierFpo();
	}

	@Override
	public List<MasterData> listSupplierCig() {
		// TODO Auto-generated method stub
		return productDistributionDAO.listSupplierCig();
	}

	@Override
	public List<MasterData> listSupplierFig() {
		// TODO Auto-generated method stub
		return productDistributionDAO.listSupplierFig();
	}

	@Override
	public List<MasterData> listSupplierProducerImporter() {
		// TODO Auto-generated method stub
		return productDistributionDAO.listSupplierProducerImporter();
	}

	public Season findCurrentSeason(String tenantId) {

		ESESystem preference = systemDAO.findPrefernceById(ESESystem.SYSTEM_SWITCH, tenantId);
		String currentSeasonCode = preference.getPreferences().get(ESESystem.CURRENT_SEASON_CODE);
		if (!StringUtil.isEmpty(currentSeasonCode)) {
			Season currentSeason = productDistributionDAO.findSeasonBySeasonCode(currentSeasonCode, tenantId);
			return currentSeason;
		}
		return null;
	}

	public ProcurementGrade findProcurementGradeByCode(String productCode, String tenantId) {

		// TODO Auto-generated method stub
		return productDistributionDAO.findProcurementGradeByCode(productCode, tenantId);
	}

	public void addProcurement(Procurement procurement, String tenantId) {
		ESESystem eseSystem = systemDAO.findPrefernceById("1");
		
		// processAgentTransaction(procurement.getAgroTransaction());
		// productDistributionDAO.save(procurement.getAgroTransaction().getRefAgroTransaction());
		

		Double bal;
		ESEAccount farmerAccount = accountDAO.findAccountByProfileId(procurement.getAgroTransaction().getFarmerId(),
				tenantId);
		if (eseSystem.getPreferences().get(ESESystem.ENABLE_LOAN_MODULE).equals("1")) {
			LoanLedger loanLedgerPro = new LoanLedger();
			loanLedgerPro.setTxnTime(procurement.getAgroTransaction().getTxnTime());
			loanLedgerPro.setFarmerId(!ObjectUtil.isEmpty(procurement.getFarmer()) ? procurement.getFarmer().getFarmerId() : "");
			loanLedgerPro.setActualAmount(procurement.getTotalProVal()); 
			loanLedgerPro.setLoanInterest(procurement.getLoanInterest());
			loanLedgerPro.setFinalPayAmt(procurement.getFinalPayAmt());
			loanLedgerPro.setLoanDesc(procurement.PROCURMEMENT);
			loanLedgerPro.setTxnType(procurement.TXN_TYPE);
			loanLedgerPro.setAccountNo(ObjectUtil.isEmpty(farmerAccount)?"":farmerAccount.getLoanAccountNo());
			loanLedgerPro.setPreFarmerBal(ObjectUtil.isEmpty(farmerAccount)?0.00:farmerAccount.getOutstandingLoanAmount());
			loanLedgerPro.setNewFarmerBal(ObjectUtil.isEmpty(farmerAccount)?0.00:farmerAccount.getLoanAmount());		
			loanLedgerPro.setBranchId(procurement.getBranchId());
			loanLedgerPro.setAccount(!ObjectUtil.isEmpty(farmerAccount) ? farmerAccount : null);
			loanLedgerPro.setReceiptNo(!ObjectUtil.isEmpty(procurement.getAgroTransaction()) ? procurement.getAgroTransaction().getReceiptNo() : "");
			ledgerService.save(loanLedgerPro);
		}
		
		// productDistributionDAO.save(procurement.getAgroTransaction());
		
		
		AgroTransaction existingAgroTxn = procurement.getAgroTransaction();
		processTransaction(procurement, tenantId);
		// productDistributionDAO.save(procurement.getAgroTransaction());
		procurement.setAgroTransaction(existingAgroTxn);
		procurement.getAgroTransaction().setTxnDesc(Procurement.PROCUREMENT_AMOUNT);
		
		
		if (!ObjectUtil.isEmpty(farmerAccount)) {
			bal = farmerAccount.getCashBalance();
			procurement.getAgroTransaction().setBalAmount(bal - procurement.getPaymentAmount());
			procurement.getAgroTransaction().setIntBalance(bal);
			procurement.getAgroTransaction().setTxnAmount(procurement.getPaymentAmount());
			procurement.getAgroTransaction().setAccount(farmerAccount);
			farmerAccount.setCashBalance(bal - procurement.getPaymentAmount());
			
			if (eseSystem.getPreferences().get(ESESystem.ENABLE_LOAN_MODULE).equals("1")) {
			
			procurement.getAgroTransaction().setTxnDesc(Procurement.REPAYMENT_AMOUNT);
			procurement.getAgroTransaction().setIntBalance(bal + procurement.getTotalProVal());
			procurement.getAgroTransaction().setTxnAmount(procurement.getLoanAmount() + procurement.getPaymentAmount());
			procurement.getAgroTransaction().setBalAmount(bal + procurement.getFinalPayAmt() - procurement.getPaymentAmount());
			
			
			procurement.getAgroTransaction().setAccount(farmerAccount);
			procurement.getAgroTransaction().setCreditAmt(procurement.getFinalPayAmt() - procurement.getPaymentAmount());
			procurement.getAgroTransaction().setProcurement(procurement);
			procurement.getAgroTransaction().setIntialBalance(bal);
			procurement.getAgroTransaction().setSeasonCode(procurement.getSeasonCode());
			farmerAccount.setCashBalance(bal + procurement.getFinalPayAmt() - procurement.getPaymentAmount());

			if(farmerAccount.getOutstandingLoanAmount()>=procurement.getLoanAmount()){
				farmerAccount.setOutstandingLoanAmount(farmerAccount.getOutstandingLoanAmount() - procurement.getLoanAmount());
			}
			
			}
			productDistributionDAO.updateESEAccount(farmerAccount, tenantId);
		}
		productDistributionDAO.saveProcurement(procurement, tenantId);
		PaymentLedger ledger = new PaymentLedger();
		ledger.setCreatedDate(procurement.getAgroTransaction().getTxnTime());
		ledger.setCreatedUser(procurement.getAgroTransaction().getAgentName());
		ledger.setType(Procurement.TXN_TYPE);
		ledger.setRefId(String.valueOf(procurement.getId()));
		ledger.setPrevValue(procurement.getAgroTransaction().getIntBalance());
		ledger.setTxnValue(procurement.getAgroTransaction().getTxnAmount());
		ledger.setNewValue(procurement.getAgroTransaction().getBalAmount());
		ledgerService.save(ledger);
		
		if (eseSystem.getPreferences().get(ESESystem.ENABLE_LOAN_MODULE).equals("1")) {
			LoanLedger loanLedger = new LoanLedger();
			loanLedger.setTxnTime(procurement.getAgroTransaction().getTxnTime());
			loanLedger.setFarmerId(!ObjectUtil.isEmpty(procurement.getFarmer()) ? procurement.getFarmer().getFarmerId() : "");
			loanLedger.setActualAmount(procurement.getLoanAmount());
			loanLedger.setLoanInterest(procurement.getLoanInterest());
			loanLedger.setFinalPayAmt(procurement.getActualAmount());
			loanLedger.setLoanDesc(procurement.REPAYMENT_AMOUNT);
			loanLedger.setTxnType(procurement.REPAYMENT_TXN_TYPE);
			loanLedger.setAccountNo(ObjectUtil.isEmpty(farmerAccount)?"":farmerAccount.getLoanAccountNo());
			loanLedger.setPreFarmerBal(ObjectUtil.isEmpty(farmerAccount)?0.00:farmerAccount.getOutstandingLoanAmount());
			loanLedger.setNewFarmerBal(ObjectUtil.isEmpty(farmerAccount)?0.00:farmerAccount.getLoanAmount());		
			loanLedger.setBranchId(procurement.getBranchId());
			loanLedger.setAccount(!ObjectUtil.isEmpty(procurement.getAgroTransaction()) && !ObjectUtil.isEmpty(procurement.getAgroTransaction().getAccount()) ? procurement.getAgroTransaction().getAccount() : null);
			loanLedger.setReceiptNo(!ObjectUtil.isEmpty(procurement.getAgroTransaction()) ? procurement.getAgroTransaction().getReceiptNo() : "");
			ledgerService.save(loanLedger);
		}
		processCityWarehouse(procurement, tenantId);
	}

	public void updateOfflineProcurement(OfflineProcurement offlineProcurement, String tenantId) {

		productDistributionDAO.updateOfflineProcurement(offlineProcurement, tenantId);
	}

	public Season findSeasonBySeasonCodeByTenantId(String seasonCode, String tenantId) {

		return productDistributionDAO.findSeasonBySeasonCodeByTenantId(seasonCode, tenantId);
	}

	public WarehouseProduct findFieldStaffAvailableStockByTenantId(String agentId, long productId, String tenantId) {

		return productDistributionDAO.findFieldStaffAvailableStockByTenantId(agentId, productId, tenantId);
	}

	public void saveDistributionAndDistributionDetail(Distribution distribution, String tenantId) {

		/** SAVING DISTRIBUTION OBJECT **/
		if (!Distribution.PRODUCT_DISTRIBUTION_TO_FARMER
				.equalsIgnoreCase(distribution.getAgroTransaction().getTxnType())) {
			if (ObjectUtil.isEmpty(distribution.getAgroTransaction().getRefAgroTransaction()))
				distribution.getAgroTransaction()
						.setRefAgroTransaction(processAgroTransactionTenant(distribution.getAgroTransaction(),tenantId));
			distribution.getAgroTransaction().getRefAgroTransaction().setSeasonCode(distribution.getSeasonCode());
			distribution.getAgroTransaction().setSeasonCode(distribution.getSeasonCode());
			productDistributionDAO.saveAgroTransaction(distribution.getAgroTransaction().getRefAgroTransaction(),
					tenantId);
			productDistributionDAO.saveAgroTransaction(distribution.getAgroTransaction(), tenantId);
			// accountDAO.updateCashBal(distribution.getAgroTransaction().getAccount().getId(),distribution.getAgroTransaction().getAccount().getCashBalance(),distribution.getAgroTransaction().getAccount().getCreditBalance());
		} else {
			List<AgroTransaction> agroDistList = buildDistributionAgroTransactionObject(distribution, tenantId);
			// List<AgroTransaction> agroDistList =
			// buildAgroTransactionDistributionObject(distribution, tenantId);

			for (AgroTransaction agro : agroDistList) {
				agro.setSeasonCode(distribution.getSeasonCode());
				productDistributionDAO.saveAgroTransaction(agro, tenantId);
			}
			// distribution.setAgroTransaction(null);
		}
		productDistributionDAO.saveDistribution(distribution, tenantId);
		processWarehouseProducts(distribution, tenantId);
	}

	public void processWarehouseProducts(Object object, String tenantId) {
		DecimalFormat df = new DecimalFormat("0.00");
		int i = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		WarehouseProduct warehouseProduct;
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof DMT) {
				DMT dmt = (DMT) object;
				if (dmt.getStatus() == DMT.Status.MTNT.ordinal()) { // Distribution
					// MTNT
					map.put(DATE, dmt.getMtntDate());
					map.put(RECEIPT_NO, dmt.getMtntReceiptNo());
					map.put(DESC, "DISTRIBUTION MTNT");
					for (DMTDetail dmtDetail : dmt.getDmtDetails()) {
						warehouseProduct = productDistributionDAO.findAvailableStock(dmt.getSenderWarehouse().getId(),
								dmtDetail.getProduct().getId(), tenantId);
						map.put(QTY, dmtDetail.getTransferedQty());
						updateWarehouseProducts(warehouseProduct, map, false, tenantId);
					}
				} else if (dmt.getStatus() == DMT.Status.MTNR.ordinal()) { // Distribution
					// MTNR
					map.put(DATE, dmt.getMtnrDate());
					map.put(RECEIPT_NO, dmt.getMtnrReceiptNo());
					map.put(DESC, "DISTRIBUTION MTNR");
					for (DMTDetail dmtDetail : dmt.getDmtDetails()) {
						warehouseProduct = productDistributionDAO.findAvailableStock(dmt.getReceiverWarehouse().getId(),
								dmtDetail.getProduct().getId(), tenantId);
						if (ObjectUtil.isEmpty(warehouseProduct)) {
							warehouseProduct = new WarehouseProduct();
							warehouseProduct.setWarehouse(dmt.getReceiverWarehouse());
							warehouseProduct.setProduct(dmtDetail.getProduct());
						}
						map.put(QTY, dmtDetail.getReceivedQty());
						updateWarehouseProducts(warehouseProduct, map, true, tenantId);
					}
				}
			} // End of DMT
			if (object instanceof Distribution) { // Distribution
				Distribution distribution = (Distribution) object;
				String agentId = distribution.getAgentId();
				String txnType = distribution.getTxnType();
				map.put(DATE, distribution.getTxnTime());
				map.put(RECEIPT_NO, distribution.getReceiptNumber());
				map.put(SEASON_CODE, distribution.getSeasonCode());
				// Product Distribution to Farmer
				if (Distribution.PRODUCT_DISTRIBUTION_TO_FARMER.equals(txnType)||Distribution.PRODUCT_DISTRIBUTION_FARMER_BALANCE.equals(txnType) ) {
					map.put(DESC, Distribution.PRODUCT_DISTRIBUTION_TO_FARMER_DESCRIPTION);
					for (DistributionDetail distributionDetail : distribution.getDistributionDetails()) {

						if (distribution.getProductStock()
								.equalsIgnoreCase(WarehouseProduct.StockType.WAREHOUSE_STOCK.name())) {

							/*
							 * warehouseProduct =
							 * productDistributionDAO.findAvailableStocks(
							 * distribution.getServicePointId(),
							 * distributionDetail.getProduct().getId(),
							 * tenantId);
							 */

							warehouseProduct = productDistributionDAO.findAvailableStocksBySeasonAndBatch(
									distribution.getServicePointId(), distributionDetail.getProduct().getId(),
									distribution.getSeasonCode(), distributionDetail.getBatchNo(), tenantId);

						} else {

							warehouseProduct = productDistributionDAO.findFieldStaffAvailableStockBySeasonAndBatch(
									agentId, distributionDetail.getProduct().getId(), distribution.getSeasonCode(),
									distributionDetail.getBatchNo(), tenantId,distribution.getBranchId());

						}
						warehouseProduct.setBranchId(distribution.getBranchId());
						warehouseProduct.setSeasonCode(distribution.getSeasonCode());
						map.put(QTY, distributionDetail.getQuantity());

						String approved = productDistributionDAO.findPrefernceByName(ESESystem.ENABLE_APPROVED,
								tenantId);

						if (Distribution.PRODUCT_DISTRIBUTION_TO_FIELDSTAFF
								.equalsIgnoreCase(distribution.getTxnType())) {
							if (approved.equalsIgnoreCase("0")) {
								updateWarehouseProducts(warehouseProduct, map, false, tenantId);
							}
						} else if (distribution.getFreeDistribution().equalsIgnoreCase("0")) {
							if (approved.equalsIgnoreCase("0")) {
								updateWarehouseProducts(warehouseProduct, map, false, tenantId);
							}
						} else {
							updateWarehouseProducts(warehouseProduct, map, false, tenantId);
						}
					}

					PaymentLedger paymentLedger = new PaymentLedger();
					paymentLedger.setCreatedDate(distribution.getTxnTime());
					paymentLedger.setCreatedUser(distribution.getAgentId() + "-" + distribution.getAgentName());
					paymentLedger.setType(Distribution.PRODUCT_DISTRIBUTION_TO_FARMER);
					paymentLedger.setPrevValue(distribution.getIntBalance());
					paymentLedger.setTxnValue(distribution.getTxnAmount());
					paymentLedger.setNewValue(distribution.getBalAmount());

					productDistributionDAO.saveByTenantId(paymentLedger, tenantId);
				}
				// Product Return from Farmer
				if (Distribution.PRODUCT_RETURN_FROM_FARMER.equals(txnType)) {
					map.put(DESC, Distribution.PRODUCT_RETURN_FROM_FARMER_DESCRIPTION);
					for (DistributionDetail distributionDetail : distribution.getDistributionDetails()) {
						processFieldStaffWarehouseProduct(agentId, distributionDetail, null, map, tenantId);
					}
				}
				// Product Distribution to Field Staff
				if (Distribution.PRODUCT_DISTRIBUTION_TO_FIELDSTAFF.equals(txnType)) {
					map.put(DESC, Distribution.PRODUCT_DISTRIBUTION_TO_FIELDSTAFF_DESCRIPTION);

					for (DistributionDetail distributionDetail : distribution.getDistributionDetails()) {

						map.put(QTY, distributionDetail.getQuantity());
						// Deduct WH stock
						// WarehouseProduct warehouseStock =
						// productDistributionDAO.findCooperativeAvailableStockByCooperativeManager(agentId,
						// distributionDetail.getProduct().getId());
						// distributionDetail.setExistingQuantity(Double.valueOf(warehouseProduct.getStock()));
						Warehouse warehouse = locationDAO
								.findCoOperativeByCode(distribution.getAgroTransaction().getServicePointId(), tenantId);
						WarehouseProduct warehouseStock = productDistributionDAO
								.findCooperativeAvailableStockByCooperative(warehouse.getId(),
										distributionDetail.getProduct().getId(), tenantId);
						String costPrice[] = distributionDetail.getCostPriceArray().split(",");
						if (costPrice[0].length() > 1) {

							map.put(COSTPRICE, costPrice[i]);
						}
						i++;
						// distributionDetail.setExistingQuantity(warehouseProd)
						warehouseStock.setBranchId(distribution.getBranchId());
						warehouseStock.setSeasonCode(distribution.getSeasonCode());
						updateWarehouseProducts(warehouseStock, map, false, tenantId);

						// Add FS stock
						processFieldStaffWarehouseProduct(
								distribution.getAgroTransaction().getRefAgroTransaction().getAgentId(),
								distributionDetail, warehouseStock.getWarehouse(), map, tenantId);
					}
				}
				// Product Return from Field Staff
				if (Distribution.PRODUCT_RETURN_FROM_FIELDSTAFF.equals(txnType)) {
					if (ObjectUtil.isEmpty(map.get(DATE)))
						map.put(DATE, new Date());
					map.put(DESC, Distribution.PRODUCT_RETURN_FROM_FIELDSTAFF_DESCRIPTION);
					for (DistributionDetail distributionDetail : distribution.getDistributionDetails()) {
						// Deduct FS stock
						WarehouseProduct fieldStaffStock = productDistributionDAO.findAgentAvailableStock(agentId,
								distributionDetail.getProduct().getId(), tenantId);
						map.put(QTY, distributionDetail.getQuantity());

						// distributionDetail.set
						updateWarehouseProducts(fieldStaffStock, map, false, tenantId);
						// Add WH stock
						WarehouseProduct warehouseStock = productDistributionDAO.findAvailableStock(
								fieldStaffStock.getWarehouse().getId(), distributionDetail.getProduct().getId(),
								tenantId);
						if (ObjectUtil.isEmpty(warehouseStock)) {
							warehouseStock = new WarehouseProduct();
							warehouseStock.setWarehouse(fieldStaffStock.getWarehouse());
							warehouseStock.setProduct(distributionDetail.getProduct());
						}
						updateWarehouseProducts(warehouseStock, map, true, tenantId);
					}
				}

			} // End of Distribution
			if (object instanceof WarehouseProduct) { // WarehouseProduct
				warehouseProduct = (WarehouseProduct) object;
				map.put(DATE, new Date());
				map.put(RECEIPT_NO, !StringUtil.isEmpty(warehouseProduct.getReceiptNumber())
						? warehouseProduct.getReceiptNumber() : "");
				map.put(QTY, warehouseProduct.getTxnQty());
				if (warehouseProduct.isEdit()) {
					map.put(DESC, "STOCK ADDED");
					updateWarehouseProducts(warehouseProduct, map, true, tenantId);
				} else {
					map.put(DESC, "STOCK DEDUCTED");
					updateWarehouseProducts(warehouseProduct, map, false, tenantId);
				}
			} // End of WarehouseProduct
		}
	}

	private void updateWarehouseProducts(WarehouseProduct warehouseProduct, Map<String, Object> map, boolean flag,
			String tenantId) {
		DecimalFormat df = new DecimalFormat("0.00");

		if (!ObjectUtil.isEmpty(warehouseProduct)) {
			WarehouseProductDetail warehouseProductDetail = new WarehouseProductDetail();
			warehouseProductDetail.setDate((Date) map.get(DATE));
			warehouseProductDetail.setWarehouseProduct(warehouseProduct);
			warehouseProductDetail.setDesc((String) map.get(DESC));
			warehouseProductDetail.setPrevStock(Double.valueOf(df.format(warehouseProduct.getStock())));
			warehouseProductDetail.setTxnStock((Double) map.get(QTY));
			warehouseProductDetail.setOrderNo(
					!StringUtil.isEmpty(warehouseProduct.getOrderNo()) ? warehouseProduct.getOrderNo() : "");
			warehouseProductDetail.setUserName(
					!StringUtil.isEmpty(warehouseProduct.getUserName()) ? warehouseProduct.getUserName() : "");
			warehouseProductDetail.setVendorId(
					!StringUtil.isEmpty(warehouseProduct.getVendorId()) ? warehouseProduct.getVendorId() : "");
			warehouseProductDetail.setDamagedQty(Double.valueOf(warehouseProduct.getDamagedQty()));
			warehouseProductDetail.setCostPrice(Double.valueOf(warehouseProduct.getCostPrice()));

			if (flag) {
				warehouseProductDetail
						.setFinalStock(warehouseProductDetail.getPrevStock() + warehouseProductDetail.getTxnStock());
			} else {

				double finalstock = warehouseProductDetail.getPrevStock() - warehouseProductDetail.getTxnStock();
				warehouseProductDetail.setFinalStock(Double.valueOf(df.format(finalstock)));

			}
			warehouseProductDetail.setReceiptNo((String) map.get(RECEIPT_NO));
			/** SAVING WAREHOUSE PRODUCT DETAIL **/
			productDistributionDAO.save(warehouseProductDetail, tenantId);
			/** UPDATING WAREHOUSE PRODUCT **/

			if (map.get(DESC).equals(Distribution.PRODUCT_DISTRIBUTION_TO_FIELDSTAFF_DESCRIPTION)) {
				warehouseProduct.setCostPrice(Double.valueOf(map.get(COSTPRICE).toString()));
				warehouseProduct.setSeasonCode(map.get(SEASON_CODE).toString());
			}
			warehouseProduct.setStock(warehouseProductDetail.getFinalStock());
			warehouseProduct.setRevisionNo(DateUtil.getRevisionNumber());
			productDistributionDAO.saveOrUpdate(warehouseProduct, tenantId);
		}

	}

	private void processFieldStaffWarehouseProduct(String agentId, DistributionDetail distributionDetail,
			Warehouse warehouse, Map<String, Object> map, String tenantId) {

		WarehouseProduct fieldStaffStock = productDistributionDAO.findFieldStaffAvailableStock(agentId,
				distributionDetail.getProduct().getId(), tenantId);
		if (ObjectUtil.isEmpty(fieldStaffStock)) {
			fieldStaffStock = new WarehouseProduct();
			Agent agent = agentDAO.findAgentByProfileId(agentId, tenantId);
			/*
			 * if (ObjectUtil.isEmpty(warehouse)) { warehouse =
			 * agent.getCooperative(); }
			 * fieldStaffStock.setWarehouse(warehouse);
			 */
			fieldStaffStock.setProduct(distributionDetail.getProduct());
			fieldStaffStock.setBranchId(warehouse.getBranchId());
			if (!ObjectUtil.isEmpty(agent))
				fieldStaffStock.setAgent(agent);
		}
		map.put(QTY, distributionDetail.getQuantity());
		updateWarehouseProducts(fieldStaffStock, map, true, tenantId);
	}

	public WarehouseProduct findAvailableStock(long warehouseId, long productId, String tenantId) {

		return productDistributionDAO.findAvailableStock(warehouseId, productId, tenantId);
	}

	@Override
	public void saveDistribution(Distribution distribution, String tenantId) {

		productDistributionDAO.saveDistribution(distribution, tenantId);

	}

	@Override
	public void saveOrUpdate(Object warehouseProduct, String tenantId) {

		productDistributionDAO.saveOrUpdate(warehouseProduct, tenantId);

	}

	@Override
	public WarehouseProduct findAvailableStocks(String servicePointId, long id, String tenantId) {

		return productDistributionDAO.findAvailableStocks(servicePointId, id, tenantId);
	}

	@Override
	public WarehouseProduct findFieldStaffAvailableStock(String agentId, long id, String tenantId) {

		return productDistributionDAO.findFieldStaffAvailableStock(agentId, id, tenantId);
	}

	@Override
	public WarehouseProduct findCooperativeAvailableStockByCooperative(long agentId, long productId, String tenantId) {

		return productDistributionDAO.findCooperativeAvailableStockByCooperative(agentId, productId, tenantId);
	}

	@Override
	public WarehouseProduct findAgentAvailableStock(String agentId, long id, String tenantId) {

		return productDistributionDAO.findAgentAvailableStock(agentId, id, tenantId);
	}

	@Override
	public void updateOfflineDistribution(OfflineDistribution offlineDistribution, String tenantId) {

		productDistributionDAO.updateOfflineDistribution(offlineDistribution, tenantId);

	}

	@Override
	public void saveAgroTransaction(AgroTransaction agroTransaction, String tenantId) {

		productDistributionDAO.saveAgroTransaction(agroTransaction, tenantId);

	}

	@Override
	public Distribution findDistributionByReceiptNoTxnType(String receiptNo, String txnType) {

		return productDistributionDAO.findDistributionByReceiptNoTxnType(receiptNo, txnType);
	}

	public List<ProcurementProduct> listFarmCrop() {
		return productDistributionDAO.listFarmCrop();
	}

	@Override
	public WarehouseStockReturn findStockReturnByRecNo(String receiptNo) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findStockReturnByRecNo(receiptNo);
	}

	@Override
	public List<OfflineMTNT> listPendingOfflineMTNTAndMTNR(String tenantId) {
		return this.productDistributionDAO.listPendingOfflineMTNTAndMTNR(tenantId);
	}

	@Override
	public MTNT findMTNByReceiptNoTypeAndOperType(String receiptNo, int type, String tenantId) {
		return this.productDistributionDAO.findMTNByReceiptNoTypeAndOperType(receiptNo, type, tenantId);
	}

	@Override
	public void editOfflineMTNT(OfflineMTNT offlineMTNT, String tenantId) {
		this.productDistributionDAO.saveOrUpdate(offlineMTNT, tenantId);

	}

	@Override
	public Season findSeasonBySeasonCode(String seasonCode, String tenantId) {

		return this.productDistributionDAO.findSeasonBySeasonCode(seasonCode, tenantId);
	}

	private Collection<AgroTransaction> buildCreditAgroTransactionDistributionObjectForGreaterAmount1(
			Distribution distribution, double trxnAmount) {
		List<AgroTransaction> agroTransactionList = new ArrayList<AgroTransaction>();
		try {
			// Farmer Account Process
			Farmer farmer = farmerDAO.findFarmerByFarmerId(distribution.getAgroTransaction().getFarmerId());
			Season season = findCurrentSeason();
			if (!ObjectUtil.isEmpty(farmer) && !ObjectUtil.isEmpty(season)) {
				ESEAccount farmerAccount = farmerDAO.findAccountBySeassonProcurmentProductFarmer(season.getId(), 0L,
						farmer.getId());
				if (!ObjectUtil.isEmpty(farmerAccount)) {

				}
				AgroTransaction agroTransactionFarmer = new AgroTransaction();

				agroTransactionFarmer.setReceiptNo(distribution.getAgroTransaction().getReceiptNo());
				agroTransactionFarmer.setAgentId("N/A");
				agroTransactionFarmer.setAgentName("N/A");
				agroTransactionFarmer.setDeviceId("N/A");
				agroTransactionFarmer.setDeviceName("N/A");
				agroTransactionFarmer.setServicePointId(null);
				agroTransactionFarmer.setServicePointName(null);
				agroTransactionFarmer.setFarmerId(distribution.getAgroTransaction().getFarmerId());
				agroTransactionFarmer.setFarmerName(distribution.getAgroTransaction().getFarmerName());
				agroTransactionFarmer.setVendorId(null);
				agroTransactionFarmer.setVendorName(null);
				agroTransactionFarmer.setProfType(null);
				agroTransactionFarmer.setOperType(1);
				agroTransactionFarmer.setTxnTime(distribution.getAgroTransaction().getTxnTime());
				agroTransactionFarmer.setTxnDesc("FARMER PRODUCT DISTRIBUTION : CREDIT PAYMENT");
				agroTransactionFarmer.setModeOfPayment(ESEAccount.PAYMENT_MODE_CREDIT);
				agroTransactionFarmer.setIntBalance(farmerAccount.getCreditBalance());
				agroTransactionFarmer.setTxnAmount(trxnAmount);

				agroTransactionFarmer
						.setBalAmount(getCreditBalance(farmerAccount.getCreditBalance(), (trxnAmount), true));

				agroTransactionFarmer.setAccount(farmerAccount);
				agroTransactionFarmer.setTxnType(Distribution.PRODUCT_DISTRIBUTION_TO_FARMER);

				farmerAccount.setCreditBalance(agroTransactionFarmer.getBalAmount());
				agroTransactionList.add(agroTransactionFarmer);

			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return agroTransactionList;
	}

	public List<Object[]> findpmtdetail(int cooperative, int procurementproduct) {

		return this.productDistributionDAO.findpmtdetail(cooperative, procurementproduct);

	}

	private Collection<AgroTransaction> buildCreditAgroTransactionDistributionObjectForGreaterAmount(
			Distribution distribution, double trxnAmount) {
		List<AgroTransaction> agroTransactionList = new ArrayList<AgroTransaction>();
		try {
			// Farmer Account Process
			Farmer farmer = farmerDAO.findFarmerByFarmerId(distribution.getAgroTransaction().getFarmerId());
			Season season = findCurrentSeason();
			if (!ObjectUtil.isEmpty(farmer) && !ObjectUtil.isEmpty(season)) {
				ESEAccount farmerAccount = farmerDAO.findAccountBySeassonProcurmentProductFarmer(season.getId(), 0L,
						farmer.getId());
				if (!ObjectUtil.isEmpty(farmerAccount)) {

				}
				AgroTransaction agroTransactionFarmer = new AgroTransaction();

				agroTransactionFarmer.setReceiptNo(distribution.getAgroTransaction().getReceiptNo());
				agroTransactionFarmer.setAgentId("N/A");
				agroTransactionFarmer.setAgentName("N/A");
				agroTransactionFarmer.setDeviceId("N/A");
				agroTransactionFarmer.setDeviceName("N/A");
				agroTransactionFarmer.setServicePointId(null);
				agroTransactionFarmer.setServicePointName(null);
				agroTransactionFarmer.setFarmerId(distribution.getAgroTransaction().getFarmerId());
				agroTransactionFarmer.setFarmerName(distribution.getAgroTransaction().getFarmerName());
				agroTransactionFarmer.setVendorId(null);
				agroTransactionFarmer.setVendorName(null);
				agroTransactionFarmer.setProfType(null);
				agroTransactionFarmer.setOperType(1);
				agroTransactionFarmer.setTxnTime(distribution.getAgroTransaction().getTxnTime());
				agroTransactionFarmer.setTxnDesc("FARMER PRODUCT DISTRIBUTION : CREDIT PAYMENT");
				agroTransactionFarmer.setModeOfPayment(ESEAccount.PAYMENT_MODE_CREDIT);
				agroTransactionFarmer.setIntBalance(farmerAccount.getCreditBalance());
				agroTransactionFarmer.setTxnAmount(trxnAmount);

				agroTransactionFarmer
						.setBalAmount(getCreditBalance(farmerAccount.getCreditBalance(), (trxnAmount), true));
				agroTransactionFarmer.setTxnType(Distribution.PRODUCT_DISTRIBUTION_TO_FARMER);
				String approved = productDistributionDAO.findPrefernceByName(ESESystem.ENABLE_APPROVED);
				if (approved.equalsIgnoreCase("1")) {
					// agroTransactionFarmer.setAccount(farmerAccount);
					// farmerAccount.setCreditBalance(agroTransactionFarmer.getBalAmount());
				} else {
					agroTransactionFarmer.setAccount(farmerAccount);
					farmerAccount.setCreditBalance(agroTransactionFarmer.getBalAmount());
				}

				agroTransactionList.add(agroTransactionFarmer);

			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return agroTransactionList;
	}

	@Override
	public List<Object[]> listPMTReceiptNumberByWarehouseI(long warehouseId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listPMTReceiptNumberByWarehouseI(warehouseId);
	}

	@Override
	public List<ProcurementProduct> listProcurementProductByReceiptNo(Long receiptNoId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listProcurementProductByReceiptNo(receiptNoId);
	}

	@Override
	public PMTDetail findpmtdetailById(long productId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findpmtdetailById(productId);
	}

	@Override
	public void update(PMTDetail existingMtntDetails) {
		// TODO Auto-generated method stub
		productDistributionDAO.update(existingMtntDetails);
	}

	@Override
	public List<AgroTransaction> findAgroTxnByReceiptNo(String receiptNo, String tenantId) {
		return productDistributionDAO.findAgroTxnByReceiptNo(receiptNo, tenantId);
	}

	@Override
	public PaymentMode findPaymentModeByCode(String paymentType, String tenantId) {
		return productDistributionDAO.findPaymentModeByCode(paymentType, tenantId);
	}

	@Override
	public void saveAgroTransactionForPayment(AgroTransaction farmerPaymentTxn, AgroTransaction agentPaymentTxn,
			String tenantId) {
		ESEAccount eseAccount = null;

		
		String description = farmerPaymentTxn.getTxnDesc();
		if (!StringUtil.isEmpty(description)) {
			String[] descriptionDetail = description.split("\\|");
			// if (!ObjectUtil.isEmpty(descriptionDetail) &&
			// descriptionDetail.length >= 2) {
			// if (!StringUtil.isEmpty(descriptionDetail[1])) {
			if (PaymentMode.DISTRIBUTION_ADVANCE_PAYMENT_MODE_NAME.equalsIgnoreCase(descriptionDetail[0])
					|| PaymentMode.DISTRIBUTION_PAYMENT_MODE_NAME.equalsIgnoreCase(descriptionDetail[0])) {
				// DISTRIBUTION ADVANCE PAYMENT
				farmerPaymentTxn.calculateBalance(farmerPaymentTxn.getAccount(), farmerPaymentTxn.getTxnAmount(), false,
						true);
				agentPaymentTxn.calculateBalance(agentPaymentTxn.getAccount(), agentPaymentTxn.getTxnAmount(), false,
						true);

				ESESystem eseSystem = systemDAO.findPrefernceById("1");
				if (eseSystem.getPreferences().get(ESESystem.INTEREST_MODULE).equals("1")) {

					InterestCalcConsolidated intCalConsolidated = farmerDAO
							.findInterestCalcConsolidatedByfarmerProfileId(farmerPaymentTxn.getFarmerId(), tenantId);
					double paymentAmt = farmerPaymentTxn.getTxnAmount();
					if (!ObjectUtil.isEmpty(intCalConsolidated) && intCalConsolidated.getAccumulatedIntAmount() > 0) {
						double remainAmt = intCalConsolidated.getAccumulatedIntAmount() - paymentAmt;
						if (remainAmt >= 0) {
							intCalConsolidated.setAccumulatedIntAmount(remainAmt);
							intCalConsolidated.setUpdateUserName(farmerPaymentTxn.getAgentId());
							intCalConsolidated.setLastUpdateDt(new Date());
							intCalConsolidated.setRevisionNo(DateUtil.getRevisionNumber());
							farmerDAO.update(intCalConsolidated);
						} else if (remainAmt < 0) {
							intCalConsolidated.setAccumulatedIntAmount(0);
							intCalConsolidated.setAccumulatedPrincipalAmount(
									intCalConsolidated.getAccumulatedPrincipalAmount() - Math.abs(remainAmt));
							intCalConsolidated.setUpdateUserName(farmerPaymentTxn.getAgentId());
							intCalConsolidated.setLastUpdateDt(new Date());
							intCalConsolidated.setRevisionNo(DateUtil.getRevisionNumber());
							farmerDAO.update(intCalConsolidated);
						}
					} /*
						 * else { intCalConsolidated.
						 * setAccumulatedPrincipalAmount( intCalConsolidated
						 * .getAccumulatedPrincipalAmount() - paymentAmt);
						 * intCalConsolidated.setUpdateUserName(
						 * farmerPaymentTxn.getAgentId());
						 * intCalConsolidated.setLastUpdateDt(new Date());
						 * intCalConsolidated.setRevisionNo(DateUtil.
						 * getRevisionNumber());
						 * farmerDAO.update(intCalConsolidated); }
						 */
				}
			}else if(PaymentMode.LOAN_REPAYMENT_MODE_NAME.equalsIgnoreCase(descriptionDetail[0])){
				farmerPaymentTxn.calculateFarmerLoanBalance(farmerPaymentTxn.getAccount(), farmerPaymentTxn.getTxnAmount());
				productDistributionDAO.save(farmerPaymentTxn);
				LoanLedger loanLedger = new LoanLedger();
				loanLedger.setTxnTime(farmerPaymentTxn.getTxnTime());
				loanLedger.setReceiptNo(farmerPaymentTxn.getReceiptNo());
				loanLedger.setFarmerId(farmerPaymentTxn.getFarmerId());
				loanLedger.setAccount(farmerPaymentTxn.getAccount());
				loanLedger.setAccountNo(farmerPaymentTxn.getAccount().getLoanAccountNo());
				loanLedger.setActualAmount(farmerPaymentTxn.getTxnAmount());
				loanLedger.setNewFarmerBal(farmerPaymentTxn.getAccount().getLoanAmount());
				loanLedger.setPreFarmerBal(farmerPaymentTxn.getBalAmount());
				loanLedger.setTxnType("702");
				loanLedger.setLoanDesc(PaymentMode.LOAN_REPAYMENT_AMOUNT);
				loanLedger.setBranchId(farmerPaymentTxn.getBranchId());
				ledgerService.save(loanLedger);
				if (!StringUtil.isEmpty(farmerPaymentTxn.getBalAmount())) {
					accountDAO.updateESEAccountOutStandingBalById(farmerPaymentTxn.getAccount().getId(),
							farmerPaymentTxn.getBalAmount());
				}else {// OTHER PAYMENTS
				farmerPaymentTxn.calculateBalance(farmerPaymentTxn.getAccount(), farmerPaymentTxn.getTxnAmount(), true,
						false);
				agentPaymentTxn.calculateBalance(agentPaymentTxn.getAccount(), agentPaymentTxn.getTxnAmount(), true,
						false);
			} 
		}else if (PaymentMode.DISTRIBUTION_ADVANCE_PAYMENT_MODE_NAME.equalsIgnoreCase(description)
				|| PaymentMode.DISTRIBUTION_PAYMENT_MODE_NAME.equalsIgnoreCase(description)) {
			// DISTRIBUTION ADVANCE PAYMENT
			farmerPaymentTxn.calculateBalance(farmerPaymentTxn.getAccount(), farmerPaymentTxn.getTxnAmount(), false,
					true);
			agentPaymentTxn.calculateBalance(agentPaymentTxn.getAccount(), agentPaymentTxn.getTxnAmount(), false,
					true);

			ESESystem eseSystem = systemDAO.findPrefernceById("1");
			if (eseSystem.getPreferences().get(ESESystem.INTEREST_MODULE).equals("1")) {

				InterestCalcConsolidated intCalConsolidated = farmerDAO
						.findInterestCalcConsolidatedByfarmerProfileId(farmerPaymentTxn.getFarmerId(), tenantId);
				double paymentAmt = farmerPaymentTxn.getTxnAmount();
				if (!ObjectUtil.isEmpty(intCalConsolidated) && intCalConsolidated.getAccumulatedIntAmount() > 0) {
					double remainAmt = intCalConsolidated.getAccumulatedIntAmount() - paymentAmt;
					if (remainAmt >= 0) {
						intCalConsolidated.setAccumulatedIntAmount(remainAmt);
						intCalConsolidated.setUpdateUserName(farmerPaymentTxn.getAgentId());
						intCalConsolidated.setLastUpdateDt(new Date());
						intCalConsolidated.setRevisionNo(DateUtil.getRevisionNumber());
						farmerDAO.update(intCalConsolidated);
					} else if (remainAmt < 0) {
						intCalConsolidated.setAccumulatedIntAmount(0);
						intCalConsolidated.setAccumulatedPrincipalAmount(
								intCalConsolidated.getAccumulatedPrincipalAmount() - Math.abs(remainAmt));
						intCalConsolidated.setUpdateUserName(farmerPaymentTxn.getAgentId());
						intCalConsolidated.setLastUpdateDt(new Date());
						intCalConsolidated.setRevisionNo(DateUtil.getRevisionNumber());
						farmerDAO.update(intCalConsolidated);
					}
				} /*
					 * else { intCalConsolidated.
					 * setAccumulatedPrincipalAmount( intCalConsolidated
					 * .getAccumulatedPrincipalAmount() - paymentAmt);
					 * intCalConsolidated.setUpdateUserName(
					 * farmerPaymentTxn.getAgentId());
					 * intCalConsolidated.setLastUpdateDt(new Date());
					 * intCalConsolidated.setRevisionNo(DateUtil.
					 * getRevisionNumber());
					 * farmerDAO.update(intCalConsolidated); }
					 */
			}
		}  else {// OTHER PAYMENTS
				farmerPaymentTxn.calculateBalance(farmerPaymentTxn.getAccount(), farmerPaymentTxn.getTxnAmount(), true,
						false);
				agentPaymentTxn.calculateBalance(agentPaymentTxn.getAccount(), agentPaymentTxn.getTxnAmount(), true,
						false);
			}
			if(!PaymentMode.LOAN_REPAYMENT_MODE_NAME.equalsIgnoreCase(descriptionDetail[0])){
				productDistributionDAO.saveByTenantId(farmerPaymentTxn, tenantId);
			productDistributionDAO.saveByTenantId(agentPaymentTxn, tenantId);
			if (!StringUtil.isEmpty(farmerPaymentTxn.getBalAmount())) {
				// accountDAO.updateESEAccountCashBalById(farmerPaymentTxn.getAccount().getId(),farmerPaymentTxn.getBalAmount());
				eseAccount = accountDAO.findAccountByProfileIdAndProfileType(farmerPaymentTxn.getAccount().getProfileId(),
						ESEAccount.CONTRACT_ACCOUNT, tenantId);
				eseAccount.setCashBalance(farmerPaymentTxn.getBalAmount());
				accountDAO.updateByTenant(eseAccount, tenantId);

			}
			if (!StringUtil.isEmpty(agentPaymentTxn.getBalAmount())) {
				/*
				 * accountDAO.updateESEAccountCashBalById(agentPaymentTxn.getAccount
				 * ().getId(), agentPaymentTxn.getBalAmount());
				 */
				eseAccount = accountDAO.findAccountByProfileIdAndProfileType(agentPaymentTxn.getAccount().getProfileId(),
						ESEAccount.AGENT_ACCOUNT, tenantId);
				eseAccount.setCashBalance(agentPaymentTxn.getBalAmount());
				accountDAO.updateByTenant(eseAccount, tenantId);
			}
			
			
			}
		} 

		
	}

	@Override
	public List<OfflinePayment> listPendingPaymentTxn(String tenantId) {
		return productDistributionDAO.listPendingPaymentTxn(tenantId);
	}

	@Override
	public void updateByTenant(Object obj, String tenantId) {
		accountDAO.updateByTenant(obj, tenantId);

	}

	@Override
	public void SaveByTenant(Object obj, String tenantId) {
		accountDAO.SaveByTenant(obj, tenantId);

	}

	@Override
	public void SaveOrUpdateByTenant(Object obj, String tenantId) {
		accountDAO.SaveOrUpdateByTenant(obj, tenantId);
	}

	@Override
	public ProcurementProduct findProcurementProductByName(String name) {
		return productDistributionDAO.findProcurementProductByName(name);
	}

	@Override
	public CityWarehouse findCityWarehouseBySamithi(long samithiId, long productId, String quality) {

		return productDistributionDAO.findCityWarehouseBySamithi(samithiId, productId, quality);
	}

	@Override
	public void addProcurement1(Procurement procurement) {

		// processAgentTransaction(procurement.getAgroTransaction());
		// productDistributionDAO.save(procurement.getAgroTransaction().getRefAgroTransaction());
		// processTransaction1(procurement);
		// productDistributionDAO.save(procurement.getAgroTransaction());
		procurement.getAgroTransaction().setTxnDesc(Procurement.PROCUREMENT_AMOUNT);
		productDistributionDAO.save(procurement);
		PaymentLedger ledger = new PaymentLedger();
		ledger.setCreatedDate(procurement.getAgroTransaction().getTxnTime());
		ledger.setCreatedUser(procurement.getAgroTransaction().getAgentName());
		ledger.setType(Procurement.TXN_TYPE);
		ledger.setRefId(String.valueOf(procurement.getId()));
		ledger.setPrevValue(procurement.getAgroTransaction().getIntBalance());
		ledger.setTxnValue(procurement.getAgroTransaction().getTxnAmount());
		ledger.setNewValue(procurement.getAgroTransaction().getBalAmount());
		ledgerService.save(ledger);
		processCityWarehouse1(procurement);

	}

	@Override
	public List<CityWarehouse> listFarmersByCoOperative(long coOperativeId) {

		return productDistributionDAO.listFarmersByCoOperative(coOperativeId);
	}

	public double findFarmerStockNetWgtByWarehouseIdAndFarmerId(long warehouseId, String farmerId) {
		return productDistributionDAO.findFarmerStockNetWgtByWarehouseIdAndFarmerId(warehouseId, farmerId);
	}

	@Override
	public List<CityWarehouse> listProductsByFarmerIdAndCooperativeId(long farmerId, long coOperativeId) {

		return productDistributionDAO.listProductsByFarmerIdAndCooperativeId(farmerId, coOperativeId);
	}

	@Override
	public List<Procurement> findProcurementByFarmerId(long farmerId) {

		return productDistributionDAO.findProcurementByFarmerId(farmerId);
	}

	@Override
	public List<ProcurementDetail> findProcurementDetailByProcurementId(long procurementId) {

		return productDistributionDAO.findProcurementDetailByProcurementId(procurementId);
	}

	@Override
	public ProcurementGrade findProcurementGradeByProcurementGradeId(Long id) {

		return productDistributionDAO.findProcurementGradeByProcurementGradeId(id);
	}

	@Override
	public List<CityWarehouseDetail> listCityWarehouseDetailsByCityWarehouseId(long cityWarehouseId) {

		return productDistributionDAO.listCityWarehouseDetailsByCityWarehouseId(cityWarehouseId);
	}

	@Override
	public CityWarehouse findCityWarehouseByCoOperativeAndFarmer(long coOperativeId, long productId, long farmerId,
			String quality) {

		return productDistributionDAO.findCityWarehouseByCoOperativeAndFarmer(coOperativeId, productId, farmerId,
				quality);
	}

	@Override
	public CityWarehouse findCityWarehouseByVillageAndFarmer(long villageId, long productId, long farmerId,
			String quality) {

		return productDistributionDAO.findCityWarehouseByVillageAndFarmer(villageId, productId, farmerId, quality);
	}

	@Override
	public Procurement findProcurementById(Long id) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findProcurementById(id);
	}

	public List<AgroTransaction> buildAgroTransactionDistributionObject(Distribution distribution, String tenantId) {

		List<AgroTransaction> agroTransactionList = new ArrayList<AgroTransaction>();
		if ("0".equalsIgnoreCase(distribution.getFreeDistribution())) {
			// AgroTransaction agroTransactionMaster = null;
			// AgroTransaction agroTransactionChild = null;
			double finalAmount = distribution.getFinalAmount();
			double paymentAmount = distribution.getPaymentAmount();

			if (distribution.getPaymentMode().equals(ESEAccount.PAYMENT_MODE_CASH)) {
				if (finalAmount == paymentAmount) {
					agroTransactionList.addAll(buildCashAgroTransactionDistributionObject(distribution,
							distribution.getPaymentAmount(), tenantId));
				} else if (distribution.getFinalAmount() > distribution.getPaymentAmount()) {
					agroTransactionList.addAll(buildCashAgroTransactionDistributionObject(distribution,
							distribution.getPaymentAmount(), tenantId));
					agroTransactionList.addAll(buildCreditAgroTransactionDistributionObject(distribution,
							(distribution.getFinalAmount() - distribution.getPaymentAmount())));
				} else if (distribution.getFinalAmount() < distribution.getPaymentAmount()) {

					agroTransactionList.addAll(buildCashAgroTransactionDistributionObject(distribution,
							distribution.getFinalAmount(), tenantId));
					agroTransactionList.addAll(buildCreditAgroTransactionDistributionObjectForGreaterAmount1(
							distribution, (distribution.getPaymentAmount() - distribution.getFinalAmount()), tenantId));
				}
			} else if (distribution.getPaymentMode().equals(ESEAccount.PAYMENT_MODE_CREDIT)) {
				agroTransactionList.addAll(buildCreditAgroTransactionDistributionObject(distribution,
						(distribution.getPaymentAmount()), tenantId));
			}
		} else if ("1".equalsIgnoreCase(distribution.getFreeDistribution())) {

			AgroTransaction freeAgroTxn = new AgroTransaction();
			freeAgroTxn.setReceiptNo(distribution.getAgroTransaction().getReceiptNo());
			freeAgroTxn.setAgentId(StringUtil.isEmpty(distribution.getAgroTransaction().getAgentId()) ? "N/A"
					: distribution.getAgroTransaction().getAgentId());
			freeAgroTxn.setAgentName(StringUtil.isEmpty(distribution.getAgroTransaction().getAgentName()) ? "N/A"
					: distribution.getAgroTransaction().getAgentName());
			freeAgroTxn.setDeviceId(StringUtil.isEmpty(distribution.getAgroTransaction().getDeviceId()) ? "N/A"
					: distribution.getAgroTransaction().getDeviceId());
			freeAgroTxn.setDeviceName(StringUtil.isEmpty(distribution.getAgroTransaction().getDeviceName()) ? "N/A"
					: distribution.getAgroTransaction().getDeviceName());
			freeAgroTxn.setServicePointId(StringUtil.isEmpty(distribution.getAgroTransaction().getServicePointId())
					? "N/A" : distribution.getAgroTransaction().getServicePointId());
			freeAgroTxn.setServicePointName(StringUtil.isEmpty(distribution.getAgroTransaction().getServicePointName())
					? "N/A" : distribution.getAgroTransaction().getServicePointName());
			freeAgroTxn.setFarmerId(distribution.getAgroTransaction().getFarmerId());
			freeAgroTxn.setFarmerName(distribution.getAgroTransaction().getFarmerName());
			freeAgroTxn.setVendorId(null);
			freeAgroTxn.setVendorName(null);
			freeAgroTxn.setProfType(null);
			freeAgroTxn.setOperType(1);
			freeAgroTxn.setTxnTime(distribution.getAgroTransaction().getTxnTime());
			freeAgroTxn.setTxnDesc("FREE PRODUCT DISTRIBUTION");
			freeAgroTxn.setModeOfPayment("N/A");
			freeAgroTxn.setIntBalance(0.00);
			freeAgroTxn.setTxnAmount(0.00);
			freeAgroTxn.setBalAmount(0.00);
			freeAgroTxn.setAccount(null);
			freeAgroTxn.setTxnType(distribution.getAgroTransaction().getTxnType());
			agroTransactionList.add(freeAgroTxn);

		}
		return agroTransactionList;

	}

	private Collection<AgroTransaction> buildCreditAgroTransactionDistributionObject(Distribution distribution,
			double trxnAmount, String tenantId) {
		List<AgroTransaction> agroTransactionList = new ArrayList<AgroTransaction>();
		try {
			if (distribution.getAgroTransaction().getServicePointId() != null
					&& (!StringUtil.isEmpty(distribution.getAgroTransaction().getServicePointId()))) {
				// Company Account Process
				String cpProfId = "BASIX";
				ESEAccount companyAccount = accountDAO.findAccountByProfileId(cpProfId, tenantId);
				if (ObjectUtil.isEmpty(companyAccount)) {

				}
				AgroTransaction agroTransactionOrg = new AgroTransaction();
				agroTransactionOrg.setReceiptNo(distribution.getAgroTransaction().getReceiptNo());
				agroTransactionOrg.setAgentId("N/A");
				agroTransactionOrg.setAgentName("N/A");
				agroTransactionOrg.setDeviceId("N/A");
				agroTransactionOrg.setDeviceName("N/A");
				agroTransactionOrg.setServicePointId(distribution.getAgroTransaction().getServicePointId());
				agroTransactionOrg.setServicePointName(distribution.getAgroTransaction().getServicePointName());
				agroTransactionOrg.setFarmerId(distribution.getAgroTransaction().getFarmerId());
				agroTransactionOrg.setFarmerName(distribution.getAgroTransaction().getFarmerName());
				agroTransactionOrg.setVendorId(null);
				agroTransactionOrg.setVendorName(null);
				agroTransactionOrg.setProfType(null);
				agroTransactionOrg.setOperType(1);
				agroTransactionOrg.setTxnTime(distribution.getAgroTransaction().getTxnTime());
				agroTransactionOrg.setTxnDesc("PRODUCT DISTRIBUTION : CREDIT PAYMENT");
				agroTransactionOrg.setModeOfPayment(ESEAccount.PAYMENT_MODE_CREDIT);
				agroTransactionOrg.setIntBalance(companyAccount.getCreditBalance());
				agroTransactionOrg.setTxnAmount(trxnAmount);
				// if((distribution.getAgroTransaction().getTxnAmount())<0){
				// agroTransactionOrg.setBalAmount(getCreditBalance(companyAccount.getCreditBalance(),
				// trxnAmount, true));
				// }
				agroTransactionOrg.setBalAmount(getCreditBalance(companyAccount.getCreditBalance(), trxnAmount, false));

				agroTransactionOrg.setTxnType(distribution.getAgroTransaction().getTxnType());

				String approved = productDistributionDAO.findPrefernceByName(ESESystem.ENABLE_APPROVED);
				if (approved.equalsIgnoreCase("1")) {
					// agroTransactionOrg.setAccount(companyAccount);
					// companyAccount.setCreditBalance(agroTransactionOrg.getBalAmount());
				} else {
					agroTransactionOrg.setAccount(companyAccount);
					companyAccount.setCreditBalance(agroTransactionOrg.getBalAmount());
				}

				agroTransactionList.add(agroTransactionOrg);

			}

			// Farmer Account Process
			Farmer farmer = farmerDAO.findFarmerByFarmerId(distribution.getAgroTransaction().getFarmerId(), tenantId);
			Season season = findCurrentSeason();
			if (!ObjectUtil.isEmpty(farmer) && !ObjectUtil.isEmpty(season)) {
				ESEAccount farmerAccount = farmerDAO.findAccountBySeassonProcurmentProductFarmer(season.getId(), 0L,
						farmer.getId(), tenantId);
				if (!ObjectUtil.isEmpty(farmerAccount)) {

				}
				AgroTransaction agroTransactionFarmer = new AgroTransaction();

				agroTransactionFarmer.setReceiptNo(distribution.getAgroTransaction().getReceiptNo());
				agroTransactionFarmer.setAgentId("N/A");
				agroTransactionFarmer.setAgentName("N/A");
				agroTransactionFarmer.setDeviceId("N/A");
				agroTransactionFarmer.setDeviceName("N/A");
				agroTransactionFarmer.setServicePointId(null);
				agroTransactionFarmer.setServicePointName(null);
				agroTransactionFarmer.setFarmerId(distribution.getAgroTransaction().getFarmerId());
				agroTransactionFarmer.setFarmerName(distribution.getAgroTransaction().getFarmerName());
				agroTransactionFarmer.setVendorId(null);
				agroTransactionFarmer.setVendorName(null);
				agroTransactionFarmer.setProfType(null);
				agroTransactionFarmer.setOperType(1);
				agroTransactionFarmer.setTxnTime(distribution.getAgroTransaction().getTxnTime());
				agroTransactionFarmer.setTxnDesc("FARMER PRODUCT DISTRIBUTION : CREDIT PAYMENT");
				agroTransactionFarmer.setModeOfPayment(ESEAccount.PAYMENT_MODE_CREDIT);
				agroTransactionFarmer.setIntBalance(farmerAccount.getCreditBalance());
				agroTransactionFarmer.setTxnAmount(trxnAmount);
				if (farmerAccount.getCreditBalance() >= trxnAmount) {
					agroTransactionFarmer
							.setBalAmount(getCreditBalance(farmerAccount.getCreditBalance(), -(trxnAmount), true));
				} else {
					agroTransactionFarmer
							.setBalAmount(getCreditBalance(farmerAccount.getCreditBalance(), trxnAmount, false));
				}
				// agroTransactionFarmer.setAccount(farmerAccount);
				agroTransactionFarmer.setTxnType(Distribution.PRODUCT_DISTRIBUTION_TO_FARMER);

				// farmerAccount.setCreditBalance(agroTransactionFarmer.getBalAmount());
				agroTransactionList.add(agroTransactionFarmer);

				if (distribution.getAgroTransaction().getAgentId() != null) {

					ESEAccount agentAccount = accountDAO.findAccountByProfileIdAndProfileType(
							distribution.getAgroTransaction().getAgentId(), ESEAccount.AGENT_ACCOUNT, tenantId);
					if (!ObjectUtil.isEmpty(agentAccount)) {
						AgroTransaction agroTransactAgent = new AgroTransaction();
						agroTransactAgent.setReceiptNo(distribution.getAgroTransaction().getReceiptNo());
						agroTransactAgent.setAgentId(distribution.getAgroTransaction().getAgentId());
						agroTransactAgent.setAgentName(distribution.getAgroTransaction().getAgentName());
						agroTransactAgent.setDeviceId(distribution.getAgroTransaction().getDeviceId());
						agroTransactAgent.setDeviceName(distribution.getAgroTransaction().getDeviceName());
						agroTransactAgent.setServicePointId(null);
						agroTransactAgent.setServicePointName(null);
						agroTransactAgent.setFarmerId(distribution.getAgroTransaction().getFarmerId());
						agroTransactAgent.setFarmerName(distribution.getAgroTransaction().getFarmerName());
						agroTransactAgent.setVendorId(null);
						agroTransactAgent.setVendorName(null);
						agroTransactAgent.setProfType(null);
						agroTransactAgent.setOperType(1);
						agroTransactAgent.setTxnTime(distribution.getAgroTransaction().getTxnTime());
						agroTransactAgent.setTxnDesc("FIELDSTAFF PRODUCT DISTRIBUTION : CREDIT PAYMENT");
						agroTransactAgent.setModeOfPayment(ESEAccount.PAYMENT_MODE_CREDIT);
						agroTransactAgent.setIntBalance(agentAccount.getCreditBalance());
						agroTransactAgent.setTxnAmount(trxnAmount);
						agroTransactAgent
								.setBalAmount(getCreditBalance(agentAccount.getCreditBalance(), trxnAmount, false));

						agroTransactAgent.setTxnType(Distribution.PRODUCT_DISTRIBUTION_TO_FIELDSTAFF);

						String approved = productDistributionDAO.findPrefernceByName(ESESystem.ENABLE_APPROVED);
						if (approved.equalsIgnoreCase("1")) {
							// agroTransactAgent.setAccount(agentAccount);
							// agentAccount.setCreditBalance(agroTransactAgent.getBalAmount());
						} else {
							agroTransactAgent.setAccount(agentAccount);
							agentAccount.setCreditBalance(agroTransactAgent.getBalAmount());
						}
						agroTransactionList.add(agroTransactAgent);

					}
				}
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return agroTransactionList;
	}

	private Collection<AgroTransaction> buildCashAgroTransactionDistributionObject(Distribution distribution,
			double trxnAmount, String tenantId) {
		AgroTransaction refAgroTransaction = new AgroTransaction();
		List<AgroTransaction> agroTransactionList = new ArrayList<AgroTransaction>();
		try {
			if (distribution.getAgroTransaction().getServicePointId() != null
					&& (!StringUtil.isEmpty(distribution.getAgroTransaction().getServicePointId()))) {
				String cmpProfId = "BASIX";
				ESEAccount companyAccount = accountDAO.findAccountByProfileId(cmpProfId, tenantId);
				if (ObjectUtil.isEmpty(companyAccount)) {

				}
				refAgroTransaction.setReceiptNo(distribution.getAgroTransaction().getReceiptNo());
				refAgroTransaction.setAgentId(null);
				refAgroTransaction.setAgentName(null);
				refAgroTransaction.setDeviceId("N/A");
				refAgroTransaction.setDeviceName("N/A");
				refAgroTransaction.setServicePointId(distribution.getAgroTransaction().getServicePointId());
				refAgroTransaction.setServicePointName(distribution.getAgroTransaction().getServicePointName());
				refAgroTransaction.setFarmerId(distribution.getAgroTransaction().getFarmerId());
				refAgroTransaction.setFarmerName(distribution.getAgroTransaction().getFarmerName());
				refAgroTransaction.setVendorId(null);
				refAgroTransaction.setVendorName(null);
				refAgroTransaction.setProfType(null);
				refAgroTransaction.setOperType(1);
				refAgroTransaction.setTxnTime(distribution.getAgroTransaction().getTxnTime());
				refAgroTransaction.setTxnDesc("PRODUCT DISTRIBUTION  : CASH PAYMENT");
				refAgroTransaction.setModeOfPayment(ESEAccount.PAYMENT_MODE_CASH);
				refAgroTransaction.setIntBalance(companyAccount.getCashBalance());
				refAgroTransaction.setTxnAmount(trxnAmount);
				refAgroTransaction.setBalAmount(getCashBalance(companyAccount.getCashBalance(), trxnAmount, true));
				refAgroTransaction.setAccount(companyAccount);
				refAgroTransaction.setTxnType(distribution.getAgroTransaction().getTxnType());

				String approved = productDistributionDAO.findPrefernceByName(ESESystem.ENABLE_APPROVED);
				if (approved.equalsIgnoreCase("1")) {
					// companyAccount.setCashBalance(refAgroTransaction.getBalAmount());
				} else {
					companyAccount.setCashBalance(refAgroTransaction.getBalAmount());
				}
				agroTransactionList.add(refAgroTransaction);

			}
			// Farmer Account Process
			// AgroTransaction agroTransactionFarmer = new AgroTransaction();

			// Agent Account Process

			if (distribution.getAgroTransaction().getAgentId() != null) {

				ESEAccount agentAccount = accountDAO.findAccountByProfileIdAndProfileType(
						distribution.getAgroTransaction().getAgentId(), ESEAccount.AGENT_ACCOUNT, tenantId);
				if (!ObjectUtil.isEmpty(agentAccount)) {
					AgroTransaction agroTransactAgent = new AgroTransaction();
					agroTransactAgent.setReceiptNo(distribution.getAgroTransaction().getReceiptNo());
					agroTransactAgent.setAgentId(distribution.getAgroTransaction().getAgentId());
					agroTransactAgent.setAgentName(distribution.getAgroTransaction().getAgentName());
					agroTransactAgent.setDeviceId(distribution.getAgroTransaction().getDeviceId());
					agroTransactAgent.setDeviceName(distribution.getAgroTransaction().getDeviceName());
					agroTransactAgent.setServicePointId(null);
					agroTransactAgent.setServicePointName(null);
					agroTransactAgent.setFarmerId(distribution.getAgroTransaction().getFarmerId());
					agroTransactAgent.setFarmerName(distribution.getAgroTransaction().getFarmerName());
					agroTransactAgent.setVendorId(null);
					agroTransactAgent.setVendorName(null);
					agroTransactAgent.setProfType(null);
					agroTransactAgent.setOperType(1);
					agroTransactAgent.setTxnTime(distribution.getAgroTransaction().getTxnTime());
					agroTransactAgent.setTxnDesc("FARMER PRODUCT DISTRIBUTION : CASH PAYMENT");
					agroTransactAgent.setModeOfPayment(ESEAccount.PAYMENT_MODE_CASH);
					agroTransactAgent.setIntBalance(agentAccount.getCashBalance());
					agroTransactAgent.setTxnAmount(trxnAmount);
					agroTransactAgent.setBalAmount(getCashBalance(agentAccount.getCashBalance(), trxnAmount, true));
					agroTransactAgent.setAccount(agentAccount);
					agroTransactAgent.setTxnType(Distribution.PRODUCT_DISTRIBUTION_TO_FIELDSTAFF);
					String approved = productDistributionDAO.findPrefernceByName(ESESystem.ENABLE_APPROVED);
					if (approved.equalsIgnoreCase("1")) {
						// agentAccount.setCashBalance(agroTransactAgent.getBalAmount());
					} else {
						agentAccount.setCashBalance(agroTransactAgent.getBalAmount());
					}
					agroTransactionList.add(agroTransactAgent);

				}
			}
			if (distribution.getAgroTransaction().getFarmerId() != null) {
				Farmer farmer = farmerDAO.findFarmerByFarmerId(distribution.getAgroTransaction().getFarmerId(),
						tenantId);
				Season season = findCurrentSeason();
				if (!ObjectUtil.isEmpty(farmer) && !ObjectUtil.isEmpty(season)) {
					ESEAccount farmerAccount = farmerDAO.findAccountBySeassonProcurmentProductFarmer(season.getId(), 0L,
							farmer.getId(), tenantId);
					if (!ObjectUtil.isEmpty(farmerAccount)) {

					}
					AgroTransaction agroTransactFarmer = new AgroTransaction();
					agroTransactFarmer.setReceiptNo(distribution.getAgroTransaction().getReceiptNo());
					agroTransactFarmer.setAgentId(null);
					agroTransactFarmer.setAgentName(null);
					agroTransactFarmer.setDeviceId("N/A");
					agroTransactFarmer.setDeviceName("N/A");
					agroTransactFarmer.setServicePointId(null);
					agroTransactFarmer.setServicePointName(null);
					agroTransactFarmer.setFarmerId(distribution.getAgroTransaction().getFarmerId());
					agroTransactFarmer.setFarmerName(distribution.getAgroTransaction().getFarmerName());
					agroTransactFarmer.setVendorId(null);
					agroTransactFarmer.setVendorName(null);
					agroTransactFarmer.setProfType(null);
					agroTransactFarmer.setOperType(1);
					agroTransactFarmer.setTxnTime(distribution.getAgroTransaction().getTxnTime());
					agroTransactFarmer.setTxnDesc("FARMER PRODUCT DISTRIBUTION : CASH PAYMENT");
					agroTransactFarmer.setModeOfPayment(ESEAccount.PAYMENT_MODE_CASH);
					agroTransactFarmer.setIntBalance(farmerAccount.getCashBalance());
					agroTransactFarmer.setTxnAmount(trxnAmount);
					if (farmerAccount.getCashBalance() > 0.00) {
						agroTransactFarmer
								.setBalAmount(getCashBalance(farmerAccount.getCashBalance(), trxnAmount, false) < 0
										? 0.00 : getCashBalance(farmerAccount.getCashBalance(), trxnAmount, false));
					} else {
						agroTransactFarmer.setBalAmount(farmerAccount.getCashBalance());
					}

					agroTransactFarmer.setAccount(farmerAccount);
					agroTransactFarmer.setTxnType(Distribution.PRODUCT_DISTRIBUTION_TO_FARMER);

					String approved = productDistributionDAO.findPrefernceByName(ESESystem.ENABLE_APPROVED);
					if (approved.equalsIgnoreCase("1")) {
						// farmerAccount.setCashBalance(agroTransactFarmer.getBalAmount());
					} else {
						farmerAccount.setCashBalance(agroTransactFarmer.getBalAmount());
					}

					agroTransactionList.add(agroTransactFarmer);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return agroTransactionList;
	}

	private Collection<AgroTransaction> buildCreditAgroTransactionDistributionObjectForGreaterAmount1(
			Distribution distribution, double trxnAmount, String tenantId) {
		List<AgroTransaction> agroTransactionList = new ArrayList<AgroTransaction>();
		try {
			// Farmer Account Process
			Farmer farmer = farmerDAO.findFarmerByFarmerId(distribution.getAgroTransaction().getFarmerId());
			Season season = findCurrentSeason();
			if (!ObjectUtil.isEmpty(farmer) && !ObjectUtil.isEmpty(season)) {
				ESEAccount farmerAccount = farmerDAO.findAccountBySeassonProcurmentProductFarmer(season.getId(), 0L,
						farmer.getId());
				if (!ObjectUtil.isEmpty(farmerAccount)) {

				}
				AgroTransaction agroTransactionFarmer = new AgroTransaction();

				agroTransactionFarmer.setReceiptNo(distribution.getAgroTransaction().getReceiptNo());
				agroTransactionFarmer.setAgentId("N/A");
				agroTransactionFarmer.setAgentName("N/A");
				agroTransactionFarmer.setDeviceId("N/A");
				agroTransactionFarmer.setDeviceName("N/A");
				agroTransactionFarmer.setServicePointId(null);
				agroTransactionFarmer.setServicePointName(null);
				agroTransactionFarmer.setFarmerId(distribution.getAgroTransaction().getFarmerId());
				agroTransactionFarmer.setFarmerName(distribution.getAgroTransaction().getFarmerName());
				agroTransactionFarmer.setVendorId(null);
				agroTransactionFarmer.setVendorName(null);
				agroTransactionFarmer.setProfType(null);
				agroTransactionFarmer.setOperType(1);
				agroTransactionFarmer.setTxnTime(distribution.getAgroTransaction().getTxnTime());
				agroTransactionFarmer.setTxnDesc("FARMER PRODUCT DISTRIBUTION : CREDIT PAYMENT");
				agroTransactionFarmer.setModeOfPayment(ESEAccount.PAYMENT_MODE_CREDIT);
				agroTransactionFarmer.setIntBalance(farmerAccount.getCreditBalance());
				agroTransactionFarmer.setTxnAmount(trxnAmount);

				agroTransactionFarmer
						.setBalAmount(getCreditBalance(farmerAccount.getCreditBalance(), (trxnAmount), true));

				agroTransactionFarmer.setAccount(farmerAccount);
				agroTransactionFarmer.setTxnType(Distribution.PRODUCT_DISTRIBUTION_TO_FARMER);

				String approved = productDistributionDAO.findPrefernceByName(ESESystem.ENABLE_APPROVED);
				if (approved.equalsIgnoreCase("1")) {
					// farmerAccount.setCreditBalance(agroTransactionFarmer.getBalAmount());
				} else {
					farmerAccount.setCreditBalance(agroTransactionFarmer.getBalAmount());
				}
				agroTransactionList.add(agroTransactionFarmer);

			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return agroTransactionList;
	}

	@Override
	public List<ProcurementGrade> listProcurementVarietyByGradeCode(String gradeCode) {

		return productDistributionDAO.listProcurementVarietyByGradeCode(gradeCode);
	}

	@Override
	public List<CityWarehouse> listProductsByProductId(long coOperativeId, long farmerId, long productId) {

		return productDistributionDAO.listProductsByProductId(coOperativeId, farmerId, productId);
	}

	@Override
	public CityWarehouse listStockByFarmerIdProductIdGradeCodeAndCooperativeId(long cooperativeId, long farmerId,
			long procurementProductId, String gradeCode) {

		return productDistributionDAO.listStockByFarmerIdProductIdGradeCodeAndCooperativeId(cooperativeId, farmerId,
				procurementProductId, gradeCode);
	}

	@Override
	public ProcurementVariety findProcurementVariertyByProductCode(String productCode) {

		return productDistributionDAO.findProcurementVariertyByProductCode(productCode);
	}

	@Override
	public CityWarehouse listCityWarehouseProductsByProductCode(String productCode) {

		return productDistributionDAO.listCityWarehouseProductsByProductCode(productCode);
	}

	@Override
	public CityWarehouse listCityWareHouseByCooperativeIdFarmerIdAndProductId(long farmerId, long procurementProductId,
			long cooperativeId) {
		return productDistributionDAO.listCityWareHouseByCooperativeIdFarmerIdAndProductId(farmerId,
				procurementProductId, cooperativeId);
	}

	@Override
	public CityWarehouse findCityWareHouseByWarehouseIdProcrmentGradeCodeAndProcurementProductIdAndFarmerId(
			long farmerId, long warehouseId, String gradeCode, long productId) {

		return productDistributionDAO
				.findCityWareHouseByWarehouseIdProcrmentGradeCodeAndProcurementProductIdAndFarmerId(farmerId,
						warehouseId, gradeCode, productId);
	}

	@Override
	public List<ProcurementVariety> listProcurementProductVarietyByvarietyCode(String code) {

		// TODO Auto-generated method stub
		return productDistributionDAO.listProcurementProductVarietyByvarietyCode(code);
	}

	@Override
	public List<ProcurementProduct> listProcurementProductByProductId(long id) {

		// TODO Auto-generated method stub
		return productDistributionDAO.listProcurementProductByProductId(id);
	}

	@Override
	public List<ProcurementVariety> listProcurementProductVarietyByVarietyId(long id) {

		// TODO Auto-generated method stub
		return productDistributionDAO.listProcurementProductVarietyByVarietyId(id);
	}

	public void updateProcurementDetails(ProcurementDetail procurementDetails) {
		// TODO Auto-generated method stub
		productDistributionDAO.update(procurementDetails);
		CityWarehouse cityWarehouse = null;
		/*if (procurementDetails.getProcurement().getAgroTransaction().getAgentId() != null) {
			cityWarehouse = findByProdIdAndGradeCode(procurementDetails.getProcurementProduct().getId(),
					procurementDetails.getProcurementGrade().getCode().trim(), procurementDetails.getTenantId());
		} else {
			cityWarehouse = findByProdIdAndGradeCode(procurementDetails.getProcurementProduct().getId(),
					procurementDetails.getProcurementGrade().getCode().trim(), procurementDetails.getTenantId());
		}*/
		
		cityWarehouse = findCityWarehouseByProductGradeWarehouse(procurementDetails.getProcurementProduct().getId(),
				procurementDetails.getProcurementGrade().getCode().trim(), procurementDetails.getTenantId(),
				Long.valueOf(procurementDetails.getProcurement().getWarehouseId()));

		for (CityWarehouseDetail cityWarehouseDetail : cityWarehouse.getCityWarehouseDetails()) {
			cityWarehouseDetail.setTxnGrossWeight(procurementDetails.getGrossWeight());
			cityWarehouseDetail.setTotalGrossWeight(procurementDetails.getGrossWeight());
			cityWarehouseDetail.setTotalNumberOfBags(procurementDetails.getNumberOfBags());
			productDistributionDAO.update(cityWarehouseDetail);
		}
		cityWarehouse.setNumberOfBags(procurementDetails.getNumberOfBags());
		cityWarehouse.setGrossWeight(procurementDetails.getGrossWeight());
		cityWarehouse.setRevisionNo(DateUtil.getRevisionNumber());
		cityWarehouse.setBranchId(procurementDetails.getProcurement().getBranchId());
		productDistributionDAO.update(cityWarehouse);
	}

	private CityWarehouse findCityWarehouseByProductGradeWarehouse(long prodId, String grade, String tenantId,
			Long warehouseId) {
		return productDistributionDAO.findCityWarehouseByProductGradeWarehouse(prodId,  grade,  tenantId,warehouseId);
	}

	@Override
	public ProcurementDetail findByProcurementDetailId(Long procuDetailId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findByProcurementDetailId(procuDetailId);
	}

	@Override
	public CityWarehouse findByProdIdAndGradeCode(long prodId, String gradeCode, String tenantId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findByProdIdAndGradeCode(prodId, gradeCode, tenantId);
	}

	@Override
	public DistributionDetail findDistributionDetailById(Long id) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findDistributionDetailById(id);
	}

	@Override
	public void updateDistributionDetail(DistributionDetail existingDistributionDetail) {
		productDistributionDAO.update(existingDistributionDetail);
	}

	@Override
	public void updateDistribution(Distribution existingDistribution) {
		// TODO Auto-generated method stub
		productDistributionDAO.update(existingDistribution);
	}

	@Override
	public List<Object[]> findWarehouseProductAvailableStockByAgentIdProductId(long id, String agentId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findWarehouseProductAvailableStockByAgentIdProductId(id, agentId);
	}

	@Override
	public void updateStockById(Long warehouseProductId, String currentQuantity) {
		// TODO Auto-generated method stub
		productDistributionDAO.updateStockById(warehouseProductId, currentQuantity);
	}

	@Override
	public List<WarehouseProductDetail> findWarehouseproductDetailByWarehouseproductIdAndReceiptNo(long id,
			String receiptNumber) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findWarehouseproductDetailByWarehouseproductIdAndReceiptNo(id, receiptNumber);
	}

	@Override
	public HarvestSeason findHarvestSeasonBySeasonCode(String seasonCode) {

		// TODO Auto-generated method stub
		return productDistributionDAO.findHarvestSeasonBySeasonCode(seasonCode);
	}

	@Override
	public Object findNoOfBagByByWarehouseIdProductIdAndFarmerId(Object warehouseId, Object productId,
			Object farmerId) {

		return productDistributionDAO.findNoOfBagByByWarehouseIdProductIdAndFarmerId(warehouseId, productId, farmerId);
	}

	@Override
	public Object findNetWeightByWarehouseIdProductIdAndFarmerId(Object warehouseId, Object productId,
			Object farmerId) {

		return productDistributionDAO.findNetWeightByWarehouseIdProductIdAndFarmerId(warehouseId, productId, farmerId);
	}

	@Override
	public List<CityWarehouse> listGradeByWarehouseIdFarmerIdProductIdAndVarietyCode(long coOperativeId, long farmerId,
			long productId, String varietyCode) {

		// TODO Auto-generated method stub
		return productDistributionDAO.listGradeByWarehouseIdFarmerIdProductIdAndVarietyCode(coOperativeId, farmerId,
				productId, varietyCode);
	}

	@Override
	public List<CityWarehouse> listProductsByFarmerId(long farmerId) {

		// TODO Auto-generated method stub
		return productDistributionDAO.listProductsByFarmerId(farmerId);
	}

	@Override
	public String addProcurementTransfer(PMT pmt) {

		if (!ObjectUtil.isEmpty(pmt) && !ObjectUtil.isListEmpty(pmt.getPmtDetails())) {
			if (StringUtil.isEmpty(pmt.getMtntReceiptNumber())) {
				pmt.setMtntReceiptNumber(idGenerator.getMTNTReceiptNoSeq());
				productDistributionDAO.save(pmt);
				processCityWarehouse1(pmt);
				return pmt.getMtntReceiptNumber();
			}
			productDistributionDAO.saveOrUpdate(pmt);
			processCityWarehouse1(pmt);
		}
		return null;
	}

	@Override
	public List<WarehousePayment> selectVendorListByBranchId(String branchId) {
		return this.productDistributionDAO.selectVendorListByBranchId(branchId);
	}

	@Override
	public WarehouseProduct findAvailableStockByWarehouseIdSelectedProductSeason(Long warehouseId, Long productId,
			String selectedSeason) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findAvailableStockByWarehouseIdSelectedProductSeason(warehouseId, productId,
				selectedSeason);
	}

	@Override
	public List<Object[]> listOfWarehouseByStockEntry() {
		// TODO Auto-generated method stub
		return productDistributionDAO.listOfWarehouseByStockEntry();
	}

	@Override
	public WarehouseProduct findAvailableStockByWarehouseIdSelectedProductSeasonBatchNo(long warehouseId,
			long productId, String selectedSeason, String batchNo) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findAvailableStockByWarehouseIdSelectedProductSeasonBatchNo(warehouseId,
				productId, selectedSeason, batchNo);
	}

	public List<Object[]> findBatchNoListByWarehouseIdProductIdSeason(long warehouseId, long productId,
			String selectedSeason) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findBatchNoListByWarehouseIdProductIdSeason(warehouseId, productId,
				selectedSeason);
	}

	@Override
	public WarehouseProduct findAvailableStockByWarehouseIdProductIdBatchNo(long warehouseId, long productId,
			String batchNo) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findAvailableStockByWarehouseIdProductIdBatchNo(warehouseId, productId, batchNo);
	}

	@Override
	public List<Object[]> findBatchNoListByAgentIdProductIdSeason(String agentId, long productId,
			String selectedSeason) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findBatchNoListByAgentIdProductIdSeason(agentId, productId, selectedSeason);
	}

	@Override
	public WarehouseProduct findAvailableStockByAgentIdProductIdBatchNoSeason(String agentId, long productId,
			String selectedSeason, String batchNo) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findAvailableStockByAgentIdProductIdBatchNoSeason(agentId, productId,
				selectedSeason, batchNo);
	}

	@Override
	public Product findProductBySubCategoryId(long subCategoryId) {

		return productDistributionDAO.findProductBySubCategoryId(subCategoryId);
	}

	@Override
	public AgroTransaction findAgentByAgentId(String agentId) {
		return productDistributionDAO.findAgentByAgentId(agentId);
	}

	@Override
	public double findAvailableStockByWarehouseIdProductIdSeasonBatchNo(long warehouseId, long productId,
			String selectedSeason, String batchNo) {

		return productDistributionDAO.findAvailableStockByWarehouseIdProductIdSeasonBatchNo(warehouseId, productId,
				selectedSeason, batchNo);
	}

	@Override
	public double findAvailableStockByAgentIdProductIdSeasonBatchNo(String agentId, long productId,
			String selectedSeason, String batchNo) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findAvailableStockByAgentIdProductIdSeasonBatchNo(agentId, productId,
				selectedSeason, batchNo);
	}

	@Override
	public WarehouseProduct findFieldStaffAvailableStockBySeasonAndBatch(String profileId, long productId,
			String seasonCode, String batchNo, String tenantId,String branchId) {
		return productDistributionDAO.findFieldStaffAvailableStockBySeasonAndBatch(profileId, productId, seasonCode,
				batchNo, tenantId,branchId);
	}

	@Override
	public Distribution findDistributionFarmerByRecNoAndTxnType(String receiptNumber, String txnType) {
		return productDistributionDAO.findDistributionFarmerByRecNoAndTxnType(receiptNumber, txnType);
	}

	@Override
	public WarehouseProduct findwarehouseProductById(Long id) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findwarehouseProductById(id);
	}

	@Override
	public WarehousePayment findwarehousePaymentById(Long id) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findwarehousePaymentById(id);
	}

	@Override
	public WarehousePaymentDetails findwarehousePaymentDetailById(long id) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findwarehousePaymentDetailById(id);
	}

	@Override
	public Object[] findAvailableStockAndDamagedStockByWarehouseIdProductIdSeasonBatchNo(long warehouseId,
			long productId, String selectedSeason, String batchNo) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findAvailableStockAndDamagedStockByWarehouseIdProductIdSeasonBatchNo(warehouseId,
				productId, selectedSeason, batchNo);
	}

	@Override
	public WarehouseProductDetail findwarehouseProductDetailByWarehouseProductIdReceiptNo(long id, String receiptNo) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findwarehouseProductDetailByWarehouseProductIdReceiptNo(id, receiptNo);
	}

	@Override
	public void updateWarehouseProductDetail(WarehouseProductDetail productDetail) {
		// TODO Auto-generated method stub
		productDistributionDAO.update(productDetail);
	}

	@Override
	public WarehouseProduct findAvailableStockByAgentIdProductIdSeason(String profileId, long productId,
			String selectedSeason) {

		// TODO Auto-generated method stub
		return productDistributionDAO.findAvailableStockByAgentIdProductIdSeason(profileId, productId, selectedSeason);
	}

	public Object findNoOfBagByByWarehouseIdProductIdAndGradeCode(Object warehouseId, Object productId,
			Object gradeCode) {

		return productDistributionDAO.findNoOfBagByByWarehouseIdProductIdAndGradeCode(warehouseId, productId,
				gradeCode);
	}

	public Object findNetWeightByWarehouseIdProductIdAndGradeCode(Object warehouseId, Object productId,
			Object gradeCode) {

		return productDistributionDAO.findNetWeightByWarehouseIdProductIdAndGradeCode(warehouseId, productId,
				gradeCode);
	}

	public Object findNoOfBagByByWarehouseIdProductId(Object warehouseId, Object productId) {

		return productDistributionDAO.findNoOfBagByByWarehouseIdProductId(warehouseId, productId);
	}

	public Object findNetWeightByWarehouseIdProductId(Object warehouseId, Object productId) {

		return productDistributionDAO.findNetWeightByWarehouseIdProductId(warehouseId, productId);
	}

	@Override
	public List<Object[]> listPMTReceiptNumberByWarehouse(long warehouseId) {
		return productDistributionDAO.listPMTReceiptNumberByWarehouse(warehouseId);
	}

	@Override
	public List<ProcurementProduct> listProcurementProductByPMTReceiptNo(String receiptNo) {
		return productDistributionDAO.listProcurementProductByPMTReceiptNo(receiptNo);
	}

	@Override
	public List<ProcurementGrade> listPMTProcurementGradeByVarietyId(String receiptNo) {
		return productDistributionDAO.listPMTProcurementGradeByVarietyId(receiptNo);
	}

	@Override
	public PMTDetail findpmtdetailByProcurementGrade(Long gradeId) {
		return productDistributionDAO.findpmtdetailByProcurementGrade(gradeId);
	}

	@Override
	public List<Object[]> listProcurementProductByCultivation() {
		// TODO Auto-generated method stub
		return productDistributionDAO.listProcurementProductByCultivation();
	}

	@Override
	public ProcurementGrade findProcurementGradeByVarityId(long varietyId) {
		return productDistributionDAO.findProcurementGradeByVarityId(varietyId);
	}

	@Override
	public List<Object[]> listProcurementProductByVariety(Long procurementProdId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listProcurementProductByVariety(procurementProdId);
	}

	@Override
	public CityWarehouse findCityWarehouseIdByFarmerAndProductIdAndGradeCode(long farmerId, long productId,
			String gradeCode) {

		return productDistributionDAO.findCityWarehouseIdByFarmerAndProductIdAndGradeCode(farmerId, productId,
				gradeCode);
	}

	@Override
	public List<AgroTransaction> findAgroTxnByFarmerIdTXnTypesSeasonCode(String farmerId, String seasonCode,
			String txnTypes) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findAgroTxnByFarmerIdTXnTypesSeasonCode(farmerId, seasonCode, txnTypes);
	}

	@Override
	public WarehouseProduct findAvailableStockByWarehouseIdSelectedProductBatchNo(long warehouseId, long productId,
			String batchNo) {

		return productDistributionDAO.findAvailableStockByWarehouseIdSelectedProductBatchNo(warehouseId, productId,
				batchNo);
	}

	@Override
	public WarehouseProduct findAvailableStockByAgentIdProductIdBatchNo(String agentId, long productId,
			String batchNo) {

		return productDistributionDAO.findAvailableStockByAgentIdProductIdBatchNo(agentId, productId, batchNo);
	}

	@Override
	public AgroTransaction findAgrotxnByReceiptNoAndProfType(String receiptNumber, String profType) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findAgrotxnByReceiptNoAndProfType(receiptNumber, profType);
	}

	@Override
	public List<AgroTransaction> listAgroTransactionByReceiptNoAndProfType(String receiptNumber, String farmerAcc) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listAgroTransactionByReceiptNoAndProfType(receiptNumber, farmerAcc);
	}

	@Override
	public void update(AgroTransaction eseFarmerTransaction) {
		// TODO Auto-generated method stub
		productDistributionDAO.update(eseFarmerTransaction);
	}

	@Override
	public double findAvailableStockByWarehouseIdProductIdBatchNum(long warehouseId, long productId, String batchNo) {

		return productDistributionDAO.findAvailableStockByWarehouseIdProductIdBatchNum(warehouseId, productId, batchNo);
	}

	@Override
	public double findAvailableStockByAgentIdProductIdBatchNum(String agentId, long productId, String batchNo) {

		return productDistributionDAO.findAvailableStockByAgentIdProductIdBatchNum(agentId, productId, batchNo);
	}

	@Override
	public WarehouseProduct findFieldStaffAvailableStockByBatch(String profileId, long productId, String batchNo,
			String tenantId) {

		return productDistributionDAO.findFieldStaffAvailableStockByBatch(profileId, productId, batchNo, tenantId);
	}

	@Override
	public List<ProcurementProduct> listProcurementProductByPMTReceiptNoAndFarmer(String receiptNoId, Long farmerid) {
		return productDistributionDAO.listProcurementProductByPMTReceiptNoAndFarmer(receiptNoId, farmerid);
	}

	@Override
	public List<Object[]> listProcurementProductFarmersByPMTReceiptNo(String receiptNoId) {
		return productDistributionDAO.listProcurementProductFarmersByPMTReceiptNo(receiptNoId);
	}

	@Override
	public PMTDetail findpmtdetailByProcurementGradeAndReceiptNo(Long gradeId, String receiptNoId) {
		return productDistributionDAO.findpmtdetailByProcurementGradeAndReceiptNo(gradeId, receiptNoId);
	}

	@Override
	public List<ProcurementGrade> listPMTProcurementGradeByVarietyIdAndProduct(String receiptNoId,
			String selectedProduct) {
		return productDistributionDAO.listPMTProcurementGradeByVarietyIdAndProduct(receiptNoId, selectedProduct);
	}

	@Override
	public Product findProductByProductId(long productId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findProductByProductId(productId);
	}

	@Override
	public WarehouseProduct findAvailableStocksByBatch(String servicePointId, long productId, String batch,
			String tenantId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findAvailableStocksByBatch(servicePointId, productId, batch, tenantId);
	}

	@Override
	public CityWarehouse findCityWarehouseIdByAgentAndProductIdAndGradeCode(String agentId, long productId,
			String gradeCode) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findCityWarehouseIdByAgentAndProductIdAndGradeCode(agentId, productId, gradeCode);
	}

	@Override
	public List<WarehousePaymentDetails> listWarehousePaymentDetailsByWarehousePaymentId(long id) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listWarehousePaymentDetailsByWarehousePaymentId(id);
	}

	public void saveProductReturnAndProductReturnDetail(ProductReturn productReturn) {

		/** SAVING productReturn OBJECT **/
		if (!ProductReturn.PRODUCT_RETURN_FROM_FARMER
				.equalsIgnoreCase(productReturn.getAgroTransaction().getTxnType())) {
			if (ObjectUtil.isEmpty(productReturn.getAgroTransaction().getRefAgroTransaction()))
				productReturn.getAgroTransaction()
						.setRefAgroTransaction(processAgroTransaction(productReturn.getAgroTransaction()));
			productDistributionDAO.save(productReturn.getAgroTransaction().getRefAgroTransaction());
			productDistributionDAO.save(productReturn.getAgroTransaction());
			// accountDAO.updateCashBal(distribution.getAgroTransaction().getAccount().getId(),distribution.getAgroTransaction().getAccount().getCashBalance(),distribution.getAgroTransaction().getAccount().getCreditBalance());
		} else {
			List<AgroTransaction> agroDistList = buildProductReturnAgroTransactionObject(productReturn);
			// List<AgroTransaction> agroDistList
			// =buildAgroTransactionDistributionObject(distribution);
			for (AgroTransaction agro : agroDistList) {
				productDistributionDAO.save(agro);

			}
			productReturn.setAgroTransaction(null);
		}
		productDistributionDAO.save(productReturn);
		processWarehouseProduct(productReturn);
	}

	private void processWarehouseProduct(Object object) {

		DecimalFormat df = new DecimalFormat("0.00");
		int i = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		WarehouseProduct warehouseProduct;
		if (!ObjectUtil.isEmpty(object)) {

			if (object instanceof ProductReturn) { // Distribution
				Double avbleStock = 0.00;
				ProductReturn productReturn = (ProductReturn) object;
				String agentId = productReturn.getAgentId();
				String txnType = productReturn.getTxnType();
				map.put(DATE, productReturn.getTxnTime());
				map.put(RECEIPT_NO, productReturn.getReceiptNumber());
				map.put(SEASON_CODE, productReturn.getSeasonCode());
				
				// Product Distribution to Farmer
				if (ProductReturn.PRODUCT_RETURN_FROM_FARMER.equals(txnType)) {
					map.put(DESC, ProductReturn.PRODUCT_RETURN_FROM_FARMER_DESCRIPTION);
					for (ProductReturnDetail productReturnDetail : productReturn.getProductReturnDetail()) {

						if (productReturn.getProductStock()
								.equalsIgnoreCase(WarehouseProduct.StockType.WAREHOUSE_STOCK.name())) {

							Warehouse warehouse = locationDAO.findCoOperativeByCode(productReturn.getServicePointId());
							warehouseProduct = productDistributionDAO
									.findAvailableStockByWarehouseIdSelectedProductSeasonBatchNo(warehouse.getId(),
											productReturnDetail.getProduct().getId(),
											productReturnDetail.getSeasonCode(), productReturnDetail.getBatchNo());
							// updateDistributionProduct(productReturnDetail);

						} else {
							warehouseProduct = productDistributionDAO.findAvailableStockByAgentIdProductIdBatchNoSeason(
									agentId, productReturnDetail.getProduct().getId(),
									productReturnDetail.getSeasonCode(), productReturnDetail.getBatchNo());

						}
						map.put(QTY, productReturnDetail.getQuantity());
						
						if(ObjectUtil.isEmpty(warehouseProduct)){
							//warehouseProduct.setBranchId(productReturnDetail.getBranchId());
							
							warehouseProduct = new WarehouseProduct();
							warehouseProduct.setBranchId(productReturn.getBranchId());
							warehouseProduct.setSeasonCode(productReturn.getSeasonCode());
							warehouseProduct.setStock(Double.valueOf(productReturnDetail.getCurrentQuantity()));
							warehouseProduct.setDamagedStock(Double.valueOf("0.00"));
							warehouseProduct.setCostPrice(Double.valueOf("0.00"));
							warehouseProduct.setWarehouse(productReturn.getWarehouse());
							warehouseProduct.setAgent(productReturn.getAgent());
							//warehouseProduct.setWarehouse(productReturnDetail.getwa);
							warehouseProduct.setProduct(productReturnDetail.getProduct());
							warehouseProduct.setRevisionNo(DateUtil.getRevisionNumber());
							warehouseProduct.setBatchNo(productReturnDetail.getBatchNo());
							
							Set<WarehouseProductDetail> WarProductDetails = new LinkedHashSet<WarehouseProductDetail>();
							
							WarehouseProductDetail warehouseProductDetail = new WarehouseProductDetail();
							warehouseProductDetail.setDate((Date) map.get(DATE));
							warehouseProductDetail.setWarehouseProduct(warehouseProduct);
							warehouseProductDetail.setDesc((String) map.get(DESC));
							warehouseProductDetail.setPrevStock(0.0);
							warehouseProductDetail.setTxnStock((Double) map.get(QTY));
							warehouseProductDetail.setOrderNo(
									!StringUtil.isEmpty(warehouseProduct.getOrderNo()) ? warehouseProduct.getOrderNo() : "");
							warehouseProductDetail.setUserName(
									!StringUtil.isEmpty(warehouseProduct.getUserName()) ? warehouseProduct.getUserName() : "");
							warehouseProductDetail.setVendorId(
									!StringUtil.isEmpty(warehouseProduct.getVendorId()) ? warehouseProduct.getVendorId() : "");
							warehouseProductDetail.setDamagedQty(Double.valueOf(warehouseProduct.getDamagedQty()));
							warehouseProductDetail.setCostPrice(Double.valueOf(warehouseProduct.getCostPrice()));
							warehouseProductDetail
										.setFinalStock(warehouseProductDetail.getPrevStock() + warehouseProductDetail.getTxnStock());
						
							warehouseProductDetail.setReceiptNo((String) map.get(RECEIPT_NO));
							WarProductDetails.add(warehouseProductDetail);
							warehouseProduct.setWarehouseDetails(WarProductDetails);
							productDistributionDAO.save(warehouseProduct);
							
						}else{
						
						map.put(QTY, productReturnDetail.getQuantity());
						String approved = productDistributionDAO.findPrefernceByName(ESESystem.ENABLE_APPROVED);
						if (ProductReturn.PRODUCT_RETURN_FROM_FARMER.equalsIgnoreCase(productReturn.getTxnType())) {
							if (approved.equalsIgnoreCase("0")) {
								updateWarehouseProduct(warehouseProduct, map, false);
							} else {
								updateWarehouseProduct(warehouseProduct, map, false);
							}
						}
						}
						/*
						 * if (approved.equalsIgnoreCase("0")) {
						 * updateWarehouseProduct(warehouseProduct, map, false);
						 * } else { updateWarehouseProduct(warehouseProduct,
						 * map, false); }
						 */

					}

					PaymentLedger paymentLedger = new PaymentLedger();
					paymentLedger.setCreatedDate(productReturn.getTxnTime());
					paymentLedger.setCreatedUser(productReturn.getUserName());
					paymentLedger.setType(ProductReturn.PRODUCT_RETURN_FROM_FARMER);
					paymentLedger.setPrevValue(productReturn.getIntBalance());
					paymentLedger.setTxnValue(productReturn.getTxnAmount());
					paymentLedger.setNewValue(productReturn.getBalAmount());

					productDistributionDAO.save(paymentLedger);
				}

				// Product Distribution to Field Staff
				if (ProductReturn.PRODUCT_RETURN_FROM_FIELDSTAFF.equals(txnType)) {
				

					map.put(DESC, Distribution.PRODUCT_RETURN_FROM_FIELDSTAFF_DESCRIPTION);

					for (ProductReturnDetail productReturnDetail : productReturn.getProductReturnDetail()) {

						map.put(QTY, productReturnDetail.getQuantity());

						Warehouse warehouse = locationDAO
								.findCoOperativeByCode(productReturn.getAgroTransaction().getServicePointId());
						/*
						 * WarehouseProduct warehouseStock =
						 * productDistributionDAO
						 * .findCooperativeAvailableStockByCooperative(warehouse
						 * .getId(), distributionDetail.getProduct().getId());
						 */
						WarehouseProduct warehouseStock = productDistributionDAO
								.findAvailableStockByWarehouseIdProductIdBatchNoAndSeason(warehouse.getId(),
										productReturnDetail.getProduct().getId(), productReturnDetail.getBatchNo(),
										productReturn.getSeasonCode());
						String costPrice[] = productReturnDetail.getCostPriceArray().split(",");
						if (costPrice[0].length() > 1) {

							map.put(COSTPRICE, costPrice[i]);
						}
						i++;
						productReturnDetail.setSeasonCode(productReturn.getSeasonCode());
						// distributionDetail.setExistingQuantity(warehouseProd)
						warehouseStock.setBranchId(productReturn.getBranchId());
						warehouseStock.setSeasonCode(productReturn.getSeasonCode());
						warehouseStock.setBatchNo(productReturnDetail.getBatchNo());
						String approved = productDistributionDAO.findPrefernceByName(ESESystem.ENABLE_APPROVED);

						if (ProductReturn.PRODUCT_RETURN_FROM_FIELDSTAFF.equalsIgnoreCase(productReturn.getTxnType())) {
							if (approved.equalsIgnoreCase("0")) {
								updateWarehouseProduct(warehouseStock, map, false);
							}
						} else {
							if (productReturn.getFreeDistribution().equalsIgnoreCase("0")) {
								if (approved.equalsIgnoreCase("0")) {
									updateWarehouseProduct(warehouseStock, map, false);
								}
							} else {
								updateWarehouseProduct(warehouseStock, map, false);
							}
						}
						// Add FS stock
						processFieldStaffsWarehouseProducts(
								productReturn.getAgroTransaction().getRefAgroTransaction().getAgentId(),
								productReturnDetail, warehouseStock.getWarehouse(), map);
					}
				
				
				}

			} // End of Distribution
			if (object instanceof WarehouseProduct) { // WarehouseProduct
				warehouseProduct = (WarehouseProduct) object;
				map.put(DATE, new Date());
				map.put(RECEIPT_NO, !StringUtil.isEmpty(warehouseProduct.getReceiptNumber())
						? warehouseProduct.getReceiptNumber() : "");
				map.put(QTY, warehouseProduct.getTxnQty());
				if (warehouseProduct.isEdit()) {
					map.put(DESC, "STOCK ADDED");
					updateWarehouseProducts(warehouseProduct, map, true);
				} else {
					map.put(DESC, "STOCK DEDUCTED");
					updateWarehouseProducts(warehouseProduct, map, false);
				}
			} // End of WarehouseProduct
		}

	}

	private List<AgroTransaction> buildProductReturnAgroTransactionObject(ProductReturn productReturn) {
		List<AgroTransaction> agroTransactionList = null;
		if (!ObjectUtil.isEmpty(productReturn)) {
			agroTransactionList = new ArrayList<AgroTransaction>();

			if (!StringUtil.isEmpty(productReturn.getFarmerId())) {// Checking
																	// Reg
																	// Former
																	// or
																	// Not
				AgroTransaction finalBalAgroObject = new AgroTransaction();
				finalBalAgroObject = formNewAgroTransaction(finalBalAgroObject, productReturn.getAgroTransaction());
				// distribution.getAgroTransaction();
				finalBalAgroObject.setIntBalance(productReturn.getAgroTransaction().getBalAmount());
				// .setTxnAmount(productReturn.getTxnAmount());
				finalBalAgroObject
						.setBalAmount(productReturn.getAgroTransaction().getBalAmount() + productReturn.getTxnAmount());
				finalBalAgroObject.setProfType("02");
				ESEAccount farmerAccount = productDistributionDAO.findESEAccountByProfileId(productReturn.getFarmerId(),
						ESEAccount.CONTRACT_ACCOUNT);
				if (!ObjectUtil.isEmpty(farmerAccount)) {
					// farmerAccount.setCashBalance(finalBalAgroObject.getBalAmount());
					double creditAmt = farmerAccount.getCreditBalance() + productReturn.getTotalAmount();
					farmerAccount.setCreditBalance(creditAmt);
					productDistributionDAO.update(farmerAccount);
				}
				finalBalAgroObject.setTxnDesc(ProductReturn.PRODUCT_RETURN_PAYMENT_AMOUNT);
				finalBalAgroObject.setTxnType(productReturn.getAgroTransaction().getTxnType());
				finalBalAgroObject.setProductReturn(productReturn);
				// agroTransactionList.add(productReturn.getAgroTransaction());
				agroTransactionList.add(finalBalAgroObject);
				// AgentORWarehouseAccount forming
				if (!StringUtil.isEmpty(productReturn.getAgentId())) {
					ESEAccount agentAccount = productDistributionDAO
							.findESEAccountByProfileId(productReturn.getAgentId(), ESEAccount.AGENT_ACCOUNT);
					if (!ObjectUtil.isEmpty(agentAccount)) {
						AgroTransaction agentAgroTransaction = new AgroTransaction();
						agentAgroTransaction = formNewAgroTransaction(agentAgroTransaction,
								productReturn.getAgroTransaction());
						agentAgroTransaction.setIntBalance(agentAccount.getCashBalance());
						agentAgroTransaction.setTxnAmount(productReturn.getTxnAmount());
						agentAgroTransaction.setBalAmount(agentAccount.getCashBalance() + productReturn.getTxnAmount());
						agentAgroTransaction.setTxnType(ProductReturn.PRODUCT_RETURN_FROM_FARMER);
						agentAccount.setCashBalance(agentAgroTransaction.getBalAmount());
						productDistributionDAO.update(agentAccount);
						agentAgroTransaction.setAccount(agentAccount);
						agentAgroTransaction.setProfType("01");// AGENT_ACCOUNT
						agentAgroTransaction.setTxnDesc(ProductReturn.PRODUCT_RETURN_PAYMENT_AMOUNT);
						agroTransactionList.add(agentAgroTransaction);
					}

				} else if (!StringUtil.isEmpty(productReturn.getServicePointId())) {
					ESEAccount servicePointAccount = productDistributionDAO
							.findESEAccountByProfileId(productReturn.getAgentId(), ESEAccount.COMPANY_ACCOUNT_TYPE);
					if (!ObjectUtil.isEmpty(servicePointAccount)) {
						AgroTransaction warehouseAgroTransactiont = new AgroTransaction();
						warehouseAgroTransactiont = formNewAgroTransaction(warehouseAgroTransactiont,
								productReturn.getAgroTransaction());
						warehouseAgroTransactiont.setIntBalance(servicePointAccount.getCashBalance());
						warehouseAgroTransactiont.setTxnAmount(productReturn.getTxnAmount());
						warehouseAgroTransactiont
								.setBalAmount(servicePointAccount.getCashBalance() + productReturn.getTxnAmount());
						warehouseAgroTransactiont.setAccount(servicePointAccount);
						warehouseAgroTransactiont.setProfType("03");// Warehouse
																	// Account
						agroTransactionList.add(warehouseAgroTransactiont);
					}
				}

			} else if (!StringUtil.isEmpty(productReturn.getAgentId())) {// single
																			// Rec
																			// for
																			// Unreg
																			// Former
				ESEAccount agentAccount = productDistributionDAO.findESEAccountByProfileId(productReturn.getAgentId(),
						ESEAccount.AGENT_ACCOUNT);
				if (!ObjectUtil.isEmpty(agentAccount)) {
					productReturn.getAgroTransaction().setIntBalance(agentAccount.getCashBalance());
					productReturn.getAgroTransaction()
							.setBalAmount(agentAccount.getCashBalance() + productReturn.getTxnAmount());
					productReturn.getAgroTransaction().setAccount(agentAccount);
					productReturn.getAgroTransaction().setTxnDesc(ProductReturn.PRODUCT_RETURN_PAYMENT_AMOUNT);
					productReturn.getAgroTransaction().setProfType("01");// AGENT_ACCOUNT
					agroTransactionList.add(productReturn.getAgroTransaction());
				}

			} else if (!StringUtil.isEmpty(productReturn.getServicePointId())) {
				ESEAccount servicePointAccount = productDistributionDAO
						.findESEAccountByProfileId(productReturn.getAgentId(), ESEAccount.COMPANY_ACCOUNT_TYPE);
				if (!ObjectUtil.isEmpty(servicePointAccount)) {
					productReturn.getAgroTransaction().setIntBalance(servicePointAccount.getCashBalance());
					productReturn.getAgroTransaction()
							.setBalAmount(servicePointAccount.getCashBalance() + productReturn.getTxnAmount());
					productReturn.getAgroTransaction().setAccount(servicePointAccount);
					productReturn.getAgroTransaction().setProfType("03");// Warehouse
																			// Account
					productReturn.getAgroTransaction().setTxnDesc(ProductReturn.PRODUCT_RETURN_PAYMENT_AMOUNT);
					agroTransactionList.add(productReturn.getAgroTransaction());
				}
			}

		}
		// TODO Auto-generated method stub
		return agroTransactionList;
	}

	public void updateDistributionProduct(ProductReturnDetail productReturnDetail) {
		DistributionDetail distributionDetails = productDistributionDAO.findByDistributionDetails(
				productReturnDetail.getProductReturn().getFarmerId(), productReturnDetail.getProduct().getId(),
				productReturnDetail.getBatchNo(), productReturnDetail.getProductReturn().getSeasonCode());
		if (!ObjectUtil.isEmpty(distributionDetails)) {
			double qty = distributionDetails.getQuantity() - productReturnDetail.getQuantity();
			distributionDetails.setQuantity(qty);
			double existQty = Double.valueOf(distributionDetails.getCurrentQuantity())
					+ productReturnDetail.getQuantity();
			distributionDetails.setCurrentQuantity(String.valueOf(existQty));
			productDistributionDAO.saveOrUpdate(distributionDetails.getDistribution());
		}
	}

	private void processFieldStaffsWarehouseProducts(String agentId, ProductReturnDetail productReturnDetail,
			Warehouse warehouse, Map<String, Object> map) {

		/*
		 * WarehouseProduct fieldStaffStock =
		 * productDistributionDAO.findFieldStaffAvailableStock(agentId,
		 * distributionDetail.getProduct().getId());
		 */
		WarehouseProduct fieldStaffStock = productDistributionDAO.findAvailableStockByAgentIdProductIdBatchNoSeason(
				agentId, productReturnDetail.getProduct().getId(), productReturnDetail.getSeasonCode(),
				productReturnDetail.getBatchNo());
		if (ObjectUtil.isEmpty(fieldStaffStock)) {
			fieldStaffStock = new WarehouseProduct();
			Agent agent = agentDAO.findAgentByProfileId(agentId);
			/*
			 * if (ObjectUtil.isEmpty(warehouse)) { warehouse =
			 * agent.getCooperative(); }
			 * fieldStaffStock.setWarehouse(warehouse);
			 */
			fieldStaffStock.setProduct(productReturnDetail.getProduct());
			fieldStaffStock.setBranchId(warehouse.getBranchId());
			fieldStaffStock.setBatchNo(productReturnDetail.getBatchNo());
			// fieldStaffStock.setSeasonCode(distributionDetail.getDistribution().getSeasonCode());
			if (!ObjectUtil.isEmpty(agent))
				fieldStaffStock.setAgent(agent);
		}
		String approved = productDistributionDAO.findPrefernceByName(ESESystem.ENABLE_APPROVED);
		if (approved.equalsIgnoreCase("1")) {
			map.put(QTY, productReturnDetail.getQuantity());
			/*
			 * if (distributionDetail.getStatus() == 1) {
			 * updateWarehouseProducts(fieldStaffStock, map, true); }
			 */
			fieldStaffStock.setBatchNo(productReturnDetail.getBatchNo());
			updateWarehouseProduct(fieldStaffStock, map, true);
		} else {
			fieldStaffStock.setBatchNo(productReturnDetail.getBatchNo());
			updateWarehouseProduct(fieldStaffStock, map, true);
		}
	}

	private void updateWarehouseProduct(WarehouseProduct warehouseProduct, Map<String, Object> map, boolean flag) {
		DecimalFormat df = new DecimalFormat("0.0000");

		if (!ObjectUtil.isEmpty(warehouseProduct)) {
			WarehouseProductDetail warehouseProductDetail = new WarehouseProductDetail();
			warehouseProductDetail.setDate((Date) map.get(DATE));
			warehouseProductDetail.setWarehouseProduct(warehouseProduct);
			warehouseProductDetail.setDesc((String) map.get(DESC));
			warehouseProductDetail.setPrevStock(Double.valueOf(df.format(warehouseProduct.getStock())));
			warehouseProductDetail.setTxnStock((Double) map.get(QTY));
			warehouseProductDetail.setOrderNo(
					!StringUtil.isEmpty(warehouseProduct.getOrderNo()) ? warehouseProduct.getOrderNo() : "");
			warehouseProductDetail.setUserName(
					!StringUtil.isEmpty(warehouseProduct.getUserName()) ? warehouseProduct.getUserName() : "");
			warehouseProductDetail.setVendorId(
					!StringUtil.isEmpty(warehouseProduct.getVendorId()) ? warehouseProduct.getVendorId() : "");
			warehouseProductDetail.setDamagedQty(Double.valueOf(warehouseProduct.getDamagedQty()));
			warehouseProductDetail.setCostPrice(Double.valueOf(warehouseProduct.getCostPrice()));

			if (flag) {

				warehouseProductDetail
						.setFinalStock(warehouseProductDetail.getPrevStock() - warehouseProductDetail.getTxnStock());

			} else {

				double finalstock = warehouseProductDetail.getPrevStock() + warehouseProductDetail.getTxnStock();
				warehouseProductDetail.setFinalStock(Double.valueOf(df.format(finalstock)));

			}
			warehouseProductDetail.setReceiptNo((String) map.get(RECEIPT_NO));
			/** SAVING WAREHOUSE PRODUCT DETAIL **/
			productDistributionDAO.save(warehouseProductDetail);
			/** UPDATING WAREHOUSE PRODUCT **/

			if (map.get(DESC).equals(ProductReturn.PRODUCT_RETURN_FROM_FIELDSTAFF_DESCRIPTION)) {
				/*if(map.get(COSTPRICE).toString()!=null){
				warehouseProduct.setCostPrice(Double.valueOf(map.get(COSTPRICE).toString()));
				}*/
				warehouseProduct.setSeasonCode(map.get(SEASON_CODE).toString());
			}
			warehouseProduct.setStock(warehouseProductDetail.getFinalStock());
			warehouseProduct.setRevisionNo(DateUtil.getRevisionNumber());

			productDistributionDAO.saveOrUpdate(warehouseProduct);
		}

	}

	public ProductReturnDetail findProductReturnDetailById(Long id) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findProductReturnDetailById(id);
	}

	@Override
	public void updateProductReturnDetail(ProductReturnDetail existingProductReturnDetail) {
		productDistributionDAO.update(existingProductReturnDetail);

	}

	public Object[] findAvailableStockByFarmer(String farmerId, long productId, String selectedSeason, String batchNo) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findAvailableStockByFarmer(farmerId, productId, selectedSeason, batchNo);

	}

	@Override
	public ProductReturn findProductReturnFarmerByRecNoAndTxnType(String receiptNumber, String distTxnType) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findProductReturnFarmerByRecNoAndTxnType(receiptNumber, distTxnType);
	}

	@Override
	public List<ProductReturnDetail> listProductReturnDetail(long id) {

		return productDistributionDAO.listProductReturnDetail(id);
	}

	@Override
	public List<ProductReturnDetail> listProductReturnDetail(long id, int startIndex, int limit) {
		return productDistributionDAO.listProductReturnDetail(id, startIndex, limit);
	}

	@Override
	public ProductReturn findProductReturnById(Long id) {

		return productDistributionDAO.findProductReturnById(id);
	}

	@Override
	public ProductReturn findProductReturnByRecNoAndTxnType(String receiptNumber, String txnType) {
		return productDistributionDAO.findProductReturnByRecNoAndTxnType(receiptNumber, txnType);
	}

	@Override
	public void saveOfflineProductReturn(OfflineProductReturn offlineProductReturn) {
		productDistributionDAO.save(offlineProductReturn);

	}

	@Override
	public ProductReturn findProductReturnByReceiptNoTxnType(String receiptNo, String txnType) {

		return productDistributionDAO.findProductReturnByReceiptNoTxnType(receiptNo, txnType);
	}

	public List<OfflineProductReturn> listOfflineProductReturn(String tenantId) {

		return productDistributionDAO.listOfflineProductReturn(tenantId);
	}

	public ProductReturn findProductReturnByReceiptNoTxnType(String receiptNo, String txnType, String tenantId) {

		return productDistributionDAO.findProductReturnByReceiptNoTxnType(receiptNo, txnType, tenantId);
	}

	public void saveProductReturnAndProductReturnDetail(ProductReturn productReturn, String tenantId) {

		/** SAVING productReturn OBJECT **/
		if (!ProductReturn.PRODUCT_RETURN_FROM_FARMER
				.equalsIgnoreCase(productReturn.getAgroTransaction().getTxnType())) {
			if (ObjectUtil.isEmpty(productReturn.getAgroTransaction().getRefAgroTransaction()))
				productReturn.getAgroTransaction()
						.setRefAgroTransaction(processAgroTransaction(productReturn.getAgroTransaction()));
			productDistributionDAO.saveAgroTransaction(productReturn.getAgroTransaction().getRefAgroTransaction(),
					tenantId);
			productDistributionDAO.saveAgroTransaction(productReturn.getAgroTransaction(), tenantId);
			// accountDAO.updateCashBal(distribution.getAgroTransaction().getAccount().getId(),distribution.getAgroTransaction().getAccount().getCashBalance(),distribution.getAgroTransaction().getAccount().getCreditBalance());
		} else {
			List<AgroTransaction> agroDistList = buildProductReturnAgroTransactionObject(productReturn, tenantId);
			// List<AgroTransaction> agroDistList =
			// buildAgroTransactionDistributionObject(distribution, tenantId);

			for (AgroTransaction agro : agroDistList) {

				productDistributionDAO.saveAgroTransaction(agro, tenantId);
			}

		}
		productDistributionDAO.saveProductReturn(productReturn, tenantId);
		processWarehouseProduct(productReturn, tenantId);

	}

	private List<AgroTransaction> buildProductReturnAgroTransactionObject(ProductReturn productReturn,
			String tenantId) {

		List<AgroTransaction> agroTransactionList = null;
		if (!ObjectUtil.isEmpty(productReturn)) {
			agroTransactionList = new ArrayList<AgroTransaction>();

			if (!StringUtil.isEmpty(productReturn.getFarmerId())) {// Checking
																	// Reg
																	// Former
																	// or
																	// Not
				AgroTransaction finalBalAgroObject = new AgroTransaction();
				finalBalAgroObject = formNewAgroTransaction(finalBalAgroObject, productReturn.getAgroTransaction());
				// distribution.getAgroTransaction();
				finalBalAgroObject.setIntBalance(productReturn.getAgroTransaction().getBalAmount());
				// .setTxnAmount(productReturn.getTxnAmount());
				finalBalAgroObject
						.setBalAmount(productReturn.getAgroTransaction().getBalAmount() + productReturn.getTxnAmount());
				finalBalAgroObject.setProfType("02");
				ESEAccount farmerAccount = productDistributionDAO.findESEAccountByProfileId(productReturn.getFarmerId(),
						ESEAccount.CONTRACT_ACCOUNT, tenantId);
				if (!ObjectUtil.isEmpty(farmerAccount)) {
					// farmerAccount.setCashBalance(finalBalAgroObject.getBalAmount());
					double creditAmt = farmerAccount.getCreditBalance() + productReturn.getTotalAmount();
					farmerAccount.setCreditBalance(creditAmt);
					productDistributionDAO.updateESEAccount(farmerAccount, tenantId);
				}
				finalBalAgroObject.setTxnDesc(ProductReturn.PRODUCT_RETURN_PAYMENT_AMOUNT);
				finalBalAgroObject.setTxnType(productReturn.getAgroTransaction().getTxnType());
				// finalBalAgroObject.setProductReturn(productReturn);
				// agroTransactionList.add(productReturn.getAgroTransaction());
				agroTransactionList.add(finalBalAgroObject);
				// AgentORWarehouseAccount forming
				if (!StringUtil.isEmpty(productReturn.getServicePointId())) {
					ESEAccount servicePointAccount = productDistributionDAO.findESEAccountByProfileId(
							productReturn.getAgentId(), ESEAccount.COMPANY_ACCOUNT_TYPE, tenantId);
					if (!ObjectUtil.isEmpty(servicePointAccount)) {
						AgroTransaction warehouseAgroTransactiont = new AgroTransaction();
						warehouseAgroTransactiont = formNewAgroTransaction(warehouseAgroTransactiont,
								productReturn.getAgroTransaction());
						warehouseAgroTransactiont.setIntBalance(servicePointAccount.getCashBalance());
						warehouseAgroTransactiont.setTxnAmount(productReturn.getTxnAmount());
						warehouseAgroTransactiont
								.setBalAmount(servicePointAccount.getCashBalance() + productReturn.getTxnAmount());
						warehouseAgroTransactiont.setAccount(servicePointAccount);
						warehouseAgroTransactiont.setProfType("03");// Warehouse
																	// Account
						agroTransactionList.add(warehouseAgroTransactiont);
					}
				}

			} else if (!StringUtil.isEmpty(productReturn.getServicePointId())) {
				ESEAccount servicePointAccount = productDistributionDAO.findESEAccountByProfileId(
						productReturn.getAgentId(), ESEAccount.COMPANY_ACCOUNT_TYPE, tenantId);
				if (!ObjectUtil.isEmpty(servicePointAccount)) {
					productReturn.getAgroTransaction().setIntBalance(servicePointAccount.getCashBalance());
					productReturn.getAgroTransaction()
							.setBalAmount(servicePointAccount.getCashBalance() + productReturn.getTxnAmount());
					productReturn.getAgroTransaction().setAccount(servicePointAccount);
					productReturn.getAgroTransaction().setProfType("03");// Warehouse
																			// Account
					productReturn.getAgroTransaction().setTxnDesc(ProductReturn.PRODUCT_RETURN_PAYMENT_AMOUNT);
					agroTransactionList.add(productReturn.getAgroTransaction());
				}
			} else {// single
					// Rec
					// for
					// Unreg
					// Former
				ESEAccount agentAccount = productDistributionDAO.findESEAccountByProfileId(productReturn.getAgentId(),
						ESEAccount.AGENT_ACCOUNT, tenantId);
				if (!ObjectUtil.isEmpty(agentAccount)) {
					productReturn.getAgroTransaction().setIntBalance(agentAccount.getCashBalance());
					productReturn.getAgroTransaction()
							.setBalAmount(agentAccount.getCashBalance() + productReturn.getTxnAmount());
					productReturn.getAgroTransaction().setAccount(agentAccount);
					productReturn.getAgroTransaction().setTxnDesc(ProductReturn.PRODUCT_RETURN_PAYMENT_AMOUNT);
					productReturn.getAgroTransaction().setProfType("01");// AGENT_ACCOUNT
					agroTransactionList.add(productReturn.getAgroTransaction());
				}

			}

		}
		// TODO Auto-generated method stub
		return agroTransactionList;

	}

	@Override
	public void updateOfflineProductReturn(OfflineProductReturn offlineProductReturn, String tenantId) {

		productDistributionDAO.updateOfflineProductReturn(offlineProductReturn, tenantId);

	}

	public void processWarehouseProduct(Object object, String tenantId) {
		DecimalFormat df = new DecimalFormat("0.00");
		int i = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		WarehouseProduct warehouseProduct;
		if (!ObjectUtil.isEmpty(object)) {

			if (object instanceof ProductReturn) { // Distribution
				ProductReturn productReturn = (ProductReturn) object;
				String agentId = productReturn.getAgentId();
				String txnType = productReturn.getTxnType();
				map.put(DATE, productReturn.getTxnTime());
				map.put(RECEIPT_NO, productReturn.getReceiptNumber());
				map.put(SEASON_CODE, productReturn.getSeasonCode());
				// Product Distribution to Farmer
				if (ProductReturn.PRODUCT_RETURN_FROM_FARMER.equals(txnType)) {
					map.put(DESC, ProductReturn.PRODUCT_RETURN_FROM_FARMER_DESCRIPTION);
					for (ProductReturnDetail productReturnDetail : productReturn.getProductReturnDetail()) {

						if (productReturn.getProductStock()
								.equalsIgnoreCase(WarehouseProduct.StockType.WAREHOUSE_STOCK.name())) {
							Warehouse warehouse = locationDAO.findCoOperativeByCode(productReturn.getServicePointId(),
									tenantId);
							/*
							 * warehouseProduct = productDistributionDAO.
							 * findAvailableStocksBySeasonAndBatch(
							 * productReturn.getServicePointId(),
							 * productReturnDetail.getProduct().getId(),
							 * productReturn.getSeasonCode(),
							 * productReturnDetail.getBatchNo(), tenantId);
							 */
							warehouseProduct = productDistributionDAO.findAvailableStocksBySeasonAndBatch(
									String.valueOf(warehouse.getId()), productReturnDetail.getProduct().getId(),
									productReturn.getSeasonCode(), productReturnDetail.getBatchNo(), tenantId);
							// updateDistributionProduct(productReturnDetail,
							// tenantId);

						} else {

							warehouseProduct = productDistributionDAO.findAvailableStockByAgentIdProductIdBatchNoSeason(
									agentId, productReturnDetail.getProduct().getId(), productReturn.getSeasonCode(),
									productReturnDetail.getBatchNo(), tenantId);

						}
						map.put(QTY, productReturnDetail.getQuantity());
						
						if(ObjectUtil.isEmpty(warehouseProduct)){
							//warehouseProduct.setBranchId(productReturnDetail.getBranchId());
							
							warehouseProduct = new WarehouseProduct();
							warehouseProduct.setBranchId(productReturn.getBranchId());
							warehouseProduct.setSeasonCode(productReturn.getSeasonCode());
							warehouseProduct.setStock(Double.valueOf(productReturnDetail.getCurrentQuantity()));
							warehouseProduct.setDamagedStock(Double.valueOf("0.00"));
							warehouseProduct.setCostPrice(Double.valueOf("0.00"));
							warehouseProduct.setWarehouse(productReturn.getWarehouse());
							warehouseProduct.setAgent(productReturn.getAgent());
							//warehouseProduct.setWarehouse(productReturnDetail.getwa);
							warehouseProduct.setProduct(productReturnDetail.getProduct());
							warehouseProduct.setRevisionNo(DateUtil.getRevisionNumber());
							warehouseProduct.setBatchNo(productReturnDetail.getBatchNo());
							
							Set<WarehouseProductDetail> WarProductDetails = new LinkedHashSet<WarehouseProductDetail>();
							
							WarehouseProductDetail warehouseProductDetail = new WarehouseProductDetail();
							warehouseProductDetail.setDate((Date) map.get(DATE));
							warehouseProductDetail.setWarehouseProduct(warehouseProduct);
							warehouseProductDetail.setDesc((String) map.get(DESC));
							warehouseProductDetail.setPrevStock(0.0);
							warehouseProductDetail.setTxnStock((Double) map.get(QTY));
							warehouseProductDetail.setOrderNo(
									!StringUtil.isEmpty(warehouseProduct.getOrderNo()) ? warehouseProduct.getOrderNo() : "");
							warehouseProductDetail.setUserName(
									!StringUtil.isEmpty(warehouseProduct.getUserName()) ? warehouseProduct.getUserName() : "");
							warehouseProductDetail.setVendorId(
									!StringUtil.isEmpty(warehouseProduct.getVendorId()) ? warehouseProduct.getVendorId() : "");
							warehouseProductDetail.setDamagedQty(Double.valueOf(warehouseProduct.getDamagedQty()));
							warehouseProductDetail.setCostPrice(Double.valueOf(warehouseProduct.getCostPrice()));
							warehouseProductDetail
										.setFinalStock(warehouseProductDetail.getPrevStock() + warehouseProductDetail.getTxnStock());
						
							warehouseProductDetail.setReceiptNo((String) map.get(RECEIPT_NO));
							WarProductDetails.add(warehouseProductDetail);
							warehouseProduct.setWarehouseDetails(WarProductDetails);
							productDistributionDAO.save(warehouseProduct,tenantId);
							
						}else{
							warehouseProduct.setBranchId(productReturn.getBranchId());
							warehouseProduct.setSeasonCode(productReturn.getSeasonCode());
							map.put(QTY, productReturnDetail.getQuantity());

							String approved = productDistributionDAO.findPrefernceByName(ESESystem.ENABLE_APPROVED,
									tenantId);

							if (ProductReturn.PRODUCT_RETURN_FROM_FARMER.equalsIgnoreCase(productReturn.getTxnType())) {
								if (approved.equalsIgnoreCase("0")) {
									updateWarehouseProduct(warehouseProduct, map, false, tenantId);
								}
							} else if (productReturn.getFreeDistribution().equalsIgnoreCase("0")) {
								if (approved.equalsIgnoreCase("0")) {
									updateWarehouseProduct(warehouseProduct, map, false, tenantId);
								}
							} else {
								updateWarehouseProduct(warehouseProduct, map, false, tenantId);
							}
						}
						

					}
				
					PaymentLedger paymentLedger = new PaymentLedger();
					paymentLedger.setCreatedDate(productReturn.getTxnTime());
					paymentLedger.setCreatedUser(productReturn.getAgentId() + "-" + productReturn.getAgentName());
					paymentLedger.setType(ProductReturn.PRODUCT_RETURN_FROM_FARMER);
					paymentLedger.setPrevValue(productReturn.getIntBalance());
					paymentLedger.setTxnValue(productReturn.getTxnAmount());
					paymentLedger.setNewValue(productReturn.getBalAmount());

					productDistributionDAO.saveByTenantId(paymentLedger, tenantId);
				}

				// Product Distribution to Field Staff
				if (ProductReturn.PRODUCT_RETURN_FROM_FIELDSTAFF.equals(txnType)) {
					map.put(DESC, ProductReturn.PRODUCT_RETURN_FROM_FIELDSTAFF_DESCRIPTION);

					for (ProductReturnDetail productReturnDetail : productReturn.getProductReturnDetail()) {

						map.put(QTY, productReturnDetail.getQuantity());

						Warehouse warehouse = locationDAO.findCoOperativeByCode(
								productReturn.getAgroTransaction().getServicePointId(), tenantId);
						WarehouseProduct warehouseStock = productDistributionDAO
								.findCooperativeAvailableStockByCooperative(warehouse.getId(),
										productReturnDetail.getProduct().getId(), tenantId);
						String costPrice[] = productReturnDetail.getCostPriceArray().split(",");
						if (costPrice[0].length() > 1) {

							map.put(COSTPRICE, costPrice[i]);
						}
						i++;
						// distributionDetail.setExistingQuantity(warehouseProd)
						warehouseStock.setBranchId(productReturn.getBranchId());
						warehouseStock.setSeasonCode(productReturn.getSeasonCode());
						updateWarehouseProducts(warehouseStock, map, false, tenantId);

						// Add FS stock
						processFieldStaffsWarehouseProducts(
								productReturn.getAgroTransaction().getRefAgroTransaction().getAgentId(),
								productReturnDetail, warehouseStock.getWarehouse(), map, tenantId);
					}
				}

			}
			 // End of Distribution
			if (object instanceof WarehouseProduct) { // WarehouseProduct
				warehouseProduct = (WarehouseProduct) object;
				map.put(DATE, new Date());
				map.put(RECEIPT_NO, !StringUtil.isEmpty(warehouseProduct.getReceiptNumber())
						? warehouseProduct.getReceiptNumber() : "");
				map.put(QTY, warehouseProduct.getTxnQty());
				if (warehouseProduct.isEdit()) {
					map.put(DESC, "STOCK ADDED");
					updateWarehouseProducts(warehouseProduct, map, true, tenantId);
				} else {
					map.put(DESC, "STOCK DEDUCTED");
					updateWarehouseProducts(warehouseProduct, map, false, tenantId);
				}
			} // End of WarehouseProduct
		}
	}

	private void updateDistributionProduct(ProductReturnDetail productReturnDetail, String tenantId) {

		DistributionDetail distributionDetails = productDistributionDAO.findByDistributionDetails(
				productReturnDetail.getProductReturn().getFarmerId(), productReturnDetail.getProduct().getId(),
				productReturnDetail.getBatchNo(), productReturnDetail.getProductReturn().getSeasonCode(), tenantId);
		double qty = distributionDetails.getQuantity() - productReturnDetail.getQuantity();
		distributionDetails.setQuantity(qty);
		double existQty = Double.valueOf(distributionDetails.getCurrentQuantity()) + productReturnDetail.getQuantity();
		distributionDetails.setCurrentQuantity(String.valueOf(existQty));
		productDistributionDAO.saveOrUpdate(distributionDetails.getDistribution(), tenantId);
	}

	private void processFieldStaffsWarehouseProducts(String agentId, ProductReturnDetail productReturnDetail,
			Warehouse warehouse, Map<String, Object> map, String tenantId) {
		WarehouseProduct fieldStaffStock = productDistributionDAO.findAvailableStockByAgentIdProductIdBatchNoSeason(
				agentId, productReturnDetail.getProduct().getId(), productReturnDetail.getSeasonCode(),
				productReturnDetail.getBatchNo(), tenantId);
		if (ObjectUtil.isEmpty(fieldStaffStock)) {
			fieldStaffStock = new WarehouseProduct();
			Agent agent = agentDAO.findAgentByProfileId(agentId, tenantId);
			/*
			 * if (ObjectUtil.isEmpty(warehouse)) { warehouse =
			 * agent.getCooperative(); }
			 * fieldStaffStock.setWarehouse(warehouse);
			 */
			fieldStaffStock.setProduct(productReturnDetail.getProduct());
			fieldStaffStock.setBranchId(warehouse.getBranchId());
			fieldStaffStock.setBatchNo(productReturnDetail.getBatchNo());
			// fieldStaffStock.setSeasonCode(distributionDetail.getDistribution().getSeasonCode());
			if (!ObjectUtil.isEmpty(agent))
				fieldStaffStock.setAgent(agent);
		}
		String approved = productDistributionDAO.findPrefernceByName(ESESystem.ENABLE_APPROVED, tenantId);
		if (approved.equalsIgnoreCase("1")) {
			map.put(QTY, productReturnDetail.getQuantity());
			/*
			 * if (distributionDetail.getStatus() == 1) {
			 * updateWarehouseProducts(fieldStaffStock, map, true); }
			 */
			fieldStaffStock.setBatchNo(productReturnDetail.getBatchNo());

			updateWarehouseProduct(fieldStaffStock, map, false, tenantId);
		} else {
			fieldStaffStock.setBatchNo(productReturnDetail.getBatchNo());
			updateWarehouseProduct(fieldStaffStock, map, true, tenantId);
		}
	}

	private void updateWarehouseProduct(WarehouseProduct warehouseProduct, Map<String, Object> map, boolean flag,
			String tenantId) {
		DecimalFormat df = new DecimalFormat("0.0000");

		if (!ObjectUtil.isEmpty(warehouseProduct)) {
			WarehouseProductDetail warehouseProductDetail = new WarehouseProductDetail();
			warehouseProductDetail.setDate((Date) map.get(DATE));
			warehouseProductDetail.setWarehouseProduct(warehouseProduct);
			warehouseProductDetail.setDesc((String) map.get(DESC));
			warehouseProductDetail.setPrevStock(Double.valueOf(df.format(warehouseProduct.getStock())));
			warehouseProductDetail.setTxnStock((Double) map.get(QTY));
			warehouseProductDetail.setOrderNo(
					!StringUtil.isEmpty(warehouseProduct.getOrderNo()) ? warehouseProduct.getOrderNo() : "");
			warehouseProductDetail.setUserName(
					!StringUtil.isEmpty(warehouseProduct.getUserName()) ? warehouseProduct.getUserName() : "");
			warehouseProductDetail.setVendorId(
					!StringUtil.isEmpty(warehouseProduct.getVendorId()) ? warehouseProduct.getVendorId() : "");
			warehouseProductDetail.setDamagedQty(Double.valueOf(warehouseProduct.getDamagedQty()));
			warehouseProductDetail.setCostPrice(Double.valueOf(warehouseProduct.getCostPrice()));

			if (flag) {
				warehouseProductDetail
						.setFinalStock(warehouseProductDetail.getPrevStock() - warehouseProductDetail.getTxnStock());
			} else {

				double finalstock = warehouseProductDetail.getPrevStock() + warehouseProductDetail.getTxnStock();
				warehouseProductDetail.setFinalStock(Double.valueOf(df.format(finalstock)));

			}
			warehouseProductDetail.setReceiptNo((String) map.get(RECEIPT_NO));
			/** SAVING WAREHOUSE PRODUCT DETAIL **/
			//productDistributionDAO.save(warehouseProductDetail);
			/** UPDATING WAREHOUSE PRODUCT **/

			if (map.get(DESC).equals(ProductReturn.PRODUCT_RETURN_FROM_FIELDSTAFF_DESCRIPTION)) {
				warehouseProduct.setCostPrice(Double.valueOf(map.get(COSTPRICE).toString()));
				warehouseProduct.setSeasonCode(map.get(SEASON_CODE).toString());
			}
			warehouseProduct.setStock(warehouseProductDetail.getFinalStock());
			warehouseProduct.setRevisionNo(DateUtil.getRevisionNumber());
			warehouseProduct.getWarehouseDetails().add(warehouseProductDetail);
			
			productDistributionDAO.saveOrUpdate(warehouseProduct, tenantId);
		}

	}

	public boolean isVarietyExists(String variety) {

		WarehouseProduct warehouseProduct = productDistributionDAO.isVarietyExists(variety);
		boolean flag = false;
		if (!ObjectUtil.isEmpty(warehouseProduct))
			flag = true;

		return flag;
	}

	@Override
	public List<WarehousePaymentDetails> listOfWarehousePaymentDetailsByWarehousePaymentId(long id) {
		return productDistributionDAO.listOfWarehousePaymentDetailsByWarehousePaymentId(id);
	}

	@Override
	public List<Object> findWarehouseProductDetailsVarityByProduct(long productId, long coOperativeId,
			String seasonCode) {
		return productDistributionDAO.findWarehouseProductDetailsVarityByProduct(productId, coOperativeId, seasonCode);
	}

	@Override
	public WarehousePaymentDetails findWarehousePaymentDetailsByProduct(long productId) {
		return productDistributionDAO.findWarehousePaymentDetailsByProduct(productId);
	}

	@Override
	public List<WarehouseProduct> listProductsByWarehouseAndSeason(long coOperativeId, String seasonCode) {
		return productDistributionDAO.listProductsByWarehouseAndSeason(coOperativeId, seasonCode);
	}

	@Override
	public WarehouseProductDetail findwarehouseProductDetailById(long id) {
		return productDistributionDAO.findwarehouseProductDetailById(id);
	}

	@Override
	public WarehouseProductDetail findwarehouseProductDetailByWarehouseProductId(long productID) {
		return productDistributionDAO.findwarehouseProductDetailByWarehouseProductId(productID);
	}

	@Override
	public ProcurementGrade findProcurementGradeByNameAndVarietyId(String name, long id) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findProcurementGradeByNameAndVarietyId(name, id);
	}

	@Override
	public ProcurementVariety findProcurementVariertyByNameAndCropId(String varietyName, long procurementProductId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findProcurementVariertyByNameAndCropId(varietyName, procurementProductId);
	}

	@Override
	public List<ProcurementProduct> listProcurementProductBasedOnCropCat() {
		return productDistributionDAO.listProcurementProductBasedOnCropCat();
	}

	@Override
	public List<ProcurementProduct> listProcurementProductBasedOnInterCropCat(String cropCategory) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listProcurementProductBasedOnInterCropCat(cropCategory);
	}

	@Override
	public List<PMTDetail> listPMTDetailByRecNoAndProduct(Long productId, String receiptNo) {
		return productDistributionDAO.listPMTDetailByRecNoAndProduct(productId, receiptNo);
	}

	@Override
	public List<PMTDetail> listPMTDetailByProductIdAndReceiptNo(Long warehouseId, String receiptNoId) {
		return productDistributionDAO.listPMTDetailByProductIdAndReceiptNo(warehouseId, receiptNoId);
	}

	@Override
	public ProcurementVariety findProcurementVariertyByNameAndProductId(String name, long id) {
		return productDistributionDAO.findProcurementVariertyByNameAndProductId(name, id);
	}

	@Override
	public List<Distribution> findDistributionByFarmerId(String farmerId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findDistributionByFarmerId(farmerId);
	}

	@Override
	public List<DistributionDetail> findDistributionDetailByDistributionId(long id) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findDistributionDetailByDistributionId(id);
	}

	@Override
	public List<Object[]> findDistributionCount() {
		// TODO Auto-generated method stub
		return productDistributionDAO.findDistributionCount();
	}

	@Override
	public List<Object[]> findProcurementCount() {
		// TODO Auto-generated method stub
		return productDistributionDAO.findProcurementCount();
	}

	@Override
	public List<Object[]> findDistributionAndProcurmentCountByFarmers() {
		// TODO Auto-generated method stub
		return productDistributionDAO.findDistributionAndProcurmentCountByFarmers();
	}

	@Override
	public double findWarehouseAvaliableStock(String warehouseId, long productId, String selectedSeason,
			String batchNo) {

		return productDistributionDAO.findWarehouseAvaliableStock(warehouseId, productId, selectedSeason, batchNo);
	}

	@Override
	public ProcurementVariety findProcurementVariertyByProductAndVarietyName(String product, String variety) {
		return productDistributionDAO.findProcurementVariertyByProductAndVarietyName(product, variety);
	}

	@Override
	public List<OfflineSupplierProcurement> listPendingOfflineSupplierProcurementList(String tenantId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listPendingOfflineSupplierProcurementList(tenantId);
	}

	@Override
	public SupplierProcurement findSupplierProcurementByRecNoAndOperType(String receiptNo, int operationType,
			String tenantId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findSupplierProcurementByRecNoAndOperType(receiptNo, operationType, tenantId);
	}

	public void addSupplierProcurement(SupplierProcurement supplierProcurement, String tenantId) {

		processTransaction(supplierProcurement, tenantId);

		supplierProcurement.getAgroTransaction().setTxnDesc(Procurement.PROCUREMENT_AMOUNT);

		Double bal;
		ESEAccount farmerAccount = accountDAO
				.findAccountByProfileId(supplierProcurement.getAgroTransaction().getFarmerId(), tenantId);

		if (!ObjectUtil.isEmpty(farmerAccount)) {
			bal = farmerAccount.getCashBalance();
			supplierProcurement.getAgroTransaction().setBalAmount(bal - supplierProcurement.getPaymentAmount());
			supplierProcurement.getAgroTransaction().setIntBalance(bal);
			supplierProcurement.getAgroTransaction().setTxnAmount(supplierProcurement.getPaymentAmount());
			supplierProcurement.getAgroTransaction().setAccount(farmerAccount);
			farmerAccount.setCashBalance(bal - supplierProcurement.getPaymentAmount());
			productDistributionDAO.updateESEAccount(farmerAccount, tenantId);
		}
		productDistributionDAO.saveSupplierProcurement(supplierProcurement, tenantId);
		PaymentLedger ledger = new PaymentLedger();
		ledger.setCreatedDate(supplierProcurement.getAgroTransaction().getTxnTime());
		ledger.setCreatedUser(supplierProcurement.getAgroTransaction().getAgentName());
		ledger.setType(Procurement.TXN_TYPE);
		ledger.setRefId(String.valueOf(supplierProcurement.getId()));
		ledger.setPrevValue(supplierProcurement.getAgroTransaction().getIntBalance());
		ledger.setTxnValue(supplierProcurement.getAgroTransaction().getTxnAmount());
		ledger.setNewValue(supplierProcurement.getAgroTransaction().getBalAmount());
		ledgerService.save(ledger);
		processCityWarehouse(supplierProcurement, tenantId);
	}

	public void updateOfflineSupplierProcurement(OfflineSupplierProcurement offlineSupplierProcurement,
			String tenantId) {

		productDistributionDAO.updateOfflineSupplierProcurement(offlineSupplierProcurement, tenantId);
	}

	public void addOfflineSupplierProcurementAndOfflineSupplierProcurementDetail(
			OfflineSupplierProcurement offlineSupplierProcurement) {

		productDistributionDAO.save(offlineSupplierProcurement);

	}

	@Override
	public SupplierProcurement findSupplierProcurementById(Long id) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findSupplierProcurementById(id);
	}

	public void addSupplierProcurement(SupplierProcurement supplierProcurement) {
		ESEAccount intialBal = productDistributionDAO.findESEAccountByProfileId(
				supplierProcurement.getAgroTransaction().getFarmerId(), ESEAccount.CONTRACT_ACCOUNT);
		Double initBal = 0.0;
		if (!ObjectUtil.isEmpty(intialBal)) {
			initBal = intialBal.getCashBalance();
		}

		AgroTransaction existingAgroTxn = supplierProcurement.getAgroTransaction();
		processTransaction(supplierProcurement);
		// productDistributionDAO.save(procurement.getAgroTransaction());
		supplierProcurement.setAgroTransaction(existingAgroTxn);
		supplierProcurement.getAgroTransaction().setTxnDesc(SupplierProcurement.PROCUREMENT_AMOUNT);
		Double bal;
		ESEAccount farmerAccount = productDistributionDAO.findESEAccountByProfileId(
				supplierProcurement.getAgroTransaction().getFarmerId(), ESEAccount.CONTRACT_ACCOUNT);
		if (!ObjectUtil.isEmpty(farmerAccount)) {
			bal = farmerAccount.getCashBalance();
			supplierProcurement.getAgroTransaction()
					.setBalAmount(bal + supplierProcurement.getTotalProVal() - supplierProcurement.getPaymentAmount());
			supplierProcurement.getAgroTransaction().setIntBalance(bal + supplierProcurement.getTotalProVal());
			supplierProcurement.getAgroTransaction().setTxnAmount(supplierProcurement.getPaymentAmount());
			supplierProcurement.getAgroTransaction().setAccount(farmerAccount);
			supplierProcurement.getAgroTransaction()
					.setCreditAmt(supplierProcurement.getTotalProVal() - supplierProcurement.getPaymentAmount());
			supplierProcurement.getAgroTransaction().setSupplierProcurement(supplierProcurement);
			supplierProcurement.getAgroTransaction().setIntialBalance(initBal);
			supplierProcurement.getAgroTransaction().setSeasonCode(supplierProcurement.getSeasonCode());
			farmerAccount.setCashBalance(
					bal + supplierProcurement.getTotalProVal() - supplierProcurement.getPaymentAmount());
			productDistributionDAO.update(farmerAccount);
		}

		productDistributionDAO.save(supplierProcurement);
		PaymentLedger ledger = new PaymentLedger();
		ledger.setCreatedDate(supplierProcurement.getAgroTransaction().getTxnTime());
		ledger.setCreatedUser(supplierProcurement.getAgroTransaction().getAgentName());
		ledger.setType(SupplierProcurement.TXN_TYPE);
		ledger.setRefId(String.valueOf(supplierProcurement.getId()));
		ledger.setPrevValue(supplierProcurement.getAgroTransaction().getIntBalance());
		ledger.setTxnValue(supplierProcurement.getAgroTransaction().getTxnAmount());
		ledger.setNewValue(supplierProcurement.getAgroTransaction().getBalAmount());
		ledgerService.save(ledger);
		processCityWarehouse(supplierProcurement);
	}

	@Override
	public SupplierProcurementDetail findSupplierProcurementDetailById(Long id) {
		return productDistributionDAO.findSupplierProcurementDetailById(id);
	}

	@Override
	public List<CityWarehouse> listProcurementStockByAgentIdRevisionNo(String agentId, Long revNo) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listProcurementStockByAgentIdRevisionNo(agentId, revNo);
	}

	@Override
	public List<SupplierProcurement> listSupplierProcurement() {
		return productDistributionDAO.listSupplierProcurement();
	}

	@Override
	public List<SupplierProcurementDetail> listSupplierProcurementProducts() {
		return productDistributionDAO.listSupplierProcurementProducts();
	}

	@Override
	public List<SupplierProcurementDetail> listSupplierProcurementVariety() {
		return productDistributionDAO.listSupplierProcurementVariety();
	}

	@Override
	public List<SupplierProcurementDetail> listSupplierProcurementDetails() {
		return productDistributionDAO.listSupplierProcurementDetails();
	}

	@Override
	public void addOfflineTraceabtyProcurementAndDetail(OfflineProcurementTraceability offlineProcurementTraceabty) {
		// TODO Auto-generated method stub
		productDistributionDAO.save(offlineProcurementTraceabty);
	}

	@Override
	public void addProcurementTracebty(ProcurementTraceability procurement, String tenantId) {

		processTransaction(procurement, tenantId);

		AgroTransaction existingAgroTxn = procurement.getAgroTransaction();
		existingAgroTxn = procurement.getAgroTransaction();
		procurement.setAgroTransaction(existingAgroTxn);
		procurement.getAgroTransaction().setTxnDesc(Procurement.PROCUREMENT_AMOUNT);

		Double bal;
		ESEAccount farmerAccount = accountDAO.findAccountByProfileId(procurement.getAgroTransaction().getFarmerId(),
				tenantId);
		if (!ObjectUtil.isEmpty(farmerAccount)) {
			bal = farmerAccount.getCashBalance();
			procurement.getAgroTransaction().setBalAmount(bal - procurement.getPaymentAmount());
			procurement.getAgroTransaction().setIntBalance(bal);
			procurement.getAgroTransaction().setTxnAmount(procurement.getPaymentAmount());
			procurement.getAgroTransaction().setAccount(farmerAccount);
			farmerAccount.setCashBalance(bal - procurement.getPaymentAmount());
			productDistributionDAO.updateESEAccount(farmerAccount, tenantId);
		}

		if (procurement.getPaymentAmount() <= 0) {
			procurement.setAgroTransaction(null);
		}

		productDistributionDAO.saveProcurementTraceability(procurement, tenantId);
		PaymentLedger ledger = new PaymentLedger();
		ledger.setCreatedDate(existingAgroTxn.getTxnTime());
		ledger.setCreatedUser(existingAgroTxn.getAgentName());
		ledger.setType(Procurement.TXN_TYPE);
		ledger.setRefId(String.valueOf(procurement.getId()));
		ledger.setPrevValue(existingAgroTxn.getIntBalance());
		ledger.setTxnValue(existingAgroTxn.getTxnAmount());
		ledger.setNewValue(existingAgroTxn.getBalAmount());
		ledgerService.save(ledger);
		processCityWarehouseTraceabilty(procurement, existingAgroTxn, tenantId);
		processProcurementStock(procurement, existingAgroTxn, tenantId);
	}

	public void processCityWarehouseTraceabilty(ProcurementTraceability object, AgroTransaction existingAgroTxn,
			String tenantId) {
		CityWarehouse cityWarehouse = null;
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		if (!tenantId.isEmpty()) {

			ProcurementTraceability procurement = (ProcurementTraceability) object;
			map.put("DATE", new Date());
			map.put("TYPE", CityWarehouseDetail.TYPE.PROCUREMENT.ordinal());
			map.put("REFERENCE_ID", procurement.getId());
			map.put("DESCRIPTION", CityWarehouseDetail.TYPE.PROCUREMENT.toString());

			Agent agent = agentDAO.findAgentByProfileId(existingAgroTxn.getAgentId(), tenantId);
			if (!ObjectUtil.isEmpty(agent)) {
				for (ProcurementTraceabilityDetails procurementDetail : procurement
						.getProcurmentTraceabilityDetails()) {
					map.put("TXN_NO_OF_BAGS", procurementDetail.getNumberOfBags());
					map.put("TXN_GROSS_WEIGHT", procurementDetail.getNetWeight());
					if (existingAgroTxn.getDeviceId().equalsIgnoreCase(AgroTransaction.NOT_APPLICABLE)) {
						cityWarehouse = productDistributionDAO.findCityWarehouseByCoOperative(
								existingAgroTxn.getWarehouse().getId(),
								procurementDetail.getProcurementProduct().getId(), existingAgroTxn.getAgentId(),
								procurementDetail.getProcuremntGrade().getCode(), tenantId);
					} else {
						cityWarehouse = productDistributionDAO.findCityWarehouseByVillage(
								procurement.getFarmer().getVillage().getId(),
								procurementDetail.getProcurementProduct().getId(), existingAgroTxn.getAgentId(),
								procurementDetail.getProcuremntGrade().getCode(), tenantId);
					}

					if (ObjectUtil.isEmpty(cityWarehouse)) {
						cityWarehouse = new CityWarehouse();
						cityWarehouse.setProcurementProduct(procurementDetail.getProcurementProduct());
						cityWarehouse.setAgentId(existingAgroTxn.getAgentId());
						cityWarehouse.setQuality(procurementDetail.getProcuremntGrade().getCode());
						cityWarehouse.setIsDelete(CityWarehouse.NOT_DELETED);
						cityWarehouse.setQuality(procurementDetail.getProcuremntGrade().getCode());
						cityWarehouse.setBranchId(procurement.getBranchId());
						if (existingAgroTxn.getDeviceId().equalsIgnoreCase(AgroTransaction.NOT_APPLICABLE)) {
							cityWarehouse.setCoOperative(existingAgroTxn.getWarehouse());
						} else {
							cityWarehouse.setVillage(procurement.getFarmer().getVillage());
						}
					}

					processCityWarehouseDetail(cityWarehouse, map, true, tenantId);
				}
			}

		} else {
			if (object instanceof ProcurementTraceability) {
				ProcurementTraceability procurement = (ProcurementTraceability) object;
				map.put("DATE", new Date());
				map.put("TYPE", CityWarehouseDetail.TYPE.PROCUREMENT.ordinal());
				map.put("REFERENCE_ID", procurement.getId());
				map.put("DESCRIPTION", CityWarehouseDetail.TYPE.PROCUREMENT.toString());

				{
					for (ProcurementTraceabilityDetails procurementDetail : procurement
							.getProcurmentTraceabilityDetails()) {
						map.put("TXN_NO_OF_BAGS", procurementDetail.getNumberOfBags());
						map.put("TXN_GROSS_WEIGHT", procurementDetail.getNetWeight());
						if (existingAgroTxn.getDeviceId().equalsIgnoreCase(AgroTransaction.NOT_APPLICABLE)) {

							if (!ObjectUtil.isEmpty(procurementDetail.getProcuremntGrade())) {
								cityWarehouse = productDistributionDAO.findCityWarehouseByCoOperative(
										existingAgroTxn.getWarehouse().getId(),
										procurementDetail.getProcurementProduct().getId(), existingAgroTxn.getAgentId(),
										procurementDetail.getProcuremntGrade().getCode());
							} else {
								cityWarehouse = productDistributionDAO.findCityWarehouseByCoOperative(
										existingAgroTxn.getWarehouse().getId(),
										procurementDetail.getProcurementProduct().getId(), existingAgroTxn.getAgentId(),
										procurementDetail.getProcurementProduct().getCode());
							}
						} else {
							cityWarehouse = productDistributionDAO.findCityWarehouseByVillage(
									procurement.getFarmer().getVillage().getId(),
									procurementDetail.getProcurementProduct().getId(), existingAgroTxn.getAgentId(),
									procurementDetail.getProcuremntGrade().getCode());
						}

						if (ObjectUtil.isEmpty(cityWarehouse)) {
							cityWarehouse = new CityWarehouse();
							cityWarehouse.setProcurementProduct(procurementDetail.getProcurementProduct());
							cityWarehouse.setAgentId(existingAgroTxn.getAgentId());

							if (!ObjectUtil.isEmpty(procurementDetail.getProcuremntGrade())) {
								cityWarehouse.setQuality(procurementDetail.getProcuremntGrade().getCode());
							} else {
								cityWarehouse.setQuality(procurementDetail.getProcurementProduct().getCode());
							}
							cityWarehouse.setIsDelete(CityWarehouse.NOT_DELETED);

							if (existingAgroTxn.getDeviceId().equalsIgnoreCase(AgroTransaction.NOT_APPLICABLE)) {
								cityWarehouse.setCoOperative(existingAgroTxn.getWarehouse());
							} else {
								cityWarehouse.setVillage(procurement.getFarmer().getVillage());
							}
						}
						cityWarehouse.setBranchId(procurement.getBranchId());
						cityWarehouse.setBatchNo(null);
						cityWarehouse.setFarmer(
								!ObjectUtil.isEmpty(procurement.getFarmer()) ? procurement.getFarmer() : null);
						processCityWarehouseDetail(cityWarehouse, map, true);
					}
				}
			}
		}
	}

	private void processProcurementStock(ProcurementTraceability Object, AgroTransaction existingAgroTxn,
			String tenantId) {
		// TODO Auto-generated method stub
		ProcurementTraceabilityStock traceabilityStock = null;
		String tenant = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID) ? ISecurityFilter.DEFAULT_TENANT_ID : "";
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if (!ObjectUtil.isEmpty(Object)) {
			if (Object instanceof ProcurementTraceability) {
				ProcurementTraceability procurement = (ProcurementTraceability) Object;
				map.put("DATE", procurement.getProcurementDate());
				map.put("TYPE", ProcurementTraceabilityStockDetails.TYPE.PROCUREMENT_TRACEABILITY.ordinal());
				map.put("REFERENCE_ID", procurement.getId());
				map.put("DESCRIPTION", ProcurementTraceabilityStockDetails.TYPE.PROCUREMENT_TRACEABILITY.toString());
				map.put("FARMER", procurement.getFarmer().getFarmerId());
				map.put("BRANCH_ID", procurement.getBranchId());
				{
					for (ProcurementTraceabilityDetails procurementDetail : procurement
							.getProcurmentTraceabilityDetails()) {
						map.put("TXN_NO_OF_BAGS", procurementDetail.getNumberOfBags());
						map.put("TXN_GROSS_WEIGHT", procurementDetail.getNetWeight());

						traceabilityStock = productDistributionDAO.findProcurementStockByIcs(
								procurement.getFarmer().getIcsName(), procurementDetail.getProcurementProduct().getId(),
								procurementDetail.getProcuremntGrade().getCode(), procurement.getWarehouse().getId(),
								tenantId,procurement.getSeason());

						if (ObjectUtil.isEmpty(traceabilityStock)) {
							traceabilityStock = new ProcurementTraceabilityStock();
							traceabilityStock.setProcurementProduct(procurementDetail.getProcurementProduct());
							traceabilityStock.setAgentId(existingAgroTxn.getAgentId());
							traceabilityStock.setGrade(procurementDetail.getProcuremntGrade().getCode());
							traceabilityStock.setCoOperative(procurement.getWarehouse());
							traceabilityStock.setVillage(procurement.getFarmer().getVillage());
							traceabilityStock.setIcs(!ObjectUtil.isEmpty(procurement.getFarmer())
									? procurement.getFarmer().getIcsName() : null);
							traceabilityStock.setVillage(!ObjectUtil.isEmpty(procurement.getFarmer())
									&& !ObjectUtil.isEmpty(procurement.getFarmer().getVillage())
											? procurement.getFarmer().getVillage() : null);
							traceabilityStock.setCity(!ObjectUtil.isEmpty(procurement.getFarmer())
									&& !ObjectUtil.isEmpty(procurement.getFarmer().getCity())
											? procurement.getFarmer().getCity() : null);
							traceabilityStock.setSeason(procurement.getSeason());
							traceabilityStock.setBranchId(procurement.getBranchId());
							processProcurementStockDetail(traceabilityStock, map, true, tenantId);
						} else {
							ProcurementTraceabilityStockDetails ptsDetail = productDistributionDAO
									.findProcurementStockDetailByIdAndFarmer(traceabilityStock.getId(),
											procurement.getFarmer().getId(),tenantId);
					
							if (!ObjectUtil.isEmpty(ptsDetail)) {
								ptsDetail.setPreviousstock(ptsDetail.getTotalstock());
								ptsDetail.setTxnstock((Double) map.get("TXN_GROSS_WEIGHT"));
								ptsDetail.setTotalstock(ptsDetail.getPreviousstock() + ptsDetail.getTxnstock());
								ptsDetail.setProcurementTraceabilityStock(traceabilityStock);
								ptsDetail.setPreviousNumberOfBags(ptsDetail.getTotalNumberOfBags());
								ptsDetail.setTxnNumberOfBags((Long) map.get("TXN_NO_OF_BAGS"));
								ptsDetail.setTotalNumberOfBags(
										ptsDetail.getPreviousNumberOfBags() + ptsDetail.getTxnNumberOfBags());
								productDistributionDAO.saveOrUpdate(ptsDetail, tenantId);
								traceabilityStock.setTotalStock(ptsDetail.getTotalstock());
								traceabilityStock.setNumberOfBags(ptsDetail.getTotalNumberOfBags());

								productDistributionDAO.saveOrUpdate(traceabilityStock, tenantId);

							} else {
								ProcurementTraceabilityStockDetails traceabilityStockDetail = new ProcurementTraceabilityStockDetails();
								traceabilityStockDetail.setProcurementTraceabilityStock(traceabilityStock);
								traceabilityStockDetail.setDate((Date) map.get("DATE"));
								traceabilityStockDetail.setReferenceId((Long) map.get("REFERENCE_ID"));
								traceabilityStockDetail.setPreviousNumberOfBags(0);
								traceabilityStockDetail.setPreviousstock(0);
								Farmer frmr = farmerDAO.findFarmerByFarmerId(String.valueOf(map.get("FARMER")),
										tenantId);
								traceabilityStockDetail.setFarmer(!ObjectUtil.isEmpty(frmr) ? frmr : null);
								traceabilityStockDetail.setTxnNumberOfBags((Long) map.get("TXN_NO_OF_BAGS"));
								traceabilityStockDetail.setTxnstock((Double) map.get("TXN_GROSS_WEIGHT"));
								traceabilityStockDetail.setDescription((String) map.get("DESCRIPTION"));
								traceabilityStockDetail.setBranchId((String) map.get("BRANCH_ID"));

								traceabilityStockDetail
										.setTotalNumberOfBags(traceabilityStockDetail.getPreviousNumberOfBags()
												+ traceabilityStockDetail.getTxnNumberOfBags());
								traceabilityStockDetail.setTotalstock(traceabilityStockDetail.getPreviousstock()
										+ traceabilityStockDetail.getTxnstock());

								traceabilityStock.setNumberOfBags(traceabilityStock.getNumberOfBags()
										+ traceabilityStockDetail.getTxnNumberOfBags());
								traceabilityStock.setTotalStock(
										traceabilityStock.getTotalStock() + traceabilityStockDetail.getTxnstock());
								traceabilityStock.setRevNo(DateUtil.getRevisionNumber());
								productDistributionDAO.saveOrUpdate(traceabilityStockDetail, tenantId);
								productDistributionDAO.saveOrUpdate(traceabilityStock, tenantId);
							}
						}
					}
				}
			}
		}

	}

	@Override
	public void updateOfflineProcurementTrace(OfflineProcurementTraceability offlineProcurementTrace, String tenantId) {
		// TODO Auto-generated method stub
		productDistributionDAO.updateOfflineProcurementTrace(offlineProcurementTrace, tenantId);
	}

	@Override
	public List<OfflineProcurementTraceability> listPendingOfflineProcurementTraceList(String tenantId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listPendingOfflineProcurementTraceList(tenantId);
	}

	@Override
	public ProcurementTraceability findProcurementTraceabtyByRecNo(String receiptNo) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findProcurementTraceabtyByRecNo(receiptNo);
	}

	@Override
	public void addProcurementTraceability(ProcurementTraceability procurement) {
		ESEAccount intialBal = productDistributionDAO
				.findESEAccountByProfileId(procurement.getAgroTransaction().getFarmerId(), ESEAccount.CONTRACT_ACCOUNT);
		Double initBal = 0.0;
		if (!ObjectUtil.isEmpty(intialBal)) {
			initBal = intialBal.getCashBalance();
		}

		AgroTransaction existingAgroTxn = procurement.getAgroTransaction();
		processTransaction(procurement);
		procurement.setAgroTransaction(existingAgroTxn);
		procurement.getAgroTransaction().setTxnDesc(Procurement.PROCUREMENT_AMOUNT);
		Double bal;
		ESEAccount farmerAccount = productDistributionDAO
				.findESEAccountByProfileId(procurement.getAgroTransaction().getFarmerId(), ESEAccount.CONTRACT_ACCOUNT);
		if (!ObjectUtil.isEmpty(farmerAccount)) {
			bal = farmerAccount.getCashBalance();
			procurement.getAgroTransaction()
					.setBalAmount(bal + procurement.getTotalProVal() - procurement.getPaymentAmount());
			procurement.getAgroTransaction().setIntBalance(bal + procurement.getTotalProVal());
			procurement.getAgroTransaction().setTxnAmount(procurement.getPaymentAmount());
			procurement.getAgroTransaction().setAccount(farmerAccount);
			procurement.getAgroTransaction()
					.setCreditAmt(procurement.getTotalProVal() - procurement.getPaymentAmount());
			procurement.getAgroTransaction().setIntialBalance(initBal);
			procurement.getAgroTransaction().setSeasonCode(procurement.getSeason());
			procurement.getAgroTransaction().setProcurementTraceability(null);
			farmerAccount.setCashBalance(bal + procurement.getTotalProVal() - procurement.getPaymentAmount());
			productDistributionDAO.update(farmerAccount);
		}

		existingAgroTxn = procurement.getAgroTransaction();
		if (procurement.getPaymentAmount() <= 0) {
			procurement.setAgroTransaction(null);
		}

		productDistributionDAO.save(procurement);

		PaymentLedger ledger = new PaymentLedger();
		ledger.setCreatedDate(existingAgroTxn.getTxnTime());
		ledger.setCreatedUser(existingAgroTxn.getAgentName());
		ledger.setType(Procurement.TXN_TYPE);
		ledger.setRefId(String.valueOf(procurement.getId()));
		ledger.setPrevValue(existingAgroTxn.getIntBalance());
		ledger.setTxnValue(existingAgroTxn.getTxnAmount());
		ledger.setNewValue(existingAgroTxn.getBalAmount());
		ledgerService.save(ledger);
		processCityWarehouseTraceabilty(procurement, existingAgroTxn);
		processProcurementStock(procurement, existingAgroTxn);

	}

	private void processProcurementStock(ProcurementTraceability Object, AgroTransaction existingAgroTxn) {
		ProcurementTraceabilityStock traceabilityStock = null;
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if (!ObjectUtil.isEmpty(Object)) {
			if (Object instanceof ProcurementTraceability) {
				ProcurementTraceability procurement = (ProcurementTraceability) Object;
				map.put("DATE", procurement.getProcurementDate());
				map.put("TYPE", ProcurementTraceabilityStockDetails.TYPE.PROCUREMENT_TRACEABILITY.ordinal());
				map.put("REFERENCE_ID", procurement.getId());
				map.put("DESCRIPTION", ProcurementTraceabilityStockDetails.TYPE.PROCUREMENT_TRACEABILITY.toString());
				map.put("FARMER", procurement.getFarmer().getFarmerId());
				map.put("BRANCH_ID", procurement.getBranchId());

				{
					for (ProcurementTraceabilityDetails procurementDetail : procurement
							.getProcurmentTraceabilityDetails()) {
						map.put("TXN_NO_OF_BAGS", procurementDetail.getNumberOfBags());
						map.put("TXN_GROSS_WEIGHT", procurementDetail.getNetWeight());

						traceabilityStock = productDistributionDAO.findProcurementStockByIcs(
								procurement.getFarmer().getIcsName(), procurementDetail.getProcurementProduct().getId(),
								procurementDetail.getProcuremntGrade().getCode(), procurement.getWarehouse().getId(),procurement.getSeason());

						if (ObjectUtil.isEmpty(traceabilityStock)) {
							traceabilityStock = new ProcurementTraceabilityStock();
							traceabilityStock.setSeason(procurement.getSeason());
							traceabilityStock.setProcurementProduct(procurementDetail.getProcurementProduct());
							traceabilityStock.setAgentId(existingAgroTxn.getAgentId());
							traceabilityStock.setGrade(procurementDetail.getProcuremntGrade().getCode());
							traceabilityStock.setCoOperative(existingAgroTxn.getWarehouse());
							traceabilityStock.setVillage(procurement.getFarmer().getVillage());
							// traceabilityStock.setReceiptNo(procurement.getReceiptNo());
							traceabilityStock.setIcs(!ObjectUtil.isEmpty(procurement.getFarmer())
									? procurement.getFarmer().getIcsName() : null);
							traceabilityStock.setVillage(!ObjectUtil.isEmpty(procurement.getFarmer())
									&& !ObjectUtil.isEmpty(procurement.getFarmer().getVillage())
											? procurement.getFarmer().getVillage() : null);
							traceabilityStock.setCity(!ObjectUtil.isEmpty(procurement.getFarmer())
									&& !ObjectUtil.isEmpty(procurement.getFarmer().getCity())
											? procurement.getFarmer().getCity() : null);
							traceabilityStock.setBranchId(procurement.getBranchId());
							processProcurementStockDetail(traceabilityStock, map, true);
						} else {
							ProcurementTraceabilityStockDetails ptsDetail = productDistributionDAO
									.findProcurementStockDetailByIdAndFarmer(traceabilityStock.getId(),
											procurement.getFarmer().getId());
							if (!ObjectUtil.isEmpty(ptsDetail)) {
								ptsDetail.setPreviousstock(ptsDetail.getTotalstock());
								ptsDetail.setTxnstock((Double) map.get("TXN_GROSS_WEIGHT"));
								ptsDetail.setTotalstock(ptsDetail.getPreviousstock() + ptsDetail.getTxnstock());

								ptsDetail.setPreviousNumberOfBags(ptsDetail.getTotalNumberOfBags());
								ptsDetail.setTxnNumberOfBags((Long) map.get("TXN_NO_OF_BAGS"));
								ptsDetail.setTotalNumberOfBags(
										ptsDetail.getPreviousNumberOfBags() + ptsDetail.getTxnNumberOfBags());
								traceabilityStock.setTotalStock(ptsDetail.getTotalstock());
								traceabilityStock.setNumberOfBags(ptsDetail.getTotalNumberOfBags());
								productDistributionDAO.update(traceabilityStock);
								productDistributionDAO.update(ptsDetail);
							} else {

								ProcurementTraceabilityStockDetails traceabilityStockDetail = new ProcurementTraceabilityStockDetails();
								traceabilityStockDetail.setProcurementTraceabilityStock(traceabilityStock);
								traceabilityStockDetail.setDate((Date) map.get("DATE"));
								traceabilityStockDetail.setReferenceId((Long) map.get("REFERENCE_ID"));
								traceabilityStockDetail.setPreviousNumberOfBags(0);
								traceabilityStockDetail.setPreviousstock(0);
								Farmer frmr = farmerDAO.findFarmerByFarmerId(String.valueOf(map.get("FARMER")));
								traceabilityStockDetail.setFarmer(!ObjectUtil.isEmpty(frmr) ? frmr : null);
								traceabilityStockDetail.setTxnNumberOfBags((Long) map.get("TXN_NO_OF_BAGS"));
								traceabilityStockDetail.setTxnstock((Double) map.get("TXN_GROSS_WEIGHT"));
								traceabilityStockDetail.setDescription((String) map.get("DESCRIPTION"));
								traceabilityStockDetail.setBranchId((String) map.get("BRANCH_ID"));

								traceabilityStockDetail
										.setTotalNumberOfBags(traceabilityStockDetail.getPreviousNumberOfBags()
												+ traceabilityStockDetail.getTxnNumberOfBags());
								traceabilityStockDetail.setTotalstock(traceabilityStockDetail.getPreviousstock()
										+ traceabilityStockDetail.getTxnstock());

								traceabilityStock.setNumberOfBags(traceabilityStock.getNumberOfBags()
										+ traceabilityStockDetail.getTxnNumberOfBags());
								traceabilityStock.setTotalStock(
										traceabilityStock.getTotalStock() + traceabilityStockDetail.getTxnstock());
								traceabilityStock.setRevNo(DateUtil.getRevisionNumber());
								productDistributionDAO.save(traceabilityStockDetail);
								productDistributionDAO.save(traceabilityStock);

							}
						}
					}
				}
			}
		}

	}

	private void processProcurementStockDetail(ProcurementTraceabilityStock traceabilityStock, Map<String, Object> map,
			boolean flag) {
		ProcurementTraceabilityStockDetails traceabilityStockDetail = new ProcurementTraceabilityStockDetails();
		traceabilityStockDetail.setProcurementTraceabilityStock(traceabilityStock);
		traceabilityStockDetail.setDate((Date) map.get("DATE"));
		traceabilityStockDetail.setReferenceId((Long) map.get("REFERENCE_ID"));
		traceabilityStockDetail.setPreviousNumberOfBags(traceabilityStock.getNumberOfBags());
		traceabilityStockDetail.setPreviousstock(traceabilityStock.getTotalStock());
		Farmer frmr = farmerDAO.findFarmerByFarmerId(String.valueOf(map.get("FARMER")));
		traceabilityStockDetail.setFarmer(!ObjectUtil.isEmpty(frmr) ? frmr : null);
		traceabilityStockDetail.setTxnNumberOfBags((Long) map.get("TXN_NO_OF_BAGS"));
		traceabilityStockDetail.setTxnstock((Double) map.get("TXN_GROSS_WEIGHT"));
		traceabilityStockDetail.setDescription((String) map.get("DESCRIPTION"));
		traceabilityStockDetail.setBranchId((String) map.get("BRANCH_ID"));

		traceabilityStockDetail.setTotalNumberOfBags(
				traceabilityStockDetail.getPreviousNumberOfBags() + traceabilityStockDetail.getTxnNumberOfBags());
		traceabilityStockDetail
				.setTotalstock(traceabilityStockDetail.getPreviousstock() + traceabilityStockDetail.getTxnstock());

		traceabilityStock.setNumberOfBags(traceabilityStockDetail.getTotalNumberOfBags());
		traceabilityStock.setTotalStock(traceabilityStockDetail.getTotalstock());
		traceabilityStock.setRevNo(DateUtil.getRevisionNumber());
		productDistributionDAO.save(traceabilityStock);
		productDistributionDAO.save(traceabilityStockDetail);

	}

	private void processProcurementStockDetail(ProcurementTraceabilityStock traceabilityStock, Map<String, Object> map,
			boolean flag, String tenantId) {
		ProcurementTraceabilityStockDetails traceabilityStockDetail = new ProcurementTraceabilityStockDetails();
		traceabilityStockDetail.setProcurementTraceabilityStock(traceabilityStock);
		traceabilityStockDetail.setDate((Date) map.get("DATE"));
		traceabilityStockDetail.setReferenceId((Long) map.get("REFERENCE_ID"));
		traceabilityStockDetail.setPreviousNumberOfBags(traceabilityStock.getNumberOfBags());
		traceabilityStockDetail.setPreviousstock(traceabilityStock.getTotalStock());
		Farmer frmr = farmerDAO.findFarmerByFarmerId(String.valueOf(map.get("FARMER")), tenantId);
		traceabilityStockDetail.setFarmer(!ObjectUtil.isEmpty(frmr) ? frmr : null);
		traceabilityStockDetail.setTxnNumberOfBags((Long) map.get("TXN_NO_OF_BAGS"));
		traceabilityStockDetail.setTxnstock((Double) map.get("TXN_GROSS_WEIGHT"));
		traceabilityStockDetail.setDescription((String) map.get("DESCRIPTION"));
		traceabilityStockDetail.setBranchId((String) map.get("BRANCH_ID"));

		traceabilityStockDetail.setTotalNumberOfBags(
				traceabilityStockDetail.getPreviousNumberOfBags() + traceabilityStockDetail.getTxnNumberOfBags());
		traceabilityStockDetail
				.setTotalstock(traceabilityStockDetail.getPreviousstock() + traceabilityStockDetail.getTxnstock());

		traceabilityStock.setNumberOfBags(traceabilityStockDetail.getTotalNumberOfBags());
		traceabilityStock.setTotalStock(traceabilityStockDetail.getTotalstock());
		traceabilityStock.setRevNo(DateUtil.getRevisionNumber());
		productDistributionDAO.save(traceabilityStock, tenantId);
		productDistributionDAO.save(traceabilityStockDetail, tenantId);

	}

	@Override
	public ProcurementProduct findProcurementProductPrice(Long selectedCrop) {
		return productDistributionDAO.findProcurementProductPrice(selectedCrop);
	}

	@Override
	public ProcurementTraceability findProcurementTraceabtyByRecNoAndOperType(String receiptNo, int onLine,
			String tenantId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findProcurementTraceabtyByRecNoAndOperType(receiptNo, onLine, tenantId);
	}

	@Override
	public List<Object[]> listProcurementTraceabilityDetailsByVillageAndProcurementCenter(
			ProcurementTraceabilityStockDetails procurementStockDetails, Map criteriaMap) {
		return productDistributionDAO
				.listProcurementTraceabilityDetailsByVillageAndProcurementCenter(procurementStockDetails, criteriaMap);
	}

	public List<Object[]> listProductTransferReceiptNumber() {

		return productDistributionDAO.listProductTransferReceiptNumber();
	}

	@Override
	public List<PMTDetail> listPMTDetailByProductIdAndReceiptNo(String receiptNoId) {
		return productDistributionDAO.listPMTDetailByProductIdAndReceiptNo(receiptNoId);
	}

	@Override
	public List<Object[]> listWarehouseByPMTReceiptNo(String receiptNoId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listWarehouseByPMTReceiptNo(receiptNoId);
	}
	ProcurementTraceabilityStock procurementTraceabilityStocks = null;
	@SuppressWarnings("null")
	@Override
	public void processPMTFarmerDetail(Object object) {
		HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
		String tenant = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID) ? ISecurityFilter.DEFAULT_TENANT_ID : "";
		if (!ObjectUtil.isEmpty(request)) {
			tenant = !StringUtil.isEmpty((String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID))
					? (String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID) : "";
		}
		procurementTraceabilityStocks = null;
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof PMT) {
				PMT proceurementMT = (PMT) object;
				map.put("REFERENCE_ID", proceurementMT.getId());
				if (proceurementMT.getStatusCode() == PMT.Status.MTNT.ordinal()) {// for
					// MTNT
					map.put("DATE", new Date());
					map.put("TYPE", ProcurementTraceabilityStockDetails.TYPE.TRANSFER_TRACEABILITY.ordinal());
					map.put("DESCRIPTION", ProcurementTraceabilityStockDetails.TYPE.TRANSFER_TRACEABILITY.toString());
					Agent agent = proceurementMT.getAgentRef();
					if (ObjectUtil.isEmpty(agent)) {
						for (PMTDetail pmtDetail : proceurementMT.getPmtDetails()) {
							map.put("TXN_NO_OF_BAGS", pmtDetail.getMtntNumberOfBags());
							map.put("TXN_STOCK", pmtDetail.getMtntGrossWeight());

 							procurementTraceabilityStocks = productDistributionDAO
									.findProcurementTraceabilityStockByCoOperative(
											proceurementMT.getCoOperative().getId(),
											pmtDetail.getProcurementProduct().getId(),
											pmtDetail.getProcurementGrade().getCode(), pmtDetail.getIcs(),proceurementMT.getSeasonCode());

							if (!ObjectUtil.isEmpty(procurementTraceabilityStocks)) {
								processProcurementTraceabilityStockDetail(procurementTraceabilityStocks,
										pmtDetail.getFarmerId(), proceurementMT.getBranchId(), map, false);
							}

						}/*proceurementMT.getPmtFarmerDetais().stream().forEach(pmtFarmerDetail->{
							if (!ObjectUtil.isEmpty(procurementTraceabilityStocks)) {
								processProcurementTraceabilityStockDetail(procurementTraceabilityStocks,
										pmtFarmerDetail.getFarmer(), proceurementMT.getBranchId(), map, false);
							}
						});*/

					}
				} else if (proceurementMT.getStatusCode() == PMT.Status.MTNR.ordinal()
						|| proceurementMT.getStatusCode() == PMT.Status.COMPLETE.ordinal()) {// for

					map.put("DATE", new Date());
					map.put("TYPE", ProcurementTraceabilityStockDetails.TYPE.RECEPTION_TRACEABILITY.ordinal());
					map.put("DESCRIPTION", ProcurementTraceabilityStockDetails.TYPE.RECEPTION_TRACEABILITY.toString());
					if (!ObjectUtil.isEmpty(proceurementMT.getMtnrTransferInfo())
							&& StringUtil.isEmpty(proceurementMT.getMtnrTransferInfo().getAgentId())) {
						for (PMTDetail pmtDetail : proceurementMT.getPmtDetails()) {

							map.put("TXN_NO_OF_BAGS", pmtDetail.getMtnrNumberOfBags());
							map.put("TXN_STOCK", pmtDetail.getMtnrGrossWeight());

							procurementTraceabilityStocks = productDistributionDAO
									.findProcurementTraceabilityStockByCoOperative(
											proceurementMT.getCoOperative().getId(),
											pmtDetail.getProcurementProduct().getId(),
											pmtDetail.getProcurementGrade().getCode(), pmtDetail.getIcs(),proceurementMT.getSeasonCode());

							if (ObjectUtil.isEmpty(procurementTraceabilityStocks)) {
								procurementTraceabilityStocks = new ProcurementTraceabilityStock();
								procurementTraceabilityStocks.setCity(null);
								procurementTraceabilityStocks.setCoOperative(proceurementMT.getCoOperative());
								procurementTraceabilityStocks.setVillage(null);
								procurementTraceabilityStocks.setProcurementProduct(pmtDetail.getProcurementProduct());
								procurementTraceabilityStocks.setGrade(pmtDetail.getProcurementGrade().getCode());
								procurementTraceabilityStocks.setIcs(pmtDetail.getIcs());
								procurementTraceabilityStocks.setBranchId(pmtDetail.getBranchId());
								procurementTraceabilityStocks.setSeason(proceurementMT.getSeasonCode());

							}
							/*proceurementMT.getPmtFarmerDetais().stream().forEach(pmtFarmerDetail->{
								if (!ObjectUtil.isEmpty(procurementTraceabilityStocks)) {
									processProcurementTraceabilityStockDetail(procurementTraceabilityStocks,
											pmtFarmerDetail.getFarmer(), proceurementMT.getBranchId(), map, false);
								}
							});*/
							
							if (!ObjectUtil.isEmpty(procurementTraceabilityStocks)) {
								processProcurementTraceabilityStockDetail(procurementTraceabilityStocks,pmtDetail.getFarmerId(),proceurementMT.getBranchId(), map, true);
							}
						}
					}

				}
			}

		}
	}

	private void processProcurementTraceabilityStockDetail(ProcurementTraceabilityStock procurementTraceabilityStock,
			String farmerId, String branchId, Map<String, Object> map, boolean flag) {
		ProcurementTraceabilityStockDetails procurementTraceabilityStockDetails = new ProcurementTraceabilityStockDetails();
		if (!ObjectUtil.isEmpty(farmerId)) {
			procurementTraceabilityStockDetails = productDistributionDAO.findProcurementStockDetailByIdAndFarmer(
					procurementTraceabilityStock.getId(), Long.valueOf(farmerId));
		}
		procurementTraceabilityStockDetails.setProcurementTraceabilityStock(procurementTraceabilityStock);
		procurementTraceabilityStockDetails.setDate((Date) map.get("DATE"));
		procurementTraceabilityStockDetails.setReferenceId((Long) map.get("REFERENCE_ID"));
		procurementTraceabilityStockDetails
				.setPreviousNumberOfBags(procurementTraceabilityStockDetails.getTotalNumberOfBags());
		procurementTraceabilityStockDetails.setPreviousstock(procurementTraceabilityStockDetails.getTotalstock());
		procurementTraceabilityStockDetails.setTxnNumberOfBags((Long) map.get("TXN_NO_OF_BAGS"));
		procurementTraceabilityStockDetails.setTxnstock((Double) map.get("TXN_STOCK"));

		procurementTraceabilityStockDetails.setDescription((String) map.get("DESCRIPTION"));
		if (flag) { // Adding of procurement stock
			procurementTraceabilityStockDetails
					.setTotalNumberOfBags(procurementTraceabilityStockDetails.getPreviousNumberOfBags()
							+ procurementTraceabilityStockDetails.getTxnNumberOfBags());
			procurementTraceabilityStockDetails.setTotalstock(procurementTraceabilityStockDetails.getPreviousstock()
					+( procurementTraceabilityStockDetails.getTxnstock()));
			productDistributionDAO.saveOrUpdate(procurementTraceabilityStockDetails);
			procurementTraceabilityStock.setNumberOfBags(procurementTraceabilityStock.getNumberOfBags()
					+ procurementTraceabilityStockDetails.getTxnNumberOfBags());
			procurementTraceabilityStock.setTotalStock(
					procurementTraceabilityStock.getTotalStock() + (procurementTraceabilityStockDetails.getTxnstock()));
			procurementTraceabilityStock.setRevNo(DateUtil.getRevisionNumber());
			productDistributionDAO.saveOrUpdate(procurementTraceabilityStock);
		} else { // Detecting of procurement stock
			procurementTraceabilityStockDetails
					.setTotalNumberOfBags(procurementTraceabilityStockDetails.getPreviousNumberOfBags()
							- procurementTraceabilityStockDetails.getTxnNumberOfBags());
			procurementTraceabilityStockDetails.setTotalstock(procurementTraceabilityStockDetails.getPreviousstock()
					- (procurementTraceabilityStockDetails.getTxnstock()));
			productDistributionDAO.saveOrUpdate(procurementTraceabilityStockDetails);
			procurementTraceabilityStock.setNumberOfBags(procurementTraceabilityStock.getNumberOfBags()
					- procurementTraceabilityStockDetails.getTxnNumberOfBags());
			procurementTraceabilityStock.setTotalStock(
					procurementTraceabilityStock.getTotalStock() - (procurementTraceabilityStockDetails.getTxnstock()));
			procurementTraceabilityStock.setRevNo(DateUtil.getRevisionNumber());
			productDistributionDAO.saveOrUpdate(procurementTraceabilityStock);
		}

	}
	ProcurementTraceabilityStock procurementTraceabilityStock = null;
	@SuppressWarnings("null")
	@Override
	public void processPMTFarmerDetail(Object object, String tenantId) {
		HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
		String tenant = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID) ? ISecurityFilter.DEFAULT_TENANT_ID : "";
		if (!ObjectUtil.isEmpty(request)) {
			tenant = !StringUtil.isEmpty((String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID))
					? (String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID) : "";
		}
		procurementTraceabilityStock = null;
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof PMT) {
				PMT proceurementMT = (PMT) object;
				map.put("REFERENCE_ID", proceurementMT.getId());
				if (proceurementMT.getStatusCode() == PMT.Status.MTNT.ordinal()) {// for
					// MTNT
					map.put("DATE", new Date());
					map.put("TYPE", ProcurementTraceabilityStockDetails.TYPE.TRANSFER_TRACEABILITY.ordinal());
					map.put("DESCRIPTION", ProcurementTraceabilityStockDetails.TYPE.TRANSFER_TRACEABILITY.toString());
					Agent agent = proceurementMT.getAgentRef();
					// if (ObjectUtil.isEmpty(agent)) {
					for (PMTDetail pmtDetail : proceurementMT.getPmtDetails()) {
						map.put("TXN_NO_OF_BAGS", pmtDetail.getMtntNumberOfBags());
						map.put("TXN_STOCK", pmtDetail.getMtntGrossWeight());

						procurementTraceabilityStock = productDistributionDAO
								.findProcurementTraceabilityStockByCoOperative(proceurementMT.getCoOperative().getId(),
										pmtDetail.getProcurementProduct().getId(),
										pmtDetail.getProcurementGrade().getCode(), pmtDetail.getIcs(), tenantId,proceurementMT.getSeasonCode());
						
						processProcurementTraceabilityStockDetail(procurementTraceabilityStock,
								pmtDetail.getFarmerId(), proceurementMT.getBranchId(), map, false, tenantId);

					}
					
					/*proceurementMT.getPmtFarmerDetais().stream().forEach(pmtFarmerDetail->{
						if (!ObjectUtil.isEmpty(procurementTraceabilityStock)) {
							processProcurementTraceabilityStockDetail(procurementTraceabilityStock,
									pmtFarmerDetail.getFarmer(), proceurementMT.getBranchId(), map, false, tenantId);
						}
					});*/

					// }
				} else if (proceurementMT.getStatusCode() == PMT.Status.MTNR.ordinal()
						|| proceurementMT.getStatusCode() == PMT.Status.COMPLETE.ordinal()) {// for

					map.put("DATE", new Date());
					map.put("TYPE", ProcurementTraceabilityStockDetails.TYPE.RECEPTION_TRACEABILITY.ordinal());
					map.put("DESCRIPTION", ProcurementTraceabilityStockDetails.TYPE.RECEPTION_TRACEABILITY.toString());

					if (!ObjectUtil.isEmpty(proceurementMT.getMtnrTransferInfo())
							&& StringUtil.isEmpty(proceurementMT.getMtnrTransferInfo().getAgentId())) {
						for (PMTDetail pmtDetail : proceurementMT.getPmtDetails()) {

							map.put("TXN_NO_OF_BAGS", pmtDetail.getMtnrNumberOfBags());
							map.put("TXN_STOCK", pmtDetail.getMtnrGrossWeight());

							procurementTraceabilityStock = productDistributionDAO
									.findProcurementTraceabilityStockByCoOperative(
											proceurementMT.getCoOperative().getId(),
											pmtDetail.getProcurementProduct().getId(),
											pmtDetail.getProcurementGrade().getCode(), pmtDetail.getIcs(), tenantId,proceurementMT.getSeasonCode());

							if (ObjectUtil.isEmpty(procurementTraceabilityStock)) {
								procurementTraceabilityStock = new ProcurementTraceabilityStock();
								procurementTraceabilityStock.setCity(null);
								procurementTraceabilityStock.setCoOperative(proceurementMT.getCoOperative());
								procurementTraceabilityStock.setVillage(null);
								procurementTraceabilityStock.setProcurementProduct(pmtDetail.getProcurementProduct());
								procurementTraceabilityStock.setGrade(pmtDetail.getProcurementGrade().getCode());
								procurementTraceabilityStock.setIcs(pmtDetail.getIcs());

							}
							processProcurementTraceabilityStockDetail(procurementTraceabilityStock,
									pmtDetail.getFarmerId(), proceurementMT.getBranchId(), map, true, tenantId);
						}
					}

				}
			}

		}
	}

	private void processProcurementTraceabilityStockDetail(ProcurementTraceabilityStock procurementTraceabilityStock,
			String farmerId, String branchId, Map<String, Object> map, boolean flag, String tenantId) {
		ProcurementTraceabilityStockDetails procurementTraceabilityStockDetails = new ProcurementTraceabilityStockDetails();
		if (!ObjectUtil.isEmpty(farmerId)) {
			procurementTraceabilityStockDetails = productDistributionDAO.findProcurementStockDetailByIdAndFarmer(
					procurementTraceabilityStock.getId(), Long.valueOf(farmerId), tenantId);
		}
		
		procurementTraceabilityStockDetails.setDate((Date) map.get("DATE"));
		procurementTraceabilityStockDetails.setReferenceId((Long) map.get("REFERENCE_ID"));
		procurementTraceabilityStockDetails.setPreviousNumberOfBags(procurementTraceabilityStockDetails.getTotalNumberOfBags());
		procurementTraceabilityStockDetails.setPreviousstock(procurementTraceabilityStockDetails.getTotalstock());
		procurementTraceabilityStockDetails.setTxnNumberOfBags((Long) map.get("TXN_NO_OF_BAGS"));
		procurementTraceabilityStockDetails.setTxnstock((Double) map.get("TXN_STOCK"));
		procurementTraceabilityStockDetails.setBranchId(branchId);

		procurementTraceabilityStockDetails.setDescription((String) map.get("DESCRIPTION"));
		if (flag) { // Adding of procurement stock
			procurementTraceabilityStockDetails
					.setTotalNumberOfBags(procurementTraceabilityStockDetails.getPreviousNumberOfBags()
							+ procurementTraceabilityStockDetails.getTxnNumberOfBags());
			procurementTraceabilityStockDetails.setTotalstock(procurementTraceabilityStockDetails.getPreviousstock()
					+ procurementTraceabilityStockDetails.getTxnstock());
			productDistributionDAO.saveOrUpdate(procurementTraceabilityStockDetails, tenantId);
			procurementTraceabilityStock.setNumberOfBags(procurementTraceabilityStock.getNumberOfBags()
					+ procurementTraceabilityStockDetails.getTxnNumberOfBags());
			procurementTraceabilityStock.setTotalStock(
					procurementTraceabilityStock.getTotalStock() + procurementTraceabilityStockDetails.getTxnstock());
			procurementTraceabilityStock.setRevNo(DateUtil.getRevisionNumber());
			productDistributionDAO.saveOrUpdate(procurementTraceabilityStock, tenantId);
		} else { // Detecting of procurement stock
			procurementTraceabilityStockDetails
					.setTotalNumberOfBags(procurementTraceabilityStockDetails.getPreviousNumberOfBags()
							- procurementTraceabilityStockDetails.getTxnNumberOfBags());
			procurementTraceabilityStockDetails.setTotalstock(procurementTraceabilityStockDetails.getPreviousstock()
					- procurementTraceabilityStockDetails.getTxnstock());
		//	productDistributionDAO.saveOrUpdate(procurementTraceabilityStockDetails, tenantId);
			procurementTraceabilityStock.setNumberOfBags(procurementTraceabilityStock.getNumberOfBags()
					- procurementTraceabilityStockDetails.getTxnNumberOfBags());
			procurementTraceabilityStock.setTotalStock(
					procurementTraceabilityStock.getTotalStock() - procurementTraceabilityStockDetails.getTxnstock());
			procurementTraceabilityStock.setRevNo(DateUtil.getRevisionNumber());
			//procurementTraceabilityStock.setProcurmentTraceabilityStockDetails(procurmentTraceabilityStockDetails);
			procurementTraceabilityStockDetails.setProcurementTraceabilityStock(procurementTraceabilityStock);
			productDistributionDAO.saveOrUpdate(procurementTraceabilityStockDetails, tenantId);
		}

	}

	@Override
	public List<Object[]> listTransferProCenters() {
		return productDistributionDAO.listTransferProCenters();
	}

	@Override
	public List<Object[]> listReceiverGinning() {
		return productDistributionDAO.listReceiverGinning();
	}

	@Override
	public ProcurementTraceabilityStockDetails findProcurementTracabiltiyDetailsStockById(Long id) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findProcurementTracabiltiyDetailsStockById(id);
	}

	@Override
	public void creatHeapSpace(HeapData hd) {
		productDistributionDAO.save(hd);

	}
	
	
	public void creatHeapSpace(HeapData hd,String tenantId) {
		productDistributionDAO.save(hd,tenantId);

	}

	@Override
	public void updateHeapSpace(HeapData heapData) {
		productDistributionDAO.update(heapData);

	}

	@Override
	public List<Object[]> listSupplierProcurementDetailProperties(String ssDate, String eeDate) {
		return productDistributionDAO.listSupplierProcurementDetailProperties(ssDate,eeDate);
	}

	@Override
	public List<Object[]> listProcurementTraceabilityStockbyAgent(String agentId, long revisionNo,String season) {

		return productDistributionDAO.listProcurementTraceabilityStockbyAgent(agentId, revisionNo,season);
	}

	@Override
	public List<PMT> findTransferStockByGinner(Integer ginner,Long warhouseId,String season) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findTransferStockByGinner(ginner,warhouseId,season);
	}

	@Override
	public void saveOfflinePMTNR(OfflinePMTNR proceurementMTNR) {
		// TODO Auto-generated method stub
		productDistributionDAO.save(proceurementMTNR);
	}

	@Override
	public List listPendingOfflineMTNR(String tenantId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listPendingOfflineMTNR(tenantId);
	}

	@Override
	public PMT findPMTNRByReceiptNoAndType(String receiptNo, String trnType, String tenantId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findPMTNRByReceiptNoAndType(receiptNo, trnType, tenantId);
	}

	@Override
	public void savePMTNR(PMT pmtnr, String tenantId) {
		// TODO Auto-generated method stub
		productDistributionDAO.savePMTNR(pmtnr, tenantId);
		if(tenantId.equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)){
		processHeapData(pmtnr,tenantId);
		}
		
	}

	
	@Override
	public void updateOfflinePMTNR(OfflinePMTNR offlinePMTNR, String tenantId) {
		// TODO Auto-generated method stub
		productDistributionDAO.updateOfflinePMTNR(offlinePMTNR, tenantId);
	}

	@Override
	public ProcurementTraceabilityStock findProcurementTraceabilityStockByCoOperative(long coOperativeId,
			long productId, String quality, String icsCode, String tenantId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findProcurementTraceabilityStockByCoOperative(coOperativeId, productId, quality,
				icsCode, tenantId);
	}

	@Override
	public List listPendingOfflineTransferTraceability(String tenantId) {
		
		return productDistributionDAO.listPendingOfflineTransferTraceability(tenantId);
	}
	
	@Override
	public List<Object> listPMTImageDetailsIdByPmtId(long pmtId) {
		return productDistributionDAO.listPMTImageDetailsIdByPmtId(pmtId);
	}
	
	@Override
	public List<Object[]> findpmtdetailByPmtId(long pmtId,long pmtDetailId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findpmtdetailByPmtId(pmtId,pmtDetailId);
	}


	@Override
	public PMTImageDetails findPMTImageDetailById(Long id) {
		return productDistributionDAO.findPMTImageDetailById(id);
	}

	@Override
	public List<Object[]> findpmtdetailByPmt(Long id, String product, String icsName){
		// TODO Auto-generated method stub
		return productDistributionDAO.findpmtdetailByPmt(id,product,icsName);
	}

	@Override
	public PMT findDriverAndTransporterByReceiptNo(String receiptNoId, String tenantId) {
		return productDistributionDAO.findDriverAndTransporterByReceiptNo(receiptNoId,tenantId);
	}
	/*@SuppressWarnings("unused")
	private void processHeapData(PMT pmtnr){
		if(!ObjectUtil.isEmpty(pmtnr)){
			for(PMTDetail pmtnrDetail:pmtnr.getPmtDetails()){
				try{
				HeapData heapData=productDistributionDAO.findHeapDataByGinnerProductIcsAndHeapCode(pmtnrDetail.getPmt().getCoOperative().getId(),pmtnrDetail.getProcurementProduct().getId(),pmtnrDetail.getHeap());
				
				if(!ObjectUtil.isEmpty(heapData)){
					HeapDataDetail hdd=new HeapDataDetail();
					hdd.setDate(pmtnrDetail.getPmt().getMtnrDate());
					hdd.setPreviousStock(heapData.getTotalStock());
					hdd.setTxnStock(pmtnrDetail.getMtnrGrossWeight());
					hdd.setTotalStock(hdd.getPreviousStock()+hdd.getTxnStock());
					hdd.setPmtDetailId(pmtnrDetail.getId());
					hdd.setStockType(HeapData.stock.HEAP.ordinal());
					hdd.setDescription(ADD_HEAP);
					hdd.setFarmer(pmtnr.getFarmerId());
					heapData.setIcs(heapData.getIcs()+","+pmtnrDetail.getIcs());
					if(pmtnr.getFarmerId().contains(",")){
						for(String frmrId:pmtnr.getFarmerId().split(",")){
							heapData.setFarmer(heapData.getFarmer()!=null && !StringUtil.isEmpty(heapData.getFarmer())?!heapData.getFarmer().contains(frmrId)?heapData.getFarmer()+","+frmrId:heapData.getFarmer():frmrId);		
						}
					}
					else{
					heapData.setFarmer(heapData.getFarmer()!=null && !StringUtil.isEmpty(heapData.getFarmer())?!heapData.getFarmer().contains(pmtnrDetail.getFarmerId())?heapData.getFarmer()+","+pmtnrDetail.getFarmerId():heapData.getFarmer():pmtnrDetail.getFarmerId());
					}//hdd.setStatus(1);
					
					heapData.setTotalStock(heapData.getTotalStock()+pmtnrDetail.getMtnrGrossWeight());
					heapData.getHeapDataDetail().add(hdd);
					productDistributionDAO.update(heapData);
				}
				else{
					HeapData hd=new HeapData();
					hd.setGinning(pmtnrDetail.getPmt().getCoOperative());
					hd.setProcurementProduct(pmtnrDetail.getProcurementProduct());
					hd.setCooperative(pmtnrDetail.getCoOperative());
					hd.setIcs(pmtnrDetail.getIcs());
					hd.setFarmer(pmtnr.getFarmerId());
					hd.setTotalStock(pmtnrDetail.getMtnrGrossWeight());
					hd.setCode(pmtnrDetail.getHeap());
					hd.setCreatedDate(pmtnrDetail.getPmt().getMtnrDate());
					hd.setBranchId(pmtnr.getBranchId());
					//FarmCatalogue fm=catalogueService.findCatalogueByCode(pmtnrDetail.getHeap());
					hd.setName(catalogueService.findCatalogueByCode(pmtnrDetail.getHeap()).getName());
					HeapDataDetail hdd=new HeapDataDetail();
					hdd.setDate(pmtnrDetail.getPmt().getMtnrDate());
					hdd.setPreviousStock(0);
					hdd.setTxnStock(pmtnrDetail.getMtnrGrossWeight());
					hdd.setTotalStock(pmtnrDetail.getMtnrGrossWeight());
					hdd.setPmtDetailId(pmtnrDetail.getId());
					hdd.setStockType(HeapData.stock.HEAP.ordinal());
					hdd.setFarmer(pmtnr.getFarmerId());
					//hdd.setStatus(1);
					hdd.setDescription(ADD_HEAP);
					hdd.setHeapData(hd);
					Set <HeapDataDetail> hddSet=new LinkedHashSet<>();
					hddSet.add(hdd);
					hd.setHeapDataDetail(hddSet);
					creatHeapSpace(hd);
				}
				}catch(Exception e){}
				}
		}
	}*/

	private void processHeapData(PMT pmtnr, String tenantId) {

		if(!ObjectUtil.isEmpty(pmtnr)){
			String farmerIds="";
			for(PMTDetail pmtnrDetail:pmtnr.getPmtDetails()){
				try{
					LedgerData led=new LedgerData();
					
				HeapData heapData=productDistributionDAO.findHeapDataByGinnerProductIcsAndHeapCode(pmtnrDetail.getPmt().getCoOperative().getId(),pmtnrDetail.getProcurementProduct().getId(),pmtnrDetail.getHeap(),tenantId,pmtnr.getSeasonCode());
				//LedgerData ledger=productDistributionDAO.findLedgerByGinnerProductIcsAndHeapCode(pmtnrDetail.getCoOperative().getId(),pmtnrDetail.getProcurementProduct().getId(),pmtnrDetail.getIcs(),pmtnrDetail.getHeap(),tenantId);
				if(!ObjectUtil.isEmpty(heapData)){
					HeapDataDetail hdd=new HeapDataDetail();
					hdd.setDate(pmtnrDetail.getPmt().getMtnrDate());
					hdd.setPreviousStock(heapData.getTotalStock());
					hdd.setTxnStock(pmtnrDetail.getMtnrGrossWeight());
					hdd.setTotalStock(hdd.getPreviousStock()+hdd.getTxnStock());
					hdd.setPmtDetailId(pmtnrDetail.getId());
					hdd.setStockType(HeapData.stock.HEAP.ordinal());
					hdd.setDescription(ADD_HEAP);
					hdd.setIcs(pmtnrDetail.getIcs());
					hdd.setFarmer(pmtnrDetail.getFarmerId());
					//hdd.setStatus(1);
					heapData.setTotalStock(heapData.getTotalStock()+pmtnrDetail.getMtnrGrossWeight());
					heapData.getHeapDataDetail().add(hdd);
					if(!heapData.getIcs().contains(pmtnrDetail.getIcs()))
						heapData.setIcs(heapData.getIcs()+","+pmtnrDetail.getIcs());
					//heapData.setFarmer(heapData.getFarmer()!=null && !StringUtil.isEmpty(heapData.getFarmer())?!heapData.getFarmer().contains(pmtnrDetail.getFarmerId())?heapData.getFarmer()+","+pmtnrDetail.getFarmerId():heapData.getFarmer():pmtnrDetail.getFarmerId());
					if(pmtnr.getFarmerId().contains(",")){
						for(String frmrId:pmtnr.getFarmerId().split(",")){
							if(pmtnrDetail.getIcs().equalsIgnoreCase(frmrId.split("~")[1]) && pmtnrDetail.getProcurementProduct().getId()==Long.parseLong(frmrId.split("~")[2])){
							heapData.setFarmer(heapData.getFarmer()!=null && !StringUtil.isEmpty(heapData.getFarmer()) ?!heapData.getFarmer().contains(frmrId.split("~")[0])?heapData.getFarmer()+","+frmrId.split("~")[0]:heapData.getFarmer():frmrId.split("~")[0]);
							}
						}
					}
					else{
						if(pmtnrDetail.getIcs().equalsIgnoreCase(pmtnrDetail.getFarmerId().split("~")[1]) && pmtnrDetail.getProcurementProduct().getId()==Long.parseLong(pmtnrDetail.getFarmerId().split("~")[2])){
					heapData.setFarmer(heapData.getFarmer()!=null && !StringUtil.isEmpty(heapData.getFarmer())?!heapData.getFarmer().contains(pmtnrDetail.getFarmerId().split("~")[0])?heapData.getFarmer()+","+pmtnrDetail.getFarmerId().split("~")[0]:heapData.getFarmer():pmtnrDetail.getFarmerId().split("~")[0]);
						}
					}
					productDistributionDAO.saveOrUpdate(heapData,tenantId);
					led.setOpenStk(hdd.getPreviousStock());
					led.setInwardStk(hdd.getTxnStock());
					led.setClosStk(hdd.getTotalStock());
					led.setBranchId(pmtnr.getBranchId());
				
				}
				else{
					HeapData hd=new HeapData();
					hd.setBranchId(pmtnr.getBranchId());
					hd.setGinning(pmtnrDetail.getPmt().getCoOperative());
					hd.setProcurementProduct(pmtnrDetail.getProcurementProduct());
					hd.setCooperative(pmtnrDetail.getCoOperative());
					hd.setIcs(pmtnrDetail.getIcs());
					hd.setSeason(pmtnr.getSeasonCode());
					for(String farmr: pmtnr.getFarmerId().split(",")){
						if(pmtnrDetail.getIcs().equalsIgnoreCase(farmr.split("~")[1])&& pmtnrDetail.getProcurementProduct().getId()==Long.parseLong(farmr.split("~")[2])){
						hd.setFarmer(hd.getFarmer()!=null && !StringUtil.isEmpty(hd.getFarmer())?hd.getFarmer()+","+farmr.split("~")[0]:farmr.split("~")[0]);
						}
					}
					//hd.setFarmer(pmtnr.getFarmerId());
					hd.setTotalStock(pmtnrDetail.getMtnrGrossWeight());
					hd.setCode(pmtnrDetail.getHeap());
					hd.setCreatedDate(pmtnrDetail.getPmt().getMtnrDate());
					//FarmCatalogue fm=catalogueService.findCatalogueByCode(pmtnrDetail.getHeap());
					hd.setName(catalogueService.findCatalogueByCode(pmtnrDetail.getHeap(),tenantId).getName());
					HeapDataDetail hdd=new HeapDataDetail();
					hdd.setDate(pmtnrDetail.getPmt().getMtnrDate());
					hdd.setPreviousStock(0);
					hdd.setTxnStock(pmtnrDetail.getMtnrGrossWeight());
					hdd.setTotalStock(pmtnrDetail.getMtnrGrossWeight());
					hdd.setPmtDetailId(pmtnrDetail.getId());
					hdd.setStockType(HeapData.stock.HEAP.ordinal());
					hdd.setDescription(ADD_HEAP);
					hdd.setHeapData(hd);
					hdd.setIcs(pmtnrDetail.getIcs());
					hdd.setFarmer(hd.getFarmer());
					Set <HeapDataDetail> hddSet=new LinkedHashSet<>();
					hddSet.add(hdd);
					hd.setHeapDataDetail(hddSet);
					
					creatHeapSpace(hd,tenantId);
					led.setOpenStk(hdd.getPreviousStock());
					led.setInwardStk(hdd.getTxnStock());
					led.setClosStk(hdd.getTotalStock());
					led.setBranchId(pmtnr.getBranchId());
				
				}
				led.setSeason(pmtnr.getSeasonCode());
				led.setPmtDetail(pmtnrDetail);
				led.setDate(pmtnrDetail.getPmt().getMtnrDate());
				led.setGinning(pmtnrDetail.getPmt().getCoOperative());
				led.setProduct(pmtnrDetail.getProcurementProduct());
				led.setIcs(pmtnrDetail.getIcs());
				led.setFarmer(pmtnrDetail.getFarmerId());
				led.setHeap(pmtnrDetail.getHeap());
				led.setLedgerType(LedgerData.type.HEAP.ordinal());
				productDistributionDAO.save(led,tenantId);
				
				}catch(Exception e){}
				}
		}
	}

	public ICatalogueService getCatalogueService() {
		return catalogueService;
	}

	public void setCatalogueService(ICatalogueService catalogueService) {
		this.catalogueService = catalogueService;
	}

	@Override
	public List<HeapData> findHeapDataByGinner(Integer ginnerType, long ginnerId,String season) {
		
		return productDistributionDAO.findHeapDataByGinner(ginnerType, ginnerId,season);
	}

	@Override
	public void addOfflineGinningProcess(OfflineGinningProcess ogp) {
		productDistributionDAO.save(ogp);
	}

	@Override
	public List listPendingOfflineGinningProcess(String tenantId) {
		return productDistributionDAO.listPendingOfflineGinningProcess(tenantId);
	}

	@Override
	public void updateOfflineGinningProcess(OfflineGinningProcess offlineProcess, String tenantId) {
		productDistributionDAO.updateOfflineGinningProcess(offlineProcess, tenantId);
	}

	@Override
	public void saveGinningProcess(GinningProcess gp,String tenantId) {
		if(!ObjectUtil.isEmpty(gp)){
			
			
			HeapData hd=productDistributionDAO.findHeapDataByGinnerProductIcsAndHeapCode(gp.getGinning().getId(), gp.getProduct().getId(),  gp.getHeapCode(),tenantId,gp.getSeason());
			if(!ObjectUtil.isEmpty(hd)){
				HeapDataDetail hdd=new HeapDataDetail();
		    	hdd.setHeapData(hd);
		    	hdd.setDescription("GINNING STOCK UPDATED");
		    	hdd.setDate(new Date());
		    	hdd.setLintCotton(gp.getTotlintCotton());
		    	hdd.setSeedCotton(gp.getTotseedCotton());
		    	hdd.setScrup(gp.getTotscrap());
		    	hdd.setStockType(HeapData.stock.GINNING.ordinal());
		    	hdd.setPmtDetailId(gp.getId());
		    	hdd.setPreviousStock(hd.getTotalStock());
		    	hdd.setTxnStock(gp.getProcessQty());
		    	hdd.setTotalStock(hdd.getPreviousStock()-hdd.getTxnStock());
		    	hd.getHeapDataDetail().add(hdd);
		    	hd.setTotalStock(hd.getTotalStock()-gp.getProcessQty());
		    	hd.setTotLintCotton(hd.getTotLintCotton()+gp.getTotlintCotton());
		    	hd.setTotSeedCotton(hd.getTotSeedCotton()+gp.getTotseedCotton());
		    	hd.setTotScrup(hd.getTotScrup()+gp.getTotscrap());
		    	GinningProcess exist  =productDistributionDAO.findGinningProcessByDateHeapAndProduct(gp.getGinning().getId(),gp.getProcessDate(), gp.getHeapCode(), gp.getProduct().getId(), tenantId,gp.getSeason());
		    	if(exist!=null){
					exist.setTotseedCotton(exist.getTotseedCotton()+gp.getTotseedCotton());
					exist.setTotscrap(exist.getTotscrap()+gp.getTotscrap());
					exist.setProcessQty(exist.getProcessQty()+gp.getProcessQty());
					exist.setSeedPer((exist.getTotseedCotton()/exist.getProcessQty())*100);
					exist.setScrapPer((exist.getTotscrap()/exist.getProcessQty())*100);
				
				}
		    	else{
					exist = gp;
				}
		    	exist.setFarmer(hd.getFarmer());
		    	exist.setIcs(hd.getIcs());
		    	productDistributionDAO.saveOrUpdate(exist,tenantId);
		    	productDistributionDAO.saveOrUpdate(hd,tenantId);
		    	LedgerData led = new LedgerData();
		    	led.setOpenStk(hdd.getPreviousStock());
				led.setInwardStk(hdd.getTxnStock());
				led.setClosStk(hdd.getTotalStock());
				led.setGinning(gp.getGinning());
			led.setProduct(gp.getProduct());
			led.setIcs(hd.getIcs());
			led.setHeap(hd.getName());
			led.setLedgerType(LedgerData.type.GINNING.ordinal());
			led.setSeason(hd.getSeason());
			productDistributionDAO.save(led,tenantId);
		    	
		    	}
		}
	}

	@Override
	public void addOfflineBaleGeneration(OfflineBaleGeneration obg) {
		productDistributionDAO.save(obg);
		
	}

	@Override
	public List listPendingOfflingBaleGeneration(String tenantId) {
		return productDistributionDAO.listPendingOfflingBaleGeneration(tenantId);
	}

	@Override
	public void saveBaleGeneration(BaleGeneration bg,String tenantId) {
		productDistributionDAO.save(bg,tenantId);
		
	}

	@Override
	public void updateOfflineBaleGeneration(OfflineBaleGeneration offlineProcess, String tenantId) {
		productDistributionDAO.updateOfflineBaleGeneration(offlineProcess, tenantId);
		
	}

	@Override
	public void addOfflineSpinningTransfer(OfflineSpinningTransfer ost) {
		productDistributionDAO.save(ost);
	}

	@Override
	public List listPendingOfflineSpinningTransfer(String tenantId) {
		return productDistributionDAO.listPendingOfflineSpinningTransfer(tenantId);
	}

	@Override
	public void saveSpinningTransfer(SpinningTransfer st) {
	productDistributionDAO.save(st);
		
	}

	@Override
	public void updateOfflineSpinningTransfer(OfflineSpinningTransfer offlineProcess, String tenantId) {
		productDistributionDAO.updateOfflineSpinningTransfer(offlineProcess, tenantId);
		
	}

	@Override
	public GinningProcess findGinningProcessByDateHeapAndProduct(long ginning, String ginDate, String heap,long product,String tenantId,String season) {
		return productDistributionDAO.findGinningProcessByDateHeapAndProduct(ginning,ginDate, heap, product,tenantId,season);
	}

	@Override
	public void updateGinningProcess(GinningProcess ginningProcess,String tenantID) {
		productDistributionDAO.updateGinningProcess(ginningProcess,tenantID);
	}

	@Override
	public List<BaleGeneration> findBaleGenerationByGinningProcessId(String id) {
		return productDistributionDAO.findBaleGenerationByGinningProcessId(id);
	}

	@Override
	public List<Object[]> listOfGinningFromSpinningTransfer() {
		return productDistributionDAO.listOfGinningFromSpinningTransfer();
	}

	@Override
	public List<Object[]> listOfSpinningFromSpinningTransfer() {
		return productDistributionDAO.listOfSpinningFromSpinningTransfer();
	}

	@Override
	public List<Object[]> listOfLotNoPrNoAndTypeFromSpinningTransfer() {
		return productDistributionDAO.listOfLotNoPrNoAndTypeFromSpinningTransfer();
	}

	@Override
	public HeapData findHeapDataByHeapCode(long ginning,String heap,String tenant,String season) {
		return productDistributionDAO.findHeapDataByHeapCode(ginning,heap,tenant,season);
	}

	@Override
	public List<Object[]> listPMTDetailByProductIdReceiptNoAndICSName(String receiptNoId) {
		return productDistributionDAO.listPMTDetailByProductIdReceiptNoAndICSName(receiptNoId);
	}
	@Override
	public List<Object[]> listGinningProcessByHeapProductAndGinning(String heap, long product, long ginning,String startDate, String endDate,String season) {
		return productDistributionDAO.listGinningProcessByHeapProductAndGinning(heap, product, ginning,startDate,endDate,season);
	}

	@Override
	public void saveSpinningTransfer(SpinningTransfer st, String tenantId) {
		productDistributionDAO.save(st,tenantId);		
	}

	@Override
	public SpinningTransfer findSpinningTransferById(long id) {
		return productDistributionDAO.findSpinningTransferById(id);
	}

	@Override
	public List<BaleGeneration> findBaleGenerationByGinningId(long ginningId,int status) {
		return productDistributionDAO.findBaleGenerationByGinningId(ginningId,status);
	}

	@Override
	public void updateBaleStatusById(String selectedBales,long id) {
		productDistributionDAO.updateBaleStatusById(selectedBales,id);
		
	}

	@Override
	public List<Object> listPMTImageDetailById(Long id, List<Integer> typeList) {
		return productDistributionDAO.listPMTImageDetailById(id, typeList);
	}

	@Override
	public List<PMTFarmerDetail> findPmtFarmerDetailByPmtId(long pmtId) {
		return productDistributionDAO.findPmtFarmerDetailByPmtId(pmtId);
	}

	@Override
	public List<PMTDetail> findpmtdetailByPmtId(long pmtId) {
		return productDistributionDAO.findpmtdetailByPmtId(pmtId);
	}

	@Override
	public DistributionBalance findDistributionBalanceByFarmerAndProduct(long farmerId, long productId,String tenantId ) {
		return productDistributionDAO.findDistributionBalanceByFarmerAndProduct(farmerId,productId,tenantId);
	}

	@Override
	public void updateDistributionBalance(DistributionBalance db,String tenantId) {
		productDistributionDAO.update(db,tenantId);
		
	}

	@Override
	public List<ProcurementProduct> listProcurementProductFromFarmCrops() {
		return productDistributionDAO.listProcurementProductFromFarmCrops();
	}

	@Override
	public void editProcurement(Procurement procurement) {
		// TODO Auto-generated method stub
		productDistributionDAO.update(procurement);
	}

	@Override
	public List<PMTImageDetails> findPMTImageDetailByDistributionId(long id) {
		return productDistributionDAO.findPMTImageDetailByDistributionId(id);
	}

	@Override
	public void updateDistribution(Distribution distribution, String tenantId) {
		productDistributionDAO.updateDistribution(distribution, tenantId);
		
	}

	@Override
	public Double findAvailableStockByWarehouseIdAndProductId(long warehouseId, long productId) {
		return productDistributionDAO.findAvailableStockByWarehouseIdAndProductId(warehouseId, productId);
	}

	@Override
	public void saveDistributionStock(DistributionStock distributionStock) {
			List<AgroTransaction> agroDistList = buildDistributionStockAgroTransactionObject(distributionStock);
			for (AgroTransaction agro : agroDistList) {
				agro.setDistributionStock(distributionStock);
				productDistributionDAO.save(agro);

			}
			

		productDistributionDAO.save(distributionStock);
		processWarehouseProducts(distributionStock);
	
		
	}

	private List<AgroTransaction> buildDistributionStockAgroTransactionObject(DistributionStock distributionStock) {
		List<AgroTransaction> agroTransactionList = null;
		if (!ObjectUtil.isEmpty(distributionStock)) {
			agroTransactionList = new ArrayList<AgroTransaction>();
			for(DistributionStockDetail distDet: distributionStock.getDistributionStockDetails()){
			AgroTransaction finalBalAgroObject = new AgroTransaction();
			
			if(distributionStock.getTxnType().equalsIgnoreCase(DistributionStock.DISTRIBUTION_STOCK_TRANSFER)){
				finalBalAgroObject.setTxnDesc(DistributionStock.DISTRIBUTION_TRANSACTION);
				finalBalAgroObject.setTxnType(DistributionStock.DISTRIBUTION_STOCK_TRANSFER);
				finalBalAgroObject.setSeasonCode(distributionStock.getSeason());
				finalBalAgroObject.setBranch_id(distributionStock.getBranchId());
			}
			else if(distributionStock.getTxnType().equalsIgnoreCase(DistributionStock.DISTRIBUTION_STOCK_RECEPTION)){
				finalBalAgroObject.setTxnDesc(DistributionStock.DISTRIBUTION_RECEPTION);
				finalBalAgroObject.setTxnType(DistributionStock.DISTRIBUTION_STOCK_RECEPTION);
				finalBalAgroObject.setSeasonCode(distributionStock.getSeason());
				finalBalAgroObject.setBranch_id(distributionStock.getBranchId());
			}
		
			finalBalAgroObject.setIntBalance(0.00);
			finalBalAgroObject.setTxnAmount(0.00);
			finalBalAgroObject.setBalAmount(0.00);
			finalBalAgroObject.setQty(distDet.getDistributionQuantity());
			finalBalAgroObject.setAccount(null);
			finalBalAgroObject.setVendorId(null);
			finalBalAgroObject.setVendorName(null);
			finalBalAgroObject.setProfType(null);
			finalBalAgroObject.setOperType(1);
			finalBalAgroObject.setModeOfPayment("N/A");
			finalBalAgroObject.setDistributionStock(distributionStock);
			finalBalAgroObject.setReceiptNo(distributionStock.getReceiptNo());
			//finalBalAgroObject.setBranchId(distributionStock.getBranchId());
			finalBalAgroObject.setTxnTime(new Date());
			agroTransactionList.add(finalBalAgroObject);
			}
			
	}
		return agroTransactionList;
	}

	@Override
	public DistributionStock findDistributionStockById(long distributionStockId) {
		return productDistributionDAO.findDistributionStockById(distributionStockId);
	}

	@Override
	public List<DistributionStockDetail> listDistributionStockDetailByReceiveWarehouseIdAndReceiptNo(
			Long receiverWarehouseId, String receiptNo) {
		return productDistributionDAO.listDistributionStockDetailByReceiveWarehouseIdAndReceiptNo(receiverWarehouseId, receiptNo);
	}

	@Override
	public DistributionStock findTransferDistributionStockByReceiptNumber(String receiptNo) {
		return productDistributionDAO.findTransferDistributionStockByReceiptNumber(receiptNo);
		
	}

	@Override
	public void update(DistributionStock distributionStock) {
		productDistributionDAO.update(distributionStock);
	}

	@Override
	public DistributionStockDetail findDistributionStockDetailById(Long distDetID) {
		return productDistributionDAO.findDistributionStockDetailById(distDetID);
	}

	@Override
	public List<AgroTransaction> findAgroTxnByFarmerIdTXnTypes(String farmerId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findAgroTxnByFarmerIdTXnTypes(farmerId);
	}

	@Override
	public List<AgroTransaction> findAgroTxnByFarmerIdTXnTypesSeasonCode(String farmerId, String seasonCode,
			String[] txnTypes, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findAgroTxnByFarmerIdTXnTypesSeasonCode(farmerId,seasonCode,txnTypes,startDate,endDate);
	}
	
	public boolean isAgentDistributionExist(String profileId) {

		return productDistributionDAO.isAgentDistributionExist(profileId);
	}

	@Override
	public List<DistributionStockDetail> findDistributionStOckDetailByDistributionId(long id) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findDistributionStOckDetailByDistributionId(id);
	}

	@Override
	public void delete(Object obj) {
		// TODO Auto-generated method stub
		productDistributionDAO.delete(obj);
	}

	@Override
	public WarehouseProduct findWarehouseProductBySenderWarehouseIdAndSeasonCode(long id, String season) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findWarehouseProductBySenderWarehouseIdAndSeasonCode(id,season);
	}

	@Override
	public void deleteAgroTxnById(long id) {
		// TODO Auto-generated method stub
		productDistributionDAO.deleteAgroTxnById(id);
	}

	@Override
	public List<Object[]> listTreeDetails(long farmId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listTreeDetails(farmId);
	}

	@Override
	public String findOrganicVartyByFarm(long farmId, String orgVartyCode) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findOrganicVartyByFarm(farmId,orgVartyCode);
	}

	@Override
	public String findConvnVartyByFarm(long farmId, String conVartyCode) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findConvnVartyByFarm(farmId,conVartyCode);
	}

	@Override
	public void updateProductReturn(ProductReturn prodReturn) {
		// TODO Auto-generated method stub
		productDistributionDAO.update(prodReturn);		
	}
	
	@Override
	public void updateProductReturn(ProductReturn productReturn,String tenantId) {
		// TODO Auto-generated method stub
		productDistributionDAO.updateProductReturn(productReturn,tenantId);		
	}

	
	
	public void addColdStorageEntry(ColdStorage coldStorage) {

		productDistributionDAO.save(coldStorage);
		
		processCityWarehouse(coldStorage);
	}

	@Override
	public double findAvailableQtyByWarehouseColdStorageNameGradeBlockFloorBay(Long warehouseId, String coldStorageName, 
			String blockName, String floorName, String bayNumber, Long productId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findAvailableQtyByWarehouseColdStorageNameGradeBlockFloorBay(warehouseId, coldStorageName, blockName, floorName, bayNumber, productId);
	}

	@Override
	public List<SupplierProcurementDetail> listSupplierProcurementDetailById(Long id) {
		return productDistributionDAO.listSupplierProcurementDetailById(id);
	}

	@Override
	public CityWarehouse findCityWarehouseByWarehouseProductBatchNoGradeFarmer(long warehouseId, long productId,
			String batchNo, String grade, String coldStorageName, String blockName, String floorName, String bayNum,long farmerId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findCityWarehouseByWarehouseProductBatchNoGradeFarmer(warehouseId, productId, batchNo, grade, coldStorageName, blockName, floorName, bayNum,farmerId);
	}

	@Override
	public List<Object[]> listProductByCityWarehouseAndColdStorage(Long valueOf, String selectedColdStorageName) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listProductByCityWarehouseAndColdStorage(valueOf, selectedColdStorageName);
	}

	@Override
	public List<String> listBatchNoByCityWarehouseAndColdStorageAndProduct(Long warehouseId,
			String selectedColdStorageName, Long productId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listBatchNoByCityWarehouseAndColdStorageAndProduct(warehouseId, selectedColdStorageName, productId);
	}

	
	
	public String addColdStorageStockTransfer(ColdStorageStockTransfer coldStorageStockTransfer) {

		if (!ObjectUtil.isEmpty(coldStorageStockTransfer) && !ObjectUtil.isListEmpty(coldStorageStockTransfer.getColdStorageStockTransferDetail())) {
			if (StringUtil.isEmpty(coldStorageStockTransfer.getReceiptNo())) {
				coldStorageStockTransfer.setReceiptNo(idGenerator.getColdStorageStockTransferReceiptNoSeq());
				productDistributionDAO.save(coldStorageStockTransfer);
				processCityWarehouse(coldStorageStockTransfer);
				return coldStorageStockTransfer.getReceiptNo();
			}
			//productDistributionDAO.saveOrUpdate(pmt);
			//processCityWarehouse(pmt);

		}
		return null;
	}

	@Override
	public List<CityWarehouseDetail> listCityWareHouseDetailByWarehouseIdGradeCodeAndProductIdAndColdStorageNameAndBatchNo(
			long parseLong, long longValue, String selectedColdStorageName, String selectedBatchNo) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listCityWareHouseDetailByWarehouseIdGradeCodeAndProductIdAndColdStorageNameAndBatchNo(parseLong, longValue, selectedColdStorageName, selectedBatchNo);
	}


	@Override
	public void addCropCalendar(CropCalendar cropCalendar) {
		// TODO Auto-generated method stub
		productDistributionDAO.save(cropCalendar);
		
	}

	@Override
	public List<CropCalendarDetail> listCropCalendarDetailByProcurementVarietyId(long id,String currentSeasonCode) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listCropCalendarDetailByProcurementVarietyId(id,currentSeasonCode);
	}


	@Override
	public List<BaleGeneration> findBaleGenerationByLotNo(String lotNo,String tenantId) {
		return productDistributionDAO.findBaleGenerationByLotNo(lotNo,tenantId);
	}

	@Override
	public List<Object[]> listActivityByCalendarIdAndVarietyId(long varietyId,String seasonCode) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listActivityByCalendarIdAndVarietyId(varietyId,seasonCode);
	}

	@Override
	public void updateProcurementVarietyRevisionNo(long varietyId, long revisionNo) {
		// TODO Auto-generated method stub
		productDistributionDAO.updateProcurementVarietyRevisionNo(varietyId, revisionNo);
	}

	public void addOfflineFarmCropCalendarAndOfflineFarmCropCalendarDetail(OfflineFarmCropCalendar offlineFarmCropCalendar) {

		productDistributionDAO.save(offlineFarmCropCalendar);

	}
	
	public List<OfflineFarmCropCalendar> listPendingOfflineFarmCropCalendarList(String tenantID) {

		return productDistributionDAO.listPendingOfflineFarmCropCalendarList(tenantID);
	}

	@Override
	public void addFarmCropCalendar(FarmCropCalendar farmCropCalendar, String tenantId) {
		// TODO Auto-generated method stub
		productDistributionDAO.saveFarmCropCalendar(farmCropCalendar, tenantId);
	}

	@Override
	public void updateOfflineFarmCropCalendar(OfflineFarmCropCalendar offlineFarmCropCalendar, String tenantId) {
		// TODO Auto-generated method stub
		productDistributionDAO.updateOfflineFarmCropCalendar(offlineFarmCropCalendar, tenantId);
	}

	@Override
	public List<Object[]> listFarmCropCalendarByFarmAndSeason(long farmId, String seasonCode) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listFarmCropCalendarByFarmAndSeason(farmId, seasonCode);
	}

	@Override
	public GinningProcess findGinningByGinningId(long ginningId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findGinningByGinningId(ginningId);
	}

	@Override
	public String findIcsNameByIcsCode(String ics) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findIcsNameByIcsCode(ics);
	}

	@Override
	public List<Object[]> findFarmerNameByFarmerId(String farmer) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findFarmerNameByFarmerId(farmer);
	}

	@Override
	public HeapData findHeapDataByGinningHeapCodeAndProduct(String heapCode, long ginningId, long prodId,
			String currentTenantId,String season) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findHeapDataByGinningHeapCodeAndProduct(heapCode,ginningId, prodId,currentTenantId,season);
	}
	
	@Override
	public CityWarehouse findCityWarehouseByWarehouseProductBatchNoGrade(long warehouseId, long productId,
			String batchNo, String grade, String coldStorageName, String blockName, String floorName, String bayNum) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findCityWarehouseByWarehouseProductBatchNoGrade(warehouseId, productId, batchNo, grade, coldStorageName, blockName, floorName, bayNum);
	}

	@Override
	public ColdStorage findColdStorageByFarmerAndBatchNo(Long farmerId, String batchNo) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findColdStorageByFarmerAndBatchNo(farmerId, batchNo);
	}

	@Override
	public ColdStorage findColdStorageByBatchNo(String batchNo) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findColdStorageByBatchNo(batchNo);
	}
	
	@Override
	public List<Object[]> listWarehouseProductByWarehouseId(String warehouseId,String season) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listWarehouseProductByWarehouseId(warehouseId,season);
	}

	@Override
	public List<Object[]> listOfGinningDateByHeap(String ginningId, String heapCode,String season){
		// TODO Auto-generated method stub
		return productDistributionDAO.listOfGinningDateByHeap(ginningId,heapCode,season);
	}
	
	@Override
	public List<FarmCrops> listfarmCropsByProcurementVarietyId(Long id){
		return productDistributionDAO.listfarmCropsByProcurementVarietyId(id);
	}

	@Override
	public List<Object[]> listPMTReceiptNumberByWarehouseAndSeason(long warehouseId, String season) {
		return productDistributionDAO.listPMTReceiptNumberByWarehouseAndSeason(warehouseId,season);
	}

	@Override
	public OfflineGinningProcess findGinningProcessByMessageNo(String messageNo) {
		return productDistributionDAO.findGinningProcessByMessageNo(messageNo);
	}

	@Override
	public OfflineBaleGeneration findBaleGenerationByMessageNo(String messageNo) {
		return productDistributionDAO.findBaleGenerationByMessageNo(messageNo);
	}

	@Override
	public OfflineSpinningTransfer findSpinningTransferByMessageNo(String messageNo) {
		return productDistributionDAO.findSpinningTransferByMessageNo(messageNo);
	}

	@Override
	public OfflineMTNT findOfflineMTNTByMessageNo(String messageNo) {
		return productDistributionDAO.findOfflineMTNTByMessageNo(messageNo);
	}

	@Override
	public OfflinePMTNR findOfflinePMTNRByMessageNo(String messageNo) {
		return productDistributionDAO.findOfflinePMTNRByMessageNo(messageNo);
	}

	@Override
	public OfflineProcurementTraceability findOfflineProcurementTraceabilityByMessageNo(String messageNo) {
		return productDistributionDAO.findOfflineProcurementTraceabilityByMessageNo(messageNo);
	}

	@Override
	public List<BaleGeneration> findBaleGenerationByGinningIdAndHeap(long ginning, String heap, int status) {
		return productDistributionDAO.findBaleGenerationByGinningIdAndHeap(ginning, heap, status);
	}

	@Override
	public List<String> listOfLotNoFromBaleGeneration() {
		return productDistributionDAO.listOfLotNoFromBaleGeneration();
	}

	@Override
	public void addUnapprovedProcurement(Procurement procurement, String tenantId) {
		procurement.getAgroTransaction().setTxnDesc(Procurement.PROCUREMENT_AMOUNT);
		productDistributionDAO.saveProcurement(procurement, tenantId);
		processUnapprovedCityWarehouse(procurement, tenantId);
	
	}

	@Override
	public void approveProcurement(Procurement procurement) {
		
		ESEAccount intialBal = productDistributionDAO
				.findESEAccountByProfileId(procurement.getAgroTransaction().getFarmerId(), ESEAccount.CONTRACT_ACCOUNT);
		Double initBal = 0.0;
		if (!ObjectUtil.isEmpty(intialBal)) {
			initBal = intialBal.getCashBalance();
		}

		AgroTransaction existingAgroTxn = procurement.getAgroTransaction();
		processTransaction(procurement);
		// productDistributionDAO.save(procurement.getAgroTransaction());
		procurement.setAgroTransaction(existingAgroTxn);
		procurement.getAgroTransaction().setTxnDesc(Procurement.PROCUREMENT_AMOUNT);
		Double bal;
		ESEAccount farmerAccount = productDistributionDAO
				.findESEAccountByProfileId(procurement.getAgroTransaction().getFarmerId(), ESEAccount.CONTRACT_ACCOUNT);
		if (!ObjectUtil.isEmpty(farmerAccount)) {
			bal = farmerAccount.getCashBalance();
			procurement.getAgroTransaction()
					.setBalAmount(bal + procurement.getTotalProVal() - procurement.getPaymentAmount());
			procurement.getAgroTransaction().setIntBalance(bal + procurement.getTotalProVal());
			procurement.getAgroTransaction().setTxnAmount(procurement.getPaymentAmount());
			procurement.getAgroTransaction().setAccount(farmerAccount);
			procurement.getAgroTransaction()
					.setCreditAmt(procurement.getTotalProVal() - procurement.getPaymentAmount());
			procurement.getAgroTransaction().setProcurement(procurement);
			procurement.getAgroTransaction().setIntialBalance(initBal);
			procurement.getAgroTransaction().setSeasonCode(procurement.getSeasonCode());
			farmerAccount.setCashBalance(bal + procurement.getTotalProVal() - procurement.getPaymentAmount());
			productDistributionDAO.update(farmerAccount);
		}
		procurement.setStatus(1);
		procurement.setPaymentAmount(procurement.getPaymentAmount());
		productDistributionDAO.update(procurement);
		PaymentLedger ledger = new PaymentLedger();
		ledger.setCreatedDate(procurement.getAgroTransaction().getTxnTime());
		ledger.setCreatedUser(procurement.getAgroTransaction().getAgentName());
		ledger.setType(Procurement.TXN_TYPE);
		ledger.setRefId(String.valueOf(procurement.getId()));
		ledger.setPrevValue(procurement.getAgroTransaction().getIntBalance());
		ledger.setTxnValue(procurement.getAgroTransaction().getTxnAmount());
		ledger.setNewValue(procurement.getAgroTransaction().getBalAmount());
		ledgerService.save(ledger);
		
		checkAccBalaceAndStatus(procurement.getFarmer().getMobileNumber());
		
		
	}

	private void checkAccBalaceAndStatus(String mobileNumber) {
		HttpClient httpclient = HttpClients.createDefault();

        try
        {
            URIBuilder builder = new URIBuilder("https://ericssonbasicapi2.azure-api.net/disbursement/v1_0/accountholder/MSISDN/"+mobileNumber+"/active");

        	String subKey = productDistributionDAO.findPrefernceByName(ESESystem.MTN_SUB_PRIMARY_KEY);
            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);
            request.setHeader("Authorization", "");
            request.setHeader("X-Target-Environment", "sandbox");
            request.setHeader("Ocp-Apim-Subscription-Key", subKey);


            // Request body
            StringEntity reqEntity = new StringEntity("{body}");
            ((HttpResponse) request).setEntity(reqEntity);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) 
            {
                System.out.println(EntityUtils.toString(entity));
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
	}
	public void processUnapprovedCityWarehouse(Object object, String tenantId){


		CityWarehouse cityWarehouse = null;
		HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
		String tenant = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID) ? ISecurityFilter.DEFAULT_TENANT_ID : "";
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof Procurement) { // to process Procurement
				Procurement procurement = (Procurement) object;
				map.put("DATE", new Date());
				map.put("TYPE", CityWarehouseDetail.TYPE.PROCUREMENT.ordinal());
				map.put("REFERENCE_ID", procurement.getId());
				map.put("DESCRIPTION", CityWarehouseDetail.TYPE.PROCUREMENT.toString());
					for (ProcurementDetail procurementDetail : procurement.getProcurementDetails()) {
						map.put("TXN_NO_OF_BAGS", procurementDetail.getNumberOfBags());
						map.put("TXN_GROSS_WEIGHT", procurementDetail.getGrossWeight());

						if (ObjectUtil.isEmpty(cityWarehouse)) {
							cityWarehouse = new CityWarehouse();
							cityWarehouse.setProcurementProduct(procurementDetail.getProcurementProduct());
							cityWarehouse.setQuality(procurementDetail.getProcurementGrade().getCode());
							cityWarehouse.setIsDelete(CityWarehouse.NOT_DELETED);
							cityWarehouse.setQuality(procurementDetail.getProcurementGrade().getCode());
							cityWarehouse.setBranchId(procurement.getBranchId());
							cityWarehouse.setVillage(procurement.getVillage());
							cityWarehouse.setFarmer(procurement.getFarmer());
							if (procurement.getAgroTransaction().getDeviceId()
									.equalsIgnoreCase(AgroTransaction.NOT_APPLICABLE)) {
								cityWarehouse.setCoOperative(procurement.getAgroTransaction().getWarehouse());
							} else {
								cityWarehouse.setVillage(procurement.getVillage());
							}
						}
						processCityWarehouseDetail(cityWarehouse, map, true, tenantId);
					}
				
			}
			
			
		}
			
	}

	
	
	@Override
	public List<Object[]> listFarmerByICS(String farmerId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listFarmerByICS(farmerId);
	}

	@Override
	public List<ColdStorageStockTransferDetail> listColdStorageStockTransferByLotCode(String lotCode) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listColdStorageStockTransferByLotCode(lotCode);
	}
	
	@Override
	public LoanDistribution findLoanDistributionById(long id) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findLoanDistributionById(id);
	}

	@Override
	public List<DistributionBalance> findDistributionBalanceByFarmerAndProductIdAndVendorId(Long id, String pCode,Long vendorId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findDistributionBalanceByFarmerAndProductIdAndVendorId(id,pCode,vendorId);
	}

	@Override
	public List<SubCategory> listSubCategory() {
		// TODO Auto-generated method stub
		return productDistributionDAO.listSubCategory();
	}

	@Override
	public List<Product> listProductsBySubCategoryId(Long categoryId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listProductsBySubCategoryId(categoryId);
	}

	@Override
	public OfflineProcurement findOfflineProcurementByReceipotNo(String receipt) {
		return productDistributionDAO.findOfflineProcurementByReceipotNo(receipt);
	}


	@Override
	public List<LoanLedger> findFarmerLoanByFarmerIdInLoanLedger(String id, String tenantId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findFarmerLoanByFarmerIdInLoanLedger(id, tenantId);
	}
	
	

	@Override
	public List<Vendor> findVendorById(String vendorId) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findVendorById(vendorId);
	}


	@Override
	public List<Vendor> listPriorityVendor() {
		// TODO Auto-generated method stub
		return productDistributionDAO.listPriorityVendor();
	}

	


	@Override
	public List<AgroTransaction> findAgroTxnByVendorIdTXnTypesSeasonCode(String farmerId, String seasonCode,
			String[] txnTypes, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findAgroTxnByVendorIdTXnTypesSeasonCode(farmerId,seasonCode,txnTypes,startDate,endDate);
	}

	@Override
	public LoanDistributionDetail findLoanDistributionDetailByIdd(long id) {
		// TODO Auto-generated method stub
		return productDistributionDAO.findLoanDistributionDetailByIdd(id);
	}
	
	@Override
	public List<LoanDistributionDetail> findLoanDistributionDetailById(long pmtId) {
		return productDistributionDAO.findLoanDistributionDetailById(pmtId);
	}

	public void addLoanDistribution(LoanDistribution loanDistribution) {/*
		ESEAccount farmerAccount = productDistributionDAO
				.findESEAccountByProfileId(loanDistribution.getAgroTransaction().getFarmerId(), ESEAccount.CONTRACT_ACCOUNT);	
		
		processTransaction(loanDistribution);	
		
		loanDistribution.getAgroTransaction().setAccount(!ObjectUtil.isEmpty(farmerAccount) ? farmerAccount : null);
		Double bal;
		
		if (!ObjectUtil.isEmpty(farmerAccount)) {
			bal = farmerAccount.getCashBalance();
			productDistributionDAO.update(farmerAccount);
		}
			
		productDistributionDAO.update(loanDistribution);*/
		

		ESEAccount farmerAccount = productDistributionDAO
				.findESEAccountByProfileId(loanDistribution.getAgroTransaction().getFarmerId(), ESEAccount.CONTRACT_ACCOUNT);	
		
		processTransaction(loanDistribution);	
		
		loanDistribution.getAgroTransaction().setAccount(!ObjectUtil.isEmpty(farmerAccount) ? farmerAccount : null);
		Double bal;
		
		if (!ObjectUtil.isEmpty(farmerAccount)) {
			bal = farmerAccount.getCashBalance();
			productDistributionDAO.update(farmerAccount);
		}
			
		productDistributionDAO.save(loanDistribution);
		LoanLedger loanLedger = new LoanLedger();
		loanLedger.setTxnTime(loanDistribution.getLoanDate());
		loanLedger.setReceiptNo(loanDistribution.getAgroTransaction().getReceiptNo());
	
		
		
		loanLedger.setAccount(loanDistribution.getAgroTransaction().getAccount());
		loanLedger.setAccountNo(loanDistribution.getAgroTransaction().getAccount().getLoanAccountNo());
		loanLedger.setFarmerId(!ObjectUtil.isEmpty(loanDistribution.getFarmer()) ? loanDistribution.getFarmer().getFarmerId() : "");
		loanLedger.setVendorId(!ObjectUtil.isEmpty(loanDistribution.getVendor()) ? loanDistribution.getVendor().getVendorId() : "");
		
		loanLedger.setAccount(!ObjectUtil.isEmpty(loanDistribution.getAgroTransaction()) && !ObjectUtil.isEmpty(loanDistribution.getAgroTransaction().getAccount()) ? loanDistribution.getAgroTransaction().getAccount() : null);
		loanLedger.setAccountNo(!ObjectUtil.isEmpty(loanDistribution.getAgroTransaction().getAccount()) ? loanDistribution.getAgroTransaction().getAccount().getLoanAccountNo() : "");

		loanLedger.setActualAmount(loanDistribution.getTotalCostToFarmer());
		/*loanLedger.setFarmerLoanBal(loanDistribution.getAgroTransaction().getAccount().getLoanAmount());
		loanLedger.setFarmerOutStandingBal(loanDistribution.getAgroTransaction().getAccount().getOutstandingLoanAmount());*/
		loanLedger.setTxnType("701");
		loanLedger.setNewFarmerBal(!ObjectUtil.isEmpty(loanDistribution.getAgroTransaction().getAccount()) ? loanDistribution.getAgroTransaction().getAccount().getLoanAmount() : 0.00);
		loanLedger.setPreFarmerBal(!ObjectUtil.isEmpty(loanDistribution.getAgroTransaction().getAccount()) ? loanDistribution.getAgroTransaction().getAccount().getOutstandingLoanAmount() : 0.00);
		loanLedger.setLoanDesc(LoanDistribution.LOAN_DISTRIBUTION_PAYMENT_AMOUNT);
		loanLedger.setBranchId(loanDistribution.getBranchId());
		ledgerService.save(loanLedger);
	
	}

	@Override
	public List<CityWarehouseDetail> listCityWareHouseDetailByWarehouseIdAndProductIdAndColdStorageNameAndBatchNo(
			long warehouseId, long productId, String coldStorageName, String[] batchNo) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listCityWareHouseDetailByWarehouseIdAndProductIdAndColdStorageNameAndBatchNo(warehouseId, productId, coldStorageName, batchNo);
	}

	@Override
	public List<Object[]> listCityWareHouseDetailByWarehouseIdAndProductIdAndColdStorageNameAndBatchNo(
			long warehouseId, long productId, String coldStorageName, String batchNo) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listCityWareHouseDetailByWarehouseIdAndProductIdAndColdStorageNameAndBatchNo(warehouseId, productId, coldStorageName, batchNo);
	}
	
	public List<ProcurementProduct> listProcurementProductByBranch(String branch){
		return productDistributionDAO.listProcurementProductByBranch(branch);
	}
	
	public List<ProcurementProduct> listProcurementProductByRevisionNoByBranch(Long revisionNo,String branch){
		return productDistributionDAO.listProcurementProductByRevisionNoByBranch(revisionNo,branch);
	}

	public List<ProcurementVariety> listProcurementProductVarietyByRevisionNoByBranch(Long revisionNo,String branch){
		return productDistributionDAO.listProcurementProductVarietyByRevisionNoByBranch(revisionNo,branch);
	}

	@Override
	public List<ColdStorageStockTransferDetail> listColdStorageStockTransferByLotCodeAndColdStorageCode(String lotCode,String coldStorageCode,String warehouseCode,String chamberCode,String floorCode,String bayCode) {
		// TODO Auto-generated method stub
		return productDistributionDAO.listColdStorageStockTransferByLotCodeAndColdStorageCode(lotCode, coldStorageCode, warehouseCode, chamberCode, floorCode, bayCode);
	}
	public void saveAgroTransactionForLoanRePayment(AgroTransaction farmerPaymentTxn) {

		String description = farmerPaymentTxn.getTxnDesc();
		if (!StringUtil.isEmpty(description) && description.contains("|")) {
			String[] descriptionDetail = description.split("\\|");
			
			if (PaymentMode.LOAN_REPAYMENT_MODE_NAME.equalsIgnoreCase(descriptionDetail[0])) {
				// LOAN REPAYMENT
				farmerPaymentTxn.calculateFarmerLoanBalance(farmerPaymentTxn.getAccount(), farmerPaymentTxn.getTxnAmount());
			
			} 
			
		} 
		productDistributionDAO.save(farmerPaymentTxn);

		if (!ObjectUtil.isEmpty(farmerPaymentTxn.getAccount()) && !StringUtil.isEmpty(farmerPaymentTxn.getBalAmount())) {
			accountDAO.updateESEAccountOutStandingBalById(farmerPaymentTxn.getAccount().getId(),
					farmerPaymentTxn.getBalAmount());
		}
		LoanLedger loanLedger = new LoanLedger();
		loanLedger.setTxnTime(farmerPaymentTxn.getTxnTime());
		loanLedger.setReceiptNo(farmerPaymentTxn.getReceiptNo());
		loanLedger.setFarmerId(farmerPaymentTxn.getFarmerId());
		loanLedger.setAccount(!ObjectUtil.isEmpty(farmerPaymentTxn) && !ObjectUtil.isEmpty(farmerPaymentTxn.getAccount()) ? farmerPaymentTxn.getAccount() : null);
		loanLedger.setAccountNo(!ObjectUtil.isEmpty(farmerPaymentTxn.getAccount())?farmerPaymentTxn.getAccount().getLoanAccountNo() :"");
		loanLedger.setActualAmount(farmerPaymentTxn.getTxnAmount());
		loanLedger.setNewFarmerBal(!ObjectUtil.isEmpty(farmerPaymentTxn.getAccount())?farmerPaymentTxn.getAccount().getLoanAmount():0.00);
		loanLedger.setPreFarmerBal(farmerPaymentTxn.getBalAmount());
		loanLedger.setTxnType("702");
		loanLedger.setLoanDesc(PaymentMode.LOAN_REPAYMENT_AMOUNT);
		loanLedger.setBranchId(farmerPaymentTxn.getBranch_id());
		ledgerService.save(loanLedger);
		
	}

	@Override
	public List<String> listLotNoFromFarmerTraceabilityData() {
		// TODO Auto-generated method stub
		return productDistributionDAO.listLotNoFromFarmerTraceabilityData();
	}


}
