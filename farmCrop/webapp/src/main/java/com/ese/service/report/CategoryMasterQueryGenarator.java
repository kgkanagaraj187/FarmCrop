package com.ese.service.report;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.sourcetrace.eses.util.ObjectUtil;

public class CategoryMasterQueryGenarator {
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("dd-MM-yyyy");
	  public static void main(String args[]) throws IOException, SQLException {
	        
	        System.out.println("Enter File Path");
	   
	        String fileName=MysqlUtility.getFilePath()+"\\Category_Master.xls";
	        FileInputStream myInput = new FileInputStream(fileName);
	        FileOutputStream myOutput = new FileOutputStream(
	                "D:\\santhosh-Aditi-pro-doc\\New_folder\\New\\Category_Master_Query_"
	                + fileNameDateFormat.format(new Date()) + ".sql");
	      
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();

	        POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
	        HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
	        HSSFSheet mySheet = myWorkBook.getSheetAt(0);

	        StringBuilder sb = new StringBuilder();
	        String initialQuery = "INSERT INTO sub_category(ID,CODE,NAME,BRANCH_ID) VALUES(NULL,";
	        int i = 3;  
	        int rowCount = mySheet.getLastRowNum();
	        System.out.println("*******ROW COUNT***********"+rowCount);
	         int noOfLines=0;
	         Map checkName=new LinkedHashMap();
	        try {
	          
	            while (i <= rowCount) {

	                HSSFRow myRow = mySheet.getRow(i);
	               
	                String code =!ObjectUtil.isEmpty(myRow.getCell(0))?myRow.getCell(0).getStringCellValue().trim():"";
	                String name= null;
		            name =!ObjectUtil.isEmpty(myRow.getCell(1))?myRow.getCell(1).getStringCellValue().trim():"";
		              
	                if(code!=""){
	                	if(!checkName.containsKey(name)){
	                	checkName.put(name, name);
	               sb.append(initialQuery);
	                sb.append("'"+code+"',");
	                sb.append("'"+name+"',");
	                String branchId="Lalteer";
	                sb.append("'"+branchId+"'");
	                sb.append(");");
	                sb.append("\n");
	                noOfLines++;
	                	}
	                }
	           i++;
	            
	           
	            }
	            baos.write(sb.toString().getBytes());
	            myOutput.write(baos.toByteArray());
	            System.out.println("------Category Master Generated Successfully total Rows="+noOfLines+"-------");
	        } catch (Exception e) {
	            System.out.println("Exception:" + e);
	            System.out.println("----------ROW_NO:" + i + "-----------");
	            e.printStackTrace();
	        }

	    
	    }
}
