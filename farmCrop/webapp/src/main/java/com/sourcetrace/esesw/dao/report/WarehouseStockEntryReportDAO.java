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
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.WarehousePaymentDetails;

@SuppressWarnings("unchecked")
public class WarehouseStockEntryReportDAO extends ReportDAO {

	private SimpleDateFormat DataBaseFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	protected void addExampleFiltering(Criteria criteria, Map params) {

		WarehousePaymentDetails entity = (WarehousePaymentDetails) params.get(FILTER);
		System.out.println("WarehousePaymentDetails" + entity);
		if (entity != null) {
			criteria.createAlias("warehousePayment", "wpa").createAlias("wpa.warehouse", "w").createAlias("wpa.vendor",
					"vr");
		}
		if (entity.getWarehousePayment() != null) {

			if (!StringUtil.isEmpty(entity.getWarehousePayment().getReceiptNo()))
				criteria.add(
						Restrictions.like("receiptNo", entity.getWarehousePayment().getReceiptNo(), MatchMode.EXACT));

			if (!ObjectUtil.isEmpty(entity.getWarehousePayment().getWarehouse())) {
				criteria.add(Restrictions.like("w.code", entity.getWarehousePayment().getWarehouse().getCode().trim(),
						MatchMode.EXACT));
			}
			if (!StringUtil.isEmpty(entity.getWarehousePayment().getOrderNo())) {
				criteria.add(Restrictions.like("wpa.orderNo", entity.getWarehousePayment().getOrderNo().trim(),
						MatchMode.EXACT));
			}
			if (!ObjectUtil.isEmpty(entity.getWarehousePayment().getVendor())) {
				criteria.add(Restrictions.like("vr.vendorId",
						entity.getWarehousePayment().getVendor().getVendorId().trim(), MatchMode.EXACT));
			}

			if (!StringUtil.isEmpty(entity.getWarehousePayment().getSeasonCode())) {
				criteria.add(Restrictions.like("wpa.seasonCode", entity.getWarehousePayment().getSeasonCode(),
						MatchMode.EXACT));
			}
		}
		if (!StringUtil.isEmpty(entity.getReceiptNo()))
			criteria.add(Restrictions.like("receiptNo", entity.getReceiptNo(), MatchMode.EXACT));

		if (!ObjectUtil.isListEmpty(entity.getBranchesList())) {
			criteria.add(Restrictions.in("branchId", entity.getBranchesList()));
		}

		if (!StringUtil.isEmpty(entity.getBranchId())) {
			criteria.add(Restrictions.eq("branchId", entity.getBranchId()));
		}
		
		if (!ObjectUtil.isEmpty(entity.getProduct())&&!StringUtil.isEmpty(entity.getProduct().getId())) {
			criteria.createAlias("product", "p");
			criteria.add(Restrictions.eq("p.id", entity.getProduct().getId()));
		}

		// Sorting Direction
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
