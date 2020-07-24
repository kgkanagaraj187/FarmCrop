package com.sourcetrace.esesw.view.general.validator;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.IProductDistributionDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.ValidationUtil;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;

public class ProcurementVarietyValidator implements IValidator {

    private IProductDistributionDAO productDistributionDAO;

    @Override
    public Map<String, String> validate(Object object) {

        ClassValidator productValidator = new ClassValidator(ProcurementVariety.class);
        ProcurementVariety aProcurementVariety = (ProcurementVariety) object;
        Map<String, String> errorCodes = new LinkedHashMap<String, String>();
        
        HttpServletRequest httpRequest=ReflectUtil.getCurrentHttpRequest();
        String branchId_F=(String) httpRequest.getSession().getAttribute(ISecurityFilter.CURRENT_BRANCH);
        String tenantId = (String) httpRequest.getSession().getAttribute(ISecurityFilter.TENANT_ID);
        tenantId = StringUtil.isEmpty(tenantId) ? ISecurityFilter.DEFAULT_TENANT_ID : tenantId;
        
        InvalidValue[] values = null;

        values = productValidator.getInvalidValues(aProcurementVariety, "code");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }        
     
        if (values == null || values.length == 0) {
            ProcurementVariety eProcurementVariety = productDistributionDAO.findProcurementVariertyByCode(aProcurementVariety.getCode());
            if (eProcurementVariety != null && !aProcurementVariety.getId().equals(eProcurementVariety.getId())) {
                errorCodes.put("code", "unique.ProcurementVarietyCode");
            }
        }      
        
        if (values == null || values.length == 0) {
            ProcurementVariety eProcurementVariety = productDistributionDAO.findProcurementVariertyByNameAndBranch(aProcurementVariety.getName(),branchId_F); 
            if (eProcurementVariety != null && aProcurementVariety.getId() != eProcurementVariety.getId()) {
                errorCodes.put("name", "unique.ProcurementVarietyName");
            }
        }
        
       /* if (!StringUtil.isEmpty(aProcurementVariety.getCode())) {
            if (!ValidationUtil.isPatternMaches(aProcurementVariety.getCode(),ValidationUtil.ALPHANUMERIC_PATTERN)) {
                errorCodes.put("pattern.code", "pattern.code");
            }
        }else{
            errorCodes.put("empty.code","empty.code");
        }
        */
       
        if (!StringUtil.isEmpty(aProcurementVariety.getName())) {
            if (!ValidationUtil.isPatternMaches(aProcurementVariety.getName(),ValidationUtil.ALPHANUMERIC_PATTERN)) {
                errorCodes.put("pattern.name", "pattern.name");
            }
        }else{
            errorCodes.put("empty.name","empty.name");
        }
        
        if(!StringUtil.isEmpty(tenantId)&&tenantId.equalsIgnoreCase("kpf")||tenantId.equalsIgnoreCase("wub")||tenantId.equalsIgnoreCase("gar")||tenantId.equalsIgnoreCase("simfed")){
        if(StringUtil.isEmpty(aProcurementVariety.getNoDaysToGrow())){
        	errorCodes.put("empty.noDaysToGrow","empty.noDaysToGrow");
        }
        
        if("0.00".equalsIgnoreCase(aProcurementVariety.getYield())){
        	errorCodes.put("empty.yield","empty.yield");
        }
        
        
        if(StringUtil.isEmpty(aProcurementVariety.getHarvestDays())){
        	errorCodes.put("empty.harvestDays","empty.harvestDays");
        }
        }
        
        return errorCodes;
    }

    public IProductDistributionDAO getProductDistributionDAO() {

        return productDistributionDAO;
    }

    public void setProductDistributionDAO(IProductDistributionDAO productDistributionDAO) {

        this.productDistributionDAO = productDistributionDAO;
    }

}
