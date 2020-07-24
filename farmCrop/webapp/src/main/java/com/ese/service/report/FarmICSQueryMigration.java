/*
 * FarmQueryMigration.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.service.report;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.sourcetrace.eses.util.ObjectUtil;

public class FarmICSQueryMigration {
    private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("dd-MM-yyyy");

  
    public static void main(String args[]) throws IOException {
        
        String fileName = "E:\\Masterdata\\NEW\\M4\\Farmer.xls";
        FileInputStream myInput = new FileInputStream(fileName);
        FileOutputStream myOutput = new FileOutputStream(
                "D:\\santhosh-Aditi-pro-doc\\New_folder\\New\\4_FarmICSInsertQuery__"
                + fileNameDateFormat.format(new Date()) + ".sql");
       
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
        HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
        HSSFSheet mySheet = myWorkBook.getSheetAt(2);

        StringBuilder sb = new StringBuilder();
        String initialQuery = "INSERT INTO FARM_ICS (FARM_ID,ICS_TYPE,SURVEY_NO,INSPECTION_LEVEL,LAST_INSEPCTION_LEVEL,STATUS) VALUES(";
     
        int i = 1;
        int rowCount = mySheet.getLastRowNum();
        int farmIdSeq = 9851;
        int farmDetailInfoIdSeq = 9887;
        Map<String, String> farmMap = new HashMap<String, String>();
        System.out.println("*******ROW COUNT***********"+rowCount);
        
        try {

            while (i <= rowCount) {
                HSSFRow myRow = mySheet.getRow(i);
               
                String farmId=getExact(String.valueOf(farmIdSeq++), 6);
                String surveyNumber = "";
                if(!ObjectUtil.isEmpty(myRow.getCell(0))){
                    double snum = myRow.getCell(1).getNumericCellValue();
                    surveyNumber = String.valueOf(snum).trim();
                   surveyNumber = myRow.getCell(0).getStringCellValue();
                    
                }
                    
                    sb.append(initialQuery);
                    String farmIdQuery="(SELECT ID FROM FARM WHERE FARM_ID='";
                    sb.append(farmIdQuery+farmId+"'),");
                    sb.append("'1','"); 
                    sb.append(surveyNumber+"','0','0','0');\n");
                    i++;

                }
               
         
            
            baos.write(sb.toString().getBytes());
            myOutput.write(baos.toByteArray());
            System.out.println("------Query Generated Successfully-------");

        } catch (Exception e) {
            System.out.println("Exception:" + e);
            System.out.println("----------ROW_NO:" + i + "-----------");
            e.printStackTrace();
        }
    }
    
  
    public static String getExact(String value, int count) {

        return ((value.length() > count) ? (value.substring(0, count)) : (getEmptyLength(count
                - value.length()))
                + value);
    }

    public static String getEmptyLength(int count) {

        StringBuffer data = new StringBuffer();
        for (int i = 0; i < count; i++)
            data.append("0");
        return data.toString();
    }

    public static StringBuilder generateFarmICSQuery(String filePath) throws IOException, SQLException{

        
        //String fileName = "E:\\Masterdata\\NEW\\M4\\Farmer.xls";
        /*FileInputStream myInput = new FileInputStream(filePath);
        String outputPath=MysqlUtility.getOutPutPath(filePath);
        FileOutputStream myOutput = new FileOutputStream(
                "D:\\santhosh-Aditi-pro-doc\\New_folder\\New\\2_FarmICSQuery_"
                + fileNameDateFormat.format(new Date()) + ".sql");
        
        FileOutputStream myOutput1 = new FileOutputStream(outputPath+"\\4_FarmICSQuery_"
                + fileNameDateFormat.format(new Date()) + ".sql");*/
    	
    	FileInputStream myInput = new FileInputStream(filePath);
		String outputPath = MysqlUtility.getOutPutPath(filePath);
		
		FileOutputStream myOutput = new FileOutputStream(
				outputPath + "\\3_FarmICSQuery_" + fileNameDateFormat.format(new Date()) + ".sql");
       
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
        HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
        HSSFSheet mySheet = myWorkBook.getSheetAt(2);

        StringBuilder sb = new StringBuilder();
        String initialQuery = "INSERT INTO FARM_ICS (ID,ICS_TYPE,SURVEY_NO,STATUS) VALUES(";
     
        int i = 1;
        int rowCount = mySheet.getLastRowNum();
        int farmIdSeq = MysqlUtility.getFarmPrimaryKeyId();
        int farmDetailInfoIdSeq = MysqlUtility.getFarmDetailedInfoIdSeq();
        Map<String, String> farmMap = new HashMap<String, String>();
        System.out.println("*******ROW COUNT***********"+rowCount);
        
        try {

            while (i <= rowCount) {
                HSSFRow myRow = mySheet.getRow(i);
                Integer farmId=farmIdSeq++;
              //  String farmId=getExact(String.valueOf(farmIdSeq++), 6);
                String surveyNumber = "";
                if(!ObjectUtil.isEmpty(myRow.getCell(0))){
                   // double snum = myRow.getCell(1).getNumericCellValue();
                    //surveyNumber = String.valueOf(0).trim();
                   surveyNumber = myRow.getCell(0).getStringCellValue();
                    
                }
                    
                    sb.append(initialQuery);
                    String farmIdQuery="(SELECT ID FROM FARM WHERE ID='";
                    sb.append(farmIdQuery+farmId+"'),");
                    sb.append("'0','"); 
                    sb.append(surveyNumber+"','0');\n");
                    i++;

                }
               
         
            
            baos.write(sb.toString().getBytes());
            myOutput.write(baos.toByteArray());
            System.out.println("------Farm ICS Query Generated Successfully-------");

        } catch (Exception e) {
            sb=null;
            System.out.println("Exception:" + e);
            System.out.println("----------ROW_NO:" + i + "-----------");
            e.printStackTrace();
        }
        return sb;
    
        
    }
}
