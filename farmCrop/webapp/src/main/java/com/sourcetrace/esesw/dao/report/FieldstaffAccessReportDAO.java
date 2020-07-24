package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.TransactionType;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.order.entity.txn.AgentAccessLog;
import com.sourcetrace.eses.order.entity.txn.AgentAccessLogDetail;
import com.sourcetrace.eses.service.AgentService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class FieldstaffAccessReportDAO extends ReportDAO {
    private static final String LOGOUT_TXN="320";
    private static final String Farmer_ENRO="357";
   // static String pratibhatxnTypes[] = { "308", "314","363","364","360"};
    @Autowired
    private IPreferencesService preferncesService;
     
    protected Criteria createCriteria(Map params) {

        String entity = (String) params.get(ENTITY);
        Object object = (Object) params.get(FILTER);
        if (!ObjectUtil.isEmpty(object)) {
            if (object instanceof AgentAccessLogDetail) {
                entity = AgentAccessLogDetail.class.getName();
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
        	
            if(obj instanceof AgentAccessLog){
                AgentAccessLog agentAccessLog = (AgentAccessLog)obj;
                if(!StringUtil.isEmpty(agentAccessLog.getSerialNo())){
                    criteria.add(Restrictions.eq("serialNo",agentAccessLog.getSerialNo()));
                }
                criteria.createAlias("agentAccessLogDetails", "acl");
                String types = preferncesService.findPrefernceByName(ESESystem.TXN_TYPES_MOBILE_ACTIVITY);
        		String[] txnTypes=types.split(",");
                criteria.add(Restrictions.in("acl.txnType",txnTypes));
                if(!StringUtil.isEmpty(agentAccessLog.getProfileId())){
                    DetachedCriteria agentProfileCriteria = DetachedCriteria.forClass(Agent.class);
                    agentProfileCriteria.setProjection(Property.forName("profileId"));
                    agentProfileCriteria.add(Restrictions.eq("profileId",agentAccessLog.getProfileId()));
                    criteria.add(Property.forName("profileId").in(agentProfileCriteria));
                }
               HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
                HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
        		String tenantId = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID) ? ISecurityFilter.DEFAULT_TENANT_ID
        				: "";
        		Object object2 = httpSession.getAttribute(ISecurityFilter.IS_MULTI_BRANCH_APP);
        		String multibranch = ObjectUtil.isEmpty(object2) ? "" : object2.toString();
        		
        		if (!ObjectUtil.isEmpty(request)) {
        				tenantId = !StringUtil.isEmpty((String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID))
        					? (String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID) : "";
        		}
        		
        		
        		if (multibranch.equals("1")) {
        			Object object1 = httpSession.getAttribute(ISecurityFilter.MAPPED_BRANCHES);
        			if (object1 != null) {
        				String[] branches = object1.toString().split(",");

        				if (branches != null && branches.length > 0) {
        					criteria.add(Restrictions.in("branchId", branches));
        				}
        			}
        		} else {
        			Object object3= httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
        			String currentBranch1 = ObjectUtil.isEmpty(object3) ? "" : object3.toString();
        			if (!StringUtil.isEmpty(currentBranch1)) {
        				criteria.add(Restrictions.like("branchId", currentBranch1, MatchMode.EXACT));
        			}
        		}

                
                if (!ObjectUtil.isListEmpty(agentAccessLog.getBranchesList())) {
           				criteria.add(Restrictions.in("branchId", agentAccessLog.getBranchesList()));
           		}
           			
           		if (!StringUtil.isEmpty(agentAccessLog.getBranchId())){
           			    criteria.add(Restrictions.eq("branchId", agentAccessLog.getBranchId()));
           		}
                
                if(!StringUtil.isEmpty(agentAccessLog.getAgentName())){
                   /* criteria.add(Restrictions.eq("profileId",agentAccessLog.getProfileId()));*/
                    DetachedCriteria agentCriteria = DetachedCriteria.forClass(Agent.class);
                    agentCriteria.setProjection(Property.forName("id"));
                    agentCriteria.createAlias("personalInfo", "pf");
                    agentCriteria.add(Restrictions.eq("pf.firstName",agentAccessLog.getAgentName()));
                    criteria.add(Property.forName("agentName").in(agentCriteria));
                }
            }else if(obj instanceof AgentAccessLogDetail){
                AgentAccessLogDetail agentAccessLogDetail = (AgentAccessLogDetail)obj;
                criteria.createAlias("agentAccessLog", "acl");
                criteria.add(Restrictions.ne("txnType",LOGOUT_TXN));
                criteria.add(Restrictions.eq("acl.id", agentAccessLogDetail.getAgentAccessLog().getId()));
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
