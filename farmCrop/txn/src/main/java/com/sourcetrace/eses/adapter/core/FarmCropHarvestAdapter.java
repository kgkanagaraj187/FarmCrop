package com.sourcetrace.eses.adapter.core;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.profile.CropHarvest;
import com.ese.entity.profile.CropHarvestDetails;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;
import com.sourcetrace.esesw.excep.SwitchException;

@Component
public class FarmCropHarvestAdapter implements ITxnAdapter {
	@Autowired
	private IAgentService agentService;
	@Autowired
    private IDeviceService deviceService;
	@Autowired
    private IFarmerService farmerService; 
	@Autowired
    private IProductService productService;
    
	private static final Logger LOGGER = Logger.getLogger(FarmCropHarvestAdapter.class.getName());
	//private int mode = 1;

	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {
		
	
		 /** GET HEAD DATA **/
		 //Map rep = new HashMap();
		  Head head = (Head) reqData.get(TransactionProperties.HEAD);
		  String agentId = head.getAgentId();
		  String serialNo = head.getSerialNo();
		  String branchId=head.getBranchId();
		  //mode = Integer.parseInt(head.getMode());

	        LOGGER.info("----- HEADER VALUES -------");
	        LOGGER.info("Serial Number : " + serialNo);
	        LOGGER.info("Agent id : " + agentId);
	        LOGGER.info("BRANCH ID : " + branchId);
	        
        /** GET REQUEST DATA **/
        
        String harvestDate = (String) reqData.get(TxnEnrollmentProperties.CROP_HARVEST_DATE);
        String farmerId = (String) reqData.get(TxnEnrollmentProperties.CROP_FARMER_ID);
        String farmId = (String) reqData.get(TxnEnrollmentProperties.CROP_FARM_ID);
        String totalQty = (String) reqData.get(TxnEnrollmentProperties.TOTAL);
        String receiptNo = (String) reqData.get(TxnEnrollmentProperties.CROP_RECEIPT_NO);
        String storedIn = (String) reqData.get(TxnEnrollmentProperties.STORAGE_IN);
        String packeIn = (String) reqData.get(TxnEnrollmentProperties.PACKED_IN);
        String storedInOth = (String) reqData.get(TxnEnrollmentProperties.STORAGE_IN_OTHER);
        String packeInOth = (String) reqData.get(TxnEnrollmentProperties.PACKED_IN_OTHER);
       
        String cSeasonCode = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.CURR_SEASON_CODE)))
				?(String)reqData.get(TxnEnrollmentProperties.CURR_SEASON_CODE) : "";
				String lat= (String) reqData.get(TxnEnrollmentProperties.LATITUDE);
				String lon= (String) reqData.get(TxnEnrollmentProperties.LONGITUDE);
            Farmer farmer = new Farmer();
            Farm farm = new Farm();
            CropHarvest cropHarvest = new CropHarvest();
            
            Date harvestDate1=null;
            
            if (StringUtil.isEmpty(harvestDate)) {
            	throw new SwitchException(SwitchErrorCodes.EMPTY_HARVEST_DATE);
            }
            else
            {
                try {
                	harvestDate1 = DateUtil.convertStringToDate(harvestDate, DateUtil.DATE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
                if (StringUtil.isEmpty(farmerId)){
                    throw new SwitchException(SwitchErrorCodes.EMPTY_FARMER);
                }
                farmer = farmerService.findFarmerByFarmerId(farmerId);
                if (ObjectUtil.isEmpty(farmer)) {
                    throw new SwitchException(SwitchErrorCodes.FARMER_NOT_EXIST);
                }
                
                if (StringUtil.isEmpty(farmId)){
                    throw new SwitchException(SwitchErrorCodes.EMPTY_FARM);
                }
				farm = farmerService.findFarmByCode(farmId);
                if (ObjectUtil.isEmpty(farm))
                    throw new SwitchException(SwitchErrorCodes.FARM_NOT_EXIST);
                
               /* if (StringUtil.isEmpty(totalQty)){
                    throw new SwitchException(SwitchErrorCodes.EMPTY_TOTAL_QTY);
                }
               */
                if (StringUtil.isEmpty(receiptNo)){
                    throw new SwitchException(SwitchErrorCodes.EMPTY_RECEIPT_NO);
                }
               
               
                cropHarvest.setHarvestDate(harvestDate1);
                cropHarvest.setFarmerId(farmerId);
                cropHarvest.setFarmCode(farm.getFarmCode());
                cropHarvest.setFarmerName(farmer.getName());
                cropHarvest.setFarmName(farm.getFarmName());
                cropHarvest.setAgentId(agentId);
                cropHarvest.setReceiptNo(receiptNo);
                cropHarvest.setBranchId(branchId);
                cropHarvest.setSeasonCode(cSeasonCode);
                cropHarvest.setStorageIn(storedIn);
                cropHarvest.setOtherStorageInType(storedInOth);
                cropHarvest.setPackedIn(packeIn);
                cropHarvest.setOtherPackedInType(packeInOth);
                cropHarvest.setLatitude(lat);
                cropHarvest.setLongitude(lon);

                /** FORMING CROP DETAIL OBJECT **/
                Set<CropHarvestDetails> cropHarvestList = new HashSet<CropHarvestDetails>();
                double totQty = 0;
                Collection collection = (Collection) reqData
                        .get(TxnEnrollmentProperties.CROP_LIST);
                List<com.sourcetrace.eses.txn.schema.Object> cropObject = collection.getObject();
                for (com.sourcetrace.eses.txn.schema.Object object : cropObject) {
                    List<Data> cropData = object.getData();
                    
                    CropHarvestDetails cropHarvestDetails = new CropHarvestDetails();
                    for (Data data : cropData) {
                        String key = data.getKey();
                        String value = data.getValue();
                       
                        
                        if (TxnEnrollmentProperties.CROP_TYPE.equalsIgnoreCase(key)) {
                            if (StringUtil.isEmpty(value)) {
                                throw new SwitchException(SwitchErrorCodes.EMPTY_CROP_TYPE);
                            }
                            cropHarvestDetails.setCropType(Integer.valueOf(value));
                        }
                        
                        if (TxnEnrollmentProperties.CROP_ID.equalsIgnoreCase(key)) {
                            if (StringUtil.isEmpty(value)) {
                                throw new SwitchException(SwitchErrorCodes.EMPTY_CROP_ID);
                            }
                            ProcurementProduct crop = productService.findProcurementProductByCode(value);
                            cropHarvestDetails.setCrop(crop);
                        }
                        
                        if (TxnEnrollmentProperties.CROP_VARIETY_ID.equalsIgnoreCase(key)) {
                            if (StringUtil.isEmpty(value)) {
                                throw new SwitchException(SwitchErrorCodes.EMPTY_CROP_VARIETY_ID);
                            }
                            ProcurementVariety variety = productService.findProcurementVarietyByCode(value);
                            cropHarvestDetails.setVariety(variety);
                        }
                        
                        if (TxnEnrollmentProperties.CROP_GRADE_CODE.equalsIgnoreCase(key)) {
                            if (StringUtil.isEmpty(value)) {
                                throw new SwitchException(SwitchErrorCodes.EMPTY_CROP_GRADE_CODE);
                            }
                            ProcurementGrade grade = productService.findProcurementGradeByCode(value);
                            cropHarvestDetails.setGrade(grade);
                        }
                        try {
                            if (TxnEnrollmentProperties.CROP_QUANTITY.equalsIgnoreCase(key)) {
                            	cropHarvestDetails.setQty(Double.valueOf(value));
                            }
                           
                             if (TxnEnrollmentProperties.CROP_PRICE.equalsIgnoreCase(key)) {
                            	 cropHarvestDetails.setPrice(!StringUtil.isEmpty(value)? Double.valueOf(value): 0.00);
                            }
                            
                            if (TxnEnrollmentProperties.CROP_SUB_TOTAL.equalsIgnoreCase(key)) {
                            	cropHarvestDetails.setSubTotal(!StringUtil.isEmpty(value)? Double.valueOf(value): 0.00);
                            }
                        } catch (Exception e) {
                            throw new SwitchException(SwitchErrorCodes.DATA_CONVERSION_ERROR);
                        }
          
                }
                    cropHarvestDetails.setCropHarvest(cropHarvest);
                    cropHarvestList.add(cropHarvestDetails);
                    double qty = Double.valueOf(cropHarvestDetails.getQty());
    				totQty += qty;
    				cropHarvest.setTotalQty(String.valueOf(totQty));
                    cropHarvest.setCropHarvestDetails(cropHarvestList); 
                    
                    
                }
                
                try {
                	productService.savecropHarvestAndCropHarvestDetails(cropHarvest);
                    
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
