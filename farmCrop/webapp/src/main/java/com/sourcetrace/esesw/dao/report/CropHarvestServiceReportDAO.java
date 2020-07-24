package com.sourcetrace.esesw.dao.report;

import java.text.SimpleDateFormat;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.ese.entity.profile.CropHarvest;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;

public class CropHarvestServiceReportDAO extends ReportDAO{
	



	private SimpleDateFormat DataBaseFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.ReportDAO#addExampleFiltering(org.hibernate.Criteria,
	 * java.util.Map)
	 */
	protected void addExampleFiltering(Criteria criteria, Map params) {

		CropHarvest entity = (CropHarvest) params.get(FILTER);
		if (!ObjectUtil.isEmpty(entity)) {
			
			
			if (!StringUtil.isEmpty(entity.getBranchId())) {
				criteria.add(Restrictions.like("branchId", entity.getBranchId(), MatchMode.EXACT));
			}


			if (entity.getHarvestDate() != null && !"".equals(entity.getHarvestDate())) {
				criteria.add(Restrictions.like("harvestDate", DataBaseFormat.format(entity.getHarvestDate()),
						MatchMode.ANYWHERE));
			}

			if (!ObjectUtil.isEmpty(entity.getFarmName()) && !StringUtil.isEmpty(entity.getFarmName())) {
				criteria.add(Restrictions.like("farmName", entity.getFarmName().trim(), MatchMode.ANYWHERE));
			}

			if (!ObjectUtil.isEmpty(entity.getFarmerName()) && !StringUtil.isEmpty(entity.getFarmerName())) {
				criteria.add(Restrictions.like("farmerName", entity.getFarmerName().trim(), MatchMode.ANYWHERE));
			}
			
			if (!ObjectUtil.isEmpty(entity.getReceiptNo()) && !StringUtil.isEmpty(entity.getReceiptNo())) {
				criteria.add(Restrictions.like("receiptNo", entity.getReceiptNo().trim(), MatchMode.ANYWHERE));
			}
			
			if (!ObjectUtil.isEmpty(entity.getVillage()) && !StringUtil.isEmpty(entity.getVillage())) {
				DetachedCriteria ownerCriteria = DetachedCriteria.forClass(Farmer.class);
				ownerCriteria.createAlias("village", "v");
				ownerCriteria.setProjection(Property.forName("farmerId"));
				ownerCriteria.add(Restrictions.like("v.name",entity.getVillage(),MatchMode.ANYWHERE ));
				criteria.add(Property.forName("farmerId").in(ownerCriteria));
			}
			if(!StringUtil.isEmpty(entity.getSeasonCode())){
        	  	DetachedCriteria subquery = DetachedCriteria.forClass(HarvestSeason.class);
            	subquery.add(Restrictions.like("name",entity.getSeasonCode(),MatchMode.ANYWHERE));
            	subquery.setProjection(Property.forName("code"));
            	  criteria.add(Property.forName("seasonCode").in(subquery));
            }

		}

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
