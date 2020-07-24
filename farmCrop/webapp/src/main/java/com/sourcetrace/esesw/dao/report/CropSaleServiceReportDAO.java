package com.sourcetrace.esesw.dao.report;

import java.text.SimpleDateFormat;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.ese.entity.profile.CropSupply;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;

public class CropSaleServiceReportDAO  extends ReportDAO {
	private SimpleDateFormat DataBaseFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.ReportDAO#addExampleFiltering(org.hibernate.Criteria,
	 * java.util.Map)
	 */
	protected void addExampleFiltering(Criteria criteria, Map params) {

		CropSupply entity = (CropSupply) params.get(FILTER);
		if (!ObjectUtil.isEmpty(entity)) {
			
			
			if (!StringUtil.isEmpty(entity.getBranchId())) {
				criteria.add(Restrictions.like("branchId", entity.getBranchId(), MatchMode.EXACT));
			}


			if (entity.getDateOfSale() != null && !"".equals(entity.getDateOfSale())) {
				criteria.add(Restrictions.like("harvestDate", DataBaseFormat.format(entity.getDateOfSale()),
						MatchMode.ANYWHERE));
			}
			Criterion criterionFarmName=null;
			Criterion criterionFarmCode=null;
			if (!ObjectUtil.isEmpty(entity.getFarmCode()) && !StringUtil.isEmpty(entity.getFarmCode())) {
				criterionFarmName=Restrictions.like("farmName", entity.getFarmName().trim(), MatchMode.ANYWHERE);
				criterionFarmCode=Restrictions.like("farmCode", entity.getFarmCode().trim(), MatchMode.ANYWHERE);
			//	criteria.add(Restrictions.like("farmCode", entity.getFarmCode().trim(), MatchMode.ANYWHERE));
				
			}
			
			if(criterionFarmCode!=null )
			{
				LogicalExpression orExpress=Restrictions.or(criterionFarmCode, criterionFarmName);
				criteria.add(orExpress);
			}
			Criterion criterionFrId = null;
			if (!ObjectUtil.isEmpty(entity.getFarmerId()) && !StringUtil.isEmpty(entity.getFarmerId())) {
				//criteria.add(Restrictions.like("farmerId", entity.getFarmerId().trim(), MatchMode.ANYWHERE));
				 criterionFrId=Restrictions.like("farmerId", entity.getFarmerId().trim(), MatchMode.ANYWHERE);
			}
			Criterion criterionFrName = null;
			if (!ObjectUtil.isEmpty(entity.getFarmerName()) && !StringUtil.isEmpty(entity.getFarmerName())) {
				//criteria.add(Restrictions.like("farmerId", entity.getFarmerId().trim(), MatchMode.ANYWHERE));
				 criterionFrName=Restrictions.like("farmerName", entity.getFarmerName().trim(),MatchMode.ANYWHERE);
			}
			if(criterionFrId!=null&&criterionFrName!=null )
			{
			LogicalExpression orExp=Restrictions.or(criterionFrId, criterionFrName);
			criteria.add(orExp);
			}
			
			if (!ObjectUtil.isEmpty(entity.getReceiptInfor()) && !StringUtil.isEmpty(entity.getReceiptInfor())) {
				criteria.add(Restrictions.like("receiptInfor", entity.getReceiptInfor().trim(), MatchMode.ANYWHERE));
			}
			
			if (!ObjectUtil.isEmpty(entity.getVillageName()) && !StringUtil.isEmpty(entity.getVillageName())) {
				DetachedCriteria ownerCriteria = DetachedCriteria.forClass(Farmer.class);
				ownerCriteria.createAlias("village", "v");
				ownerCriteria.setProjection(Property.forName("farmerId"));
				ownerCriteria.add(Restrictions.like("v.name",entity.getVillageName(),MatchMode.ANYWHERE ));
				criteria.add(Property.forName("farmerId").in(ownerCriteria));
			}
			if(!StringUtil.isEmpty(entity.getCurrentSeasonCode())){
        	  	DetachedCriteria subquery = DetachedCriteria.forClass(HarvestSeason.class);
            	subquery.add(Restrictions.like("name",entity.getCurrentSeasonCode(),MatchMode.ANYWHERE));
            	subquery.setProjection(Property.forName("code"));
            	  criteria.add(Property.forName("currentSeasonCode").in(subquery));
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
