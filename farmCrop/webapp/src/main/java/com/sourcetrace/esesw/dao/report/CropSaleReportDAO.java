package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.ese.entity.profile.CropSupply;
import com.ese.entity.profile.CropSupplyDetails;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farmer;

// TODO: Auto-generated Javadoc
/**
 * The Class CropSaleReportDAO.
 */
public class CropSaleReportDAO extends ReportDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.ReportDAO#createCriteria(java.util.Map)
	 */
	protected Criteria createCriteria(Map params) {

		String entity = (String) params.get(ENTITY);
		Object object = (Object) params.get(FILTER);
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof CropSupplyDetails) {
				entity = CropSupplyDetails.class.getName();
			}
		}

		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(entity);

		return criteria;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.ReportDAO#addExampleFiltering(org.hibernate.Criteria,
	 * java.util.Map)
	 */
	protected void addExampleFiltering(Criteria criteria, Map params) {
		Object object = (Object) params.get(FILTER);
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof CropSupply) {
				CropSupply cropSupply = (CropSupply) object;
				criteria.createAlias("cropSupplyDetails", "csd").createAlias("buyerInfo", "bi",
						JoinType.LEFT_OUTER_JOIN);

				criteria.createAlias("cropSupplyDetails.crop", "csdCrop");
				criteria.createAlias("csd.cropSupply", "csdCropSupply");

				// criteria.createAlias("csdCropSupply.buyerInfo",
				// "csdCropSupplyBuyerInfo");*/

				HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
				HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
				String tenantId = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID)
						? ISecurityFilter.DEFAULT_TENANT_ID : "";
				Object object2 = httpSession.getAttribute(ISecurityFilter.IS_MULTI_BRANCH_APP);
				String multibranch = ObjectUtil.isEmpty(object2) ? "" : object2.toString();

				if (!ObjectUtil.isEmpty(request)) {
					tenantId = !StringUtil
							.isEmpty((String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID))
									? (String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID) : "";
				}

				if (multibranch.equals("1")) {
					Object object1 = cropSupply.getBranchesList();
					if (object1 != null) {
						String[] branches = object1.toString().split(",");
						if (!StringUtil.isEmpty(cropSupply.getBranchId())) {
							criteria.add(Restrictions.like("branchId", cropSupply.getBranchId(), MatchMode.EXACT));
						}else{
					
							if (branches != null && branches.length > 0) {
								criteria.add(Restrictions.in("branchId", cropSupply.getBranchesList()));
							}
						}
					}
				} else {
					Object object3 = httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
					String currentBranch1 = ObjectUtil.isEmpty(object3) ? "" : object3.toString();
					if (!StringUtil.isEmpty(currentBranch1)) {
						criteria.add(Restrictions.like("branchId", currentBranch1, MatchMode.EXACT));
					}

				}

				/*if (!StringUtil.isEmpty(cropSupply.getBranchId())) {
					criteria.add(Restrictions.like("branchId", cropSupply.getBranchId(), MatchMode.EXACT));
				}*/

				if (!ObjectUtil.isEmpty(cropSupply.getFarmerId())) {
					criteria.add(Restrictions.eq("farmerId", cropSupply.getFarmerId()));
				}

				/*
				 * if (!ObjectUtil.isEmpty(cropSupply.getFatherName()) &&
				 * !StringUtil.isEmpty(cropSupply.getFatherName())) {
				 * criteria.add(Restrictions.like("csdCropSupply.fatherName",
				 * cropSupply.getFatherName().trim(), MatchMode.ANYWHERE)); }
				 */

				if (!ObjectUtil.isEmpty(cropSupply.getBuyerInfo())) {
					criteria.add(Restrictions.eq("bi.id", cropSupply.getBuyerInfo().getId()));
				}

				if (!ObjectUtil.isEmpty(cropSupply.getBatchLotNo())) {
					criteria.add(Restrictions.like("csd.batchLotNo", cropSupply.getBatchLotNo(), MatchMode.ANYWHERE));
				}

				if (!ObjectUtil.isEmpty(cropSupply.getPoNumber())) {
					criteria.add(
							Restrictions.like("csdCropSupply.poNumber", cropSupply.getPoNumber(), MatchMode.ANYWHERE));
				}

				/*
				 * if (!ObjectUtil.isEmpty(cropSupply.getVillageName())) {
				 * criteria.add(Restrictions.like("cs.villageName",
				 * cropSupply.getVillageName(), MatchMode.ANYWHERE)); }
				 */
				if (cropSupply.getVillage() != null) {
					String name = cropSupply.getVillageName();
					if (name != null && !"".equals(name)) {
						criteria.add(Restrictions.like("csdCropSupply.villageName", name, MatchMode.ANYWHERE));
					}
				}

				/*
				 * if (!StringUtil.isEmpty(cropSupply.getCropType())) {
				 * criteria.add(Restrictions.eq("csd.cropType", cropSupply
				 * .getCropType())); }
				 */
				// criteria.add(Restrictions.eq("csd.cropType",0));
				if (!ObjectUtil.isEmpty(cropSupply.getAgentId())) {
					criteria.add(Restrictions.eq("agentId", cropSupply.getAgentId()));
				}

				if (!StringUtil.isEmpty(cropSupply.getCurrentSeasonCode())) {

					criteria.add(Restrictions.like("currentSeasonCode", cropSupply.getCurrentSeasonCode(),
							MatchMode.ANYWHERE));
				}
				if (!ObjectUtil.isEmpty(cropSupply.getIcsName()) && !StringUtil.isEmpty(cropSupply.getIcsName())) {
					// criteria.add(Restrictions.like("cd.farmer.icsName",
					// entity.getCropHarvest().getIcsname(),
					// MatchMode.ANYWHERE));
					DetachedCriteria farmerCriteria = DetachedCriteria.forClass(Farmer.class);
					farmerCriteria.setProjection(Property.forName("farmerId"));
					farmerCriteria.add(Restrictions.like("icsName", cropSupply.getIcsName(), MatchMode.ANYWHERE));

					criteria.add(Property.forName("farmerId").in(farmerCriteria));
				}
				/** State Filter **/
				if (cropSupply.getStateName() != null) {
					DetachedCriteria ownerCriteria = DetachedCriteria.forClass(Farmer.class);
					ownerCriteria.createAlias("village", "v");
					ownerCriteria.createAlias("v.city", "city");
					ownerCriteria.createAlias("city.locality", "locality");
					ownerCriteria.createAlias("locality.state", "state");
					ownerCriteria.setProjection(Property.forName("farmerId"));
					ownerCriteria.add(Restrictions.like("state.code", cropSupply.getStateName(), MatchMode.ANYWHERE));

					criteria.add(Property.forName("farmerId").in(ownerCriteria));
				}

				/** Coopertive Filter **/
				if (!ObjectUtil.isEmpty(cropSupply.getFpo()) && !StringUtil.isEmpty(cropSupply.getFpo())) {

					DetachedCriteria farmerCriteria = DetachedCriteria.forClass(Farmer.class);
					farmerCriteria.setProjection(Property.forName("farmerId"));
					farmerCriteria.add(Restrictions.like("fpo", cropSupply.getFpo(), MatchMode.ANYWHERE));

					criteria.add(Property.forName("farmerId").in(farmerCriteria));
				}

			} else if (object instanceof CropSupplyDetails) {

				CropSupplyDetails cropSupplyDetails = (CropSupplyDetails) object;

				criteria.createAlias("cropSupply", "cs", Criteria.LEFT_JOIN);

				if (!ObjectUtil.isEmpty(cropSupplyDetails.getCropSupply()))
					criteria.add(Restrictions.eq("cs.id", cropSupplyDetails.getCropSupply().getId()));

			}

		}

	}

}
