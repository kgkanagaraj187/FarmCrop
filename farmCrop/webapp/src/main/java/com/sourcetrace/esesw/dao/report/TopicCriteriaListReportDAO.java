/*
 * TopicCriteriaListReportDAO.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.ese.entity.txn.training.Topic;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class TopicCriteriaListReportDAO extends ReportDAO {

    protected void addExampleFiltering(Criteria criteria, Map params) {

        Topic topic = (Topic) params.get(FILTER);
        criteria.createAlias("topicCategory", "topicCategory", Criteria.LEFT_JOIN);
        if (!ObjectUtil.isEmpty(topic)) {
        	
        	if (!ObjectUtil.isListEmpty(topic.getBranchesList())) {
				criteria.add(Restrictions.in("branchId", topic.getBranchesList()));
			}
			
			if (!StringUtil.isEmpty(topic.getBranchId())){
			    criteria.add(Restrictions.eq("branchId", topic.getBranchId()));
			}

            if (!StringUtil.isEmpty(topic.getCode())) {
                criteria.add(Restrictions.like("code", topic.getCode().trim(), MatchMode.ANYWHERE));
            }
            if (!StringUtil.isEmpty(topic.getPrinciple())) {
                criteria.add(Restrictions.like("principle", topic.getPrinciple().trim(),
                        MatchMode.ANYWHERE));
            }
            
            if (!StringUtil.isEmpty(topic.getDes())) {
                criteria.add(Restrictions.like("des", topic.getDes().trim(), MatchMode.ANYWHERE));
            }
            
            
            if (!ObjectUtil.isEmpty(topic.getTopicCategory())) {
                if (!StringUtil.isEmpty(topic.getTopicCategory().getName())) {
                    criteria.add(Restrictions.like("topicCategory.name", topic.getTopicCategory()
                            .getName().trim(), MatchMode.ANYWHERE));
                }
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
