/*
 * AnimalHusbandaryReportDAO.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.dao.report;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.txn.agrocert.CropYield;
import com.sourcetrace.eses.txn.agrocert.CropYieldDetail;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicData;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicFieldsValue;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;
import java.util.List;
import java.util.stream.Collectors;

public class DynamicViewReportDAO extends ReportDAO {

	protected Criteria createCriteria(Map params) {

		String entity = (String) params.get(ENTITY);
		Object object = (Object) params.get(FILTER);
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof FarmerDynamicData) {
				entity = FarmerDynamicData.class.getName();
			}
			else if(object instanceof CropYieldDetail){
				entity = CropYieldDetail.class.getName();
			}
		}

		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(entity);

		return criteria;
	}

	protected void addExampleFiltering(Criteria criteria, Map params) {

		Object object = (Object) params.get(FILTER);
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof FarmerDynamicData) {
				
				FarmerDynamicData entity = (FarmerDynamicData) object;
				criteria.add(Restrictions.eq("txnType", entity.getTxnType()));
				criteria.add(Restrictions.eq("referenceId", entity.getReferenceId()));
				//criteria.createAlias("farmerDynamicFieldsValues", "fdfv");
				if(!StringUtil.isEmpty(entity.getTxnType()) && entity.getTxnType().equalsIgnoreCase("2019")){
					criteria.createAlias("farmerDynamicFieldsValues", "fdfv");
					criteria.add(Restrictions.eq("fdfv.id", entity.getFarmerDynamicFieldValueId()));
				}
				
				/*criteria.setProjection(Projections.projectionList()
						.add(Projections.groupProperty("id")));*/
				
				
			}
			else if(object instanceof CropYieldDetail){

				CropYieldDetail entity=(CropYieldDetail) object;
				criteria.createAlias("cropYield", "c");
				criteria.add(Restrictions.like("c.landHolding", entity.getMoleculeValue(), MatchMode.ANYWHERE));
			
			}
			else{
				Map<String, String> filters = (Map<String, String>) object;
				filters.entrySet().stream().forEach(u -> {
					if (u.getValue().contains("~")) {
						String cond = u.getValue().split("~")[0].trim();
						String value = u.getValue().split("~")[1].trim();
						Object valuee = new Object();
						valuee = value;
						if (cond.equals("1")&&!StringUtil.isEmpty(u.getValue().split("~")[2])&&u.getValue().split("~")[2].trim().equalsIgnoreCase("2")) {
							criteria.add(Restrictions.eq(u.getKey(), value));
						}else if (cond.equals("1")&&!StringUtil.isEmpty(u.getValue().split("~")[2])&&u.getValue().split("~")[2].trim().equalsIgnoreCase("3")) {     
							List<Long> list = Arrays.asList(value.toString().split("\\|")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
							criteria.add(Restrictions.in(u.getKey(), list));
						}else if(cond.equals("1")&&!StringUtil.isEmpty(u.getValue().split("~")[2])&&u.getValue().split("~")[2].trim().equalsIgnoreCase("1")){
							criteria.add(Restrictions.eq(u.getKey(), Integer.parseInt(value)));
						}else if (cond.equals("1")&&!StringUtil.isEmpty(u.getValue().split("~")[2])&&u.getValue().split("~")[2].trim().equalsIgnoreCase("4")) {     
							List<String> list = Arrays.asList(value.toString().split("\\|")).stream().map(s ->s.trim()).collect(Collectors.toList());
							criteria.add(Restrictions.in(u.getKey(), list));
						}else if (cond.equals("1")){
							criteria.add(Restrictions.eq(u.getKey(), valuee));
						}
						  else if (cond.equals("2")) {
							String[] dates = value.split("\\|");
							Date startDate = null;
							Date endDate = null;
							try {
								startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dates[0]+" 00:00:01");
								endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dates[1]+" 23:59:59");
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							// =DateUtil.convertStringToDate(dates[0],"dd-MM-yyyy");
							criteria.add(Restrictions.between(u.getKey(), startDate, endDate));

						} else if (cond.equals("3")) {
							criteria.add(Restrictions.gt(u.getKey(), valuee));
						} else if (cond.equals("4")) {
							criteria.add(Restrictions.lt(u.getKey(), valuee));
						} else if (cond.equals("5")) {
							criteria.add(Restrictions.le(u.getKey(), valuee));
						} else if (cond.equals("6")) {
							criteria.add(Restrictions.ge(u.getKey(), valuee));
						} else if (cond.equals("7")) {
							criteria.add(Restrictions.eq(u.getKey(), valuee));
						} else if (cond.equals("8")) {
							criteria.add(Restrictions.like(u.getKey(), value));
						} else if (cond.equals("9")) {
							if (value == null || StringUtil.isEmpty(value) || value.equalsIgnoreCase("null")) {

							} else {
								criteria.add(Restrictions.eq(u.getKey(), value));
							}

						} else if(cond.equals("10")){
							criteria.add(Restrictions.in(u.getKey(), Arrays.asList(value.toString().split(","))));
						}
					}
				});

			}
		}
		String alias = (String) params.get("alias");
		if (alias != null && !StringUtil.isEmpty(alias)) {
			String[] seperated = alias.split(",");
			for (String row : seperated) {
				String[] objectData = row.split("=");
				if(objectData!=null && objectData.length==3){
					criteria.createAlias(objectData[1], objectData[0],Integer.valueOf(objectData[2]));
				}else if(objectData!=null && objectData.length==2){
				criteria.createAlias(objectData[1], objectData[0]);
				}
					
			}

		}
	}
	

}
