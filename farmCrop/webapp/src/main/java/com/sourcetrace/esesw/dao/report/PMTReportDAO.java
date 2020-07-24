/*
 * PMTReportDAO.java
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
import org.hibernate.sql.JoinType;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.order.entity.txn.PMT;
import com.sourcetrace.eses.order.entity.txn.PMTDetail;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class PMTReportDAO extends ReportDAO {

    /*
     * (non-Javadoc)
     * @see com.ese.dao.ReportDAO#createCriteria(java.util.Map)
     */
    protected Criteria createCriteria(Map params) {

        String entity = (String) params.get(ENTITY);
        Object object = (Object) params.get(FILTER);
        if (!ObjectUtil.isEmpty(object)) {
            if (object instanceof PMTDetail) {
                entity = PMTDetail.class.getName();
            }
        }
        Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createCriteria(entity);

        return criteria;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.dao.ReportDAO#addExampleFiltering(org.hibernate.Criteria, java.util.Map)
     */
    @Override
    protected void addExampleFiltering(Criteria criteria, Map params) {

        Object object = (Object) params.get(FILTER);
        if (!ObjectUtil.isEmpty(object)) {

            if (object instanceof PMT) {
                PMT entity = (PMT) object;

                String dir = (String) params.get(DIR);
                String sort = (String) params.get(SORT_COLUMN);
                if (entity != null) {
                    Date from = (Date) params.get(FROM_DATE);
                    // to date
                    Date to = (Date) params.get(TO_DATE);
                    
                    criteria.createAlias("coOperative", "c");
                    criteria.createAlias("pmtDetails", "pmd");
                    criteria.createAlias("pmd.coOperative", "pc");
                    criteria.createAlias("pmd.procurementProduct","pp");
                    criteria.createAlias("pmd.procurementGrade","pg",criteria.LEFT_JOIN);
                    criteria.createAlias("pg.procurementVariety", "pv",criteria.LEFT_JOIN);
                    criteria.createAlias("mtntTransferInfo", "ti",criteria.LEFT_JOIN);
                    criteria.createAlias("mtnrTransferInfo", "ri",criteria.LEFT_JOIN);
                    criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("pmd.pmt"))); 
                    //criteria.createAlias("pmtImageDetails", "pmtImg",criteria.LEFT_JOIN);
                  
                 
                    
                    HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
		            HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
		            
		            String tenantId = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID) ? ISecurityFilter.DEFAULT_TENANT_ID
		    				: "";
		    		Object object2 = httpSession.getAttribute(ISecurityFilter.IS_MULTI_BRANCH_APP);
		    		String multibranch = ObjectUtil.isEmpty(object2) ? "" : object2.toString();
		    		
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
                    
                    // MTNT Filter
                    if (entity.getStatusCode() == PMT.Status.MTNT.ordinal()) {
                    	// criteria.createAlias("mtntTransferInfo","tr",criteria.LEFT_JOIN);
                   //     criteria.createAlias("pmtFarmerDetais", "pf",JoinType.LEFT_OUTER_JOIN);

                        // MTNT Date
                        if(!ObjectUtil.isEmpty(from)&&!ObjectUtil.isEmpty(to)){
                        criteria.add(Restrictions.between("mtntDate", from, to));
                        }

                        // MTNT RECEIPT #
                        if (entity.getMtntReceiptNumber() != null
                                && !"".equals(entity.getMtntReceiptNumber())) {
                            criteria.add(Restrictions.like("mtntReceiptNumber", entity
                                    .getMtntReceiptNumber(), MatchMode.EXACT));
                        }
                        
                     // SEASON
                        if (entity.getSeasonCode() != null && !"".equals(entity.getSeasonCode())) {
                            criteria.add(Restrictions.like("seasonCode",entity.getSeasonCode(),
                                    MatchMode.ANYWHERE));
                        }
                        
                        if (!ObjectUtil.isEmpty(entity.getMtntTransferInfo()) && !StringUtil.isEmpty(entity.getMtntTransferInfo().getAgentId())) {
                            criteria.add(Restrictions.eq("ti.agentId",entity.getMtntTransferInfo().getAgentId()));
                        }
                        
                        if (entity.getCoOperative()!=null && !ObjectUtil.isEmpty(entity.getCoOperative())) {
                            criteria.add(Restrictions.eq("c.code", entity.getCoOperative().getCode()));
                        }
                        if(!StringUtil.isEmpty(entity.getGinningCode())){
                       	 criteria.add(Restrictions.eq("pc.code", entity.getGinningCode()));
                       }
                        criteria.add(Restrictions.in("statusCode", new Object[] {
                                PMT.Status.MTNT.ordinal(),PMT.Status.COMPLETE.ordinal()}));

                    }
                    // MTNR Filter
                    if (entity.getStatusCode() == PMT.Status.MTNR.ordinal()) {
                        // MTNR Date
                        if(!ObjectUtil.isEmpty(from)&&!ObjectUtil.isEmpty(to)){
                        criteria.add(Restrictions.between("mtnrDate", from, to));
                        }
                        // MTNR RECEIPT #
                        if (entity.getMtnrReceiptNumber() != null
                                && !"".equals(entity.getMtnrReceiptNumber())) {
                            criteria.add(Restrictions.like("mtnrReceiptNumber", entity
                                    .getMtnrReceiptNumber(), MatchMode.EXACT));
                        }
                        // MTNR Status
                        criteria.add(Restrictions.in("statusCode", new Object[] {
                                PMT.Status.MTNR.ordinal(), PMT.Status.COMPLETE.ordinal() }));
                        
                     // SEASON
                        if (entity.getSeasonCode() != null && !"".equals(entity.getSeasonCode())) {
                            criteria.add(Restrictions.like("seasonCode",entity.getSeasonCode(),
                                    MatchMode.ANYWHERE));
                        }
                        //COOPEATIVE
                        if (!ObjectUtil.isEmpty(entity.getCoOperative()) ) {
                            criteria.add(Restrictions.eq("pc.code", entity.getCoOperative().getCode()));
                        }
                    

                   
                    if(!StringUtil.isEmpty(entity.getGinningCode())){
                    	 criteria.add(Restrictions.eq("c.code", entity.getGinningCode()));
                    }
                    }
                    // TRUCK ID
                    if (entity.getTruckId() != null && !"".equals(entity.getTruckId())) {
                        criteria.add(Restrictions.like("truckId", entity.getTruckId(),
                                MatchMode.ANYWHERE));
                    }

                    // DRIVER NAME
                    if (entity.getDriverName() != null && !"".equals(entity.getDriverName())) {
                        criteria.add(Restrictions.like("driverName", entity.getDriverName(),
                                MatchMode.ANYWHERE));
                    }

                    if(entity.getSeasonCode()!=null && !"".equals(entity.getSeasonCode())){
                    	criteria.add(Restrictions.eq("seasonCode", entity.getSeasonCode()));
                    }
                    //Transaction Type
                    if (entity.getTrnType() != null && !"".equals(entity.getTrnType())) {
                    	if(entity.getTrnType().equals(PMT.TRN_TYPE_OTEHR)){
                    	  criteria.add(Restrictions.in("trnType", new String[]{entity.getTrnType(),"",null}));
                    	}else{
                          criteria.add(Restrictions.eq("trnType", entity.getTrnType()));
                    	}
                       }

                    if (!ObjectUtil.isListEmpty(entity.getBranchesList())) {
            			criteria.add(Restrictions.in("branchId", entity.getBranchesList()));
            		}

            		if (!StringUtil.isEmpty(entity.getBranchId())) {
            			criteria.add(Restrictions.eq("branchId", entity.getBranchId()));
            		}
            		
                }
                if (dir != null && sort != null) {
                    if (dir.equals(DESCENDING)) {
                        criteria.addOrder(Order.desc(sort));
                    } else {
                        criteria.addOrder(Order.asc(sort));
                    }
                }

            } else if (object instanceof PMTDetail) {

                PMTDetail pmtDetail = (PMTDetail) object;

                criteria.createAlias("pmt", "pd", Criteria.LEFT_JOIN);

                if (!ObjectUtil.isEmpty(pmtDetail.getPmt()) && pmtDetail.getPmt().getId()>0)
                    criteria.add(Restrictions.eq("pd.id", pmtDetail.getPmt().getId()));
                
                
                Date from = (Date) params.get(FROM_DATE);
                // to date
                Date to = (Date) params.get(TO_DATE);

                criteria.createAlias("pd.coOperative", "c");
            
                criteria.createAlias("coOperative", "pc");
                criteria.createAlias("procurementProduct","pp");
             /*   criteria.createAlias("procurementGrade","pg");
                criteria.createAlias("pg.procurementVariety", "pv");*/
                //criteria.createAlias("pmtImageDetails", "pmtImg",criteria.LEFT_JOIN);
                //criteria.createAlias("transferInfo","tr",JoinType.LEFT_OUTER_JOIN);
                HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
	            HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
	            
	            String tenantId = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID) ? ISecurityFilter.DEFAULT_TENANT_ID
	    				: "";
	    		Object object2 = httpSession.getAttribute(ISecurityFilter.IS_MULTI_BRANCH_APP);
	    		String multibranch = ObjectUtil.isEmpty(object2) ? "" : object2.toString();
	    		
                if (multibranch.equals("1")) {
	    			Object object1 = httpSession.getAttribute(ISecurityFilter.MAPPED_BRANCHES);
	    			if (object1 != null) {
	    				String[] branches = object1.toString().split(",");

	    				if (branches != null && branches.length > 0) {
	    					criteria.add(Restrictions.in("pd.branchId", branches));
	    				}
	    			}
	    		} else {
	    			Object object3= httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
	    			String currentBranch1 = ObjectUtil.isEmpty(object3) ? "" : object3.toString();
	    			if (!StringUtil.isEmpty(currentBranch1)) {
	    				criteria.add(Restrictions.like("pd.branchId", currentBranch1, MatchMode.EXACT));
	    			}
	    		}
                
                // MTNT Filter
                if (pmtDetail.getPmt().getStatusCode() == PMT.Status.MTNT.ordinal()) {
                   // criteria.createAlias("pd.pmtFarmerDetais", "pf",JoinType.LEFT_OUTER_JOIN);
                	 criteria.add(Restrictions.in("pd.statusCode", new Object[] {
                             PMT.Status.MTNT.ordinal()}));
                    // MTNT Date
                    if(!ObjectUtil.isEmpty(from)&&!ObjectUtil.isEmpty(to)){
                    criteria.add(Restrictions.between("pd.mtntDate", from, to));
                    }

                    // MTNT RECEIPT #
                    if (pmtDetail.getPmt().getMtntReceiptNumber() != null
                            && !"".equals(pmtDetail.getPmt().getMtntReceiptNumber())) {
                        criteria.add(Restrictions.like("pd.mtntReceiptNumber", pmtDetail.getPmt()
                                .getMtntReceiptNumber(), MatchMode.EXACT));
                    }
                    
                 // SEASON
                    if (pmtDetail.getPmt().getSeasonCode() != null && !"".equals(pmtDetail.getPmt().getSeasonCode())) {
                        criteria.add(Restrictions.like("pd.seasonCode",pmtDetail.getPmt().getSeasonCode(),
                                MatchMode.ANYWHERE));
                    }
                    if (!ObjectUtil.isEmpty(pmtDetail.getPmt().getCoOperative())) {
                        criteria.add(Restrictions.eq("c.code", pmtDetail.getPmt().getCoOperative().getCode()));
                    }
                    if(!StringUtil.isEmpty(pmtDetail.getPmt().getGinningCode())){
                    	 criteria.add(Restrictions.eq("pc.code", pmtDetail.getPmt().getGinningCode()));
                    }

                }
                // MTNR Filter
                if (pmtDetail.getPmt().getStatusCode() == PMT.Status.MTNR.ordinal()) {
                    // MTNR Date
                    if(!ObjectUtil.isEmpty(from)&&!ObjectUtil.isEmpty(to)){
                    criteria.add(Restrictions.between("pd.mtnrDate", from, to));
                    }
                    // MTNR RECEIPT #
                    if (pmtDetail.getPmt().getMtnrReceiptNumber() != null
                            && !"".equals(pmtDetail.getPmt().getMtnrReceiptNumber())) {
                        criteria.add(Restrictions.like("mtnrReceiptNumber", pmtDetail.getPmt()
                                .getMtnrReceiptNumber(), MatchMode.EXACT));
                    }
                    // MTNR Status
                    criteria.add(Restrictions.in("pd.statusCode", new Object[] {
                            PMT.Status.MTNR.ordinal()}));
                    
                 // SEASON
                    if (pmtDetail.getPmt().getSeasonCode() != null && !"".equals(pmtDetail.getPmt().getSeasonCode())) {
                        criteria.add(Restrictions.like("pd.seasonCode",pmtDetail.getPmt().getSeasonCode(),
                                MatchMode.ANYWHERE));
                    }
                    
                    if (!ObjectUtil.isEmpty(pmtDetail.getPmt().getCoOperative())) {
                        criteria.add(Restrictions.eq("pc.code", pmtDetail.getPmt().getCoOperative().getCode()));
                    }
                    if(!StringUtil.isEmpty(pmtDetail.getPmt().getGinningCode())){
                    	 criteria.add(Restrictions.eq("c.code", pmtDetail.getPmt().getGinningCode()));
                    }
                }

                //COOPEATIVE
               
                
                // TRUCK ID
                if (pmtDetail.getPmt().getTruckId() != null && !"".equals(pmtDetail.getPmt().getTruckId())) {
                    criteria.add(Restrictions.like("pd.truckId", pmtDetail.getPmt().getTruckId(),
                            MatchMode.ANYWHERE));
                }

                // DRIVER NAME
                if (pmtDetail.getPmt().getDriverName() != null && !"".equals(pmtDetail.getPmt().getDriverName())) {
                    criteria.add(Restrictions.like("pd.driverName", pmtDetail.getPmt().getDriverName(),
                            MatchMode.ANYWHERE));
                }

                
                //Transaction Type
                if (pmtDetail.getPmt().getTrnType() != null && !"".equals(pmtDetail.getPmt().getTrnType())) {
                	if(pmtDetail.getPmt().getTrnType().equals(PMT.TRN_TYPE_OTEHR)){
                	  criteria.add(Restrictions.in("pd.trnType", new String[]{pmtDetail.getPmt().getTrnType(),"",null}));
                	}else{
                      criteria.add(Restrictions.eq("pd.trnType", pmtDetail.getPmt().getTrnType()));
                	}
                   }

                if (!ObjectUtil.isListEmpty(pmtDetail.getPmt().getBranchesList())) {
        			criteria.add(Restrictions.in("pd.branchId", pmtDetail.getPmt().getBranchesList()));
        		}

        		if (!StringUtil.isEmpty(pmtDetail.getPmt().getBranchId())) {
        			criteria.add(Restrictions.eq("pd.branchId", pmtDetail.getPmt().getBranchId()));
        		} 
                
                /*To Fetch Only Initial Transfer Records*/
               // criteria.add(Restrictions.in("status", new String[]{'0','1','2'}));

            }

        }
        

    }

}
