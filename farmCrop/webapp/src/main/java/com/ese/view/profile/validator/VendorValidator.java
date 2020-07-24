package com.ese.view.profile.validator;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.IAgentDAO;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.ValidationUtil;
import com.sourcetrace.esesw.entity.profile.Vendor;

public class VendorValidator implements IValidator
{
    
    private static final Logger logger = Logger.getLogger(VendorValidator.class);
    private IAgentDAO agentDAO;
    
    /**
     * Validate.
     * @param object the object
     * @return the map< string, string>
     * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> validate(Object object) {

        // TODO Auto-generated method stub
        ClassValidator vendorValidator = new ClassValidator(Vendor.class);
        Vendor vendor = (Vendor) object;
        String STRING_PATTERN = "[a-zA-Z]+";  
        
        Map<String, String> errorCodes = new LinkedHashMap<String, String>();

        if (logger.isInfoEnabled())
        {
            logger.info("validate(Object) " + vendor.toString());
        }
        InvalidValue[] values = null;

       /* values = vendorValidator.getInvalidValues(vendor, "vendorName");
        for (InvalidValue value : values)
        {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }

        values = vendorValidator.getInvalidValues(vendor, "vendorAddress");
        for (InvalidValue value : values)
        {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        
        values = vendorValidator.getInvalidValues(vendor, "landLine");
        for (InvalidValue value : values)
        {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        values = vendorValidator.getInvalidValues(vendor, "personName");
        for (InvalidValue value : values) 
        {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        values = vendorValidator.getInvalidValues(vendor, "mobileNumber");
        for (InvalidValue value : values)
        {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        values = vendorValidator.getInvalidValues(vendor, "email");
        for (InvalidValue value : values) 
        {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }*/
        
        values = vendorValidator.getInvalidValues(vendor, "mobileNumber");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        
        if(!StringUtil.isEmpty(vendor.getVendorName())){
            if (!ValidationUtil.isPatternMaches(vendor.getVendorName(),ValidationUtil.ALPHANUMERIC_PATTERN)) {
                errorCodes.put("pattern.vendorName", "pattern.vendorName");
            }
        }else{
            errorCodes.put("empty.vendorName", "empty.vendorName");
        }
        
        if(!StringUtil.isEmpty(vendor.getPersonName())){
            if (!ValidationUtil.isPatternMaches(vendor.getPersonName(),ValidationUtil.NAME_PATTERN)) {
                errorCodes.put("personName.incorrect", "personName.incorrect");
            }
        }
        
        if(!StringUtil.isEmpty(vendor.getMobileNumber())){
            if (!ValidationUtil.isPatternMaches(vendor.getMobileNumber(),ValidationUtil.NUMBER_PATTERN)) {
                errorCodes.put("pattern.mobileNumber", "pattern.mobileNumber");
            }
        }
        if(!StringUtil.isEmpty(vendor.getLandLine())){
            if (!ValidationUtil.isPatternMaches(vendor.getLandLine(),ValidationUtil.NUMBER_PATTERN)) {
                errorCodes.put("pattern.landLine", "pattern.landLine");
            }
        }
        
        if(!StringUtil.isEmpty(vendor.getEmail())){
            if (!ValidationUtil.isPatternMaches(vendor.getEmail(),ValidationUtil.EMAIL_PATTERN)) {
                errorCodes.put("pattern.email", "pattern.email");
            }
        }
        

        return errorCodes;
    }

    public void setAgentDAO(IAgentDAO agentDAO) {

        this.agentDAO = agentDAO;
    }

    public IAgentDAO getAgentDAO() {

        return agentDAO;
    }  

}
