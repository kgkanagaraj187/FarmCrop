package com.sourcetrace.esesw.view.general.validator;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import com.ese.entity.txn.training.Observations;
import com.ese.entity.txn.training.TrainingMaterial;
import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.ITrainingDAO;
import com.sourcetrace.eses.util.StringUtil;

public class TrainingMaterialValidator implements IValidator {
	private static final Logger logger = Logger.getLogger(ObservationsValidator.class);
	 private ITrainingDAO trainingDAO;
	@Override
	public Map<String, String> validate(Object object) {

      ClassValidator trainingValidator = new ClassValidator(TrainingMaterialValidator.class);
      TrainingMaterial atrainingMaterialValidator = (TrainingMaterial) object;
      Map<String, String> errorCodes = new LinkedHashMap<String, String>();
      if (logger.isInfoEnabled()) {
          logger.info("validate(Object) " + atrainingMaterialValidator.toString());
      }
      InvalidValue[] values = null;

      values = trainingValidator.getInvalidValues(atrainingMaterialValidator, "code");
      for (InvalidValue value : values) {
          errorCodes.put(value.getPropertyName(), value.getMessage());
      }

    /*  if (values == null || values.length == 0) {
          TrainingMethod eTrainingMethod = trainingDAO.findTrainingMethodByCode(aTrainingMethod
                  .getCode());
          if (eTrainingMethod != null && aTrainingMethod.getId() != eTrainingMethod.getId()) {
              errorCodes.put("code", "unique.trainingMethodCode");
          }
      }*/

      values = trainingValidator.getInvalidValues(atrainingMaterialValidator, "name");
      for (InvalidValue value : values) {
          errorCodes.put(value.getPropertyName(), value.getMessage());
      }
      if(StringUtil.isEmpty(atrainingMaterialValidator.getName())){
      	errorCodes.put("name","empty.trainingMaterialName");
      }

      if (values == null || values.length == 0) {
    	  TrainingMaterial eMaterial = trainingDAO.findTrainingMaterialByName(atrainingMaterialValidator.getName());
          if (eMaterial != null && atrainingMaterialValidator.getId() != eMaterial.getId()) {
              errorCodes.put("name", "unique.trainingMaterialName");
          }
      }

      return errorCodes;
  }
	public ITrainingDAO getTrainingDAO() {
		return trainingDAO;
	}
	public void setTrainingDAO(ITrainingDAO trainingDAO) {
		this.trainingDAO = trainingDAO;
	}

	
}
