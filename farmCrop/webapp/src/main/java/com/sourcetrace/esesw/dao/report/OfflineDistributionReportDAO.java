package com.sourcetrace.esesw.dao.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.order.entity.txn.OfflineDistribution;
import com.sourcetrace.eses.order.entity.txn.OfflineDistributionDetail;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class OfflineDistributionReportDAO extends ReportDAO{
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	protected Criteria createCriteria(Map params) {

		String entity = (String) params.get(ENTITY);		
		Object object = (Object) params.get(FILTER);
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof OfflineDistributionDetail) {
				entity = OfflineDistributionDetail.class.getName();
			}
		}

		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(entity);

		return criteria;
	}
		
	protected void addExampleFiltering(Criteria criteria, Map params) {
		Object object = (Object) params.get(FILTER);
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof OfflineDistribution) {				
				OfflineDistribution offlineDistribution = (OfflineDistribution) object;
				
				// from date
				Date from = (Date) params.get(FROM_DATE);
				// to date
				Date to = (Date) params.get(TO_DATE);
				
				if(!StringUtil.isEmpty(from.toString()) && !StringUtil.isEmpty(to.toString()))
				criteria.add(Restrictions.between("distributionDate",sdf.format(from), sdf.format(to)));
				
				criteria.add(Restrictions.ne("statusCode", 0));

				
				if(!StringUtil.isEmpty(offlineDistribution.getReceiptNo())){
					criteria.add(Restrictions.like("receiptNo", offlineDistribution.getReceiptNo().trim(),MatchMode.EXACT));
				}
				
				if(!StringUtil.isEmpty(offlineDistribution.getFarmerId())){
					criteria.add(Restrictions.like("farmerId", offlineDistribution.getFarmerId().trim(),MatchMode.ANYWHERE));
				}
				
				if(!StringUtil.isEmpty(offlineDistribution.getWarehouseCode())){
					criteria.add(Restrictions.like("warehouseCode", offlineDistribution.getWarehouseCode().trim(),MatchMode.ANYWHERE));
				}
				
				if(offlineDistribution.getStatusCode() > 0){
					criteria.add(Restrictions.eq("statusCode", offlineDistribution.getStatusCode()));
				}
				
			}else if(object instanceof OfflineDistributionDetail){
				
				OfflineDistributionDetail offlineDistributionDetail = (OfflineDistributionDetail) object;
				
				criteria.createAlias("offlineDistribution", "od",Criteria.LEFT_JOIN);
				
				if(!ObjectUtil.isEmpty(offlineDistributionDetail.getOfflineDistribution()))
					criteria.add(Restrictions.eq("od.id", offlineDistributionDetail.getOfflineDistribution().getId()));
				
			}
			
		}	
		  
	}	
	
	 protected void addDateRangeFiltering(Criteria criteria, Map params) {

	      
	    }
	    
	

}
