package com.sourcetrace.eses.adapter.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.sms.SMSHistory;
import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.order.entity.profile.PaymentImage;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.OfflinePayment;
import com.sourcetrace.eses.order.entity.txn.PaymentMode;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.ISMSService;
import com.sourcetrace.eses.service.IServicePointService;
import com.sourcetrace.eses.txn.exception.OfflineTransactionException;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.ServicePoint;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;

@Component
public class OfflinePaymentSchedulerTask {

	private static final Logger LOGGER = Logger.getLogger(OfflinePaymentSchedulerTask.class.getName());
	private static final DateFormat TXN_DATE_FORMAT = new SimpleDateFormat(DateUtil.TXN_DATE_TIME);
	 @Autowired
	private IProductDistributionService productDistributionService;
	 @Autowired
	private IAgentService agentService;
	 @Autowired
	private IDeviceService deviceService;
	 @Autowired
	private IServicePointService servicePointService;
	 @Autowired
	private IFarmerService farmerService;
	 @Autowired
	private ILocationService locationService;
	 @Autowired
	private IAccountService accountService;
	 @Resource(name = "datasources")
	private Map<String, DataSource> datasources;
	 
	   @Autowired
	    private IClientService clientService;
	   
		@Autowired
		private ISMSService smsService;
		@Autowired
		private IPreferencesService preferencesService;
	  
	   String providerResponse = null;
		int smsType = SMSHistory.SMS_SINGLE;
		String status = null;
		String descrption = null;
	
	public void process() {
Vector<String> tenantIds=new Vector(datasources.keySet());
    	
    	for (String tenantId : tenantIds) 
		{
    		List<OfflinePayment> paymentList = Collections.synchronizedList(productDistributionService.listPendingPaymentTxn(tenantId));
    		for (OfflinePayment payment : paymentList) {
    			int statusCode = ESETxnStatus.SUCCESS.ordinal();
    			String statusMsg = ESETxnStatus.SUCCESS.toString();
    			AgroTransaction agentPaymentTxn = new AgroTransaction();
    			AgroTransaction farmerPaymentTxn = new AgroTransaction();

    			/** VALIDATION OF REQUEST DATA **/
    			try {
    				if (StringUtil.isEmpty(payment.getAgentId())) {
    					throw new OfflineTransactionException(SwitchErrorCodes.AGENT_ID_EMPTY);
    				}
    				//Agent agent = agentService.findAgentByAgentId(payment.getAgentId());
    				 Agent agent = agentService.findAgentByAgentId(payment.getAgentId(), tenantId,payment.getBranchId());
    				if (ObjectUtil.isEmpty(agent)) {
    					throw new OfflineTransactionException(SwitchErrorCodes.INVALID_AGENT);
    				}

    				if (StringUtil.isEmpty(payment.getDeviceId())) {
    					throw new OfflineTransactionException(SwitchErrorCodes.EMPTY_SERIAL_NO);
    				}
    				Device device = deviceService.findDeviceBySerialNumber(payment.getDeviceId(),tenantId);
    				if (ObjectUtil.isEmpty(device)) {
    					throw new OfflineTransactionException(SwitchErrorCodes.INVALID_DEVICE);
    				}
    				if (StringUtil.isEmpty(payment.getServicePointId())) {
    					throw new OfflineTransactionException(SwitchErrorCodes.EMPTY_SERV_POINT_ID);
    				}
    				ServicePoint servicePoint = servicePointService.findServicePointByServPointId(payment.getServicePointId(),tenantId);
    				if (ObjectUtil.isEmpty(servicePoint)) {
    					throw new OfflineTransactionException(SwitchErrorCodes.INVALID_SERVICE_POINT);
    				}
    				if (StringUtil.isEmpty(payment.getReceiptNo())) {
    					throw new OfflineTransactionException(SwitchErrorCodes.EMPTY_RECEIPT_NO);
    				}
    				List<AgroTransaction> existing = productDistributionService.findAgroTxnByReceiptNo(payment.getReceiptNo(),tenantId);
    				if (!ObjectUtil.isListEmpty(existing)) {
    					throw new OfflineTransactionException(SwitchErrorCodes.PAYMENT_ALREADY_EXIST);
    				}
    				if (StringUtil.isEmpty(payment.getFarmerId())) {
    					throw new OfflineTransactionException(SwitchErrorCodes.EMPTY_FARMER_ID);
    				}
    				Farmer farmer = farmerService.findFarmerByFarmerId(payment.getFarmerId(),tenantId);
    				if (ObjectUtil.isEmpty(farmer)) {
    					throw new OfflineTransactionException(SwitchErrorCodes.FARMER_NOT_EXIST);
    				}

    				if (StringUtil.isEmpty(payment.getSeasonCode()))
    					throw new OfflineTransactionException(SwitchErrorCodes.EMPTY_SEASON_CODE);
    				HarvestSeason season= clientService.findSeasonByCodeByTenant(payment.getSeasonCode(), tenantId);
    				if (ObjectUtil.isEmpty(season))
    					throw new OfflineTransactionException(SwitchErrorCodes.SEASON_NOT_EXIST);

    				if (StringUtil.isEmpty(payment.getPaymentType()))
    					throw new OfflineTransactionException(SwitchErrorCodes.EMPTY_PAYMENT_TYPE);
    				PaymentMode paymentMode = null;
    					/*if(tenantId.equalsIgnoreCase("lalteer")){
		    				 paymentMode = productDistributionService.findPaymentModeByCode(payment.getPaymentType(),tenantId);
		    				if (ObjectUtil.isEmpty(paymentMode))
		    					throw new OfflineTransactionException(SwitchErrorCodes.INVALID_PAYMENT_MODE);
    					}*/
    				
    				 paymentMode = productDistributionService.findPaymentModeByCode(payment.getPaymentType(),tenantId);
	    				if (ObjectUtil.isEmpty(paymentMode))
	    					throw new OfflineTransactionException(SwitchErrorCodes.INVALID_PAYMENT_MODE);
    				if (StringUtil.isEmpty(payment.getPaymentDate()))
    					throw new OfflineTransactionException(SwitchErrorCodes.EMPTY_PAYMENT_DATE);

    				/** FETCHING ACCOUNT FOR AGENT **/
    				ESEAccount agentAccount = accountService.findAccountByProfileIdAndProfileType(payment.getAgentId(), ESEAccount.AGENT_ACCOUNT,tenantId);
    				if (ObjectUtil.isEmpty(agentAccount))
    					throw new OfflineTransactionException(SwitchErrorCodes.AGENT_ACCOUNT_INVALID);
    				else if (ESEAccount.INACTIVE == agentAccount.getStatus())
    					throw new OfflineTransactionException(SwitchErrorCodes.AGENT_ACCOUNT_INACTIVE);

    				/** FETCHING ACCOUNT FOR FARMER **/
    				ESEAccount farmerAccount = farmerService.findAccountBySeassonProcurmentProductFarmer(0L, farmer.getId(),tenantId);
    				if (ObjectUtil.isEmpty(farmerAccount))
    					throw new OfflineTransactionException(SwitchErrorCodes.FARMER_ACCOUNT_UNAVAILABLE);
    				else if (ESEAccount.INACTIVE == farmerAccount.getStatus())
    					throw new OfflineTransactionException(SwitchErrorCodes.FARMER_ACCOUNT_INACTIVE);

    				LOGGER.info("ACCOUNT NO OF AGENT   :" + agentAccount.getAccountNo());
    				LOGGER.info("ACCOUNT NO OF FARMER  :" + farmerAccount.getAccountNo());

    				
    				if (StringUtil.isEmpty(payment.getPageNo()))
    					payment.setPageNo(AgroTransaction.EMPTY_PAGE_NO);
    				/** FORMING AGRO TRANSACTION FOR AGENT **/
    				if(!PaymentMode.LOAN_REPAYMENT
                            .equalsIgnoreCase(payment.getPaymentType())){
    					agentPaymentTxn.setReceiptNo(payment.getReceiptNo());
        				agentPaymentTxn.setSeasonCode(payment.getSeasonCode());
        				agentPaymentTxn.setAgentId(payment.getAgentId());
        				agentPaymentTxn.setAgentName(!ObjectUtil.isEmpty(agent.getPersonalInfo()) ? agent.getPersonalInfo().getAgentName() : "");
        				agentPaymentTxn.setDeviceId(device.getCode());
        				agentPaymentTxn.setDeviceName(device.getName());
        				agentPaymentTxn.setServicePointId(servicePoint.getCode());
        				agentPaymentTxn.setServicePointName(servicePoint.getName());
        				agentPaymentTxn.setFarmerId(payment.getFarmerId());
        				agentPaymentTxn.setFarmerName(farmer.getFirstName() + " " + farmer.getLastName());
        				agentPaymentTxn.setOperType(ESETxn.ON_LINE);
        				agentPaymentTxn.setProfType(Profile.CO_OPEARATIVE_MANAGER);
        				agentPaymentTxn.setTxnTime(DateUtil.convertStringToDate(payment.getPaymentDate(), DateUtil.TXN_DATE_TIME));
        				agentPaymentTxn.setTxnAmount(Double.parseDouble(payment.getPaymentAmt()));
        				agentPaymentTxn.setLatitude(payment.getLatitude());
        				agentPaymentTxn.setLongitude(payment.getLongitude());
        				/*if(tenantId.equalsIgnoreCase("lalteer")){
        				agentPaymentTxn.setTxnDesc(paymentMode.getName());
        				}*/
        				agentPaymentTxn.setTxnDesc(paymentMode.getName().toUpperCase());
        				agentPaymentTxn.setBranch_id(payment.getBranchId());
    				}
    				
    				
    				/** FORMING AGRO TRANSACTION FOR FARMER **/
    				farmerPaymentTxn.setReceiptNo(payment.getReceiptNo());
    				farmerPaymentTxn.setSeasonCode(payment.getSeasonCode());
    				farmerPaymentTxn.setAgentId(payment.getAgentId());
    				farmerPaymentTxn.setAgentName(!ObjectUtil.isEmpty(agent.getPersonalInfo()) ? agent.getPersonalInfo().getAgentName() : "");
    				farmerPaymentTxn.setDeviceId(device.getCode());
    				farmerPaymentTxn.setDeviceName(device.getName());
    				farmerPaymentTxn.setServicePointId(servicePoint.getCode());
    				farmerPaymentTxn.setServicePointName(servicePoint.getName());
    				farmerPaymentTxn.setFarmerId(payment.getFarmerId());
    				farmerPaymentTxn.setFarmerName(farmer.getFirstName() + " " + farmer.getLastName());
    				farmerPaymentTxn.setOperType(ESETxn.ON_LINE);
    				farmerPaymentTxn.setProfType(Profile.CLIENT);
    				farmerPaymentTxn.setTxnTime(DateUtil.convertStringToDate(payment.getPaymentDate(), DateUtil.TXN_DATE_TIME));
    				farmerPaymentTxn.setTxnAmount(Double.parseDouble(payment.getPaymentAmt()));
    				farmerPaymentTxn.setLatitude(payment.getLatitude());
    				farmerPaymentTxn.setLongitude(payment.getLongitude());

    				/*if(tenantId.equalsIgnoreCase("lalteer")){
    				 farmerPaymentTxn.setTxnDesc(paymentMode.getName());
    				}*/
    				farmerPaymentTxn.setTxnDesc(paymentMode.getName().toUpperCase());
    				farmerPaymentTxn.setBranch_id(payment.getBranchId());
    				// Adding remarks to the Transaction description
    				if (!StringUtil.isEmpty(payment.getRemarks())) {
  					  if(!PaymentMode.LOAN_REPAYMENT
  	                           .equalsIgnoreCase(payment.getPaymentType())){
  					agentPaymentTxn.setTxnDesc(agentPaymentTxn.getTxnDesc().toUpperCase() + "|" + payment.getRemarks());
  					  }
  					farmerPaymentTxn.setTxnDesc(farmerPaymentTxn.getTxnDesc().toUpperCase() + "|" + payment.getRemarks());
  				}

    				// Added txn insufficient balance check
    				/*if(tenantId.equalsIgnoreCase("lalteer")){
    				if (!PaymentMode.DISTRIBUTION_ADVANCE_PAYMENT_MODE_CODE.equalsIgnoreCase(paymentMode.getCode())) {
    					agentPaymentTxn.setTxnType(PaymentMode.PROCURMENT_PAYMENT_TXN);
    					farmerPaymentTxn.setTxnType(PaymentMode.PROCURMENT_PAYMENT_TXN);
    				} else {
    					agentPaymentTxn.setTxnType(PaymentMode.DISTIBUTION_PAYMENT_TXN);
    					farmerPaymentTxn.setTxnType(PaymentMode.DISTIBUTION_PAYMENT_TXN);
    				}
    			}*/
    				if (!PaymentMode.DISTRIBUTION_ADVANCE_PAYMENT_MODE_CODE.equalsIgnoreCase(paymentMode.getName())) {
    					agentPaymentTxn.setTxnType(PaymentMode.PROCURMENT_PAYMENT_TXN);
    					farmerPaymentTxn.setTxnType(PaymentMode.PROCURMENT_PAYMENT_TXN);
    				}else if(PaymentMode.LOAN_REPAYMENT
	                        .equalsIgnoreCase(payment.getPaymentType())){
	                     farmerPaymentTxn.setTxnType(PaymentMode.LOAN_REPAYMENT_TXN);
	                } else {
    					agentPaymentTxn.setTxnType(PaymentMode.DISTIBUTION_PAYMENT_TXN);
    					farmerPaymentTxn.setTxnType(PaymentMode.DISTIBUTION_PAYMENT_TXN);
    				}
    				
    				
    				/** SETTING ACCOUNT FOR AGENT AND FARMER **/
    				farmerPaymentTxn.setAccount(farmerAccount);
    				agentPaymentTxn.setAccount(agentAccount);
    				
    				farmerPaymentTxn.setBranch_id(payment.getBranchId());
    				agentPaymentTxn.setBranch_id(payment.getBranchId());
    				
    				farmerPaymentTxn.setBranchId(payment.getBranchId());
    				agentPaymentTxn.setBranchId(payment.getBranchId());
    				
    				 if(PaymentMode.LOAN_REPAYMENT
 	                        .equalsIgnoreCase(payment.getPaymentType())){
 	                	farmerPaymentTxn.setIntBalance(farmerAccount.getOutstandingLoanAmount());
 	                	farmerPaymentTxn.setTxnAmount(Double.parseDouble(payment.getPaymentAmt()));
 	                	farmerPaymentTxn.setBalAmount(farmerAccount.getOutstandingLoanAmount() - Double.parseDouble(payment.getPaymentAmt()));
 	    	            
 	                }
    				//Double farmerBal=farmerAccount.getCashBalance()-farmerPaymentTxn.getTxnAmount();
    				//Double agentBal=agentAccount.getCashBalance()-agentPaymentTxn.getTxnAmount();
    				/** SAVE PAYMENT TXN **/
    				productDistributionService.saveAgroTransactionForPayment(farmerPaymentTxn, agentPaymentTxn,tenantId);
    				
    				
if(tenantId=="gsma"){
					ESESystem system = preferencesService.findPrefernceById("1");
					SMSHistory sms = new SMSHistory();
					sms.setCreatedUser(payment.getAgentId());
					sms.setBranchId(payment.getBranchId());
					sms.setReceiverMobNo(farmer.getMobileNumber());
					sms.setSenderMobNo(farmer.getMobileNumber());
					sms.setSmsRoute(system.getPreferences().get(ESESystem.SMS_ROUTE));
					
					
					
					if (!ObjectUtil.isEmpty(system) && !ObjectUtil.isEmpty(system.getPreferences())) {
						String msg =  system.getPreferences().get(ESESystem.SMS_MESSAGE_PAYMENT);
						
						 msg = msg.replaceAll("<cash_mode>",paymentMode.getName());
						 msg = msg.replaceAll("<txn_amt>", String.valueOf(farmerPaymentTxn.getTxnAmount()));
						 msg = msg.replaceAll("<bal_amt>", String.valueOf(farmerPaymentTxn.getBalAmount()));
						 
						 if(farmerPaymentTxn.getIntialBalance() < farmerPaymentTxn.getBalAmount()){
							 msg = msg + " CR";
						 }else{
							 msg = msg + " DR";
						 }
						 
						 sms.setMessage(msg);
					}
					
					if(!StringUtil.isEmpty(sms.getReceiverMobNo()) && !StringUtil.isEmpty(sms.getMessage())){
						providerResponse = smsService.sendSMS(smsType, sms.getReceiverMobNo(),sms.getMessage());
					}else{
						providerResponse = ISMSService.ERROR+" : mobileNo or msgContent is empty";
					}
					
					sms.setResponce(providerResponse);
					if (!StringUtil.isEmpty(providerResponse)) {
						JSONObject respObj = null;
						try {
							respObj = new JSONObject(providerResponse);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (!respObj.has(ISMSService.ERROR)) {
							
							//statusMsg = "Success";
							sms.setStatusMsg("Delivered");
							
							try {
								sms.setUuid(respObj.getString("batch_id"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							farmerService.save(sms);
						} else {
							//statusMsg = "ERROR";
							sms.setStatusMsg("Failed");
							farmerService.save(sms);
						}
						
					}
				
}			
    				
    				
    				/** UPDATE ACCOUNT FOR AGENT AND FARMER **/
    				//farmerAccount.setCashBalance(farmerBal);
    				//agentAccount.setCashBalance(agentBal);
    				//accountService.updateByTenant(farmerAccount,tenantId);
    				//accountService.updateByTenant(agentAccount,tenantId);
    				System.out.println(farmerPaymentTxn);
    			} catch (OfflineTransactionException ote) {
    				statusCode = ESETxnStatus.ERROR.ordinal();
    				statusMsg = ote.getError();
    			} catch (Exception e) { // Catches all type of exception except
    				// OfflineTransactionException
    				statusCode = ESETxnStatus.ERROR.ordinal();
    				if (!ObjectUtil.isEmpty(e)) {
    					// statusMsg = e.getMessage().substring(0,
    					// e.getMessage().length() > 40 ? 40 :
    					// e.getMessage().length());
    					statusMsg = e.getMessage() + "";
    				}
    				e.printStackTrace();
    			}
    			payment.setStatusCode(statusCode);
    			payment.setStatusMsg(statusMsg);
    			productDistributionService.updateByTenant(payment,tenantId);
    			
    			PaymentImage paymentImage = new PaymentImage();
    			paymentImage.setPhoto(payment.getImage());
    			if (!StringUtil.isEmpty(payment.getPcTime())) {
                    try {
                        Date photoCaptureDate = TXN_DATE_FORMAT.parse(payment.getPcTime());
                        paymentImage.setPhotoCaptureTime(photoCaptureDate);
                    } catch (Exception e) {
                        LOGGER.info(e.getMessage());
                        paymentImage.setPhotoCaptureTime(null);
                    }
                }
    			paymentImage.setLatitude(payment.getLatitude());
    			paymentImage.setLongitude(payment.getLongitude());
    			paymentImage.setTxn(farmerPaymentTxn);
    			productDistributionService.SaveByTenant(paymentImage,tenantId);
    		}// end of For loop
    	
		}
	}

}
