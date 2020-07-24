package com.sourcetrace.eses.txn.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class ExceptionMapperImpl implements ExceptionMapper<TransactionException> {

	@Override
	public Response toResponse(TransactionException transactionException) {
        
    	com.sourcetrace.eses.txn.schema.Response response=new com.sourcetrace.eses.txn.schema.Response();      
    	com.sourcetrace.eses.txn.schema.Status status = new com.sourcetrace.eses.txn.schema.Status();
        status.setCode(transactionException.getCode());
        status.setMessage(transactionException.getMessage());
        response.setStatus(status);
        return Response.ok(response).build();

    }

}
