/*
 * FarmListReportDAO.java
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
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Farmer;



public class FarmListReportDAO extends ReportDAO {

    protected void addExampleFiltering(Criteria criteria, Map params) {

        // check for filter entity
        Farm entity = (Farm) params.get(FILTER);
        if (entity != null) {

            criteria.createAlias("farmer", "f");
            criteria.createAlias("farmDetailedInfo", "fdi");
            criteria.createAlias("f.village", "v");
         
            if (!StringUtil.isEmpty(entity.getFarmCode()))
                criteria.add(Restrictions.like("farmCode", entity.getFarmCode(), MatchMode.ANYWHERE));

            if (!StringUtil.isEmpty(entity.getFarmName()))
                criteria.add(Restrictions.like("farmName", entity.getFarmName(), MatchMode.ANYWHERE));
            
            
            if (!ObjectUtil.isEmpty(entity.getFarmDetailedInfo()) && !StringUtil.isEmpty(entity.getFarmDetailedInfo().getRegYear()))
             criteria.add(Restrictions.like("fdi.regYear", entity.getFarmDetailedInfo().getRegYear(), MatchMode.ANYWHERE));
            
            if (!ObjectUtil.isEmpty(entity.getFarmDetailedInfo()) && !StringUtil.isEmpty(entity.getFarmDetailedInfo().getSurveyNumber()))
                criteria.add(Restrictions.like("fdi.surveyNumber", entity.getFarmDetailedInfo().getSurveyNumber(), MatchMode.ANYWHERE));
            
           
            criteria.add(Restrictions.in("status", new Integer[]{Farmer.Status.ACTIVE.ordinal(),Farmer.Status.INACTIVE.ordinal()}));
            
            
            if (!ObjectUtil.isEmpty(entity.getFarmer()) && entity.getFarmer().getId()!=0)
                criteria.add(Restrictions.eq("f.id", entity.getFarmer().getId()));
            
            if (!ObjectUtil.isEmpty(entity.getFarmer()) && !StringUtil.isEmpty(entity.getFarmer().getFirstName()))
                criteria.add(Restrictions.like("f.firstName", entity.getFarmer().getFirstName(), MatchMode.ANYWHERE));

            if (!StringUtil.isEmpty(entity.getHectares()))
                criteria.add(Restrictions.like("hectares", entity.getHectares(), MatchMode.ANYWHERE));

            if (!StringUtil.isEmpty(entity.getLandInProduction()))
                criteria.add(Restrictions.like("landInProduction", entity.getLandInProduction(), MatchMode.ANYWHERE));

            if (!StringUtil.isEmpty(entity.getLandNotInProduction()))
                criteria.add(Restrictions.like("landNotInProduction", entity.getLandNotInProduction(), MatchMode.ANYWHERE));
            
            if (!ObjectUtil.isEmpty(entity.getFarmer()) && !StringUtil.isEmpty(entity.getFarmer().getFarmerCode()))
                criteria.add(Restrictions.like("f.farmerCode", entity.getFarmer().getFarmerCode(), MatchMode.ANYWHERE));
            
            if (!ObjectUtil.isEmpty(entity.getFarmer()) && !StringUtil.isEmpty(entity.getFarmer().getVillage()))
                criteria.add(Restrictions.like("v.name", entity.getFarmer().getVillage().getName(), MatchMode.ANYWHERE)); 

            
            if (!StringUtil.isEmpty(entity.getLocalNameOfCrotenTree())) {
            	 criteria.createAlias("farmICSConversion", "ics");
                criteria.add(Restrictions.like("ics.inspectorName", entity.getLocalNameOfCrotenTree()));
            }
            
            // sorting direction
            String dir = (String) params.get(DIR);
            // sorting column
            String sort = (String) params.get(SORT_COLUMN);
            if (dir.equals(DESCENDING)) {
                // sort descending
                criteria.addOrder(Order.desc(sort));
            } else {
                // sort ascending
                criteria.addOrder(Order.asc(sort));
            }
        }
    }
}
