package com.sourcetrace.eses.order.entity.profile;

import java.util.Set;

public class PaymentImageInfo {
	 private long id;
	 private String txnType;
	 private Set<PaymentImage> paymentImages;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public Set<PaymentImage> getPaymentImages() {
		return paymentImages;
	}
	public void setPaymentImages(Set<PaymentImage> paymentImages) {
		this.paymentImages = paymentImages;
	} 
}
