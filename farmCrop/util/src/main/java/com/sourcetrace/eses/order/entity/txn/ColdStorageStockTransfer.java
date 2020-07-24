/*
 * PMT.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;

public class ColdStorageStockTransfer {

	private long id;
	private Date transferDate;
	private Warehouse warehouse;
	private String coldStorageName;
	private Customer buyer;
	private String truckId;
	private String driverName;
	private String batchNo;
	private String invoiceNo;
	private String branchId;
	private String receiptNo;
	private Set<ColdStorageStockTransferDetail> coldStorageStockTransferDetail;
	private String createdUserName;
	private Date createdDate;
	private String updatedUserName;
	private Date updatedDate;
	private long totalNoOfBags;
	private double totalQty;
	private String revisionNo;
	private String poNumber;
	private String invoiceNumber;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getTransferDate() {
		return transferDate;
	}
	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}
	public Warehouse getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}
	public String getColdStorageName() {
		return coldStorageName;
	}
	public void setColdStorageName(String coldStorageName) {
		this.coldStorageName = coldStorageName;
	}
	public String getTruckId() {
		return truckId;
	}
	public void setTruckId(String truckId) {
		this.truckId = truckId;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	public Set<ColdStorageStockTransferDetail> getColdStorageStockTransferDetail() {
		return coldStorageStockTransferDetail;
	}
	public void setColdStorageStockTransferDetail(Set<ColdStorageStockTransferDetail> coldStorageStockTransferDetail) {
		this.coldStorageStockTransferDetail = coldStorageStockTransferDetail;
	}
	public String getCreatedUserName() {
		return createdUserName;
	}
	public void setCreatedUserName(String createdUserName) {
		this.createdUserName = createdUserName;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getUpdatedUserName() {
		return updatedUserName;
	}
	public void setUpdatedUserName(String updatedUserName) {
		this.updatedUserName = updatedUserName;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public long getTotalNoOfBags() {
		return totalNoOfBags;
	}
	public void setTotalNoOfBags(long totalNoOfBags) {
		this.totalNoOfBags = totalNoOfBags;
	}
	public double getTotalQty() {
		return totalQty;
	}
	public void setTotalQty(double totalQty) {
		this.totalQty = totalQty;
	}
	public Customer getBuyer() {
		return buyer;
	}
	public void setBuyer(Customer buyer) {
		this.buyer = buyer;
	}
	public String getRevisionNo() {
		return revisionNo;
	}
	public void setRevisionNo(String revisionNo) {
		this.revisionNo = revisionNo;
	}
	public String getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	
	
	 
	

}
