/*
 * CategoryService.java
 * Copyright (c) 2012-2013, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

package com.sourcetrace.eses.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sourcetrace.eses.dao.ICategoryDAO;
import com.sourcetrace.eses.order.entity.profile.Category;
import com.sourcetrace.eses.order.entity.profile.SubCategory;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.profile.Product;

// TODO: Auto-generated Javadoc
/**
 * The Class CategoryService.
 */
@Transactional
@Service
public class CategoryService implements ICategoryService {

    /** The category dao. */
    @Autowired
    private ICategoryDAO categoryDAO;

    /**
     * Gets the category dao.
     * @return the category dao
     */
    public ICategoryDAO getCategoryDAO() {

        return categoryDAO;
    }

    /**
     * Sets the category dao.
     * @param categoryDAO the new category dao
     */
    public void setCategoryDAO(ICategoryDAO categoryDAO) {

        this.categoryDAO = categoryDAO;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.order.service.service.ICategoryService#addCategory
     * (com.sourcetrace.eses.order.entity.profile.Category)
     */
    public void addCategory(Category category) {

        categoryDAO.save(category);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.order.service.service.ICategoryService#findCategoryById (long)
     */
    public Category findCategoryById(long id) {

        return categoryDAO.findCategoryById(id);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.order.service.service.ICategoryService#editCategory
     * (com.sourcetrace.eses.order.entity.profile.Category)
     */
    public void editCategory(Category existing) {

        if (!ObjectUtil.isListEmpty(existing.getSubcategory())) {
            for (SubCategory subCategory : existing.getSubcategory()) {
                if (!ObjectUtil.isListEmpty(subCategory.getProducts())) {
                    for (Product product : subCategory.getProducts()) {
                        product.setRevisionNo(DateUtil.getRevisionNumber());
                    }
                }
            }
        }
        categoryDAO.update(existing);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.order.service.service.ICategoryService#removeCategory
     * (com.sourcetrace.eses.order.entity.profile.Category)
     */
    public void removeCategory(Category category) {

        categoryDAO.delete(category);

    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.eses.order.service.service.ICategoryService#
     * findCategoryByName(java.lang.String)
     */
    public Category findCategoryByName(String name) {

        return categoryDAO.findCategoryByName(name);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.order.service.service.ICategoryService#listCategory ()
     */
    public List<Category> listCategory() {

        return categoryDAO.listCategory();
    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.eses.order.service.service.ICategoryService#
     * findSubCategoryByName(java.lang.String)
     */
    public SubCategory findSubCategoryByName(String name) {

        return categoryDAO.findSubCategoryByName(name);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.order.service.service.ICategoryService#listSubCategory ()
     */
    public List<SubCategory> listSubCategory() {

        return categoryDAO.listSubCategory();
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.order.service.service.ICategoryService#addSubCategory
     * (com.sourcetrace.eses.order.entity.profile.SubCategory)
     */
    public void addSubCategory(SubCategory subcategory) {

        categoryDAO.save(subcategory);

    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.order.service.service.ICategoryService#editSubCategory
     * (com.sourcetrace.eses.order.entity.profile.SubCategory)
     */
    public void editSubCategory(SubCategory subcategory) {

        if (!ObjectUtil.isListEmpty(subcategory.getProducts())) {
            for (Product product : subcategory.getProducts()) {
                product.setRevisionNo(DateUtil.getRevisionNumber());
            }
        }
        categoryDAO.update(subcategory);
    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.eses.order.service.service.ICategoryService# findSubCategoryById(long)
     */
    public SubCategory findSubCategoryById(long id) {

        return categoryDAO.findSubCategoryById(id);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.order.service.service.ICategoryService#removeSubCategory
     * (com.sourcetrace.eses.order.entity.profile.SubCategory)
     */
    public void removeSubCategory(SubCategory subcategory) {

        categoryDAO.delete(subcategory);

    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.eses.order.service.service.ICategoryService#
     * listSubCategoryByCategory(java.lang.String)
     */
    public List<SubCategory> listSubCategoryByCategory(String name) {

        return categoryDAO.listSubCategoryByCategory(name);

    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.eses.order.service.service.ICategoryService#
     * findSubCategoryByCategoryCode(java.lang.String)
     */
    public List<SubCategory> findSubCategoryByCategoryCode(String categoryCode) {

        return categoryDAO.findSubCategoryByCategoryCode(categoryCode);
    }

    public List<SubCategory> listSubCategoryByDefaultCategory() {

        return categoryDAO.listSubCategoryByDefaultCategory();

    }

    @Override
    public SubCategory findSubCategoryByCode(String code) {

        return categoryDAO.findSubCategoryByCode(code);
    }

	@Override
	public Object[] findByCategoryNameAndId(String categoryName) {
		// TODO Auto-generated method stub
		return categoryDAO.findByCategoryNameAndId(categoryName);
	}

	public List<SubCategory> listDistributionDetailBySubCategory() {
		// TODO Auto-generated method stub
		return categoryDAO.listDistributionDetailBySubCategory();
	}
}
