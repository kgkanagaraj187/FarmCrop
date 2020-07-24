/*
 * CategoryServiceReportDAO.java
 * Copyright (c) 2012-2013, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.Farmer;

public class FarmCropsReportDAO extends ReportDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.ReportDAO#addExampleFiltering(org.hibernate.Criteria,
	 * java.util.Map)
	 */
	protected void addExampleFiltering(Criteria criteria, Map params) {

		// check for filter entity
		FarmCrops entity = (FarmCrops) params.get(FILTER);

		if (entity != null) {
			//criteria.createAlias("farmCropsMaster", "fcm");
			
			HttpSession httpSession = ReflectUtil.getCurrentHttpSession();

			Object object = httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
			String currentBranch = ObjectUtil.isEmpty(object) ? "" : object.toString();

			/*if (!StringUtil.isEmpty(currentBranch)) {
				criteria.add(Restrictions.like("branchId", currentBranch));
			}*/
			criteria.createAlias("farm", "f", Criteria.LEFT_JOIN).createAlias("f.farmer","fa",Criteria.LEFT_JOIN);
			 criteria.createAlias("procurementVariety", "pv");
             criteria.createAlias("pv.procurementProduct", "pc");
            
             if (!StringUtil.isEmpty(entity.getBranchId())) {
 				criteria.add(Restrictions.like("branchId", entity.getBranchId(), MatchMode.EXACT));
 			}             
			
             if(!StringUtil.isEmpty(entity.getCategoryFilter()) && StringUtil.isInteger(entity.getCategoryFilter()) && Integer.parseInt(entity.getCategoryFilter())>=0){
     			criteria.add(Restrictions.eq("cropCategory",Integer.parseInt(entity.getCategoryFilter())));
     		}
             
		    if(!ObjectUtil.isEmpty(entity.getFarm()) && !ObjectUtil.isEmpty(entity.getFarm().getFarmer())){
			    criteria.add(Restrictions.eq("fa.id",entity.getFarm()
					.getFarmer().getId()));
			}
		    
		    if (!StringUtil.isEmpty(entity.getFarm().getFarmName()))
                criteria.add(Restrictions.like("f.farmName", entity.getFarm().getFarmName(), MatchMode.ANYWHERE));
            
		    
		    if (entity.getProcurementVariety()!= null
                    && entity.getProcurementVariety().getProcurementProduct()!=null
                    &&  entity.getProcurementVariety().getProcurementProduct().getName()!= null
                    && !"".equals(entity.getProcurementVariety().getProcurementProduct().getName())) {
		       
                criteria.add(Restrictions.like("pc.name", entity
                        .getProcurementVariety().getProcurementProduct().getName(), MatchMode.ANYWHERE));
            }
		    
		    criteria.add(Restrictions.eq("status", Farmer.Status.ACTIVE.ordinal()));
            
            if (entity.getProcurementVariety()!= null
                    &&  entity.getProcurementVariety().getName()!= null
                    && !"".equals(entity.getProcurementVariety().getName())) {
                criteria.add(Restrictions.like("pv.name", entity.getProcurementVariety().getName(), MatchMode.ANYWHERE));
            }
            
            if (!StringUtil.isEmpty(entity.getEstYldPfx())) {
            	
                criteria.add(Restrictions.like("estimatedYield", entity.getEstYldPfx()));
            }
            
            if (!StringUtil.isEmpty(entity.getCultiArea())) {
            	
                criteria.add(Restrictions.like("cultiArea", entity.getCultiArea()));
            }
            
            
            if (!StringUtil.isEmpty(entity.getSeason())&&entity.getSeason()!=0&&entity.getSeason()!=-1) {
            	 criteria.createAlias("cropSeason", "s");
                criteria.add(Restrictions.eq("s.id", entity.getSeason()));
            }
			
			/*if (entity.getFarmCropsMaster() != null
					&& entity.getFarmCropsMaster().getCode() != null
					&& !"".equals(entity.getFarmCropsMaster().getCode())) {
				criteria.add(Restrictions.like("fcm.code", entity
						.getFarmCropsMaster().getCode(), MatchMode.ANYWHERE));
			}
			
			if (entity.getFarmCropsMaster() != null
					&& entity.getFarmCropsMaster().getName() != null
					&& !"".equals(entity.getFarmCropsMaster().getName())) {
				criteria.add(Restrictions.like("fcm.name", entity
						.getFarmCropsMaster().getName(), MatchMode.ANYWHERE));
			}
			
			if (entity.getFarm() != null
					&& entity.getFarm().getFarmName() != null
					&& !"".equals(entity.getFarm().getFarmName())) {
				criteria.add(Restrictions.like("f.farmName", entity
						.getFarm().getFarmName(), MatchMode.ANYWHERE));
			}
			
			if (!StringUtil.isEmpty(entity.getCropArea()))
				criteria.add(Restrictions.like("cropArea",
						entity.getCropArea(), MatchMode.ANYWHERE));*/
            
			/*if (!StringUtil.isEmpty(entity.getProductionPerYear()))
				criteria.add(Restrictions.like("productionPerYear", entity
						.getProductionPerYear(), MatchMode.ANYWHERE));*/
			
			if (!ObjectUtil.isEmpty(entity.getFarm()) && entity.getFarm().getId()>0)
                criteria.add(Restrictions.eq("fa.id", entity.getFarm().getId()));
			
			// get Farm crops list from list of farms under one particular
			// farmer--- Another method
			/*DetachedCriteria farmCriteria = DetachedCriteria.forClass(
					Farm.class).setProjection(Property.forName("id"));
			farmCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			if (!ObjectUtil.isEmpty(entity.getFarm())
					&& !ObjectUtil.isEmpty(entity.getFarm().getFarmer())) {
				farmCriteria.add(Restrictions.eq("farmer.id", entity.getFarm()
						.getFarmer().getId()));
			}
			criteria.add(Subqueries.propertyIn("farm.id", farmCriteria));*/
			
			
			
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
