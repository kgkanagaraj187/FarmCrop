/*
 * WarehouseProductListRepotDAO.java
 * Copyright (c) 2012-2013, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.dao.report;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.WarehousePayment;

public class WarehouseProductListRepotDAO extends ReportDAO {

    private SimpleDateFormat DataBaseFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void addExampleFiltering(Criteria criteria, Map params) {

      WarehousePayment entity = (WarehousePayment) params.get(FILTER);
      if (entity != null) {
    	  
    	  	criteria.createAlias("vendor", "v");
    	  	criteria.createAlias("warehouse", "w");
            if (!ObjectUtil.isEmpty(entity)) {
            	LocalDateTime dateBefore10Days = LocalDateTime.now().minusDays(10);
            	// if you want a java.util.Date get it back
            	Date date = Date.from(dateBefore10Days.toInstant(ZoneOffset.UTC));
            	/*DetachedCriteria crit=DetachedCriteria.forClass(WarehousePayment.class);
            	crit.setProjection(Projections.max("trxnDate"));*/
           // criteria.add(Restrictions.ge("trxnDate", date));
            if (!StringUtil.isEmpty(entity.getBranchId())){
			    criteria.add(Restrictions.eq("branchId", entity.getBranchId()));
			}            
            
            if(!StringUtil.isEmpty(entity.getOrderNo())){
            	criteria.add(Restrictions.like("orderNo", entity.getOrderNo().trim(), MatchMode.ANYWHERE));
            }
                                
            if(!StringUtil.isEmpty(entity.getReceiptNo())){
            	criteria.add(Restrictions.like("receiptNo", entity.getReceiptNo(),MatchMode.ANYWHERE));
            }
            
            if(!ObjectUtil.isEmpty(entity.getWarehouse()) && !StringUtil.isEmpty(entity.getWarehouse().getName() !=null)){
            	criteria.add(Restrictions.like("w.name", entity.getWarehouse().getName(), MatchMode.ANYWHERE));
            }
            
            if(!ObjectUtil.isEmpty(entity.getVendor()) && !StringUtil.isEmpty(entity.getVendor().getVendorName() !=null)){
            	criteria.add(Restrictions.like("v.vendorName", entity.getVendor().getVendorName(), MatchMode.ANYWHERE));
            }
            /*if(!StringUtil.isEmpty(entity.getSeasonCode())){
            	criteria.add(Restrictions.like("seasonCode", entity.getSeasonCode().trim(), MatchMode.ANYWHERE));
            }*/
            
            if(!StringUtil.isEmpty(entity.getSeasonCode())){
        	  	DetachedCriteria subquery = DetachedCriteria.forClass(HarvestSeason.class);
            	subquery.add(Restrictions.like("name",entity.getSeasonCode(),MatchMode.ANYWHERE));
            	subquery.setProjection(Property.forName("code"));
            	  criteria.add(Property.forName("seasonCode").in(subquery));
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
