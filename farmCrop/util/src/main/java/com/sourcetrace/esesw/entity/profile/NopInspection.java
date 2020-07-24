package com.sourcetrace.esesw.entity.profile;

import java.util.Set;

public class NopInspection {

	
	private long id;
	private long farmId;
	private double acresOwned;
	private double acresFarmed;
	private double acresOrganic;
	private double acresTransition;
	private double acresEligibleNext;
	private double acresReqIns;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public double getAcresOwned() {
		return acresOwned;
	}
	public void setAcresOwned(double acresOwned) {
		this.acresOwned = acresOwned;
	}
	public double getAcresFarmed() {
		return acresFarmed;
	}
	public void setAcresFarmed(double acresFarmed) {
		this.acresFarmed = acresFarmed;
	}
	public double getAcresOrganic() {
		return acresOrganic;
	}
	public void setAcresOrganic(double acresOrganic) {
		this.acresOrganic = acresOrganic;
	}
	public double getAcresTransition() {
		return acresTransition;
	}
	public void setAcresTransition(double acresTransition) {
		this.acresTransition = acresTransition;
	}
	public double getAcresEligibleNext() {
		return acresEligibleNext;
	}
	public void setAcresEligibleNext(double acresEligibleNext) {
		this.acresEligibleNext = acresEligibleNext;
	}
	public double getAcresReqIns() {
		return acresReqIns;
	}
	public void setAcresReqIns(double acresReqIns) {
		this.acresReqIns = acresReqIns;
	}
	public long getFarmId() {
		return farmId;
	}
	public void setFarmId(long farmId) {
		this.farmId = farmId;
	}
	
	
	
}
