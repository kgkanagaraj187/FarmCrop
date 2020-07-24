/*
 * TrainingCompletionReportDAO.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.dao.report;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.ese.entity.txn.training.TrainingStatus;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class TrainingCompletionReportDAO extends ReportDAO {

    @Override
    protected void addExampleFiltering(Criteria criteria, Map params) {

        // check for filter entity
        TrainingStatus entity = (TrainingStatus) params.get(FILTER);

        if (entity != null) {
            Date from = (Date) params.get(FROM_DATE);
            // to date
            Date to = (Date) params.get(TO_DATE);

            criteria.add(Restrictions.between("trainingDate", from, to));
            criteria.createAlias("transferInfo", "tr").createAlias("learningGroup", "lg",Criteria.LEFT_JOIN);

            HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
            HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
    		String tenantId = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID) ? ISecurityFilter.DEFAULT_TENANT_ID
    				: "";
    		if (!ObjectUtil.isEmpty(request)) {
    				tenantId = !StringUtil.isEmpty((String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID))
    					? (String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID) : "";
    		}
    		if (tenantId.equalsIgnoreCase("indev")) {
            Object object = httpSession.getAttribute(ISecurityFilter.MAPPED_BRANCHES);
            /*String currentBranch = ObjectUtil.isEmpty(object) ? "" : object.toString();*/
            if(object!=null){
            String[] branches = object.toString().split(",");

            if (branches!=null&&branches.length>0) {
                criteria.add(Restrictions.in("branchId", branches));
            }
    		}}
            
            if (!ObjectUtil.isEmpty(entity.getTransferInfo())
                    && !StringUtil.isEmpty(entity.getTransferInfo().getAgentId())) {
                criteria.add(Restrictions.like("tr.agentId", entity.getTransferInfo().getAgentId(),
                        MatchMode.ANYWHERE));
            }

            if (!ObjectUtil.isEmpty(entity.getLearningGroup())
                    && !StringUtil.isEmpty(entity.getLearningGroup().getCode())) {
                criteria.add(Restrictions.like("lg.code", entity.getLearningGroup().getCode(),
                        MatchMode.EXACT));
            }
            
          /*  if(entity.getTrainingCode()!=null){
                String[] training = entity.getTrainingCode().toString().split(",");

                if (training!=null&&training.length>0) {
                criteria.add(Restrictions.in("trainingCode", training));
              
            }}*/
           
            if(entity.getTrainingCode()!=null){
                String[] training = entity.getTrainingCode().toString().split(",");
        

                if (training!=null&&training.length>0) {
                	  criteria.add(Restrictions.like("trainingCode","%" +entity.getTrainingCode()+"%",
                              MatchMode.ANYWHERE));
              
            }}
            
            
            if(entity.getFarmerIds()!=null){
                String[] famer = entity.getFarmerIds().toString().split(",");

                if (famer!=null&&famer.length>0) {
                criteria.add(Restrictions.like("farmerIds","%"+entity.getFarmerIds()+"%",MatchMode.ANYWHERE));
              
            }
            }
			if (!ObjectUtil.isListEmpty(entity.getBranchesList())) {
				criteria.add(Restrictions.in("branchId", entity.getBranchesList()));
			}
			
			if (!StringUtil.isEmpty(entity.getBranchId())){
			    criteria.add(Restrictions.eq("branchId", entity.getBranchId()));
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

    protected void addDateRangeFiltering(Criteria criteria, Map params) {

    }

}
