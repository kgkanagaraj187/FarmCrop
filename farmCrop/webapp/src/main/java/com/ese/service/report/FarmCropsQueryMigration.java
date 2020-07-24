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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;

public class FarmCropsQueryMigration {
    private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("dd-MM-yyyy");

 
    public static void main(String args[]) throws IOException {

        String fileName = "D:\\santhosh-Aditi-pro-doc\\New_folder\\New\\FarmsList.XLS";
        FileInputStream myInput = new FileInputStream(fileName);
        FileOutputStream myOutput = new FileOutputStream(
                "D:\\santhosh-Aditi-pro-doc\\New_folder\\New\\FarmCropsInsertQuery_"
                        + fileNameDateFormat.format(new Date()) + ".sql");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
        HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
        HSSFSheet mySheet = myWorkBook.getSheetAt(3);

        StringBuilder sb = new StringBuilder();
        long revisionNumber=DateUtil.getRevisionNumber();
        String farmCropMasterQuery1="INSERT INTO FARM_CROPS_MASTER VALUES ('59','FC000055','Ragi','"+revisionNumber+"');";
        sb.append(farmCropMasterQuery1+"\n");
        
        revisionNumber=DateUtil.getRevisionNumber();
        String farmCropMasterQuery2="INSERT INTO FARM_CROPS_MASTER VALUES ('60','FC000056','Cotton','"+revisionNumber+"');";         
        sb.append(farmCropMasterQuery2+"\n");
        
        
        
        String initialQuery = "INSERT INTO FARM_CROPS (FARM_CROP_CODE,CROP_NAME,CROP_AREA,PRODUCTION_PER_YEAR,CROP_SEASON,CROP_CATEGORY,FARM_ID,FARM_CROPS_MASTER_ID) VALUES(";
     
        int i = 1;
        int rowCount = mySheet.getLastRowNum();
        int farmIdSeq = 34;
        int farmDetailInfoIdSeq = 39;

        Map<String, String> farmMap = new HashMap<String, String>();

        try {

            while (i <= rowCount) {
                HSSFRow myRow = mySheet.getRow(i);
               
                String farmId=getExact(String.valueOf(farmIdSeq++), 6);
               
                
                String cropSeason="";
                String cropCategory="";
                String karifOnionArea="";
                String karifOnionEst="";
                String karifCottonArea="";
                String karifCottonEst="";                
                String rabiRagiArea="";
                String rabiRagiEst="";
                String rabiJawarArea="";
                String rabiJawaEst="";                
                String perinialCoconutArea="";
                String perinialCoconutEst="";
               
                String farmIdQuery="(SELECT ID FROM FARM WHERE FARM_ID='";
                String farmCropMasterIdQuery="(SELECT ID FROM FARM_CROPS_MASTER WHERE CROP_CODE='";
                
                if(!ObjectUtil.isEmpty(myRow.getCell(1)) && !ObjectUtil.isEmpty(myRow.getCell(2))){
                    cropSeason="1";
                    cropCategory="1";
                    karifOnionArea= myRow.getCell(1).getStringCellValue();
                    karifOnionEst= myRow.getCell(2).getStringCellValue();

                    sb.append(initialQuery);
                    
                    String cropMasterCode="FC000046";
                    String cropMasterName="Onion";
                    sb.append("\""+cropMasterCode+"\",\""+cropMasterName+"\",'"+karifOnionArea+"','"+karifOnionEst+"','"+cropSeason+"','"
                            +cropCategory+"',"+farmIdQuery+farmId+"'),"+farmCropMasterIdQuery+cropMasterCode+"')); \n");
                } 
                
                if(!ObjectUtil.isEmpty(myRow.getCell(3)) && !ObjectUtil.isEmpty(myRow.getCell(4))){
                    cropSeason="1";
                    cropCategory="1";
                    karifCottonArea= myRow.getCell(3).getStringCellValue();
                    karifCottonEst= myRow.getCell(4).getStringCellValue();

                    sb.append(initialQuery);
                    String cropMasterCode="FC000056";
                    String cropMasterName="Cotton";
                    sb.append("\""+cropMasterCode+"\",\""+cropMasterName+"\",'"+karifCottonArea+"','"+karifCottonEst+"','"+cropSeason+"','"
                            +cropCategory+"',"+farmIdQuery+farmId+"'),"+farmCropMasterIdQuery+cropMasterCode+"')); \n");
                } 
                
                if(!ObjectUtil.isEmpty(myRow.getCell(5)) && !ObjectUtil.isEmpty(myRow.getCell(6))){
                    cropSeason="2";
                    cropCategory="1";
                    rabiRagiArea= myRow.getCell(5).getStringCellValue();
                    rabiRagiEst= myRow.getCell(6).getStringCellValue();

                    sb.append(initialQuery);
                    String cropMasterCode="FC000055";
                    String cropMasterName="Ragi";
                    sb.append("\""+cropMasterCode+"\",\""+cropMasterName+"\",'"+rabiRagiArea+"','"+rabiRagiEst+"','"+cropSeason+"','"
                            +cropCategory+"',"+farmIdQuery+farmId+"'),"+farmCropMasterIdQuery+cropMasterCode+"')); \n");
                }
                
                if(!ObjectUtil.isEmpty(myRow.getCell(7)) && !ObjectUtil.isEmpty(myRow.getCell(8))){
                    cropSeason="2";
                    cropCategory="1";
                    rabiJawarArea= myRow.getCell(7).getStringCellValue();
                    rabiJawaEst= myRow.getCell(8).getStringCellValue();

                    sb.append(initialQuery);
                    String cropMasterCode="FC000055";
                    String cropMasterName="Ragi";
                    sb.append("\""+cropMasterCode+"\",\""+cropMasterName+"\",'"+rabiJawarArea+"','"+rabiJawaEst+"','"+cropSeason+"','"
                            +cropCategory+"',"+farmIdQuery+farmId+"'),"+farmCropMasterIdQuery+cropMasterCode+"')); \n");
                }
                
                if(!ObjectUtil.isEmpty(myRow.getCell(9)) && !ObjectUtil.isEmpty(myRow.getCell(10))){
                    cropSeason="0";
                    cropCategory="1";
                    perinialCoconutArea= myRow.getCell(9).getStringCellValue();
                    perinialCoconutEst= myRow.getCell(10).getStringCellValue();

                    sb.append(initialQuery);
                    String cropMasterCode="FC000003";
                    String cropMasterName="Coconut";
                    sb.append("\""+cropMasterCode+"\",\""+cropMasterName+"\",'"+perinialCoconutArea+"','"+perinialCoconutEst+"','"+cropSeason+"','"
                            +cropCategory+"',"+farmIdQuery+farmId+"'),"+farmCropMasterIdQuery+cropMasterCode+"')); \n");
                }   
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

}
