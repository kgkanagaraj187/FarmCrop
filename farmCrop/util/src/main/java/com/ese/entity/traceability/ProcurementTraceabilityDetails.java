package com.ese.entity.traceability;

import java.util.List;

import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;

public class ProcurementTraceabilityDetails {

	private long id;
	private ProcurementTraceability procurementTraceability;
	private ProcurementGrade procuremntGrade;
	private ProcurementProduct procurementProduct;
	private String unit;
	private double price;
	private double premiumPrice;
	private double totalPrice;
	private double totalPricepremium;
	private double netWeight;
	private long numberOfBags;

	// transient
	private List<String> branchesList;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ProcurementTraceability getProcurementTraceability() {
		return procurementTraceability;
	}

	public void setProcurementTraceability(ProcurementTraceability procurementTraceability) {
		this.procurementTraceability = procurementTraceability;
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
		return netWeight;
	}

	public void setNetWeight(double netWeight) {
		this.netWeight = netWeight;
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

	public List<String> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}

}
