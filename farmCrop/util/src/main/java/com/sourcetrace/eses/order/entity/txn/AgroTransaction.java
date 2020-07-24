/*
 * AgroTransaction.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ese.entity.traceability.ProcurementTraceability;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.esesw.entity.profile.ESEAccount;

// TODO: Auto-generated Javadoc
public class AgroTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String NOT_APPLICABLE = "N/A";
    public static final String CASH_PAYMENT = "CASH PAYMENT";
    public static final String CASH_WITHDRAW = "CASH WITHDRAW";
    public static final String CASH_RECEIVED = "CASH RECEIVED";
    public static final String CASH_SETTLEMENT = "CASH SETTLEMENT";

    public static final String EMPTY_PAGE_NO = "-";

    private long id;
    private String receiptNo;
    private String agentId;
    private String agentName;
    private String deviceId;
    private String deviceName;
    private String servicePointId;
    private String servicePointName;
    private String farmerId;
    private String farmerName;
    private String vendorId;
    private String vendorName;
    private String customerId;
    private String customerName;
    private String profType;
    private String txnType;
    private String modeOfPayment;
    private double intBalance;
    private double txnAmount;
    private double balAmount;
    private int operType;
    private Date txnTime;
    private String txnDesc;
    private ESEAccount account;
    private Warehouse samithi;
    private byte[] audioFile;
    private String productStock;
    // private String stockType;
    private String warehouseCode;
    private Distribution distribution;
    private Procurement procurement;
    private Double debitAmt;
    private String seasonCode ;
    private Double qty;
    private Double intialBalance;
    private Double creditAmt;
    private String branch_id;
    private ProductReturn productReturn;
    private ProcurementTraceability procurementTraceability;
    private DistributionStock distributionStock;
private String latitude;
private String longitude;

    public String getLatitude() {
	return latitude;
}

public void setLatitude(String latitude) {
	this.latitude = latitude;
}

public String getLongitude() {
	return longitude;
}

public void setLongitude(String longitude) {
	this.longitude = longitude;
}

	// transient variable
    private String transientId;
    private int balanceType;
    private AgroTransaction refAgroTransaction;
    private Warehouse warehouse;
    private long eseAccountId;
    private List<String> agentList;
    private List<String> txnTypeList;
    private String branchId;
    private Double balanceAmt;
    private String item;
    private String finalBalance;
    private List<DistributionDetail> distributionDetailList;
    private List<ProcurementDetail>procurementDetailList;
    private Double paidAmount;
    private String txnTimeStr;
    private SupplierProcurement supplierProcurement;
    private Map<String, String> filterData;
    
    private String tempBalAmt;
    private String temptxnAmount;
    private String tempIntBalance;
    private String startDate;
    private String endDate;
    
    private double vendorIntBalance;
    private double vendorTxnAmount;
    private double vendorBalAmount;  
    private long samithiId;

	/**
     * Sets the id.
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Gets the id.
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Sets the receipt no.
     * @param receiptNo the new receipt no
     */
    public void setReceiptNo(String receiptNo) {

        this.receiptNo = receiptNo;
    }

    /**
     * Gets the receipt no.
     * @return the receipt no
     */
    public String getReceiptNo() {

        return receiptNo;
    }

    /**
     * Sets the agent id.
     * @param agentId the new agent id
     */
    public void setAgentId(String agentId) {

        this.agentId = agentId;
    }

    /**
     * Gets the agent id.
     * @return the agent id
     */
    public String getAgentId() {

        return agentId;
    }

    /**
     * Sets the agent name.
     * @param agentName the new agent name
     */
    public void setAgentName(String agentName) {

        this.agentName = agentName;
    }

    /**
     * Gets the agent name.
     * @return the agent name
     */
    public String getAgentName() {

        return agentName;
    }

    /**
     * Sets the device id.
     * @param deviceId the new device id
     */
    public void setDeviceId(String deviceId) {

        this.deviceId = deviceId;
    }

    /**
     * Gets the device id.
     * @return the device id
     */
    public String getDeviceId() {

        return deviceId;
    }

    /**
     * Sets the device name.
     * @param deviceName the new device name
     */
    public void setDeviceName(String deviceName) {

        this.deviceName = deviceName;
    }

    /**
     * Gets the device name.
     * @return the device name
     */
    public String getDeviceName() {

        return deviceName;
    }

    /**
     * Sets the service point id.
     * @param servicePointId the new service point id
     */
    public void setServicePointId(String servicePointId) {

        this.servicePointId = servicePointId;
    }

    /**
     * Gets the service point id.
     * @return the service point id
     */
    public String getServicePointId() {

        return servicePointId;
    }

    /**
     * Sets the service point name.
     * @param servicePointName the new service point name
     */
    public void setServicePointName(String servicePointName) {

        this.servicePointName = servicePointName;
    }

    /**
     * Gets the service point name.
     * @return the service point name
     */
    public String getServicePointName() {

        return servicePointName;
    }

    /**
     * Sets the farmer id.
     * @param farmerId the new farmer id
     */
    public void setFarmerId(String farmerId) {

        this.farmerId = farmerId;
    }

    /**
     * Gets the farmer id.
     * @return the farmer id
     */
    public String getFarmerId() {

        return farmerId;
    }

    /**
     * Sets the farmer name.
     * @param farmerName the new farmer name
     */
    public void setFarmerName(String farmerName) {

        this.farmerName = farmerName;
    }

    /**
     * Gets the farmer name.
     * @return the farmer name
     */
    public String getFarmerName() {

        return farmerName;
    }

    /**
     * Gets the vendor id.
     * @return the vendor id
     */
    public String getVendorId() {

        return vendorId;
    }

    /**
     * Sets the vendor id.
     * @param vendorId the new vendor id
     */
    public void setVendorId(String vendorId) {

        this.vendorId = vendorId;
    }

    /**
     * Gets the vendor name.
     * @return the vendor name
     */
    public String getVendorName() {

        return vendorName;
    }

    /**
     * Sets the vendor name.
     * @param vendorName the new vendor name
     */
    public void setVendorName(String vendorName) {

        this.vendorName = vendorName;
    }

    /**
     * Gets the customer id.
     * @return the customer id
     */
    public String getCustomerId() {

        return customerId;
    }

    /**
     * Sets the customer id.
     * @param customerId the new customer id
     */
    public void setCustomerId(String customerId) {

        this.customerId = customerId;
    }

    /**
     * Gets the customer name.
     * @return the customer name
     */
    public String getCustomerName() {

        return customerName;
    }

    /**
     * Sets the customer name.
     * @param customerName the new customer name
     */
    public void setCustomerName(String customerName) {

        this.customerName = customerName;
    }

    /**
     * Sets the prof type.
     * @param profType the new prof type
     */
    public void setProfType(String profType) {

        this.profType = profType;
    }

    /**
     * Gets the prof type.
     * @return the prof type
     */
    public String getProfType() {

        return profType;
    }

    /**
     * Sets the txn type.
     * @param txnType the new txn type
     */
    public void setTxnType(String txnType) {

        this.txnType = txnType;
    }

    /**
     * Gets the txn type.
     * @return the txn type
     */
    public String getTxnType() {

        return txnType;
    }

    /**
     * Gets the mode of payment.
     * @return the mode of payment
     */
    public String getModeOfPayment() {

        return modeOfPayment;
    }

    /**
     * Sets the mode of payment.
     * @param modeOfPayment the new mode of payment
     */
    public void setModeOfPayment(String modeOfPayment) {

        this.modeOfPayment = modeOfPayment;
    }

    /**
     * Sets the int balance.
     * @param intBalance the new int balance
     */
    public void setIntBalance(double intBalance) {

        this.intBalance = intBalance;
    }

    /**
     * Gets the int balance.
     * @return the int balance
     */
    public double getIntBalance() {

        return intBalance;
    }

    /**
     * Sets the txn amount.
     * @param txnAmount the new txn amount
     */
    public void setTxnAmount(double txnAmount) {

        this.txnAmount = txnAmount;
    }

    /**
     * Gets the txn amount.
     * @return the txn amount
     */
    public double getTxnAmount() {

        return txnAmount;
    }

    /**
     * Sets the bal amount.
     * @param balAmount the new bal amount
     */
    public void setBalAmount(double balAmount) {

        this.balAmount = balAmount;
    }

    /**
     * Gets the bal amount.
     * @return the bal amount
     */
    public double getBalAmount() {

        return balAmount;
    }

    /**
     * Sets the oper type.
     * @param operType the new oper type
     */
    public void setOperType(int operType) {

        this.operType = operType;
    }

    /**
     * Gets the oper type.
     * @return the oper type
     */
    public int getOperType() {

        return operType;
    }

    /**
     * Sets the txn time.
     * @param txnTime the new txn time
     */
    public void setTxnTime(Date txnTime) {

        this.txnTime = txnTime;
    }

    /**
     * Gets the txn time.
     * @return the txn time
     */
    public Date getTxnTime() {

        return txnTime;
    }

    /**
     * Sets the txn desc.
     * @param txnDesc the new txn desc
     */
    public void setTxnDesc(String txnDesc) {

        this.txnDesc = txnDesc;
    }

    /**
     * Gets the txn desc.
     * @return the txn desc
     */
    public String getTxnDesc() {

        return txnDesc;
    }

    /**
     * Gets the account.
     * @return the account
     */
    public ESEAccount getAccount() {

        return account;
    }

    /**
     * Sets the account.
     * @param account the new account
     */
    public void setAccount(ESEAccount account) {

        this.account = account;
    }

    /**
     * Gets the transient id.
     * @return the transient id
     */
    public String getTransientId() {

        return transientId;
    }

    /**
     * Sets the transient id.
     * @param transientId the new transient id
     */
    public void setTransientId(String transientId) {

        this.transientId = transientId;
    }

    /**
     * Gets the samithi.
     * @return the samithi
     */
    public Warehouse getSamithi() {

        return samithi;
    }

    /**
     * Sets the samithi.
     * @param samithi the new samithi
     */
    public void setSamithi(Warehouse samithi) {

        this.samithi = samithi;
    }

    /**
     * Sets the ref agro transaction.
     * @param refAgroTransaction the new ref agro transaction
     */
    public void setRefAgroTransaction(AgroTransaction refAgroTransaction) {

        this.refAgroTransaction = refAgroTransaction;
    }

    /**
     * Gets the ref agro transaction.
     * @return the ref agro transaction
     */
    public AgroTransaction getRefAgroTransaction() {

        return refAgroTransaction;
    }

    /**
     * Calculate balance.
     * @param initBalance the init balance
     * @param txnAmount the txn amount
     * @param isCredit the is credit
     */
    public void calculateBalance(double initBalance, double txnAmount, boolean isCredit) {

        this.intBalance = initBalance;
        this.txnAmount = txnAmount;
        this.balAmount = isCredit ? (this.intBalance + this.txnAmount)
                : (this.intBalance - this.txnAmount);
    }

    /**
     * Calculate balance.
     * @param account the account
     * @param txnAmount the txn amount
     * @param isProcurementBalance the is procurement balance
     * @param isCredit the is credit
     */
    public void calculateBalance(ESEAccount account, double txnAmount,
            boolean isProcurementBalance, boolean isCredit) {

        this.account = account;
        if (!ObjectUtil.isEmpty(this.account)) {
            if (isProcurementBalance) {
                calculateBalance(this.account.getCashBalance(), txnAmount, isCredit);
                this.account.setCashBalance(this.balAmount);
            } else {
                calculateBalance(this.account.getCashBalance(), txnAmount, isCredit);
                this.account.setCashBalance(this.balAmount);
            }
        } else {
            calculateBalance(this.intBalance, txnAmount, isCredit);
        }
    }
    
    
    public void calculateCreditBalance(double initBalance, double txnAmount, boolean isCredit) {

        this.intBalance = initBalance;
        this.txnAmount = txnAmount;
        this.balAmount = isCredit ? (this.intBalance + this.txnAmount)
                : (this.intBalance - this.txnAmount);
    }

    /**
     * Calculate balance.
     * @param account the account
     * @param txnAmount the txn amount
     * @param isProcurementBalance the is procurement balance
     * @param isCredit the is credit
     */
    public void calculateCreditBalance(ESEAccount account, double txnAmount,
            boolean isProcurementBalance, boolean isCredit) {

        this.account = account;
        if (!ObjectUtil.isEmpty(this.account)) {
            if (!isProcurementBalance) {
                calculateCreditBalance(this.account.getCreditBalance(), txnAmount, isCredit);
                this.account.setCreditBalance(this.balAmount);
            } else {
                calculateCreditBalance(this.account.getCreditBalance(), txnAmount, isCredit);
                this.account.setCreditBalance(this.balAmount);
            }
        } else {
            calculateCreditBalance(this.intBalance, txnAmount, isCredit);
        }
    }


    /**
     * Sets the balance type.
     * @param balanceType the new balance type
     */
    public void setBalanceType(int balanceType) {

        this.balanceType = balanceType;
    }

    /**
     * Gets the balance type.
     * @return the balance type
     */
    public int getBalanceType() {

        return balanceType;
    }

    /**
     * Gets the audio file.
     * @return the audio file
     */
    public byte[] getAudioFile() {

        return audioFile;
    }

    /**
     * Sets the audio file.
     * @param audioFile the new audio file
     */
    public void setAudioFile(byte[] audioFile) {

        this.audioFile = audioFile;
    }

    /*
     * public String getStockType() { return stockType; } public void setStockType(String stockType)
     * { this.stockType = stockType; }
     */

    /**
     * Gets the warehouse code.
     * @return the warehouse code
     */
    public String getWarehouseCode() {

        return warehouseCode;
    }

    /**
     * Sets the warehouse code.
     * @param warehouseCode the new warehouse code
     */
    public void setWarehouseCode(String warehouseCode) {

        this.warehouseCode = warehouseCode;
    }

    /**
     * Gets the product stock.
     * @return the product stock
     */
    public String getProductStock() {

        return productStock;
    }

    /**
     * Sets the product stock.
     * @param productStock the new product stock
     */
    public void setProductStock(String productStock) {

        this.productStock = productStock;
    }

    /**
     * Gets the warehouse.
     * @return the warehouse
     */
    public Warehouse getWarehouse() {

        return warehouse;
    }

    /**
     * Sets the warehouse.
     * @param warehouse the new warehouse
     */
    public void setWarehouse(Warehouse warehouse) {

        this.warehouse = warehouse;
    }

	public long getEseAccountId() {
		return eseAccountId;
	}

	public void setEseAccountId(long eseAccountId) {
		this.eseAccountId = eseAccountId;
	}

    public List<String> getAgentList() {
    
        return agentList;
    }

    public void setAgentList(List<String> agentList) {
    
        this.agentList = agentList;
    }

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

    public List<String> getTxnTypeList() {
    
        return txnTypeList;
    }

    public void setTxnTypeList(List<String> txnTypeList) {
    
        this.txnTypeList = txnTypeList;
    }

	
	

	public Distribution getDistribution() {
		return distribution;
	}

	public void setDistribution(Distribution distribution) {
		this.distribution = distribution;
	}

	public Procurement getProcurement() {
		return procurement;
	}

	public void setProcurement(Procurement procurement) {
		this.procurement = procurement;
	}

	public Double getDebitAmt() {
		return debitAmt;
	}

	public void setDebitAmt(Double debitAmt) {
		this.debitAmt = debitAmt;
	}

	public Double getIntialBalance() {
		return intialBalance;
	}

	public void setIntialBalance(Double intialBalance) {
		this.intialBalance = intialBalance;
	}

	public Double getCreditAmt() {
		return creditAmt;
	}

	public void setCreditAmt(Double creditAmt) {
		this.creditAmt = creditAmt;
	}

	public String getSeasonCode() {
		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
	}

	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}

	public Double getBalanceAmt() {
		return balanceAmt;
	}

	public void setBalanceAmt(Double balanceAmt) {
		this.balanceAmt = balanceAmt;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getFinalBalance() {
		return finalBalance;
	}

	public void setFinalBalance(String finalBalance) {
		this.finalBalance = finalBalance;
	}

	public List<DistributionDetail> getDistributionDetailList() {
		return distributionDetailList;
	}

	public void setDistributionDetailList(List<DistributionDetail> distributionDetailList) {
		this.distributionDetailList = distributionDetailList;
	}

	public List<ProcurementDetail> getProcurementDetailList() {
		return procurementDetailList;
	}

	public void setProcurementDetailList(List<ProcurementDetail> procurementDetailList) {
		this.procurementDetailList = procurementDetailList;
	}

	public Double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public String getTxnTimeStr() {
		return txnTimeStr;
	}

	public void setTxnTimeStr(String txnTimeStr) {
		this.txnTimeStr = txnTimeStr;
	}

	public String getBranch_id() {
		return branch_id;
	}

	public void setBranch_id(String branch_id) {
		this.branch_id = branch_id;
	}

	public ProductReturn getProductReturn() {
		return productReturn;
	}

	public void setProductReturn(ProductReturn productReturn) {
		this.productReturn = productReturn;
	}

	public Map<String, String> getFilterData() {
		return filterData;
	}

	public void setFilterData(Map<String, String> filterData) {
		this.filterData = filterData;
	}

	public SupplierProcurement getSupplierProcurement() {
		return supplierProcurement;
	}

	public void setSupplierProcurement(SupplierProcurement supplierProcurement) {
		this.supplierProcurement = supplierProcurement;
	}

	public ProcurementTraceability getProcurementTraceability() {
		return procurementTraceability;
	}

	public void setProcurementTraceability(ProcurementTraceability procurementTraceability) {
		this.procurementTraceability = procurementTraceability;
	}

	public DistributionStock getDistributionStock() {
		return distributionStock;
	}

	public void setDistributionStock(DistributionStock distributionStock) {
		this.distributionStock = distributionStock;
	}

	public String getTempBalAmt() {
		return tempBalAmt;
	}

	public void setTempBalAmt(String tempBalAmt) {
		this.tempBalAmt = tempBalAmt;
	}

	public String getTempIntBalance() {
		return tempIntBalance;
	}

	public void setTempIntBalance(String tempIntBalance) {
		this.tempIntBalance = tempIntBalance;
	}

	public String getTemptxnAmount() {
		return temptxnAmount;
	}

	public void setTemptxnAmount(String temptxnAmount) {
		this.temptxnAmount = temptxnAmount;
	}
    
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public double getVendorIntBalance() {
		return vendorIntBalance;
	}

	public void setVendorIntBalance(double vendorIntBalance) {
		this.vendorIntBalance = vendorIntBalance;
	}

	public double getVendorTxnAmount() {
		return vendorTxnAmount;
	}

	public void setVendorTxnAmount(double vendorTxnAmount) {
		this.vendorTxnAmount = vendorTxnAmount;
	}

	public double getVendorBalAmount() {
		return vendorBalAmount;
	}

	public void setVendorBalAmount(double vendorBalAmount) {
		this.vendorBalAmount = vendorBalAmount;
	}

	public long getSamithiId() {
		return samithiId;
	}

	public void setSamithiId(long samithiId) {
		this.samithiId = samithiId;
	}
	 public void calculateFarmerLoanBalance(ESEAccount account, double txnAmount) {

	        this.account = account;
	        if (!ObjectUtil.isEmpty(this.account)) {
	           calculateLoanBalance(this.account.getOutstandingLoanAmount(), txnAmount);
	                this.account.setOutstandingLoanAmount(this.balAmount);
	           
	        } 
	    }
	 
	 public void calculateLoanBalance(double outStandingLoanAmt, double txnAmount) {

	        this.intBalance = outStandingLoanAmt;
	        this.txnAmount = txnAmount;
	        if(this.intBalance > this.txnAmount){
	        	this.balAmount = this.intBalance - this.txnAmount;
	        }else{
	        	this.balAmount =0.0;
	        }
	        
	    }
	
}
