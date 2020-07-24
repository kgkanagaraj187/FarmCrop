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

public class SupplierDownloadTest 
{
    /**
     * @param args
     */
    private static Logger LOGGER = Logger.getLogger(SupplierDownloadTest.class);

    private static final String LOCALHOST = "http://localhost:8090/tserv/rs";
    private static final String format = "application/json";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.TXN_TIME_FORMAT);
    /**
     * The main method.
     * @param args the arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {

        WebClient client = WebClient.create(LOCALHOST);
        client.path("processTxnRequest").accept(format).type(format);
        Request request = new Request();
        Head head = new Head();
        head.setVersionNo("1.00");
        head.setSerialNo("1234567890");
        head.setAgentId("555");
        head.setAgentToken("1EBE1622CEAAC2EB3268EF937E20973C");
        head.setServPointId("");
        head.setTxnTime(sdf.format(new Date()));
        head.setTxnType(TransactionTypeProperties.SUPPLIER_DOWNLOAD);
        head.setOperType(TransactionProperties.REGULAR_TXN);
        head.setMode(TransactionProperties.ONLINE_MODE);
        head.setResentCount("0");
        head.setMsgNo("1234567");

        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put(TransactionProperties.SUPPLIER_DOWNLOAD_REVISION_NO, "0");

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

    }

}
