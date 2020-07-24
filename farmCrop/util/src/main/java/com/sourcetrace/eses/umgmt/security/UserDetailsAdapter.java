/*
 * UserDetailsAdapter.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.umgmt.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.dao.IClientDAO;
import com.sourcetrace.eses.dao.IESESystemDAO;
import com.sourcetrace.eses.dao.IMenuDAO;
import com.sourcetrace.eses.dao.IRoleDAO;
import com.sourcetrace.eses.dao.IUserDAO;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.txn.exception.WebConsoleError;
import com.sourcetrace.eses.umgmt.entity.Entitlement;
import com.sourcetrace.eses.umgmt.entity.Menu;
import com.sourcetrace.eses.umgmt.entity.Role;
import com.sourcetrace.eses.umgmt.entity.User;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.entity.PreferenceType;

public class UserDetailsAdapter implements IUserDetailsAdapter {

    private static final Logger LOGGER = Logger.getLogger(UserDetailsAdapter.class);
    @Autowired
    private IUserDAO userDAO;
    private IMenuDAO menuDAO;
    private IRoleDAO roleDAO;
    @Autowired
    private IESESystemDAO systemDAO;
    @Autowired
    IClientDAO clientDAO;
    
    private String branches; 

    /**
     * Sets the role dao.
     * @param roleDAO the new role dao
     */
    public void setRoleDAO(IRoleDAO roleDAO) {

        this.roleDAO = roleDAO;
    }

    /**
     * Sets the user dao.
     * @param userDAO the new user dao
     */
    public void setUserDAO(IUserDAO userDAO) {

        this.userDAO = userDAO;
    }

    /**
     * Sets the menu dao.
     * @param menuDAO the new menu dao
     */
    public void setMenuDAO(IMenuDAO menuDAO) {

        this.menuDAO = menuDAO;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.security.userdetails.UserDetailsService#
     * loadUserByUsername(java.lang.String)
     */
    public UserDetails loadUserByUsername(String username) {
        int isMultibranchApp = 0;
        HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
        String branchId = null;
        String branchIdParm = null;

        Map<String, String> preferences = systemDAO.findPrefernceById(ESESystem.SYSTEM_ESE)
                .getPreferences();
        String branchVal = preferences.get(ESESystem.ENABLE_BRANCH);
        int branchEnabled = Integer.parseInt(branchVal);
        String multiBranchVal = systemDAO.findPrefernceByName(ESESystem.IS_MULTI_BRANCH_APP);
        isMultibranchApp = StringUtil.isInteger(multiBranchVal)
                ? Integer.parseInt(multiBranchVal) : 0;
        if (!ObjectUtil.isEmpty(request)) {

           
           

            request.getSession().setAttribute(ISecurityFilter.IS_MULTI_BRANCH_APP,isMultibranchApp);
                    
            if (isMultibranchApp == 0) {
                if (branchEnabled == 1) {
                    branchIdParm = request.getParameter(ISecurityFilter.BRANCH_ID);
                    request.getSession().setAttribute(ESESystem.MAIN_BRANCH_NAME,
                            preferences.get(ESESystem.MAIN_BRANCH_NAME));
                } else {
                    branchIdParm = preferences.get(ESESystem.TENANT_ID);
                }
            }
            branchId = StringUtil.isEmpty(branchIdParm) ? null : branchIdParm;
            BranchMaster branchMaster = StringUtil.isEmpty(branchId) ? null
                    : clientDAO.findBranchMasterByBranchId(branchId);
            
            
            if (!ObjectUtil.isEmpty(branchMaster)) {
                request.getSession().setAttribute(ISecurityFilter.CURRENT_BRANCH_NAME,
                        branchEnabled == 1 ? branchMaster.getName() : "");
                request.getSession().setAttribute(ISecurityFilter.CURRENT_BRANCH, branchId);
            } else {
                request.getSession().setAttribute(ISecurityFilter.CURRENT_BRANCH_NAME, null);
                request.getSession().setAttribute(ISecurityFilter.CURRENT_BRANCH, null);
            }
        }
        User user = null;
        if(isMultibranchApp==1){
            if (isMultibranchApp == 1) {
                if (branchEnabled == 1) {
                    branchIdParm = request.getParameter(ISecurityFilter.BRANCH_ID);
                    request.getSession().setAttribute(ESESystem.MAIN_BRANCH_NAME,
                            preferences.get(ESESystem.MAIN_BRANCH_NAME));
                } else {
                    branchIdParm = preferences.get(ESESystem.TENANT_ID);
                }
            }
            if(StringUtil.isEmpty(branchIdParm)){
            	user = userDAO.findByUserNameExcludeBranch(username);
            }else{
            user = userDAO.findByUserNameIncludeBranch(username,branchIdParm);
            }
            if(!ObjectUtil.isEmpty(user)){
    /*        	  branchId = StringUtil.isEmpty(branchIdParm) ? null : branchIdParm;
                  BranchMaster branchMaster = StringUtil.isEmpty(branchId) ? null
                          : clientDAO.findBranchMasterByBranchId(branchId);
                request.getSession().setAttribute(ISecurityFilter.CURRENT_BRANCH_NAME, branchMaster==null ? null :branchMaster.getName());
                request.getSession().setAttribute(ISecurityFilter.CURRENT_BRANCH, branchIdParm);*/
                
            	 branchId = StringUtil.isEmpty(branchIdParm) ? null : branchIdParm;
                 BranchMaster branchMaster = StringUtil.isEmpty(branchId) ? null
                         : clientDAO.findBranchMasterByBranchId(branchId);
                 
                 
                 if (!ObjectUtil.isEmpty(branchMaster)) {
                     request.getSession().setAttribute(ISecurityFilter.CURRENT_BRANCH_NAME,
                             branchEnabled == 1 ? branchMaster.getName() : "");
                     request.getSession().setAttribute(ISecurityFilter.CURRENT_BRANCH, branchId);
                 } else {
                     request.getSession().setAttribute(ISecurityFilter.CURRENT_BRANCH_NAME, null);
                     request.getSession().setAttribute(ISecurityFilter.CURRENT_BRANCH, null);
                 }
               
                if(!ObjectUtil.isEmpty(branchMaster)){
                    if(StringUtil.isEmpty(branchMaster.getParentBranch())){
                        /*request.getSession().setAttribute(ISecurityFilter.MAPPED_BRANCHES, user.getBranchId()+","+branchMaster.getParentBranch());*/
                    	if(branchMaster.getStatus().equals(BranchMaster.INACTIVE)){
                    		throw new BranchDisabledException(WebConsoleError.BRANCH_DISABLED);
                    	}
                        branches = branchMaster.getBranchId()+",";
                        List<BranchMaster> branchesList = clientDAO.listChildBranchIds(branchMaster.getBranchId());
                        branchesList.stream().filter(branch->!StringUtil.isEmpty(branch)).forEach(branch->{
                            branches+=branch.getBranchId()+",";
                        });
                        request.getSession().setAttribute(ISecurityFilter.MAPPED_BRANCHES,StringUtil.removeLastComma(branches));
                    }else{
                    	if(branchMaster.getStatus().equals(BranchMaster.INACTIVE)){
                    		throw new BranchDisabledException(WebConsoleError.BRANCH_DISABLED);
                    	}
                    	BranchMaster parentBranchMaster = StringUtil.isEmpty(branchMaster.getParentBranch()) ? null
                                : clientDAO.findBranchMasterByBranchId(branchMaster.getParentBranch());
                    	
                    	if(!ObjectUtil.isEmpty(parentBranchMaster)&&parentBranchMaster.getStatus().equals(BranchMaster.INACTIVE)){
                    		throw new BranchDisabledException(WebConsoleError.PARENT_BRANCH_DISABLED);
                    	}
                        request.getSession().setAttribute(ISecurityFilter.MAPPED_BRANCHES, user.getBranchId());
                    }
                }
            }
        }else{
            user = userDAO.findByUsername(username);
        }
        // invalid user
        if (user == null) {
            throw new BadCredentialsException(WebConsoleError.INVALID_LOGIN);
        }

        Set<Entitlement> entitlements = null;
        if (user.getRole() != null) {
            Role role = roleDAO.loadRole(user.getRole().getId());
            if (role == null) {
                throw new UndefinedEntitlementException(WebConsoleError.INVALID_ENTITLEMENT);
            } else {
                entitlements = role.getEntitlements();
            }
        }

        // add user specific entitlements if any
        List<Entitlement> ents = userDAO.listEntitlements(username);
        if (ents != null && ents.size() > 0) {
            entitlements.addAll(ents);
        }

        if (entitlements == null || entitlements.size() == 0) {
            throw new UndefinedEntitlementException(WebConsoleError.INVALID_ENTITLEMENT);
        }

        LOGGER.debug("User Entitlements " + entitlements);

        GrantedAuthority[] authorities = (GrantedAuthority[]) entitlements
                .toArray(new GrantedAuthority[entitlements.size()]);
        // List<GrantedAuthority> authorityList = Arrays.asList(authorities);

        List<Entitlement> authorityList = new ArrayList<Entitlement>(
                user.getRole().getEntitlements());

        
        if(user.getRole().getIsAdminUser()!=null && user.getRole().getIsAdminUser().equals("true") && (user.getRole().getBranchId()==null || user.getRole().getBranchId().isEmpty())){
        	Entitlement ent =new Entitlement();
        	ent.setId(0);
        	ent.setAuthority("SuperAdmin");
        	authorityList.add(ent);
        }
        
        
        
        boolean accountNotLocked = isAccountNotLocked(user);

        String tenantId = (String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID);
        tenantId = StringUtil.isEmpty(tenantId) ? ISecurityFilter.DEFAULT_TENANT_ID : tenantId;
        String branchIdPart = StringUtil.isEmpty(branchId) ? "" : branchId + "_";
        String tenantUserName = tenantId + "_" + branchIdPart + user.getUsername();

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                tenantUserName, user.getPassword(), user.isEnabled(), true, true, accountNotLocked,
                authorityList);

        return userDetails;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.umgmt.security.IUserDetailsAdapter# isAccountNotLocked
     * (com.sourcetrace.eses.umgmt.entity.user.User)
     */
    public boolean isAccountNotLocked(User user) {

        // application settings
        ESESystem eseSystem = systemDAO.findESESystem();
        HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
        Object biObj = ObjectUtil.isEmpty(request) ? null
                : request.getSession().getAttribute(ISecurityFilter.CURRENT_BRANCH);

        if (biObj != null) {
            ESESystem eseSystemTemp = systemDAO.findPrefernceByOrganisationId(biObj.toString());
            eseSystem = eseSystemTemp == null || eseSystemTemp.getPreferences() == null
                    || eseSystemTemp.getPreferences().isEmpty() ? eseSystem : eseSystemTemp;
        }
        // invalid login count
        int count = 0;
        // configured time interval user is blocked after max invalid attempt
        long autoReleaseTime = 0;

        try {
            // read the above settings from system preferences
            count = Integer.parseInt(eseSystem.getPreferences()
                    .get(PreferenceType.NO_OF_INVALID_LOGIN_ATTEMPTS.name()));
            autoReleaseTime = Long.parseLong(
                    eseSystem.getPreferences().get(PreferenceType.TIME_TO_AUTO_RELEASE.name()));
        } catch (NumberFormatException e) {
            LOGGER.error("Wrong Invalid Login Settings");
        }

        boolean accountNotLocked = user.getStatus() < count;

        if (!accountNotLocked) {
            long msec = new Date().getTime() - user.getMilliSecond();
            accountNotLocked = msec >= autoReleaseTime;

            if (accountNotLocked) {
                if (user.getStatus() > 0) {
                    user.setStatus(0);
                    userDAO.update(user);
                }
            }
        }

        return accountNotLocked;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.umgmt.security.IUserDetailsAdapter#
     * setInvalidLoginCounter(java.lang.String)
     */

    public void setInvalidLoginCounter(String username) {

        User user = userDAO.findByUsername(username);

        if (user != null) {
            user.setStatus(user.getStatus() + 1);
            // user.setUpdateTime(new Date());
            user.setMilliSecond(new Date().getTime());
            userDAO.update(user);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.umgmt.security.IUserDetailsAdapter#
     * resetInvalidLoginCounter(java.lang.String)
     */
    public void resetInvalidLoginCounter(String username) {

        User user = userDAO.findByUsername(username);
        if (user.getStatus() > 0) {
            user.setStatus(0);
            userDAO.update(user);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.umgmt.security.IUserDetailsAdapter#getUserMenus( java.lang.String)
     */
    public List<Menu> getUserMenus(String username) {

        User user = userDAO.findByUsername(username);

        // application defined menu heirarchy
        List<Menu> menus = menuDAO.listMenus();

        // user menu in the application defined menu heirarchy
        List<Menu> userMenus = new ArrayList<Menu>();

        Role role = roleDAO.loadRoleMenus(user.getRole().getId());
        Set<Menu> set = role.getMenus();
        boolean dataFiltered = role.isDataFiltered();
        // loop all application defined menu
        for (Menu menu : menus) {

            // application menu is available in the user menu
            if (set.contains(menu)) {
                Menu userMenu = menu;
                Set<Menu> subMenus = menu.getSubMenus();
                userMenu.setSubMenus(getSubMenus(subMenus, set, dataFiltered));
                if (!ObjectUtil.isListEmpty(userMenu.getSubMenus()))
                    userMenus.add(userMenu);
            }
        }

        return userMenus;
    }

    /**
     * Gets the sub menus.
     * @param subMenus the sub menus
     * @param set the set
     * @param dataFiltered the data filtered
     * @return the sub menus
     */
    private Set<Menu> getSubMenus(Set<Menu> subMenus, Set<Menu> set, boolean dataFiltered) {

        Set<Menu> userSubMenus = null;
        if (subMenus != null) {
            userSubMenus = new LinkedHashSet<Menu>();
            for (Menu subMenu : subMenus) {
                if (set.contains(subMenu)) {
                    Menu userSubMenu = subMenu;
                    Set<Menu> subSubMenus = subMenu.getSubMenus();
                    userSubMenu.setSubMenus(getSubMenus(subSubMenus, set, dataFiltered));
                    if (!dataFiltered || subMenu.isDataFiltered()) {
                        if (subSubMenus.size() == 0 || userSubMenu.getSubMenus().size() > 0) {
                            userSubMenus.add(userSubMenu);
                        }
                    }
                }
            }
        }

        return userSubMenus;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.umgmt.security.IUserDetailsAdapter#getUserLanguage
     * (java.lang.String)
     */
    public String getUserLanguage(String username) {

        User user = userDAO.findByUsername(username);
        return user.getLanguage();
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.umgmt.security.IUserDetailsAdapter#getUser(java. lang.String)
     */
    public User getUser(String username) {

        User user = userDAO.findByUsername(username);
        return user;
    }

    @Override
    public Object[] getAgentInfo(String profileId) {

        return userDAO.findWebUserAgentInfoByProfileId(profileId);
    }

}
