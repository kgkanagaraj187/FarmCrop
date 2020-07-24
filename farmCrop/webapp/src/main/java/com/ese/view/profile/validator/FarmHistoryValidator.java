package com.ese.view.profile.validator;

import java.util.Map;

import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.IFarmerDAO;

public class FarmHistoryValidator implements IValidator {

	private IFarmerDAO farmerDAO;
	@Override
	public Map<String, String> validate(Object object) {
		// TODO Auto-generated method stub
		return null;
	}
	   public void setFarmerDAO(IFarmerDAO farmerDAO) {

	        this.farmerDAO = farmerDAO;
	    }
	   public IFarmerDAO getFarmerDAO() {

	        return farmerDAO;
	    }

	
}
