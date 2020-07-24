package com.sourcetrace.eses.dao;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unchecked")
@Repository
@Transactional
public class LedgerDAO extends ESEDAO implements ILedgerDAO{
	
	 @Autowired
	    public LedgerDAO(SessionFactory sessionFactory) {
	        this.setSessionFactory(sessionFactory);
	    }

}
