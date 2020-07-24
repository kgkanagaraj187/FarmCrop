package com.sourcetrace.ws.txn.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.txn.schema.Request;
import com.sourcetrace.eses.txn.schema.Response;
import com.sourcetrace.eses.util.CollectionUtil;
import com.sourcetrace.eses.util.DateUtil;

public class FarmCropHarvestTest {
	private static Logger LOGGER = Logger.getLogger(FarmCropHarvestTest.class);
	
	private static final String LOCALHOST = "http://localhost:9001/esetxn/rs";
	private static final String format = "application/json";
	
	public static void main(String[] args) throws Exception {

		 SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.TXN_DATE_TIME);

		 	WebClient client = WebClient.create(LOCALHOST);
	        client.path("processTxnRequest").accept(format).type(format);
	        Request request = new Request();
	        Head head = new Head();
	        head.setSerialNo("f3db81f1162d1d85b49d87c3110d54ad");
	        head.setAgentId("vicky");
	        head.setAgentToken("ADE636843963A6C0EF5D86A6378CAC50");
	        head.setServPointId("SP001");
	        head.setTxnTime(sdf.format(new Date()));
	        head.setTxnType(TransactionTypeProperties.FARMER_ENROLLMENT);
	        head.setOperType(TransactionProperties.REGULAR_TXN);
	        head.setMode(TransactionProperties.ONLINE_MODE);
	        head.setResentCount("0");
	        head.setMsgNo("201");
			
			Map<String, String> dataMap = new HashMap<String, String>();
			List<Object> listOfOrderObject = new ArrayList<Object>();
			
			dataMap.put(TxnEnrollmentProperties.CROP_HARVEST_DATE,
					sdf.format(new Date()));
			dataMap.put(TxnEnrollmentProperties.CROP_FARMER_ID, "000323");
			dataMap.put(TxnEnrollmentProperties.CROP_FARM_ID, "2016040818394149");
			dataMap.put(TxnEnrollmentProperties.TOTAL,"2");
			dataMap.put(TxnEnrollmentProperties.CROP_RECEIPT_NO,"");
			
			
			Data data1 = new Data();
			data1.setKey(TxnEnrollmentProperties.CROP_TYPE);
			data1.setValue("0");
			
			Data data2 = new Data();
			data2.setKey(TxnEnrollmentProperties.CROP_ID);
			data2.setValue("0");
			
			Data data3 = new Data();
			data3.setKey(TxnEnrollmentProperties.CROP_VARIETY_ID);
			data3.setValue("6");
			
			Data data4 = new Data();
			data4.setKey(TxnEnrollmentProperties.CROP_GRADE_CODE);
			data4.setValue("56");
			
			Data data5 = new Data();
			data5.setKey(TxnEnrollmentProperties.CROP_QUANTITY);
			data5.setValue("1");
			
			Data data6 = new Data();
			data6.setKey(TxnEnrollmentProperties.CROP_PRICE);
			data6.setValue("5.00");
			
			Data data7 = new Data();
			data7.setKey(TxnEnrollmentProperties.CROP_SUB_TOTAL);
			data7.setValue("5");
			
			List<Data> harvestData = new ArrayList<Data>();
			harvestData.add(data1);
			harvestData.add(data2);
			harvestData.add(data3);
			harvestData.add(data4);
			harvestData.add(data5);
			harvestData.add(data6);
			harvestData.add(data7);
			
			Object harvestObject = new Object();
			harvestObject.setData(harvestData);

			listOfOrderObject.add(harvestObject);

			List<Data> dataList = CollectionUtil.mapToList(dataMap);

			Collection collection = new Collection();
			collection.setObject(listOfOrderObject);

			Data dataOrder = new Data();
			dataOrder.setKey(TxnEnrollmentProperties.CROP_LIST);
			dataOrder.setCollectionValue(collection);

			dataList.add(dataOrder);

			Body body = new Body();
			body.setData(dataList);

			request.setHead(head);
			request.setBody(body);

			Response response = client.post(request, Response.class);
			LOGGER.info(response.getStatus().getCode());
			LOGGER.info(response.getStatus().getMessage());
			System.out.println("Status Code : " + response.getStatus().getCode());
			System.out
					.println("Status Msg  : " + response.getStatus().getMessage());
	}
}
