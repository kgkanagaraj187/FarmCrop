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
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.txn.schema.Body;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Request;
import com.sourcetrace.eses.txn.schema.Response;
import com.sourcetrace.eses.util.CollectionUtil;
import com.sourcetrace.eses.util.DateUtil;

public class CultivationAdapterTest
{
	  private static Logger LOGGER = Logger.getLogger(CultivationAdapterTest.class);
	  private static final String LOCALHOST = "http://localhost:9090/esetxn/rs";
	  private static final String format = "application/json";
	    
	    
	    public static void main(String[] args) throws Exception 
	    {
	    	
	    	SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.TXN_DATE_TIME);

	        WebClient client = WebClient.create(LOCALHOST);
	        client.path("processTxnRequest").accept(format).type(format);
	        Request request = new Request();
	        Head head = new Head();
	        head.setSerialNo("f9d6c95b54b5b74ae05f8a48110680d1");
	        head.setAgentId("subramani");
	        head.setAgentToken("75198AD076275CF3A587D632A667E727");
	        head.setServPointId("SP001");
	        head.setTxnTime(sdf.format(new Date()));
	        head.setTxnType(TransactionTypeProperties.CULTIVATION);
	        head.setOperType(TransactionProperties.REGULAR_TXN);
	        head.setMode(TransactionProperties.ONLINE_MODE);
	        head.setResentCount("0");
	        head.setMsgNo(DateUtil.getUniqueDate());
	
	        Map<String, String> dataMap = new HashMap<String, String>();
			
			dataMap.put(TxnEnrollmentProperties.CULTIVATION_RECEIPT_NO,"000001");
			dataMap.put(TxnEnrollmentProperties.CULTIVATION_FARM_ID, "20160408174541943");
			dataMap.put(TxnEnrollmentProperties.EXPENSE_TYPE, "7");
			dataMap.put(TxnEnrollmentProperties.PLOUGHING,"2.01");
			dataMap.put(TxnEnrollmentProperties.RIDGE_FURROW,"10.10");
			/*dataMap.put(TxnEnrollmentProperties.COST_OF_SEED, "12.03");
			dataMap.put(TxnEnrollmentProperties.SOWING_TREAT, "11.05");
			dataMap.put(TxnEnrollmentProperties.LABOURCOST_MEN,"14.11");
			dataMap.put(TxnEnrollmentProperties.LABOURCOST_WOMEN,"8.12");
			dataMap.put(TxnEnrollmentProperties.PACKING_MATERIAL, "5.22");*/
			dataMap.put(TxnEnrollmentProperties.TRANSPORT,"12.00");
			dataMap.put(TxnEnrollmentProperties.MISCELLANEOUS,"8.15");
			dataMap.put(TxnEnrollmentProperties.COST_OF_FERTILIZER,"1:100,2:200,3:300");
			dataMap.put(TxnEnrollmentProperties.TXN_TYPE,"1");
			dataMap.put(TxnEnrollmentProperties.AGRI_INCOME,"11");
			dataMap.put(TxnEnrollmentProperties.INTERCROP_INCOME,"11");
			dataMap.put(TxnEnrollmentProperties.OTHERSOURCES_INCOME,"11");
			List<Data> dataList = CollectionUtil.mapToList(dataMap);	
	        
			Body body = new Body();
			body.setData(dataList);

			request.setHead(head);
			request.setBody(body);
			
			try {
	            Response response = client.post(request, Response.class);
	            LOGGER.info(response.getStatus().getCode());
	            LOGGER.info(response.getStatus().getMessage());
	            System.out.println("result" + response.getStatus().getCode());
	            System.out.println("result" + response.getStatus().getMessage());

	            LOGGER.info("-----END------");

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
} }
