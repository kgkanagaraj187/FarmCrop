package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.ReportMailConfiguration;

public class ReportMailConfigurationReportDAO extends ReportDAO {

    protected void addExampleFiltering(Criteria criteria, Map params) {

        ReportMailConfiguration entity = (ReportMailConfiguration) params.get(FILTER);

        if (!ObjectUtil.isEmpty(entity)) {

            if (!StringUtil.isEmpty(entity.getName()))
                criteria.add(Restrictions.like("name", entity.getName(), MatchMode.ANYWHERE));

            if (!StringUtil.isEmpty(entity.getMailId()))
                criteria.add(Restrictions.like("mailId", entity.getMailId(), MatchMode.ANYWHERE));

            if (entity.getStatus() >= 0) {
                if (entity.getStatus() == ReportMailConfiguration.ACTIVE)
                    criteria.add(Restrictions.eq("status", ReportMailConfiguration.ACTIVE));
                else
                    criteria.add(Restrictions.eq("status", ReportMailConfiguration.INACTIVE));
            }

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
