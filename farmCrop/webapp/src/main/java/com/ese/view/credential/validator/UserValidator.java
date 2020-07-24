package com.ese.view.credential.validator;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.IUserDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.umgmt.entity.User;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.ValidationUtil;

public class UserValidator implements IValidator {

    private static final Logger logger = Logger.getLogger(UserValidator.class);
    private IUserDAO userDAO;

    public void setUserDAO(IUserDAO userDAO) {

        this.userDAO = userDAO;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
     */
    public Map<String, String> validate(Object user) {

        HttpServletRequest httpRequest = ReflectUtil.getCurrentHttpRequest();
        String branchId_F = httpRequest.getParameter("branchId_F");
        String currentBrnch=(String) httpRequest.getSession().getAttribute(ISecurityFilter.CURRENT_BRANCH);

        User aUser = (User) user;
        Map<String, String> errorCodes = new LinkedHashMap<String, String>();
        if (logger.isInfoEnabled()) {
            logger.info("validate(Object) " + user.toString());
        }

        /*
         * if(!ObjectUtil.isEmpty(aUser)){ User existingUser =
         * userDAO.findByUsernameAndBranchId(aUser.getUsername(), branchId_F);
         * if(!ObjectUtil.isEmpty(existingUser) && aUser.getId() != existingUser.getId()) {
         * errorCodes.put("unique.userName", "unique.userName"); } }
         */

        if (aUser.isChangePassword()) {

            if (StringUtil.isEmpty(aUser.getPassword())// if password and confirm password is not
                                                       // given
                    || StringUtil.isEmpty(aUser.getConfirmPassword())) {
                errorCodes.put("user.password", "password.missing");
            } else if (!aUser.getPassword().equals(aUser.getConfirmPassword())) {// if password and
                                                                                 // confirm password
                                                                                 // is not matched
                errorCodes.put("user.password", "password.missmatch");
            }

            else if (aUser.getPassword().length() < 8) {
                errorCodes.put("user.password", "length.password");
            }

            else if (aUser.getPassword().length() > 16) {
                errorCodes.put("user.password", "length.password");
            }

        }
        if (!StringUtil.isEmpty(branchId_F)) {
            if (branchId_F.equals("-1")) {
                errorCodes.put("empty.branchId", "empty.branchId");
            }
        }
        if (!StringUtil.isEmpty(aUser.getUsername())) {
            if (aUser.getIsMultiBranch().equalsIgnoreCase("1")) {
                User existingUser = userDAO.findUserByUserNameExcludeBranch(aUser.getUsername());
                if (!ObjectUtil.isEmpty(existingUser) && existingUser.getId() != aUser.getId()) {
                    errorCodes.put("unique.userName", "unique.userName");
                }
            }else{
                User existingUser = userDAO.findUserByNameAndBranchId(aUser.getUsername(), aUser.getParentBranchId());
                if (!ObjectUtil.isEmpty(existingUser) && existingUser.getId() != aUser.getId()) {
                    errorCodes.put("unique.userName", "unique.userName");
                } 
            }
           /* if (!ValidationUtil.isPatternMaches(aUser.getUsername(),
                    ValidationUtil.ALPHANUMERIC_PATTERN)) {
                errorCodes.put("pattern.userName", "pattern.userName");
            }
        } else {
            errorCodes.put("user.username.empty", "user.username.empty");
        }*/
        }
            if(StringUtil.isEmpty(aUser.getUsername())){
            	 errorCodes.put("user.username.empty", "user.username.empty");
            }

        if (!StringUtil.isEmpty(aUser.getPersonalInfo().getFirstName())) {
            if (!ValidationUtil.isPatternMaches(aUser.getPersonalInfo().getFirstName(),
                    ValidationUtil.ALPHANUMERIC_PATTERN)) {
                errorCodes.put("pattern.firstName", "pattern.firstName");
            }
        } else {
            errorCodes.put("empty.firstName", "empty.firstName");
        }
        if (!StringUtil.isEmpty(aUser.getPersonalInfo().getFirstName())) {
            if (!ValidationUtil.isPatternMaches(aUser.getPersonalInfo().getFirstName(),
                    ValidationUtil.ALPHANUMERIC_PATTERN)) {
                errorCodes.put("pattern.lastName", "pattern.lastName");
            }
        }
        if (!StringUtil.isEmpty(aUser.getContactInfo().getMobileNumber())) {
            if (!ValidationUtil.isPatternMaches(aUser.getContactInfo().getMobileNumber(),
                    ValidationUtil.NUMBER_PATTERN)) {
                errorCodes.put("pattern.mobile", "pattern.mobile");
            }
        }
        if (!StringUtil.isEmpty(aUser.getContactInfo().getEmail())) {
            if (!ValidationUtil.isPatternMaches(aUser.getContactInfo().getEmail(),
                    ValidationUtil.EMAIL_PATTERN)) {
                errorCodes.put("pattern.email", "pattern.email");
            }else{
                User user1 = userDAO.findUserByEmailId(aUser.getContactInfo().getEmail());
                
                if( user1!=null && user1.getId() != aUser.getId() && user1.getBranchId().equals(aUser.getParentBranchId()) ){
                    errorCodes.put("unique.email", "unique.email");
                }
            }
        } else {
            errorCodes.put("empty.email", "empty.email");
        }

        if (StringUtil.isEmpty(aUser.getRole().getId()) || aUser.getRole().getId() == 0) {
            errorCodes.put("empty.role.id", "empty.role");
        }

        if (StringUtil.isEmpty(aUser.getLanguage())) {
            errorCodes.put("empty.language", "empty.language");
        }

        return errorCodes;
    }

}
