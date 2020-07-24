/*
 * FarmerListReportDAO.java
 * Copyright (c) 2012-2013, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Property;
import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;

public class FarmerListReportDAO extends ReportDAO {

	protected Criteria createCriteria(Map params) {
		String entity = (String) params.get(ENTITY);
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		if (!ObjectUtil.isEmpty(entity)) {
			Farmer farmer = (Farmer) params.get(FILTER);
			if (!ObjectUtil.isEmpty(farmer) && !StringUtil.isEmpty(farmer.getTypez())
					&& farmer.getTypez().equals(Farmer.IRP)) {
				session.disableFilter(ISecurityFilter.BRANCH_FILTER);
			}
		}
		Criteria criteria = session.createCriteria(entity);
		return criteria;
	}

	protected void addExampleFiltering(Criteria criteria, Map params) {

		// check for filter entity
		Farmer entity = (Farmer) params.get(FILTER);
		if (entity != null) {

			HttpServletRequest httpRequest = ReflectUtil.getCurrentHttpRequest();
			String branchId_F = (String) httpRequest.getSession().getAttribute(ISecurityFilter.CURRENT_BRANCH);
			String tenantId = (String) httpRequest.getSession().getAttribute(ISecurityFilter.TENANT_ID);
			tenantId = StringUtil.isEmpty(tenantId) ? ISecurityFilter.DEFAULT_TENANT_ID : tenantId;

			/*if(tenantId.equalsIgnoreCase(ESESystem.PGSS_TENANT_ID)){
				criteria.createAlias("village", "v");
				criteria.createAlias("v.gramPanchayat", "gramPanchayat");
				criteria.createAlias("v.city", "city");
				criteria.createAlias("city.locality", "locality");
				criteria.createAlias("locality.state", "state");
				criteria.createAlias("state.country", "country");
				criteria.createAlias("samithi", "s", criteria.LEFT_JOIN);
			}
			else{*/
				criteria.createAlias("village", "v",criteria.LEFT_JOIN);
				criteria.createAlias("v.city", "city",criteria.LEFT_JOIN);
				criteria.createAlias("city.locality", "locality",criteria.LEFT_JOIN);
				criteria.createAlias("locality.state", "state",criteria.LEFT_JOIN);
				criteria.createAlias("samithi", "s", criteria.LEFT_JOIN);
				criteria.createAlias("certificateStandard", "cs", criteria.LEFT_JOIN);
			
			//}
		
			
			//criteria.createAlias("farmerHealthAsses", "fh",criteria.LEFT_JOIN);
			//criteria.createAlias("farmerSelfAsses", "fs",criteria.LEFT_JOIN);
			
			// criteria.createAlias("village", "v").createAlias("samithi",
			// "s").createAlias("certificateStandard", "cs",
			// Criteria.LEFT_JOIN);

			// Farmers statusCode should be 0 { 0 : SUCCESSFULL ENROLLMENT }
			criteria.add(Restrictions.eq("statusCode", ESETxnStatus.SUCCESS.ordinal()));

			if (!ObjectUtil.isListEmpty(entity.getBranchesList())) {
				criteria.add(Restrictions.in("branchId", entity.getBranchesList()));
			}

			if (!StringUtil.isEmpty(entity.getBranchId())) {
				criteria.add(Restrictions.like("branchId", entity.getBranchId(), MatchMode.EXACT));
			}
			
			if (!StringUtil.isEmpty(entity.getProofNo())) {
				criteria.add(Restrictions.like("proofNo", entity.getProofNo(), MatchMode.ANYWHERE));
			}

			if (!StringUtil.isEmpty(entity.getTypez()) && entity.getTypez().equals(Farmer.IRP)) {
				if (!StringUtil.isEmpty(branchId_F)) {
					criteria.add(Restrictions.like("branchId", branchId_F, MatchMode.EXACT));
				}
				criteria.add(Restrictions.like("typez", entity.getTypez(), MatchMode.EXACT));
			}

			if (entity.getFarmerId() != null)
				criteria.add(Restrictions.like("farmerId", entity.getFarmerId(), MatchMode.ANYWHERE));

			if (entity.getFirstName() != null)
				criteria.add(Restrictions.like("firstName", entity.getFirstName(), MatchMode.ANYWHERE));

			if (entity.getLastName() != null)
				criteria.add(Restrictions.like("lastName", entity.getLastName(), MatchMode.ANYWHERE));

			if (entity.getSurName() != null)
				criteria.add(Restrictions.like("surName", entity.getSurName(), MatchMode.ANYWHERE));

			if (entity.getVillage() != null) {
				String name = entity.getVillage().getName();
				if (name != null && !"".equals(name)) {
					criteria.add(Restrictions.like("v.name", name, MatchMode.ANYWHERE));
				}
			}
			
			if (entity.getVillage() != null  && entity.getVillage().getCity() != null && entity.getVillage().getCity().getLocality() != null && entity.getVillage().getCity().getLocality().getState() != null) {
				String name = entity.getVillage().getCity().getLocality().getState().getName();
				if (name != null && !"".equals(name)) {
					criteria.add(Restrictions.like("state.name", name, MatchMode.ANYWHERE));
				}
			}
			
			if (entity.getCity() != null && entity.getCity().getLocality() != null && entity.getCity().getLocality().getState() != null) {
				String name = entity.getCity().getLocality().getState().getName();
				if (name != null && !"".equals(name)) {
					criteria.add(Restrictions.like("state.name", name, MatchMode.ANYWHERE));
				}
			}
			if (entity.getCity() != null && entity.getCity().getLocality() != null ) {
				String name = entity.getCity().getLocality().getName();
				if (name != null && !"".equals(name)) {
					criteria.add(Restrictions.like("locality.name", name, MatchMode.ANYWHERE));
				}
			}
			if (entity.getCity() != null ) {
				String name = entity.getCity().getName();
				if (name != null && !"".equals(name)) {
					criteria.add(Restrictions.like("city.name", name, MatchMode.ANYWHERE));
				}
			}
			if (entity.getSamithi() != null) {
				String name = entity.getSamithi().getName();
				if (name != null && !"".equals(name)) {
					criteria.add(Restrictions.like("s.name", name, MatchMode.ANYWHERE));
				}
			}

			
			 if(!ObjectUtil.isEmpty(entity.getCertificateStandard()) &&
			 !StringUtil.isEmpty(entity.getCertificateStandard().getName())){
			 criteria.add(Restrictions.like("cs.name",
			 entity.getCertificateStandard().getName(), MatchMode.EXACT)); }
			 
			 if(entity.getCertificateStandardLevel()!=-1){
				  criteria.add(Restrictions.eq("certificateStandardLevel",
				  entity.getCertificateStandardLevel())); }
			/*
			 * if(entity.getCertificationType()!=-1){
			 * criteria.add(Restrictions.eq("certificationType",
			 * entity.getCertificationType())); }
			 * 
			 * 
			 * if (entity.getIsCertifiedFarmer() >=0) {
			 * criteria.add(Restrictions.eq("isCertifiedFarmer",
			 * entity.getIsCertifiedFarmer())); }
			 */

			if (!StringUtil.isEmpty(entity.getCertificationFilter())) {
				criteria.add(Restrictions.eq("isCertifiedFarmer", Integer.parseInt(entity.getCertificationFilter())));
			}
			if(tenantId.equalsIgnoreCase(ESESystem.AGRO_TENANT)) {
				  if (!StringUtil.isEmpty(entity.getFarmSize())){
					    if(entity.getFarmSize()==1){
					    criteria.add(Restrictions.ge("farmSize", entity.getFarmSize()));
					    }else if(entity.getFarmSize()==2){
					    	  criteria.add(Restrictions.between("farmSize", 0.0,1.0));
					    }else if(entity.getFarmSize()==3){
					    	//criteria.add(Restrictions.isNull("farmSize"));
					    	criteria.add(Restrictions.eq("farmSize", -1.0));
					    }
					
					   }
			}
			if(tenantId.equalsIgnoreCase(ESESystem.OCP)) {
				  if (!StringUtil.isEmpty(entity.getFarmSize())){
					    if(entity.getFarmSize()==1){
					    criteria.add(Restrictions.ge("farmSize", entity.getFarmSize()));
					    }else if(entity.getFarmSize()==2){
					    	  criteria.add(Restrictions.between("farmSize", 0.0,1.0));
					    }else if(entity.getFarmSize()==3){
					    	//criteria.add(Restrictions.isNull("farmSize"));
					    	criteria.add(Restrictions.eq("farmSize", -1.0));
					    }
					
					   }
			}
			if (entity.getFilterStatus() != null && !"".equals(entity.getFilterStatus())) {
				if (entity.getFilterStatus().equals("status")) {
					criteria.add(Restrictions.like("status", entity.getStatus()));
				}
			}
			
			if(tenantId.equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)){
				if (!StringUtil.isEmpty(entity.getTotalCultivatable())) {
					criteria.add(Restrictions.like("totalCultivatable", entity.getTotalCultivatable(), MatchMode.ANYWHERE));
				}
			
			}
			if (!StringUtil.isEmpty(entity.getMobileNumber())) {
				criteria.add(Restrictions.like("mobileNumber", entity.getMobileNumber(), MatchMode.ANYWHERE));
			}

			if (!StringUtil.isEmpty(entity.getSearchPage()) && entity.getSearchPage().equalsIgnoreCase("smsList")) {
				criteria.add(
						Restrictions.and(Restrictions.ne("mobileNumber", ""), Restrictions.isNotNull("mobileNumber")));
			}
			if(tenantId.equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
				if(StringUtil.isEmpty(branchId_F)|| branchId_F.equalsIgnoreCase(ESESystem.PRATIBHA_ORGANIC_BRANCH_ID)){
				if(!StringUtil.isEmpty(entity.getFarmersCodeTracenet())){
					criteria.add(Restrictions.like("farmersCodeTracenet", entity.getFarmersCodeTracenet(), MatchMode.ANYWHERE));
				}
			}}
			else 	
			if (!StringUtil.isEmpty(entity.getFarmerCode())) {
				criteria.add(Restrictions.like("farmerCode", entity.getFarmerCode(), MatchMode.ANYWHERE));
			}

			/*
			 * if (!StringUtil.isEmpty(entity.getAdhaarNo())) {
			 * criteria.add(Restrictions.like("adhaarNo", entity.getAdhaarNo(),
			 * MatchMode.ANYWHERE)); }
			 */
			if (entity.getCreatedUsername() != null)
				criteria.add(Restrictions.like("createdUsername", entity.getCreatedUsername(), MatchMode.ANYWHERE));
			
			if (entity.getLastUpdatedUsername() != null)
				criteria.add(Restrictions.like("lastUpdatedUsername", entity.getLastUpdatedUsername(), MatchMode.ANYWHERE));
			
            if (entity.getCreatedDate() != null) {
            	if(tenantId.equalsIgnoreCase(ESESystem.OCP) ){
            		criteria.add(Restrictions.between("createdDate", DateUtil.getDateWithoutTime(entity
                            .getCreatedDate()), DateUtil.getDateWithLastMinuteofDay((entity
                                    .getCreatedDate()))));
            	}else{
                criteria.add(Restrictions.between("dateOfJoining", DateUtil.getDateWithoutTime(entity
                        .getCreatedDate()), DateUtil.getDateWithLastMinuteofDay((entity
                        .getCreatedDate())))); 
                }
            }
            
            if (entity.getLastUpdatedDate() != null) {
            	 criteria.add(Restrictions.between("lastUpdatedDate", DateUtil.getDateWithoutTime(entity
                         .getLastUpdatedDate()), DateUtil.getDateWithLastMinuteofDay((entity
                         .getLastUpdatedDate())))); 
            	
            }
			
			if (tenantId != null) {
				if (!StringUtil.isEmpty(entity.getSeasonCode())) {
					criteria.add(Restrictions.like("seasonCode", entity.getSeasonCode(), MatchMode.ANYWHERE));
				}
			}
			
	
			if (!StringUtil.isEmpty(entity.getMasterData())) {
             DetachedCriteria farmerCriteria = DetachedCriteria.forClass(MasterData.class);
              farmerCriteria.setProjection(Property.forName("code"));
              farmerCriteria.add(Restrictions.like("name", entity.getMasterData(),
                      MatchMode.ANYWHERE));
              
              criteria.add(Property.forName("masterData").in(farmerCriteria));
            }
			
			if(tenantId.equalsIgnoreCase(ESESystem.IFFCO_TENANT_ID) || tenantId.equalsIgnoreCase(ESESystem.AWI_TENANT_ID)){
				if (entity.getFpo() !=null && !StringUtil.isEmpty(entity.getFpo()) && !entity.getFpo().equals("-1")) {
					criteria.add(Restrictions.eq("fpo", entity.getFpo()));
				}
			}
			
			if(tenantId.equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)){
				if (!StringUtil.isEmpty(entity.getTotalAcreage())) {
					criteria.add(Restrictions.like("totalAcreage", entity.getTotalAcreage(), MatchMode.ANYWHERE));
				}
			}
			
			if (tenantId.equalsIgnoreCase(ESESystem.OCP)) {
				if (!StringUtil.isEmpty(entity.getCropNames())) {
					criteria.add(Restrictions.like("cropNames", entity.getCropNames(), MatchMode.ANYWHERE));
				}
			}
			
			// sorting direction
			String dir = (String) params.get(DIR);
			// sorting column
			String sort = (String) params.get(SORT_COLUMN);
			if (dir != null && sort != null) {
				if (dir.equals(DESCENDING)) {
					// sort descending
					criteria.addOrder(Order.desc(sort));
				} else {
					// sort ascending
					criteria.addOrder(Order.asc(sort));
				}
			} else {
				criteria.addOrder(Order.asc("id"));
			}
		}
	}

}
