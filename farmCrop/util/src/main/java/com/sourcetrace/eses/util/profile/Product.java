/*
 * Product.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

package com.sourcetrace.eses.util.profile;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.order.entity.profile.SubCategory;
import com.sourcetrace.eses.util.First;



/**
 * The Class Product.
 */
public class Product {

    public static final int MAX_LENGTH_NAME = 50;
    public static final String PRICE_EXPRESSION = "[0-9.]*";
    private long id;
    private String code;
    private String name;
    private SubCategory subcategory;
    private String price;
    private String unit;
    private long revisionNo;
	private FarmCatalogue type;
	public static final int listUnitBasedOnUOM=10;
	public String manufacture;
	public String ingredient;
//TransientVariable
    
    private String pricePrefix;
    private String priceSuffix;
    
 

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
     * Gets the code.
     * @return the code
     */
    @Pattern(groups = First.class,regexp = "[^\\p{Punct}]+$", message = "pattern.code")
    @NotEmpty(message = "empty.code")
    public String getCode() {

        return code;
    }

    /**
     * Sets the code.
     * @param code the new code
     */
    public void setCode(String code) {

        this.code = code;
    }

    /**
     * Gets the name.
     * @return the name
     */
    @Length(max = MAX_LENGTH_NAME, message = "length.name")
    @NotEmpty(message = "empty.name")
    public String getName() {

        return name;
    }

    /**
     * Sets the name.
     * @param name the new name
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Sets the subcategory.
     * @param subcategory the new subcategory
     */
    public void setSubcategory(SubCategory subcategory) {

        this.subcategory = subcategory;
    }

    /**
     * Gets the subcategory.
     * @return the subcategory
     */
    public SubCategory getSubcategory() {

        return subcategory;
    }

    /**
     * Sets the price.
     * @param price the new price
     */
    public void setPrice(String price) {

        this.price = price;
    }

    /**
     * Gets the price.
     * @return the price
     */

    
    public String getPrice() {

        return price;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return name;
    }

    /**
     * Sets the unit.
     * @param unit the new unit
     */
    public void setUnit(String unit) {

        this.unit = unit;
    }

    /**
     * Gets the unit.
     * @return the unit
     */
  /*  @Pattern(groups = First.class,regexp = "[0-9]+$", message = "pattern.unit")
    @NotEmpty(message = "empty.unit")*/
    public String getUnit() {

        return unit;
    }

    /**
     * Gets the revision no.
     * @return the revision no
     */
    public long getRevisionNo() {

        return revisionNo;
    }

    /**
     * Sets the revision no.
     * @param revisionNo the new revision no
     */
    public void setRevisionNo(long revisionNo) {

        this.revisionNo = revisionNo;
    }

    public String getPricePrefix() {
    
        return pricePrefix;
    }

    public void setPricePrefix(String pricePrefix) {
    
        this.pricePrefix = pricePrefix;
    }

    public String getPriceSuffix() {
    
        return priceSuffix;
    }

    public void setPriceSuffix(String priceSuffix) {
    
        this.priceSuffix = priceSuffix;
    }

	public FarmCatalogue getType() {
		return type;
	}

	public void setType(FarmCatalogue type) {
		this.type = type;
	}

	public String getManufacture() {
		return manufacture;
	}

	public void setManufacture(String manufacture) {
		this.manufacture = manufacture;
	}

	public String getIngredient() {
		return ingredient;
	}

	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}

	

    
}
