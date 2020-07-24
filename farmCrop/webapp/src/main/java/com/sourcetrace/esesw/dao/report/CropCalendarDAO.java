
package com.sourcetrace.esesw.dao.report;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.order.entity.txn.ProcurementDetail;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

import com.sourcetrace.esesw.entity.profile.CropCalendar;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;

import java.util.Map;
import java.util.function.BiConsumer;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.orm.hibernate5.HibernateTemplate;

public class CropCalendarDAO extends ReportDAO {
	
    protected Criteria createCriteria(Map params) {

		String entity = (String) params.get(ENTITY);
		Object object = (Object) params.get(FILTER);
        if (!ObjectUtil.isEmpty(object) && object instanceof CropCalendar) {
            entity = CropCalendar.class.getName();
        }
        Criteria criteria = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(entity);
        return criteria;
    }

    protected void addExampleFiltering(Criteria criteria, Map params) {
        Object object = params.get("example");
        if (!ObjectUtil.isEmpty(object)) {
            if (object instanceof CropCalendar) {
            	CropCalendar cropCalendar = (CropCalendar)object;
            	criteria.createAlias("procurementProduct", "pp");
            	criteria.createAlias("procurementVariety", "pv");
            	
            	if (!StringUtil.isEmpty(cropCalendar.getSeasonCode())) {
            	    
					criteria.add(Restrictions.like("seasonCode", cropCalendar.getSeasonCode(), MatchMode.EXACT));
				}
            	 if(!ObjectUtil.isEmpty(cropCalendar.getProcurementProduct()) && !StringUtil.isEmpty(cropCalendar.getProcurementProduct().getName() !=null)){
                 	criteria.add(Restrictions.like("pp.name", cropCalendar.getProcurementProduct().getName(), MatchMode.ANYWHERE));
                 }
                 
                 if(!ObjectUtil.isEmpty(cropCalendar.getProcurementVariety()) && !StringUtil.isEmpty(cropCalendar.getProcurementVariety().getName() !=null)){
                 	criteria.add(Restrictions.like("pv.name", cropCalendar.getProcurementVariety().getName(), MatchMode.ANYWHERE));
                 }
                 
                 criteria.setProjection(Projections.projectionList()
                		 .add(Projections.groupProperty("pv.id"))
                		 .add(Projections.groupProperty("seasonCode"))
                	); 
            	
              
            } 
        }
    }
}