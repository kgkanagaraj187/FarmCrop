package com.sourcetrace.eses.service;

import java.util.HashMap;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.sourcetrace.eses.entity.Transaction;
import com.sourcetrace.eses.interceptor.ITxnMessageUtil;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.TxnFault;
import com.sourcetrace.eses.txn.schema.Body;
import com.sourcetrace.eses.txn.schema.Request;
import com.sourcetrace.eses.txn.schema.Response;
import com.sourcetrace.eses.txn.schema.Status;
import com.sourcetrace.eses.util.CollectionUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

@Component
@Path("/")
@Produces({ "application/json", "application/xml" })
@WebService(targetNamespace = "http://service.eses.sourcetrace.com/", endpointInterface = "com.sourcetrace.eses.service.ITxnProcessService", portName = "tservport", serviceName = "TxnProcessServiceImpl")
public class TxnFarmerTraceDetailsProcessService implements ITxnFarmerTraceDetailsProcessService {
	private static final Logger LOGGER = Logger.getLogger(TxnProcessServiceImpl.class.getName());
	public static final String TXN_ID = "txnId";
	public static final String SERVER_ERROR = "SERVER_ERROR";

	private Map<String, ITxnAdapter> txnAdapterMap;
	@CrossOrigin(origins = "http://localhost:8080")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@POST
	@Path("/processTxnRequest")
	@Consumes({ "application/json", "application/xml" })
	@WebMethod
	public Response processRequest(Request request) {
		Response response = new Response();
		Map reqData = new HashMap();
		Map respData = new HashMap();
		Status status = new Status();

		ITxnAdapter txnAdapter = getTxnAdapter(request.getHead().getTxnType());

		if (!ObjectUtil.isEmpty(txnAdapter)) {
			try {
				if (!ObjectUtil.isEmpty(request.getBody()) && !request.getBody().getData().isEmpty())
					reqData = CollectionUtil.listToMap(request.getBody());
				reqData.put(ITxnMessageUtil.HEAD, request.getHead());
				if (Transaction.OPERATIONTYPE_REGULAR.equalsIgnoreCase(request.getHead().getOperType())) {
					
					respData = txnAdapter.process(reqData);
					Body body = new Body();
					body.setData(CollectionUtil.mapToList(respData));
					response.setBody(body);
					
				//	response.setStatus(status);
				}
			} catch (Exception exception) {
				String exceptionDetailMsg = exception.getMessage();
				if (exception instanceof TxnFault) {
					TxnFault txnFault = (TxnFault) exception;
					status.setCode(txnFault.getCode());
					status.setMessage(txnFault.getMessage());
				} else {
					status.setCode(SERVER_ERROR);
					String exceptionDetailLog = exception.getMessage();
					if (StringUtil.isEmpty(exceptionDetailLog) || exceptionDetailLog.equalsIgnoreCase("null")) {
						// StringWriter sw = new StringWriter();
						// PrintWriter pw = new PrintWriter(sw);
						// exception.printStackTrace(pw);
						// exceptionDetailMsg = sw.toString();
						exceptionDetailMsg = org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(exception);
						exceptionDetailLog = exception.toString();
					}
					status.setMessage(exceptionDetailLog);
				}
				throw new TxnFault(status.getCode(), status.getMessage());
			}
		}

		return response;
	}

	public ITxnAdapter getTxnAdapter(String txnType) {

		LOGGER.info("Txn Type " + txnType);
		return txnAdapterMap.get(txnType);
	}

	public Map<String, ITxnAdapter> getTxnAdapterMap() {
		return txnAdapterMap;
	}

	public void setTxnAdapterMap(Map<String, ITxnAdapter> txnAdapterMap) {
		this.txnAdapterMap = txnAdapterMap;
	}

}
