
package com.sourcetrace.eses.umgmt.entity;

import java.io.Serializable;
import java.util.Set;

/**
 * The Class Menu.
 */
public class Menu implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -400994598453972549L;
    public static final int DIMENSION_VERTICAL = 0;
    public static final int DIMENSION_HORIZONTAL = 1;

    public static enum PRIORITIES {
        OLD, NEW
    };
    
    private long id;
    private Long parentId;
    private int order;
    private String description;
    private String label;
    private String url;
    private Set<Action> actions;
    private Set<Menu> subMenus;
    private boolean dataFiltered;
    private int dimension;
    private boolean exportAvailability;
    private int priority;
    private String iconClass;

    // Transient variable to set entitlement name as icon class name
    private String menuClassName;

    /**
     * Checks if is data filtered.
     *
     * @return true, if is data filtered
     */
    public boolean isDataFiltered() {

        return dataFiltered;
    }

    /**
     * Sets the data filtered.
     *
     * @param dataFiltered the new data filtered
     */
    public void setDataFiltered(boolean dataFiltered) {

        this.dataFiltered = dataFiltered;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Gets the label.
     *
     * @return the label
     */
    public String getLabel() {

        return label;
    }

    /**
     * Sets the label.
     *
     * @param label the new label
     */
    public void setLabel(String label) {

        this.label = label;
    }

    /**
     * Gets the url.
     *
     * @return the url
     */
    public String getUrl() {

        return url;
    }

    /**
     * Sets the url.
     *
     * @param url the new url
     */
    public void setUrl(String url) {

        this.url = url;
    }

    /**
     * Gets the sub menus.
     *
     * @return the sub menus
     */
    public Set<Menu> getSubMenus() {

        return subMenus;
    }

    /**
     * Sets the sub menus.
     *
     * @param subMenu the new sub menus
     */
    public void setSubMenus(Set<Menu> subMenu) {

        this.subMenus = subMenu;
    }

    /**
     * Gets the order.
     *
     * @return the order
     */
    public int getOrder() {

        return order;
    }

    /**
     * Sets the order.
     *
     * @param order the new order
     */
    public void setOrder(int order) {

        this.order = order;
    }

    /**
     * Gets the parent id.
     *
     * @return the parent id
     */
    public Long getParentId() {

        return parentId;
    }

    /**
     * Sets the parent id.
     *
     * @param parentId the new parent id
     */
    public void setParentId(Long parentId) {

        this.parentId = parentId;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {

        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the new description
     */
    public void setDescription(String description) {

        this.description = description;
    }

    /**
     * Gets the abbreviation.
     *
     * @return the abbreviation
     */
    public String getAbbreviation() {

        return null;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {

        return getLabel();
    }

    /**
     * Sets the abbreviation.
     *
     * @param abbreviation the new abbreviation
     */
    public void setAbbreviation(String abbreviation) {

    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name) {

        setLabel(name);
    }

    /**
     * Gets the actions.
     *
     * @return the actions
     */
    public Set<Action> getActions() {

        return actions;
    }

    /**
     * Sets the actions.
     *
     * @param actions the new actions
     */
    public void setActions(Set<Action> actions) {

        this.actions = actions;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return label;
    }

    public String getMenuClassName() {
        return menuClassName;
    }

    public void setMenuClassName(String menuClassName) {
        this.menuClassName = menuClassName;
    }

    /**
     * Gets the dimension.
     *
     * @return the dimension
     */
    public int getDimension() {

        return dimension;
    }

    /**
     * Sets the dimension.
     *
     * @param dimension the new dimension
     */
    public void setDimension(int dimension) {

        this.dimension = dimension;
    }

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        boolean equals = false;

        if (obj instanceof Menu) {
            Menu menu = (Menu) obj;
            equals = menu.id == this.id;
        }

        return equals;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        Object obj = new Long(id);
        return obj.hashCode();
    }

    /**
     * Checks if is vertical dimension.
     *
     * @return true, if is vertical dimension
     */
    public boolean isVerticalDimension() {

        return (dimension == DIMENSION_VERTICAL);
    }

    /**
     * Checks if is horizontal dimension.
     *
     * @return true, if is horizontal dimension
     */
    public boolean isHorizontalDimension() {

        return (dimension == DIMENSION_HORIZONTAL);
    }

    public boolean isExportAvailability() {
        return exportAvailability;
    }

    public void setExportAvailability(boolean exportAvailability) {
        this.exportAvailability = exportAvailability;
    }

}