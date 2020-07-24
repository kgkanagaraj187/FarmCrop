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
import com.sourcetrace.eses.util.StringUtil;

public class FarmDetailInfoQueryMigration {
    private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("dd-MM-yyyy");

  
    public static void main(String args[]) throws IOException, SQLException {
        
        String fileName = "E:\\Masterdata\\NEW\\M4\\Farmer.xls";
        FileInputStream myInput = new FileInputStream(fileName);
        FileOutputStream myOutput = new FileOutputStream(
                "D:\\santhosh-Aditi-pro-doc\\New_folder\\New\\3_FarmDatailInfoInsertQuery_"
                + fileNameDateFormat.format(new Date()) + ".sql");
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
        HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
        HSSFSheet mySheet = myWorkBook.getSheetAt(1);

        StringBuilder sb = new StringBuilder();
        
      String alterQuery= "ALTER TABLE `farm_detailed_info` MODIFY COLUMN `CONV_CROPS`  varchar(90) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER `CONV_EST_YIELD`;\n\n";
      sb.append(alterQuery);
      
        String initialQuery = "INSERT INTO FARM_DETAILED_INFO (IS_FARM_ADDRESS_SAME_AS_FARMERS,FARM_ADDRESS,FARM_AREA,FARM_PRODUCTIVE_AREA,WATER_BODIES_COUNT,FULL_TIME_WORKER_COUNT,FARM_OWNED,AREA_UNDER_IRRIGATION,IRRIGATION_SOURCE,SOIL_TYPE,LAST_DATE_CHE_APP,CONV_CROPS,CONV_LAND,FARM_IRRIGATION,IRRIGATION_METHOD,SOIL_FERTILITY,ICS_STATUS) VALUES ('";

        int i = 1;
        int rowCount = mySheet.getLastRowNum();
        int farmIdSeq = MysqlUtility.getFarmIdSeq();
        int farmDetailInfoIdSeq =MysqlUtility.getFarmDetailedInfoIdSeq();
        System.out.println("*******ROW COUNT***********"+rowCount);
        
        Map<String, String> farmMap = new HashMap<String, String>();

        try {

            while (i <= rowCount) {
                HSSFRow myRow = mySheet.getRow(i);
               
                String farmId=getExact(String.valueOf(farmIdSeq++), 6);
               
                String farmAddress = myRow.getCell(2).getStringCellValue().trim();
                sb.append(initialQuery);
                sb.append("0"+"',");
                sb.append(farmAddress+",'");
                
                String farmArea="";
                
                if(!ObjectUtil.isEmpty(myRow.getCell(3))){
                   farmArea=  myRow.getCell(3).getStringCellValue();
                    double tempFarmArea = myRow.getCell(3).getNumericCellValue();
                    farmArea = String.valueOf(tempFarmArea);
                }
                sb.append(farmArea+"','");
               
                String farmProductiveArea = "";
                
                if(!ObjectUtil.isEmpty(myRow.getCell(4))){
                    double temp = myRow.getCell(4).getNumericCellValue();
                    farmProductiveArea = String.valueOf(temp);
                   farmProductiveArea=  myRow.getCell(4).getStringCellValue();
                }
                sb.append(farmProductiveArea+"',");
                
                String waterbodies = "";
                
               if(!ObjectUtil.isEmpty(myRow.getCell(5))){
                    waterbodies=  myRow.getCell(5).getStringCellValue();
                    sb.append("'"+waterbodies+"',");
                }else{
                    sb.append("'',");  
                }
                
                String fullTimeWorkers = "";
                
                if(!ObjectUtil.isEmpty(myRow.getCell(6))){
                    fullTimeWorkers=  myRow.getCell(6).getStringCellValue();
                }
                sb.append("\'"+fullTimeWorkers+"','");
                
                String farmOwned = "-1";
                
                if(!ObjectUtil.isEmpty(myRow.getCell(7))){
                    farmOwned=  myRow.getCell(7).getStringCellValue();
                }
                sb.append(farmOwned+"','");
                
                String areaUnderIrrigation = "";
                
                if(!ObjectUtil.isEmpty(myRow.getCell(8))){
                    areaUnderIrrigation=  myRow.getCell(8).getStringCellValue();
                }
                sb.append(areaUnderIrrigation+"','");
                
                String irrigationSource = "-1";
                
                if(!ObjectUtil.isEmpty(myRow.getCell(9))){
                    irrigationSource=  myRow.getCell(9).getStringCellValue().trim();
                    if(irrigationSource.equalsIgnoreCase("Borewell")){
                        irrigationSource ="1";
                    }else{
                        irrigationSource ="-1";
                    }
                }
                
                sb.append(irrigationSource+"','");
                
                String soilType = "-1";
                
                if(!ObjectUtil.isEmpty(myRow.getCell(10))){
                    soilType=  myRow.getCell(10).getStringCellValue().trim();
                    if(soilType.equalsIgnoreCase("Red sandy")){
                        soilType ="3";
                    }
                }               
                  
                sb.append(soilType+"','");
                
                    
                  
                
                String weedControl = "-1";
                
                if(!ObjectUtil.isEmpty(myRow.getCell(10))){
                    weedControl=  myRow.getCell(10).getStringCellValue().trim();
                    if(weedControl.equalsIgnoreCase("Red sandy")){
                        weedControl ="3";
                    }
                }  
                
                sb.append(weedControl+"','");
                
                /*String pestDiesMgt = "-1";               
                
                sb.append(pestDiesMgt+"',\"");
                String pestDiesMgtOth="";//myRow.getCell(14).getStringCellValue();
                sb.append(pestDiesMgtOth+"\",'");*/
                
                String lastDateChmiacl="";//myRow.getCell(15).getStringCellValue();
                sb.append(lastDateChmiacl+"',\"");
                
                String conventionalCrops="";//myRow.getCell(16).getStringCellValue();
                sb.append(conventionalCrops+"\",'");
              
                
                String convLand = "";
                
                if(!ObjectUtil.isEmpty(myRow.getCell(17))){
                    convLand=  myRow.getCell(17).getStringCellValue().trim();
                   
                }               
                  
                sb.append(convLand+"','-1','-1','-1','-1','-1','-1','-1','-1');\n");
                
                  
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
    
    
    public static StringBuilder generateFarmDetailQuery(String filePath) throws IOException, SQLException{

        
    	FileInputStream myInput = new FileInputStream(filePath);
		String outputPath = MysqlUtility.getOutPutPath(filePath);
		
		FileOutputStream myOutput = new FileOutputStream(
				outputPath + "\\4_FarmDetailQuery_" + fileNameDateFormat.format(new Date()) + ".sql");
		
		
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
        HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
        HSSFSheet mySheet = myWorkBook.getSheetAt(1);

        StringBuilder sb = new StringBuilder();
        
      String alterQuery= "ALTER TABLE `farm_detailed_info` MODIFY COLUMN `CONV_CROPS`  varchar(90) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER `CONV_EST_YIELD`;\n\n";
      sb.append(alterQuery);
      
        String initialQuery = "INSERT INTO FARM_DETAILED_INFO (IS_FARM_ADDRESS_SAME_AS_FARMERS,FARM_ADDRESS,FARM_AREA,FARM_PRODUCTIVE_AREA,WATER_BODIES_COUNT,FULL_TIME_WORKER_COUNT,FARM_OWNED,AREA_UNDER_IRRIGATION,IRRIGATION_SOURCE,SOIL_TYPE,LAST_DATE_CHE_APP,CONV_CROPS,CONV_LAND,FARM_IRRIGATION,IRRIGATION_METHOD,SOIL_FERTILITY,ICS_STATUS) VALUES ('";

        int i = 1;
        int rowCount = mySheet.getLastRowNum();
        int farmIdSeq = MysqlUtility.getFarmIdSeq();
        int farmDetailInfoIdSeq = MysqlUtility.getFarmDetailedInfoIdSeq();
        System.out.println("*******ROW COUNT***********"+rowCount);
        
        Map<String, String> farmMap = new HashMap<String, String>();

        try {

            while (i <= rowCount) {
                HSSFRow myRow = mySheet.getRow(i);
               
                String farmId=getExact(String.valueOf(farmIdSeq++), 6);
               
                String farmAddress =!StringUtil.isEmpty(myRow.getCell(2))?myRow.getCell(2).getStringCellValue().trim():"";
                sb.append(initialQuery);
                sb.append("0"+"',");
                sb.append("'"+farmAddress+"'"+",'");
                
                String farmArea="";
                
                if(!ObjectUtil.isEmpty(myRow.getCell(3))){
                   farmArea=  String.valueOf(myRow.getCell(3));
                   // double tempFarmArea = myRow.getCell(3).getNumericCellValue();
                    farmArea = String.valueOf(farmArea);
                }
                sb.append(farmArea+"','");
               
                String farmProductiveArea = "";
                
                if(!ObjectUtil.isEmpty(myRow.getCell(4))){
                   // double temp = myRow.getCell(4).getNumericCellValue();
                   // farmProductiveArea = String.valueOf(temp);
                   farmProductiveArea=  String.valueOf(myRow.getCell(4));
                }
                sb.append(farmProductiveArea+"',");
                
                String waterbodies = "";
                
               if(!ObjectUtil.isEmpty(myRow.getCell(5))){
                    waterbodies=  myRow.getCell(5).getStringCellValue();
                    sb.append("'"+waterbodies+"',");
                }else{
                    sb.append("'',");  
                }
                
                String fullTimeWorkers = "";
                
                if(!ObjectUtil.isEmpty(myRow.getCell(6))){
                    fullTimeWorkers=  myRow.getCell(6).getStringCellValue();
                }
                sb.append("\'"+fullTimeWorkers+"','");
                
                String farmOwned = "-1";
                
                if(!ObjectUtil.isEmpty(myRow.getCell(7))){
                    farmOwned=  myRow.getCell(7).getStringCellValue();
                }
                sb.append(farmOwned+"','");
                
                String areaUnderIrrigation = "";
                
                if(!ObjectUtil.isEmpty(myRow.getCell(8))){
                    areaUnderIrrigation=  myRow.getCell(8).getStringCellValue();
                }
                sb.append(areaUnderIrrigation+"','");
                
                String irrigationSource = "-1";
                
                if(!ObjectUtil.isEmpty(myRow.getCell(9))){
                    irrigationSource=  myRow.getCell(9).getStringCellValue().trim();
                    if(irrigationSource.equalsIgnoreCase("Borewell")){
                        irrigationSource ="1";
                    }else{
                        irrigationSource ="-1";
                    }
                }
                
                sb.append(irrigationSource+"','");
                
                String soilType = "-1";
                
                if(!ObjectUtil.isEmpty(myRow.getCell(10))){
                    soilType=  myRow.getCell(10).getStringCellValue().trim();
                    if(soilType.equalsIgnoreCase("Red sandy")){
                        soilType ="3";
                    }
                }               
                  
                sb.append(soilType+"','");
                
                    /*String soilInput= "-1"; 
                
                sb.append(soilInput+"','");*/
                  
                
               /* String weedControl = "-1";
                
                if(!ObjectUtil.isEmpty(myRow.getCell(10))){
                    weedControl=  myRow.getCell(10).getStringCellValue().trim();
                    if(weedControl.equalsIgnoreCase("Red sandy")){
                        weedControl ="3";
                    }
                }  
                
                sb.append(weedControl+"','");*/
                
               /* String pestDiesMgt = "-1";               
                
                sb.append(pestDiesMgt+"',\"");
                String pestDiesMgtOth="";//myRow.getCell(14).getStringCellValue();
                sb.append(pestDiesMgtOth+"\",'");*/
                
                String lastDateChmiacl="";//myRow.getCell(15).getStringCellValue();
                sb.append(lastDateChmiacl+"',\"");
                
                String conventionalCrops="";//myRow.getCell(16).getStringCellValue();
                sb.append(conventionalCrops+"\",'");
              
                
                String convLand = "";
                
                if(!ObjectUtil.isEmpty(myRow.getCell(17))){
                    convLand=  myRow.getCell(17).getStringCellValue().trim();
                   
                }               
                  
                sb.append(convLand+"','-1','-1','-1','-1');\n");
                
                  
                    i++;

                }
               
        
         
            baos.write(sb.toString().getBytes());
            myOutput.write(baos.toByteArray());
            System.out.println("------Frm Detail Query Generated Successfully-------");
            FarmICSQueryMigration.generateFarmICSQuery(filePath);

        } catch (Exception e) {
            sb = null;
            System.out.println("Exception:" + e);
            System.out.println("----------ROW_NO:" + i + "-----------");
            e.printStackTrace();
        }
        return sb;
        
    }
}
