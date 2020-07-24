/*
 * CropSummaryReportDAO.java
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

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.esesw.entity.profile.FarmCrops;

public class CropYieldVillageReportDAO extends ReportDAO {

    protected void addExampleFiltering(Criteria criteria, Map params) {

        FarmCrops entity = (FarmCrops) params.get(FILTER);
    }
}
