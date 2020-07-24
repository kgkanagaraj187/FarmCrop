/*
 * FarmInventoryReportDAO.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.esesw.entity.profile.FarmInventory;

public class FarmInventoryReportDAO extends ReportDAO {

    protected void addExampleFiltering(Criteria criteria, Map params) {

        // check for filter entity
        FarmInventory entity = (FarmInventory) params.get(FILTER);

        if (entity != null) {

            criteria.createAlias("farmer", "f");

            if (entity.getFarmer() != null && entity.getFarmer().getId() > 0
                    && !"".equals(entity.getFarmer().getId()))
                criteria.add(Restrictions.eq("f.id", entity.getFarmer().getId()));

//            if (entity.getInventoryItem() > -1)
//                criteria.add(Restrictions.eq("inventoryItem", entity.getInventoryItem()));

            if (entity.getItemCount() != null && !"".equals(entity.getItemCount()))
                criteria.add(Restrictions.like("itemCount", entity.getItemCount(),
                        MatchMode.ANYWHERE));

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
