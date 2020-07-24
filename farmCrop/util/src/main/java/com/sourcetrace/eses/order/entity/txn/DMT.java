/*
 * DMT.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;
import java.util.Set;

import com.sourcetrace.eses.entity.Warehouse;

public class DMT {

    public static enum Status {
        NONE, MTNT, MTNR, COMPLETE
    }

    private long id;
    private Date mtntDate;
    private Date mtnrDate;
    private String mtntReceiptNo;
    private String mtnrReceiptNo;
    private Warehouse senderWarehouse;
    private Warehouse receiverWarehouse;
    private TransferInfo senderTransferInfo;
    private TransferInfo receiverTransferInfo;
    private String truckId;
    private String driverName;
    private int status;
    private Set<DMTDetail> dmtDetails;

    // transient variable
    private String transientType;

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Gets the id.
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Sets the mtnt date.
     * @param mtntDate the new mtnt date
     */
    public void setMtntDate(Date mtntDate) {

        this.mtntDate = mtntDate;
    }

    /**
     * Gets the mtnt date.
     * @return the mtnt date
     */
    public Date getMtntDate() {

        return mtntDate;
    }

    /**
     * Sets the mtnr date.
     * @param mtnrDate the new mtnr date
     */
    public void setMtnrDate(Date mtnrDate) {

        this.mtnrDate = mtnrDate;
    }

    /**
     * Gets the mtnr date.
     * @return the mtnr date
     */
    public Date getMtnrDate() {

        return mtnrDate;
    }

    /**
     * Sets the mtnt receipt no.
     * @param mtntReceiptNo the new mtnt receipt no
     */
    public void setMtntReceiptNo(String mtntReceiptNo) {

        this.mtntReceiptNo = mtntReceiptNo;
    }

    /**
     * Gets the mtnt receipt no.
     * @return the mtnt receipt no
     */
    public String getMtntReceiptNo() {

        return mtntReceiptNo;
    }

    /**
     * Sets the mtnr receipt no.
     * @param mtnrReceiptNo the new mtnr receipt no
     */
    public void setMtnrReceiptNo(String mtnrReceiptNo) {

        this.mtnrReceiptNo = mtnrReceiptNo;
    }

    /**
     * Gets the mtnr receipt no.
     * @return the mtnr receipt no
     */
    public String getMtnrReceiptNo() {

        return mtnrReceiptNo;
    }

    /**
     * Sets the sender warehouse.
     * @param senderWarehouse the new sender warehouse
     */
    public void setSenderWarehouse(Warehouse senderWarehouse) {

        this.senderWarehouse = senderWarehouse;
    }

    /**
     * Gets the sender warehouse.
     * @return the sender warehouse
     */
    public Warehouse getSenderWarehouse() {

        return senderWarehouse;
    }

    /**
     * Sets the receiver transfer info.
     * @param receiverTransferInfo the new receiver transfer info
     */
    public void setReceiverTransferInfo(TransferInfo receiverTransferInfo) {

        this.receiverTransferInfo = receiverTransferInfo;
    }

    /**
     * Gets the receiver transfer info.
     * @return the receiver transfer info
     */
    public TransferInfo getReceiverTransferInfo() {

        return receiverTransferInfo;
    }

    /**
     * Sets the receiver warehouse.
     * @param receiverWarehouse the new receiver warehouse
     */
    public void setReceiverWarehouse(Warehouse receiverWarehouse) {

        this.receiverWarehouse = receiverWarehouse;
    }

    /**
     * Gets the receiver warehouse.
     * @return the receiver warehouse
     */
    public Warehouse getReceiverWarehouse() {

        return receiverWarehouse;
    }

    /**
     * Sets the sender transfer info.
     * @param senderTransferInfo the new sender transfer info
     */
    public void setSenderTransferInfo(TransferInfo senderTransferInfo) {

        this.senderTransferInfo = senderTransferInfo;
    }

    /**
     * Gets the sender transfer info.
     * @return the sender transfer info
     */
    public TransferInfo getSenderTransferInfo() {

        return senderTransferInfo;
    }

    /**
     * Sets the truck id.
     * @param truckId the new truck id
     */
    public void setTruckId(String truckId) {

        this.truckId = truckId;
    }

    /**
     * Gets the truck id.
     * @return the truck id
     */
    public String getTruckId() {

        return truckId;
    }

    /**
     * Sets the driver name.
     * @param driverName the new driver name
     */
    public void setDriverName(String driverName) {

        this.driverName = driverName;
    }

    /**
     * Gets the driver name.
     * @return the driver name
     */
    public String getDriverName() {

        return driverName;
    }

    /**
     * Sets the status.
     * @param status the new status
     */
    public void setStatus(int status) {

        this.status = status;
    }

    /**
     * Gets the status.
     * @return the status
     */
    public int getStatus() {

        return status;
    }

    /**
     * Sets the dmt details.
     * @param dmtDetails the new dmt details
     */
    public void setDmtDetails(Set<DMTDetail> dmtDetails) {

        this.dmtDetails = dmtDetails;
    }

    /**
     * Gets the dmt details.
     * @return the dmt details
     */
    public Set<DMTDetail> getDmtDetails() {

        return dmtDetails;
    }

    /**
     * Gets the transient type.
     * @return the transient type
     */
    public String getTransientType() {

        return transientType;
    }

    /**
     * Sets the transient type.
     * @param transientType the new transient type
     */
    public void setTransientType(String transientType) {

        this.transientType = transientType;
    }

}
