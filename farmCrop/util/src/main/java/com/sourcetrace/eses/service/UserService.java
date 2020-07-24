/*
 * UserService.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.dao.IAffiliateDAO;
import com.sourcetrace.eses.dao.IESESystemDAO;
import com.sourcetrace.eses.dao.IFederationDAO;
import com.sourcetrace.eses.dao.IMenuDAO;
import com.sourcetrace.eses.dao.IProviderDAO;
import com.sourcetrace.eses.dao.IStoreDAO;
import com.sourcetrace.eses.dao.IUserDAO;
import com.sourcetrace.eses.dao.ServicePointDAO;
import com.sourcetrace.eses.dao.UserDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.txn.exception.ESEException;
import com.sourcetrace.eses.umgmt.entity.Entitlement;
import com.sourcetrace.eses.umgmt.entity.IdentityType;
import com.sourcetrace.eses.umgmt.entity.Menu;
import com.sourcetrace.eses.umgmt.entity.Role;
import com.sourcetrace.eses.umgmt.entity.User;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.crypto.CryptoUtil;
import com.sourcetrace.eses.util.crypto.ICryptoUtil;

/**
 * The Class UserService.
 */
@Service
@Transactional
public class UserService implements IUserService {

	private static final Logger LOGGER = Logger.getLogger(UserService.class);
	@Autowired
	private IUserDAO userDAO;
	@Autowired
	private ICryptoUtil cryptoUtil;
	@Autowired
	private IMenuDAO menuDAO;
	@Autowired
	private IESESystemDAO systemDAO;

	private MailSender mailSender;
	private SimpleMailMessage messageTemplate;

	private VelocityEngine velocityEngine;
	private String webconsoleURL;
	@Autowired
	private IProviderDAO providerDAO;
	@Autowired
	private IFederationDAO federationDAO;
	@Autowired
	private IAffiliateDAO affiliateDAO;
	@Autowired
	private IStoreDAO storeDAO;
	@Autowired
	private IServicePointDAO servicePointDAO;

	/**
	 * Sets the user dao.
	 * 
	 * @param userDAO
	 *            the new user dao
	 */
	public void setUserDAO(IUserDAO userDAO) {

		this.userDAO = userDAO;
	}

	/**
	 * Sets the provider dao.
	 * 
	 * @param providerDAO
	 *            the new provider dao
	 */
	/*
	 * public void setProviderDAO(ProviderDAO providerDAO) {
	 * 
	 * this.providerDAO = providerDAO; }
	 */
	/**
	 * Sets the federation dao.
	 * 
	 * @param federationDAO
	 *            the new federation dao
	 */
	/*
	 * public void setFederationDAO(FederationDAO federationDAO) {
	 * 
	 * this.federationDAO = federationDAO; }
	 */

	/**
	 * Sets the affiliate dao.
	 * 
	 * @param affiliateDAO
	 *            the new affiliate dao
	 */
	/*
	 * public void setAffiliateDAO(AffiliateDAO affiliateDAO) {
	 * 
	 * this.affiliateDAO = affiliateDAO; }
	 */

	/**
	 * Sets the store dao.
	 * 
	 * @param storeDAO
	 *            the new store dao
	 */
	/*
	 * public void setStoreDAO(StoreDAO storeDAO) {
	 * 
	 * this.storeDAO = storeDAO; }
	 */

	/**
	 * Sets the service point dao.
	 * 
	 * @param servicePointDAO
	 *            the new service point dao
	 */
	public void setServicePointDAO(ServicePointDAO servicePointDAO) {

		this.servicePointDAO = servicePointDAO;
	}

	/**
	 * Sets the webconsole url.
	 * 
	 * @param webconsoleURL
	 *            the new webconsole url
	 */
	public void setWebconsoleURL(String webconsoleURL) {

		this.webconsoleURL = webconsoleURL;
	}

	/**
	 * Sets the velocity engine.
	 * 
	 * @param velocityEngine
	 *            the new velocity engine
	 */
	public void setVelocityEngine(VelocityEngine velocityEngine) {

		this.velocityEngine = velocityEngine;
	}

	/**
	 * Sets the mail sender.
	 * 
	 * @param mailSender
	 *            the new mail sender
	 */
	public void setMailSender(MailSender mailSender) {

		this.mailSender = mailSender;
	}

	/**
	 * Sets the message template.
	 * 
	 * @param messageTemplate
	 *            the new message template
	 */
	public void setMessageTemplate(SimpleMailMessage messageTemplate) {

		this.messageTemplate = messageTemplate;
	}

	/**
	 * Sets the crypto util.
	 * 
	 * @param cryptoUtil
	 *            the new crypto util
	 */
	public void setCryptoUtil(ICryptoUtil cryptoUtil) {

		this.cryptoUtil = cryptoUtil;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IUserService#isValidESEUser(java.lang.String,
	 * java.lang.String)
	 */
	public int isValidESEUser(String username, String password) {

		ESESystem eseSystem = systemDAO.findESESystem();
		int count = 0;
		long autoReleaseTime = 0;
		try {
			count = Integer.parseInt(eseSystem.getPreferences().get(ESESystem.INVALID_ATTEMPTS_COUNT));
			autoReleaseTime = Long.parseLong(eseSystem.getPreferences().get(ESESystem.TIME_TO_AUTO_RELEASE));
		} catch (NumberFormatException e1) {

		}
		User user = userDAO.isValidESEUser(username);
		int valid = User.INVALID_LOGIN;
		if (user == null) {
			return User.NO_USER_PRESENT;
		}
		if (user != null && !user.isEnabled()) {
			return User.USER_NOT_ENABLED;
		}
		try {
			password = CryptoUtil.getEncryptedString(username + password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (user != null && user.getPassword().equals(password)) {

			long msec = new Date().getTime() - user.getMilliSecond();
			if (user.getStatus() == count) {
				if (msec > autoReleaseTime) {
					valid = User.SUCCESS_LOGIN;
				} else {
					valid = User.ACCOUNT_BLOCKED;
				}
			} else {
				valid = User.SUCCESS_LOGIN;
			}
		} else {
			if (user.getStatus() < count) {
				user.setStatus(user.getStatus() + 1);
				valid = User.INVALID_LOGIN;
			} else if (count == user.getStatus()) {
				valid = User.ACCOUNT_BLOCKED;
			}

		}
		if (valid == User.SUCCESS_LOGIN) {
			user.setStatus(0);
		}
		if (user != null) {
			// user.setUpdateTime(new Date());
			user.setMilliSecond(new Date().getTime());
			userDAO.update(user);
		}

		return valid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IUserService#isValidMDBUser(java.lang.String,
	 * java.lang.String)
	 */
	public int isValidMDBUser(String username, String password) {

		ESESystem eseSystem = systemDAO.findESESystem();
		int count = 0;
		long autoReleaseTime = 0;
		try {
			count = Integer.parseInt(eseSystem.getPreferences().get(ESESystem.MDB_INVALID_ATTEMPTS_COUNT));
			autoReleaseTime = Long.parseLong(eseSystem.getPreferences().get(ESESystem.MDB_TIME_TO_AUTO_RELEASE));
		} catch (NumberFormatException e1) {

		}
		User user = userDAO.isValidMDBUser(username);

		int valid = User.INVALID_LOGIN;
		if (user == null) {
			return User.NO_USER_PRESENT;
		}
		Agent agent = null;
		/*
		 * if (agent != null && !agent.isEnabled()) { return
		 * User.USER_NOT_ENABLED; } if (agent != null &&
		 * !agent.isAgentHaveMDBAccount()) { return User.NOT_MDB_USER; }
		 */try {
			password = CryptoUtil.getEncryptedString(username + password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (user != null && user.getPassword().equals(password)) {

			long msec = new Date().getTime() - user.getMilliSecond();
			if (user.getStatus() == count) {
				if (msec > autoReleaseTime) {
					valid = User.SUCCESS_LOGIN;
				} else {
					valid = User.ACCOUNT_BLOCKED;
				}
			} else {
				valid = User.SUCCESS_LOGIN;
			}
		} else {
			if (user.getStatus() < count) {
				user.setStatus(user.getStatus() + 1);
				valid = User.INVALID_LOGIN;
			} else if (count == user.getStatus()) {
				valid = User.ACCOUNT_BLOCKED;
			}

		}
		if (valid == User.SUCCESS_LOGIN) {
			user.setStatus(0);
		}
		if (user != null) {
			// user.setUpdateTime(new Date());
			user.setMilliSecond(new Date().getTime());
			userDAO.update(user);
		}

		return valid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IUserService#getUserMenus(java.util.Map)
	 */
	public List<Menu> getUserMenus(Map<Long, Integer> entitlements) {

		List<Menu> menus = menuDAO.listMenus();
		List<Menu> userMenus = new ArrayList<Menu>();
		for (Menu menu : menus) {
			if (entitlements.containsKey(menu.getId())) {
				if (entitlements.get(menu.getId()) != 10) {
					Menu userMenu = new Menu();
					userMenu.setId(menu.getId());
					userMenu.setLabel(menu.getLabel());
					userMenu.setUrl(menu.getUrl());
					userMenu.setParentId(menu.getParentId());

					Set<Menu> subMenus = menu.getSubMenus();
					userMenu.setSubMenus(getSubMenus(subMenus, entitlements));
					userMenus.add(userMenu);
				}

			}
		}

		return userMenus;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IUserService#getUserEnt(java.util.Map)
	 */
	public List<Menu> getUserEnt(Map<Long, Integer> entitlements) {

		List<Menu> menus = menuDAO.listMenus();
		List<Menu> userMenus = new ArrayList<Menu>();
		for (Menu menu : menus) {
			if (entitlements.containsKey(menu.getId())) {

				Menu userMenu = new Menu();
				userMenu.setId(menu.getId());
				userMenu.setLabel(menu.getLabel());
				userMenu.setUrl(menu.getUrl());
				userMenu.setParentId(menu.getParentId());

				Set<Menu> subMenus = menu.getSubMenus();
				userMenu.setSubMenus(getSubEnt(subMenus, entitlements));
				userMenus.add(userMenu);

			}
		}

		return userMenus;
	}

	/**
	 * Gets the sub menus.
	 * 
	 * @param subMenus
	 *            the sub menus
	 * @param entitlements
	 *            the entitlements
	 * @return the sub menus
	 */
	private Set<Menu> getSubMenus(Set<Menu> subMenus, Map<Long, Integer> entitlements) {

		Set<Menu> userSubMenus = null;
		if (subMenus != null) {
			userSubMenus = new LinkedHashSet<Menu>();
			for (Menu subMenu : subMenus) {
				if (entitlements.containsKey(subMenu.getId())) {
					if (entitlements.get(subMenu.getId()) != 10) {

						Menu userSubMenu = new Menu();
						userSubMenu.setId(subMenu.getId());
						userSubMenu.setLabel(subMenu.getLabel());
						userSubMenu.setOrder(subMenu.getOrder());
						userSubMenu.setParentId(subMenu.getParentId());
						userSubMenu.setUrl(subMenu.getUrl());
						userSubMenu.setSubMenus(getSubMenus(subMenu.getSubMenus(), entitlements));
						userSubMenus.add(userSubMenu);
					}
				}
			}
		}

		return userSubMenus;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IUserService#getMDBUserMenus()
	 */
	public List<Menu> getMDBUserMenus() {

		List<Menu> menus = menuDAO.listMenus();
		List<Menu> userMenus = new ArrayList<Menu>();
		for (Menu menu : menus) {

			Menu userMenu = new Menu();
			userMenu.setId(menu.getId());
			userMenu.setLabel(menu.getLabel());
			userMenu.setUrl(menu.getUrl());
			userMenu.setParentId(menu.getParentId());

			Set<Menu> subMenus = menu.getSubMenus();
			userMenu.setSubMenus(getMDBSubMenus(subMenus));
			userMenus.add(userMenu);

		}

		return userMenus;
	}

	/**
	 * Gets the mDB sub menus.
	 * 
	 * @param subMenus
	 *            the sub menus
	 * @return the mDB sub menus
	 */
	private Set<Menu> getMDBSubMenus(Set<Menu> subMenus) {

		Set<Menu> userSubMenus = null;
		if (subMenus != null) {
			userSubMenus = new LinkedHashSet<Menu>();
			for (Menu subMenu : subMenus) {

				Menu userSubMenu = new Menu();
				userSubMenu.setId(subMenu.getId());
				userSubMenu.setLabel(subMenu.getLabel());
				userSubMenu.setOrder(subMenu.getOrder());
				userSubMenu.setParentId(subMenu.getParentId());
				userSubMenu.setUrl(subMenu.getUrl());
				userSubMenu.setSubMenus(getMDBSubMenus(subMenu.getSubMenus()));
				userSubMenus.add(userSubMenu);

			}
		}

		return userSubMenus;
	}

	/**
	 * Gets the sub ent.
	 * 
	 * @param subMenus
	 *            the sub menus
	 * @param entitlements
	 *            the entitlements
	 * @return the sub ent
	 */
	private Set<Menu> getSubEnt(Set<Menu> subMenus, Map<Long, Integer> entitlements) {

		Set<Menu> userSubMenus = null;
		if (subMenus != null) {
			userSubMenus = new LinkedHashSet<Menu>();
			for (Menu subMenu : subMenus) {
				if (entitlements.containsKey(subMenu.getId())) {

					Menu userSubMenu = new Menu();
					userSubMenu.setId(subMenu.getId());
					userSubMenu.setLabel(subMenu.getLabel());
					userSubMenu.setOrder(subMenu.getOrder());
					userSubMenu.setParentId(subMenu.getParentId());
					userSubMenu.setUrl(subMenu.getUrl());
					userSubMenu.setSubMenus(getSubEnt(subMenu.getSubMenus(), entitlements));
					userSubMenus.add(userSubMenu);

				}
			}
		}

		return userSubMenus;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IUserService#getUserEntitlements(java.util.Map,
	 * java.util.List)
	 */
	public Map<String, Integer> getUserEntitlements(Map<Long, Integer> entitlements, List<Menu> menus) {

		Map<String, Integer> userEntitlements = new LinkedHashMap<String, Integer>();

		for (Menu menu : menus) {
			checkAndAddEntitlement(menu, entitlements, userEntitlements);
			Set<Menu> subMenus = menu.getSubMenus();
			if (subMenus != null) {
				for (Menu subMenu : subMenus) {
					checkAndAddEntitlement(subMenu, entitlements, userEntitlements);
					Set<Menu> subSubMenus = subMenu.getSubMenus();
					if (subSubMenus != null) {
						for (Menu subSubMenu : subSubMenus) {
							checkAndAddEntitlement(subSubMenu, entitlements, userEntitlements);
						}
					}
				}
			}
		}

		return userEntitlements;
	}

	/**
	 * Check and add entitlement.
	 * 
	 * @param menu
	 *            the menu
	 * @param entitlements
	 *            the entitlements
	 * @param userEntitlements
	 *            the user entitlements
	 */
	private void checkAndAddEntitlement(Menu menu, Map<Long, Integer> entitlements,
			Map<String, Integer> userEntitlements) {

		String url = menu.getUrl();
		int index = url.indexOf('_');
		index = (index == -1) ? url.indexOf('.') : index;
		if (index != -1) {
			String prefix = url.substring(0, index);
			userEntitlements.put(prefix, entitlements.get(menu.getId()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.user.IUserService#findUserByUserName(java.lang.String)
	 */
	public User findUserByUserName(String username) {

		return userDAO.findByUsername(username);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IUserService#listUsers()
	 */
	public List<User> listUsers() {

		return userDAO.listUsers();
	}

	/**
	 * Sets the user dao.
	 * 
	 * @param userDAO
	 *            the new user dao
	 */
	public void setUserDAO(UserDAO userDAO) {

		this.userDAO = userDAO;
	}

	/**
	 * Sets the menu dao.
	 * 
	 * @param menuDAO
	 *            the new menu dao
	 */
	public void setMenuDAO(IMenuDAO menuDAO) {

		this.menuDAO = menuDAO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IUserService#addUser(com.ese.entity.user.User)
	 */
	public void addUser(User user) {

		userDAO.save(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IUserService#editUser(com.ese.entity.user.User)
	 */
	public void editUser(User user) {

		userDAO.update(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.user.IUserService#editUserCredential(com.ese.entity.user.
	 * User)
	 */
	public void editUserCredential(User user) {

		User aUser = userDAO.findByUsernameAndBranchId(user.getUsername(), user.getBranchId());

		if (!StringUtil.isEmpty(user.getPassword())) {
			try {
				String clearText = user.getUsername() + user.getPassword();
				String password = cryptoUtil.encrypt(StringUtil.getMulipleOfEight(clearText));
				aUser.setPassword(password);
			} catch (Exception e) {
				LOGGER.error("Error Encrypting password", e);
			}
		}

		aUser.setRole(user.getRole());
		// aUser.setFilter(user.getFilter());
		// aUser.setDataId(user.getDataId());
		aUser.setEnabled(user.isEnabled());
		userDAO.update(aUser);

		if (!StringUtil.isEmpty(user.getPassword())) {
			// emailUserCredential(aUser);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.user.IUserService#removeUser(com.ese.entity.user.User)
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void removeUser(User user) {

		if (user == null) {
			throw new ESEException("Invalid User Name to Delete");
		}

		userDAO.delete(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IUserService#findRole(long)
	 */
	public Role findRole(long roleId) {

		return userDAO.findRole(roleId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IUserService#listRoles()
	 */
	public List<Role> listRoles() {

		return userDAO.listRoles();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IUserService#findUser(long)
	 */
	public User findUser(long id) {

		return userDAO.findUser(id);
	}

	/**
	 * Sets the system dao.
	 * 
	 * @param systemDAO
	 *            the new system dao
	 */
	public void setSystemDAO(IESESystemDAO systemDAO) {

		this.systemDAO = systemDAO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IUserService#getESEDateFormat()
	 */
	/*
	 * public String getESEDateFormat() {
	 * 
	 * ESESystem eseSystem = systemDAO.findESESystem(); return
	 * eseSystem.getPreferences().get(ESESystem.DATE_FORMAT); }
	 * 
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IUserService#getESEDateTimeFormat()
	 * 
	 * public String getESEDateTimeFormat() {
	 * 
	 * ESESystem eseSystem = systemDAO.findESESystem(); return
	 * eseSystem.getPreferences().get(ESESystem.DATE_TIME_FORMAT);
	 * 
	 * }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.user.IUserService#listUsersByUserName(java.lang.String)
	 */
	public long listUsersByUserName(String userName) {

		return userDAO.listUsersByUserName(userName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.user.IUserService#listUsersByUserCount(java.lang.String,
	 * long)
	 */
	public long listUsersByUserCount(String userName, long counts) {

		return userDAO.listUsersByUserCount(userName, counts);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.user.IUserService#findUserByProfileId(java.lang.String)
	 */
	public User findUserByProfileId(String id) {

		return userDAO.findUserByProfileId(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.user.IUserService#emailUserCredential(com.ese.entity.user
	 * .User)
	 */
	public void emailUserCredential(User user) {

		SimpleMailMessage msg = new SimpleMailMessage(messageTemplate);
		msg.setTo(user.getContactInfo().getEmail());

		msg.setText(getEmailText(user));

		try {
			mailSender.send(msg);
		} catch (MailException ex) {
			LOGGER.error("Error sending email", ex);
		}
	}

	/**
	 * Gets the email text.
	 * 
	 * @param user
	 *            the user
	 * @return the email text
	 */
	public String getEmailText(User user) {

		Map<String, String> model = new HashMap<String, String>();
		model.put("userName", user.getUsername());
		String clearText = cryptoUtil.decrypt(user.getPassword());
		String password = clearText.substring(user.getUsername().length());
		model.put("password", password);
		model.put("webConsoleURL", webconsoleURL);
		model.put("firstName", user.getPersonalInfo().getFirstName());
		model.put("middleName", user.getPersonalInfo().getMiddleName());
		model.put("lastName", user.getPersonalInfo().getLastName());
		model.put("secondLastName", user.getPersonalInfo().getSecondLastName());

		String emailTemplate = null;
		/*
		 * if (user.getFilter().getId() == User.PROVIDER) { Provider provider =
		 * providerDAO.find(user.getDataId()); model.put("provider",
		 * provider.getName()); emailTemplate = "provider.vm"; } else if
		 * (user.getFilter().getId() == User.FEDERATION) { Federation federation
		 * = federationDAO.find(user.getDataId()); model.put("federation",
		 * federation.getName()); emailTemplate = "federation.vm"; } else if
		 * (user.getFilter().getId() == User.AFFILIATE) { Affiliate affiliate =
		 * affiliateDAO.findAffiliate(user.getDataId()); model.put("affiliate",
		 * affiliate.getName()); emailTemplate = "affiliate.vm"; } else if
		 * (user.getFilter().getId() == User.STORE) { Store store =
		 * storeDAO.find(user.getDataId()); model.put("store", store.getName());
		 * emailTemplate = "store.vm"; } else if (user.getFilter().getId() ==
		 * User.SERVICE_POINT) { ServicePoint servicePoint =
		 * servicePointDAO.find(user.getDataId()); model.put("servicePoint",
		 * servicePoint.getName()); emailTemplate = "servicePoint.vm"; } else if
		 * (user.getFilter().getId() == User.SWITCH) { User userName =
		 * userDAO.findUser(user.getId()); model.put("administrator",
		 * userName.getUsername()); emailTemplate = "administrator.vm"; }
		 */
		String text = null;
		try {
			// create email text using the template
			text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, emailTemplate, "UTF-8", model);
		} catch (VelocityException e) {
			LOGGER.error("Error in building email text", e);
		}

		return text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IUserService#editUserEntitlements(long,
	 * java.util.List)
	 */
	public void editUserEntitlements(long id, List<String> entitlements) {

		User user = userDAO.findUser(id);
		LOGGER.debug("Selected entitlements " + entitlements);

		Set<Entitlement> selectedEnts = new LinkedHashSet<Entitlement>();
		for (String entitlement : entitlements) {
			LOGGER.debug("finding entitlement " + entitlement);
			Entitlement newEntitlement = userDAO.findEntitlement(entitlement);
			LOGGER.debug("adding entitlement " + newEntitlement.getId());
			selectedEnts.add(newEntitlement);
		}

		user.setEntitlements(selectedEnts);
		userDAO.update(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IUserService#listEntitlements(java.lang.String)
	 */
	public List<Entitlement> listEntitlements(String username) {

		return userDAO.listEntitlements(username);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IUserService#listUsersByRole(int)
	 */
	public List<User> listUsersByRole(int role) {

		return userDAO.listUsersByRole(role);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IUserService#findPreference()
	 */
	public ESESystem findPreference() {

		ESESystem system = systemDAO.findPrefernceById(ESESystem.SYSTEM_SWITCH);
		return system;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.user.IUserService#findPreferenceValidation(java.lang.
	 * Long)
	 */
	public ESESystem findPreferenceValidation(Long id) {

		ESESystem system = systemDAO.findPrefernceById(String.valueOf(id));
		return system;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IUserService#listIdentityType()
	 */
	public List<IdentityType> listIdentityType() {

		return userDAO.listIdentityType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.user.IUserService#getEmailMessage(com.ese.entity.user
	 * .User)
	 */
	public String getEmailMessage(User user) {

		Map<String, String> model = new HashMap<String, String>();
		model.put("userName", user.getUsername());
		if (user.getPersonalInfo() != null) {
			model.put("firstName",
					user.getPersonalInfo().getFirstName() != null ? user.getPersonalInfo().getFirstName() : "");
			model.put("middleName",
					user.getPersonalInfo().getMiddleName() != null ? user.getPersonalInfo().getMiddleName() : "");
			model.put("lastName",
					user.getPersonalInfo().getLastName() != null ? user.getPersonalInfo().getLastName() : "");
			model.put("secondLastName", user.getPersonalInfo().getSecondLastName() != null
					? user.getPersonalInfo().getSecondLastName() : "");
		}

		String emailTemplate = null;

		model.put("user", user.getUsername() != null ? user.getUsername() : "");
		emailTemplate = "user.vm";

		String text = null;
		try {
			// create email text using the template
			text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, emailTemplate, "UTF-8", model);
		} catch (VelocityException e) {
			LOGGER.error("Error in building email text", e);
		}

		return text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.user.IUserService#getEmailMessageForPassword(com.ese.
	 * entity.user.User)
	 */
	public String getEmailMessageForPassword(User user) {

		Map<String, String> model = new HashMap<String, String>();
		model.put("userName", user.getUsername());
		model.put("password", StringUtil.generatePassword());
		if (!ObjectUtil.isEmpty(user.getPersonalInfo()))
			model.put("firstName", user.getPersonalInfo().getFirstName());

		// Updating New Password For User
		String cryptText = cryptoUtil.encrypt(StringUtil.getMulipleOfEight(user.getUsername() + model.get("password")));
		user.setPassword(cryptText);
		user.setReset(true);
		userDAO.update(user);

		String emailTemplate = null;

		model.put("user", user.getUsername() != null ? user.getUsername() : "");
		emailTemplate = "email.vm";

		String text = null;
		try {
			// create email text using the template
			text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, emailTemplate, "UTF-8", model);
		} catch (VelocityException e) {
			LOGGER.error("Error in building email text", e);
		}

		return text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.user.IUserService#isDashboardUserEntitlementAvailable(
	 * java.lang.String)
	 */
	public boolean isDashboardUserEntitlementAvailable(String userName) {

		return userDAO.isDashboardUserEntitlementAvailable(userName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.user.IUserService#findUserByEmailId(java.lang.String)
	 */
	public User findUserByEmailId(String emailId) {

		return userDAO.findUserByEmailId(emailId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IUserService#isMailIdExit(long,
	 * java.lang.String)
	 */
	public boolean isMailIdExit(long id, String mailId) {

		return userDAO.isMailIdExit(id, mailId);

	}

	@Override
	public String getESEDateFormat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getESEDateTimeFormat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateUserLanguage(String userName, String lang) {
		// TODO Auto-generated method stub
		userDAO.updateUserLanguage(userName, lang);
	}

	@Override
	public byte[] findUserImage(long userId) {
		// TODO Auto-generated method stub
		return userDAO.findUserImage(userId);
	}

	@Override
	public Integer findUserCount() {
		return userDAO.findUserCount();
	}

	@Override
	public Integer findUserCountByMonth(Date sDate, Date eDate) {
		
		return userDAO.findUserCountByMonth(sDate,eDate);
	}

	@Override
	public Object[] findMenuInfo(long id) {
		// TODO Auto-generated method stub
		return menuDAO.findMenuInfo(id);
	}

    @Override
    public Role findRoleExcludeBranch(long selectedRole) {

        return menuDAO.findRoleExcludeBranch(selectedRole);
    }
    
    @Override
    public Integer findFarmerCottonCount() {

       
        return userDAO.findFarmerCottonCount();
    }

    @Override
    public Integer findFarmerCottonCountByMonth(Date sDate, Date eDate) {

        return userDAO.findFarmerCottonCountByMonth(sDate,eDate);
    }
    
    public boolean isDashboardUserEntitlementAvailable(String userName,String branch){
    	return userDAO.isDashboardUserEntitlementAvailable(userName,branch);
    }
    public User findByUserNameIncludeBranch(String userName,String branchIdParm){
    	return userDAO.findByUserNameIncludeBranch(userName,branchIdParm);
    }
    
	@Override
	public User isValidESEUser(String username) {
		// TODO Auto-generated method stub
		return userDAO.isValidESEUser(username);
	}
}
