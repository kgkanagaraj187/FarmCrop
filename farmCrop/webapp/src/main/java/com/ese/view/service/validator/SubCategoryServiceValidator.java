package com.ese.view.service.validator;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.ICategoryDAO;
import com.sourcetrace.eses.order.entity.profile.SubCategory;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.ValidationUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class SubCategoryServiceValidator.
 */
public class SubCategoryServiceValidator implements IValidator {

    private static final Logger logger = Logger.getLogger(SubCategoryServiceValidator.class);
    private ICategoryDAO categoryDAO;
    private static final String SELECT = "-1";
    private static final String CATEGORY_NOT_FOUND = "CategoryNotFound";
    private static final String SCATEGORY_NOT_FOUND_PROPERTY = "category.notfound";

    /**
     * Validate.
     * @param object the object
     * @return the map< string, string>
     * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> validate(Object object) {

        ClassValidator categoryValidator = new ClassValidator(SubCategory.class);
        SubCategory aSubCategory = (SubCategory) object;
        Map<String, String> errorCodes = new LinkedHashMap<String, String>();
        if (logger.isInfoEnabled()) {
            logger.info("validate(Object) " + aSubCategory.toString());
        }
        InvalidValue[] values = null;

        values = categoryValidator.getInvalidValues(aSubCategory, "code");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }

        if (values == null || values.length == 0) {
            SubCategory eSubCategory = categoryDAO.findSubCategoryByCode(aSubCategory.getCode());
            if (eSubCategory != null && aSubCategory.getId() != eSubCategory.getId()) {
                errorCodes.put("code", "unique.SubCategoryCode");
            }
        }

        values = categoryValidator.getInvalidValues(aSubCategory, "name");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }

        if (values == null || values.length == 0) {
            SubCategory eSubCategory = categoryDAO.findSubCategoryByName(aSubCategory.getName());
            if (eSubCategory != null && aSubCategory.getId() != eSubCategory.getId()) {
                errorCodes.put("name", "unique.SubCategoryName");
            }
        }
        
      /*  if (!StringUtil.isEmpty(aSubCategory.getCode())) {
            if (!ValidationUtil.isPatternMaches(aSubCategory.getCode(),ValidationUtil.ALPHANUMERIC_PATTERN)) {
                errorCodes.put("pattern.code", "pattern.code");
            }
        }else{
            errorCodes.put("empty.code","empty.code");
        }*/
        
        if (!StringUtil.isEmpty(aSubCategory.getName())) {
            if (!ValidationUtil.isPatternMaches(aSubCategory.getName(),ValidationUtil.ALPHANUMERIC_PATTERN)) {
                errorCodes.put("pattern.name", "pattern.name");
            }
        }else{
            errorCodes.put("empty.name","empty.name");
        }

        
        return errorCodes;
    }

    /**
     * Gets the category dao.
     * @return the category dao
     */
    public ICategoryDAO getCategoryDAO() {

        return categoryDAO;
    }

    /**
     * Sets the category dao.
     * @param categoryDAO the new category dao
     */
    public void setCategoryDAO(ICategoryDAO categoryDAO) {

        this.categoryDAO = categoryDAO;
    }

}
