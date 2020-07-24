/*
 * ICategoryDAO.java
 * Copyright (c) 2012-2013, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.List;

import com.sourcetrace.eses.order.entity.profile.Category;
import com.sourcetrace.eses.order.entity.profile.SubCategory;

// TODO: Auto-generated Javadoc
/**
 * The Interface ICategoryDAO.
 */
public interface ICategoryDAO extends IESEDAO {

    /**
     * Find category by code.
     * @param code the code
     * @return the category
     */
    Category findCategoryByCode(String code);

    /**
     * Find category by name.
     * @param name the name
     * @return the category
     */
    Category findCategoryByName(String name);

    /**
     * Find category by id.
     * @param id the id
     * @return the category
     */
    Category findCategoryById(long id);

    /**
     * List category.
     * @return the list< category>
     */
    List<Category> listCategory();

    /**
     * Find sub category by name.
     * @param name the name
     * @return the sub category
     */
    SubCategory findSubCategoryByName(String name);

    /**
     * List sub category.
     * @return the list< sub category>
     */
    List<SubCategory> listSubCategory();

    /**
     * Find sub category by id.
     * @param id the id
     * @return the sub category
     */
    SubCategory findSubCategoryById(long id);

    /**
     * Find sub category by code.
     * @param code the code
     * @return the sub category
     */
    SubCategory findSubCategoryByCode(String code);

    /**
     * List sub category by category.
     * @param name the name
     * @return the list< sub category>
     */
    List<SubCategory> listSubCategoryByCategory(String name);

    /**
     * Find sub category by category code.
     * @param categoryCode the category code
     * @return the list< sub category>
     */
    List<SubCategory> findSubCategoryByCategoryCode(String categoryCode);

    List<SubCategory> listSubCategoryByDefaultCategory();

	Object[] findByCategoryNameAndId(String categoryName);

	List<SubCategory> listDistributionDetailBySubCategory();
}
