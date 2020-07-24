package com.sourcetrace.eses.order.entity.txn;

public class OfflinePMTNRDetail
{
	
	private Long id;
	private String productCode;
	private String noOfBags;
	private String grossWeight;
	private String coOperativeCode;
	private String subTotal;
	//private String gradeCode;
	private String transferBags;
	private String transferWeight;
	private OfflinePMTNR offlinePMTNR;
	private String ics;
	private String heap;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNoOfBags() {
		return noOfBags;
	}
	public void setNoOfBags(String noOfBags) {
		this.noOfBags = noOfBags;
	}
	public String getGrossWeight() {
		return grossWeight;
	}
	public void setGrossWeight(String grossWeight) {
		this.grossWeight = grossWeight;
	}
	
	
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	

	public OfflinePMTNR getOfflinePMTNR() {
		return offlinePMTNR;
	}
	public void setOfflinePMTNR(OfflinePMTNR offlinePMTNR) {
		this.offlinePMTNR = offlinePMTNR;
	}
	
	public String getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}
	public String getCoOperativeCode() {
		return coOperativeCode;
	}
	public void setCoOperativeCode(String coOperativeCode) {
		this.coOperativeCode = coOperativeCode;
	}
	/*public String getGradeCode() {
		return gradeCode;
	}
	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}*/
	public String getIcs() {
		return ics;
	}
	public void setIcs(String ics) {
		this.ics = ics;
	}
	public String getTransferBags() {
		return transferBags;
	}
	public void setTransferBags(String transferBags) {
		this.transferBags = transferBags;
	}
	public String getTransferWeight() {
		return transferWeight;
	}
	public void setTransferWeight(String transferWeight) {
		this.transferWeight = transferWeight;
	}
	public String getHeap() {
		return heap;
	}
	public void setHeap(String heap) {
		this.heap = heap;
	}


}
