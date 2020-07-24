package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Property;

import com.ese.entity.profile.CropHarvest;
import com.ese.entity.profile.CropHarvestDetails;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farmer;

public class CropHarvestReportDAO extends ReportDAO {
	protected Criteria createCriteria(Map params) {
		String entity = (String) params.get(ENTITY);
		Object object = (Object) params.get(FILTER);
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof CropHarvestDetails) {
				entity = CropHarvestDetails.class.getName();
			}
		}
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(entity);

		return criteria;
	}
	protected void addExampleFiltering(Criteria criteria, Map params) {
		Object object = (Object) params.get(FILTER);
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof CropHarvest) {
				CropHarvest cropHarvest = (CropHarvest) object;
				criteria.createAlias("cropHarvestDetails", "chd");
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
		    		
				if (!ObjectUtil.isEmpty(cropHarvest.getFarmerId())) {
					criteria.add(Restrictions.eq("farmerId", cropHarvest
							.getFarmerId()));
				}
				
				if (!ObjectUtil.isEmpty(cropHarvest.getFarmCode())) {
					criteria.add(Restrictions.eq("farmCode", cropHarvest
							.getFarmCode()));
				}
				if (!ObjectUtil.isEmpty(cropHarvest.getAgentId())) {
					criteria.add(Restrictions.eq("agentId", cropHarvest.getAgentId()));
				}
				 if (!StringUtil.isEmpty(cropHarvest.getBranchId())) {
						criteria.add(Restrictions.like("branchId", cropHarvest.getBranchId(), MatchMode.EXACT));
					}
				 
				 if(!StringUtil.isEmpty(cropHarvest.getSeasonCode())) 
	             {
	              
	              criteria.add(Restrictions.like("seasonCode", cropHarvest.getSeasonCode(),MatchMode.ANYWHERE));
	             }
				 
				 /**State Filter**/
	                if(cropHarvest.getStateName()!=null){
	                    DetachedCriteria ownerCriteria = DetachedCriteria.forClass(Farmer.class);
	                    ownerCriteria.createAlias("village", "v");
	                    ownerCriteria.createAlias("v.city", "city");
	                    ownerCriteria.createAlias("city.locality", "locality");
	                    ownerCriteria.createAlias("locality.state", "state");
	                    ownerCriteria.setProjection(Property.forName("farmerId"));
	                    ownerCriteria.add(Restrictions.like("state.code",cropHarvest.getStateName(),MatchMode.ANYWHERE ));
	                    
	                    criteria.add(Property.forName("farmerId").in(ownerCriteria));
	                }
	                 /**ICS  Filter**/   
	                 if (!ObjectUtil.isEmpty(cropHarvest.getIcsname())
	                         && !StringUtil.isEmpty(cropHarvest.getIcsname())) {
	                  
	                  DetachedCriteria farmerCriteria = DetachedCriteria.forClass(Farmer.class);
	                   farmerCriteria.setProjection(Property.forName("farmerId"));
	                   farmerCriteria.add(Restrictions.like("icsName", cropHarvest.getIcsname(),
	                           MatchMode.ANYWHERE));
	                   
	                   criteria.add(Property.forName("farmerId").in(farmerCriteria));
	                 }
	                /**Coopertive Filter**/
	                 if (!ObjectUtil.isEmpty(cropHarvest.getFpo())
	                         && !StringUtil.isEmpty(cropHarvest.getFpo())) {
	                    
	                  DetachedCriteria farmerCriteria = DetachedCriteria.forClass(Farmer.class);
	                   farmerCriteria.setProjection(Property.forName("farmerId"));
	                   farmerCriteria.add(Restrictions.like("fpo", cropHarvest.getFpo(),
	                           MatchMode.ANYWHERE));
	                   
	                   criteria.add(Property.forName("farmerId").in(farmerCriteria));
	                 }

				 
				 if(!StringUtil.isEmpty(cropHarvest.getCropName())) 
	             {
					 criteria.createAlias("chd.crop", "crop");
	              criteria.add(Restrictions.like("crop.name", cropHarvest.getCropName(),MatchMode.ANYWHERE));
	             }

			} else if (object instanceof CropHarvestDetails) {

				CropHarvestDetails cropHarvestDetails = (CropHarvestDetails) object;

				criteria.createAlias("cropHarvest", "ch", Criteria.LEFT_JOIN);

				if (!ObjectUtil.isEmpty(cropHarvestDetails.getCropHarvest()))
					criteria.add(Restrictions.eq("ch.id", cropHarvestDetails
							.getCropHarvest().getId()));
				
				

			}

		}

	
	}
}
