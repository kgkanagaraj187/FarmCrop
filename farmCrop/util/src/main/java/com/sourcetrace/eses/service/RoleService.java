/*
 * RoleService.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sourcetrace.eses.dao.IMenuDAO;
import com.sourcetrace.eses.dao.IRoleDAO;
import com.sourcetrace.eses.umgmt.entity.Action;
import com.sourcetrace.eses.umgmt.entity.Entitlement;
import com.sourcetrace.eses.umgmt.entity.Filter;
import com.sourcetrace.eses.umgmt.entity.Menu;
import com.sourcetrace.eses.umgmt.entity.Role;
import com.sourcetrace.eses.util.ObjectUtil;

/**
 * RoleService implements the interface IRoleService.
 * 
 * @author $Author: aravind $
 * @version $Rev: 684 $, $Date: 2010-02-03 22:32:04 +0530 (Wed, 03 Feb 2010) $
 */

@Service
@Transactional
public class RoleService implements IRoleService {

	private static Logger LOGGER = Logger.getLogger(RoleService.class);
	@Autowired
	private IRoleDAO roleDAO;
	@Autowired
	private IMenuDAO menuDAO;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IRoleService#listRoles()
	 */
	public List<Role> listRoles() {

		return roleDAO.listRoles();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IRoleService#findRole(long)
	 */
	public Role findRole(long id) {

		return roleDAO.findRole(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IRoleService#addRole(com.ese.entity.user.Role)
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void addRole(Role aRole) {

		roleDAO.save(aRole);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IRoleService#editRole(com.ese.entity.user.Role)
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void editRole(Role role) {

		roleDAO.update(role);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ese.service.user.IRoleService#removeRole(com.ese.entity.user.Role)
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void removeRole(Role aRole) {

		roleDAO.delete(aRole);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IRoleService#listParentMenus()
	 */
	public List<Menu> listParentMenus() {

		return menuDAO.listTopParentMenus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IRoleService#getAvailableSelectedSubMenus(long,
	 * long)
	 */
	public Map<String, List<Menu>> getAvailableSelectedSubMenus(long roleId, long parentMenuId) {

		// load role menus
		Role role = roleDAO.loadRoleMenus(roleId);

		// list flat all sub menus for the given top most parent menu
		List<Menu> subMenus = menuDAO.listFlatSubMenusForTopParent(parentMenuId);

		// create the map and list to hold the selected and available menus
		Map<String, List<Menu>> menusAvailableSelected = new LinkedHashMap<String, List<Menu>>();
		List<Menu> selected = new ArrayList<Menu>();
		menusAvailableSelected.put(SELECTED, selected);
		List<Menu> available = new ArrayList<Menu>();
		menusAvailableSelected.put(AVAILABLE, available);

		if (role != null) {
			// get menus for the given role
			Set<Menu> menus = role.getMenus();
			boolean dataFiltered = role.isDataFiltered();

			// loop all submenus for the given parent menu id
			for (Menu subMenu : subMenus) {
				if (menus.contains(subMenu)) {
					// add the menu to selected, if the set of menu for the role
					// has it
					if (dataFiltered) {
						if (subMenu.isDataFiltered()) {
							selected.add(subMenu);
						}
					} else {
						selected.add(subMenu);
					}
				} else {
					// add the menu to available, if the set of menu for the
					// role does not have it
					if (dataFiltered) {
						if (subMenu.isDataFiltered()) {
							available.add(subMenu);
						}
					} else {
						available.add(subMenu);
					}
				}
			}
		} else {
			available.addAll(subMenus);
		}

		return menusAvailableSelected;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IRoleService#getSelectedSubMenus(long, long)
	 */
	public List<Menu> getSelectedSubMenus(long roleId, long parentMenuId) {

		// find the role for the given id
		Role role = roleDAO.loadRoleMenus(roleId);

		// list to hold selected menus for the given role and top most parent
		List<Menu> selected = new ArrayList<Menu>();

		if (role != null) {
			LOGGER.debug("Menus " + role.getMenus());
			// get menus for the given role
			Set<Menu> menus = role.getMenus();
			boolean dataFiltered = role.isDataFiltered();

			// list flat all sub menus for the given top most parent menu
			List<Menu> subMenus = menuDAO.listFlatSubMenusForTopParent(parentMenuId);

			// loop all sub menus
			for (Menu subMenu : subMenus) {
				// given role has the sub menu, then add to list
				if (menus.contains(subMenu)) {
					if (dataFiltered) {
						if (subMenu.isDataFiltered()) {
							selected.add(subMenu);
						}
					} else {
						selected.add(subMenu);
					}
				}
			}
		}

		return selected;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IRoleService#editSubMenusForRole(long, long,
	 * java.util.List)
	 */

	public void editSubMenusForRole(long roleId, long parentMenuId, List<Long> selectedSubMenus) {

		// find the role for the given id
		Role role = roleDAO.loadRoleMenus(roleId);
		Set<Menu> menus = null;

		if (role != null) {
			// get menus for the given role
			menus = role.getMenus();
			// remove menus which were not selected
			removeUnselectedSubMenu(parentMenuId, menus, selectedSubMenus, role);
			// remove parent menus which don't have any child
			removeParentsWithoutChild(menus);
		} else {
			role = roleDAO.findRole(roleId);
			menus = new LinkedHashSet<Menu>();
			role.setMenus(menus);
		}

		// add newly selected menus and any of its missing parent
		addSelectedSubMenu(menus, selectedSubMenus, role);

		// update the menus for the given role and top most parent menu
		roleDAO.update(role);
	}

	/**
	 * Removes the unselected sub menu.
	 * 
	 * @param parentMenuId
	 *            the parent menu id
	 * @param menus
	 *            the menus
	 * @param selectedSubMenus
	 *            the selected sub menus
	 */
	private void removeUnselectedSubMenu(Long parentMenuId, Set<Menu> menus, List<Long> selectedSubMenus, Role role) {

		// list to hold the sub menu ids, which have to be removed
		List<Long> removeIds = new ArrayList<Long>();
		// list flat all sub menus for the given top most parent menu
		List<Menu> subMenus = menuDAO.listFlatSubMenusForTopParent(parentMenuId);
		// loop all sub menus
		for (Menu subMenu : subMenus) {
			// submenu is not present in the selected sub menu list
			// add it to removal list
			if (!selectedSubMenus.contains(subMenu.getId())) {
				removeIds.add(subMenu.getId());
			}
		}

		// loop the list of sub menus to be removed
		for (Long id : removeIds) {
			// loop the set of menus for the given role
			for (Menu menu : menus) {
				// menu id equals the id to be removed
				if (menu.getId() == id) {

					// // loading entitlements for Menu name
					// List<Object[]> entitlements =
					// roleDAO.loadEntitlements(menu.getName());
					// if (!entitlements.isEmpty()) {
					// for (Object[] menuEntitlement : entitlements) {
					// long entId =
					// Long.parseLong(menuEntitlement[0].toString());
					// // remove entitlement for the unselected menu
					// roleDAO.removeEntitlement(role.getId(), entId);
					// }
					// }

					// remove the menu from the set of menus
					menus.remove(menu);
					break;
				}
			}
		}
	}

	/**
	 * Removes the parents without child.
	 * 
	 * @param menus
	 *            the menus
	 */
	private void removeParentsWithoutChild(Set<Menu> menus) {

		List<Menu> parentsToRemove = new ArrayList<Menu>();
		for (Menu menu : menus) {
			if (menu.getUrl().equals("javascript:void(0)") && menu.getParentId() != null) {
				boolean childExists = false;
				for (Menu aMenu : menus) {
					if (aMenu.getParentId() != null && aMenu.getParentId().equals(menu.getId())) {
						childExists = true;
					}
				}

				if (!childExists) {
					parentsToRemove.add(menu);
				}
			}
		}

		for (Menu parentMenu : parentsToRemove) {
			LOGGER.debug("removing parent menu as it has no child " + parentMenu);
			menus.remove(parentMenu);
		}
	}

	/**
	 * Adds the selected sub menu.
	 * 
	 * @param menus
	 *            the menus
	 * @param selectedSubMenus
	 *            the selected sub menus
	 * @param role
	 *            the role
	 */
	public void addSelectedSubMenu(Set<Menu> menus, List<Long> selectedSubMenus, Role role) {

		if (ObjectUtil.isListEmpty(role.getEntitlements())) {
			role.setEntitlements(new LinkedHashSet<Entitlement>());
		}

		for (Long id : selectedSubMenus) {
			Menu menu = menuDAO.findMenu(id);
			if (!menus.contains(menu)) {
				LOGGER.debug("adding menu " + menu);
				menus.add(menu);
				// When adding a menu for a role,Add the List Entitlement also
				Entitlement ent = roleDAO.findEntitlement(menu.getName() + ".list");
				if (!ObjectUtil.isEmpty(ent)) {
					role.getEntitlements().add(ent);
				}
			}

			Menu parent = menuDAO.findMenu(menu.getParentId());
			if (!menus.contains(parent)) {
				LOGGER.debug("adding parent " + parent);
				menus.add(parent);
			}

			if (parent.getParentId() != null) {
				parent = menuDAO.findMenu(parent.getParentId());
				if (!menus.contains(parent)) {
					LOGGER.debug("adding parent of parent " + parent);
					menus.add(parent);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IRoleService#findRole(java.lang.String)
	 */
	public Role findRole(String name) {

		Role role = roleDAO.findRoleByName(name);
		return role;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IRoleService#getSelectedSubMenusActions(long,
	 * long)
	 */
	public Map<String, Map<String, String>> getSelectedSubMenusActions(long roleId, long parentMenuId) {

		List<Menu> selectedSubMenus = getSelectedSubMenus(roleId, parentMenuId);
		Map<String, Map<String, String>> subMenusActions = buildMapForSelectedMenusActions(selectedSubMenus);
		return subMenusActions;
	}

	/**
	 * Builds the map for selected menus actions.
	 * 
	 * @param selectedSubMenus
	 *            the selected sub menus
	 * @return the map< string, map< string, boolean>>
	 */
	private Map<String, Map<String, String>> buildMapForSelectedMenusActions(List<Menu> selectedSubMenus) {

		Map<String, Map<String, String>> subMenusActions = new LinkedHashMap<String, Map<String, String>>();

		for (Menu selectedSubMenu : selectedSubMenus) {

			String subMenuName = selectedSubMenu.getName();
			Map<String, String> selectedActions = new LinkedHashMap<String, String>();

			for (Action action : selectedSubMenu.getActions()) {
				String actionName = action.getName();
				String entitlementName = subMenuName + "." + actionName;
				selectedActions.put(actionName, entitlementName);
			}
			subMenusActions.put(subMenuName, selectedActions);
		}

		return subMenusActions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IRoleService#getEntitlements(long, long)
	 */
	public List<String> getEntitlements(long roleId, long parentMenuId) {

		Role role = roleDAO.loadRole(roleId);
		List<String> entitlements = new ArrayList<String>();

		if (role != null) {
			Set<Entitlement> ents = role.getEntitlements();
			List<Menu> selectedSubMenus = getSelectedSubMenus(roleId, parentMenuId);
			for (Menu selected : selectedSubMenus) {
				Set<Action> actions = selected.getActions();
				for (Action action : actions) {
					String entitlementName = selected.getName() + "." + action.getName();
					boolean entitled = false;
					for (Entitlement ent : ents) {
						if (entitlementName.equals(ent.getAuthority())) {
							entitled = true;
							break;
						}
					}

					if (entitled) {
						entitlements.add(entitlementName);
					}
				}
			}
		}

		return entitlements;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IRoleService#editEntitlements(long, long,
	 * java.util.List)
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Role editEntitlements(long roleId, long parentMenuId, List<String> entitlements) {

		Role role = roleDAO.loadRole(roleId);
		Set<Entitlement> ents = null;
		Set<Menu> menus = null;

		if (role == null) {
			role = roleDAO.loadRoleMenus(roleId);
			ents = new LinkedHashSet<Entitlement>();
			if (!ObjectUtil.isEmpty(role)) {
				role.setEntitlements(ents);
			}
		} else {
			ents = role.getEntitlements();
		}

		List<Menu> subMenus = menuDAO.listFlatSubMenusForTopParent(parentMenuId);
		List<Menu> selectedSubMenus = new ArrayList<Menu>();

		if (role != null) {
			menus = role.getMenus();

			for (Menu subMenu : subMenus) {
				if (menus.contains(subMenu)) {
					selectedSubMenus.add(subMenu);
				}
			}
		}

		LOGGER.debug("Existing entitlements " + ents);
		LOGGER.debug("Selected entitlements " + entitlements);

		List<Entitlement> selectedEnts = new ArrayList<Entitlement>();
		for (String entitlement : entitlements) {
			Entitlement selectedEnt = new Entitlement();
			selectedEnt.setAuthority(entitlement);
			selectedEnts.add(selectedEnt);
		}

		removeUnselectedActions(ents, selectedSubMenus, selectedEnts);

		for (Entitlement selected : selectedEnts) {
			if (!ents.contains(selected)) {
				LOGGER.info("finding entitlement " + selected.getAuthority());
				Entitlement newEntitlement = roleDAO.findEntitlement(selected.getAuthority());
				LOGGER.debug("adding entitlement " + newEntitlement.getId());
				ents.add(newEntitlement);
			}
		}
		if (!ObjectUtil.isEmpty(role)) {
			roleDAO.update(role);
		}
		return role;
	}

	/**
	 * Removes the unselected actions.
	 * 
	 * @param ents
	 *            the ents
	 * @param selectedSubMenus
	 *            the selected sub menus
	 * @param selectedEnts
	 *            the selected ents
	 */
	private void removeUnselectedActions(Set<Entitlement> ents, List<Menu> selectedSubMenus,
			List<Entitlement> selectedEnts) {

		for (Menu selectedSubMenu : selectedSubMenus) {

			String subMenuName = selectedSubMenu.getName();

			for (Action action : selectedSubMenu.getActions()) {
				String actionName = action.getName();
				String entitlementName = subMenuName + "." + actionName;
				Entitlement oldEnt = new Entitlement();
				oldEnt.setAuthority(entitlementName);
				if (!selectedEnts.contains(oldEnt)) {
					LOGGER.debug("removing entitlement " + oldEnt);
					ents.remove(oldEnt);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IRoleService#listRolesByType(int)
	 */
	public List<Role> listRolesByType(int type) {

		return roleDAO.listRolesByType(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IRoleService#listFilters()
	 */
	public List<Filter> listFilters() {

		return roleDAO.listFilters();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IRoleService#findFilter(int)
	 */
	public Filter findFilter(int id) {

		return roleDAO.findFilter(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.service.user.IRoleService#listAction()
	 */
	public List<Action> listAction() {

		return roleDAO.listAction();
	}

	@Override
	public List<Role> listRolesByTypeAndBranchId(int type, String branchId) {
		// TODO Auto-generated method stub
		return roleDAO.listRolesByTypeAndBranchId(type, branchId);
	}

	@Override
	public Object[] findRoleInfo(long id) {
		// TODO Auto-generated method stub
		return roleDAO.findRoleInfo(id);
	}

    @Override
    public List<Role> listRolesByTypeAndBranchIdExcludeBranch(int id, String branchId_F) {

        return roleDAO.listRolesByTypeAndBranchIdExcludeBranch(id,branchId_F);
    }

}
