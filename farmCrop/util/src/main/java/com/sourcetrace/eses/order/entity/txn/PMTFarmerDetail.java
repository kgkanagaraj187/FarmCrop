/*
 * PMTDetail.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.profile.GradeMaster;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.Village;

// TODO: Auto-generated Javadoc
public class PMTFarmerDetail {

	private long id;
	private GradeMaster gradeMaster;
	private ProcurementProduct procurementProduct;
	private ProcurementGrade procurementGrade;
	private long mtntNumberOfBags;
	private long mtnrNumberOfBags;
	private double mtntGrossWeight;
	private double mtnrGrossWeight;
	private Village village;
	private PMT pmt;
	private Warehouse coOperative;
	private String farmer;
	private String branchId;
	private int status;
	private String ics;
	private String season;

	private String pricePerUnit;
	private String subTotal;
	private String uom;

	// Transient variables
	private String varietyname;
	private String grade;
	private String bags;
	private String weight;

	public String getVarietyname() {
		return varietyname;
	}

	public void setVarietyname(String varietyname) {
		this.varietyname = varietyname;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getBags() {
		return bags;
	}

	public void setBags(String bags) {
		this.bags = bags;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {

		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(long id) {

		this.id = id;
	}

	/**
	 * Gets the grade master.
	 * 
	 * @return the grade master
	 */
	public GradeMaster getGradeMaster() {

		return gradeMaster;
	}

	/**
	 * Sets the grade master.
	 * 
	 * @param gradeMaster
	 *            the new grade master
	 */
	public void setGradeMaster(GradeMaster gradeMaster) {

		this.gradeMaster = gradeMaster;
	}

	/**
	 * Gets the procurement product.
	 * 
	 * @return the procurement product
	 */
	public ProcurementProduct getProcurementProduct() {

		return procurementProduct;
	}

	/**
	 * Sets the procurement product.
	 * 
	 * @param procurementProduct
	 *            the new procurement product
	 */
	public void setProcurementProduct(ProcurementProduct procurementProduct) {

		this.procurementProduct = procurementProduct;
	}

	/**
	 * Gets the procurement grade.
	 * 
	 * @return the procurement grade
	 */
	public ProcurementGrade getProcurementGrade() {

		return procurementGrade;
	}

	/**
	 * Sets the procurement grade.
	 * 
	 * @param procurementGrade
	 *            the new procurement grade
	 */
	public void setProcurementGrade(ProcurementGrade procurementGrade) {

		this.procurementGrade = procurementGrade;
	}

	/**
	 * Gets the mtnt number of bags.
	 * 
	 * @return the mtnt number of bags
	 */
	public long getMtntNumberOfBags() {

		return mtntNumberOfBags;
	}

	/**
	 * Sets the mtnt number of bags.
	 * 
	 * @param mtntNumberOfBags
	 *            the new mtnt number of bags
	 */
	public void setMtntNumberOfBags(long mtntNumberOfBags) {

		this.mtntNumberOfBags = mtntNumberOfBags;
	}

	/**
	 * Gets the mtnr number of bags.
	 * 
	 * @return the mtnr number of bags
	 */
	public long getMtnrNumberOfBags() {

		return mtnrNumberOfBags;
	}

	/**
	 * Sets the mtnr number of bags.
	 * 
	 * @param mtnrNumberOfBags
	 *            the new mtnr number of bags
	 */
	public void setMtnrNumberOfBags(long mtnrNumberOfBags) {

		this.mtnrNumberOfBags = mtnrNumberOfBags;
	}

	/**
	 * Gets the mtnt gross weight.
	 * 
	 * @return the mtnt gross weight
	 */
	public double getMtntGrossWeight() {

		return mtntGrossWeight;
	}

	/**
	 * Sets the mtnt gross weight.
	 * 
	 * @param mtntGrossWeight
	 *            the new mtnt gross weight
	 */
	public void setMtntGrossWeight(double mtntGrossWeight) {

		this.mtntGrossWeight = mtntGrossWeight;
	}

	/**
	 * Gets the mtnr gross weight.
	 * 
	 * @return the mtnr gross weight
	 */
	public double getMtnrGrossWeight() {

		return mtnrGrossWeight;
	}

	/**
	 * Sets the mtnr gross weight.
	 * 
	 * @param mtnrGrossWeight
	 *            the new mtnr gross weight
	 */
	public void setMtnrGrossWeight(double mtnrGrossWeight) {

		this.mtnrGrossWeight = mtnrGrossWeight;
	}

	/**
	 * Gets the pmt.
	 * 
	 * @return the pmt
	 */
	public PMT getPmt() {

		return pmt;
	}

	/**
	 * Sets the pmt.
	 * 
	 * @param pmt
	 *            the new pmt
	 */
	public void setPmt(PMT pmt) {

		this.pmt = pmt;
	}

	/**
	 * Gets the intransient number of bags.
	 * 
	 * @return the intransient number of bags
	 */
	public long getIntransientNumberOfBags() {

		return getMtntNumberOfBags() - getMtnrNumberOfBags();
	}

	/**
	 * Gets the intransient gross weight.
	 * 
	 * @return the intransient gross weight
	 */
	public double getIntransientGrossWeight() {

		return getMtntGrossWeight() - getMtnrGrossWeight();
	}

	/**
	 * Gets the village.
	 * 
	 * @return the village
	 */
	public Village getVillage() {

		return village;
	}

	/**
	 * Sets the village.
	 * 
	 * @param village
	 *            the new village
	 */
	public void setVillage(Village village) {

		this.village = village;
	}

	/**
	 * Gets the co operative.
	 * 
	 * @return the co operative
	 */
	public Warehouse getCoOperative() {

		return coOperative;
	}

	/**
	 * Sets the co operative.
	 * 
	 * @param coOperative
	 *            the new co operative
	 */
	public void setCoOperative(Warehouse coOperative) {

		this.coOperative = coOperative;
	}

	public String getFarmer() {
		return farmer;
	}

	public void setFarmer(String farmer) {
		this.farmer = farmer;
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

	public String getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(String pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public String getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public String getIcs() {
		return ics;
	}

	public void setIcs(String ics) {
		this.ics = ics;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}
	
	
}
