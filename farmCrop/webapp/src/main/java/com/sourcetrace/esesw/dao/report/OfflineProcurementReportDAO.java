package com.sourcetrace.esesw.dao.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.order.entity.txn.OfflineProcurement;
import com.sourcetrace.eses.order.entity.txn.OfflineProcurementDetail;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class OfflineProcurementReportDAO extends ReportDAO{

private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	protected Criteria createCriteria(Map params) {

		String entity = (String) params.get(ENTITY);		
		Object object = (Object) params.get(FILTER);
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof OfflineProcurementDetail) {
				entity = OfflineProcurementDetail.class.getName();
			}
		}

		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(entity);

		return criteria;
	}
		
	protected void addExampleFiltering(Criteria criteria, Map params) {
		Object object = (Object) params.get(FILTER);
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof OfflineProcurement) {				
				OfflineProcurement offlineProcurement = (OfflineProcurement) object;
				
				// from date
				Date from = (Date) params.get(FROM_DATE);
				// to date
				Date to = (Date) params.get(TO_DATE);
				
				if(!StringUtil.isEmpty(from.toString()) && !StringUtil.isEmpty(to.toString()))
				criteria.add(Restrictions.between("procurementDate",sdf.format(from), sdf.format(to)));
				
				criteria.add(Restrictions.ne("statusCode", 0));

				
				if(!StringUtil.isEmpty(offlineProcurement.getType())){
					criteria.add(Restrictions.like("type", offlineProcurement.getType().trim(),MatchMode.ANYWHERE));
				}
				
				if(!StringUtil.isEmpty(offlineProcurement.getReceiptNo())){
					criteria.add(Restrictions.like("receiptNo", offlineProcurement.getReceiptNo().trim(),MatchMode.EXACT));
				}
				if(!StringUtil.isEmpty(offlineProcurement.getFarmerId())){
					criteria.add(Restrictions.like("farmerId", offlineProcurement.getFarmerId().trim(),MatchMode.ANYWHERE));
				}
				
				
				
				if(offlineProcurement.getStatusCode() > 0){
					criteria.add(Restrictions.eq("statusCode", offlineProcurement.getStatusCode()));
				}
				
			}else if(object instanceof OfflineProcurementDetail){
				
				OfflineProcurementDetail offlineProcurementDetail = (OfflineProcurementDetail) object;
				
				criteria.createAlias("offlineProcurement", "op",Criteria.LEFT_JOIN);
				
				if(!ObjectUtil.isEmpty(offlineProcurementDetail.getOfflineProcurement()))
					criteria.add(Restrictions.eq("op.id", offlineProcurementDetail.getOfflineProcurement().getId()));
				
			}
			
		}	
		  
	}	
	
	 protected void addDateRangeFiltering(Criteria criteria, Map params) {

	      
	    }
	    
}
