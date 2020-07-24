/*
 * TrainingCompletionReportDAO.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.dao.report;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.txn.agrocert.FarmerCropProdAnswers;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
public class FarmerInspectionIcsReportDAO extends ReportDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.ReportDAO#addExampleFiltering(org.hibernate.Criteria,
	 * java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void addExampleFiltering(Criteria criteria, Map params) {{

			FarmerCropProdAnswers entity = (FarmerCropProdAnswers) params
					.get(FILTER);

			if (entity != null) {
				HttpSession httpSession = ReflectUtil.getCurrentHttpSession();

				Object object = httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
				String currentBranch = ObjectUtil.isEmpty(object) ? "" : object.toString();
				
			    Date from = (Date) params.get(FROM_DATE);
				// to date
				Date to = (Date) params.get(TO_DATE);
				
			criteria.add(Restrictions.between("answeredDate", from, to));
				criteria.add(Restrictions.like("categoryCode", entity
						.getCategoryCode(), MatchMode.EXACT));
				if(!StringUtil.isEmpty(currentBranch))
					criteria.add(Restrictions.eq("branchId",currentBranch));
				
				if (!StringUtil.isEmpty(entity.getFarmerId())) {
					criteria.add(          Restrictions.like("farmerId",
							entity.getFarmerId(), MatchMode.EXACT));
				}
				
				if(!StringUtil.isEmpty(entity.getFarmId())){
	                criteria.add(Restrictions.like("farmId",
	                        entity.getFarmId().trim(), MatchMode.EXACT));
	            }
				
				if (!ObjectUtil.isListEmpty(entity.getBranchesList())) {
					criteria.add(Restrictions.in("branchId", entity.getBranchesList()));
				}
				
				if (!StringUtil.isEmpty(entity.getBranchId())){
				    criteria.add(Restrictions.eq("branchId", entity.getBranchId()));
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

}

