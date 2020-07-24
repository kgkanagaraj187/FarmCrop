/*
 * FieldStaffManagementReportDAO.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.dao.report;

import java.text.SimpleDateFormat;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.entity.WarehouseProduct;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

@SuppressWarnings("unchecked")
public class WarehouseStockReportDAO extends ReportDAO {

	private SimpleDateFormat DataBaseFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	protected void addExampleFiltering(Criteria criteria, Map params) {

		WarehouseProduct entity = (WarehouseProduct) params.get(FILTER);
		if (entity != null) {
			criteria.createAlias("warehouse", "w").createAlias("product", "p").createAlias("p.subcategory", "sc");
			//criteria.createAlias("warehouseDetails","wd");
			
			if (!ObjectUtil.isEmpty(entity.getWarehouse())) {

				criteria.add(Restrictions.eq("w.typez",Warehouse.COOPERATIVE ));

				if (!StringUtil.isEmpty(entity.getWarehouse().getCode())) {
					criteria.add(Restrictions.like("w.code", entity.getWarehouse().getCode(), MatchMode.EXACT));

				}
				if (!ObjectUtil.isEmpty(entity.getProduct()) && !StringUtil.isEmpty(entity.getProduct().getCode())) {
					criteria.add(Restrictions.like("p.code", entity.getProduct().getCode(), MatchMode.EXACT));

				}

				if (!ObjectUtil.isListEmpty(entity.getBranchesList())) {
					criteria.add(Restrictions.in("branchId", entity.getBranchesList()));
				}

				if (!StringUtil.isEmpty(entity.getBranchId())) {
					criteria.add(Restrictions.eq("branchId", entity.getBranchId()));
				}

				if (!StringUtil.isEmpty(entity.getSeasonCode())) {
					criteria.add(Restrictions.like("seasonCode", entity.getSeasonCode(), MatchMode.EXACT));
				}
				if (entity.getWarehouse().getName() != null) {
					String name = entity.getWarehouse().getName();
					if (name != null && !"".equals(name)) {
						criteria.add(Restrictions.like("w.name", name, MatchMode.ANYWHERE));
					}
				}
				if(!ObjectUtil.isEmpty(entity.getProduct()) && !ObjectUtil.isEmpty(entity.getProduct().getSubcategory()) && !StringUtil.isEmpty(entity.getProduct().getSubcategory().getName())){
					String name=entity.getProduct().getSubcategory().getName();
					if(!StringUtil.isEmpty(name)){
						criteria.add(Restrictions.like("sc.name",name,MatchMode.ANYWHERE));
					}
				}
				if(!ObjectUtil.isEmpty(entity.getProduct())&& !StringUtil.isEmpty(entity.getProduct().getName())){
					String name=entity.getProduct().getName();
					if(!StringUtil.isEmpty(name)){
						criteria.add(Restrictions.like("p.name",name,MatchMode.ANYWHERE));
					}
				}
			}

		}

		// sorting direction

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
