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
import com.sourcetrace.esesw.entity.profile.WarehouseStockReturnDetails;

public class WarehouseStockReturnReportDAO extends ReportDAO {
	
	 private SimpleDateFormat DataBaseFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	    @Override
	    protected void addExampleFiltering(Criteria criteria, Map params) {

	    	WarehouseStockReturnDetails entity = (WarehouseStockReturnDetails) params.get(FILTER);

	        if (entity != null) {
	            criteria.createAlias("warehouseStockReturn", "wsr").createAlias("wsr.warehouse", "w")
	                    .createAlias("wsr.vendor", "vr");
	        }
	        if (entity.getWarehouseStockReturn() != null) {


	            if (!ObjectUtil.isEmpty(entity.getWarehouseStockReturn().getWarehouse())) {
	                criteria.add(Restrictions.like("w.code", entity.getWarehouseStockReturn()
	                        .getWarehouse().getCode().trim(), MatchMode.EXACT));
	            }

	            if (!StringUtil.isEmpty(entity.getWarehouseStockReturn().getOrderNo()))
	            {
	                criteria.add(Restrictions.like("wsr.orderNo", entity.getWarehouseStockReturn()
	                        .getOrderNo().trim(), MatchMode.EXACT));
	            }
	            if (!ObjectUtil.isEmpty(entity.getWarehouseStockReturn().getVendor()))
	            {
	                criteria.add(Restrictions.like("vr.vendorId", entity.getWarehouseStockReturn()
	                        .getVendor().getVendorId().trim(), MatchMode.EXACT));
	            }
	            
	            if (!StringUtil.isEmpty(entity.getWarehouseStockReturn().getReturnType()))
		        {
		            criteria.add(Restrictions.like("wsr.returnType", entity.getWarehouseStockReturn().getReturnType(), MatchMode.EXACT));
		        } 
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
