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

public class FarmQueryMigration {
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("dd-MM-yyyy");

	public static void main(String args[]) throws IOException, SQLException {
		String fileName = "E:\\Masterdata\\NEW\\M4\\Farmer.xls";
		FileInputStream myInput = new FileInputStream(fileName);
		FileOutputStream myOutput = new FileOutputStream(
				"E:\\Masterdata\\NEW\\M4\\2_FarmInsertQuery_" + fileNameDateFormat.format(new Date()) + ".sql");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
		HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
		HSSFSheet mySheet = myWorkBook.getSheetAt(1);

		StringBuilder sb = new StringBuilder();
		String initialQuery = "INSERT INTO FARM (FARM_ID,FARM_NAME,FARMER_ID,FARM_DETAILED_INFO_ID) VALUES(";
		// String farmerDetailQuery = "INSERT INTO FARM_DETAILED_INFO
		// (ID,FARM_OWNED,FARM_IRRIGATION,IRRIGATION_SOURCE,IRRIGATION_METHOD,SOIL_TYPE,SOIL_FERTILITY,ICS_STATUS)
		// VALUES(";

		int i = 1;
		int rowCount = mySheet.getLastRowNum();
		System.out.println("*******ROW COUNT***********" + rowCount);
		int farmIdSeq = MysqlUtility.getFarmIdSeq();
		int farmDetailInfoIdSeq = MysqlUtility.getFarmDetailedInfoIdSeq();
		int farmerSeq = MysqlUtility.getFarmerIdSeq();

		Map<String, String> farmMap = new HashMap<String, String>();

		try {

			while (i <= rowCount) {
				HSSFRow myRow = mySheet.getRow(i);

				String farmId = getExact(String.valueOf(farmIdSeq++), 6);
				String farmName = myRow.getCell(0).getStringCellValue().trim();
				// String farmerId =
				// myRow.getCell(1).getStringCellValue().trim();
				String farmerId = getExact(String.valueOf(farmerSeq++), 6);

				sb.append(initialQuery);
				sb.append("\"" + farmId + "\"");
				sb.append(",\"");
				sb.append(farmName);
				sb.append("\",");
				// Use Farmer ID in Excel Sheet - After run the Farmer Script
				String farmerIdQuery = "(SELECT ID FROM FARMER WHERE FARMER_ID=";
				sb.append(farmerIdQuery + "'" + farmerId + "'),'");
				sb.append(farmDetailInfoIdSeq + "');\n");

				farmDetailInfoIdSeq++;
				i++;

			}

			sb.append("UPDATE FARM_ID_SEQ SET WEB_SEQ=");
			sb.append(farmIdSeq - 1);
			sb.append(";\n");

			baos.write(sb.toString().getBytes());
			myOutput.write(baos.toByteArray());
			System.out.println("------ Query Generated Successfully-------");

		} catch (Exception e) {
			System.out.println("Exception:" + e);
			System.out.println("----------ROW_NO:" + i + "-----------");
			e.printStackTrace();
		}
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

	public static StringBuilder generateFarmQuery(String filePath) throws IOException, SQLException {

		// String fileName = "E:\\Masterdata\\NEW\\M4\\Farmer.xls";
		FileInputStream myInput = new FileInputStream(filePath);
		String outputPath = MysqlUtility.getOutPutPath(filePath);
		/*
		 * FileOutputStream myOutput = new FileOutputStream(
		 * "D:\\santhosh-Aditi-pro-doc\\New_folder\\New\\2_FarmInsertQuery_" +
		 * fileNameDateFormat.format(new Date()) + ".sql");
		 */

		FileOutputStream myOutput = new FileOutputStream(
				outputPath + "\\2_FarmInsertQuery_" + fileNameDateFormat.format(new Date()) + ".sql");

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
		HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
		HSSFSheet mySheet = myWorkBook.getSheetAt(1);

		StringBuilder sb = new StringBuilder();
		String initialQuery = "INSERT INTO FARM (FARM_CODE,FARM_NAME,FARMER_ID,FARM_DETAILED_INFO_ID) VALUES(";
		// String farmerDetailQuery = "INSERT INTO FARM_DETAILED_INFO
		// (ID,FARM_OWNED,FARM_IRRIGATION,IRRIGATION_SOURCE,IRRIGATION_METHOD,SOIL_TYPE,SOIL_FERTILITY,ICS_STATUS)
		// VALUES(";

		int i = 1;
		int rowCount = mySheet.getLastRowNum();
		System.out.println("*******ROW COUNT***********" + rowCount);
		int farmIdSeq = MysqlUtility.getFarmIdSeq();
		int farmDetailInfoIdSeq = MysqlUtility.getFarmDetailedInfoIdSeq();
		int farmerSeq = MysqlUtility.getFarmerIdSeq();

		Map<String, String> farmMap = new HashMap<String, String>();

		try {

			while (i <= rowCount) {
				HSSFRow myRow = mySheet.getRow(i);

				String farmId = getExact(String.valueOf(farmIdSeq++), 6);
				String farmName = myRow.getCell(0).getStringCellValue().trim();
				String farmerCode = myRow.getCell(1).getStringCellValue().trim();
				// String farmerId =
				// myRow.getCell(1).getStringCellValue().trim();
				// String farmerId = getExact(String.valueOf(farmerSeq++), 6);
				String farmerID = "(SELECT ID FROM FARMER WHERE FARMER_CODE='" + farmerCode + "'),'";
				sb.append(initialQuery);
				sb.append("\"" + farmId + "\"");
				sb.append(",\"");
				sb.append(farmName);
				sb.append("\",");
				// Use Farmer ID in Excel Sheet - After run the Farmer Script
				// String farmerIdQuery="(SELECT ID FROM FARMER WHERE
				// FARMER_ID=";
				// sb.append(farmerIdQuery+"'"+farmerId + "'),'");
				sb.append(farmerID);
				sb.append(farmDetailInfoIdSeq + "');\n");

				farmDetailInfoIdSeq++;
				i++;

			}

			sb.append("UPDATE FARM_ID_SEQ SET WEB_SEQ=");
			sb.append(farmIdSeq - 1);
			sb.append(";\n");

			baos.write(sb.toString().getBytes());
			myOutput.write(baos.toByteArray());
			System.out.println("------Farm Query Generated Successfully-------");
			// FarmDetailInfoQueryMigration.generateFarmDetailQuery(filePath);
		} catch (Exception e) {
			sb = null;
			System.out.println("Exception:" + e);
			System.out.println("----------ROW_NO:" + i + "-----------");
			e.printStackTrace();
		}

		return sb;

	}
}
