package com.sourcetrace.esesw.entity.profile;

import com.sourcetrace.eses.util.profile.Product;

public class WarehouseStockReturnDetails 
{
	private long id;
	private Product product;
	private Double costPrice;
	private Long damagedStock;
	private double amount;
	private Long noOfStockReturned;
	private WarehouseStockReturn warehouseStockReturn;
	
	
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
     * Gets the product.
     * @return the product
     */
    public Product getProduct() {

        return product;
    }

    /**
     * Sets the product.
     * @param product the new product
     */
    public void setProduct(Product product) {

        this.product = product;
    }

    /**
     * Gets the cost price.
     * @return the cost price
     */
    public Double getCostPrice() {

        return costPrice;
    }

    /**
     * Sets the cost price.
     * @param costPrice the new cost price
     */
    public void setCostPrice(Double costPrice) {

        this.costPrice = costPrice;
    }
    
    /**
     * Gets the damaged stock.
     * @return the damaged stock
     */
    public Long getDamagedStock() {

        return damagedStock;
    }

    /**
     * Sets the damaged stock.
     * @param damagedStock the new damaged stock
     */
    public void setDamagedStock(Long damagedStock) {

        this.damagedStock = damagedStock;
    }


	 /**
    * Sets the Warehouse Stock Return.
    * param warehouseStockReturn
    */

	public void setWarehouseStockReturn(WarehouseStockReturn warehouseStockReturn) {
		this.warehouseStockReturn = warehouseStockReturn;
	}
	
	 /**
     * Gets the Warehouse Stock Return.
     * @return the Warehouse Stock Return
     */

	public WarehouseStockReturn getWarehouseStockReturn() {
		return warehouseStockReturn;
	}

    public double getAmount() {
    
        return amount;
    }

    public void setAmount(double amount) {
    
        this.amount = amount;
    }

    public Long getNoOfStockReturned() {
    
        return noOfStockReturned;
    }

    public void setNoOfStockReturned(Long noOfStockReturned) {
    
        this.noOfStockReturned = noOfStockReturned;
    }

  

}
