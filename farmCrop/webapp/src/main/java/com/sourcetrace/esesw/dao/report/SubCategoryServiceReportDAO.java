package com.sourcetrace.esesw.dao.report;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.order.entity.profile.SubCategory;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class SubCategoryServiceReportDAO.
 */
public class SubCategoryServiceReportDAO extends ReportDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.dao.ReportDAO#addExampleFiltering(org.hibernate.Criteria,
	 * java.util.Map)
	 */
	@SuppressWarnings("null")
	protected void addExampleFiltering(Criteria criteria, Map params) {

		// check for filter entity
		SubCategory entity = (SubCategory) params.get(FILTER);

		if (entity != null) {
			

			if (!ObjectUtil.isListEmpty(entity.getBranchesList())) {
				criteria.add(Restrictions.in("branchId", entity.getBranchesList()));
			}
			
			if (!StringUtil.isEmpty(entity.getBranchId())){
			    criteria.add(Restrictions.eq("branchId", entity.getBranchId()));
			}
			//criteria.createAlias("category", "c");

			if (entity.getName() != null && !"".equals(entity.getName()))
				criteria.add(Restrictions.like("name", entity.getName(),
						MatchMode.ANYWHERE));
			if (entity.getCode() != null && !"".equals(entity.getCode()))
				criteria.add(Restrictions.like("code", entity.getCode(),
						MatchMode.ANYWHERE));

			if (!ObjectUtil.isEmpty(entity.getCategory())
					&& !StringUtil.isEmpty(entity.getCategory().getName())) {
				criteria.add(Restrictions.like("c.name", entity.getCategory()
						.getName().trim(), MatchMode.ANYWHERE));
			}

			// sorting direction
			String dir = (String) params.get(DIR);
			// sorting column
			String sort = (String) params.get(SORT_COLUMN);
			// sort a column in the given direction ascending/descending
			if (dir != null && sort != null) {
				Criteria sortCriteria = null;
				if (sort.contains(DELIMITER)) {
					sort = sort.substring(sort.lastIndexOf(DELIMITER) + 1);
					if (dir.equals(DESCENDING)) {
						// sort descending
						sortCriteria.addOrder(Order.desc(sort));
					} else {
						// sort ascending
						sortCriteria.addOrder(Order.asc(sort));
					}
				}
			}
		}
	}

}
