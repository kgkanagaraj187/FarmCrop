/*
 * FarmInventory.java
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
public class FarmInventory {

    private String id;
    private FarmCatalogue inventoryItem;
    private String otherInventoryItem;
    private String itemCount;
    private String farmId;
    //private Farm farm;
    private Farmer farmer;
    
    //Transient var
    private String invItem;
    
    public static final int INVENTORY_OTHER = 99;

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
     * Gets the inventory item.
     * @return the inventory item
     */
    public FarmCatalogue getInventoryItem() {

        return inventoryItem;
    }

    /**
     * Sets the inventory item.
     * @param inventoryItem the new inventory item
     */
    public void setInventoryItem(FarmCatalogue inventoryItem) {

        this.inventoryItem = inventoryItem;
    }

    /**
     * Gets the other inventory item.
     * @return the other inventory item
     */
   // @NotEmpty(message = "empty.otherInventoryItem")
    public String getOtherInventoryItem() {

        return otherInventoryItem;
    }

    /**
     * Sets the other inventory item.
     * @param otherInventoryItem the new other inventory item
     */
    public void setOtherInventoryItem(String otherInventoryItem) {

        this.otherInventoryItem = otherInventoryItem;
    }

    /**
     * Gets the item count.
     * @return the item count
     */
    //@Pattern(regex = "[0-9]+$", message = "pattern.itemCount")
    //@NotEmpty(message = "empty.itemCount")
    public String getItemCount() {

        return itemCount;
    }

    /**
     * Sets the item count.
     * @param itemCount the new item count
     */
    public void setItemCount(String itemCount) {

        this.itemCount = itemCount;
    }

	public String getInvItem() {
		return invItem;
	}

	public void setInvItem(String invItem) {
		this.invItem = invItem;
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
    
	
}
