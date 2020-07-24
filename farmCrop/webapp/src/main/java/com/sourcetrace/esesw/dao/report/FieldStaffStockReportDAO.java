package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.entity.WarehouseProduct;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class FieldStaffStockReportDAO extends ReportDAO {
	@Override
	protected void addExampleFiltering(Criteria criteria, Map params) {
		WarehouseProduct entity = (WarehouseProduct) params.get(FILTER);
		if (!ObjectUtil.isEmpty(entity)) {
			criteria.createAlias("product", "p").createAlias("agent", "ag").createAlias("ag.personalInfo", "pi")
					.createAlias("p.subcategory", "sc");
			criteria.add(Restrictions.isNotNull("agent"));
			criteria.add(Restrictions.gt("stock", 0D));
			
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
				Object object = httpSession.getAttribute(ISecurityFilter.MAPPED_BRANCHES);
				if (object != null) {
					String[] branches = object.toString().split(",");

					if (branches != null && branches.length > 0) {
						criteria.add(Restrictions.in("branchId", branches));
					}
				}
			} else {
				Object object = httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
				String currentBranch = ObjectUtil.isEmpty(object) ? "" : object.toString();
				if (!StringUtil.isEmpty(currentBranch)) {
					criteria.add(Restrictions.like("branchId", currentBranch, MatchMode.EXACT));
				}
			}

			if (entity.getAgent()!=null && !ObjectUtil.isEmpty(entity.getAgent()) && !StringUtil.isEmpty(entity.getAgent().getId())) {
				criteria.add(Restrictions.eq("ag.id", entity.getAgent().getId()));
			}

			if (entity.getProduct() !=null && !ObjectUtil.isEmpty(entity.getProduct()) && !StringUtil.isEmpty(entity.getProduct().getCode())) {
				criteria.add(Restrictions.eq("p.code", entity.getProduct().getCode()));
			}
			if (entity.getProduct() !=null && !ObjectUtil.isEmpty(entity.getProduct()) && !StringUtil.isEmpty(entity.getProduct().getUnit())) {
				criteria.add(Restrictions.eq("p.unit", entity.getProduct().getUnit()));
			}
			if (!StringUtil.isEmpty(entity.getBranchId())) {
				criteria.add(Restrictions.like("branchId", entity.getBranchId(), MatchMode.EXACT));
			}
			
			if (!ObjectUtil.isListEmpty(entity.getBranchesList())) {
				criteria.add(Restrictions.in("branchId", entity.getBranchesList()));
			}

			if (!StringUtil.isEmpty(entity.getSeasonCode())) {
				criteria.add(Restrictions.like("seasonCode", entity.getSeasonCode(), MatchMode.EXACT));
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
