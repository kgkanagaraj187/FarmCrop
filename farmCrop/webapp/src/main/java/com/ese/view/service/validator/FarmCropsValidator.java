package com.ese.view.service.validator;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import com.ese.entity.util.ESESystem;
import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.IFarmCropsDAO;
import com.sourcetrace.eses.dao.IFarmerDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.FarmCrops;

public class FarmCropsValidator implements IValidator {

    private static final Logger logger = Logger.getLogger(FarmCropsValidator.class);
    private IFarmCropsDAO farmCropsDAO;
    private IFarmerDAO farmerDAO;
  
    /**
     * Gets the farm crops dao.
     * @return the farm crops dao
     */
    public IFarmCropsDAO getFarmCropsDAO() {

        return farmCropsDAO;
    }

    /**
     * Sets the farm crops dao.
     * @param farmCropsDAO the new farm crops dao
     */
    public void setFarmCropsDAO(IFarmCropsDAO farmCropsDAO) {

        this.farmCropsDAO = farmCropsDAO;
    }

    /**
     * @param farmerDAO the farmerDAO to set
     */
    public void setFarmerDAO(IFarmerDAO farmerDAO) {

        this.farmerDAO = farmerDAO;
    }

    /**
     * @return the farmerDAO
     */
    public IFarmerDAO getFarmerDAO() {

        return farmerDAO;
    }

    /**
     * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> validate(Object object) {

        ClassValidator<FarmCrops> farmCropsValidator = new ClassValidator<FarmCrops>(FarmCrops.class);
        FarmCrops aFarmCrops = (FarmCrops) object;
        HttpServletRequest httpRequest=ReflectUtil.getCurrentHttpRequest();
        String tenantId = (String) httpRequest.getSession().getAttribute(ISecurityFilter.TENANT_ID);
        tenantId = StringUtil.isEmpty(tenantId) ? ISecurityFilter.DEFAULT_TENANT_ID : tenantId;
        
        Map<String, String> errorCodes = new LinkedHashMap<String, String>();
        if (logger.isInfoEnabled()) {
            logger.info("validate(Object) " + aFarmCrops.toString());
        }
        InvalidValue[] values = null;

       
        if (ObjectUtil.isEmpty(aFarmCrops.getProcurementVariety()) || StringUtil.isEmpty(aFarmCrops.getProcurementVariety().getCode()) ||
                aFarmCrops.getProcurementVariety().getCode().equalsIgnoreCase("-1")|| aFarmCrops.getProcurementVariety().getCode().equalsIgnoreCase("0")) {
            errorCodes.put("emptyFarmCropsProcurementVariety", "empty.procurementVariety");
        }
        if(!tenantId.equalsIgnoreCase(ESESystem.GRIFFITH_TENANT_ID) && !tenantId.equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID) && !tenantId.equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)
        		&& !tenantId.equalsIgnoreCase("kenyafpo")){
	        if(aFarmCrops.getSeedSource() == null||aFarmCrops.getSeedSource().equalsIgnoreCase("")){
	       	 errorCodes.put("seedSource", "empty.seedSource");
	       }
        }
        if(tenantId.equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)){
        if(aFarmCrops.getCultiArea() == null||aFarmCrops.getCultiArea().equalsIgnoreCase("")){
        	 errorCodes.put("cropArea", "pattern.cropArea");
        }
       if(StringUtil.isEmpty(aFarmCrops.getSowingDate())){
        	errorCodes.put("cropArea", "pattern.sowingDate");
        }
        }
        
        if(tenantId.equalsIgnoreCase("griffith")){
        	if(aFarmCrops.getCultiArea() == null || aFarmCrops.getCultiArea().equalsIgnoreCase("")){
           	 errorCodes.put("cultiArea", "pattern.cultiArea");
           }
        	if(aFarmCrops.getType() == null || aFarmCrops.getType().equalsIgnoreCase("") ||  aFarmCrops.getType().equalsIgnoreCase("-1")){
           	 errorCodes.put("type", "pattern.type");
        	}
        	if(aFarmCrops.getStapleLength()==null || StringUtil.isEmpty(aFarmCrops.getStapleLength())){
              errorCodes.put("stapleLength", "pattern.stapleLength");
           }
        	if(aFarmCrops.getProdTrees()==null || StringUtil.isEmpty(aFarmCrops.getProdTrees())){
                errorCodes.put("prodTrees", "pattern.prodTrees");
             }
        	if(aFarmCrops.getEstYldPfx()==null || StringUtil.isEmpty(aFarmCrops.getEstYldPfx())|| aFarmCrops.getEstYldPfx().equalsIgnoreCase("0")){
                errorCodes.put("estYldPfx", "pattern.estYldPfx");
             }
        	/*if(aFarmCrops.getEstYldSfx()==null || StringUtil.isEmpty(aFarmCrops.getEstYldSfx())|| aFarmCrops.getEstYldSfx().equalsIgnoreCase("0")){
                errorCodes.put("estYldSfx", "pattern.estYldSfx");
             }*/
        	
        }
        if(tenantId.equalsIgnoreCase("ecoagri")){
        	if(aFarmCrops !=null && StringUtil.isEmpty(aFarmCrops.getSeedSource())){
           	 errorCodes.put("seedSource", "empty.seedSource");
           	 
            }
        }
        if(tenantId.equalsIgnoreCase("iccoa")){
            if(aFarmCrops.getSeedSource() == null||aFarmCrops.getSeedSource().equalsIgnoreCase("")){
            	 errorCodes.put("seedSource", "empty.seedSource");
            }
        }
 /* FarmCrops aFarmCro= farmCropsDAO.findFarmCropsrepeated(aFarmCrops.getProcurementVariety().getId(),aFarmCrops.getType(),aFarmCrops.getCropCategory());
        if(aFarmCro.getCropName()!=null &&aFarmCro.getCropName()!=aFarmCrops.getCropName() &&aFarmCro.getProcurementVariety().getId()!=null&& aFarmCro.getProcurementVariety().getId()!=aFarmCrops.getProcurementVariety().getId() && aFarmCro.getCropCategory()!=aFarmCrops.getCropCategory()){
      
     aFarmCro.getProcurementVariety().getProcurementProduct().getName();
     if(!ObjectUtil.isEmpty(aFarmCro)){
        	 errorCodes.put("alreadyExist", "empty.procurementVariety");
       } 
        }*/
    /* if(aFarmCrops !=null && aFarmCrops.getId()!= aFarmCrops.getId()){
    	 errorCodes.put("emptyFarmCropsProcurementVariety", "empty.procurementVariety");
    	 
     }*/
        
       /*values = productValidator.getInvalidValues(aProcurementProduct, "name");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
*/
     /*   if (values == null || values.length == 0) {
            ProcurementProduct eProcurementProduct = productDistributionDAO
                    .findProcurementProductByNameAndBranch(aProcurementProduct.getName(),branchId_F);
            if (eProcurementProduct != null
                    && aProcurementProduct.getId() != eProcurementProduct.getId()) {
                errorCodes.put("name", "unique.ProcurementProductName");
            }
        }*/
        /*if (StringUtil.isEmpty(aFarmCrops.getFarm().getFarmCode())) {
            errorCodes.put("farmCode", "empty.farmCode");
        }*/
        
        /*if(!ObjectUtil.isEmpty(aFarmCrops.getFarm()) && !ObjectUtil.isEmpty(aFarmCrops.getFarm().getFarmer())){
            if(aFarmCrops.getFarm().getFarmer().getCertificationType()!=0){
                if(aFarmCrops.getCropSeason()==-1){
                    errorCodes.put("farmCropSeason", "empty.cropSeason");
                }
                if(aFarmCrops.getCropCategory()==-1){
                    errorCodes.put("farmCropCategory", "empty.cropCategory");
                }
            }
        }
        */
        /*values = farmCropsValidator.getInvalidValues(aFarmCrops, "cropArea");
        if (values.length == 0) {
            Double cropArea = Double.valueOf(aFarmCrops.getCropArea());
            if (cropArea == 0) {
                errorCodes.put("cropArea", "pattern.cropArea");
            }else {
                Farm farm = farmerDAO.findFarmByFarmCode(aFarmCrops.getFarm().getFarmCode());
                if (!ObjectUtil.isEmpty(farm) && !ObjectUtil.isEmpty(farm.getFarmer()) && farm.getFarmer().getCertificationType() == Farmer.CERTIFICATION_TYPE_NONE) {
                    try {
                        Double landInProduction = Double.parseDouble(farm.getLandInProduction());
                        if (landInProduction < cropArea) {
                            errorCodes.put("totalArea", "totalArea.cropArea");
                        }
                    } catch (Exception e) {
                        errorCodes.put("errorprocessing", "error.processing");
                    }
                }
            }
        }
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }*/

        //values = farmCropsValidator.getInvalidValues(aFarmCrops, "productionPerYear");
       /* if (values.length == 0) {
            if (Double.valueOf(aFarmCrops.getProductionPerYear()) == 0) {
                errorCodes.put("productionPerYear", "pattern.productionPerYear");
            }
        }*/
        values = farmCropsValidator.getInvalidValues(aFarmCrops);
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        	/*if (StringUtil.isEmpty(aFarmCrops.getInterAcre())) {
           	 errorCodes.put("empty.interAcre", "empty.interAcre");
           }
           
           if (StringUtil.isEmpty(aFarmCrops.getTotalCropHarv())) {
          	 errorCodes.put("empty.totalCropHarv", "empty.totalCropHarv");
           }
           
           if (StringUtil.isEmpty(aFarmCrops.getGrossIncome())) {
          	 errorCodes.put("empty.grossIncome", "empty.grossIncome");
           }	*/
        return errorCodes;
    }

}
