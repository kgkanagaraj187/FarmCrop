/*
 * CropYieldReportDAO.java
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
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.txn.agrocert.CropYield;
import com.sourcetrace.eses.txn.agrocert.CropYieldDetail;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class CropYieldReport1DAO extends ReportDAO {

    @Override
    protected Criteria createCriteria(Map params) {

        Object filter = params.get(FILTER);
        Criteria criteria = super.createCriteria(params);
        if (!ObjectUtil.isEmpty(filter)) {
            if (filter instanceof CropYieldDetail) {
                criteria = getHibernateTemplate().getSessionFactory().getCurrentSession()
                        .createCriteria(CropYieldDetail.class.getName());
            }
        }
        return criteria;
    }

    protected void addExampleFiltering(Criteria criteria, Map params) {

        Object filter = params.get(FILTER);
        if (!ObjectUtil.isEmpty(filter)) {
            if (filter instanceof CropYield) {
                criteria.createAlias("season", "s");
                criteria.createAlias("farm", "f");
                CropYield cropYield = (CropYield) filter;
                if (!ObjectUtil.isEmpty(cropYield.getFarm()) && cropYield.getFarm().getId() > 0) {
                    criteria.add(Restrictions.eq("f.id", cropYield.getFarm().getId()));
                }

                if (!ObjectUtil.isEmpty(cropYield.getSeason())
                        && !StringUtil.isEmpty(cropYield.getSeason().getName())) {
                    criteria.add(Restrictions.like("s.name", cropYield.getSeason().getName(),
                            MatchMode.ANYWHERE));
                }

                if (!StringUtil.isEmpty(cropYield.getLandHolding())) {
                    criteria.add(Restrictions.like("landHolding", cropYield.getLandHolding(),
                            MatchMode.ANYWHERE));
                }

                if (!StringUtil.isEmpty(cropYield.getLatitude())) {
                    criteria.add(Restrictions.like("latitude", cropYield.getLatitude(),
                            MatchMode.ANYWHERE));
                }

                if (!StringUtil.isEmpty(cropYield.getLongitude())) {
                    criteria.add(Restrictions.like("longitude", cropYield.getLongitude(),
                            MatchMode.ANYWHERE));
                }

            } else if (filter instanceof CropYieldDetail) {
                criteria.createAlias("cropYield", "cYield");
                CropYieldDetail cropYieldDetail = (CropYieldDetail) filter;
                if (!ObjectUtil.isEmpty(cropYieldDetail.getCropYield())
                        && cropYieldDetail.getCropYield().getId() > 0) {
                    criteria.add(Restrictions.eq("cYield.id", cropYieldDetail.getCropYield()
                            .getId()));
                }
            }
        }
    }

}
