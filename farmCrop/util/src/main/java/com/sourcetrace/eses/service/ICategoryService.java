/*
 * ICategoryService.java
 * Copyright (c) 2012-2013, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import java.util.List;

import com.sourcetrace.eses.order.entity.profile.Category;
import com.sourcetrace.eses.order.entity.profile.SubCategory;

// TODO: Auto-generated Javadoc
/**
 * The Interface ICategoryService.
 */
public interface ICategoryService {

	/**
	 * Adds the category.
	 * 
	 * @param category
	 *            the category
	 */
	void addCategory(Category category);

	/**
	 * Find category by id.
	 * 
	 * @param id
	 *            the id
	 * 
	 * @return the category
	 */
	Category findCategoryById(long id);

	/**
	 * Edits the category.
	 * 
	 * @param existing
	 *            the existing
	 */
	void editCategory(Category existing);

	/**
	 * Removes the category.
	 * 
	 * @param category
	 *            the category
	 */
	void removeCategory(Category category);

	/**
	 * Find category by name.
	 * 
	 * @param name
	 *            the name
	 * 
	 * @return the category
	 */
	Category findCategoryByName(String name);

	/**
	 * List category.
	 * 
	 * @return the list< category>
	 */
	List<Category> listCategory();

	/**
	 * Adds the sub category.
	 * 
	 * @param subcategory
	 *            the subcategory
	 */
	void addSubCategory(SubCategory subcategory);

	/**
	 * Find sub category by id.
	 * 
	 * @param id
	 *            the id
	 * 
	 * @return the sub category
	 */
	SubCategory findSubCategoryById(long id);

	/**
	 * Edits the sub category.
	 * 
	 * @param existing
	 *            the existing
	 */
	void editSubCategory(SubCategory existing);

	/**
	 * Removes the sub category.
	 * 
	 * @param subcategory
	 *            the subcategory
	 */
	void removeSubCategory(SubCategory subcategory);

	/**
	 * Find sub category by name.
	 * 
	 * @param name
	 *            the name
	 * 
	 * @return the sub category
	 */
	SubCategory findSubCategoryByName(String name);

	/**
	 * List sub category.
	 * 
	 * @return the list< sub category>
	 */
	List<SubCategory> listSubCategory();

	/**
	 * List sub category by category.
	 * 
	 * @param name
	 *            the name
	 * 
	 * @return the list< sub category>
	 */
	List<SubCategory> listSubCategoryByCategory(String name);

	/**
	 * Find sub category by category code.
	 * 
	 * @param categoryCode
	 *            the category code
	 * 
	 * @return the list< sub category>
	 */
	List<SubCategory> findSubCategoryByCategoryCode(String categoryCode);

    List<SubCategory> listSubCategoryByDefaultCategory();
    
    SubCategory findSubCategoryByCode(String code);

	Object[] findByCategoryNameAndId(String categoryName);

	List<SubCategory> listDistributionDetailBySubCategory();
    

}
