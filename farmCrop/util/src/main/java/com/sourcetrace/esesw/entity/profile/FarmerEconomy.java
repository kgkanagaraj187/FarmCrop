/*
 * FarmerEconomy.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

// TODO: Auto-generated Javadoc
public class FarmerEconomy {
	
	public enum HOUSING_OWNERSHIP{
		NA,LEASED,OWN,RENTED,OTHERS
	}
	
    private long id;
    private Farmer farmer;
    private int housingOwnership;
    private String housingType;
    private String otherHousingType;
    private String annualIncome;
    private int electrifiedHouse;
    private String drinkingWaterSource;
    private int lifeOrHealthInsurance;
    private int cropInsurance;
    private String housingOwnershipOther;
    private String drinkingWaterSourceOther;
    private String cookingFuel;
    private String cookingFuelSourceOther;
    private Integer toiletAvailable;
    private String ifToiletAvailable;
    private String qtyCookingPerMonth;
    private String costCookingPerMonth;

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
     * Gets the farmer.
     * @return the farmer
     */
    public Farmer getFarmer() {

        return farmer;
    }

    /**
     * Sets the farmer.
     * @param farmer the new farmer
     */
    public void setFarmer(Farmer farmer) {

        this.farmer = farmer;
    }

    /**
     * Gets the housing ownership.
     * @return the housing ownership
     */
   // @NotEmpty(message = "empty.housingOwnership")
   // @NotNull(message = "empty.housingOwnership")
    public int getHousingOwnership() {

        return housingOwnership;
    }

    /**
     * Sets the housing ownership.
     * @param housingOwnership the new housing ownership
     */
    public void setHousingOwnership(int housingOwnership) {

        this.housingOwnership = housingOwnership;
    }

  
    /**
     * Gets the other housing type.
     * @return the other housing type
     */
    public String getOtherHousingType() {

        return otherHousingType;
    }

    /**
     * Sets the other housing type.
     * @param otherHousingType the new other housing type
     */
    public void setOtherHousingType(String otherHousingType) {

        this.otherHousingType = otherHousingType;
    }

    /**
     * Sets the annual income.
     * @param annualIncome the new annual income
     */
    public void setAnnualIncome(String annualIncome) {

        this.annualIncome = annualIncome;
    }

    /**
     * Gets the annual income.
     * @return the annual income
     */
   // @NotEmpty(message = "empty.annualIncome")
   // @NotNull(message = "empty.annualIncome")
    public String getAnnualIncome() {

        return annualIncome;
    }

  
    /**
     * Gets the drinking water source.
     * @return the drinking water source
     */
    public String getDrinkingWaterSource() {

        return drinkingWaterSource;
    }

    /**
     * Sets the drinking water source.
     * @param drinkingWaterSource the new drinking water source
     */
    public void setDrinkingWaterSource(String drinkingWaterSource) {

        this.drinkingWaterSource = drinkingWaterSource;
    }

    /**
     * Gets the housing ownership other.
     * @return the housing ownership other
     */
    public String getHousingOwnershipOther() {

        return housingOwnershipOther;
    }

    /**
     * Sets the housing ownership other.
     * @param housingOwnershipOther the new housing ownership other
     */
    public void setHousingOwnershipOther(String housingOwnershipOther) {

        this.housingOwnershipOther = housingOwnershipOther;
    }

	public int getElectrifiedHouse() {
		return electrifiedHouse;
	}

	public void setElectrifiedHouse(int electrifiedHouse) {
		this.electrifiedHouse = electrifiedHouse;
	}

	public int getLifeOrHealthInsurance() {
		return lifeOrHealthInsurance;
	}

	public void setLifeOrHealthInsurance(int lifeOrHealthInsurance) {
		this.lifeOrHealthInsurance = lifeOrHealthInsurance;
	}

	public int getCropInsurance() {
		return cropInsurance;
	}

	public void setCropInsurance(int cropInsurance) {
		this.cropInsurance = cropInsurance;
	}

	public String getDrinkingWaterSourceOther() {
		return drinkingWaterSourceOther;
	}

	public void setDrinkingWaterSourceOther(String drinkingWaterSourceOther) {
		this.drinkingWaterSourceOther = drinkingWaterSourceOther;
	}

	public String getCookingFuelSourceOther() {
		return cookingFuelSourceOther;
	}

	public void setCookingFuelSourceOther(String cookingFuelSourceOther) {
		this.cookingFuelSourceOther = cookingFuelSourceOther;
	}

	public String getCookingFuel() {
		return cookingFuel;
	}

	public void setCookingFuel(String cookingFuel) {
		this.cookingFuel = cookingFuel;
	}

	public Integer getToiletAvailable() {
		return toiletAvailable;
	}

	public void setToiletAvailable(Integer toiletAvailable) {
		this.toiletAvailable = toiletAvailable;
	}

	public String getIfToiletAvailable() {
		return ifToiletAvailable;
	}

	public void setIfToiletAvailable(String ifToiletAvailable) {
		this.ifToiletAvailable = ifToiletAvailable;
	}

	public String getHousingType() {
		return housingType;
	}

	public void setHousingType(String housingType) {
		this.housingType = housingType;
	}

	public String getQtyCookingPerMonth() {
		return qtyCookingPerMonth;
	}

	public void setQtyCookingPerMonth(String qtyCookingPerMonth) {
		this.qtyCookingPerMonth = qtyCookingPerMonth;
	}

	public String getCostCookingPerMonth() {
		return costCookingPerMonth;
	}

	public void setCostCookingPerMonth(String costCookingPerMonth) {
		this.costCookingPerMonth = costCookingPerMonth;
	}
   

}
