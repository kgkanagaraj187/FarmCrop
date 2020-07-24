/*
 * FieldStaffManagementReportDAO.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.dao.report;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.entity.LocationHistory;
import com.sourcetrace.eses.entity.LocationHistoryDetail;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;

@SuppressWarnings("unchecked")
public class FieldStaffManagementReportDAO extends ReportDAO {

	private SimpleDateFormat DataBaseFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private IPreferencesService preferncesService;
	@Override
	protected void addExampleFiltering(Criteria criteria, Map params) {
		String Accuracy =preferncesService.findPrefernceByName(ESESystem.IS_ACCURACY_VALUE);
		Object object = (Object) params.get(FILTER);
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof LocationHistory) {
				LocationHistory locationHistory = (LocationHistory) object;
				if (!ObjectUtil.isEmpty(locationHistory)) {

					// criteria.setProjection(Projections.projectionList().add(Projections.property("id")).add(Projections.property("branchId"))
					// .add(Projections.sqlGroupProjection("date(TXN_TIME) as
					// txnTime", "date(TXN_TIME)",
					// new String[] { "txnTime" }, new Type[] {
					// StandardBasicTypes.DATE }))
					// .add(Projections.groupProperty("agentId")));

					
					HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
		            HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
		    		String tenantId = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID) ? ISecurityFilter.DEFAULT_TENANT_ID
		    				: "";
		    		
		    		Object object2 = httpSession.getAttribute(ISecurityFilter.IS_MULTI_BRANCH_APP);
		    		String multibranch = ObjectUtil.isEmpty(object2) ? "" : object2.toString();
		    		
		    		if (!ObjectUtil.isEmpty(request)) {
		    				tenantId = !StringUtil.isEmpty((String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID))
		    					? (String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID) : "";
		    		}
		    		
		    		if (multibranch.equals("1")) {
		    			Object object1 = httpSession.getAttribute(ISecurityFilter.MAPPED_BRANCHES);
		    			if (object1 != null) {
		    				String[] branches = object1.toString().split(",");

		    				if (branches != null && branches.length > 0) {
		    					criteria.add(Restrictions.in("branchId", branches));
		    				}
		    			}
		    		} else {
		    			Object object3= httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
		    			String currentBranch1 = ObjectUtil.isEmpty(object3) ? "" : object3.toString();
		    			if (!StringUtil.isEmpty(currentBranch1)) {
		    				criteria.add(Restrictions.like("branchId", currentBranch1, MatchMode.EXACT));
		    			}
		    		}
		    		
		    		
					if (!ObjectUtil.isEmpty(locationHistory.getTxnTime()))
						criteria.add(Restrictions.ge("txnTime", locationHistory.getTxnTime()));
					
					if (!ObjectUtil.isListEmpty(locationHistory.getBranchesList())) {
						criteria.add(Restrictions.in("branchId", locationHistory.getBranchesList()));
					}
					
					if (!StringUtil.isEmpty(locationHistory.getBranchId())){
					    criteria.add(Restrictions.eq("branchId", locationHistory.getBranchId()));
					}

					String agentId = locationHistory.getAgentId();
					if (!StringUtil.isEmpty(agentId)) {
						criteria.add(Restrictions.like("agentId", agentId.trim(), MatchMode.EXACT));
					}
					
					/*if (!StringUtil.isEmpty(locationHistory.getAccuracy())){
						criteria.add(Restrictions.lt("accuracy",new Long(Accuracy)));
					}*/
				//	criteria.add(Restrictions.ne("longitude",""));
					//criteria.add(Restrictions.ne("latitude",""));
					
				}

			}

		}
	}

	@Override
	protected void addProjections(Criteria criteria, Map params) {

	}

	/*@Override
	protected void addGrouping(Map params, ProjectionList list) {
		list.add(Projections.sqlGroupProjection("agent_id as txnTime", "agent_id",
				new String[] { "txnTime" }, new Type[] { StandardBasicTypes.STRING }));
	}*/

	@Override
	protected void addPropertyProjections(Criteria criteria, Map params) {
		// TODO Auto-generated method stub
		ProjectionList p1 = Projections.projectionList();
		p1.add(Projections.property("id"));
		p1.add(Projections.property("branchId"));
		p1.add(Projections.property("txnTime"));
	//	p1.add(Projections.property("agentId"));
		p1.add(Projections.groupProperty("agentId"));
		//p1.add(Projections.groupProperty("txnTime"));
		p1.add(Projections.sqlGroupProjection("date(TXN_TIME) as txnTime", "date(TXN_TIME)", new String[] { "txnTime" },
				new Type[] { StandardBasicTypes.DATE }));
		p1.add(Projections.property("latitude"));
		p1.add(Projections.property("longitude"));
		p1.add(Projections.property("endlatitude"));
		p1.add(Projections.property("endlongitude"));
		p1.add(Projections.property("startAddress"));
		p1.add(Projections.property("endAddress"));
		criteria.setProjection(p1);
	}

	@Override
	protected Object getRowCount(Map params) {

		// create the criteria for the given entity type
		Criteria criteria = createCriteria(params);
		ProjectionList list = Projections.projectionList();
		
		// to get row count
		list.add(Projections.rowCount());
		list.add(Projections.sqlGroupProjection("date(TXN_TIME) as txnTime", "date(TXN_TIME)", new String[] { "txnTime" },
				new Type[] { StandardBasicTypes.DATE }));
		list.add(Projections.groupProperty("agentId"));
		addGrouping(params, list);
		criteria.setProjection(list);
		// apply the filters, if any
		// add example filtering
		addExampleFiltering(criteria, params);
		// add date range filtering
		addDateRangeFiltering(criteria, params);
		// total row count
		List results = criteria.list();
	      return results.size();
		// } else {
		// if (results.get(0) instanceof Long) {
		// return ObjectUtil.isListEmpty(results) ? 0 : (((Long)
		// results.get(0))).intValue();
		// } else if (results.get(0) instanceof Integer) {
		// return ObjectUtil.isListEmpty(results) ? 0 : ((Integer)
		// results.get(0));
		// }
		// return 0;
		// }
	}

}
