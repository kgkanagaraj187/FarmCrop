package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.ese.entity.traceability.FarmerTraceability;
import com.ese.entity.traceability.GinningProcess;
import com.ese.entity.traceability.SpinningTransfer;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.txn.agrocert.CropYield;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class MoleculeProcessReportDAO extends ReportDAO {

	protected void addExampleFiltering(Criteria criteria, Map params) {
		Object obj = (Object) params.get(FILTER);
		if(obj!=null && !ObjectUtil.isEmpty(obj)){
			if(obj instanceof CropYield){
				CropYield entity=(CropYield) obj;
				
			 	if (!ObjectUtil.isListEmpty(entity.getBranchesList())) {
					criteria.add(Restrictions.in("branchId", entity.getBranchesList()));
				}
				
				if (!StringUtil.isEmpty(entity.getBranchId())){
				    criteria.add(Restrictions.eq("branchId", entity.getBranchId()));
				}
				if (!ObjectUtil.isEmpty(entity.getLandHolding())) {
					criteria.add(Restrictions.like("landHolding", entity.getLandHolding(), MatchMode.ANYWHERE));
				}


			}
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
