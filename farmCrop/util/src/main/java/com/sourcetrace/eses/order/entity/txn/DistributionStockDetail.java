/*
 * DistributionDetail.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.io.Serializable;
import java.util.List;

import com.sourcetrace.eses.util.profile.Product;

public class DistributionStockDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    private long id;
    private Product product;
    private DistributionStock distributionStock;
    private double distributionQuantity;
    private double damageQuantity;
    private double totalQuantity;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public DistributionStock getDistributionStock() {
		return distributionStock;
	}
	public void setDistributionStock(DistributionStock distributionStock) {
		this.distributionStock = distributionStock;
	}
	public double getDistributionQuantity() {
		return distributionQuantity;
	}
	public void setDistributionQuantity(double distributionQuantity) {
		this.distributionQuantity = distributionQuantity;
	}
	
	public double getDamageQuantity() {
		return damageQuantity;
	}
	public void setDamageQuantity(double damageQuantity) {
		this.damageQuantity = damageQuantity;
	}
	public double getTotalQuantity() {
		return totalQuantity;
	}
	public void setTotalQuantity(double totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	
	
    
   

}
