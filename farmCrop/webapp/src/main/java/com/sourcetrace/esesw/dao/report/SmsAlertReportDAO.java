/*
 * FarmerReportDAO.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ese.entity.sms.SMSHistoryDetail;
import com.ese.entity.sms.SmsGroupDetail;
import com.ese.entity.sms.SmsGroupHeader;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.umgmt.entity.User;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farmer;


@Repository
public class SmsAlertReportDAO extends ReportDAO {

    /**
     * Instantiates a new farmer report dao.
     * @param sessionFactory the session factory
     */
    @Autowired
    public SmsAlertReportDAO(SessionFactory sessionFactory) {

        this.setSessionFactory(sessionFactory);
    }

    @SuppressWarnings("rawtypes")
    protected void addExampleFiltering(Criteria criteria, Map params) {

        Object object = params.get(FILTER);
        if (!ObjectUtil.isEmpty(object)) {
            if (object instanceof SMSHistoryDetail) {
                SMSHistoryDetail entity = (SMSHistoryDetail) object;
                criteria.createAlias("smsHistory", "sh");
                if (!StringUtil.isEmpty(entity.getReceiverName())) {
                    DetachedCriteria farmerCriteria = DetachedCriteria.forClass(Farmer.class);
                    farmerCriteria.setProjection(Property.forName("id"));
                    farmerCriteria.add(Restrictions.like("name", entity.getReceiverName(),
                            MatchMode.ANYWHERE));

                    DetachedCriteria agentCriteria = DetachedCriteria.forClass(Agent.class);
                    agentCriteria.setProjection(Property.forName("id"));
                    agentCriteria.createAlias("personalInfo", "pf");
                    agentCriteria.add(Restrictions.like("pf.firstName", entity.getReceiverName(),
                            MatchMode.ANYWHERE));

                    DetachedCriteria userCriteria = DetachedCriteria.forClass(User.class);
                    userCriteria.setProjection(Property.forName("id"));
//                    userCriteria.createAlias("personalInfo", "pf");
//                    userCriteria.add(Restrictions.like("pf.firstName", entity.getReceiverName(),
//                            MatchMode.ANYWHERE));
                    userCriteria.add(Restrictions.like("username",  entity.getReceiverName(),
                          MatchMode.ANYWHERE));
                    if (entity.getReceiverType().equalsIgnoreCase(SmsGroupDetail.PROFILE_TYPE_ALL)) {
                        criteria.add(Restrictions.disjunction()
                                .add(Property.forName("receiverId").in(farmerCriteria))
                                .add(Property.forName("receiverId").in(agentCriteria))
                                .add(Property.forName("receiverId").in(userCriteria)));
                    } else if (entity.getReceiverType().equalsIgnoreCase(
                            SmsGroupDetail.PROFILE_TYPE_FARMER)) {
                        criteria.add(Property.forName("receiverId").in(farmerCriteria));
                    } else if (entity.getReceiverType().equalsIgnoreCase(
                            SmsGroupDetail.PROFILE_TYPE_FIELD_STAFF)) {
                        criteria.add(Property.forName("receiverId").in(agentCriteria));
                    } else if (entity.getReceiverType().equalsIgnoreCase(
                            SmsGroupDetail.PROFILE_TYPE_USER)) {
                        criteria.add(Property.forName("receiverId").in(userCriteria));
                    } else if (entity.getReceiverType().equalsIgnoreCase(
                            SmsGroupDetail.PROFILE_TYPE_OTHER)) {
                        criteria.add(Restrictions.or(Restrictions.isNull("receiverId"),
                                Restrictions.eq("receiverId", "")));
                    }

                }

                if (!StringUtil.isEmpty(entity.getReceiverNo())) {
                    criteria.add(Restrictions.like("receiverNo", entity.getReceiverNo(),
                            MatchMode.ANYWHERE));
                }

                if (!StringUtil.isEmpty(entity.getReceiverType())
                        && !entity.getReceiverType().equalsIgnoreCase(
                                SmsGroupDetail.PROFILE_TYPE_ALL)) {
                    criteria.add(Restrictions.eq("receiverType", entity.getReceiverType()));
                }
                if (!StringUtil.isEmpty(entity.getReceiverGroupName())) {
                    DetachedCriteria groupCriteria = DetachedCriteria
                            .forClass(SmsGroupHeader.class);
                    groupCriteria.setProjection(Property.forName("id"));
                    groupCriteria.add(Restrictions.like("name", entity.getReceiverGroupName(),
                            MatchMode.ANYWHERE));
                    criteria.add(Property.forName("groupId").in(groupCriteria));
                }
                if (!StringUtil.isEmpty(entity.getCreateUser())) {
                    criteria.add(Restrictions.like("createdUserName", entity.getCreateUser(),
                            MatchMode.ANYWHERE));
                }
                if (!StringUtil.isEmpty(entity.getStatusz())) {
                    criteria.add(Restrictions.like("statusz", entity.getStatusz(), MatchMode.ANYWHERE));
                }
            }
        }
    }

}
