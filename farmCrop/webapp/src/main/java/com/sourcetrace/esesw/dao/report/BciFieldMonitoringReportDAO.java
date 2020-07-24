package com.sourcetrace.esesw.dao.report;

import java.text.SimpleDateFormat;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.order.entity.txn.Cultivation;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farm;

public class BciFieldMonitoringReportDAO extends ReportDAO{
private SimpleDateFormat DataBaseFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
    protected void addExampleFiltering(Criteria criteria, Map params) {
		Farm entity = (Farm) params.get(FILTER);
		criteria.createAlias("farmer", "f").createAlias("f.village", "fv");
		criteria.createAlias("farmCrops", "fc");
		criteria.createAlias("farmDetailedInfo", "fd");
		if(!StringUtil.isEmpty(entity.getFarmCode())){
			criteria.add(Restrictions.like("farmCode",entity.getFarmCode().trim(), MatchMode.EXACT));
		}
		 if(!ObjectUtil.isEmpty(entity.getFarmer()) &&!StringUtil.isEmpty(entity.getFarmer().getFarmerId())){
             criteria.add(Restrictions.like("f.farmerId",entity.getFarmer().getFarmerId().trim(), MatchMode.EXACT));
         }
		 if(!ObjectUtil.isEmpty(entity.getFarmer()) &&!StringUtil.isEmpty(entity.getFarmer().getLastName())){
             criteria.add(Restrictions.like("f.lastName",entity.getFarmer().getLastName().trim(), MatchMode.EXACT));
         }
		 if(!ObjectUtil.isEmpty(entity.getFarmer()) && !ObjectUtil.isEmpty(entity.getFarmer().getVillage()) && !StringUtil.isEmpty(entity.getFarmer().getVillage().getCode())){
      	   criteria.add(Restrictions.like("fv.code", entity.getFarmer().getVillage().getCode()));
         }
		 if(!ObjectUtil.isEmpty(entity.getFarmer()) &&!StringUtil.isEmpty(entity.getFarmer().getBranchId())){
             criteria.add(Restrictions.like("f.branchId",entity.getFarmer().getBranchId().trim(), MatchMode.EXACT));
         }
		
	}
}
