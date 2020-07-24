package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.ese.entity.traceability.ProcurementTraceability;
import com.ese.entity.traceability.ProcurementTraceabilityDetails;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.FarmCrops;

public class ProcurementTraceabilityReportDAO extends ReportDAO {
	protected Criteria createCriteria(Map params) {
		String entity = (String) params.get(ENTITY);
		Object object = (Object) params.get(FILTER);
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof ProcurementTraceabilityDetails) {
				entity = ProcurementTraceabilityDetails.class.getName();
			}
		}
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(entity);

		return criteria;
	}

	protected void addExampleFiltering(Criteria criteria, Map params) {

		ProcurementTraceabilityDetails entity = (ProcurementTraceabilityDetails) params.get(FILTER);

		if (entity != null) {
			//criteria.createAlias("procurementTraceability", "pt").createAlias("pt.farmer", "fr").createAlias("pt.warehouse", "w");
		
			//criteria.createAlias("procurementTraceability", "pt").createAlias("pt.farmer", "fr");
			criteria.createAlias("procurementTraceability", "pt").createAlias("pt.farmer", "fr").createAlias("pt.warehouse", "w").createAlias("fr.village", "fv").createAlias("fr.city", "fc");
			criteria.createAlias("procurementProduct","pp");
			
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
				Object object3 = httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
				String currentBranch1 = ObjectUtil.isEmpty(object3) ? "" : object3.toString();
				if (!StringUtil.isEmpty(currentBranch1)) {
					criteria.add(Restrictions.like("pt.branchId", currentBranch1, MatchMode.EXACT));
				}
			}

			
			if ((!ObjectUtil.isEmpty(entity.getProcurementTraceability()))&&!StringUtil.isEmpty(entity.getProcurementTraceability().getBranchId())) {
				criteria.add(Restrictions.like("pt.branchId", entity.getProcurementTraceability().getBranchId(), MatchMode.EXACT));
			}
			if ((!ObjectUtil.isEmpty(entity.getProcurementTraceability()))&&!StringUtil.isEmpty(entity.getProcurementTraceability().getSeason())) {
				criteria.add(Restrictions.like("pt.season", entity.getProcurementTraceability().getSeason(), MatchMode.EXACT));
			}
			if (!ObjectUtil.isEmpty(entity.getProcurementTraceability())
					&& !ObjectUtil.isEmpty(entity.getProcurementTraceability().getFarmer()) && !StringUtil.isEmpty(entity.getProcurementTraceability().getFarmer().getFirstName())) {
				criteria.add(Restrictions.like("fr.farmerId",
						entity.getProcurementTraceability().getFarmer().getFirstName(), MatchMode.ANYWHERE));
			}
			if (!ObjectUtil.isEmpty(entity.getProcurementTraceability())
					&& !ObjectUtil.isEmpty(entity.getProcurementTraceability().getFarmer()) && !StringUtil.isEmpty(entity.getProcurementTraceability().getFarmer().getIcsName() )) {
				criteria.add(Restrictions.like("fr.icsName",
						entity.getProcurementTraceability().getFarmer().getIcsName(), MatchMode.ANYWHERE));
			}
			
			if(!ObjectUtil.isEmpty(entity.getProcurementTraceability()) && !ObjectUtil.isEmpty(entity.getProcurementTraceability().getFarmer()) && !ObjectUtil.isEmpty(entity.getProcurementTraceability().getFarmer().getVillage()) && !StringUtil.isEmpty(entity.getProcurementTraceability().getFarmer().getVillage().getCode())){
				criteria.add(Restrictions.eq("fv.code", entity.getProcurementTraceability().getFarmer().getVillage().getCode()));
			}
			
			if(!ObjectUtil.isEmpty(entity.getProcurementTraceability()) && !ObjectUtil.isEmpty(entity.getProcurementTraceability().getFarmer()) && !ObjectUtil.isEmpty(entity.getProcurementTraceability().getFarmer().getCity()) && !StringUtil.isEmpty(entity.getProcurementTraceability().getFarmer().getCity().getCode())){
				criteria.add(Restrictions.like("fc.code", entity.getProcurementTraceability().getFarmer().getCity().getCode()));
			}
			if(!ObjectUtil.isEmpty(entity.getProcurementTraceability()) && !ObjectUtil.isEmpty(entity.getProcurementTraceability().getFarmer()) && !StringUtil.isEmpty(entity.getProcurementTraceability().getFarmer().getFpo())){
				criteria.add(Restrictions.like("fr.fpo", entity.getProcurementTraceability().getFarmer().getFpo()));
			}
			if (!ObjectUtil.isEmpty(entity.getProcurementProduct())
					&& !StringUtil.isEmpty(entity.getProcurementProduct().getName())) {
				criteria.add(Restrictions.like("pp.name",
						entity.getProcurementProduct().getName(), MatchMode.ANYWHERE));
			}
			
			if (!ObjectUtil.isEmpty(entity.getProcurementTraceability())
					&& !ObjectUtil.isEmpty(entity.getProcurementTraceability().getWarehouse())) {
				criteria.add(Restrictions.like("w.code",
						entity.getProcurementTraceability().getWarehouse().getCode(), MatchMode.ANYWHERE));
			}
		}

		// sort a column in the given direction ascending/descending
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
