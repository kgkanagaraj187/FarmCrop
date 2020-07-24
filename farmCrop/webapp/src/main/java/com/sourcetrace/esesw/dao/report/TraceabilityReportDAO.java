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
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class TraceabilityReportDAO extends ReportDAO {

	protected void addExampleFiltering(Criteria criteria, Map params) {
		Object obj = (Object) params.get(FILTER);
		if(obj!=null && !ObjectUtil.isEmpty(obj)){
			if(obj instanceof FarmerTraceability){
				FarmerTraceability entity=(FarmerTraceability) obj;
				if(entity!=null){
					if(entity.getSeason()!=null && !ObjectUtil.isEmpty(entity.getSeason())){
						criteria.add(Restrictions.eq("season", entity.getSeason()));
					}
					if(entity.getLotNo()!=null && !ObjectUtil.isEmpty(entity.getLotNo())){
						criteria.add(Restrictions.in("lotNo", entity.getLotNo().split(",")));
					}
					if(entity.getPrNo()!=null && !ObjectUtil.isEmpty(entity.getPrNo())){
						criteria.add(Restrictions.like("prNo", entity.getPrNo()));
					}
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
