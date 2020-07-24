package com.sourcetrace.ws.txn.adapter;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

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

public class CropInspectionUploadTest {
    private static Logger LOGGER = Logger.getLogger(CropInspectionUploadTest.class);

    private static final String LOCALHOST = "http://localhost:8090/tserv/rs";
    private static final String format = "application/json";
    

    /**
     * The main method.
     * @param args the arguments
     */
    @SuppressWarnings("unchecked")
    public static void main(String args[]) {
        
        DateFormat formatter = new SimpleDateFormat(DateUtil.TXN_DATE_TIME);

        WebClient client = WebClient.create(LOCALHOST);
        client.path("processTxnRequest").accept(format).type(format);
        Request request = new Request();
        Head head = new Head();
        head.setSerialNo("54311db0887a400d8ede4fbdc2d1afac");
        head.setAgentId("111222");
        head.setAgentToken("111111");
        head.setServPointId("SP001");
        head.setTxnTime(formatter.format(new Date()));
        head.setTxnType(TransactionTypeProperties.CROP_INSPECTION_UPLOAD);
        head.setOperType(TransactionProperties.REGULAR_TXN);
        head.setMode(TransactionProperties.ONLINE_MODE);
        head.setResentCount("0");
        head.setMsgNo(DateUtil.getUniqueDate());
        
        Map mainDataMap = new HashMap();

        mainDataMap.put(TxnEnrollmentProperties.SEASON_CODE, "SE12016");
        mainDataMap.put(TxnEnrollmentProperties.FARMER_ID, "000002");
        mainDataMap.put(TxnEnrollmentProperties.ANSWER_DATE, "2016-01-28 12:43:43");
        mainDataMap.put(TxnEnrollmentProperties.CERTIFICATE_CATEGORY, "CC001");
        mainDataMap.put(TxnEnrollmentProperties.STANDARDS, buildInspectionStandards());
        mainDataMap.put(TxnEnrollmentProperties.SECTIONS, buildFarmerSectionAnswers());
                
        mainDataMap.put(TxnEnrollmentProperties.FARM_ID, "20160524133753345");
        mainDataMap.put(TxnEnrollmentProperties.CROP_INSPECTION_FARM_LATITUDE, "13.45627");
        mainDataMap.put(TxnEnrollmentProperties.CROP_INSPECTION_FARM_LONGITUDE, "11.98765");
        mainDataMap.put(TxnEnrollmentProperties.LAND_HOLDING, "15 Acres");
        mainDataMap.put(TxnEnrollmentProperties.CROP_YIELD_LIST, buildCropYieldList());       
        
        List<Data> dataList = CollectionUtil.mapToList(mainDataMap);

        Body body = new Body();
        body.setData(dataList);

        request.setHead(head);
        request.setBody(body);

        Response response = client.post(request, Response.class);

        LOGGER.info(response.getStatus().getCode());
        LOGGER.info(response.getStatus().getMessage());
    }

    private static Collection buildInspectionStandards() {

        Collection collection = new Collection();

        Data standardCode = new Data();
        List<Data> dataList = new ArrayList<Data>();
        Object object = new Object();
        List<Object> objectList = new ArrayList<Object>();

        standardCode.setKey(TxnEnrollmentProperties.STANDARD_CODE);
        standardCode.setValue("ST0001");

        dataList.add(standardCode);
        object.setData(dataList);
        objectList.add(object);
        
        collection.setObject(objectList);

        return collection;
    }

    /**
     * Build farmer section answers.
     * @return the collection
     */
    private static Collection buildFarmerSectionAnswers() {

        Collection collection = new Collection();
        Data sectionCodeData = new Data();
        Data questionsData = new Data();
        List<Data> dataList = new ArrayList<Data>();
        Object object = new Object();
        List<Object> objectList = new ArrayList<Object>();

        sectionCodeData.setKey(TxnEnrollmentProperties.SECTION_CODE);
        sectionCodeData.setValue("S01");
        dataList.add(sectionCodeData);

        questionsData.setKey(TxnEnrollmentProperties.QUESTIONS);
        questionsData.setCollectionValue(buildFarmerQuestionAnswers());
        dataList.add(questionsData);

        object.setData(dataList);
        objectList.add(object);
        collection.setObject(objectList);

        return collection;
    }

    /**
     * Build farmer question answers.
     * @return the collection
     */
    private static Collection buildFarmerQuestionAnswers() {

        Collection collection = new Collection();

        com.sourcetrace.eses.txn.schema.Data questionCodeData = new Data();
        com.sourcetrace.eses.txn.schema.Data answersData = new Data();
        com.sourcetrace.eses.txn.schema.Data latitudeData = new Data();
        com.sourcetrace.eses.txn.schema.Data longititudeData = new Data();
        com.sourcetrace.eses.txn.schema.Data captureDateTimeData = new Data();
        
        List<Data> dataList = new ArrayList<Data>();
        Object object = new Object();
        List<Object> objectList = new ArrayList<Object>();

        questionCodeData.setKey(TxnEnrollmentProperties.QUESTION_CODE);
        questionCodeData.setValue("Q0001");
        dataList.add(questionCodeData);

        answersData.setKey(TxnEnrollmentProperties.ANSWERS);
        answersData.setCollectionValue(buildAnswerObjList());
        dataList.add(answersData);
        
        latitudeData.setKey(TxnEnrollmentProperties.QUESTION_LATITUDE);
        latitudeData.setValue("15.897");
        dataList.add(latitudeData);
        
        longititudeData.setKey(TxnEnrollmentProperties.QUESTION_LONGITUDE);
        longititudeData.setValue("67.463");
        dataList.add(longititudeData);
        
        captureDateTimeData.setKey(TxnEnrollmentProperties.GPS_CAPTURE_DATE_TIME);
        captureDateTimeData.setValue("2013-09-25 14:01:15");
        dataList.add(captureDateTimeData);

        object.setData(dataList);
        objectList.add(object);
        
        List<Data> dataList1 = new ArrayList<Data>();
        Object object1 = new Object();
        com.sourcetrace.eses.txn.schema.Data questionCodeData1 = new Data();
        com.sourcetrace.eses.txn.schema.Data answersData1 = new Data();
        com.sourcetrace.eses.txn.schema.Data latitudeData1 = new Data();
        com.sourcetrace.eses.txn.schema.Data longititudeData1 = new Data();
        com.sourcetrace.eses.txn.schema.Data captureDateTimeData1 = new Data();
        
        questionCodeData1.setKey(TxnEnrollmentProperties.QUESTION_CODE);
        questionCodeData1.setValue("Q0002");
        dataList1.add(questionCodeData1);
        
        answersData1.setKey(TxnEnrollmentProperties.ANSWERS);
        answersData1.setCollectionValue(buildAnswerObjList1());
        dataList1.add(answersData1);
        
        latitudeData1.setKey(TxnEnrollmentProperties.QUESTION_LATITUDE);
        latitudeData1.setValue("56.64");
        dataList1.add(latitudeData1);
        
        longititudeData1.setKey(TxnEnrollmentProperties.QUESTION_LONGITUDE);
        longititudeData1.setValue("89.32");
        dataList1.add(longititudeData1);
        
        captureDateTimeData1.setKey(TxnEnrollmentProperties.GPS_CAPTURE_DATE_TIME);
        captureDateTimeData1.setValue("2013-09-25 14:02:36");
        dataList1.add(captureDateTimeData1);
        
        object1.setData(dataList1);
        objectList.add(object1);

        collection.setObject(objectList);

        return collection;
    }

    /**
     * Build farmer sub question answers.
     * @return the collection
     */
    public static Collection buildFarmerSubQuestionAnswers() {

        Collection collection = new Collection();

        com.sourcetrace.eses.txn.schema.Data questionCodeData = new Data();
        com.sourcetrace.eses.txn.schema.Data followupData = new Data();
        com.sourcetrace.eses.txn.schema.Data answersData = new Data();
        Data photo1 = new Data();

        List<Data> dataList = new ArrayList<Data>();
        Object object = new Object();
        List<Object> objectList = new ArrayList<Object>();

        questionCodeData.setKey(TxnEnrollmentProperties.QUESTION_CODE);
        questionCodeData.setValue("Q0060");
        dataList.add(questionCodeData);

        followupData.setKey(TxnEnrollmentProperties.FOLLOW_UP);
        followupData.setValue("1");
        dataList.add(followupData);

        photo1.setKey(TxnEnrollmentProperties.PHOTO);
        FileDataSource dataSource = new FileDataSource(new File("C:/Users/preethi/Desktop/icon/male-icon.png"));
        DataHandler handler = new DataHandler(dataSource);
        photo1.setBinaryValue(handler);
        dataList.add(photo1);

        answersData.setKey(TxnEnrollmentProperties.ANSWERS);
        answersData.setCollectionValue(buildAnswerObjList());
        dataList.add(answersData);

        object.setData(dataList);
        objectList.add(object);

        collection.setObject(objectList);

        return collection;
    }

    /**
     * Build answer obj list.
     * @return the com.sourcetrace.eses.txn.schema. collection
     */
    private static com.sourcetrace.eses.txn.schema.Collection buildAnswerObjList() {

        com.sourcetrace.eses.txn.schema.Collection collection = new Collection();
        List<com.sourcetrace.eses.txn.schema.Object> objectList = new ArrayList<Object>();

        com.sourcetrace.eses.txn.schema.Object object1 = new Object();

        com.sourcetrace.eses.txn.schema.Data ansType1 = new com.sourcetrace.eses.txn.schema.Data();
        com.sourcetrace.eses.txn.schema.Data answer1 = new com.sourcetrace.eses.txn.schema.Data();
        com.sourcetrace.eses.txn.schema.Data answer2 = new com.sourcetrace.eses.txn.schema.Data();

        List<com.sourcetrace.eses.txn.schema.Data> dataList1 = new ArrayList<com.sourcetrace.eses.txn.schema.Data>();

        ansType1.setKey(TxnEnrollmentProperties.ANSWER_TYPE);
        ansType1.setValue("2");
        dataList1.add(ansType1);

        answer1.setKey(TxnEnrollmentProperties.ANSWER);
        answer1.setValue("0");
        dataList1.add(answer1);

        answer2.setKey(TxnEnrollmentProperties.ANSWER_1);
        answer2.setValue("No. It is been converted except Animal Husbandary");
        dataList1.add(answer2);

        object1.setData(dataList1);
        objectList.add(object1);
        collection.setObject(objectList);

        return collection;
    }
    
    private static com.sourcetrace.eses.txn.schema.Collection buildAnswerObjList1() {

        com.sourcetrace.eses.txn.schema.Collection collection = new Collection();
        List<com.sourcetrace.eses.txn.schema.Object> objectList = new ArrayList<Object>();

        com.sourcetrace.eses.txn.schema.Object object1 = new Object();

        com.sourcetrace.eses.txn.schema.Data ansType1 = new com.sourcetrace.eses.txn.schema.Data();
        com.sourcetrace.eses.txn.schema.Data answer1 = new com.sourcetrace.eses.txn.schema.Data();
        com.sourcetrace.eses.txn.schema.Data answer2 = new com.sourcetrace.eses.txn.schema.Data();
        com.sourcetrace.eses.txn.schema.Data answer3 = new com.sourcetrace.eses.txn.schema.Data();

        List<com.sourcetrace.eses.txn.schema.Data> dataList1 = new ArrayList<com.sourcetrace.eses.txn.schema.Data>();

        ansType1.setKey(TxnEnrollmentProperties.ANSWER_TYPE);
        ansType1.setValue("2");
        dataList1.add(ansType1);

        answer1.setKey(TxnEnrollmentProperties.ANSWER);
        answer1.setValue("27");
        dataList1.add(answer1);

        answer2.setKey(TxnEnrollmentProperties.ANSWER_1);
        answer2.setValue("Due to heavy rain, not reached the farm land");
        dataList1.add(answer2);
        
        answer3.setKey(TxnEnrollmentProperties.ANSWER_2);
        answer3.setValue("Reason to get delayed");
        dataList1.add(answer3);

        object1.setData(dataList1);
        objectList.add(object1);
        collection.setObject(objectList);

        return collection;
    }
    
    private static com.sourcetrace.eses.txn.schema.Collection  buildCropYieldList(){
        
        com.sourcetrace.eses.txn.schema.Collection collection = new Collection();
        List<com.sourcetrace.eses.txn.schema.Object> objectList = new ArrayList<Object>();

        com.sourcetrace.eses.txn.schema.Object object1 = new Object();
        com.sourcetrace.eses.txn.schema.Object object2 = new Object();

        com.sourcetrace.eses.txn.schema.Data farmCropsCode = new com.sourcetrace.eses.txn.schema.Data();
        com.sourcetrace.eses.txn.schema.Data yield = new com.sourcetrace.eses.txn.schema.Data();        

        List<com.sourcetrace.eses.txn.schema.Data> dataList1 = new ArrayList<com.sourcetrace.eses.txn.schema.Data>();

        farmCropsCode.setKey(TxnEnrollmentProperties.FARM_CROPS_CODE);
        farmCropsCode.setValue("FC001");
        dataList1.add(farmCropsCode);

        yield.setKey(TxnEnrollmentProperties.YIELD);
        yield.setValue("67.890");
        dataList1.add(yield);

        object1.setData(dataList1);
        
        List<com.sourcetrace.eses.txn.schema.Data> dataList2 = new ArrayList<com.sourcetrace.eses.txn.schema.Data>();
        
        farmCropsCode = new com.sourcetrace.eses.txn.schema.Data();
        farmCropsCode.setKey(TxnEnrollmentProperties.FARM_CROPS_CODE);
        farmCropsCode.setValue("FC001");
        dataList2.add(farmCropsCode);
        
        yield = new com.sourcetrace.eses.txn.schema.Data();
        yield.setKey(TxnEnrollmentProperties.YIELD);
        yield.setValue("67.890");
        dataList2.add(yield);

        object2.setData(dataList1);
        
        objectList.add(object1);
        objectList.add(object2);
        collection.setObject(objectList);

        return collection;
    }
}
