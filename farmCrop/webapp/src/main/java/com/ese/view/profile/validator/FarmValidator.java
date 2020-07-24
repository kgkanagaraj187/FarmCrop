/*
 * FarmValidator.java
 * Copyright (c) 2012-2013, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.profile.validator;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import com.ese.entity.util.ESESystem;
import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.IFarmerDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.ValidationUtil;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmDetailedInfo;

public class FarmValidator implements IValidator {

    private static final Logger logger = Logger.getLogger(FarmValidator.class);
    private IFarmerDAO farmerDAO;

    /**
     * Gets the farmer dao.
     * @return the farmer dao
     */
    public IFarmerDAO getFarmerDAO() {

        return farmerDAO;
    }

    /**
     * Sets the farmer dao.
     * @param farmerDAO the new farmer dao
     */
    public void setFarmerDAO(IFarmerDAO farmerDAO) {

        this.farmerDAO = farmerDAO;
    }

    /**
     * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
     */
    public Map<String, String> validate(Object object) {

        ClassValidator<Farm> farmValidator = new ClassValidator<Farm>(Farm.class);
        ClassValidator farmDetailValidator = new ClassValidator(FarmDetailedInfo.class);
        HttpServletRequest httpRequest=ReflectUtil.getCurrentHttpRequest();
        String branchId_F=(String) httpRequest.getSession().getAttribute(ISecurityFilter.CURRENT_BRANCH);
        String tenantId = (String) httpRequest.getSession().getAttribute(ISecurityFilter.TENANT_ID);
        tenantId = StringUtil.isEmpty(tenantId) ? ISecurityFilter.DEFAULT_TENANT_ID : tenantId;
        Farm aFarm = (Farm) object;

        Map<String, String> errorCodes = new LinkedHashMap<String, String>();
        if (logger.isInfoEnabled()) {
            logger.info("validate(Object) " + aFarm.toString());
        }

        InvalidValue[] values = null;
        if(!tenantId.equalsIgnoreCase("griffith")){
            if (!StringUtil.isEmpty(aFarm.getFarmName())) {
            	 if(!tenantId.equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)){
              	 if (!ValidationUtil.isPatternMaches(aFarm.getFarmName(),ValidationUtil.ALPHANUMERIC_PATTERN)) {
              	 errorCodes.put("pattern.farmName", "pattern.farmName");      
              	 }}
              	if(tenantId!=null){
                Farm eFarm = farmerDAO.findFarmByFarmNameAndFarmerId(aFarm.getFarmName(),aFarm.getFarmer().getId());
                if (eFarm != null && eFarm.getId() != aFarm.getId()) {
                    errorCodes.put("farmName", "unique.farmName");
                }
              	}
              }else{
           	   if(tenantId!=null)
           		   errorCodes.put("empty.farmName", "empty.farmName");
              }
        }
     
   
        /*if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
                && !StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getFarmAddress())) {
        	 if (!ValidationUtil.isPatternMaches(aFarm.getFarmDetailedInfo().getFarmAddress(),ValidationUtil.ALPHANUMERIC_PATTERN)) {
               	 errorCodes.put("pattern.farmAddress", "pattern.farmAddress");              
        
        }
        }*/
        
        if(!tenantId.equalsIgnoreCase("griffith") && !tenantId.equalsIgnoreCase("susagri")){
        	if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
    				&& StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getTotalLandHolding())) {
    			errorCodes.put("totalLandHolding", "pattern.totalLandHolding");

    		}
        	/*if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
    				&& StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getFarmOwned())) {
    			errorCodes.put("farmOwned", "pattern.farmOwned");

    		} */	
        }
        if(!tenantId.equalsIgnoreCase("griffith")){
        	if(!tenantId.equalsIgnoreCase("olivado")){
                if(!tenantId.equalsIgnoreCase("symrise") && !tenantId.equalsIgnoreCase("gsma") && !tenantId.equalsIgnoreCase("susagri")){
        		if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
        				&& StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getTotalLandHolding())) {
        			errorCodes.put("totalLandHolding", "pattern.totalLandHolding");

        		}}
                }
                else{
        		if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
        				&& StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getProposedPlantingArea())) {
        			errorCodes.put("proposedPlantingArea", "pattern.proposedPlantingAreas");

        		}
                
        		if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
        				&& StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getProcessingActivity())
        				|| aFarm.getFarmDetailedInfo().getProcessingActivity() == 0) {
        			errorCodes.put("pattern.processingActivity", "pattern.processingActivity");

        		}

        		if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
        				&& StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getFullTimeWorkersCount())) {
        			errorCodes.put("fullTimeWorkersCount", "pattern.fullTimeWorkersCount");

        		}
                
        		if (StringUtil.isEmpty(aFarm.getPresenceBananaTrees())) {
        			errorCodes.put("presenceBananaTrees", "pattern.presenceBananaTrees");

        		}
        		
        		
        		if (StringUtil.isEmpty(aFarm.getPresenceHiredLabour())) {
        			errorCodes.put("presenceHiredLabour", "pattern.presenceHiredLabour");

        		}

        /*		if (StringUtil.isEmpty(aFarm.getRiskCategory())) {
        			errorCodes.put("riskCategory", "pattern.riskCategory");

        		}*/
        		
        	/*	if (StringUtil.isEmpty(aFarm.getLeasedLand())) {
        		errorCodes.put("leasedLand", "pattern.leasedLand");

        	      }*/
                }
                
        }
        
		
       if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
            && !StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getFarmArea())&& (aFarm.getFarmDetailedInfo().getFarmArea().trim().length() > 0)) {
      		 if (Double.valueOf(aFarm.getFarmDetailedInfo().getFarmArea()) == 0||!ValidationUtil.isPatternMaches(aFarm.getFarmDetailedInfo().getFarmArea(),ValidationUtil.NUMBER_PATTERN)){
                   errorCodes.put("farmArea", "pattern.farmArea");             
       		 }
             
          }

        if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
                && !StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getFarmProductiveArea())
                && (aFarm.getFarmDetailedInfo().getFarmProductiveArea().trim().length() > 0)){
                    if (Double.valueOf(aFarm.getFarmDetailedInfo().getFarmProductiveArea()) == 0){ 
                        errorCodes.put("farmProductiveArea", "pattern.productiveArea");
                    }
            }
            

        if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
                && !StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getFarmConservationArea())
                && (aFarm.getFarmDetailedInfo().getFarmConservationArea().trim().length() > 0)) {
            
                    if (Double.valueOf(aFarm.getFarmDetailedInfo().getFarmConservationArea()) == 0){ 
                        errorCodes.put("farmConservationArea", "pattern.conservationArea");
            }
        }

        if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
                && !StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getWaterBodiesCount())) {
        	 if (!ValidationUtil.isPatternMaches(aFarm.getFarmDetailedInfo().getWaterBodiesCount(),ValidationUtil.NUMBER_PATTERN)) {
                 errorCodes.put("pattern.waterBodiesCount", "pattern.waterBodiesCount");
        	 }
        }

        if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
                && !StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getAreaUnderIrrigation())
                && (aFarm.getFarmDetailedInfo().getAreaUnderIrrigation().trim().length() > 0)) {
            try {
                Double.valueOf(aFarm.getFarmDetailedInfo().getAreaUnderIrrigation());
            } catch (Exception e) {
                errorCodes.put("areaUnderIrrigation", "Please enter valid Area Under Irrigation");
            }
        }
        
        if(aFarm.getLatitude()!=null  && !aFarm.getLatitude().isEmpty() && !aFarm.getLatitude().contains(",,")){
        	if(!aFarm.getLatitude().matches(ValidationUtil.LATITUDE_PATTERN)){
        		  errorCodes.put("pattern.latitude", "pattern.latitude");
        	}
        }
        
        if(aFarm.getLongitude()!=null && !aFarm.getLongitude().isEmpty()&& !aFarm.getLongitude().contains(",,")){
        	if(!aFarm.getLongitude().matches(ValidationUtil.LONGITUDE_PATTERN)){
        		  errorCodes.put("pattern.longitude", "pattern.longitude");
        	}
        }
        if(tenantId!=null && tenantId.equalsIgnoreCase("iccoa")){
        	if(aFarm.getFarmIcsConv()!=null && !ObjectUtil.isEmpty(aFarm.getFarmIcsConv())){
                if(aFarm.getFarmIcsConv().getScope().isEmpty() || aFarm.getFarmIcsConv().getIcsType().isEmpty() || aFarm.getFarmIcsConv().getInspectionDateString().isEmpty()){
                	errorCodes.put("pattern.landIcs", "pattern.landIcs");
                }
        	}
        }
        if(tenantId!=null && !tenantId.equalsIgnoreCase("iccoa") && !tenantId.equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)){
        int i = 0;
        if(aFarm.getFarmIcsConv()!=null && !ObjectUtil.isEmpty(aFarm.getFarmIcsConv())){
            if(aFarm.getFarmIcsConv().getIcsType()!=null && !aFarm.getFarmIcsConv().getIcsType().isEmpty()){
                i++;
                
            }
           
            if(aFarm.getFarmIcsConv().getInspectionDateString()!=null && !aFarm.getFarmIcsConv().getInspectionDateString().isEmpty()){
                i++;
                
            }
            if(aFarm.getFarmIcsConv().getInspectorName()!=null && !aFarm.getFarmIcsConv().getInspectorName().isEmpty()){
                i++;
                
            }
            if(aFarm.getFarmIcsConv().getQualified()==1 || aFarm.getFarmIcsConv().getQualified()==0 ){
                i++;
                
            }
            
        /*    if(i<4 && i>0){
            	
            		errorCodes.put("empty.icsDetails", "empty.icsDetails");
            
            	else
            	{
            		errorCodes.put("empty.icsConv", "empty.icsConv");
            	}
            }*/


        }
        }
        if(tenantId.equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)){
        	if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
    				&& StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getTotalLandHolding())) {
    			errorCodes.put("totalLandHolding", "pattern.totalLandHolding");

    		}
        	if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
			&& StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getFarmOwned()) || aFarm.getFarmDetailedInfo().getFarmOwned().equals("-1")) {
        			errorCodes.put("farmOwned", "pattern.farmOwned");

        	} 
        	if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo()) && StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getFarmAddress())) {
    	 
           	 errorCodes.put("pattern.farmAddress", "pattern.farmAddress");              
        	}
        	
        if(aFarm.getId()!=0 && StringUtil.isEmpty(aFarm.getFarmImage()) && !ObjectUtil.isEmpty(aFarm.getPhoto()) )
			{
					errorCodes.put("empty.farmerPhoto", "empty.farmerPhoto");
			}
			
			if(aFarm.getId()==0 && StringUtil.isEmpty(aFarm.getFarmImage()))
			{
				
				errorCodes.put("empty.farmPhoto", "empty.farmPhoto");
			}
        	
        	if(StringUtil.isEmpty(aFarm.getLandTopology()) || aFarm.getLandTopology().equals("-1")){
        		errorCodes.put("empty.landTopology", "empty.landTopology");
        	}
        	if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
    				&& StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getFullTimeWorkersCount())) {
    			errorCodes.put("fullTimeWorkersCount", "pattern.fullTimeWorkersCount");

    		}
        	if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
    				&& StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getSeasonalWorkersCount())) {
    			errorCodes.put("seasonalWorkersCount", "pattern.seasonalWorkersCount");

    		}
        	if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
    				&& StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getLastDateOfChemicalApplication())) {
    			errorCodes.put("lastDateOfChemicalApplication", "pattern.lastDateOfChemicalApplication");

    		}
        	if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
    				&& StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getConventionalLand())) {
    			errorCodes.put("conventionalLand", "pattern.conventionalLand");

    		}
        	if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
    				&& StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getFallowOrPastureLand())) {
    			errorCodes.put("fallowOrPastureLand", "pattern.fallowOrPastureLand");

    		}
        }
        
        
/*        if(tenantId.equalsIgnoreCase(ESESystem.FARM_AGG) ){
        	if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
        			&& StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getFarmIrrigation()) || aFarm.getFarmDetailedInfo().getFarmIrrigation().equals("-1")) {
    			errorCodes.put("irrigationSource", "pattern.irrigationSource");
    		}
        	
        }*/
        
        if(tenantId.equalsIgnoreCase(ESESystem.FARM_AGG) && branchId_F!=null && !branchId_F.equals("Corporate") ){
        
        	if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
    				&& StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getFullTimeWorkersCount())) {
    			errorCodes.put("fullTimeWorkersCount", "pattern.fullTimeWorkersCount");

    		}
        	
        	if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
    				&& StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getPartTimeWorkersCount())) {
    			errorCodes.put("partTimeWorkersCount", "pattern.partTimeWorkersCount");

    		}
       
        
        	if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
    				&& StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getSeasonalWorkersCount())) {
    			errorCodes.put("seasonalWorkersCount", "pattern.seasonalWorkersCount");

    		}
        	
        }
        
       /* if(tenantId!=null && tenantId.equalsIgnoreCase("iccoa")){
          
            if(aFarm.getFarmIcsConv()!=null && !ObjectUtil.isEmpty(aFarm.getFarmIcsConv())){
                if(aFarm.getFarmIcsConv().getIcsType()==null && aFarm.getFarmIcsConv().getIcsType().isEmpty()){
                	errorCodes.put("empty.icsType", "empty.icsType");
                    
                }            
                          
            }
            }*/

        /*  if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
                && !StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getFullTimeWorkersCount())) {
            values = farmDetailValidator.getInvalidValues(aFarm.getFarmDetailedInfo(),
                    "fullTimeWorkersCount");
            for (InvalidValue value : values) {
                errorCodes.put(value.getPropertyName(), value.getMessage());
            }
        }

        if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
                && !StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getPartTimeWorkersCount())) {
            values = farmDetailValidator.getInvalidValues(aFarm.getFarmDetailedInfo(),
                    "partTimeWorkersCount");
            for (InvalidValue value : values) {
                errorCodes.put(value.getPropertyName(), value.getMessage());
            }
        }

        if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
                && !StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getSeasonalWorkersCount())) {
            values = farmDetailValidator.getInvalidValues(aFarm.getFarmDetailedInfo(),
                    "seasonalWorkersCount");
            for (InvalidValue value : values) {
                errorCodes.put(value.getPropertyName(), value.getMessage());
            }
        }
        
        if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
                && !StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getFallowOrPastureLand())
                && (aFarm.getFarmDetailedInfo().getFallowOrPastureLand().trim().length() > 0)) {
            values = farmDetailValidator.getInvalidValues(aFarm.getFarmDetailedInfo(),"fallowOrPastureLand");
            if (values.length == 0) {
                    if (Double.valueOf(aFarm.getFarmDetailedInfo().getFallowOrPastureLand()) == 0) 
                        errorCodes.put("fallowOrPastureLand", "pattern.pastureLand");
            }
            for (InvalidValue value : values) {
                errorCodes.put(value.getPropertyName(), value.getMessage());
            }
        }
        if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
                && !StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getConventionalLand())
                && (aFarm.getFarmDetailedInfo().getConventionalLand().trim().length() > 0)) {
            values = farmDetailValidator.getInvalidValues(aFarm.getFarmDetailedInfo(),"conventionalLand");
            if (values.length == 0) {
                    if (Double.valueOf(aFarm.getFarmDetailedInfo().getConventionalLand()) == 0) 
                        errorCodes.put("conventionalLand", "pattern.conventionalLand");
            }
            for (InvalidValue value : values) {
                errorCodes.put(value.getPropertyName(), value.getMessage());
            }
        }
        if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
                && !StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getConventionalCrops())
                && (aFarm.getFarmDetailedInfo().getConventionalCrops().trim().length() > 0)) {
            values = farmDetailValidator.getInvalidValues(aFarm.getFarmDetailedInfo(),"conventionalCrops");
            if (values.length == 0) {
                    if (Double.valueOf(aFarm.getFarmDetailedInfo().getConventionalLand()) == 0) 
                        errorCodes.put("conventionalCrops", "pattern.conventionalCrops");
            }
            for (InvalidValue value : values) {
                errorCodes.put(value.getPropertyName(), value.getMessage());
            }
        }
        if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
                && !StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getConventionalEstimatedYield())
                && (aFarm.getFarmDetailedInfo().getConventionalEstimatedYield().trim().length() > 0)) {
            values = farmDetailValidator.getInvalidValues(aFarm.getFarmDetailedInfo(),"conventionalEstimatedYield");
            if (values.length == 0) {
                    if (Double.valueOf(aFarm.getFarmDetailedInfo().getConventionalEstimatedYield()) == 0) 
                        errorCodes.put("conventionalEstimatedYield", "pattern.estimatedYield");
            }
            for (InvalidValue value : values) {
                errorCodes.put(value.getPropertyName(), value.getMessage());
            }
        }
        */
        if(!tenantId.equalsIgnoreCase("olivado") && !tenantId.equalsIgnoreCase("griffith")){
        if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo()) && !aFarm.getFarmDetailedInfo().getProposedPlantingArea().isEmpty() 
        		&& (aFarm.getFarmDetailedInfo().getTotalLandHolding()!=null) && !StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getTotalLandHolding())){
            try{
                 if(Double.valueOf(aFarm.getFarmDetailedInfo().getProposedPlantingArea()) > Double.valueOf(aFarm.getFarmDetailedInfo().getTotalLandHolding())) {
                	 if(tenantId.equalsIgnoreCase("wilmar")){
                		 errorCodes.put("proposedPlantingArea", "pattern.organicArea");
                	 }else{
                		 errorCodes.put("proposedPlantingArea", "pattern.proposedPlantingArea");
                	 }
          
                    
                 }
                 }catch(Exception e){
                     errorCodes.put("proposedPlantingArea", "pattern.proposedPlantingArea.string");
                     
                 }
        
        
        }}
        
        if(tenantId.equalsIgnoreCase("gsma")){
        	if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
    				&& StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getTotalLandHolding())) {
    			errorCodes.put("totalLandHolding", "pattern.totalLandHoldingValue");

    		}
        }
        
        if(!StringUtil.isEmpty(aFarm.getTenantId()) && aFarm.getTenantId()!=null && aFarm.getTenantId().equalsIgnoreCase(ESESystem.AWI_TENANT_ID)){
            if(ObjectUtil.isEmpty(aFarm.getVillage())){
                errorCodes.put("empty.village", "empty.village");
            }
        }
        
        if(tenantId.equalsIgnoreCase("ocp")){
        	if (!ObjectUtil.isEmpty(aFarm.getFarmDetailedInfo())
    				&& StringUtil.isEmpty(aFarm.getFarmDetailedInfo().getTotalLandHolding())) {
    			errorCodes.put("totalLandHolding", "pattern.totalLandHoldingValues");

    		}
        }
        
        
        return errorCodes;
    }

}
