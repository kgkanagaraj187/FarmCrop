package com.sourcetrace.esesw.view.general.validator;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.IProductDistributionDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.ValidationUtil;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;

public class ProcurementGradeValidator implements IValidator {

    private IProductDistributionDAO productDistributionDAO;

    @Override
    public Map<String, String> validate(Object object) {

        Map<String, String> errorCodes = new LinkedHashMap<String, String>();
        ClassValidator productValidator = new ClassValidator(ProcurementGrade.class);
        ProcurementGrade aProcurementGrade = (ProcurementGrade) object;
        InvalidValue[] values = null;
        
        HttpSession httpSession=ReflectUtil.getCurrentHttpSession();
    	String branchId_F=(String)httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
       	
    	
        values = productValidator.getInvalidValues(aProcurementGrade, "code");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        
        if (!StringUtil.isEmpty(aProcurementGrade.getName())) {
            if (!ValidationUtil.isPatternMaches(aProcurementGrade.getName(),ValidationUtil.ALPHANUMERIC_PATTERN)) {
                errorCodes.put("pattern.name", "pattern.name");
            }
        }else{
            errorCodes.put("empty.name","empty.name");
        }
        
        
        if(!ObjectUtil.isEmpty(aProcurementGrade.getType())){
            if(aProcurementGrade.getType().getCode().equals("-1")){
                errorCodes.put("pattern.unit", "pattern.unit");
            }
          }
        
      
        if (StringUtil.isEmpty(aProcurementGrade.getPrice())) {
            try {
                if (getPriceDoubleValue(aProcurementGrade) <= 0.0) {
                    errorCodes.put("price", "empty.price");
                }
            } catch(Exception e) {
                errorCodes.put("price", "invalid.price");
            }
        }
        
        if (values == null || values.length == 0) {
            ProcurementGrade eProcurementGrade = productDistributionDAO.findProcurementGradeByCode(aProcurementGrade.getCode()); 
            if (eProcurementGrade != null && !aProcurementGrade.getId().equals(eProcurementGrade.getId())) {
                errorCodes.put("code", "unique.procurementGradeCode");
            }
        }
        
        if (values == null || values.length == 0) {
            ProcurementGrade eProcurementGrade = productDistributionDAO.findProcurementGradeByNameAndBranch(aProcurementGrade.getName(),branchId_F);
        	if(eProcurementGrade != null && aProcurementGrade.getId() != eProcurementGrade.getId()){
              errorCodes.put("name", "unique.procurementGradeName");            
        	}
        }
        
      /*  if (!StringUtil.isEmpty(aProcurementGrade.getCode())) {
            if (!ValidationUtil.isPatternMaches(aProcurementGrade.getCode(),ValidationUtil.ALPHANUMERIC_PATTERN)) {
                errorCodes.put("pattern.code", "pattern.code");
            }
        }else{
            errorCodes.put("empty.code","empty.code");
        }
        */
       
        return errorCodes;
    }
    
    public double getPriceDoubleValue(ProcurementGrade aProcurementGrade) {

        double price = 0.0;
        String priceString = "";
        if (!StringUtil.isEmpty(aProcurementGrade.getPricePrefix()))
            priceString = aProcurementGrade.getPricePrefix();
        if (!StringUtil.isEmpty(aProcurementGrade.getPriceSuffix()))
            priceString = priceString + "." + aProcurementGrade.getPriceSuffix();
        if (!StringUtil.isEmpty(priceString))
            price = Double.valueOf(priceString);

        return price;
    }
    
    public IProductDistributionDAO getProductDistributionDAO() {
    
        return productDistributionDAO;
    }

    public void setProductDistributionDAO(IProductDistributionDAO productDistributionDAO) {
    
        this.productDistributionDAO = productDistributionDAO;
    }

}
