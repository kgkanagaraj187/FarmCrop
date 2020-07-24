package com.sourcetrace.esesw.view.general.validator;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.IClientDAO;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.util.StringUtil;


public class MasterDataValidator implements IValidator
{
    /** Validate. */
    /** The Constant logger. */
    private static final Logger logger = Logger.getLogger(MasterDataValidator.class);

    private IClientDAO clientDAO;

    public void setClientDAO(IClientDAO clientDAO) {
		this.clientDAO = clientDAO;
	}

	/**
     * Validate.
     * @param object the object
     * @return the map< string, string>
     * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> validate(Object object) 
    {
        ClassValidator masterDataValidator = new ClassValidator(MasterData.class);
        MasterData masterData = (MasterData) object;
        
        Map<String, String> errorCodes = new LinkedHashMap<String, String>();
        if (logger.isInfoEnabled()) {
            logger.info("validate(Object) " + masterData.toString());
        }
        InvalidValue[] values = null;
        
        values = masterDataValidator.getInvalidValues(masterData, "name");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        
        if(StringUtil.isEmpty(masterData.getMasterType()) || masterData.getMasterType().equals("-1"))
        {            
        	errorCodes.put("masterType","empty.masterType");
        }
        
        if(StringUtil.isEmpty(masterData.getName()))
        {            
        	errorCodes.put("name","empty.name"); 
        }
       
        return errorCodes;
    }
}
