/*
 * UserDAO.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.umgmt.entity.Entitlement;
import com.sourcetrace.eses.umgmt.entity.IdentityType;
import com.sourcetrace.eses.umgmt.entity.Role;
import com.sourcetrace.eses.umgmt.entity.User;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;

/**
 * The Class UserDAO.
 * 
 * @author $Author: aravind $
 * @version $Rev: 1268 $, $Date: 2010-11-24 10:52:01 +0530 (Wed, 24 Nov 2010) $
 */
@Repository
@Transactional
public class UserDAO extends ESEDAO implements IUserDAO {

	@Autowired
	public UserDAO(SessionFactory sessionFactory) {
		// TODO Auto-generated constructor stub
		this.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.user.IUserDAO#findByUsername(java.lang.String)
	 */
	public User findByUsername(String username) {

		Session session = getSessionFactory().getCurrentSession();
		org.hibernate.Filter branchFilter = session.getEnabledFilter(ISecurityFilter.BRANCH_FILTER);
		User user = null;
		if (ObjectUtil.isEmpty(branchFilter)) {
			user = (User) find(
					"from User user LEFT JOIN FETCH user.entitlements ent LEFT JOIN FETCH user.role rl LEFT JOIN FETCH rl.entitlements rent where user.username = ? AND user.branchId IS NULL",
					username);
		} else {
			user = (User) find(
					"from User user LEFT JOIN FETCH user.entitlements ent LEFT JOIN FETCH user.role rl LEFT JOIN FETCH rl.entitlements rent where user.username = ?",
					username);
		}
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.user.IUserDAO#isValidESEUser(java.lang.String)
	 */
	public User isValidESEUser(String username) {

		User user = (User) find("from User user where user.username = ? ", username);
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.user.IUserDAO#isValidMDBUser(java.lang.String)
	 */
	public User isValidMDBUser(String username) {

		User user = (User) find("from User user where user.username = ? ", username);
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.user.IUserDAO#findUser(long)
	 */
	public User findUser(long id) {

		User user = (User) find("from User user where user.id = ?", id);
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.user.IUserDAO#findEntitlement(java.lang.String)
	 */
	public Entitlement findEntitlement(String name) {

		Entitlement ent = (Entitlement) find("FROM Entitlement e WHERE e.authority = ?", name);
		return ent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.user.IUserDAO#listUsers()
	 */
	public List<User> listUsers() {

		// excluding MDB users.
		return list("from User user ORDER BY user.username ASC");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.user.IUserDAO#findUserByProfileId(java.lang.String)
	 */
	public User findUserByProfileId(String profileId) {

		// TODO Auto-generated method stub
		User user = (User) find("from User user where user.username = ?", profileId);
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.user.IUserDAO#listUsersByUserName(java.lang.String)
	 */
	public long listUsersByUserName(String username) {

		Long count = (Long) list(
				"select count(user.id) from User user where user.username like '" + username.trim() + "%'").get(0);

		return count;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.user.IUserDAO#listUsersByUserCount(java.lang.String,
	 * long)
	 */
	public long listUsersByUserCount(String username, long counts) {

		long count = 0;
		if (counts != 0) {
			count = (Long) list(
					"select count(user.id) from User user where user.username like '" + username.trim() + counts + "'")
							.get(0);
		} else {
			count = (Long) list(
					"select count(user.id) from User user where user.username like '" + username.trim() + "'").get(0);
		}

		return count;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.user.IUserDAO#findRole(long)
	 */
	public Role findRole(long roleId) {

		return (Role) find("FROM Role r WHERE r.id = ?", roleId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.user.IUserDAO#listRoles()
	 */
	public List<Role> listRoles() {

		return (List<Role>) list("FROM Role");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.user.IUserDAO#listEntitlements(java.lang.String)
	 */
	public List<Entitlement> listEntitlements(String username) {

		List<Entitlement> ents = list("select user.entitlements from User user where user.username = ?", username);
		return ents;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.user.IUserDAO#listUsersByRole(int)
	 */
	public List<User> listUsersByRole(int role) {

		return list("from User user where user.filter.id=?", role);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.user.IUserDAO#loadUserByUsername(java.lang.String)
	 */
	public User loadUserByUsername(String username) {

		User user = (User) find("from User user left join fetch user.entitlements where user.username = ?", username);
		return user;
	}

	/*
	 * (non-Javadoc
	 * 
	 * @see com.ese.dao.user.IUserDAO#listIdentityType()
	 */
	public List<IdentityType> listIdentityType() {

		return list("From IdentityType it ORDER BY it.name ASC");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.user.IUserDAO#isCityMappingexist(long)
	 */
	public boolean isCityMappingexist(long id) {

		List<User> userList = list("FROM User u WHERE u.contactInfo.city.id = ?", id);
		if (!ObjectUtil.isListEmpty(userList)) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.dao.user.IUserDAO#isDashboardUserEntitlementAvailable(java.lang.
	 * String)
	 */
	public boolean isDashboardUserEntitlementAvailable(String userName) {

		// String queryString = "SELECT u.ID FROM ese_user AS u INNER JOIN
		// ese_role_ent AS re ON re.ROLE_ID = u.ESE_ROLE_ID INNER JOIN ese_ent
		// AS e ON e.ID = re.ENT_ID WHERE u.USER_NAME = '"
		// + userName + "' AND e.NAME = 'dashboard.dashboard.list'";

		String hqlString = "SELECT u.id FROM User u INNER JOIN u.role.entitlements ets WHERE u.username = :uname AND ets.authority = :aut ";
		Session sessions = getSessionFactory().getCurrentSession();
		Query query = sessions.createQuery(hqlString);
		query.setParameter("uname", userName);
		query.setParameter("aut", Entitlement.DASHBOARD_ENTITLEMENT);
		List results = query.list();

		return (results.size() > 0) ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.user.IUserDAO#findUserByEmailId(java.lang.String)
	 */
	public User findUserByEmailId(String emailId) {

		User user = (User) find("FROM User us WHERE us.contactInfo.email = ?", emailId);
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.user.IUserDAO#isMailIdExit(long, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public boolean isMailIdExit(long id, String mailId) {

		if (id > 0) {
			List<User> userList = list("FROM User u WHERE u.id !=" + id + " AND u.contactInfo.email = ?", mailId);
			if (!ObjectUtil.isListEmpty(userList)) {
				return true;
			}
			return false;
		} else {
			List<User> userList = list("FROM User u WHERE u.contactInfo.email = ?", mailId);
			if (!ObjectUtil.isListEmpty(userList)) {
				return true;
			}
			return false;
		}
	}

	@Override
	public void updateUsersStatusUpdate(int status, String username) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateUsersStatusUpdate(int status, String username, long milliSecond) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] findWebUserAgentInfoByProfileId(String profileId) {

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User findByUsernameAndBranchId(String username, String branchId) {

		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		org.hibernate.Filter branchFilter = session.getEnabledFilter(ISecurityFilter.BRANCH_FILTER);
		User user = null;
		if (!ObjectUtil.isEmpty(branchFilter)) {
			session.disableFilter(ISecurityFilter.BRANCH_FILTER);
		}
		if (StringUtil.isEmpty(branchId)) {
			user = (User) find(
					"from User user LEFT JOIN FETCH user.entitlements ent LEFT JOIN FETCH user.role rl LEFT JOIN FETCH rl.entitlements rent where user.username = ? AND user.branchId IS NULL",
					username);
		} else {

			user = (User) find(
					"from User user LEFT JOIN FETCH user.entitlements ent LEFT JOIN FETCH user.role rl LEFT JOIN FETCH rl.entitlements rent where user.username = ? AND user.branchId = ?",
					new Object[] { username, branchId });
		}
		return user;
	}

	@Override
	public void updateUserLanguage(String userName, String lang) {

		// TODO Auto-generated method stub
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("UPDATE User SET language= :lang WHERE username=:userName");
		query.setString("lang", lang);
		query.setString("userName", userName);
		query.executeUpdate();
		session.flush();
		session.close();
	}

	@Override
	public byte[] findUserImage(long userId) {

		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("SELECT u.personalInfo.image FROM User u WHERE u.id=:userID");
		query.setLong("userID", userId);
		return (byte[]) query.uniqueResult();
	}

	@Override
	public Integer findUserCount() {

		Session session = getSessionFactory().getCurrentSession();
		return ((Long) session.createQuery("select count(*) from User").uniqueResult()).intValue();
	}

	public Integer findUserCountByMonth(Date sDate, Date eDate) {

		Session session = getSessionFactory().openSession();
		Query query = session
				.createQuery("select count(*) from User where createdDate BETWEEN :startDate AND :endDate");
		query.setParameter("startDate", sDate).setParameter("endDate", eDate);

		Integer val = ((Long) query.uniqueResult()).intValue();
		session.flush();
		session.close();
		return val;
	}

	public User findByUserNameExcludeBranch(String userName) {

		Session session = getSessionFactory().openSession();
		session.disableFilter(ISecurityFilter.BRANCH_FILTER);

		User user = null;
		user = (User) find(
				"from User user LEFT JOIN FETCH user.entitlements ent LEFT JOIN FETCH user.role rl LEFT JOIN FETCH rl.entitlements rent where user.username = ?",
				userName);
		session.flush();
		session.clear();
		session.close();
		return user;
	}

	public User findUserByUserNameExcludeBranch(String userName) {
		Session session = getSessionFactory().openSession();
		session.disableFilter(ISecurityFilter.BRANCH_FILTER);
		Query query = session.createQuery("From User where username=:userName");
		query.setParameter("userName", userName);
		List list = query.list();
		User user = null;
		if (list.size() > 0) {
			user = (User) query.list().get(0);
		}
		session.flush();
		session.close();
		return user;
	}

	@Override
	public User findUserByNameAndBranchId(String username, String branchId) {
		// TODO Auto-generated method stub
		User user = null;
		if (StringUtil.isEmpty(branchId)) {
			user = (User) find("from User user where user.username = ? AND user.branchId IS NULL", username);
		} else {
			user = (User) find("from User user where user.username = ? AND user.branchId = ?",
					new Object[] { username, branchId });
		}
		return user;
	}

	@Override
	public Integer findFarmerCottonCount() {

		long totalFarmers = 0;
		Session sessions = getSessionFactory().openSession();
		String queryString = "SELECT COUNT(DISTINCT fm.ID) FROM farmer fm " + "INNER JOIN farm f on f.FARMER_ID=fm.ID "
				+ "INNER JOIN farm_crops fc on fc.FARM_ID=f.ID "
				+ "INNER JOIN HARVEST_SEASON hs ON fc.CROP_SEASON = hs.id "
				+ "WHERE fc.PROCUREMENT_CROPS_VARIETY_ID IN(SELECT pv.ID from procurement_product pp "
				+ "INNER JOIN procurement_variety pv on pv.PROCUREMENT_PRODUCT_ID=pp.ID "
				+ "where pp.NAME LIKE '%cotton%') AND fm.STATUS_CODE = '" + ESETxnStatus.SUCCESS.ordinal() + "'";
		Query query = sessions.createSQLQuery(queryString);

		int count = Integer.valueOf(query.list().get(0).toString());

		if (count > 0) {
			totalFarmers = count;
		}
		sessions.flush();
		sessions.close();
		return (int) totalFarmers;
	}

	@Override
	public Integer findFarmerCottonCountByMonth(Date sDate, Date eDate) {

		long totalFarmers = 0;
		Session sessions = getSessionFactory().openSession();
		Query query = sessions.createSQLQuery("SELECT COUNT(DISTINCT fm.ID) FROM farmer fm "
				+ "INNER JOIN farm f on f.FARMER_ID=fm.ID " + "INNER JOIN farm_crops fc on fc.FARM_ID=f.ID "
				+ "WHERE fc.PROCUREMENT_CROPS_VARIETY_ID IN(SELECT pv.ID from procurement_product pp "
				+ "INNER JOIN procurement_variety pv on pv.PROCUREMENT_PRODUCT_ID=pp.ID "
				+ "where pp.NAME LIKE '%cotton%') AND fm.STATUS_CODE = '" + ESETxnStatus.SUCCESS.ordinal()
				+ "' AND fm.CREATED_DATE BETWEEN :startDate AND :endDate");

		query.setParameter("startDate", sDate).setParameter("endDate", eDate);

		int count = Integer.valueOf(query.list().get(0).toString());

		if (count > 0) {
			totalFarmers = count;
		}
		sessions.flush();
		sessions.close();
		return (int) totalFarmers;
	}
	
	public boolean isDashboardUserEntitlementAvailable(String userName,String branch){
		String hqlString = "SELECT u.id FROM User u INNER JOIN u.role.entitlements ets WHERE u.username = :uname AND ets.authority = :aut  AND u.branchId = :branch";
		Session sessions = getSessionFactory().getCurrentSession();
		Query query = sessions.createQuery(hqlString);
		query.setParameter("uname", userName);
		query.setParameter("branch", branch);
		query.setParameter("aut", Entitlement.DASHBOARD_ENTITLEMENT);
		List results = query.list();

		return (results.size() > 0) ? true : false;
	}
	
	public User findByUserNameIncludeBranch(String userName,String branchIdParm) {

		Session session = getSessionFactory().openSession();
		session.disableFilter(ISecurityFilter.BRANCH_FILTER);

		User user = null;
		user = (User) find(
				"from User user LEFT JOIN FETCH user.entitlements ent LEFT JOIN FETCH user.role rl LEFT JOIN FETCH rl.entitlements rent where user.username = ? AND user.branchId = ?",
				new Object[] { userName, branchIdParm });
		session.flush();
		session.clear();
		session.close();
		return user;
	
	}
	


}
