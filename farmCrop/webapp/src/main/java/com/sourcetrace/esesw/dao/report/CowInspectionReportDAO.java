package com.sourcetrace.esesw.dao.report;

import java.text.SimpleDateFormat;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.order.entity.txn.CowInspection;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class CowInspectionReportDAO extends ReportDAO
{
	  private SimpleDateFormat DataBaseFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	protected void addExampleFiltering(Criteria criteria, Map params)
	{
		CowInspection entity = (CowInspection) params.get(FILTER);
		if (entity != null) {
			
			if (!StringUtil.isEmpty(entity.getCurrentInspDate())) {
				criteria.add(Restrictions.like("currentInspDate", DataBaseFormat.format(entity.getCurrentInspDate()), MatchMode.ANYWHERE));
			}
		
			if (!StringUtil.isEmpty(entity.getBranchId())) {
			criteria.add(Restrictions.like("branchId", entity.getBranchId(), MatchMode.EXACT));
			}

		if (!StringUtil.isEmpty(entity.getCowId()))
			criteria.add(Restrictions.like("cowId", entity.getCowId(), MatchMode.ANYWHERE));
		
		
		if (!StringUtil.isEmpty(entity.getFarmerId()))
			criteria.add(Restrictions.like("farmerId", entity.getFarmerId(), MatchMode.ANYWHERE));
		
		if (!StringUtil.isEmpty(entity.getElitType()))
			criteria.add(Restrictions.eq("elitType", entity.getElitType()));
		
		
		if (!StringUtil.isEmpty(entity.getInspectionNo()))
			criteria.add(Restrictions.like("inspectionNo", entity.getInspectionNo(), MatchMode.ANYWHERE));
	
		if (!ObjectUtil.isEmpty(entity.getResearchStation()) &&entity.getResearchStation().getId()>0)
		{
			criteria.createAlias("researchStation", "rs");
			criteria.add(Restrictions.eq("rs.id", entity.getResearchStation().getId()));
		}
		

		
		String dir = (String) params.get(DIR);
		String sort = (String) params.get(SORT_COLUMN);

			if (dir != null && sort != null) {
				criteria.addOrder(Order.desc(sort));
				
			}
		}
	}

}
