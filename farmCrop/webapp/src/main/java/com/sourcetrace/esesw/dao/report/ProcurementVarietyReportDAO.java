package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;

public class ProcurementVarietyReportDAO extends ReportDAO {
	protected void addExampleFiltering(Criteria criteria, Map params) {
		ProcurementVariety entity = (ProcurementVariety) params.get(FILTER);

		if (entity != null) {

            criteria.createAlias("procurementProduct", "pp");
            if (!ObjectUtil.isEmpty(entity.getCode()))
                criteria.add(Restrictions.like("code", entity.getCode(), MatchMode.ANYWHERE));
            if (!ObjectUtil.isEmpty(entity.getName()))
                criteria.add(Restrictions.like("name", entity.getName(), MatchMode.ANYWHERE));
            
           if (!ObjectUtil.isEmpty(entity.getNoDaysToGrow()))
                criteria.add(Restrictions.like("noDaysToGrow", entity.getNoDaysToGrow(), MatchMode.ANYWHERE));
           
           if (!ObjectUtil.isEmpty(entity.getYield()))
               criteria.add(Restrictions.like("yield", entity.getYield(), MatchMode.ANYWHERE));
           
           if (!ObjectUtil.isEmpty(entity.getHarvestDays()))
               criteria.add(Restrictions.like("harvestDays", entity.getHarvestDays(), MatchMode.ANYWHERE));
            
            if (!ObjectUtil.isEmpty(entity.getProcurementProduct()) && entity.getProcurementProduct().getName()!=null)
                criteria.add(Restrictions.like("pp.name", entity.getProcurementProduct().getName(), MatchMode.ANYWHERE));
            
            if (!ObjectUtil.isEmpty(entity.getProcurementProduct()) && entity.getProcurementProduct().getId()!=0)
                criteria.add(Restrictions.eq("pp.id", entity.getProcurementProduct().getId()));
            
            /*if (!ObjectUtil.isEmpty(entity.getProcurementProduct()) && entity.getProcurementProduct().getBranchId() !=null)
                criteria.add(Restrictions.eq("pp.branchId", entity.getProcurementProduct().getBranchId()));*/
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
