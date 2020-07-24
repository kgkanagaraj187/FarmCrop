/*
 * DistributionReportDAO.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.dao.report;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.DoubleType;
import org.hibernate.type.Type;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.order.entity.txn.Distribution;
import com.sourcetrace.eses.order.entity.txn.DistributionDetail;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farmer;

public class DistributionReportDAO extends ReportDAO {

	protected Criteria createCriteria(Map params) {

		// the entity type
		String entity = (String) params.get(ENTITY);
		Object obj = (Object) params.get(FILTER);
		Criteria criteria= getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(entity);
		if (obj instanceof Distribution) {
			Distribution dist = (Distribution) obj;
			if (dist.getTxnType().equals(Distribution.AGENT)) {
				criteria = getHibernateTemplate().getSessionFactory().getCurrentSession()
						.createCriteria(DistributionDetail.class);
				DistributionDetail db = new DistributionDetail();
				db.setDistribution(dist);
				params.put(FILTER, db);
				params.put(ENTITY, DistributionDetail.class.getName());
			}
			

		}

		return criteria;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.ReportDAO#addExampleFiltering(org.hibernate.Criteria,
	 * java.util.Map)
	 */
	@Override
	protected void addExampleFiltering(Criteria criteria, Map params) {

		// check for filter entity
		Object obj = (Object) params.get(FILTER);

		if (obj != null) {

			HttpSession httpSession = ReflectUtil.getCurrentHttpSession();

			Object object = httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
			String currentBranch = ObjectUtil.isEmpty(object) ? "" : object.toString();

			Date from = (Date) params.get(FROM_DATE);
			// to date
			Date to = (Date) params.get(TO_DATE);
			if (obj instanceof Distribution) {
				Distribution entity = (Distribution) obj;
				if (entity != null) {

					/*
					 * if (!ObjectUtil.isEmpty(entity.getAgroTransaction())) {
					 * criteria.createAlias("agroTransaction", "a1"); if
					 * (entity.getTxnType().equals(Distribution.AGENT))
					 * criteria.createAlias("a1.refAgroTransaction", "refTxn");
					 */
					
					criteria.add(Restrictions.ne("status", Distribution.DELETE_STATUS));
					if (!ObjectUtil.isEmpty(from) && !ObjectUtil.isEmpty(to)) {
						criteria.add(Restrictions.between("txnTime", from, to));
					}

					if (entity.getTxnType().equals(Distribution.FARMER)) {
						// criteria.createAlias("village", "v");
						// criteria.createAlias("v.city", "c",
						// Criteria.LEFT_JOIN);

						// criteria.createAlias("ds.product","p",Criteria.LEFT_JOIN);
						criteria.add(Restrictions.in("txnType",
								new Object[] { Distribution.PRODUCT_DISTRIBUTION_TO_FARMER,
										Distribution.PRODUCT_RETURN_FROM_FARMER,
										Distribution.PRODUCT_DISTRIBUTION_FARMER_BALANCE }));
						/*
						 * if (!ObjectUtil.isEmpty(entity.getSeason())) {
						 * 
						 * if (entity.getSeason().getCode() != null &&
						 * !"".equals(entity.getSeason().getCode())) {
						 * criteria.createAlias("season", "s");
						 * criteria.add(Restrictions.like("s.code",
						 * entity.getSeason().getCode(), MatchMode.ANYWHERE)); }
						 * }
						 * 
						 * if (!ObjectUtil.isEmpty(entity.getVillage())) { if
						 * (!StringUtil.isEmpty(entity.getVillage().getName()))
						 * { criteria.add(Restrictions.like("v.name",
						 * entity.getVillage().getName(), MatchMode.ANYWHERE));
						 * } }
						 */
						if (!ObjectUtil.isEmpty(entity.getServicePointId())) {

							criteria.add(Restrictions.like("servicePointId", entity.getServicePointId(),
									MatchMode.EXACT));

						}
						if (!ObjectUtil.isEmpty(entity.getWarehouseName())) {
							criteria.add(
									Restrictions.like("warehouseName", entity.getWarehouseName(), MatchMode.EXACT));
						}
						if (!ObjectUtil.isEmpty(entity.getWarehouseCode())) {
							criteria.add(
									Restrictions.like("warehouseCode", entity.getWarehouseCode(), MatchMode.EXACT));
						}
						if (!StringUtil.isEmpty(entity.getAgentId())) {
							criteria.add(Restrictions.like("agentId", entity.getAgentId(), MatchMode.EXACT));
						}

						if (!StringUtil.isEmpty(entity.getFatherName())) {
							criteria.add(Restrictions.like("fatherName", entity.getFatherName(), MatchMode.EXACT));
						}

						if (!StringUtil.isEmpty(entity.getFarmerName())) {
							criteria.add(Restrictions.like("farmerName", entity.getFarmerName(), MatchMode.ANYWHERE));
						}
						/** State Filter **/
						if (entity.getStateName() != null) {
							DetachedCriteria ownerCriteria = DetachedCriteria.forClass(Farmer.class);
							ownerCriteria.createAlias("village", "v");
							ownerCriteria.createAlias("v.city", "city");
							ownerCriteria.createAlias("city.locality", "locality");
							ownerCriteria.createAlias("locality.state", "state");
							ownerCriteria.setProjection(Property.forName("farmerId"));
							ownerCriteria
									.add(Restrictions.like("state.code", entity.getStateName(), MatchMode.ANYWHERE));

							criteria.add(Property.forName("farmerId").in(ownerCriteria));
						}
						/** ICS Filter **/
						if (!ObjectUtil.isEmpty(entity.getIcsName()) && !StringUtil.isEmpty(entity.getIcsName())) {

							DetachedCriteria farmerCriteria = DetachedCriteria.forClass(Farmer.class);
							farmerCriteria.setProjection(Property.forName("farmerId"));
							farmerCriteria.add(Restrictions.like("icsName", entity.getIcsName(), MatchMode.ANYWHERE));

							criteria.add(Property.forName("farmerId").in(farmerCriteria));
						}
						/** Coopertive Filter **/
						if (!ObjectUtil.isEmpty(entity.getFpo()) && !StringUtil.isEmpty(entity.getFpo())) {

							DetachedCriteria farmerCriteria = DetachedCriteria.forClass(Farmer.class);
							farmerCriteria.setProjection(Property.forName("farmerId"));
							farmerCriteria.add(Restrictions.like("fpo", entity.getFpo(), MatchMode.ANYWHERE));

							criteria.add(Property.forName("farmerId").in(farmerCriteria));
						}
						/**Group Filter Susagri**/
						if (!ObjectUtil.isEmpty(entity.getGroupId()) && entity.getGroupId()!=0) {

							DetachedCriteria farmerCriteria = DetachedCriteria.forClass(Farmer.class);
							farmerCriteria.setProjection(Property.forName("farmerId"));
							farmerCriteria.add(Restrictions.eq("samithi.id", entity.getGroupId()));

							criteria.add(Property.forName("farmerId").in(farmerCriteria));
						}


						/*
						 * if (entity.getProductId() != 0) {
						 * criteria.createAlias("distributionDetails",
						 * "ds").createAlias("ds.product", "p");
						 * criteria.add(Restrictions.eq("p.id",
						 * entity.getProductId())); }
						 */

					} else if (entity.getTxnType().equals(Distribution.AGENT)) {
						criteria.add(Restrictions.in("txnType",
								new Object[] { Distribution.PRODUCT_DISTRIBUTION_TO_FIELDSTAFF /*
																								 * Distribution
																								 * .
																								 * PRODUCT_RETURN_FROM_FIELDSTAFF
																								 * ,Distribution.
																								 * PRODUCT_DISTRIBUTION_TO_FARMER
																								 * ,Distribution.
																								 * PRODUCT_RETURN_FROM_FARMER
																								 */
								}));

						if (!StringUtil.isEmpty(entity.getAgentId())) {
							criteria.add(Restrictions.like("agentId", entity.getAgentId(), MatchMode.EXACT));
						}

						if (!ObjectUtil.isEmpty(entity.getServicePointName())) {

							criteria.add(Restrictions.like("servicePointId", entity.getServicePointName(),
									MatchMode.EXACT));

						}

					}

					if (entity.getProductId() != 0) {
						criteria.createAlias("distributionDetails", "ds").createAlias("ds.product", "p");
						criteria.add(Restrictions.eq("p.id", entity.getProductId()));
					}

					if (!ObjectUtil.isListEmpty(entity.getBranchesList())) {
						criteria.add(Restrictions.in("branchId", entity.getBranchesList()));
					}

					if (!StringUtil.isEmpty(entity.getBranchId())) {
						criteria.add(Restrictions.eq("branchId", entity.getBranchId()));
					}

					if (!StringUtil.isEmpty(entity.getSeasonCode())) {
						criteria.add(Restrictions.like("seasonCode", entity.getSeasonCode(), MatchMode.EXACT));
					}
				}
			} else if (obj instanceof DistributionDetail) {
				DistributionDetail entity = (DistributionDetail) obj;

				criteria.createAlias("distribution", "d");
				criteria.add(Restrictions.between("d.txnTime", from, to));
				if (entity.getDistribution().getTxnType().equals(Distribution.AGENT)) {
					
					// criteria.createAlias("v.city", "c", Criteria.LEFT_JOIN);

					criteria.add(
							Restrictions.in("d.txnType", new Object[] { Distribution.PRODUCT_DISTRIBUTION_TO_FIELDSTAFF,
							/*
							 * Distribution.PRODUCT_RETURN_FROM_FARMER
							 */ }));

					if (!ObjectUtil.isEmpty(entity.getDistribution().getVillage())) {
						criteria.createAlias("d.village", "v");
						if (!StringUtil.isEmpty(entity.getDistribution().getVillage().getName())) {
							criteria.add(Restrictions.like("v.name", entity.getDistribution().getVillage().getName(),
									MatchMode.ANYWHERE));
						}
					}

				} 
				if (!ObjectUtil.isListEmpty(entity.getDistribution().getBranchesList())) {
					criteria.add(Restrictions.in("d.branchId", entity.getDistribution().getBranchesList()));
				}

				if (!StringUtil.isEmpty(entity.getDistribution().getBranchId())) {
					criteria.add(Restrictions.eq("d.branchId", entity.getDistribution().getBranchId()));
				}
					criteria.add(
							Restrictions.in("d.txnType", new Object[] { Distribution.PRODUCT_DISTRIBUTION_TO_FIELDSTAFF,
							/*
							 * Distribution.PRODUCT_RETURN_FROM_FIELDSTAFF
							 * ,Distribution. PRODUCT_DISTRIBUTION_TO_FARMER,
							 * Distribution .PRODUCT_RETURN_FROM_FARMER
							 */ }));

					if (!ObjectUtil.isEmpty(entity.getDistribution().getServicePointName())) {

						criteria.add(Restrictions.like("d.servicePointId",
								entity.getDistribution().getServicePointName(), MatchMode.EXACT));

					}
					if (!StringUtil.isEmpty(entity.getDistribution().getSeasonCode())) {
						criteria.add(Restrictions.like("d.seasonCode", entity.getDistribution().getSeasonCode(), MatchMode.EXACT));
					}

					/*if (!StringUtil.isEmpty(entity.getDistribution().getAgentId())) {
						criteria.add(Restrictions.like("d.agentId", entity.getDistribution().getAgentId(),
								MatchMode.ANYWHERE));
					}*/

				
				
				
				if (!ObjectUtil.isEmpty(entity.getDistribution().getWarehouseName())) {
					criteria.add(
							Restrictions.like("d.warehouseCode", entity.getDistribution().getWarehouseName(), MatchMode.EXACT));
				}

				if (!StringUtil.isEmpty(entity.getDistribution().getAgentId())) {
					criteria.add(Restrictions.like("d.agentId", entity.getDistribution().getAgentId(), MatchMode.EXACT));
				}
				
				

				/*
				 * if (!ObjectUtil.isEmpty(entity.getAgroTransaction()) &&
				 * !StringUtil
				 * .isEmpty(entity.getAgroTransaction().getServicePointName()))
				 * {
				 * criteria.add(Restrictions.like("a1.servicePointId",entity.
				 * getAgroTransaction(). getServicePointName(),
				 * MatchMode.ANYWHERE)); }
				 */

				if (entity.getDistribution().getAgentId() != null
						&& !"".equals(entity.getDistribution().getAgentId())) {
					criteria.add(
							Restrictions.like("d.agentId", entity.getDistribution().getAgentId(), MatchMode.EXACT));
				}
				
				if (entity.getDistribution().getProductId() > 0) {
					criteria.createAlias("product", "p");
					criteria.add(
							Restrictions.eq("p.id", entity.getDistribution().getProductId()));
				}

				if (entity.getDistribution().getFarmerId() != null
						&& !"".equals(entity.getDistribution().getFarmerId())) {
					criteria.add(
							Restrictions.like("d.farmerId", entity.getDistribution().getFarmerId(), MatchMode.EXACT));
				}

				
				if (entity.getDistribution().getFarmerName() != null
						&& !"".equals(entity.getDistribution().getFarmerName())) {
					criteria.add(Restrictions.like("d.farmerName", entity.getDistribution().getFarmerName(),
							MatchMode.EXACT));
				}

				if (!StringUtil.isEmpty(entity.getDistribution().getReceiptNumber()))
					criteria.add(Restrictions.like("d.receiptNumber", entity.getDistribution().getReceiptNumber(),
							MatchMode.EXACT));

			}

		}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.ReportDAO#addDateRangeFiltering(org.hibernate.Criteria,
	 * java.util.Map)
	 */
	protected void addDateRangeFiltering(Criteria criteria, Map params) {

	}
	public Object[] getRowCount(Map params) {
		Object obj = (Object) params.get(FILTER);
		if(obj instanceof Distribution){
        // create the criteria for the given entity type
        Criteria criteria = createCriteria(params);
        ProjectionList list = Projections.projectionList();
        list.add(Projections.rowCount());
        list.add(Projections.sqlProjection("sum(this_.final_amount ) as finalMat", new String[] {"finalMat"} , new Type[] {DoubleType.INSTANCE}));
        list.add(Projections.sqlProjection("sum(this_.PAYMENT_AMT ) as payMat", new String[] {"payMat"} , new Type[] {DoubleType.INSTANCE}));
         list.add(Projections.sqlProjection("(select sum(db.QUANTITY) from distribution_detail db where FIND_IN_SET(db.DISTRIBUTION_ID,GROUP_concat(this_.id))) AS QTY", new String[] {"QTY"} , new Type[] {DoubleType.INSTANCE}));
        
          addGrouping(params, list);
        criteria.setProjection(list);
        // apply the filters, if any
        // add example filtering
        addExampleFiltering(criteria, params);
        // add date range filtering
        addDateRangeFiltering(criteria, params);
        // total row count
        List<Object[]> results = criteria.list();
        String groupPropertyString = (String) params.get(PROJ_GROUP);
        
        return results.get(0);
		}
		else if(obj instanceof DistributionDetail){
			//select sum(dd.quantity),sum(dd.existingQuantity),sum(dd.currentQuantity)
		        Criteria criteria = createCriteria(params);
		        ProjectionList list = Projections.projectionList();
		        list.add(Projections.rowCount());
		        list.add(Projections.sqlProjection("sum(this_.QUANTITY) as qty", new String[] {"qty"} , new Type[] {DoubleType.INSTANCE}));
		        list.add(Projections.sqlProjection("sum(this_.EXISTING_QUANTITY ) as existQty", new String[] {"existQty"} , new Type[] {DoubleType.INSTANCE}));
		        list.add(Projections.sqlProjection("sum(this_.CURRENT_QUANTITY ) AS curQty", new String[] {"curQty"} , new Type[] {DoubleType.INSTANCE}));
		        addGrouping(params, list);
		        criteria.setProjection(list);
		        addExampleFiltering(criteria, params);
		        addDateRangeFiltering(criteria, params);
		        List<Object[]> results = criteria.list();
		        String groupPropertyString = (String) params.get(PROJ_GROUP);
		        
		        return results.get(0);
		}
		else{
			return null;
		}
    }
}
