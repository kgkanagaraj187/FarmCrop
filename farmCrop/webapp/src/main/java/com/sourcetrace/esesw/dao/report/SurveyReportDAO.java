package com.sourcetrace.esesw.dao.report;

import java.util.Date;
import java.util.Map;

import javax.persistence.FetchType;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.txn.agrocert.SurveyFarmerCropProdAnswers;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class SurveyReportDAO extends ReportDAO{
	protected void addExampleFiltering(Criteria criteria, Map params) {
		SurveyFarmerCropProdAnswers entity = (SurveyFarmerCropProdAnswers) params.get(FILTER);			
		
		if (!ObjectUtil.isEmpty(entity)) {
			if (!ObjectUtil.isEmpty(entity.getFromAnsweredDate()) && !ObjectUtil.isEmpty(entity.getToAnsweredDate())) {
				Date sDate = entity.getFromAnsweredDate();
				Date eDate = entity.getToAnsweredDate();
				eDate = DateUtil.getNextDate(eDate);
				criteria.add(Restrictions.ge("answeredDate", sDate));
				criteria.add(Restrictions.le("answeredDate", eDate));
			}
			if (!StringUtil.isEmpty(entity.getFarmerId())) {
					criteria.add(Restrictions.sqlRestriction("find_in_set("+entity.getFarmerId()+",this_.farmer_id)"));
				
			
			}
			
			criteria.add(Restrictions.eq("saveStatus", SurveyFarmerCropProdAnswers.SAVE_STATUS_FULL));
			if (!StringUtil.isEmpty(entity.getSurveyCode())) {
				criteria.add(Restrictions.eq("surveyCode", entity.getSurveyCode()));
			}
			if (!StringUtil.isEmpty(entity.getBranchId())) {
				criteria.add(Restrictions.eq("branchId", entity.getBranchId()));
			}
			if (!StringUtil.isEmpty(entity.getFarmerOrgCode())) {
				criteria.add(Restrictions.eq("farmerOrgCode", entity.getFarmerOrgCode()));
			}
		}
		
		String dir = (String) params.get(DIR);
		String sort = (String) params.get(SORT_COLUMN);

		if (dir != null && sort != null) {
			criteria.addOrder(Order.desc(sort));

		}
	}
}
