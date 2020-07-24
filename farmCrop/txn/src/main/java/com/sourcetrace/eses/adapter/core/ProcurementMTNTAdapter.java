/*
 * ProcurementMTNTAdapter.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.MTNT;
import com.sourcetrace.eses.order.entity.txn.OfflineMTNT;
import com.sourcetrace.eses.order.entity.txn.OfflineMTNTDetail;
import com.sourcetrace.eses.order.entity.txn.PMT;
import com.sourcetrace.eses.order.entity.txn.PMTDetail;
import com.sourcetrace.eses.order.entity.txn.TransferInfo;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;
import com.sourcetrace.esesw.excep.SwitchException;
@Component
public class ProcurementMTNTAdapter implements ITxnAdapter {

    private static final Logger LOGGER = Logger.getLogger(ProcurementMTNTAdapter.class);
    @Autowired
    private ILocationService locationService;
    @Autowired
    private IAgentService agentService;
    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private IProductDistributionService productDistributionService;

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)
     */
    @Override
    public Map<?, ?> process(Map<?, ?> reqData) {

        /** GET REQUEST DATA **/
        Head head = (Head) reqData.get(TransactionProperties.HEAD);
        String date = (String) reqData.get(TxnEnrollmentProperties.MTNT_DATE);
        String receiptNo = (String) reqData.get(TxnEnrollmentProperties.RECEIPT_NO);
        String truckId = (String) reqData.get(TxnEnrollmentProperties.TRUCK_ID);
        String driverId = (String) reqData.get(TxnEnrollmentProperties.DRIVER_ID);
        String coOperativeCode = (String) reqData.get(TxnEnrollmentProperties.CO_OPERATIVE_CODE);
        String season = (String) reqData.get(TxnEnrollmentProperties.CURRENT_SEASON_CODE);
        String lat= (String) reqData.get(TxnEnrollmentProperties.LATITUDE);
        String lon= (String) reqData.get(TxnEnrollmentProperties.LONGITUDE);
        
        String labourCost = (reqData.containsKey(TxnEnrollmentProperties.LABOUR_COST))
                ? (String) reqData.get(TxnEnrollmentProperties.LABOUR_COST) : "";
        String transCost = (reqData.containsKey(TxnEnrollmentProperties.TRANS_COST))
                ? (String) reqData.get(TxnEnrollmentProperties.TRANS_COST) : "";
                
        String client = (reqData.containsKey(TxnEnrollmentProperties.CLIENT))
                        ? (String) reqData.get(TxnEnrollmentProperties.CLIENT) : "";
        String totalAmt = (reqData.containsKey(TxnEnrollmentProperties.TOTAL_AMOUNT))
                        ? (String) reqData.get(TxnEnrollmentProperties.TOTAL_AMOUNT) : "";
        String invoiceNo = (reqData.containsKey(TxnEnrollmentProperties.INVOICE_NO))
                        ? (String) reqData.get(TxnEnrollmentProperties.INVOICE_NO) : "";

        // If mode is online then do separate logic
        if (ESETxn.ONLINE_MODE == Integer.parseInt(head.getMode())) {
            /** VALIDATING REQUEST DATA **/
            // Agent
            Agent agent = agentService.findAgentByProfileId(head.getAgentId());
            if (ObjectUtil.isEmpty(agent))
                throw new SwitchException(SwitchErrorCodes.INVALID_AGENT);

            // Receipt #
            if (StringUtil.isEmpty(receiptNo))
                throw new SwitchException(SwitchErrorCodes.EMPTY_RECEIPT_ID);
            
            PMT pmt = productDistributionService.findPMTByReceiptNumber(receiptNo);
            if (!ObjectUtil.isEmpty(pmt))
                throw new SwitchException(SwitchErrorCodes.MTNT_EXIST);

            // Forming new PMT
            pmt = new PMT();
            pmt.setAgentRef(agent);
            pmt.setMtntReceiptNumber(receiptNo);

            // MTNT Date
            try {
                Date mtntDate = DateUtil.convertStringToDate(date, DateUtil.TXN_DATE_TIME);
                pmt.setMtntDate(mtntDate);
            } catch (Exception e) {
                throw new SwitchException(SwitchErrorCodes.INVALID_DATE_FORMAT);
            }

            // Truck Id
            if (StringUtil.isEmpty(truckId))
                throw new SwitchException(SwitchErrorCodes.EMPTY_TRUCK_ID);
            pmt.setTruckId(truckId);

            // Driver Id
            if (StringUtil.isEmpty(driverId))
                throw new SwitchException(SwitchErrorCodes.EMPTY_DRIVER_ID);
            pmt.setDriverName(driverId);

            // CoOperative
            if (StringUtil.isEmpty(coOperativeCode))
                throw new SwitchException(SwitchErrorCodes.EMPTY_CO_OPERATIVE_CODE);
            Warehouse coOperative = locationService.findCoOperativeByCode(coOperativeCode);
            if (ObjectUtil.isEmpty(coOperative))
                throw new SwitchException(SwitchErrorCodes.CO_OPERATIVE_NOT_EXIST);
            pmt.setCoOperative(coOperative);
            //Branch id
            if(head.getBranchId()!= null && head.getBranchId() != "" ){
            pmt.setBranchId(head.getBranchId());
            }else{
            	pmt.setBranchId("");
            }

            Map<String, String> villageMap = locationService.getVillageMappedWithAgent(agent.getWareHouses());
            // Forming PMT Details
            Collection collection = (Collection) reqData.get(TxnEnrollmentProperties.PROCUREMENT_MTNT_PRODUCTS);
            if (!ObjectUtil.isEmpty(collection)) {
                List<Object> objectList = collection.getObject();
                if (!ObjectUtil.isEmpty(objectList) && objectList.size() > 0) {
                    Set<PMTDetail> pmtDetails = new LinkedHashSet<PMTDetail>();
                    for (Object object : objectList) {
                        List<Data> mtntDataList = object.getData();
                        PMTDetail pmtDetail = new PMTDetail();
                        // ProcurementProduct product = null;
                        // GradeMaster gradeMaster = null;
                        ProcurementGrade procurementGrade = null;
                        for (Data mtntData : mtntDataList) {
                            String key = mtntData.getKey();
                            String value = mtntData.getValue();
                            // Product
                            /*
                            if (TxnEnrollmentProperties.PRODUCT_CODE.equalsIgnoreCase(key)) {
                                if (StringUtil.isEmpty(value))
                                    throw new SwitchException(SwitchErrorCodes.EMPTY_PRODUCT_CODE);
                                product = productDistributionService.findProcurementProductByCode(value);
                                if (ObjectUtil.isEmpty(product)) {
                                    throw new SwitchException(SwitchErrorCodes.PRODUCT_DOES_NOT_EXIST);
                                }
                                pmtDetail.setProcurementProduct(product);
                            }
                            */
                            // Village
                            if (TxnEnrollmentProperties.PROCUREMENT_VILLAGE_CODE.equalsIgnoreCase(key)) {
                                if (StringUtil.isEmpty(value))
                                    throw new SwitchException(SwitchErrorCodes.EMPTY_VILLAGE_CODE);
                                Village village = locationService.findVillageByCode(value);
                                if (ObjectUtil.isEmpty(village))
                                    throw new SwitchException(SwitchErrorCodes.VILLAGE_NOT_EXIST);

                                // Validation to check whether village comes under agent samithi
                               /* if (!villageMap.containsKey(value))
                                    throw new SwitchException(SwitchErrorCodes.VILLAGE_NOT_MAPPED_WITH_SAMITHI);*/
                                
                                pmtDetail.setVillage(village);
                            }
                            // No Of Bags
                            if (TxnEnrollmentProperties.NO_OF_BAGS.equalsIgnoreCase(key)) {
                                try {
                                    pmtDetail.setMtntNumberOfBags(Long.valueOf(value));
                                    //pmtDetail.setMtnrNumberOfBags(Long.valueOf(value));
                                } catch (Exception e) {
                                    throw new SwitchException(SwitchErrorCodes.INVALID_NO_OF_BAGS);
                                }
                            }
                            // Gross Weight
                            if (TxnEnrollmentProperties.GROSS_WEIGHT.equalsIgnoreCase(key)) {
                                try {
                                    pmtDetail.setMtntGrossWeight(Double.valueOf(value));
                                    //pmtDetail.setMtnrGrossWeight(Double.valueOf(value));
                                } catch (Exception e) {
                                    throw new SwitchException(SwitchErrorCodes.INVALID_GROSS_WEIGHT);
                                }
                            }
                            // Grade
                            if (TxnEnrollmentProperties.GRADE_CODE.equalsIgnoreCase(key)) {
                                if (StringUtil.isEmpty(value))
                                    throw new SwitchException(SwitchErrorCodes.EMPTY_GRADE_CODE);
                                procurementGrade = productDistributionService.findProcurementGradeByCode(value);
                                if (ObjectUtil.isEmpty(procurementGrade))
                                    throw new SwitchException(SwitchErrorCodes.GRADE_DOES_NOT_EXIST);
                                pmtDetail.setProcurementGrade(procurementGrade);
                                pmtDetail.setProcurementProduct(ObjectUtil.isEmpty(procurementGrade.getProcurementVariety()) ? null : ObjectUtil.isEmpty(procurementGrade.getProcurementVariety().getProcurementProduct())? null : procurementGrade.getProcurementVariety().getProcurementProduct());
                            }
                        }
                        
                        /*
                        if (gradeMaster.getProduct().getId() != product.getId())// Product-Grade Mapping
                            throw new SwitchException(SwitchErrorCodes.PRODUCT_VARIETY_MISMATCH);
                        */
                        
                        pmtDetail.setPmt(pmt);
                        pmtDetails.add(pmtDetail);
                    }
                    pmt.setPmtDetails(pmtDetails);
                    pmt.setMtntTransferInfo(getTransferInfo(head));
                    pmt.setStatusCode(PMT.Status.MTNT.ordinal());
                    pmt.setTrnType(PMT.TRN_TYPE_OTEHR);
                    pmt.setSeasonCode(season);
                    pmt.setLatitude(lat);
                    pmt.setLongitude(lon);
                    // Saving PMT and PMT Details
                    productDistributionService.addProcurementMTNT(pmt);
                } else {
                    throw new SwitchException(SwitchErrorCodes.PROCUREMENT_MENT_PRODUCTS_NOT_FOUND);
                }
            }
        } else if (ESETxn.OFFLINE_MODE == Integer.parseInt(head.getMode())) {
            /** UPDATE OFFLINE RECIPT NO IN AGENT OBJECT **/
            if (!StringUtil.isEmpty(receiptNo)) {
                agentService.updateAgentReceiptNoSequence(head.getAgentId(), receiptNo);
            }
            try {
                // If mode is offline then save object and send success as response
                OfflineMTNT offlineMTNT = new OfflineMTNT();
                offlineMTNT.setAgentId(head.getAgentId());
                offlineMTNT.setDeviceId(head.getSerialNo());
                offlineMTNT.setServicePointId(head.getServPointId());
                offlineMTNT.setReceiptNo(receiptNo);
                offlineMTNT.setWarehouseCode(coOperativeCode);
                offlineMTNT.setTruckId(truckId);
                offlineMTNT.setDriverId(driverId);
                offlineMTNT.setType(MTNT.Type.MTNT.ordinal());
                offlineMTNT.setMtntDate((String) reqData.get(TxnEnrollmentProperties.MTNT_DATE));
                offlineMTNT.setBranchId(head.getBranchId());
                offlineMTNT.setSeasonCode(season);
                offlineMTNT.setLatitude(lat);
                offlineMTNT.setLongitude(lon);
                offlineMTNT.setTotalLabourCost(Double.valueOf(!StringUtil.isEmpty(labourCost) ? labourCost : "0.0"));
                offlineMTNT.setTransportCost(Double.valueOf(!StringUtil.isEmpty(transCost) ? transCost : "0.0"));
                
                offlineMTNT.setClient(!StringUtil.isEmpty(client) ? client : "");
                offlineMTNT.setTotalAmt(Double.valueOf(!StringUtil.isEmpty(totalAmt) ? totalAmt : "0.0"));
                offlineMTNT.setInvoiceNo(invoiceNo);

                Set<OfflineMTNTDetail> offlineMTNTDetails = new HashSet<OfflineMTNTDetail>();

                Collection collection = (Collection) reqData.get(TxnEnrollmentProperties.PROCUREMENT_MTNT_PRODUCTS);
                List<Object> mtntObjects = collection.getObject();

                for (Object mtntObject : mtntObjects) {
                    OfflineMTNTDetail offlineMTNTDetail = new OfflineMTNTDetail();
                    List<Data> mtntDataList = mtntObject.getData();
                    for (Data mtntData : mtntDataList) {

                        String key = mtntData.getKey();
                        String value = mtntData.getValue();

                        if (TxnEnrollmentProperties.PRODUCT_CODE.equalsIgnoreCase(key)) {
                            offlineMTNTDetail.setProductCode(value);
                        }
                        if (TxnEnrollmentProperties.PROCUREMENT_VILLAGE_CODE.equalsIgnoreCase(key)) {
                            offlineMTNTDetail.setVillageCode(value);
                        }
                        if (TxnEnrollmentProperties.NO_OF_BAGS.equalsIgnoreCase(key)) {
                            offlineMTNTDetail.setNumberOfBags(value);
                        }
                        if (TxnEnrollmentProperties.GROSS_WEIGHT.equalsIgnoreCase(key)) {
                            offlineMTNTDetail.setNetWeight(value);
                            offlineMTNTDetail.setTareWeight("0");
                            offlineMTNTDetail.setGrossWeight(value);
                        }
                        if (TxnEnrollmentProperties.GRADE_CODE.equalsIgnoreCase(key)) {
                            offlineMTNTDetail.setQuality(value);
                        }
                        if (TxnEnrollmentProperties.PRICE_PER_UNIT.equalsIgnoreCase(key)) {
                            offlineMTNTDetail.setPricePerUnit(value);
                            }
                            if (TxnEnrollmentProperties.SUB_TOTAL.equalsIgnoreCase(key)) {
                            offlineMTNTDetail.setSubTotal(value);
                            }
                            if (TxnEnrollmentProperties.PROCURMENT_UNIT.equalsIgnoreCase(key)) {
                            offlineMTNTDetail.setUom(value);
                            }
                    }
                    offlineMTNTDetail.setOfflineMTNT(offlineMTNT);
                    offlineMTNTDetails.add(offlineMTNTDetail);
                }
                // sets set of offlineMtntdetail object to offlineMtnt
                offlineMTNT.setOfflineMTNTDetails(offlineMTNTDetails);

                offlineMTNT.setStatusCode(ESETxnStatus.PENDING.ordinal());
                offlineMTNT.setStatusMessage(ESETxnStatus.PENDING.toString());

                // save the object
                this.productDistributionService.addOfflineMTNT(offlineMTNT);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error(e.getMessage());
            }
        }
        /** FORM RESPONSE DATA **/
        Map<String, Object> resp = new LinkedHashMap<String, Object>();
        return resp;
    }

    private TransferInfo getTransferInfo(Head head) {

        TransferInfo transferInfo = new TransferInfo();
        Agent agent = agentService.findAgentByProfileId(head.getAgentId());
        if (!ObjectUtil.isEmpty(agent)) {
            transferInfo.setAgentId(agent.getProfileId());
            transferInfo.setAgentName((ObjectUtil.isEmpty(agent.getPersonalInfo()) ? "" : agent.getPersonalInfo().getAgentName()));
            transferInfo.setServicePointId((ObjectUtil.isEmpty(agent.getServicePoint()) ? "" : agent.getServicePoint().getCode()));
            transferInfo.setServicePointName((ObjectUtil.isEmpty(agent.getServicePoint()) ? "" : agent.getServicePoint().getName()));
        }
        Device device = deviceService.findDeviceBySerialNumber(head.getSerialNo());
        if (!ObjectUtil.isEmpty(device)) {
            transferInfo.setDeviceId(device.getCode());
            transferInfo.setDeviceName(device.getName());
        }
        transferInfo.setOperationType(ESETxn.ON_LINE);
        return transferInfo;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#processVoid(java.util.Map)
     */
    @Override
    public Map<?, ?> processVoid(Map<?, ?> reqData) {

        return null;
    }

   

}