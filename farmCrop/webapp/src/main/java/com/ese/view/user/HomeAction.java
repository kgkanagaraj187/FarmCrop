/*
 * HomeAction.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.user;

import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.ese.view.ESEAction;
import com.sourcetrace.eses.service.IUserService;
import com.sourcetrace.eses.umgmt.entity.User;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class HomeAction extends ESEAction {

	@Autowired
	private IUserService userService;

	/**
	 * 
	 */
	private static final long serialVersionUID = -1150435185457016568L;

	public String list() throws Exception {

		request.setAttribute(HEADING, getText(LIST));
		// Added to Disable HomeList when user is not logged in
		// Authentication Manual handling is needed since filters="none"
		// attribute is set in spring
		// security
		// Added for Bug Fix (ESE_AGRO 242 - Login Page Freeze)
		if (ObjectUtil.isEmpty(request.getSession()) || StringUtil.isEmpty(request.getSession().getAttribute("user"))) {
			return "login";
		}
		String userName = (String) request.getSession().getAttribute("user");
		// checked whether password has been reset
		User user = userService.findUserByUserName(userName);
		// type attribute is set when its redirected from password reset page
		String type = request.getParameter("type");
		/*if (user.isReset() && StringUtil.isEmpty(type))
			return "resetPassword";*/
		if (userService.isDashboardUserEntitlementAvailable(userName)) {
			return "dashboard";
		} else {

			// sets ese applcation theme after the login.
			if (StringUtil.isEmpty(request.getSession().getAttribute(ESESystem.SESSION_THEME_ATTRIBUTE_NAME))) {
				// String theme =
				// this.preferncesService.findPrefernceById(ESESystem.SYSTEM_ESE)
				// .getPreferences().get(ESESystem.THEME);
				String theme = "agro-theme";
				request.getSession().setAttribute(ESESystem.SESSION_THEME_ATTRIBUTE_NAME,
						StringUtil.isEmpty(theme) ? getText("default.theme") : theme);
			}

			localizeMenu();
			return LIST;
		}
		// return userService.isDashboardUserEntitlementAvailable(userName) ?
		// "dashboard" : LIST ;
	}

}
