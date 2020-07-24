/*
 * FarmerQueryMigration.java
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

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.StringUtil;

public class FarmerQueryMigration {
	
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("dd-MM-yyyy");
	String fileName="";
	@SuppressWarnings({ "deprecation", "deprecation" })
	public static void main(String args[]) throws IOException, NumberFormatException, SQLException {
		System.out.println("Enter File Path");
		String fileName = MysqlUtility.getFilePath() + "\\Farmer.xls";
		String outputPath = MysqlUtility.getOutPutPath(fileName);
		FileInputStream myInput = new FileInputStream(fileName);

		FileOutputStream myOutput = new FileOutputStream(
				outputPath + "\\1_FarmerInsertQuery_" + fileNameDateFormat.format(new Date()) + ".sql");

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
		HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
		HSSFSheet mySheet = myWorkBook.getSheetAt(0);

		StringBuilder sb = new StringBuilder();
		String initialQuery = "INSERT INTO FARMER(FARMER_ID,FARMER_CODE,FIRST_NAME,LAST_NAME,GENDER,DOB,NO_OF_FAMILY_MEMEBERS,DOJ,LATITUDE,LONGITUDE,ADDRESS,CITY_ID,VILLAGE_ID,PIN_CODE,POST_OFFICE,PHONE_NUMBER,MOBILE_NUMBER,EMAIL,SAMITHI_ID) VALUES(";
		String accountInsertQuery = "INSERT INTO ESE_ACCOUNT VALUES (NULL,\"22";
		String contractQuery = "INSERT INTO CONTRACT VALUES (NULL,\"";
		String cardInsertQuery = "INSERT INTO ESE_CARD VALUES (NULL,\"12";
		String contarctPricePatternQuery = "INSERT INTO CONTRACT_PRICEPATTERN_MAP VALUES ('";
		String nullString = null;

		int farmerSeq = MysqlUtility.getFarmerIdSeq();
		int accountSeq = MysqlUtility.getEseAccSeq();
		int contractSeq = MysqlUtility.getContractSeq();
		int contractPriceSeq = MysqlUtility.getContractSeq();
		int cardSeq = MysqlUtility.getCardSeq();

		int i = 1;
		int rowCount = mySheet.getLastRowNum();
		System.out.println("*******ROW COUNT***********" + rowCount);

		while (i <= rowCount) {
			
			HSSFRow myRow = mySheet.getRow(i);
			System.out.println("----------ROW_NO:" + i + "-----------");
			 String farmerId = getExact(String.valueOf(farmerSeq++), 6);
			String farmerCode = !StringUtil.isEmpty(myRow.getCell(0))
					? String.valueOf(myRow.getCell(0).getStringCellValue()) : nullString;

			String farmerName = !StringUtil.isEmpty(myRow.getCell(1).getStringCellValue())
					? String.valueOf(myRow.getCell(1).getStringCellValue()) : nullString;

			String lastName = !StringUtil.isEmpty(myRow.getCell(2))
					? String.valueOf(myRow.getCell(2).getStringCellValue()) : nullString;

			Date dob = !StringUtil.isEmpty(myRow.getCell(3))
					? (myRow.getCell(3).getDateCellValue()) : null;

			String gender = !StringUtil.isEmpty(myRow.getCell(4).getStringCellValue())
					? String.valueOf(myRow.getCell(4).getStringCellValue()) : nullString;

			String noOffamilyMembers = !StringUtil.isEmpty(myRow.getCell(5))
					? String.valueOf(myRow.getCell(5).getStringCellValue()) : nullString;

			Date reg = !StringUtil.isEmpty(myRow.getCell(6))
					? (myRow.getCell(6).getDateCellValue()) : null;


			String latitude = !StringUtil.isEmpty(myRow.getCell(7))
					? String.valueOf(myRow.getCell(7).getStringCellValue()) : nullString;

			String longitude = !StringUtil.isEmpty(myRow.getCell(8))
					? String.valueOf(myRow.getCell(8).getStringCellValue()) : nullString;

			String address = !StringUtil.isEmpty(myRow.getCell(9))
					? String.valueOf(myRow.getCell(9).getStringCellValue()) : nullString;

			String country = !StringUtil.isEmpty(myRow.getCell(10))
					? String.valueOf(myRow.getCell(10).getStringCellValue()) : nullString;

			String state = !StringUtil.isEmpty(myRow.getCell(11))
					? String.valueOf(myRow.getCell(11).getStringCellValue()) : nullString;

			String district = !StringUtil.isEmpty(myRow.getCell(12))
					? String.valueOf(myRow.getCell(12).getStringCellValue()) : nullString;

			String city = !StringUtil.isEmpty(myRow.getCell(13))
					? String.valueOf(myRow.getCell(13).getStringCellValue()) : nullString;

			String gpanchayat = !StringUtil.isEmpty(myRow.getCell(14))
					? String.valueOf(myRow.getCell(14).getStringCellValue()) : nullString;

			String village = !StringUtil.isEmpty(myRow.getCell(15))
					? String.valueOf(myRow.getCell(15).getStringCellValue()) : nullString;

			String servicePoint = !StringUtil.isEmpty(myRow.getCell(16))
					? String.valueOf(myRow.getCell(16).getStringCellValue()) : nullString;

			String postCode = !StringUtil.isEmpty(myRow.getCell(17))
					? String.valueOf(myRow.getCell(17).getStringCellValue()) : nullString;

			String postOffice = !StringUtil.isEmpty(myRow.getCell(18))
					? String.valueOf(myRow.getCell(18).getStringCellValue()) : nullString;

			String phnNumber = !StringUtil.isEmpty(myRow.getCell(19))
					? String.valueOf(myRow.getCell(19).getStringCellValue()) : nullString;

			String mobileNumber = !StringUtil.isEmpty(myRow.getCell(20))
					? String.valueOf(myRow.getCell(20).getStringCellValue()) : nullString;

			String email = !StringUtil.isEmpty(myRow.getCell(21))
					? String.valueOf(myRow.getCell(21).getStringCellValue()) : nullString;

			sb.append(initialQuery);
			// CITY_ID,VILLAGE_ID,PIN_CODE,POST_OFFICE,PHONE_NUMBER,MOBILE_NUMBER)
			// VALUES("
			sb.append("'" + farmerId + "',");
			sb.append("'" + farmerCode + "',");
			sb.append("'" + farmerName + "',");
			sb.append("'" + lastName + "',");
			sb.append("'" + gender + "',");
			
			String dobString = null;
            if (!StringUtil.isEmpty(dob)) {
                 dobString = DateUtil.convertDateToString(dob, DateUtil.DATABASE_DATE_FORMAT);
             }
			
			sb.append("'" + dobString + "',");
			sb.append("'" + noOffamilyMembers + "',");
			
			String regString = null;
            if (!StringUtil.isEmpty(reg)) {
                 regString = DateUtil.convertDateToString(reg, DateUtil.DATABASE_DATE_FORMAT);
             }
			
			sb.append("'" + regString + "',");

			sb.append("'" + latitude + "',");
			sb.append("'" + longitude + "',");
			sb.append("'" + address + "',");

			sb.append("(SELECT id FROM CITY WHERE NAME='" + city+ "' AND LOCATION_ID=(SELECT id from location_detail where name='"+district+"' )),");
			sb.append("(SELECT id FROM village WHERE NAME='" + village+ "'),");
			sb.append("'" + postCode + "',");
			sb.append("'" + postOffice + "',");

			sb.append("'" + phnNumber+ "',");
			sb.append("'" + mobileNumber + "',");
			sb.append("'" + email + "',");
			sb.append("(SELECT ID FROM WAREHOUSE WHERE NAME='Jibonnagar Branch'));\n");
			//);\n
			
			sb.append(accountInsertQuery);
            String accountNo = getExact(String.valueOf(accountSeq++), 10);
            sb.append(accountNo);
            sb.append("\",'SB','3',CURDATE(),'1',CURDATE(),CURDATE(),'"+farmerId+"','0','0','0','0','0','0');\n");

            
            
            
            sb.append(contractQuery);
            String contractNo = getExact(String.valueOf(contractSeq++), 6);
            sb.append(contractNo);
            sb.append("\",(SELECT ID FROM FARMER WHERE FARMER_ID=\"");
            sb.append(farmerId);
            sb.append("\"),null,1,null,(SELECT ID FROM ESE_ACCOUNT WHERE PROFILE_ID=\"");
            sb.append(farmerId);
            sb.append("\"),0.000,0.00,1);\n");
            
           /* sb.append(contarctPricePatternQuery);
            sb.append(contractPriceSeq+"',"+"1);\n");
            sb.append(contarctPricePatternQuery);
            sb.append(contractPriceSeq+"',"+"2);\n");
            sb.append(contarctPricePatternQuery);
            sb.append(contractPriceSeq+"',"+"3);\n");
            sb.append(contarctPricePatternQuery);
            sb.append(contractPriceSeq+"',"+"4);\n");*/
            
            contractPriceSeq++;
            
            sb.append(cardInsertQuery);
            String cardNo = getExact(String.valueOf(cardSeq++), 10);
            sb.append(cardNo);
            sb.append("\",NULL,'2',CURDATE(),'0',NOW(),NOW(),\"");
            sb.append(farmerId);
            sb.append("\",'0');\n\n");
			
			//System.out.println(sb.toString());
			i++;
		}
		
		sb.append("UPDATE ESE_SEQ SET SEQ_VAL=");
        sb.append(contractSeq - 1);
        sb.append(" WHERE SEQ_KEY='CONTRACT_NO_SEQ';\n");
        
        sb.append("UPDATE ESE_SEQ SET SEQ_VAL=");            
        sb.append(accountSeq - 1);
        sb.append(" WHERE SEQ_KEY='FARMER_ACCOUNT_NO_SEQ';\n");
        
        sb.append("UPDATE ESE_SEQ SET SEQ_VAL=");
        sb.append(cardSeq - 1);
        sb.append(" WHERE SEQ_KEY='FARMER_CARD_ID_SEQ';\n");
        
        sb.append("UPDATE FARMER_ID_SEQ SET WEB_SEQ=");
        sb.append(farmerSeq - 1);
        sb.append(";\n");
        
        baos.write(sb.toString().replace("'null'","NULL").getBytes());
        myOutput.write(baos.toByteArray());
        System.out.println("------Farmer Query Generated Successfully-------");
        
     /*   StringBuilder farmQuery = FarmQueryMigration.generateFarmQuery(fileName);
        StringBuilder farmDetailQuery = FarmDetailInfoQueryMigration.generateFarmDetailQuery(fileName);
        StringBuilder farmICSQuery = FarmICSQueryMigration.generateFarmICSQuery(fileName);*/
        
        
	}

	public static String getExact(String value, int count) {

		return ((value.length() > count) ? (value.substring(0, count))
				: (getEmptyLength(count - value.length())) + value);
	}

	public static String getEmptyLength(int count) {

		StringBuffer data = new StringBuffer();
		for (int i = 0; i < count; i++)
			data.append("0");
		return data.toString();
	}
}
