/*
 * IUserService.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.umgmt.entity.Entitlement;
import com.sourcetrace.eses.umgmt.entity.IdentityType;
import com.sourcetrace.eses.umgmt.entity.Menu;
import com.sourcetrace.eses.umgmt.entity.Role;
import com.sourcetrace.eses.umgmt.entity.User;

/**
 * The Interface IUserService.
 * 
 * @author $Author: aravind $
 * @version $Rev: 1323 $ $Date: 2010-11-25 14:16:08 +0530 (Thu, 25 Nov 2010) $
 */
public interface IUserService {

	/**
	 * Checks if is valid ese user.
	 * 
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @return the int
	 */
	public int isValidESEUser(String username, String password);

	/**
	 * Checks if is valid mdb user.
	 * 
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @return the int
	 */
	public int isValidMDBUser(String username, String password);

	/**
	 * Gets the user menus.
	 * 
	 * @param entitlements
	 *            the entitlements
	 * @return the user menus
	 */
	public List<Menu> getUserMenus(Map<Long, Integer> entitlements);

	/**
	 * Gets the mDB user menus.
	 * 
	 * @return the mDB user menus
	 */
	public List<Menu> getMDBUserMenus();

	/**
	 * Gets the user ent.
	 * 
	 * @param entitlements
	 *            the entitlements
	 * @return the user ent
	 */
	public List<Menu> getUserEnt(Map<Long, Integer> entitlements);

	/**
	 * Gets the user entitlements.
	 * 
	 * @param entitlements
	 *            the entitlements
	 * @param menus
	 *            the menus
	 * @return the user entitlements
	 */
	public Map<String, Integer> getUserEntitlements(Map<Long, Integer> entitlements, List<Menu> menus);

	/**
	 * List users.
	 * 
	 * @return the list< user>
	 */
	public List<User> listUsers();

	/**
	 * Adds the user.
	 * 
	 * @param user
	 *            the user
	 */
	public void addUser(User user);

	/**
	 * Edits the user.
	 * 
	 * @param user
	 *            the user
	 */
	public void editUser(User user);

	/**
	 * Removes the user.
	 * 
	 * @param user
	 *            the user
	 */
	public void removeUser(User user);

	/**
	 * Find user by user name.
	 * 
	 * @param userId
	 *            the user id
	 * @return the user
	 */
	public User findUserByUserName(String userId);

	/**
	 * Find user.
	 * 
	 * @param id
	 *            the id
	 * @return the user
	 */
	public User findUser(long id);

	/**
	 * List roles.
	 * 
	 * @return the list< role>
	 */
	public List<Role> listRoles();

	/**
	 * Find role.
	 * 
	 * @param roleId
	 *            the role id
	 * @return the role
	 */
	public Role findRole(long roleId);

	/**
	 * Edits the user credential.
	 * 
	 * @param user
	 *            the user
	 */
	public void editUserCredential(User user);

	/**
	 * Gets the eSE date format.
	 * 
	 * @return the eSE date format
	 */
	public String getESEDateFormat();

	/**
	 * Gets the eSE date time format.
	 * 
	 * @return the eSE date time format
	 */
	public String getESEDateTimeFormat();

	/**
	 * List users by user name.
	 * 
	 * @param userName
	 *            the user name
	 * @return the long
	 */
	public long listUsersByUserName(String userName);

	/**
	 * List users by user count.
	 * 
	 * @param userName
	 *            the user name
	 * @param counts
	 *            the counts
	 * @return the long
	 */
	public long listUsersByUserCount(String userName, long counts);

	/**
	 * Find user by profile id.
	 * 
	 * @param id
	 *            the id
	 * @return the user
	 */
	public User findUserByProfileId(String id);

	/**
	 * Email user credential.
	 * 
	 * @param user
	 *            the user
	 */
	public void emailUserCredential(User user);

	/**
	 * Edits the user entitlements.
	 * 
	 * @param id
	 *            the id
	 * @param entitlements
	 *            the entitlements
	 */
	public void editUserEntitlements(long id, List<String> entitlements);

	/**
	 * List entitlements.
	 * 
	 * @param username
	 *            the username
	 * @return the list< entitlement>
	 */
	public List<Entitlement> listEntitlements(String username);

	/**
	 * List users by role.
	 * 
	 * @param role
	 *            the role
	 * @return the list< user>
	 */
	public List<User> listUsersByRole(int role);

	/**
	 * Find preference.
	 * 
	 * @return the eSE system
	 */
	public ESESystem findPreference();

	/**
	 * Find preference validation.
	 * 
	 * @param id
	 *            the id
	 * @return the eSE system
	 */
	public ESESystem findPreferenceValidation(Long id);

	/**
	 * List identity type.
	 * 
	 * @return the list< identity type>
	 */
	public List<IdentityType> listIdentityType();

	/**
	 * Gets the email message.
	 * 
	 * @param user
	 *            the user
	 * @return the email message
	 */
	public String getEmailMessage(User user);

	/**
	 * Checks if is dashboard user entitlement available.
	 * 
	 * @param userName
	 *            the user name
	 * @return true, if is dashboard user entitlement available
	 */
	public boolean isDashboardUserEntitlementAvailable(String userName);

	/**
	 * Find user by email id.
	 * 
	 * @param emailId
	 *            the email id
	 * @return the user
	 */
	public User findUserByEmailId(String emailId);

	/**
	 * Gets the email message for password.
	 * 
	 * @param user
	 *            the user
	 * @return the email message for password
	 */
	public String getEmailMessageForPassword(User user);

	/**
	 * Checks if is mail id exit.
	 * 
	 * @param id
	 *            the id
	 * @param mailId
	 *            the mail id
	 * @return true, if is mail id exit
	 */
	public boolean isMailIdExit(long id, String mailId);

	public void updateUserLanguage(String userName, String lang);

	public byte[] findUserImage(long userId);
	
	public Integer findUserCount();
	
	public Integer findUserCountByMonth(Date sDate,Date eDate);

	public Object[] findMenuInfo(long id);

    public Role findRoleExcludeBranch(long selectedRole);
    public Integer findFarmerCottonCount();
    public Integer findFarmerCottonCountByMonth(Date sDate,Date eDate);
    
    public String getEmailText(User user);
    
    public boolean isDashboardUserEntitlementAvailable(String userName,String branch);
    
    public User findByUserNameIncludeBranch(String userName,String branchIdParm);
    
    public User isValidESEUser(String username);
}
