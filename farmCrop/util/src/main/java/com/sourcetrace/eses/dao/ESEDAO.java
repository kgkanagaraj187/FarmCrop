package com.sourcetrace.eses.dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

public class ESEDAO extends HibernateDaoSupport implements IESEDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.dao.IESEDAO#save(java.lang.Object)
	 */
	public void save(Object obj) {
		getHibernateTemplate().save(obj);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.dao.IESEDAO#saveOrUpdate(java.lang.Object)
	 */
	public void saveOrUpdate(Object obj) {
		getHibernateTemplate().saveOrUpdate(obj);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.dao.IESEDAO#update(java.lang.Object)
	 */
	public void update(Object obj) {
		getHibernateTemplate().update(obj);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.dao.IESEDAO#delete(java.lang.Object)
	 */
	public void delete(Object obj) {
		getHibernateTemplate().delete(obj);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.dao.IESEDAO#list(java.lang.String)
	 */
	public List list(String query) {

		return getHibernateTemplate().find(query);
	}

	/**
	 * List.
	 * 
	 * @param query
	 *            the query
	 * @param value
	 *            the value
	 * @return the list
	 */
	public List list(String query, Object value) {

		return getHibernateTemplate().find(query, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.dao.IESEDAO#list(java.lang.String,
	 * java.lang.Object[])
	 */
	public List list(String query, Object[] bindValues) {

		return getHibernateTemplate().find(query, bindValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.dao.IESEDAO#find(java.lang.String,
	 * java.lang.Object)
	 */
	public Object find(String query, Object value) {

		List list = getHibernateTemplate().find(query, value);
		return (list.size() >= 1) ? list.get(0) : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.dao.IESEDAO#find(java.lang.String,
	 * java.lang.Object[])
	 */
	public Object find(String query, Object[] values) {

		List list = getHibernateTemplate().find(query, values);
		return (list.size() >= 1) ? list.get(0) : null;
	}
	
	public void removeBlindChilds(String tableName, String columnName,String tenantId) {
		// TODO Auto-generated method stub
	    Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		SQLQuery sqlQuery = session.createSQLQuery("DELETE FROM " + tableName + " WHERE " + columnName + " IS NULL");
		sqlQuery.executeUpdate();
		session.flush();
		session.close();	
	}

}
