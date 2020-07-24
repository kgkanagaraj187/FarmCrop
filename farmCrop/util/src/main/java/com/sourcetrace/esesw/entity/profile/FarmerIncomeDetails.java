package com.sourcetrace.esesw.entity.profile;

public class FarmerIncomeDetails {
    private Long id;
    private String sourceName;
    private double sourceIncome;
    private Long farmerSourceId;
    private String sourceNameOther;

    /* Transient */
    private String name;

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public String getSourceName() {

        return sourceName;
    }

    public void setSourceName(String sourceName) {

        this.sourceName = sourceName;
    }

    public double getSourceIncome() {

        return sourceIncome;
    }

    public void setSourceIncome(double sourceIncome) {

        this.sourceIncome = sourceIncome;
    }

    public Long getFarmerSourceId() {

        return farmerSourceId;
    }

    public void setFarmerSourceId(Long farmerSourceId) {

        this.farmerSourceId = farmerSourceId;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

	public String getSourceNameOther() {
		return sourceNameOther;
	}

	public void setSourceNameOther(String sourceNameOther) {
		this.sourceNameOther = sourceNameOther;
	}

    
}
