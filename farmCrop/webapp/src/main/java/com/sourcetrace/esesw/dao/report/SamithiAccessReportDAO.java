package com.sourcetrace.esesw.dao.report;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.TransactionType;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.AgentAccessLog;
import com.sourcetrace.eses.order.entity.txn.AgentAccessLogDetail;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.PeriodicInspection;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farmer;

public class SamithiAccessReportDAO extends ReportDAO {
    private static final String LOGOUT_TXN="320";
    protected Criteria createCriteria(Map params) {

        String entity = (String) params.get(ENTITY);
        Object object = (Object) params.get(FILTER);
        if (!ObjectUtil.isEmpty(object)) {
            if (object instanceof AgroTransaction) {
                entity = AgroTransaction.class.getName();
            }
        }
        Criteria criteria = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createCriteria(entity);
        
       
        return criteria;
    }
    
    @Override
    protected void addExampleFiltering(Criteria criteria, Map params) {
        Object obj = (Object) params.get(FILTER);
        if(!ObjectUtil.isEmpty(obj)){
            if(obj instanceof Warehouse){
                Warehouse samithiAccessLog = (Warehouse)obj;
                if(StringUtil.isEmpty(samithiAccessLog.getTypez())){
                    criteria.add(Restrictions.eq("typez",1));
                }
                if(!StringUtil.isEmpty(samithiAccessLog.getId()) && samithiAccessLog.getId()!=0){
                    criteria.add(Restrictions.eq("id",samithiAccessLog.getId()));
                }
                                            
            }else if(obj instanceof AgroTransaction){
                AgroTransaction agrotxn = (AgroTransaction)obj;    
              
               criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("txnType"))); 
               criteria.add(Restrictions.in("agentId",agrotxn.getAgentList()));
               criteria.add(Restrictions.in("txnType", new String[]{"314","316","334","344","345","334P","334D"}));
               //criteria.add(Restrictions.in("txnType", agrotxn.getTxnTypeList()));
            
               /* DetachedCriteria periodicCriteria = DetachedCriteria.forClass(PeriodicInspection.class);
               periodicCriteria.setProjection(Property.forName("txnType"));     
               
                periodicCriteria.add(Restrictions.in("createdUserName",agrotxn.getAgentList()));
                criteria.add(Property.forName("txnType").in(periodicCriteria));*/
             
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
