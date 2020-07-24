package com.sourcetrace.ws.txn.adapter;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.log4j.Logger;

import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TransactionTypeProperties;
import com.sourcetrace.eses.txn.schema.Body;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Request;
import com.sourcetrace.eses.txn.schema.Response;
import com.sourcetrace.eses.util.CollectionUtil;
import com.sourcetrace.eses.util.DateUtil;

public class ProductsDownloadTest {

	
		private static Logger LOGGER = Logger.getLogger(ProductsDownloadTest.class);

		private static final String LOCALHOST = "http://127.0.0.1:8090/tserv/rs";
		private static final String format = "application/json";
		private static final SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.TXN_TIME_FORMAT);

	
		public static void main(String[] args) {								
			WebClient client = WebClient.create(LOCALHOST);
			client.path("processTxnRequest").accept(format).type(format);
			 Request request = new Request();
		     Head head = new Head();
		     head.setVersionNo("1.00");
		     head.setSerialNo("8ee91731f326621878bcfaa1a0fca6be");
		     head.setAgentId("Kisan");
		     head.setAgentToken("E19F3A9A722E31FEEF5D86A6378CAC50");
		     head.setServPointId("SP001");
		     head.setTxnTime(sdf.format(new Date()));
		     head.setTxnType("379");
		     head.setOperType(TransactionProperties.REGULAR_TXN);
		     head.setMode(TransactionProperties.ONLINE_MODE);
		     head.setResentCount("0");
		     head.setMsgNo("201");

			Map<String,String> dataMap = new HashMap<String, String>();
	        dataMap.put(TransactionProperties.PRODUCTS_DOWNLOAD_REVISION_NO,"0");
	        
	        List<Data> dataList = CollectionUtil.mapToList(dataMap);
	        Body body=new Body();
	        body.setData(dataList);

	        request.setHead(head);
			request.setBody(body);

			Response response = client.post(request, Response.class);// get(Response.class);post(request,
			// Response.class);
			LOGGER.info(response.getStatus().getCode());
			LOGGER.info(response.getStatus().getMessage());
			System.out.println("Status Code : " + response.getStatus().getCode());
			System.out
					.println("Status Msg  : " + response.getStatus().getMessage());
		}


	}


