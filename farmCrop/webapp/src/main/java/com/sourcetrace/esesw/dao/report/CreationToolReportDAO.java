package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.ese.entity.profile.CropSupplyDetails;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.DynamicFieldConfig;
import com.sourcetrace.eses.entity.DynamicSectionConfig;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class CreationToolReportDAO extends ReportDAO  {

	protected Criteria createCriteria(Map params) {

		String entity = (String) params.get(ENTITY);
		Object object = (Object) params.get(FILTER);
		if (!ObjectUtil.isEmpty(object)) {
			
			if (object instanceof DynamicSectionConfig) {
				entity = DynamicSectionConfig.class.getName();
			}
		}

		Criteria criteria = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createCriteria(entity);

		return criteria;
	}

	
	protected void addExampleFiltering(Criteria criteria, Map params) {

		if(params.get(FILTER) instanceof DynamicFieldConfig){

			
			// check for filter entity
			DynamicFieldConfig entity = (DynamicFieldConfig) params.get(FILTER);
			if (entity != null) {
				
				criteria.createAlias("dynamicSectionConfig", "dsc");
				
				if (!StringUtil.isEmpty(entity.getDynamicSectionConfig())) {
					if (!StringUtil.isEmpty(entity.getDynamicSectionConfig().getSectionCode())) {
						String codeOrName = entity.getDynamicSectionConfig().getSectionCode();
						char first = codeOrName.charAt(0);
						char last = codeOrName.charAt(codeOrName.length() - 1);
							
					if(Character.isDigit(last)){
						criteria.add(Restrictions.like("dsc.sectionCode", "%"+entity.getDynamicSectionConfig().getSectionCode()+"%"));
					}else{
						criteria.add(Restrictions.like("dsc.sectionName", "%"+entity.getDynamicSectionConfig().getSectionCode()+"%"));
					}
				  }
				}
				
				if (!StringUtil.isEmpty(entity.getComponentType())) {
					criteria.add(Restrictions.like("componentType", entity.getComponentType()));
				}
				
				if (!StringUtil.isEmpty(entity.getComponentName())) {
					criteria.add(Restrictions.like("componentName", "%"+entity.getComponentName()+"%"));
				}
				
				
				//criteria.add(Restrictions.ne("componentType", "8"));
				criteria.add(Restrictions.ne("componentType", "10"));
				
				//criteria.createAlias("personalInfo", "pI").createAlias("agentType", "aT").createAlias("contactInfo", "cI");

				/*if (!ObjectUtil.isListEmpty(entity.getComponentType())) {
					criteria.add(Restrictions.in("componentType", entity.getComponentType()));
				}*/

			/*	if (!StringUtil.isEmpty(entity.getBranchId())) {
					criteria.add(Restrictions.eq("branchId", entity.getBranchId()));
				}
				if (entity.getProfileId() != null)
					criteria.add(Restrictions.like("profileId", entity.getProfileId(), MatchMode.ANYWHERE));

				if (entity.getPersonalInfo() != null) {

					String firstName = entity.getPersonalInfo().getFirstName();
					if (firstName != null && !"".equals(firstName)) {
						criteria.add(Restrictions.like("pI.firstName", firstName, MatchMode.ANYWHERE));
					}

					String lastName = entity.getPersonalInfo().getLastName();
					if (lastName != null && !"".equals(lastName)) {
						criteria.add(Restrictions.like("pI.lastName", lastName, MatchMode.ANYWHERE));
					}
				}

				if (!ObjectUtil.isEmpty(entity.getAgentType()) && entity.getAgentType().getId() > 0) {
					criteria.add(Restrictions.eq("aT.id", entity.getAgentType().getId()));
				}

				if (entity.getBodStatus() != -1) {
					criteria.add(Restrictions.eq("bodStatus", entity.getBodStatus()));
				}

				if (entity.getContactInfo() != null) {

					String mobileNo = entity.getContactInfo().getMobileNumber().trim();
					if (mobileNo != null && !"".equals(mobileNo)) {
						criteria.add(Restrictions.like("cI.mobileNumber", mobileNo, MatchMode.ANYWHERE));
					}
				}

				if (!StringUtil.isEmpty(entity.getSearchPage()) && entity.getSearchPage().equalsIgnoreCase("smsList")) {
					criteria.add(Restrictions.and(Restrictions.ne("cI.mobileNumber", ""),
							Restrictions.isNotNull("cI.mobileNumber")));
				}*/

				// sorting direction
				String dir = (String) params.get(DIR);
				// sorting column
				String sort = (String) params.get(SORT_COLUMN);
				if (dir.equals(DESCENDING)) {
					// sort descending
					criteria.addOrder(Order.desc(sort));
				} else {
					// sort ascending
					criteria.addOrder(Order.asc(sort));
				}
			}
		}else if(params.get(FILTER) instanceof DynamicSectionConfig){
			// check for filter entity
			DynamicSectionConfig entity = (DynamicSectionConfig) params.get(FILTER);
			if (entity != null) {
				
				if (!StringUtil.isEmpty(entity.getSectionName())) {
					criteria.add(Restrictions.like("sectionName", "%"+entity.getSectionName()+"%"));
				}
				
				if (!StringUtil.isEmpty(entity.getSectionCode())) {
					criteria.add(Restrictions.like("sectionCode", "%"+entity.getSectionCode()+"%"));
				}
				
				String dir = (String) params.get(DIR);
				String sort = (String) params.get(SORT_COLUMN);
				if (dir.equals(DESCENDING)) {
					criteria.addOrder(Order.desc(sort));
				} else {
					criteria.addOrder(Order.asc(sort));
				}
			}
		}
		
	}

	private Criteria createAlias(String string, String string2) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
