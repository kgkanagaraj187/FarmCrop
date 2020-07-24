/*
 * ChangePasswordAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.sourcetrace.eses.dao.IUserDAO;
import com.sourcetrace.eses.service.IUserService;
import com.sourcetrace.eses.umgmt.entity.User;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.crypto.ICryptoUtil;
import com.sourcetrace.eses.validator.AbstractValidator;
import com.sourcetrace.esesw.view.SwitchAction;

@SuppressWarnings("serial")
public class ChangePasswordAction extends SwitchAction {

    private User user;
    
    private IUserService userService;
    protected AbstractValidator validator;
    @Autowired
    private IUserDAO userDAO;
    @Autowired
    private ICryptoUtil cryptoUtil;

    /**
     * Edits the.
     * @return the string
     */
    public String edit() {

        return INPUT;
    }

    /**
     * Update.
     * @return the string
     */
    public String update() {

        if (!ObjectUtil.isEmpty(this.user)) {
            User eUser = userService.findUserByUserName(this.user.getUsername());
            if (!ObjectUtil.isEmpty(eUser)) {
                eUser.setChangePassword(true);
                eUser.setPassword(this.user.getPassword());
                userService.editUserCredential(eUser);
            }
        }
        return SUCCESS;
    }

//    @Override
//    protected Object getData() {
//
//        if (!ObjectUtil.isEmpty(this.user)) {
//            this.user.setUsername(getLoggedUser().getUsername());
//        }
//        return this.user;
//    }

    /**
     * Gets the user.
     * @return the user
     */
    public User getUser() {

        return user;
    }

    /**
     * Sets the user.
     * @param user the new user
     */
    public void setUser(User user) {

        this.user = user;
    }

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public AbstractValidator getValidator() {
		return validator;
	}

	public void setValidator(AbstractValidator validator) {
		this.validator = validator;
	}
	
	public void validate(){

        Map<String, String> errors = new HashMap<String, String>();
       if (!ObjectUtil.isEmpty(user)) 
        {
            User entity = user;
            User eUser = null;

            if (!StringUtil.isEmpty(entity.getUsername()))
                eUser = userDAO.findByUsername(entity.getUsername());

            if (ObjectUtil.isEmpty(eUser)) {
                errors.put("user.oldPassword", "changePassword.user.notfound");
                addActionError(getText("changePassword.user.notfound"));
            }

            if (StringUtil.isEmpty(entity.getOldPassword())) {
                errors.put("user.oldPassword", "changePassword.empty.oldPassword");
                addActionError(getText("changePassword.empty.oldPassword"));
            }

            if (StringUtil.isEmpty(entity.getPassword())) {
                errors.put("user.password", "changePassword.empty.password");
                addActionError(getText("changePassword.empty.password"));
            }
            else if (entity.getPassword().length() < User.PASSWD_MIN_LENGTH
             || entity.getPassword().length() > User.PASSWD_MAX_LENGTH) {
            	errors.put("user.password", "changePassword.length.password");
             	addActionError(getText("changePassword.length.password"));
             }

            if (StringUtil.isEmpty(entity.getConfirmPassword())) {
                errors.put("user.confirmPassword", "changePassword.empty.confirmPassword");
                addActionError(getText("changePassword.empty.confirmPassword"));
            }

            // password and confirmPassword should be same
            if (StringUtil.isEmpty(errors.get("user.password"))
                    && StringUtil.isEmpty(errors.get("user.confirmPassword"))) {
                if (!entity.getPassword().equalsIgnoreCase(entity.getConfirmPassword())) {
                    errors.put("user.password", "changePassword.password.mismatch");
                    addActionError(getText("changePassword.password.mismatch"));
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
                        addActionError(getText("changePassword.oldPassword.mismatch"));
                    }
                }

                // Validating password is should not same as users current
                // password
                if (StringUtil.isEmpty(errors.get("user.password"))) {
                    String newPasswordToken = cryptoUtil.encrypt(StringUtil
                            .getMulipleOfEight(eUser.getUsername() + entity.getPassword()));
                    if (newPasswordToken.equalsIgnoreCase(eUser.getPassword())) {
                        errors.put("user.password", "changePassword.password.exists");
                        addActionError(getText("changePassword.password.exists"));
                    }
                }
            }
        }
	}
}
