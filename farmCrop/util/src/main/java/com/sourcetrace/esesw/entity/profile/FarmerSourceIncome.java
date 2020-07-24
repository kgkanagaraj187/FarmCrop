package com.sourcetrace.esesw.entity.profile;

import java.util.List;
import java.util.Set;

public class FarmerSourceIncome
{
	private Long id;
	private String name;
	private Long farmerId;
	private Set<FarmerIncomeDetails> farmerIncomeDetails;
	
	//Transient
	private  List<FarmerIncomeDetails> incomeDetailsList ;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<FarmerIncomeDetails> getFarmerIncomeDetails() {
		return farmerIncomeDetails;
	}
	public void setFarmerIncomeDetails(Set<FarmerIncomeDetails> farmerIncomeDetails) {
		this.farmerIncomeDetails = farmerIncomeDetails;
	}
	public Long getFarmerId() {
		return farmerId;
	}
	public void setFarmerId(Long farmerId) {
		this.farmerId = farmerId;
	}
	
	
	public List<FarmerIncomeDetails> getFarmerIncomeDetailsList() {

        return farmerIncomeDetailsList;
    }
    public void setFarmerIncomeDetailsList(List<FarmerIncomeDetails> farmerIncomeDetailsList) {

        this.farmerIncomeDetailsList = farmerIncomeDetailsList;
    }


    private List<FarmerIncomeDetails> farmerIncomeDetailsList;
	public List<FarmerIncomeDetails> getIncomeDetailsList() {
		return incomeDetailsList;
	}
	public void setIncomeDetailsList(List<FarmerIncomeDetails> incomeDetailsList) {
		this.incomeDetailsList = incomeDetailsList;
	}
	
	
}
