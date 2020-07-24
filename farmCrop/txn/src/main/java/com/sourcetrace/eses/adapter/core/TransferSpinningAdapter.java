package com.sourcetrace.eses.adapter.core;


import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.appengine.api.datastore.Transaction;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.order.entity.txn.OfflineGinningProcess;
import com.sourcetrace.eses.order.entity.txn.OfflinePMTImageDetails;
import com.sourcetrace.eses.order.entity.txn.OfflineSpinningTransfer;
import com.sourcetrace.eses.order.entity.txn.PMTImageDetails;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.OfflineTransactionException;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;

@Component
public class TransferSpinningAdapter implements ITxnAdapter {
	private static final Logger LOGGER = Logger.getLogger(TransferSpinningAdapter.class.getName());
	public static final String NOT_APPLICABLE = "N/A";
	
	@Autowired
	private IAgentService agentService;
	@Autowired
	private IProductDistributionService productDistributionService;
	
	
	
	
	
	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {
		Head head=(Head)reqData.get(TransactionProperties.HEAD);
		String messageNo=head.getMsgNo();
		OfflineSpinningTransfer exist=productDistributionService.findSpinningTransferByMessageNo(messageNo);
		if(exist==null || ObjectUtil.isEmpty(exist)){
		String agentId=head.getAgentId();
		String serialNo=head.getSerialNo();
		String servicePointId=head.getServPointId();
		String txnMode=head.getTxnType();
		String branch=head.getBranchId();
		String transDate=(String) reqData.get(TransactionProperties.TRANS_DATE);
		String invoiceNo=(String)reqData.get(TransactionProperties.TAX_INVOICE_NO);
		String truckNo=(String) reqData.get(TransactionProperties.TRUCK_NO);
		String spinCode=(String) reqData.get(TransactionProperties.SPINNING_CODE);
		String lotNo=(String)reqData.get(TransactionProperties.LOT_NO);
		String prNo=(String) reqData.get(TransactionProperties.PR_NO);
		String noBal=(String) reqData.get(TransactionProperties.BALE_COUNT);
		String netWeight=(String) reqData.get(TransactionProperties.NET_WEIGHT);
		String rate=(String) reqData.get(TransactionProperties.RATE);
		String netAmt=(String)reqData.get(TransactionProperties.NET_AMT);
		String type=(String)reqData.get(TransactionProperties.TYPE);
		DataHandler photo1 = (DataHandler) reqData.get(TxnEnrollmentProperties.SP_PHOTO_1);
		DataHandler photo2 = (DataHandler) reqData.get(TxnEnrollmentProperties.SP_PHOTO_2);
		String lat1 = (String) reqData.get(TxnEnrollmentProperties.SP_LAT_1);
		String lat2 = (String) reqData.get(TxnEnrollmentProperties.SP_LAT_2);
		String lon1 = (String) reqData.get(TxnEnrollmentProperties.SP_LON_1);
		String lon2 = (String) reqData.get(TxnEnrollmentProperties.SP_LON_2);
		String capTime1 = (String) reqData.get(TxnEnrollmentProperties.SP_CAT_TIME1);
		String capTime2 = (String) reqData.get(TxnEnrollmentProperties.SP_CAT_TIME2);
		String season=(String) reqData.get(TxnEnrollmentProperties.SEASON);
		
		Agent agent=agentService.findAgentByAgentId(agentId);
		if(ObjectUtil.isEmpty(agent))
			throw new SwitchException(SwitchErrorCodes.INVALID_AGENT);
		else
			LOGGER.info("AGENT ID: "+agentId);
		if(StringUtil.isEmpty(serialNo))
			try{
			throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_SERIAL_NO);
			}catch(Exception e) {}
		else
			LOGGER.info("SERIAL NO: "+serialNo);
		
			LOGGER.info("SERVICE POINT ID: "+servicePointId);
			LOGGER.info("TXN MODE: "+txnMode);
		OfflineSpinningTransfer ost=new OfflineSpinningTransfer();
		ost.setMessageNo(messageNo);
		ost.setAgentId(agentId);
		ost.setBranchId(branch);
		ost.setCreatedDate(new Date());
		ost.setCreatedUser(agentId);
		ost.setUpdatedUser(agentId);
		ost.setDeviceId(serialNo);
		ost.setStatusCode(ESETxnStatus.PENDING.ordinal());
		ost.setStatusMsg(ESETxnStatus.PENDING.toString());
		if(StringUtil.isEmpty(transDate))
			throw new SwitchException(SwitchErrorCodes.EMPTY_TRANSFER_DATE);
		else
			ost.setTransDate(transDate);
		/*if(StringUtil.isEmpty(invoiceNo))
			throw new SwitchException(SwitchErrorCodes.EMPTY_INVOICE_NO);
		else*/
		ost.setInvoiceNo(invoiceNo);
		if(StringUtil.isEmpty(season))
			throw new SwitchException(SwitchErrorCodes.EMPTY_SEASON_CODE);
		else
			ost.setSeason(season);
		if(StringUtil.isEmpty(truckNo))
			throw new SwitchException(SwitchErrorCodes.EMPTY_TRUCK_ID);
		else
			ost.setTruckNo(truckNo);
		if(StringUtil.isEmpty(spinCode))
			throw new SwitchException(SwitchErrorCodes.EMPTY_SPINNER_CODE);
		else
			ost.setSpinnerCode(spinCode);
		if(StringUtil.isEmpty(lotNo))
			throw new SwitchException(SwitchErrorCodes.EMPTY_LOT_NO);
		else
			ost.setLotNo(lotNo);
		if(StringUtil.isEmpty(prNo))
			throw new SwitchException(SwitchErrorCodes.EMPTY_PR_NO);
		else
			ost.setPrNo(prNo);
		if(StringUtil.isEmpty(noBal))
			throw new SwitchException(SwitchErrorCodes.EMPTY_NO_OF_BALES);
		else
			ost.setNoBals(noBal);
		if(StringUtil.isEmpty(netWeight))
			throw new SwitchException(SwitchErrorCodes.EMPTY_NET_WEIGHT);
		else
			ost.setNetWeight(netWeight);
		if(StringUtil.isEmpty(rate))
			throw new SwitchException(SwitchErrorCodes.EMPTY_RATE);
		else
			ost.setRate(rate);
		if(StringUtil.isEmpty(netAmt))
			throw new SwitchException(SwitchErrorCodes.EMPTY_NET_RATE);
		else
			ost.setNetAmt(netAmt);
		if(StringUtil.isEmpty(type))
			throw new SwitchException(SwitchErrorCodes.EMPTY_TYPE);
		else
			ost.setType(type);
		
		
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
				img1.setType(PMTImageDetails.Type.SPINNING.ordinal());
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
				img1.setType(PMTImageDetails.Type.SPINNING.ordinal());
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
	
		ost.setOfflinePmtImages(imageSet);
		
		productDistributionService.addOfflineSpinningTransfer(ost);
		}
		Map<String, Object> resp = new LinkedHashMap<String, Object>();
		return resp;
	}
	@Override
	public Map<?, ?> processVoid(Map<?, ?> reqData) {
		return null;
	}
	public IAgentService getAgentService() {
		return agentService;
	}
	public void setAgentService(IAgentService agentService) {
		this.agentService = agentService;
	}
	
	
}
