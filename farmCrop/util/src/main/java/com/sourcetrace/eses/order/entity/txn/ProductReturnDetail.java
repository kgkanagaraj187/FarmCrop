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
import java.util.Map;

import com.sourcetrace.eses.util.profile.Product;

public class ProductReturnDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    private long id;
    private Product product;
    private double quantity;
    private String unit;
    private double pricePerUnit;
    private double subTotal;
    private ProductReturn productReturn;
    private double sellingPrice;
    private double tax;
    private double finalAmount;
    private String costPriceArray;
    private double costPrice;
    private String batchNo;
    private Map<String, String> filterData;
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
    private long productReturnId;
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
    //Transient 
    private String individulalFinalBal;
    private String item;
	private List<String> branchesList;
	private int amountFlag;
	private int qtyflag;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public double getPricePerUnit() {
		return pricePerUnit;
	}
	public void setPricePerUnit(double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}
	public double getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}
	public ProductReturn getProductReturn() {
		return productReturn;
	}
	public void setProductReturn(ProductReturn productReturn) {
		this.productReturn = productReturn;
	}
	public double getSellingPrice() {
		return sellingPrice;
	}
	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}
	public double getTax() {
		return tax;
	}
	public void setTax(double tax) {
		this.tax = tax;
	}
	public double getFinalAmount() {
		return finalAmount;
	}
	public void setFinalAmount(double finalAmount) {
		this.finalAmount = finalAmount;
	}
	public String getCostPriceArray() {
		return costPriceArray;
	}
	public void setCostPriceArray(String costPriceArray) {
		this.costPriceArray = costPriceArray;
	}
	public double getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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
	public String getDisExistQuantity() {
		return disExistQuantity;
	}
	public void setDisExistQuantity(String disExistQuantity) {
		this.disExistQuantity = disExistQuantity;
	}
	public String getAvlQty() {
		return avlQty;
	}
	public void setAvlQty(String avlQty) {
		this.avlQty = avlQty;
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
	public long getProductReturnId() {
		return productReturnId;
	}
	public void setProductReturnId(long productReturnId) {
		this.productReturnId = productReturnId;
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
	public String getSeasonCode() {
		return seasonCode;
	}
	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
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
	public List<String> getBranchesList() {
		return branchesList;
	}
	public void setBranchesList(List<String> branchesList) {
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Map<String, String> getFilterData() {
		return filterData;
	}
	public void setFilterData(Map<String, String> filterData) {
		this.filterData = filterData;
	}
	
	
    
}
