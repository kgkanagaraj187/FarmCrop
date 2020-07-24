package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Cow;

public class CowReportDAO  extends ReportDAO  {
	
	protected void addExampleFiltering(Criteria criteria, Map params)
	{
		Cow entity = (Cow) params.get(FILTER);
		if (entity != null) {
		
	if (!StringUtil.isEmpty(entity.getBranchId())) {
			criteria.add(Restrictions.like("branchId", entity.getBranchId(), MatchMode.EXACT));
		}

		if (!StringUtil.isEmpty(entity.getCowId()))
			criteria.add(Restrictions.like("cowId", entity.getCowId(), MatchMode.ANYWHERE));
		
		
		if (!StringUtil.isEmpty(entity.getLactationNo()))
			criteria.add(Restrictions.like("lactationNo", entity.getLactationNo(), MatchMode.ANYWHERE));
		
		if (!StringUtil.isEmpty(entity.getTagNo()))
			criteria.add(Restrictions.like("tagNo", entity.getTagNo(), MatchMode.ANYWHERE));
		
		
		if (!StringUtil.isEmpty(entity.getDamId()))
			criteria.add(Restrictions.like("damId", entity.getDamId(), MatchMode.ANYWHERE));
		
		
		if (!StringUtil.isEmpty(entity.getGenoType()))
			criteria.add(Restrictions.like("genoType", entity.getGenoType(), MatchMode.ANYWHERE));
		
		
				
				if (!StringUtil.isEmpty(entity.getFarmerId()))
					criteria.add(Restrictions.like("farmerId", entity.getFarmerId(), MatchMode.ANYWHERE));
				
				if(!ObjectUtil.isEmpty(entity.getFarm()))
				{
					criteria.createAlias("farm", "fa");
					criteria.add(Restrictions.eq("fa.id", entity.getFarm().getId()));
				}
			
				
				if (!ObjectUtil.isEmpty(entity.getResearchStation())){
					criteria.createAlias("researchStation", "rs");
					criteria.add(Restrictions.eq("rs.id", entity.getResearchStation().getId()));
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
