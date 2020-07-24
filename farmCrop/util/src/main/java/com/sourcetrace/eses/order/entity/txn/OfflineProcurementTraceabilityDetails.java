package com.sourcetrace.eses.order.entity.txn;

import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;

public class OfflineProcurementTraceabilityDetails 
{
	
	
	private long id;
	private OfflineProcurementTraceability offlineProcurementTraceability;
	private ProcurementGrade procuremntGrade;
	private ProcurementProduct procurementProduct;
	private String unit;
	private double price;
	private double premiumPrice;
	private double totalPrice;
	private double totalPricepremium;
	private double NetWeight;
	private long numberOfBags;
	private String gradeCode;
	private String productCode;
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public ProcurementGrade getProcuremntGrade() {
		return procuremntGrade;
	}

	public void setProcuremntGrade(ProcurementGrade procuremntGrade) {
		this.procuremntGrade = procuremntGrade;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public ProcurementProduct getProcurementProduct() {
		return procurementProduct;
	}

	public void setProcurementProduct(ProcurementProduct procurementProduct) {
		this.procurementProduct = procurementProduct;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public double getNetWeight() {
		return NetWeight;
	}

	public void setNetWeight(double netWeight) {
		NetWeight = netWeight;
	}

	public long getNumberOfBags() {
		return numberOfBags;
	}

	public void setNumberOfBags(long numberOfBags) {
		this.numberOfBags = numberOfBags;
	}

	public double getPremiumPrice() {
		return premiumPrice;
	}

	public void setPremiumPrice(double premiumPrice) {
		this.premiumPrice = premiumPrice;
	}

	public double getTotalPricepremium() {
		return totalPricepremium;
	}

	public void setTotalPricepremium(double totalPricepremium) {
		this.totalPricepremium = totalPricepremium;
	}

	public OfflineProcurementTraceability getOfflineProcurementTraceability() {
		return offlineProcurementTraceability;
	}

	public void setOfflineProcurementTraceability(OfflineProcurementTraceability offlineProcurementTraceability) {
		this.offlineProcurementTraceability = offlineProcurementTraceability;
	}

	public String getGradeCode() {
		return gradeCode;
	}

	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}


}
