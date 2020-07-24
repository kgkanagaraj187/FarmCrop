package com.sourcetrace.eses.adapter.core;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.profile.CropSupply;
import com.ese.entity.profile.CropSupplyDetails;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.order.entity.txn.CropSupplyImages;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.CollectionUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.entity.Image;
import com.sourcetrace.eses.util.entity.ImageInfo;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.DynamicImageData;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerHealthAsses;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;
import com.sourcetrace.esesw.excep.SwitchException;

@Component
public class ProductSalesAdapter implements ITxnAdapter {
	private static final Logger LOGGER = Logger
			.getLogger(ProductSalesAdapter.class.getName());
	private static final DateFormat TXN_DATE_FORMAT = new SimpleDateFormat(DateUtil.TXN_DATE_TIME);
	public static final String NOT_APPLICABLE = "N/A";
	 @Autowired
	private IProductDistributionService productDistributionService;
	 @Autowired
    private IProductService productService;
	 @Autowired
    private IAgentService agentService;
	 @Autowired
    private IDeviceService deviceService;
	 @Autowired
	private IFarmerService farmerService;
	 @Autowired
	private IClientService clientService;
	 @Autowired
	private IAccountService accountService;

	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {
		/** REQUEST DATA **/
		Head head = (Head) reqData.get(TransactionProperties.HEAD);

		/** VALIDATING REQUEST DATA **/
		String agentId = head.getAgentId();
		String serialNo = head.getSerialNo();
		String servPointId = head.getServPointId();
		String txnMode = head.getMode();
		String branchId=head.getBranchId();
		LOGGER.info("AGENT ID : " + agentId);
		LOGGER.info("SERIAL NO : " + serialNo);
		LOGGER.info("SERVICE POINT ID : " + servPointId);
		LOGGER.info("TXN MODE: " + txnMode);
		LOGGER.info("BRNCH ID: " + branchId);
		//* GET REQUEST DATA*/
		 Farmer farmer = new Farmer();
         Farm farm = new Farm();
         CropSupply cropSupply = new CropSupply();
         Date salesDate1 =null;
         
         Double finalBuyerAccCashBal = (double) 0;
    	 Double finalBuyerAccCreditBal = (double) 0;
    	
    	 Double finalCompAccCashBal = (double) 0;
    	 Double finalCompAccCreditBal = (double) 0;
			// ///
			// To fetch sales date
			String salesDate = (String) reqData
					.get(TxnEnrollmentProperties.SALES_DATE);
			if (ObjectUtil.isEmpty(salesDate)){
				throw new SwitchException(SwitchErrorCodes.EMPTY_FARMER_ID);
			}
			else
            {
                try {
                	salesDate1 = DateUtil.convertStringToDate(salesDate, DateUtil.FARMER_DOB);
                } catch (Exception e) {
                    e.printStackTrace();
                	throw new SwitchException(SwitchErrorCodes.EMPTY_FARMER_ID);
                }
            }
			// To fetch farmer Id
			String farmerId = (String) reqData
					.get(TxnEnrollmentProperties.FARMER_ID);
			if (StringUtil.isEmpty(farmerId))
				throw new SwitchException(SwitchErrorCodes.EMPTY_FARMER_ID);
            farmer = farmerService.findFarmerByFarmerId(farmerId);
			// To fetch farmId
			String farmId = (String) reqData
					.get(TxnEnrollmentProperties.FARM_ID);
			if (StringUtil.isEmpty(farmId))
				throw new SwitchException(SwitchErrorCodes.EMPTY_FARM_ID);
			farm = farmerService.findFarmByCode(farmId);
			// To fetch buyer
			String customerID = (String) reqData
			.get(TxnEnrollmentProperties.SALES_BUYER);
			Customer buyer = (Customer) clientService.findCustomerById(customerID);
			if (ObjectUtil.isEmpty(buyer)){
				throw new SwitchException(SwitchErrorCodes.EMPTY_BUYER_NAME);
			}
			// To fetch transporter name
			String transporterName = (String) reqData
					.get(TxnEnrollmentProperties.TRANSPORTER_NAME);
			// To fetch vehicle number
			String vehicleNumber = (String) reqData
					.get(TxnEnrollmentProperties.VEHICLE_NUMBER);
			// To fetch invoice detail
			String invoiceDetail = (String) reqData
					.get(TxnEnrollmentProperties.INVOICE_DETAIL);
			// To fetch total
			String total = (String) reqData
					.get(TxnEnrollmentProperties.TOTAL);
			if (ObjectUtil.isEmpty(total))
				throw new SwitchException(SwitchErrorCodes.EMPTY_FARMER_ID);
			// To fetch receipt number
			String receiptNo = (String) reqData
					.get(TxnEnrollmentProperties.RECEIPT_NO);
			// To fetch po number
			String poNumber = (String) reqData
					.get(TxnEnrollmentProperties.PO_NUMBER);
			String latitude =(String) reqData
					.get(TxnEnrollmentProperties.LATITUDE);
			String longitude =(String) reqData
					.get(TxnEnrollmentProperties.LONGITUDE);
			String payment = (String) reqData
					.get(TxnEnrollmentProperties.PAYEMENT);
			
			double paymentAnt = payment==null || StringUtil.isEmpty(payment) ? 0.00 : Double.valueOf(payment);
			
			String cSeasonCode = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.CURR_SEASON_CODE)))
					?(String)reqData.get(TxnEnrollmentProperties.CURR_SEASON_CODE) : "";
					
			
			
			cropSupply.setAgentId(agentId);
			cropSupply.setDateOfSale(salesDate1);
			cropSupply.setFarmerId(farmerId);
			cropSupply.setFarmCode(farm.getFarmCode());
			cropSupply.setFarmName(farm.getFarmName());
			cropSupply.setFarmerName(farmer.getName());
			cropSupply.setBuyerInfo(buyer);
			cropSupply.setTransportDetail(transporterName);
			cropSupply.setVehicleNo(vehicleNumber);
			cropSupply.setInvoiceDetail(invoiceDetail);
			cropSupply.setTotalSaleValu(Double.parseDouble(total));
			cropSupply.setReceiptInfor(receiptNo);			
			cropSupply.setPaymentInfo(payment);
			cropSupply.setBranchId(branchId);
			cropSupply.setCurrentSeasonCode(cSeasonCode);
			cropSupply.setPoNumber(!StringUtil.isEmpty(poNumber)?poNumber:"");
			cropSupply.setLatitude(latitude);
			cropSupply.setLongitude(longitude);
			/** FORMING CROP IMAGE OBJECT **/
			Collection photoList = (reqData.containsKey(TxnEnrollmentProperties.PHOTO_LIST))
					? (Collection) reqData.get(TxnEnrollmentProperties.PHOTO_LIST) : null;
					
					Set<CropSupplyImages> cropSupplyImageSet =  new LinkedHashSet<>();
					if (!CollectionUtil.isCollectionEmpty(photoList)) {
						List<com.sourcetrace.eses.txn.schema.Object> fieldObjects = photoList.getObject();
						for (com.sourcetrace.eses.txn.schema.Object fieldObject : fieldObjects) {
							CropSupplyImages cropSupplyImages=new CropSupplyImages();
							List<Data> dynamicData = fieldObject.getData();
							for (Data data : dynamicData) {
								String key = data.getKey();
								String value = data.getValue();
								if (TxnEnrollmentProperties.PHOTO_CAPTURE_TIME.equalsIgnoreCase(key)) {
									try {
									Date photoCaptureDate = TXN_DATE_FORMAT.parse(value);
									cropSupplyImages.setCaptureTime(photoCaptureDate);
									}catch (Exception e) {
										LOGGER.info(e.getMessage());
									}
									
								}
								 else if (TxnEnrollmentProperties.PHOTO_LATITUDE.equalsIgnoreCase(key)) {
									 cropSupplyImages.setLatitude(value);
								 }
								 else if (TxnEnrollmentProperties.PHOTO_LONGITUDE.equalsIgnoreCase(key)) {
									 cropSupplyImages.setLongitude(value);
								 }
								 else if (TxnEnrollmentProperties.PHOTO.equalsIgnoreCase(key)){
									 DataHandler photo = data.getBinaryValue();
										byte[] imageContent = null;
										try {
											if (photo != null && photo.getInputStream().available() > 0) {
												imageContent = IOUtils.toByteArray(photo.getInputStream());
												cropSupplyImages.setPhoto(imageContent);
											}
										} catch (Exception e) {
											e.printStackTrace();
											throw new SwitchException(ITxnErrorCodes.ERR0R_WHILE_PROCESSING);
										}

								 }
							}
							cropSupplyImages.setCropSupply(cropSupply);
								cropSupplyImageSet.add(cropSupplyImages);
								cropSupply.setCropSupplyImages(cropSupplyImageSet); 
						}
						
						}
		
			/** FORMING CROP DETAIL OBJECT **/
            Set<CropSupplyDetails> cropSupplyList = new HashSet<CropSupplyDetails>();

            Collection collection = (Collection) reqData
                    .get(TxnEnrollmentProperties.CROP_LIST);
            List<com.sourcetrace.eses.txn.schema.Object> cropObject = collection.getObject();
            for (com.sourcetrace.eses.txn.schema.Object object : cropObject) {
                List<Data> cropData = object.getData();
                CropSupplyDetails cropSupplyDetails = new CropSupplyDetails();
                for (Data data : cropData) {
                    String key = data.getKey();
                    String value = data.getValue();
                    
                    if (TxnEnrollmentProperties.CROP_TYPE.equalsIgnoreCase(key)) {
                        if (StringUtil.isEmpty(value)) {
                            throw new SwitchException(SwitchErrorCodes.EMPTY_CROP_TYPE);
                        }
                        cropSupplyDetails.setCropType(Integer.valueOf(value));
                    }
                    
                    if (TxnEnrollmentProperties.CROP_ID.equalsIgnoreCase(key)) {
                        if (StringUtil.isEmpty(value)) {
                            throw new SwitchException(SwitchErrorCodes.EMPTY_CROP_ID);
                        }
                        ProcurementProduct crop = productService.findProcurementProductByCode(value);
                        cropSupplyDetails.setCrop(crop);
                    }
                    
                    if (TxnEnrollmentProperties.CROP_VARIETY_ID.equalsIgnoreCase(key)) {
                        if (StringUtil.isEmpty(value)) {
                            throw new SwitchException(SwitchErrorCodes.EMPTY_CROP_VARIETY_ID);
                        }
                        ProcurementVariety variety = productService.findProcurementVarietyByCode(value);
                        cropSupplyDetails.setVariety(variety);
                    }
                    
                    if (TxnEnrollmentProperties.CROP_GRADE_CODE.equalsIgnoreCase(key)) {
                        if (StringUtil.isEmpty(value)) {
                            throw new SwitchException(SwitchErrorCodes.EMPTY_CROP_GRADE_CODE);
                        }
                        ProcurementGrade grade = productService.findProcurementGradeByCode(value);
                        cropSupplyDetails.setGrade(grade);
                    }
                    try {
                        if (TxnEnrollmentProperties.CROP_QUANTITY.equalsIgnoreCase(key)) {
                        	cropSupplyDetails.setQty(Double.valueOf(value));
                        }
                        
                        if (TxnEnrollmentProperties.BATCH_NO.equalsIgnoreCase(key)) {
                        	cropSupplyDetails.setBatchLotNo(value);
                        }	
                       
                         if (TxnEnrollmentProperties.CROP_PRICE.equalsIgnoreCase(key)) {
                        	 cropSupplyDetails.setPrice(!StringUtil.isEmpty(value)? Double.valueOf(value): 0.00);
                        }
                        
                        if (TxnEnrollmentProperties.CROP_SUB_TOTAL.equalsIgnoreCase(key)) {
                        	cropSupplyDetails.setSubTotal(Double.valueOf(value));
                        }
                    } catch (Exception e) {
                        throw new SwitchException(SwitchErrorCodes.DATA_CONVERSION_ERROR);
                    }
      
            }
                cropSupplyDetails.setCropSupply(cropSupply);
                cropSupplyList.add(cropSupplyDetails);
                cropSupply.setCropSupplyDetails(cropSupplyList); 
                
            }
            
            try {
            	
            	Customer customer = clientService.findCustomer(Long.valueOf(buyer.getId()));
	    		if(!ObjectUtil.isEmpty(customer))
	    		{
	    			ESEAccount buyerAccount = accountService.findEseAccountByBuyerIdAndTypee(customer.getCustomerId(), ESEAccount.CLIENT_ACCOUNT);
	    			
	    			ESEAccount companyAccount = accountService.findEseAccountByProfileId(ESEAccount.BASIX_ACCOUNT);
	    			
	    			Double buyerAccCashBal = buyerAccount.getCashBalance();
	    			Double buyerAccCreditBal = buyerAccount.getCreditBalance();
	    			Double compAccCashBal = companyAccount.getCashBalance();
	    			Double compAccCreditBal = companyAccount.getCreditBalance();
	    			
	    			//if ("0".equals(paymentMode))  
	    			//{
	    				finalBuyerAccCashBal = buyerAccCashBal - paymentAnt;
	    				finalBuyerAccCreditBal = buyerAccCreditBal - (Double.valueOf(total) - paymentAnt);
	    				
	    				finalCompAccCashBal = compAccCashBal + paymentAnt;
	    				if(!buyerAccCreditBal.equals(finalBuyerAccCreditBal) && (paymentAnt > Double.parseDouble(total)))   // finalBuyerAccCreditBal>0
	    					finalCompAccCreditBal = compAccCreditBal - (paymentAnt + buyerAccCreditBal - finalBuyerAccCreditBal);
	    				else if(!buyerAccCreditBal.equals(finalBuyerAccCreditBal) && (paymentAnt < Double.parseDouble(total)))   // finalBuyerAccCreditBal>0
	    					finalCompAccCreditBal = compAccCreditBal + (Double.valueOf(total) - paymentAnt);
	    				else
	    					finalCompAccCreditBal = compAccCreditBal; 
	    				
	    				

	    				accountService.updateCashBal(buyerAccount.getId(), finalBuyerAccCashBal, finalBuyerAccCreditBal);
	    				accountService.updateCashBal(companyAccount.getId(), finalCompAccCashBal, finalCompAccCreditBal);
	                //} 
	    			/*else if ("1".equals(paymentMode)) 
	                {
	    				finalBuyerAccCashBal = buyerAccCashBal;
	    				finalBuyerAccCreditBal = buyerAccCreditBal - Double.valueOf(totalSalePrice);
	    				
	    				finalCompAccCashBal = compAccCashBal;
	    				finalCompAccCreditBal = compAccCreditBal;
	                }*/
	    			/*if(finalBuyerAccCashBal<0)
	    			{
	    				throw new SwitchException(SwitchErrorCodes.INSUFFICIENT_BAL);
	    			}
	    			else
	    			{
	    				//compAccCashBal = compAccCashBal + Double.valueOf(payment);
	    				
	    				accountService.updateCashBal(buyerAccount.getId(), finalBuyerAccCashBal, finalBuyerAccCreditBal);
	    				accountService.updateCashBal(companyAccount.getId(), finalCompAccCashBal, finalCompAccCreditBal);
	    				
	    				accountService.updateESEAccountCashBalById(buyerAccount.getId(), buyerAccCashBal);
	    				accountService.updateESEAccountCashBalById(companyAccount.getId(), compAccCashBal);
	    			}*/
	    		}
            	
            	productDistributionService.saveCropSupplyAndCropSupplyDetails(cropSupply);
                
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error(e.getMessage());
            }
            return new HashMap();

	}

	@Override
	public Map<?, ?> processVoid(Map<?, ?> reqData) {
		return null;
	}

}
