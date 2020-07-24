/*
 * ProductServiceReportDAO.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.profile.Product;
import com.sourcetrace.esesw.entity.profile.Farmer;

/**
 * The Class ProductServiceReportDAO.
 */
public class ProductServiceReportDAO extends ReportDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.ReportDAO#addExampleFiltering(org.hibernate.Criteria,
	 * java.util.Map)
	 */
	@Override
	protected void addExampleFiltering(Criteria criteria, Map params) {

		// check for filter entity
		Product entity = (Product) params.get(FILTER);

		if (entity != null) {

			criteria.createAlias("subcategory", "sc");

			if (entity.getName() != null && !"".equals(entity.getName()))
				criteria.add(Restrictions.like("name", entity.getName(), MatchMode.ANYWHERE));
			if (entity.getCode() != null && !"".equals(entity.getCode()))
				criteria.add(Restrictions.like("code", entity.getCode(), MatchMode.ANYWHERE));

			if (entity.getPrice() != null && !"".equals(entity.getPrice()))
				criteria.add(Restrictions.like("price", entity.getPrice(), MatchMode.ANYWHERE));

			if (entity.getUnit() != null && !"".equals(entity.getUnit()))
				criteria.add(Restrictions.like("unit", entity.getUnit(), MatchMode.ANYWHERE));

			if (entity.getSubcategory() != null && entity.getSubcategory().getName() != null
					&& !"".equals(entity.getSubcategory().getName()))
				criteria.add(Restrictions.like("sc.name", entity.getSubcategory().getName(), MatchMode.ANYWHERE));
					
	/*		if (!ObjectUtil.isEmpty(entity.getSubcategory()) && entity.getSubcategory().getBranchId() !=null) {
				criteria.add(
						Restrictions.like("sc.branchId", entity.getSubcategory().getBranchId(), MatchMode.ANYWHERE));

			}*/

			if (entity.getSubcategory() != null && entity.getSubcategory().getCategory() != null
					&& entity.getSubcategory().getCategory().getName() != null
					&& !"".equals(entity.getSubcategory().getCategory().getName()))
				criteria.add(Restrictions.like("c.name", entity.getSubcategory().getCategory().getName(),
						MatchMode.ANYWHERE));

			if(entity.getManufacture() !=null && !"".equals(entity.getManufacture())){
			
				 DetachedCriteria farmerCriteria = DetachedCriteria.forClass(FarmCatalogue.class);
                 farmerCriteria.setProjection(Property.forName("code"));
                 farmerCriteria.add(Restrictions.like("name", entity.getManufacture(),
                         MatchMode.ANYWHERE));
                 
                 criteria.add(Property.forName("manufacture").in(farmerCriteria));
			}
			
			if(entity.getIngredient() !=null && !"".equals(entity.getIngredient()))
				criteria.add(Restrictions.like("ingredient", entity.getIngredient(),MatchMode.ANYWHERE));
			
			String dir = (String) params.get(DIR);
			String sort = (String) params.get(SORT_COLUMN);
			if (dir != null && sort != null) {
				if (dir.equals(DESCENDING)) {
					criteria.addOrder(Order.desc(sort));
				} else {
					criteria.addOrder(Order.asc(sort));
				}
			}
		}
	}

}
