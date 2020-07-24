package com.sourcetrace.eses.multitenancy;

import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.txn.schema.Locate;
import com.sourcetrace.eses.txn.schema.Request;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class WebSessionCurrentTenantIdentifierResolver implements CurrentTenantIdentifierResolver {

	@Resource(name = "datasources")
	private Map<String, DataSource> datasources;

	
	
	@Override
	public String resolveCurrentTenantIdentifier() {
		String tenantId = ISecurityFilter.DEFAULT_TENANT_ID;
		Object reqObj = ReflectUtil.getCurrentTxnRequestData();
		if (reqObj instanceof Request) {
		Request requestData =  (Request) reqObj;
		if (!ObjectUtil.isEmpty(requestData)) {
			String tenantval = requestData.getHead().getTenantId();
			if (!StringUtil.isEmpty(tenantval) && datasources.containsKey(tenantval)) {
				tenantId = tenantval;
			}
		}
		}else if (reqObj instanceof Locate) {
			Locate requestData =  (Locate) reqObj;
			if (!ObjectUtil.isEmpty(requestData)) {
				String tenantval = requestData.getTenantId();
				if (!StringUtil.isEmpty(tenantval) && datasources.containsKey(tenantval)) {
					tenantId = tenantval;
				}
			}
		}
		if (StringUtil.isEmpty(tenantId)) {
			tenantId = ISecurityFilter.DEFAULT_TENANT_ID;
		}
		return tenantId;
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return false;
	}
}
