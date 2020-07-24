package com.sourcetrace.eses.adapter.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.order.entity.txn.OfflineBaleGeneration;
import com.sourcetrace.eses.order.entity.txn.OfflinePMTImageDetails;
import com.sourcetrace.eses.order.entity.txn.OfflinePMTNR;
import com.sourcetrace.eses.order.entity.txn.OfflinePMTNRDetail;
import com.sourcetrace.eses.order.entity.txn.PMT;
import com.sourcetrace.eses.order.entity.txn.PMTDetail;
import com.sourcetrace.eses.order.entity.txn.PMTImageDetails;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.OfflineTransactionException;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;

@Component
public class ProductReceptionAdapter implements ITxnAdapter {
	private static final Logger LOGGER = Logger.getLogger(TransferStockDownload.class.getName());
	@Autowired
	private IAgentService agentService;
	@Autowired
	private IProductDistributionService productDistributionService;
	@Autowired
	protected IClientService clientService;
	@Autowired
	private IDeviceService deviceService;
	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {
		// TODO Auto-generated method stub
		Head head = (Head) reqData.get(TransactionProperties.HEAD);
		String messageNo=head.getMsgNo();
		OfflinePMTNR exist=productDistributionService.findOfflinePMTNRByMessageNo(messageNo);
		if(exist==null || ObjectUtil.isEmpty(exist)){
		String date = (String) reqData.get(TxnEnrollmentProperties.MTNT_DATE);
		String receiptNo = (String) reqData.get(TransactionProperties.TRANSFER_RECEIPT_NO);
		String mtnrreceiptNo = (String) reqData.get(TxnEnrollmentProperties.RECEIPT_NO);
		DataHandler photo1 = (DataHandler) reqData.get(TxnEnrollmentProperties.PMT_PHOTO_1);

		DataHandler photo2 = (DataHandler) reqData.get(TxnEnrollmentProperties.PMT_PHOTO_2);
		String lat1 = (String) reqData.get(TxnEnrollmentProperties.PMT_LAT_1);
		String lat2 = (String) reqData.get(TxnEnrollmentProperties.PMT_LAT_2);
		String lon1 = (String) reqData.get(TxnEnrollmentProperties.PMT_LON_1);
		String lon2 = (String) reqData.get(TxnEnrollmentProperties.PMT_LON_2);

		String capTime1 = (String) reqData.get(TransactionProperties.PMT_CAT_TIME1);
		String capTime2 = (String) reqData.get(TransactionProperties.PMT_CAT_TIME2);

			/** VALIDATING REQUEST DATA **/
			// Agent
			Agent agent = agentService.findAgentByProfileId(head.getAgentId());
			if (ObjectUtil.isEmpty(agent))
				throw new SwitchException(SwitchErrorCodes.INVALID_AGENT);
		

			// Receipt #
			if (StringUtil.isEmpty(receiptNo))
				throw new SwitchException(SwitchErrorCodes.EMPTY_RECEIPT_ID);

			PMT pmt = productDistributionService.findPMTByReceiptNumber(receiptNo);
			if (ObjectUtil.isEmpty(pmt))
				throw new SwitchException(SwitchErrorCodes.MTNR_NOT_EXIST);

			OfflinePMTNR offlinePMTNR = new OfflinePMTNR();
			Set<OfflinePMTNRDetail> pmtDetails = new HashSet<OfflinePMTNRDetail>();
			try {
				offlinePMTNR.setMtnrDate(date);
			} catch (Exception e) {
				throw new SwitchException(SwitchErrorCodes.INVALID_DATE_FORMAT);
			}

			if (StringUtil.isEmpty(head.getSerialNo()))
				try {
					throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_SERIAL_NO);
				} catch (Exception e) {
					// TODO: handle exception

				}
			offlinePMTNR.setMessageNo(messageNo);
			offlinePMTNR.setAgentId(head.getAgentId());
			offlinePMTNR.setCreatedDated(new Date());
			offlinePMTNR.setCreatedUser(head.getAgentId());
			offlinePMTNR.setUpdatedUser(head.getAgentId());
			offlinePMTNR.setCoOperativeCode(pmt.getPmtDetails().iterator().next().getCoOperative().getCode());
			offlinePMTNR.setTruckId(pmt.getTruckId());
			offlinePMTNR.setDriverName(pmt.getDriverName());
			offlinePMTNR.setTransporter(pmt.getTransporter());
			offlinePMTNR.setStatusCode(ESETxnStatus.PENDING.ordinal());
			offlinePMTNR.setStatusMsg(ESETxnStatus.PENDING.toString());
			offlinePMTNR.setMtntReceiptNo(receiptNo);
			offlinePMTNR.setReceiptNo(mtnrreceiptNo);
			offlinePMTNR.setDeviceId(head.getSerialNo());
			
			// proceurementMTNR.setMtntReceiptNumber(receiptNo);
			String seasonCode = clientService.findCurrentSeasonCodeByBranchId(head.getBranchId());
			offlinePMTNR.setSeasonCode(seasonCode);
			offlinePMTNR.setBranchId(head.getBranchId());
			//offlinePMTNR.setTransporter(pmt.getTransporter());
			offlinePMTNR.setTrnType(PMT.TRN_TYPE_PRODUCT_RECEPTION);
			Collection collection = (Collection) reqData.get(TxnEnrollmentProperties.PROCUREMENT_MTNT_PRODUCTS);
			if (!ObjectUtil.isEmpty(collection)) {
				List<com.sourcetrace.eses.txn.schema.Object> objectList = collection.getObject();
				if (!ObjectUtil.isEmpty(objectList) && objectList.size() > 0) {

					for (Object object : objectList) {
						List<Data> mtntDataList = object.getData();
						OfflinePMTNRDetail offlinePmtnrDetail = new OfflinePMTNRDetail();
						for (Data mtntData : mtntDataList) {
							String key = mtntData.getKey();
							String value = mtntData.getValue();

							if (TxnEnrollmentProperties.PRODUCT_CODE.equalsIgnoreCase(key)) {
								if (StringUtil.isEmpty(value))
									throw new SwitchException(SwitchErrorCodes.EMPTY_PRODUCT_CODE);
								offlinePmtnrDetail.setProductCode(value);
								if (ObjectUtil.isEmpty(value)) {
									throw new SwitchException(SwitchErrorCodes.PRODUCT_DOES_NOT_EXIST);
								}
							}

							// No Of Bags
							if (TxnEnrollmentProperties.NO_OF_BAGS.equalsIgnoreCase(key)) {
								try {
									// pmtDetail.setMtntNumberOfBags(Long.valueOf(value));
									offlinePmtnrDetail.setNoOfBags(value);
								} catch (Exception e) {
									throw new SwitchException(SwitchErrorCodes.INVALID_NO_OF_BAGS);
								}
							}
							// Gross Weight
							if (TxnEnrollmentProperties.GROSS_WEIGHT.equalsIgnoreCase(key)) {
								try {
									// pmtDetail.setMtntGrossWeight(Double.valueOf(value));
									offlinePmtnrDetail.setGrossWeight(value);
								} catch (Exception e) {
									throw new SwitchException(SwitchErrorCodes.INVALID_GROSS_WEIGHT);
								}
							}
							if(TxnEnrollmentProperties.ICS_CD.equalsIgnoreCase(key)){
								try{
									offlinePmtnrDetail.setIcs(value);
								}catch (Exception e) {
									throw new SwitchException(SwitchErrorCodes.INVALID_ICS_CODE);
								}
							}
							if(TxnEnrollmentProperties.HEAP_CODE.equalsIgnoreCase(key)){
								try{
									offlinePmtnrDetail.setHeap(value);
								}catch (Exception e){
									throw new SwitchException(SwitchErrorCodes.INVALID_HEAP_CODE);
								}
								
							}

						}
						
						//offlinePmtnrDetail.setIcs(pmt.getPmtDetails().iterator().next().getIcs());
						//offlinePmtnrDetail.setGradeCode(pmt.getPmtDetails().iterator().next().getProcurementGrade().getCode());
						offlinePmtnrDetail.setCoOperativeCode(pmt.getCoOperative().getCode());
						offlinePmtnrDetail.setTransferBags(String.valueOf("0"));
						offlinePmtnrDetail.setTransferWeight(String.valueOf("0.0"));
						pmt.getPmtDetails().stream().filter(grade-> grade.getProcurementProduct().getCode().equalsIgnoreCase(offlinePmtnrDetail.getProductCode()) && grade.getIcs().equalsIgnoreCase(offlinePmtnrDetail.getIcs())).forEach(va -> {
						offlinePmtnrDetail.setTransferBags(String.valueOf(Long.valueOf(offlinePmtnrDetail.getTransferBags())+va.getMtntNumberOfBags()));
							offlinePmtnrDetail.setTransferWeight(String.valueOf(Double.valueOf(offlinePmtnrDetail.getTransferWeight())+va.getMtntQuintalWeight()));		
							va.setStatus(PMT.Status.COMPLETE.ordinal());
					  	productDistributionService.update(va);
						
							
							});
					
					
						
						offlinePmtnrDetail.setOfflinePMTNR(offlinePMTNR);
						pmtDetails.add(offlinePmtnrDetail);
					}
					offlinePMTNR.setOfflinePMTRDetail(pmtDetails);
					Set<OfflinePMTImageDetails> imageSet = new HashSet<>();
					try
					{
						if (!ObjectUtil.isEmpty(photo1) && photo1.getInputStream().available()>0) {
							OfflinePMTImageDetails img1 = new OfflinePMTImageDetails();
							try {
								img1.setPhoto(IOUtils.toByteArray(photo1.getInputStream()));
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							img1.setLatitude(lat1);
							img1.setLongitude(lon1);
							img1.setType(PMTImageDetails.Type.PMT.ordinal());
							try {
								Date mtntDate = DateUtil.convertStringToDate(capTime1, DateUtil.TXN_DATE_TIME);
								img1.setCaptureTime(mtntDate);
							} catch (Exception e) {
								throw new SwitchException(SwitchErrorCodes.INVALID_DATE_FORMAT);
							}
							imageSet.add(img1);
						}
						
						if (!ObjectUtil.isEmpty(photo2) && photo2.getInputStream().available()>0){
							OfflinePMTImageDetails img1 = new OfflinePMTImageDetails();
							try {
								img1.setPhoto(IOUtils.toByteArray(photo2.getInputStream()));
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							img1.setLatitude(lat2);
							img1.setLongitude(lon2);
							img1.setType(PMTImageDetails.Type.PMT.ordinal());
							try {
								Date mtntDate = DateUtil.convertStringToDate(capTime2, DateUtil.TXN_DATE_TIME);
								img1.setCaptureTime(mtntDate);
							} catch (Exception e) {
								throw new SwitchException(SwitchErrorCodes.INVALID_DATE_FORMAT);
							}
							imageSet.add(img1);
						}
						
						
					}
					catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
			

				
					offlinePMTNR.setOfflinePmtImages(imageSet);
					productDistributionService.saveOfflinePMTNR(offlinePMTNR);
				} else {
					throw new SwitchException(SwitchErrorCodes.PROCUREMENT_MENT_PRODUCTS_NOT_FOUND);
				}
			}
		/** FORM RESPONSE DATA **/
		}
		Map<String, Object> resp = new LinkedHashMap<String, Object>();
		return resp;
	}

	@Override
	public Map<?, ?> processVoid(Map<?, ?> reqData) {
		// TODO Auto-generated method stub
		return null;
	}

}
