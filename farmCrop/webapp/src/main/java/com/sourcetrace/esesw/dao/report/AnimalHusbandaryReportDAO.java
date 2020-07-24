/*
 * AnimalHusbandaryReportDAO.java
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
import com.sourcetrace.esesw.entity.profile.AnimalHusbandary;

public class AnimalHusbandaryReportDAO extends ReportDAO {

    protected void addExampleFiltering(Criteria criteria, Map params) {

        // check for filter entity
        AnimalHusbandary entity = (AnimalHusbandary) params.get(FILTER);

        if (entity != null) {

            criteria.createAlias("farm", "f");

            if (entity.getFarmer() != null && entity.getFarmer().getId() > 0
                    && !"".equals(entity.getFarmer().getId()))
                criteria.add(Restrictions.eq("f.id", entity.getFarmer().getId()));

//            if (entity.getFarmAnimal() > -1) {
//                criteria.add(Restrictions.eq("farmAnimal", entity.getFarmAnimal()));
//            }

            if (entity.getAnimalCount() != null && !"".equals(entity.getAnimalCount()))
                criteria.add(Restrictions.like("animalCount", entity.getAnimalCount(),
                        MatchMode.ANYWHERE));

            if (entity.getFeedingUsed() > -1) {
                criteria.add(Restrictions.eq("feedingUsed", entity.getFeedingUsed()));
            }
//
//            if (entity.getAnimalHousing() > -1) {
//                criteria.add(Restrictions.eq("animalHousing", entity.getAnimalHousing()));
//            }

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
