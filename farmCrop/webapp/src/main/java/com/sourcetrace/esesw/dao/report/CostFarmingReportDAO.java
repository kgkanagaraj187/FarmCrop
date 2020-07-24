package com.sourcetrace.esesw.dao.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.order.entity.txn.CostFarming;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Country;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.ResearchStation;

public class CostFarmingReportDAO extends ReportDAO{
	
	private SimpleDateFormat DataBaseFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	protected void addExampleFiltering(Criteria criteria, Map params) {
		// check for filter entity
		CostFarming entity = (CostFarming) params.get(FILTER);
		criteria.createAlias("cow", "c");
		

		if (entity != null) {

			if (entity.getCollectionDate() != null && !"".equals(entity.getCollectionDate())) {
				criteria.add(Restrictions.like("collectionDate", DataBaseFormat.format(entity.getCollectionDate()),
						MatchMode.ANYWHERE));
			}
			if (!StringUtil.isEmpty(entity.getBranchId())) {
				criteria.add(Restrictions.like("branchId", entity.getBranchId(), MatchMode.EXACT));
			}

			if (!StringUtil.isEmpty(entity.getElitType())) {
				criteria.add(Restrictions.eq("elitType", entity.getElitType()));
			}

			if (entity.getResearchStationId() != null && !"".equals(entity.getResearchStationId()))
				criteria.add(Restrictions.like("researchStationId", entity.getResearchStationId(), MatchMode.ANYWHERE));

			if (!ObjectUtil.isEmpty(entity.getCow().getFarm().getFarmer()) && !StringUtil.isEmpty(entity.getCow().getFarm().getFarmer().getFirstName()))
			{
				criteria.createAlias("c.farm", "fa");
				criteria.createAlias("fa.farmer", "f");
				
				criteria.add(Restrictions.like("f.firstName", entity.getCow().getFarm().getFarmer().getFirstName(), MatchMode.ANYWHERE));
			}	
			
			 if (!ObjectUtil.isEmpty(entity.getCow()) &&
			  !StringUtil.isEmpty(entity.getCow().getCowId()))
			 criteria.add(Restrictions.eq("c.id",entity.getCow().getId()));
			 

			if (entity.getReceiptNo() != null && !"".equals(entity.getReceiptNo()))
				criteria.add(Restrictions.like("receiptNo", entity.getReceiptNo(), MatchMode.ANYWHERE));

			// sorting direction
			String dir = (String) params.get(DIR);
			// sorting column
			String sort = (String) params.get(SORT_COLUMN);
			// sort a column in the given direction ascending/descending
			if (dir != null && sort != null) {
				Criteria sortCriteria = null;
				if (sort.contains(DELIMITER)) {
					sort = sort.substring(sort.lastIndexOf(DELIMITER) + 1);
					if (dir.equals(DESCENDING)) {
						// sort descending
						sortCriteria.addOrder(Order.desc(sort));
					} else {
						// sort ascending
						sortCriteria.addOrder(Order.asc(sort));
					}
				}
			}
		}
	}

		
	}


