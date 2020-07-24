/*
 * IUserDAO.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.Date;
import java.util.List;

import com.sourcetrace.eses.umgmt.entity.Entitlement;
import com.sourcetrace.eses.umgmt.entity.IdentityType;
import com.sourcetrace.eses.umgmt.entity.Role;
import com.sourcetrace.eses.umgmt.entity.User;

/**
 * The Interface IUserDAO.
 * 
 * @author $Author: aravind $
 * @version $Rev: 1268 $ $Date: 2010-11-24 10:52:01 +0530 (Wed, 24 Nov 2010) $
 */

public interface IUserDAO extends IESEDAO {

	/**
	 * Find by username.
	 * 
	 * @param username
	 *            the username
	 * @return the user
	 */
	@SuppressWarnings("unchecked")
	public abstract User findByUsername(String username);

	/**
	 * Checks if is valid ese user.
	 * 
	 * @param username
	 *            the username
	 * @return the user
	 */
	@SuppressWarnings("unchecked")
	public abstract User isValidESEUser(String username);

	/**
	 * Checks if is valid mdb user.
	 * 
	 * @param username
	 *            the username
	 * @return the user
	 */
	public abstract User isValidMDBUser(String username);

	/**
	 * Find user.
	 * 
	 * @param id
	 *            the id
	 * @return the user
	 */
	@SuppressWarnings("unchecked")
	public abstract User findUser(long id);

	/**
	 * List users.
	 * 
	 * @return the list< user>
	 */
	@SuppressWarnings("unchecked")
	public abstract List<User> listUsers();

	/**
	 * Find role.
	 * 
	 * @param roleId
	 *            the role id
	 * @return the role
	 */
	public abstract Role findRole(long roleId);

	/**
	 * List roles.
	 * 
	 * @return the list< role>
	 */
	public abstract List<Role> listRoles();

	/**
	 * Find user by profile id.
	 * 
	 * @param id
	 *            the id
	 * @return the user
	 */
	public User findUserByProfileId(String id);

	/**
	 * List users by user name.
	 * 
	 * @param username
	 *            the username
	 * @return the long
	 */
	public long listUsersByUserName(String username);

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
	 * Find entitlement.
	 * 
	 * @param name
	 *            the name
	 * @return the entitlement
	 */
	public Entitlement findEntitlement(String name);

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
	 * Load user by username.
	 * 
	 * @param username
	 *            the username
	 * @return the user
	 */
	public User loadUserByUsername(String username);

	/**
	 * List identity type.
	 * 
	 * @return the list< identity type>
	 */
	public List<IdentityType> listIdentityType();

	/**
	 * Checks if is city mappingexist.
	 * 
	 * @param id
	 *            the id
	 * @return true, if is city mappingexist
	 */
	public boolean isCityMappingexist(long id);

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
	 * Checks if is mail id exit.
	 * 
	 * @param id
	 *            the id
	 * @param mailId
	 *            the mail id
	 * @return true, if is mail id exit
	 */
	public boolean isMailIdExit(long id, String mailId);

	public abstract void updateUsersStatusUpdate(int status, String username);

	public abstract void updateUsersStatusUpdate(int status, String username, long milliSecond);

	public abstract Object[] findWebUserAgentInfoByProfileId(String profileId);

	public abstract User findByUsernameAndBranchId(String username, String branchId);

	public void updateUserLanguage(String userName, String lang);

	public byte[] findUserImage(long userId);

	public Integer findUserCount();

	public Integer findUserCountByMonth(Date sDate, Date eDate);

	public User findByUserNameExcludeBranch(String userName);

	public User findUserByUserNameExcludeBranch(String userName);

	public User findUserByNameAndBranchId(String username, String branchId);

	public Integer findFarmerCottonCount();

	public Integer findFarmerCottonCountByMonth(Date sDate, Date eDate);
	
	public boolean isDashboardUserEntitlementAvailable(String userName,String branch);
	
	public User findByUserNameIncludeBranch(String userName,String branchIdParm);
	
	
}
