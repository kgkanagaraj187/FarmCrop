/*
 * ProductDistributionReportDAO.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.order.entity.txn.DistributionDetail;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.profile.Product;
import com.sourcetrace.esesw.entity.profile.Farmer;

@SuppressWarnings("unchecked")
public class ProductDistributionReportDAO extends ReportDAO {

	protected void addExampleFiltering(Criteria criteria, Map params) {
		Object object = (Object) params.get(FILTER);
		if (!ObjectUtil.isEmpty(object)) {
			
			Map<String,Object> map = (Map<String,Object>) object;
			
			String filterEntity = (String)map.get("FILTER");
			DistributionDetail distributionDetail = (DistributionDetail) map.get("FILTERENTITY");
			
			if(Product.class.getName().equalsIgnoreCase(filterEntity)){
				
				criteria.createAlias("product","p",Criteria.LEFT_JOIN).createAlias("distribution", "d").createAlias("p.subcategory", "sc",Criteria.LEFT_JOIN).createAlias("sc.category", "c",Criteria.LEFT_JOIN);
				
				criteria.add(Restrictions.isNotNull("d.village"));
				criteria.add(Restrictions.isNotNull("d.season"));
				
				if(!ObjectUtil.isEmpty(distributionDetail.getProduct()) && !ObjectUtil.isEmpty(distributionDetail.getProduct().getSubcategory()) && !ObjectUtil.isEmpty(distributionDetail.getProduct().getSubcategory().getCategory()) && !StringUtil.isEmpty(distributionDetail.getProduct().getSubcategory().getCategory().getName())){
					criteria.add(Restrictions.like("c.name", distributionDetail.getProduct().getSubcategory().getCategory().getName(), MatchMode.ANYWHERE));
				}
				
				if(!ObjectUtil.isEmpty(distributionDetail.getProduct()) && !ObjectUtil.isEmpty(distributionDetail.getProduct().getSubcategory()) && !StringUtil.isEmpty(distributionDetail.getProduct().getSubcategory().getName())){
					criteria.add(Restrictions.like("sc.name", distributionDetail.getProduct().getSubcategory().getName() , MatchMode.ANYWHERE));
				}
				
				if(!ObjectUtil.isEmpty(distributionDetail.getProduct()) && !StringUtil.isEmpty(distributionDetail.getProduct().getCode())){
					criteria.add(Restrictions.like("p.code", distributionDetail.getProduct().getCode() , MatchMode.ANYWHERE));
				}
				
				if(!ObjectUtil.isEmpty(distributionDetail.getDistribution()) && !ObjectUtil.isEmpty(distributionDetail.getDistribution().getVillage()) && !StringUtil.isEmpty(distributionDetail.getDistribution().getVillage().getName())){
					String subQuery = " this_.distribution_id IN ("
									  +" SELECT d.id FROM distribution d"
									  +" LEFT JOIN distribution_detail dd ON dd.DISTRIBUTION_ID = d.ID"
									  +" LEFT JOIN village v ON v.ID = d.VILLAGE_ID "
									  +" WHERE v.NAME = '"+distributionDetail.getDistribution().getVillage().getName()+"'"
									  +" )";
					criteria.add(Restrictions.sqlRestriction(subQuery));
				}
				
				criteria.setProjection(Projections.projectionList()
						.add(Projections.groupProperty("p.id"))
						.add(Projections.property("p.code"))
						.add(Projections.property("p.name"))
						.add(Projections.property("sc.name"))
				     	.add(Projections.property("p.unit"))
						.add(Projections.property("p.price"))
						.add(Projections.sum("quantity"))
						.add(Projections.sum("subTotal")));
				
			}else if(Farmer.class.getName().equalsIgnoreCase(filterEntity)){
				
				criteria.createAlias("distribution","d").createAlias("d.agroTransaction", "a1").createAlias("d.village", "v").createAlias("product", "p");
				
				if(!ObjectUtil.isEmpty(distributionDetail.getProduct())){
					criteria.add(Restrictions.eq("p.id", distributionDetail.getProduct().getId()));
				}
				
				if(!ObjectUtil.isEmpty(distributionDetail.getDistribution()) && !ObjectUtil.isEmpty(distributionDetail.getDistribution().getVillage()) && !StringUtil.isEmpty(distributionDetail.getDistribution().getVillage().getName())){
					String subQuery = " this_.distribution_id IN ("
									  +" SELECT d.id FROM distribution d"
									  +" LEFT JOIN distribution_detail dd ON dd.DISTRIBUTION_ID = d.ID"
									  +" LEFT JOIN village v ON v.ID = d.VILLAGE_ID "
									  +" WHERE v.NAME = '"+distributionDetail.getDistribution().getVillage().getName()+"'"
									  +" )";
					criteria.add(Restrictions.sqlRestriction(subQuery));
				}
				
				criteria.setProjection(Projections.projectionList()
						.add(Projections.groupProperty("a1.farmerId"))						
						.add(Projections.property("a1.farmerName"))
						.add(Projections.property("v.name"))
						.add(Projections.sum("quantity"))
						.add(Projections.sum("subTotal")));
				
			}
			
		}
	}
	
	protected void addProjections(Criteria criteria, Map params) {
		
	}
	
}
