package com.sourcetrace.esesw.dao.report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.entity.DynamicFeildMenuConfig;
import com.sourcetrace.eses.entity.DynamicFieldReportJoinMap;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicData;

public class DynamicCertificationReportDAO extends ReportDAO {

	private SimpleDateFormat DataBaseFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	DetachedCriteria dCriteria = null;
	
	@Override
	protected void addExampleFiltering(Criteria criteria, Map params) {
		FarmerDynamicData entity = (FarmerDynamicData) params.get(FILTER);
		SimpleExpression branchFieldExpression = null;
		 
		if (!ObjectUtil.isEmpty(entity)) {
			if (!ObjectUtil.isListEmpty(entity.getBranchesList())) {
				criteria.add(Restrictions.in("branch", entity.getBranchesList()));
			}
			
			if (!StringUtil.isEmpty(entity.getBranch())){
			    criteria.add(Restrictions.eq("branch", entity.getBranch()));
			}
			  
			if (!StringUtil.isEmpty(entity.getTxnType())) {
				criteria.add(Restrictions.eq("txnType", entity.getTxnType()));
			}

			if (!StringUtil.isEmpty(entity.getSeason())) {
				criteria.add(Restrictions.like("season", entity.getSeason(), MatchMode.EXACT));
			}
			
			if (!StringUtil.isEmpty(entity.getConversionStatus())) {
				criteria.add(Restrictions.eq("conversionStatus", entity.getConversionStatus()));
			}
			
			if (!StringUtil.isEmpty(entity.getTotalScore())) {
				criteria.add(Restrictions.eq("totalScore", entity.getTotalScore()));
			}
			
			if (!StringUtil.isEmpty(entity.getCreatedUser()) && entity.getCreatedUser().split("~")[0].trim() != null && !StringUtil.isEmpty(entity.getCreatedUser().split("~")[0].trim())) {
				criteria.add(Restrictions.eq("createdUser", entity.getCreatedUser().split("~")[0].trim().toString()));
			}
/*			branchFieldExpression = setBranchFieldFilter(entity);
			if (!ObjectUtil.isEmpty(branchFieldExpression)) {
				criteria.add(branchFieldExpression);
			}*/
			if (!StringUtil.isEmpty(entity.getReferenceId())) {
				criteria.add(Restrictions.sqlRestriction("find_in_set("+entity.getReferenceId()+",this_.REFERENCE_ID)"));
			
		
		}		
			if(!StringUtil.isEmpty(entity.getId()) && entity.getId()>0){
				criteria.add(Restrictions.eq("id", entity.getId()));
			}
			if (!ObjectUtil.isEmpty(entity.getEntityMap()) && entity.getEntityMap().size() >= 1 && entity.getEntityMap().containsKey("filters")) {
				Map<String,String> entityType = (Map<String,String>) entity.getEntityMap().get("filters");
				if(!entityType.isEmpty()){
				
					criteria.createAlias("farmerDynamicFieldsValues", "fd");
				
				entityType.entrySet().stream().forEach(u ->{
					String key  =u.getKey().split("~")[0].toString();
					String type  =u.getKey().split("~")[1].toString();
					if(type.equals("1")){
						criteria.add(Restrictions.eq("fd.fieldName",key));
						criteria.add(Restrictions.like("fd.fieldValue",u.getValue()));
					}else if(type.equals("2")){
						DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
						String startDate = u.getValue().split("-")[0].toString();
						String endDate = u.getValue().split("-")[1].toString();
							criteria.add(Restrictions.between("fd.fieldValue",startDate.trim(),endDate.trim()));
							criteria.add(Restrictions.eq("fd.fieldName",key));
						
					}else if(type.equals("3")){
						criteria.add(Restrictions.eq("fd.fieldName",key));
						criteria.add(Restrictions.eq("fd.fieldValue",u.getValue()));
					}
					
				});
				}
			}
			if (!ObjectUtil.isEmpty(entity.getEntityMap()) && entity.getEntityMap().size() >= 1 && entity.getEntityMap().containsKey(ENTITY)) {
				String entityType = (String) entity.getEntityMap().get(ENTITY);
				List<DynamicFieldReportJoinMap> mapList = (List<DynamicFieldReportJoinMap>) entity.getEntityMap().get(JOIN_MAP);
				//DetachedCriteria dCriteria = null;
				if (entityType.equals(String.valueOf(DynamicFeildMenuConfig.EntityTypes.FARMER.ordinal())) || entityType.equals(String.valueOf(DynamicFeildMenuConfig.EntityTypes.TRAINING.ordinal()))) {
					dCriteria = DetachedCriteria.forClass(Farmer.class);
				} else if (entityType.equals(String.valueOf(DynamicFeildMenuConfig.EntityTypes.FARM.ordinal())) || entityType.equals(String.valueOf(DynamicFeildMenuConfig.EntityTypes.CERTIFICATION.ordinal()))) {
					dCriteria = DetachedCriteria.forClass(Farm.class);
					
					if(entityType.equals(String.valueOf(DynamicFeildMenuConfig.EntityTypes.CERTIFICATION.ordinal())))
					criteria.createAlias("farmIcs", "fc", criteria.LEFT_JOIN);
					
				/*	if(entity.getEntityMap().containsKey("farmer")){
					//	dCriteria.createAlias("farmer", "f");
						dCriteria.add(Restrictions.eq("f.id", Long.valueOf(entity.getEntityMap().get("farmer").toString())));
					}*/
				} else if (entityType.equals(String.valueOf(DynamicFeildMenuConfig.EntityTypes.GROUP.ordinal()))) {
					dCriteria = DetachedCriteria.forClass(Warehouse.class);
				}else if (entityType.equals(String.valueOf(DynamicFeildMenuConfig.EntityTypes.FARM_CROPS.ordinal()))) {
					dCriteria = DetachedCriteria.forClass(FarmCrops.class);
					
				}
				StringBuilder alias = new StringBuilder();
				mapList.stream().forEach(restriction->{
					String[] aliasSplit = restriction.getProperties().split("=");
					dCriteria.createAlias(aliasSplit[1].toString().trim(), aliasSplit[0].toString().trim(), criteria.LEFT_JOIN);
				});
				
				Map<String,String> conditoon = (Map<String,String>) entity.getEntityMap().get(IClientService.PROJECTIONS_CONDITION);
				if(conditoon!=null && !conditoon.isEmpty()){
					conditoon.entrySet().stream().forEach(restriction->{
					if(StringUtil.isLong(restriction.getValue().split("~")[0].trim())){
						dCriteria.add(Restrictions.eq(restriction.getKey().split("~")[0].trim(),Long.valueOf(restriction.getValue().split("~")[0].trim())));
					}else{
						dCriteria.add(Restrictions.eq(restriction.getKey().split("~")[0].trim(),restriction.getValue().split("~")[0].trim()));
					}
					
				});
					
					dCriteria.setProjection(Property.forName("id"));
				}
				
				
          	  criteria.add(Property.forName("referenceId").in(dCriteria));
			}
		}
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
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
	
	/*SimpleExpression setBranchFieldFilter(FarmerDynamicData entity) {
		SimpleExpression simpleExpression = null;
		if (!ObjectUtil.isEmpty(entity) && !StringUtil.isEmpty(entity.getBranch())) {
			simpleExpression = Restrictions.eq("branch", entity.getBranch());
		}
		return simpleExpression;
	}*/

}
