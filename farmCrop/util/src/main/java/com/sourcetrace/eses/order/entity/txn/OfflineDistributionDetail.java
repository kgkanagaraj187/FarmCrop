package com.sourcetrace.eses.order.entity.txn;

public class OfflineDistributionDetail {

    private long id;
    private String productCode;
    private String quantity;
    private String pricePerUnit;
    private String subTotal;
    private double sellingPrice;
    private OfflineDistribution offlineDistribution;
    private String batchNo;

    /**
     * @param id the id to set
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * @param productCode the productCode to set
     */
    public void setProductCode(String productCode) {

        this.productCode = productCode;
    }

    /**
     * @return the productCode
     */
    public String getProductCode() {

        return productCode;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(String quantity) {

        this.quantity = quantity;
    }

    /**
     * @return the quantity
     */
    public String getQuantity() {

        return quantity;
    }

    /**
     * @param pricePerUnit the pricePerUnit to set
     */
    public void setPricePerUnit(String pricePerUnit) {

        this.pricePerUnit = pricePerUnit;
    }

    /**
     * @return the pricePerUnit
     */
    public String getPricePerUnit() {

        return pricePerUnit;
    }

    /**
     * @param subTotal the subTotal to set
     */
    public void setSubTotal(String subTotal) {

        this.subTotal = subTotal;
    }

    /**
     * @return the subTotal
     */
    public String getSubTotal() {

        return subTotal;
    }

    /**
     * @param offlineDistribution the offlineDistribution to set
     */
    public void setOfflineDistribution(OfflineDistribution offlineDistribution) {

        this.offlineDistribution = offlineDistribution;
    }

    /**
     * @return the offlineDistribution
     */
    public OfflineDistribution getOfflineDistribution() {

        return offlineDistribution;
    }

    public double getSellingPrice() {

        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {

        this.sellingPrice = sellingPrice;
    }

    public String getBatchNo() {

        return batchNo;
    }

    public void setBatchNo(String batchNo) {

        this.batchNo = batchNo;
    }
}
