package com.ese.entity.profile;

import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;

// TODO: Auto-generated Javadoc
public class CropSupplyDetails {

    public static enum cropType {
        MAINCROP, INTERCROP
    }

    private long id;
    private ProcurementProduct crop;
    private ProcurementGrade grade;
    private String batchLotNo;
    private int cropType;
    private double qty;
    private double price;
    private double subTotal;
    private CropSupply cropSupply;
    private ProcurementVariety variety;
    
    /** Transient Variable */
	private String cropTypeStr;
    private long cropId;
    private long varietyId;
    private long gradeId;	
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
     * Gets the crop.
     * @return the crop
     */
    public ProcurementProduct getCrop() {

        return crop;
    }

    /**
     * Sets the crop.
     * @param crop the new crop
     */
    public void setCrop(ProcurementProduct crop) {

        this.crop = crop;
    }

    /**
     * Gets the crop type.
     * @return the crop type
     */
    public int getCropType() {

        return cropType;
    }

    /**
     * Sets the crop type.
     * @param cropType the new crop type
     */
    public void setCropType(int cropType) {

        this.cropType = cropType;
    }

    /**
     * Gets the qty.
     * @return the qty
     */
    public double getQty() {

        return qty;
    }

    /**
     * Sets the qty.
     * @param qty the new qty
     */
    public void setQty(double qty) {

        this.qty = qty;
    }

    /**
     * Gets the price.
     * @return the price
     */
    public double getPrice() {

        return price;
    }

    /**
     * Sets the price.
     * @param price the new price
     */
    public void setPrice(double price) {

        this.price = price;
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
     * Gets the grade.
     * @return the grade
     */
    public ProcurementGrade getGrade() {

        return grade;
    }

    /**
     * Sets the grade.
     * @param grade the new grade
     */
    public void setGrade(ProcurementGrade grade) {

        this.grade = grade;
    }

    /**
     * Gets the batch lot no.
     * @return the batch lot no
     */
    public String getBatchLotNo() {

        return batchLotNo;
    }

    /**
     * Sets the batch lot no.
     * @param batchLotNo the new batch lot no
     */
    public void setBatchLotNo(String batchLotNo) {

        this.batchLotNo = batchLotNo;
    }

    /**
     * Gets the crop supply.
     * @return the crop supply
     */
    public CropSupply getCropSupply() {

        return cropSupply;
    }

    /**
     * Sets the crop supply.
     * @param cropSupply the new crop supply
     */
    public void setCropSupply(CropSupply cropSupply) {

        this.cropSupply = cropSupply;
    }

    public ProcurementVariety getVariety() {
    
        return variety;
    }

    public void setVariety(ProcurementVariety variety) {
    
        this.variety = variety;
    }

	public String getCropTypeStr() {
		return cropTypeStr;
	}

	public void setCropTypeStr(String cropTypeStr) {
		this.cropTypeStr = cropTypeStr;
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
    
    

}
