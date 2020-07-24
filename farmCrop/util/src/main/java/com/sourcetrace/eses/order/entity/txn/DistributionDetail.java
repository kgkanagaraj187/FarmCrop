/*
 * DistributionDetail.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.io.Serializable;
import java.util.List;

import com.sourcetrace.eses.util.profile.Product;

public class DistributionDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    private long id;
    private Product product;
    private double quantity;
    private String unit;
    private double pricePerUnit;
    private double subTotal;
    private Distribution distribution;
    private double sellingPrice;
    private double tax;
    private double finalAmount;
    private String costPriceArray;
    private double costPrice;
    private String batchNo;
    //transient variable
    private String categoryName;
    private String currentQuantity;
    private String existingQuantity;
    private String quantiy;
    private String disQuantity;
    private String disExistQuantity;
    private String avlQty;
    private String amount;
    private String productName;
    private long distributionId;
    private String agentId;
    private String warehouseCode;
    private long productId;
    private String agentName;
    private String warehouseName;
    private String branchId;
    private int status;
    private String receiptNo;
    private String enableApproved;
   
    private String seasonCode;
    private String sagiCode;
    //Transient 
    private String individulalFinalBal;
    private String item;
	private List<String> branchesList;
	private int amountFlag;
	private int qtyflag;
	//NSwitch Distribution based on individual farmer product balance
	private double initFBalance;
	private double initTBalance;
	private double finalFBalance;
     public double getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(Double finalAmount) {
		this.finalAmount = finalAmount;
	}

	public double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	

	public double getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	/**
     * Gets the id.
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Gets the product.
     * @return the product
     */
    public Product getProduct() {

        return product;
    }

    /**
     * Sets the product.
     * @param product the new product
     */
    public void setProduct(Product product) {

        this.product = product;
    }

    /**
     * Gets the quantity.
     * @return the quantity
     */
    public double getQuantity() {

        return quantity;
    }

    /**
     * Sets the quantity.
     * @param quantity the new quantity
     */
    public void setQuantity(double quantity) {

        this.quantity = quantity;
    }

    /**
     * Gets the price per unit.
     * @return the price per unit
     */
    public double getPricePerUnit() {

        return pricePerUnit;
    }

    /**
     * Sets the price per unit.
     * @param pricePerUnit the new price per unit
     */
    public void setPricePerUnit(double pricePerUnit) {

        this.pricePerUnit = pricePerUnit;
    }

    /**
     * Gets the sub total.
     * @return the sub total
     */
    public double getSubTotal() {

        return subTotal;
    }

    /**
     * Sets the sub total.
     * @param subTotal the new sub total
     */
    public void setSubTotal(double subTotal) {

        this.subTotal = subTotal;
    }

    /**
     * Gets the distribution.
     * @return the distribution
     */
    public Distribution getDistribution() {

        return distribution;
    }

    /**
     * Sets the distribution.
     * @param distribution the new distribution
     */
    public void setDistribution(Distribution distribution) {

        this.distribution = distribution;
    }

    /**
     * Sets the unit.
     * @param unit the new unit
     */
    public void setUnit(String unit) {

        this.unit = unit;
    }

    /**
     * Gets the unit.
     * @return the unit
     */
    public String getUnit() {

        return unit;
    }

	public void setCostPriceArray(String costPriceArray) {
		this.costPriceArray = costPriceArray;
	}

	public String getCostPriceArray() {
		return costPriceArray;
	}

	public String getCurrentQuantity() {
		return currentQuantity;
	}

	public void setCurrentQuantity(String currentQuantity) {
		this.currentQuantity = currentQuantity;
	}

	public String getExistingQuantity() {
		return existingQuantity;
	}

	public void setExistingQuantity(String existingQuantity) {
		this.existingQuantity = existingQuantity;
	}

	public double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}

	public String getQuantiy() {
		return quantiy;
	}

	public void setQuantiy(String quantiy) {
		this.quantiy = quantiy;
	}

	public String getDisQuantity() {
		return disQuantity;
	}

	public void setDisQuantity(String disQuantity) {
		this.disQuantity = disQuantity;
	}

	public String getAvlQty() {
		return avlQty;
	}

	public void setAvlQty(String avlQty) {
		this.avlQty = avlQty;
	}

	public void setTax(double tax) {
		this.tax = tax;
	}

	public void setFinalAmount(double finalAmount) {
		this.finalAmount = finalAmount;
	}

	

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public long getDistributionId() {
		return distributionId;
	}

	public void setDistributionId(long distributionId) {
		this.distributionId = distributionId;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getEnableApproved() {
		return enableApproved;
	}

	public void setEnableApproved(String enableApproved) {
		this.enableApproved = enableApproved;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getSeasonCode() {
		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
	}

	public String getDisExistQuantity() {
		return disExistQuantity;
	}

	public void setDisExistQuantity(String disExistQuantity) {
		this.disExistQuantity = disExistQuantity;
	}

	public String getIndividulalFinalBal() {
		return individulalFinalBal;
	}

	public void setIndividulalFinalBal(String individulalFinalBal) {
		this.individulalFinalBal = individulalFinalBal;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public void setBranchesList(List<String> branchesList) {
		// TODO Auto-generated method stub
		this.branchesList = branchesList;
	}

	public int getAmountFlag() {
		return amountFlag;
	}

	public void setAmountFlag(int amountFlag) {
		this.amountFlag = amountFlag;
	}

	public int getQtyflag() {
		return qtyflag;
	}

	public void setQtyflag(int qtyflag) {
		this.qtyflag = qtyflag;
	}

	public String getSagiCode() {
		return sagiCode;
	}

	public void setSagiCode(String sagiCode) {
		this.sagiCode = sagiCode;
	}

	public double getInitFBalance() {
		return initFBalance;
	}

	public void setInitFBalance(double initFBalance) {
		this.initFBalance = initFBalance;
	}

	public double getInitTBalance() {
		return initTBalance;
	}

	public void setInitTBalance(double initTBalance) {
		this.initTBalance = initTBalance;
	}

	public double getFinalFBalance() {
		return finalFBalance;
	}

	public void setFinalFBalance(double finalFBalance) {
		this.finalFBalance = finalFBalance;
	}

	public List<String> getBranchesList() {
		return branchesList;
	}
	
	

}
