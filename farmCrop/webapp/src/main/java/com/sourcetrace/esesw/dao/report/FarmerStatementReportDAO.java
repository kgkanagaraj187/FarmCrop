package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.ese.entity.util.LoanLedger;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Farmer;

public class FarmerStatementReportDAO extends ReportDAO {

	
	protected void addExampleFiltering(Criteria criteria, Map params) {
		Object obj = (Object) params.get(FILTER);
		
		if (obj != null) {
			if (obj instanceof ESEAccount) {
				
				ESEAccount entity = (ESEAccount) obj;


				if (!ObjectUtil.isEmpty(entity) && entity.getId() == 0) {				
						criteria.add(Restrictions.eq("type", 3));


				}
				if (!ObjectUtil.isEmpty(entity) && !StringUtil.isEmpty(entity.getProfileId())) {
					criteria.add(Restrictions.like("profileId", entity.getProfileId()));
				}
				
				if (!ObjectUtil.isEmpty(entity) && !StringUtil.isEmpty(entity.getProfileId())) {
					criteria.add(Restrictions.like("profileId", entity.getProfileId()));
				}
				
				if(!ObjectUtil.isEmpty(entity) && !StringUtil.isEmpty(entity.getBranchId())){
					criteria.add(Restrictions.eq("branchId", entity.getBranchId()));
				}
				
				Criterion rest1= Restrictions.gt("loanAmount",0.00);
				Criterion rest2= Restrictions.gt("cashBalance",0.0);
				
				if (!ObjectUtil.isEmpty(entity)) {
					criteria.add(Restrictions.or(rest1,rest2));
				}
				/*if (!ObjectUtil.isEmpty(entity)) {
					criteria.add(Restrictions.gt("cashBalance",0.0));
				}*/

				if (!ObjectUtil.isEmpty(entity) && !StringUtil.isEmpty(entity.getVillage()) && StringUtil.isLong(entity.getVillage())) {
					DetachedCriteria subquery = DetachedCriteria.forClass(Farmer.class);
					subquery.createAlias("village", "v");
					subquery.add(Restrictions.eq("v.id",Long.valueOf(entity.getVillage())));
				   	subquery.setProjection(Property.forName("farmerId"));
	            	  criteria.add(Property.forName("profileId").in(subquery));
				}
				if (!ObjectUtil.isEmpty(entity) && entity.getId() > 0) {
				DetachedCriteria subquery = DetachedCriteria.forClass(LoanLedger.class);
            	subquery.add(Restrictions.eq("account.id",entity.getId()));
            	subquery.add(Restrictions.ne("txnType","701"));
            	subquery.setProjection(Property.forName("txnTime"));
            	  criteria.add(Property.forName("id").in(subquery));
				}
				if (!ObjectUtil.isEmpty(entity)) {
					DetachedCriteria subquery = DetachedCriteria.forClass(LoanLedger.class);	            	
	            	subquery.add(Restrictions.ne("txnType","701"));
	            	
	            	subquery.setProjection(Property.forName("account.id"));
	            	  criteria.add(Property.forName("id").in(subquery));
					}
			
			}/*else if(obj instanceof LoanLedger){
				LoanLedger loanLedger = (LoanLedger) obj;
				
				if (!ObjectUtil.isEmpty(loanLedger) && loanLedger.getId() == 0) {
					criteria.createAlias("account", "account", JoinType.RIGHT_OUTER_JOIN);
				
						criteria.add(Restrictions.eq("account.type", Integer.valueOf(loanLedger.getAccount().getType())));


				}
				if (!ObjectUtil.isEmpty(loanLedger) && !ObjectUtil.isEmpty(loanLedger.getAccount())) {
					criteria.add(Restrictions.eq("account.id", loanLedger.getAccount().getId()));
				}
				
				
				if(!StringUtil.isEmpty(loanLedger.getBranchId())){
					criteria.add(Restrictions.eq("branchId", loanLedger.getBranchId()));
				}
			}*/
		}
		
		
	
	}
	
	

}

