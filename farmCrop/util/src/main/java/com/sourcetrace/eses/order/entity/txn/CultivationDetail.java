package com.sourcetrace.eses.order.entity.txn;

// TODO: Auto-generated Javadoc
public class CultivationDetail {
	
	
	public static final Long FERTILIZER_TYPE = 1L;
	public static final Long PESTICIDE_TYPE = 2L;
	public static final Long MANURE_TYPE = 3L;
	
	private long id;
	private String fertilizerType;
	private Cultivation cultivation;
	private Double cost;
	private Long type;
	private String usageLevel;
	private double qty;
	private String completed;
	private String uom;
	
	//Formula Query Variable
	private String fertilizerName; 
	private String usageLevelName;

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the fertilizer type.
	 * 
	 * @return the fertilizer type
	 */
	

	/**
	 * Gets the cultivation.
	 * 
	 * @return the cultivation
	 */
	public Cultivation getCultivation() {
		return cultivation;
	}

	/**
	 * Sets the cultivation.
	 * 
	 * @param cultivation
	 *            the new cultivation
	 */
	public void setCultivation(Cultivation cultivation) {
		this.cultivation = cultivation;
	}

	/**
	 * Gets the cost.
	 * 
	 * @return the cost
	 */
	public Double getCost() {
		return cost;
	}

	/**
	 * Sets the cost.
	 * 
	 * @param cost
	 *            the new cost
	 */
	public void setCost(Double cost) {
		this.cost = cost;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public Long getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(Long type) {
		this.type = type;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public String getFertilizerType() {
		return fertilizerType;
	}

	public void setFertilizerType(String fertilizerType) {
		this.fertilizerType = fertilizerType;
	}

    public String getUsageLevel() {
    
        return usageLevel;
    }

    public void setUsageLevel(String usageLevel) {
    
        this.usageLevel = usageLevel;
    }

    public String getUsageLevelName() {
    
        return usageLevelName;
    }

    public void setUsageLevelName(String usageLevelName) {
    
        this.usageLevelName = usageLevelName;
    }

	public String getFertilizerName() {
		return fertilizerName;
	}

	public void setFertilizerName(String fertilizerName) {
		this.fertilizerName = fertilizerName;
	}

	public String getCompleted() {
		return completed;
	}

	public void setCompleted(String completed) {
		this.completed = completed;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}
	

}
