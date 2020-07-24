package com.sourcetrace.esesw.dao.report;


import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;

public class FarmerReportListDAO extends ReportDAO {

	protected void addExampleFiltering(Criteria criteria, Map params) {

		// check for filter entity
		Farmer entity = (Farmer) params.get(FILTER);
		if (entity != null) {
			criteria.createAlias("village", "v");
			 criteria.createAlias("samithi", "s",criteria.LEFT_JOIN);
			// criteria.createAlias("village", "v").createAlias("samithi",
			// "s").createAlias("certificateStandard", "cs",
			// Criteria.LEFT_JOIN);

			// Farmers statusCode should be 0 { 0 : SUCCESSFULL ENROLLMENT }
			criteria.add(Restrictions.eq("statusCode", ESETxnStatus.SUCCESS.ordinal()));

			if (!ObjectUtil.isListEmpty(entity.getBranchesList())) {
				criteria.add(Restrictions.in("branchId", entity.getBranchesList()));
			}
			
			if (!StringUtil.isEmpty(entity.getBranchId())){
			    criteria.add(Restrictions.eq("branchId", entity.getBranchId()));
			}

			if (entity.getFarmerId() != null)
				criteria.add(Restrictions.like("farmerId", entity.getFarmerId(), MatchMode.ANYWHERE));

			if (entity.getFirstName() != null)
				criteria.add(Restrictions.like("firstName", entity.getFirstName(), MatchMode.ANYWHERE));

			if (entity.getLastName() != null)
				criteria.add(Restrictions.like("lastName", entity.getLastName(), MatchMode.ANYWHERE));
			
			/*if (entity.getSurName() != null)
				criteria.add(Restrictions.like("surName", entity.getSurName(), MatchMode.ANYWHERE));*/

			if (entity.getVillage() != null) {
				String name = entity.getVillage().getName();
				if (name != null && !"".equals(name)) {
					criteria.add(Restrictions.like("v.name", name, MatchMode.ANYWHERE));
				}
			}

			 if (entity.getSamithi() != null) {
			 String name = entity.getSamithi().getName();
			 if (name != null && !"".equals(name)) {
			 criteria.add(Restrictions.like("s.name", name,
			 MatchMode.ANYWHERE));
			 }
			 }

			/*
			 * if(!ObjectUtil.isEmpty(entity.getCertificateStandard()) &&
			 * !StringUtil.isEmpty(entity.getCertificateStandard().getName())){
			 * criteria.add(Restrictions.like("cs.name",
			 * entity.getCertificateStandard().getName(), MatchMode.EXACT)); }
			 */

			
			 /* if(entity.getCertificationType()!=-1){
			  criteria.add(Restrictions.eq("certificationType",
			  entity.getCertificationType())); }
			 

			if (entity.getIsCertifiedFarmer() >=0) {
				criteria.add(Restrictions.eq("isCertifiedFarmer", entity.getIsCertifiedFarmer()));
			}*/
			 
			 if(!StringUtil.isEmpty(entity.getCertificationFilter())){
				 criteria.add(Restrictions.eq("isCertifiedFarmer", Integer.parseInt(entity.getCertificationFilter())));
			 }

			if (entity.getFilterStatus() != null && !"".equals(entity.getFilterStatus())) {
				if (entity.getFilterStatus().equals("status")) {
					criteria.add(Restrictions.like("status", entity.getStatus()));
				}
			}

			if (!StringUtil.isEmpty(entity.getMobileNumber())) {
				criteria.add(Restrictions.like("mobileNumber", entity.getMobileNumber(), MatchMode.ANYWHERE));
			}

			if (!StringUtil.isEmpty(entity.getSearchPage()) && entity.getSearchPage().equalsIgnoreCase("smsList")) {
				criteria.add(
						Restrictions.and(Restrictions.ne("mobileNumber", ""), Restrictions.isNotNull("mobileNumber")));
			}

			if (!StringUtil.isEmpty(entity.getFarmerCode())) {
				criteria.add(Restrictions.like("farmerCode", entity.getFarmerCode(), MatchMode.ANYWHERE));
			}

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
