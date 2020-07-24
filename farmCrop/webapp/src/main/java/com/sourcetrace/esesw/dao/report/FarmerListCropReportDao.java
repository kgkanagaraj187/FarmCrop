package com.sourcetrace.esesw.dao.report;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import com.ese.entity.profile.CropHarvestDetails;
import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.AFLExport;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;

public class FarmerListCropReportDao extends ReportDAO {
	static String inProcessTypes[] = { "0", "1","2"};
	protected Criteria createCriteria(Map params) {
		String entity = (String) params.get(ENTITY);
		Object object = (Object) params.get(FILTER);
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof FarmCrops) {
				entity = FarmCrops.class.getName();
			}else if(object instanceof Farm){
				entity = Farm.class.getName();
			}
			else if(object instanceof AFLExport){
				entity=AFLExport.class.getName();
			}
		}
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(entity);

		return criteria;
	}

    protected void addDateRangeFiltering(Criteria criteria, Map params) {
    	HttpServletRequest httpRequest = ReflectUtil.getCurrentHttpRequest();
    	String tenantId = (String) httpRequest.getSession().getAttribute(ISecurityFilter.TENANT_ID);
		tenantId = StringUtil.isEmpty(tenantId) ? ISecurityFilter.DEFAULT_TENANT_ID : tenantId;
    }
	
	
	
	protected void addExampleFiltering(Criteria criteria, Map params) {

		// check for filter entity
		Object object = (Object) params.get(FILTER);
		HttpServletRequest httpRequest = ReflectUtil.getCurrentHttpRequest();
		String tenantId = (String) httpRequest.getSession().getAttribute(ISecurityFilter.TENANT_ID);
		tenantId = StringUtil.isEmpty(tenantId) ? ISecurityFilter.DEFAULT_TENANT_ID : tenantId;
		
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof FarmCrops) {
				FarmCrops entity = (FarmCrops) object;
				if (entity != null) {
						criteria.createAlias("procurementVariety", "variety", criteria.LEFT_JOIN);
						criteria.createAlias("variety.procurementProduct", "product", criteria.LEFT_JOIN);
						// criteria.createAlias("samithi", "s", criteria.LEFT_JOIN);
						criteria.createAlias("farm", "fa", criteria.LEFT_JOIN);
						criteria.createAlias("fa.farmer", "fr", criteria.LEFT_JOIN);
						criteria.createAlias("fa.farmDetailedInfo", "fdi", criteria.LEFT_JOIN);
						// criteria.createAlias("fa.farmCrops", "fc",
						// criteria.LEFT_JOIN);
						criteria.createAlias("fa.farmICSConversion", "fics", criteria.LEFT_JOIN);
						criteria.createAlias("cropSeason", "cs");

						criteria.createAlias("fr.village", "v");
						criteria.createAlias("v.city", "city");
						criteria.createAlias("city.locality", "locality");
						criteria.createAlias("locality.state", "state");
						criteria.createAlias("state.country", "country");
						criteria.createAlias("fr.samithi", "s");
						criteria.createAlias("fr.imageInfo", "img", criteria.LEFT_JOIN);

					
					
					
					criteria.add(Restrictions.eq("fr.statusCode", ESETxnStatus.SUCCESS.ordinal()));

					if (!ObjectUtil.isEmpty(entity.getFarm())&& 
							!ObjectUtil.isEmpty(entity.getFarm().getFarmer())&&!StringUtil.isEmpty(entity.getFarm().getFarmer().getBranchId())) {
						criteria.add(Restrictions.eq("fr.branchId", entity.getFarm().getFarmer().getBranchId()));
					}
					/*if (!StringUtil.isEmpty(entity.getCropSeason().getCode())) {
						criteria.add(Restrictions.eq("cs.code", entity.getCropSeason().getCode()));
					}*/
					
					if(!ObjectUtil.isEmpty(entity.getFarm())&& 
							!ObjectUtil.isEmpty(entity.getFarm().getFarmer())&&!StringUtil.isEmpty(entity.getFarm().getFarmer().getSeasonCode())){
						criteria.add(Restrictions.eq("fr.seasonCode", entity.getFarm().getFarmer().getSeasonCode()));
						
					}

					if (!ObjectUtil.isEmpty(entity.getFarm())&& 
							!ObjectUtil.isEmpty(entity.getFarm().getFarmer())&&!ObjectUtil.isListEmpty(entity.getFarm().getFarmer().getBranchesList())) {
						criteria.add(Restrictions.in("fr.branchId", entity.getFarm().getFarmer().getBranchesList()));
					}
					if (!ObjectUtil.isEmpty(entity) && !ObjectUtil.isEmpty(entity.getFarm())
							&& !ObjectUtil.isEmpty(entity.getFarm().getFarmer())) {

						if (!StringUtil.isEmpty(entity.getFarm().getFarmer().getFarmerId())
								&& entity.getFarm().getFarmer().getFarmerId() != null)
							criteria.add(Restrictions.like("fr.farmerId", entity.getFarm().getFarmer().getFarmerId(),
									MatchMode.ANYWHERE));

						if (!StringUtil.isEmpty(entity.getFarm().getFarmer().getFirstName())
								&& entity.getFarm().getFarmer().getFirstName() != null)
							criteria.add(Restrictions.like("fr.firstName", entity.getFarm().getFarmer().getFirstName(),
									MatchMode.ANYWHERE));

						if (!StringUtil.isEmpty(entity.getFarm().getFarmer().getLastName())
								&& entity.getFarm().getFarmer().getLastName() != null)
							criteria.add(Restrictions.like("fr.lastName", entity.getFarm().getFarmer().getLastName(),
									MatchMode.ANYWHERE));

						if (!ObjectUtil.isEmpty(entity.getFarm().getFarmer().getVillage())
								&& entity.getFarm().getFarmer().getVillage() != null) {
							String name = entity.getFarm().getFarmer().getVillage().getCode();
							if (name != null && !"".equals(name)) {
								criteria.add(Restrictions.like("v.code", name, MatchMode.ANYWHERE));
							}
						}
						if (!StringUtil.isEmpty(entity.getFarm().getFarmer().getStateName())
								&& entity.getFarm().getFarmer().getStateName() != null) {
							criteria.add(Restrictions.like("state.code", entity.getFarm().getFarmer().getStateName(),
									MatchMode.ANYWHERE));
						}

						if (!StringUtil.isEmpty(entity.getFarm().getFarmer().getIcsName())
								&& entity.getFarm().getFarmer().getIcsName() != null)
							criteria.add(Restrictions.like("fr.icsName", entity.getFarm().getFarmer().getIcsName(),
									MatchMode.ANYWHERE));

						if (!StringUtil.isEmpty(entity.getFarm().getFarmer().getFpo())
								&& entity.getFarm().getFarmer().getFpo() != null)
							criteria.add(Restrictions.like("fr.fpo", entity.getFarm().getFarmer().getFpo(),
									MatchMode.ANYWHERE));

						if (!StringUtil.isEmpty(entity.getFarm().getFarmer().getGender())
								&& entity.getFarm().getFarmer().getGender() != null)
							criteria.add(Restrictions.eq("fr.gender", entity.getFarm().getFarmer().getGender()));						
						
						if (!StringUtil.isEmpty(entity.getFarm().getFarmer().getCreatedUsername())
								&& entity.getFarm().getFarmer().getCreatedUsername() != null)
							criteria.add(Restrictions.eq("fr.createdUsername", entity.getFarm().getFarmer().getCreatedUsername()));						

						if (!StringUtil.isEmpty(entity.getFarm().getFarmer().getCertificationFilter())) {
							criteria.add(Restrictions.eq("fr.isCertifiedFarmer",
									Integer.parseInt(entity.getFarm().getFarmer().getCertificationFilter())));
						}

						if (!StringUtil.isEmpty(entity.getFarm().getFarmer().getIcsType())
								&& entity.getFarm().getFarmer().getIcsType() != null)
							criteria.add(Restrictions.like("fics.icsType", entity.getFarm().getFarmer().getIcsType(),
									MatchMode.ANYWHERE));

						if (!StringUtil.isEmpty(entity.getFarm().getFarmer().getFilterStatus())
								&& entity.getFarm().getFarmer().getFilterStatus() != null
								&& !"".equals(entity.getFarm().getFarmer().getFilterStatus())) {
							if (entity.getFarm().getFarmer().getFilterStatus().equals("fr.status")) {
								criteria.add(Restrictions.like("fr.status", entity.getFarm().getFarmer().getStatus()));
							}
						}

						if (!StringUtil.isEmpty(entity.getFarm().getFarmer().getMobileNumber())) {
							criteria.add(Restrictions.like("fr.mobileNumber",
									entity.getFarm().getFarmer().getMobileNumber(), MatchMode.ANYWHERE));
						}

						if (!StringUtil.isEmpty(entity.getFarm().getFarmer().getSearchPage())
								&& entity.getFarm().getFarmer().getSearchPage().equalsIgnoreCase("fr.smsList")) {
							criteria.add(Restrictions.and(Restrictions.ne("fr.mobileNumber", ""),
									Restrictions.isNotNull("fr.mobileNumber")));
						}

						if (!StringUtil.isEmpty(entity.getFarm().getFarmer().getFarmerCode())) {
							criteria.add(Restrictions.like("fr.farmerCode",
									entity.getFarm().getFarmer().getFarmerCode(), MatchMode.ANYWHERE));
						}
						
					}
				}
			} else if (object instanceof Farmer) {
				Farmer entity = (Farmer) object;
				criteria.createAlias("village", "v");
				criteria.createAlias("v.city", "city");
				criteria.createAlias("city.locality", "locality");
				criteria.createAlias("locality.state", "state");
				criteria.createAlias("state.country", "country");
				criteria.createAlias("samithi", "s");
				criteria.createAlias("farms", "fa", criteria.LEFT_JOIN);
				criteria.createAlias("fa.farmDetailedInfo", "fdi", criteria.LEFT_JOIN);
				criteria.createAlias("imageInfo", "img", criteria.LEFT_JOIN);
				criteria.createAlias("img.photo", "pht", criteria.LEFT_JOIN);
				criteria.createAlias("fa.farmICSConversion", "fic", criteria.LEFT_JOIN);
				
				if (entity != null) {
					
				criteria.add(Restrictions.eq("statusCode", ESETxnStatus.SUCCESS.ordinal()));
					
				if (!StringUtil.isEmpty(entity.getFirstName())){
						
					criteria.add(Restrictions.like("firstName", entity.getFirstName(),
							MatchMode.ANYWHERE));
				}
				if (!StringUtil.isEmpty(entity.getVillage())){
					String name = entity.getVillage().getCode();
					if (name != null && !"".equals(name)) {

					criteria.add(Restrictions.like("v.code", name,
							MatchMode.ANYWHERE));
					}
				}
				if (tenantId.equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)){
				if (!StringUtil.isEmpty(entity.getOrganicStatus())){
					if (!StringUtil.isEmpty(entity.getOrganicStatus()) && !(entity.getOrganicStatus().equalsIgnoreCase("-1"))){
						if(entity.getOrganicStatus().equalsIgnoreCase("3") && entity.getIsCertifiedFarmer()== 1){
					criteria.add(Restrictions.eq("fic.organicStatus", String.valueOf(entity.getOrganicStatus())));
					
					criteria.add(Restrictions.eq("isCertifiedFarmer",
							Integer.parseInt(String.valueOf(entity.getIsCertifiedFarmer()))));
						}
						else if(entity.getOrganicStatus().equalsIgnoreCase("0") && entity.getIsCertifiedFarmer()== 1){
							criteria.add(Restrictions.in("fic.organicStatus", inProcessTypes));
							criteria.add(Restrictions.eq("isCertifiedFarmer",
									Integer.parseInt(String.valueOf(entity.getIsCertifiedFarmer()))));
						}
						else{
							/*String subQuery = "fic11_.ORGANIC_STATUS IS NULL ";
							criteria.add(Restrictions.sqlRestriction(subQuery));*/
							criteria.add(Restrictions.eq("isCertifiedFarmer",
									Integer.parseInt(String.valueOf(entity.getIsCertifiedFarmer()))));
						}
					}
							
				}
				}
				if (!StringUtil.isEmpty(entity.getStateName())
						&& entity.getStateName() != null) {
					criteria.add(Restrictions.like("state.code", entity.getStateName(),
							MatchMode.ANYWHERE));
				}
				if (!StringUtil.isEmpty(entity.getFpo())
						&& entity.getFpo() != null)
					criteria.add(Restrictions.like("fpo", entity.getFpo(),
							MatchMode.ANYWHERE));
				
				
				if (!StringUtil.isEmpty(entity.getFirstName())){
					
					criteria.add(Restrictions.like("firstName", entity.getFirstName(),
							MatchMode.ANYWHERE));
				}
				
				if (!StringUtil.isEmpty(entity.getFarmerId())){					
					criteria.add(Restrictions.like("farmerId", entity.getFarmerId(),
							MatchMode.ANYWHERE));
				}
				
				
				if (!StringUtil.isEmpty(entity.getFarmerCode())){
					
					criteria.add(Restrictions.like("farmerCode", entity.getFarmerCode(),
							MatchMode.ANYWHERE));
				}
				
				
				
				if (entity.getSamithi() != null && !StringUtil.isEmpty(entity.getSamithi().getCode())){
					criteria.add(Restrictions.like("s.code", entity.getSamithi().getCode(),
							MatchMode.ANYWHERE));
				}
				
				if (!StringUtil.isEmpty(entity.getCreatedUsername())
						&& entity.getCreatedUsername() != null)
					criteria.add(Restrictions.eq("createdUsername", entity.getCreatedUsername()));						

				
				if (!StringUtil.isEmpty(entity.getIcsName())
						&& entity.getIcsName() != null)
					criteria.add(Restrictions.like("icsName", entity.getIcsName(),
							MatchMode.ANYWHERE));
				
				if(!StringUtil.isEmpty(entity.getInsyear()) && entity.getInsyear()!=null){
					String subQuery = " year(fic11_.INSPECTION_DATE) = "+entity.getInsyear();
			criteria.add(Restrictions.sqlRestriction(subQuery));
				}
				
				if (!StringUtil.isEmpty(entity.getGender())
						&& entity.getGender() != null)
					criteria.add(Restrictions.eq("gender", entity.getGender()));
							
				if (!ObjectUtil.isListEmpty(entity.getBranchesList())) {
					criteria.add(Restrictions.in("branchId", entity.getBranchesList()));
				}
				
				if (!StringUtil.isEmpty(entity.getBranchId())){
				    criteria.add(Restrictions.eq("branchId", entity.getBranchId()));
				}
				
				if (!StringUtil.isEmpty(entity.getSeasonCode())
						&& entity.getSeasonCode() != null)
					criteria.add(Restrictions.like("seasonCode", entity.getSeasonCode(),
							MatchMode.ANYWHERE));
				}
			}
			else if (object instanceof AFLExport) {
				AFLExport entity = (AFLExport) object;
				if (entity != null) {
					if (!StringUtil.isEmpty(entity.getIcsName())){
						criteria.add(Restrictions.like("icsName", entity.getIcsName(),
							MatchMode.ANYWHERE));
					}
					if (!ObjectUtil.isListEmpty(entity.getBranchesList())) {
						criteria.add(Restrictions.in("branch", entity.getBranchesList()));
					}
					if (!ObjectUtil.isEmpty(entity.getBranch())) {
						criteria.add(Restrictions.eq("branch", entity.getBranch()));
					}
					
					if (entity.getGroup() != null && !StringUtil.isEmpty(entity.getGroup())){
						criteria.add(Restrictions.eq("group", entity.getGroup()));
					}
					
					if (entity.getVillageName() != null && !StringUtil.isEmpty(entity.getVillageName())){
						criteria.add(Restrictions.eq("villageName", entity.getVillageName()));
					}
					
					if (entity.getStateName() != null && !StringUtil.isEmpty(entity.getStateName())){
						criteria.add(Restrictions.eq("stateName", entity.getStateName()));
					}
					
					if (entity.getGender() != null && !StringUtil.isEmpty(entity.getGender())){
						criteria.add(Restrictions.eq("gender", entity.getGender()));
					}								
				}
			}
			
			else if (object instanceof Farm) {

				Farm farm = (Farm) object;

				criteria.createAlias("farmer", "frmr", Criteria.LEFT_JOIN);
				criteria.createAlias("farmICSConversion", "fic", criteria.LEFT_JOIN);
				if (!ObjectUtil.isEmpty(farm.getFarmer()))
					criteria.add(Restrictions.eq("frmr.id", farm.getFarmer().getId()));
				
				if (tenantId.equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)){
					if (!StringUtil.isEmpty(farm.getFarmer().getOrganicStatus())){
						if (!StringUtil.isEmpty(farm.getFarmer().getOrganicStatus()) && !(farm.getFarmer().getOrganicStatus().equalsIgnoreCase("-1"))){
							if(farm.getFarmer().getOrganicStatus().equalsIgnoreCase("3") && farm.getFarmer().getIsCertifiedFarmer()== 1){
						criteria.add(Restrictions.eq("fic.organicStatus", String.valueOf(farm.getFarmer().getOrganicStatus())));
						criteria.add(Restrictions.eq("frmr.isCertifiedFarmer",
								Integer.parseInt(String.valueOf(farm.getFarmer().getIsCertifiedFarmer()))));
							}
							else if(farm.getFarmer().getOrganicStatus().equalsIgnoreCase("0") && farm.getFarmer().getIsCertifiedFarmer()== 1){
								criteria.add(Restrictions.in("fic.organicStatus", inProcessTypes));
								criteria.add(Restrictions.eq("frmr.isCertifiedFarmer",
										Integer.parseInt(String.valueOf(farm.getFarmer().getIsCertifiedFarmer()))));
							}
							else{
								/*String subQuery = "fic11_.ORGANIC_STATUS IS NULL ";
								criteria.add(Restrictions.sqlRestriction(subQuery));*/
								criteria.add(Restrictions.eq("frmr.isCertifiedFarmer",
										Integer.parseInt(String.valueOf(farm.getFarmer().getIsCertifiedFarmer()))));
							}
						}
								
					}
					}

			}

			
			
			/*else if (object instanceof Farm) {
				Farm entity = (Farm) object;
				criteria.createAlias("farmer", "f");
				criteria.createAlias("f.village", "v");
				criteria.createAlias("v.city", "city");
				criteria.createAlias("city.locality", "locality");
				criteria.createAlias("locality.state", "state");
				criteria.createAlias("state.country", "country");
				criteria.createAlias("f.samithi", "s");
				criteria.createAlias("farmDetailedInfo", "fdi");
				
				
				if (entity != null) {
					
					criteria.add(Restrictions.eq("f.statusCode", ESETxnStatus.SUCCESS.ordinal()));
						
					if (!StringUtil.isEmpty(entity.getFarmer())&&!StringUtil.isEmpty(entity.getFarmer().getFirstName())){
							
						criteria.add(Restrictions.like("f.firstName", entity.getFarmer().getFirstName(),
								MatchMode.ANYWHERE));
					}
					if (!StringUtil.isEmpty(entity.getFarmer())
							&& !StringUtil.isEmpty(entity.getFarmer().getVillage())){
						String name = entity.getFarmer().getVillage().getCode();
						if (name != null && !"".equals(name)) {

						criteria.add(Restrictions.like("v.code", name,
								MatchMode.ANYWHERE));
						}
					}
					if (!StringUtil.isEmpty(entity.getFarmer())&& !StringUtil.isEmpty(entity.getFarmer().getStateName())) {
						criteria.add(Restrictions.like("state.code", entity.getFarmer().getStateName(),
								MatchMode.ANYWHERE));
					}
					
					if (!StringUtil.isEmpty(entity.getFarmer())
							&& !StringUtil.isEmpty(entity.getFarmer().getSamithi())){
					criteria.add(Restrictions.like("s.code", entity.getFarmer().getSamithi().getCode(),
								MatchMode.ANYWHERE));
					}
					
					if (!StringUtil.isEmpty(entity.getFarmDetailedInfo())
							&& !StringUtil.isEmpty(entity.getFarmDetailedInfo().getTotalLandHolding())){
						if(entity.getFarmDetailedInfo().getTotalLandHolding().equalsIgnoreCase("1")){
						criteria.add(Restrictions.lt("fdi.totalLandHolding", entity.getFarmDetailedInfo().getTotalLandHolding()));
						}else{
						criteria.add(Restrictions.ge("fdi.totalLandHolding", entity.getFarmDetailedInfo().getTotalLandHolding()));
						}
					}
					
					if (!StringUtil.isEmpty(entity.getFarmer())
							&& !StringUtil.isEmpty(entity.getFarmer().getGender())
							&& entity.getFarmer().getGender() != null){
						criteria.add(Restrictions.eq("f.gender", entity.getFarmer().getGender()));}

					if (!StringUtil.isEmpty(entity.getFarmer())
							&& !StringUtil.isEmpty(entity.getFarmer().getSeasonCode())
							&& entity.getFarmer().getSeasonCode() != null){
						criteria.add(Restrictions.like("f.seasonCode", entity.getFarmer().getSeasonCode(),
								MatchMode.ANYWHERE));}
					
				
					}
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
	}

}
