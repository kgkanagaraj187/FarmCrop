package com.sourcetrace.ws.txn.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.log4j.Logger;

import com.sourcetrace.eses.adapter.core.ProductDistribution;
import com.sourcetrace.eses.order.entity.txn.OfflineProcurement;
import com.sourcetrace.eses.order.entity.txn.Procurement;
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

public class ProcurementProductUploadTest {
	private static Logger LOGGER = Logger.getLogger(OfflineProcurement.class);
	private static final String LOCALHOST = "http://localhost:8090/tserv/rs";
	private static final String format = "application/json";

	public static void main(String[] args) throws Exception {
		
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.TXN_DATE_TIME);

	     WebClient client = WebClient.create(LOCALHOST);
	     client.path("processTxnRequest").accept(format).type(format);
	     Request request = new Request();
	     Head head = new Head();
	     head.setVersionNo("1.00");
	     head.setSerialNo("8c40dac151c1731f6377fe5b51d043c6");
	     head.setAgentId("111222");
	     head.setAgentToken("25D08C028FE308B6E2EF598F18AC28D3");
	     head.setServPointId("SP001");
	     head.setTxnTime(sdf.format(new Date()));
	     head.setTxnType(TransactionTypeProperties.PROCUREMENT_PRODUCT_ENROLLMENT);
	     head.setOperType(TransactionProperties.REGULAR_TXN);
	     head.setMode(TransactionProperties.OFFLINE_MODE);
	     head.setResentCount("0");
	     head.setMsgNo("201");
	     head.setTenantId("AWI");

		Map<String, String> dataMap = new HashMap<String, String>();
		List<Object> listOfOrderObject = new ArrayList<Object>();
		dataMap.put(TxnEnrollmentProperties.SAMITHI_CODE, "G00003");
		dataMap.put(TxnEnrollmentProperties.PROCUREMENT_VILLAGE_CODE, "V00009");
        dataMap.put(TxnEnrollmentProperties.PROCUREMENT_DATE,
                sdf.format(new Date()));
        dataMap.put(TxnEnrollmentProperties.RECEIPT_NO,  String.valueOf(System.currentTimeMillis()));
        dataMap.put(TxnEnrollmentProperties.ROAD_MAP_CODE, "785412369");
        dataMap.put(TxnEnrollmentProperties.VEHICLE_NUM, "TN97K1896");
		dataMap.put(TxnEnrollmentProperties.FARMER_ID, "004511");
		dataMap.put(TxnEnrollmentProperties.FARM_ID, "");
		dataMap.put(TxnEnrollmentProperties.SUBSTITUTE_NAME, "");
		dataMap.put(TxnEnrollmentProperties.FARMER_ATTENDANCE, "");
		dataMap.put(TxnEnrollmentProperties.TOTAL_AMOUNT, "70");
		dataMap.put(TxnEnrollmentProperties.MOBILE_NO, "96587412");
		
		
		dataMap.put(TxnEnrollmentProperties.PAYMENT, "50");
		dataMap.put(TxnEnrollmentProperties.CURRENT_SEASON_CODE, "HS00001");
			
		Data data2 = new Data();
		data2.setKey(TxnEnrollmentProperties.NO_OF_BAGS);
		data2.setValue("1");

		Data data3 = new Data();
		data3.setKey(TxnEnrollmentProperties.GROSS_WEIGHT);
		data3.setValue("2");

		Data data4 = new Data();
		data4.setKey(TxnEnrollmentProperties.TARE_WEIGHT);
		data4.setValue("0");

		Data data5 = new Data();
		data5.setKey(TxnEnrollmentProperties.NET_WEIGHT);
		data5.setValue("2");

		Data data6 = new Data();
		data6.setKey(TxnEnrollmentProperties.PRICE_PER_UNIT);
		data6.setValue("70");

		Data data7 = new Data();
		data7.setKey(TxnEnrollmentProperties.PRODUCT_CODE);
		data7.setValue("CR00001");

		Data data8 = new Data();
		data8.setKey(TxnEnrollmentProperties.SUB_TOTAL);
		data8.setValue("70");

		Data data9 = new Data();
		data9.setKey(TxnEnrollmentProperties.QUALITY);
		data9.setValue("G00001");

		List<Data> productData = new ArrayList<Data>();
		productData.add(data9);
		productData.add(data2);
		productData.add(data3);
		productData.add(data4);
		productData.add(data5);
		productData.add(data6);
		productData.add(data7);
		productData.add(data8);

		Object productObject = new Object();
		productObject.setData(productData);

		listOfOrderObject.add(productObject);
		
		List<Data> dataList = CollectionUtil.mapToList(dataMap);

		Collection collection = new Collection();
		collection.setObject(listOfOrderObject);

		Data dataOrder = new Data();
		dataOrder.setKey(TxnEnrollmentProperties.PROCUREMENT_PRODUCT_DETAIL);
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
		System.out.println("Status Msg  : " + response.getStatus().getMessage());

	}

}
