/*
 * SurveyScopeValidator.java
 * Copyright (c) 2008-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.profile.validator;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import org.springframework.beans.factory.annotation.Autowired;
import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.ICertificationDAO;
import com.sourcetrace.eses.inspect.agrocert.SurveyMaster;
import com.sourcetrace.eses.inspect.agrocert.SurveyType;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.DataLevel;

public class SurveyMasterValidator implements IValidator {

	private static final Logger logger = Logger.getLogger(SurveyMasterValidator.class);
	@Autowired
	private ICertificationDAO certificationDAO;

	/**
	 * Validate.
	 * 
	 * @param object
	 *            the object
	 * @return the map< string, string>
	 * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
	 */
	@Override
	public Map<String, String> validate(Object object) {

		Map<String, String> errorCodes = new LinkedHashMap<String, String>();

		ClassValidator<SurveyMaster> classValidator = new ClassValidator<SurveyMaster>(SurveyMaster.class);
		SurveyMaster aSurveyMaster = (SurveyMaster) object;

		if (logger.isInfoEnabled()) {
			logger.info("validate(Object) " + aSurveyMaster.toString());
		}

		InvalidValue[] values = null;

		values = classValidator.getInvalidValues(aSurveyMaster, "name");
		for (InvalidValue value : values) {
			errorCodes.put(value.getPropertyName(), value.getMessage());
		}

		if (StringUtil.isEmpty(aSurveyMaster.getName())) {
			errorCodes.put("name", "empty.name");
		}

		if (ObjectUtil.isEmpty(aSurveyMaster.getSurveyType()) || aSurveyMaster.getSurveyType().getId() < 1) {
			errorCodes.put("surveyType", "empty.surveyType");
		}
		
		if(aSurveyMaster.getDataLevel()!=null && aSurveyMaster.getDataLevel().getCode()!=null){
			DataLevel dataLevel = certificationDAO.findDataLevelByCode(aSurveyMaster.getDataLevel().getCode());
			if(dataLevel==null){
				errorCodes.put("dataLevel", "empty.dataLevel");
			}
			aSurveyMaster.setDataLevel(dataLevel);
		}else{
			errorCodes.put("dataLevel", "empty.dataLevel");
		}


		if (StringUtil.isListEmpty(aSurveyMaster.getQuestionCodes())) {
			errorCodes.put("name", "empty.QuestionCodes");
		}
		return errorCodes;
	}

}
