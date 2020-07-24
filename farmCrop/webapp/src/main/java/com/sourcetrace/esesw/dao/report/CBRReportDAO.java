package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.ese.entity.profile.ViewFarmerFarm;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farm;

public class CBRReportDAO extends ReportDAO {
	
	protected Criteria createCriteria(Map params) {
		String entity = (String) params.get(ENTITY);
		Object object = (Object) params.get(FILTER);
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof ViewFarmerFarm) {
				entity = ViewFarmerFarm.class.getName();
			}
		}
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(entity);

		return criteria;
	}
	
	protected void addExampleFiltering(Criteria criteria, Map params) {

		Object obj = params.get(FILTER);
		Farm entity = null;
		if (obj instanceof Farm) {
			entity = (Farm) params.get(FILTER);
		} else if (obj instanceof ViewFarmerFarm) {
			
		}
		if (entity != null) {

			criteria.createAlias("farmer", "f").createAlias("f.village", "v");
			criteria.createAlias("f.samithi", "s");
			criteria.createAlias("farmDetailedInfo", "fdi");

			/*
			 * if (!StringUtil.isEmpty(entity.getFaimingtype1())) {
			 * ProjectionList pList = Projections.projectionList();
			 * pList.add(Projections.sum("totalCoc"));
			 * pList.add(Projections.sum("interCropIncome"));
			 * pList.add(Projections.sum("agriIncome"));
			 * pList.add(Projections.sum("otherSourcesIncome"));
			 * pList.add(Projections.sum("cottonQty"));
			 * pList.add(Projections.sum("unitSalePrice"));
			 * pList.add(Projections.sum("unitSalePrice"));
			 * pList.add(Projections.sum("saleCottonIncome"));
			 * pList.add(Property.forName("farmId"));
			 * 
			 * DetachedCriteria ownerCriteria =
			 * DetachedCriteria.forClass(Cultivation.class);
			 * ownerCriteria.setProjection(pList);
			 * criteria.add(Property.forName("farmCode").in(ownerCriteria)); }
			 */

			HttpSession httpSession = ReflectUtil.getCurrentHttpSession();

			Object object = httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
			String currentBranch = ObjectUtil.isEmpty(object) ? "" : object.toString();

			if (!StringUtil.isEmpty(currentBranch)) {
				criteria.add(Restrictions.eq("f.branchId", currentBranch));
			}

			if (!ObjectUtil.isEmpty(entity.getFarmer())) {

				if (entity.getFarmer().getFarmerCode() != null)
					criteria.add(
							Restrictions.like("f.farmerCode", entity.getFarmer().getFarmerCode(), MatchMode.ANYWHERE));
				if (entity.getFarmer().getFirstName() != null)
					criteria.add(
							Restrictions.like("f.firstName", entity.getFarmer().getFirstName(), MatchMode.ANYWHERE));

				if (entity.getFarmer().getLastName() != null)
					criteria.add(Restrictions.like("f.lastName", entity.getFarmer().getLastName(), MatchMode.ANYWHERE));

				if (entity.getFarmer().getVillage() != null) {
					String name = entity.getFarmer().getVillage().getCode();
					if (name != null && !"".equals(name)) {
						criteria.add(Restrictions.like("v.code", name, MatchMode.ANYWHERE));
					}
				}

				/*
				 * if (!StringUtil.isEmpty(entity.getFarmer().getBranchId())) {
				 * criteria.add(Restrictions.like("f.branchId",
				 * entity.getFarmer().getBranchId(), MatchMode.EXACT)); }
				 */

				if (!ObjectUtil.isListEmpty(entity.getFarmer().getBranchesList())) {
					criteria.add(Restrictions.in("f.branchId", entity.getFarmer().getBranchesList()));
				}

				if (!StringUtil.isEmpty(entity.getFarmer().getBranchId())) {
					criteria.add(Restrictions.eq("f.branchId", entity.getFarmer().getBranchId()));
				}

				if (!StringUtil.isEmpty(entity.getFarmer().getIcsName())) {
					criteria.add(Restrictions.like("f.icsName", entity.getFarmer().getIcsName(), MatchMode.ANYWHERE));
				}

				if (!ObjectUtil.isEmpty(entity.getFarmer())) {

					if (!ObjectUtil.isEmpty(entity.getFarmer().getSamithi())) {
						if (!StringUtil.isEmpty(entity.getFarmer().getSamithi().getName())) {
							criteria.add(Restrictions.like("s.name", entity.getFarmer().getSamithi().getName(),
									MatchMode.EXACT));
						}
					}
				}

				if (entity.getFarmer().getFarmerId() != null)
					criteria.add(Restrictions.like("f.farmerId", entity.getFarmer().getFarmerId(), MatchMode.EXACT));

				if (!StringUtil.isEmpty(entity.getFarmer().getSeasonCode())) {
					criteria.add(Restrictions.eq("f.seasonCode", entity.getFarmer().getSeasonCode()));
				}

			}
			/** State Filter **/
			if (!ObjectUtil.isEmpty(entity.getFarmer())) {
				if (!StringUtil.isEmpty(entity.getFarmer().getStateName())) {
					criteria.createAlias("v.city", "city");
					criteria.createAlias("city.locality", "locality");
					criteria.createAlias("locality.state", "state");
					criteria.add(
							Restrictions.like("state.code", entity.getFarmer().getStateName(), MatchMode.ANYWHERE));
				}

				/** Coopertive Filter **/
				if (entity.getFarmer().getFpo() != null) {
					criteria.add(Restrictions.like("f.fpo", entity.getFarmer().getFpo(), MatchMode.ANYWHERE));
				}
			}

			if (!StringUtil.isEmpty(entity.getFarmName())) {
				criteria.add(Restrictions.like("farmName", entity.getFarmName(), MatchMode.ANYWHERE));
			}

		}
	}

}
