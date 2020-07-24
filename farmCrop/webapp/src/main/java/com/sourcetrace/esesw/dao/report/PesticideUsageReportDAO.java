package com.sourcetrace.esesw.dao.report;

import java.text.SimpleDateFormat;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.order.entity.txn.PeriodicInspection;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class PesticideUsageReportDAO extends ReportDAO{
	 private SimpleDateFormat DataBaseFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 @Override
	    protected void addExampleFiltering(Criteria criteria, Map params) {
		 PeriodicInspection entity = (PeriodicInspection) params.get(FILTER);
		 criteria.createAlias("farm", "f").createAlias("f.farmer", "fm").createAlias("fm.village", "fv");
		 if (entity.getInspectionDate() != null && !"".equals(entity.getInspectionDate())) {
             criteria.add(Restrictions.like("inspectionDate", DataBaseFormat.format(entity.getInspectionDate()), MatchMode.ANYWHERE));
         }
		 if(!StringUtil.isEmpty(entity.getSeasonCode()) && (entity.getSeasonCode()!=null)){
             criteria.add(Restrictions.like("seasonCode",entity.getSeasonCode(), MatchMode.EXACT));
         }
		 
		 if(!ObjectUtil.isEmpty(entity.getFarm()) && !StringUtil.isEmpty(entity.getFarm().getFarmCode())){
             criteria.add(Restrictions.like("f.farmCode",entity.getFarm().getFarmCode().trim(), MatchMode.EXACT));
         }
		 
		 if(!ObjectUtil.isEmpty(entity.getFarm()) && !StringUtil.isEmpty(entity.getFarm().getFarmer())&&!StringUtil.isEmpty(entity.getFarm().getFarmer().getFarmerId())){
             criteria.add(Restrictions.like("fm.farmerId",entity.getFarm().getFarmer().getFarmerId().trim(), MatchMode.EXACT));
         }
		 if(!ObjectUtil.isEmpty(entity.getFarm()) && !ObjectUtil.isEmpty(entity.getFarm().getFarmer()) && !ObjectUtil.isEmpty(entity.getFarm().getFarmer().getVillage()) && !StringUtil.isEmpty(entity.getFarm().getFarmer().getVillage().getCode())){
      	   criteria.add(Restrictions.like("fv.code", entity.getFarm().getFarmer().getVillage().getCode()));
         }
         
         if(!ObjectUtil.isEmpty(entity.getFarm()) && !StringUtil.isEmpty(entity.getFarm().getFarmer())&&!StringUtil.isEmpty(entity.getFarm().getFarmer().getLastName())){
             criteria.add(Restrictions.like("fm.lastName",entity.getFarm().getFarmer().getLastName().trim(), MatchMode.EXACT));
         }
		 
         if(!ObjectUtil.isEmpty(entity.getInspectionType())) {
             criteria.add(Restrictions.like("inspectionType",entity.getInspectionType().trim(), MatchMode.EXACT));
         }
	 }
}
