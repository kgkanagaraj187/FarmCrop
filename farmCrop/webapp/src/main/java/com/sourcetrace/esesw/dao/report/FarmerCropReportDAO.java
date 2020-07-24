/*
 * CustomerRegistrationReportDAO.java
 * Copyright (c) 2012-2013, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

package com.sourcetrace.esesw.dao.report;

import java.util.Date;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;

import aj.org.objectweb.asm.Type;

public class FarmerCropReportDAO extends ReportDAO {

	@Override
	protected void addExampleFiltering(Criteria criteria, Map params) {

		// check for filter entity
		FarmCrops entity = (FarmCrops) params.get(FILTER);

		if (entity != null) {
			criteria.createAlias("farm", "farm").createAlias("farm.farmer", "farmer")
					.createAlias("farmer.village", "village").createAlias("farm.farmDetailedInfo", "detailedInfo")
					.createAlias("cropSeason", "cropSeason");
			criteria.createAlias("procurementVariety", "procurementVariety")
					.createAlias("procurementVariety.procurementProduct", "procurementProduct")
					.createAlias("farmer.samithi", "s");
		}

		if (!ObjectUtil.isListEmpty(entity.getBranchesList())) {
			criteria.add(Restrictions.in("branchId", entity.getBranchesList()));
		}

		if (!StringUtil.isEmpty(entity.getBranchId())) {
			criteria.add(Restrictions.eq("branchId", entity.getBranchId()));
		}

/*		if (params.get(FROM_DATE) != null && params.get(TO_DATE) != null && !ObjectUtil.isEmpty(entity.getFarm())
				&& !ObjectUtil.isEmpty(entity.getFarm().getFarmer())) {
			Date from = (Date) params.get(FROM_DATE);
			// to date
			Date to = (Date) params.get(TO_DATE);
			criteria.add(Restrictions.between("farmer.enrollDate", from, to));
		}*/
		criteria.add(Restrictions.eq("status", ESEAccount.ACTIVE));

		if (!ObjectUtil.isEmpty(entity.getFarm()) && entity.getFarm().getFarmer() != null) {
			if (!StringUtil.isEmpty(entity.getFarm().getFarmer().getFarmerId())) {
				criteria.add(Restrictions.like("farmer.farmerId", entity.getFarm().getFarmer().getFarmerId(),
						MatchMode.EXACT));
			}
		}

		if (!ObjectUtil.isEmpty(entity.getFarm()) && entity.getFarm().getFarmer() != null) {
			if (!StringUtil.isEmpty(entity.getFarm().getFarmer().getFarmerCode())) {
				criteria.add(Restrictions.like("farmer.farmerCode", entity.getFarm().getFarmer().getFarmerCode(),
						MatchMode.EXACT));
			}
		}

		if (!ObjectUtil.isEmpty(entity.getFarm()) && entity.getFarm().getFarmer() != null) {
			if (!StringUtil.isEmpty(entity.getFarm().getFarmer().getFirstName())) {
				criteria.add(Restrictions.like("farmer.firstName", entity.getFarm().getFarmer().getFirstName(),
						MatchMode.ANYWHERE));
			}
		}

		if (!ObjectUtil.isEmpty(entity.getFarm()) && entity.getFarm().getFarmer() != null) {
			if (!StringUtil.isEmpty(entity.getFarm().getFarmer().getLastName())) {
				criteria.add(Restrictions.like("farmer.lastName", entity.getFarm().getFarmer().getLastName(),
						MatchMode.ANYWHERE));
			}
		}

		if (!ObjectUtil.isEmpty(entity.getFarm()) && entity.getFarm().getFarmer() != null) {
			if (!ObjectUtil.isEmpty(entity.getFarm().getFarmer().getVillage())) {
				criteria.add(Restrictions.like("village.code", entity.getFarm().getFarmer().getVillage().getCode(),
						MatchMode.EXACT));
			}
		}

		if (!ObjectUtil.isEmpty(entity.getFarm()) && entity.getFarm().getFarmer() != null) {
			if (!ObjectUtil.isEmpty(entity.getFarm().getFarmer().getSamithi())) {
				if (!StringUtil.isEmpty(entity.getFarm().getFarmer().getSamithi().getName())) {
					criteria.add(
							Restrictions.like("s.name", entity.getFarm().getFarmer().getSamithi().getName(), MatchMode.EXACT));
				}
			}
		}

		if (!ObjectUtil.isEmpty(entity.getCropSeason())) {
			if (!StringUtil.isEmpty(entity.getCropSeason().getCode())) {
				criteria.add(Restrictions.like("cropSeason.code", entity.getCropSeason().getCode(), MatchMode.EXACT));
			}
		}
		/** State Filter **/
		if (!ObjectUtil.isEmpty(entity.getFarm().getFarmer())) {
			if (!StringUtil.isEmpty(entity.getFarm().getFarmer().getStateName())) {
				criteria.createAlias("village.city", "city");
				criteria.createAlias("city.locality", "locality");
				criteria.createAlias("locality.state", "state");
				criteria.add(Restrictions.like("state.code", entity.getFarm().getFarmer().getStateName(),
						MatchMode.ANYWHERE));
			}

			/** Coopertive Filter **/
			if (entity.getFarm().getFarmer().getFpo() != null) {
				criteria.add(
						Restrictions.like("farmer.fpo", entity.getFarm().getFarmer().getFpo(), MatchMode.ANYWHERE));
			}
			/** ICS Filter **/
			if (entity.getFarm().getFarmer().getIcsName() != null) {
				criteria.add(Restrictions.like("farmer.icsName", entity.getFarm().getFarmer().getIcsName(),
						MatchMode.ANYWHERE));
			}
		}

		ProjectionList pList = Projections.projectionList();
		pList.add(Projections.property("branchId")); //0
		pList.add(Projections.property("cropSeason.name")); //1
		pList.add(Projections.property("farmer.farmerCode"));//2
		pList.add(Projections.property("farmer.firstName"));//3
		pList.add(Projections.property("farmer.icsName"));//4
		pList.add(Projections.property("village.name"));//5
		pList.add(Projections.property("farm.farmName"));//6
		pList.add(Projections.property("cropCategory"));//7
		pList.add(Projections.property("procurementProduct.name"));//8
		pList.add(Projections.property("detailedInfo.totalLandHolding"));//9
		pList.add(Projections.property("cultiArea"));//10
		pList.add(Projections.property("type"));//11
		pList.add(Projections.property("stapleLength"));//12
		pList.add(Projections.property("estimatedYield"));//13
		pList.add(Projections.property("farm.farmCode"));//14
		pList.add(Projections.property("detailedInfo.proposedPlantingArea"));//15
		pList.add(Projections.property("procurementProduct.code"));//16
		pList.add(Projections.property("id"));//17
		pList.add(Projections.property("sowingDate"));//18
		pList.add(Projections.property("prodTrees"));//19
		pList.add(Projections.property("affTrees"));//20
		pList.add(Projections.property("noOfTrees"));//21
		pList.add(Projections.property("seedQtyUsed"));//22
		pList.add(Projections.property("detailedInfo.totalLandHolding"));//23
		pList.add(Projections.property("procurementVariety.name"));//24
		pList.add(Projections.property("farm.landTopology"));//25
		pList.add(Projections.property("farmer.id"));//26
		pList.add(Projections.property("procurementProduct.unit"));//27
		criteria.setProjection(pList);

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
