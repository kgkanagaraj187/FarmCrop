package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.ese.entity.profile.CropSupplyDetails;
import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.order.entity.txn.Distribution;
import com.sourcetrace.eses.order.entity.txn.DistributionStock;
import com.sourcetrace.eses.order.entity.txn.DistributionStockDetail;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class DistributionStockTransferReportDAO extends ReportDAO {
	HttpServletRequest request;
	protected Criteria createCriteria(Map params) {


		String entity = (String) params.get(ENTITY);
		Object object = (Object) params.get(FILTER);
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof DistributionStockDetail) {
				entity = DistributionStockDetail.class.getName();
			}
		}
		Criteria criteria = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createCriteria(entity);

		return criteria;
		
		
	}

	
	protected void addExampleFiltering(Criteria criteria, Map params) {

		// check for filter entity
		Object object = (Object) params.get(FILTER);
		HttpServletRequest httpRequest = ReflectUtil.getCurrentHttpRequest();
		request=ReflectUtil.getCurrentHttpRequest();
		String tenantId = (String) httpRequest.getSession().getAttribute(ISecurityFilter.TENANT_ID);
		String branch=(String) httpRequest.getSession().getAttribute(ISecurityFilter.CURRENT_BRANCH);
		tenantId = StringUtil.isEmpty(tenantId) ? ISecurityFilter.DEFAULT_TENANT_ID : tenantId;
		
		if (!ObjectUtil.isEmpty(object)) {
			if (object instanceof DistributionStock) {
				DistributionStock entity = (DistributionStock) object;
				if (entity != null) {
					
			/*		 criteria.createAlias("distributionStockDetails", "dsd");
					 criteria.createAlias("dsd.product", "pro");*/
					  criteria.createAlias("senderWarehouse", "sw");
					  criteria.createAlias("receiverWarehouse", "rw");
					 
					if (entity.getTxnType().equals(DistributionStock.DISTRIBUTION_STOCK_TRANSFER)) {
						 criteria.add(Restrictions.eq("sw.branchId",branch));
						criteria.add(
								Restrictions.eq("txnType", entity.getTxnType()));
						if(entity.getSeason() !=null && !"".equals(entity.getSeason())){
							criteria.add(Restrictions.eq("season", entity.getSeason()));
						}
					    if (entity.getTruckId() != null && !"".equals(entity.getTruckId())) {
	                        criteria.add(Restrictions.like("truckId", entity.getTruckId(),
	                                MatchMode.ANYWHERE));
	                    }
					    
					    if (entity.getReceiptNo() != null && !"".equals(entity.getReceiptNo())) {
	                        criteria.add(Restrictions.like("receiptNo", entity.getReceiptNo(),
	                                MatchMode.ANYWHERE));
	                    }
						if (!ObjectUtil.isEmpty(entity.getSenderWarehouse())) {
							criteria.add(Restrictions.eq("sw.id", entity.getSenderWarehouse().getId()));
						}
						
						if (!ObjectUtil.isEmpty(entity.getReceiverWarehouse())) {
							criteria.add(Restrictions.eq("rw.id", entity.getReceiverWarehouse().getId()));

						}
						
						if (!ObjectUtil.isListEmpty(entity.getBranchesList())) {
							criteria.add(Restrictions.in("branchId", entity.getSenderWarehouse().getBranchesList()));
						}
						
				/*		if (!ObjectUtil.isEmpty(entity.getProductCode())&& entity.getProductCode()!=0) {
							criteria.createAlias("distributionStockDetails", "ds").createAlias("ds.product", "p");
							criteria.add(Restrictions.eq("p.id", entity.getProductCode()));
						}*/

						
				/*		if (!ObjectUtil.isEmpty(entity.getProductCode())&& entity.getProductCode()!=0) {
							criteria.add(Restrictions.eq("pro.id", entity.getProductCode()));

						}*/
						
						if (!ObjectUtil.isEmpty(entity.getProductCode())&& entity.getProductCode()!=0) {
							criteria.createAlias("distributionStockDetails", "ds").createAlias("ds.product", "p");
							criteria.add(Restrictions.eq("p.id", entity.getProductCode()));
						}
						
					}else if (entity.getTxnType().equals(DistributionStock.DISTRIBUTION_STOCK_RECEPTION)) {
						criteria.add(Restrictions.eq("txnType", DistributionStock.DISTRIBUTION_STOCK_RECEPTION));
						criteria.add(Restrictions.eq("branchId",branch));
						if(entity.getSeason() !=null && !"".equals(entity.getSeason())){
							criteria.add(Restrictions.eq("season", entity.getSeason()));
						}
						if (entity.getTruckId() != null && !"".equals(entity.getTruckId())) {
	                        criteria.add(Restrictions.like("truckId", entity.getTruckId(),
	                                MatchMode.ANYWHERE));
	                    }
					    
					    if (entity.getReceiptNo() != null && !"".equals(entity.getReceiptNo())) {
	                        criteria.add(Restrictions.like("receiptNo", entity.getReceiptNo(),
	                                MatchMode.ANYWHERE));
	                    }
						if (!ObjectUtil.isEmpty(entity.getSenderWarehouse())) {
							criteria.add(Restrictions.eq("sw.id", entity.getSenderWarehouse().getId()));
						}
						
						if (!ObjectUtil.isEmpty(entity.getReceiverWarehouse())) {
							criteria.add(Restrictions.eq("rw.id", entity.getReceiverWarehouse().getId()));

						}
						
						if (!ObjectUtil.isListEmpty(entity.getBranchesList())) {
							criteria.add(Restrictions.in("branchId", entity.getBranchesList()));
						}
						
						if (!ObjectUtil.isEmpty(entity.getProductCode())&& entity.getProductCode()!=0) {
							criteria.createAlias("distributionStockDetails", "ds").createAlias("ds.product", "p");
							criteria.add(Restrictions.eq("p.id", entity.getProductCode()));
						}
						
						
				}
			} 
			
			} else if (object instanceof DistributionStockDetail) {
				 DistributionStockDetail entity = (DistributionStockDetail) object;
				 //criteria.createAlias("distributionStock", "ds", Criteria.LEFT_JOIN);
				 criteria.createAlias("distributionStock", "ds");
				 criteria.createAlias("product", "pro");
				 
				 if (!ObjectUtil.isEmpty(entity.getDistributionStock()))
						criteria.add(Restrictions.eq("ds.id", entity
								.getDistributionStock().getId()));
				 
					if (!ObjectUtil.isEmpty(entity.getProduct())) {
						/*criteria.createAlias("distributionStockDetails", "ds").createAlias("ds.product", "p");*/
						criteria.add(Restrictions.eq("pro.id", entity.getProduct().getId()));
					}
				 
			}

		
			
			
			// sorting direction
			String dir = (String) params.get(DIR);
			// sorting column
			String sort = (String) params.get(SORT_COLUMN);
			if (dir.equals(DESCENDING)) {
				// sort descending
				criteria.addOrder(Order.desc(sort));
			} else {
				// sort ascending
				criteria.addOrder(Order.asc(sort));
			}
		
	}
	}
	
}
