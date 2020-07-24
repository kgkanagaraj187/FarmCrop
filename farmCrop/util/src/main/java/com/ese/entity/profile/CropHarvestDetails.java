package com.ese.entity.profile;

import java.util.List;

import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;

// TODO: Auto-generated Javadoc
public class CropHarvestDetails {

	public static enum cropType {
		MAINCROP, INTERCROP
	}

	private long id;
	private ProcurementProduct crop;
	private ProcurementGrade grade;
	private ProcurementVariety variety;
	private int cropType;
	private double qty;
	private double price;
	private double subTotal;
	private CropHarvest cropHarvest;

	// transient variables
	private long cropId;
	private long varietyId;
	private long gradeId;
	private String harvestSeason;

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
	 * Gets the crop.
	 * 
	 * @return the crop
	 */
	public ProcurementProduct getCrop() {

		return crop;
	}

	/**
	 * Sets the crop.
	 * 
	 * @param crop
	 *            the new crop
	 */
	public void setCrop(ProcurementProduct crop) {

		this.crop = crop;
	}

	/**
	 * Gets the crop type.
	 * 
	 * @return the crop type
	 */
	public int getCropType() {

		return cropType;
	}

	/**
	 * Sets the crop type.
	 * 
	 * @param cropType
	 *            the new crop type
	 */
	public void setCropType(int cropType) {

		this.cropType = cropType;
	}

	/**
	 * Gets the qty.
	 * 
	 * @return the qty
	 */
	public double getQty() {

		return qty;
	}

	/**
	 * Sets the qty.
	 * 
	 * @param qty
	 *            the new qty
	 */
	public void setQty(double qty) {

		this.qty = qty;
	}

	/**
	 * Gets the price.
	 * 
	 * @return the price
	 */
	public double getPrice() {

		return price;
	}

	/**
	 * Sets the price.
	 * 
	 * @param price
	 *            the new price
	 */
	public void setPrice(double price) {

		this.price = price;
	}

	/**
	 * Gets the sub total.
	 * 
	 * @return the sub total
	 */
	public double getSubTotal() {

		return subTotal;
	}

	/**
	 * Sets the sub total.
	 * 
	 * @param subTotal
	 *            the new sub total
	 */
	public void setSubTotal(double subTotal) {

		this.subTotal = subTotal;
	}

	/**
	 * Gets the crop harvest.
	 * 
	 * @return the crop harvest
	 */
	public CropHarvest getCropHarvest() {

		return cropHarvest;
	}

	/**
	 * Sets the crop harvest.
	 * 
	 * @param cropHarvest
	 *            the new crop harvest
	 */
	public void setCropHarvest(CropHarvest cropHarvest) {

		this.cropHarvest = cropHarvest;
	}

	public ProcurementGrade getGrade() {

		return grade;
	}

	public void setGrade(ProcurementGrade grade) {

		this.grade = grade;
	}

	public ProcurementVariety getVariety() {

		return variety;
	}

	public void setVariety(ProcurementVariety variety) {

		this.variety = variety;
	}

	public long getCropId() {
		return cropId;
	}

	public void setCropId(long cropId) {
		this.cropId = cropId;
	}

	public long getVarietyId() {
		return varietyId;
	}

	public void setVarietyId(long varietyId) {
		this.varietyId = varietyId;
	}

	public long getGradeId() {
		return gradeId;
	}

	public void setGradeId(long gradeId) {
		this.gradeId = gradeId;
	}

	public String getHarvestSeason() {
		return harvestSeason;
	}

	public void setHarvestSeason(String harvestSeason) {
		this.harvestSeason = harvestSeason;
	}
}
