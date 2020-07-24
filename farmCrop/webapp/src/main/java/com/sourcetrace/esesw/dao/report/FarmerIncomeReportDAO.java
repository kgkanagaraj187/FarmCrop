package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.order.entity.txn.Cultivation;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farmer;


public class FarmerIncomeReportDAO extends ReportDAO {
	protected void addExampleFiltering(Criteria criteria, Map params) { 
		Cultivation entity = (Cultivation) params.get(FILTER);

		/*if (entity != null) {
			 criteria.createAlias("farmer", "farmer").createAlias("farmer.village", "village");
			 criteria.createAlias("farmer.samithi", "s");
		}*/
			criteria.add(Restrictions.like("txnType", "1"));
		HttpSession httpSession = ReflectUtil.getCurrentHttpSession();

		Object object = httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
		String currentBranch = ObjectUtil.isEmpty(object) ? "" : object.toString();
		if (!ObjectUtil.isEmpty(entity.getFarmerName())) {
			criteria.add(Restrictions.like("farmerName", entity
					.getFarmerName(), MatchMode.ANYWHERE));
		}
				if(!ObjectUtil.isEmpty(entity.getVillageId())){
				criteria.add(Restrictions.eq("villageId", entity.getVillageId()));
			    }
				if(!ObjectUtil.isEmpty(entity.getCurrentSeasonCode())){
					criteria.add(Restrictions.eq("currentSeasonCode", entity.getCurrentSeasonCode()));
				}
				if(!ObjectUtil.isEmpty(entity.getBranchId())){
					criteria.add(Restrictions.eq("branchId", entity.getBranchId()));
				}
				if(!ObjectUtil.isEmpty(entity.getFarmerId())){
					criteria.add(Restrictions.eq("farmerId", entity.getFarmerId()));
				}
				/**State Filter**/
				if(entity.getStateName()!=null){
				    DetachedCriteria ownerCriteria = DetachedCriteria.forClass(Farmer.class);
	                ownerCriteria.createAlias("village", "v");
	                ownerCriteria.createAlias("v.city", "city");
	                ownerCriteria.createAlias("city.locality", "locality");
	                ownerCriteria.createAlias("locality.state", "state");
	                ownerCriteria.setProjection(Property.forName("farmerId"));
	                ownerCriteria.add(Restrictions.like("state.code",entity.getStateName(),MatchMode.ANYWHERE ));
	                
	                criteria.add(Property.forName("farmerId").in(ownerCriteria));
	            }
				 /**ICS  Filter**/   
				 if (!ObjectUtil.isEmpty(entity.getIcsName())
	                     && !StringUtil.isEmpty(entity.getIcsName())) {
	              
	              DetachedCriteria farmerCriteria = DetachedCriteria.forClass(Farmer.class);
	               farmerCriteria.setProjection(Property.forName("farmerId"));
	               farmerCriteria.add(Restrictions.like("icsName", entity.getIcsName(),
	                       MatchMode.ANYWHERE));
	               
	               criteria.add(Property.forName("farmerId").in(farmerCriteria));
	             }
	            /**Coopertive Filter**/
				 if (!ObjectUtil.isEmpty(entity.getFpo())
	                     && !StringUtil.isEmpty(entity.getFpo())) {
	                
	              DetachedCriteria farmerCriteria = DetachedCriteria.forClass(Farmer.class);
	               farmerCriteria.setProjection(Property.forName("farmerId"));
	               farmerCriteria.add(Restrictions.like("fpo", entity.getFpo(),
	                       MatchMode.ANYWHERE));
	               
	               criteria.add(Property.forName("farmerId").in(farmerCriteria));
	             }

		/*if (!StringUtil.isEmpty(currentBranch)) {
			criteria.add(Restrictions.like("farmer.branchId", currentBranch, MatchMode.EXACT));
		}
		if(params.get(FROM_DATE)!=null && params.get(TO_DATE)!=null && !ObjectUtil.isEmpty(entity.getFarmer())){
			Date from = (Date) params.get(FROM_DATE);
			// to date
			Date to = (Date) params.get(TO_DATE);
			criteria.add(Restrictions.between("farmer.enrollDate", from, to));
		}
		if (!ObjectUtil.isEmpty(entity.getFarmer())) {
			if(!StringUtil.isEmpty(entity.getFarmer().getBranchId())){
			criteria.add(Restrictions.like("farmer.branchId", entity.getFarmer()
					.getBranchId(), MatchMode.EXACT));
			}
		}
		
			if (!ObjectUtil.isEmpty(entity.getFarmer())) {
				if(!StringUtil.isEmpty(entity.getFarmer().getFarmerId())){
				criteria.add(Restrictions.like("farmer.farmerId", entity.getFarmer()
						.getFarmerId(), MatchMode.ANYWHERE));
				}
			}
			
			if (!ObjectUtil.isEmpty(entity.getFarmer())) {
				if(!StringUtil.isEmpty(entity.getFarmer().getFarmerCode())){
				criteria.add(Restrictions.like("farmer.farmerCode", entity.getFarmer()
						.getFarmerCode(), MatchMode.EXACT));
				}
			}
			
			
			if (!ObjectUtil.isEmpty(entity.getFarmer())) {
                if(!StringUtil.isEmpty(entity.getFarmer().getLastName())){
                criteria.add(Restrictions.like("farmer.lastName", entity.getFarmer()
                        .getLastName(), MatchMode.EXACT));
                }
            }
			
			if (!ObjectUtil.isEmpty(entity.getFarmer())) {
                if(!StringUtil.isEmpty(entity.getFarmer().getFirstName())){
                criteria.add(Restrictions.like("farmer.firstName", entity.getFarmer()
                        .getFirstName(), MatchMode.EXACT));
                }
            }
			
			if (!ObjectUtil.isEmpty(entity.getFarmer()) && !ObjectUtil.isEmpty(entity.getFarmer().getVillage())) {
				if(!ObjectUtil.isEmpty(entity.getFarmer().getVillage().getCode())){
				criteria.add(Restrictions.like("village.code", entity
						.getFarmer().getVillage().getCode(), MatchMode.EXACT));
				}
			}
			
			
			
			 if(!ObjectUtil.isEmpty(entity.getFarmer())) 
             {
				 if(!StringUtil.isEmpty(entity.getFarmer().getSeasonCode()))
				 {
					 criteria.add(Restrictions.like("farmer.seasonCode", entity.getFarmer().getSeasonCode(),MatchMode.ANYWHERE));
				 }
             }
			 
			 //Farmer Income in services Filters start
			 
			 
			 if (!ObjectUtil.isEmpty(entity.getFarmer()) && !ObjectUtil.isEmpty(entity.getFarmer().getVillage())) {
					if(!ObjectUtil.isEmpty(entity.getFarmer().getVillage().getName())){
					criteria.add(Restrictions.like("village.name", entity
							.getFarmer().getVillage().getName(), MatchMode.ANYWHERE));
					}
				}
			 
			 
			 if (!ObjectUtil.isEmpty(entity.getFarmer())) {
					if(!StringUtil.isEmpty(entity.getFarmer().getFirstName())){
					criteria.add(Restrictions.like("farmer.firstName", entity.getFarmer()
							.getFirstName(), MatchMode.ANYWHERE));
					}
				}*/
			 

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
