/*
 * CategoryDAO.java
 * Copyright (c) 2012-2013, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sourcetrace.eses.order.entity.profile.Category;
import com.sourcetrace.eses.order.entity.profile.SubCategory;

// TODO: Auto-generated Javadoc
/**
 * The Class CategoryDAO.
 */
@Repository
@Transactional
public class CategoryDAO extends ESEDAO implements ICategoryDAO {

	@Autowired
	public CategoryDAO(SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.ICategoryDAO#findCategoryByCode
	 * (java.lang.String)
	 */
	public Category findCategoryByCode(String code) {
		Category category = (Category) find("FROM Category c LEFT JOIN FETCH c.subcategory WHERE c.code = ?", code);
		return category;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.ICategoryDAO#findCategoryByName
	 * (java.lang.String)
	 */
	public Category findCategoryByName(String name) {
		Category category = (Category) find("FROM Category c LEFT JOIN FETCH c.subcategory WHERE c.name = ?", name);
		return category;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.ICategoryDAO#findCategoryById(
	 * long)
	 */
	public Category findCategoryById(long id) {
		Category category = (Category) find("FROM Category c LEFT JOIN FETCH c.subcategory WHERE c.id = ?", id);
		return category;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.ICategoryDAO#listCategory()
	 */
	@SuppressWarnings("unchecked")
	public List<Category> listCategory() {
		return list("FROM Category c ORDER BY c.name ASC");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.ICategoryDAO#findSubCategoryByName
	 * (java.lang.String)
	 */
	public SubCategory findSubCategoryByName(String name) {
		SubCategory subcategory = (SubCategory) find(
				"FROM SubCategory sc LEFT JOIN FETCH sc.products WHERE sc.name = ?", name);
		return subcategory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.ICategoryDAO#listSubCategory()
	 */
	@SuppressWarnings("unchecked")
	public List<SubCategory> listSubCategory() {
		return list("FROM SubCategory");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.ICategoryDAO#findSubCategoryById
	 * (long)
	 */
	public SubCategory findSubCategoryById(long id) {
		SubCategory subcategory = (SubCategory) find("FROM SubCategory sc LEFT JOIN FETCH sc.products WHERE sc.id = ?",
				id);
		return subcategory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.ICategoryDAO#findSubCategoryByCode
	 * (java.lang.String)
	 */
	public SubCategory findSubCategoryByCode(String code) {
		SubCategory subCategory = (SubCategory) find(
				"FROM SubCategory sc LEFT JOIN FETCH sc.products WHERE sc.code = ?", code);
		return subCategory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.ICategoryDAO#
	 * listSubCategoryByCategory (java.lang.String)
	 */
	public List<SubCategory> listSubCategoryByCategory(String name) {
		return list("FROM SubCategory sc  WHERE sc.category.name = ?  ORDER BY sc.name ASC", name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.ICategoryDAO#
	 * findSubCategoryByCategoryCode(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<SubCategory> findSubCategoryByCategoryCode(String categoryCode) {

		return list("FROM SubCategory sc WHERE sc.category.code=?", categoryCode);
	}

	public List<SubCategory> listSubCategoryByDefaultCategory() {

		return list("FROM SubCategory sc ORDER BY sc.name ASC");
	}

	@Override
	public Object[] findByCategoryNameAndId(String categoryName) {
		// TODO Auto-generated method stub
		return (Object[]) find("select sc.id,sc.name from SubCategory sc where sc.name=?",categoryName);
	}

	@Override
	public List<SubCategory> listDistributionDetailBySubCategory() {
		// TODO Auto-generated method stub
		return list("FROM  SubCategory dd ORDER BY dd.name");
	}

}
