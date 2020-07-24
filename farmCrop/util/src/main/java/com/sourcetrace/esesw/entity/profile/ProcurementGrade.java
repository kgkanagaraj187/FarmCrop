/*
 * ProcurementProduct.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import java.util.Set;

import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.util.StringUtil;

// TODO: Auto-generated Javadoc
public class ProcurementGrade {
	
    private long id;
    private ProcurementVariety procurementVariety;
    private String code;
    private String name;
    private Double price;
    private Long revisionNo;
    private String unit;
    private FarmCatalogue type;
    private Set<ProcurementGradePricingHistory> priceHistories;
    
    //TransientVariable
    
    private String pricePrefix;
    private String priceSuffix;

    /**
     * Gets the id.
     * @return the id
     */
    public Long getId() {

        return id;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(Long id) {

        this.id = id;
    }

    /**
     * Gets the procurement variety.
     * @return the procurement variety
     */
    public ProcurementVariety getProcurementVariety() {

        return procurementVariety;
    }

    /**
     * Sets the procurement variety.
     * @param procurementVariety the new procurement variety
     */
    public void setProcurementVariety(ProcurementVariety procurementVariety) {

        this.procurementVariety = procurementVariety;
    }

    /**
     * Gets the code.
     * @return the code
     */
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
     * Gets the price.
     * @return the price
     */
    public Double getPrice() {

        return price;
    }

    /**
     * Sets the price.
     * @param price the new price
     */
    public void setPrice(Double price) {

        this.price = price;
        this.pricePrefix = getStringValueFromDouble(this.price, 0);
        this.priceSuffix = getStringValueFromDouble(this.price, 1);
    }

    /**
     * Gets the revision no.
     * @return the revision no
     */
    public Long getRevisionNo() {

        return revisionNo;
    }

    /**
     * Sets the revision no.
     * @param revisionNo the new revision no
     */
    public void setRevisionNo(Long revisionNo) {

        this.revisionNo = revisionNo;
    }

    /**
     * Gets the price histories.
     * @return the price histories
     */
    public Set<ProcurementGradePricingHistory> getPriceHistories() {

        return priceHistories;
    }

    /**
     * Sets the price histories.
     * @param priceHistories the new price histories
     */
    public void setPriceHistories(Set<ProcurementGradePricingHistory> priceHistories) {

        this.priceHistories = priceHistories;
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

    
    public void processPriceFromString() {

        this.price = getDoubleValueFromString(this.pricePrefix,
                this.priceSuffix);
    }
    public void processPrice() {
        processPriceFromString();

    }
    public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	

	public FarmCatalogue getType() {
		return type;
	}

	public void setType(FarmCatalogue type) {
		this.type = type;
	}

	private String getStringValueFromDouble(double fromValue, int index) {

        String str = "";
        try {
            //if (fromValue > 0) {
            String fullDouble = String.valueOf(fromValue);
            String[] arr = fullDouble.split("\\.");
            if (arr.length == 2) {
                str = arr[index];
            }
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    private double getDoubleValueFromString(String str1, String str2) {

        double toDoubleValue = 0d;
        try {

            StringBuffer sb = new StringBuffer();
            if (!StringUtil.isEmpty(str1)) {
                sb.append(str1);
            }
            if (!StringUtil.isEmpty(str2)) {
                sb.append(".");
                sb.append(str2);
            }
            if (!StringUtil.isEmpty(sb.toString())) {
                toDoubleValue = Double.valueOf(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toDoubleValue;
    }  
}
