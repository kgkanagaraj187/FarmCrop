package com.sourcetrace.esesw.dao.report;

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

import com.ese.entity.util.LoanLedger;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.order.entity.txn.Cultivation;
import com.sourcetrace.eses.order.entity.txn.LoanDistributionDetail;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class LoanManagementReportDAO extends ReportDAO {
	protected void addExampleFiltering(Criteria criteria, Map params) { 
		LoanLedger entity = (LoanLedger) params.get(FILTER);
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
		if (!ObjectUtil.isEmpty(entity)) {
			 if (!StringUtil.isEmpty(entity.getBranchId())) {
					criteria.add(Restrictions.like("branchId", entity.getBranchId(), MatchMode.EXACT));
				}
		}
		
	}
	
	

	
/*	@Override
	protected void addPropertyProjections(Criteria criteria, Map params) {
		// TODO Auto-generated method stub
		ProjectionList p1 = Projections.projectionList();
		//p1.add(Projections.property("id"));
		//p1.add(Projections.property("branchId"));
		p1.add(Projections.property("txnTime"));
	//	p1.add(Projections.property("agentId"));
		//p1.add(Projections.groupProperty("agentId"));
		//p1.add(Projections.groupProperty("txnTime"));
	//	p1.add(Projections.sqlGroupProjection("date(TXN_TIME) as txnTime", "date(TXN_TIME)", new String[] { "txnTime" },
			//	new Type[] { StandardBasicTypes.DATE }));
		criteria.setProjection(p1);
	}
	*/
}
