
package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.inspect.agrocert.Question;
import com.sourcetrace.eses.inspect.agrocert.SurveyQuestion;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Country;

public class QuestionReportDAO extends ReportDAO {

	Object object;
	
	protected Criteria createCriteria(Map params) {

		String entity = (String) params.get(ENTITY);
		object = (Object) params.get(FILTER);
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof SurveyQuestion) {
				entity = SurveyQuestion.class.getName();
			}
		}

		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(entity);

		return criteria;
	}

	
	protected void addExampleFiltering(Criteria criteria, Map params) {
		Object object = params.get(FILTER);
		if (object instanceof SurveyQuestion) {
			SurveyQuestion question = (SurveyQuestion) object;
			if (!ObjectUtil.isEmpty(question)) {
				criteria.createAlias("section", "sec", Criteria.LEFT_JOIN);
				if (!StringUtil.isEmpty(question.getCode())) {
					criteria.add(Restrictions.like("code", question.getCode(), MatchMode.ANYWHERE));
				}

				if (!StringUtil.isEmpty(question.getName())) {
					criteria.add(Restrictions.like("name", question.getName(), MatchMode.ANYWHERE));
				}

				if (!StringUtil.isEmpty(question.getSection().getName())) {
					criteria.add(Restrictions.like("sec.name", question.getSection().getName(), MatchMode.ANYWHERE));
				}
				
				

				/*if (question.getCountry() != null){
					criteria.createAlias("country", "county");
				
					criteria.add(Restrictions.like("country", question.getCountry()));
				}*/
			}
		}
	}
}
