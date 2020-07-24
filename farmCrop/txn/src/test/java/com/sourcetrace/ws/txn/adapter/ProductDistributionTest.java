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

public class ProductDistributionTest {
	private static Logger LOGGER = Logger.getLogger(ProductDistribution.class);
	private static final String LOCALHOST = "http://localhost:9001/esetxn/rs";
	private static final String format = "application/json";
	
	public static void main(String[] args) throws Exception {

	 SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.TXN_DATE_TIME);

	 WebClient client = WebClient.create(LOCALHOST);
     client.path("processTxnRequest").accept(format).type(format);
     Request request = new Request();
     Head head = new Head();
     head.setVersionNo("1.00");
     head.setSerialNo("1234567890");
     head.setAgentId("555");
     head.setAgentToken("1EBE1622CEAAC2EB3268EF937E20973C");
     head.setServPointId("SP001");
     head.setTxnTime(sdf.format(new Date()));
     head.setTxnType("379");
     head.setOperType(TransactionProperties.REGULAR_TXN);
     head.setMode(TransactionProperties.ONLINE_MODE);
     head.setResentCount("0");
     head.setMsgNo("201");

		Map<String, String> dataMap = new HashMap<String, String>();
		List<Object> listOfOrderObject = new ArrayList<Object>();

		dataMap.put(TxnEnrollmentProperties.FARMER_ID, "111");
		dataMap.put(TxnEnrollmentProperties.PROCUREMENT_VILLAGE_CODE,"V00001");
		dataMap.put(TxnEnrollmentProperties.SAMITHI_CODE,"G00002");
		dataMap.put(TxnEnrollmentProperties.PRODUCT_DISTRIBUTION_DATE,
				sdf.format(new Date()));
		dataMap.put(TxnEnrollmentProperties.RECEIPT_NO,String.valueOf(System.currentTimeMillis()));
		dataMap.put(TxnEnrollmentProperties.SEASON_CODE,"SE12015");
		dataMap.put(TxnEnrollmentProperties.PAYMENT, "2");
		//dataMap.put(TxnEnrollmentProperties.MOBILE_NO, "09876543212");
		dataMap.put(TxnEnrollmentProperties.IS_FREE_DISTRIBUTION,"");
		dataMap.put(TxnEnrollmentProperties.TAX,"");
		dataMap.put(TxnEnrollmentProperties.MODE_OF_PAYMENT,"");
		
		Data data1 = new Data();
		data1.setKey(TxnEnrollmentProperties.PRODUCT_CODE);
		data1.setValue("PDF08");

		Data data2 = new Data();

		data2.setKey(TxnEnrollmentProperties.QUANTITY);
		data2.setValue("1");

		Data data3 = new Data();
		data3.setKey(TxnEnrollmentProperties.PRICE_PER_UNIT);
		data3.setValue("5.00");

		Data data4 = new Data();
		data4.setKey(TxnEnrollmentProperties.SUB_TOTAL);
		data4.setValue("5");

		List<Data> productData = new ArrayList<Data>();
		productData.add(data1);
		productData.add(data2);
		productData.add(data3);
		productData.add(data4);

		Object productObject = new Object();
		productObject.setData(productData);

		listOfOrderObject.add(productObject);

		List<Data> dataList = CollectionUtil.mapToList(dataMap);

		Collection collection = new Collection();
		collection.setObject(listOfOrderObject);

		Data dataOrder = new Data();
		dataOrder.setKey(TxnEnrollmentProperties.PRODUCT_LIST);
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
