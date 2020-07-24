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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.order.entity.txn.Cultivation;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.Village;

@SuppressWarnings("unchecked")
public class CultivationReportDAO extends ReportDAO {

	@SuppressWarnings("unused")
	private SimpleDateFormat DataBaseFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	protected void addExampleFiltering(Criteria criteria, Map params) {

		Cultivation entity = (Cultivation) params.get(FILTER);
		if (!ObjectUtil.isEmpty(entity)) {

			if (entity != null) {
				/*
				 * Date from = (Date) params.get(FROM_DATE); // to date Date to
				 * = (Date) params.get(TO_DATE);
				 * criteria.add(Restrictions.between("expenseDate", from, to));
				 */
				

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
	    		
				if (!StringUtil.isEmpty(entity.getExpenseDate())) {
					criteria.add(Restrictions.like("expenseDate", DataBaseFormat.format(entity.getExpenseDate()),
							MatchMode.ANYWHERE));
				}

				if (!StringUtil.isEmpty(entity.getFarmerId())) {
					criteria.add(Restrictions.like("farmerId", entity.getFarmerId(), MatchMode.ANYWHERE));
				}

				if (!StringUtil.isEmpty(entity.getFarmerName())) {
					criteria.add(Restrictions.like("farmerName", entity.getFarmerName(), MatchMode.ANYWHERE));
				}

				if (!ObjectUtil.isEmpty(entity.getFatherName())) {
					criteria.add(Restrictions.like("fatherName", entity.getFatherName().trim(), MatchMode.EXACT));
				}
				if (!ObjectUtil.isEmpty(entity.getCropCode())) {
					criteria.add(Restrictions.like("cropCode", entity.getCropCode().trim(), MatchMode.EXACT));
				}

				if (!StringUtil.isEmpty(entity.getVillageId()) && !entity.getVillageId().equalsIgnoreCase("null")) {
					criteria.add(Restrictions.like("villageId", entity.getVillageId(), MatchMode.ANYWHERE));
				}

				if (!ObjectUtil.isListEmpty(entity.getBranchesList())) {
					criteria.add(Restrictions.in("branchId", entity.getBranchesList()));
				}

				if (!StringUtil.isEmpty(entity.getBranchId())) {
					criteria.add(Restrictions.eq("branchId", entity.getBranchId()));
				}
				
				if (!StringUtil.isEmpty(entity.getFarmName())) {
					criteria.add(Restrictions.like("farmName", entity.getFarmName(),MatchMode.ANYWHERE));
				}


				if (!StringUtil.isEmpty(entity.getCurrentSeasonCode())) {
					criteria.add(
							Restrictions.like("currentSeasonCode", entity.getCurrentSeasonCode(), MatchMode.ANYWHERE));
				}
				
				if (!StringUtil.isEmpty(entity.getTxnType())) {
					criteria.add(Restrictions.like("txnType", entity.getTxnType()));
				}
				if(!StringUtil.isEmpty(entity.getCropName()))
				{
					 DetachedCriteria cropCriteria = DetachedCriteria.forClass(ProcurementProduct.class);
					 cropCriteria.setProjection(Property.forName("code"));
					 cropCriteria.add(Restrictions.eq("name", entity.getCropName()));
					 
					 criteria.add(Restrictions.disjunction()
	                         .add(Property.forName("cropCode").in(cropCriteria)));
				}
				
				if(!StringUtil.isEmpty(entity.getVillageName()))
				{
					
					 DetachedCriteria villageCriteria = DetachedCriteria.forClass(Village.class);
					 villageCriteria.setProjection(Property.forName("id"));
					 villageCriteria.add(Restrictions.like("name", entity.getVillageName(),
			                 MatchMode.ANYWHERE));
					 
					 criteria.add(Restrictions.disjunction()
	                         .add(Property.forName("villageId").in(villageCriteria)));
				}
				if(!StringUtil.isEmpty(entity.getFarmerCode()))
				{
					 DetachedCriteria farmerCriteria = DetachedCriteria.forClass(Farmer.class);
					 farmerCriteria.setProjection(Property.forName("farmerId"));
					 farmerCriteria.add(Restrictions.like("farmerCode", entity.getFarmerCode(), MatchMode.ANYWHERE));
					 
					 criteria.add(Restrictions.disjunction()
	                         .add(Property.forName("farmerId").in(farmerCriteria)));
					
				}
			}
			/**State Filter**/
			if(entity.getStateName()!=null){
			    DetachedCriteria ownerCriteria = DetachedCriteria.forClass(Farmer.class);
                ownerCriteria.createAlias("village", "v");
                ownerCriteria.createAlias("v.city", "city");
                ownerCriteria.createAlias("city.locality", "locality");
                ownerCriteria.createAlias("locality.state", "state");
                ownerCriteria.setProjection(Property.forName("farmerId"));
                ownerCriteria.add(Restrictions.like("state.code",entity.getStateName(),MatchMode.ANYWHERE ));
                
                criteria.add(Property.forName("farmerId").in(ownerCriteria));
            }
			 /**ICS  Filter**/   
			 if (!ObjectUtil.isEmpty(entity.getIcsName())
                     && !StringUtil.isEmpty(entity.getIcsName())) {
              
              DetachedCriteria farmerCriteria = DetachedCriteria.forClass(Farmer.class);
               farmerCriteria.setProjection(Property.forName("farmerId"));
               farmerCriteria.add(Restrictions.like("icsName", entity.getIcsName(),
                       MatchMode.ANYWHERE));
               
               criteria.add(Property.forName("farmerId").in(farmerCriteria));
             }
            /**Coopertive Filter**/
			 if (!ObjectUtil.isEmpty(entity.getFpo())
                     && !StringUtil.isEmpty(entity.getFpo())) {
                
              DetachedCriteria farmerCriteria = DetachedCriteria.forClass(Farmer.class);
               farmerCriteria.setProjection(Property.forName("farmerId"));
               farmerCriteria.add(Restrictions.like("fpo", entity.getFpo(),
                       MatchMode.ANYWHERE));
               
               criteria.add(Property.forName("farmerId").in(farmerCriteria));
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
