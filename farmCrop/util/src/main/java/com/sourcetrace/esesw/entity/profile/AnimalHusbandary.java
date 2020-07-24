/*
 * AnimalHusbandary.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import com.sourcetrace.eses.entity.FarmCatalogue;

// TODO: Auto-generated Javadoc
public class AnimalHusbandary {

    private String id;
    private Farmer farmer;
    private FarmCatalogue farmAnimal;
    private String otherFarmAnimal;
    private int feedingUsed;
    private FarmCatalogue animalHousing;
    private String otherAnimalHousing;
    private String animalCount;
    private String production;
    private String fymEstimatedOutput;
    private String fodder;
    private String otherFodder;    
    private String revenue;
    private String otherRevenue;
    private String breed;
    private String farmId;
    private String manureCollected;
    private String urineCollected;

  //Transient var
    private String invItem1;
    private String revenueStr;
    private String fodderStr;
    private String housStr;
    private String animalStr;
    private String breedStr;
    /**
     * Gets the id.
     * @return the id
     */
    public String getId() {

        return id;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(String id) {

        this.id = id;
    }

   

    /**
     * Gets the farm animal.
     * @return the farm animal
     */
    public FarmCatalogue getFarmAnimal() {

        return farmAnimal;
    }

    /**
     * Sets the farm animal.
     * @param farmAnimal the new farm animal
     */
    public void setFarmAnimal(FarmCatalogue farmAnimal) {

        this.farmAnimal = farmAnimal;
    }

    /**
     * Gets the other farm animal.
     * @return the other farm animal
     */
    public String getOtherFarmAnimal() {

        return otherFarmAnimal;
    }

    /**
     * Sets the other farm animal.
     * @param otherFarmAnimal the new other farm animal
     */
    public void setOtherFarmAnimal(String otherFarmAnimal) {

        this.otherFarmAnimal = otherFarmAnimal;
    }

    /**
     * Gets the feeding used.
     * @return the feeding used
     */
    public int getFeedingUsed() {

        return feedingUsed;
    }

    /**
     * Sets the feeding used.
     * @param feedingUsed the new feeding used
     */
    public void setFeedingUsed(int feedingUsed) {

        this.feedingUsed = feedingUsed;
    }

    /**
     * Gets the animal housing.
     * @return the animal housing
     */
    public FarmCatalogue getAnimalHousing() {

        return animalHousing;
    }

    /**
     * Sets the animal housing.
     * @param animalHousing the new animal housing
     */
    public void setAnimalHousing(FarmCatalogue animalHousing) {

        this.animalHousing = animalHousing;
    }

    /**
     * Gets the animal count.
     * @return the animal count
     */
   // @Pattern(regex = "[0-9]+$", message = "pattern.animalCount")
    //@NotEmpty(message = "empty.animalCount")
    public String getAnimalCount() {

        return animalCount;
    }

    /**
     * Sets the animal count.
     * @param animalCount the new animal count
     */
    public void setAnimalCount(String animalCount) {

        this.animalCount = animalCount;
    }

    /**
     * Gets the production.
     * @return the production
     */
    public String getProduction() {

        return production;
    }

    /**
     * Sets the production.
     * @param production the new production
     */
    public void setProduction(String production) {

        this.production = production;
    }

    /**
     * Gets the fym estimated output.
     * @return the fym estimated output
     */
    public String getFymEstimatedOutput() {

        return fymEstimatedOutput;
    }

    /**
     * Sets the fym estimated output.
     * @param fymEstimatedOutput the new fym estimated output
     */
    public void setFymEstimatedOutput(String fymEstimatedOutput) {

        this.fymEstimatedOutput = fymEstimatedOutput;
    }

   

    public String getFodder() {
    
        return fodder;
    }

    public void setFodder(String fodder) {
    
        this.fodder = fodder;
    }

    /**
     * Gets the other fodder.
     * @return the other fodder
     */
    public String getOtherFodder() {

        return otherFodder;
    }

    /**
     * Sets the other fodder.
     * @param otherFodder the new other fodder
     */
    public void setOtherFodder(String otherFodder) {

        this.otherFodder = otherFodder;
    }

    /**
     * Gets the other animal housing.
     * @return the other animal housing
     */
    public String getOtherAnimalHousing() {

        return otherAnimalHousing;
    }

    /**
     * Sets the other animal housing.
     * @param otherAnimalHousing the new other animal housing
     */
    public void setOtherAnimalHousing(String otherAnimalHousing) {

        this.otherAnimalHousing = otherAnimalHousing;
    }

    /**
     * Gets the revenue.
     * @return the revenue
     */
    public String getRevenue() {
		return revenue;
	}

    /**
     * Sets the revenue.
     * @param revenue the new revenue
     */
	public void setRevenue(String revenue) {
		this.revenue = revenue;
	}

	/**
     * Gets the other revenue.
     * @return the other revenue
     */
    public String getOtherRevenue() {

        return otherRevenue;
    }

    /**
     * Sets the other revenue.
     * @param otherRevenue the new other revenue
     */
    public void setOtherRevenue(String otherRevenue) {

        this.otherRevenue = otherRevenue;
    }

	public String getInvItem1() {
		return invItem1;
	}

	public void setInvItem1(String invItem1) {
		this.invItem1 = invItem1;
	}

	public String getRevenueStr() {
		return revenueStr;
	}

	public void setRevenueStr(String revenueStr) {
		this.revenueStr = revenueStr;
	}

	public String getFodderStr() {
		return fodderStr;
	}

	public void setFodderStr(String fodderStr) {
		this.fodderStr = fodderStr;
	}

	public String getHousStr() {
		return housStr;
	}

	public void setHousStr(String housStr) {
		this.housStr = housStr;
	}

	public String getAnimalStr() {
		return animalStr;
	}

	public void setAnimalStr(String animalStr) {
		this.animalStr = animalStr;
	}

	public String getBreed() {
		return breed;
	}

	public void setBreed(String breed) {
		this.breed = breed;
	}

	public String getBreedStr() {
		return breedStr;
	}

	public void setBreedStr(String breedStr) {
		this.breedStr = breedStr;
	}

	public String getFarmId() {
		return farmId;
	}

	public void setFarmId(String farmId) {
		this.farmId = farmId;
	}

	public Farmer getFarmer() {
		return farmer;
	}

	public void setFarmer(Farmer farmer) {
		this.farmer = farmer;
	}

	public String getManureCollected() {
		return manureCollected;
	}

	public void setManureCollected(String manureCollected) {
		this.manureCollected = manureCollected;
	}

	public String getUrineCollected() {
		return urineCollected;
	}

	public void setUrineCollected(String urineCollected) {
		this.urineCollected = urineCollected;
	}
	
	
	
}
