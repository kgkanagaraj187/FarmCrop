package com.ese.entity.util;

public class LoanInterest {
	
    private long id; 
	private long minRange;
	private long maxRange;
	private double interest;
	private ESESystem ese;

	

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getMinRange() {
		return minRange;
	}
	public void setMinRange(long minRange) {
		this.minRange = minRange;
	}
	public long getMaxRange() {
		return maxRange;
	}
	public void setMaxRange(long maxRange) {
		this.maxRange = maxRange;
	}
	public double getInterest() {
		return interest;
	}
	public void setInterest(double interest) {
		this.interest = interest;
	}
	public ESESystem getEse() {
		return ese;
	}
	public void setEse(ESESystem ese) {
		this.ese = ese;
	}


}
