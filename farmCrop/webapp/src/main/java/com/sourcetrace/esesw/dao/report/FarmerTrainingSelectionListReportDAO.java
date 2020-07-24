package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.ese.entity.txn.training.FarmerTraining;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class FarmerTrainingSelectionListReportDAO extends ReportDAO {

    protected void addExampleFiltering(Criteria criteria, Map params) {

        // check for filter entity
        FarmerTraining entity = (FarmerTraining) params.get(FILTER);

        if (entity != null) {
            
            criteria.createAlias("trainingTopic", "tt");
            //criteria.createAlias("targetGroup", "tg");
            if (!StringUtil.isEmpty(entity.getBranchId())) {
				criteria.add(Restrictions.like("branchId", entity.getBranchId(), MatchMode.EXACT));
			}
            if (entity.getCode() != null && !"".equals(entity.getCode()))
                criteria.add(Restrictions.like("code", entity.getCode(),
                        MatchMode.ANYWHERE));

            if (!ObjectUtil.isEmpty(entity.getTrainingTopic())
                    && !StringUtil.isEmpty(entity.getTrainingTopic().getName()))
                criteria.add(Restrictions.like("tt.name", entity.getTrainingTopic()
                        .getName(), MatchMode.ANYWHERE));
            
     /*       if (entity.getFilterStatus() != null && !"".equals(entity.getFilterStatus())) {
                if (entity.getFilterStatus().equals("status")) {
                    if(entity.getStatus() == FarmerTraining.ACTIVE){
                        criteria.add(Restrictions.in("status", new Object[] {
                                FarmerTraining.ACTIVE, FarmerTraining.INACTIVE }));
                    }
                    else
                        criteria.add(Restrictions.like("status", entity.getStatus()));
                } 
            }*/
            
            if(!StringUtil.isEmpty(entity.getStatus()) && entity.getStatus()!=0 )
            {
            	
            	criteria.add(Restrictions.eq("status", entity.getStatus()));
            }
            
            if(!StringUtil.isEmpty(entity.getSelectionType()))
            {
            	
            	criteria.add(Restrictions.eq("selectionType", entity.getSelectionType()));
            }

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
