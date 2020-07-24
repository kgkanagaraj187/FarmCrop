package com.sourcetrace.eses.service;

import javax.jws.WebService;

import com.sourcetrace.eses.txn.schema.Request;
import com.sourcetrace.eses.txn.schema.Response;

@WebService
public interface ITxnTraceabilityProcessService {
	public Response processRequest(Request request);
}
