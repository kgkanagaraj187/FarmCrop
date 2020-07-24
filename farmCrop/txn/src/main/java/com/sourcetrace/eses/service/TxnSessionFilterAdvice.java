package com.sourcetrace.eses.service;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.txn.schema.Request;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;

@Service
@Aspect
public class TxnSessionFilterAdvice {

	@AfterReturning(pointcut = "execution(* org.hibernate.SessionFactory.openSession(..)) || execution(* org.hibernate.SessionFactory.getCurrentSession(..))", returning = "session")
	public void setupFilter(Session session) {
		Object reqObj = ReflectUtil.getCurrentTxnRequestData();
		if (reqObj instanceof Request) {
			Request requestData = (Request) ReflectUtil.getCurrentTxnRequestData();
			String currentBranch = ObjectUtil.isEmpty(requestData) ? null : requestData.getHead().getBranchId();

			if (!StringUtil.isEmpty(currentBranch)) {
				org.hibernate.Filter branchFilter = session.getEnabledFilter(ISecurityFilter.BRANCH_FILTER);
				if (ObjectUtil.isEmpty(branchFilter)) {
					List<String> branches = new ArrayList<String>();
					branches.add(currentBranch);
					if (branches.size() != 0) {
						session.enableFilter(ISecurityFilter.BRANCH_FILTER)
								.setParameterList(ISecurityFilter.BRANCH_FILTER_PARM, branches);
					}
				}

			}
		}

	}
}