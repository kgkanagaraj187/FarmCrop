package com.sourcetrace.esesw.view.general.validator;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import com.ese.entity.txn.training.Observations;
import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.ITrainingDAO;
import com.sourcetrace.eses.util.StringUtil;

public class ObservationsValidator implements IValidator{
	private static final Logger logger = Logger.getLogger(ObservationsValidator.class);
	 private ITrainingDAO trainingDAO;
	@Override
	public Map<String, String> validate(Object object) {

       ClassValidator observationValidator = new ClassValidator(ObservationsValidator.class);
       Observations aObservationValidator = (Observations) object;
       Map<String, String> errorCodes = new LinkedHashMap<String, String>();
       if (logger.isInfoEnabled()) {
           logger.info("validate(Object) " + aObservationValidator.toString());
       }
       InvalidValue[] values = null;

       values = observationValidator.getInvalidValues(aObservationValidator, "code");
       for (InvalidValue value : values) {
           errorCodes.put(value.getPropertyName(), value.getMessage());
       }

    

       values = observationValidator.getInvalidValues(aObservationValidator, "name");
       for (InvalidValue value : values) {
           errorCodes.put(value.getPropertyName(), value.getMessage());
       }
       if(StringUtil.isEmpty(aObservationValidator.getName())){
       	errorCodes.put("name","empty.trainingObervations");
       }

       if (values == null || values.length == 0) { 
       	Observations eObservations = null;//trainingDAO.findObservationsByName(aObservationValidator.getName());
           if (eObservations != null && aObservationValidator.getId() != eObservations.getId()) {
               errorCodes.put("name", "unique.trainingObservationName");
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

