package com.sourcetrace.eses.order.entity.txn;

import java.io.Serializable;
import java.util.Date;

public class ProductPendingDetail implements Serializable {
	private long id;
	private String shopDealerId;
	private String shopDealerName;
	private String orderNo;
	private Date orderDate;	
	private double orderQty;
	private double deliveryQty;
	private double pendingQty;
	private long productId;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getShopDealerId() {
		return shopDealerId;
	}
	public void setShopDealerId(String shopDealerId) {
		this.shopDealerId = shopDealerId;
	}
	public String getShopDealerName() {
		return shopDealerName;
	}
	public void setShopDealerName(String shopDealerName) {
		this.shopDealerName = shopDealerName;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}	
	public double getOrderQty() {
		return orderQty;
	}
	public void setOrderQty(double orderQty) {
		this.orderQty = orderQty;
	}
	public double getDeliveryQty() {
		return deliveryQty;
	}
	public void setDeliveryQty(double deliveryQty) {
		this.deliveryQty = deliveryQty;
	}
	public double getPendingQty() {
		return pendingQty;
	}
	public void setPendingQty(double pendingQty) {
		this.pendingQty = pendingQty;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	
}
