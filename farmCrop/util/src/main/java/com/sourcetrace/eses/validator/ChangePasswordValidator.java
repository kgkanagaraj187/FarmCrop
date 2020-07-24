/*
 * ChangePasswordValidator.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.validator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.sourcetrace.eses.dao.IUserDAO;
import com.sourcetrace.eses.umgmt.entity.User;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.crypto.ICryptoUtil;

public class ChangePasswordValidator extends AbstractValidator{

    private IUserDAO userDAO;
    @Autowired
    private ICryptoUtil cryptoUtil;
    
   

    @Override
    protected Map<String, String> validate(Object object) {

        Map<String, String> errors = new HashMap<String, String>();
        if (!ObjectUtil.isEmpty(object) && object instanceof User) {
            User entity = (User) object;
            User eUser = null;

            if (!StringUtil.isEmpty(entity.getUsername()))
                eUser = userDAO.findByUsername(entity.getUsername());

            if (ObjectUtil.isEmpty(eUser)) {
                errors.put("user.oldPassword", "changePassword.user.notfound");
            }

            if (StringUtil.isEmpty(entity.getOldPassword())) {
                errors.put("user.oldPassword", "empty.oldPassword");
            }

            if (StringUtil.isEmpty(entity.getPassword())) {
                errors.put("user.password", "empty.newPassword");
            }
            else if (entity.getPassword().length() < User.PASSWD_MIN_LENGTH
             || entity.getPassword().length() > User.PASSWD_MAX_LENGTH) {
             errors.put("user.password", "changePassword.length.password");
             }

            if (StringUtil.isEmpty(entity.getConfirmPassword())) {
                errors.put("user.confirmPassword", "changePassword.empty.confirmPassword");
            }

            // password and confirmPassword should be same
            if (StringUtil.isEmpty(errors.get("user.password"))
                    && StringUtil.isEmpty(errors.get("user.confirmPassword"))) {
                if (!entity.getPassword().equalsIgnoreCase(entity.getConfirmPassword())) {
                    errors.put("user.password", "changePassword.password.mismatch");
                }
            }

            if (!ObjectUtil.isEmpty(eUser)) {

                // Validating oldPassword should match with users current
                // password
                if (StringUtil.isEmpty(errors.get("user.oldPassword"))) {
                    String oldPasswordToken = cryptoUtil.encrypt(StringUtil
                            .getMulipleOfEight(eUser.getUsername() + entity.getOldPassword()));
                    if (!oldPasswordToken.equalsIgnoreCase(eUser.getPassword())) {
                        errors.put("user.oldPassword", "changePassword.oldPassword.mismatch");
                    }
                }

                // Validating password is should not same as users current
                // password
                if (StringUtil.isEmpty(errors.get("user.password"))) {
                    String newPasswordToken = cryptoUtil.encrypt(StringUtil
                            .getMulipleOfEight(eUser.getUsername() + entity.getPassword()));
                    if (newPasswordToken.equalsIgnoreCase(eUser.getPassword())) {
                        errors.put("user.password", "changePassword.password.exists");
                    }
                }
            }
        }
        return errors;
    }

	public IUserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(IUserDAO userDAO) {
		this.userDAO = userDAO;
	}

	
}
