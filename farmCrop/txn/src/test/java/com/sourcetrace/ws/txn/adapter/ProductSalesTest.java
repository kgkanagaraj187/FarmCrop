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
public class ProductSalesTest {
	private static Logger LOGGER = Logger.getLogger(ProductSalesTest.class);
	private static final String LOCALHOST = "http://localhost:8082/esetxn/rs";
	private static final String format = "application/json";

	public static void main(String[] args) throws Exception {
		
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.TXN_DATE_TIME);

		WebClient client = WebClient.create(LOCALHOST);
		client.path("processTxnRequest").accept(format).type(format);
		Request request = new Request();
		Head head = new Head();
		head.setSerialNo("1122233334444");
		head.setAgentId("1123");
		head.setAgentToken("D0F14258115CB34044FC86FD4D34DBAB");
		head.setServPointId("SP001");
		head.setTxnTime(sdf.format(new Date()));
		head.setTxnType(TransactionTypeProperties.PRODUCT_SALES);
		head.setOperType(TransactionProperties.REGULAR_TXN);
		head.setMode(TransactionProperties.ONLINE_MODE);
		head.setResentCount("0");
		head.setMsgNo(DateUtil.convertDateToString(new Date(), DateUtil.SYNC_DATE_TIME));

		Map<String, String> dataMap = new HashMap<String, String>();
		List<Object> listOfOrderObject = new ArrayList<Object>();

		
		dataMap.put(TxnEnrollmentProperties.SALES_DATE,sdf.format(new Date()));
		dataMap.put(TxnEnrollmentProperties.CROP_FARMER_ID, "000323");
		dataMap.put(TxnEnrollmentProperties.CROP_FARM_ID, "2016040818394149");
		dataMap.put(TxnEnrollmentProperties.TOTAL,"2");
		dataMap.put(TxnEnrollmentProperties.SALES_BUYER,"buyer");
		dataMap.put(TxnEnrollmentProperties.TRANSPORTER_NAME,"");
		dataMap.put(TxnEnrollmentProperties.INVOICE_DETAIL,"");
		dataMap.put(TxnEnrollmentProperties.CROP_RECEIPT_NO,"");
		dataMap.put(TxnEnrollmentProperties.VEHICLE_NO,"");
		
		/*dataMap.put(TxnEnrollmentProperties.CROP_ID, "0");
		dataMap.put(TxnEnrollmentProperties.CROP_VARIETY_ID,"11110");
		dataMap.put(TxnEnrollmentProperties.GRADE_CODE, "70");
		dataMap.put(TxnEnrollmentProperties.BATCH_NO,"1001");
		dataMap.put(TxnEnrollmentProperties.PRICE, "199");
		dataMap.put(TxnEnrollmentProperties.CROP_QUANTITY, "10");
		dataMap.put(TxnEnrollmentProperties.SUB_TOTAL, "150");*/
		
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
		
		Data data8 = new Data();
		data8.setKey(TxnEnrollmentProperties.BATCH_NO);
		data8.setValue("0");
	
		
		/*Data data2 = new Data();
		data2.setKey(TxnEnrollmentProperties.SALES_DATE);
		data2.setValue(sdf.format(new Date()));

		Data data3 = new Data();
		data3.setKey(TxnEnrollmentProperties.SALES_BUYER);
		data3.setValue("buyer");

		Data data4 = new Data();
		data4.setKey(TxnEnrollmentProperties.FARMER_ID);
		data4.setValue("000323");

		Data data5 = new Data();
		data5.setKey(TxnEnrollmentProperties.FARMER_NAME);
		data5.setValue("LOGU");

		Data data6 = new Data();
		data6.setKey(TxnEnrollmentProperties.FARM_ID);
		data6.setValue("2016040818394149");

		Data data7 = new Data();
		data7.setKey(TxnEnrollmentProperties.TRANSPORTER_NAME);
		data7.setValue("001002-TN");

		Data data8 = new Data();
		data8.setKey(TxnEnrollmentProperties.VEHICLE_NUMBER);
		data8.setValue("TN-0020-45");

		Data data9 = new Data();
		data9.setKey(TxnEnrollmentProperties.INVOICE_DETAIL);
		data9.setValue("12222");
		
		Data data10 = new Data();
		data10.setKey(TxnEnrollmentProperties.TOTAL);
		data10.setValue("20000");
		
		Data data11 = new Data();
		data11.setKey(TxnEnrollmentProperties.CROP_LIST);
		data11.setValue("crops");
		
		Data data12 = new Data();
		data12.setKey(TxnEnrollmentProperties.RECEIPT_NO);
		data12.setValue("ReciptNo");*/

		List<Data> productData = new ArrayList<Data>();
		productData.add(data1);
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
		System.out.println("Status Msg  : " + response.getStatus().getMessage());

	}
}
